# মডিউল ০১: LangChain4j দিয়ে শুরু করা

## বিষয়সূচি

- [ভিডিও ওয়াকথ্রু](../../../01-introduction)
- [আপনি যা শিখবেন](../../../01-introduction)
- [প্রাথমিক শর্তাবলী](../../../01-introduction)
- [মূল সমস্যাটি বোঝা](../../../01-introduction)
- [টোকেনগুলি বোঝা](../../../01-introduction)
- [মেমরি কিভাবে কাজ করে](../../../01-introduction)
- [কিভাবে এটি LangChain4j ব্যবহার করে](../../../01-introduction)
- [Azure OpenAI অবকাঠামো ডেপ্লয় করা](../../../01-introduction)
- [স্থানীয়ভাবে অ্যাপ্লিকেশন চালানো](../../../01-introduction)
- [অ্যাপ্লিকেশন ব্যবহার করা](../../../01-introduction)
  - [স্টেটলেস চ্যাট (বাম প্যানেল)](../../../01-introduction)
  - [স্টেটফুল চ্যাট (ডান প্যানেল)](../../../01-introduction)
- [পরবর্তী ধাপ](../../../01-introduction)

## ভিডিও ওয়াকথ্রু

এই লাইভ সেশনটি দেখুন যা এই মডিউল দিয়ে শুরু করার পদ্ধতি ব্যাখ্যা করে: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## আপনি যা শিখবেন

যদি আপনি দ্রুত শুরু শেষ করে থাকেন, আপনি দেখেছেন কীভাবে প্রম্পট পাঠানো হয় এবং উত্তর পাওয়া যায়। এটিই ভিত্তি, কিন্তু বাস্তব অ্যাপ্লিকেশনগুলোর জন্য আরও কিছু প্রয়োজন। এই মডিউলে আপনি শিখবেন কিভাবে এমন কথোপকথনমূলক AI তৈরি করতে হয় যা প্রসঙ্গ মনে রাখে এবং স্টেট বজায় রাখে — একটি এককালীন ডেমো এবং একটি প্রোডাকশন-এ প্রস্তুত অ্যাপ্লিকেশনের মধ্যে পার্থক্য।

আমরা এই গাইড জুড়ে Azure OpenAI এর GPT-5.2 ব্যবহার করব কারণ এর উন্নত যুক্তি ক্ষমতা প্যাটার্নগুলোর আচরণ স্পষ্ট করে তোলে। যখন আপনি মেমরি যোগ করবেন, তখন আপনি পার্থক্য স্পষ্টভাবে দেখতে পাবেন। এটি বুঝতে সহজ করে দেয় প্রতিটি অংশ আপনার অ্যাপ্লিকেশনে কী নিয়ে আসে।

আপনি একটি অ্যাপ্লিকেশন তৈরি করবেন যা দুইটি প্যাটার্ন প্রদর্শন করবে:

**স্টেটলেস চ্যাট** - প্রতিটি অনুরোধ স্বতন্ত্র। মডেলের পূর্বের মেসেজের কোন মেমরি নেই। এটি হলো সেই প্যাটার্ন যা আপনি দ্রুত শুরুর সময় ব্যবহার করেছিলেন।

**স্টেটফুল কথোপকথন** - প্রতিটি অনুরোধে কথোপকথনের ইতিহাস অন্তর্ভুক্ত থাকে। মডেল একাধিক টার্ন জুড়ে প্রসঙ্গ বজায় রাখে। এটাই প্রোডাকশন অ্যাপ্লিকেশনগুলোর জন্য প্রয়োজনীয়।

## প্রাথমিক শর্তাবলী

- Azure সাবস্ক্রিপশন যার মধ্যে Azure OpenAI অ্যাক্সেস আছে
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **নোট:** Java, Maven, Azure CLI এবং Azure Developer CLI (azd) দেয়া devcontainer এ প্রি-ইনস্টল করা আছে।

> **নোট:** এই মডিউল GPT-5.2 Azure OpenAI তে ব্যবহার করে। ডিপ্লয়মেন্ট `azd up` কমান্ডের মাধ্যমে স্বয়ংক্রিয়ভাবে কনফিগার করা হয় — কোডের মধ্যে মডেল নাম পরিবর্তন করবেন না।

## মূল সমস্যাটি বোঝা

ল্যাঙ্গুয়েজ মডেলগুলি স্টেটলেস। প্রতিটি API কল স্বতন্ত্র। যদি আপনি বলুন "আমার নাম জন" এবং তারপর জিজ্ঞাসা করেন "আমার নাম কী?", মডেল জানবে না আপনি সদ্য নিজেকে পরিচয় করিয়েছেন। এটি প্রতিটি অনুরোধকে মনে করে যেন এটি আপনার প্রথম কথোপকথন।

