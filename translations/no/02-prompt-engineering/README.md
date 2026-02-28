# Modul 02: Prompt Engineering med GPT-5.2

## Innholdsfortegnelse

- [Video Gjennomgang](../../../02-prompt-engineering)
- [Hva Du Vil Lære](../../../02-prompt-engineering)
- [Forutsetninger](../../../02-prompt-engineering)
- [Forstå Prompt Engineering](../../../02-prompt-engineering)
- [Grunnleggende om Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Maler](../../../02-prompt-engineering)
- [Avanserte Mønstre](../../../02-prompt-engineering)
- [Bruke Eksisterende Azure-Ressurser](../../../02-prompt-engineering)
- [Applikasjonsskjermbilder](../../../02-prompt-engineering)
- [Utforske Mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høy Iver](../../../02-prompt-engineering)
  - [Utføring av Oppgaver (Verktøypreambler)](../../../02-prompt-engineering)
  - [Selvreflekterende Kode](../../../02-prompt-engineering)
  - [Strukturert Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trinnvis Resonnering](../../../02-prompt-engineering)
  - [Begrenset Utdata](../../../02-prompt-engineering)
- [Hva Du Egentlig Lærer](../../../02-prompt-engineering)
- [Neste Steg](../../../02-prompt-engineering)

## Video Gjennomgang

Se denne livesesjonen som forklarer hvordan du kommer i gang med denne modulen:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Hva Du Vil Lære

<img src="../../../translated_images/no/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I forrige modul så du hvordan minne muliggjør konversasjons-AI og brukte GitHub-modeller for grunnleggende interaksjoner. Nå skal vi fokusere på hvordan du stiller spørsmål — selve promptene — ved bruk av Azure OpenAI sin GPT-5.2. Måten du strukturerer promptene dine på påvirker dramatisk kvaliteten på svarene du får. Vi starter med en gjennomgang av grunnleggende promptingsteknikker, og går deretter over til åtte avanserte mønstre som utnytter GPT-5.2 fullt ut.

Vi bruker GPT-5.2 fordi den introduserer kontroll over resonnement – du kan fortelle modellen hvor mye tenking den skal gjøre før den svarer. Dette gjør ulike promptingstrategier tydeligere og hjelper deg å forstå når du bør bruke hver metoder. Vi drar også nytte av Azures færre begrensninger på forespørsler for GPT-5.2 sammenlignet med GitHub-modeller.

## Forutsetninger

