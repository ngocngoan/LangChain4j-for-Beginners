# মডিউল ০১: LangChain4j দিয়ে শুরু করা

## বিষয়সূচী

- [ভিডিও ওয়াকথ্রু](../../../01-introduction)
- [আপনি যা শিখবেন](../../../01-introduction)
- [প্রয়োজনীয়তা](../../../01-introduction)
- [মূল সমস্যাটি বোঝা](../../../01-introduction)
- [টোকেন বোঝা](../../../01-introduction)
- [মেমোরি কীভাবে কাজ করে](../../../01-introduction)
- [LangChain4j কীভাবে ব্যবহৃত হয়](../../../01-introduction)
- [Azure OpenAI অবকাঠামো ডিপ্লয় করুন](../../../01-introduction)
- [অ্যাপ্লিকেশন লোকালি চালান](../../../01-introduction)
- [অ্যাপ্লিকেশন ব্যবহারের পদ্ধতি](../../../01-introduction)
  - [স্টেটলেস চ্যাট (বাম প্যানেল)](../../../01-introduction)
  - [স্টেটফুল চ্যাট (ডান প্যানেল)](../../../01-introduction)
- [পরবর্তী ধাপগুলি](../../../01-introduction)

## ভিডিও ওয়াকথ্রু

এই লাইভ সেশনটি দেখুন যা এই মডিউল দিয়ে শুরু করার পদ্ধতি বোঝায়:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## আপনি যা শিখবেন

যদি আপনি দ্রুত শুরু সম্পন্ন করে থাকেন, আপনি দেখেছেন কীভাবে প্রম্পট পাঠাতে হয় এবং উত্তর পেতে হয়। সেটাই ভিত্তি, কিন্তু প্রকৃত অ্যাপ্লিকেশনগুলোর বেশি কিছু প্রয়োজন। এই মডিউলটি শেখাবে কীভাবে এমন কথোপকথনমূলক এআই তৈরি করবেন যা প্রসঙ্গ স্মরণ করে এবং অবস্থা বজায় রাখে - যা এককালীন ডেমো এবং প্রোডাকশন-প্রস্তুত অ্যাপ্লিকেশনের মধ্যে পার্থক্য।

আমরা এই নির্দেশনায় সব জায়গায় Azure OpenAI এর GPT-5.2 ব্যবহার করব কারণ এর উন্নত যুক্তিবিদ্যা ক্ষমতা বিভিন্ন প্যাটার্নের আচরণ স্পষ্ট করে তোলে। যখন আপনি মেমোরি যোগ করবেন, আপনি স্পষ্টভাবে পার্থক্য দেখতে পাবেন। এটি বুঝতে সহজ করে তোলে অ্যাপ্লিকেশন-এর প্রতিটি উপাদান কী নিয়ে আসে।

আপনি একটি অ্যাপ্লিকেশন তৈরি করবেন যা উভয় প্যাটার্ন প্রদর্শন করবে:

**স্টেটলেস চ্যাট** - প্রতিটি অনুরোধ স্বতন্ত্র। মডেলের পূর্বের মেসেজের কোনও স্মৃতি নেই। এই প্যাটার্নটি আপনি দ্রুত শুরুতে ব্যবহার করেছিলেন।

**স্টেটফুল কথোপকথন** - প্রতিটি অনুরোধে কথোপকথনের ইতিহাস থাকে। মডেল একাধিক রাউন্ড জুড়ে প্রসঙ্গ বজায় রাখে। প্রোডাকশন অ্যাপ্লিকেশনগুলো যা প্রয়োজন।

## প্রয়োজনীয়তা

- Azure সাবস্ক্রিপশন এবং Azure OpenAI অ্যাক্সেস
- Java ২১, Maven ৩.৯+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **নোট:** Java, Maven, Azure CLI এবং Azure Developer CLI (azd) প্রদানকৃত devcontainer-এ পূর্বেই ইনস্টল করা আছে।

> **নোট:** এই মডিউল Azure OpenAI এর GPT-5.2 ব্যবহার করে। ডিপ্লয়মেন্ট স্বয়ংক্রিয়ভাবে `azd up` এর মাধ্যমে কনফিগার করা হয়েছে - কোডে মডেলের নাম পরিবর্তন করবেন না।

## মূল সমস্যাটি বোঝা

ভাষা মডেলগুলো স্টেটলেস। প্রতিটি API কল স্বতন্ত্র। যদি আপনি বলেন "আমার নাম জন" তারপর "আমার নাম কী?" মডেল জানে না আপনি মাত্রই পরিচয় দিয়েছেন। এটি প্রত্যেকটি অনুরোধকে মনে করে প্রথম কথোপকথন।

