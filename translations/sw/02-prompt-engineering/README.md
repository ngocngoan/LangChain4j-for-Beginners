# Moduli 02: Uhandisi wa Prompt na GPT-5.2

## Orodha ya Yaliyomo

- [Video ya Maelezo](../../../02-prompt-engineering)
- [Utajifunza Nini](../../../02-prompt-engineering)
- [Mahitaji ya Awali](../../../02-prompt-engineering)
- [Kuelewa Uhandisi wa Prompt](../../../02-prompt-engineering)
- [Misingi ya Uhandisi wa Prompt](../../../02-prompt-engineering)
  - [Prompt bila Mfano (Zero-Shot)](../../../02-prompt-engineering)
  - [Prompt zenye Mifano Machache (Few-Shot)](../../../02-prompt-engineering)
  - [Mnyororo wa Fikiria (Chain of Thought)](../../../02-prompt-engineering)
  - [Prompt Kwa Kuingia Nafasi (Role-Based)](../../../02-prompt-engineering)
  - [Mifumo ya Prompt](../../../02-prompt-engineering)
- [Mifumo ya Juu Zaidi](../../../02-prompt-engineering)
- [Kutumia Rasilimali Zilizopo Azure](../../../02-prompt-engineering)
- [Picha za Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Hamasa Chini Vs Juu](../../../02-prompt-engineering)
  - [Utekelezaji wa Kazi (Utangulizi wa Zana)](../../../02-prompt-engineering)
  - [Kanuni za Kujitathmini](../../../02-prompt-engineering)
  - [Uchambuzi wa Kimuundo](../../../02-prompt-engineering)
  - [Mazungumzo ya Marudio Mengi](../../../02-prompt-engineering)
  - [Kutafsiri Hatua kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yaliyo Thibitishwa](../../../02-prompt-engineering)
- [Unajifunza Kwa Ukweli](../../../02-prompt-engineering)
- [Hatua Zifuatazo](../../../02-prompt-engineering)

## Video ya Maelezo

Tazama kikao hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Utajifunza Nini

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Katika moduli iliyopita, uliwahi kuona jinsi kumbukumbu inavyowezesha AI ya mazungumzo na ulitumia Modeli za GitHub kwa mwingiliano wa msingi. Sasa tutazingatia jinsi unavyouliza maswali — yale maelekezo yenyewe — ukitumia GPT-5.2 ya Azure OpenAI. Njia unavyopanga maelekezo yako inaathiri sana ubora wa majibu unayopata. Tunaanzia kwa mapitio ya mbinu msingi za maelekezo, kisha tunaelekea kwenye mifumo nane ya juu zaidi inayotumia kikamilifu uwezo wa GPT-5.2.

Tutatumia GPT-5.2 kwa sababu inaletesha udhibiti wa fikra - unaweza kusema kwa mfano kiasi gani cha kufikiria kifanye kabla ya kujibu. Hii inafanya mbinu tofauti za maelekezo kuwa wazi zaidi na inakusaidia kuelewa lini kutumia kila mbinu. Pia tutafaidika na mipaka midogo zaidi ya kiwango cha matumizi ya GPT-5.2 kwenye Azure ikilinganishwa na Modeli za GitHub.

## Mahitaji ya Awali

