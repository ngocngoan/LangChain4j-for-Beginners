# ماڈیول 02: GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ

## فہرست مضامین

- [ویڈیو واک تھرو](../../../02-prompt-engineering)
- [آپ کیا سیکھیں گے](../../../02-prompt-engineering)
- [ضروریات](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کی سمجھ](../../../02-prompt-engineering)
- [پرامپٹ انجینئرنگ کی بنیادیں](../../../02-prompt-engineering)
  - [زیرو شوٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [فیوشوٹ پرامپٹنگ](../../../02-prompt-engineering)
  - [چین آف تھوٹ](../../../02-prompt-engineering)
  - [رول بیسڈ پرامپٹنگ](../../../02-prompt-engineering)
  - [پرامپٹ ٹیمپلیٹس](../../../02-prompt-engineering)
- [اپ گریڈ شدہ پیٹرنز](../../../02-prompt-engineering)
- [ایپلیکیشن چلائیں](../../../02-prompt-engineering)
- [ایپلیکیشن کے اسکرین شاٹس](../../../02-prompt-engineering)
- [پیٹرنز کا جائزہ](../../../02-prompt-engineering)
  - [کم بمقابلہ زیادہ ولولہ](../../../02-prompt-engineering)
  - [ٹاسک ایکزیکیوشن (ٹول پری ایمبلز)](../../../02-prompt-engineering)
  - [سیلف ریفلیکٹنگ کوڈ](../../../02-prompt-engineering)
  - [ساختی تجزیہ](../../../02-prompt-engineering)
  - [کئی مرحلوں والی بات چیت](../../../02-prompt-engineering)
  - [قدم بہ قدم منطق](../../../02-prompt-engineering)
  - [محدود خارج](../../../02-prompt-engineering)
- [آپ واقعی کیا سیکھ رہے ہیں](../../../02-prompt-engineering)
- [اگلے اقدامات](../../../02-prompt-engineering)

## ویڈیو واک تھرو

اس لائیو سیشن کو دیکھیں جو وضاحت کرتا ہے کہ اس ماڈیول کے ساتھ کیسے شروع کیا جائے:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## آپ کیا سیکھیں گے

مندرجہ ذیل خاکہ اس ماڈیول میں آپ کی ترقی کے لیے کلیدی موضوعات اور مہارتوں کا جائزہ پیش کرتا ہے—پرامپٹ بہتر بنانے کی تکنیکوں سے لے کر وہ مرحلہ وار ورک فلو جو آپ کو اپنانا ہوگا۔

<img src="../../../translated_images/ur/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

پچھلے ماڈیولز میں، آپ نے LangChain4j کے بنیادی تعاملات کو GitHub ماڈلز کے ساتھ دریافت کیا اور دیکھا کہ کس طرح یادداشت Azure OpenAI کے ساتھ بات چیت کرنے والی AI کو ممکن بناتی ہے۔ اب ہم توجہ مرکوز کریں گے کہ آپ سوالات کیسے پوچھتے ہیں—یعنی پرامپٹس خود—Azure OpenAI کے GPT-5.2 کا استعمال کرتے ہوئے۔ آپ کے پرامپٹس کی ساخت آپ کو موصول ہونے والے جوابات کی معیار پر نمایاں اثر ڈالتی ہے۔ ہم بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہوئے شروع کریں گے، پھر آگے آٹھ اپ گریڈ شدہ پیٹرنز کی طرف بڑھیں گے جو GPT-5.2 کی صلاحیتوں کو مکمل طور پر استعمال کرتے ہیں۔

ہم GPT-5.2 استعمال کریں گے کیونکہ یہ استدلالی کنٹرول متعارف کرواتا ہے—آپ ماڈل کو بتا سکتے ہیں کہ جواب دینے سے پہلے کتنا سوچنا ہے۔ یہ مختلف پرامپٹنگ حکمت عملیوں کو واضح کرتا ہے اور آپ کو سمجھنے میں مدد دیتا ہے کہ کب کون سا طریقہ استعمال کرنا ہے۔ ہم Azure کی GPT-5.2 کے لیے کم ریٹ لمٹس سے بھی فائدہ اٹھائیں گے جو GitHub ماڈلز کے مقابلے میں بہتر ہے۔

## ضروریات

- ماڈیول 01 مکمل کیا ہوا ہو (Azure OpenAI وسائل ڈپلائے کیے گئے ہوں)
- روٹ ڈائریکٹری میں `.env` فائل Azure کی سندوں کے ساتھ (جو ماڈیول 01 میں `azd up` کے ذریعے بنائی گئی ہو)

> **نوٹ:** اگر آپ نے ماڈیول 01 مکمل نہیں کیا ہے، تو پہلے وہاں موجود ڈپلائے کرنے کی ہدایات پر عمل کریں۔

## پرامپٹ انجینئرنگ کی سمجھ

بنیادی طور پر، پرامپٹ انجینئرنگ مبہم ہدایات اور واضح ہدایات کے درمیان فرق ہے، جیسا کہ درج ذیل موازنہ دکھاتا ہے۔

<img src="../../../translated_images/ur/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

پرامپٹ انجینئرنگ ایسی ان پٹ ٹیکسٹ ڈیزائن کرنے کے بارے میں ہے جو مستقل طور پر آپ کو مطلوبہ نتائج دیتی ہے۔ یہ صرف سوالات پوچھنے کی بات نہیں ہے—یہ درخواستوں کو اس طرح مرتب کرنے کی بات ہے کہ ماڈل بالکل سمجھ سکے کہ آپ کیا چاہتے ہیں اور کیسے فراہم کرنا ہے۔

اسے ایسے سمجھیں جیسے آپ اپنے ساتھی کو ہدایات دے رہے ہوں۔ "Bug درست کریں" مبہم ہے۔ "UserService.java کی لائن 45 میں null pointer exception کو null چیک شامل کر کے درست کریں" مخصوص اور واضح ہے۔ زبان کے ماڈلز بھی اسی طرح کام کرتے ہیں—وضاحت اور ساخت اہمیت رکھتے ہیں۔

ذیل کا خاکہ دکھاتا ہے کہ LangChain4j اس پروسیس میں کیسے فٹ ہوتا ہے—آپ کے پرامپٹ پیٹرنز کو SystemMessage اور UserMessage کی تعمیراتی اجزاء کے ذریعے ماڈل سے جوڑنا۔

<img src="../../../translated_images/ur/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j بنیادی ڈھانچہ فراہم کرتا ہے—ماڈل کنکشنز، میموری، اور پیغام کی اقسام—جبکہ پرامپٹ پیٹرنز صرف وہ متن ہوتے ہیں جو آپ اس ڈھانچے کے ذریعے بھیجتے ہیں۔ کلیدی اجزاء `SystemMessage` (جو AI کے رویے اور کردار کو مرتب کرتا ہے) اور `UserMessage` (جو آپ کی اصل درخواست لے کر آتا ہے) ہیں۔

## پرامپٹ انجینئرنگ کی بنیادیں

نیچے دکھائی گئی پانچ بنیادی تکنیکیں مؤثر پرامپٹ انجینئرنگ کی بنیاد ہیں۔ ہر ایک زبان کے ماڈلز سے بات چیت کے مختلف پہلو کو حل کرتی ہے۔

<img src="../../../translated_images/ur/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ہم اس ماڈیول میں اپ گریڈ شدہ پیٹرنز میں جانے سے پہلے پانچ بنیادی پرامپٹنگ تکنیکوں کا جائزہ لیتے ہیں۔ یہ وہ تعمیراتی بلاکس ہیں جو ہر پرامپٹ انجینئر کو جاننا چاہیے۔ اگر آپ پہلے ہی [Quick Start ماڈیول](../00-quick-start/README.md#2-prompt-patterns) سے گزر چکے ہیں، تو آپ نے انہیں عملی طور پر دیکھا ہے—یہاں ان کے پیچھے کا نظریاتی فریم ورک ہے۔

### زیرو شوٹ پرامپٹنگ

سب سے سادہ طریقہ: ماڈل کو براہ راست ہدایت دیں بغیر کسی مثال کے۔ ماڈل پورے عمل کو سمجھنے اور انجام دینے کے لیے اپنی تربیت پر انحصار کرتا ہے۔ یہ سیدھے سادے سوالات کے لیے بہتر کام کرتا ہے جہاں متوقع رویہ واضح ہو۔

<img src="../../../translated_images/ur/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*بغیر مثالوں کے براہ راست ہدایت—ماڈل صرف ہدایت سے ہی کام کا اندازہ لگاتا ہے*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// جواب: "مثبت"
```

**استعمال کب کریں:** سادہ درجہ بندیاں، براہ راست سوالات، ترجمے، یا کوئی بھی کام جسے ماڈل بغیر اضافی رہنمائی کے انجام دے سکے۔

### فیوشوٹ پرامپٹنگ

ایسی مثالیں فراہم کریں جو ماڈل کو وہ پیٹرن سکھائیں جو آپ چاہتے ہیں وہ اپنائے۔ ماڈل آپ کی مثالوں سے مطلوبہ ان پٹ-آؤٹ پٹ فارمیٹ سیکھتا ہے اور نئے ان پٹ پر لاگو کرتا ہے۔ یہ ان کاموں کے لیے مستقل مزاجی کو بہت بہتر بناتا ہے جہاں متوقع انداز یا رویہ واضح نہیں ہوتا۔

<img src="../../../translated_images/ur/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*مثالوں سے سیکھنا—ماڈل پیٹرن پہچانتا ہے اور نئے ان پٹس پر لاگو کرتا ہے*

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

**استعمال کب کریں:** حسب ضرورت درجہ بندیاں، مستقل فارمیٹنگ، مخصوص ڈومین کے کام، یا جب زیرو شوٹ کے نتائج غیر مستقل ہوں۔

### چین آف تھوٹ

ماڈل کو اپنے استدلال کو قدم بہ قدم دکھانے کو کہیں۔ براہ راست جواب دینے کی بجائے ماڈل مسئلہ کو توڑ کر ہر حصے کو واضح طور پر حل کرتا ہے۔ یہ ریاضی، منطق، اور کثیر مرحلوں والے استدلال کے کاموں کی درستی میں بہتری لاتا ہے۔

<img src="../../../translated_images/ur/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*قدم بہ قدم استدلال—پیچیدہ مسائل کو واضح منطقی مراحل میں توڑنا*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ماڈل دکھاتا ہے: 15 - 8 = 7، پھر 7 + 12 = 19 سیب
```

**استعمال کب کریں:** ریاضی کے مسائل، منطقی پہیلیاں، ڈیبگنگ، یا کوئی بھی کام جہاں استدلالی عمل دکھانے سے درستی اور اعتماد بڑھتا ہے۔

### رول بیسڈ پرامپٹنگ

اپنا سوال پوچھنے سے پہلے AI کے لیے ایک کردار یا شخصیت سیٹ کریں۔ یہ سیاق و سباق فراہم کرتا ہے جو جواب کے انداز، گہرائی، اور توجہ کو شکل دیتا ہے۔ "سافٹ ویئر آرکیٹیکٹ" مختلف مشورہ دیتا ہے بجائے "جونئیر ڈیویلپر" یا "سیکورٹی آڈیٹر" کے۔

<img src="../../../translated_images/ur/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*سیاق و سباق اور کردار سیٹ کرنا—ایک سوال کو مختلف رولز کے تحت مختلف جواب ملتے ہیں*

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

**استعمال کب کریں:** کوڈ ریویوز، ٹیوشنگ، مخصوص ڈومین کا تجزیہ، یا جب آپ کو مخصوص مہارت کے درجے یا نقطہ نظر کے مطابق جوابات چاہئیں۔

### پرامپٹ ٹیمپلیٹس

ری یوز ایبل پرامپٹس بنائیں جن میں متغیر جگہیں ہوں۔ ہر بار نیا پرامپٹ لکھنے کی بجائے، ایک ٹیمپلیٹ ایک دفعہ بنائیں اور مختلف قدریں اس میں بھریں۔ LangChain4j کا `PromptTemplate` کلاس یہ آسان بناتا ہے `{{variable}}` کی نحو کے ساتھ۔

<img src="../../../translated_images/ur/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*متغیر جگہوں کے ساتھ ری یوز ایبل پرامپٹس—ایک ٹیمپلیٹ، متعدد استعمال*

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

**استعمال کب کریں:** مختلف ان پٹس کے ساتھ بار بار پوچھے جانے والے سوالات، بیچ پروسیسنگ، ری یوز ایبل AI ورک فلو بنانے، یا کوئی بھی صورت جہاں پرامپٹ کی ساخت وہی رہے مگر ڈیٹا مختلف ہو۔

---

یہ پانچ بنیادی باتیں آپ کو زیادہ تر پرامپٹنگ کاموں کے لیے ایک مضبوط ٹول کٹ دیتی ہیں۔ اس ماڈیول کے باقی حصے میں **آٹھ اپ گریڈ شدہ پیٹرنز** شامل ہیں جو GPT-5.2 کی استدلالی کنٹرول، خود جائزہ، اور ساختی آؤٹ پٹ صلاحیتوں کو استعمال کرتے ہیں۔

## اپ گریڈ شدہ پیٹرنز

بنیادی باتوں کے بعد، آئیں ان آٹھ اپ گریڈ شدہ پیٹرنز کی طرف بڑھیں جو اس ماڈیول کو منفرد بناتے ہیں۔ ہر مسئلہ ایک جیسا طریقہ نہیں مانگتا۔ کچھ سوالات کو فوری جواب چاہیے، کچھ گہری سوچ۔ کچھ کو واضح استدلال چاہیے، کچھ کو صرف نتائج۔ نیچے ہر پیٹرن کو ایک الگ صورت حال کے لیے بہترین بنایا گیا ہے—اور GPT-5.2 کا استدلالی کنٹرول فرق مزید نمایاں کر دیتا ہے۔

<img src="../../../translated_images/ur/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*آٹھ پرامپٹنگ پیٹرنز اور ان کے استعمال کے مواقع کا جائزہ*

GPT-5.2 ان پیٹرنز میں ایک نیا پہلو شامل کرتا ہے: *استدلالی کنٹرول*۔ نیچے دیا گیا سلائیڈر دکھاتا ہے کہ آپ ماڈل کے سوچنے کی مقدار کو کیسے ایڈجسٹ کر سکتے ہیں—تیز، براہ راست جوابات سے گہری، مکمل تجزیہ تک۔

<img src="../../../translated_images/ur/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 کا استدلالی کنٹرول آپ کو بتانے دیتا ہے کہ ماڈل کتنا سوچے—تیز، براہ راست جواب سے لے کر گہری دریافت تک*

**کم ولولہ (تیز اور مرکوز)** - آسان سوالات کے لیے جہاں آپ کو فوری، براہ راست جواب چاہئے۔ ماڈل کم سے کم استدلال کرتا ہے—زیادہ سے زیادہ 2 مراحل۔ اس کا استعمال کیلکولیشن، تلاشی، یا سادہ سوالات کے لیے کریں۔

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
> - "کم ولولہ اور زیادہ ولولہ پرامپٹنگ پیٹرنز میں کیا فرق ہے؟"
> - "پرامپٹس میں XML ٹیگز AI کے جواب کی ساخت کو کیسے مدد دیتے ہیں؟"
> - "کب خود عکاسی والے پیٹرنز استعمال کرنا چاہیے اور کب براہ راست ہدایت؟"

**زیادہ ولولہ (گہرا اور مکمل)** - پیچیدہ مسائل کے لیے جہاں آپ کو جامع تجزیہ درکار ہو۔ ماڈل پوری طرح تلاش کرتا ہے اور تفصیلی استدلال دکھاتا ہے۔ اس کو سسٹم ڈیزائن، آرکیٹیکچر فیصلے، یا پیچیدہ تحقیق کے لیے استعمال کریں۔

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ٹاسک ایکزیکیوشن (قدم بہ قدم پیش رفت)** - کثیر مرحلوں والے ورک فلو کے لیے۔ ماڈل پہلے سے منصوبہ فراہم کرتا ہے، ہر قدم کام کے دوران بیان کرتا ہے، پھر خلاصہ پیش کرتا ہے۔ یہ ماگریشنز، عمل درآمد، یا کسی بھی کثیر مرحلوں کے عمل کے لیے موزوں ہے۔

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

چین آف تھوٹ پرامپٹنگ خاص طور پر ماڈل سے کہتا ہے کہ اپنی استدلالی فرآیند دکھائے، جس سے پیچیدہ کاموں کی درستی میں بہتری آتی ہے۔ قدم بہ قدم تقسیم انسانوں اور AI دونوں کو منطق سمجھنے میں مدد دیتی ہے۔

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ کوشش کریں:** اس پیٹرن کے بارے میں پوچھیں:
> - "لمبے عرصے کے آپریشنز کے لیے ٹاسک ایکزیکیوشن پیٹرن کو کیسے ڈھالوں؟"
> - "پروڈکشن ایپلیکیشنز میں ٹول پری ایمبلز کو ترتیب دینے کے بہترین طریقے کیا ہیں؟"
> - "UI میں مڈل پروگریس اپ ڈیٹس کو کیسے کیپچر اور ڈسپلے کروں؟"

ذیل کا خاکہ اس Plan → Execute → Summarize ورک فلو کی وضاحت کرتا ہے۔

<img src="../../../translated_images/ur/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*کثیر مرحلوں والے کاموں کے لیے Plan → Execute → Summarize ورک فلو*

**سیلف ریفلیکٹنگ کوڈ** - پروڈکشن معیار کا کوڈ تیار کرنے کے لیے۔ ماڈل پروڈکشن معیار کے مطابق کوڈ جنریٹ کرتا ہے جس میں مناسب ایرر ہینڈلنگ ہو۔ جب نئے فیچرز یا سروسز بنا رہے ہوں تو اس کا استعمال کریں۔

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

ذیل میں یہ تکراری بہتری کا چکر دکھایا گیا ہے—کوڈ پیدا کریں، جائزہ لیں، کمزوریاں معلوم کریں، اور جب تک کوڈ پروڈکشن معیار پر نہ پہنچے بہتری کریں۔

<img src="../../../translated_images/ur/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*تکراری بہتری کا چکر—پیدا کریں، جائزہ لیں، مسائل معلوم کریں، بہتر بنائیں، دہرائیں*

**ساختی تجزیہ** - مستقل جائزہ کے لیے۔ ماڈل کوڈ کو ایک مقررہ فریم ورک کے مطابق ریویو کرتا ہے (درستی، عمل، کارکردگی، سلامتی، قابل دیکھ بھال ہونا)۔ کوڈ ریویوز یا معیار کے جائزے کے لیے استعمال کریں۔

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) چیٹ کے ساتھ آزمائیں:** ساختی تجزیہ کے بارے میں پوچھیں:
> - "مختلف قسم کے کوڈ ریویوز کے لیے تجزیاتی فریم ورک کو کیسے حسب ضرورت بناؤں؟"
> - "ساختی آؤٹ پٹ کو پروگرام کے ذریعے بہترین طور پر کیسے پارس اور عمل کیا جائے؟"
> - "مختلف ریویو سیشنز کے دوران مستقل سطحات شدت کو کیسے یقینی بناؤں؟"

