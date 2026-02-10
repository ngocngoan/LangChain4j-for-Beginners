# Module 02: Uhandisi wa Prompti na GPT-5.2

## Table of Contents

- [Utazojifunza](../../../02-prompt-engineering)
- [Mahitaji ya Awali](../../../02-prompt-engineering)
- [Kuelewa Uhandisi wa Prompti](../../../02-prompt-engineering)
- [Jinsi Hii Inavyotumia LangChain4j](../../../02-prompt-engineering)
- [Mifumo Mikuu](../../../02-prompt-engineering)
- [Kutumia Rasilimali Zaidi za Azure Zilizopo](../../../02-prompt-engineering)
- [Picha za Skrini za Programu](../../../02-prompt-engineering)
- [Kuchunguza Mifumo](../../../02-prompt-engineering)
  - [Hamasa Ndogo vs Hamasa Juu](../../../02-prompt-engineering)
  - [Utekeshaji wa Kazi (Utangulizi wa Vifaa)](../../../02-prompt-engineering)
  - [Msimbo wa Kujitathmini](../../../02-prompt-engineering)
  - [Uchambuzi wa Muundo](../../../02-prompt-engineering)
  - [Maongezi ya Mizunguko mingi](../../../02-prompt-engineering)
  - [Ufakirishaji Hatua kwa Hatua](../../../02-prompt-engineering)
  - [Matokeo Yaliyopigwa Hati](../../../02-prompt-engineering)
- [Unajifunza Kweli Nini](../../../02-prompt-engineering)
- [Hatua Zifuatazo](../../../02-prompt-engineering)

## Utazojifunza

Katika moduli iliyopita, ukaona jinsi kumbukumbu inavyowezesha AI ya mazungumzo na ukatumia Modeli za GitHub kwa mwingiliano wa msingi. Sasa tutalenga jinsi unavyouliza maswali - prompti zenyewe - ukitumia GPT-5.2 ya Azure OpenAI. Njia unavyopanga prompti zako inaathiri kwa kiasi kikubwa ubora wa majibu unayopata.

Tutatumia GPT-5.2 kwa sababu inaleta udhibiti wa ufakirishaji - unaweza kumuambia modeli ni kiasi gani cha kufikiri kabla ya kujibu. Hii inafanya mbinu tofauti za kuprompt kuwa wazi zaidi na inakusaidia kuelewa lini utumie kila njia. Pia tutafaidika na ukomo mdogo wa viwango vya Azure kwa GPT-5.2 ikilinganishwa na Modeli za GitHub.

## Mahitaji ya Awali

- Kumaliza Module 01 (Rasilimali za Azure OpenAI zimewekwa)
- Faili `.env` katika mzizi wa saraka yenye usajili wa Azure (iliyotengenezwa na `azd up` katika Module 01)

> **Kumbuka:** Ikiwa hujamaliza Module 01, fuata maelekezo ya usambazaji huko kwanza.

## Kuelewa Uhandisi wa Prompti

Uhandisi wa prompt ni kuhusu kubuni maandishi ya kuingiza yanayokupa matokeo unayohitaji kila wakati. Sio tu kuuliza maswali - bali ni kupanga maombi ili modeli ifahamu kabisa unachotaka na jinsi ya kuyatoa.

Fikiria kama kumtumia maelekezo mwenzako. "Rekebisha hitilafu" ni ya jumla. "Rekebisha hitilafu ya null pointer katika UserService.java mstari wa 45 kwa kuongeza ukaguzi wa null" ni maalum. Modeli za lugha hufanya kazi kwa njia ile ile - umakini na muundo ni muhimu.

## Jinsi Hii Inavyotumia LangChain4j

Moduli hii inaonyesha mifumo ya hali ya juu ya kuprompti kwa kutumia msingi ule ule wa LangChain4j kutoka moduli za awali, ikilenga muundo wa prompti na udhibiti wa ufakirishaji.

<img src="../../../translated_images/sw/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Jinsi LangChain4j inavyounganisha prompti zako na Azure OpenAI GPT-5.2*

