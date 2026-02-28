# Module 02: Prompt Engineering met GPT-5.2

## Inhoudsopgave

- [Video Walkthrough](../../../02-prompt-engineering)
- [Wat Je Zal Leren](../../../02-prompt-engineering)
- [Vereisten](../../../02-prompt-engineering)
- [Begrijpen van Prompt Engineering](../../../02-prompt-engineering)
- [Fundamenten van Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Geavanceerde Patronen](../../../02-prompt-engineering)
- [Gebruik van Bestaande Azure Resources](../../../02-prompt-engineering)
- [Applicatie Screenshots](../../../02-prompt-engineering)
- [Verkennen van de Patronen](../../../02-prompt-engineering)
  - [Lage vs Hoge Enthousiasme](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Preambles)](../../../02-prompt-engineering)
  - [Zelfreflecterende Code](../../../02-prompt-engineering)
  - [Gestructureerde Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Stap-voor-Stap Redeneren](../../../02-prompt-engineering)
  - [Beperkte Output](../../../02-prompt-engineering)
- [Wat Je Echt Leert](../../../02-prompt-engineering)
- [Volgende Stappen](../../../02-prompt-engineering)

## Video Walkthrough

Bekijk deze live sessie waarin wordt uitgelegd hoe je met deze module aan de slag gaat: [Prompt Engineering met LangChain4j - Live Sessie](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Wat Je Zal Leren

<img src="../../../translated_images/nl/what-youll-learn.c68269ac048503b2.webp" alt="Wat Je Zal Leren" width="800"/>

In de vorige module zag je hoe geheugen conversational AI mogelijk maakt en gebruikte je GitHub Models voor basale interacties. Nu richten we ons op hoe je vragen stelt — de prompts zelf — met Azure OpenAI's GPT-5.2. De manier waarop je je prompts structureert heeft grote invloed op de kwaliteit van de antwoorden die je krijgt. We beginnen met een overzicht van de fundamentele prompttechnieken en gaan dan over naar acht geavanceerde patronen die optimaal gebruikmaken van GPT-5.2's mogelijkheden.

We gebruiken GPT-5.2 omdat dit model redeneervermogen introduceert — je kunt het model aangeven hoeveel het moet nadenken voordat het antwoordt. Dit maakt verschillende prompting strategieën duidelijker en helpt je begrijpen wanneer je welke aanpak moet gebruiken. We profiteren ook van de minder strenge rate limits voor GPT-5.2 binnen Azure vergeleken met GitHub Models.

## Vereisten

- Module 01 voltooid (Azure OpenAI resources gedeployed)
- `.env` bestand in de hoofdmap met Azure credentials (aangemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt afgerond, volg dan eerst de deployment instructies daar.

## Begrijpen van Prompt Engineering

<img src="../../../translated_images/nl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Wat is Prompt Engineering?" width="800"/>

Prompt engineering gaat over het ontwerpen van invoertekst die consistent de resultaten oplevert die je nodig hebt. Het gaat niet alleen om het stellen van vragen — het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het dat moet leveren.

Zie het als het geven van instructies aan een collega. "Fix de bug" is vaag. "Los de null pointer exception op in UserService.java regel 45 door een null check toe te voegen" is specifiek. Taalmodellen werken op dezelfde manier — specificiteit en structuur zijn belangrijk.

<img src="../../../translated_images/nl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hoe LangChain4j Past" width="800"/>

LangChain4j levert de infrastructuur — modelverbindingen, geheugen en berichttypes — terwijl prompt patronen zorgvuldig gestructureerde tekst zijn die je via die infrastructuur verstuurt. De belangrijkste bouwstenen zijn `SystemMessage` (die het gedrag en de rol van de AI bepaalt) en `UserMessage` (die je daadwerkelijke verzoek bevat).

## Fundamenten van Prompt Engineering

<img src="../../../translated_images/nl/five-patterns-overview.160f35045ffd2a94.webp" alt="Overzicht van Vijf Prompt Engineering Patronen" width="800"/>

Voordat we induiken in de geavanceerde patronen van deze module, laten we vijf fundamentele prompting technieken herzien. Dit zijn de bouwstenen die elke prompt engineer zou moeten kennen. Als je al met de [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) hebt gewerkt, heb je deze al in de praktijk gezien — hier is het conceptuele raamwerk erachter.

### Zero-Shot Prompting

De eenvoudigste aanpak: geef het model een directe instructie zonder voorbeelden. Het model vertrouwt volledig op zijn training om de taak te begrijpen en uit te voeren. Dit werkt goed voor eenvoudige verzoeken waarbij het verwachte gedrag duidelijk is.

<img src="../../../translated_images/nl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Directe instructie zonder voorbeelden — het model leidt de taak af uit de instructie zelf*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwoord: "Positief"
```

**Wanneer gebruiken:** Eenvoudige classificaties, directe vragen, vertalingen of elke taak die het model zonder extra aanwijzingen aankan.

### Few-Shot Prompting

Geef voorbeelden die het patroon aantonen dat je wilt dat het model volgt. Het model leert het verwachte input-output formaat van je voorbeelden en past dit toe op nieuwe invoer. Dit verbetert de consistentie aanzienlijk voor taken waarbij het gewenste formaat of gedrag niet meteen duidelijk is.

<img src="../../../translated_images/nl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Leren van voorbeelden — het model identificeert het patroon en past het toe op nieuwe inputs*

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

**Wanneer gebruiken:** Aangepaste classificaties, consistente formatteervereisten, domeinspecifieke taken, of wanneer zero-shot resultaten inconsistent zijn.

### Chain of Thought

Vraag het model om zijn redenering stap voor stap te tonen. In plaats van meteen met een antwoord te komen, verdeelt het model het probleem en werkt elke stap expliciet uit. Dit verbetert de nauwkeurigheid bij wiskunde, logica en taken die meerdere stappen vereisen.

<img src="../../../translated_images/nl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Stap-voor-stap redenering — complexe problemen opdelen in expliciete logische stappen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Het model toont: 15 - 8 = 7, dan 7 + 12 = 19 appels
```

**Wanneer gebruiken:** Wiskundige problemen, logische puzzels, debuggen of elke taak waarbij het tonen van het redeneerproces de nauwkeurigheid en het vertrouwen verhoogt.

### Role-Based Prompting

Stel een persona of rol in voor de AI voordat je je vraag stelt. Dit geeft context die de toon, diepgang en focus van het antwoord bepaalt. Een "software architect" geeft ander advies dan een "junior developer" of een "security auditor".

<img src="../../../translated_images/nl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

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

**Wanneer gebruiken:** Code reviews, tutoring, domeinspecifieke analyse of wanneer je antwoorden wilt die afgestemd zijn op een bepaald expertiseniveau of perspectief.

### Prompt Templates

Maak herbruikbare prompts met variabele placeholders. In plaats van elke keer een nieuwe prompt te schrijven, definieer je een template en vul je die met verschillende waarden. LangChain4j's `PromptTemplate` klasse maakt dit makkelijk met `{{variabele}}` syntax.

<img src="../../../translated_images/nl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Herbruikbare prompts met variabele placeholders — één template, vele toepassingen*

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

**Wanneer gebruiken:** Herhaalde queries met verschillende invoer, batchverwerking, het bouwen van herbruikbare AI-workflows, of scenarios waarin de prompt structuur hetzelfde blijft maar de data verandert.

---

Deze vijf fundamenten geven je een solide toolkit voor de meeste prompting taken. De rest van deze module bouwt hierop voort met **acht geavanceerde patronen** die gebruikmaken van GPT-5.2's redeneervermogen, zelfevaluatie en gestructureerde output mogelijkheden.

## Geavanceerde Patronen

Met de fundamenten behandeld, gaan we naar de acht geavanceerde patronen die deze module uniek maken. Niet elk probleem vraagt om dezelfde aanpak. Sommige vragen hebben snelle antwoorden nodig, andere diep nadenken. Sommige vragen hebben zichtbare redenering nodig, andere alleen de resultaten. Elk patroon hieronder is geoptimaliseerd voor een verschillend scenario — en GPT-5.2's redeneervermogen maakt deze verschillen nog duidelijker.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompting Patronen" width="800"/>

*Overzicht van de acht prompt engineering patronen en hun gebruikssituaties*

<img src="../../../translated_images/nl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Redeneervermogen Controle met GPT-5.2" width="800"/>

*GPT-5.2's redeneervermogen controle laat je specificeren hoeveel nadenken het model moet doen — van snel directe antwoorden tot diepgaande verkenning*

**Laag Enthousiasme (Snel & Gefocust)** - Voor simpele vragen wanneer je snelle, directe antwoorden wil. Het model redeneert minimaal - maximaal 2 stappen. Gebruik dit voor berekeningen, opzoeken of eenvoudige vragen.

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

> 💡 **Verken met GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) en stel vragen zoals:
> - "Wat is het verschil tussen lage en hoge enthousiasme prompting patronen?"
> - "Hoe helpen de XML-tags in prompts om het AI antwoord te structureren?"
> - "Wanneer moet ik zelfreflectie patronen gebruiken versus directe instructies?"

**Hoog Enthousiasme (Diep & Grondig)** - Voor complexe problemen waarbij je een grondige analyse wil. Het model onderzoekt uitvoerig en toont gedetailleerde redenering. Gebruik dit voor systeemontwerp, architectuurbesluiten of complex onderzoek.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-Stap Voortgang)** - Voor workflows met meerdere stappen. Het model levert een plan vooraf, vertelt elke stap terwijl het werkt, en geeft daarna een samenvatting. Gebruik dit voor migraties, implementaties of elke multi-step procedure.

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