- Kumaliza Moduli 01 (rasilimali za Azure OpenAI zimetoweka)
- Faili `.env` katika saraka kuu na taarifa za Azure (iliyoanzishwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujamaliza Moduli 01, tafadhali fuata maagizo ya uanzishaji hapo kwanza.

## Kuelewa Uhandisi wa Prompt

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Uhandisi wa prompt ni kuhusu kubuni maandishi ya kuingiza ambayo kwaheri yanakupatia matokeo unayohitaji kwa kutegemea. Hii siyo tu kuhusu kuuliza maswali - ni kuhusu kupanga maombi kwa njia ambayo mfano unaelewa kabisa unachotaka na jinsi ya kutoa majibu hayo.

Fikiria kama unamwambia mwenzako maelekezo. "Rekebisha hitilafu" ni ya jumla. "Rekebisha hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Modeli za lugha zinafanya hivyo vile vile - umakini na muundo ni muhimu.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j hutoa miundombinu — muunganisho wa modeli, kumbukumbu, na aina za ujumbe — huku mifumo ya maelekezo ikiwa maandishi yaliyo pangwa kwa makini unayotumia kupitia miundombinu hiyo. Vipengele muhimu ni `SystemMessage` (ambayo inaweka tabia na nafasi ya AI) na `UserMessage` (ambayo inaomba halisi yako).

## Misingi ya Uhandisi wa Prompt

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Kabla ya kuingia kwenye mifumo ya juu katika moduli hii, hebu pitia mbinu tano msingi za kuandaa maelekezo. Hizi ni msingi ambao kila mhandisi wa prompt anapaswa kujua. Ikiwa tayari umefanya kazi kwenye [Moduli ya Kuanzia Haraka](../00-quick-start/README.md#2-prompt-patterns), umezoea haya — hapa kuna msururu wa dhana nyuma yake.

### Prompt Bila Mfano (Zero-Shot Prompting)

Njia rahisi kabisa: toa maelekezo ya moja kwa moja bila mifano. Mfano hutegemea kabisa mafunzo yake kuelewa na kutekeleza kazi. Hii hufanya kazi vizuri kwa maombi rahisi ambapo tabia inayotarajiwa ni dhahiri.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Maelekezo ya moja kwa moja bila mifano — mfano hutambua kazi kutoka tu kwa maelekezo*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Jibu: "Chanya"
```

**Wakati wa kutumia:** Uainishaji rahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote ambayo mfano unaweza kushughulikia bila mwongozo zaidi.

### Prompt zenye Mifano Machache (Few-Shot Prompting)

Toa mifano inayothibitisha muundo unaotaka mfano ufuate. Mfano hujifunza muundo wa kuingiza-na-kutoa kutoka kwa mifano yako na kuutumia kwa maingizo mapya. Hii huongeza uthabiti kwa njia kubwa katika kazi ambazo muundo au tabia inayotarajiwa si dhahiri.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Kujifunza kutoka kwa mifano — mfano hutambua muundo na kuutumia kwa maingizo mapya*

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

**Wakati wa kutumia:** Uainishaji maalum, fomati thabiti, kazi za kikoa maalum, au wakati matokeo ya zero-shot hayako thabiti.

### Mnyororo wa Fikiria (Chain of Thought)

Muulize mfano aoneshe sababu zake hatua kwa hatua. Badala ya kuruka moja kwa moja kwenye jibu, mfano hugawanya tatizo na kufanya kazi kupitia kila sehemu waziwazi. Hii inaongeza usahihi kwenye hesabu, mantiki, na kazi za kufikiria hatua nyingi.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Fikra hatua kwa hatua — kugawanya matatizo magumu katika hatua za mantiki wazi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 tufaha
```

**Wakati wa kutumia:** Matatizo ya hisabati, fumbo za mantiki, ugundaji wa makosa, au kazi yoyote ambapo kuonyesha mchakato wa fikra huongeza usahihi na imani.

### Prompt Kwa Kuingia Nafasi (Role-Based Prompting)

Weka utu au nafasi kwa AI kabla ya kuuliza swali lako. Hii hutoa muktadha unaobadilisha sauti, kina, na lengo la jibu. "Mhandisi wa programu" hutoa ushauri tofauti na "mwanafunzi mdogo" au "mkaguzi wa usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kuweka muktadha na utu — swali lile lina jibu tofauti kulingana na nafasi iliyowekwa*

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

**Wakati wa kutumia:** Ukaguzi wa kanuni, kufundisha, uchambuzi maalum wa kikoa, au wakati unahitaji majibu yaliyoandaliwa kwa kiwango fulani cha utaalamu au mtazamo.

### Mifumo ya Prompt

Tengeneza prompt zinazowezekana kutumika tena zenye viboreshaji vya thamani. Badala ya kuandika prompt mpya kila mara, fafanua template mara moja na ujaze thamani tofauti. Darasa la LangChain4j `PromptTemplate` linafanya hivi kwa urahisi kwa sintaksia ya `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt zinazotumika tena zenye viboreshaji vya thamani — template moja, matumizi mengi*

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

**Wakati wa kutumia:** Maswali yanayorudiwa yenye maingizo tofauti, utayarishaji wa kundi, kujenga mitiririko ya AI inayoweza kutumika tena, au hali yoyote ambapo muundo wa prompt unabaki ule lakini data hubadilika.

---

Misingi mitano hii inakupa vyombo thabiti kwa kazi nyingi za kuandaa maelekezo. Mengine ya moduli hii hujengwa juu yake kwa **mifumo nane ya juu zaidi** inayotumia udhibiti wa fikra wa GPT-5.2, auto-evaluation, na uwezo wa kutoa matokeo ya muundo.

## Mifumo ya Juu Zaidi

Kwa msingi umefunzwa, turuka kwenye mifumo nane ya juu inayoifanya moduli hii kuwa ya kipekee. Sio matatizo yote yanahitaji mbinu ile ile. Maswali mengine yanahitaji majibu ya haraka, mengine yanahitaji fikra za kina. Baadhi yanahitaji fikra inayoonekana, mengine yanahitaji tu matokeo. Kila mfumo hapa chini umeboreka kwa hali tofauti — na udhibiti wa fikra wa GPT-5.2 hufanya tofauti hizi zionekane zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Muhtasari wa mifumo nane ya uhandisi wa prompt na matumizi yake*

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Udhibiti wa fikra wa GPT-5.2 unakuwezesha kubainisha kiasi gani cha kufikiria mfano unapaswa kufanya — kutoka majibu ya haraka ya moja kwa moja hadi uchunguzi wa kina*

**Hamasa Chini (Haraka & Zilizolengwa)** - Kwa maswali rahisi unayotaka majibu ya haraka na ya moja kwa moja. Mfano hufanya fikra kidogo sana - hatua 2 kwa hali ya juu. Tumia hii kwa hesabu, utafutaji, au maswali rahisi.

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
> - "Tofauti gani kati ya mifumo ya hamasa ya chini na ya juu?"
> - "Je, tagi za XML kwenye prompt zinawezaje kusaidia kupanga majibu ya AI?"
> - "Ninapotumia mifumo ya kujitathmini mwenyewe vs maelekezo ya moja kwa moja ni lini?"

**Hamasa Juu (Kina & Kwa Kina)** - Kwa matatizo magumu unayotaka uchambuzi kamili. Mfano huchunguza kwa kina na kuonyesha sababu zilizojitokeza. Tumia hili kwa usanifu wa mfumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utekelezaji wa Kazi (Maendeleo Hatua kwa Hatua)** - Kwa mitiririko ya kazi yenye hatua kadhaa. Mfano hutoa mpango wa awali, hueleza kila hatua inavyotekelezwa, kisha hutoa muhtasari. Tumia hili kwa uhamishaji, utekelezaji, au mchakato wowote wa hatua nyingi.

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

Prompt ya Mnyororo wa Fikiria huelekeza mfano kuonyesha mchakato wa kufikiria, huongeza usahihi kwa kazi ngumu. Kugawanya hatua kwa hatua husaidia watu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Mazungumzo:** Uliza kuhusu mfumo huu:
> - "Ningebadilishaje mfumo wa utekelezaji wa kazi kwa shughuli za muda mrefu?"
> - "Nini mbinu bora za kupanga utangulizi wa zana katika programu za uzalishaji?"
> - "Ninawezaje kunasa na kuonyesha taarifa za maendeleo ya kati katika UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Mpango → Tekeleza → Fupisha mtiririko wa kazi za hatua nyingi*

**Kanuni za Kujitathmini** - Kwa uzalishaji wa kanuni bora. Mfano hutengeneza kanuni ndogo ndogo ikifuata viwango vya uzalishaji na usimamizi sahihi wa makosa. Tumia hili wakati wa kujenga vipengele vipya au huduma.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Mzunguko wa maboresho - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi wa Kimuundo** - Kwa tathmini thabiti. Mfano hupitia kanuni kwa kutumia mfumo thabiti (usahihi, taratibu, utendaji, usalama, urejeshaji). Tumia hili kwa ukaguzi wa kanuni au tathmini za ubora.

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

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Mazungumzo:** Uliza kuhusu uchambuzi wa muundo:
> - "Ninawezaje kuboresha mfumo wa uchambuzi kwa aina mbalimbali za ukaguzi wa kanuni?"
> - "Njia bora ya kuchambua na kutenda kwa matokeo ya muundo ni ipi?"
> - "Ninavyoweza kuhakikisha viwango vya ukali vinazingatiwa sawasawa katika vipindi tofauti vya ukaguzi?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Mfumo wa ukaguzi thabiti wa kanuni zenye viwango vya ukali*

**Mazungumzo ya Marudio Mengi** - Kwa mazungumzo yanayohitaji muktadha. Mfano hukumbuka ujumbe wa awali na kuendeleza kutoka hapo. Tumia hili kwa sehemu za msaada wa kuingiliana au maswali na majibu magumu.

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

*Jinsi muktadha wa mazungumzo unavyojikusanya kupitia marudio mengi hadi kufikia kikomo cha tokeni*

**Kutafsiri Hatua kwa Hatua** - Kwa matatizo yanayohitaji mantiki inayoonekana. Mfano huonyesha fikra wazi kwa kila hatua. Tumia hili kwa matatizo ya hisabati, fumbo la mantiki, au unapotaka kuelewa mchakato wa kufikiria.

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

*Kuangazia matatizo katika hatua za mantiki zilizofafanuliwa*

**Matokeo Yaliyo Thibitishwa** - Kwa majibu yenye mahitaji maalum ya muundo. Mfano hufuata kwa ukali kanuni za muundo na urefu. Tumia hili kwa muhtasari au unapotaka muundo kamili wa matokeo.

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

*Kutekeleza mahitaji maalum ya muundo, urefu, na muundo wa matokeo*

## Kutumia Rasilimali Zilizopo Azure

**Thibitisha uanzishaji:**

Hakikisha faili `.env` ipo katika saraka kuu na taarifa za Azure (iliyoanzishwa wakati wa Moduli 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii inafanya kazi tayari kwenye bandari 8083. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8083.
**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kifurushi cha maendeleo kina kiendelezi cha Spring Boot Dashboard, ambacho kinatoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kupata kwenye Msururu wa Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana kwenye nafasi ya kazi
- Anza/acha programu kwa kubofya mara moja
- Tazama rekodi za programu kwa wakati halisi
- Fuatilia hali ya programu

Bofya tu kitufe cha kucheza karibu na "prompt-engineering" kuanzisha moduli hii, au anzisha moduli zote mara moja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia skiripti za shell**

anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka katika directory ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anza moduli hii pekee:

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

Skiripti zote mbili hurusha moja kwa moja vigezo vya mazingira kutoka kwenye faili `.env` ya mzizi na zitajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanza:
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

Fungua http://localhost:8083 kwenye kivinjari chako.

**Kusitisha:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Module hii tu
# Au
cd ..; .\stop-all.ps1  # Modules zote
```

## Picha za Programu

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashibodi kuu ikionyesha mifano 8 ya prompt engineering na sifa zake pamoja na matumizi yake*

## Kuchunguza Mifano

Kiolesura cha wavuti hukuruhusu kujaribu mbinu tofauti za kuomba. Kila mfano unatatua matatizo tofauti - jaribu kuona lini kila mbinu inafanya vizuri.

> **Kumbuka: Streaming vs isiyo ya Streaming** — Kila ukurasa wa mfano hutoa vifungo viwili: **🔴 Rejea Mtiririko (Moja kwa Moja)** na chaguo la **Isiyo ya mtiririko**. Streaming hutumia Matukio Yanayotumwa Kwenye Server (SSE) kuonyesha tokeni kwa wakati halisi wakati mfano unazitoa, hivyo unaona maendeleo mara moja. Chaguo lisilo la mtiririko huchukua jibu lote kabla ya kuonyesha. Kwa maombi yanayohitaji uelewa wa kina (kwa mfano, High Eagerness, Self-Reflecting Code), simu isiyo ya mtiririko inaweza kuchukua muda mrefu — wakati mwingine dakika — bila maoni yoyote yaliyo wazi. **Tumia streaming unapojaribu prompts ngumu** ili uweze kuona mfano ukifanya kazi na kuepuka hisia kuwa ombi limekwisha muda.
>
> **Kumbuka: Hitaji la Kivinjari** — Kipengele cha streaming hutumia API ya Fetch Streams (`response.body.getReader()`) ambayo inahitaji kivinjari kamili (Chrome, Edge, Firefox, Safari). HAIENDI kazi kwenye Simple Browser ya VS Code iliyojengwa ndani, kwani webview yake haitegemezi API ya ReadableStream. Ikiwa unatumia Simple Browser, vifungo visivyo vya mtiririko bado vitafanya kazi kawaida — vifungo vya streaming peke yake vinaathirika. Fungua `http://localhost:8083` kwenye kivinjari cha nje kwa uzoefu kamili.

### Chochote Kidogo vs Kito Kwa Ari

Uliza swali rahisi kama "Nini ni 15% ya 200?" ukitumia Low Eagerness. Utapata jibu la moja kwa moja, papo hapo. Sasa uliza jambo zito kama "Tengeneza mkakati wa kusajili kwa API yenye trafiki kubwa" ukitumia High Eagerness. Bonyeza **🔴 Rejea Mtiririko (Moja kwa Moja)** na tazama mfano ukielezea kwa kina hatua baada ya hatua. Mfano uleule, muundo uleule wa swali - lakini ombi linaeleza kiwango cha kufikiria kinachoendana.

### Utekelezaji wa Kazi (Utangulizi wa Zana)

Mchakato wa hatua nyingi unafaidika na upangaji wa awali na usimulizi wa maendeleo. Mfano unaonyesha atakachofanya, unasimulia kila hatua, kisha husisitiza matokeo.

### Msimbo Unaojitathmini

Jaribu "Tengeneza huduma ya uthibitisho wa barua pepe". Badala ya tu kuzalisha msimbo na kusitisha, mfano huunda, hukadiria kwa vigezo vya ubora, hutambua udhaifu, na kuboresha. Utaona ukirudia hadi msimbo ukidhi viwango vya uzalishaji.

### Uchambuzi wa Muundo

Mapitio ya msimbo yanahitaji mfumo thabiti wa tathmini. Mfano huchambua msimbo kwa kutumia vikundi vilivyowekwa (uhakika, mazoea, utendaji, usalama) kwa viwango vya ukali.

### Mazungumzo ya Mizunguko Mingi

Uliza "Spring Boot ni nini?" kisha moja kwa moja fuata na "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na anakupa mfano wa Spring Boot mahususi. Bila kumbukumbu, swali la pili lingewezekana kusahaulika sana.

### Ufafanuzi Hatua kwa Hatua

Chagua tatizo la hisabati na ujaribu kwa Hatua kwa Hatua na Low Eagerness. Low eagerness hutupia jibu tu - haraka lakini kutobainika. Hatua kwa hatua huonyesha kila hesabu na uamuzi.

### Matokeo Yaliyokomaliwa

Unapohitaji miundo maalum au hesabu ya maneno, mfano huu hutekeleza masharti thabiti. Jaribu kuzalisha muhtasari kwa maneno 100 kwa muundo wa vidokezo.

## Unachojifunza Kweli

**Juhudi za Kufikiria Hubadilisha Kila Kitu**

GPT-5.2 inakuwezesha kudhibiti juhudi za kihesabu kupitia prompt zako. Juhudi ndogo zinamaanisha majibu ya haraka na uchunguzi mdogo. Juhudi kubwa zinamaanisha mfano unachukua muda kufikiri kwa kina. Unajifunza kuendana na ugumu wa kazi - usipate muda kwa maswali rahisi, lakini pia usikimbilie maamuzi magumu.

**Muundo Huelekeza Tabia**

Umeona vitambulisho vya XML katika prompt? Sio mapambo. Mifano hufuata maelekezo ya muundo kwa uhakika zaidi kuliko maandishi huru. Unapohitaji michakato ya hatua nyingi au mantiki tata, muundo husaidia mfano kufuatilia wapi ipo na nini kinakuja.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Muundo wa prompt uliopangwa vyema na sehemu wazi na upangaji wa mtindo wa XML*

**Ubora Kupitia Kujiangalia**

Mifano inayojitathmini hufanya vigezo vya ubora kuwa wazi. Badala ya kutegemea mfano " Kufanya haki", unaeleza kwa usahihi maana ya "haki": mantiki sahihi, kushughulikia makosa, utendaji, usalama. Mfano unaweza kisha kutathmini matokeo yake na kuboresha. Hii hubadilisha uzalishaji wa msimbo kuwa mchakato badala ya bahati nasibu.

**Muktadha Ni Mdogo**

Mazungumzo ya mizunguko mingi hufanya kazi kwa kujumuisha historia ya ujumbe kwa kila ombi. Lakini kuna kikomo - kila mfano una idadi ya tokeni ya juu kabisa. Kadri mazungumzo yanavyoongezeka, utahitaji mbinu za kuhifadhi muktadha muhimu bila kufikia kikomo hicho. Moduli hii inakuonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kufupisha, lini kusahau, na lini kupata tena.

## Hatua Zifuatayo

**Moduli Inayofuata:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Uelekeo:** [← Kabla: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Maelezo ya Hati**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kuwa tafsiri za moja kwa moja zinaweza kuwa na makosa au kasoro. Hati ya asili katika lugha yake ya kichawi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatubeba dhamana kwa kutoelewana au tafsiri potofu zitokanazo na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->