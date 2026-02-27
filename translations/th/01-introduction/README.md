# Module 01: เริ่มต้นกับ LangChain4j

## สารบัญ

- [วิดีโอสอนใช้งาน](../../../01-introduction)
- [สิ่งที่คุณจะได้เรียนรู้](../../../01-introduction)
- [สิ่งที่ต้องเตรียม](../../../01-introduction)
- [ทำความเข้าใจปัญหาหลัก](../../../01-introduction)
- [ทำความเข้าใจโทเคน](../../../01-introduction)
- [การทำงานของหน่วยความจำ](../../../01-introduction)
- [วิธีการใช้งาน LangChain4j นี้](../../../01-introduction)
- [ติดตั้งโครงสร้างพื้นฐาน Azure OpenAI](../../../01-introduction)
- [รันแอปพลิเคชันบนเครื่องของคุณ](../../../01-introduction)
- [การใช้งานแอปพลิเคชัน](../../../01-introduction)
  - [แชทแบบ Stateless (แผงซ้าย)](../../../01-introduction)
  - [แชทแบบ Stateful (แผงขวา)](../../../01-introduction)
- [ขั้นตอนถัดไป](../../../01-introduction)

## วิดีโอสอนใช้งาน

ดูเซสชันสดนี้ที่อธิบายวิธีเริ่มต้นกับโมดูลนี้: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## สิ่งที่คุณจะได้เรียนรู้

ถ้าคุณทำตาม quick start เสร็จแล้ว คุณจะเห็นวิธีส่งพรอมต์และรับคำตอบ นั่นเป็นพื้นฐาน แต่แอปพลิเคชันจริงต้องการมากกว่านั้น โมดูลนี้จะสอนวิธีสร้าง AI สนทนาที่จดจำบริบทและรักษาสถานะ — ความแตกต่างระหว่างเดโมแบบครั้งเดียวกับแอปพลิเคชันที่พร้อมใช้งานจริง

เราจะใช้ GPT-5.2 ของ Azure OpenAI ตลอดคู่มือนี้ เพราะความสามารถในการให้เหตุผลขั้นสูงทำให้พฤติกรรมของรูปแบบต่าง ๆ ชัดเจนขึ้น เมื่อคุณเพิ่มหน่วยความจำ คุณจะเห็นความแตกต่างอย่างชัดเจน ซึ่งช่วยให้เข้าใจว่าทุกส่วนประกอบนำอะไรมาให้แอปพลิเคชันของคุณ

คุณจะสร้างแอปพลิเคชันหนึ่งตัวที่แสดงทั้งสองรูปแบบ:

**Stateless Chat** - ทุกคำร้องขอเป็นอิสระ Model ไม่มีหน่วยความจำของข้อความก่อนหน้านี้ นี่คือรูปแบบที่คุณใช้ใน quick start

**Stateful Conversation** - ทุกคำร้องขอรวมประวัติการสนทนา Model รักษาบริบทข้ามรอบสนทนาหลายครั้ง นี่คือสิ่งที่แอปพลิเคชันใช้งานจริงต้องการ

## สิ่งที่ต้องเตรียม

- บัญชีสมาชิก Azure ที่มีสิทธิ์เข้าถึง Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI และ Azure Developer CLI (azd) ถูกติดตั้งล่วงหน้าใน devcontainer ที่ให้มา

> **Note:** โมดูลนี้ใช้งาน GPT-5.2 บน Azure OpenAI การดีพลอยจะถูกกำหนดค่าโดยอัตโนมัติผ่าน `azd up` — ห้ามแก้ไขชื่อโมเดลในโค้ด

## ทำความเข้าใจปัญหาหลัก

โมเดลภาษาเป็นแบบ stateless การเรียก API แต่ละครั้งเป็นอิสระ หากคุณส่งข้อความว่า "ชื่อของฉันคือ John" แล้วถามว่า "ชื่อฉันคืออะไร?" โมเดลจะไม่ทราบว่าคุณเพิ่งแนะนำตัว มันปฏิบัติกับทุกคำร้องเหมือนเป็นการสนทนาแรกที่คุณมี

สิ่งนี้ใช้ได้ดีกับคำถามและคำตอบง่าย ๆ แต่ไร้ประโยชน์สำหรับแอปพลิเคชันจริง บอตบริการลูกค้าต้องจำสิ่งที่คุณบอกไป ผู้ช่วยส่วนตัวต้องมีบริบท การสนทนาที่มีรอบหลาย ๆ ครั้งจำเป็นต้องมีหน่วยความจำ

<img src="../../../translated_images/th/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ความแตกต่างระหว่างการสนทนาแบบ stateless (การเรียกที่เป็นอิสระ) กับ stateful (รับรู้บริบท)*

## ทำความเข้าใจโทเคน

ก่อนจะเข้าสู่การสนทนา สำคัญที่ต้องเข้าใจโทเคน — หน่วยพื้นฐานของข้อความที่โมเดลภาษาประมวลผล:

<img src="../../../translated_images/th/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ตัวอย่างการแยกข้อความเป็นโทเคน — "I love AI!" กลายเป็น 4 หน่วยประมวลผลแยกกัน*

