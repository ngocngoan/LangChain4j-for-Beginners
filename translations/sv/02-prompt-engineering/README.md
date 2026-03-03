# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Video Walkthrough](../../../02-prompt-engineering)
- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förutsättningar](../../../02-prompt-engineering)
- [Förstå Prompt Engineering](../../../02-prompt-engineering)
- [Grunderna i Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollbaserad Prompting](../../../02-prompt-engineering)
  - [Promptmallar](../../../02-prompt-engineering)
- [Avancerade mönster](../../../02-prompt-engineering)
- [Kör applikationen](../../../02-prompt-engineering)
- [Skärmdumpar från applikationen](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs Hög Iver](../../../02-prompt-engineering)
  - [Uppgiftshantering (Verktygspreambler)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flerstegs-chatt](../../../02-prompt-engineering)
  - [Steg-för-steg-resonemang](../../../02-prompt-engineering)
  - [Begränsad output](../../../02-prompt-engineering)
- [Vad du egentligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Video Walkthrough

Titta på denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Vad du kommer att lära dig

Följande diagram ger en översikt över de viktigaste ämnena och färdigheterna du kommer att utveckla i denna modul — från tekniker för förfining av prompts till det steg-för-steg-arbetsflöde du kommer följa.

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I tidigare moduler har du utforskat grundläggande LangChain4j-interaktioner med GitHub Models och sett hur minne möjliggör konversations-AI med Azure OpenAI. Nu ska vi fokusera på hur du ställer frågor — själva prompterna — med Azure OpenAI:s GPT-5.2. Sättet du strukturerar dina prompts påverkar dramatiskt kvaliteten på svaren du får. Vi börjar med en översikt över grundläggande prompttekniker, sedan går vi vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapabiliteter fullt ut.

Vi använder GPT-5.2 eftersom det introducerar resonemangskontroll – du kan berätta för modellen hur mycket tänkande den ska göra innan den svarar. Detta gör olika prompting-strategier tydligare och hjälper dig förstå när du ska använda varje metod. Vi drar också nytta av Azures färre begränsningar för GPT-5.2 jämfört med GitHub Models.

## Förutsättningar

- Genomförd Modul 01 (Azure OpenAI-resurser utplacerade)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Obs:** Om du inte har genomfört Modul 01, följ först installationsinstruktionerna där.

## Förstå Prompt Engineering

I grunden handlar prompt engineering om skillnaden mellan vaga instruktioner och precisa sådana, som jämförelsen nedan illustrerar.

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handlar om att designa inmatad text som konsekvent ger dig de resultat du behöver. Det är inte bara att ställa frågor – det handlar om att strukturera begäran så att modellen exakt förstår vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fix the bug" är vagt. "Fix the null pointer exception in UserService.java line 45 by adding a null check" är specifikt. Språkmodeller fungerar på samma sätt – precision och struktur spelar roll.

Diagrammet nedan visar hur LangChain4j passar in i bilden — kopplar dina promptmönster till modellen via byggstenarna SystemMessage och UserMessage.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j tillhandahåller infrastrukturen — modellanslutningar, minne och meddelandetyper — medan promptmönster är bara noggrant strukturerad text som du skickar genom den infrastrukturen. De viktiga byggstenarna är `SystemMessage` (som ställer in AI:ns beteende och roll) och `UserMessage` (som bär din faktiska begäran).

## Grunderna i Prompt Engineering

De fem kärnteknikerna nedan utgör grunden för effektiv prompt engineering. Var och en tar upp en annan aspekt av hur du kommunicerar med språkmodeller.

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Innan vi dyker in i de avancerade mönstren i denna modul, låt oss gå igenom fem grundläggande prompting-tekniker. Dessa är byggstenarna som varje promptingenjör bör känna till. Om du redan har arbetat igenom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns) har du sett dessa i praktiken – här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Det enklaste tillvägagångssättet: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Detta fungerar bra för enkla begäranden där förväntat beteende är uppenbart.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel – modellen härleder uppgiften endast från instruktionen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**När man ska använda:** Enkla klassificeringar, direkta frågor, översättningar eller uppgifter som modellen kan hantera utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar mönstret du vill att modellen ska följa. Modellen lär sig det förväntade indata-utdata-formatet från dina exempel och tillämpar det på nya indata. Detta förbättrar dramatiskt konsekvensen för uppgifter där önskat format eller beteende inte är uppenbart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lär sig från exempel – modellen identifierar mönstret och tillämpar det på nya indata*

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

