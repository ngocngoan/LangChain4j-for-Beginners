# Module 02: Prompt Engineering gamit ang GPT-5.2

## Table of Contents

- [Ano ang Matututuhan Mo](../../../02-prompt-engineering)
- [Mga Kinakailangan](../../../02-prompt-engineering)
- [Pag-unawa sa Prompt Engineering](../../../02-prompt-engineering)
- [Mga Pangunahing Kaalaman sa Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Mga Advanced na Pattern](../../../02-prompt-engineering)
- [Paggamit ng Umiiral na Azure Resources](../../../02-prompt-engineering)
- [Mga Screenshot ng Aplikasyon](../../../02-prompt-engineering)
- [Paggalugad sa mga Pattern](../../../02-prompt-engineering)
  - [Mababang vs Mataas na Kasigasigan](../../../02-prompt-engineering)
  - [Pagpapatupad ng Gawain (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [Ano Talaga ang Iyong Natututuhan](../../../02-prompt-engineering)
- [Mga Susunod na Hakbang](../../../02-prompt-engineering)

## Ano ang Matututuhan Mo

<img src="../../../translated_images/tl/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Sa nakaraang module, nakita mo kung paano nagbibigay-daan ang memorya sa conversational AI at ginamit ang GitHub Models para sa mga pangunahing interaksyon. Ngayon ay tututok tayo kung paano ka magtatanong — ang mga prompt mismo — gamit ang Azure OpenAI's GPT-5.2. Ang paraan ng pagbuo ng iyong mga prompt ay malaki ang epekto sa kalidad ng mga sagot na matatanggap mo. Nagsisimula tayo sa pagsusuri ng mga pangunahing teknik sa prompting, pagkatapos ay lilipat sa walong advanced patterns na ganap na ginagamit ang kakayahan ng GPT-5.2.

Gagamitin natin ang GPT-5.2 dahil ipinakikilala nito ang reasoning control - maaari mong sabihin sa modelo kung gaano karaming pag-iisip ang gagawin bago sumagot. Ginagawa nitong mas malinaw ang iba't ibang estratehiya sa prompting at tinutulungan kang maintindihan kung kailan gagamitin ang bawat isa. Aabot din tayo sa benepisyo ng mas kaunting mga limitasyon sa rate ng Azure para sa GPT-5.2 kumpara sa GitHub Models.

## Mga Kinakailangan

- Natapos ang Module 01 (Naideploy na ang Azure OpenAI resources)
- `.env` na file sa root directory na may Azure credentials (nalikha ng `azd up` sa Module 01)

> **Tandaan:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubilin sa deployment doon.

## Pag-unawa sa Prompt Engineering

<img src="../../../translated_images/tl/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ang prompt engineering ay tungkol sa pagdidisenyo ng input na teksto na palaging nakukuha ang resulta na kailangan mo. Hindi lang ito basta pagtatanong - ito ay tungkol sa pag-istraktura ng mga kahilingan upang maunawaan ng modelo nang eksakto kung ano ang gusto mo at paano ito ihahatid.

Isipin mo ito na parang pagbibigay ng mga tagubilin sa isang kasamahan. Ang "Ayusin ang bug" ay malabo. Ang "Ayusin ang null pointer exception sa UserService.java line 45 sa pamamagitan ng pagdagdag ng null check" ay tiyak. Ganun din ang mga language model - mahalaga ang pagiging tiyak at istraktura.

<img src="../../../translated_images/tl/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

Nagbibigay ang LangChain4j ng imprastruktura — mga koneksyon sa modelo, memorya, at uri ng mga mensahe — habang ang mga prompt pattern ay mga maingat na nakaayos na teksto na ipinapadala mo sa imprastrukturang iyon. Ang mga pangunahing bahagi ay ang `SystemMessage` (na nagse-set ng asal at papel ng AI) at `UserMessage` (na nagdadala ng iyong aktwal na kahilingan).

## Mga Pangunahing Kaalaman sa Prompt Engineering

<img src="../../../translated_images/tl/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Bago sumabak sa mga advanced na pattern sa modulong ito, balikan muna natin ang limang pangunahing teknik sa prompting. Ito ang mga basehan na dapat alam ng bawat prompt engineer. Kung nakapagsimula ka na sa [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), nakita mo na ang mga ito sa aksyon — narito ang konseptwal na balangkas sa likod nila.

### Zero-Shot Prompting

Pinakasimpleng pamamaraan: bigyan ang modelo ng direktang utos nang walang mga halimbawa. Umaasa ang modelo sa kanyang training upang maintindihan at isagawa ang gawain. Maganda ito para sa mga madaling kahilingan kung saan halatang-halata ang inaasahang kilos.

<img src="../../../translated_images/tl/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direktang utos na walang mga halimbawa — hinuhinuha ng modelo ang gawain mula sa utos lamang*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Tugon: "Positibo"
```

**Kailan gagamitin:** Mga simpleng klasipikasyon, direktang tanong, pagsasalin, o anomang gawain na kaya ng modelo nang walang dagdag na gabay.

### Few-Shot Prompting

Magbigay ng mga halimbawa na nagpapakita ng pattern na gusto mong sundin ng modelo. Natututuhan ng modelo ang inaasahang format ng input-output mula sa iyong mga halimbawa at inaaplay ito sa mga bagong input. Malaking tulong ito sa consistency para sa mga gawain na hindi halata ang tamang format o kilos.

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

**Kailan gagamitin:** Custom classifications, consistent formatting, domain-specific tasks, o kapag hindi konsistent ang resulta sa zero-shot.

### Chain of Thought

Sabihin sa modelo na ipakita ang kanyang pag-iisip ng hakbang-hakbang. Sa halip na diretsong sumagot, hinahati-hati ng modelo ang problema at ipinapaliwanag bawat bahagi. Mas bumubuti ang katumpakan sa math, logic, at multi-step reasoning tasks.

<img src="../../../translated_images/tl/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Hakbang-hakbang na pag-iisip — paghahati ng komplikadong problema sa mga tiyak na lohikal na hakbang*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Ipinapakita ng modelo: 15 - 8 = 7, pagkatapos 7 + 12 = 19 na mansanas
```

**Kailan gagamitin:** Mga problema sa matematika, logic puzzles, debugging, o anumang gawain kung saan ang pagpapakita ng proseso ng pag-iisip ay nagpapabuti ng katumpakan at tiwala.

### Role-Based Prompting

Mag-set ng persona o papel para sa AI bago magtanong. Nagbibigay ito ng konteksto na humuhubog sa tono, lalim, at pokus ng sagot. Iba ang maibibigay na payo ng "software architect" kaysa "junior developer" o "security auditor".

<img src="../../../translated_images/tl/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Pag-set ng konteksto at persona — ibang sagot ang parehong tanong depende sa naitalagang papel*

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

**Kailan gagamitin:** Code reviews, pagtuturo, domain-specific analysis, o kapag kailangan mo ng sagot na nakaayon sa partikular na level ng expertise o perspektibo.

### Prompt Templates

Lumikha ng mga reusable prompt na may mga variable na placeholder. Sa halip na magsulat ng bagong prompt sa bawat pagkakataon, gumawa ng template isang beses at punan ng iba't ibang halaga. Ginagawa itong madali ng langChain4j na `PromptTemplate` na may syntax na `{{variable}}`.

<img src="../../../translated_images/tl/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable na mga prompt na may mga variable placeholder — isang template, maraming gamit*

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

**Kailan gagamitin:** Paulit-ulit na mga query na may iba't ibang input, batch processing, paggawa ng reusable na AI workflows, o anumang sitwasyon kung saan ang istraktura ng prompt ay pareho lang ngunit nagbabago ang data.

---

Binibigyan ka ng limang pundasyong ito ng solidong toolkit para sa karamihan ng mga prompting task. Ang natitira sa modulong ito ay nagtatayo sa mga ito gamit ang **walong advanced na pattern** na ginagamit ang reasoning control, self-evaluation, at structured output capabilities ng GPT-5.2.

## Mga Advanced na Pattern

Pagkatapos ng mga pangunahing kaalaman, lumipat tayo sa walong advanced na pattern na nagpapalutang sa module na ito. Hindi lahat ng problema ay nangangailangan ng parehong paraan. Ang ilan ay nangangailangan ng mabilisang sagot, ang iba ay malalim na pag-iisip. Ang iba ay kailangan ng nakikitang paliwanag, ang iba ay kailangan lang ng resulta. Ang bawat pattern sa ibaba ay iniaayos para sa iba't ibang senaryo — at pinapatingkad ng reasoning control ng GPT-5.2 ang pagkakaiba.

<img src="../../../translated_images/tl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pangkalahatang-ideya ng walong prompt engineering pattern at kanilang mga gamit*

<img src="../../../translated_images/tl/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Pinapayagan ka ng reasoning control ng GPT-5.2 na tukuyin kung gaano karaming pag-iisip ang gagawin ng modelo — mula sa mabilis na direktang sagot hanggang sa malalim na eksplorasyon*

**Mababang Kasigasigan (Mabilis at Tiyak)** - Para sa simpleng mga tanong kung saan gusto mo ng mabilis, direktang sagot. Minimal ang pag-iisip ng modelo - maximum na 2 hakbang. Gamitin ito para sa kalkulasyon, paghahanap, o madaling tanong.

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

> 💡 **Suriin gamit ang GitHub Copilot:** Buksan ang [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) at itanong:
> - "Ano ang pagkakaiba ng low eagerness at high eagerness prompting patterns?"
> - "Paano nakatutulong ang mga XML tags sa prompts sa pag-istraktura ng sagot ng AI?"
> - "Kailan ko gagamitin ang self-reflection patterns kumpara sa direct instruction?"

**Mataas na Kasigasigan (Malalim at Masusi)** - Para sa masalimuot na mga problema kung saan gusto mong magkaroon ng komprehensibong pagsusuri. Malalim ang eksplorasyon ng modelo at nagpapakita ng detalyadong pag-iisip. Gamitin ito para sa disenyo ng sistema, desisyon sa arkitektura, o komplikadong pananaliksik.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Pagpapatupad ng Gawain (Hakbang-hakbang na Progreso)** - Para sa mga multi-step na workflow. Nagbibigay ang modelo ng plano sa simula, ikinukuwento ang bawat hakbang habang ginagawa ito, at nagbibigay ng buod pagkatapos. Gamitin ito para sa migrations, implementations, o anumang multi-step na proseso.

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

Ang Chain-of-Thought prompting ay hayagang hinihiling sa modelo na ipakita ang proseso ng pag-iisip, nagpapabuti ng katumpakan para sa mga komplikadong gawain. Ang hakbang-hakbang na pagkakapira-piraso ay tumutulong sa parehong tao at AI na maunawaan ang lohika.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Magtanong tungkol sa pattern na ito:
> - "Paano ko iaangkop ang task execution pattern para sa mga long-running na operasyon?"
> - "Ano ang mga pinakamainam na praktis sa pag-istruktura ng tool preambles sa mga production na aplikasyon?"
> - "Paano ko makukuha at maipapakita ang mga intermediate progress update sa UI?"

<img src="../../../translated_images/tl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planuhin → Isagawa → Ibuod na workflow para sa multi-step na gawain*

**Self-Reflecting Code** - Para sa pagbuo ng production-quality na code. Naggegenerate ang modelo ng code na sumusunod sa production standards na may tamang error handling. Gamitin ito kapag gumagawa ng bagong feature o serbisyo.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ulit-ulit na proseso ng pagpapabuti - generate, evaluate, hanapin ang problema, pagbutihin, ulitin*

**Structured Analysis** - Para sa consistent na pagsusuri. Nirereview ng modelo ang code gamit ang nakatakdang framework (katumpakan, praktis, performance, seguridad, maintainability). Gamitin ito sa code review o quality assessments.

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
> - "Paano ko mapapalit ang analysis framework para sa iba't ibang klase ng code review?"
> - "Ano ang pinakamahusay na paraan para i-parse at i-apply ang structured output programmatically?"
> - "Paano ko masisiguro ang consistent na severity levels sa iba't ibang sesyon ng review?"

<img src="../../../translated_images/tl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework para sa consistent na code review na may severity levels*

**Multi-Turn Chat** - Para sa mga pag-uusap na nangangailangan ng konteksto. Naalala ng modelo ang mga naunang mensahe at napapatuloy ang usapan. Gamitin ito para sa interactive help sessions o komplikadong Q&A.

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

*Paano naipon ang konteksto ng pag-uusap sa maraming turn hanggang umabot sa token limit*

**Step-by-Step Reasoning** - Para sa mga problema na nangangailangan ng nakikitang lohika. Ipinapakita ng modelo ang tiyak na pag-iisip bawat hakbang. Gamitin ito sa mga problema sa matematika, logic puzzles, o kapag gusto mong maintindihan ang proseso ng pag-iisip.

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

*Paghahati ng problema sa mga tiyak na lohikal na hakbang*

**Constrained Output** - Para sa mga sagot na may partikular na format na kailangan sundin. Mahigpit na sinusunod ng modelo ang mga patakaran sa format at haba. Gamitin ito para sa mga buod o kapag kailangan ng eksaktong output structure.

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

*Pagsunod sa partikular na format, haba, at mga kinakailangang istraktura*

## Paggamit ng Umiiral na Azure Resources

**I-verify ang deployment:**

Siguraduhing may `.env` file sa root directory na may Azure credentials (nalikha noong Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, ang module na ito ay tumatakbo na sa port 8083. Maaari mo nang laktawan ang mga start commands sa ibaba at diretso na sa http://localhost:8083.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para i-manage ang lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan ang lahat ng available na Spring Boot applications sa workspace
- Simulan/hintuin ang mga aplikasyon sa isang click lang
- Tingnan ang mga log ng aplikasyon nang real-time
- I-monitor ang status ng aplikasyon
Pindutin lamang ang play button sa tabi ng "prompt-engineering" upang simulan ang module na ito, o simulan lahat ng mga module nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Paggamit ng shell scripts**

Simulan lahat ng web applications (mga module 01-04):

**Bash:**
```bash
cd ..  # Mula sa root na direktoryo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa direktoryo ng ugat
.\start-all.ps1
```

O simulan lamang ang module na ito:

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

Awtomatikong iniloload ng parehong scripts ang mga environment variable mula sa root `.env` file at bubuuin ang mga JAR kung hindi pa ito umiiral.

> **Note:** Kung nais mong manu-manong buuin lahat ng mga module bago magsimula:
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
./stop.sh  # Ang module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng module
```

**PowerShell:**
```powershell
.\stop.ps1  # Module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Mga Screenshot ng Aplikasyon

<img src="../../../translated_images/tl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pangunahing dashboard na nagpapakita ng lahat ng 8 prompt engineering patterns kasama ang kanilang mga katangian at gamit*

## Pagsusuri sa mga Pattern

Pinapayagan ka ng web interface na subukan ang iba't ibang estratehiya sa prompting. Bawat pattern ay nagsosolusyon ng mga ibang problema - subukan ang mga ito upang makita kung kailan pinakamahusay gamitin ang bawat isa.

### Mababa vs Mataas na Kagustuhan (Eagerness)

Magtanong ng simpleng tanong tulad ng "Ano ang 15% ng 200?" gamit ang Mababang Kagustuhan. Makakakuha ka ng agarang, direktang sagot. Ngayon, magtanong ng komplikadong bagay tulad ng "Disenyo ng caching strategy para sa high-traffic API" gamit ang Mataas na Kagustuhan. Mapapansin mong bumagal ang modelo at nagbibigay ng detalyadong paliwanag. Parehong modelo, parehong estruktura ng tanong - ngunit ang prompt ang nagsasabi kung gaano karaming pag-iisip ang gagawin.

<img src="../../../translated_images/tl/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Mabilisang kalkulasyon na may kaunting paliwanag*

<img src="../../../translated_images/tl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Komprehensibong caching strategy (2.8MB)*

### Pagsasagawa ng Task (Tool Preambles)

Nakikinabang ang mga multi-step na workflow mula sa maagang pagpaplano at pagsasalaysay ng progreso. Inilalahad ng modelo kung ano ang gagawin, isinasalaysay ang bawat hakbang, at pagkatapos ay inilalahad ang mga resulta.

<img src="../../../translated_images/tl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Paglikha ng REST endpoint na may hakbang-hakbang na pagsasalaysay (3.9MB)*

### Self-Reflecting Code

Subukang "Gumawa ng email validation service". Sa halip na basta bumuo ng code at tumigil, ang modelo ay bumubuo, sinusuri laban sa mga pamantayan ng kalidad, tinutukoy ang mga kahinaan, at pinapabuti. Makikita mong inuulit ito hanggang maabot ng code ang pamantayan para sa produksyon.

<img src="../../../translated_images/tl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Kompletong email validation service (5.2MB)*

### Structured Analysis

Kailangan ng mga pagsusuri ng code ng mga pare-parehong framework sa pagsusuri. Sinusuri ng modelo ang code gamit ang mga nakapirming kategorya (katumpakan, kasanayan, pagganap, seguridad) na may antas ng tindi.

<img src="../../../translated_images/tl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Pagsusuri ng code batay sa framework*

### Multi-Turn Chat

Magtanong ng "Ano ang Spring Boot?" tapos agad sumunod sa "Ipakita mo ako ng isang halimbawa". Naalala ng modelo ang iyong unang tanong at nagbibigay sa iyo ng isang halimbawa ng Spring Boot nang partikular. Kung walang memorya, sobrang malabo ng pangalawang tanong.

<img src="../../../translated_images/tl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Pagpapanatili ng konteksto sa buong mga tanong*

### Hakbang-hakbang na Paliwanag

Pumili ng problema sa matematika at subukan ito gamit ang parehong Hakbang-hakbang na Paliwanag at Mababang Kagustuhan. Minsang ibinibigay lang ng mababang kagustuhan ang sagot - mabilis pero hindi malinaw. Ipinapakita ng hakbang-hakbang ang bawat pagkalkula at desisyon.

<img src="../../../translated_images/tl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema sa matematika na may malinaw na mga hakbang*

### Limitadong Output

Kapag kailangan mo ng mga partikular na format o bilang ng salita, pinipilit ng pattern na ito ang mahigpit na pagsunod. Subukang gumawa ng buod na may eksaktong 100 salita sa anyong bullet points.

<img src="../../../translated_images/tl/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Buod ng machine learning na may kontrol sa format*

## Ang Tunay Mong Natututuhan

**Binabago ng Pagsisikap sa Papiisipan ang Lahat**

Pinapayagan ka ng GPT-5.2 na kontrolin ang computational effort sa pamamagitan ng iyong mga prompt. Ang mababang pagsisikap ay nangangahulugan ng mabilis na tugon na may kaunting paggalugad. Ang mataas na pagsisikap ay nangangahulugan na maglalaan ng oras ang modelo upang mag-isip nang malalim. Natututuhan mong iayon ang pagsisikap sa kumplikasyon ng gawain - huwag sayangin ang oras sa simpleng mga tanong, pero huwag din magmadali sa mga komplikadong desisyon.

**Gumagabay ang Estruktura sa Pag-uugali**

Napansin mo ba ang mga tag na XML sa mga prompt? Hindi ito palamuti lang. Mas sumusunod ang mga modelo sa mga istrukturadong utos kaysa sa malayang teksto. Kapag kailangan mo ng multi-step na proseso o komplikadong lohika, tinutulungan ng estruktura ang modelo na masubaybayan kung nasaan na ito at ano ang susunod.

<img src="../../../translated_images/tl/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomiya ng mahusay na istrukturadong prompt na may malinaw na mga seksyon at organisasyong estilo XML*

**Kalidad sa Pamamagitan ng Sariling Pagsusuri**

Gumagana ang mga self-reflecting pattern sa pamamagitan ng pagpapaliwanag nang malinaw ng mga pamantayan ng kalidad. Sa halip na maghintay na "tamang gawin" ng modelo, sinasabi mo dito nang eksakto kung ano ang ibig sabihin ng "tama": tamang lohika, paghawak ng mga error, pagganap, seguridad. Kaya nitong tasahin ang sariling output at pagbutihin ito. Ginagawa nitong proseso ang paglikha ng code mula sa isang suwerte.

**May Hangganan ang Konteksto**

Gumagana ang multi-turn na usapan sa pamamagitan ng pagsama ng kasaysayan ng mensahe sa bawat hiling. Pero may limitasyon - bawat modelo ay may maximum na bilang ng token. Habang lumalaki ang mga pag-uusap, kakailanganin mo ng mga estratehiya para mapanatili ang kaugnay na konteksto nang hindi nalalampasan ang limitasyon. Ipinapakita ng module na ito kung paano gumagana ang memorya; mamaya, matututunan mo kung kailan magsasummarize, kailan magpapabaya, at kailan kukuha muli.

## Mga Susunod na Hakbang

**Susunod na Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Nakaraan: Module 01 - Panimula](../01-introduction/README.md) | [Bumalik sa Pangunahing Pahina](../README.md) | [Susunod: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagsasabi ng Paalala**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat aming pinagsisikapang maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tiyak na impormasyon. Ang orihinal na dokumento sa wikang pinagmulan nito ang dapat ituring na pinakamalawak na sanggunian. Para sa mga mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->