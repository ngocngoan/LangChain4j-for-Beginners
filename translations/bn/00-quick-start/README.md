# মডিউল ০০: দ্রুত শুরু

## বিষয়বস্তু সূচি

- [পরিচিতি](../../../00-quick-start)
- [LangChain4j কি?](../../../00-quick-start)
- [LangChain4j নির্ভরশীলতা](../../../00-quick-start)
- [প্রয়োজনীয়তা](../../../00-quick-start)
- [সেটআপ](../../../00-quick-start)
  - [১। আপনার গিটহাব টোকেন পান](../../../00-quick-start)
  - [২। আপনার টোকেন সেট করুন](../../../00-quick-start)
- [উদাহরণ চালান](../../../00-quick-start)
  - [১। মৌলিক চ্যাট](../../../00-quick-start)
  - [২। প্রম্পট প্যাটার্ন](../../../00-quick-start)
  - [৩। ফাংশন কলিং](../../../00-quick-start)
  - [৪। ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)](../../../00-quick-start)
  - [৫। দায়িত্বশীল AI](../../../00-quick-start)
- [প্রতি উদাহরণ কি দেখায়](../../../00-quick-start)
- [পরবর্তী পদক্ষেপ](../../../00-quick-start)
- [সমস্যা সমাধান](../../../00-quick-start)

## পরিচিতি

এই দ্রুত শুরু নির্দেশিকা আপনাকে LangChain4j দিয়ে যত দ্রুত সম্ভব শুরু করতে সাহায্য করার জন্য তৈরি। এটি LangChain4j এবং GitHub মডেল সহ AI অ্যাপ্লিকেশন তৈরি করার মৌলিক বিষয়গুলোকে কভার করে। পরবর্তী মডিউলগুলিতে আপনি Azure OpenAI এবং GPT-5.2 এ স্যুইচ করবেন এবং প্রতিটি ধারণার বিষয়ে আরও গভীরে যাবেন।

## LangChain4j কি?

LangChain4j হল একটি জাভা লাইব্রেরি যা AI-চালিত অ্যাপ্লিকেশন তৈরিকে সহজ করে তোলে। HTTP ক্লায়েন্ট এবং JSON পার্সিং নিয়ে কাজ করার পরিবর্তে, আপনি পরিষ্কার জাভা API দিয়ে কাজ করবেন।

LangChain এর "chain" মানে হল একাধিক উপাদানকে শৃঙ্খলায় বাধা—আপনি হয়ত একটি প্রম্পটকে মডেল ও তারপর পার্সারে শৃঙ্খলিত করবেন, বা একাধিক AI কলকে শৃঙ্খলিত করবেন যেখানে একটি আউটপুট পরবর্তী ইনপুটে দেয়। এই দ্রুত শুরু ধাপে আমরা প্রাথমিক বিষয়গুলো নিয়ে কাজ করব, জটিল শৃঙ্খলাগুলো পরবর্তীতে দেখতে পাব।

<img src="../../../translated_images/bn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-তে উপাদান শৃঙ্খলা - নির্মাণ ব্লকগুলো সংযুক্ত হয়ে শক্তিশালী AI ওয়ার্কফ্লো তৈরি করে*

আমরা তিনটি মূল উপাদান ব্যবহার করব:

**ChatModel** - AI মডেল ইন্টারঅ্যাকশনের ইন্টারফেস। `model.chat("prompt")` কল করুন এবং একটি উত্তর স্ট্রিং পান। আমরা `OpenAiOfficialChatModel` ব্যবহার করি যা GitHub মডেলের মতো OpenAI-সঙ্গত API এ কাজ করে।

**AiServices** - টাইপ-সেফ AI সার্ভিস ইন্টারফেস তৈরি করে। পদ্ধতি নির্ধারণ করুন, `@Tool` দিয়ে চিহ্নিত করুন, এবং LangChain4j সামঞ্জস্যকরণ পরিচালনা করে। AI প্রয়োজন অনুসারে আপনার জাভা মেথডগুলো স্বয়ংক্রিয়ভাবে কল করে।

**MessageWindowChatMemory** - কথোপকথনের ইতিহাস বজায় রাখে। এটি ছাড়া প্রতিটি অনুরোধ স্বাধীন। এর মাধ্যমে AI পূর্বের মেসেজগুলো মনে রাখে এবং একাধিক দফায় প্রসঙ্গ বজায় রাখে।

