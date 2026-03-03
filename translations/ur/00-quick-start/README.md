# ماڈیول 00: تیز آغاز

## فہرست مضامین

- [تعارف](../../../00-quick-start)
- [LangChain4j کیا ہے؟](../../../00-quick-start)
- [LangChain4j کی منحصرات](../../../00-quick-start)
- [ضروریات](../../../00-quick-start)
- [سیٹ اپ](../../../00-quick-start)
  - [1. اپنا GitHub ٹوکن حاصل کریں](../../../00-quick-start)
  - [2. اپنا ٹوکن سیٹ کریں](../../../00-quick-start)
- [مثالیں چلائیں](../../../00-quick-start)
  - [1. بنیادی چیٹ](../../../00-quick-start)
  - [2. پرامپٹ پیٹرنز](../../../00-quick-start)
  - [3. فنکشن کالنگ](../../../00-quick-start)
  - [4. دستاویز سوال و جواب (آسان RAG)](../../../00-quick-start)
  - [5. ذمہ دار AI](../../../00-quick-start)
- [ہر مثال کیا دکھاتی ہے](../../../00-quick-start)
- [آگے کے مراحل](../../../00-quick-start)
- [مسائل کا حل](../../../00-quick-start)

## تعارف

یہ تیز آغاز آپ کو LangChain4j کے ساتھ جلد از جلد کام شروع کرنے کے لیے ہے۔ یہ LangChain4j اور GitHub Models کے ساتھ AI ایپلیکیشنز بنانے کی بنیادی باتیں کور کرتا ہے۔ اگلے ماڈیولز میں آپ Azure OpenAI اور GPT-5.2 پر منتقل ہوں گے اور ہر تصور کو مزید گہرائی سے سمجھیں گے۔

## LangChain4j کیا ہے؟

LangChain4j ایک Java لائبریری ہے جو AI سے چلنے والی ایپلیکیشنز بنانے کو آسان بناتی ہے۔ HTTP کلائنٹس اور JSON تجزیہ کے بجائے، آپ صاف ستھری Java APIs کے ساتھ کام کرتے ہیں۔

LangChain میں "چین" سے مراد متعدد اجزاء کو آپس میں جوڑنا ہے - آپ پرامپٹ کو ماڈل سے پارسر تک چیک کر سکتے ہیں، یا متعدد AI کالز کو ایک دوسرے کے ساتھ سلسلے وار جوڑ سکتے ہیں جہاں ایک آؤٹ پٹ اگلے ان پٹ میں جاتا ہے۔ یہ تیز آغاز بنیادی اصولوں پر توجہ دیتا ہے اس سے پہلے کہ آپ مزید پیچیدہ چینز کو دریافت کریں۔

<img src="../../../translated_images/ur/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j میں اجزاء کو سلسلے وار جوڑنا - بلاکس کو جوڑ کر طاقتور AI ورک فلو بنانا*

ہم تین بنیادی اجزاء استعمال کریں گے:

**ChatModel** - AI ماڈل کے ساتھ بات چیت کا انٹرفیس۔ `model.chat("prompt")` کال کریں اور جواب حاصل کریں۔ ہم `OpenAiOfficialChatModel` استعمال کرتے ہیں جو OpenAI-مطابقت والے اینڈ پوائنٹس جیسے GitHub Models کے ساتھ کام کرتا ہے۔

**AiServices** - قسم محفوظ AI سروس انٹرفیسز بناتا ہے۔ طریقے define کریں، ان پر `@Tool` کا ٹیگ لگائیں، اور LangChain4j انتظام کرتا ہے۔ AI آپ کے Java طریقوں کو خود بخود کال کرتا ہے جب ضرورت ہو۔

**MessageWindowChatMemory** - گفتگو کی تاریخ کو برقرار رکھتا ہے۔ اس کے بغیر ہر درخواست آزاد ہوتی ہے۔ اس کے ساتھ، AI پچھلے پیغامات کو یاد رکھتا ہے اور متعدد ٹرنز میں سیاق و سباق بنائے رکھتا ہے۔

