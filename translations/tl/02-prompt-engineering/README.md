# Module 02: Prompt Engineering with GPT-5.2

## Table of Contents

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

## What You'll Learn

<img src="../../../translated_images/tl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Sa nakaraang module, nakita mo kung paano nagbibigay-daan ang memorya sa conversational AI at ginamit ang GitHub Models para sa mga pangunahing interaksyon. Ngayon, magfo-focus tayo sa kung paano ka magtatanong — ang mga prompts mismo — gamit ang Azure OpenAI's GPT-5.2. Ang paraan ng pag-structure ng iyong mga prompt ay malaking epekto sa kalidad ng mga sagot na matatanggap mo. Magsisimula tayo sa pag-review ng mga pangunahing teknik sa prompting, pagkatapos ay lilipat tayo sa walong advanced na pattern na buong pinapakinabangan ang kakayahan ng GPT-5.2.

Gagamit tayo ng GPT-5.2 dahil ito ay nagtatampok ng control sa reasoning - maaari mong sabihin sa modelo kung gaano karaming pag-iisip ang gagawin bago sumagot. Pinapalinaw nito ang iba't ibang istratehiya sa prompting at tinutulungan kang maunawaan kung kailan gagamitin ang bawat isa. Makikinabang din tayo mula sa mas kakaunting rate limits ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Prerequisites

- Natapos na ang Module 01 (na-deploy na ang Azure OpenAI resources)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga deployment instructions doon.

## Understanding Prompt Engineering

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdisenyo ng input text na palaging nagbibigay sa'yo ng resulta na kailangan mo. Hindi lang ito pagtatanong — ito ay pag-structure ng mga kahilingan para maintindihan ng modelo nang eksakto kung ano ang gusto mo at paano ito ihahatid.