مندرجہ ذیل خاکہ دکھاتا ہے کہ یہ ساختی فریم ورک کوڈ ریویو کو مستقل کیٹیگریز اور شدت کی سطحوں میں کیسے منظم کرتا ہے۔

<img src="../../../translated_images/ur/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*شدت کی سطحوں کے ساتھ مستقل کوڈ ریویو کا فریم ورک*

**کئی مرحلوں والی بات چیت** - ایسی گفتگو کے لیے جسے سیاق و سباق کی ضرورت ہو۔ ماڈل پچھلے پیغامات کو یاد رکھتا ہے اور ان پر تعمیر کرتا ہے۔ یہ انٹرایکٹو مدد کے سیشنز یا پیچیدہ سوال جواب کے لیے مفید ہے۔

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

ذیل کا خاکہ دکھاتا ہے کہ گفتگو کا سیاق و سباق ہر مرحلے کے ساتھ کیسے جمع ہوتا ہے اور ماڈل کی ٹوکن حد سے کیسے جڑا ہوتا ہے۔

<img src="../../../translated_images/ur/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*گفتگو کا سیاق و سباق متعدد مراحل کے دوران جمع ہوتا ہے جب تک کہ ٹوکن حد تک نہ پہنچ جائے*
**مرحلہ بہ مرحلہ استدلال** – ان مسائل کے لیے جنہیں واضح منطق کی ضرورت ہو۔ ماڈل ہر قدم کے لیے واضح استدلال دکھاتا ہے۔ اسے ریاضی کے مسائل، منطقی پہیلیوں یا جب آپ سوچنے کے عمل کو سمجھنا چاہتے ہوں تب استعمال کریں۔

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

