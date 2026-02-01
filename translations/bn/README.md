<img src="../../translated_images/bn/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 বহুভাষিক সমর্থন

#### GitHub Action দ্বারা সমর্থিত (স্বয়ংক্রিয় ও সবসময় আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[আরবি](../ar/README.md) | [বাংলা](./README.md) | [বুলগেরিয়ান](../bg/README.md) | [বার্মিজ (মায়ানমার)](../my/README.md) | [চীনা (সরলীকৃত)](../zh-CN/README.md) | [চীনা (প্রথাগত, হংকং)](../zh-HK/README.md) | [চীনা (প্রথাগত, ম্যাকাও)](../zh-MO/README.md) | [চীনা (প্রথাগত, তাইওয়ান)](../zh-TW/README.md) | [ক্রোয়েশিয়ান](../hr/README.md) | [চেক](../cs/README.md) | [ড্যানিশ](../da/README.md) | [ডাচ](../nl/README.md) | [এস্তোনীয়](../et/README.md) | [ফিনিশ](../fi/README.md) | [ফরাসি](../fr/README.md) | [জার্মান](../de/README.md) | [গ্রিক](../el/README.md) | [হিব্রু](../he/README.md) | [হিন্দি](../hi/README.md) | [হাঙ্গেরিয়ান](../hu/README.md) | [ইন্দোনেশিয়ান](../id/README.md) | [ইতালীয়](../it/README.md) | [জাপানি](../ja/README.md) | [কান্নাডা](../kn/README.md) | [কোরিয়ান](../ko/README.md) | [লিথুয়ানিয়ান](../lt/README.md) | [মালয়](../ms/README.md) | [মালায়ালাম](../ml/README.md) | [মারাঠি](../mr/README.md) | [নেপালি](../ne/README.md) | [নাইজেরিয়ান পিজিন](../pcm/README.md) | [নরওয়েজিয়ান](../no/README.md) | [ফার্সি (পার্থীয়)](../fa/README.md) | [পোলিশ](../pl/README.md) | [পর্তুগিজ (ব্রাজিল)](../pt-BR/README.md) | [পর্তুগিজ (পর্তুগাল)](../pt-PT/README.md) | [পাঞ্জাবি (গুরুমুখী)](../pa/README.md) | [রোমানিয়ান](../ro/README.md) | [রাশিয়ান](../ru/README.md) | [সার্বিয়ান (সিরিলিক)](../sr/README.md) | [স্লোভাক](../sk/README.md) | [স্লোভেনিয়ান](../sl/README.md) | [স্প্যানিশ](../es/README.md) | [সোয়াহিলি](../sw/README.md) | [সুইডিশ](../sv/README.md) | [টাগালোগ (ফিলিপিনো)](../tl/README.md) | [তামিল](../ta/README.md) | [তেলুগু](../te/README.md) | [থাই](../th/README.md) | [তুর্কি](../tr/README.md) | [উক্রেনিয়ান](../uk/README.md) | [উর্দু](../ur/README.md) | [ভিয়েতনামী](../vi/README.md)

> **স্থানীয়ভাবে ক্লোন করতে চান?**

> এই রেপোজিটরিতে ৫০+ ভাষার অনুবাদ অন্তর্ভুক্ত রয়েছে যা ডাউনলোড সাইজ উল্লেখযোগ্যভাবে বৃদ্ধি করে। অনুবাদ ছাড়া ক্লোন করতে, ব্যবহার করুন sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> এটি আপনাকে দ্রুত ডাউনলোডের মাধ্যমে কোর্স সম্পন্ন করার জন্য প্রয়োজনীয় সবকিছু প্রদান করবে।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j শিক্ষানবিশদের জন্য

LangChain4j এবং Azure OpenAI GPT-5 দিয়ে AI অ্যাপ্লিকেশন তৈরি করার জন্য একটি কোর্স, বেসিক চ্যাট থেকে শুরু করে AI এজেন্ট পর্যন্ত।

**LangChain4j তে নতুন?** গুরুত্বপূর্ণ শব্দ ও ধারণাগুলির সংজ্ঞার জন্য [Glossary](docs/GLOSSARY.md) দেখুন।

## বিষয়সূচি

