# Module 01: การเริ่มต้นใช้งาน LangChain4j

## สารบัญ

- [วิดีโอสอน](../../../01-introduction)
- [สิ่งที่คุณจะได้เรียนรู้](../../../01-introduction)
- [ข้อกำหนดเบื้องต้น](../../../01-introduction)
- [เข้าใจปัญหาหลัก](../../../01-introduction)
- [เข้าใจเกี่ยวกับโทเค็น](../../../01-introduction)
- [วิธีการทำงานของความจำ](../../../01-introduction)
- [วิธีการใช้ LangChain4j ในที่นี้](../../../01-introduction)
- [การปรับใช้โครงสร้างพื้นฐาน Azure OpenAI](../../../01-introduction)
- [รันแอปพลิเคชันในเครื่อง](../../../01-introduction)
- [การใช้งานแอปพลิเคชัน](../../../01-introduction)
  - [แชทแบบไม่มีสถานะ (แผงซ้าย)](../../../01-introduction)
  - [แชทแบบมีสถานะ (แผงขวา)](../../../01-introduction)
- [ขั้นตอนถัดไป](../../../01-introduction)

## วิดีโอสอน

ชมเซสชันสดนี้ที่อธิบายวิธีเริ่มต้นใช้งานโมดูลนี้:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## สิ่งที่คุณจะได้เรียนรู้

ถ้าคุณทำแบบเริ่มต้นด่วน คุณจะเห็นวิธีส่งพรอมต์และรับคำตอบ นั่นคือพื้นฐาน แต่แอปพลิเคชันจริงต้องการมากกว่านั้น โมดูลนี้สอนคุณวิธีสร้าง AI การสนทนาที่จดจำบริบทและรักษาสถานะได้ — ความแตกต่างระหว่างเดโมครั้งเดียวกับแอปพลิเคชันที่พร้อมใช้งานจริง

เราจะใช้ GPT-5.2 ของ Azure OpenAI ตลอดคำแนะนำนี้เพราะความสามารถในการให้เหตุผลขั้นสูงทำให้พฤติกรรมของรูปแบบต่าง ๆ ชัดเจนขึ้น เมื่อคุณเพิ่มความจำคุณจะเห็นความแตกต่างได้ชัดเจนขึ้น ซึ่งช่วยให้เข้าใจว่าทุกองค์ประกอบมีผลต่อแอปพลิเคชันของคุณอย่างไร

คุณจะสร้างแอปพลิเคชันหนึ่งตัวที่สาธิตรูปแบบทั้งสองนี้:

**แชทแบบไม่มีสถานะ** - ทุกคำขอมีความเป็นอิสระ โมเดลจะไม่จำข้อความก่อนหน้า นี่คือรูปแบบที่คุณใช้ในแบบเริ่มต้นด่วน

**สนทนาแบบมีสถานะ** - ทุกคำขอจะมีประวัติการสนทนา โมเดลจะรักษาบริบทข้ามรอบหลายรอบ นี่คือสิ่งที่แอปพลิเคชันในโลกจริงต้องการ

## ข้อกำหนดเบื้องต้น

- การสมัครใช้งาน Azure ที่เข้าถึง Azure OpenAI ได้
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI และ Azure Developer CLI (azd) ได้ถูกติดตั้งไว้ล่วงหน้าใน devcontainer ที่ให้มาแล้ว

> **Note:** โมดูลนี้ใช้ GPT-5.2 บน Azure OpenAI การปรับใช้นี้ถูกกำหนดค่าโดยอัตโนมัติผ่าน `azd up` — ห้ามแก้ไขชื่อโมเดลในโค้ด

## เข้าใจปัญหาหลัก

โมเดลภาษาเป็นแบบไม่มีสถานะ การเรียก API แต่ละครั้งคือการดำเนินการที่แยกจากกัน หากคุณส่ง "ชื่อของฉันคือจอห์น" แล้วถามทันทีว่า "ชื่อของฉันคืออะไร?" โมเดลจะไม่รู้ว่าคุณเพิ่งแนะนำตัวเอง มันจะปฏิบัติกับทุกคำขอเหมือนเป็นการสนทนาแรกที่คุณเคยมีเท่านั้น

