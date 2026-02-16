# Modul 02: Prompt Engineering med GPT-5.2

## Indholdsfortegnelse

- [Hvad du vil lære](../../../02-prompt-engineering)
- [Forudsætninger](../../../02-prompt-engineering)
- [Forståelse af Prompt Engineering](../../../02-prompt-engineering)
- [Grundlæggende om Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Avancerede Mønstre](../../../02-prompt-engineering)
- [Brug af eksisterende Azure-ressourcer](../../../02-prompt-engineering)
- [Applikationsskærmbilleder](../../../02-prompt-engineering)
- [Udforskning af mønstrene](../../../02-prompt-engineering)
  - [Lav vs høj ivrighed](../../../02-prompt-engineering)
  - [Opgaveudførelse (værktøjspreambler)](../../../02-prompt-engineering)
  - [Selvreflekterende kode](../../../02-prompt-engineering)
  - [Struktureret analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trin-for-trin ræsonnering](../../../02-prompt-engineering)
  - [Begrænset output](../../../02-prompt-engineering)
- [Hvad du virkelig lærer](../../../02-prompt-engineering)
- [Næste skridt](../../../02-prompt-engineering)

## Hvad du vil lære

<img src="../../../translated_images/da/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I det forrige modul så du, hvordan hukommelse muliggør samtale-AI, og du brugte GitHub Models til grundlæggende interaktioner. Nu vil vi fokusere på, hvordan du stiller spørgsmål — altså promptene selv — ved at bruge Azure OpenAI's GPT-5.2. Måden du strukturerer dine prompts på, påvirker dramatisk kvaliteten af de svar, du får. Vi starter med en gennemgang af de grundlæggende promptingsteknikker og går derefter videre til otte avancerede mønstre, der udnytter GPT-5.2's muligheder fuldt ud.

Vi bruger GPT-5.2, fordi det introducerer kontrol over ræsonnering - du kan fortælle modellen, hvor meget den skal tænke, før den svarer. Det gør forskellige promptingstrategier mere tydelige og hjælper dig med at forstå, hvornår du skal bruge hver tilgang. Vi får også fordel af færre ratebegrænsninger for GPT-5.2 på Azure sammenlignet med GitHub Models.

## Forudsætninger

- Fuldført Modul 01 (Azure OpenAI-ressourcer implementeret)
- `.env`-fil i rodmappen med Azure-legitimationsoplysninger (oprettet via `azd up` i Modul 01)

> **Note:** Hvis du ikke har fuldført Modul 01, følg først deploymentsinstruktionerne der.

## Forståelse af Prompt Engineering

<img src="../../../translated_images/da/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handler om at designe inputtekst, som konsekvent giver dig de resultater, du har brug for. Det handler ikke kun om at stille spørgsmål - det handler om at strukturere anmodninger, så modellen præcist forstår, hvad du vil, og hvordan det skal leveres.

Tænk på det som at give instruktioner til en kollega. "Fix fejlen" er vag. "Fix null pointer exception i UserService.java linje 45 ved at tilføje en null check" er specifik. Sprogmodeller fungerer på samme måde - specificitet og struktur betyder noget.

<img src="../../../translated_images/da/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j leverer infrastrukturen — modelforbindelser, hukommelse og beskedtyper — mens promptmønstre blot er omhyggeligt struktureret tekst, du sender gennem denne infrastruktur. De centrale byggeklodser er `SystemMessage` (som definerer AI’ens adfærd og rolle) og `UserMessage` (som indeholder din faktiske forespørgsel).

## Grundlæggende om Prompt Engineering

<img src="../../../translated_images/da/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Før vi dykker ned i de avancerede mønstre i dette modul, lad os gennemgå fem grundlæggende promptingsteknikker. Disse er byggeklodser, som enhver prompt-ingeniør bør kende. Hvis du allerede har arbejdet med [Quick Start-modulet](../00-quick-start/README.md#2-prompt-patterns), har du set dem i aktion — her er det konceptuelle rammeværk bag dem.

### Zero-Shot Prompting

Den simpleste tilgang: giv modellen en direkte instruktion uden eksempler. Modellen baserer sig udelukkende på sin træning til at forstå og udføre opgaven. Det fungerer godt til simple forespørgsler, hvor den forventede opførsel er klar.

<img src="../../../translated_images/da/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruktion uden eksempler — modellen udleder opgaven ud fra instruksen alene*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Hvornår skal den bruges:** Simpel klassifikation, direkte spørgsmål, oversættelser eller enhver opgave, som modellen kan håndtere uden yderligere vejledning.

### Few-Shot Prompting

