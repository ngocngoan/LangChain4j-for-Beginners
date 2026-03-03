# Module 00: விரைவு துவக்கம்

## உள்ளடக்க பட்டியல்

- [அறிமுகம்](../../../00-quick-start)
- [LangChain4j என்றால் என்ன?](../../../00-quick-start)
- [LangChain4j சார்புகள்](../../../00-quick-start)
- [முன் தேவைகள்](../../../00-quick-start)
- [அமைப்பு](../../../00-quick-start)
  - [1. உங்கள் GitHub டோக்கனைப் பெறவும்](../../../00-quick-start)
  - [2. உங்கள் டோக்கனை அமைக்கவும்](../../../00-quick-start)
- [உதாரணங்களை இயக்கவும்](../../../00-quick-start)
  - [1. அடிப்படைக் காட்சி](../../../00-quick-start)
  - [2. கூற்றுப் பாடமுறைமை](../../../00-quick-start)
  - [3. செயல்பாடு அழைப்பு](../../../00-quick-start)
  - [4. ஆவண கேள்வி & பதில் (எளிய RAG)](../../../00-quick-start)
  - [5. பொறுப்பான AI](../../../00-quick-start)
- [ஒவ்வொரு உதாரணமும் என்ன காட்டுுகிறது](../../../00-quick-start)
- [அடுத்த படிகள்](../../../00-quick-start)
- [பிரச்சனை தீர்க்கும் வழிகள்](../../../00-quick-start)

## அறிமுகம்

இந்த விரைவு துவக்கம் LangChain4j உடன் உங்களை விரைவாகத் தொடங்க உதவுகிறது. இது LangChain4j மற்றும் GitHub மாதிரிகளோடு AI பயன்பாடுகளை கட்டுமானம் செய்வதற்கான அடிப்படைகளை உள்ளடக்கியது. அடுத்த மூடியத்தில் நீங்கள் Azure OpenAI மற்றும் GPT-5.2 க்கு மாறி ஒவ்வொரு கருத்தையும் ஆழமாக ஆராய்வீர்கள்.

## LangChain4j என்றால் என்ன?

LangChain4j என்பது AI-ஆல் இயக்கப்படும் பயன்பாடுகளை எளிதாக கட்டமைக்க உதவும் Java நூலகம். HTTP கிளையன்ட்கள் மற்றும் JSON பார்சிங் கையாள்வதைவிட நீங்கள் சுத்தமான Java APIs உடன் பணியாற்றலாம்.

LangChainஇல் "chain" என்பது பல கூறுகளை சங்கிலிப்படி இணைப்பதை குறிக்கிறது - உங்களால் ஒரு ப்ராம்ப்டை மாதிரி ஒன்றோடு, ஒரு பார்சருடன் இணைக்கலாம் அல்லது பல AI அழைப்புகளை சங்கிலி செய்யலாம், இதில் ஒரு வெளியீடு அடுத்த உள்ளீட்டுக்கு வழங்கப்படுகிறது. இந்த விரைவு துவக்கம் அடிப்படைகளைக் கவனமாக விளக்குகிறது பின்னர் சிக்கலான சங்கிலிகளை ஆராயவும்.

<img src="../../../translated_images/ta/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j சங்கிலி கருத்து" width="800"/>

*LangChain4j இல் கூறுகளை சங்கிலி செய்கிறது - கட்டுமானத் தொகுதிகள் இணைந்து சக்திவாய்ந்த AI வேலைசெய்திகளை உருவாக்குகிறது*

நாம் மூன்று முக்கிய கூறுகளைப் பயன்படுத்துவோம்:

**ChatModel** - AI மாதிரி தொடர்புக்கான இடைமுகம். `model.chat("prompt")` ஐ அழைத்து பதிலாக ஒரு சரத்தைப் பெறலாம். நாம் `OpenAiOfficialChatModel` ஐப் பயன்படுத்துகிறோம், இது GitHub Models போன்ற OpenAI-ஐ ஒத்துள்ளது என்ற APIயுடன் இயங்குகிறது.

