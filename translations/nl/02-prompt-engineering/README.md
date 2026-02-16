# Module 02: Prompt Engineering met GPT-5.2

## Inhoudsopgave

- [Wat je zult leren](../../../02-prompt-engineering)
- [Vereisten](../../../02-prompt-engineering)
- [Begrijpen van Prompt Engineering](../../../02-prompt-engineering)
- [Fundamenten van Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rolgebaseerde Prompting](../../../02-prompt-engineering)
  - [Prompt Sjablonen](../../../02-prompt-engineering)
- [Geavanceerde Patronen](../../../02-prompt-engineering)
- [Gebruik van bestaande Azure-resources](../../../02-prompt-engineering)
- [Applicatie Screenshots](../../../02-prompt-engineering)
- [Ontdek de Patronen](../../../02-prompt-engineering)
  - [Lage vs Hoge Gevoeligheid](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Preambules)](../../../02-prompt-engineering)
  - [Zelfreflecterende Code](../../../02-prompt-engineering)
  - [Gestructureerde Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Stapsgewijze Redenering](../../../02-prompt-engineering)
  - [Beperkte Output](../../../02-prompt-engineering)
- [Wat je eigenlijk leert](../../../02-prompt-engineering)
- [Volgende Stappen](../../../02-prompt-engineering)

## Wat je zult leren

<img src="../../../translated_images/nl/what-youll-learn.c68269ac048503b2.webp" alt="Wat je zult leren" width="800"/>

In de vorige module zag je hoe geheugen conversatie-AI mogelijk maakt en gebruikte je GitHub Models voor basisinteracties. Nu richten we ons op hoe je vragen stelt — de prompts zelf — met Azure OpenAI's GPT-5.2. De manier waarop je je prompts structureert beïnvloedt dramatisch de kwaliteit van de reacties die je krijgt. We beginnen met een overzicht van de fundamentele prompting-technieken, en gaan dan over naar acht geavanceerde patronen die de volledige mogelijkheden van GPT-5.2 benutten.

We gebruiken GPT-5.2 omdat het redeneersturing introduceert — je kunt het model vertellen hoeveel het moet nadenken voordat het antwoordt. Dit maakt verschillende prompting-strategieën duidelijker en helpt je te begrijpen wanneer je elke aanpak moet gebruiken. We profiteren ook van de lagere limieten van Azure voor GPT-5.2 vergeleken met GitHub Models.

## Vereisten

