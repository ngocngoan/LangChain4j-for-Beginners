# الوحدة 01: البدء مع LangChain4j

## جدول المحتويات

- [شاهد الفيديو التوضيحي](../../../01-introduction)
- [ما الذي ستتعلمه](../../../01-introduction)
- [المتطلبات المسبقة](../../../01-introduction)
- [فهم المشكلة الأساسية](../../../01-introduction)
- [فهم الرموز](../../../01-introduction)
- [كيف يعمل الذاكرة](../../../01-introduction)
- [كيف يستخدم هذا LangChain4j](../../../01-introduction)
- [نشر بنية Azure OpenAI التحتية](../../../01-introduction)
- [تشغيل التطبيق محليًا](../../../01-introduction)
- [استخدام التطبيق](../../../01-introduction)
  - [دردشة بدون حالة (لوحة اليسار)](../../../01-introduction)
  - [دردشة بحالة (لوحة اليمين)](../../../01-introduction)
- [الخطوات التالية](../../../01-introduction)

## شاهد الفيديو التوضيحي

شاهد هذا الجلسة الحية التي تشرح كيفية البدء مع هذه الوحدة:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="البدء مع LangChain4j - جلسة مباشرة" width="800"/></a>

## ما الذي ستتعلمه

إذا أكملت البداية السريعة، فقد رأيت كيف ترسل المطالبات وتحصل على ردود. هذه هي الأساس، لكن التطبيقات الحقيقية تحتاج أكثر. هذه الوحدة تعلمك كيفية بناء ذكاء اصطناعي محادثي يتذكر السياق ويحافظ على الحالة - الفرق بين عرض توضيحي لمرة واحدة وتطبيق جاهز للإنتاج.

سنستخدم GPT-5.2 من Azure OpenAI طوال هذا الدليل لأن قدراته المتقدمة على الاستدلال تجعل سلوك الأنماط المختلفة أكثر وضوحًا. عند إضافة الذاكرة، سترى الفرق بوضوح. هذا يجعل من الأسهل فهم ما يقدمه كل مكون لتطبيقك.

ستبني تطبيقًا واحدًا يوضح كلا النمطين:

**دردشة بدون حالة** - كل طلب مستقل. لا يتذكر النموذج الرسائل السابقة. هذا هو النمط الذي استخدمته في البداية السريعة.

**محادثة بحالة** - كل طلب يشمل تاريخ المحادثة. يحتفظ النموذج بالسياق عبر عدة جولات. هذا ما تتطلبه تطبيقات الإنتاج.

## المتطلبات المسبقة

- اشتراك Azure مع وصول Azure OpenAI
- جافا 21، مافن 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ملاحظة:** جافا، مافن، Azure CLI و Azure Developer CLI (azd) مثبتة مسبقًا في devcontainer المقدم.

> **ملاحظة:** تستخدم هذه الوحدة GPT-5.2 على Azure OpenAI. يتم تكوين النشر تلقائيًا عبر `azd up` - لا تقم بتعديل اسم النموذج في الكود.

## فهم المشكلة الأساسية

نماذج اللغات بدون حالة. كل طلب API مستقل. إذا أرسلت "اسمي جون" ثم سألت "ما اسمي؟"، النموذج لا يعلم أنك قدمت نفسك للتو. يعامل كل طلب كما لو كانت هذه أول محادثة لك.

هذا جيد لأسئلة وأجوبة بسيطة لكنه غير مفيد للتطبيقات الحقيقية. Bots خدمة العملاء بحاجة لتذكر ما قلته لهم. المساعدون الشخصيون يحتاجون للسياق. أي محادثة متعددة الجولات تتطلب ذاكرة.

<img src="../../../translated_images/ar/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="المحادثات بدون حالة مقابل المحادثات بحالة" width="800"/>

*الفرق بين المحادثات بدون حالة (طلبات مستقلة) والمحادثات بحالة (واعية للسياق)*

## فهم الرموز

قبل الغوص في المحادثات، من المهم فهم الرموز - الوحدات الأساسية للنص التي تعالجها نماذج اللغة:

<img src="../../../translated_images/ar/token-explanation.c39760d8ec650181.webp" alt="شرح الرمز" width="800"/>

*مثال على كيفية تقسيم النص إلى رموز - "أحب الذكاء الاصطناعي!" تصبح 4 وحدات معالجة منفصلة*