Chain-of-Thought prompting vraagt expliciet aan het model om zijn redeneerproces te tonen, wat de nauwkeurigheid bij complexe taken verbetert. De stap-voor-stap uitsplitsing helpt zowel mensen als AI om de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over dit patroon:
> - "Hoe zou ik het taakuitvoeringspatroon aanpassen voor langlopende operaties?"
> - "Wat zijn de best practices voor het structureren van tool preambles in productieapplicaties?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en weergeven in een UI?"

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Taakuitvoeringspatroon" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor multi-step taken*

**Zelfreflecterende Code** - Voor het genereren van productieklare code. Het model genereert code die voldoet aan productiestandaarden met correcte foutafhandeling. Gebruik dit bij het bouwen van nieuwe features of services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Zelfreflectie Cyclus" width="800"/>

*Iteratieve verbeterlus - genereer, evalueer, identificeer problemen, verbeter, herhaal*

**Gestructureerde Analyse** - Voor consistente evaluatie. Het model beoordeelt code met een vast framework (correctheid, praktijken, prestatie, beveiliging, onderhoudbaarheid). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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
> - "Wat is de beste manier om gestructureerde output programmatisch te parseren en verwerken?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review sessies heen?"

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Gestructureerde Analyse Patroon" width="800"/>

*Framework voor consistente code reviews met ernstniveaus*

