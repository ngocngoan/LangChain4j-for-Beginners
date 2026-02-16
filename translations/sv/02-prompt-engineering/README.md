# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förkunskaper](../../../02-prompt-engineering)
- [Förståelse för Prompt Engineering](../../../02-prompt-engineering)
- [Grundläggande Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Rollbaserad Prompting](../../../02-prompt-engineering)
  - [Promptmallar](../../../02-prompt-engineering)
- [Avancerade mönster](../../../02-prompt-engineering)
- [Använda befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmbilder](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs hög iver](../../../02-prompt-engineering)
  - [Uppgiftsutförande (Verktygsinledningar)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flerstegs-chatt](../../../02-prompt-engineering)
  - [Steg-för-steg-resonemang](../../../02-prompt-engineering)
  - [Begränsad utdata](../../../02-prompt-engineering)
- [Vad du verkligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Vad du kommer att lära dig

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

I föregående modul såg du hur minne möjliggör konversations-AI och använde GitHub-modeller för grundläggande interaktioner. Nu fokuserar vi på hur du ställer frågor – själva promptarna – med Azure OpenAI:s GPT-5.2. Hur du strukturerar dina prompts påverkar dramatiskt kvaliteten på svaren du får. Vi börjar med en genomgång av grundläggande prompttekniker, och går sedan vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapaciteter fullt ut.

Vi använder GPT-5.2 eftersom modellen introducerar resonemangskontroll – du kan tala om för modellen hur mycket tänkande den ska göra innan svar. Det gör olika prompting-strategier tydligare och hjälper dig förstå när du ska använda varje metod. Vi drar också nytta av Azures färre hastighetsbegränsningar för GPT-5.2 jämfört med GitHub-modeller.

## Förkunskaper

- Slutförd Modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Obs:** Om du inte har slutfört Modul 01, följ installationsinstruktionerna där först.

## Förståelse för Prompt Engineering

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering handlar om att designa indata som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor – det handlar om att strukturera förfrågningar så att modellen förstår exakt vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fix the bug" är otydligt. "Fix the null pointer exception i UserService.java rad 45 genom att lägga till en nullkontroll" är specifikt. Språkmodeller fungerar på samma sätt – specifika detaljer och struktur är viktiga.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j tillhandahåller infrastrukturen – modellkopplingar, minne och meddelandetyper – medan promptmönster är bara noggrant strukturerad text du skickar genom infrastrukturen. De centrala byggstenarna är `SystemMessage` (som sätter AI:ns beteende och roll) och `UserMessage` (som innehåller din faktiska förfrågan).

## Grundläggande Prompt Engineering

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Innan vi dyker in i de avancerade mönstren i denna modul, låt oss gå igenom fem grundläggande prompting-tekniker. Dessa är byggstenarna som varje promptingenjör bör känna till. Om du redan har gått igenom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns) har du sett dem i praktiken – här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Den enklaste metoden: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Det fungerar bra för enkla förfrågningar där förväntat beteende är uppenbart.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel – modellen härleder uppgiften från instruktionen ensam*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```
  
**När du ska använda:** Enkla klassificeringar, direkta frågor, översättningar eller andra uppgifter som modellen kan hantera utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar vilket mönster du vill att modellen ska följa. Modellen lär sig förväntat indata-utdataformat från dina exempel och applicerar det på nya indatat. Detta förbättrar dramatiskt konsekvensen för uppgifter där önskat format eller beteende inte är uppenbart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lärande från exempel – modellen identifierar mönstret och tillämpar det på ny indatat*

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
  
**När du ska använda:** Anpassade klassificeringar, konsekvent formatering, domänspecifika uppgifter eller när zero-shot-resultat är inkonsekventa.

### Chain of Thought

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar delar modellen upp problemet och arbetar igenom varje del explicit. Detta ökar noggrannheten på matematik-, logik- och mångstegsresoneringsuppgifter.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg-resonemang – delar komplexa problem i explicita logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```
  
**När du ska använda:** Matematikproblem, logikpussel, felsökning eller andra uppgifter där visat resonemang förbättrar noggrannhet och tillit.

### Rollbaserad Prompting

Sätt en persona eller roll för AI:n innan du ställer din fråga. Detta ger kontext som formar tonen, djupet och fokus i svaret. En "mjukvaruarkitekt" ger andra råd än en "juniorutvecklare" eller "säkerhetsgranskare".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Anger kontext och persona – samma fråga ger olika svar beroende på tilldelad roll*

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
  
**När du ska använda:** Kodgranskningar, handledning, domänspecifika analyser eller när du behöver svar anpassade till viss expertisnivå eller perspektiv.