- Voltooide Module 01 (Azure OpenAI-resources gedeployed)
- `.env` bestand in de hoofdmap met Azure-gegevens (aangemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 niet hebt voltooid, volg dan eerst de deployment-instructies daar.

## Begrijpen van Prompt Engineering

<img src="../../../translated_images/nl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Wat is Prompt Engineering?" width="800"/>

Prompt engineering draait om het ontwerpen van inputtekst die je consequent de gewenste resultaten oplevert. Het gaat niet alleen om vragen stellen — het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het dat moet leveren.

Zie het als het geven van instructies aan een collega. "Repareer de bug" is vaag. "Repareer de null pointer exception in UserService.java regel 45 door een null check toe te voegen" is specifieker. Taalmodellen werken op dezelfde manier — specificiteit en structuur zijn belangrijk.

<img src="../../../translated_images/nl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hoe LangChain4j past" width="800"/>

LangChain4j biedt de infrastructuur — modelverbindingen, geheugen en berichttypes — terwijl promptpatronen gewoon zorgvuldig gestructureerde tekst zijn die je door die infrastructuur stuurt. De belangrijkste bouwstenen zijn `SystemMessage` (die het gedrag en de rol van de AI instelt) en `UserMessage` (die je daadwerkelijke verzoek bevat).

## Fundamenten van Prompt Engineering

<img src="../../../translated_images/nl/five-patterns-overview.160f35045ffd2a94.webp" alt="Overzicht van vijf prompt engineering patronen" width="800"/>

Voordat we de geavanceerde patronen in deze module induiken, laten we vijf fundamentele prompting-technieken herhalen. Dit zijn de bouwstenen die elke prompt engineer moet kennen. Als je al hebt gewerkt met de [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), heb je deze in actie gezien — hier is het conceptuele kader erachter.

### Zero-Shot Prompting

De eenvoudigste aanpak: geef het model een directe instructie zonder voorbeelden. Het model vertrouwt volledig op zijn training om de taak te begrijpen en uit te voeren. Dit werkt goed voor simpele verzoeken waarbij het verwachte gedrag duidelijk is.

<img src="../../../translated_images/nl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Directe instructie zonder voorbeelden — het model leidt de taak af uit alleen de instructie*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Reactie: "Positief"
```

**Wanneer te gebruiken:** Eenvoudige classificaties, directe vragen, vertalingen, of elke taak die het model zonder extra aanwijzingen aankan.

### Few-Shot Prompting

Geef voorbeelden die het patroon tonen dat je wilt dat het model volgt. Het model leert het verwachte input-outputformaat van jouw voorbeelden en past dit toe op nieuwe input. Dit verbetert de consistentie sterk bij taken waar het gewenste formaat of gedrag niet vanzelfsprekend is.

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

**Wanneer te gebruiken:** Aangepaste classificaties, consistente formattering, domeinspecifieke taken, of wanneer zero-shot resultaten inconsistent zijn.

### Chain of Thought

Vraag het model om zijn redenering stap voor stap te laten zien. In plaats van direct een antwoord te geven, breekt het model het probleem op en werkt het elk deel expliciet uit. Dit verbetert de nauwkeurigheid bij wiskunde, logica en complexe redeneertaakjes.

<img src="../../../translated_images/nl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Stapsgewijze redenering — complexe problemen opsplitsen in expliciete logische stappen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Het model toont: 15 - 8 = 7, dan 7 + 12 = 19 appels
```

**Wanneer te gebruiken:** Wiskundeproblemen, logische puzzels, debugging, of elke taak waar het tonen van het redeneerproces nauwkeurigheid en vertrouwen verhoogt.

### Rolgebaseerde Prompting

Stel een persona of rol in voor de AI voordat je je vraag stelt. Dit biedt context die de toon, diepgang en focus van het antwoord bepaalt. Een "software architect" geeft ander advies dan een "junior ontwikkelaar" of een "security auditor".

<img src="../../../translated_images/nl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rolgebaseerde Prompting" width="800"/>

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

**Wanneer te gebruiken:** Code reviews, tutoring, domeinspecifieke analyses, of wanneer je antwoorden wilt afgestemd op een bepaald deskundigheidsniveau of perspectief.

### Prompt Sjablonen

Maak herbruikbare prompts met variabele plaatsaanduiders. In plaats van elke keer een nieuwe prompt te schrijven, definieer je een sjabloon en vul je verschillende waarden in. De `PromptTemplate`-klasse van LangChain4j maakt dit gemakkelijk met de `{{variable}}` syntax.

<img src="../../../translated_images/nl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Sjablonen" width="800"/>

*Herbruikbare prompts met variabele plaatsaanduiders — één sjabloon, veel toepassingen*

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

**Wanneer te gebruiken:** Herhaalde queries met verschillende input, batchverwerking, het bouwen van herbruikbare AI-workflows, of elke situatie waarin de promptstructuur hetzelfde blijft maar de data verandert.

---

Deze vijf fundamenten geven je een solide toolkit voor de meeste prompting-taken. De rest van deze module bouwt hierop voort met **acht geavanceerde patronen** die gebruikmaken van GPT-5.2's redeneersturing, zelfevaluatie en gestructureerde outputmogelijkheden.

## Geavanceerde Patronen

Met de fundamenten afgehandeld, gaan we naar de acht geavanceerde patronen die deze module uniek maken. Niet alle problemen vragen dezelfde aanpak. Sommige vragen hebben snelle antwoorden nodig, andere diep nadenken. Sommige vereisen zichtbare redenering, andere alleen het resultaat. Elk patroon hieronder is geoptimaliseerd voor een verschillende situatie — en GPT-5.2's redeneersturing maakt de verschillen nog duidelijker.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht prompting patronen" width="800"/>

*Overzicht van de acht prompt engineering-patronen en hun gebruiksscenario’s*

<img src="../../../translated_images/nl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Redeneersturing met GPT-5.2" width="800"/>

*GPT-5.2's redeneersturing laat je opgeven hoeveel het model moet nadenken — van snelle directe antwoorden tot diepgaande verkenning*

<img src="../../../translated_images/nl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Vergelijking van redeneringsinspanning" width="800"/>

*Lage gevoeligheid (snel, direct) versus hoge gevoeligheid (grondig, verkennend) redeneeraanpakken*

**Lage Gevoeligheid (Snel & Gefocust)** - Voor simpele vragen waarbij je snel en direct antwoord wilt. Het model doet minimale redenering - maximaal 2 stappen. Gebruik dit voor berekeningen, opzoekingen of eenvoudige vragen.

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
> - "Wat is het verschil tussen lage en hoge gevoeligheid prompting patronen?"
> - "Hoe helpen de XML tags in prompts de AI-antwoordstructuur?"
> - "Wanneer gebruik ik zelfreflectiepatronen versus directe instructie?"

**Hoge Gevoeligheid (Diep & Grondig)** - Voor complexe problemen waarbij je een uitgebreide analyse wilt. Het model onderzoekt grondig en toont gedetailleerde redenering. Gebruik dit bij systeemontwerp, architectuurkeuzes of complex onderzoek.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-stap voortgang)** - Voor workflows met meerdere stappen. Het model geeft eerst een plan, beschrijft elke stap tijdens het werk, en geeft dan een samenvatting. Gebruik dit bij migraties, implementaties of elk proces met meerdere stappen.

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

