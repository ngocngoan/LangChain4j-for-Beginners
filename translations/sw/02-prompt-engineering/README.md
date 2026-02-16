# Moduli 02: Uhandisi wa Prompt na GPT-5.2

## Jedwali la Yaliyomo

- [Utakayo Jifunza](../../../02-prompt-engineering)
- [Mambo ya Kuzijua Kabla](../../../02-prompt-engineering)
- [Kuelewa Uhandisi wa Prompt](../../../02-prompt-engineering)
- [Misingi ya Uhandisi wa Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Mifumo ya Juu Zaidi](../../../02-prompt-engineering)
- [Kutumia Rasilimali Zilizopo za Azure](../../../02-prompt-engineering)
- [Picha za Maonyesho ya Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Kiukutani Kidogo vs Kikubwa](../../../02-prompt-engineering)
  - [Utekelezaji wa Kazi (Utangulizi wa Zana)](../../../02-prompt-engineering)
  - [Msimbo wa Kujitathmini](../../../02-prompt-engineering)
  - [Uchambuzi uliopangwa](../../../02-prompt-engineering)
  - [Mazungumzo ya Mizunguko Mingi](../../../02-prompt-engineering)
  - [Ufahamu wa Hatua Kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yaliyo Thibitishwa](../../../02-prompt-engineering)
- [Unachojifunza Kiwango Halisi](../../../02-prompt-engineering)
- [Hatua Zijazo](../../../02-prompt-engineering)

## Utakayo Jifunza

<img src="../../../translated_images/sw/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Katika moduli iliyopita, umeona jinsi kumbukumbu inavyowawezesha AI ya mazungumzo na kutumia Mifano ya GitHub kwa maingiliano ya msingi. Sasa tutazingatia jinsi unavyouliza maswali — prompts yenyewe — kwa kutumia GPT-5.2 ya Azure OpenAI. Njia unavyojenga prompts zako inaathiri sana ubora wa majibu unayopata. Tunaanza na mapitio ya mbinu za msingi za prompting, kisha tunaenda kwenye mifumo nane ya juu zaidi inayotumia uwezo wa GPT-5.2 kikamilifu.

Tutatumia GPT-5.2 kwa sababu inaleta udhibiti wa ufahamu - unaweza kueleza kwa mfano kiasi gani cha kufikiri kinachopaswa kufanywa kabla ya kujibu. Hii hufanya mikakati tofauti ya prompting ionekane zaidi na inakusaidia kuelewa lini utumie kila mbinu. Pia tutafaidika na vikwazo vya chini vya viwango vya Azure kwa GPT-5.2 ikilinganishwa na Mifano ya GitHub.

## Mambo ya Kuzijua Kabla

