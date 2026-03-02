# Modul 01: Komma igång med LangChain4j

## Innehållsförteckning

- [Video Genomgång](../../../01-introduction)
- [Vad du kommer lära dig](../../../01-introduction)
- [Förkunskaper](../../../01-introduction)
- [Förstå kärnproblemet](../../../01-introduction)
- [Förstå tokens](../../../01-introduction)
- [Hur minne fungerar](../../../01-introduction)
- [Hur detta använder LangChain4j](../../../01-introduction)
- [Distribuera Azure OpenAI-infrastruktur](../../../01-introduction)
- [Kör applikationen lokalt](../../../01-introduction)
- [Använda applikationen](../../../01-introduction)
  - [Stateless chat (vänster panel)](../../../01-introduction)
  - [Stateful chat (höger panel)](../../../01-introduction)
- [Nästa steg](../../../01-introduction)

## Video Genomgång

Titta på denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Komma igång med LangChain4j - Live Session" width="800"/></a>

## Vad du kommer lära dig

Om du genomförde snabbstarten såg du hur man skickar prompts och får svar. Det är grunden, men verkliga applikationer behöver mer. Denna modul lär dig hur du bygger konverserande AI som kommer ihåg kontext och håller tillstånd – skillnaden mellan en engångs-demo och en produktionsklar applikation.

Vi använder Azure OpenAI:s GPT-5.2 genom hela guiden eftersom dess avancerade resonemangsförmåga gör beteendet hos olika mönster tydligare. När du lägger till minne ser du tydligt skillnaden. Det gör det lättare att förstå vad varje komponent tillför din applikation.

Du kommer bygga en applikation som demonstrerar båda mönstren:

**Stateless Chat** – Varje förfrågan är oberoende. Modellen har inget minne av tidigare meddelanden. Detta är mönstret du använde i snabbstarten.

**Stateful Conversation** – Varje förfrågan inkluderar konversationshistorik. Modellen behåller kontext över flera turer. Detta är vad produktionsapplikationer kräver.

## Förkunskaper

- Azure-prenumeration med Azure OpenAI-åtkomst
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notera:** Java, Maven, Azure CLI och Azure Developer CLI (azd) är förinstallerade i den medföljande devcontainer.

> **Notera:** Denna modul använder GPT-5.2 på Azure OpenAI. Utrullningen konfigureras automatiskt via `azd up` – ändra inte modellnamnet i koden.

## Förstå kärnproblemet

Språkmodeller är stateless. Varje API-anrop är oberoende. Om du säger "Mitt namn är John" och sedan frågar "Vad heter jag?" har modellen ingen aning om att du just presenterade dig. Den behandlar varje förfrågan som om det vore den första konversationen du någonsin haft.

Detta är okej för enkla frågor och svar men oanvändbart för verkliga applikationer. Kundservicebotar måste komma ihåg vad du berättade för dem. Personliga assistenter behöver kontext. Alla flertur-konversationer kräver minne.

<img src="../../../translated_images/sv/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Skillnaden mellan stateless (oberoende anrop) och stateful (konstextmedvetna) konversationer*

## Förstå tokens

Innan man dyker in i konversationer är det viktigt att förstå tokens – de grundläggande textenheterna som språkmodeller bearbetar:

<img src="../../../translated_images/sv/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exempel på hur text delas upp i tokens – "I love AI!" blir 4 separata bearbetningsenheter*

Tokens är hur AI-modeller mäter och bearbetar text. Ord, skiljetecken och till och med mellanslag kan vara tokens. Din modell har en gräns för hur många tokens den kan bearbeta samtidigt (400 000 för GPT-5.2, med upp till 272 000 inmatningstokens och 128 000 utmatningstokens). Att förstå tokens hjälper dig att hantera konversationslängd och kostnader.

## Hur minne fungerar

Chattminne löser problemet med stateless genom att behålla konversationshistorik. Innan du skickar din förfrågan till modellen lägger ramverket till relevanta tidigare meddelanden. När du frågar "Vad heter jag?" skickar systemet faktiskt hela konversationshistoriken, vilket gör att modellen ser att du tidigare sagt "Mitt namn är John."

LangChain4j erbjuder minnesimplementationer som hanterar detta automatiskt. Du väljer hur många meddelanden som ska sparas och ramverket sköter kontextfönstret.

<img src="../../../translated_images/sv/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory behåller ett glidande fönster av senaste meddelanden och tar automatiskt bort gamla*

## Hur detta använder LangChain4j

Denna modul utökar snabbstarten genom att integrera Spring Boot och lägga till konversationsminne. Så här passar delarna ihop:

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

