# Module 00: క్విక్ స్టార్‌ట్

## పట్టిక విషయం

- [పరిచయం](../../../00-quick-start)
- [LangChain4j అంటే ఏమిటి?](../../../00-quick-start)
- [LangChain4j ఆధారితాలు](../../../00-quick-start)
- [ఆవశ్యకతలు](../../../00-quick-start)
- [సెట్ అప్](../../../00-quick-start)
  - [1. మీ GitHub టోకెన్ పొందండి](../../../00-quick-start)
  - [2. మీ టోకెన్ సెట్ చేయండి](../../../00-quick-start)
- [ఉదాహరణలను నడపండి](../../../00-quick-start)
  - [1. ప్రాథమిక చాట్](../../../00-quick-start)
  - [2. ప్రాంప్ట్ నమూనాలు](../../../00-quick-start)
  - [3. ఫంక్షన్ కాలింగ్](../../../00-quick-start)
  - [4. డాక్యుమెంట్ ప్రశ్న & జవాబు (ఈజీ RAG)](../../../00-quick-start)
  - [5. బాధ్యమైన AI](../../../00-quick-start)
- [ప్రతి ఉదాహరణ ఏమి చూపిస్తుంది](../../../00-quick-start)
- [తరువాతి దశలు](../../../00-quick-start)
- [పరిష్కార మార్గాలు](../../../00-quick-start)

## పరిచయం

ఈ క్విక్ స్టార్ట్ మీకు LangChain4j తో వీలైనంత త్వరగా ప్రారంభించేలా చేయగలదు. ఇది LangChain4j మరియు GitHub మోడెల్స్ తో AI అప్లికేషన్లను నిర్మించడంలోని అతి పునాది అంశాలను కవర్ చేస్తుంది. తర్వాతి మాడ్యూల్స్ లో మీరు Azure OpenAI ని LangChain4j తో ఉపయోగించి మరింత అభివృద్ధి చెందిన అప్లికేషన్లు నిర్మిస్తారు.

## LangChain4j అంటే ఏమిటి?

LangChain4j అనేది AI-ఆధారిత అప్లికేషన్లు సులభంగా నిర్మించుకునేందుకు ఉపయోగించే జావా లైబ్రరీ. HTTP క్లయింట్లు మరియు JSON పార్సింగ్ తో సర్ధుబాటు కాకుండా, మీరు శుభ్రమైన జావా API లతో పని చేస్తారు.

LangChain లో "చెయిన్" అనగా అనేక భాగాలను ఒకదానికొకటి చేర్చడం - మీరు ఒక ప్రాంప్ట్ ని ఒక మోడల్ కి, ఆ తర్వాత ఒక పార్సర్ కి వెళ్లించవచ్చు, లేదా అనేక AI కాల్స్ ని ఒకదానికి మరోదాని అవుట్‌పుట్ గా చేర్చవచ్చు. ఈ క్విక్ స్టార్ట్ పాఠశాలలో పునాది అంశాలపై దృష్టి సారించి, క్లిష్టమైన చెయిన్ గురించి తరవాత అర్థం చేసుకుంటారు.

<img src="../../../translated_images/te/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j లో భాగాలను చెయిన్ చేయడం - బ్లాక్స్ కలిసి శక్తివంతమైన AI వర్క్‌ఫ్లోలను సృష్టించడం*

మేము మూడు ముఖ్య భాగాలను ఉపయోగిస్తాము:

**ChatModel** - AI మోడల్ తో సంభాషణ కోసం ఇంటర్‌ఫేస్. `model.chat("prompt")` ని పిలవండి, మరియు ప్రతిస్పందన స్ట్రింగ్ పొందండి. మేము `OpenAiOfficialChatModel` ని ఉపయోగిస్తాము, ఇది GitHub మోడెల్స్ వంటి OpenAI అనుకూల ఎండ్పాయింట్లతో పని చేస్తుంది.

