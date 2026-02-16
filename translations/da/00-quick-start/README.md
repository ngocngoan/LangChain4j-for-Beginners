# Modul 00: Kom godt i gang

## Indholdsfortegnelse

- [Introduktion](../../../00-quick-start)
- [Hvad er LangChain4j?](../../../00-quick-start)
- [LangChain4j Afhængigheder](../../../00-quick-start)
- [Forudsætninger](../../../00-quick-start)
- [Opsætning](../../../00-quick-start)
  - [1. Få dit GitHub-token](../../../00-quick-start)
  - [2. Indstil dit token](../../../00-quick-start)
- [Kør eksemplerne](../../../00-quick-start)
  - [1. Grundlæggende chat](../../../00-quick-start)
  - [2. Prompt-mønstre](../../../00-quick-start)
  - [3. Funktionsopkald](../../../00-quick-start)
  - [4. Dokument Q&A (RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hvad hvert eksempel viser](../../../00-quick-start)
- [Næste skridt](../../../00-quick-start)
- [Fejlfinding](../../../00-quick-start)

## Introduktion

Denne quickstart er lavet til at få dig hurtigt i gang med LangChain4j. Den dækker det absolutte grundlag for at bygge AI-applikationer med LangChain4j og GitHub Models. I de næste moduler bruger du Azure OpenAI med LangChain4j til at bygge mere avancerede applikationer.

## Hvad er LangChain4j?

LangChain4j er et Java-bibliotek, der forenkler opbygningen af AI-drevne applikationer. I stedet for at håndtere HTTP-klienter og JSON-parsing arbejder du med rene Java API'er.

"Chain" i LangChain refererer til kædning af flere komponenter sammen – du kan kæde et prompt til en model til en parser, eller kæde flere AI-opkald sammen, hvor et output føder det næste input. Denne quickstart fokuserer på det fundamentale, før vi udforsker mere komplekse kæder.

<img src="../../../translated_images/da/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kædning af komponenter i LangChain4j - byggesten forbinder for at skabe kraftfulde AI-arbejdsgange*

Vi bruger tre kernekomponenter:

**ChatLanguageModel** - Interfacet til AI-model-interaktioner. Kald `model.chat("prompt")` og få et svar som streng. Vi bruger `OpenAiOfficialChatModel`, som virker med OpenAI-kompatible endpoints som GitHub Models.

**AiServices** - Opretter typesikre AI service-interfacer. Definer metoder, annoter dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI'en kalder automatisk dine Java-metoder, når det er nødvendigt.

**MessageWindowChatMemory** - Vedligeholder samtalehistorik. Uden dette er hver forespørgsel uafhængig. Med dette husker AI tidligere beskeder og bevarer kontekst over flere runder.

<img src="../../../translated_images/da/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arkitektur - kernekomponenter der arbejder sammen for at drive dine AI-applikationer*

## LangChain4j Afhængigheder

Denne quickstart bruger to Maven-afhængigheder i [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Modulet `langchain4j-open-ai-official` leverer klassen `OpenAiOfficialChatModel`, som forbinder til OpenAI-kompatible API'er. GitHub Models bruger samme API-format, så der er ikke brug for en særlig adapter - bare peg base-URL’en til `https://models.github.ai/inference`.

## Forudsætninger

**Bruger du Dev Container?** Java og Maven er allerede installeret. Du skal kun have en GitHub Personal Access Token.

**Lokal Udvikling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruktioner nedenfor)

> **Bemærk:** Dette modul bruger `gpt-4.1-nano` fra GitHub Models. Ændr ikke modelnavnet i koden – det er konfigureret til at fungere med GitHubs tilgængelige modeller.

## Opsætning

### 1. Få dit GitHub-token

1. Gå til [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik på "Generate new token"
3. Sæt et beskrivende navn (f.eks. "LangChain4j Demo")
4. Sæt udløbstid (7 dage anbefales)
5. Under "Account permissions" find "Models" og sæt til "Read-only"
6. Klik på "Generate token"
7. Kopiér og gem dit token – du vil ikke kunne se det igen

### 2. Indstil dit token

**Mulighed 1: Bruger VS Code (anbefalet)**

Hvis du bruger VS Code, tilføj dit token i `.env`-filen i projektroden:

Hvis `.env`-filen ikke findes, kopiér `.env.example` til `.env` eller opret en ny `.env`-fil i projektroden.

**Eksempel på `.env`-fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Så kan du blot højreklikke på en demo-fil (f.eks. `BasicChatDemo.java`) i explorer og vælge **"Run Java"** eller bruge startkonfigurationerne i Run and Debug-panelet.

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

**Bruger VS Code:** Højreklik på en demo-fil i explorer og vælg **"Run Java"**, eller brug startkonfigurationerne i Run and Debug-panelet (sørg for du først har tilføjet token til `.env`-filen).

**Bruger Maven:** Alternativt kan du køre fra kommandolinjen:

### 1. Grundlæggende chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt-mønstre

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Viser zero-shot, few-shot, chain-of-thought og rollebaseret prompting.

### 3. Funktionsopkald

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI kalder automatisk dine Java-metoder, når det er nødvendigt.

### 4. Dokument Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stil spørgsmål om indholdet i `document.txt`.

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

## Hvad hvert eksempel viser

**Grundlæggende chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for at se LangChain4j på det enkleste. Du opretter et `OpenAiOfficialChatModel`, sender et prompt med `.chat()`, og får et svar tilbage. Dette demonstrerer fundamentet: hvordan man initialiserer modeller med brugerdefinerede endpoints og API-nøgler. Når du forstår dette mønster, bygger alt andet videre på det.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
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

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu hvor du ved, hvordan du snakker med en model, lad os udforske, hvad du siger til den. Denne demo bruger samme modelsetup, men viser fem forskellige prompt-mønstre. Prøv zero-shot prompts for direkte instruktioner, few-shot prompts der lærer fra eksempler, chain-of-thought prompts der afslører ræsonnementstrin, og rollebaserede prompts der sætter kontekst. Du vil se, hvordan samme model giver dramatisk forskellige resultater afhængigt af, hvordan du formulerer din anmodning.

Demoen viser også prompt-templates, som er en kraftfuld måde at skabe genanvendelige prompts med variable.
Eksemplet nedenfor viser en prompt med LangChain4j `PromptTemplate` til at udfylde variabler. AI vil svare baseret på den angivne destination og aktivitet.

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
> - "Hvad er forskellen på zero-shot og few-shot prompting, og hvornår skal jeg bruge dem hver især?"
> - "Hvordan påvirker temperatur-parameteren modellens svar?"
> - "Hvilke teknikker kan forhindre prompt injection angreb i produktion?"
> - "Hvordan kan jeg lave genanvendelige PromptTemplate-objekter til almindelige mønstre?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her bliver LangChain4j virkelig kraftfuld. Du bruger `AiServices` til at skabe en AI-assistent, der kan kalde dine Java-metoder. Bare annoter metoder med `@Tool("beskrivelse")` og LangChain4j tager sig af resten – AI’en beslutter automatisk, hvornår hver tool skal bruges baseret på brugerens spørgsmål. Dette demonstrerer funktionsopkald, en nøglemetode til at bygge AI, der kan udføre handlinger og ikke kun svare på spørgsmål.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) og spørg:
> - "Hvordan fungerer @Tool-annotationen og hvad gør LangChain4j med den bag kulisserne?"
> - "Kan AI kalde flere værktøjer i sekvens for at løse komplekse problemer?"
> - "Hvad sker der, hvis et værktøj kaster en undtagelse – hvordan håndterer jeg fejl?"
> - "Hvordan integrerer jeg en rigtig API i stedet for dette kalkulator-eksempel?"

**Dokument Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du fundamentet for RAG (retrieval-augmented generation). I stedet for at basere sig på modellens træningsdata indlæser du indhold fra [`document.txt`](../../../00-quick-start/document.txt) og inkluderer det i prompten. AI svarer baseret på dit dokument, ikke den generelle viden. Dette er første skridt mod at bygge systemer, som kan arbejde med dine egne data.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Bemærk:** Denne simple metode loader hele dokumentet ind i prompten. For store filer (>10KB) overskrider du kontekstgrænser. Modul 03 dækker chunking og vektorsøgning til produktions-RAG-systemer.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) og spørg:
> - "Hvordan forhindrer RAG AI-hallucinationer sammenlignet med modellens træningsdata?"
> - "Hvad er forskellen på denne simple tilgang og at bruge vektorembeddings til retrieval?"
> - "Hvordan skalerer jeg dette til at håndtere flere dokumenter eller større videnbaser?"
> - "Hvad er bedste praksis for at strukturere prompten, så AI kun bruger den angivne kontekst?"

**Ansvarlig AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Byg AI-sikkerhed med forsvar i dybden. Denne demo viser to beskyttelseslag, der arbejder sammen:

**Del 1: LangChain4j Input Guardrails** - Bloker farlige prompts inden de når LLM'en. Lav egne guardrails, der tjekker for forbudte nøgleord eller mønstre. De kører i din kode, så de er hurtige og gratis.

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

**Del 2: Provider Safety Filters** - GitHub Models har indbyggede filtre, der fanger det, dine guardrails måske overser. Du vil se hårde blokeringer (HTTP 400 fejl) for alvorlige overtrædelser og bløde afslag, hvor AI høfligt takker nej.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spørg:
> - "Hvad er InputGuardrail og hvordan laver jeg mine egne?"
> - "Hvad er forskellen på en hård blokering og et blødt afslag?"
> - "Hvorfor bruge både guardrails og provider-filtre sammen?"

## Næste skridt

**Næste modul:** [01-introduction - Kom godt i gang med LangChain4j og gpt-5 på Azure](../01-introduction/README.md)

---

**Navigation:** [← Tilbage til hoved](../README.md) | [Næste: Modul 01 - Introduktion →](../01-introduction/README.md)

---

## Fejlfinding

### Første Maven Build

**Problem**: Første `mvn clean compile` eller `mvn package` tager lang tid (10-15 minutter)

**Årsag**: Maven skal hente alle projektets afhængigheder (Spring Boot, LangChain4j biblioteker, Azure SDK'er osv.) ved første bygning.

**Løsning**: Dette er normalt. Efterfølgende builds bliver meget hurtigere, da afhængigheder caches lokalt. Downloadtid afhænger af din netværkshastighed.
### PowerShell Maven Kommandosyntaks

**Problem**: Maven-kommandoer fejler med fejl `Unknown lifecycle phase ".mainClass=..."`

**Årsag**: PowerShell fortolker `=` som en variabeltildelingsoperator, hvilket bryder Maven-ejendomssyntaksen

**Løsning**: Brug stop-parsing-operatoren `--%` før Maven-kommandoen:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatoren `--%` fortæller PowerShell at videregive alle resterende argumenter bogstaveligt til Maven uden fortolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar viser vraggtegn (f.eks. `????` eller `â??`) i stedet for emojis i PowerShell

**Årsag**: PowerShells standardkodning understøtter ikke UTF-8 emojis

**Løsning**: Kør denne kommando før kørsel af Java-applikationer:
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
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi stræber efter nøjagtighed, bedes du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument i dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->