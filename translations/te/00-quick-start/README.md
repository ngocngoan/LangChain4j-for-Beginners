# మాడ్యూల్ 00: త్వరిత ప్రారంభం

## సూచిక

- [పరిచయం](../../../00-quick-start)
- [LangChain4j అంటే ఏమిటి?](../../../00-quick-start)
- [LangChain4j ఆధారాలు](../../../00-quick-start)
- [ముందస్తు అవసరాలు](../../../00-quick-start)
- [సెట్టప్](../../../00-quick-start)
  - [1. మీ GitHub టోకెన్ తీసుకోండి](../../../00-quick-start)
  - [2. మీ టోకెన్ సెట్ చేయండి](../../../00-quick-start)
- [ఉదాహరణలు నడపండి](../../../00-quick-start)
  - [1. బేసిక్ చాట్](../../../00-quick-start)
  - [2. ప్రాంప్ట్ నమూనాలు](../../../00-quick-start)
  - [3. ఫంక్షన్ కాలింగ్](../../../00-quick-start)
  - [4. డాక్యుమెంట్ ప్రశ్నలు మరియు సమాధానాలు (RAG)](../../../00-quick-start)
  - [5. బాధ్యతాయుత AI](../../../00-quick-start)
- [ప్రతి ఉదాహరణ ఏం చూపిస్తుంది](../../../00-quick-start)
- [తర్వాతి దశలు](../../../00-quick-start)
- [సమస్య పరిష్కారం](../../../00-quick-start)

## పరిచయం

ఈ త్వరిత ప్రారంభం మీకు LangChain4j తో సాధ్యమైనంత త్వరగా ప్రారంభించేందుకు తయారు చేయబడింది. ఇది LangChain4j మరియు GitHub మోడల్స్ తో AI అప్లికేషన్లను నిర్మించే ప్రాథమిక విషయాలను కవర్ చేస్తుంది. తర్వాతి మాడ్యూల్స్ లో మీరు Azure OpenAI ను LangChain4j తో ఉపయోగించి మరింత ఆధునిక అప్లికేషన్లను నిర్మిస్తారు.

## LangChain4j అంటే ఏమిటి?

LangChain4j అనేది Java లైబ్రరీ, ఇది AI-శక్తివంతమైన అప్లికేషన్లను సులభంగా నిర్మించేందుకు సహాయపడుతుంది. HTTP క్లయింట్లు మరియు JSON పార్సింగ్ లో ఇబ్బంది పడటం కంటే, మీరు శుభ్రమైన Java APIs తో పనిచేస్తారు.

LangChain లో "చైన్" అనగా పలు భాగాలను కలో కలుపటం. మీరు ప్రాంప్ట్ నుండి మోడల్ కి, ఆపై పార్సర్ కి చైన్ చేయవచ్చు, లేదా పలు AI కాల్స్ ను జతచేసి ఒక అవుట్పుట్ తర్వాతి ఇన్‌పుట్‌గా ఉపయోగించవచ్చు. ఈ త్వరిత ప్రారంభం ఈ ప్రాథమిక అంశాలతో ప్రారంభించి కోంప్లెక్స్ చైన్లను పరిశీలిస్తుంది.

<img src="../../../translated_images/te/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j లో భాగాలను చైన్ చేయడం - బ్లాక్‌లతో శక్తివంతమైన AI వర్క్‌ఫ్లోలు నిర్మణం*

మేము మూడు ప్రధాన భాగాలను ఉపయోగిస్తాము:

**ChatLanguageModel** - AI మోడల్ ఇంటరాక్షన్స్ కోసం ఇంటర్‌ఫేస్. `model.chat("prompt")` పిలవండి మరియు స్పందన స్ట్రింగ్ పొందండి. మేము `OpenAiOfficialChatModel` ని ఉపయోగిస్తున్నాము, ఇది OpenAI అనుకూల ఎండ్‌పాయింట్‌లతో పని చేస్తుంది, GitHub మోడల్స్ వంటి.

