# Modul 00: Rask start

## Innholdsfortegnelse

- [Introduksjon](../../../00-quick-start)
- [Hva er LangChain4j?](../../../00-quick-start)
- [LangChain4j avhengigheter](../../../00-quick-start)
- [Forutsetninger](../../../00-quick-start)
- [Oppsett](../../../00-quick-start)
  - [1. Skaff ditt GitHub-token](../../../00-quick-start)
  - [2. Sett ditt token](../../../00-quick-start)
- [Kjør eksemplene](../../../00-quick-start)
  - [1. Grunnleggende chat](../../../00-quick-start)
  - [2. Prompt-mønstre](../../../00-quick-start)
  - [3. Funksjonsanrop](../../../00-quick-start)
  - [4. Dokument Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hva hvert eksempel viser](../../../00-quick-start)
- [Neste steg](../../../00-quick-start)
- [Feilsøking](../../../00-quick-start)

## Introduksjon

Denne raske starten er ment å få deg i gang med LangChain4j så raskt som mulig. Den dekker det absolutte grunnlaget for å bygge AI-applikasjoner med LangChain4j og GitHub-modeller. I de neste modulene vil du bruke Azure OpenAI med LangChain4j for å bygge mer avanserte applikasjoner.

## Hva er LangChain4j?

LangChain4j er et Java-bibliotek som forenkler bygging av AI-drevne applikasjoner. I stedet for å håndtere HTTP-klienter og JSON-parsing, jobber du med rene Java-APIer.

“Chain” i LangChain refererer til å kople sammen flere komponenter – du kan for eksempel koble en prompt til en modell til en parser, eller kjede flere AI-kall der ett output føres inn som input til neste. Denne raske starten fokuserer på grunnprinsippene før vi utforsker mer komplekse kjeder.

<img src="../../../translated_images/no/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kobling av komponenter i LangChain4j – byggeklosser kobles for å skape kraftige AI-arbeidsflyter*

Vi bruker tre kjernekomponenter:

**ChatModel** – Grensesnittet for interaksjoner med AI-modeller. Kall `model.chat("prompt")` og få en svarstreng tilbake. Vi bruker `OpenAiOfficialChatModel` som fungerer med OpenAI-kompatible endepunkter som GitHub Models.

**AiServices** – Oppretter typesikre AI-tjenestegrensesnitt. Definer metoder, annoter dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI-en kaller automatisk dine Java-metoder når det trengs.

**MessageWindowChatMemory** – Opprettholder samtalehistorikk. Uten dette er hvert kall uavhengig. Med det husker AI tidligere meldinger og bevarer kontekst gjennom flere runder.

<img src="../../../translated_images/no/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-arkitektur – kjernekomponenter som jobber sammen for å drive dine AI-applikasjoner*

## LangChain4j avhengigheter

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
  
`langchain4j-open-ai-official`-modulen tilbyr klassen `OpenAiOfficialChatModel` som kobler til OpenAI-kompatible API-er. GitHub Models bruker samme API-format, så ingen spesiell adapter er nødvendig – bare pek basis-URL til `https://models.github.ai/inference`.

`langchain4j-easy-rag`-modulen tilbyr automatisk dokumentsplitting, embedding og henting slik at du kan bygge RAG-applikasjoner uten å manuelt konfigurere hvert steg.

## Forutsetninger

**Bruker du Dev Container?** Java og Maven er allerede installert. Du trenger kun et GitHub Personal Access Token.

**Lokal utvikling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instruksjoner nedenfor)

> **Merk:** Denne modulen bruker `gpt-4.1-nano` fra GitHub Models. Ikke modifiser modellnavnet i koden – det er konfigurert til å fungere med GitHubs tilgjengelige modeller.

## Oppsett

### 1. Skaff ditt GitHub-token

