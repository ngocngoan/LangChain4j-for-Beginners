# Module 02: GPT-5.2 తో ప్రాంప్ట్ ఇంజనీరింగ్

## విషయాలు సూచిక

- [మీరు నేర్చుకునే విషయాలు](../../../02-prompt-engineering)
- [పూర్వపాఠ్యాలు](../../../02-prompt-engineering)
- [ప్రాంప్ట్ ఇంజనీరింగ్ అర్థం చేసుకోవడం](../../../02-prompt-engineering)
- [ఈ విధంగా LangChain4j ఎలా వాడుతుందో](../../../02-prompt-engineering)
- [ప్రధాన ప్యాటర్న్‌లు](../../../02-prompt-engineering)
- [ఇప్పటికే ఉన్న Azure వనరులను ఉపయోగించడం](../../../02-prompt-engineering)
- [యాప్లికేషన్ స్క్రీన్‌షాట్‌లు](../../../02-prompt-engineering)
- [ప్యాటర్న్‌ల అన్వేషణ](../../../02-prompt-engineering)
  - [తక్కువ శీఘ్రత vs ఎక్కువ శీఘ్రత](../../../02-prompt-engineering)
  - [టాస్క్ ఎగ్జిక్యూషన్ (టూల్ ప్రీాంబుల్స్)](../../../02-prompt-engineering)
  - [స్వయం-పరిశీలన కోడ్](../../../02-prompt-engineering)
  - [సంస్థిత విశ్లేషణ](../../../02-prompt-engineering)
  - [బహుళ-తిరుగుబాటు చాట్](../../../02-prompt-engineering)
  - [దశలవారీ తర్కం](../../../02-prompt-engineering)
  - [నియంత్రిత అవుట్పుట్](../../../02-prompt-engineering)
- [మీరు నిజంగా ఏమి నేర్చుకుంటున్నారు](../../../02-prompt-engineering)
- [తరవాతి దశలు](../../../02-prompt-engineering)

## మీరు నేర్చుకునే విషయాలు

గత మాడ్యూల్‌లో, మీరు మెమరీ సంభాషణాత్మక AIని ఎలా సాధ్యం చేస్తుంది మరియు ప్రాథమిక అంతరదృశ్యాల కోసం GitHub Models ను వాడటం చూశారు. ఇప్పుడు మనం ప్రశ్నలు ఎలా అడగాలో — ప్రాంప్ట్‌లను ఎలా రూపొందించాలో Azure OpenAI యొక్క GPT-5.2 ఉపయోగించి తెలుసుకోబోతున్నాం. మీరు ప్రాంప్ట్‌లను ఎలా నిర్మించారో మీకు వచ్చే ప్రతిస్పందనల నాణ్యతను తీవ్రంగా ప్రభావితం చేస్తుంది.

మనం GPT-5.2ని ఉపయోగిస్తాం ఎందుకంటే ఇది తర్క నియంత్రణను పరిచయం చేస్తుంది — మీరు మోడల్ కు సమాధానం ఇచ్చే ముందు ఎంత ఆలోచించాలో చెప్తారు. ఇది విభిన్న ప్రాంప్టింగ్ వ్యూహాలను స్పష్టంగా చేస్తుంది మరియు ప్రతి ఒక దాన్ని ఎప్పుడు ఉపయోగించాలో మీకు అర్థం చేసుకోవచ్చు. GitHub Models తో పోలిస్తే GPT-5.2 కోసం Azure లో తక్కువ రేటు పరిమితులు అందుబాటులో ఉంటాయి.

## పూర్వపాఠ్యాలు

- మాడ్యూల్ 01 పూర్తి చేసుకోవడం (Azure OpenAI వనరులు ఏర్పాటు చేయబడినవి)
- రూట్ డైరక్టరీలో `.env` ఫైల్ (Module 01లో `azd up` ద్వారా సృష్టించబడింది)

> **గమనిక:** మీరు Module 01 ని పూర్తి చేయని ఉంటే, ముందు అక్కడి అమరికలను అనుసరించండి.

## ప్రాంప్ట్ ఇంజనీరింగ్ అర్థం చేసుకోవడం

