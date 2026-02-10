# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ

## فہرست مضامین

- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [پیشگی ضروریات](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کو سمجھنا](../../../02-prompt-engineering)
- [یہ LangChain4j کیسے استعمال کرتا ہے](../../../02-prompt-engineering)
- [بنیادی پیٹرن](../../../02-prompt-engineering)
- [موجودہ Azure وسائل کا استعمال](../../../02-prompt-engineering)
- [درخواست کے اسکرین شاٹس](../../../02-prompt-engineering)
- [پیٹرنز کو دریافت کرنا](../../../02-prompt-engineering)
  - [کم بمقابلہ زیادہ جوش](../../../02-prompt-engineering)
  - [ٹاسک کا نفاذ (ٹول پری ایمبلز)](../../../02-prompt-engineering)
  - [خود عکاسی کوڈ](../../../02-prompt-engineering)
  - [ساختی تجزیہ](../../../02-prompt-engineering)
  - [کئی دورانیے کی چیٹ](../../../02-prompt-engineering)
  - [مرحلہ وار استدلال](../../../02-prompt-engineering)
  - [محدود آؤٹ پٹ](../../../02-prompt-engineering)
- [آپ کیا واقعی سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے مراحل](../../../02-prompt-engineering)

## آپ کیا سیکھیں گے

پچھلے ماڈیول میں، آپ نے دیکھا کہ یادداشت کس طرح گفتگویی اے آئی کو ممکن بناتی ہے اور بنیادی بات چیت کے لیے GitHub ماڈلز استعمال کیے۔ اب ہم اس بات پر توجہ دیں گے کہ آپ سوالات کیسے پوچھتے ہیں—یعنی پرامپٹس خود—Azure OpenAI کے GPT-5.2 کو استعمال کرتے ہوئے۔ جس طرح آپ اپنے پرامپٹس کو ترتیب دیتے ہیں وہ آپ کو ملنے والے جوابات کے معیار پر گہرا اثر ڈالتا ہے۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلالی کنٹرول متعارف کراتا ہے—آپ ماڈل کو بتا سکتے ہیں کہ کتنی سوچ بچار کے بعد جواب دینا ہے۔ یہ مختلف پرامپٹنگ حکمت عملیوں کو واضح بناتا ہے اور آپ کو سمجھنے میں مدد دیتا ہے کہ کب کونسی حکمت عملی استعمال کرنی چاہیے۔ نیز، Azure کے GPT-5.2 کے لیے GitHub ماڈلز کے مقابلے میں کم ریٹس کی حد ہوتی ہے، جس کا فائدہ بھی اٹھائیں گے۔

## پیشگی ضروریات

- ماڈیول 01 مکمل کیا ہوا (Azure OpenAI وسائل تعینات کیے ہوئے)
- روٹ ڈائریکٹری میں `.env` فائل Azure کی اسناد کے ساتھ (جو ماڈیول 01 میں `azd up` سے بنائی گئی ہو)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا، تو پہلے وہاں دی گئی تعیناتی کی ہدایات پر عمل کریں۔

## پرامپٹ انجینئرنگ کو سمجھنا

پرامپٹ انجینئرنگ وہ عمل ہے جس میں ایسی ان پُٹ تحریر ڈیزائن کی جاتی ہے جو مسلسل مطلوبہ نتائج دیتی ہے۔ یہ صرف سوالات پوچھنے کے بارے میں نہیں بلکہ درخواستوں کو اس طرح ترتیب دینے کے بارے میں ہے کہ ماڈل کو بالکل سمجھ آجائے کہ آپ کیا چاہتے ہیں اور کس طرح فراہم کرنا ہے۔

اسے یوں سمجھیں جیسے آپ کسی ساتھی کو ہدایات دے رہے ہوں۔ "بگ ٹھیک کرو" غیر واضح ہے۔ "UserService.java کی لائن 45 میں null pointer exception کو null چیک شامل کرکے ٹھیک کرو" واضح ہے۔ زبان کے ماڈلز بھی ایسا ہی کرتے ہیں—وضاحت اور ساخت بہت اہم ہے۔