Giv eksempler, der demonstrerer det mønster, du vil have modellen til at følge. Modellen lærer det forventede input-output format fra dine eksempler og anvender det på nye input. Det forbedrer markant konsistensen for opgaver, hvor ønsket format eller adfærd ikke er oplagt.

<img src="../../../translated_images/da/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Læring fra eksempler — modellen identificerer mønsteret og anvender det på nye input*

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

**Hvornår skal den bruges:** Specialiseret klassifikation, ensartet formatering, domænespecifikke opgaver eller når zero-shot resultater er inkonsistente.

### Chain of Thought

Bed modellen om at vise sin ræsonnering trin for trin. I stedet for at springe direkte til et svar, bryder modellen problemet ned og arbejder igennem hver del eksplicit. Det forbedrer nøjagtigheden på matematik, logik og flertrins ræsonneringsopgaver.

<img src="../../../translated_images/da/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trin-for-trin ræsonnering — opdeling af komplekse problemer i eksplicitte logiske trin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, derefter 7 + 12 = 19 æbler
```

**Hvornår skal den bruges:** Matematikopgaver, logikpuslespil, debugging eller enhver opgave, hvor det at vise ræsonneringsprocessen forbedrer nøjagtighed og tillid.

### Role-Based Prompting

Sæt en persona eller rolle for AI’en før du stiller spørgsmål. Det giver kontekst, som former tonen, dybden og fokus i svaret. En "softwarearkitekt" giver andre råd end en "juniorudvikler" eller en "sikkerhedsrevisor".

<img src="../../../translated_images/da/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Angivelse af kontekst og persona — det samme spørgsmål får forskellige svar afhængig af den tildelte rolle*

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

**Hvornår skal den bruges:** Kodegennemgange, undervisning, domænespecifik analyse eller når du har behov for svar tilpasset et bestemt ekspertiseniveau eller synspunkt.

### Prompt Templates

Lav genanvendelige prompts med variable pladsholdere. I stedet for at skrive en ny prompt hver gang, definer en skabelon én gang og udfyld forskellige værdier. LangChain4j’s `PromptTemplate` klasse gør dette nemt med `{{variable}}` syntaks.

<img src="../../../translated_images/da/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Genanvendelige prompts med variable pladsholdere — én skabelon, mange anvendelser*

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

**Hvornår skal den bruges:** Gentagne forespørgsler med forskellige input, batchbehandling, opbygning af genanvendelige AI-arbejdsgange eller enhver situation, hvor promptstrukturen forbliver den samme, men data ændrer sig.

---

Disse fem grundlæggende giver dig et solidt værktøjssæt til de fleste promptingopgaver. Resten af dette modul bygger videre på dem med **otte avancerede mønstre**, der udnytter GPT-5.2’s ræsonneringskontrol, selvevaluering og strukturerede output-funktioner.

## Avancerede Mønstre

Med det grundlæggende dækket går vi videre til de otte avancerede mønstre, der gør dette modul unikt. Ikke alle problemer kræver den samme tilgang. Nogle spørgsmål skal have hurtige svar, andre skal have dybdegående overvejelser. Nogle behøver synlig ræsonnering, andre nøjes med resultater. Hvert mønster nedenfor er optimeret til et forskelligt scenarie — og GPT-5.2’s ræsonneringskontrol gør forskellene endnu mere tydelige.

<img src="../../../translated_images/da/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Oversigt over de otte prompt engineering-mønstre og deres anvendelsestilfælde*

<img src="../../../translated_images/da/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2’s ræsonneringskontrol lader dig specificere, hvor meget modellen skal tænke — fra hurtige direkte svar til dybdegående udforskning*

<img src="../../../translated_images/da/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Lav ivrighed (hurtigt, direkte) vs høj ivrighed (grundigt, udforskende) ræsonneringsmetoder*

**Lav Ivrighed (Hurtig & Fokusert)** - Til simple spørgsmål, hvor du ønsker hurtige, direkte svar. Modellen laver minimal ræsonnering - maksimalt 2 trin. Brug denne til beregninger, opslag eller simple spørgsmål.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Udforsk med GitHub Copilot:** Åbn [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) og spørg:
> - "Hvad er forskellen mellem lav ivrighed og høj ivrighed i promptingmønstre?"
> - "Hvordan hjælper XML-tags i prompts med at strukturere AI’ens svar?"
> - "Hvornår bør jeg bruge selvrefleksionsmønstre vs direkte instruktion?"

**Høj Ivrighed (Dybdegående & Grundig)** - Til komplekse problemer, hvor du ønsker en omfattende analyse. Modellen udforsker grundigt og viser detaljeret ræsonnering. Brug denne til systemdesign, arkitekturvalg eller kompleks forskning.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Opgaveudførelse (Trin-for-trin fremgang)** - Til flertrinsarbejdsgange. Modellen giver en forudgående plan, beskriver hvert trin, mens det udføres, og giver derefter et resumé. Brug dette til migrationer, implementeringer eller enhver flertrinsproces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting beder eksplicit modellen om at vise sin ræsonneringsproces og forbedrer nøjagtigheden for komplekse opgaver. Nedbrydningen trin-for-trin hjælper både mennesker og AI med at forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om dette mønster:
> - "Hvordan tilpasser jeg opgaveudførelsesmønsteret til langvarige processer?"
> - "Hvad er bedste praksis for strukturering af værktøjspreambler i produktionsapplikationer?"
> - "Hvordan kan jeg indfange og vise løbende statusopdateringer i en brugergrænseflade?"

<img src="../../../translated_images/da/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Udfør → Opsummer arbejdsgang til flertrinsopgaver*

**Selvreflekterende kode** - Til at generere produktionsklar kode. Modellen genererer kode, tjekker den mod kvalitetskriterier og forbedrer den iterativt. Brug dette ved udvikling af nye funktioner eller services.

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

<img src="../../../translated_images/da/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ forbedringscyklus - generer, evaluér, identificér problemer, forbedr, gentag*

**Struktureret analyse** - Til konsistent evaluering. Modellen gennemgår kode ved hjælp af en fast ramme (korrekthed, praksis, ydeevne, sikkerhed). Brug dette til kodegennemgange eller kvalitetsvurderinger.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om struktureret analyse:
> - "Hvordan kan jeg tilpasse analyseframeworket for forskellige typer kodegennemgange?"
> - "Hvad er den bedste måde at parse og agere på struktureret output programmatisk?"
> - "Hvordan sikrer jeg konsistente alvorlighedsniveauer på tværs af forskellige reviewsessioner?"

<img src="../../../translated_images/da/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Fire-kategori rammeværk til konsistente kodegennemgange med alvorlighedsniveauer*

**Multi-Turn Chat** - Til samtaler, der har brug for kontekst. Modellen husker tidligere beskeder og bygger videre på dem. Brug dette til interaktive hjælpesessioner eller komplekse Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/da/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hvordan samtalekontekst akkumuleres over flere omgange, indtil token-grænsen nås*

**Trin-for-trin ræsonnering** - Til problemer, der kræver synlig logik. Modellen viser eksplicit ræsonnering for hvert trin. Brug dette til matematikopgaver, logikpuslespil eller når du har brug for at forstå tankegangen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/da/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Nedbrydning af problemer i eksplicitte logiske trin*

**Begrænset output** - Til svar med specifikke formatkrav. Modellen følger nøje format- og længderegler. Brug dette til resuméer eller når du har brug for præcis outputstruktur.

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

<img src="../../../translated_images/da/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Håndhævelse af specifikke format-, længde- og strukturkrav*

## Brug af eksisterende Azure-ressourcer

**Bekræft deployment:**

Sørg for, at `.env`-filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Note:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8083. Du kan springe start-kommandoerne over og gå direkte til http://localhost:8083.

**Mulighed 1: Brug af Spring Boot Dashboard (anbefalet til VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at administrere alle Spring Boot-applikationer. Du finder den i aktivitetsbjælken til venstre i VS Code (se efter Spring Boot-ikonet).
Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i arbejdsområdet
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationsstatus

Klik blot på afspilningsknappen ved siden af "prompt-engineering" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug af shell-scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodmappe
.\start-all.ps1
```

