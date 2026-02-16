# মডিউল 00: দ্রুত শুরু

## বিষয়বস্তু

- [পরিচিতি](../../../00-quick-start)
- [LangChain4j কি?](../../../00-quick-start)
- [LangChain4j নির্ভরশীলতা](../../../00-quick-start)
- [পূর্বশর্ত](../../../00-quick-start)
- [সেটআপ](../../../00-quick-start)
  - [1. আপনার GitHub টোকেন পান](../../../00-quick-start)
  - [2. আপনার টোকেন সেট করুন](../../../00-quick-start)
- [উদাহরণ চালান](../../../00-quick-start)
  - [1. মৌলিক চ্যাট](../../../00-quick-start)
  - [2. প্রম্পট প্যাটার্ন](../../../00-quick-start)
  - [3. ফাংশন কলিং](../../../00-quick-start)
  - [4. ডকুমেন্ট প্রশ্ন ও উত্তর (RAG)](../../../00-quick-start)
  - [5. দায়িত্বশীল AI](../../../00-quick-start)
- [প্রতি উদাহরণ কি দেখায়](../../../00-quick-start)
- [পরবর্তী পদক্ষেপ](../../../00-quick-start)
- [সমস্যা সমাধান](../../../00-quick-start)

## পরিচিতি

এই দ্রুত শুরু গাইডটি আপনাকে LangChain4j এর সাথে দ্রুত শুরু করার জন্য তৈরি। এটি LangChain4j এবং GitHub মডেল ব্যবহার করে AI অ্যাপ্লিকেশন তৈরির মৌলিক বিষয়গুলো কভার করে। পরবর্তী মডিউলগুলোতে আপনি LangChain4j এর সাথে Azure OpenAI ব্যবহার করে আরো উন্নত অ্যাপ্লিকেশন তৈরি করবেন।

## LangChain4j কি?

LangChain4j একটি Java লাইব্রেরি যা AI-চালিত অ্যাপ্লিকেশন তৈরি করা সহজ করে তোলে। HTTP ক্লায়েন্ট এবং JSON পার্সিং নিয়ে ঝামেলা না করে, আপনি পরিষ্কার Java API-র মাধ্যমে কাজ করবেন।

LangChain এর "চেইন" বলতে একাধিক উপাদানকে একসাথে সংযুক্ত করা বোঝায় - আপনি একটি প্রম্পটকে একটি মডেল বা একটি পার্সারে চেইন করতে পারেন, অথবা একাধিক AI কল একসাথে যেখানে একটি আউটপুট পরবর্তী ইনপুটে ফিড হয়। এই দ্রুত শুরু গাইডটি আরও জটিল চেইনগুলি অন্বেষণের আগে মূল বিষয়গুলোতে ফোকাস করে।

<img src="../../../translated_images/bn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-তে উপাদানগুলি চেইনিং - বিল্ডিং ব্লকগুলি একসাথে যুক্ত হয়ে শক্তিশালী AI ওয়ার্কফ্লো তৈরি করে*

আমরা তিনটি মূল উপাদান ব্যবহার করব:

**ChatLanguageModel** - AI মডেলের সাথে ইন্টারঅ্যাকশনের ইন্টারফেস। `model.chat("prompt")` কল করুন এবং একটি উত্তর স্ট্রিং পান। আমরা `OpenAiOfficialChatModel` ব্যবহার করি যা GitHub মডেলসের মতো OpenAI-অনুকূল এন্ডপয়েন্টগুলোর সাথে কাজ করে।

**AiServices** - টাইপ-নিরাপদ AI সেবা ইন্টারফেস তৈরি করে। মেথড ডিফাইন করুন, সেগুলোকে `@Tool` দিয়ে অ্যানোটেট করুন, এবং LangChain4j অর্কেস্ট্রেশন হ্যান্ডেল করে। AI স্বয়ংক্রিয়ভাবে আপনার Java মেথড কল করে যখন প্রয়োজন হয়।

**MessageWindowChatMemory** - কথোপকথন ইতিহাস পরিচালনা করে। এটা না থাকলে প্রতিটি অনুরোধ স্বতন্ত্র হবে। এটা থাকলে AI পূর্বের বার্তা মনে রাখে এবং একাধিক পাল্লায় প্রসঙ্গ বজায় রাখে।

