# الوحدة 01: البدء مع LangChain4j

## جدول المحتويات

- [جولة فيديو](../../../01-introduction)
- [ما الذي ستتعلمه](../../../01-introduction)
- [المتطلبات المسبقة](../../../01-introduction)
- [فهم المشكلة الأساسية](../../../01-introduction)
- [فهم الرموز](../../../01-introduction)
- [كيف يعمل الذاكرة](../../../01-introduction)
- [كيف يستخدم هذا LangChain4j](../../../01-introduction)
- [نشر بنية Azure OpenAI التحتية](../../../01-introduction)
- [تشغيل التطبيق محلياً](../../../01-introduction)
- [استخدام التطبيق](../../../01-introduction)
  - [دردشة بلا حالة (اللوحة اليسرى)](../../../01-introduction)
  - [دردشة بحالة (اللوحة اليمنى)](../../../01-introduction)
- [الخطوات التالية](../../../01-introduction)

## جولة فيديو

شاهد هذه الجلسة الحية التي تشرح كيفية البدء بهذه الوحدة: [البدء مع LangChain4j - جلسة مباشرة](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## ما الذي ستتعلمه

إذا أكملت البداية السريعة، فقد رأيت كيفية إرسال المطالبات والحصول على الردود. هذا هو الأساس، لكن التطبيقات الحقيقية تحتاج إلى المزيد. تعلمك هذه الوحدة كيفية بناء ذكاء اصطناعي محادثي يتذكر السياق ويحافظ على الحالة - الفرق بين عرض توضيحي لمرة واحدة وتطبيق جاهز للإنتاج.

سنستخدم GPT-5.2 من Azure OpenAI طوال هذا الدليل لأن قدراته المتقدمة على التفكير تجعل سلوك الأنماط المختلفة أكثر وضوحًا. عند إضافة الذاكرة، سترى الفرق بوضوح. هذا يجعل من الأسهل فهم ما يضيفه كل مكون لتطبيقك.

ستبني تطبيقًا واحدًا يوضح كلا النمطين:

**دردشة بلا حالة** - كل طلب مستقل. النموذج لا يتذكر الرسائل السابقة. هذا هو النمط الذي استخدمته في البداية السريعة.

**محادثة بحالة** - يشمل كل طلب تاريخ المحادثة. يحافظ النموذج على السياق عبر جولات متعددة. هذا ما تتطلبه تطبيقات الإنتاج.

## المتطلبات المسبقة

- اشتراك Azure مع وصول إلى Azure OpenAI
- جافا 21، Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ملاحظة:** جافا و Maven و Azure CLI و Azure Developer CLI (azd) مثبتة مسبقًا في حاوية التطوير المقدمة.

> **ملاحظة:** تستخدم هذه الوحدة GPT-5.2 عبر Azure OpenAI. يتم تكوين النشر تلقائيًا عبر `azd up` - لا تقم بتعديل اسم النموذج في الكود.

## فهم المشكلة الأساسية

نماذج اللغة بلا حالة. كل استدعاء API مستقل. إذا أرسلت "اسمي جون" ثم سألت "ما اسمي؟"، النموذج لا يعرف أنك قدمت نفسك للتو. يعامل كل طلب كما لو أنه أول محادثة تجريها.

هذا جيد لأسئلة وأجوبة بسيطة لكنه عديم الفائدة للتطبيقات الحقيقية. تحتاج روبوتات خدمة العملاء لتذكر ما قلته لهم. يحتاج المساعدون الشخصيون للسياق. أي محادثة متعددة الجولات تتطلب ذاكرة.

<img src="../../../translated_images/ar/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="المحادثات بلا حالة مقابل المحادثات بحالة" width="800"/>

*الفرق بين المحادثات بلا حالة (استدعاءات مستقلة) والمحادثات بحالة (واعية بالسياق)*

## فهم الرموز

قبل الخوض في المحادثات، من المهم فهم الرموز - الوحدات الأساسية للنص التي تعالجها نماذج اللغة:

<img src="../../../translated_images/ar/token-explanation.c39760d8ec650181.webp" alt="شرح الرمز" width="800"/>

*مثال على كيفية تقسيم النص إلى رموز - "I love AI!" تصبح 4 وحدات معالجة منفصلة*

الرموز هي كيف تقيس نماذج الذكاء الاصطناعي النص وتعالجه. الكلمات وعلامات الترقيم وحتى الفراغات يمكن أن تكون رموزًا. لدى نموذجك حد لعدد الرموز التي يمكن معالجتها مرة واحدة (400,000 لـ GPT-5.2، مع ما يصل إلى 272,000 رمز إدخال و128,000 رمز إخراج). فهم الرموز يساعدك في إدارة طول المحادثة والتكاليف.

## كيف يعمل الذاكرة

تحل ذاكرة الدردشة مشكلة بلا حالة من خلال الحفاظ على تاريخ المحادثة. قبل إرسال طلبك إلى النموذج، تضيف الإطار الرسائل السابقة ذات الصلة. عندما تسأل "ما اسمي؟"، يرسل النظام بالفعل كامل تاريخ المحادثة، مما يسمح للنموذج برؤية أنك قلت سابقًا "اسمي جون".

يوفر LangChain4j تطبيقات للذاكرة تدير هذا تلقائيًا. تختار عدد الرسائل التي تحتفظ بها ويدير الإطار نافذة السياق.

<img src="../../../translated_images/ar/memory-window.bbe67f597eadabb3.webp" alt="مفهوم نافذة الذاكرة" width="800"/>

*MessageWindowChatMemory يحافظ على نافذة منزلقة من الرسائل الأخيرة، ويتخلص تلقائيًا من الرسائل القديمة*

## كيف يستخدم هذا LangChain4j

تمدد هذه الوحدة البداية السريعة من خلال دمج Spring Boot وإضافة ذاكرة المحادثة. هكذا تتناسب الأجزاء معًا:

**التبعيات** - أضف مكتبتين LangChain4j:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**نموذج الدردشة** - قم بتكوين Azure OpenAI كـ bean في Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

يقوم المُنشئ بقراءة بيانات الاعتماد من متغيرات البيئة التي يضبطها `azd up`. تجعل تعيين `baseUrl` إلى نقطة نهاية Azure الخاصة بك عميل OpenAI يعمل مع Azure OpenAI.

**ذاكرة المحادثة** - تتبع سجل الدردشة باستخدام MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

أنشئ ذاكرة باستخدام `withMaxMessages(10)` للاحتفاظ بآخر 10 رسائل. أضف رسائل المستخدم والذكاء الاصطناعي باستخدام غلافات من نوع: `UserMessage.from(text)` و `AiMessage.from(text)`. استرجع السجل باستخدام `memory.messages()` وأرسله إلى النموذج. يخزن الخدمة نسخ ذاكرة منفصلة لكل معرف محادثة، مما يسمح لعدة مستخدمين بالدردشة في نفس الوقت.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) واطرح:
> - "كيف يقرر MessageWindowChatMemory أي الرسائل يتم حذفها عندما تكون النافذة ممتلئة؟"
> - "هل يمكنني تنفيذ تخزين ذاكرة مخصص باستخدام قاعدة بيانات بدلاً من التخزين في الذاكرة؟"
> - "كيف يمكنني إضافة التلخيص لضغط تاريخ المحادثة القديم؟"

