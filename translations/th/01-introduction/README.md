# Module 01: การเริ่มต้นใช้งาน LangChain4j

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../01-introduction)
- [ข้อกำหนดเบื้องต้น](../../../01-introduction)
- [ทำความเข้าใจปัญหาหลัก](../../../01-introduction)
- [ทำความเข้าใจโทเค็น](../../../01-introduction)
- [การทำงานของหน่วยความจำ](../../../01-introduction)
- [วิธีที่โมดูลนี้ใช้ LangChain4j](../../../01-introduction)
- [ปรับใช้โครงสร้างพื้นฐาน Azure OpenAI](../../../01-introduction)
- [รันแอปพลิเคชันในเครื่อง](../../../01-introduction)
- [การใช้งานแอปพลิเคชัน](../../../01-introduction)
  - [แชทแบบไม่มีสถานะ (แผงซ้าย)](../../../01-introduction)
  - [แชทแบบมีสถานะ (แผงขวา)](../../../01-introduction)
- [ขั้นตอนถัดไป](../../../01-introduction)

## สิ่งที่คุณจะได้เรียนรู้

หากคุณได้ทำตามการเริ่มต้นอย่างรวดเร็วแล้ว คุณจะได้เห็นวิธีส่งพรอมต์และรับคำตอบ นั่นคือฐานราก แต่แอปพลิเคชันจริงต้องการมากกว่านั้น โมดูลนี้จะสอนวิธีสร้าง AI สนทนาที่จำบริบทและรักษาสถานะ ต่างจากการสาธิตครั้งเดียวกับแอปที่จะพร้อมทำงานจริง

เราจะใช้ GPT-5.2 ของ Azure OpenAI ตลอดคู่มือนี้ เพราะความสามารถการให้เหตุผลขั้นสูงช่วยให้พฤติกรรมของรูปแบบต่าง ๆ ชัดเจนยิ่งขึ้น เมื่อเพิ่มหน่วยความจำ คุณจะเห็นความแตกต่างได้ชัดเจนขึ้น ซึ่งทำให้เข้าใจได้ง่ายว่าแต่ละองค์ประกอบช่วยอะไรกับแอปของคุณ

คุณจะสร้างแอปเดียวที่สาธิตทั้งสองรูปแบบ:

**แชทแบบไม่มีสถานะ** – แต่ละคำขอแยกกัน โมเดลไม่มีหน่วยความจำเกี่ยวกับข้อความก่อนหน้า นี่คือรูปแบบที่ใช้ในขั้นตอนเริ่มต้นอย่างรวดเร็ว

**สนทนาแบบมีสถานะ** – แต่ละคำขอรวมประวัติการสนทนา โมเดลรักษาบริบทข้ามรอบต่าง ๆ นี่คือสิ่งที่แอปพลิเคชันผลิตจริงต้องการ

## ข้อกำหนดเบื้องต้น

- บัญชีผู้ใช้ Azure ที่สามารถเข้าใช้ Azure OpenAI ได้
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI และ Azure Developer CLI (azd) ติดตั้งมาแล้วใน devcontainer ที่จัดเตรียมไว้ให้

> **Note:** โมดูลนี้ใช้ GPT-5.2 บน Azure OpenAI การปรับใช้นี้ตั้งค่าอัตโนมัติผ่านคำสั่ง `azd up` – อย่าแก้ไขชื่อโมเดลในโค้ด

## ทำความเข้าใจปัญหาหลัก

โมเดลภาษาไม่มีสถานะ การเรียกใช้งาน API แต่ละครั้งแยกจากกัน หากคุณส่งข้อความ "ชื่อของฉันคือ John" แล้วถาม "ชื่อของฉันคืออะไร?" โมเดลจะไม่รู้ว่าคุณเพิ่งแนะนำตัว เพราะถือว่าทุกคำขอเป็นการสนทนาแรกเสมอ

นี่ใช้ได้ดีกับคำถามง่าย ๆ แต่ไม่ใช่กับแอปจริง ๆ บอทบริการลูกค้าต้องจดจำสิ่งที่คุณบอกผู้ช่วยส่วนตัวต้องมีบริบท การสนทนาหลายรอบต้องมีหน่วยความจำ

<img src="../../../translated_images/th/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ความแตกต่างระหว่างการสนทนาแบบไม่มีสถานะ (การเรียกแยกกัน) กับแบบมีสถานะ (รู้บริบท)*

## ทำความเข้าใจโทเค็น

ก่อนเข้าสู่การสนทนา จำเป็นต้องเข้าใจโทเค็น – หน่วยพื้นฐานของข้อความที่โมเดลภาษาจะประมวลผล:

<img src="../../../translated_images/th/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ตัวอย่างการแบ่งข้อความเป็นโทเค็น – "I love AI!" กลายเป็นหน่วยประมวลผลแยก 4 หน่วย*

