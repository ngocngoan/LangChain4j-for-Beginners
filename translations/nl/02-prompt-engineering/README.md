# Module 02: Prompt Engineering met GPT-5.2

## Inhoudsopgave

- [Wat Je Zal Leren](../../../02-prompt-engineering)
- [Vereisten](../../../02-prompt-engineering)
- [Begrijpen van Prompt Engineering](../../../02-prompt-engineering)
- [Hoe Dit LangChain4j Gebruikt](../../../02-prompt-engineering)
- [De Kernpatronen](../../../02-prompt-engineering)
- [Gebruik van Bestaande Azure Resources](../../../02-prompt-engineering)
- [Applicatie Schermafbeeldingen](../../../02-prompt-engineering)
- [De Patronen Verkennen](../../../02-prompt-engineering)
  - [Lage vs Hoge Bereidheid](../../../02-prompt-engineering)
  - [Taakuitvoering (Tool Preambles)](../../../02-prompt-engineering)
  - [Zelfreflecterende Code](../../../02-prompt-engineering)
  - [Gestructureerde Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Stap-voor-Stap Redeneren](../../../02-prompt-engineering)
  - [Beperkte Output](../../../02-prompt-engineering)
- [Wat Je Echt Leren](../../../02-prompt-engineering)
- [Volgende Stappen](../../../02-prompt-engineering)

## Wat Je Zal Leren

In de vorige module zag je hoe geheugen conversatie-AI mogelijk maakt en gebruikte je GitHub Models voor basisinteracties. Nu richten we ons op hoe je vragen stelt - de prompts zelf - met Azure OpenAI's GPT-5.2. De manier waarop je je prompts structureert heeft een grote invloed op de kwaliteit van de antwoorden die je krijgt.

We gebruiken GPT-5.2 omdat het redeneringscontrole introduceert - je kunt het model vertellen hoeveel het moet nadenken voor het antwoorden. Dit maakt verschillende prompting strategieën duidelijker en helpt je begrijpen wanneer je welke methode moet toepassen. We profiteren ook van Azure's minder strenge snelheidsbeperkingen voor GPT-5.2 vergeleken met GitHub Models.

## Vereisten

