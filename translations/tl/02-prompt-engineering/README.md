# Module 02: Prompt Engineering with GPT-5.2

## Table of Contents

- [Video Walkthrough](../../../02-prompt-engineering)
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

## Video Walkthrough

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula sa module na ito: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## What You'll Learn

<img src="../../../translated_images/tl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Sa nakaraang module, nakita mo kung paano pinapayagan ng memorya ang conversational AI at ginamit ang GitHub Models para sa mga pangunahing interaksyon. Ngayon, tututok tayo sa kung paano mo itatanong ang mga tanong — ang mga prompt mismo — gamit ang GPT-5.2 ng Azure OpenAI. Ang paraan ng pag-istruktura mo ng iyong mga prompt ay malaki ang epekto sa kalidad ng mga sagot na makukuha mo. Nagsisimula tayo sa isang pagsusuri ng mga pangunahing teknik sa prompting, at pagkatapos ay lilipat tayo sa walong advanced na pattern na lubos na nagagamit ang kakayahan ng GPT-5.2.

Gagamitin natin ang GPT-5.2 dahil ipinakikilala nito ang kontrol sa pag-iisip - maaari mong sabihin sa modelo kung gaano karaming pag-iisip ang gagawin bago sumagot. Ginagawa nitong mas malinaw ang iba't ibang istratehiya sa prompting at tinutulungan kang maintindihan kung kailan gagamitin ang bawat isang pamamaraan. Makikinabang din tayo mula sa mas kaunting mga rate limit ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Prerequisites

