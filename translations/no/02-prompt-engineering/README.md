# Modul 02: Prompt Engineering med GPT-5.2

## Innholdsfortegnelse

- [Hva du vil lære](../../../02-prompt-engineering)
- [Forutsetninger](../../../02-prompt-engineering)
- [Forstå prompt engineering](../../../02-prompt-engineering)
- [Grunnleggende prompt engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollebassert prompting](../../../02-prompt-engineering)
  - [Promptmaler](../../../02-prompt-engineering)
- [Avanserte mønstre](../../../02-prompt-engineering)
- [Bruke eksisterende Azure-ressurser](../../../02-prompt-engineering)
- [Applikasjonsskjermbilder](../../../02-prompt-engineering)
- [Utforske mønstrene](../../../02-prompt-engineering)
  - [Lav vs høy iver](../../../02-prompt-engineering)
  - [Oppgaveutførelse (verktøypreambler)](../../../02-prompt-engineering)
  - [Selvreflekterende kode](../../../02-prompt-engineering)
  - [Strukturert analyse](../../../02-prompt-engineering)
  - [Multi-turn chat](../../../02-prompt-engineering)
  - [Trinnvis resonnement](../../../02-prompt-engineering)
  - [Begrenset utdata](../../../02-prompt-engineering)
- [Hva du egentlig lærer](../../../02-prompt-engineering)
- [Neste steg](../../../02-prompt-engineering)

## Hva du vil lære

<img src="../../../translated_images/no/what-youll-learn.c68269ac048503b2.webp" alt="Hva du vil lære" width="800"/>

I forrige modul så du hvordan minne muliggjør samtale-AI og brukte GitHub-modeller for grunnleggende interaksjoner. Nå skal vi fokusere på hvordan du stiller spørsmål — selve promptene — ved hjelp av Azure OpenAI sin GPT-5.2. Måten du strukturerer promptene dine på påvirker dramatisk kvaliteten på svarene du får. Vi begynner med en gjennomgang av grunnleggende promptingsteknikker, og går deretter videre til åtte avanserte mønstre som utnytter GPT-5.2s muligheter fullt ut.

Vi bruker GPT-5.2 fordi den introduserer styring av resonnement — du kan fortelle modellen hvor mye tenking den skal gjøre før den svarer. Dette gjør ulike promptingstrategier mer tydelige og hjelper deg å forstå når hver tilnærming bør brukes. Vi drar også nytte av Azures færre ratebegrensninger for GPT-5.2 sammenlignet med GitHub-modeller.

## Forutsetninger

