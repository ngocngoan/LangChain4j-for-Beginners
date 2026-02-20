# Moduli 02: Uhandisi wa Maagizo na GPT-5.2

## Orodha ya Maudhui

- [Utajifunza Nini](../../../02-prompt-engineering)
- [Mahitaji ya Awali](../../../02-prompt-engineering)
- [Kufahamu Uhandisi wa Maagizo](../../../02-prompt-engineering)
- [Misingi ya Uhandisi wa Maagizo](../../../02-prompt-engineering)
  - [Maagizo ya Zero-Shot](../../../02-prompt-engineering)
  - [Maagizo ya Few-Shot](../../../02-prompt-engineering)
  - [Mnyororo wa Mawazo](../../../02-prompt-engineering)
  - [Maagizo Yanayotegemea Nafasi](../../../02-prompt-engineering)
  - [Violezo vya Maagizo](../../../02-prompt-engineering)
- [Mifumo ya Juu Zaidi](../../../02-prompt-engineering)
- [Kutumia Rasilimali Zilizopo za Azure](../../../02-prompt-engineering)
- [Picha za Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Mtiifu Mdogo vs Mtiifu Mkuu](../../../02-prompt-engineering)
  - [Utekelezaji wa Kazi (Utangulizi wa Zana)](../../../02-prompt-engineering)
  - [Kanuni Inayojiangalia](../../../02-prompt-engineering)
  - [Uchambuzi ulio Pangwa](../../../02-prompt-engineering)
  - [Mazungumzo ya Mara Nyingi](../../../02-prompt-engineering)
  - [Ufafanuzi Hatua kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yaliyodhibitiwa](../../../02-prompt-engineering)
- [Unachojifunza Kwa Ukweli](../../../02-prompt-engineering)
- [Hatua Zinazofuata](../../../02-prompt-engineering)

## Utajifunza Nini

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="Utajifunza Nini" width="800"/>

Katika moduli iliyopita, uliona jinsi kumbukumbu inavyoruhusu AI za mazungumzo na ulitumia Mifano ya GitHub kwa mwingiliano wa msingi. Sasa tutajikita kwenye jinsi unavyouliza maswali — maagizo yenyewe — ukitumia GPT-5.2 wa Azure OpenAI. Njia unavyoandaa maagizo yako inaathiri kwa kiasi kikubwa ubora wa majibu unayopata. Tunaanza na mapitio ya mbinu msingi za maagizo, kisha tunaendelea kwenye mifumo nane ya hali ya juu inayochukua fursa kamili ya uwezo wa GPT-5.2.

Tutatumia GPT-5.2 kwa sababu inaleta udhibiti wa sababu - unaweza kueleza mfano kiwango gani cha kufikiria kinapaswa kufanywa kabla ya kutoa jibu. Hii hufanya mbinu tofauti za maagizo kuwa dhahiri zaidi na husaidia kuelewa ni lini kutumia kila njia. Pia tutafaidika na vikwazo vya kiwango cha chini vya Azure kwa GPT-5.2 ikilinganishwa na Mifano ya GitHub.

## Mahitaji ya Awali