ذیل کا خاکہ ظاہر کرتا ہے کہ ماڈل مسائل کو واضح، نمبر شدہ منطقی مراحل میں کیسے تقسیم کرتا ہے۔

<img src="../../../translated_images/ur/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="مرحلہ بہ مرحلہ نمونہ" width="800"/>

*مسائل کو واضح منطقی مراحل میں تقسیم کرنا*

**محدود آؤٹ پٹ** – مخصوص فارمیٹ کے تقاضوں والے جوابات کے لیے۔ ماڈل سختی سے فارمیٹ اور لمبائی کے قواعد پر عمل کرتا ہے۔ اسے خلاصوں کے لیے یا جب آپ کو درست آؤٹ پٹ کی ساخت چاہیے ہو تب استعمال کریں۔

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

ذیل کا خاکہ دکھاتا ہے کہ کس طرح پابندیاں ماڈل کو ایسا آؤٹ پٹ بنانے پر مجبور کرتی ہیں جو آپ کے فارمیٹ اور لمبائی کی شرائط پر سختی سے عمل کرتا ہے۔

<img src="../../../translated_images/ur/constrained-output-pattern.0ce39a682a6795c2.webp" alt="محدود آؤٹ پٹ نمونہ" width="800"/>

*مخصوص فارمیٹ، لمبائی، اور ساخت کی شرائط پر عملدرآمد*