- Umehitimu Moduli 01 (Rasilimali za Azure OpenAI zimewekwa)
- Faili `.env` iko kwenye saraka kuu yenye taarifa za Azure (iliundwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujamaliza Moduli 01, fuata maelekezo ya usakinishaji hapo kwanza.

## Kuelewa Uhandisi wa Prompt

<img src="../../../translated_images/sw/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Uhandisi wa prompt ni kuhusu kubuni maandishi ya kuingiza ambayo yanakuletea matokeo unayohitaji mara kwa mara. Sio tu kuuliza maswali - ni kuhusu kuunda maombi ili mfano uelewe hasa unachotaka na jinsi ya kuileta.

Fikiria kama unampa maelekezo mwenzako. "Rekebisha hitilafu" ni neno la jumla. "Rekebisha hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Mifano ya lugha hufanya kazi sawa - ukamilifu na muundo ni muhimu.

<img src="../../../translated_images/sw/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j hutengeneza miundombinu — muunganisho wa mfano, kumbukumbu, na aina za ujumbe — wakati mifumo ya prompt ni maandishi yaliyoundwa kwa makini unayotuma kupitia miundombinu hiyo. Vitu muhimu ni `SystemMessage` (ambayo inaweka tabia na nafasi ya AI) na `UserMessage` (ambayo inabeba ombi lako halisi).

## Misingi ya Uhandisi wa Prompt

<img src="../../../translated_images/sw/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Kabla ya kuingia kwenye mifumo ya juu katika moduli hii, hebu pitia mbinu tano za msingi za prompting. Hizi ni vifaa vya msingi ambavyo kila mhandisi wa prompt anapaswa kujua. Ikiwa tayari umefanya kazi kupitia [Moduli ya Mwanzo Haraka](../00-quick-start/README.md#2-prompt-patterns), umeona hizi zikiendeshwa — hapa kuna muundo wa dhana nyuma yao.

### Zero-Shot Prompting

Njia rahisi kabisa: mpe mfano maagizo moja kwa moja bila mifano. Mfano hutegemea mafunzo yake kikamilifu kuelewa na kutekeleza kazi. Hii hufanya kazi vizuri kwa maombi rahisi ambapo mwenendo unaotarajiwa ni wazi.

<img src="../../../translated_images/sw/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Maagizo ya moja kwa moja bila mifano — mfano hutafsiri kazi kutoka kwa maagizo pekee*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Jibu: "Chanya"
```

**Lini utumie:** Kuelezea kwa urahisi, maswali ya moja kwa moja, tafsiri, au kazi yoyote mfano anaweza kushughulikia bila mwelekeo zaidi.

### Few-Shot Prompting

Toa mifano inayothibitisha muundo unaotaka mfano ufuate. Mfano hujifunza muundo wa pembejeo-toa toka kwa mifano yako na kulitumia kwa pembejeo mpya. Hii huongeza uthabiti kwa kazi ambapo muundo au mwenendo unaotakiwa hauonekani wazi.

<img src="../../../translated_images/sw/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Kujifunza kutoka kwa mifano — mfano hutambua muundo na kulitumia kwa pembejeo mpya*

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

**Lini utumie:** Uainishaji wa maalum, uundaji wa maandishi unaolingana, kazi za eneo mahususi, au wakati matokeo ya zero-shot hayazingani.

### Chain of Thought

Muulize mfano aonyeshe sababu zake hatua kwa hatua. Badala ya kuruka moja kwa moja kwa jibu, mfano huvunjika tatizo na kufanyia kila sehemu kwa uwazi. Hii huongeza usahihi kwa hisabati, mantiki, na kazi ngumu za hatua nyingi.

<img src="../../../translated_images/sw/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Sababu hatua kwa hatua — kuvunjika kwa matatizo magumu katika hatua za mantiki wazi*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Mfano unaonyesha: 15 - 8 = 7, kisha 7 + 12 = 19 tufaha
```

**Lini utumie:** Matatizo ya hisabati, fumbo za mantiki, utatuzi wa makosa, au kazi yoyote ambapo kuonyesha mchakato wa sababu huongeza usahihi na kuaminika.

### Role-Based Prompting

Weka persona au nafasi kwa AI kabla ya kuuliza swali lako. Hii hutoa muktadha unaoamua sauti, kina, na mtazamo wa jibu. "Mhandisi programu" hutoa ushauri tofauti na "mwanfunzi waani" au "mdhibiti usalama".

<img src="../../../translated_images/sw/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Kuweka muktadha na persona — swali moja hupata majibu tofauti kwa kulingana na nafasi iliyowekwa*

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

**Lini utumie:** Ukaguzi wa msimbo, kufundisha, uchambuzi wa eneo mahususi, au wakati unahitaji majibu yaliyobinafsishwa kwa kiwango fulani cha utaalamu au mtazamo.

### Prompt Templates

Tengeneza prompts zinazoweza kutumika tena na sehemu za kubadilika. Badala ya kuandika prompt mpya kila wakati, fafanua templeti mara moja kisha jaza thamani tofauti. Darasa la LangChain4j `PromptTemplate` linafanya hili kuwa rahisi kwa sintaksia ya `{{variable}}`.

<img src="../../../translated_images/sw/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompts zinazoweza kutumika tena na sehemu zinazobadilika — templeti moja, matumizi mengi*

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

**Lini utumie:** Maswali yanayojirudia mara kwa mara na pembejeo tofauti, usindikaji wa kundi, kujenga mizunguko ya AI inayoweza kutumika tena, au hali yoyote ambapo muundo wa prompt unabaki ule ule lakini data hubadilika.

---

Misingi mitano hii inakupa zana imara kwa kazi nyingi za prompting. Zilizobaki za moduli hii zinajenga juu yake kwa **mifumo nane ya juu zaidi** inayotumia udhibiti wa kufikiri wa GPT-5.2, kujitathmini, na uwezo wa matokeo yaliyopangwa.

## Mifumo ya Juu Zaidi

Baada ya kufunika misingi, twende kwa mifumo nane ya juu zaidi inayofanya moduli hii kuwa ya kipekee. Sio matatizo yote yanayohitaji njia ile ile. Maswali mengine yanahitaji majibu ya haraka, mengine yanahitaji kufikiri kwa kina. Baadhi yanahitaji dalili za ufahamu, mengine tu matokeo. Kila mfumo hapa chini umeboreshwa kwa hali tofauti — na udhibiti wa ufahamu wa GPT-5.2 hufanya tofauti hizi ionekane zaidi.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Muhtasari wa mifumo nane ya uhandisi wa prompt na matumizi yao*

<img src="../../../translated_images/sw/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Udhibiti wa ufahamu wa GPT-5.2 hukuwezesha kubainisha kiasi cha kufikiri mfano anapaswa kufanya — kutoka majibu ya moja kwa moja yenye kasi hadi uchunguzi wa kina*

<img src="../../../translated_images/sw/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Kiukutani Kidogo (haraka, moja kwa moja) vs Kiukutani Kikubwa (kikamilifu, kinachochunguza) mbinu za ufahamu*

**Kiukutani Kidogo (Haraka & Lenye Lengo)** - Kwa maswali rahisi ambapo unahitaji majibu ya haraka na ya moja kwa moja. Mfano hufanya ufahamu mdogo - hatua 2 tu kama kiwango cha juu. Tumia hii kwa mahesabu, kuangalia, au maswali rahisi.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Chunguza kwa GitHub Copilot:** Fungua [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) na uulize:
> - "Je, ni tofauti gani kati ya mifumo ya prompting ya kiukutani kidogo na kiukutani kikubwa?"
> - "Je, mabandiko ya XML katika prompts huwasaidiaje kupanga jibu la AI?"
> - "Ninahitaji kutumia lini mifumo ya kujitathmini vs maagizo ya moja kwa moja?"

**Kiukutani Kikubwa (Kina & Kinachochunguza)** - Kwa matatizo magumu ambapo unahitaji uchambuzi wa kina. Mfano huchunguza kwa kina na kuonyesha sababu za kina. Tumia hii kwa usanifu wa mifumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utekelezaji wa Kazi (Maendeleo Hatua kwa Hatua)** - Kwa mizunguko ya hatua nyingi. Mfano hutoa mpango wa awali, unasimulia kila hatua anapofanya kazi, kisha hutoa muhtasari. Tumia hii kwa uhamishaji, utekelezaji, au mchakato wowote wa hatua nyingi.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting huomba mfano kuonyesha mchakato wake wa ufahamu waziwazi, huongeza usahihi kwa kazi ngumu. Kuvunjika kwa hatua kwa hatua kunasaidia watu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:
> - "Nitabadilishaje mfumo wa utekelezaji wa kazi kwa ajili ya shughuli za muda mrefu?"
> - "Ni mbinu gani bora za kupanga utangulizi wa zana katika programu za uzalishaji?"
> - "Nawezaje kunasa na kuonyesha taarifa za maendeleo za kati kwenye UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Mpango → Tekeleza → Fupisha mchakato wa kazi za hatua nyingi*

**Msimbo wa Kujitathmini** - Kwa kuzalisha msimbo wa ubora wa uzalishaji. Mfano huzalisha msimbo, kuupima dhidi ya viwango vya ubora, na kuuboresha hatua kwa hatua. Tumia hii unapotengeneza vipengele au huduma mpya.

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

<img src="../../../translated_images/sw/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Mzunguko wa kuboresha kwa kurudia - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi uliopangwa** - Kwa tathmini ya uthabiti. Mfano hupitia msimbo kwa kutumia mfumo uliowekwa (usayisi, mbinu, utendaji, usalama). Tumia hii kwa ukaguzi wa msimbo au tathmini za ubora.

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

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu uchambuzi uliopangwa:
> - "Nawezaje kubinafsisha mfumo wa uchambuzi kwa aina tofauti za ukaguzi wa msimbo?"
> - "Ni njia gani bora ya kusoma na kutenda kwa matokeo yaliyopangwa kwa njia ya programu?"
> - "Ninahakikishaje viwango vya uzito viko sawa katika vikao vya ukaguzi tofauti?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Mfumo wa makundi manne kwa ukaguzi thabiti wa msimbo wenye viwango vya uzito*

**Mazungumzo ya Mizunguko Mingi** - Kwa mazungumzo yanayohitaji muktadha. Mfano unakumbuka ujumbe wa awali na kujenga juu yake. Tumia hii kwa vikao vya msaada wa kuingiliana au maswali na majibu magumu.

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

*Jinsi muktadha wa mazungumzo unavyojikusanya juu ya mizunguko mingi hadi kufikia kikomo cha tokeni*

**Ufahamu wa Hatua Kwa Hatua** - Kwa matatizo yanayohitaji mantiki wazi. Mfano huonyesha sababu wazi kwa kila hatua. Tumia hii kwa matatizo ya hisabati, fumbo za mantiki, au unapotaka kuelewa mchakato wa kufikiri.

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

*Kuvunjika kwa matatizo katika hatua za mantiki wazi*

**Matokeo Yaliyo Thibitishwa** - Kwa majibu yenye mahitaji maalum ya muundo. Mfano hufuata kwa ukali kanuni za muundo na urefu. Tumia hii kwa muhtasari au unapotaka muundo sahihi wa matokeo.

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

*Kufuata kanuni za muundo, urefu, na mahitaji ya muundo kwa usahihi*

## Kutumia Rasilimali Zilizopo za Azure

**Thibitisha usanikishaji:**

Hakikisha faili `.env` ipo kwenye saraka kuu yenye taarifa za Azure (iliyotengenezwa wakati wa Moduli 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendeshwa kwenye bandari 8083. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inayopendekezwa kwa watumiaji wa VS Code)**

Kontena la maendeleo linajumuisha ugani wa Spring Boot Dashboard, unaotoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kulipata kwenye Ukanda wa Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).
Kutoka kwenye Dashibodi ya Spring Boot, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana katika eneo la kazi
- Anzisha/zimia programu kwa kubofya mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fuatilia hali ya programu

