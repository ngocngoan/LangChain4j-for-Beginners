# Modul 00: Hurtig Start

## Indholdsfortegnelse

- [Introduktion](../../../00-quick-start)
- [Hvad er LangChain4j?](../../../00-quick-start)
- [LangChain4j Afhængigheder](../../../00-quick-start)
- [Forudsætninger](../../../00-quick-start)
- [Opsætning](../../../00-quick-start)
  - [1. Få dit GitHub-token](../../../00-quick-start)
  - [2. Sæt dit token](../../../00-quick-start)
- [Kør eksemplerne](../../../00-quick-start)
  - [1. Grundlæggende Chat](../../../00-quick-start)
  - [2. Prompt Mønstre](../../../00-quick-start)
  - [3. Funktionskald](../../../00-quick-start)
  - [4. Dokument Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hvad hver enkelt eksempel viser](../../../00-quick-start)
- [Næste skridt](../../../00-quick-start)
- [Fejlfinding](../../../00-quick-start)

## Introduktion

Denne hurtigstart er lavet for at få dig op at køre med LangChain4j så hurtigt som muligt. Den dækker det absolutte grundlag for at bygge AI-applikationer med LangChain4j og GitHub Models. I de næste moduler vil du bruge Azure OpenAI med LangChain4j til at bygge mere avancerede applikationer.

## Hvad er LangChain4j?

LangChain4j er et Java-bibliotek, der forenkler opbygning af AI-drevne applikationer. I stedet for at håndtere HTTP-klienter og JSON-parsing arbejder du med rene Java-API’er.

“Chain” i LangChain refererer til at kæde flere komponenter sammen – du kan kæde et prompt til en model til en parser eller kæde flere AI-kald sammen, hvor ét output føres ind som næste input. Denne hurtigstart fokuserer på grundprincipperne, før vi udforsker mere komplekse kæder.

<img src="../../../translated_images/da/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kædning af komponenter i LangChain4j - byggeklodser, der forbinder for at skabe kraftfulde AI-arbejdsgange*

Vi bruger tre kernekomponenter:

**ChatModel** - Interfacet for AI-model-interaktioner. Kald `model.chat("prompt")` og få en svarstreng. Vi bruger `OpenAiOfficialChatModel`, som fungerer med OpenAI-kompatible endpoints som GitHub Models.

**AiServices** - Opretter typesikre AI-serviceinterfaces. Definer metoder, annoter dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI’en kalder automatisk dine Java-metoder, når det er nødvendigt.

**MessageWindowChatMemory** - Vedligeholder samtalehistorik. Uden denne er hver forespørgsel uafhængig. Med denne husker AI tidligere beskeder og bevarer kontekst over flere ture.

<img src="../../../translated_images/da/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arkitektur - kernekomponenter der arbejder sammen for at drive dine AI-applikationer*

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

Modulet `langchain4j-open-ai-official` leverer klassen `OpenAiOfficialChatModel`, som forbinder til OpenAI-kompatible API’er. GitHub Models bruger samme API-format, så der er ikke brug for en særlig adapter - peger blot base-URL’en på `https://models.github.ai/inference`.

Modulet `langchain4j-easy-rag` leverer automatisk deling, embedding og hentning af dokumenter, så du kan bygge RAG-applikationer uden manuelt at konfigurere hvert trin.

## Forudsætninger

**Bruger du Dev Container?** Java og Maven er allerede installeret. Du har kun brug for et GitHub Personal Access Token.

**Lokal udvikling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruktioner nedenfor)

> **Bemærk:** Dette modul bruger `gpt-4.1-nano` fra GitHub Models. Ændr ikke modelnavnet i koden - det er konfigureret til at fungere med GitHubs tilgængelige modeller.

## Opsætning

### 1. Få dit GitHub-token

