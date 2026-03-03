# మాడ్యూల్ 00: త్వరిత ప్రారంభం

## విషయ సూచిక

- [పరిచయం](../../../00-quick-start)
- [LangChain4j అంటే ఏమిటి?](../../../00-quick-start)
- [LangChain4j ఆధారాలు](../../../00-quick-start)
- [అవసరాలు](../../../00-quick-start)
- [సెట్టప్](../../../00-quick-start)
  - [1. మీ GitHub టోకెన్ పొందండి](../../../00-quick-start)
  - [2. మీ టోకెన్ సెట్ చేయండి](../../../00-quick-start)
- [ఉదాహరణలను నడపండి](../../../00-quick-start)
  - [1. ప్రాథమిక చాట్](../../../00-quick-start)
  - [2. ప్రాంప్ట్ నమూనాలు](../../../00-quick-start)
  - [3. ఫంక్షన్ కాలింగ్](../../../00-quick-start)
  - [4. డాక్యుమెంట్ ప్రశ్నోత్తరాలు (సులభమైన RAG)](../../../00-quick-start)
  - [5. బాధ్యతాయుత AI](../../../00-quick-start)
- [ప్రతి ఉదాహరణ ఏమి చూపిస్తుంది](../../../00-quick-start)
- [తరువాతి దశలు](../../../00-quick-start)
- [సమస్య పరిష్కారం](../../../00-quick-start)

## పరిచయం

ఈ త్వరితప్రారంభం LangChain4j తో మీరు వీక్షించేందుకు మరియు నిర్దేశించేందుకు గడువు వేస్తుంది. ఇది LangChain4j మరియు GitHub Models తో AI అప్లికేషన్‌లు నిర్మిస్తున్న అత్యంత ప్రాథమిక విషయాలు కవర్ చేస్తుంది. తదుపరి మాడ్యూల్స్‌లో మీరు Azure OpenAI మరియు GPT-5.2 కు మార్చుకుంటారు మరియు ప్రతి భావనలో లోతుగా జారతారు.

## LangChain4j అంటే ఏమిటి?

LangChain4j అనేది AI-శక్తివంతమైన అప్లికేషన్‌ల నిర్మాణాన్ని సులభతరం చేసే జావా లైబ్రరీ. HTTP క్లయింట్లు మరియు JSON అనలిసిస్ చేయాల్సిన అవసరం లేకుండా, మీరు శుచిగా ఉన్న జావా APIలతో పనిచేస్తారు.

LangChainలో "చైన్" అనేది అనేక భాగాలు కలిపే క్రమం - మీరు ఒక ప్రాంప్ట్ నుండి మోడల్, తరువాత పార్సర్ కు లేదా అనేక AI పిలుపులు కలిపి ఒకౌట్‌పుట్ తదుపరి ఇన్‌పుట్‌కి ఇచ్చే రూపంలో కలిపే విధంగా ఉంటుంది. ఈ త్వరితప్రారంభం కాంప్లెక్స్ చైన్లు అన్వేషించడానికి మునుపుగా ప్రాథమిక విషయాలు మీద దృష్టి సారిస్తుంది.

<img src="../../../translated_images/te/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4jలో భాగాలను చైన్ చేయడం - శక్తివంతమైన AI పనిముట్లు సృష్టించడానికి బ్లాకులు కలిపే విధానం*

మేము మూడు ముఖ్యమైన భాగాలను ఉపయోగిస్తాము:

**ChatModel** - AI మోడల్ ఇంటరాక్షన్ ఇంటర్ఫేస్. `model.chat("prompt")` ను పిలిచి ఒక ప్రతిస్పందన స్ట్రింగ్ పొందండి. మేము `OpenAiOfficialChatModel` ఉపయోగిస్తాము, ఇది GitHub Models వంటి OpenAI-అనుకూల ఎండ్పాయింట్లతో పని చేస్తుంది.

