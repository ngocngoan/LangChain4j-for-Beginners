# Modul 02: Prompt Engineering med GPT-5.2

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../02-prompt-engineering)
- [Forudsætninger](../../../02-prompt-engineering)
- [Forståelse af Prompt Engineering](../../../02-prompt-engineering)
- [Grundlæggende i Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Avancerede Mønstre](../../../02-prompt-engineering)
- [Brug af Eksisterende Azure-Ressourcer](../../../02-prompt-engineering)
- [Applikationsskærmbilleder](../../../02-prompt-engineering)
- [Udforskning af Mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høj Iver](../../../02-prompt-engineering)
  - [Udførelse af Opgaver (Tool Preambles)](../../../02-prompt-engineering)
  - [Selvreflekterende Kode](../../../02-prompt-engineering)
  - [Struktureret Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trin-for-Trin Resonnering](../../../02-prompt-engineering)
  - [Begrænset Output](../../../02-prompt-engineering)
- [Hvad Du Virkelig Lærer](../../../02-prompt-engineering)
- [Næste Skridt](../../../02-prompt-engineering)

## Hvad Du Vil Lære

<img src="../../../translated_images/da/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I den forrige modul så du, hvordan hukommelse muliggør konversationel AI og brugte GitHub Models til grundlæggende interaktioner. Nu fokuserer vi på, hvordan du stiller spørgsmål – selve prompts – ved brug af Azure OpenAI's GPT-5.2. Måden du strukturerer dine prompts på, påvirker dramatisk kvaliteten af svarene, du får. Vi starter med en gennemgang af de grundlæggende prompting-teknikker og bevæger os derefter ind i otte avancerede mønstre, der udnytter GPT-5.2's kapaciteter fuldt ud.

Vi bruger GPT-5.2, fordi det introducerer kontrol over resonnement – du kan fortælle modellen, hvor meget tænkning den skal udføre før svar. Det gør forskellige prompting-strategier mere tydelige og hjælper dig med at forstå, hvornår du skal bruge hver tilgang. Vi får også fordel af Azures færre ratebegrænsninger for GPT-5.2 sammenlignet med GitHub Models.

## Forudsætninger

- Fuldført Modul 01 (Azure OpenAI-ressourcer implementeret)
- `.env` fil i rodmappen med Azure legitimationsoplysninger (oprettet af `azd up` i Modul 01)

> **Note:** Hvis du ikke har fuldført Modul 01, følg først implementeringsinstruktionerne der.

## Forståelse af Prompt Engineering

<img src="../../../translated_images/da/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handler om at designe inputtekst, der konsekvent giver dig de resultater, du har brug for. Det handler ikke blot om at stille spørgsmål – det handler om at strukturere forespørgsler, så modellen præcist forstår, hvad du vil have, og hvordan det skal leveres.

Tænk på det som at give instruktioner til en kollega. "Ret fejlen" er upræcist. "Ret null pointer exception i UserService.java linje 45 ved at tilføje en null-check" er specifikt. Sproglige modeller fungerer på samme måde – præcision og struktur betyder noget.

<img src="../../../translated_images/da/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j leverer infrastrukturen — modelforbindelser, hukommelse og meddelelsestyper — mens prompt-mønstre blot er omhyggeligt struktureret tekst, du sender gennem denne infrastruktur. De vigtigste byggesten er `SystemMessage` (som sætter AI's adfærd og rolle) og `UserMessage` (som indeholder din egentlige anmodning).

## Grundlæggende i Prompt Engineering

<img src="../../../translated_images/da/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Før vi dykker ned i de avancerede mønstre i denne modul, lad os gennemgå fem grundlæggende prompting-teknikker. Det er byggesten, som enhver prompt-ingeniør bør kende. Hvis du allerede har arbejdet igennem [Quick Start modulet](../00-quick-start/README.md#2-prompt-patterns), har du set disse i praksis – her er det konceptuelle rammeværk bag dem.

### Zero-Shot Prompting

Den simpleste tilgang: giv modellen en direkte instruktion uden eksempler. Modellen baserer sig udelukkende på sin træning for at forstå og udføre opgaven. Dette fungerer godt til ligefremme forespørgsler, hvor forventet adfærd er åbenlys.

<img src="../../../translated_images/da/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruktion uden eksempler — modellen udleder opgaven alene ud fra instruktionen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Hvornår du skal bruge:** Enkle klassifikationer, direkte spørgsmål, oversættelser eller enhver opgave, som modellen kan håndtere uden yderligere vejledning.

