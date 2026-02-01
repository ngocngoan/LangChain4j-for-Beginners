<img src="../../translated_images/fa/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 پشتیبانی چندزبانه

#### پشتیبانی از طریق GitHub Action (خودکار و همیشه به‌روز)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[عربی](../ar/README.md) | [بنگالی](../bn/README.md) | [بلغاری](../bg/README.md) | [بورمی (میانمار)](../my/README.md) | [چینی (ساده‌شده)](../zh-CN/README.md) | [چینی (سنتی، هنگ‌کنگ)](../zh-HK/README.md) | [چینی (سنتی، ماکائو)](../zh-MO/README.md) | [چینی (سنتی، تایوان)](../zh-TW/README.md) | [کرواسی](../hr/README.md) | [چکی](../cs/README.md) | [دانمارکی](../da/README.md) | [هلندی](../nl/README.md) | [استونیایی](../et/README.md) | [فنلاندی](../fi/README.md) | [فرانسوی](../fr/README.md) | [آلمانی](../de/README.md) | [یونانی](../el/README.md) | [عبری](../he/README.md) | [هندی](../hi/README.md) | [مجارستانی](../hu/README.md) | [اندونزیایی](../id/README.md) | [ایتالیایی](../it/README.md) | [ژاپنی](../ja/README.md) | [کانادا](../kn/README.md) | [کره‌ای](../ko/README.md) | [لیتوانیایی](../lt/README.md) | [مالایی](../ms/README.md) | [مالایالام](../ml/README.md) | [مراتی](../mr/README.md) | [نپالی](../ne/README.md) | [پیدگین نیجریه‌ای](../pcm/README.md) | [نروژی](../no/README.md) | [فارسی (دری)](./README.md) | [لهستانی](../pl/README.md) | [پرتغالی (برزیل)](../pt-BR/README.md) | [پرتغالی (پرتغال)](../pt-PT/README.md) | [پنجابی (گورمکھی)](../pa/README.md) | [رومانیایی](../ro/README.md) | [روسی](../ru/README.md) | [صربی (سیریلیک)](../sr/README.md) | [اسلواکی](../sk/README.md) | [اسلوونیایی](../sl/README.md) | [اسپانیایی](../es/README.md) | [سواحیلی](../sw/README.md) | [سوئدی](../sv/README.md) | [تاگالوگ (فیلیپینی)](../tl/README.md) | [تامیلی](../ta/README.md) | [تلوگو](../te/README.md) | [تایلندی](../th/README.md) | [ترکی](../tr/README.md) | [اوکراینی](../uk/README.md) | [اردو](../ur/README.md) | [ویتنامی](../vi/README.md)

> **ترجیح می‌دهید به صورت محلی کلون کنید؟**

> این مخزن شامل بیش از ۵۰ ترجمه زبان است که حجم دانلود را به طور قابل توجهی افزایش می‌دهد. برای کلون کردن بدون ترجمه‌ها، از sparse checkout استفاده کنید:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> این به شما همه چیز لازم را برای تکمیل دوره با دانلود بسیار سریع‌تر می‌دهد.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j برای مبتدیان

یک دوره برای ساخت برنامه‌های هوش مصنوعی با LangChain4j و Azure OpenAI GPT-5، از چت پایه تا عامل‌های هوش مصنوعی.

**جدید در LangChain4j؟** برای تعاریف اصطلاحات کلیدی و مفاهیم به [واژه‌نامه](docs/GLOSSARY.md) مراجعه کنید.

## فهرست مطالب

1. [شروع سریع](00-quick-start/README.md) - شروع کار با LangChain4j
2. [مقدمه](01-introduction/README.md) - یادگیری اصول پایه LangChain4j
3. [مهندسی پرامپت](02-prompt-engineering/README.md) - تسلط بر طراحی موثر پرامپت
4. [RAG (تولید تقویت‌شده بازیابی)](03-rag/README.md) - ساخت سیستم‌های هوشمند مبتنی بر دانش
5. [ابزارها](04-tools/README.md) - ادغام ابزارهای خارجی و دستیاران ساده
6. [MCP (پروتکل زمینه مدل)](05-mcp/README.md) - کار با پروتکل زمینه مدل (MCP) و ماژول‌های عامل
---

## مسیر یادگیری

> **شروع سریع**

