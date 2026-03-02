# মডিউল ০২: GPT-5.2 সহ প্রম্পট ইঞ্জিনিয়ারিং

## বিষয়বস্তু সূচি

- [ভিডিও ওয়াকথ্রু](../../../02-prompt-engineering)
- [আপনি যা শিখবেন](../../../02-prompt-engineering)
- [পূর্বশর্ত](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং বোঝা](../../../02-prompt-engineering)
- [প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়াবলী](../../../02-prompt-engineering)
  - [জিরো-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [ফিউ-শট প্রম্পটিং](../../../02-prompt-engineering)
  - [চেইন অফ থট](../../../02-prompt-engineering)
  - [রোল-ভিত্তিক প্রম্পটিং](../../../02-prompt-engineering)
  - [প্রম্পট টেমপ্লেটস](../../../02-prompt-engineering)
- [উন্নত প্যাটার্নস](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন চালান](../../../02-prompt-engineering)
- [অ্যাপ্লিকেশন স্ক্রীনশটস](../../../02-prompt-engineering)
- [প্যাটার্নস অন্বেষণ](../../../02-prompt-engineering)
  - [লো বনাম হাই ইগারনেস](../../../02-prompt-engineering)
  - [টাস্ক এক্সিকিউশন (টুল প্রিম্বলস)](../../../02-prompt-engineering)
  - [সেলফ-রিফ্লেক্টিং কোড](../../../02-prompt-engineering)
  - [স্ট্রাকচার্ড অ্যানালাইসিস](../../../02-prompt-engineering)
  - [মাল্টি-টার্ন চ্যাট](../../../02-prompt-engineering)
  - [পর্বক্রমে যুক্তি](../../../02-prompt-engineering)
  - [নিয়ন্ত্রিত আউটপুট](../../../02-prompt-engineering)
- [আপনি আসলেই যা শিখছেন](../../../02-prompt-engineering)
- [পরবর্তী ধাপ](../../../02-prompt-engineering)

## ভিডিও ওয়াকথ্রু

এই লাইভ সেশনটি দেখুন যা ব্যাখ্যা করে কীভাবে এই মডিউল দিয়ে শুরু করবেন:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## আপনি যা শিখবেন

নিম্নলিখিত ডায়াগ্রামটি এই মডিউলে আপনি যে মূল বিষয় ও দক্ষতা অর্জন করবেন তার একটি ওভারভিউ প্রদান করে — প্রম্পট পরিমার্জন কৌশল থেকে শুরু করে আপনি যে পর্বক্রমভিত্তিক কাজের ধারা অনুসরণ করবেন তা পর্যন্ত।

<img src="../../../translated_images/bn/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

পূর্ববর্তী মডিউলগুলিতে, আপনি GitHub মডেলগুলির সাথে LangChain4j বেসিক ইন্টারঅ্যাকশন অন্বেষণ করেছেন এবং দেখেছেন কিভাবে মেমোরি Azure OpenAI-র সাথে কথোপকথনের AI সক্ষম করে। এখন আমরা ফোকাস করব প্রশ্ন করার পদ্ধতি — অর্থাৎ প্রম্পট নিজেই — Azure OpenAI-এর GPT-5.2 ব্যবহার করে। আপনার প্রম্পট গঠন করার ধরণ আপনার যে প্রতিক্রিয়া পাবেন তার গুণমানকে নাটকীয়ভাবে প্রভাবিত করে। আমরা মৌলিক প্রম্পটিং কৌশলগুলি পর্যালোচনা দিয়ে শুরু করব, তারপর আটটি উন্নত প্যাটার্নে যাব যা GPT-5.2-এর ক্ষমতাগুলি পুরোপুরি কাজে লাগায়।

আমরা GPT-5.2 ব্যবহার করব কারণ এটি যুক্তি নিয়ন্ত্রণ পরিচয় করায় - আপনি মডেলকে বলতে পারেন কতটা চিন্তা করতে হবে উত্তর দেওয়ার আগে। এটি বিভিন্ন প্রম্পটিং কৌশল আরও স্পষ্ট করে এবং আপনাকে বুঝতে সাহায্য করে কখন কোন পদ্ধতি ব্যবহার করবেন। আরও, GitHub মডেলগুলোর তুলনায় GPT-5.2-এর জন্য Azure-এর কম রেট লিমিট সুবিধাও পাব।

## পূর্বশর্ত

- মডিউল ০১ সম্পন্ন (Azure OpenAI রিসোর্স মোতায়েন)
- রুট ডিরেক্টরিতে `.env` ফাইল Azure শংসাপত্র সহ (মডিউল ০১-এ `azd up` দ্বারা তৈরি)

> **ট্রিক:** আপনি যদি মডিউল ০১ শেষ না করে থাকেন, প্রথমে সেখানে দেয়া ডিপ্লয়মেন্ট নির্দেশাবলী অনুসরণ করুন।

## প্রম্পট ইঞ্জিনিয়ারিং বোঝা

মূলত, প্রম্পট ইঞ্জিনিয়ারিং হল অস্পষ্ট নির্দেশনা এবং সুনির্দিষ্ট নির্দেশনার মধ্যে পার্থক্য, নিচের তুলনাটি তা দেখায়।

<img src="../../../translated_images/bn/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

প্রম্পট ইঞ্জিনিয়ারিং মানে এমন ইনপুট টেক্সট ডিজাইন করা যা নিয়মিতভাবে আপনাকে আপনার প্রয়োজনীয় ফলাফল দেয়। এটি শুধুমাত্র প্রশ্ন করার ব্যাপার নয়—এটি এমনভাবে অনুরোধ সাজানোর ব্যাপার যাতে মডেল স্পষ্টভাবে বুঝতে পারে আপনি কী চান এবং কিভাবে তা সরবরাহ করতে হবে।

এটি ভাবুন আপনার সহকর্মীকে নির্দেশ দেওয়ার মত। "বাগ ঠিক করো" অস্পষ্ট। "UserService.java ফাইলে ৪৫ নম্বর লাইনে null পয়েন্টার এক্সসেপশন ঠিক করো null চেক যোগ করে" সুনির্দিষ্ট। ভাষার মডেলও একই রকম কাজ করে—বিশেষত্ব এবং গঠন গুরুত্বপূর্ণ।

নিচের ডায়াগ্রামটি দেখায় কীভাবে LangChain4j এই ছবি টেপে প্রম্পট প্যাটার্নগুলিকে মডেলের সাথে সংযুক্ত করে SystemMessage এবং UserMessage ব্লকগুলোর মাধ্যমে।

<img src="../../../translated_images/bn/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j অবকাঠামো সরবরাহ করে — মডেল সংযোগ, মেমোরি, ও মেসেজ টাইপ — আর প্রম্পট প্যাটার্ন হল সাবধানে গঠিত পাঠ্য যা আপনি ওই অবকাঠামোর মধ্যে পাঠান। মূল ব্লক হল `SystemMessage` (যা AI এর আচরণ ও ভূমিকা নির্ধারণ করে) এবং `UserMessage` (যা আপনার প্রকৃত অনুরোধ বহন করে)।

## প্রম্পট ইঞ্জিনিয়ারিং মৌলিক বিষয়াবলী

নিম্নলিখিত পাঁচটি মূল কৌশল কার্যকর প্রম্পট ইঞ্জিনিয়ারিংয়ের ভিত্তি গঠন করে। প্রত্যেকটি ভাষার মডেলের সাথে আপনার যোগাযোগের একটি ভিন্ন দিক নজর দেয়।

<img src="../../../translated_images/bn/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

এই মডিউলের উন্নত প্যাটার্নগুলিতে প্রবেশ করার আগে, চলুন পাঁচটি মৌলিক প্রম্পটিং কৌশল পর্যালোচনা করি। এগুলো হল সেই নির্মাণ ব্লক যা প্রতিটি প্রম্পট ইঞ্জিনিয়ারকে জানা উচিত। আপনি যদি ইতিমধ্যে [Quick Start মডিউল](../00-quick-start/README.md#2-prompt-patterns) অনুসরণ করে থাকেন, তাহলে এগুলো দেখতে পেয়েছেন — এগুলোর পেছনের ধারনামূলক কাঠামো এখানে আছে।

### জিরো-শট প্রম্পটিং

সবচেয়ে সোজা পদ্ধতি: মডেলকে কোনো উদাহরণ ছাড়াই সরাসরি নির্দেশ দিন। মডেল সম্পূর্ণরূপে তার প্রশিক্ষণের ওপর নির্ভর করে কাজ বুঝে এবং সম্পাদন করে। এটি সোজা অনুরোধের ক্ষেত্রে ভালো কাজ করে যেখানে প্রত্যাশিত আচরণ স্পষ্ট।

<img src="../../../translated_images/bn/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*উদাহরণ ছাড়া সরাসরি নির্দেশ — মডেল শুধু নির্দেশ থেকে টাস্ক বুজে নেয়*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// প্রতিক্রিয়া: "ইতিবাচক"
```

**কখন ব্যবহার করবেন:** সরল শ্রেণীবিভাগ, সরাসরি প্রশ্ন, অনুবাদ, অথবা এমন কোনো টাস্ক যেখানে অতিরিক্ত নির্দেশনার প্রয়োজন নেই।

### ফিউ-শট প্রম্পটিং

উদাহরণ দিন যা মডেলকে আপনি যে প্যাটার্ন অনুসরণ করতে চান তা প্রদর্শন করে। মডেল আপনার উদাহরণ থেকে প্রত্যাশিত ইনপুট-আউটপুট ফর্ম্যাট শিখে নতুন ইনপুটের ওপর প্রয়োগ করে। এটি উন্নত সামঞ্জস্যতা দেয় যখন কাঙ্ক্ষিত ফর্ম্যাট বা আচরণ অপ্রচলিত।

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

**কখন ব্যবহার করবেন:** কাস্টম শ্রেণীবিভাগ, সঙ্গতিপূর্ণ ফরম্যাটিং, ডোমেইন-নির্দিষ্ট কাজ, অথবা যখন জিরো-শট ফলাফল অনিয়মিত হয়।

### চেইন অফ থট

মডেলকে তার যুক্তি পর্বক্রমে দেখাতে বলুন। সরাসরি উত্তর দেওয়ার পরিবর্তে মডেল সমস্যা ভেঙে প্রতিটি অংশ স্পষ্টভাবে ব্যাখ্যা করে। এটি গণিত, লজিক, ও বহু-পর্ব যুক্তি কার্যক্রমে নির্ভুলতা বাড়ায়।

<img src="../../../translated_images/bn/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*পর্বক্রমে যুক্তি — জটিল সমস্যাকে স্পষ্ট যৌক্তিক ধাপে ভাঙা*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// মডেলটি প্রদর্শন করে: ১৫ - ৮ = ৭, তারপর ৭ + ১২ = ১৯ আপেল
```

**কখন ব্যবহার করবেন:** গাণিতিক সমস্যাগুলি, যুক্তি পাজল, ডিবাগিং, অথবা এমন টাস্ক যেখানে যুক্তি প্রক্রিয়া প্রদর্শন সঠিকতা ও বিশ্বাসযোগ্যতা বাড়ায়।

### রোল-ভিত্তিক প্রম্পটিং

প্রশ্ন করার আগে AI-এর জন্য একটি ব্যক্তিত্ব বা ভূমিকা নির্ধারণ করুন। এটি প্রসঙ্গ দেয় যা প্রতিক্রিয়ার সুর, গভীরতা, ও ফোকাস গঠন করে। একজন "সফটওয়্যার আর্কিটেক্ট" এর পরামর্শ "জুনিয়র ডেভেলপার" বা "সিকিউরিটি অডিটর" থেকে আলাদা।

<img src="../../../translated_images/bn/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*প্রসঙ্গ ও ব্যক্তিত্ব নির্ধারণ — একই প্রশ্ন ভিন্ন ভূমিকার ওপর ভিত্তি করে ভিন্ন উত্তর পায়*

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

**কখন ব্যবহার করবেন:** কোড রিভিউ, টিউটরিং, ডোমেইন-নির্দিষ্ট বিশ্লেষণ, অথবা নির্দিষ্ট দক্ষতা স্তর বা দৃষ্টিভঙ্গির উপযোগী উত্তর চাইলে।

### প্রম্পট টেমপ্লেটস

ভারি প্লেসহোল্ডার সহ পুনঃব্যবহারযোগ্য প্রম্পট তৈরি করুন। প্রতি বার নতুন প্রম্পট লেখার পরিবর্তে একবার টেমপ্লেট ডিফাইন করুন এবং বিভিন্ন মান পূরণ করুন। LangChain4j-এর `PromptTemplate` ক্লাস `{{variable}}` সিনট্যাক্স দিয়ে এটিকে সহজ করে তোলে।

<img src="../../../translated_images/bn/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*পুনঃব্যবহারযোগ্য প্রম্পটস সহ প্লেসহোল্ডার — এক টেমপ্লেট, অনেক ব্যবহার*

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

**কখন ব্যবহার করবেন:** বিভিন্ন ইনপুটসহ পুনরাবৃত্ত প্রশ্ন, ব্যাচ প্রোসেসিং, পুনঃব্যবহারযোগ্য AI ওয়ার্কফ্লো তৈরি, অথবা যেখানে প্রম্পট কাঠামো একই থাকে কিন্তু ডেটা পরিবর্তিত হয়।

---

এই পাঁচটি মৌলিক বিষয় আপনার জন্য একটি শক্তিশালী সরঞ্জাম প্যাকেজ দেয় অধিকাংশ প্রম্পটিং কাজের জন্য। বাকিটা এই মডিউল জুড়ে আপনাকে আটটি উন্নত প্যাটার্ন শেখাবে যা GPT-5.2-এর যুক্তি নিয়ন্ত্রণ, আত্ম-মূল্যায়ন, এবং কাঠামোবদ্ধ আউটপুট সক্ষমতা ব্যবহার করে।

## উন্নত প্যাটার্নস

মৌলিক বিষয়গুলি আচ্ছাদিত হলে, চলুন শুরু করি আটটি উন্নত প্যাটার্ন দিয়ে যা এই মডিউলকে অনন্য করে তোলে। সব সমস্যার জন্য একই পদ্ধতি প্রয়োজন হয় না। কিছু প্রশ্ন দ্রুত উত্তর চাই, অন্যগুলি গভীর চিন্তা। কিছু দৃশ্যমান যুক্তি চাই, কিছু শুধু ফলাফল। নিচের প্রতিটি প্যাটার্ন ভিন্ন পরিস্থিতির জন্য অপ্টিমাইজড — আর GPT-5.2-এর যুক্তি নিয়ন্ত্রণ পার্থক্যগুলো আরও স্পষ্ট করে তোলে।

<img src="../../../translated_images/bn/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*আটটি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন এবং তাদের ব্যবহারের ক্ষেত্রে ওভারভিউ*

GPT-5.2 এই প্যাটার্নগুলিতে আরেকটি মাত্রা যোগ করে: *যুক্তি নিয়ন্ত্রণ*। নিচের স্লাইডার দেখায় কিভাবে আপনি মডেলের চিন্তার মাত্রা সামঞ্জস্য করতে পারেন — দ্রুত, সরাসরি উত্তর থেকে শুরু করে গভীর ও বিস্তৃত বিশ্লেষণ পর্যন্ত।

<img src="../../../translated_images/bn/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2-এর যুক্তি নিয়ন্ত্রণ আপনাকে নির্দিষ্ট করতে দেয় মডেল কতটা চিন্তা করবে — দ্রুত সরাসরি উত্তর থেকে গভীর অনুসন্ধান পর্যন্ত*

**লো ইগারনেস (দ্রুত ও ফোকাসড)** - সরল প্রশ্নের জন্য যেখানে আপনি দ্রুত, সরাসরি উত্তর চান। মডেল সর্বোচ্চ ২ ধাপ চিন্তা করে। গণনা, লুকআপ, বা সরল প্রশ্নের জন্য ব্যবহার করুন।

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

> 💡 **GitHub Copilot দিয়ে এক্সপ্লোর করুন:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) খুলে জিজ্ঞাসা করুন:
> - "লো ইগারনেস এবং হাই ইগারনেস প্রম্পটিং প্যাটার্নগুলোর মধ্যে পার্থক্য কী?"
> - "প্রম্পটে XML ট্যাগগুলো কীভাবে AI-এর প্রতিক্রিয়া গঠন করতে সাহায্য করে?"
> - "সেলফ-রিফ্লেকশন প্যাটার্ন কখন আর সরাসরি নির্দেশ কখন ব্যবহার করব?"

**হাই ইগারনেস (গভীর ও বিস্তৃত)** - জটিল সমস্যার জন্য যেখানে আপনি ব্যাপক বিশ্লেষণ চান। মডেল বিস্তারিতভাবে তদন্ত করে যুক্তি প্রদর্শন করে। সিস্টেম ডিজাইন, আর্কিটেকচার সিদ্ধান্ত, বা জটিল গবেষণার জন্য ব্যবহার করুন।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**টাস্ক এক্সিকিউশন (পর্বক্রমে অগ্রগতি)** - বহু পর্যায়ের ওয়ার্কফ্লো জন্য। মডেল প্রথমে প্ল্যান দেয়, কাজের সময় প্রতিটি ধাপ বর্ণনা করে, তারপর সারাংশ দেয়। মাইগ্রেশন, বাস্তবায়ন বা যেকোনো বহু-পর্বের কাজের জন্য ব্যবহার করুন।

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

চেইন-অফ-থট প্রম্পটিং মডেলকে স্পষ্টভাবে তার যুক্তি প্রক্রিয়া দেখাতে বলে, যা জটিল কাজের নির্ভুলতা বাড়ায়। পর্বক্রমে যুক্তি মানুষের পাশাপাশি AI-কে লজিক বুঝতে সাহায্য করে।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাটের সাথে চেষ্টা করুন:** এই প্যাটার্ন নিয়ে জিজ্ঞাসা করুন:
> - "দীর্ঘকাল চলমান অপারেশনের জন্য টাস্ক এক্সিকিউশন প্যাটার্ন কিভাবে অভিযোজিত হবে?"
> - "প্রোডাকশনে টুল প্রিম্বল গঠন করার সেরা পদ্ধতিগুলো কী?"
> - "UI তে মধ্যবর্তী অগ্রগতি আপডেট ধরার এবং প্রদর্শনের উপায় কী?"

নিচের ডায়াগ্রামটি দেখায় এই Plan → Execute → Summarize ওয়ার্কফ্লো।

<img src="../../../translated_images/bn/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*পর্বক্রমে টাস্কের জন্য Plan → Execute → Summarize ওয়ার্কফ্লো*

**সেলফ-রিফ্লেক্টিং কোড** - প্রোডাকশন মানের কোড তৈরি করার জন্য। মডেল উৎপাদন মান অনুসরণ করে কোড জেনারেট করে যা সঠিক ত্রুটি হ্যান্ডলিং সহ। নতুন ফিচার বা সার্ভিস নির্মাণের জন্য ব্যবহার করুন।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

নিচের ডায়াগ্রামটি দেখায় এই পুনরাবৃত্তিমূলক উন্নতি চক্র — জেনারেট, মূল্যায়ন, দুর্বলতা চিহ্নিতকরণ, ও পরিমার্জন যতক্ষণ না কোড প্রোডাকশন মান পূরণ করে।

<img src="../../../translated_images/bn/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*পুনরাবৃত্তিমূলক উন্নতি চক্র - তৈরি করুন, মূল্যায়ন করুন, সমস্যা ধরুন, উন্নত করুন, পুনরাবৃত্তি করুন*

**স্ট্রাকচার্ড অ্যানালাইসিস** - সচ্ছল মূল্যায়নের জন্য। মডেল কোড পর্যালোচনা করে একটি নির্দিষ্ট কাঠামো অনুসারে (সঠিকতা, অনুশীলন, কর্মক্ষমতা, নিরাপত্তা, রক্ষণাবেক্ষণযোগ্যতা)। কোড রিভিউ বা গুণমান নিরূপণের জন্য ব্যবহার করুন।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) চ্যাট দিয়ে চেষ্টা করুন:** স্ট্রাকচার্ড অ্যানালাইসিস সম্পর্কে জিজ্ঞাসা করুন:
> - "বিভিন্ন ধরনের কোড রিভিউ জন্য আমি কিভাবে বিশ্লেষণ কাঠামো কাস্টমাইজ করব?"
> - "স্ট্রাকচার্ড আউটপুট প্রোগ্রাম্যাটিকভাবে পার্স এবং প্রয়োগের সেরা উপায় কী?"
> - "কিভাবে নিশ্চিত করব বিভিন্ন রিভিউ সেশনে ধারাবাহিক গুরুতর স্তর থাকবে?"

