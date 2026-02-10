# الوحدة 01: البدء مع LangChain4j

## جدول المحتويات

- [ما الذي ستتعلمه](../../../01-introduction)
- [المتطلبات الأساسية](../../../01-introduction)
- [فهم المشكلة الأساسية](../../../01-introduction)
- [فهم الرموز](../../../01-introduction)
- [كيف تعمل الذاكرة](../../../01-introduction)
- [كيف يستخدم هذا LangChain4j](../../../01-introduction)
- [نشر بنية تحتية Azure OpenAI](../../../01-introduction)
- [تشغيل التطبيق محليًا](../../../01-introduction)
- [استخدام التطبيق](../../../01-introduction)
  - [الدردشة بدون حالة (اللوحة اليسرى)](../../../01-introduction)
  - [الدردشة بحالة (اللوحة اليمنى)](../../../01-introduction)
- [الخطوات التالية](../../../01-introduction)

## ما الذي ستتعلمه

إذا أتممت البداية السريعة، فقد رأيت كيف ترسل المطالبات وتحصل على ردود. هذا هو الأساس، ولكن التطبيقات الحقيقية تحتاج إلى المزيد. تعلمك هذه الوحدة كيفية بناء ذكاء اصطناعي محادثي يتذكر السياق ويحافظ على الحالة - الفرق بين عرض تجريبي لمرة واحدة وتطبيق جاهز للإنتاج.

سنستخدم GPT-5.2 من Azure OpenAI طوال هذا الدليل لأن قدراته المتقدمة في الاستدلال تجعل سلوك الأنماط المختلفة أكثر وضوحًا. عند إضافة الذاكرة، سترى الفرق بوضوح. هذا يسهل فهم ما يضيفه كل مكون لتطبيقك.

ستبني تطبيقًا واحدًا يظهر كلا النمطين:

**الدردشة بدون حالة** - كل طلب مستقل. النموذج لا يحتفظ بذاكرة الرسائل السابقة. هذا هو النمط الذي استخدمته في البداية السريعة.

**المحادثة بحالة** - كل طلب يتضمن سجل المحادثة. النموذج يحافظ على السياق عبر جولات متعددة. هذا ما تتطلبه تطبيقات الإنتاج.

## المتطلبات الأساسية

- اشتراك Azure مع وصول إلى Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ملاحظة:** Java و Maven و Azure CLI و Azure Developer CLI (azd) مثبتة مسبقًا في الحاوية التطويرية المقدمة.

> **ملاحظة:** تستخدم هذه الوحدة GPT-5.2 على Azure OpenAI. يتم تكوين النشر تلقائيًا عبر `azd up` - لا تقم بتعديل اسم النموذج في الكود.

## فهم المشكلة الأساسية

نماذج اللغة بدون حالة. كل استدعاء API مستقل. إذا أرسلت "اسمي جون" ثم سألت "ما اسمي؟"، النموذج ليس لديه فكرة أنك قدمت نفسك للتو. يعامل كل طلب كما لو كانت هذه هي أول محادثة لديك على الإطلاق.

هذا مناسب للأسئلة والإجابات البسيطة لكنه عديم الفائدة للتطبيقات الحقيقية. روبوتات خدمة العملاء بحاجة لتذكر ما أخبرتهم به. المساعدون الشخصيون بحاجة للسياق. أي محادثة متعددة الجولات تتطلب ذاكرة.

<img src="../../../translated_images/ar/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="المحادثات بدون حالة مقابل بحالة" width="800"/>

*الفرق بين المحادثات بدون حالة (استدعاءات مستقلة) والمحادثات بحالة (واعية للسياق)*

## فهم الرموز

قبل الخوض في المحادثات، من المهم فهم الرموز - الوحدات الأساسية للنص التي تعالجها نماذج اللغة:

<img src="../../../translated_images/ar/token-explanation.c39760d8ec650181.webp" alt="شرح الرموز" width="800"/>

*مثال على كيفية تقسيم النص إلى رموز - "أحب الذكاء الاصطناعي!" تصبح 4 وحدات معالجة منفصلة*

الرموز هي كيفية قياس نماذج الذكاء الاصطناعي ومعالجة النص. الكلمات وعلامات الترقيم وحتى المسافات يمكن أن تكون رموزًا. لنموذجك حد أقصى لعدد الرموز التي يمكنها معالجتها مرة واحدة (400,000 لـ GPT-5.2، مع ما يصل إلى 272,000 رمز مدخلات و128,000 رمز مخرجات). فهم الرموز يساعدك في إدارة طول المحادثة والتكاليف.

