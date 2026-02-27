# ماڈیول 00: تیز آغاز

## فہرست مضامین

- [تعارف](../../../00-quick-start)
- [LangChain4j کیا ہے؟](../../../00-quick-start)
- [LangChain4j انحصار](../../../00-quick-start)
- [ضروریات](../../../00-quick-start)
- [سیٹ اپ](../../../00-quick-start)
  - [1. اپنا GitHub ٹوکن حاصل کریں](../../../00-quick-start)
  - [2. اپنا ٹوکن سیٹ کریں](../../../00-quick-start)
- [مثالیں چلائیں](../../../00-quick-start)
  - [1. بنیادی چیٹ](../../../00-quick-start)
  - [2. پرامپٹ پیٹرنز](../../../00-quick-start)
  - [3. فنکشن کالنگ](../../../00-quick-start)
  - [4. دستاویز Q&A (آسان RAG)](../../../00-quick-start)
  - [5. ذمہ دار AI](../../../00-quick-start)
- [ہر مثال میں کیا دکھایا گیا ہے](../../../00-quick-start)
- [اگلے مراحل](../../../00-quick-start)
- [مسائل کا حل](../../../00-quick-start)

## تعارف

یہ تیز آغاز آپ کو جتنا جلدی ممکن ہو LangChain4j کے ساتھ شروع کرنے میں مدد دینے کے لئے ہے۔ یہ LangChain4j اور GitHub ماڈلز کے ساتھ AI ایپلیکیشنز بنانے کی مکمل بنیادی باتوں کا احاطہ کرتا ہے۔ اگلے ماڈیولز میں آپ Azure OpenAI کو LangChain4j کے ساتھ استعمال کریں گے تاکہ مزید پیچیدہ ایپلیکیشنز بنائی جا سکیں۔

## LangChain4j کیا ہے؟

LangChain4j ایک جاوا لائبریری ہے جو AI سے چلنے والی ایپلیکیشنز بنانے کو آسان بناتی ہے۔ HTTP کلائنٹس اور JSON پارسنگ سے نمٹنے کی بجائے، آپ صاف ستھرے جاوا APIs کے ساتھ کام کرتے ہیں۔ 

LangChain میں "چین" متعدد اجزاء کو آپس میں جوڑنے کی طرف اشارہ کرتا ہے - آپ ایک پرامپٹ کو ماڈل کے ساتھ، پھر ایک پارسر کے ساتھ جوڑ سکتے ہیں، یا متعدد AI کالز کو اس طرح جوڑ سکتے ہیں جہاں ایک آؤٹ پٹ اگلے ان پٹ میں جاتا ہے۔ یہ تیز آغاز بنیادی باتوں پر توجہ دیتا ہے اس سے پہلے کہ زیادہ پیچیدہ چینز کی جانچ کی جائے۔

<img src="../../../translated_images/ur/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j میں اجزاء کی چیننگ - بلاکس کو جوڑ کر طاقتور AI ورک فلو بنانا*

ہم تین مرکزی اجزاء استعمال کریں گے:

**ChatModel** - AI ماڈل کے ساتھ انٹرایکشن کے لئے انٹرفیس۔ `model.chat("prompt")` کال کریں اور ایک جواب کا سٹرنگ حاصل کریں۔ ہم `OpenAiOfficialChatModel` استعمال کرتے ہیں جو GitHub ماڈلز جیسے OpenAI-مطابق اینڈپوائنٹس کے ساتھ کام کرتا ہے۔

**AiServices** - قسم-محفوظ AI سروس انٹرفیسز بناتا ہے۔ طریقے وضع کریں، انہیں `@Tool` کے ساتھ تشریح کریں، اور LangChain4j نظم و نسق سنبھالتا ہے۔ AI خود بخود جب ضرورت ہو آپ کے جاوا طریقوں کو کال کرتا ہے۔

