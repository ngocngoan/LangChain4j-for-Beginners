# Module 02: Prompt Engineering met GPT-5.2

## Inhoudsopgave

- [Video Walkthrough](../../../02-prompt-engineering)
- [Wat Je Zal Leren](../../../02-prompt-engineering)
- [Vereisten](../../../02-prompt-engineering)
- [Begrip van Prompt Engineering](../../../02-prompt-engineering)
- [Basisprincipes van Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Geavanceerde Patronen](../../../02-prompt-engineering)
- [Gebruik van Bestaande Azure Resources](../../../02-prompt-engineering)
- [Applicatiescreenshots](../../../02-prompt-engineering)
- [Verkenning van de Patronen](../../../02-prompt-engineering)
  - [Lage vs Hoge Bereidheid](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Preambles)](../../../02-prompt-engineering)
  - [Zelfreflecterende Code](../../../02-prompt-engineering)
  - [Gestructureerde Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Stap-voor-Stap Redenering](../../../02-prompt-engineering)
  - [Beperkte Output](../../../02-prompt-engineering)
- [Wat Je Echt Leert](../../../02-prompt-engineering)
- [Volgende Stappen](../../../02-prompt-engineering)

## Video Walkthrough

Bekijk deze live sessie waarin wordt uitgelegd hoe je aan de slag kunt met deze module:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Wat Je Zal Leren

<img src="../../../translated_images/nl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

In de vorige module zag je hoe geheugen gesprek-AI mogelijk maakt en gebruikte je GitHub Models voor basisinteracties. Nu richten we ons op hoe je vragen stelt — de prompts zelf — met Azure OpenAI's GPT-5.2. De manier waarop je je prompts structureert, beïnvloedt drastisch de kwaliteit van de reacties die je krijgt. We beginnen met een overzicht van de fundamentele promptingtechnieken, daarna behandelen we acht geavanceerde patronen die volledig gebruikmaken van GPT-5.2's mogelijkheden.

We gebruiken GPT-5.2 omdat het redeneercontrole introduceert - je kunt het model vertellen hoeveel denken het moet doen vóór het antwoordt. Dit maakt verschillende promptingstrategieën duidelijker en helpt je te begrijpen wanneer je welke aanpak moet gebruiken. We profiteren ook van Azure's minder strikte limieten voor GPT-5.2 vergeleken met GitHub Models.

## Vereisten

