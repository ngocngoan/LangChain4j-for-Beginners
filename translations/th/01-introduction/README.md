# Module 01: เริ่มต้นใช้งาน LangChain4j

## สารบัญ

- [วิดีโอสาธิต](../../../01-introduction)
- [สิ่งที่คุณจะได้เรียนรู้](../../../01-introduction)
- [ข้อกำหนดเบื้องต้น](../../../01-introduction)
- [เข้าใจปัญหาหลัก](../../../01-introduction)
- [ทำความเข้าใจเกี่ยวกับโทเค็น](../../../01-introduction)
- [วิธีการทำงานของหน่วยความจำ](../../../01-introduction)
- [วิธีที่ใช้ LangChain4j](../../../01-introduction)
- [ดีพลอยโครงสร้างพื้นฐาน Azure OpenAI](../../../01-introduction)
- [รันแอปพลิเคชันในเครื่อง](../../../01-introduction)
- [การใช้งานแอปพลิเคชัน](../../../01-introduction)
  - [แชทแบบไม่มีสถานะ (แผงซ้าย)](../../../01-introduction)
  - [แชทแบบมีสถานะ (แผงขวา)](../../../01-introduction)
- [ขั้นตอนถัดไป](../../../01-introduction)

## วิดีโอสาธิต

ชมเซสชันถ่ายทอดสดนี้ซึ่งอธิบายวิธีเริ่มต้นใช้งานโมดูลนี้:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## สิ่งที่คุณจะได้เรียนรู้

ใน Quick Start คุณใช้ GitHub Models เพื่อส่ง prompt, เรียกใช้เครื่องมือ, สร้าง pipeline RAG และทดสอบ guardrails ตัวอย่างเหล่านั้นแสดงให้เห็นว่าสิ่งใดเป็นไปได้ — ตอนนี้เราจะเปลี่ยนมาใช้ Azure OpenAI และ GPT-5.2 และเริ่มสร้างแอปพลิเคชันสไตล์ production โมดูลนี้เน้นด้าน AI การสนทนาที่จำบริบทและรักษาสถานะ — แนวคิดที่ตัวอย่าง Quick Start ใช้เบื้องหลังแต่ไม่ได้อธิบาย

เราจะใช้ GPT-5.2 ของ Azure OpenAI ตลอดคำแนะนำนี้เพราะความสามารถในการเข้าเหตุผลขั้นสูงทำให้พฤติกรรมของแต่ละรูปแบบชัดเจนขึ้น เมื่อคุณเพิ่มหน่วยความจำ คุณจะมองเห็นความแตกต่างได้ชัดเจน ซึ่งช่วยให้เข้าใจว่าส่วนประกอบแต่ละอย่างนำอะไรมาให้แอปพลิเคชันของคุณ

คุณจะสร้างแอปพลิเคชันหนึ่งที่สาธิตทั้งสองรูปแบบ:

**แชทไม่มีสถานะ** - ทุกคำขอเป็นอิสระ โมเดลไม่มีความทรงจำของข้อความก่อนหน้า นี่คือรูปแบบที่คุณใช้ใน quick start

**การสนทนามีสถานะ** - ทุกคำขอรวมประวัติการสนทนา โมเดลรักษาบริบทตลอดหลายรอบ นี่คือสิ่งที่แอปพลิเคชันใน production ต้องการ

## ข้อกำหนดเบื้องต้น

- บัญชี Azure พร้อมสิทธิ์เข้าถึง Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **หมายเหตุ:** Java, Maven, Azure CLI และ Azure Developer CLI (azd) ติดตั้งไว้ล่วงหน้าแล้วใน devcontainer ที่เตรียมไว้ให้

> **หมายเหตุ:** โมดูลนี้ใช้ GPT-5.2 บน Azure OpenAI การดีพลอยตั้งค่าให้อัตโนมัติผ่านคำสั่ง `azd up` - ห้ามแก้ไขชื่อโมเดลในโค้ด

## เข้าใจปัญหาหลัก

โมเดลภาษามีสถานะเป็น Stateless แต่ละคำขอ API เป็นอิสระกัน หากคุณส่ง "My name is John" แล้วถามว่า "What's my name?" โมเดลจะไม่รู้ว่าคุณเพิ่งแนะนำตัว เพราะถือว่าแต่ละคำขอเป็นบทสนทนาแรกครั้งเสมอ

สิ่งนี้เหมาะสำหรับ Q&A ง่าย ๆ แต่ไร้ประโยชน์สำหรับแอปพลิเคชันจริง ๆ บอทบริการลูกค้าต้องจำสิ่งที่คุณบอก ผู้ช่วยส่วนตัวต้องมีบริบท การสนทนาหลายรอบต้องการหน่วยความจำ

แผนภาพนี้เปรียบเทียบทั้งสองแนวทาง — ด้านซ้าย คือการเรียกแบบ stateless ที่ลืมชื่อคุณ; ด้านขวา คือการเรียกแบบ stateful โดยใช้ ChatMemory ที่จำชื่อคุณได้

<img src="../../../translated_images/th/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ความแตกต่างระหว่างการสนทนาแบบ stateless (เรียกอิสระ) และ stateful (มีบริบท)*