### Few-Shot Prompting

Giv eksempler, der demonstrerer det mønster, du vil have modellen til at følge. Modellen lærer den forventede input-output-form fra dine eksempler og anvender den på nye input. Det forbedrer markant konsistensen i opgaver, hvor ønsket format eller adfærd ikke er åbenlys.

<img src="../../../translated_images/da/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Læring fra eksempler — modellen identificerer mønstret og anvender det på nye input*

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

**Hvornår du skal bruge:** Tilpassede klassifikationer, konsekvent formatering, domænespecifikke opgaver eller når zero-shot resultater er inkonsekvente.

### Chain of Thought

Bed modellen om at vise sit resonnement trin for trin. I stedet for at springe direkte til et svar, opdeler modellen problemet og arbejder sig igennem hver del eksplicit. Det forbedrer nøjagtigheden i matematik, logik og flerstegs-resonnering.

<img src="../../../translated_images/da/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trin-for-trin resonnement — opdeler komplekse problemer i eksplicitte logiske skridt*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, derefter 7 + 12 = 19 æbler
```

**Hvornår du skal bruge:** Matematikopgaver, logikgåder, debugging eller enhver opgave, hvor visning af resonnement øger præcision og tillid.

### Role-Based Prompting

Sæt en persona eller rolle for AI’en, før du stiller dit spørgsmål. Det giver kontekst, som former tonen, dybden og fokus i svaret. En "softwarearkitekt" giver andre råd end en "juniorudvikler" eller en "sikkerhedsrevisor".

<img src="../../../translated_images/da/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Opsætning af kontekst og persona — det samme spørgsmål får forskellige svar afhængigt af tildelt rolle*

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

**Hvornår du skal bruge:** Kodegennemgange, vejledning, domænespecifik analyse eller når du har brug for svar tilpasset et bestemt ekspertiseniveau eller perspektiv.

### Prompt Templates

Opret genanvendelige prompts med variable pladsholdere. I stedet for at skrive en ny prompt hver gang, definer en skabelon én gang og indsæt forskellige værdier. LangChain4j's `PromptTemplate`-klasse gør dette nemt med `{{variable}}` syntax.

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

**Hvornår du skal bruge:** Gentagne forespørgsler med forskelligt input, batch-behandling, opbygning af genanvendelige AI-arbejdsgange eller enhver situation, hvor promptstrukturen forbliver den samme, men data ændres.

---

Disse fem grundlæggende giver dig et solidt værktøjssæt til de fleste prompting-opgaver. Resten af modulet bygger videre på dem med **otte avancerede mønstre**, der udnytter GPT-5.2's kontrol over resonnement, selvevaluering og strukturerede output.

## Avancerede Mønstre

Med grundlæggende dækket, lad os gå videre til de otte avancerede mønstre, som gør dette modul unikt. Ikke alle problemer behøver samme tilgang. Nogle spørgsmål kræver hurtige svar, andre dyb tænkning. Nogle behøver synligt resonnement, andre blot resultater. Hvert mønster nedenfor er optimeret til et forskelligt scenarie – og GPT-5.2’s kontrol over resonnement gør forskellene endnu mere tydelige.

<img src="../../../translated_images/da/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Oversigt over de otte prompt engineering-mønstre og deres brugstilfælde*

<img src="../../../translated_images/da/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2's kontrol over resonnement lader dig specificere, hvor meget tænkning modellen skal udføre – fra hurtige direkte svar til dyb udforskning*

<img src="../../../translated_images/da/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Lav iver (hurtig, direkte) vs høj iver (grundig, undersøgende) resonnementstilgange*

**Lav Iver (Hurtigt & Fokuseret)** - Til simple spørgsmål, hvor du ønsker hurtige, direkte svar. Modellen foretager minimal resonnement - maksimum 2 trin. Brug dette til beregninger, opslag eller ligefremme spørgsmål.

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
> - "Hvordan hjælper XML-tags i prompts med at strukturere AI'ens svar?"
> - "Hvornår skal jeg bruge selvrefleksion-mønstre vs direkte instruktion?"

**Høj Iver (Dyb & Grundig)** - Til komplekse problemer, hvor du ønsker en omfattende analyse. Modellen undersøger grundigt og viser detaljeret resonnement. Brug dette til systemdesign, arkitekturvalg eller kompleks forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Udførelse af Opgaver (Trin-for-Trin Fremgang)** - Til arbejdsprocesser med flere trin. Modellen giver en indledende plan, forklarer hvert trin under arbejdet, og giver derefter en opsummering. Brug dette til migrationer, implementeringer eller andre flerstegsprocesser.

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

Chain-of-Thought prompting beder eksplicit modellen om at vise sit resonnement, hvilket forbedrer nøjagtighed for komplekse opgaver. Den trin-for-trin opdeling hjælper både mennesker og AI med at forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om dette mønster:
> - "Hvordan kan jeg tilpasse task execution-mønstret til langvarige operationer?"
> - "Hvad er bedste praksis for at strukturere tool preambles i produktionsapplikationer?"
> - "Hvordan kan jeg indfange og vise mellemliggende fremskridt i en UI?"

<img src="../../../translated_images/da/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Udfør → Opsummer arbejdsgang for flerstegsopgaver*

**Selvreflekterende Kode** - Til generering af kode i produktionskvalitet. Modellen genererer kode, der følger produktionsstandarder med ordentlig fejlhåndtering. Brug dette ved opbygning af nye funktioner eller services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/da/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ forbedringscyklus – generer, evaluer, identificer problemer, forbedr, gentag*

**Struktureret Analyse** - Til konsekvent evaluering. Modellen gennemgår kode ved brug af et fast rammeværk (korrekthed, praksis, ydeevne, sikkerhed, vedligeholdelse). Brug dette til kodegennemgange eller kvalitetsvurderinger.

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
> - "Hvordan kan jeg tilpasse analyse-rammeværket for forskellige typer kodegennemgange?"
> - "Hvad er den bedste måde at parse og agere på struktureret output programmæssigt?"
> - "Hvordan sikrer jeg konsekvente alvorlighedsniveauer på tværs af forskellige review-sessioner?"

<img src="../../../translated_images/da/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Rammeværk til konsekvente kodegennemgange med alvorlighedsniveauer*

**Multi-Turn Chat** - Til samtaler, der behøver kontekst. Modellen husker tidligere beskeder og bygger videre på dem. Brug dette til interaktive hjælpesessioner eller kompleks Q&A.

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

*Hvordan samtalekontekst ophobes over flere runder indtil token-grænsen nås*

**Trin-for-Trin Resonnering** - Til problemer, der kræver synlig logik. Modellen viser eksplicit resonnement for hvert trin. Brug dette til matematikproblemer, logikgåder eller når du skal forstå tænkeprocessen.

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

*Opdeling af problemer i eksplicitte logiske trin*

**Begrænset Output** - Til svar med specifikke formatkrav. Modellen følger strengt formaterings- og længderegler. Brug dette til opsummeringer eller når du har brug for præcis outputstruktur.

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

## Brug af Eksisterende Azure-Ressourcer

**Bekræft implementering:**

Sørg for, at `.env` filen findes i rodmappen med Azure legitimationsoplysninger (oprettet under Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Note:** Hvis du allerede har startet alle applikationer via `./start-all.sh` fra Modul 01, kører denne modul allerede på port 8083. Du kan springe start-kommandoerne over nedenfor og gå direkte til http://localhost:8083.

**Option 1: Brug af Spring Boot Dashboard (Anbefalet til VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at administrere alle Spring Boot-applikationer. Du kan finde den i aktivitetsbjælken på venstre side i VS Code (se efter Spring Boot-ikonet).
Fra Spring Boot Dashboard, kan du:
- Se alle tilgængelige Spring Boot-applikationer i arbejdsområdet
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationsstatus

Klik blot på afspilningsknappen ved siden af "prompt-engineering" for at starte denne modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug af shell-scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodbiblioteket
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodkatalog
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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env`-fil og bygger JAR-filene, hvis de ikke findes.

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