- Module 01 voltooid (Azure OpenAI resources gedeployed)
- `.env` bestand in de hoofdmap met Azure-gegevens (gemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst de deployment-instructies daar.

## Begrip van Prompt Engineering

<img src="../../../translated_images/nl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering gaat over het ontwerpen van invoertekst die je consequent de resultaten geeft die je nodig hebt. Het gaat niet alleen om het stellen van vragen - het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het geleverd moet worden.

Denk er aan als instructies geven aan een collega. "Los de fout op" is vaag. "Los de null pointer exception op in UserService.java regel 45 door een null-check toe te voegen" is specifiek. Taalmodellen werken hetzelfde - specificiteit en structuur zijn belangrijk.

<img src="../../../translated_images/nl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j levert de infrastructuur — modelverbindingen, geheugen en berichttypes — terwijl promptpatronen gewoon zorgvuldig gestructureerde tekst zijn die je via die infrastructuur verstuurt. De sleutelbouwstenen zijn `SystemMessage` (dat het gedrag en de rol van de AI instelt) en `UserMessage` (dat je daadwerkelijke verzoek draagt).

## Basisprincipes van Prompt Engineering

<img src="../../../translated_images/nl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Voordat we ingaan op de geavanceerde patronen in deze module, laten we vijf fundamentele promptingtechnieken herzien. Dit zijn de bouwstenen die elke prompt-engineer zou moeten kennen. Als je al door de [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) hebt gewerkt, heb je deze al in actie gezien — hier is het conceptuele raamwerk erachter.

### Zero-Shot Prompting

De eenvoudigste aanpak: geef het model een directe instructie zonder voorbeelden. Het model vertrouwt volledig op zijn training om de taak te begrijpen en uit te voeren. Dit werkt goed voor eenvoudige verzoeken waarbij het verwachte gedrag duidelijk is.

<img src="../../../translated_images/nl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Directe instructie zonder voorbeelden — het model leidt de taak af uit de instructie alleen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwoord: "Positief"
```

**Wanneer te gebruiken:** Eenvoudige classificaties, directe vragen, vertalingen, of elke taak die het model kan afhandelen zonder aanvullende begeleiding.

### Few-Shot Prompting

Geef voorbeelden die het patroon laten zien dat je wilt dat het model volgt. Het model leert het verwachte invoer-uitvoerformaat van je voorbeelden en past dit toe op nieuwe invoer. Dit verbetert de consistentie sterk voor taken waarbij het gewenste formaat of gedrag niet vanzelfsprekend is.

<img src="../../../translated_images/nl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Leren van voorbeelden — het model herkent het patroon en past het toe op nieuwe invoer*

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

Vraag het model om zijn redenering stap voor stap te tonen. In plaats van direct met een antwoord te komen, breekt het model het probleem af en werkt het elk onderdeel expliciet uit. Dit verbetert de nauwkeurigheid bij wiskunde, logica en meerstapsredeneringen.

<img src="../../../translated_images/nl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Stap-voor-stap redenering — complexe problemen opdelen in expliciete logische stappen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Het model toont: 15 - 8 = 7, daarna 7 + 12 = 19 appels
```

**Wanneer te gebruiken:** Wiskundige problemen, logische puzzels, debugging, of elke taak waarbij het tonen van het redeneerproces nauwkeurigheid en vertrouwen verbetert.

### Role-Based Prompting

Stel een persona of rol in voor de AI voordat je je vraag stelt. Dit verschaft context die de toon, diepgang en focus van het antwoord bepaalt. Een "software architect" geeft ander advies dan een "junior ontwikkelaar" of een "security auditor".

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

**Wanneer te gebruiken:** Code reviews, tutoring, domeinspecifieke analyse, of wanneer je antwoorden wilt die zijn afgestemd op een bepaald expertise- of perspectiefniveau.

### Prompt Templates

Maak herbruikbare prompts met variabele plaatsaanduidingen. In plaats van elke keer een nieuwe prompt te schrijven, definieer je een sjabloon één keer en vul je verschillende waarden in. LangChain4j's `PromptTemplate` klasse maakt dit makkelijk met `{{variable}}` syntax.

<img src="../../../translated_images/nl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Herbruikbare prompts met variabele plaatsaanduidingen — één sjabloon, veel toepassingen*

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

**Wanneer te gebruiken:** Herhaalde queries met verschillende invoer, batchverwerking, opbouwen van herbruikbare AI-workflows, of elk scenario waar de prompt-structuur hetzelfde blijft maar de data verandert.

---

Deze vijf basisprincipes geven je een solide toolkit voor de meeste promptingtaken. De rest van deze module bouwt hierop voort met **acht geavanceerde patronen** die gebruikmaken van GPT-5.2's redeneercontrole, zelfevaluatie en gestructureerde outputmogelijkheden.

## Geavanceerde Patronen

Met de basisprincipes behandeld, gaan we naar de acht geavanceerde patronen die deze module uniek maken. Niet alle problemen vragen om dezelfde aanpak. Sommige vragen vereisen snelle antwoorden, andere diep nadenken. Sommige hebben zichtbare redenering nodig, andere alleen resultaten. Elk patroon hieronder is geoptimaliseerd voor een specifieke situatie — en GPT-5.2's redeneercontrole maakt de verschillen nog duidelijker.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overzicht van de acht prompt engineering patronen en hun gebruikssituaties*

<img src="../../../translated_images/nl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2's redeneercontrole stelt je in staat te specificeren hoeveel denken het model moet doen — van snelle directe antwoorden tot diepgaande verkenning*

**Lage Bereidheid (Snel & Gefocust)** - Voor eenvoudige vragen waarbij je snelle, directe antwoorden wilt. Het model doet minimale redenering - maximaal 2 stappen. Gebruik dit voor berekeningen, opzoeken of rechttoe rechtaan vragen.

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
> - "Wat is het verschil tussen low eagerness en high eagerness prompting patronen?"
> - "Hoe helpen de XML-tags in prompts de reactie van de AI structureren?"
> - "Wanneer gebruik ik zelfreflectiepatronen versus directe instructie?"

**Hoge Bereidheid (Diepgaand & Grondig)** - Voor complexe problemen waarbij je uitgebreide analyse wilt. Het model onderzoekt grondig en toont gedetailleerde redenering. Gebruik dit voor systeemontwerp, architectuurbeslissingen of complex onderzoek.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-Stap Vooruitgang)** - Voor meervoudige stap-processen. Het model geeft een plan vooraf, vertelt elke stap tijdens het werk en levert daarna een samenvatting. Gebruik dit voor migraties, implementaties of elk meerstapsproces.

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

