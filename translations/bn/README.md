<img src="../../translated_images/bn/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j শিক্ষুকদের জন্য

LangChain4j এবং Azure OpenAI GPT-5.2 দিয়ে AI অ্যাপ্লিকেশন তৈরি করার একটি কোর্স, মূল কথোপকথন থেকে AI এজেন্ট পর্যন্ত।

### 🌐 বহুভাষিক সমর্থন

#### গিটহাব অ্যাকশনের মাধ্যমে সমর্থিত (স্বয়ংক্রিয় এবং সর্বদা আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[আরবি](../ar/README.md) | [বাংলা](./README.md) | [বুলগেরিয়ান](../bg/README.md) | [বার্মিজ (মায়ানমার)](../my/README.md) | [চীনা (সরলীকৃত)](../zh-CN/README.md) | [চীনা (প্রচলিত, হংকং)](../zh-HK/README.md) | [চীনা (প্রচলিত, ম্যাকাউ)](../zh-MO/README.md) | [চীনা (প্রচলিত, তাইওয়ান)](../zh-TW/README.md) | [ক্রোয়েশিয়ান](../hr/README.md) | [চেক](../cs/README.md) | [ড্যানিশ](../da/README.md) | [ডাচ](../nl/README.md) | [এস্তোনীয়](../et/README.md) | [ফিনিশ](../fi/README.md) | [ফরাসি](../fr/README.md) | [জার্মান](../de/README.md) | [গ্রীক](../el/README.md) | [হিব্রু](../he/README.md) | [হিন্দি](../hi/README.md) | [হাঙ্গেরিয়ান](../hu/README.md) | [ইন্দোনেশিয়ান](../id/README.md) | [ইতালিয়ান](../it/README.md) | [জাপানি](../ja/README.md) | [কন্নড়](../kn/README.md) | [কোরিয়ান](../ko/README.md) | [লিথুয়ানিয়ান](../lt/README.md) | [মালয়](../ms/README.md) | [মালয়ালাম](../ml/README.md) | [মারাঠি](../mr/README.md) | [নেপালি](../ne/README.md) | [নাইজেরিয়ান পিজিন](../pcm/README.md) | [নরওয়েজিয়ান](../no/README.md) | [ফার্সি (পারস্য)](../fa/README.md) | [পোলিশ](../pl/README.md) | [পর্তুগিজ (ব্রাজিল)](../pt-BR/README.md) | [পর্তুগিজ (পর্তুগাল)](../pt-PT/README.md) | [পাঞ্জাবি (গুরুমুখী)](../pa/README.md) | [রোমানিয়ান](../ro/README.md) | [রাশিয়ান](../ru/README.md) | [সার্বিয়ান (সিরিলিক)](../sr/README.md) | [স্লোভাক](../sk/README.md) | [স্লোভেনিয়ান](../sl/README.md) | [স্পেনীয়](../es/README.md) | [সোয়াহিলি](../sw/README.md) | [সুইডিশ](../sv/README.md) | [তাগালগ (ফিলিপিনো)](../tl/README.md) | [তামিল](../ta/README.md) | [তেলুগু](../te/README.md) | [থাই](../th/README.md) | [তুর্কী](../tr/README.md) | [ইউক্রেনীয়](../uk/README.md) | [উর্দু](../ur/README.md) | [ভিয়েতনামী](../vi/README.md)

> **স্থানীয়ভাবে ক্লোন করতে পছন্দ করেন?**
>
> এই রেপোজিটরিতে ৫০টিরও বেশি ভাষার অনুবাদ রয়েছে যা ডাউনলোড সাইজ অনেক বাড়ায়। অনুবাদ ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:
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
> এটি আপনাকে কোর্স সম্পন্ন করার জন্য সবকিছু দ্রুত ডাউনলোড করতে দেয়।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## বিষয়সূচী

1. [দ্রুত শুরু](00-quick-start/README.md) - LangChain4j দিয়ে শুরু করুন
2. [পরিচয়](01-introduction/README.md) - LangChain4j এর মৌলিক বিষয় শিখুন
3. [প্রম্পট ইঞ্জিনিয়ারিং](02-prompt-engineering/README.md) - কার্যকর প্রম্পট ডিজাইন মাস্টার করুন
4. [RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](03-rag/README.md) - বুদ্ধিমান জ্ঞানভিত্তিক সিস্টেম গঠন করুন
5. [টুলস](04-tools/README.md) - বহিরাগত টুলস এবং সরল সহকারীরা একত্রিত করুন
6. [MCP (মডেল কনটেক্সট প্রোটোকল)](05-mcp/README.md) - মডেল কনটেক্সট প্রোটোকল (MCP) এবং-Agentic মডিউল নিয়ে কাজ করুন

### ভিডিও ওয়াকথ্রুজ

প্রত্যেক মডিউলের জন্য একটি সম্পৃক্ত লাইভ সেশন রয়েছে যেখানে আমরা ধাপে ধাপে ধারণা এবং কোড ব্যাখ্যা করি।