## یہ LangChain4j کیسے استعمال کرتا ہے

یہ ماڈیول پرامپٹنگ کے جدید پیٹرنز کا مظاہرہ کرتا ہے جو پچھلے ماڈیولز کی طرح LangChain4j کی بنیاد پر مبنی ہیں، لیکن توجہ پرامپٹ کی ساخت اور استدلالی کنٹرول پر ہے۔

<img src="../../../translated_images/ur/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j فلُو" width="800"/>

*LangChain4j آپ کے پرامپٹس کو Azure OpenAI GPT-5.2 سے کیسے جوڑتا ہے*

**انحصار** - ماڈیول 02 `pom.xml` میں بیان کردہ درج ذیل langchain4j انحصارات استعمال کرتا ہے:
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

**OpenAiOfficialChatModel کنفیگریشن** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

چیٹ ماڈل دستی طور پر ایک Spring bean کے طور پر OpenAI کی سرکاری کلائنٹ کے ساتھ ترتیب دیا گیا ہے، جو Azure OpenAI کے اینڈپوائنٹس کو سپورٹ کرتا ہے۔ ماڈیول 01 سے بنیادی فرق یہ ہے کہ ہم پرامپٹس کو `chatModel.chat()` کو کس طرح بھیجتے ہیں، ماڈل کی ترتیب نہیں۔

**سسٹم اور یوزر میسجز** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j پیغامات کی اقسام کو وضاحت کے لیے الگ رکھتا ہے۔ `SystemMessage` AI کے رویے اور سیاق و سباق سیٹ کرتا ہے (مثلاً "آپ ایک کوڈ ریویور ہیں")، جبکہ `UserMessage` اصل درخواست رکھتا ہے۔ اس علیحدگی سے آپ مختلف یوزر سوالات پر AI کے رویے کو مستقل رکھ سکتے ہیں۔

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ur/message-types.93e0779798a17c9d.webp" alt="پیغام کی اقسام کی ساخت" width="800"/>

*SystemMessage مستقل سیاق و سباق فراہم کرتا ہے جبکہ UserMessages انفرادی درخواستیں رکھتے ہیں*

**Multi-Turn کے لیے MessageWindowChatMemory** - کثیر مرحلہ بات چیت پیٹرن کے لیے، ہم ماڈیول 01 کی `MessageWindowChatMemory` کو دوبارہ استعمال کرتے ہیں۔ ہر سیشن کی اپنی میموری ہوتی ہے جو `Map<String, ChatMemory>` میں محفوظ ہوتی ہے، اس سے متعدد بیک وقت بات چیت بغیر سیاق و سباق کے مکس ہوئے چل سکتی ہیں۔

**پرامپٹ ٹیمپلیٹس** - اصل توجہ پرامپٹ انجینئرنگ پر ہے، نہ کہ نئے LangChain4j APIs پر۔ ہر پیٹرن (کم جوش، زیادہ جوش، ٹاسک ایگزیکیوشن وغیرہ) ایک ہی `chatModel.chat(prompt)` طریقہ استعمال کرتا ہے لیکن احتیاط سے ترتیب دیے پرامپٹ اسٹرنگز کے ساتھ۔ XML ٹیگز، ہدایات، اور فارمیٹنگ سب پرامپٹ متن کا حصہ ہیں، LangChain4j کی خصوصیات نہیں۔

**استدلالی کنٹرول** - GPT-5.2 کا استدلالی محنت پرامپٹ ہدایات جیسے "زیادہ سے زیادہ 2 استدلالی قدم" یا "تفصیل سے دریافت کریں" کے ذریعے کنٹرول کیا جاتا ہے۔ یہ پرامپٹ انجینئرنگ کی تکنیکیں ہیں، LangChain4j کنفیگریشنز نہیں۔ لائبریری صرف آپ کے پرامپٹس کو ماڈل تک پہنچاتی ہے۔

