# தொகுதி 00: விரைவு துவக்கம்

## உள்ளடக்க அட்டவணை

- [அறிமுகம்](../../../00-quick-start)
- [LangChain4j என்றால் என்ன?](../../../00-quick-start)
- [LangChain4j சார்புகள்](../../../00-quick-start)
- [முன் தேவைகள்](../../../00-quick-start)
- [செட்டப்](../../../00-quick-start)
  - [1. உங்கள் GitHub டோக்கனை பெறவும்](../../../00-quick-start)
  - [2. உங்கள் டோக்கனை அமைக்கவும்](../../../00-quick-start)
- [உதாரணங்களை இயக்கவும்](../../../00-quick-start)
  - [1. அடிப்படை உரையாடல்](../../../00-quick-start)
  - [2. தூண்டுதலுக்கான மாதிரிகள்](../../../00-quick-start)
  - [3. செயல்பாடு அழைப்பு](../../../00-quick-start)
  - [4. ஆவண கேள்வி & பதில் (எளிய RAG)](../../../00-quick-start)
  - [5. பொறுப்பான AI](../../../00-quick-start)
- [ஒவ்வொரு உதாரணமும் என்ன காண்பிக்கிறது](../../../00-quick-start)
- [அடுத்த படிகள்](../../../00-quick-start)
- [பிரச்சினைகள் தீர்க்கல்](../../../00-quick-start)

## அறிமுகம்

இந்த விரைவு துவக்கம் LangChain4j உடன் மிகவும் விரைவாக செயல்படுத்துவதை உங்களுக்கு உதவ உருவாக்கப்பட்டுள்ளது. இது LangChain4j மற்றும் GitHub மாதிரிகள் கொண்டு AI பயன்பாடுகளை உருவாக்கும் அடிப்படைகளை மட்டும் உள்ளடக்கியது. அடுத்த தொகுதிகளில் Azure OpenAI ஐ LangChain4j உடன் சேர்த்து மேலும் மேம்பட்ட பயன்பாடுகளை நீங்கள் உருவாக்கப் போகிறீர்கள்.

## LangChain4j என்றால் என்ன?

LangChain4j என்பது AI-ஆதாரித பயன்பாடுகளை உருவாக்க எளிமையாக்கும் ஜாவா நூலகம் ஆகும். HTTP கிளையண்ட்கள் மற்றும் JSON பார்சிங் போன்றவைகளுடன் கைகூடாமல், தூய ஜாவா APIகளோடு நீங்கள் வேலை செய்யலாம்.

LangChain இல் உள்ள "செயிண்" என்பது பல கூறுகளை ஒன்றுக்கொன்று தொடுசெய்தல் - நீங்கள் ஒரு தூண்டுதலை ஒரு மாதிரிக்கு, அப்படியே ஒரு பார்சருக்கு இணைக்கலாம், அல்லது பல AI அழைப்புகளை ஒன்றுக்கு மற்றொன்று இணைத்துக்கொள்ளலாம், ஒரே வெளிச்சம் அடுத்த உள்ளீட்டை வழங்கும். இந்த விரைவு துவக்கம் அடிப்படைகளைக் கவனித்துப் பின்னர் இன்னும் கூடிக்கூடிய செயிண்கள் பற்றிப் பார்க்கிறது.

<img src="../../../translated_images/ta/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j இல் கூறுகளை சங்கிலி - சக்திவாய்ந்த AI வேலைப்பாட்டை உருவாக்க கூறுகள் இணைகின்றன*

நாம் மூன்று முக்கிய கூறுகளைப் பயன்படுத்தப் போகிறோம்:

**ChatModel** - AI மாதிரி தொடர்பிற்கான இடைமுகம். `model.chat("prompt")` என அழைத்து பதில்சொற்களை பெறலாம். OpenAI-க்கு இணக்கமான GitHub மாதிரிகள் போன்ற இடைத்தெருவுகளோடு பணியாற்றும் `OpenAiOfficialChatModel` ஐ நாம் பயன்படுத்துகிறோம்.