วิธีนี้ใช้ได้กับคำถาม-คำตอบง่าย ๆ แต่ไม่มีประโยชน์กับแอปพลิเคชันจริง หุ่นยนต์บริการลูกค้าต้องจำสิ่งที่คุณบอก ผู้ช่วยส่วนตัวต้องมีบริบท การสนทนาหลายรอบต้องมีความจำ

<img src="../../../translated_images/th/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ความแตกต่างระหว่างการสนทนาแบบไม่มีสถานะ (การเรียกแยกกัน) และแบบมีสถานะ (มีบริบท)*

## เข้าใจเกี่ยวกับโทเค็น

ก่อนจะลงมือพูดคุย มันสำคัญที่ต้องเข้าใจโทเค็น — หน่วยพื้นฐานของข้อความที่โมเดลภาษาประมวลผล:

<img src="../../../translated_images/th/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ตัวอย่างการแบ่งข้อความเป็นโทเค็น — "I love AI!" กลายเป็นหน่วยประมวลผลแยก 4 ตัว*

โทเค็นคือวิธีที่โมเดล AI วัดและประมวลผลข้อความ คำ เครื่องหมายวรรคตอน และแม้แต่ช่องว่างสามารถเป็นโทเค็นได้ โมเดลของคุณมีข้อจำกัดจำนวนโทเค็นที่ประมวลผลได้พร้อมกัน (400,000 สำหรับ GPT-5.2 โดยรับเข้าได้สูงสุด 272,000 โทเค็นและส่งออกได้ 128,000 โทเค็น) การเข้าใจโทเค็นช่วยให้คุณจัดการความยาวการสนทนาและค่าใช้จ่ายได้

## วิธีการทำงานของความจำ

ความจำแชทแก้ปัญหาแบบไม่มีสถานะด้วยการรักษาประวัติการสนทนา ก่อนส่งคำขอไปยังโมเดล เฟรมเวิร์กจะต่อข้อความก่อนหน้าที่เกี่ยวข้องเข้าไป เมื่อคุณถามว่า "ชื่อของฉันคืออะไร?" ระบบจะแนบประวัติการสนทนาทั้งหมด ส่งให้โมเดลเพื่อให้เห็นว่าคุณได้บอกว่า "ชื่อของฉันคือจอห์น" ไปก่อนหน้านั้นแล้ว

LangChain4j มีการติดตั้งความจำที่จัดการสิ่งนี้โดยอัตโนมัติ คุณเลือกจำนวนข้อความที่ต้องการเก็บไว้และเฟรมเวิร์กจะจัดการกับหน้าต่างบริบท

<img src="../../../translated_images/th/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory รักษาหน้าต่างเลื่อนของข้อความล่าสุดโดยทิ้งข้อความเก่าออกโดยอัตโนมัติ*

## วิธีการใช้ LangChain4j ในที่นี้

โมดูลนี้ขยายแบบเริ่มต้นด่วนด้วยการผนวก Spring Boot และเพิ่มความจำการสนทนา นี่คือวิธีที่ชิ้นส่วนต่าง ๆ ประสานกัน:

