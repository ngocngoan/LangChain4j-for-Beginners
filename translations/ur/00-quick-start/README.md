# ماڈیول 00: فوری آغاز

## مواد کی فہرست

- [تعارف](../../../00-quick-start)
- [LangChain4j کیا ہے؟](../../../00-quick-start)
- [LangChain4j کی انحصاریاں](../../../00-quick-start)
- [ضروری شرائط](../../../00-quick-start)
- [سیٹ اپ](../../../00-quick-start)
  - [1. اپنا GitHub ٹوکن حاصل کریں](../../../00-quick-start)
  - [2. اپنا ٹوکن سیٹ کریں](../../../00-quick-start)
- [مثالیں چلائیں](../../../00-quick-start)
  - [1. بنیادی چیٹ](../../../00-quick-start)
  - [2. پرامپٹ پیٹرنز](../../../00-quick-start)
  - [3. فنکشن کالنگ](../../../00-quick-start)
  - [4. دستاویز کا سوال و جواب (RAG)](../../../00-quick-start)
  - [5. ذمہ دار AI](../../../00-quick-start)
- [ہر مثال کیا دکھاتی ہے](../../../00-quick-start)
- [اگلے اقدامات](../../../00-quick-start)
- [مسائل کا حل](../../../00-quick-start)

## تعارف

یہ فوری آغاز آپ کو LangChain4j کے ساتھ جلد از جلد کام شروع کرنے کے لیے بنایا گیا ہے۔ یہ LangChain4j اور GitHub ماڈلز کے ساتھ AI ایپلیکیشنز بنانے کی انتہائی بنیادی باتوں کا احاطہ کرتا ہے۔ اگلے ماڈیولز میں آپ Azure OpenAI کو LangChain4j کے ساتھ استعمال کرتے ہوئے زیادہ پیچیدہ ایپلیکیشنز بنائیں گے۔

## LangChain4j کیا ہے؟

LangChain4j جاوا کی ایک لائبریری ہے جو AI سے چلنے والی ایپلیکیشنز بنانے کو آسان بناتی ہے۔ HTTP کلائنٹس اور JSON پارسنگ کے بجائے، آپ صاف جاوا APIs کے ساتھ کام کرتے ہیں۔

LangChain میں "چین" سے مراد متعدد اجزاء کو آپس میں جوڑنا ہے - آپ ایک پرامپٹ کو ماڈل کے ساتھ جوڑ سکتے ہیں یا کئی AI کالز کو اس طرح چین کرسکتے ہیں کہ ایک آؤٹ پٹ اگلے ان پٹ میں فیڈ ہو۔ یہ فوری آغاز بنیادی باتوں پر توجہ دیتا ہے اس سے پہلے کہ ہم مزید پیچیدہ چینز کو دریافت کریں۔

<img src="../../../translated_images/ur/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j میں اجزاء کو جوڑنا - تعمیراتی بلاکس جو طاقتور AI ورک فلوز بنانے کے لیے جڑے ہوتے ہیں*

ہم تین بنیادی اجزاء استعمال کریں گے:

**ChatLanguageModel** - AI ماڈل کے تعاملات کے لیے انٹرفیس۔ `model.chat("prompt")` کال کریں اور ردعمل حاصل کریں۔ ہم `OpenAiOfficialChatModel` استعمال کرتے ہیں جو OpenAI-مطابق اینڈپوائنٹس جیسے GitHub ماڈلز کے ساتھ کام کرتا ہے۔

**AiServices** - ٹائپ-سیف AI سروس انٹرفیس تیار کرتا ہے۔ طریقے متعین کریں، انہیں `@Tool` کے ساتھ نشان زد کریں، اور LangChain4j تنظیم کا خیال رکھتا ہے۔ AI خود بخود آپ کے جاوا طریقوں کو جب ضرورت ہو کال کرتا ہے۔

