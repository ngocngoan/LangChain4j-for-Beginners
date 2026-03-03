# Moduli 02: Uhandisi wa Maagizo na GPT-5.2

## Muhtasari wa Maudhui

- [Video ya Maelezo](../../../02-prompt-engineering)
- [Utajifunza Nini](../../../02-prompt-engineering)
- [Mahitaji ya Msingi](../../../02-prompt-engineering)
- [Kuelewa Uhandisi wa Maagizo](../../../02-prompt-engineering)
- [Misingi ya Uhandisi wa Maagizo](../../../02-prompt-engineering)
  - [Uagizo wa Zero-Shot](../../../02-prompt-engineering)
  - [Uagizo wa Few-Shot](../../../02-prompt-engineering)
  - [Mnyororo wa Mawazo](../../../02-prompt-engineering)
  - [Uagizo wa Kutoa Nafasi](../../../02-prompt-engineering)
  - [Mifano ya Maagizo](../../../02-prompt-engineering)
- [Mifumo ya Juu Zaidi](../../../02-prompt-engineering)
- [Endesha Programu](../../../02-prompt-engineering)
- [Picha za Skrini za Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Kujiamini Kidogo dhidi ya Kujiamini Kuu](../../../02-prompt-engineering)
  - [Utekelezaji wa Kazi (Utangulizi wa Zana)](../../../02-prompt-engineering)
  - [Msimbo unaojitathmini](../../../02-prompt-engineering)
  - [Uchambuzi uliopangwa](../../../02-prompt-engineering)
  - [Mazungumzo ya Mzunguko Mwingi](../../../02-prompt-engineering)
  - [Sababu Hatua kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yaliyodhibitiwa](../../../02-prompt-engineering)
- [Kile Unachojifunza Kweli](../../../02-prompt-engineering)
- [Hatua Zifuatazo](../../../02-prompt-engineering)

## Video ya Maelezo

Tazama kikao hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Uhandisi wa Maagizo na LangChain4j - Kikao cha Moja kwa Moja" width="800"/></a>

## Utajifunza Nini

Mchoro ufuatao unaonesha muhtasari wa mada kuu na ujuzi utakaojifunza katika moduli hii — kutoka mbinu za kusafisha maagizo hadi mtiririko wa hatua kwa hatua utakaoifuata.

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="Utajifunza Nini" width="800"/>

Katika moduli zilizopita, ulichunguza mwingiliano wa msingi wa LangChain4j na Modeli za GitHub na ukaona jinsi kumbukumbu inavyorahisisha AI ya mazungumzo na Azure OpenAI. Sasa tutaangazia jinsi unavyouliza maswali — maagizo yenyewe — ukitumia GPT-5.2 wa Azure OpenAI. Jinsi unavyoandika maagizo yako huathiri kwa kiwango kikubwa ubora wa majibu unayopata. Tunaanza kwa mapitio ya mbinu za msingi za maagizo, kisha tunaingia kwenye mifumo nane ya juu inayotumia uwezo kamili wa GPT-5.2.

Tutatumia GPT-5.2 kwa sababu inaleta udhibiti wa sababu - unaweza kuambia modeli ni kiasi gani cha kufikiria kufanywa kabla ya kujibu. Hii inafanya mbinu tofauti za maagizo ziwe wazi zaidi na inakusaidia kuelewa wapi unapaswa kutumia kila njia. Pia tutafaidika na vikwazo vidogo vya Azure kwa GPT-5.2 ikilinganishwa na Modeli za GitHub.

## Mahitaji ya Msingi

- Imekamilika Moduli 01 (Rasilimali za Azure OpenAI zimetumika)
- Faili `.env` katika saraka kuu yenye taarifa za Azure (iliundwa na `azd up` katika Moduli 01)

> **Kumbuka:** Kama hujakamilisha Moduli 01, fuata maelekezo ya utekelezaji hapo kwanza.

## Kuelewa Uhandisi wa Maagizo

Kwa msingi wake, uhandisi wa maagizo ni tofauti kati ya maelekezo yasiyoeleweka na yale yanayokuwa makini, kama inavyoonyeshwa kwenye mfano hapa chini.

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Nini Uhandisi wa Maagizo?" width="800"/>

Uhandisi wa maagizo ni kuhusu kubuni maandishi ya kuingiza ambayo yanakupatia matokeo unayohitaji kwa kufuata. Sio tu kuuliza maswali - ni jinsi ya kupanga maombi ili modeli iaelewe hasa unachotaka na jinsi ya kutoa majibu hayo.