**Dependencies** - เพิ่มไลบรารี LangChain4j สองตัว:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
**Chat Model** - กำหนดค่า Azure OpenAI เป็น Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```
  
ตัวสร้างอ่านข้อมูลรับรองจากตัวแปรแวดล้อมที่ตั้งโดย `azd up` การตั้งค่า `baseUrl` เป็นจุดสิ้นสุดของ Azure ของคุณทำให้ไคลเอนต์ OpenAI ทำงานร่วมกับ Azure OpenAI ได้

**Conversation Memory** - ติดตามประวัติการแชทด้วย MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
สร้างความจำด้วย `withMaxMessages(10)` เพื่อเก็บข้อความล่าสุด 10 ข้อความ เพิ่มข้อความผู้ใช้และ AI ด้วยตัวห่อประเภท: `UserMessage.from(text)` และ `AiMessage.from(text)` ดึงประวัติด้วย `memory.messages()` และส่งไปยังโมเดล เซอร์วิสเก็บความจำแต่ละตัวแยกตาม ID การสนทนา ทำให้หลายผู้ใช้แชทพร้อมกันได้

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) และถามว่า:
> - "MessageWindowChatMemory ตัดสินใจจะทิ้งข้อความไหนเมื่อหน้าต่างเต็มอย่างไร?"
> - "สามารถสร้างการเก็บความจำแบบกำหนดเองโดยใช้ฐานข้อมูลแทนในหน่วยความจำได้ไหม?"
> - "จะเพิ่มฟีเจอร์สรุปเพื่อบีบอัดประวัติการสนทนาเก่าอย่างไร?"

จุดสิ้นสุดแชทแบบไม่มีสถานะข้ามความจำไปเลย — เพียงเรียก `chatModel.chat(prompt)` เหมือนแบบเริ่มต้นด่วน จุดสิ้นสุดแชทแบบมีสถานะเพิ่มข้อความลงในความจำ ดึงประวัติ และรวมบริบทนั้นในทุกคำขอ การกำหนดค่าโมเดลเหมือนกัน แต่รูปแบบแตกต่างกัน

## การปรับใช้โครงสร้างพื้นฐาน Azure OpenAI

**Bash:**  
```bash
cd 01-introduction
azd up  # เลือกการสมัครใช้งานและตำแหน่งที่ตั้ง (แนะนำ eastus2)
```
  
**PowerShell:**  
```powershell
cd 01-introduction
azd up  # เลือกการสมัครใช้งานและตำแหน่งที่ตั้ง (แนะนำ eastus2)
```
  
> **Note:** หากเจอข้อผิดพลาด timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ให้รัน `azd up` อีกครั้ง ทรัพยากร Azure อาจยังอยู่ในขั้นตอนการจัดสรร การลองใหม่จะช่วยให้การปรับใช้เสร็จสมบูรณ์เมื่อทรัพยากรอยู่ในสถานะสิ้นสุด

สิ่งนี้จะทำ:
1. ปรับใช้ทรัพยากร Azure OpenAI ด้วยโมเดล GPT-5.2 และ text-embedding-3-small
2. สร้างไฟล์ `.env` อัตโนมัติในโฟลเดอร์โปรเจกต์พร้อมข้อมูลรับรอง
3. ตั้งค่าตัวแปรแวดล้อมทั้งหมดที่จำเป็น

**มีปัญหาการปรับใช้?** ดู [Infrastructure README](infra/README.md) สำหรับการแก้ปัญหาโดยละเอียด รวมถึงปัญหาชื่อซับโดเมนซ้ำ ขั้นตอนการปรับใช้ด้วยตนเองผ่าน Azure Portal และคำแนะนำการตั้งค่าโมเดล

**ยืนยันว่าการปรับใช้สำเร็จ:**

**Bash:**  
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, เป็นต้น
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, เป็นต้น
```
  
> **Note:** คำสั่ง `azd up` สร้างไฟล์ `.env` อัตโนมัติ หากต้องการอัปเดตภายหลัง คุณสามารถแก้ไขไฟล์ `.env` ด้วยตนเองหรือสร้างใหม่โดยรัน:
>
> **Bash:**  
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>  
> **PowerShell:**  
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```
  
## รันแอปพลิเคชันในเครื่อง

**ตรวจสอบการปรับใช้:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` มีอยู่ที่โฟลเดอร์หลักพร้อมข้อมูลรับรอง Azure:

**Bash:**  
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**เริ่มแอปพลิเคชัน:**

**ตัวเลือก 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