<img src="../../../translated_images/bn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j স্থাপত্য - মূল উপাদান একসাথে কাজ করে আপনার AI অ্যাপ্লিকেশন চালায়*

## LangChain4j নির্ভরশীলতা

এই দ্রুত শুরুতে তিনটি Maven নির্ভরশীলতা ব্যবহৃত হয় [`pom.xml`](../../../00-quick-start/pom.xml) এ:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```


`langchain4j-open-ai-official` মডিউলটি `OpenAiOfficialChatModel` ক্লাস প্রদান করে যা OpenAI-সঙ্গত API গুলোর সাথে সংযোগ স্থাপন করে। GitHub মডেল একই API ফরম্যাট ব্যবহার করে, তাই বিশেষ অ্যাডাপ্টারের দরকার নেই — শুধু বেস URL `https://models.github.ai/inference` এ নির্দেশ করুন।

`langchain4j-easy-rag` মডিউলটি স্বয়ংক্রিয় ডকুমেন্ট বিভাজন, এমবেডিং এবং রিট্রিভাল সরবরাহ করে তাই আপনি প্রতিটি ধাপ ম্যানুয়ালি কনফিগার না করেও RAG অ্যাপ্লিকেশন তৈরি করতে পারেন।

## প্রয়োজনীয়তা

**ডেভ কনটেইনার ব্যবহার করছেন?** Java এবং Maven ইতিমধ্যেই ইনস্টল করা আছে। আপনি শুধু একটি GitHub Personal Access Token প্রয়োজন।

**লোকাল ডেভেলপমেন্ট:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (নিচের নির্দেশনা অনুসরণ করুন)

> **দ্রষ্টব্য:** এই মডিউল GitHub মডেলের `gpt-4.1-nano` ব্যবহার করে। কোডে মডেল নাম পরিবর্তন করবেন না - এটি GitHub এর উপলব্ধ মডেলের সাথে কাজ করার জন্য কনফিগার করা।

## সেটআপ

### ১। আপনার গিটহাব টোকেন পান

