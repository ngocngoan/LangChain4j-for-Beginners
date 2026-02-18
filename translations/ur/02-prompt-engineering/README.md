# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ

## فہرست مضامین

- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [ضروریات](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کو سمجھنا](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کے بنیادی اصول](../../../02-prompt-engineering)
  - [زیرو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [فیو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [چین آف تھکاٹ](../../../02-prompt-engineering)
  - [رول پر مبنی پرامپٹنگ](../../../02-prompt-engineering)
  - [پرامپٹ ٹیمپلیٹس](../../../02-prompt-engineering)
- [جدید پیٹرنز](../../../02-prompt-engineering)
- [موجودہ آزور وسائل کا استعمال](../../../02-prompt-engineering)
- [ایپلیکیشن اسکرین شاٹس](../../../02-prompt-engineering)
- [پیٹرنز کو دریافت کرنا](../../../02-prompt-engineering)
  - [کم اور زیادہ جوش](../../../02-prompt-engineering)
  - [ٹاسک ایگزیکیوشن (ٹول پریایمبلز)](../../../02-prompt-engineering)
  - [سیلف ریفلیکٹنگ کوڈ](../../../02-prompt-engineering)
  - [ساختہ تجزیہ](../../../02-prompt-engineering)
  - [کئی مرحلوں کی چیٹ](../../../02-prompt-engineering)
  - [مرحلہ وار استدلال](../../../02-prompt-engineering)
  - [محدود آؤٹ پٹ](../../../02-prompt-engineering)
- [آپ واقعی کیا سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے اقدامات](../../../02-prompt-engineering)

## آپ کیا سیکھیں گے

<img src="../../../translated_images/ur/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

پچھلے ماڈیول میں، آپ نے دیکھا کہ میموری کس طرح مکالماتی AI کو ممکن بناتی ہے اور گٹ ہب ماڈلز کو بنیادی تعاملات کے لیے استعمال کیا۔ اب ہم اس بات پر توجہ دیں گے کہ آپ کیسے سوالات کرتے ہیں — یعنی پرامپٹس خود — آزور اوپن AI کے GPT-5.2 کا استعمال کرتے ہوئے۔ جس طرح آپ اپنے پرامپٹس کی ساخت کرتے ہیں، اس کا ردعمل کی کوالٹی پر زبردست اثر ہوتا ہے۔ ہم بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیں گے، پھر آٹھ جدید پیٹرنز کی طرف جائیں گے جو GPT-5.2 کی صلاحیتوں کا مکمل فائدہ اٹھاتے ہیں۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلال کی کنٹرول متعارف کراتا ہے - آپ ماڈل کو بتا سکتے ہیں کہ جواب دینے سے پہلے کتنی سوچ بچار کرنی ہے۔ اس سے مختلف پرامپٹنگ حکمت عملیاں زیادہ واضح ہو جاتی ہیں اور آپ کو سمجھنے میں مدد ملتی ہے کہ ہر طریقہ کب استعمال کرنا ہے۔ ہم آزور کے کم ریٹ لمٹس سے بھی فائدہ اٹھائیں گے جیسا کہ GPT-5.2 کے مقابلے میں گٹ ہب ماڈلز کے لیے۔

## ضروریات

- ماڈیول 01 مکمل کیا ہوا ہو (آزور اوپن AI کے وسائل تعینات کیے ہوئے)
- روٹ ڈائریکٹری میں `.env` فائل آزور کی اسناد کے ساتھ (جو ماڈیول 01 میں `azd up` کے ذریعے بنائی گئی ہو)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا ہے، تو پہلے وہاں دی گئی تعیناتی کی ہدایات پر عمل کریں۔

## پرامپٹ انجینئرنگ کو سمجھنا

<img src="../../../translated_images/ur/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

پرامپٹ انجینئرنگ کا مطلب ہے ایسا ان پٹ متن تیار کرنا جو مستقل طور پر آپ کو مطلوبہ نتائج فراہم کرے۔ یہ صرف سوالات کرنے کا معاملہ نہیں ہے - بلکہ درخواستوں کی ایسی ساخت مرتب کرنے کا ہے جس سے ماڈل بالکل سمجھ جائے کہ آپ کیا چاہتے ہیں اور اسے کیسے فراہم کرنا ہے۔

اسے یوں سوچیں جیسے آپ اپنے ساتھی کو ہدایت دے رہے ہوں۔ "بگ کو درست کریں" مبہم ہے۔ "UserService.java کی لائن 45 میں null pointer exception درست کریں، null چیک شامل کرکے" مخصوص ہے۔ لینگویج ماڈلز بھی اسی طرح کام کرتے ہیں - وضاحت اور ساخت اہم ہے۔

<img src="../../../translated_images/ur/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

لانگ چین 4ج وہ انفراسٹرکچر فراہم کرتا ہے — ماڈل کنکشنز، میموری، اور میسج ٹائپس — جبکہ پرامپٹ پیٹرنز محض محتاط ساخت شدہ متن ہیں جو آپ اس انفراسٹرکچر کے ذریعے بھیجتے ہیں۔ اہم تعمیراتی اجزاء `SystemMessage` (جو AI کے رویے اور کردار کو سیٹ کرتا ہے) اور `UserMessage` (جو آپ کی اصل درخواست لے کر جاتا ہے) ہیں۔

## پرامپٹ انجینئرنگ کے بنیادی اصول

<img src="../../../translated_images/ur/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

اس ماڈیول میں جدید پیٹرنز میں کودنے سے پہلے، آئیے پانچ بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہیں۔ یہ وہ تعمیراتی بلاکس ہیں جو ہر پرامپٹ انجینئر کو جاننے چاہئیں۔ اگر آپ نے پہلے ہی [کوئیک اسٹارٹ ماڈیول](../00-quick-start/README.md#2-prompt-patterns) کیا ہے، تو آپ نے انہیں عمل میں دیکھا ہوگا — یہ ان کے پیچھے کا تصوری خاکہ ہے۔

### زیرو شاٹ پرامپٹنگ

سب سے آسان طریقہ: ماڈل کو براہ راست ہدایت دیں بغیر کسی مثال کے۔ ماڈل مکمل طور پر اپنی تربیت پر اتکا ہوتا ہے کہ وہ کام کو سمجھے اور انجام دے۔ یہ سیدھی سادھی درخواستوں کے لیے بہت موثر ہے جہاں متوقع رویہ واضح ہو۔

<img src="../../../translated_images/ur/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*کوئی مثال نہیں، صرف براہِ راست ہدایت — ماڈل صرف ہدایت سے کام کا اندازہ لگاتا ہے*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// ردعمل: "مثبت"
```

**استعمال کب کریں:** سادہ درجہ بندی، براہ راست سوالات، تراجم، یا کوئی بھی کام جو ماڈل بغیر اضافی رہنمائی کے کر سکتا ہو۔

### فیو شاٹ پرامپٹنگ

ایسے مثالیں دیں جو وہ پیٹرن ظاہر کرتی ہوں جسے آپ ماڈل سے چاہ رہے ہیں۔ ماڈل آپ کی مثالوں سے متوقع ان پٹ آؤٹ پٹ فارمیٹ سیکھتا ہے اور اسے نئے ان پٹ پر لاگو کرتا ہے۔ یہ ایسے کاموں میں مستقل مزاجی نمایاں طور پر بہتر کرتا ہے جن میں مطلوبہ فارمیٹ یا رویہ واضح نہیں ہوتا۔

<img src="../../../translated_images/ur/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*مثالوں سے سیکھنا — ماڈل پیٹرن پہچان کر نئے ان پٹ پر لگاتا ہے*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**استعمال کب کریں:** کسٹم درجہ بندی، مستقل فارمیٹنگ، مخصوص ڈومین کے کام، یا جب زیرو شاٹ نتائج غیر مستقل ہوں۔

### چین آف تھکاٹ

ماڈل سے کہیں کہ وہ اپنا استدلال مرحلہ وار دکھائے۔ سیدھا جواب دینے کے بجائے، ماڈل مسئلہ کو توڑ کر ہر حصے کو واضح طور پر حل کرتا ہے۔ یہ ریاضی، منطق، اور کثیر مرحلوں والے استدلال کے کاموں میں درستگی بہتر کرتا ہے۔

<img src="../../../translated_images/ur/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*مرحلہ وار استدلال — پیچیدہ مسائل کو واضح منطقی مراحل میں تقسیم کرنا*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ماڈل دکھاتا ہے: ۱۵ - ۸ = ۷، پھر ۷ + ۱۲ = ۱۹ سیب
```

**استعمال کب کریں:** ریاضیاتی مسائل، منطقی پہیلیاں، ڈیبگنگ، یا کوئی بھی کام جہاں استدلال کا عمل دکھانے سے درستگی اور اعتماد بہتر ہو۔

### رول پر مبنی پرامپٹنگ

AI کے لیے کوئی کردار یا شخصیت متعین کریں قبل اس کے کہ آپ سوال پوچھیں۔ یہ ایسا سیاق و سباق مہیا کرتا ہے جو جواب کے لہجے، گہرائی، اور فوکس کو شکل دیتا ہے۔ "سافٹ ویئر آرکیٹیکٹ" کی مشورے "جونئیر ڈویلپر" یا "سیکیورٹی آڈیٹر" سے مختلف ہوتے ہیں۔

<img src="../../../translated_images/ur/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*سیاق و سباق اور کردار کی تعین — ایک ہی سوال کردار کے مطابق مختلف جواب دیتا ہے*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**استعمال کب کریں:** کوڈ ریویوز، تدریس، مخصوص ڈومین تجزیہ، یا جب آپ کو کسی خاص ماہر سطح یا نقطہ نظر کے مطابق جوابات چاہئیں۔

### پرامپٹ ٹیمپلیٹس

دوبارہ قابل استعمال پرامپٹس بنائیں جن میں متغیر جگہیں ہوں۔ ہر بار نیا پرامپٹ لکھنے کے بجائے، ایک ٹیمپلیٹ مرتب کریں اور مختلف قدریں بھریں۔ لانگ چین 4ج کی `PromptTemplate` کلاس اسے `{{variable}}` سنٹیکس سے آسان بناتی ہے۔

<img src="../../../translated_images/ur/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*دوبارہ استعمال کے پرامپٹس متغیر مقامات کے ساتھ — ایک ٹیمپلیٹ، کئی استعمالات*

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

**استعمال کب کریں:** مختلف ان پٹ کے ساتھ دہرائے جانے والے سوالات، بیچ پروسیسنگ، دوبارہ قابل استعمال AI ورک فلو بنانے، یا کوئی بھی منظر جہاں پرامپٹ کی ساخت ایک جیسی رہے مگر ڈیٹا تبدیل ہو۔

---

یہ پانچ بنیادی اصول زیادہ تر پرامپٹنگ کاموں کے لیے آپ کو ایک مضبوط ٹول کٹ فراہم کرتے ہیں۔ اس ماڈیول کا باقی حصہ ان پر آٹھ **جدید پیٹرنز** کے ساتھ ترقی کرتا ہے جو GPT-5.2 کی استدلال کنٹرول، خود جائزہ، اور ساختہ آؤٹ پٹ صلاحیتوں کا فائدہ اٹھاتے ہیں۔

## جدید پیٹرنز

بنیادی اصولوں کا احاطہ کرنے کے بعد، آئیے آٹھ جدید پیٹرنز کی طرف بڑھیں جو اس ماڈیول کو منفرد بناتے ہیں۔ تمام مسائل کو ایک ہی طریقہ کار کی ضرورت نہیں ہوتی۔ کچھ سوالات کو تیز جواب چاہیے، کچھ کو گہری سوچ۔ کچھ کو واضح استدلال چاہیے، کچھ کو صرف نتیجہ چاہیے۔ ہر نیچے دیا گیا پیٹرن مختلف منظر کے لیے بہتر بنایا گیا ہے — اور GPT-5.2 کی استدلال کنٹرول ان فرقوں کو اور نمایاں بناتی ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*آٹھ پرامپٹ انجینئرنگ پیٹرنز اور ان کے استعمال کے منظر نامے کا جائزہ*

<img src="../../../translated_images/ur/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 کا استدلال کنٹرول آپ کو بتانے دیتا ہے کہ ماڈل کو کتنا سوچنا چاہیے — تیز، براہ راست جوابات سے لے کر گہرے جائزے تک*

**کم جوش (تیز اور مرکوز)** - سادہ سوالات کے لیے جہاں آپ کو تیز، براہ راست جواب چاہیے۔ ماڈل کم از کم استدلال کرتا ہے - زیادہ سے زیادہ ۲ مراحل۔ حساب کتاب، تلاش، یا سیدھے سادے سوالات کے لیے استعمال کریں۔

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot کے ساتھ دریافت کریں:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) کھولیں اور پوچھیں:
> - "کم جوش اور زیادہ جوش پرامپٹنگ پیٹرنز میں کیا فرق ہے؟"
> - "پرامپٹس میں XML ٹیگز AI کے جواب کی ساخت میں کیسے مدد دیتے ہیں؟"
> - "میں کب خود عکاسی کے پیٹرنز اور کب براہ راست ہدایت استعمال کروں؟"

**زیادہ جوش (گہرا اور مکمل)** - پیچیدہ مسائل کے لیے جہاں آپ کو جامع تجزیہ چاہیے۔ ماڈل گہرائی سے تحقیق کرتا ہے اور مفصل استدلال دکھاتا ہے۔ نظام کے ڈیزائن، آرکیٹیکچر کے فیصلوں، یا پیچیدہ تحقیق کے لیے استعمال کریں۔

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایگزیکیوشن (مرحلہ وار پیش رفت)** - کثیر مرحلوں والے ورک فلو کے لیے۔ ماڈل پہلے ایک منصوبہ فراہم کرتا ہے، ہر مرحلے کی وضاحت کرتا ہے، پھر خلاصہ دیتا ہے۔ مائیگریشن، نفاذ، یا کسی بھی کثیر مرحلوں کے عمل کے لیے استعمال کریں۔

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

چین آف تھکاٹ پرامپٹنگ واضح طور پر ماڈل سے کہتا ہے کہ وہ اپنا استدلال دکھائے، جو پیچیدہ کاموں میں درستگی بہتر بناتا ہے۔ مرحلہ وار تجزیہ انسانوں اور AI دونوں کو منطق سمجھنے میں مدد دیتا ہے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** اس پیٹرن کے بارے میں پوچھیں:
> - "میں طویل دورانیے کے کاموں کے لیے ٹاسک ایگزیکیوشن پیٹرن کو کیسے اپنانا چاہوں گا؟"
> - "پروڈکشن ایپلیکیشن میں ٹول پریایمبلز کی تشکیل کے بہترین طریقے کیا ہیں؟"
> - "UI میں درمیانی پیش رفت کی تازہ کاریوں کو کیسے پکڑا اور دکھایا جا سکتا ہے؟"

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*کئی مراحل والے کاموں کے لیے منصوبہ → عمل درآمد → خلاصہ ورک فلو*

**سیلف-ریفلیکٹنگ کوڈ** - پروڈکشن معیار کے کوڈ کے لیے۔ ماڈل پروڈکشن اسٹینڈرڈز کے مطابق کوڈ جنریٹ کرتا ہے جس میں مناسب ایرر ہینڈلنگ ہوتی ہے۔ نئی خصوصیات یا خدمات بناتے وقت استعمال کریں۔

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*دہرائی کے ذریعے بہتری کا چکر - جنریٹ کریں، اندازہ لگائیں، مسائل شناخت کریں، بہتر کریں، دہرائیں*

**ساختہ تجزیہ** - مستقل جائزے کے لیے۔ ماڈل کوڈ کا جائزہ لیتا ہے ایک طے شدہ فریم ورک (درستگی، طرز عمل، کارکردگی، سیکیورٹی، قابلِ دیکھ بھال) کے مطابق۔ کوڈ ریویوز یا معیار کی جانچ کے لیے استعمال کریں۔

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** ساختہ تجزیہ کے بارے میں پوچھیں:
> - "میں مختلف قسم کے کوڈ ریویوز کے لیے تجزیاتی فریم ورک کو کیسے حسب ضرورت بنا سکتا ہوں؟"
> - "ساختہ آؤٹ پٹ کو پروگرام کے ذریعے پارس اور عمل کرنے کا بہترین طریقہ کیا ہے؟"
> - "میں مختلف ریویو سیشنز میں یکساں سنگینی کی سطح کیسے یقینی بنا سکتا ہوں؟"

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*یکساں کوڈ ریویو کے لیے فریم ورک، سنگینی کی سطحوں کے ساتھ*

**کئی مرحلوں کی چیٹ** - ایسے مکالمے جنہیں سیاق و سباق کی ضرورت ہو۔ ماڈل پچھلے پیغامات یاد رکھتا ہے اور ان پر مزید تعمیر کرتا ہے۔ انٹرایکٹو ہیلپ سیشنز یا پیچیدہ سوال و جواب کے لیے استعمال کریں۔

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ur/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*مکالمے کا سیاق و سباق کئی مراحل میں جمع ہوتا ہے جب تک کہ ٹوکن کی حد نہ پہنچ جائے*

**مرحلہ وار استدلال** - ایسے مسائل کے لیے جنہیں واضح منطق کی ضرورت ہو۔ ماڈل ہر مرحلے کے لیے واضح استدلال دکھاتا ہے۔ ریاضیاتی مسائل، منطقی پہیلیاں، یا جب آپ کو سوچنے کے عمل کو سمجھنا ہو استعمال کریں۔

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*مسائل کو واضح منطقی مراحل میں تقسیم کرنا*

**محدود آؤٹ پٹ** - مخصوص فارمیٹ کی شرطوں والے جوابات کے لیے۔ ماڈل سختی سے فارمیٹ اور لمبائی کے قواعد کی پیروی کرتا ہے۔ خلاصوں یا جب آپ کو درست آؤٹ پٹ ساخت چاہیے استعمال کریں۔

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

<img src="../../../translated_images/ur/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*مخصوص فارمیٹ، لمبائی، اور ساخت کی شرطوں کا نفاذ*

## موجودہ آزور وسائل کا استعمال

**تصدیق کریں تعیناتی:**

یقینی بنائیں کہ `.env` فائل روٹ ڈائریکٹری میں موجود ہو آزور کی اسناد کے ساتھ (جو ماڈیول 01 کے دوران بنائی گئی تھی):
```bash
cat ../.env  # ظاہر ہونا چاہیے AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT
```

**ایپلیکیشن شروع کریں:**

> **نوٹ:** اگر آپ نے پہلے ماڈیول 01 سے `./start-all.sh` کے ذریعے تمام ایپلیکیشنز شروع کی ہیں، تو یہ ماڈیول پہلے ہی پورٹ 8083 پر چل رہا ہے۔ نیچے دیے گئے شروع کرنے کے کمانڈز چھوڑ کر براہ راست http://localhost:8083 پر جا سکتے ہیں۔

**اختیار 1: اسپرنگ بوٹ ڈیش بورڈ کا استعمال (VS کوڈ صارفین کے لیے سفارش کی جاتی ہے)**

ڈیو کنٹینر میں اسپرنگ بوٹ ڈیش بورڈ ایکسٹینشن شامل ہے، جو تمام اسپرنگ بوٹ ایپلیکیشنز کو نظم و نسق کے لیے ایک بصری انٹرفیس فراہم کرتا ہے۔ اسے VS کوڈ کے Activity بار کے بائیں طرف دیکھیں (اسپرنگ بوٹ کا آئیکن تلاش کریں)۔

اسپرنگ بوٹ ڈیش بورڈ سے آپ:
- ورک اسپیس میں دستیاب تمام اسپرنگ بوٹ ایپلیکیشنز دیکھ سکتے ہیں
- ایپلیکیشنز کو ایک کلک سے شروع/روک سکتے ہیں
- ایپلیکیشن لاگز کو حقیقی وقت میں دیکھ سکتے ہیں
- ایپلیکیشن کی حالت کی نگرانی کر سکتے ہیں
بس "prompt-engineering" کے ساتھ کھیلنے کا بٹن دبائیں تاکہ یہ ماڈیول شروع ہو، یا تمام ماڈیولز کو ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ڈیش بورڈ" width="400"/>

**آپشن 2: شیل اسکرپٹس کا استعمال**

تمام ویب ایپلیکیشنز (ماڈیولز 01-04) شروع کریں:

**باش:**
```bash
cd ..  # جڑ ڈائریکٹری سے
./start-all.sh
```

**پاور شیل:**
```powershell
cd ..  # جڑ ڈائرکٹری سے
.\start-all.ps1
```

یا صرف اس ماڈیول کو شروع کریں:

**باش:**
```bash
cd 02-prompt-engineering
./start.sh
```

**پاور شیل:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

دونوں اسکرپٹس خود بخود روٹ `.env` فائل سے ماحول کے متغیرات لوڈ کرتے ہیں اور اگر JAR فائلز موجود نہ ہوں تو انہیں بنائیں گے۔

> **نوٹ:** اگر آپ شروع کرنے سے پہلے تمام ماڈیولز کو دستی طور پر بنانا پسند کرتے ہیں:
>
> **باش:**
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

**روکنے کے لیے:**

**باش:**
```bash
./stop.sh  # صرف یہ ماڈیول
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**پاور شیل:**
```powershell
.\stop.ps1  # صرف یہ ماڈیول
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## ایپلیکیشن اسکرین شاٹس

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="ڈیش بورڈ ہوم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*اہم ڈیش بورڈ جو تمام 8 پرامپٹ انجینئرنگ پیٹرنز کو ان کی خصوصیات اور استعمال کے کیسز کے ساتھ دکھاتا ہے*

## پیٹرنز کی جانچ پڑتال

ویب انٹرفیس آپ کو مختلف پرامپٹنگ حکمت عملیوں کے ساتھ تجربہ کرنے دیتا ہے۔ ہر پیٹرن مختلف مسائل حل کرتا ہے - انہیں آزمائیں دیکھنے کے لیے کہ کب کون سا طریقہ کار کارآمد ہوتا ہے۔

### کم اور زیادہ جوش

کم جوش کے ساتھ سادہ سوال کریں جیسے "200 کا 15٪ کیا ہے؟" آپ کو فوری، براہ راست جواب ملے گا۔ اب زیادہ جوش کے ساتھ کچھ پیچیدہ پوچھیں جیسے "ہائی ٹریفک API کے لیے کیشنگ حکمت عملی ڈیزائن کریں"۔ دیکھیں کہ ماڈل کیسے سست ہو کر تفصیلی وضاحت فراہم کرتا ہے۔ وہی ماڈل، وہی سوالی ڈھانچہ - لیکن پرامپٹ بتاتا ہے کہ کتنا سوچنا ہے۔

<img src="../../../translated_images/ur/low-eagerness-demo.898894591fb23aa0.webp" alt="کم جوش ڈیمو" width="800"/>

*کم سے کم سوچ بچار کے ساتھ فوری حساب*

<img src="../../../translated_images/ur/high-eagerness-demo.4ac93e7786c5a376.webp" alt="زیادہ جوش ڈیمو" width="800"/>

*جامع کیشنگ حکمت عملی (2.8MB)*

### ٹاسک کی انجام دہی (ٹول پری ایمبلز)

کئی مراحل کے ورک فلو کو پہلے سے منصوبہ بندی اور پیش رفت کی وضاحت سے فائدہ ہوتا ہے۔ ماڈل بتاتا ہے کہ وہ کیا کرنے والا ہے، ہر قدم کو بیان کرتا ہے، اور پھر نتائج کو خلاصہ کرتا ہے۔

<img src="../../../translated_images/ur/tool-preambles-demo.3ca4881e417f2e28.webp" alt="ٹاسک انجام دہی ڈیمو" width="800"/>

*قدم بہ قدم وضاحت کے ساتھ REST اینڈپوائنٹ بنانا (3.9MB)*

### خود عکاسی کرنے والا کوڈ

"ایک ای میل ویلیڈیشن سروس بنائیں" آزما کر دیکھیں۔ کوڈ بنانے اور رکنے کے بجائے، ماڈل تخلیق کرتا ہے، معیار کے خلاف جانچتا ہے، کمزوریوں کی نشاندہی کرتا ہے، اور بہتری لاتا ہے۔ آپ اسے بار بار دہرایا دیکھیں گے جب تک کہ کوڈ پروڈکشن معیار پر نہ پہنچ جائے۔

<img src="../../../translated_images/ur/self-reflecting-code-demo.851ee05c988e743f.webp" alt="خود عکاسی کرنے والے کوڈ کا ڈیمو" width="800"/>

*مکمل ای میل ویلیڈیشن سروس (5.2MB)*

### ساختی تجزیہ

کوڈ کے جائزے کے لیے مستقل جائزہ فریم ورک ضروری ہے۔ ماڈل کوڈ کا تجزیہ مستقل زمروں (درستی، طریقے، کارکردگی، سکیورٹی) کے مطابق شدت کی سطحوں کے ساتھ کرتا ہے۔

<img src="../../../translated_images/ur/structured-analysis-demo.9ef892194cd23bc8.webp" alt="ساختی تجزیہ ڈیمو" width="800"/>

*فریم ورک پر مبنی کوڈ ریویو*

### کثیر مرحلہ چیٹ

"Spring Boot کیا ہے؟" پوچھیں، پھر فوراً "ایک مثال دکھائیں" سے آگے بڑھیں۔ ماڈل آپ کا پہلا سوال یاد رکھتا ہے اور خصوصی طور پر Spring Boot کی مثال دیتا ہے۔ بغیر یادداشت کے، دوسرا سوال بہت مبہم ہوتا۔

<img src="../../../translated_images/ur/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="کثیر مرحلہ چیٹ ڈیمو" width="800"/>

*سوالات کے دوران سیاق و سباق کا تحفظ*

### قدم بہ قدم منطق

کسی ریاضی مسئلے کا انتخاب کریں اور اسے Step-by-Step Reasoning اور Low Eagerness دونوں کے ساتھ آزمائیں۔ کم جوش بس جواب دیتا ہے — تیز لیکن غیر واضح۔ قدم بہ قدم آپ کو ہر حساب اور فیصلے کی وضاحت دکھاتا ہے۔

<img src="../../../translated_images/ur/step-by-step-reasoning-demo.12139513356faecd.webp" alt="قدم بہ قدم منطق ڈیمو" width="800"/>

*واضح مراحل کے ساتھ ریاضی مسئلہ*

### محدود نتیجہ

جب آپ کو مخصوص فارمیٹس یا الفاظ کی تعداد کی ضرورت ہو، یہ پیٹرن سخت پابندی عائد کرتا ہے۔ ایک ایسا خلاصہ بنانے کی کوشش کریں جو بالکل 100 الفاظ پر مشتمل ہو اور بلٹ پوائنٹس میں ہو۔

<img src="../../../translated_images/ur/constrained-output-demo.567cc45b75da1633.webp" alt="محدود نتیجہ ڈیمو" width="800"/>

*فارمیٹ کنٹرول کے ساتھ مشین لرننگ کا خلاصہ*

## آپ حقیقت میں کیا سیکھ رہے ہیں

**منطق کی کوشش سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنے پرامپٹس کے ذریعے حسابی کوشش کو کنٹرول کرنے دیتا ہے۔ کم کوشش کا مطلب ہے تیز جواب اور کم تلاش۔ زیادہ کوشش کا مطلب ہے کہ ماڈل وقت لے کر گہرائی سے سوچتا ہے۔ آپ سیکھ رہے ہیں کہ کام کی پیچیدگی کے مطابق کوشش کریں - سادہ سوالات پر وقت ضائع نہ کریں، مگر پیچیدہ فیصلوں کو جلد بازی میں نہ کریں۔

**ڈھانچہ رویے کی رہنمائی کرتا ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دیکھے؟ یہ سجاوٹ نہیں ہیں۔ ماڈلز ساختی ہدایات کو آزاد متنی تحریر کی نسبت زیادہ قابل اعتبار طریقے سے فالو کرتے ہیں۔ جب آپ کو کئی مراحل یا پیچیدہ منطق کی ضرورت ہو، ڈھانچہ ماڈل کو بتاتا ہے کہ وہ کہاں ہے اور اگلا کیا کرنا ہے۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="پرامپٹ کا ڈھانچہ" width="800"/>

*واضح سیکشنز اور XML طرز کی تنظیم کے ساتھ ایک اچھی ساخت والا پرامپٹ*

**خود تشخیص کے ذریعے معیار**

خود عکاسی کرنے والے پیٹرنز معیار کے معیارات کو واضح کرتے ہیں۔ اس کے بجائے کہ ماڈل کو "صحیح کرنے کی امید کریں"، آپ اسے بتاتے ہیں کہ "صحیح" کا مطلب کیا ہے: درست منطق، خرابی کی ہینڈلنگ، کارکردگی، سکیورٹی۔ پھر ماڈل اپنی آؤٹ پٹ کا جائزہ لے کر اسے بہتر بناتا ہے۔ یہ کوڈ بنانے کو قرعہ اندازی سے ایک عمل میں بدل دیتا ہے۔

**سیاق و سباق محدود ہے**

کثیر مرحلہ بات چیت ہر درخواست کے ساتھ پیغام کی تاریخ کو شامل کر کے کام کرتی ہے۔ لیکن حد ہوتی ہے - ہر ماڈل کی زیادہ سے زیادہ ٹوکن تعداد ہوتی ہے۔ جیسے جیسے بات چیت بڑھتی ہے، آپ کو ایسی حکمت عملیوں کی ضرورت ہوگی کہ متعلقہ سیاق و سباق برقرار رکھیں بغیر حد کو چھوئے۔ یہ ماڈیول آپ کو یادداشت کا کام سکھاتا ہے؛ بعد میں آپ سیکھیں گے کہ کب خلاصہ کرنا ہے، کب بھولنا ہے، اور کب بازیافت کرنا ہے۔

## اگلے اقدامات

**اگلا ماڈیول:** [03-rag - RAG (ریٹریول-اگمینٹڈ جنریشن)](../03-rag/README.md)

---

**نیویگیشن:** [← پچھلا: ماڈیول 01 - تعارف](../01-introduction/README.md) | [مین پر واپس](../README.md) | [اگلا: ماڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ذمہ داری سے انکار**:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم یہ بات ذہن میں رکھیں کہ خودکار ترجمے میں غلطیاں یا نقائص ہو سکتے ہیں۔ اصل دستاویز اپنی مقامی زبان میں ماخذ اور معتبر سمجھا جائے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تعبیر کی ذمہ داری ہم پر نہیں ہوگی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->