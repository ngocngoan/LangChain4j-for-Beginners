# Module 02: Prompt Engineering with GPT-5.2

## Table of Contents

- [Wetyn You Go Learn](../../../02-prompt-engineering)
- [Wetyn You Need First](../../../02-prompt-engineering)
- [How Prompt Engineering Be](../../../02-prompt-engineering)
- [How Dis One Take Use LangChain4j](../../../02-prompt-engineering)
- [The Main Patterns](../../../02-prompt-engineering)
- [How To Take Use Existing Azure Resources](../../../02-prompt-engineering)
- [Application Screenshots](../../../02-prompt-engineering)
- [Exploring The Patterns](../../../02-prompt-engineering)
  - [Low vs High Eagerness](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [Self-Reflecting Code](../../../02-prompt-engineering)
  - [Structured Analysis](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Step-by-Step Reasoning](../../../02-prompt-engineering)
  - [Constrained Output](../../../02-prompt-engineering)
- [Wetyn You Really Dey Learn](../../../02-prompt-engineering)
- [Next Steps](../../../02-prompt-engineering)

## Wetyn You Go Learn

For the last module, you don see how memory dey enable conversational AI and how you fit use GitHub Models for basic interactions. Now, we go focus on how you dey ask questions - the prompts on their own - using Azure OpenAI GPT-5.2. How you take arrange your prompts go affect the quality of the answers wey you go get.

We dey use GPT-5.2 because e get reasoning control - you fit tell the model how much to think before e answer. Dis one dey make different ways of prompting clear and e go help you sabi when to use each approach. Plus, Azure get smaller rate limits for GPT-5.2 compared to GitHub Models, so we benefit.

## Wetyn You Need First

- You must don finish Module 01 (wey you don deploy Azure OpenAI resources)
- Get `.env` file for your root directory wey get your Azure credentials (e be like say `azd up` for Module 01 na im generate am)

> **Note:** If you never finish Module 01, abeg try check the deployment instructions for there first.

## How Prompt Engineering Be

Prompt engineering na how you dey design the input text so dat e go dey always give you the results you want. E no stop for just asking questions - na how you go set your request so the model go understand wetin you want and how to deliver am.

Make you reason am like you dey give beta instructions to your colleague. If you talk "Fix the bug," e dey vague. But if you talk "Fix the null pointer exception for UserService.java for line 45 by adding null check," e clear die. Language models na so e dey work too - specificity and structure matter well well.

## How Dis One Take Use LangChain4j

Dis module dey show advanced prompting patterns wey use the same LangChain4j base from before but e dey mainly focus on prompt structure and reasoning control.

<img src="../../../translated_images/pcm/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*How LangChain4j connect your prompts go Azure OpenAI GPT-5.2*

**Dependencies** - Module 02 dey use these langchain4j dependencies wey dem define for `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel Configuration** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

The chat model na manually configured Spring bean wey use OpenAI Official client, wey dey support Azure OpenAI endpoints. The main difference from Module 01 na how we dey arrange the prompts wey we send enter `chatModel.chat()`, e no be the model setup itself.

**System and User Messages** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j dey separate message types make e clear. `SystemMessage` na im dey set the AI behavior and context (like "You be code reviewer"), while `UserMessage` na the actual request. Dis one make e easy to keep AI behavior steady no matter how many different user questions you get.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/pcm/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage dey provide persistent context, UserMessages get their own requests*

**MessageWindowChatMemory for Multi-Turn** - For multi-turn conversation pattern, we still dey use `MessageWindowChatMemory` from Module 01. Every session get im own memory instance wey dem store inside `Map<String, ChatMemory>`, so e fit handle many chats at once without mixing context.

**Prompt Templates** - The main thing for here na prompt engineering, no be fresh LangChain4j APIs. Each pattern (low eagerness, high eagerness, task execution and so on) dey use the same `chatModel.chat(prompt)` method but with prompts wey dem carefully arrange. XML tags, instructions, and formatting na part of prompt text, no be LangChain4j feature.

**Reasoning Control** - GPT-5.2 dey control reasoning effort with instructions inside prompt like "maximum 2 reasoning steps" or "explore thoroughly". Na prompt engineering trick dem be, no be LangChain4j configuration. The library dey only deliver your prompts go the model.

The key point: LangChain4j na the infrastructure provider (model connection from [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memory, message handling from [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), while dis module dey teach how to build correct prompts inside dat system.

## The Main Patterns

No all wahala need same method. Some questions need quick answer, some need deep thinking. Some need visible reasoning, some only want results. Dis module cover eight prompting patterns - each one fit better for different situations. You go test all of dem to sabi when each approach work well.

<img src="../../../translated_images/pcm/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Overview of the eight prompt engineering patterns and their use cases*

<img src="../../../translated_images/pcm/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Low eagerness (fast, direct) vs High eagerness (thorough, exploratory) reasoning approaches*

**Low Eagerness (Quick & Focused)** - For simple questions where you want fast, direct answers. The model does minimal reasoning - maximum 2 steps. Use this for calculations, lookups, or straightforward questions.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore with GitHub Copilot:** Open [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) and ask:
> - "Wetin be difference between low eagerness and high eagerness prompting patterns?"
> - "How XML tags inside prompts dey help structure AI response?"
> - "When I suppose use self-reflection patterns vs direct instruction?"

**High Eagerness (Deep & Thorough)** - For complex problems where you need comprehensive analysis. The model go explore well and show detailed reasoning. Use this for system design, architecture decisions, or complex research.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (Step-by-Step Progress)** - For multi-step workflow. The model go give plan before e start, tell each step as e dey work, then give summary at the end. Use am for migrations, implementations, or any multi-step process.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting make model show how e reason, e dey improve accuracy for complex work. The step-by-step breakdown dey help both humans and AI understand the logic.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about this pattern:
> - "How I go adapt task execution pattern for long-run operations?"
> - "Wetin be best practice to arrange tool preambles for production applications?"
> - "How I fit capture and show intermediate progress update for UI?"

<img src="../../../translated_images/pcm/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Execute → Summarize workflow for multi-step tasks*

**Self-Reflecting Code** - For generating production-quality code. The model go generate code, check am against quality rules, then improve am step by step. Use this when you dey build new features or services.

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

*Iterative improvement loop - generate, evaluate, find issues, improve, repeat*

**Structured Analysis** - For consistent evaluation. The model go review code with fixed framework (correctness, practices, performance, security). Use this for code reviews or quality assessments.

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

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Ask about structured analysis:
> - "How I fit customize analysis framework for different types code reviews?"
> - "Wetin be best way to parse and act on structured output programmatically?"
> - "How I fit make sure severity levels remain consistent for different review sessions?"

<img src="../../../translated_images/pcm/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Four-category framework for consistent code reviews with severity levels*

**Multi-Turn Chat** - For conversations wey need context. The model go remember previous messages and build upon dem. Use this for interactive help sessions or complex Q&A.

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

*How conversation context dey accumulate for many turns until e reach token limit*

**Step-by-Step Reasoning** - For problems wey need visible logic. The model go show explicit reasoning for every step. Use this for maths problems, logic puzzles, or when you want understand thinking process.

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

**Constrained Output** - For responses with special format requirements. The model go follow format and length rules strictly. Use this for summaries or when you want precise output structure.

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

*Enforcing specific format, length, and structure requirements*

## How To Take Use Existing Azure Resources

**Make sure say dem deploy am:**

Check if `.env` file dey root directory with your Azure credentials (e be like say Module 01 generate am):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**How to start the application:**

> **Note:** If you don start all applications before with `./start-all.sh` for Module 01, this module dey already run for port 8083. You fit skip the start commands below, head straight to http://localhost:8083.

**Option 1: Use Spring Boot Dashboard (Better if you dey use VS Code)**

Inside the dev container, dem include Spring Boot Dashboard extension wey get visual interface to manage all Spring Boot applications. You fit find am for the Activity Bar for left side of VS Code (look out for Spring Boot icon).

From Spring Boot Dashboard you fit:
- See all the Spring Boot applications in your workspace
- Start/stop applications with one click
- View application logs for real-time
- Monitor the application status

Just click the play button beside "prompt-engineering" to start this module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Use shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root directory
.\start-all.ps1
```

Or start just this module:

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

Both scripts go automatically load environment variables from the root `.env` file and go build the JARs if dem never exist.

> **Note:** If you prefer to build all modules manually before starting:
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

Open your browser go http://localhost:8083.

**How to stop:**

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

*Main dashboard wey show all 8 prompt engineering patterns with their characteristics and use cases*

## Exploring The Patterns

The web interface go allow you to try different prompting strategies. Each pattern dey solve different kind problems - try all of dem to see when each one dey shine.

### Low vs High Eagerness

Ask simple question like "Wetin be 15% of 200?" using Low Eagerness. You go get answer quick and straightforward. Now ask something complex like "Design caching strategy for high-traffic API" using High Eagerness. Watch as the model go slow down and give detailed reasoning. Na same model, same question structure - but the prompt go tell am how much to think.
<img src="../../../translated_images/pcm/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Quick calculation wit small reasonin*

<img src="../../../translated_images/pcm/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Full caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

Multi-step workflows dey benefit from upfront plan and progress narration. Di model dey talk wetin e go do, narrate each step, den summarize results.

<img src="../../../translated_images/pcm/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Creating REST endpoint wit step-by-step narration (3.9MB)*

### Self-Reflecting Code

Try "Create an email validation service". Instead make e just generate code and stop, di model go generate, check am against quality criteria, find weak points, then improve am. You go see am dey iterate until di code reach production standards.

<img src="../../../translated_images/pcm/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Complete email validation service (5.2MB)*

### Structured Analysis

Code reviews need constant evaluation frameworks. Di model dey analyze code using fixed categories (correctness, practices, performance, security) with severity levels.

<img src="../../../translated_images/pcm/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Framework-based code review*

### Multi-Turn Chat

Ask "Wetyn be Spring Boot?" then sharply follow-up with "Show me example". Di model go remember your first question and show you Spring Boot example specially. Without memory, dat second question go too vague.

<img src="../../../translated_images/pcm/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Context preservation across questions*

### Step-by-Step Reasoning

Pick math problem and try am wit both Step-by-Step Reasoning and Low Eagerness. Low eagerness just give you answer - fast but no clarity. Step-by-step show you every calculation and decision.

<img src="../../../translated_images/pcm/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Math problem wit clear steps*

### Constrained Output

When you need specific formats or word count, dis pattern force strict adherence. Try generate summary wit exactly 100 words in bullet point format.

<img src="../../../translated_images/pcm/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Machine learning summary wit format control*

## Wetin You Really Dey Learn

**Reasoning Effort Changes Everything**

GPT-5.2 make you control how you wan put effort for computational work through your prompts. Low effort mean fast response wit small checking. High effort mean di model go take time to reason deep. You dey learn how to match effort to how complex di work be - no waste time for simple question, but no rush complex decisions too.

**Structure Guides Behavior**

You see di XML tags for di prompts? Them no be just for show. Models dey follow structured instructions better pass freeform text. When you need multi-step process or complex logic, structure help di model know wetin e dey do and wetin go come next.

<img src="../../../translated_images/pcm/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*How well-structured prompt take be, wit clear sections and XML-style organization*

**Quality Through Self-Evaluation**

Di self-reflecting patterns dey work by making quality criteria clear. Instead make you hope say di model "go do am right", you dey talk am exactly wetin "right" mean: correct logic, error handling, performance, security. Di model fit check its own output and improve. This one make code generation no be lottery again, na proper process.

**Context Is Finite**

Multi-turn conversations dey work by including message history wit every request. But e get limit - every model get max token count. As conversations dey grow, you go need strategies to keep relevant context without reach dat limit. Dis module go show you how memory dey work; later you go learn when to summarize, when to forget, and when to bring back.

## Next Steps

**Next Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg sabi say automated translation fit get mistakes or no too correct. Di original document wey original language na di correct one. If na important info, e good make professional human translator do am. We no go take responsibility for any misunderstanding or wrong interpretation wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->