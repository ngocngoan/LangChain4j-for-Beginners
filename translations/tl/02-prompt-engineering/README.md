# Module 02: Prompt Engineering gamit ang GPT-5.2

## Table of Contents

- [Ano ang Matututuhan Mo](../../../02-prompt-engineering)
- [Mga Kinakailangan](../../../02-prompt-engineering)
- [Pag-unawa sa Prompt Engineering](../../../02-prompt-engineering)
- [Paano Ito Gumagamit ng LangChain4j](../../../02-prompt-engineering)
- [Ang Mga Pangunahing Pattern](../../../02-prompt-engineering)
- [Paggamit ng Mga Umiiral na Azure Resources](../../../02-prompt-engineering)
- [Mga Screenshot ng Aplikasyon](../../../02-prompt-engineering)
- [Paggalugad sa Mga Pattern](../../../02-prompt-engineering)
  - [Mababang Pagnanais vs Mataas na Pagnanais](../../../02-prompt-engineering)
  - [Pagpapatupad ng Gawain (Mga Preambles ng Tool)](../../../02-prompt-engineering)
  - [Sariling Pagninilay ng Code](../../../02-prompt-engineering)
  - [Istrakturadong Pagsusuri](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Hakbang-hakbang na Pagsusuri](../../../02-prompt-engineering)
  - [Limitadong Output](../../../02-prompt-engineering)
- [Ano Talagang Iyong Natututuhan](../../../02-prompt-engineering)
- [Mga Susunod na Hakbang](../../../02-prompt-engineering)

## Ano ang Matututuhan Mo

Sa nakaraang module, nakita mo kung paano nagbibigay-daan ang memorya sa conversational AI at gumamit ng GitHub Models para sa mga pangunahing interaksyon. Ngayon, magtutuon tayo sa paraan ng pagtatanong mo – ang mga prompt mismo – gamit ang GPT-5.2 ng Azure OpenAI. Ang paraan ng pagsasaayos ng iyong mga prompt ay malaki ang epekto sa kalidad ng mga sagot na matatanggap mo.

Gagamitin natin ang GPT-5.2 dahil ipinakilala nito ang kontrol sa pag-iisip – maaari mong sabihin sa modelo kung gaano karaming pag-iisip ang gagawin bago sumagot. Pinapalinaw nito ang iba't ibang estratehiya ng pag-prompt at tinutulungan kang maunawaan kung kailan gagamitin ang bawat isa. Makikinabang din tayo mula sa mas kaunting mga limitasyon sa rate ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Mga Kinakailangan

- Nakumpleto ang Module 01 (Azure OpenAI resources na na-deploy)
- May `.env` na file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Tandaan:** Kung hindi mo pa nakumpleto ang Module 01, sundin muna ang mga tagubilin sa pag-deploy doon.

## Pag-unawa sa Prompt Engineering

Ang prompt engineering ay tungkol sa pagdisenyo ng input na teksto na palaging nagbibigay sa iyo ng kinakailangang resulta. Hindi lang ito tungkol sa pagtatanong – ito ay tungkol sa pagsasaayos ng mga kahilingan upang maintindihan ng modelo kung ano talaga ang gusto mo at kung paano ito ihahatid.

Isipin mo ito bilang pagbibigay ng mga tagubilin sa isang katrabaho. Ang "Ayusin ang bug" ay malabo. Ang "Ayusin ang null pointer exception sa UserService.java linya 45 sa pamamagitan ng pagdagdag ng null check" ay tiyak. Ganun din ang mga language model – mahalaga ang pagiging tiyak at istruktura.

## Paano Ito Gumagamit ng LangChain4j

Ipinapakita ng module na ito ang mga advanced na pattern ng pag-prompt gamit ang parehong pundasyon ng LangChain4j mula sa mga naunang module, na nakatuon sa istruktura ng prompt at kontrol ng pag-iisip.

<img src="../../../translated_images/tl/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Paano kinokonekta ng LangChain4j ang iyong mga prompt sa Azure OpenAI GPT-5.2*

