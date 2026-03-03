# الوحدة 01: بدء العمل مع LangChain4j

## جدول المحتويات

- [عرض الفيديو](../../../01-introduction)
- [ما ستتعلمه](../../../01-introduction)
- [المتطلبات الأساسية](../../../01-introduction)
- [فهم المشكلة الأساسية](../../../01-introduction)
- [فهم الرموز](../../../01-introduction)
- [كيفية عمل الذاكرة](../../../01-introduction)
- [كيفية استخدام هذا مع LangChain4j](../../../01-introduction)
- [نشر بنية تحتية Azure OpenAI](../../../01-introduction)
- [تشغيل التطبيق محليًا](../../../01-introduction)
- [استخدام التطبيق](../../../01-introduction)
  - [الدردشة بدون حالة (اللوحة اليسرى)](../../../01-introduction)
  - [الدردشة مع الحالة (اللوحة اليمنى)](../../../01-introduction)
- [الخطوات التالية](../../../01-introduction)

## عرض الفيديو

شاهد هذه الجلسة المباشرة التي تشرح كيفية البدء مع هذه الوحدة:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="بدء العمل مع LangChain4j - جلسة مباشرة" width="800"/></a>

## ما ستتعلمه

في البداية السريعة، استخدمت نماذج GitHub لإرسال المطالبات، واستدعاء الأدوات، وبناء خط أنابيب RAG، واختبار الضوابط الأمنية. أظهرت تلك العروض ما هو ممكن — الآن ننتقل إلى Azure OpenAI وGPT-5.2 ونبدأ في بناء تطبيقات أسلوب الإنتاج. تركز هذه الوحدة على الذكاء الاصطناعي الحواري الذي يتذكر السياق ويحافظ على الحالة — المفاهيم التي استخدمتها نماذج البداية السريعة خلف الكواليس ولكنها لم تشرحها.

سنستخدم GPT-5.2 من Azure OpenAI طوال هذا الدليل لأن قدراته المتقدمة على الاستدلال تجعل سلوك الأنماط المختلفة أكثر وضوحًا. عندما تضيف الذاكرة، سترى الفرق بوضوح. هذا يجعل من الأسهل فهم ما يقدمه كل مكون لتطبيقك.

ستبني تطبيقًا واحدًا يعرض كلا النمطين:

**الدردشة بدون حالة** - كل طلب مستقل. النموذج لا يملك ذاكرة عن الرسائل السابقة. هذا هو النمط الذي استخدمته في البداية السريعة.

**المحادثة مع الحالة** - كل طلب يتضمن تاريخ المحادثة. النموذج يحتفظ بالسياق عبر عدة جولات. هذا ما تتطلبه تطبيقات الإنتاج.

## المتطلبات الأساسية

- اشتراك Azure مع وصول إلى Azure OpenAI
- Java 21، Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ملاحظة:** Java وMaven وAzure CLI وAzure Developer CLI (azd) مثبتة مسبقًا في الحاوية التطويرية المقدمة.

> **ملاحظة:** تستخدم هذه الوحدة GPT-5.2 على Azure OpenAI. التهيئة تتم تلقائيًا عبر `azd up` - لا تقم بتعديل اسم النموذج في الكود.

## فهم المشكلة الأساسية

نماذج اللغة عديمة الحالة. كل استدعاء API مستقل. إذا أرسلت "اسمي جون" ثم سألت "ما اسمي؟"، النموذج لا يعلم أنك قدمت نفسك للتو. يعامل كل طلب كما لو أنها أول محادثة تجريها.

هذا جيد لأسئلة وأجوبة بسيطة لكنه غير مفيد للتطبيقات الحقيقية. تحتاج روبوتات خدمة العملاء إلى تذكر ما قلته لهم. المساعدون الشخصيون يحتاجون إلى السياق. أي محادثة متعددة الجولات تتطلب ذاكرة.

يوضح المخطط التالي الفرق بين النهجين — على اليسار، استدعاء بدون حالة ينسى اسمك؛ وعلى اليمين، استدعاء مع الحالة مدعوم بـ ChatMemory التي تتذكره.

<img src="../../../translated_images/ar/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="المحادثات بدون حالة مقابل المحادثات مع حالة" width="800"/>

*الفرق بين المحادثات بدون حالة (استدعاءات مستقلة) والمحادثات مع حالة (مدركة للسياق)*

## فهم الرموز

قبل الخوض في المحادثات، من المهم فهم الرموز - الوحدات الأساسية للنص التي تعالجها نماذج اللغة:

<img src="../../../translated_images/ar/token-explanation.c39760d8ec650181.webp" alt="شرح الرموز" width="800"/>

*مثال على كيفية تقسيم النص إلى رموز - "أحب الذكاء الاصطناعي!" تصبح 4 وحدات معالجة منفصلة*

الرموز هي كيفية قياس نماذج الذكاء الاصطناعي ومعالجتها للنص. الكلمات، علامات الترقيم، وحتى الفراغات يمكن أن تكون رموزًا. لدى نموذجك حد لعدد الرموز التي يمكنه معالجتها مرة واحدة (400,000 لـ GPT-5.2، مع ما يصل إلى 272,000 رمز إدخال و128,000 رمز إخراج). يساعد فهم الرموز في إدارة طول المحادثة والتكاليف.

## كيفية عمل الذاكرة

تحل ذاكرة الدردشة مشكلة عديم الحالة من خلال الاحتفاظ بتاريخ المحادثة. قبل إرسال طلبك للنموذج، يقوم الإطار بإضافة الرسائل السابقة ذات الصلة في البداية. عندما تسأل "ما اسمي؟"، يرسل النظام فعليًا كامل تاريخ المحادثة، مما يسمح للنموذج برؤية أنك قلت سابقًا "اسمي جون."

يوفر LangChain4j تنفيذات ذاكرة تتعامل مع هذا تلقائيًا. تختار عدد الرسائل التي تحتفظ بها ويدير الإطار نافذة السياق. يوضح المخطط أدناه كيف يحافظ MessageWindowChatMemory على نافذة انزلاقية للرسائل الحديثة.

<img src="../../../translated_images/ar/memory-window.bbe67f597eadabb3.webp" alt="مفهوم نافذة الذاكرة" width="800"/>

*يحافظ MessageWindowChatMemory على نافذة منزلقة من الرسائل الحديثة، ويتخلص تلقائيًا من الرسائل القديمة*

## كيفية استخدام هذا مع LangChain4j

توسّع هذه الوحدة البداية السريعة من خلال دمج Spring Boot وإضافة ذاكرة المحادثة. إليك كيف تتجمع القطع معًا:

**المكتبات:** أضف مكتبتين لـ LangChain4j:

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

يقوم المنشئ بقراءة بيانات الاعتماد من متغيرات البيئة التي يضبطها `azd up`. تعيين `baseUrl` إلى نقطة نهاية Azure يجعله يعمل مع Azure OpenAI.

**ذاكرة المحادثة** - تتبع سجل الدردشة باستخدام MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

قم بإنشاء ذاكرة مع `withMaxMessages(10)` للاحتفاظ بآخر 10 رسائل. أضف رسائل المستخدم والذكاء الاصطناعي باستخدام التغليف النوعي: `UserMessage.from(text)` و `AiMessage.from(text)`. استرجع التاريخ بـ `memory.messages()` وأرسله للنموذج. يخزن الخادم حالات ذاكرة منفصلة لكل معرف محادثة، مما يسمح لعدة مستخدمين بالدردشة في نفس الوقت.

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) واسأل:
> - "كيف يقرر MessageWindowChatMemory أي الرسائل يتم إسقاطها عندما تمتلئ النافذة؟"
> - "هل يمكنني تنفيذ تخزين ذاكرة مخصص باستخدام قاعدة بيانات بدلاً من الذاكرة الداخلية؟"
> - "كيف يمكنني إضافة التلخيص لضغط تاريخ المحادثة القديم؟"

تتخطى نقطة النهاية للدردشة بدون حالة الذاكرة تمامًا - فقط `chatModel.chat(prompt)` مثل البداية السريعة. تضيف نقطة النهاية ذات الحالة الرسائل إلى الذاكرة، تسترجع التاريخ، وتشمل ذلك السياق مع كل طلب. نفس تهيئة النموذج، أنماط مختلفة.

## نشر بنية تحتية Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يفضل eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يفضل eastus2)
```

> **ملاحظة:** إذا واجهت خطأ مهلة (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)، ما عليك سوى تشغيل `azd up` مرة أخرى. قد تكون موارد Azure لا تزال تُنشأ في الخلفية، والمحاولة مجددًا تسمح بإكمال النشر بمجرد وصول الموارد إلى حالة نهائية.

سيقوم هذا بـ:
1. نشر مورد Azure OpenAI مع نماذج GPT-5.2 وtext-embedding-3-small
2. إنشاء ملف `.env` تلقائيًا في جذر المشروع مع بيانات الاعتماد
3. إعداد جميع متغيرات البيئة المطلوبة

**هل تواجه مشاكل في النشر؟** انظر إلى [ملف README للبنية التحتية](infra/README.md) للحصول على حل مشاكل تفصيلي بما في ذلك تعارض أسماء النطاقات الفرعية، خطوات النشر اليدوي من بوابة Azure، وإرشادات تكوين النموذج.

**تحقق من نجاح النشر:**

**Bash:**
```bash
cat ../.env  # يجب أن يُظهر AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