<img src="../../../translated_images/bn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j আর্কিটেকচার - মূল উপাদানগুলো একসাথে কাজ করে আপনার AI অ্যাপ্লিকেশন চালাতে*

## LangChain4j নির্ভরশীলতা

এই দ্রুত শুরু গাইডে দুটি Maven নির্ভরশীলতা [`pom.xml`](../../../00-quick-start/pom.xml) ফাইলে ব্যবহৃত হয়েছে:

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
```

`langchain4j-open-ai-official` মডিউলটি `OpenAiOfficialChatModel` ক্লাস দেয় যা OpenAI-অনুকূল API-তে সংযোগ করে। GitHub মডেলস একই API ফরম্যাট ব্যবহার করে, তাই বিশেষ কোনো অ্যাডাপ্টারের প্রয়োজন নেই - শুধু বেস URL সেট করুন `https://models.github.ai/inference` এ।

## পূর্বশর্ত

**ডেভ কন্টেইনার ব্যবহার করছেন?** Java এবং Maven ইতিমধ্যে ইন্সটল করা আছে। আপনার শুধু একটি GitHub পার্সোনাল অ্যাক্সেস টোকেন লাগবে।

**স্থানীয় উন্নয়ন:**
- Java 21+, Maven 3.9+
- GitHub পার্সোনাল অ্যাক্সেস টোকেন (নির্দেশনা নিচে)

> **দ্রষ্টব্য:** এই মডিউল GitHub মডেলস থেকে `gpt-4.1-nano` ব্যবহার করে। কোডে মডেল নাম পরিবর্তন করবেন না - এটি GitHub এর উপলব্ধ মডেলসের সাথে কাজ করার জন্য কনফিগার করা হয়েছে।

## সেটআপ

### 1. আপনার GitHub টোকেন পান

