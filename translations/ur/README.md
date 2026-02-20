<img src="../../translated_images/ur/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ابتدائیوں کے لیے

LangChain4j اور Azure OpenAI GPT-5.2 کے ساتھ AI ایپلیکیشنز بنانے کا ایک کورس، بنیادی چیٹ سے لے کر AI ایجنٹس تک۔

### 🌐 کثیراللسانی سپورٹ

#### GitHub Action کے ذریعے سپورٹ یافتہ (خودکار اور ہمیشہ تازہ ترین)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](./README.md) | [Vietnamese](../vi/README.md)

> **مقامی طور پر کلون کرنا پسند کریں؟**
>
> اس ریپوزیٹری میں 50+ زبانوں کے تراجم شامل ہیں جو ڈاؤن لوڈ کا سائز کافی بڑھاتے ہیں۔ بغیر تراجم کے کلون کرنے کے لیے sparse checkout استعمال کریں:
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
> اس سے آپ کو کورس مکمل کرنے کے لیے تمام ضرورتیں مل جاتی ہیں اور ڈاؤن لوڈ بھی تیز ہوتا ہے۔
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## فہرست مواد

1. [فوری آغاز](00-quick-start/README.md) - LangChain4j کے ساتھ شروعات کریں
2. [تعارف](01-introduction/README.md) - LangChain4j کی بنیادی باتیں سیکھیں
3. [پرومپٹ انجینیئرنگ](02-prompt-engineering/README.md) - مؤثر پرومپٹ ڈیزائن میں مہارت حاصل کریں
4. [RAG (ریٹریول-آگمینٹڈ جنریشن)](03-rag/README.md) - ذہین علمی نظام بنائیں
5. [ٹولز](04-tools/README.md) - بیرونی ٹولز اور سادہ اسسٹنٹس کو ضم کریں
6. [MCP (ماڈل کانٹیکسٹ پروٹوکول)](05-mcp/README.md) - ماڈل کانٹیکسٹ پروٹوکول (MCP) اور ایجنٹک ماڈیولز کے ساتھ کام کریں
---

## تعلیمی راستہ

**LangChain4j میں نئے ہیں؟** کلیدی اصطلاحات اور تصورات کی تعریفوں کے لیے [Glossary](docs/GLOSSARY.md) دیکھیں۔

> **فوری آغاز**

1. اس ریپوزیٹری کو اپنے GitHub اکاؤنٹ پر فورک کریں
2. **Code** → **Codespaces** ٹیب → **...** → **New with options...** پر کلک کریں
3. ڈیفالٹس استعمال کریں – یہ اس کورس کے لیے بنائے گئے ڈیولپمنٹ کنٹینر کو منتخب کرے گا
4. **Create codespace** پر کلک کریں
5. ماحول کے تیار ہونے کے لیے 5-10 منٹ انتظار کریں
6. فوراً شروع کرنے کے لیے [Quick Start](./00-quick-start/README.md) پر جائیں!

ماڈیول مکمل کرنے کے بعد، LangChain4j کی ٹیسٹنگ کے تصورات کو عملی طور پر دیکھنے کے لیے [Testing Guide](docs/TESTING.md) دریافت کریں۔

> **نوٹ:** یہ تربیت GitHub Models اور Azure OpenAI دونوں استعمال کرتی ہے۔ [Quick Start](00-quick-start/README.md) ماڈیول GitHub Models استعمال کرتا ہے (Azure سبسکرپشن کی ضرورت نہیں)، جبکہ ماڈیولز 1-5 Azure OpenAI استعمال کرتے ہیں۔ اگر آپ کے پاس نہیں ہے تو [مفت Azure اکاؤنٹ](https://aka.ms/azure-free-account) سے شروع کریں۔


## GitHub Copilot کے ساتھ تعلیم

فوری طور پر کوڈنگ شروع کرنے کے لیے، اس پروجیکٹ کو GitHub Codespace میں یا اپنے لوکل IDE میں دی گئی devcontainer کے ساتھ کھولیں۔ اس کورس میں استعمال ہونے والا devcontainer پہلے سے GitHub Copilot کے ساتھ AI جوڑی پروگرامنگ کے لیے ترتیب دیا گیا ہے۔

ہر کوڈ مثال میں ایسے سوالات شامل ہیں جنہیں آپ GitHub Copilot سے پوچھ سکتے ہیں تاکہ اپنی سمجھ بوجھ گہری کریں۔ 💡/🤖 پرامپٹ کے لیے دیکھیں:

- **Java فائل ہیڈرز** - ہر مثال کے مخصوص سوالات
- **ماڈیول README فائلز** - کوڈ کی مثالوں کے بعد دریافت کے سوالات

**استعمال کا طریقہ:** کوئی بھی کوڈ فائل کھولیں اور Copilot سے تجویز کردہ سوالات پوچھیں۔ اسے کوڈبیس کا مکمل سیاق و سباق معلوم ہے اور یہ وضاحت، توسیع اور متبادل تجویز کر سکتا ہے۔

مزید جاننا چاہتے ہیں؟ [Copilot برائے AI جوڑی پروگرامنگ](https://aka.ms/GitHubCopilotAI) دیکھیں۔


## اضافی وسائل

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
 
### جنریٹیو AI سیریز
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### بنیادی تعلیم
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### کوپائلٹ سیریز
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## مدد حاصل کریں

اگر آپ پھنس جائیں یا AI ایپس بنانے کے بارے میں کوئی سوال ہو، تو شامل ہوں:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

اگر آپ کو پروڈکٹ فیڈ بیک یا تعمیر کے دوران کوئی غلطی ہو تو یہاں تشریف لائیں:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## لائسنس

MIT لائسنس - تفصیلات کے لیے [LICENSE](../../LICENSE) فائل دیکھیں۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**دستخطی اعلان**:  
یہ دستاویز AI ترجمہ خدمات [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم نوٹ کریں کہ خودکار تراجم میں غلطیاں یا کمی بیشی ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں معتبر ماخذ سمجھی جائے گی۔ اہم معلومات کے لیے پیشہ ورانہ انسانی ترجمہ تجویز کیا جاتا ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم پر نہیں ہوگی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->