Fikiria kama unatoa maagizo kwa mfanyakazi mwenzako. "Rekebisha hitilafu" ni maelekezo yasiyo wazi. "Rekebisha hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maelekezo maalum. Modeli za lugha zinavyofanya kazi ni hivyo hivyo - umakini na muundo ni muhimu.

Mchoro hapa chini unaonyesha jinsi LangChain4j inavyoweka picha hii wazi — ukiunganisha mifumo yako ya maagizo na modeli kupitia vipengele vya SystemMessage na UserMessage.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Jinsi LangChain4j Inavyofaa" width="800"/>

LangChain4j hutoa miundombinu — muunganisho wa modeli, kumbukumbu, na aina za ujumbe — wakati mifumo ya maagizo ni maandishi yaliyopangwa kwa makini unayotuma kupitia miundombinu hiyo. Viunganishi muhimu ni `SystemMessage` (ambayo inaweka tabia na nafasi ya AI) na `UserMessage` (ambayo huleta ombi lako halisi).

## Misingi ya Uhandisi wa Maagizo

Mbinu tano kuu zilizoonyeshwa hapa chini ni msingi wa uhandisi wa maagizo yenye ufanisi. Kila moja inashughulikia kipengele tofauti cha jinsi unavyozungumza na modeli za lugha.

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Muhtasari wa Mifumo Tano ya Uhandisi wa Maagizo" width="800"/>

