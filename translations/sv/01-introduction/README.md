# Modul 01: Komma igång med LangChain4j

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../01-introduction)
- [Förutsättningar](../../../01-introduction)
- [Förstå det grundläggande problemet](../../../01-introduction)
- [Förstå tokens](../../../01-introduction)
- [Hur minnet fungerar](../../../01-introduction)
- [Hur detta använder LangChain4j](../../../01-introduction)
- [Distribuera Azure OpenAI-infrastruktur](../../../01-introduction)
- [Köra applikationen lokalt](../../../01-introduction)
- [Använda applikationen](../../../01-introduction)
  - [Stateless Chat (vänster panel)](../../../01-introduction)
  - [Stateful Chat (höger panel)](../../../01-introduction)
- [Nästa steg](../../../01-introduction)

## Vad du kommer att lära dig

Om du gjorde snabbstarten såg du hur man skickar prompts och får svar. Det är grunden, men riktiga applikationer behöver mer. Denna modul lär dig hur du bygger konversations-AI som kommer ihåg kontext och bibehåller tillstånd – skillnaden mellan en engångs-demo och en produktionsredo applikation.

Vi kommer använda Azure OpenAI:s GPT-5.2 genom hela guiden eftersom dess avancerade resonemangsförmågor gör beteendet hos olika mönster tydligare. När du lägger till minne ser du skillnaden klart och tydligt. Detta gör det lättare att förstå vad varje komponent tillför din applikation.

Du kommer bygga en applikation som demonstrerar båda mönstren:

**Stateless Chat** – Varje förfrågan är oberoende. Modellen har inget minne av tidigare meddelanden. Detta är mönstret som användes i snabbstarten.

**Stateful Conversation** – Varje förfrågan inkluderar konversationshistorik. Modellen behåller kontext över flera turer. Detta är vad produktionsapplikationer kräver.

## Förutsättningar

- Azure-prenumeration med tillgång till Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notera:** Java, Maven, Azure CLI och Azure Developer CLI (azd) är förinstallerade i den tillhandahållna utvecklingscontainern.

> **Notera:** Denna modul använder GPT-5.2 på Azure OpenAI. Distribueringen konfigureras automatiskt via `azd up` – ändra inte modellnamnet i koden.

## Förstå det grundläggande problemet

Språkmodeller är stateless. Varje API-anrop är oberoende. Om du skriver "Mitt namn är John" och sedan frågar "Vad heter jag?", har modellen ingen aning om att du precis presenterade dig. Den behandlar varje förfrågan som om det är den första konversationen du någonsin haft.

Det fungerar för enkla frågor och svar men är värdelöst för riktiga applikationer. Kundservicebotar måste komma ihåg vad du sagt. Personliga assistenter behöver kontext. Alla konversationer med flera turer kräver minne.

<img src="../../../translated_images/sv/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Skillnaden mellan stateless (oberoende anrop) och stateful (konstextmedvetna) konversationer*

## Förstå tokens

Innan vi dyker in i konversationer är det viktigt att förstå tokens – de grundläggande textenheterna som språkmodeller bearbetar:

<img src="../../../translated_images/sv/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exempel på hur text bryts ner i tokens – "I love AI!" blir 4 separata bearbetningsenheter*

Tokens är hur AI-modeller mäter och bearbetar text. Ord, skiljetecken och till och med mellanslag kan vara tokens. Din modell har en gräns för hur många tokens den kan bearbeta samtidigt (400 000 för GPT-5.2, med upp till 272 000 inmatnings- och 128 000 utmatningstokens). Att förstå tokens hjälper dig att hantera konversationslängd och kostnader.

## Hur minnet fungerar

Chatminne löser det stateless problemet genom att bibehålla konversationshistorik. Innan din förfrågan skickas till modellen förser ramverket den med relevanta tidigare meddelanden. När du frågar "Vad heter jag?" skickar systemet hela konversationshistoriken, vilket gör att modellen kan se att du tidigare sagt "Mitt namn är John."

LangChain4j tillhandahåller minnesimplementationer som hanterar detta automatiskt. Du väljer hur många meddelanden som ska behållas och ramverket hanterar kontextfönstret.

<img src="../../../translated_images/sv/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory bibehåller ett glidande fönster med senaste meddelandena och släpper automatiskt äldre*

## Hur detta använder LangChain4j

Denna modul bygger vidare på snabbstarten genom att integrera Spring Boot och lägga till samtalsminne. Så här passar delarna ihop:

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