**Mategemeo** - Module 02 inatumia utegemezi wa langchain4j uliofafanuliwa katika `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Usanidi wa OpenAiOfficialChatModel** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Modeli ya mazungumzo imewekwa kwa mkono kama bean ya Spring kwa kutumia mteja rasmi wa OpenAI, unaounga mkono vituo vya Azure OpenAI. Tofauti kuu na Module 01 ni jinsi tunavyopanga prompti zinazotumwa kwa `chatModel.chat()`, si usanidi wa modeli yenyewe.

**Ujumbe wa Mfumo na Mtumiaji** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j hutofautisha aina za ujumbe kwa uwazi. `SystemMessage` huweka tabia na muktadha wa AI (kama "Wewe ni mkaguzi wa msimbo"), wakati `UserMessage` ina ombi halisi. Tofauti hii inakuwezesha kudumisha tabia thabiti ya AI kwa maswali tofauti ya mtumiaji.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/sw/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage hutoa muktadha wa kudumu wakati UserMessages zina maombi binafsi*

**MessageWindowChatMemory kwa Mizunguko mingi** - Kwa muundo wa mazungumzo ya mizunguko mingi, tunatumia tena `MessageWindowChatMemory` kutoka Module 01. Kila kikao kinapata kumbukumbu yake mwenyewe iliyohifadhiwa ndani ya `Map<String, ChatMemory>`, kuruhusu mazungumzo mengi yanayoendeshwa sambamba bila kuchanganya muktadha.

**Templates za Prompti** - Lengo kuu hapa ni uhandisi wa prompti, si API mpya za LangChain4j. Kila mfumo (hamasa ndogo, hamasa juu, utekelezaji wa kazi, nk) hutumia njia ile ile ya `chatModel.chat(prompt)` lakini na mistari ya prompt iliyopangwa kwa uangalifu. Mienendo ya XML, maelekezo, na muundo vyote ni sehemu ya maandishi ya prompt, si sifa za LangChain4j.

**Udhibiti wa Ufakirishaji** - Juhudi za ufakirishaji za GPT-5.2 zinadhibitiwa kupitia maelekezo ya prompt kama "hatua mbili za maksimum za ufakirishaji" au "chunguza kwa kina". Hizi ni mbinu za uhandisi wa prompti, si usanidi wa LangChain4j. Maktaba hutoa tu prompti zako kwa modeli.

Mafunzo muhimu: LangChain4j hutoa miundombinu (unganisho wa modeli kupitia [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), kumbukumbu, usimamizi wa ujumbe kupitia [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), wakati moduli hii inakufundisha jinsi ya kutengeneza prompti zenye ufanisi ndani ya miundombinu hiyo.

## Mifumo Mikuu

Sio matatizo yote yanahitaji njia ile ile. Maswali mengine yanahitaji majibu ya haraka, mengine yafikiri kwa kina. Baadhi yanahitaji ufakirishaji unaoonekana, mengine tu matokeo. Moduli hii inajumuisha mifumo nane ya kuprompti - kila mmoja umeboreshwa kwa hali tofauti. Utajaribu yote ili ujue lini kila njia inafaa.

<img src="../../../translated_images/sw/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Muhtasari wa mifumo nane ya uhandisi wa prompt na matumizi yao*

<img src="../../../translated_images/sw/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Hamasa ndogo (haraka, moja kwa moja) dhidi ya Hamasa Juu (kwa kina, kinachochunguza) katika mbinu za ufakirishaji*

**Hamasa Ndogo (Haraka & Iliyolengwa)** - Kwa maswali rahisi ambapo unataka majibu ya haraka na ya moja kwa moja. Modeli hufanya ufakirishaji mdogo - hatua 2 maksimum. Tumia hii kwa mahesabu, utaftaji, au maswali rahisi.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Chunguza na GitHub Copilot:** Fungua [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ulize:
> - "Tofauti baina ya mifumo ya hamasa ndogo na hamasa kubwa ni ipi?"
> - "Je, lebo za XML katika prompti husaidiaje kupanga jibu la AI?"
> - "Lini nitumie mifumo ya kujitathmini dhidi ya maagizo ya moja kwa moja?"

**Hamasa Juu (Kina & Kina)** - Kwa matatizo magumu ambapo unahitaji uchambuzi kamili. Modeli huchunguza kwa kina na inaonyesha ufakirishaji wa kina. Tumia hii kwa muundo wa mfumo, maamuzi ya usanifu, au utafiti mgumu.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Utekelezaji wa Kazi (Maendeleo Hatua kwa Hatua)** - Kwa mtiririko wa kazi za hatua nyingi. Modeli hutoa mpango wa awali, hueleza kila hatua anapofanya kazi, kisha hutoa muhtasari. Tumia hii kwa uhamishaji, utekelezaji, au mchakato wowote wa hatua nyingi.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Mipangilio ya Chain-of-Thought inamwomba modeli kuonyesha mchakato wake wa ufakirishaji, kuboresha usahihi kwa kazi ngumu. Ugawaji hatua kwa hatua husaidia wanadamu na AI kuelewa mantiki.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu mfumo huu:
> - "Ningebadilishaji vipi mfumo wa utekelezaji wa kazi kwa shughuli za muda mrefu?"
> - "Nini ni mbinu bora za kupanga utangulizi wa vifaa katika programu za uzalishaji?"
> - "Ninawezaje kunasa na kuonyesha taarifa za maendeleo ya kati kwenye UI?"

<img src="../../../translated_images/sw/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Mpango → Tekeleza → Fupisha mtiririko wa kazi kwa shughuli za hatua nyingi*

**Msimbo wa Kujitathmini** - Kwa kuunda msimbo wa ubora wa uzalishaji. Modeli hutengeneza msimbo, kuupitia kulingana na vigezo vya ubora, na kuuboresha taratibu. Tumia hii wakati wa kujenga vipengele au huduma mpya.

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

*Mzunguko wa kuboresha taratibu - tengeneza, tathmini, tambua matatizo, boresha, rudia*

**Uchambuzi wa Muundo** - Kwa tathmini thabiti. Modeli hupitia msimbo kwa kutumia mfumo uliowekwa (usahihi, mbinu, utendaji, usalama). Tumia hii kwa ukaguzi wa msimbo au tathmini za ubora.

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

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Uliza kuhusu uchambuzi wa muundo:
> - "Ningebadilisha vipi mfumo wa uchambuzi kwa aina tofauti za ukaguzi wa msimbo?"
> - "Njia bora ya kuchambua na kutekeleza matokeo yaliyopangwa kiutendaji ni ipi?"
> - "Ninawezaje kuhakikisha viwango thabiti vya ukali katika vikao tofauti vya ukaguzi?"

<img src="../../../translated_images/sw/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Mfumo wa makundi manne kwa ukaguzi thabiti wa msimbo na viwango vya ukali*

**Maongezi ya Mizunguko mingi** - Kwa mazungumzo yanayohitaji muktadha. Modeli hukumbuka ujumbe wa awali na kujenga juu yake. Tumia hii kwa vikao vya msaada wa mwingiliano au maswali na majibu magumu.

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

*Jinsi muktadha wa mazungumzo unavyojikusanya baada ya mizunguko mingi hadi kufikia kikomo cha tokeni*

**Ufakirishaji Hatua kwa Hatua** - Kwa matatizo yanayohitaji mantiki inayoonekana. Modeli inaonyesha ufakirishaji wazi kwa kila hatua. Tumia hii kwa matatizo ya hisabati, fumbo za mantiki, au wakati unahitaji kuelewa mchakato wa kufikiri.

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

*Kutenganisha matatizo kuwa hatua za mantiki zilizo wazi*

**Matokeo Yaliyopigwa Hati** - Kwa majibu yenye mahitaji maalum ya muundo. Modeli hufuata kwa umakini kanuni za muundo na urefu. Tumia hii kwa muhtasari au wakati unahitaji muundo sahihi wa matokeo.

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

*Kutekeleza mahitaji maalum ya muundo, urefu, na mpangilio*

## Kutumia Rasilimali Zaidi za Azure Zilizopo

**Hakikisha usambazaji:**

Hakikisha faili `.env` ipo katika mzizi wa saraka ikiwa na usajili wa Azure (iliyotengenezwa wakati wa Module 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Module 01, moduli hii inasubiri kazi kwa bandari 8083. Unaweza kuruka amri za kuanzisha hapa na kwenda moja kwa moja http://localhost:8083.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kontena ya maendeleo ina nyongeza ya Spring Boot Dashboard, ambayo hutoa uso wa kuona wa kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Bar ya Shughuli upande wa kushoto wa VS Code (atafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kazini
- Kuanza/kukomesha programu kwa bonyeza moja
- Kuangalia kumbukumbu za programu wakati halisi
- Kufuatilia hali ya programu

Bofya kitufe cha kucheza kando ya "prompt-engineering" kuanzisha moduli hii, au anzisha moduli zote kwa pamoja.

<img src="../../../translated_images/sw/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia shell script**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwa saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
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

Skripti zote huchukua kwa otomatiki vigezo vya mazingira kutoka faili ya mzizi `.env` na zitajenga JAR ikiwa hazipo.

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

**Kusitisha:**

**Bash:**
```bash
./stop.sh  # Hii moduli tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Moduli hii pekee
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Picha za Skrini za Programu