ప్రాంప్ట్ ఇంజనీరింగ్ అనేది మీరు కావలసిన ఫలితాలను నిరంతరం పొందేందుకు ఇన్‌పుట్ టెక్స్ట్ ను డిజైన్ చేయడమే. ఇది కేవలం ప్రశ్నలు అడగడమే కాదండి — ఇది అభ్యర్థనలను ఇంతగా నిర్మించడమే, తద్వారా మోడల్ మీకు ఏంటో మరియు ఎలా అందించాలో సరిగ్గా అర్థం చేసుకోగలుగుతుంది.

దీనిని మీరు సహోద్యోగికి సూచనలు ఇవ్వడం వంటి భావించండి. "బగ్‌ని సరిచేయండి" అంటే అస్పష్టమై ఉంటుంది. "UserService.java లైన్ 45 లో null pointer exception సరిచేయడానికి null చెక్ చేర్చండి" అంటే స్పష్టంగా ఉంటుంది. భాషా మోడళ్లు ఒకే విధంగా పనిచేస్తాయి — స్పష్టత్వం మరియు నిర్మాణం ముఖ్యమయినవి.

## ఈ విధంగా LangChain4j ఎలా వాడుతుందో

ఈ మాడ్యూల్ ముందటి మాడ్యూల్స్ నుండే అదే LangChain4j బేస్ ఉపయోగించి అభివృద్ధి చెందిన ప్రాంప్టింగ్ ప్యాటర్న్‌లను చూపిస్తుంది, ప్రాంప్ట్ నిర్మాణం మరియు తర్క నియంత్రణ పై దృష్టి పెట్టి.

<img src="../../../translated_images/te/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j మీ ప్రాంప్ట్‌లను Azure OpenAI GPT-5.2 కి ఎలా కనెక్ట్ చేస్తుందో*

**అవసరాలు** - Module 02 కింది langchain4j ఆధారాలు `pom.xml`లో నిర్వచించబడినవి వాడుతుంది:
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

**OpenAiOfficialChatModel అమరిక** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

చాట్ మోడల్‌ను మాన్యువల్ గా స్ప్రింగ్ బీన్ గా ఆమోదం చేస్తారు, ఇది Azure OpenAI ఎండ్‌పాయింట్‌లకు మద్దతు ఇస్తుంది. Module 01 తో తేడా ఏమిటంటే, మోడల్ సెటప్ కాదు కానీ `chatModel.chat()`కు పంపే ప్రాంప్ట్‌లను ఎలా నిర్మించాలో.

**సిస్టమ్ మరియు యూజర్ మెసేజ్‌లు** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j మెసేజ్ రకాలను స్పష్టం చేస్తుంది. `SystemMessage` AI ప్రవర్తన మరియు సందర్భాన్ని (ఉదా. "మీరు కోడ్ రివ్యూయర్") సెట్ చేస్తుంది, `UserMessage` అసలు అభ్యర్థన వుంటుంది. ఈ విడగొట్టడం వలన యూజర్ వివిధ ప్రశ్నలతో కూడ ప్రతి సందర్భంలో ఒకటేసారి AI ప్రవర్తన స్థిరంగా ఉంచవచ్చు.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/te/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage శాశ్వత సందర్భాన్ని ఇస్తుంది, UserMessages వ్యక్తిగత అభ్యర్థనలను కలిగి ఉంటాయి*

**Multi-Turn కోసం MessageWindowChatMemory** - బహుళ-తిరుగుబాటు సంభాషణ ప్యాటర్న్ కోసం, మనము Module 01 నుండి `MessageWindowChatMemory` పునఃఉపయోగిస్తున్నాము. ప్రతి సెషన్ కి అదనపు మెమరీ ఇన్స్టెన్స్ ఉంటుంది, అది `Map<String, ChatMemory>` లో నిల్వ అవుతుంది, తద్వారా అనేక సంభాషణలు ఒకదానితో మిక్స్ లేకుండా కొనసాగుతాయి.