- Natapos ang Module 01 (Azure OpenAI resources na na-deploy na)
- `.env` file sa root directory na may Azure mga kredensyal (nilikhang gamit ang `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundan muna ang mga tagubilin sa deployment doon.

## Understanding Prompt Engineering

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdisenyo ng input na teksto na patuloy na nagbibigay sa iyo ng mga resulta na kailangan mo. Hindi lang ito tungkol sa pagtatanong — ito ay tungkol sa pag-istruktura ng mga kahilingan upang maunawaan ng modelo nang eksakto kung ano ang gusto mo at kung paano ito ihahatid.

Isipin ito na parang pagbibigay ng mga tagubilin sa isang katrabaho. "Ayusin ang bug" ay malabo. "Ayusin ang null pointer exception sa UserService.java linya 45 sa pamamagitan ng pagdaragdag ng null check" ay tiyak. Ganun din ang mga language model — mahalaga ang pagiging tiyak at istruktura.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Nagbibigay ang LangChain4j ng imprastraktura — mga koneksyon sa modelo, memorya, at mga uri ng mensahe — habang ang mga prompt pattern ay mga maingat na istrukturadong teksto na ipinapadala mo sa imprastrakturang iyon. Ang mga pangunahing bahagi ay ang `SystemMessage` (na nagseset ng kilos at papel ng AI) at `UserMessage` (na nagdadala ng iyong tunay na kahilingan).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago tayo sumabak sa mga advanced na pattern sa module na ito, balikan muna natin ang limang pundamental na teknik sa prompting. Ito ang mga pundasyon na dapat malaman ng bawat prompt engineer. Kung natalakay mo na ang [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na ang mga ito sa aksyon — narito ang konseptwal na balangkas sa likod nila.

### Zero-Shot Prompting

Ang pinakasimpleng paraan: bigyan ang modelo ng direktang tagubilin na walang mga halimbawa. Nakasandig ang modelo nang buong-buo sa kanyang pagsasanay upang maintindihan at ipatupad ang gawain. Epektibo ito para sa mga pamilyar na kahilingan kung saan malinaw ang inaasahang kilos.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang tagubilin nang walang mga halimbawa — hinihinuha ng modelo ang gawain mula sa tagubilin mismo*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Simpleng klasipikasyon, direktang tanong, pagsasalin, o anumang gawain na kayang gawin ng modelo nang walang dagdag na patnubay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na gusto mong sundan ng modelo. Natutunan ng modelo ang inaasahang input-output na format mula sa iyong mga halimbawa at inaaplay ito sa mga bagong input. Malaki ang naitutulong nito sa pagiging consistent para sa mga gawain kung saan hindi obvious ang nais na format o kilos.

<img src="../../../translated_images/tl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Pagtututo mula sa mga halimbawa — nakakakita ang modelo ng pattern at inaaplay ito sa bagong mga input*

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

**Kailan gagamitin:** Pasadyang klasipikasyon, consistent na pag-format, mga gawain na tiyak sa domain, o kapag hindi consistent ang mga resulta ng zero-shot.

### Chain of Thought

Hilingin sa modelo na ipakita ang kaniyang pag-iisip hakbang-hakbang. Sa halip na direktang sumagot, pinaghahati-hati ng modelo ang problema at tinatrabahong isa-isang bahagi nang malinaw. Nakakatulong ito ng husto sa katumpakan ng math, lohika, at multi-step na pag-iisip.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Hakbang-hakbang na pag-iisip — paghahati ng mga komplikadong problema sa malinaw na mga hakbang ng lohika*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, pagkatapos 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Problema sa math, palaisipan sa lohika, pag-debug, o anumang gawain kung saan ang pagpapakita ng proseso ng pag-iisip ay nagpapabuti ng katumpakan at pagtitiwala.

### Role-Based Prompting

Itakda ang isang persona o papel para sa AI bago itanong ang iyong tanong. Nagbibigay ito ng konteksto na humuhubog sa tono, lalim, at pokus ng tugon. Ibang payo ang ibinibigay ng "software architect" kaysa sa "junior developer" o "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pagtatakda ng konteksto at persona — ang iisang tanong ay nagkakaroon ng ibang tugon depende sa papel na itinakda*

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

**Kailan gagamitin:** Pag-review ng code, pagtuturo, domain-specific na pagsusuri, o kapag kailangan mo ng mga tugon na angkop sa partikular na antas ng kadalubhasaan o pananaw.

### Prompt Templates

Gumawa ng mga reusable na prompt na may variable placeholders. Sa halip na magsulat ng bagong prompt tuwing gagamitin, magtakda ng isang template at palitan ang mga halaga. Pinapasimple ito ng `PromptTemplate` class ng LangChain4j gamit ang `{{variable}}` na syntax.

<img src="../../../translated_images/tl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Mga reusable na prompt na may mga variable placeholder — isang template, maraming gamit*

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

**Kailan gagamitin:** Mga paulit-ulit na tanong na may iba’t ibang input, batch processing, paggawa ng reusable AI workflows, o anumang senaryo kung saan pareho ang istruktura ng prompt ngunit nag-iiba ang datos.

---

Ang limang pundamental na ito ay nagbibigay sa iyo ng matibay na gamit para sa karamihan ng mga gawain sa prompting. Ang natitirang bahagi ng module na ito ay bumubuo sa mga ito gamit ang **walong advanced na pattern** na gumagamit ng reasoning control, self-evaluation, at structured output na kakayahan ng GPT-5.2.

## Advanced Patterns

Matapos masaklaw ang mga pundamental, lumipat tayo sa walong advanced na mga pattern na nagpapatangi sa module na ito. Hindi lahat ng problema ay kailangang pareho ang diskarte. May mga tanong na nangangailangan ng mabilis na sagot, at may iba naman na kailangan ng malalim na pag-iisip. May ilan na gusto mong makita ang lohika, may ilan namang sapat na ang resulta lamang. Ang bawat pattern sa ibaba ay naka-optimize para sa iba’t ibang sitwasyon — at lalo pang pinapalinaw ito ng reasoning control ng GPT-5.2.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang-ideya ng walong prompt engineering patterns at kanilang mga gamit*

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Ang reasoning control ng GPT-5.2 ay nagpapahintulot sa iyo na tukuyin kung gaano kalalim ang pag-iisip ng modelo — mula sa mabilis na direktang sagot hanggang sa malalim na pagsusuri*

**Low Eagerness (Mabilis at Nakatuon)** - Para sa mga simpleng tanong kung saan gusto mo ng mabilis at direktang sagot. Minimal lang ang pag-iisip ng modelo - maximum na 2 hakbang. Gamitin ito para sa mga kalkulasyon, paghahanap, o mga tuwirang tanong.

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

> 💡 **Subukan gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness na prompting pattern?"
> - "Paano nakatutulong ang mga XML tag sa mga prompt sa pag-istruktura ng tugon ng AI?"
> - "Kailan ko dapat gamitin ang self-reflection pattern laban sa direktang tagubilin?"

**High Eagerness (Malalim at Masusing Pagsusuri)** - Para sa mga komplikadong problema kung saan gusto mo ng komprehensibong pagsusuri. Malalim at detalyado ang pag-iisip ng modelo. Gamitin ito para sa disenyo ng sistema, mga desisyon sa arkitektura, o kumplikadong pananaliksik.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Hakbang-hakbang na Progreso)** - Para sa multi-step na workflows. Nagbibigay ang modelo ng plano sa simula, nagsasalaysay ng bawat hakbang habang ginagawa, at nagbibigay ng buod pagkatapos. Gamitin ito para sa mga migration, implementasyon, o anumang multi-step na proseso.

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

Ang Chain-of-Thought prompting ay tahasang hinihiling sa modelo na ipakita ang proseso ng pag-iisip, na nagpapabuti ng katumpakan para sa mga komplikadong gawain. Nakakatulong ang hakbang-hakbang na paghahati sa parehong tao at AI na maintindihan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa mga long-running na operasyon?"
> - "Ano ang mga best practice sa pag-istruktura ng tool preambles sa mga production na aplikasyon?"
> - "Paano ko mahuhuli at maipapakita ang mga intermediate na update ng progreso sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plano → Gawin → Buodin na workflow para sa multi-step na gawain*

**Self-Reflecting Code** - Para sa paggawa ng kodigo na mataas ang kalidad para sa production. Gumagawa ang modelo ng kodigo na sumusunod sa mga pamantayan ng produksyon kasama ang angkop na error handling. Gamitin ito kapag gumagawa ng mga bagong feature o serbisyo.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratibong paikot ng pagpapabuti - gumawa, suriin, tukuyin ang isyu, pagbutihin, ulitin*

**Structured Analysis** - Para sa consistent na pagsusuri. Sinusuri ng modelo ang kodigo gamit ang isang tiyak na framework (katumpakan, mga gawi, performance, seguridad, maintainability). Gamitin ito para sa pag-review ng code o pagtatasa ng kalidad.

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
> - "Paano ko maiaangkop ang analysis framework para sa iba’t ibang uri ng pag-review ng code?"
> - "Ano ang pinakamainam na paraan para i-parse at aksyunan ang structured output programmatically?"
> - "Paano ko masisiguro na consistent ang severity levels sa iba't ibang sesyon ng pag-review?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework para sa consistent na pag-review ng code kasama ang severity levels*

**Multi-Turn Chat** - Para sa mga usapan na nangangailangan ng konteksto. Naaalala ng modelo ang mga naunang mensahe at pinauunlad ang usapan. Gamitin ito para sa interactive na sessions ng tulong o komplikadong Q&A.

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

*Paano nakakalikom ang konteksto ng usapan sa maraming turn hanggang maabot ang token limit*

**Step-by-Step Reasoning** - Para sa mga problema na nangangailangan ng nakikitang lohika. Ipinapakita ng modelo ang malinaw na pag-iisip para sa bawat hakbang. Gamitin ito para sa math problems, logic puzzles, o kapag gusto mong maintindihan ang proseso ng pag-iisip.

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

*Paghahati ng mga problema sa malinaw na mga hakbang ng lohika*

**Constrained Output** - Para sa mga sagot na may partikular na kinakailangan sa format. Mahigpit na sinusunod ng modelo ang mga panuntunan sa format at haba. Gamitin ito para sa mga buod o kapag kailangan mo ng eksaktong istruktura ng output.

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

*Pagsunod sa partikular na format, haba, at istruktura ng mga kinakailangan*

## Using Existing Azure Resources

**Suriin ang deployment:**

Siguraduhing naroroon ang `.env` file sa root directory na may Azure mga kredensyal (nilikhang noong Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Note:** Kung sinimulan mo na ang lahat ng mga application gamit ang `./start-all.sh` mula sa Module 01, tumatakbo na ang module na ito sa port 8083. Maari mong laktawan ang mga start command sa ibaba at diretso na sa http://localhost:8083.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**
Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface upang pamahalaan ang lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari kang:
- Makita ang lahat ng magagamit na Spring Boot applications sa workspace
- Simulan/tigilin ang mga aplikasyon sa isang pag-click lang
- Tingnan ang logs ng aplikasyon nang real-time
- I-monitor ang status ng aplikasyon

I-click lang ang play button sa tabi ng "prompt-engineering" upang simulan ang module na ito, o simulan ang lahat ng mga module nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Paggamit ng shell scripts**

Simulan ang lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa direktoryo ng ugat
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa punong direktoryo
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

Parehong awtomatikong niloload ng mga scripts ang mga environment variables mula sa root na `.env` file at magtatayo ng mga JAR kung wala pa ang mga ito.

> **Note:** Kung mas gusto mong i-build ang lahat ng modules nang manu-mano bago magsimula:
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
./stop.sh  # Module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng module
```

**PowerShell:**
```powershell
.\stop.ps1  # Para lamang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Mga Screenshot ng Aplikasyon

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns kasama ang kanilang mga katangian at mga kaso ng paggamit*

## Pagsusuri sa Mga Pattern

Pinapayagan ka ng web interface na subukan ang iba't ibang prompting strategies. Bawat pattern ay naglutas ng iba't ibang problema - subukan ang mga ito upang makita kung kailan pinakamahusay gamitin ang bawat isa.

> **Note: Streaming vs Non-Streaming** — Bawat pahina ng pattern ay may dalawang button: **🔴 Stream Response (Live)** at isang **Non-streaming** na opsyon. Ginagamit ng streaming ang Server-Sent Events (SSE) para ipakita ang mga token nang real-time habang ginagawa ito ng modelo, kaya agad mong nakikita ang progreso. Ang non-streaming option naman ay hinihintay ang buong sagot bago ito ipakita. Para sa mga prompt na nagti-trigger ng malalim na pag-iisip (hal., High Eagerness, Self-Reflecting Code), maaaring tumagal nang matagal ang non-streaming call — minsan ay ilang minuto — na walang nakikitang feedback. **Gamitin ang streaming kapag nagsusubok ng komplikadong mga prompt** para makita mong gumagana ang modelo at maiwasan ang impresyon na nag-timeout ang request.
>
> **Note: Browser Requirement** — Ginagamit ng streaming feature ang Fetch Streams API (`response.body.getReader()`) na nangangailangan ng full browser (Chrome, Edge, Firefox, Safari). Hindi ito gumagana sa built-in Simple Browser ng VS Code dahil hindi sinusuportahan ng webview nito ang ReadableStream API. Kung gagamit ka ng Simple Browser, gagana naman nang normal ang non-streaming buttons — ang apektado lang ay ang streaming buttons. Buksan ang `http://localhost:8083` sa external browser para sa buong karanasan.

### Mababa vs Mataas na Eagerness

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Mababang Eagerness. Makakakuha ka ng agarang direktang sagot. Ngayon subukan ang kumplikadong tanong na "Disenyo ng caching strategy para sa high-traffic API" gamit ang Mataas na Eagerness. I-click ang **🔴 Stream Response (Live)** at panoorin ang detalyadong pag-iisip ng modelo na lumalabas token-by-token. Parehong modelo, parehas na estruktura ng tanong — ngunit sinasabi ng prompt kung gaano karaming pag-iisip ang gagawin.

### Pagpapatupad ng Gawain (Tool Preambles)

Nakikinabang ang multi-step workflows mula sa maagang pagpaplano at pagsasalaysay ng progreso. Inilalarawan ng modelo kung ano ang gagawin, sinasalaysay ang bawat hakbang, pagkatapos ay pinagsasama ang mga resulta.

### Self-Reflecting Code

Subukan ang "Lumikha ng email validation service". Sa halip na gumenerate lang ng code at tumigil, gumagawa ang modelo, sinusuri batay sa mga pamantayan ng kalidad, tinutukoy ang kahinaan, at pinapabuti. Makikita mo itong umuulit hanggang ang code ay umabot sa pamantayan para sa produksyon.

### Structured Analysis

Kailangang may konsistenteng evaluation frameworks ang mga code review. Sinusuri ng modelo ang code gamit ang mga nakapirming kategorya (katumpakan, praktis, performance, seguridad) na may mga level ng kalubhaan.

### Multi-Turn Chat

Magtanong ng "Ano ang Spring Boot?" pagkatapos ay agad na sundan ng "Ipakita mo sa akin ang isang halimbawa". Naalala ng modelo ang unang tanong mo at bibigyan ka ng isang halimbawa ng Spring Boot nang eksakto. Kung walang memorya, masyadong malabo ang pangalawang tanong.

### Step-by-Step Reasoning

Pumili ng problema sa matematika at subukan ito gamit ang parehong Step-by-Step Reasoning at Low Eagerness. Ang Low eagerness ay nagbibigay lang ng sagot - mabilis pero hindi malinaw. Ipinapakita ng step-by-step ang bawat kalkulasyon at desisyon.

### Constrained Output

Kapag kailangan mo ng tiyak na format o bilang ng salita, pini-pilit ng pattern na ito ang mahigpit na pagsunod. Subukan gumawa ng buod na may eksaktong 100 salita sa bullet point na format.

## Ang Talagang Iyong Natututuhan

**Binabago ng Pagsisikap sa Pangangatwiran ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang computational effort sa pamamagitan ng mga prompt mo. Ang mababang effort ay nangangahulugang mabilis na mga sagot na may kaunting eksplorasyon. Ang mataas na effort naman ay nangangahulugang magtatagal ang modelo sa malalim na pag-iisip. Natututuhan mong itugma ang effort sa pagiging komplikado ng gawain - huwag sayangin ang oras sa mga simpleng tanong, ngunit huwag din magmadali sa mga komplikadong desisyon.

**Gabay ng Estruktura sa Ugali**

Napapansin mo ba ang mga XML tags sa mga prompt? Hindi ito para lamang palamuti. Mas sumusunod ang mga modelo sa istrukturadong mga instruksyon kaysa sa malayang teksto. Kapag kailangan mo ng multi-step na proseso o kumplikadong lohika, tinutulungan ng estruktura ang modelo na matunton kung nasaan ito at kung ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng isang mahusay na istrukturadong prompt na may malinaw na mga seksyon at organisasyong estilo-XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang self-reflecting patterns sa pamamagitan ng pagpapahayag nang malinaw ng mga pamantayan ng kalidad. Sa halip na umasa na "tama ang gagawin" ng modelo, sinasabi mo nang eksakto kung ano ang ibig sabihin ng "tama": tamang lohika, paghawak ng error, performance, seguridad. Kaya nitong suriin ang sarili nitong output at pagandahin ito. Ginagawa nitong proseso ng paggawa ng code ang halip na suwerte lang.

**May Hangganan ang Konteksto**

Gumagana ang multi-turn conversations sa pamamagitan ng pagsama ng kasaysayan ng mga mensahe sa bawat request. Ngunit may limitasyon - bawat modelo ay may maximum token count. Habang lumalaki ang mga pag-uusap, kakailanganin mong mga estratehiya para mapanatili ang mahalagang konteksto nang hindi lalampas sa limitasyon. Itinuturo ng module na ito kung paano gumagana ang memorya; sa susunod matututuhan mo kung kailan magsi-summarize, kailan makakalimot, at kailan kukuha muli.

## Mga Susunod na Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Nakaraan: Module 01 - Introduction](../01-introduction/README.md) | [Pabalik sa Pangunahing](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pabatid**:
Ang dokumentong ito ay naisalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami na maging tumpak, pakatandaan na ang mga awtomatikong salin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang katutubong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->