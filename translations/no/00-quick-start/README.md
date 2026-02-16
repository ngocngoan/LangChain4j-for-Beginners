# Modul 00: Rask Start

## Innholdsfortegnelse

- [Introduksjon](../../../00-quick-start)
- [Hva er LangChain4j?](../../../00-quick-start)
- [LangChain4j Avhengigheter](../../../00-quick-start)
- [Forutsetninger](../../../00-quick-start)
- [Oppsett](../../../00-quick-start)
  - [1. Få Din GitHub Token](../../../00-quick-start)
  - [2. Sett Din Token](../../../00-quick-start)
- [Kjør Eksemplene](../../../00-quick-start)
  - [1. Grunnleggende Chat](../../../00-quick-start)
  - [2. Promptmønstre](../../../00-quick-start)
  - [3. Funksjonskalling](../../../00-quick-start)
  - [4. Dokument Q&A (RAG)](../../../00-quick-start)
  - [5. Ansvarlig AI](../../../00-quick-start)
- [Hva Hver Eksempel Viser](../../../00-quick-start)
- [Neste Steg](../../../00-quick-start)
- [Feilsøking](../../../00-quick-start)

## Introduksjon

Denne raskstarten er ment å få deg i gang med LangChain4j så raskt som mulig. Den dekker det mest grunnleggende for å bygge AI-applikasjoner med LangChain4j og GitHub-modeller. I de neste modulene vil du bruke Azure OpenAI med LangChain4j for å bygge mer avanserte applikasjoner.

## Hva er LangChain4j?

LangChain4j er et Java-bibliotek som gjør det enklere å bygge AI-drevne applikasjoner. I stedet for å håndtere HTTP-klienter og JSON-parsing, jobber du med rene Java-APIer.

«Chain»-delen i LangChain refererer til å koble sammen flere komponenter – du kan koble en prompt til en modell til en parser, eller koble sammen flere AI-kall der én output mates inn som input i neste. Denne raskstarten fokuserer på grunnleggende konsepter før vi utforsker mer komplekse kjeder.

<img src="../../../translated_images/no/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kobling av komponenter i LangChain4j – byggeklosser kobles sammen for å skape kraftige AI-arbeidsflyter*

Vi bruker tre kjernekomponenter:

**ChatLanguageModel** – Grensesnittet for AI-modellinteraksjoner. Kall `model.chat("prompt")` og få tilbake en responsstreng. Vi bruker `OpenAiOfficialChatModel` som fungerer med OpenAI-kompatible endepunkter som GitHub Models.

**AiServices** – Lager typer-sikre AI-tjenestegrensesnitt. Definer metoder, merk dem med `@Tool`, og LangChain4j håndterer orkestreringen. AI-en kaller automatisk dine Java-metoder ved behov.

**MessageWindowChatMemory** – Opprettholder samtalehistorikk. Uten dette er hver forespørsel uavhengig. Med det husker AI tidligere meldinger og opprettholder kontekst over flere runder.

<img src="../../../translated_images/no/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arkitektur – kjernekomponenter som jobber sammen for å drive AI-applikasjonene dine*

## LangChain4j Avhengigheter

