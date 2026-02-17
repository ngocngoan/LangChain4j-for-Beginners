# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förutsättningar](../../../02-prompt-engineering)
- [Förstå prompt engineering](../../../02-prompt-engineering)
- [Grundläggande prompt engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollbaserad promptning](../../../02-prompt-engineering)
  - [Promptmallar](../../../02-prompt-engineering)
- [Avancerade mönster](../../../02-prompt-engineering)
- [Använda befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmbilder](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs hög iver](../../../02-prompt-engineering)
  - [Uppgiftsutförande (verktygspreambler)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flerstegs-chatt](../../../02-prompt-engineering)
  - [Steg-för-steg-resonemang](../../../02-prompt-engineering)
  - [Begränsad utdata](../../../02-prompt-engineering)
- [Vad du verkligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Vad du kommer att lära dig

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I föregående modul såg du hur minne möjliggör konverserande AI och använde GitHub Models för grundläggande interaktioner. Nu fokuserar vi på hur du ställer frågor — själva promptarna — med hjälp av Azure OpenAI:s GPT-5.2. Sättet du strukturerar dina prompts på påverkar dramatiskt kvaliteten på de svar du får. Vi börjar med en genomgång av grundläggande prompttekniker och går sedan vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapabiliteter fullt ut.

Vi använder GPT-5.2 eftersom det introducerar styrning av resonemang - du kan tala om för modellen hur mycket tänkande den ska göra innan den svarar. Detta gör olika promptstrategier tydligare och hjälper dig att förstå när varje metod ska användas. Vi drar också nytta av Azures färre hastighetsbegränsningar för GPT-5.2 jämfört med GitHub Models.

## Förutsättningar

- Genomförd Modul 01 (Azure OpenAI-resurser deployade)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Obs:** Om du inte har genomfört Modul 01, följ först instruktionerna där för deployment.

## Förstå prompt engineering

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handlar om att designa indatatext som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor — utan att strukturera förfrågningar så att modellen förstår exakt vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fixera buggen" är vagt. "Fixera null pointer-exception i UserService.java rad 45 genom att lägga till en null-kontroll" är specifikt. Språkmodeller fungerar på samma sätt — specifikhet och struktur är viktigt.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j tillhandahåller infrastrukturen — modellkopplingar, minne och meddelandetyper — medan promptmönster är bara noggrant strukturerad text du skickar genom den infrastrukturen. De viktiga byggstenarna är `SystemMessage` (som sätter AI:ns beteende och roll) och `UserMessage` (som bär din faktiska förfrågan).

## Grundläggande prompt engineering

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Innan vi går in på de avancerade mönstren i denna modul, låt oss granska fem grundläggande prompttekniker. Dessa är byggstenarna som varje promptingenjör bör känna till. Om du redan har arbetat igenom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns), har du sett dem i praktiken — här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Det enklaste tillvägagångssättet: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Detta fungerar bra för enkla förfrågningar där det förväntade beteendet är uppenbart.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel — modellen sluter sig till uppgiften endast från instruktionen*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```
  
**När man ska använda:** Enkla klassificeringar, direkta frågor, översättningar eller någon uppgift modellen kan hantera utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar det mönster du vill att modellen ska följa. Modellen lär sig det förväntade indata- och utdataformatet från dina exempel och applicerar det på nya indata. Detta förbättrar dramatiskt konsistensen för uppgifter där önskat format eller beteende inte är uppenbart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lärande från exempel — modellen identifierar mönstret och applicerar det på nya indata*

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

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar bryter modellen ner problemet och arbetar igenom varje del explicit. Detta förbättrar noggrannhet inom matematik, logik och uppgifter med flera steg i resonemanget.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg-resonemang — bryter ner komplexa problem i explicita logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```
  
**När man ska använda:** Matteproblem, logikpussel, felsökning eller någon uppgift där att visa resonemangsprocessen förbättrar noggrannhet och förtroende.

### Rollbaserad promptning

Sätt en persona eller roll för AI:n innan du ställer din fråga. Detta ger kontext som formar tonen, djupet och fokuset i svaret. En "mjukvaruarkitekt" ger andra råd än en "juniorutvecklare" eller en "säkerhetsrevisor".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ange kontext och persona — samma fråga får olika svar beroende på tilldelad roll*

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