**MessageWindowChatMemory** - گفتگو کی تاریخ کو برقرار رکھتا ہے۔ اس کے بغیر ہر درخواست آزاد ہوتی ہے۔ اس کے ساتھ، AI پچھلے پیغامات کو یاد رکھتا ہے اور مختلف ٹرنز میں سیاق و سباق کو برقرار رکھتا ہے۔

<img src="../../../translated_images/ur/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j کی ساخت - بنیادی اجزاء جو آپ کی AI ایپلیکیشنز کو طاقت دیتے ہیں*

## LangChain4j کی انحصاریاں

یہ فوری آغاز دو Maven انحصاریوں کو [`pom.xml`](../../../00-quick-start/pom.xml) میں استعمال کرتا ہے:

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

`langchain4j-open-ai-official` ماڈیول `OpenAiOfficialChatModel` کلاس فراہم کرتا ہے جو OpenAI-مطابق APIs سے جڑتا ہے۔ GitHub ماڈلز بھی اسی API فارمیٹ کو استعمال کرتے ہیں، اس لیے کوئی خاص ایڈاپٹر درکار نہیں - بس بیس URL کو `https://models.github.ai/inference` پر سیٹ کریں۔

## ضروری شرائط

**Dev Container استعمال کر رہے ہیں؟** Java اور Maven پہلے سے انسٹال ہیں۔ آپ کو صرف GitHub Personal Access Token کی ضرورت ہے۔

**لوکل ڈیویلپمنٹ:**
- Java 21+، Maven 3.9+
- GitHub Personal Access Token (نیچے دی گئی ہدایات)

> **نوٹ:** اس ماڈیول میں `gpt-4.1-nano` GitHub ماڈلز سے استعمال ہوتا ہے۔ کوڈ میں ماڈل کا نام تبدیل نہ کریں - یہ GitHub کے دستیاب ماڈلز کے لیے کنفیگرڈ ہے۔

## سیٹ اپ

### 1. اپنا GitHub ٹوکن حاصل کریں

1. جائیں [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" پر کلک کریں
3. ایک واضح نام دیں (مثلاً "LangChain4j Demo")
4. مدت ختم ہونے کا وقت سیٹ کریں (7 دن کی سفارش ہے)
5. "Account permissions" میں "Models" تلاش کریں اور اسے "Read-only" پر رکھیں
6. "Generate token" پر کلک کریں
7. اپنا ٹوکن کاپی کریں اور محفوظ کریں - دوبارہ نہیں دیکھیں گے

### 2. اپنا ٹوکن سیٹ کریں

**اختیار 1: VS Code استعمال کرنا (تجویز کردہ)**

اگر آپ VS Code استعمال کر رہے ہیں، تو اپنے پروجیکٹ کی روٹ میں `.env` فائل میں اپنا ٹوکن ڈالیں:

اگر `.env` فائل موجود نہیں ہے تو `.env.example` کو `.env` میں کاپی کریں یا نیا `.env` فائل بنائیں۔

**مثال `.env` فائل:**
```bash
# میں /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

پھر آپ کسی بھی ڈیمو فائل (جیسے `BasicChatDemo.java`) پر ایکسپلورر میں رائٹ کلک کرکے **"Run Java"** منتخب کر سکتے ہیں یا رن اور ڈیبگ پینل سے لانچ کنفیگریشنز استعمال کر سکتے ہیں۔

**اختیار 2: ٹرمینل استعمال کرنا**

ٹوکن کو ماحول کی متغیر کے طور پر سیٹ کریں:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## مثالیں چلائیں

**VS Code استعمال کرتے ہوئے:** ایکسپلورر میں کسی بھی ڈیمو فائل پر رائٹ کلک کریں اور **"Run Java"** منتخب کریں، یا رن اور ڈیبگ پینل سے لانچ کنفیگریشنز کا استعمال کریں (یہ یقینی بنائیں کہ پہلے آپ نے `.env` فائل میں اپنا ٹوکن شامل کیا ہے)۔

**Maven استعمال کرتے ہوئے:** متبادل طور پر آپ کمانڈ لائن سے چلا سکتے ہیں:

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

زیرو شاٹ، فیو شاٹ، چین آف تھاتھ، اور رول-بیسڈ پرامپٹنگ دکھاتا ہے۔

### 3. فنکشن کالنگ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI خود بخود جب ضرورت ہو آپ کے جاوا طریقے کال کرتا ہے۔

### 4. دستاویز کا سوال و جواب (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` میں مواد کے بارے میں سوالات کریں۔

### 5. ذمہ دار AI

**بش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

دیکھیں کہ AI سیفٹی فلٹرز نقصان دہ مواد کو کیسے روک دیتے ہیں۔

## ہر مثال کیا دکھاتی ہے

**بنیادی چیٹ** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

یہاں سے شروع کریں کہ LangChain4j کو اس کی سادگی میں دیکھیں۔ آپ ایک `OpenAiOfficialChatModel` بنائیں گے، `.chat()` کے ساتھ پرامپٹ بھیجیں گے، اور ردعمل حاصل کریں گے۔ یہ بنیاد ظاہر کرتا ہے: کس طرح ماڈلز کو کسٹم اینڈپوائنٹس اور API کلیدوں کے ساتھ شروع کیا جاتا ہے۔ جب یہ پیٹرن سمجھ لیں تو باقی سب اسی پر مبنی ہیں۔

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) کھولیں اور پوچھیں:
> - "میں اس کوڈ میں GitHub Models سے Azure OpenAI پر کیسے سوئچ کروں؟"
> - "OpenAiOfficialChatModel.builder() میں میں اور کیا پیرامیٹرز ترتیب دے سکتا ہوں؟"
> - "مکمل جواب کے انتظار کے بجائے اسٹریمنگ جوابات کیسے شامل کروں؟"