**AiServices** - రకం-భద్రత కలిగిన AI సేవా ఇంటర్‌ఫేస్లను సృష్టిస్తుంది. విధానాలను నిర్వచించండి, వాటిని `@Tool` తో గుర్తించండి, LangChain4j ప్రమేయం నిర్వహిస్తుంది. AI అవసరమైనప్పుడు మీ Java పద్ధతులను ఆటోమాటిగ్గా పిలుస్తుంది.

**MessageWindowChatMemory** - సంభాషణ చరిత్రను నిర్వహిస్తుంది. దేని లేకపోతే ప్రతి అభ్యర్థన స్వతంత్రం. దీని తో, AI మొన్నటి మెసేజులు గుర్తుంచుకుంటుంది మరియు అనేక టర్న్స్ మధ్య సందర్భాన్ని కాపాడుతుంది.

<img src="../../../translated_images/te/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j నిర్మాణం - మీ AI అప్లికేషన్లను శక్తివంతం చేయడానికి ప్రధాన భాగాలు కలిసి పని చేస్తాయి*

## LangChain4j ఆధారాలు

ఈ త్వరిత ప్రారంభం [`pom.xml`](../../../00-quick-start/pom.xml) లో రెండు మావెన్ ఆధారాలను ఉపయోగిస్తుంది:

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
```

`langchain4j-open-ai-official` మాడ్యూల్ `OpenAiOfficialChatModel` క్లాస్‌ను అందిస్తుంది, ఇది OpenAI అనుకూల API లకు కనెక్ట్ అవుతుంది. GitHub Models అదే API ఫార్మాట్ ఉపయోగిస్తాయి, కాబట్టి ప్రత్యేక అడాప్టర్ అవసరం లేదు - కేవలం ప్రాథమిక URL ను `https://models.github.ai/inference` కు సూచించండి.

## ముందస్తు అవసరాలు

**డెవ్ కంటైనర్ ఉపయోగిస్తున్నారా?** Java మరియు Maven ఇప్పటికే ఇన్‌స్టాల్ అయి ఉన్నాయి. మీరు మీకు కేవలం GitHub Personal Access Token అవసరం.

**లోకల్ డెవలప్మెంట్:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (క్రింది సూచనలు)

> **గమనిక:** ఈ మాడ్యూల్ `gpt-4.1-nano` ని GitHub Models నుండి ఉపయోగిస్తుంది. కోడులో మోడల్ పేరును మార్చవద్దు - ఇది GitHub అందుబాటులో ఉన్న మోడల్స్ తో పనిచేయడానికి కన్‌ఫిగర్ చేయబడింది.

## సెట్టప్

### 1. మీ GitHub టోకెన్ పొందండి

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) కి వెళ్లండి
2. "Generate new token" క్లిక్ చేయండి
3. వివరణాత్మక పేరు పెట్టండి (ఉదా: "LangChain4j Demo")
4. ముగింపు తేదీ సెట్ చేయండి (7 రోజులు సిఫార్సు)
5. "Account permissions" లో "Models" ను "Read-only" గా సెట్ చేయండి
6. "Generate token" క్లిక్ చేయండి
7. మీ టోకెన్ కాపీ చేసి సేవ్ చేసుకోండి - ఇది తర్వాత చూడలేరు

### 2. మీ టోకెన్ సెట్ చేయండి

**ఎంపిక 1: VS Code ఉపయోగించి (సిఫార్సు)**

మీరు VS Code ఉపయోగిస్తే, ప్రాజెక్ట్ రూట్ లో `.env` ఫైల్ కు మీ టోకెన్ జోడించండి:

`.env` ఫైల్ లేకపోతే, `.env.example` ను `.env` కాపీ చేయండి లేదా కొత్త `.env` ఫైల్ క్రియేట్ చేయండి.