## ทำความเข้าใจเกี่ยวกับโทเค็น

ก่อนเข้าสู่การสนทนา จำเป็นต้องเข้าใจโทเค็น - หน่วยพื้นฐานของข้อความที่โมเดลภาษาประมวลผล:

<img src="../../../translated_images/th/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ตัวอย่างการแบ่งข้อความออกเป็นโทเค็น - "I love AI!" แยกเป็นหน่วยประมวลผล 4 หน่วย*

โทเค็นคือวิธีที่โมเดล AI วัดและประมวลผลข้อความ คำ, เครื่องหมายวรรคตอน และช่องว่างสามารถเป็นโทเค็นได้ โมเดลแต่ละตัวมีขีดจำกัดจำนวนโทเค็นที่ประมวลผลได้พร้อมกัน (400,000 สำหรับ GPT-5.2 โดยรับเข้าสูงสุด 272,000 โทเค็น และส่งออกสูงสุด 128,000 โทเค็น) การเข้าใจโทเค็นช่วยคุณจัดการความยาวการสนทนาและค่าใช้จ่ายได้

## วิธีการทำงานของหน่วยความจำ

หน่วยความจำแชทแก้ปัญหา stateless โดยเก็บประวัติการสนทนา ก่อนส่งคำขอ คุณสมบัติใน framework จะเพิ่มข้อความก่อนหน้าที่เกี่ยวข้องเข้าไป เมื่อคุณถามว่า "What's my name?" ระบบจะส่งประวัติการสนทนาทั้งหมด ช่วยให้โมเดลเห็นว่าคุณเคยกล่าว "My name is John."

LangChain4j มี implementation ของหน่วยความจำให้จัดการเรื่องนี้โดยอัตโนมัติ คุณเลือกจำนวนข้อความที่จะเก็บ และ framework จะจัดการหน้าต่างบริบท แผนภาพด้านล่างแสดงการทำงานของ MessageWindowChatMemory ที่รักษาหน้าต่างเลื่อนของข้อความล่าสุด

<img src="../../../translated_images/th/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory รักษาหน้าต่างเลื่อนของข้อความล่าสุดโดยอัตโนมัติ ลบข้อความเก่าเมื่อมีข้อความใหม่เข้ามา*

## วิธีที่ใช้ LangChain4j

โมดูลนี้ขยาย quick start โดยรวม Spring Boot และเพิ่มหน่วยความจำการสนทนา นี่คือการประกอบส่วนต่าง ๆ:

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

บิวเดอร์อ่านข้อมูลรับรองจาก environment variables ที่ตั้งโดยคำสั่ง `azd up` การตั้งค่า `baseUrl` เป็น endpoint ของ Azure ของคุณจะทำให้ OpenAI client ทำงานกับ Azure OpenAI ได้

**หน่วยความจำการสนทนา** - ติดตามประวัติแชทด้วย MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

สร้างหน่วยความจำด้วย `withMaxMessages(10)` เพื่อเก็บข้อความล่าสุด 10 ข้อความ เพิ่มข้อความผู้ใช้และ AI ด้วยตัวห่อ typed wrappers: `UserMessage.from(text)` และ `AiMessage.from(text)` รับประวัติด้วย `memory.messages()` แล้วส่งไปยังโมเดล บริการนี้เก็บหน่วยความจำแยกต่างหากตาม ID ของการสนทนา ทำให้ผู้ใช้หลายคนแชทได้พร้อมกัน

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) แล้วถาม:
> - "MessageWindowChatMemory ตัดสินใจอย่างไรว่าจะลบข้อความไหนเมื่อหน้าต่างเต็ม?"
> - "ฉันจะเก็บหน่วยความจำแบบกำหนดเองโดยใช้ฐานข้อมูลแทนใน-memory ได้ไหม?"
> - "ฉันจะเพิ่มการสรุปข้อความเพื่อบีบอัดประวัติการสนทนาเก่าได้อย่างไร?"

จุดสิ้นสุดแชทแบบ stateless ข้ามการใช้หน่วยความจำเลย - เพียงแค่ `chatModel.chat(prompt)` เหมือน quick start ส่วน stateful เพิ่มข้อความในหน่วยความจำ, ดึงประวัติ และผนวกบริบทนั้นกับแต่ละคำขอ การกำหนดค่าโมเดลเหมือนกัน แต่แพทเทิร์นต่างกัน

## ดีพลอยโครงสร้างพื้นฐาน Azure OpenAI

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

> **หมายเหตุ:** หากพบข้อผิดพลาด timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ให้รันคำสั่ง `azd up` อีกครั้ง ได้โปรดทราบว่า Azure อาจยังอยู่ระหว่าง provisioning ในเบื้องหลัง การลองใหม่จะช่วยให้ดีพลอยเสร็จสมบูรณ์เมื่อ resources อยู่ในสถานะสุดท้าย

ขั้นตอนจะ:
1. ดีพลอย Azure OpenAI resource กับโมเดล GPT-5.2 และ text-embedding-3-small
2. สร้างไฟล์ `.env` อัตโนมัติในโฟลเดอร์โปรเจกต์ด้วยข้อมูลรับรอง
3. ตั้งค่าสิ่งแวดล้อมที่จำเป็นทั้งหมด