โทเค็นคือหน่วยที่โมเดล AI ใช้วัดและประมวลผลข้อความ คำ เครื่องหมายวรรคตอน และแม้แต่ช่องว่างก็เป็นโทเค็นได้ โมเดลของคุณมีขีดจำกัดจำนวนโทเค็นที่ประมวลผลในครั้งเดียว (400,000 สำหรับ GPT-5.2 โดยมีสูงสุด 272,000 โทเค็นนำเข้า และ 128,000 โทเค็นผลลัพธ์) การเข้าใจโทเค็นช่วยจัดการความยาวและค่าใช้จ่ายของการสนทนา

## การทำงานของหน่วยความจำ

หน่วยความจำแชทแก้ปัญหาโมเดลที่ไม่มีสถานะโดยเก็บประวัติการสนทนา ก่อนส่งคำขอไปยังโมเดล โครงสร้างจะเพิ่มข้อความที่เกี่ยวข้องก่อนหน้าเข้ามา เมื่อคุณถาม "ชื่อของฉันคืออะไร?" ระบบจะส่งประวัติการสนทนาทั้งหมด ทำให้โมเดลเห็นว่าคุณเพิ่งบอกว่า "ชื่อของฉันคือ John"

LangChain4j มีการใช้งานหน่วยความจำที่จัดการนี้ให้อัตโนมัติ คุณเลือกจำนวนข้อความที่จะเก็บไว้ และโครงสร้างจะดูแลบริบทเอง

<img src="../../../translated_images/th/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory คงหน่วยความจำข้อความล่าสุดในหน้าต่างเลื่อนอัตโนมัติ โดยลบข้อความเก่า ๆ ออก*

## วิธีที่โมดูลนี้ใช้ LangChain4j

โมดูลนี้ขยายการเริ่มต้นใช้งานด้วยการรวม Spring Boot และเพิ่มหน่วยความจำสนทนา ต่อไปนี้เป็นวิธีการทำงานร่วมกันของส่วนประกอบ:

**Dependencies** – เพิ่มไลบรารี LangChain4j สองตัว:

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

**Chat Model** – กำหนดค่า Azure OpenAI เป็น Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

ตัวสร้างอ่านข้อมูลรับรองจากตัวแปรสภาพแวดล้อมที่ตั้งไว้โดย `azd up` การตั้ง `baseUrl` เป็นจุดสิ้นสุด Azure ของคุณทำให้ไคลเอนต์ OpenAI ทำงานกับ Azure OpenAI ได้

**หน่วยความจำการสนทนา** – ติดตามประวัติแชทด้วย MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

สร้างหน่วยความจำด้วย `withMaxMessages(10)` เพื่อเก็บข้อความล่าสุด 10 ข้อความ เพิ่มข้อความของผู้ใช้และ AI ด้วยตัวห่อชนิด: `UserMessage.from(text)` และ `AiMessage.from(text)` ดึงประวัติด้วย `memory.messages()` แล้วส่งไปยังโมเดล บริการเก็บหน่วยความจำแยกตาม ID การสนทนา เพื่อให้ผู้ใช้หลายคนแชทพร้อมกันได้

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) และถาม:
> - "MessageWindowChatMemory เลือกข้อความไหนที่จะลบเมื่อหน้าต่างเต็มอย่างไร?"
> - "ฉันจะสร้างที่เก็บหน่วยความจำแบบกำหนดเองโดยใช้ฐานข้อมูลแทนในหน่วยความจำได้หรือไม่?"
> - "ฉันจะเพิ่มการสรุปย่อตอนเก่าเพื่อบีบอัดประวัติสนทนาได้อย่างไร?"

ปลายทางแชทแบบไม่มีสถานะจะข้ามหน่วยความจำโดยสิ้นเชิง – ใช้ `chatModel.chat(prompt)` เหมือนการเริ่มต้นอย่างรวดเร็ว ส่วนปลายทางแบบมีสถานะเพิ่มข้อความลงหน่วยความจำ ดึงประวัติ และส่งบริบทนั้นพร้อมคำขอ รูปแบบโมเดลเหมือนกัน แต่รูปแบบการใช้งานต่างกัน

## ปรับใช้โครงสร้างพื้นฐาน Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # เลือกการสมัครสมาชิกและสถานที่ตั้ง (แนะนำ eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # เลือกการสมัครใช้งานและตำแหน่งที่ตั้ง (แนะนำ eastus2)
```

> **Note:** หากเจอข้อผิดพลาดหมดเวลา (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ให้รัน `azd up` อีกครั้ง ส่วนทรัพยากร Azure อาจยังอยู่ระหว่างการปรับใช้ และการลองใหม่จะทำให้การปรับใช้สำเร็จเมื่อทรัพยากรเข้าถึงสถานะสุดท้าย

การทำเช่นนี้จะ:
1. ปรับใช้ทรัพยากร Azure OpenAI พร้อมโมเดล GPT-5.2 และ text-embedding-3-small
2. สร้างไฟล์ `.env` ในรากโปรเจกต์อัตโนมัติพร้อมข้อมูลรับรอง
3. ตั้งค่าตัวแปรสภาพแวดล้อมที่ต้องการทั้งหมด

**หากพบปัญหาการปรับใช้?** ดู [Infrastructure README](infra/README.md) สำหรับการแก้ไขปัญหาโดยละเอียด รวมถึงความขัดแย้งของชื่อโดเมนย่อย ขั้นตอนปรับใช้ใน Azure Portal ด้วยตนเอง และคำแนะนำการตั้งค่าโมเดล

**ตรวจสอบการปรับใช้สำเร็จ:**

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, ฯลฯ
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, ฯลฯ
```