*Hoveddashboardet viser alle 8 prompt engineering mønstre med deres karakteristika og anvendelsestilfælde*

## Udforskning af mønstrene

Webinterfacet lader dig eksperimentere med forskellige prompt-strategier. Hvert mønster løser forskellige problemer – prøv dem for at se, hvornår hver fremgangsmåde fungerer bedst.

### Lav vs Høj Iver

Stil et simpelt spørgsmål som "Hvad er 15% af 200?" brugende Lav Iver. Du får et øjeblikkeligt, direkte svar. Stil nu noget komplekst som "Design en cache-strategi til en højtrafikeret API" brugende Høj Iver. Se hvordan modellen sænker tempoet og giver detaljeret begrundelse. Samme model, samme spørgsmålstruktur – men prompten fortæller, hvor meget tænkning der skal til.

<img src="../../../translated_images/da/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hurtig beregning med minimal begrundelse*

<img src="../../../translated_images/da/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende cache-strategi (2.8MB)*

### Opgaveudførelse (Tool Preambles)

Multi-trins arbejdsgange drager fordel af forudgående planlægning og narrativ fremdrift. Modellen skitserer, hvad den vil gøre, fortæller om hvert trin og opsummerer derefter resultater.

<img src="../../../translated_images/da/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Oprettelse af en REST-endpoint med trin-for-trin fortælling (3.9MB)*

