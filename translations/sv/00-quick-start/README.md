# Module 00: Kom igång snabbt

## Innehållsförteckning

- [Introduktion](../../../00-quick-start)
- [Vad är LangChain4j?](../../../00-quick-start)
- [LangChain4j Beroenden](../../../00-quick-start)
- [Förutsättningar](../../../00-quick-start)
- [Installation](../../../00-quick-start)
  - [1. Skaffa ditt GitHub-token](../../../00-quick-start)
  - [2. Sätt ditt token](../../../00-quick-start)
- [Kör exemplen](../../../00-quick-start)
  - [1. Grundläggande chatt](../../../00-quick-start)
  - [2. Promptmönster](../../../00-quick-start)
  - [3. Funktionsanrop](../../../00-quick-start)
  - [4. Dokument Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Ansvarsfull AI](../../../00-quick-start)
- [Vad varje exempel visar](../../../00-quick-start)
- [Nästa steg](../../../00-quick-start)
- [Felsökning](../../../00-quick-start)

## Introduktion

Denna snabbstart är till för att du snabbt ska komma igång med LangChain4j. Den täcker det absoluta grundläggande för att bygga AI-applikationer med LangChain4j och GitHub Models. I nästa moduler kommer du att använda Azure OpenAI med LangChain4j för att bygga mer avancerade applikationer.

## Vad är LangChain4j?

LangChain4j är ett Java-bibliotek som förenklar byggandet av AI-drivna applikationer. Istället för att hantera HTTP-klienter och JSON-parsing arbetar du med rena Java-API:er. 

"Chain" i LangChain syftar på att länka ihop flera komponenter – du kan kedja en prompt till en modell till en parser, eller koppla samman flera AI-anrop där ett utdata matas in som nästa indata. Denna snabbstart fokuserar på grunderna innan vi utforskar mer komplexa kedjor.

<img src="../../../translated_images/sv/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kedja ihop komponenter i LangChain4j – byggblock kopplas ihop för att skapa kraftfulla AI-arbetsflöden*

Vi använder tre kärnkomponenter:

**ChatModel** – Gränssnittet för AI-modellinteraktioner. Anropa `model.chat("prompt")` och få en svarsträng. Vi använder `OpenAiOfficialChatModel` som fungerar med OpenAI-kompatibla ändpunkter som GitHub Models.

**AiServices** – Skapar typsäkra AI-tjänstegränssnitt. Definiera metoder, annotera dem med `@Tool` och LangChain4j tar hand om orkestreringen. AI:n anropar automatiskt dina Java-metoder vid behov.

**MessageWindowChatMemory** – Underhåller konversationshistorik. Utan detta är varje förfrågan oberoende. Med detta kommer AI ihåg tidigare meddelanden och upprätthåller kontext över flera turer.

<img src="../../../translated_images/sv/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-arkitektur – kärnkomponenter som samarbetar för att driva dina AI-applikationer*

## LangChain4j Beroenden

Denna snabbstart använder tre Maven-beroenden i [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Modulen `langchain4j-open-ai-official` tillhandahåller klassen `OpenAiOfficialChatModel` som ansluter till OpenAI-kompatibla API:er. GitHub Models använder samma API-format, så ingen särskild adapter behövs – peka bara bas-URL:en till `https://models.github.ai/inference`.

Modulen `langchain4j-easy-rag` erbjuder automatisk dokumentuppdelning, inbäddning och hämtning så att du kan bygga RAG-applikationer utan att manuellt konfigurera varje steg.

## Förutsättningar

**Använder du Dev Container?** Java och Maven är redan installerade. Du behöver bara ett GitHub Personal Access Token.

**Lokal utveckling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruktioner nedan)

> **Observera:** Denna modul använder `gpt-4.1-nano` från GitHub Models. Ändra inte modellnamnet i koden – det är konfigurerat för att fungera med GitHubs tillgängliga modeller.

## Installation

### 1. Skaffa ditt GitHub-token