| মডিউল | ভিডিও |
|--------|-------|
| ০১ - পরিচয় | [LangChain4j দিয়ে শুরু করা](https://www.youtube.com/live/nl_troDm8rQ) |
| ০২ - প্রম্পট ইঞ্জিনিয়ারিং | [LangChain4j দিয়ে প্রম্পট ইঞ্জিনিয়ারিং](https://www.youtube.com/live/PJ6aBaE6bog) |
| ০৩ - RAG | [LangChain4j দিয়ে RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| ০৪ - টুলস & ০৫ - MCP | [টুলস এবং MCP সহ AI এজেন্টস](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## শিক্ষার পথ

**LangChain4j এ নতুন?** মূল শব্দ ও ধারণার সংজ্ঞার জন্য [শব্দকোষ](docs/GLOSSARY.md) দেখুন।

> **দ্রুত শুরু**

১. এই রেপোজিটরি আপনার গিটহাব অ্যাকাউন্টে ফর্ক করুন  
২. ক্লিক করুন **Code** → **Codespaces** ট্যাব → **...** → **New with options...**  
৩. ডিফল্ট গুলো ব্যবহার করুন – এটি এই কোর্সের জন্য নির্মিত ডেভেলপমেন্ট কন্টেইনার নির্বাচন করবে  
৪. ক্লিক করুন **Create codespace**  
৫. পরিবেশ প্রস্তুতির জন্য ৫-১০ মিনিট অপেক্ষা করুন  
৬. সরাসরি [দ্রুত শুরু](./00-quick-start/README.md) এ যান এবং শুরু করুন!

মডিউলগুলি শেষ করার পরে, LangChain4j পরীক্ষণের ধারণাগুলি কার্যকরভাবে দেখতে [পরীক্ষণ গাইড](docs/TESTING.md) এক্সপ্লোর করুন।

> **দ্রষ্টব্য:** এই প্রশিক্ষণে GitHub মডেল এবং Azure OpenAI দুইই ব্যবহার করা হয়। [দ্রুত শুরু](00-quick-start/README.md) মডিউলে GitHub মডেল ব্যবহার করা হয় (কোনো Azure সাবস্ক্রিপশন প্রয়োজন নেই), যখন মডিউল ১-৫ Azure OpenAI ব্যবহার করে। আপনার যদি না থাকে তাহলে একটি [বিনামূল্যের Azure অ্যাকাউন্ট](https://aka.ms/azure-free-account) দিয়ে শুরু করুন।


## GitHub Copilot সহ শেখা

দ্রুত কোডিং শুরু করতে, এই প্রজেক্টটি GitHub Codespace অথবা আপনার স্থানীয় IDE তে devcontainer ব্যবহার করে খুলুন। এই কোর্সে ব্যবহৃত devcontainer আগেই GitHub Copilot AI পেয়ার প্রোগ্রামিংয়ের জন্য কনফিগার করা আছে।

প্রতিটি কোড উদাহরণে GitHub Copilot কে জিজ্ঞেস করার জন্য সাজেস্টেড প্রশ্ন রয়েছে যেন আপনি গভীরভাবে শিখতে পারেন। খুঁজে বের করুন 💡/🤖 প্রম্পটগুলো:

- **জাভা ফাইলের হেডারগুলোতে** - প্রতিটি উদাহরণের নির্দিষ্ট প্রশ্ন  
- **মডিউল README গুলোতে** - কোড উদাহরণের পরে অনুসন্ধান প্রম্পটগুলি

**কিভাবে ব্যবহার করবেন:** কোনো কোড ফাইল খুলুন এবং সাজেস্টেড প্রশ্নগুলো Copilot কে জিজ্ঞেস করুন। এটি পুরো কোডবেসের প্রেক্ষাপট বুঝে এবং ব্যাখ্যা, বর্ধিতকরণ ও বিকল্প পরামর্শ দিতে পারে।

আরও জানতে চান? দেখুন [AI পেয়ার প্রোগ্রামিংয়ের জন্য Copilot](https://aka.ms/GitHubCopilotAI)।

## অতিরিক্ত সম্পদ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j শিক্ষুকদের জন্য](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js শিক্ষুকদের জন্য](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain শিক্ষুকদের জন্য](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD শিক্ষুকদের জন্য](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI শিক্ষুকদের জন্য](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP শিক্ষুকদের জন্য](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents শিক্ষুকদের জন্য](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### জেনারেটিভ AI সিরিজ
[![জেনারেটিভ AI শিক্ষুকদের জন্য](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![জেনারেটিভ AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![জেনারেটিভ AI (জাভা)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![জেনারেটিভ AI (জাভাস্ক্রিপ্ট)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### মূল শিক্ষা
[![শুরুদের জন্য এমএল](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুদের জন্য ডাটা সায়েন্স](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুদের জন্য AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুর জন্য সাইবারসিকিউরিটি](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![শুরুর জন্য ওয়েব ডেভ](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুর জন্য আইওটি](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরুর জন্য এক্সআর ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কপাইলট সিরিজ
[![এআই পেয়ারড প্রোগ্রামিংয়ের জন্য কপাইলট](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET এর জন্য কপাইলট](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![কপাইলট অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সহায়তা প্রাপ্তি

যদি আপনি আটকে যান বা এআই অ্যাপ নির্মাণ সম্পর্কে কোনো প্রশ্ন থাকে, যোগ দিন:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

যদি আপনি পণ্য প্রতিক্রিয়া বা নির্মাণের সময় ত্রুটি দেখতে পান, ভিজিট করুন:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## লাইসেন্স

MIT লাইসেন্স - বিস্তারিত জানতে দেখুন [LICENSE](../../LICENSE) ফাইল।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকারোক্তি**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার জন্য চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজ ভাষায়ই একমাত্র বিশ্বস্ত উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুতর তথ্যের জন্য পেশাদার মানব অনুবাদ গ্রহণ করা উচিৎ। এই অনুবাদের ব্যবহারে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা কোনো দায়বদ্ধতা নিতে পারি না।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->