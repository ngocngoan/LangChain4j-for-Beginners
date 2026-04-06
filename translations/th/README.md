<img src="../../translated_images/th/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j สำหรับผู้เริ่มต้น

คอร์สสำหรับสร้างแอปพลิเคชัน AI ด้วย LangChain4j และ Azure OpenAI GPT-5.2 ตั้งแต่แชทพื้นฐานจนถึงตัวแทน AI

### 🌐 การรองรับหลายภาษา

#### รองรับผ่าน GitHub Action (อัตโนมัติ & อัพเดตเสมอ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](./README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ต้องการโคลนลงเครื่องหรือไม่?**
>
> ที่เก็บนี้รวมการแปลมากกว่า 50 ภาษา ซึ่งเพิ่มขนาดการดาวน์โหลดอย่างมาก สำหรับโคลนโดยไม่รวมการแปล ให้ใช้ sparse checkout:
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
> วิธีนี้จะให้ทุกสิ่งที่คุณต้องการเพื่อทำคอร์สนี้ให้เสร็จพร้อมการดาวน์โหลดที่เร็วขึ้นมาก
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## สารบัญ

1. [เริ่มต้นอย่างรวดเร็ว](00-quick-start/README.md) - เริ่มต้นกับ LangChain4j
2. [บทนำ](01-introduction/README.md) - เรียนรู้พื้นฐานของ LangChain4j
3. [การออกแบบ Prompt](02-prompt-engineering/README.md) - เชี่ยวชาญการออกแบบ prompt ที่มีประสิทธิภาพ
4. [RAG (การสร้างด้วยการค้นคืน)](03-rag/README.md) - สร้างระบบฐานความรู้ที่ชาญฉลาด
5. [เครื่องมือ](04-tools/README.md) - รวมเครื่องมือภายนอกและผู้ช่วยง่ายๆ
6. [MCP (โปรโตคอลบริบทของโมเดล)](05-mcp/README.md) - ทำงานกับโปรโตคอลบริบทของโมเดล (MCP) และโมดูล Agentic

### วิดีโอแนะนำ

แต่ละโมดูลมีเซสชันถ่ายทอดสดที่เราจะอธิบายแนวคิดและโค้ดทีละขั้นตอน

| โมดูล | วิดีโอ |
|--------|-------|
| 01 - บทนำ | [เริ่มต้นกับ LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - การออกแบบ Prompt | [การออกแบบ Prompt ด้วย LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG ด้วย LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - เครื่องมือ & 05 - MCP | [ตัวแทน AI พร้อมเครื่องมือและ MCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## เส้นทางการเรียนรู้

**ใหม่กับ LangChain4j?** ดู [พจนานุกรม](docs/GLOSSARY.md) สำหรับคำจำกัดความของคำและแนวคิดหลัก

> **เริ่มต้นอย่างรวดเร็ว**

1. Fork ที่เก็บนี้ไปยังบัญชี GitHub ของคุณ
2. คลิก **Code** → แท็บ **Codespaces** → **...** → **สร้างใหม่พร้อมตัวเลือก...**
3. ใช้ค่าพื้นฐาน – จะเลือก Development container ที่สร้างไว้สำหรับคอร์สนี้
4. คลิก **Create codespace**
5. รอ 5-10 นาทีจนสภาพแวดล้อมพร้อมใช้งาน
6. ไปที่ [เริ่มต้นอย่างรวดเร็ว](./00-quick-start/README.md) เพื่อเริ่มต้น!

หลังจากทำโมดูลเสร็จแล้ว สำรวจ [คู่มือการทดสอบ](docs/TESTING.md) เพื่อดูแนวคิดการทดสอบ LangChain4j ในการใช้งานจริง

> **หมายเหตุ:** การอบรมนี้ใช้ GitHub Models และ Azure OpenAI โมดูล [เริ่มต้นอย่างรวดเร็ว](00-quick-start/README.md) ใช้ GitHub Models (ไม่ต้องใช้การสมัคร Azure) ขณะที่โมดูล 1-5 ใช้ Azure OpenAI เริ่มต้นด้วย [บัญชี Azure ฟรี](https://aka.ms/azure-free-account) หากคุณยังไม่มี


## การเรียนรู้กับ GitHub Copilot

เพื่อเริ่มโค้ดอย่างรวดเร็ว เปิดโปรเจกต์นี้ใน GitHub Codespace หรือ IDE ในเครื่องของคุณด้วย devcontainer ที่เตรียมไว้ล่วงหน้า devcontainer ที่ใช้ในคอร์สนี้มาพร้อม GitHub Copilot สำหรับการเขียนโค้ดแบบจับคู่กับ AI

แต่ละตัวอย่างโค้ดมีคำถามแนะนำที่คุณสามารถถาม GitHub Copilot เพื่อเสริมสร้างความเข้าใจ ค้นหาสัญลักษณ์ 💡/🤖 ใน:

- **ส่วนหัวไฟล์ Java** - คำถามเฉพาะสำหรับแต่ละตัวอย่าง
- **README โมดูล** - คำถามสำรวจหลังตัวอย่างโค้ด

**ใช้อย่างไร:** เปิดไฟล์โค้ดใดก็ได้แล้วถาม Copilot ตามคำถามที่แนะนำ มันมีบริบทของฐานโค้ดทั้งหมดและสามารถอธิบาย ขยายความ และแนะนำทางเลือกได้

ต้องการเรียนรู้เพิ่มเติม? ดูที่ [Copilot สำหรับการเขียนโค้ดกับ AI](https://aka.ms/GitHubCopilotAI)


## แหล่งข้อมูลเพิ่มเติม

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP สำหรับผู้เริ่มต้น](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ตัวแทน AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ชุด AI เชิงสร้างสรรค์
[![Generative AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### การเรียนรู้แกนหลัก
[![ML สำหรับผู้เริ่มต้น](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ชุดซีรีส์ Copilot
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## การขอความช่วยเหลือ

หากคุณติดขัดหรือต้องการถามคำถามเกี่ยวกับการสร้างแอป AI เข้าร่วมที่:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

หากคุณมีข้อเสนอแนะหรือพบข้อผิดพลาดขณะพัฒนาโปรดเยี่ยมชม:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ใบอนุญาต

ใบอนุญาต MIT - ดูไฟล์ [LICENSE](../../LICENSE) สำหรับรายละเอียดเพิ่มเติม

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารฉบับนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้มีความถูกต้อง โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้บริการแปลโดยมืออาชีพที่เป็นมนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->