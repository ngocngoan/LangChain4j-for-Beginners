<img src="../../translated_images/bn/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# নবীনদের জন্য LangChain4j

LangChain4j এবং Azure OpenAI GPT-5.2 দিয়ে AI অ্যাপ্লিকেশন তৈরি করার একটি কোর্স, প্রাথমিক চ্যাট থেকে AI এজেন্ট পর্যন্ত।

### 🌐 বহু-ভাষা সমর্থন

#### GitHub Action এর মাধ্যমে সমর্থিত (স্বয়ংক্রিয় ও সর্বদা আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](./README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **লোকালি ক্লোন করতে চান?**
>
> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ রয়েছে যা ডাউনলোড সাইজ অনেক বড় করে তোলে। অনুবাদ ছাড়া ক্লোন করতে, sparse checkout ব্যবহার করুন:
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
> এটি আপনাকে কোর্সটি সম্পন্ন করতে প্রয়োজনীয় সমস্ত কিছু দ্রুত ডাউনলোড করতে দেয়।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## বিষয়বস্তু সূচি

1. [দ্রুত শুরু](00-quick-start/README.md) - LangChain4j দিয়ে শুরু করুন
2. [পরিচিতি](01-introduction/README.md) - LangChain4j এর মূল বিষয় শিখুন
3. [প্রম্পট ইঞ্জিনিয়ারিং](02-prompt-engineering/README.md) - কার্যকর প্রম্পট ডিজাইন মাস্টার করুন
4. [RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](03-rag/README.md) - বুদ্ধিমান জ্ঞানভিত্তিক ব্যবস্থা তৈরি করুন
5. [টুলস](04-tools/README.md) - বাহ্যিক টুলস এবং সরল সহকারী সংহত করুন
6. [MCP (মডেল কনটেক্সট প্রোটোকল)](05-mcp/README.md) - মডেল কনটেক্সট প্রোটোকল (MCP) এবং এজেন্টিক মডিউল নিয়ে কাজ করুন

### ভিডিও ওয়াকথ্রু

প্রতিটি মডিউলের সাথে একটি সঙ্গী লাইভ সেশন থাকে যেখানে আমরা ধাপে ধাপে ধারণা এবং কোড একত্রে প্রদর্শন করি।

| মডিউল | ভিডিও |
|--------|-------|
| ০১ - পরিচিতি | [LangChain4j দিয়ে শুরু করা](https://www.youtube.com/live/nl_troDm8rQ) |
| ০২ - প্রম্পট ইঞ্জিনিয়ারিং | [LangChain4j সাথে প্রম্পট ইঞ্জিনিয়ারিং](https://www.youtube.com/live/PJ6aBaE6bog) |
| ০৩ - RAG | [LangChain4j এর মাধ্যমে RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| ০৪ - টুলস ও ০৫ - MCP | [টুলস এবং MCP সহ AI এজেন্ট](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## শিক্ষার পথ

**LangChain4j তে নতুন?** মূল শব্দ এবং ধারণার সংজ্ঞার জন্য [Glossary](docs/GLOSSARY.md) দেখুন।

> **দ্রুত শুরু**

১. এই রিপোজিটরিটি আপনার GitHub অ্যাকাউন্টে fork করুন  
২. **Code** → **Codespaces** ট্যাবে ক্লিক করুন → **...** → **নতুন অপশনসহ...** নির্বাচন করুন  
৩. ডিফল্ট ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি ডেভেলপমেন্ট কনটেইনার বেছে নেবে  
৪. **Create codespace** ক্লিক করুন  
৫. পরিবেশ প্রস্তুত হতে ৫-১০ মিনিট অপেক্ষা করুন  
৬. সরাসরি [দ্রুত শুরু](./00-quick-start/README.md) এ যান শুরু করার জন্য!

মডিউলগুলি সম্পন্ন করার পরে, LangChain4j টেস্টিং ধারণা বাস্তবায়নের জন্য [Testing Guide](docs/TESTING.md) দেখুন।

> **দ্রষ্টব্য:** এই প্রশিক্ষণে GitHub মডেল এবং Azure OpenAI উভয়ই ব্যবহার করা হয়। [দ্রুত শুরু](00-quick-start/README.md) মডিউল GitHub মডেল ব্যবহার করে (Azure সাবস্ক্রিপশন প্রয়োজন নেই), যখন মডিউল ১-৫ Azure OpenAI ব্যবহার করে। যদি আপনার না থাকে তবে [FREE Azure অ্যাকাউন্ট](https://aka.ms/azure-free-account) দিয়ে শুরু করুন।


## GitHub Copilot এর সাথে শিক্ষালাভ

দ্রুত কোডিং শুরু করতে, GitHub Codespace বা আপনার স্থানীয় IDE-তে এই প্রকল্পটি devcontainer সহ খুলুন। এই কোর্সের devcontainer ইতোমধ্যে GitHub Copilot নিয়ে AI পেয়ার প্রোগ্রামিং এর জন্য প্রস্তুত করা হয়েছে।

প্রতিটি কোড উদাহরণে GitHub Copilot কে জিজ্ঞাসা করার জন্য প্রস্তাবিত প্রশ্ন অন্তর্ভুক্ত আছে যা আপনার বোঝাপড়া গভীর করবে। 💡/🤖 প্রবণতা জন্য খুঁজুন:

- **জাভা ফাইল হেডার** - প্রতিটি উদাহরণের জন্য নির্দিষ্ট প্রশ্ন  
- **মডিউল README** - কোড উদাহরণের পরে অনুসন্ধানের প্রশ্ন

**কিভাবে ব্যবহার করবেন:** যেকোন কোড ফাইল খুলুন এবং Copilot কে প্রস্তাবিত প্রশ্ন করুন। এটি সম্পূর্ণ কোডবেসের প্রেক্ষাপট ধরে রেখেছে এবং ব্যাখ্যা, সম্প্রসারণ এবং বিকল্প প্রস্তাব করতে পারে।

আরও জানতে চান? [কোপাইলট ফর AI পেয়ার প্রোগ্রামিং](https://aka.ms/GitHubCopilotAI) দেখুন।


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
[![শুরুকারীদের জন্য সাইবারসিকিউরিটি](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![শুরুকারীদের জন্য ওয়েব ডেভেলপমেন্ট](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুকারীদের জন্য ইওটি](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুকারীদের জন্য এক্সআর ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### কোপাইলট সিরিজ
[![কোপাইলট ফর এআই-পেয়ার্ড প্রোগ্রামিং](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![কোপাইলট ফর C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![কোপাইলট অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সাহায্য গ্রহণ

যদি আপনি আটকে যান বা AI অ্যাপ নির্মাণ সম্পর্কে কোন প্রশ্ন থাকে, তাহলে যোগ দিন:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

যদি আপনি প্রোডাক্ট ফিডব্যাক বা ত্রুটি সম্পর্কে জানাতে চান, তাহলে যান:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## লাইসেন্স

MIT লাইসেন্স - বিস্তারিত জানতে দেখুন [LICENSE](../../LICENSE) ফাইল।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:  
এই নথি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা সঠিকতার জন্য চেষ্টা করি, তবে অনুগ্রহ করে জানুন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। প্রকৃত নথিটি তার নিজ ভাষায় প্রাধান্যপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানুষের দ্বারা করা অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে সৃষ্ট কোনো ভুল ধারণা বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->