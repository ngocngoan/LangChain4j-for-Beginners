# Modul 02: Prompt Engineering med GPT-5.2

## Indholdsfortegnelse

- [Video Gennemgang](../../../02-prompt-engineering)
- [Det Du Vil Lære](../../../02-prompt-engineering)
- [Forudsætninger](../../../02-prompt-engineering)
- [Forståelse af Prompt Engineering](../../../02-prompt-engineering)
- [Grundlæggende Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Skabeloner](../../../02-prompt-engineering)
- [Avancerede Mønstre](../../../02-prompt-engineering)
- [Kør Applikationen](../../../02-prompt-engineering)
- [Applikationsskærmbilleder](../../../02-prompt-engineering)
- [Udforskning af Mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høj Iver](../../../02-prompt-engineering)
  - [Opgaveudførelse (Tool Preambles)](../../../02-prompt-engineering)
  - [Selvreflekterende Kode](../../../02-prompt-engineering)
  - [Struktureret Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trin-for-Trin Resonnering](../../../02-prompt-engineering)
  - [Begrænset Output](../../../02-prompt-engineering)
- [Hvad Du Virkelig Lærer](../../../02-prompt-engineering)
- [Næste Skridt](../../../02-prompt-engineering)

## Video Gennemgang

Se denne live-session, der forklarer, hvordan du kommer i gang med dette modul:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Det Du Vil Lære

Følgende diagram giver et overblik over de nøgleemner og færdigheder, du vil udvikle i dette modul — fra promptforfiningsteknikker til den trin-for-trin arbejdsgang, du vil følge.

<img src="../../../translated_images/da/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I de tidligere moduler undersøgte du grundlæggende LangChain4j-interaktioner med GitHub-modeller og så, hvordan hukommelse muliggør konversationel AI med Azure OpenAI. Nu vil vi fokusere på, hvordan du stiller spørgsmål — selve promptene — ved brug af Azure OpenAI's GPT-5.2. Den måde, du strukturerer dine prompts på, påvirker dramatisk kvaliteten af de svar, du får. Vi starter med en gennemgang af de fundamentale prompting-teknikker og går derefter videre til otte avancerede mønstre, der udnytter GPT-5.2's kapaciteter fuldt ud.

Vi bruger GPT-5.2, fordi den introducerer kontrol over ræsonnement - du kan fortælle modellen, hvor meget tænkning der skal foretages, før den svarer. Dette gør forskellige prompting-strategier mere tydelige og hjælper dig med at forstå, hvornår hver tilgang skal anvendes. Vi vil også drage fordel af Azures færre hastighedsbegrænsninger for GPT-5.2 sammenlignet med GitHub models.

## Forudsætninger

- Gennemført Modul 01 (Azure OpenAI ressourcer implementeret)
- `.env` fil i rodmappen med Azure legitimationsoplysninger (oprettet via `azd up` i Modul 01)

> **Note:** Hvis du ikke har gennemført Modul 01, følg først implementeringsinstruktionerne der.

## Forståelse af Prompt Engineering

I sin kerne er prompt engineering forskellen mellem vage instruktioner og præcise, som sammenligningen nedenfor illustrerer.

<img src="../../../translated_images/da/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handler om at designe inputtekst, der konsekvent giver dig de resultater, du har brug for. Det handler ikke kun om at stille spørgsmål - det handler om at strukturere forespørgsler, så modellen forstår præcis, hvad du vil, og hvordan det skal leveres.

Tænk på det som at give instruktioner til en kollega. "Fix fejlen" er vag. "Fix null-pointer exception i UserService.java linje 45 ved at tilføje en null-tjek" er specifik. Sprogmodeller fungerer på samme måde - specificitet og struktur betyder noget.

Diagrammet nedenfor viser, hvordan LangChain4j passer ind i dette billede — ved at forbinde dine promptmønstre til modellen gennem SystemMessage og UserMessage byggesten.

<img src="../../../translated_images/da/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j leverer infrastrukturen — modelforbindelser, hukommelse og meddelelsestyper — mens promptmønstre blot er nøje struktureret tekst, du sender gennem infrastrukturen. De nøglebyggesten er `SystemMessage` (som sætter AI’ens adfærd og rolle) og `UserMessage` (som indeholder din faktiske forespørgsel).

