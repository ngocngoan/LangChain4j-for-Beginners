# Module 02: Uhandisi wa Maelekezo na GPT-5.2

## Table of Contents

- [What You'll Learn](../../../02-prompt-engineering)
- [Prerequisites](../../../02-prompt-engineering)
- [Understanding Prompt Engineering](../../../02-prompt-engineering)
- [Prompt Engineering Fundamentals](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Advanced Patterns](../../../02-prompt-engineering)
- [Using Existing Azure Resources](../../../02-prompt-engineering)
- [Application Screenshots](../../../02-prompt-engineering)
- [Exploring the Patterns](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [What You're Really Learning](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## What You'll Learn

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Katika moduli iliyopita, ulichunguza jinsi kumbukumbu inavyowezesha AI ya mazungumzo na kutumia GitHub Models kwa mwingiliano wa msingi. Sasa tutazingatia jinsi unavyouliza maswali — maelekezo yenyewe — kwa kutumia GPT-5.2 ya Azure OpenAI. Njia unavyoandaa maelekezo yako inaathiri sana ubora wa majibu unayopata. Tunaanza na mapitio ya mbinu za msingi za maelekezo, kisha tunaenda kwenye mifumo minane ya hali ya juu inayotumia uwezo wa GPT-5.2 kikamilifu.

Tutatumia GPT-5.2 kwa sababu inaanzisha udhibiti wa fikra - unaweza kumuambia mfano kiwango cha kufikiri kabla ya kutoa jibu. Hii inafanya mikakati tofauti ya maelekezo ionekane zaidi na kukusaidia kuelewa ni lini kutumia kila mbinu. Pia tutanufaika na mipaka midogo ya kiwango cha maombi ya Azure kwa GPT-5.2 ikilinganishwa na GitHub Models.

## Prerequisites

- Umehitimu Moduli 01 (Rasilimali za Azure OpenAI zimewekwa)
- Faili `.env` katika saraka ya mzizi yenye vyeti vya Azure (imeundwa na `azd up` katika Moduli 01)

> **Note:** Ikiwa hujakamilisha Moduli 01, tafadhali fuata maelekezo ya usambazaji hapo kwanza.

## Understanding Prompt Engineering

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Uhandisi wa maelekezo ni kuhusu kubuni maandishi ya kuingiza ambayo yanakupatia matokeo unayohitaji kila wakati. Siyo tu kuuliza maswali - ni kuhusu kupanga maombi ili mfano ufahamu hasa unachotaka na jinsi ya kuyatoa.

Fikiria kama unamwelekeza mwenzako kazini. "Tengeneza hitilafu" ni hilo la kawaida. "Tengeneza hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Mifano ya lugha hufanya kazi sawa - umaelekezaji maalum na muundo ni muhimu.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j inatoa miundombinu — muunganisho wa modeli, kumbukumbu, na aina za ujumbe — wakati mifumo ya maelekezo ni maandishi yaliyopangwa kwa umakini unayotuma kupitia miundombinu hiyo. Vifunguo muhimu ni `SystemMessage` (ambayo huweka tabia na jukumu la AI) na `UserMessage` (ambayo huleta ombi lako halisi).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Kabla ya kuingia kwenye mifumo ya hali ya juu katika moduli hii, hebu livutie upya mbinu tano za msingi za kuandika maelekezo. Hizi ni msingi wa kila mhandisi wa maelekezo anapaswa kujua. Ikiwa tayari umefanya kazi kupitia [moduli ya kuanza haraka](../00-quick-start/README.md#2-prompt-patterns), umeona hizi zikifanya kazi — hapa kuna mfumo wa dhana nyuma yao.

### Zero-Shot Prompting

Njia rahisi kabisa: mpe mfano maelekezo moja kwa moja bila mifano. Mfano hutegemea kabisa mafunzo yake kuelewa na kutekeleza kazi. Hii hufanya kazi vizuri kwa maombi rahisi ambapo tabia inayotarajiwa ni wazi.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Maelekezo ya moja kwa moja bila mifano — mfano huhitimisha kazi kutokana na maelekezo pekee*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Jibu: "Chanya"
```

**Wakati wa kutumia:** Uainishaji rahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote ambayo mfano unaweza kushughulikia bila mwongozo zaidi.

### Few-Shot Prompting

Toa mifano inayonyesha muundo unaotaka mfano ufuate. Mfano hujifunza fomati ya kuingiza-kutoa inayotarajiwa kutoka kwa mifano yako na kuitumia kwa ingizo jipya. Hii huongeza uthabiti kwa kazi ambazo muundo au tabia inayotakiwa haijulikani kwa uwazi.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Kujifunza kutoka kwa mifano — mfano hutambua muundo na kuutumia kwa ingizo jipya*

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

**Wakati wa kutumia:** Uainishaji wa desturi, muundo thabiti, kazi maalum kwa nyanja, au wakati matokeo ya zero-shot hayana uthabiti.

### Chain of Thought

Muombe mfano kuonyesha mchakato wake wa kufikiri hatua kwa hatua. Badala ya kuruka moja kwa moja kwenye jibu, mfano hutenganisha tatizo na kufanya kazi kupitia kila sehemu waziwazi. Hii huongeza usahihi kwa hesabu, mantiki, na kazi za kufikiri hatua kwa hatua.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Ufikiriaji hatua kwa hatua — kugawanya matatizo magumu katika hatua za mantiki wazi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 maapulo
```

**Wakati wa kutumia:** Matatizo ya hisabati, fumbo za mantiki, utatuzi wa makosa, au kazi yoyote ambapo kuonyesha mchakato wa kufikiri huongeza usahihi na kuaminika.

### Role-Based Prompting

Weka hadhira au jukumu kwa AI kabla ya kuuliza swali. Hii hutoa muktadha unaoathiri toni, kina, na umakini wa jibu. "Mbunifu wa programu" hutoa ushauri tofauti na "mendelezaji mdogo" au "mchambuzi wa usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kuweka muktadha na hadhira — swali moja hupokea majibu tofauti kulingana na jukumu lililowekwa*

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

**Wakati wa kutumia:** Mapitio ya msimbo, ufundishaji, uchambuzi wa nyanja maalum, au wakati unahitaji majibu yaliyobinafsishwa kwa kiwango fulani cha utaalamu au mtazamo.

### Prompt Templates

Tengeneza maelekezo yanayoweza kutumika tena yenye nafasi za kubadilisha. Badala ya kuandika maelekezo mapya kila mara, fafanua kiolezo mara moja na ujaze thamani tofauti. Darasa la LangChain4j `PromptTemplate` linafaa kwa hii kutumia muundo wa `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Maelekezo yanayoweza kutumika tena yenye sehemu za kubadilika — kiolezo kimoja, matumizi mengi*

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

**Wakati wa kutumia:** Maswali yanayojirudia na ingizo tofauti, usindikaji wa kikundi, ujenzi wa mtiririko wa kazi wa AI unaorudiwa, au hali yoyote ambapo muundo wa maelekezo unabaki uleule lakini data hubadilika.

---

Misingi hii mitano inakupa chombo thabiti kwa kazi nyingi za kuandika maelekezo. Sehemu nyingine ya moduli hii inajengwa juu yao na **mifumo minane ya hali ya juu** inayotumia udhibiti wa fikra wa GPT-5.2, kujitathmini, na uwezo wa kutoa maelezo yaliyopangwa.

## Advanced Patterns

Baada ya kufunzwa misingi, hebu tuelekee kwenye mifumo minane ya hali ya juu inayofanya moduli hii kuwa ya kipekee. Siyo matatizo yote yanahitaji njia sawa. Maswali mengine yanahitaji majibu ya haraka, mengine yanahitaji kufikiri kwa kina. Baadhi yanahitaji fikra zinazojulikana, mengine moto matokeo tu. Kila mfumo hapa chini umeboreshwa kwa hali tofauti — na udhibiti wa fikra wa GPT-5.2 unafanya tofauti hizi ziwe dhahiri zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Muhtasari wa mifumo minane ya uhandisi wa maelekezo na matumizi yake*

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Udhibiti wa fikra wa GPT-5.2 hukuruhusu kutaja kiasi gani cha kufikiri mfano unapaswa kufanya — kutoka majibu ya haraka hadi uchunguzi wa kina*

**Low Eagerness (Haraka & Lenga)** - Kwa maswali rahisi ambapo unataka majibu ya haraka na ya moja kwa moja. Mfano hufanya fikra kidogo - hatua 2 tu kwa kiwango cha juu. Tumia hii kwa hesabu, uangalizi, au maswali ya moja kwa moja.

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

> 💡 **Chunguza na GitHub Copilot:** Fungua [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) na uliza:
> - "Tofauti kati ya mifumo ya maelekezo ya low eagerness na high eagerness ni gani?"
> - "Je, lebo za XML katika maelekezo husaidiaje kupanga jibu la AI?"
> - "Ninapumzaje mifumo ya kujitathmini dhidi ya maelekezo ya moja kwa moja?"

**High Eagerness (Kina & Kina Kamilifu)** - Kwa matatizo magumu ambapo unataka uchambuzi wa kina. Mfano huchunguza kwa kina na kuonyesha fikra za kina. Tumia kwa usanifu wa mifumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - Kwa mchakato wa hatua nyingi. Mfano hutoa mpango wa awali, unaelezea kila hatua anapofanya kazi, kisha hutoa muhtasari. Tumia hii kwa uhamishaji, utekelezaji, au mchakato wowote wa hatua nyingi.

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

Prompting ya Chain-of-Thought huomba mfano kuonyesha mchakato wake wa fikra waziwazi, kuongeza usahihi kwa kazi ngumu. Ugawaji wa hatua kwa hatua husaidia wanadamu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:
> - "Ningebadilishaje mfumo wa utekelezaji wa kazi kwa shughuli za muda mrefu?"
> - "Ni mbinu gani bora za kupanga maelezo ya zana katika programu za uzalishaji?"
> - "Ninawezaje kuchukua na kuonyesha taarifa za maendeleo ya kati katika UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Mpango → Tekeleza → Fupisha mtiririko wa kazi kwa kazi za hatua nyingi*

**Self-Reflecting Code** - Kwa kuzalisha msimbo wa ubora wa uzalishaji. Mfano huzalisha msimbo unaoendana na viwango vya uzalishaji na usimishaji bora wa makosa. Tumia hii wakati wa kujenga vipengele vyote vipya au huduma.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Mzunguko wa kuboresha mara kwa mara - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Structured Analysis** - Kwa tathmini thabiti. Mfano hupitia msimbo kwa kutumia mfumo uliowekwa (ukweli, mbinu, utendaji, usalama, urahisi wa matengenezo). Tumia hii kwa mapitio ya msimbo au tathmini za ubora.

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

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu uchambuzi uliopangwa:
> - "Ninawezaje kubinafsisha mfumo wa uchambuzi kwa aina tofauti za mapitio ya msimbo?"
> - "Njia bora ya kuchambua na kutenda juu ya matokeo yaliyopangwa programmatically ni ipi?"
> - "Ninawezaje kuhakikisha viwango vya ukali viko thabiti katika vikao tofauti vya mapitio?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Mfumo wa mapitio ya msimbo thabiti na viwango vya ukali*

**Multi-Turn Chat** - Kwa mazungumzo yanayohitaji muktadha. Mfano hukumbuka ujumbe wa awali na kujenga juu yake. Tumia kwa vikao vya msaada wa ushirikiano au maswali na majibu magumu.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sw/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Jinsi muktadha wa mazungumzo unavyokusanywa kwa mizunguko mingi hadi kufikia kikomo cha tokeni*

**Step-by-Step Reasoning** - Kwa matatizo yanayohitaji mantiki inayojulikana. Mfano huonyesha ufikiri wa wazi kwa kila hatua. Tumia kwa matatizo ya hisabati, fumbo za mantiki, au unapotaka kuelewa mchakato wa kufikiri.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sw/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Kuvunja matatizo katika hatua za mantiki wazi*

**Constrained Output** - Kwa majibu yenye mahitaji mahsusi ya muundo. Mfano hufuata vikali sheria za muundo na urefu. Tumia hii kwa muhtasari au wakati unahitaji muundo sahihi wa jibu.

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

<img src="../../../translated_images/sw/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Kutekeleza vigezo vya muundo, urefu, na mahitaji ya muundo*

## Using Existing Azure Resources

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo katika saraka ya mzizi yenye vyeti vya Azure (imeundwa wakati wa Moduli 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Note:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendeshwa kwenye bandari 8083. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Container ya maendeleo inaongeza programu-jalizi ya Spring Boot Dashboard, ambayo hutoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Ukanda wa Shughuli upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana katika eneo la kazi
- Anzisha/zimia programu kwa bonyeza mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Simamia hali ya programu
Bofya tu kitufe cha kuanza kando ya "prompt-engineering" kuanzisha moduli hii, au anzisha moduli zote mara moja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo la 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka ya mizizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anzisha moduli hii tu:

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

Skripti zote huhamisha moja kwa moja vigezo vya mazingira kutoka kwa faili `.env` ya mzizi na zitaunda JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanzisha:
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

Fungua http://localhost:8083 katika kivinjari chako.

**Kusimama:**

**Bash:**
```bash
./stop.sh  # Hii moduli pekee
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Moduli hii tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Picha za Programu

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashibodi kuu inaonyesha mifumo 8 ya uhandisi wa prompt na sifa zao pamoja na matumizi yao*

## Kuchunguza Mifumo

Kiolesura cha wavuti hukuruhusu kujifunza mikakati tofauti ya utoaji wa prompt. Kila mfumo unatatua matatizo tofauti - jaribu kuona ni lini kila njia inafanya vizuri.

> **Kumbuka: Utiririshaji dhidi ya Kutokutiiririshaji** — Kila ukurasa wa muundo una vitufe viwili: **🔴 Stream Response (Live)** na chaguo la **Non-streaming**. Utiririshaji hutumia Matukio Yanayotoka Kwenye Mtandao (SSE) kuonesha tokeni kwa wakati halisi wakati mfano unazitengeneza, hivyo unaona maendeleo mara moja. Chaguo la kutokutiiririshaji husubiri jibu zima kabla ya kuionyesha. Kwa prompt zinazochochea mawazo ya kina (mfano, High Eagerness, Self-Reflecting Code), simu isiyo ya kutiririshaji inaweza kuchukua muda mrefu sana — wakati mwingine dakika — bila maoni yanayoonekana. **Tumia utiririshaji unapojaribu prompts za changamoto ili uone mfano ukifanya kazi na kuepuka hisia kwamba ombi limechelewa.**
>
> **Kumbuka: Sharti la Kivinjari** — Kipengele cha utiririshaji kinatumia API ya Fetch Streams (`response.body.getReader()`) ambayo huhitaji kivinjari kamili (Chrome, Edge, Firefox, Safari). Hakifanyi kazi katika Kivinjari Rahisi kilichojengwa ndani ya VS Code, kwani mtazamo wake wa wavuti hauungi mkono API ya ReadableStream. Ukitumia Kivinjari Rahisi, vitufe vya kutokutiiririshaji bado vitafanya kazi kawaida — ni vitufe vya utiririshaji tu vinavyoathirika. Fungua `http://localhost:8083` katika kivinjari cha nje kwa uzoefu kamili.

### Chochote Chenye Nidhamu Nchini High Eagerness

Uliza swali rahisi kama "Asilimia 15 ya 200 ni kiasi gani?" ukitumia Nidhamu Chini. Utapokea jibu la moja kwa moja mara moja. Sasa uliza jambo tata kama "Tengeneza mkakati wa caching kwa API yenye trafiki nyingi" ukitumia Nidhamu Juu. Bofya **🔴 Stream Response (Live)** na angalia maelezo ya mfano yakitokea tokeni kwa tokeni. Mfano mmoja, muundo sawa wa swali - lakini prompt inaelezea kiasi gani cha fikra gumu kinahitajika.

### Utekelezaji wa Kazi (Utangulizi wa Zana)

Mchakato wa hatua nyingi hufaidika na mipango ya awali na maelezo ya maendeleo. Mfano huonyesha atakachofanya, kuelezea kila hatua, kisha kufupisha matokeo.

### Kanuni Zinayojitafakari

Jaribu "Tengeneza huduma ya uthibitishaji wa barua pepe". Badala ya kuzalisha tu msimbo na kusimama, mfano huzalisha, kutathmini kulingana na vigezo vya ubora, kutambua udhaifu, na kuboresha. Utaiona ikitiririka hadi msimbo ukidhi viwango vya uzalishaji.

### Uchambuzi uliopangwa

Mapitio ya msimbo yanahitaji mifumo ya tathmini inayowiana. Mfano huchambua msimbo kwa makundi yaliyoamika (usahihi, mbinu, utendakazi, usalama) kwa viwango vya ukali.

### Mazungumzo ya Zamu Nyingi

Uliza "Spring Boot ni nini?" kisha kufuatia mara moja na "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na kukupa mfano wa Spring Boot hasa. Bila kumbukumbu, swali la pili lingeonekana dhahiri mno.

### Mawazo Hatua kwa Hatua

Chagua tatizo la hesabu na jaribu kwa Mawazo Hatua kwa Hatua na Nidhamu Chini. Nidhamu chini hupata jibu tu - haraka lakini haueleweki. Hatua kwa hatua huonyesha kila hesabu na uamuzi.

### Matokeo Yaliyopigwa Hadi

Unapohitaji muundo maalum au idadi ya maneno, mfumo huu huweka utekelezaji mkali. Jaribu kuzalisha muhtasari wenye maneno 100 hasa kwa muundo wa vidokezo.

## Unajifunza Nini Kweli

**Juhudi za Mawazo Hubadilisha Kila Kitu**

GPT-5.2 hukuwezesha kudhibiti juhudi za kompyuta kupitia prompt zako. Juhudi ndogo zinamaanisha majibu ya haraka na uchunguzi mdogo. Juhudi kubwa zinamaanisha mfano hulazimika kufikiria kwa kina. Una Jifunza kupima juhudi kwa ugumu wa kazi - usipoteze muda kwa maswali rahisi, lakini pia usikimbilie maamuzi magumu.

**Muundo Huweka Mwelekeo wa Tabia**

Umeona lebo za XML katika prompt? Si mapambo tu. Mifano hufuata maagizo yaliyopangwa kwa uaminifu zaidi kuliko maandishi ya kawaida. Unapohitaji michakato ya hatua nyingi au mantiki tata, muundo huwasaidia mifano kufuatilia wapi iko na nini kinakuja.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Sehemu ya muundo mzuri wa prompt yenye sehemu wazi na mpangilio wa mtindo wa XML*

**Ubora Kupitia Tathmini Binafsi**

Mifumo inayojiendesha yenyewe hufanya vigezo vya ubora kuwa wazi. Badala ya kutegemea mfano "ufanye kwa usahihi", unamuambia maana halisi ya "sahihi": mantiki sahihi, usimamizi wa kosa, utendakazi, usalama. Mfano huweza kisha kutathmini utendaji wake na kuboresha. Hii hubadilisha utengenezaji wa msimbo kutoka bahati nasibu kuwa mchakato.

**Muktadha Una Kipepeo**

Mazungumzo ya zamu nyingi hufanyika kwa kujumuisha historia ya ujumbe na kila ombi. Lakini kuna kikomo - kila mfano una idadi ya juu ya tokeni. Kadiri mazungumzo yanavyozidi, unahitaji mikakati ya kuhifadhi muktadha muhimu bila kufikia kikomo hicho. Moduli hii inaonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kufupisha, lini kusahau, na lini kupata tena.

## Hatua Zifuatazo

**Moduli Inayofuata:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Mwelekeo:** [← Awali: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Hukumu**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au upotoshaji. Nyaraka ya asili katika lugha yake asilia inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayotolewa na binadamu inashauriwa. Hatuna dhamana kwa masuala yoyote ya kutoelewana au tafsiri isiyo sahihi inayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->