1. Gå til [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikk “Generate new token”
3. Sett et beskrivende navn (f.eks. “LangChain4j Demo”)
4. Sett utløpstid (7 dager anbefalt)
5. Under “Account permissions”, finn “Models” og sett til “Read-only”
6. Klikk “Generate token”
7. Kopier og lagre token – du vil ikke se det igjen

### 2. Sett ditt token

**Alternativ 1: Bruke VS Code (Anbefalt)**

Om du bruker VS Code, legg token i `.env`-filen i prosjektroten:

Hvis `.env`-fil ikke finnes, kopier `.env.example` til `.env` eller lag en ny `.env`-fil i prosjektroten.

**Eksempel på `.env` fil:**  
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Deretter kan du enkelt høyreklikke på en demo-fil (f.eks. `BasicChatDemo.java`) i Explorer og velge **"Run Java"** eller bruke oppstartskonfigurasjoner fra Run and Debug-panelet.

**Alternativ 2: Bruke terminal**

Sett token som miljøvariabel:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  
## Kjør eksemplene

**Bruker VS Code:** Høyreklikk på en demo-fil i Explorer og velg **"Run Java"**, eller bruk oppstartskonfigurasjoner fra Run and Debug-panelet (sørg for at du har lagt til token i `.env`-filen først).

**Bruke Maven:** Alternativt kan du kjøre fra kommandolinjen:

### 1. Grunnleggende chat

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
  
Viser zero-shot, few-shot, chain-of-thought, og rollebasert prompting.

### 3. Funksjonsanrop

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
AI kaller automatisk dine Java-metoder når det trengs.

### 4. Dokument Q&A (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Still spørsmål om dine dokumenter med Easy RAG med automatisk embedding og henting.

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

## Hva hvert eksempel viser

**Grunnleggende chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for å se LangChain4j på sitt enkleste. Du lager en `OpenAiOfficialChatModel`, sender en prompt med `.chat()` og får et svar tilbake. Dette demonstrerer grunnlaget: hvordan å initialisere modeller med egendefinerte endepunkter og API-nøkler. Når du forstår dette mønsteret, bygger alt annet på det.

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
> - "Hvordan legger jeg til strømmede svar i stedet for å vente på hele svaret?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nå som du vet hvordan du snakker med en modell, la oss utforske hva du sier til den. Denne demoen bruker samme modelloppsett men viser fem ulike prompting-mønstre. Prøv zero-shot-prompter for direkte instruksjoner, few-shot-prompter som lærer fra eksempler, chain-of-thought-prompter som avslører resonnementstrinn, og rollebaserte prompter som setter kontekst. Du vil se hvordan samme modell gir dramatisk forskjellige resultater basert på hvordan du rammer inn forespørselen.

Demoen viser også prompt-maler, som er en kraftfull måte å lage gjenbrukbare prompter med variabler på.  
Eksempelet under viser en prompt som bruker LangChain4j `PromptTemplate` for å fylle inn variabler. AI vil svare basert på gitt destinasjon og aktivitet.

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
> - "Hva er forskjellen mellom zero-shot og few-shot prompting, og når bør jeg bruke hver av dem?"  
> - "Hvordan påvirker temperatur-parameteren modellens svar?"  
> - "Hvilke teknikker finnes for å forhindre prompt-injeksjonsangrep i produksjon?"  
> - "Hvordan kan jeg lage gjenbrukbare PromptTemplate-objekter for vanlige mønstre?"

**Integrasjon av verktøy** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her blir LangChain4j kraftfullt. Du bruker `AiServices` til å lage en AI-assistent som kan kalle dine Java-metoder. Bare annoter metodene med `@Tool("beskrivelse")` og LangChain4j tar seg av resten – AI bestemmer automatisk når hvert verktøy skal brukes basert på hva brukeren spør om. Dette demonstrerer funksjonsanrop, en nøkkelteknikk for å bygge AI som kan utføre handlinger, ikke bare svare på spørsmål.

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
> - "Kan AI kalle flere verktøy i sekvens for å løse komplekse problemer?"  
> - "Hva skjer hvis et verktøy kaster en exception – hvordan bør jeg håndtere feil?"  
> - "Hvordan integrerer jeg et ekte API i stedet for dette kalkulator-eksemplet?"

**Dokument Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du RAG (retrieval-augmented generation) ved bruk av LangChain4j sin "Easy RAG"-tilnærming. Dokumenter lastes inn, deles automatisk og embeds i et minnelager, deretter henter en innholdshenter relevante biter til AI-en når du spør. AI svarer basert på dine dokumenter, ikke dens generelle kunnskap.

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
> - "Hvordan forhindrer RAG AI-hallusinasjoner sammenlignet med bruk av modellens treningsdata?"  
> - "Hva er forskjellen mellom denne enkle tilnærmingen og en tilpasset RAG-pipeline?"  
> - "Hvordan kan jeg skalere dette til å håndtere flere dokumenter eller større kunnskapsbaser?"

**Ansvarlig AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bygg AI-sikkerhet med forsvar i dybden. Denne demoen viser to lag av beskyttelse som jobber sammen:

**Del 1: LangChain4j Input Guardrails** – Blokker farlige prompter før de når LLM. Lag egendefinerte beskyttelsesregler som sjekker for forbudte nøkkelord eller mønstre. Disse kjører i koden din, så de er raske og gratis.

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
  
**Del 2: Providers sikkerhetsfiltre** – GitHub Models har innebygde filtre som fanger opp det dine guardrails kan gå glipp av. Du vil se harde blokkeringer (HTTP 400-feil) for alvorlige brudd og myke avslag hvor AI høflig avslår.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spør:  
> - "Hva er InputGuardrail og hvordan lager jeg min egen?"  
> - "Hva er forskjellen på hard blokkering og mykt avslag?"  
> - "Hvorfor bruke både guardrails og providere filtre samtidig?"

## Neste steg

**Neste modul:** [01-introduction - Komme i gang med LangChain4j og gpt-5 på Azure](../01-introduction/README.md)

---

**Navigasjon:** [← Tilbake til hoved](../README.md) | [Neste: Modul 01 - Introduksjon →](../01-introduction/README.md)

---

## Feilsøking

### Første Maven-build

**Problem:** Første `mvn clean compile` eller `mvn package` tar lang tid (10-15 minutter)

**Årsak:** Maven trenger å laste ned alle prosjektavhengigheter (Spring Boot, LangChain4j-biblioteker, Azure SDK-er osv.) ved første bygging.

**Løsning:** Dette er normal oppførsel. Etterfølgende bygginger går mye raskere siden avhengighetene caches lokalt. Nedlastningstid avhenger av nettverkshastighet.

### PowerShell Maven-kommando syntaks

**Problem:** Maven-kommandoer feiler med feil `Unknown lifecycle phase ".mainClass=..."`
**Årsak**: PowerShell tolker `=` som en variabeltilordningsoperator, noe som bryter Maven-egenskapssyntaksen

**Løsning**: Bruk stop-parser-operatoren `--%` før Maven-kommandoen:

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

**Problem**: AI-svar viser søppelkarakterer (f.eks. `????` eller `â??`) i stedet for emojier i PowerShell

**Årsak**: PowerShells standardkoding støtter ikke UTF-8-emojier

**Løsning**: Kjør denne kommandoen før du kjører Java-applikasjoner:
```cmd
chcp 65001
```

Dette tvinger UTF-8-koding i terminalen. Alternativt kan du bruke Windows Terminal som har bedre Unicode-støtte.

### Feilsøking av API-kall

**Problem**: Autentiseringsfeil, grenseverdier, eller uventede svar fra AI-modellen

**Løsning**: Eksemplene inkluderer `.logRequests(true)` og `.logResponses(true)` for å vise API-kall i konsollen. Dette hjelper med å feilsøke autentiseringsfeil, grenseverdier eller uventede svar. Fjern disse flaggene i produksjon for å redusere loggstøy.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets opprinnelige språk bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->