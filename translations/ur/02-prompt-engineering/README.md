# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ

## فہرست مضامین

- [ویڈیو واک تھرو](../../../02-prompt-engineering)
- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [ضروریات](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کو سمجھنا](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کے بنیادی اصول](../../../02-prompt-engineering)
  - [زیرو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [فیو شاٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [چین آف تھوٹ](../../../02-prompt-engineering)
  - [رول پر مبنی پرامپٹنگ](../../../02-prompt-engineering)
  - [پرامپٹ ٹیمپلیٹس](../../../02-prompt-engineering)
- [ایڈوانسڈ پیٹرنز](../../../02-prompt-engineering)
- [موجودہ Azure ذرائع استعمال کرنا](../../../02-prompt-engineering)
- [ایپلیکیشن کے اسکرین شاٹس](../../../02-prompt-engineering)
- [پیٹرنز کی تلاش](../../../02-prompt-engineering)
  - [کم بمقابلہ زیادہ جذبہ](../../../02-prompt-engineering)
  - [ٹاسک ایکزیکیوشن (ٹول پری ایمبلز)](../../../02-prompt-engineering)
  - [خود احتساب کوڈ](../../../02-prompt-engineering)
  - [مضبوط تجزیہ](../../../02-prompt-engineering)
  - [ملٹی ٹرن چیٹ](../../../02-prompt-engineering)
  - [قدم بہ قدم استدلال](../../../02-prompt-engineering)
  - [محدود آؤٹ پٹ](../../../02-prompt-engineering)
- [آپ حقیقت میں کیا سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے اقدامات](../../../02-prompt-engineering)

## ویڈیو واک تھرو

یہ لائیو سیشن دیکھیں جو اس ماڈیول کے ساتھ شروع ہونے کا طریقہ بتاتا ہے: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## آپ کیا سیکھیں گے

<img src="../../../translated_images/ur/what-youll-learn.c68269ac048503b2.webp" alt="آپ کیا سیکھیں گے" width="800"/>

گزشتہ ماڈیول میں، آپ نے دیکھا کہ میموری کس طرح گفتگو کرنے والی AI کو ممکن بناتی ہے اور بنیادی انٹریکشن کے لیے GitHub ماڈلز استعمال کیے گئے۔ اب ہم اس بات پر توجہ دیں گے کہ آپ سوالات کیسے پوچھتے ہیں — یعنی خود پرامپٹس — Azure OpenAI کے GPT-5.2 کا استعمال کرتے ہوئے۔ جس طرح آپ اپنے پرامپٹس کو ترتیب دیتے ہیں وہ آپ کو ملنے والے جوابات کے معیار پر انتہائی اثر ڈالتی ہے۔ ہم بنیادی پرامپٹنگ تکنیکوں کا جائزہ لے کر شروع کرتے ہیں، پھر آگے بڑھ کر آٹھ ایڈوانسڈ پیٹرنز پر آتے ہیں جو GPT-5.2 کی مکمل صلاحیتوں کا فائدہ اٹھاتے ہیں۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلال کنٹرول متعارف کراتا ہے - آپ ماڈل کو بتا سکتے ہیں کہ جواب دینے سے پہلے کتنی سوچ کرنی ہے۔ یہ مختلف پرامپٹنگ حکمت عملیوں کو واضح بناتا ہے اور آپ کو سمجھنے میں مدد دیتا ہے کہ کب کون سے طریقے استعمال کرنے چاہئیں۔ ہم Azure کی کم ریٹ حدوں سے بھی فائدہ اٹھائیں گے جب کہ GitHub ماڈلز کے مقابلے میں GPT-5.2 کے لیے۔

## ضروریات

- ماڈیول 01 مکمل کیا ہوا ہو (Azure OpenAI وسائل نافذ کیے ہوئے ہوں)
- روٹ ڈائریکٹری میں `.env` فائل Azure کی اسناد کے ساتھ موجود ہو (جو ماڈیول 01 میں `azd up` کے ذریعے تیار کی گئی ہو)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا تو پہلے وہاں موجود تعیناتی کی ہدایات پیروی کریں۔

## پرامپٹ انجینئرنگ کو سمجھنا

<img src="../../../translated_images/ur/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="پرامپٹ انجینئرنگ کیا ہے؟" width="800"/>

پرامپٹ انجینئرنگ اس بارے میں ہے کہ آپ ایسی ان پٹ ٹیکسٹ ڈیزائن کریں جو مستقل طور پر آپ کو مطلوبہ نتائج دے۔ یہ صرف سوالات پوچھنے کے بارے میں نہیں— بلکہ درخواستوں کو اس طرح تشکیل دینے کے بارے میں ہے کہ ماڈل بالکل سمجھ سکے کہ آپ کیا چاہتے ہیں اور کس طرح فراہم کرنا ہے۔

اسے ایسے سمجھیں جیسے آپ کسی ساتھی کو ہدایات دے رہے ہوں۔ "بگ فکس کریں" مبہم ہے۔ "UserService.java کی لائن 45 میں null pointer exception کو null چیک شامل کرکے ٹھیک کریں" مخصوص ہے۔ زبان کے ماڈلز بھی اسی طرح کام کرتے ہیں - وضاحت اور ساخت بہت اہم ہے۔

<img src="../../../translated_images/ur/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j کیسے فٹ بیٹھتا ہے" width="800"/>

LangChain4j بنیادی ڈھانچہ فراہم کرتا ہے — ماڈل کنیکشن، میموری، اور پیغام کی اقسام — جبکہ پرامپٹ پیٹرنز صرف محتاط ترتیب دی گئی ٹیکسٹ ہیں جو آپ اس بنیادی ڈھانچے میں بھیجتے ہیں۔ اہم بلڈنگ بلاکس `SystemMessage` ہے (جو AI کے رویے اور کردار کو سیٹ کرتا ہے) اور `UserMessage` (جو آپ کی اصل درخواست لے کر آتا ہے)۔

## پرامپٹ انجینئرنگ کے بنیادی اصول

<img src="../../../translated_images/ur/five-patterns-overview.160f35045ffd2a94.webp" alt="پرامپٹ انجینئرنگ کے پانچ بنیادی پیٹرنز کا جائزہ" width="800"/>

اس ماڈیول میں ایڈوانسڈ پیٹرنز میں جانے سے پہلے، آئیے پانچ بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہیں۔ یہ وہ بنیادی اجزاء ہیں جو ہر پرامپٹ انجینئر کو معلوم ہونے چاہئیں۔ اگر آپ نے پہلے ہی [Quick Start ماڈیول](../00-quick-start/README.md#2-prompt-patterns) مکمل کیا ہے، تو آپ نے انہیں عمل میں دیکھا ہوگا — یہاں ان کے پیچھے نظریاتی فریم ورک موجود ہے۔

### زیرو شاٹ پرامپٹنگ

سب سے آسان طریقہ: ماڈل کو کوئی مثال دیے بغیر براہ راست ہدایت دینا۔ ماڈل اپنے تربیتی ڈیٹا پر انحصار کرتا ہے کہ وہ کام کو سمجھے اور مکمل کرے۔ یہ سیدھے سادے کاموں کے لیے اچھا ہے جہاں مطلوبہ رویہ واضح ہو۔

<img src="../../../translated_images/ur/zero-shot-prompting.7abc24228be84e6c.webp" alt="زیرو شاٹ پرامپٹنگ" width="800"/>

*مثال کے بغیر براہ راست ہدایت — ماڈل صرف ہدایت سے کام کو سمجھتا ہے*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// جواب: "مثبت"
```

**کب استعمال کریں:** آسان درجہ بندی، سیدھے سوالات، تراجم، یا کوئی بھی کام جسے ماڈل اضافی رہنمائی کے بغیر سنبھال سکتا ہو۔

### فیو شاٹ پرامپٹنگ

ایسی مثالیں دیں جو نمونہ واضح کرتی ہوں جس کی ماڈل پیروی کرے۔ ماڈل آپ کی مثالوں سے متوقع ان پٹ-آؤٹ پٹ فارمیٹ سیکھتا ہے اور اسے نئی ان پٹس پر لاگو کرتا ہے۔ یہ ان کاموں کے لیے مستقل مزاجی کو نمایاں طور پر بہتر بناتا ہے جہاں مطلوبہ فارمیٹ یا رویہ واضح نہیں ہوتا۔

<img src="../../../translated_images/ur/few-shot-prompting.9d9eace1da88989a.webp" alt="فیو شاٹ پرامپٹنگ" width="800"/>

*مثالوں سے سیکھنا — ماڈل پیٹرن شناخت کر کے نئی ان پٹس پر لگاتا ہے*

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

**کب استعمال کریں:** کسٹم درجہ بندی، مستقل فارمیٹنگ، خاص شعبے کے کام، یا جب زیرو شاٹ نتائج غیر مستقل ہوں۔

### چین آف تھوٹ

ماڈل سے کہیں کہ وہ اپنا استدلال قدم بہ قدم دکھائے۔ ایک جواب پر فوراً پہنچنے کے بجائے، ماڈل مسئلے کو تحلیل کرتا ہے اور ہر حصہ واضح طور پر حل کرتا ہے۔ یہ ریاضی، منطق، اور کئی قدموں پر مشتمل استدلال کے کاموں کی درستگی کو بہتر بناتا ہے۔

<img src="../../../translated_images/ur/chain-of-thought.5cff6630e2657e2a.webp" alt="چین آف تھوٹ پرامپٹنگ" width="800"/>

*قدم بہ قدم استدلال — پیچیدہ مسائل کو واضح منطقی مراحل میں توڑنا*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ماڈل دکھاتا ہے: ۱۵ - ۸ = ۷، پھر ۷ + ۱۲ = ۱۹ سیب
```

**کب استعمال کریں:** ریاضی کے مسائل، منطقی پہیلیاں، ڈیبگنگ، یا کوئی بھی کام جس میں استدلال کا عمل دکھانے سے درستگی اور اعتماد بڑھتا ہے۔

### رول پر مبنی پرامپٹنگ

سوال پوچھنے سے پہلے AI کے لیے ایک کردار یا شخصیت مقرر کریں۔ یہ ایسا سیاق و سباق فراہم کرتا ہے جو جواب کے انداز، گہرائی، اور توجہ کو تشکیل دیتا ہے۔ "سافٹ ویئر آرکیٹیکٹ" کی طرف سے مشورہ "جونئیر ڈویلپر" یا "سیکیورٹی آڈیٹر" کی نسبت مختلف ہوتا ہے۔

<img src="../../../translated_images/ur/role-based-prompting.a806e1a73de6e3a4.webp" alt="رول پر مبنی پرامپٹنگ" width="800"/>

*سیاق و سباق اور شخصیت مقرر کرنا — ایک ہی سوال مختلف کردار کے مطابق مختلف جواب دیتا ہے*

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

**کب استعمال کریں:** کوڈ ریویوز، ٹیوشن، خاص شعبے کا تجزیہ، یا جب آپ کو مخصوص ماہر معیار یا نقطہ نظر کے مطابق جوابات چاہیے ہوں۔

### پرامپٹ ٹیمپلیٹس

متغیر جگہ دار کے ساتھ قابلِ استعمال پرامپٹس بنائیں۔ ہر بار نیا پرامپٹ لکھنے کے بجائے، ایک ٹیمپلیٹ تیار کریں اور مختلف اقدار ڈالتے رہیں۔ LangChain4j کا `PromptTemplate` کلاس `{{variable}}` ترکیب کے ساتھ یہ آسان بناتا ہے۔

<img src="../../../translated_images/ur/prompt-templates.14bfc37d45f1a933.webp" alt="پرامپٹ ٹیمپلیٹس" width="800"/>

*متغیر جگہ دار کے ساتھ قابلِ دوبارہ استعمال پرامپٹس — ایک ٹیمپلیٹ، متعدد استعمال*

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

**کب استعمال کریں:** مختلف ان پٹس کے ساتھ بار بار سوالات، بیچ پروسیسنگ، قابلِ استعمال AI ورک فلو بنانا، یا کوئی بھی حالت جہاں پرامپٹ کی ساخت ایک جیسی رہے مگر ڈیٹا مختلف ہو۔

---

یہ پانچ بنیادی اصول آپ کو زیادہ تر پرامپٹنگ کاموں کے لیے ایک مضبوط ٹول کِٹ فراہم کرتے ہیں۔ اس ماڈیول کا باقی حصہ انہی پر مبنی ہے اور **آٹھ جدید پیٹرنز** پر مشتمل ہے جو GPT-5.2 کے استدلال کنٹرول، خود تشخیص، اور منظم آؤٹ پٹ صلاحیتوں کا فائدہ اٹھاتے ہیں۔

## ایڈوانسڈ پیٹرنز

بنیادی اصولوں کے بعد، آئیے آٹھ ایڈوانسڈ پیٹرنز کی طرف بڑھتے ہیں جو اس ماڈیول کو منفرد بناتے ہیں۔ تمام مسائل کے لیے ایک ہی طریقہ کار نہیں چاہیے۔ کچھ سوالات کو فوری جواب درکار ہوتا ہے، دوسرے کو گہری سوچ۔ کچھ کو ظاہر شدہ استدلال چاہیے، اور کچھ کو صرف نتائج۔ ہر پیٹرن مختلف منظرنامے کے لیے بہتر ہے — اور GPT-5.2 کا استدلال کنٹرول ان فرقوں کو زیادہ نمایاں کرتا ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="آٹھ پرامپٹنگ پیٹرنز کا جائزہ" width="800"/>

*آٹھ پرامپٹ انجینئرنگ پیٹرنز اور ان کے استعمال کیسز کا خلاصہ*

<img src="../../../translated_images/ur/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 کے ساتھ استدلال کنٹرول" width="800"/>

*GPT-5.2 کا استدلال کنٹرول آپ کو بتانے دیتا ہے کہ ماڈل کو کتنا سوچنا چاہیے — فوری اور سیدھے جوابات سے لے کر گہری تحقیق تک*

**کم جذبہ (تیز اور مرکوز)** - سادہ سوالات کے لیے جہاں آپ کو فوری، براہ راست جوابات چاہیے۔ ماڈل کم از کم استدلال کرتا ہے - زیادہ سے زیادہ 2 قدم۔ اسے حسابات، تلاش، یا آسان سوالات کے لیے استعمال کریں۔

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
> - "کم جذبہ اور زیادہ جذبہ پرامپٹنگ پیٹرنز میں کیا فرق ہے؟"
> - "پرامپٹس میں XML ٹیگز AI کے جواب کو کیسے منظم کرتے ہیں؟"
> - "میں خود احتساب پیٹرنز کب استعمال کروں اور کب براہ راست ہدایت دوں؟"

**زیادہ جذبہ (گہرا اور جامع)** - پیچیدہ مسائل کے لیے جہاں آپ کو مکمل تجزیہ چاہیے۔ ماڈل تفصیل سے تحقیق کرتا ہے اور مفصل استدلال دکھاتا ہے۔ اسے سسٹم ڈیزائن، آرکیٹیکچر کے فیصلے، یا پیچیدہ تحقیق کے لیے استعمال کریں۔

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایکزیکیوشن (قدم بہ قدم پیش رفت)** - کئی مراحل پر مشتمل ورک فلو کے لیے۔ ماڈل پہلے ایک منصوبہ دیتا ہے، ہر قدم کی وضاحت کرتا ہے، پھر خلاصہ پیش کرتا ہے۔ اسے مائیگریشنز، عمل درآمد، یا کسی بھی کثیر مرحلہ عمل کے لیے استعمال کریں۔

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

چین آف تھوٹ پرامپٹنگ ماڈل کو واضح طور پر استدلال کا عمل دکھانے کے لیے کہتی ہے، جو پیچیدہ کاموں کی درستگی بہتر بناتی ہے۔ قدم بہ قدم تجزیہ انسانوں اور AI دونوں کو منطق سمجھنے میں مدد دیتا ہے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** اس پیٹرن کے بارے میں پوچھیں:
> - "میں طویل دورانیے کے آپریشنز کے لیے ٹاسک ایکزیکیوشن پیٹرن کو کیسے ڈھالوں؟"
> - "پروڈکشن ایپلیکیشنز میں ٹول پری ایمبلز کی ترتیب کے بہترین طریقے کیا ہیں؟"
> - "میں UI میں درمیانی پیش رفت کی رپورٹس کیسے پکڑ اور دکھا سکتا ہوں؟"

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="ٹاسک ایکزیکیوشن پیٹرن" width="800"/>

*کثیر قدمی کاموں کے لیے منصوبہ بندی → عمل درآمد → خلاصہ ورک فلو*

**خود احتساب کوڈ** - پروڈکشن معیار کا کوڈ بنانے کے لیے۔ ماڈل ایسے کوڈ تیار کرتا ہے جو پروڈکشن کے معیارات اور درست ایرر ہینڈلنگ کے مطابق ہو۔ اسے نئی خصوصیات یا سروسز بنانے کے لیے استعمال کریں۔

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="خود احتساب کا چکر" width="800"/>

*بہتری کا دورانیہ - تیار کریں، جائزہ لیں، مسائل کی نشاندہی کریں، بہتر بنائیں، دہرائیں*

**مضبوط تجزیہ** - مستقل جائزے کے لیے۔ ماڈل کوڈ کو ایک مقررہ فریم ورک کے تحت دیکھتا ہے (درستگی، عملی طریقے، کارکردگی، سیکیورٹی، برقرار رکھنے کی صلاحیت)۔ اسے کوڈ ریویوز یا معیار کے جائزے کے لیے استعمال کریں۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** مضبوط تجزیے کے بارے میں پوچھیں:
> - "میں مختلف قسم کی کوڈ ریویوز کے لیے تجزیہ فریم ورک کو کس طرح حسب ضرورت بنا سکتا ہوں؟"
> - "میں منظم آؤٹ پٹ کو پروگراماتی طور پر کیسے پارس اور کارروائی کر سکتا ہوں؟"
> - "میں مختلف ریویو سیشنز میں مسلسل سنگینی کی سطحیں کیسے یقینی بنا سکتا ہوں؟"

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="مضبوط تجزیہ پیٹرن" width="800"/>

*سنگینی کی سطحوں کے ساتھ مستقل کوڈ ریویو کا فریم ورک*

**ملٹی ٹرن چیٹ** - ایسی گفتگو کے لیے جنہیں سیاق و سباق کی ضرورت ہو۔ ماڈل پچھلے پیغامات کو یاد رکھتا ہے اور ان کی بنیاد پر جواب دیتا ہے۔ اسے انٹرایکٹو مدد کے سیشنز یا پیچیدہ سوال و جواب کے لیے استعمال کریں۔

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

*گفتگو کا سیاق و سباق کئی دوروں میں جمع ہوتا ہے جب تک کہ ٹوکن کی حد نہ پہنچ جائے*

**قدم بہ قدم استدلال** - ایسے مسائل کے لیے جنہیں واضح منطق کی ضرورت ہو۔ ماڈل ہر قدم کا واضح استدلال دکھاتا ہے۔ اسے ریاضی کے مسائل، منطقی پہیلیوں، یا جب آپ کو سوچنے کے عمل کو سمجھنا ہو استعمال کریں۔

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

**محدود آؤٹ پٹ** - ایسے جوابات کے لیے جن کے لیے مخصوص فارمیٹ ضروری ہو۔ ماڈل مکمل طور پر فارمیٹ اور لمبائی کے اصولوں کی پابندی کرتا ہے۔ اسے خلاصوں یا جب آپ کو درست آؤٹ پٹ ساخت چاہیے ہو استعمال کریں۔

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

*مخصوص فارمیٹ، لمبائی، اور ساخت کی پابندی کرنا*

## موجودہ Azure ذرائع استعمال کرنا

**تعیناتی کی تصدیق کریں:**

یقینی بنائیں کہ روٹ ڈائریکٹری میں `.env` فائل Azure کی اسناد کے ساتھ موجود ہے (جو ماڈیول 01 میں بنائی گئی تھی):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT دکھانا چاہیے
```

**ایپلیکیشن شروع کریں:**

> **نوٹ:** اگر آپ نے ماڈیول 01 سے `./start-all.sh` کے ذریعے تمام ایپلیکیشنز پہلے ہی شروع کر دی ہیں، تو یہ ماڈیول پورٹ 8083 پر پہلے سے چل رہا ہے۔ آپ نیچے شروع کرنے کے کمانڈز کو چھوڑ کر براہ راست http://localhost:8083 پر جا سکتے ہیں۔

**اختیار 1: اسپرنگ بوٹ ڈیش بورڈ استعمال کرنا (VS Code صارفین کے لیے تجویز کردہ)**
ڈیولپمنٹ کنٹینر میں اسپرنگ بوٹ ڈیش بورڈ ایکسٹینشن شامل ہے، جو تمام اسپرنگ بوٹ ایپلیکیشنز کو منظم کرنے کے لیے ایک بصری انٹرفیس فراہم کرتا ہے۔ آپ اسے VS کوڈ کی بائیں طرف موجود ایکٹیویٹی بار میں تلاش کر سکتے ہیں (اسپرنگ بوٹ آئیکن دیکھیں)۔

اسپرنگ بوٹ ڈیش بورڈ سے آپ کر سکتے ہیں:
- ورک اسپیس میں دستیاب تمام اسپرنگ بوٹ ایپلیکیشنز دیکھیں
- ایک کلک سے ایپلیکیشنز کو شروع/روکیں
- ایپلیکیشن کے لاگز ریئل ٹائم میں دیکھیں
- ایپلیکیشن کی حالت کی نگرانی کریں

بس "prompt-engineering" کے ساتھ پلے بٹن پر کلک کریں تاکہ یہ ماڈیول شروع ہو، یا تمام ماڈیولز کو ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**آپشن 2: شیل اسکرپٹس استعمال کرنا**

تمام ویب ایپلیکیشنز (ماڈیول 01-04) شروع کریں:

**Bash:**
```bash
cd ..  # روٹ ڈائریکٹری سے
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # روٹ ڈائریکٹری سے
.\start-all.ps1
```

یا صرف یہ ماڈیول شروع کریں:

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

دونوں اسکرپٹس خودکار طریقے سے روٹ `.env` فائل سے ماحولیاتی متغیرات لوڈ کرتے ہیں اور اگر JAR فائلیں موجود نہیں ہوتیں تو ان کو بنائیں گے۔

> **نوٹ:** اگر آپ سارے ماڈیولز کو دستی طور پر بنانا چاہتے ہیں اس سے پہلے کہ شروع کریں:
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

**رکنے کے لیے:**

**Bash:**
```bash
./stop.sh  # صرف یہ ماڈیول
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**PowerShell:**
```powershell
.\stop.ps1  # یہ ماڈیول فقط
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## ایپلیکیشن کے اسکرین شاٹس

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*مین ڈیش بورڈ جو تمام 8 پرامپٹ انجینئرنگ پیٹرنز کو ان کی خصوصیات اور استعمال کی مثالوں کے ساتھ دکھاتا ہے*

## پیٹرنز کی دریافت

ویب انٹرفیس آپ کو مختلف پرامپٹنگ حکمت عملیوں کے ساتھ تجربہ کرنے دیتا ہے۔ ہر پیٹرن مختلف مسائل کو حل کرتا ہے — ان کو آزمائیں تاکہ معلوم ہو کہ ہر طریقہ کب بہترین کام کرتا ہے۔

> **نوٹ: اسٹریمنگ بمقابلہ نان اسٹریمنگ** — ہر پیٹرن کے صفحے پر دو بٹن ہوتے ہیں: **🔴 اسٹریمنگ رسپانس (لائیو)** اور ایک **نان اسٹریمنگ** آپشن۔ اسٹریمنگ ماڈل کے ٹوکنز کو حقیقت وقت میں دکھانے کے لیے سرور سینٹ ایونٹس (SSE) استعمال کرتی ہے، اس لیے آپ پیش رفت فوراً دیکھ سکتے ہیں۔ نان اسٹریمنگ آپشن مکمل جوابی مواد کے بعد اسے دکھاتا ہے۔ ایسے پرامپٹس جو گہری سوچ پر مجبور کرتے ہیں (مثلاً High Eagerness، Self-Reflecting Code) کے لیے نان اسٹریمنگ کال بہت طویل ہو سکتی ہے — بعض اوقات منٹوں تک — بغیر کسی واضح فیڈ بیک کے۔ **پیچیدہ پرامپٹس کے ساتھ تجربہ کرتے وقت اسٹریمنگ استعمال کریں** تاکہ آپ ماڈل کو کام کرتے دیکھ سکیں اور یہ احساس نہ ہو کہ درخواست ٹائم آؤٹ ہو گئی ہے۔
>
> **نوٹ: براؤزر کی ضرورت** — اسٹریمنگ فیچر Fetch Streams API (`response.body.getReader()`) استعمال کرتا ہے جو کہ مکمل براؤزرز (کروم، ایج، فائر فاکس، سفاری) میں دستیاب ہے۔ VS کوڈ کے بلٹ ان سمپل براؤزر میں یہ کام نہیں کرتا، کیونکہ اس کا ویب ویو ReadableStream API کو سپورٹ نہیں کرتا۔ اگر آپ سمپل براؤزر استعمال کرتے ہیں، تو نان اسٹریمنگ بٹن معمول کے مطابق کام کریں گے — صرف اسٹریمنگ بٹن متاثر ہوں گے۔ مکمل تجربے کے لیے `http://localhost:8083` کسی بیرونی براؤزر میں کھولیں۔

### Low vs High Eagerness

سادہ سوال پوچھیں جیسے "15٪ کا 200 میں سے کیا ہے؟" Low Eagerness استعمال کرتے ہوئے۔ آپ کو فوری اور سیدھا جواب ملے گا۔ اب کچھ پیچیدہ پوچھیں جیسے "ہائی ٹریفک API کے لیے کیشنگ اسٹریٹیجی ڈیزائن کریں" High Eagerness کے ساتھ۔ **🔴 Stream Response (Live)** پر کلک کریں اور ہر ٹوکن کے ساتھ ماڈل کی تفصیلی سوچ دیکھیں۔ ایک ہی ماڈل، ایک ہی سوالی ساخت — لیکن پرامپٹ اسے بتاتی ہے کتنی گہرائی سے سوچنا ہے۔

### ٹاسک ایگزیکیوشن (ٹول پری ایمبلز)

کئی مراحل کے ورک فلو کو پیشگی منصوبہ بندی اور پیش رفت کی تشریح سے فائدہ ہوتا ہے۔ ماڈل بتاتا ہے کہ کیا کرے گا، ہر مرحلہ بیان کرتا ہے، پھر نتائج کا خلاصہ پیش کرتا ہے۔

### Self-Reflecting Code

"ایک ای میل ویلیڈیشن سروس بنائیں" آزما کر دیکھیں۔ ماڈل صرف کوڈ جنریٹ کر کے رکنے کی بجائے، کوڈ تیار کرتا ہے، معیار کے مطابق جانچتا ہے، کمزوریاں پہچانتا ہے، اور بہتر بناتا ہے۔ آپ دیکھیں گے کہ یہ بار بار بہتری کرتا ہے یہاں تک کہ کوڈ پروڈکشن معیار پر پہنچ جائے۔

### اسٹرکچرڈ اینالیسس

کوڈ ریویوز کے لیے مستقل جانچ کے فریم ورک کی ضرورت ہوتی ہے۔ ماڈل کوڈ کا تجزیہ کرتا ہے مخصوص زمروں کے تحت (درستگی، طریقہ کار، کارکردگی، سیکیورٹی) جس میں شدت کی درجہ بندی بھی ہوتی ہے۔

### ملٹی ٹرن چیٹ

پوچھیں "اسپرنگ بوٹ کیا ہے؟" پھر فوراً پوچھیں "ایک مثال دکھائیں"۔ ماڈل آپ کے پہلے سوال کو یاد رکھتا ہے اور خاص طور پر اسپرنگ بوٹ کی مثال دیتا ہے۔ بغیر میموری کے، دوسرا سوال بہت مبہم ہو گا۔

### مرحلہ وار استدلال

کوئی ریاضی کا مسئلہ منتخب کریں اور اسے Step-by-Step Reasoning اور Low Eagerness دونوں میں آزمائیں۔ Low Eagerness بس جواب دیتا ہے — تیز لیکن غیر واضح۔ Step-by-step ہر حساب اور فیصلہ دکھاتا ہے۔

### محدود آؤٹ پٹ

جب آپ کو مخصوص فارمیٹس یا الفاظ کی تعداد کی ضرورت ہو، یہ پیٹرن سختی سے پابند ہوتا ہے۔ بالکل 100 الفاظ کے ساتھ خلاصہ تیار کرنے کی کوشش کریں، بلٹ پوائنٹس کی شکل میں۔

## آپ واقعی کیا سیکھ رہے ہیں

**استدلال کی کوشش سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنے پرامپٹس کے ذریعے حسابی کوشش کو کنٹرول کرنے دیتا ہے۔ کم کوشش کا مطلب ہے تیز جواب کم تلاش کے ساتھ۔ زیادہ کوشش مطلب ماڈل گہرائی سے سوچنے کے لیے وقت لیتا ہے۔ آپ سیکھ رہے ہیں کہ کوشش کو کام کی پیچیدگی کے مطابق ملائیں — سادہ سوالات پر وقت ضائع نہ کریں، مگر پیچیدہ فیصلوں کو بھی جلد بازی میں نہ کریں۔

**ساخت رویے کی رہنمائی کرتی ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دیکھے؟ یہ صرف سجاوٹ نہیں ہیں۔ ماڈلز ترتیب دی گئی ہدایات کو آزاد متن سے زیادہ قابل اعتماد طریقے سے فالو کرتے ہیں۔ جب آپ کو کئی مراحل کے عمل یا پیچیدہ منطق کی ضرورت ہو، ساخت ماڈل کی مدد کرتی ہے کہ وہ جان سکے وہ کہاں ہے اور اگلا کیا آئے گا۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ایک اچھی طرح سے منظم پرامپٹ کا خاکہ واضح حصوں اور XML طرز کی ترتیب کے ساتھ*

**خود تشخیص کے ذریعے معیار**

سیلف ریفلیکٹنگ پیٹرنز معیار کے معیارات کو واضح کر کے کام کرتے ہیں۔ اس کی بجائے کہ امید کریں ماڈل "صحیح" کرے گا، آپ اسے بالکل بتاتے ہیں کہ "صحیح" کیا ہے: درست منطق، ایرر ہینڈلنگ، کارکردگی، سیکیورٹی۔ ماڈل پھر اپنی پیداوار کا اندازہ لگا کر بہتر بنا سکتا ہے۔ یہ کوڈ جنریشن کو لاٹری سے ایک عمل میں بدل دیتا ہے۔

**سیاق و سباق محدود ہے**

ملٹی ٹرن بات چیت ہر درخواست کے ساتھ پیغام کی تاریخ شامل کر کے کام کرتی ہے۔ لیکن ایک حد ہوتی ہے — ہر ماڈل کا زیادہ سے زیادہ ٹوکن شمار ہوتا ہے۔ جب بات چیت بڑھتی ہے، تو آپ کو حکمت عملی چاہیے کہ متعلقہ سیاق و سباق کو برقرار رکھیں بغیر حد تک پہنچے۔ یہ ماڈیول آپ کو دکھاتا ہے کہ میموری کیسے کام کرتی ہے؛ بعد میں آپ سیکھیں گے کہ کب خلاصہ کریں، کب بھول جائیں، اور کب بازیافت کریں۔

## اگلے اقدامات

**اگلا ماڈیول:** [03-rag - RAG (ریٹریول-آگمینٹڈ جنریشن)](../03-rag/README.md)

---

**نیوگیشن:** [← پچھلا: ماڈیول 01 - تعارف](../01-introduction/README.md) | [مین صفحے پر واپس](../README.md) | [آگے: ماڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دستبرداری**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کا استعمال کرتے ہوئے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشش کرتے ہیں، براہ کرم آگاہ رہیں کہ خودکار ترجمہ میں غلطیاں یا ناکامیاں ہو سکتی ہیں۔ اصل دستاویز اپنی مادری زبان میں معتبر ماخذ تصور کی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمہ کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا تشریح کی ذمہ داری ہماری نہیں ہوگی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->