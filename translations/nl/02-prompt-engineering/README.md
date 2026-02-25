# Module 02: Prompt Engineering met GPT-5.2

## Inhoudsopgave

- [Wat je zult leren](../../../02-prompt-engineering)
- [Vereisten](../../../02-prompt-engineering)
- [Begrijpen van Prompt Engineering](../../../02-prompt-engineering)
- [Grondbeginselen van Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rolgebaseerd Prompting](../../../02-prompt-engineering)
  - [Promptsjablonen](../../../02-prompt-engineering)
- [Geavanceerde Patronen](../../../02-prompt-engineering)
- [Bestaande Azure-bronnen gebruiken](../../../02-prompt-engineering)
- [Applicatie screenshots](../../../02-prompt-engineering)
- [Patronen verkennen](../../../02-prompt-engineering)
  - [Lage versus hoge gretigheid](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Voorafgaande Teksten)](../../../02-prompt-engineering)
  - [Zelfreflecterende code](../../../02-prompt-engineering)
  - [Gestructureerde analyse](../../../02-prompt-engineering)
  - [Multi-turn chat](../../../02-prompt-engineering)
  - [Stap-voor-stap redeneren](../../../02-prompt-engineering)
  - [Beperkte output](../../../02-prompt-engineering)
- [Wat je echt leert](../../../02-prompt-engineering)
- [Volgende stappen](../../../02-prompt-engineering)

## Wat je zult leren

<img src="../../../translated_images/nl/what-youll-learn.c68269ac048503b2.webp" alt="Wat je zult leren" width="800"/>

In de vorige module zag je hoe geheugen gespreks-AI mogelijk maakt en gebruikte je GitHub-modellen voor basisinteracties. Nu richten we ons op hoe je vragen stelt — de prompts zelf — met behulp van Azure OpenAI’s GPT-5.2. De manier waarop je je prompts structureert, beïnvloedt de kwaliteit van de antwoorden die je krijgt drastisch. We beginnen met een overzicht van de fundamentele prompting-technieken, en gaan vervolgens over naar acht geavanceerde patronen die optimaal gebruikmaken van de mogelijkheden van GPT-5.2.

We gebruiken GPT-5.2 omdat het redeneervermogen introduceert — je kunt het model vertellen hoeveel het moet nadenken voordat het antwoordt. Dit maakt de verschillende prompting-strategieën duidelijker en helpt je begrijpen wanneer je welke aanpak moet gebruiken. We profiteren ook van de minder strikte tarieflimieten van Azure voor GPT-5.2 in vergelijking met GitHub-modellen.

## Vereisten

