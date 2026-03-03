# Module 00: Kom igång

## Innehållsförteckning

- [Introduktion](../../../00-quick-start)
- [Vad är LangChain4j?](../../../00-quick-start)
- [LangChain4j beroenden](../../../00-quick-start)
- [Förutsättningar](../../../00-quick-start)
- [Installation](../../../00-quick-start)
  - [1. Skaffa din GitHub-token](../../../00-quick-start)
  - [2. Sätt din token](../../../00-quick-start)
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

Denna snabbstart är avsedd att få dig att komma igång med LangChain4j så snabbt som möjligt. Den täcker det absoluta grunderna för att bygga AI-applikationer med LangChain4j och GitHub Models. I nästa moduler går du över till Azure OpenAI och GPT-5.2 och fördjupar dig i varje koncept.

## Vad är LangChain4j?

LangChain4j är ett Java-bibliotek som förenklar att bygga AI-drivna applikationer. Istället för att hantera HTTP-klienter och JSON-parsing arbetar du med rena Java-API:er.

"Kedjan" i LangChain avser att kedja ihop flera komponenter – du kan kedja en prompt till en modell till en parser, eller kedja flera AI-anrop där en output matas in som nästa input. Denna snabbstart fokuserar på grunderna innan mer komplexa kedjor utforskas.

<img src="../../../translated_images/sv/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kedja ihop komponenter i LangChain4j – byggblock kopplas ihop för att skapa kraftfulla AI-arbetsflöden*

Vi använder tre kärnkomponenter:

**ChatModel** – Gränssnittet för AI-modellinteraktioner. Anropa `model.chat("prompt")` och få ett svar som sträng. Vi använder `OpenAiOfficialChatModel` som fungerar med OpenAI-kompatibla endpoints som GitHub Models.

**AiServices** – Skapar typ-säkra AI-tjänstegränssnitt. Definiera metoder, annotera med `@Tool`, och LangChain4j sköter orkestreringen. AI kallar automatiskt dina Java-metoder vid behov.

**MessageWindowChatMemory** – Underhåller konversationshistorik. Utan denna är varje anrop oberoende. Med den kommer AI ihåg tidigare meddelanden och bibehåller kontext över flera turer.

<img src="../../../translated_images/sv/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-arkitektur – kärnkomponenter som arbetar ihop för att driva dina AI-applikationer*

## LangChain4j beroenden

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

Modulen `langchain4j-open-ai-official` tillhandahåller klassen `OpenAiOfficialChatModel` som kopplar till OpenAI-kompatibla API:er. GitHub Models använder samma API-format, så ingen särskild adapter behövs – peka bara bas-URL:en till `https://models.github.ai/inference`.

Modulen `langchain4j-easy-rag` tillhandahåller automatisk dokumentuppdelning, inbäddning och hämtning så att du kan bygga RAG-applikationer utan manuell konfiguration av varje steg.

## Förutsättningar

**Använder du Dev Container?** Java och Maven är redan installerade. Du behöver bara en GitHub Personal Access Token.

**Lokal utveckling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruktioner nedan)

> **Notera:** Denna modul använder `gpt-4.1-nano` från GitHub Models. Ändra inte modellnamnet i koden – det är konfigurerat att fungera med GitHubs tillgängliga modeller.

## Installation

### 1. Skaffa din GitHub-token

1. Gå till [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klicka på "Generate new token"
3. Ange ett beskrivande namn (t.ex. "LangChain4j Demo")
4. Sätt utgångsdatum (7 dagar rekommenderas)
5. Under "Account permissions", hitta "Models" och sätt till "Read-only"
6. Klicka på "Generate token"
7. Kopiera och spara din token – du kommer inte se den igen

### 2. Sätt din token

**Alternativ 1: Använda VS Code (Rekommenderat)**

Om du använder VS Code, lägg till din token i `.env`-filen i projektets rot:

Om `.env`-filen inte finns, kopiera `.env.example` till `.env` eller skapa en ny `.env`-fil i projektroten.

**Exempel på `.env`-fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Därefter kan du högerklicka på valfri demo-fil (t.ex. `BasicChatDemo.java`) i Explorer och välja **"Run Java"** eller använda startkonfigurationerna från Run and Debug-panelen.

**Alternativ 2: Använd Terminal**

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

**Med VS Code:** Högerklicka helt enkelt på någon demofil i Explorer och välj **"Run Java"**, eller använd startkonfigurationer i Run and Debug-panelen (se till att du först lagt till din token i `.env`-filen).

**Med Maven:** Alternativt kan du köra via kommandoraden:

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

AI anropar automatiskt dina Java-metoder vid behov.

### 4. Dokument Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Ställ frågor om dina dokument med Easy RAG, med automatisk inbäddning och hämtning.

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

Börja här för att se LangChain4j i sin enklaste form. Du skapar en `OpenAiOfficialChatModel`, skickar en prompt med `.chat()` och får tillbaka ett svar. Detta visar grunden: hur man initierar modeller med egna endpoints och API-nycklar. När du förstår detta mönster bygger allt annat på det.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) och fråga:
> - "Hur byter jag från GitHub Models till Azure OpenAI i den här koden?"
> - "Vilka andra parametrar kan jag konfigurera i OpenAiOfficialChatModel.builder()?"
> - "Hur lägger jag till streaming-svar istället för att vänta på komplett svar?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu när du vet hur man pratar med en modell, låt oss utforska vad du säger till den. Den här demon använder samma modellinställning men visar fem olika promptmönster. Prova zero-shot-prompter för direkta instruktioner, few-shot-prompter som lär från exempel, chain-of-thought-prompter som visar resonemangssteg och rollbaserade promter som sätter kontext. Du ser hur samma modell ger dramatiskt olika resultat beroende på hur du formulerar din fråga.

