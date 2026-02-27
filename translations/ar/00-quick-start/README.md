# الوحدة 00: البداية السريعة

## جدول المحتويات

- [مقدمة](../../../00-quick-start)
- [ما هو LangChain4j؟](../../../00-quick-start)
- [اعتمادات LangChain4j](../../../00-quick-start)
- [المتطلبات الأساسية](../../../00-quick-start)
- [الإعداد](../../../00-quick-start)
  - [1. احصل على رمز GitHub الخاص بك](../../../00-quick-start)
  - [2. ضبط رمزك](../../../00-quick-start)
- [تشغيل الأمثلة](../../../00-quick-start)
  - [1. الدردشة الأساسية](../../../00-quick-start)
  - [2. أنماط المطالبات](../../../00-quick-start)
  - [3. استدعاء الدوال](../../../00-quick-start)
  - [4. الأسئلة والأجوبة في المستندات (Easy RAG)](../../../00-quick-start)
  - [5. الذكاء الاصطناعي المسؤول](../../../00-quick-start)
- [ما الذي يوضحه كل مثال](../../../00-quick-start)
- [الخطوات التالية](../../../00-quick-start)
- [حل المشكلات](../../../00-quick-start)

## مقدمة

تهدف هذه البداية السريعة إلى تمكينك من البدء مع LangChain4j بأسرع وقت ممكن. تغطي الأساسيات المطلقة لبناء تطبيقات الذكاء الاصطناعي باستخدام LangChain4j ونماذج GitHub. في الوحدات القادمة ستستخدم Azure OpenAI مع LangChain4j لبناء تطبيقات أكثر تقدمًا.

## ما هو LangChain4j؟

LangChain4j هي مكتبة جافا تبسط بناء تطبيقات مدعومة بالذكاء الاصطناعي. بدلاً من التعامل مع عملاء HTTP وتحليل JSON، تعمل مع واجهات برمجة تطبيقات جافا نظيفة.

تشير كلمة "chain" في LangChain إلى ربط مكونات متعددة معًا - قد تربط مطالبة بنموذج ثم بمحلل، أو تربط عدة مكالمات للذكاء الاصطناعي حيث يتم تمرير مخرج إلى المدخل التالي. تركز هذه البداية السريعة على الأساسيات قبل استكشاف سلاسل أكثر تعقيدًا.

<img src="../../../translated_images/ar/langchain-concept.ad1fe6cf063515e1.webp" alt="مفهوم ربط LangChain4j" width="800"/>

*ربط المكونات في LangChain4j - الوحدات البنائية تتصل لإنشاء تدفقات عمل ذكاء اصطناعي قوية*

سنستخدم ثلاث مكونات أساسية:

**ChatModel** - واجهة التفاعل مع نموذج الذكاء الاصطناعي. استدعاء `model.chat("prompt")` والحصول على سلسلة استجابة. نستخدم `OpenAiOfficialChatModel` الذي يعمل مع نقاط نهاية متوافقة مع OpenAI مثل نماذج GitHub.

**AiServices** - تنشئ واجهات خدمة ذكاء اصطناعي آمنة من حيث النوع. عرّف الطرق، علق عليها بـ `@Tool`، ويتولى LangChain4j التنسيق. يقوم الذكاء الاصطناعي باستدعاء طرق جافا الخاصة بك تلقائيًا عند الحاجة.

**MessageWindowChatMemory** - يحافظ على سجل المحادثة. بدونها، كل طلب مستقل. معها، يذكر الذكاء الاصطناعي الرسائل السابقة ويحافظ على السياق عبر عدة جولات.

<img src="../../../translated_images/ar/architecture.eedc993a1c576839.webp" alt="بنية LangChain4j" width="800"/>

*بنية LangChain4j - المكونات الأساسية تعمل معًا لتشغيل تطبيقات الذكاء الاصطناعي الخاصة بك*

## اعتمادات LangChain4j

تستخدم هذه البداية السريعة ثلاث اعتمادات Maven في [`pom.xml`](../../../00-quick-start/pom.xml):

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


