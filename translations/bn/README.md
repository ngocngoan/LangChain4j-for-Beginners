<img src="../../translated_images/bn/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 বহুভাষিক সমর্থন

#### GitHub Action এর মাধ্যমে সমর্থিত (স্বয়ংক্রিয় ও সর্বদা আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](./README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **স্থানীয়ভাবে ক্লোন করতে চান?**
>
> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ রয়েছে যা ডাউনলোডের পরিমাণ উল্লেখযোগ্যভাবে বৃদ্ধি করে। অনুবাদ ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:
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
> এটি আপনাকে দ্রুত ডাউনলোড সহ কোর্স সম্পন্ন করার জন্য প্রয়োজনীয় সবকিছু দিবে।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j নবীনদের জন্য

LangChain4j ও Azure OpenAI GPT-5.2 দিয়ে AI অ্যাপ্লিকেশন তৈরির একটি কোর্স, মৌলিক চ্যাট থেকে AI এজেন্ট পর্যন্ত।

**LangChain4j তে নতুন?** মূল শব্দ ও ধারণাগুলোর সংজ্ঞার জন্য [Glossary](docs/GLOSSARY.md) দেখুন।

## বিষয়সূচি

1. [দ্রুত শুরু](00-quick-start/README.md) - LangChain4j দিয়ে শুরু করুন
2. [পরিচয়](01-introduction/README.md) - LangChain4j এর মৌলিক বিষয় শিখুন
3. [প্রম্পট ইঞ্জিনিয়ারিং](02-prompt-engineering/README.md) - কার্যকর প্রম্পট ডিজাইন আয়ত্ত করুন
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - বুদ্ধিমান জ্ঞানভিত্তিক সিস্টেম তৈরি করুন
5. [টুলস](04-tools/README.md) - বাহ্যিক টুল এবং সাধারণ সহায়ক একীভূত করুন
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) এবং Agentic মডিউল নিয়ে কাজ করুন
---

## শেখার পথ

> **দ্রুত শুরু**

1. এই রিপোজিটরিটি আপনার GitHub একাউন্টে fork করুন
2. ক্লিক করুন **Code** → **Codespaces** ট্যাব → **...** → **New with options...**
3. ডিফল্ট ব্যবহার করুন – এটি কোর্সের জন্য তৈরি ডেভেলপমেন্ট কনটেইনার নির্বাচন করবে
4. ক্লিক করুন **Create codespace**
5. পরিবেশ প্রস্তুতির জন্য ৫-১০ মিনিট অপেক্ষা করুন
6. সরাসরি [দ্রুত শুরু](./00-quick-start/README.md) তে যান শুরু করতে!

মডিউল সম্পন্ন করার পর, LangChain4j পরীক্ষার ধারণাগুলো দেখতে [Testing Guide](docs/TESTING.md) অন্বেষণ করুন।

> **নোট:** এই প্রশিক্ষণে GitHub Models এবং Azure OpenAI উভয়ই ব্যবহৃত হয়। [দ্রুত শুরু](00-quick-start/README.md) মডিউল GitHub Models ব্যবহার করে (Azure সাবস্ক্রিপশন দরকার নেই), যখন ১-৫ মডিউল Azure OpenAI ব্যবহার করে। আপনার যদি না থাকে তবে একটি [বিনামূল্যের Azure অ্যাকাউন্ট](https://aka.ms/azure-free-account) দিয়ে শুরু করুন।


## GitHub Copilot নিয়ে শেখা

দ্রুত কোড লিখতে, GitHub Codespace বা স্থানীয় IDE এ devcontainer সহ এই প্রকল্পটি খুলুন। এই কোর্সে ব্যবহৃত devcontainer পূর্বে GitHub Copilot দিয়ে কনফিগার করা হয়েছে AI যুগল প্রোগ্রামিংয়ের জন্য।

প্রতিটি কোড উদাহরণে GitHub Copilot কে জিজ্ঞাসা করার জন্য প্রস্তাবিত প্রশ্ন রয়েছে যা আপনার বুঝ আরও গভীর করবে। 💡/🤖 প্রম্পটগুলো অনুসন্ধান করুন:

- **Java ফাইলের হেডার** - প্রতিটি উদাহরণের জন্য নির্দিষ্ট প্রশ্ন
- **মডিউল README** - কোড উদাহরণের পরে অনুসন্ধান প্রম্পট

**কিভাবে ব্যবহার করবেন:** যেকোনো কোড ফাইল খুলুন এবং Copilot কে প্রস্তাবিত প্রশ্ন করুন। এটি সম্পূর্ণ কোডবেসের প্রসঙ্গ ধারণ করে এবং ব্যাখ্যা, সম্প্রসারণ ও বিকল্প প্রস্তাব করতে পারে।

আরও জানতে চান? দেখুন [AI যুগল প্রোগ্রামিংয়ের জন্য Copilot](https://aka.ms/GitHubCopilotAI)।

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
 
### মূল শেখা
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![শিক্ষানবিসদের জন্য IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![শিক্ষানবিসদের জন্য XR ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কপাইলট সিরিজ
[![এআই পেয়ারড প্রোগ্রামিংয়ের জন্য কপাইলট](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET এর জন্য কপাইলট](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![কপাইলট অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সাহায্য পান

আপনি যদি আটকে যান বা AI অ্যাপ তৈরি করার বিষয়ে কোনও প্রশ্ন থাকে, তাহলে যোগ দিন:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

পণ্য প্রতিক্রিয়া বা ত্রুটি থাকলে নিম্নলিখিত স্থানটি পরিদর্শন করুন:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## লাইসেন্স

MIT লাইসেন্স - বিস্তারিত দেখুন [LICENSE](../../LICENSE) ফাইলে।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকার**:  
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা যথাসাধ্য সঠিকতার চেষ্টা করি, তবুও স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি এর মূল ভাষায়ই কর্তৃত্বপূর্ণ উৎস হিসাবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারে সৃষ্ট কোনো ভুলবোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->