Skapa återanvändbara prompts med variabla platshållare. Istället för att skriva en ny prompt varje gång, definiera en mall en gång och fyll i olika värden. LangChain4js `PromptTemplate`-klass gör detta enkelt med syntaxen `{{variable}}`.

<img src="../../../translated_images/sv/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Återanvändbara prompts med variabla platshållare — en mall, många användningsområden*

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
  
**När man ska använda:** Upprepade frågeställningar med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller i vilket scenario som helst där promptstrukturen förblir lika men datan ändras.

---

Dessa fem grunder ger dig en stabil verktygslåda för de flesta promptuppgifter. Resten av denna modul bygger vidare på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s resonemangskontroll, självutvärdering och strukturerade utdatakapaciteter.

## Avancerade mönster

Med grunderna täckta, låt oss gå vidare till de åtta avancerade mönster som gör denna modul unik. Inte alla problem behöver samma tillvägagångssätt. Vissa frågor behöver snabba svar, andra kräver djupt tänkande. Vissa behöver synligt resonemang, andra vill bara ha resultat. Varje mönster nedan är optimerat för ett annat scenario — och GPT-5.2:s resonemangskontroll gör skillnaderna ännu tydligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Översikt över de åtta prompt engineering-mönstren och deras användningsområden*

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2:s resonemangskontroll låter dig specificera hur mycket tänkande modellen ska göra — från snabba direkta svar till djup utforskning*

**Låg iver (Snabbt & Fokuserat)** - För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimalt med resonemang - maximalt 2 steg. Använd detta för beräkningar, uppslagningar eller enkla frågor.

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
> - "Vad är skillnaden mellan låg och hög iver i prompting-mönster?"  
> - "Hur hjälper XML-taggar i promptar till att strukturera AI:ns svar?"  
> - "När ska jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög iver (Djupt & Grundligt)** - För komplexa problem där du vill ha omfattande analys. Modellen utforskar grundligt och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturbeslut eller komplex forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Uppgiftsutförande (Steg-för-steg-framsteg)** - För arbetsflöden med flera steg. Modellen ger en upfront-plan, berättar varje steg när det pågår och ger sedan en sammanfattning. Använd detta för migreringar, implementationer eller vilken flerstegsprocess som helst.

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
  
Chain-of-Thought-prompting ber explicit modellen att visa sin resonemangsprocess, vilket förbättrar noggrannheten för komplexa uppgifter. Steg-för-steg-nedbrytningen hjälper både människor och AI att förstå logiken.

> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:  
> - "Hur anpassar jag uppgiftsutförande-mönstret för långvariga operationer?"  
> - "Vilka är bästa praxis för att strukturera verktygspreambler i produktionsapplikationer?"  
> - "Hur kan jag fånga och visa mellanliggande framstegsuppdateringar i en UI?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planera → Utföra → Sammanfatta arbetsflöde för flerstegsuppgifter*

**Självreflekterande kod** - För att generera produktionskvalitetskod. Modellen genererar kod enligt produktionsstandarder med korrekt felhantering. Använd detta vid byggande av nya funktioner eller tjänster.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ förbättringscykel - generera, utvärdera, identifiera problem, förbättra, upprepa*

**Strukturerad analys** - För konsekvent utvärdering. Modellen granskar kod enligt ett fast ramverk (riktighet, praxis, prestanda, säkerhet, underhållbarhet). Använd detta för kodgranskning eller kvalitetsbedömning.

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
  
> **🤖 Testa med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om strukturerad analys:  
> - "Hur kan jag anpassa analysramverket för olika typer av kodgranskningar?"  
> - "Vad är bästa sättet att tolka och agera på strukturerad output programmässigt?"  
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer över olika granskningssessioner?"

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
  
<img src="../../../translated_images/sv/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hur kontext i konversation ackumuleras över flera turer tills token-gräns uppnås*

**Steg-för-steg-resonemang** - För problem som kräver synlig logik. Modellen visar explicit resonemang för varje steg. Använd detta för matteproblem, logikpussel eller när du vill förstå tänkandeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sv/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Nedbrytning av problem i explicita logiska steg*

**Begränsad utdata** - För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt utdatastruktur.

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
  
<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Genomförande av specifika format-, längd- och strukturkrav*

## Använda befintliga Azure-resurser

**Verifiera deployment:**

Säkerställ att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapades under Modul 01):
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Starta applikationen:**

