# মডিউল ০১: LangChain4j দিয়ে শুরু করা

## বিষয়সূচি

- [আপনি যা শিখবেন](../../../01-introduction)
- [প্রয়োজনীয়তাসমূহ](../../../01-introduction)
- [মূল সমস্যা বোঝা](../../../01-introduction)
- [টোকেন বোঝা](../../../01-introduction)
- [মেমরি কিভাবে কাজ করে](../../../01-introduction)
- [কিভাবে এটি LangChain4j ব্যবহার করে](../../../01-introduction)
- [Azure OpenAI অবকাঠামো ডিপ্লয় করুন](../../../01-introduction)
- [অ্যাপ্লিকেশন লোকালে চালান](../../../01-introduction)
- [অ্যাপ্লিকেশন ব্যবহার](../../../01-introduction)
  - [ Stateless চ্যাট (বাম প্যানেল)](../../../01-introduction)
  - [ Stateful চ্যাট (ডান প্যানেল)](../../../01-introduction)
- [পরবর্তী ধাপসমূহ](../../../01-introduction)

## আপনি যা শিখবেন

যদি আপনি দ্রুত শুরুটি সম্পন্ন করে থাকেন, আপনি দেখেছেন কিভাবে প্রম্পট পাঠাতে হয় এবং প্রতিক্রিয়া পাওয়া যায়। সেটাই ভিত্তি, কিন্তু বাস্তব অ্যাপ্লিকেশনগুলো আরও কিছু প্রয়োজন। এই মডিউলটিতে শেখানো হবে কিভাবে এমন কনভারসেশনাল AI তৈরি করবেন যা প্রসঙ্গ মনে রাখে এবং অবস্থা রক্ষা করে—একবারের ডেমো এবং প্রোডাকশন-রেডি অ্যাপ্লিকেশনের মধ্যে পার্থক্য।

আমরা এই গাইডে Azure OpenAI-এর GPT-5.2 ব্যবহার করবো কারণ এর উন্নত যুক্তি ক্ষমতাগুলো বিভিন্ন প্যাটার্নের আচরণ স্পষ্ট করে। যখন আপনি মেমরি যোগ করবেন, তখন পার্থক্য স্পষ্ট বুঝতে পারবেন। এটি সহজ করে তোলে বুঝতে যে প্রতিটি কম্পোনেন্ট আপনার অ্যাপ্লিকেশনে কি নিয়ে আসে।

আপনি একটি অ্যাপ্লিকেশন তৈরি করবেন যা দুইটি প্যাটার্ন প্রদর্শন করে:

**Stateless Chat** - প্রতিটি অনুরোধ স্বতন্ত্র। মডেল পূর্বের মেসেজ মনে রাখে না। এই প্যাটার্নটি আপনি দ্রুত শুরুতে ব্যবহার করেছিলেন।

**Stateful Conversation** - প্রতিটি অনুরোধে কনভারসেশন ইতিহাস থাকে। মডেল একাধিক পালার মধ্যে প্রসঙ্গ রক্ষা করে। এটাই প্রোডাকশন অ্যাপ্লিকেশনের জন্য প্রয়োজন।

## প্রয়োজনীয়তাসমূহ

- Azure সাবস্ক্রিপশান যা Azure OpenAI অ্যাক্সেস সহ
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **বিঃদ্রঃ** Java, Maven, Azure CLI এবং Azure Developer CLI (azd) প্রোভাইডকৃত devcontainer-এ পূর্বেই ইনস্টল করা আছে।

> **বিঃদ্রঃ** এই মডিউলটি Azure OpenAI তে GPT-5.2 ব্যবহার করে। ডিপ্লয়মেন্ট `azd up` এর মাধ্যমে স্বয়ংক্রিয়ভাবে কনফিগার করা হয়—কোডে মডেলের নাম পরিবর্তন করবেন না।

## মূল সমস্যা বোঝা

ল্যাঙ্গুয়েজ মডেলগুলো stateless। প্রতিটি API কল স্বতন্ত্র। আপনি যদি বলেন "My name is John" এবং তারপর প্রশ্ন করেন "What's my name?", মডেলটি আপনার নাম জানতে পারবে না কারণ এটি প্রতিটি অনুরোধকে প্রথম কনভারসেশন হিসেবে বিবেচনা করে।

