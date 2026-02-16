# మాడ్యూల్ 02: GPT-5.2 తో ప్రాంప్ట్ ఇంజనీరింగ్

## సూచిక

- [మీరు నేర్చుకునేదేంటి](../../../02-prompt-engineering)
- [అవసర పరిస్థితులు](../../../02-prompt-engineering)
- [ప్రాంప్ట్ ఇంజనీరింగ్ అర్థం చేసుకోవడం](../../../02-prompt-engineering)
- [ప్రాంప్ట్ ఇంజనీరింగ్ మౌలికాలు](../../../02-prompt-engineering)
  - [జీరో-షాట్ ప్రాంప్టింగ్](../../../02-prompt-engineering)
  - [ఫ్యూ-షాట్ ప్రాంప్టింగ్](../../../02-prompt-engineering)
  - [చెయిన్ ఆఫ్ థాట్](../../../02-prompt-engineering)
  - [రోల్-ఆధారిత ప్రాంప్టింగ్](../../../02-prompt-engineering)
  - [ప్రాంప్ట్ టెంప్లేట్లు](../../../02-prompt-engineering)
- [అత్యుత్తమ నమూనాలు](../../../02-prompt-engineering)
- [ఉపలబ్ధి ఉన్న అజ్యూర్ వనరులను ఉపయోగించడం](../../../02-prompt-engineering)
- [అనువర్తన స్క్రీన్‌షాట్లు](../../../02-prompt-engineering)
- [నమూనాలను అన్వేషించడం](../../../02-prompt-engineering)
  - [తక్కువ vs అధిక ఉత్సాహం](../../../02-prompt-engineering)
  - [టాస్క్ ఎగ్జిక్యూషన్ (టూల్ ప్రాంబుల్స్)](../../../02-prompt-engineering)
  - [స్వీయ-చింతన కోడ్](../../../02-prompt-engineering)
  - [సంరచిత విశ్లేషణ](../../../02-prompt-engineering)
  - [బహుళ-టర్న్ చాట్](../../../02-prompt-engineering)
  - [దశలవారీగా తర్కం](../../../02-prompt-engineering)
  - [నియంత్రిత అవుట్పుట్](../../../02-prompt-engineering)
- [మీరు నిజంగా నేర్చుకుంటున్నదేంటి](../../../02-prompt-engineering)
- [తదుపరి దశలు](../../../02-prompt-engineering)

## మీరు నేర్చుకునేదేంటి

<img src="../../../translated_images/te/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

గత మాడ్యూల్‌లో, జ్ఞాపకం సంభాషణాత్మక AI కి ఎలా సహాయపడుతుందో మరియు GitHub మోడల్స్ ఉపయోగించి ప్రాథమిక పరస్పర చర్యలను చూశారు. ఇప్పుడు మేము మీరు ప్రశ్నలు ఎలా అడగాలో — ప్రాంప్ట్లు స్వయం — Azure OpenAI యొక్క GPT-5.2 ఉపయోగించి దృష్టి సారిస్తాము. మీరు మీ ప్రాంప్ట్లను ఎలా నిర్మిస్తారో ప్రతిస్పందనల గుణాత్మకతపై భారీ ప్రభావం ఉంటుంది. మేము ప్రాథమిక ప్రాంప్టింగ్ సాంకేతిక పద్ధతుల సమీక్షతో మొదలుపెడతాము, ఆపై GPT-5.2 యొక్క సామర్థ్యాలను పూర్తి ప్రయోజనం పొందే ఎనిమిది ఉత్తమ నమూనాలకు వెళ్తాము.

మేము GPT-5.2 ను ఉపయోగిస్తాము ఎందుకంటే అది తర్క నియంత్రణని పరిచయం చేస్తుంది - మీరు మోడల్ ఎంతగా ఆలోచించాలో చెప్పవచ్చు. ఇది భిన్న ప్రాంప్టింగ్ వ్యూహాలను మరింత స్పష్టంగా చేసి, ఎప్పుడెప్పుడు ఏ పద్ధతిని ఉపయోగించాలో అర్థం చేసుకోవడానికి సహాయపడుతుంది. GitHub మోడల్స్ తో పోలిస్తే Azure లో GPT-5.2 కి తక్కువ రేట్ పరిమితులు ఉంటాయి, దీని నుంచీ కూడా లాభం పొందుతాము.

## అవసర పరిస్థితులు

- మాడ్యూల్ 01 పూర్తి అయినది (Azure OpenAI వనరులు పైకి పెట్టబడినవి)
- మూల డైరెక్టరీలో `.env` ఫైల్ Azure క్రెడెన్షియల్స్ తో (మాడ్యూల్ 01 లో `azd up` ద్వారా సృష్టించబడింది)

