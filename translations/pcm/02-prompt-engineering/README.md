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

## Video Walkthrough

Watch dis live session wey explain how to start dis module:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Wetin You Go Learn

<img src="../../../translated_images/pcm/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

For di previous module, you see how memory dey enable conversational AI and how you take use GitHub Models for basic talks. Now we go focus on how you dey ask questions – the prompts themself – using Azure OpenAI GPT-5.2. Di way wey you organize your prompts go really affect di quality of di answers wey you go get. We go start wit review of di basic prompting techniques, then go enter eight advanced patterns wey go make full use of GPT-5.2 power.

We go use GPT-5.2 because e get reasoning control – you fit tell di model how much thinking e go do before e answer. Dis one dey make different prompting strategies clear wella and e dey help you sabi when to use which style. Plus, we go benefit from Azure wey get fewer rate limits for GPT-5.2 compared to GitHub Models.

## Prerequisites

- You don finish Module 01 (Azure OpenAI resources don deploy)
- `.env` file dey inside root directory wit Azure credentials (e self create when you run `azd up` for Module 01)

> **Note:** If you neva finish Module 01, abeg follow di deployment instructions wey dey there first.

## Understanding Prompt Engineering

<img src="../../../translated_images/pcm/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering na how you design input text wey go make sure say you dey get di results wey you want. E no be only to dey ask questions – na to arrange your request make di model understand well well wetin you want make e deliver am.

Think am like you dey give instruction to your work padi. "Fix the bug" no clear. "Fix the null pointer exception in UserService.java line 45 by adding a null check" sharp wella. Language models dey work same way – e good make you dey specific and organize well.

<img src="../../../translated_images/pcm/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j na di infrastructure – e get model connections, memory, and message types – but prompt patterns na just well-arranged text wey you send through dat infrastructure. Di main building blocks na `SystemMessage` (wey dey set how AI go behave and wetin role e get) and `UserMessage` (wey carry your exact request).

## Prompt Engineering Fundamentals

<img src="../../../translated_images/pcm/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Before we dive into di advanced patterns for dis module, make we review five basic prompting techniques. Dem be like di foundation wey every prompt engineer suppose sabi. If you don work through di [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), you don see dem work – here be di conceptual idea wey dey behind dem.

### Zero-Shot Prompting

Di simplest style: give di model direct instruction without any example. Di model go rely purely on how e take train to understand and do di task. E dey work well for simple requests wey clear.

<img src="../../../translated_images/pcm/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Direct instruction without examples – model dey infer di task from di instruction alone*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Response: "Positiv"
```

**When to use:** Simple classifications, direct questions, translations, or any task wey di model fit handle without extra guidance.

### Few-Shot Prompting

You go provide examples wey go show di pattern wey you want make di model follow. Di model go learn di expected input-output format from your examples and apply am for new inputs. E dey improve consistency wella where di correct format no too obvious.

<img src="../../../translated_images/pcm/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Learning from examples – di model dey identify di pattern and apply am for new inputs*

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

Ask di model to show how e dey reason step-by-step. Instead make e jump direct to answer, model go break di problem into parts and reason each step sharp sharp. E dey improve accuracy for math, logic, and many-step reasoning tasks.

<img src="../../../translated_images/pcm/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Step-by-step reasoning – breaking complex problems into explicit logical steps*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Di model dey show: 15 - 8 = 7, den 7 + 12 = 19 apples
```

**When to use:** Math problems, logic puzzles, debugging, or any task wey showing reasoning process go improve accuracy and trust.

### Role-Based Prompting

Before you ask question, set di AI persona or role. Dis one dey provide context wey go shape di tone, depth, and focus of di answer. A "software architect" go give different advice than "junior developer" or "security auditor".

<img src="../../../translated_images/pcm/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setting context and persona – di same question fit get different answer depending on role assigned*

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

**When to use:** Code reviews, tutoring, domain-specific analysis, or if you want answer wey focus for particular expertise level or perspective.

### Prompt Templates

Make re-usable prompts wit variable placeholders. Instead of writing new prompt every time, you fit define template once and put different values inside. LangChain4j `PromptTemplate` class dey make dis easy wit `{{variable}}` syntax.