**ప్రాంప్ట్ టెంప్లేట్లు** - ఇక్కడ నిజమైన దృష్టి ప్రాంప్ట్ ఇంజనీరింగ్. కొత్త LangChain4j API లు కాదు. ప్రతి ప్యాటర్న్ (తక్కువ, ఎక్కువ శీఘ్రత, టాస్క్ ఎగ్జిక్యూషన్ etc) ఒకే విధంగా `chatModel.chat(prompt)` ని వాడుతుంది కానీ చాలా జాగ్రత్తగా నిర్మించిన ప్రాంప్ట్ స్ట్రింగ్స్ తో. XML ట్యాగ్లు, సూచనలు, ఫార్మాటింగ్ అన్నీ ప్రాంప్ట్ టెక్స్ట్ లో భాగముగా ఉంటాయి కానీ LangChain4j ప్రత్యేకత కాదు.

**తర్క నియంత్రణ** - GPT-5.2 యొక్క తర్క శ్రమను ప్రాంప్ట్ సూచనల ద్వారా నియంత్రిస్తారు, ఉదా: "గరిష్టం 2 తర్క దశలు" లేదా "వివరంగా పరిశీలించు". ఇవి ప్రాంప్ట్ ఇంజనీరింగ్ సాంకేతికతలు, LangChain4j సెట్టింగులు కాదు. లైబ్రరీ మీ ప్రాంప్ట్‌లను మోడల్ కు అందిస్తుంది.

ప్రధాన విషయం: LangChain4j మోడల్ కనెక్షన్ ([LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)), మెమరీ, మెసేజ్ నిర్వహణ ([Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)) వంటి మౌలిక సదుపాయాల్ని అందిస్తుంది, ఈ మాడ్యూల్ ఆ ఇన్ఫ్రాస్ట్రక్చర్ లో సమర్థవంతమైన ప్రాంప్ట్‌లు ఎలా రూపొందించాలో నేర్పిస్తుంది.

## ప్రధాన ప్యాటర్న్‌లు

అన్ని సమస్యలకు ఒకే విధానము అవసరం కాదు. కొన్ని ప్రశ్నలకు వెంటనే సమాధానం కావాలి, మరికొన్నింటికి లోతైన ఆలోచన. కొన్నింటికి స్పష్టమైన తర్కం కావాలి, మరికొన్నింటికి ఫలితమే కావాలి. ఈ మాడ్యూల్ ఎనిమిది ప్రాంప్టింగ్ ప్యాట్‌ర్న్లను కవర్ చేస్తుంది – ప్రతి ఒక్కదీ భిన్న సందర్భాలకు ఉద్దేశించబడ్డవి. మీరు వాటిని అందరిని ప్రయోగించి ఏ సమయంలో ఏది ఉత్తమమో తెలుసుకుంటారు.

<img src="../../../translated_images/te/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ఎనిమిది ప్రాంప్ట్ ఇంజనీరింగ్ ప్యాటర్న్లు మరియు వాటి వినియోగ సందర్భాలు*

<img src="../../../translated_images/te/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*తక్కువ శీఘ్రత (వేగమైన, ప్రత్యక్ష) vs ఎక్కువ శీఘ్రత (వివరణాత్మక, అన్వేషణాత్మక) తర్క పద్ధతులు*

**తక్కువ శీఘ్రత (తక్షణం & కేంద్రీకృతం)** - సులభమైన ప్రశ్నల కోసం, మీరు వేగంగా ప్రత్యక్ష సమాధానాలు కోరేటప్పుడు. మోడల్ తక్కువగా తర్కిస్తుంది — గరిష్టం 2 దశలు. గణన, లుకప్ లేదా ప్రత్యక్ష ప్రశ్నలకు వాడండి.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot తో అన్వేషించండి:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) తెరవండి మరియు అడగండి:
> - "తక్కువ శీఘ్రత మరియు ఎక్కువ శీఘ్రత ప్రాంప్టింగ్ ప్యాటర్న్ల మధ్య వ్యత్యాసం ఏమిటి?"
> - "ప్రాంప్ట్‌లలో XML ట్యాగ్లు AI ప్రతిస్పందన నిర్మాణానికి ఎలా సహాయపడతాయి?"
> - "నేను ఎప్పుడు స్వయం-పరిశీలన ప్యాటర్న్లు ఉపయోగించాలి లేదా ప్రత్యక్ష మార్గదర్శకం వాడాలి?"

