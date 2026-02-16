# Module 00: விரைவு தொடக்கம்

## உள்ளடக்கங்கள் அட்டவணை

- [அறிமுகம்](../../../00-quick-start)
- [LangChain4j என்பதும் என்ன?](../../../00-quick-start)
- [LangChain4j சார்புகள்](../../../00-quick-start)
- [முந்தைய தேவைகள்](../../../00-quick-start)
- [அமைப்புகள்](../../../00-quick-start)
  - [1. உங்கள் GitHub Token ஐப் பெறுங்கள்](../../../00-quick-start)
  - [2. உங்கள் Token ஐ அமைக்கவும்](../../../00-quick-start)
- [உதாரணங்களை இயக்கவும்](../../../00-quick-start)
  - [1. அடிப்படை உரையாடல்](../../../00-quick-start)
  - [2. கிளியிரை பாணிகள்](../../../00-quick-start)
  - [3. செயலி அழைப்பு](../../../00-quick-start)
  - [4. ஆவண கேள்வி & பதில் (RAG)](../../../00-quick-start)
  - [5. பொறுப்புள்ள AI](../../../00-quick-start)
- [ஒவ்வொரு உதாரணமும் என்ன காட்டுகிறது](../../../00-quick-start)
- [அடுத்த படிகள்](../../../00-quick-start)
- [பிரச்சனைகளுக்கு தீர்வுகள்](../../../00-quick-start)

## அறிமுகம்

இந்த விரைவு தொடக்கம் LangChain4j உடன் நீங்கள் விரைவாக வேலை செய்ய தொடங்க உதவ உருவாக்கப்பட்டது. இது LangChain4j மற்றும் GitHub Models உடன் AI பயன்பாடுகளை உருவாக்கும் அடிப்படைகளை உள்ளடக்கியது. அடுத்த பகுதிகளில் Azure OpenAI உடன் LangChain4j ஆகியவற்றைப் பயன்படுத்தி மேலும் மேம்பட்ட பயன்பாடுகளை உருவாக்குவீர்கள்.

## LangChain4j என்பதும் என்ன?

LangChain4j என்பது AI இயக்கப்படும் செயலிகளை எளிதாக உருவாக்க Java நூலகமாகும். HTTP கிளையண்டுகள் மற்றும் JSON பகுப்பாய்வுடன் வேலை செய்யாமல், நீங்கள் தூய Java APIகளுடன் பணியாற்றுவீர்கள்.

LangChain இல் "செய்கை சங்கிலி" என்பது பல கூறுகளை இணைப்பதைக் குறிக்கும் - நீங்கள் ஒரு கிளியிரை ஒரு மாடல் ஆக, பின்னர் ஒரு பகுப்பாய்வாளர் ஆக சங்கிலி செய்யலாம், அல்லது பல AI அழைப்புகளை ஒன்றோடும் தொடர்புபடுத்தலாம், ஒருங்கிணைந்த வெளியீடு அடுத்த உள்ளீட்டுக்கு போடப்படுகிறது. இந்த விரைவு தொடக்கம் அடிப்படைகளை கவனம் செலுத்துகிறது பின்னர் அதிக சிக்கலான சங்கிலிகளை ஆராய்கிறது.

<img src="../../../translated_images/ta/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-இல் கூறுகளைக் சங்கிலி செய்வது - பல தொகுதிகள் இணைந்து சக்திவாய்ந்த AI பணிகள் உருவாக்கும்*

நாம் மூன்று பிரதான கூறுகளை பயன்படுத்துவோம்:

**ChatLanguageModel** - AI மாடல் தொடர்புகளுக்கான இடைமுகம். `model.chat("prompt")` அழைத்து பதில் வரியை பெறலாம். நாங்கள் `OpenAiOfficialChatModel` ஐப் பயன்படுத்துகிறோம், இது OpenAI இணக்கமான APIகளுடன் GitHub Models போன்றவற்றுடன் செயல்படுகிறது.

