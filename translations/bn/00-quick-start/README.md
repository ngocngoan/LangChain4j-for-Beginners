# Module 00: দ্রুত শুরু

## বিষয়বস্তু সূচি

- [পরিচিতি](../../../00-quick-start)
- [LangChain4j কি?](../../../00-quick-start)
- [LangChain4j নির্ভরতা](../../../00-quick-start)
- [প্রয়োজনীয়তা](../../../00-quick-start)
- [সেটআপ](../../../00-quick-start)
  - [1. আপনার GitHub টোকেন পান](../../../00-quick-start)
  - [2. আপনার টোকেন সেট করুন](../../../00-quick-start)
- [উদাহরণসমূহ চালান](../../../00-quick-start)
  - [1. বেসিক চ্যাট](../../../00-quick-start)
  - [2. প্রম্পট প্যাটার্ন](../../../00-quick-start)
  - [3. ফাংশন কলিং](../../../00-quick-start)
  - [4. ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)](../../../00-quick-start)
  - [5. দায়িত্বশীল AI](../../../00-quick-start)
- [প্রত্যেক উদাহরণ কী দেখায়](../../../00-quick-start)
- [পরবর্তী পদক্ষেপ](../../../00-quick-start)
- [সমস্যা সমাধান](../../../00-quick-start)

## পরিচিতি

এই দ্রুত শুরু আপনার LangChain4j ব্যবহার শুরু করার জন্য যতটা সম্ভব দ্রুত আপনাকে প্রস্তুত করতে তৈরি করা হয়েছে। এটি LangChain4j এবং GitHub মডেলের সঙ্গে AI অ্যাপ্লিকেশন তৈরির নূন্যতম বিষয়গুলি অন্তর্ভুক্ত করে। পরবর্তী মডিউলগুলোতে আপনি LangChain4j এর সঙ্গে Azure OpenAI ব্যবহার করে উন্নত অ্যাপ্লিকেশন তৈরি করবেন।

## LangChain4j কি?

LangChain4j হল একটি জাভা লাইব্রেরি যা AI চালিত অ্যাপ্লিকেশন তৈরি সহজ করে তোলে। HTTP ক্লায়েন্ট এবং JSON পার্স করার ঝামেলা না নিয়ে, আপনি পরিষ্কার জাভা API ব্যবহার করেন। 

LangChain এর "চেইন" অর্থ একাধিক উপাদান একে অপরের সাথে যুক্ত করা — আপনি একটি প্রম্পট মডেলে, মডেল থেকে পার্সারে চেইন করতে পারেন অথবা একাধিক AI কল একসঙ্গে যেখানে একটি আউটপুট পরবর্তী ইনপুটে পৌঁছায়। এই দ্রুত শুরু মৌলিক ধারণা নিয়ে কাজ করে, আরও জটিল চেইন পরীক্ষা করার আগে।

<img src="../../../translated_images/bn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-তে উপাদানগুলো একত্রিত হয়ে শক্তিশালী AI ওয়ার্কফ্লো গঠন করে*

আমরা তিনটি মূল উপাদান ব্যবহার করব:

**ChatModel** - AI মডেলের সাথে সংযোগের ইন্টারফেস। `model.chat("prompt")` কল করে একটি রেসপন্স সাইন্টিং পাবেন। আমরা `OpenAiOfficialChatModel` ব্যবহার করি, যা GitHub মডেলের মত OpenAI-সঙ্গত এন্ডপয়েন্টের সাথে কাজ করে।

**AiServices** - টাইপ-সেফ AI সেবা ইন্টারফেস তৈরি করে। মেথড ডিফাইন করুন, `@Tool` এর মাধ্যমে অ্যানোটেট করুন এবং LangChain4j ঐতিহাসিক ব্যবস্থাপনার কাজ করে। AI প্রয়োজন অনুযায়ী স্বয়ংক্রিয়ভাবে আপনার জাভা মেথড কল করে।