**پرامپٹ انجینئرنگ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

اب جب آپ جانتے ہیں کہ ماڈل سے بات کیسے کرنی ہے، تو چلیں دیکھتے ہیں کہ آپ اس سے کیا کہتے ہیں۔ یہ ڈیمو اسی ماڈل سیٹ اپ کا استعمال کرتا ہے لیکن پانچ مختلف پرامپٹ پیٹرنز دکھاتا ہے۔ زیرو شاٹ پرامپٹس کے لیے براہ راست ہدایات، فیو شاٹ پرامپٹس جو مثالوں سے سیکھتے ہیں، چین آف تھاتھ پرامپٹس جو منطق کے مراحل ظاہر کرتے ہیں، اور رول-بیسڈ پرامپٹس جو سیاق و سباق متعین کرتے ہیں آزمائیں۔ آپ دیکھیں گے کہ وہی ماڈل آپ کی درخواست کی ساخت پر منحصر بہت مختلف نتائج دیتا ہے۔

ڈیمو میں پرامپٹ ٹیمپلیٹس بھی دکھائے گئے ہیں، جو متغیرات کے ساتھ قابل استعمال پرامپٹس تیار کرنے کا طاقتور طریقہ ہیں۔
نیچے کی مثال LangChain4j `PromptTemplate` کا استعمال کرتے ہوئے متغیرات کو بھرنے والا پرامپٹ دکھاتی ہے۔ AI دی گئی منزل اور سرگرمی کی بنیاد پر جواب دے گا۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) کھولیں اور پوچھیں:
> - "زیرو شاٹ اور فیو شاٹ پرامپٹنگ میں کیا فرق ہے، اور مجھے کب کون سا استعمال کرنا چاہیے؟"
> - "ماڈل کے جوابات پر temperature پیرامیٹر کا کیا اثر ہے؟"
> - "پروڈکشن میں پرامپٹ انجیکشن حملوں کو روکنے کے لیے کوئی تکنیکیں کیا ہیں؟"
> - "عام پیٹرنز کے لیے دوبارہ استعمال ہونے والے PromptTemplate آبجیکٹس کیسے بنا سکتا ہوں؟"

**ٹول انٹیگریشن** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

