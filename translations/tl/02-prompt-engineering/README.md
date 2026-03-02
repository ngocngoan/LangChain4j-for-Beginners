# Module 02: Prompt Engineering gamit ang GPT-5.2

## Talaan ng Nilalaman

- [Paglalakad sa Video](../../../02-prompt-engineering)
- [Mga Matututuhan Mo](../../../02-prompt-engineering)
- [Mga Kinakailangan](../../../02-prompt-engineering)
- [Pag-unawa sa Prompt Engineering](../../../02-prompt-engineering)
- [Mga Pangunahing Kaalaman sa Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Mga Paunlarin na Pattern](../../../02-prompt-engineering)
- [Paggamit ng Umiiral na Azure Resources](../../../02-prompt-engineering)
- [Mga Screenshot ng Aplikasyon](../../../02-prompt-engineering)
- [Pagsisiyasat sa mga Pattern](../../../02-prompt-engineering)
  - [Mababang Eagerness kumpara sa Mataas na Eagerness](../../../02-prompt-engineering)
  - [Pagpapatupad ng Gawain (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Hakbang-hakbang na Pangangatwiran](../../../02-prompt-engineering)
  - [Limitadong Output](../../../02-prompt-engineering)
- [Ang Tunay Mong Natututuhan](../../../02-prompt-engineering)
- [Mga Susunod na Hakbang](../../../02-prompt-engineering)

## Paglalakad sa Video

Panoorin ang live na sesyon na nagpapaliwanag kung paano magsimula gamit ang module na ito:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Mga Matututuhan Mo

<img src="../../../translated_images/tl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Sa naunang module, nakita mo kung paano pinapagana ng memorya ang conversational AI at ginamit ang GitHub Models para sa mga pangunahing interaksyon. Ngayon ay magtutuon tayo kung paano magtanong — ang mga prompt mismo — gamit ang Azure OpenAI's GPT-5.2. Malaki ang epekto ng paraan ng pagbuo mo ng mga prompt sa kalidad ng mga sagot na matatanggap mo. Magsisimula tayo sa pagrepaso sa mga pangunahing teknik sa prompting, tapos lilipat sa walong advanced na mga pattern na gumagamit nang buong-buo sa kakayahan ng GPT-5.2.

Gagamitin natin ang GPT-5.2 dahil ipinakikilala nito ang control sa pangangatwiran — maaari mong sabihin sa modelo kung gaano kalalim ang isipin bago sumagot. Ginagawa nitong mas malilinaw ang iba't ibang estratehiya sa prompting at tinutulungan kang maintindihan kung kailan angkop gamitin ang bawat isa. Makikinabang din tayo mula sa mas kaunting rate limits ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Mga Kinakailangan

- Natapos ang Module 01 (Azure OpenAI resources na na-deploy)
- `.env` file sa root directory na may Azure credentials (nilikha gamit ang `azd up` sa Module 01)

> **Tandaan:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubilin sa pag-deploy doon.

## Pag-unawa sa Prompt Engineering

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdidisenyo ng input text na palaging nagbibigay sa'yo ng mga resulta na kailangan mo. Hindi lang ito basta pagtatanong — ito ay ang pagbuo ng mga kahilingan na nauunawaan ng modelo kung ano talaga ang gusto mo at kung paano ito ipapahayag.

Isipin mo itong pagbibigay ng instruksyon sa katrabaho. Ang "Ayusin ang bug" ay malabo. "Ayusin ang null pointer exception sa UserService.java line 45 sa pamamagitan ng pagdagdag ng null check" ay tiyak. Gumagana ang mga language model sa parehong paraan — mahalaga ang pagiging tiyak at ang istruktura.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Nagbibigay ang LangChain4j ng imprastraktura — mga koneksyon sa modelo, memorya, at mga uri ng mensahe — habang ang mga pattern sa prompt ay maingat na nakaayos na teksto na ipapadala sa imprastrakturang iyon. Ang mga pangunahing bloke ng konstruksyon ay `SystemMessage` (na nagse-set ng ugali at papel ng AI) at `UserMessage` (na nagdala ng aktwal mong kahilingan).

## Mga Pangunahing Kaalaman sa Prompt Engineering

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago tayo pumasok sa mga advanced na pattern sa module na ito, magrepaso tayo ng limang pangunahing teknik sa prompting. Ito ang mga pundasyon na dapat malaman ng bawat prompt engineer. Kung nagtrabaho ka na sa [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na itong mga ito sa aksyon — narito ang konseptwal na balangkas sa likod nila.

### Zero-Shot Prompting

Pinakapayak na paraan: bigyan ang modelo ng direktang instruksyon na walang mga halimbawa. Ang modelo ay umaasa nang buo sa kanyang training upang maintindihan at isagawa ang gawain. Maganda ito para sa mga tuwirang kahilingan kung saan halata ang inaasahang kilos.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang instruksyon na walang halimbawa — hinuhinuha ng modelo ang gawain mula sa instruksyon lamang*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Simpleng klasipikasyon, direktang tanong, pagsasalin, o anumang gawain na kayang hawakan ng modelo nang walang dagdag na patnubay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na gusto mong sundan ng modelo. Natututuhan ng modelo ang inaasahang input-output format mula sa iyong mga halimbawa at inaaplay ito sa mga bagong input. Malaki ang pagbuti ng consistency para sa mga gawain kung saan hindi halata ang nais na format o kilos.

<img src="../../../translated_images/tl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Pagkatuto mula sa mga halimbawa — kinikilala ng modelo ang pattern at inilalapat ito sa mga bagong input*

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

**Kailan gagamitin:** Mga custom na klasipikasyon, consistent formatting, domain-specific tasks, o kapag hindi consistent ang zero-shot results.

### Chain of Thought

Hilingin sa modelo na ipakita ang kanyang pangangatwiran hakbang-hakbang. Sa halip na diretso sa sagot, hinahati ng modelo ang problema at tinatrabaho ang bawat bahagi nang hayagan. Pinapabuti nito ang kawastuhan sa math, lohika, at mga multi-step na pag-aanalisa.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Hakbang-hakbang na pangangatwiran — paghahati ng mga komplikadong problema sa malinaw na lohikal na hakbang*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, saka 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Mga problema sa math, lohikal na palaisipan, debugging, o anumang gawain kung saan ang pagpapakita ng proseso ng pangangatwiran ay nagpapabuti ng katumpakan at tiwala.

### Role-Based Prompting

Itakda ang isang persona o papel para sa AI bago magtanong. Nagbibigay ito ng konteksto na humuhubog sa tono, lalim, at pokus ng sagot. Iba ang payo ng "software architect" kaysa sa "junior developer" o "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pagtatakda ng konteksto at persona — ibang sagot ang lalabas base sa naka-assign na papel*

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

**Kailan gagamitin:** Mga pagsusuri sa code, pagtuturo, domain-specific na pagsusuri, o kapag kailangan mo ng sagot na angkop sa isang partikular na antas ng kadalubhasaan o pananaw.

### Prompt Templates

Gumawa ng mga reusable na prompt na may variable placeholder. Sa halip na lumikha ng bagong prompt sa bawat pagkakataon, magdeklara ng template minsan lang at punuin ng iba't ibang mga halaga. Madali itong gawin gamit ang `PromptTemplate` class ng LangChain4j gamit ang sintaks na `{{variable}}`.

<img src="../../../translated_images/tl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Mga reusable prompt na may variable placeholders — isang template, maraming gamit*

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

**Kailan gagamitin:** Mga paulit-ulit na tanong na may iba't ibang input, batch processing, paggawa ng reusable AI workflows, o anumang senaryo kung saan pareho ang estruktura ng prompt ngunit nagbabago ang data.

---

Ang limang pundasyong ito ay nagbibigay sa'yo ng matibay na kasangkapan para sa karamihan ng mga gawain sa prompting. Ang natitira sa module na ito ay itinayo gamit ang **walong advanced na pattern** na ginagamit ang reasoning control, self-evaluation, at structured output na kakayahan ng GPT-5.2.

## Mga Paunlarin na Pattern

Kapag natutuhan na ang mga pundasyon, pumunta tayo sa walong advanced na pattern na nagpapasikat sa module na ito. Hindi lahat ng problema ay nangangailangan ng iisang paraan. Ang iba ay kailangan ng mabilis na sagot, ang ilan naman ay nangangailangan ng malalim na pag-iisip. May ilan na kailangang ipakita ang pangangatwiran, ang iba ay basta resulta lang ang kailangan. Bawat pattern sa ibaba ay na-optimize para sa ibang sitwasyon — at lalo pang pinaigting ng reasoning control ng GPT-5.2 ang mga pagkakaiba.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang ideya ng walong pattern sa prompt engineering at kanilang mga gamit*

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Pinapayagan ng reasoning control ng GPT-5.2 na tukuyin kung gaano kalalim ang pag-iisip ng modelo — mula sa mabilis na direktang sagot hanggang sa malalim na pagsasaliksik*

**Mababang Eagerness (Mabilis at Nakatuon)** - Para sa simpleng tanong na gusto mong mabilis at diretso ang sagot. Ang modelo ay minimal lang mag-iisip — maximum na 2 hakbang. Gamitin ito para sa mga kalkulasyon, pagtingin, o tuwirang tanong.

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

> 💡 **Galugarin gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness na prompting patterns?"
> - "Paano nakakatulong ang mga tag na XML sa mga prompt upang istraktura ang tugon ng AI?"
> - "Kailan ko dapat gamitin ang self-reflection patterns kumpara sa direktang instruksyon?"

**Mataas na Eagerness (Malalim at Masusing Pagsusuri)** - Para sa mga kumplikadong problema na gusto mo ng komprehensibong pagsusuri. Masusing sinusuri ng modelo at ipinapakita ang detalyadong pangangatwiran. Gamitin ito para sa disenyo ng sistema, mga desisyon sa arkitektura, o komplikadong pananaliksik.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pagpapatupad ng Gawain (Hakbang-hakbang na Progreso)** - Para sa multi-step na mga workflow. Nagbibigay ang modelo ng plano sa umpisa, binabanggit ang bawat hakbang habang ginagawa ito, at pagkatapos ay naglalabas ng buod. Gamitin ito para sa migrations, implementasyon, o anumang multi-step na proseso.

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

Ang Chain-of-Thought prompting ay sadyang humihiling sa modelo na ipakita ang proseso ng pangangatwiran, pinapabuti ang kawastuhan para sa mga komplikadong gawain. Ang hakbang-hakbang na paghahati ay tumutulong sa tao at AI na maintindihan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa long-running operations?"
> - "Ano ang mga best practices sa pag-istruktura ng tool preambles sa production na aplikasyon?"
> - "Paano ko mahuhuli at maipapakita ang mga intermediate progress updates sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Workflow na Planuhin → Isagawa → Buodin para sa mga multi-step na gawain*

**Self-Reflecting Code** - Para sa paggawa ng production-quality na code. Gumagawa ang modelo ng code na sumusunod sa production standards na may tamang error handling. Gamitin ito kapag bumubuo ng mga bagong feature o serbisyo.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratibong loop ng pagpapabuti - gumawa, suriin, tukuyin ang mga isyu, pagbutihin, ulitin*

**Structured Analysis** - Para sa consistent na pagsusuri. Sinusuri ng modelo ang code gamit ang isang fixed framework (kawastuhan, mga kasanayan, performance, seguridad, maintainability). Gamitin ito para sa mga code review o quality assessments.

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

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa structured analysis:
> - "Paano ko mae-customize ang analysis framework para sa iba't ibang uri ng code review?"
> - "Ano ang pinakamahusay na paraan para i-parse at gamitin ang structured output programmatically?"
> - "Paano ko sisiguraduhin ang consistent severity levels sa iba't ibang sesyon ng review?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Balangkas para sa consistent na code reviews na may antas ng kaseryosohan*

**Multi-Turn Chat** - Para sa mga usapan na kailangan ng konteksto. Naalala ng modelo ang mga naunang mensahe at pinag-iigting ito. Gamitin ito para sa interactive help sessions o komplikadong Q&A.

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

*Paano naiipon ang konteksto ng usapan sa maraming pag-uusap hanggang maabot ang token limit*

**Hakbang-hakbang na Pangangatwiran** - Para sa mga problema na nangangailangan ng nakikitang lohika. Ipinapakita ng modelo ang hayagang pangangatwiran sa bawat hakbang. Gamitin ito para sa mga problema sa math, palaisipan sa lohika, o kapag gusto mong maintindihan ang proseso ng pag-iisip.

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

*Paghahati ng mga problema sa malinaw na lohikal na mga hakbang*

**Limitadong Output** - Para sa mga sagot na may tiyak na format na kailangan. Mahigpit na sinusunod ng modelo ang mga tuntunin sa format at haba. Gamitin ito para sa mga buod o kapag kailangan mo ng eksaktong estruktura ng output.

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

*Pagsunod sa tiyak na format, haba, at mga kinakailangan sa estruktura*

## Paggamit ng Umiiral na Azure Resources

**Siguraduhin ang deployment:**

Tiyaking may `.env` file sa root directory na may Azure credentials (nilikha noong Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, ang module na ito ay tumatakbo na sa port 8083. Maaari mong laktawan ang mga command sa pagsisimula sa ibaba at direktang pumunta sa http://localhost:8083.
**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot applications. Mahahanap mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:
- Makita lahat ng magagamit na Spring Boot applications sa workspace
- Simulan/ihinto ang mga application sa isang click lang
- Tingnan ang mga logs ng application nang real-time
- Subaybayan ang status ng application

I-click lang ang play button sa tabi ng "prompt-engineering" para simulan ang module na ito, o simulan lahat ng modules nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Paggamit ng shell scripts**

Simulan lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa ugat na direktoryo
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

Awtomatikong niloload ng parehong scripts ang mga environment variables mula sa root `.env` file at magbubuo ng JARs kung wala pa.

> **Tandaan:** Kung mas gusto mong manu-manong buuin lahat ng modules bago simulan:
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

Buksan ang http://localhost:8083 sa iyong browser.

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Sa module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Sa module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Mga Screenshot ng Application

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns kasama ang kanilang mga katangian at mga gamit*

## Paggalugad sa Mga Pattern

Pinapayagan ka ng web interface na subukan ang iba't ibang mga prompting strategies. Bawat pattern ay nagsosolusyon ng iba't ibang mga problema - subukan ang mga ito para makita kung kailan nagiging epektibo ang bawat paraan.

> **Tandaan: Streaming vs Non-Streaming** — Bawat pahina ng pattern ay may dalawang button: **🔴 Stream Response (Live)** at isang **Non-streaming** na opsyon. Ang streaming ay gumagamit ng Server-Sent Events (SSE) para ipakita ang mga tokens nang real-time habang ginagawa ito ng modelo, kaya nakikita mo agad ang progreso. Ang non-streaming option naman ay naghihintay ng buong output bago ipakita. Para sa mga prompt na nangangailangan ng malalim na pag-iisip (hal. High Eagerness, Self-Reflecting Code), ang non-streaming na tawag ay maaaring tumagal nang matagal — minsan minuto — nang walang nakikitang feedback. **Gamitin ang streaming kapag nagsasanay sa mga kumplikadong prompt** upang makita ang trabaho ng modelo at maiwasan ang impresyon na na-timeout ang request.
>
> **Tandaan: Pangangailangan sa Browser** — Ang streaming feature ay gumagamit ng Fetch Streams API (`response.body.getReader()`) na nangangailangan ng ganap na browser (Chrome, Edge, Firefox, Safari). Hindi ito gumagana sa built-in na Simple Browser ng VS Code, dahil ang webview nito ay hindi sumusuporta sa ReadableStream API. Kung gagamitin mo ang Simple Browser, gagana pa rin ng normal ang mga non-streaming na button — streaming buttons lang ang apektado. Buksan ang `http://localhost:8083` sa panlabas na browser para sa kumpletong karanasan.

### Low vs High Eagerness

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Low Eagerness. Makakakuha ka ng instant na direktang sagot. Ngayon subukan ang isang kumplikadong tanong tulad ng "Magdisenyo ng caching strategy para sa high-traffic API" gamit ang High Eagerness. I-click ang **🔴 Stream Response (Live)** at panoorin ang detalyadong pag-iisip ng modelo na lumalabas token-by-token. Parehong modelo, parehong istruktura ng tanong - pero sinasabi ng prompt kung gaano kalalim ang pag-iisip nito.

### Task Execution (Mga Tool Preambles)

Nakikinabang ang mga multi-step workflows mula sa maagang pagpaplano at pagsasalaysay ng progreso. Inilalarawan ng modelo kung ano ang gagawin, isinasalaysay ang bawat hakbang, tapos sinusuma ang mga resulta.

### Self-Reflecting Code

Subukan ang "Gumawa ng email validation service". Sa halip na basta gumawa ng code at huminto, ang modelo ay gumagawa, sinusuri laban sa mga quality criteria, tinutukoy ang mga kahinaan, at pinapahusay. Makikita mo itong paulit-ulit na pinapabuti hanggang umabot sa production standards ang code.

### Structured Analysis

Kailangan ng code reviews ng mga consistent na evaluation frameworks. Sina-suri ng modelo ang code gamit ang mga fixed categories (correctness, practices, performance, security) na may severity levels.

### Multi-Turn Chat

Magtanong ng "Ano ang Spring Boot?" tapos agad sundan ng "Ipakita mo ang isang halimbawa". Naaalala ng modelo ang unang tanong mo at nagbibigay ng tiyak na halimbawa tungkol sa Spring Boot. Kung walang memory, magiging masyadong vague ang pangalawang tanong.

### Step-by-Step Reasoning

Pumili ng math problem at subukan ito gamit ang parehong Step-by-Step Reasoning at Low Eagerness. Ang low eagerness ay biglang nagbibigay ng sagot - mabilis pero hindi malinaw ang proseso. Ipinapakita ng step-by-step ang bawat kalkulasyon at desisyon.

### Constrained Output

Kapag kailangan mong magkaroon ng specific na format o bilang ng salita, pinipilit ng pattern na ito ang mahigpit na pagsunod. Subukan gumawa ng summary na eksaktong 100 salita sa bullet point format.

## Ang Talagang Natututuhan Mo

**Binabago ng Effort sa Pagsusuri ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang computational effort sa pamamagitan ng iyong mga prompt. Ang mababang effort ay para sa mabilis na tugon na may kaunting pag-iimbestiga. Ang mataas na effort naman ay nagpapahintulot sa modelo na mag-isip nang malalim. Natututuhan mong iayon ang effort sa kahirapan ng gawain — huwag sayangin ang oras sa mga simpleng tanong, pero huwag rin magmadali sa mga kumplikadong desisyon.

**Nagtuturo ang Estruktura ng Pag-uugali**

Napansin mo ba ang mga XML tag sa mga prompt? Hindi ito palamuti. Mas sinusunod ng mga modelo ang structured instructions kaysa sa malayang text. Kapag kailangan mo ng multi-step na proseso o komplikadong logic, tinutulungan ng estruktura ang modelo na malaman kung nasaan ito at ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng maayos na istrukturang prompt na may malinaw na mga seksyon at organisasyong paraang XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang self-reflecting patterns sa pamamagitan ng paglalantad ng quality criteria. Sa halip na umaasa na "tama ang gawin" ng modelo, sinasabi mo nang eksakto kung ano ang ibig sabihin ng "tama": tamang lohika, pag-handle ng error, performance, seguridad. Kaya nasusuri ng modelo ang sariling output at napapabuti ito. Pinagbabago nito ang pagbuo ng code mula isang laro ng tsansa tungo sa isang proseso.

**May Hangganan ang Konteksto**

Gumagana ang multi-turn conversations sa pamamagitan ng pagsasama ng kasaysayan ng mga mensahe sa bawat request. Pero may limitasyon — bawat modelo ay may maximum token count. Habang lumalaki ang usapan, kailangan mong magkaroon ng mga stratehiya para mapanatili ang kaugnay na konteksto nang hindi naabot ang limitasyon. Itinuturo ng module na ito kung paano gumagana ang memorya; sa susunod matututunan mo kung kailan magsu-summary, kailan kakalimutan, at kailan kukunin muli.

## Mga Susunod na Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Nakaraan: Module 01 - Introduksyon](../01-introduction/README.md) | [Bumalik sa Pangunahing](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, mangyaring maunawaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tumpak na impormasyon. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na mapagkakatiwalaang sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->