يتخطى نقطة نهاية الدردشة بلا حالة الذاكرة تمامًا - فقط `chatModel.chat(prompt)` كما في البداية السريعة. تضيف نقطة نهاية الدردشة بحالة الرسائل للذاكرة، تسترجع السجل، وتضمّن هذا السياق مع كل طلب. نفس تكوين النموذج، أنماط مختلفة.

## نشر بنية Azure OpenAI التحتية

**Bash:**
```bash
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يفضل eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يوصى بـ eastus2)
```

> **ملاحظة:** إذا واجهت خطأ انتهاء مهلة (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)، فقط شغّل `azd up` مرة أخرى. قد تكون موارد Azure لا تزال قيد الإعداد في الخلفية، وإعادة المحاولة تسمح لاكتمال النشر بمجرد وصول الموارد إلى حالة نهائية.

هذا سيفعل:
1. نشر مورد Azure OpenAI مع نماذج GPT-5.2 و text-embedding-3-small
2. إنشاء ملف `.env` تلقائيًا في جذر المشروع مع بيانات الاعتماد
3. إعداد جميع متغيرات البيئة المطلوبة

**هل تواجه مشكلات في النشر؟** انظر إلى [ملف README للبنية التحتية](infra/README.md) للحصول على خطوات تفصيلية لحل المشكلات بما في ذلك تعارضات أسماء النطاق الفرعي، خطوات نشر Azure Portal اليدوية، وإرشادات تكوين النموذج.