## كيف تعمل الذاكرة

تحل ذاكرة الدردشة مشكلة عدم الحالة عن طريق الحفاظ على سجل المحادثة. قبل إرسال طلبك إلى النموذج، الإطار يضيف الرسائل السابقة ذات الصلة في المقدمة. عندما تسأل "ما اسمي؟"، النظام يرسل فعليًا سجل المحادثة بالكامل، مما يسمح للنموذج برؤية أنك قلت سابقًا "اسمي جون".

يوفر LangChain4j تطبيقات للذاكرة تتعامل مع هذا تلقائيًا. تختار عدد الرسائل التي تحتفظ بها والإطار يدير نافذة السياق.

<img src="../../../translated_images/ar/memory-window.bbe67f597eadabb3.webp" alt="مفهوم نافذة الذاكرة" width="800"/>

*MessageWindowChatMemory يحافظ على نافذة منزلقة للرسائل الحديثة، تتخلص تلقائيًا من الرسائل القديمة*

## كيف يستخدم هذا LangChain4j

توسع هذه الوحدة البداية السريعة من خلال دمج Spring Boot وإضافة ذاكرة المحادثة. إليك كيف تتوافق الأجزاء معًا:

**الاعتمادات** - أضف مكتبتين من LangChain4j:

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

**نموذج الدردشة** - تكوين Azure OpenAI كـ bean في Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

يقوم الباني بقراءة بيانات الاعتماد من متغيرات البيئة التي يضبطها `azd up`. ضبط `baseUrl` على نقطة نهاية Azure الخاصة بك يجعل عميل OpenAI يعمل مع Azure OpenAI.

**ذاكرة المحادثة** - تتبع سجل الدردشة باستخدام MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

إنشاء الذاكرة باستخدام `withMaxMessages(10)` للاحتفاظ بآخر 10 رسائل. أضف رسائل المستخدم و AI باستخدام أغلفة مكتوبة: `UserMessage.from(text)` و `AiMessage.from(text)`. استرجع السجل بـ `memory.messages()` وأرسله للنموذج. الخدمة تخزن نسخ ذاكرة منفصلة لكل معرّف محادثة، مما يسمح لمستخدمين متعددين بالدردشة في الوقت نفسه.