Eller start kun dette modul:

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

Begge scripts loader automatisk miljøvariable fra rodens `.env` fil og vil bygge JAR’erne, hvis de ikke findes.

> **Note:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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

Åbn http://localhost:8083 i din browser.

**For at stoppe:**

**Bash:**
```bash
./stop.sh  # Kun denne modul
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modul
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Applikationsskærmbilleder

<img src="../../../translated_images/da/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashboard, der viser alle 8 prompt engineering-mønstre med deres karakteristika og anvendelsestilfælde*

## Udforskning af Mønstrene

Webgrænsefladen giver dig mulighed for at eksperimentere med forskellige prompting-strategier. Hvert mønster løser forskellige problemer - prøv dem for at se, hvornår hver tilgang er bedst.

### Lav vs Høj Ivrighed

Spørg et simpelt spørgsmål som "Hvad er 15% af 200?" ved brug af Lav Ivrighed. Du får et øjeblikkeligt, direkte svar. Stil nu noget komplekst som "Design en caching-strategi for en højtrafikeret API" ved brug af Høj Ivrighed. Se hvordan modellen sænker tempoet og leverer detaljeret ræsonnering. Samme model, samme spørgsmålstruktur - men prompten fortæller den, hvor meget tænkning, der kræves.

<img src="../../../translated_images/da/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hurtig beregning med minimal ræsonnering*

<img src="../../../translated_images/da/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende caching-strategi (2.8MB)*

### Opgaveudførelse (Tool Preambles)

Workflow med flere trin drager fordel af forudplanlægning og fremskridt-fortælling. Modellen skitserer, hvad den vil gøre, fortæller om hvert trin, og opsummerer derefter resultater.

<img src="../../../translated_images/da/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Oprettelse af et REST-endpoint med trin-for-trin fortælling (3.9MB)*

### Selvransagende Kode

Prøv "Opret en email-valideringstjeneste". I stedet for blot at generere kode og stoppe, genererer modellen, vurderer den på kvalitetskriterier, identificerer svagheder og forbedrer. Du vil se den iterere, indtil koden opfylder produktionsstandarder.

<img src="../../../translated_images/da/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fuldt udbygget email-valideringstjeneste (5.2MB)*

### Struktureret Analyse

Kodegennemgange kræver konsistente evalueringsrammer. Modellen analyserer kode ved hjælp af faste kategorier (korrekthed, praksis, ydeevne, sikkerhed) med sværhedsgradsniveauer.

<img src="../../../translated_images/da/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rammebaseret kodegennemgang*

### Multi-Turn Chat

Spørg "Hvad er Spring Boot?" og følg straks op med "Vis mig et eksempel". Modellen husker dit første spørgsmål og giver dig et specifikt Spring Boot-eksempel. Uden hukommelse ville det andet spørgsmål være for uklart.

<img src="../../../translated_images/da/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarelse af kontekst over spørgsmål*

### Trin-for-Trin Ræsonnering

Vælg et matematikproblem og prøv det både med Trin-for-Trin Ræsonnering og Lav Ivrighed. Lav ivrighed giver dig bare svaret - hurtigt men uigennemsigtigt. Trin-for-trin viser dig hver beregning og beslutning.

<img src="../../../translated_images/da/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikproblem med eksplicitte trin*

### Begrænset Output

Når du har brug for specifikke formater eller ordantal, sikrer dette mønster streng overholdelse. Prøv at generere et resumé med nøjagtigt 100 ord i punktform.

<img src="../../../translated_images/da/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskinlæringsresumé med formatkontrol*

## Hvad Du Virkeligt Lærer

**Ræsonneringsindsats Ændrer Alt**

GPT-5.2 lader dig styre beregningsindsatsen gennem dine prompts. Lav indsats betyder hurtige svar med minimal udforskning. Høj indsats betyder, at modellen tager tid til at tænke dybt. Du lærer at afstemme indsats efter opgavens kompleksitet - spild ikke tid på simple spørgsmål, men forhast ikke komplekse beslutninger.

**Struktur Styrer Adfærd**

Læg mærke til XML-tags i promptene? De er ikke dekorative. Modeller følger strukturerede instruktioner mere pålideligt end fri tekst. Når du har brug for processer med flere trin eller kompleks logik, hjælper struktur modellen med at holde styr på, hvor den er, og hvad der kommer næste.

<img src="../../../translated_images/da/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi af en veldesignet prompt med klare sektioner og XML-stil organisering*

**Kvalitet Gennem Selvevaluering**

De selvransagende mønstre fungerer ved at gøre kvalitetskriterier eksplicitte. I stedet for at håbe, at modellen "gør det rigtigt", fortæller du den præcis, hvad "rigtigt" betyder: korrekt logik, fejlhåndtering, ydeevne, sikkerhed. Modellen kan så evaluere sit eget output og forbedre det. Det forvandler kodegenerering fra lotteri til en proces.

**Kontekst er Begrænset**

Samtaler med flere runder fungerer ved at inkludere beskedhistorik i hver anmodning. Men der er en grænse - hver model har et maksimalt antal tokens. Efterhånden som samtaler vokser, bliver du nødt til at bruge strategier for at bevare relevant kontekst uden at ramme loftet. Dette modul viser dig, hvordan hukommelse fungerer; senere lærer du, hvornår du skal opsummere, hvornår du skal glemme, og hvornår du skal genkalde.

## Næste Skridt

**Næste Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Forrige: Modul 01 - Introduktion](../01-introduction/README.md) | [Tilbage til Forside](../README.md) | [Næste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->