**AiServices** - வகை-பாதுகாப்பான AI சேவை இடைமுகங்களை உருவாக்குகிறது. முறைகள் அமைக்கவும், அவற்றை `@Tool` ஆக்கமிடவும், LangChain4j ஒழுங்குபடுத்தலை கையாளும். AI தேவையான போது உங்கள் Java முறைகளை தானாக அழைக்கும்.

**MessageWindowChatMemory** - உரையாடல் வரலாறை பராமரிக்கிறது. இதை இல்லாமல் ஒவ்வொரு கோரிக்கையும் தனித்துவமானது. இதோடு, AI முந்திய செய்திகள் மற்றும் பல முறை உரையாடலில் உள்ளடக்கத்தை நினைவில் வைக்கிறது.

<img src="../../../translated_images/ta/architecture.eedc993a1c576839.webp" alt="LangChain4j கட்டமைப்பு" width="800"/>

*LangChain4j கட்டமைப்பு - முக்கிய கூறுகள் சேர்ந்து உங்கள் AI பயன்பாடுகளைக் இயக்குகின்றன*

## LangChain4j சார்புகள்

இந்த விரைவு துவக்கம் [`pom.xml`](../../../00-quick-start/pom.xml) இல் மூன்று Maven சார்புகளைப் பயன்படுத்துகிறது:

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

`langchain4j-open-ai-official` தொகுதி OpenAiOfficialChatModel வகுப்பை வழங்குகிறது, இது OpenAI-ஐ ஒத்த APIகளுடன் இணைக்கிறது. GitHub Models இதே API வடிவமைப்பை பயன்படுத்துகிறது, எனவே வேறு ஒரு தனித்துவமான அணுகு கருவி தேவையில்லை - அடிப்படை URL ஐ `https://models.github.ai/inference` என்று காட்டவும்.

`langchain4j-easy-rag` தொகுதி தானாக ஆவணங்களை பிரிதல், செருகல் மற்றும் மீட்பு செய்வதற்கு உதவுகிறது, எனவே நீங்கள் ஒவ்வொரு படியான செயல்முறையையும் கைமுறை அமைக்காமல் RAG பயன்பாடுகளை கட்டமைக்க முடியும்.

## முன் தேவைகள்

**Dev Container பயன்படுத்துகிறீர்களா?** Java மற்றும் Maven ஏற்கனவே நிறுவப்பட்டு உள்ளது. உங்களுக்கு GitHub Personal Access Token மட்டுமே தேவை.

**உள்ளூர் மேம்பாடு:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (வழிமுறைகள் கீழே)

> **குறிப்பு:** இந்த தொகுதி GitHub Models இல் உள்ள `gpt-4.1-nano`ஐப் பயன்படுத்துகிறது. கோடில் மாதிரி பெயரை மாற்ற வேண்டாம் - இது GitHub க்குப் பயன்படும் மாதிரிகளுடன் வேலை செய்ய அமைக்கப்பட்டுள்ளது.

## அமைப்பு

### 1. உங்கள் GitHub டோக்கனைப் பெறவும்