## Grundlæggende Prompt Engineering

De fem kerne-teknikker vist nedenfor udgør fundamentet for effektiv prompt engineering. Hver adresserer en anden del af, hvordan du kommunikerer med sprogmodeller.

<img src="../../../translated_images/da/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Før vi dykker ned i de avancerede mønstre i dette modul, lad os gennemgå fem grundlæggende prompting-teknikker. Disse er byggesten, som enhver promptingeniør bør kende. Hvis du allerede har arbejdet med [Quick Start modulet](../00-quick-start/README.md#2-prompt-patterns), har du set dem i praksis — her er det konceptuelle grundlag bag dem.

### Zero-Shot Prompting

Den simpleste tilgang: giv modellen en direkte instruktion uden eksempler. Modellen baserer sig udelukkende på sin træning for at forstå og udføre opgaven. Dette fungerer godt for ligetil forespørgsler, hvor den forventede adfærd er åbenlys.

<img src="../../../translated_images/da/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkte instruktion uden eksempler — modellen tolker opgaven ud fra instruktionen alene*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**Hvornår man skal bruge:** Enkle klassifikationer, direkte spørgsmål, oversættelser eller enhver opgave, modellen kan håndtere uden yderligere vejledning.

### Few-Shot Prompting

Giv eksempler, der demonstrerer det mønster, du vil have modellen til at følge. Modellen lærer det forventede input-output-format fra dine eksempler og anvender det på nye input. Dette forbedrer dramatisk konsistensen for opgaver, hvor det ønskede format eller adfærd ikke er åbenlys.

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

**Hvornår man skal bruge:** Tilpassede klassifikationer, konsistent formatering, domænespecifikke opgaver eller når zero-shot resultater er inkonsistente.

### Chain of Thought

Bed modellen om at vise sin ræsonnering trin-for-trin. I stedet for at springe direkte til et svar, nedbryder modellen problemet og arbejder gennem hver del eksplicit. Dette forbedrer nøjagtigheden på matematik, logik og komplekse ræsonnement-opgaver.