**AiServices** - வகை-பாதுகாக்கப்பட்ட AI சேவை இடைமுகங்களை உருவாக்குகிறது. முறைகளை வரையறுக்கவும், அவற்றை `@Tool` என்ற குறியாக்கத்துடன் குறிக்கவும், LangChain4j ஒழுங்குபடுத்தலை கையாள்கிறது. தேவையானபோது AI உங்கள் Java முறைகள் இயங்கச் செய்துவிடும்.

**MessageWindowChatMemory** - உரையாடலின் வரலாற்றை பாதுகாக்கிறது. இது இல்லாவிட்டால், ஒவ்வொரு கோரிக்கையும் தனித்ததாக இருக்கும். இதில் இருந்தால், AI முந்தைய செய்திகள் மற்றும் பல முறை வரிகளின் வாயிலாக உள்ளடக்கத்தை நினைவில் வைக்கும்.

<img src="../../../translated_images/ta/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j கட்டமைப்பு - முக்கிய கூறுகள் ஒன்றிணைந்து உங்கள் AI பயன்பாடுகளை இயக்குகின்றன*

## LangChain4j சார்புகள்

இந்த விரைவு தொடக்கம் [`pom.xml`](../../../00-quick-start/pom.xml) கோப்பில் இரண்டு Maven சார்புகளைப் பயன்படுத்துகிறது:

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

`langchain4j-open-ai-official` என்னும் மாட்யூல் `OpenAiOfficialChatModel` வகுப்பை வழங்குகிறது, இது OpenAI இணக்கமான APIகளுடன் இணைகிறது. GitHub Models இதே API வடிவத்தைப் பயன்படுத்தும், எனவே உருவாக்கி தேவையில்லை - בסיס URL ஐ `https://models.github.ai/inference` என அமைக்க کافی.

## முந்தைய தேவைகள்

**டெவ் கண்டெய்னரைப் பயன்படுத்துகிறீர்களா?** Java மற்றும் Maven ஏற்கனவே நிறுவப்பட்டுள்ளன. GitHub தனிப்பட்ட அணுகல் டோக்கன் மட்டும் தேவையாகும்.

**உள்ளூர் மேம்பாடு:**
- Java 21+, Maven 3.9+
- GitHub தனிப்பட்ட அணுகல் டோக்கன் (குறிப்புகள் கீழே)

> **குறிப்பு:** இந்த தொகுதி GitHub Models இல் இருந்து `gpt-4.1-nano` ஐப் பயன்படுத்துகிறது. குறியீட்டில் மாடல் பெயரை மாற்ற வேண்டாம் - அது GitHub இல் கிடைக்கும் மாடல்களுக்கு ஏற்றதாக அமைக்கப்பட்டுள்ளது.

## அமைப்புகள்

### 1. உங்கள் GitHub Token ஐப் பெறுங்கள்