**ఎక్కువ శీఘ్రత (లోతైన & సమగ్ర)** - సంక్లిష్ట సమస్యలకు, మీరు సమగ్ర విశ్లేషణ కావాలనుకుంటే. మోడల్ లోతుగా అన్వేషించి వివరమైన తర్కాన్ని చూపుతుంది. సిస్టమ్ డిజైన్, నిర్మాణ నిర్ణయాలు, లేదా క్లిష్ట పరిశోధనలకు వాడండి.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**టాస్క్ ఎగ్జిక్యూషన్ (దశలవారీ ప్రగతి)** - బహుళ దశల వర్క్‌ఫ్లోల కోసం. మోడల్ ముందస్తుగా ఒక ప్రణాళికను ఇస్తుంది, ప్రతి దశను వివరిస్తూ పని చేస్తుంది, చివరికి సమ్మరీ ఇస్తుంది. మైగ్రేషన్లు, అమలు, లేదా ఏ multi-step ప్రాసెస్ కోసం వాడండి.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting మోడల్ తర్క ప్రక్రియని చూపించమని స్పష్టంగా అడుగుతుంది, క్లిష్ట పనుల కోసం ఖచ్చితత్వాన్ని పెంచుతుంది. దశలవారీ బ్రేక్‌డౌన్ తో మానవులు మరియు AI ఇద్దరూ తర్కాన్ని మెరుగ్గా అర్థం చేసుకుంటారు.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat తో ప్రయత్నించండి:** ఈ ప్యాటర్న్ గురించి అడగండి:
> - "నిరంతరంగా నడిచే ఆపరేషన్ల కోసం టాస్క్ ఎగ్జిక్యూషన్ ప్యాటర్న్ ఎలా సరిపోయేలా చేస్తాను?"
> - "ఉత్పత్తి యాప్లలో టూల్ ప్రీాంబుల్స్ నిర్మాణానికి ఉత్తమ పద్ధతులు ఏమిటి?"
> - "ఇంటర్మీడియట్ ప్రగతి అప్‌డేట్స్ UIలో ఎలా క్యాప్చర్ చేసి ప్రదర్శిస్తారు?"

<img src="../../../translated_images/te/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*బహుళ-దశల పనుల కోసం ప్రణాళిక → అమలు → సమ్మరీ వర్క్‌ఫ్లో*

**స్వయం-పరిశీలన కోడ్** - ఉత్పత్తి నాణ్యత కోడ్ సృష్టించడానికి. మోడల్ కోడ్ తయారు చేసి, నాణ్యత ప్రమాణాలతో తనిఖీ చేసి, మెరుగుపరుస్తుంది. కొత్త ఫీచర్లు లేదా సర్వీసులు నిర్మించే సమయంలో వాడండి.

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

<img src="../../../translated_images/te/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*పునరావృత మెరుగుదల చక్రం – సృష్టించండి, మూల్యాంకనం చేయండి, సమస్యలు గుర్తించండి, మెరుగుపరచండి, పునరావృతం*

**సంస్థిత విశ్లేషణ** - సुस్పష్టం మైన మೌಲ్యాంకనం కోసం. మోడల్ ఒక స్థిరమైన ఫ్రేమ్‌వర్క్ (సరైనదైన, ఆచారాలు, పనితీరు, భద్రత) ఉపయోగించి కోడ్ సమీక్ష చేస్తుంది. కోడ్ రివ్యూలు లేదా నాణ్యత అంచనాలకు వాడండి.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat తో ప్రయత్నించండి:** సంస్థిత విశ్లేషణ గురించి అడగండి:
> - "వివిధ రకాల కోడ్ రివ్యూలకు విశ్లేషణ ఫ్రేమ్‌వర్క్ ని ఎలా అనుకూలీకరించగలను?"
> - "సంస్థిత అవుట్పుట్‌ను ప్రోగ్రామాటిక్‌గా పార్స్ చేసి చర్యలు తీసుకోవడం ఉత్తమ విధానం ఏమిటి?"
> - "విభిన్న రివ్యూలలో స్థిరమైన తీవ్రత స్థాయిలను ఎలా నిర్ధారించాలి?"

<img src="../../../translated_images/te/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*సంస్థిత కోడ్ రివ్యూల కోసం నాలుగు-విభాగాల ఫ్రేమ్‌వర్క్ మరియు తీవ్రత స్ధాయిలు*

