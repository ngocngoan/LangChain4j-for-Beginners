# ماڈیول 01: LangChain4j کے ساتھ آغاز کرنا

## فہرست مضامین

- [ویڈیو واک تھرو](../../../01-introduction)
- [آپ کیا سیکھیں گے](../../../01-introduction)
- [پہلے سے درکار چیزیں](../../../01-introduction)
- [بنیادی مسئلے کو سمجھنا](../../../01-introduction)
- [ٹوکنز کو سمجھنا](../../../01-introduction)
- [میموری کیسے کام کرتی ہے](../../../01-introduction)
- [یہ کیسے LangChain4j استعمال کرتا ہے](../../../01-introduction)
- [Azure OpenAI انفراسٹرکچر کی تعیناتی](../../../01-introduction)
- [ایپلیکیشن کو مقامی طور پر چلانا](../../../01-introduction)
- [ایپلیکیشن کا استعمال](../../../01-introduction)
  - [اسٹیٹ لیس چیٹ (بائیں پینل)](../../../01-introduction)
  - [اسٹیٹ فل چیٹ (دائیں پینل)](../../../01-introduction)
- [اگلے مراحل](../../../01-introduction)

## ویڈیو واک تھرو

اس لائیو سیشن کو دیکھیں جو اس ماڈیول کے ساتھ شروع کرنے کا طریقہ بتاتا ہے:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## آپ کیا سیکھیں گے

اگر آپ نے کوئیک اسٹارٹ مکمل کیا ہے، تو آپ نے دیکھا کہ کیسے پرامپٹس بھیج کر جوابات حاصل کیے جاتے ہیں۔ یہ بنیاد ہے، لیکن حقیقی ایپلیکیشنز کو اس سے زیادہ کچھ چاہیے۔ یہ ماڈیول آپ کو ایسا مکالماتی AI بنانے کی تربیت دیتا ہے جو سیاق و سباق یاد رکھتا ہے اور حالت کو برقرار رکھتا ہے — جو ایک وقتی ڈیمو اور پروڈکشن کے لئے تیار ایپلیکیشن میں فرق ہے۔

ہم اس رہنما میں Azure OpenAI کا GPT-5.2 استعمال کریں گے کیونکہ اس کی اعلیٰ استدلال کی صلاحیتیں مختلف پیٹرنز کے رویے کو واضح بناتی ہیں۔ جب آپ میموری شامل کریں گے تو فرق واضح طور پر نظر آئے گا۔ اس سے یہ سمجھنا آسان ہو جاتا ہے کہ ہر جزو آپ کی ایپ میں کیا لاتا ہے۔

آپ ایک ایسی ایپلیکیشن بنائیں گے جو دونوں پیٹرنز کا مظاہرہ کرتی ہے:

**اسٹیٹ لیس چیٹ** - ہر درخواست خودمختار ہوتی ہے۔ ماڈل کو پچھلے پیغامات کی کوئی یادداشت نہیں ہوتی۔ یہ وہی پیٹرن ہے جو آپ نے کوئیک اسٹارٹ میں استعمال کیا۔

**اسٹیٹ فل گفتگو** - ہر درخواست میں بات چیت کی تاریخ شامل ہوتی ہے۔ ماڈل متعدد بات چیت کے چکروں میں سیاق و سباق کو برقرار رکھتا ہے۔ یہی پروڈکشن ایپلیکیشنز کے لئے ضروری ہے۔

## پہلے سے درکار چیزیں

- Azure سبسکرپشن جس میں Azure OpenAI کا رسائی ہو
- جاوا 21، میون 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **نوٹ:** جاوا، میون، Azure CLI اور Azure Developer CLI (azd) فراہم کردہ devcontainer میں پہلے سے انسٹال ہیں۔

> **نوٹ:** یہ ماڈیول Azure OpenAI پر GPT-5.2 استعمال کرتا ہے۔ تعیناتی خودکار طور پر `azd up` کے ذریعے کی جاتی ہے — کوڈ میں ماڈل کے نام کو تبدیل نہ کریں۔

## بنیادی مسئلے کو سمجھنا

زبان کے ماڈلز اسٹیٹ لیس ہوتے ہیں۔ ہر API کال خودمختار ہوتی ہے۔ اگر آپ "میرا نام جان ہے" بھیجیں اور پھر پوچھیں "میرا نام کیا ہے؟"، تو ماڈل کو کوئی اندازہ نہیں ہوتا کہ آپ نے اپنے آپ کو متعارف کرایا ہے۔ یہ ہر درخواست کو اس طرح سمجھتا ہے جیسے یہ آپ کی پہلی گفتگو ہو۔

