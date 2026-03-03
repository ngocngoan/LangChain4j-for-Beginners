# الوحدة 00: البداية السريعة

## جدول المحتويات

- [مقدمة](../../../00-quick-start)
- [ما هو LangChain4j؟](../../../00-quick-start)
- [اعتمادات LangChain4j](../../../00-quick-start)
- [المتطلبات المسبقة](../../../00-quick-start)
- [الإعداد](../../../00-quick-start)
  - [1. احصل على رمز GitHub الخاص بك](../../../00-quick-start)
  - [2. ضبط الرمز الخاص بك](../../../00-quick-start)
- [تشغيل الأمثلة](../../../00-quick-start)
  - [1. الدردشة الأساسية](../../../00-quick-start)
  - [2. أنماط الإدخال](../../../00-quick-start)
  - [3. استدعاء الدوال](../../../00-quick-start)
  - [4. أسئلة وأجوبة الوثائق (RAG السهل)](../../../00-quick-start)
  - [5. الذكاء الاصطناعي المسؤول](../../../00-quick-start)
- [ما الذي يُظهره كل مثال](../../../00-quick-start)
- [الخطوات التالية](../../../00-quick-start)
- [استكشاف الأخطاء وإصلاحها](../../../00-quick-start)

## مقدمة

تهدف هذه البداية السريعة إلى تمكينك من بدء استخدام LangChain4j بأسرع وقت ممكن. تغطي الأساسيات المطلقة لبناء تطبيقات الذكاء الاصطناعي باستخدام LangChain4j ونماذج GitHub. في الوحدات التالية، ستنتقل إلى Azure OpenAI و GPT-5.2 وتغوص أعمق في كل مفهوم.

## ما هو LangChain4j؟

LangChain4j هو مكتبة Java تبسط بناء تطبيقات مدعومة بالذكاء الاصطناعي. بدلاً من التعامل مع عملاء HTTP وتحليل JSON، تعمل بواجهات برمجة تطبيقات Java نظيفة.

تشير كلمة "chain" في LangChain إلى ربط مكونات متعددة معًا - قد تربط مطالبة مع نموذج ثم محلل، أو تربط عدة استدعاءات للذكاء الاصطناعي معًا حيث تغذي ناتج واحد الإدخال التالي. تركز هذه البداية السريعة على الأساسيات قبل استكشاف سلاسل أكثر تعقيدًا.

<img src="../../../translated_images/ar/langchain-concept.ad1fe6cf063515e1.webp" alt="مفهوم ربط LangChain4j" width="800"/>

*ربط المكونات في LangChain4j - الكتل البنائية تتصل لإنشاء تدفقات عمل ذكية قوية*

سنستخدم ثلاثة مكونات أساسية:

**ChatModel** - واجهة للتفاعل مع نماذج الذكاء الاصطناعي. استدعِ `model.chat("prompt")` واحصل على سلسلة رد. نستخدم `OpenAiOfficialChatModel` الذي يعمل مع نقاط نهاية متوافقة مع OpenAI مثل نماذج GitHub.

**AiServices** - ينشئ واجهات خدمات ذكاء اصطناعي آمنة من حيث النوع. عرّف طرقًا، علّمها بـ `@Tool`، ويتولى LangChain4j التنفيذ. يستدعي الذكاء الاصطناعي طرق Java الخاصة بك تلقائيًا عند الحاجة.

**MessageWindowChatMemory** - يحتفظ بتاريخ المحادثة. بدون هذا، كل طلب مستقل. به، يتذكّر الذكاء الاصطناعي الرسائل السابقة ويحافظ على السياق عبر دورات متعددة.

<img src="../../../translated_images/ar/architecture.eedc993a1c576839.webp" alt="هندسة LangChain4j" width="800"/>

*هندسة LangChain4j - المكونات الأساسية تعمل معًا لتشغيل تطبيقات الذكاء الاصطناعي الخاصة بك*

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

