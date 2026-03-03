# Modul 01: Komma igång med LangChain4j

## Innehållsförteckning

- [Videogenomgång](../../../01-introduction)
- [Vad Du Kommer Lära Dig](../../../01-introduction)
- [Förutsättningar](../../../01-introduction)
- [Förstå kärnproblemet](../../../01-introduction)
- [Förstå tokens](../../../01-introduction)
- [Hur minne fungerar](../../../01-introduction)
- [Hur detta använder LangChain4j](../../../01-introduction)
- [Distribuera Azure OpenAI-infrastruktur](../../../01-introduction)
- [Kör applikationen lokalt](../../../01-introduction)
- [Använda applikationen](../../../01-introduction)
  - [Stateless Chat (vänsterpanel)](../../../01-introduction)
  - [Stateful Chat (högerpanel)](../../../01-introduction)
- [Nästa steg](../../../01-introduction)

## Videogenomgång

Titta på denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Vad Du Kommer Lära Dig

I snabbstarten använde du GitHub Models för att skicka prompts, anropa verktyg, bygga en RAG-pipeline och testa skyddsbarriärer. Dessa demo visade vad som är möjligt – nu byter vi till Azure OpenAI och GPT-5.2 och börjar bygga produktionslika applikationer. Denna modul fokuserar på konversationell AI som kommer ihåg kontext och bevarar tillstånd – de koncept som snabbstartsdemona använde i bakgrunden men inte förklarade.

Vi använder Azure OpenAI:s GPT-5.2 genom hela denna guide eftersom dess avancerade resonemangsförmåga gör beteendet hos olika mönster tydligare. När du lägger till minne kommer du tydligt att se skillnaden. Detta gör det enklare att förstå vad varje komponent bidrar med till din applikation.

Du kommer att bygga en applikation som demonstrerar båda mönstren:

**Stateless Chat** – Varje förfrågan är oberoende. Modellen har inget minne av tidigare meddelanden. Detta är mönstret du använde i snabbstarten.

**Stateful Conversation** – Varje förfrågan inkluderar konversationshistorik. Modellen bevarar kontext över flera vändningar. Detta är vad produktionsapplikationer kräver.

## Förutsättningar

- Azure-prenumeration med tillgång till Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Obs:** Java, Maven, Azure CLI och Azure Developer CLI (azd) är förinstallerade i den tillhandahållna devcontainern.

> **Obs:** Denna modul använder GPT-5.2 på Azure OpenAI. Utplaceringen konfigureras automatiskt via `azd up` – ändra inte modellnamnet i koden.

## Förstå kärnproblemet

Språkmodeller är stateless. Varje API-anrop är oberoende. Om du skickar "Mitt namn är John" och sedan frågar "Vad heter jag?", har modellen ingen aning om att du just presenterade dig. Den behandlar varje förfrågan som om det vore din första konversation någonsin.

Det fungerar för enkla frågor och svar men är värdelöst för riktiga applikationer. Kundtjänstrobotar måste komma ihåg vad du har sagt. Personliga assistenter behöver kontext. Alla flervändskonversationer kräver minne.

Följande diagram kontrasterar de två tillvägagångssätten – till vänster, ett stateless-anrop som glömmer ditt namn; till höger, ett stateful-anrop som stöds av ChatMemory som kommer ihåg det.

<img src="../../../translated_images/sv/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Skillnaden mellan stateless (oberoende anrop) och stateful (konstextmedvetna) konversationer*

## Förstå tokens

Innan vi dyker in i konversationer är det viktigt att förstå tokens – de grundläggande textenheterna som språkmodeller bearbetar:

<img src="../../../translated_images/sv/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exempel på hur text delas upp i tokens – "I love AI!" blir 4 separata bearbetningsenheter*

Tokens är hur AI-modeller mäter och bearbetar text. Ord, skiljetecken och till och med mellanslag kan vara tokens. Din modell har en gräns för hur många tokens den kan bearbeta åt gången (400 000 för GPT-5.2, med upp till 272 000 inmatningstokens och 128 000 utmatningstokens). Att förstå tokens hjälper dig att hantera konversationens längd och kostnader.

## Hur minne fungerar

Chattminne löser det stateless-problemet genom att bevara konversationshistorik. Innan din förfrågan skickas till modellen lägger ramverket till relevanta tidigare meddelanden. När du frågar "Vad heter jag?" skickar systemet faktiskt hela konversationshistoriken, vilket gör det möjligt för modellen att se att du tidigare sa "Mitt namn är John."

LangChain4j tillhandahåller minnesimplementationer som hanterar detta automatiskt. Du väljer hur många meddelanden som ska behållas och ramverket hanterar kontextfönstret. Diagrammet nedan visar hur MessageWindowChatMemory upprätthåller ett rullande fönster av nyliga meddelanden.

<img src="../../../translated_images/sv/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory upprätthåller ett rullande fönster av nyliga meddelanden, som automatiskt kastar äldre*

## Hur detta använder LangChain4j

Denna modul utökar snabbstarten genom att integrera Spring Boot och lägga till konversationsminne. Så här passar bitarna ihop:

**Beroenden** – Lägg till två LangChain4j-bibliotek:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chattmodell** – Konfigurera Azure OpenAI som en Spring-bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Buildern läser inloggningsuppgifter från miljövariabler som sätts av `azd up`. Att sätta `baseUrl` till din Azure-endpoint gör att OpenAI-klienten fungerar med Azure OpenAI.

**Konversationsminne** – Spåra chattens historik med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Skapa minnet med `withMaxMessages(10)` för att behålla de sista 10 meddelandena. Lägg till användar- och AI-meddelanden med typade wrappers: `UserMessage.from(text)` och `AiMessage.from(text)`. Hämta historik med `memory.messages()` och skicka den till modellen. Tjänsten lagrar separata minnesinstanser per konversations-ID, vilket tillåter flera användare att chatta samtidigt.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) och fråga:
> - "Hur bestämmer MessageWindowChatMemory vilka meddelanden som ska tas bort när fönstret är fullt?"
> - "Kan jag implementera eget minneslagring med en databas istället för i minnet?"
> - "Hur skulle jag lägga till sammanfattning för att komprimera gammal konversationshistorik?"

Stateless chatt-endpointen hoppar över minne helt – bara `chatModel.chat(prompt)` som i snabbstarten. Stateful-endpointen lägger till meddelanden i minnet, hämtar historik och inkluderar den kontexten med varje förfrågan. Samma modellkonfiguration, olika mönster.

## Distribuera Azure OpenAI-infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Välj prenumeration och plats (eastus2 rekommenderas)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Välj prenumeration och plats (eastus2 rekommenderas)
```

> **Obs:** Om du får ett timeout-fel (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kör helt enkelt `azd up` igen. Azure-resurser kan fortfarande sättas upp i bakgrunden, och att försöka igen låter distributionen slutföras när resurserna går in i ett slutligt tillstånd.

Detta kommer att:
1. Distribuera Azure OpenAI-resurs med GPT-5.2 och text-embedding-3-small modeller
2. Automatiskt generera `.env`-fil i projektets rot med inloggningsuppgifter
3. Sätta upp alla nödvändiga miljövariabler

**Har du distributionsproblem?** Se [Infrastructure README](infra/README.md) för detaljerad felsökning inklusive problem med subdomännamn, manuella steg för Azure Portal-distribution och modellkonfigurationsråd.

**Verifiera att distributionen lyckades:**

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

> **Obs:** Kommandot `azd up` genererar automatiskt `.env`-filen. Om du behöver uppdatera den senare kan du redigera `.env`-filen manuellt eller generera om den genom att köra:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Kör applikationen lokalt

**Verifiera distribution:**

Säkerställ att `.env`-filen finns i rotkatalogen med Azure-uppgifter. Kör detta från modulkatalogen (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationerna:**

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Devcontainern inkluderar Spring Boot Dashboard-tillägget som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet på vänster sida av VS Code (titta efter Spring Boot-ikonen).

I Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i workspace
- Starta/stoppa applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka helt enkelt på play-knappen bredvid "introduction" för att starta denna modul, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — starta, stoppa och övervaka alla moduler från en plats*

**Alternativ 2: Använd shell-script**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotkatalogen
.\start-all.ps1
```

Eller starta endast denna modul:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Båda skripten laddar automatiskt miljövariabler från root `.env`-fil och bygger JAR-filerna om de inte finns.

> **Obs:** Om du föredrar att bygga alla moduler manuellt innan start:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Öppna http://localhost:8080 i din webbläsare.

**För att stoppa:**

**Bash:**
```bash
./stop.sh  # Endast denna modul
# Eller
cd .. && ./stop-all.sh  # Alla moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Endast denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Använda applikationen

Applikationen erbjuder ett webbgränssnitt med två chattimplementationer sida vid sida.

<img src="../../../translated_images/sv/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som visar både Enkel Chatt (stateless) och Konversationell Chatt (stateful) alternativ*

### Stateless Chat (vänsterpanel)

Prova detta först. Säg "Mitt namn är John" och fråga direkt efter "Vad heter jag?" Modellen kommer inte ihåg eftersom varje meddelande är oberoende. Detta demonstrerar kärnproblemet med grundläggande språkmodellintegration – ingen konversationskontext.

<img src="../../../translated_images/sv/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI kommer inte ihåg ditt namn från föregående meddelande*

### Stateful Chat (högerpanel)

Prova nu samma sekvens här. Säg "Mitt namn är John" och sedan "Vad heter jag?" Den här gången minns den. Skillnaden är MessageWindowChatMemory – den bevarar konversationshistorik och inkluderar den med varje förfrågan. Så fungerar produktionsklar konversationell AI.

<img src="../../../translated_images/sv/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI minns ditt namn från tidigare i konversationen*

Båda panelerna använder samma GPT-5.2-modell. Den enda skillnaden är minnet. Detta visar tydligt vad minnet tillför till din applikation och varför det är nödvändigt för verkliga användningsfall.

## Nästa steg

**Nästa modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigering:** [← Föregående: Modul 00 - Snabbstart](../00-quick-start/README.md) | [Tillbaka till huvudsidan](../README.md) | [Nästa: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör man vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess ursprungsspråk ska betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->