**MessageWindowChatMemory** - সংলাপ ইতিহাস সংরক্ষণ করে। এটি না থাকলে প্রতিটি অনুরোধ স্বাধীন থাকে। থাকলে AI পূর্ববর্তী বার্তা মনে রাখে এবং একাধিক টার্নের মাঝে প্রসঙ্গ বজায় রাখে।

<img src="../../../translated_images/bn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j স্থাপত্য - মূল উপাদান একসঙ্গে কাজ করে আপনার AI অ্যাপ্লিকেশন চালায়*

## LangChain4j নির্ভরতা

এই দ্রুত শুরুতে তিনটি Maven নির্ভরতা ব্যবহৃত হয়েছে [`pom.xml`](../../../00-quick-start/pom.xml) এ:

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

`langchain4j-open-ai-official` মডিউলটি `OpenAiOfficialChatModel` ক্লাস প্রদান করে যা OpenAI-সঙ্গত API গুলোর সাথে সংযুক্ত হয়। GitHub Models একই API ফর্ম্যাট ব্যবহার করে, অতএব আলাদা এডাপ্টর দরকার নেই — শুধু বেস URL সেট করুন `https://models.github.ai/inference`।

`langchain4j-easy-rag` মডিউলটি স্বয়ংক্রিয় ডকুমেন্ট বিভাজন, এম্বেডিং এবং পুনরুদ্ধার প্রদান করে, যাতে আপনি ম্যানুয়ালি প্রতিটি ধাপ কনফিগার না করেই RAG অ্যাপ্লিকেশন তৈরি করতে পারেন।

## প্রয়োজনীয়তা

**ডেভ কন্টেইনার ব্যবহার করছেন?** জাভা এবং Maven ইতিমধ্যে ইনস্টল করা আছে। আপনার শুধু একটি GitHub Personal Access Token দরকার।

**লোকাল ডেভেলপমেন্ট:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (নিচের নির্দেশ অনুসরণ করুন)

> **দ্রষ্টব্য:** এই মডিউল GitHub Models থেকে `gpt-4.1-nano` ব্যবহার করে। কোডে মডেল নাম পরিবর্তন করবেন না - এটি GitHub এর বিদ্যমান মডেলের জন্য কনফিগার করা।

## সেটআপ

### 1. আপনার GitHub টোকেন পান