Chain-of-Thought prompting vraagt het model expliciet om zijn redeneerproces te tonen, wat de nauwkeurigheid bij complexe taken verbetert. De stapsgewijze uitsplitsing helpt zowel mensen als AI de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag naar dit patroon:
> - "Hoe zou ik het taakuitvoeringspatroon aanpassen voor langlopende operaties?"
> - "Wat zijn best practices voor het structureren van tool preambles in productieapplicaties?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en weergeven in een UI?"

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Taakuitvoeringspatroon" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor taken met meerdere stappen*

**Zelfreflecterende Code** - Voor het genereren van productieklare code. Het model creëert code volgens productiestandaarden met correcte foutafhandeling. Gebruik dit bij het bouwen van nieuwe functionaliteiten of services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Zelfreflectiecyclus" width="800"/>

*Iteratieve verbetercyclus - genereren, evalueren, problemen identificeren, verbeteren, herhalen*

**Gestructureerde Analyse** - Voor consistente evaluatie. Het model beoordeelt code met een vaste raamwerk (correctheid, praktijken, prestaties, beveiliging, onderhoudbaarheid). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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
> - "Hoe kan ik het analyse-raamwerk aanpassen voor verschillende soorten code reviews?"
> - "Wat is de beste manier om gestructureerde output programmatisch te parsen en te gebruiken?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review sessies?"

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Gestructureerde analysepatroon" width="800"/>

*Raamwerk voor consistente code reviews met ernstniveaus*

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

*Hoe gesprekcontext zich opbouwt over meerdere beurten tot het tokenlimiet bereikt wordt*

**Stapsgewijze Redenering** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering voor elke stap. Gebruik dit bij wiskundeproblemen, logische puzzels, of als je het denkproces wilt begrijpen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Stapsgewijs patroon" width="800"/>

*Problematische problemen opsplitsen in expliciete logische stappen*

**Beperkte Output** - Voor antwoorden met specifieke formaatvereisten. Het model volgt strikt formaat- en lengteregels. Gebruik dit voor samenvattingen of wanneer je precieze outputstructuur nodig hebt.

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

*Handhaving van specifieke formaat-, lengte- en structuurvereisten*

## Gebruik van bestaande Azure-resources

**Verifieer deployment:**

Zorg dat het `.env` bestand in de hoofdmap bestaat met Azure-gegevens (aangemaakt tijdens Module 01):
```bash
cat ../.env  # Zou AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT moeten tonen
```

**Start de applicatie:**

> **Opmerking:** Als je al alle applicaties hebt gestart met `./start-all.sh` uit Module 01, draait deze module al op poort 8083. Je kunt de startcommando’s hieronder overslaan en direct naar http://localhost:8083 gaan.

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code-gebruikers)**

De dev container bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt deze in de Activity Bar aan de linkerkant van VS Code (zoek het Spring Boot-icoon).
Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de werkruimte zien
- Applicaties met één klik starten/stoppen
- Applicatielogs in realtime bekijken
- Applicatiestatus monitoren