**ఉదాహరణ `.env` ఫైల్:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env లో
GITHUB_TOKEN=your_token_here
```

తర్వాత మీరు Explorer లో ఏ డెమో ఫైల్ (`BasicChatDemo.java`)పై రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోండి లేదా రన్ & డీబగ్ ప్యానెల్ లోని లాంచ్ కాన్ఫిగరేషన్లను ఉపయోగించండి.

**ఎంపిక 2: టెర్మినల్ ఉపయోగించి**

టోకెన్ ను యాక్సెస్ వాతావరణ మార్పిడిగా సెట్ చేయండి:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ఉదాహరణలు నడపండి

**VS Code ఉపయోగించి:** Explorer లో ఏ డెమో ఫైల్ పై రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోండి లేదా రన్ & డీబగ్ ప్యానెల్ యొక్క లాంచ్ కాన్ఫిగరేషన్లను ఉపయోగించండి (ముందుగా మీ టోకెన్ `.env` ఫైల్ లో జోడించండి).

**Maven ఉపయోగించి:** లేకపోతే, మీరు క‌మాండ్ లైన్ నుండి నడపవచ్చు:

### 1. బేసిక్ చాట్

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

జీరో-షాట్, ఫ్యూ-షాట్, చైన్-ఆఫ్-థాట్, మరియు పాత్ర ఆధారిత ప్రాంప్టింగ్ చూపిస్తుంది.

### 3. ఫంక్షన్ కాలింగ్

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI అవసరమైనప్పుడు ఆటోమాటిగ్గా మీ Java పద్ధతులను పిలుస్తుంది.

### 4. డాక్యుమెంట్ ప్రశ్నలు మరియు సమాధానాలు (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` లోని కంటెంట్ పై ప్రశ్నలు అడగండి.

### 5. బాధ్యతాయుత AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI సేఫ్టీ ఫిల్టర్ల వల్ల హానికర కంటెంట్ ను ఎలా నిరోధిస్తాయో చూడండి.

## ప్రతి ఉదాహరణ ఏం చూపిస్తుంది

**బేసిక్ చాట్** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j యొక్క అత్యంత మూలభూతం ఎలా ఉందో ఇక్కడ చూడండి. మీరు `OpenAiOfficialChatModel` ని క్రియేట్ చేసి, `.chat()` తో ప్రాంప్ట్ పంపించి, స్పందన పొందుతారు. ఇది క్రియేట్ చేయడం, కస్టమ్ ఎండ్‌పాయింట్స్ మరియు API కీలు ఉపయోగించే పద్ధతులు చూపిస్తుంది. ఈ నమూనా అర్థం చేసుకున్న తర్వాత, మిగతా భాగాలు దీని మీద ఆధారపడి ఉంటాయి.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ని తెరిచి అడగండి:
> - "ఈ కోడ్ లో GitHub మోడల్స్ నుండి Azure OpenAI కి ఎలా మారుతాను?"
> - "OpenAiOfficialChatModel.builder() లో నేను మరే ఇతర పారామీటర్లు సెట్ చేయగలను?"
> - "పూర్తి స్పందన కోసం గుడుసుకోవడం కాకుండా స్ట్రీమింగ్ ప్రతిస్పందనలు ఎలా జోడించాలి?"

**ప్రాంప్ట్ ఇంజనీరింగ్** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

మీరు మీకు ఒక మోడల్ తో ఎలా మాట్లాడాలో తెలుసుకున్న తర్వాత, మీరు దానికి ఏమి చెప్పాలో తెలుసుకుందాం. ఈ డెమో ఒకే మోడల్ సెట్ ఉపయోగించి ఐదు వేర్వేరు ప్రాంప్ట్ నమూనాలను చూపిస్తుంది. సూటి స్పష్టమైన సూచనల కోసం జీరో-షాట్, ఉదాహరణలనుంచి నేర్చుకునే ఫ్యూ-షాట్, తార్కిక దశలను ప్రకటించే చైన్-ఆఫ్-థాట్, మరియు సందర్భాన్ని నిర్ణయించే పాత్ర ఆధారిత ప్రాంప్ట్‌లు చూడండి. మీరు ఎలా అభ్యర్థనను ఇచ్చారో ఆధారంగా అదే మోడల్ ఎలా భిన్న ఫలితాలు ఇస్తుందో చూడగలరు.