1. যান [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. ক্লিক করুন "Generate new token"
3. একটি বর্ণনামূলক নাম দিন (যেমন "LangChain4j Demo")
4. মেয়াদ নির্ধারণ করুন (৭ দিন সুপারিশকৃত)
5. "Account permissions" এ "Models" এর অধীনে "Read-only" নির্বাচন করুন
6. ক্লিক করুন "Generate token"
7. আপনার টোকেন কপি করে সংরক্ষণ করুন — এটি আর দেখানো হবে না

### ২। আপনার টোকেন সেট করুন

**অপশন ১: VS Code ব্যবহার করে (সুপারিশকৃত)**

আপনি যদি VS Code ব্যবহার করেন, তাহলে প্রজেক্ট রুটের `.env` ফাইলে আপনার টোকেন যুক্ত করুন:

যদি `.env` ফাইল না থাকে, `.env.example` থেকে কপি করে `.env` তৈরি করুন অথবা নতুন `.env` ফাইল তৈরি করুন।

**উদাহরণ `.env` ফাইল:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env এ
GITHUB_TOKEN=your_token_here
```


তারপর আপনি এক্সপ্লোরারে যেকোনো ডেমো ফাইলে (যেমন `BasicChatDemo.java`) রাইট-ক্লিক করে **"Run Java"** নির্বাচন করতে পারেন অথবা রান ও ডিবাগ প্যানেল থেকে লঞ্চ কনফিগারেশন ব্যবহার করুন।

**অপশন ২: টার্মিনাল ব্যবহার করে**

টোকেনকে পরিবেশ ভেরিয়েবল হিসেবে সেট করুন:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```


**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```


## উদাহরণ চালান

**VS Code ব্যবহার করে:** এক্সপ্লোরারে যেকোনো ডেমো ফাইলে রাইট-ক্লিক করে **"Run Java"** নির্বাচন করুন, অথবা রান ও ডিবাগ প্যানেলের লঞ্চ কনফিগারেশন ব্যবহার করুন (প্রথমে অবশ্যই `.env` ফাইলে টোকেন যুক্ত করতে হবে)।

**Maven ব্যবহার করে:** বিকল্পভাবে, কমান্ড লাইন থেকে চালাতে পারেন:

### ১। মৌলিক চ্যাট

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```


**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```


### ২। প্রম্পট প্যাটার্ন

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```


**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```


শো করে শূন্য-শট, কয়েক-শট, চিন্তার শৃঙ্খলা, এবং ভূমিকা-ভিত্তিক প্রম্পটিং।

### ৩। ফাংশন কলিং

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```


**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```


AI প্রয়োজন হলে স্বয়ংক্রিয়ভাবে আপনার জাভা পদ্ধতিগুলো কল করে।

### ৪। ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```


**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```


সহজ RAG ব্যবহার করে ডকুমেন্ট সম্পর্কে প্রশ্ন করুন যেখানে স্বয়ংক্রিয় এমবেডিং ও পুনরুদ্ধার করে।

### ৫। দায়িত্বশীল AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```


**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```


দেখুন কীভাবে AI নিরাপত্তা ফিল্টার ক্ষতিকর বিষয়বস্তু ব্লক করে।

## প্রতি উদাহরণ কি দেখায়

**মৌলিক চ্যাট** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

এখান থেকে শুরু করুন LangChain4j-কে সবচেয়ে সরল রূপে দেখতে। আপনি `OpenAiOfficialChatModel` তৈরি করবেন, `.chat()` এর মাধ্যমে একটি প্রম্পট পাঠাবেন, এবং উত্তর পাবেন। এটি ভিত্তি প্রদর্শন করে: কাস্টম এন্ডপয়েন্ট ও API কী নিয়ে মডেলগুলি কীভাবে ইনিশিয়ালাইজ করতে হয়। এই প্যাটার্ন বুঝলে বাকি সব সহজ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```


> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat দিয়ে চেষ্টা করুন:** খুলুন [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) এবং জিজ্ঞাসা করুন:
> - "এই কোডে GitHub মডেল থেকে Azure OpenAI এ কীভাবে স্যুইচ করব?"
> - "OpenAiOfficialChatModel.builder() এ আর কোন প্যারামিটার কনফিগার করা যায়?"
> - "পূর্ণ উত্তর পাওয়ার পরিবর্তে কীভাবে স্ট্রিমিং রেসপন্স যুক্ত করব?"

**প্রম্পট ইঞ্জিনিয়ারিং** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

এখন আপনি বুঝলেন কিভাবে মডেলের সাথে কথা বলতে হয়, চলুন দেখি কী বলবেন। এই ডেমো একই মডেল সেটআপ ব্যবহার করে কিন্তু পাঁচটি ভিন্ন প্রম্পট প্যাটার্ন দেখায়। সরাসরি নির্দেশনার জন্য শূন্য-শট প্রম্পট, উদাহরণের মাধ্যমে শেখার জন্য কয়েক-শট, যুক্তি প্রকাশের জন্য চিন্তার শৃঙ্খলা, এবং প্রসঙ্গ স্থাপনার জন্য ভূমিকা-ভিত্তিক প্রম্পট চেষ্টা করুন। আপনি দেখবেন একই মডেল আপনার রিকোয়েস্ট ফ্রেম করার উপর ভিত্তি করে ভিন্ন ফলাফল দেয়।

ডেমো প্রম্পট টেমপ্লেটও দেখায়, যা ভেরিয়েবল সহ পুনরায় ব্যবহারযোগ্য প্রম্পট তৈরি করার শক্তিশালী উপায়।

নিচের উদাহরণ LangChain4j এর `PromptTemplate` ব্যবহার করে ভেরিয়েবল ভর্তি করার প্রম্পট দেখায়। AI প্রদত্ত গন্তব্য ও কার্যকলাপের ভিত্তিতে উত্তর দেবে।

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


> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat দিয়ে চেষ্টা করুন:** খুলুন [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) এবং জিজ্ঞাসা করুন:
> - "শূন্য-শট ও কয়েক-শট প্রম্পটিংয়ের মধ্যে পার্থক্য কী, এবং কখন কোনটি ব্যবহার করব?"
> - "মডেলের উত্তরগুলিতে তাপমাত্রা প্যারামিটার কীভাবে প্রভাব ফেলে?"
> - "প্রম্পট ইনজেকশন আক্রমণ রোধে কী কৌশল প্রয়োগ করা যায়?"
> - "সাধারণ প্যাটার্নের জন্য পুনরায় ব্যবহারযোগ্য PromptTemplate অবজেক্ট কিভাবে তৈরি করব?"

**টুল ইন্টিগ্রেশন** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

এখানেই LangChain4j শক্তিশালী হয়। আপনি `AiServices` ব্যবহার করবেন একটি AI সহকারী তৈরি করতে যা আপনার জাভা মেথড কল করতে পারে। শুধু আরোপ করুন মেথডগুলোতে `@Tool("বর্ণনা")` এবং LangChain4j বাকি সামলায়—AI স্বয়ংক্রিয়ভাবে ব্যবহারকারীর প্রশ্ন অনুযায়ী যেই টুল দরকার সেই টুল ব্যবহার করে। এটি ফাংশন কলিং দেখায়, একটি গুরুত্বপূর্ণ কৌশল যা AI কে শুধুমাত্র প্রশ্নের উত্তর দেওয়ার চেয়ে কাজ নিতে সক্ষম করে।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```


> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat দিয়ে চেষ্টা করুন:** খুলুন [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) এবং জিজ্ঞাসা করুন:
> - "@Tool অ্যানোটেশন কীভাবে কাজ করে এবং LangChain4j পেছনে কী করে?"
> - "AI কি একাধিক টুল সিকোয়েন্সে কল করে জটিল সমস্যার সমাধান করতে পারে?"
> - "যদি কোন টুল এক্সসেপশন ছুড়ে, কীভাবে ত্রুটি পরিচালনা করব?"
> - "এই ক্যালকুলেটর উদাহরণের পরিবর্তে বাস্তব API কিভাবে ইন্টিগ্রেট করব?"

**ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

এখানে আপনি RAG (রিকভারি-অগমেন্টেড জেনারেশন) দেখতে পাবেন LangChain4j এর "সহজ RAG" পদ্ধতির মাধ্যমে। ডকুমেন্ট লোড করা হয়, স্বয়ংক্রিয়ভাবে বিভক্ত এবং ইন-মেমোরি স্টোরে এমবেড করা হয়, তারপর বিষয়বস্তু পুনরুদ্ধারকারী AI কে উপযুক্ত অংশ সরবরাহ করে প্রশ্নের সময়। AI আপনার ডকুমেন্টভিত্তিক উত্তর দেয়, তার সাধারন জ্ঞানের উপর নয়।

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```


> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat দিয়ে চেষ্টা করুন:** খুলুন [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) এবং জিজ্ঞাসা করুন:
> - "RAG AI হ্যালুসিনেশন কীভাবে প্রতিরোধ করে সাধারণ মডেলের প্রশিক্ষণ তথ্য ব্যবহার করার তুলনায়?"
> - "এই সহজ পদ্ধতির এবং একটি কাস্টম RAG পাইপলাইনের মধ্যে পার্থক্য কী?"
> - "একাধিক ডকুমেন্ট অথবা বড় জ্ঞানের ভাণ্ডার পরিচালনার জন্য এটি কিভাবে স্কেল করব?"

**দায়িত্বশীল AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

নিরাপত্তা স্তর দিয়ে AI তৈরি করুন। এই ডেমোটি দুইটি প্রটেকশন স্তর দেখায় একসাথে কাজ করছে:

**পর্ব ১: LangChain4j ইনপুট গার্ডরেইলস** - LLM এ পৌঁছানোর আগে বিপজ্জনক প্রম্পট ব্লক করে। নিজস্ব কাস্টম গার্ডরেইল তৈরি করুন যা নিষিদ্ধ কীওয়ার্ড বা প্যাটার্ন চেক করে। এগুলো আপনার কোডে চলবে, তাই দ্রুত এবং ফ্রি।

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```


**পর্ব ২: প্রদানকারী নিরাপত্তা ফিল্টার** - GitHub মডেলের নির্মিত ফিল্টার যা আপনার গার্ডরেইল মিস করা বিষয় ধরে ফেলে। আপনি কঠোর ব্লক (HTTP 400 ত্রুটি) এবং নম্র প্রত্যাখ্যান দেখবেন যেখানে AI ভদ্রতার সাথে প্রত্যাখ্যান করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat দিয়ে চেষ্টা করুন:** খুলুন [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) এবং জিজ্ঞাসা করুন:
> - "InputGuardrail কি এবং আমি কিভাবে নিজস্ব তৈরি করব?"
> - "কঠোর ব্লক এবং নম্র প্রত্যাখ্যানের মধ্যে পার্থক্য কী?"
> - "গার্ডরেইল এবং প্রদানকারী ফিল্টার একসাথে কেন ব্যবহার করব?"

## পরবর্তী পদক্ষেপ

**পরবর্তী মডিউল:** [০১-পরিচিতি - LangChain4j দিয়ে শুরু](../01-introduction/README.md)

---

**নেভিগেশন:** [← প্রধান পৃষ্ঠায় ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০১ - পরিচিতি →](../01-introduction/README.md)

---

## সমস্যা সমাধান

### প্রথমবার Maven বিল্ড

**সমস্যা:** প্রাথমিক `mvn clean compile` অথবা `mvn package` অনেক সময় নেয় (১০-১৫ মিনিট)

**কারণ:** Maven প্রথমবারে সকল নির্ভরশীলতা (Spring Boot, LangChain4j লাইব্রেরি, Azure SDK ইত্যাদি) ডাউনলোড করে।

**সমাধান:** এটি স্বাভাবিক। পরবর্তীতে বিল্ডগুলো দ্রুত হবে কারণ নির্ভরশীলতাগুলো লোকালি ক্যাশ থাকবে। ডাউনলোড সময় আপনার নেটওয়ার্ক স্পিডের উপর নির্ভরশীল।

### PowerShell Maven কমান্ড সিনট্যাক্স

**সমস্যা:** Maven কমান্ড `Unknown lifecycle phase ".mainClass=..."` ত্রুটিতে ব্যর্থ হয়।
**কারণ**: PowerShell `=` কে একটি ভেরিয়েবল এসাইনমেন্ট অপারেটর হিসেবে ব্যাখ্যা করে, যা Maven এর প্রোপার্টি সিনট্যাক্স ভাঙ্গে

**সমাধান**: Maven কমান্ডের আগে stop-parsing অপারেটর `--%` ব্যবহার করুন:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` অপারেটর PowerShell কে বলে বাকি সমস্ত আর্গুমেন্টগুলো Maven-এ লিটারালি পাঠাতে, ব্যাখ্যা না করে।

### Windows PowerShell ইমোজি প্রদর্শন

**সমস্যা**: PowerShell এ AI রেসপন্সে ইমোজির পরিবর্তে কীটপতঙ্গ অক্ষর (যেমন, `????` বা `â??`) দেখা যাচ্ছে

**কারণ**: PowerShell এর ডিফল্ট এনকোডিং UTF-8 ইমোজি সাপোর্ট করে না

**সমাধান**: Java অ্যাপ্লিকেশন চালানোর আগে এই কমান্ডটি রান করুন:
```cmd
chcp 65001
```

এটি টার্মিনালে UTF-8 এনকোডিং জোরপূর্বক সক্রিয় করে। বিকল্প হিসাবে, Windows Terminal ব্যবহার করুন যার ইউনিকোড সাপোর্ট আরও ভালো।

### API কল ডিবাগিং

**সমস্যা**: অথেন্টিকেশন এরর, রেট লিমিট, বা AI মডেল থেকে অপ্রত্যাশিত রেসপন্স

**সমাধান**: উদাহরণগুলোতে `.logRequests(true)` এবং `.logResponses(true)` অন্তর্ভুক্ত আছে যা কনসোলে API কলগুলো প্রদর্শন করে। এটি অথেন্টিকেশন এরর, রেট লিমিট, বা অপ্রত্যাশিত রেসপন্স সমাধানে সাহায্য করে। প্রোডাকশনে লগ গোলমাল কমাতে এই ফ্ল্যাগগুলো সরিয়ে দিন।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**দায়িত্ব পরিহার**:
এই নথি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা ভুল থাকতে পারে। মূল নথি তার স্বাভাবিক ভাষায় কর্তৃত্বপ্রাপ্ত উৎস হিসেবে বিবেচিত হবে। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদ ব্যবহারে কোনো ভুলবোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী থাকবো না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->