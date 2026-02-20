# Module 02: GPT-5.2 এর সাথে প্রম্পট ইঞ্জিনিয়ারিং

## Table of Contents

- [আপনি যা শিখবেন](../../../02-prompt-engineering)
- [পূর্বশর্ত](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং মূলসূত্র](../../../02-prompt-engineering)
  - [জিরো-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [ফিউ-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [চেইন অফ থট](../../../02-prompt-engineering)
  - [রোল-ভিত্তিক প্রম্পটিং](../../../02-prompt-engineering)
  - [প্রম্পট টেমপ্লেট](../../../02-prompt-engineering)
- [উন্নত প্যাটার্নস](../../../02-prompt-engineering)
- [বিদ্যমান Azure রিসোর্স ব্যবহার](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রিনশট](../../../02-prompt-engineering)
- [প্যাটার্নসমূহ অন্বেষণ](../../../02-prompt-engineering)
  - [কম বনাম বেশি আগ্রহ](../../../02-prompt-engineering)
  - [টাস্ক এক্সিকিউশন (টুল প্রিম্বলস)](../../../02-prompt-engineering)
  - [সেল্ফ-রিফ্লেক্টিং কোড](../../../02-prompt-engineering)
  - [স্ট্রাকচার্ড অ্যানালাইসিস](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [ধাপে ধাপে যুক্তি](../../../02-prompt-engineering)
  - [সীমাবদ্ধ আউটপুট](../../../02-prompt-engineering)
- [আপনি যা সত্যিই শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী ধাপ](../../../02-prompt-engineering)

## আপনি যা শিখবেন

<img src="../../../translated_images/bn/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

পূর্ববর্তী মডিউলে, আপনি দেখেছেন কিভাবে মেমোরি কথোপকথনমূলক AI সক্ষম করে এবং GitHub মডেল ব্যবহার করে মৌলিক ইন্টার‌্যাকশনগুলো সম্পন্ন করেছেন। এখন আমরা ফোকাস করব কিভাবে আপনি প্রশ্ন করবেন — প্রম্পটগুলো নিজেই — Azure OpenAI এর GPT-5.2 ব্যবহার করে। আপনার প্রম্পট গঠন করার উপায় অত্যন্ত প্রভাব ফেলে আপনি প্রাপ্ত উত্তরের গুণমান। আমরা শুরু করব প্রাথমিক প্রম্পটিং কৌশলগুলো নিয়ে একটি পর্যালোচনা দিয়ে, তারপর এগিয়ে যাব আটটি উন্নত প্যাটার্নে যা GPT-5.2 এর পূর্ণ ক্ষমতা কাজে লাগায়।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তি নিয়ন্ত্রণ চালু করে — আপনি মডেলকে বলতে পারেন উত্তর দেওয়ার আগে কতটা চিন্তা করতে হবে। এটা বিভিন্ন প্রম্পটিং স্ট্র্যাটেজিগুলোর পার্থক্য স্পষ্ট করে এবং আপনাকে বুঝতে সাহায্য করে কখন কোন পদ্ধতি ব্যবহার করবেন। আমরা Azure এর বেশি সীমিত রেট লিমিটের সুবিধাও পাবো যা GPT-5.2 এর জন্য GitHub মডেল তুলনায় কম।

## পূর্বশর্ত

- মডিউল ০১ সম্পন্ন করা (Azure OpenAI রিসোর্স ডিপ্লয় করা)
- রুট ডিরেক্টরিতে `.env` ফাইল যা Azure ক্রেডেনশিয়াল নিয়ে তৈরি (মডিউল ০১ এ `azd up` দ্বারা তৈরি)

> **বিঃদ্রঃ** যদি আপনি মডিউল ০১ শেষ না করে থাকেন, প্রথমে সেখানে ডিপ্লয়মেন্ট নির্দেশাবলী অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

<img src="../../../translated_images/bn/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

প্রম্পট ইঞ্জিনিয়ারিং হল এমন ইনপুট টেক্সট ডিজাইন করা যা দেখে বোঝা যায় আপনি কি ফলাফল চাইবেন। এটা শুধুমাত্র প্রশ্ন করাই নয় — এটি এমনভাবে অনুরোধ সাজানো যাতে মডেল সঠিকভাবে বুঝতে পারে আপনি কি চান এবং কিভাবে তা সরবরাহ করতে হবে।

এটি ভাবুন একজন সহকর্মীকে নির্দেশ দেয়ার মতো। "বাগ ঠিক কর" অস্পষ্ট। "UserService.java ফাইলে লাইন ৪৫ এর নাল পয়েন্টার এক্সসেপশন ফিক্স করুন নাল চেক যোগ করে" নির্দিষ্ট। ভাষার মডেলও একইভাবে কাজ করে — স্পষ্টতা এবং গঠন গুরুত্বপূর্ণ।

<img src="../../../translated_images/bn/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j সরঞ্জাম ও ইন্সফ্রাংশট্রাকচার দেয় — মডেল সংযোগ, মেমোরি, মেসেজ টাইপ — আর প্রম্পট প্যাটার্নগুলো হল সাবধানে গঠিত টেক্সট যা এই ইন্সফ্রাংশট্রাকচারের মাধ্যমে পাঠানো হয়। প্রধান বিল্ডিং ব্লক হল `SystemMessage` (যা AI এর আচরণ ও রোল সেট করে) এবং `UserMessage` (যা আপনার প্রকৃত অনুরোধ বহন করে)।

## প্রম্পট ইঞ্জিনিয়ারিং মূলসূত্র

<img src="../../../translated_images/bn/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

এই মডিউলে উন্নত প্যাটার্নে যাওয়ার আগে, আসুন পাঁচটি মৌলিক প্রম্পটিং কৌশল পর্যালোচনা করি। এগুলো এমন ভিত্তি যা প্রত্যেক প্রম্পট ইঞ্জিনিয়ারকে জানা উচিত। আপনি যদি ইতিমধ্যে [Quick Start মডিউল](../00-quick-start/README.md#2-prompt-patterns) শেষ করে থাকেন, তাহলে এগুলো কর্মে দেখেছেন — এখানে এগুলোর ধারণাগত কাঠামো দেওয়া হল।

### জিরো-শট প্রম্পটিং

সবচেয়ে সহজ পদ্ধতি: মডেলকে সরাসরি কোনো উদাহরণ ছাড়াই নির্দেশ দেওয়া। মডেল সম্পূর্ণভাবে নিজের প্রশিক্ষণের ওপর নির্ভর করে কাজটি বুঝে এবং সম্পন্ন করে। এটা ভাল কাজ করে সহজ অনুরোধে যেখানে প্রত্যাশিত আচরণ স্পষ্ট।

<img src="../../../translated_images/bn/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*উদাহরণ ছাড়া সরাসরি নির্দেশ — মডেল কেবল নির্দেশ থেকে কাজ অনুমান করে*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// প্রতিক্রিয়া: "ইতিবাচক"
```

**কখন ব্যবহার করবেন:** সরল শ্রেণীবিভাগ, সরাসরি প্রশ্ন, অনুবাদ বা যেকোনো কাজ যেটা মডেল অতিরিক্ত গাইড ছাড়া করতে পারে।

### ফিউ-শট প্রম্পটিং

মডেলকে প্রমাণ দিন যাতে আপনি চাইছেন মডেল যে প্যাটার্ন অনুসরণ করুক। মডেল আপনার উদাহরণ থেকে ইনপুট-আউটপুট ফরম্যাট শিখে নেয় এবং নতুন ইনপুটে প্রয়োগ করে। এর ফলে এমন কাজের জন্য ধারাবাহিকতা অনেক বেশি থাকে যেখানে প্রত্যাশিত ফরম্যাট বা আচরণ স্পষ্ট নয়।

<img src="../../../translated_images/bn/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*উদাহরণ থেকে শেখা — মডেল প্যাটার্ন চিনে নতুন ইনপুটে প্রয়োগ করে*

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

**কখন ব্যবহার করবেন:** কাস্টম শ্রেণীবিভাগ, ধারাবাহিক বিন্যাস, ডোমেইন নির্দিষ্ট কাজ, বা যখন জিরো-শট ফলাফল অনিয়মিত হয়।

### চেইন অফ থট

মডেলকে ধাপে ধাপে তার যুক্তি দেখাতে বলুন। সরাসরি উত্তরে ঝাঁপিয়ে না গিয়ে, মডেল সমস্যা ভাঙ্গে এবং স্পষ্টভাবে প্রতিটি ধাপ বিবৃত করে। এর মাধ্যমে গণিত, লজিক, এবং মাল্টি-স্টেপ যুক্তিবিজ্ঞান কাজের সঠিকতা বাড়ে।

<img src="../../../translated_images/bn/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*ধাপে ধাপে যুক্তি — জটিল সমস্যা স্পষ্ট লজিকাল ধাপে ভাঙা*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// মডেলটি দেখায়: ১৫ - ৮ = ৭, তারপর ৭ + ১২ = ১৯ আপেল
```

**কখন ব্যবহার করবেন:** গণিত সমস্যা, লজিক পাজল, ডিবাগিং বা যেকোনো কাজ যেখানে যুক্তি দেখানোতে সঠিকতা ও বিশ্বাসযোগ্যতা বাড়ে।

### রোল-ভিত্তিক প্রম্পটিং

প্রশ্ন করার আগে AI এর জন্য একটি চরিত্র বা ভুমিকা সেট করুন। এটি এমন প্রেক্ষাপট দেয় যা উত্তরের সুর, গভীরতা, ও ফোকাসকে প্রভাবিত করে। "সফটওয়্যার আর্কিটেক্ট" "জুনিয়র ডেভেলপার" বা "সিকিউরিটি অডিটর" থেকে ভিন্ন পরামর্শ দেয়।

<img src="../../../translated_images/bn/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*প্রেক্ষাপট এবং চরিত্র নির্ধারণ — একই প্রশ্নে ভিন্ন ভুমিকা ভিন্ন উত্তর দেয়*

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

**কখন ব্যবহার করবেন:** কোড রিভিউ, শিক্ষকতা, ডোমেইন-নির্দিষ্ট বিশ্লেষণ, অথবা যখন বিশেষ দক্ষতা স্তর বা দৃষ্টিভঙ্গি অনুযায়ী উত্তর দরকার।

### প্রম্পট টেমপ্লেট

বিভিন্ন ভেরিয়েবল প্লেসহোল্ডার সহ পুনঃব্যবহারযোগ্য প্রম্পট তৈরি করুন। প্রতিবার নতুন প্রম্পট লেখার বদলে একবার টেমপ্লেট তৈরি করে নানা মান ব্যবহার করুন। LangChain4j এর `PromptTemplate` ক্লাস `{{variable}}` সিনট্যাক্স দিয়ে এটা সহজ করে তোলে।

<img src="../../../translated_images/bn/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*পুনঃব্যবহারযোগ্য প্রম্পট ভেরিয়েবল প্লেসহোল্ডার-সহ — এক টেমপ্লেট, বহু ব্যবহার*

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

**কখন ব্যবহার করবেন:** বিভিন্ন ইনপুট দিয়ে বারবার প্রশ্ন, ব্যাচ প্রসেসিং, পুনঃব্যবহারযোগ্য AI ওয়ার্কফ্লো তৈরি, বা যখন প্রম্পটের গঠন একই থাকে কিন্তু ডাটা পরিবর্তিত হয়।

---

এই পাঁচটি মূল বিষয় বেশিরভাগ প্রম্পটিং কাজের জন্য একটি শক্তিশালী টুলকিট প্রদান করে। এই মডিউলের বাকি অংশে আমরা এগুলোর উপর ভিত্তি করে **আটটি উন্নত প্যাটার্ন** দেখাবো যা GPT-5.2 এর যুক্তি নিয়ন্ত্রণ, স্ব-মূল্যায়ন এবং গঠনমূলক আউটপুটের ক্ষমতা ব্যবহার করে।

## উন্নত প্যাটার্নস

মূলসূত্রগুলি কভার করার পর, আসুন এগিয়ে যাই আটটি উন্নত প্যাটার্নে যা এই মডিউলটিকে বিশেষ করে তোলে। সব সমস্যার জন্য একই পদ্ধতি দরকার হয় না। কিছু প্রশ্নের দ্রুত উত্তর দরকার, অন্যগুলোর গভীর চিন্তা প্রয়োজন। কিছুতে দৃশ্যমান যুক্তি দরকার, অন্যগুলির কেবল ফলাফল। নিচের প্রতিটি প্যাটার্ন ভিন্ন পরিস্থিতির জন্য অপ্টিমাইজ করা হয়েছে এবং GPT-5.2 এর যুক্তি নিয়ন্ত্রণ পার্থক্যগুলো আরও স্বচ্ছ করে তোলে।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন ও তাদের ব্যবহারের সংক্ষিপ্ত বিবরণ*

<img src="../../../translated_images/bn/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 এর যুক্তি নিয়ন্ত্রণ আপনাকে নির্দিষ্ট করতে দেয় মডেল কতটা চিন্তা করবে — দ্রুত সরাসরি উত্তরের থেকে গভীর অনুসন্ধান পর্যন্ত*

**কম আগ্রহ (দ্রুত ও মনোযোগী)** - সহজ প্রশ্নের জন্য যেখানে দ্রুত সরাসরি উত্তর চান। মডেল কম যুক্তি করে — সর্বোচ্চ ২ ধাপ। গণনা, লুকআপ বা সরল প্রশ্নের জন্য ব্যবহার করুন।

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

> 💡 **GitHub Copilot দিয়ে অনুসন্ধান করুন:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) খুলে জিজ্ঞাসা করুন:
> - "কম আগ্রহ এবং বেশি আগ্রহ প্রম্পটিং প্যাটার্নের পার্থক্য কী?"
> - "প্রম্পটে XML ট্যাগ কিভাবে AI উত্তর গঠন করতে সাহায্য করে?"
> - "সেল্ফ-রিফ্লেকশন প্যাটার্ন কখন ব্যবহার করব সরাসরি নির্দেশনা বনাম?"

**বেশি আগ্রহ (গভীর ও বিস্তৃত)** - জটিল সমস্যার জন্য যেখানে বিস্তর বিশ্লেষণ দরকার। মডেল ভালোমত অনুসন্ধান করে বিস্তারিত যুক্তি দেখায়। সিস্টেম ডিজাইন, আর্কিটেকচার সিদ্ধান্ত বা জটিল গবেষণায় ব্যবহার করুন।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**টাস্ক এক্সিকিউশন (ধাপে ধাপে অগ্রগতি)** - মাল্টি-স্টেপ ওয়ার্কফ্লোর জন্য। মডেল আগাম একটি পরিকল্পনা দেয়, কাজের প্রতিটি ধাপ বর্ণনা করে, পরে সারাংশ দেয়। মাইগ্রেশন, বাস্তবায়ন বা যেকোনো মাল্টি-স্টেপ প্রক্রিয়ার জন্য ব্যবহার করুন।

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

চেইন-অফ-থট প্রম্পটিং স্পষ্টভাবে মডেলকে তার যুক্তিপূর্ণ প্রক্রিয়া দেখাতে বলে, যা জটিল কাজের সঠিকতা বাড়ায়। ধাপে ধাপে বিশ্লেষণ মানুষের এবং AI উভয়ের জন্যই যুক্তিবিজ্ঞান বুঝতে সাহায্য করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** এই প্যাটার্ন সম্পর্কে জিজ্ঞাসা করুন:
> - "দীর্ঘমেয়াদী প্রক্রিয়াগুলোর জন্য টাস্ক এক্সিকিউশন প্যাটার্ন কিভাবে মানানসই করব?"
> - "প্রডাকশন অ্যাপ্লিকেশনে টুল প্রিম্বল গঠনের সেরা প্র্যাকটিস কি?"
> - "কিভাবে UI তে মধ্যবর্তী অগ্রগতি আপডেট ধারণ ও প্রদর্শন করব?"

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*পরিকল্পনা → বাস্তবায়ন → সারাংশ মাল্টি-স্টেপ কাজের জন্য ওয়ার্কফ্লো*

**সেল্ফ-রিফ্লেক্টিং কোড** - প্রোডাকশন মানের কোড তৈরি করার জন্য। মডেল প্রোডাকশন স্ট্যান্ডার্ড অনুসারে কোড তৈরি করে উপযুক্ত ত্রুটি হ্যান্ডলিং সহ। নতুন ফিচার বা সার্ভিস গড়ার সময় ব্যবহার করুন।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*ইটারেটিভ উন্নয়ন চক্র - তৈরি করা, মূল্যায়ন করা, সমস্যা চিহ্নিত করা, উন্নত করা, পুনরাবৃত্তি*

**স্ট্রাকচার্ড অ্যানালাইসিস** - ধারাবাহিক মূল্যায়নের জন্য। মডেল একটি নির্দিষ্ট ফ্রেমওয়ার্ক অনুযায়ী কোড রিভিউ করে (সঠিকতা, প্র্যাকটিস, পারফরমেন্স, নিরাপত্তা, রক্ষণাবেক্ষণযোগ্যতা)। কোড রিভিউ বা গুণগত মূল্যায়নের জন্য ব্যবহার করুন।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** স্ট্রাকচার্ড অ্যানালাইসিস সম্পর্কে জিজ্ঞাসা করুন:
> - "বিভিন্ন প্রকার কোড রিভিউয়ের জন্য অ্যানালাইসিস ফ্রেমওয়ার্ক কিভাবে কাস্টমাইজ করব?"
> - "প্রোগ্রাম্যাটিকভাবে স্ট্রাকচার্ড আউটপুট পার্স এবং প্রয়োগের সেরা উপায় কী?"
> - "কিভাবে বিভিন্ন রিভিউ সেশনে ধারাবাহিক সেভারিটি লেভেল নিশ্চিত করব?"

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*সেভারিটি লেভেলসহ ধারাবাহিক কোড রিভিউর জন্য কাঠামো*

**মাল্টি-টার্ন চ্যাট** - এমন কথোপকথনের জন্য যা প্রেক্ষাপট প্রয়োজন। মডেল পূর্ববর্তী মেসেজ মনে রাখে এবং সে অনুযায়ী উত্তর গড়ে তোলে। ইন্টারেক্টিভ হেল্প সেশন বা জটিল প্রশ্নোত্তরের জন্য ব্যবহার করুন।

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

*কত বার টার্নের মধ্যে কথোপকথনের প্রেক্ষাপট জমা হয় যতক্ষন পর্যন্ত টোকেন সীমায় পৌঁছায়*

**ধাপে ধাপে যুক্তি** - দৃশ্যমান লজিক দরকার এমন সমস্যার জন্য। মডেল প্রতিটি ধাপের স্পষ্ট যুক্তি দেখায়। গণিত সমস্যা, লজিক পাজল বা চিন্তার প্রক্রিয়া বোঝার জন্য ব্যবহার করুন।

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

*সমস্যাগুলো স্পষ্ট লজিক্যাল ধাপে ভাঙা*

**সীমাবদ্ধ আউটপুট** - নির্দিষ্ট ফরম্যাট নিয়মাবলী অনুযায়ী উত্তর দরকার হলে। মডেল কঠোরভাবে ফরম্যাট ও দৈর্ঘ্য নিয়ম মেনে চলে। সারাংশ বা সুনির্দিষ্ট আউটপুট কাঠামো প্রয়োজন হলে ব্যবহার করুন।

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

*নির্দিষ্ট ফরম্যাট, দৈর্ঘ্য এবং কাঠামো বজায় রাখা*

## বিদ্যমান Azure রিসোর্স ব্যবহার

**ডিপ্লয়মেন্ট যাচাই করুন:**

রুট ডিরেক্টরিতে `.env` ফাইলটি আছে নিশ্চিত করুন যার মধ্যে Azure ক্রেডেনশিয়াল রয়েছে (মডিউল ০১ এর সময় তৈরি):

```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**অ্যাপ্লিকেশন শুরু করুন:**

> **বিঃদ্রঃ:** যদি আপনি ইতিমধ্যেই সব অ্যাপ্লিকেশন চালু করে থাকেন `./start-all.sh` থেকে মডিউল ০১, তাহলে এই মডিউল ইতিমধ্যে পোর্ট ৮০৮৩ এ চলমান। আপনি নিচের শুরু কমান্ডগুলো এড়িয়ে সরাসরি http://localhost:8083 এ যেতে পারেন।

**অপশন ১: স্প্রিং বুট ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য প্রস্তাবিত)**

ডেভ কন্টেইনারে স্প্রিং বুট ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত আছে, যা সব স্প্রিং বুট অ্যাপ্লিকেশনগুলি পরিচালনার জন্য ভিজ্যুয়াল ইন্টারফেস দেয়। এটি VS Code এর বাম পাশে অ্যাক্টিভিটি বারে (স্প্রিং বুট আইকন অনুসন্ধান করুন) পাওয়া যাবে।

স্প্রিং বুট ড্যাশবোর্ড থেকে আপনি পারেন:
- কর্মস্থলে সব স্প্রিং বুট অ্যাপ্লিকেশন দেখতে
- এক ক্লিকে অ্যাপ্লিকেশন শুরু/বন্ধ করতে
- বাস্তব সময় অ্যাপ্লিকেশন লগ দেখতে
- অ্যাপ্লিকেশন স্থিতি মনিটর করতে
কেবল "prompt-engineering" এর পাশে প্লে বোতামে ক্লিক করুন এই মডিউলটি শুরু করতে, অথবা একবারে সব মডিউল শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার করা**

সব ওয়েব অ্যাপ্লিকেশন (মডিউল ০১-০৪) শুরু করুন:

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

অথবা শুধু এই মডিউলটি শুরু করুন:

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে রুট `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করে এবং JAR ফাইলগুলি তৈরি করবে যদি সেগুলি না থাকে।

> **দ্রষ্টব্য:** আপনি যদি শুরু করার আগে সমস্ত মডিউল ম্যানুয়ালি তৈরি করতে চান:
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
cd .. && ./stop-all.sh  # সব মডিউলগুলি
```

**PowerShell:**
```powershell
.\stop.ps1  # এই মডিউল মাত্র
# অথবা
cd ..; .\stop-all.ps1  # সমস্ত মডিউলসমূহ
```

## অ্যাপ্লিকেশন স্ক্রিনশট

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*প্রধান ড্যাশবোর্ড যা সমস্ত ৮ টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন তাদের বৈশিষ্ট্য এবং ব্যবহারের ক্ষেত্রে প্রদর্শন করে*

## প্যাটার্নগুলো অন্বেষণ

ওয়েব ইন্টারফেস আপনাকে বিভিন্ন প্রম্পটিং কৌশলগুলো পরীক্ষা করার সুযোগ দেয়। প্রতিটি প্যাটার্ন বিভিন্ন সমস্যা সমাধান করে - এগুলো চেষ্টা করুন দেখতে কখন কোন পদ্ধতি কার্যকর হয়।

### কম ও উচ্চ উদ্যমতা

কম উদ্যমতা ব্যবহার করে একটি সহজ প্রশ্ন করুন যেমন "২০০ এর ১৫% কত?" আপনি পাবেন একটি তাৎক্ষণিক, সরাসরি উত্তর। এখন জটিল কিছু জিজ্ঞাসা করুন যেমন "উচ্চ ট্রাফিক API এর জন্য ক্যাশিং স্ট্রাটেজি ডিজাইন করুন" উচ্চ উদ্যমতা ব্যবহার করে। দেখুন কিভাবে মডেল ধীরে কাজ করে এবং বিস্তারিত বিশ্লেষণ প্রদান করে। একই মডেল, একই প্রশ্ন কাঠামো - কিন্তু প্রম্পট নির্দেশ দেয় কতটা চিন্তা করতে হবে।

<img src="../../../translated_images/bn/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*কম বিশ্লেষণসহ দ্রুত গণনা*

<img src="../../../translated_images/bn/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*বিস্তৃত ক্যাশিং স্ট্রাটেজি (২.৮MB)*

### টাস্ক এক্সিকিউশন (টুল প্রিম্বল)

বহু ধাপবিশিষ্ট ওয়ার্কফ্লোতে আগাম পরিকল্পনা ও অগ্রগতির বর্ণনা উপকারী। মডেল প্রথমে কী করবে তা ব্যাখ্যা করে, প্রতিটি ধাপ বর্ণনা করে, এরপর ফলাফল সংক্ষেপ করে।

<img src="../../../translated_images/bn/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ধাপে ধাপে বর্ণনা সহ REST এন্ডপয়েন্ট তৈরি (৩.৯MB)*

### স্ব-মূল্যায়ন কোড

"একটি ইমেইল ভ্যালিডেশন সার্ভিস তৈরী করুন" চেষ্টা করুন। শুধু কোড তৈরি করে থামানোর পরিবর্তে, মডেল কোড তৈরি করে, মানদণ্ড অনুযায়ী মূল্যায়ন করে, দুর্বলতা চিহ্নিত করে এবং উন্নতি করে। আপনি দেখতে পাবেন এটি পুনরাবৃত্তি করে যতক্ষণ না কোড প্রোডাকশন মান পূরণ করে।

<img src="../../../translated_images/bn/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*সম্পূর্ণ ইমেইল ভ্যালিডেশন সার্ভিস (৫.২MB)*

### গঠনমূলক বিশ্লেষণ

কোড রিভিউতে ধারাবাহিক মূল্যায়ন কাঠামো প্রয়োজন। মডেল নির্দিষ্ট ক্যাটেগরির (সঠিকতা, প্র্যাকটিস, কর্মক্ষমতা, সুরক্ষা) ভিত্তিতে বিশ্লেষণ করে গুরত্বের মাত্রা প্রদান করে।

<img src="../../../translated_images/bn/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ফ্রেমওয়ার্ক ভিত্তিক কোড রিভিউ*

### বহু-বার্তালাপ চ্যাট

"Spring Boot কী?" জিজ্ঞাসা করুন, তারপর সাথে সাথেই "একটি উদাহরণ দেখাও" বলুন। মডেল আপনার প্রথম প্রশ্ন মনে রাখে এবং আপনাকে স্পেসিফিক Spring Boot উদাহরণ দেয়। স্মৃতি ছাড়া, দ্বিতীয় প্রশ্নটি খুব অস্পষ্ট হত।

<img src="../../../translated_images/bn/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*প্রশ্নগুলোর মধ্যে প্রাসঙ্গিকতা সংরক্ষণ*

### ধাপে ধাপে বিশ্লেষণ

একটি গণিত সমস্যা বেছে নিয়ে এটিকে ধাপে ধাপে বিশ্লেষণ এবং কম উদ্যমতা উভয় মাধ্যমে চেষ্টা করুন। কম উদ্যমতা শুধু দ্রুত উত্তর দেয় - কিন্তু অস্পষ্ট। ধাপে ধাপে আপনাকে প্রতিটি গণনা ও সিদ্ধান্ত দেখায়।

<img src="../../../translated_images/bn/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*স্পষ্ট ধাপ সহ গণিত সমস্যা*

### সীমিত আউটপুট

যখন নির্দিষ্ট ফরম্যাট বা শব্দসংখ্যার প্রয়োজন হয়, এই প্যাটার্ন কঠোর নিয়ম মানায়। ঠিক ১০০ শব্দের বুলেট পয়েন্ট ফরম্যাটে একটি সারাংশ তৈরি করার চেষ্টা করুন।

<img src="../../../translated_images/bn/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ফরম্যাট নিয়ন্ত্রণ সহ মেশিন লার্নিং সারাংশ*

## আপনি আসলেই যা শিখছেন

**যুক্তিবাদী প্রচেষ্টা সবকিছু পরিবর্তন করে**

GPT-5.2 আপনাকে আপনার প্রম্পটের মাধ্যমে কম্পিউটেশনাল প্রচেষ্টার নিয়ন্ত্রণ দেয়। কম প্রচেষ্টা মানে দ্রুত উত্তর যা কম অনুসন্ধান করে। উচ্চ প্রচেষ্টা মানে মডেল গভীরভাবে চিন্তা করতে সময় নেয়। আপনি শিখছেন কাজের জটিলতার সাথে প্রচেষ্টার মেলবন্ধন করতে - সহজ প্রশ্নে সময় নষ্ট করবেন না, জটিল সিদ্ধান্তেও দ্রুত করবেন না।

**গঠন আচরণ নির্দেশ করে**

প্রম্পটের XML ট্যাগগুলো লক্ষ্য করেছেন? সেগুলো কেবল সাজানোর জন্য নয়। মডেল গঠনবদ্ধ নির্দেশনাগুলো অনুসরণ করে অবাধ লেখার থেকে বেশি নির্ভরযোগ্যভাবে। যখন মাল্টি-স্টেপ প্রক্রিয়া বা জটিল লজিকের প্রয়োজন হয়, গঠন মডেলকে সাহায্য করে কোথায় আছে এবং পরবর্তী কী তা বুঝতে।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*স্পষ্ট বিভাগ এবং XML-স্টাইল সংগঠনের সাথে একটি ভাল গঠনকৃত প্রম্পটের গঠন*

**স্ব-মূল্যায়নের মাধ্যমে গুণগত মান**

স্ব-মূল্যায়ন প্যাটার্নগুলো মানদণ্ড স্পষ্ট করে। মডেল "সঠিকভাবে করে" আশায় না থেকে, আপনি তাকে বলছেন "সঠিক" মানে কী: সঠিক লজিক, ত্রুটি হ্যান্ডলিং, কর্মক্ষমতা, নিরাপত্তা। এরপর মডেল নিজেই তার আউটপুট মূল্যায়ন করে উন্নতি করে। এটি কোড তৈরীকে অন্যরকম লটারির বদলে প্রক্রিয়ায় পরিণত করে।

**প্রসঙ্গ সীমিত**

বহু-বার্তালাপ চ্যাটে প্রতিটি অনুরোধে বার্তা ইতিহাস অন্তর্ভুক্ত থাকে। কিন্তু একটি সীমা আছে - প্রতিটি মডেলের সর্বোচ্চ টোকেন সংখ্যা। কথোপকথন বাড়ার সাথে সাথে আপনাকে কৌশল নিতে হবে যাতে প্রাসঙ্গিক প্রসঙ্গ রাখা যায় ছাদের ছিঁড়ে না যাওয়া পর্যন্ত। এই মডিউলটি দেখায় স্মৃতি কিভাবে কাজ করে; পরে আপনি শিখবেন কখন সারাংশ তৈরি করতে হবে, কখন ভুলে যেতে হবে, এবং কখন পুনরুদ্ধার করতে হবে।

## পরবর্তী পদক্ষেপ

**পরবর্তী মডিউল:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল ০১ - পরিচিতি](../01-introduction/README.md) | [মেইনে ফিরুন](../README.md) | [পরবর্তী: মডিউল ০৩ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকার**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি এর নিজস্ব ভাষায় সর্বোত্তম ও কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানুষের অনুবাদ নেওয়াই উপযুক্ত। এই অনুবাদ ব্যবহারের ফলে কোনো বিভ্রান্তি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->