# মডিউল ০২: GPT-5.2 সহ প্রম্পট ইঞ্জিনিয়ারিং

## বিষয়বস্তুসমূহ

- [ভিডিও ওয়াকথ্রু](../../../02-prompt-engineering)
- [আপনি যা শিখবেন](../../../02-prompt-engineering)
- [প্রাকশর্ত](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং এর মৌলিক বিষয়াবলী](../../../02-prompt-engineering)
  - [জিরো-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [ফিউ-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [চেইন অফ থট](../../../02-prompt-engineering)
  - [রোল-ভিত্তিক প্রম্পটিং](../../../02-prompt-engineering)
  - [প্রম্পট টেমপ্লেটস](../../../02-prompt-engineering)
- [উন্নত প্যাটার্নস](../../../02-prompt-engineering)
- [বিদ্যমান Azure রিসোর্স ব্যবহার](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রিনশটস](../../../02-prompt-engineering)
- [প্যাটার্নস অনুসন্ধান](../../../02-prompt-engineering)
  - [কম বনাম বেশি উৎসুকতা](../../../02-prompt-engineering)
  - [টাস্ক এক্সিকিউশন (টুল প্রিমেবলস)](../../../02-prompt-engineering)
  - [আত্ম-প্রতিফলিত কোড](../../../02-prompt-engineering)
  - [গঠিত বিশ্লেষণ](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [ধাপে ধাপে যুক্তি](../../../02-prompt-engineering)
  - [সীমাবদ্ধ আউটপুট](../../../02-prompt-engineering)
- [আপনি প্রকৃতপক্ষে যা শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী পদক্ষেপ](../../../02-prompt-engineering)

## ভিডিও ওয়াকথ্রু

এই লাইভ সেশনটি দেখুন যা দেখায় কীভাবে এই মডিউলটি শুরু করতে হয়:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## আপনি যা শিখবেন

<img src="../../../translated_images/bn/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

পূর্ববর্তী মডিউলে, আপনি দেখেছিলেন কীভাবে মেমোরি কথোপকথনমূলক AI সক্ষম করে এবং সাধারণ ইন্টারঅ্যাকশনের জন্য GitHub মডেল ব্যবহার করেছেন। এখন আমরা কেন্দ্রিত হব কীভাবে আপনি প্রশ্ন করেন—সেগুলো নিজেই প্রম্পট হিসেবে—Azure OpenAI-এর GPT-5.2 ব্যবহার করে। আপনি কীভাবে প্রম্পট গঠন করেন তা নাটকীয়ভাবে আপনার উত্তরগুলোর গুণগত মান প্রভাবিত করে। আমরা মৌলিক প্রম্পটিং কৌশলগুলি পর্যালোচনা দিয়ে শুরু করব, তারপর GPT-5.2-এর সম্পূর্ণ ক্ষমতা ব্যবহার করে আটটি উন্নত প্যাটার্নে প্রবেশ করব।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তির নিয়ন্ত্রণ পরিচয় করায় - আপনি মডেলকে বলতে পারেন উত্তর দেওয়ার আগে কতটা চিন্তা করা উচিত। এটি বিভিন্ন প্রম্পটিং কৌশলকে আরো স্পষ্ট করে তোলে এবং বুঝতে সাহায্য করে কখন কোন পদ্ধতি ব্যবহার করতে হবে। এছাড়াও, Azure-এর GPT-5.2 জন্য GitHub মডেলের তুলনায় কম রেট সীমাবদ্ধতা রয়েছে, যা সুবিধাজনক।

## প্রাকশর্ত

- মডিউল ০১ সম্পন্ন হয়েছে (Azure OpenAI রিসোর্স স্থাপিত)
- রুট ডিরেক্টরিতে `.env` ফাইল আছে Azure ক্রেডেনশিয়াল সহ (মডিউল ০১-এ `azd up` দিয়ে তৈরি)

> **দ্রষ্টব্য:** আপনি যদি মডিউল ০১ সম্পন্ন না করে থাকেন, প্রথমে সেখানে ডিপ্লয়মেন্ট নির্দেশনাবলী অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

<img src="../../../translated_images/bn/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

প্রম্পট ইঞ্জিনিয়ারিং বলতে বুঝায় এমন ইনপুট টেক্সট ডিজাইন করা যা ধারাবাহিকভাবে আপনার প্রয়োজনীয় ফলাফলগুলো দেয়। এটি শুধু প্রশ্ন করার ব্যাপার নয় - বরং এমনভাবে অনুরোধ গঠন করা যাতে মডেল স্পষ্টভাবে বুঝতে পারে আপনি কী চাইছেন এবং কীভাবে তা দিতে হবে।

ধরুন এটি একটি সহকর্মীকে নির্দেশ দেওয়ার মত। "বাগ ঠিক কর" অস্পষ্ট। "UserService.java লাইনের ৪৫-এ নাল পয়েন্টার এক্সসেপশন ঠিক কর একটি নাল চেক যোগ করে" স্পষ্ট। ভাষা মডেলগুলোও একইভাবে কাজ করে — নির্দিষ্টতা এবং গঠন গুরুত্বপূর্ণ।

<img src="../../../translated_images/bn/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j প্রদান করে অবকাঠামো—মডেল সংযোগ, মেমোরি, এবং মেসেজ ধরণ—আর প্রম্পট প্যাটার্নস হলো সেই সাবধানে গঠিত টেক্সট যা আপনি ঐ অবকাঠামোর মাধ্যমে পাঠান। মূল নির্মাণ ব্লক হলো `SystemMessage` (যা AI এর আচরণ ও ভূমিকা নির্ধারণ করে) এবং `UserMessage` (যা আপনার প্রকৃত অনুরোধ বহন করে)।

## প্রম্পট ইঞ্জিনিয়ারিং এর মৌলিক বিষয়াবলী

<img src="../../../translated_images/bn/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

এই মডিউলের উন্নত প্যাটার্নে প্রবেশ করার আগে, আসুন পাঁচটি মৌলিক প্রম্পটিং কৌশল পর্যালোচনা করি। এগুলো হলো এমন মূল ভিত্তি যা প্রতিটি প্রম্পট ইঞ্জিনিয়ারকে জানতে হবে। আপনি যদি ইতিমধ্যেই [কুইক স্টার্ট মডিউল](../00-quick-start/README.md#2-prompt-patterns) সম্পন্ন করে থাকেন, তাহলে এগুলো কাজের মধ্যে দেখেছেন—এখানে তাদের ভাবনাগত কাঠামো।

### জিরো-শট প্রম্পটিং

সরাসরি নির্দেশ দিয়ে সবচেয়ে সহজ পদ্ধতি: কোনো উদাহরণ ছাড়াই মডেলকে কাজ করার জন্য নির্দেশ দেয়া হয়। মডেল সম্পূর্ণরূপে তার প্রশিক্ষণের ওপর নির্ভর করে কাজ বুঝে ও সম্পাদন করে। সরল অনুরোধের জন্য এটি ভাল কাজ করে যেখানে প্রত্যাশিত আচরণ স্বচ্ছ।

<img src="../../../translated_images/bn/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*উদাহরণ ছাড়াই সরাসরি নির্দেশ—মডেল শুধুমাত্র নির্দেশ থেকে কাজ অনুমান করে*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// প্রতিক্রিয়া: "ধনাত্মক"
```

**কখন ব্যবহার করবেন:** সাধারণ শ্রেণীবিভাগ, সরাসরি প্রশ্ন, অনুবাদ বা যেকোনো কাজ যেখানে অতিরিক্ত নির্দেশনার প্রয়োজন হয় না।

### ফিউ-শট প্রম্পটিং

উদাহরণ প্রদান করুন যা মডেলকে কাঙ্ক্ষিত প্যাটার্ন অনুসরণ করতে শেখায়। মডেল উদাহরণ থেকে ইনপুট-আউটপুট ফরম্যাট শিখে এবং নতুন ইনপুটে প্রয়োগ করে। যেসব কাজের কাঙ্ক্ষিত ফরম্যাট বা আচরণ স্পষ্ট নয় সেগুলোর জন্য এটি ধারাবাহিকতা নাটকীয়ভাবে উন্নত করে।

<img src="../../../translated_images/bn/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*উদাহরণ থেকে শেখা—মডেল প্যাটার্ন চিহ্নিত করে এবং নতুন ইনপুটে প্রয়োগ করে*

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

**কখন ব্যবহার করবেন:** কাস্টম শ্রেণীবিভাগ, ধারাবাহিক ফরম্যাটিং, ডোমেন-মুখী কাজ, অথবা যখন জিরো-শট ফলাফল অসঙ্গতিপূর্ণ হয়।

### চেইন অফ থট

মডেলকে তার যুক্তি ধাপে ধাপে প্রদর্শন করতে বলুন। সরাসরি উত্তর দেওয়ার পরিবর্তে মডেল সমস্যা ভাঙে এবং প্রতিটি অংশ স্পষ্টভাবে কাজ করে। এটি গণিত, যুক্তি, এবং বহু-ধাপ যুক্তির কাজের নির্ভুলতা বাড়ায়।

<img src="../../../translated_images/bn/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*ধাপে ধাপে যুক্তি—জটিল সমস্যা স্পষ্ট যুক্তিসঙ্গত ধাপে ভাগ করা*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// মডেলটি দেখায়: ১৫ - ৮ = ৭, তারপর ৭ + ১২ = ১৯ আপেল
```

**কখন ব্যবহার করবেন:** গণিত সমস্যা, যুক্তি পাজল, ডিবাগিং, বা যেকোনো কাজ যেখানে যুক্তি প্রকাশ নির্ভুলতা ও বিশ্বাসযোগ্যতা বাড়ায়।

### রোল-ভিত্তিক প্রম্পটিং

আপনার প্রশ্ন করার আগে AI এর জন্য একটি ব্যক্তিত্ব বা ভূমিকা সেট করুন। এটি এমন প্রসঙ্গ দেয় যা প্রতিক্রিয়ার ধরণ, গভীরতা, এবং ফোকাস নির্ধারণ করে। "সফটওয়্যার আর্কিটেক্ট" "জুনিয়র ডেভেলপার" অথবা "সিকিউরিটি অডিটার" থেকে আলাদা পরামর্শ দেয়।

<img src="../../../translated_images/bn/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*প্রসঙ্গ ও ব্যক্তিত্ব নির্ধারণ—একই প্রশ্নে বিভিন্ন ভূমিকার ভিত্তিতে আলাদা উত্তর*

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

**কখন ব্যবহার করবেন:** কোড পর্যালোচনা, টিউটরিং, ডোমেন-মুখী বিশ্লেষণ, অথবা যখন নির্দিষ্ট দক্ষতা স্তর বা দৃষ্টিভঙ্গি অনুযায়ী প্রতিক্রিয়া প্রয়োজন।

### প্রম্পট টেমপ্লেটস

পরিবর্তনীয় প্লেসহোল্ডার সহ পুনরায় ব্যবহারযোগ্য প্রম্পট তৈরি করুন। প্রতিবার নতুন প্রম্পট লেখার পরিবর্তে একটি টেমপ্লেট তৈরি করুন এবং বিভিন্ন মান পূরণ করুন। LangChain4j এর `PromptTemplate` ক্লাস `{{variable}}` সিনট্যাক্স সহ এটি সহজ করে তোলে।

<img src="../../../translated_images/bn/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*পরিবর্তনীয় প্লেসহোল্ডার সহ পুনঃব্যবহারযোগ্য প্রম্পট—একটি টেমপ্লেট, বহু ব্যবহার*

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

**কখন ব্যবহার করবেন:** বিভিন্ন ইনপুট নিয়ে পুনরাবৃত্ত অনুরোধ, ব্যাচ প্রসেসিং, পুনঃব্যবহারযোগ্য AI ওয়ার্কফ্লো তৈরি, অথবা যেকোনো পরিস্থিতিতে যেখানে প্রম্পটের কাঠামো একই থাকে কিন্তু ডেটা পরিবর্তিত হয়।

---

এই পাঁচটি মৌলিক বিষয় আপনাকে বেশিরভাগ প্রম্পটিং কাজের জন্য একটি শক্তিশালী টুলকিট দেয়। এই মডিউলের বাকি অংশ এইগুলোর ওপর ভিত্তি করে তৈরি করে **আটটি উন্নত প্যাটার্ন** যা GPT-5.2 এর যুক্তির নিয়ন্ত্রণ, আত্ম-মূল্যায়ন, এবং গঠিত আউটপুটের ক্ষমতাগুলো ব্যবহার করে।

## উন্নত প্যাটার্নস

মৌলিক বিষয়গুলো পূর্ণ করে, আসুন সেই আটটি উন্নত প্যাটার্নে যাই যা এই মডিউলটিকে অনন্য করে তোলে। সব সমস্যা একই পদ্ধতি প্রয়োজন হয় না। কিছু প্রশ্ন দ্রুত উত্তর চায়, অন্যরা গভীর চিন্তা চায়। কিছুতে দৃশ্যমান যুক্তি প্রয়োজন, অন্যদের শুধু ফলাফল দরকার। নিচের প্রতিটি প্যাটার্ন ভিন্ন পরিস্থিতির জন্য অপ্টিমাইজড—আর GPT-5.2 এর যুক্তির নিয়ন্ত্রণ এই পার্থক্যগুলো আরো স্পষ্ট করে তোলে।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*প্রম্পট ইঞ্জিনিয়ারিং এর আটটি প্যাটার্ন এবং তাদের ব্যবহারের সারাংশ*

<img src="../../../translated_images/bn/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 এর যুক্তি নিয়ন্ত্রণ আপনাকে মডেলকে কতটা চিন্তা করতে হবে নির্দিষ্ট করতে দেয়—দ্রুত সরাসরি উত্তর থেকে গভীর অন্বেষণ পর্যন্ত*

**কম উৎসুকতা (দ্রুত ও কেন্দ্রীভূত)** - সরল প্রশ্নের জন্য যেখানে দ্রুত, সরাসরি উত্তর চান। মডেল ন্যূনতম যুক্তি প্রয়োগ করে - সর্বোচ্চ ২ ধাপ। হিসাব, অনুসন্ধান, বা সরল প্রশ্নের জন্য ব্যবহার করুন।

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

> 💡 **GitHub Copilot দিয়ে অনুসন্ধান করুন:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) খুলে জিজ্ঞাসা করুন:
> - "কম উৎসুকতা এবং উচ্চ উৎসুকতা প্রম্পটিং প্যাটার্নের মধ্যে পার্থক্য কী?"
> - "প্রম্পটের XML ট্যাগগুলো কীভাবে AI এর প্রতিক্রিয়া গঠন করতে সাহায্য করে?"
> - "আমি কখন আত্ম-প্রতিফলন প্যাটার্ন ব্যবহার করব এবং কখন সরাসরি নির্দেশ দেব?"

**বেশি উৎসুকতা (গভীর ও পরিপূর্ণ)** - জটিল সমস্যার জন্য যেখানে পূর্ণাঙ্গ বিশ্লেষণ চান। মডেল গভীরভাবে অনুসন্ধান করে এবং বিস্তারিত যুক্তি প্রদর্শন করে। সিস্টেম ডিজাইন, আর্কিটেকচার সিদ্ধান্ত, বা জটিল গবেষণার জন্য ব্যবহার করুন।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**টাস্ক এক্সিকিউশন (ধাপে ধাপে অগ্রগতি)** - বহু ধাপের ওয়ার্কফ্লোর জন্য। মডেল আগে পরিকল্পনা দেয়, প্রত্যেক ধাপ বর্ণনা করে, পরে সারমর্ম দেয়। মাইগ্রেশন, বাস্তবায়ন, বা যেকোনো বহু-ধাপ প্রক্রিয়ার জন্য ব্যবহার করুন।

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

চেইন-অফ-থট প্রম্পটিং স্পষ্টভাবে মডেলকে তার যুক্তি দেখাতে বলে, জটিল কাজের নির্ভুলতা বাড়ায়। ধাপে ধাপে বিচ্ছেদ মানুষ ও AI উভয়েরই লজিক বুঝতে সাহায্য করে।

> **🤖 GitHub Copilot Chat সঙ্গে চেষ্টা করুন:** এই প্যাটার্ন নিয়ে জিজ্ঞাসা করুন:
> - "দীর্ঘমেয়াদী অপারেশনের জন্য আমি কিভাবে টাস্ক এক্সিকিউশন প্যাটার্ন উপযোগী করব?"
> - "প্রোডাকশনে টুল প্রিমেবল গঠনের জন্য সেরা অনুশীলন কী?"
> - "কিভাবে UI তে মধ্যবর্তী অগ্রগতি আপডেট ধরে এবং প্রদর্শন করব?"

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*পরিকল্পনা → বাস্তবায়ন → সারমর্মের ওয়ার্কফ্লো বহু ধাপের কাজের জন্য*

**আত্ম-প্রতিফলিত কোড** - প্রোডাকশন-মানের কোড তৈরি করার জন্য। মডেল প্রোডাকশনের মানদণ্ড অনুসারে কোড জেনারেট করে সঠিক ত্রুটি পরিচালনাসহ। নতুন ফিচার বা সার্ভিস তৈরির জন্য ব্যবহার করুন।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*পুনরাবৃত্তিমূলক উন্নতির চক্র - তৈরি, মূল্যায়ন, সমস্যা শনাক্ত, উন্নতি, পুনরাবৃত্তি*

**গঠিত বিশ্লেষণ** - ধারাবাহিক মূল্যায়নের জন্য। মডেল একটি নির্দিষ্ট কাঠামো (সঠিকতা, অনুশীলন, পারফরম্যান্স, সিকিউরিটি, রক্ষণাবেক্ষণ) ব্যবহার করে কোড পর্যালোচনা করে। কোড রিভিউ বা মান যাচাইয়ের জন্য ব্যবহার করুন।

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

> **🤖 GitHub Copilot Chat এর সাথে চেষ্টা করুন:** গঠিত বিশ্লেষণ নিয়ে জিজ্ঞাসা করুন:
> - "বিভিন্ন প্রকার কোড পর্যালোচনার জন্য আমি কিভাবে বিশ্লেষণ কাঠামোকে কাস্টমাইজ করব?"
> - "গঠিত আউটপুট প্রোগ্রাম্যাটিকভাবে পার্স এবং পরিচালনার সেরা উপায় কী?"
> - "বিভিন্ন পর্যালোচনা সেশনের মধ্যে ধারাবাহিক সেভেরিটি লেভেল নিশ্চিত করার উপায় কী?"

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*ধারাবাহিক কোড পর্যালোচনার জন্য কাঠামো এবং সেভেরিটি লেভেল*

**মাল্টি-টার্ন চ্যাট** - যেখানে প্রসঙ্গের প্রয়োজন এমন কথোপকথনের জন্য। মডেল পূর্বের বার্তাগুলো মনে রাখে এবং সেগুলোর ওপর ভিত্তি করে তৈরি করে। ইন্টারেক্টিভ সাহায্য সেশন বা জটিল Q&A জন্য ব্যবহার করুন।

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

*বহু টার্ন জুড়ে কথোপকথনের প্রসঙ্গ কিভাবে জমে যতক্ষণ না টোকেন সীমা পৌঁছায়*

**ধাপে ধাপে যুক্তি** - দৃশ্যমান লজিকের প্রয়োজন এমন সমস্যার জন্য। মডেল প্রতিটি ধাপের জন্য স্পষ্ট যুক্তি প্রদর্শন করে। গণিত সমস্যা, যুক্তি পাজল, বা চিন্তার প্রক্রিয়া বোঝার জন্য ব্যবহার করুন।

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

*সমস্যা স্পষ্ট যুক্তির ধাপে ভাগ করে*

**সীমাবদ্ধ আউটপুট** - নির্দিষ্ট ফরম্যাটের জন্য প্রতিক্রিয়া। মডেল ফরম্যাট এবং দৈর্ঘ্যের নিয়ম কঠোরভাবে অনুসরণ করে। সারাংশ বা যখন সুনির্দিষ্ট আউটপুট কাঠামোর প্রয়োজন হয় তখন ব্যবহার করুন।

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

*নির্দিষ্ট ফরম্যাট, দৈর্ঘ্য ও কাঠামোর নিয়ম আরোপ*

## বিদ্যমান Azure রিসোর্স ব্যবহার

**ডিপ্লয়মেন্ট যাচাই করুন:**

রুট ডিরেক্টরিতে `.env` ফাইল আছে নিশ্চিত করুন Azure ক্রেডেনশিয়ালসহ (মডিউল ০১ এর সময় তৈরি):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT প্রদর্শন করা উচিত
```

**অ্যাপ্লিকেশন চালু করুন:**

> **দ্রষ্টব্য:** আপনি যদি ইতিমধ্যে মডিউল ০১ থেকে `./start-all.sh` দিয়ে সব অ্যাপ্লিকেশন চালু করে থাকেন, তাহলে এই মডিউলটি পোর্ট ৮০৮৩-এ চলছে। নিচের স্টার্ট কমান্ডগুলো এড়িয়ে সরাসরি http://localhost:8083 যান।
**বিকল্প ১: স্প্রিং বুট ড্যাশবোর্ড ব্যবহার করা (VS কোড ব্যবহারকারীদের জন্য সুপারিশকৃত)**

ডেভ কন্টেইনারে স্প্রিং বুট ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত, যা সমস্ত স্প্রিং বুট অ্যাপ্লিকেশনগুলি পরিচালনার জন্য একটি ভিজ্যুয়াল ইন্টারফেস প্রদান করে। আপনি এটি VS কোডের বাম পাশে অ্যাকটিভিটি বার থেকে দেখতে পাবেন (স্প্রিং বুট আইকন খুঁজুন)।

স্প্রিং বুট ড্যাশবোর্ড থেকে আপনি করতে পারেন:  
- কর্মক্ষেত্রে উপলব্ধ সমস্ত স্প্রিং বুট অ্যাপ্লিকেশন দেখা  
- এক ক্লিকে অ্যাপ্লিকেশন শুরু/বন্ধ করা  
- রিয়েল-টাইমে অ্যাপ্লিকেশন লগ দেখা  
- অ্যাপ্লিকেশন স্ট্যাটাস মনিটর করা  

শুধুমাত্র "prompt-engineering" এর পাশে প্লে বোতামে ক্লিক করুন এই মডিউল শুরু করার জন্য, অথবা একটি সাথে সব মডিউল শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার করা**

সব ওয়েব অ্যাপ্লিকেশন (মডিউল ০১-০৪) চালু করুন:

**বাশ:**  
```bash
cd ..  # মূল ডিরেক্টরি থেকে
./start-all.sh
```
  
**পাওয়ারশেল:**  
```powershell
cd ..  # মূল ডিরেক্টরি থেকে
.\start-all.ps1
```
  
অথবা শুধুমাত্র এই মডিউলটি শুরু করুন:

**বাশ:**  
```bash
cd 02-prompt-engineering
./start.sh
```
  
**পাওয়ারশেল:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে মূল `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করে এবং জার ফাইলগুলি না থাকলে তৈরি করবে।

> **বিঃদ্রঃ:** আপনি যদি সব মডিউলগুলি ম্যানুয়ালি তৈরি করতে পছন্দ করেন শুরু করার আগে:  
>  
> **বাশ:**  
> > ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
>  
> **পাওয়ারশেল:**  
> > ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
আপনার ব্রাউজারে http://localhost:8083 খুলুন।

**বন্ধ করার জন্য:**

**বাশ:**  
```bash
./stop.sh  # শুধুমাত্র এই মডিউল
# অথবা
cd .. && ./stop-all.sh  # সব মডিউলগুলি
```
  
**পাওয়ারশেল:**  
```powershell
.\stop.ps1  # এই মডিউল শুধুমাত্র
# অথবা
cd ..; .\stop-all.ps1  # সব মডিউলসমূহ
```
  
## অ্যাপ্লিকেশন স্ক্রিনশট

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*মেইন ড্যাশবোর্ড যা ৮টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন তাদের বৈশিষ্ট্য এবং ব্যবহার ক্ষেত্রে দেখাচ্ছে*

## প্যাটার্নগুলি অন্বেষণ করা

ওয়েব ইন্টারফেস আপনাকে বিভিন্ন প্রম্পটিং কৌশল পরীক্ষা করার সুযোগ দেয়। প্রতিটি প্যাটার্ন বিভিন্ন সমস্যা সমাধান করে - এগুলো চেষ্টা করুন কখন কোন পদ্ধতি সেরা কাজ করে তা দেখতে।

> **বিঃদ্রঃ: স্ট্রিমিং বনাম নন-স্ট্রিমিং** — প্রতিটি প্যাটার্ন পৃষ্ঠায় দুইটি বোতাম থাকে: **🔴 Stream Response (Live)** এবং একটি **নন-স্ট্রিমিং** অপশন। স্ট্রিমিং সার্ভার-সেন্ট ইভেন্ট (SSE) ব্যবহার করে মডেল টোকেন তৈরি করার সাথে সাথে তা দেখায়, তাই আপনি প্রগতি অবিলম্বে দেখতে পান। নন-স্ট্রিমিং অপশন সম্পূর্ণ উত্তর পাওয়ার জন্য অপেক্ষা করে তারপর দেখায়। গভীর বিশ্লেষণ সৃষ্টিকারী প্রম্পটগুলির জন্য (যেমন, হাই ইগারনেস, সেলফ-রিফ্লেক্টিং কোড) নন-স্ট্রিমিং কল অনেক সময় নেয় — কখনও কখনও মিনিটের পর মিনিট — কোন দৃশ্যমান ফিডব্যাক ছাড়াই। **জটিল প্রম্পট পরীক্ষার সময় স্ট্রিমিং ব্যবহার করুন** যাতে আপনি মডেল কাজ করছে দেখতে পারেন এবং মনে না হয় অনুরোধটি টাইমআউট হয়েছে।  
>  
> **বিঃদ্রঃ: ব্রাউজার প্রয়োজনীয়তা** — স্ট্রিমিং ফিচার Fetch Streams API (`response.body.getReader()`) ব্যবহার করে যা সম্পূর্ণ ব্রাউজার (ক্রোম, এজ, ফায়ারফক্স, সাফারি) প্রয়োজন। VS কোডের বিল্ট-ইন সিম্পল ব্রাউজারে এটি কাজ করে না, কারণ এর ওয়েবভিউ ReadableStream API সমর্থন করে না। যদি আপনি সিম্পল ব্রাউজার ব্যবহার করেন, নন-স্ট্রিমিং বোতামগুলো স্বাভাবিকভাবেই কাজ করবে — কেবল স্ট্রিমিং বোতামগুলো প্রভাবিত হবে। পূর্ণ অভিজ্ঞতার জন্য `http://localhost:8083` একটি বাহ্যিক ব্রাউজারে খুলুন।

### লো বনাম হাই ইগারনেস

লো ইগারনেস ব্যবহার করে সহজ প্রশ্ন করুন, যেমন "২০০-র ১৫% কত?" আপনি দ্রুত এবং সরাসরি উত্তর পাবেন। এখন জটিল কিছু জিজ্ঞাসা করুন, যেমন "হাই ট্রাফিক API-র জন্য ক্যাশিং স্ট্র্যাটেজি ডিজাইন করুন" হাই ইগারনেস সহ। **🔴 Stream Response (Live)** ক্লিক করুন এবং মডেলের বিশদ যুক্তি টোকেন-বাই-টোকেন দেখতে থাকুন। একই মডেল, একই প্রশ্ন গঠন - তবে প্রম্পট বলে দেয় কতটা চিন্তা করতে হবে।

### টাস্ক এক্সিকিউশন (টুল প্রিম্বলস)

বহু-ধাপের ওয়ার্কফ্লো গুলো পূর্ব পরিকল্পনা ও অগ্রগতি বর্ণনার মাধ্যমে লাভবান হয়। মডেল বলে দেয় কী করবে, প্রতিটি ধাপ বর্ণনা করে, তারপর ফলাফল সংক্ষেপ করে।

### সেলফ-রিফ্লেক্টিং কোড

"একটি ইমেইল ভ্যালিডেশন সার্ভিস তৈরি করুন" চেষ্টা করুন। শুধুমাত্র কোড জেনারেট করে থেমে না থেকে, মডেল তৈরি করে, গুণগত মানের বিরুদ্ধে মূল্যায়ন করে, দুর্বলতা চিহ্নিত করে এবং উন্নত করে। আপনি দেখবেন এটি পুনরাবৃত্তি করে যতক্ষণ না কোড প্রোডাকশন মান পূরণ করে।

### স্ট্রাকচার্ড অ্যানালিসিস

কোড রিভিউয়ের জন্য নির্ভরযোগ্য মূল্যায়ন ফ্রেমওয়ার্ক প্রয়োজন। মডেল নির্দিষ্ট বিভাগ (সঠিকতা, প্র্যাকটিস, কর্মক্ষমতা, নিরাপত্তা) ব্যবহার করে এবং গুরুত্ব স্তর নির্ধারণ করে কোড বিশ্লেষণ করে।

### মাল্টি-টার্ন চ্যাট

"স্প্রিং বুট কী?" জিজ্ঞাসা করুন এবং সঙ্গে সঙ্গেই "আমাকে একটি উদাহরণ দেখাও" বলুন। মডেল আপনার প্রথম প্রশ্ন মনে রাখে এবং স্প্রিং বুট উদাহরণ দেয়। স্মৃতি না থাকলে দ্বিতীয় প্রশ্নটি খুব অস্পষ্ট হত।

### ধাপ-বাই-ধাপ যুক্তি

একটি গণিত সমস্যা বাছুন এবং এটিকে ধাপ-বাই-ধাপ যুক্তি ও লো ইগারনেস উভয় দিয়ে চেষ্টা করুন। লো ইগারনেস কেবল দ্রুত উত্তর দেয় - কিন্তু অস্পষ্ট। ধাপ-বাই-ধাপ প্রতিটি হিসাব ও সিদ্ধান্ত প্রদর্শন করে।

### সীমাবদ্ধ আউটপুট

যখন নির্দিষ্ট ফরম্যাট বা শব্দ সংখ্যা প্রয়োজন, এই প্যাটার্ন কঠোরভাবে পালন নিশ্চিত করে। ১০০টি শব্দের একটি সারসংক্ষেপ পয়েন্ট ফরম্যাটে তৈরি করার চেষ্টা করুন।

## আপনি আসলে কী শিখছেন

**যুক্তি প্রচেষ্টা সবকিছু বদলে দেয়**

GPT-5.2 আপনাকে আপনার প্রম্পটের মাধ্যমে গণনামূলক প্রচেষ্টা নিয়ন্ত্রণ করতে দেয়। কম প্রচেষ্টা মানে দ্রুত প্রতিক্রিয়া এবং সীমিত অনুসন্ধান। বেশি প্রচেষ্টা মানে মডেল গভীরভাবে চিন্তা করতে সময় নেয়। আপনি শিখছেন কাজের জটিলতার সাথে প্রচেষ্টা মিলিয়ে চালাতে - সহজ প্রশ্নে সময় নষ্ট করবেন না, কিন্তু জটিল সিদ্ধান্ত তাড়াহুড়ো করবেন না।

**গঠন আচরণ নিয়ন্ত্রণ করে**

প্রম্পটের XML ট্যাগগুলি লক্ষ্য করেছেন? এগুলো সাজানোর জন্য নয়। মডেল গঠিত নির্দেশাবলী স্বাধীন টেক্সটের তুলনায় বেশি নির্ভরযোগ্যভাবে অনুসরণ করে। যখন বহু ধাপের প্রক্রিয়া বা জটিল লজিক প্রয়োজন, গঠন মডেলকে বুঝতে সাহায্য করে সেটা কোথায় আছে এবং পরবর্তী কী।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*একটি ভাল গঠিত প্রম্পটের গঠন যার স্পষ্ট বিভাগ এবং XML-শৈলী সংগঠন*

**গুণগত মান স্ব-মূল্যায়নের মাধ্যমে**

সেলফ-রিফ্লেক্টিং প্যাটার্নগুলো গুণগত মান স্পষ্ট করে তোলে। মডেল "সঠিক" কাজ করবে আশা করার পরিবর্তে আপনি তাকে বলেন ঠিক কী মানে "সঠিক": সঠিক যুক্তি, ত্রুটি হ্যান্ডলিং, কর্মক্ষমতা, নিরাপত্তা। মডেল তার নিজস্ব আউটপুট মূল্যায়ন করতে পারে এবং উন্নত করতে পারে। এটি কোড জেনারেশনকে লটারির পরিবর্তে একটি প্রক্রিয়ায় পরিণত করে।

**প্রসঙ্গ সীমিত**

মাল্টি-টার্ন কথোপকথন কাজ করে প্রতিটি অনুরোধের সঙ্গে মেসেজ ইতিহাস অন্তর্ভুক্ত করে। কিন্তু এর একটি সীমা আছে - প্রতিটি মডেলের সর্বোচ্চ টোকেন সংখ্যা থাকে। কথোপকথন যত বাড়বে, আপনাকে এমন কৌশল নিতে হবে যাতে প্রাসঙ্গিক প্রসঙ্গ রাখে কিন্তু সীমা ছাড়িয়ে না যায়। এই মডিউলটি আপনাকে স্মৃতির কাজ শেখায়; পরে আপনি জানবেন কখন সারাংশ তৈরি করতে হবে, কখন ভুলে যেতে হবে, এবং কখন পুনরুদ্ধার করতে হবে।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**নেভিগেশন:** [← আগের: মডিউল ০১ - পরিচিতি](../01-introduction/README.md) | [মেইন পেজে ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০৩ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকারোক্তি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা সঠিকতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথি যে ভাষায় লেখা হয়েছে তা কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহার থেকে সৃষ্ট কোনো ভুলবোঝা বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->