اہم نقطہ: LangChain4j بنیادی ڈھانچہ فراہم کرتا ہے (ماڈل کنکشن [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)، میموری، پیغام ہینڈلنگ [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) کے ذریعے)، جبکہ یہ ماڈیول آپ کو سکھاتا ہے کہ اس بنیادی ڈھانچے کے اندر مؤثر پرامپٹس کیسے تیار کیے جائیں۔

## بنیادی پیٹرنز

تمام مسائل کے لیے ایک ہی طریقہ کار ضروری نہیں ہوتا۔ کچھ سوالات کو فوری جواب درکار ہوتا ہے، بعض کو عمیق سوچ کی ضرورت ہوتی ہے۔ کچھ کو واضح استدلال چاہیے، اور بعض بس نتائج چاہتے ہیں۔ یہ ماڈیول آٹھ پرامپٹنگ پیٹرنز کو کور کرتا ہے—ہر ایک مختلف حالات کے لیے مخصوص ہے۔ آپ سب کے ساتھ تجربہ کریں گے تاکہ سمجھ سکیں کہ کون سا طریقہ کب بہترین ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="آٹھ پرامپٹنگ پیٹرنز" width="800"/>

*آٹھ پرامپٹ انجینئرنگ پیٹرنز کا جائزہ اور ان کے استعمال کے کیسز*

<img src="../../../translated_images/ur/reasoning-effort.db4a3ba5b8e392c1.webp" alt="استدلالی محنت کا موازنہ" width="800"/>

*کم جوش (تیز، براہ راست) بمقابلہ زیادہ جوش (تفصیلی، تحقیقی) استدلالی طریقے*

**کم جوش (تیز اور مرکوز)** - آسان سوالات کے لیے جہاں آپ کو فوری اور براہ راست جواب چاہیے۔ ماڈل کم از کم استدلال کرتا ہے—زیادہ سے زیادہ 2 قدم۔ اس کا استعمال حساب، تلاش یا سیدھے سوالات کے لیے کریں۔

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub کوپائلٹ کے ساتھ دریافت کریں:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) کھول کر پوچھیں:
> - "کم جوش اور زیادہ جوش پرامپٹنگ پیٹرنز میں کیا فرق ہے؟"
> - "پرامپٹس میں XML ٹیگز AI کے جواب کی ساخت میں کیسے مدد دیتے ہیں؟"
> - "میں کب خود عکاسی کے پیٹرنز استعمال کروں اور کب براہ راست ہداہت دوں؟"

**زیادہ جوش (گہرائی اور تفصیل میں)** - پیچیدہ مسائل کے لیے جہاں جامع تجزیہ چاہیے۔ ماڈل تفصیل سے غور کرتا ہے اور مفصل استدلال دکھاتا ہے۔ اس کا استعمال سسٹم ڈیزائن، معمارانہ فیصلے، یا پیچیدہ تحقیق کے لیے کریں۔

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایگزیکیوشن (مرحلہ وار پیش رفت)** - کثیر مرحلہ ورک فلو کے لیے۔ ماڈل پہلے ایک منصوبہ فراہم کرتا ہے، ہر مرحلہ بیان کرتا ہے، پھر خلاصہ دے دیتا ہے۔ اس کا استعمال مائیگریشن، نفاذ، یا کسی بھی کثیر مرحلہ عمل کے لیے کریں۔

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

چھان بین کے عمل (Chain-of-Thought) پرامپٹنگ ماڈل سے استدلالی عمل دکھانے کا کہہ کر درستگی بڑھاتا ہے۔ مرحلہ وار وضاحت انسانوں اور AI دونوں کے لیے منطق کو سمجھنا آسان بناتی ہے۔