**MessageWindowChatMemory** - بات چیت کی تاریخ کو برقرار رکھتا ہے۔ اس کے بغیر، ہر درخواست آزاد ہوتی ہے۔ اس کے ساتھ، AI پچھلے پیغامات کو یاد رکھتا ہے اور متعدد دوروں کے دوران سیاق و سباق برقرار رکھتا ہے۔

<img src="../../../translated_images/ur/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j فن تعمیر - مرکزی اجزاء ایک ساتھ کام کر کے آپ کی AI ایپلیکیشنز کو بااختیار بناتے ہیں*

## LangChain4j انحصار

یہ تیز آغاز [`pom.xml`](../../../00-quick-start/pom.xml) میں تین Maven انحصار استعمال کرتا ہے:

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

`langchain4j-open-ai-official` ماڈیول `OpenAiOfficialChatModel` کلاس فراہم کرتا ہے جو OpenAI-مطابق APIs سے منسلک ہوتا ہے۔ GitHub ماڈلز اسی API فارمیٹ کا استعمال کرتے ہیں، اس لیے کسی خاص اڈاپٹر کی ضرورت نہیں - بس بیس URL کو `https://models.github.ai/inference` پر پوائنٹ کریں۔

`langchain4j-easy-rag` ماڈیول خودکار دستاویز تقسیم، ایمبیڈنگ، اور بازیافت فراہم کرتا ہے تاکہ آپ بغیر دستی ترتیب دیے RAG ایپلیکیشنز بنا سکیں۔

## ضروریات

**کیا آپ ڈیولپمنٹ کنٹینر استعمال کر رہے ہیں؟** جاوا اور Maven پہلے سے نصب ہیں۔ آپ کو صرف ایک GitHub پرسنل ایکسس ٹوکن چاہیے۔

**مقامی ترقی:**
- جاوا 21+، Maven 3.9+
- GitHub پرسنل ایکسس ٹوکن (نیچے ہدایات)

> **نوٹ:** یہ ماڈیول GitHub ماڈلز سے `gpt-4.1-nano` استعمال کرتا ہے۔ کوڈ میں ماڈل کا نام تبدیل نہ کریں - یہ GitHub کے دستیاب ماڈلز کے مطابق ترتیب دیا گیا ہے۔

## سیٹ اپ

### 1. اپنا GitHub ٹوکن حاصل کریں