یہاں LangChain4j طاقتور ہو جاتا ہے۔ آپ `AiServices` استعمال کریں گے تاکہ AI اسسٹنٹ بنایا جا سکے جو آپ کے جاوا طریقے کال کر سکے۔ صرف طریقوں کو `@Tool("description")` کے ساتھ نشان زد کریں اور LangChain4j باقی کا خیال رکھتا ہے - AI خود بخود ہر ٹول کا استعمال اس وقت کرتا ہے جب صارف کو ضرورت ہو۔ یہ فنکشن کالنگ کو ظاہر کرتا ہے، جو AI بنانے کے لیے اہم تکنیک ہے جو صرف سوالات کا جواب نہیں دیتی بلکہ کارروائی بھی کرتی ہے۔

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) کھولیں اور پوچھیں:
> - "@Tool تشریح کیسے کام کرتی ہے اور LangChain4j اس کے پیچھے کیا کرتا ہے؟"
> - "کیا AI پیچیدہ مسائل حل کرنے کے لیے متعدد ٹولز کو تسلسل میں کال کر سکتا ہے؟"
> - "اگر کوئی ٹول استثنا پھینکے تو کیا ہوتا ہے - میں غلطیوں کو کیسے سنبھالوں؟"
> - "اس کیلکولیٹر مثال کے بجائے اصلی API کو کس طرح شامل کروں؟"

**دستاویز کا سوال و جواب (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

یہاں آپ RAG (retrieval-augmented generation) کی بنیاد دیکھیں گے۔ ماڈل کے تربیتی ڈیٹا پر انحصار کرنے کے بجائے، آپ [`document.txt`](../../../00-quick-start/document.txt) سے مواد لوڈ کرتے ہیں اور اسے پرامپٹ میں شامل کرتے ہیں۔ AI آپ کے دستاویز پر مبنی جواب دیتا ہے، نہ کہ اس کی عمومی معلومات پر۔ یہ اپنے ڈیٹا کے ساتھ کام کرنے والے نظام بنانے کا پہلا قدم ہے۔

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **نوٹ:** یہ سادہ طریقہ پورا دستاویز پرامپٹ میں لوڈ کرتا ہے۔ بڑے فائلوں (>10KB) کے لیے آپ سیاق و سباق کی حدیں عبور کر لیں گے۔ ماڈیول 03 میں چنکنگ اور ویکٹر سرچ کے لئے پروڈکشن RAG سسٹمز شامل ہیں۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) کھولیں اور پوچھیں:
> - "RAG ماڈل کے تربیتی ڈیٹا استعمال کرنے کے مقابلے میں AI ہیلوسینیشنز کو کیسے روکتا ہے؟"
> - "اس سادہ طریقے اور ویکٹر ایمبیڈنگز کے ذریعے ریٹریول میں کیا فرق ہے؟"
> - "اسے متعدد دستاویزات یا بڑے نالج بیس سے نمٹنے کے لیے کیسے اسکیل کر سکتا ہوں؟"
> - "AI کو فراہم کردہ سیاق و سباق استعمال کرنے کے لیے بہترین طریقے کیا ہیں؟"

**ذمہ دار AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

دیپتھ میں حفاظتی تہوں کے ساتھ AI سیفٹی بنائیں۔ یہ ڈیمو دو تہوں کی حفاظت دکھاتا ہے جو ایک ساتھ کام کرتی ہیں:

**حصہ 1: LangChain4j Input Guardrails** - خطرناک پرامپٹس کو LLM تک پہنچنے سے پہلے بلاک کرنا۔ اپنی مرضی کے گارڈریل بنائیں جو ممنوعہ کلیدی الفاظ یا پیٹرنز چیک کرتے ہیں۔ یہ آپ کے کوڈ میں چلتے ہیں، اس لیے یہ تیز اور مفت ہیں۔

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