1. Gå til [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik på "Generate new token"
3. Sæt et beskrivende navn (f.eks. "LangChain4j Demo")
4. Sæt udløbstid (7 dage anbefales)
5. Under "Account permissions" find "Models" og sæt til "Read-only"
6. Klik på "Generate token"
7. Kopiér og gem dit token – du vil ikke se det igen

### 2. Sæt dit token

**Mulighed 1: Brug VS Code (Anbefalet)**

Hvis du bruger VS Code, tilføj dit token til `.env`-filen i projektets rod:

Hvis `.env`-filen ikke findes, kopiér `.env.example` til `.env` eller opret en ny `.env`-fil i projektets rod.

**Eksempel på `.env`-fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Derefter kan du simpelthen højreklikke på en hvilken som helst demo-fil (f.eks. `BasicChatDemo.java`) i Explorer og vælge **"Run Java"** eller bruge lanceringskonfigurationer i Run and Debug-panelet.

**Mulighed 2: Brug Terminal**

Sæt token som miljøvariabel:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Kør eksemplerne

**Brug VS Code:** Højreklik blot på en demo-fil i Explorer og vælg **"Run Java"**, eller brug lanceringskonfigurationerne i Run and Debug-panelet (sørg for først at have tilføjet dit token til `.env`-filen).

**Brug Maven:** Alternativt kan du køre fra kommandolinjen:

### 1. Grundlæggende Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Mønstre

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Viser zero-shot, few-shot, kæde-af-tanke og rollebaserede prompts.

### 3. Funktionskald

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI kalder automatisk dine Java-metoder, når det er nødvendigt.

### 4. Dokument Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stil spørgsmål om dine dokumenter ved brug af Easy RAG med automatisk embedding og hentning.

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

## Hvad hver enkelt eksempel viser

**Grundlæggende Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for at se LangChain4j i sin enkleste form. Du opretter en `OpenAiOfficialChatModel`, sender et prompt med `.chat()` og får et svar tilbage. Dette demonstrerer fundamentet: hvordan du initialiserer modeller med brugerdefinerede endpoints og API-nøgler. Når du forstår dette mønster, bygger alt andet videre på det.

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
> - "Hvordan tilføjer jeg streaming responses i stedet for at vente på hele svaret?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu hvor du ved, hvordan du taler til en model, lad os udforske, hvad du siger til den. Denne demo bruger samme modelopsætning men viser fem forskellige prompt-mønstre. Prøv zero-shot prompts for direkte instruktioner, few-shot prompts som lærer fra eksempler, kæde-af-tanke prompts der afslører ræsonneringstrin, og rollebaserede prompts der sætter kontekst. Du vil se, hvordan samme model giver dramatisk forskellige resultater afhængig af, hvordan du rammer din forespørgsel ind.

Demoen demonstrerer også prompt-templates, som er en kraftfuld måde at skabe genanvendelige prompts med variable.
Nedenstående eksempel viser et prompt ved brug af LangChain4j `PromptTemplate` til at udfylde variable. AI’en svarer baseret på den angivne destination og aktivitet.

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
> - "Hvilke teknikker findes til at forhindre prompt injection angreb i produktion?"
> - "Hvordan kan jeg oprette genanvendelige PromptTemplate-objekter til almindelige mønstre?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her bliver LangChain4j kraftfuldt. Du bruger `AiServices` til at oprette en AI-assistent, der kan kalde dine Java-metoder. Bare annoter metoder med `@Tool("beskrivelse")` og LangChain4j klarer resten - AI beslutter automatisk, hvornår den skal bruge hvert værktøj baseret på brugerens spørgsmål. Dette demonstrerer funktionskald, en vigtig teknik til at bygge AI, der kan handle, ikke blot besvare spørgsmål.

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
> - "Hvordan fungerer @Tool-annotationen, og hvad gør LangChain4j med den bag scenen?"
> - "Kan AI kalde flere værktøjer i rækkefølge for at løse komplekse problemer?"
> - "Hvad sker der, hvis et værktøj kaster en undtagelse - hvordan håndterer jeg fejl?"
> - "Hvordan ville jeg integrere en rigtig API i stedet for dette regnemaskine-eksempel?"

**Dokument Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du RAG (retrieval-augmented generation) ved brug af LangChain4j’s "Easy RAG"-tilgang. Dokumenter indlæses, deles automatisk og embedded i en lager i hukommelsen, hvorefter en indholdshenter leverer relevante bidder til AI ved forespørgselstidspunktet. AI svarer baseret på dine dokumenter, ikke sin generelle viden.

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
> - "Hvordan forhindrer RAG AI-hallucinationer sammenlignet med brug af modellens træningsdata?"
> - "Hvad er forskellen på denne nemme tilgang og en brugerdefineret RAG-pipeline?"
> - "Hvordan skalerer jeg dette til at håndtere flere dokumenter eller større vidensbaser?"

**Ansvarlig AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Byg AI-sikkerhed med dyb forsvar. Denne demo viser to beskyttelseslag, der arbejder sammen:

**Del 1: LangChain4j Input Guardrails** - Bloker farlige prompts, før de når LLM. Opret brugerdefinerede guardrails, som tjekker for forbudte nøgleord eller mønstre. Disse kører i din kode, så de er hurtige og gratis.

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

**Del 2: Udbyder Sikkerhedsfiltre** - GitHub Models har indbyggede filtre, der fanger det, dine guardrails måske overser. Du vil se hårde blokeringer (HTTP 400-fejl) for alvorlige overtrædelser og bløde afvisninger, hvor AI høfligt afslår.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spørg:
> - "Hvad er InputGuardrail, og hvordan opretter jeg mine egne?"
> - "Hvad er forskellen på en hård blok og en blød afvisning?"
> - "Hvorfor bruge både guardrails og udbyderfiltre sammen?"

## Næste skridt

**Næste modul:** [01-introduction - Kom godt i gang med LangChain4j og gpt-5 på Azure](../01-introduction/README.md)

---

**Navigation:** [← Tilbage til hoved](../README.md) | [Næste: Modul 01 - Introduktion →](../01-introduction/README.md)

---

## Fejlfinding

### Første Maven Build

**Problem:** Første `mvn clean compile` eller `mvn package` tager lang tid (10-15 minutter)

**Årsag:** Maven skal downloade alle projekt-afhængigheder (Spring Boot, LangChain4j biblioteker, Azure SDK’er osv.) ved første build.

**Løsning:** Dette er normalt. Efterfølgende builds går meget hurtigere, da afhængigheder caches lokalt. Download-tiden afhænger af din netværkshastighed.

### PowerShell Maven Kommandosyntaks

**Problem:** Maven-kommandoer fejler med fejl `Unknown lifecycle phase ".mainClass=..."`
**Årsag**: PowerShell fortolker `=` som en variabeltildelingsoperator, hvilket bryder Maven-egenskabssyntaksen

**Løsning**: Brug stop-parse-operatoren `--%` før Maven-kommandoen:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatoren `--%` fortæller PowerShell at sende alle følgende argumenter bogstaveligt til Maven uden fortolkning.

### Windows PowerShell Emoji-Visning

**Problem**: AI-svar viser skraldetegn (fx `????` eller `â??`) i stedet for emojis i PowerShell

**Årsag**: PowerShells standardkodning understøtter ikke UTF-8 emojis

**Løsning**: Kør denne kommando før du udfører Java-applikationer:
```cmd
chcp 65001
```

Dette tvinger UTF-8-kodning i terminalen. Alternativt kan du bruge Windows Terminal, som har bedre Unicode-understøttelse.

### Fejlfinding af API-kald

**Problem**: Godkendelsesfejl, hastighedsbegrænsninger eller uventede svar fra AI-modellen

**Løsning**: Eksemplerne inkluderer `.logRequests(true)` og `.logResponses(true)` for at vise API-kald i konsollen. Dette hjælper med at fejlfinde godkendelsesfejl, hastighedsbegrænsninger eller uventede svar. Fjern disse flag i produktion for at reducere logstøj.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For vigtig information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->