সাধারণ প্রশ্নোত্তরের জন্য এটা ঠিক আছে, কিন্তু বাস্তব অ্যাপ্লিকেশনের জন্য কার্যহীন। কাস্টমার সার্ভিস বটগুলোকে আপনাকে বলেছিলো কি সেটা মনে রাখতে হয়। ব্যক্তিগত অ্যাসিস্ট্যান্টদের প্রসঙ্গ দরকার। যেকোনো বহু-পালা কনভারসেশনের জন্য মেমরি প্রয়োজন।

<img src="../../../translated_images/bn/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

* Stateless (স্বতন্ত্র কল) এবং Stateful (প্রসঙ্গ সচেতন) কনভারসেশনের পার্থক্য *

## টোকেন বোঝা

কনভারসেশনে ডুব দেওয়ার আগে টোকেন বোঝা গুরুত্বপূর্ণ — ভাষা মডেলগুলো যে মৌলিক ইউনিটগুলো প্রক্রিয়া করে:

<img src="../../../translated_images/bn/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*টেক্সটকে কিভাবে টোকেনে ভাগ করা হয়— "I love AI!" চারটি আলাদা প্রসেসিং ইউনিটে পরিণত হয়*

টোকেন হল AI মডেলগুলোর টেক্সট মাপার এবং প্রসেস করার একক। শব্দ, বিরামচিহ্ন, এমনকি স্পেসও টোকেন হতে পারে। আপনার মডেলের কত টোকেন একবারে প্রক্রিয়া করতে পারে তার সীমা আছে (GPT-5.2 এর জন্য ৪,০০,০০০, যার মধ্যে ইনপুট টোকেন সর্বোচ্চ ২৭২,০০০ এবং আউটপুট ১,২৮,০০০)। টোকেন বোঝা কনভারসেশন দৈর্ঘ্য এবং খরচ নিয়ন্ত্রণে সাহায্য করে।

## মেমরি কিভাবে কাজ করে

চ্যাট মেমরি stateless সমস্যার সমাধান করে কনভারসেশন ইতিহাস ধরে রেখে। মডেলে অনুরোধ পাঠানোর আগে, ফ্রেমওয়ার্ক প্রাসঙ্গিক পূর্বের মেসেজগুলো অগ্রসর করে। যখন আপনি "What's my name?" প্রশ্ন করেন, সিস্টেম পুরো কনভারসেশন ইতিহাস পাঠায়, তাই মডেল দেখতে পায় আপনি আগে বলেছিলেন "My name is John."

LangChain4j স্বয়ংক্রিয়ভাবে এটি পরিচালনার জন্য মেমরি ইমপ্লিমেন্টেশনগুলো প্রদান করে। আপনি ঠিক করেন কতগুলো মেসেজ রাখতে চান এবং ফ্রেমওয়ার্ক প্রসঙ্গ উইন্ডোটি পরিচালনা করে।

<img src="../../../translated_images/bn/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory সাম্প্রতিক মেসেজগুলোর স্লাইডিং উইন্ডো রাখে, পুরানোগুলো স্বয়ংক্রিয়ভাবে বাদ দেয়*

## কিভাবে এটি LangChain4j ব্যবহার করে

এই মডিউলটি দ্রুত শুরু থেকে বাড়িয়ে Spring Boot একীভূত করে এবং কনভারসেশন মেমরি যোগ করে। অংশগুলো কিভাবে একসাথে বসে:

**ডিপেন্ডেন্সিস** - দুটি LangChain4j লাইব্রেরি যোগ করুন:

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

**চ্যাট মডেল** - Azure OpenAIকে Spring বিন হিসেবে কনফিগার করুন ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

বিল্ডার `azd up` দ্বারা সেট হওয়া এনভায়রনমেন্ট ভেরিয়েবল থেকে ক্রেডেনশিয়াল পড়ে। `baseUrl` আপনার Azure এন্ডপয়েন্টে সেট করলে OpenAI ক্লায়েন্ট Azure OpenAI এর সাথে কাজ করে।