সরল প্রশ্নোত্তরের জন্য এটা ঠিক, তবে বাস্তব অ্যাপ্লিকেশনের জন্য অকার্যকর। গ্রাহক সেবা বটদের আপনার যা বলেছিলেন তা মনে রাখতে হয়। ব্যক্তিগত সহকারীকে প্রসঙ্গ দরকার। যেকোনো বহু-রাউন্ড কথোপকথনে মেমোরি প্রয়োজন।

<img src="../../../translated_images/bn/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*স্টেটলেস (স্বতন্ত্র কল) এবং স্টেটফুল (প্রসঙ্গ-সচেতন) কথোপকথনের পার্থক্য*

## টোকেন বোঝা

কথোপকথনে ডুবে যাওয়ার আগে টোকেনগুলো বোঝা জরুরি - যা ভাষা মডেলগুলি প্রক্রিয়াকরণের মৌলিক ইউনিট:

<img src="../../../translated_images/bn/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*কীভাবে লেখা টোকেনে বিভক্ত হয় এর উদাহরণ - "I love AI!" ৪টি পৃথক প্রক্রিয়াকরণ ইউনিটে রূপান্তরিত*

টোকেন দিয়ে AI মডেল টেক্সট পরিমাপ ও প্রক্রিয়াকরণ করে। শব্দ, বিরামচিহ্ন, এমনকি স্পেসও হতে পারে টোকেন। আপনার মডেলের একসাথে কত টোকেন প্রসেস করতে পারে তার সীমা আছে (GPT-5.2 এর জন্য ৪০০,০০০, যার মধ্যে ২৭২,০০০ ইনপুট এবং ১২৮,০০০ আউটপুট)। টোকেন বোঝা কথোপকথনের দৈর্ঘ্য ও খরচ ম্যানেজ করতে সাহায্য করে।

## মেমোরি কীভাবে কাজ করে

চ্যাট মেমোরি স্টেটলেস সমস্যাটি সমাধান করে কথোপকথনের ইতিহাস বজায় রেখে। মডেলের কাছে অনুরোধ পাঠানোর আগে ফ্রেমওয়ার্ক প্রাসঙ্গিক পূর্বের মেসেজগুলো যোগ করে। যখন আপনি "আমার নাম কী?" জিজ্ঞাসা করেন, সিস্টেম আসলে পুরো কথোপকথনের ইতিহাস পাঠায়, মডেল দেখতে পারে আপনি আগে বলেছিলেন "আমার নাম জন।"

LangChain4j এমন মেমোরি ইমপ্লিমেন্টেশন দেয় যা এটি স্বয়ংক্রিয়ভাবে পরিচালনা করে। আপনি নির্ধারণ করেন কত মেসেজ রাখতে হবে এবং ফ্রেমওয়ার্ক প্রসঙ্গ উইন্ডো নিয়ন্ত্রণ করে।

<img src="../../../translated_images/bn/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory সাম্প্রতিক মেসেজের স্লাইডিং উইন্ডো বজায় রাখে, পুরনো মেসেজ স্বয়ংক্রিয়ভাবে ফেলে দেয়*

## LangChain4j কীভাবে ব্যবহৃত হয়

এই মডিউল দ্রুত শুরু সম্প্রসারিত করে Spring Boot সংযুক্ত করে এবং কথোপকথন মেমোরি যোগ করে। উপাদানগুলো কীভাবে যুক্ত হয়:

**নিভন্ধকতা** - দুটি LangChain4j লাইব্রেরি যোগ করুন:

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

**চ্যাট মডেল** - Azure OpenAI কে Spring bean হিসেবে কনফিগার করুন ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

বিল্ডার পরিবেশ ভেরিয়েবল থেকে শনাক্তকরণের তথ্য পড়ে যা `azd up` দ্বারা সেট করা হয়। `baseUrl` আপনার Azure এন্ডপয়েন্টে সেট করলে OpenAI ক্লায়েন্ট Azure OpenAI এর সাথে কাজ করে।