1. [GitHub அமைப்புகள் → தனிப்பட்ட அணுகல் டோக்கன்கள்](https://github.com/settings/personal-access-tokens) சென்று
2. "புதிய டோக்கன் உருவாக்கவும்" ஐ கிளிக் செய்யவும்
3. விளக்கமான பெயர் (எ.கா. "LangChain4j டெமோ") அமைக்கவும்
4. காலாவதி (7 நாட்கள் பரிந்துரைக்கப்படுகிறது) அமைக்கவும்
5. "கணக்கு அனுமதிகள்" உள்ள "Models" ஐ "படிக்க மட்டும்" ஆக அமைக்கவும்
6. "டோக்கன் உருவாக்கவும்" கிளிக் செய்யவும்
7. உங்கள் டோக்கனை நகலெடுத்து சேமித்து வையுங்கள் - அதனை மீண்டும் பார்க்கமுடியாது

### 2. உங்கள் Token ஐ அமைக்கவும்

**விருப்பம் 1: VS Code பயன்படுத்துதல் (பரிந்துரை)**

VS Code ஐப் பயன்படுத்தினால், உங்கள் டோக்கனை `.env` கோப்பில் திட்ட அடிப்பகுதியில் சேர்க்கவும்:

`.env` கோப்பு இல்லையெனில் `.env.example` ஐ `.env` ஆக நகலெடுக்கவும் அல்லது புதிய `.env` கோப்பை உருவாக்கவும்.

**உதாரண `.env` கோப்பு:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env இல்
GITHUB_TOKEN=your_token_here
```

பிறகு நீங்கள் எதாவது டெமோ கோப்பில் (எ.கா., `BasicChatDemo.java`) எக்ஸ்ப்ளோரரில் வலது கிளிக் செய்து **"Java ஐ இயக்குக"** தெரிவு செய்யலாம் அல்லது நடத்தை மற்றும் பிழைத்திருத்தவியல் குழாயில் இருந்து தொடக்கத்தை பயன்படுத்தலாம்.

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

## உதாரணங்கள் இயக்குக

**VS Code ஐப் பயன்படுத்துதல்:** எது வேண்டுமானாலும் டெமோ கோப்பில் பொருந்திய இடத்தில் வலது பட்டன் அழுத்தி **"Java ஐ இயக்குக"** தேர்வு செய்யவும், அல்லது நடத்தை மற்றும் பிழைத்திருத்தவியல் குழாயில் தொடக்கங்களைப் பயன்படுத்தவும் (முதலில் `.env` கோப்பில் token சேர்க்கப்பட்டிருப்பதை உறுதி செய்யவும்).

**Maven ஐப் பயன்படுத்துதல்:** மாற்றாக, கட்டளைக் கோலகுள் நகர்வில் இயக்கலாம்:

### 1. அடிப்படை உரையாடல்

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. கிளியிரை பாணிகள்

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

சூன்ய-காட்சிப்படுத்தல் (zero-shot), குறைந்த-காட்சிப்படுத்தல் (few-shot), சிந்தனை-சங்கிலி (chain-of-thought), மற்றும் பாத்திர அடிப்படையிலான (role-based) கிளியிரை முறைகளை காட்சிப்படுத்துகிறது.

### 3. செயலி அழைப்பு

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI தேவையானபோது உங்கள் Java முறைகளை தானாக அழைக்கிறது.

### 4. ஆவண கேள்வி & பதில் (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` உள்ளடக்கத்திற்கான கேள்விகளை கேளுங்கள்.

### 5. பொறுப்புள்ள AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI பாதுகாப்பு வடிகட்டிகள் தீங்கு விளைவிக்கும் உள்ளடக்கத்தை எவ்வாறு தடுப்பதைக் காண்பிக்கிறது.

## ஒவ்வொரு உதாரணமும் என்ன காட்டுகிறது

**அடிப்படை உரையாடல்** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j இன் அடிப்படைகளைக் காண இங்கு தொடங்குங்கள். நீங்கள் `OpenAiOfficialChatModel` ஐ உருவாக்கி, `.chat()` உடன் கிளியிரை அனுப்பு, பதிலைப் பெறுவீர்கள். இது அடித்தளத்தை விளக்கியது: தனிப்பயன் அங்கீகாரங்கள் மற்றும் API விசைகளுடன் மாடல்களை ஆரம்பிப்பது எப்படி. இந்த முறையை புரிந்துகொண்டதும், மற்ற அனைத்தும் அதற்குப் பொறுத்து உருவாகும்.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடல் முயற்சி செய்யுங்கள்:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) திறந்து கேளுங்கள்:
> - "இந்தக் குறியீட்டில் GitHub Models இடத்தில் Azure OpenAI யை எப்படி மாறுவேன்?"
> - "OpenAiOfficialChatModel.builder() இல் வேறு எந்த அம்சங்களை அமைக்க முடியும்?"
> - "முழுமையான பதிலுக்குப் பதிலாக ஸ்ட்ரீமிங் பதில்களை எப்படி சேர்க்கலாம்?"

**கிளியிரை பொறியியல்** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