1. این مخزن را به حساب گیت‌هاب خود فورک کنید
2. روی **Code** کلیک کنید → زبانه **Codespaces** → **...** → **New with options...**
3. پیش‌فرض‌ها را استفاده کنید – این فضای توسعه کانتینر ایجاد شده برای این دوره را انتخاب می‌کند
4. روی **Create codespace** کلیک کنید
5. ۵ تا ۱۰ دقیقه منتظر بمانید تا محیط آماده شود
6. مستقیم به [شروع سریع](./00-quick-start/README.md) بروید و فعالیت را آغاز کنید!

پس از تکمیل ماژول‌ها، راهنمای [آزمون](docs/TESTING.md) را برای مشاهده مفاهیم آزمون LangChain4j در عمل بررسی کنید.

> **نکته:** این آموزش از مدل‌های GitHub و Azure OpenAI استفاده می‌کند. ماژول [شروع سریع](00-quick-start/README.md) از مدل‌های GitHub استفاده می‌کند (نیازی به اشتراک Azure ندارد)، در حالی که ماژول‌های ۱ تا ۵ از Azure OpenAI بهره می‌برند. اگر حساب Azure ندارید، با یک [حساب رایگان Azure](https://aka.ms/azure-free-account) شروع کنید.


## یادگیری با GitHub Copilot

برای شروع سریع کدنویسی، این پروژه را در GitHub Codespace یا IDE محلی خود با devcontainer ارائه‌شده باز کنید. devcontainer استفاده‌شده در این دوره از قبل با GitHub Copilot برای برنامه‌نویسی جفت هوش مصنوعی پیکربندی شده است.

هر مثال کد شامل سوالاتی است که می‌توانید از GitHub Copilot بپرسید تا درک خود را عمیق‌تر کنید. به دنبال علامت‌های 💡/🤖 در:

- **هدرهای فایل جاوا** - سوالات مخصوص هر مثال
- **فایل‌های README ماژول‌ها** - پرسش‌های اکتشافی بعد از مثال‌های کد

**نحوه استفاده:** هر فایل کدی را باز کنید و سوالات پیشنهادی را از Copilot بپرسید. او به طور کامل با کد آشنا است و می‌تواند توضیح دهد، توسعه دهد و جایگزین پیشنهاد کند.

می‌خواهید بیشتر یاد بگیرید؟ به [Copilot برای برنامه‌نویسی جفت هوش مصنوعی](https://aka.ms/GitHubCopilotAI) سر بزنید.


## منابع اضافی

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j برای مبتدیان](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js برای مبتدیان](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / عامل‌ها
[![AZD برای مبتدیان](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI برای مبتدیان](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP برای مبتدیان](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![عامل‌های هوش مصنوعی برای مبتدیان](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سری هوش مصنوعی مولد
[![هوش مصنوعی مولد برای مبتدیان](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![هوش مصنوعی مولد (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![هوش مصنوعی مولد (جاوا)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![هوش مصنوعی مولد (جاوااسکریپت)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### یادگیری پایه
[![یادگیری ماشین برای مبتدیان](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![علم داده برای مبتدیان](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![هوش مصنوعی برای مبتدیان](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![امنیت سایبری برای مبتدیان](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![توسعه وب برای مبتدیان](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![اینترنت اشیاء برای مبتدیان](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![توسعه XR برای مبتدیان](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سری کپلوت
[![کپلوت برای برنامه‌نویسی جفتی هوش مصنوعی](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![کپلوت برای C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![ماجراجویی کپلوت](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## دریافت کمک

اگر گیر کردید یا سوالی در مورد ساخت برنامه‌های هوش مصنوعی دارید، بپیوندید به:

[![دیسکورد آزور AI فاندری](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

اگر بازخورد محصول یا خطاهایی هنگام ساخت مشاهده کردید، مراجعه کنید به:

[![انجمن توسعه‌دهندگان آزور AI فاندری](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## مجوز

مجوز MIT - برای جزئیات فایل [LICENSE](../../LICENSE) را ببینید.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه خودکار [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما تلاش می‌کنیم دقت را حفظ کنیم، لطفاً آگاه باشید که ترجمه‌های خودکار ممکن است حاوی خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئول هیچ سوءتفاهم یا تفسیر نادرستی که به‌واسطه استفاده از این ترجمه به وجود آید نیستیم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->