<img src="../../../translated_images/ur/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j کی ساخت - بنیادی اجزاء مل کر آپ کی AI ایپلیکیشنز کو چلانے کے لیے کام کرتے ہیں*

## LangChain4j کی منحصرات

یہ تیز آغاز [`pom.xml`](../../../00-quick-start/pom.xml) میں تین Maven منحصرات استعمال کرتا ہے:

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
  
`langchain4j-open-ai-official` ماڈیول `OpenAiOfficialChatModel` کلاس فراہم کرتا ہے جو OpenAI-مطابقت رکھنے والی APIs سے جڑتا ہے۔ GitHub Models اسی API فارمٹ استعمال کرتا ہے، اس لیے کوئی خاص اڈاپٹر نہیں چاہیے - بس بیس URL کو `https://models.github.ai/inference` پر پوائنٹ کریں۔

`langchain4j-easy-rag` ماڈیول خودکار دستاویزی تقسیم، ایمبیڈنگ اور بازیافت فراہم کرتا ہے تاکہ آپ بغیر دستی طور پر ہر مرحلہ مرتب کیے RAG ایپلیکیشنز بنا سکیں۔

## ضروریات

**Dev Container استعمال کر رہے ہیں؟** Java اور Maven پہلے سے انسٹال ہیں۔ آپ کو صرف GitHub Personal Access Token چاہیے۔

**مقامی ترقی:**
- Java 21+، Maven 3.9+
- GitHub Personal Access Token (نیچے ہدایات)

> **نوٹ:** یہ ماڈیول GitHub Models سے `gpt-4.1-nano` استعمال کرتا ہے۔ کوڈ میں ماڈل نام تبدیل نہ کریں - یہ GitHub کے دستیاب ماڈلز کے ساتھ کام کرنے کے لیے ترتیب دیا گیا ہے۔

## سیٹ اپ

### 1. اپنا GitHub ٹوکن حاصل کریں

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) پر جائیں  
2. "Generate new token" پر کلک کریں  
3. ایک وضاحتی نام دیں (مثلاً "LangChain4j Demo")  
4. مدت ختم ہونے کی مدت مقرر کریں (7 دن کی سفارش کی جاتی ہے)  
5. "Account permissions" میں "Models" پر جائیں اور اسے "Read-only" پر سیٹ کریں  
6. "Generate token" پر کلک کریں  
7. اپنا ٹوکن کاپی کریں اور محفوظ رکھیں - آپ کو یہ دوبارہ نظر نہیں آئے گا  

### 2. اپنا ٹوکن سیٹ کریں

**آپشن 1: VS Code استعمال کرتے ہوئے (تجویز کردہ)**

اگر آپ VS Code استعمال کر رہے ہیں تو اپنے پراجیکٹ کے روٹ میں `.env` فائل میں اپنا ٹوکن شامل کریں:

اگر `.env` فائل موجود نہیں، تو `.env.example` کو `.env` میں کاپی کریں یا نیا `.env` فائل بنائیں۔