নিম্নলিখিত ডায়াগ্রাম দেখায় কিভাবে এই কাঠামো একটি কোড রিভিউকে ধারাবাহিক বিভাগে গুরুতর স্তরসহ সংগঠিত করে।

<img src="../../../translated_images/bn/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*গুরুত্ব স্তরের সাথে ধারাবাহিক কোড রিভিউয়ের ফ্রেমওয়ার্ক*

**মাল্টি-টার্ন চ্যাট** - প্রসঙ্গ প্রয়োজন এমন কথোপকথনের জন্য। মডেল পূর্বের বার্তাগুলো মনে রাখে এবং তার ওপর ভিত্তি করে তৈরি করে। ইন্টারেক্টিভ সাহায্য সেশন বা জটিল প্রশ্নোত্তরের জন্য ব্যবহার করুন।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

নিচের ডায়াগ্রামটি দেখায় কিভাবে কথোপকথনের প্রসঙ্গ প্রতি টার্নে জমা হয় এবং তা মডেলের টোকেন সীমার সাথে সম্পর্কিত।

<img src="../../../translated_images/bn/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*কথোপকথনের প্রসঙ্গ কিভাবে একাধিক পর্যায়ে জমা হয় যতক্ষণ না টোকেন সীমায় পৌঁছে*
**ধাপে ধাপে যুক্তি** - স্পষ্ট যুক্তি প্রয়োজন এমন সমস্যার জন্য। মডেল প্রতিটি ধাপের জন্য স্পষ্ট যুক্তি প্রদর্শন করে। গণিত সমস্যা, যুক্তি ধাঁধা, অথবা যখন আপনি চিন্তার প্রক্রিয়া বুঝতে চান তখন এটি ব্যবহার করুন।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