dev container มีส่วนขยาย Spring Boot Dashboard ซึ่งให้ส่วนติดต่อแบบภาพเพื่อจัดการแอป Spring Boot ทั้งหมด คุณพบได้ในแถบกิจกรรมด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:  
- ดูแอป Spring Boot ทั้งหมดในพื้นที่ทำงาน  
- เริ่ม/หยุดแอปได้ด้วยคลิกเดียว  
- ดูบันทึกแอปแบบเรียลไทม์  
- ตรวจสอบสถานะแอป

คลิกปุ่มเล่นข้าง "introduction" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือก 2: ใช้สคริปต์เชลล์**

เริ่มเว็บแอปทั้งหมด (โมดูล 01-04):

**Bash:**  
```bash
cd ..  # จากไดเรกทอรีรูท
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # จากไดเรกทอรีรูท
.\start-all.ps1
```
  
หรือเริ่มเฉพาะโมดูลนี้:

**Bash:**  
```bash
cd 01-introduction
./start.sh
```
  
**PowerShell:**  
```powershell
cd 01-introduction
.\start.ps1
```
  
ทั้งสองสคริปต์จะโหลดตัวแปรแวดล้อมจากไฟล์ `.env` ที่โฟลเดอร์หลักโดยอัตโนมัติและจะสร้าง JAR หากยังไม่มี

> **Note:** หากคุณต้องการสร้างโมดูลทั้งหมดด้วยตนเองก่อนเริ่ม:
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
  
เปิด http://localhost:8080 ในเบราว์เซอร์ของคุณ

**จะหยุดโปรแกรม:**

**Bash:**  
```bash
./stop.sh  # โมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # โมดูลทั้งหมด
```
  
## การใช้งานแอปพลิเคชัน

แอปพลิเคชันมีอินเทอร์เฟซเว็บพร้อมสองระบบแชทอยู่เคียงข้างกัน

<img src="../../../translated_images/th/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*แดชบอร์ดแสดงตัวเลือก Simple Chat (ไม่มีสถานะ) และ Conversational Chat (มีสถานะ)*

### แชทแบบไม่มีสถานะ (แผงซ้าย)

ลองใช้ก่อน ถามว่า "ชื่อของฉันคือจอห์น" แล้วถามทันทีว่า "ชื่อของฉันคืออะไร?" โมเดลจะไม่จำเพราะแต่ละข้อความแยกจากกัน นี่แสดงปัญหาหลักของการผนวกรวมโมเดลภาษาแบบพื้นฐาน — ไม่มีบริบทการสนทนา

<img src="../../../translated_images/th/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI จะไม่จำชื่อของคุณจากข้อความก่อนหน้า*

### แชทแบบมีสถานะ (แผงขวา)

ลองทำลำดับเดียวกันที่นี่ ถามว่า "ชื่อของฉันคือจอห์น" แล้วถาม "ชื่อของฉันคืออะไร?" ครั้งนี้มันจำได้ ความแตกต่างคือ MessageWindowChatMemory — มันรักษาประวัติการสนทนาและรวมในทุกคำขอ นี่คือวิธีที่ AI การสนทนาในโลกจริงทำงาน

<img src="../../../translated_images/th/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI จำชื่อของคุณได้จากการสนทนาในช่วงก่อนหน้า*

ทั้งสองแผงใช้โมเดล GPT-5.2 เดียวกัน ความแตกต่างเพียงอย่างเดียวคือความจำ นี่ทำให้เห็นอย่างชัดเจนว่าความจำช่วยแอปพลิเคชันของคุณอย่างไรและทำไมจึงจำเป็นสำหรับกรณีใช้งานจริง

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [02-prompt-engineering - การออกแบบพรอมต์ด้วย GPT-5.2](../02-prompt-engineering/README.md)

---

**นำทาง:** [← ก่อนหน้า: Module 00 - เริ่มต้นด่วน](../00-quick-start/README.md) | [กลับสู่หน้าแรก](../README.md) | [ถัดไป: Module 02 - การออกแบบพรอมต์ →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**: เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางถือเป็นแหล่งข้อมูลที่น่าเชื่อถือที่สุด สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลโดยมืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดใด ๆ ที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->