> **🤖 GitHub Copilot چیٹ کے ساتھ آزمائیں:** اس پیٹرن کے بارے میں پوچھیں:
> - "میں لمبے عرصے والے عمل کے لیے ٹاسک ایگزیکیوشن پیٹرن کو کیسے اپناؤں؟"
> - "پروڈکشن ایپلیکیشنز میں ٹول پری ایمبلز کی بہترین ساخت کیا ہے؟"
> - "UI میں درمیانی پیش رفت کی اپ ڈیٹس کو کیسے کیپچر اور ڈسپلے کیا جا سکتا ہے؟"

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="ٹاسک ایگزیکیوشن پیٹرن" width="800"/>

*مرحلہ وار کام کے لیے منصوبہ → نفاذ → خلاصہ ورک فلو*

**خود عکاسی کوڈ** - اعلی معیار کا کوڈ پیدا کرنے کے لیے۔ ماڈل کوڈ بناتا ہے، معیار کے معیار کے مطابق جانچتا ہے، اور اسے بہتر بناتا ہے۔ نئی خصوصیات یا خدمات بنانے کے وقت استعمال کریں۔

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="خود عکاسی سائیکل" width="800"/>

*تکراری بہتری کا چکر - پیدا کریں، جائزہ لیں، مسائل کی نشاندہی کریں، بہتر کریں، دہرانا*

**ساختی تجزیہ** - مستقل جائزے کے لیے۔ ماڈل ایک مقررہ فریم ورک (درستی، طریقے، کارکردگی، سیکیورٹی) کے مطابق کوڈ کا جائزہ لیتا ہے۔ یہ کوڈ ریویو یا معیار کے جائزے کے لیے استعمال ہوتا ہے۔

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 GitHub Copilot چیٹ کے ساتھ آزمائیں:** ساختی تجزیہ کے بارے میں پوچھیں:
> - "میں مختلف قسم کے کوڈ ریویوز کے لیے تجزیاتی فریم ورک کو کیسے حسب ضرورت بنا سکتا ہوں؟"
> - "ساختی آؤٹ پٹ کو پروگراماتی طور پر پارس اور عمل کرنے کا بہترین طریقہ کیا ہے؟"
> - "میں مختلف جائزہ سیشنز میں یکساں شدت کی سطحیں کیسے یقینی بناؤں؟"

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ساختی تجزیہ پیٹرن" width="800"/>

*یکساں کوڈ جائزوں کے لیے چار زمرہ جاتی فریم ورک، شدت کی سطحوں کے ساتھ*

**کئی دورانیے کی چیٹ** - ایسی بات چیت کے لیے جسے سیاق و سباق کی ضرورت ہو۔ ماڈل سابقہ پیغامات کو یاد رکھتا ہے اور ان پر تعمیر کرتا ہے۔ انٹرایکٹیو مدد یا پیچیدہ سوال جواب کے لیے استعمال کریں۔

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ur/context-memory.dff30ad9fa78832a.webp" alt="سیاق و سباق میموری" width="800"/>

*متعدد دورانیوں میں بات چیت کا سیاق و سباق جمع ہونا جب تک کہ ٹوکن کی حد نہ پہنچ جائے*

**مرحلہ وار استدلال** - ایسے مسائل کے لیے جو واضح منطق چاہتے ہیں۔ ماڈل ہر قدم کے لیے واضح استدلال دکھاتا ہے۔ اس کا استعمال ریاضی کے مسائل، منطقی پہیلیوں، یا جب آپ سوچنے کے عمل کو سمجھنا چاہتے ہیں کریں۔

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="مرحلہ وار پیٹرن" width="800"/>

*مسائل کو واضح منطقی مراحل میں تقسیم کرنا*

**محدود آؤٹ پٹ** - خاص فارمیٹ کی ضروریات والے جوابات کے لیے۔ ماڈل فارمیٹ اور لمبائی کے قواعد کی سختی سے پیروی کرتا ہے۔ خلاصے یا جب دقیق آؤٹ پٹ ساخت چاہیے ہو استعمال کریں۔

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/constrained-output-pattern.0ce39a682a6795c2.webp" alt="محدود آؤٹ پٹ پیٹرن" width="800"/>