নিচের ছবিটি দেখায় কীভাবে মডেল সমস্যা স্পষ্ট, নম্বরকৃত যুক্তির ধাপে ধাপে বিভক্ত করে।

<img src="../../../translated_images/bn/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*সমস্যাগুলোকে স্পষ্ট যুক্তির ধাপে বিভক্ত করা হচ্ছে*

**সীমাবদ্ধ আউটপুট** - নির্দিষ্ট ফরম্যাটের প্রয়োজন এমন প্রতিক্রিয়ার জন্য। মডেল ফরম্যাট এবং দৈর্ঘ্যের নিয়ম কঠোরভাবে অনুসরণ করে। সারাংশ বা নির্দিষ্ট আউটপুট কাঠামো প্রয়োজন হলে ব্যবহার করুন।

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

নিম্নের চিত্রটি দেখায় কীভাবে সীমাবদ্ধতা মডেলকে আপনার ফরম্যাট এবং দৈর্ঘ্যের নিয়ম কঠোরভাবে মেনে আউটপুট তৈরি করতে নির্দেশ দেয়।

<img src="../../../translated_images/bn/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*নির্দিষ্ট ফরম্যাট, দৈর্ঘ্য, এবং কাঠামো প্রয়োগ করা হচ্ছে*

## অ্যাপ্লিকেশন চালান

