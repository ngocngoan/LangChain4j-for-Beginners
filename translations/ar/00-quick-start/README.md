# الوحدة 00: بداية سريعة

## جدول المحتويات

- [مقدمة](../../../00-quick-start)
- [ما هو LangChain4j؟](../../../00-quick-start)
- [اعتمادات LangChain4j](../../../00-quick-start)
- [المتطلبات الأساسية](../../../00-quick-start)
- [الإعداد](../../../00-quick-start)
  - [1. احصل على رمز GitHub الخاص بك](../../../00-quick-start)
  - [2. قم بتعيين رمزك](../../../00-quick-start)
- [تشغيل الأمثلة](../../../00-quick-start)
  - [1. دردشة أساسية](../../../00-quick-start)
  - [2. أنماط المحفزات](../../../00-quick-start)
  - [3. مناداة الوظائف](../../../00-quick-start)
  - [4. أسئلة وأجوبة الوثائق (RAG)](../../../00-quick-start)
  - [5. الذكاء الاصطناعي المسؤول](../../../00-quick-start)
- [ما الذي يظهره كل مثال](../../../00-quick-start)
- [الخطوات التالية](../../../00-quick-start)
- [استكشاف الأخطاء وإصلاحها](../../../00-quick-start)

## مقدمة

تهدف هذه البداية السريعة إلى مساعدتك على تشغيل LangChain4j بأسرع وقت ممكن. تغطي هذه الوحدة الأساسيات المطلقة لبناء تطبيقات الذكاء الاصطناعي باستخدام LangChain4j ونماذج GitHub. في الوحدات التالية، ستستخدم Azure OpenAI مع LangChain4j لبناء تطبيقات أكثر تقدمًا.

## ما هو LangChain4j؟

LangChain4j هي مكتبة جافا تبسط بناء التطبيقات المعتمدة على الذكاء الاصطناعي. بدلاً من التعامل مع عملاء HTTP وتحليل JSON، تعمل مع واجهات برمجة تطبيقات جافا نظيفة.

ـ "السلسلة" في LangChain تشير إلى ربط مكونات متعددة معًا - قد تربط محفزًا بنموذج إلى محلل، أو توصل مكالمات الذكاء الاصطناعي حيث يُغذى إخراج واحد إلى الإدخال التالي. تركز هذه البداية السريعة على الأساسيات قبل استكشاف سلاسل أكثر تعقيدًا.

<img src="../../../translated_images/ar/langchain-concept.ad1fe6cf063515e1.webp" alt="مفهوم ربط LangChain4j" width="800"/>

*ربط المكونات في LangChain4j - الوحدات الأساسية تتصل لإنشاء تدفقات عمل ذكاء اصطناعي قوية*

سنستخدم ثلاثة مكونات أساسية:

**ChatLanguageModel** - واجهة للتفاعل مع نموذج الذكاء الاصطناعي. استدعِ `model.chat("prompt")` لتحصل على سلسلة استجابة. نستخدم `OpenAiOfficialChatModel` الذي يعمل مع نقاط نهاية متوافقة مع OpenAI مثل نماذج GitHub.

**AiServices** - ينشئ واجهات خدمات ذكاء اصطناعي آمنة نوعياً. عرّف الطرق، وعلّمها بـ `@Tool`، ويدير LangChain4j التنسيق. تستدعي الذكاء الاصطناعي طرق جافا الخاصة بك تلقائيًا عند الحاجة.

**MessageWindowChatMemory** - يحتفظ بسجل المحادثة. بدونها، كل طلب مستقل. بها، يتذكر الذكاء الاصطناعي الرسائل السابقة ويحافظ على السياق عبر عدة جولات.

<img src="../../../translated_images/ar/architecture.eedc993a1c576839.webp" alt="هندسة LangChain4j" width="800"/>

*هندسة LangChain4j - المكونات الأساسية تعمل معًا لتشغيل تطبيقات الذكاء الاصطناعي الخاصة بك*

## اعتمادات LangChain4j

تستخدم هذه البداية السريعة اثنين من اعتمادات Maven في [`pom.xml`](../../../00-quick-start/pom.xml):

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