- Module 01 voltooid (Azure OpenAI-resources gedeployed)
- `.env` bestand in de hoofdmap met Azure-gegevens (gemaakt met `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst de deployment-instructies daar.

## Begrijpen van Prompt Engineering

<img src="../../../translated_images/nl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Wat is Prompt Engineering?" width="800"/>

Prompt engineering gaat over het ontwerpen van invoertekst die consequent de resultaten oplevert die je nodig hebt. Het gaat niet alleen om vragen stellen — het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het dat moet leveren.

Denk eraan als het geven van instructies aan een collega. "Los de bug op" is vaag. "Los de null pointer exception op in UserService.java regel 45 door een null check toe te voegen" is specifiek. Taalmodellen werken op dezelfde manier — specificiteit en structuur zijn belangrijk.

<img src="../../../translated_images/nl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hoe LangChain4j Past" width="800"/>

LangChain4j biedt de infrastructuur — modelverbindingen, geheugen en berichttypen — terwijl promptpatronen gewoon zorgvuldig gestructureerde tekst zijn die je via die infrastructuur verzendt. De belangrijkste bouwstenen zijn `SystemMessage` (dat het gedrag en de rol van de AI instelt) en `UserMessage` (dat je daadwerkelijke verzoek draagt).

## Grondbeginselen van Prompt Engineering

<img src="../../../translated_images/nl/five-patterns-overview.160f35045ffd2a94.webp" alt="Overzicht van vijf prompt engineering patronen" width="800"/>

Voordat we ingaan op de geavanceerde patronen in deze module, laten we vijf fundamentele prompting-technieken herzien. Dit zijn de bouwstenen die elke prompt engineer moet kennen. Als je al hebt gewerkt met de [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), heb je deze al in actie gezien — hier is het conceptuele kader erachter.

### Zero-Shot Prompting

De eenvoudigste aanpak: geef het model een directe instructie zonder voorbeelden. Het model vertrouwt volledig op zijn training om de taak te begrijpen en uit te voeren. Dit werkt goed voor eenvoudige verzoeken waar het verwachte gedrag duidelijk is.

<img src="../../../translated_images/nl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Directe instructie zonder voorbeelden — het model leidt de taak alleen af uit de instructie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Reactie: "Positief"
```

**Wanneer te gebruiken:** Eenvoudige classificaties, directe vragen, vertalingen, of elke taak die het model zonder aanvullende begeleiding kan afhandelen.

### Few-Shot Prompting

Geef voorbeelden die het patroon laten zien dat je wilt dat het model volgt. Het model leert van jouw voorbeelden het verwachte in- en uitvoerformaat en past dit toe op nieuwe input. Dit verbetert de consistentie aanzienlijk voor taken waar het gewenste formaat of gedrag niet vanzelfsprekend is.

<img src="../../../translated_images/nl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Leren van voorbeelden — het model identificeert het patroon en past het toe op nieuwe input*

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

**Wanneer te gebruiken:** Aangepaste classificaties, consistente opmaak, domeinspecifieke taken, of wanneer zero-shot resultaten inconsistent zijn.

### Chain of Thought

Vraag het model om stap voor stap zijn redenering te tonen. In plaats van meteen met een antwoord te komen, breekt het model het probleem af en werkt elk onderdeel expliciet uit. Dit verbetert de nauwkeurigheid bij wiskunde, logica en meerstapsredeneringen.

<img src="../../../translated_images/nl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Stap-voor-stap redenering — complexe problemen opsplitsen in duidelijke logische stappen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Het model toont: 15 - 8 = 7, dan 7 + 12 = 19 appels
```

**Wanneer te gebruiken:** Wiskundeproblemen, logische puzzels, debugging, of elke taak waarbij het tonen van het redeneerproces de nauwkeurigheid en het vertrouwen verbetert.

### Rolgebaseerd Prompting

Stel een persona of rol in voor de AI voordat je je vraag stelt. Dit geeft context die de toon, diepgang en focus van het antwoord beïnvloedt. Een "software architect" geeft andere adviezen dan een "junior developer" of een "security auditor".

<img src="../../../translated_images/nl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rolgebaseerd Prompting" width="800"/>

*Context en persona instellen — dezelfde vraag krijgt een ander antwoord afhankelijk van de toegewezen rol*

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

**Wanneer te gebruiken:** Code reviews, tutoring, domeinspecifieke analyses, of wanneer je antwoorden nodig hebt die zijn toegespitst op een bepaald deskundigheidsniveau of perspectief.

### Promptsjablonen

Maak herbruikbare prompts met variabele placeholders. In plaats van elke keer een nieuwe prompt te schrijven, definieer je een sjabloon en vul je verschillende waarden in. De `PromptTemplate`-klasse van LangChain4j maakt dit makkelijk met `{{variable}}`-syntaxis.

<img src="../../../translated_images/nl/prompt-templates.14bfc37d45f1a933.webp" alt="Promptsjablonen" width="800"/>

*Herbruikbare prompts met variabele placeholders — één sjabloon, vele toepassingen*

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

**Wanneer te gebruiken:** Herhaalde queries met verschillende input, batchverwerking, bouwen van herbruikbare AI-werkstromen, of elk scenario waarbij de promptstructuur hetzelfde blijft maar de gegevens veranderen.

---

Deze vijf grondbeginselen geven je een solide toolkit voor de meeste prompting-taken. De rest van deze module bouwt hierop voort met **acht geavanceerde patronen** die gebruikmaken van GPT-5.2’s redeneervermogen, zelfevaluatie en gestructureerde outputmogelijkheden.

## Geavanceerde Patronen

Met de basisprincipes behandeld, gaan we naar de acht geavanceerde patronen die deze module uniek maken. Niet alle problemen vereisen dezelfde aanpak. Sommige vragen hebben snelle antwoorden nodig, andere diepgaande analyse. Sommige hebben zichtbare redenering nodig, andere alleen het resultaat. Elk patroon hieronder is geoptimaliseerd voor een andere situatie — en GPT-5.2’s redeneervermogen maakt de verschillen nog duidelijker.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht prompting patronen" width="800"/>

*Overzicht van de acht prompt engineering patronen en hun toepassingen*

<img src="../../../translated_images/nl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Redeneercontrole met GPT-5.2" width="800"/>

*GPT-5.2’s redeneervermogen stelt je in staat te bepalen hoeveel het model moet nadenken — van snelle directe antwoorden tot diepe verkenning*

**Lage gretigheid (Snel & Gericht)** - Voor eenvoudige vragen waarbij je snelle, directe antwoorden wilt. Het model doet minimale redenering — maximaal 2 stappen. Gebruik dit voor berekeningen, opzoeken of eenvoudige vragen.

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

> 💡 **Verken met GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) en vraag:
> - "Wat is het verschil tussen lage en hoge gretigheid prompting-patronen?"
> - "Hoe helpen XML-tags in prompts de AI-respons te structureren?"
> - "Wanneer gebruik ik zelfreflectiepatronen versus directe instructie?"

**Hoge gretigheid (Diep & Grondig)** - Voor complexe problemen waarbij je een grondige analyse wilt. Het model onderzoekt uitgebreid en toont gedetailleerde redenering. Gebruik dit voor systeemontwerp, architectuurbeslissingen of complex onderzoek.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-Stap Voortgang)** - Voor meerstaps workflows. Het model geeft van tevoren een plan, vertelt elke stap tijdens het werken, en geeft daarna een samenvatting. Gebruik dit voor migraties, implementaties of elk meerstapsproces.

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

Chain-of-Thought prompting vraagt het model expliciet om zijn redeneerproces te tonen, wat de nauwkeurigheid bij complexe taken vergroot. De stap-voor-stap opsplitsing helpt zowel mensen als AI de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over dit patroon:
> - "Hoe zou ik het taakuitvoeringspatroon aanpassen voor langlopende operaties?"
> - "Wat zijn best practices voor het structureren van tool voorafgaande teksten in productietoepassingen?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en weergeven in een UI?"

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Taakuitvoeringspatroon" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor taken met meerdere stappen*

**Zelfreflecterende code** - Voor het genereren van productieklare code. Het model genereert code volgens productienormen met juiste foutafhandeling. Gebruik dit bij het bouwen van nieuwe features of services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Zelfreflectiecyclus" width="800"/>

*Iteratieve verbeterlus - genereren, evalueren, problemen identificeren, verbeteren, herhalen*

**Gestructureerde analyse** - Voor consistente evaluatie. Het model beoordeelt code aan de hand van een vast kader (correctheid, praktijken, prestaties, beveiliging, onderhoudbaarheid). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over gestructureerde analyse:
> - "Hoe kan ik het analyseframework aanpassen voor verschillende soorten code reviews?"
> - "Wat is de beste manier om gestructureerde output programmatisch te ontsluiten en te verwerken?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review sessies?"

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Gestructureerd analysepatroon" width="800"/>

*Kader voor consistente code reviews met ernstniveaus*

**Multi-turn chat** - Voor gesprekken die context nodig hebben. Het model onthoudt eerdere berichten en bouwt daarop voort. Gebruik dit voor interactieve hulpsessies of complexe Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/nl/context-memory.dff30ad9fa78832a.webp" alt="Contextgeheugen" width="800"/>

*Hoe gesprekscontext zich opbouwt over meerdere beurten tot de tokenlimiet is bereikt*

**Stap-voor-stap redeneren** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering voor elke stap. Gebruik dit voor wiskundeproblemen, logische puzzels, of wanneer je het denkproces wilt doorgronden.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Stap-voor-stap patroon" width="800"/>

*Complexe problemen opsplitsen in expliciete logische stappen*

**Beperkte output** - Voor reacties met specifieke formaatvereisten. Het model volgt strikt format- en lengtevoorschriften. Gebruik dit voor samenvattingen of wanneer je een nauwkeurige outputstructuur nodig hebt.

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

<img src="../../../translated_images/nl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Beperkt outputpatroon" width="800"/>

*Het afdwingen van specifieke format-, lengte- en structuurregels*

## Bestaande Azure-bronnen gebruiken

**Controleer deployment:**

Zorg dat het `.env` bestand bestaat in de hoofdmap met Azure-gegevens (gemaakt tijdens Module 01):
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` uit Module 01, draait deze module al op poort 8083. Je kunt de onderstaande startcommando’s overslaan en direct naar http://localhost:8083 gaan.

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code-gebruikers)**