**ডিপ্লয়মেন্ট যাচাই করুন:**

রুট ডিরেক্টরিতে `.env` ফাইলটি নিশ্চিত করুন যা Azure সার্টিফিকেট (Module 01 চলাকালীন তৈরি) সহ রয়েছে। এটি মডিউল ডিরেক্টরি (`02-prompt-engineering/`) থেকে চালান:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT দেখানো উচিত
```

**অ্যাপ্লিকেশন শুরু করুন:**

> **বিঃদ্রঃ:** যদি আপনি ইতোমধ্যে রুট ডিরেক্টরি থেকে `./start-all.sh` ব্যবহার করে সব অ্যাপ্লিকেশন চালু করে থাকেন (যেমন Module 01 এ বর্ণিত), তবে এই মডিউল ইতোমধ্যে পোর্ট 8083 এ চলছে। নিচের শুরু কমান্ডগুলো এড়িয়ে সরাসরি http://localhost:8083 এ যেতে পারেন।

**বিকল্প ১: Spring Boot ড্যাশবোর্ড ব্যবহার (VS Code ব্যবহারকারীদের জন্য সুপারিশ)**

ডেভ কন্টেইনারে Spring Boot ড্যাশবোর্ড এক্সটেনশন অন্তর্ভুক্ত রয়েছে, যা সব Spring Boot অ্যাপ্লিকেশন পরিচালনার জন্য ভিজ্যুয়াল ইন্টারফেস প্রদান করে। এটি VS Code এর বাম পাশের Activity Bar এ পাওয়া যাবে (Spring Boot আইকন খুঁজুন)।

Spring Boot ড্যাশবোর্ড থেকে আপনি:
- ওয়ার্কস্পেসের সব উপলব্ধ Spring Boot অ্যাপ্লিকেশন দেখতে পাবেন
- এক ক্লিকে অ্যাপ্লিকেশন চালু/বন্ধ করতে পারবেন
- রিয়েল-টাইমে অ্যাপ্লিকেশন লগ দেখতে পারবেন
- অ্যাপ্লিকেশন স্থিতি পর্যবেক্ষণ করতে পারবেন

সরাসরি "prompt-engineering" এর পাশে প্লে বোতামে ক্লিক করে এই মডিউল চালু করুন, অথবা সকল মডিউল একসাথে শুরু করুন।

<img src="../../../translated_images/bn/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code এর Spring Boot ড্যাশবোর্ড — এক স্থান থেকে সব মডিউল শুরু, বন্ধ ও পর্যবেক্ষণ করুন*

**বিকল্প ২: শেল স্ক্রিপ্ট ব্যবহার**

সব ওয়েব অ্যাপ্লিকেশন চালু করুন (মডিউল 01-04):

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

উভয় স্ক্রিপ্ট স্বয়ংক্রিয়ভাবে রুট `.env` ফাইল থেকে পরিবেশ ভেরিয়েবল লোড করে এবং JAR ফাইলগুলি তৈরি করবে যদি সেগুলি না থাকে।

> **বিঃদ্রঃ:** যদি আপনি শুরু করার আগে সব মডিউল নিজে নিজে তৈরি করতে চান:
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
cd .. && ./stop-all.sh  # সমস্ত মডিউলগুলি
```

