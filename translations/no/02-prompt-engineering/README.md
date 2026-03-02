# Modul 02: Prompt Engineering med GPT-5.2

## Innholdsfortegnelse

- [Video Gjennomgang](../../../02-prompt-engineering)
- [Hva du vil lære](../../../02-prompt-engineering)
- [Forutsetninger](../../../02-prompt-engineering)
- [Forstå prompt engineering](../../../02-prompt-engineering)
- [Grunnleggende om prompt engineering](../../../02-prompt-engineering)
  - [Zero-Shot prompting](../../../02-prompt-engineering)
  - [Few-Shot prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollebasert prompting](../../../02-prompt-engineering)
  - [Prompt maler](../../../02-prompt-engineering)
- [Avanserte mønstre](../../../02-prompt-engineering)
- [Kjør applikasjonen](../../../02-prompt-engineering)
- [Skjermbilder fra applikasjonen](../../../02-prompt-engineering)
- [Utforske mønstrene](../../../02-prompt-engineering)
  - [Lav vs høy iver](../../../02-prompt-engineering)
  - [Oppgaveutførelse (Verktøy-preambler)](../../../02-prompt-engineering)
  - [Selvreflekterende kode](../../../02-prompt-engineering)
  - [Strukturert analyse](../../../02-prompt-engineering)
  - [Samtale med mange runder](../../../02-prompt-engineering)
  - [Trinnvis resonnering](../../../02-prompt-engineering)
  - [Begrenset output](../../../02-prompt-engineering)
- [Hva du egentlig lærer](../../../02-prompt-engineering)
- [Neste steg](../../../02-prompt-engineering)

## Video Gjennomgang

Se denne livesesjonen som forklarer hvordan du kommer i gang med denne modulen:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Hva du vil lære

Følgende diagram gir en oversikt over hovedtemaene og ferdighetene du vil utvikle i denne modulen — fra teknikker for promptforfining til den trinnvise arbeidsflyten du vil følge.

<img src="../../../translated_images/no/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I tidligere moduler har du utforsket grunnleggende LangChain4j-interaksjoner med GitHub-modeller og sett hvordan minne muliggjør konverserende AI med Azure OpenAI. Nå vil vi fokusere på hvordan du stiller spørsmål — selve promptene — ved å bruke Azure OpenAIs GPT-5.2. Måten du strukturerer promptene dine på påvirker dramatisk kvaliteten på svarene du får. Vi starter med en gjennomgang av grunnleggende promptingsteknikker, og går deretter videre til åtte avanserte mønstre som utnytter GPT-5.2 sine kapasiteter fullt ut.

Vi bruker GPT-5.2 fordi den introduserer kontroll over resonnementet – du kan fortelle modellen hvor mye den skal tenke før den svarer. Dette gjør ulike promptingstrategier mer tydelige og hjelper deg å forstå når du bør bruke hver tilnærming. Vi drar også nytte av Azures færre ratebegrensninger for GPT-5.2 sammenlignet med GitHub-modeller.

## Forutsetninger