## ایپلیکیشن چلائیں

**ڈیپلائمنٹ کی تصدیق کریں:**

یقینی بنائیں کہ `.env` فائل روٹ ڈائریکٹری میں Azure کی اسناد کے ساتھ موجود ہے (جو موڈیول 01 میں بنائی گئی تھی)۔ اسے moڈیول ڈائریکٹری (`02-prompt-engineering/`) سے چلائیں:

**بش:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT دکھانا چاہیے
```

**پاور شیل:**
```powershell
Get-Content ..\.env  # دکھانا چاہیے AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**ایپلیکیشن شروع کریں:**

> **نوٹ:** اگر آپ نے پہلے ہی تمام ایپلیکیشنز کو `./start-all.sh` کے ذریعے روٹ ڈائریکٹری سے شروع کر دیا ہے (جیسا کہ موڈیول 01 میں بیان کیا گیا ہے)، تو یہ موڈیول پہلے سے پورٹ 8083 پر چل رہا ہے۔ آپ نیچے دیے گئے کمانڈز کو چھوڑ کر سیدھا http://localhost:8083 پر جا سکتے ہیں۔

**اختیار 1: سپرنگ بوٹ ڈیش بورڈ استعمال کرنا (VS Code صارفین کے لیے تجویز کردہ)**

