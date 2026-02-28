# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Video Genomgång](../../../02-prompt-engineering)
- [Vad Du Kommer Lära Dig](../../../02-prompt-engineering)
- [Förkunskaper](../../../02-prompt-engineering)
- [Förståelse av Prompt Engineering](../../../02-prompt-engineering)
- [Grundläggande Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Avancerade Mönster](../../../02-prompt-engineering)
- [Använda Befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmdumpar](../../../02-prompt-engineering)
- [Utforska Mönstren](../../../02-prompt-engineering)
  - [Låg vs Hög Iver](../../../02-prompt-engineering)
  - [Uppgiftsutförande (Verktygspreambuler)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad Analys](../../../02-prompt-engineering)
  - [Flerstegs Chatt](../../../02-prompt-engineering)
  - [Steg-för-steg Resonemang](../../../02-prompt-engineering)
  - [Begränsad Output](../../../02-prompt-engineering)
- [Vad Du Verkligen Lär Dig](../../../02-prompt-engineering)
- [Nästa Steg](../../../02-prompt-engineering)

## Video Genomgång

Titta på denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering med LangChain4j - Live Session" width="800"/></a>

## Vad Du Kommer Lära Dig

<img src="../../../translated_images/sv/what-youll-learn.c68269ac048503b2.webp" alt="Vad Du Kommer Lära Dig" width="800"/>

I föregående modul såg du hur minne möjliggör konversationell AI och använde GitHub-modeller för grundläggande interaktioner. Nu fokuserar vi på hur du ställer frågor – själva prompts – med Azure OpenAI:s GPT-5.2. Hur du strukturerar dina prompts påverkar dramatiskt kvaliteten på svaren du får. Vi börjar med en genomgång av grundläggande promptingstekniker och går sedan vidare till åtta avancerade mönster som utnyttjar GPT-5.2:s kapaciteter fullt ut.

Vi använder GPT-5.2 eftersom den introducerar styrning av resonemang – du kan ange för modellen hur mycket tänkande den ska göra innan den svarar. Detta gör olika promptingstrategier tydligare och hjälper dig förstå när du ska använda varje metod. Vi drar också nytta av Azures mindre begränsningar i användningsfrekvens för GPT-5.2 jämfört med GitHub-modeller.

## Förkunskaper

- Avslutad Modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotmappen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Notera:** Om du inte har slutfört Modul 01, följ först distributionens instruktioner där.

## Förståelse av Prompt Engineering

<img src="../../../translated_images/sv/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Vad är Prompt Engineering?" width="800"/>

Prompt engineering handlar om att designa inmatningstext som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor – det handlar om att strukturera förfrågningar så att modellen förstår exakt vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fix the bug" är otydligt. "Fix the null pointer exception i UserService.java rad 45 genom att lägga till en null-check" är specifikt. Språkmodeller fungerar på samma sätt – specificitet och struktur är viktiga.

<img src="../../../translated_images/sv/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Hur LangChain4j Passar In" width="800"/>

LangChain4j tillhandahåller infrastrukturen — modellanslutningar, minne och meddelandetyper — medan promptmönster bara är noggrant strukturerad text som du skickar genom infrastrukturen. De viktigaste byggstenarna är `SystemMessage` (som sätter AI:ns beteende och roll) och `UserMessage` (som bär din faktiska förfrågan).

## Grundläggande Prompt Engineering

<img src="../../../translated_images/sv/five-patterns-overview.160f35045ffd2a94.webp" alt="Översikt av Fem Prompt Engineering-mönster" width="800"/>