1. Gå till [GitHub Inställningar → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klicka på "Generate new token"
3. Ange ett beskrivande namn (t.ex. "LangChain4j Demo")
4. Ställ in utgångsdatum (7 dagar rekommenderas)
5. Under "Account permissions", hitta "Models" och sätt till "Read-only"
6. Klicka på "Generate token"
7. Kopiera och spara ditt token – du får inte se det igen

### 2. Sätt ditt token

**Alternativ 1: Använd VS Code (Rekommenderat)**

Om du använder VS Code, lägg till ditt token i `.env`-filen i projektets rot:

Om `.env`-filen inte finns, kopiera `.env.example` till `.env` eller skapa en ny `.env`-fil i projektets rot.

**Exempel på `.env`-fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Därefter kan du helt enkelt högerklicka på valfri demo-fil (t.ex. `BasicChatDemo.java`) i Utforskaren och välja **"Run Java"** eller använda startkonfigurationerna från panelen för Kör och Debugga.

**Alternativ 2: Använd terminalen**

Sätt token som en miljövariabel:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Kör exemplen

**Med VS Code:** Högerklicka på valfri demo-fil i Utforskaren och välj **"Run Java"**, eller använd startkonfigurationerna från panelen Kör och Debugga (se till att du först lagt till ditt token i `.env`-filen).

**Med Maven:** Alternativt kan du köra från kommandoraden:

### 1. Grundläggande chatt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Promptmönster

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Visar zero-shot, few-shot, chain-of-thought och rollbaserad prompting.

### 3. Funktionsanrop

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI anropar automatiskt dina Java-metoder när det behövs.

### 4. Dokument Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Ställ frågor om dina dokument med Easy RAG med automatisk inbäddning och hämtning.

### 5. Ansvarsfull AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Se hur AI-säkerhetsfilter blockerar skadligt innehåll.

## Vad varje exempel visar

**Grundläggande chatt** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Börja här för att se LangChain4j i dess enklaste form. Du skapar en `OpenAiOfficialChatModel`, skickar en prompt med `.chat()` och får tillbaka ett svar. Detta demonstrerar grunden: hur man initialiserar modeller med anpassade ändpunkter och API-nycklar. När du förstår detta mönster bygger allt annat på det.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) och fråga:
> - "Hur skulle jag byta från GitHub Models till Azure OpenAI i denna kod?"
> - "Vilka andra parametrar kan jag konfigurera i OpenAiOfficialChatModel.builder()?"
> - "Hur lägger jag till strömningssvar istället för att vänta på komplett svar?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu när du vet hur du pratar med en modell, låt oss utforska vad du säger till den. Denna demo använder samma modellinställning men visar fem olika promptmönster. Prova zero-shot-promptar för direkta instruktioner, few-shot-promptar som lär sig från exempel, chain-of-thought-promptar som avslöjar resonemangssteget, och rollbaserade promptar som sätter kontext. Du kommer att se hur samma modell ger dramatiskt olika resultat beroende på hur du formulerar din förfrågan.

Demon visar också promptmallar, som är ett kraftfullt sätt att skapa återanvändbara promptar med variabler.
Exemplet nedan visar en prompt med LangChain4j `PromptTemplate` för att fylla i variabler. AI:n svarar baserat på angiven destination och aktivitet.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) och fråga:
> - "Vad är skillnaden mellan zero-shot och few-shot prompting, och när ska jag använda respektive?"
> - "Hur påverkar temperaturparametern modellens svar?"
> - "Vilka tekniker finns för att förhindra promptinjektionsattacker i produktion?"
> - "Hur kan jag skapa återanvändbara PromptTemplate-objekt för vanliga mönster?"

**Verktygsintegration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Här blir LangChain4j kraftfullt. Du använder `AiServices` för att skapa en AI-assistent som kan anropa dina Java-metoder. Bara annotera metoder med `@Tool("beskrivning")` och LangChain4j sköter resten – AI:n bestämmer automatiskt när varje verktyg ska användas baserat på användarens fråga. Detta demonstrerar funktionsanrop, en nyckelmetod för att bygga AI som kan utföra handlingar, inte bara svara på frågor.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) och fråga:
> - "Hur fungerar @Tool-annoteringen och vad gör LangChain4j med den bakom kulisserna?"
> - "Kan AI anropa flera verktyg i följd för att lösa komplexa problem?"
> - "Vad händer om ett verktyg kastar ett undantag – hur bör jag hantera fel?"
> - "Hur skulle jag integrera ett riktigt API istället för detta kalkyleringsexempel?"

