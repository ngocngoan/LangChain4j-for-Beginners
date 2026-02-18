<img src="../../translated_images/bn/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j শুরু করার জন্য

LangChain4j এবং Azure OpenAI GPT-5.2 দিয়ে AI অ্যাপ্লিকেশন তৈরির একটি কোর্স, মৌলিক চ্যাট থেকে AI এজেন্ট পর্যন্ত।

### 🌐 বহু-ভাষার সমর্থন

#### GitHub Action এর মাধ্যমে সমর্থিত (স্বয়ংক্রিয় ও সর্বদা আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](./README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **স্থানীয়ভাবে ক্লোন করতে চান?**
>
> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ রয়েছে যা ডাউনলোডের আকার উল্লেখযোগ্যভাবে বাড়ায়। অনুবাদ ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> এতে আপনি কোর্সটি সম্পন্ন করার জন্য প্রয়োজনীয় সব কিছু অনেক দ্রুত ডাউনলোডের মাধ্যমে পাবেন।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## বিষয় তালিকা

1. [দ্রুত শুরু](00-quick-start/README.md) - LangChain4j দিয়ে শুরু করুন
2. [পরিচিতি](01-introduction/README.md) - LangChain4j এর মৌলিক বিষয় শিখুন
3. [প্রম্পট ইঞ্জিনিয়ারিং](02-prompt-engineering/README.md) - কার্যকর প্রম্পট ডিজাইনে পারদর্শী হন
4. [RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](03-rag/README.md) - বুদ্ধিমান জ্ঞানভিত্তিক সিস্টেম তৈরি করুন
5. [টুলস](04-tools/README.md) - বহিরাগত টুলস এবং সরল সহকারী ইন্টিগ্রেট করুন
6. [MCP (মডেল কন্টেক্সট প্রোটোকল)](05-mcp/README.md) - মডেল কন্টেক্সট প্রোটোকল (MCP) এবং এজেন্টিক মডিউলের সাথে কাজ করুন
---

## শেখার পথ

**LangChain4j নতুন?** মূল টার্ম এবং ধারণাগুলোর সংজ্ঞার জন্য [Glossary](docs/GLOSSARY.md) দেখুন।

> **দ্রুত শুরু**

1. এই রিপোজিটরিটি আপনার GitHub অ্যাকাউন্টে Fork করুন
2. **Code** → **Codespaces** ট্যাবে ক্লিক করুন → **...** → **New with options...**
3. ডিফল্ট সেটিং ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি Development container নির্বাচন করবে
4. **Create codespace** এ ক্লিক করুন
5. পরিবেশ প্রস্তুতির জন্য ৫-১০ মিনিট অপেক্ষা করুন
6. সরাসরি [দ্রুত শুরু](./00-quick-start/README.md) এ যান এবং শুরু করুন!

মডিউল সম্পন্ন করার পর, LangChain4j পরীক্ষার ধারণাগুলো অ্যাকশনে দেখতে [Testing Guide](docs/TESTING.md) অনুসন্ধান করুন।

> **দ্রষ্টব্য:** এই প্রশিক্ষণে GitHub Models এবং Azure OpenAI দুটোই ব্যবহার করা হয়। [দ্রুত শুরু](00-quick-start/README.md) মডিউল GitHub Models ব্যবহার করে (Azure সাবস্ক্রিপশন প্রয়োজন নেই), আর মডিউল ১-৫ Azure OpenAI ব্যবহার করে। যদি আপনার অ্যাকাউন্ট না থাকে তাহলে একটি [FREE Azure অ্যাকাউন্ট](https://aka.ms/azure-free-account) দিয়ে শুরু করুন।


## GitHub Copilot দিয়ে শিখুন

দ্রুত কোডিং শুরু করতে, GitHub Codespace বা আপনার লোকাল IDE তে এই প্রজেক্টটি devcontainer সহ খুলুন। এই কোর্সে ব্যবহৃত devcontainer GitHub Copilot সহ প্রি-কনফিগার করা আছে AI পেয়ারড প্রোগ্রামিং এর জন্য।

প্রতিটি কোড উদাহরণে GitHub Copilot কে জিজ্ঞাসা করার জন্য প্রস্তাবিত প্রশ্নের তালিকা রয়েছে যাতে আপনার বোঝাপড়া বাড়ে। 💡/🤖 প্রম্পটগুলো খুঁজে দেখুন:

- **Java ফাইল হেডারগুলো** - প্রতিটি উদাহরণের জন্য নির্দিষ্ট প্রশ্ন
- **মডিউল README গুলো** - কোড উদাহরণের পর অনুসন্ধানমূলক প্রম্পট

**কিভাবে ব্যবহার করবেন:** কোন কোড ফাইল খুলুন এবং Copilot কে প্রস্তাবিত প্রশ্নগুলি জিজ্ঞাসা করুন। এর কাছে সম্পূর্ণ কোডবেসের প্রসঙ্গ আছে, এটি ব্যাখ্যা করতে, সম্প্রসারিত করতে এবং বিকল্প সাজেস্ট করতে পারে।

আরও জানতে চান? [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) দেখুন।


## অতিরিক্ত সম্পদ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI Series
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![শুরু করার জন্য আইওটি](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরু করার জন্য এক্সআর ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কপাইলট সিরিজ
[![কপাইলট ফর এআই পেয়ারড প্রোগ্রামিং](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![কপাইলট ফর C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![কপাইলট অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সাহায্য গ্রহণ

যদি আপনি আটকে যান বা AI অ্যাপ তৈরি করতে কোনো প্রশ্ন থাকে, যোগ দিন:

[![আজুর এআই ফাউন্ড্রি ডিসকাশন](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

যদি আপনার পণ্য প্রতিক্রিয়া বা বিল্ড করার সময় ত্রুটি থাকে, দেখুন:

[![আজুর এআই ফাউন্ড্রি ডেভেলপার ফোরাম](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## লাইসেন্স

MIT লাইসেন্স - বিস্তারিত জানতে দেখুন [LICENSE](../../LICENSE) ফাইল।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**দায়িত্ব ঘোষণা**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। যদিও আমরা সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজস্ব ভাষায়ই কর্তৃপক্ষের সূত্র হিসেবে গণ্য করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহার থেকে উদ্ভূত কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা কোনো দায়বদ্ধতা গ্রহণ করুম না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->