**PowerShell:**
```powershell
.\stop.ps1  # শুধুমাত্র এই মডিউল
# অথবা
cd ..; .\stop-all.ps1  # সমস্ত মডিউল
```

## অ্যাপ্লিকেশন স্ক্রিনশট

এখানে প্রম্পট ইঞ্জিনিয়ারিং মডিউলের প্রধান ইন্টারফেস, যেখানে আপনি সব আটটি প্যাটার্ন পাশে পাশে পরীক্ষা করতে পারবেন।

<img src="../../../translated_images/bn/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*মূল ড্যাশবোর্ড যেখানে সব ৮টি প্রম্পট ইঞ্জিনিয়ারিং প্যাটার্ন তাদের স্বকীয়তা ও ব্যবহার ক্ষেত্রসহ দেখানো হয়েছে*

## প্যাটার্নগুলি আবিষ্কার করা

ওয়েব ইন্টারফেসে আপনি বিভিন্ন প্রম্পটিং কৌশল পরীক্ষা করতে পারেন। প্রতিটি প্যাটার্ন বিভিন্ন সমস্যা সমাধান করে – দেখে নিন কোন পদ্ধতি কখন কার্যকর।

> **বিঃদ্রঃ: স্ট্রিমিং বনাম নন-স্ট্রিমিং** — প্রতিটি প্যাটার্ন পৃষ্ঠায় দুইটি বোতাম আছে: **🔴 Stream Response (Live)** এবং একটি **Non-streaming** বিকল্প। স্ট্রিমিং Server-Sent Events (SSE) ব্যবহার করে মডেল তৈরি করা টোকেনগুলো রিয়েল-টাইমে দেখায়, তাই আপনি প্রগতি তৎক্ষণাৎ দেখতে পাবেন। নন-স্ট্রিমিং বিকল্প পুরো উত্তর আসার জন্য অপেক্ষা করে তারপর প্রদর্শন করে। গভীর যুক্তি প্রয়োজন এমন প্রম্পটের জন্য (যেমন High Eagerness, Self-Reflecting Code), নন-স্ট্রিমিং অনেক সময় নিতে পারে — কখনও কখনও মিনিটের পর মিনিট — কিন্তু কোনো দৃশ্যমান প্রতিক্রিয়া থাকে না। **জটিল প্রম্পট পরীক্ষা করার সময় স্ট্রিমিং ব্যবহার করুন** যাতে আপনি মডেল কাজ করছে দেখতে পারেন এবং মনে না হয় অনুরোধ সময়সীমা পেরিয়েছে।
>
> **ব্রাউজার প্রয়োজনীয়তা** — স্ট্রিমিং ফিচার Fetch Streams API (`response.body.getReader()`) ব্যবহার করে, যা পূর্ণ ব্রাউজার (Chrome, Edge, Firefox, Safari) এ কাজ করে। VS Code এর বিল্ট-ইন Simple Browser এ কাজ করে না, কারণ তার webview ReadableStream API সমর্থন করে না। Simple Browser ব্যবহার করলে নন-স্ট্রিমিং বোতামগুলি স্বাভাবিক কাজ করবে — শুধু স্ট্রিমিং বোতামগুলো প্রভাবিত হবে। পূর্ণ অভিজ্ঞতার জন্য http://localhost:8083 একটি বাহ্যিক ব্রাউজারে খুলুন।