> **గమనిక:** మీరు మాడ్యూల్ 01 పూర్తి చేయకపోతే, ముందుగా అక్కడ ఉన్న డిప్లాయ్ మెంట్ సూచనలను అనుసరించండి.

## ప్రాంప్ట్ ఇంజనీరింగ్ అర్థం చేసుకోవడం

<img src="../../../translated_images/te/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

ప్రాంప్ట్ ఇంజనీరింగ్ అంటే మీరు అవసరమైన ఫలితాలను నిరంతరం అందుకునేలా ఇన్పుట్ టెక్స్ట్ డిజైన్ చేయడం. ఇది కేవలం ప్రశ్నలు అడగడం మాత్రమే కాదు - మోడల్ మీ అభ్యర్థనను సరిగ్గా అర్ధం చేసుకుని ఎట్లా అందించాలో గ్రహించేలా విజ్ఞప్తులను నిర్మించడం.

ఇది మీ సహచరునికి సూచనలు ఇవ్వడానికి సరసమనుగా పరిగణించండి. "బగ్గు సరిచేయండి" అనేది అస్పష్టంగా ఉంటుంది. "UserService.java లైన్ 45 లో నల్ పాయింటర్ ఎక్సెప్షన్ సరిచేయండి, నల్ చెక్ చేర్చడం ద్వారా" అనడం స్పష్టంగా ఉంటుంది. భాషా మోడల్స్ కూడా ఇదే విధంగా పనిచేస్తాయి - స్పష్టత మరియు నిర్మాణం ముఖ్యం.

<img src="../../../translated_images/te/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j మోడల్ కనెక్ట్‌లు, మెమరీ మరియు సందేశ రకాల మౌలిక నిర్మాణం అందిస్తుంది, ప్రాంప్ట్ నమూనాలు ఆ నిర్మాణం ద్వారా పంపే శ్రద్ధగా నిర్మించిన టెక్స్ట్ మాత్రమే. కీలకమైన నిర్మాణాల బ్లాక్‌లు `SystemMessage` (ఏఐ యొక్క ప్రవర్తన మరియు పాత్రను సెట్ చేయడం) మరియు `UserMessage` (మీ నిజమైన అభ్యర్థనను తీసుకెళ్తుంది).

## ప్రాంప్ట్ ఇంజనీరింగ్ మౌలికాలు

<img src="../../../translated_images/te/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ఈ మాడ్యూల్ లోని అత్యుత్తమ నమూనాల లోతైన లోటు దిగేదానికి ముందు, ఐదు ప్రాథమిక ప్రాంప్టింగ్ సాంకేతిక పరిజ్ఞానాలను సమీక్షించుకుందాము. ప్రతి ప్రాంప్ట్ ఇంజనీరు ఇవి తెలుసుకోకుండా ఉండకూడదు. మీరు ఇప్పటికే [క్విక్ స్టార్ట్ మాడ్యూల్](../00-quick-start/README.md#2-prompt-patterns) నుండి ఈ పద్ధతులను చూశారు — ఇవి దాని వెనుక సౌలభ్య భవనం.

### జీరో-షాట్ ప్రాంప్టింగ్

అతి సరళమైన విధానం: ఉదాహరణలు లేకుండా మోడల్ కు ప్రత్యక్ష సూచన ఇవ్వండి. మోడల్ పూర్తిగా తన శిక్షణ పై ఆధారపడి టాస్క్ అర్థం చేసుకొని నిర్వర్తిస్తుంది. ఇది లాజిక్ స్పష్టంగా ఉన్న సరళమైన అభ్యర్థనలకు అనుకూలం.