এটি সাধারণ প্রশ্নোত্তরের জন্য ঠিক, কিন্তু বাস্তব অ্যাপ্লিকেশনের জন্য অকার্যকর। কাস্টমার সার্ভিস বটগুলোকে আপনাকে যে কথা বলেছেন তা মনে রাখতে হয়। ব্যক্তিগত সহকারীকে প্রসঙ্গ প্রয়োজন। যেকোনো বহু-টার্ন কথোপকথনের জন্য মেমরি প্রয়োজন।

<img src="../../../translated_images/bn/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*স্টেটলেস (স্বতন্ত্র কল) এবং স্টেটফুল (প্রসঙ্গ-সচেতন) কথোপকথনের মধ্যে পার্থক্য*

## টোকেনগুলি বোঝা

কথোপকথনে প্রবেশ করার আগে, টোকেন সম্পর্কে বোঝা জরুরি — যেসব মৌলিক টেক্সট ইউনিট যেগুলো ল্যাঙ্গুয়েজ মডেল প্রক্রিয়াজাত করে:

<img src="../../../translated_images/bn/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*কীভাবে টেক্সট টোকেনে ভাঙ্গা হয় তার উদাহরণ - "I love AI!" চারটি আলাদা প্রক্রিয়াকরণ ইউনিট হয়ে যায়*

টোকেন হল কিভাবে AI মডেল টেক্সটের পরিমাণ মাপেন এবং প্রক্রিয়া করেন। শব্দ, বিরামচিহ্ন এবং এমনকি স্পেসও টোকেন হতে পারে। আপনার মডেলের একটি সীমা আছে কত টোকেন একসাথে প্রক্রিয়াজাত করতে পারে (GPT-5.2 এর জন্য ৪০০,০০০, যার মধ্যে সর্বোচ্চ ২৭২,০০০ ইনপুট টোকেন এবং ১২৮,০০০ আউটপুট টোকেন)। টোকেন বোঝা কথোপকথনের দৈর্ঘ্য এবং খরচ পরিচালনায় সাহায্য করে।

## মেমরি কিভাবে কাজ করে

চ্যাট মেমরি স্টেটলেস সমস্যার সমাধান করে কথোপকথনের ইতিহাস বজায় রেখে। মডেলে অনুরোধ পাঠানোর আগে, ফ্রেমওয়ার্ক প্রাসঙ্গিক পূর্ববর্তী মেসেজগুলো যুক্ত করে দেয়। যখন আপনি জিজ্ঞাসা করেন "আমার নাম কী?", তখন সিস্টেম আসলে সম্পূর্ণ কথোপকথনের ইতিহাস পাঠায়, মডেল বুঝতে পারে আপনি আগে বলেছিলেন "আমার নাম জন।"

LangChain4j মেমরি ইমপ্লিমেন্টেশন সরবরাহ করে যা এটি স্বয়ংক্রিয়ভাবে পরিচালনা করে। আপনি নির্ধারণ করবেন কত মেসেজ রাখা হবে এবং ফ্রেমওয়ার্ক প্রসঙ্গ উইন্ডো পরিচালনা করবে।

<img src="../../../translated_images/bn/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory সাম্প্রতিক মেসেজের একটি স্লাইডিং উইন্ডো বজায় রাখে, স্বয়ংক্রিয়ভাবে পুরনোগুলো ফেলে দেয়*

## কিভাবে এটি LangChain4j ব্যবহার করে

এই মডিউল দ্রুত শুরু সম্প্রসারিত করে Spring Boot ইন্টিগ্রেট করে এবং কথোপকথন মেমরি যোগ করে। উপাদানগুলো কিভাবে একত্রে যায়:

**ডিপেন্ডেন্সি** - দুইটি LangChain4j লাইব্রেরি যোগ করুন:

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

**চ্যাট মডেল** - Azure OpenAI কে Spring বীন হিসাবে কনফিগার করুন ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

বিল্ডার পরিবেশের `azd up` দ্বারা সেট করা এনভায়রনমেন্ট ভেরিয়েবল থেকে ক্রেডেনশিয়াল পড়ে। `baseUrl` Azure এন্ডপয়েন্টে সেট করলে OpenAI ক্লায়েন্ট Azure OpenAI এর সাথে কাজ করে।