- Umemaliza Moduli 01 (rasilimali za Azure OpenAI zimeanzishwa)
- Faili `.env` katika saraka kuu yenye taarifa za Azure (iliyoundwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujamaliza Moduli 01, tafadhali fuata maelekezo ya usambazaji hapo kwanza.

## Kufahamu Uhandisi wa Maagizo

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Uhandisi wa Maagizo ni Nini?" width="800"/>

Uhandisi wa maagizo ni kuhusu kubuni maandishi ya kuingiza ambayo kila mara yatakupatia matokeo unayohitaji. Si tu kuuliza maswali - ni kuhusu kuunda maombi ili mfano uelewe hasa unachotaka na jinsi ya kuwasilisha.

Fikiria kama unatoa maelekezo kwa mfanyakazi mwenzako. "Rekebisha hitilafu" ni ya jumla. "Rekebisha hitilafu ya null pointer kwenye UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Mifano ya lugha hufanya kazi hivyo - umakini na muundo ni muhimu.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Inavyolingana" width="800"/>

LangChain4j hutoa miundombinu — muunganisho wa mifano, kumbukumbu, na aina za ujumbe — wakati mifumo ya maagizo ni maandishi yaliyopangwa kwa umakini unaotumwa kupitia miundombinu hiyo. Vitu muhimu ni `SystemMessage` (inayoanzisha tabia na nafasi ya AI) na `UserMessage` (inayoleta ombi lako halisi).

## Misingi ya Uhandisi wa Maagizo

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Muhtasari wa Mifano Mitano ya Uhandisi wa Maagizo" width="800"/>

Kabla ya kuingia kwenye mifumo ya hali ya juu katika moduli hii, tuchambue mbinu tano za msingi za maagizo. Hizi ni bricks za ujenzi ambazo kila mhandisi wa maagizo anapaswa kujua. Ikiwa tayari umefanya kazi kupitia [moduli ya Kuanzia Haraka](../00-quick-start/README.md#2-prompt-patterns), umeona haya yaliyotekelezwa — hapa ni mfumo wa mawazo nyuma yake.

### Maagizo ya Zero-Shot

Njia rahisi kabisa: toa maagizo moja kwa moja kwa mfano bila mifano. Mfano hutegemea mafunzo yake kutambua na kutekeleza kazi. Hii hufanya kazi vyema kwa maombi rahisi ambapo tabia inayotarajiwa ni dhahiri.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Maagizo ya Zero-Shot" width="800"/>

*Maagizo ya moja kwa moja bila mifano — mfano hutafsiri kazi kutoka kwa maagizo pekee*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Majibu: "Chanya"
```
  
**Wakati wa kutumia:** Uainishaji rahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote ambayo mfano unaweza kushughulikia bila mwongozo zaidi.

### Maagizo ya Few-Shot

Toa mifano inayoonyesha muundo unaotaka mfano ufuate. Mfano hujifunza miundo ya ingizo-toa kutoka kwa mifano yako na kuitumia kwa ingizo jipya. Hii huongeza uthabiti kwa kazi ambapo muundo au tabia inayotakiwa si dhahiri.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Maagizo ya Few-Shot" width="800"/>

*Kujifunza kutoka kwa mifano — mfano hutambua muundo na kuitumia kwa ingizo jipya*

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
  
**Wakati wa kutumia:** Uainishaji wa kawaida, muundo thabiti, kazi maalum kiwanja, au wakati matokeo ya zero-shot hayako thabiti.

### Mnyororo wa Mawazo

Muulize mfano aonyeshe sababu zake hatua kwa hatua. Badala ya kuruka moja kwa moja kwenye jibu, mfano huvunja tatizo na kufanyia kazi kila sehemu waziwazi. Hii huongeza usahihi kwa hesabu, mantiki, na kazi za kutatua hatua kwa hatua.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Maagizo ya Mnyororo wa Mawazo" width="800"/>

*Ufafanuzi hatua kwa hatua — kugawa matatizo magumu katika hatua za mantiki wazi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 tufaha
```
  
**Wakati wa kutumia:** Matatizo ya hesabu, fumbo za mantiki, kutatua makosa, au kazi yoyote ambapo kuonyesha mchakato wa kufikiria huongeza usahihi na imani.

### Maagizo Yanayotegemea Nafasi

Weka mtu au nafasi kwa AI kabla ya kuuliza swali. Hii hutoa muktadha unaobadilisha mtindo, kina, na lengo la jibu. "Mbunifu wa programu" hutoa ushauri tofauti na "mhandisi mdogo" au "mkaguzi wa usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Maagizo Yanayotegemea Nafasi" width="800"/>

*Kuweka muktadha na mtu — swali lile linapata jibu tofauti kulingana na nafasi iliyowekwa*

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
  
**Wakati wa kutumia:** Mapitio ya msimbo, ufundishaji, uchambuzi wa kiwanja maalum, au unapotaka majibu yaliyobinafsishwa kwa kiwango au mtazamo fulani wa utaalamu.

### Violezo vya Maagizo

Tengeneza maagizo yanayoweza kutumika tena yenye sehemu zinazobadilika. Badala ya kuandika agizo jipya kila mara, fafanua kiangalizo mara moja na jaza maadili tofauti. Darasa la `PromptTemplate` la LangChain4j hufanya hii iwe rahisi kwa sintaksia ya `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Violezo vya Maagizo" width="800"/>

*Maagizo yanayoweza kutumika tena yenye sehemu zinazobadilika — kiolezo kimoja, matumizi mengi*

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
  
**Wakati wa kutumia:** Maswali yanayorudiwa na viingilio tofauti, usindikaji kwa vikundi, ujenzi wa mtiririko wa AI unaoweza kutumika tena, au hali yoyote ambapo muundo wa agizo unabaki sawa lakini data hubadilika.

---

Misingi hii mitano inakupa zana imara kwa kazi nyingi za maagizo. Mifumo mingine katika moduli hii inaongeza hayo kwa **mifumo nane ya hali ya juu** inayotumia udhibiti wa sababu wa GPT-5.2, auto-evaluation, na uwezo wa kutoa matokeo yaliyo pangwa.

## Mifumo ya Juu Zaidi

Baada ya kufunika misingi, hebu tuhamie mifumo nane ya hali ya juu inayoyafanya moduli hii kuwa ya kipekee. Suala zote hazihitaji njia ile ile. Maswali baadhi yanahitaji majibu ya haraka, mengine mahitaji mawazo makubwa. Baadhi yanahitaji sababu inayojulikana, mengine matokeo tu. Kila mfumo hapa chini umeboreshwa kwa hali tofauti — na udhibiti wa sababu wa GPT-5.2 hufanya tofauti ziwe wazi zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Mifumo Saba ya Maagizo" width="800"/>

*Muhtasari wa mifumo nane ya uhandisi wa maagizo na matumizi yake*

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Udhibiti wa Sababu na GPT-5.2" width="800"/>

*Udhibiti wa sababu wa GPT-5.2 hukuwezesha kueleza kiwango cha kufikiria kinachotakiwa — kutoka majibu ya haraka hadi uchunguzi wa kina*

**Mtiifu Mdogo (Haraka & Walengwa)** - Kwa maswali rahisi unayotaka majibu ya haraka na ya moja kwa moja. Mfano hufanya fikira kidogo - hatua 2 za juu kabisa. Tumia hii kwa hesabu, utafutaji, au maswali ya moja kwa moja.

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
> - "Tofauti kati ya mifumo ya mtiifu mdogo na mtiifu mkuu ni ipi?"  
> - "Je, lebo za XML katika maagizo husaidiaje kuunda jibu la AI?"  
> - "Ni lini nitumie mifumo ya kujitathmini mwenyewe dhidi ya maagizo ya moja kwa moja?"

**Mtiifu Mkuu (Kina & Zaidi)** - Kwa matatizo magumu unayotaka uchambuzi wa kina. Mfano huchunguza kwa kufidia na kuonyesha sababu za kina. Tumia hii kwa muundo wa mfumo, uamuzi wa usanifu, au utafiti mgumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Utekelezaji wa Kazi (Maendeleo Hatua kwa Hatua)** - Kwa mitiririko ya kazi yenye hatua nyingi. Mfano hutoa mpango wa awali, unasimulia kila hatua anapofanya kazi, halafu hutupa muhtasari. Tumia hii kwa uhamishaji, utekelezaji, au mchakato wowote wenye hatua nyingi.

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
  
Maagizo ya Mnyororo wa Mawazo huuliza mfano aonyeshe mchakato wake wa kufikiria wazi, huongeza usahihi kwa kazi ngumu. Kugawanya hatua kwa hatua husaidia binadamu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:  
> - "Ningebadilishaje mfumo wa utekelezaji wa kazi kwa operesheni za muda mrefu?"  
> - "Je, ni mbinu gani bora za kuunda utangulizi wa zana katika programu za uzalishaji?"  
> - "Nawezaje kunasa na kuonyesha taarifa za maendeleo ya kati kwenye UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mfumo wa Utekelezaji wa Kazi" width="800"/>

*Mpango → Tekeleza → Muhtasari kwa kazi za hatua nyingi*

**Kanuni Inayojiangalia** - Kwa kuunda msimbo wa ubora wa uzalishaji. Mfano huzalisha msimbo unafuata viwango vya uzalishaji na usimamizi wa makosa ipasavyo. Tumia hii unapoanzisha vipengele vipya au huduma.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Mzunguko wa Kujitathmini" width="800"/>

*Mzunguko wa kuboresha mara kwa mara - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi ulio Pangwa** - Kwa tathmini thabiti. Mfano huangalia msimbo kwa kutumia muundo ulioamilishwa (usahihi, kanuni, utendaji, usalama, utunzaji). Tumia hii kwa mapitio ya msimbo au tathmini ya ubora.

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
> - "Njia bora ya kuchambua na kuchukua hatua kwenye matokeo yaliyopangwa ipi?"  
> - "Jinsi gani nahakikisha viwango vya ukali viko sawasawa kwenye vikao tofauti vya mapitio?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mfumo wa Uchambuzi ulio Pangwa" width="800"/>

*Muundo wa mapitio ya msimbo thabiti na viwango vya ukali*

**Mazungumzo ya Mara Nyingi** - Kwa mazungumzo yanayohitaji muktadha. Mfano hukumbuka ujumbe wa awali na kujenga juu yake. Tumia hii kwa vikao vya msaada wa mwingiliano au maswali na majibu magumu.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/sw/context-memory.dff30ad9fa78832a.webp" alt="Kumbukumbu ya Muktadha" width="800"/>

*Jinsi muktadha wa mazungumzo unavyojikusanya kwa mizunguko mingi hadi kufikia kikomo cha tokeni*

**Ufafanuzi Hatua kwa Hatua** - Kwa matatizo yanayohitaji mantiki inayoonekana. Mfano huonyesha sababu moja kwa moja kwa kila hatua. Tumia hii kwa matatizo ya hesabu, fumbo za mantiki, au wakati unahitaji kuelewa mchakato wa kufikiria.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sw/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Mfumo wa Hatua kwa Hatua" width="800"/>

*Kupanga matatizo katika hatua za mantiki wazi*

**Matokeo Yaliyodhibitiwa** - Kwa majibu yenye mahitaji maalum ya muundo. Mfano hufuata kwa ukali sheria za muundo na urefu. Tumia hii kwa muhtasari au unapotaka muundo sahihi wa matokeo.

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
  
<img src="../../../translated_images/sw/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Mfumo wa Matokeo Yaliyodhibitiwa" width="800"/>

*Kutekeleza mahitaji maalum ya muundo, urefu, na muundo*

## Kutumia Rasilimali Zilizopo za Azure

**Hakikisha usambazaji:**

Hakiki kuwa faili `.env` ipo katika saraka kuu yenye taarifa za Azure (iliyoundwa wakati wa Moduli 01):  
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendesha kwenye bandari 8083. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Dashibodi ya Spring Boot (Inapendekezwa kwa watumiaji wa VS Code)**

Sura ya dev container ina nyongeza ya Dashibodi ya Spring Boot, inayotoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Bar ya Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).

Kutoka Dashibodi ya Spring Boot, unaweza:  
- Kuona programu zote za Spring Boot zinazopatikana katika eneo la kazi  
- Anzisha/acha programu kwa click moja  
- Tazama logi za programu kwa wakati halisi  
- Fuata hali ya programu
Bonyeza tu kitufe cha kucheza karibu na "prompt-engineering" kuanza moduli hii, au anzisha moduli zote kwa pamoja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo la 2: Kutumia shell scripts**

Anzisha programu zote za mtandao (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka kuu
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka katika saraka kuu
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

Skripti zote mbili zinapakua moja kwa moja vigezo vya mazingira kutoka kwenye faili la mizizi `.env` na zitaunda JARs ikiwa hazipo.

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
.\stop.ps1  # Moduli hii tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Picha za Programu

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Kuweka dashibodi kuu inaonyesha mifumo yote 8 ya uhandisi wa prompt pamoja na sifa zao na matumizi*

## Kuchunguza Mifumo

Kiolesura cha wavuti hukuruhusu kujaribu mbinu tofauti za kuanzisha maombi. Kila mfumo unatatua matatizo tofauti - jaribu kuona lini kila mbinu huangaza.

### Hamasa Nafasi Ndogo vs Juu

Uliza swali rahisi kama "Ni asilimia ngapi 15% ya 200?" kwa kutumia Hamasa Nafasi Ndogo. Utapokea jibu la moja kwa moja papo hapo. Sasa uliza kitu kigumu kama "Tengeneza mkakati wa kuhifadhi data (caching) kwa API yenye trafiki kubwa" kwa kutumia Hamasa Nafasi Juu. Angalia jinsi mfano unavyochelewesha na kutoa sababu za kina. Mfano uleule, muundo uleule wa swali - lakini prompt inaeleza ni kiasi gani cha kufikiria kinahitajika.

<img src="../../../translated_images/sw/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hesabu ya haraka yenye sababu chache*

<img src="../../../translated_images/sw/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Mkakati wa kina wa kuhifadhi data (2.8MB)*

### Utekelezaji wa Kazi (Maelezo ya Zana)

Mchakato wa hatua nyingi unafaidika na upangaji wa awali na kufafanua maendeleo. Mfano huelezea atakachofanya, huandika kila hatua, kisha huhitimisha matokeo.

<img src="../../../translated_images/sw/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Kutengeneza endpoint ya REST kwa uandishi hatua kwa hatua (3.9MB)*

### Koodi Inayojitathmini

Jaribu "Tengeneza huduma ya kuthibitisha barua pepe". Badala ya kuzalisha koodi tu na kuacha, mfano huzalisha, kutathmini kulingana na vigezo vya ubora, kubaini mapungufu, na kuboresha. Utaona ikirudia mpaka koodi ifikie viwango vya uzalishaji.

<img src="../../../translated_images/sw/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Huduma kamili ya uthibitishaji barua pepe (5.2MB)*

### Uchambuzi Ulio Rasilimaliwa

Mapitio ya koodi yanahitaji mfumo thabiti wa tathmini. Mfano hutathmini koodi kwa kutumia makundi yaliyowekwa (usalama, mazoea, utendaji, usalama) kwa viwango vya ukali.

<img src="../../../translated_images/sw/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Mapitio ya koodi kwa mfumo wa makundi*

### Maongezi ya Mzunguko Mmoja Baada ya Mwengine

Uliza "Spring Boot ni nini?" kisha mara moja uliza "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na anakupa mfano wa Spring Boot mahsusi. Bila kumbukumbu, swali la pili lingekuwa wazi mno.

<img src="../../../translated_images/sw/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Uhifadhi wa muktadha kati ya maswali*

### Ufafanuzi wa Hatua kwa Hatua

Chagua tatizo la hesabu na lijiunge na Ufafanuzi wa Hatua kwa Hatua na Hamasa Nafasi Ndogo. Hamasa nafsi ndogo inakupa jibu tu - haraka lakini gumu kueleweka. Hatua kwa hatua hionyesha kila hesabu na uamuzi.

<img src="../../../translated_images/sw/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Tatizo la hesabu kwa hatua wazi*

### Matokeo Yenye Mipaka

Unapohitaji miundo maalum au idadi ya maneno, mfumo huu unalazimisha kufuata kwa makini. Jaribu kuzalisha muhtasari wenye maneno 100 haswa kwa njia ya vidokezo.

<img src="../../../translated_images/sw/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Muhtasari wa mashine kujifunza kwa udhibiti wa muundo*

## Kinachojifunza Kwako Kweli

**Juu ya Juhudi za Kufikiria Hubadilisha Kila Kitu**

GPT-5.2 hukuruhusu kudhibiti juhudi za kihesabu kupitia maelekezo yako. Juhudi ndogo ina maana majibu ya haraka na uchunguzi mdogo. Juhudi kubwa ina maana mfano hupata muda wa kufikiria kwa kina. Unajifunza kulinganisha juhudi na ugumu wa kazi - usipoteze muda kwa maswali rahisi, lakini pia usikimbilie maamuzi magumu.

**Muundo Husaidia Tabia**

Je, umeona lebo za XML katika maelekezo? Sio mapambo tu. Mifano hufuata maelekezo yaliyopangwa kwa uaminifu kuliko maandishi ya uhuru. Unapohitaji michakato ya hatua nyingi au mantiki ngumu, muundo husaidia mfano kufuatilia wapi yako na nini kinafuata.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Muundo wa kila sehemu na mpangilio wa mtindo wa XML wa maelekezo*

**Ubora Kupitia Kujitathmini**

Mifumo inayojitathmini inafanya vigezo vya ubora wazi. Badala ya kutegemea mfano "ufanye vizuri", unaelezea maana ya "bora": mantiki sahihi, kushughulikia makosa, utendaji, usalama. Mfano basi unaweza kutathmini mazao yake na kuboresha. Hii hubadilisha uzalishaji wa koodi kuwa mchakato wa uhakika zaidi.

**Muktadha Ni Mdogo**

Mazungumzo ya mzunguko mwingi hufanya kazi kwa kuingiza historia ya ujumbe kwa kila ombi. Lakini kuna kikomo - kila mfano una idadi kubwa ya tokeni. Kadri mazungumzo yanavyozidi, utahitaji mbinu za kuhifadhi muktadha muhimu bila kufikia kikomo hicho. Moduli hii inaonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kujumlisha, lini kusahau, na lini kupata tena.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Urambazo:** [← Iliyotangulia: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Onyo**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au ukosefu wa usahihi. Nyaraka ya asili katika lugha yake asilia inapaswa kuchukuliwa kama chanzo halali. Kwa taarifa muhimu, tafsiri ya kitaalamu inayotolewa na wanadamu inapendekezwa. Hatubeba dhamana kwa ukiukwaji wowote wa maana au tafsiri potofu zitakazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->