<img src="../../../translated_images/te/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*ఉదాహరణలు లేకుండా ప్రత్యక్ష సూచన — మోడల్ సూచన నుండే టాస్క్‌ను అర్థం చేసుకుంటుంది*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// స్పందన: "సానుకూలమైన"
```

**ఏప్పుడుపయోగించాలి:** సాదా వర్గీకరణలు, ప్రత్యక్ష ప్రశ్నలు, అనువాదాలు లేదా అదనపు మార్గదర్శకం లేకుండా మోడల్ నిర్వహించగల టాస్క్‌లు.

### ఫ్యూ-షాట్ ప్రాంప్టింగ్

మోడల్ అనుసరించాల్సిన నమూనాను చూపించే ఉదాహరణలను ఇవ్వండి. మోడల్ మీ ఉదాహరణల నుంచి ఆశించబడే ఇన్‌పుట్-అవుట్‌పుట్ ఫార్మాట్ నేర్చుకొని కొత్త ఇన్‌పుట్లకు అప్లై చేస్తుంది. ఇది అభ్యర్థన ఫార్మాట్ స్పష్టంగా లేకపోయినప్పుడు స్థిరత్వాన్ని 크게 మెరుగుపరుస్తుంది.

<img src="../../../translated_images/te/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*ఉదాహరణల నుంచి నేర్చుకోవడం — మోడల్ నమూనాను గుర్తించి కొత్త ఇన్‌పుట్లకు వర్తింప చేస్తుంది*

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

**ఏప్పుడుపయోగించాలి:** కస్టమ్ వర్గీకరణలు, సకాలమయిన ఫార్మాటింగ్, డొమైన్-నిర్భర టాస్క్‌లు లేదా జీరో-షాట్ ఫలితాలు స్థిరంగా లేవు.

### చెయిన్ ఆఫ్ థాట్

మోడల్ దాని తర్కాన్ని దశలవారీగా చూపించమని అడగండి. ప్రత్యక్ష సమాధానానికి పైగా, మోడల్ సమస్యను భాగాలుగా విడగొట్టి ప్రతి భాగంలో సూటిగా పని చేస్తుంది. ఇది గణితం, తర్కం మరియు బహుళ దశల తర్కం టాస్క్‌లలో ఖచ్చితత్వాన్ని మెరుగుపరుస్తుంది.

<img src="../../../translated_images/te/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*దశలవారీగా తర్కం — సంక్లిష్ట సమస్యలను స్పష్టమైన తర్కమయిన దశ‌లుగా విభజించడం*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// నమూనా చూపిస్తుంది: 15 - 8 = 7, తరువాత 7 + 12 = 19 యాపిల్స్
```

**ఏప్పుడుపయోగించాలి:** గణిత సమస్యలు, తర్క పజిల్స్, డీబగ్గింగ్ లేదా తర్క ప్రక్రియ చూపించడం ఖచ్చితత్వం మరియు నమ్మకాన్ని పెంచే టాస్క్‌లు.

### రోల్-ఆధారిత ప్రాంప్టింగ్

ప్రశ్న అడగడానికి ముందు AIకి ఒక వ్యక్తిత్వం లేదా పాత్రని సెట్ చేయండి. ఇది ప్రతిస్పందన రీత్యా, లోతు మరియు దృష్టిని తీర్చిదిద్దనుంది. "సాఫ్ట్‌వేర్ ఆర్కిటెక్ట్" "జూనియర్ డెవలపర్" లేదా "సెక్యూరిటీ ఆడిటర్" కన్నా భిన్నమైన సలహా ఇస్తుంది.

<img src="../../../translated_images/te/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*సందర్భం మరియు వ్యక్తిత్వం సెట్ చేయడం — అదే ప్రశ్నకు అనుగుణ పాత్రపై ఆధారపడి వేరైన స్పందన వస్తుంది*

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

**ఏప్పుడుపయోగించాలి:** కోడ్ సమీక్షలు, బోధన, డొమైన్-నిర్భర విశ్లేషణ, లేదా నిర్దిష్ట నైపుణ్య మాదిరి లేదా దృష్టికోణానికి తగ్గిన ప్రతిస్పందనలు అవసరమైనపుడు.

### ప్రాంప్ట్ టెంప్లేట్లు

చట్రిక లోకాలను కలిగిన మళ్లీ ఉపయోగించగల ప్రాంప్ట్‌లు సృష్టించండి. ప్రతిసారి కొత్త ప్రాంప్ట్ రాయడంచేసే బదులుగా, ఒకసారి టెంప్లేట్ నిర్వచించండి మరియు వేర్వేరు విలువలు నింపండి. LangChain4j యొక్క `PromptTemplate` క్లాస్ ఇది `{{variable}}` సింటాక్స్ తో సులభం చేస్తుంది.

<img src="../../../translated_images/te/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*వేరియబుల్ ప్లేస్‌హోల్డర్లతో మళ్లీ ఉపయోగించదగిన ప్రాంప్ట్‌లు — ఒక టెంప్లేట్, అనేక ఉపయోగాలు*

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

**ఏప్పుడుపయోగించాలి:** వేరే ఇన్‌పుట్లతో పునరావృత ప్రశ్నలు, బ్యాచ్ ప్రాసెసింగ్, మళ్లీ ఉపయోగించగల AI వర్క్‌ఫ్లోలు లేదా ప్రాంప్ట్ నిర్మాణం అదే గాను డేటా మారేటప్పుడు.

---

ఈ ఐదు మౌలికాలు చాలా ప్రాంప్టింగ్ టాస్కులకు బలమైన సాధనాలుగా ఉంటాయి. మిగిలిన మాడ్యూల్ వాటిపై ఆధారపడి GPT-5.2 యొక్క తర్క నియంత్రణ, స్వీయ-మూల్యాంకనం మరియు సంరచిత అవుట్పుట్ సామర్థ్యాలను ఉపయోగించే **ఎనిమిది అత్యుత్తమ నమూనాలు** నిర్మించబడతాయి.