### Promptmallar

Skapa återanvändbara prompts med variabla platshållare. Istället för att skriva en ny prompt varje gång, definiera en mall en gång och fyll i olika värden. LangChain4j:s `PromptTemplate`-klass gör detta enkelt med `{{variable}}`-syntax.

<img src="../../../translated_images/sv/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Återanvändbara prompts med variabla platshållare – en mall, många användningsområden*

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
  
**När du ska använda:** Upprepade förfrågningar med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller när promptstrukturen är densamma men datan ändras.

---

Dessa fem grundläggande tekniker ger dig en stabil verktygslåda för de flesta prompting-uppgifter. Resten av denna modul bygger vidare på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s resonemangskontroll, självvärdering och strukturerade utdata.

## Avancerade mönster

När grunderna är täckta går vi vidare till de åtta avancerade mönster som gör denna modul unik. Inte alla problem behöver samma tillvägagångssätt. Vissa frågor behöver snabba svar, andra djupgående tankar. Några behöver synligt resonemang, andra bara resultat. Varje mönster nedan är optimerat för ett annat scenario – och GPT-5.2:s resonemangskontroll gör skillnaderna ännu tydligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Översikt av de åtta prompt engineering-mönstren och deras användningsfall*

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2:s resonemangskontroll låter dig ange hur mycket tänkande modellen ska göra – från snabba direkta svar till djup utforskning*

<img src="../../../translated_images/sv/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Låg iver (snabb, direkt) vs hög iver (grundlig, utforskande) resonemangstillvägagångssätt*

**Låg iver (Snabb & Fokuserad)** – För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimalt med resonemang – maximalt 2 steg. Använd det för beräkningar, uppslagningar eller enkla frågor.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Utforska med GitHub Copilot:** Öppna [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) och fråga:  
> - "Vad är skillnaden mellan låg iver och hög iver i prompting-mönster?"  
> - "Hur hjälper XML-taggar i prompts att strukturera AI:s svar?"  
> - "När bör jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög iver (Djup & Grundlig)** – För komplexa problem där du vill ha omfattande analys. Modellen utforskar noggrant och visar detaljerat resonemang. Använd det för systemdesign, arkitekturval eller komplex forskning.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Uppgiftsutförande (Steg-för-steg-framsteg)** – För mångstegsarbetsflöden. Modellen ger en upfront-plan, berättar varje steg under arbetet och ger sedan en sammanfattning. Använd det för migrationer, implementationer eller andra mångstegsprocesser.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Chain-of-Thought-prompting ber uttryckligen modellen visa sitt resonemang, vilket förbättrar noggrannheten i komplexa uppgifter. Steg-för-steg-analysen hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:  
> - "Hur skulle jag anpassa uppgiftsutförandemönstret för långvariga operationer?"  
> - "Vilka är bästa praxis för att strukturera verktygsinledningar i produktionsapplikationer?"  
> - "Hur kan jag fånga och visa mellanliggande framsteg i ett UI?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planera → Utför → Sammanfatta arbetsflöde för mångstegsuppgifter*

**Självreflekterande kod** – För att generera produktionskvalitativ kod. Modellen genererar kod, granskar den mot kvalitetskriterier och förbättrar den iterativt. Använd detta när du bygger nya funktioner eller tjänster.

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
  
<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ förbättringscykel – generera, utvärdera, identifiera problem, förbättra, upprepa*

**Strukturerad analys** – För konsekvent utvärdering. Modellen granskar kod med ett fast ramverk (korrekthet, praxis, prestanda, säkerhet). Använd för kodgranskningar eller kvalitetsbedömningar.

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
  
> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om strukturerad analys:  
> - "Hur kan jag anpassa analysramverket för olika typer av kodgranskningar?"  
> - "Vad är bästa sättet att programatiskt tolka och agera på strukturerad utdata?"  
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer över olika granskningssessioner?"

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Fyra-kategorier-ramverk för konsekventa kodgranskningar med allvarlighetsnivåer*

**Flerstegs-chatt** – För konversationer som behöver kontext. Modellen kommer ihåg tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplex Q&A.

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

*Hur samtalskontext ackumuleras över flera varv tills token-gränsen nås*

**Steg-för-steg-resonemang** – För problem som kräver synlig logik. Modellen visar explicit resonemang för varje steg. Använd det för matematikproblem, logikpussel eller när du behöver förstå tankegången.

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

*Bryter ner problem i explicita logiska steg*

**Begränsad utdata** – För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver precis utdata-struktur.

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

*Tillämpning av specifika format-, längd- och strukturkrav*

## Använda befintliga Azure-resurser