**حصہ 2: پرووائیڈر سیفٹی فلٹرز** - GitHub ماڈلز میں بلٹ ان فلٹرز ہیں جو آپ کے گارڈریل کی چوک کو پکڑتے ہیں۔ آپ سخت بلاکس (HTTP 400 ایررز) سنگین خلاف ورزیوں کے لیے اور نرم انکار جہاں AI مؤدبانہ انکار کرتا ہے دیکھیں گے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) کھولیں اور پوچھیں:
> - "InputGuardrail کیا ہے اور میں اپنا کیسے بنا سکتا ہوں؟"
> - "سخت بلاک اور نرم انکار میں کیا فرق ہے؟"
> - "کیوں دونوں گارڈریل اور پرووائیڈر فلٹرز کو ساتھ استعمال کیا جائے؟"

## اگلے اقدامات

**اگلا ماڈیول:** [01-introduction - LangChain4j اور gpt-5 پر Azure کے ساتھ شروعات](../01-introduction/README.md)

---

**نیویگیشن:** [← مرکزی صفحہ پر واپس](../README.md) | [اگلا: ماڈیول 01 - تعارف →](../01-introduction/README.md)

---

## مسائل کا حل

### پہلی بار Maven بلڈ

**مسئلہ:** ابتدا میں `mvn clean compile` یا `mvn package` میں طویل وقت لگتا ہے (10-15 منٹ)

**وجہ:** Maven کو پہلی بار تمام پروجیکٹ انحصاریاں (Spring Boot، LangChain4j لائبریریز، Azure SDKs، وغیرہ) ڈاؤن لوڈ کرنی ہوتی ہیں۔

**حل:** یہ معمول کا رویہ ہے۔ بعد کے بلڈز بہت تیز ہوں گے کیونکہ انحصاریاں لوکل کیش ہو چکی ہوں گی۔ ڈاؤن لوڈ کا وقت آپ کے نیٹ ورک سپیڈ پر منحصر ہے۔
### پاور شیل میون کمانڈ کی ترکیب

**مسئلہ**: میون کمانڈز میں خرابی آتی ہے `Unknown lifecycle phase ".mainClass=..."`

**سبب**: پاور شیل `=` کو ویری ایبل اسائنمنٹ آپریٹر سمجھتا ہے، جو میون پراپرٹی ترکیب کو توڑ دیتا ہے

**حل**: میون کمانڈ سے پہلے اسٹاپ-پارسنگ آپریٹر `--%` کا استعمال کریں:

**پاور شیل:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**باش:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

آپریٹر `--%` پاور شیل کو باقی دلائل کو بالکل ویسے ہی میون کو پارس کیے بغیر منتقل کرنے کا کہتا ہے۔

### ونڈوز پاور شیل میں ایموجی کی نمائش

**مسئلہ**: AI جوابات میں پاور شیل میں ایموجیز کی جگہ گندہ علامات (مثلاً `????` یا `â??`) دکھائی دیتی ہیں

**سبب**: پاور شیل کی ڈیفالٹ انکوڈنگ UTF-8 ایموجیز کی حمایت نہیں کرتی

**حل**: جاوا ایپلیکیشنز چلانے سے پہلے یہ کمانڈ چلائیں:
```cmd
chcp 65001
```

یہ ٹرمینل میں UTF-8 انکوڈنگ کو مجبور کرتا ہے۔ متبادل طور پر، Windows Terminal استعمال کریں جس میں یونیکوڈ کی بہتر سپورٹ ہے۔

### API کالز کی ڈیبگنگ

**مسئلہ**: مصدقہ غلطیاں، ریٹ لمٹس، یا AI ماڈل سے غیر متوقع جوابات

**حل**: مثالوں میں `.logRequests(true)` اور `.logResponses(true)` شامل ہیں تاکہ کنسول میں API کالز دکھائی جا سکیں۔ یہ مصدقہ غلطیوں، ریٹ لمٹس، یا غیر متوقع جوابات کو حل کرنے میں مدد دیتے ہیں۔ پروڈکشن میں ان فلیگز کو ہٹا دیں تاکہ لاگ کی تعداد کم ہو۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دسکلیمَر**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا کمی بیشی ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں حتمی اور مستند ماخذ سمجھا جانا چاہیے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ تجویز کیا جاتا ہے۔ ہم اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تعبیر کے ذمہ دار نہیں ہیں۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->