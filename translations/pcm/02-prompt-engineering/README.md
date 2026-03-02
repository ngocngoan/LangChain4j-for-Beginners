# Module 02: Prompt Engineering wit GPT-5.2

## Table of Contents

- [Video Walkthrough](../../../02-prompt-engineering)
- [Wetin You Go Learn](../../../02-prompt-engineering)
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
- [Wetin You Really Dey Learn](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## Video Walkthrough

Watch dis live session wey go explain how to start wit dis module:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Wetin You Go Learn

Dis diagram show overview of the main topics and skills wey you go develop for dis module — from prompt refinement techniques to the step-by-step workflow you go follow.

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di previous modules, you don explore basic LangChain4j interactions wit GitHub Models and see how memory dey enable conversational AI wit Azure OpenAI. Now we go focus on how you dey ask questions — di prompts themselves — using Azure OpenAI's GPT-5.2. Di way wey you structure your prompts go really affect di quality of di responses you go get. We go start wit review of di fundamental prompting techniques, then move go eight advanced patterns wey go take full advantage of GPT-5.2's capabilities.

We go use GPT-5.2 because e bring reasoning control - you fit tell di model how much thinking to do before e answer. Dis one make different prompting strategies clear and go help you sabi when to use each approach. We go also benefit from Azure's fewer rate limits for GPT-5.2 compared to GitHub Models.

## Prerequisites

- Complete Module 01 (Azure OpenAI resources don deploy)
- `.env` file for root directory wit Azure credentials (wey `azd up` make for Module 01)

> **Note:** If you never finish Module 01, make you follow deployment instructions there first.

## Understanding Prompt Engineering

At dia core, prompt engineering na di difference between vague instructions and precise ones, as di comparison below show.

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na to design input text wey go always give you di results wey you want. E no just dey about to ask questions - na to structure requests so di model go understand exactly wetin you want and how e go deliver am.

Think am like how you go dey give instruction to colleague. "Fix the bug" na vague. "Fix the null pointer exception for UserService.java line 45 by adding a null check" na specific instruction. Language models dey work the same way - specificity and structure matter.

Di diagram below show how LangChain4j dey fit inside dis picture — connecting your prompt patterns to di model through SystemMessage and UserMessage building blocks.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j dey provide di infrastructure — model connections, memory, and message types — while prompt patterns na just carefully structured text wey you dey send through dat infrastructure. Di main building blocks na `SystemMessage` (wey dey set di AI's behavior and role) and `UserMessage` (wey carry your actual request).

## Prompt Engineering Fundamentals

Di five core techniques wey dem show below na di foundation of effective prompt engineering. Each one dey handle different aspect of how you dey talk to language models.

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we enter di advanced patterns for dis module, make we review five foundational prompting techniques. Dem na di building blocks wey every prompt engineer suppose sabi. If you don already work through di [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), you don see dem for action — here na di conceptual framework behind dem.

### Zero-Shot Prompting

Di simplest way: give di model direct instruction wit no example. Di model dey rely completely on im training to understand and execute di task. Dis one dey work well for straightforward requests wey di expected behavior clear.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction wit no examples — di model dey infer di task from di instruction alone*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Positiv"
```

**When to use:** Simple classifications, direct questions, translations, or any task wey di model fit handle without extra guidance.

### Few-Shot Prompting

Give examples wey show di pattern wey you want di model to follow. Di model go learn di expected input-output format from your examples and apply am to new inputs. Dis one improve consistency wella for tasks wey di desired format or behavior no obvious.

<img src="../../../translated_images/pcm/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Learn from examples — di model dey identify di pattern and apply am to new inputs*

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

**When to use:** Custom classifications, consistent formatting, domain-specific tasks, or when zero-shot results no dey consistent.

### Chain of Thought

Ask di model to show hin reasoning step-by-step. Instead of jumping direct to answer, di model go break down di problem and work through each part explicitly. Dis one go improve accuracy for math, logic, and multi-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning — dey break complex problems into explicit logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model dey show: 15 - 8 = 7, afta dat 7 + 12 = 19 apple dem
```

**When to use:** Math problems, logic puzzles, debugging, or any task wey showing di reasoning process go improve accuracy and trust.

### Role-Based Prompting

Set persona or role for di AI before you ask your question. Dis one dey provide context wey go shape di tone, depth, and focus of di response. "Software architect" go give different advice than "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona — di same question fit get different response based on di role wey assign*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or when you need responses wey dem tailor to particular expertise level or perspective.

### Prompt Templates

Create reusable prompts wit variable placeholders. Instead of writing new prompt anytime, define template once and fill different values. LangChain4j's `PromptTemplate` class make am easy wit `{{variable}}` syntax.

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

**When to use:** Repeated queries wit different inputs, batch processing, building reusable AI workflows, or any scenario wey di prompt structure remain the same but di data changes.

---

Dis five fundamentals go give you solid toolkit for most prompting tasks. Di rest of dis module go build on dem wit **eight advanced patterns** wey dey leverage GPT-5.2's reasoning control, self-evaluation, and structured output capabilities.

## Advanced Patterns

Now we don cover fundamentals, make we move go di eight advanced patterns wey make dis module special. No all problems need di same approach. Some questions need quick answers, others need deep thinking. Some need visible reasoning, others just need results. Each pattern below optimize for different scenario — and GPT-5.2's reasoning control go make di difference dem even clear.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of di eight prompt engineering patterns and their use cases*

GPT-5.2 add one extra dimension to dis patterns: *reasoning control*. Di slider below show how you fit adjust di model's thinking effort — from quick, direct answers to deep, thorough analysis.

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2's reasoning control let you specify how much thinking di model suppose do — from fast direct answers to deep exploration*

**Low Eagerness (Quick & Focused)** - For simple questions where you want fast, direct answers. Di model go do minimal reasoning - max 2 steps. Use dis for calculations, lookups, or straightforward questions.

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

> 💡 **Explore wit GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be di difference between low eagerness and high eagerness prompting patterns?"
> - "How di XML tags for prompts dey help structure di AI's response?"
> - "When I go use self-reflection patterns vs direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problems where you want full analysis. Di model go explore well and show detailed reasoning. Use dis one for system design, architecture decisions, or complex research.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflows. Di model go provide upfront plan, narrate each step as e dey work, then give summary. Use dis for migrations, implementations, or any multi-step process.

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

Chain-of-Thought prompting dey directly ask di model to show hin reasoning process, to improve accuracy for complex tasks. Di step-by-step breakdown dey help both humans and AI understand di logic.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about dis pattern:
> - "How I go adapt di task execution pattern for long-running operations?"
> - "Wetin be best practices for structuring tool preambles for production applications?"
> - "How I fit capture and display intermediate progress updates for UI?"

Di diagram below show dis Plan → Execute → Summarize workflow.

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generating production-quality code. Di model dey generate code following pro production standards wit proper error handling. Use dis when you dey build new features or services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Di diagram below show dis iterative improvement loop — generate, evaluate, identify weaknesses, and refine till di code reach production standards.

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, identify issues, improve, repeat*

**Structured Analysis** - For consistent evaluation. Di model dey review code using fixed framework (correctness, practices, performance, security, maintainability). Use dis one for code reviews or quality assessments.

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

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize di analysis framework for different types of code reviews?"
> - "Wetin be di best way to parse and act on structured output programmatically?"
> - "How I fit ensure consistent severity levels across different review sessions?"

Di diagram below show how dis structured framework dey organize code review into consistent categories wit severity levels.

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework for consistent code reviews wit severity levels*

**Multi-Turn Chat** - For conversations wey need context. Di model dey remember previous messages and dey build on dem. Use dis for interactive help sessions or complex Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Di diagram below dey show how conversation context dey accumulate as turns dey increase and how e relate to di model's token limit.

<img src="../../../translated_images/pcm/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*How conversation context dey accumulate over multiple turns till e reach di token limit*
**Step-by-Step Reasoning** - For problems wey need visible logic. Di model dey show clear reasoning for every step. Use dis one for math problems, logic puzzles, or anytime you want understand how di thinking process dey go.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Di diagram below dey show how di model dey break problems into clear, numbered logical steps.

<img src="../../../translated_images/pcm/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Breaking down problems into explicit logical steps*

**Constrained Output** - For responses wey get specific format requirements. Di model go follow format and length rules strictly. Use dis one for summaries or anytime you need precise output structure.

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

Di diagram wey follow dey show how constraints dey guide di model to produce output wey strictly follow your format and length requirements.

<img src="../../../translated_images/pcm/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Enforcing specific format, length, and structure requirements*

## Run the Application

**Verify deployment:**

Make sure di `.env` file dey inside root directory with Azure credentials (wey dem create for Module 01). Run dis one from di module directory (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don start all applications already using `./start-all.sh` from root directory (like dem talk for Module 01), dis module don dey run for port 8083. You fit skip di start commands below and go straight to http://localhost:8083.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension wey dey provide visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for di Spring Boot icon).

From di Spring Boot Dashboard, you fit:
- See all di Spring Boot applications wey dey for workspace
- Start/stop applications with just one click
- View application logs in real-time
- Monitor application status

Just click di play button wey dey near "prompt-engineering" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*The Spring Boot Dashboard in VS Code — start, stop, and monitor all modules from one place*

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From di root directory
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

Both scripts go automatically load environment variables from root `.env` file and them go build di JARs if dem no dey.

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
cd .. && ./stop-all.sh  # All modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All di modules
```

## Application Screenshots

Here be di main interface of di prompt engineering module, where you fit experiment with all eight patterns side by side.

<img src="../../../translated_images/pcm/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Di main dashboard wey dey show all 8 prompt engineering patterns with their characteristics and use cases*

## Exploring the Patterns

Di web interface dey let you experiment with different prompting strategies. Every pattern dey solve different problems - try dem to see when each approach go shine.

> **Note: Streaming vs Non-Streaming** — Every pattern page get two buttons: **🔴 Stream Response (Live)** and one **Non-streaming** option. Streaming dey use Server-Sent Events (SSE) to display tokens for real-time as di model dey generate dem, so you fit see progress immediately. Di non-streaming option go wait for di full response before e show am. For prompts wey go make model do deep reasoning (like High Eagerness, Self-Reflecting Code), di non-streaming call fit take plenty time — sometimes minutes — with no feedback wey dey visible. **Use streaming anytime you dey experiment with complex prompts** so you fit see di model dey work and avoid di impression say di request don time out.
>
> **Note: Browser Requirement** — Di streaming feature dey use Fetch Streams API (`response.body.getReader()`) wey require full browser (Chrome, Edge, Firefox, Safari). E no dey work for VS Code's built-in Simple Browser, because di webview no support di ReadableStream API. If you dey use di Simple Browser, di non-streaming buttons go still work normally — na only di streaming buttons go get wahala. Open `http://localhost:8083` for external browser to get full experience.

### Low vs High Eagerness

Ask simple question like "Wetn be 15% of 200?" using Low Eagerness. You go get answer sharp and direct. Now ask something complex like "Design caching strategy for high-traffic API" using High Eagerness. Click **🔴 Stream Response (Live)** and watch model dey show detailed reasoning token-by-token. Same model, same question structure - but di prompt dey guide am how much thinking to do.

### Task Execution (Tool Preambles)

Multi-step workflows need upfront planning and progress narration. Di model dey outline wetn e go do, narrate every step, then summarize results.

### Self-Reflecting Code

Try "Create an email validation service". Instead of just generating code and stop, di model go generate, check am against quality criteria, find weaknesses, and improve. You go see am dey try till di code meet production standards.

### Structured Analysis

Code reviews need constant evaluation frameworks. Di model go analyze code using fixed categories (correctness, practices, performance, security) with severity levels.

### Multi-Turn Chat

Ask "Wetn be Spring Boot?" then follow immediately with "Show me an example". Di model go remember your first question and give you Spring Boot example specially. Without memory, dat second question go too vague.

### Step-by-Step Reasoning

Pick math problem try am with Step-by-Step Reasoning and Low Eagerness together. Low eagerness go just give answer quick but no details. Step-by-step go show every calculation and decision.

### Constrained Output

If you need specific formats or word count, dis pattern dey enforce strict rules. Try generate summary with exactly 100 words for bullet points format.

## Wetn You Really Dey Learn

**Reasoning Effort Changes Everything**

GPT-5.2 allow you control computational effort through your prompts. Low effort mean fast response with small exploration. High effort mean model go spend time think well. You dey learn how to match effort to task complexity - no waste time on simple questions, but no rush complex decisions too.

**Structure Guides Behavior**

You see di XML tags for di prompts? Dem no be decoration. Models dey follow structured instructions more steady pass freeform text. When you need multi-step processes or complex logic, structure dey help model track where e dey and wetn come next. Di diagram below break down well-structured prompt, showing how tags like `<system>`, `<instructions>`, `<context>`, `<user-input>`, and `<constraints>` dey organize your instructions into clear sections.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomy of well-structured prompt with clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Di self-reflecting patterns dey work by making quality criteria explicit. Instead of hoping say di model "go do am right", you go tell am exactly wetn "right" mean: correct logic, error handling, performance, security. Di model fit evaluate im own output and improve. Dis one dey turn code generation from lottery into process.

**Context Is Finite**

Multi-turn conversation dey work by including message history with every request. But e get limit - every model get max token count. As conversation dey grow, you need strategy to keep relevant context without reach dat limit. Dis module go show you how memory work; later you go learn when to summarize, when to forget, and when to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate by AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we try make am correct, abeg sabi say automated translations fit get some mistakes or wrong parts. Di original document for dia correct language na di main thing wey you suppose trust. For important tori, e better make professional human translation do am. We no go take responsibility if person misunderstand or misinterpret anything wey come from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->