الرموز هي كيفية قياس نماذج الذكاء الاصطناعي للنص ومعالجته. الكلمات، علامات الترقيم، وحتى الفراغات يمكن أن تكون رموزًا. لنموذجك حد أقصى لعدد الرموز التي يمكنه معالجتها دفعة واحدة (400,000 لـ GPT-5.2، مع ما يصل إلى 272,000 رمز إدخال و128,000 رمز إخراج). فهم الرموز يساعدك على إدارة طول المحادثة والتكاليف.

## كيف يعمل الذاكرة

ذاكرة الدردشة تحل مشكلة عدم وجود حالة من خلال الاحتفاظ بتاريخ المحادثة. قبل إرسال طلبك إلى النموذج، يقوم الإطار بإضافة الرسائل السابقة ذات الصلة. عندما تسأل "ما اسمي؟"، يرسل النظام فعليًا كامل تاريخ المحادثة، مما يسمح للنموذج برؤية أنك قلت سابقًا "اسمي جون".

تقدم LangChain4j تنفيذات للذاكرة تتعامل مع هذا تلقائيًا. تختار عدد الرسائل التي تريد الاحتفاظ بها ويدير الإطار نافذة السياق.

<img src="../../../translated_images/ar/memory-window.bbe67f597eadabb3.webp" alt="مفهوم نافذة الذاكرة" width="800"/>

*MessageWindowChatMemory يحتفظ بنافذة منزلقة للرسائل الحديثة، يحذف الرسائل القديمة تلقائيًا*

## كيف يستخدم هذا LangChain4j

تمتد هذه الوحدة البداية السريعة من خلال دمج Spring Boot وإضافة ذاكرة المحادثة. هكذا تتكامل القطع:

**التبعيات** - أضف مكتبتين من LangChain4j:

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

يقوم المنشئ بقراءة بيانات الاعتماد من متغيرات البيئة التي تم تعيينها بواسطة `azd up`. تعيين `baseUrl` إلى نقطة النهاية في Azure الخاصة بك يجعل عميل OpenAI يعمل مع Azure OpenAI.

**ذاكرة المحادثة** - تتبع سجل الدردشة باستخدام MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

أنشئ الذاكرة باستخدام `withMaxMessages(10)` للاحتفاظ بآخر 10 رسائل. أضف رسائل المستخدم والذكاء الاصطناعي باستخدام الأغلفة المكتوبة نوعيًا: `UserMessage.from(text)` و `AiMessage.from(text)`. استرجع التاريخ باستخدام `memory.messages()` وأرسله إلى النموذج. يقوم الخدمة بتخزين نسخ ذاكرة منفصلة لكل معرف محادثة، مما يسمح لعدة مستخدمين بالدردشة في آن واحد.