**Multi-Turn Chat** - Voor gesprekken die context nodig hebben. Het model onthoudt eerdere berichten en bouwt daarop voort. Gebruik dit voor interactieve hulpsessies of complexe Q&A.

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

*Hoe context uit gesprekken zich ophoopt over meerdere beurten tot de tokenlimiet is bereikt*

**Stap-voor-Stap Redeneren** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering per stap. Gebruik dit voor wiskundeproblemen, logische puzzels of om het denkproces te begrijpen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Stap-voor-Stap Patroon" width="800"/>

*Problemen opdelen in expliciete logische stappen*

**Beperkte Output** - Voor antwoorden met specifieke formatvereisten. Het model volgt strikt de regels voor formaat en lengte. Gebruik dit voor samenvattingen of wanneer je precieze outputstructuur nodig hebt.

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

<img src="../../../translated_images/nl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Beperkte Output Patroon" width="800"/>

*Handhaven van specifieke formaat-, lengte- en structuurvereisten*

## Gebruik van Bestaande Azure Resources

**Verifieer de deployment:**

Zorg ervoor dat het `.env` bestand in de hoofdmap bestaat met Azure credentials (aangemaakt tijdens Module 01):
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` uit Module 01, draait deze module al op poort 8083. Je kunt onderstaande startcommando's overslaan en direct naar http://localhost:8083 gaan.

**Optie 1: Gebruik van Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**
De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-toepassingen te beheren. Je vindt deze in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot-pictogram).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de workspace zien
- Applicaties met één klik starten/stoppen
- Applicatielogs realtime bekijken
- Applicatiestatus monitoren

Klik gewoon op de afspeelknop naast "prompt-engineering" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik van shell-scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanaf de hoofdmap
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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env`-bestand en bouwen de JAR’s als deze nog niet bestaan.

> **Opmerking:** Als je er de voorkeur aan geeft om alle modules handmatig te bouwen voordat je ze start:
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

## Applicatie Screenshots

<img src="../../../translated_images/nl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Het hoofd dashboard dat alle 8 prompt-engineering patronen toont met hun kenmerken en gebruikssituaties*

## Patronen Verkennen

De webinterface laat je experimenteren met verschillende promptingstrategieën. Elk patroon lost andere problemen op - probeer ze om te zien wanneer welk benadering het beste werkt.