<img src="../../../translated_images/da/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Trinvis ræsonnering — nedbrydning af komplekse problemer i eksplicitte logiske trin*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen viser: 15 - 8 = 7, derefter 7 + 12 = 19 æbler
```

**Hvornår man skal bruge:** Matematikopgaver, logikpuslespil, fejlfinding eller enhver opgave, hvor det at vise ræsonnementet forbedrer nøjagtigheden og tilliden.

### Role-Based Prompting

Sæt en persona eller rolle på AI’en, før du stiller dit spørgsmål. Dette giver kontekst, som former tonen, dybden og fokus i svaret. En "softwarearkitekt" giver anderledes rådgivning end en "juniorudvikler" eller en "sikkerhedsauditor".

<img src="../../../translated_images/da/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Sætter kontekst og persona — det samme spørgsmål får et forskelligt svar afhængigt af den tildelte rolle*

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

**Hvornår man skal bruge:** Code reviews, vejledning, domænespecifik analyse eller når du har brug for svar tilpasset en bestemt ekspertisegrad eller perspektiv.

### Prompt Skabeloner

Lav genanvendelige prompts med variable pladsholdere. I stedet for at skrive en ny prompt hver gang, definer én skabelon og udfyld forskellige værdier. LangChain4j's `PromptTemplate` klasse gør dette nemt med `{{variable}}` syntaks.

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

**Hvornår man skal bruge:** Gentagne forespørgsler med forskellige input, batch-behandling, opbygning af genanvendelige AI-arbejdsgange eller enhver situation, hvor promptstrukturen forbliver den samme, men dataene ændres.

---

Disse fem grundprincipper giver dig et solidt værktøjssæt til de fleste prompting-opgaver. Resten af dette modul bygger videre på dem med **otte avancerede mønstre**, der udnytter GPT-5.2's kontrol over ræsonnement, selvevaluering og struktureret output.

## Avancerede Mønstre

Med grundprincipperne dækket, lad os gå over til de otte avancerede mønstre, der gør dette modul unikt. Ikke alle problemer kræver den samme tilgang. Nogle spørgsmål kræver hurtige svar, andre dyb tænkning. Nogle kræver synligt ræsonnement, andre blot resultater. Hvert mønster nedenfor er optimeret til en forskellig situation — og GPT-5.2's ræsonnementskontrol gør forskellene endnu mere tydelige.

<img src="../../../translated_images/da/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Oversigt over de otte prompt engineering mønstre og deres brugsscenarier*

GPT-5.2 tilføjer dimensionen *ræsonnementskontrol* til disse mønstre. Slideren nedenfor viser, hvordan du kan justere modellens tænkearbejde — fra hurtige, direkte svar til dyb, grundig analyse.

<img src="../../../translated_images/da/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2's ræsonnementskontrol lader dig specificere, hvor meget tænkning modellen skal gøre — fra hurtige, direkte svar til dyb udforskning*

**Lav Iver (Hurtigt & Fokuseret)** - Til simple spørgsmål, hvor du ønsker hurtige, direkte svar. Modellen udfører minimal ræsonnering - maks 2 trin. Brug dette til beregninger, opslag eller ligetil spørgsmål.

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
> - "Hvad er forskellen på lav og høj iver i prompting-mønstre?"
> - "Hvordan hjælper XML-tags i prompts med at strukturere AI’ens svar?"
> - "Hvornår skal jeg bruge selvrefleksion fremfor direkte instruktion?"

**Høj Iver (Dyb & Grundig)** - Til komplekse problemer, hvor du ønsker dybdegående analyse. Modellen udforsker grundigt og viser detaljeret ræsonnering. Brug dette til systemdesign, arkitekturvalg eller kompleks forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Opgaveudførelse (Trin-for-Trin Fremdrift)** - Til multi-trins arbejdsgange. Modellen giver en forudgående plan, fortæller om hvert trin undervejs, og afslutter med en opsummering. Brug dette til migreringer, implementeringer eller enhver fler-trins proces.

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

Chain-of-Thought prompting beder eksplicit modellen om at vise sin ræsonneringsproces, hvilket forbedrer nøjagtigheden ved komplekse opgaver. Den trin-for-trin nedbrydning hjælper både mennesker og AI med at forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg ind til dette mønster:
> - "Hvordan kan jeg tilpasse opgaveudførelsesmønstret til langvarige operationer?"
> - "Hvad er bedste praksis til at strukturere tool preambles i produktionsapplikationer?"
> - "Hvordan kan jeg fange og vise mellemliggende fremdriftsopdateringer i en UI?"

Diagrammet nedenfor illustrerer denne Plan → Udfør → Opsummer arbejdsgang.

<img src="../../../translated_images/da/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Udfør → Opsummer arbejdsgang for fler-trins opgaver*

**Selvreflekterende Kode** - Til generering af produktionsklar kode. Modellen genererer kode, der følger produktionsstandarder med korrekt fejlhåndtering. Brug dette når du bygger nye funktioner eller services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Diagrammet nedenfor viser denne iterative forbedrings-cyklus — generer, evaluer, identificer svagheder og forfin indtil koden lever op til produktionsstandarderne.

<img src="../../../translated_images/da/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ forbedringscyklus - generer, evaluer, identificer problemer, forbedr, gentag*

**Struktureret Analyse** - Til konsistent evaluering. Modellen gennemgår kode med en fast ramme (korrekthed, praksis, ydeevne, sikkerhed, vedligeholdelsesvenlighed). Brug dette til code reviews eller kvalitetsvurderinger.

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
> - "Hvordan kan jeg tilpasse analyse-rammen til forskellige typer kodegennemgange?"
> - "Hvad er den bedste måde at programmere en fortolkning og handling på struktureret output?"
> - "Hvordan sikrer jeg konsistente alvorlighedsniveauer på tværs af forskellige anmeldelsessessioner?"

Følgende diagram viser, hvordan denne strukturerede ramme organiserer en code review i konsistente kategorier med alvorlighedsniveauer.

<img src="../../../translated_images/da/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Ramme for konsistente code reviews med alvorlighedsniveauer*

**Multi-Turn Chat** - Til samtaler, der har brug for kontekst. Modellen husker tidligere beskeder og bygger videre på dem. Brug dette til interaktive hjælpesessioner eller komplekse spørgsmål og svar.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Diagrammet nedenfor visualiserer, hvordan samtalens kontekst akkumuleres for hvert svar og hvordan det relaterer til modellens token-grænse.

<img src="../../../translated_images/da/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hvordan samtalens kontekst ophobes gennem flere runder indtil token-grænsen nås*
**Trin-for-trin ræsonnering** - Til problemer, der kræver synlig logik. Modellen viser eksplicit ræsonnering for hvert trin. Brug dette til matematiske problemer, logikpuslespil eller når du skal forstå tankeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagrammet nedenfor illustrerer, hvordan modellen opdeler problemer i eksplicitte, nummererede logiske trin.

<img src="../../../translated_images/da/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Nedbrydning af problemer i eksplicitte logiske trin*

**Begrænset output** - Til svar med specifikke formatkrav. Modellen følger streng format- og længderegler. Brug dette til sammendrag eller når du har brug for præcist outputformat.

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

Følgende diagram viser, hvordan begrænsninger guider modellen til at producere output, der strikt overholder dine format- og længdekrav.

<img src="../../../translated_images/da/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Håndhævelse af specifikke format-, længde- og strukturkrav*

## Kør applikationen

**Bekræft udrulning:**

Sørg for, at `.env`-filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01). Kør dette fra modulets mappe (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Bemærk:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra rodmappen (som beskrevet i Modul 01), kører dette modul allerede på port 8083. Du kan springe start-kommandoerne nedenfor over og gå direkte til http://localhost:8083.

**Mulighed 1: Brug Spring Boot Dashboard (anbefalet til VS Code brugere)**

Dev containeren inkluderer Spring Boot Dashboard udvidelsen, som giver et visuelt interface til at administrere alle Spring Boot applikationer. Du finder den i Aktivitetslinjen til venstre i VS Code (se efter Spring Boot ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot applikationer i workspace
- Starte/stoppe applikationer med ét klik
- Se applikationslogfiler i realtid
- Overvåge applikationsstatus

Klik blot på afspilningsknappen ud for "prompt-engineering" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stop og overvåg alle moduler fra ét sted*

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodkataloget
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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env`-fil og bygger JAR-filerne, hvis de ikke findes.