நீங்கள் மாடலை எப்படி பேசுவது தெரிந்துவிட்டதால், அடுத்ததாக நீங்கள் என்ன சொல்கிறீர்கள் என்பதை ஆராய்வோம். இந்த டெமோ அதே மாடல் அமைப்பைப் பயன்படுத்துகிறது எனினும் ஐந்து விதமான கிளியிரை பாணிகளை காண்பிக்கிறது. நேரடி வழிமுறைகளுக்கான சூன்ய-காட்சிப்படுத்தல்கள், உதாரணங்களிலிருந்து கற்றுக்கொள்ளும் குறைந்த-காட்சிப்படுத்தல்கள், சிந்தனை படிநிலைகளை வெளிப்படுத்தும் ச_CHAIN-of-thought கிளியிரைகள் மற்றும் சூழலை அமைக்கும் பாத்திர அடிப்படையிலான கிளியிரைகள் ஆகியவை முயற்சி செய்யலாம். அதே மாடல் உங்கள் கோரிக்கையை எப்படி வித்தியாசமாக விடை சொல்கிறது என்பதைக் காண்பீர்கள்.

இந்த டெமோவை புறம், மறுபயன்பாட்டுக்கான கிளியிரைகளை உருவாக்கும் சக்தி வாய்ந்த வழி, `PromptTemplate` பயன்படுத்தும் கிளியிரை வடிவங்களைவும் காண்பிக்கிறது.
கீழ் உள்ள உதாரணம் LangChain4j இன் `PromptTemplate` ஐ பயன்படுத்தி மாதிரிவாதங்களைக் காட்சி செய்கிறது. AI முன் கொடுக்கப்பட்ட இடம் மற்றும் நடவடிக்கையின் அடிப்படையில் பதிலளிக்கும்.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடல் முயற்சி செய்யுங்கள்:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) திறந்து கேளுங்கள்:
> - "சூன்ய-காட்சிப்படுத்தல் மற்றும் குறைந்த-காட்சிப்படுத்தலை இன்றும் எப்போது பயன்படுத்த வேண்டும் என்ற வேறுபாடு என்ன?"
> - "தெளிவு உஷ்ணநிலை (temperature) அம்சம் மாடல் பதில்களில் எவ்வாறு தாக்கம் விடும்?"
> - "தயாரிப்பில் கிளியிரை ஊடுருவல் தாக்குதல்களைத் தடுக்கும் சில முறைகள் என்ன?"
> - "பொதுவான பாணிகளுக்கு மறுபயன்பாட்டுக்கான PromptTemplate பொருட்களை எப்படி உருவாக்கலாம்?"

**கருவி ஒருங்கிணைப்பு** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

இங்கே LangChain4j சக்திவாய்ந்ததாக மாறுகிறது. நீங்கள் `AiServices` ஐ பயன்படுத்தி உங்கள் Java முறைகளை அழைக்கக்கூடிய AI உதவியாளரை உருவாக்குவீர்கள். முறைகளை `@Tool("விவரம்")` என குறிக்கவும், LangChain4j வேறு அனைத்தையும் பராமரிக்கும் - பயனர் கேட்கும் தேவைப்படி AI தானாக கருவிகளை பயன்படுத்தத் தீர்மானிக்கும். இது செயலி அழைப்பை, AI செயல்பாடுகளுக்கு பதிலளிப்பதுமல்லாமல் நடவடிக்கைகளை எடுக்கும் முக்கிய தொழில்நுட்பத்தைக் காட்டுகிறது.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடல் முயற்சி செய்யுங்கள்:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) திறந்து கேளுங்கள்:
> - "@Tool குறி எப்படி வேலை செய்கிறது, LangChain4j இதை பின்னணி வேலைகளில் எப்படி கையாள்கிறது?"
> - "AI பல கருவிகளை தொடர் முறையில் அழைத்து சிக்கலான பிரச்சனைகளை தீர்க்க முடியுமா?"
> - "ஒரு கருவி தவறு உதைத்தால் - பிழைகளை எப்படி கையாள வேண்டும்?"
> - "இந்த கணக்காசூத்திர உதாரணத்திற்கு பதிலாக உண்மையான API இணைப்பை எப்படி செய்வேன்?"

