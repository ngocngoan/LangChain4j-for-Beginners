# Module 02: Prompt Engineering wit GPT-5.2

## Table of Contents

- [Video Walkthrough](../../../02-prompt-engineering)
- [Wetín You Go Learn](../../../02-prompt-engineering)
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
- [Wetín You Really Dey Learn](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## Video Walkthrough

Watch dis live session wey dey explain how to start wit dis module: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Wetín You Go Learn

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di module wey pass, you don see how memory dey enable conversational AI and how you take use GitHub Models for basic interactions. Now we go focus on how you go dey ask questions — di prompts by themselves — using Azure OpenAI GPT-5.2. Di way wey you arrange your prompts dey affect di quality of answers wey you go get. We go start wit review of di fundamental prompting techniques, then move enter eight advanced patterns wey go make full use of GPT-5.2 power.

We dey use GPT-5.2 because e get reasoning control - you fit tell di model how much e suppose think before e answer. This one dey make different prompting strategies clear and e go help you know when you go use each approach. We go also enjoy Azure less strict limit for GPT-5.2 compared to GitHub Models.

## Prerequisites

- You don finish Module 01 (Azure OpenAI resources don deploy)
- `.env` file dey root directory wit Azure credentials (make e from `azd up` for Module 01)

> **Note:** If you never finish Module 01, abeg follow di deployment instruction wey dey dia first.

## Understanding Prompt Engineering

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na designing input text wey go always give you di kind results wey you want. E no be just to dey ask questions - na to arrange your request so dat di model go understand exactly wetin you want and how e go deliver am.

Think am like to dey give instruction to colleague. "Fix the bug" na vague. But "Fix the null pointer exception for UserService.java line 45 by adding null check" na specific. Language models work same way - specificity and structure dey important.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j na di infrastructure — model connections, memory, and message types — but prompt patterns na just carefully arranged text wey you send through dat infrastructure. Di main building blocks na `SystemMessage` (wey set di AI behavior and role) and `UserMessage` (wey carry your real request).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we dive into di advanced patterns for dis module, mek we review five foundational prompting techniques. Dis na di building blocks wey every prompt engineer suppose sabi. If you don already waka through di [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), you don see dem for action — na dis conceptual framework de behind dem.

### Zero-Shot Prompting

Na di simplest way: give di model direct instruction without example. Di model go rely totally on dia training to understand and do di work. E good for simple request wey di expected behavior easy to see.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction without examples — di model dey infer di task from di instruction only*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Positif"
```

**When to use:** Simple classifications, direct questions, translations, or any task wey di model fit do without extra guideline.

### Few-Shot Prompting

Give examples wey show di pattern wey you want make di model follow. Di model go learn how input and output suppose be from your examples and apply am to new inputs. Dis one dey improve consistency wella for tasks wey di desired format or behavior no too clear.

<img src="../../../translated_images/pcm/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Learning from examples — di model dey identify di pattern and apply am to new inputs*

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

**When to use:** Custom classifications, consistent formatting, domain-specific tasks, or when zero-shot results no too consistent.

### Chain of Thought

Ask di model to show how e dey reason step by step. Instead of jumping direct to answer, di model go break down di problem and take am one step at a time. Dis one dey boost accuracy for maths, logic, and multi-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning — breaking complex problems into explicit logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model show: 15 - 8 = 7, den 7 + 12 = 19 apples
```

**When to use:** Maths wahala, logic puzzles, debugging, or any task wey showing di reasoning go make di answer accurate and trustworthy.

### Role-Based Prompting

Set persona or role for AI before you ask dia question. Dis one dey provide context wey go shape di tone, depth, and focus of di answer. "Software architect" go talk different thing pass "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona — di same question fit get different answer based on di role assigned*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or when you want answer wey fit particular expertise level or perspective.

### Prompt Templates

Create prompts wey you fit reuse with variable placeholders. Instead of to dey write new prompt every time, define template once and fill different values. LangChain4j `PromptTemplate` class dey make dis easy wit `{{variable}}` syntax.

<img src="../../../translated_images/pcm/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable prompts wey get variable placeholders — one template, many uses*

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

**When to use:** Repeated queries with different inputs, batch processing, building reusable AI workflows, or any case wey di prompt structure dey same but data dey change.

---

Dis five fundamentals give you solid toolkit for most prompting tasks. Di rest of dis module go build on top dem with **eight advanced patterns** wey go use GPT-5.2 reasoning control, self-evaluation, and structured output power.

## Advanced Patterns

With fundamentals done, make we move to di eight advanced patterns wey make dis module special. No all problem need di same way. Some questions need quick answer, others need deep thinking. Some need visible reasoning, others just want results. Each pattern below optimize for different scenario — and GPT-5.2 reasoning control dey make di differences clear.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of di eight prompt engineering patterns and wetin dem dey use for*

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning control dey let you tell how much thinking di model suppose do — from quick direct answers to deep investigation*

**Low Eagerness (Quick & Focused)** - For simple questions wey you want quick, direct answers. Model dey do just little reasoning - maximum two steps. Use am for calculations, lookups, or direct questions.

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

> 💡 **Explore with GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be di difference between low eagerness and high eagerness prompting patterns?"
> - "How di XML tags for prompts dey help to arrange di AI response?"
> - "When I suppose use self-reflection patterns versus direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problems wey you want thorough analysis. Di model go explore well and show detailed reasoning. Use am for system design, architecture decisions, or complex research.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflows. Di model go give upfront plan, talk about each step as e dey do am, then give summary. Use am for migrations, implementations, or any multi-step process.

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

Chain-of-Thought prompting dey explicitly ask di model to show dia reasoning process, e dey help improve accuracy for complex tasks. Di step-by-step breakup dey help both humans and AI understand di logic.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about dis pattern:
> - "How I fit adjust task execution pattern for long-running operations?"
> - "Wetin be best practice for arranging tool preambles in production applications?"
> - "How I fit capture and show intermediate progress updates for UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generating production-quality code. Di model go generate code wey follow production standards with proper error handling. Use am when you dey build new features or services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, find problems, improve, repeat*

**Structured Analysis** - For consistent evaluation. Model go review code using one fixed framework (correctness, practices, performance, security, maintainability). Use am for code reviews or quality checks.

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

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize analysis framework for different code review types?"
> - "Wetin be di best way to parse and act on structured output programmatically?"
> - "How I fit make sure consistent severity levels across different review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework for consistent code reviews with severity levels*

**Multi-Turn Chat** - For conversations wey need context. Di model go remember previous messages and build on top dem. Use am for interactive help sessions or complex Q&A.

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

*How conversation context dey collect over multiple turns till e reach token limit*

**Step-by-Step Reasoning** - For problems wey need visible logic. Di model go show explicit reasoning for each step. Use am for math problems, logic puzzles, or when you want understand how e think.

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

*Breaking down problems into explicit logical steps*

**Constrained Output** - For answers wey get specific format rules. Di model go strictly follow format and length rules. Use am for summaries or when you need precise output structure.

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

*Enforcing specific format, length, and structural rules*

## Using Existing Azure Resources

**Verify deployment:**

Make sure `.env` file dey root directory wit Azure credentials (wey dem create for Module 01):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don already start all applications using `./start-all.sh` from Module 01, dis module don dey run for port 8083. You fit skip di start commands below and go straight to http://localhost:8083.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**
Di dev container get di Spring Boot Dashboard extension, wey dey provide beta visual interface to manage all Spring Boot applications. You fit find am for di Activity Bar for di left side of VS Code (look for di Spring Boot icon).

From di Spring Boot Dashboard, you fit:
- See all di available Spring Boot applications for di workspace
- Start/stop applications wit one click
- View application logs for real-time
- Monitor application status

Just click di play button wey dey next to "prompt-engineering" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory na where e start from
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

Both scripts dey automatically load environment variables from di root `.env` file and go build di JARs if dem no dey.

> **Note:** If you prefer to build all modules manually before you start am:
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

Open http://localhost:8083 inside your browser.

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

*Di main dashboard wey dey show all 8 prompt engineering patterns wit their characteristics and how dem dey use am*

## Exploring the Patterns

Di web interface dey make you experiment wit different prompting strategies. Each pattern dey solve different problems - try dem to see wen each approach dey shine.

> **Note: Streaming vs Non-Streaming** — Every pattern page get two buttons: **🔴 Stream Response (Live)** and one **Non-streaming** option. Streaming dey use Server-Sent Events (SSE) to display tokens live as di model dey generate dem, so you dey see progress sharp-sharp. Di non-streaming option go wait make di whole response finish before e display am. For prompts wey dey trigger deep reasoning (e.g., High Eagerness, Self-Reflecting Code), di non-streaming call fit take plenty time — sometimes minutes — without any visible feedback. **Use streaming when you dey experiment wit complex prompts** so you fit see di model dey work and no go think say di request don time out.
>
> **Note: Browser Requirement** — Di streaming feature dey use di Fetch Streams API (`response.body.getReader()`) wey need full browser (Chrome, Edge, Firefox, Safari). E no dey work for VS Code built-in Simple Browser, as e webview no support di ReadableStream API. If you use di Simple Browser, di non-streaming buttons go still work normally — na only di streaming buttons dey affected. Open `http://localhost:8083` for external browser to get di complete experience.

### Low vs High Eagerness

Ask simple question like "Wetin be 15% of 200?" using Low Eagerness. You go get instant, direct answer. Now ask something complex like "Design a caching strategy for high-traffic API" using High Eagerness. Click **🔴 Stream Response (Live)** and watch how di model go reason every token. Same model, same question structure - but di prompt tell am how much thinking e suppose do.

### Task Execution (Tool Preambles)

Multi-step workflows dey benefit from upfront planning and progress narration. Di model go tell wetin e go do, go yarn each step, then go summarize results.

### Self-Reflecting Code

Try "Create an email validation service". Instead of just to generate code and stop, di model go generate, evaluate the code against quality criteria, find weaknesses, and improve am. You go see am dey try until di code reach production standard.

### Structured Analysis

Code reviews need consistent evaluation frameworks. Di model go analyze code using fixed categories (correctness, practices, performance, security) with levels of severity.

### Multi-Turn Chat

Ask "Wetin be Spring Boot?" then immediately follow up wit "Show me example". Di model go remember your first question and give you example wey concern Spring Boot. Without memory, that second question go too vague.

### Step-by-Step Reasoning

Pick one math problem and try am wit both Step-by-Step Reasoning and Low Eagerness. Low eagerness go just give you di answer - fast but no clear. Step-by-step go show you every calculation and decision.

### Constrained Output

When you need specific formats or word count, dis pattern go force strict adherence. Try generate summary wey get exactly 100 words wit bullet point format.

## Wetin You Really Dey Learn

**Reasoning Effort Changes Everything**

GPT-5.2 let you control computational effort thru your prompts. Low effort mean fast responses wit small exploration. High effort mean di model go take time to reason well-well. You dey learn how to match effort to task complexity - no waste time on simple questions, but no rush complex decisions too.

**Structure Guides Behavior**

You notice di XML tags inside di prompts? Dem no be decoration. Models dey follow structured instructions pass freeform text. When you need multi-step processes or complex logic, structure dey help di model track where e dey and wetin e go do next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Di anatomy of correct structured prompt wit clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Di self-reflecting patterns dey work by making quality criteria clear. Instead say make model "do am right", you go tell am exactly wetin "right" mean: correct logic, error handling, performance, security. Di model fit then evaluate im own output and improve am. Dis turn code generation from luck into process.

**Context Is Finite**

Multi-turn conversations dey work by including message history wit each request. But get limit - every model get max token count. As conversations dey grow, you go need strategies to keep relevant context without hit di limit. Dis module dey show you how memory dey work; later you go learn wen to summarize, wen to forget, and wen to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dis document na translated version wey AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) do. Even though we dey try make am correct, abeg sabi say automated translations fit get some errors or mistakes. The original document wey e dey for im own language na the correct and original source. If na important info you need, make you use professional human translation. We no go take responsibility for any wrong understanding or wrong meaning wey fit show because of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->