**কথোপকথন মেমরি** - MessageWindowChatMemory দিয়ে চ্যাট ইতিহাস ট্র্যাক করুন ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` দিয়ে সর্বশেষ ১০টি মেসেজ রাখার জন্য মেমরি তৈরি করুন। ব্যবহারকারী এবং AI মেসেজ `UserMessage.from(text)` এবং `AiMessage.from(text)` রূপে যোগ করুন। ঐতিহাসিক মেসেজ `memory.messages()` দিয়ে আনুন এবং মডেলে পাঠান। সার্ভিস প্রতিটি কথোপকথনের জন্য আলাদা মেমরি ইনস্ট্যান্স সংরক্ষণ করে, ফলে একাধিক ব্যবহারকারী একই সময়ে চ্যাট করতে পারে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) খুলুন এবং জিজ্ঞাসা করুন:
> - "MessageWindowChatMemory উইন্ডো পূর্ণ হলে কোন মেসেজগুলো ফেলার সিদ্ধান্ত কীভাবে নেয়?"
> - "কেন আমি কাস্টম মেমরি স্টোরেজ ইন-মেমরি নয়, ডাটাবেস ব্যবহার করে ইমপ্লিমেন্ট করতে পারি?"
> - "পুরনো কথোপকথন সারাংশ করার জন্য আমি কিভাবে সামারি যুক্ত করব?"

স্টেটলেস চ্যাট এন্ডপয়েন্ট মেমরি সম্পূর্ণ এড়িয়ে চলে — শুধু `chatModel.chat(prompt)` করা হয় দ্রুত শুরুর মতো। স্টেটফুল এন্ডপয়েন্ট মেমরিতে মেসেজ যুক্ত করে, ইতিহাস আনয়ন করে এবং প্রসঙ্গ প্রতিটি অনুরোধের সাথে যোগ করে। একই মডেল কনফিগারেশন, বিভিন্ন প্যাটার্ন।

## Azure OpenAI অবকাঠামো ডেপ্লয় করা

**Bash:**
```bash
cd 01-introduction
azd up  # সাবস্ক্রিপশন এবং অবস্থান নির্বাচন করুন (eastus2 সুপারিশ করা হয়)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # সদস্যতা এবং অবস্থান নির্বাচন করুন (eastus2 সুপারিশ করা হয়)
```

> **নোট:** যদি টাইমআউট ত্রুটি পান (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), আবার `azd up` চালান। Azure রিসোর্সগুলি ব্যাকগ্রাউন্ডে প্রোভিশনিং করতে পারে, পুনরায় চেষ্টা করলে ডিপ্লয়মেন্ট সম্পন্ন হবে যখন রিসোর্স টার্মিনাল অবস্থায় পৌঁছাবে।

এটি:
১. GPT-5.2 এবং text-embedding-3-small মডেলসহ Azure OpenAI রিসোর্স ডিপ্লয় করবে
২. প্রোজেক্ট রুটে স্বয়ংক্রিয়ভাবে `.env` ফাইল তৈরি করবে ক্রেডেনশিয়ালসহ
৩. সমস্ত প্রয়োজনীয় এনভায়রনমেন্ট ভেরিয়েবল সেট আপ করবে

**ডিপ্লয়মেন্টে সমস্যা হচ্ছে?** অতিরিক্ত সমস্যা সমাধান, সাবডোমেইন নাম সংঘাত, ম্যানুয়াল Azure পোর্টাল ডিপ্লয়মেন্ট ধাপ, এবং মডেল কনফিগারেশন নির্দেশনার জন্য [Infrastructure README](infra/README.md) দেখুন।

**ডিপ্লয়মেন্ট সফল হয়েছে কিনা যাচাই করুন:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ইত্যাদি দেখানো উচিত।
```

> **নোট:** `azd up` কমান্ড স্বয়ংক্রিয়ভাবে `.env` ফাইল তৈরি করে। পরবর্তীতে এটি আপডেট করতে চাইলে, আপনি ম্যানুয়ালি `.env` এডিট করতে পারেন অথবা আবার তৈরি করতে পারেন:
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


## স্থানীয়ভাবে অ্যাপ্লিকেশন চালানো

**ডিপ্লয়মেন্ট যাচাই করুন:**