**AiServices** - టైపు-భద్రమయి AI సర్వీస్ ఇంటర్ఫేస్‌ను సృష్టిస్తుంది. విధానాలను నిర్వచించండి, వాటిని `@Tool` తో ట్యాగ్ చేయండి, LangChain4j మొత్తం ఒర్పెస్ట్రేషన్‌ను హ్యాండిల్ చేస్తుంది. అవసరం వచ్చినప్పుడు AI మీ జావా పద్ధతులను ఆటోమేటిక్ పిలుస్తుంది.

**MessageWindowChatMemory** - సంభాషణ చరిత్రను నిలుపుతుంది. దీని లేకుండా ప్రతి అభ్యర్థన స్వతంత్రమైనది. దీనితో AI ముందు సందేశాలను గుర్తుంచుకుంటూ సంగతులను అనుసరిస్తుంది.

<img src="../../../translated_images/te/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j నిర్మాణం - ముఖ్య భాగాలు కలిసి మీ AI అప్లికేషన్ లకు శక్తి అందజేస్తాయి*

## LangChain4j ఆధారాలు

ఈ త్వరితప్రారంభం మూడు Maven ఆధారాలను [`pom.xml`](../../../00-quick-start/pom.xml) లో ఉపయోగిస్తుంది:

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

`langchain4j-open-ai-official` మాడ్యూల్ `OpenAiOfficialChatModel` క్లాస్ అందిస్తుంది, ఇది OpenAI అనుకూల APIs తో కనెక్ట్ అవుతుంది. GitHub Models కూడా అదే API ఫార్మాట్ ఉపయోగిస్తుంది, కాబట్టి ప్రత్యేక అడాప్టర్ అవసరం లేదు - బేస్ URLను `https://models.github.ai/inference` గా పాయింట్ చేయండి.

`langchain4j-easy-rag` మాడ్యూల్ ఆటోమేటిక్ డాక్యుమెంట్ విభజన, ఎంబెడింగ్ మరియు రిట్రీవల్ అందిస్తుంది, అందువల్ల మీరు RAG అప్లికేషన్‌లను మానవీయంగా ప్రతీ దశను నిర్దేశించకుండా నిర్మించవచ్చు.

## అవసరాలు

**Dev Container ఉపయోగిస్తున్నారా?** జావా మరియు మావెన్ ఇప్పటికే ఇన్‌స్టాల్ చేయబడ్డాయి. మీరు GitHub వ్యక్తిగత యాక్సెస్ టోకెన్ మాత్రమే అవసరం.

**లోకల్ అభివృద్ధి:**
- జావా 21+, మావెన్ 3.9+
- GitHub వ్యక్తిగత యాక్సెస్ టోకెన్ (కింద ఇచ్చిన సూచనలు)

> **గమనిక:** ఈ మాడ్యూల్ GitHub Models నుండి `gpt-4.1-nano` ను ఉపయోగిస్తుంది. కోడ్‌లో మోడల్ పేరును మార్చవద్దు - ఇది GitHub అందుబాటులో ఉన్న మోడల్స్‌తో పనిచేసే విధంగా కాన్ఫిగర్ చేయబడింది.

## సెట్‌అప్

### 1. మీ GitHub టోకెన్ పొందండి