> **Observera:** Om du redan har startat alla applikationer med `./start-all.sh` från Modul 01, körs denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet på vänster sida i VS Code (letar efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stopp applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationens status
Klicka helt enkelt på play-knappen bredvid "prompt-engineering" för att starta detta modul, eller starta alla moduler samtidigt.

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

Båda skripten laddar automatiskt miljövariabler från root `.env` filen och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan start:
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
./stop.sh  # Denna modul endast
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

*Huvudpanelen visar alla 8 prompt-engineering-mönster med deras egenskaper och användningsfall*

## Utforska Mönstren

Webbgränssnittet låter dig experimentera med olika prompting-strategier. Varje mönster löser olika problem – prova dem för att se när varje tillvägagångssätt fungerar bäst.

### Låg vs Hög Entusiasm

Ställ en enkel fråga som "Vad är 15 % av 200?" med låg entusiasm. Du får ett omedelbart och direkt svar. Fråga sedan något komplext som "Designa en cache-strategi för ett högtrafikerat API" med hög entusiasm. Se hur modellen saktar ner och ger detaljerade resonemang. Samma modell, samma frågestruktur – men prompten berättar hur mycket eftertanke som ska göras.

<img src="../../../translated_images/sv/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snabb beräkning med minimal eftertanke*

<img src="../../../translated_images/sv/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattande cache-strategi (2.8MB)*

### Uppgiftsutförande (Verktygspreambler)

Flerstegsarbetsflöden gynnas av förhandsplanering och löpande berättande. Modellen beskriver vad den ska göra, berättar varje steg och sammanfattar sedan resultatet.

<img src="../../../translated_images/sv/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Skapa en REST-endpoint med steg-för-steg-berättande (3.9MB)*

### Självreflekterande Kod

Prova "Skapa en e-postvalideringstjänst". Istället för att endast generera kod och stoppa, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du ser hur den itererar tills koden uppfyller produktionskrav.

<img src="../../../translated_images/sv/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Komplett e-postvalideringstjänst (5.2MB)*

### Strukturerad Analys

Kodgranskningar behöver konsekventa utvärderingsramverk. Modellen analyserar koden med fasta kategorier (korrekthet, praxis, prestanda, säkerhet) med allvarlighetsnivåer.

<img src="../../../translated_images/sv/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ramverksbaserad kodgranskning*

### Flerskiftschatt

Fråga "Vad är Spring Boot?" och följ upp direkt med "Visa mig ett exempel". Modellen kommer ihåg din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne skulle andra frågan vara för vag.

<img src="../../../translated_images/sv/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Kontextbevarande över frågor*

### Steg-för-steg-resonemang

Välj ett mattetal och prova det med både Steg-för-steg-resonemang och Låg entusiasm. Låg entusiasm ger dig bara svaret – snabbt men otydligt. Steg-för-steg visar varje beräkning och beslut.

<img src="../../../translated_images/sv/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Mattetal med tydliga steg*

### Begränsad Output

När du behöver specifika format eller ordantal, tvingar detta mönster strikt efterlevnad. Prova att generera en sammanfattning med exakt 100 ord i punktlista.

<img src="../../../translated_images/sv/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Sammanfattning om maskininlärning med formatkontroll*

## Vad Du Verkligen Lär Dig

**Ansträngning i resonemang förändrar allt**

GPT-5.2 låter dig kontrollera beräkningsinsatsen via dina prompts. Låg insats ger snabba svar med minimal utforskning. Hög insats innebär att modellen tar tid att tänka djupt. Du lär dig anpassa insatsen efter uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte heller på komplexa beslut.

**Struktur styr beteende**

Lägger du märke till XML-taggarna i promptarna? De är inte dekorativa. Modeller följer strukturerade instruktioner mer pålitligt än fri text. När du behöver flerstegsprocesser eller komplex logik, hjälper struktur modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-stil organisation*

**Kvalitet genom självvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas att modellen "gör rätt", säger du exakt vad "rätt" innebär: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen output och förbättra. Detta förvandlar kodgenerering från lotteri till process.

**Kontext är ändlig**

Flerskiftskonversationer fungerar genom att inkludera meddelandehistorik med varje begäran. Men det finns en gräns – varje modell har ett max antal tokens. När konversationer växer behöver du strategier för att behålla relevant kontext utan att nå taket. Denna modul visar hur minne fungerar; senare lär du dig när bort sammanfatta, när glömma och när hämta.

## Nästa Steg

**Nästa Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigering:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till Start](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess originalspråk ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->