De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt het in de Activity Bar aan de linkerzijde van VS Code (zoek naar het Spring Boot icoon).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de workspace zien
- Applicaties met één klik starten/stoppen
- Applicatielogs realtime bekijken
- Applicatiestatus monitoren
Klik simpelweg op de afspeelknop naast "prompt-engineering" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik van shell-scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de hoofdmap
.\start-all.ps1
```

Of start alleen deze module:

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

Beide scripts laden automatisch omgevingsvariabelen vanuit het root `.env`-bestand en bouwen de JAR-bestanden indien deze nog niet bestaan.

> **Opmerking:** Als je er de voorkeur aan geeft om alle modules handmatig te bouwen voordat je start:
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

Open http://localhost:8083 in je browser.

**Om te stoppen:**

**Bash:**
```bash
./stop.sh  # Alleen deze module
# Of
cd .. && ./stop-all.sh  # Alle modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Alleen deze module
# Of
cd ..; .\stop-all.ps1  # Alle modules
```

## Applicatieschermen

<img src="../../../translated_images/nl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Het hoofdscherm met alle 8 prompt engineering-patronen met hun kenmerken en gebruiksscenario's*

## Verkennen van de Patronen

De webinterface laat je experimenteren met verschillende promptstrategieën. Elk patroon lost andere problemen op – probeer ze om te zien wanneer welke aanpak zijn meerwaarde heeft.

> **Opmerking: Streaming vs Niet-Streaming** — Elke patroonpagina biedt twee knoppen: **🔴 Stream Response (Live)** en een **Niet-streaming** optie. Streaming gebruikt Server-Sent Events (SSE) om tokens in realtime weer te geven terwijl het model ze genereert, zodat je de voortgang direct ziet. De niet-streaming optie wacht tot de volledige respons binnen is voordat deze wordt getoond. Voor prompts die diepgaande redeneervragen triggeren (bijv. High Eagerness, Zelfreflecterende Code) kan de niet-streaming oproep erg lang duren — soms minuten — zonder zichtbare feedback. **Gebruik streaming bij het experimenteren met complexe prompts** om het model in actie te zien en te voorkomen dat het lijkt alsof het verzoek is vastgelopen.
>
> **Opmerking: Browservereiste** — De streamingfunctie gebruikt de Fetch Streams API (`response.body.getReader()`) die een volledige browser vereist (Chrome, Edge, Firefox, Safari). Het werkt **niet** in VS Code’s ingebouwde Simple Browser, omdat de webview daar de ReadableStream API niet ondersteunt. Gebruik je de Simple Browser, dan werken de niet-streaming knoppen nog gewoon — alleen de streaming knoppen zijn beperkt. Open `http://localhost:8083` in een externe browser voor de volledige ervaring.