డెమోలో ప్రాంప్ట్ టెంప్లేట్లు కూడా చూపబడతాయి, ఇవి మార్పిడి చేయదగిన ప్రాంప్ట్‌లను సృష్టించడానికి శక్తివంతమైన మార్గం.

క్రింది ఉదాహరణ LangChain4j `PromptTemplate` ఉపయోగించి వేరియబుల్స్ నింపటాన్ని చూపిస్తోంది. AI ఇచ్చిన గమ్యం మరియు కార్యకలాపాల ఆధారంగా జవాబు ఇస్తుంది.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) తెరిచి అడగండి:
> - "జీరో-షాట్ మరియు ఫ్యూ-షాట్ prompting లో తేడా ఏమిటి, ఏది ఎప్పుడు ఉపయోగించాలి?"
> - "మోడల్ ప్రతిస్పందనలపై తాపన పరిమాణం ఎలా ప్రభావితం చేస్తుంది?"
> - "ప్రాంప్ట్ ఇంజెక్షన్ దాడులను ఆపడానికి కొన్ని సాంకేతికాలు ఏమిటి?"
> - "సాధారణ నమూనాల కోసం ఎలా పునర్వినియోగపరచదగిన PromptTemplate ఆబ్జెక్టులను సృష్టించగలను?"

**టూల్ ఇంటిగ్రేషన్** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ఇక్కడ LangChain4j శక్తివంతంగా మారుతుంది. మీరు `AiServices` ఉపయోగించి AI సహాయకుడును సృష్టిస్తారు, ఇది మీ Java పద్ధతులను పిలవగలదు. మీ పద్ధతులను `@Tool("వివరణ")` తో గుర్తించండి, LangChain4j మిగతా పనిని నిర్వహిస్తుంది - AI యూజర్ అడిగిన విషయాల ఆధారంగా ప్రతి టూల్ ను ఎప్పుడు ఉపయోగించాలో నిర్ణయిస్తుంది. ఇది ఫంక్షన్ కాలింగ్ ను చూపిస్తుంది, అంటే AI చర్యలు తీసుకునేందుకు, అదేవిధంగా ప్రశ్నలకు సమాధానాలు ఇవ్వడమే కాకుండా.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) తెరిచి అడగండి:
> - "@Tool అర్థం ఏమిటి, LangChain4j దానితో వెనుకబడి ఏమి చేస్తుంది?"
> - "AI జంటగా పలు టూల్స్ ని పిలవగలదా క్లిష్ట సమస్యలను పరిష్కరించడానికి?"
> - "ఎలాంటి టూల్ తప్పిదం విసిరినప్పుడు - నేను దానిని ఎలా నిర్వహించాలి?"
> - "ఈ క్యాల్క్యులేటర్ ఉదాహరణ కన్నా వాస్తవ API ఎలా ఇంటిగ్రేట్ చేసుకోవచ్చు?"

