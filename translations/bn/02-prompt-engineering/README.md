# মডিউল ০২: GPT-5.2 সহ প্রম্পট ইঞ্জিনিয়ারিং

## বিষয়সূচি

- [আপনি কী শিখবেন](../../../02-prompt-engineering)
- [প্রয়োজনীয়তাসমূহ](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়সমূহ](../../../02-prompt-engineering)
  - [জিরো-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [ফিউ-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [চেইন অফ থট](../../../02-prompt-engineering)
  - [রোল-ভিত্তিক প্রম্পটিং](../../../02-prompt-engineering)
  - [প্রম্পট টেমপ্লেটস](../../../02-prompt-engineering)
- [অগ্রণী প্যাটার্নস](../../../02-prompt-engineering)
- [বিদ্যমান Azure রিসোর্স ব্যবহার](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রিনশটস](../../../02-prompt-engineering)
- [প্যাটার্নস অন্বেষণ](../../../02-prompt-engineering)
  - [কম বনাম বেশি উৎসাহ](../../../02-prompt-engineering)
  - [টাস্ক এক্সিকিউশন (টুল প্রাথমিক অংশ)](../../../02-prompt-engineering)
  - [স্ব-পর্যালোচনা কোড](../../../02-prompt-engineering)
  - [গঠিত বিশ্লেষণ](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [ধাপে ধাপে যুক্তি](../../../02-prompt-engineering)
  - [নির্দিষ্ট আউটপুট](../../../02-prompt-engineering)
- [আপনি আসলে কী শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী ধাপসমূহ](../../../02-prompt-engineering)

## আপনি কী শিখবেন

<img src="../../../translated_images/bn/what-youll-learn.c68269ac048503b2.webp" alt="আপনি কী শিখবেন" width="800"/>

গত মডিউলে, আপনি দেখেছেন কীভাবে মেমোরি কথোপকথনমূলক AI সক্ষম করে এবং GitHub মডেলগুলি ব্যবহার করে মৌলিক ইন্টারঅ্যাকশন করেছেন। এখন আমরা ফোকাস করব কীভাবে আপনি প্রশ্ন জিজ্ঞাসা করেন — অর্থাৎ প্রম্পটগুলি — Azure OpenAI এর GPT-5.2 ব্যবহার করে। আপনার প্রম্পট গঠন করার ধরণ ড্রাম্যাটিক্যালি আপনার প্রাপ্ত প্রতিক্রিয়ার গুণমান প্রভাবিত করে। আমরা শুরু করব মৌলিক প্রম্পটিং কৌশলগুলির পর্যালোচনায়, তারপর এগিয়ে যাব আটটি উন্নত প্যাটার্নে যা GPT-5.2 এর সম্পূর্ণ ক্ষমতা কাজে লাগায়।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তি নিয়ন্ত্রণ পরিচয় করায় - আপনি মডেলকে বলতে পারেন কতটা চিন্তা করতে হবে উত্তর দেওয়ার আগে। এটি বিভিন্ন প্রম্পটিং কৌশলগুলো স্পষ্ট করে তোলে এবং বুঝতে সাহায্য করে কখন কোন পদ্ধতি ব্যবহার করবেন। Azure এর GPT-5.2 জন্য GitHub মডেল তুলনায় কম রেট লিমিটও আমরা কাজে লাগাব।

## প্রয়োজনীয়তাসমূহ

- মডিউল ০১ সম্পন্ন (Azure OpenAI রিসোর্স ডিপ্লয় করা হয়েছে)
- মূল ডিরেক্টরিতে `.env` ফাইল যা Azure পরিচয়পত্র ধারণ করে (মডিউল ০১ এ `azd up` দ্বারা তৈরি)

> **নোট:** আপনি যদি মডিউল ০১ শেষ না করে থাকেন, সেক্ষেত্রে সেখানে ডিপ্লয়মেন্ট নির্দেশনাগুলি প্রথমে অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

<img src="../../../translated_images/bn/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="প্রম্পট ইঞ্জিনিয়ারিং কি?" width="800"/>

প্রম্পট ইঞ্জিনিয়ারিং মূলত এমন ইনপুট টেক্সট ডিজাইন করা যা ধারাবাহিকভাবে আপনার প্রয়োজনীয় ফলাফল দেয়। এটি শুধু প্রশ্ন করা নয় - এটি এমনভাবে অনুরোধ গঠন করা যাতে মডেল সঠিকভাবে বুঝতে পারে আপনি কী চান এবং কীভাবে তা প্রদান করবে।

এটি এমন যেন আপনি সহকর্মীকে নির্দেশনা দিচ্ছেন। "বাগ ঠিক করো" অস্পষ্ট। "UserService.java এর লাইন ৪৫ এ নাল চেক যোগ করে নাল পয়েন্টার এক্সেপশন ঠিক করো" স্পষ্ট। ভাষা মডেলগুলোর ক্ষেত্রে একই নিয়ম প্রযোজ্য - স্পষ্টতা এবং গঠন গুরুত্বপূর্ণ।

<img src="../../../translated_images/bn/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="কিভাবে LangChain4j ফিট করে" width="800"/>

LangChain4j ইনফ্রাস্ট্রাকচার প্রদান করে — মডেল সংযোগ, মেমোরি, এবং ম্যাসেজ ধরনের ব্যবস্থা — আর প্রম্পট প্যাটার্ন হলো সেসব ইনফ্রাস্ট্রাকচারের মাধ্যমে পাঠানো যত্নসহ গঠিত টেক্সট। মূল বিল্ডিং ব্লক হলো `SystemMessage` (যা AI এর আচরণ ও ভূমিকা নির্ধারণ করে) এবং `UserMessage` (যা আপনার প্রকৃত অনুরোধ বহন করে)।

## প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়সমূহ

<img src="../../../translated_images/bn/five-patterns-overview.160f35045ffd2a94.webp" alt="পাঁচটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্নের সংক্ষিপ্ত বিবরণ" width="800"/>

এই মডিউলের উন্নত প্যাটার্নে যাওয়ার আগে, চলুন পাঁচটি মৌলিক প্রম্পটিং কৌশল পর্যালোচনা করি। এগুলো হলো প্রম্পট ইঞ্জিনিয়ারদের জন্য অপরিহার্য। আপনি যদি ইতিমধ্যে [কুইক স্টার্ট মডিউল](../00-quick-start/README.md#2-prompt-patterns) শেষ করে থাকেন, তাহলে এগুলো কার্যক্রমে দেখেছেন — এখানে তাদের ধারণাগত কাঠামো দেওয়া হয়েছে।

### জিরো-শট প্রম্পটিং

সবচেয়ে সহজ পদ্ধতি: মডেলকে সরাসরি কোনো উদাহরণ ছাড়া নির্দেশনা দেওয়া। মডেল সম্পূর্ণরূপে তার প্রশিক্ষণের উপর নির্ভর করে কাজ বুঝে এবং সম্পাদন করে। এটি সোজাসাপটা অনুরোধের জন্য ভালো কাজ করে যেখানে প্রত্যাশিত আচরণ স্বচ্ছ।

<img src="../../../translated_images/bn/zero-shot-prompting.7abc24228be84e6c.webp" alt="জিরো-শট প্রম্পটিং" width="800"/>

*উদাহরণ ছাড়া সরাসরি নির্দেশনা — মডেল কেবল নির্দেশ থেকে কাজ অনুমান করে*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// প্রতিক্রিয়া: "ইতিবাচক"
```
  
**কখন ব্যবহার করবেন:** সহজ শ্রেণীবিন্যাস, সরাসরি প্রশ্ন, অনুবাদ, অথবা এমন কোনো কাজ যেখানে অতিরিক্ত নির্দেশনার প্রয়োজন নেই।

### ফিউ-শট প্রম্পটিং

মডেলকে যে প্যাটার্ন অনুসরণ করতে হবে তা প্রদর্শন করে উদাহরণ দিন। মডেল আপনার উদাহরণ থেকে প্রত্যাশিত ইনপুট-আউটপুট ফরম্যাট শেখে এবং নতুন ইনপুটে তা প্রয়োগ করে। এটি সেগুলোর জন্য ধারাবাহিকতা উল্লেখযোগ্যভাবে বাড়িয়ে দেয় যেখানে কাঙ্ক্ষিত ফরম্যাট বা আচরণ স্পষ্ট নয়।

<img src="../../../translated_images/bn/few-shot-prompting.9d9eace1da88989a.webp" alt="ফিউ-শট প্রম্পটিং" width="800"/>

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
  
**কখন ব্যবহার করবেন:** কাস্টম শ্রেণীবিন্যাস, ধারাবাহিক ফরম্যাটিং, নির্দিষ্ট ডোমেনের কাজ, অথবা যখন জিরো-শট ফলাফল অনিশ্চিত হয়।

### চেইন অফ থট

মডেলকে ধাপে ধাপে তার যুক্তি প্রদর্শন করতে বলুন। সরাসরি উত্তর না দিয়ে, মডেল সমস্যা ভেঙে প্রতিটি অংশ স্পষ্টভাবে কাজ করে। এটি গণিত, লজিক, এবং বহু-ধাপ যুক্তি নিয়ন্ত্রণ কাজের নির্ভুলতা বাড়ায়।

<img src="../../../translated_images/bn/chain-of-thought.5cff6630e2657e2a.webp" alt="চেইন অফ থট প্রম্পটিং" width="800"/>

*ধাপে ধাপে যুক্তি প্রদর্শন — জটিল সমস্যাগুলো স্পষ্ট যৌক্তিক ধাপে ভাঙা*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// মডেলটি দেখায়: ১৫ - ৮ = ৭, তারপর ৭ + ১২ = ১৯ টি আপেল
```
  
**কখন ব্যবহার করবেন:** গণিত সমস্যা, লজিক পাজল, ডিবাগিং, অথবা এমন কোনো কাজ যেখানে যুক্তি প্রদর্শন নির্ভুলতা ও বিশ্বাসযোগ্যতা বাড়ায়।

### রোল-ভিত্তিক প্রম্পটিং

AI এর জন্য একটি persona বা ভূমিকা সেট করুন প্রশ্ন করার আগে। এটি উত্তরের স্বর, গভীরতা ও ফোকাস নির্ধারণ করে। "সফটওয়্যার আর্কিটেক্ট" "জুনিয়র ডেভেলপার" বা "সিকিউরিটি অডিটর" এর থেকে আলাদা পরামর্শ দেয়।

<img src="../../../translated_images/bn/role-based-prompting.a806e1a73de6e3a4.webp" alt="রোল-ভিত্তিক প্রম্পটিং" width="800"/>

*প্রসঙ্গ এবং persona সেট করা — একই প্রশ্ন পায় ভিন্ন উত্তর ভিত্তি করে ভিত্তি ভুমিকা*

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
  
**কখন ব্যবহার করবেন:** কোড রিভিউ, টিউটরিং, ডোমেন-নির্দিষ্ট বিশ্লেষণ, অথবা যখন বিশেষ দক্ষতা স্তর বা দৃষ্টিভঙ্গি অনুযায়ী উত্তর প্রয়োজন।

### প্রম্পট টেমপ্লেটস

পরিবর্তনীয় প্লেসহোল্ডার সহ পুনর্ব্যবহারযোগ্য প্রম্পট তৈরি করুন। প্রতি বার নতুন প্রম্পট লেখার বদলে, একবার টেমপ্লেট নির্ধারণ করুন এবং বিভিন্ন মান পূরণ করুন। LangChain4j এর `PromptTemplate` ক্লাস `{{variable}}` সিনট্যাক্সের মাধ্যমে এটি সহজ করে।

<img src="../../../translated_images/bn/prompt-templates.14bfc37d45f1a933.webp" alt="প্রম্পট টেমপ্লেটস" width="800"/>

*পরিবর্তনীয় প্লেসহোল্ডার সহ পুনরায় ব্যবহারযোগ্য প্রম্পট — এক টেমপ্লেট, বহু ব্যবহার*

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
  
**কখন ব্যবহার করবেন:** বিভিন্ন ইনপুটের সাথে পুনরাবৃত্তিমূলক প্রশ্ন, ব্যাচ প্রসেসিং, পুনঃব্যবহারযোগ্য AI ওয়ার্কফ্লো নির্মাণ, অথবা যেকোনো পরিস্থিতি যেখানে প্রম্পট কাঠামো একই থাকে কিন্তু ডেটা পরিবর্তিত হয়।

---

এই পাঁচটি মৌলিক বিষয় আপনাকে বেশিরভাগ প্রম্পটিং কাজের জন্য একটি দৃঢ় টুলকিট দেয়। অবশিষ্ট মডিউলটি এগুলোতে ভিত্তি করে গড়ে তোলা হয়েছে **আটটি উন্নত প্যাটার্নের** মাধ্যমে যা GPT-5.2 এর যুক্তি নিয়ন্ত্রণ, স্ব-মূল্যায়ন, এবং গঠিত আউটপুট ক্ষমতাসমূহ কাজে লাগায়।

## অগ্রণী প্যাটার্নস

মৌলিক বিষয়গুলো পর্যালোচনা হওয়ায়, চলুন আটটি উন্নত প্যাটার্নের দিকে যাই যা এই মডিউলকে অনন্য করে তোলে। সব সমস্যায় একই পদ্ধতির প্রয়োজন হয় না। কিছু প্রশ্ন দ্রুত উত্তর চায়, অন্যগুলো গভীর চিন্তা চায়। কিছুতে দৃশ্যমান যুক্তি দরকার, অন্যগুলো শুধু ফলাফল চায়। নিচের প্রতিটি প্যাটার্ন ভিন্ন পরিস্থিতির জন্য অপ্টিমাইজ করা হয়েছে — আর GPT-5.2 এর যুক্তি নিয়ন্ত্রণ পার্থক্যগুলোকে আরও স্পষ্ট করে তোলে।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="আটটি প্রম্পটিং প্যাটার্ন" width="800"/>

*আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন এবং তাদের ব্যবহার ক্ষেত্রের ওভারভিউ*

<img src="../../../translated_images/bn/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 এর যুক্তি নিয়ন্ত্রণ" width="800"/>

*GPT-5.2 এর যুক্তি নিয়ন্ত্রণ যা আপনাকে নির্দিষ্ট করতে দেয় মডেল কতটা চিন্তা করবে — দ্রুত সরাসরি উত্তর থেকে গভীর অন্বেষণ পর্যন্ত*

**কম উৎসাহ (দ্রুত ও ফোকাসড)** - সহজ প্রশ্নের জন্য যেখানে আপনি দ্রুত এবং সরাসরি উত্তর চান। মডেল কম যত্নসহকারে যুক্তি করে - সর্বাধিক ২ ধাপ। গণনা, লুকআপ বা সরল প্রশ্নের জন্য ব্যবহার করুন।

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
  
> 💡 **GitHub Copilot এর সাথে অন্বেষণ করুন:** ওপেন করুন [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) এবং জিজ্ঞাসা করুন:
> - "কম উৎসাহ এবং বেশি উৎসাহ প্রম্পট প্যাটার্নগুলোর মধ্যে পার্থক্য কী?"
> - "প্রম্পটগুলোর মধ্যে XML ট্যাগ কিভাবে AI এর উত্তর গঠন সাহায্য করে?"
> - "কখন স্ব-পর্যালোচনা প্যাটার্ন ব্যবহার করব এবং কখন সরাসরি নির্দেশনা?"

**বেশি উৎসাহ (গভীর ও সম্পূর্ণ)** - জটিল সমস্যার জন্য যেখানে আপনি ব্যাপক বিশ্লেষণ চান। মডেল পূর্ণাঙ্গভাবে অনুসন্ধান করে এবং বিস্তারিত যুক্তি প্রদর্শন করে। সিস্টেম ডিজাইন, আর্কিটেকচার সিদ্ধান্ত, বা জটিল গবেষণার জন্য ব্যবহার করুন।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**টাস্ক এক্সিকিউশন (ধাপে ধাপে অগ্রগতি)** - বহু-ধাপ ওয়ার্কফ্লোর জন্য। মডেল সর্বপ্রথম একটি পরিকল্পনা দেয়, কাজের প্রতি ধাপ বর্ণনা করে, তারপরে সারসংক্ষেপ প্রদান করে। মাইগ্রেশন, ইমপ্লিমেন্টেশন, বা যেকোনো বহু-ধাপ প্রক্রিয়ার জন্য ব্যবহার করুন।

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
  
চেইন-অফ-থট প্রম্পটিং স্পষ্টভাবে মডেলকে তার যুক্তি প্রদর্শন করতে বলে, যা জটিল কাজে নির্ভুলতা বাড়ায়। ধাপে ধাপে ভাঙ্গা মানব এবং AI উভয়ের জন্য যুক্তি বোঝা সহজ করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাটের সাথে চেষ্টা করুন:** এই প্যাটার্ন সম্পর্কে জিজ্ঞাসা করুন:
> - "দীর্ঘমেয়াদী অপারেশনের জন্য টাস্ক এক্সিকিউশন প্যাটার্ন কীভাবে অভিযোজিত করব?"
> - "উৎপাদন অ্যাপ্লিকেশনে টুল প্রাথমিক অংশ কাঠামোগত করার সেরা অনুশীলনগুলো কী?"
> - "কিভাবে UI তে মধ্যবর্তী অগ্রগতি আপডেট ধারণ ও প্রদর্শন করব?"

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="টাস্ক এক্সিকিউশন প্যাটার্ন" width="800"/>

*পরিকল্পনা → সম্পাদন → সারসংক্ষেপ বহু-ধাপ কাজের জন্য ওয়ার্কফ্লো*

**স্ব-পর্যালোচনা কোড** - উৎপাদন-গুণমান কোড তৈরি করার জন্য। মডেল উৎপাদন মান অনুসারে সঠিক ত্রুটি পরিচালনার সাথে কোড তৈরি করে। নতুন ফিচার বা সেবা নির্মাণের সময় ব্যবহার করুন।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="স্ব-পর্যালোচনা চক্র" width="800"/>

*পরিপূর্ণ উন্নয়ন চক্র - তৈরি, মূল্যায়ন, সমস্যা সনাক্তকরণ, উন্নতি, পুনরাবৃত্তি*

**গঠিত বিশ্লেষণ** - ধারাবাহিক মূল্যায়নের জন্য। মডেল নির্দিষ্ট ফ্রেমওয়ার্ক (সঠিকতা, অনুশীলন, কর্মক্ষমতা, সুরক্ষা, রক্ষণাবেক্ষণযোগ্যতা) ব্যবহার করে কোড পর্যালোচনা করে। কোড রিভিউ বা মান নিরীক্ষণের জন্য ব্যবহার করুন।

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
  
> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাটের সাথে চেষ্টা করুন:** গঠিত বিশ্লেষণ সম্পর্কে জিজ্ঞাসা করুন:
> - "বিভিন্ন ধরনের কোড রিভিউয়ের জন্য বিশ্লেষণ কাঠামো কিভাবে কাস্টমাইজ করব?"
> - "গঠিত আউটপুট প্রোগ্রাম্যাটিক্যালি পার্স এবং ব্যবহার করার সেরা পদ্ধতি কী?"
> - "বিভিন্ন রিভিউ সেশনে ধারাবাহিক গুরুতরতা স্তর নিশ্চিত করতে কিভাবে করব?"

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="গঠিত বিশ্লেষণ প্যাটার্ন" width="800"/>

*গুরুতরতা স্তরসহ ধারাবাহিক কোড পর্যালোচনার জন্য ফ্রেমওয়ার্ক*

**মাল্টি-টার্ন চ্যাট** - প্রসঙ্গের প্রয়োজন এমন কথোপকথনের জন্য। মডেল পূর্ববর্তী বার্তাগুলো স্মরণ করে এবং তাদের ওপর ভিত্তি করে তৈরি করে। ইন্টারেক্টিভ হেল্প সেশন বা জটিল প্রশ্নোত্তরের জন্য ব্যবহার করুন।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/bn/context-memory.dff30ad9fa78832a.webp" alt="প্রসঙ্গ স্মৃতি" width="800"/>

*কথোপকথনের প্রসঙ্গ একাধিক দফাতে জমা হয় যতক্ষণ না টোকেন সীমা পূর্ণ হয়*

**ধাপে ধাপে যুক্তি** - দৃশ্যমান লজিক প্রয়োজন এমন সমস্যার জন্য। মডেল প্রত্যেক ধাপের জন্য স্পষ্ট যুক্তি প্রদর্শন করে। গণিত সমস্যা, লজিক পাজল বা চিন্তাভাবনার প্রক্রিয়া বুঝতে চাইলে ব্যবহার করুন।

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

*সমস্যাগুলো স্পষ্ট যৌক্তিক ধাপে ভাঙ্গা*

**নির্দিষ্ট আউটপুট** - বিশেষ ফরম্যাটের শর্ত সাপেক্ষে প্রতিক্রিয়ার জন্য। মডেল ফরম্যাট এবং দৈর্ঘ্যের নিয়ম কঠোরভাবে অনুসরণ করে। সারসংক্ষেপ বা নির্দিষ্ট আউটপুট কাঠামোর প্রয়োজন হলে ব্যবহার করুন।

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
  
<img src="../../../translated_images/bn/constrained-output-pattern.0ce39a682a6795c2.webp" alt="নির্দিষ্ট আউটপুট প্যাটার্ন" width="800"/>

*বিশেষ ফরম্যাট, দৈর্ঘ্য, এবং কাঠামোর শর্ত কার্যকর করা*

## বিদ্যমান Azure রিসোর্স ব্যবহার

**ডিপ্লয়মেন্ট যাচাই করুন:**

মূল ডিরেক্টরিতে `.env` ফাইল নিশ্চিত করুন যা Azure পরিচয়পত্র ধারণ করে (মডিউল ০১ চলাকালীন তৈরি হয়েছে):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```
  
**অ্যাপ্লিকেশন চালু করুন:**

> **নোট:** আপনি যদি ইতিমধ্যে মডিউল ০১ থেকে `./start-all.sh` ব্যবহার করে সব অ্যাপ্লিকেশন চালু করে থাকেন, তাহলে এই মডিউলটি পোর্ট 8083 এ ইতিমধ্যে চলছে। নিচের স্টার্ট কমান্ডগুলি এড়িয়ে সরাসরি http://localhost:8083 এ যেতে পারেন।

**বিকল্প ১: স্প্রিং বুট ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য সুপারিশকৃত)**

ডেভ কন্টেইনারে স্প্রিং বুট ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত আছে, যা সব স্প্রিং বুট অ্যাপ্লিকেশন ভিজ্যুয়াল ইন্টারফেসে পরিচালনা করতে দেয়। VS Code এর বাম পাশে অ্যাক্টিভিটি বারে (স্প্রিং বুট আইকন খুঁজে) এটি পাওয়া যাবে।

স্প্রিং বুট ড্যাশবোর্ড থেকে আপনি:
- ওয়ার্কস্পেসের সব উপলব্ধ স্প্রিং বুট অ্যাপ্লিকেশন দেখতে পারবেন
- এক ক্লিকে অ্যাপ্লিকেশন শুরু/বন্ধ করতে পারবেন
- বাস্তব সময়ে অ্যাপ্লিকেশন লগ দেখতে পারবেন
- অ্যাপ্লিকেশন স্ট্যাটাস মনিটর করতে পারবেন
শুধুমাত্র "prompt-engineering" এর পাশে থাকা প্লে বাটনে ক্লিক করুন এই মডিউল শুরু করতে, অথবা সমস্ত মডিউল একসাথে শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার করা**

সমস্ত ওয়েব অ্যাপ্লিকেশন শুরু করুন (মডিউল ০১-০৪):

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

অথবা শুধুমাত্র এই মডিউলটি শুরু করুন:

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে রুট `.env` ফাইল থেকে পরিবেশ ভেরিয়েবলগুলি লোড করবে এবং যদি JAR ফাইলগুলি না থাকে তাহলে সেগুলো তৈরি করবে।

> **নোট:** আপনি যদি শুরু করার আগে সমস্ত মডিউল ম্যানুয়ালি তৈরি করতে চান:
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

**বন্ধ করতে:**

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
cd ..; .\stop-all.ps1  # সমস্ত মডিউলগুলি
```

## অ্যাপ্লিকেশন স্ক্রিনশটগুলি

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*মেন ড্যাশবোর্ড যেখানে সমস্ত ৮টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন তাদের বৈশিষ্ট্য ও ব্যবহারের ক্ষেত্রসহ প্রদর্শিত হয়*

## প্যাটার্নগুলি অন্বেষণ করা

ওয়েব ইন্টারফেসটি আপনাকে বিভিন্ন প্রম্পটিং কৌশল নিয়ে পরীক্ষা-নিরীক্ষা করার সুযোগ দেয়। প্রতিটি প্যাটার্ন বিভিন্ন ধরণের সমস্যার সমাধান করে - চেষ্টা করে দেখুন কখন কোন পদ্ধতি সবচেয়ে কার্যকর।

> **নোট: স্ট্রিমিং বনাম নন-স্ট্রিমিং** — প্রতিটি প্যাটার্ন পৃষ্ঠায় দুইটি বাটন থাকে: **🔴 Stream Response (Live)** এবং একটি **নন-স্ট্রিমিং** অপশন। স্ট্রিমিং সার্ভার-সেন্ট ইভেন্টস (SSE) ব্যবহার করে মডেল টোকেনগুলো তৈরি করার সময় তা রিয়েল-টাইমে দেখায়, তাই আপনি সাথে সাথে অগ্রগতি দেখতে পারেন। নন-স্ট্রিমিং অপশন পুরো প্রতিক্রিয়া আসা পর্যন্ত অপেক্ষা করে তারপর তা প্রদর্শন করে। যারা গভীর যুক্তি প্রয়োজন হয় এমন প্রম্পটের জন্য (যেমন High Eagerness, Self-Reflecting Code), নন-স্ট্রিমিং কল অনেক সময় নেয় — কখনো কখনো মিনিট ধরে — এবং কোন দৃশ্যমান প্রতিক্রিয়া পাওয়া যায় না। **জটিল প্রম্পটের সাথে পরীক্ষা করার সময় স্ট্রিমিং ব্যবহার করুন** যাতে আপনি মডেলের কাজ দেখতে পারেন এবং মনে না হয় অনুরোধটি সময় সীমা পার করে গেছে।
>
> **নোট: ব্রাউজারের প্রয়োজনীয়তা** — স্ট্রিমিং বৈশিষ্ট্যটি Fetch Streams API (`response.body.getReader()`) ব্যবহার করে যা একটি পূর্ণাঙ্গ ব্রাউজার (Chrome, Edge, Firefox, Safari) প্রয়োজন। VS Code এর বিল্ট-ইন Simple Browser এ কাজ করে না, কারণ এর ওয়েবভিউ ReadableStream API সাপোর্ট করে না। যদি আপনি Simple Browser ব্যবহার করেন, তাহলে নন-স্ট্রিমিং বাটনগুলি স্বাভাবিকভাবেই কাজ করবে — শুধুমাত্র স্ট্রিমিং বাটনগুলো প্রভাবিত হবে। সম্পূর্ণ অভিজ্ঞতার জন্য `http://localhost:8083` একটি বাহ্যিক ব্রাউজারে খুলুন।

### Low বনাম High Eagerness

একটি সহজ প্রশ্ন করুন যেমন "200 এর ১৫% কত?" Low Eagerness ব্যবহার করে। আপনি সঙ্গে সঙ্গে সরাসরি উত্তর পাবেন। এখন কিছু জটিল প্রশ্ন করুন যেমন "একটি হাই-ট্রাফিক API এর জন্য ক্যাশিং স্ট্র্যাটেজি ডিজাইন করুন" High Eagerness ব্যবহার করে। **🔴 Stream Response (Live)** ক্লিক করুন এবং মডেলের বিস্তারিত যুক্তি টোকেন-বাই-টোকেন দেখুন। একই মডেল, একই প্রশ্ন কাঠামো - কিন্তু প্রম্পট তাকে কয়টা ভাবতে হবে বলে নির্দেশ দেয়।

### Task Execution (টুল প্রিমেব্লস)

মাল্টি-স্টেপ ওয়ার্কফ্লোতে আগাম পরিকল্পনা এবং অগ্রগতি বর্ণনা লাভদায়ক। মডেল বলে কী করবে, প্রতিটি ধাপ বর্ণনা করে, তারপর ফলাফল সারাংশ দেয়।

### Self-Reflecting Code

"একটি ইমেইল যাচাই সেবা তৈরি করুন" চেষ্টা করুন। শুধুমাত্র কোড তৈরি ও থেমে না থেকে, মডেল কোড তৈরি করে, গুণগত মান যাচাই করে, দুর্বলতা চিহ্নিত করে, এবং উন্নত করে। আপনি দেখবেন এটি বারবার উন্নতি করছে যতক্ষণ না কোড প্রোডাকশন মান পূরণ করে।

### Structured Analysis

কোড রিভিউ ধারাবাহিক মূল্যায়ন কাঠামো চায়। মডেল একটি নির্দিষ্ট ক্যাটেগরিতে (সঠিকতা, অনুশীলন, কর্মক্ষমতা, নিরাপত্তা) কোড বিশ্লেষণ করে এবং গুরুত্ব স্তর দেয়।

### Multi-Turn Chat

"Spring Boot কী?" জিজ্ঞেস করুন, তারপরে সাথে সাথে "আমাকে একটি উদাহরণ দেখাও" বলুন। মডেল আপনার প্রথম প্রশ্ন মনে রাখে এবং বিশেষভাবে Spring Boot উদাহরণ দেয়। স্মৃতি না থাকলে দ্বিতীয় প্রশ্নটি অস্পষ্ট হত।

### Step-by-Step Reasoning

একটি গণিত সমস্যা বেছে নিন এবং Step-by-Step Reasoning এবং Low Eagerness উভয় দিয়ে চেষ্টা করুন। Low eagerness শুধু উত্তর দেয় - দ্রুত কিন্তু অস্পষ্ট। Step-by-step প্রতিটি হিসাব এবং সিদ্ধান্ত দেখায়।

### Constrained Output

যখন নির্দিষ্ট ফরম্যাট বা শব্দ সংখ্যা দরকার, এই প্যাটার্ন কঠোর নিয়ম মানে। ঠিক ১০০ শব্দের একটি সারাংশ বুলেট পয়েন্ট ফরম্যাটে তৈরি করে চেষ্টা করুন।

## আপনি যেটা আসলেই শিখছেন

**যুক্তিগত প্রচেষ্টা সবকিছু পরিবর্তন করে**

GPT-5.2 আপনাকে প্রম্পটের মাধ্যমে গণনাক্রমিক প্রচেষ্টা নিয়ন্ত্রণ করতে দেয়। কম প্রচেষ্টা মানে দ্রুত উত্তর কিন্তু কম অনুসন্ধান। বেশি প্রচেষ্টা মানে মডেল গভীরভাবে ভাবতে সময় নেয়। আপনি শেখাচ্ছেন কাজের জটিলতার সাথে প্রচেষ্টার মিল রাখা - সহজ প্রশ্নে সময় নষ্ট করবেন না, কিন্তু জটিল সিদ্ধান্ত দ্রুত নেবেন না।

**গঠন আচরণ নির্দেশ করে**

প্রম্পটগুলিতে XML ট্যাগ দেখছেন? সেগুলো সাজসজ্জা নয়। মডেলগুলি গঠনমূলক নির্দেশনা ফলো করে ফ্রি টেক্সটের চেয়ে বেশি নির্ভরযোগ্যভাবে। যখন মাল্টি-স্টেপ প্রক্রিয়া বা জটিল যুক্তি দরকার হয়, গঠন মডেলকে ট্র্যাক রাখতে সাহায্য করে এটি কোথায় আছে এবং পরবর্তী কি আসবে।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*একটি ভাল-গঠনকৃত প্রম্পটের শরীরচিত্র যেখানে পরিষ্কার বিভাগ এবং XML-শৈলীর আয়োজন রয়েছে*

**নিজেই মূল্যায়ন করে গুণগত মান**

সেলফ-রিফ্লেক্টিং প্যাটার্নগুলি গুণগত মান স্পষ্ট করে দেয়। মডেল "ঠিক কাজ করবে" আশা না করে, আপনি তাকে ঠিক কী বোঝায় "ঠিক": সঠিক যুক্তি, ত্রুটি পরিচালনা, কর্মক্ষমতা, নিরাপত্তা জানিয়ে দেন। তারপর মডেল নিজের আউটপুট যাচাই করে উন্নত করতে পারে। এটি কোড জেনারেশনকে একটি লটারি থেকে একটি প্রক্রিয়ায় পরিণত করে।

**কনটেক্সট সীমিত**

মাল্টি-টার্ণ কথোপকথন প্রতিটি অনুরোধের সাথে মেসেজ ইতিহাস অন্তর্ভুক্ত করে কাজ করে। কিন্তু একটি সীমা আছে - প্রতিটি মডেল একটি সর্বোচ্চ টোকেন সংখ্যা রাখে। কথোপকথন বাড়ার সাথে সাথে, আপনাকে প্রাসঙ্গিক কনটেক্সট বজায় রাখার কৌশল নিতে হবে যাতে তা সীমা ছাড়িয়ে না যায়। এই মডিউলটি শেখায় স্মৃতি কিভাবে কাজ করে; পরবর্তীতে আপনি শিখবেন কখন সারাংশ করতে হবে, কখন ভুলতে হবে, এবং কখন পুনরুদ্ধার করতে হবে।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**নেভিগেশন:** [← পূর্বের: মডিউল 01 - পরিচিতি](../01-introduction/README.md) | [মেইনে ফিরে যান](../README.md) | [পরবর্তী: মডিউল 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকারোক্তি**:  
এই দলিলটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল ভাষায় থাকা মূল দলিলটিকেই কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে সৃষ্ট কোনো ভুল বোঝাবুঝি বা ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->