> **🤖 جرب مع [GitHub Copilot](https://github.com/features/copilot) Chat:** افتح [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) واطرح:
> - "كيف يقرر MessageWindowChatMemory الرسائل التي يحذفها عندما تكون النافذة ممتلئة؟"
> - "هل يمكنني تنفيذ تخزين ذاكرة مخصص باستخدام قاعدة بيانات بدلاً من التخزين في الذاكرة؟"
> - "كيف يمكنني إضافة التلخيص لضغط سجل المحادثة القديم؟"

نقطة النهاية للدردشة بدون حالة تتجاهل الذاكرة تمامًا - مجرد `chatModel.chat(prompt)` كما في البداية السريعة. نقطة النهاية للحوار بحالة تضيف الرسائل إلى الذاكرة، تسترجع السجل، وتشمل هذا السياق مع كل طلب. نفس تكوين النموذج، أنماط مختلفة.

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

> **ملاحظة:** إذا واجهت خطأ مهلة (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)، فقط أعد تشغيل `azd up` مرة أخرى. قد تكون موارد Azure لا تزال في طور الإعداد في الخلفية، وإعادة المحاولة تسمح للنشر بأن يكتمل عند وصول الموارد إلى حالة نهائية.

سيفعل هذا:
1. ينشر مورد Azure OpenAI مع نماذج GPT-5.2 و text-embedding-3-small
2. يولد تلقائيًا ملف `.env` في جذر المشروع مع بيانات الاعتماد
3. يضبط جميع متغيرات البيئة المطلوبة

**تواجه مشاكل في النشر؟** اطلع على [README البنية التحتية](infra/README.md) للتفاصيل حول استكشاف الأخطاء، بما في ذلك تعارضات اسم النطاق الفرعي، خطوات النشر اليدوي عبر بوابة Azure، وإرشادات تكوين النماذج.

**تحقق من نجاح النشر:**

**Bash:**
```bash
cat ../.env  # يجب عرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، إلخ.
```

> **ملاحظة:** أمر `azd up` يولد تلقائيًا ملف `.env`. إذا كنت بحاجة لتحديثه لاحقًا، يمكنك تعديل ملف `.env` يدويًا أو إعادة توليده بتشغيل:
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
cat ../.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # يجب أن يعرض AZURE_OPENAI_ENDPOINT و API_KEY و DEPLOYMENT
```

**ابدأ التطبيقات:**

**الخيار 1: استخدام لوحة تحكم Spring Boot (مفضل لمستخدمي VS Code)**

تحتوي حاوية التطوير على امتداد لوحة تحكم Spring Boot، الذي يوفر واجهة بصرية لإدارة كل تطبيقات Spring Boot. يمكنك العثور عليها في شريط النشاط بالجانب الأيسر من VS Code (ابحث عن رمز Spring Boot).

من لوحة تحكم Spring Boot، يمكنك:
- رؤية جميع تطبيقات Spring Boot المتاحة في مساحة العمل
- بدء/إيقاف التطبيقات بنقرة واحدة
- عرض سجلات التطبيقات في الوقت الحقيقي
- مراقبة حالة التطبيق

فقط انقر على زر التشغيل بجانب "introduction" لبدء هذه الوحدة، أو ابدأ كل الوحدات دفعة واحدة.

<img src="../../../translated_images/ar/dashboard.69c7479aef09ff6b.webp" alt="لوحة تحكم Spring Boot" width="400"/>

**الخيار 2: استخدام سكربتات الشل**

ابدأ جميع التطبيقات الويبية (الوحدات 01-04):

**Bash:**
```bash
cd ..  # من دليل الجذر
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # من دليل الجذر
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

كلا السكربتين يحملان تلقائيًا متغيرات البيئة من ملف `.env` في الجذر وسيبنيان ملفات JAR إذا لم تكن موجودة.

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

**للإيقاف:**

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

يقدم التطبيق واجهة ويب مع تنفيذين للدردشة جنبًا إلى جنب.

<img src="../../../translated_images/ar/home-screen.121a03206ab910c0.webp" alt="شاشة الصفحة الرئيسية للتطبيق" width="800"/>

*لوحة تعرض خيارات الدردشة البسيطة (بدون حالة) والدردشة الحوارية (بحالة)*

### الدردشة بدون حالة (اللوحة اليسرى)

جرب هذا أولاً. اسأل "اسمي جون" ثم اسأل فورًا "ما اسمي؟" النموذج لن يتذكر لأن كل رسالة مستقلة. هذا يوضح المشكلة الأساسية في دمج نموذج اللغة الأساسي - لا يوجد سياق للمحادثة.

<img src="../../../translated_images/ar/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="عرض للدردشة بدون حالة" width="800"/>

*الذكاء الاصطناعي لا يتذكر اسمك من الرسالة السابقة*

### الدردشة بحالة (اللوحة اليمنى)

الآن جرب نفس التسلسل هنا. اسأل "اسمي جون" ثم "ما اسمي؟" هذه المرة يتذكر. الفرق هو MessageWindowChatMemory - التي تحافظ على سجل المحادثة وتضمينه مع كل طلب. هذا هو كيف يعمل الذكاء الاصطناعي في المحادثات الإنتاجية.

<img src="../../../translated_images/ar/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="عرض للدردشة بحالة" width="800"/>

*الذكاء الاصطناعي يتذكر اسمك من وقت سابق في المحادثة*

كلتا اللوحتين تستخدمان نفس نموذج GPT-5.2. الاختلاف الوحيد هو الذاكرة. هذا يوضح بجلاء ما تضيفه الذاكرة لتطبيقك ولماذا هي ضرورية للاستخدامات الحقيقية.

## الخطوات التالية

**الوحدة التالية:** [02-prompt-engineering - هندسة المطالبات مع GPT-5.2](../02-prompt-engineering/README.md)

---

**التنقل:** [← السابق: الوحدة 00 - البداية السريعة](../00-quick-start/README.md) | [العودة للرئيسية](../README.md) | [التالي: الوحدة 02 - هندسة المطالبات →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**إخلاء المسؤولية**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر المعتمد والرسمي. بالنسبة للمعلومات الحساسة أو المهمة، يُنصح بالاعتماد على الترجمة البشرية المهنية. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->