<img src="../../../translated_images/sw/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashibodi kuu inaonyesha mifumo yote 8 ya uhandisi wa prompti na sifa zake pamoja na matumizi*

## Kuchunguza Mifumo

Uso wa wavuti unakuwezesha kujaribu mbinu tofauti za kuprompt. Kila mfumo unatatua matatizo tofauti - jaribu kuona lini kila njia inaingia kazini vizuri.

### Hamasa Ndogo vs Hamasa Juu

Uliza swali rahisi kama "Ni asilimia ngapi 15 ya 200?" ukitumia Hamasa Ndogo. Utapata jibu la moja kwa moja kwa haraka. Sasa uliza jambo zito kama "Tengeneza mkakati wa kuhifadhi kwa API yenye trafiki kubwa" kwa Hamasa Juu. Angalia jinsi modeli inachelewa na kutoa ufakirishaji wa kina. Sawa modeli, muundo sawa wa swali - lakini prompti inaeleza kiasi cha kufikiri kinachotakiwa.
<img src="../../../translated_images/sw/low-eagerness-demo.898894591fb23aa0.webp" alt="Onyesho la Hamu Ndogo" width="800"/>

*Hesabu ya haraka na sababu kidogo*

<img src="../../../translated_images/sw/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Onyesho la Hamu Kubwa" width="800"/>