**AiServices** - టైప్-సేఫ్ AI సర్వీస్ ఇంటర్‌ఫేస్‌లను సృష్టిస్తుంది. మెతడ్స్ ను నిర్వచించండి, వాటిని `@Tool` తో గుర్తించండి, మరియు LangChain4j ఆర్కెస్ట్రేషన్ ను నిర్వహిస్తుంది. అవసరమైనప్పుడు AI మీ జావా మెతడ్స్ ను ఆటోమేటిక్ గా పిలుస్తుంది.

**MessageWindowChatMemory** - సంభాషణ చరిత్రను నిర్వహిస్తుంది. ఇది లేకపోతే, ప్రతి అభ్యర్థన స్వతంత్రం. దీనితో, AI గత సందేశాలను గుర్తుపడతాడు మరియు బహుళ టర్న్‌లలో సందర్భాన్ని నిలుపుకుంటాడు.

<img src="../../../translated_images/te/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ఆర్కిటెచర్ - మీ AI అప్లికేషన్లకు శక్తివంతమైన సహజ భాగాలు కలిసి పని చేస్తాయి*

## LangChain4j ఆధారితాలు

ఈ క్విక్ స్టార్ట్ [`pom.xml`](../../../00-quick-start/pom.xml) లో మూడు Maven ఆధారితాలను ఉపయోగిస్తుంది:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` మాడ్యూల్ `OpenAiOfficialChatModel` క్లాస్ ను అందిస్తుంది, ఇది OpenAI అనుకూల API లకు కనెక్ట్ అవుతుంది. GitHub మోడెల్లు అదే API ఫార్మాట్ ఉపయోగిస్తాయి, అందుకే ప్రత్యేక అడాప్టర్ అవసరం లేదు - కేవలం బేస్ URL ను `https://models.github.ai/inference` కు పాయింట్ చేయండి.

`langchain4j-easy-rag` మాడ్యూల్ స్వయంచాలక డాక్యుమెంట్ విభజన, ఎంబెడ్డింగ్, మరియు రిట్రీవల్ ను అందిస్తుంది కాబట్టి మీరు మాన్యువల్ గా ప్రతి దశను కాన్ఫిగర్ చేయవలసిన అవసరం లేకుండా RAG అప్లికేషన్లు సృష్టించవచ్చు.

## ఆవశ్యకతలు

**డెవ్ కంటైనర్ ఉపయోగిస్తున్నారా?** జావా మరియు Maven ముందుగా ఇన్స్టాల్ అయి ఉంటాయి. మీకు కేవలం GitHub పర్సనల్ యాక్సెస్ టోకెన్ అవసరం.

**లోకల్ అభివృద్ధి:**
- Java 21+, Maven 3.9+
- GitHub పర్సనల్ యాక్సెస్ టోకెన్ (క్రింది సూచనలు చూడండి)

> **గమనిక:** ఈ మాడ్యూల్ GitHub మోడెల్స్ లోని `gpt-4.1-nano` ను ఉపయోగిస్తుంది. కోడ్‌లో మోడల్ పేరును మార్చకండి - ఇది GitHub అందుబాటులో ఉన్న మోడెల్స్ తో పనిచేయడానికి సెట్ చేయబడింది.

## సెట్ అప్

### 1. మీ GitHub టోకెన్ పొందండి