**AiServices** - வகை பாதுகாப்பு (type-safe) AI சேவை இடைமுகங்களை உருவாக்கும். முறைகளை வரையறுக்கவும், அவற்றை `@Tool` இல் குறிக்கவும், LangChain4j ஒழுங்குப்படுத்தலை கையாளும். தேவையான போது AI தானாக உங்கள் ஜாவா முறைகளை அழைக்கும்.

**MessageWindowChatMemory** - உரையாடல் வரலாற்றை நிர்வகிக்கும். இதின்றி ஒவ்வொரு கோரிக்கையும் தனியாகும். இதுடன் AI முந்தைய செய்திகளை நினைத்து பல மடங்குகளில் கருத்தை தொடர்கிறது.

<img src="../../../translated_images/ta/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j கட்டமைப்பு - உங்கள் AI பயன்பாடுகளுக்கு சக்தியளிக்கும் முக்கிய கூறுகள் ஒருங்கிணைந்துள்ளனர்*

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

`langchain4j-open-ai-official` தொகுதி OpenAI இணக்கமான APIகளுடன் இணைக்கும் `OpenAiOfficialChatModel` வகுப்பை வழங்குகிறது. GitHub மாதிரியானவை அதே API வடிவத்தைப் பயன்படுத்துவதால் தனிச்சிறப்பான இடைமுகம் தேவையில்லை - அடிப்படை URL ஆக `https://models.github.ai/inference` ஐ மட்டும் குறிக்கவும்.

`langchain4j-easy-rag` தொகுதி ஆவணங்களை தானாக பிரிப்பது, உள்வாங்குதல் மற்றும் மீட்டெடுப்பை வழங்குகிறது; இதனால் ஒவ்வொரு படியானும் கையால் அமைப்பதற்கு தேவையில்லை.

## முன் தேவைகள்

**Dev Container பயன்படுத்துகிறீர்களா?** ஜாவா மற்றும் மேவன் ஏற்கனவே நிறுவப்பட்டுள்ளது. உங்களுக்கு மட்டும் ஒரு GitHub தனிப்பட்ட அணுகல் டோக்கன் வேண்டும்.

**உள்நாட்டுப் பகுதி வளர்ச்சி:**
- ஜாவா 21+, மேவன் 3.9+
- GitHub தனிப்பட்ட அணுகல் டோக்கன் (கீழே வழிமுறைகள்)

> **கவனம்:** இந்த தொகுதி GitHub மாதிரிகளிலிருந்து `gpt-4.1-nano` பயன்படுத்துகிறது. கோடில் மாதிரி பெயரை மாற்றாதீர்கள் - இது GitHub-இல் கிடைக்கும் மாதிரிகளுடன் பணியாற்ற ஏற்கனவே அமைக்கப்பட்டுள்ளது.

## செட்டப்

### 1. உங்கள் GitHub டோக்கனை பெறவும்