**Dependencies** – Gumagamit ang Module 02 ng mga sumusunod na langchain4j dependencies na nakasaad sa `pom.xml`:  
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
  
**OpenAiOfficialChatModel Configuration** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Ang chat model ay manu-manong naka-configure bilang Spring bean gamit ang OpenAI Official client, na sumusuporta sa mga Azure OpenAI endpoint. Ang pangunahing kaibahan mula sa Module 01 ay kung paano natin isinaayos ang mga prompt na ipinapadala sa `chatModel.chat()`, hindi ang setup ng modelo mismo.

**System at User Messages** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

Ihiwalay ng LangChain4j ang mga uri ng mensahe para sa kalinawan. Ang `SystemMessage` ay nagseset ng pag-uugali at konteksto ng AI (tulad ng "Ikaw ay isang tagasuri ng code"), habang ang `UserMessage` ay naglalaman ng aktwal na kahilingan. Pinapahintulutan ka ng paghihiwalay na ito na mapanatili ang pare-parehong pag-uugali ng AI sa iba't ibang user queries.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/tl/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*Ang SystemMessage ay nagbibigay ng permanenteng konteksto habang ang UserMessages ay naglalaman ng mga indibiduwal na kahilingan*

**MessageWindowChatMemory para sa Multi-Turn** – Para sa multi-turn na pattern ng usapan, nire-reuse natin ang `MessageWindowChatMemory` mula sa Module 01. Ang bawat sesyon ay may sariling instance ng memory na naka-imbak sa isang `Map<String, ChatMemory>`, na nagpapahintulot ng maraming magkakasabay na pag-uusap nang hindi nagkakalito ang mga konteksto.

**Prompt Templates** – Ang tunay na sentro dito ay prompt engineering, hindi mga bagong LangChain4j API. Ang bawat pattern (mababang pagnanais, mataas na pagnanais, pagpapatupad ng gawain, atbp.) ay gumagamit ng parehong `chatModel.chat(prompt)` method pero may maingat na istrukturang prompt na mga string. Ang mga XML tag, mga tagubilin, at pag-format ay bahagi ng prompt text, hindi mga feature ng LangChain4j.

**Kontrol sa Pag-iisip** – Kinokontrol ng GPT-5.2 ang pag-iisip gamit ang mga tagubilin ng prompt tulad ng "maximum 2 reasoning steps" o "explore thoroughly". Ito ay mga teknik sa prompt engineering, hindi mga setting ng LangChain4j. Ang library lang ang naghahatid ng iyong mga prompt sa modelo.

Ang mahalagang punto: Nagbibigay ang LangChain4j ng imprastraktura (model connection via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memory, paghawak ng mensahe via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), habang itinuturo ng module na ito kung paano gumawa ng epektibong mga prompt sa loob ng imprastrakturang iyon.

## Ang Mga Pangunahing Pattern

Hindi lahat ng problema ay nangangailangan ng parehong pamamaraan. Ang ilang mga tanong ay kailangan ng mabilisang sagot, ang iba ay kailangan ng malalim na pagiisip. Ang iba ay kailangang may nakikitang pag-iisip, ang iba ay gusto lang ng resulta. Sinasaklaw ng module na ito ang walong pattern ng pag-prompt - bawat isa ay naka-optimize para sa iba't ibang mga senaryo. Susubukan mo silang lahat para matutunan kung kailan mas epektibo ang bawat approach.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang pananaw ng walong prompt engineering patterns at ang kanilang mga gamit*

<img src="../../../translated_images/tl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Mababang pagnanais (mabilis, direkta) vs Mataas na pagnanais (malalim, pagsasaliksik) na mga paraan ng pag-iisip*

