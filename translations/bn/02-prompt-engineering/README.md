# Module 02: GPT-5.2 দিয়ে প্রম্পট ইঞ্জিনিয়ারিং

## বিষয়ের তালিকা

- [আপনি কি শেখাবেন](../../../02-prompt-engineering)
- [প্রয়োজনীয়তাসমূহ](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়সমূহ](../../../02-prompt-engineering)
  - [জিরো-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [ফিউ-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [চেইন অব থট](../../../02-prompt-engineering)
  - [রোল-ভিত্তিক প্রম্পটিং](../../../02-prompt-engineering)
  - [প্রম্পট টেমপ্লেটস](../../../02-prompt-engineering)
- [উন্নত প্যাটার্নসমূহ](../../../02-prompt-engineering)
- [বিদ্যমান Azure রিসোর্স ব্যবহারের উপায়](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রিনশট](../../../02-prompt-engineering)
- [প্যাটার্ন সমূহ অন্বেষণ](../../../02-prompt-engineering)
  - [কম বনাম উচ্চ উৎসাহ](../../../02-prompt-engineering)
  - [টাস্ক এক্সিকিউশন (টুল প্রিম্বলস)](../../../02-prompt-engineering)
  - [স্ব-প্রতিফলন কোড](../../../02-prompt-engineering)
  - [গঠিত বিশ্লেষণ](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [ধাপে ধাপে যুক্তি](../../../02-prompt-engineering)
  - [সীমাবদ্ধ আউটপুট](../../../02-prompt-engineering)
- [আপনি সত্যিই কী শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী ধাপসমূহ](../../../02-prompt-engineering)

## আপনি কি শেখাবেন

<img src="../../../translated_images/bn/what-youll-learn.c68269ac048503b2.webp" alt="আপনি কি শেখাবেন" width="800"/>

পূর্ববর্তী মডিউলে, আপনি দেখেছিলেন কীভাবে মেমোরি কথোপকথনমূলক AI কে সক্ষম করে এবং GitHub মডেল ব্যবহার করে মৌলিক ইন্টারঅ্যাকশনগুলি করা যায়। এখন আমরা ফোকাস করব কীভাবে আপনি প্রশ্ন জিজ্ঞাসা করবেন — অর্থাৎ প্রম্পটগুলি — Azure OpenAI এর GPT-5.2 ব্যবহার করে। আপনি যেভাবে প্রম্পট গঠন করবেন তা জবাবের গুণমানকে নাটকীয়ভাবে প্রভাবিত করে। আমরা শুরু করব প্রাথমিক প্রম্পটিং কৌশলগুলোর পুনঃপর্যালোচনা দিয়ে, তারপর এগিয়ে যাব আটটি উন্নত প্যাটার্নে যা GPT-5.2 এর ক্ষমতাগুলো সম্পূর্ণরূপে কাজে লাগায়।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তি নিয়ন্ত্রণ নিয়ে এসেছে - আপনি মডেলকে বলতে পারেন কতটা চিন্তা করতে হবে উত্তর দেওয়ার আগে। এটি বিভিন্ন প্রম্পটিং কৌশলগুলিকে আরও স্পষ্ট করে তোলে এবং আপনাকে বুঝতে সাহায্য করে কখন প্রতিটি পদ্ধতি ব্যবহার করবেন। আমরা Azure এর GPT-5.2 এর জন্য কম রেট লিমিট থেকেও লাভবান হবো, যা GitHub মডেলগুলোর তুলনায় বেশি সুবিধাজনক।

## প্রয়োজনীয়তাসমূহ

- সম্পন্ন করা হয়েছে মডিউল ০১ (Azure OpenAI রিসোর্স প্রয়োগ করা হয়েছে)  
- রুট ডিরেক্টরিতে `.env` ফাইল রয়েছে Azure ক্রেডেনশিয়ালস সহ (মডিউল ০১ এ `azd up` দ্বারা তৈরি)

> **নোট:** আপনি যদি মডিউল ০১ সম্পন্ন না করে থাকেন, প্রথমে সেখানে নির্দেশিত ডিপ্লয়মেন্ট পদক্ষেপ অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

<img src="../../../translated_images/bn/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="প্রম্পট ইঞ্জিনিয়ারিং কী?" width="800"/>

প্রম্পট ইঞ্জিনিয়ারিং হলো এমন ইনপুট টেক্সট ডিজাইন করা যা ধারাবাহিকভাবে আপনি যে ফলাফল চান সেগুলো পেতে সাহায্য করে। এটা শুধুমাত্র প্রশ্ন করা নয় — এটি এমনভাবে অনুরোধ গঠন করা যাতে মডেল ঠিক বুঝতে পারে আপনি কী চান এবং কীভাবে তা প্রদান করতে হবে।

এটাকে ভাবুন যেন আপনি একটি সহকর্মীকে নির্দেশ দিচ্ছেন। "বাগ ঠিক করো" হলো অস্পষ্ট। "UserService.java এর লাইন ৪৫ এ নল পয়েন্টার এক্সসেপশন ঠিক করো একটি নল চেক যোগ করে" হলো নির্দিষ্ট। ভাষা মডেলগুলোর ক্ষেত্রেও একই নিয়ম প্রযোজ্য - স্পষ্টতা এবং গঠন গুরুত্বপূর্ণ।

<img src="../../../translated_images/bn/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j কিভাবে ফিট করে" width="800"/>

LangChain4j প্রদান করে অবকাঠামো — মডেল সংযোগ, মেমরি, এবং মেসেজ টাইপগুলি — যেখানে প্রম্পট প্যাটার্ন হলো যত্নের সাথে গঠিত লেখা যা ওই অবকাঠামোর মাধ্যমে পাঠানো হয়। মূল নির্মাণ ব্লক হচ্ছে `SystemMessage` (যা AI এর আচরণ ও ভূমিকা নির্ধারণ করে) এবং `UserMessage` (যা আপনার প্রকৃত অনুরোধ বহন করে)।

## প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়সমূহ

<img src="../../../translated_images/bn/five-patterns-overview.160f35045ffd2a94.webp" alt="প্রম্পট ইঞ্জিনিয়ারিং পাঁচটি প্যাটার্নের ওভারভিউ" width="800"/>

এই মডিউলের উন্নত প্যাটার্নগুলোর আগে, আসুন পাঁচটি মৌলিক প্রম্পটিং কৌশল রিভিউ করি। এগুলোই হল সেই ভিত্তি যেগুলো প্রতিটি প্রম্পট ইঞ্জিনিয়ারকে জানা প্রয়োজন। আপনি যদি ইতোমধ্যে [Quick Start মডিউল](../00-quick-start/README.md#2-prompt-patterns) সম্পন্ন করে থাকেন, তাহলে এগুলো কার্যকর ভাবেই দেখেছেন — এখানে তাদের ধারণাগত কাঠামো তুলে ধরা হলো।

### জিরো-শট প্রম্পটিং

সবচেয়ে সহজ পদ্ধতি: মডেলকে সরাসরি নির্দেশনা দিন কোনো উদাহরণ ছাড়াই। মডেল তার প্রশিক্ষণ সম্পূর্ণরূপে নির্ভর করে কাজটি বোঝার জন্য। এটি সাধারণ অনুরোধের জন্য কার্যকর, যেখানে প্রত্যাশিত আচরণ স্পষ্ট।

<img src="../../../translated_images/bn/zero-shot-prompting.7abc24228be84e6c.webp" alt="জিরো-শট প্রম্পটিং" width="800"/>

*উদাহরণ ছাড়া সরাসরি নির্দেশনা - মডেল কেবল নির্দেশনা থেকে কাজ অনুমান করে*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// উত্তর: "ইতিবাচক"
```
  
**কখন ব্যবহার করবেন:** সরল শ্রেণীবিন্যাস, সরাসরি প্রশ্ন, অনুবাদ, বা যেকোনো কাজ যা অতিরিক্ত নির্দেশনা ছাড়াই মডেল করতে পারে।

### ফিউ-শট প্রম্পটিং

মডেলকে এমন উদাহরণ দিন যা তিনি অনুসরণ করার নিদর্শন দেখায়। মডেল আপনার উদাহরণ থেকে প্রত্যাশিত ইনপুট-আউটপুট ফরম্যাট শিখে নতুন ইনপুটে প্রয়োগ করে। এটি এমন কাজের জন্য ধারাবাহিকতা অনেক বাড়িয়ে তোলে যেখানে কাঙ্খিত ফরম্যাট বা আচরণ স্পষ্ট নয়।

<img src="../../../translated_images/bn/few-shot-prompting.9d9eace1da88989a.webp" alt="ফিউ-শট প্রম্পটিং" width="800"/>

*উদাহরণ থেকে শেখা — মডেল নিদর্শন চিনে নিয়ে নতুন ইনপুটে প্রয়োগ করে*

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
  
**কখন ব্যবহার করবেন:** কাস্টম শ্রেণীবিন্যাস, ধারাবাহিক ফরম্যাটিং, ডোমেইন-নির্দিষ্ট কাজ, অথবা যখন জিরো-শট ফলাফল অনিশ্চিত হয়।

### চেইন অব থট

মডেলকে ধাপে ধাপে তার যুক্তি প্রদর্শন করতে বলুন। সরাসরি উত্তর দেওয়ার বদলে, মডেল সমস্যা ভেঙে প্রতিটি অংশ বিস্তারিতভাবে কাজ করে। এটি গণিত, লজিক, এবং বহু ধাপ যুক্তি কাজের সঠিকতা বাড়ায়।

<img src="../../../translated_images/bn/chain-of-thought.5cff6630e2657e2a.webp" alt="চেইন অব থট প্রম্পটিং" width="800"/>

*ধাপে ধাপে যুক্তি — জটিল সমস্যাকে স্পষ্ট যৌক্তিক ধাপে ভেঙে ফেলা*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// মডেল দেখায়: ১৫ - ৮ = ৭, তারপর ৭ + ১২ = ১৯ টি আপেল
```
  
**কখন ব্যবহার করবেন:** গণিত সমস্যা, লজিক পাজল, ডিবাগিং, অথবা যেকোনো কাজ যেখানে যুক্তি প্রক্রিয়া দেখানো সঠিকতা এবং বিশ্বাসযোগ্যতা বাড়ায়।

### রোল-ভিত্তিক প্রম্পটিং

প্রশ্ন করার আগে AI এর জন্য একটি ভুমিকা বা পাত্র সৃষ্ট করুন। এতে এমন প্রসঙ্গ আসে যা উত্তরটির স্বর, গভীরতা, এবং ফোকাস নির্ধারণ করে। "সফটওয়্যার আর্কিটেক্ট" একজন "জুনিয়র ডেভেলপার" বা "সিকিউরিটি অডিটর" থেকে আলাদা পরামর্শ দেয়।

<img src="../../../translated_images/bn/role-based-prompting.a806e1a73de6e3a4.webp" alt="রোল-ভিত্তিক প্রম্পটিং" width="800"/>

*প্রসঙ্গ এবং পাত্র নির্ধারণ — একই প্রশ্ন আলাদা আলাদা ভূমিকা অনুযায়ী ভিন্ন উত্তর পায়*

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
  
**কখন ব্যবহার করবেন:** কোড রিভিউ, শিক্ষাদান, ডোমেইন-নির্দিষ্ট বিশ্লেষণ, বা যখন বিশেষ দক্ষতা বা দৃষ্টিভঙ্গি অনুযায়ী উত্তর প্রয়োজন।

### প্রম্পট টেমপ্লেটস

পুনর্ব্যবহারযোগ্য প্রম্পট তৈরি করুন ভেরিয়েবল প্লেসহোল্ডার সহ। প্রতি বার নতুন প্রম্পট লেখার বদলে, একবার একটি টেমপ্লেট সংজ্ঞায়িত করুন এবং বিভিন্ন মান পূরণ করুন। LangChain4j এর `PromptTemplate` ক্লাসটি এটি `{{variable}}` সিনট্যাক্স দিয়ে সহজ করে তোলে।

<img src="../../../translated_images/bn/prompt-templates.14bfc37d45f1a933.webp" alt="প্রম্পট টেমপ্লেটস" width="800"/>

*পুনর্ব্যবহারযোগ্য প্রম্পটস ভেরিয়েবল প্লেসহোল্ডার দিয়ে — এক টেমপ্লেট, অনেক ব্যবহার*

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
  
**কখন ব্যবহার করবেন:** বিভিন্ন ইনপুট সহ পুনরাবৃত্ত প্রশ্ন, ব্যাচ প্রক্রিয়াকরণ, পুনর্ব্যবহারযোগ্য AI ওয়ার্কফ্লো নির্মাণ, বা যেকোনো পরিস্থিতিতে যেখানে প্রম্পট কাঠামো একই থাকে কিন্তু ডেটা পরিবর্তিত হয়।

---

এই পাঁচটি মৌলিক বিষয় আপনাকে অধিকাংশ প্রম্পট কাজের জন্য একটি শক্ত ভিত দেয়। মডিউলের বাকিটা এগুলোকে ভিত্তি করে তৈরি করা হয়েছে **আটটি উন্নত প্যাটার্নের** মাধ্যমে যা GPT-5.2 এর যুক্তি নিয়ন্ত্রণ, স্ব-মূল্যায়ন, এবং গঠিত আউটপুট ক্ষমতা ব্যবহার করে।

## উন্নত প্যাটার্নসমূহ

মৌলিক বিষয়গুলো কভার করার পর, চলুন এগিয়ে যাই আটটি উন্নত প্যাটার্নের দিকে যা এই মডিউলকে অনন্য করে তোলে। সব সমস্যার জন্য একই পদ্ধতি প্রয়োজন হয় না। কিছু প্রশ্নের জন্য দ্রুত উত্তর দরকার, অন্যগুলোতে গভীর ভাবনার প্রয়োজন। কিছু সুস্পষ্ট যুক্তি দেখতে চায়, অন্যগুলো কেবল ফলাফল চায়। নিচের প্রতিটি প্যাটার্ন ভিন্ন পরিস্থিতির জন্য সর্বোত্তম — এবং GPT-5.2 এর যুক্তি নিয়ন্ত্রণের মাধ্যমে পার্থক্যগুলো আরও প্রকাশ পায়।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন" width="800"/>

*আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন ও তাদের ব্যবহার ক্ষেত্রের ওভারভিউ*

<img src="../../../translated_images/bn/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 সহ যুক্তি নিয়ন্ত্রণ" width="800"/>

*GPT-5.2 এর যুক্তি নিয়ন্ত্রণ আপনাকে বলে দেয় মডেল কতটা চিন্তা করবে — দ্রুত সরাসরি উত্তর থেকে গভীর অনুসন্ধান পর্যন্ত*

<img src="../../../translated_images/bn/reasoning-effort.db4a3ba5b8e392c1.webp" alt="যুক্তি প্রচেষ্টার তুলনা" width="800"/>

*কম উৎসাহ (দ্রুত, সরাসরি) বনাম উচ্চ উৎসাহ (গভীর, অনুসন্ধানমূলক) যুক্তি পদ্ধতি*

**কম উৎসাহ (দ্রুত ও মনোযোগী)** - সহজ প্রশ্নের জন্য যেখানে দ্রুত, সরাসরি উত্তর প্রয়োজন। মডেল সর্বাধিক ২ ধাপ যুক্তি করে। গণনা, সন্ধান, বা সরল প্রশ্নের জন্য ব্যবহার করুন।

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
  
> 💡 **GitHub Copilot দিয়ে অন্বেষণ করুন:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) খুলে জিজ্ঞাসা করুন:  
> - "কম উৎসাহ ও উচ্চ উৎসাহ প্রম্পটিং প্যাটার্নের মধ্যে পার্থক্য কী?"  
> - "প্রম্পটের XML ট্যাগগুলো কিভাবে AI এর উত্তর গঠন করতে সাহায্য করে?"  
> - "কখন স্ব-প্রতিফলন প্যাটার্ন ব্যবহার করবেন, কখন সরাসরি নির্দেশনা?"

**উচ্চ উৎসাহ (গভীর ও বিস্তারিত)** - জটিল সমস্যার জন্য যেখানে পূর্ণাঙ্গ বিশ্লেষণ প্রয়োজন। মডেল সম্পূর্ণরূপে অনুসন্ধান করে এবং বিস্তারিত যুক্তি প্রদর্শন করে। সিস্টেম ডিজাইন, আর্কিটেকচার সিদ্ধান্ত, বা জটিল গবেষণার জন্য ব্যবহার করুন।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**টাস্ক এক্সিকিউশন (ধাপে ধাপে অগ্রগতি)** - বহু ধাপের কাজের জন্য। মডেল একটি পরিকল্পনা দেয়, প্রতিটি পদক্ষেপ কাজ করার সময় বর্ণনা করে, তারপর সারাংশ প্রদান করে। মাইগ্রেশন, ইমপ্লিমেন্টেশন, বা কোনো বহু ধাপের প্রক্রিয়ার জন্য ব্যবহার করুন।

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
  
চেইন-অফ-থট প্রম্পটিং মডেলকে তার যুক্তি প্রক্রিয়া প্রদর্শন করতে বলে, যা জটিল কাজের সঠিকতা বাড়ায়। ধাপে ধাপে ভাঙন মানুষ ও AI উভয়েরই যুক্তি বোঝার জন্য সহায়ক।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** এই প্যাটার্ন সম্পর্কে প্রশ্ন করুন:  
> - "দীর্ঘ সময় গ্রহণকারী অপারেশনগুলির জন্য টাস্ক এক্সিকিউশন প্যাটার্ন কীভাবে মানিয়ে নেব?"  
> - "প্রোডাকশনে টুল প্রিম্বল গঠন করার সেরা পদ্ধতি কী?"  
> - "UI তে মধ্যবর্তী অগ্রগতি আপডেট কিভাবে ক্যাপচার ও প্রদর্শন করব?"

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="টাস্ক এক্সিকিউশন প্যাটার্ন" width="800"/>

*পরিকল্পনা → সম্পাদন → সারাংশ ধাপে ধাপে কর্মপ্রবাহ*

**স্ব-প্রতিফলন কোড** - উৎপাদন মানের কোড তৈরির জন্য। মডেল উৎপাদন মান অনুসারে কোড তৈরি করে উপযুক্ত ত্রুটি সমাধান সহ। নতুন ফিচার বা সেবা নির্মাণের সময় ব্যবহার করুন।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="স্ব-প্রতিফলন চক্র" width="800"/>

*ক্রমাগত উন্নতির লুপ - তৈরি, মূল্যায়ন, সমস্যা সনাক্তকরণ, উন্নয়ন, পুনরাবৃত্তি*

**গঠিত বিশ্লেষণ** - ধারাবাহিক মূল্যায়নের জন্য। মডেল একটি নির্দিষ্ট কাঠামো অনুসারে কোড পর্যালোচনা করে (সঠিকতা, অনুশীলন, কর্মক্ষমতা, সুরক্ষা, রক্ষণাবেক্ষণযোগ্যতা)। কোড রিভিউ বা গুণগত মূল্যায়নের জন্য ব্যবহার করুন।

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
  
> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** গঠিত বিশ্লেষণ সম্পর্কে প্রশ্ন করুন:  
> - "বিভিন্ন ধরনের কোড রিভিউয়ের জন্য বিশ্লেষণ কাঠামো কিভাবে কাস্টমাইজ করবো?"  
> - "গঠিত আউটপুট প্রোগ্রাম্যাটিক্যালি পার্স ও ব্যবহার করার সেরা উপায় কী?"  
> - "বিভিন্ন রিভিউ সেশনে ধারাবাহিক গুরুত্ব স্তর নিশ্চিত করতে কী করণীয়?"

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="গঠিত বিশ্লেষণ প্যাটার্ন" width="800"/>

*গুরুত্ব স্তরের সঙ্গে ধারাবাহিক কোড রিভিউয়ের কাঠামো*

**মাল্টি-টার্ন চ্যাট** - প্রসঙ্গ প্রয়োজন এমন কথোপকথনের জন্য। মডেল পূর্বের বার্তাগুলো মনে রাখে এবং তাদের উপর ভিত্তি করে উত্তর দেয়। ইন্টার‌্যাকটিভ হেল্প সেশন বা জটিল প্রশ্নোত্তরের জন্য ব্যবহার করুন।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/bn/context-memory.dff30ad9fa78832a.webp" alt="প্রসঙ্গ মেমোরি" width="800"/>

*বহু বার্তায় কথোপকথনের প্রসঙ্গ কিভাবে সঞ্চিত হয় যতদূর টোকেন সীমা পৌঁছায়*

**ধাপে ধাপে যুক্তি** - দৃশ্যমান যৌক্তিকতা দরকার এমন সমস্যার জন্য। মডেল প্রতিটি ধাপে স্পষ্ট যুক্তি দেখায়। গণিত সমস্যা, লজিক পাজল, বা চিন্তার প্রক্রিয়া বোঝার জন্য ব্যবহার করুন।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/bn/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="ধাপে ধাপে প্যাটার্ন" width="800"/>

*সমস্যাগুলোকে স্পষ্ট যৌক্তিক ধাপে ভেঙে ফেলা*

**সীমাবদ্ধ আউটপুট** - নির্দিষ্ট ফরম্যাটের নিয়ম প্রয়োজনে। মডেল কঠোরভাবে ফরম্যাট এবং দৈর্ঘ্যের নিয়ম অনুসরণ করে। সারাংশ বা সঠিক আউটপুট গঠন দরকার এমন পরিস্থিতির জন্য ব্যবহার করুন।

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
  
<img src="../../../translated_images/bn/constrained-output-pattern.0ce39a682a6795c2.webp" alt="সীমাবদ্ধ আউটপুট প্যাটার্ন" width="800"/>

*নির্দিষ্ট ফরম্যাট, দৈর্ঘ্য, এবং গঠনের নিয়ম শর্ত*

## বিদ্যমান Azure রিসোর্স ব্যবহারের উপায়

**ডিপ্লয়মেন্ট যাচাই করুন:**

রুট ডিরেক্টরিতে `.env` ফাইল আছে কিনা নিশ্চিত করুন Azure ক্রেডেনশিয়ালস সহ (মডিউল ০১ এর সময় তৈরি হয়েছে):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```
  
**অ্যাপ্লিকেশন শুরু করুন:**

> **নোট:** আপনি যদি ইতিমধ্যেই সব অ্যাপ্লিকেশন `./start-all.sh` ব্যবহার করে মডিউল ০১ থেকে চালু করে থাকেন, তাহলে এই মডিউল ইতোমধ্যেই পোর্ট ৮০৮৩ এ চলছে। আপনি নিচের স্টার্ট কমান্ডগুলো বাদ দিয়ে সরাসরি http://localhost:8083 এ যেতে পারেন।

**অপশন ১: স্প্রিং বুট ড্যাশবোর্ড ব্যবহার (VS কোড ব্যবহারকারীদের জন্য সুপারিশকৃত)**

ডেভ কন্টেইনারে স্প্রিং বুট ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত রয়েছে, যা সব স্প্রিং বুট অ্যাপ্লিকেশন ব্যবস্থাপনার জন্য ভিজ্যুয়াল ইন্টারফেস প্রদান করে। এটি VS কোডের Activity Bar-এ বাম পাশে পাওয়া যাবে (স্প্রিং বুট আইকন দেখুন)।
Spring Boot ড্যাশবোর্ড থেকে, আপনি পারেন:
- ওয়ার্কস্পেসে উপলব্ধ সব Spring Boot অ্যাপ্লিকেশন দেখতে
- এক ক্লিকে অ্যাপ্লিকেশন শুরু/বন্দ করতে
- অ্যাপ্লিকেশন লগ রিয়েল-টাইমে দেখতে
- অ্যাপ্লিকেশন স্ট্যাটাস মনিটর করতে

শুধু "prompt-engineering" এর পাশে প্লে বোতামে ক্লিক করলেই এই মডিউল শুরু হবে, অথবা সব মডিউল একসাথে শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার করে**

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

অথবা শুধু এই মডিউল শুরু করুন:

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

উভয় স্ক্রিপ্ট অটোম্যাটিক্যালি মূল `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করে এবং যদি JAR না থাকে তাহলে জার বিল্ড করবে।

> **দ্রষ্টব্য:** যদি আপনি মডিউলগুলো শুরু করার আগে নিজে হাতে সব মডিউল বিল্ড করতে চান:
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

**থামাতে:**

**Bash:**
```bash
./stop.sh  # শুধুমাত্র এই মডিউল
# অথবা
cd .. && ./stop-all.sh  # সমস্ত মডিউলগুলি
```

**PowerShell:**
```powershell
.\stop.ps1  # এই মডিউল শুধুমাত্র
# অথবা
cd ..; .\stop-all.ps1  # সব মডিউলগুলি
```

## অ্যাপ্লিকেশন স্ক্রিনশট

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*প্রধান ড্যাশবোর্ড যেখানে সব ৮টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন তাদের বৈশিষ্ট্য ও ব্যবহার ক্ষেত্রসহ প্রদর্শিত হচ্ছে*

## প্যাটার্নগুলি অন্বেষণ

ওয়েব ইন্টারফেস আপনাকে বিভিন্ন প্রম্পটিং কৌশল নিয়ে পরীক্ষা-নিরীক্ষা করতে দেয়। প্রতিটি প্যাটার্ন আলাদা সমস্যা সমাধান করে – চেষ্টা করুন বুঝতে কখন কোন পদ্ধতি সবচেয়ে কার্যকর।

### কম বনাম উচ্চ উৎসাহ

কম উৎসাহ ব্যবহার করে সহজ প্রশ্ন করুন যেমন "২০০ এর ১৫% কত?" আপনি পাবেন দ্রুত, সরাসরি উত্তর। এখন উচ্চ উৎসাহ ব্যবহার করে কোনো জটিল প্রশ্ন করুন যেমন "একটি উচ্চ-ট্রাফিক API এর জন্য ক্যাশিং কৌশল ডিজাইন করুন"। দেখবেন মডেল ধীরে ধীরে বিশদ বিশ্লেষণসহ উত্তর দেয়। একই মডেল, একই প্রশ্নের কাঠামো – কিন্তু প্রম্পট বলে দেয় কতটা চিন্তা করতে হবে।

<img src="../../../translated_images/bn/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*সর্বনিম্ন বিশ্লেষণ সহ দ্রুত হিসাব*

<img src="../../../translated_images/bn/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*সম্পূর্ণ ক্যাশিং কৌশল (২.৮MB)*

### টাস্ক এক্সিকিউশন (টুল প্রিম্যাবলস)

বহু ধাপের ওয়ার্কফ্লো রূপায়ণে আগাম পরিকল্পনা এবং অগ্রগতির বর্ণনা প্রয়োজন। মডেল বলে কী করবে, প্রতিটি ধাপ বর্ণনা করে, শেষে ফলাফল সারাংশ দেয়।

<img src="../../../translated_images/bn/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ধাপে ধাপে REST endpoint তৈরির বর্ণনা (৩.৯MB)*

### আত্ম-প্রতিফলিত কোড

"একটি ইমেইল ভ্যালিডেশন সার্ভিস তৈরি করুন" চেষ্টা করুন। সরাসরি কোড তৈরি বন্ধ না করে, মডেল কোড তৈরি করে, মানদণ্ডের বিরুদ্ধে মূল্যায়ন করে, দুর্বলতা শনাক্ত করে এবং উন্নত করে। দেখবেন কোড ঠিকঠাক প্রোডাকশন মান পূরণ না হওয়া পর্যন্ত এটি অনেকবার পুনরাবৃত্তি করে।

<img src="../../../translated_images/bn/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*সম্পূর্ণ ইমেইল ভ্যালিডেশন সার্ভিস (৫.২MB)*

### কাঠামোবদ্ধ বিশ্লেষণ

কোড রিভিউয়ের জন্য ধারাবাহিক মূল্যায়ন কাঠামো দরকার। মডেল নির্দিষ্ট শ্রেণীবিভাগ (সঠিকতা, চর্চা, কর্মক্ষমতা, সুরক্ষা) ও তীব্রতা অনুযায়ী কোড বিশ্লেষণ করে।

<img src="../../../translated_images/bn/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ফ্রেমওয়ার্ক ভিত্তিক কোড রিভিউ*

### বহু-ব্যাপী চ্যাট

"Spring Boot কি?" জিজ্ঞাসা করুন, তারপর সঙ্গে সঙ্গেই "আমাকে একটি উদাহরণ দেখাও"। মডেল আপনার প্রথম প্রশ্ন মনে রাখে এবং স্পষ্ট Spring Boot উদাহরণ দেয়। যদি স্মৃতি না থাকে, তাহলে দ্বিতীয় প্রশ্ন অস্পষ্ট হয়ে যাবে।

<img src="../../../translated_images/bn/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*প্রশ্নের মাঝে প্রসঙ্গ সংরক্ষণ*

### ধাপে ধাপে যুক্তি

একটি গাণিতিক সমস্যা বেছে নিন এবং একবার ধাপে ধাপে যুক্তি এবং একবার কম উৎসাহ দিয়ে চেষ্টা করুন। কম উৎসাহ আপনাকে দ্রুত উত্তর দেয় – কিন্তু অস্পষ্ট। ধাপে ধাপে যুক্তি সব হিসাব ও সিদ্ধান্ত দেখায়।

<img src="../../../translated_images/bn/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*স্পষ্ট ধাপ সহ গাণিতিক সমস্যা*

### সঙ্খলিত আউটপুট

যখন নির্দিষ্ট ফরম্যাট বা শব্দ সংখ্যা দরকার, প্যাটার্ন কঠোরভাবে তা নিশ্চিত করে। ১০০ শব্দের সঠিক বিন্যাসে সংক্ষিপ্তসার তৈরি করে দেখুন।

<img src="../../../translated_images/bn/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*মেশিন লার্নিং সংক্ষিপ্তসার ফরম্যাট নিয়ন্ত্রণসহ*

## আপনি আসলেই কী শিখছেন

**যুক্তি প্রচেষ্টা সব কিছুকে বদলে দেয়**

GPT-5.2 আপনাকে প্রম্পটের মাধ্যমে গণনামূলক প্রচেষ্টা নিয়ন্ত্রণ করার সুযোগ দেয়। কম প্রচেষ্টা মানে দ্রুত প্রতিক্রিয়া এবং সর্বনিম্ন বিশ্লেষণ। বেশি প্রচেষ্টা মানে মডেল গভীরভাবে চিন্তা করতে সময় নেয়। আপনি শিখছেন কাজের জটিলতার সঙ্গে প্রচেষ্টা মেলাতে – সহজ প্রশ্নে সময় নষ্ট করবেন না, কিন্তু জটিল সিদ্ধান্তও দ্রুত নেবেন না।

**কাঠামো আচরণ নির্দেশ করে**

প্রম্পটে XML ট্যাগ দেখুন? সেগুলো শুধু সাজানোর জন্য নয়। মডেল কাঠামোবদ্ধ নির্দেশ অনুসরণ করে বেশি নির্ভরযোগ্যভাবে। যখন বহু-ধাপ প্রক্রিয়া বা জটিল লজিক দরকার, কাঠামো মডেলকে বলে দেয় এটি কোথায় আছে এবং পরবর্তী কী।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*সুসংগঠিত প্রম্পটের গঠন ও XML-শৈলীর বিভাজন*

**গুণমান আত্ম-মূল্যায়নের মাধ্যমে আসে**

আত্ম-প্রতিফলিত প্যাটার্নগুলি মানদণ্ড স্পষ্ট করে দেয়। মডেলের সঠিক কাজ করার আশা না করে, আপনি বলে দেন কী অর্থে “সঠিক”: সঠিক যুক্তি, ত্রুটি পরিচালনা, কর্মক্ষমতা, সুরক্ষা। মডেল তার নিজের আউটপুট মূল্যায়ন করতে পারে এবং উন্নত করতে পারে। এতে কোড জেনারেশন লটারি নয়, একটি প্রক্রিয়া হয়।

**প্রসঙ্গ সীমিত**

বহু-ব্যাপী আলোচনা মেসেজ ইতিহাস প্রতিটি অনুরোধের সঙ্গে অন্তর্ভুক্ত করে কাজ করে। কিন্তু সীমা আছে – প্রতিটি মডেলের সর্বোচ্চ টোকেন সংখ্যা। কথোপকথন বাড়ার সাথে সাথে প্রাসঙ্গিক প্রসঙ্গ ধরে রাখতে আপনাকে কৌশল নিতে হবে যেন সীমা না ছাড়িয়ে যায়। এই মডিউল আপনাকে স্মৃতির কাজ শেখায়; পরে আপনি শিখবেন কখন সারাংশ করতে হবে, কখন ভুলতে হবে, আর কখন পুনরুদ্ধার করতে হবে।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল ০১ - পরিচিতি](../01-introduction/README.md) | [প্রধান পৃষ্ঠায় ফিরে যান](../README.md) | [পরবর্তী: মডিউল ০৩ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**দ্রষ্টব্য**:  
এই দলিলটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসাধ্য সঠিকতা বজায় রাখার চেষ্টা করি, তবুও স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল দলিলটিকে তার স্বাভাবিক ভাষায় কর্তৃত্ব সূত্র হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->