> **Note:** คำสั่ง `azd up` สร้างไฟล์ `.env` อัตโนมัติ หากต้องการอัปเดตทีหลัง คุณสามารถแก้ไขไฟล์ `.env` ด้วยตนเอง หรือสร้างใหม่โดยรัน:
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

ตรวจสอบว่าไฟล์ `.env` มีอยู่ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure:

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มต้นแอปพลิเคชัน:**

**ตัวเลือก 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

dev container มีส่วนขยาย Spring Boot Dashboard ที่ให้หน้าจอภาพสำหรับจัดการแอป Spring Boot ทั้งหมด คุณสามารถหาได้จาก Activity Bar ทางด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอป Spring Boot ทั้งหมดใน workspace
- เริ่ม/หยุดแอปด้วยคลิกเดียว
- ดูบันทึกแอปแบบเรียลไทม์
- ตรวจสอบสถานะแอป

เพียงคลิกปุ่มเล่นข้าง "introduction" เพื่อเริ่มโมดูลนี้ หรือเริ่มทุกโมดูลพร้อมกัน

<img src="../../../translated_images/th/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือก 2: ใช้สคริปต์ shell**

เริ่มแอปเว็บทั้งหมด (โมดูล 01-04):

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

หรือเริ่มแค่โมดูลนี้:

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

สคริปต์ทั้งสองโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ที่รากอัตโนมัติ และจะสร้าง JAR หากยังไม่มี

> **Note:** หากต้องการสร้างโมดูลทั้งหมดเองก่อนเริ่มต้น:
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

**เมื่อต้องการหยุด:**

**Bash:**
```bash
./stop.sh  # โมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # โมดูลทั้งหมด
```

**PowerShell:**
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## การใช้งานแอปพลิเคชัน

แอปพลิเคชันมีอินเทอร์เฟซเว็บพร้อมการแชทสองแบบข้างกัน

<img src="../../../translated_images/th/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*แดชบอร์ดแสดงตัวเลือกทั้ง Simple Chat (ไม่มีสถานะ) และ Conversational Chat (มีสถานะ)*

### แชทแบบไม่มีสถานะ (แผงซ้าย)

ลองก่อน ถามว่า "ชื่อของฉันคือ John" แล้วถามทันทีว่า "ชื่อของฉันคืออะไร?" โมเดลจะไม่จำเพราะแต่ละข้อความแยกกัน นี่แสดงปัญหาหลักของการเชื่อมต่อโมเดลภาษาแบบพื้นฐาน – ไม่มีบริบทการสนทนา

<img src="../../../translated_images/th/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ไม่จำชื่อของคุณจากข้อความก่อนหน้า*

### แชทแบบมีสถานะ (แผงขวา)

ตอนนี้ลองชุดคำถามเดียวกันที่นี่ ถามว่า "ชื่อของฉันคือ John" แล้วถาม "ชื่อของฉันคืออะไร?" คราวนี้จำได้ ความแตกต่างคือ MessageWindowChatMemory – มันรักษาประวัติการสนทนาและส่งให้โมเดลกับแต่ละคำขอ นี่คือวิธีที่ AI สนทนาในงานผลิตจริงทำงาน

<img src="../../../translated_images/th/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI จำชื่อของคุณจากช่วงต้นการสนทนา*

ทั้งสองแผงใช้โมเดล GPT-5.2 เดียวกัน ความแตกต่างคือหน่วยความจำ สิ่งนี้แสดงให้เห็นชัดเจนว่าหน่วยความจำช่วยแอปของคุณอย่างไร และทำไมจึงสำคัญสำหรับกรณีใช้งานจริง

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [02-prompt-engineering - การออกแบบพรอมต์กับ GPT-5.2](../02-prompt-engineering/README.md)

---

**การนำทาง:** [← ก่อนหน้า: Module 00 - เริ่มต้นอย่างรวดเร็ว](../00-quick-start/README.md) | [กลับไปหน้าหลัก](../README.md) | [ถัดไป: Module 02 - การออกแบบพรอมต์ →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:
เอกสารฉบับนี้ได้รับการแปลโดยใช้บริการแปลด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความแม่นยำสูงสุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลโดยมนุษย์ผู้เชี่ยวชาญ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดขึ้นจากการใช้การแปลฉบับนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->