<img src="../../../translated_images/pcm/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Reusable prompts wit variable placeholders – one template, many uses*

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

**When to use:** Repeated queries with different inputs, batch processing, building reusable AI workflows, or any situation wey di prompt structure stay same but data change.

---

Dis five fundamentals go give you solid toolkit for most prompting tasks. Di rest of dis module go build on dem wit **eight advanced patterns** wey take advantage of GPT-5.2 reasoning control, self-evaluation, and structured output skills.

## Advanced Patterns

After we cover fundamentals, make we move go di eight advanced patterns wey make dis module special. No be all problem go need same approach. Some questions need quick answer, some need deep thinking. Some need visible reasoning, some just need result. Each pattern below dey optimized for different cases – plus GPT-5.2 reasoning control dey make difference clear well well.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of di eight prompt engineering patterns and how dem dey use dem*

<img src="../../../translated_images/pcm/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning control dey let you decide how much thinking di model go do – from quick direct answers to deep exploration*

**Low Eagerness (Quick & Focused)** - For simple question wey you want fast, direct answer. Model go do small thinking - like maximum 2 steps. Use dis one for calculations, lookups, or simple question.

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
> - "How di XML tags for prompts dey help structure di AI response?"
> - "When I go use self-reflection patterns vs direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex wahala wey you want full analysis. Model go explore well well and show detailed reasoning. Use dis one for system design, architecture decisions, or complex research.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For workflows wey get many steps. Model go give upfront plan, talk each step as e dey work, then give summary. Use dis one for migrations, implementations, or any multi-step process.

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

Chain-of-Thought prompting dey explicitly ask di model show how e reason, e dey improve accuracy for complex tasks. Di step-by-step breakdown dey help humans and AI understand di logic.

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about dis pattern:
> - "How I fit adapt di task execution pattern for long-running operations?"
> - "Wetin be best practice for structuring tool preambles for production app?"
> - "How I fit capture and show intermediate progress update for UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generating code wey production quality. Model go generate code wey follow production standard with proper error handling. Use dis when you dey build new features or services.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pcm/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterative improvement loop - generate, evaluate, find issues, improve, repeat*

**Structured Analysis** - For consistent evaluation. Model go review code using fixed framework (correctness, practices, performance, security, maintainability). Use dis for code reviews or quality check.

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
> - "How I fit make sure severity levels dey consistent across different review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework for consistent code reviews wit severity levels*

**Multi-Turn Chat** - For conversations wey need context. Model go remember previous messages and build on top dem. Use dis for interactive help session or complex Q&A.

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

*How conversation context dey gather over many turns until token limit reach*

**Step-by-Step Reasoning** - For wahala wey need visible logic. Model go show explicit reasoning for each step. Use dis one for math problems, logic puzzles, or when you need understand how e dey think.

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

*Breaking down problem into explicit logical steps*

**Constrained Output** - For answers wey get specific format requirements. Model go follow format and length rules well well. Use dis for summaries or when you want precise output structure.

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

*Forcing specific format, length, and structure rules*

## Using Existing Azure Resources

**Make sure deployment don correct:**

Check say `.env` file dey inside root directory wit Azure credentials (dis one create during Module 01):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di application:**

> **Note:** If you don start all applications using `./start-all.sh` for Module 01, dis module don dey run already for port 8083. You fit skip the start commands below and go directly to http://localhost:8083.
**Option 1: Using Spring Boot Dashboard (We Recommend for VS Code Users)**

Di dev container get di Spring Boot Dashboard extension, wey dey provide visual interface to manage all Spring Boot applications. You fit find am for di Activity Bar for di left side of VS Code (look for di Spring Boot icon).

From di Spring Boot Dashboard, you fit:
- See all di Spring Boot applications wey dey di workspace
- Start/stop applications with one click
- View application logs for real-time
- Monitor application status

Just click di play button wey dey beside "prompt-engineering" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From di root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root directory na
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

Both di scripts go automatically load environment variables from di root `.env` file and dem go build di JARs if dem no dey.