**تحقق من نجاح النشر:**

**Bash:**
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

> **ملاحظة:** ينشئ أمر `azd up` ملف `.env` تلقائيًا. إذا كنت بحاجة لتحديثه لاحقًا، يمكنك إما تعديل ملف `.env` يدويًا أو إعادة إنشائه بتشغيل:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## تشغيل التطبيق محلياً

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذري مع بيانات اعتماد Azure:

**Bash:**
```bash
cat ../.env  # يجب عرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT ومفتاح API ونشر DEPLOYMENT
```

**ابدأ التطبيقات:**

**الخيار 1: استخدام لوحة Spring Boot (مستحسن لمستخدمي VS Code)**

تتضمن حاوية التطوير إضافة لوحة Spring Boot، التي توفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط اضغط زر التشغيل بجانب "introduction" لبدء هذه الوحدة، أو شغّل كل الوحدات دفعة واحدة.

<img src="../../../translated_images/ar/dashboard.69c7479aef09ff6b.webp" alt="لوحة Spring Boot" width="400"/>

**الخيار 2: استخدام سكربتات shell**

ابدأ جميع تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من دليل الجذر
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من الدليل الجذر
.\start-all.ps1
```

أو ابدأ هذه الوحدة فقط:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

كلا السكربتات يحملان متغيرات البيئة تلقائيًا من ملف `.env` بالجذر وسيبنيان ملفات JAR إذا لم تكن موجودة.

> **ملاحظة:** إذا فضلت بناء كل الوحدات يدويًا قبل البدء:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

افتح http://localhost:8080 في متصفحك.

**للتوقف:**

**Bash:**
```bash
./stop.sh  # هذا الوحدة فقط
# أو
cd .. && ./stop-all.sh  # جميع الوحدات
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الموديول فقط
# أو
cd ..; .\stop-all.ps1  # جميع الموديولات
```

## استخدام التطبيق

يوفر التطبيق واجهة ويب مع تنفيذين للدردشة جنبًا إلى جنب.

<img src="../../../translated_images/ar/home-screen.121a03206ab910c0.webp" alt="شاشة البداية للتطبيق" width="800"/>

*لوحة تعرض خيارات الدردشة البسيطة (بلا حالة) والدردشة المحادثية (بحالة)*

### الدردشة بلا حالة (اللوحة اليسرى)

جرب هذا أولاً. اسأل "اسمي جون" ثم اسأل فوراً "ما اسمي؟" لن يتذكر النموذج لأن كل رسالة مستقلة. هذا يوضح المشكلة الأساسية لدمج نموذج اللغة الأساسي - لا يوجد سياق محادثة.

<img src="../../../translated_images/ar/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="عرض ديمو الدردشة بلا حالة" width="800"/>

*الذكاء الاصطناعي لا يتذكر اسمك من الرسالة السابقة*

### الدردشة بحالة (اللوحة اليمنى)

الآن جرب نفس التسلسل هنا. اسأل "اسمي جون" ثم "ما اسمي؟" هذه المرة يتذكر. الفرق هو MessageWindowChatMemory - يحافظ على سجل المحادثة ويتضمنه مع كل طلب. هكذا يعمل الذكاء الاصطناعي المحادثي في الإنتاج.

<img src="../../../translated_images/ar/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="عرض ديمو الدردشة بحالة" width="800"/>

*الذكاء الاصطناعي يتذكر اسمك من وقت سابق في المحادثة*

تستخدم اللوحتان نفس نموذج GPT-5.2. الفرق الوحيد هو الذاكرة. هذا يوضح بجلاء ما تضيفه الذاكرة لتطبيقك ولماذا هي ضرورية للحالات الواقعية.

## الخطوات التالية

**الوحدة التالية:** [02-prompt-engineering - هندسة المطالبات مع GPT-5.2](../02-prompt-engineering/README.md)

---

**التنقل:** [← السابقة: الوحدة 00 - البداية السريعة](../00-quick-start/README.md) | [العودة إلى الرئيسية](../README.md) | [التالي: الوحدة 02 - هندسة المطالبات →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يُرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية هو المصدر المعتمد. للمعلومات الهامة، يُنصح بالاعتماد على الترجمة المهنية البشرية. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->