**কনভারসেশন মেমরি** - MessageWindowChatMemory দিয়ে চ্যাট ইতিহাস ট্র্যাক করুন ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` দিয়ে সর্বশেষ ১০টি মেসেজ রাখার মেমরি তৈরি করুন। টাইপ করা র‍্যাপার ব্যবহার করে ইউজার এবং AI মেসেজ যোগ করুন: `UserMessage.from(text)` ও `AiMessage.from(text)`। ইতিহাস `memory.messages()` দিয়ে আনুন এবং মডেলে পাঠান। সার্ভিসটি প্রতিটি কনভারসেশন ID অনুযায়ী আলাদা মেমরি ইনস্ট্যান্স সংরক্ষণ করে, ফলে একাধিক ইউজার একসাথে চ্যাট করতে পারে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** ওপেন করুন [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) এবং জিজ্ঞাসা করুন:
> - "MessageWindowChatMemory কীভাবে নির্ধারণ করে কোন মেসেজগুলো ফেলে দিতে হবে যখন উইন্ডো পূর্ণ?"
> - "কীভাবে কাস্টম মেমরি স্টোরেজ ইমপ্লিমেন্ট করা যাবে মেমোরির পরিবর্তে ডাটাবেস ব্যবহারে?"
> - "পুরানো কনভারসেশন ইতিহাসকে সংক্ষিপ্ত করতে কীভাবে সারমাইজেশন যোগ করা যায়?"

Stateless চ্যাট এন্ডপয়েন্ট মেমরি পুরোপুরি এড়িয়ে চলে — শুধু `chatModel.chat(prompt)` হয় দ্রুত শুরু অনুযায়ী। Stateful এন্ডপয়েন্ট মেমরিতে মেসেজ যোগ করে, ইতিহাস আনতে এবং প্রতিটি অনুরোধের সঙ্গে প্রসঙ্গ যোগ করে। একই মডেল কনফিগারেশন, ভিন্ন প্যাটার্ন।

## Azure OpenAI অবকাঠামো ডিপ্লয় করুন

**Bash:**
```bash
cd 01-introduction
azd up  # সাবস্ক্রিপশন এবং অবস্থান নির্বাচন করুন (eastus2 সুপারিশ করা হয়েছে)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # সাবস্ক্রিপশন এবং অবস্থান নির্বাচন করুন (eastus2 পরামর্শকৃত)
```

> **বিঃদ্রঃ:** যদি টাইমআউট এরর পান (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), শুধু আবার `azd up` চালান। Azure রিসোর্সসমূহ ব্যাকগ্রাউন্ডে প্রোভাইজনিং এ থাকতে পারে, পুনরায় চেষ্টা করলে রিসোর্সগুলো টারমিনাল স্থিতি এলে ডিপ্লয়মেন্ট সম্পন্ন হয়।

এটি করবে:
1. Azure OpenAI রিসোর্স GPT-5.2 এবং text-embedding-3-small মডেলসহ ডিপ্লয়মেন্ট
2. প্রজেক্ট রুটে স্বয়ংক্রিয় `.env` ফাইল তৈরি, যেখানে ক্রেডেনশিয়াল থাকবে
3. প্রয়োজনীয় সকল এনভায়রনমেন্ট ভেরিয়েবল সেট আপ

**ডিপ্লয়মেন্টে সমস্যা?** বিস্তারিত সমস্যার সমাধানের জন্য [Infrastructure README](infra/README.md) দেখুন যেখানে সাবডোমেইন নাম সংঘাত, ম্যানুয়াল Azure Portal ডিপ্লয়মেন্ট ধাপ এবং মডেল কনফিগারেশন গাইড আছে।

**ডিপ্লয়মেন্ট সফল কিনা যাচাই করুন:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

> **বিঃদ্রঃ:** `azd up` কমান্ড স্বয়ংক্রিয় `.env` ফাইল তৈরি করে। পরে এটি আপডেট করতে চাইলে, `.env` ফাইল ম্যানুয়ালি সম্পাদনা করতে পারেন অথবা পুনরায় তৈরি করতে পারেন:
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


## অ্যাপ্লিকেশন লোকালে চালান

**ডিপ্লয়মেন্ট যাচাই করুন:**

নিশ্চিত করুন `.env` ফাইল রুট ডিরেক্টরিতে আছে এবং Azure ক্রেডেনশিয়াল সেট করা আছে:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**অ্যাপ্লিকেশন চালু করুন:**

**অপশন ১: Spring Boot ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য সুপারিশকৃত)**

ডেভ কনটেইনারে Spring Boot ড্যাশবোর্ড এক্সটেনশন ইনক্লুড করা আছে যা সব Spring Boot অ্যাপ্লিকেশনের জন্য ভিজ্যুয়াল ইন্টারফেস দেয়। VS Code এর বামদিকের Activity Bar-এ Spring Boot আইকন দেখতে পাবেন।

Spring Boot ড্যাশবোর্ড থেকে আপনি:
- ওয়ার্কস্পেসের সব Spring Boot অ্যাপ্লিকেশন দেখতে পারবেন
- এক ক্লিকে অ্যাপ্লিকেশান শুরু/বন্দ করতে পারবেন
- রিয়েল-টাইম অ্যাপ্লিকেশন লগ দেখতে পারবেন
- অ্যাপ্লিকেশন স্ট্যাটাস মনিটর করতে পারবেন

শুধু "introduction" এর পাশে প্লে বোতামে ক্লিক করুন এই মডিউল চালানোর জন্য, অথবা একসাথে সব মডিউল শুরু করুন।

<img src="../../../translated_images/bn/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**অপশন ২: শেল স্ক্রিপ্ট ব্যবহার**

সব ওয়েব অ্যাপ্লিকেশন (মডিউল ০১-০৪) শুরু করুন:

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে রুট `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করবে এবং যদি JAR ফাইল না থাকে তাহলে বিল্ড করবে।

