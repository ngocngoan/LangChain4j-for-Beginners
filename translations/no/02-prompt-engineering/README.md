# Modul 02: Prompt Engineering med GPT-5.2

## Innholdsfortegnelse

- [Hva du vil lære](../../../02-prompt-engineering)
- [Forutsetninger](../../../02-prompt-engineering)
- [Forståelse av Prompt Engineering](../../../02-prompt-engineering)
- [Grunnleggende for Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Avanserte Mønstre](../../../02-prompt-engineering)
- [Bruke eksisterende Azure-ressurser](../../../02-prompt-engineering)
- [Applikasjonsskjermbilder](../../../02-prompt-engineering)
- [Utforske mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høy Iver](../../../02-prompt-engineering)
  - [Oppgaveutførelse (verktøypreambler)](../../../02-prompt-engineering)
  - [Selvreflekterende kode](../../../02-prompt-engineering)
  - [Strukturert analyse](../../../02-prompt-engineering)
  - [Flertur-chatt](../../../02-prompt-engineering)
  - [Trinnvis resonnering](../../../02-prompt-engineering)
  - [Begrenset utdata](../../../02-prompt-engineering)
- [Hva du egentlig lærer](../../../02-prompt-engineering)
- [Neste steg](../../../02-prompt-engineering)

## Hva du vil lære

<img src="../../../translated_images/no/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I forrige modul så du hvordan minne muliggjør samtale-AI og brukte GitHub-modeller for grunnleggende interaksjoner. Nå skal vi fokusere på hvordan du stiller spørsmål — selve promptene — ved bruk av Azure OpenAIs GPT-5.2. Måten du strukturerer promptene dine på påvirker dramatisk kvaliteten på svarene du får. Vi begynner med en gjennomgang av grunnleggende promptingsteknikker, og beveger oss deretter inn i åtte avanserte mønstre som utnytter GPT-5.2 sine muligheter fullt ut.

Vi bruker GPT-5.2 fordi den introduserer kontroll over resonnering - du kan fortelle modellen hvor mye tenking den skal gjøre før den svarer. Dette gjør ulike promptingstrategier mer tydelige og hjelper deg å forstå når hver tilnærming bør brukes. Vi vil også dra nytte av at Azure har færre begrensninger per ratenivå for GPT-5.2 sammenlignet med GitHub-modeller.

## Forutsetninger