Chain-of-Thought prompting vraagt het model expliciet om zijn redeneerproces te tonen, wat de nauwkeurigheid verbetert bij complexe taken. De stap-voor-stap uitsplitsing helpt zowel mensen als AI de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over dit patroon:
> - "Hoe zou ik het taakuitvoeringspatroon aanpassen voor langlopende operaties?"
> - "Wat zijn best practices voor het structureren van tool preambles in productieapplicaties?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en tonen in een UI?"

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor meervoudige stap-taken*

**Zelfreflecterende Code** - Voor het genereren van productieklare code. Het model genereert code volgens productiestandaarden met correcte foutafhandeling. Gebruik dit bij het bouwen van nieuwe features of services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratieve verbetercyclus - genereren, evalueren, problemen identificeren, verbeteren, herhalen*

**Gestructureerde Analyse** - Voor consistente evaluatie. Het model beoordeelt code met een vaste raamwerk (correctheid, praktische methoden, performance, beveiliging, onderhoudbaarheid). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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
> - "Hoe kan ik het analyse-raamwerk aanpassen voor verschillende typen code reviews?"
> - "Wat is de beste manier om gestructureerde output programmatisch te parseren en erop te reageren?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review-sessies?"

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

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

<img src="../../../translated_images/nl/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hoe gesprekcontext zich ophoopt over meerdere beurten totdat de tokenlimiet is bereikt*

**Stap-voor-Stap Redenering** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering voor elke stap. Gebruik dit voor wiskundige problemen, logische puzzels of wanneer je het denkproces wilt begrijpen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/nl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Complexe problemen opdelen in expliciete logische stappen*

**Beperkte Output** - Voor antwoorden met specifieke formatteereisen. Het model volgt strikt format- en lengteregels. Gebruik dit voor samenvattingen of wanneer je een precieze outputstructuur nodig hebt.

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

<img src="../../../translated_images/nl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Handhaven van specifieke format-, lengte- en structuurvereisten*

## Gebruik van Bestaande Azure Resources

**Controleer de deployment:**

Zorg dat het `.env` bestand in de hoofdmap staat met Azure-gegevens (gemaakt tijdens Module 01):
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` uit Module 01 draait deze module al op poort 8083. Je kunt onderstaande startcommando’s overslaan en direct naar http://localhost:8083 gaan.
**Optie 1: Gebruik van Spring Boot Dashboard (Aanbevolen voor VS Code-gebruikers)**

De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-toepassingen te beheren. Je vindt deze in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot-pictogram).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-toepassingen in de werkruimte zien
- Toepassingen starten/stoppen met een enkele klik
- Applicatielogs in realtime bekijken
- De status van applicaties monitoren

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
cd ..  # Vanaf de root directory
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

> **Opmerking:** Als je alle modules handmatig wilt bouwen vóór het starten:
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

*Het hoofd-dashboard dat alle 8 prompt engineering-patronen toont met hun kenmerken en toepassingsgevallen*

## De Patronen Verkennen

De webinterface laat je experimenteren met verschillende promptingstrategieën. Elk patroon lost verschillende problemen op – probeer ze om te zien wanneer elke aanpak het beste werkt.

> **Opmerking: Streaming versus Niet-Streaming** — Elke patroonpagina biedt twee knoppen: **🔴 Stream Response (Live)** en een **Niet-streaming** optie. Streaming gebruikt Server-Sent Events (SSE) om tokens realtime weer te geven terwijl het model ze genereert, zodat je direct voortgang ziet. De niet-streaming optie wacht op de volledige reactie voordat deze wordt getoond. Voor prompts die diepgaand redeneren triggeren (bijv. Hoge Begeerte, Zelfreflecterende Code), kan de niet-streaming oproep erg lang duren — soms minuten — zonder zichtbare feedback. **Gebruik streaming bij het experimenteren met complexe prompts** zodat je het model aan het werk ziet en niet de indruk krijgt dat de aanvraag is verlopen.
>
> **Opmerking: Browservereiste** — De streamingfunctie gebruikt de Fetch Streams API (`response.body.getReader()`) die een volledige browser vereist (Chrome, Edge, Firefox, Safari). Het werkt **niet** in VS Code's ingebouwde Simple Browser, omdat de webview de ReadableStream API niet ondersteunt. Als je de Simple Browser gebruikt, werken de niet-streaming knoppen nog normaal — alleen de streaming knoppen worden beïnvloed. Open `http://localhost:8083` in een externe browser voor de volledige ervaring.