**Dokument Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Här ser du RAG (retrieval-augmented generation) med LangChain4js "Easy RAG"-metod. Dokument laddas, delas automatiskt upp, bäddas in i ett minneslager, och en innehållshämtare levererar relevanta delar till AI:n vid frågetillfället. AI:n svarar baserat på dina dokument, inte dess allmänna kunskap.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) och fråga:
> - "Hur förhindrar RAG AI-hallucinationer jämfört med att använda modellens träningsdata?"
> - "Vad är skillnaden mellan denna enkla metod och en anpassad RAG-pipeline?"
> - "Hur skulle jag skala detta för att hantera flera dokument eller större kunskapsbaser?"

**Ansvarsfull AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bygg AI-säkerhet med försvar i flera lager. Denna demo visar två skyddsskikt som samarbetar:

**Del 1: LangChain4j Input Guardrails** – Blockera farliga promptar innan de når LLM. Skapa anpassade skydd som kontrollerar förbjudna nyckelord eller mönster. Dessa körs i din kod, så de är snabba och gratis.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Del 2: Provider Safety Filters** – GitHub Models har inbyggda filter som fångar upp det dina skydd kanske missar. Du kommer att se hårda blockeringar (HTTP 400-fel) för allvarliga överträdelser och mjuka avslag där AI:n artigt tackar nej.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) och fråga:
> - "Vad är InputGuardrail och hur skapar jag en egen?"
> - "Vad är skillnaden mellan ett hårt block och ett mjukt avslag?"
> - "Varför använda både guardrails och leverantörsfilter tillsammans?"

## Nästa steg

**Nästa modul:** [01-introduction - Kom igång med LangChain4j och gpt-5 på Azure](../01-introduction/README.md)

---

**Navigering:** [← Tillbaka till huvudmenyn](../README.md) | [Nästa: Modul 01 - Introduktion →](../01-introduction/README.md)

---

## Felsökning

### Första Maven-byggning

**Problem:** Initial `mvn clean compile` eller `mvn package` tar lång tid (10–15 minuter)

**Orsak:** Maven behöver ladda ner alla projektberoenden (Spring Boot, LangChain4j-bibliotek, Azure SDK:er etc.) vid första byggningen.

**Lösning:** Detta är normalt. Efterföljande byggen går mycket snabbare då beroenden är cachade lokalt. Nedladdningstiden beror på din nätverkshastighet.

### PowerShell Maven-kommandosyntax

**Problem:** Maven-kommandon misslyckas med felmeddelandet `Unknown lifecycle phase ".mainClass=..."`
**Orsak**: PowerShell tolkar `=` som en variabeltilldelningsoperator, vilket bryter Maven-egenskapsyntaxen

**Lösning**: Använd stopparsningsoperatorn `--%` före Maven-kommandot:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorn `--%` talar om för PowerShell att skicka alla återstående argument bokstavligt till Maven utan tolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar visar skräptecken (t.ex. `????` eller `â??`) istället för emojis i PowerShell

**Orsak**: PowerShells standardkodning stödjer inte UTF-8-emojis

**Lösning**: Kör detta kommando innan du kör Java-applikationer:
```cmd
chcp 65001
```

Detta tvingar UTF-8-kodning i terminalen. Alternativt kan du använda Windows Terminal som har bättre Unicode-stöd.

### Felsökning av API-anrop

**Problem**: Autentiseringsfel, hastighetsbegränsningar eller oväntade svar från AI-modellen

**Lösning**: Exemplen inkluderar `.logRequests(true)` och `.logResponses(true)` för att visa API-anrop i konsolen. Detta hjälper till att felsöka autentiseringsfel, hastighetsbegränsningar eller oväntade svar. Ta bort dessa flaggor i produktion för att minska loggbrus.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess originalspråk ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->