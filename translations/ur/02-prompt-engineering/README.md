# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینیئرنگ

## فہرست مطالبات

- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [پری ریکوزٹس](../../../02-prompt-engineering)
- [پرامپٹ انجینیئرنگ کی سمجھ](../../../02-prompt-engineering)
- [پرامپٹ انجینیئرنگ کے اصول](../../../02-prompt-engineering)
  - [زیرو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [فیو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [چین آف تھوٹ](../../../02-prompt-engineering)
  - [رول بیسڈ پرامپٹنگ](../../../02-prompt-engineering)
  - [پرامپٹ ٹیمپلٹس](../../../02-prompt-engineering)
- [جدید پیٹرنز](../../../02-prompt-engineering)
- [موجودہ Azure وسائل کا استعمال](../../../02-prompt-engineering)
- [ایپلیکیشن کے اسکرین شاٹس](../../../02-prompt-engineering)
- [پیٹرنز کی کھوج](../../../02-prompt-engineering)
  - [کم اور زیادہ جوش](../../../02-prompt-engineering)
  - [ٹاسک ایگزیکیوشن (ٹول پری ایمبلز)](../../../02-prompt-engineering)
  - [سیلف ریفلیکٹنگ کوڈ](../../../02-prompt-engineering)
  - [مربوط تجزیہ](../../../02-prompt-engineering)
  - [کئی مرحلوں کی چیٹ](../../../02-prompt-engineering)
  - [قدم بہ قدم استدلال](../../../02-prompt-engineering)
  - [محدود آؤٹ پٹ](../../../02-prompt-engineering)
- [آپ واقعی کیا سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے اقدامات](../../../02-prompt-engineering)

## آپ کیا سیکھیں گے

<img src="../../../translated_images/ur/what-youll-learn.c68269ac048503b2.webp" alt="آپ کیا سیکھیں گے" width="800"/>

پچھلے ماڈیول میں، آپ نے دیکھا کہ میموری کس طرح مکالماتی AI کو ممکن بناتی ہے اور GitHub ماڈلز کو بنیادی تعاملات کے لیے استعمال کیا۔ اب ہم اس بات پر توجہ دیں گے کہ آپ سوالات کیسے پوچھتے ہیں — یعنی خود پرامپٹس — Azure OpenAI کے GPT-5.2 کا استعمال کرتے ہوئے۔ آپ کے پرامپٹ کی ساخت بالکل جواب کی معیار کو متاثر کرتی ہے۔ ہم بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہیں، پھر آگے بڑھ کر آٹھ جدید پیٹرنز کو دیکھتے ہیں جو GPT-5.2 کی صلاحیتوں کا مکمل فائدہ اٹھاتے ہیں۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلالی کنٹرول متعارف کراتا ہے - آپ ماڈل کو بتا سکتے ہیں کہ جواب دینے سے پہلے کتنا سوچنا ہے۔ اس سے مختلف پرامپٹنگ حکمت عملیاں واضح ہوتی ہیں اور آپ کو سمجھنے میں مدد ملتی ہے کہ ہر طریقہ کب استعمال کرنا ہے۔ Azure کی جانب سے GPT-5.2 کے لیے کم ریٹ لمٹس بھی ہمیں GitHub ماڈلز کے مقابلے میں فائدہ دیتی ہیں۔

## پری ریکوزٹس

- ماڈیول 01 مکمل کیا ہوا ہو (Azure OpenAI وسائل تعینات کیے ہوئے)
- روٹ ڈائریکٹری میں `.env` فائل موجود ہو جس میں Azure کی اسناد ہوں (ماڈیول 01 میں `azd up` کے ذریعہ بنائی گئی)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا، تو وہاں دی گئی تعیناتی کی ہدایات پہلے مکمل کریں۔

## پرامپٹ انجینیئرنگ کی سمجھ

<img src="../../../translated_images/ur/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="پرامپٹ انجینیئرنگ کیا ہے؟" width="800"/>

پرامپٹ انجینیئرنگ اس بارے میں ہے کہ ایسے ان پٹ ٹیکسٹ کو ڈیزائن کرنا جو مستقل طور پر آپ کو مطلوبہ نتائج فراہم کرے۔ یہ صرف سوالات کرنے کے بارے میں نہیں ہے - بلکہ درخواستوں کی ایسی ساخت دینے کے بارے میں ہے کہ ماڈل بخوبی سمجھے کہ آپ کیا چاہتے ہیں اور اسے کیسے فراہم کرنا ہے۔

اسے ایک ساتھی کو ہدایات دینے کی مانند سمجھیں۔ "بگ درست کرو" مبہم ہے۔ "UserService.java کی لائن 45 میں نل پوائنٹر استثناء کو نل چیک شامل کرکے درست کرو" مخصوص ہے۔ زبان کے ماڈلز بھی ایسے ہی کام کرتے ہیں — وضاحت اور ساخت اہمیت رکھتی ہے۔

<img src="../../../translated_images/ur/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j کیسے فٹ بیٹھتا ہے" width="800"/>

LangChain4j وہ بنیادی ڈھانچہ فراہم کرتا ہے — ماڈل کنکشنز، میموری، اور میسج کی اقسام — جبکہ پرامپٹ پیٹرنز صرف محتاط ساخت والے متن ہوتے ہیں جو اس بنیادی ڈھانچے کے ذریعے بھیجے جاتے ہیں۔ اہم عمارت کے بلاکس `SystemMessage` (جو AI کے رویے اور کردار کو سیٹ کرتا ہے) اور `UserMessage` (جو آپ کی اصل درخواست لے کر آتا ہے) ہیں۔

## پرامپٹ انجینیئرنگ کے اصول

<img src="../../../translated_images/ur/five-patterns-overview.160f35045ffd2a94.webp" alt="پرامپٹ انجینیئرنگ کے پانچ پیٹرنز کا جائزہ" width="800"/>

اس ماڈیول میں جدید پیٹرنز میں غوطہ لگانے سے پہلے، آئیے پانچ بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہیں۔ یہ وہ بنیادی عمارت کے بلاکس ہیں جنہیں ہر پرامپٹ انجینیئر کو جاننا چاہیے۔ اگر آپ پہلے ہی [Quick Start ماڈیول](../00-quick-start/README.md#2-prompt-patterns) کر چکے ہیں، تو آپ نے انہیں فعال طور پر دیکھا ہے — یہاں ان کے تصوراتی فریم ورک کی وضاحت ہے۔

### زیرو شاٹ پرامپٹنگ

سب سے آسان طریقہ: ماڈل کو سیدھی ہدایت دیں بغیر کسی مثال کے۔ ماڈل مکمل طور پر اپنی تربیت پر انحصار کرتا ہے کہ وہ کام کو سمجھے اور انجام دے۔ یہ آسان درخواستوں کے لیے بہترین ہے جہاں متوقع رویہ واضح ہو۔

<img src="../../../translated_images/ur/zero-shot-prompting.7abc24228be84e6c.webp" alt="زیرو شاٹ پرامپٹنگ" width="800"/>

*مثال کے بغیر براہ راست ہدایت — ماڈل صرف ہدایت سے کام کا اندازہ لگاتا ہے*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// جواب: "مثبت"
```

**استعمال کب کریں:** سادہ درجہ بندی، سیدھے سوالات، تراجم، یا کسی بھی کام کے لیے جسے ماڈل اضافی رہنمائی کے بغیر سنبھال سکتا ہو۔

### فیو شاٹ پرامپٹنگ

مثالیں فراہم کریں جو وہ انداز ماڈل پر عمل کرے۔ ماڈل آپ کی مثالوں سے متوقع ان پٹ آؤٹ پٹ شکل کو سیکھتا ہے اور نئے ان پٹ پر لاگو کرتا ہے۔ یہ خاص طور پر ان کاموں کے لیے بہترین ہے جہاں مطلوبہ فارمیٹ یا رویہ واضح نہیں ہوتا۔

<img src="../../../translated_images/ur/few-shot-prompting.9d9eace1da88989a.webp" alt="فیو شاٹ پرامپٹنگ" width="800"/>

*مثالوں سے سیکھنا — ماڈل پیٹرن کو پہچان کر نئے ان پٹ پر لاگو کرتا ہے*

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

**استعمال کب کریں:** حسب ضرورت درجہ بندی، مستقل فارمیٹنگ، مخصوص دائرہ کار کے کام، یا جب زیرو شاٹ نتائج غیر مستقل ہوں۔

### چین آف تھوٹ

ماڈل سے کہیں کہ وہ اپنا استدلال قدم بہ قدم دکھائے۔ سیدھا جواب دینے کے بجائے، ماڈل مسئلہ کو حصوں میں توڑ کر واضح طریقے سے حل کرتا ہے۔ یہ ریاضی، منطق، اور کثیر مرحلہ استدلال کے کاموں میں درستگی بڑھاتا ہے۔

<img src="../../../translated_images/ur/chain-of-thought.5cff6630e2657e2a.webp" alt="چین آف تھوٹ پرامپٹنگ" width="800"/>

*قدم بہ قدم دلیل — پیچیدہ مسائل کو واضح منطقی مراحل میں توڑنا*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ماڈل دکھاتا ہے: ۱۵ - ۸ = ۷، پھر ۷ + ۱۲ = ۱۹ سیب
```

**استعمال کب کریں:** ریاضی کے مسائل، منطقی پہیلیاں، ڈیبگنگ، یا کسی بھی کام کے لیے جہاں استدلال کا مظاہرہ درستگی اور اعتماد بڑھاتا ہو۔

### رول بیسڈ پرامپٹنگ

AI کے لیے کسی کردار یا شخصیت کو سیٹ کریں سوال کرنے سے پہلے۔ یہ وہ سیاق و سباق فراہم کرتا ہے جو جواب کے لہجے، گہرائی، اور توجہ کو تشکیل دیتا ہے۔ "سافٹ ویئر آرکیٹیکٹ" مختلف مشورے دیتا ہے "جونئیر ڈویلپر" یا "سیکیورٹی آڈیٹر" کے مقابلے میں۔

<img src="../../../translated_images/ur/role-based-prompting.a806e1a73de6e3a4.webp" alt="رول بیسڈ پرامپٹنگ" width="800"/>

*سیاق و سباق اور کردار کا تعین — اسی سوال کا مختلف کردار کے مطابق مختلف جواب آتا ہے*

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

**استعمال کب کریں:** کوڈ ریویوز، ٹیوشن، مخصوص دائرہ کار کے تجزیات، یا جب آپ کو کسی خاص مہارت کی سطح یا نقطہ نظر کے مطابق جوابات چاہیے ہوں۔

### پرامپٹ ٹیمپلٹس

قابل استعمال بار بار آنے والے پرامپٹس بنائیں جن میں متغیر جگہیں ہوں۔ ہر بار نیا پرامپٹ لکھنے کے بجائے، ایک ٹیمپلیٹ بنائیں اور مختلف قدریں بھریں۔ LangChain4j کی `PromptTemplate` کلاس `{{variable}}` نحو کے ساتھ یہ آسان بناتی ہے۔

<img src="../../../translated_images/ur/prompt-templates.14bfc37d45f1a933.webp" alt="پرامپٹ ٹیمپلٹس" width="800"/>

*متغیر جگہوں کے ساتھ قابل استعمال پرامپٹس — ایک ٹیمپلیٹ، متعدد استعمال*

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

**استعمال کب کریں:** مختلف ان پٹ کے ساتھ بار بار سوالات، بیچ پروسیسنگ، قابل استعمال AI ورک فلو بنانے کے لیے، یا کسی بھی منظر نامے میں جہاں پرامپٹ کی ساخت ایک جیسی رہے مگر ڈیٹا مختلف ہو۔

---

یہ پانچ بنیادی اصول زیادہ تر پرامپٹنگ کاموں کے لیے آپ کو ایک مضبوط ٹول کٹ دیتے ہیں۔ اس ماڈیول کا باقی حصہ ان پر مبنی ہے اور اُن کے ساتھ **آٹھ جدید پیٹرنز** شامل ہیں جو GPT-5.2 کے استدلالی کنٹرول، خود تشخیص، اور مربوط آؤٹ پٹ صلاحیتوں کا فائدہ اٹھاتے ہیں۔

## جدید پیٹرنز

بنیادی اصولوں کی کوریج کے بعد، آئیے آٹھ جدید پیٹرنز کا جائزہ لیں جو اس ماڈیول کو منفرد بناتے ہیں۔ تمام مسائل کو ایک جیسا طریقہ نہیں چاہیے۔ کچھ سوالات کو فوری جوابات چاہیے، کچھ کو گہری سوچ۔ کچھ کو مرئی استدلال چاہیے، کچھ صرف نتائج۔ ہر ذیل میں دیا گیا پیٹرن ایک مختلف منظر نامے کے لیے بہتر بنایا گیا ہے — اور GPT-5.2 کا استدلالی کنٹرول ان فرق کو مزید واضح بناتا ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="آٹھ پرامپٹنگ پیٹرنز" width="800"/>

*آٹھ پرامپٹ انجینیئرنگ پیٹرنز اور ان کے استعمال کے کیسز کا جائزہ*

<img src="../../../translated_images/ur/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 کے ساتھ استدلالی کنٹرول" width="800"/>

*GPT-5.2 کا استدلالی کنٹرول آپ کو بتانے دیتا ہے کہ ماڈل کو کتنا سوچنا چاہیے — تیز اور سیدھے جواب سے لے کر گہرے جائزے تک*

**کم جوش (تیز اور مرکوز)** - آسان سوالات کے لیے جہاں آپ فوری، سیدھے جواب چاہتے ہیں۔ ماڈل کم از کم استدلال کرتا ہے — زیادہ سے زیادہ 2 مراحل۔ حساب کتاب، تلاش یا سیدھے سوالات کے لیے استعمال کریں۔

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
> - "میں خود تفکر پیٹرنز کب استعمال کروں بمقابلہ براہ راست ہدایت؟"

**زیادہ جوش (گہرا اور مکمل)** - پیچیدہ مسائل کے لیے جہاں آپ مفصل تجزیہ چاہتے ہیں۔ ماڈل مکمل طور پر جائزہ لیتا ہے اور مفصل استدلال دکھاتا ہے۔ نظام کی ڈیزائننگ، آرکیٹیکچر فیصلے یا پیچیدہ تحقیق کے لیے استعمال کریں۔

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایگزیکیوشن (قدم بہ قدم پیش رفت)** - کثیر مرحلہ ورک فلو کے لیے۔ ماڈل پہلے ایک منصوبہ دیتا ہے، ہر قدم کو بیاں کرتا ہے، پھر خلاصہ پیش کرتا ہے۔ مائیگریشنز، نفاذ یا کوئی بھی کثیر مرحلہ عمل کے لیے استعمال کریں۔

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

چین آف تھوٹ پرامپٹنگ ماڈل سے واضح طور پر کہتی ہے کہ وہ اپنے استدلال کے عمل کو دکھائے، جس سے پیچیدہ کاموں کی درستگی بڑھتی ہے۔ قدم بہ قدم تجزیہ انسانوں اور AI دونوں کو منطق سمجھنے میں مدد دیتا ہے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** اس پیٹرن کے بارے میں پوچھیں:
> - "لمبے عمل کے لیے ٹاسک ایگزیکیوشن پیٹرن کو کیسے اپناؤں؟"
> - "پروڈکشن ایپلیکیشنز میں ٹول پری ایمبلز کی ساخت کے بہترین طریقے کیا ہیں؟"
> - "UI میں درمیانی پیش رفت کی معلومات کیسے حاصل اور دکھا سکتا ہوں؟"

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="ٹاسک ایگزیکیوشن پیٹرن" width="800"/>

*ملٹی اسٹیپ کاموں کے لیے منصوبہ بندی → عمل درآمد → خلاصہ ورک فلو*

**سیلف ریفلیکٹنگ کوڈ** - پیداواری معیار کا کوڈ بنانے کے لیے۔ ماڈل پیداواری معیارات کے مطابق کوڈ پیدا کرتا ہے جس میں مناسب ایرر ہینڈلنگ شامل ہوتی ہے۔ نئے فیچرز یا سروسز بنانے کے لیے استعمال کریں۔

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="خود تفکر کا چکر" width="800"/>

*تکراری بہتری کا لوپ - جنریٹ کریں، جائزہ لیں، مسائل کی شناخت کریں، بہتری لائیں، دہرائیں*

**مربوط تجزیہ** - مستقل جائزہ کے لیے۔ ماڈل کوڈ کا جائزہ ایک مقررہ فریم ورک (صحیحیت، طریقے، کارکردگی، سیکیورٹی، برقرار رکھنے کی قابلیت) کے تحت لیتا ہے۔ کوڈ ریویوز یا معیار کی جانچ کے لیے استعمال کریں۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** مربوط تجزیہ کے بارے میں پوچھیں:
> - "مختلف قسم کے کوڈ ریویوز کے لیے تجزیہ فریم ورک کو کیسے حسب ضرورت بناؤں؟"
> - "مربوط آؤٹ پٹ کو پروگرامنگ طریقے سے پارس اور عمل میں لانے کا بہترین طریقہ کیا ہے؟"
> - "مختلف ریویو سیشنز میں مستقل شدت کے درجات کو کیسے یقینی بناؤں؟"

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="مربوط تجزیہ پیٹرن" width="800"/>

*شدت کے درجات کے ساتھ مستقل کوڈ ریویوز کے لیے فریم ورک*

**کئی مرحلوں کی چیٹ** - ایسی گفتگو کے لیے جسے سیاق و سباق کی ضرورت ہو۔ ماڈل پچھلے پیغامات کو یاد رکھتا ہے اور ان پر تعمیر کرتا ہے۔ انٹرایکٹو مدد سیشن یا پیچیدہ سوالات و جوابات کے لیے استعمال کریں۔

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ur/context-memory.dff30ad9fa78832a.webp" alt="سیاق و سباق کی میموری" width="800"/>

*کس طرح گفتگو کا سیاق و سباق متعدد مرحلوں میں جمع ہوتا ہے جب تک کہ ٹوکن کی حد تک نہ پہنچ جائے*

**قدم بہ قدم استدلال** - ایسے مسائل کے لیے جنہیں واضح منطق چاہیے۔ ماڈل ہر قدم کے لیے واضح استدلال دکھاتا ہے۔ ریاضی کے مسائل، منطقی پہیلیوں، یا جب آپ کو سوچنے کے عمل کو سمجھنا ہو اس کے لیے استعمال کریں۔

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="قدم بہ قدم پیٹرن" width="800"/>

*مسائل کو واضح منطقی مراحل میں توڑنا*

**محدود آؤٹ پٹ** - ایسے جوابات کے لیے جن میں مخصوص فارمیٹ کی ضروت ہو۔ ماڈل سختی سے فارمیٹ اور لمبائی کے قواعد کی پیروی کرتا ہے۔ خلاصوں کے لیے یا جب آپ کو درست ساخت والا آؤٹ پٹ چاہیے ہو استعمال کریں۔

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

*مخصوص فارمیٹ، لمبائی، اور ساخت کی ضروریات کا نفاذ*

## موجودہ Azure وسائل کا استعمال

**تصدیق کریں تعیناتی:**

روٹ ڈائریکٹری میں `.env` فائل یقینی بنائیں جس میں Azure کی اسناد موجود ہوں (جو ماڈیول 01 کے دوران بنائی گئی):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT دکھانا چاہیے
```

**ایپلیکیشن شروع کریں:**

> **نوٹ:** اگر آپ نے ماڈیول 01 سے `./start-all.sh` کے ذریعے تمام ایپلیکیشنز پہلے ہی شروع کر دی ہیں، تو یہ ماڈیول پہلے ہی پورٹ 8083 پر چل رہا ہے۔ آپ نیچے دیے گئے شروع کرنے کے کمانڈز کو چھوڑ کر براہ راست http://localhost:8083 پر جا سکتے ہیں۔

**اختیار 1: Spring Boot ڈیش بورڈ استعمال کرنا (VS Code صارفین کے لیے تجویز کردہ)**

ڈیولپمنٹ کنٹینر میں Spring Boot ڈیش بورڈ ایکسٹینشن شامل ہے، جو تمام Spring Boot ایپلیکیشنز کو منظم کرنے کے لیے ایک بصری انٹرفیس مہیا کرتا ہے۔ آپ اسے VS Code کے بائیں جانب Activity Bar میں Spring Boot آئیکن کے طور پر دیکھ سکتے ہیں۔

Spring Boot ڈیش بورڈ سے آپ:
- ورک اسپیس میں موجود تمام Spring Boot ایپلیکیشنز دیکھ سکتے ہیں
- ایک کلک سے ایپلیکیشنز شروع یا بند کر سکتے ہیں
- حقیقی وقت میں ایپلیکیشن لاگز دیکھ سکتے ہیں
- ایپلیکیشن کی حالت کی نگرانی کر سکتے ہیں
بس "prompt-engineering" کے ساتھ پلے بٹن پر کلک کریں تاکہ یہ ماڈیول شروع ہو جائے، یا تمام ماڈیولز کو ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**آپشن 2: شیل اسکرپٹس کا استعمال**

تمام ویب ایپلیکیشنز (ماڈیول 01-04) شروع کریں:

**بش:**
```bash
cd ..  # جڑ ڈائریکٹری سے
./start-all.sh
```

**پاور شیل:**
```powershell
cd ..  # جڑ ڈائریکٹری سے
.\start-all.ps1
```

یا صرف اس ماڈیول کو شروع کریں:

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

دونوں اسکرپٹس خود بخود روٹ `.env` فائل سے ماحول کے متغیرات کو لوڈ کرتے ہیں اور اگر JARs موجود نہ ہوں تو انہیں بنائیں گے۔

> **نوٹ:** اگر آپ شروع کرنے سے پہلے تمام ماڈیولز کو دستی طور پر بنانا پسند کرتے ہیں:
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

**رکنے کے لیے:**

**بش:**
```bash
./stop.sh  # یہ ماڈیول صرف
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**پاور شیل:**
```powershell
.\stop.ps1  # یہ ماڈیول ہی
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## ایپلیکیشن کے اسکرین شاٹس

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*مین ڈیش بورڈ جو آٹھوں پرامپٹ انجینیئرنگ پیٹرنز کو ان کی خصوصیات اور استعمال کے کیسز کے ساتھ دکھاتا ہے*

## پیٹرنز کو دریافت کرنا

ویب انٹرفیس آپ کو مختلف پرامپٹنگ حکمت عملیوں کے ساتھ تجربہ کرنے دیتا ہے۔ ہر پیٹرن مختلف مسائل کو حل کرتا ہے - انہیں آزمائیں تاکہ آپ دیکھ سکیں کہ کون سا طریقہ کب بہترین کام کرتا ہے۔

> **نوٹ: سٹریمنگ بمقابلہ نان-سٹریمنگ** — ہر پیٹرن پیج میں دو بٹن ہوتے ہیں: **🔴 سٹریمنگ رسپانس (لائیو)** اور ایک **نان سٹریمنگ** آپشن۔ سٹریمنگ سرور-سینٹ ایونٹس (SSE) کا استعمال کرتا ہے تاکہ ماڈل جنریٹ کرتے ہوئے ٹوکنز کو حقیقی وقت میں ظاہر کرے، اس لیے آپ فوری پیش رفت دیکھ سکتے ہیں۔ نان سٹریمنگ آپشن پورا جواب آنے تک انتظار کرتا ہے۔ ایسے پرامپٹس کے لیے جو گہرے استدلال کو متحرک کرتے ہیں (مثلاً، ہائی ایگرنس، سیلف-ریفلیکٹنگ کوڈ)، نان سٹریمنگ کال بہت زیادہ وقت لے سکتی ہے — کبھی کبھی منٹوں تک — اور کسی قسم کی نظر آنے والی فیڈبیک نہیں ہوتی۔ **پیچیدہ پرامپٹس کے ساتھ تجربہ کرتے ہوئے سٹریمنگ کا استعمال کریں** تاکہ آپ ماڈل کی ورکنگ دیکھ سکیں اور یہ تاثر نہ ہو کہ درخواست کا وقت ختم ہو گیا ہے۔
>
> **نوٹ: براؤزر کی ضرورت** — سٹریمنگ فیچر Fetch Streams API (`response.body.getReader()`) استعمال کرتا ہے جو مکمل براؤزر (Chrome, Edge, Firefox, Safari) میں کام کرتا ہے۔ یہ VS Code کے بلٹ ان سمپل براؤزر میں کام نہیں کرتا کیونکہ اس کا ویب ویو ReadableStream API کو سپورٹ نہیں کرتا۔ اگر آپ سمپل براؤزر استعمال کرتے ہیں تو نان سٹریمنگ بٹنز عام طور پر کام کریں گے — صرف سٹریمنگ بٹنز متاثر ہوتے ہیں۔ مکمل تجربے کے لیے `http://localhost:8083` کو ایک بیرونی براؤزر میں کھولیں۔

### کم بمقابلہ زیادہ ایگرنس

ایک آسان سوال پوچھیں جیسے "15% of 200 کیا ہے؟" کم ایگرنس استعمال کرتے ہوئے۔ آپ کو فوری، سیدھا جواب ملے گا۔ اب ایک پیچیدہ سوال پوچھیں جیسے "ہائی-ٹریفک API کے لیے کیشنگ حکمت عملی ڈیزائن کریں" زیادہ ایگرنس استعمال کرتے ہوئے۔ **🔴 سٹریمنگ رسپانس (لائیو)** پر کلک کریں اور ماڈل کی تفصیلی استدلال کو ٹوکن بہ ٹوکن دیکھیں۔ یہی ماڈل، وہی سوال کا ڈھانچہ - مگر پرامپٹ ماڈل کو بتاتا ہے کہ کتنی گہرائی سے سوچنا ہے۔

### ٹاسک کے نفاذ (ٹول پری ایمبلز)

کئی اقدامات والے ورک فلو کو پہلے سے منصوبہ بندی اور پیش رفت کے بیان کی ضرورت ہوتی ہے۔ ماڈل بتاتا ہے کہ کیا کرے گا، ہر مرحلے کی وضاحت کرتا ہے، پھر نتائج کا خلاصہ پیش کرتا ہے۔

### سیلف-ریفلیکٹنگ کوڈ

آزمائیں "ایک ای میل ویلیڈیشن سروس بنائیں"۔ صرف کوڈ جنریٹ کرنے اور روکنے کے بجائے، ماڈل کوڈ بناتا ہے، معیار کے خلاف اس کا جائزہ لیتا ہے، کمزوریوں کی نشاندہی کرتا ہے، اور بہتر بناتا ہے۔ آپ دیکھیں گے کہ یہ تب تک دہرائے گا جب تک کوڈ پروڈکشن معیار پر نہ پہنچ جائے۔

### منظم تجزیہ

کوڈ ریویوز کیلئے مستقل تشخیصی فریم ورک کی ضرورت ہوتی ہے۔ ماڈل کوڈ کا تجزیہ طے شدہ زمروں (درستی، طریقے، کارکردگی، سیکیورٹی) اور شدت کی سطحوں کے ساتھ کرتا ہے۔

### ملٹی-ٹرن چیٹ

پوچھیں "Spring Boot کیا ہے؟" پھر فوراً فالو اپ میں پوچھیں "مجھے ایک مثال دکھائیں"۔ ماڈل آپ کے پہلے سوال کو یاد رکھتا ہے اور خاص طور پر Spring Boot کی مثال دیتا ہے۔ بغیر میموری کے، دوسرا سوال بہت مبہم ہوتا۔

### مرحلہ وار استدلال

کسی ریاضی کے مسئلے کو منتخب کریں اور اسے دونوں طریقوں سے آزمائیں: مرحلہ وار استدلال اور کم ایگرنس۔ کم ایگرنس آپ کو صرف جواب دیتا ہے — تیز مگر غیر واضح۔ مرحلہ وار آپ کو ہر حساب کتاب اور فیصلے کو دکھاتا ہے۔

### محدود آؤٹ پٹ

جب آپ کو مخصوص فارمیٹس یا لفظوں کی تعداد کی ضرورت ہو، یہ پیٹرن سختی سے اس کی پابندی کرتا ہے۔ بال پوائنٹ فارمیٹ میں بالکل 100 الفاظ کا خلاصہ بنانے کی کوشش کریں۔

## آپ حقیقت میں کیا سیکھ رہے ہیں

**استدلال کی محنت سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنے پرامپٹس کے ذریعے حسابی کوشش کو کنٹرول کرنے دیتا ہے۔ کم کوشش کا مطلب ہے کم تلاش کے ساتھ تیز جوابات۔ زیادہ کوشش کا مطلب ہے کہ ماڈل گہرائی میں سوچنے کے لیے وقت لیتا ہے۔ آپ سیکھ رہے ہیں کہ کوشش کو کام کی پیچیدگی کے مطابق کریں — آسان سوالات پر وقت ضائع نہ کریں، اور پیچیدہ فیصلوں کو جلد بازی میں نہ لائیں۔

**ساخت رویے کی رہنمائی کرتی ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دیکھیے؟ وہ سجھاوٹی نہیں ہیں۔ ماڈلز آزاد متن کی نسبت منظم ہدایات پر زیادہ معتبر طریقے سے عمل کرتے ہیں۔ جب آپ کو کئی مرحلوں کے عمل یا پیچیدہ لاجک کی ضرورت ہوتی ہے، تو ساخت ماڈل کو یہ ٹریک کرنے میں مدد دیتی ہے کہ یہ کہاں ہے اور اگلا کیا آتا ہے۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*اچھے منظم پرامپٹ کا نقاب، واضح سیکشنز اور XML طرز کی ترتیب کے ساتھ*

**معیار خود جائزہ کے ذریعے**

سیلف-ریفلیکٹنگ پیٹرنز معیار کے معیار کو واضح کرتے ہوئے کام کرتے ہیں۔ ماڈل سے امید رکھنے کے بجائے کہ "یہ صحیح کرے گا"، آپ اسے بالکل بتاتے ہیں کہ "صحیح" کیا ہے: درست منطق، ایرر ہینڈلنگ، کارکردگی، سیکیورٹی۔ پھر ماڈل اپنی آؤٹ پٹ کا جائزہ لے کر اسے بہتر بنا سکتا ہے۔ یہ کوڈ جنریشن کو قرعہ اندازی سے ایک عمل میں بدل دیتا ہے۔

**سیاق و سباق محدود ہے**

کئی مرحلوں کی بات چیت ہر درخواست کے ساتھ میسج ہسٹری کو شامل کر کے کام کرتی ہے۔ لیکن حد ہوتی ہے — ہر ماڈل کا زیادہ سے زیادہ ٹوکن شمار ہوتا ہے۔ جیسے جیسے بات چیت بڑھتی ہے، آپ کو حکمت عملیاں اپنانا ہوں گی تاکہ متعلقہ سیاق و سباق کو محفوظ رکھیں بغیر حد کو پہنچائے۔ یہ ماڈیول آپ کو دکھاتا ہے کہ میموری کیسے کام کرتی ہے؛ بعد میں آپ سیکھیں گے کہ کب خلاصہ کرنا ہے، کب بھول جانا ہے، اور کب بازیافت کرنا ہے۔

## اگلے اقدامات

**اگلا ماڈیول:** [03-rag - RAG (ریٹریول ایگیمنٹڈ جنریشن)](../03-rag/README.md)

---

**نیویگیشن:** [← پچھلا: ماڈیول 01 - تعارف](../01-introduction/README.md) | [مین پر واپس](../README.md) | [اگلا: ماڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دستخطی نوٹ**:
اس دستاویز کا ترجمہ مصنوعی ذہانت کی ترجمہ خدمت [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے کیا گیا ہے۔ اگرچہ ہم درستگی کے لئے کوشاں ہیں، براہ کرم نوٹ فرمائیں کہ خودکار ترجموں میں غلطیاں یا عدم صحت ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں ہی مستند ذریعہ سمجھی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->