**బహుళ-తిరుగుబాటు చాట్** - సందర్భాన్ని అవసరం చేసుకునే సంభాషణల కోసం. మోడల్ మునుపటి సందేశాలను గుర్తుంచుకొని వాటిని ఆధారంగా చేస్తుంది. ఇంటరాక్టివ్ సహాయ సెషన్‌లు లేదా క్లిష్ట ప్రశ్నలు - సమాధానాలకు వాడండి.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/te/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*సంధర్భం మెమరీ టోకెన్ పరిమితి చేరే వరకు బహుళ తురుగుబాట్లు గడిచే విధంగా క్రమం పూర్వకంగా నిల్వ అవుతుంది*

**దశలవారీ తర్కం** - స్పష్టమైన తర్కం కావలసిన సమస్యలకు. మోడల్ ప్రతి దశకు సందిగ్ధత లేకుండా తర్కాన్ని చూపిస్తుంది. గణితం సమస్యలు, తర్క ముద్రలు లేదా ఆలోచన ప్రక్రియ అర్థం చేసుకోవడానికి ఉపయోగించండి.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/te/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*సమస్యలను స్పష్టమైన తర్క దశలుగా విభజించడం*

**నియంత్రిత అవుట్పుట్** - నిర్దిష్ట ఫార్మాట్ అవసరమున్న ప్రతిస్పందనలకు. మోడల్ ఫార్మాట్ మరియు పరిమాణ నియమాలను కఠినంగా అనుసరిస్తుంది. సారాంశాలు లేదా ఖచ్చితమైన అవుట్పుట్ నిర్మాణం అవసరమైతే వాడండి.

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

<img src="../../../translated_images/te/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*నిర్దిష్ట ఫార్మాట్, పరిమాణం, మరియు నిర్మాణ నియమాల పాటింపు*

## ఇప్పటికే ఉన్న Azure వనరులను ఉపయోగించడం

**అమరింత ధృవీకరించండి:**

