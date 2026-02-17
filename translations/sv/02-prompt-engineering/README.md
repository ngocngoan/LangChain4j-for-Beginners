# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förutsättningar](../../../02-prompt-engineering)
- [Förståelse för Prompt Engineering](../../../02-prompt-engineering)
- [Grunderna i Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollbaserad Prompting](../../../02-prompt-engineering)
  - [Promptmallar](../../../02-prompt-engineering)
- [Avancerade mönster](../../../02-prompt-engineering)
- [Använda befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmbilder](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs Hög Engagemang](../../../02-prompt-engineering)
  - [Uppgiftsutförande (Verktygsmallar)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flerstegs-chatt](../../../02-prompt-engineering)
  - [Steg-för-steg resonemang](../../../02-prompt-engineering)
  - [Begränsat output](../../../02-prompt-engineering)
- [Vad du egentligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Vad du kommer att lära dig

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="Vad du kommer att lära dig" width="800"/>

I föregående modul såg du hur minne möjliggör konversations-AI och använde GitHub-modeller för grundläggande interaktioner. Nu fokuserar vi på hur du ställer frågor – själva promptarna – med hjälp av Azure OpenAI:s GPT-5.2. Sättet du strukturerar dina promptar påverkar dramatiskt kvaliteten på svaren du får. Vi börjar med en genomgång av de grundläggande prompting-teknikerna för att sedan gå vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapabiliteter fullt ut.

Vi använder GPT-5.2 eftersom den introducerar styrning av resonemang - du kan tala om för modellen hur mycket tänkande den ska göra innan den svarar. Detta gör olika prompting-strategier mer tydliga och hjälper dig att förstå när du ska använda varje metod. Vi drar också nytta av Azures färre begränsningar för GPT-5.2 jämfört med GitHub-modeller.

## Förutsättningar

- Avslutat Modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotkatalogen med Azure-referenser (skapad av `azd up` i Modul 01)

> **Notera:** Om du inte har avslutat Modul 01, följ först deploymentsinstruktionerna där.

## Förståelse för Prompt Engineering

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Vad är Prompt Engineering?" width="800"/>

Prompt engineering handlar om att designa indata som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor – det handlar om att strukturera förfrågningar så att modellen exakt förstår vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fixera buggen" är oklart. "Fixera null pointer exception i UserService.java rad 45 genom att lägga till en null-check" är specifikt. Språkmodeller fungerar likadant – specifikhet och struktur är viktiga.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hur LangChain4j passar in" width="800"/>

LangChain4j erbjuder infrastrukturen – modellkopplingar, minne och meddelandetyper – medan promptmönster bara är noggrant strukturerad text som skickas genom denna infrastruktur. De viktiga byggstenarna är `SystemMessage` (som bestämmer AI:ns beteende och roll) och `UserMessage` (som bär din faktiska förfrågan).

## Grunderna i Prompt Engineering

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Översikt över fem prompting-mönster" width="800"/>

Innan vi dyker ner i de avancerade mönstren i denna modul, låt oss gå igenom fem grundläggande prompting-tekniker. Dessa är byggstenarna som varje promptingenjör bör känna till. Om du redan har jobbat igenom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns) har du sett dem i aktion – här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Det enklaste tillvägagångssättet: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Detta fungerar bra för enkla förfrågningar där förväntat beteende är uppenbart.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel – modellen härleder uppgiften enbart från instruktionen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**När man ska använda:** Enkla klassificeringar, direkta frågor, översättningar eller någon uppgift modellen klarar utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar det mönster du vill att modellen ska följa. Modellen lär sig det förväntade indata-utdata-formatet från dina exempel och applicerar det på nya indata. Detta förbättrar kraftigt konsistensen för uppgifter där önskat format eller beteende inte är uppenbart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lära från exempel – modellen identifierar mönstret och applicerar det på ny indata*

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

**När man ska använda:** Anpassade klassificeringar, konsekvent formatering, domänspecifika uppgifter eller när zero-shot-resultaten är inkonsekventa.