ڈیولپمنٹ کنٹینر میں سپرنگ بوٹ ڈیش بورڈ ایکسٹینشن شامل ہے، جو تمام سپرنگ بوٹ ایپلیکیشنز کو منظم کرنے کے لیے بصری انٹرفیس فراہم کرتا ہے۔ آپ اسے VS Code کے بائیں جانب Activity Bar میں سپرنگ بوٹ آئیکن کے طور پر دیکھ سکتے ہیں۔

سپرنگ بوٹ ڈیش بورڈ سے آپ:
- ورک اسپیس میں دستیاب تمام سپرنگ بوٹ ایپلیکیشنز دیکھ سکتے ہیں
- ایک کلک سے ایپلیکیشنز کو شروع/روک سکتے ہیں
- ریئل ٹائم میں ایپلیکیشن لاگز دیکھ سکتے ہیں
- ایپلیکیشن کی حالت کی نگرانی کر سکتے ہیں

سادہ طور پر "prompt-engineering" کے ساتھ پلے بٹن پر کلک کریں اس موڈیول کو شروع کرنے کے لیے، یا تمام موڈیولز کو ایک ساتھ شروع کریں۔

<img src="../../../translated_images/ur/dashboard.da2c2130c904aaf0.webp" alt="سپرنگ بوٹ ڈیش بورڈ" width="400"/>