*مخصوص فارمیٹ، لمبائی، اور ساخت کی پابندیوں کو نافذ کرنا*

## موجودہ Azure وسائل کا استعمال

**تعیناتی کی تصدیق کریں:**

یقینی بنائیں کہ روٹ ڈائریکٹری میں `.env` فائل موجود ہے جس میں Azure اسناد ہوں (جو ماڈیول 01 کے دوران بنائی گئی تھیں):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT کو دکھانا چاہیے
```

**درخواست شروع کریں:**

> **نوٹ:** اگر آپ پہلے ہی ماڈیول 01 سے `./start-all.sh` استعمال کرکے تمام ایپلیکیشنز چلا چکے ہیں، تو یہ ماڈیول پہلے ہی پورٹ 8083 پر چل رہا ہے۔ آپ نیچے دیے گئے شروع کرنے کے کمانڈز کو چھوڑ کر براہ راست http://localhost:8083 پر جا سکتے ہیں۔

**اختیار 1: Spring Boot ڈیش بورڈ کا استعمال (VS Code صارفین کے لیے سفارش شدہ)**

ڈویلپمنٹ کنٹینر میں Spring Boot ڈیش بورڈ ایکسٹینشن شامل ہے، جو تمام Spring Boot ایپلیکیشنز کو منظم کرنے کے لیے بصری انٹرفیس فراہم کرتا ہے۔ آپ اسے VS Code کے بائیں جانب Activity Bar میں Spring Boot کے آئکن کے ذریعے تلاش کر سکتے ہیں۔

Spring Boot ڈیش بورڈ سے آپ کر سکتے ہیں:
- ورک اسپیس میں تمام دستیاب Spring Boot ایپلیکیشنز دیکھیں
- ایک کلک سے ایپلیکیشنز شروع یا بند کریں
- ریئل ٹائم میں ایپلیکیشن لاگز دیکھیں
- ایپلیکیشن کی حالت مانیٹر کریں

بس "prompt-engineering" کے ساتھ پلے بٹن پر کلک کریں تاکہ یہ ماڈیول شروع ہو، یا تمام ماڈیولز ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ڈیش بورڈ" width="400"/>

**اختیار 2: شیل اسکرپٹس کا استعمال**

تمام ویب ایپلیکیشنز (ماڈیول 01-04) شروع کریں:

**بش:**
```bash
cd ..  # روٹ ڈائریکٹری سے
./start-all.sh
```

**پاور شیل:**
```powershell
cd ..  # روٹ ڈائریکٹری سے
.\start-all.ps1
```

یا صرف یہی ماڈیول شروع کریں:

**بش:**
```bash
cd 02-prompt-engineering
./start.sh
```

**پاور شیل:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

دونوں اسکرپٹس خود بخود روٹ `.env` فائل سے ماحول کے متغیرات لوڈ کرتے ہیں اور اگر JARs موجود نہیں تو بنائیں گے۔

> **نوٹ:** اگر آپ ترجیح دیتے ہیں کہ شروع کرنے سے پہلے تمام ماڈیولز کو دستی طور پر بنائیں:
>
> **بش:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **پاور شیل:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

اپنے براؤزر میں http://localhost:8083 کھولیں۔

**بند کرنے کے لیے:**

**بش:**
```bash
./stop.sh  # صرف یہ ماڈیول
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**پاور شیل:**
```powershell
.\stop.ps1  # یہ ماڈیول صرف
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## درخواست کے اسکرین شاٹس

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="ڈیش بورڈ ہوم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*مین ڈیش بورڈ جو تمام 8 پرامپٹ انجینئرنگ پیٹرنز ان کی خصوصیات اور استعمال کی صورتوں کے ساتھ دکھا رہا ہے*

## پیٹرنز کو دریافت کرنا

ویب انٹرفیس آپ کو مختلف پرامپٹنگ حکمت عملیوں کے ساتھ تجربہ کرنے دیتا ہے۔ ہر پیٹرن مختلف مسائل کو حل کرتا ہے—انہیں آزمائیں تاکہ دیکھ سکیں کہ ہر طریقہ کب زیادہ مؤثر ہے۔

### کم بمقابلہ زیادہ جوش

ایک سادہ سوال پوچھیں جیسے "200 کا 15% کیا ہے؟" کم جوش استعمال کرتے ہوئے۔ آپ کو فوری، براہ راست جواب ملے گا۔ اب ایک پیچیدہ سوال پوچھیں جیسے "ایک ہائی ٹریفک API کے لیے کیشنگ حکمت عملی ڈیزائن کریں" زیادہ جوش کے ساتھ۔ دیکھیں کہ ماڈل کیسے سست ہوتا ہے اور تفصیلی استدلال فراہم کرتا ہے۔ ایک ہی ماڈل، ایک ہی سوال کی ساخت - لیکن پرامپٹ ماڈل کو بتاتا ہے کہ کتنی سوچ بچار کرنی ہے۔
<img src="../../../translated_images/ur/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*کم سے کم استدلال کے ساتھ تیز حساب کتاب*

<img src="../../../translated_images/ur/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*جامع کیشنگ حکمت عملی (2.8MB)*

### کام کی تکمیل (آلات کی تعارفی ہدایات)

کئی مراحل والے ورک فلو کو پہلے سے منصوبہ بندی اور پیش رفت کی وضاحت سے فائدہ ہوتا ہے۔ ماڈل بیان کرتا ہے کہ وہ کیا کرے گا، ہر مرحلہ کی وضاحت کرتا ہے، پھر نتائج کا خلاصہ پیش کرتا ہے۔

<img src="../../../translated_images/ur/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*مرحلہ وار وضاحت کے ساتھ REST انڈپوائنٹ بنانا (3.9MB)*

### خود جائزہ لینے والا کوڈ

کوشش کریں "ای میل ویلیڈیشن سروس بنائیں"۔ صرف کوڈ بنانے اور رکنے کی بجائے، ماڈل تخلیق کرتا ہے، معیار کی بنیاد پر جائزہ لیتا ہے، کمزوریوں کی نشاندہی کرتا ہے، اور بہتری لاتا ہے۔ آپ دیکھیں گے کہ یہ تب تک دہرائے گا جب تک کوڈ پیداوار کے معیار پر پورا نہ اترے۔

<img src="../../../translated_images/ur/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*مکمل ای میل ویلیڈیشن سروس (5.2MB)*

### منظم تشخیص

کوڈ کا جائزہ لینے کے لئے مسلسل تشخیصی فریم ورک کی ضرورت ہوتی ہے۔ ماڈل کوڈ کو مقررہ زمروں (درستی، عملی طریقے، کارکردگی، سیکیورٹی) کے ساتھ شدت کی سطحوں کے تحت تجزیہ کرتا ہے۔

<img src="../../../translated_images/ur/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*فریم ورک کی بنیاد پر کوڈ کا جائزہ*

### متعدد جولانوالی گفتگو

پوچھیں "Spring Boot کیا ہے؟" پھر فوراً پوچھیں "مجھے ایک مثال دکھائیں"۔ ماڈل آپ کے پہلے سوال کو یاد رکھتا ہے اور خاص طور پر Spring Boot کی مثال دیتا ہے۔ یادداشت کے بغیر دوسرا سوال بہت مبہم ہوتا۔

<img src="../../../translated_images/ur/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*سوالات کے درمیان سیاق و سباق کی حفاظت*

### قدم بہ قدم استدلال

کسی ریاضی کے مسئلے کا انتخاب کریں اور اسے Step-by-Step Reasoning اور Low Eagerness دونوں کے ساتھ آزمائیں۔ Low Eagerness صرف جواب دیتا ہے - تیز لیکن غیر واضح۔ Step-by-step ہر حساب اور فیصلہ دکھاتا ہے۔

<img src="../../../translated_images/ur/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*واضح مراحل کے ساتھ ریاضی کا مسئلہ*

### محدود آؤٹ پٹ

جب آپ کو مخصوص فارمیٹس یا الفاظ کی تعداد کی ضرورت ہو، یہ پیٹرن سخت پابندی کرتا ہے۔ کوشش کریں بال پوائنٹ فارم میں بالکل 100 الفاظ کے خلاصے کی تخلیق کریں۔

<img src="../../../translated_images/ur/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*فارمیٹ کنٹرول کے ساتھ مشین لرننگ کا خلاصہ*

## آپ واقعی کیا سیکھ رہے ہیں

**استدلال کی محنت سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنے پرامپٹس کے ذریعے حسابی محنت کنٹرول کرنے دیتا ہے۔ کم محنت کا مطلب ہے تیز جوابات کم تفصیلی تلاش کے ساتھ۔ زیادہ محنت کا مطلب ہے کہ ماڈل گہرائی سے سوچنے میں وقت لگاتا ہے۔ آپ سیکھ رہے ہیں کہ محنت کو کام کی پیچیدگی کے مطابق ملائیں - سادہ سوالات پر وقت ضائع نہ کریں، مگر پیچیدہ فیصلوں میں جلد بازی بھی نہ کریں۔

**ساخت رویے کی رہنمائی کرتی ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دھیان سے دیکھے؟ وہ صرف تزئین کے لئے نہیں ہیں۔ ماڈلز منظم ہدایات کو آزاد متن سے زیادہ قابل اعتبار طریقے سے فالو کرتے ہیں۔ جب آپ کو متعدد مرحلوں کے عمل یا پیچیدہ منطق کی ضرورت ہو، ساخت مدد دیتی ہے کہ ماڈل اپنی جگہ اور اگلے قدم کو ٹریک کرے۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*واضح سیکشنز اور XML طرز تنظیم کے ساتھ اچھی طرح ساخت بند پرامپٹ کا خاکہ*

**خود جائزہ کے ذریعے معیار**

خود جائزہ لینے والے پیٹرنز معیاری شرائط کو واضح کرکے کام کرتے ہیں۔ بجائے اس کے کہ ماڈل “درست طریقے سے کرے”، آپ اسے بتاتے ہیں کہ “درست” کا مطلب کیا ہے: درست منطق، غلطی کی ہینڈلنگ، کارکردگی، سیکیورٹی۔ پھر ماڈل اپنی پیداوار کا جائزہ لے سکتا ہے اور بہتری لا سکتا ہے۔ یہ کوڈ تخلیق کو قرعہ اندازی سے ایک منظم عمل میں بدل دیتا ہے۔

**سیاق و سباق محدود ہے**

متعدد جُولانوالی بات چیت ہر درخواست کے ساتھ پیغام کی تاریخ شامل کر کے کام کرتی ہے۔ لیکن حد ہے - ہر ماڈل کا زیادہ سے زیادہ ٹوکن شمار ہوتا ہے۔ جوں جوں بات چیت بڑھتی ہے، آپ کو اس حد کو نہ پہنچنے دینا ہوتا ہے ایک حکمت عملی کے ساتھ متعلقہ سیاق و سباق کو برقرار رکھنا پڑتا ہے۔ یہ ماڈیول آپ کو دکھاتا ہے کہ میموری کیسے کام کرتی ہے؛ بعد میں آپ سیکھیں گے کب خلاصہ کرنا ہے، کب بھولنا ہے، اور کب بازیافت کرنا ہے۔

## اگلے مراحل

**اگلا ماڈیول:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**نیویگیشن:** [← پچھلا: ماڈیول 01 - تعارف](../01-introduction/README.md) | [مین صفحہ پر واپس](../README.md) | [اگلا: ماڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دفعہ ذمہ داری**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کی بھرپور کوشش کرتے ہیں، براہ کرم یاد رکھیں کہ خودکار تراجم میں غلطیاں یا کمی بیشی ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں ہی معتبر ماخذ سمجھی جائے۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمہ کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کے لیے ہم ذمہ دار نہیں ہوں گے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->