**Mababang Pagnanais (Mabilis at Nakatuon)** – Para sa mga simpleng tanong kung saan gusto mo ng mabilis at direktang sagot. Gumagawa ang modelo ng kaunting pag-iisip – maximum ng 2 hakbang. Gamitin ito para sa mga kalkulasyon, paghahanap, o mga tuwirang tanong.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Subukan gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness prompting patterns?"
> - "Paano nakakatulong ang mga XML tag sa mga prompt sa pagsasaayos ng sagot ng AI?"
> - "Kailan ko gagamitin ang self-reflection patterns kumpara sa direktang tagubilin?"

**Mataas na Pagnanais (Malalim at Malawak)** – Para sa mga complex na problema kung saan gusto mo ng komprehensibong pagsusuri. Nag-eexplore ng mabuti ang modelo at nagpapakita ng detalyadong pag-iisip. Gamitin ito para sa disenyo ng system, mga desisyon sa arkitektura, o komplikadong pananaliksik.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pagpapatupad ng Gawain (Hakbang-hakbang na Progreso)** – Para sa mga multi-step na workflow. Nagbibigay ang modelo ng upfront plan, nilalahad ang bawat hakbang habang ginagawa, at pagkatapos ay nagbibigay ng buod. Gamitin ito para sa migrasyon, implementasyon, o anumang multi-step na proseso.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Ang Chain-of-Thought prompting ay tahasang hinihiling sa modelo na ipakita ang proseso ng pag-iisip nito, pinapabuti ang katumpakan para sa mga kumplikadong gawain. Ang hakbang-hakbang na paghahati ay tumutulong sa parehong tao at AI na maunawaan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa mga long-running operation?"
> - "Ano ang mga best practice sa pagsasaayos ng mga tool preambles sa production na aplikasyon?"
> - "Paano ko mahuhuli at maipapakita ang mga intermediate progress update sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planuhin → Isagawa → Buodin na workflow para sa multi-step na mga gawain*

**Sariling Pagninilay ng Code** – Para sa paggawa ng kalidad na production code. Gumagawa ang modelo ng code, sinusuri ito gamit ang mga pamantayan sa kalidad, at pinapabuti ito nang paulit-ulit. Gamitin ito kapag bumubuo ng mga bagong feature o serbisyo.

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

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratibong loop ng pagpapabuti - bumuo, suriin, tukuyin ang mga isyu, pagbutihin, ulitin*

**Istrakturadong Pagsusuri** – Para sa pare-parehong pagsusuri. Sinusuri ng modelo ang code gamit ang isang nakatakdang balangkas (correctness, practices, performance, security). Gamitin ito para sa mga code review o pagsusuri sa kalidad.

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

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa istrakturadong pagsusuri:
> - "Paano ko nako-customize ang analysis framework para sa iba't ibang uri ng code review?"
> - "Ano ang pinakamahusay na paraan upang i-parse at i-apply ang istrakturadong output programmatically?"
> - "Paano ko mapapanatili ang pare-parehong severity levels sa iba't ibang session ng review?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Apat na kategorya na balangkas para sa pare-parehong code review na may severity levels*

**Multi-Turn Chat** – Para sa mga pag-uusap na nangangailangan ng konteksto. Tinutandaan ng modelo ang mga naunang mensahe at nagtatayo mula doon. Gamitin ito para sa interactive na help session o komplikadong Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/tl/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Paano naiipon ang konteksto ng pag-uusap sa maraming turn hanggang maabot ang token limit*

**Hakbang-hakbang na Pagsusuri** – Para sa mga problema na nangangailangan ng nakikitang lohika. Nagpapakita ang modelo ng tahasang pag-iisip sa bawat hakbang. Gamitin ito para sa mga math problem, logic puzzle, o kapag nais mong maunawaan ang proseso ng pagiisip.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Paghahati ng problema sa tahasang mga hakbang ng lohika*

**Limitadong Output** – Para sa mga sagot na may tiyak na mga patakaran sa format. Mahigpit na sinusunod ng modelo ang mga alituntunin sa format at haba. Gamitin ito para sa mga summary o kapag kailangan mo ng eksaktong istruktura ng output.

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

<img src="../../../translated_images/tl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Pagsunod sa mga tiyak na requirement sa format, haba, at istruktura*