1. [GitHub அமைப்புகள் → Personal Access Tokens](https://github.com/settings/personal-access-tokens) செல்லவும்
2. "Generate new token" என்பதைக் கிளிக் செய்க
3. விளக்கமான பெயரை அமைக்கவும் (எ.கா. "LangChain4j Demo")
4. காலாவதி (7 நாட்கள் பரிந்துரைக்கப்படுகிறது)
5. "Account permissions" கீழ் "Models" என்பதை "Read-only" ஆக்கு
6. "Generate token" கிளிக் செய்க
7. உங்கள் டோக்கனை நகலெடுக்கவும் மற்றும் சேமிக்கவும் - மீண்டும் பார்க்க முடியாது

### 2. உங்கள் டோக்கனை அமைக்கவும்

**விருப்பம் 1: VS Code பயன்படுத்துதல் (பரிந்துரைக்கப்படுகிறது)**

VS Code பயன்படுத்துகிறீர்கள் என்றால், உங்கள் திட்ட அடிவெளியில் `.env` கோப்பில் உங்கள் டோக்கனைச் சேர்க்கவும்:

`.env` கோப்பு இல்லை என்றால், `.env.example` ஐ `.env` ஆக நகலெடுக்கவும் அல்லது புதிய `.env` கோப்பை உருவாக்கவும்.

**எடுத்துக்காட்டு `.env` கோப்பு:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env இல்
GITHUB_TOKEN=your_token_here
```

பிறகு Explorer இல் எந்தவொரு டெமோ கோப்பையும் (எ.கா. `BasicChatDemo.java`) வலது கிளிக் செய்து **"Run Java"** ஐ தேர்வு செய்யலாம் அல்லது Run and Debug குழுமத்தில் இருந்து தொடக்க வடிவமைப்புகளைப் பயன்படுத்தலாம்.

**விருப்பம் 2: டெர்மினல் பயன்படுத்துதல்**

டோக்கனை சுற்றுச்சூழல் மாறியாக அமைக்கவும்:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## உதாரணங்களை இயக்கவும்

**VS Code பயன்படுத்துதல்:** Explorer இல் எந்தவொரு டெமோ கோப்பையும் வலது கிளிக் செய்து **"Run Java"** ஐ தேர்வு செய்யவும் அல்லது Run and Debug குழுமத்திலிருந்து தொடக்க வடிவமைப்புகளைப் பயன்படுத்தவும் (முதல் `.env` கோப்பில் டோக்கன் சேர்க்கப்பட்டுள்ளது என்பதை உறுதி செய்யவும்).

**Maven பயன்படுத்துதல்:** மாற்றாக, நீங்கள் கட்டளை வரியில் இருந்து இயக்கலாம்:

### 1. அடிப்படைக் காட்சி

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. கூற்றுப் பாடமுறைமை

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

சூன்ய ஷாட், சில ஷாட், சங்கிலி சிந்தனை மற்றும் பாத்திர அடிப்படையிலான ப்ராம்ப்டிங் காட்டு.

### 3. செயல்பாடு அழைப்பு

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI தேவையான போது உங்கள் Java முறைகளை தானாக அழைக்கும்.

### 4. ஆவண கேள்வி & பதில் (எளிய RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

தானாக செருகலும் மீட்பும் உடன் எளிய RAG பயன்படுத்தி உங்கள் ஆவணங்களைப் பற்றி கேள்விகள் கேளுங்கள்.

### 5. பொறுப்பான AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI பாதுகாப்பு வடிகட்டிகள் தீங்கான உள்ளடக்கத்தை எவ்வாறு தடுக்கும் என்பதைக் காண்பிக்கிறது.

## ஒவ்வொரு உதாரணமும் என்ன காட்டுுகிறது

**அடிப்படைக் காட்சி** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j இன் அடிப்படைகளை இதிலிருந்து தொடங்குங்கள். `OpenAiOfficialChatModel` உருவாக்கி, `.chat()` உடன் கோரிக்கை அனுப்பி பதிலைக் பெறுவீர்கள். இது அழகான ஆதாரம்: தனித்துவமான அலுவலகங்கள் மற்றும் API விசைகளுடன் மாதிரிகளை எவ்வாறு ஆரம்பிப்பது என்பதை காட்டுகிறது. இந்த முறை தெரிந்த பிறகு மற்ற அனைத்தும் அதன் மேல் கட்டமைக்கப்படும்.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat உடன் முயற்சியுங்கள்:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) திறந்து கேளுங்கள்:  
> - "GitHub மாதிரிகளிலிருந்து Azure OpenAIக்கு இந்தக் கோடில் எப்படிப் பிரவேசிப்பேன்?"  
> - "OpenAiOfficialChatModel.builder() இல் இன்னும் என்ன அளவுரைகள் உள்ளன?"  
> - "முழு பதிலை காத்திருக்காமல் ஸ்ட்ரீமிங் பதில்களைச் சேர்க்க எப்படி?"  

**ப்ராம்ப்ட் பொறியியல்** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

மாதிரிக்கோப்புடன் நீங்கள் பேசுவது எப்படி என்பதை இப்போது ஆராய்வோம். இந்த டெமோ அதே மாதிரி அமைப்பை பயன்படுத்துகிறது ஆனால் ஐந்து விதமான ப்ராம்ப்ட் நிலைப்பாடுகளை காட்டுகிறது. நேரடி அறிவுரைக்கு சூன்ய ஷாட் ப்ராம்ப்ட்களை முயற்சிக்கவும், எடுத்துக்காட்டுக்களிலிருந்து கற்றுக் கொள்ள சில ஷாட் ப்ராம்ப்ட்களைப் பார்க்கவும், காரணிகளை வெளிப்படுத்த சங்கிலி சிந்தனை ப்ராம்ப்ட்களைப்பார்க்கவும், மற்றும் சூழலை அமைக்கும் பாத்திர அடிப்படையிலான ப்ராம்ப்ட்களைப் பயன்படுத்தவும். அதே மாதிரி உங்கள் கோரிக்கையை எப்படி வடிவமைக்கிறீர்கள் என்பதைக் கருத்தில் கொண்டு மிக வேறுபட்ட முடிவுகளைப் பெறுவீர்கள்.

டெமோ ப்ராம்ப்ட் வார்ப்புருக்களையும் காட்டுகிறது, இது மாறிலிகளுடன் மறுபயன்படக் கூடிய ப்ராம்ப்ட்களை உருவாக்கும் சக்திவாய்ந்த வழி ஆகும்.  
கீழே உள்ள எடுத்துக்காட்டு LangChain4j `PromptTemplate` பயன்படுத்தி மாறிலிகளை நிரப்ப ஒரு ப்ராம்ப்டை காட்டுகிறது. AI கொடுக்கப்பட்ட இடமும் செயல்பாட்டையும் அடிப்படையாகக் கொண்டு பதிலளிக்கும்.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat உடன் முயற்சியுங்கள்:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) திறந்து கேளுங்கள்:  
> - "சூன்ய ஷாட் மற்றும் சில ஷாட் ப்ராம்ப்டிங்கில் என்ன வேறுபாடு, எப்போது எது பயன்படுத்தப்படும்?"  
> - "தபால்தாபம் அளவுரை மாதிரியின் பதில்களை எவ்வாறு பாதிக்கிறது?"  
> - "உற்பத்தியில் ப்ராம்ப்ட் இன்ஜெக்ஷன் தாக்குதல்களைத் தடுப்பதற்கான சில நுட்பங்கள் என்ன?"  
> - "பொதுவான வடிவங்களுக்கான மறுபயன்படக்கூடிய ப்ராம்ப்ட் வார்ப்புருக்களை எப்படி உருவாக்குவது?"  

**கருவி ஒருங்கிணைப்பு** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

இங்கே LangChain4j மிகவும் சக்திவாய்ந்ததாக உள்ளது. `AiServices` ஐப் பயன்படுத்தி உங்கள் Java முறைகளை அழைக்கும் AI உதவியாளரை உருவாக்குவீர்கள். முறைகளை `@Tool("விளக்கம்")` என குறிக்கவும், LangChain4j மற்ற அனைத்தையும் கையாளும் - பயனர் கேட்டதை அடிப்படையாகக் கொண்டு AI எந்த கருவியை எப்போது பயன்படுத்துவது என்று தானாக முடிவெடுக்கிறது. இது செயல்பாடு அழைப்பை விளக்குகிறது, கேள்விகளுக்கு பதிலளிப்பதற்கு மட்டுமல்லாமல் நடவடிக்கைகளை எடுக்கக்கூடிய AI கட்டமைப்பின் முக்கிய முறையாகும்.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat உடன் முயற்சியுங்கள்:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) திறந்து கேளுங்கள்:  
> - "@Tool குறிப்பு எவ்வாறு செயல்படுகிறது மற்றும் LangChain4j அதை பின்னணியில் எவ்வாறு கையாள்கிறது?"  
> - "AI பல கருவிகளை தொடர் முறையாக அழைத்து கடுமையான பிரச்சனைகளை தீர்க்க முடியுமா?"  
> - "ஒரு கருவி தவறாக செயல் புரிந்தால் என்ன நடக்கிறது - பிழைகளை எப்படி கையாள வேண்டும்?"  
> - "இந்த கணக்கீட்டுப் பொருள் எடுத்துக்காட்டின் பதிலுக்கு உண்மையான API-ஐ எப்படி ஒருங்கிணைப்பேன்?"  

**ஆவண கேள்வி & பதில் (எளிய RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

இங்கே நீங்கள் LangChain4j இன் "எளிய RAG" முறையைப் பயன்படுத்தி RAG (மீட்டல்-தீர்க்கப்பட்ட உருவாக்கம்)ஐ காண்பீர்கள். ஆவணங்கள் ஏற்றப்பட்டு தானாகவே பிரிக்கப்பட்டு உள்ள மனசை நினைவகத்தில் செருகப்படும், பின்னர் உள்ளடக்க மீட்டல் AIக்கு தொடர்புடைய துண்டுகளை அளிக்கும். AI பொது அறிவின் பதிலாக உங்கள் ஆவணங்களின் அடிப்படையில் பதிலளிக்கிறது.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat உடன் முயற்சியுங்கள்:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) திறந்து கேளுங்கள்:  
> - "RAG AI கண்னிக்கை தவறுகளை மாதிரியின் பயிற்சி தரவுகளையும் ஒப்பிடுகையில் எப்படி தடுக்கும்?"  
> - "இந்த எளிய முறையும் தனிப்பயன் RAG குழாயும் என்ன வேறுபாடு?"  
> - "பல ஆவணங்கள் அல்லது பெரிய அறிவு தளங்களைக் கையாள இதை எப்படி அளவிடுவேன்?"  

**பொறுப்பான AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ஆழமான பாதுகாப்புடன் AI பாதுகாப்பை கட்டியெழுப்புங்கள். இந்த டெமோ இரண்டு பாதுகாப்பு அடுக்குகளை ஒன்றாகச் செயல்படுத்துகிறது:

**பகுதி 1: LangChain4j உள்ளீட்டு பாதுகாப்பு** - LLM-க்கு அனுப்புவதற்கு முன் தீங்கான ப்ராம்ப்ட்களைத் தடுக்கிறது. தடைசெய்யப்பட்ட முக்கிய வார்த்தைகள் அல்லது முறைகளைப் பரிசோதிக்கும் தனிப்பயன் பாதுகாப்பு தடைகளைக் கட்டமைக்கவும். இவை உங்கள் கோடில் இயங்கும் என்பதால் வேகமாகவும் இலவசமுமാണ്.

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

**பகுதி 2: வழங்குநர் பாதுகாப்பு வடிகட்டிகள்** - GitHub Models உள்நுழைவிற்கான வடிகட்டிகளை கொண்டுள்ளது, உங்கள் பாதுகாப்பு தடைகள் கவரவில்லை என்ற சாத்தியமானவற்றை பிடிக்கிறது. கடுமையான மீறல்களுக்கு (HTTP 400 பிழைகள்) கடுமையான தடைகள் மற்றும் AI மரியாதையாக மறுத்து விட்ட இடங்களுக்கு மென்மையான மறுக்கல்களைக் காண்பீர்கள்.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat உடன் முயற்சியுங்கள்:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) திறந்து கேளுங்கள்:  
> - "InputGuardrail என்றால் என்ன மற்றும் என் சொந்ததை எப்படி உருவாக்குவது?"  
> - "கடுமையான தடையுடன் மென்மையான மறுக்கும் இடையிலான வேறுபாடு என்ன?"  
> - "எதற்கு இரண்டையும் கூட பாதுகாப்பு தடைகள் மற்றும் வழங்குநர் வடிகட்டிகளை பயன்படுத்த வேண்டும்?"  

## அடுத்த படிகள்

**அடுத்த தொகுதி:** [01-introduction - LangChain4j உடன் துவக்கம்](../01-introduction/README.md)

---

**வழிசெலுத்தல்:** [← முதன்மைக்குத் திரும்புக](../README.md) | [அடுத்தது: Module 01 - அறிமுகம் →](../01-introduction/README.md)

---

## பிரச்சனை தீர்க்கும் வழிகள்

### முதன்முறை Maven கட்டிடம்

**பிரச்சனை:** முதலில் `mvn clean compile` அல்லது `mvn package` கட்டளை 10-15 நிமிடங்கள் ஆகும்

**காரணம்:** Maven அனைத்து திட்ட சார்புகளையும் (Spring Boot, LangChain4j நூலகங்கள், Azure SDKகள் மற்றும் பிற) முதன்முறை கட்டலில் பதிவிறக்கம் செய்ய வேண்டும்.

**தீர்வு:** இது சாதாரண நடத்தை. பின்வரும் கட்டிடங்கள் வேகமாக இருக்கும், ஏனெனில் சார்புகள் உள்ளகமாக சேமிக்கப்படுகின்றன. பதிவிறக்க நேரம் உங்கள் நெட்வொர்க் வேகத்தை சார்ந்தது.

### PowerShell Maven கட்டளை வடிவம்

**பிரச்சனை:** Maven கட்டளைகள் `Unknown lifecycle phase ".mainClass=..."` பிழையுடன் தோல்வி அடைகின்றன
**காரணம்**: PowerShell `=` ஐ மாறிலி ஒதுக்கல் இயக்கியாகப் பொருள் படுத்துகிறது, இது Maven சொத்துக் கோட்பாட்டை உடைக்கிறது

**தீர்வு**: Maven கட்டளைக்கு முன் நிறுத்த-பர்சிங் இயக்கி `--%` ஐ பயன்படுத்தவும்:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` இயக்கி PowerShell மற்ற அனைத்து வாதங்களையும் வார்த்தைபடுத்தாமலேயே Maven க்கு நேரடியாக அனுப்பும்படி சொல்கிறது.

### Windows PowerShell எமோஜி காட்சி

**பிரச்சனை**: PowerShell இல் எமோஜிகளுக்கு பதிலாக குப்பை எழுத்துக்கள் ரெண்டர் ஆகின்றன (எ.கா., `????` அல்லது `â??`)

**காரணம்**: PowerShell இன் இயல்புநிலை குறியீட்டாக்கம் UTF-8 எமோஜிகளை ஆதரிக்காது

**தீர்வு**: Java செயலிகளை இயக்கும்முன் இந்த கட்டளையை செயல்படுத்தவும்:
```cmd
chcp 65001
```

இது டெர்மினலில் UTF-8 குறியீட்டாக்கத்தைக் கட்டாயப்படுத்தும். மாற்றாக, சிறந்த யூனிகோட் ஆதரவுடன் Windows Terminal ஐப் பயன்படுத்தவும்.

### API அழைப்புகளை பிழைத்திருத்தல்

**பிரச்சனை**: அடையாளத்தேர்வு பிழைகள், விகித வரம்புகள், அல்லது எதிர்பாராத பதில்கள் AI மாதிரியிலிருந்து

**தீர்வு**: எடுத்துக்காட்டுகள் `.logRequests(true)` மற்றும் `.logResponses(true)` உள்ளன, அவை API அழைப்புக்களை கன்சோலில் காட்சி படுத்துகின்றன. இதனால் அடையாளத்தேர்வு பிழைகள், விகித வரம்புகள், அல்லது எதிர்பாராத பதில்களை தீர்க்க உதவும். உற்பத்தியில் இந்த கொடிகளை நீக்கி பதிவு சத்தங்களை குறைக்கவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**பிரதிபாதிப்பு**:  
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) ஆகியவற்றைப் பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. எங்கள் முயற்சியேயானாலும், தானியங்கி மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்கலாம் என கவனித்துக் கொள்ளவும். இயல்புநிலை மொழியில் உள்ள அசல் ஆவணம் அதிகாரபூர்வ மூலமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பு பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பின் பயன்பாட்டினால் ஏற்பட்ட எந்தவொரு தவறான புரிதலுக்கும் அல்லது தவறான விளக்கங்களுக்கும் எங்களை பொறுப்பேற்க தேவையில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->