> **🤖 جرب مع دردشة [GitHub Copilot](https://github.com/features/copilot):** افتح [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) واسأل:
> - "كيف يقرر MessageWindowChatMemory أي الرسائل يتم حذفها عندما تمتلئ النافذة؟"
> - "هل يمكنني تنفيذ تخزين ذاكرة مخصص باستخدام قاعدة بيانات بدلاً من الذاكرة الداخلية؟"
> - "كيف يمكنني إضافة التلخيص لضغط تاريخ المحادثة القديم؟"

نقطة نهاية الدردشة بدون حالة تتخطى الذاكرة بالكامل - فقط `chatModel.chat(prompt)` كما في البداية السريعة. نقطة النهاية بحالة تضيف الرسائل إلى الذاكرة، تسترجع التاريخ، وتضمّن السياق مع كل طلب. نفس إعداد النموذج، أنماط مختلفة.

## نشر بنية Azure OpenAI التحتية

**Bash:**
```bash
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يوصى بـ eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # اختر الاشتراك والموقع (يوصى بـ eastus2)
```

> **ملاحظة:** إذا واجهت خطأ انتهاء مهلة (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)، فقط شغّل `azd up` مرة أخرى. قد تكون موارد Azure لا تزال قيد التهيئة في الخلفية، وإعادة المحاولة تسمح للنشر بالاكتمال عند وصول الموارد إلى حالة نهائية.

سوف يقوم ذلك بـ:
1. نشر مورد Azure OpenAI مع نماذج GPT-5.2 و text-embedding-3-small
2. إنشاء ملف `.env` تلقائيًا في جذر المشروع مع بيانات الاعتماد
3. إعداد جميع متغيرات البيئة المطلوبة

**هل تواجه مشاكل في النشر؟** انظر [README الخاصة بالبنية التحتية](infra/README.md) للحصول على تفاصيل حل المشكلات بما في ذلك تعارضات اسم النطاق الفرعي، خطوات النشر اليدوي عبر بوابة Azure، وإرشادات تكوين النموذج.

**تحقق من نجاح النشر:**

**Bash:**
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب عرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

> **ملاحظة:** يقوم الأمر `azd up` بإنشاء ملف `.env` تلقائيًا. إذا احتجت لتحديثه لاحقًا، يمكنك إما تعديل ملف `.env` يدويًا أو إعادة إنشائه من خلال تشغيل:
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

تأكد من وجود ملف `.env` في الدليل الجذر مع بيانات اعتماد Azure:

**Bash:**
```bash
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT وAPI_KEY وDEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيقات:**

**الخيار 1: استخدام لوحة Spring Boot (مستحسن لمستخدمي VS Code)**

يحتوي حاوية التطوير على إضافة لوحة Spring Boot، التي توفر واجهة مرئية لإدارة كل تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط على الجانب الأيسر من VS Code (ابحث عن أيقونة Spring Boot).

من لوحة Spring Boot، يمكنك:
- رؤية كل تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيقات في الوقت الحقيقي
- مراقبة حالة التطبيقات

فقط اضغط على زر التشغيل بجانب "introduction" لبدء هذه الوحدة، أو ابدأ كل الوحدات معًا.

<img src="../../../translated_images/ar/dashboard.69c7479aef09ff6b.webp" alt="لوحة Spring Boot" width="400"/>

**الخيار 2: استخدام سكريبتات shell**

ابدأ كل تطبيقات الويب (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من الدليل الجذري
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من الدليل الجذري
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

كلتا السكريبتات تقوم بتحميل متغيرات البيئة تلقائيًا من ملف `.env` الجذري وستبني ملفات JAR إذا لم تكن موجودة.

> **ملاحظة:** إذا كنت تفضل بناء كل الوحدات يدويًا قبل البدء:
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

**لإيقاف التطبيق:**

**Bash:**
```bash
./stop.sh  # هذا الموديول فقط
# أو
cd .. && ./stop-all.sh  # جميع الموديولات
```

**PowerShell:**
```powershell
.\stop.ps1  # هذا الوحدة فقط
# أو
cd ..; .\stop-all.ps1  # كل الوحدات
```

## استخدام التطبيق

يوفر التطبيق واجهة ويب مع اثنين من تطبيقات الدردشة جنبًا إلى جنب.

<img src="../../../translated_images/ar/home-screen.121a03206ab910c0.webp" alt="شاشة البداية للتطبيق" width="800"/>

*لوحة بيانات تظهر خيارات الدردشة البسيطة (بدون حالة) والدردشة المحادثية (بحالة)*

### دردشة بدون حالة (لوحة اليسار)

جربها أولاً. اسأل "اسمي جون" ثم مباشرة اسأل "ما اسمي؟" لن يتذكر النموذج لأنه كل رسالة مستقلة. هذا يوضح المشكلة الأساسية مع دمج نماذج اللغة البسيطة - لا يوجد سياق للمحادثة.

<img src="../../../translated_images/ar/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="عرض دردشة بدون حالة" width="800"/>

*الذكاء الاصطناعي لا يتذكر اسمك من الرسالة السابقة*

### دردشة بحالة (لوحة اليمين)

الآن جرب نفس التسلسل هنا. اسأل "اسمي جون" ثم "ما اسمي؟" هذه المرة يتذكر. الفرق هو MessageWindowChatMemory - يحتفظ بتاريخ المحادثة ويضمّنه مع كل طلب. هكذا يعمل الذكاء الاصطناعي المحادثي في الإنتاج.

<img src="../../../translated_images/ar/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="عرض دردشة بحالة" width="800"/>

*الذكاء الاصطناعي يتذكر اسمك من السابق في المحادثة*

كلتا اللوحتين تستخدمان نفس نموذج GPT-5.2. الفرق الوحيد هو الذاكرة. هذا يوضح بجلاء ما تضيفه الذاكرة لتطبيقك ولماذا هي ضرورية للاستخدامات الحقيقية.

## الخطوات التالية

**الوحدة التالية:** [02-هندسة المطالبات - هندسة المطالبات مع GPT-5.2](../02-prompt-engineering/README.md)

---

**التنقل:** [← السابق: الوحدة 00 - البداية السريعة](../00-quick-start/README.md) | [العودة للرئيسية](../README.md) | [التالي: الوحدة 02 - هندسة المطالبات →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى إلى الدقة، يرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق به. للمعلومات الهامة، يُنصح بالاعتماد على الترجمة المهنية البشرية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->