## అత్యుత్తమ నమూనాలు

మౌలికాలు సమాప్తమైన తర్వాత, ఈ మాడ్యూల్ ప్రత్యేకత చేసే ఎనిమిది అత్యుత్తమ నమూనాల వైపు సాగుదాం. అన్ని సమస్యలకు ఒకేగా పద్ధతి అవసరం కాదు. కొంత ప్రశ్నలకు త్వరిత సమాధానాలు కావాలి, మరికొన్ని లోతైన ఆలోచన అవసరం. కొంత విన్నట్లుగా తర్కం కావాలి, మరికొన్నది ఫలితాలని మాత్రమే. కింద ప్రతీ నమూనా వేరే సందర్భానికి సరిపోతుంది — మరియు GPT-5.2 యొక్క తర్క నియంత్రణ తేడాలను మర్యాదగా చూపిస్తుంది.

<img src="../../../translated_images/te/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ఎనిమిది ప్రాంప్ట్ ఇంజనీరింగ్ నమూనాల అవలోకనం మరియు వాటి ఉపయోగ సందర్భాలు*

<img src="../../../translated_images/te/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 తర్క నియంత్రణ మీకు మోడల్ ఎంతగా ఆలోచించాలో స్పెసిఫై చేయడానికి అనుమతిస్తుంది — వేగవంతమైన ప్రత్యక్ష సమాధానాల నుండి లోతైన అన్వేషణ వరకు*

<img src="../../../translated_images/te/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*తక్కువ ఉత్సాహం (వేగవంతమైన, ప్రత్యక్ష) vs అధిక ఉత్సాహం (వివరమైన, అన్వేషణాత్మక) తర్క పద్ధతులు*

**తక్కువ ఉత్సాహం (తక్షణం & సేంద్రీయమైన)** - సరళమైన ప్రశ్నలకు మీరు వేగవంతమైన ప్రత్యక్ష సమాధానాలు కోరినప్పుడు. మోడల్ తక్కువగా మాత్రమే తర్క చేస్తుంది - గరిష్ఠంగా 2 దశల వరకు. గణనలు, లుకప్‌లు లేక సూటిగా ఉండే ప్రశ్నలకు దీన్ని ఉపయోగించండి.

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

> 💡 **GitHub Copilot తో అన్వేషించండి:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) తెరిచి అడగండి:
> - "తక్కువ ఉత్సాహం మరియు అధిక ఉత్సాహం ప్రాంప్టింగ్ నమూనాల మధ్య తేడా ఏమిటి?"
> - "ప్రాంప్ట్లలో XML ట్యాగులు AI ప్రతిస్పందన నిర్మాణానికి ఎలా సహాయపడతాయి?"
> - "స్వీయ-చింతన నమూనాలు vs ప్రత్యక్ష సూచన ఎప్పుడూ వాడాలి?"

**అధిక ఉత్సాహం (లోతైన & పూర్తిగా)** - సంక్లిష్ట సమస్యలకు మీరు సమగ్ర విశ్లేషణ కోరినప్పుడు. మోడల్ గాఢంగా అన్వేషించి వివరమైన తర్కాన్ని చూపిస్తుంది. సిస్టమ్ డిజైన్, ఆర్కిటెక్చర్ నిర్ణయాలు లేదా సంక్లిష్ట పరిశోధనలకు దీన్ని ఉపయోగించండి.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**టాస్క్ ఎగ్జిక్యూషన్ (దశల వారీగా పురోగతి)** - బహుళ దశల వర్క్‌ఫ్లోలకు. మోడల్ ముందస్తుగా ప్రణాళిక ఇస్తుంది, ప్రతి దశను పని చేస్తున్నప్పుడు ప్రస్తావిస్తుంది, తరువాత సారాంశం ఇస్తుంది. మైగ్రేషన్లు, అమలు లేదా ఏదైనా బహుళ దశల ప్రక్రియలకు దీన్ని ఉపయోగించండి.

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