`জড়ি` ডিরেক্টরিতে `.env` ফাইলটি যাচাই করুন যাতে Azure ক্রেডেনশিয়াল আছে:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT প্রদর্শন করা উচিত
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT প্রদর্শন করা উচিত
```

**অ্যাপ্লিকেশন শুরু করুন:**

**বিকল্প ১: Spring Boot ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য প্রস্তাবিত)**

ডেভ কন্টেইনারে Spring Boot ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত আছে, যা একটি ভিজ্যুয়াল ইন্টারফেস প্রদান করে সব Spring Boot অ্যাপ্লিকেশন ম্যানেজ করতে। VS Code এর Activity Bar (বাম পাশে) থেকে Spring Boot আইকনটি দেখুন।

Spring Boot ড্যাশবোর্ড থেকে আপনি:
- ওয়ার্কস্পেসের সব Spring Boot অ্যাপ্লিকেশন দেখতে পারবেন
- একটি ক্লিকেই শুরু/বন্ধ করতে পারবেন
- রিয়েল-টাইমে লগ দেখতে পারবেন
- অ্যাপ্লিকেশনের স্ট্যাটাস দেখবেন

শুধুমাত্র "introduction" পাশে প্লে বোতামটি ক্লিক করে মডিউলটি শুরু করুন, অথবা সব মডিউল একসাথে চালু করুন।

<img src="../../../translated_images/bn/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার**

সব ওয়েব অ্যাপ্লিকেশন শুরু করুন (মডিউল ০১-০৪):

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

অথবা শুধুমাত্র এই মডিউল শুরু করুন:

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে মূল `.env` থেকে এনভায়রনমেন্ট ভেরিয়েবল লোড করে এবং যদি JAR ফাইল না থাকে তবে তা তৈরি করবে।

> **নোট:** আপনি চাইলে শুরু করার আগে সব মডিউল ম্যানুয়ালি বিল্ড করতে পারেন:
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
./stop.sh  # এই মডিউল শুধুমাত্র
# অথবা
cd .. && ./stop-all.sh  # সব মডিউলগুলি
```

**PowerShell:**
```powershell
.\stop.ps1  # এই মডিউল শুধুমাত্র
# অথবা
cd ..; .\stop-all.ps1  # সমস্ত মডিউলসমূহ
```


## অ্যাপ্লিকেশন ব্যবহার করা

অ্যাপ্লিকেশনটি দুটি চ্যাট ইমপ্লিমেন্টেশন পাশাপাশি ওয়েব ইন্টারফেস প্রদান করে।

<img src="../../../translated_images/bn/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ড্যাশবোর্ডে Simple Chat (স্টেটলেস) এবং Conversational Chat (স্টেটফুল) অপশন উভয় দেখানো হয়েছে*

### স্টেটলেস চ্যাট (বাম প্যানেল)

প্রথমে এটি চেষ্টা করুন। "My name is John" বলুন এবং সঙ্গে সঙ্গে জিজ্ঞাসা করুন "What's my name?" মডেল মনে রাখবে না কারণ প্রতিটি মেসেজ স্বতন্ত্র। এটি মৌলিক ল্যাঙ্গুয়েজ মডেল ইন্টিগ্রেশনের মূল সমস্যাটি প্রদর্শন করে — কথোপকথনের প্রসঙ্গ নেই।

<img src="../../../translated_images/bn/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI আপনার আগের মেসেজ থেকে নাম মনে রাখে না*

### স্টেটফুল চ্যাট (ডান প্যানেল)

এবার একই সিরিজ এখানে চেষ্টা করুন। "My name is John" বলুন এবং "What's my name?" জিজ্ঞাসা করুন। এবার এটি মনে রাখে। পার্থক্য হলো MessageWindowChatMemory — এটি কথোপকথনের ইতিহাস বজায় রাখে এবং প্রতিটি অনুরোধের সঙ্গে সেটি অন্তর্ভুক্ত করে। এভাবেই উৎপাদন কথোপকথনমূলক AI কাজ করে।

<img src="../../../translated_images/bn/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI কথোপকথনের আগের অংশ থেকে আপনার নাম মনে রাখে*

উভয় প্যানেল একই GPT-5.2 মডেল ব্যবহার করে। মাত্র পার্থক্য হলো মেমরি। এটি স্পষ্ট করে তোলে মেমরি কী নিয়ে আসে আপনার অ্যাপ্লিকেশনে এবং বাস্তব ব্যবহারের জন্য কেন এটি অপরিহার্য।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [02-prompt-engineering - GPT-5.2 দিয়ে প্রম্পট ইঞ্জিনিয়ারিং](../02-prompt-engineering/README.md)

---

**নেভিগেশন:** [← আগে: মডিউল ০০ - দ্রুত শুরু](../00-quick-start/README.md) | [প্রধান পৃষ্ঠায় ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০২ - প্রম্পট ইঞ্জিনিয়ারিং →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**দায়বদ্ধতা পরিত্রাণ**:
এই নথিটি এআই অনুবাদ সেবা [কো-অপ ট্রান্সলেটর](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতিগুলি থাকতে পারে বলে দয়া করে মনোযোগ দিন। মূল নথিটি তার মাতৃভাষায় সর্বাধিক সরকারী উৎস হিসাবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের ক্ষেত্রে পেশাদার মানব অনুবাদ প্রয়োজন। এই অনুবাদের ব্যবহারে যে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->