# Modul 00: Hurtig Start

## Indholdsfortegnelse

- [Introduktion](../../../00-quick-start)
- [Hvad er LangChain4j?](../../../00-quick-start)
- [LangChain4j Afhængigheder](../../../00-quick-start)
- [Forudsætninger](../../../00-quick-start)
- [Opsætning](../../../00-quick-start)
  - [1. Få dit GitHub-token](../../../00-quick-start)
  - [2. Angiv dit token](../../../00-quick-start)
- [Kør eksemplerne](../../../00-quick-start)
  - [1. Grundlæggende Chat](../../../00-quick-start)
  - [2. Promptmønstre](../../../00-quick-start)
  - [3. Funktionsopkald](../../../00-quick-start)
  - [4. Dokument Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hvad Hver Eksempel Viser](../../../00-quick-start)
- [Næste Trin](../../../00-quick-start)
- [Fejlfinding](../../../00-quick-start)

## Introduktion

Denne hurtigstart er ment til at få dig i gang med LangChain4j så hurtigt som muligt. Den dækker det absolutte grundlag for at bygge AI-applikationer med LangChain4j og GitHub Models. I de næste moduler skifter du til Azure OpenAI og GPT-5.2 og dykker dybere ned i hvert koncept.

## Hvad er LangChain4j?

LangChain4j er et Java-bibliotek, der forenkler opbygningen af AI-drevne applikationer. I stedet for at håndtere HTTP-klienter og JSON-parsing arbejder du med rene Java-API'er.

"Chain" i LangChain refererer til at kæde flere komponenter sammen – du kan kæde en prompt til en model til en parser, eller kæde flere AI-opkald, hvor én output fødes videre som input til det næste. Denne hurtigstart fokuserer på grundlæggende før vi udforsker mere komplekse kæder.

<img src="../../../translated_images/da/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kædning af komponenter i LangChain4j – byggeklodser forbindes for at skabe kraftfulde AI-workflows*

Vi bruger tre kernekomponenter:

**ChatModel** – Interfacet for AI-modelinteraktioner. Kald `model.chat("prompt")` og få en respons som streng. Vi bruger `OpenAiOfficialChatModel`, som virker med OpenAI-kompatible endpoints som GitHub Models.

**AiServices** – Opretter typesikre AI service interfaces. Definer metoder, annoter dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI’en kalder automatisk dine Java-metoder når nødvendigt.

**MessageWindowChatMemory** – Vedligeholder samtalehistorik. Uden denne er hver anmodning uafhængig. Med den husker AI tidligere beskeder og bevarer kontekst på tværs af flere runder.

<img src="../../../translated_images/da/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arkitektur – kernekomponenter arbejder sammen for at drive dine AI-applikationer*

## LangChain4j Afhængigheder

Denne hurtigstart bruger tre Maven-afhængigheder i [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulet `langchain4j-open-ai-official` leverer klassen `OpenAiOfficialChatModel`, der forbinder til OpenAI-kompatible API’er. GitHub Models bruger samme API-format, så der behøves ingen særlig adapter – blot peg base-URL’en til `https://models.github.ai/inference`.

Modulet `langchain4j-easy-rag` leverer automatisk dokumentsplitning, embedding og retrieval, så du kan bygge RAG-applikationer uden manuelt at konfigurere hvert trin.

## Forudsætninger

**Bruger du Dev Container?** Java og Maven er allerede installeret. Du skal kun bruge et GitHub Personal Access Token.

**Lokal Udvikling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruktioner nedenfor)

> **Bemærk:** Dette modul bruger `gpt-4.1-nano` fra GitHub Models. Ændr ikke modelnavnet i koden – det er konfigureret til at fungere med GitHubs tilgængelige modeller.

## Opsætning

### 1. Få dit GitHub-token