చెయిన్-ఆఫ్-థాట్ ప్రాంప్టింగ్ మోడల్ తర్క ప్రక్రియను స్పష్టంగా చూపించమని అడుగుతుంది, సంక్లిష్ట టాస్కుల ఖచ్చితత్వాన్ని పెంచుతుంది. దశల వారీగా విభజన మానవులు మరియు AIకి తర్కాన్ని అర్థం చేసుకోవడంలో సహాయపడుతుంది.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** ఈ నమూనా గురించి అడగండి:
> - "పలుభిన్న-చెలామణి కార్యకలాపాలకు టాస్క్ ఎగ్జిక్యూషన్ నమూనాను ఎలా అనుకూలమ్గో మార్చగలను?"
> - " ఉత్పత్తి అప్లికేషన్లలో టూల్ ప్రాంబుల్స్ నిర్మాణానికి ఉత్తమ పద్ధతులు ఏమిటి?"
> - "UIలో మధ్యస్థ పురోగతి నవీకరణలను ఎలా సేకరించి ప్రదర్శించగలుగుతాను?"

<img src="../../../translated_images/te/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*ప్రణాళిక → అమలు → సారాంశం వర్క్‌ఫ్లో బహుళ దశల టాస్కుల కోసం*

**స్వీయ-చింతన కోడ్** - ఉత్పత్తి-నాణ్యత కోడ్ జనరేట్ చేయడానికి. మోడల్ ఉత్పత్తి ప్రమాణాలు పాటిస్తూ పూర్ణమైన లోపాల నిర్వహణతో కోడ్ సృష్టిస్తుంది. కొత్త ఫీచర్స్ లేదా సర్వీసులు నిర్మిస్తుండగా దీనిని ఉపయోగించండి.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/te/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*పునరావృత మెరుగుదల చక్రం - సృష్టించు, మూల్యాంకన చేయు, సమస్యలు గుర్తించు, మెరుగుపరచు, పునరావృతం చేయు*

**సంరచిత విశ్లేషణ** - స్థిరమైన మూల్యాంకన కోసం. మోడల్ కోడ్‌ను ఫిక్స్ చేయబడిన ఫ్రేమ్‌వర్క్ (కరెక్ట్‌నెస్, ప్రాక్టీసెస్, పనితీరు, భద్రత, నిర్వహణ సౌల్య) పై సమీక్షిస్తుంది. కోడ్ సమీక్ష లేదా నాణ్యత అంచనాలకు దీన్ని ఉపయోగించండి.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** సంరచిత విశ్లేషణ గురించి అడగండి:
> - "వివిధ రకాల కోడ్ సమీక్షలకు విశ్లేషణ ఫ్రేమ్‌వర్క్‌ను ఎలా అనుకూలీకరించవచ్చు?"
> - "సంరచిత అవుట్‌పుట్‌ను ప్రోగ్రామాటిక్గా ఎలా విరగరించి, చర్యల్ని తీసుకోవచ్చు?"
> - "వివిధ సమీక్ష సెషన్లలో స్థిరమైన తీవ్రత స్థాయిలను ఎలా నిర్ధారించగలము?"

<img src="../../../translated_images/te/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*స్థిరమైన కోడ్ సమీక్షల కోసం ఫ్రేమ్‌వర్క్, తీవ్రత స్థాయిలతో*

**బహుళ-టర్న్ చాట్** - సందర్భం అవసరమైన సంభాషణలకు. మోడల్ గత సందేశాలను గుర్తించి వాటిపై నిర్మిస్తుంది. ఇంటరాక్టివ్ సహాయ సెషన్లు లేదా సంక్లిష్ట Q&A కోసం దీన్ని ఉపయోగించండి.

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

*పలు టర్న్ల పాటు సంభాషణ సందర్భం ఎలా సమీకరణ అవుతుంది, టోకెన్ పరిమితి చేరేవరకు*

**దశలవారీగా తర్కం** - కనిష్ట తర్కం అవసరమయిన సమస్యలకు. మోడల్ ప్రతి దశ కోసం స్పష్టమైన తర్కం చూపిస్తుంది. గణిత సమస్యలు, తర్క పజిల్స్ లేదా ఆలోచనా ప్రక్రియ అర్థం చేసుకోవడానికీ ఉపయోగించండి.

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

*సమస్యలను స్పష్టమైన తర్కాత్మక దశలుగా విభజించడం*

**నియంత్రిత అవుట్పుట్** - నిర్దిష్ట ఫార్మాట్ అవసరాలతో ప్రతిస్పందనలకు. మోడల్ ఫార్మాట్ మరియు పొడవు నిబంధనలను కఠినంగా పాటిస్తుంది. సారాంశాలకు లేదా ఖచ్చితమైన అవుట్పుట్ నిర్మాణం కావలసినప్పుడు దీన్ని ఉపయోగించండి.

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

*నిర్దిష్ట ఫార్మాట్, పొడవు, నిర్మాణం అవసరాలు అమలు చేయడం*

## ఉపలబ్ధి ఉన్న అజ్యూర్ వనరులను ఉపయోగించడం

**డిప్లాయ్‌మెంట్ నిర్ధారించండి:**