โทเคนคือวิธีที่โมเดล AI วัดและประมวลผลข้อความ คำ เครื่องหมายวรรคตอน รวมถึงช่องว่างก็อาจเป็นโทเคนได้ โมเดลของคุณมีขีดจำกัดว่าใข้โทเคนได้กี่ตัวในการประมวลผลครั้งเดียว (400,000 สำหรับ GPT-5.2 โดยรับเข้าได้สูงสุด 272,000 โทเคน และส่งออกได้สูงสุด 128,000 โทเคน) การเข้าใจโทเคนช่วยให้คุณจัดการความยาวการสนทนาและค่าใช้จ่ายได้

## การทำงานของหน่วยความจำ

หน่วยความจำแชทแก้ปัญหา stateless โดยการเก็บประวัติการสนทนา ก่อนส่งคำร้องขอไปยังโมเดล เฟรมเวิร์กจะนำข้อความก่อนหน้าที่เกี่ยวข้องมาใส่ไว้ข้างหน้า เมื่อคุณถามว่า "ชื่อฉันคืออะไร?" ระบบจะส่งประวัติการสนทนาทั้งหมด ทำให้โมเดลเห็นว่าคุณเพิ่งบอกว่า "ชื่อของฉันคือ John"

LangChain4j มีการใช้งานหน่วยความจำที่จัดการให้อัตโนมัติ คุณเลือกได้ว่าจะเก็บกี่ข้อความและเฟรมเวิร์กจะจัดการบริบทให้

<img src="../../../translated_images/th/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory รักษาหน้าต่างเลื่อนของข้อความล่าสุดโดยอัตโนมัติทิ้งข้อความเก่า*

## วิธีการใช้งาน LangChain4j นี้

โมดูลนี้ต่อยอดจาก quick start โดยผสาน Spring Boot และเพิ่มหน่วยความจำการสนทนา นี่คือวิธีเชื่อมต่อส่วนต่าง ๆ:

**Dependencies** — เพิ่มไลบรารี LangChain4j สองตัว:

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
  
**Chat Model** — กำหนดค่า Azure OpenAI เป็น Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
Builder อ่านข้อมูลรับรองจากตัวแปรสภาพแวดล้อมที่ตั้งค่าโดย `azd up` การกำหนด `baseUrl` ไปยังจุดสิ้นสุด Azure ของคุณทำให้ไคลเอนต์ OpenAI ทำงานกับ Azure OpenAI

**Conversation Memory** — ติดตามประวัติการสนทนาด้วย MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
สร้างหน่วยความจำด้วย `withMaxMessages(10)` เพื่อเก็บข้อความล่าสุด 10 ข้อความ เพิ่มข้อความผู้ใช้และ AI ด้วยตัวห่อชนิดที่กำหนด: `UserMessage.from(text)` และ `AiMessage.from(text)` เรียกดูประวัติด้วย `memory.messages()` และส่งไปที่โมเดล เซอร์วิสจะเก็บหน่วยความจำของแต่ละ ID การสนทนาแยกกัน ช่วยให้ผู้ใช้หลายคนสนทนาพร้อมกันได้

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) แล้วถาม:
> - "MessageWindowChatMemory ตัดสินใจทิ้งข้อความไหนเมื่อหน้าต่างเต็มอย่างไร?"
> - "ฉันสามารถใช้งานที่เก็บหน่วยความจำแบบกำหนดเองโดยใช้ฐานข้อมูลแทนการเก็บในหน่วยความจำได้ไหม?"
> - "จะเพิ่มการสรุปเพื่อบีบอัดประวัติการสนทนาเก่าได้อย่างไร?"

จุดเชื่อมต่อแชทแบบ stateless จะข้ามหน่วยความจำทั้งหมด — เพียงเรียก `chatModel.chat(prompt)` เหมือนใน quick start ส่วนจุดเชื่อมต่อแบบ stateful จะเพิ่มข้อความเก็บลงหน่วยความจำ ดึงประวัติ และแนบบริบทนั้นพร้อมกับทุกคำร้องขอ ใช้การตั้งค่าโมเดลเดียวกัน แต่รูปแบบต่างกัน

## ติดตั้งโครงสร้างพื้นฐาน Azure OpenAI

**Bash:**  
```bash
cd 01-introduction
azd up  # เลือกการสมัครใช้งานและสถานที่ตั้ง (แนะนำ eastus2)
```
  
**PowerShell:**  
```powershell
cd 01-introduction
azd up  # เลือกการสมัครใช้งานและที่ตั้ง (แนะนำ eastus2)
```
  
> **Note:** หากพบข้อผิดพลาด timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ให้วิ่งคำสั่ง `azd up` อีกครั้ง ทรัพยากร Azure อาจยังอยู่ระหว่างการจัดเตรียมในเบื้องหลัง และการลองใหม่จะทำให้การดีพลอยสำเร็จเมื่อทรัพยากรพร้อมแล้ว