*Mikakati kamilifu ya kuhifadhi (2.8MB)*

### Utekelezaji wa Kazi (Utangulizi wa Zana)

Mchakato wa hatua nyingi unafaidika na upangaji wa awali na maelezo ya maendeleo. Mfano unaeleza kinachofanya, unaelezea kila hatua, kisha hutoa muhtasari wa matokeo.

<img src="../../../translated_images/sw/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Onyesho la Utekelezaji wa Kazi" width="800"/>

*Kuunda nukta ya REST hatua kwa hatua (3.9MB)*

### Msimbo Unaojitathmini

Jaribu "Unda huduma ya uhakiki wa barua pepe". Badala ya kuzalisha tu msimbo na kusimama, mfano huzalisha, kuhakiki dhidi ya vigezo vya ubora, kubaini mapungufu, na kuboresha. Utaona ukirudia hadi msimbo ufikie viwango vya uzalishaji.

<img src="../../../translated_images/sw/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Onyesho la Msimbo Unaojitathmini" width="800"/>

*Huduma kamili ya uhakiki wa barua pepe (5.2MB)*

### Uchambuzi Uliojenziwa

Mapitio ya msimbo yanahitaji mfumo thabiti wa tathmini. Mfano unachambua msimbo kwa kutumia makundi ya kudumu (usahihi, mazoea, utendaji, usalama) na viwango vya ukali.

<img src="../../../translated_images/sw/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Onyesho la Uchambuzi Uliojenizwa" width="800"/>

*Mapitio ya msimbo kwa mfumo rasmi*

### Mazungumzo ya Mara-Nyingi

