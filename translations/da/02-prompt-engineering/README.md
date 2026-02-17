# Modul 02: Prompt Engineering med GPT-5.2

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../02-prompt-engineering)
- [Forudsætninger](../../../02-prompt-engineering)
- [Forståelse af Prompt Engineering](../../../02-prompt-engineering)
- [Grundlæggende Principper for Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Skabeloner](../../../02-prompt-engineering)
- [Avancerede Mønstre](../../../02-prompt-engineering)
- [Brug af Eksisterende Azure-Ressourcer](../../../02-prompt-engineering)
- [Applikationsskærmbilleder](../../../02-prompt-engineering)
- [Udforskning af Mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høj Iver](../../../02-prompt-engineering)
  - [Opgaveudførelse (Værktøjsintroduktioner)](../../../02-prompt-engineering)
  - [Selvreflekterende Kode](../../../02-prompt-engineering)
  - [Struktureret Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trin-for-Trin Logik](../../../02-prompt-engineering)
  - [Begrænset Output](../../../02-prompt-engineering)
- [Hvad Du I Virkeligheden Lærer](../../../02-prompt-engineering)
- [Næste Skridt](../../../02-prompt-engineering)

## Hvad Du Vil Lære

<img src="../../../translated_images/da/what-youll-learn.c68269ac048503b2.webp" alt="Hvad Du Vil Lære" width="800"/>

I det forrige modul så du, hvordan hukommelse muliggør samtale-AI, og du brugte GitHub-modeller til basale interaktioner. Nu fokuserer vi på, hvordan du stiller spørgsmål – selve promptsene – ved brug af Azure OpenAI’s GPT-5.2. Den måde, du strukturerer dine prompts på, påvirker dramatisk kvaliteten af de svar, du får. Vi starter med en gennemgang af de grundlæggende prompting-teknikker og bevæger os så videre til otte avancerede mønstre, der udnytter GPT-5.2’s evner fuldt ud.

Vi bruger GPT-5.2, fordi det introducerer styring af ræsonnement – du kan fortælle modellen, hvor meget tænkning den skal gøre, inden den svarer. Det gør forskellige prompting-strategier mere tydelige og hjælper dig med at forstå, hvornår du skal bruge hver tilgang. Vi får også fordel af Azures færre begrænsninger mht. hastighed for GPT-5.2 sammenlignet med GitHub-modeller.

## Forudsætninger

- Fuldført Modul 01 (Azure OpenAI-ressourcer implementeret)
- `.env`-fil i rodkatalog med Azure-legitimationsoplysninger (oprettet af `azd up` i Modul 01)

> **Note:** Hvis du ikke har fuldført Modul 01, følg først implementeringsvejledningen der.

## Forståelse af Prompt Engineering

<img src="../../../translated_images/da/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Hvad er Prompt Engineering?" width="800"/>

Prompt engineering handler om at designe inputtekst, der konsekvent giver dig de resultater, du har brug for. Det handler ikke blot om at stille spørgsmål – det handler om at strukturere forespørgsler, så modellen præcist forstår, hvad du vil, og hvordan det skal leveres.

Tænk på det som at give instruktioner til en kollega. "Ret fejlen" er vagt. "Ret null-pointer-exception i UserService.java linje 45 ved at tilføje en nullcheck" er specifikt. Sprogmodeller fungerer på samme måde – specificitet og struktur betyder noget.

<img src="../../../translated_images/da/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hvordan LangChain4j passer ind" width="800"/>

LangChain4j leverer infrastrukturen – modelforbindelser, hukommelse og beskedtyper – mens promptmønstre blot er omhyggeligt struktureret tekst, du sender gennem infrastrukturen. De centrale byggesten er `SystemMessage` (som sætter AI’ens adfærd og rolle) og `UserMessage` (som bærer din egentlige anmodning).

## Grundlæggende Principper for Prompt Engineering

<img src="../../../translated_images/da/five-patterns-overview.160f35045ffd2a94.webp" alt="Oversigt over fem Prompt Engineering-mønstre" width="800"/>