- Fullført Modul 01 (Azure OpenAI-ressurser distribuert)
- `.env` fil i rotkatalogen med Azure-legitimasjon (laget av `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg distribusjonsinstruksjonene der først.

## Forstå prompt engineering

<img src="../../../translated_images/no/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Hva er Prompt Engineering?" width="800"/>

Prompt engineering handler om å designe inndata-tekst som konsekvent gir deg de resultatene du trenger. Det handler ikke bare om å stille spørsmål — det handler om å strukturere forespørsler så modellen forstår nøyaktig hva du vil ha og hvordan det skal leveres.

Tenk på det som å gi instruksjoner til en kollega. "Fiks feilen" er vagt. "Fiks nullpointer-unntaket i UserService.java linje 45 ved å legge til en null-sjekk" er spesifikt. Språkmodeller fungerer på samme måte — spesifisitet og struktur betyr mye.

<img src="../../../translated_images/no/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hvordan LangChain4j passer inn" width="800"/>

LangChain4j tilbyr infrastrukturen — modellforbindelser, minne og meldingstyper — mens promptmønstre bare er nøye strukturerte tekster du sender gjennom infrastrukturen. De viktigste byggesteinene er `SystemMessage` (som setter AIens oppførsel og rolle) og `UserMessage` (som bærer din faktiske forespørsel).

## Grunnleggende prompt engineering

<img src="../../../translated_images/no/five-patterns-overview.160f35045ffd2a94.webp" alt="Oversikt over fem prompt engineering-mønstre" width="800"/>

Før vi dykker inn i de avanserte mønstrene i denne modulen, la oss gjennomgå fem grunnleggende promptingsteknikker. Dette er byggesteinene som enhver prompt-ingeniør bør kjenne til. Hvis du allerede har jobbet gjennom [Rask start-modulen](../00-quick-start/README.md#2-prompt-patterns), har du sett disse i praksis — her er det konseptuelle rammeverket bak dem.

### Zero-Shot Prompting

Den enkleste tilnærmingen: gi modellen en direkte instruksjon uten eksempler. Modellen stoler helt på treningen sin for å forstå og utføre oppgaven. Dette fungerer bra for enkle forespørsler hvor forventet oppførsel er åpenbar.

<img src="../../../translated_images/no/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruksjon uten eksempler — modellen utleder oppgaven kun fra instruksjonen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Når du skal bruke:** Enkle klassifiseringer, direkte spørsmål, oversettelser eller oppgaver modellen kan håndtere uten ekstra veiledning.

### Few-Shot Prompting

Gi eksempler som demonstrerer mønsteret du vil at modellen skal følge. Modellen lærer forventet input-output-format fra eksemplene dine og anvender det på nye innganger. Dette forbedrer konsistensen dramatisk for oppgaver hvor ønsket format eller oppførsel ikke er åpenbar.

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

**Når du skal bruke:** Egendefinerte klassifiseringer, konsekvent formatering, domenespesifikke oppgaver eller når zero-shot resultater er inkonsistente.

### Chain of Thought

Be modellen vise sitt resonnement steg for steg. I stedet for å hoppe rett til et svar, bryter modellen ned problemet og arbeider gjennom hver del eksplisitt. Dette forbedrer nøyaktigheten ved matematikk, logikk og flertrinns resonnement.

<img src="../../../translated_images/no/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trinnvis resonnement — bryte ned komplekse problemer i eksplisitte logiske steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, deretter 7 + 12 = 19 epler
```

**Når du skal bruke:** Matematikkoppgaver, logikkpuzzles, feilsøking eller oppgaver hvor det å vise resonnementet forbedrer nøyaktighet og tillit.

### Rollebassert prompting

Sett en persona eller rolle for AI før du stiller spørsmålet. Dette gir kontekst som former tonen, dybden og fokuset i svaret. En "software architect" gir annerledes råd enn en "junior developer" eller en "security auditor".

<img src="../../../translated_images/no/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollebassert prompting" width="800"/>

*Setter kontekst og persona — samme spørsmål får ulikt svar avhengig av tildelt rolle*

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

**Når du skal bruke:** Kodegjennomganger, veiledning, domenespesifikk analyse, eller når du trenger svar tilpasset et spesielt ekspertisenivå eller perspektiv.

### Promptmaler

Lag gjenbrukbare prompts med variable plassholdere. I stedet for å skrive en ny prompt hver gang, definer en mal én gang og fyll inn forskjellige verdier. LangChain4js `PromptTemplate` klasse gjør dette enkelt med `{{variable}}`-syntaks.

<img src="../../../translated_images/no/prompt-templates.14bfc37d45f1a933.webp" alt="Promptmaler" width="800"/>

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

**Når du skal bruke:** Gjentatte forespørsler med forskjellige inndata, batchbehandling, bygge gjenbrukbare AI-arbeidsflyter, eller enhver situasjon hvor promptstrukturen er lik, men dataen endres.

---

Disse fem grunnleggende gir deg et solid verktøysett for de fleste promptingoppgaver. Resten av denne modulen bygger videre på dem med **åtte avanserte mønstre** som utnytter GPT-5.2s resonnementskontroll, selvevaluering og strukturerte output-muligheter.

## Avanserte mønstre

Med det grunnleggende på plass, la oss gå videre til de åtte avanserte mønstrene som gjør denne modulen unik. Ikke alle problemer trenger samme tilnærming. Noen spørsmål krever raske svar, andre krever dyp tenking. Noen trenger synlig resonnement, andre trenger bare resultater. Hvert mønster nedenfor er optimalisert for et ulikt scenario — og GPT-5.2s styring av resonnement gjør forskjellene enda tydeligere.

<img src="../../../translated_images/no/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtte prompting-mønstre" width="800"/>

*Oversikt over de åtte prompt engineering-mønstrene og deres bruksområder*

<img src="../../../translated_images/no/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Resonnementskontroll med GPT-5.2" width="800"/>

*GPT-5.2s resonnementskontroll lar deg angi hvor mye tenking modellen skal gjøre — fra raske direkte svar til dyp utforskning*

<img src="../../../translated_images/no/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Sammenligning av resonnementinnsats" width="800"/>

*Lav iver (raskt, direkte) vs høy iver (grundig, utforskende) resonnementstilnærminger*

**Lav iver (rask & fokusert)** - For enkle spørsmål hvor du vil ha raske, direkte svar. Modellen gjør minimal resonnement - maksimalt 2 steg. Bruk dette for beregninger, oppslag eller enkle spørsmål.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Utforsk med GitHub Copilot:** Åpne [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) og spør:
> - "Hva er forskjellen mellom lav og høy iver i prompting-mønstre?"
> - "Hvordan hjelper XML-tagger i prompts til å strukturere AIens svar?"
> - "Når bør jeg bruke selvrefleksjonsmønstre kontra direkte instruksjon?"

**Høy iver (dyp & grundig)** - For komplekse problemer hvor du vil ha omfattende analyse. Modellen utforsker grundig og viser detaljert resonnement. Bruk dette for systemdesign, arkitekturavgjørelser eller komplekse undersøkelser.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Oppgaveutførelse (trinnvis fremdrift)** - For flertrinns arbeidsflyter. Modellen gir en plan på forhånd, forteller hver steg mens den jobber, og gir til slutt en oppsummering. Bruk dette for migrasjoner, implementasjoner eller enhver flertrinnsprosess.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting ber eksplisitt modellen vise sitt resonnement, noe som forbedrer nøyaktigheten for komplekse oppgaver. Den trinnvise gjennomgangen hjelper både mennesker og AI til å forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om dette mønsteret:
> - "Hvordan kan jeg tilpasse oppgaveutførelsesmønsteret for langvarige operasjoner?"
> - "Hva er beste praksis for å strukturere verktøypreambler i produksjonsapplikasjoner?"
> - "Hvordan kan jeg fange opp og vise mellomresultater i en brukergrensesnitt?"

<img src="../../../translated_images/no/task-execution-pattern.9da3967750ab5c1e.webp" alt="Oppgaveutførelsesmønster" width="800"/>

*Planlegg → Utfør → Oppsummer arbeidsflyt for flertrinnsoppgaver*

**Selvreflekterende kode** - For å generere produksjonskvalitetskode. Modellen genererer kode, sjekker den opp mot kvalitetskriterier, og forbedrer den iterativt. Bruk dette når du bygger nye funksjoner eller tjenester.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selvrefleksjonssyklus" width="800"/>

*Iterativ forbedringssløyfe - generer, evaluer, identifiser problemer, forbedre, gjenta*

**Strukturert analyse** - For konsistente evalueringer. Modellen gjennomgår kode med en fast ramme (korrekthet, praksis, ytelse, sikkerhet). Bruk dette til kodegjennomganger eller kvalitetsvurderinger.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spør om strukturert analyse:
> - "Hvordan kan jeg tilpasse analyse-rammeverket for forskjellige typer kodegjennomganger?"
> - "Hva er beste metoden for å tolke og handle på strukturert output programmatisk?"
> - "Hvordan sikrer jeg konsekvente alvorlighetsgrader på tvers av ulike gjennomgangssesjoner?"

<img src="../../../translated_images/no/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturert analysemønster" width="800"/>

*Fire-kategori rammeverk for konsistente kodegjennomganger med alvorlighetsgrader*

**Multi-turn chat** - For samtaler som trenger kontekst. Modellen husker tidligere meldinger og bygger videre på dem. Bruk dette til interaktive hjelpesesjoner eller komplekse spørsmål og svar.

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

*Hvordan samtalekontekst akkumuleres over flere runder til tokengrense nås*

**Trinnvis resonnement** - For problemer som krever synlig logikk. Modellen viser eksplisitt resonnement for hvert steg. Bruk dette for matematikkoppgaver, logikkpuzzles eller når du må forstå tankeprosessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/no/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Trinnvis resonnementmønster" width="800"/>

*Bryte ned problemer i eksplisitte logiske steg*

**Begrenset utdata** - For svar med spesifikke formatkrav. Modellen følger strengt format- og lengderegler. Bruk dette for sammendrag eller når du trenger presis utdata-struktur.

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

*Påtvinge spesifikke format-, lengde- og strukturelle krav*

## Bruke eksisterende Azure-ressurser

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (laget under Modul 01):
```bash
cat ../.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede har startet alle applikasjoner med `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8083. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8083.

**Alternativ 1: Bruke Spring Boot Dashboard (anbefalt for VS Code-brukere)**

Dev containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir en visuell grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetslinjen til venstre i VS Code (se etter Spring Boot-ikonet).
Fra Spring Boot-dashbordet kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk enkelt på spill-knappen ved siden av "prompt-engineering" for å starte denne modulen, eller start alle moduler samtidig.

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

Begge skriptene laster automatisk miljøvariabler fra rot `.env`-filen og bygger JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle modulene manuelt før du starter:
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
./stop.sh  # Bare denne modulen
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Denne modulen bare
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Applikasjonsskjermbilder

<img src="../../../translated_images/no/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashbordet som viser alle 8 prompt engineering-mønstre med deres kjennetegn og bruksområder*

## Utforske Mønstrene

Nettgrensesnittet lar deg eksperimentere med forskjellige prompting-strategier. Hvert mønster løser forskjellige problemer – prøv dem for å se når hver tilnærming fungerer best.

### Lav vs Høy Innsats

Still et enkelt spørsmål som "Hva er 15% av 200?" med Lav Innsats. Du får et øyeblikkelig, direkte svar. Still deretter noe komplekst som "Design en caching-strategi for en trafikkert API" med Høy Innsats. Se hvordan modellen senker farten og gir detaljert resonnering. Samme modell, samme spørsmålstruktur – men prompten forteller hvor mye tenking som skal gjøres.

<img src="../../../translated_images/no/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Rask utregning med minimal resonnering*

<img src="../../../translated_images/no/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende caching-strategi (2.8MB)*

### Oppgaveutførelse (Verktøypreambler)

Flere trinns arbeidsflyter drar nytte av planlegging på forhånd og fremdriftsberetning. Modellen skisserer hva den vil gjøre, forteller om hvert steg, og oppsummerer resultatene.

<img src="../../../translated_images/no/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Opprette et REST-endepunkt med steg-for-steg fortelling (3.9MB)*

### Selvreflekterende Kode

Prøv "Lag en e-postvalideringstjeneste". I stedet for bare å generere kode og stoppe, genererer modellen, evaluerer mot kvalitetskriterier, identifiserer svakheter, og forbedrer. Du vil se den iterere til koden oppfyller produksjonsstandarder.

<img src="../../../translated_images/no/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fullstendig e-postvalideringstjeneste (5.2MB)*

### Strukturert Analyse

Kodegjennomganger trenger konsistente evalueringsrammer. Modellen analyserer kode ved hjelp av faste kategorier (riktighet, praksis, ytelse, sikkerhet) med alvorlighetsnivåer.

<img src="../../../translated_images/no/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rammeverksbasert kodegjennomgang*

### Multi-Turn Chat

Spør "Hva er Spring Boot?" og følg umiddelbart opp med "Vis meg et eksempel". Modellen husker ditt første spørsmål og gir deg et spesifikt Spring Boot-eksempel. Uten hukommelse ville det andre spørsmålet vært for vagt.

<img src="../../../translated_images/no/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevar kontekst over flere spørsmål*

### Steg-for-steg-resonnering

Velg et mattestykke og prøv det med både Steg-for-steg-resonnering og Lav Innsats. Lav innsats gir deg bare svaret – raskt men uklart. Steg-for-steg viser deg hver utregning og beslutning.

<img src="../../../translated_images/no/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matteproblem med tydelige steg*

### Begrenset Utdata

Når du trenger spesifikke formater eller antall ord, sørger dette mønsteret for streng overholdelse. Prøv å generere et sammendrag med nøyaktig 100 ord i punktlisteformat.

<img src="../../../translated_images/no/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskinlæringsoppsummering med formatkontroll*

## Hva du egentlig lærer

**Resonneringsinnsats endrer alt**

GPT-5.2 lar deg styre den beregningsmessige innsatsen gjennom promptene dine. Lav innsats betyr raske svar med minimal utforskning. Høy innsats betyr at modellen tar seg tid til å tenke grundig. Du lærer å matche innsats til oppgavens kompleksitet – ikke kast bort tid på enkle spørsmål, men ikke hast beslutninger heller.

**Struktur styrer atferd**

Merker du XML-taggene i promptene? De er ikke dekorative. Modeller følger strukturerte instruksjoner mer pålitelig enn fri tekst. Når du trenger flere trinns prosesser eller kompleks logikk, hjelper struktur modellen med å holde styr på hvor den er og hva som kommer.

<img src="../../../translated_images/no/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en godt strukturert prompt med klare seksjoner og XML-stil organisering*

**Kvalitet gjennom selvevaluering**

De selvreflekterende mønstrene fungerer ved å gjøre kvalitetskriteriene eksplisitte. I stedet for å håpe at modellen "gjør det rett", forteller du den akkurat hva "riktig" betyr: korrekt logikk, feilbehandling, ytelse, sikkerhet. Modellen kan da evaluere egen output og forbedre seg. Dette gjør kodegenerering fra lotteri til en prosess.

**Kontekst er begrenset**

Multi-turn-samtaler fungerer ved å inkludere meldingshistorikk i hver forespørsel. Men det finnes en grense – hver modell har et maksimum token-antall. Når samtaler vokser, må du ha strategier for å beholde relevant kontekst uten å nå denne grensen. Denne modulen viser hvordan hukommelse fungerer; senere lærer du når du skal oppsummere, glemme og hente.

## Neste steg

**Neste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasjon:** [← Forrige: Modul 01 - Introduksjon](../01-introduction/README.md) | [Tilbake til hovedsiden](../README.md) | [Neste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved bruk av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på dets opprinnelige språk skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som følge av bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->