> **Opmerking: Streaming vs Niet-Streaming** — Elke patroonpagina biedt twee knoppen: **🔴 Stream Response (Live)** en een **Niet-streaming** optie. Streaming gebruikt Server-Sent Events (SSE) om tokens in realtime te tonen zodra het model ze genereert, zodat je meteen voortgang ziet. De niet-streaming optie wacht totdat de hele respons binnen is voordat deze wordt weergegeven. Voor prompts die diepgaande redenering oproepen (bijv. High Eagerness, Self-Reflecting Code) kan de niet-streamende oproep erg lang duren — soms minuten — zonder zichtbare feedback. **Gebruik streaming wanneer je experimenteert met complexe prompts** zodat je kunt zien hoe het model werkt en niet de indruk krijgt dat de aanvraag is vastgelopen.
>
> **Opmerking: Browservereiste** — De streamingfunctie gebruikt de Fetch Streams API (`response.body.getReader()`) die een volledige browser (Chrome, Edge, Firefox, Safari) vereist. Het werkt **niet** in VS Code’s ingebouwde Simple Browser, omdat de webview de ReadableStream API niet ondersteunt. Als je de Simple Browser gebruikt, werken de niet-streaming knoppen nog steeds normaal — alleen de streaming knoppen zijn beperkt. Open `http://localhost:8083` in een externe browser voor de volledige ervaring.

### Lage vs Hoge Eagerheid

Stel een simpele vraag zoals "Wat is 15% van 200?" met Lage Eagerheid. Je krijgt direct, snel antwoord. Stel nu iets complexers zoals "Ontwerp een cachingstrategie voor een API met veel verkeer" met Hoge Eagerheid. Klik op **🔴 Stream Response (Live)** en bekijk het gedetailleerde redeneringsproces van het model token voor token. Zelfde model, zelfde vraagstructuur - maar de prompt vertelt hoeveel nadenken er moet gebeuren.

### Taakuitvoering (Tool Preambles)

Multi-step workflows profiteren van vooraf plannen en het vertellen van voortgang. Het model geeft aan wat het gaat doen, licht elke stap toe en vat daarna de resultaten samen.

### Zelf-reflecterende Code

Probeer "Maak een e-mailvalidatiedienst". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert op basis van kwaliteitscriteria, identificeert zwaktes en verbetert. Je ziet het iteratief aanpassen totdat de code voldoet aan productievereisten.

### Gestructureerde Analyse

Code reviews hebben consistente beoordelingskaders nodig. Het model analyseert code met vaste categorieën (correctheid, werkwijzen, prestaties, beveiliging) met ernstniveaus.

### Multi-Turn Chat

Vraag "Wat is Spring Boot?" en volg direct op met "Laat me een voorbeeld zien". Het model onthoudt je eerste vraag en geeft je specifiek een Spring Boot-voorbeeld. Zonder geheugen zou die tweede vraag te vaag zijn.

### Stap-voor-stap Redenering

Kies een wiskundig probleem en probeer het met zowel Stap-voor-stap Redenering als Lage Eagerheid. Lage eagerheid geeft je alleen het antwoord - snel maar ondoorzichtig. Stap-voor-stap toont elke berekening en beslissing.

### Beperkte Output

Als je specifieke formaten of woordenaantallen nodig hebt, zorgt dit patroon voor strikte naleving. Probeer een samenvatting te genereren met precies 100 woorden in bullet point-formaat.

## Wat Je Echt Leert

**Redeneringsinspanning Verandert Alles**

GPT-5.2 laat je de rekenkundige inspanning via je prompts controleren. Lage inspanning betekent snelle antwoorden met minimale verkenning. Hoge inspanning betekent dat het model de tijd neemt om diep na te denken. Je leert de inspanning afstemmen op de taakcomplexiteit - verspil geen tijd aan simpele vragen, maar haast je ook niet bij complexe beslissingen.

**Structuur Stuurt Gedrag**

Merk je de XML-tags in de prompts? Die zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Wanneer je multi-step processen of complexe logica nodig hebt, helpt structuur het model te volgen waar het is en wat er volgt.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structuur" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-achtige organisatie*

**Kwaliteit Door Zelfevaluatie**

De zelfreflecterende patronen werken door kwaliteitscriteria expliciet te maken. In plaats van te hopen dat het model het "goed doet", vertel je precies wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan dan zijn eigen output evalueren en verbeteren. Dit verandert codegeneratie van een loterij in een proces.

**Context Is Beperkt**

Multi-turn gesprekken werken doordat berichtgeschiedenis meegegeven wordt bij elke aanvraag. Maar er is een limiet - elk model heeft een maximum token aantal. Naarmate gesprekken groter worden, heb je strategieën nodig om relevante context te behouden zonder die limiet te bereiken. Deze module laat zien hoe geheugen werkt; later leer je wanneer je moet samenvatten, vergeten en ophalen.

## Volgende Stappen

**Volgende Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet als de gezaghebbende bron worden beschouwd. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->