**ประสบปัญหาการดีพลอย?** ดู [Infrastructure README](infra/README.md) สำหรับการแก้ไขปัญหาละเอียด รวมถึงปัญหาชื่อโดเมนย่อย, ขั้นตอนการดีพลอยผ่าน Azure Portal ด้วยตนเอง, และคำแนะนำการตั้งค่าโมเดล

**ตรวจสอบว่า deployment สำเร็จ:**

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, เป็นต้น
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, เป็นต้น
```

> **หมายเหตุ:** คำสั่ง `azd up` สร้างไฟล์ `.env` ให้อัตโนมัติ หากคุณต้องการแก้ไขภายหลัง สามารถแก้ไขไฟล์ `.env` ด้วยมือ หรือสร้างไฟล์ใหม่โดยรัน:
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

**ตรวจสอบ deployment:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` อยู่ในโฟลเดอร์หลักและมีข้อมูลรับรอง Azure รันคำสั่งนี้จากโฟลเดอร์โมดูล (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มต้นแอปพลิเคชัน:**

**ตัวเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

dev container มี extension Spring Boot Dashboard ซึ่งให้หน้าต่าง UI สำหรับจัดการแอป Spring Boot ทั้งหมดใน workspace คุณจะเจอมันใน Activity Bar ด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอป Spring Boot ทั้งหมดที่พร้อมใช้งานใน workspace
- สั่งเริ่ม/หยุดแอปด้วยการคลิกเดียว
- ดูล็อกแอปแบบเรียลไทม์
- ตรวจสอบสถานะของแอป

คลิกปุ่มเล่นข้างๆ “introduction” เพื่อเริ่มโมดูลนี้ หรือสั่งเริ่มทุกโมดูลพร้อมกัน

<img src="../../../translated_images/th/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard ใน VS Code — เริ่ม หยุด และตรวจสอบโมดูลทั้งหมดจากที่เดียว*

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มแอปเว็บทั้งหมด (โมดูล 01-04):

**Bash:**
```bash
cd ..  # จากไดเรกทอรีราก
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # จากไดเรกทอรีหลัก
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

สคริปต์ทั้งสองโหลด environment variables จากไฟล์ `.env` ที่โฟลเดอร์หลักโดยอัตโนมัติ และจะ build JAR หากยังไม่มีไฟล์

> **หมายเหตุ:** หากต้องการ build โมดูลทั้งหมดด้วยมือก่อนรัน:
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

เปิดเบราว์เซอร์ที่ http://localhost:8080

**เพื่อหยุด:**

**Bash:**
```bash
./stop.sh  # เฉพาะโมดูลนี้
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # โมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## การใช้งานแอปพลิเคชัน

แอปมีอินเทอร์เฟซเว็บที่มีแชทสองรูปแบบอยู่เคียงข้างกัน

<img src="../../../translated_images/th/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*แดชบอร์ดแสดงตัวเลือกทั้ง Simple Chat (แบบ stateless) และ Conversational Chat (แบบ stateful)*

### แชทแบบไม่มีสถานะ (แผงซ้าย)

ลองใช้แบบนี้ก่อน ถามว่า "My name is John" แล้วถามทันทีว่า "What's my name?" โมเดลจะไม่จำเพราะแต่ละข้อความเป็นอิสระ นี่แสดงปัญหาหลักของการใช้โมเดลภาษาง่าย ๆ — ไม่มีบริบทของการสนทนา

<img src="../../../translated_images/th/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI จะไม่จำชื่อคุณจากข้อความก่อนหน้า*

### แชทแบบมีสถานะ (แผงขวา)

คราวนี้ลองชุดคำถามเดียวกันที่นี่ ถามว่า "My name is John" แล้วถาม "What's my name?" โมเดลจะจำได้ ความแตกต่างคือ MessageWindowChatMemory ที่รักษาประวัติการสนทนาและแนบร่วมกับทุกคำขอ นี่คือวิธีที่ AI สนทนาใน production ทำงาน

<img src="../../../translated_images/th/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI จำชื่อคุณจากการสนทนาในช่วงก่อนหน้า*

ทั้งสองแผงใช้โมเดล GPT-5.2 ตัวเดียว ความแตกต่างคือหน่วยความจำ ซึ่งทำให้เห็นชัดเจนว่าหน่วยความจำช่วยแอปพลิเคชันอย่างไรและเหตุใดจึงจำเป็นสำหรับใช้งานจริง

## ขั้นตอนถัดไป

**โมดูลต่อไป:** [02-prompt-engineering - การสร้าง Prompt กับ GPT-5.2](../02-prompt-engineering/README.md)

---

**การนำทาง:** [← ก่อนหน้า: Module 00 - Quick Start](../00-quick-start/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**คำปฏิเสธความรับผิด**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางถือเป็นแหล่งข้อมูลที่ถูกต้องและเชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้การแปลโดยผู้เชี่ยวชาญมนุษย์เป็นหลัก เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->