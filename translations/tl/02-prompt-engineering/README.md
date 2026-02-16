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

Sa nakaraang module, nakita mo kung paano nagbibigay ng kakayahan ang memorya sa conversational AI at ginamit ang GitHub Models para sa mga pangunahing interaksyon. Ngayon ay magpupokus tayo sa kung paano ka magtatanong — ang mga prompt mismo — gamit ang Azure OpenAI's GPT-5.2. Ang paraan ng pagstra-structura mo ng iyong mga prompt ay may malaking epekto sa kalidad ng mga sagot na makukuha mo. Magsisimula tayo sa isang pagrepaso ng mga pangunahing teknik ng prompting, pagkatapos ay lilipat tayo sa walong advanced na pattern na ganap na ginagamit ang kakayahan ng GPT-5.2.

Gagamitin natin ang GPT-5.2 dahil ipinakilala nito ang kontrol sa pagre-reason — maaari mong sabihin sa modelo kung gaano karaming pag-iisip ang gagawin bago sumagot. Ginagawa nitong mas maliwanag ang iba't ibang mga estratehiya sa prompting at tumutulong sa iyo na maunawaan kung kailan gagamitin ang bawat isa. Makikinabang din tayo sa mas kaunting mga limitasyon sa rate ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Prerequisites

- Natapos ang Module 01 (Mga Azure OpenAI resources na na-deploy)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundan muna ang mga tagubilin doon para sa deployment.

## Understanding Prompt Engineering

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdidisenyo ng input na teksto na palaging nagbibigay sa iyo ng mga resulta na kailangan mo. Hindi lang ito paghahanap ng mga tanong — ito ay tungkol sa pag-istraktura ng mga kahilingan upang maunawaan ng modelo nang eksakto kung ano ang gusto mo at paano ito ihahatid.