### Selvreflekterende kode

Prøv "Opret en e-mail valideringstjeneste". I stedet for blot at generere kode og stoppe, genererer modellen, evaluerer efter kvalitetskriterier, identificerer svagheder og forbedrer. Du vil se den iterere, indtil koden opfylder produktionsstandarder.

<img src="../../../translated_images/da/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Komplet e-mail valideringstjeneste (5.2MB)*

### Struktureret analyse

Kodegennemgange kræver konsistente evalueringsrammer. Modellen analyserer kode med faste kategorier (korrekthed, praksis, ydeevne, sikkerhed) med alvorlighedsniveauer.

<img src="../../../translated_images/da/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rammeværksbaseret kodegennemgang*

### Multi-Turn Chat

Spørg "Hvad er Spring Boot?" og følg straks op med "Vis mig et eksempel". Modellen husker dit første spørgsmål og giver dig et specifikt Spring Boot-eksempel. Uden hukommelse ville det andet spørgsmål være for vagt.

<img src="../../../translated_images/da/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarelse af kontekst på tværs af spørgsmål*

### Trin-for-trin begrundelse

Vælg en matematikopgave og prøv den med både Trin-for-trin begrundelse og Lav Iver. Lav iver giver dig bare svaret – hurtigt men uigennemsigtigt. Trin-for-trin viser dig hver beregning og beslutning.

<img src="../../../translated_images/da/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikopgave med eksplicitte trin*

### Begrænset output

Når du har brug for specifikke formater eller ordantal, håndhæver dette mønster streng overholdelse. Prøv at generere et resumé med præcis 100 ord i punktform.

<img src="../../../translated_images/da/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskinlæringsresumé med formatkontrol*

## Hvad du virkelig lærer

**Begrundelsesindsats ændrer alt**

GPT-5.2 lader dig kontrollere beregningsindsatsen via dine prompts. Lav indsats betyder hurtige svar med minimal udforskning. Høj indsats betyder, at modellen tager sig tid til at tænke dybt. Du lærer at matche indsats med opgavens kompleksitet – spild ikke tid på simple spørgsmål, men forhast heller ikke komplekse beslutninger.

**Struktur guider adfærd**

Læg mærke til XML-tags i promptene? De er ikke dekorative. Modeller følger strukturerede instruktioner mere pålideligt end frit tekstformateret. Når du har brug for multi-trins processer eller kompleks logik, hjælper struktur modellen med at holde styr på, hvor den er, og hvad der kommer næste.

<img src="../../../translated_images/da/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi af en velstruktureret prompt med klare sektioner og XML-style organisering*

**Kvalitet gennem selvevaluering**

De selvreflekterende mønstre fungerer ved at gøre kvalitetskriterier eksplicitte. I stedet for at håbe modellen "gør det rigtigt", fortæller du præcis, hvad "rigtigt" betyder: korrekt logik, fejlhåndtering, ydeevne, sikkerhed. Modellen kan dermed evaluere eget output og forbedre det. Det gør kodegenerering til en proces fremfor lotteri.

**Kontekst er begrænset**

Multi-turn samtaler fungerer ved at inkludere beskedhistorik med hver anmodning. Men der er en grænse – hver model har maksimalt tokenantal. Når samtaler vokser, skal du bruge strategier for at bevare relevant kontekst uden at nå loftet. Dette modul viser dig, hvordan hukommelse fungerer; senere lærer du, hvornår du skal opsummere, glemme og hente.

## Næste trin

**Næste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Forrige: Modul 01 - Introduktion](../01-introduction/README.md) | [Tilbage til hoved](../README.md) | [Næste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er oversat ved hjælp af AI-oversættelsesservicen [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For vigtig information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for eventuelle misforståelser eller fejltolkninger, der måtte opstå ved brug af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->