1. [GitHub அமைப்புகள் → தனிப்பட்ட அணுகல் டோக்கன்கள்](https://github.com/settings/personal-access-tokens) இற்கு செல்லவும்
2. "புதிய டோக்கன் உருவாக்கு" கிளிக் செய்யவும்
3. விளக்கமான பெயர் அமைக்கவும் (எ.கா., "LangChain4j டெமோ")
4. காலாவதியான நாள் நிர்ணயிக்கவும் (7 நாட்கள் பரிந்துரை)
5. "கணக்கு அனுமதிகள்" பகுதியில் "Models" ஐ "ஓர்முகம் வாசிக்க" ஆக்கவும்
6. "டோக்கன் உருவாக்கு" கிளிக் செய்யவும்
7. உங்கள் டோக்கனை நகலெடுக்கவும். மீண்டும் காண்பிக்கப்படாது.

### 2. உங்கள் டோக்கனை அமைக்கவும்

**விருப்பம் 1: VS Code பயன்படுத்துதல் (பரிந்துரைக்கப்படுகிறது)**

VS Code பயன்படுத்தினால், `.env` கோப்பில் உங்கள் டோக்கனை சேர்க்கவும்:

`.env` கோப்பு இல்லை எனில் `.env.example` ஐ `.env` ஆக நகலெடுக்கவும் அல்லது புதிய `.env` கோப்பு உருவாக்கவும்.

**உதாரண `.env` கோப்பு:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env இல்
GITHUB_TOKEN=your_token_here
```

பின்னர், Explorer இல் உள்ள எந்தவொரு டெமோ கோப்பிலும் (எ.கா., `BasicChatDemo.java`) வலது கிளிக் செய்து **"Run Java"** தேர்வு செய்யலாம் அல்லது இயக்கவும் டிபக் குழப்பிலுள்ள தொடங்கல் கட்டமைப்புகளை பயன்படுத்தவும்.

**விருப்பம் 2: டெர்மினல் பயன்படுத்துதல்**

டோக்கனை சுற்றுச்சூழல் மாறிலியாக அமைக்கவும்:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## உதாரணங்களை இயக்கவும்

**VS Code பயன்படுத்தும் போது:** Explorer இல் உள்ள எந்த டெமோ கோப்பிலும் வலதி கிளிக் செய்து **"Run Java"** தேர்வு செய்க, அல்லது இயக்கவும் டிபக் குழப்பிலுள்ள தொடங்கல் கட்டமைப்புகளைப் பயன்படுத்தவும் (முதல் `.env` கோப்பில் டோக்கன் சேர்க்கப்பட்டுள்ளது என்பதை உறுதிப்படுத்தவும்).

**Maven பயன்படுத்தும் போது:** மாறாக, கட்டளை வரியில் இருந்து இயக்கு முடியும்:

### 1. அடிப்படை உரையாடல்

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. தூண்டுதலுக்கான மாதிரிகள்

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ஜீரோ-ஷாட், ஃபியூ-ஷாட், எண்ணக்குழல், மற்றும் பாத்திர அடிப்படையிலான தூண்டுதலை காண்பிக்கிறது.

### 3. செயல்பாடு அழைப்பு

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

தேவையான சமயத்தில் AI தானாக உங்கள் ஜாவா முறைகளை அழைக்கும்.

### 4. ஆவண கேள்வி & பதில் (எளிய RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

தானாக உள்ளத்துண்டுகளை உருவாக்கி மற்றும் பின்வாங்கும் எளிய RAG மூலம் ஆவணங்கள் பற்றி கேள்வி கேட்கவும்.

### 5. பொறுப்பான AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI பாதுகாப்பு வடிகட்டிகள் தாக்குதல்களை பகிரங்கச் செய்தியிலிருந்து மறுக்கும்.

## ஒவ்வொரு உதாரணமும் என்ன காண்பிக்கிறது

**அடிப்படை உரையாடல்** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j யைப் மிக எளிதாக தொடர்ந்து பார்க்க இத்தொடக்கம். நீங்கள் ஒரு `OpenAiOfficialChatModel` உருவாக்கி, `.chat()` உடன் தூண்டுதலை அனுப்பி, பதிலை பெறுவீர்கள். இதன் மூலம் மாதிரிகளை தனிப்பயன் இடைத்தெருவுகள் மற்றும் API விசைகளுடன் துவங்குவது எப்படி என்பதை விளக்கும். இந்த மாதிரியைப் புரிந்துகொண்டால், மற்றவை அனைத்தும் இதன் அடிப்படையில் உருவாக்கப்படும்.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடலுடன் முயற்சி செய்யவும்:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) திறந்து கேளவும்:
> - "இந்தக் கோடில் GitHub மாதிரிகளிலிருந்து Azure OpenAI க்கு எப்படி மாறுவது?"
> - "OpenAiOfficialChatModel.builder() இல் மற்ற மேற்கோள்களை எப்படி அமைக்கலாம்?"
> - "முழுமையான பதிலை காத்திருக்காமல் ஸ்ட்ரீமிங் பதிலை எப்படி சேர்க்கலாம்?"

**தூண்டுதல் பொருத்துகை** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

மாதிரியுடன் எப்படி பேசுவது தெரியும்போது, நீங்கள் அதற்கு என்ன சொல்வீர்கள் என்பதைக் காணலாம். இந்த டெமோ அதே மாதிரி அமைப்பை பயன்படுத்தி ஐந்து பல்வேறு தூண்டுதலுக்கான மாதிரிகளை காட்டுகிறது. நேரடி குறிகள், எடுத்துக்காட்டுகளை வைத்து கற்றுக்கொள்ளும் குறிகள், எண்ணக்குழல், மற்றும் பாத்திர அடிப்படையிலான தூண்டுதல்களை முயற்சி செய்க. நீங்கள் எவ்வாறு கேட்குமோ அதற்கிணங்க மாதிரி முற்றிலும் வேறுபட்ட பதில்களை வழங்குகிறது என்பதைப் பார்க்கலாம்.

இந்த டெமோ மாறிலிகளுடன் பலமுறை பயன்படுத்தக்கூடிய தூண்டுதல்களை உருவாக்கும் முறைமைகளையும் காட்டுகிறது.
கீழே LangChain4j `PromptTemplate` பயன்படுத்தி மாறிலிகளை நிரப்பும் தூண்டுதலை காட்டுகிறது. AI கொடுக்கப்பட்ட இடம் மற்றும் செயல்பாடு அடிப்படையில் பதிலளிக்கும்.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடலுடன் முயற்சி செய்யவும்:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) திறந்து கேளவும்:
> - "ஜீரோ-ஷாட் மற்றும் ஃபியூ-ஷாட் தூண்டுதலின் வேறுபாடு என்ன மற்றும் எப்போது எந்தவற்றைப் பயன்படுத்த வேண்டும்?"
> - "தயாரிப்பாளி அளவு மாதிரியின் பதில்களை எப்படி பாதிக்கிறது?"
> - "தூண்டுதல் உள்ளீடு தாக்குதல்களை தடுக்க உற்பத்தி நிலைமையில் என்ன தொழில்நுட்பங்கள் உள்ளன?"
> - "சாதாரண மாதிரிகளுக்கு மீண்டும் பயன்படுத்தக்கூடிய PromptTemplate ஆப்ஜெக்ட்களை எப்படி உருவாக்குவது?"

**கருவி ஒருங்கிணைப்பு** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

இங்கு LangChain4j வலிமையடைந்து செயற்படும். `AiServices` பயன்படுத்தி உங்கள் ஜாவா முறைகளை அழைக்கும் AI உதவியாளரை உருவாக்குவீர்கள். முறைகளை `@Tool("விளக்கம்")` என குறித்துக் கொடுத்து, LangChain4j அது சீரமைப்பை கையாளும் - பயனரின் கேள்விகளை அடுத்து AI தானாக தேவையான கருவிகளை பயன்படுத்த முடியும். இது செயல்பாடு அழைப்பு, செயல்களை எடுக்கக்கூடிய AI உருவாக்கும் முக்கிய தொழில்நுட்பத்தை விளக்குகிறது.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடலுடன் முயற்சி செய்யவும்:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) திறந்து கேளவும்:
> - "@Tool குறிப்பு எப்படி செயல்படுகிறது மற்றும் LangChain4j அதற்கு பின்புலத்தில் என்ன செய்கிறது?"
> - "பல கருவிகளைத் தொடர் முறையில் AI அழைக்க முடியுமா?"
> - "கருவி செய்யாத தவறுகள் ஏற்பட்டால் அதை எப்படி கையாள வேண்டும்?"
> - "இந்த கணக்குப் பட்டியலை விட உண்மையான API ஒருங்கிணைப்பது எப்படி?"

**ஆவணம் கேள்வி & பதில் (எளிய RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

இங்கே LangChain4j இன் "எளிய RAG" அணுகுமுறையை பயன்படுத்தி RAG (மீட்டெடுக்கும்-உற்பத்தி) காணலாம். ஆவணங்கள் ஏற்றப்பட்டு தானாக பிரிக்கப்பட்டு நினைவகத்தில் சேமிக்கப்படுகிறன. பின்னர் தொடர்புடைய துண்டுகள் AI கேள்வியுறையில் வழங்கப்படுகின்றன. AI உங்கள் ஆவணங்களை அடிப்படையாக கொண்டு பதிலளிக்கும், அதன் பொது அறிவியல் அல்ல.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடலுடன் முயற்சி செய்யவும்:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) திறந்து கேளவும்:
> - "மாதிரியின் பயிற்சி தரவுக்கு பதிலாக RAG, AI புல்லிங்கை எப்படி தடுக்கும்?"
> - "இந்த எளிய அணுகுமுறை மற்றும் தனிப்பயன் RAG குழாய் இடையே என்ன வித்தியாசம்?"
> - "பல ஆவணங்களும் அல்லது பெரிய அறிவுத்தளங்களையும் கையாள இதை எவ்வாறு பரப்புவது?"

**பொறுப்பான AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ஆழமான பாதுகாப்புடன் AI பாதுகாப்பை உருவாக்குங்கள். இந்த டெமோ இரண்டு பாதுகாப்பு அடுக்குகளை இயக்குகிறது:

**பகுதி 1: LangChain4j உள்ளீட்டு காவல்சட்டங்கள்** - LLM-க்கு செல்லும் முன் தீங்கு விளைவிக்கும் தூண்டுதல்களை தடுக்கும். தடைசெய்யப்படும் சொற்கள் அல்லது மாதிரிகளை சோதிக்கும் தனிப்பயன் காவல்சட்டங்களை உருவாக்குங்கள். அவை உங்கள் கோடில் செயற்படும், அதனால் வேகம் மிகுந்தவை மற்றும் இலவசம்.

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

**பகுதி 2: வழங்குநர் பாதுகாப்பு வடிகட்டிகள்** - GitHub மாதிரிகள் உங்கள் காவல்சட்டங்கள் தவறவிட்டதை பிடிக்கும் உள்ளமைவு வடிகட்டிகளை கொண்டுள்ளது. கடுமையான தடைகள் (HTTP 400 பிழைகள்) மற்றும் இனிய மறுப்பு நிலைகள் காணப்படுகின்றன.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடலுடன் முயற்சி செய்யவும்:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) திறந்து கேளவும்:
> - "InputGuardrail என்றால் என்ன மற்றும் நான் எப்படி உருவாக்க வேண்டும்?"
> - "கடுமையான தடையும் மென்மையான மறுப்பும் என்ன வேறுபாடு?"
> - "காவல்சட்டங்களும் வழங்குநர் வடிகட்டுகளும் ஒருசேர உரியதா?"

## அடுத்த படிகள்

**அடுத்த தொகுதி:** [01-அறிமுகம் - LangChain4j மற்றும் gpt-5 உடன் Azure இல் துவக்கம்](../01-introduction/README.md)

---

**நெவிகேஷன்:** [← பிரதான பக்கத்திற்கு திரும்பு](../README.md) | [அடுத்தது: தொகுதி 01 - அறிமுகம் →](../01-introduction/README.md)

---

## பிரச்சினைகள் தீர்க்கல்

### முதன்முறையாக Maven கட்டமைப்பு

**பிரச்சினை**: முதல் `mvn clean compile` அல்லது `mvn package` அதிக நேரம் (10-15 நிமிடங்கள்) எடுத்துக் கொள்ளும்

**காரணம்**: Maven முதன்முதல் கட்டமைப்பில் அனைத்து திட்ட சார்புகளையும் (Spring Boot, LangChain4j நூலகங்கள், Azure SDKகள் போன்றவை) பதிவிறக்கம் செய்ய வேண்டும்.

**தீர்வு**: இது சாதாரண நடைமுறை. அடுத்த கட்டமைப்புகள் வேகமாக நடக்கும், காரணம் சார்புகள் உள்ளூர் சேமிப்பு (cache) ஆகும். பதிவிறக்கம் நேரம் உங்கள் இணைய வேகத்திற்கு ஏற்ப இருக்கும்.

### PowerShell Maven கட்டளையின் உரிமையிலக்கியம்

**பிரச்சினை**: Maven கட்டளைகள் `Unknown lifecycle phase ".mainClass=..."` என்னும் பிழையுடன் தோல்வியடையும்
**காரணம்**: PowerShell `=` என்பதை மாறி ஒதுக்குதல் இயக்கியாக புரிந்து கொண்டு, Maven சொத்துக் கட்டமைப்பை முற்றிலும் பிளவு செய்கிறது

**தீர்வு**: Maven கட்டளை முன் stop-parsing இயக்கி `--%` ஐ பயன்படுத்தவும்:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` இயக்கி PowerShell இல் இருந்து Maven க்கு மீதமுள்ள அனைத்து_ARGUMENTS_களையும் துல்லியமாக எந்தவிதம் விளக்கமளிப்பின்றி அனுப்ப சொல்லுகிறது.

### Windows PowerShell எமோஜி காட்சியிடல்

**பிரச்சினை**: PowerShell இல் AI பதில்கள் இடம் பெற்று, எமோஜிகளுக்கு பதிலாக பதற்றமான எழுத்துக்கள் (எ.கா., `????` அல்லது `â??`) காணப்படுவது

**காரணம்**: PowerShell இன் இயல்புநிலை குறியீட்டு முறையானது UTF-8 எமோஜிகளுக்கு ஆதரவளிக்கவில்லை

**தீர்வு**: ஜாவா செயலிகளைக் கொண்டு செயல்படுத்த முன் இந்த கட்டளையை இயக்கவும்:
```cmd
chcp 65001
```

இது டெர்மினலில் UTF-8 குறியீட்டைப் பலப்படுத்தி விடும். மாற்றாக, சிறந்த யூனிகோட் ஆதரவு உள்ள Windows Terminal ஐப் பயன்படுத்தலாம்.

### API அழைப்புகளை பிழைத்திருத்தம் செய்தல்

**பிரச்சினை**: அடையாளபடுத்தல் பிழைகள், வீத வரம்புகள் அல்லது எதிர்பாராத AI பதில்கள்

**தீர்வு**: எடுத்துக்காட்டுகளில் `.logRequests(true)` மற்றும் `.logResponses(true)` செட் செய்யப்பட்டுள்ளது, இது API அழைப்புகளை குறித்துக்காட்டுகிறது. இதனால் அடையாளபடுத்தல் பிழைகள், வீத வரம்புகள், அல்லது எதிர்பாராத பதில்களை கண்டறிய உதவுகிறது. உற்பத்திப் பயன்பாட்டில் இதை நீக்கவும், வெளிப்படுத்தப்படும் பதிவு எண்ணிக்கையை குறைக்க.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**தயவு 注**:
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயலுகிறோம் என்றாலும், தானியக்க மொழிபெயர்ப்புகளில் தவறுகள் அல்லது தவறான தகவல்கள் இருக்க வாய்ப்பு உள்ளது என்பதை தயவு செய்து கவனிக்கவும். தாய்மொழியில் உள்ள அசல் ஆவணம் அதிகாரபூர்வமான மூலமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பு பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பின் பயன்பாட்டால் ஏற்படும் எந்த தவறான புரிதல்கள் அல்லது தவறான விளக்கங்களுக்கும் நாங்கள் பொறுப்பு இல்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->