**কথোপকথন মেমোরি** - MessageWindowChatMemory দিয়ে চ্যাট ইতিহাস ট্র্যাক করুন ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` দিয়ে সর্বশেষ ১০টি মেসেজ রাখার জন্য মেমোরি তৈরি করুন। ব্যবহারকারী ও এআই মেসেজ টাইপড র‌্যাপার দিয়ে যোগ করুন: `UserMessage.from(text)` এবং `AiMessage.from(text)`। `memory.messages()` দিয়ে ইতিহাস পুনরুদ্ধার করে মডেলে পাঠান। সার্ভিস প্রতিটি কথোপকথন আইডির জন্য আলাদা মেমোরি ইন্সট্যান্স রাখে, যা একাধিক ব্যবহারকারীর একই সময়ে চ্যাট করার সুযোগ দেয়।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট ব্যবহার করে চেষ্টা করুন:** খুলুন [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) এবং জিজ্ঞাসা করুন:
> - "MessageWindowChatMemory কীভাবে সিদ্ধান্ত নেয় কোন মেসেজ ফেলা হবে যখন উইন্ডো পূর্ণ?"
> - "কীভাবে ইন-মেমোরির পরিবর্তে ডাটাবেস ব্যবহার করে কাস্টম মেমোরি স্টোরেজ ইমপ্লিমেন্ট করব?"
> - "পুরনো কথোপকথনের ইতিহাস সংক্ষেপে সরাসরি যুক্ত করার জন্য কীভাবে সারাংশ যোগ করব?"

স্টেটলেস চ্যাট এন্ডপয়েন্ট মেমোরি সম্পূর্ণ এড়িয়ে যায় - যেমন দ্রুত শুরুতে `chatModel.chat(prompt)`। স্টেটফুল এন্ডপয়েন্ট মেমোরিতে মেসেজ যোগ করে, ইতিহাস পুনরুদ্ধার করে এবং প্রতিটি অনুরোধে প্রসঙ্গ যোগ করে। একই মডেল কনফিগারেশন, পার্থক্য প্যাটার্নে।

## Azure OpenAI অবকাঠামো ডিপ্লয় করুন

**Bash:**
```bash
cd 01-introduction
azd up  # সাবস্ক্রিপশন এবং অবস্থান নির্বাচন করুন (eastus2 সুপারিশ করা হয়েছে)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # সাবস্ক্রিপশন এবং অবস্থান নির্বাচন করুন (eastus2 সুপারিশকৃত)
```

> **নোট:** যদি আপনি টাইমআউট (RequestConflict: Cannot modify resource ... provisioning state is not terminal) ত্রুটি পান, শুধু আবার `azd up` চালান। Azure রিসোর্সগুলো এখনও পেছনে provisioning-এ থাকতে পারে, পুনরায় চেষ্টা করলে যখন রিসোর্সগুলি শেষ পর্যায়ে পৌছাবে তখন ডিপ্লয়মেন্ট সম্পন্ন হবে।

এর ফলে:
1. Azure OpenAI রিসোর্স GPT-5.2 ও text-embedding-3-small মডেল সহ ডিপ্লয় করা হবে
2. প্রকল্পের মূল ডিরেক্টরিতে স্বয়ংক্রিয়ভাবে `.env` ফাইল তৈরি হবে এবং ক্রেডেনশিয়াল অন্তর্ভুক্ত থাকবে
3. সব প্রয়োজনীয় পরিবেশ ভেরিয়েবল সেটআপ করা হবে

**ডিপ্লয়মেন্টে সমস্যা?** বিস্তারিত সমস্যা সমাধানের জন্য [Infrastructure README](infra/README.md) দেখুন যেখানে সাবডোমেন নাম সংক্রান্ত দ্বন্দ্ব, ম্যানুয়াল Azure পোর্টাল ডিপ্লয়মেন্ট পদ্ধতি, এবং মডেল কনফিগারেশন নির্দেশনা আছে।

**ডিপ্লয়মেন্ট সফল হয়েছে কি না যাচাই করুন:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

> **নোট:** `azd up` কমান্ড স্বয়ংক্রিয়ভাবে `.env` ফাইল তৈরি করে। পরে আপডেট করতে চাইলে ম্যানুয়ালি `.env` ফাইল সম্পাদনা করুন অথবা নিম্নলিখিত কমান্ড দিয়ে পুনরায় জেনারেট করুন:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## অ্যাপ্লিকেশন লোকালি চালান

**ডিপ্লয়মেন্ট যাচাই:**

মূল ডিরেক্টরিতে `.env` ফাইলের মধ্যে Azure ক্রেডেনশিয়াল নিশ্চিত করুন:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```


**অ্যাপ্লিকেশন শুরু করুন:**

**বিকল্প ১: Spring Boot Dashboard ব্যবহার করে (VS Code ব্যবহারকারীদের জন্য সুপারিশকৃত)**

devcontainer-এ Spring Boot Dashboard এক্সটেনশন অন্তর্ভুক্ত, যা সমস্ত Spring Boot অ্যাপ্লিকেশন পরিচালনার জন্য ভিজ্যুয়াল ইন্টারফেস প্রদান করে। VS Code এর বাম পাশে Activity Bar-এ Spring Boot আইকন খুঁজে পাবেন।