### কম বনাম উচ্চ আগ্রহ

সহজ প্রশ্ন জিজ্ঞাসা করুন, যেমন "200 এর 15% কত?" কম আগ্রহ ব্যবহার করে। আপনি দ্রুত, সরাসরি উত্তর পাবেন। এখন জটিল কিছু জিজ্ঞাসা করুন, যেমন "একটি উচ্চ-ট্রাফিক API এর জন্য কীভাবে ক্যাশিং কৌশল ডিজাইন করবেন" উচ্চ আগ্রহ ব্যবহার করে। **🔴 Stream Response (Live)** এ ক্লিক করুন এবং মডেলের বিস্তারিত যুক্তি টোকেন অনুযায়ী আসতে দেখুন। একই মডেল, একই প্রশ্ন কাঠামো - তবে প্রম্পট তাকে কতটা চিন্তা করতে হবে বলে নির্দেশ দেয়।

### টাস্ক এক্সিকিউশন (টুল প্রিম্বলস)

মাল্টি-স্টেপ ওয়ার্কফ্লো সাধনায় প্রথমে পরিকল্পনা ও প্রগতি বর্ণনা উপকারী হয়। মডেল বলে দেয় কি করবে, প্রতিটি ধাপ বর্ণনা করে, তারপর ফলাফল সারাংশ দেয়।