สิ่งนี้จะทำ:  
1. ดีพลอยทรัพยากร Azure OpenAI พร้อมโมเดล GPT-5.2 และ text-embedding-3-small  
2. สร้างไฟล์ `.env` อัตโนมัติในโฟลเดอร์รากของโปรเจกต์พร้อมข้อมูลรับรอง  
3. ตั้งค่าตัวแปรสิ่งแวดล้อมที่จำเป็นทั้งหมด

**มีปัญหาในการดีพลอย?** ดู [Infrastructure README](infra/README.md) สำหรับการแก้ปัญหาโดยละเอียด เช่น ปัญหาการชนกันของชื่อซับโดเมน ขั้นตอนดีพลอยด้วย Azure Portal แบบแมนนวล และคำแนะนำการตั้งค่าโมเดล

**ตรวจสอบว่าการดีพลอยสำเร็จ:**

**Bash:**  
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, เป็นต้น
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, ฯลฯ
```
  
> **Note:** คำสั่ง `azd up` สร้างไฟล์ `.env` อัตโนมัติ หากต้องการอัปเดตไฟล์คุณสามารถแก้ไขไฟล์ `.env` ด้วยตนเอง หรือสร้างใหม่โดยการรัน:
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
  
## รันแอปพลิเคชันบนเครื่องของคุณ

**ตรวจสอบการดีพลอย:**

ตรวจสอบว่าไฟล์ `.env` มีอยู่ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure:

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

Dev container มีส่วนขยาย Spring Boot Dashboard ที่ให้หน้าจอกราฟิกจัดการแอปพลิเคชัน Spring Boot ทั้งหมด คุณจะพบที่แถบกิจกรรมด้านซ้ายมือของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:  
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดในเวิร์กสเปซ  
- เริ่ม/หยุดแอปพลิเคชันด้วยคลิกเดียว  
- ดูบันทึกแอปพลิเคชันแบบเรียลไทม์  
- ตรวจสอบสถานะแอปพลิเคชัน

แค่คลิกปุ่มเล่นข้าง "introduction" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือก 2: ใช้สคริปต์เชลล์**

เริ่มเว็บไซต์ทั้งหมด (โมดูล 01-04):

**Bash:**  
```bash
cd ..  # จากไดเรกทอรีหลัก
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # จากไดเรกทอรีราก
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
  
ทั้งสองสคริปต์โหลดตัวแปรสิ่งแวดล้อมจากไฟล์ `.env` ที่รากโปรเจกต์ และจะคอมไพล์ JAR หากยังไม่มี

> **Note:** หากคุณต้องการคอมไพล์โมดูลทั้งหมดด้วยตนเองก่อนเริ่ม:
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

**เพื่อหยุด:**

**Bash:**  
```bash
./stop.sh  # มีเฉพาะโมดูลนี้
# หรือ
cd .. && ./stop-all.sh  # โมดูลทั้งหมด
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้
# หรือ
cd ..; .\stop-all.ps1  # โมดูลทั้งหมด
```
  
## การใช้งานแอปพลิเคชัน

แอปพลิเคชันมีเว็บอินเทอร์เฟซที่มีสองการแชทแสดงเคียงกัน

<img src="../../../translated_images/th/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*แดชบอร์ดแสดงตัวเลือกทั้ง Simple Chat (stateless) และ Conversational Chat (stateful)*

### แชทแบบ Stateless (แผงซ้าย)

ลองสิ่งนี้ก่อน ถามว่า "ชื่อของฉันคือ John" แล้วถามตามทันทีว่า "ชื่อฉันคืออะไร?" โมเดลจะไม่จำเพราะแต่ละข้อความเป็นอิสระ แสดงปัญหาหลักของการรวมโมเดลภาษาแบบพื้นฐาน — ไม่มีบริบทการสนทนา

<img src="../../../translated_images/th/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ไม่จำชื่อของคุณจากข้อความก่อนหน้า*

### แชทแบบ Stateful (แผงขวา)

ลองลำดับเดียวกันนี้ที่นี่ ถามว่า "ชื่อของฉันคือ John" แล้วตามด้วย "ชื่อฉันคืออะไร?" รอบนี้จำได้ ความต่างคือ MessageWindowChatMemory — มันรักษาประวัติการสนทนาและแนบไปกับทุกคำร้องขอ นี่คือวิธีการทำงานของ AI สนทนาในงานจริง

<img src="../../../translated_images/th/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI จำชื่อของคุณจากตอนต้นของการสนทนา*

ทั้งสองแผงใช้โมเดล GPT-5.2 เดียวกัน ความต่างคือหน่วยความจำ สิ่งนี้ทำให้เห็นชัดว่า memory ช่วยแอปพลิเคชันของคุณอย่างไรและทำไมจำเป็นสำหรับกรณีใช้งานจริง

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [02-prompt-engineering - การออกแบบพรอมต์กับ GPT-5.2](../02-prompt-engineering/README.md)

---

**นำทาง:** [← ก่อนหน้า: Module 00 - Quick Start](../00-quick-start/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) ถึงแม้เราจะพยายามให้ถูกต้องเท่าที่ทำได้ กรุณาทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่มีความสำคัญ ขอแนะนำให้ใช้บริการแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดใดๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->