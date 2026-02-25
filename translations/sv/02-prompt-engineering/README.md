# Module 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förkunskaper](../../../02-prompt-engineering)
- [Förstå prompt engineering](../../../02-prompt-engineering)
- [Grundläggande om prompt engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollbaserad prompting](../../../02-prompt-engineering)
  - [Promptmallar](../../../02-prompt-engineering)
- [Avancerade mönster](../../../02-prompt-engineering)
- [Använda befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmbilder](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs hög iver](../../../02-prompt-engineering)
  - [Uppgiftsutförande (verktygspreambler)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flera rundor chatt](../../../02-prompt-engineering)
  - [Steg-för-steg resonemang](../../../02-prompt-engineering)
  - [Begränsat utdata](../../../02-prompt-engineering)
- [Vad du egentligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Vad du kommer att lära dig

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="Vad du kommer att lära dig" width="800"/>

I föregående modul såg du hur minne möjliggör konversations-AI och använde GitHub-modeller för grundläggande interaktioner. Nu fokuserar vi på hur du ställer frågor — själva promptarna — med Azure OpenAI:s GPT-5.2. Sättet du strukturerar dina promptar påverkar dramatiskt kvaliteten på svaren du får. Vi börjar med en genomgång av grundläggande prompttekniker och går sedan vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapaciteter fullt ut.

Vi använder GPT-5.2 eftersom den introducerar styrning av resonemang – du kan berätta för modellen hur mycket tänkande den ska göra innan den svarar. Det gör olika promptstrategier tydligare och hjälper dig att förstå när du ska använda varje tillvägagångssätt. Vi drar också nytta av Azures färre hastighetsbegränsningar för GPT-5.2 jämfört med GitHub-modeller.

## Förkunskaper

- Genomförd modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i modul 01)

> **Notera:** Om du inte har slutfört modul 01, följ först distributionsinstruktionerna där.

## Förstå prompt engineering

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Vad är prompt engineering?" width="800"/>

Prompt engineering handlar om att designa inmatningstext som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor – utan om att strukturera förfrågningar så att modellen förstår exakt vad du vill och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fixa buggen" är otydligt. "Fixa null pointer exception i UserService.java rad 45 genom att lägga till en null-kontroll" är specifikt. Språkmodeller fungerar likadant – specifikhet och struktur spelar roll.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hur LangChain4j passar in" width="800"/>

LangChain4j levererar infrastrukturen — modellkopplingar, minne och meddelandetyper — medan promptmönster är bara noggrant strukturerad text du skickar genom infrastrukturen. De viktigaste byggstenarna är `SystemMessage` (som sätter AI:ns beteende och roll) och `UserMessage` (som bär din faktiska förfrågan).

## Grundläggande om prompt engineering

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Översikt av fem prompt engineering-mönster" width="800"/>

