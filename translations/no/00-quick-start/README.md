# Modul 00: Rask Start

## Innholdsfortegnelse

- [Introduksjon](../../../00-quick-start)
- [Hva er LangChain4j?](../../../00-quick-start)
- [LangChain4j Avhengigheter](../../../00-quick-start)
- [Forutsetninger](../../../00-quick-start)
- [Oppsett](../../../00-quick-start)
  - [1. Skaff GitHub-tokenet ditt](../../../00-quick-start)
  - [2. Sett tokenet ditt](../../../00-quick-start)
- [Kjør Eksemplene](../../../00-quick-start)
  - [1. Grunnleggende Chat](../../../00-quick-start)
  - [2. Prompt Mønstre](../../../00-quick-start)
  - [3. Funksjonskall](../../../00-quick-start)
  - [4. Dokument Spørsmål & Svar (Easy RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hva Hvert Eksempel Viser](../../../00-quick-start)
- [Neste Steg](../../../00-quick-start)
- [Feilsøking](../../../00-quick-start)

## Introduksjon

Denne raske starten er ment å komme i gang med LangChain4j så raskt som mulig. Den dekker det absolutte grunnlaget for å bygge AI-applikasjoner med LangChain4j og GitHub Models. I de neste modulene vil du bytte til Azure OpenAI og GPT-5.2 og gå dypere inn i hvert konsept.

## Hva er LangChain4j?

LangChain4j er et Java-bibliotek som forenkler bygging av AI-drevne applikasjoner. I stedet for å håndtere HTTP-klienter og JSON-parsing, jobber du med rene Java-APIer.

"Chain" i LangChain refererer til å koble sammen flere komponenter – du kan kjede en prompt til en modell til en parser, eller kjede flere AI-kall sammen hvor ett output mates inn som input til det neste. Denne raske starten fokuserer på det grunnleggende før du utforsker mer komplekse kjeder.

<img src="../../../translated_images/no/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kjede av komponenter i LangChain4j – byggesteiner kobles sammen for å skape kraftige AI-arbeidsflyter*

Vi bruker tre kjernekomponenter:

**ChatModel** – Grensesnittet for AI-modellinteraksjoner. Kall `model.chat("prompt")` og få en responsstreng. Vi bruker `OpenAiOfficialChatModel` som fungerer med OpenAI-kompatible endepunkter som GitHub Models.

**AiServices** – Lager typesikre AI-tjenestegrensesnitt. Definer metoder, annoter dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI-en kaller automatisk Java-metodene dine når det trengs.

**MessageWindowChatMemory** – Opprettholder samtalehistorikk. Uten dette er hver forespørsel uavhengig. Med dette husker AI tidligere meldinger og opprettholder kontekst over flere turer.

<img src="../../../translated_images/no/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arkitektur – kjernekomponenter som samarbeider for å styrke AI-applikasjonene dine*

## LangChain4j Avhengigheter

Denne raske starten bruker tre Maven-avhengigheter i [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulen `langchain4j-open-ai-official` tilbyr klassen `OpenAiOfficialChatModel` som kobler til OpenAI-kompatible APIer. GitHub Models bruker samme API-format, så det trengs ingen spesiell adapter – bare pek base-URL til `https://models.github.ai/inference`.

Modulen `langchain4j-easy-rag` tilbyr automatisk dokumentdeling, embedding, og henting slik at du kan bygge RAG-applikasjoner uten manuelt oppsett av hvert steg.

## Forutsetninger

**Bruker du Dev Container?** Java og Maven er allerede installert. Du trenger kun en GitHub Personal Access Token.

**Lokal utvikling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruksjoner nedenfor)

> **Merk:** Denne modulen bruker `gpt-4.1-nano` fra GitHub Models. Ikke endre modellnavnet i koden – det er konfigurert til å fungere med GitHubs tilgjengelige modeller.

## Oppsett

### 1. Skaff GitHub-tokenet ditt

1. Gå til [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikk "Generate new token"
3. Sett et beskrivende navn (f.eks. "LangChain4j Demo")
4. Sett utløpstid (7 dager anbefales)
5. Under "Account permissions", finn "Models" og sett til "Read-only"
6. Klikk "Generate token"
7. Kopier og lagre tokenet – du får ikke se det igjen

### 2. Sett tokenet ditt

**Alternativ 1: Bruke VS Code (Anbefalt)**

Hvis du bruker VS Code, legg tokenet ditt i `.env`-filen i prosjektets rotmappe:

Hvis `.env`-filen ikke finnes, kopier `.env.example` til `.env` eller opprett en ny `.env`-fil i prosjektets rotmappe.

**Eksempel på `.env`-fil:**
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Da kan du enkelt høyreklikke på en demo-fil (f.eks. `BasicChatDemo.java`) i Explorer og velge **"Run Java"** eller bruke oppstartskonfigurasjonene fra Run and Debug-panelet.

**Alternativ 2: Bruke Terminal**

Sett tokenet som en miljøvariabel:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Kjør Eksemplene

**Bruke VS Code:** Høyreklikk på en demo-fil i Explorer og velg **"Run Java"**, eller bruk oppstartskonfigurasjonene fra Run and Debug-panelet (sørg for at du har lagt til tokenet i `.env`-filen først).

**Bruke Maven:** Alternativt kan du kjøre fra kommandolinjen:

### 1. Grunnleggende Chat

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

Viser zero-shot, few-shot, chain-of-thought og rollebasert prompting.

### 3. Funksjonskall

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI-en kaller automatisk Java-metodene dine når det trengs.

### 4. Dokument Spørsmål & Svar (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Still spørsmål om dokumentene dine ved hjelp av Easy RAG med automatisk embedding og gjenfinning.

### 5. Ansvarlig AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Se hvordan AI-sikkerhetsfiltre blokkerer skadelig innhold.

## Hva Hvert Eksempel Viser

**Grunnleggende Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for å se LangChain4j på sitt enkleste. Du lager en `OpenAiOfficialChatModel`, sender en prompt med `.chat()`, og får tilbake et svar. Dette viser grunnlaget: hvordan initialisere modeller med egendefinerte endepunkter og API-nøkler. Når du forstår dette mønsteret, bygger alt annet på det.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) og spør:
> - "Hvordan bytter jeg fra GitHub Models til Azure OpenAI i denne koden?"
> - "Hvilke andre parametere kan jeg konfigurere i OpenAiOfficialChatModel.builder()?"
> - "Hvordan legger jeg til streaming-respons i stedet for å vente på hele responsen?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nå som du vet hvordan du snakker med en modell, la oss utforske hva du sier til den. Denne demoen bruker samme modelloppsett, men viser fem forskjellige prompting-mønstre. Prøv zero-shot prompts for direkte instruksjoner, few-shot prompts som lærer fra eksempler, chain-of-thought prompts som viser resonnementstrinn, og rollebaserte prompts som setter kontekst. Du vil se hvordan samme modell gir dramatisk forskjellige resultater basert på hvordan du formulerer forespørselen din.

Demoen viser også prompt-maler, som er en kraftig måte å lage gjenbrukbare prompts med variabler.
Eksemplet nedenfor viser en prompt som bruker LangChain4j `PromptTemplate` til å fylle inn variabler. AI-en vil svare basert på gitt destinasjon og aktivitet.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) og spør:
> - "Hva er forskjellen på zero-shot og few-shot prompting, og når bør jeg bruke hver?"
> - "Hvordan påvirker temperaturparameter modellens svar?"
> - "Hva er noen teknikker for å forhindre prompt injection-angrep i produksjon?"
> - "Hvordan kan jeg lage gjenbrukbare PromptTemplate-objekter for vanlige mønstre?"