**När man ska använda:** Anpassade klassificeringar, konsekvent formattering, domänspecifika uppgifter eller när zero-shot-resultat är inkonsekventa.

### Chain of Thought

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar bryter modellen ner problemet och arbetar igenom varje del explicit. Detta förbättrar noggrannheten i matematik, logik och flerstegsresonemangsuppgifter.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg-resonemang – bryter ner komplexa problem i explicita logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```

**När man ska använda:** Matematikproblem, logikpussel, felsökning eller andra uppgifter där att visa resonemangsprocessen förbättrar noggrannhet och förtroende.

### Rollbaserad Prompting

Sätt en persona eller roll för AI:n innan du ställer din fråga. Detta ger kontext som formar tonen, djupet och fokus i svaret. En "software architect" ger annan rådgivning än en "junior developer" eller en "security auditor".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Sätta kontext och persona – samma fråga får olika svar beroende på tilldelad roll*

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

**När man ska använda:** Kodgranskningar, handledning, domänspecifik analys eller när du behöver svar anpassade till en särskild expertisnivå eller perspektiv.

### Promptmallar

Skapa återanvändbara prompts med variabla platshållare. Istället för att skriva en ny prompt varje gång, definiera en mall en gång och fyll i olika värden. LangChain4js `PromptTemplate`-klass gör detta enkelt med `{{variable}}`-syntakt.

<img src="../../../translated_images/sv/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Återanvändbara prompts med variabla platshållare – en mall, många användningar*

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

**När man ska använda:** Upprepade frågor med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller i situationer där promptens struktur förblir densamma men data ändras.

---

Dessa fem grunder ger dig en robust verktygslåda för de flesta prompting-uppgifter. Resten av denna modul bygger vidare på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s resonemangskontroll, självvärdering och strukturerade output-funktioner.

## Avancerade mönster

Med grunderna på plats går vi vidare till de åtta avancerade mönster som gör denna modul unik. Inte alla problem behöver samma tillvägagångssätt. Vissa frågor kräver snabba svar, andra kräver djup eftertanke. Vissa behöver synligt resonemang, andra bara resultat. Varje mönster nedan är optimerat för ett specifikt scenario — och GPT-5.2:s resonemangskontroll förstärker skillnaderna ytterligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Översikt över de åtta prompt engineering-mönstren och deras användningsområden*

GPT-5.2 tillför en dimension till dessa mönster: *resonemangskontroll*. Reglaget nedan visar hur du kan justera modellens tänkande – från snabba, direkta svar till djup, grundlig analys.

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2:s resonemangskontroll låter dig specificera hur mycket modellen ska tänka – från snabba, direkta svar till djupgående utforskning*

**Låg iver (Snabb & Fokuserad)** - För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimalt resonemang - max 2 steg. Använd detta för beräkningar, uppslag eller enkla frågor.

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

> 💡 **Utforska med GitHub Copilot:** Öppna [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) och fråga:
> - "Vad är skillnaden mellan låga och höga iver-promptmönster?"
> - "Hur hjälper XML-taggarna i prompts att strukturera AI:ns svar?"
> - "När ska jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög iver (Djup & Grundlig)** - För komplexa problem där du vill ha en omfattande analys. Modellen undersöker noggrant och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturval eller komplex forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Uppgiftshantering (Steg-för-steg-framsteg)** - För arbetsflöden med flera steg. Modellen presenterar en plan i förväg, berättar om varje steg medan den arbetar, och avslutar med en sammanfattning. Använd detta för migrationer, implementeringar eller andra flerstegsprocesser.

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

Chain-of-Thought prompting ber uttryckligen modellen att visa sitt resonemang, vilket förbättrar noggrannheten vid komplexa uppgifter. Steg-för-steg-nedbrytningen hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:
> - "Hur skulle jag anpassa uppgiftshanteringsmönstret för långvariga operationer?"
> - "Vilka är bästa praxis för att strukturera verktygspreambler i produktionsapplikationer?"
> - "Hur kan jag fånga och visa uppdateringar om mellanliggande framsteg i ett UI?"

Diagrammet nedan illustrerar detta Planera → Utföra → Sammanfatta arbetsflöde.

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Arbetsflöde Planera → Utföra → Sammanfatta för flerstegsuppgifter*

**Självreflekterande kod** - För att generera produktionskvalitativ kod. Modellen genererar kod enligt produktionsstandarder med korrekt felhantering. Använd detta när du bygger nya funktioner eller tjänster.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Diagrammet nedan visar denna iterativa förbättringscykel – generera, utvärdera, identifiera svagheter och förbättra tills koden uppfyller produktionsstandarder.

<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ förbättringscykel – generera, utvärdera, upptäcka problem, förbättra, upprepa*

**Strukturerad analys** - För konsekvent utvärdering. Modellen granskar kod med ett fast ramverk (korrekthet, praxis, prestanda, säkerhet, underhållbarhet). Använd detta för kodgranskningar eller kvalitetsbedömningar.

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

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om strukturerad analys:
> - "Hur kan jag anpassa analysramverket för olika typer av kodgranskningar?"
> - "Vad är bästa sättet att programmässigt tolka och agera på strukturerad output?"
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer mellan olika granskningssessioner?"

Diagrammet nedan visar hur detta strukturerade ramverk organiserar en kodgranskning i konsekventa kategorier med allvarlighetsnivåer.

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Ramverk för konsekventa kodgranskningar med allvarlighetsnivåer*

**Flerstegs-chatt** - För konversationer som behöver kontext. Modellen kommer ihåg tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplexa frågor och svar.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Diagrammet nedan visualiserar hur kontexten i samtalet ackumuleras med varje tur och hur det förhåller sig till modellens tokenbegränsning.

<img src="../../../translated_images/sv/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hur samtalskontext ackumuleras över flera turer tills tokenbegränsningen nås*
**Steg-för-steg-resonemang** – För problem som kräver synlig logik. Modellen visar tydlig resonemang för varje steg. Använd detta för matematiska problem, logikpussel eller när du behöver förstå tankegången.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagrammet nedan illustrerar hur modellen delar upp problem i explicita, numrerade logiska steg.

<img src="../../../translated_images/sv/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Att bryta ned problem i explicita logiska steg*

**Begränsad Output** – För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt outputstruktur.

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

Följande diagram visar hur begränsningar styr modellen att producera output som strikt följer dina format- och längdkrav.

<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Att tillämpa specifika format-, längd- och strukturkrav*

## Kör applikationen

**Verifiera distribution:**

Säkerställ att filen `.env` finns i rotkatalogen med Azure-behörigheter (skapades under Modul 01). Kör detta från modulkatalogen (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startade alla applikationer med `./start-all.sh` från rotkatalogen (som beskrivs i Modul 01) kör denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet till vänster i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stoppa applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationens status

Klicka bara på play-knappen bredvid "prompt-engineering" för att starta denna modul, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — starta, stoppa och övervaka alla moduler från en plats*

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotkatalogen
.\start-all.ps1
```