Innan vi dyker in i de avancerade mönstren i denna modul, låt oss gå igenom fem grundläggande prompttekniker. Dessa är byggstenar som varje promptingenjör bör känna till. Om du redan har arbetat igenom [snabbstartmodulen](../00-quick-start/README.md#2-prompt-patterns) har du sett dem i praktiken – här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Den enklaste metoden: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Detta fungerar bra för enkla förfrågningar där det förväntade beteendet är uppenbart.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel — modellen härleder uppgiften enbart från instruktionen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**När det ska användas:** Enkla klassificeringar, direkta frågor, översättningar eller andra uppgifter modellen klarar utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar det mönster du vill att modellen ska följa. Modellen lär sig det förväntade in- och utdataformatet från dina exempel och tillämpar det på nya indata. Detta förbättrar konsekvensen dramatiskt för uppgifter där önskat format eller beteende inte är självklart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lär sig från exempel — modellen identifierar mönstret och tillämpar det på ny indata*

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

**När det ska användas:** Anpassade klassificeringar, konsekvent formatering, domänspecifika uppgifter eller när zero-shot-resultat är inkonsekventa.

### Chain of Thought

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar bryter modellen ner problemet och går igenom varje del uttryckligen. Detta förbättrar noggrannheten i matematik, logik och flerstegsresonemang.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg-resonemang — bryter ned komplexa problem till explicita logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```

**När det ska användas:** Matematikproblem, logikpussel, felsökning eller uppgifter där visat resonemang förbättrar noggrannheten och förtroendet.

### Rollbaserad prompting

Sätt en persona eller roll för AI:n innan du ställer din fråga. Detta ger kontext som formar tonen, djupet och fokus för svaret. En "mjukvaruarkitekt" ger andra råd än en "juniorutvecklare" eller en "säkerhetsrevisor".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollbaserad prompting" width="800"/>

*Anger kontext och persona — samma fråga får olika svar beroende på tilldelad roll*

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

**När det ska användas:** Kodgranskningar, handledning, domänspecifika analyser eller när du vill ha svar anpassade efter en viss expertisnivå eller perspektiv.

### Promptmallar

Skapa återanvändbara promptar med variabla platshållare. Istället för att skriva en ny prompt varje gång, definiera en mall en gång och fyll i olika värden. LangChain4j:s `PromptTemplate`-klass gör detta enkelt med `{{variable}}`-syntax.

<img src="../../../translated_images/sv/prompt-templates.14bfc37d45f1a933.webp" alt="Promptmallar" width="800"/>

*Återanvändbara promptar med variabla platshållare — en mall, många användningar*

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

**När det ska användas:** Upprepade frågor med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller när promptstrukturen förblir densamma men datan varierar.

---

Dessa fem grunder ger dig ett robust verktyg för de flesta prompting-uppgifter. Resten av denna modul bygger på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s styrning av resonemang, självutvärdering och strukturerade utdatakapaciteter.

## Avancerade mönster

Med grunderna täckta går vi vidare till de åtta avancerade mönstren som gör denna modul unik. Inte alla problem kräver samma tillvägagångssätt. Vissa frågor behöver snabba svar, andra behöver djup reflektion. Vissa behöver tydligt resonemang, andra bara resultat. Varje mönster nedan är optimerat för ett särskilt scenario — och GPT-5.2:s styrning av resonemang gör skillnaderna ännu tydligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtta promptingmönster" width="800"/>

*Översikt över de åtta prompt engineering-mönstren och deras användningsområden*

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Styrning av resonemang med GPT-5.2" width="800"/>

*GPT-5.2:s styrning av resonemang låter dig ange hur mycket tänkande modellen ska göra — från snabba direkta svar till djup utforskning*

**Låg iver (snabbt & fokuserat)** - För enkla frågor där du vill ha snabba, direkt svar. Modellen gör minimal resonemang - max 2 steg. Använd detta för beräkningar, uppslagningar eller enkla frågor.

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
> - "Vad är skillnaden mellan låg och hög iver i promptingmönster?"
> - "Hur hjälper XML-taggarna i promptar till att strukturera AI:ns svar?"
> - "När bör jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög iver (djup & noggrann)** - För komplexa problem där du vill ha omfattande analys. Modellen utforskar grundligt och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturbeslut eller komplex forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Uppgiftsutförande (steg-för-steg-framsteg)** - För flerstegsarbetsflöden. Modellen ger en plan i förväg, berättar om varje steg under arbetets gång och ger sedan en sammanfattning. Använd detta för migreringar, implementeringar eller andra flerstegsprocesser.

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

Chain-of-Thought prompting ber modellen uttryckligen visa sitt resonemangsprocess, vilket förbättrar noggrannheten för komplexa uppgifter. Nedbrytningen steg för steg hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:
> - "Hur skulle jag anpassa mönstret för uppgiftsutförande för långkörande operationer?"
> - "Vilka är bästa praxis för att strukturera verktygspreambler i produktionsapplikationer?"
> - "Hur kan jag fånga och visa mellanliggande framstegsuppdateringar i ett användargränssnitt?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Uppgiftsutförandemönster" width="800"/>

*Planera → Utföra → Sammanfatta för flerstegsuppgifter*

**Självreflekterande kod** - För att generera produktionsklar kod. Modellen genererar kod enligt produktionsstandard med korrekt felhantering. Använd detta vid utveckling av nya funktioner eller tjänster.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Självreflektionscykel" width="800"/>

*Iterativ förbättringscykel - generera, utvärdera, identifiera problem, förbättra, upprepa*

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
> - "Vad är bästa sättet att programmässigt tolka och agera på strukturerat utdata?"
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer över olika granskningssessioner?"

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturerad analysmönster" width="800"/>

*Ramverk för konsekventa kodgranskningar med allvarlighetsnivåer*

**Fler-rundors chatt** - För konversationer som behöver kontext. Modellen minns tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplex Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sv/context-memory.dff30ad9fa78832a.webp" alt="Kontextminne" width="800"/>

*Hur kontext i konversationen ackumuleras över flera rundor tills token-gräns nås*

**Steg-för-steg resonemang** - För problem som kräver synlig logik. Modellen visar explicit resonemang för varje steg. Använd detta för matematikproblem, logikpussel eller när du behöver förstå tankeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sv/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Steg-för-steg-mönster" width="800"/>

*Bryta ned problem till explicita logiska steg*

**Begränsat utdata** - För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt outputstruktur.

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

<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Begränsat utdatmönster" width="800"/>

*Tvingar fram specifika format-, längd- och strukturkrav*

## Använda befintliga Azure-resurser

**Verifiera distribution:**

Se till att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapad under modul 01):
```bash
cat ../.env  # Ska visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startat alla applikationer med `./start-all.sh` från modul 01, körs denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använda Spring Boot Dashboard (rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet på vänster sida i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stoppa applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationstillstånd
Klicka helt enkelt på spela-knappen bredvid "prompt-engineering" för att starta denna modul, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

Eller starta bara denna modul:

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

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan du startar:
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

## Applikationsskärmbilder

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvuddashboard som visar alla 8 prompt engineering-mönster med deras egenskaper och användningsfall*

## Utforska mönstren

Webbgränssnittet låter dig experimentera med olika prompting-strategier. Varje mönster löser olika problem – prova dem för att se när varje tillvägagångssätt fungerar bäst.

> **Notera: Streaming vs Icke-streaming** — Varje mönstersida erbjuder två knappar: **🔴 Stream Response (Live)** och ett **icke-streaming** alternativ. Streaming använder Server-Sent Events (SSE) för att visa token i realtid när modellen genererar dem, så du ser framsteg direkt. Icke-streaming alternativet väntar på hela svaret innan det visas. För prompts som kräver djupare resonemang (t.ex. High Eagerness, Self-Reflecting Code), kan icke-streaming-anropet ta mycket lång tid – ibland minuter – utan synlig feedback. **Använd streaming när du experimenterar med komplexa prompts** så att du kan se modellen arbeta och undvika intrycket att förfrågan har tidsgränsats.
>
> **Notera: Webbläsarkrav** — Streaming-funktionen använder Fetch Streams API (`response.body.getReader()`) vilket kräver en fullständig webbläsare (Chrome, Edge, Firefox, Safari). Det fungerar **inte** i VS Code:s inbyggda Simple Browser, då dess webview inte stödjer ReadableStream API. Om du använder Simple Browser fungerar icke-streaming-knapparna normalt — endast streaming-knapparna påverkas. Öppna `http://localhost:8083` i en extern webbläsare för full upplevelse.

### Low vs High Eagerness

Ställ en enkel fråga som "Vad är 15 % av 200?" med Low Eagerness. Du får ett omedelbart, direkt svar. Ställ nu något komplext som "Designa en caching-strategi för ett högtrafikerat API" med High Eagerness. Klicka på **🔴 Stream Response (Live)** och se modellens detaljerade resonemang visas token för token. Samma modell, samma frågestruktur – men prompten säger hur mycket tänkande som ska göras.

### Uppgiftsutförande (Verktygspreambler)

Flerstegsarbetsflöden gynnas av förhandsplanering och framstegsberättande. Modellen skisserar vad den ska göra, berättar om varje steg och sammanfattar resultat.

### Självreflekterande kod

Prova "Skapa en e-postvalideringstjänst". Istället för att bara generera kod och sluta, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du ser den iterera tills koden uppfyller produktionsstandard.

### Strukturerad analys

Kodgranskningar behöver konsekventa utvärderingsramverk. Modellen analyserar kod med fasta kategorier (riktighet, praxis, prestanda, säkerhet) med allvarlighetsnivåer.

### Flerstegs-chatt

Fråga "Vad är Spring Boot?" och följ sedan upp med "Visa ett exempel". Modellen minns din första fråga och ger dig ett Spring Boot-exempel specifikt. Utan minne skulle den andra frågan vara för vag.

### Steg-för-steg-resonemang

Välj ett matematiskt problem och prova det med både Steg-för-steg-resonemang och Low Eagerness. Low Eagerness ger bara svaret – snabbt men otydligt. Steg-för-steg visar varje beräkning och beslut.

### Begränsad output

När du behöver specifika format eller ordantal, tvingar detta mönster strikt efterlevnad. Prova att generera en sammanfattning med exakt 100 ord i punktform.

## Vad du verkligen lär dig

**Resoneringsinsats förändrar allt**

GPT-5.2 låter dig styra beräkningsinsats genom dina prompts. Låg insats betyder snabba svar med minimal utforskning. Hög insats innebär att modellen tar tid att tänka djupt. Du lär dig matcha insats till uppgiftskomplexitet – slösa inte tid på enkla frågor, men stressa inte heller komplexa beslut.

**Struktur styr beteende**

Lägg märke till XML-taggarna i promptarna? De är inte dekorativa. Modeller följer strukturerade instruktioner mer pålitligt än fritt formulerad text. När du behöver flerstegsprocesser eller komplex logik hjälper struktur modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-stil organisation*

**Kvalitet genom självutvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas att modellen "gör rätt" säger du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen output och förbättra. Detta förvandlar kodgenerering från lotteri till en process.

**Kontext är ändlig**

Flerstegs-konversationer fungerar genom att inkludera meddelandehistorik i varje förfrågan. Men det finns en gräns – varje modell har ett max antal token. När konversationer växer behöver du strategier för att behålla relevant kontext utan att nå taket. Denna modul visar dig hur minne fungerar; senare lär du dig när man ska sammanfatta, när man ska glömma och när man ska hämta.

## Nästa steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till start](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Originaldokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->