### Lage vs Hoge Eagerheid

Stel een simpele vraag als "Wat is 15% van 200?" met Lage Eagerheid. Je krijgt direct een duidelijk antwoord. Stel nu een complexe vraag zoals "Ontwerp een cachingstrategie voor een API met veel verkeer" met Hoge Eagerheid. Klik op **🔴 Stream Response (Live)** en bekijk hoe de gedetailleerde redenering van het model token-voor-token verschijnt. Zelfde model, zelfde vraagstructuur - maar de prompt geeft aan hoeveel nadenken er moet plaatsvinden.

### Taakuitvoering (Tool Preambles)

Multi-step workflows profiteren van vooraf plannen en voortgangsnarratie. Het model schetst wat het gaat doen, vertelt over elke stap en vat vervolgens de resultaten samen.

### Zelfreflecterende Code

Probeer "Maak een e-mail validatieservice". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert het tegen kwaliteitscriteria, identificeert zwakke punten en verbetert het. Je ziet het itereren totdat de code aan productienormen voldoet.

### Gestructureerde Analyse

Code reviews vragen om consistente beoordelingskaders. Het model analyseert code met vaste categorieën (correctheid, best practices, prestaties, beveiliging) met ernstniveau’s.

### Multi-Turn Chat

Vraag "Wat is Spring Boot?" en stel dan direct "Laat me een voorbeeld zien". Het model onthoudt je eerste vraag en geeft specifiek een Spring Boot voorbeeld. Zonder geheugen zou die tweede vraag te vaag zijn.

