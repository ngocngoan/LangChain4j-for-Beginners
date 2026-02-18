# Module 02: Prompt Engineering wit GPT-5.2

## Table of Contents

- [Wetin You Go Learn](../../../02-prompt-engineering)
- [Wetyn You Need First](../../../02-prompt-engineering)
- [How Prompt Engineering Dey Work](../../../02-prompt-engineering)
- [Basic Prompt Engineering](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Advanced Patterns](../../../02-prompt-engineering)
- [How to Use Azure Resources Wey Don Dey](../../../02-prompt-engineering)
- [Application Screenshots](../../../02-prompt-engineering)
- [How to Explore di Patterns](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [Wetin You Dey Learn True-True](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## Wetin You Go Learn

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di last module, you don see how memory dey help conversational AI and you use GitHub Models do basic kain talks. Now we go focus on how you dey ask questions — the prompts dem — using Azure OpenAI's GPT-5.2. How you take arrange your prompts go affect well well the kind answer wey you go get. We go start with review of the basic prompting methods, then move to eight advanced patterns wey go help you use GPT-5.2 sabi work well.

We go use GPT-5.2 because e get reasoning control - you fit talk to di model how much e go think before e answer. Dis one dey make different prompting strategies clear and e go help you sabi when to use each. We go also gain benefit from Azure's reduced rate limits for GPT-5.2 compared to GitHub Models.

## Wetyn You Need First

- Finish Module 01 (Azure OpenAI resources don deploy)
- `.env` file dey root directory with Azure credentials (wey `azd up` create for Module 01)

> **Note:** If you never finish Module 01, abeg follow the deployment instructions wey dey that module first.

## How Prompt Engineering Dey Work

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na how you take design input text wey go always give you wetin you want. E no just be to ask questions - na how you arrange your request so that di model sabi wetin you mean and how e go deliver am.

Think am like how you go take give instruction to your colleague. "Fix the bug" no clear. "Fix the null pointer exception for UserService.java line 45 by adding null check" clear. Language models na so dem dey work - specificity and structure matter.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j na infrastructure — e handle model connections, memory, and message types — but prompt patterns na text wey you arrange well well wey you dey send through di infrastructure. Di main blocks na `SystemMessage` (wey set the AI behavior and role) and `UserMessage` (wey carry your actual request).

## Basic Prompt Engineering

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we begin the advanced patterns for dis module, make we review five basic prompting techniques. Dem dey form di foundation wey all prompt engineer suppose sabi. If you don work through the [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), you don see these ones for action — na di conceptual framework behind am.

### Zero-Shot Prompting

Di simplest way: give di model direct instruction without example. Di model go use all e training to understand and do di work. E work well for straightforward request wey di expected behavior clear.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction without example — model go infer di wahala from di instruction alone*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Positive"
```

**When to use:** Simple classifications, direct questions, translations, or any task di model fit do without extra guidance.

### Few-Shot Prompting

You go provide examples wey show di pattern wey you want di model follow. Di model go learn di input-output format from your examples and apply am enter new inputs. Dis one dey improve consistency for tasks wey di format or behavior no obvious.

<img src="../../../translated_images/pcm/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Learning from examples — di model go identify di pattern and use am for new inputs*

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

**When to use:** Custom classifications, consistent formatting, domain-specific tasks, or when zero-shot results dey inconsistent.

### Chain of Thought

Ask di model make e show di steps wey e reason. No just jump enter answer, di model go break down di problem and work inside each part clearly. E dey improve accuracy for math, logic, and multi-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning — breaking complex problems enter clear logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model dey show: 15 - 8 = 7, den 7 + 12 = 19 apple dem
```

**When to use:** Math problems, logic puzzles, debugging, or any task wey showing how you reason go improve accuracy and trust.

### Role-Based Prompting

You fit set persona or role for di AI before you ask question. Dis one dey give context wey fit shape di tone, level, and focus of di answer. "software architect" go give different advice from "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona — same question, different answer based on assigned role*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or when you wan get answer wey dey fit particular expertise or point of view.

### Prompt Templates

Create prompts wey you fit reuse with variable placeholders. No need write new prompt every time, define template once and put different values. LangChain4j's `PromptTemplate` class dey make am easy with `{{variable}}` syntax.

<img src="../../../translated_images/pcm/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable prompts with variable placeholders — one template, many uses*

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

**When to use:** Repeated queries with different inputs, batch processing, building reusable AI workflows, or anywhere prompt structure remain the same but data change.

---

These five fundamentals go give you solid toolkit for most prompting tasks. Di rest of dis module go build on top am with **eight advanced patterns** wey go use GPT-5.2 reasoning control, self-evaluation, and structured output abilities.

## Advanced Patterns

With fundamentals done, make we waka enter the eight advanced patterns wey make dis module different. No all problem need di same method. Some questions need quick answers, others need deep thinking. Some need visible reasoning, others just need result. Each pattern below dey designed for different situation — and GPT-5.2 reasoning control go make di differences clear well well.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of eight prompt engineering patterns and their use cases*

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning control dey allow you specify how much di model suppose think — from fast direct answers to deep exploration*

**Low Eagerness (Quick & Focused)** - For simple questions wey you want fast, direct answer. Di model no go too reason - max 2 steps. Use dis for calculations, lookups, or straightforward questions.

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

> 💡 **Try am with GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be difference between low eagerness and high eagerness prompting patterns?"
> - "How di XML tags for prompts dey help form di AI’s response?"
> - "When I go use self-reflection patterns vs direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problems wey you want thorough analysis. Di model go explore well and show detailed reasoning. Use dis for system design, architecture decisions, or deep research.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflow. Di model go provide upfront plan, narrate each step as e dey work, then give summary. Use dis for migrations, implementations, or any multi-step process.

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

Chain-of-Thought prompting dey explicitly tell di model to show how e reason, wey improve accuracy for complex work. Dis step-by-step breakdown dey help both people and AI understand di logic.

> **🤖 Try am with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about this pattern:
> - "How I fit adapt task execution pattern for long-running operations?"
> - "Wetin be best practice to structure tool preambles for production apps?"
> - "How I fit capture and show intermediate progress updates for UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generate production-quality code. Di model go generate code wey follow production standard and handle error well. Use dis when you dey build new features or services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, find wahala, fix am, repeat*

**Structured Analysis** - For consistent evaluation. Di model go review code using fixed framework (correctness, practices, performance, security, maintainability). Use dis for code reviews or quality checks.

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

> **🤖 Try am with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize analysis framework for different code review kinds?"
> - "Wetin be best way to parse and act on structured output programmatically?"
> - "How I go fit ensure severity levels dey consistent across review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework to do consistent code reviews with severity levels*

**Multi-Turn Chat** - For chat wey need context. Di model go remember previous messages and build on top. Use dis for interactive help or complex Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pcm/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*How conversation context dey build across multiple turns until token limit full*

**Step-by-Step Reasoning** - For problems wey need visible logic. Di model go show clear reasoning for each step. Use dis for math problems, logic puzzles, or when you need understand how e reason.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pcm/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Breaking down problems into clear logical steps*

**Constrained Output** - For answer wey need specific format. Di model go follow format and length rules well well. Use dis for summaries or when you need exact output structure.

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

<img src="../../../translated_images/pcm/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Enforcing specific format, length, and structure rules*

## How to Use Azure Resources Wey Don Dey

**Make sure deployment dey okay:**

Check say `.env` file dey root directory with Azure credentials (wey Module 01 create):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don start all the applications using `./start-all.sh` from Module 01, dis module don dey run for port 8083 already. You fit skip start commands below and just go http://localhost:8083 directly.

**Option 1: Use Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey dey give visual interface to manage all Spring Boot apps. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).

For Spring Boot Dashboard, you fit:
- See all Spring Boot apps wey dey workspace
- Start/stop apps with one click
- View app logs for real-time
- Check app status
Simply click di play button wey dey next to "prompt-engineering" to start dis module, or start all di modules for once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From di root folder
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root directory
.\start-all.ps1
```

Or start just dis module:

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

Both scripts go automatically load environment variables from di root `.env` file and dem go build di JARs if dem no dey.

> **Note:** If you prefer to build all modules manually before you start:
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

Open http://localhost:8083 for your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # Dis module only
# Or
cd .. && ./stop-all.sh  # All di modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All di modules
```

## Application Screenshots

<img src="../../../translated_images/pcm/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Di main dashboard wey dey show all 8 prompt engineering patterns wit their characteristics and wey you fit use dem*

## Exploring di Patterns

Di web interface dey let you try different prompting strategies. Each pattern dey solve different problems - try am to see when each approach go shine.

### Low vs High Eagerness

Ask simple question like "WetIn 15% of 200?" using Low Eagerness. You go get instant, direct answer. Now ask something wey complex like "Design caching strategy for high-traffic API" using High Eagerness. Watch how model go slow down and provide detailed reasoning. Na same model, same question style - but di prompt dey tell am how much e suppose think.

<img src="../../../translated_images/pcm/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Quick calculation wit small reasoning*

<img src="../../../translated_images/pcm/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Complete caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Multi-step workflows dey benefit from upfront planning and progress narration. Di model go outline wetin e go do, talk each step, then summarize results.

<img src="../../../translated_images/pcm/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Creating REST endpoint wit step-by-step narration (3.9MB)*

### Self-Reflecting Code

Try "Create email validation service". Instead of just generating code and stopping, di model go generate, evaluate am based on quality criteria, find where e weak, and improve am. You go see am dey keep try till di code reach production standards.

<img src="../../../translated_images/pcm/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Complete email validation service (5.2MB)*

### Structured Analysis

Code reviews need consistent evaluation frameworks. Di model go analyze code using fixed categories (correctness, practices, performance, security) wit severity levels.

<img src="../../../translated_images/pcm/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based code review*

### Multi-Turn Chat

Ask "WetIn Spring Boot?" then quickly follow up wit "Show me example". Di model go remember your first question and give you Spring Boot example specially. Without memory, that second question go too vague.

<img src="../../../translated_images/pcm/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Context preservation across questions*

### Step-by-Step Reasoning

Pick math problem and try am wit Step-by-Step Reasoning and Low Eagerness. Low eagerness just give you answer - fast but no clear. Step-by-step go show every calculation and decision.

<img src="../../../translated_images/pcm/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Math problem wit clear steps*

### Constrained Output

When you need specific format or word counts, dis pattern dey enforce strict adherence. Try generate summary wit exactly 100 words for bullet point format.

<img src="../../../translated_images/pcm/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine learning summary wit format control*

## Wetin You Really Dey Learn

**Reasoning Effort Dey Change Everything**

GPT-5.2 go let you control computational effort through your prompts. Low effort mean fast responses wit small exploration. High effort mean say model go take time think well well. You dey learn how to match effort to task complexity - no waste time on simple questions, but no rush complex decisions too.

**Structure Dey Guide Behaviour**

You notice di XML tags for di prompts? Dem no be for decoration. Models dey follow structured instructions more correct than freeform text. When you need multi-step processes or complex logic, structure dey help di model track where e dey and wetin e go do next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomy of well-structured prompt wit clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Di self-reflecting patterns work by making quality criteria clear. Instead of hoping say model "go do am right", you tell am exactly wetin "right" mean: correct logic, error handling, performance, security. Di model fit evaluate im own output and improve. This one dey turn code generation from lottery to process.

**Context Na Finite**

Multi-turn conversations dey work by including message history wit each request. But e get limit - every model get maximum token count. As conversations dey grow, you go need strategies to keep relevant context without reach dat limit. Dis module go show you how memory dey work; later you go learn when to summarize, when to forget, and when to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) wey translate am. Even though we dey try make am correct, abeg sabi say automated translation fit get error or no too correct. The original document wey dem write for im own language na di correct one. If na serious matter, e good make person wey sabi translate am well do am. We no go take responsibility if person no understand well or if mistake show from this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->