Demon visar också promptmallar, ett kraftfullt sätt att skapa återanvändbara prompts med variabler.
Nedanstående exempel visar en prompt som använder LangChain4j:s `PromptTemplate` för att fylla i variabler. AI kommer svara baserat på angiven destination och aktivitet.

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

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) och fråga:
> - "Vad är skillnaden mellan zero-shot och few-shot prompting, och när bör jag använda vardera?"
> - "Hur påverkar temperatur-parametern modellens svar?"
> - "Vilka tekniker finns för att förhindra promptinjektionsattacker i produktion?"
> - "Hur kan jag skapa återanvändbara PromptTemplate-objekt för vanliga mönster?"

**Verktygsintegration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Här blir LangChain4j kraftfullt. Du använder `AiServices` för att skapa en AI-assistent som kan anropa dina Java-metoder. Annotera bara metoder med `@Tool("beskrivning")` och LangChain4j sköter resten – AI avgör automatiskt när varje verktyg ska användas baserat på användarens fråga. Detta demonstrerar funktionsanrop, en nyckelteknik för att bygga AI som kan utföra handlingar, inte bara svara på frågor.

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

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) och fråga:
> - "Hur fungerar @Tool-annoteringen och vad gör LangChain4j med den bakom kulisserna?"
> - "Kan AI använda flera verktyg i följd för att lösa komplexa problem?"
> - "Vad händer om ett verktyg kastar ett undantag – hur bör jag hantera fel?"
> - "Hur integrerar jag ett verkligt API istället för detta exempel med kalkylator?"

**Dokument Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Här ser du RAG (retrieval-augmented generation) med LangChain4j:s "Easy RAG"-metod. Dokument laddas, delas automatiskt upp och bäddas in i ett minneslager, sedan levererar en innehållsinnhämtare relevanta delar till AI vid frågetillfället. AI svarar baserat på dina dokument, inte dess generella kunskap.

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

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) och fråga:
> - "Hur förhindrar RAG AI-hallucinationer jämfört med att använda modellens träningsdata?"
> - "Vad är skillnaden mellan detta enkla tillvägagångssätt och en egen RAG-pipeline?"
> - "Hur kan jag skala detta för att hantera flera dokument eller större kunskapsbaser?"

**Ansvarsfull AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bygg AI-säkerhet med försvar i djupet. Denna demo visar två skyddslager som arbetar tillsammans:

**Del 1: LangChain4j Input Guardrails** – Blockera farliga prompts innan de når LLM. Skapa egna guardrails som kollar efter förbjudna nyckelord eller mönster. Dessa körs i din kod, så de är snabba och kostnadsfria.

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

**Del 2: Leverantörens säkerhetsfilter** – GitHub Models har inbyggda filter som fångar vad dina guardrails kan missa. Du ser hårda blockeringar (HTTP 400-fel) vid allvarliga överträdelser och mjuka avslag där AI artigt tackar nej.

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) och fråga:
> - "Vad är InputGuardrail och hur skapar jag egna?"
> - "Vad är skillnaden mellan hårt block och mjukt avslag?"
> - "Varför använda både guardrails och leverantörsfilter samtidigt?"

## Nästa steg

**Nästa modul:** [01-introduction - Kom igång med LangChain4j](../01-introduction/README.md)

---

**Navigering:** [← Tillbaka till huvudsidan](../README.md) | [Nästa: Modul 01 - Introduktion →](../01-introduction/README.md)

---

## Felsökning

### Första bygg med Maven

**Problem:** Initierande `mvn clean compile` eller `mvn package` tar lång tid (10-15 minuter)

**Orsak:** Maven behöver ladda ner alla projektberoenden (Spring Boot, LangChain4j-bibliotek, Azure SDKs, etc.) vid första byggningen.

**Lösning:** Detta är normalt. Efterföljande byggningar går mycket snabbare eftersom beroenden cachas lokalt. Nedladdningstiden beror på din nätverkshastighet.

### Syntax för Maven-kommando i PowerShell

**Problem:** Maven-kommandon misslyckas med felmeddelandet `Unknown lifecycle phase ".mainClass=..."`
**Orsak**: PowerShell tolkar `=` som en variabeltilldelningsoperator, vilket bryter Maven-egenskapssyntaxen

**Lösning**: Använd stopparsingoperatorn `--%` före Maven-kommandot:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorn `--%` talar om för PowerShell att vidarebefordra alla resterande argument bokstavligt till Maven utan tolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar visar skräptecken (t.ex. `????` eller `â??`) istället för emojis i PowerShell

**Orsak**: PowerShells standardkodning stödjer inte UTF-8-emojis

**Lösning**: Kör detta kommando innan du kör Java-applikationer:
```cmd
chcp 65001
```

Detta tvingar UTF-8-kodning i terminalen. Alternativt, använd Windows Terminal som har bättre Unicode-stöd.

### Felsökning av API-anrop

**Problem**: Autentiseringsfel, hastighetsbegränsningar eller oväntade svar från AI-modellen

**Lösning**: Exemplen inkluderar `.logRequests(true)` och `.logResponses(true)` för att visa API-anrop i konsolen. Det hjälper till att felsöka autentiseringsfel, hastighetsbegränsningar eller oväntade svar. Ta bort dessa flaggor i produktion för att minska loggbrus.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->