*VS Code میں سپرنگ بوٹ ڈیش بورڈ – تمام موڈیولز کو ایک جگہ سے شروع، روک، اور مانیٹر کریں*

**اختیار 2: شیل اسکرپٹس استعمال کرنا**

تمام ویب ایپلیکیشنز (موڈیولز 01-04) کو شروع کریں:

**بش:**
```bash
cd ..  # بنیادی ڈائریکٹری سے
./start-all.sh
```

**پاور شیل:**
```powershell
cd ..  # روٹ ڈائریکٹری سے
.\start-all.ps1
```

یا صرف اس موڈیول کو شروع کریں:

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

دونوں اسکرپٹس خودکار طریقے سے روٹ `.env` فائل سے ماحولیاتی متغیرات لوڈ کرتے ہیں اور اگر JAR موجود نہ ہوں تو انہیں بنائیں گے۔

> **نوٹ:** اگر آپ شروع کرنے سے پہلے تمام موڈیولز کو دستی طور پر بنانا ترجیح دیتے ہیں:
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

**روکنے کے لیے:**

**بش:**
```bash
./stop.sh  # صرف یہ ماڈیول
# یا
cd .. && ./stop-all.sh  # تمام ماڈیولز
```

**پاور شیل:**
```powershell
.\stop.ps1  # یہ صرف ماڈیول
# یا
cd ..; .\stop-all.ps1  # تمام ماڈیولز
```

## ایپلیکیشن کے اسکرین شاٹس

یہاں پرامپٹ انجینئرنگ موڈیول کا مرکزی انٹرفیس ہے، جہاں آپ آٹھوں پیٹرنز کو ایک ساتھ تجربہ کر سکتے ہیں۔

<img src="../../../translated_images/ur/dashboard-home.5444dbda4bc1f79d.webp" alt="ڈیش بورڈ ہوم" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*مرکزی ڈیش بورڈ جو آٹھ پرامپٹ انجینئرنگ پیٹرنز اور ان کی خصوصیات و استعمال کے کیسز دکھاتا ہے*

## پیٹرنز کو دریافت کرنا

ویب انٹرفیس آپ کو مختلف پرامپٹ حکمت عملیاں آزمانے دیتا ہے۔ ہر پیٹرن مختلف مسائل حل کرتا ہے – تجربہ کریں تاکہ جان سکیں کہ کون سا طریقہ کب بہترین کام کرتا ہے۔