- Module 01 voltooid (Azure OpenAI-resources gedeployed)
- `.env` bestand in de hoofdmap met Azure-gegevens (gemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 niet hebt voltooid, volg dan eerst de deploymentsinstructies daar.

## Begrijpen van Prompt Engineering

Prompt engineering draait om het ontwerpen van invoerteksten die consequent de resultaten opleveren die je nodig hebt. Het gaat niet alleen om het stellen van vragen - het gaat om het structureren van verzoeken zodat het model precies begrijpt wat je wilt en hoe het dat moet leveren.

Denk er aan als instructies geven aan een collega. "Fix de bug" is vaag. "Fix de null pointer exception in UserService.java regel 45 door een null check toe te voegen" is specifiek. Taalmodellen werken hetzelfde - specificiteit en structuur zijn belangrijk.

## Hoe Dit LangChain4j Gebruikt

Deze module toont geavanceerde prompting patronen met dezelfde LangChain4j basis uit eerdere modules, met focus op promptstructuur en redeneringscontrole.

<img src="../../../translated_images/nl/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Stroom" width="800"/>

*Hoe LangChain4j je prompts verbindt met Azure OpenAI GPT-5.2*

**Dependencies** - Module 02 gebruikt de volgende langchain4j dependencies gedefinieerd in `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel Configuratie** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Het chatmodel is handmatig geconfigureerd als een Spring bean met de OpenAI Official client, die Azure OpenAI endpoints ondersteunt. Het grootste verschil met Module 01 is hoe we de prompts structureren die naar `chatModel.chat()` worden gestuurd, niet de modelopzet zelf.

**System en User Berichten** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j scheidt berichttypes voor duidelijkheid. `SystemMessage` stelt het gedrag en de context van de AI in (zoals "Je bent een code reviewer"), terwijl `UserMessage` het daadwerkelijke verzoek bevat. Deze scheiding zorgt ervoor dat je consistente AI-gedrag behoudt over verschillende gebruikersvragen heen.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/nl/message-types.93e0779798a17c9d.webp" alt="Architectuur Berichten Types" width="800"/>

*SystemMessage biedt blijvende context terwijl UserMessages individuele verzoeken bevatten*

**MessageWindowChatMemory voor Multi-Turn** - Voor het multi-turn gesprekspatroon hergebruiken we `MessageWindowChatMemory` uit Module 01. Elke sessie krijgt zijn eigen geheugeninstantie opgeslagen in een `Map<String, ChatMemory>`, waardoor meerdere gelijktijdige gesprekken mogelijk zijn zonder contextverwarring.

**Prompt Templates** - De echte focus ligt hier op prompt engineering, niet op nieuwe LangChain4j API’s. Elk patroon (lage bereidheid, hoge bereidheid, taakuitvoering, enz.) gebruikt dezelfde `chatModel.chat(prompt)` methode maar met zorgvuldig gestructureerde promptteksten. De XML-tags, instructies en opmaak maken allemaal deel uit van de prompttekst, niet van LangChain4j-functionaliteit.

**Redeneringscontrole** - GPT-5.2’s redeneringsinspanning wordt geregeld via promptinstructies zoals "maximaal 2 redeneringsstappen" of "grondig onderzoeken". Dit zijn prompt engineering technieken, geen LangChain4j-configuraties. De bibliotheek levert simpelweg je prompts aan het model.

De belangrijkste les: LangChain4j verzorgt de infrastructuur (modelverbinding via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), geheugen, berichtafhandeling via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), terwijl deze module je leert hoe je effectieve prompts maakt binnen die infrastructuur.

## De Kernpatronen

Niet alle problemen vragen om dezelfde aanpak. Sommige vragen vragen snelle antwoorden, andere diepe denkwijzen. Sommige hebben zichtbare redenering nodig, andere alleen resultaten. Deze module behandelt acht prompting patronen - elk geoptimaliseerd voor verschillende scenario’s. Je gaat met ze allemaal experimenteren om te leren wanneer welke aanpak het beste werkt.

<img src="../../../translated_images/nl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Acht Prompting Patronen" width="800"/>

*Overzicht van de acht prompt engineering patronen en hun gebruikssituaties*

<img src="../../../translated_images/nl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Vergelijking Redeneringsinspanning" width="800"/>

*Lage bereidheid (snel, direct) vs Hoge bereidheid (grondig, verkennend) redeneringsaanpakken*

**Lage Bereidheid (Snel & Gericht)** - Voor eenvoudige vragen waarbij je snelle, directe antwoorden wilt. Het model doet minimale redenering - maximaal 2 stappen. Gebruik dit voor berekeningen, opzoeken of simpele vragen.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Verken met GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) en vraag:
> - "Wat is het verschil tussen lage en hoge bereidheid prompting patronen?"
> - "Hoe helpen de XML-tags in prompts om het antwoord van de AI te structureren?"
> - "Wanneer gebruik ik zelfreflectiepatronen versus directe instructies?"

**Hoge Bereidheid (Diep & Grondig)** - Voor complexe problemen waarbij je uitgebreide analyse wilt. Het model onderzoekt grondig en toont gedetailleerde redenering. Gebruik dit voor systeemontwerp, architectuurbeslissingen of complex onderzoek.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Taakuitvoering (Stap-voor-Stap Voortgang)** - Voor workflows met meerdere stappen. Het model biedt een plan vooraf, beschrijft elke stap tijdens uitvoering, en geeft daarna een samenvatting. Gebruik dit voor migraties, implementaties of elke meerstapsprocedure.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting vraagt het model expliciet om zijn redeneringsproces te tonen, wat de nauwkeurigheid bij complexe taken verbetert. De stapsgewijze uitsplitsing helpt zowel mensen als AI om de logica te begrijpen.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over dit patroon:
> - "Hoe pas ik het taakuitvoeringspatroon aan voor langlopende bewerkingen?"
> - "Wat zijn best practices voor het structureren van tool preambles in productieapplicaties?"
> - "Hoe kan ik tussentijdse voortgangsupdates vastleggen en tonen in een UI?"

<img src="../../../translated_images/nl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Taakuitvoeringspatroon" width="800"/>

*Plan → Uitvoeren → Samenvatten workflow voor meerstaps taken*

**Zelfreflecterende Code** - Voor het genereren van code van productiekwaliteit. Het model genereert code, controleert die aan kwaliteitseisen, en verbetert iteratief. Gebruik dit bij het bouwen van nieuwe functies of services.

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

<img src="../../../translated_images/nl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Zelfreflectiecyclus" width="800"/>

*Iteratieve verbeterlus - genereren, evalueren, problemen identificeren, verbeteren, herhalen*

**Gestructureerde Analyse** - Voor consistente evaluaties. Het model beoordeelt code via een vast kader (correctheid, praktijken, prestaties, beveiliging). Gebruik dit voor code reviews of kwaliteitsbeoordelingen.

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

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Vraag over gestructureerde analyse:
> - "Hoe kan ik het analyse-framework aanpassen voor verschillende soorten code reviews?"
> - "Wat is de beste manier om gestructureerde output programatisch te verwerken?"
> - "Hoe zorg ik voor consistente ernstniveaus over verschillende review-sessies heen?"

<img src="../../../translated_images/nl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Gestructureerd Analysepatroon" width="800"/>

*Vier-categorie kader voor consistente code reviews met serieusheidsniveaus*

**Multi-Turn Chat** - Voor gesprekken die context nodig hebben. Het model onthoudt vorige berichten en bouwt daarop voort. Gebruik dit voor interactieve hulpsessies of complexe Q&A.

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

*Hoe gesprekcontext zich opstapelt over meerdere beurten tot de tokengrens is bereikt*

**Stap-voor-Stap Redeneren** - Voor problemen die zichtbare logica vereisen. Het model toont expliciete redenering voor elke stap. Gebruik dit voor wiskundige problemen, logische puzzels of als je het denkproces wilt begrijpen.

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

*Problemen uitsplitsen in expliciete logische stappen*

**Beperkte Output** - Voor antwoorden met specifieke formatvereisten. Het model volgt strikt format- en lengteregels. Gebruik dit voor samenvattingen of wanneer een precieze outputstructuur nodig is.

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

<img src="../../../translated_images/nl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Beperkt Output Patroon" width="800"/>

*Opleggen van specifieke format-, lengte- en structuurvereisten*

## Gebruik van Bestaande Azure Resources

**Controleer de deployment:**

Zorg dat het `.env` bestand in de hoofdmap staat met Azure-gegevens (gemaakt tijdens Module 01):
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_SLEUTEL, IMPLEMENTATIE tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` uit Module 01, draait deze module al op poort 8083. Je kunt de onderstaande startcommando’s overslaan en direct naar http://localhost:8083 gaan.

**Optie 1: Gebruik Spring Boot Dashboard (aanbevolen voor VS Code gebruikers)**

De dev container bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt hem in de Activity Bar links in VS Code (zoek het Spring Boot icoon).

Vanuit het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de workspace zien
- Applicaties met één klik starten/stoppen
- Applicatielogs in realtime bekijken
- Applicatiestatus monitoren

Klik simpelweg op de afspeelknop naast "prompt-engineering" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de rootdirectory
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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand en bouwen de JAR’s als die nog niet bestaan.

> **Opmerking:** Als je liever alle modules handmatig bouwt voor het starten:
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

**Stoppen:**

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

## Applicatie Schermafbeeldingen

<img src="../../../translated_images/nl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Startpagina" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Het hoofd dashboard toont alle 8 prompt engineering patronen met hun kenmerken en gebruikssituaties*

## De Patronen Verkennen

De webinterface laat je experimenteren met verschillende prompting strategieën. Elk patroon lost andere problemen op - probeer ze om te zien wanneer welke aanpak uitblinkt.

### Lage vs Hoge Bereidheid

Stel een simpele vraag als "Wat is 15% van 200?" met Lage Bereidheid. Je krijgt een direct, snel antwoord. Vraag dan iets complexers zoals "Ontwerp een cachingstrategie voor een API met veel verkeer" met Hoge Bereidheid. Zie hoe het model vertraagt en gedetailleerde redenering geeft. Zelfde model, zelfde vraagstructuur - maar de prompt vertelt hoeveel nadenken het moet doen.
<img src="../../../translated_images/nl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snel rekenen met minimale redenering*

<img src="../../../translated_images/nl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omvattende cachingstrategie (2.8MB)*

### Taakuitvoering (Tool Preambles)

Workflows met meerdere stappen profiteren van vooraf plannen en voortgangsnarratie. Het model schetst wat het gaat doen, vertelt elke stap, en vat dan de resultaten samen.

<img src="../../../translated_images/nl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Een REST-eindpunt maken met stapsgewijze narratie (3.9MB)*

### Zelfreflecterende Code

Probeer "Maak een e-mail validatieservice". In plaats van alleen code te genereren en te stoppen, genereert het model, evalueert aan de hand van kwaliteitscriteria, identificeert zwaktes, en verbetert. Je ziet het itereren totdat de code voldoet aan productienormen.

<img src="../../../translated_images/nl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Volledige e-mail validatieservice (5.2MB)*

### Gestructureerde Analyse

Code reviews hebben consistente beoordelingskaders nodig. Het model analyseert code met vaste categorieën (correctheid, praktijken, prestaties, beveiliging) met ernstniveaus.

<img src="../../../translated_images/nl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-gebaseerde code review*

### Multi-Turn Chat

Vraag "Wat is Spring Boot?" en volg direct op met "Laat me een voorbeeld zien". Het model onthoudt je eerste vraag en geeft je specifiek een Spring Boot voorbeeld. Zonder geheugen zou die tweede vraag te vaag zijn.

<img src="../../../translated_images/nl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Contextbehoud over vragen heen*

### Stap-voor-Stap Redenering

Kies een wiskundeprobleem en probeer het met zowel Stap-voor-Stap Redenering als Lage Eagerness. Lage eagerness geeft je alleen het antwoord - snel maar ondoorzichtig. Stap-voor-stap toont elke berekening en beslissing.

<img src="../../../translated_images/nl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Wiskundeprobleem met expliciete stappen*

### Beperkte Uitvoer

Wanneer je specifieke formaten of woordenaantallen nodig hebt, zorgt dit patroon voor strikte naleving. Probeer een samenvatting te genereren met precies 100 woorden in opsommingstekens.

<img src="../../../translated_images/nl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Samenvatting machine learning met formatcontrole*

## Wat je Echt Lërt

**Redeneerinspanning Verandert Alles**

GPT-5.2 laat je de rekencapaciteit regelen via je prompts. Lage inspanning betekent snelle antwoorden met minimale verkenning. Hoge inspanning betekent dat het model de tijd neemt om diep na te denken. Je leert inspanning af te stemmen op taakcomplexiteit - verspil geen tijd aan eenvoudige vragen, maar haast je ook niet bij complexe beslissingen.

**Structuur Stuurt Gedrag**

Zie je de XML-tags in de prompts? Ze zijn niet decoratief. Modellen volgen gestructureerde instructies betrouwbaarder dan vrije tekst. Wanneer je meerstapprocessen of complexe logica nodig hebt, helpt structuur het model te volgen waar het is en wat volgt.

<img src="../../../translated_images/nl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomie van een goed gestructureerde prompt met duidelijke secties en XML-stijl organisatie*

**Kwaliteit Door Zelfevaluatie**

De zelfreflecterende patronen werken door kwaliteitscriteria expliciet te maken. In plaats van te hopen dat het model "het goed doet", vertel je precies wat "goed" betekent: correcte logica, foutafhandeling, prestaties, beveiliging. Het model kan zo zijn eigen output evalueren en verbeteren. Dit verandert codegeneratie van een loterij in een proces.

**Context is Eindig**

Multi-turn gesprekken werken door de berichtgeschiedenis bij elk verzoek mee te sturen. Maar er is een limiet - elk model heeft een maximum aantal tokens. Naarmate gesprekken groeien, heb je strategieën nodig om relevante context te behouden zonder die limiet te bereiken. Deze module laat zien hoe geheugen werkt; later leer je wanneer samenvatten, vergeten en ophalen nodig is.

## Volgende Stappen

**Volgende Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigatie:** [← Vorige: Module 01 - Introductie](../01-introduction/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de originele taal geldt als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->