- Fullført Modul 01 (Azure OpenAI-ressurser deployert)
- `.env`-fil i rotkatalog med Azure-legitimasjon (opprettet ved `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg deployeringsinstruksjonene der først.

## Forstå Prompt Engineering

<img src="../../../translated_images/no/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Hva er Prompt Engineering?" width="800"/>

Prompt engineering handler om å designe inndata som konsekvent gir deg de resultatene du trenger. Det handler ikke bare om å stille spørsmål – det handler om å strukturere forespørsler så modellen forstår nøyaktig hva du vil ha og hvordan det skal leveres.

Tenk på det som å gi instruksjoner til en kollega. "Fiks feilen" er vagt. "Fiks nullpekker unntaket i UserService.java linje 45 ved å legge til en null-sjekk" er spesifikt. Språkmodeller fungerer på samme måte – presisjon og struktur er viktig.

<img src="../../../translated_images/no/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hvordan LangChain4j Passer Inn" width="800"/>

LangChain4j tilbyr infrastrukturen — modelltilkoblinger, minne og meldingstyper — mens promptmønstre bare er nøye strukturerte tekster du sender gjennom infrastrukturen. De viktigste byggeklossene er `SystemMessage` (som setter AI sin atferd og rolle) og `UserMessage` (som bærer din faktiske forespørsel).

## Grunnleggende om Prompt Engineering

<img src="../../../translated_images/no/five-patterns-overview.160f35045ffd2a94.webp" alt="Oversikt over Fem Prompt Engineering Mønstre" width="800"/>

Før vi dykker inn i de avanserte mønstrene i denne modulen, la oss repetere fem grunnleggende promptingsteknikker. Dette er byggeklossene enhver promptingeniør bør kjenne til. Hvis du allerede har jobbet gjennom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns), har du sett disse i praksis — her er det konseptuelle rammeverket bak dem.

### Zero-Shot Prompting

Den enkleste tilnærmingen: gi modellen en direkte instruksjon uten eksempler. Modellen stoler helt på treningen sin for å forstå og utføre oppgaven. Dette fungerer bra for enkle forespørsler hvor forventet oppførsel er åpenbar.

<img src="../../../translated_images/no/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruksjon uten eksempler — modellen utleder oppgaven kun fra instruksjonen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Respons: "Positiv"
```

**Når bruke:** Enkle klassifiseringer, direkte spørsmål, oversettelser eller enhver oppgave modellen klarer uten ekstra veiledning.

### Few-Shot Prompting

Gi eksempler som demonstrerer mønsteret du ønsker modellen skal følge. Modellen lærer forventet input-output-format fra eksemplene dine og anvender det på nye inndata. Dette forbedrer konsistensen dramatisk for oppgaver hvor ønsket format eller oppførsel ikke er opplagt.

<img src="../../../translated_images/no/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Læring fra eksempler — modellen identifiserer mønsteret og anvender det på nye inndata*

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

**Når bruke:** Egne klassifiseringer, konsekvent formatering, domene-spesifikke oppgaver eller når zero-shot resultater er inkonsistente.

### Chain of Thought

Be modellen vise sitt resonnement steg for steg. I stedet for å hoppe rett til et svar, bryter modellen ned problemet og jobber gjennom hver del eksplisitt. Dette forbedrer nøyaktigheten på matte, logikk og flerstegs resonneringsoppgaver.

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

**Når bruke:** Matteproblemer, logikkpuslespill, debugging eller enhver oppgave hvor det å vise resonnementet forbedrer nøyaktigheten og tilliten.

### Role-Based Prompting

Sett en persona eller rolle for AI før du stiller spørsmålet. Dette gir kontekst som former tonen, dybden og fokuset i svaret. En "programvarearkitekt" gir andre råd enn en "juniorutvikler" eller en "sikkerhetsrevisor".

<img src="../../../translated_images/no/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setter kontekst og persona — samme spørsmål får ulike svar avhengig av tildelt rolle*

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

**Når bruke:** Kodegjennomganger, veiledning, domene-spesifikk analyse eller når du trenger svar tilpasset et bestemt ekspertisenivå eller perspektiv.

### Prompt Maler

Lag gjenbrukbare prompts med variableplassholdere. I stedet for å skrive en ny prompt hver gang, definer en mal én gang og fyll inn ulike verdier. LangChain4js `PromptTemplate`-klasse gjør dette enkelt med `{{variable}}`-syntaks.

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

**Når bruke:** Gjentatte forespørsler med forskjellige inndata, batch-prosessering, bygging av gjenbrukbare AI-arbeidsflyter, eller enhver situasjon hvor prompt-strukturen er lik men dataene endres.

---

Disse fem grunnprinsippene gir deg et solid verktøysett for de fleste promptingoppgaver. Resten av denne modulen bygger på dem med **åtte avanserte mønstre** som utnytter GPT-5.2 sin kontroll over resonnement, selvevaluering og strukturerte utdata.

## Avanserte Mønstre

Med grunnlaget på plass, la oss gå til de åtte avanserte mønstrene som gjør denne modulen unik. Ikke alle problemer trenger samme tilnærming. Noen spørsmål krever raske svar, andre trenger dyp tenkning. Noen trenger synlig resonnement, andre bare resultatene. Hvert mønster nedenfor er optimalisert for en ulik situasjon — og GPT-5.2 sin kontroll over resonnement gjør forskjellene enda tydeligere.

<img src="../../../translated_images/no/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtte Prompting Mønstre" width="800"/>

*Oversikt over de åtte prompt engineering-mønstrene og deres bruksområder*

<img src="../../../translated_images/no/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Kontroll over Resonnering med GPT-5.2" width="800"/>

*GPT-5.2 sin kontroll over resonnering lar deg spesifisere hvor mye tenking modellen skal gjøre — fra raske direkte svar til dyptgående utforskning*

**Lav Iver (Rask & Fokusert)** - For enkle spørsmål der du vil ha raske, direkte svar. Modellen gjør minimal resonnement – maks 2 steg. Bruk dette for beregninger, oppslag eller enkle spørsmål.

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
> - "Hva er forskjellen mellom mønstrene lav iver og høy iver?"
> - "Hvordan hjelper XML-taggene i promptene med å strukturere AI-ens svar?"
> - "Når bør jeg bruke selvrefleksjonsmønstre vs direkte instruksjon?"

**Høy Iver (Dyp & Grundig)** - For komplekse problemer der du ønsker en omfattende analyse. Modellen utforsker grundig og viser detaljert resonnering. Bruk dette for systemdesign, arkitekturvalg eller komplekse forskningsoppgaver.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utføring av Oppgaver (Trinnvis Fremgang)** - For flerstegs arbeidsflyter. Modellen gir en forhåndsplan, forteller om hvert steg mens den jobber, og gir så en oppsummering. Bruk dette for migrasjoner, implementeringer eller enhver flerstegsprosess.

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

Chain-of-Thought prompting ber eksplisitt modellen vise resonneringsprosessen sin, noe som forbedrer nøyaktigheten ved komplekse oppgaver. Den trinnvise nedbrytningen hjelper både mennesker og AI å forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om dette mønsteret:
> - "Hvordan kunne jeg tilpasse mønsteret for oppgaveutførelse til langvarige operasjoner?"
> - "Hva er beste praksis for strukturering av verktøypreambler i produksjonsapplikasjoner?"
> - "Hvordan kan jeg fange og vise mellomliggende fremdriftsoppdateringer i et brukergrensesnitt?"

<img src="../../../translated_images/no/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mønster for Oppgaveutførelse" width="800"/>

*Planlegg → Utfør → Oppsummer arbeidsflyt for flerstegsoppgaver*

**Selvreflekterende Kode** - For å generere produksjonsklar kode. Modellen genererer kode i henhold til produksjonsstandarder med skikkelig feilhåndtering. Bruk dette ved bygging av nye funksjoner eller tjenester.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Syklus for Selvrefleksjon" width="800"/>

*Iterativ forbedringssløyfe - generer, evaluer, identifiser problemer, forbedre, gjenta*

**Strukturert Analyse** - For konsekvent evaluering. Modellen gjennomgår kode med en fast ramme (korrekthet, praksis, ytelse, sikkerhet, vedlikeholdbarhet). Bruk dette til kodegjennomganger eller kvalitetsvurderinger.

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
> - "Hva er beste måte å parse og handle på strukturerte utdata programmessig?"
> - "Hvordan sikrer jeg konsistente alvorlighetsnivåer på tvers av ulike gjennomgangssesjoner?"

<img src="../../../translated_images/no/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mønster for Strukturert Analyse" width="800"/>

*Rammeverk for konsistente kodegjennomganger med alvorlighetsnivåer*

**Multi-Turn Chat** - For samtaler som trenger kontekst. Modellen husker tidligere meldinger og bygger videre på dem. Bruk dette for interaktive hjelpesesjoner eller komplekse Q&A.

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

*Hvordan samtalekontekst akkumuleres over flere turer til token-grensen nås*

**Trinnvis Resonnering** - For problemer som krever synlig logikk. Modellen viser eksplisitt resonnement for hvert trinn. Bruk dette for matteproblemer, logikkpuslespill eller når du trenger å forstå tenkeprosessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Mønster for Trinnvis Resonnering" width="800"/>

*Bryter problemer ned i eksplisitte logiske steg*

**Begrenset Utdata** - For svar med spesifikke formatkrav. Modellen følger strengt krav til format og lengde. Bruk dette for oppsummeringer eller når du trenger presis utdatastruktur.

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

<img src="../../../translated_images/no/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mønster for Begrenset Utdata" width="800"/>

*Håndheving av spesifikke krav til format, lengde og struktur*

## Bruke Eksisterende Azure-Ressurser

**Verifiser deployering:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (opprettet i Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede startet alle applikasjoner med `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8083. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8083.
**Alternativ 1: Bruke Spring Boot Dashboard (anbefalt for VS Code-brukere)**

Utviklingscontaineren inkluderer Spring Boot Dashboard-utvidelsen, som gir en visuell grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i Aktivitetslinjen på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett enkelt klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk enkelt på avspillingsknappen ved siden av "prompt-engineering" for å starte dette modulen, eller start alle moduler på en gang.

<img src="../../../translated_images/no/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Bruke shell-skript**

Start alle nettapplikasjoner (moduler 01-04):

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

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og bygger JAR-filene hvis de ikke eksisterer.

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
./stop.sh  # Bare denne modulen
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

*Hoveddashboardet som viser alle 8 prompt engineering-mønstre med deres kjennetegn og bruksområder*

## Utforske mønstrene

Webgrensesnittet lar deg eksperimentere med forskjellige prompting-strategier. Hvert mønster løser ulike problemer – prøv dem for å se når hver tilnærming fungerer best.

> **Merk: Streaming vs ikke-streaming** — Hver mønsterside tilbyr to knapper: **🔴 Stream Response (Live)** og et **Ikke-streaming** alternativ. Streaming bruker Server-Sent Events (SSE) for å vise token i sanntid mens modellen genererer dem, så du ser fremgangen umiddelbart. Ikke-streaming venter på hele svaret før det vises. For spørsmål som krever dyp tenkning (f.eks. High Eagerness, Self-Reflecting Code), kan ikke-streaming ta veldig lang tid – noen ganger minutter – uten synlig tilbakemelding. **Bruk streaming når du eksperimenterer med komplekse prompts** slik at du kan se modellen arbeide og unngå inntrykket av at forespørselen har tidsavbrutt.
>
> **Merk: Nettleserkrav** — Streaming-funksjonen bruker Fetch Streams API (`response.body.getReader()`) som krever en full nettleser (Chrome, Edge, Firefox, Safari). Den fungerer **ikke** i VS Codes innebygde Simple Browser, da webviewen ikke støtter ReadableStream API. Hvis du bruker Simple Browser, vil ikke-streaming-knappene fortsatt fungere normalt – kun streaming-knappene påvirkes. Åpne `http://localhost:8083` i en ekstern nettleser for full opplevelse.

### Lav vs Høy Iværksomhet

Still et enkelt spørsmål som "Hva er 15 % av 200?" med Lav Iværksomhet. Du får et øyeblikkelig, direkte svar. Still deretter noe komplekst som "Design en caching-strategi for en høytrafikk API" med Høy Iværksomhet. Klikk **🔴 Stream Response (Live)** og se modellens detaljerte resonnement komme frem token-for-token. Samme modell, samme spørsmålsstruktur – men prompten forteller den hvor mye tenking som skal gjøres.

### Oppgaveutførelse (Tool Preambles)

Flertrinns arbeidsflyter drar nytte av forhåndsplanlegging og fremdriftsnarrasjon. Modellen skisserer hva den vil gjøre, forteller om hvert steg, og oppsummerer resultatene.

### Selvreparerende Kode

Prøv "Lag en e-postvalideringstjeneste". I stedet for å bare generere kode og stoppe, genererer modellen, evaluerer mot kvalitetskriterier, identifiserer svakheter, og forbedrer. Du ser det iterere til koden oppfyller produksjonsstandarder.

### Strukturert Analyse

Kodegjennomganger trenger konsistente evalueringsrammer. Modellen analyserer kode med faste kategorier (korrekthet, praksis, ytelse, sikkerhet) med alvorlighetsnivåer.

### Fler-Dialog Chat

Spør "Hva er Spring Boot?" og følg opp umiddelbart med "Vis meg et eksempel". Modellen husker ditt første spørsmål og gir deg et Spring Boot-eksempel spesielt tilpasset. Uten hukommelse ville det andre spørsmålet vært for vagt.

### Steg-for-Steg Resonnement

Velg et mattestykke og prøv det med både Steg-for-steg Resonnement og Lav Iværksomhet. Lav iværksomhet gir deg bare svaret – raskt men uklart. Steg-for-steg viser alle beregninger og beslutninger.

### Begrenset Utdata

Når du trenger spesifikke formater eller ordtellinger, håndhever dette mønsteret strenge regler. Prøv å generere et sammendrag med nøyaktig 100 ord i punktlisteformat.

## Hva du egentlig lærer

**Resonneringsinnsats endrer alt**

GPT-5.2 lar deg styre beregningsinnsatsen gjennom promptene dine. Lav innsats betyr raske svar med minimal utforskning. Høy innsats betyr at modellen tar seg tid til dyp tenking. Du lærer å tilpasse innsats til oppgavens kompleksitet – ikke kast bort tid på enkle spørsmål, men ikke forhast komplekse avgjørelser heller.

**Struktur styrer atferd**

Legg merke til XML-taggene i promptene? De er ikke dekorative. Modeller følger strukturerte instruksjoner mer pålitelig enn fri tekst. Når du trenger flertrinnsprosesser eller kompleks logikk, hjelper struktur modellen å holde oversikt over hvor den er og hva som kommer.

<img src="../../../translated_images/no/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en velstrukturert prompt med klare seksjoner og XML-stil organisering*

**Kvalitet gjennom selvevaluering**

De selvreflekterende mønstrene fungerer ved å gjøre kvalitetskriterier eksplisitte. I stedet for å håpe at modellen "gjør det riktig", forteller du nøyaktig hva "riktig" betyr: korrekt logikk, feilhåndtering, ytelse, sikkerhet. Modellen kan deretter evaluere sitt eget resultat og forbedre seg. Dette gjør koding fra et lotteri til en prosess.

**Kontekst er begrenset**

Flerdialog-samtaler fungerer ved å inkludere meldingshistorikk i hver forespørsel. Men det finnes en grense – hver modell har en maksimal token-grense. Når samtaler vokser, trenger du strategier for å holde relevant kontekst uten å nå taket. Denne modulen viser hvordan hukommelse fungerer; senere lærer du når du skal oppsummere, når du skal glemme, og når du skal hente.

## Neste steg

**Neste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasjon:** [← Forrige: Modul 01 - Introduksjon](../01-introduction/README.md) | [Tilbake til hovedmeny](../README.md) | [Neste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi etterstreber nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på dets opprinnelige språk skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår som følge av bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->