Isipin mo ito na parang pagbibigay ng mga tagubilin sa kasamahan. Ang "Ayusin ang bug" ay malabo. Ang "Ayusin ang null pointer exception sa UserService.java linya 45 sa pamamagitan ng pagdagdag ng null check" ay tiyak. Ganito rin ang pagtatrabaho ng mga language model — mahalaga ang pagiging tiyak at estruktura.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Ang LangChain4j ay nagbibigay ng imprastraktura — mga koneksyon sa modelo, memorya, at mga uri ng mensahe — habang ang mga prompt pattern ay mga maingat na naistrukturang teksto na ipinapadala mo sa imprastrakturang iyon. Ang mga pangunahing bahagi ay ang `SystemMessage` (na nagse-set ng pag-uugali at papel ng AI) at `UserMessage` (na nagdadala ng iyong aktwal na kahilingan).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago pumasok sa mga advanced na pattern sa module na ito, repasuhin muna natin ang limang pundamental na teknik sa prompting. Ito ang mga building block na dapat malaman ng bawat prompt engineer. Kung nagawa mo na ang [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na ang mga ito sa aksyon — narito ang konseptwal na balangkas sa likod ng mga ito.

### Zero-Shot Prompting

Ang pinakasimpleng paraan: bigyan ang modelo ng direktang utos na walang mga halimbawa. Ang modelo ay umaasa nang buo sa kanyang pagsasanay upang maunawaan at maisagawa ang gawain. Mabisa ito para sa mga diretso at simple na kahilingan kung saan obvious ang inaasahang pag-uugali.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang utos na walang mga halimbawa — hinuhinuha ng modelo ang gawain mula sa utos lamang*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Mga simpleng klasipikasyon, direktang mga tanong, pagsasalin, o anumang gawain na kaya ng modelo nang walang karagdagang gabay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na gusto mong sundan ng modelo. Natututuhan ng modelo ang inaasahang format ng input-output mula sa iyong mga halimbawa at inilalapat ito sa mga bagong input. Malaking tulong ito para sa pagiging consistent sa mga gawain kung saan hindi obvious ang format o pag-uugali na nais.

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

**Kailan gagamitin:** Mga custom na klasipikasyon, consistent na pag-format, mga domain-specific na gawain, o kapag hindi consistent ang zero-shot na resulta.

### Chain of Thought

Hilingin sa modelo na ipakita ang kanyang pagre-reason step-by-step. Sa halip na diretsong lumukso sa sagot, hinahati-hati ng modelo ang problema at pinoproseso ang bawat bahagi nang malinaw. Pinapahusay nito ang katumpakan sa math, logic, at mga multi-step na reasoning na gawain.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step na pagre-reason — paghahati ng kumplikadong mga problema sa mga malinaw na lohikal na hakbang*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, pagkatapos ay 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Mga problemang math, logic puzzles, debugging, o anumang gawain kung saan ang pagpapakita ng proseso ng pag-iisip ay nagpapabuti ng katumpakan at tiwala.

### Role-Based Prompting

Mag-set ng persona o papel para sa AI bago magtanong. Nagbibigay ito ng konteksto na nakakaapekto sa tono, lalim, at pokus ng sagot. Iba ang payo ng "software architect" kumpara sa "junior developer" o "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pag-set ng konteksto at persona — ang parehong tanong ay nagkakaroon ng ibang sagot depende sa itinakdang papel*

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

**Kailan gagamitin:** Code reviews, pagtuturo, domain-specific na pagsusuri, o kapag kailangan mo ng mga sagot na nakaangkop sa partikular na antas ng eksperto o pananaw.

### Prompt Templates

Gumawa ng reusable na mga prompt na may variable placeholders. Sa halip na sumulat ng bagong prompt sa bawat oras, magdefina ng template isang beses at punan ito ng iba't ibang mga halaga. Pinapadali ito ng `PromptTemplate` class sa LangChain4j gamit ang `{{variable}}` syntax.

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

**Kailan gagamitin:** Paulit-ulit na mga query na may iba't ibang input, batch processing, paggawa ng mga reusable AI workflows, o anumang senaryo kung saan ang istraktura ng prompt ay pareho ngunit nagbabago ang data.

---

Ang limang pundamental na ito ay nagbibigay sa iyo ng matibay na toolkit para sa karamihan ng mga gawain sa prompting. Ang natitirang bahagi ng module na ito ay nagtatayo sa mga ito gamit ang **walong advanced na pattern** na ginagamit ang reasoning control, self-evaluation, at structured output na kakayahan ng GPT-5.2.

## Advanced Patterns

Nakapagtakda na ng pundasyon, lumipat tayo sa walong advanced na pattern na ginagawang natatangi ang module na ito. Hindi lahat ng problema ay nangangailangan ng parehong diskarte. May mga tanong na kailangan ng mabilis na sagot, ang iba ay kailangan ng malalim na pag-iisip. May ilan na kailangan ng nakikitang pagre-reason, ang iba naman ay kailangan lang ng resulta. Ang bawat pattern sa ibaba ay naka-optimize para sa iba't ibang sitwasyon — at lalo pang pinapatingkad ng reasoning control ng GPT-5.2 ang mga pagkakaiba.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang-ideya ng walong prompt engineering na pattern at ang kanilang mga gamit*

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Pinapahintulutan ka ng reasoning control ng GPT-5.2 na tukuyin kung gaano karaming pag-iisip ang gagawin ng modelo — mula sa mabilis na diretso na sagot hanggang sa malalim na pagsisiyasat*

<img src="../../../translated_images/tl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Mababang kasigasigan (mabilis, diretso) vs Mataas na kasigasigan (masinsin, eksploratoryo) na mga paraan ng pagre-reason*

**Low Eagerness (Quick & Focused)** - Para sa simpleng mga tanong kung saan gusto mo ng mabilis at diretsong sagot. Gumagawa ang modelo ng minimal na pagre-reason - maximum na 2 hakbang. Gamitin ito para sa mga kalkulasyon, pagtingin, o mga diretso na tanong.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **I-explore gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness na mga pattern sa prompting?"
> - "Paano nakakatulong ang mga XML tag sa prompts sa pag-istruktura ng sagot ng AI?"
> - "Kailan ako gagamit ng self-reflection na mga pattern kumpara sa direct instruction?"

**High Eagerness (Deep & Thorough)** - Para sa mga komplikadong problema kung saan gusto mo ng komprehensibong pagsusuri. Masusing sinusuri ng modelo at ipinapakita ang detalyadong pagre-reason. Gamitin ito para sa system design, mga desisyon sa arkitektura, o malalim na pananaliksik.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - Para sa mga multi-step na workflow. Nagbibigay ang modelo ng upfront na plano, isinasalaysay ang bawat hakbang habang ginagawa, pagkatapos ay nagbibigay ng buod. Gamitin ito para sa migrations, implementasyon, o anumang multi-step na proseso.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Ang Chain-of-Thought prompting ay tahasang humihiling sa modelo na ipakita ang proseso ng pagre-reason nito, na nagpapahusay ng katumpakan para sa mga kumplikadong gawain. Ang step-by-step breakdown ay nakakatulong sa parehong tao at AI na maunawaan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa pattern na ito:
> - "Paano ko ia-adapt ang task execution pattern para sa mga long-running operations?"
> - "Ano ang mga best practice sa pag-istruktura ng tool preambles sa production applications?"
> - "Paano ko mahuhuli at maipapakita ang mga intermediate progress update sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planuhin → Isagawa → Buodin na workflow para sa mga multi-step na gawain*

**Self-Reflecting Code** - Para sa pag-generate ng production-quality na code. Nag-ge-generate ang modelo ng code, chine-check ito laban sa mga kriteriya ng kalidad, at pinapabuti ito nang paulit-ulit. Gamitin ito kapag bumubuo ng mga bagong feature o serbisyo.

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

*Iterative improvement loop - gumawa, suriin, tukuyin ang mga isyu, pagbutihin, ulitin*

**Structured Analysis** - Para sa konsistent na pagsusuri. Nirereview ng modelo ang code gamit ang isang fixed na framework (correctness, practices, performance, security). Gamitin ito para sa code reviews o pagsusuri ng kalidad.

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

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa structured analysis:
> - "Paano ko maiaangkop ang analysis framework para sa iba't ibang uri ng code review?"
> - "Ano ang pinakamabisang paraan para i-parse at i-proseso ang structured output programmatically?"
> - "Paano ko mapapanatili ang konsistent na severity levels sa iba't ibang review sessions?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Apat na kategorya ng framework para sa konsistenteng code review na may severity levels*

**Multi-Turn Chat** - Para sa mga usapan na nangangailangan ng konteksto. Naaalala ng modelo ang mga naunang mensahe at pinagbubuo ito. Gamitin ito para sa interactive help sessions o kumplikadong Q&A.

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

*Paano naiipon ang konteksto ng usapan sa maraming tugon hanggang sa maabot ang token limit*

**Step-by-Step Reasoning** - Para sa mga problema na nangangailangan ng nakikitang lohika. Ipinapakita ng modelo ang malinaw na pagre-reason sa bawat hakbang. Gamitin ito para sa mga problemang math, logic puzzles, o kapag kailangan mong maunawaan ang proseso ng pag-iisip.

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

*Paghahati ng mga problema sa malinaw na mga lohikal na hakbang*

**Constrained Output** - Para sa mga sagot na may specific na mga format na kinakailangan. Mahigpit na sinusunod ng modelo ang mga panuntunan sa format at haba. Gamitin ito para sa mga buod o kapag kailangan mo ng eksaktong estruktura ng output.

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

*Pagsunod sa mga specific na requirements sa format, haba, at estruktura*

## Using Existing Azure Resources

**I-verify ang deployment:**

Siguraduhin na ang `.env` file ay nandiyan sa root directory na may Azure credentials (nilikha noong Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Note:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, ang module na ito ay tumatakbo na sa port 8083. Maaari mong laktawan ang mga utos sa pagsisimula sa ibaba at pumunta na diretso sa http://localhost:8083.

**Option 1: Gamitin ang Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot na aplikasyon. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang Spring Boot icon).
Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan lahat ng available na Spring Boot applications sa workspace
- Simulan/hinto ang mga aplikasyon sa isang click lang
- Tingnan ang mga application logs nang real-time
- I-monitor ang status ng aplikasyon

I-click lang ang play button sa tabi ng "prompt-engineering" para simulan ang module na ito, o simulan lahat ng modules nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Paggamit ng shell scripts**

Simulan lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa direktoryo ng root
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

Parehong awtomatikong naglo-load ng environment variables mula sa root `.env` file ang mga script at magbubuo ng JARs kung wala pa ang mga ito.

> **Tandaan:** Kung mas gusto mong manu-manong buuin ang lahat ng modules bago magsimula:
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
./stop.sh  # Para lang sa module na ito
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Para lamang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Mga Screenshot ng Aplikasyon

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ang pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns kasama ang kanilang mga katangian at mga gamit*

## Pag-explore ng mga Pattern

Pinapayagan ka ng web interface na subukan ang iba't ibang prompting strategies. Bawat pattern ay nagsosolusyon ng iba't ibang problema - subukan ang mga ito para makita kung kailan pinakamainam ang bawat paraan.

### Mababa vs Mataas na Eagerness

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Mababang Eagerness. Makakakuha ka ng agarang, direktang sagot. Ngayon magtanong ng mas kumplikado tulad ng "Disenyo ng caching strategy para sa high-traffic API" gamit ang Mataas na Eagerness. Panoorin kung paano bumagal ang modelo at nagbibigay ng detalyadong paliwanag. Parehong modelo, parehas na estruktura ng tanong - pero sinasabi ng prompt kung gaano karaming pag-iisip ang gagawin.

<img src="../../../translated_images/tl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Mabilis na kalkulasyon na may minimal na pag-iisip*

<img src="../../../translated_images/tl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komprehensibong caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Nakikinabang ang multi-step workflows mula sa maagang pagpaplano at pagsasalaysay ng progreso. Ipinapakita ng modelo kung ano ang gagawin nito, sinasalaysay ang bawat hakbang, tapos pinagsasama-sama ang mga resulta.

<img src="../../../translated_images/tl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Paglikha ng REST endpoint na may hakbang-hakbang na pagsasalaysay (3.9MB)*

### Self-Reflecting Code

Subukan ang "Gumawa ng email validation service". Sa halip na basta gumawa ng code at tumigil, ang modelo ay gumagawa, nagsusuri laban sa mga kalidad na pamantayan, tinutukoy ang kahinaan, at pinapabuti. Makikita mo itong ulitin hanggang maabot ng code ang pamantayang pang-produksyon.

<img src="../../../translated_images/tl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletong email validation service (5.2MB)*

### Structured Analysis

Kailangan ng code reviews ng consistent na evaluation frameworks. Sinusuri ng modelo ang code gamit ang fixed categories (katumpakan, mga kasanayan, performance, seguridad) na may severity levels.

<img src="../../../translated_images/tl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based code review*

### Multi-Turn Chat

Magtanong ng "Ano ang Spring Boot?" tapos sundan kaagad ng "Ipakita mo sa akin ang isang halimbawa". Tinatandaan ng modelo ang iyong unang tanong at bibigyan ka ng isang halimbawa ng Spring Boot nang eksakto. Kung walang memorya, magiging masyadong malabo ang pangalawang tanong.

<img src="../../../translated_images/tl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Pagpapanatili ng konteksto sa mga tanong*

### Step-by-Step Reasoning

Pumili ng math problem at subukan ito gamit ang parehong Step-by-Step Reasoning at Low Eagerness. Ang mababang eagerness ay binibigay lang ang sagot - mabilis pero hindi detalyado. Ipinapakita ng step-by-step ang bawat kalkulasyon at desisyon.

<img src="../../../translated_images/tl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problemang matematikal na may malinaw na mga hakbang*

### Constrained Output

Kapag kailangan mo ng specific na format o bilang ng salita, pinipilit ng pattern na ito ang mahigpit na pagsunod. Subukan gumawa ng summary na may eksaktong 100 salita sa bullet point format.

<img src="../../../translated_images/tl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Buod ng machine learning na may kontrol sa format*

## Ang Talagang Iyong Natutunan

**Nagbabago ang Lahat ng Pagsisikap sa Pag-iisip**

Pinapayagan ka ng GPT-5.2 na kontrolin ang pagsisikap sa computation gamit ang iyong mga prompt. Ang mababang pagsisikap ay nangangahulugang mabilis na mga sagot na may minimal na pagsasaliksik. Ang mataas na pagsisikap ay nangangahulugang maglalaan ang modelo ng oras upang mag-isip nang malalim. Natututo kang ipantay ang pagsisikap sa kumplikado ng gawain - huwag sayangin ang oras sa simpleng mga tanong, pero huwag din magmadali sa komplikadong mga desisyon.

**Gumagabay ang Estruktura ng Pag-uugali**

Napansin mo ba ang XML tags sa mga prompt? Hindi lang ito mga dekorasyon. Mas mapagkakatiwalaan ang mga modelo kapag sinusunod ang mga estrukturadong tagubilin kaysa sa malayang teksto. Kapag kailangan mo ng multi-step na proseso o kumplikadong lohika, tinutulungan ng estruktura ang modelo na subaybayan kung nasaan ito at kung ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng maayos na estrukturadong prompt na may malinaw na mga seksyon at organisasyong estilo XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang self-reflecting patterns sa pamamagitan ng pagpapaliwanag ng mga pamantayan ng kalidad. Sa halip na umasa na "tama ang gagawin" ng modelo, sinasabi mo dito kung ano talaga ang ibig sabihin ng "tama": tamang lohika, paghawak ng error, performance, seguridad. Kaya nitong suriin ang sariling output at magpabuti. Ginagawa nitong proseso ng paggawa ng code ang dating parang suwerte lang.

**May Hangganan ang Konteksto**

Gumagana ang multi-turn conversations sa pamamagitan ng pagsama ng kasaysayan ng mensahe sa bawat request. Pero may hangganan - bawat modelo ay may maximum na bilang ng token. Habang lumalaki ang pag-uusap, kakailanganin mo ng mga estratehiya para mapanatili ang relevant na konteksto nang hindi naabot ang limitasyon. Ipinapakita ng module na ito kung paano gumagana ang memorya; mamaya, matututuhan mo kung kailan magbubuod, kailan kakalimutan, at kailan kukuha muli.

## Mga Susunod na Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Pag-navigate:** [← Nakaraan: Module 01 - Panimula](../01-introduction/README.md) | [Bumalik sa Pangunahing pahina](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:  
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI na pagsasalin [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, mangyaring tandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatumpak. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->