Bonyeza tu kitufe cha kucheza kando ya "prompt-engineering" kuanzisha moduli hii, au anzisha moduli zote kwa wakati mmoja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia shell scripts**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwa saraka ya mzizi
.\start-all.ps1
```

Au anzisha moduli hii pekee:

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

Skripti zote mbili hujipakia kiotomatiki vigezo vya mazingira kutoka kwenye faili `.env` ya mzizi na zitaunda JARs kama hazipo.

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

Fungua http://localhost:8083 katika kivinjari chako.

**Kuzimia:**

**Bash:**
```bash
./stop.sh  # Hii moduli tu
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

*Dashibodi kuu ikionyesha mifumo 8 yote ya uhandisi wa prompt pamoja na sifa na matumizi yao*

## Kuchunguza Mifumo

Kiolesura cha wavuti hukuruhusu kujaribu mikakati tofauti ya kutoa maelekezo. Kila mfumo unasuluhisha matatizo tofauti - jaribu kuona lini kila mbinu huangaza.

### Udaraja wa Hamu Chini dhidi ya Juu

Uliza swali rahisi kama "Je, 15% ya 200 ni kiasi gani?" kwa kutumia Hamu Chini. Utapata jibu la haraka na moja kwa moja. Sasa uliza jambo gumu kama "Tengeneza mkakati wa caching kwa API yenye trafiki kubwa" kwa kutumia Hamu Juu. Tazama jinsi mfano unavyopungua kasi na kutoa maelezo ya kina. Mfano ule ule, muundo ule ule wa swali - lakini prompt inaeleza kiasi gani cha kufikiri kinapaswa kufanyika.