Azure క్రెడెన్షియల్స్ తో మూల డైరెక్టరీలో `.env` ఫైల్ ఉన్నదో లేదో నిర్ధారించండి (మాడ్యూల్ 01 లో సృష్టించబడింది):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT చూపించాలి
```

**అనువర్తనం ప్రారంభించండి:**

> **గమనిక:** మీరు ఇప్పటికే మాడ్యూల్ 01 నుండి `./start-all.sh` ఉపయోగించి అన్ని అనువర్తనాలను ప్రారంభిస్తే, ఈ మాడ్యూల్ ఇప్పటికే పోర్ట్ 8083 పై రన్ అవుతోంది. మీరు క్రింద-start ఆదేశాలను వదిలిపెడుతూ నేరుగా http://localhost:8083 కు వెళ్ళవచ్చు.

**ఎంపిక 1: స్ప్రింగ్ బూట్ డాష్‌బోర్డ్ ఉపయోగించడం (VS కోడ్ వాడుకరులకు సిఫార్సు)**

డెవ్ కంటైనర్ లో స్ప్రింగ్ బూట్ డాష్‌బోర్డ్ ఎక్స్‌టెన్షన్ ఉంటుంది, ఇది అన్ని స్ప్రింగ్ బూట్ అనువర్తనాలను చూడటానికి విజువల్ ఇంటర్ఫేస్ అందిస్తుంది. మీరు VS కోడ్ ఎడిటర్ ఎడమవైపు ఉన్న యాక్టివిటీ బార్ లో (Spring Boot ఐకాన్ కోసం చూడండి) దీన్ని కనుగొనవచ్చు.
Spring Boot డ్యాష్‌బోర్డ్ నుండి, మీరు చేయవచ్చు:
- వర్క్‌స్పేస్‌లో అందుబాటులో ఉన్న అన్ని Spring Boot అనువర్తనాలను చూడు
- ఒక క్లిక్‌తో అనువర్తనాలు ప్రారంభించు/నిరోధించు
- అనువర్తన లాగ్‌లను రియల్-టైమ్‌లో వీక్షించు
- అనువర్తన స్థితిని పర్యవేక్షించు

ఈ మాడ్యూల్‌ను ప్రారంభించడానికి "prompt-engineering" పక్కన ఉన్న ప్లే బట్లున్‌పై క్లిక్ చేయండి, లేదా ఒకేసారి అన్ని మాడ్యూల్‌లను ప్రారంభించండి.

<img src="../../../translated_images/te/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**వికల్పం 2: షెల్ స్క్రిప్ట్‌లను ఉపయోగించడం**

అన్ని వెబ్ అనువర్తనాలను ప్రారంభించండి (మాడ్యూల్‌లు 01-04):

**Bash:**
```bash
cd ..  # మూల డైరెక్టరీ నుండి
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # మూల డైరెక్టరీ నుండి
.\start-all.ps1
```

లేదా ఈ మాడ్యూల్‌ని మాత్రమే ప్రారంభించండి:

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

ఈ రెండు స్క్రిప్ట్‌లు root `.env` ఫైల్ నుండి ఆటోమేటిక్‌గా ఎన్విరాన్‌మెంట్ వేరియబుల్స్‌ను లోడ్ చేస్తాయి మరియు JAR‌లు లేకుంటే అవి నిర్మిస్తాయి.

> **గమనిక:** మీరు ప్రారంభించే ముందు అన్ని మాడ్యూల్‌లను చేతంగా నిర్మించాలనుకుంటే:
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

**నిరోధించడానికి:**

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

## అనువర్తన స్క్రీన్‌షాట్‌లు

<img src="../../../translated_images/te/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*అన్ని 8 ప్రాంప్ట్ ఇంజనీరింగ్ ప్యాటర్న్‌లు వాటి లక్షణాలు మరియు ఉపయోగ కేసులతో ప్రధాన డ్యాష్‌బోర్డ్‌లో చూపిస్తున్నాయి*

## ప్యాటర్న్‌ల అన్వేషణ

వెబ్ ఇంటర్‌ఫేస్ ద్వారా మీరు వివిధ ప్రాంప్టింగ్ వ్యూహాలతో ప్రయోగం చేయవచ్చు. ప్రతి ప్యాటర్న్ వేర్వేరు సమస్యలను పరిష్కరిస్తుంది - ఏ విధంగా ప్రతిభ చూపిస్తుంది చూసేందుకు ప్రయత్నించండి.

### తక్కువ మరియు ఎక్కువ ఆసక్తి (Low vs High Eagerness)

"200 యొక్క 15% ఎంత?" అనే సరళమైన ప్రశ్నను తక్కువ ఆసక్తితో అడగండి. మీరు తక్షణం, నేరుగా సమాధానం పొందుతారు. ఇప్పుడు "హై-ట్రాఫిక్ API కోసం క్యాచింగ్ వ్యూహం రూపకల్పన చేయండి" అని ఎక్కువ ఆసక్తితో అడగండి. మోడల్ ఎలా మెల్లగా స్పందించి, వివరంగా వివరణ ఇస్తుందో గమనించండి. ఒకే మోడల్, ఒకే ప్రశ్న రచన - కానీ ప్రాంప్ట్ అది ఎంత ఆలోచించాలో చెప్తుంది.

<img src="../../../translated_images/te/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*తగ్గిన ఆలోచనతో వేగవంతమైన లెక్కింపు*

<img src="../../../translated_images/te/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*వివరణాత్మక క్యాచింగ్ వ్యూహం (2.8MB)*

### టాస్క్ ఎగ్జిక్యూషన్ (టూల్ ప్రీమేబుల్స్)

బహుళ-దశల వర్క్‌ఫ్లోలు ముందస్తు ప్రణాళిక మరియు అభివృద్ది వ్రాయడానికి లాభదాయకం. మోడల్ చేయాల్సిన విషయంలో సారాంశం, ప్రతి దశను వివరిస్తూ, తరువాత ఫలితాలను సారంశం చేస్తుంది.

<img src="../../../translated_images/te/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*దశల వారీ వివరణతో REST ఎండ్‌పాయింట్ సృష్టి (3.9MB)*

### స్వీయ-పరిశీలన కొడ్

"ఇమెయిల్ చెల్లింపు సేవను సృష్టించు" అని ప్రయత్నించండి. కేవలం కోడ్ తయారుచేసి ఆగకుండా, మోడల్ సృష్టించి, నాణ్యత ప్రమాణాలకు సరిపోల్చి, లోపాలను కనుగొని, మెరుగుపరుస్తుంది. కోడ్ ఉత్పత్తి ప్రమాణాలకు సరిపోతే వరకు ఇది పునరావృతం అవుతుంది.

<img src="../../../translated_images/te/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*పూర్తి ఇమెయిల్ చెల్లింపు సేవ (5.2MB)*

### నిర్మిత విశ్లేషణ

కోడ్ సమీక్షలు స్థిరమైన మూల్యాంకన ఫ్రేమ్‌వర్క్‌లను అవసరం చేస్తాయి. మోడల్ కోడ్‌ను ఫిక్స్ అయిన వర్గాలు (సరైనదైనద్, ఆచరణ, ప్రదర్శన, భద్రత) మరియు తీవ్రత స్థాయిలతో విశ్లేషిస్తుంది.

<img src="../../../translated_images/te/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ఫ్రేమ్‌వర్క్ ఆధారిత కోడ్ సమీక్ష*

### బహుళ-టర్న్ చాట్

"Spring Boot అంటే ఏమిటి?" అని అడగండి, వెంటనే "ఉదాహరణ చూపించు" అని అడగండి. మోడల్ మీ మొదటి ప్రశ్నను గుర్తుంచుకుని స్పెసిఫిక్ గా Spring Boot ఉదాహరణ ఇస్తుంది. స్మృతి లేకపోతే రెండో ప్రశ్న చాలా అపరిమితంగా ఉండేది.

<img src="../../../translated_images/te/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*ప్రశ్నల మధ్య సందర్భ పరిరక్షణ*

### దశల వారీ ఆలోచన

ఒక గణిత సమస్య ఎన్నుకుని దశల వారీ ఆలోచన మరియు తక్కువ ఆసక్తి రెండు విధానాలతో ప్రయత్నించండి. తక్కువ ఆసక్తి వెంటనే సమాధానం ఇస్తుంది - వేగంగా కానీ పారదర్శకం కాదు. దశల వారీ ఆలోచన మీరు ప్రతి లెక్క మరియు నిర్ణయాన్ని చూపిస్తుంది.

<img src="../../../translated_images/te/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*స్పష్ట దశలతో గణిత సమస్య*

### పరిమిత అవుట్‌పుట్

మీరు ప్రత్యేక ఫార్మాట్‌లు లేదా పద సంఖ్యలు కావాలనుకుంటే, ఈ ప్యాటర్న్ కఠిన నియమాలను అమలు చేస్తుంది. స్పష్టంగా 100 పదాలు ఉన్న బుల్లెట్ పాయింట్ ఫార్మాట్‌లో సారాంశం సృష్టించడానికి ప్రయత్నించండి.

<img src="../../../translated_images/te/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*మిషిన్ లెర్నింగ్ సారాంశం ఫార్మాట్ నియంత్రణతో*

## మీరు నిజంగా నేర్చుకుంటున్నది

**ఆలోచనా కృషి అన్ని విషయాలను మార్చుతుంది**

GPT-5.2 మీ ప్రాంప్ట్‌ల ద్వారా గణనాత్మక కృషిని నియంత్రించగలదు. తక్కువ కృషి అంటే తక్కువ అన్వేషణతో వేగవంతమైన ప్రతిస్పందనలు. ఎక్కువ కృషి అంటే మోడల్ లోతుగా ఆలోచించడానికి సమయం తీసుకుంటుంది. మీరు పని సంక్లిష్టతకు తగిన కృషిని సరిపోల్చటం నేర్చుకుంటున్నారు - సాదారణ ప్రశ్నలకు సమయం వృధా చేయవద్దు, కానీ క్లిష్ట నిర్ణయాలను త్వరగా తీసుకోకండి.

**రచన ప్రవర్తనను మార్గనిర్దేశిస్తుంది**

ప్రాంప్ట్‌లలో XML ట్యాగ్లు గమనించారా? అవి అలంకారంగా లేవు. మోడల్‌లు నిర్మిత సూచనలను స్వేచ్ఛాత్మక వచనం కంటే విశ్వసనీయంగా అనుసరిస్తాయి. మీరు బహుళ-దశ ప్రಕ್ರియలు లేదా క్లిష్ట లాజిక్ కావలసినప్పుడు, నిర్మాణం మోడల్‌కు ఎక్కడ ఉందో, తరువాత ఏమి చేయాలో ట్రాక్ చేయడంలో సహాయపడుతుంది.

<img src="../../../translated_images/te/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*స్పష్ట విభాగాలు మరియు XML-శైలితో కలిగిన బాగా నిర్మిత ప్రాంప్ట్ శరీరం*

**నాణ్యత స్వీయ-మూల్యాంకన ద్వారా**

స్వీయ-పరిశీలన ప్యాటర్న్‌లు నాణ్యత ప్రమాణాలను స్పష్టంగా చేస్తూ పనిచేస్తాయి. మోడల్ "సరైన విధంగా చేస్తుందేమో" ఆశించకుండా, మీరు దానికి "సరైనది" అంటే ఏమిటి అనేది చెప్పాలి: సరైన లాజిక్, లోపాల నిర్వహణ, ప్రదర్శన, భద్రత. ఆ తరువాత మోడల్ తన ఉత్పత్తిని స్వయంగా మూల్యాంకనం చేసి మెరుగుపరుచుతుంది. ఇది కోడ్ ఉత్పత్తిని లాటరీ నుండి ఒక ప్రాసెస్‌గా మార్చుతుంది.

**సందర్భం పరిమితమైనది**

బహుళ-టర్న్ సంభాషణలు ప్రతి అభ్యర్థనతో సందేశ చరిత్రను చేర్చడం ద్వారా పనిచేస్తాయి. కాని పరిమితి ఉంది - ప్రతి మోడల్‌కు గరిష్ట టోకెన్ లెక్క ఉంది. సంభాషణలు పెరిగేకొద్దీ, మీరు సంబంధిత సందర్భాన్ని నిలబెట్టుకునే వ్యూహాలు అవసరం ఉంటుంది, ఆ గరిష్టాన్ని తాకకుండా. ఈ మాడ్యూల్ మేథస్సు ఎలా పనిచేస్తుందో చూపిస్తుంది; తర్వాత మీరు ఎప్పుడు సారాంశం చేయాలో, ఎప్పుడు మర్చిపోాలో, ఎప్పుడు పొందాలో నేర్చుకుంటారు.

## తదుపరి దశలు

**తదుపరి మాడ్యూల్:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**నావిగేషన్:** [← ముందటి: మాడ్యూల్ 01 - పరిచయం](../01-introduction/README.md) | [ప్రధానం‌కు తిరిగి](../README.md) | [తదుపరి: మాడ్యూల్ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**డిస్క్లైమర్**:
ఈ డాక్యుమెంట్‌ను AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించారు. గణనీయంగా ఖచ్చితత్వానికి ప్రయత్నించినప్పటికీ, ఆటోమేటెడ్ అనువాదాల్లో పొరపాట్లు లేదా అసంపూర్ణతలు ఉండవచ్చు. స్థానిక భాషలో ఉన్న అసలు డాక్యుమెంట్‌ను అధికారిక మూలం గా భావించాలి. ముఖ్యమైన సమాచారానికి, ప్రొఫెషనల్ మానవ అనువాదాన్ని సూచించబడినది. ఈ అనువాదం వాడకంలో వచ్చే ఏమైనా తప్పులక లేదా అవగాహన లోపాలకు మేము బాధ్యత నిర్వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->