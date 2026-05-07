<img src="../../translated_images/ar/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j للمبتدئين

دورة لبناء تطبيقات الذكاء الاصطناعي باستخدام LangChain4j و Azure OpenAI GPT-5.2، من المحادثة الأساسية حتى الوكلاء الذكيين.

### 🌐 دعم متعدد اللغات

#### مدعوم عبر GitHub Action (آلي ودائم التحديث)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[العربية](./README.md) | [البنغالية](../bn/README.md) | [البلغارية](../bg/README.md) | [البورمية (ميانمار)](../my/README.md) | [الصينية (المبسطة)](../zh-CN/README.md) | [الصينية (التقليدية، هونغ كونغ)](../zh-HK/README.md) | [الصينية (التقليدية، ماكاو)](../zh-MO/README.md) | [الصينية (التقليدية، تايوان)](../zh-TW/README.md) | [الكرواتية](../hr/README.md) | [التشيكية](../cs/README.md) | [الدانماركية](../da/README.md) | [الهولندية](../nl/README.md) | [الإستونية](../et/README.md) | [الفنلندية](../fi/README.md) | [الفرنسية](../fr/README.md) | [الألمانية](../de/README.md) | [اليونانية](../el/README.md) | [العبرية](../he/README.md) | [الهندية](../hi/README.md) | [الهنغارية](../hu/README.md) | [الإندونيسية](../id/README.md) | [الإيطالية](../it/README.md) | [اليابانية](../ja/README.md) | [الكانادا](../kn/README.md) | [الخميرية](../km/README.md) | [الكورية](../ko/README.md) | [الليتوانية](../lt/README.md) | [الماليزية](../ms/README.md) | [المالايالامية](../ml/README.md) | [الماراثي](../mr/README.md) | [النيبالية](../ne/README.md) | [البيجنيه النيجيرية](../pcm/README.md) | [النرويجية](../no/README.md) | [الفارسية (اللغة الفارسية)](../fa/README.md) | [البولندية](../pl/README.md) | [البرتغالية (البرازيل)](../pt-BR/README.md) | [البرتغالية (البرتغال)](../pt-PT/README.md) | [البنجابية (جرمخي)](../pa/README.md) | [الرومانية](../ro/README.md) | [الروسية](../ru/README.md) | [الصربية (السيريلية)](../sr/README.md) | [السلوفاكية](../sk/README.md) | [السلوفينية](../sl/README.md) | [الإسبانية](../es/README.md) | [السواحيلية](../sw/README.md) | [السويدية](../sv/README.md) | [التاغالوغية (الفلبينية)](../tl/README.md) | [التاميل](../ta/README.md) | [التيلجو](../te/README.md) | [التايلاندية](../th/README.md) | [التركية](../tr/README.md) | [الأوكرانية](../uk/README.md) | [الأردية](../ur/README.md) | [الفيتنامية](../vi/README.md)

> **هل تفضل النسخ محليًا؟**
>
> يتضمن هذا المستودع أكثر من 50 ترجمة للغات تزيد بشكل كبير من حجم التنزيل. للنسخ بدون الترجمات، استخدم السحب الانتقائي:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (ويندوز):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> هذا يوفر لك كل ما تحتاجه لإكمال الدورة مع تنزيل أسرع بكثير.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## جدول المحتويات

1. [البدء السريع](00-quick-start/README.md) - ابدأ مع LangChain4j
2. [مقدمة](01-introduction/README.md) - تعلم أساسيات LangChain4j
3. [هندسة الموجهات](02-prompt-engineering/README.md) - إتقان تصميم الموجهات الفعالة
4. [RAG (التوليد المعزز بالاسترجاع)](03-rag/README.md) - بناء أنظمة ذكية مبنية على المعرفة
5. [الأدوات](04-tools/README.md) - دمج الأدوات الخارجية والمساعدين البسيطين
6. [MCP (بروتوكول سياق النموذج)](05-mcp/README.md) - العمل مع بروتوكول سياق النموذج (MCP) والوحدات الوكلائية

### جولات الفيديو التوضيحية

لكل وحدة جلسة مباشرة مصاحبة حيث نشرح المفاهيم والكود خطوة بخطوة.

