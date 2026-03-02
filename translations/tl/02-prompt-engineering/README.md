# Module 02: Prompt Engineering gamit ang GPT-5.2

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
- [Run the Application](../../../02-prompt-engineering)
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

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula sa module na ito:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

Ang sumusunod na diagram ay nagbibigay ng pangkalahatang ideya ng mga pangunahing paksa at kasanayan na iyong mauunawaan sa module na ito — mula sa mga teknik ng pagpipino ng prompt hanggang sa step-by-step workflow na iyong susundan.

<img src="../../../translated_images/tl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Sa mga naunang module, na-explore mo ang mga basic LangChain4j na interaksyon gamit ang GitHub Models at nakita kung paano pinapagana ng memorya ang conversational AI gamit ang Azure OpenAI. Ngayon, tututok tayo sa kung paano ka nagtatanong — ang mismong mga prompt — gamit ang GPT-5.2 ng Azure OpenAI. Malaki ang epekto ng pagstruktura ng mga prompt sa kalidad ng mga sagot na matatanggap mo. Magsisimula tayo sa pag-review ng mga pangunahing teknik sa pag-prompt, tapos lilipat tayo sa walong advanced na pattern na ganap na nakikinabang sa kakayahan ng GPT-5.2.

Gagamit tayo ng GPT-5.2 dahil ipinakikilala nito ang reasoning control - maaari mong sabihin sa modelo kung gaano karami ang pag-iisip bago sumagot. Pinapalinaw nito ang iba't ibang estratehiya ng pag-prompt at tinutulungan kang maintindihan kung kailan gagamitin ang bawat paraan. Magkakaroon din tayo ng benepisyo sa mas kaunting rate limits ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Prerequisites

- Nakumpleto ang Module 01 (Azure OpenAI resources ay nadeploy na)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang deployment instructions doon.

## Understanding Prompt Engineering

Sa pinakapuno ng kahulugan, ang prompt engineering ay ang pagkakaiba sa pagitan ng malabong instruksyon at tiyak na mga instruksyon, tulad ng ipinapakita ng paghahambing sa ibaba.

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdisenyo ng input na teksto na palaging nagbibigay sa iyo ng mga resulta na kailangan mo. Hindi ito tungkol lang sa pagtatanong - ito ay tungkol sa pagstruktura ng mga kahilingan upang maunawaan ng modelo kung ano talaga ang gusto mo at kung paano ito ihahatid.

Isipin mo ito na parang pagbibigay ng mga instruksiyon sa isang katrabaho. "Ayusin ang bug" ay malabo. "Ayusin ang null pointer exception sa UserService.java linya 45 sa pamamagitan ng pagdagdag ng null check" ay tiyak. Ganun din ang mga language model - mahalaga ang pagiging tiyak at ang kaayusan.

Ipinapakita ng diagram sa ibaba kung paano kasya ang LangChain4j sa larangang ito — ikinakabit ang iyong mga prompt pattern sa modelo sa pamamagitan ng mga SystemMessage at UserMessage na mga bahagi.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Nagbibigay ang LangChain4j ng imprastruktura — mga koneksyon sa modelo, memorya, at uri ng mga mensahe — habang ang mga prompt pattern ay mga maingat na naistrukturang teksto na ipinapadala mo sa pamamagitan ng imprastrukturang iyon. Ang mga pangunahing bahagi ay ang `SystemMessage` (na nagtatakda ng ugali at papel ng AI) at `UserMessage` (na nagdadala ng iyong aktwal na kahilingan).

## Prompt Engineering Fundamentals