### Lage versus Hoge Begeerte

Stel een eenvoudige vraag zoals "Wat is 15% van 200?" met Lage Begeerte. Je krijgt een direct en direct antwoord. Stel nu iets complexers zoals "Ontwerp een cachingstrategie voor een API met veel verkeer" met Hoge Begeerte. Klik op **🔴 Stream Response (Live)** en bekijk hoe het model gedetailleerde redeneringen token-voor-token toont. Zelfde model, zelfde vraagstructuur – maar de prompt vertelt het hoeveel nadenkwerk het moet doen.

### Taakuitvoering (Tool Preambles)

Multi-stap workflows profiteren van vooraf plannen en voortgangsnarratie. Het model geeft aan wat het gaat doen, vertelt elke stap en vat vervolgens de resultaten samen.

### Zelfreflecterende Code

Probeer "Maak een e-mail validatieservice". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert aan de hand van kwaliteitscriteria, identificeert zwaktes en verbetert. Je ziet het itereren totdat de code aan productiestandaarden voldoet.

### Gestructureerde Analyse

Code reviews hebben consistente beoordelingskaders nodig. Het model analyseert code met vaste categorieën (correctheid, praktijken, prestaties, beveiliging) met ernstniveaus.

### Multi-Turn Chat

Vraag "Wat is Spring Boot?" en volg direct op met "Laat me een voorbeeld zien". Het model onthoudt je eerste vraag en geeft je specifiek een Spring Boot-voorbeeld. Zonder geheugen zou die tweede vraag te vaag zijn.

### Stap-voor-stap Redeneren

Kies een wiskundeprobleem en probeer het met zowel Stap-voor-stap Redeneren als Lage Begeerte. Lage begeerte geeft gewoon het antwoord – snel maar ondoorzichtig. Stap-voor-stap laat elke berekening en beslissing zien.

### Beperkte Output

Wanneer je specifieke formaten of woordenaantallen nodig hebt, dwingt dit patroon strikte naleving af. Probeer een samenvatting te genereren met precies 100 woorden in opsomming.

## Wat Je Echt Leert

**Redeneerinspanning Verandert Alles**

GPT-5.2 laat je de computationele inspanning via je prompts regelen. Lage inspanning betekent snelle antwoorden met minimale exploratie. Hoge inspanning betekent dat het model de tijd neemt om diep na te denken. Je leert inspanning af te stemmen op de complexiteit van de taak – verspil geen tijd aan eenvoudige vragen, maar haast je ook niet bij complexe beslissingen.

**Structuur Stuurt Gedrag**

Merk je de XML-tags in de prompts op? Ze zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Wanneer je meerstapsprocessen of complexe logica nodig hebt, helpt structuur het model bij te houden waar het is en wat er daarna komt.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-stijl organisatie*

**Kwaliteit Door Zelfevaluatie**

De zelfreflecterende patronen werken door kwaliteitscriteria expliciet te maken. In plaats van te hopen dat het model "het goed doet", vertel je precies wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan dan zijn eigen output beoordelen en verbeteren. Dit verandert codegeneratie van een loterij in een proces.

**Context Is Beperkt**

Multi-turn gesprekken werken door berichtgeschiedenis mee te nemen bij elke aanvraag. Maar er is een limiet – elk model heeft een maximaal tokenaantal. Naarmate gesprekken groeien, heb je strategieën nodig om relevante context te bewaren zonder die limiet te overschrijden. Deze module laat zien hoe geheugen werkt; later leer je wanneer je moet samenvatten, vergeten en ophalen.

## Volgende Stappen

**Volgende Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal geldt als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->