1. যান [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" এ ক্লিক করুন
3. একটি বর্ণনামূলক নাম দিন (যেমন, "LangChain4j Demo")
4. মেয়াদ নির্ধারণ করুন (৭ দিন সুপারিশিত)
5. "Account permissions" এর মধ্যে "Models" খুঁজে সেট করুন "Read-only"
6. "Generate token" ক্লিক করুন
7. টোকেনটি কপি করুন এবং সেভ করুন - একবার দেখলে আর পাবেন না

### 2. আপনার টোকেন সেট করুন

**অপশন ১: VS Code ব্যবহার (সুপারিশ)**

VS Code ব্যবহার করলে, প্রকল্পের মূল ফোল্টারে `.env` ফাইলে আপনার টোকেন যোগ করুন:

যদি `.env` ফাইল না থাকে, `.env.example` থেকে `.env` কপি করুন অথবা নতুন `.env` ফাইল তৈরি করুন।

**`.env` ফাইলের উদাহরণ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env এ
GITHUB_TOKEN=your_token_here
```

তারপর আপনি সহজেই কোনও ডেমো ফাইলে (যেমন `BasicChatDemo.java`) এক্সপ্লোরারে রাইট-ক্লিক করে **"Run Java"** সিলেক্ট করতে পারেন অথবা Run and Debug প্যানেল থেকে লঞ্চ কনফিগারেশন ব্যবহার করতে পারেন।

**অপশন ২: টার্মিনাল ব্যবহার**

টোকেনটি পরিবেশভেরিয়েবল হিসেবে সেট করুন:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## উদাহরণ চালান

**VS Code ব্যবহার:** এক্সপ্লোরারে যে কোনো ডেমো ফাইলে রাইট-ক্লিক করে **"Run Java"** বেছে নিন, অথবা Run and Debug প্যানেল থেকে লঞ্চ কনফিগারেশন ব্যবহার করুন (প্রথমে `.env` ফাইল-এ টোকেন যোগ করা আবশ্যক)।

**Maven ব্যবহার:** বিকল্পভাবে কমান্ড লাইন থেকে চালাতে পারেন:

### 1. মৌলিক চ্যাট

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

জিরো-শট, ফিউ-শট, চেইন-অফ-থট, এবং রোল-ভিত্তিক প্রম্পটিং দেখায়।

### 3. ফাংশন কলিং

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI স্বয়ংক্রিয়ভাবে আপনার Java মেথডগুলো কল করবে যখন প্রয়োজন।

### 4. ডকুমেন্ট প্রশ্ন ও উত্তর (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` এর বিষয়বস্তু দিয়ে প্রশ্ন করুন।

### 5. দায়িত্বশীল AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

দেখুন AI নিরাপত্তা ফিল্টার কীভাবে ক্ষতিকর বিষয়বস্তু ব্লক করে।

## প্রতি উদাহরণ কি দেখায়

**মৌলিক চ্যাট** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

এখান থেকেই শুরু করুন LangChain4j এর সবচেয়ে মৌলিক রূপ দেখতে। আপনি একটি `OpenAiOfficialChatModel` তৈরি করবেন, `.chat()` দিয়ে প্রম্পট পাঠাবেন, এবং একটি উত্তর পাবেন। এটি প্রমাণ করে কিভাবে কাস্টম এন্ডপয়েন্ট এবং API কী দিয়ে মডেল আরম্ভ করবেন। এই প্যাটার্ন বুঝলে বাকী সব সহজে করা যাবে।

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot Chat ব্যবহার করে চেষ্টা করুন:** খোলুন [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) এবং জিজ্ঞেস করুন:
> - "কোডে কিভাবে GitHub মডেল থেকে Azure OpenAI এ পরিবর্তন করব?"
> - "OpenAiOfficialChatModel.builder() এ আর কোন প্যারামিটার কনফিগার করতে পারি?"
> - "পরিপূর্ণ উত্তর অপেক্ষা না করে কীভাবে স্ট্রিমিং রেসপন্স যুক্ত করব?"

**প্রম্পট ইঞ্জিনিয়ারিং** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

এখন যেহেতু আপনি মডেলের সাথে কথা বলতে জানেন, দেখা যাক কি বলেন। এই ডেমোতে একই মডেল সেটআপ ব্যবহার করে পাঁচটি ভিন্ন প্রম্পট প্যাটার্ন দেখানো হয়েছে। সরাসরি নির্দেশনার জন্য জিরো-শট প্রম্পট, উদাহরণ থেকে শেখার জন্য ফিউ-শট, যুক্তি প্রদর্শনের জন্য চেইন-অফ-থট, এবং প্রসঙ্গ নির্ধারণের জন্য রোল-ভিত্তিক প্রম্পট চেষ্টা করুন। আপনি দেখবেন একই মডেল কিভাবে বিভিন্ন রকম ফলাফল দেয় প্রম্পট বিন্যাস অনুযায়ী।

ডেমোটি প্রম্পট টেমপ্লেটও দেখায়, যা ভেরিয়েবল দিয়ে পুনঃব্যবহারযোগ্য প্রম্পট তৈরি করার একটি শক্তিশালী উপায়।
নিচের উদাহরণ LangChain4j `PromptTemplate` ব্যবহার করে ভেরিয়েবল পূরণের মাধ্যমে প্রম্পট দেখায়। AI প্রদত্ত গন্তব্য এবং ক্রিয়াকলাপের ভিত্তিতে উত্তর দেবে।

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

> **🤖 GitHub Copilot Chat ব্যবহার করে চেষ্টা করুন:** খোলুন [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) এবং জিজ্ঞেস করুন:
> - "জিরো-শট এবং ফিউ-শট প্রম্পটিং এ পার্থক্য কী, এবং কবে কোনটি ব্যবহার করবেন?"
> - "তাপমাত্রা প্যারামিটার মডেলের উত্তরকে কীভাবে প্রভাবিত করে?"
> - "প্রোডাকশনে প্রম্পট ইনজেকশন আক্রমণ থেকে রক্ষা পেতে কী কৌশল আছে?"
> - "সাধারণ প্যাটার্নের জন্য পুনঃব্যবহারযোগ্য PromptTemplate অবজেক্ট কিভাবে তৈরি করব?"

**টুল ইন্টিগ্রেশন** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

এখানেই LangChain4j শক্তিশালী হয়। আপনি `AiServices` ব্যবহার করে AI সহকারী তৈরি করবেন যা আপনার Java মেথড কল করতে পারে। শুধু মেথডগুলোর উপর `@Tool("বর্ণনা")` অ্যানোটেট করুন এবং LangChain4j বাকি হ্যান্ডেল করবে - AI স্বয়ংক্রিয়ভাবে সরঞ্জামটি ব্যবহার করবে যা ব্যবহারকারী চায় তার ভিত্তিতে। এটি ফাংশন কলিং প্রমাণ করে, যা AI কে শুধু প্রশ্নের উত্তর না দিয়ে কাজ করার জন্য তৈরি করার একটি মূল প্রযুক্তি।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 GitHub Copilot Chat ব্যবহার করে চেষ্টা করুন:** খোলুন [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) এবং জিজ্ঞেস করুন:
> - "@Tool অ্যানোটেশন কিভাবে কাজ করে এবং LangChain4j এর পেছনে এটি কী করে?"
> - "AI কি জটিল সমস্যা সমাধানের জন্য একাধিক টুল ধারাবাহিকভাবে কল করতে পারে?"
> - "যদি কোনো টুল এক্সসেপশন ছুড়ে, আমি কীভাবে এরর হ্যান্ডেল করব?"
> - "এই ক্যালকুলেটর উদাহরণের পরিবর্তে আসল API কীভাবে ইন্টিগ্রেট করব?"

**ডকুমেন্ট প্রশ্ন ও উত্তর (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

এখানে আপনি RAG (retrieval-augmented generation) এর ভিত্তি দেখবেন। মডেলের প্রশিক্ষণ ডেটার ওপর নির্ভর করার বদলে, আপনি [`document.txt`](../../../00-quick-start/document.txt) থেকে বিষয়বস্তু লোড করে প্রম্পটে অন্তর্ভুক্ত করবেন। AI আপনার ডকুমেন্টের ভিত্তিতে উত্তর দেবে, তার সাধারণ জ্ঞানের উপর নয়। এটি আপনার নিজের ডেটা নিয়ে কাজ করার সিস্টেম তৈরির প্রথম ধাপ।

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **দ্রষ্টব্য:** এই সহজ পদ্ধতিতে পুরো ডকুমেন্ট প্রম্পটে লোড হয়। বড় ফাইল (>10KB) এর ক্ষেত্রে প্রসঙ্গ সীমা অতিক্রম করবে। মডিউল 03 প্রোডাকশন RAG সিস্টেমের জন্য চাংকিং এবং ভেক্টর সার্চ কভার করে।

> **🤖 GitHub Copilot Chat ব্যবহার করে চেষ্টা করুন:** খোলুন [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) এবং জিজ্ঞেস করুন:
> - "RAG কিভাবে AI হ্যালুসিনেশন প্রতিরোধ করে মডেলের প্রশিক্ষণ ডেটার তুলনায়?"
> - "এই সরল পদ্ধতি এবং রিট্রিভালের জন্য ভেক্টর এমবেডিং ব্যবহারের পার্থক্য কী?"
> - "আমি কিভাবে এটা বহু ডকুমেন্ট বা বড় নলেজ বেসে স্কেল করব?"
> - "AI শুধুমাত্র প্রদানকৃত প্রসঙ্গ ব্যবহার করবে তা নিশ্চিত করতে প্রম্পট গঠন করার সেরা অভ্যাস কী?"

**দায়িত্বশীল AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

সংরক্ষণের গভীরে AI নিরাপত্তা নির্মাণ করুন। এই ডেমোতে দুটি স্তরের সুরক্ষা দেখানো হয়েছে:

**পর্ব 1: LangChain4j ইনপুট গার্ডরেইলস** - LLM পৌঁছানোর আগে বিপজ্জনক প্রম্পট ব্লক করে। কাস্টম গার্ডরেইল তৈরি করুন যা নিষিদ্ধ কীওয়ার্ড বা প্যাটার্ন চেক করে। এগুলো আপনার কোডে চলে, তাই দ্রুত এবং বিনামূল্যে।

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

**পর্ব 2: প্রোভাইডার সেফটি ফিল্টারস** - GitHub মডেলসে বিল্ট-ইন ফিল্টার আছে যা আপনার গার্ডরেইল থেকে বাদ পড়া বিষয়গুলি খুঁজে পায়। আপনি কঠোর ব্লক (HTTP 400 এরর) এবং নরম প্রত্যাখ্যান (AI বিনয়পূর্বক অস্বীকার করে) দেখবেন।

> **🤖 GitHub Copilot Chat ব্যবহার করে চেষ্টা করুন:** খোলুন [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) এবং জিজ্ঞেস করুন:
> - "InputGuardrail কী এবং কীভাবে নিজের তৈরি করব?"
> - "কঠোর ব্লক এবং নরম প্রত্যাখ্যানের মধ্যে পার্থক্য কী?"
> - "কেন গার্ডরেইলস এবং প্রোভাইডার ফিল্টার একসাথে ব্যবহার করতে হবে?"

## পরবর্তী পদক্ষেপ

**পরবর্তী মডিউল:** [01-introduction - LangChain4j এবং gpt-5 এর সাথে Azure তে শুরু](../01-introduction/README.md)

---

**নেভিগেশন:** [← মূল ভাগে ফিরে যান](../README.md) | [পরবর্তী: মডিউল 01 - পরিচিতি →](../01-introduction/README.md)

---

## সমস্যা সমাধান

### প্রথমবার Maven বিল্ড

**সমস্যা**: প্রথম `mvn clean compile` বা `mvn package` অনেক বেশি সময় (১০-১৫ মিনিট) নেয়

**কারণ**: Maven প্রথম বিল্ডে সব প্রকল্প নির্ভরশীলতা (Spring Boot, LangChain4j লাইব্রেরি, Azure SDK ইত্যাদি) ডাউনলোড করতে হয়।

**সমাধান**: এটি স্বাভাবিক আচরণ। পরবর্তী বিল্ডগুলি অনেক দ্রুত হবে কারণ নির্ভরশীলতাগুলি স্থানীয় ক্যাশে থাকবে। ডাউনলোডের সময় আপনার নেটওয়ার্ক স্পিডের ওপর নির্ভর করে।
### PowerShell Maven কমান্ড সিনট্যাক্স

**সমস্যা**: Maven কমান্ডে ত্রুটি আসে `Unknown lifecycle phase ".mainClass=..."`

**কারণ**: PowerShell `=` চিহ্নকে ভেরিয়েবল অ্যাসাইনমেন্ট অপারেটর হিসেবে ব্যাখ্যা করে, যা Maven প্রপার্টি সিনট্যাক্স ভঙ্গ করে

**সমাধান**: Maven কমান্ডের আগে stop-parsing operator `--%` ব্যবহার করুন:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` অপারেটরটি PowerShell কে বাকি সব আর্গুমেন্ট মুভনকে সরাসরি এবং অব্যাখ্যাতভাবে পাঠানোর নির্দেশ দেয়।

### Windows PowerShell Emoji প্রদর্শন

**সমস্যা**: PowerShell-এ AI উত্তরগুলোতে ইমোজির পরিবর্তে জাঙ্ক ক্যারেক্টার (যেমন, `????` বা `â??`) দেখা যায়

**কারণ**: PowerShell এর ডিফল্ট এনকোডিং UTF-8 ইমোজি সমর্থন করে না

**সমাধান**: Java অ্যাপ্লিকেশন চালানোর আগে এই কমান্ডটি চালান:
```cmd
chcp 65001
```

এটি টার্মিনালে UTF-8 এনকোডিং প্রয়োগ করে। বিকল্পভাবে, Windows Terminal ব্যবহার করুন যা উন্নত ইউনিকোড সমর্থন করে।

### API কলের ডিবাগিং

**সমস্যা**: AI মডেল থেকে প্রমাণীকরণ ত্রুটি, রেট সীমাবদ্ধতা, অথবা অপ্রত্যাশিত প্রতিক্রিয়া

**সমাধান**: উদাহরণগুলোতে `.logRequests(true)` এবং `.logResponses(true)` ব্যবহারে API কলগুলি কনসোলে দেখা যায়। এটি প্রমাণীকরণ ত্রুটি, রেট সীমাবদ্ধতা বা অপ্রত্যাশিত প্রতিক্রিয়া ডিবাগ করতে সাহায্য করে। প্রোডাকশনে লগ শব্দ কমাতে এগুলো সরিয়ে ফেলুন।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি এআই অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার জন্য যত্নশীল হওয়া সত্ত্বেও, অনুগ্রহ করে সচেতন থাকুন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা ভুল থাকতে পারে। মূল নথিটি তার প্রাকৃতিক ভাষায়ই বিশ্বাসযোগ্য উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ করা শ্রেয়। এই অনুবাদের ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->