يوفر الموديول `langchain4j-open-ai-official` فئة `OpenAiOfficialChatModel` التي تتصل بواجهات API المتوافقة مع OpenAI. تستخدم نماذج GitHub نفس تنسيق API، لذا لا حاجة إلى محول خاص - فقط وجه عنوان القاعدة إلى `https://models.github.ai/inference`.

يوفر الموديول `langchain4j-easy-rag` تقسيم الوثائق التلقائي، والتضمين، والاسترجاع حتى تتمكن من بناء تطبيقات RAG دون تكوين يدوي لكل خطوة.

## المتطلبات المسبقة

**هل تستخدم Dev Container؟** Java و Maven مثبتان بالفعل. أنت فقط بحاجة إلى رمز الوصول الشخصي من GitHub.

**التطوير المحلي:**
- Java 21+، Maven 3.9+
- رمز وصول شخصي من GitHub (التعليمات أدناه)

> **ملاحظة:** تستخدم هذه الوحدة `gpt-4.1-nano` من نماذج GitHub. لا تعدل اسم النموذج في الكود - هو مهيأ للعمل مع نماذج GitHub المتاحة.

## الإعداد

### 1. احصل على رمز GitHub الخاص بك

1. انتقل إلى [إعدادات GitHub → رموز الوصول الشخصية](https://github.com/settings/personal-access-tokens)
2. اضغط على "إنشاء رمز جديد"
3. ضع اسمًا وصفيًا (مثلاً: "LangChain4j Demo")
4. حدد تاريخ انتهاء صلاحية (7 أيام موصى بها)
5. تحت "أذونات الحساب"، ابحث عن "Models" واضبطها على "قراءة فقط"
6. اضغط على "إنشاء رمز"
7. انسخ وصِل رمزك - لن تراه مرة أخرى

### 2. ضبط الرمز الخاص بك

**الخيار 1: استخدام VS Code (موصى به)**

إذا كنت تستخدم VS Code، أضف الرمز الخاص بك إلى ملف `.env` في جذر المشروع:

إذا لم يكن ملف `.env` موجودًا، انسخ `.env.example` إلى `.env` أو أنشئ ملف `.env` جديد في جذر المشروع.

**مثال على ملف `.env`:**
```bash
# في /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

ثم يمكنك ببساطة النقر بزر الفأرة الأيمن على أي ملف تجريبي (مثل `BasicChatDemo.java`) في المستكشف واختيار **"Run Java"** أو استخدام تكوينات التشغيل من لوحة Run و Debug.

**الخيار 2: استخدام الطرفية**

اضبط الرمز كمتغير بيئة:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## تشغيل الأمثلة

**باستخدام VS Code:** فقط انقر بزر الفأرة الأيمن على أي ملف تجريبي في المستكشف واختر **"Run Java"**، أو استخدم تكوينات التشغيل من لوحة Run و Debug (تأكد من إضافة الرمز إلى ملف `.env` أولاً).

**باستخدام Maven:** بدلاً من ذلك، يمكنك التشغيل من سطر الأوامر:

### 1. الدردشة الأساسية

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. أنماط الإدخال

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

يعرض أنماط بدون أمثلة، مع أمثلة قليلة، السلسلة الفكرية، والإدخال القائم على الأدوار.

### 3. استدعاء الدوال

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

الذكاء الاصطناعي يستدعي طرق Java الخاصة بك تلقائيًا عند الحاجة.

### 4. أسئلة وأجوبة الوثائق (RAG السهل)

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

شاهد كيف تمنع فلاتر سلامة الذكاء الاصطناعي المحتوى الضار.

## ما الذي يُظهره كل مثال

**الدردشة الأساسية** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ابدأ هنا لرؤية LangChain4j في أبسط صوره. ستنشئ `OpenAiOfficialChatModel`، ترسل مطالبة باستخدام `.chat()`، وتحصل على رد. هذا يوضح الأساس: كيف تهيئ النماذج بنقاط نهاية ومفاتيح API مخصصة. بمجرد فهمك لهذا النمط، يبنى كل شيء آخر عليه.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 جرّب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) واطرح:
> - "كيف يمكنني التبديل من نماذج GitHub إلى Azure OpenAI في هذا الكود؟"
> - "ما هي المعلمات الأخرى التي يمكنني تكوينها في OpenAiOfficialChatModel.builder()؟"
> - "كيف أضيف ردود تدفق بدلاً من انتظار الرد الكامل؟"

**هندسة المطالبات** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

الآن بعد أن عرفت كيف تتحدث إلى نموذج، دعنا نستكشف ما تقول له. تستخدم هذه التجربة نفس إعداد النموذج لكنها تعرض خمسة أنماط مختلفة من المطالبات. جرّب مطالبات بدون أمثلة مباشرة، مطالبات مع أمثلة قليلة تتعلم منها، مطالبات السلسلة الفكرية التي تكشف خطوات التفكير، والمطالبات القائمة على الأدوار التي تحدد السياق. سترى كيف يعطي نفس النموذج نتائج مختلفة تمامًا بناءً على كيفية صياغة طلبك.

تُظهر التجربة أيضًا قوالب المطالبات، وهي طريقة قوية لإنشاء مطالبات قابلة لإعادة الاستخدام مع متغيرات.
يُظهر المثال أدناه مطالبة تستخدم قالب LangChain4j `PromptTemplate` لملء المتغيرات. سيجيب الذكاء الاصطناعي بناءً على الوجهة والنشاط المقدمين.

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

> **🤖 جرّب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) واطرح:
> - "ما الفرق بين مطالبة بدون أمثلة ومطالبة مع أمثلة قليلة، ومتى أستخدم كل منهما؟"
> - "كيف يؤثر متغير درجة الحرارة على استجابات النموذج؟"
> - "ما هي بعض التقنيات لمنع هجمات حقن المطالبات في الإنتاج؟"
> - "كيف يمكنني إنشاء كائنات PromptTemplate قابلة لإعادة الاستخدام للأنماط الشائعة؟"

**تكامل الأدوات** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

هنا يصبح LangChain4j قويًا. ستستخدم `AiServices` لإنشاء مساعد ذكاء اصطناعي يمكنه استدعاء طرق Java الخاصة بك. فقط عين الطرق بـ `@Tool("الوصف")` ويتولى LangChain4j الباقي - يقرر الذكاء الاصطناعي تلقائيًا متى يستخدم كل أداة بناءً على طلب المستخدم. توضح هذه التجربة استدعاء الدوال، وهي تقنية رئيسية لبناء ذكاء اصطناعي يمكنه اتخاذ إجراءات، وليس مجرد الإجابة عن الأسئلة.

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

> **🤖 جرّب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) واطرح:
> - "كيف تعمل التعليمة @Tool وماذا يفعل LangChain4j معها خلف الكواليس؟"
> - "هل يمكن للذكاء الاصطناعي استدعاء أدوات متعددة على التوالي لحل مشكلات معقدة؟"
> - "ماذا يحدث إذا رمت أداة استثناء - كيف أتعامل مع الأخطاء؟"
> - "كيف أدمج واجهة برمجة تطبيقات حقيقية بدلاً من مثال الآلة الحاسبة هذا؟"

**أسئلة وأجوبة الوثائق (RAG السهل)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

هنا ستشاهد RAG (التوليد المعزز بالاسترجاع) باستخدام نهج LangChain4j "Easy RAG". تُحمّل الوثائق، تُقسّم تلقائيًا وتُدمج في ذاكرة داخلية، ثم يزود مسترجع المحتوى الذكاء الاصطناعي بالقطع ذات الصلة وقت الاستعلام. يجيب الذكاء الاصطناعي بناءً على مستنداتك، وليس معرفته العامة.

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

> **🤖 جرّب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) واطرح:
> - "كيف يمنع RAG الهلوسة في الذكاء الاصطناعي مقارنة باستخدام بيانات تدريب النموذج؟"
> - "ما الفرق بين هذا النهج السهل وخط أنابيب RAG مخصص؟"
> - "كيف أقوم بتوسيعه ليتعامل مع مستندات متعددة أو قواعد معرفة أكبر؟"

**الذكاء الاصطناعي المسؤول** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ابنِ أمان الذكاء الاصطناعي مع دفاع متعدد الطبقات. تعرض هذه التجربة طبقتين من الحماية تعملان معًا:

**الجزء 1: قواعد حماية الإدخال في LangChain4j** - تمنع المطالبات الخطرة قبل وصولها إلى LLM. أنشئ قواعد حماية مخصصة تتحقق من كلمات أو أنماط محظورة. تعمل هذه في كودك، لذا فهي سريعة ومجانية.

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

**الجزء 2: فلاتر السلامة لدى المزود** - لدى نماذج GitHub مرشحات مدمجة تلتقط ما قد تفوته قواعد الحماية لديك. سترى حجبًا صارمًا (أخطاء HTTP 400) للانتهاكات الجسيمة والرفض اللطيف حيث يرفض الذكاء الاصطناعي بأدب.

> **🤖 جرّب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) واطرح:
> - "ما هو InputGuardrail وكيف أصنع واحدًا بنفسي؟"
> - "ما الفرق بين الحجب الصارم والرفض اللطيف؟"
> - "لماذا نستخدم كلًا من قواعد الحماية وفلاتر المزود معًا؟"

## الخطوات التالية

**الوحدة التالية:** [01-introduction - البدء مع LangChain4j](../01-introduction/README.md)

---

**التنقل:** [← العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 01 - مقدمة →](../01-introduction/README.md)

---

## استكشاف الأخطاء وإصلاحها

### بناء Maven لأول مرة

**المشكلة**: يأخذ الأمر `mvn clean compile` أو `mvn package` الأول وقتًا طويلاً (10-15 دقيقة)

**السبب**: يحتاج Maven إلى تنزيل جميع اعتمادات المشروع (Spring Boot، مكتبات LangChain4j، مجموعات Azure SDK، إلخ) في البناء الأول.

**الحل**: هذا سلوك طبيعي. ستكون عمليات البناء اللاحقة أسرع بكثير لأن الاعتمادات مخزنة محليًا. يعتمد وقت التنزيل على سرعة الشبكة الخاصة بك.

### صياغة أوامر Maven في PowerShell

**المشكلة**: تفشل أوامر Maven بخطأ `Unknown lifecycle phase ".mainClass=..."`
**السبب**: يقوم PowerShell بتفسير `=` كعامل تخصيص متغير، مما يكسر بناء جملة خصائص Maven

**الحل**: استخدم عامل إيقاف التحليل `--%` قبل أمر Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

عامل `--%` يخبر PowerShell بتمرير جميع الوسائط المتبقية حرفيًا إلى Maven دون تفسير.

### عرض الرموز التعبيرية في Windows PowerShell

**المشكلة**: تظهر استجابات الذكاء الاصطناعي حروفًا تالفة (مثل `????` أو `â??`) بدلاً من الرموز التعبيرية في PowerShell

**السبب**: الترميز الافتراضي لـ PowerShell لا يدعم الرموز التعبيرية بنمط UTF-8

**الحل**: شغّل هذا الأمر قبل تنفيذ تطبيقات Java:
```cmd
chcp 65001
```

هذا يجبر الترميز UTF-8 في الطرفية. بدلاً من ذلك، استخدم Windows Terminal الذي يدعم Unicode بشكل أفضل.

### تصحيح أخطاء استدعاءات API

**المشكلة**: أخطاء مصادقة، حدود معدلات، أو استجابات غير متوقعة من نموذج الذكاء الاصطناعي

**الحل**: تتضمن الأمثلة `.logRequests(true)` و `.logResponses(true)` لعرض استدعاءات API في الكونسول. هذا يساعد في معالجة أخطاء المصادقة، حدود المعدلات، أو الاستجابات غير المتوقعة. قم بإزالة هذه العلامات في بيئة الإنتاج لتقليل ضوضاء السجل.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). على الرغم من سعينا لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. ينبغي اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق به. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسيرات خاطئة ناتجة عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->