### Chain of Thought

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar bryter modellen ner problemet och arbetar igenom varje del explicit. Detta förbättrar noggrannheten för matematik, logik och flerstegsresonemang.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg resonemang – bryta ner komplexa problem i explicita logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```

**När man ska använda:** Matteproblem, logikpussel, felsökning eller någon uppgift där visat resonemang förbättrar noggrannhet och förtroende.

### Rollbaserad Prompting

Sätt en persona eller roll för AI:n innan du ställer din fråga. Detta ger en kontext som formar ton, djup och fokus i svaret. En "mjukvaruarkitekt" ger andra råd än en "juniorutvecklare" eller en "säkerhetsrevisor".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rollbaserad Prompting" width="800"/>

*Sätta kontext och persona – samma fråga ger olika svar beroende på tilldelad roll*

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

**När man ska använda:** Kodgranskningar, handledning, domänspecifik analys eller när du behöver svar anpassade till en viss expertisnivå eller perspektiv.

### Promptmallar

Skapa återanvändbara promptar med variabla platshållare. Istället för att skriva en ny prompt varje gång definierar du en mall en gång och fyller i olika värden. LangChain4j:s `PromptTemplate`-klass gör detta enkelt med syntaxen `{{variable}}`.

<img src="../../../translated_images/sv/prompt-templates.14bfc37d45f1a933.webp" alt="Promptmallar" width="800"/>

*Återanvändbara promptar med variabla platshållare – en mall, många användningar*

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

**När man ska använda:** Upprepade förfrågningar med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller när promptstrukturen är samma men datan varierar.

---

Dessa fem grunder ger dig en solid verktygslåda för de flesta prompting-uppgifter. Resten av denna modul bygger vidare på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s kontroll av resonemang, självutvärdering och strukturerade output-funktioner.

## Avancerade mönster

Med grunderna klara går vi vidare till de åtta avancerade mönster som gör denna modul unik. Inte alla problem behöver samma strategi. Vissa frågor kräver snabba svar, andra djup analys. Vissa behöver synligt resonemang, andra bara resultat. Varje mönster nedan är optimerat för ett specifikt scenario – och GPT-5.2:s resonemangsstyrning gör skillnaderna ännu tydligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtta prompting-mönster" width="800"/>

*Översikt över de åtta prompting-mönstren och deras användningsområden*

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Resonemangsstyrning med GPT-5.2" width="800"/>

*GPT-5.2:s resonemangsstyrning låter dig ange hur mycket tänkande modellen ska göra – från snabba direkta svar till djup utforskning*

<img src="../../../translated_images/sv/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Jämförelse av resonemangsinsats" width="800"/>

*Låg entusiasm (snabb, direkt) vs Hög entusiasm (grundlig, utforskande) resonemangsstrategier*

**Låg Entusiasm (Snabb & Fokuserad)** - För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimal resonemang – max 2 steg. Använd detta för beräkningar, uppslagningar eller enkla frågor.

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
> - "Vad är skillnaden mellan låg och hög entusiasm i prompting-mönster?"
> - "Hur hjälper XML-taggarna i promptar att strukturera AI:ns svar?"
> - "När ska jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög Entusiasm (Djup & Grundlig)** - För komplexa problem där du vill ha omfattande analys. Modellen utforskar grundligt och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturval eller komplex forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Uppgiftsutförande (Steg-för-steg framsteg)** - För arbetsflöden med flera steg. Modellen ger en upfront-plan, berättar om varje steg under arbetet och ger sedan en sammanfattning. Använd detta för migreringar, implementationer eller andra flerstegsprocesser.

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

Chain-of-Thought prompting ber explicit modellen visa sitt resonemang, vilket förbättrar noggrannheten för komplicerade uppgifter. Uppdelningen steg för steg hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:
> - "Hur skulle jag anpassa uppgiftsutförandemönstret för långvariga operationer?"
> - "Vilka är bästa praxis för att strukturera verktygsmallar i produktionsapplikationer?"
> - "Hur kan jag fånga och visa mellanliggande framsteg i en UI?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Uppgiftsutförande-mönster" width="800"/>

*Planera → Utföra → Sammanfatta arbetsflöde för flerstegsuppgifter*

**Självreflekterande kod** - För att generera produktionskvalitetskod. Modellen genererar kod enligt produktionsstandard med korrekt felhantering. Använd detta när du bygger nya funktioner eller tjänster.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Självreflektionscykel" width="800"/>

*Iterativ förbättringscykel – generera, utvärdera, identifiera problem, förbättra, upprepa*

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
> - "Vad är bästa sättet att tolka och agera på strukturerad output programmässigt?"
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer mellan olika granskningssessioner?"

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturerad analysmönster" width="800"/>

*Ramverk för konsekvent kodgranskning med allvarlighetsnivåer*

**Flerstegs-chatt** - För samtal som behöver kontext. Modellen minns tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplexa Q&A.

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

*Hur samtalskontext ackumuleras över flera vändor tills tokensgränsen nås*

**Steg-för-steg resonemang** - För problem som kräver synlig logik. Modellen visar explicit resonemang för varje steg. Använd detta för matteproblem, logikpussel eller när du vill förstå tankeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sv/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Steg-för-steg mönster" width="800"/>

*Bryta ner problem i explicita logiska steg*

**Begränsat output** - För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt outputstruktur.

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

<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Begränsat output möster" width="800"/>

*Tvinga fram specifika format-, längd- och strukturella krav*

## Använda befintliga Azure-resurser

**Verifiera distribution:**

Se till att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapad under Modul 01):
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startat alla applikationer via `./start-all.sh` från Modul 01 körs denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderat för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet på vänstra sidan i VS Code (sök efter Spring Boot-ikonen).
Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stanna applikationer med ett enda klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka helt enkelt på play-knappen bredvid "prompt-engineering" för att starta denna modul, eller starta alla moduler på en gång.

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

Båda skripten läser automatiskt in miljövariabler från `.env`-filen i roten och bygger JAR-filerna om de inte finns.

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
.\stop.ps1  # Bara denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Applikationsskärmdumpar

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvuddashboarden som visar alla 8 prompt engineering-mönster med deras karaktäristika och användningsområden*

## Utforska Mönstren

Webbgränssnittet låter dig experimentera med olika prompting-strategier. Varje mönster löser olika problem – prova dem för att se när varje metod passar bäst.

### Låg vs Hög Entusiasm

Ställ en enkel fråga som "Vad är 15 % av 200?" med låg entusiasm. Du får ett omedelbart, direkt svar. Ställ sedan något komplext som "Designa en caching-strategi för ett högtrafikerat API" med hög entusiasm. Se hur modellen saktar ner och ger detaljerade resonemang. Samma modell, samma frågestruktur – men prompten talar om hur mycket eftertanke som ska göras.

<img src="../../../translated_images/sv/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snabb beräkning med minimal eftertanke*

<img src="../../../translated_images/sv/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Utförlig caching-strategi (2.8MB)*

### Utförande av Uppgift (Tool Preambles)

Flerstegsarbetsflöden gynnas av planering i förväg och berättande om framsteg. Modellen beskriver vad den kommer göra, berättar om varje steg och sammanfattar sedan resultaten.

<img src="../../../translated_images/sv/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Skapande av en REST-endpoint med steg-för-steg-berättande (3.9MB)*

### Självreflekterande Kod

Prova "Skapa en tjänst för e-postvalidering". Istället för att bara generera kod och sluta, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du ser hur den itererar tills koden uppfyller produktionsstandard.

<img src="../../../translated_images/sv/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fullständig tjänst för e-postvalidering (5.2MB)*

### Strukturerad Analys

Kodgranskningar kräver konsekventa utvärderingsramverk. Modellen analyserar kod med fasta kategorier (korrekthet, metoder, prestanda, säkerhet) med allvarlighetsnivåer.

<img src="../../../translated_images/sv/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ramverksbaserad kodgranskning*

### Fleromgångssamtal

Fråga "Vad är Spring Boot?" och följ sedan direkt upp med "Visa mig ett exempel". Modellen kommer ihåg din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne skulle den andra frågan vara för vag.

<img src="../../../translated_images/sv/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarande av kontext över frågor*

### Steg-för-Steg Resonemang

Välj ett matteproblem och prova både Steg-för-Steg resonemang och Låg Entusiasm. Låg entusiasm ger dig bara svaret – snabbt men otydligt. Steg-för-steg visar varje beräkning och beslut.

<img src="../../../translated_images/sv/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matteproblem med explicita steg*

### Begränsat Utdata

När du behöver specifika format eller ordantal, tvingar detta mönster strikt efterlevnad. Prova att generera en sammanfattning med exakt 100 ord i punktform.

<img src="../../../translated_images/sv/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Sammanfattning av maskininlärning med formatkontroll*

## Vad Du Egentligen Lär Dig

**Resonemangsinsats Förändrar Allt**

GPT-5.2 låter dig styra beräkningsinsatsen via dina prompts. Låg insats ger snabba svar med minimal utforskning. Hög insats innebär att modellen tar tid att tänka igenom noggrant. Du lär dig anpassa insatsen efter uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte igenom komplicerade beslut heller.

**Struktur Vägledar Beteende**

Lägger du märke till XML-taggarna i promptarna? De är inte dekorativa. Modeller följer strukturerade instruktioner mer tillförlitligt än fri form-text. När du behöver fler steg eller komplex logik, hjälper strukturen modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-liknande organisation*

**Kvalitet Genom Självutvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas modellen "gör rätt", berättar du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen output och förbättras. Detta förvandlar kodgenerering från lotteri till en process.

**Kontekst Är Begränsad**

Fleromgångssamtal fungerar genom att inkludera meddelandehistorik med varje förfrågan. Men det finns en gräns – varje modell har ett max antal tokens. När samtalen växer behöver du strategier för att behålla relevant kontext utan att nå maxgränsen. Den här modulen visar hur minne fungerar; senare lär du dig när du ska sammanfatta, när du ska glömma och när du ska hämta information.

## Nästa Steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till början](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->