1. যান [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" ক্লিক করুন
3. একটি বর্ণনামূলক নাম দিন (যেমন, "LangChain4j Demo")
4. মেয়াদ নির্ধারণ করুন (৭ দিন সুপারিশ)
5. "Account permissions" এ গিয়ে "Models" এর অধীনে "Read-only" সেট করুন
6. "Generate token" ক্লিক করুন
7. টোকেনটি কপি করে সংরক্ষণ করুন — এটি আর দেখবেন না

### 2. আপনার টোকেন সেট করুন

**অপশন ১: VS Code ব্যবহার করে (সাংখ্যিক)**

VS Code ব্যবহার করলে আপনার টোকেনটি প্রকল্পের মূল `.env` ফাইলে যোগ করুন:

যদি `.env` ফাইল না থাকে, `.env.example` থেকে `.env` কপি করুন অথবা নতুন `.env` ফাইল তৈরি করুন।

**উদাহরণ `.env` ফাইল:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env এ
GITHUB_TOKEN=your_token_here
```

এরপর যে কোনও ডেমো ফাইলে (যেমন, `BasicChatDemo.java`) Explorer এ ডানক্লিক করে **"Run Java"** নির্বাচন করতে পারেন অথবা Run এবং Debug প্যানেল থেকে লঞ্চ কনফিগারেশন ব্যবহার করুন।

**অপশন ২: টার্মিনাল ব্যবহার করে**

টোকেনটি পরিবেশ পরিবর্তনশীল হিসেবে সেট করুন:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## উদাহরণসমূহ চালান

**VS Code ব্যবহার করলে:** যে কোনও ডেমো ফাইলে ডানক্লিক করে **"Run Java"** নির্বাচন করুন অথবা Run ও Debug প্যানেল থেকে লঞ্চ কনফিগারেশন ব্যবহার করুন (অবশ্য `.env` ফাইলে আগে টোকেন যোগ করা আছে).

**Maven ব্যবহার করলে:** বিকল্পভাবে কমান্ড লাইনে চালাতে পারেন:

### 1. বেসিক চ্যাট

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. প্রম্পট প্যাটার্ন

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

জিরো-শট, ফิว-শট, চেইন-অফ-থট এবং রোল-ভিত্তিক প্রম্পট দেখায়।

### 3. ফাংশন কলিং

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI প্রয়োজনে আপনার জাভা মেথড স্বয়ংক্রিয়ভাবে কল করে।

### 4. ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

সহজ RAG দ্বারা স্বয়ংক্রিয় এম্বেডিং ও পুনরুদ্ধারের মাধ্যমে আপনার ডকুমেন্ট নিয়ে প্রশ্ন করুন।

### 5. দায়িত্বশীল AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

কীভাবে AI নিরাপত্তা ফিল্টার ক্ষতিকর বিষয়বস্তু ব্লক করে তা দেখুন।

## প্রত্যেক উদাহরণ কী দেখায়

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

এখান থেকেই শুরু করুন, LangChain4j এর সবচেয়ে সহজ উপায় দেখতে। আপনি একটি `OpenAiOfficialChatModel` তৈরি করবেন, `.chat()` দিয়ে প্রম্পট পাঠাবেন, এবং একটি জবাব পাবেন। এটি মৌলিক বিষয়গুলি প্রদর্শন করে: কাস্টম এন্ডপয়েন্ট এবং API কী দিয়ে মডেল কিভাবে আরম্ভ করবেন। একবার এই প্যাটার্ন বুঝে গেলে বাকিটা নির্মাণ সহজ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** খুলুন [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) এবং জিজ্ঞাসা করুন:
> - "কিভাবে GitHub Models থেকে Azure OpenAI তে এই কোডে পরিবর্তন করব?"
> - "OpenAiOfficialChatModel.builder() এর অন্যান্য কোন প্যারামিটার কনফিগার করা যায়?"
> - "সম্পূর্ণ রেসপন্সের পরিবর্তে স্ট্রিমিং রেসপন্স কিভাবে যোগ করব?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

এখন যে মডেলের সাথে কথা বলবেন সেটা জানেন, চলুন দেখি কী বলেন। এই ডেমো একই মডেল ব্যবহার করে, কিন্তু পাঁচটি ভিন্ন প্রম্পট প্যাটার্ন দেখায়। সরাসরি নির্দেশের জন্য জিরো-শট, উদাহরণের থেকে শেখার জন্য ফিউ-শট, কারণ প্রকাশের জন্য চেইন-অফ-থট, এবং প্রসঙ্গ স্থাপনের জন্য রোল-ভিত্তিক প্রম্পট চেষ্টা করুন। একই মডেল কিভাবে প্রেরণ পদ্ধতির ভিত্তিতে বিভিন্ন ফলাফল দেয় দেখতে পাবেন।

ডেমোটি প্রম্পট টেমপ্লেটও দেখায়, যা ভেরিয়েবল দিয়ে পুনঃব্যবহারযোগ্য প্রম্পট তৈরি করার একটি শক্তিশালী উপায়।
নিচের উদাহরণটি LangChain4j `PromptTemplate` ব্যবহার করে ভেরিয়েবল পূরণ করে। AI প্রদত্ত গন্তব্য ও কার্যক্রম ভিত্তিতে উত্তর দিবে।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** খুলুন [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) এবং জিজ্ঞাসা করুন:
> - "জিরো-শট আর ফিউ-শট prompting এর মধ্যে পার্থক্য কী, এবং কখন কোনটি ব্যবহার করব?"
> - "মডেলের রেসপন্সে তাপমাত্রা প্যারামিটার কিভাবে প্রভাব ফেলে?"
> - "পণ্যকরণে প্রম্পট ইঞ্জেকশন আক্রমণ প্রতিরোধে কিছু কৌশল কী কী?"
> - "সাধারণ প্যাটার্নের জন্য পুনঃব্যবহারযোগ্য PromptTemplate অবজেক্ট কিভাবে তৈরি করব?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

এখানেই LangChain4j শক্তিশালী হয়। আপনি `AiServices` ব্যবহার করে একটি AI অ্যাসিস্ট্যান্ট তৈরি করবেন যা আপনার জাভা মেথডগুলো কল করতে পারে। মেথড গুলো `@Tool("বর্ণনা")` দিয়ে অ্যানোটেট করুন এবং LangChain4j বাকিটা পরিচালনা করবে — AI স্বয়ংক্রিয়ভাবে ব্যবহারকারীর অনুরোধ অনুসারে টুলগুলো ব্যবহার করবে। এটি ফাংশন কলিং প্রদর্শন করে, AI কে এককভাবে প্রশ্নের উত্তর দেওয়ার পরিবর্তে পদক্ষেপ নিতে সক্ষম করে।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** খুলুন [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) এবং জিজ্ঞাসা করুন:
> - "@Tool অ্যানোটেশন কীভাবে কাজ করে এবং LangChain4j এর পেছনে কী করে?"
> - "AI কি জটিল সমস্যা সমাধানের জন্য একাধিক টুল ধারাবাহিকে কল করতে পারে?"
> - "যদি কোন টুল এক্সসেপশন দেয় - আমি কিভাবে ত্রুটি পরিচালনা করব?"
> - "এই ক্যালকুলেটর উদাহরণ বাদ দিয়ে আসল API কিভাবে যুক্ত করব?"

**ডকুমেন্ট প্রশ্নোত্তর (সহজ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

এখানে আপনি LangChain4j এর "সহজ RAG" পদ্ধতি ব্যবহার করে RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন) দেখবেন। ডকুমেন্টগুলো লোড হয়, স্বয়ংক্রিয়ভাবে বিভক্ত ও এম্বেড হয়ে একটি ইন-মেমোরি স্টোরে রাখা হয়, পরে একটি কন্টেন্ট রিট্রিভার AI কে প্রাসঙ্গিক অংশ সরবরাহ করে জিজ্ঞাসার সময়। AI কেবল আপনার ডকুমেন্ট ভিত্তিক উত্তর দেয়, সবার সাধারণ জ্ঞানের উপর নির্ভর করে না।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** খুলুন [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) এবং জিজ্ঞাসা করুন:
> - "মডেলের প্রশিক্ষণ ডেটা ব্যবহার করার তুলনায় RAG কীভাবে AI হ্যালুসিনেশন প্রতিরোধ করে?"
> - "এই সহজ পদ্ধতির এবং একটি কাস্টম RAG পাইপলাইন এর মধ্যে পার্থক্য কী?"
> - "কিভাবে আমি একাধিক ডকুমেন্ট বা বড় জ্ঞানভিত্তির জন্য এটি স্কেল করব?"

**দায়িত্বশীল AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

গভীর নিরাপত্তাবিধি সহ AI নিরাপত্তা নির্মাণ করুন। এই ডেমো দুই স্তরের সুরক্ষা দেখায় যা একসাথে কাজ করে:

**অংশ ১: LangChain4j ইনপুট গার্ডরেইলস** - ক্ষতিকর প্রম্পট LLM পর্যন্ত পৌঁছানোর আগে ব্লক করে। কাস্টম গার্ডরেইল তৈরি করুন যা নিষিদ্ধ শব্দ বা প্যাটার্ন পরীক্ষা করে। এগুলি আপনার কোডে চলে, তাই দ্রুত এবং বিনামূল্যে।

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

**অংশ ২: প্রোভাইডার নিরাপত্তা ফিল্টার** - GitHub Models এ অন্তর্নির্মিত ফিল্টার আছে যা আপনার গার্ডরেইল মিস করতে পারে এমন কিছু ক্যাচ করে। আপনি কড়া ব্লক (HTTP 400 এরর) এবং নরম প্রত্যাখ্যান দেখতে পাবেন যেখানে AI বিনয়ের সাথে অস্বীকার করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** খুলুন [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) এবং জিজ্ঞাসা করুন:
> - "InputGuardrail কী এবং নিজের কিভাবে তৈরি করব?"
> - "কঠোর ব্লক আর নরম প্রত্যাখ্যান এর মধ্যে পার্থক্য কী?"
> - "কেন গার্ডরেইলস এবং প্রোভাইডার ফিল্টার একসঙ্গে ব্যবহার করব?"

## পরবর্তী পদক্ষেপ

**পরবর্তী মডিউল:** [01-পরিচিতি - LangChain4j এবং gpt-5 এর সাথে Azure-তে শুরু](../01-introduction/README.md)

---

**নেভিগেশন:** [← প্রধান পৃষ্ঠায় ফিরে যান](../README.md) | [পরের: মডিউল 01 - পরিচিতি →](../01-introduction/README.md)

---

## সমস্যা সমাধান

### প্রথমবার Maven বিল্ড

**সমস্যা**: প্রাথমিক `mvn clean compile` বা `mvn package` ধীরে চলে (১০-১৫ মিনিট)

**কারণ**: Maven প্রথম বিল্ডে সমস্ত প্রকল্প নির্ভরতা (Spring Boot, LangChain4j লাইব্রেরি, Azure SDKs ইত্যাদি) ডাউনলোড করে।

**সমাধান**: এটি স্বাভাবিক। পরে বিল্ড গুলো অনেক দ্রুত হবে কারণ নির্ভরতা লোকাল ক্যাশে থাকবে। ডাউনলোড সময় আপনার নেটওয়ার্ক স্পিডের ওপর নির্ভর করে।

### PowerShell Maven কমান্ড সিনট্যাক্স

**সমস্যা**: Maven কমান্ডগুলো ত্রুটি দেয় `Unknown lifecycle phase ".mainClass=..."`
**কারণ**: PowerShell `=` কে একটি ভেরিয়েবল অ্যাসাইনমেন্ট অপারেটর হিসেবে ব্যাখ্যা করে, যা Maven প্রপার্টি সিনট্যাক্স ভেঙে দেয়

**সমাধান**: Maven কমান্ডের আগে stop-parsing অপারেটর `--%` ব্যবহার করুন:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` অপারেটর PowerShell কে নির্দেশ দেয় বাকি সব আর্গুমেন্ট অর্থেই Maven-এ পাস করতে, কোনো ব্যাখ্যা ছাড়াই।

### Windows PowerShell ইমোজি প্রদর্শন

**সমস্যা**: PowerShell এ AI রেসপন্সে ইমোজির পরিবর্তে গার্বেজ ক্যারেক্টার (যেমন `????` বা `â??`) দেখায়

**কারণ**: PowerShell এর ডিফল্ট এনকোডিং UTF-8 ইমোজি সাপোর্ট করে না

**সমাধান**: Java অ্যাপ্লিকেশন চালানোর আগে এই কমান্ডটি রান করুন:
```cmd
chcp 65001
```

এটি টার্মিনালে UTF-8 এনকোডিং জোরদার করে। বিকল্প হিসেবে Windows Terminal ব্যবহার করুন যা উন্নত ইউনিকোড সাপোর্ট দেয়।

### API কল ডিবাগিং

**সমস্যা**: AI মডেল থেকে অথেনটিকেশন এরর, রেট লিমিট, বা অপ্রত্যাশিত রেসপন্স

**সমাধান**: উদাহরণগুলোতে `.logRequests(true)` এবং `.logResponses(true)` আছে যা API কল কনসোলে দেখায়। এটি অথেনটিকেশন এরর, রেট লিমিট, বা অপ্রত্যাশিত রেসপন্স সমস্যাগুলো ট্রাবলশুট করতে সাহায্য করে। প্রোডাকশনে লগ গোলমাল কমাতে এই ফ্ল্যাগগুলো সরিয়ে ফেলুন।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য যথার্থতার জন্য চেষ্টা করি, তবে দয়া করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসামঞ্জস্যতা থাকতে পারে। মূল নথিটি তার স্বাভাবিক ভাষায় কর্তৃপক্ষপূর্ণ উৎস হিসাবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানুষিক অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহার থেকে উদ্ভূত কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->