يوفر موديل `langchain4j-open-ai-official` الفئة `OpenAiOfficialChatModel` التي تتصل بواجهات برمجة التطبيقات المتوافقة مع OpenAI. تستخدم نماذج GitHub نفس تنسيق الواجهة، لذلك لا حاجة لمحول خاص - فقط وجه عنوان URL الأساسي إلى `https://models.github.ai/inference`.

## المتطلبات الأساسية

**هل تستخدم حاوية التطوير؟** جافا وMaven مثبتان بالفعل. أنت بحاجة فقط إلى رمز وصول شخصي من GitHub.

**التطوير المحلي:**
- جافا 21 أو أحدث، Maven 3.9 أو أحدث
- رمز وصول شخصي من GitHub (تعليمات أدناه)

> **ملاحظة:** تستخدم هذه الوحدة نموذج `gpt-4.1-nano` من نماذج GitHub. لا تعدّل اسم النموذج في الكود - تم إعداده للعمل مع نماذج GitHub المتوفرة.

## الإعداد

### 1. احصل على رمز GitHub الخاص بك

1. اذهب إلى [إعدادات GitHub → رموز الوصول الشخصي](https://github.com/settings/personal-access-tokens)
2. انقر على "إنشاء رمز جديد"
3. عيّن اسمًا وصفيًا (مثل "عرض LangChain4j")
4. عيّن فترة انتهاء (7 أيام موصى بها)
5. ضمن "أذونات الحساب"، اعثر على "النماذج" واضبطها على "للقراءة فقط"
6. انقر على "إنشاء الرمز"
7. انسخ رمزك واحفظه - لن يظهر لك مرة أخرى

### 2. قم بتعيين رمزك

**الخيار 1: باستخدام VS Code (موصى به)**

إذا كنت تستخدم VS Code، أضف رمزك إلى ملف `.env` في جذر المشروع:

إذا لم يكن ملف `.env` موجودًا، انسخ `.env.example` إلى `.env` أو أنشئ ملف `.env` جديد في جذر المشروع.

**مثال لملف `.env`:**
```bash
# في /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

يمكنك ببساطة النقر بزر الماوس الأيمن على أي ملف عرض (مثل `BasicChatDemo.java`) في المستكشف واختيار **"تشغيل جافا"** أو استخدام تكوينات التشغيل من لوحة التشغيل والتحرير.

**الخيار 2: باستخدام الطرفية**

عيّن الرمز كمتغير بيئي:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## تشغيل الأمثلة

**باستخدام VS Code:** ببساطة انقر بزر الماوس الأيمن على أي ملف عرض في المستكشف واختر **"تشغيل جافا"** أو استخدم تكوينات التشغيل من لوحة التشغيل والتحرير (تأكد من إضافة رمزك أولاً إلى ملف `.env`).

**باستخدام Maven:** بدلاً من ذلك، يمكنك التشغيل من سطر الأوامر:

### 1. دردشة أساسية

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. أنماط المحفزات

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

يعرض الصفر-طلقة، القليل-طلقة، سلسلة التفكير، والمحاكاة بالأدوار.

### 3. مناداة الوظائف

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

يتصل الذكاء الاصطناعي تلقائيًا بطرق جافا الخاصة بك عند الحاجة.

### 4. أسئلة وأجوبة الوثائق (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

اطرح أسئلة حول محتوى `document.txt`.

### 5. الذكاء الاصطناعي المسؤول

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

شاهد كيف تمنع فلاتر أمان الذكاء الاصطناعي المحتوى الضار.

## ما الذي يظهره كل مثال

**الدردشة الأساسية** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ابدأ هنا لترى LangChain4j بأبسط صوره. ستنشئ `OpenAiOfficialChatModel`، ترسل محفزًا باستخدام `.chat()`، وتحصل على استجابة. يظهر هذا الأساس: كيفية تهيئة النماذج مع نقاط نهاية مخصصة ومفاتيح API. بمجرد فهم هذا النمط، يُبنى كل شيء آخر عليه.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) واسأل:
> - "كيف أتحول من نماذج GitHub إلى Azure OpenAI في هذا الكود؟"
> - "ما هي المعلمات الأخرى التي يمكنني تكوينها في OpenAiOfficialChatModel.builder()؟"
> - "كيف يمكنني إضافة استجابات متدفقة بدلاً من الانتظار للرد الكامل؟"

**هندسة المحفزات** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

الآن بعد أن عرفت كيف تتحدث إلى نموذج، دعنا نستكشف ما تقوله له. يستخدم هذا العرض نفس إعداد النموذج لكنه يعرض خمسة أنماط مختلفة من المحفزات. جرب المحفزات صفر-طلقة للتعليمات المباشرة، المحفزات قليل-طلقة التي تتعلم من الأمثلة، المحفزات سلسلة-التفكير التي تكشف خطوات المنطق، والمحاكاة بالأدوار التي تحدد السياق. سترى كيف يعطي نفس النموذج نتائج مختلفة بشكل كبير بناءً على كيفية تأطير طلبك.

يعرض العرض أيضًا قوالب المحفزات، وهي طريقة قوية لإنشاء محفزات قابلة لإعادة الاستخدام مع متغيرات.
يُظهر المثال أدناه محفزًا يستخدم `PromptTemplate` من LangChain4j لملء المتغيرات. يجيب الذكاء الاصطناعي بناءً على الوجهة والنشاط المزودين.

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

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) واسأل:
> - "ما الفرق بين المحفز صفر-طلقة وقليل-طلقة، ومتى يجب أن أستخدم كلًا منهما؟"
> - "كيف تؤثر معلمة درجة الحرارة على استجابات النموذج؟"
> - "ما هي بعض التقنيات لمنع هجمات حقن المحفزات في الإنتاج؟"
> - "كيف يمكنني إنشاء كائنات PromptTemplate قابلة لإعادة الاستخدام للأنماط الشائعة؟"

**دمج الأدوات** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

هنا يظهر LangChain4j قوته. ستستخدم `AiServices` لإنشاء مساعد ذكاء اصطناعي يمكنه مناداة طرق جافا الخاصة بك. فقط علّم الطرق بـ `@Tool("الوصف")` ويتولى LangChain4j الباقي - يقرر الذكاء الاصطناعي تلقائيًا متى يستخدم كل أداة بناءً على ما يسأله المستخدم. يظهر هذا مناداة الوظائف، وهي تقنية رئيسية لبناء ذكاء اصطناعي يمكنه اتخاذ إجراءات، وليس فقط الإجابة عن الأسئلة.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) واسأل:
> - "كيف تعمل التعليمة @Tool وماذا يفعل LangChain4j بها خلف الكواليس؟"
> - "هل يمكن للذكاء الاصطناعي استدعاء أدوات متعددة بالتتابع لحل مشاكل معقدة؟"
> - "ماذا يحدث إذا أطلقت أداة استثناءً — كيف يجب علي التعامل مع الأخطاء؟"
> - "كيف أدمج واجهة برمجة تطبيقات حقيقية بدلًا من مثال الآلة الحاسبة هذا؟"

**أسئلة وأجوبة الوثائق (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

هنا سترى أساس RAG (التوليد المعزز بالاسترجاع). بدلاً من الاعتماد على بيانات تدريب النموذج، تقوم بتحميل محتوى من [`document.txt`](../../../00-quick-start/document.txt) وتدرجه في المحفز. يجيب الذكاء الاصطناعي بناءً على مستندك، وليس على معرفته العامة. هذه هي الخطوة الأولى نحو بناء أنظمة يمكنها العمل مع بياناتك الخاصة.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **ملاحظة:** هذه الطريقة البسيطة تحمل المستند بأكمله في المحفز. للملفات الكبيرة (>10KB)، ستتجاوز حدود السياق. تغطي الوحدة 03 تقسيم الوثائق وبحث المتجهات لأنظمة RAG للإنتاج.

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) واسأل:
> - "كيف يمنع RAG الهلوسات في الذكاء الاصطناعي مقارنة باستخدام بيانات تدريب النموذج؟"
> - "ما الفرق بين هذه الطريقة البسيطة واستخدام تضمينات المتجهات للاسترجاع؟"
> - "كيف أوسع هذا للتعامل مع مستندات متعددة أو قواعد معرفة أكبر؟"
> - "ما هي أفضل الممارسات لهندسة المحفز لضمان استخدام الذكاء الاصطناعي للسياق المزود فقط؟"

**الذكاء الاصطناعي المسؤول** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ابنِ أمان الذكاء الاصطناعي بالدفاع متعدد الطبقات. يعرض هذا العرض طبقتين من الحماية تعملان معًا:

**الجزء 1: حواجز إدخال LangChain4j** - تمنع المحفزات الخطرة قبل وصولها إلى LLM. أنشئ حواجز مخصصة تفحص الكلمات المفتاحية أو الأنماط المحظورة. تعمل هذه في كودك، لذا فهي سريعة ومجانية.

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

**الجزء 2: فلاتر أمان المزود** - لدى نماذج GitHub فلاتر مدمجة تلتقط ما قد يفوته الحواجز الخاصة بك. سترى حظرًا صارمًا (أخطاء HTTP 400) للمخالفات الشديدة ورفضًا ناعمًا حيث يرفض الذكاء الاصطناعي بأدب.

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) واسأل:
> - "ما هو InputGuardrail وكيف أنشئ واحدًا خاصًا بي؟"
> - "ما الفرق بين الحظر الصارم والرفض الناعم؟"
> - "لماذا نستخدم الحواجز وفلاتر المزود معًا؟"

## الخطوات التالية

**الوحدة التالية:** [01-introduction - البدء مع LangChain4j و gpt-5 على Azure](../01-introduction/README.md)

---

**التنقل:** [← العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 01 - مقدمة →](../01-introduction/README.md)

---

## استكشاف الأخطاء وإصلاحها

### أول بناء Maven

**المشكلة**: يستغرق الأمر وقتًا طويلاً (10-15 دقيقة) عند `mvn clean compile` أو `mvn package` لأول مرة

**السبب**: يحتاج Maven إلى تنزيل جميع اعتمادات المشروع (Spring Boot، مكتبات LangChain4j، SDKات Azure، إلخ) في البناء الأول.

**الحل**: هذا سلوك طبيعي. ستكون عمليات البناء اللاحقة أسرع بكثير لأن الاعتمادات مخزنة محليًا. يعتمد وقت التنزيل على سرعة الشبكة لديك.
### بناء جملة أوامر Maven في PowerShell

**المشكلة**: تفشل أوامر Maven بخطأ `Unknown lifecycle phase ".mainClass=..."`

**السبب**: يقوم PowerShell بتفسير `=` كعامل تعيين متغير، مما يكسر بناء جملة خصائص Maven

**الحل**: استخدم عامل إيقاف التحليل `--%` قبل أمر Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

العامل `--%` يخبر PowerShell بتمرير جميع الوسائط المتبقية حرفيًا إلى Maven دون تفسير.

### عرض الإيموجي في Windows PowerShell

**المشكلة**: تظهر استجابات الذكاء الاصطناعي أحرفًا غير مفهومة (مثل `????` أو `â??`) بدلاً من الإيموجي في PowerShell

**السبب**: ترميز PowerShell الافتراضي لا يدعم إيموجي UTF-8

**الحل**: نفّذ هذا الأمر قبل تشغيل تطبيقات Java:
```cmd
chcp 65001
```

هذا يجبر استخدام ترميز UTF-8 في الطرفية. بدلاً من ذلك، استخدم Windows Terminal الذي يدعم Unicode بشكل أفضل.

### تصحيح أخطاء استدعاءات API

**المشكلة**: أخطاء المصادقة، حدود المعدل، أو استجابات غير متوقعة من نموذج الذكاء الاصطناعي

**الحل**: تشتمل الأمثلة على `.logRequests(true)` و `.logResponses(true)` لعرض استدعاءات API في وحدة التحكم. هذا يساعد في استكشاف أخطاء المصادقة، حدود المعدل، أو الاستجابات غير المتوقعة. قم بإزالة هذه العلامات في الإنتاج لتقليل ضوضاء السجل.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق به. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->