1. [GitHub సెట్టింగ్స్ → వ్యక్తిగత యాక్సెస్ టోకెన్లు](https://github.com/settings/personal-access-tokens) కి వెళ్లండి  
2. "Generate new token" పై క్లిక్ చేయండి  
3. వివరణాత్మక పేరు సెట్ చేయండి (ఉదాహరణకు, "LangChain4j Demo")  
4. గడువును సెట్ చేయండి (7 రోజులు సిఫార్సు)  
5. "ఖాతా అనుమతులు" కింద "Models" ను "Read-only" గా సెట్ చేయండి  
6. "Generate token" పై క్లిక్ చేయండి  
7. మీ టోకెన్ ను కాపీ చేసి సేవ్ చేసుకోండి - మీరు మళ్లీ చుడలేనిది  

### 2. మీ టోకెన్ సెట్ చేయండి

**ఎంపిక 1: VS Code ఉపయోగించటం (సిఫార్సు చేయబడింది)**

మీరు VS Code ఉపయోగిస్తే, ప్రాజెక్టు రూట్ లో `.env` ఫైల్‌లో మీ టోకెన్ ను జోడించండి:

`.env` ఫైల్ లేని పక్షంలో, `.env.example` నుండి `.env` కు కాపీ చేయండి లేదా కొత్త `.env` ఫైల్ సృష్టించండి.

**ఉదాహరణ `.env` ఫైల్:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env లో
GITHUB_TOKEN=your_token_here
```

తర్వాత మీరు ఎక్స్ప్లోరర్‌లో ఏ డెమో ఫైలుపై అయినా (ఉదాహరణకు `BasicChatDemo.java`) రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోవచ్చు లేదా Run & Debug ప్యానెల్ నుండి లాంఛ్ కాన్ఫిగరేషన్లను ఉపయోగించవచ్చు.

**ఎంపిక 2: టెర్మినల్ ఉపయోగించడం**

టోకెన్ ను ఎన్విరాన్‌మెంట్ వేరియబుల్‌గా సెట్ చేయండి:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ఉదాహరణలను నడపండి

**VS Code ఉపయోగిస్తున్నప్పుడు:** ఎక్స్ప్లోరర్‌లో ఏ డెమో ఫైల్‌ను అయినా రైట్ క్లిక్ చేసి **"Run Java"** ఎంచుకోండి, లేదా ముందుగా `.env` ఫైల్‌లో టోకెన్ జోడించి Run & Debug ప్యానెల్ లో లాంఛ్ కాన్ఫిగరేషన్లను ఉపయోగించండి.

**Maven ఉపయోగించి:** లేదా, కింది కమాండ్ లైన్ నుంచి నడిపించండి:

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

జీరో-షాట్, ఫ్యూషాట్, చైన్-ఆఫ్-తొట, మరియు పాత్ర-ఆధారిత ప్రాంప్టింగ్ చూపిస్తుంది.

### 3. ఫంక్షన్ కాలింగ్

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI అవసరమనుకుంటే ఆటోమేటిక్ గా మీ జావా మెథడ్స్ పిలుస్తుంది.

### 4. డాక్యుమెంట్ ప్రశ్నోత్తరాలు (సులభమైన RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ఆటోమేటిక్ ఎంబెడింగ్ మరియు రిట్రీవల్ తో Easy RAG ఉపయోగించి మీ డాక్యుమెంట్లకు సంబంధించిన ప్రశ్నలను అడగండి.

### 5. బాధ్యతాయుత AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI సేఫ్టీ ఫిల్టర్లు హానికర కంటెంట్ ని ఎలా నిరోధిస్తాయో చూడండి.

## ప్రతి ఉదాహరణ ఏమి చూపిస్తుంది

**ప్రాథమిక చాట్** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ఇక్కడ LangChain4j యొక్క అత్యంత సరళమైన రూపాన్ని చూడండి. మీరు ఒక `OpenAiOfficialChatModel` సృష్టించి, `.chat()` తో ఒక ప్రాంప్ట్ పంపించి, స్పందన పొందగలిగే విధంగా ఉంటుంది. ఇది పునాదులను చూపిస్తుంది: కస్టమ్ ఎండ్పాయింట్లు మరియు API కీలు ఉపయోగించి మోడల్స్‌ను స్వయంచాలకంగా ప్రారంభించడం. ఈ నమూనా అర్థమైతే, మిగతా ప్రతీది దీని మీద ఆధారపడి ఉంటుంది.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) తెరవండి మరియు అడగండి:  
> - "GitHub Models నుండి Azure OpenAI కి ఈ కోడ్‌లో ఎలా మార్చాలి?"  
> - "OpenAiOfficialChatModel.builder() లో మరే ఇతర ప్యారామీటర్లను నాను సెట్ చేయవచ్చు?"  
> - "పూర్తి స్పందన కోసం వేచి ఉండకుండా స్ట్రీమింగ్ స్పందనలను ఎలా జోడించాలి?"  

**ప్రాంప్ట్ ఇంజనీరింగ్** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ఇప్పుడు మీరు మోడల్‌తో ఎలా మాట్లాడాలో తెలుసుకున్న తర్వాత, మీరు దానికి ఏమి చెప్పాలో అన్వేషించుకుందాం. ఈ డెమో అదే మోడల్ సెటప్‌ను ఉపయోగించి ఐదు వేర్వేరు ప్రాంప్టింగ్ నమూనాలను చూపిస్తుంది. నేరుగా సూచనలు కోసం జీరో-షాట్ ప్రాంప్ట్లు, ఉదాహరణల నుంచి నేర్చుకునే ఫ్యూషాట్ ప్రాంప్ట్‌లు, తర్క సాధన చర్యలను వెల్లడి చేసే చైన్-ఆఫ్-తోట్ ప్రాంప్ట్లు, మరియు సందర్భాన్ని సేత్ చేసే పాత్ర-ఆధారిత ప్రాంప్ట్‌లు చూడండి. ఒకే మోడల్ అడిగే విధానాన్ని బట్టి ఎంత భిన్నమైన ఫలితాలు ఇస్తుంది గమనించండి.

డెమో ప్రాంప్ట్ టెంప్లేట్లను కూడా చూపిస్తుంది, ఇవి వేరియబుల్స్ తో రీయూజబుల్ ప్రాంప్ట్‌లు సృష్టించడానికి శక్తివంతమైన పద్ధతి.

కింద ఇచ్చిన ఉదాహరణలో LangChain4j `PromptTemplate` ఉపయోగించి వేరియబుల్స్ నింపడం చూపిస్తుంది. AI ఇచ్చిన గమ్యం మరియు కార్యకలాపం ఆధారంగా జవాబు ఇస్తుంది.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) తెరవండి మరియు అడగండి:  
> - "జీరో-షాట్ మరియు ఫ్యూషాట్ ప్రాంప్టింగ్ మధ్య తేడా ఏంటి, ఏది ఎప్పుడు ఉపయోగించాలి?"  
> - "టెంపరేచర్ ప్యారామీటర్ ఎలా మోడల్ స్పందనలను ప్రభావితం చేస్తుంది?"  
> - "ప్రొడక్షన్‌లో ప్రాంప్ట్ ఇంజక్షన్ దాడులను ఎలా నివారించాలి?"  
> - "సాధారణ నమూనాల కోసం రీయూజబుల్ PromptTemplate వస్తువులను ఎలా సృష్టించాలి?"  

**టూల్ ఇంటిగ్రేషన్** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ఇక్కడ LangChain4j శక్తివంతం అవుతుంది. మీరు `AiServices` ఉపయోగించి మీ జావా పద్ధతులను పిలవగల AI సహాయకుని సృష్టిస్తారు. వికలంగా, `@Tool("description")` తో మెథడ్స్‌ను ట్యాగ్ చేయండి మరియు LangChain4j మిగత దాన్ని నిర్వహిస్తుంది - AI యూజర్ అడిగిన దాని ఆధారంగా ఏ టూల్ ఉపయోగించాలో నిర్ణయిస్తుంది. ఇది పంక్షన్ కాలింగ్‌ను చూపిస్తుంది, ఇది ప్రశ్నలకు మాత్రమే కాక చర్యలు చేయగల AI సృష్టించడానికి ముఖ్యమైన సాంకేతికత.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) తెరవండి మరియు అడగండి:  
> - "@Tool అనోటేషన్ ఎలా పనిచేస్తుంది మరియు LangChain4j వెనుక దాన్ని ఎలా నిర్వహిస్తుంది?"  
> - "AI పలు టూల్స్‌ను వరుసగా పిలిచి సంక్లిష్ట సమస్యలను ఎలా పరిష్కరిస్తుంది?"  
> - "టూల్ ఎర్రర్‌లు చోటుచేసుకుంటే నేను ఎలా హ్యాండిల్ చేయాలి?"  
> - "ఈ కాలిక్యులేటర్ ఉదాహరణకు బదులుగా వాస్తవ APIని ఎలా ఇంటిగ్రేట్ చేయాలి?"  

**డాక్యుమెంట్ ప్రశ్నోత్తరాలు (సులభమైన RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ఇక్కడ మీరు LangChain4j "Easy RAG" విధానం ఉపయోగించి RAG (రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్) చూడగలరు. డాక్యుమెంట్లు లోడ్ అవుతాయి, ఆటోమేటిక్ గా విడగొట్టబడతాయి మరియు మెమరీలోని స్టోర్‌లో ఎంబెడ్ చేయబడతాయి, ఆపై కంటెంట్ రిట్రీవర్ ప్రాస్న సమయానికి సంబంధిత భాగాలను AI కి అందిస్తుంది. AI మీ డాక్యుమెంట్ల ఆధారంగా సమాధానాలు ఇస్తుంది, దాని సాధారణ జ్ఞానంపై కాకుండా.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) తెరవండి మరియు అడగండి:  
> - "AI హల్యూసినేషన్లను RAG ఎలా నివారిస్తుంది, మోడల్ ట్రైనింగ్ డేటాతో పోల్చితే?"  
> - "ఈ సులభ పద్ధతి మరియు కస్టమ్ RAG పైప్లిన్ మధ్య తేడా ఏమిటి?"  
> - "ఈ విధానాన్ని బహుళ డాక్యుమెంట్లు లేదా పెద్ద జ్ఞాన ఆధారాలు కోసం ఎలా పెంచుకోవచ్చు?"  

**బాధ్యతాయుత AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

రక్షణలో మందగింపు కలిగించి AI సేఫ్టీని నిర్మించండి. ఈ డెమో రెండు పరతుల రక్షణను కలిపి చూపిస్తుంది:

**భాగం 1: LangChain4j ఇన్‌పుట్ గార్డ్‌రెయిల్స్** - LLM కి చేరకముందే ప్రమాదకర ప్రాంప్ట్‌లను బ్లాక్ చేయండి. నిషేధిత కీవర్డ్స్ లేదా నమూనాలను తనిఖీ చేసే కస్టమ్ గార్డ్‌రెయిల్స్ సృష్టించండి. ఇవి మీ కోడ్‌లో నడుస్తాయి కాబట్టి త్వరగా మరియు ఉచితంగా పనిచేస్తాయి.

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

**భాగం 2: ప్రొవైడర్ సేఫ్టీ ఫిల్టర్స్** - GitHub Models లో బిల్ట్-ఇన్ ఫిల్టర్లు ఉన్నాయి, ఇవి మీ గార్డ్‌రెయిల్స్ తప్పిపోయే వాటిని పట్టుకుంటాయి. తీవ్రమైన ఉల్లంఘనలకు హార్డ్ బ్లాక్స్ (HTTP 400 ఎర్రర్లు) మరియు మంచి సౌమ్యంగా నిరాకరణల రూపంలో సాఫ్ట్ రిఫ్యూజల్స్ ఉంటాయి.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) చాట్ తో ప్రయత్నించండి:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) తెరవండి మరియు అడగండి:  
> - "InputGuardrail అంటే ఏమిటి మరియు నేను ఎలా నా సొంత వాటిని సృష్టించవచ్చు?"  
> - "హార్డ్ బ్లాక్ మరియు సాఫ్ట్ రిఫ్యూజల్ మధ్య తేడా ఏమిటి?"  
> - "ఎందుకు గార్డ్‌రెయిల్స్ మరియు ప్రొవైడర్ ఫిల్టర్స్ రెండింటినీ కలిపి వాడాలి?"  

## తర్వాతి దశలు

**తరువాతి మాడ్యూల్:** [01-పరిచయం - LangChain4j తో ప్రారంభం](../01-introduction/README.md)

---

**నావిగేషన్:** [← మెయిన్‌కు తిరిగి](../README.md) | [తరువాత: మాడ్యూల్ 01 - పరిచయం →](../01-introduction/README.md)

---

## సమస్య పరిష్కారం

### మొదటి సారి Maven బిల್ಡ್

**సమस्या**: మొదటి `mvn clean compile` లేదా `mvn package` చాలా సమయం తీసుకుంటుంది (10-15 నిమిషాలు)

**కారణం**: Maven మొదటి సారి అన్ని ప్రాజెక్టు ఆధారాలు (Spring Boot, LangChain4j లైబ్రరీలు, Azure SDKలు మొదలైనవి) డౌన్లోడ్ చేయాల్సి ఉంటుంది.

**పరిష్కారం**: ఇది సాధారణ ప్రవర్తన. తరువాతి బిల్డ్స్ చాలా వేగంగా ఉంటాయి ఎందుకంటే ఆధారాలను స్థానికంగా క్యాష్ చేస్తుంది. డౌన్లోడ్ సమయము మీ నెట్‌వర్క్ వేగం మీద ఆధారపడి ఉంటుంది.

### PowerShell Maven కమాండ్ సింటాక్స్

**సమస్య**: Maven కమాండ్లు `Unknown lifecycle phase ".mainClass=..."` అనే తప్పిదంతో విఫలమవుతాయి
**కారణం**: PowerShell `=` ను వేరియబుల్ అసైన్‌మెంట్ ఆపరేటర్‌గా గ్రహించి, Maven ప్రాపర్టీ సింటాక్స్‌ను విరుచుకుంటుంది

**పరిష్కారం**: Maven కమాండ్ ముందు `--%` స్టాప్-పార్సింగ్ ఆపరేటర్‌ను ఉపయోగించండి:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ఆపరేటర్ PowerShellకి మిగిలిన ఆర్గుమెంట్స్‌ను Mavenకు అనువాదం లేకుండా సరిగ్గా გადასరించమని సూచిస్తుంది.

### Windows PowerShell ఎమోజీ ప్రదర్శన

**సమస్య**: PowerShellలో AI ప్రతిస్పందనలు ఎమోజీల స్థానంలో నాశనం అయిన అక్షరాలు (ఉదా: `????` లేదా `â??`) చూపించాయి

**కారణం**: PowerShell యొక్క డిఫాల్ట్ ఎన్‌కోడింగ్ UTF-8 ఎమోజీలను మద్దతు ఇవ్వదు

**పరిష్కారం**: Java అప్లికేషన్లు నడుపు ముందు ఈ కమాండ్‌ను నడపండి:
```cmd
chcp 65001
```

ఇది టెర్మినల్‌లో UTF-8 ఎన్‌కోడింగ్‌ను బలవంతం చేస్తుంది. మరోవైపు, మెరుగైన యూనికోడ్ మద్దతు ఉన్న Windows Terminalను వాడండి.

### API కాల్లను డీబగ్ చేయడం

**సమస్య**: AI మోడల్ నుండి ఆథెంటికేషన్ లోపాలు, రేట్ లిమిట్స్, లేక అనుకోని ప్రతిస్పందనలు

**పరిష్కారం**: ఉదాహరణల్లో `.logRequests(true)` మరియు `.logResponses(true)` ఉన్నాయి, ఇవి API కాల్లను కన్సోల్‌లో చూపిస్తాయి. ఇది ఆథెంటికేషన్ లోపాలు, రేట్ లిమిట్స్, లేదా అనుకోని ప్రతిస్పందనలను అన్వేషించడంలో సహాయపడుతుంది. ఉత్పత్తిలో ఈ ఫ్లాగ్లను తీసివేసి లాగ్ శబ్దాన్ని తగ్గించండి.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ఉపసంహారం**:
ఈ పత్రాన్ని AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ను ఉపయోగించి అనువదించాము. సరైన అనువాదానికి ప్రయత్నించినప్పటికీ, ఆగమికార అనువాదాలలో తప్పులూ లేదా అసమర్థతలూ ఉండకూడదు. స్థానిక భాషలో ఉన్న అసలు పత్రం ప్రామాణిక మూలంగా తీసుకోవాలి. ముఖ్యమైన సమాచారానికి వృత్తిపరమైన మానవ అనువాదం చేయించడం మేలైంది. ఈ అనువాదం వాడుక వల్ల కలిగే ఏదైనా తప్పు అర్థగర్భితం లేదా పొరపాటులకు మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->