1. جائیں [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" پر کلک کریں
3. ایک وضاحتی نام دیں (مثلاً "LangChain4j Demo")
4. معیاد (7 دن کی تجویز)
5. "Account permissions" میں "Models" کو "Read-only" پر سیٹ کریں
6. "Generate token" پر کلک کریں
7. اپنا ٹوکن کاپی کریں اور محفوظ کریں - آپ اسے دوبارہ نہیں دیکھیں گے

### 2. اپنا ٹوکن سیٹ کریں

**آپشن 1: VS Code استعمال کرتے ہوئے (تجویز کردہ)**

اگر آپ VS Code استعمال کر رہے ہیں تو پروجیکٹ روٹ میں `.env` فائل میں اپنا ٹوکن شامل کریں:

اگر `.env` فائل موجود نہیں ہے تو `.env.example` کو `.env` میں کاپی کریں یا نیا `.env` فائل خود بنائیں۔

**مثال `.env` فائل:**
```bash
# میں /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

پھر آپ صرف کسی بھی ڈیمو فائل (مثلاً `BasicChatDemo.java`) پر رائٹ کلک کر کے **"Run Java"** منتخب کر سکتے ہیں یا Run and Debug پینل سے لانچ کنفیگریشنز استعمال کر سکتے ہیں۔

**آپشن 2: ٹرمینل استعمال کریں**

ٹوکن کو ماحول کے متغیر کے طور پر سیٹ کریں:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## مثالیں چلائیں

**VS Code استعمال کرتے ہوئے:** صرف کسی بھی ڈیمو فائل پر رائٹ کلک کریں اور **"Run Java"** منتخب کریں، یا Run and Debug پینل سے لانچ کنفیگریشنز استعمال کریں (یقینی بنائیں کہ آپ نے پہلے `.env` فائل میں اپنا ٹوکن شامل کیا ہے)۔

**Maven استعمال کرتے ہوئے:** متبادل کے طور پر، آپ کمانڈ لائن سے چلا سکتے ہیں:

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

AI خود بخود آپ کے جاوا طریقوں کو کال کرتا ہے جب ضرورت ہو۔

### 4. دستاویز Q&A (آسان RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

آسان RAG کے ساتھ خودکار ایمبیڈنگ اور بازیافت استعمال کرتے ہوئے اپنی دستاویزات کے بارے میں سوالات کریں۔

### 5. ذمہ دار AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

دیکھیں کہ AI کی حفاظتی فلٹرز کس طرح مضر مواد کو روکتی ہیں۔

## ہر مثال میں کیا دکھایا گیا ہے

**بنیادی چیٹ** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

یہاں سے شروع کریں تاکہ LangChain4j کو اس کی سب سے سادہ صورت میں دیکھ سکیں۔ آپ `OpenAiOfficialChatModel` بنائیں گے، `.chat()` کے ساتھ پرامپٹ بھیجیں گے، اور جواب حاصل کریں گے۔ یہ بنیاد دکھاتا ہے: کس طرح ماڈلز کو کسٹم اینڈپوائنٹس اور API کیز کے ساتھ شروع کریں۔ جب آپ اس پیٹرن کو سمجھ جائیں گے، سب کچھ اسی پر قائم ہوگا۔

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** کھولیں [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) اور پوچھیں:
> - "میں اس کوڈ میں GitHub ماڈلز سے Azure OpenAI میں کیسے سوئچ کروں؟"
> - "OpenAiOfficialChatModel.builder() میں اور کون سے پیرامیٹرز ترتیب دے سکتا ہوں؟"
> - "میں مکمل جواب کا انتظار کرنے کے بجائے کیسے اسٹریمنگ جوابات شامل کر سکتا ہوں؟"

**پرامپٹ انجینئرنگ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

اب جب آپ جان گئے کہ ماڈل سے بات کیسے کریں، آئیں دیکھتے ہیں کہ آپ اسے کیا کہتے ہیں۔ یہ ڈیمو وہی ماڈل سیٹ اپ استعمال کرتا ہے لیکن پانچ مختلف پرامپٹنگ پیٹرنز دکھاتا ہے۔ زیرو شاٹ پرامپٹ آزمائیں براہ راست ہدایات کے لیے، فیو شاٹ پرامپٹس جن سے مثالیں سیکھیں، چین آف تھوٹ پرامپٹس جو استدلال کے مراحل ظاہر کرتے ہیں، اور رول بیسڈ پرامپٹس جو سیاق و سباق طے کرتے ہیں۔ آپ دیکھیں گے کہ وہی ماڈل کس طرح بہت مختلف نتائج دیتا ہے اس پر منحصر ہے کہ آپ درخواست کیسے بناتے ہیں۔

یہ ڈیمو پرامپٹ ٹیمپلیٹس بھی دکھاتا ہے، جو متغیرات کے ساتھ دوبارہ قابل استعمال پرامپٹس بنانے کا طاقتور طریقہ ہیں۔
نیچے والا مثال LangChain4j `PromptTemplate` استعمال کرتے ہوئے متغیرات بھرنے کا ایک پرامپٹ دکھاتا ہے۔ AI فراہم کردہ منزل اور سرگرمی کی بنیاد پر جواب دے گا۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** کھولیں [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) اور پوچھیں:
> - "زیرو شاٹ اور فیو شاٹ پرامپٹنگ میں کیا فرق ہے، اور میں کب کس کا استعمال کروں؟"
> - "ٹیمپریچر پیرامیٹر ماڈل کے جوابات پر کیسے اثر انداز ہوتا ہے؟"
> - "پیداوار میں پرامپٹ انجیکشن حملوں کو روکنے کے لئے کیا تکنیک ہیں؟"
> - "میں عام پیٹرنز کے لئے دوبارہ قابل استعمال PromptTemplate آبجیکٹس کیسے بنا سکتا ہوں؟"

**ٹول انٹیگریشن** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

یہیں سے LangChain4j طاقتور بنتا ہے۔ آپ `AiServices` استعمال کریں گے تاکہ ایک AI اسسٹنٹ بنائیں جو آپ کے جاوا طریقے کال کر سکے۔ بس طریقوں کی تشریح کریں `@Tool("description")` کے ساتھ اور LangChain4j باقی نظم سنبھالتا ہے - AI خود بخود فیصلہ کرتا ہے کہ ہر ٹول کب استعمال کرنا ہے، صارف کی درخواست کی بنیاد پر۔ یہ فنکشن کالنگ کو دکھاتا ہے، جو AI بنانے کی کلیدی تکنیک ہے جو صرف سوالات کے جواب دینے کے بجائے عمل کر سکتا ہے۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** کھولیں [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) اور پوچھیں:
> - "@Tool تشریح کیسے کام کرتی ہے اور LangChain4j اس کے پیچھے کیا کرتا ہے؟"
> - "کیا AI پیچیدہ مسائل حل کرنے کے لئے متواتر طور پر متعدد ٹولز کال کر سکتا ہے؟"
> - "اگر کوئی ٹول استثنا پھینکے تو میں غلطیوں کو کیسے سنبھالوں؟"
> - "میں اس کیلکولیٹر مثال کی جگہ حقیقی API کیسے انٹیگریٹ کروں؟"

**دستاویز Q&A (آسان RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

یہاں آپ LangChain4j کے "آسان RAG" طریقہ استعمال کرتے ہوئے RAG (ریکوریئل-آگمینٹڈ جنریشن) دیکھیں گے۔ دستاویزات لوڈ کی جاتی ہیں، خودکار طور پر تقسیم اور ایمبیڈ کر کے میموری اسٹور میں ڈال دی جاتی ہیں، پھر ایک کنٹینٹ ریکوریور AI کو متعلقہ حصے وقتِ سوال فراہم کرتا ہے۔ AI آپ کی دستاویزات کی بنیاد پر جواب دیتا ہے، نہ کہ اپنی عمومی معلومات پر۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** کھولیں [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) اور پوچھیں:
> - "RAG AI ہیلوسنیشن کو ماڈل کی ٹریننگ ڈیٹا استعمال کرنے کے مقابلے میں کیسے روک دیتا ہے؟"
> - "اس آسان طریقہ اور کسٹم RAG پائپ لائن میں کیا فرق ہے؟"
> - "میں اسے متعدد دستاویزات یا بڑے نالج بیس کے لئے کیسے وسعت دوں؟"

**ذمہ دار AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

گہری دفاع کے ساتھ AI حفاظت بنائیں۔ یہ ڈیمو حفاظتی دو پرتوں کو دکھاتا ہے جو مل کر کام کرتی ہیں:

**حصہ 1: LangChain4j ان پٹ گارڈریل** - خطرناک پرامپٹس کو LLM تک پہنچنے سے پہلے روکیں۔ ایسی اپنی مرضی کی گارڈریل بنائیں جو ممنوعہ کلیدی الفاظ یا پیٹرنز چیک کرتی ہیں۔ یہ آپ کے کوڈ میں چلتی ہیں، اس لئے تیز اور مفت ہیں۔

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

**حصہ 2: فراہم کنندہ کی حفاظتی فلٹرز** - GitHub ماڈلز میں بلٹ ان فلٹرز ہیں جو آپ کی گارڈریل سے بچنے والی چیزوں کو پکڑتے ہیں۔ آپ سخت بلاکس (HTTP 400 کی خرابی) اور نرم انکار جہاں AI مہذب انداز میں انکار کرتا ہے، دیکھیں گے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** کھولیں [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) اور پوچھیں:
> - "InputGuardrail کیا ہے اور میں اپنا خود کیسے بنا سکتا ہوں؟"
> - "سخت بلاک اور نرم انکار میں کیا فرق ہے؟"
> - "میں گارڈریل اور فراہم کنندہ کے فلٹرز دونوں کیوں استعمال کروں؟"

## اگلے مراحل

**اگلا ماڈیول:** [01-introduction - LangChain4j اور gpt-5 کے ساتھ Azure پر شروع کرنا](../01-introduction/README.md)

---

**نیویگیشن:** [← مین پیج پر واپس](../README.md) | [اگلا: ماڈیول 01 - تعارف →](../01-introduction/README.md)

---

## مسائل کا حل

### پہلی بار Maven بلڈ

**مسئلہ:** ابتدائی `mvn clean compile` یا `mvn package` میں زیادہ وقت لگنا (10-15 منٹ)

**وجہ:** Maven کو پہلی بار تمام پروجیکٹ انحصارات (Spring Boot، LangChain4j لائبریریز، Azure SDKs، وغیرہ) ڈاؤنلوڈ کرنے ہوتے ہیں۔

**حل:** یہ معمول کی بات ہے۔ بعد کی بلڈز بہت تیز ہوں گی کیونکہ انحصارات مقامی طور پر کیش ہو جاتی ہیں۔ ڈاؤنلوڈ وقت آپ کے نیٹ ورک کی رفتار پر منحصر ہے۔

### PowerShell Maven کمانڈ کی ترکیب

**مسئلہ:** Maven کمانڈز `Unknown lifecycle phase ".mainClass=..."` کی خرابی کے ساتھ ناکام ہو جاتی ہیں۔
**باعث**: پاور شیل `=` کو متغیر تفویض آپریٹر کے طور پر سمجھتا ہے، جو میون پراپرٹی کے نحو کو توڑ دیتا ہے

**حل**: میون کمانڈ سے پہلے اسٹاپ-پارسنگ آپریٹر `--%` استعمال کریں:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` آپریٹر پاور شیل کو کہتا ہے کہ وہ باقی تمام دلائل کو بغیر کسی تشریح کے حرف بہ حرف میون کو دے۔

### ونڈوز پاور شیل ایموجی ڈسپلے

**مسئلہ**: AI جوابات پاور شیل میں ایموجیز کی بجائے گڑبڑ والے کریکٹرز (مثلاً `????` یا `â??`) دکھاتے ہیں

**باعث**: پاور شیل کی ڈیفالٹ انکوڈنگ UTF-8 ایموجیز کی حمایت نہیں کرتی

**حل**: جاوا ایپلیکیشنز چلانے سے پہلے یہ کمانڈ چلائیں:
```cmd
chcp 65001
```

یہ ٹرمینل میں UTF-8 انکوڈنگ کو جبراً فعال کرتا ہے۔ متبادل طور پر، ونڈوز ٹرمینل استعمال کریں جس میں بہتر یونیکوڈ سپورٹ موجود ہے۔

### API کالز کی ڈیبگنگ

**مسئلہ**: AI ماڈل سے توثیقی غلطیاں، ریٹ لمٹس، یا غیر متوقع جوابات

**حل**: مثالوں میں `.logRequests(true)` اور `.logResponses(true)` شامل ہیں تاکہ کنسول میں API کالز ظاہر ہوں۔ یہ توثیقی غلطیوں، ریٹ لمٹس، یا غیر متوقع جوابات کو مسئلہ حل کرنے میں مدد دیتا ہے۔ پروڈکشن میں لوگ شور کم کرنے کے لیے ان فلیگز کو ہٹا دیں۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**اعلانِ ذمہ داری**:
اس دستاویز کا ترجمہ AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کی مدد سے کیا گیا ہے۔ اگرچہ ہم درستگی کی بھرپور کوشش کرتے ہیں، لیکن براہِ کرم اس بات کا خیال رکھیں کہ خودکار ترجمے میں غلطیاں یا نقصانات ہو سکتے ہیں۔ اصل دستاویز اپنی مادری زبان میں ہی مستند ماخذ سمجھی جائے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ تجویز کیا جاتا ہے۔ ہم اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریحات کے ذمہ دار نہیں ہوں گے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->