Eller starta enbart denna modul:

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

Båda skripten laddar automatiskt miljövariabler från roten `.env`-filen och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att manuellt bygga alla moduler innan start:
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

Öppna http://localhost:8083 i din webbläsare.

**För att stoppa:**

**Bash:**
```bash
./stop.sh  # Endast denna modul
# Eller
cd .. && ./stop-all.sh  # Alla moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Endast denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Applikationsskärmdumpar

Här är huvudgränssnittet för prompt engineering-modulen, där du kan experimentera med alla åtta mönster sida vid sida.

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvuddashboard som visar alla 8 prompt engineering-mönster med deras egenskaper och användningsområden*

## Utforska mönstren

Webbgränssnittet låter dig experimentera med olika promptingstrategier. Varje mönster löser olika problem – prova dem för att se när varje metod passar bäst.

> **Notera: Streaming vs Icke-streaming** — Varje mönstersida erbjuder två knappar: **🔴 Stream Response (Live)** och ett **Icke-streaming** alternativ. Streaming använder Server-Sent Events (SSE) för att visa tokens i realtid medan modellen genererar dem, så du ser framsteg direkt. Icke-streaming väntar på hela svaret innan det visas. För prompts som triggar djup resonemang (t.ex. High Eagerness, Self-Reflecting Code) kan icke-streaming-anrop ta väldigt lång tid — ibland flera minuter — utan synlig återkoppling. **Använd streaming när du experimenterar med komplexa prompts** så kan du se modellen arbeta och undvika intrycket att förfrågan har hängt sig.
>
> **Notera: Webbläsarkrav** — Streamingfunktionen använder Fetch Streams API (`response.body.getReader()`) som kräver en fullständig webbläsare (Chrome, Edge, Firefox, Safari). Det fungerar **inte** i VS Codes inbyggda Simple Browser, eftersom dess webvy inte stödjer ReadableStream API. Om du använder Simple Browser fungerar icke-streaming-knapparna normalt — det är bara streaming-knapparna som påverkas. Öppna `http://localhost:8083` i en extern webbläsare för full upplevelse.