Kabla ya kuingia kwenye mifumo ya juu katika moduli hii, tuchunguze mbinu tano za msingi za maagizo. Hizi ni vipengele vya msingi ambavyo kila mhandisi wa maagizo anapaswa kujua. Ikiwa tayari umefanya kazi kupitia [moduli ya Kuanzisha Haraka](../00-quick-start/README.md#2-prompt-patterns), umeona hizi zikitumika — hapa kuna mfumo wa dhana nyuma yao.

### Uagizo wa Zero-Shot

Mbinu rahisi kabisa: toa maagizo ya moja kwa moja kwa modeli bila mifano. Modeli hutegemea kabisa mafunzo yake kuelewa na kutekeleza kazi. Hii hufanya kazi vizuri kwa maombi rahisi ambapo tabia inayotarajiwa ni dhahiri.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Uagizo wa Zero-Shot" width="800"/>

*Maagizo ya moja kwa moja bila mifano — modeli husisitiza kazi kutoka kwa maagizo pekee*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Jibu: "Chanya"
```

**Linapopaswa kutumika:** Uainishaji rahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote modeli inaweza kushughulikia bila mwongozo zaidi.

### Uagizo wa Few-Shot

Toa mifano inayoonyesha muundo unayotaka modeli ifuate. Modeli hujifunza muundo wa pembejeo na matokeo kutokana na mifano yako na kuitumia kwa pembejeo mpya. Hii huongeza uthabiti kwa kazi ambapo muundo unaotakiwa au tabia si wazi.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Uagizo wa Few-Shot" width="800"/>

*Kujifunza kutoka kwa mifano — modeli hutambua muundo na kuitumia kwa pembejeo mpya*

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

**Linapopaswa kutumika:** Uainishaji wa kawaida, miundo thabiti, kazi maalum za kikoa, au wakati matokeo ya zero-shot hayako thabiti.

### Mnyororo wa Mawazo

Muulize modeli ionyeshe mchakato wake wa kufikiria hatua kwa hatua. Badala ya kuruka hadi jibu moja kwa moja, modeli hugawanya tatizo na kufanya kazi kupitia kila sehemu kwa uwazi. Hii huongeza usahihi kwa hesabu, mantiki, na kazi za kufikiria hatua nyingi.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Uagizo wa Mnyororo wa Mawazo" width="800"/>

*Sababu hatua kwa hatua — kugawanya matatizo magumu katika hatua wazi za mantiki*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 apple
```

**Linapopaswa kutumika:** Matatizo ya Hisabati, fumbo za mantiki, kuchangua kosa, au kazi yoyote ambapo kuonyesha mchakato wa sababu huongeza usahihi na kuaminiwa.

### Uagizo wa Kutoa Nafasi

Weka tabia au nafasi kwa AI kabla ya kuuliza swali lako. Hii hutoa muktadha unaoathiri tone, kina, na mkazo wa jibu. "Mbunifu wa programu" hutoa ushauri tofauti kuliko "mwanafunzi mdogo" au "mkaguzi wa usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Uagizo wa Kutoa Nafasi" width="800"/>

*Kuweka muktadha na nafasi — swali moja hupata majibu tofauti kulingana na nafasi iliyotolewa*

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

**Linapopaswa kutumika:** Ukaguzi wa msimbo, ufundishaji, uchambuzi maalum wa kikoa, au wakati unahitaji majibu yaliyojaa kwa kiwango fulani cha utaalamu au mtazamo.

### Mifano ya Maagizo

Tengeneza maagizo yanayotumika tena yenye nafasi za kuhifadhi tofauti. Badala ya kuandika agizo jipya kila wakati, fafanua mfano mara moja na ujaze maadili tofauti. Darasa la LangChain4j `PromptTemplate` hufanya hili kuwa rahisi kwa sintaksia ya `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Mifano ya Maagizo" width="800"/>

*Maagizo yanayotumika tena yenye nafasi za kuhifadhi — mfano mmoja, matumizi mengi*

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

**Linapopaswa kutumika:** Maswali yanayoarudiwa na pembejeo tofauti, usindikaji wa kundi, ujenzi wa mitiririko ya AI inayotumika tena, au hali yoyote ambapo muundo wa agizo hubaki sawa lakini data hubadilika.

---

Misingi hii mitano inakupa zana thabiti kwa kazi nyingi za maagizo. Mengine ya moduli hii hujengwa juu yake na **mifumo nane ya juu zaidi** inayotumia udhibiti wa sababu wa GPT-5.2, kujitathmini, na uwezo wa kutoa matokeo yaliyopangwa.

## Mifumo ya Juu Zaidi

Baada ya msingi kufunikwa, tuingie katika mifumo nane ya juu zaidi inayofanya moduli hii kuwa ya kipekee. Sio matatizo yote yanahitaji njia moja. Baadhi ya maswali yanahitaji majibu ya haraka, mengine yanahitaji kufikiri kwa kina. Baadhi yanahitaji sababu inayoonekana, mengine yanahitaji tu matokeo. Kila mfumo hapa chini umeboreshwa kwa hali tofauti — na udhibiti wa sababu wa GPT-5.2 hufanya tofauti hizi zionekane zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Mifumo Nane ya Uhandisi wa Maagizo" width="800"/>

*Muhtasari wa mifumo nane ya uhandisi wa maagizo na matumizi yake*

GPT-5.2 inaongeza kipengele kingine kwa mifumo hii: *udhibiti wa sababu*. Shuka hapa chini unaonyesha jinsi unavyoweza kurekebisha jitihada za kufikiri za modeli — kutoka majibu ya haraka, ya moja kwa moja hadi uchambuzi wa kina na wa kina.

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Udhibiti wa Sababu na GPT-5.2" width="800"/>

*Udhibiti wa sababu wa GPT-5.2 unakuwezesha kubainisha kiasi cha kufikiri modeli inapaswa kufanya — kutoka majibu ya haraka ya moja kwa moja hadi uchunguzi wa kina*

**Kujiamini Kidogo (Haraka & Lengo) -** Kwa maswali rahisi ambapo unataka majibu ya haraka na ya moja kwa moja. Modeli hufanya sababu kidogo sana - hatua 2 pekee. Tumia hii kwa hesabu, utafutaji, au maswali rahisi.

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
> - "Tofauti kati ya mifumo ya kujiamini kidogo na kujiamini kuu ni nini?"
> - "Michoro ya XML katika maagizo inasaidiaje kupanga majibu ya AI?"
> - "Ninapopaswa kutumia mifumo ya kujitathmini binafsi dhidi ya maagizo ya moja kwa moja?"

**Kujiamini Kuu (Kina & Kina) -** Kwa matatizo magumu ambapo unahitaji uchambuzi kamili. Modeli inachunguza kwa kina na inaonyesha sababu za kina. Tumia hii kwa muundo wa mifumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utekelezaji wa Kazi (Mkilele wa Hatua kwa Hatua)** - Kwa mitiririko ya kazi yenye hatua nyingi. Modeli huweka mpango wa awali, huandika kila hatua anapofanya kazi, kisha hutumia muhtasari. Tumia hii kwa uhamishaji, utekelezaji, au michakato yenye hatua nyingi.

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

Uagizo wa Mnyororo wa Mawazo huuliza modeli kwa uwazi ionyeshe mchakato wake wa sababu, kuboresha usahihi kwa kazi ngumu. Mgawanyo wa hatua kwa hatua husaidia binadamu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:
> - "Ningebadilishaje mfumo wa utekelezaji wa kazi kwa shughuli zinazochukua muda mrefu?"
> - "Nini mbinu bora za kupanga utangulizi wa zana katika programu za uzalishaji?"
> - "Ninawezaje kurekodi na kuonyesha taarifa za maendeleo ya kati katika UI?"

Mchoro hapa chini unaelezea mtiririko wa Kuweka Mpango → Kutekeleza → Kutoa Muhtasari.

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Mtiririko wa Mfumo wa Utekelezaji wa Kazi" width="800"/>

*Mpango → Utekelezaji → Muhtasari kwa kazi zenye hatua nyingi*

**Msimbo unaojitathmini** - Kwa kuzalisha msimbo wa ubora wa uzalishaji. Modeli hutoa msimbo unaofuata viwango vya uzalishaji na usimamizi sahihi wa makosa. Tumia hii unapo tengeneza vipengele vipya au huduma.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Mchoro hapa chini unaonyesha mzunguko huu wa kuboresha kidogo kidogo — tengeneza, tathmini, tambua udhaifu, na boresha hadi msimbo ukidhi viwango vya uzalishaji.

<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Mzunguko wa Kujitathmini" width="800"/>

*Mzunguko wa kuboresha taratibu - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi Uliopangwa** - Kwa tathmini thabiti. Modeli hupitia msimbo kwa kutumia mfumo uliofungwa (usahihi, mbinu, utendaji, usalama, urahisi wa kudumisha). Tumia hii kwa ukaguzi wa msimbo au tathmini za ubora.

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
> - "Ninawezaje kubadilisha mfumo wa uchambuzi kwa aina tofauti za ukaguzi wa msimbo?"
> - "Njia bora ya kuchambua na kutenda kwa matokeo yaliyopangwa kiforodha ni ipi?"
> - "Ninadhani vipi kuhakikisha viwango vya ugumu viko sawa katika vikao tofauti vya ukaguzi?"

Mchoro ufuatao unaonyesha jinsi mfumo huu uliopangwa unavyopanga ukaguzi wa msimbo katika makundi thabiti yenye viwango vya ugumu.

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Mfumo wa Uchambuzi Uliopangwa" width="800"/>

*Mfumo wa ukaguzi wa msimbo thabiti wenye viwango vya ugumu*

**Mazungumzo ya Mzunguko Mwingi** - Kwa mazungumzo yanayohitaji muktadha. Modeli inakumbuka ujumbe wa awali na kujenga juu yake. Tumia hii kwa vikao vya msaada wa mwingiliano au maswali magumu.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Mchoro hapa chini unaona jinsi muktadha wa mazungumzo unavyokusanywa kwa kila mzunguko na jinsi unavyoendana na kikomo cha tokeni cha modeli.

<img src="../../../translated_images/sw/context-memory.dff30ad9fa78832a.webp" alt="Kumbukumbu ya Muktadha" width="800"/>

*Jinsi muktadha wa mazungumzo unavyokusanywa katika mizunguko mingi hadi kufikia kikomo cha tokeni*
**Uelewa Hatua kwa Hatua** - Kwa matatizo yanayohitaji mantiki inayoweza kuonekana. Mfano unaonyesha uelewa wazi kwa kila hatua. Tumia hili kwa matatizo ya hisabati, vitendawili vya mantiki, au unapohitaji kuelewa mchakato wa kufikiria.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Michoro ifuatayo inaonyesha jinsi mfano unavyovitenganisha matatizo katika hatua za mantiki zilizoandikwa na kuwekwa nambari.

<img src="../../../translated_images/sw/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Kupunguza matatizo katika hatua za mantiki zilizo wazi*

**Matokeo Yaliyopigwa Xaria** - Kwa majibu yenye mahitaji maalum ya muundo. Mfano hufuata kwa uangalifu kanuni za muundo na urefu. Tumia hili kwa muhtasari au unapohitaji muundo sahihi wa matokeo.

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

Michoro ifuatayo inaonyesha jinsi masharti yanavyoongoza mfano kutoa matokeo ambayo yanazingatia kwa ukamilifu muundo na mahitaji ya urefu.

<img src="../../../translated_images/sw/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Kutekeleza mahitaji maalum ya muundo, urefu, na muundo*

## Endesha Programu

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo kwenye saraka kuu ikiwa na vyeti vya Azure (vilivyotengenezwa wakati wa Moduli 01). Endesha hii kutoka kwenye saraka ya moduli (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka kwenye saraka kuu (kama ilivyoelezwa katika Moduli 01), moduli hii tayari inaendesha kwenye bandari 8083. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kontena la maendeleo linajumuisha ugani wa Spring Boot Dashboard, ambao hutoa interface ya kuona ili kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Upau wa Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).

Kutoka kwenye Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana kwenye eneo la kazi
- Kuanza/kusimamisha programu kwa bonyeza mara moja
- Kuona rekodi za programu kwa wakati halisi
- Kufuatilia hali ya programu

Bonyeza tu kitufe cha kuanzisha karibu na "prompt-engineering" kuanzisha moduli hii, au anza moduli zote mara moja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard katika VS Code — anzisha, simamisha, na fuatilia moduli zote kutoka mahali pamoja*

**Chaguo 2: Kutumia scripts za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwa saraka ya mizizi
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

Misingi yote huchukua moja kwa moja mabadiliko ya mazingira kutoka faili `.env` la saraka kuu na itajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote mwenyewe kabla ya kuanza:
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

**Kusimamisha:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Hii moduli tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Picha za Programu

Hapa ni interface kuu ya moduli ya uhandisi wa prompt, ambapo unaweza kujaribu mifumo nane kwa pamoja.

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashibodi kuu inayoonyesha mifumo yote 8 ya uhandisi wa prompt pamoja na sifa zao na matumizi yao*

## Kuchunguza Mifumo

Interface ya wavuti inakuwezesha kujaribu mbinu tofauti za kuweka prompt. Kila mfumo hutatua matatizo tofauti - jaribu kuona lini kila njia inafaa.

> **Kumbuka: Streaming dhidi ya Non-Streaming** — Kila ukurasa wa mfumo hutoa vitufe viwili: **🔴 Stream Response (Live)** na chaguo la **Non-streaming**. Streaming hutumia Matukio Yanayotumwa na Seva (SSE) kuonyesha tokeni kwa wakati halisi huku mfano ukiizalisha, hivyo unaona maendeleo mara moja. Chaguo la non-streaming linasubiri jibu lote kabla ya kuonyesha. Kwa prompt zinazochochea uelewa wa kina (mfano, High Eagerness, Self-Reflecting Code), simu isiyo ya streaming inaweza kuchukua muda mrefu sana — mara nyingine dakika — bila maoni yoyote yanayoonekana. **Tumia streaming unapo jaribu prompt tata** ili uone mfano ukiendelea na kuepuka hisia kwamba ombi limechelewa.
>
> **Kumbuka: Mahitaji ya Kivinjari** — Kipengele cha streaming kinatumia Fetch Streams API (`response.body.getReader()`) ambayo inahitaji kivinjari kamili (Chrome, Edge, Firefox, Safari). Hakifanyi kazi katika Kivinjari Rahisi kilichojengwa ndani ya VS Code, kwani webview yake haisaidii API ya ReadableStream. Ukitumia Kivinjari Rahisi, vitufe vya non-streaming bado vitafanya kazi kawaida — tu vitufe vya streaming vinaathirika. Fungua `http://localhost:8083` katika kivinjari cha nje kwa uzoefu kamili.

### Matamanio ya Chini dhidi ya Juu

Uliza swali rahisi kama "Ni asilimia 15 ya 200 ni kiasi gani?" ukitumia Matamanio ya Chini. Utapata jibu la haraka na moja kwa moja. Sasa uliza jambo tata kama "Buni mkakati wa caching kwa API yenye trafiki nyingi" ukitumia Matamanio ya Juu. Bonyeza **🔴 Stream Response (Live)** na kutazama maelezo ya mfano yakiibuka tokeni kwa tokeni. Mfano ule ule, muundo ule ule wa swali - lakini prompt inaeleza kiasi cha kufikiria kinachohitajika.

### Utekelezaji wa Kazi (Utangulizi wa Vifaa)

Mifumo ya hatua nyingi inafaidika na upangaji wa awali na kusimuliwa kwa maendeleo. Mfano unaeleza atakachofanya, unasimulia kila hatua, halafu hutoa muhtasari wa matokeo.

### Msimbo Unaotafakari Mwelekeo Wake

Jaribu "Tengeneza huduma ya kuthibitisha barua pepe". Badala ya tu kutengeneza msimbo na kusitisha, mfano hutengeneza, kutathmini dhidi ya vigezo vya ubora, kugundua udhaifu, na kuboresha. Utaona ukirudia mpaka msimbo ufanikiwe kwa viwango vya uzalishaji.

### Uchambuzi wa Kijanib

Mapitio ya msimbo yanahitaji mifumo thabiti ya kutathmini. Mfano unachambua msimbo ukitumia makundi maalum (usahihi, mbinu, utendaji, usalama) na viwango vya uzito.

### Maongezi ya Mazungumzo yenye Zamu Nyingi

Uliza "Spring Boot ni nini?" kisha moja kwa moja uliza "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na akutie mfano wa Spring Boot mahsusi. Bila kumbukumbu, swali la pili lingekuwa la mkanganyiko.

### Uelewa Hatua kwa Hatua

Chagua tatizo la hisabati na jaribu kwa Uelewa Hatua kwa Hatua na Matamanio ya Chini. Matamanio ya chini yanakupa jibu tu - haraka lakini si wazi. Uelewa hatua kwa hatua unaonyesha kila hesabu na uamuzi.

### Matokeo Yaliyopigwa Xaria

Unapohitaji muundo maalum au idadi ya maneno, mfumo huu unatekeleza utekelezwaji mkali. Jaribu kutengeneza muhtasari wenye maneno 100 kabisa kwa muundo wa pointi.

## Unachojifunza Kweli

**Jitihada za Uelewa Hubadilisha Kila Kitu**

GPT-5.2 inakuwezesha kudhibiti jitihada za kompyuta kupitia prompt zako. Jitihada kidogo maana yake majibu ya haraka kwa uchunguzi mdogo. Jitihada kubwa maana yake mfano hutumia muda kufikiria kwa kina. Unajifunza kulinganisha jitihada na ugumu wa kazi - usipoteze muda kwa maswali rahisi, lakini usikimbize maamuzi magumu pia.

**Muundo Unaongoza Tabia**

Tazama lebo za XML katika prompt? Sio mapambo. Mifano hufuata maagizo yaliyopangwa kwa muundo zaidi kwa kuaminika kuliko maandishi yasiyo rasmi. Unapohitaji michakato ya hatua nyingi au mantiki tata, muundo husaidia mfano kufuatilia anapoelekea wapi na kinachofuata. Michoro ifuatayo inavunjwa prompt iliyopangwa vizuri, ikionyesha jinsi lebo kama `<system>`, `<instructions>`, `<context>`, `<user-input>`, na `<constraints>` zinavyoratibu maagizo yako katika sehemu wazi.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi ya prompt iliyopangwa vizuri yenye sehemu wazi na muundo wa mtindo wa XML*

**Ubora Kupitia Tathmini Binafsi**

Mifumo inayotafakari kazi zake inafanya vigezo vya ubora kuwa wazi. Badala ya kutegemea mfano kufanyia "sahihi", unaeleza hasa maana ya "sahihi": mantiki sahihi, utawala wa makosa, utendaji, usalama. Mfano unaweza kisha kutathmini matokeo yake na kuboresha. Hii hubadilisha utengenezaji wa msimbo kutoka bahati nasibu hadi mchakato.

**Muktadha Ni Mdogo**

Mazungumzo yenye zamu nyingi hufanya kazi kwa kujumuisha historia ya ujumbe katika kila ombi. Lakini kuna kikomo - kila mfano una idadi kubwa ya tokeni zinazoweza kutumiwa. Kadiri mazungumzo yanavyozidi, utahitaji mikakati ya kuweka muktadha mzuri bila kufikia kikomo hicho. Moduli hii inaonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kufupisha, lini kusahau, na lini kutafuta.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [03-rag - RAG (Uzalishaji Ulioongeza Ushuru)](../03-rag/README.md)

---

**Muelekeo:** [← Iliyopita: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Taarifa ya Kukataa**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au upungufu wa usahihi. Hati asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutangazwa kuwajibika kwa kutokuelewana au makosa ya tafsiri yanayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->