**Chatmodell** – Konfigurera Azure OpenAI som en Spring-bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Buildern läser inloggningsuppgifter från miljövariabler som sätts av `azd up`. Att ställa in `baseUrl` till din Azure-endpoint gör att OpenAI-klienten fungerar med Azure OpenAI.

**Konversationsminne** – Spåra chatthistorik med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Skapa minnet med `withMaxMessages(10)` för att behålla de senaste 10 meddelandena. Lägg till användar- och AI-meddelanden med typade wrappers: `UserMessage.from(text)` och `AiMessage.from(text)`. Hämta historik med `memory.messages()` och skicka det till modellen. Tjänsten lagrar separata minnesinstanser per konversations-ID, vilket möjliggör att flera användare kan chatta samtidigt.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) och fråga:
> - "Hur bestämmer MessageWindowChatMemory vilka meddelanden som ska tas bort när fönstret är fullt?"
> - "Kan jag implementera egen minneslagring med databaser istället för i minnet?"
> - "Hur skulle jag lägga till summering för att komprimera gammal konversationshistorik?"

Stateless chat-endpoint hoppar helt över minnet – bara `chatModel.chat(prompt)` som i snabbstarten. Stateful endpoint lägger till meddelanden till minnet, hämtar historik och inkluderar den kontexten vid varje förfrågan. Samma modellkonfiguration, olika mönster.

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

> **Notera:** Om du stöter på en timeout-error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kör bara `azd up` igen. Azure-resurser kan fortfarande provisioneras i bakgrunden, och att försöka igen låter distributionen slutföras när resurserna når ett terminalt tillstånd.

Detta kommer att:
1. Distribuera Azure OpenAI-resurs med GPT-5.2 och text-embedding-3-small modeller
2. Automatiskt generera `.env`-fil i projektets rot med inloggningsuppgifter
3. Konfigurera alla nödvändiga miljövariabler

**Problem med distributionen?** Se [Infrastruktur-README](infra/README.md) för detaljerad felsökning inklusive subdomännamnskonflikter, manuella steg via Azure Portal och modellkonfigurationsråd.

**Verifiera att distribution lyckades:**

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Notera:** Kommandot `azd up` genererar `.env`-filen automatiskt. Om du behöver uppdatera den senare kan du antingen redigera `.env` manuellt eller generera om den genom att köra:
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

Se till att `.env`-filen finns i rotmappen med Azure-uppgifter:

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationerna:**

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderat för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget som ger en visuell vy för att hantera alla Spring Boot-applikationer. Du hittar den i aktivitetsfältet till vänster i VS Code (letar efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stopp applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka bara på play-knappen bredvid "introduction" för att starta denna modul, eller starta alla moduler på en gång.

<img src="../../../translated_images/sv/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Använda shell-skript**

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

Eller starta bara denna modul:

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

Båda skripten laddar automatiskt miljövariabler från `.env`-filen i rotmappen och bygger JAR-filer om de inte finns.

> **Notera:** Om du vill bygga alla moduler manuellt innan du startar:
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

Applikationen erbjuder en webbgränssnitt med två chattimplementationer sida vid sida.

<img src="../../../translated_images/sv/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som visar både Enkel Chatt (stateless) och Konverserande Chatt (stateful)*

### Stateless Chat (vänster panel)

Testa detta först. Säg "Mitt namn är John" och fråga sedan omedelbart "Vad heter jag?" Modellen kommer inte ihåg eftersom varje meddelande är oberoende. Detta demonstrerar huvudproblemet med grundläggande språkmodellsintegration – ingen kontext i konversationen.

<img src="../../../translated_images/sv/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI kommer inte ihåg ditt namn från föregående meddelande*

### Stateful Chat (höger panel)

Nu testa samma sekvens här. Säg "Mitt namn är John" och sedan "Vad heter jag?" Denna gång kommer den ihåg. Skillnaden är MessageWindowChatMemory – den behåller konversationshistorik och inkluderar den i varje förfrågan. Så här fungerar produktionsklar konverserande AI.

<img src="../../../translated_images/sv/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI kommer ihåg ditt namn från tidigare i konversationen*

Båda paneler använder samma GPT-5.2-modell. Den enda skillnaden är minnet. Det gör det tydligt vad minnet tillför din applikation och varför det är avgörande för verkliga användningsfall.

## Nästa steg

**Nästa modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Föregående: Modul 00 - Snabbstart](../00-quick-start/README.md) | [Tillbaka till huvudmenyn](../README.md) | [Nästa: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen var medveten om att automatiska översättningar kan innehålla fel eller brister. Originaldokumentet på dess modersmål ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->