Dashboard থেকে আপনি:
- ওয়ার্কস্পেসে সমস্ত উপলব্ধ Spring Boot অ্যাপ দেখতে পারবেন
- এক ক্লিকে অ্যাপ চালু/বন্ধ করতে পারবেন
- অ্যাপ্লিকেশনের লগ বাস্তব সময়ে দেখতে পারবেন
- অ্যাপ্লিকেশনের অবস্থা মনিটর করতে পারবেন

শুধু "introduction" এর পাশে প্লে বোতাম ক্লিক করুন এই মডিউল চালু করতে, অথবা সব মডিউল একসাথে চালু করুন।

<img src="../../../translated_images/bn/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার**

সব ওয়েব অ্যাপ চালু করুন (মডিউল ০১-০৪):

**Bash:**
```bash
cd ..  # রুট ডিরেক্টরি থেকে
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # রুট ডিরেক্টরি থেকে
.\start-all.ps1
```


অথবা শুধু এই মডিউল চালু করুন:

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


উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে মূল `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করে এবং JAR না থাকলে বিল্ড করে।

> **নোট:** আপনি চাইলে সব মডিউল পূর্বে নিজে থেকে বিল্ড করতে পারেন:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```


আপনার ব্রাউজারে http://localhost:8080 খুলুন।

**বন্ধ করতে:**

**Bash:**
```bash
./stop.sh  # শুধু এই মডিউল
# অথবা
cd .. && ./stop-all.sh  # সব মডিউলসমূহ
```

**PowerShell:**
```powershell
.\stop.ps1  # শুধুমাত্র এই মডিউল
# অথবা
cd ..; .\stop-all.ps1  # সব মডিউলগুলো
```


## অ্যাপ্লিকেশন ব্যবহারের পদ্ধতি

অ্যাপ্লিকেশন একটি ওয়েব ইন্টারফেস দেয় যেখানে দুই ধরনের চ্যাট ইমপ্লিমেন্টেশন পাশাপাশিঃ

<img src="../../../translated_images/bn/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ড্যাশবোর্ডে Simple Chat (স্টেটলেস) এবং Conversational Chat (স্টেটফুল) উভয় বিকল্প দেখানো হয়েছে*

### স্টেটলেস চ্যাট (বাম প্যানেল)

এটি প্রথমে চেষ্টা করুন। বলেন "আমার নাম জন" তারপর সাথে সাথে বলেন "আমার নাম কী?" মডেল মনে রাখবে না কারণ প্রতিটি মেসেজ স্বতন্ত্র। এটি মৌলিক ভাষা মডেল সংহতির মূল সমস্যাটি প্রদর্শন করে - কথোপকথনের প্রসঙ্গ নেই।

<img src="../../../translated_images/bn/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*পূর্বের মেসেজ থেকে AI আপনার নাম মনে রাখে না*

### স্টেটফুল চ্যাট (ডান প্যানেল)

এখন একই সিকোয়েন্স এখানে চেষ্টা করুন। বলেন "আমার নাম জন" তারপর "আমার নাম কী?" এবার মডেল মনে রাখে। পার্থক্য হল MessageWindowChatMemory - এটি কথোপকথনের ইতিহাস বজায় রাখে এবং প্রতিটি অনুরোধে সেটি অন্তর্ভুক্ত করে। এটাই প্রোডাকশন কথোপকথনমূলক AI কাজ করে।

<img src="../../../translated_images/bn/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI কথোপকথনের পূর্বের অংশ থেকে আপনার নাম মনে রাখে*

উভয় প্যানেলে একই GPT-5.2 মডেল ব্যবহার হয়। একমাত্র পার্থক্য মেমোরি। এটি পরিষ্কার করে তোলে মেমোরি কী নিয়ে আসে এবং কেন বাস্তব ব্যবহারের জন্য এটি অপরিহার্য।

## পরবর্তী ধাপগুলি

**পরবর্তী মডিউল:** [০২-প্রম্পট-ইঞ্জিনিয়ারিং - GPT-5.2 দিয়ে প্রম্পট ইঞ্জিনিয়ারিং](../02-prompt-engineering/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল ০০ - দ্রুত শুরু](../00-quick-start/README.md) | [প্রধান পৃষ্ঠায় ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০২ - প্রম্পট ইঞ্জিনিয়ারিং →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**বিস্তারিত বিবৃতি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার প্রতি চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে তা অনুমান করুন। সে নথির প্রাথমিক ভাষায় থাকা মূল নথিকেই কর্তৃত্বপূর্ণ উৎস হিসেবে গণ্য করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হচ্ছে। এই অনুবাদের ব্যবহার থেকে উদ্ভূত কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী থাকব না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->