**Verktøyintegrasjon** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her blir LangChain4j kraftig. Du bruker `AiServices` for å lage en AI-assistent som kan kalle dine Java-metoder. Bare annoter metoder med `@Tool("beskrivelse")` og LangChain4j tar seg av resten – AI-en bestemmer automatisk når hver verktøy skal brukes basert på hva brukeren spør om. Dette demonstrerer funksjonskall, en nøkkelteknikk for å bygge AI som kan utføre handlinger, ikke bare svare på spørsmål.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) og spør:
> - "Hvordan fungerer @Tool-annotasjonen og hva gjør LangChain4j med den bak kulissene?"
> - "Kan AI kalle flere verktøy i rekkefølge for å løse komplekse problemer?"
> - "Hva skjer hvis et verktøy kaster en unntak – hvordan bør jeg håndtere feil?"
> - "Hvordan integrerer jeg en ekte API i stedet for dette kalkulator-eksempelet?"

**Dokument Spørsmål & Svar (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du RAG (retrieval-augmented generation) ved hjelp av LangChain4js "Easy RAG"-metode. Dokumenter lastes inn, deles automatisk opp, og embedes i et minnelager. Deretter leverer en innholdshenter relevante biter til AI-en ved spørringstid. AI-en svarer basert på dokumentene dine, ikke på generell kunnskap.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) og spør:
> - "Hvordan hindrer RAG AI-hallusinasjoner sammenlignet med å bruke modellens treningsdata?"
> - "Hva er forskjellen på denne enkle metoden og en tilpasset RAG-pipeline?"
> - "Hvordan skalerer jeg dette til å håndtere flere dokumenter eller større kunnskapsbaser?"