> **বিঃদ্রঃ:** যদি আপনি শুরু করার আগে সব মডিউল ম্যানুয়ালি বিল্ড করতে চান:
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

আপনার ব্রাউজারে http://localhost:8080 খুলুন।

**বন্ধ করতে:**

**Bash:**
```bash
./stop.sh  # এই মডিউল মাত্র
# অথবা
cd .. && ./stop-all.sh  # সব মডিউল
```

**PowerShell:**
```powershell
.\stop.ps1  # এই মডিউল শুধুমাত্র
# অথবা
cd ..; .\stop-all.ps1  # সব মডিউলগুলি
```


## অ্যাপ্লিকেশন ব্যবহার

অ্যাপ্লিকেশনটি দুইটি চ্যাট ইমপ্লিমেন্টেশন সাইট-বাই-সাইট ওয়েব ইন্টারফেস দেয়।

<img src="../../../translated_images/bn/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ড্যাশবোর্ড যা Simple Chat (stateless) এবং Conversational Chat (stateful) অপশন প্রদর্শন করে*

### Stateless চ্যাট (বাম প্যানেল)

এটি আগে চেষ্টা করুন। বলুন "My name is John" এবং সঙ্গে সঙ্গে প্রশ্ন করুন "What's my name?" মডেল মনে রাখতে পারবে না কারণ প্রতিটি মেসেজ স্বতন্ত্র। এটি বেসিক ল্যাঙ্গুয়েজ মডেল একীভূতির মূল সমস্যাটি দেখায় — কোনো কনভারসেশন প্রসঙ্গ নেই।

<img src="../../../translated_images/bn/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI পূর্ববর্তী মেসেজ থেকে আপনার নাম মনে রাখে না*

### Stateful চ্যাট (ডান প্যানেল)

এবার একই সিকোয়েন্স এখানে চেষ্টা করুন। "My name is John" বলুন এবং তারপর "What's my name?" এবার মডেল মনে রাখে। পার্থক্য হল MessageWindowChatMemory — এটি কনভারসেশন ইতিহাস রক্ষা করে এবং প্রতিটি অনুরোধের সঙ্গে পাঠায়। এভাবেই প্রোডাকশন কনভারসেশনাল AI কাজ করে।

<img src="../../../translated_images/bn/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI আগের কনভারসেশন থেকে আপনার নাম মনে রাখে*

দুই প্যানেল একই GPT-5.2 মডেল ব্যবহার করে। একমাত্র পার্থক্য মেমরি। এটি স্পষ্ট করে তোলে মেমরি আপনার অ্যাপ্লিকেশনে কি নিয়ে আসে এবং কেন বাস্তব ক্ষেত্রে এটি অপরিহার্য।

## পরবর্তী ধাপসমূহ

**পরবর্তী মডিউল:** [০২-প্রম্পট ইঞ্জিনিয়ারিং - GPT-5.2 দিয়ে প্রম্পট ইঞ্জিনিয়ারিং](../02-prompt-engineering/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল ০০ - দ্রুত শুরু](../00-quick-start/README.md) | [মুখ্যপাতায় ফেরত](../README.md) | [পরবর্তী: মডিউল ০২ - প্রম্পট ইঞ্জিনিয়ারিং →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসাধ্য সঠিকতার চেষ্টা করলেও, স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজস্ব ভাষায়ই প্রামাণিক উৎস হিসেবে বিবেচিত উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ গ্রহণ করার পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নয়।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->