## Paggamit ng Mga Umiiral na Azure Resources

**Suriin ang deployment:**

Siguraduhing mayroon ang `.env` file sa root directory na may Azure credentials (nalikha noong Module 01):  
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, tumatakbo na ang module na ito sa port 8083. Maaari mo nang laktawan ang mga command sa pagsisimula sa ibaba at direktang pumunta sa http://localhost:8083.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual interface upang pamahalaan ang lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:  
- Tingnan ang lahat ng magagamit na Spring Boot applications sa workspace  
- Simulan/hinto ang applications sa isang click lang  
- Tingnan ang mga log ng aplikasyon nang real-time  
- I-monitor ang status ng aplikasyon  

I-click lang ang play button sa tabi ng "prompt-engineering" para simulan ang module na ito, o simulan lahat ng module nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Paggamit ng shell scripts**

Simulan lahat ng web application (module 01-04):

**Bash:**  
```bash
cd ..  # Mula sa root directory
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Mula sa root na direktoryo
.\start-all.ps1
```
  
O simulan lang ang module na ito:

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
  
Awtomatikong niloload ng parehong mga script ang mga environment variable mula sa root `.env` file at bubuuin ang mga JAR kapag wala pa ito.

> **Tandaan:** Kung gusto mong manu-manong buuin lahat ng module bago simulan:  
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
  
Buksan ang http://localhost:8083 sa iyong browser.

**Para ihinto:**

**Bash:**  
```bash
./stop.sh  # Para sa module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Para lamang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng modules
```
  
## Mga Screenshot ng Aplikasyon

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns kasama ang kanilang mga katangian at gamit*

## Paggalugad sa Mga Pattern

Pinapayagan ka ng web interface na mag-eksperimento sa iba't ibang estratehiya ng pag-prompt. Ang bawat pattern ay nagsosolba ng iba't ibang problema – subukan mo ito para makita kung kailan maging epektibo ang bawat isa.

### Mababang Pagnanais vs Mataas na Pagnanais

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Mababang Pagnanais. Makakakuha ka ng agarang, direktang sagot. Ngayon magtanong ng mas kumplikadong bagay tulad ng "Disenyo ng caching strategy para sa high-traffic API" gamit ang Mataas na Pagnanais. Pansinin kung paano bumabagal ang modelo at nagbibigay ng detalyadong pag-iisip. Parehong modelo, parehong istruktura ng tanong – pero sinasabi ng prompt kung gaano karaming pagiisip ang gagawin.
<img src="../../../translated_images/tl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Mabilis na kalkulasyon na may minimal na pangangatwiran*

<img src="../../../translated_images/tl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komprehensibong estratehiya sa pag-cache (2.8MB)*

### Pagpapatupad ng Gawain (Mga Preambles ng Kasangkapan)

Nakikinabang ang multi-step na mga workflow mula sa maagang pagpaplano at pagsasalaysay ng progreso. Ipinapaliwanag ng modelo ang gagawin nito, isinasalaysay ang bawat hakbang, pagkatapos ay inilalahad ang mga resulta.

<img src="../../../translated_images/tl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Paglikha ng isang REST endpoint na may step-by-step na pagsasalaysay (3.9MB)*

### Sariling Pagninilay sa Code

Subukan ang "Gumawa ng serbisyo para sa pag-validate ng email". Sa halip na simpleng bumuo ng code at huminto, ang modelo ay gumagawa, nagsusuri laban sa mga pamantayan ng kalidad, tinutukoy ang mga kahinaan, at nagpapabuti. Makikita mong inuulit-ulit ito hanggang sa maabot ng code ang mga pamantayan sa produksyon.

<img src="../../../translated_images/tl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletong serbisyo sa pag-validate ng email (5.2MB)*

### Estrukturadong Pagsusuri

Ang pagsusuri ng code ay nangangailangan ng pare-parehong mga balangkas ng pagsusuri. Sinusuri ng modelo ang code gamit ang mga nakatakdang kategorya (katumpakan, mga kaugalian, pagganap, seguridad) na may mga antas ng kaseryosohan.