**Ansvarlig AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bygg AI-sikkerhet med forsvar i dybden. Denne demoen viser to beskyttelseslag som samarbeider:

**Del 1: LangChain4j Input Guardrails** – Blokker farlige prompts før de når LLM. Lag egne guardrails som sjekker for forbudte nøkkelord eller mønstre. Disse kjøres i koden din, så de er raske og gratis.

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

**Del 2: Leverandørsikkerhetsfiltre** – GitHub Models har innebygde filtre som fanger opp det guardrails kanskje overser. Du vil se harde blokker (HTTP 400-feil) for alvorlige brudd og myke refusjoner hvor AI høflig avstår.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spør:
> - "Hva er InputGuardrail og hvordan lager jeg min egen?"
> - "Hva er forskjellen på en hard blokk og en myk refusjon?"
> - "Hvorfor bruke både guardrails og leverandørfiltre sammen?"

## Neste Steg

**Neste Modul:** [01-introduction - Komme i gang med LangChain4j](../01-introduction/README.md)

---

**Navigasjon:** [← Tilbake til Hoved](../README.md) | [Neste: Modul 01 - Introduksjon →](../01-introduction/README.md)

---

## Feilsøking

### Første Maven Build

**Problem:** Første `mvn clean compile` eller `mvn package` tar lang tid (10-15 minutter)

**Årsak:** Maven må laste ned alle prosjektavhengigheter (Spring Boot, LangChain4j-biblioteker, Azure SDK-er, osv.) ved første bygg.

**Løsning:** Dette er normal oppførsel. Etterfølgende bygg er mye raskere da avhengigheter caches lokalt. Nedlastningstid avhenger av nettverkshastighet.

### PowerShell Maven Kommando-syntaks

**Problem:** Maven-kommandoer feiler med feilmeldingen `Unknown lifecycle phase ".mainClass=..."`
**Årsak**: PowerShell tolker `=` som en variabeltilordningsoperator, noe som bryter Maven-egenskapssyntaksen

**Løsning**: Bruk stopp-parsing-operatoren `--%` før Maven-kommandoen:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%`-operatoren forteller PowerShell å sende alle resterende argumenter bokstavelig til Maven uten tolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar viser søppelkarakterer (f.eks. `????` eller `â??`) i stedet for emojis i PowerShell

**Årsak**: PowerShells standardkoding støtter ikke UTF-8-emoji

**Løsning**: Kjør denne kommandoen før du kjører Java-applikasjoner:
```cmd
chcp 65001
```

Dette tvinger UTF-8-koding i terminalen. Alternativt kan du bruke Windows Terminal som har bedre Unicode-støtte.

### Feilsøking av API-kall

**Problem**: Autentiseringsfeil, ratebegrensninger, eller uventede svar fra AI-modellen

**Løsning**: Eksemplene inkluderer `.logRequests(true)` og `.logResponses(true)` for å vise API-kall i konsollen. Dette hjelper med å feilsøke autentiseringsfeil, ratebegrensninger eller uventede svar. Fjern disse flaggene i produksjon for å redusere loggstøy.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets opprinnelige språk skal betraktres som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->