> **نوٹ: اسٹریمنگ بمقابلہ نان-اسٹریمنگ** — ہر پیٹرن پیج دو بٹن فراہم کرتا ہے: **🔴 اسٹریم جوابی عمل (لائیو)** اور ایک **نان-اسٹریمنگ** آپشن۔ اسٹریمنگ سرور-سینٹ ایونٹس (SSE) استعمال کرتی ہے تاکہ ماڈل کے بناتے ہوئے ٹوکنز کو فوری طور پر دکھایا جا سکے، جس سے آپ کو پیش رفت فوراً نظر آتی ہے۔ نان-اسٹریمنگ آپشن پورے جواب کے مکمل ہونے کا انتظار کرتا ہے۔ گہری استدلال والے پرامپٹس (مثلاً High Eagerness، Self-Reflecting Code) کے لیے، نان-اسٹریم کال بہت طویل وقت لے سکتی ہے — کبھی کبھی منٹوں تک — بغیر کوئی نظر آنے والا فیڈ بیک۔ **پیچیدہ پرامپٹس کی جانچ کرتے وقت اسٹریمنگ استعمال کریں** تاکہ آپ ماڈل کے کام کو دیکھ سکیں اور یہ تاثر نہ بنے کہ درخواست وقت سے باہر ہو گئی۔
>
> **نوٹ: براؤزر کی ضرورت** — اسٹریمنگ فیچر Fetch Streams API (`response.body.getReader()`) استعمال کرتا ہے جو مکمل براؤزر (کروم، ایج، فائرفوکس، سفاری) کے لیے ضروری ہے۔ یہ VS Code کے بلٹ-ان سمپل براؤزر میں کام نہیں کرتا کیونکہ اس کا ویب ویو ReadableStream API کو سپورٹ نہیں کرتا۔ اگر آپ سمپل براؤزر استعمال کرتے ہیں، تو نان-اسٹریمنگ بٹنز عام طور پر کام کریں گے — صرف اسٹریمنگ بٹن متاثر ہوں گے۔ بہترین تجربے کے لیے `http://localhost:8083` کو کسی بیرونی براؤزر میں کھولیں۔

### کم بمقابلہ زیادہ جوش

ساده سوال جیسے "15% کا 200 کیا ہے؟" کم جوش کے ساتھ پوچھیں۔ آپ کو فوری اور براہ راست جواب ملے گا۔ اب کچھ پیچیدہ پوچھیں جیسے "ہائی ٹریفک API کے لیے کیشنگ حکمت عملی تیار کریں" زیادہ جوش کے ساتھ۔ **🔴 اسٹریم جوابی عمل (لائیو)** پر کلک کریں اور ماڈل کی تفصیلی استدلال کو ٹوکن بہ ٹوکن دیکھیں۔ ایک ہی ماڈل، ایک ہی سوالی ساخت – پرامپٹ بتاتا ہے کہ کتنا سوچنا ہے۔

### کام کا نفاذ (ٹول پری ایمبلز)

کئی مرحلوں کے ورک فلو کو پیشگی منصوبہ بندی اور پیش رفت کی بیانکاری سے فائدہ ہوتا ہے۔ ماڈل بتاتا ہے کہ کیا کرے گا، ہر قدم کی وضاحت کرتا ہے، پھر نتائج کا خلاصہ پیش کرتا ہے۔

### خود-عکاسی کرنے والا کوڈ

"ای میل ویلیڈیشن سروس بنائیں" آزما کر دیکھیں۔ بس کوڈ بنانے اور روکنے کے بجائے، ماڈل کوڈ جنریٹ کرتا ہے، معیار کی جانچ کرتا ہے، کمزوریاں پہچانتا ہے، اور بہتری لاتا ہے۔ آپ دیکھیں گے کہ یہ تب تک دہرائے گا جب تک کوڈ پروڈکشن معیار پر نہ پہنچ جائے۔

### منظم تجزیہ

کوڈ ریویوز کے لیے مستقل جانچ کے فریم ورک کی ضرورت ہوتی ہے۔ ماڈل کوڈ کا تجزیہ معین زمروں (صحیح ہونا، طریقے، کارکردگی، حفاظتی پہلو) کے تحت شدت کی سطحوں کے ساتھ کرتا ہے۔

### کثیر رفتاری چیٹ

"سپرنگ بوٹ کیا ہے؟" پوچھیں اور فوراً "ایک مثال دکھائیں" کا سوال کریں۔ ماڈل آپ کے پہلے سوال کو یاد رکھتا ہے اور خاص طور پر سپرنگ بوٹ کی مثال دیتا ہے۔ یادداشت کے بغیر دوسرا سوال بہت مبہم ہوتا۔

### مرحلہ بہ مرحلہ استدلال

کسی ریاضی کے مسئلے کا انتخاب کریں اور اسے مرحلہ بہ مرحلہ استدلال اور کم جوش دونوں کے ساتھ آزما کر دیکھیں۔ کم جوش صرف جواب دیتا ہے — تیز لیکن غیر واضح۔ مرحلہ بہ مرحلہ آپ کو ہر حساب اور فیصلہ دکھاتا ہے۔

### محدود آؤٹ پٹ

جب آپ کو مخصوص فارمیٹ یا لفظوں کی تعداد کی ضرورت ہو، یہ پیٹرن سخت پابندی نافذ کرتا ہے۔ بال پوائنٹس میں عین 100 الفاظ پر مشتمل خلاصہ بنانے کی کوشش کریں۔