> **Bemærk:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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

Her er hovedgrænsefladen for prompt engineering modulet, hvor du kan eksperimentere med alle otte mønstre side om side.

<img src="../../../translated_images/da/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashboard, der viser alle 8 prompt engineering mønstre med deres karakteristika og anvendelsestilfælde*

## Udforskning af mønstrene

Webgrænsefladen lader dig eksperimentere med forskellige prompting strategier. Hvert mønster løser forskellige problemer – prøv dem for at se, hvornår hver tilgang fungerer bedst.

> **Bemærk: Streaming vs Ikke-streaming** — Hver mønsterside tilbyder to knapper: **🔴 Stream Response (Live)** og en **Ikke-streaming** mulighed. Streaming bruger Server-Sent Events (SSE) til at vise tokens i realtid, mens modellen genererer dem, så du ser fremdrift med det samme. Ikke-streaming venter på hele svaret, før det vises. For prompts, der udløser dyb ræsonnering (f.eks. High Eagerness, Self-Reflecting Code), kan ikke-streaming kald tage meget lang tid – nogle gange flere minutter – uden synlig feedback. **Brug streaming når du eksperimenterer med komplekse prompts**, så du kan se modellen arbejde og undgå indtryk af timeout.
>
> **Bemærk: Browserkrav** — Streaming-funktionen bruger Fetch Streams API (`response.body.getReader()`), som kræver en fuld browser (Chrome, Edge, Firefox, Safari). Det virker **ikke** i VS Codes indbyggede Simple Browser, da dens webview ikke understøtter ReadableStream API’en. Hvis du bruger Simple Browser, fungerer ikke-streaming knapperne stadig normalt — kun streaming knapperne påvirkes. Åbn `http://localhost:8083` i en ekstern browser for fuld oplevelse.