### সেলফ-রিফ্লেক্টিং কোড

"একটি ইমেইল বৈধতা সেবা তৈরি করুন" চেষ্টা করুন। শুধু কোড তৈরি করে থেমে না, মডেল কোড তৈরি করে, গুণগত মান যাচাই করে, দুর্বলতা সনাক্ত করে এবং উন্নত করে। আপনি দেখবেন এটি কতক্ষণ পুনরাবৃত্তি করে যতক্ষণ কোড প্রোডাকশন মান পূরণ করে।

### স্ট্রাকচারড অ্যানালিসিস

কোড রিভিউ এর জন্য নির্দিষ্ট মূল্যায়ন ফ্রেমওয়ার্ক দরকার। মডেল কোড বিশ্লেষণ করে নির্দিষ্ট বিভাগে (সঠিকতা, চর্চা, কর্মক্ষমতা, সুরক্ষা) এবং গাম্ভীর্যের স্তর নির্ধারণ করে।

### মাল্টি-টার্ন চ্যাট

"Spring Boot কী?" জিজ্ঞাসা করুন, তারপর তৎক্ষণাৎ "আমাকে একটি উদাহরণ দেখাও" বলুন। মডেল আপনার প্রথম প্রশ্ন স্মরণ রাখে এবং স্পেসিফিক একটি Spring Boot উদাহরণ দেয়। স্মৃতি না থাকলে দ্বিতীয় প্রশ্নটি খুব অস্পষ্ট হত।

### ধাপে ধাপে যুক্তি