1. [GitHub సెట్టింగ్స్ → పర్సనల్ యాక్సెస్ టోకెన్స్](https://github.com/settings/personal-access-tokens) కు వెళ్లండి
2. "Generate new token" క్లిక్ చేయండి
3. వివరణాత్మక పేరు ఇవ్వండి (ఉదా: "LangChain4j Demo")
4. కాలపరిమితి సెట్ చేయండి (7 రోజుల సిఫార్సు)
5. "యాకౌంట్ అనుమతులు" కింద "Models" ని చూసి "Read-only" గా సెట్ చేయండి
6. "Generate token" క్లిక్ చేయండి
7. టోకెన్ ను కాపీ చేసి భద్రపరచండి - మళ్ళీ చూసే అవకాశం ఉండదు

### 2. మీ టోకెన్ సెట్ చేయండి

**ఎంపిక 1: VS కోడ్ ఉపయోగించడం (శిఫార్సు)**

మీరు VS Code ఉపయోగిస్తుంటే, ప్రాజెక్ట్ రూట్లో `.env` ఫైల్ లో మీ టోకెన్ జోడించండి:

`.env` ఫైల్ లేకపోతే, `.env.example` ని కాపీ చేసి `.env` గా మార్చండి లేదా కొత్త `.env` ఫైల్ సృష్టించండి.

**ఉదాహరణ `.env` ఫైల్:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env లో
GITHUB_TOKEN=your_token_here
```

అరవుతూ, Explorer లో ఏదైనా డెమో ఫైల్ (ఉదా: `BasicChatDemo.java`) పై రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోవచ్చు లేదా Run మరియు Debug ప్యానెల్ నుండి లాంచ్ కాన్ఫిగరేషన్లు ఉపయోగించండి.

**ఎంపిక 2: టెర్మినల్ ఉపయోగించడం**

టోకెన్ ని ఎన్విరాన్‌మెంట్ వేరియబుల్ గా సెట్ చేయండి:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ఉదాహరణలను నడపండి

**VS కోడ్ ఉపయోగిస్తూ:** Explorer లో ఏదైనా డెమో ఫైల్ పై రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోండి, లేదా Run & Debug ప్యానెల్ లో లాంచ్ కాన్ఫిగరేషన్లు రన్ చేయండి (ముందుగా `.env` ఫైల్ లో మీ టోకెన్ జోడించారని నిర్ధారించుకోండి).

**Maven ఉపయోగిస్తూ:** లేకపోతే మీరు కింద ఇచ్చిన కమాండ్ లతో కమాండ్ లైన్ నుండి నడపవచ్చు:

### 1. ప్రాథమిక చాట్

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. ప్రాంప్ట్ నమూనాలు

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

జీరో-షాట్, ఫ్యూ-షాట్, చైన్-ఆఫ్-థాట్, మరియు రోల్-బేస్డ్ ప్రాంప్టింగ్ చూపిస్తుంది.

### 3. ఫంక్షన్ కాలింగ్

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI అవసరమైనప్పుడు ఆటోమేటిక్ గా మీ జావా మెతడ్స్ ని పిలుస్తుంది.

### 4. డాక్యుమెంట్ ప్రశ్న & జవాబు (ఈజీ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ఈజీ RAG ని ఉపయోగించి ఆటోమేటిక్ ఎంబెడ్డింగ్ మరియు రిట్రీవల్ తో మీ డాక్యుమెంట్ల గురించి ప్రశ్నలు అడగండి.

### 5. బాధ్యమైన AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI సేఫ్టీ ఫిల్టర్స్ హానికర కంటెంట్ ని ఎలా అడ్డుకుంటాయో చూడండి.

## ప్రతి ఉదాహరణ ఏమి చూపిస్తుంది

**ప్రాథమిక చాట్** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ఇక్కడ మొదలుపెట్టండి, LangChain4j ఎంత సులభమో చూడండి. మీరు `OpenAiOfficialChatModel` రూపొందించి, `.chat()` తో ప్రాంప్ట్ పంపించి, ప్రతిస్పందన పొందుతారు. ఇది ఫౌండేషన్ ని చూపిస్తుంది: కస్టమ్ ఎండ్పాయింట్లు మరియు API కీస్ తో మోడల్స్ ని ప్రారంభించడం. ఈ పద్ధతి అర్థమైతే, మిగతా ప్రతీది దీని మీదే ఉంటుంది.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ఓపెన్ చేసి అడగండి:
> - "ఈ కోడ్ లో GitHub మోడెల్స్ నుండి Azure OpenAI కి ఎలా మారుస్తాను?"
> - "OpenAiOfficialChatModel.builder() లో మరే ఇతర పారామీటర్లు కాన్ఫిగర్ చేయవచ్చా?"
> - "पूर्ण ప్రతిస్పందన కోసం క్యూకి కాకుండా స్ట్రీమింగ్ ప్రతిస్పందనలను ఎలా జతచేస్తాను?"

**ప్రాంప్ట్ ఇంజనీరింగ్** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ఇప్పుడు మోడల్ తో ఎలా మాట్లాడాలో తెలుసుకున్నార, మీరు దానికి ఏమి చెప్పాలో చూద్దాం. ఈ డెమో అదే మోడల్ సెటప్ వాడి ఐదు వేర్వేరు ప్రాంప్టింగ్ నమూనాలను చూపుతుంది. నేరుగా సూచనలు ఇచ్చే జీరో-షాట్ ప్రాంప్ట్స్, ఉదహరణల ద్వారా నేర్పుకునే ఫ్యూ-షాట్, ఆలోచనా ప్రక్రియను చూపించే చైన్-ఆఫ్-థాట్, ఇంకా సందర్భాన్ని ఏర్పరచుకునే రోల్-బేస్డ్ ప్రాంప్ట్స్ చూస్తారు. మీరు ఎలా అడిగినా అదే మోడల్ వేర్వేరు ఫలితాలు చూపుతుందనే విషయం తెలుసుకుంటారు.

డెమో ప్రాంప్ట్ టెంప్లేట్స్ ను కూడా చూపిస్తుంది, ఇవి వేరియబుల్స్ తో పునర్వినియోగమయ్యే శక్తివంతమైన ప్రాంప్ట్‌లను సృష్టించడానికి ఉపయోగపడతాయి.
క్రింది ఉదాహరణ LangChain4j `PromptTemplate` ఉపయోగించి వేరియబుల్స్ నింపే విధానం చూపిస్తుంది. ఇచ్చిన గమ్యం మరియు కార్యాచరణ ఆధారంగా AI జవాబిస్తుంది.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ఓపెన్ చేసి అడగండి:
> - "జీరో-షాట్ మరియు ఫ్యూ-షాట్ ప్రాంప్టింగ్ లో తేడా ఏమిటి, మరియు ఏ సందర్భంలో ఏది ఉపయోగించాలి?"
> - "తపతులెన్సీ పారామీటరు మోడల్ ప్రతిస్పందనలపై ఎలా ప్రభావం చూపుతుందో?"
> - "ప్రొడక్షన్ లో ప్రాంప్ట్ ఇంజెక్షన్ అటాక్స్ ని నివారించేందుకు కొన్ని సాంకేతికతలు ఏవి?"
> - "సాధారణ నమూనాల కోసం పునర్వినియోగ పాఠ్యాల `PromptTemplate` ఆబ్జెక్ట్స్ ను ఎలా సృష్టించాలి?"

**టూల్ ఇంటిగ్రేషన్** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ఇక్కడని LangChain4j శక్తివంతంగా మారుతుంది. మీరు `AiServices` ఉపయోగించి ఒక AI సహాయకుడిని సృష్టిస్తారు, ఇది మీ జావా మెతడ్స్ ని పిలవగలదు. మెతడ్స్ కి `@Tool("వివరణ")` అనోటేట్ చేయండి, మిగతావన్నీ LangChain4j నిర్వహిస్తుంది - యూజర్ అడిగిన ప్రకారం AI తగినప్పుడు టూల్స్ ని పిలుస్తుంది. ఫంక్షన్ కాలింగ్ సూత్రాన్ని ఇది చూపిస్తుంది, అవి కేవలం ప్రశ్నలకు సమాధానం ఇవ్వకుండా చర్యలు తీసుకునేందుకు వీలు ఇస్తుంది.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ఓపెన్ చేసి అడగండి:
> - "@Tool అనోటేషన్ ఎలా పనిచేస్తుంది మరియు LangChain4j దాని వెనుక ఏమి చేస్తుంది?"
> - "సమస్య పరిష్కారానికి AI ఒకదాని తర్వాత మరో టూల్స్ ను వరుసగా పిలవగలదా?"
> - "ఒక టూల్ ఎర్రర్.Throw చేస్తే ఎలా నిర్వహించాలి?"
> - "ఈ కేల్క్యులేటర్ ఉదాహరణ కాకుండా నిజమైన API ని ఎలా ఇంటిగ్రేట్ చేస్తాను?"

**డాక్యుమెంట్ ప్రశ్న & జవాబు (ఈజీ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ఇక్కడ మీరు LangChain4j యొక్క "ఈజీ RAG" ధోరణిని (retrieval-augmented generation) చూడగలరు. డాక్యుమెంట్లు లోడ్ అవుతాయి, ఆటోమేటిక్ గా విడగొట్టబడతాయి, ఎంబెడ్ చేసబడతాయి, ఆపై కంటెంట్ రిట్రీవర్ AI కి సంబంధిత భాగాలను అందిస్తుంది. AI మీ డాక్యుమెంట్ల ఆధారంగా సమాధానం ఇస్తుంది, సాధారణ జ్ఞానం ఆధారంగా కాదు.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ఓపెన్ చేసి అడగండి:
> - "మోడల్ శిక్షణ డేటా వాడే సందర్భంలో RAG AI కల్పనలను ఎలా నివారిస్తుంది?"
> - "ఈ సులభ విధానం మరియు కస్టమ్ RAG పైప్‌లైన్ మధ్య తేడా ఏమిటి?"
> - "బహుళ డాక్యుమెంట్లు లేదా పెద్ద జ్ఞాన నిధులను ఎలా విస్తరించాల?"

**బాధ్యమైన AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

AI సేఫ్టీని లోతైన రక్షణతో నిర్మించండి. ఈ డెమో రెండు రక్షణ పొరలను చూపిస్తుంది:

**భాగం 1: LangChain4j ఇన్‌పుట్ గార్డ్‌రెయిల్స్** - ప్రమాదకర ప్రాంప్ట్స్ ని LLM చేరక ముందే అడ్డుకుంటుంది. నిషేధిత కీవర్డ్స్ లేదా నమూనాల కోసం అనుకూల గార్డ్‌రెయిల్స్ సృష్టించండి. ఇవి మీ కోడ్ లో నడిచేలా ఉండగా వేగవంతంగా మరియు ఉచితం.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**భాగం 2: ప్రొవైడర్ సేఫ్టీ ఫిల్టర్స్** - GitHub మోడెల్లకు అనుబంధ ఫిల్టర్స్ ఉన్నాయి, అవి మీ గార్డ్‌రెయిల్స్ మరచిపోయిన వాటిని గుర్తిస్తాయి. తీవ్ర ఉల్లంఘనల వలన హార్డ్ బ్లాక్స్ (HTTP 400 errors) మరియు సాఫ్ట్ రిఫ్యూసల్స్ (AI మృదువుగా తిరస్కరిస్తుంది) ఉంటాయి.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ఓపెన్ చేసి అడగండి:
> - "InputGuardrail అంటే ఏమిటి మరియు నేను నా గార్డ్‌రెయిల్స్ ఎలా సృష్టించాలి?"
> - "హార్డ్ బ్లాక్ మరియు సాఫ్ట్ రిఫ్యూసల్ మధ్య తేడా ఏమిటి?"
> - "గార్డ్‌రెయిల్స్ మరియు ప్రొవైడర్ ఫిల్టర్స్ రెండింటినీ ఎందుకు ఉపయోగించాలి?"

## తరువాతి దశలు

**తరువాతి మాడ్యూల్:** [01-పరిచయం - LangChain4j మరియు gpt-5 తో Azure లో ప్రారంభించడం](../01-introduction/README.md)

---

**నావిగేషన్:** [← ప్రధానానికి తిరిగి](../README.md) | [తరువాత: Module 01 - పరిచయం →](../01-introduction/README.md)

---

## పరిష్కార మార్గాలు

### మొదటి సారి Maven నిర్మాణం

**సమస్య:** మొదటిసారి `mvn clean compile` లేదా `mvn package` చాలా సమయం తీసుకుంటుంది (10-15 నిమిషాలు)

**కారణం:** Maven మొదటి బిల్డ్ లో అన్ని ప్రాజెక్ట్ ఆధారితాలు (Spring Boot, LangChain4j లైబ్రరీలు, Azure SDKలు తదితరాలు) డౌన్‌లోడ్ చేయాలి.

**పరిష్కారం:** ఇది సాధారణ కార్యకలాపం. తదుపరి బిల్డ్స్ లో ఆధారితాలు లోకల్ లో కాచై ఉంటాయి కాబట్టి వేగవంతంగా ఉంటాయి. డౌన్‌లోడ్ సమయం మీ నెట్‌వర్క్ వేగంపై ఆధారపడి ఉంటుంది.

### పవర్‌షెల్ Maven కమాండ్ సింటాక్స్

**సమస్య:** Maven కమాండ్లు `Unknown lifecycle phase ".mainClass=..."` అనే ఎర్రర్ తో విఫలమవుతాయి
**కారణం**: PowerShell `=` ను వేరియబుల్ అసైన్‌మెంట్ ఆపరేటర్‌గా అర్థం చేసుకుంటుంది, దీని వల్ల Maven ప్రాపర్టీ సింటాక్స్ మధ్యలో భంగం కలుగుతుంది

**పరిష్కారం**: Maven కమాండ్ ముందు స్టాప్-పార్సింగ్ ఆపరేటర్ `--%` ను ఉపయోగించండి:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ఆపరేటర్ PowerShellకు మిగిలిన అన్ని ఆర్గుమెంట్స్‌ను అర్థం చేసుకోకుండా సూటిగా Mavenకు పాస్ చేస్తుంది.

### Windows PowerShell ఎమోజి ప్రదర్శన

**సమస్య**: PowerShellలో ఎమోజీల స్థానంలో AI సమాధానాలు చెత్త అక్షరాలు (ఉదాహరణకు, `????` లేదా `â??`) చూపిస్తాయి

**కారణం**: PowerShell ప్రామాణిక ఎన్‌కోడింగ్ UTF-8 ఎమోజీలను మద్దతు ఇవ్వదు

**పరిష్కారం**: జావా అప్లికేషన్లు నడిపించే ముందు ఈ కమాండ్‌ను అమలు చేయండి:
```cmd
chcp 65001
```

ఇది టర్మినల్‌లో UTF-8 ఎన్‌కోడింగ్‌ను బలపరుస్తుంది. లేదా, మెరుగైన యూనicode మద్దతుతో Windows Terminalని ఉపయోగించండి.

### API కాల్స్ డీబగ్గింగ్

**సమస్య**: AI మోడల్ నుండి ప్రమాణీకరణ లోపాలు, రేట్ పరిమితులు, లేదా ఆశించిన కాని స్పందనలు

**పరిష్కారం**: ఈ ఉదాహరణల్లో `.logRequests(true)` మరియు `.logResponses(true)` ఉంటాయి, ఇవి API కాల్స్‌ను కన్సోల్‌లో చూపిస్తాయి. ఇది ప్రమాణీకరణ లోపాలు, రేట్ పరిమితులు, లేదా ఆశించని స్పందనలను ట్రబుల్‌షూట్ చేయడానికి సహాయపడుతుంది. ప్రొడక్షన్‌లో లాగ్ శబ్దం తగ్గించడానికి ఈ ఫ్లాగ్స్‌ను తీసివేయండి.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**డిస్క్లైమర్**:  
ఈ పత్రాన్ని AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ద్వారా అనువదించబడింది. మనం ఖచ్చితత్వాన్ని అలవర్చుకున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాల్లో పొరపాట్లు లేదా లోపాలు ఉండవచ్చు అని దయచేసి తెలియజేయాలి. మూల పత్రం తమ స్వదేశీ భాషలోనే అధికారిక మూలం గా పరిగణించాలి. కీలక సమాచారానికి, ప్రొఫెషనల్ మానవ అనువాదం సిఫార్సు చేయబడుతుంది. ఈ అనువాదం వలన ఏర్పడే ఏవైనా అభిప్రాయాలు లేదా తప్పుదోవలకు మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->