## آپ اصل میں کیا سیکھ رہے ہیں

**استدلال کی کوشش سب کچھ بدل دیتی ہے**

GPT-5.2 آپ کو اپنی پرامپٹس کے ذریعے کمپیوٹیشنل کوشش کو کنٹرول کرنے دیتا ہے۔ کم کوشش کا مطلب ہے تیز جوابات، کم گہرائی میں تلاش۔ زیادہ کوشش کا مطلب ہے ماڈل وقت لے کر گہرائی سے سوچتا ہے۔ آپ سیکھ رہے ہیں کہ کوشش کو کام کی پیچیدگی کے مطابق کریں – سادہ سوالات پر وقت ضائع نہ کریں، مگر پیچیدہ فیصلوں میں جلد بازی نہ کریں۔

**ساختی طریقہ کار رویے کی رہنمائی کرتا ہے**

کیا آپ نے پرامپٹس میں XML ٹیگز دیکھے؟ وہ صرف سجاوٹ نہیں ہیں۔ ماڈل منظم ہدایات کو آزاد متن کے مقابلے میں زیادہ قابل اعتماد طریقے سے فالو کرتے ہیں۔ جب آپ کو کثیر مرحلہ عمل یا پیچیدہ منطق کی ضرورت ہو، ساخت ماڈل کو اس بات کی نشاندہی میں مدد دیتی ہے کہ وہ کہاں ہے اور آگے کیا کرنا ہے۔ ذیل کا خاکہ ایک اچھے منظم پرامپٹ کو دکھاتا ہے، جس میں `<system>`, `<instructions>`, `<context>`, `<user-input>`, اور `<constraints>` جیسے ٹیگز آپ کی ہدایات کو واضح حصوں میں تقسیم کرتے ہیں۔

<img src="../../../translated_images/ur/prompt-structure.a77763d63f4e2f89.webp" alt="پرومپٹ کی ساخت" width="800"/>

*ایک اچھی منظم پرامپٹ کی ترکیب، واضح سیکشنز اور XML طرز کی تنظیم کے ساتھ*

**خود جائزہ کے ذریعے معیار**

خود-عکاسی کرنے والے پیٹرنز معیار کے معیارات کو واضح کرتے ہیں۔ ماڈل سے "درست کرنے کی امید" کرنے کے بجائے، آپ بالکل بتاتے ہیں کہ "صحیح" کا مطلب کیا ہے: درست منطق، خرابی کا ہینڈلنگ، کارکردگی، سیکیورٹی۔ ماڈل پھر اپنی ہی پیداوار کا جائزہ لے سکتا ہے اور اسے بہتر بنا سکتا ہے۔ یہ کوڈ بنانے کے عمل کو لاتری سے ایک منظم طریقہ کار میں بدل دیتا ہے۔

**سیاق و سباق محدود ہوتا ہے**

کثیر مرحلوں کی بات چیت ہر درخواست کے ساتھ پیغام کی تاریخ شامل کر کے کام کرتی ہے۔ لیکن ایک حد ہوتی ہے – ہر ماڈل کا زیادہ سے زیادہ ٹوکن شمار ہوتا ہے۔ جیسے جیسے بات چیت بڑھتی ہے، آپ کو حکمت عملیاں اپنانا ہوں گی تاکہ متعلقہ سیاق و سباق کو برقرار رکھتے ہوئے حد کو نہ پہنچیں۔ یہ موڈیول آپ کو یادداشت کیسے کام کرتی ہے دکھاتا ہے؛ بعد میں آپ سیکھیں گے کہ کب خلاصہ کرنا ہے، کب بھول جانا ہے، اور کب دوبارہ حاصل کرنا ہے۔

## اگلے اقدامات

**اگلا موڈیول:** [03-rag - RAG (ریٹریول-آگمینٹڈ جنریشن)](../03-rag/README.md)

---

**نیویگیشن:** [← پچھلا: موڈیول 01 - تعارف](../01-introduction/README.md) | [مین پر واپس جائیں](../README.md) | [اگلا: موڈیول 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دستاویز کی وضاحت**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ ہم درستگی کے لئے کوشاں ہیں، لیکن براہ کرم نوٹ کریں کہ خودکار ترجمے میں غلطیاں یا نقصاں ہو سکتے ہیں۔ اصل دستاویز اپنی مادری زبان میں قابلِ اعتبار حوالہ سمجھی جانی چاہیے۔ اہم معلومات کے لئے پیشہ ور انسانی ترجمہ تجویز کیا جاتا ہے۔ اس ترجمہ کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا بدفہمی کے لئے ہم ذمہ دار نہیں ہیں۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->