یہ سادہ سوال و جواب کے لئے ٹھیک ہے لیکن حقیقی ایپلیکیشنز کے لئے بے معنی ہے۔ کسٹمر سروس بوٹس کو یاد رکھنا ہوتا ہے کہ آپ نے کیا کہا۔ ذاتی معاونین کو سیاق و سباق کی ضرورت ہوتی ہے۔ کوئی بھی کثیر چکروں والی گفتگو میموری کا تقاضا کرتی ہے۔

<img src="../../../translated_images/ur/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*اسٹیٹ لیس (خودمختار کالز) اور اسٹیٹ فل (سیاق و سباق سے باخبر) گفتگو کے درمیان فرق*

## ٹوکنز کو سمجھنا

گفتگو میں داخل ہونے سے پہلے، ٹوکنز کو سمجھنا ضروری ہے — وہ بنیادی متن کی اکائیاں جو زبان کے ماڈلز پروسیس کرتے ہیں:

<img src="../../../translated_images/ur/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*متن کو ٹوکنز میں توڑنے کی مثال — "I love AI!" چار علیحدہ پراسیسنگ یونٹس بن جاتے ہیں*

ٹوکنز وہ طریقہ ہیں جس سے AI ماڈلز متن کو ناپتے اور پروسیس کرتے ہیں۔ الفاظ، رموز، اور یہاں تک کہ خالی جگہیں بھی ٹوکنز ہو سکتی ہیں۔ آپ کے ماڈل کے پاس ایک حد ہوتی ہے کہ وہ بیک وقت کتنے ٹوکنز کو پروسیس کر سکتا ہے (GPT-5.2 کے لیے 400,000، جن میں 272,000 ان پٹ اور 128,000 آؤٹ پٹ ٹوکنز شامل ہیں)۔ ٹوکنز کو سمجھنا آپ کو گفتگو کی لمبائی اور لاگت کو مینیج کرنے میں مدد دیتا ہے۔

## میموری کیسے کام کرتی ہے

چیٹ میموری اسٹیٹ لیس مسئلہ کو بات چیت کی تاریخ برقرار رکھ کر حل کرتی ہے۔ ماڈل کو اپنی درخواست بھیجنے سے پہلے، فریم ورک متعلقہ پچھلے پیغامات کو اس کے آگے لگا دیتا ہے۔ جب آپ پوچھتے ہیں "میرا نام کیا ہے؟"، تو سسٹم پورے گفتگو کی تاریخ بھیجتا ہے، جس سے ماڈل دیکھ سکتا ہے کہ آپ نے پہلے کہا "میرا نام جان ہے۔"

LangChain4j میموری کی ایسی امپلیمنٹیشنز فراہم کرتا ہے جو یہ خود بخود سنبھالتی ہیں۔ آپ منتخب کرتے ہیں کہ آپ کتنے پیغامات رکھنا چاہتے ہیں اور فریم ورک سیاق و سباق کی ونڈو کا انتظام کرتا ہے۔

<img src="../../../translated_images/ur/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory حالیہ پیغامات کی سلائیڈنگ ونڈو برقرار رکھتا ہے، پرانے خود بخود ڈراپ کر دیتا ہے*

## یہ کیسے LangChain4j استعمال کرتا ہے

یہ ماڈیول کوئیک اسٹارٹ کو بڑھاتے ہوئے Spring Boot کے ساتھ انٹیگریٹ کرتا ہے اور بات چیت کی میموری شامل کرتا ہے۔ یہاں مختلف حصے کیسے جُڑتے ہیں:

**Dependencies** - دو LangChain4j لائبریریاں شامل کریں:

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
  
**چیٹ ماڈل** - Azure OpenAI کو Spring bean کے طور پر کنفیگر کریں ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
بلڈر environment variables سے اسناد پڑھتا ہے جو `azd up` نے سیٹ کی ہیں۔ `baseUrl` کو آپ کے Azure اینڈپوائنٹ پر سیٹ کرنے سے OpenAI کلائنٹ Azure OpenAI کے ساتھ کام کرنے لگتا ہے۔

**بات چیت کی میموری** - MessageWindowChatMemory کے ساتھ چیٹ کی تاریخ ٹریک کریں ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
میموری کو `withMaxMessages(10)` کے ساتھ بنائیں تاکہ آخری 10 پیغامات رکھے جائیں۔ یوزر اور AI کے پیغامات ٹائپڈ ریپّر `UserMessage.from(text)` اور `AiMessage.from(text)` کے ذریعے شامل کریں۔ تاریخ کو `memory.messages()` سے حاصل کریں اور ماڈل کو بھیجیں۔ سروس ہر گفتگو ID کے لئے الگ میموری انسٹینسز رکھتی ہے، جو متعدد یوزرز کو بیک وقت بات چیت کرنے دیتی ہے۔