| الوحدة | الفيديو |
|--------|-------|
| 01 - مقدمة | [البداية مع LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - هندسة الموجهات | [هندسة الموجهات مع LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG مع LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - الأدوات & 05 - MCP | [وكلاء الذكاء الاصطناعي مع الأدوات وMCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## مسار التعلم

**جديد في LangChain4j؟** اطلع على [مسرد المصطلحات](docs/GLOSSARY.md) لتعريفات المصطلحات والمفاهيم الأساسية.

> **البدء السريع**

1. قم بتفرع هذا المستودع إلى حسابك على GitHub
2. اضغط على **الكود** → تبويب **مساحات الشيفرة** → **...** → **جديد مع خيارات...**
3. استخدم الإعدادات الافتراضية – سيختار هذا حاوية التطوير التي تم إنشاؤها لهذه الدورة
4. اضغط **إنشاء مساحة الشيفرة**
5. انتظر من 5 إلى 10 دقائق حتى يصبح البيئة جاهزة
6. انتقل مباشرة إلى [البدء السريع](./00-quick-start/README.md) للبدء!

بعد إكمال الوحدات، استكشف [دليل الاختبار](docs/TESTING.md) لمشاهدة مفاهيم اختبارات LangChain4j قيد العمل.

> **ملاحظة:** يستخدم هذا التدريب نماذج GitHub ونماذج Azure OpenAI. يستخدم وحدة [البدء السريع](00-quick-start/README.md) نماذج GitHub (لا تحتاج إلى اشتراك Azure)، بينما تستخدم الوحدات 1-5 Azure OpenAI. ابدأ باستخدام [حساب Azure مجاني](https://aka.ms/azure-free-account) إذا لم يكن لديك واحد.


## التعلم مع GitHub Copilot

للبدء السريع في الكتابة، افتح هذا المشروع في مساحة شيفرة على GitHub أو في بيئة التطوير المحلية لديك مع devcontainer المقدم. تأتي الحاوية المطورة المستخدمة في هذه الدورة معدة مسبقًا مع GitHub Copilot للبرمجة المزدوجة بالذكاء الاصطناعي.

كل مثال كود يتضمن أسئلة مقترحة يمكنك طرحها على GitHub Copilot لتعميق فهمك. ابحث عن رموز 💡/🤖 في:

- **رؤوس ملفات جافا** - أسئلة محددة لكل مثال
- **ملفات README للوحدات** - تلميحات استكشافية بعد أمثلة الكود

**كيفية الاستخدام:** افتح أي ملف كود واطرح على Copilot الأسئلة المقترحة. لديه السياق الكامل لقاعدة الشيفرة ويمكنه الشرح، التوسيع، واقتراح البدائل.

هل تريد معرفة المزيد؟ اطلع على [Copilot للبرمجة المزدوجة بالذكاء الاصطناعي](https://aka.ms/GitHubCopilotAI).


## موارد إضافية

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j للمبتدئين](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js للمبتدئين](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain للمبتدئين](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD للمبتدئين](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI للمبتدئين](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP للمبتدئين](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![وكلاء الذكاء الاصطناعي للمبتدئين](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سلسلة الذكاء الاصطناعي التوليدي
[![الذكاء الاصطناعي التوليدي للمبتدئين](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (جافا)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (جافا سكريبت)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### التعليم الأساسي
[![تعلم الآلة للمبتدئين](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![علوم البيانات للمبتدئين](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي للمبتدئين](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![الأمن السيبراني للمبتدئين](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![تطوير الويب للمبتدئين](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![إنترنت الأشياء للمبتدئين](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![تطوير XR للمبتدئين](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سلسلة Copilot
[![Copilot للبرمجة التعاونية بالذكاء الاصطناعي](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot لـ C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![مغامرة Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## الحصول على المساعدة

إذا واجهت صعوبة أو كان لديك أي أسئلة حول بناء تطبيقات الذكاء الاصطناعي، انضم إلى:

[![مجتمع Microsoft Foundry في ديسكورد](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

إذا كان لديك ملاحظات على المنتج أو أخطاء أثناء البناء، قم بزيارة:

[![منتدى مطوري Microsoft Foundry على GitHub](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## الترخيص

ترخيص MIT - انظر ملف [LICENSE](../../LICENSE) للتفاصيل.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم بأن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر المعتمد. بالنسبة للمعلومات الحساسة، يُنصح بالترجمة المهنية البشرية. نحن غير مسؤولين عن أي سوء فهم أو تفسيرات خاطئة ناتجة عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->