1. [দ্রুত শুরু](00-quick-start/README.md) - LangChain4j দিয়ে শুরু করুন
2. [পরিচিতি](01-introduction/README.md) - LangChain4j এর মৌলিক বিষয় শিখুন
3. [প্রম্পট ইঞ্জিনিয়ারিং](02-prompt-engineering/README.md) - কার্যকর প্রম্পট ডিজাইনে দক্ষতা অর্জন করুন
4. [RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](03-rag/README.md) - বুদ্ধিমান জ্ঞানের ভিত্তিক সিস্টেম তৈরি করুন
5. [টুলস](04-tools/README.md) - বাহ্যিক টুল ও সরল সহকারী সংযুক্ত করুন
6. [MCP (মডেল প্রসঙ্গ প্রোটোকল)](05-mcp/README.md) - Model Context Protocol (MCP) ও Agentic মডিউল নিয়ে কাজ করুন
---

## শেখার পথ

> **দ্রুত শুরু**

1. এই রেপোজিটরিটি আপনার GitHub অ্যাকাউন্টে Fork করুন
2. ক্লিক করুন **Code** → **Codespaces** ট্যাব → **...** → **New with options...**
3. ডিফল্ট ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি Development container নির্বাচন করবে
4. ক্লিক করুন **Create codespace**
5. পরিবেশ প্রস্তুত হতে ৫-১০ মিনিট অপেক্ষা করুন
6. শুরু করতে সরাসরি [Quick Start](./00-quick-start/README.md) এ যান!

মডিউলগুলো সম্পন্ন করার পরে, LangChain4j এর টেস্টিং ধারণাগুলো দেখতে [Testing Guide](docs/TESTING.md) অনুসন্ধান করুন।

> **নোট:** এই প্রশিক্ষণে GitHub Models এবং Azure OpenAI দুইটিই ব্যবহার হয়। [Quick Start](00-quick-start/README.md) মডিউল GitHub Models ব্যবহার করে (কোন Azure সাবস্ক্রিপশন প্রয়োজন নেই), আর ১-৫ মডিউল Azure OpenAI ব্যবহার করে। যদি না থাকে, তাহলে একটি [FREE Azure অ্যাকাউন্ট](https://aka.ms/azure-free-account) নিয়ে শুরু করুন।


## GitHub Copilot সহ শেখা

দ্রুত কোডিং শুরু করতে, এই প্রজেক্টটি GitHub Codespace বা আপনার লোকাল IDE তে devcontainer সহ খুলুন। এই কোর্সে ব্যবহার হওয়া devcontainer GitHub Copilot সহ প্রাক-কনফিগার করা যা AI পেয়ার প্রোগ্রামিংয়ের জন্য।

প্রতিটি কোড উদাহরণে GitHub Copilot কে জিজ্ঞাসা করার জন্য প্রস্তাবিত প্রশ্ন রয়েছে যা আপনার ধারণা গভীর করবে। 💡/🤖 ইঙ্গিতগুলি খুঁজুন:

- **Java ফাইল হেডার** - প্রতিটি উদাহরণের নির্দিষ্ট প্রশ্ন
- **মডিউল README** - কোড উদাহরণের পর অনুসন্ধানমূলক প্রম্পট

**কিভাবে ব্যবহার করবেন:** যেকোনো কোড ফাইল খুলুন এবং Copilot কে প্রস্তাবিত প্রশ্ন করুন। এটি কোডবেসের সম্পূর্ণ প্রসঙ্গ জানে এবং ব্যাখ্যা করতে, বাড়াতে এবং বিকল্প প্রস্তাব করতে পারে।

আরও জানতে চান? দেখুন [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)।

## অতিরিক্ত সম্পদ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

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
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কোপাইলট সিরিজ
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সাহায্য পাওয়া

যদি আপনি আটকে যান বা AI অ্যাপ তৈরি সম্পর্কে কোনো প্রশ্ন থাকে, যোগ দিন:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

যদি আপনার পণ্য প্রতিক্রিয়া বা ত্রুটি থাকে তৈরি করার সময়, যান:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## লাইসেন্স

MIT লাইসেন্স - বিস্তারিত জানতে দেখুন [LICENSE](../../LICENSE) ফাইল।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**বর্জনমূলক বক্তব্য**:  
এই ডকুমেন্টটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতা নিশ্চিত করার চেষ্টা করি, কিন্তু দয়া করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার নিজ নিজ ভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে গণ্য করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ পরামর্শ দেওয়া হয়। এই অনুবাদ ব্যবহারের কারণে কোনো বিভ্রান্তি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->