**مثال `.env` فائل:**  
```bash
# میں /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
پھر آپ کسی بھی ڈیمو فائل (مثلاً `BasicChatDemo.java`) پر ایکسپلورر میں رائٹ کلک کر کے **"Run Java"** منتخب کریں یا Run and Debug پینل سے لانچ کنفیگریشنز استعمال کریں۔

**آپشن 2: ٹرمینل استعمال کرتے ہوئے**

ٹوکن کو بطور ماحول کا متغیر سیٹ کریں:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  

## مثالیں چلائیں

**VS Code استعمال کرتے ہوئے:** کسی بھی ڈیمو فائل پر رائٹ کلک کریں اور **"Run Java"** منتخب کریں، یا Run and Debug پینل سے لانچ کنفیگریشنز استعمال کریں (پہلے `.env` میں اپنا ٹوکن شامل کرنا یقینی بنائیں)۔

**Maven استعمال کرتے ہوئے:** متبادل طور پر، آپ کمانڈ لائن سے چلائیں:

### 1. بنیادی چیٹ

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  

### 2. پرامپٹ پیٹرنز

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
زیرو شاٹ، فیو شاٹ، چین آف تھوٹ، اور رول بیسڈ پرامپٹنگ دکھاتا ہے۔

### 3. فنکشن کالنگ

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
AI ضرورت پڑنے پر خود بخود آپ کے Java طریقے کال کرتا ہے۔

### 4. دستاویز سوال و جواب (آسان RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
آسان RAG کے ساتھ خودکار ایمبیڈنگ اور بازیافت کے ذریعے اپنے دستاویزات کے بارے میں سوالات پوچھیں۔

### 5. ذمہ دار AI

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
دیکھیں کہ AI سیفٹی فلٹرز کس طرح نقصان دہ مواد کو روک دیتے ہیں۔

## ہر مثال کیا دکھاتی ہے

**بنیادی چیٹ** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

یہاں شروع کریں تاکہ LangChain4j کو اس کی سادگی میں دیکھ سکیں۔ آپ ایک `OpenAiOfficialChatModel` بنائیں گے، `.chat()` کے ذریعے پرامپٹ بھیجیں گے، اور جواب حاصل کریں گے۔ یہ بنیاد دکھاتا ہے: ماڈلز کو کس طرح کسٹم اینڈ پوائنٹس اور API کیز کے ساتھ initialize کرنا ہے۔ ایک بار جب آپ یہ پیٹرن سمجھ لیں، باقی سب اس پر مبنی ہوگا۔

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 GitHub Copilot Chat کے ساتھ آزمائیں:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) کھولیں اور پوچھیں:  
> - "میں اس کوڈ میں GitHub Models سے Azure OpenAI کیسے سوئچ کروں؟"  
> - "OpenAiOfficialChatModel.builder() میں کون سے اور پیرامیٹرز مرتب کر سکتا ہوں؟"  
> - "میں مکمل جواب کا انتظار کیے بغیر اسٹریم شدہ جوابات کیسے شامل کروں؟"  

**پرامپٹ انجینئرنگ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

اب جب آپ کو ماڈل سے بات کرنا آتا ہے، تو دیکھیں کہ آپ اس سے کیا کہتے ہیں۔ یہ ڈیمو وہی ماڈل سیٹ اپ استعمال کرتا ہے لیکن پانچ مختلف پرامپٹنگ پیٹرنز دکھاتا ہے۔ براہ راست ہدایتوں کے لیے زیرو شاٹ پرامپٹ آزمائیں، مثالوں سے سیکھنے کے لیے فیو شاٹ، استدلال کے مراحل کے لیے چین آف تھوٹ، اور سیاق و سباق کے لیے رول بیسڈ پرامپٹ۔ آپ دیکھیں گے کہ ایک ہی ماڈل کس قدر مختلف نتائج دیتا ہے کہ آپ اپنی درخواست کیسے فریم کرتے ہیں۔

یہ ڈیمو پرامپٹ ٹیمپلیٹس بھی دکھاتا ہے، جو متغیرات کے ساتھ دوبارہ قابل استعمال پرامپٹس بنانے کا ایک طاقتور طریقہ ہے۔  
نیچے والا مثال LangChain4j `PromptTemplate` استعمال کرتا ہے تاکہ متغیرات بھریں۔ AI دی گئی منزل اور سرگرمی کی بنیاد پر جواب دے گا۔

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
  
> **🤖 GitHub Copilot Chat کے ساتھ آزمائیں:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) کھولیں اور پوچھیں:  
> - "زیرو شاٹ اور فیو شاٹ پرامپٹنگ میں کیا فرق ہے، اور مجھے کب کون سا استعمال کرنا چاہیے؟"  
> - "درجہ حرارت (temperature) پیرامیٹر ماڈل کے جوابات کو کیسے متاثر کرتا ہے؟"  
> - "پروڈکشن میں پرامپٹ انجیکشن حملوں کو روکنے کی کیا تکنیکیں ہیں؟"  
> - "عام پیٹرنز کے لیے reusable PromptTemplate آبجیکٹس کیسے بناؤں؟"  

**ٹول انٹیگریشن** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

یہاں LangChain4j طاقتور ہو جاتا ہے۔ آپ `AiServices` استعمال کریں گے تاکہ AI معاون بنائیں جو آپ کے Java طریقے کال کرے۔ بس اپنے طریقوں پر `@Tool("description")` کا ٹیگ لگائیں اور LangChain4j باقی انتظام کرے گا - AI خود بخود فیصلہ کرتا ہے کہ صارف کیا پوچھتا ہے اس کی بنیاد پر کون سا ٹول کب استعمال کرنا ہے۔ یہ فنکشن کالنگ ظاہر کرتا ہے، جو AI کو صرف سوالوں کا جواب دینے کی بجائے عمل کرنے کے قابل بناتا ہے۔

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
  
> **🤖 GitHub Copilot Chat کے ساتھ آزمائیں:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) کھولیں اور پوچھیں:  
> - "@Tool انوٹیشن کیسے کام کرتی ہے اور LangChain4j اس کے پیچھے کیا کرتا ہے؟"  
> - "کیا AI ایک سلسلے میں متعدد ٹولز کال کر سکتا ہے تاکہ پیچیدہ مسائل حل کرے؟"  
> - "اگر کوئی ٹول استثنا پھینکے تو میں غلطیوں کو کیسے ہینڈل کروں؟"  
> - "میں اس کیلکولیٹر مثال کے بجائے اصلی API کیسے انٹیگریٹ کروں؟"  

**دستاویز سوال و جواب (آسان RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

یہاں آپ LangChain4j کی "Easy RAG" اپروچ کے ذریعے RAG (retrieval-augmented generation) دیکھیں گے۔ دستاویزات لوڈ ہوتی ہیں، خودکار طریقے سے تقسیم اور ایمبیڈ کی جاتی ہیں، پھر ایک مواد بازیافت کنندہ وقتِ سوال AI کو متعلقہ حصے فراہم کرتا ہے۔ AI آپ کی دستاویزات کی بنیاد پر جواب دیتا ہے، اپنی عمومی معلومات نہیں۔

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
  
> **🤖 GitHub Copilot Chat کے ساتھ آزمائیں:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) کھولیں اور پوچھیں:  
> - "ماڈل کی تربیتی ڈیٹا کے مقابلے میں RAG AI کے ہیلوسینیشن کو کیسے روکتا ہے؟"  
> - "یہ آسان طریقہ اور کسٹم RAG پائپ لائن میں کیا فرق ہے؟"  
> - "میں اسے متعدد دستاویزات یا بڑے نالج بیس کے لیے کیسے اسکیل کر سکتا ہوں؟"  

**ذمہ دار AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

گہرائی سے دفاع کے ساتھ AI سیفٹی بنائیں۔ یہ ڈیمو دو حفاظتی پرتیں دکھاتا ہے جو اکٹھے کام کرتی ہیں:

**حصہ 1: LangChain4j ان پٹ گارڈریل** - خطرناک پرامپٹس کو LLM تک پہنچنے سے پہلے روکتا ہے۔ اپنی کسٹم گارڈریل بنائیں جو ممنوعہ الفاظ یا پیٹرنز کو چیک کرتی ہیں۔ یہ آپ کے کوڈ میں چلتے ہیں، اس لیے تیز اور مفت ہیں۔

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
  
**حصہ 2: پرووائیڈر سیفٹی فلٹرز** - GitHub Models میں بلٹ ان فلٹرز ہیں جو آپ کی گارڈریل کی ممکنہ کمی کو پورا کرتے ہیں۔ آپ سخت بلاکس (HTTP 400 ایررز) اور نرم انکار دیکھیں گے جہاں AI شائستگی سے انکار کرتا ہے۔

> **🤖 GitHub Copilot Chat کے ساتھ آزمائیں:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) کھولیں اور پوچھیں:  
> - "InputGuardrail کیا ہے اور میں اپنا کیسے بناؤں؟"  
> - "سخت بلاک اور نرم انکار میں کیا فرق ہے؟"  
> - "گارڈریل اور پرووائیڈر فلٹرز دونوں استعمال کرنے کی کیا وجہ ہے؟"  

## آگے کے مراحل

**اگلا ماڈیول:** [01-introduction - LangChain4j کے ساتھ شروعات](../01-introduction/README.md)

---

**نیوگیشن:** [← مین پیج پر واپس](../README.md) | [اگلا: ماڈیول 01 - تعارف →](../01-introduction/README.md)

---

## مسائل کا حل

### پہلی بار Maven بلڈ

**مسئلہ:** ابتدائی `mvn clean compile` یا `mvn package` میں طویل وقت لگنا (10-15 منٹ)

**سبب:** Maven کو پہلی بار تمام پروجیکٹ منحصرات ڈاؤن لوڈ کرنے کی ضرورت ہوتی ہے (Spring Boot، LangChain4j لائبریریز، Azure SDKs، وغیرہ)۔

**حل:** یہ معمول کی بات ہے۔ بعد کی بلڈز زیادہ تیز ہوں گی کیونکہ منحصرات لوکل کیش ہو چکی ہوتی ہیں۔ ڈاؤن لوڈ کا وقت آپ کے نیٹ ورک کی رفتار پر منحصر ہے۔

### PowerShell Maven کمانڈ کی نحو

**مسئلہ:** Maven کمانڈز `Unknown lifecycle phase ".mainClass=..."` کی خرابی کے ساتھ ناکام ہونا
**وجہ**: PowerShell `=` کو ویری ایبل اسائنمنٹ آپریٹر کے طور پر سمجھتا ہے، جس سے Maven پراپرٹی کا سینٹیکس ٹوٹ جاتا ہے

**حل**: Maven کمانڈ سے پہلے stop-parsing آپریٹر `--%` استعمال کریں:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` آپریٹر PowerShell کو بتاتا ہے کہ وہ باقی تمام دلائل کو بغیر کسی تشریح کے بالکل ویسے ہی Maven کو بھیج دے۔