1. Gå til [GitHub Indstillinger → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik på "Generate new token"
3. Sæt et beskrivende navn (f.eks. "LangChain4j Demo")
4. Sæt udløbstid (7 dage anbefalet)
5. Under "Account permissions", find "Models" og sæt til "Read-only"
6. Klik "Generate token"
7. Kopiér og gem dit token – du vil ikke se det igen

### 2. Angiv dit token

**Mulighed 1: Brug af VS Code (Anbefalet)**

Hvis du bruger VS Code, tilføj dit token til `.env` filen i projektroden:

Hvis `.env` filen ikke findes, kopier `.env.example` til `.env` eller opret en ny `.env` fil i projektroden.

**Eksempel på `.env` fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Så kan du blot højreklikke på en hvilken som helst demo-fil (f.eks. `BasicChatDemo.java`) i Explorer og vælge **"Run Java"** eller bruge launch-konfigurationerne i Run and Debug-panelet.

**Mulighed 2: Brug af Terminal**

Sæt token som en miljøvariabel:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Kør eksemplerne

**Med VS Code:** Højreklik på enhver demo-fil i Explorer og vælg **"Run Java"**, eller brug launch-konfigurationerne i Run and Debug-panelet (sørg for at du først har tilføjet dit token til `.env` filen).

**Med Maven:** Alternativt kan du køre fra kommandolinjen:

### 1. Grundlæggende Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Promptmønstre

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Viser zero-shot, few-shot, kæde-af-tanke og rollebaseret prompting.

### 3. Funktionsopkald

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI kalder automatisk dine Java-metoder når nødvendigt.

### 4. Dokument Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stil spørgsmål om dine dokumenter ved hjælp af Easy RAG med automatisk embedding og retrieval.

### 5. Ansvarlig AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Se hvordan AI-sikkerhedsfiltre blokerer skadeligt indhold.

## Hvad Hver Eksempel Viser

**Grundlæggende Chat** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for at se LangChain4j på dets enkleste. Du opretter en `OpenAiOfficialChatModel`, sender en prompt med `.chat()`, og får tilbage et svar. Dette demonstrerer fundamentet: hvordan man initialiserer modeller med brugerdefinerede endpoints og API-nøgler. Når du forstår dette mønster, bygger alt andet på det.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) og spørg:
> - "Hvordan skifter jeg fra GitHub Models til Azure OpenAI i denne kode?"
> - "Hvilke andre parametre kan jeg konfigurere i OpenAiOfficialChatModel.builder()?"
> - "Hvordan tilføjer jeg streaming-svar i stedet for at vente på hele svaret?"

**Prompt Engineering** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu hvor du ved, hvordan du taler til en model, lad os udforske hvad du siger til den. Denne demo bruger samme modelopsætning, men viser fem forskellige prompt-mønstre. Prøv zero-shot prompts for direkte instruktioner, few-shot prompts der lærer af eksempler, chain-of-thought prompts der afslører ræsonnementstrin, og rollebaserede prompts der sætter kontekst. Du vil se, hvordan samme model giver dramatisk forskellige resultater baseret på hvordan du rammer din anmodning ind.

Demoen demonstrerer også prompt-skabeloner, som er en kraftfuld måde at lave genanvendelige prompts med variabler.
Nedenstående eksempel viser en prompt ved hjælp af LangChain4j `PromptTemplate` til at udfylde variabler. AI’en svarer baseret på den angivne destination og aktivitet.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) og spørg:
> - "Hvad er forskellen på zero-shot og few-shot prompting, og hvornår skal jeg bruge hver?"
> - "Hvordan påvirker temperatur-parameteren modellens svar?"
> - "Hvilke teknikker kan forhindre prompt injection angreb i produktion?"
> - "Hvordan kan jeg lave genanvendelige PromptTemplate-objekter til almindelige mønstre?"

**Tool Integration** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her bliver LangChain4j virkelig kraftfuld. Du bruger `AiServices` til at oprette en AI-assistent, som kan kalde dine Java-metoder. Bare annoter metoder med `@Tool("beskrivelse")`, og LangChain4j klarer resten – AI’en beslutter automatisk, hvornår hvert værktøj skal bruges baseret på hvad brugeren spørger om. Dette demonstrerer funktionsopkald, en nøglemetode til at bygge AI, der kan handle, ikke bare svare på spørgsmål.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) og spørg:
> - "Hvordan virker @Tool-annotationen, og hvad gør LangChain4j med den bag scenen?"
> - "Kan AI kalde flere værktøjer i rækkefølge for at løse komplekse problemer?"
> - "Hvad sker der, hvis et værktøj kaster en undtagelse – hvordan håndterer jeg fejl?"
> - "Hvordan integrerer jeg en rigtig API i stedet for dette lommeregner-eksempel?"

