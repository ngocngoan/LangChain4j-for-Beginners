# Module 02: Prompt Engineering wit GPT-5.2

## Table of Contents

- [Wetin You Go Learn](../../../02-prompt-engineering)
- [Wetsudo You Need](../../../02-prompt-engineering)
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
- [Wetin You Dey Really Learn](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## Wetin You Go Learn

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di last module, you don see how memory dey enable conversational AI and how to use GitHub Models for small small interaction dem. Now we go focus on how you dey ask question — the prompts theirself — using Azure OpenAI's GPT-5.2. Di way wey you structure your prompts dey affect how better the answers wey you go receive be. We go start with review of di main prompting techniques, then move go eight advanced patterns wey go use GPT-5.2 full ground.

We go use GPT-5.2 because e introduce reasoning control - you fit talk how much thinking di model suppose do before e give answer. Dis one dey make different prompting strategies clear and e help you sabi wen you go use each kind approach. We also go get benefit from Azure wey get less rate limits for GPT-5.2 compared to GitHub Models.

## Wetsudo You Need

- You don finish Module 01 (Azure OpenAI resources deploy inside)
- `.env` file dey root directory wit Azure credentials (e dey created by `azd up` for Module 01)

> **Note:** If you never finish Module 01, abeg follow di deployment instructions wey dey there first.

## Understanding Prompt Engineering

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na about how you go design di input text wey go always bring di results wey you want. E no be just ask question - e mean structure di request so di model go sabi exactly wetin you want and how e go deliver am.

Think am like how you go give instruction to your colleague. "Fix the bug" na vague instruction. "Fix the null pointer exception for UserService.java line 45 by adding null check" na specific instruction. Language models dey work like dat too - specificity and structure matter well well.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j na di infrastructure — model connections, memory, and message types — while prompt patterns na just well structured text wey you dey send through dat infrastructure. Di key building blocks na `SystemMessage` (wey dey set AI behavior and role) and `UserMessage` (wey carry your actual request).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we enter advanced patterns for dis module, make we review five main prompting techniques. Dem be di building blocks wey every prompt engineer suppose sabi. If you don work wit di [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) before, you don see these ones in action already — here na di main idea behind dem.

### Zero-Shot Prompting

Na di simplest way be dis: you give di model direct instruction without example. Di model go depend on im training alone to understand and do di work. Dis one dey work well for simple requests wey di expected behavior dey clear.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction without example — di model go infer di task from di instruction only*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Positiv"
```

**When to use:** Simple classifications, direct questions, translations, or any task wey di model fit handle without extra guidance.

### Few-Shot Prompting

Make you provide examples wey show di pattern wey you want di model to follow. Di model go learn di expected input-output format from your examples and apply am to new input dem. Dis one dey sharply improve consistency for tasks wey di desired format or behavior no too clear.

<img src="../../../translated_images/pcm/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Learning from examples — di model go identify di pattern and apply am to new inputs*

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

**When to use:** Custom classifications, consistent formatting, domain-specific tasks, or wen zero-shot results no steady.

### Chain of Thought

Ask di model to show how e dey reason step by step. Instead of just jump straight to answer, di model go break down di problem and work through each part clearly. Dis one dey improve accuracy for math, logic, and multi-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning — breaking complex problems into explicit logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model dey show: 15 - 8 = 7, den 7 + 12 = 19 apple dem
```

**When to use:** Math problems, logic puzzles, debugging, or any task wey showing reasoning dey improve accuracy and trust.

### Role-Based Prompting

Set persona or role for di AI before you ask your question. Dis one dey provide context wey dey shape di tone, depth, and focus of di answer. "Software architect" go give different advice than "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona — di same question fit get different answer depending on di assigned role*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or wen you need answers wey fit one particular expertise level or perspective.

### Prompt Templates

You fit create reusable prompts wey get variable placeholders. Instead of you write new prompt every time, you fit define one template once and just add different values. LangChain4j `PromptTemplate` class dey make dis easy wit `{{variable}}` syntax.

<img src="../../../translated_images/pcm/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable prompts wit variable placeholders — one template, many uses*

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

**When to use:** Repeated queries wit different inputs, batch processing, building reusable AI workflows, or any time wey prompt structure dey same but data dey change.

---

Dis five fundamentals go give you solid toolkit for most prompting tasks. Di rest of dis module go build on dem wit **eight advanced patterns** wey go use GPT-5.2 reasoning control, self-evaluation, and structured output abilities.

## Advanced Patterns

Now we don cover fundamentals, make we move to di eight advanced patterns wey make dis module special. No every question need di same way. Some questions need quick answers, some need deep thinking. Some need visible reasoning, some just need results. Each pattern below dey designed for different scenario — and GPT-5.2 reasoning control dey make di difference even clearpass.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of eight prompt engineering patterns and their use cases*

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning control let you tell how much thinking di model go do — from fast direct answers to deep exploration*

<img src="../../../translated_images/pcm/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Low eagerness (fast, direct) vs High eagerness (thorough, exploratory) reasoning approaches*

**Low Eagerness (Quick & Focused)** - For simple questions where you want fast, direct answer dem. Di model dey do small reasoning - max 2 steps. Use dis one for calculations, lookup, or simple question dem.

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

> 💡 **Try am wit GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be di difference between low eagerness and high eagerness prompting patterns?"
> - "How di XML tags for prompts dey help structure di AI response?"
> - "Wen I suppose use self-reflection patterns vs direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problem dem wey you want full analysis. Di model go explore thoroughly and show detailed reasoning. Use dis for system design, architecture decisions, or complex research.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflow dem. Di model go give upfront plan, narrate each step as e dey work, then give summary. Use dis for migrations, implementation, or any multi-step process.

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

Chain-of-Thought prompting explicitly talk make di model show im reasoning process, e dey improve accuracy for complex tasks. Di step-by-step breakdown dey help human and AI understand di logic better.

> **🤖 Try am wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about dis pattern:
> - "How I go adapt task execution pattern for long-running operations?"
> - "Wetin be best practices for structuring tool preambles for production apps?"
> - "How I fit capture and show intermediate progress updates inside UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generating production-level code. Di model go generate code wey follow production standard wit proper error handling. Use dis for building new feature dem or services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, find issues, improve, repeat*

**Structured Analysis** - For consistent evaluation. Di model go review code using fixed framework (correctness, practices, performance, security, maintainability). Use dis for code reviews or quality assessment.

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

> **🤖 Try am wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize analysis framework for different kind code reviews?"
> - "Wetin be best way to parse and act on structured output programmatically?"
> - "How I fit ensure steady severity levels across different review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework for steady code reviews wit severity levels*

**Multi-Turn Chat** - For conversation wey need context. Di model go remember old messages and build on top of dem. Use dis for interactive help session or complex Q&A.

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

*How conversation context dey add up through many turns until token limit reach*

**Step-by-Step Reasoning** - For problem dem wey need show logic. Di model go show reasoning clearly for every step. Use dis for math problems, logic puzzles, or wen you want understand di thinking process.

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

*Breaking down problem dem into clear logical steps*

**Constrained Output** - For answers wey get specific format requirements. Di model go strictly follow format and length rule. Use dis for summaries or if you want correct output structure.

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

*Make sure say specific format, length, and structure requirement dem dey follow*

## Using Existing Azure Resources

**Make sure deployment dey:**

Check say `.env` file dey root directory with Azure credentials (e dey created during Module 01):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di application:**

> **Note:** If you don start all di applications using `./start-all.sh` from Module 01, dis module don dey run for port 8083. You fit skip di start commands below and go straight to http://localhost:8083.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey provide visual interface to manage all Spring Boot applications. You fit find am for di Activity Bar for left side of VS Code (look for Spring Boot icon).
From di Spring Boot Dashboard, you fit:
- See all di Spring Boot applications wey dey for di workspace
- Start/stop applications with just one click
- View application logs for real-time
- Monitor application status

Just click di play button wey dey next to "prompt-engineering" to start dis module, or start all di modules at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root folder
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

Both scripts go automatically load environment variables from di root `.env` file and go build di JARs if dem no dey.

> **Note:** If you prefer to build all di modules manually before you start:
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
cd .. && ./stop-all.sh  # All modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All modules
```

## Application Screenshots

<img src="../../../translated_images/pcm/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Di main dashboard dey show all di 8 prompt engineering patterns with their characteristics and how dem dey use*

## Exploring di Patterns

Di web interface dey allow you experiment wit different prompting strategies. Each pattern dey solve different wahala - try dem to see when each approach dey show.

### Low vs High Eagerness

Ask simple question like "Wetyn be 15% of 200?" using Low Eagerness. You go get quick, direct answer. Now ask something wey complex like "Design caching strategy for high-traffic API" using High Eagerness. Watch how di model slow down and give detailed reasoning. Same model, same question format - but di prompt dey tell am how much e go reason.

<img src="../../../translated_images/pcm/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Quick calculation wit small reason*

<img src="../../../translated_images/pcm/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Complete caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Multi-step workflow dem sabi benefit from upfront planning and narration of progress. Di model go talk wetin e go do, narrate each step, then summarize di results.

<img src="../../../translated_images/pcm/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Wetin dey create REST endpoint with step-by-step narration (3.9MB)*

### Self-Reflecting Code

Try "Create email validation service". Instead make e just generate code and stop, di model go generate, check with quality criteria, find weak points, and improve. You go see am dey repeat until di code reach production standard.

<img src="../../../translated_images/pcm/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Complete email validation service (5.2MB)*

### Structured Analysis

Code reviews need consistent evaluation framework. Di model go analyze code using fixed categories (correctness, practices, performance, security) with severity levels.

<img src="../../../translated_images/pcm/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based code review*

### Multi-Turn Chat

Ask "Wetyn be Spring Boot?" then immediately follow up wit "Show me example". Di model go remember your first question and give you Spring Boot example specifically. Without memory, dat second question go too vague.

<img src="../../../translated_images/pcm/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Context dey preserved across questions*

### Step-by-Step Reasoning

Pick math problem and try am wit both Step-by-Step Reasoning and Low Eagerness. Low eagerness go just give you answer quick - fast but e no clear. Step-by-step go show you every calculation and decision.

<img src="../../../translated_images/pcm/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Math problem wit clear steps*

### Constrained Output

When you need specific format or word count, dis pattern go enforce strict adherence. Try generate summary wit exactly 100 words for bullet point format.

<img src="../../../translated_images/pcm/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine learning summary wit format control*

## Wetyn You Dey Really Learn

**Reasoning Effort Changes Everything**

GPT-5.2 go allow you control computational effort through your prompts. Low effort mean fast answers wit small exploration. High effort mean di model go take time to reason well. You dey learn how to match effort to task complexity - no waste time for simple questions, but no rush complex decisions either.

**Structure Guides Behavior**

You see XML tags for di prompts? Dem no just dey for decoration. Models follow structured instructions better than freeform text. When you need multi-step processes or complex logic, structure go help di model remember where e dey and wetin come next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*An anatomy of well-structured prompt wit clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Self-reflecting patterns dey work by making quality criteria explicit. Instead of hoping di model "go do am right", you go tell am exactly wetin "right" mean: correct logic, error handling, performance, security. Di model fit then evaluate its own output and improve. Dis turn code generation from lottery to better process.

**Context Na Limited**

Multi-turn conversations work by including message history for each request. But e get limit - every model get max token count. As conversations grow, you go need strategies to keep relevant context without reach dat limit. Dis module show you how memory dey work; later you go learn when to summarize, when to forget, and when to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**: Dis document e don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we try make e correct, abeg make you sabi say automatic translation fit get some mistake or no too accurate. Di original document for dia own language na di correct one you suppose trust. If na serious tins, make person wey sabi translate do am. We no responsible if you no understand well or if you get wahala because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->