<img src="../../../translated_images/tl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pagsusuri ng code gamit ang balangkas*

### Multi-Turn Chat

Itanong ang "Ano ang Spring Boot?" pagkatapos ay agad na sundan ng "Ipakita mo sa akin ang isang halimbawa". Tinatandaan ng modelo ang iyong unang tanong at bibigyan ka nito ng partikular na halimbawa ng Spring Boot. Kung walang memorya, magiging masyadong malabo ang pangalawang tanong.

<img src="../../../translated_images/tl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Pagpapanatili ng konteksto sa mga tanong*

### Step-by-Step Reasoning

Pumili ng problema sa math at subukan ito gamit ang Parehong Step-by-Step Reasoning at Low Eagerness. Ang mababang sigasig ay nagbibigay lang ng sagot - mabilis ngunit hindi malinaw. Ipinapakita ng step-by-step ang bawat kalkulasyon at desisyon.

<img src="../../../translated_images/tl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema sa math na may tiyak na mga hakbang*

### Constrained Output

Kapag kailangan mo ng mga tiyak na format o bilang ng mga salita, pinipilit ng pattern na ito ang mahigpit na pagsunod. Subukang gumawa ng buod na eksaktong may 100 salita sa format na bullet point.

<img src="../../../translated_images/tl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Buod ng machine learning na may kontrol sa format*

## Ang Talagang Iyong Natututuhan

**Binabago ng Pagsisikap sa Pangangatwiran ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang pagsisikap na pangkomputa sa pamamagitan ng iyong mga prompt. Ang mababang pagsisikap ay nangangahulugan ng mabilis na tugon na may minimal na paggalugad. Ang mataas na pagsisikap ay nangangahulugan na maglalaan ng panahon ang modelo upang malalim na mag-isip. Natututo kang iangkop ang pagsisikap sa kahirapan ng gawain - huwag sayangin ang oras sa simpleng mga tanong, ngunit huwag din magmadali sa mga komplikadong desisyon.

**Ginagabay ng Estruktura ang Pag-uugali**

Napansin mo ba ang mga tag ng XML sa mga prompt? Hindi ito mga palamuti. Mas maaasahan ang pagsunod ng mga modelo sa mga istrukturadong tagubilin kaysa sa malayang teksto. Kapag kailangan mo ng mga multi-step na proseso o komplikadong lohika, nakakatulong ang estruktura upang matunton ng modelo kung nasaan ito at kung ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng isang mahusay na estrukturadong prompt na may malinaw na mga seksyon at organisasyong estilo XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumana ang mga pattern ng sariling pagninilay sa pamamagitan ng pagpapaliwanag nang malinaw ng mga pamantayan ng kalidad. Sa halip na umasa na "gagawin nito nang tama" ang modelo, sinasabi mo sa kanya kung ano talaga ang ibig sabihin ng "tama": tamang lohika, paghawak ng error, pagganap, seguridad. Maaari nang suriin ng modelo ang sarili nitong output at pagbutihin ito. Ginagawa nitong proseso ang pagbuo ng code mula sa isang suwerte.

**May Hangganan ang Konteksto**

Gumana ang mga multi-turn na usapan sa pamamagitan ng pagsasama ng kasaysayan ng mensahe sa bawat kahilingan. Ngunit may hangganan - bawat modelo ay may pinakamataas na bilang ng token. Habang lumalaki ang mga usapan, kakailanganin mo ng mga estratehiya upang mapanatili ang mahalagang konteksto nang hindi nadudulot ang limitasyong iyon. Ipinapakita ng module na ito kung paano gumagana ang memorya; sa susunod, matututunan mo kung kailan magbubuod, kailan makakalimot, at kailan kukuha.

## Mga Susunod na Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigasyon:** [← Nakaraang: Module 01 - Panimula](../01-introduction/README.md) | [Bumalik sa Pangunahing Pahina](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kaming maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->