### Windows PowerShell میں ایموجی کی نمائش

**مسئلہ**: PowerShell میں AI جوابات ایموجیز کی بجائے بے معنی حروف دکھاتے ہیں (جیسے `????` یا `â??`)

**وجہ**: PowerShell کی ڈیفالٹ انکوڈنگ UTF-8 ایموجیز کی حمایت نہیں کرتی

**حل**: جاوا ایپلیکیشنز چلانے سے پہلے یہ کمانڈ چلائیں:
```cmd
chcp 65001
```

یہ ٹرمینل میں UTF-8 انکوڈنگ کو زبردستی فعال کرتا ہے۔ متبادل طور پر، Windows Terminal استعمال کریں جو بہتر یونیکوڈ سپورٹ فراہم کرتا ہے۔

### API کالز کی ڈیبگنگ

**مسئلہ**: تصدیقی غلطیاں، ریٹ لمٹس، یا AI ماڈل سے غیر متوقع جوابات

**حل**: مثالوں میں `.logRequests(true)` اور `.logResponses(true)` شامل ہیں تاکہ API کالز کنسول میں دکھائی دیں۔ یہ تصدیقی غلطیوں، ریٹ لمٹس، یا غیر متوقع جوابات کو حل کرنے میں مدد دیتا ہے۔ پیداوار میں ان فلیگز کو ہٹا دیں تاکہ لاگ شور کم ہو۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**اہم اطلاع**:  
یہ دستاویز AI تراجم کی سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کی بھرپور کوشش کرتے ہیں، براہ کرم اس بات کا خیال رکھیں کہ خودکار تراجم میں غلطیاں یا نا درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنی مادری زبان میں ہی معتبر ماخذ سمجھی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ تجویز کیا جاتا ہے۔ اس ترجمے کے استعمال سے ہونے والی کسی بھی غلط فہمی یا غلط تعبیر کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->