**ஆவண கேள்வி & பதில் (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

இங்கு நீங்கள் RAG (உறுப்பமூட்டப்பட்ட உருவாக்கம்) அடிப்படையை காண்பீர்கள். மாடல் பயிற்சி தரவின் மீது பொருத்துமாறு நம்பாமல், நீங்கள் [`document.txt`](../../../00-quick-start/document.txt) இருந்து உள்ளடக்கத்தை ஏற்றிக் கொண்டு அதை கிளியிரையில் சேர்க்கிறீர்கள். AI உங்கள் ஆவணத்தின் அடிப்படையில் பதிலளிக்கும், பொதுவான அறிவில் அல்ல. இது உங்கள் சொந்த தரவுடன் இயங்க முடியும் அமைப்புகளை உருவாக்க முதல் படி.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **குறிப்பு:** இந்த எளிய முறை முழு ஆவணத்தையும் கிளியிரையில் ஏற்றுகிறது. பெரிய கோப்புகள் (>10KB) உள்ள போது குறியீட்டு வரம்புகளை மீறும். தொகுதி 03 இல் பயன்பாட்டு RAG அமைப்புகளுக்கான துண்டாக்கல் மற்றும் வெக்டர் தேடல் கையாளப்படுகிறது.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடல் முயற்சி செய்யுங்கள்:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) திறந்து கேளுங்கள்:
> - "RAG, மாடல் பயிற்சி தரவின் இணைப்பை தவிர்த்து AI தோற்றங்களை எவ்வாறு தடுக்கும்?"
> - "இந்த எளிய முறை மற்றும் தேடலுக்கு வெக்டர் உட்கோவை பயன்படுத்துவதில் உள்ள வேறுபாடு என்ன?"
> - "பல ஆவணங்கள் அல்லது பெரிய அறிவுத்தளங்களை கையாள இதை எப்படி விரிவுபடுத்த முடியும்?"
> - "AI கொடுக்கப்பட்ட உள்ளடக்கத்தை மட்டுமே பயன்படுத்த கிளியிரையை எப்படி அமைக்க சிறந்த முறைகள்?"

**பொறுப்புள்ள AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ஆழமான பாதுகாப்புடன் AI பாதுகாப்பை உருவாக்குங்கள். இந்த டெமோ இரண்டு உள்நிலை பாதுகாப்பு அடுக்குகளைக் காண்பிக்கிறது:

**பகுதி 1: LangChain4j உள்ளீட்டு பாதுகாப்பு வரம்புகள்** - LLM-க்கு செல்லும் அபாயகரமான கிளியிரைகளை தடுக்கும். தடைசெய்யப்பட்ட முக்கிய வார்த்தைகள் அல்லது மாதிரிகள் இருப்பதைக் கட்டுப்படுத்த தனிப்பயன் பாதுகாப்பு வரம்புகள் உருவாக்கலாம். இவை உங்கள் குறியீட்டில் இயங்குவதால் வேகம் கூடியது மற்றும் இலவசம்.

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

**பகுதி 2: வழங்குநர் பாதுகாப்பு வடிகட்டிகள்** - GitHub Models இரண்டாவது கட்டுப்பாடு உட்படுள்ளவை, உங்கள் பாதுகாப்பு வரம்புகள் காணாமல் விடும் உள்ளடக்கத்தை பிடிக்கும். கடுமையான தடைகள் (HTTP 400 பிழைகள்) மற்றும் மெல்லிய மறுத்தல்கள் (AI சிஷ்டமாக மறுப்பு) ஆகியவை காணப்படும்.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) உரையாடல் முயற்சி செய்யுங்கள்:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) திறந்து கேளுங்கள்:
> - "InputGuardrail என்றால் என்ன, எனது சொந்தத்தை எப்படி உருவாக்கலாம்?"
> - "கடுமையான தடையும் மெல்லிய மறுக்கும் இடையேயான வேறுபாடு என்ன?"
> - "எதற்கு இரண்டும், பாதுகாப்பு வரம்புகளும் வழங்குநர் வடிகட்டிகளும், ஒன்றாக பயன்படுத்தப்படுகின்றன?"

## அடுத்த படிகள்

**அடுத்த தொகுதி:** [01-அறிமுகம் - LangChain4j மற்றும் gpt-5 ஐ Azure இல் தொடங்குதல்](../01-introduction/README.md)

---

**நிசிலான njia:** [← முக்கியப் பக்கத்திற்கு திரும்பவும்](../README.md) | [அடுத்தது: Module 01 - அறிமுகம் →](../01-introduction/README.md)

---