- Fullført Modul 01 (Azure OpenAI-ressurser deployert)
- `.env`-fil i rotkatalogen med Azure-legitimasjon (opprettet med `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg deploy-instruksjonene der først.

## Forstå prompt engineering

I kjernen av prompt engineering ligger forskjellen mellom vage og presise instruksjoner, som illustrert i sammenligningen under.

<img src="../../../translated_images/no/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handler om å utforme inndata som konsekvent gir deg de resultatene du trenger. Det handler ikke bare om å stille spørsmål – det handler om å strukturere forespørsler slik at modellen forstår nøyaktig hva du ønsker og hvordan den skal levere det.

Tenk på det som å gi instrukser til en kollega. "Fiks feilen" er vagt. "Fiks null pointer exception i UserService.java linje 45 ved å legge til en null-sjekk" er spesifikt. Språkmodeller fungerer på samme måte – spesifisitet og struktur teller.

Diagrammet under viser hvordan LangChain4j passer inn i bildet — ved å koble dine prompt-mønstre til modellen gjennom byggeklossene SystemMessage og UserMessage.

<img src="../../../translated_images/no/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j tilbyr infrastrukturen — modellforbindelser, minne og meldingstyper — mens prompt-mønstrene bare er nøye strukturerte tekster du sender gjennom den infrastrukturen. De viktige byggeklossene er `SystemMessage` (som setter AIens oppførsel og rolle) og `UserMessage` (som bærer den faktiske forespørselen din).

## Grunnleggende om prompt engineering

De fem kjerne-teknikkene vist under danner fundamentet for effektiv prompt engineering. Hver av dem adresserer en forskjellig side av hvordan du kommuniserer med språkmodeller.

<img src="../../../translated_images/no/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Før vi går inn på de avanserte mønstrene i denne modulen, la oss se på fem grunnleggende promptingsteknikker. Dette er byggeklossene som enhver prompt-ingeniør bør kjenne til. Har du allerede jobbet gjennom [Rask start-modulen](../00-quick-start/README.md#2-prompt-patterns), har du sett disse i praksis – her er det konseptuelle rammeverket bak dem.

### Zero-Shot prompting

Den enkleste tilnærmingen: gi modellen en direkte instruksjon uten eksempler. Modellen baserer seg fullt ut på sin trening for å forstå og utføre oppgaven. Dette fungerer godt for enkle forespørsler der forventet oppførsel er åpenbar.

<img src="../../../translated_images/no/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruksjon uten eksempler — modellen utleder oppgaven kun fra instruksjonen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Når bruke:** Enkle klassifiseringer, direkte spørsmål, oversettelser eller enhver oppgave modellen kan håndtere uten ytterligere veiledning.

### Few-Shot prompting

Gi eksempler som demonstrerer mønsteret du ønsker modellen skal følge. Modellen lærer det forventede inn-data/ut-data-formatet fra eksemplene og anvender det på nye innspill. Dette forbedrer konsistensen dramatisk for oppgaver der ønsket format eller oppførsel ikke er innlysende.

<img src="../../../translated_images/no/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lære fra eksempler — modellen identifiserer mønsteret og bruker det på nye innspill*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Når bruke:** Tilpassede klassifiseringer, konsistent formatering, domene-spesifikke oppgaver, eller når zero-shot-resultater er inkonsistente.

### Chain of Thought

Be modellen vise sitt resonnement steg-for-steg. I stedet for å hoppe rett til et svar, bryter modellen ned problemet og jobber gjennom hvert delsteg eksplisitt. Dette øker nøyaktigheten på matematikk, logikk og komplekse resonneringsoppgaver.

<img src="../../../translated_images/no/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trinnvis resonnering — bryter komplekse problemer ned i eksplisitte logiske steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, deretter 7 + 12 = 19 epler
```

**Når bruke:** Matematikkoppgaver, logikkpuslespill, debugging, eller enhver oppgave der visning av resonnement prosessen øker nøyaktighet og tillit.

### Rollebasert prompting

Sett en persona eller rolle for AI før du stiller spørsmålet ditt. Dette gir kontekst som former tonen, dybden og fokuset i svaret. En "programvarearkitekt" gir annerledes råd enn en "juniorutvikler" eller en "sikkerhetsrevisor".

<img src="../../../translated_images/no/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setter kontekst og persona — samme spørsmål får ulik respons avhengig av tildelt rolle*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Når bruke:** Kodegjennomganger, veiledning, domene-spesifikk analyse, eller når du trenger svar skreddersydd til et bestemt ekspertisenivå eller perspektiv.

### Prompt maler

Lag gjenbrukbare prompts med variable plassholdere. I stedet for å skrive en ny prompt hver gang, definer en mal én gang og fyll inn forskjellige verdier. LangChain4js `PromptTemplate`-klasse gjør dette enkelt med `{{variable}}`-syntaks.

<img src="../../../translated_images/no/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Gjenbrukbare prompts med variable plassholdere — én mal, mange bruksområder*

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

**Når bruke:** Gjentatte henvendelser med ulike innspill, batch-prosessering, bygge gjenbrukbare AI-arbeidsflyter, eller enhver situasjon der prompt-strukturen er konstant men data endres.

---

Disse fem grunnleggende teknikkene gir deg et solid verktøysett for de fleste prompting-oppgaver. Resten av denne modulen bygger på dem med **åtte avanserte mønstre** som utnytter GPT-5.2 sin kontroll over resonnering, selvevaluering og strukturerte output-muligheter.

## Avanserte mønstre

Med de grunnleggende på plass, går vi videre til de åtte avanserte mønstrene som gjør denne modulen unik. Ikke alle problemer trenger samme tilnærming. Noen spørsmål trenger raske svar, andre trenger dyp tenking. Noen krever synlig resonnement, andre bare resultater. Hvert mønster under er optimalisert for et forskjellig scenario — og GPT-5.2 sin resonneringskontroll gjør forskjellene enda mer tydelige.

<img src="../../../translated_images/no/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Oversikt over de åtte promptengineering-mønstrene og deres bruksområder*

GPT-5.2 tilfører en ny dimensjon til disse mønstrene: *kontroll over resonnering*. Skyveknappen under viser hvordan du kan justere modellens tankeinnsats — fra raske, direkte svar til dyp, grundig analyse.

<img src="../../../translated_images/no/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 sin resonneringskontroll lar deg spesifisere hvor mye modellens tenkning skal være — fra raske direkte svar til dyp utforskning*

**Lav iver (Rask & fokusert)** - For enkle spørsmål der du vil ha raske, direkte svar. Modellen gjør minimal resonnement - maks 2 steg. Bruk dette til beregninger, oppslag eller enkle spørsmål.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Utforsk med GitHub Copilot:** Åpne [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) og spør:
> - "Hva er forskjellen mellom lav og høy iver i prompting-mønstre?"
> - "Hvordan hjelper XML-taggene i prompts til med å strukturere AI-ens svar?"
> - "Når bør jeg bruke selvrefleksjon-mønstre kontra direkte instruksjoner?"

**Høy iver (Dyp & grundig)** - For komplekse problemer der du ønsker omfattende analyse. Modellen utforsker grundig og viser detaljert resonnement. Bruk dette til systemdesign, arkitekturvalg eller omfattende forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Oppgaveutførelse (Trinnvis progresjon)** - For arbeidsflyter med flere trinn. Modellen gir en plan på forhånd, forteller om hvert trinn underveis, og gir til slutt en oppsummering. Bruk dette til migrasjoner, implementeringer eller øvrige flertrinnsprosesser.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting ber uttrykkelig modellen om å vise sitt resonnement, noe som øker nøyaktigheten for komplekse oppgaver. Den trinnvise oppdelingen hjelper både mennesker og AI til å forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om dette mønsteret:
> - "Hvordan kan jeg tilpasse oppgaveutførelsesmønsteret for langvarige operasjoner?"
> - "Hva er beste praksis for å strukturere verktøy-preambler i produksjonsapplikasjoner?"
> - "Hvordan kan jeg fange og vise mellomliggende fremdriftsoppdateringer i et brukergrensesnitt?"

Diagrammet under illustrerer denne Plan → Utfør → Oppsummer arbeidsflyten.

<img src="../../../translated_images/no/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Utfør → Oppsummer arbeidsflyt for flertrinnsoppgaver*

**Selvreflekterende kode** - For å generere produksjonsklar kode. Modellen genererer kode i henhold til produksjonsstandarder med korrekt feilhåndtering. Bruk dette ved utvikling av nye funksjoner eller tjenester.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Diagrammet under viser denne iterative forbedringssløyfen — generer, evaluer, identifiser svakheter, og forbedre til koden møter produksjonsstandarder.

<img src="../../../translated_images/no/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ forbedringssløyfe - generer, evaluer, identifiser problemer, forbedre, gjenta*

**Strukturert analyse** - For konsistent evaluering. Modellen gjennomgår kode med et fast rammeverk (korrrekthet, praksis, ytelse, sikkerhet, vedlikeholdbarhet). Bruk dette til kodegjennomganger eller kvalitetsvurderinger.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om strukturert analyse:
> - "Hvordan kan jeg tilpasse analyse-rammeverket for ulike typer kodegjennomganger?"
> - "Hva er beste måten å analysere og handle på strukturert output programmessig?"
> - "Hvordan sikrer jeg konsistente alvorlighetsgrader på tvers av ulike gjennomgangssessioner?"

Diagrammet under viser hvordan dette strukturerte rammeverket organiserer en kodegjennomgang i konsistente kategorier med alvorlighetsnivåer.

<img src="../../../translated_images/no/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Rammeverk for konsistente kodegjennomganger med alvorlighetsnivåer*

**Samtale med mange runder** - For samtaler som trenger kontekst. Modellen husker tidligere meldinger og bygger videre på dem. Bruk dette til interaktive hjelpesesjoner eller komplekse spørsmål og svar.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Diagrammet under visualiserer hvordan samtalekontekst akkumuleres med hver runde og hvordan det relaterer til modellens token-grense.

<img src="../../../translated_images/no/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hvordan samtalekontekst akkumuleres over flere runder til token-grensen nås*
**Trinnvis resonnement** - For problemer som krever synlig logikk. Modellen viser eksplisitt resonnement for hvert trinn. Bruk dette for matematikkoppgaver, logikkpuslespill eller når du trenger å forstå tankegangen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagrammet nedenfor illustrerer hvordan modellen bryter ned problemer i eksplisitte, nummererte logiske trinn.

<img src="../../../translated_images/no/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Nedbryting av problemer i eksplisitte logiske trinn*

**Begrenset utdata** - For svar med spesifikke formatkrav. Modellen følger strengt format- og lengderegler. Bruk dette for sammendrag eller når du trenger presis outputstruktur.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

Følgende diagram viser hvordan begrensninger styrer modellen til å produsere utdata som strengt følger dine format- og lengdekrav.

<img src="../../../translated_images/no/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Håndhever spesifikke krav til format, lengde og struktur*

## Kjør applikasjonen

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (laget under Modul 01). Kjør dette fra modulkatalogen (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede har startet alle applikasjoner med `./start-all.sh` fra rotkatalogen (som beskrevet i Modul 01), kjører denne modulen allerede på port 8083. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8083.

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Dev-containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i Aktivitetslinjen på venstre side av VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stanse applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk enkelt på play-knappen ved siden av "prompt-engineering" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stopp og overvåk alle moduler fra ett sted*

**Alternativ 2: Bruke shell-skript**

Start alle nettapplikasjoner (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rotkatalog
.\start-all.ps1
```

Eller start kun denne modulen:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og bygger JAR-filene om de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før oppstart:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Åpne http://localhost:8083 i nettleseren din.

**For å stoppe:**

**Bash:**
```bash
./stop.sh  # Denne modulen kun
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Bare denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Applikasjonsskjermbilder

Her er hovedgrensesnittet til prompt engineering-modulen, hvor du kan eksperimentere med alle åtte mønstrene side om side.

<img src="../../../translated_images/no/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashbord som viser alle 8 prompt engineering-mønstre med deres egenskaper og bruksområder*

## Utforske mønstrene

Nettgrensesnittet lar deg eksperimentere med ulike prompting-strategier. Hvert mønster løser forskjellige problemer – prøv dem for å se når hver tilnærming fungerer best.

> **Merk: Streaming vs Ikke-streaming** — Hver mønsterside tilbyr to knapper: **🔴 Stream Response (Live)** og et **Ikke-streaming** alternativ. Streaming bruker Server-Sent Events (SSE) for å vise tokens i sanntid mens modellen genererer dem, slik at du ser fremdriften umiddelbart. Ikke-streaming venter på hele svaret før det vises. For forespørsler som utløser dypt resonnement (f.eks. High Eagerness, Self-Reflecting Code), kan ikke-streaming-kallet ta veldig lang tid — noen ganger minutter — uten synlig tilbakemelding. **Bruk streaming når du eksperimenterer med komplekse prompts** så du kan se modellen jobbe og unngå å få inntrykk av at forespørselen har gått ut på tid.
>
> **Merk: Nettleserkrav** — Streaming-funksjonen bruker Fetch Streams API (`response.body.getReader()`) som krever en full nettleser (Chrome, Edge, Firefox, Safari). Det fungerer **ikke** i VS Codes innebygde Simple Browser, da webviewen ikke støtter ReadableStream API. Hvis du bruker Simple Browser, vil ikke-streaming-knappene fortsatt fungere som normalt — bare streaming-knappene påvirkes. Åpne `http://localhost:8083` i en ekstern nettleser for full opplevelse.

### Lav vs Høy entusiasme

Still et enkelt spørsmål som "Hva er 15 % av 200?" med Lav entusiasme. Du får et raskt, direkte svar. Still så noe komplisert som "Design en caching-strategi for et høytrafikkert API" med Høy entusiasme. Klikk **🔴 Stream Response (Live)** og se modellens detaljerte resonnement komme token for token. Samme modell, samme spørsmålsstruktur – men prompten forteller hvor mye tenking som skal gjøres.

### Oppgaveutførelse (Verktøypreambler)

Flerskritt arbeidsflyter drar nytte av forhåndsplanlegging og progresjonsfortelling. Modellen skisserer hva den skal gjøre, forteller hvert trinn, og oppsummerer deretter resultatene.

### Selvreflekterende kode

Prøv "Lag en e-postvalideringstjeneste". I stedet for bare å generere kode og stoppe, genererer modellen, vurderer den opp mot kvalitetskriterier, identifiserer svakheter og forbedrer. Du ser at den itererer til koden møter produksjonsstandarder.

### Strukturert analyse

Kodegjennomganger trenger konsekvente evalueringsrammer. Modellen analyserer kode med faste kategorier (riktighet, praksis, ytelse, sikkerhet) med alvorlighetsnivåer.

### Fler-trinns chat

Spør "Hva er Spring Boot?" og følg straks opp med "Vis meg et eksempel". Modellen husker ditt første spørsmål og gir deg et Spring Boot-eksempel spesifikt. Uten hukommelse ville det andre spørsmålet vært for vagt.

### Trinnvis resonnement

Velg et mattespørsmål og prøv det med både Trinnvis resonnement og Lav entusiasme. Lav entusiasme gir deg bare svaret – raskt men uklart. Trinnvis viser hver beregning og avgjørelse.

### Begrenset utdata

Når du trenger spesifikke formater eller ordtall, håndhever dette mønsteret streng etterlevelse. Prøv å generere et sammendrag med nøyaktig 100 ord i punktlisteformat.

## Hva du virkelig lærer

**Resonnementets innsats endrer alt**

GPT-5.2 lar deg styre beregningsinnsatsen gjennom promptene dine. Lav innsats betyr raske svar med minimal utforskning. Høy innsats betyr at modellen tar seg tid til å tenke dypt. Du lærer å matche innsats med oppgavens kompleksitet – ikke kast bort tid på enkle spørsmål, men ikke skyv gjennom komplekse avgjørelser heller.

**Struktur styrer atferd**

Legg merke til XML-taggene i promptene? De er ikke dekorative. Modeller følger strukturerte instrukser mer pålitelig enn fri tekst. Når du trenger flertrinnsprosesser eller kompleks logikk, hjelper struktur modellen å holde styr på hvor den er og hva som kommer neste. Diagrammet nedenfor bryter ned en velstrukturert prompt, og viser hvordan tagger som `<system>`, `<instructions>`, `<context>`, `<user-input>`, og `<constraints>` organiserer instruksjonene dine i klare seksjoner.

<img src="../../../translated_images/no/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en velstrukturert prompt med klare seksjoner og XML-stil organisering*

**Kvalitet gjennom selvevaluering**

De selvreflekterende mønstrene fungerer ved å gjøre kvalitetskriterier eksplisitte. I stedet for å håpe modellen "gjør det riktig", forteller du nøyaktig hva "riktig" betyr: korrekt logikk, feilhåndtering, ytelse, sikkerhet. Modellen kan så evaluere sitt eget output og forbedre seg. Dette forvandler kodegenerering fra et lotteri til en prosess.

**Kontekst er begrenset**

Flertrinnssamtaler fungerer ved å inkludere meldingshistorikk med hver forespørsel. Men det finnes en grense – hver modell har et maksimalt antall tokens. Når samtaler vokser, trenger du strategier for å beholde relevant kontekst uten å nå denne grensen. Denne modulen viser deg hvordan minne fungerer; senere lærer du når du skal oppsummere, når du skal glemme, og når du skal hente.

## Neste steg

**Neste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasjon:** [← Forrige: Modul 01 - Introduksjon](../01-introduction/README.md) | [Tilbake til hovedmeny](../README.md) | [Neste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på det opprinnelige språket bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->