<img src="../../../translated_images/sw/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hesabu ya haraka na kufikiri kidogo*

<img src="../../../translated_images/sw/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Mkakati wa kina wa caching (2.8MB)*

### Utekelezaji wa Kazi (Utangulizi wa Zana)

Mchakato wa hatua nyingi unafaidika na upangaji wa awali na kueleza maendeleo. Mfano hueleza atakayofanya, husimulia kila hatua, kisha hutoa muhtasari wa matokeo.

<img src="../../../translated_images/sw/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Kutengeneza endpoint ya REST kwa kuonyesha hatua kwa hatua (3.9MB)*

### Msimamo wa Kujiangalia Msimbo

Jaribu "Tengeneza huduma ya uthibitishaji barua pepe". Badala ya tu kutengeneza msimbo na kuacha, mfano hutengeneza, hupima dhidi ya vigezo vya ubora, hutambua udhaifu, na kuboresha. Utaona ikirudia mchakato hadi msimbo ukidhi viwango vya uzalishaji.

<img src="../../../translated_images/sw/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Huduma kamili ya uthibitishaji barua pepe (5.2MB)*

### Uchambuzi wa Muundo

Mapitio ya msimbo yanahitaji mifumo thabiti ya tathmini. Mfano huchambua msimbo kwa kutumia makundi ya malengo (usalama, mazoea, utendaji, usalama) na viwango vya ukali.

