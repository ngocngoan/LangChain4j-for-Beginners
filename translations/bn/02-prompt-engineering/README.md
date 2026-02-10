# মডিউল ০২: GPT-5.2 দিয়ে প্রম্পট ইঞ্জিনিয়ারিং

## বিষয় তালিকা

- [আপনি যা শিখবেন](../../../02-prompt-engineering)
- [পূর্বশর্তসমূহ](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [এটি কিভাবে LangChain4j ব্যবহার করে](../../../02-prompt-engineering)
- [মূল প্যাটার্নসমূহ](../../../02-prompt-engineering)
- [বিদ্যমান Azure সম্পদ ব্যবহার করা](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রীনশট](../../../02-prompt-engineering)
- [প্যাটার্নগুলি অন্বেষণ করা](../../../02-prompt-engineering)
  - [কম বনাম বেশি উৎসাহ](../../../02-prompt-engineering)
  - [কাজ সম্পাদন (টুল প্রিমেবলস)](../../../02-prompt-engineering)
  - [স্ব-প্রতিফলিত কোড](../../../02-prompt-engineering)
  - [গঠিত বিশ্লেষণ](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [ধাপে ধাপে যুক্তি](../../../02-prompt-engineering)
  - [সীমাবদ্ধ আউটপুট](../../../02-prompt-engineering)
- [আপনি আসলে যা শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী ধাপসমূহ](../../../02-prompt-engineering)

## আপনি যা শিখবেন

আগের মডিউলে, আপনি দেখেছেন কীভাবে মেমরি কথোপকথনমূলক AI সক্ষম করে এবং প্রাথমিক ইন্টারঅ্যাকশনের জন্য GitHub মডেলগুলি ব্যবহার করেছেন। এখন আমরা ফোকাস করব আপনি কিভাবে প্রশ্ন করবেন — অর্থাৎ প্রম্পটগুলি — Azure OpenAI এর GPT-5.2 ব্যবহার করে। আপনার প্রম্পট গঠন করার পদ্ধতি যে উত্তর গুণমান প্রভাবিত করে তা ব্যাপকভাবে গুরুত্বপূর্ণ।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তি নিয়ন্ত্রণ প্রবর্তন করে — আপনি মডেলকে বলতে পারেন উত্তর দেওয়ার আগে কতটুকু চিন্তা করতে হবে। এটি বিভিন্ন প্রম্পটিং কৌশলগুলো স্পষ্ট করে তোলে এবং আপনাকে বুঝতে সাহায্য করে কখন কোন পন্থা ব্যবহার করবেন। এছাড়াও GitHub মডেলগুলোর তুলনায় Azure এর GPT-5.2 এর কম রেট সীমা আমাদের জন্য সুবিধাজনক।

## পূর্বশর্তসমূহ

- মডিউল ০১ সম্পন্ন (Azure OpenAI সম্পদ স্থাপিত)
- মূল ডিরেক্টরিতে `.env` ফাইল রয়েছে Azure পরিচয়পত্র সহ (মডিউল ০১ এ `azd up` দ্বারা তৈরি)

> **নোট:** যদি আপনি মডিউল ০১ সম্পন্ন না করে থাকেন, সেখানে দেওয়া ডিপ্লয়মেন্ট নির্দেশাবলী প্রথমে অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

প্রম্পট ইঞ্জিনিয়ারিং হল এমন ইনপুট টেক্সট ডিজাইন করা যা ধারাবাহিকভাবে আপনাকে প্রয়োজনীয় ফলাফল দেয়। এটি কেবল প্রশ্ন করার ব্যাপার নয় — এটি অনুরোধগুলিকে এমনভাবে তৈরি করার ব্যাপার যাতে মডেল স্পষ্টভাবে বুঝে আপনি কী চান এবং কীভাবে তা সরবরাহ করতে হবে।

এটি ভাবুন এমনভাবে যেন আপনি কোনো সহকর্মীকে নির্দেশনা দিচ্ছেন। "বাগ ঠিক করো" অস্পষ্ট। "UserService.java এর লাইন ৪৫ এ নাল চেক যোগ করে নাল পয়েন্টার এক্সসেপশন ঠিক করো" নির্দিষ্ট। ভাষা মডেল একইভাবে কাজ করে — নির্দিষ্টতা এবং গঠন গুরুত্বপূর্ণ।

## এটি কিভাবে LangChain4j ব্যবহার করে

এই মডিউলে পূর্ববর্তী মডিউল থেকে একই LangChain4j ভিত্তি ব্যবহার করে উন্নত প্রম্পটিং প্যাটার্ন প্রমাণিত হয়েছে, যেখানে ফোকাস আছে প্রম্পট গঠন এবং যুক্তি নিয়ন্ত্রণে।

<img src="../../../translated_images/bn/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*কিভাবে LangChain4j আপনার প্রম্পটগুলো Azure OpenAI GPT-5.2 এর সাথে সংযুক্ত করে*

**নির্ভরশীলতা** — মডিউল ০২ `pom.xml` এ নির্ধারিত নিম্নলিখিত langchain4j নির্ভরশীলতাগুলো ব্যবহার করে:
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

**OpenAiOfficialChatModel কনফিগারেশন** — [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

চ্যাট মডেলটি OpenAI অফিসিয়াল ক্লায়েন্ট ব্যবহার করে একটি Spring bean হিসাবে ম্যানুয়ালি কনফিগার করা হয়েছে, যা Azure OpenAI এন্ডপয়েন্ট সমর্থন করে। মডিউল ০১ থেকে মূল পার্থক্য হল আমরা `chatModel.chat()` এ পাঠানো প্রম্পটগুলো কীভাবে গঠন করি, মডেল সেটআপ নয়।

**সিস্টেম এবং ইউজার মেসেজ** — [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j স্পষ্টতার জন্য মেসেজ টাইপ আলাদা করে। `SystemMessage` AI এর আচরণ এবং প্রসঙ্গ নির্ধারণ করে (যেমন "আপনি কোড রিভিউয়ার"), আর `UserMessage` আসল অনুরোধ ধারণ করে। এই পৃথকরণ আপনাকে বিভিন্ন ব্যবহারকারী প্রশ্নে একই AI আচরণ বজায় রাখতে দেয়।

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/bn/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage স্থায়ী প্রসঙ্গ দেয়, UserMessages পৃথক অনুরোধ ধারণ করে*

**Multi-Turn এর জন্য MessageWindowChatMemory** — মাল্টি-টার্ন কথোপকথন প্যাটার্নের জন্য আমরা মডিউল ০১ থেকে `MessageWindowChatMemory` পুনরায় ব্যবহার করি। প্রতিটি সেশনের নিজস্ব মেমোরি ইনস্ট্যান্স একটি `Map<String, ChatMemory>` তে সংরক্ষণ করা হয়, যাতে একাধিক সংলাপ একই সময়ে চলতে পারে প্রসঙ্গ মিশ্রিত না হয়ে।

**প্রম্পট টেমপ্লেট** — এখানে আসল ফোকাস প্রম্পট ইঞ্জিনিয়ারিং, নতুন LangChain4j API নয়। প্রতিটি প্যাটার্ন (কম উৎসাহ, বেশি উৎসাহ, কাজ সম্পাদন ইত্যাদি) একই `chatModel.chat(prompt)` পদ্ধতি ব্যবহার করে তবে সাবধানে গঠিত প্রম্পট স্ট্রিং সহ। XML ট্যাগ, নির্দেশাবলী এবং ফরম্যাটিং সব প্রম্পট টেক্সটের অংশ, LangChain4j ফিচার নয়।

**যুক্তি নিয়ন্ত্রণ** — GPT-5.2 এর যুক্তি প্রচেষ্টা প্রম্পট নির্দেশাবলীর মাধ্যমে নিয়ন্ত্রণ করা হয় যেমন "সর্বোচ্চ ২টি যুক্তি ধাপ" বা "বিশদরূপে অনুসন্ধান করো"। এগুলো প্রম্পট ইঞ্জিনিয়ারিং কৌশল, LangChain4j কনফিগারেশন নয়। লাইব্রেরি কেবল আপনার প্রম্পটগুলো মডেলে পাঠায়।

মূল উপসংহার: LangChain4j অবকাঠামো প্রদান করে (মডেল সংযোগ [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), মেমরি, মেসেজ হেন্ডলিং [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), আর এই মডিউল শেখায় কিভাবে সেই অবকাঠামোর মধ্যে কার্যকর প্রম্পট তৈরি করবেন।

## মূল প্যাটার্নসমূহ

সব সমস্যার জন্য একই পন্থা প্রয়োজন হয় না। কিছু প্রশ্ন দ্রুত উত্তর প্রয়োজন, কিছু গভীর চিন্তা দরকার। কিছু দৃশ্যমান যুক্তি প্রয়োজন, কিছু শুধুই ফলাফল। এই মডিউলে আঠটি প্রম্পটিং প্যাটার্ন আছে — প্রতিটি বিভিন্ন পরিস্থিতির জন্য অপটিমাইজড। আপনি সবগুলো পরীক্ষা করবেন কখন কোন পন্থা সেরা কাজ করে তা জানতে।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন এবং তাদের ব্যবহারের পরিচিতি*

<img src="../../../translated_images/bn/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*কম উৎসাহ (দ্রুত, সরাসরি) বনাম বেশি উৎসাহ (বিস্তারিত, অনুসন্ধানাত্মক) যুক্তি পন্থাগুলো*

**কম উৎসাহ (দ্রুত ও কেন্দ্রীভূত)** — সহজ প্রশ্নের জন্য যেখানে দ্রুত, সরাসরি উত্তর চান। মডেল গুণগত বিশ্লেষণ কম করে — সর্বোচ্চ ২ ধাপ। হিসাব, খোঁজ বা সরল প্রশ্নের জন্য ব্যবহার করুন।

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot দিয়ে অন্বেষণ করুন:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) খুলুন এবং জিজ্ঞাসা করুন:
> - "কম উৎসাহ এবং বেশি উৎসাহ প্রম্পটিং প্যাটার্নগুলোর মধ্যে পার্থক্য কী?"
> - "কিভাবে প্রম্পটের XML ট্যাগগুলো AI এর উত্তরের গঠন করতে সাহায্য করে?"
> - "কখন স্ব-প্রতিফলন প্যাটার্ন এবং কখন সরাসরি নির্দেশনা ব্যবহার করা উচিত?"

**বেশি উৎসাহ (গভীর ও বিস্তারিত)** — জটিল সমস্যার জন্য যেখানে বিস্তৃত বিশ্লেষণ চান। মডেল বিস্তারিত অনুসন্ধান করে এবং বিস্তারিত যুক্তি দেখায়। সিস্টেম ডিজাইন, স্থাপত্য সিদ্ধান্ত বা জটিল গবেষণার জন্য ব্যবহার করুন।

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**কাজ সম্পাদন (ধাপে ধাপে অগ্রগতি)** — মাল্টি-স্টেপ ওয়ার্কফ্লো জন্য। মডেল একটি প্রাথমিক পরিকল্পনা দেয়, প্রতিটি ধাপ বর্ণনা করে কাজ চালায়, পরে সারসংক্ষেপ দেয়। মাইগ্রেশন, বাস্তবায়ন বা যেকোনো মাল্টি-স্টেপ প্রক্রিয়ার জন্য ব্যবহার করুন।

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

চেইন-অফ-থট প্রম্পটিং মডেলকে তার যুক্তি প্রক্রিয়া দেখাতে অনুরোধ করে, যা জটিল কাজের সঠিকতা বাড়ায়। ধাপে ধাপে বিশ্লেষণ মানুষ এবং AI দুই পক্ষকেই লজিক বোঝাতে সাহায্য করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** এই প্যাটার্ন সম্পর্কে প্রশ্ন করুন:
> - "দীর্ঘকালীন অপারেশনের জন্য কাজ সম্পাদন প্যাটার্ন কিভাবে অভিযোজিত করব?"
> - "প্রোডাকশন অ্যাপ্লিকেশনে টুল প্রিমেবল গঠন করার সেরা পদ্ধতি কী?"
> - "কিভাবে UI তে মধ্যবর্তী অগ্রগতি আপডেটগুলি ক্যাপচার এবং প্রদর্শন করব?"

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*পরিকল্পনা → সম্পাদন → সারসংক্ষেপ, মাল্টি-স্টেপ কাজের জন্য*

**স্ব-প্রতিফলিত কোড** — প্রোডাকশন-মানের কোড তৈরি করার জন্য। মডেল কোড তৈরি করে, মান নিয়ন্ত্রণ করে, এবং পুনরাবৃত্তি করে উন্নত করে। নতুন ফিচার বা সেবা তৈরির জন্য ব্যবহার করুন।

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*পুনরাবৃত্ত উন্নতির চক্র - তৈরি, মূল্যায়ন, সমস্যা সনাক্তকরণ, উন্নতি, পুনরাবৃত্তি*

**গঠিত বিশ্লেষণ** — ধারাবাহিক মূল্যায়নের জন্য। মডেল নির্দিষ্ট কাঠামো (সঠিকতা, প্র্যাকটিস, কর্মক্ষমতা, নিরাপত্তা) অনুসারে কোড পর্যালোচনা করে। কোড রিভিউ বা গুণমান মূল্যায়নের জন্য ব্যবহার করুন।

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** গঠিত বিশ্লেষণ সম্পর্কে প্রশ্ন করুন:
> - "বিভিন্ন ধরনের কোড রিভিউয়ের জন্য বিশ্লেষণ কাঠামো কাস্টমাইজ কিভাবে করবো?"
> - "গঠিত আউটপুট প্রোগ্রাম্যাটিক্যালি পার্স এবং ব্যবহার করার সেরা পদ্ধতি কী?"
> - "বিভিন্ন রিভিউ সেশনের মধ্যে ধারাবাহিক গুরুত্ব স্তর কিভাবে নিশ্চিত করব?"

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*চার-প্রকারের কাঠামো দ্বারা ধারাবাহিক কোড রিভিউ, গুরুত্ব স্তর সহ*

**মাল্টি-টার্ন চ্যাট** — প্রসঙ্গ প্রয়োজন এমন কথোপকথনের জন্য। মডেল পূর্বের মেসেজ মনে রাখে এবং সেগুলো ব্যবহার করে উত্তর দেয়। ইন্টারঅ্যাকটিভ হেল্প সেশন বা জটিল Q&A জন্য ব্যবহার করুন।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/bn/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*কতিপয় কথ্য প্রসঙ্গ একত্রিত হয় মাল্টি-টার্নে যতক্ষণ না টোকেন সীমা পূর্ণ হয়*

**ধাপে ধাপে যুক্তি** — দৃশ্যমান লজিক প্রয়োজন এমন সমস্যার জন্য। মডেল প্রতিটি ধাপে স্পষ্ট যুক্তি দেখায়। গণিত সমস্যা, লজিক ধাঁধা বা চিন্তাভাবনা বোঝার জন্য ব্যবহার করুন।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bn/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*সমস্যাগুলো স্পষ্ট যুক্তিবদ্ধ ধাপে বিভক্ত করা*

**সীমাবদ্ধ আউটপুট** — নির্দিষ্ট ফরম্যাট প্রয়োজন এমন উত্তরের জন্য। মডেল কড়া ফরম্যাট এবং দৈর্ঘ্য নিয়ম অনুসরণ করে। সারসংক্ষেপ বা যথাযথ আউটপুট গঠন দরকার হলে ব্যবহার করুন।

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

<img src="../../../translated_images/bn/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*নির্দিষ্ট ফরম্যাট, দৈর্ঘ্য, এবং গঠন নিয়ম প্রয়োগ করা*

## বিদ্যমান Azure সম্পদ ব্যবহার করা

**ডিপ্লয়মেন্ট যাচাই করুন:**

মূল ডিরেক্টরিতে `.env` ফাইলের মধ্যে Azure পরিচয়পত্র নিশ্চিত করুন (মডিউল ০১ চলাকালীন তৈরি হয়েছিল):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**অ্যাপ্লিকেশন শুরু করুন:**

> **নোট:** আপনি যদি আগে থেকেই মডিউল ০১ থেকে `./start-all.sh` ব্যবহার করে সব অ্যাপ্লিকেশন শুরু করে থাকেন, তাহলে এই মডিউল ইতিমধ্যেই পোর্ট ৮০৮৩ এ চলছে। আপনি নিচের শুরু কমান্ডগুলো স্কিপ করে সরাসরি http://localhost:8083 এ যেতে পারেন।

**বিকল্প ১: Spring Boot ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য সুপারিশকৃত)**

ডেভ কন্টেনারে Spring Boot Dashboard এক্সটেনশান অন্তর্ভুক্ত রয়েছে, যা সকল Spring Boot অ্যাপ্লিকেশন পরিচালনার ভিজ্যুয়াল ইন্টারফেস দেয়। VS Code এর বাম পাশে অ্যাকটিভিটি বারে (Spring Boot আইকন অনুসন্ধান করুন) পাবেন।

Spring Boot Dashboard থেকে আপনি:
- ওয়ার্কস্পেসের সব Spring Boot অ্যাপ দেখতে পারেন
- এক ক্লিকে অ্যাপ শুরু/বন্ধ করতে পারেন
- রিয়েল-টাইম লগ দেখতে পারেন
- অ্যাপ্লিকেশন অবস্থা দেখতে পারেন

শুধুমাত্র "prompt-engineering" এর পাশের প্লে বোতামে ক্লিক করে এই মডিউল শুরু করুন, অথবা সব মডিউল একসাথে শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার**

সব ওয়েব অ্যাপ্লিকেশন (মডিউল ০১-০৪) শুরু করুন:

**Bash:**
```bash
cd ..  # রুট ডিরেক্টরি থেকে
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # মূল ডিরেক্টরি থেকে
.\start-all.ps1
```

অথবা শুধুমাত্র এই মডিউল শুরু করুন:

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে মূল `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করবে এবং যদি JAR না থাকে তবে বিল্ড করবে।

> **নোট:** আপনি যদি শুরু করার আগে সব মডিউল নিজে নিজে বিল্ড করতে চান:
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

আপনার ব্রাউজারে http://localhost:8083 খুলুন।

**বন্ধ করার জন্য:**

**Bash:**
```bash
./stop.sh  # শুধুমাত্র এই মডিউল
# অথবা
cd .. && ./stop-all.sh  # সমস্ত মডিউলসমূহ
```

**PowerShell:**
```powershell
.\stop.ps1  # শুধুমাত্র এই মডিউল
# অথবা
cd ..; .\stop-all.ps1  # সমস্ত মডিউলসমূহ
```

## অ্যাপ্লিকেশন স্ক্রীনশট

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*মূল ড্যাশবোর্ড যেখানে সব ৮টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্নের বৈশিষ্ট্য এবং ব্যবহারের তথ্য দেয়া আছে*

## প্যাটার্নগুলি অন্বেষণ করা

ওয়েব ইন্টারফেস আপনাকে বিভিন্ন প্রম্পটিং কৌশল নিয়ে পরীক্ষা করার সুযোগ দেয়। প্রতিটি প্যাটার্ন বিভিন্ন সমস্যা সমাধান করে—এগুলো চেষ্টা করুন দেখতে কখন কোন পন্থা সবচেয়ে ভালো কাজ করে।

### কম বনাম বেশি উৎসাহ

"২০০ এর ১৫% কত?" এর মতো সহজ প্রশ্ন কম উৎসাহ দিয়ে জিজ্ঞাসা করুন। আপনি তৎক্ষণাৎ, সরাসরি উত্তর পাবেন। এখন "উচ্চ ট্রাফিক API এর জন্য ক্যাশিং স্ট্র্যাটেজি ডিজাইন করুন" এর মতো জটিল প্রশ্ন বেশি উৎসাহ দিয়ে জিজ্ঞাসা করুন। দেখুন মডেল কেমন করে ধীরে ধীরে বিশদ যুক্তি দেয়। একই মডেল, একই প্রশ্ন কাঠামো — তবে প্রম্পট বলে দেয় কতটা চিন্তা করতে হবে।
<img src="../../../translated_images/bn/low-eagerness-demo.898894591fb23aa0.webp" alt="কম উদ্দীপনা ডেমো" width="800"/>

*অল্প যুক্তিসহ দ্রুত হিসাব*

<img src="../../../translated_images/bn/high-eagerness-demo.4ac93e7786c5a376.webp" alt="উচ্চ উদ্দীপনা ডেমো" width="800"/>

*ব্যাপক ক্যাশিং কৌশল (২.৮এমবি)*

### টাস্ক সম্পাদন (টুল পীমেবল)

মাল্টি-স্টেপ ওয়ার্কফ্লো পূর্ব পরিকল্পনা এবং অগ্রগতি বর্ণনা থেকে উপকৃত হয়। মডেল বলে দেয় কী করবে, প্রতিটি ধাপ বর্ণনা করে, তারপর ফলাফল সারাংশ তৈরি করে।

<img src="../../../translated_images/bn/tool-preambles-demo.3ca4881e417f2e28.webp" alt="টাস্ক সম্পাদন ডেমো" width="800"/>

*ধাপে ধাপে বর্ণনা সহ একটি REST এন্ডপয়েন্ট তৈরি করা (৩.৯এমবি)*

### স্ব-প্রতিফলিত কোড

"একটি ইমেইল যাচাই পরিষেবা তৈরি করুন" চেষ্টা করুন। কেবল কোড তৈরি ও বন্ধ করার পরিবর্তে, মডেল তৈরি করে, গুণগত মানের বিরুদ্ধে মূল্যায়ন করে, দুর্বলতা চিহ্নিত করে, ও উন্নয়ন করে। আপনি দেখতে পাবেন এটি পুনরাবৃত্তি করছে যতক্ষণ না কোড উৎপাদন মান পূরণ করে।

<img src="../../../translated_images/bn/self-reflecting-code-demo.851ee05c988e743f.webp" alt="স্ব-প্রতিফলিত কোড ডেমো" width="800"/>

*সম্পূর্ণ ইমেইল যাচাই পরিষেবা (৫.২এমবি)*

### কাঠামোগত বিশ্লেষণ

কোড রিভিউতে সঙ্গত মূল্যায়ন ফ্রেমওয়ার্ক প্রয়োজন। মডেল নির্দিষ্ট বিভাগ (সঠিকতা, অনুশীলন, কর্মক্ষমতা, সুরক্ষা) এবং গুরুত্ব স্তর ব্যবহার করে কোড বিশ্লেষণ করে।

<img src="../../../translated_images/bn/structured-analysis-demo.9ef892194cd23bc8.webp" alt="কাঠামোগত বিশ্লেষণ ডেমো" width="800"/>

*ফ্রেমওয়ার্ক-ভিত্তিক কোড পর্যালোচনা*

### মাল্টি-টার্ন চ্যাট

"স্প্রিং বুট কী?" জিজ্ঞাসা করুন, তারপর সঙ্গে সঙ্গেই "আমাকে একটি উদাহরণ দেখাও" বলুন। মডেল আপনার প্রথম প্রশ্ন মনে রাখে এবং স্পেসিফিক্যালি একটি স্প্রিং বুট উদাহরণ দেয়। স্মৃতি ব্যতীত, দ্বিতীয় প্রশ্নটি খুব অস্পষ্ট হত।

<img src="../../../translated_images/bn/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="মাল্টি-টার্ন চ্যাট ডেমো" width="800"/>

*প্রশ্নের মাঝে প্রসঙ্গ সংরক্ষণ*

### ধাপে ধাপে যুক্তি

একটি গণিত সমস্যা বেছে নিন এবং ধাপে ধাপে যুক্তি এবং কম উদ্দীপনা উভয় দিয়ে চেষ্টা করুন। কম উদ্দীপনা কেবল উত্তর দেয় - দ্রুত কিন্তু অস্পষ্ট। ধাপে ধাপে আপনাকে প্রতিটি হিসাব এবং সিদ্ধান্ত দেখায়।

<img src="../../../translated_images/bn/step-by-step-reasoning-demo.12139513356faecd.webp" alt="ধাপে ধাপে যুক্তি ডেমো" width="800"/>

*স্পষ্ট ধাপ সহ গণিত সমস্যা*

### সীমাবদ্ধ আউটপুট

যখন নির্দিষ্ট ফর্ম্যাট বা শব্দ সংখ্যা প্রয়োজন, তখন এই প্যাটার্ন কঠোর অনুশীলন চাপিয়ে দেয়। নির্দিষ্ট ১০০ শব্দের সারণী তৈরি করে দেখতে পারেন।

<img src="../../../translated_images/bn/constrained-output-demo.567cc45b75da1633.webp" alt="সীমাবদ্ধ আউটপুট ডেমো" width="800"/>

*ফর্ম্যাট নিয়ন্ত্রণ সহ মেশিন লার্নিং সারাংশ*

## আপনি আসলেই কি শিখছেন

**যুক্তি প্রচেষ্টা সবকিছু বদলে দেয়**

GPT-5.2 আপনাকে আপনার প্রম্পটের মাধ্যমে গণনাগত প্রচেষ্টা নিয়ন্ত্রণ করতে দেয়। কম প্রচেষ্টা মানে দ্রুত প্রতিক্রিয়া এবং সীমিত অনুসন্ধান। উচ্চ প্রচেষ্টা মানে মডেল গভীরভাবে চিন্তা করতে সময় নেয়। আপনি শেখাচ্ছেন কাজের জটিলতার সাথে প্রচেষ্টা মিলিয়ে নিতে - সহজ প্রশ্নে সময় ব্যয় করবেন না, কিন্তু জটিল সিদ্ধান্তে দ্রুত করবেন না।

**কাঠামো আচরণ গাইড করে**

প্রম্পটে XML ট্যাগ লক্ষ্য করেছেন? এগুলো শুধুই সাজসজ্জা নয়। মডেলগুলি কাঠামোবদ্ধ নির্দেশাবলী স্বাধীন পাঠের থেকে বেশি নির্ভরযোগ্যভাবে অনুসরণ করে। যখন আপনি মাল্টি-স্টেপ প্রক্রিয়া বা জটিল লজিক চান, কাঠামো মডেলকে তার অবস্থান ও পরবর্তী ধাপ ট্র্যাক করতে সাহায্য করে।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="প্রম্পট কাঠামো" width="800"/>

*পরিষ্কার বিভাগ ও XML-শৈলী সংস্থাপনা সহ একটি ভালো কাঠামোবদ্ধ প্রম্পটের রূপ*

**স্ব-মূল্যায়নের মাধ্যমে গুণমান**

স্ব-প্রতিফলিত প্যাটার্নগুলি গুণগত মানের মাপকাঠির স্পষ্টতা দ্বারা কাজ করে। মডেলকে শুধু "সঠিক কর" আশা করার বদলে, আপনি স্পষ্ট বলে দেন "সঠিক" মানে কী: সঠিক যুক্তি, ত্রুটি ব্যবস্থাপনা, কর্মক্ষমতা, সুরক্ষা। মডেল তার আউটপুট নিজেই মূল্যায়ন করে এবং উন্নতি করে। এতে কোড তৈরি লটারি থেকে প্রক্রিয়ায় রূপান্তরিত হয়।

**প্রসঙ্গ সীমিত**

মাল্টি-টার্ন কথোপকথন প্রতিটি অনুরোধের সাথে মেসেজ ইতিহাস অন্তর্ভুক্ত করে কাজ করে। কিন্তু সর্বোচ্চ টোকেন সংখ্যা সীমা থাকে। কথোপকথন বাড়ার সাথে, আপনি প্রাসঙ্গিক প্রসঙ্গ বজায় রাখতে কৌশল প্রয়োগ করতে হবে যাতে সেই সীমা ছোঁয় না। এই মডিউল দেখায় স্মৃতি কীভাবে কাজ করে; পরে আপনি শিখবেন কখন সারাংশ তৈরি করতে হয়, কখন ভুলে যেতে হয়, এবং কখন আনা যায়।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [03-rag - RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](../03-rag/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল ০১ - পরিচিতি](../01-introduction/README.md) | [মেইনে ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০৩ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা সঠিকতার জন্য যতটা সম্ভব চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজস্ব ভাষায় কর্তৃপক্ষ সংবলিত উৎস হিসেবে গণ্য করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানৱ অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে ঘটে যাওয়া কোনো ভুল বোঝাবুঝি বা বিভ্রান্তির জন্য আমরা দায়িত্ব গ্রহণ করি না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->