**Verifiera distribution:**

Se till att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapad under Modul 01):  
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Starta applikationen:**

> **Obs:** Om du redan startat alla applikationer med `./start-all.sh` från Modul 01, kör denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet till vänster i VS Code (sök efter Spring Boot-ikonen).
Från Spring Boot-instrumentpanelen kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stanna applikationer med en enda klickning
- Visa applikationsloggar i realtid
- Övervaka applikationens status

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

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filerna om de inte finns.

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

## Skärmbilder från applikationen

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvudinstrumentpanelen som visar alla 8 prompt engineering-mönster med deras egenskaper och användningsområden*

## Utforska mönstren

Webbgränssnittet låter dig experimentera med olika promptningsstrategier. Varje mönster löser olika problem – prova dem för att se när varje metod lyser.

### Låg vs Hög Iver

Ställ en enkel fråga som "Vad är 15 % av 200?" med låg iver. Du får ett direkt och omedelbart svar. Ställ nu något komplext som "Designa en caching-strategi för en API med hög trafik" med hög iver. Se hur modellen saktar ner och ger en detaljerad motivering. Samma modell, samma frågestruktur – men prompten talar om för den hur mycket tänkande som krävs.

<img src="../../../translated_images/sv/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snabb beräkning med minimal motivering*

<img src="../../../translated_images/sv/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattande caching-strategi (2,8MB)*

### Uppgiftsutförande (Verktygsintroduktioner)

Flerstegsarbetsflöden gynnas av förhandsplanering och löpande berättande. Modellen beskriver vad den kommer att göra, berättar om varje steg och sammanfattar sedan resultaten.

<img src="../../../translated_images/sv/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Skapar en REST-endpoint med steg-för-steg-berättande (3,9MB)*

### Självreflekterande kod

Prova "Skapa en e-postvalideringstjänst". Istället för att bara generera kod och stanna, genererar modellen, utvärderar utifrån kvalitetskriterier, identifierar svagheter och förbättrar. Du ser den iterera tills koden uppfyller produktionsstandarder.

<img src="../../../translated_images/sv/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fullständig e-postvalideringstjänst (5,2MB)*

### Strukturerad analys

Kodgranskningar behöver konsekventa utvärderingsramverk. Modellen analyserar koden med fasta kategorier (korrekthet, praxis, prestanda, säkerhet) och allvarlighetsnivåer.

<img src="../../../translated_images/sv/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ramverksbaserad kodgranskning*

### Flerstegs-chatt

Fråga "Vad är Spring Boot?" och följ direkt upp med "Visa mig ett exempel". Modellen minns din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne skulle den andra frågan vara för vag.

<img src="../../../translated_images/sv/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Behållen kontext över frågor*

### Steg-för-steg-motivering

Välj ett mattetal och prova både Steg-för-steg-motivering och Låg iver. Låg iver ger bara svaret – snabbt men otydligt. Steg-för-steg visar varje beräkning och beslut.

<img src="../../../translated_images/sv/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Mattetal med explicita steg*

### Begränsad output

När du behöver specifika format eller ordantal, tvingar detta mönster strikt efterlevnad. Prova att generera en sammanfattning med exakt 100 ord i punktform.

<img src="../../../translated_images/sv/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskininlärningssammanfattning med formatkontroll*

## Vad du verkligen lär dig

**Motiveringsinsats ändrar allt**

GPT-5.2 låter dig styra den beräkningsinsats som görs genom dina prompts. Låg insats betyder snabba svar med minimal utforskning. Hög insats betyder att modellen tar tid på sig att tänka djupt. Du lär dig att anpassa insatsen efter uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte heller på komplexa beslut.

**Struktur styr beteendet**

Ser du XML-taggarna i prompts? De är inte dekorativa. Modeller följer strukturerade instruktioner mer pålitligt än fri text. När du behöver flerstegsprocesser eller komplex logik hjälper struktur modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-stil organisation*

**Kvalitet genom självutvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas att modellen "gör rätt", säger du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen output och förbättras. Detta förvandlar kodgenerering från ett lotteri till en process.

**Kontext är begränsad**

Flerstegs-konversationer fungerar genom att inkludera meddelandehistorik med varje förfrågan. Men det finns en gräns – varje modell har ett maximalt antal tokens. När konversationer växer behöver du strategier för att behålla relevant kontext utan att nå taket. Denna modul visar hur minne fungerar; senare lär du dig när du ska sammanfatta, när du ska glömma och när du ska hämta.

## Nästa steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigering:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till huvudsida](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Friskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Vi strävar efter noggrannhet, men var medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för missförstånd eller feltolkningar som kan uppstå vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->