Denne raskstarten bruker to Maven-avhengigheter i [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
Modulen `langchain4j-open-ai-official` tilbyr `OpenAiOfficialChatModel`-klassen som kobler til OpenAI-kompatible APIer. GitHub Models bruker samme API-format, så ingen spesiell adapter trengs – bare pek base-URL til `https://models.github.ai/inference`.

## Forutsetninger

**Bruker du Dev Container?** Java og Maven er allerede installert. Du trenger kun en personlig tilgangstoken fra GitHub.

**Lokal utvikling:**  
- Java 21+, Maven 3.9+  
- GitHub personlig tilgangstoken (instruksjoner under)

> **Merk:** Denne modulen bruker `gpt-4.1-nano` fra GitHub Models. Ikke endre modellnavnet i koden – det er konfigurert for å fungere med GitHubs tilgjengelige modeller.

## Oppsett

### 1. Få Din GitHub Token

1. Gå til [GitHub Innstillinger → Personal Access Tokens](https://github.com/settings/personal-access-tokens)  
2. Klikk "Generate new token"  
3. Sett et beskrivende navn (f.eks. "LangChain4j Demo")  
4. Sett utløpstid (7 dager anbefales)  
5. Under "Account permissions", finn "Models" og sett til "Read-only"  
6. Klikk "Generate token"  
7. Kopier og lagre token – du får ikke se den igjen

### 2. Sett Din Token

**Alternativ 1: Bruke VS Code (Anbefalt)**

Hvis du bruker VS Code, legg token i `.env`-filen i prosjektets rotmappe:

Hvis `.env`-filen ikke finnes, kopier `.env.example` til `.env` eller opprett en ny `.env`-fil i prosjektrot.

**Eksempel på `.env`-fil:**  
```bash
# I /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Deretter kan du høyreklikke på hvilken som helst demo-fil (f.eks. `BasicChatDemo.java`) i Explorer og velge **"Run Java"** eller bruke launch-konfigurasjoner fra Run and Debug-panelet.

**Alternativ 2: Bruke Terminal**

Sett token som et miljøvariabel:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  
## Kjør Eksemplene

**Bruke VS Code:** Høyreklikk på en demo-fil i Explorer og velg **"Run Java"**, eller bruk launch-konfigurasjonene i Run and Debug-panelet (pass på at token er lagt til i `.env`-filen først).

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
  
### 2. Promptmønstre

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
Viser zero-shot, few-shot, chain-of-thought og rollebasert prompting.

### 3. Funksjonskalling

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
AI-en kaller automatisk dine Java-metoder ved behov.

### 4. Dokument Q&A (RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Still spørsmål om innhold i `document.txt`.

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

## Hva Hver Eksempel Viser

**Grunnleggende Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Start her for å se LangChain4j på sitt enkleste. Du lager en `OpenAiOfficialChatModel`, sender en prompt med `.chat()`, og får tilbake et svar. Dette demonstrerer grunnlaget: hvordan man initialiserer modeller med egendefinerte endepunkter og API-nøkler. Når du skjønner dette mønsteret, bygger alt annet på det.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
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
> - "Hvordan legger jeg til streaming av svar i stedet for å vente på hele responsen?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nå som du vet hvordan du snakker til en modell, la oss utforske hva du sier til den. Denne demoen bruker samme modelloppsett, men viser fem forskjellige promptmønstre. Prøv zero-shot-prompt for direkte instruksjoner, few-shot-prompt som lærer fra eksempler, chain-of-thought-prompt som viser resonnementstrinn, og rollebasert prompt som setter kontekst. Du vil se hvordan samme modell gir dramatisk forskjellige resultater basert på hvordan du rammer inn forespørselen.

Demoen viser også promptmaler, som er en kraftfull måte å lage gjenbrukbare prompts med variabler. Eksempelet under viser en prompt som bruker LangChain4j `PromptTemplate` for å fylle inn variabler. AI-en svarer basert på oppgitt destinasjon og aktivitet.

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
> - "Hva er forskjellen mellom zero-shot og few-shot prompting, og når bør jeg bruke hver?"  
> - "Hvordan påvirker temperaturparameteren modellens svar?"  
> - "Hva er noen teknikker for å forhindre prompt-injeksjonsangrep i produksjon?"  
> - "Hvordan kan jeg lage gjenbrukbare PromptTemplate-objekter for vanlige mønstre?"

**Verktøyintegrasjon** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Her blir LangChain4j kraftig. Du bruker `AiServices` for å lage en AI-assistent som kan kalle dine Java-metoder. Merk bare metoder med `@Tool("beskrivelse")` og LangChain4j håndterer resten – AI-en bestemmer automatisk når hvert verktøy skal brukes basert på hva brukeren spør om. Dette demonstrerer funksjonskalling, en nøkkelteknikk for å bygge AI som kan utføre handlinger, ikke bare svare på spørsmål.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```
  
> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) og spør:  
> - "Hvordan fungerer @Tool-annotasjonen og hva gjør LangChain4j med den bak kulissene?"  
> - "Kan AI kalle flere verktøy i sekvens for å løse komplekse problemer?"  
> - "Hva skjer hvis et verktøy kaster en unntak - hvordan bør jeg håndtere feil?"  
> - "Hvordan integrerer jeg et ekte API i stedet for dette kalkulator-eksemplet?"

**Dokument Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Her ser du grunnlaget for RAG (retrieval-augmented generation). I stedet for å stole på modellens treningsdata, laster du inn innhold fra [`document.txt`](../../../00-quick-start/document.txt) og inkluderer det i prompten. AI-en svarer basert på dokumentet ditt, ikke sin generelle kunnskap. Dette er første steg mot å bygge systemer som kan jobbe med egne data.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```
  
> **Merk:** Denne enkle tilnærmingen laster hele dokumentet inn i prompten. For store filer (>10KB) vil du overskride kontekstgrenser. Modul 03 dekker innholdsdeling (chunking) og vektorsøk for produksjonssystemer med RAG.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) og spør:  
> - "Hvordan forhindrer RAG AI-hallusinasjoner sammenlignet med å bruke modellens treningsdata?"  
> - "Hva er forskjellen mellom denne enkle tilnærmingen og å bruke vektorembeddinger for henting?"  
> - "Hvordan kan jeg skalere dette for å håndtere flere dokumenter eller større kunnskapsbaser?"  
> - "Hva er beste praksis for å strukturere prompten slik at AI kun bruker oppgitt kontekst?"

**Ansvarlig AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bygg AI-sikkerhet med forsvar i dybden. Denne demoen viser to lag med beskyttelse som jobber sammen:

**Del 1: LangChain4j Input Guardrails** – Blokkerer farlige prompts før de når LLM. Lag egendefinerte guardrails som sjekker for forbudte nøkkelord eller mønstre. Disse kjører i koden din, så de er raske og gratis.

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
  
**Del 2: Provider Safety Filters** – GitHub Models har innebygde filtre som fanger opp det som guardrails kan overse. Du vil se harde blokker (HTTP 400-feil) for alvorlige brudd og myke refusjoner hvor AI høflig avslår.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) og spør:  
> - "Hva er InputGuardrail og hvordan lager jeg mine egne?"  
> - "Hva er forskjellen på hard blokkering og myk avvisning?"  
> - "Hvorfor bruke både guardrails og provider-filtre sammen?"

## Neste Steg

**Neste Modul:** [01-introduction - Komme i gang med LangChain4j og gpt-5 på Azure](../01-introduction/README.md)

---

**Navigasjon:** [← Tilbake til Hoved](../README.md) | [Neste: Modul 01 - Introduksjon →](../01-introduction/README.md)

---

## Feilsøking

### Første-gangs Maven Bygg

**Problem:** Første `mvn clean compile` eller `mvn package` tar lang tid (10-15 minutter)

**Årsak:** Maven må laste ned alle prosjektavhengigheter (Spring Boot, LangChain4j-biblioteker, Azure SDK-er, osv.) ved første bygg.

**Løsning:** Dette er normalt. Etterfølgende bygg blir mye raskere fordi avhengighetene er bufret lokalt. Nedlastningstiden avhenger av nettverkshastigheten din.
### PowerShell Maven-kommandosyntaks

**Problem**: Maven-kommandoer feiler med feilen `Unknown lifecycle phase ".mainClass=..."`

**Årsak**: PowerShell tolker `=` som en variabeltildelingsoperator, noe som ødelegger Maven-egenskapssyntaksen

**Løsning**: Bruk stopp-parsing-operatoren `--%` før Maven-kommandoen:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatoren `--%` forteller PowerShell å sende alle resterende argumenter bokstavelig til Maven uten tolkning.

### Windows PowerShell Emoji-visning

**Problem**: AI-svar viser søppelkarakterer (f.eks. `????` eller `â??`) i stedet for emojis i PowerShell

**Årsak**: PowerShells standardkoding støtter ikke UTF-8-emojier

**Løsning**: Kjør denne kommandoen før du kjører Java-applikasjoner:
```cmd
chcp 65001
```

Dette tvinger UTF-8-koding i terminalen. Alternativt kan du bruke Windows Terminal som har bedre Unicode-støtte.

### Feilsøking av API-kall

**Problem**: Autentiseringsfeil, grenseverdier eller uventede svar fra AI-modellen

**Løsning**: Eksemplene inkluderer `.logRequests(true)` og `.logResponses(true)` for å vise API-kall i konsollen. Dette hjelper med å feilsøke autentiseringsfeil, grenseverdier eller uventede svar. Fjern disse flaggene i produksjon for å redusere loggstøy.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som følge av bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->