> **Note:** If you like build all modules manually before you start:
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
cd ..; .\stop-all.ps1  # All modules
```

## Application Screenshots

<img src="../../../translated_images/pcm/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Di main dashboard wey dey show all 8 prompt engineering patterns with their characteristics and how you fit use dem*

## Exploring di Patterns

Di web interface go allow you experiment with different prompting strategies. Each pattern dey solve different wahala - try dem make you see when e make sense to use each approach.

> **Note: Streaming vs Non-Streaming** — Every pattern page get two buttons: **🔴 Stream Response (Live)** and one **Non-streaming** option. Streaming dey use Server-Sent Events (SSE) to show tokens live as di model dey generate dem, so you fit see progress sharply. Di non-streaming option dey wait for di full response before e show am. For prompts wey need heavy reasoning (e.g., High Eagerness, Self-Reflecting Code), di non-streaming call fit take long time — sometimes minutes — without any visible feedback. **Use streaming when you dey test complex prompts** so you go fit see di model dey work and no go think say di request don time out.
>
> **Note: Browser Requirement** — Di streaming feature dey use di Fetch Streams API (`response.body.getReader()`) wey require full browser (Chrome, Edge, Firefox, Safari). E no dey work for VS Code built-in Simple Browser, because e webview no support ReadableStream API. If you dey use Simple Browser, di non-streaming buttons still go work normal — only di streaming buttons go get wahala. Open `http://localhost:8083` for external browser make you get full experience.

### Low vs High Eagerness

Ask simple question like "Wetin be 15% of 200?" with Low Eagerness. You go get quick, direct answer. Now ask something wey complex like "Design caching strategy for high-traffic API" with High Eagerness. Click **🔴 Stream Response (Live)** and watch di model reason well well, token by token. Di same model, di same question pattern - but di prompt dey tell am how much brain e go use.

### Task Execution (Tool Preambles)

Multi-step workflows go benefit if you plan first and talk about progress. Di model go talk wetin e go do, narrate each step, then summarize results.

### Self-Reflecting Code

Try "Create email validation service". Instead make e just generate code and stop, di model go generate, check against quality rules, find weaknesses, and improve. You go see am dey iterate till di code ready well well for production.

### Structured Analysis

Code reviews need stable ways to evaluate. Di model dey analyze code using fixed categories (correctness, practices, performance, security) with severity levels.

### Multi-Turn Chat

Ask "Wetin be Spring Boot?" then follow fast with "Show me example". Di model go remember your first question and give you example wey be Spring Boot specifically. Without memory, second question fit be too vague.

### Step-by-Step Reasoning

Pick math problem and try am with Step-by-Step Reasoning and Low Eagerness. Low eagerness go just give you answer - fast but e no clear. Step-by-step go show you each calculation and decision.

### Constrained Output

If you need specific formats or word counts, dis pattern go make sure you follow strictly. Try generate summary with exactly 100 words for bullet point format.

## Wetin You Dey Really Learn

**Reasoning Effort Dey Change Everything**

GPT-5.2 go allow you control computational effort with your prompts. Low effort mean fast responses with little exploration. High effort mean model go take time to reason deep. You dey learn how to match effort with task complexity - no waste time for simple questions, but no rush complex decisions too.

**Structure Dey Guide Behavior**

You see di XML tags for di prompts? Dem no be for decoration. Models dey follow structured instructions well pass freeform text. When you need multi-step processes or complex logic, structure go help model sabi where e dey and wetin go happen next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*How well-structured prompt be with clear sections and XML-style organisation*

**Quality Through Self-Evaluation**

Di self-reflecting patterns dey work by making quality criteria clear. Instead of hoping say model "go do am right", you talk wetin "right" mean: correct logic, error handling, performance, security. Di model fit then check its own output make e improve. E turn code generation from lottery to process.

**Context Get Limit**

Multi-turn conversations dey work by adding message history for each request. But e get limit - every model get max token count. As conversations dey grow, you go need ways to keep correct context without exceed di limit. Dis module show you how memory dey work; later you go learn when to summarize, when to forget, and when to retrieve.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg sabi say automated translation fit get mistake or no too correct. Di original document wey dem write for im own language na di correct one wey you suppose trust. If na important information, e better make professional human translation do am. We no go responsible for any misunderstanding or wrong interpretation wey fit happen from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->