Før vi dykker ned i de avancerede mønstre i dette modul, lad os gennemgå fem grundlæggende prompting teknikker. Disse er byggesten, som enhver promptingeniør bør kende. Hvis du allerede har arbejdet med [Quick Start-modulet](../00-quick-start/README.md#2-prompt-patterns), har du set dem i aktion – her er det konceptuelle fundament bag dem.

### Zero-Shot Prompting

Den simpleste tilgang: giv modellen en direkte instruktion uden eksempler. Modellen stoler udelukkende på sin træning for at forstå og udføre opgaven. Dette virker godt til simple forespørgsler, hvor forventet adfærd er tydelig.

<img src="../../../translated_images/da/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruktion uden eksempler – modellen udleder opgaven ud fra instruktionen alene*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Hvornår man bruger det:** Enkle klassifikationer, direkte spørgsmål, oversættelser eller enhver opgave modellen kan håndtere uden yderligere vejledning.

### Few-Shot Prompting

Giv eksempler, der demonstrerer det mønster, du vil have modellen til at følge. Modellen lærer det forventede input-output-format ud fra dine eksempler og anvender det på nye input. Dette forbedrer konsistensen markant for opgaver, hvor det ønskede format eller adfærd ikke er åbenlyst.

<img src="../../../translated_images/da/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Læring fra eksempler – modellen identificerer mønsteret og anvender det på nyt input*

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

**Hvornår man bruger det:** Tilpassede klassifikationer, ensartet formatering, domænespecifikke opgaver eller når zero-shot-resultater er inkonsistente.

### Chain of Thought

Bed modellen om at vise sin ræsonnering trin for trin. I stedet for at hoppe direkte til et svar bryder modellen problemet ned og gennemgår hver del eksplicit. Dette forbedrer nøjagtigheden ved matematik, logik og opgaver med flere trin.

<img src="../../../translated_images/da/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trin-for-trin ræsonnering – opdeling af komplekse problemer i eksplicitte logiske trin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, derefter 7 + 12 = 19 æbler
```

**Hvornår man bruger det:** Matematikopgaver, logikpuslespil, fejlfinding eller enhver opgave, hvor at vise ræsonnementet forbedrer nøjagtighed og tillid.

### Role-Based Prompting

Sæt en persona eller rolle for AI’en, før du stiller dit spørgsmål. Dette giver kontekst, som former tonen, dybden og fokus for svaret. En "softwarearkitekt" giver anden rådgivning end en "juniorudvikler" eller en "sikkerhedsrevisor".

<img src="../../../translated_images/da/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Sætter kontekst og persona – det samme spørgsmål får forskelligt svar afhængig af den tildelte rolle*

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

**Hvornår man bruger det:** Kodegennemgange, vejledning, domænespecifik analyse eller når du har brug for svar tilpasset et bestemt ekspertiseniveau eller perspektiv.

### Prompt Skabeloner

Lav genanvendelige prompts med variable pladsholdere. I stedet for at skrive en ny prompt hver gang, definer én skabelon én gang og udfyld med forskellige værdier. LangChain4j’s `PromptTemplate` klasse gør dette nemt med `{{variable}}` syntaks.

<img src="../../../translated_images/da/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Skabeloner" width="800"/>

*Genanvendelige prompts med variable pladsholdere – én skabelon, mange anvendelser*

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

**Hvornår man bruger det:** Gentagne forespørgsler med forskellige input, batch-behandling, opbygning af genanvendelige AI-arbejdsgange eller scenarier hvor prompt-strukturen forbliver den samme, men data ændrer sig.

---

Disse fem grundprincipper giver dig en solid værktøjskasse til de fleste prompting-opgaver. Resten af dette modul bygger videre på dem med **otte avancerede mønstre**, der udnytter GPT-5.2’s ræsonneringskontrol, selvevaluering og strukturerede output-muligheder.

## Avancerede Mønstre

Med grundprincipperne på plads bevæger vi os til de otte avancerede mønstre, som gør dette modul unikt. Ikke alle problemer har brug for samme tilgang. Nogle spørgsmål kræver hurtige svar, andre dyb tænkning. Nogle har brug for synligt ræsonnement, andre kun resultater. Hvert mønster nedenfor er optimeret til et forskelligt scenarie – og GPT-5.2’s ræsonneringskontrol tydeliggør forskellene endnu mere.

<img src="../../../translated_images/da/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Otte Prompting-Mønstre" width="800"/>

*Oversigt over de otte prompt engineering-mønstre og deres anvendelsestilfælde*

<img src="../../../translated_images/da/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Ræsonneringskontrol med GPT-5.2" width="800"/>

*GPT-5.2’s ræsonneringskontrol lader dig specificere, hvor meget tænkning modellen skal gøre – fra hurtige, direkte svar til dybdegående udforskning*

**Lav Iver (Hurtig & Fokuseret)** – Til simple spørgsmål, hvor du ønsker hurtige, direkte svar. Modellen laver minimal ræsonnering – maksimalt 2 trin. Brug dette til beregninger, opslag eller ligetil spørgsmål.

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

> 💡 **Udforsk med GitHub Copilot:** Åbn [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) og spørg:
> - "Hvad er forskellen mellem lav iver og høj iver prompting-mønstre?"
> - "Hvordan hjælper XML-tags i prompts med at strukturere AI’ens svar?"
> - "Hvornår bør jeg bruge selvrefleksion kontra direkte instruktion?"

**Høj Iver (Dyb & Grundig)** – Til komplekse problemer, hvor du ønsker omfattende analyse. Modellen udforsker grundigt og viser detaljeret ræsonnering. Brug dette til systemdesign, arkitektur-beslutninger eller kompleks forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Opgaveudførelse (Trin-for-Trin Fremgang)** – Til workflows med flere trin. Modellen giver en indledende plan, fortæller løbende om hvert trin, mens den arbejder, og giver derefter et resume. Brug dette til migrationer, implementeringer eller andre processer med flere trin.

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

Chain-of-Thought prompting beder eksplicit modellen om at vise sin ræsonneringsproces, hvilket forbedrer nøjagtigheden for komplekse opgaver. Nedbrydningen trin for trin hjælper både mennesker og AI med at forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om dette mønster:
> - "Hvordan kan jeg tilpasse opgaveudførelsesmønstret til langvarige operationer?"
> - "Hvad er bedste praksis for at strukturere værktøjsintroduktioner i produktionsapplikationer?"
> - "Hvordan kan jeg indfange og vise mellemliggende fremskridt i en brugergrænseflade?"

<img src="../../../translated_images/da/task-execution-pattern.9da3967750ab5c1e.webp" alt="Opgaveudførelsesmønster" width="800"/>

*Plan → Udfør → Opsummer arbejdsproces for opgaver med flere trin*

**Selvreflekterende Kode** – Til generering af kode i produktionskvalitet. Modellen genererer kode efter produktionsstandarder med korrekt håndtering af fejl. Brug dette, når du bygger nye funktioner eller services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/da/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Selvrefleksionscyklus" width="800"/>

*Iterativ forbedringscyklus – generer, vurder, identificer problemer, forbedr, gentag*

**Struktureret Analyse** – Til konsekvent evaluering. Modellen gennemgår kode med et fast rammeværk (korrekthed, praksis, ydeevne, sikkerhed, vedligeholdelse). Brug dette til kodegennemgange eller kvalitetsvurderinger.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om struktureret analyse:
> - "Hvordan kan jeg tilpasse analysetilgangen til forskellige typer kodegennemgange?"
> - "Hvad er den bedste måde at parse og handle på struktureret output programmæssigt?"
> - "Hvordan sikrer jeg ensartede alvorlighedsniveauer på tværs af forskellige gennemgangssessioner?"

<img src="../../../translated_images/da/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureret Analyse-mønster" width="800"/>

*Rammeværk for konsekvente kodegennemgange med alvorlighedsniveauer*

**Multi-Turn Chat** – Til samtaler, der har brug for kontekst. Modellen husker tidligere beskeder og bygger videre på dem. Brug dette til interaktive hjælpesessioner eller komplekse spørgsmål og svar.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/da/context-memory.dff30ad9fa78832a.webp" alt="Samtalekontekst" width="800"/>

*Hvordan samtalekontekst akkumuleres over flere omgange indtil token-grænsen nås*

**Trin-for-Trin Logik** – Til problemer, der kræver synlig logik. Modellen viser eksplicit ræsonnering for hvert trin. Brug dette til matematikopgaver, logikpuslespil eller når du vil forstå tænkeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/da/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Trin-for-Trin Mønster" width="800"/>

*Nedbrydning af problemer i eksplicitte logiske trin*

**Begrænset Output** – Til svar med specifikke formateringskrav. Modellen følger strengt format- og længderegler. Brug dette til resuméer eller når du har brug for præcis outputstruktur.

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

<img src="../../../translated_images/da/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Begrænset Output-Mønster" width="800"/>

*Håndhævelse af specifikke formaterings-, længde- og strukturkrav*

## Brug af Eksisterende Azure-Ressourcer

**Verificer implementering:**

Sørg for, at `.env`-filen findes i rodkatalog med Azure-legitimationsoplysninger (oprettet i Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Note:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8083. Du kan springe start-kommandoerne over og gå direkte til http://localhost:8083.

**Mulighed 1: Brug Spring Boot Dashboard (Anbefalet til VS Code-brugere)**

Dev containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at styre alle Spring Boot-applikationer. Du finder den i Activity Bar til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i arbejdsområdet
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationers status
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
cd ..  # Fra rodmappen
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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env`-fil og vil bygge JAR-filerne, hvis de ikke findes.

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

*Det primære dashboard, der viser alle 8 prompt engineering-mønstre med deres karakteristika og anvendelsestilfælde*

## Udforskning af mønstrene

Webgrænsefladen lader dig eksperimentere med forskellige promptstrategier. Hvert mønster løser forskellige problemer - prøv dem for at se, hvornår hver tilgang fungerer bedst.

### Lav vs Høj Iver

Stil et simpelt spørgsmål som "Hvad er 15 % af 200?" med Lav Iver. Du får et øjeblikkeligt, direkte svar. Stil nu noget komplekst som "Design en caching-strategi for en højtrafikeret API" med Høj Iver. Se, hvordan modellen sænker tempoet og leverer detaljeret ræsonnering. Samme model, samme spørgsmålstruktur - men prompten fortæller, hvor meget tænkning der skal til.

<img src="../../../translated_images/da/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hurtig beregning med minimal ræsonnering*

<img src="../../../translated_images/da/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende caching-strategi (2.8MB)*

### Opgaveudførelse (Tool Preambles)

Workflow med flere trin har gavn af forudgående planlægning og løbende beretning. Modellen skitserer, hvad den vil gøre, beretter hvert trin og opsummerer derefter resultaterne.

<img src="../../../translated_images/da/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Oprettelse af et REST-endpoint med trin-for-trin-beretning (3.9MB)*

### Selvreflekterende kode

Prøv "Opret en e-mail valideringstjeneste". I stedet for kun at generere kode og stoppe, genererer modellen, evaluerer op imod kvalitetskriterier, identificerer svagheder og forbedrer. Du vil se den gentage processen, indtil koden opfylder produktionsstandarder.

<img src="../../../translated_images/da/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Færdig e-mail valideringstjeneste (5.2MB)*

### Struktureret analyse

Kodegennemgange kræver konsistente evalueringsrammer. Modellen analyserer kode ved hjælp af faste kategorier (korrekthed, praksis, ydeevne, sikkerhed) med alvorlighedsniveauer.

<img src="../../../translated_images/da/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rammebaseret kodegennemgang*

### Multi-turn chat

Spørg "Hvad er Spring Boot?" og følg straks op med "Vis mig et eksempel". Modellen husker dit første spørgsmål og giver dig et specifikt Spring Boot-eksempel. Uden hukommelse ville det andet spørgsmål være for vagt.

<img src="../../../translated_images/da/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarelse af kontekst på tværs af spørgsmål*

### Trin-for-trin ræsonnering

Vælg et matematikproblem og prøv det både med Trin-for-trin Ræsonnering og Lav Iver. Lav iver giver dig bare svaret - hurtigt men uigennemsigtigt. Trin-for-trin viser dig hver beregning og beslutning.

<img src="../../../translated_images/da/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikproblem med eksplicitte trin*

### Begrænset output

Når du har brug for specifikke formater eller ordantal, håndhæver dette mønster streng overholdelse. Prøv at generere et resumé med præcis 100 ord i punktform.

<img src="../../../translated_images/da/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskinlæringsresumé med formatkontrol*

## Hvad du virkelig lærer

**Ræsonneringsindsatsen ændrer alt**

GPT-5.2 lader dig kontrollere den beregningsmæssige indsats gennem dine prompts. Lav indsats betyder hurtige svar med minimal udforskning. Høj indsats betyder, at modellen tager sig tid til at tænke dybt. Du lærer at afstemme indsatsen efter opgavens kompleksitet - spild ikke tid på simple spørgsmål, men forhast heller ikke komplekse beslutninger.

**Struktur styrer adfærden**

Læg mærke til XML-tags i promptene? De er ikke pynt. Modeller følger strukturerede instruktioner mere pålideligt end frit tekstinput. Når du har brug for processer med flere trin eller kompleks logik, hjælper struktur modellen med at holde styr på, hvor den er, og hvad der kommer næste.

<img src="../../../translated_images/da/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi af en velstruktureret prompt med klare sektioner og XML-lignende organisering*

**Kvalitet gennem selv-evaluering**

De selvreflekterende mønstre fungerer ved at gøre kvalitetskriterier eksplicitte. I stedet for at håbe på, at modellen "gør det rigtigt", fortæller du den præcis, hvad "rigtigt" betyder: korrekt logik, fejlhåndtering, ydeevne, sikkerhed. Modellen kan så evaluere sit eget output og forbedre det. Det ændrer kodegenerering fra et lotteri til en proces.

**Kontekst er begrænset**

Samtaler over flere runder fungerer ved at inkludere beskedhistorik med hver anmodning. Men der er en grænse - hver model har et maksimum for token-antallet. Efterhånden som samtaler vokser, har du brug for strategier til at bevare relevant kontekst uden at ramme loftet. Dette modul viser dig, hvordan hukommelse fungerer; senere lærer du, hvornår du skal opsummere, hvornår du skal glemme, og hvornår du skal hente.

## Næste skridt

**Næste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Forrige: Modul 01 - Introduktion](../01-introduction/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->