### Låg vs Hög Eagerness

Ställ en enkel fråga som "Vad är 15% av 200?" med låg eagerness. Du får ett omedelbart, direkt svar. Ställ sedan en komplex fråga som "Designa en caching-strategi för ett högtrafikerat API" med hög eagerness. Klicka på **🔴 Stream Response (Live)** och se modellens detaljerade resonemang dyka upp token för token. Samma modell, samma frågestruktur – men prompten berättar hur mycket tänkande som ska göras.

### Uppgiftsutförande (Verktygs-Preamblar)

Flerstegsrutiner vinner på att planera och berätta om framsteg i förväg. Modellen beskriver vad den kommer att göra, kommenterar varje steg och sammanfattar sedan resultaten.

### Självreflekterande kod

Prova "Skapa en e-postvalideringstjänst". Istället för att bara generera kod och sluta, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du ser den iterera tills koden uppfyller produktionsstandard.

### Strukturerad analys

Kodgranskningar behöver konsekventa utvärderingsramverk. Modellen analyserar kod med fasta kategorier (riktighet, praxis, prestanda, säkerhet) med allvarlighetsnivåer.

### Flerstegs-chatt

Fråga "Vad är Spring Boot?" och följ sedan omedelbart upp med "Visa mig ett exempel". Modellen minns din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne hade den andra frågan varit för vag.

### Steg-för-steg-resonemang

Välj ett matteproblem och prova det både med Steg-för-steg-resonemang och Låg Eagerness. Låg eagerness ger bara svaret – snabbt men ointuitivt. Steg-för-steg visar varje beräkning och beslut.

### Begränsad output

När du behöver specifika format eller antal ord, ser detta mönster till att formatet följs strikt. Prova att generera en sammanfattning med exakt 100 ord i punktform.

## Vad du verkligen lär dig

**Resonerande ansträngning förändrar allt**

GPT-5.2 låter dig styra beräkningsinsatsen genom dina prompts. Låg ansträngning betyder snabba svar med minimal utforskning. Hög ansträngning betyder att modellen tar tid att tänka djupt. Du lär dig att matcha ansträngning efter uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte komplexa beslut.

**Struktur styr beteende**

Lägg märke till XML-taggarna i promptarna? De är inte dekorativa. Modeller följer strukturerade instruktioner mer pålitligt än fri form text. När du behöver flerstegsprocesser eller komplex logik hjälper strukturen modellen att hålla reda på var den är och vad som kommer härnäst. Diagrammet nedan bryter ner en välstrukturerad prompt och visar hur taggar som `<system>`, `<instructions>`, `<context>`, `<user-input>`, och `<constraints>` organiserar dina instruktioner i tydliga sektioner.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-liknande organisation*

**Kvalitet genom självvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas att modellen "gör rätt", berättar du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan värdera sin egen output och förbättra sig. Detta förvandlar kodgenerering från ett lotteri till en process.

**Kontext är ändlig**

Flerstegs-konversationer fungerar genom att inkludera meddelandehistorik med varje förfrågan. Men det finns en gräns — varje modell har ett maximalt tokenantal. När konversationer växer behöver du strategier för att behålla relevant kontext utan att nå taket. Denna modul visar hur minnet fungerar; senare lär du dig när du ska sammanfatta, när du ska glömma och när du ska hämta.

## Nästa steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigering:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till huvud](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål ska anses vara den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för missförstånd eller feltolkningar som uppstår från användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->