Innan vi går in på de avancerade mönstren i denna modul, låt oss gå igenom fem grundläggande promptingstekniker. Dessa är byggstenar som varje prompt-ingenjör bör känna till. Om du redan har jobbat igenom [Quick Start-modulen](../00-quick-start/README.md#2-prompt-patterns) har du sett dessa i praktiken – här är det konceptuella ramverket bakom dem.

### Zero-Shot Prompting

Det enklaste tillvägagångssättet: ge modellen en direkt instruktion utan exempel. Modellen förlitar sig helt på sin träning för att förstå och utföra uppgiften. Detta fungerar bra för enkla förfrågningar där förväntat beteende är tydligt.

<img src="../../../translated_images/sv/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direkt instruktion utan exempel – modellen härleder uppgiften från instruktionen ensam*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Svar: "Positiv"
```

**När använda:** Enkla klassificeringar, direkta frågor, översättningar eller vilken uppgift som helst som modellen kan hantera utan ytterligare vägledning.

### Few-Shot Prompting

Ge exempel som visar det mönster du vill att modellen ska följa. Modellen lär sig det förväntade in- och utdataformatet från dina exempel och tillämpar det på nya indata. Detta förbättrar dramatiskt konsekvensen för uppgifter där önskat format eller beteende inte är självklart.

<img src="../../../translated_images/sv/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Lärande från exempel – modellen identifierar mönstret och tillämpar det på nya indata*

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

**När använda:** Anpassade klassificeringar, konsekvent formatering, domänspecifika uppgifter eller när zero-shot-resultat är inkonsekventa.

### Chain of Thought

Be modellen visa sitt resonemang steg för steg. Istället för att hoppa direkt till ett svar bryter modellen ner problemet och arbetar igenom varje del uttryckligen. Detta förbättrar noggrannheten vid matematik, logik och flerstegsresonemang.

<img src="../../../translated_images/sv/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Steg-för-steg resonemang – bryter ner komplexa problem i uttryckliga logiska steg*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modellen visar: 15 - 8 = 7, sedan 7 + 12 = 19 äpplen
```

**När använda:** Matteproblem, logikpussel, felsökning eller vilken uppgift som helst där visat resonemang förbättrar noggrannhet och förtroende.

### Role-Based Prompting

Sätt en persona eller roll för AI innan du ställer din fråga. Detta ger kontext som formar ton, djup och fokus i svaret. En "software architect" ger annat råd än en "junior developer" eller en "security auditor".

<img src="../../../translated_images/sv/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Anger kontext och persona – samma fråga får olika svar beroende på tilldelad roll*

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

**När använda:** Kodgranskningar, handledning, domänspecifik analys eller när du behöver svar anpassade efter särskild expertis eller perspektiv.

### Prompt Templates

Skapa återanvändbara prompts med variabla platshållare. Istället för att skriva ny prompt varje gång, definiera en mall en gång och fyll i olika värden. LangChain4j:s `PromptTemplate`-klass gör detta enkelt med `{{variable}}`-syntax.

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

**När använda:** Upprepade frågor med olika indata, batchbearbetning, bygga återanvändbara AI-arbetsflöden eller vilken situation som helst där promptens struktur är densamma men datan ändras.

---

Dessa fem grunder ger dig en solid verktygslåda för de flesta promptinguppgifter. Resten av denna modul bygger vidare på dem med **åtta avancerade mönster** som utnyttjar GPT-5.2:s styrning av resonemang, självutvärdering och strukturerad output.

## Avancerade Mönster

Med grunderna täckta, låt oss gå vidare till de åtta avancerade mönstren som gör denna modul unik. Inte alla problem behöver samma tillvägagångssätt. Vissa frågor behöver snabba svar, andra kräver djup eftertanke. Vissa behöver synligt resonemang, andra bara resultat. Varje mönster nedan är optimerat för en särskild situation – och GPT-5.2:s styrning av resonemang gör skillnaderna ännu tydligare.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Åtta Prompt Engineering-mönster" width="800"/>

*Översikt över de åtta prompt engineering-mönstren och deras användningsområden*

<img src="../../../translated_images/sv/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Styrning av resonemang med GPT-5.2" width="800"/>

*GPT-5.2:s styrning av resonemang låter dig specificera hur mycket tänkande modellen ska göra – från snabba direkta svar till djup utforskning*

**Låg Iver (Snabb & Målinriktad)** - För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimal resonemang - max 2 steg. Använd detta för beräkningar, uppslagningar eller enkla frågor.

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
> - "Hur hjälper XML-taggar i prompts att strukturera AI:ns svar?"
> - "När ska jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög Iver (Djup & Noggrann)** - För komplexa problem där du vill ha en komplett analys. Modellen utforskar grundligt och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturval eller komplex forskning.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Uppgiftsutförande (Steg-för-steg Framskridande)** - För arbetsflöden i flera steg. Modellen ger en upfront plan, berättar om varje steg medan den arbetar och avslutar med en sammanfattning. Använd detta för migrationer, implementationer eller alla flerstegsprocesser.

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

Chain-of-Thought prompting ber uttryckligen modellen visa sitt resonemangsprocess, vilket förbättrar noggrannheten på komplexa uppgifter. Den stegvisa nedbrytningen hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:
> - "Hur skulle jag anpassa uppgiftsutförandemönstret för långvariga operationer?"
> - "Vilka är bästa praxis för att strukturera verktygspreambuler i produktionsapplikationer?"
> - "Hur kan jag fånga och visa mellanliggande progressuppdateringar i en UI?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Uppgiftsutförande-mönster" width="800"/>

*Planera → Utföra → Sammanfatta arbetsflöde för flerstegsuppgifter*

**Självreflekterande Kod** - För att generera kod i produktionskvalitet. Modellen genererar kod enligt produktionsstandarder med rätt felhantering. Använd detta när du bygger nya funktioner eller tjänster.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Självreflektionscykel" width="800"/>

*Iterativ förbättringscykel – generera, utvärdera, identifiera problem, förbättra, repetera*

**Strukturerad Analys** - För konsekvent utvärdering. Modellen granskar kod med ett fast ramverk (korrekthet, praxis, prestanda, säkerhet, underhållbarhet). Använd detta för kodgranskningar eller kvalitetsbedömningar.

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
> - "Vad är bästa sättet att analysera och agera på strukturerad output programmässigt?"
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer i olika granskningssessioner?"

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Strukturerat Analysmönster" width="800"/>

*Ramverk för konsekventa kodgranskningar med allvarlighetsnivåer*

**Flerstegs Chatt** - För konversationer som behöver kontext. Modellen kommer ihåg tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplexa Q&A.

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

*Hur samtalskontext ackumuleras över flera turer tills token-gränsen nås*

**Steg-för-steg Resonemang** - För problem som kräver synlig logik. Modellen visar uttryckligt resonemang för varje steg. Använd detta för matteproblem, logikpussel eller när du behöver förstå tankegången.

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

*Bryta ner problem i uttryckliga logiska steg*

**Begränsad Output** - För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt outputstruktur.

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

<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Begränsat Outputmönster" width="800"/>

*Upprätthålla specifika format-, längd- och strukturella krav*

## Använda Befintliga Azure-resurser

**Verifiera distribution:**

Se till att `.env`-filen finns i rotmappen med Azure-uppgifter (skapad under Modul 01):
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startat alla applikationer med `./start-all.sh` från Modul 01, kör denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.
**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som tillhandahåller ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet till vänster i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stanna applikationer med ett enda klick
- Visa applikationsloggar i realtid
- Övervaka applikationens status

Klicka helt enkelt på play-knappen bredvid "prompt-engineering" för att starta den här modulen, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalog
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

Båda skripten laddar automatiskt miljövariabler från rotfilen `.env` och bygger JAR-filerna om de inte finns.

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
.\stop.ps1  # Denna modul endast
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Applikationsskärmdumpar

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Hem" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvuddashboard som visar alla 8 prompt engineering-mönster med deras egenskaper och användningsfall*

## Utforska Mönstren

Webbgränssnittet låter dig experimentera med olika strategier för prompting. Varje mönster löser olika problem – testa dem för att se när varje tillvägagångssätt är bäst.

> **Notera: Streaming vs Ej Streaming** — Varje mönstersida erbjuder två knappar: **🔴 Stream Response (Live)** och ett **Ej streaming**-alternativ. Streaming använder Server-Sent Events (SSE) för att visa token i realtid när modellen genererar dem, så du ser framstegen direkt. Ej-streaming väntar på hela svaret innan det visas. För prompts som kräver djup resonemang (t.ex. High Eagerness, Självreflekterande Kod) kan ej-streamingsanropet ta mycket lång tid – ibland minuter – utan synlig återkoppling. **Använd streaming när du experimenterar med komplexa prompts** så att du kan se modellen arbeta och undvika intrycket av att förfrågan har gått ut på tid.
>
> **Notera: Webbläsarkrav** — Streamingfunktionen använder Fetch Streams API (`response.body.getReader()`) som kräver en fullständig webbläsare (Chrome, Edge, Firefox, Safari). Det fungerar **inte** i VS Codes inbyggda Simple Browser, eftersom dess webview inte stödjer ReadableStream API. Om du använder Simple Browser fungerar knapparna för ej streaming fortfarande som vanligt – det är bara streamingknapparna som påverkas. Öppna `http://localhost:8083` i en extern webbläsare för full upplevelse.

### Låg vs Hög Entusiasm

Ställ en enkel fråga som "Vad är 15% av 200?" med låg entusiasm. Du får ett omedelbart, direkt svar. Ställ sedan något komplext som "Designa en caching-strategi för en API med hög trafik" med hög entusiasm. Klicka på **🔴 Stream Response (Live)** och se modellens detaljerade resonemang visas token för token. Samma modell, samma frågestruktur – men prompten talar om hur mycket tänkande som ska göras.

### Uppgiftsutförande (Verktygs-Preambeler)

Flerstegsarbetsflöden gynnas av planering i förväg och framstegsberättande. Modellen beskriver vad den ska göra, berättar om varje steg och sammanfattar sedan resultatet.

### Självreflekterande Kod

Testa "Skapa en tjänst för e-postvalidering". Istället för att bara generera kod och stanna, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du ser den iterera tills koden uppfyller produktionsstandarder.

### Strukturerad Analys

Kodgranskningar behöver konsekventa utvärderingsramverk. Modellen analyserar kod med fasta kategorier (riktighet, praxis, prestanda, säkerhet) med olika allvarlighetsnivåer.

### Fleromgångschatt

Fråga "Vad är Spring Boot?" och följ direkt upp med "Visa mig ett exempel". Modellen minns din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne skulle den andra frågan vara för vag.

### Steg-för-steg Resonemang

Välj ett matteproblem och prova både steg-för-steg resonemang och låg entusiasm. Låg entusiasm ger bara svaret – snabbt men oklart. Steg-för-steg visar varje beräkning och beslut.

### Begränsad Output

När du behöver specifika format eller ordantal, säkerställer detta mönster strikt efterlevnad. Testa att generera en sammanfattning med exakt 100 ord i punktlista.

## Vad du verkligen lär dig

**Resoneringsinsats förändrar allt**

GPT-5.2 låter dig kontrollera den beräkningsmässiga insatsen genom dina prompts. Låg insats innebär snabba svar med minimal utforskning. Hög insats betyder att modellen tar tid att tänka djupt. Du lär dig att matcha insats efter uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte komplexa beslut heller.

**Struktur styr beteende**

Lägg märke till XML-taggarna i prompts? De är inte dekorativa. Modeller följer strukturerade instruktioner mer tillförlitligt än fri text. När du behöver flerstegsprocesser eller komplex logik hjälper struktur modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Promptstrukturen" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-stil organisation*

**Kvalitet genom självvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier tydliga. Istället för att hoppas att modellen "gör rätt", berättar du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen output och förbättra den. Detta förvandlar kodgenerering från ett lotteri till en process.

**Kontext är begränsad**

Fleromgångssamtal fungerar genom att inkludera meddelandehistorik med varje förfrågan. Men det finns en gräns – varje modell har ett maximalt antal tokens. När samtalen växer behöver du strategier för att behålla relevant kontext utan att nå taket. Den här modulen visar hur minne fungerar; senare lär du dig när du ska sammanfatta, när du ska glömma och när du ska hämta.

## Nästa steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till huvudsidan](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Friskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig observera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess ursprungsspråk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår från användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->