কোনো গণিত সমস্যার কাজ নিয়ে দেখুন ধাপে ধাপে যুক্তি এবং কম আগ্রহ দিয়ে। কম আগ্রহ দ্রুত উত্তর দেয় - দ্রুত কিন্তু অস্বচ্ছ। ধাপে ধাপে প্রতিটি হিসাব ও সিদ্ধান্ত দেখায়।

### সীমাবদ্ধ আউটপুট

যখন নির্দিষ্ট ফরম্যাট বা শব্দ সংখ্যা দরকার, এই প্যাটার্ন কঠোরভাবে অনুসরণ করে। উদাহরণস্বরূপ ঠিক ১০০ শব্দের বুলেট পয়েন্ট সারসংক্ষেপ তৈরি করুন।

## আপনি আসলেই কি শিখছেন

**যুক্তি প্রচেষ্টা সবকিছু পরিবর্তন করে**

GPT-5.2 আপনাকে আপনার প্রম্পটের মাধ্যমে কম্পিউটেশনাল প্রচেষ্টা নিয়ন্ত্রণ করতে দেয়। কম প্রচেষ্টা মানে দ্রুত সাড়া, সীমিত অনুসন্ধান। উচ্চ প্রচেষ্টা মানে মডেল গভীরভাবে চিন্তা করে। আপনি শিখছেন কিভাবে কাজের জটিলতার সাথে প্রচেষ্টা মিলিয়ে নিতে হয় - সহজ প্রশ্নে সময় নষ্ট করবেন না, কিন্তু জটিল সিদ্ধান্তও তাড়াহুড়ো করবেন না।

**কাঠামো আচরণ নির্দেশ করে**

প্রম্পটে দেখতে XML ট্যাগ আছে? সেগুলো সাজসরঞ্জাম নয়। মডেলগুলো গঠনভিত্তিক নির্দেশ ভালভাবে অনুসরণ করে। যখন মাল্টি-স্টেপ প্রক্রিয়া বা জটিল যুক্তি দরকার, কাঠামো মডেলকে ঠিক কতদূর গিয়েছে এবং পরবর্তী ধাপ কি তা বুঝতে সাহায্য করে। নিচের চিত্রটি একটি সুসংগঠিত প্রম্পটের গঠন দেখায়, যেখানে `<system>`, `<instructions>`, `<context>`, `<user-input>`, এবং `<constraints>` ট্যাগগুলো স্পষ্ট বিভাগে আপনার নির্দেশ দেয়।

<img src="../../../translated_images/bn/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*পরিষ্কার বিভাগের সাথে সুসংগঠিত প্রম্পটের অ্যানাটমি এবং XML-ধাঁচের সংগঠন*

**গুণগত মান স্ব-মূল্যায়নের মাধ্যমে**

সেলফ-রিফ্লেক্টিং প্যাটার্নগুলো গুণগত মান স্পষ্ট করে দেয়। মডেল "ঠিক কাজ করেছে" আশা না করে আপনি ঠিক কি "ঠিক" তা বলেন: সঠিক যুক্তি, ত্রুটি হ্যান্ডলিং, কর্মক্ষমতা, নিরাপত্তা। এরপর মডেল নিজের আউটপুট মূল্যায়ন করে উন্নতি করতে পারে। এটি কোড তৈরি প্রক্রিয়াকে লটারি থেকে পদ্ধতিতে পরিণত করে।

**প্রসঙ্গ সীমিত**

মাল্টি-টার্ন কথোপকথন প্রতিটি অনুরোধের সাথে মেসেজ ইতিহাস রাখার মাধ্যমে কাজ করে। কিন্তু একটি সীমা আছে - প্রতিটি মডেলের সর্বোচ্চ টোকেন সংখ্যা। কথোপকথন বাড়ার সাথে সাথে আপনাকে কৌশল পান যাতে প্রাসঙ্গিক প্রসঙ্গ বজায় থাকে ছাড়াই সীমা ছাড়িয়ে না যায়। এই মডিউলটি আপনাকে স্মৃতি কিভাবে কাজ করে দেখায়; পরে আপনি শিখবেন কখন সারসংক্ষেপ করতে হয়, কখন ভুলে যেতে হয়, এবং কখন পুনরুদ্ধার করতে হয়।

## পরবর্তী ধাপ

**পরবর্তী মডিউল:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**নেভিগেশন:** [← পূর্ববর্তী: মডিউল 01 - পরিচিতি](../01-introduction/README.md) | [মূল পৃষ্ঠায় ফিরে যান](../README.md) | [পরবর্তী: মডিউল 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**দায়ভার বিবৃতি**:  
এই ডকুমেন্টটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংচালিত অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল ডকুমেন্টের নিজস্ব ভাষা হলো কর্তৃত্বপূর্ণ উত্স হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানুষ দ্বারা অনুবাদ করানোই সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোন ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->