- Fullførte Modul 01 (Azure OpenAI-ressurser distribuert)
- `.env`-fil i rotkatalogen med Azure-legitimasjon (opprettet av `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg distribusjonsinstruksjonene der først.

## Forståelse av Prompt Engineering

<img src="../../../translated_images/no/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Hva er Prompt Engineering?" width="800"/>

Prompt engineering handler om å designe inputtekst som konsekvent gir deg resultatene du trenger. Det handler ikke bare om å stille spørsmål — det handler om å strukturere forespørsler slik at modellen forstår nøyaktig hva du vil ha og hvordan det skal leveres.

Tenk på det som å gi instruksjoner til en kollega. "Fiks feilen" er vagt. "Fiks nullpekerunntaket i UserService.java linje 45 ved å legge til en nullsjekk" er spesifikt. Språkmodeller fungerer på samme måte — presisjon og struktur er viktige.

<img src="../../../translated_images/no/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hvordan LangChain4j passer inn" width="800"/>

LangChain4j gir infrastrukturen — modellkoblinger, minne og meldingsformer — mens promptmønstre bare er tekst som er nøye strukturert og sendes gjennom infrastrukturen. Nøkkelbyggesteinene er `SystemMessage` (som setter AI-ens atferd og rolle) og `UserMessage` (som bærer din faktiske forespørsel).

## Grunnleggende for Prompt Engineering

<img src="../../../translated_images/no/five-patterns-overview.160f35045ffd2a94.webp" alt="Oversikt over fem mønstre for prompt engineering" width="800"/>

Før vi dykker ned i de avanserte mønstrene i denne modulen, la oss gå gjennom fem grunnleggende promptingsteknikker. Disse er byggesteinene enhver promptingeniør bør kjenne til. Hvis du allerede har jobbet deg gjennom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns), har du sett disse i praksis — her er det konseptuelle rammeverket bak dem.

### Zero-Shot Prompting

Den enkleste tilnærmingen: gi modellen en direkte instruksjon uten eksempler. Modellen stoler helt på sin trening for å forstå og utføre oppgaven. Dette fungerer godt for enkle forespørsler der forventet atferd er åpenbar.

<img src="../../../translated_images/no/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruksjon uten eksempler — modellen utleder oppgaven utelukkende fra instruksjonen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Når bruke:** Enkle klassifiseringer, direkte spørsmål, oversettelser eller enhver oppgave modellen kan håndtere uten ekstra veiledning.

### Few-Shot Prompting

Gi eksempler som demonstrerer mønsteret du vil at modellen skal følge. Modellen lærer forventet input-output format fra eksemplene dine og anvender det på nye inndata. Dette forbedrer konsistensen betydelig for oppgaver hvor ønsket format eller oppførsel ikke er åpenbart.

<img src="../../../translated_images/no/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Læring fra eksempler — modellen identifiserer mønsteret og bruker det på nye inndata*

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

**Når bruke:** Tilpassede klassifiseringer, konsekvent formatering, domene-spesifikke oppgaver eller når zero-shot-resultater er inkonsistente.

### Chain of Thought

Be modellen vise sitt resonneringssteg for steg. I stedet for å hoppe rett til et svar, bryter modellen ned problemet og arbeider gjennom hvert delsteg eksplisitt. Dette forbedrer nøyaktigheten på matte, logikk og flertrinns resonneringsoppgaver.

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

**Når bruke:** Matteoppgaver, logikkpuzzle, feilsøking, eller enhver oppgave hvor å vise resonneringsprosessen forbedrer nøyaktighet og tillit.

### Role-Based Prompting

Sett en persona eller rolle for AI-en før du stiller spørsmålet ditt. Dette gir kontekst som former tonen, dybden og fokuset i svaret. En "programvarearkitekt" gir ulik rådgivning sammenlignet med en "juniorutvikler" eller en "sikkerhetsrevisor".

<img src="../../../translated_images/no/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setter kontekst og persona — det samme spørsmålet får ulikt svar basert på den tildelte rollen*

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

**Når bruke:** Kodegjennomganger, veiledning, domene-spesifikk analyse, eller når du trenger svar tilpasset et bestemt ekspertisenivå eller perspektiv.

### Prompt Templates

Lag gjenbrukbare prompts med variabelplassholdere. I stedet for å skrive en ny prompt hver gang, definer en mal én gang og fyll inn ulike verdier. LangChain4js `PromptTemplate`-klasse gjør dette enkelt med `{{variable}}` syntaks.

<img src="../../../translated_images/no/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Gjenbrukbare prompts med variabelplassholdere — én mal, mange bruksområder*

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

**Når bruke:** Gjentatte forespørsler med ulik input, batch-prosessering, bygge gjenbrukbare AI-arbeidsflyter, eller enhver situasjon hvor prompt-strukturen forblir den samme men dataene endres.

---

Disse fem grunnleggende gir deg et solid verktøysett for de fleste promptingoppgaver. Resten av denne modulen bygger videre på dem med **åtte avanserte mønstre** som utnytter GPT-5.2 sin kontroll på resonnering, selvevaluering og strukturerte produksjoner.

## Avanserte Mønstre

Med det grunnleggende på plass, går vi videre til åtte avanserte mønstre som gjør denne modulen unik. Ikke alle problemer trenger samme tilnærming. Noen spørsmål krever raske svar, andre dyp tenkning. Noen trenger synlig resonnering, andre bare resultater. Hvert mønster nedenfor er optimalisert for en ulik situasjon — og GPT-5.2 sin resonneringskontroll gjør forskjellene enda tydeligere.

<img src="../../../translated_images/no/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtte prompt engineering mønstre" width="800"/>

*Oversikt over de åtte mønstrene for prompt engineering og deres brukstilfeller*

<img src="../../../translated_images/no/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Resonneringskontroll med GPT-5.2" width="800"/>

*GPT-5.2 sin resonneringskontroll lar deg spesifisere hvor mye tenking modellen skal gjøre — fra raske direkte svar til dyp utforskning*

<img src="../../../translated_images/no/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Sammenligning av resonneringsinnsats" width="800"/>

*Lav iver (rask, direkte) vs Høy iver (grundig, utforskende) resonneringstilnærminger*

**Lav Iver (Rask & Fokusert)** - For enkle spørsmål hvor du ønsker raske, direkte svar. Modellen gjør minimal resonnering - maksimum 2 trinn. Bruk dette for kalkulasjoner, oppslag eller enkle spørsmål.

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
> - "Hva er forskjellen mellom lav iver og høy iver i promptmønstre?"
> - "Hvordan hjelper XML-tagene i promptene til med å strukturere AI-ens svar?"
> - "Når bør jeg bruke selvrefleksjonsmønstre vs direkte instruksjon?"

**Høy Iver (Dyp & Grundig)** - For komplekse problemer hvor du ønsker omfattende analyse. Modellen utforsker grundig og viser detaljert resonnering. Bruk dette til systemdesign, arkitekturvalg eller kompleks forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Oppgaveutførelse (Trinnvis framdrift)** - For flertrinns arbeidsflyter. Modellen gir en innledende plan, forteller hvert steg mens den jobber, og gir til slutt en oppsummering. Bruk dette ved migrasjoner, implementasjoner eller enhver flertrinnsprosess.

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

Chain-of-Thought prompting ber eksplisitt modellen vise sin resonneringsprosess, noe som forbedrer nøyaktigheten for komplekse oppgaver. Trinnvis nedbrytning hjelper både mennesker og AI å forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om dette mønsteret:
> - "Hvordan kan jeg tilpasse oppgaveutførelsesmønsteret for langvarige operasjoner?"
> - "Hva er beste praksis for å strukturere verktøypreambler i produksjonsapplikasjoner?"
> - "Hvordan kan jeg fange og vise mellomliggende fremdriftsoppdateringer i en UI?"

<img src="../../../translated_images/no/task-execution-pattern.9da3967750ab5c1e.webp" alt="Oppgaveutførelsesmønster" width="800"/>

*Planlegg → Utfør → Oppsummer arbeidsflyt for flertrinnsoppgaver*

**Selvreflekterende kode** - For å generere kode av produksjonskvalitet. Modellen genererer kode i henhold til produksjonsstandarder med korrekt feilbehandling. Bruk dette når du bygger nye funksjoner eller tjenester.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selvrefleksjonssyklus" width="800"/>

*Iterativ forbedringssløyfe - generer, vurder, identifiser problemer, forbedre, gjenta*

**Strukturert analyse** - For konsistent evaluering. Modellen gjennomgår kode ved hjelp av et fast rammeverk (korrekthet, praksis, ytelse, sikkerhet, vedlikeholdbarhet). Bruk dette til kodegjennomganger eller kvalitetssikring.

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
> - "Hva er beste måte å parse og handle på strukturert output programmatisk?"
> - "Hvordan sikrer jeg konsekvente alvorlighetsgrader på tvers av forskjellige gjennomgangssesjoner?"

<img src="../../../translated_images/no/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturert analysemønster" width="800"/>

*Rammeverk for konsistente kodegjennomganger med alvorlighetsnivåer*

**Flertur-chatt** - For samtaler som trenger kontekst. Modellen husker tidligere meldinger og bygger videre på dem. Bruk dette til interaktive hjelpesesjoner eller komplekse spørsmål og svar.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/no/context-memory.dff30ad9fa78832a.webp" alt="Kontekstminne" width="800"/>

*Hvordan samtalekontekst akkumuleres over flere runder til token-grense nås*

**Trinnvis resonnering** - For problemer som krever synlig logikk. Modellen viser eksplisitt resonnering for hvert trinn. Bruk dette til matteoppgaver, logikkpuzzle, eller når du trenger å forstå tenkeprosessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Trinnvis resonneringsmønster" width="800"/>

*Bryter ned problemer i eksplisitte logiske steg*

**Begrenset utdata** - For svar med spesifikke formatkrav. Modellen følger nøye regler om format og lengde. Bruk dette for sammendrag eller når du trenger presis utdatastruktur.

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

<img src="../../../translated_images/no/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Begrenset utdata-mønster" width="800"/>

*Håndhever spesifikke krav til format, lengde og struktur*

## Bruke eksisterende Azure-ressurser

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (opprettet under Modul 01):
```bash
cat ../.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DISTRIBUSJON
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede har startet alle applikasjoner med `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8083. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8083.

**Alternativ 1: Bruke Spring Boot Dashboard (anbefalt for VS Code-brukere)**

Dev-containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å håndtere alle Spring Boot-applikasjoner. Du finner den i aktivitetsfeltet på venstre side av VS Code (se etter Spring Boot-ikonet).
Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med et enkelt klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk bare på spill-knappen ved siden av "prompt-engineering" for å starte denne modulen, eller start alle modulene samtidig.

<img src="../../../translated_images/no/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rotkatalogen
.\start-all.ps1
```

Eller start bare denne modulen:

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

Begge skriptene laster automatisk miljøvariabler fra rot `.env`-filen og bygger JAR-ene hvis de ikke eksisterer.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før du starter:
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

Åpne http://localhost:8083 i nettleseren din.

**For å stoppe:**

**Bash:**
```bash
./stop.sh  # Kun denne modulen
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Applikasjonsskjermbilder

<img src="../../../translated_images/no/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashbordet som viser alle 8 prompt engineering-mønstre med deres egenskaper og bruksområder*

## Utforske mønstrene

Nettgrensesnittet lar deg eksperimentere med forskjellige prompting-strategier. Hvert mønster løser forskjellige problemer - prøv dem for å se når hver tilnærming er best.

### Lav vs Høy Iver

Still et enkelt spørsmål som "Hva er 15 % av 200?" med Lav Iver. Du får et øyeblikkelig, direkte svar. Nå spør noe komplekst som "Design en caching-strategi for en API med høy trafikk" med Høy Iver. Se hvordan modellen senker tempoet og gir detaljert resonnering. Samme modell, samme spørsmålsstruktur - men prompten forteller hvor mye tenking som skal til.

<img src="../../../translated_images/no/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rask utregning med minimal resonnering*

<img src="../../../translated_images/no/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende caching-strategi (2.8MB)*

### Oppgaveutførelse (Verktøy Preambler)

Flere-trinns arbeidsflyter drar nytte av planlegging på forhånd og løpende fortelling. Modellen skisserer hva den vil gjøre, forteller om hvert steg, og oppsummerer deretter resultatene.

<img src="../../../translated_images/no/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Oppretter et REST-endepunkt med trinnvis fortelling (3.9MB)*

### Selvevaluerende kode

Prøv "Lag en e-postvalideringstjeneste". I stedet for bare å generere kode og stoppe, genererer modellen, evaluerer opp mot kvalitetskriterier, identifiserer svakheter og forbedrer. Du vil se den iterere til koden oppfyller produksjonsstandarder.

<img src="../../../translated_images/no/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fullstendig e-postvalideringstjeneste (5.2MB)*

### Strukturert analyse

Kodegjennomganger trenger konsistente evalueringsrammer. Modellen analyserer kode ved bruk av faste kategorier (korrekthet, praksis, ytelse, sikkerhet) med alvorlighetsnivåer.

<img src="../../../translated_images/no/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Kodegjennomgang basert på rammeverk*

### Multi-Turn Chat

Spør "Hva er Spring Boot?" og følg opp umiddelbart med "Vis meg et eksempel". Modellen husker ditt første spørsmål og gir deg et spesifikt Spring Boot-eksempel. Uten hukommelse ville det andre spørsmålet vært for vagt.

<img src="../../../translated_images/no/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Konservering av kontekst over flere spørsmål*

### Trinnvis resonnering

Velg et mattestykke og prøv det med både Trinnvis resonnering og Lav Iver. Lav iver gir deg bare svaret – raskt men utydelig. Trinnvis viser deg hver utregning og beslutning.

<img src="../../../translated_images/no/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Mattestykke med eksplisitte steg*

### Begrenset utdata

Når du trenger spesifikke formater eller ordtellinger, håndhever dette mønsteret streng overholdelse. Prøv å generere et sammendrag med nøyaktig 100 ord i punktlisteformat.

<img src="../../../translated_images/no/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Oppsummering av maskinlæring med formatkontroll*

## Det du egentlig lærer

**Resonneringsinnsats endrer alt**

GPT-5.2 lar deg styre beregningsinnsatsen via promptene dine. Lav innsats betyr raske svar med minimalt med utforskning. Høy innsats betyr at modellen bruker tid på å tenke dypt. Du lærer å tilpasse innsatsen til oppgavens kompleksitet – ikke kast bort tid på enkle spørsmål, men ikke forhast komplekse avgjørelser heller.

**Struktur veileder atferd**

Legg merke til XML-tagene i promptene? De er ikke dekorative. Modeller følger strukturerte instruksjoner mer pålitelig enn fri tekst. Når du trenger flere-trinns prosesser eller kompleks logikk, hjelper struktur modellen med å holde oversikt over hvor den er og hva som kommer neste.

<img src="../../../translated_images/no/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en godt strukturert prompt med klare seksjoner og XML-lignende organisering*

**Kvalitet gjennom selvevaluering**

De selvevaluerende mønstrene fungerer ved å gjøre kvalitetskriterier eksplisitte. I stedet for å håpe at modellen "gjør det riktig", forteller du den nøyaktig hva "riktig" betyr: korrekt logikk, feilhåndtering, ytelse, sikkerhet. Modellen kan dermed evaluere eget utdata og forbedre. Dette forvandler kodegenerering fra lotteri til en prosess.

**Kontekst er begrenset**

Flere-turns samtaler fungerer ved å inkludere meldingshistorikk med hver forespørsel. Men det finnes en grense – hver modell har et maksimalt token-antall. Etter hvert som samtaler vokser, trenger du strategier for å beholde relevant kontekst uten å nå denne grensen. Denne modulen viser deg hvordan hukommelse fungerer; senere lærer du når du skal oppsummere, når du skal glemme, og når du skal hente tilbake.

## Neste steg

**Neste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasjon:** [← Forrige: Modul 01 - Introduksjon](../01-introduction/README.md) | [Tilbake til hovedmeny](../README.md) | [Neste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->