## பிரச்சனைகளுக்கு தீர்வுகள்

### முதல் முறையான Maven கட்டமைப்பு

**பிரச்சனை:** ஆரம்ப `mvn clean compile` அல்லது `mvn package` இயக்கம் நீண்ட நேரம் (10-15 நிமிடங்கள்) எடுக்கிறது

**காரணம்:** Maven இரட்டை வரி மேற்கொண்டு அனைத்து திட்ட சார்புகளையும் (Spring Boot, LangChain4j நூலகங்கள், Azure SDKகள் மற்றும் பிறவற்றை) முதல்முறை கட்டமைப்பில் பதிவிறக்க வேண்டும்.

**தீர்வு:** இது சாதாரண நடத்தை. அடுத்த கட்டமைப்புகள் வேகமாக நடக்கும், காரணம் சார்புகள் உள்ளூர் சேமிக்கப்பட்டிருக்கும். பதிவிறக்க நேரம் உங்கள் இணைய வேகத்தை சார்ந்தது.
### PowerShell Maven கட்டளையின்.Syntax

**பிரச்சனை**: Maven கட்டளைகள் `Unknown lifecycle phase ".mainClass=..."` என்ற பிழையுடன் தோல்வியுறுகின்றன

**காரணம்**: PowerShell `=` ஐ மாறிலி ஒதுக்கீடின் ஆபரேட்டராக புரிந்து கொண்டு Maven சொத்து வாக்கியத்தைக் கெடுப்பது

**தீர்வு**: Maven கட்டளைக்கு முன்னர் நிறுத்து-பர்சிங் ஆபரேட்டர் `--%` ஐ பயன்படுத்தல்:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ஆபரேட்டர் PowerShellக்கு மீதமுள்ள அனைத்து வருமானங்களையும் விளக்கம் இல்லாமல் நேரடியாக Mavenக்கு அனுப்பச் செய்கிறது.

### Windows PowerShell எமோஜி காட்சி

**பிரச்சனை**: PowerShell இல் AI பதில்கள் எமோஜி பதிலாக குப்பை எழுத்துக்கள் (எ.கு., `????` அல்லது `â??`) வெளிப்படுகின்றன

**காரணம்**: PowerShell இன் இயல்புநிலை குறியீடு UTF-8 எமோஜி ஆதரிக்காது

**தீர்வு**: ஜாவா பயன்பாடுகளை இயக்கும் முன் இக்கட்டளையை இயக்கவும்:
```cmd
chcp 65001
```

இது டெர்மினலில் UTF-8 குறியீட்டை வலுப்படுத்துகிறது. மாற்றாக, சிறந்த யுனிகோட் ஆதரவு உடைய Windows Terminal ஐப் பயன்படுத்தவும்.

### API அழைப்புகளை பிழையணைக்குதல்

**பிரச்சனை**: அங்கீகார பிழைகள், கட்டுப்பாட்டு வரம்புகள், அல்லது எதிர்பாராத AI பதில்கள்

**தீர்வு**: எடுத்துக்காட்டுகள் `.logRequests(true)` மற்றும் `.logResponses(true)` ஐ உள்ளடக்குகின்றன, அவை API அழைப்புகளை கான்சோலில் காட்சிப்படுத்தும். இது அங்கீகார பிழைகள், கட்டுப்பாட்டு வரம்புகள் அல்லது எதிர்பாராத பதில்களை கண்டறிந்துகொள்ள உதவுகிறது. உற்பத்தியில் உள்ளவரை இந்தக் கொடிகளை அகற்றவும், இது பதிவுகளின் சத்தத்தை குறைக்கும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**பிரகடனம்**:
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்தை உறுதி செய்வதற்காக முயற்சித்தாலும், தானியங்கி மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்கலாம் என கவனத்தில் கொள்ளவும். அந்த மொழியில் உள்ள அசல் ஆவணம் அதிகாரபூர்வமான மூலமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழிபெயர்ப்பின் பயன்பாட்டிலிருந்து ஏற்பட்ட எந்த தவறுகளுக்கும் அல்லது தவறான புரிதல்களுக்கும் நாம் பொறுப்பேற்க மாட்டோம்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->