### Lav vs Høj entusiasme

Stil et simpelt spørgsmål som "Hvad er 15% af 200?" med Lav entusiasme. Du får et øjeblikkeligt, direkte svar. Stil nu noget komplekst som "Design en caching strategi for en højt trafikeret API" med Høj entusiasme. Klik **🔴 Stream Response (Live)** og se modellens detaljerede ræsonnering komme frem token for token. Samme model, samme spørgsmål — men prompten fortæller hvor meget tænkning, der skal til.

### Opgaveudførelse (Tool Preambles)

Workflow med flere trin profiterer af forudgående planlægning og statusfortælling. Modellen skitserer hvad den vil gøre, fortæller om hvert trin og opsummerer så resultater.

### Selvreflekterende kode

Prøv "Lav en email valideringstjeneste". I stedet for bare at generere kode og stoppe, genererer modellen, evaluerer ift. kvalitetskriterier, identificerer svagheder og forbedrer. Du ser iterationer, indtil koden opfylder produktionsstandarder.

### Struktureret analyse

Kodegennemgange kræver konsekvente evalueringsrammer. Modellen analyserer kode med faste kategorier (korrekthed, praksis, ydeevne, sikkerhed) med alvorlighedsniveauer.

### Multi-turn chat

Spørg "Hvad er Spring Boot?" og følg straks op med "Vis mig et eksempel". Modellen husker dit første spørgsmål og giver et Spring Boot eksempel specifikt til dig. Uden hukommelse ville det andet spørgsmål være for vagt.

### Trin-for-trin ræsonnering

Vælg et matematisk problem og prøv det med både Trin-for-trin ræsonnering og Lav entusiasme. Lav entusiasme giver bare svaret – hurtigt men uigennemsigtigt. Trin for trin viser hver udregning og beslutning.

### Begrænset output

Når du har brug for specifikke formater eller ordantal, sikrer dette mønster streng overholdelse. Prøv at generere et sammendrag med præcis 100 ord i punktopstilling.

## Hvad du virkelig lærer

**Ræsonneringsindsats ændrer alt**

GPT-5.2 lader dig styre den beregningsmæssige indsats via dine prompts. Lav indsats betyder hurtige svar med minimal udforskning. Høj indsats betyder, at modellen tager sig tid til at tænke grundigt. Du lærer at matche indsatsen med opgavens kompleksitet – spild ikke tid på simple spørgsmål, men skynd dig ikke også komplekse beslutninger.

**Struktur guider adfærd**

Bemærk XML-tags i promptene? De er ikke dekorative. Modeller følger strukturerede instruktioner mere pålideligt end frit formuleret tekst. Når du har brug for multi-trins processer eller kompleks logik, hjælper struktur modellen med at holde styr på, hvor den er, og hvad der kommer næste. Diagrammet nedenfor opdeler en veldesignet prompt og viser hvordan tags som `<system>`, `<instructions>`, `<context>`, `<user-input>`, og `<constraints>` organiserer dine instruktioner i klare sektioner.

<img src="../../../translated_images/da/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi af en veldesignet prompt med klare sektioner og XML-lignende organisering*

**Kvalitet gennem selvevaluering**

De selvreflekterende mønstre virker ved at gøre kvalitetskriterier eksplicitte. I stedet for at håbe på, at modellen "gør det rigtigt", fortæller du præcis hvad "rigtigt" betyder: korrekt logik, fejlhåndtering, ydeevne, sikkerhed. Modellen kan så evaluere sit eget output og forbedre det. Det gør kodegenerering til en proces i stedet for lotteri.

**Kontekst er begrænset**

Multi-turn samtaler virker ved at inkludere meddelelseshistorik ved hvert kald. Men der er en grænse – hver model har et maksimum token antal. Når samtaler bliver længere, skal du bruge strategier for at holde relevant kontekst uden at ramme loftet. Dette modul viser hvordan hukommelse fungerer; senere lærer du hvornår du skal opsummere, glemme og hente.

## Næste skridt

**Næste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Forrige: Modul 01 - Introduktion](../01-introduction/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi stræber efter nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør anses for at være den autoritative kilde. For vigtig information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for eventuelle misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->