> **ملاحظة:** يقوم الأمر `azd up` تلقائيًا بإنشاء ملف `.env`. إذا احتجت إلى تحديثه لاحقًا، يمكنك تحرير ملف `.env` يدويًا أو إعادة إنشائه بتشغيل:
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

## تشغيل التطبيق محليًا

**تحقق من النشر:**

تأكد من وجود ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure. شغّل هذا من دليل الوحدة (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيقات:**

**الخيار 1: استخدام لوحة تحكم Spring Boot (موصى لمستخدمي VS Code)**

تشمل الحاوية التطويرية امتداد لوحة تحكم Spring Boot، الذي يوفر واجهة بصرية لإدارة جميع تطبيقات Spring Boot. يمكنك العثور عليه في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في بيئة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيق في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط انقر على زر التشغيل بجانب "introduction" لبدء هذه الوحدة، أو ابدأ كل الوحدات مرة واحدة.

<img src="../../../translated_images/ar/dashboard.69c7479aef09ff6b.webp" alt="لوحة تحكم Spring Boot" width="400"/>

*لوحة تحكم Spring Boot في VS Code — ابدأ، أوقف، وراقب جميع الوحدات من مكان واحد*

**الخيار 2: استخدام سكريبتات الشل**

ابدأ كل تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذري
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من الدليل الجذر
.\start-all.ps1
```

أو ابدأ فقط هذه الوحدة:

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

كلا السكريبتين يقومان بتحميل متغيرات البيئة تلقائيًا من ملف .env في الجذر وسيبنيان ملفات JAR إذا لم تكن موجودة.

> **ملاحظة:** إذا فضلت بناء جميع الوحدات يدويًا قبل البدء:
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

**لإيقاف التشغيل:**

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

يوفر التطبيق واجهة ويب تحتوي على تنفيذين للدردشة جنبًا إلى جنب.

<img src="../../../translated_images/ar/home-screen.121a03206ab910c0.webp" alt="شاشة رئيسية للتطبيق" width="800"/>

*لوحة تحكم تعرض كل من الدردشة البسيطة (بدون حالة) والدردشة الحواريّة (مع حالة)*

### الدردشة بدون حالة (اللوحة اليسرى)

جرّب هذا أولاً. اسأل "اسمي جون" ثم اسأل فورًا "ما اسمي؟" لن يتذكر النموذج لأن كل رسالة مستقلة. يوضح هذا المشكلة الأساسية مع دمج نماذج اللغة البسيطة — لا يوجد سياق للمحادثة.

<img src="../../../translated_images/ar/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="عرض توضيحي للدردشة بدون حالة" width="800"/>

*الذكاء الاصطناعي لا يتذكر اسمك من الرسالة السابقة*

### الدردشة مع الحالة (اللوحة اليمنى)

الآن جرّب نفس التسلسل هنا. اسأل "اسمي جون" ثم "ما اسمي؟" هذه المرة يتذكر. الفرق هو MessageWindowChatMemory - فهي تحافظ على تاريخ المحادثة وتضمّنه مع كل طلب. هكذا يعمل الذكاء الاصطناعي الحواري في الإنتاج.

<img src="../../../translated_images/ar/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="عرض توضيحي للدردشة مع حالة" width="800"/>

*الذكاء الاصطناعي يتذكر اسمك من سابق المحادثة*

تستخدم اللوحتان نفس نموذج GPT-5.2. الاختلاف الوحيد هو الذاكرة. هذا يوضح ما تضيفه الذاكرة إلى تطبيقك ولماذا هي أساسية لحالات الاستخدام الحقيقية.

## الخطوات التالية

**الوحدة التالية:** [02-prompt-engineering - هندسة المطالبات مع GPT-5.2](../02-prompt-engineering/README.md)

---

**التنقل:** [← السابق: الوحدة 00 - البداية السريعة](../00-quick-start/README.md) | [عودة إلى الرئيسي](../README.md) | [التالي: الوحدة 02 - هندسة المطالبات →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). على الرغم من سعينا لتحقيق الدقة، يُرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. بالنسبة للمعلومات الهامة، يُنصح بالاعتماد على ترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->