**Dokument Q&A (Easy RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du RAG (retrieval-augmented generation) ved hjælp af LangChain4j’s "Easy RAG" tilgang. Dokumenter indlæses, deles automatisk op og embeddes i en in-memory store, hvorefter en indholdsretriever leverer relevante bidder til AI’en ved forespørgselstidspunktet. AI’en svarer baseret på dine dokumenter, ikke sin generelle viden.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) og spørg:
> - "Hvordan forhindrer RAG AI-hallucinationer sammenlignet med at bruge modellens træningsdata?"
> - "Hvad er forskellen på denne nemme tilgang og en tilpasset RAG pipeline?"
> - "Hvordan ville jeg skalere dette til at håndtere flere dokumenter eller større vidensbaser?"

**Ansvarlig AI** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Byg AI-sikkerhed med forsvar i dybden. Denne demo viser to beskyttelseslag, der arbejder sammen:

**Del 1: LangChain4j Input Guardrails** – Bloker farlige prompts før de når LLM. Opret tilpassede guardrails, der tjekker for forbudte nøgleord eller mønstre. De kører i din kode, så de er hurtige og gratis.

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

**Del 2: Udbyderens Sikkerhedsfiltre** – GitHub Models har indbyggede filtre, der fanger hvad dine guardrails måske misser. Du vil se hårde blokeringer (HTTP 400 fejl) for alvorlige overtrædelser og bløde afslag, hvor AI’en høfligt nægter.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spørg:
> - "Hvad er InputGuardrail, og hvordan laver jeg mine egne?"
> - "Hvad er forskellen mellem en hård blokering og et blødt afslag?"
> - "Hvorfor bruge både guardrails og udbyderfiltre sammen?"

## Næste Trin

**Næste Modul:** [01-introduction - Kom godt i gang med LangChain4j](../01-introduction/README.md)

---

**Navigation:** [← Tilbage til Hoved](../README.md) | [Næste: Modul 01 - Introduktion →](../01-introduction/README.md)

---

## Fejlfinding

### Første Maven Build

**Problem**: Første `mvn clean compile` eller `mvn package` tager lang tid (10-15 minutter)

**Årsag**: Maven skal downloade alle projektets afhængigheder (Spring Boot, LangChain4j biblioteker, Azure SDK’er osv.) ved første build.

**Løsning**: Dette er normal opførsel. Efterfølgende builds bliver meget hurtigere, da afhængigheder caches lokalt. Downloadtiden afhænger af din netværkshastighed.

### PowerShell Maven Kommando-syntaks

**Problem**: Maven kommandoer fejler med fejl `Unknown lifecycle phase ".mainClass=..."`
**Årsag**: PowerShell fortolker `=` som en variabel-assigneringsoperator, hvilket ødelægger Maven-egenskabssyntaksen

**Løsning**: Brug stop-parsing operatoren `--%` før Maven-kommandoen:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatoren `--%` fortæller PowerShell at sende alle resterende argumenter bogstaveligt til Maven uden fortolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar viser uforståelige tegn (f.eks. `????` eller `â??`) i stedet for emojis i PowerShell

**Årsag**: PowerShells standardkodning understøtter ikke UTF-8 emojis

**Løsning**: Kør denne kommando før du kører Java-applikationer:
```cmd
chcp 65001
```

Dette tvinger UTF-8 kodning i terminalen. Alternativt kan du bruge Windows Terminal, der har bedre Unicode-understøttelse.

### Fejlfinding af API-kald

**Problem**: Autentificeringsfejl, raterestriktioner eller uventede svar fra AI-modellen

**Løsning**: Eksemplerne inkluderer `.logRequests(true)` og `.logResponses(true)` for at vise API-kald i konsollen. Dette hjælper med at fejlfinde autentificeringsfejl, raterestriktioner eller uventede svar. Fjern disse flags i produktion for at reducere logstøj.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->