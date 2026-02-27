# Moduli 02: Uhandisi wa Maagizo na GPT-5.2

## Orodha ya Maudhui

- [Maelezo ya Video](../../../02-prompt-engineering)
- [Utajifunza Nini](../../../02-prompt-engineering)
- [Mahitaji ya Awali](../../../02-prompt-engineering)
- [Kuelewa Uhandisi wa Maagizo](../../../02-prompt-engineering)
- [Misingi ya Uhandisi wa Maagizo](../../../02-prompt-engineering)
  - [Kuagiza Bila Mifano](../../../02-prompt-engineering)
  - [Kuagiza kwa Mifano Michache](../../../02-prompt-engineering)
  - [Mnyororo wa Mawazo](../../../02-prompt-engineering)
  - [Kuagiza Kulingana na Nafasi](../../../02-prompt-engineering)
  - [Mifanano ya Maagizo](../../../02-prompt-engineering)
- [Mifumo ya Juu Zaidi](../../../02-prompt-engineering)
- [Kutumia Rasilimali Zilizopo Azure](../../../02-prompt-engineering)
- [Picha za Matumizi ya Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Hamasa Ndogo vs Hamasa Kubwa](../../../02-prompt-engineering)
  - [Utekelezaji wa Kazi (Utangulizi wa Zana)](../../../02-prompt-engineering)
  - [Msimbo wa Kujitathmini](../../../02-prompt-engineering)
  - [Uchambuzi wa Muundo](../../../02-prompt-engineering)
  - [Mazungumzo ya Marudio Mengi](../../../02-prompt-engineering)
  - [Ushauri Hatua kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yenye Mipaka](../../../02-prompt-engineering)
- [Unajifunza Kweli Nini](../../../02-prompt-engineering)
- [Hatua Zifuatayo](../../../02-prompt-engineering)

## Maelezo ya Video

Tazama kipindi hiki cha moja kwa moja kinachoelezea jinsi ya kuanzisha na moduli hii: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Utajifunza Nini

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Katika moduli iliyopita, uliona jinsi kumbukumbu inavyoweza kuwezesha AI ya mazungumzo na kutumia Modeli za GitHub kwa maingiliano ya msingi. Sasa tutazingatia jinsi unavyouliza maswali — maagizo yenyewe — kwa kutumia GPT-5.2 ya Azure OpenAI. Jinsi unavyopanga maagizo yako huathiri sana ubora wa majibu unayopokea. Tunaanzia kwa kukagua mbinu za msingi za kuagiza, kisha tunaingia katika mifumo nane ya juu zaidi inayotumia uwezo kamili wa GPT-5.2.

Tutatumia GPT-5.2 kwa sababu inatambulisha udhibiti wa hoja - unaweza kusema kwa mfano kiasi gani cha kufikiri kinapaswa kufanywa kabla ya kujibu. Hii huifanya mikakati tofauti ya kuagiza ionekane vizuri zaidi na kukusaidia kuelewa lini utumie kila njia. Pia tutafaidika na mipaka michache ya kiwango ya Azure kwa GPT-5.2 ikilinganishwa na Modeli za GitHub.

## Mahitaji ya Awali