Ang limang pangunahing teknik na ipinapakita sa ibaba ay bumubuo sa pundasyon ng epektibong prompt engineering. Bawat isa ay tumutugon sa ibang aspeto kung paano ka makikipagkomunikasyon sa mga language model.

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago pumasok sa mga advanced na pattern sa module na ito, balikan muna natin ang limang pundamental na teknik sa pag-prompt. Ito ang mga bloke ng pundasyon na dapat malaman ng bawat prompt engineer. Kung napag-aralan mo na ang [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na ang mga ito sa aksyon — narito ang konseptwal na balangkas sa likod nila.

### Zero-Shot Prompting

Ang pinakasimpleng paraan: bigyan ang modelo ng direktang utos na walang mga halimbawa. Umaasa ang modelo nang buo sa kanyang pagsasanay upang maunawaan at maisakatuparan ang gawain. Epektibo ito sa mga tuwirang kahilingan kung saan halata ang inaasahang ugali.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang utos nang walang mga halimbawa — ang modelo ay naghihinuha ng gawain mula sa utos lamang*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Mga simpleng klasipikasyon, direktang tanong, pagsasalin, o anumang gawaing kaya ng modelo nang walang dagdag na gabay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na nais mong sundan ng modelo. Natututuhan ng modelo ang inaasahang format ng input-output mula sa mga halimbawa mo at inilalapat ito sa mga bagong input. Lubhang pinapaganda nito ang konsistensi para sa mga gawaing kung saan hindi halata ang nais na format o ugali.

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

**Kailan gagamitin:** Pasadyang klasipikasyon, pare-parehong pag-format, mga gawain sa espesipikong domain, o kapag hindi consistent ang zero-shot na resulta.

### Chain of Thought

Tanungin ang modelo na ipakita ang pag-iisip nito nang hakbang-hakbang. Sa halip na diretsong sumagot, hinahati-hati ng modelo ang problema at nilalakad ito ng bawat bahagi nang malinaw. Pinapabuti nito ang katumpakan sa math, lohika, at mga gawaing may maraming hakbang ng pag-iisip.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Hakbang-hakbang na pag-iisip — paghahati ng kumplikadong problema sa mga malinaw na lohikal na hakbang*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, pagkatapos ay 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Mga problema sa math, palaisipan sa lohika, debugging, o anumang gawain kung saan pinapabuti ng pagpapakita ng proseso ng pag-iisip ang katumpakan at pagtitiwala.

### Role-Based Prompting

Itakda ang persona o papel para sa AI bago mo itanong ang iyong tanong. Nagbibigay ito ng konteksto na humuhubog sa tono, lalim, at pokus ng sagot. Ang "software architect" ay nagbibigay ng ibang payo kaysa sa "junior developer" o isang "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pagtatakda ng konteksto at persona — ang parehong tanong ay nakakakuha ng ibang sagot depende sa itinalagang papel*

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

**Kailan gagamitin:** Mga pagsusuri ng code, pagtuturo, espesipikong pagsusuri sa domain, o kapag kailangan mo ng mga sagot na iniangkop sa partikular na antas ng kadalubhasaan o pananaw.

### Prompt Templates

Lumikha ng mga reusable na prompt na may mga variable placeholder. Sa halip na magsulat ng bagong prompt kada oras, tukuyin muna ang template at punan ito ng iba't ibang mga halaga. Ginagawa itong madali ng LangChain4j na `PromptTemplate` class gamit ang `{{variable}}` syntax.

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

**Kailan gagamitin:** Paulit-ulit na mga query na may iba't ibang input, batch processing, pagbuo ng reusable na AI workflows, o anumang senaryo kung saan pareho ang istruktura ng prompt ngunit nagbabago ang datos.

---

Ang limang pundamental na ito ay nagbibigay sa iyo ng matibay na kagamitan para sa karamihan ng mga gawaing prompt. Ang natitirang bahagi ng module na ito ay nakabase sa mga ito gamit ang **walo pang advanced na mga pattern** na gumagamit ng reasoning control, self-evaluation, at structured output capabilities ng GPT-5.2.

## Advanced Patterns

Matapos matalakay ang mga pundamental, lumipat tayo sa walong advanced na pattern na ginagawang natatangi ang module na ito. Hindi lahat ng problema ay nangangailangan ng parehong pamamaraan. Ang iba ay nangangailangan ng mabilisang sagot, ang ilan naman ay malalim na pag-iisip. Ang iba ay kailangan ng nakikitang pag-iisip, ang iba naman ay kailangan lang ng mga resulta. Ang bawat pattern sa ibaba ay naka-optimize para sa iba’t ibang senaryo — at pinatutunayan ng reasoning control ng GPT-5.2 ang pagkakaiba ng mga ito.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang ideya ng walong prompt engineering pattern at kanilang mga gamit*

Nagdadagdag ang GPT-5.2 ng isa pang dimensyon sa mga pattern na ito: *reasoning control*. Ipinapakita ng slider sa ibaba kung paano mo maaaring ayusin ang pagsisikap sa pag-iisip ng modelo — mula sa mabilis, diretso na mga sagot hanggang sa malalim, masusing pagsusuri.

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Hinahayaan ka ng reasoning control ng GPT-5.2 na tukuyin kung gaano karaming pag-iisip ang dapat gawin ng modelo — mula sa mabilis na direktang sagot hanggang sa malalim na pagsasaliksik*

**Low Eagerness (Mabilis at Nakatuon)** - Para sa mga simpleng tanong kung saan gusto mo ng mabilis at diretsong sagot. Minimal lang ang pag-iisip ng modelo - maximum na 2 hakbang. Gamitin ito sa mga kalkulasyon, paghahanap, o mga tuwirang tanong.

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
> - "Ano ang pagkakaiba ng low eagerness at high eagerness na mga prompt pattern?"
> - "Paano nakakatulong ang XML tags sa prompts sa pagstruktura ng sagot ng AI?"
> - "Kailan dapat gumamit ng self-reflection patterns kumpara sa direktang utos?"

**High Eagerness (Malalim at Masusing)** - Para sa mga komplikadong problema kung saan gusto mo ng komprehensibong pagsusuri. Malalim na sinusuri ng modelo at ipinapakita ang detalyadong pag-iisip. Gamitin ito para sa system design, architecture decisions, o komplikadong pananaliksik.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - Para sa mga workflows na may maraming hakbang. Nagbibigay ang modelo ng plano sa umpisa, ikinukwento ang bawat hakbang habang ginagawa ito, pagkatapos ay nagbibigay ng buod. Gamitin ito para sa migrations, implementasyon, o anumang multi-step na proseso.

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

Ang Chain-of-Thought prompting ay tahasang hinihiling sa modelo na ipakita ang kanyang proseso ng pag-iisip, na nagpapabuti ng katumpakan para sa komplikadong mga gawain. Ang hakbang-hakbang na breakdown ay tumutulong pareho sa tao at AI na maintindihan ang lohika.

> **🤖 Subukan sa [GitHub Copilot](https://github.com/features/copilot) Chat:** Itanong tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa mga matagal na operation?"
> - "Ano ang mga best practice para sa pagstruktura ng tool preambles sa production applications?"
> - "Paano ko mahuhuli at maipapakita ang mga intermediate progress update sa UI?"

Ipinapakita ng diagram sa ibaba ang Plan → Execute → Summarize workflow na ito.

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow para sa multi-step na mga gawain*

**Self-Reflecting Code** - Para sa pagbuo ng production-quality na code. Gumagawa ang modelo ng code ayon sa mga production standards na may tamang error handling. Gamitin ito kapag nagtatayo ng mga bagong feature o serbisyo.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Ipinapakita ng diagram sa ibaba ang paulit-ulit na proseso ng pagbuti — gumawa, suriin, tukuyin ang mga kahinaan, at pinuhin hanggang matugunan ng code ang mga pamantayan ng production.

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Paulit-ulit na proseso ng pagbuti - gumawa, suriin, tukuyin ang mga isyu, pagbutihin, ulitin*

**Structured Analysis** - Para sa consistent na pagsusuri. Sine-review ng modelo ang code gamit ang isang fixed na framework (katumpakan, pagsasanay, performance, seguridad, maintainability). Gamitin ito para sa mga code review o pagsusuri ng kalidad.

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

> **🤖 Subukan sa [GitHub Copilot](https://github.com/features/copilot) Chat:** Itanong tungkol sa structured analysis:
> - "Paano ko ma-customize ang analysis framework para sa iba't ibang uri ng code review?"
> - "Ano ang pinakamahusay na paraan para i-parse at gampanan ang structured output programmatically?"
> - "Paano ko matitiyak ang consistent na severity levels sa iba't ibang mga review session?"

Ipinapakita ng sumusunod na diagram kung paano ini-organisa ng structured framework na ito ang code review sa mga konsistenteng kategorya na may severity levels.

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework para sa konsistenteng code review na may severity levels*

**Multi-Turn Chat** - Para sa mga pag-uusap na nangangailangan ng konteksto. Natatandaan ng modelo ang mga nakaraang mensahe at nililinang ang mga ito. Gamitin ito para sa interactive help sessions o kumplikadong Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Ipinapakita ng diagram sa ibaba kung paano naiipon ang konteksto ng pag-uusap sa bawat turn at kung paano ito nauugnay sa token limit ng modelo.

<img src="../../../translated_images/tl/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Paano naiipon ang konteksto ng pag-uusap sa maraming turn hanggang maabot ang token limit*
**Step-by-Step Reasoning** - Para sa mga problema na nangangailangan ng malinaw na lohika. Ipinapakita ng modelo ang malinaw na pangangatwiran para sa bawat hakbang. Gamitin ito para sa mga problemang matematika, palaisipan sa lohika, o kapag kailangang maunawaan ang proseso ng pag-iisip.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Ang diagram sa ibaba ay nagpapakita kung paano hinahati ng modelo ang mga problema sa malinaw at naka-numero na mga hakbang ng lohika.

<img src="../../../translated_images/tl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Pag-hati ng mga problema sa malinaw na mga hakbang ng lohika*

**Constrained Output** - Para sa mga sagot na may tiyak na mga kinakailangan sa format. Mahigpit na sinusunod ng modelo ang mga tuntunin sa format at haba. Gamitin ito para sa mga buod o kapag kailangan ng eksaktong istruktura ng output.

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

Ipinapakita ng sumusunod na diagram kung paano ginagabayan ng mga limitasyon ang modelo upang gumawa ng output na mahigpit na sumusunod sa iyong mga kinakailangan sa format at haba.

<img src="../../../translated_images/tl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Pagpapatupad ng mga tiyak na kinakailangan sa format, haba, at istruktura*

## Patakbuhin ang Aplikasyon

**Suriin ang deployment:**

Tiyakin na ang file na `.env` ay nasa root directory kasama ang mga kredensyal ng Azure (nilikha noong Module 01). Patakbuhin ito mula sa direktoryo ng module (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tala:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa root directory (tulad ng inilarawan sa Module 01), tumatakbo na ang module na ito sa port 8083. Maaari mong laktawan ang mga utos ng pagsisimula sa ibaba at direktang pumunta sa http://localhost:8083.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface upang pamahalaan ang lahat ng Spring Boot na aplikasyon. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:
- Makita ang lahat ng available na Spring Boot na aplikasyon sa workspace
- Simulan/hintuin ang aplikasyon sa isang click lang
- Tingnan ang mga log ng aplikasyon nang real-time
- Subaybayan ang status ng aplikasyon

I-click lang ang play button sa tabi ng "prompt-engineering" para simulan ang module na ito, o simulan lahat ng module nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Ang Spring Boot Dashboard sa VS Code — simulan, hintuin, at subaybayan ang lahat ng module mula sa isang lugar*

**Opsyon 2: Paggamit ng shell scripts**

Simulan ang lahat ng web application (modules 01-04):

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

Ang parehong mga script ay awtomatikong naglo-load ng mga environment variable mula sa root `.env` file at magbu-build ng mga JAR kung wala pa ito.

> **Tala:** Kung gusto mong manu-manong i-build ang lahat ng module bago simulan:
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

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Para sa modulong ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Tanging module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Mga Screenshot ng Aplikasyon

Narito ang pangunahing interface ng prompt engineering module, kung saan maaari mong subukan lahat ng walong pattern nang magkatabi.

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering pattern kasama ang kanilang mga katangian at gamit*

## Pagsusuri sa mga Pattern

Pinapayagan ka ng web interface na subukan ang iba't ibang estratehiya sa pag-prompt. Bawat pattern ay naglutas ng ibang problema - subukan upang makita kung kailan angkop ang bawat paraan.

> **Tala: Streaming vs Non-Streaming** — Bawat pattern page ay may dalawang button: **🔴 Stream Response (Live)** at isang **Non-streaming** na opsyon. Ang streaming ay gumagamit ng Server-Sent Events (SSE) upang ipakita ang mga token nang real-time habang ginagawa ito ng modelo, kaya makikita mo agad ang progreso. Ang non-streaming na opsyon ay naghihintay ng buong sagot bago ito ipakita. Para sa mga prompt na nangangailangan ng malalim na pag-iisip (hal. High Eagerness, Self-Reflecting Code), ang non-streaming call ay maaaring tumagal nang matagal — minsan ay mga minuto — nang walang nakikitang feedback. **Gamitin ang streaming kapag nagsusubok ng mga komplikadong prompt** upang makita kung paano gumagana ang modelo at maiwasan ang impresyon na nag-timeout ang request.
>
> **Tala: Kinakailangan ng Browser** — Ginagamit ng streaming feature ang Fetch Streams API (`response.body.getReader()`) na nangangailangan ng full browser (Chrome, Edge, Firefox, Safari). Hindi ito gumagana sa built-in Simple Browser ng VS Code, dahil hindi sinusuportahan ng webview nito ang ReadableStream API. Kung gagamitin ang Simple Browser, gagana pa rin ang non-streaming buttons nang normal — streaming buttons lang ang maaapektuhan. Buksan ang `http://localhost:8083` sa panlabas na browser para sa buong karanasan.

### Low vs High Eagerness

Magtanong ng simpleng tanong tulad ng "What is 15% of 200?" gamit ang Low Eagerness. Makakakuha ka ng agad-agad, direktang sagot. Ngayon magtanong ng komplikado tulad ng "Design a caching strategy for a high-traffic API" gamit ang High Eagerness. I-click ang **🔴 Stream Response (Live)** at panoorin ang detalyadong pangangatwiran ng modelo lumitaw token-by-token. Parehong modelo, parehong estruktura ng tanong - pero sinasabi ng prompt kung gaano kalalim ang pag-iisip.

### Pagpapatupad ng Task (Tool Preambles)

Nakikinabang ang multi-step workflows mula sa maagang pagpaplano at pagsusulat ng progreso. Inilalahad ng modelo kung ano ang gagawin, kinukwento ang bawat hakbang, pagkatapos ay binubuod ang mga resulta.

### Self-Reflecting Code

Subukan ang "Create an email validation service". Sa halip na gumawa lang ng code at tumigil, gumagawa ang modelo, sinusuri batay sa mga pamantayan ng kalidad, tinutukoy ang kahinaan, at nagpapaayos. Makikita mo itong ulit-ulitin hanggang maabot ang pamantayan para sa produksyon.

### Structured Analysis

Kailangan ng code reviews ng consistent na evaluasyon. Ina-analisa ng modelo ang code gamit ang mga fixed na kategorya (tama, practices, performance, security) at mga antas ng kalubhaan.

### Multi-Turn Chat

Magtanong ng "What is Spring Boot?" pagkatapos ay sunod na tanungin kaagad ng "Show me an example". Naaalala ng modelo ang iyong unang tanong at nagbibigay ng halimbawa ng Spring Boot nang partikular. Kung walang memorya, magiging masyadong malabo ang pangalawang tanong.

### Step-by-Step Reasoning

Pumili ng problemang matematika at subukan ito gamit ang Step-by-Step Reasoning at Low Eagerness. Ang low eagerness ay nagbibigay lang ng sagot - mabilis pero hindi maliwanag. Ang step-by-step ay ipinapakita ang bawat kalkulasyon at desisyon.

### Constrained Output

Kapag kailangan mo ng tiyak na mga format o bilang ng salita, pinatutupad ng pattern na ito ang mahigpit na pagsunod. Subukang gumawa ng buod na eksaktong 100 salita sa bullet point format.

## Ano ang Talagang Natututunan Mo

**Binabago ng Pagsisikap sa Pangangatwiran ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang computational effort sa pamamagitan ng iyong mga prompt. Ang mababang effort ay nangangahulugang mabilis na sagot na may kaunting pagsisiyasat. Ang mataas na effort ay nangangahulugang maglalaan ng oras ang modelo upang mag-isip nang malalim. Natututo kang iayon ang effort sa kahirapan ng gawain - huwag magaksaya ng oras sa simpleng tanong, pero huwag rin magmadali sa komplikadong desisyon.

**Ang Estruktura ang Gumagabay sa Pag-uugali**

Napansin mo ba ang mga XML tag sa mga prompt? Hindi sila palamuti lang. Mas maaasahan ang mga modelo kapag sumusunod sa mga istrukturadong tagubilin kaysa sa malayang teksto. Kapag kailangan mo ng mga multi-step na proseso o kumplikadong lohika, nakakatulong ang estruktura upang matunton ng modelo kung nasaan na ito at ano ang susunod. Ang diagram sa ibaba ay nagpapakita ng isang mahusay na naka-istrukturang prompt, kung paano inaayos ng mga tag tulad ng `<system>`, `<instructions>`, `<context>`, `<user-input>`, at `<constraints>` ang iyong mga tagubilin sa mga malinaw na seksyon.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng isang mahusay na naka-istrukturang prompt na may malinaw na mga seksyon at organisasyon na estilo XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang mga self-reflecting pattern sa pagpapahayag ng mga pamantayan ng kalidad nang malinaw. Sa halip na umaasa na ang modelo "gagawin ito nang tama", sinasabi mo mismo kung ano ang ibig sabihin ng "tama": tamang lohika, paghawak ng error, performance, security. Pagkatapos, maaaring suriin at pagandahin ng modelo ang sarili nitong output. Ginagawa nitong proseso ang paggawa ng code, hindi suwerteng laro.

**Limitado ang Context**

Ang mga multi-turn conversation ay gumagana sa pamamagitan ng pagsama ng kasaysayan ng mensahe sa bawat request. Pero may limitasyon - bawat modelo ay may maximum na bilang ng token. Habang lumalaki ang pag-uusap, kakailanganin mo ng mga estratehiya upang mapanatili ang mahalagang konteksto nang hindi lumalampas sa limitasyon. Itinuturo sa module na ito kung paano gumagana ang memorya; sa susunod ay matututuhan mo kung kailan magbuod, kailan kalimutan, at kailan kunin muli ang impormasyon.

## Susunod na Mga Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Nakaraan: Module 01 - Introduksyon](../01-introduction/README.md) | [Bumalik sa Pangunahing](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat aming pinagsusumikapan ang pagiging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o kamalian. Ang orihinal na dokumento sa orihinal na wika nito ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->