> **🤖 GitHub Copilot کے ساتھ آزما کر دیکھیں:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) کھولیں اور پوچھیں:  
> - "MessageWindowChatMemory ونڈو بھرنے پر کون سے پیغامات ڈراپ کرنے کا فیصلہ کیسے کرتا ہے؟"  
> - "کیا میں ان-میموری کے بجائے ڈیٹا بیس استعمال کرتے ہوئے کسٹم میموری اسٹوریج بنا سکتا ہوں؟"  
> - "میں پرانی بات چیت کی تاریخ کو کمپریس کرنے کے لیے سمری کیسے شامل کر سکتا ہوں؟"

اسٹیٹ لیس چیٹ اینڈپوائنٹ میموری کو مکمل نظر انداز کرتا ہے — صرف `chatModel.chat(prompt)` جیسا کہ کوئیک اسٹارٹ میں تھا۔ اسٹیٹ فل اینڈپوائنٹ میسجز کو میموری میں شامل کرتا ہے، تاریخ بازیافت کرتا ہے، اور ہر درخواست کے ساتھ سیاق و سباق شامل کرتا ہے۔ ایک ہی ماڈل کنفیگریشن، مختلف پیٹرنز۔

## Azure OpenAI انفراسٹرکچر کی تعیناتی

**بش:**  
```bash
cd 01-introduction
azd up  # سبسکرپشن اور مقام منتخب کریں (eastus2 کی سفارش کی جاتی ہے)
```
  
**پاور شیل:**  
```powershell
cd 01-introduction
azd up  # سبسکرپشن اور مقام منتخب کریں (eastus2 تجویز کردہ)
```
  
> **نوٹ:** اگر آپ کو ٹائم آؤٹ کی خرابی ( `RequestConflict: Cannot modify resource ... provisioning state is not terminal`) کا سامنا ہو، تو بس دوبارہ `azd up` چلائیں۔ Azure وسیلے ابھی پس منظر میں تعینات ہو رہے ہوں گے، اور دوبارہ کوشش کرنے سے تعیناتی مکمل ہو جاتی ہے جب وسیلے مکمل طور پر تیار ہو جائیں۔

یہ کرے گا:  
1. Azure OpenAI وسیلہ GPT-5.2 اور text-embedding-3-small ماڈلز کے ساتھ تعینات کرے گا  
2. خودکار طور پر پروجیکٹ روٹ میں `.env` فائل بنائے گا جس میں اسناد ہوں گی  
3. تمام مطلوبہ environment variables سیٹ کرے گا

**تعیناتی میں مسائل؟** تفصیلی مسائل حل کرنے کے لیے [Infrastructure README](infra/README.md) دیکھیں جس میں سب ڈومین نام کے تنازعات، دستی Azure پورٹل تعیناتی کے مراحل، اور ماڈل کنفیگریشن کی رہنمائی شامل ہے۔

**تعیناتی کی تصدیق کریں:**

**بش:**  
```bash
cat ../.env  # دکھانا چاہیے AZURE_OPENAI_ENDPOINT، API_KEY، وغیرہ۔
```
  
**پاور شیل:**  
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT، API_KEY وغیرہ دکھانا چاہیے۔
```
  
> **نوٹ:** `azd up` کمانڈ خودکار طور پر `.env` فائل بناتا ہے۔ اگر آپ کو اسے بعد میں اپ ڈیٹ کرنا ہو تو یا تو `.env` فائل کو دستی طور پر ایڈٹ کریں یا درج ذیل کمانڈز چلائیں:  
>  
> **بش:**  
> ```bash
> cd ..
> bash .azd-env.sh
> ```
  
> **پاور شیل:**  
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```
  

## ایپلیکیشن کو مقامی طور پر چلانا

**تعیناتی کی تصدیق کریں:**

یقین دہانی کریں کہ `.env` فائل روٹ ڈائریکٹری میں موجود ہو اور اس میں Azure کی اسناد ہوں:

**بش:**  
```bash
cat ../.env  # دکھانا چاہیے AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT
```
  