- Umekamilisha Moduli 01 (Rasilimali za Azure OpenAI zimesambazwa)
- Faili `.env` katika saraka kuu yenye vyeti vya Azure (iliyoandaliwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujakamilisha Moduli 01, fuata maagizo ya usambazaji huko kwanza.

## Kuelewa Uhandisi wa Maagizo

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Uhandisi wa maagizo ni kuhusu kubuni maandishi ya pembejeo ambayo yanakupa matokeo unayohitaji mara kwa mara. Siyo tu kuuliza maswali - ni kuhusu kupanga maombi ili mfano uelewe hasa unachotaka na jinsi ya kuyatoa.

Fikiria kama kutoa maelekezo kwa mwenzako. "Tengeneza hitilafu" ni duni. "Tengeneza hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Mfumo wa lugha hufanya kazi kwa njia ile ile - maalum na muundo ni muhimu.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j hutoa miundombinu — muunganisho wa modeli, kumbukumbu, na aina za ujumbe — wakati mifumo ya maagizo ni maandishi yaliyopangwa kwa umakini unaotumwa kupitia miundombinu hiyo. Vitu muhimu ni `SystemMessage` (ambayo inaweka tabia na nafasi ya AI) na `UserMessage` (ambayo huleta ombi lako halisi).

## Misingi ya Uhandisi wa Maagizo

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Kabla ya kuingia kwenye mifumo ya juu ya moduli hii, hebu pitia mbinu tano za msingi za kuagiza. Hizi ni zana msingi ambazo kila mhandisi wa maagizo anapaswa kujua. Ikiwa tayari umefanya kazi kupitia [moduli ya Mwanzo wa Haraka](../00-quick-start/README.md#2-prompt-patterns), umeona hizi zikifanyakazi — hapa ni mfumo wa dhana nyuma yao.

### Kuagiza Bila Mifano

Njia rahisi kabisa: toa maagizo ya moja kwa moja bila mifano yoyote. Mfano hutegemea kabisa mafunzo yake kuelewa na kutekeleza kazi. Hii hufanya kazi vizuri kwa maombi rahisi ambapo tabia inayotarajiwa ni dhahiri.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Maagizo ya moja kwa moja bila mifano — mfano hutambua kazi kutoka kwa maelekezo tu*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Jibu: "Chanya"
```

**Ni lini utumie:** Uainishaji rahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote ambayo mfano unaweza kushughulikia bila mwongozo wa ziada.

### Kuagiza kwa Mifano Michache

Toa mifano inayoonyesha muundo unaotaka mfano ufuate. Mfano hujifunza muundo wa pembejeo-toleo kutoka kwa mifano yako na kuutumia kwa pembejeo mpya. Hii huongeza uthabiti kwa kiasi kikubwa kwa kazi ambapo muundo au tabia inayotakiwa si ya dhahiri.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Kujifunza kutoka kwa mifano — mfano hutambua muundo na kuutumia kwa pembejeo mpya*

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

**Ni lini utumie:** Uainishaji maalum, uundaji thabiti, kazi maalum za sekta, au wakati matokeo ya zero-shot hayana uthabiti.

### Mnyororo wa Mawazo

Muulize mfano aonyesha hoja zake hatua kwa hatua. Badala ya kuruka moja kwa moja kwenye jibu, mfano hugawanya tatizo na kufanya kazi kila sehemu waziwazi. Hii huongeza usahihi katika hisabati, mantiki, na kazi za hoja za hatua nyingi.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Hoja hatua kwa hatua — kugawanya matatizo magumu katika hatua za mantiki za wazi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 tufaha
```

**Ni lini utumie:** Matatizo ya hisabati, fumbo za mantiki, urekebishaji wa hitilafu, au kazi yoyote ambapo kuonyesha mchakato wa hoja huongeza usahihi na imani.

### Kuagiza Kulingana na Nafasi

Weka persona au nafasi kwa AI kabla ya kuuliza swali lako. Hii hutoa muktadha unaoathiri toni, kina, na lengo la jibu. "Mbunifu wa programu" hutoa ushauri tofauti na "mwanafunzi mtarajiwa" au "mchunguzi wa usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kuweka muktadha na persona — swali lile lina jibu tofauti kulingana na nafasi iliyotengwa*

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

**Ni lini utumie:** Ukaguzi wa msimbo, ufundishaji, uchambuzi maalum wa sekta, au wakati unahitaji majibu yaliyoandaliwa kwa kiwango fulani cha utaalamu au mtazamo.

### Mifanano ya Maagizo

Tengeneza maagizo yanayoweza kutumika tena yenye vifungu vya kubadilika. Badala ya kuandika agizo jipya kila wakati, fafanua kifuniko mara moja na ujaze thamani tofauti. Darasa la LangChain4j `PromptTemplate` linafanya hili kuwa rahisi kwa sintaksia ya `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Maagizo yanayotumika tena yenye vifungu vya kubadilika — kifuniko kimoja, matumizi mengi*

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

**Ni lini utumie:** Maswali yanayojirudia yenye pembejeo tofauti, usindikaji wa kundi, ujenzi wa mtiririko wa AI unaotumika tena, au hali yoyote ambapo muundo wa agizo unabaki ule ule lakini data hubadilika.

---

Misingi hii mitano inakupa zana thabiti kwa kazi nyingi za kuagiza. Mengine ya moduli hii hujengwa juu yake kwa **mifumo nane ya hali ya juu** inayotumia udhibiti wa hoja wa GPT-5.2, kujitathmini, na uwezo wa kutoa matokeo yaliyopangwa.

## Mifumo ya Juu Zaidi

Baada ya kufunika misingi, hebu tuende kwenye mifumo nane ya hali ya juu inayofanya moduli hii kuwa ya kipekee. Siyo matatizo yote yanahitaji njia ile ile. Maswali mengine yanahitaji majibu ya haraka, mengine yanahitaji kufikiri kwa kina. Baadhi yanahitaji hoja inayoonekana, mengine yanahitaji matokeo tu. Kila mfumo hapa chini umeboreshwa kwa hali tofauti — na udhibiti wa hoja wa GPT-5.2 huifanya tofauti ziwe wazi zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Muhtasari wa mifumo minane ya uhandisi wa maagizo na matumizi yake*

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Udhibiti wa hoja wa GPT-5.2 hukuruhusu kubainisha kiasi cha kufikiri mfano unapaswa kufanya — kutoka majibu ya moja kwa moja yenye kasi hadi uchunguzi wa kina*

**Hamasa Ndogo (Haraka & Imebeba Lengo)** - Kwa maswali rahisi unayotaka majibu ya haraka na ya moja kwa moja. Mfano hufanya hoja kidogo - hatua za juu 2. Tumia hii kwa mahesabu, utafutaji, au maswali ya moja kwa moja.

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
> - "Tofauti kati ya hamasa ndogo na hamasa kubwa katika mifumo ya kuagiza ni gani?"
> - "Jedwali za XML katika maagizo husaidiaje kupanga jibu la AI?"
> - "Ni lini nitumie mifumo ya kujitathmini dhidi ya maagizo ya moja kwa moja?"

**Hamasa Kubwa (Kina & Zaidi ya Kila Kitu)** - Kwa matatizo magumu unayotaka uchambuzi wa kina. Mfano huchunguza kwa kina na kuonyesha hoja za kina. Tumia hii kwa muundo wa mifumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utekelezaji wa Kazi (Maendeleo Hatua kwa Hatua)** - Kwa mtiririko wa kazi za hatua nyingi. Mfano hutengeneza mpango wa awali, hueleza kila hatua anapofanya kazi, kisha hutoa muhtasari. Tumia hii kwa uhamisho, utekelezaji, au mchakato wowote wa hatua nyingi.

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

Kuagiza kwa mnyororo wa mawazo huagiza mfano aonyeshe mchakato wake wa hoja, huku huongeza usahihi kwa kazi ngumu. Ugawaji wa hatua kwa hatua husaidia watu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:
> - "Ningebadilishaje mfumo wa utekelezaji wa kazi kwa shughuli za muda mrefu?"
> - "Ni mbinu gani bora za kupanga utangulizi wa zana katika matumizi ya uzalishaji?"
> - "Ninawezaje kukamata na kuonyesha masasisho ya maendeleo kati ya hatua katika UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Mpango → Tekeleza → Fupisha mtiririko wa kazi kwa kazi za hatua nyingi*

**Msimbo wa Kujitathmini** - Kwa kuzalisha msimbo wa ubora wa uzalishaji. Mfano hutengeneza msimbo unaofuata viwango vya uzalishaji pamoja na udhibiti wa makosa. Tumia hii wakati wa kujenga sifa mpya au huduma.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Mzunguko wa kuboresha kwa mfululizo - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi wa Muundo** - Kwa tathmini thabiti. Mfano hupitia msimbo kwa kutumia mfumo thabiti (usalama, utendaji kazi, ufuatiliaji, uendelevu). Tumia hii kwa ukaguzi wa msimbo au tathmini za ubora.

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

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu uchambuzi wa muundo:
> - "Je, nawezaje kubinafsisha mfumo wa uchambuzi kwa aina tofauti za ukaguzi wa msimbo?"
> - "Njia bora za kusahihisha na kutenda kwa matokeo yaliyopangwa kwa njia ya programu ni zipi?"
> - "Ninawezaje kuhakikisha viwango thabiti vya ukali katika vikao tofauti vya ukaguzi?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Mfumo wa ukaguzi wa msimbo thabiti na viwango vya ukali*

**Mazungumzo ya Marudio Mengi** - Kwa mazungumzo yanayohitaji muktadha. Mfano hukumbuka ujumbe uliopita na kujenga juu yake. Tumia hii kwa vikao vya msaada vinavyoshirikiana au maswali na majibu magumu.

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

*Jinsi muktadha wa mazungumzo unavyojilimbikizia kwa marudio mengi hadi kufikia kikomo cha tokeni*

**Ushauri Hatua kwa Hatua** - Kwa matatizo yanayohitaji mantiki inayoonekana. Mfano huonyesha hoja wazi kwa kila hatua. Tumia hii kwa matatizo ya hisabati, fumbo za mantiki, au unapotaka kuelewa mchakato wa kufikiri.

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

*Kugawanya matatizo katika hatua za mantiki wazi*

**Matokeo Yenye Mipaka** - Kwa majibu yenye mahitaji maalum ya muundo. Mfano hufuata kwa ukali sheria za muundo na urefu. Tumia hii kwa muhtasari au unapohitaji muundo wa matokeo sahihi.

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

*Kutekeleza mahitaji maalum ya muundo, urefu, na muundo*

## Kutumia Rasilimali Zilizopo Azure

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo katika saraka kuu yenye vyeti vya Azure (vilivyoundwa wakati wa Moduli 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendeshwa kwenye bandari 8083. Unaweza kupita amri za kuanza hapo chini na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Dashibodi ya Spring Boot (Inapendekezwa kwa watumiaji wa VS Code)**
Kavazi ya msanidi inajumuisha nyongeza ya Spring Boot Dashboard, ambayo hutoa interface ya kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Ukanda wa Shughuli upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye eneo la kazi
- Anzisha/zimia programu kwa bonyeza moja tu
- Tazama logi za programu kwa wakati halisi
- Simamia hali ya programu

Bonyeza tu kitufe cha kucheza kando ya "prompt-engineering" kuanzisha moduli hii, au anzisha moduli zote kwa wakati mmoja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo la 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka katika saraka ya mzizi
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

Skripti zote mbili huingiza moja kwa moja vigezo vya mazingira kutoka kwenye faili la mizizi `.env` na zitajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mikono kabla ya kuanzisha:
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

Fungua http://localhost:8083 kwenye kivinjari chako.

**Kuzimia:**

**Bash:**
```bash
./stop.sh  # Huu moduli pekee
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Kizungi hiki tu
# Au
cd ..; .\stop-all.ps1  # Vizungi vyote
```

## Picha za Programu

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashibodi kuu ikionyesha mifumo 8 ya uhandisi wa prompt pamoja na sifa zao na matumizi yao*

## Kuchunguza Mifumo

Interface ya wavuti inakuwezesha kujaribu mikakati tofauti ya kuamsha. Kila mfumo unatatua matatizo tofauti - jaribu kuona lini kila mbinu huangaza.

> **Kumbuka: Streaming vs Non-Streaming** — Kila ukurasa wa mfumo hutoa vifungo viwili: **🔴 Mtiririko wa Majibu (Moja kwa moja)** na chaguo la **Isiyo ya mtiririko**. Mtiririko hutumia Matukio Yanayotolewa na Server (SSE) kuonyesha tokeni moja kwa moja wakati mfano unazizalisha, hivyo unaona maendeleo mara moja. Chaguo isiyo ya mtiririko husubiri jibu lote kabla ya kuonyesha. Kwa prompts zinazochochea uelewa wa kina (mfano, High Eagerness, Self-Reflecting Code), simu isiyo ya mtiririko inaweza kuchukua muda mrefu sana — wakati mwingine dakika — bila maoni yoyote ya kuonekana. **Tumia mtiririko unapojaribu prompts ngumu** ili uone mfano unavyofanya kazi na kuepuka hisia kwamba ombi limekwisha muda.
>
> **Kumbuka: Mahitaji ya Kivinjari** — Kipengele cha mtiririko kinatumia Fetch Streams API (`response.body.getReader()`) ambayo inahitaji kivinjari kamili (Chrome, Edge, Firefox, Safari). Hachafanyi kazi katika Kivinjari Rahisi kilichojengwa ndani ya VS Code, kwani webview yake haijiunga na API ya ReadableStream. Ukitumia Kivinjari Rahisi, vifungo vya isiyo ya mtiririko bado vitafanya kazi kama kawaida — tu vifungo vya mtiririko vinaathirika. Fungua `http://localhost:8083` kwenye kivinjari cha nje kwa uzoefu kamili.

### High vs Low Eagerness

Uliza swali rahisi kama "Nani ni 15% ya 200?" ukitumia Low Eagerness. Utapata jibu la moja kwa moja mara moja. Sasa uliza jambo tata kama "Tengeneza mkakati wa caching kwa API yenye trafiki kubwa" ukitumia High Eagerness. Bonyeza **🔴 Mtiririko wa Majibu (Moja kwa moja)** na tazama maelezo ya kina ya mfano yakionekana tokeni kwa tokeni. Mfano uleule, muundo uleule wa swali - lakini prompt inamuambia kiasi gani cha kufikiri kufanya.

### Utekelezaji wa Kazi (Tool Preambles)

Mchakato wa hatua nyingi unafaidika na upangaji wa awali na ufafanuzi wa maendeleo. Mfano huonyesha atakachofanya, huhadithi kila hatua, kisha hutoa muhtasari wa matokeo.

### Self-Reflecting Code

Jaribu "Tengeneza huduma ya uthibitishaji wa barua pepe". Badala ya kuunda tu nambari na kusimama, mfano huunda, huhakiki dhidi ya vigezo vya ubora, hutambua udhaifu, na kuboresha. Utaona ukirudia mpaka nambari ifikie viwango vya uzalishaji.

### Uchambuzi Uliyopangwa

Mapitio ya msimbo yanahitaji mifumo ya tathmini ya kuwa thabiti. Mfano huchambua msimbo kwa kutumia makundi maalum (usahihi, taratibu, utendaji, usalama) na viwango vya ukali.

### Mazungumzo ya Multi-Turn

Uliza "Nini Spring Boot?" kisha mara moja fuata na "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na anakupa mfano wa Spring Boot hasa. Bila kumbukumbu, swali la pili lingekuwa la jumla mno.

### Ufafanuzi wa Hatua kwa Hatua

Chagua tatizo la hisabati na lijaribu kwa Step-by-Step Reasoning na Low Eagerness. Low eagerness hutupa jibu tu - haraka lakini gumu kueleweka. Step-by-step inakuonyesha kila hesabu na uamuzi.

### Matokeo Yaliyodhibitiwa

Unapohitaji muundo maalum au idadi ya maneno, mfumo huu unafikia kufuata masharti magumu. Jaribu kuunda muhtasari wenye maneno 100 kabisa kwa muundo wa alama.

## Unavyojifunza Kifafanushi

**Juhudi za Uelewa Hubadilisha Yote**

GPT-5.2 inakuwezesha kudhibiti juhudi za kisaikolojia kupitia prompts zako. Juhudi ndogo inamaanisha majibu ya haraka yenye uchunguzi mdogo. Juhudi kubwa inamaanisha mfano hutumia muda kufikiri kwa kina. Unajifunza kulinganisha juhudi na ugumu wa kazi - usipoteze muda kwenye maswali rahisi, lakini pia usichangamke kwa maamuzi magumu.

**Muundo Huuongoza Tabia**

Umeshughulikia lebo za XML ndani ya prompts? Haizidi kutuwaza. Mifano hufuata maagizo yaliyoandaliwa vyema kwa kuaminika kuliko maandishi ya kawaida. Unapohitaji michakato ya hatua nyingi au mantiki tata, muundo husaidia mfano kufuatilia wapi yuko na nini kinakuja.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Muundo wa prompt uliopangwa vyema na sehemu wazi na mpangilio wa mtindo wa XML*

**Ubora Kupitia Kujitathmini**

Mifumo inayojitathmini inafanya vigezo vya ubora kuwa wazi. Badala ya kutegemea mfano "ufanye vizuri", unamuambia hasa maana ya "sahihi": mantiki sahihi, utunzaji wa makosa, utendaji, usalama. Mfano unaweza kisha kutathmini matokeo yake na kuboresha. Hii hubadilisha utengenezaji wa msimbo kuwa mchakato badala ya bahati nasibu.

**Muktadha Ni Mdogo**

Mazungumzo ya multi-turn hufanya kazi kwa kujumuisha historia ya ujumbe katika kila ombi. Lakini kuna kikomo - kila mfano una kipimo cha juu cha tokeni. Kadri mazungumzo yanavyoongezeka, utahitaji mikakati ya kuhifadhi muktadha unaohusika bila kufikia kikomo hicho. Moduli hii inakuonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kupitisha, lini kusahau, na lini kuchukua.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Uvinjari:** [← Iliyopita: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiarifa cha Kukataliwa**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya kutafsiri kwa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba utafsiri otomatiki unaweza kuwa na makosa au upungufu wa usahihi. Nyaraka ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo halali. Kwa taarifa muhimu, tafsiri ya kitaalamu kutoka kwa mtafsiri wa binadamu inashauriwa. Hatubebeki dhamana kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->