### Stap-voor-Stap Redenering

Pak een wiskundig probleem en probeer het met zowel Stap-voor-Stap Redenering als Lage Eagerheid. Lage eagerheid geeft alleen het antwoord - snel maar ondoorzichtig. Stap-voor-stap laat elke berekening en beslissing zien.

### Beperkte Output

Als je specifieke formaten of woordenaantallen nodig hebt, dwingt dit patroon strikte naleving af. Probeer een samenvatting te genereren van precies 100 woorden in opsommingstekens.

## Wat Je Echt Leert

**Redeneringsinspanning Verandert Alles**

GPT-5.2 laat je de rekenkundige inspanning via je prompts sturen. Lage inspanning betekent snelle antwoorden met minimale verkenning. Hoge inspanning betekent dat het model de tijd neemt om diep na te denken. Je leert de inspanning af te stemmen op de complexiteit van de taak – verspil geen tijd aan eenvoudige vragen, maar haast je ook niet bij complexe beslissingen.

**Structuur Stuurt Gedrag**

Zie je de XML-tags in de prompts? Die zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Voor multi-step processen of complexe logica helpt structuur het model om bij te houden waar het is en wat er volgt.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-achtige organisatie*

**Kwaliteit Door Zelfevaluatie**

De zelfreflecterende patronen werken door kwaliteitscriteria expliciet te maken. In plaats van te hopen dat het model "het goed doet", vertel je exact wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan dan zijn eigen output evalueren en verbeteren. Dit verandert codegeneratie van loterij in proces.

**Context Is Eindig**

Multi-turn gesprekken werken door berichtgeschiedenis mee te sturen met elk verzoek. Maar er is een limiet - elk model heeft een maximum aantal tokens. Naarmate gesprekken groeien, heb je strategieën nodig om relevante context te behouden zonder die limiet te overschrijden. Deze module laat je zien hoe geheugen werkt; later leer je wanneer samenvatten, vergeten en ophalen aan de orde zijn.

## Volgende Stappen

**Volgende Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel wij streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal dient als de gezaghebbende bron te worden beschouwd. Voor cruciale informatie wordt aanbevolen een professionele menselijke vertaling te laten maken. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->