**پاور شیل:**  
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT، API_KEY، DEPLOYMENT دکھانا چاہیے
```
  
**ایپلیکیشنز کو شروع کریں:**

**آپشن 1: Spring Boot Dashboard استعمال کرنا (VS Code صارفین کے لیے تجویز کردہ)**

devcontainer میں Spring Boot Dashboard ایکسٹینشن شامل ہے، جو تمام Spring Boot ایپلیکیشنز کو منظم کرنے کے لیے بصری انٹرفیس فراہم کرتا ہے۔ آپ اسے VS Code کے بائیں جانب ایکٹیویٹی بار میں، Spring Boot آئیکون کے طور پر دیکھ سکتے ہیں۔

Spring Boot Dashboard سے آپ:  
- ورک اسپیس میں دستیاب تمام Spring Boot ایپلیکیشنز دیکھ سکتے ہیں  
- ایک کلک سے ایپلیکیشنز کو شروع/روک سکتے ہیں  
- ایپلیکیشن کے لاگز ریئل ٹائم میں دیکھ سکتے ہیں  
- ایپلیکیشن کی صورتحال مانیٹر کر سکتے ہیں

بس "introduction" کے ساتھ پلے بٹن پر کلک کریں تاکہ یہ ماڈیول شروع ہو، یا ایک ساتھ تمام ماڈیولز چلائیں۔

<img src="../../../translated_images/ur/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**آپشن 2: شیل اسکرپٹس استعمال کرنا**

تمام ویب ایپلیکیشنز (ماڈیولز 01-04) شروع کریں:

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
  
یا صرف یہ ماڈیول شروع کریں:

**بش:**  
```bash
cd 01-introduction
./start.sh
```
  
**پاور شیل:**  
```powershell
cd 01-introduction
.\start.ps1
```
  
دونوں اسکرپٹس خودکار طور پر روٹ `.env` فائل سے environment variables لوڈ کرتے ہیں اور اگر JARs موجود نہ ہوں تو وہ بنا لیتے ہیں۔

> **نوٹ:** اگر آپ شروع کرنے سے پہلے تمام ماڈیولز کو دستی طور پر بنانا چاہتے ہیں:  
>  
> **بش:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **پاور شیل:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
براہ کرم http://localhost:8080 اپنے براؤزر میں کھولیں۔

**روکنے کے لیے:**

**بش:**  
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
  

## ایپلیکیشن کا استعمال

ایپلیکیشن دو چیٹ امپلیمنٹیشنز کو سائیڈ بائی سائیڈ ویب انٹرفیس پر فراہم کرتی ہے۔

<img src="../../../translated_images/ur/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ڈیش بورڈ جو سادہ چیٹ (اسٹیٹ لیس) اور مکالماتی چیٹ (اسٹیٹ فل) دونوں کے اختیارات دکھا رہا ہے*

### اسٹیٹ لیس چیٹ (بائیں پینل)

سب سے پہلے یہ آزمائیں۔ پوچھیں "میرا نام جان ہے" اور فوراً بعد میں پوچھیں "میرا نام کیا ہے؟" ماڈل یاد نہیں رکھے گا کیونکہ ہر پیغام خودمختار ہوتا ہے۔ یہ اس بنیادی مسئلے کو ظاہر کرتا ہے جو سادہ زبان ماڈل کے انضمام کے ساتھ آتا ہے — کوئی گفتگو کا سیاق و سباق نہیں۔

<img src="../../../translated_images/ur/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI آپ کا نام پچھلے پیغام سے یاد نہیں رکھتا*

### اسٹیٹ فل چیٹ (دائیں پینل)

اب یہی سلسلہ یہاں آزمائیں۔ پوچھیں "میرا نام جان ہے" اور پھر "میرا نام کیا ہے?" اس بار ماڈل یاد رکھتا ہے۔ فرق MessageWindowChatMemory ہے — یہ بات چیت کی تاریخ برقرار رکھتا ہے اور ہر درخواست کے ساتھ شامل کرتا ہے۔ یہی طریقہ ہے جس سے پروڈکشن مکالماتی AI کام کرتا ہے۔

<img src="../../../translated_images/ur/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI آپ کا نام پہلے کی گفتگو سے یاد رکھتا ہے*

دونوں پینلز ایک ہی GPT-5.2 ماڈل استعمال کرتے ہیں۔ واحد فرق میموری ہے۔ اس سے واضح ہوتا ہے کہ میموری آپ کی ایپلیکیشن میں کیا لاتی ہے اور یہ حقیقی استعمال کے لیے کیوں ضروری ہے۔

## اگلے مراحل

**اگلا ماڈیول:** [02-prompt-engineering - GPT-5.2 کے ساتھ پرامپٹ انجینئرنگ](../02-prompt-engineering/README.md)

---

**نیویگیشن:** [← پچھلا: ماڈیول 00 - کوئیک اسٹارٹ](../00-quick-start/README.md) | [مین پر واپس](../README.md) | [اگلا: ماڈیول 02 - پرامپٹ انجینئرنگ →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دستخطی اعلامیہ**:  
اس دستاویز کا ترجمہ AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے کیا گیا ہے۔ اگرچہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجموں میں غلطیاں یا غیر یقینی باتیں ہو سکتی ہیں۔ اصل دستاویز اپنی مادری زبان میں ہی معتبر ماخذ سمجھی جائے گی۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ تجویز کیا جاتا ہے۔ اس ترجمہ کے استعمال سے پیدا ہونے والے کسی بھی غلط فہمی یا غلط تعبیر کی ذمہ داری ہماری نہیں ہے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->