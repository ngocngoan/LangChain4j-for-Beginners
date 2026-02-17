# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ

## جدول مضامین

- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [ضروریات](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کو سمجھنا](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کے بنیادی اصول](../../../02-prompt-engineering)
  - [زیرو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [فیوشاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [چین آف تھوٹ](../../../02-prompt-engineering)
  - [رول پر مبنی پرامپٹنگ](../../../02-prompt-engineering)
  - [پرامپٹ ٹیمپلیٹس](../../../02-prompt-engineering)
- [ترقی یافتہ نمونے](../../../02-prompt-engineering)
- [موجودہ Azure وسائل کا استعمال](../../../02-prompt-engineering)
- [ایپلیکیشن کی سکرین شاٹس](../../../02-prompt-engineering)
- [نمونوں کا جائزہ](../../../02-prompt-engineering)
  - [کم بمقابلہ زیادہ جذبہ](../../../02-prompt-engineering)
  - [ٹاسک ایگزیکیوشن (ٹول پری ایمبلز)](../../../02-prompt-engineering)
  - [خود احتساب کوڈ](../../../02-prompt-engineering)
  - [منظم تجزیہ](../../../02-prompt-engineering)
  - [کئی مرحلوں کی بات چیت](../../../02-prompt-engineering)
  - [قدم بہ قدم استدلال](../../../02-prompt-engineering)
  - [محدود آؤٹ پٹ](../../../02-prompt-engineering)
- [آپ واقعی کیا سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے اقدامات](../../../02-prompt-engineering)

## آپ کیا سیکھیں گے

<img src="../../../translated_images/ur/what-youll-learn.c68269ac048503b2.webp" alt="آپ کیا سیکھیں گے" width="800"/>

گزشتہ ماڈیول میں، آپ نے دیکھا کہ یاداشت کس طرح بات چیت کرنے والی مصنوعی ذہانت کو ممکن بناتی ہے اور بنیادی تعاملی کاموں کے لیے GitHub ماڈلز استعمال کیے۔ اب ہم اس بات پر توجہ مرکوز کریں گے کہ آپ سوالات کیسے پوچھتے ہیں — یعنی خود پرامپٹس کو کیسے تیار کرتے ہیں — Azure OpenAI کے GPT-5.2 کا استعمال کرتے ہوئے۔ جس انداز میں آپ پرامپٹس کو ترتیب دیتے ہیں اس سے آپ کو ملنے والے جوابات کا معیار بہت متاثر ہوتا ہے۔ ہم بنیادی پرامپٹ کرنے کی تکنیکوں کا جائزہ لیتے ہیں، پھر آٹھ ترقی یافتہ نمونوں کی طرف بڑھتے ہیں جو GPT-5.2 کی صلاحیتوں سے مکمل فائدہ اٹھاتے ہیں۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلال کنٹرول متعارف کراتا ہے - آپ ماڈل کو یہ بتا سکتے ہیں کہ جواب دینے سے پہلے کتنا سوچنا ہے۔ اس سے مختلف پرامپٹ کرنے کی حکمت عملیاں واضح ہوتی ہیں اور آپ کو سمجھنے میں مدد ملتی ہے کہ کب کون سا طریقہ استعمال کرنا ہے۔ ہم Azure کی کم ریٹ لمٹس سے بھی فائدہ اٹھائیں گے جو GPT-5.2 کے لیے GitHub ماڈلز کے مقابلے میں بہتر ہیں۔

## ضروریات

- ماڈیول 01 مکمل کیا ہوا ہو (Azure OpenAI وسائل تعینات کیے گئے ہوں)
- فولڈر کی روٹ ڈائریکٹری میں `.env` فائل Azure کی سندوں کے ساتھ موجود ہو (ماڈیول 01 میں `azd up` کے ذریعے بنائی گئی)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا، تو پہلے وہاں دی گئی تعیناتی کی ہدایات پر عمل کریں۔

## پرامپٹ انجینئرنگ کو سمجھنا

<img src="../../../translated_images/ur/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="پرامپٹ انجینئرنگ کیا ہے؟" width="800"/>

پرامپٹ انجینئرنگ کا مقصد ایسے ان پٹ متن کی ڈیزائننگ ہے جو آپ کو مستقل طور پر وہ نتائج فراہم کرے جو آپ چاہتے ہیں۔ یہ صرف سوالات پوچھنے کے بارے میں نہیں ہے — بلکہ درخواستوں کو اس طرح ترتیب دینے کے بارے میں ہے تاکہ ماڈل بالکل سمجھ سکے کہ آپ کیا چاہتے ہیں اور اسے کیسے فراہم کرنا ہے۔

اسے ایسے سمجھیں جیسے کسی ساتھی کو ہدایات دے رہے ہوں۔ "بگ کو ٹھیک کریں" مبہم ہے۔ "UserService.java کی لائن 45 میں null pointer exception کو null چیک شامل کرکے ٹھیک کریں" مخصوص ہے۔ زبان کے ماڈلز بھی اسی طرح کام کرتے ہیں — وضاحت اور ساخت اہم ہوتی ہے۔

<img src="../../../translated_images/ur/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j کس طرح فٹ بیٹھتا ہے" width="800"/>

LangChain4j انفراسٹرکچر فراہم کرتا ہے — ماڈل کنیکشنز، یادداشت، اور پیغام کی اقسام — جبکہ پرامپٹ کے نمونے صرف احتیاط سے ترتیب دیا گیا متن ہیں جو آپ اس انفراسٹرکچر کے ذریعے بھیجتے ہیں۔ کلیدی بلاکس `SystemMessage` ہیں (جو AI کے رویے اور کردار کو سیٹ کرتا ہے) اور `UserMessage` جو آپ کی اصل درخواست لے کر آتا ہے۔

## پرامپٹ انجینئرنگ کے بنیادی اصول

<img src="../../../translated_images/ur/five-patterns-overview.160f35045ffd2a94.webp" alt="پانچ پرامپٹ انجینئرنگ نمونوں کا جائزہ" width="800"/>

اس ماڈیول میں ترقی یافتہ نمونوں میں غوطہ لگانے سے پہلے، آئیے پانچ بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیں۔ یہ وہ بنیادی ستون ہیں جو ہر پرامپٹ انجینئر کو جاننے چاہئیں۔ اگر آپ پہلے سے [Quick Start ماڈیول](../00-quick-start/README.md#2-prompt-patterns) سے گزر چکے ہیں، تو آپ نے انہیں عملی طور پر دیکھا ہوگا — یہ ان کے پیچھے نظریاتی فریم ورک ہے۔

### زیرو شاٹ پرامپٹنگ

سب سے آسان طریقہ: ماڈل کو براہ راست ہدایت دینا بغیر کسی مثال کے۔ ماڈل اپنے تربیتی ڈیٹا پر پوری طرح انحصار کرتا ہے تاکہ کام کو سمجھے اور انجام دے۔ یہ سادہ درخواستوں کے لیے اچھا کام کرتا ہے جہاں مطلوبہ رویہ واضح ہو۔

<img src="../../../translated_images/ur/zero-shot-prompting.7abc24228be84e6c.webp" alt="زیرو شاٹ پرامپٹنگ" width="800"/>

*مثال کے بغیر براہ راست ہدایت — ماڈل صرف ہدایت کی بنیاد پر کام سمجھتا ہے*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// جواب: "مثبت"
```

**استعمال کب کریں:** سادہ درجہ بندی، براہ راست سوالات، ترجمہ، یا کوئی بھی ایسا کام جسے ماڈل بغیر اضافی رہنمائی کے ہینڈل کر سکے۔

### فیوشاٹ پرامپٹنگ

ایسے مثالیں فراہم کریں جو ماڈل کو دکھائیں کہ آپ کن پیٹرنز کی توقع رکھتے ہیں۔ ماڈل آپ کی مثالوں سے ان پٹ-آوٹ پٹ کے فارمیٹ کو سیکھتا ہے اور اسے نئے ان پٹس پر اپلائی کرتا ہے۔ اس سے ان کاموں میں مکمل یکسانیت آتی ہے جہاں مطلوبہ فارمیٹ یا رویہ واضح نہ ہو۔

<img src="../../../translated_images/ur/few-shot-prompting.9d9eace1da88989a.webp" alt="فیوشاٹ پرامپٹنگ" width="800"/>

*مثالوں سے سیکھنا — ماڈل پیٹرن کو پہچان کر نئے ان پٹس پر لاگو کرتا ہے*

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

**استعمال کب کریں:** حسب ضرورت درجہ بندی، مستقل فارمیٹنگ، مخصوص ڈومین کے کام، یا جب زیرو شاٹ نتائج غیر مستقل ہوں۔

### چین آف تھوٹ

ماڈل سے کہیں کہ وہ اپنے استدلال کو قدم بہ قدم ظاہر کرے۔ سیدھے جواب پر جانے کے بجائے، ماڈل مسئلہ کو توڑ کر ہر حصہ کی وضاحت کرتا ہے۔ اس سے ریاضی، منطق، اور کئی مراحل پر مشتمل استدلالی کاموں کی درستگی بہتر ہوتی ہے۔

<img src="../../../translated_images/ur/chain-of-thought.5cff6630e2657e2a.webp" alt="چین آف تھوٹ پرامپٹنگ" width="800"/>

*قدم بہ قدم استدلال — پیچیدہ مسائل کو واضح منطقی مراحل میں تقسیم کرنا*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ماڈل دکھاتا ہے: 15 - 8 = 7، پھر 7 + 12 = 19 سیبیں
```

**استعمال کب کریں:** ریاضی کے مسائل، منطق کے معما، ڈیبگنگ، یا کوئی ایسا کام جہاں استدلالی عمل دکھانے سے درستگی اور اعتماد بڑھتا ہو۔

### رول پر مبنی پرامپٹنگ

اپنے سوال پوچھنے سے پہلے AI کے لیے ایک شخص یا کردار مرتب کریں۔ یہ ایسا سیاق و سباق فراہم کرتا ہے جو جواب کے لہجے، گہرائی، اور توجہ کو متعین کرتا ہے۔ "سافٹ ویئر آکیٹیکٹ" "جونئیر ڈیویلپر" یا "سیکیورٹی آڈیٹر" کے مقابلے مختلف مشورے دیتا ہے۔

<img src="../../../translated_images/ur/role-based-prompting.a806e1a73de6e3a4.webp" alt="رول پر مبنی پرامپٹنگ" width="800"/>

*سیاق و سباق اور کردار سیٹ کرنا — ایک ہی سوال کو مختلف کرداروں کے تحت مختلف جواب ملتے ہیں*

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

**استعمال کب کریں:** کوڈ ریویوز، ٹیوٹرنگ، ڈومین مخصوص تجزیہ، یا جب آپ کو مخصوص مہارت یا نقطہ نظر کے مطابق جوابات چاہیے ہوں۔

### پرامپٹ ٹیمپلیٹس

دوبارہ استعمال کے قابل پرامپٹس بنائیں جن میں متغیر مقامات ہوں۔ ہر بار نیا پرامپٹ لکھنے کی بجائے، ایک ٹیمپلیٹ ڈیفائن کریں اور مختلف اقدار بھر دیں۔ LangChain4j کی `PromptTemplate` کلاس `{{variable}}` سینٹیکس کے ساتھ یہ آسان بناتی ہے۔

<img src="../../../translated_images/ur/prompt-templates.14bfc37d45f1a933.webp" alt="پرامپٹ ٹیمپلیٹس" width="800"/>

*متغیر مقامات کے ساتھ دوبارہ استعمال کے قابل پرامپٹس — ایک ٹیمپلیٹ، کئی استعمال*

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

**استعمال کب کریں:** مختلف ان پٹس کے ساتھ بار بار کیے جانے والے سوالات، بیچ پروسیسنگ، دوبارہ استعمال کے قابل AI ورک فلو بنانے، یا کسی بھی منظرنامے میں جہاں پرامپٹ کی ساخت یکساں ہو مگر ڈیٹا بدلتا رہے۔

---

یہ پانچ بنیادی اصول زیادہ تر پرامپٹنگ کاموں کے لیے آپ کو ایک مضبوط ٹول کٹ فراہم کرتے ہیں۔ اس ماڈیول کا باقی حصہ ان پر مبنی ہے اور GPT-5.2 کے استدلال کنٹرول، خود تشخیص، اور منظم آؤٹ پٹ کی قابلیتوں کو استعمال کرتے ہوئے **آٹھ ترقی یافتہ نمونے** پیش کرتا ہے۔

## ترقی یافتہ نمونے

بنیادی اصولوں کے احاطے کے بعد، آئیے آٹھ ایسے ترقی یافتہ نمونوں کی طرف بڑھتے ہیں جو اس ماڈیول کو منفرد بناتے ہیں۔ تمام مسائل کے حل کے لیے ایک ہی طریقہ کار ضروری نہیں۔ بعض سوالات کے فوری جواب چاہیے، بعض کو گہری سوچ درکار ہوتی ہے۔ کچھ میں واضح استدلال چاہیے، کچھ میں صرف نتائج۔ درج ذیل ہر نمونہ ایک مختلف منظرنامے کے لیے بہتر ہے — اور GPT-5.2 کا استدلال کنٹرول ان اختلافات کو اور بھی نمایاں کرتا ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="آٹھ پرامپٹ انجینئرنگ نمونے" width="800"/>

*آٹھ پرامپٹ انجینئرنگ نمونوں کا جائزہ اور ان کے استعمال کے کیسز*

<img src="../../../translated_images/ur/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 کے ساتھ استدلال کنٹرول" width="800"/>

*GPT-5.2 کا استدلال کنٹرول آپ کو ماڈل سے سوچنے کی مقدار بتانے دیتا ہے — تیز اور براہ راست جوابات سے لے کر گہری تلاش تک*

<img src="../../../translated_images/ur/reasoning-effort.db4a3ba5b8e392c1.webp" alt="استدلال کی کوشش کا موازنہ" width="800"/>

*کم جذبہ (تیز، براہ راست) بمقابلہ زیادہ جذبہ (تفصیلی، تحقیقی) استدلالی طریقے*

**کم جذبہ (تیز اور مرتکز)** - آسان سوالات کے لیے جہاں آپ تیز، براہ راست جواب چاہتے ہیں۔ ماڈل کم سے کم استدلال کرتا ہے - زیادہ سے زیادہ 2 مراحل۔ اسے حساب کتاب، تلاش، یا سیدھے سوالات کے لیے استعمال کریں۔

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
> - "کم جذبہ اور زیادہ جذبہ پرامپٹنگ نمونوں میں کیا فرق ہے؟"
> - "پرامپٹس میں XML ٹیگز AI کے جواب کو کیسے ترتیب دیتے ہیں؟"
> - "خود احتساب نمونے کب استعمال کیے جائیں اور کب براہ راست ہدایت دیں؟"

**زیادہ جذبہ (گہری اور مکمل)** - پیچیدہ مسائل کے لیے جہاں آپ جامع تجزیہ چاہتے ہیں۔ ماڈل مکمل طور پر تلاش کرتا ہے اور تفصیلی استدلال دکھاتا ہے۔ اسے سسٹم ڈیزائن، آرکیٹیکچر کے فیصلوں، یا پیچیدہ تحقیق کے لیے استعمال کریں۔

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایگزیکیوشن (قدم بہ قدم پیش رفت)** - کثیر مرحلوں کے ورک فلو کے لیے۔ ماڈل ایک ابتدائی منصوبہ فراہم کرتا ہے، ہر قدم پر کام کرتے ہوئے بیان کرتا ہے، پھر خلاصہ دیتا ہے۔ اسے مائیگریشنز، امپلیمنٹیشنز، یا کسی بھی کثیر مرحلہ عمل کے لیے استعمال کریں۔

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

چین آف تھوٹ پرامپٹنگ ماڈل سے واضح طور پر استدلالی عمل دکھانے کا کہتا ہے، جو پیچیدہ کاموں میں درستگی بہتر بناتا ہے۔ قدم بہ قدم تقسیم انسانوں اور AI دونوں کو منطق سمجھنے میں مدد دیتی ہے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** اس نمونے کے بارے میں پوچھیں:
> - "میں طویل مدتی آپریشنز کے لیے ٹاسک ایگزیکیوشن نمونہ کیسے اپناؤں؟"
> - "پروڈکشن ایپلیکیشنز میں ٹول پری ایمبلز کی ساخت کے بہترین طریقے کیا ہیں؟"
> - "UI میں درمیانی پیش رفت کی اپڈیٹس کیسے کیپچر اور ڈسپلے کی جا سکتی ہیں؟"

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="ٹاسک ایگزیکیوشن نمونہ" width="800"/>

*کئی مراحل کے کاموں کے لیے منصوبہ بنائیں → عمل کریں → خلاصہ کریں*

**خود احتساب کوڈ** - پروڈکشن معیار کے کوڈ کی تخلیق کے لیے۔ ماڈل پروڈکشن کے معیار کے مطابق کوڈ جنریٹ کرتا ہے جس میں مناسب ایرر ہینڈلنگ شامل ہو۔ جب نئے فیچرز یا سروسز بنانا ہوں تو اسے استعمال کریں۔

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="خود احتساب چکر" width="800"/>

*دہرانے والا بہتری کا لوپ - تخلیق، جائزہ، مسائل کی شناخت، بہتری، دوبارہ عمل*

**منظم تجزیہ** - مستقل جانچ کے لیے۔ ماڈل کوڈ کو ایک مقررہ فریم ورک (درستی، طریقہ کار، کارکردگی، سلامتی، برقرار رکھنے کی قابلیت) کے تحت جائزہ لیتا ہے۔ اسے کوڈ ریویوز یا معیار کی جانچ کے لیے استعمال کریں۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** منظم تجزیہ کے بارے میں پوچھیں:
> - "مختلف قسم کے کوڈ ریویوز کے لیے تجزیاتی فریم ورک کس طرح حسب ضرورت بنایا جا سکتا ہے؟"
> - "منظم آؤٹ پٹ کو پروگرام کے ذریعے کیسے پارس اور استعمال کیا جا سکتا ہے؟"
> - "مختلف ریویو سیشنز میں مسلسل شدت کی سطح کو کیسے یقینی بنایا جائے؟"

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="منظم تجزیہ نمونہ" width="800"/>

*شدت کی سطحوں کے ساتھ کوڈ ریویوز کے لیے فریم ورک*

**کئی مرحلوں کی بات چیت** - وہ بات چیت جن کے لیے سیاق و سباق ضروری ہو۔ ماڈل پچھلے پیغامات کو یاد رکھتا ہے اور ان بناء پر تعمیر کرتا ہے۔ اسے انٹرایکٹو مدد کے سیشنز یا پیچیدہ سوال و جواب کے لیے استعمال کریں۔

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ur/context-memory.dff30ad9fa78832a.webp" alt="سیاق و سباق کی یادداشت" width="800"/>

*کئی چکروں میں بات چیت کا سیاق و سباق کیسے جمع ہوتا ہے جب تک کہ ٹوکن کی حد تک پہنچ جائے*

**قدم بہ قدم استدلال** - ایسے مسائل کے لیے جن میں واضح منطق چاہیے۔ ماڈل ہر قدم کے لیے واضح استدلال دکھاتا ہے۔ اسے ریاضی کے مسائل، منطق کے معمہ، یا جب آپ کو سوچنے کے عمل کو سمجھنا ہو، استعمال کریں۔

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="قدم بہ قدم نمونہ" width="800"/>

*مسائل کو واضح منطقی مراحل میں توڑنا*

**محدود آؤٹ پٹ** - ایسے جوابات کے لیے جن کی مخصوص فارمیٹ کی ضرورت ہو۔ ماڈل سختی سے فارمیٹ اور لمبائی کے قواعد کی پیروی کرتا ہے۔ اسے خلاصوں یا جب آپ کو بالکل مخصوص آؤٹ پٹ کی ساخت چاہیے ہو، استعمال کریں۔

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

<img src="../../../translated_images/ur/constrained-output-pattern.0ce39a682a6795c2.webp" alt="محدود آؤٹ پٹ نمونہ" width="800"/>

*مخصوص فارمیٹ، لمبائی، اور ساخت کی شرائط پر عمل درآمد*

## موجودہ Azure وسائل کا استعمال

**تعییناتی تصدیق:**

یقینی بنائیں کہ `.env` فائل روٹ ڈائریکٹری میں Azure کی سندوں کے ساتھ موجود ہو (ماڈیول 01 کے دوران بنائی گئی):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT دکھانا چاہیے
```

**ایپلیکیشن شروع کریں:**

> **نوٹ:** اگر آپ نے ماڈیول 01 سے `./start-all.sh` استعمال کرتے ہوئے تمام ایپلیکیشنز پہلے ہی شروع کر رکھی ہیں، تو یہ ماڈیول پہلے سے ہی پورٹ 8083 پر چل رہا ہوگا۔ آپ نیچے دیے گئے شروع کرنے والے کمانڈز کو چھوڑ کر براہ راست http://localhost:8083 پر جا سکتے ہیں۔

**آپشن 1: اسپِرنگ بوٹ ڈیش بورڈ کا استعمال (VS Code صارفین کے لیے تجویز کردہ)**

ڈویلپمنٹ کنٹینر میں Spring Boot Dashboard ایکسٹینشن شامل ہے، جو تمام Spring Boot ایپلیکیشنز کو منظم کرنے کے لیے بصری انٹرفیس فراہم کرتا ہے۔ آپ اسے VS Code کے بائیں جانب ایکٹویٹی بار میں (Spring Boot آئیکن دیکھیں) تلاش کر سکتے ہیں۔
Spring Boot ڈیش بورڈ سے، آپ کر سکتے ہیں:
- ورک اسپیس میں دستیاب تمام Spring Boot ایپلیکیشنز دیکھیں
- ایک کلک سے ایپلیکیشنز کو شروع/روکیں
- ایپلیکیشن کے لاگز کو حقیقی وقت میں دیکھیں
- ایپلیکیشن کی حالت کی نگرانی کریں

بس "prompt-engineering" کے ساتھ پلے بٹن پر کلک کریں اس ماڈیول کو شروع کرنے کے لیے، یا تمام ماڈیولز کو ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**اختیار 2: شیل اسکرپٹس کا استعمال**

تمام ویب ایپلیکیشنز (ماڈیولز 01-04) شروع کریں:

**Bash:**
```bash
cd ..  # جڑ ڈائریکٹری سے
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # روٹ ڈائریکٹری سے
.\start-all.ps1
```

یا صرف اس ماڈیول کو شروع کریں:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

دونوں اسکرپٹس خود بخود root `.env` فائل سے ماحولیاتی متغیرات لوڈ کرتے ہیں اور اگر JAR موجود نہیں ہیں تو انہیں بنائیں گے۔

> **نوٹ:** اگر آپ شروع کرنے سے پہلے تمام ماڈیولز کو دستی طور پر بنانا چاہتے ہیں:
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

اپنے براؤزر میں http://localhost:8083 کھولیں۔

**روکنے کے لیے:**

**Bash:**
```bash
./stop.sh  # صرف یہ ماڈیول
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**PowerShell:**
```powershell
.\stop.ps1  # یہ ماڈیول صرف
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## ایپلیکیشن اسکرین شاٹس

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*مین ڈیش بورڈ جس میں آٹھوں پرامپٹ انجینئرنگ پیٹرنز اور ان کی خصوصیات و استعمال کے کیسز دکھائے گئے ہیں*

## پیٹرنز کی تلاش

ویب انٹرفیس آپ کو مختلف پرامپٹنگ حکمت عملیاں آزمانے دیتا ہے۔ ہر پیٹرن مختلف مسائل حل کرتا ہے - انہیں آزما کر دیکھیں کہ کب کون سا طریقہ کار مؤثر ہے۔

### کم مقابلہ بمقابلہ زیادہ مقابلہ

سادی سی سوال پوچھیں جیسے "200 کا 15٪ کیا ہے؟" کم مقابلے کے ساتھ۔ آپ کو فوری اور براہِ راست جواب ملے گا۔ اب کوئی پیچیدہ سوال پوچھیں جیسے "ہائی ٹریفک API کے لیے کیشنگ کی حکمت عملی ڈیزائن کریں" زیادہ مقابلے کے ساتھ۔ دیکھیں کہ ماڈل کس طرح سست ہو کر مفصل منطق فراہم کرتا ہے۔ ایک ہی ماڈل، ایک ہی سوال کا ڈھانچہ - مگر پرامپٹ اسے بتاتا ہے کہ کتنی سوچ بچار کرنی ہے۔

<img src="../../../translated_images/ur/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*کم منطق کے ساتھ فوری حساب*

<img src="../../../translated_images/ur/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*تفصیلی کیشنگ حکمت عملی (2.8MB)*

### ٹاسک کی تکمیل (ٹول پریایمبلز)

کئی مراحل پر مشتمل ورک فلو کو ابتدائی منصوبہ بندی اور پیش رفت کی تشریح سے فائدہ ہوتا ہے۔ ماڈل بیان کرتا ہے کہ کیا کرے گا، ہر مرحلے کی تفصیل دیتا ہے، پھر نتائج کا خلاصہ پیش کرتا ہے۔

<img src="../../../translated_images/ur/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*مرحلہ وار وضاحت کے ساتھ REST اینڈپوائنٹ بنانا (3.9MB)*

### خود تجزیہ کرنے والا کوڈ

آزمائیں "ایک ای میل ویلیڈیشن سروس بنائیں"۔ صرف کوڈ پیدا کرنے اور روکنے کی بجائے، ماڈل کوڈ تیار کرتا ہے، معیار کے خلاف جائزہ لیتا ہے، نقائص کی نشاندہی کرتا ہے، اور بہتری لاتا ہے۔ آپ دیکھیں گے کہ یہ تب تک دہرائے گا جب تک کوڈ پروڈکشن معیار پر پورا نہ اترے۔

<img src="../../../translated_images/ur/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*مکمل ای میل ویلیڈیشن سروس (5.2MB)*

### منظم تجزیہ

کوڈ کے جائزے کے لیے مستقل جانچ کے فریم ورک کی ضرورت ہوتی ہے۔ ماڈل کوڈ کو مخصوص زمروں میں (صحیحیت، طریقہ کار، کارکردگی، حفاظتی پہلو) شدت کی سطح کے ساتھ تجزیہ کرتا ہے۔

<img src="../../../translated_images/ur/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*فریم ورک پر مبنی کوڈ جائزہ*

### کثیر دوری چیٹ

پوچھیں "Spring Boot کیا ہے؟" پھر فوراً "کوئی مثال دکھائیں" پوچھیں۔ ماڈل آپ کا پہلا سوال یاد رکھتا ہے اور آپ کو خاص طور پر Spring Boot کی مثال دیتا ہے۔ یادداشت کے بغیر، دوسرا سوال بہت مبہم ہوتا۔

<img src="../../../translated_images/ur/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*سوالات کے درمیان سیاق و سباق کی حفاظت*

### قدم بہ قدم منطق

کسی ریاضی مسئلے کا انتخاب کریں اور اسے دونوں طریقوں سے آزمائیں: قدم بہ قدم منطق اور کم مقابلہ۔ کم مقابلہ صرف جواب دیتا ہے - تیز مگر مبہم۔ قدم بہ قدم ہر حساب اور فیصلہ دکھاتا ہے۔

<img src="../../../translated_images/ur/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*واضح مراحل کے ساتھ ریاضی کا مسئلہ*

### محدود آؤٹ پٹ

جب آپ کو مخصوص فارمیٹس یا الفاظ کی تعداد کی ضرورت ہو، یہ پیٹرن سختی سے پابندی لگاتا ہے۔ کوشش کریں کہ بالکل 100 الفاظ میں بلٹ پوائنٹ کے ساتھ خلاصہ تیار کریں۔

<img src="../../../translated_images/ur/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*مشین لرننگ کا خلاصہ فارمیٹ کنٹرول کے ساتھ*

## آپ واقعی کیا سیکھ رہے ہیں

**منطق کی کوشش سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنی پرامپٹس کے ذریعے حسابی کوشش کو کنٹرول کرنے دیتا ہے۔ کم کوشش کا مطلب ہے تیز جواب کم کھوج کے ساتھ۔ زیادہ کوشش سے ماڈل گہری سوچ میں وقت لگاتا ہے۔ آپ سیکھ رہے ہیں کہ کوشش کو کام کی پیچیدگی کے مطابق ملائیں - سادہ سوالات پر وقت ضائع نہ کریں، مگر پیچیدہ فیصلوں میں جلد بازی نہ کریں۔

**ساخت رویے کی رہنمائی کرتی ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دیکھے؟ یہ صرف سجاوٹ نہیں ہیں۔ ماڈلز منظم ہدایات کی آزاد متن کی نسبت بہتر پیروی کرتے ہیں۔ جب آپ کو کئی مراحل یا پیچیدہ منطق کی ضرورت ہو، ساخت ماڈل کو بتاتی ہے کہ وہ کہاں ہے اور آگے کیا کرنا ہے۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*صفحات اور XML طرز کی تنظیم کے ساتھ اچھی ساخت والے پرامپٹ کی مثال*

**خود جائزہ کے ذریعے معیار**

خود تجزیہ کرنے والے پیٹرنز معیار کے معیار کو واضح کرتے ہیں۔ ماڈل سے صرف یہ امید کرنے کے بجائے کہ "یہ صحیح کرے گا"، آپ اسے بتاتے ہیں کہ "صحیح" کا کیا مطلب ہے: درست منطقی، غلطی سنبھالنا، کارکردگی، سلامتی۔ ماڈل اپنی پیداوار کا جائزہ لے کر بہتری لا سکتا ہے۔ یہ کوڈ جنریشن کو لاٹری سے ایک عمل میں بدل دیتا ہے۔

**سیاق و سباق محدود ہے**

کثیر دوری مکالمے ہر درخواست کے ساتھ پیغام کی تاریخ شامل کرکے کام کرتے ہیں۔ لیکن حد ہے - ہر ماڈل کی زیادہ سے زیادہ ٹوکن تعداد ہوتی ہے۔ جیسے جیسے بات چیت بڑھتی ہے، آپ کو حکمت عملی چاہیے کہ کس طرح متعلقہ سیاق کیسے رکھا جائے بغیر حد کو پار کیے۔ یہ ماڈیول آپ کو دکھاتا ہے کہ یادداشت کیسے کام کرتی ہے؛ بعد میں آپ سیکھیں گے کب خلاصہ کرنا ہے، کب بھولنا ہے، اور کب بازیافت کرنا ہے۔

## اگلے اقدامات

**اگلا ماڈیول:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**نیویگیشن:** [← پچھلا: ماڈیول 01 - تعارف](../01-introduction/README.md) | [مین پر واپس](../README.md) | [اگلا: ماڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تعارفی دستبرداری**:  
یہ دستاویز اے آئی ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لئے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار تراجم میں غلطیاں یا غیر درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنی مادری زبان میں معتبر ماخذ سمجھا جانا چاہیے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->