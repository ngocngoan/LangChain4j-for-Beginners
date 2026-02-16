# Module 02: Prompt Engineering wit GPT-5.2

## Table of Contents

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
- [Wetin You Really Dey Learn](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## What You'll Learn

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di module way pass, you see how memory dey enable conversational AI and how we use GitHub Models for basic interaction dem. Now we go focus on how you dey ask questions — di prompts dem self — using Azure OpenAI GPT-5.2. Di way wey you dey arrange your prompts dey affect correct answer dem wey you go get. We go start with review of di basic prompting techniques, then go move enter eight advanced patterns wey take full advantage of GPT-5.2 power.

We go use GPT-5.2 because e introduce reasoning control - you fit tell di model how much e suppose reason before e give answer. This one dey make different prompting strategies clear well well and e go help you sabi when to use each approach. We go also benefit from Azure better rate limit for GPT-5.2 compared to GitHub Models dem.

## Prerequisites

- Finished Module 01 (Azure OpenAI resources wey dem deploy)
- `.env` file dey root directory wit Azure credentials (wey `azd up` create for Module 01)

> **Note:** If you never finish Module 01, first follow deployment instruction dem for there.

## Understanding Prompt Engineering

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na about how you design input text wey go always bring di results wey you need. E no be just to ask question - na how you arrange the request dem so the model go understand exactly wetin you want and how e go deliver am.

Make you think am like you dey give instruction to padi for work. "Fix the bug" no clear. "Fix the null pointer exception for UserService.java line 45 by adding null check" na specific instruction. Language models dem dey work the same way - to get right result specificity and good arrangement of instruction matter.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j na di setup — model connections, memory, and message types — while prompt patterns na carefully arranged text wey you go send through dat setup. Di main things be `SystemMessage` (wey control AI behavior and role) and `UserMessage` (wey carry your real request).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we waka enter advanced patterns for dis module, make we recap five foundational prompting ways. Na dem be di building blocks wey every prompt engineer suppose sabi. If you don already use di [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), you don see dem dey work — dis na di concept framework behind dem.

### Zero-Shot Prompting

Di simplest way: give di model direct instruction without any example. Di model go rely totally on im training to understand and do di task. E good for simple requests wey di behavior wey dem expect clear well well.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction without examples — di model dey figure di task from di instruction only*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Correct"
```

**When to use:** Simple classifications, direct questions, translations, or any task wey di model fit handle without extra instruction.

### Few-Shot Prompting

Give examples wey show di pattern wey you want di model follow. Di model go learn di right input-output format from your examples and apply am to new inputs. Dis one go improve consistency well for tasks wey di desired format or behavior no obvious.

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

**When to use:** Custom classifications, consistent formatting, domain-specific tasks, or when zero-shot results no dey consistent.

### Chain of Thought

Make di model show how e reason step-by-step. Instead make e jump direct to answer, di model go break di problem and work through each part explicitly. Dis one go improve accuracy for math, logic, and multi-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning — breaking complex problems into clear logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model dey show: 15 - 8 = 7, afta dat 7 + 12 = 19 apple dem
```

**When to use:** Math problems, logic puzzles, debugging, or any task wey showing di reasoning process dey improve accuracy plus trust.

### Role-Based Prompting

Assign persona or role to di AI before you ask question. Dis one go give context wey shape di tone, depth, and focus of di response. "Software architect" go give different advice than "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona — same question go get different response based on assigned role*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or when you need responses wey fit special expertise level or perspective.

### Prompt Templates

Make reusable prompts with variable placeholders. Instead make you write new prompt every time, define one template once and fill different values. LangChain4j `PromptTemplate` class dey make dis easy wit `{{variable}}` syntax.

<img src="../../../translated_images/pcm/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable prompts with variable placeholders — one template, many use*

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

**When to use:** Repeated queries wit different inputs, batch processing, building reusable AI workflows, or any situation wey di prompt structure no dey change but data go change.

---

These five fundamentals give you solid toolkit for most prompting tasks. Di rest of dis module go build on top dem with **eight advanced patterns** wey leverage GPT-5.2 reasoning control, self evaluation, and structured output skill.

## Advanced Patterns

With fundamentals cover, make we move enter di eight advanced patterns wey dey make dis module special. No be all problem go need same approach. Some questions need quick answers, others deep thinking. Some need visible reasoning, others na just result we want. Each pattern below dey optimize for different situation — and GPT-5.2 reasoning control dey make di difference come clear well well.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of di eight prompt engineering patterns and their use cases*

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning control let you tell how much di model suppose think — from quick direct answers to deep search*

<img src="../../../translated_images/pcm/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Low eagerness (fast, direct) vs High eagerness (thorough, exploratory) reasoning methods*

**Low Eagerness (Quick & Focused)** - For simple questions wey you want fast, direct answers. Di model go do minimum reasoning - max 2 steps. Use dis for calculations, lookups, or straightforward questions dem.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore wit GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be difference between low eagerness and high eagerness prompting patterns?"
> - "How di XML tags for prompts dey help arrange di AI's response?"
> - "When I suppose use self-reflection patterns and when I suppose use direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problems wey you want deep analysis. Di model dey explore thoroughly and show detailed reasoning. Use dis for system design, architecture decisions, or complex research.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflows. Di model go provide upfront plan, talk each step as e dey do am, then give summary. Use dis for migrations, implementations, or any multi-step process.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting clear-clear ask di model to show how e reason, e dey improve accuracy for complex tasks. Step-by-step breakdown dey help both human and AI understand di logic.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about dis pattern:
> - "How I fit adapt task execution pattern for long-running operations?"
> - "Wetin be best way for arranging tool preambles for production applications?"
> - "How I fit capture and show intermediate progress updates for UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For produce production-quality code. Di model go generate code, check am against quality criteria, then improve am step by step. Use dis when you dey build new features or services.

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

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, find wahala, improve, repeat*

**Structured Analysis** - For consistent evaluation. Di model go review code wit fixed framework (correctness, practices, performance, security). Use dis for code reviews or quality check.

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

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize analysis framework for different code review types?"
> - "Wetin be best way to parse and act on structured output with program?"
> - "How I go make sure consistent severity levels across different review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Four-category framework for consistent code reviews with severity levels*

**Multi-Turn Chat** - For conversation wey need context. Di model go remember previous messages and build on dem. Use dis for interactive help sessions or complex Q&A.

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

*How conversation context dey hold steady over many turns until token limit full*

**Step-by-Step Reasoning** - For problems wey need visible logic. Di model go show explicit reasoning every step. Use dis for math problems, logic puzzles, or when you need sabi di thinking process.

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

*Break down problems into clear logical steps*

**Constrained Output** - For answers wey get specific format rules. Di model go strictly follow format and length rules. Use dis for summaries or when you need exact output structure.

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

*Enforce specific format, length, and structure rules*

## Using Existing Azure Resources

**Make sure deployment correct:**

Check say `.env` file dey root directory wit Azure credentials (wey Module 01 create):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di application:**

> **Note:** If you don already start all apps using `./start-all.sh` from Module 01, dis module dey run already for port 8083. You fit skip di start commands below and go direct to http://localhost:8083.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey provide visual interface to manage all Spring Boot applications. You fit find am for Activity Bar for left side of VS Code (look for Spring Boot icon).
From di Spring Boot Dashboard, you fit:
- See all di Spring Boot applications wey dey available for di workspace
- Start/stop applications wit one click
- View application logs for real-time
- Monitor application status

Just click di play button wey dey next to "prompt-engineering" to start dis module, or start all modules all at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From de root directory
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
cd ..; .\stop-all.ps1  # All modules
```

## Application Screenshots

<img src="../../../translated_images/pcm/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Di main dashboard wey dey show all 8 prompt engineering patterns wit dia characteristics and use cases*

## Exploring di Patterns

Di web interface let you try different prompting strategies. Each pattern dey solve different problems - try dem make you sabi when each method dey shine.

### Low vs High Eagerness

Ask one simple question like "Wetin be 15% of 200?" using Low Eagerness. You go get quick, direct answer. Now ask something complex like "Design a caching strategy for a high-traffic API" using High Eagerness. Watch how di model slow down and provide detailed reasoning. Same model, same question structure - but di prompt tell am how much thinking make e do.

<img src="../../../translated_images/pcm/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Quick calculation wit small reasoning*

<img src="../../../translated_images/pcm/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Complete caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Multi-step workflows benefit from upfront planning and progress narration. Di model go outline wetin e go do, narrate each step, then summarise di results.

<img src="../../../translated_images/pcm/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Creating one REST endpoint wit step-by-step narration (3.9MB)*

### Self-Reflecting Code

Try "Create an email validation service". Instead of just generate code and stop, di model go generate, evaluate am against quality criteria, identify di weak points, and improve. You go see am dey repeat until di code meet production standards.

<img src="../../../translated_images/pcm/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Complete email validation service (5.2MB)*

### Structured Analysis

Code reviews need to get consistent evaluation frameworks. Di model dey analyze code using fixed categories (correctness, practices, performance, security) wit severity levels.

<img src="../../../translated_images/pcm/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based code review*

### Multi-Turn Chat

Ask "Wetin be Spring Boot?" then immediately follow up wit "Show me an example". Di model go remember your first question and give you one Spring Boot example especially. Without memory, dat second question go be too vague.

<img src="../../../translated_images/pcm/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Context preservation across questions*

### Step-by-Step Reasoning

Pick one math problem and try am wit both Step-by-Step Reasoning and Low Eagerness. Low eagerness just go give you di answer - fast but hard to understand. Step-by-step go show you every calculation and decision.

<img src="../../../translated_images/pcm/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Math problem wit explicit steps*

### Constrained Output

When you need specific formats or word counts, dis pattern go make sure Strict Adherence. Try generate one summary wit exactly 100 words in bullet point format.

<img src="../../../translated_images/pcm/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine learning summary wit format control*

## Wetin You Dey Really Learn

**Reasoning Effort Changes Everything**

GPT-5.2 go let you control computational effort through your prompts. Low effort mean fast response wit minimal exploration. High effort mean say di model go take time to think deeply. You dey learn to match effort to task complexity - no waste time on simple questions, but no too rush complex decisions either.

**Structure Guides Behavior**

You notice di XML tags inside di prompts? Dem no be decoration. Models dey follow structured instructions more reliably than freeform text. When you need multi-step processes or complex logic, structure dey help di model track where e dey and wetin come next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomy of one well-structured prompt wit clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Di self-reflecting patterns dey work by making quality criteria explicit. Instead of hoping say di model "go do am right", you go tell am exactly wetin "right" mean: correct logic, error handling, performance, security. Di model fit evaluate im own output and improve. Dis one make code generation no be lottery again but na proper process.

**Context Is Finite**

Multi-turn conversations dey work by including message history wit every request. But e get limit - every model get maximum token count. As conversations dey grow, you go need strategies to keep relevant context without hit dat ceiling. Dis module go show you how memory dey work; later you go learn when to summarize, when to forget, and when to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service wey dem dey call [Co-op Translator](https://github.com/Azure/co-op-translator). As we dey try make am correct, abeg sabi say automated translation fit get some mistakes or small error. Di original document wey dey di main language na di correct one you suppose trust. If na serious matter, e better make you use human professional wey sabi translate. We no go take any blame if person no understand or make mistake because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->