Uliza "Spring Boot ni nini?" kisha mara moja fuata na "Nionyeshe mfano". Mfano unakumbuka swali lako la kwanza na anakupa mfano wa Spring Boot mahsusi. Bila kumbukumbu, swali la pili lingeweza kuwa na kutiliwa shaka.

<img src="../../../translated_images/sw/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Onyesho la Mazungumzo ya Mara-Nyingi" width="800"/>

*Kuhifadhi muktadha kati ya maswali*

### Sababu Hatua kwa Hatua

Chagua tatizo la hesabu na liajaribu kwa Sababu Hatua kwa Hatua na Hamu Ndogo. Hamu ndogo inakupa jibu tu - haraka lakini haijafumbuliwa. Hatua kwa hatua inakuonyesha kila hesabu na uamuzi.

<img src="../../../translated_images/sw/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Onyesho la Sababu Hatua kwa Hatua" width="800"/>

*Tatizo la hisabati lenye hatua wazi*

### Matokeo Yaliyodhibitiwa

Unapotaka miundo maalum au idadi ya maneno, mwendelezo huu unahimiza kushikamana mkali. Jaribu kuzalisha muhtasari wenye maneno 100 kabisa kwa muundo wa vidokezo.

<img src="../../../translated_images/sw/constrained-output-demo.567cc45b75da1633.webp" alt="Onyesho la Matokeo Yaliyodhibitiwa" width="800"/>

*Muhtasari wa kujifunza mashine wenye udhibiti wa muundo*

## Unachojifunza Kweli

**Juhudi ya Kufikiria Hubadilisha Kila Kitu**

GPT-5.2 inakuwezesha kudhibiti juhudi za kompyuta kupitia maelekezo yako. Juhudi ndogo ina maana majibu ya haraka na utafiti mdogo. Juhudi kubwa ina maana mfano unachukua muda kufikiria kwa kina. Unajifunza kulinganisha juhudi na ugumu wa kazi - usipoteze muda kwa maswali rahisi, lakini usikimbilie maamuzi magumu pia.

**Muundo Huelekeza Tabia**

Tambua lebo za XML katika maelekezo? Hazijapambwa tu. Mifano huifuata maagizo yaliyopangwa kwa uaminifu zaidi kuliko maandishi ya bure. Unapotaka michakato ya hatua nyingi au mantiki tata, muundo husaidia mfano kufuatilia wapi uko na nini kinakuja baadae.

<img src="../../../translated_images/sw/prompt-structure.a77763d63f4e2f89.webp" alt="Muundo wa Maelekezo" width="800"/>

*Muundo wa maelekezo yaliyopangwa vyema yenye sehemu wazi na usanifu wa mtindo wa XML*

**Ubora Kupitia Kujitathmini**

Mifano ya kujitathmini huenda kwa kufanya vigezo vya ubora vya wazi. Badala ya kutegemea mfano "ufanye vizuri", unaeleza hasa maana ya "vema": mantiki sahihi, usimamizi wa makosa, utendaji, usalama. Mfano unaweza kisha kutathmini matokeo yake na kuboresha. Hii hubadilisha uzalishaji wa msimbo kutoka bahati nasibu kuwa mchakato.

**Muktadha Ni Mdogo**

Mazungumzo ya mara-nwingi hufanya kazi kwa kujumuisha historia ya ujumbe na kila ombi. Lakini kuna kikomo - kila mfano una idadi kubwa ya tokeni. Kadri mazungumzo yanavyoongezeka, utahitaji mikakati ya kuweka muktadha muhimu bila kufikia kikomo hicho. Moduli hii inakuonyesha jinsi kumbukumbu inavyofanya kazi; baadaye utajifunza lini kuifupisha, lini kusahau, na lini kuipata tena.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [03-rag - RAG (Uzalishaji ulioboreshwa kwa Kupata Taarifa)](../03-rag/README.md)

---

**Urambazaji:** [← Zamani: Moduli 01 - Utangulizi](../01-introduction/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiarifu cha Kukataa**:
Hati hii imetafsiriwa kwa kutumia huduma ya utafsiri wa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au upungufu. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, utafsiri wa kitaalamu unaofanywa na binadamu unashauriwa. Hatubebebi lawama kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->