మాడ్యూల్ 01లో సృష్టించినట్లుగా రూట్ డైరెక్టరీలో `.env` ఫైల్ Azure క్రెడెన్షియల్స్ తో ఉండాలి:
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT చూపించాలి
```

**యాప్లికేషన్ ప్రారంభించండి:**

> **గమనిక:** మీరు ఇప్పటికే Module 01 నుండి `./start-all.sh` ని ఉపయోగించి అన్ని యాప్లికేషన్లను ప్రారంభించి ఉంటే, ఈ మాడ్యూల్ 8083 పోర్ట్‌పై ఇప్పటికే నడుస్తోంది. కాబట్టి క్రింది ప్రారంభ ఆదేశాలను తప్పించుకోవచ్చు మరియు నేరుగా http://localhost:8083కి వెళ్ళవచ్చు.

**ఎంపిక 1: Spring Boot Dashboard ఉపయోగించడం (VS Code వాడేవారు కోసం సిఫార్సు)**

డేవ్ కంటైనర్‌లో Spring Boot Dashboard ఎక్స్‌టెన్షన్ ఉంది, ఇది అన్ని Spring Boot అప్లికేషన్లను నిర్వహించడానికి గ్రాఫికల్ ఇంటర్ఫేస్ ఇస్తుంది. VS Code ఎడమ వైపు Activity Barలో Spring Boot చిహ్నం చూడండి.

Spring Boot డాష్‌బోర్డు నుండి మీరు:
- వర్క్‌స్పేస్‌లో అందుబాటులో ఉన్న అన్ని Spring Boot అప్లికేషన్లను చూడవచ్చు
- ఒక క్లిక్ తో యాప్లికేషన్లను ఆరంభించండి/ఆపు
- యాప్లికేషన్ లాగ్‌లు రియల్-టైమ్ లో వీక్షించండి
- యాప్లికేషన్ స్థితిని మానిటర్ చేసుకోండి

"prompt-engineering" పక్కన ఉన్న ప్లే బటన్ క్లిక్ చేసి ఈ మాడ్యూల్ ప్రారంభించండి, లేదా అన్ని మాడ్యూల్స్ ని ఒకేసారి ప్రారంభించండి.

<img src="../../../translated_images/te/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ఎంపిక 2: shell స్క్రిప్ట్‌ల ఉపయోగం**

అన్ని వెబ్ యాప్లికేషన్లను (మాడ్యూల్స్ 01-04):
 
**Bash:**
```bash
cd ..  # రూట్ డైరెక్టరీ నుండి
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # రూట్ డైరెక్టరీ నుండి
.\start-all.ps1
```

లేదా కేవలం ఈ మాడ్యూల్ ప్రారంభించండి:

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

రెండు స్క్రిప్ట్‌లు కూడ రూట్ `.env` నుండి పర్యావరణ మాడల్స్‌ను ఆటోమేటిగ్గా లోడ్ చేస్తాయి మరియు JAR ఫైళ్ళు లేవనైతే నిర్మిస్తాయి.

> **గమనిక:** మీరు ప్రారంభించే ముందు అన్ని మాడ్యూల్స్‌ను మాన్యువల్ గా నిర్మించాలనుకుంటే:
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

మీ బ్రౌజర్‌లో http://localhost:8083 తెరవండి.

**ఆపు కోసం:**

**Bash:**
```bash
./stop.sh  # ఈ మాడ్యూల్ మాత్రమే
# లేదా
cd .. && ./stop-all.sh  # అన్ని మాడ్యూల్స్
```

**PowerShell:**
```powershell
.\stop.ps1  # ఈ మాడ్యూల్ మాత్రమే
# లేదా
cd ..; .\stop-all.ps1  # అన్ని మాడ్యూల్స్
```

## యాప్లికేషన్ స్క్రీన్‌షాట్‌లు

<img src="../../../translated_images/te/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ప్రధాన డాష్‌బోర్డు, అన్ని 8 ప్రాంప్ట్ ఇంజనీరింగ్ ప్యాటర్న్లను వారి లక్షణాలు మరియు వినియోగ సందర్భాలతో చూపిస్తుంది*

## ప్యాటర్న్‌ల అన్వేషణ

వెబ్ ఇంటర్‌ఫేస్ మీకు వివిధ ప్రాంప్టింగ్ వ్యూహాలతో ప్రయోగించడానికి అనుమతిస్తుంది. ప్రతి ప్యాటర్న్ వేరే సమస్యలను పరిష్కరిస్తుంది - వాటిని ఉపయోగించి ఏ సమయంలో ఏది ఉత్తమమో ఇక్కడ తెలుసుకోండి.

### తక్కువ శీఘ్రత vs ఎక్కువ శీఘ్రత

"200 లో 15% ఎంత?" లాంటి సులభమైన ప్రశ్నను తక్కువ శీఘ్రతతో అడగండి. మీరు తక్షణం, ప్రత్యక్ష సమాధానం పొందుతారు. ఇప్పుడు "ఉన్నత-ట్రాఫిక్ APIకి caching వ్యూహం డిజైన్ చేయండి" లాంటి క్లిష్ట ప్రశ్నను ఎక్కువ శీఘ్రతతో అడగండి. మోడల్ ఎలా నెమ్మదిగా చొరవ పెట్టి, వివరణాత్మక తర్కాన్ని ఇస్తుందో గమనించండి. అదే మోడల్, అదే ప్రశ్న నిర్మాణం — కానీ ప్రాంప్ట్ దానికి ఎంత ఆలోచించాలో చెప్పింది.
<img src="../../../translated_images/te/low-eagerness-demo.898894591fb23aa0.webp" alt="కనిష్ట ఉత్సాహం డెమో" width="800"/>

*తక్కువ కారణీకరణతో వేగవంతమైన లెక్కింపు*

<img src="../../../translated_images/te/high-eagerness-demo.4ac93e7786c5a376.webp" alt="అత్యధిక ఉత్సాహం డెమో" width="800"/>

*సంపూర్ణ కాష్ విధానం (2.8MB)*

### పనితీరు అమలు (సాధన పూర్వప్రారంభాలు)

బహుళ దశల వర్క్‌ఫ్లోలు ముందస్తు ప్రణాళిక మరియు అభివృద్ధి వివరణతో లాభపడతాయి. మోడల్ ఎతను చేస్తుందో వివరణ ఇస్తూ ప్రతి దశను పేర్కొంటుంది, తదుపరి ఫలితాల సమ్మేళనం చేస్తుంది.

<img src="../../../translated_images/te/tool-preambles-demo.3ca4881e417f2e28.webp" alt="పని అమలు డెమో" width="800"/>

*దశల వారీగా వివరణతో REST ఎండ్‌పాయింట్ సృష్టించడం (3.9MB)*

### స్వీయ పరామర్శ కోడ్

"ఇమెయిల్ ధృవీకరణ సేవను సృష్టించు" అని ప్రయత్నించండి. కేవలం కోడ్ రూపొందించి ఆగకుండా, మోడల్ ఉత్పత్తి నాణ్యత ప్రమాణాలపై మూల్యాంకనం చేసి లోపాలను గుర్తించి మెరుగుపరుస్తుంది. చివరికి ప్రొడక్షన్ ప్రమాణాలకు సరిపోయేలా పునరావృతం చూడవచ్చు.

<img src="../../../translated_images/te/self-reflecting-code-demo.851ee05c988e743f.webp" alt="స్వీయ పరామర్శ కోడ్ డెమో" width="800"/>

*సంపూర్ణ ఇమెయిల్ ధృవీకరణ సేవ (5.2MB)*

### క్రమీకృత విశ్లేషణ

కోడ్ సమీక్షలకు స్థిరమైన మూల్యాంకన ఫ్రేమ్‌వర్క్‌లు అవసరం. మోడల్ ఖచ్చితత, పద్ధతులు, పనితనం, భద్రత వంటి వర్గాలపరంగా కోడ్‌ను విశ్లేషిస్తుంది, తీవ్రత స్థాయిలతో.

<img src="../../../translated_images/te/structured-analysis-demo.9ef892194cd23bc8.webp" alt="క్రమీకృత విశ్లేషణ డెమో" width="800"/>

*ఫ్రేమ్‌వర్క్ ఆధారిత కోడ్ సమీక్ష*

### బహుళ తరం చాట్

"స్ప్రింగ్ బూట్ అంటే ఏమిటి?" అని అడిగి వెంటనే "ఒక ఉదాహరణ చూపించు" అని అడగండి. మొదటి ప్రశ్నను మోడల్ గుర్తుంచుకుంటుంది మరియు స్ప్రింగ్ బూట్ ఉదాహరణ ఇస్తుంది. జ్ఞాపకం లేకుంటే, రెండో ప్రశ్న చాలా సాధారణంగా ఉంటుంది.

<img src="../../../translated_images/te/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="బహుళ తరం చాట్ డెమో" width="800"/>

*ప్రశ్నల మధ్య సందర్భ పరిరక్షణ*

### దశల వారీ కారణీకరణ

గణిత సమస్య ఎంచుకుని దశల వారీ కారణీకరణ మరియు కనిష్ట ఉత్సాహం రెండింటినీ ఉపయోగించి ప్రయత్నించండి. కనిష్ట ఉత్సాహం కేవలం ఫలితం ఇస్తుంది - వేగంగా కానీ ఆమేధంగా. దశల వారీ విధానం ప్రతి లెక్కింపును మరియు నిర్ణయాన్ని చూపిస్తుంది.

<img src="../../../translated_images/te/step-by-step-reasoning-demo.12139513356faecd.webp" alt="దశల వారీ కారణీకరణ డెమో" width="800"/>

*స్పష్ట దశలతో గణితం సమస్య*

### ఆంకితమైన అవుట్పుట్

ఖచ్చితమైన ఆకృతి లేదా పద సంఖ్య అవసరమైనపుడు ఈ నమూనా కఠినంగా పాటిస్తుంది. బుల్లెట్ పాయింట్లలో ఖచ్చితంగా 100 పదాల సారాంశం రూపొందించండి.

<img src="../../../translated_images/te/constrained-output-demo.567cc45b75da1633.webp" alt="ఆంకితమైన అవుట్పుట్ డెమో" width="800"/>

*రూపరేఖ నియంత్రణతో మెషీన్ లెర్నింగ్ సారాంశం*

## మీరు నిజంగా నేర్చుకుంటున్నది

**కారణీగరణ శ్రమ ప్రతిదిని మారుస్తుంది**

GPT-5.2 ద్వారా మీరు మీ ప్రాంప్ట్స్ ద్వారా గణన Effort ను నియంత్రించవచ్చు. తక్కువ శ్రమ అంటే వేగవంతమైన ప్రతిస్పందనలు, తక్కువ అన్వేషణతో. అధిక శ్రమ అంటే మోడల్ లోతుగా ఆలోచించడానికి సమయం తీసుకుంటుంది. మీరు పని సంక్లిష్టతకు సరిపోయే శ్రమను నేర్చుకుంటున్నారు - సులభ ప్రశ్నలపై సమయం వృథా చేయకండి, కానీ క్లిష్ట నిర్ణయాలను కూడా త్వరగా చేయొద్దు.

**సంరచన ప్రవర్తనని మార్గనిర్దేశిస్తుంది**

ప్రాంప్ట్‌లలో XML ట్యాగ్లు గమనించారా? అవి అలంకరణ కాదు. మోడల్స్ స్వేచ్ఛాత్మక టెక్స్ట్ కంటే నిర్మిత సూచనలను విశ్వసనీయంగా అనుసరిస్తాయి. బహుళ దశల ప్రక్రియలు లేదా క్లిష్ట తర్కాలకు, నిర్మాణం మోడల్ ఎక్కడ ఉందో మరియు తర్వాత ఏమి చేస్తుందో ట్రాక్ చేయడంలో సహాయపడుతుంది.

<img src="../../../translated_images/te/prompt-structure.a77763d63f4e2f89.webp" alt="ప్రాంప్ట్ నిర్మాణం" width="800"/>

*స్పష్ట విడతలు మరియు XML శైలి వ్యవస్థతో బాగా నిర్మిత ప్రాంప్ట్ శరీరం*

**గుణమానం స్వీయ-మూల్యాంకనం ద్వారా**

స్వీయ-పరామర్శ నమూనాలు నాణ్యత ప్రమాణాలను స్పష్టంగా చేయడం ద్వారా పనిచేస్తాయి. "సరైనది" అంటే ఏమిటి అనేది మోడల్‌కు మీరు చెప్పడం ద్వారా అవుట్పుట్‌ను స్వయంగా మూల్యాంకనం చేసి మెరుగుపరుస్తుంది. ఇది కోడ్ సృష్టిని ఓ లాటరీ నుండి ఒక సులభ గతి ప్రక్రియగా మార్చుతుంది.

**సందర్భం పరిమితమే**

బహుళ తరం సంభాషణలు ప్రతి అభ్యర్ధనతో సందేశ చరిత్రను కలిపి పనిచేస్తాయి. కానీ పరిమితి ఉంది - ప్రతి మోడల్‌కు గరిష్ట టోకెన్ సంఖ్య ఉంటుంది. సంభాషణలు పెరిగేటప్పుడు, సంబంధిత సందర్భాన్ని ఉంచేందుకు వ్యూహాలు అవసరం, అతి గరిష్టాన్ని తాకవద్దు. ఈ మాడ్యూల్‌లో మీరు జ్ఞాపకం ఎలా పనిచేస్తుందో నేర్చుకుంటారు; తరువాత 언제 సారాంశం చేయాలో, ఎప్పుడు మరవాలో, ఎప్పుడు తిరిగి తెచ్చుకోవాలో తెలుసుకుంటారు.

## తరువాతి దశలు

**తర్వాతి మాడ్యూల్:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**నావిగేషన్:** [← మునుపటి: మాడ్యూల్ 01 - పరిచయం](../01-introduction/README.md) | [ప్రధానానికి వెళ్ళు](../README.md) | [తర్వాత: మాడ్యూల్ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టత**:  
ఈ డాక్యుమెంట్ ను AI అనువాద సేవ అయిన [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము సరిగా అనువదించడానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలలో పొరబాట్లు లేదా అసంపూర్ణతలు ఉన్నట్టుండవచ్చు. మూల భాషలో ఉన్న అసలు డాక్యుమెంట్ ను అధికారిక మూలంగా పరిగణించాలి. ముఖ్యమైన సమాచారానికి, వృత్తిపరమైన మానవ అనువాదాన్ని సూచించబడుతుంది. ఈ అనువాదం ఉపయోగించడంలో జరిగే వేటింతగావేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->