**డాక్యుమెంట్ ప్రశ్నలు మరియు సమాధానాలు (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ఇక్కడ మీరు RAG (రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్) ప్రాథమికాన్ని చూడగలరు. మోడల్ శిక్షణ డేటాపై ఆధారపడకుండా, మీరు [`document.txt`](../../../00-quick-start/document.txt) నుండి కంటెంట్ లోడ్ చేసి, ప్రాంప్ట్ లో చేర్చతారు. AI మీ డాక్యుమెంట్ ఆధారంగా జవాబు ఇస్తుంది, సాధారణ జ్ఞానం ఆధారంగా కాదు. ఇది మీ డేటాతో పనిచేసే వ్యవస్థలను నిర్మించడం కోసం ప్రథమ దశ.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **గమనిక:** ఈ సరళమైన పద్ధతి దానికి డాక్యుమెంట్ మొత్తాన్నీ ప్రాంప్ట్ లో లోడ్ చేస్తుంది. పెద్ద ఫైళ్ల (10KBకు మించి) కోసం, మీరు కాంటెక్స్ట్ పరిమితులను దాటిపోతారు. మాడ్యూల్ 03‌లో ప్రొడక్షన్ RAG వ్యవస్థలకు చంకింగ్ మరియు వెక్టర్ సెర్చ్ ను కవర్ చేస్తారు.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) తెరిచి అడగండి:
> - "RAG AI హలుసినేషన్లను ఎలా నివారిస్తుంది, మోడల్ శిక్షణ డేటాతో పోల్చితే?"
> - "ఈ సరళమైన పద్ధతి మరియు వెక్టర్ ఎంబెడింగ్స్ ద్వారా రిట్రీవల్ మధ్య తేడా ఏమిటి?"
> - "నేను దీన్ని పలు డాక్యుమెంట్లు లేదా పెద్ద జ్ఞానానికి ఎలా పెంచగలను?"
> - "AI ను కేవలం అందించిన సందర్భాన్ని ఉపయోగించడానికి ప్రాంప్ట్ ని ఎలా నిర్మించాలి?"

**బాధ్యతాయుత AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

సురక్షిత AI ఆస్థాయిలతో రూపొందించండి. ఈ డెమో రెండు రక్షణ పొరలను చూపిస్తుంది:

**భాగం 1: LangChain4j ఇన్‌పుట్ గార్డ్రైల్స్** - LLM చేరనుండనే ప్రమాదకర ప్రాంప్ట్‌లను బ్లాక్ చేస్తుంది. నిషేధిత కీవర్డ్లు లేదా నమూనాలు కోసం మీ కోడ్ లో ప్రత్యేక గార్డ్రైల్స్ సృష్టించండి. ఇవి వేగంగా, ఉచితంగా నడుస్తాయి.

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

**భాగం 2: ప్రొవైడర్ సేఫ్టీ ఫిల్టర్లు** - GitHub Models లో బిల్ట్-ఇన్ ఫిల్టర్లు ఉన్నాయి, ఇవి మీ గార్డ్రైల్స్ కనిపించని వాటిని పట్టుకుంటాయి. మీరు గట్టి బ్లాక్స్ (HTTP 400 లోపాలు) మరియు మృదువైన తిరస్కారాలను చూస్తారు, ఇక్కడ AI politely నిరాకరిస్తుంది.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) తెరిచి అడగండి:
> - "InputGuardrail అంటే ఏమిటి మరియు నేను ఎలా నా స్వంతది సృష్టించాలి?"
> - "గట్టి బ్లాక్ మరియు మృదువు తిరస్కారం మధ్య తేడా ఏమిటి?"
> - "ఇక సామాన్యంగా గార్డ్రైల్స్ మరియు ప్రొవైడర్ ఫిల్టర్లను కలిపి ఎందుకు ఉపయోగిస్తారు?"

## తర్వాతి దశలు

**తర్వాతి మాడ్యూల్:** [01-introduction - LangChain4j మరియు gpt-5 తో Azure పై ప్రారంభించడం](../01-introduction/README.md)

---

**నావిగేషన్:** [← మెయిన్ కు తిరుగు](../README.md) | [తర్వాత: మాడ్యూల్ 01 - పరిచయం →](../01-introduction/README.md)

---

## సమస్య పరిష్కారం

### మొదటి సారి Maven బిల్డ్

**సమస్య**: మొదటి సారి `mvn clean compile` లేదా `mvn package` చాలా సమయం (10-15 నిమిషాలు) తీసుకుంటుంది

**కారణం**: Maven అన్ని ప్రాజెక్ట్ ఆధారాలను (Spring Boot, LangChain4j లైబ్రరీలు, Azure SDKలు మొదలైనవి) మొదటిసారి బిల్డు సమయంలో డౌన్‌లోడ్ చేయాలి.