<img src="../../../translated_images/sw/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Mapitio ya msimbo yanayotegemea mfumo*

### Mazungumzo ya Mizunguko Mingi

Uliza "Ni nini Spring Boot?" kisha mara moja fuata na "Nionyeshe mfano". Mfano hukumbuka swali lako la kwanza na anakupa mfano wa Spring Boot kwa kushindana. Bila kumbukumbu, swali la pili lingeweza kuwa la kukosa maelezo.

<img src="../../../translated_images/sw/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Uhifadhi wa muktadha katika maswali*

### Kufikiri Hatua kwa Hatua

Chagua tatizo la hesabu na lijaribu kwa kutumia Hamu ya Hatua kwa Hatua na Hamu Chini. Hamu ya chini hupitia jibu tu - haraka lakini haieleweki. Hatua kwa hatua huonyesha kila hesabu na uamuzi.

<img src="../../../translated_images/sw/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Tatizo la hesabu na hatua wazi*

### Matokeo Yaliyopigwa Mipaka

Unapohitaji miundo maalum au idadi ya maneno, mfumo huu unaweka utekelezaji mkali. Jaribu kutengeneza muhtasari wenye maneno 100 hasa kwa muundo wa pointi za risasi.

<img src="../../../translated_images/sw/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Muhtasari wa mashine learning na udhibiti wa muundo*

## Unachojifunza Kweli

**Juhudi za Kufikiri Hubadilisha Kila Kitu**

GPT-5.2 hukuruhusu kudhibiti juhudi za kihesabu kupitia prompts zako. Juhudi ndogo inamaanisha majibu ya haraka na uchunguzi mdogo. Juhudi kubwa inamaanisha mfano unachukua muda kufikiria kwa kina. Unajifunza kulingana juhudi na ugumu wa kazi - usipoteze muda kwa maswali rahisi, lakini pia usikimbize maamuzi magumu.

**Muundo Huelekeza Tabia**

Umeona lebo za XML katika maelekezo? Sio mapambo. Mifano hufuata maagizo yenye muundo kwa uaminifu zaidi kuliko maandishi huru. Unapohitaji michakato ya hatua nyingi au mantiki ngumu, muundo husaidia mfano kufuatilia wapi yuko na kinachofuata.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Muundo wa prompt ulio na sehemu wazi na mpangilio wa aina ya XML*

**Ubora Kupitia Kujipima**

Mifumo ya kujikagua yenyewe hufanya vigezo vya ubora kuwa wazi. Badala ya kutegemea mfano "ufanye vizuri", unaeleza hasa maana ya "sahihi": mantiki sahihi, usimamizi wa makosa, utendaji, usalama. Mfano kisha unaweza kutathmini matokeo yake na kuboresha. Hii hubadilisha uzalishaji wa msimbo kutoka bahati nasibu kuwa mchakato.

**Muktadha Una Kipimo**

Mazungumzo ya mizunguko mingi hufanya kazi kwa kujumuisha historia ya ujumbe kwa kila ombi. Lakini kuna kikomo - kila mfano una kikomo cha herufi (tokens). Kadiri mazungumzo yanavyokua, utahitaji mbinu za kuhifadhi muktadha muhimu bila kufikia kikomo hicho. Moduli hii inaonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kufupisha, lini kusahau, na lini kuchukua tena.

## Hatua Zifuatazo

**Moduli Inayofuata:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Utembeleaji:** [← Awali: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kukataliwa Kwa Mlolongo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu katika usahihi. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatuna dhamana kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->