**Chattmodell** – Konfigurera Azure OpenAI som en Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Buildern läser autentiseringsuppgifter från miljövariabler som sätts av `azd up`. Att ange `baseUrl` till din Azure-endpoint gör att OpenAI-klienten fungerar med Azure OpenAI.

**Konversationsminne** – Spåra chattens historik med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Skapa minnet med `withMaxMessages(10)` för att behålla de sista 10 meddelandena. Lägg till användar- och AI-meddelanden med typade wrappers: `UserMessage.from(text)` och `AiMessage.from(text)`. Hämta historik med `memory.messages()` och skicka det till modellen. Tjänsten lagrar separata minnesinstanser per konversations-ID, vilket tillåter flera användare att chatta samtidigt.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) och fråga:
> - "Hur bestämmer MessageWindowChatMemory vilka meddelanden som ska släppas när fönstret är fullt?"
> - "Kan jag implementera egen minneslagring med en databas istället för in-memory?"
> - "Hur skulle jag lägga till summering för att komprimera gammal konversationshistorik?"

Den stateless chatt-endpointen hoppar helt över minnet – endast `chatModel.chat(prompt)` som i snabbstarten. Den stateful endpointen lägger till meddelanden i minnet, hämtar historik och inkluderar den kontexten med varje förfrågan. Samma modellkonfiguration, olika mönster.

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

> **Notera:** Om du får ett timeout-fel (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kör helt enkelt `azd up` igen. Azure-resurser kan fortfarande provisioneras i bakgrunden, och ett nytt försök låter distributionen slutföras när resurserna når ett terminalt tillstånd.

Detta kommer att:
1. Distribuera Azure OpenAI-resurs med GPT-5.2 och text-embedding-3-small-modeller
2. Automatiskt generera `.env`-fil i projektroten med autentiseringsuppgifter
3. Ställa in alla nödvändiga miljövariabler

**Har du distributionsproblem?** Se [Infrastructure README](infra/README.md) för detaljerad felsökning inklusive subdomänkonflikter, manuella distributionssteg i Azure Portal samt vägledning om modellkonfiguration.

**Verifiera att distributionen lyckades:**

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Notera:** Kommandot `azd up` genererar automatiskt `.env`-filen. Om du behöver uppdatera den senare kan du antingen redigera `.env` manuellt eller generera om den genom att köra:
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

## Köra applikationen lokalt

**Verifiera distributionen:**

Se till att `.env`-filen finns i rotkatalogen med Azure-autentiseringsuppgifter:

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ska visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationerna:**

**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Utvecklingscontainern inkluderar Spring Boot Dashboard extension som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar den i Aktivitetsfältet till vänster i VS Code (se efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stoppa applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka bara på play-knappen bredvid "introduction" för att starta denna modul, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Använda shell-script**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotkatalog
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

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan start:
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

Applikationen erbjuder ett webbgränssnitt med två chattimplementeringar sida vid sida.

<img src="../../../translated_images/sv/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som visar både Simple Chat (stateless) och Conversational Chat (stateful) alternativ*

### Stateless Chat (vänster panel)

Testa detta först. Säg "Mitt namn är John" och fråga sedan direkt "Vad heter jag?" Modellen kommer inte ihåg eftersom varje meddelande är oberoende. Detta demonstrerar det grundläggande problemet med grundläggande språkmodellsintegration – ingen konversationskontext.

<img src="../../../translated_images/sv/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI kommer inte ihåg ditt namn från föregående meddelande*

### Stateful Chat (höger panel)

Testa nu samma sekvens här. Säg "Mitt namn är John" och sedan "Vad heter jag?" Denna gång kommer den ihåg. Skillnaden är MessageWindowChatMemory – det underhåller konversationshistoriken och inkluderar den vid varje förfrågan. Så fungerar produktionskonversations-AI.

<img src="../../../translated_images/sv/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI kommer ihåg ditt namn från tidigare i konversationen*

Båda panelerna använder samma GPT-5.2-modell. Den enda skillnaden är minnet. Det gör klart vad minnet tillför din applikation och varför det är avgörande för verkliga användningsfall.

## Nästa steg

**Nästa modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigering:** [← Föregående: Modul 00 - Snabbstart](../00-quick-start/README.md) | [Tillbaka till huvudmenyn](../README.md) | [Nästa: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål ska betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->