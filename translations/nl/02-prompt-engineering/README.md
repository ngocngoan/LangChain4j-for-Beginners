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
  - [Rolgebaseerd Prompting](../../../02-prompt-engineering)
  - [Prompt Sjablonen](../../../02-prompt-engineering)
- [Geavanceerde Patronen](../../../02-prompt-engineering)
- [De Applicatie Uitvoeren](../../../02-prompt-engineering)
- [Applicatie Screenshots](../../../02-prompt-engineering)
- [De Patronen Verkennen](../../../02-prompt-engineering)
  - [Lage vs Hoge Begeerte](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Intro's)](../../../02-prompt-engineering)
  - [Zelfreflecterende Code](../../../02-prompt-engineering)
  - [Gestructureerde Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Stap-voor-Stap Redeneren](../../../02-prompt-engineering)
  - [Beperkte Output](../../../02-prompt-engineering)
- [Wat Je Echt Leren](../../../02-prompt-engineering)
- [Volgende Stappen](../../../02-prompt-engineering)

## Video Walkthrough

Bekijk deze live sessie waarin wordt uitgelegd hoe je aan deze module kunt beginnen:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering met LangChain4j - Live Sessie" width="800"/></a>

## Wat Je Zal Leren

Het volgende diagram geeft een overzicht van de belangrijkste onderwerpen en vaardigheden die je in deze module ontwikkelt — van prompt verfijningstechnieken tot de stap-voor-stap workflow die je volgt.

<img src="../../../translated_images/nl/what-youll-learn.c68269ac048503b2.webp" alt="Wat Je Zal Leren" width="800"/>

In eerdere modules heb je basisinteracties met LangChain4j en GitHub Modellen onderzocht en gezien hoe geheugen gesprek-KI met Azure OpenAI mogelijk maakt. Nu richten we ons op de vragen die je stelt — de prompts zelf — met behulp van Azure OpenAI's GPT-5.2. De manier waarop je prompts structureert, bepaalt in hoge mate de kwaliteit van de antwoorden die je krijgt. We beginnen met een overzicht van de fundamentele prompting technieken en gaan vervolgens over op acht geavanceerde patronen die de volledige mogelijkheden van GPT-5.2 benutten.

We gebruiken GPT-5.2 omdat het redeneerscontrole introduceert - je kunt het model vertellen hoeveel het moet nadenken voordat het antwoord geeft. Dit maakt verschillende prompting strategieën duidelijker en helpt je begrijpen wanneer je welke aanpak moet gebruiken. Bovendien profiteren we van de minder strikte snelheidslimieten van Azure voor GPT-5.2 vergeleken met GitHub Modellen.

## Vereisten

- Module 01 voltooid (Azure OpenAI bronnen gedeployed)
- `.env` bestand in de hoofdmap met Azure-gegevens (aangemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt afgerond, volg dan eerst de deployment instructies daar.

## Begrip van Prompt Engineering

In de kern is prompt engineering het verschil tussen vage en precieze instructies, zoals de vergelijking hieronder laat zien.

<img src="../../../translated_images/nl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Wat is Prompt Engineering?" width="800"/>

Prompt engineering gaat over het ontwerpen van invoertekst die consequent de resultaten oplevert die je nodig hebt. Het gaat niet alleen om het stellen van vragen — het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het geleverd moet worden.

Denk erover als het geven van instructies aan een collega. "Los de bug op" is vaag. "Los de null pointer exception op in UserService.java regel 45 door een null check toe te voegen" is specifiek. Taalmodellen werken op precies dezelfde manier — specificiteit en structuur zijn belangrijk.

Het onderstaande diagram toont hoe LangChain4j in dit plaatje past — het koppelt je prompt patronen aan het model via SystemMessage en UserMessage bouwstenen.

<img src="../../../translated_images/nl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hoe LangChain4j Passen" width="800"/>

LangChain4j levert de infrastructuur — modelverbindingen, geheugen en berichttypes — terwijl prompt patronen gewoon zorgvuldig gestructureerde tekst zijn die je via die infrastructuur verstuurt. De belangrijkste bouwstenen zijn `SystemMessage` (dat het gedrag en de rol van de AI bepaalt) en `UserMessage` (dat je daadwerkelijke verzoek draagt).

## Basisprincipes van Prompt Engineering

De vijf kerntechnieken hieronder vormen de basis van effectieve prompt engineering. Elk adresseert een ander aspect van hoe je met taalmodellen communiceert.

<img src="../../../translated_images/nl/five-patterns-overview.160f35045ffd2a94.webp" alt="Overzicht Vijf Prompt Engineering Patronen" width="800"/>

Voordat we ingaan op de geavanceerde patronen in deze module, laten we vijf fundamentele prompting technieken herzien. Dit zijn de bouwstenen die elke prompt engineer zou moeten kennen. Als je de [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) al hebt doorlopen, heb je deze al in actie gezien — hier is het conceptuele raamwerk erachter.

### Zero-Shot Prompting

De eenvoudigste aanpak: geef het model een directe instructie zonder voorbeelden. Het model vertrouwt volledig op zijn training om de taak te begrijpen en uit te voeren. Dit werkt goed voor eenvoudige verzoeken waarbij het verwachte gedrag duidelijk is.

<img src="../../../translated_images/nl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Directe instructie zonder voorbeelden — het model leidt de taak af uit de instructie alleen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Antwoord: "Positief"
```

**Wanneer te gebruiken:** Simpele classificaties, directe vragen, vertalingen of elke taak die het model zonder extra begeleiding aankan.

### Few-Shot Prompting

Geef voorbeelden die het patroon tonen dat je wilt dat het model volgt. Het model leert het verwachte invoer-uitvoerformaat van je voorbeelden en past dat toe op nieuwe invoer. Dit verbetert de consistentie aanzienlijk voor taken waarbij het gewenste formaat of gedrag niet vanzelfsprekend is.

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

**Wanneer te gebruiken:** Aangepaste classificaties, consistente opmaak, domeinspecifieke taken of wanneer zero-shot resultaten inconsistent zijn.

### Chain of Thought

Vraag het model zijn redenering stap voor stap te laten zien. In plaats van direct een antwoord te geven, breekt het model het probleem af en werkt het expliciet door elke stap heen. Dit verbetert de nauwkeurigheid bij wiskunde, logica en meerstaps redeneertaken.

<img src="../../../translated_images/nl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Stap-voor-stap redeneren — complexe problemen opdelen in expliciete logische stappen*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Het model toont: 15 - 8 = 7, dan 7 + 12 = 19 appels
```

**Wanneer te gebruiken:** Wiskundeproblemen, logische puzzels, debugging of elke taak waarbij het tonen van de redeneercyclus de nauwkeurigheid en vertrouwen verhoogt.

### Rolgebaseerd Prompting

Stel een persona of rol in voor de AI voordat je je vraag stelt. Dit geeft context die de toon, diepgang, en focus van het antwoord vormgeeft. Een "software-architect" geeft anders advies dan een "junior developer" of een "security auditor".

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

**Wanneer te gebruiken:** Code-reviews, tutoring, domeinspecifieke analyse of wanneer je antwoorden wilt afgestemd op een bepaald expertise niveau of perspectief.

### Prompt Sjablonen

Maak herbruikbare prompts met variabele placeholders. In plaats van elke keer een nieuwe prompt te schrijven, definieer je één sjabloon en vul je verschillende waardes in. De `PromptTemplate` klasse van LangChain4j maakt dit eenvoudig met `{{variable}}` syntax.

<img src="../../../translated_images/nl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Sjablonen" width="800"/>

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

**Wanneer te gebruiken:** Herhaalde vragen met verschillende invoeren, batchverwerking, het bouwen van herbruikbare AI workflows, of elk scenario waarbij de promptstructuur hetzelfde blijft maar de data verandert.

---

Deze vijf fundamenten geven je een solide toolkit voor de meeste prompting taken. De rest van deze module bouwt daarop voort met **acht geavanceerde patronen** die de redeneerscontrole, zelfevaluatie en gestructureerde output capaciteiten van GPT-5.2 benutten.

## Geavanceerde Patronen

Met de fundamenten behandeld, gaan we nu naar de acht geavanceerde patronen die deze module uniek maken. Niet alle vraagstukken vereisen dezelfde aanpak. Sommige vragen hebben snelle antwoorden nodig, andere diepgaand denken. Sommige vragen vergen zichtbare redenering, andere alleen resultaten. Elk patroon hieronder is geoptimaliseerd voor een verschillend scenario — en de redeneerscontrole van GPT-5.2 maakt de verschillen nog duidelijker.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompt Engineering Patronen" width="800"/>

*Overzicht van de acht prompt engineering patronen en hun gebruikssituaties*

GPT-5.2 voegt een extra dimensie toe aan deze patronen: *redeneerscontrole*. De schuifregelaar hieronder laat zien hoe je het denkvermogen van het model kunt aanpassen — van snelle, directe antwoorden tot diepe, grondige analyse.

<img src="../../../translated_images/nl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Redeneerscontrole met GPT-5.2" width="800"/>

*De redeneerscontrole van GPT-5.2 laat je specificeren hoeveel het model moet nadenken — van snel directe antwoorden tot diepgaande exploratie*

**Lage Begeerte (Snel & Gefocust)** - Voor eenvoudige vragen waar je snelle, directe antwoorden wilt. Het model doet minimale redenering - maximaal 2 stappen. Gebruik dit voor berekeningen, opzoeken of rechttoe rechtaan vragen.

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
> - "Wat is het verschil tussen lage en hoge begeerte prompting patronen?"
> - "Hoe helpen de XML-tags in prompts om de reactie van de AI te structureren?"
> - "Wanneer gebruik ik zelfreflectie patronen versus directe instructie?"

**Hoge Begeerte (Diep & Grondig)** - Voor complexe problemen waar je een uitgebreide analyse wilt. Het model onderzoekt grondig en toont gedetailleerde redenering. Gebruik dit voor systeemuontwerp, architectuurbeslissingen of diepgaand onderzoek.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-Stap Voortgang)** - Voor meerstaps workflows. Het model geeft een upfront plan, beschrijft elke stap terwijl het werkt, en geeft een samenvatting. Gebruik dit voor migraties, implementaties, of elk meerstaps proces.

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

Chain-of-Thought prompting vraagt het model expliciet zijn redeneercyclus te tonen, wat de nauwkeurigheid bij complexe taken verbetert. De stap-voor-stap opsplitsing helpt zowel mensen als AI om de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over dit patroon:
> - "Hoe pas ik het taakuitvoeringspatroon aan voor langlopende operaties?"
> - "Wat zijn best practices voor het structureren van tool intro's in productieomgevingen?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en tonen in een UI?"

Het onderstaande diagram illustreert deze Plan → Uitvoeren → Samenvatten workflow.

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Taakuitvoeringspatroon" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor meerstaps taken*

**Zelfreflecterende Code** - Voor het genereren van productiekwaliteit code. Het model genereert code volgens productiestandaarden met correcte foutafhandeling. Gebruik dit bij het bouwen van nieuwe features of services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Het onderstaande diagram toont deze iteratieve verbetercyclus — genereren, evalueren, zwakke punten identificeren, en verfijnen totdat de code voldoet aan de productiestandaarden.

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Zelfreflectiecyclus" width="800"/>

*Iteratieve verbetercyclus - genereren, evalueren, problemen identificeren, verbeteren, herhalen*

**Gestructureerde Analyse** - Voor consistente evaluatie. Het model beoordeelt code volgens een vast kader (correctheid, praktijken, prestaties, veiligheid, onderhoudbaarheid). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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
> - "Hoe kan ik het analyse raamwerk aanpassen voor verschillende typen code reviews?"
> - "Wat is de beste manier om gestructureerde output programmatisch te parsen en erop te reageren?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review sessies heen?"

Het volgende diagram toont hoe dit gestructureerde kader een code review ordent in consistente categorieën met ernstniveaus.

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Gestructureerd Analysepatroon" width="800"/>

*Kader voor consistente code reviews met ernstniveaus*

**Multi-Turn Chat** - Voor gesprekken die context nodig hebben. Het model onthoudt eerdere berichten en bouwt daarop voort. Gebruik dit voor interactieve hulpsessies of complexe vraag & antwoord.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Het onderstaande diagram visualiseert hoe de context van het gesprek zich opstapelt bij elke beurt en hoe dit relateert aan de tokenlimiet van het model.

<img src="../../../translated_images/nl/context-memory.dff30ad9fa78832a.webp" alt="Contextgeheugen" width="800"/>

*Hoe gesprekcontext zich opstapelt over meerdere beurten tot aan de tokenlimiet*
**Stapsgewijze Uitleg** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering voor elke stap. Gebruik dit voor wiskundige problemen, logische puzzels, of wanneer je het denkproces wilt begrijpen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Het onderstaande diagram illustreert hoe het model problemen opsplitst in expliciete, genummerde logische stappen.

<img src="../../../translated_images/nl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Stapsgewijs Patroon" width="800"/>

*Problemen opsplitsen in expliciete logische stappen*

**Beperkte Output** - Voor antwoorden met specifieke formatteervereisten. Het model volgt strikt de format- en lengteregels. Gebruik dit voor samenvattingen of wanneer je een nauwkeurige outputstructuur nodig hebt.

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

Het volgende diagram toont hoe beperkingen het model sturen om output te produceren die strikt voldoet aan jouw format- en lengtevereisten.

<img src="../../../translated_images/nl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Beperkt Output Patroon" width="800"/>

*Specifieke eisen aan format, lengte en structuur afdwingen*

## De Applicatie uitvoeren

**Verifieer de deployment:**

Zorg ervoor dat het `.env` bestand aanwezig is in de hoofdmap met Azure-gegevens (gemaakt tijdens Module 01). Voer dit uit vanuit de modulemap (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` vanuit de hoofdmap (zoals beschreven in Module 01), draait deze module al op poort 8083. Je kunt de onderstaande startopdrachten overslaan en direct naar http://localhost:8083 gaan.

**Optie 1: Gebruik van Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De dev container bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt deze in de Activity Bar aan de linkerkant van VS Code (zoek naar het Spring Boot icoon).

Vanuit het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de werkruimte zien
- Applicaties starten/stoppen met één klik
- Applicatielogs in real-time bekijken
- Applicatiestatus monitoren

Klik simpelweg op de afspeelknop naast "prompt-engineering" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Het Spring Boot Dashboard in VS Code — start, stop en monitor alle modules vanaf één plek*

**Optie 2: Gebruik van shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de root directory
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

Beide scripts laden automatisch omgevingsvariabelen uit het `.env` bestand in de hoofdmap en bouwen de JAR bestanden als deze nog niet bestaan.

> **Opmerking:** Als je alle modules liever handmatig bouwt voordat je start:
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

## Applicatie Screenshots

Hier is de hoofdinterface van de prompt engineering module, waar je met alle acht patronen tegelijk kunt experimenteren.

<img src="../../../translated_images/nl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Het hoofd dashboard toont alle 8 prompt engineering patronen met hun kenmerken en gebruikssituaties*

## Het verkennen van de Patronen

De webinterface laat je experimenteren met verschillende prompting strategieën. Elk patroon lost andere problemen op – probeer ze uit om te zien wanneer elke aanpak uitblinkt.

> **Opmerking: Streaming versus Non-Streaming** — Elke patroonpagina biedt twee knoppen: **🔴 Stream Response (Live)** en een **Non-streaming** optie. Streaming gebruikt Server-Sent Events (SSE) om tokens in real-time weer te geven terwijl het model ze genereert, zodat je direct voortgang ziet. De non-streaming optie wacht totdat de hele reactie compleet is voordat deze getoond wordt. Voor prompts die diepgaande redenering triggeren (bijv. High Eagerness, Self-Reflecting Code) kan de non-streaming oproep erg lang duren – soms minuten – zonder zichtbare feedback. **Gebruik streaming bij experimenteren met complexe prompts** zodat je het model aan het werk ziet en de indruk van een time-out voorkomt.
>
> **Opmerking: Browservereiste** — De streaming functie gebruikt de Fetch Streams API (`response.body.getReader()`) die een volledige browser vereist (Chrome, Edge, Firefox, Safari). Het werkt **niet** in de ingebouwde Simple Browser van VS Code, omdat de webview de ReadableStream API niet ondersteunt. Als je de Simple Browser gebruikt, werken de non-streaming knoppen wel normaal — alleen de streaming knoppen niet. Open `http://localhost:8083` in een externe browser voor de volledige ervaring.

### Lage versus Hoge Bereidheid

Stel een simpele vraag zoals "Wat is 15% van 200?" met Lage Bereidheid. Je krijgt een direct, snel antwoord. Stel nu iets complexers zoals "Ontwerp een caching strategie voor een drukke API" met Hoge Bereidheid. Klik op **🔴 Stream Response (Live)** en volg hoe het model gedetailleerde redeneringen token-voor-token laat zien. Zelfde model, zelfde vraagstructuur - maar de prompt vertelt hoeveel het moet nadenken.

### Taakuitvoering (Tool Preambles)

Meerstaps workflows profiteren van vooraf plannen en voortgangsnarratie. Het model schetst wat het gaat doen, vertelt elke stap, en vat daarna de resultaten samen.

### Zelfreflecterende Code

Probeer "Maak een e-mail validatieservice". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert op kwaliteitseisen, identificeert zwaktes en verbetert. Je ziet hoe het iteratief doorgaat tot de code voldoet aan productie-eisen.

### Gestructureerde Analyse

Code reviews vragen om consistente evaluatiekaders. Het model analyseert code met vaste categorieën (correctheid, praktijken, prestaties, beveiliging) met ernstniveaus.

### Meerstaps Chat

Vraag "Wat is Spring Boot?" en volg meteen op met "Laat me een voorbeeld zien". Het model onthoudt je eerste vraag en geeft een voorbeeld dat bij Spring Boot past. Zonder geheugen zou die tweede vraag te vaag zijn.

### Stapsgewijze Redenering

Kies een wiskundeprobleem en probeer het met Stapsgewijze Redenering en Lage Bereidheid. Lage bereidheid geeft alleen het antwoord – snel maar ondoorzichtig. Stapsgewijs laat elke berekening en beslissing zien.

### Beperkte Output

Als je specifieke formaten of woordenaantallen nodig hebt, dwingt dit patroon strikte naleving af. Probeer een samenvatting te genereren van precies 100 woorden in bullet points.

## Wat je Echt Leert

**Redeneringsinspanning Verandert Alles**

GPT-5.2 laat je de rekeninspanning sturen via je prompts. Lage inspanning betekent snelle reacties met minimale exploratie. Hoge inspanning betekent dat het model de tijd neemt om diep na te denken. Je leert de inspanning afstemmen op de taakcomplexiteit - verspil geen tijd aan simpele vragen, maar haast complexe beslissingen ook niet.

**Structuur Stuurt Gedrag**

Valt het je op dat prompts XML-tags bevatten? Die zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Als je meerstaps processen of complexe logica nodig hebt, helpt structuur het model bij te houden waar het is en wat volgt. Het diagram hieronder ontleedt een goed gestructureerde prompt met tags zoals `<system>`, `<instructions>`, `<context>`, `<user-input>`, en `<constraints>` die je instructies in duidelijke secties ordenen.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structuur" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-stijl organisatie*

**Kwaliteit Door Zelfevaluatie**

De zelfreflecterende patronen maken kwaliteitscriteria expliciet. In plaats van te hopen dat het model "het goed doet", vertel je precies wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan zo zijn eigen output evalueren en verbeteren. Dit maakt codegeneratie van een loterij tot een proces.

**Context is Beperkt**

Meerstapsgesprekken werken door het opnemen van berichtgeschiedenis bij elke aanvraag. Maar er is een limiet – elk model heeft een maximum aantal tokens. Naarmate gesprekken groeien, heb je strategieën nodig om relevante context te bewaren zonder dat plafond te raken. Deze module laat zien hoe geheugen werkt; later leer je wanneer samenvatten, wanneer vergeten, en wanneer ophalen nodig is.

## Volgende Stappen

**Volgende Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar Main](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal dient als de gezaghebbende bron te worden beschouwd. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->