Isipin mo itong tulad ng pagbibigay ng mga instruksyon sa isang katrabaho. "Ayusin ang bug" ay malabo. "Ayusin ang null pointer exception sa UserService.java sa linya 45 sa pamamagitan ng pagdagdag ng null check" ay specific. Ganon din ang trabaho ng mga language model — mahalaga ang specificity at structure.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Nagbibigay ang LangChain4j ng infrastructure — mga koneksyon sa modelo, memorya, at uri ng mga mensahe — habang ang mga prompt pattern ay mga maingat na inayos na text na ipinapadala mo sa infrastructure na iyon. Ang mga pangunahing bahagi ay ang `SystemMessage` (na nagse-set ng behavior at role ng AI) at `UserMessage` (na nagdadala ng iyong aktwal na kahilingan).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago tayo tumungo sa mga advanced na pattern sa module na ito, balikan muna natin ang limang pundamental na teknik sa prompting. Ito ang mga building blocks na dapat malaman ng bawat prompt engineer. Kung nakapagtrabaho ka na sa [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na ang mga ito — narito ang konseptwal na balangkas sa likod nila.

### Zero-Shot Prompting

Pinakasimpleng paraan: bigyan ng direktang instruksyon ang modelo nang walang mga halimbawa. Umaasa ang modelo nang buong-buo sa kanyang training para maintindihan at maisakatuparan ang gawain. Epektibo ito para sa mga tuwirang kahilingan kung saan halata ang inaasahang pag-uugali.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang instruksyon nang walang mga halimbawa — hinuhulaan ng modelo ang gawain mula sa instruksyon lamang*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Mga simpleng klasipikasyon, direktang tanong, pagsasalin, o anumang gawain na kaya ng modelo nang walang dagdag na gabay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na gusto mong sundan ng modelo. Natututo ang modelo ng inaasahang input-output na format mula sa mga halimbawa mo at inaaplay ito sa mga bagong input. Malaki ang naitutulong nito para sa consistency lalo na kung hindi halata ang nais na format o pag-uugali.

<img src="../../../translated_images/tl/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Pagkatuto mula sa mga halimbawa — kinikilala ng modelo ang pattern at inaaplay ito sa bagong inputs*

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

**Kailan gagamitin:** Pasadyang klasipikasyon, consistent na formatting, mga domain-specific na gawain, o kapag hindi consistent ang zero-shot results.

### Chain of Thought

Hilingin sa modelo na ipakita ang kanyang pag-iisip nang sunud-sunod na hakbang. Sa halip na diretso sa sagot, hinahati ng modelo ang problema at nilalakad ang bawat bahagi nang tahasan. Pinapahusay nito ang katumpakan sa math, logic, at multi-step reasoning tasks.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Sunod-sunod na pag-iisip — hinahati ang komplikadong problema sa explicit na mga lohikal na hakbang*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, pagkatapos ay 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Mga problemang math, logic puzzles, debugging, o anumang gawain kung saan nagpapabuti ang pagpapakita ng reasoning process ng katumpakan at tiwala.

### Role-Based Prompting

Mag-set ng persona o role para sa AI bago magtanong. Nagbibigay ito ng konteksto na nag-uukit sa tono, lalim, at pokus ng sagot. Ang "software architect" ay nagbibigay ng ibang payo kumpara sa "junior developer" o "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pag-set ng konteksto at persona — pareho ng tanong pero iba ang sagot depende sa itinakdang role*

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

**Kailan gagamitin:** Code reviews, pagtuturo, domain-specific na pagsusuri, o kapag kailangan mo ng sagot na naka-tailor sa partikular na antas ng ekspertis o pananaw.

### Prompt Templates

Gumawa ng reusable na mga prompt na may variable placeholders. Sa halip na magsulat ng bagong prompt sa bawat pagkakataon, tukuyin ang template isang beses at punan ang iba't ibang halaga. Pinapadali ito ng `PromptTemplate` class ng LangChain4j gamit ang syntax na `{{variable}}`.

<img src="../../../translated_images/tl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable na mga prompt na may variable placeholders — isang template, maraming gamit*

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

**Kailan gagamitin:** Paulit-ulit na mga query na may iba't ibang inputs, batch processing, paggawa ng reusable AI workflows, o anumang sitwasyon kung saan pare-pareho ang structure ng prompt pero nagbabago ang data.

---

Ang limang pundamental na ito ay nagbibigay sa'yo ng matibay na toolkit para sa karamihan ng mga prompting tasks. Ang natitirang bahagi ng module na ito ay nagpapalawig gamit ang **walong advanced na pattern** na gumagamit ng reasoning control, self-evaluation, at structured output capabilities ng GPT-5.2.

## Advanced Patterns

Kapag natutunan na ang pundamental, lumipat tayo sa walong advanced na pattern na nagbibigay-katangian sa module na ito. Hindi lahat ng problema ay kailangan ng parehong approach. Ang ilan ay nangangailangan ng mabilisang sagot, ang iba ay malalim na pag-iisip. Ang ilan ay kailangang ipakita ang reasoning, ang iba ay kailangan lang ng resulta. Ang bawat pattern sa ibaba ay optimized para sa ibang senaryo — at ang reasoning control ng GPT-5.2 ang lalo pang nagpapalinaw ng mga pagkakaiba.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang-ideya ng walong prompt engineering pattern at kanilang gamit*

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Pinahihintulutan ng reasoning control ng GPT-5.2 na tukuyin kung gaano karaming pag-iisip ang gagawin ng modelo — mula mabilis na direktang sagot hanggang malalim na pagsisiyasat*

<img src="../../../translated_images/tl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Mababang eagerness (mabilis, direkta) kumpara sa Mataas na eagerness (masinsin, malalim) na pamamaraan sa pag-iisip*

**Low Eagerness (Mabilis at Tinutukan)** - Para sa simpleng mga tanong kung saan gusto mo ng mabilis at direktang sagot. Minimal lang ang pag-iisip ng modelo - maximum ng 2 hakbang. Gamitin ito para sa mga kalkulasyon, pagkuha ng datos, o mga tuwirang tanong.

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

> 💡 **Tuklasin gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness prompting patterns?"
> - "Paano nakakatulong ang mga XML tag sa prompts para ma-structure ang sagot ng AI?"
> - "Kailan gagamitin ang self-reflection patterns kumpara sa direct instruction?"

**High Eagerness (Malalim at Masinsin)** - Para sa mga komplikadong problema kung saan kailangan ng masusing pagsusuri. Masinsinang sinusuri ng modelo at ipinapakita ang detalyadong pag-iisip. Gamitin ito para sa design ng system, mga desisyon sa arkitektura, o malalalim na pananaliksik.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - Para sa mga multi-step na workflow. Nagbibigay ang modelo ng paunang plano, kinukwento ang bawat hakbang habang ginagawa ito, tapos nagbibigay ng buod. Gamitin ito para sa mga migration, implementasyon, o anumang multi-step na proseso.

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

Ang Chain-of-Thought prompting ay tahasang hinihiling sa modelo na ipakita ang proseso ng pag-iisip, na nagpapataas ng katumpakan para sa mga komplikadong gawain. Ang sunod-sunod na paghahati ng hakbang ay nakakatulong sa parehong tao at AI na maintindihan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Itanong ang tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa mga long-running operation?"
> - "Ano ang mga best practice para sa pag-structure ng mga tool preambles sa production na aplikasyon?"
> - "Paano ko makukuhanan at maipapakita ang mga intermediate progress updates sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planuhin → Isagawa → Ibuod na workflow para sa multi-step na mga gawain*

**Self-Reflecting Code** - Para sa pagbuo ng production-quality na code. Gumagawa ang modelo ng code na sumusunod sa production standards na may wastong error handling. Gamitin ito kapag gumagawa ng bagong mga feature o serbisyo.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - bumuo, suriin, tukuyin ang mga isyu, pagbutihin, ulitin*

**Structured Analysis** - Para sa consistent na pagsusuri. Sinusuri ng modelo ang code gamit ang fixed na framework (katumpakan, mga praktikang ginagamit, performance, seguridad, maintainability). Gamitin ito para sa code reviews o pagtatasa ng kalidad.

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

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Itanong ang tungkol sa structured analysis:
> - "Paano ko iko-customize ang analysis framework para sa iba't ibang uri ng code review?"
> - "Ano ang pinakamahusay na paraan upang i-parse at gawan ng aksyon ang structured output programmatically?"
> - "Paano ko masisiguro ang consistent na severity levels sa iba't ibang review session?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework para sa consistent na code review na may severity levels*

**Multi-Turn Chat** - Para sa mga usapan na kailangan ng konteksto. Naaalala ng modelo ang mga naunang mensahe at pinapalawak ang mga ito. Gamitin ito para sa interactive help sessions o komplikadong Q&A.

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

*Paano nag-iipon ang konteksto ng pag-uusap sa maraming turn hanggang sa maabot ang token limit*

**Step-by-Step Reasoning** - Para sa mga problema na nangangailangan ng nakikitang lohika. Ipinapakita ng modelo ang tahasang reasoning sa bawat hakbang. Gamitin ito para sa mga problemang math, logic puzzles, o kapag nais mong maintindihan ang proseso ng pag-iisip.

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

*Paghahati ng mga problema sa explicit na mga lohikal na hakbang*

**Constrained Output** - Para sa mga sagot na may tiyak na format na kinakailangan. Mahigpit na sinusunod ng modelo ang mga patakaran sa format at haba. Gamitin ito para sa mga summary o kung kailangang eksakto ang istruktura ng output.

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

*Pagtitiyak ng partikular na format, haba, at mga kinakailangang istruktura*

## Using Existing Azure Resources

**I-verify ang deployment:**

Siguraduhin na ang `.env` file ay nasa root directory at may Azure credentials (nilikha sa panahon ng Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Note:** Kung nagsimula ka na ng lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, tumatakbo na ang module na ito sa port 8083. Pwede mong laktawan ang mga start commands sa ibaba at diretso na lang pumunta sa http://localhost:8083.

**Opsyon 1: Gamitin ang Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual interface para pamahalaan ang lahat ng Spring Boot application. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang Spring Boot icon).
Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan ang lahat ng magagamit na Spring Boot applications sa workspace
- Simulan/hinto ang mga aplikasyon gamit ang isang click lang
- Tingnan ang application logs nang real-time
- Subaybayan ang status ng aplikasyon

I-click lamang ang play button sa tabi ng "prompt-engineering" para simulan ang module na ito, o simulan ang lahat ng mga module nang sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Paggamit ng shell scripts**

Simulan ang lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa root directory
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

Awtomatikong niloload ng parehong scripts ang environment variables mula sa root `.env` file at bubuuin ang mga JAR kung wala pa ang mga ito.

> **Note:** Kung nais mong buuin lahat ng modules nang manu-mano bago magsimula:
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
./stop.sh  # Sa module na ito lang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Para sa module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Mga Screenshot ng Aplikasyon

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns na may kani-kanilang katangian at gamit*

## Pag-explore ng Mga Pattern

Pinapahintulutan ka ng web interface na mag-eksperimento sa iba't ibang prompting strategies. Bawat pattern ay nagsosolba ng iba't ibang problema - subukan ang mga ito upang makita kung kailan nangingibabaw ang bawat pamamaraan.

### Low vs High Eagerness

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Low Eagerness. Makakakuha ka ng agarang, direktang sagot. Ngayon naman magtanong ng kumplikado tulad ng "Disenyo ng caching strategy para sa high-traffic API" gamit ang High Eagerness. Panoorin kung paano bumagal ang modelo at nagbibigay ng detalyadong paliwanag. Parehong modelo, parehas na istruktura ng tanong - ngunit nagsasabi ang prompt kung gaano karaming pag-iisip ang gagawin.

<img src="../../../translated_images/tl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Mabilis na kalkulasyon na may kaunting pag-iisip*

<img src="../../../translated_images/tl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komprehensibong caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Nakikinabang ang multi-step workflows mula sa maagang pagpaplano at pagsasalaysay ng progreso. Ipinapaliwanag ng modelo kung ano ang gagawin, sinasabi bawat hakbang, at pagkatapos ay ibinubuod ang mga resulta.

<img src="../../../translated_images/tl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Gumagawa ng REST endpoint na may step-by-step narration (3.9MB)*

### Self-Reflecting Code

Subukan ang "Gumawa ng email validation service". Sa halip na basta gumawa lang ng code at tumigil, lumilikha ang modelo, sine-survey ayon sa quality criteria, tinutukoy ang mga kahinaan, at nagpapabuti. Makikita mo itong paulit-ulit na ginagawa hanggang sa maabot ng code ang mga pamantayan sa produksyon.

<img src="../../../translated_images/tl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletong email validation service (5.2MB)*

### Structured Analysis

Kailangan ang pagsusuri ng code ng mga pare-parehong evaluation frameworks. Sinusuri ng modelo ang code gamit ang mga nakatakdang kategorya (katumpakan, mga praktis, performance, seguridad) na may antas ng kalubhaan.

<img src="../../../translated_images/tl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based na pagsusuri ng code*

### Multi-Turn Chat

Magtanong ng "Ano ang Spring Boot?" pagkatapos ay agad sundan ng "Ipakita mo sa akin ang isang halimbawa". Naalala ng modelo ang unang tanong mo at nagbibigay sa iyo ng isang halimbawa ng Spring Boot na eksakto. Kung walang memorya, magiging masyadong malabo ang pangalawang tanong na iyon.

<img src="../../../translated_images/tl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Pagpapanatili ng konteksto sa mga tanong*

### Step-by-Step Reasoning

Pumili ng problemang pang-matematika at subukan ito gamit ang Step-by-Step Reasoning at Low Eagerness. Ang low eagerness ay nagbibigay lamang ng sagot - mabilis pero hindi malinaw. Ipinapakita ng step-by-step ang bawat kalkulasyon at desisyon.

<img src="../../../translated_images/tl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problemang pang-matematika na may malinaw na mga hakbang*

### Constrained Output

Kapag kailangan mo ng partikular na format o bilang ng salita, ipinatutupad ng pattern na ito ang mahigpit na pagsunod. Subukang gumawa ng buod na may eksaktong 100 salita sa format na bullet points.

<img src="../../../translated_images/tl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Buod ng machine learning na may kontrol sa format*

## Ang Tunay Mong Natututuhan

**Binabago ng Pag-iisip ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang computational effort sa pamamagitan ng iyong mga prompt. Ang mababang effort ay nangangahulugan ng mabilisang tugon na may kaunting pagsasaliksik. Ang mataas na effort ay nangangahulugan na maglalaan ng oras ang modelo upang mag-isip nang malalim. Natututuhan mong iayon ang effort sa pagiging kumplikado ng gawain - huwag magaksaya ng oras sa mga simpleng tanong, pero huwag rin magmadali sa mga kumplikadong desisyon.

**Ang Istruktura ang Gumagabay sa Pag-uugali**

Napansin mo ba ang mga XML tags sa mga prompt? Hindi ito palamuti. Mas maayos na sinusunod ng mga modelo ang mga structured instructions kaysa sa libreng teksto. Kapag kailangan mo ng multi-step na proseso o kumplikadong lohika, nakakatulong ang istruktura sa modelo na sundan kung nasaan ito at ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng isang mahusay na istrukturadong prompt na may malinaw na mga seksyon at organisasyon na parang XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang mga self-reflecting patterns sa pamamagitan ng pagpapalinaw ng quality criteria. Sa halip na umasa na "gagawin ng tama" ng modelo, sinasabi mo mismo kung ano ang ibig sabihin ng "tama": tamang lohika, error handling, performance, seguridad. Kaya maaaring suriin ng modelo ang sarili nitong output at magpabuti. Ginagawa nitong isang proseso ang pagbuo ng code sa halip na sugal.

**May Hangganan ang Konteksto**

Ang multi-turn conversations ay gumagana sa pamamagitan ng pagsama ng kasaysayan ng mga mensahe sa bawat request. Ngunit may limitasyon - bawat modelo ay may maximum token count. Habang lumalaki ang mga pag-uusap, kailangan mo ng mga estratehiya para panatilihin ang mahalagang konteksto nang hindi umaabot sa hangganan. Ipinapakita ng module na ito kung paano gumagana ang memorya; sa mga susunod na module malalaman mo kung kailan magbubuod, kailan kakalimutan, at kailan kukunin.

## Mga Susunod na Hakbang

**Sunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Nakaraan: Module 01 - Introduction](../01-introduction/README.md) | [Bumalik sa Main](../README.md) | [Sunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa kawastuhan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang orihinal na wika ang dapat ituring na pinakapinagkakatiwalaang sanggunian. Para sa mahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->