Klik eenvoudig op de afspeelknop naast "prompt-engineering" om deze module te starten, of start direct alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Shell-scripts gebruiken**

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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env`-bestand en bouwen de JAR's als deze nog niet bestaan.

> **Notitie:** Als je liever alle modules handmatig bouwt voordat je begint:
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

*Het hoofd dashboard toont alle 8 prompt engineering patronen met hun kenmerken en toepassingsgevallen*

## De patronen verkennen

De webinterface laat je experimenteren met verschillende prompting strategieën. Elk patroon lost andere problemen op - probeer ze om te zien wanneer elk aanpak het beste werkt.

### Lage vs Hoge Bereidheid

Stel een eenvoudige vraag zoals "Wat is 15% van 200?" met Lage Bereidheid. Je krijgt een direct, onmiddellijk antwoord. Stel nu iets complexers zoals "Ontwerp een cachingstrategie voor een hoogbelast API" met Hoge Bereidheid. Zie hoe het model vertraagt en gedetailleerde redeneringen geeft. Zelfde model, zelfde vraagstructuur - maar de prompt geeft aan hoeveel denkwerk het moet doen.

<img src="../../../translated_images/nl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snel rekenen met minimale redenering*

<img src="../../../translated_images/nl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Uitgebreide cachingstrategie (2.8MB)*

### Taakuitvoering (Tool Preambles)

Workflows met meerdere stappen profiteren van planning vooraf en voortgangsvertelling. Het model schetst wat het gaat doen, vertelt elke stap, en vat dan de resultaten samen.

<img src="../../../translated_images/nl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Een REST endpoint creëren met stapsgewijze vertelling (3.9MB)*

### Zelfreflecterende Code

Probeer "Maak een e-mail validatie service". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert aan kwaliteitscriteria, identificeert zwaktes en verbetert. Je ziet het itereren tot de code voldoet aan productienormen.

<img src="../../../translated_images/nl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Volledige e-mail validatie service (5.2MB)*

### Gestructureerde Analyse

Code reviews hebben consistente evaluatiekaders nodig. Het model analyseert code met vaste categorieën (correctheid, praktijken, prestaties, beveiliging) met ernstniveaus.

<img src="../../../translated_images/nl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Code review op basis van een framework*

### Meerdere Ronden Chat

Vraag: "Wat is Spring Boot?" en volg meteen op met "Laat me een voorbeeld zien". Het model herinnert zich je eerste vraag en geeft een specifiek Spring Boot voorbeeld. Zonder geheugen zou die tweede vraag te vaag zijn.

<img src="../../../translated_images/nl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Context behoud tussen vragen door*

### Stap-voor-Stap Redeneren

Kies een wiskundig probleem en probeer het met zowel Stap-voor-Stap Redeneren als Lage Bereidheid. Lage bereidheid geeft alleen het antwoord - snel maar ondoorzichtig. Stap-voor-stap toont elke berekening en beslissing.

<img src="../../../translated_images/nl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Wiskundig probleem met expliciete stappen*

### Beperkte Output

Wanneer je specifieke formaten of woordenaantallen nodig hebt, zorgt dit patroon voor strikte naleving. Probeer een samenvatting te genereren met precies 100 woorden in opsomming.

<img src="../../../translated_images/nl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Samenvatting machine learning met formatcontrole*

## Wat je echt leert

**Redeneerinspanning verandert alles**

GPT-5.2 laat je de rekencapaciteit via je prompts regelen. Lage inspanning betekent snelle antwoorden met minimale verkenning. Hoge inspanning betekent dat het model tijd neemt om diep na te denken. Je leert inspanning af te stemmen op taakcomplexiteit - verspil geen tijd aan simpele vragen, maar haasten ook niet bij complexe beslissingen.

**Structuur stuurt gedrag**

Zie je de XML-tags in de prompts? Ze zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Wanneer je meerstaps processen of complexe logica nodig hebt, helpt structuur het model te volgen waar het is en wat er komt.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-achtige organisatie*

**Kwaliteit door zelfevaluatie**

De zelfreflecterende patronen maken kwaliteitscriteria expliciet. In plaats van te hopen dat het model "het goed doet", vertel je precies wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan dan zijn eigen output evalueren en verbeteren. Dit verandert codegeneratie van een loterij in een proces.

**Context is beperkt**

Meerdere ronden gesprekken werken doordat ze berichtenhistorie bij elke aanvraag meenemen. Maar er is een limiet - elk model heeft een maximum aantal tokens. Als gesprekken groeien, heb je strategieën nodig om relevante context te behouden zonder dat plafond te bereiken. Deze module toont hoe geheugen werkt; later leer je wanneer samenvatten, vergeten en ophalen verstandig is.

## Volgende stappen

**Volgende module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar hoofdpagina](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel wij streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onjuistheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal moet als de gezaghebbende bron worden beschouwd. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->