يوفر الموديل `langchain4j-open-ai-official` فئة `OpenAiOfficialChatModel` التي تتصل بواجهات API متوافقة مع OpenAI. يستخدم GitHub Models نفس صيغة API، لذا لا يحتاج إلى محول خاص - فقط وجه عنوان URL الأساسي إلى `https://models.github.ai/inference`.

يوفر الموديل `langchain4j-easy-rag` تقسيم المستندات التلقائي، والتضمين، والاسترجاع حتى تتمكن من بناء تطبيقات RAG بدون إعداد يدوي لكل خطوة.

## المتطلبات الأساسية

**هل تستخدم حاوية التطوير؟** Java وMaven مثبتان بالفعل. تحتاج فقط إلى رمز وصول شخصي من GitHub.

**التطوير المحلي:**
- Java 21+، Maven 3.9+
- رمز الوصول الشخصي لـ GitHub (التعليمات أدناه)

> **ملاحظة:** تستخدم هذه الوحدة `gpt-4.1-nano` من نماذج GitHub. لا تغير اسم النموذج في الكود - فهو مهيأ للعمل مع نماذج GitHub المتوفرة.

## الإعداد

### 1. احصل على رمز GitHub الخاص بك

1. انتقل إلى [إعدادات GitHub → رموز الوصول الشخصية](https://github.com/settings/personal-access-tokens)
2. انقر على "إنشاء رمز جديد"
3. ضع اسمًا وصفيًا (مثلاً: "عرض LangChain4j")
4. اضبط تاريخ الانتهاء (7 أيام موصى بها)
5. تحت "أذونات الحساب"، ابحث عن "نماذج" واضبطها على "قراءة فقط"
6. انقر على "إنشاء رمز"
7. انسخ رمزك واحفظه - لن تراه مرة أخرى

### 2. ضبط رمزك

**الخيار 1: استخدام VS Code (موصى به)**

إذا كنت تستخدم VS Code، أضف رمزك إلى ملف `.env` في جذر المشروع:

إذا لم يكن ملف `.env` موجودًا، انسخ `.env.example` إلى `.env` أو أنشئ ملف `.env` جديد في جذر المشروع.

**مثال على ملف `.env`:**
```bash
# في /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```


بعد ذلك يمكنك ببساطة النقر بزر الفأرة الأيمن على أي ملف تجريبي (مثلاً `BasicChatDemo.java`) في المستعرض واختيار **"تشغيل جافا"** أو استخدام إعدادات التشغيل من لوحة التشغيل والتصحيح.

**الخيار 2: باستخدام الطرفية**

اضبط الرمز كمتغير بيئي:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```


## تشغيل الأمثلة

**باستخدام VS Code:** ببساطة انقر بزر الفأرة الأيمن على أي ملف تجريبي في المستعرض واختر **"تشغيل جافا"**، أو استخدم إعدادات التشغيل من لوحة التشغيل والتصحيح (تأكد من إضافة رمزك إلى ملف `.env` أولاً).

**باستخدام Maven:** بدلاً من ذلك يمكنك التشغيل من سطر الأوامر:

### 1. الدردشة الأساسية

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```


### 2. أنماط المطالبات

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```


يعرض محادثات بدون أمثلة، مع أمثلة قليلة، سلسلة التفكير، ومطالبات قائمة على الأدوار.

### 3. استدعاء الدوال

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```


يقوم الذكاء الاصطناعي بالاتصال التلقائي بطرق جافا الخاصة بك عندما يكون ذلك مطلوبًا.

### 4. الأسئلة والأجوبة في المستندات (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```


اطرح أسئلة حول مستنداتك باستخدام Easy RAG مع التضمين والاسترجاع التلقائي.

### 5. الذكاء الاصطناعي المسؤول

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```


شاهد كيف تحجب مرشحات أمان الذكاء الاصطناعي المحتوى الضار.

## ما الذي يوضحه كل مثال

**الدردشة الأساسية** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ابدأ من هنا لرؤية LangChain4j في أبسط صوره. ستنشئ `OpenAiOfficialChatModel`، ترسل مطالبة باستخدام `.chat()`، وتحصل على رد. يظهر هذا الأساس: كيف تهيئ النماذج بنقاط نهاية مخصصة ومفاتيح API. بمجرد فهم هذا النمط، يبنى كل شيء آخر عليه.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 جرّب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) واسأل:
> - "كيف يمكنني التبديل من نماذج GitHub إلى Azure OpenAI في هذا الكود؟"
> - "ما هي المعلمات الأخرى التي يمكنني تكوينها في OpenAiOfficialChatModel.builder()؟"
> - "كيف أضيف ردودًا متدفقة بدلًا من الانتظار للحصول على الرد الكامل؟"

**هندسة المطالبات** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

الآن بعد أن عرفت كيفية التحدث إلى نموذج، دعنا نستكشف ما تقوله له. يستخدم هذا العرض نفس إعداد النموذج لكنه يعرض خمسة أنماط مختلفة للمطالبات. جرب مطالبات بدون أمثلة للتعليمات المباشرة، مطالبات بأمثلة قليلة للتعلم من الأمثلة، مطالبات سلسلة التفكير التي تكشف خطوات الاستدلال، والمطالبات القائمة على الأدوار التي تضبط السياق. سترى كيف يقدم نفس النموذج نتائج مختلفة بشكل كبير اعتمادًا على كيفية صياغة طلبك.

يعرض العرض أيضًا قوالب المطالبات، وهي طريقة قوية لإنشاء مطالبات قابلة لإعادة الاستخدام مع متغيرات.
يوضح المثال أدناه مطالبة تستخدم `PromptTemplate` من LangChain4j لملء المتغيرات. سيجيب الذكاء الاصطناعي بناءً على الوجهة والنشاط المقدمين.

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

> **🤖 جرّب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) واسأل:
> - "ما الفرق بين المطالبة بدون أمثلة والأمثلة القليلة، ومتى يجب استخدام كل منهما؟"
> - "كيف تؤثر معلمة درجة الحرارة على ردود النموذج؟"
> - "ما هي بعض التقنيات لمنع هجمات حقن المطالبات في الإنتاج؟"
> - "كيف يمكنني إنشاء كائنات PromptTemplate قابلة لإعادة الاستخدام للأنماط الشائعة؟"

**دمج الأدوات** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

هنا يصبح LangChain4j قويًا. ستستخدم `AiServices` لإنشاء مساعد ذكاء اصطناعي يمكنه استدعاء طرق جافا الخاصة بك. فقط علق الطرق بـ `@Tool("الوصف")` ويتولى LangChain4j الباقي - يقرر الذكاء الاصطناعي تلقائيًا متى يستخدم كل أداة بناءً على ما يطلبه المستخدم. يوضح هذا استدعاء الدوال، وهي تقنية رئيسية لبناء ذكاء اصطناعي يمكنه اتخاذ إجراءات، وليس مجرد الإجابة على الأسئلة.

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

> **🤖 جرّب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) واسأل:
> - "كيف تعمل التعليمة @Tool وماذا يفعل LangChain4j بها في الخلفية؟"
> - "هل يمكن للذكاء الاصطناعي استدعاء أدوات متعددة بالتتابع لحل مشكلات معقدة؟"
> - "ماذا يحدث إذا رمى أداة استثناءً - كيف يجب التعامل مع الأخطاء؟"
> - "كيف سأدمج واجهة API حقيقية بدلاً من هذا المثال الحاسوبي؟"

**الأسئلة والأجوبة في المستندات (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

هنا سترى RAG (التوليد المدعوم بالاسترجاع) باستخدام نهج "Easy RAG" في LangChain4j. يتم تحميل المستندات وتقسيمها تلقائيًا وتضمينها في مخزن ذاكرة مؤقت، ثم يوفر مسترجع المحتوى أجزاء ذات صلة للذكاء الاصطناعي عند وقت الاستعلام. يجيب الذكاء الاصطناعي بناءً على مستنداتك وليس على معرفته العامة.

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

> **🤖 جرّب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) واسأل:
> - "كيف يمنع RAG الهلوسات في الذكاء الاصطناعي مقارنة باستخدام بيانات تدريب النموذج؟"
> - "ما الفرق بين هذا النهج السهل وخط أنابيب RAG مخصص؟"
> - "كيف أوسع هذا ليشمل مستندات متعددة أو قواعد معرفة أكبر؟"

**الذكاء الاصطناعي المسؤول** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ابنِ أمان الذكاء الاصطناعي بدفاع متعدد الطبقات. يظهر هذا العرض طبقتين من الحماية تعملان معًا:

**الجزء 1: حواجز LangChain4j للمدخلات** - تحجب المطالبات الخطيرة قبل وصولها إلى LLM. أنشئ حواجز مخصصة تتحقق من كلمات أو أنماط محظورة. تعمل هذه داخل كودك، لذا فهي سريعة ومجانية.

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

**الجزء 2: مرشحات الأمان المزود** - لدى نماذج GitHub مرشحات مدمجة تلتقط ما قد تفوّته حواجزك. سترى حواجز صارمة (أخطاء HTTP 400) للانتهاكات الشديدة ورفضًا لطيفًا حيث يعتذر الذكاء الاصطناعي بأدب.

> **🤖 جرّب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) واسأل:
> - "ما هو InputGuardrail وكيف أصنع خاصتي؟"
> - "ما الفرق بين الحاجز الصارم والرفض اللين؟"
> - "لماذا نستخدم كلًا من الحواجز والمرشحات المزودة معًا؟"

## الخطوات التالية

**الوحدة التالية:** [01-introduction - البدء مع LangChain4j و gpt-5 على Azure](../01-introduction/README.md)

---

**التنقل:** [← العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 01 - مقدمة →](../01-introduction/README.md)

---

## حل المشكلات

### البناء الأول لـ Maven

**المشكلة**: يستغرق `mvn clean compile` أو `mvn package` الأولي وقتًا طويلاً (10-15 دقيقة)

**السبب**: يحتاج Maven إلى تنزيل كل اعتمادات المشروع (Spring Boot، مكتبات LangChain4j، SDK Azure، إلخ) في البناء الأول.

**الحل**: هذا سلوك طبيعي. ستكون عمليات البناء اللاحقة أسرع بكثير لأن الاعتمادات مخزنة محليًا. يعتمد وقت التنزيل على سرعة شبكة الاتصال لديك.

### صياغة أوامر Maven في PowerShell

**المشكلة**: تفشل أوامر Maven مع خطأ `Unknown lifecycle phase ".mainClass=..."`
**السبب**: يقوم PowerShell بتفسير `=` كعامل إسناد لمتغير، مما يكسر تركيب خصائص Maven

**الحل**: استخدم عامل إيقاف التحليل `--%` قبل أمر Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

العامل `--%` يخبر PowerShell بتمرير كل الوسيطات المتبقية حرفيًا إلى Maven بدون تفسير.

### عرض الرموز التعبيرية في Windows PowerShell

**المشكلة**: تظهر ردود AI حروف غير مفهومة (مثل `????` أو `â??`) بدلاً من الرموز التعبيرية في PowerShell

**السبب**: الترميز الافتراضي لـ PowerShell لا يدعم الرموز التعبيرية بنظام UTF-8

**الحل**: قم بتشغيل هذا الأمر قبل تنفيذ تطبيقات Java:
```cmd
chcp 65001
```

هذا يجبر الترميز على UTF-8 في الطرفية. بدلاً من ذلك، استخدم Windows Terminal الذي يدعم Unicode بشكل أفضل.

### تصحيح مكالمات API

**المشكلة**: أخطاء المصادقة، حدود المعدل، أو ردود غير متوقعة من نموذج AI

**الحل**: تتضمن الأمثلة `.logRequests(true)` و `.logResponses(true)` لعرض مكالمات API في وحدة التحكم. هذا يساعد في استكشاف أخطاء المصادقة، حدود المعدل، أو الردود غير المتوقعة. قم بإزالة هذه العلامات في بيئة الإنتاج لتقليل ضجيج السجلات.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بالترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->