**పరిష్కారం**: ఇది సాధారణ వాతావరణం. తర్వాత బిల్డ్లు వేగంగా జరుగుతాయి ఎందుకంటే Dependencies లోకల్ గా నిల్వ అవుతాయి. డౌన్‌లోడ్ సమయం మీ ఇంటర్నెట్ స్పీడ్ మీద ఆధారపడి ఉంటుంది.
### PowerShell Maven కమాండ్ శబ్దరసం

**సమస్య**: Maven కమాండ్లు `Unknown lifecycle phase ".mainClass=..."` అనే లోపంతో విఫలమవుతాయి

**కారణం**: PowerShell `=` ను వేరియబుల్ అసైన్‌మెంట్ ఆపరేటర్‌గా పిలచటం వల్ల Maven ప్రాపర్టీ శబ్దరసం పగిలిపోతుంది

**పరిష్కారం**: Maven కమాండ్ ముందు స్టాప్-పార్సింగ్ ఆపరేటర్ `--%` వాడండి:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ఆపరేటర్ PowerShell కు Maven కు మిగిలిన అవర్గాలను లిటరల్‌గా, ఎప్పటికీ అన్వయించకుండా అందజేస్తుంది.

### Windows PowerShell ఎమోజీ ప్రదర్శన

**సమస్య**: PowerShell లో AI ప్రతిస్పందనలు ఎమోజీల స్థానంలో జంక్ అక్షరాలు (ఉదా: `????` లేదా `â??`) చూపించడం

**కారణం**: PowerShell యొక్క డిఫాల్ట్ ఎన్‌కోడ్ UTF-8 ఎమోజీలు మద్దతు ఇవ్వదు

**పరిష్కారం**: Java యాప్లికేషన్‌లు అమలు చేసే ముందు ఈ కమాండ్ నడపండి:
```cmd
chcp 65001
```

ఇది టెర్మినల్‌లో UTF-8 ఎన్‌కోడ్‌ను బలవంతం చేస్తుంది. దాదాపుగా మెరుగైన యూనికోడ్ మద్దతు ఉన్న Windows Terminal ను ఉపయోగించడం కూడా పనిచేస్తుంది.

### API కాల్స్ డీబగ్గింగ్

**సమస్య**: ప్రామాణీకరణ లోపాలు, రేటు పరిమితులు లేదా అనుకోని AI నమూనా స్పందనలు

**పరిష్కారం**: ఈ ఉదాహరణలు `.logRequests(true)` మరియు `.logResponses(true)` కలిగి ఉంటాయి, ఇవి API కాల్స్‌ను కన్సోల్‌లో చూపిస్తాయి. ఇది ప్రామాణీకరణ లోపాలు, రేటు పరిమితులు లేదా అనుకోని స్పందనలను డీబగ్గింగ్ చేయడంలో సహాయపడుతుంది. ప్రొడక్షన్‌లో ఈ ఫ్లాగ్‌లను తీసివేయడం ద్వారా లాగ్ శబ్దం తగ్గుతుంది.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టం**:  
ఈ పత్రాన్ని AI అనువాద సేవ [కో-ఆప్ ట్రాన్స్లేటర్](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము శుద్ధతకు ప్రయత్నించినప్పటికీ, ఆటోమేటెడ్ అనువాదాలలో తప్పులు లేదా అభిప్రాయం భిన్నతలు ఉండవచ్చు. మాతృ భాషలో ఉన్న మౌలిక పత్రాన్ని అధికారిక సూత్రంగా పరిగణించవలెనని సూచించబడింది. ముఖ్యమైన సమాచారం కోసం, నిపుణుల చేతి అనువాదం మంచి ఎంపిక. ఈ అనువాదం ఉపయోగం ద్వారా ఏర్పడే ఏదైనా అపార్థం లేదా తప్పు అర్థం మాకు బాధ్యతగా భావించబడదు.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->