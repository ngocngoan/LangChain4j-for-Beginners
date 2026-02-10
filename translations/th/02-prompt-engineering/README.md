# Module 02: การสร้างคำสั่ง (Prompt Engineering) กับ GPT-5.2

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [เข้าใจการสร้างคำสั่ง (Prompt Engineering)](../../../02-prompt-engineering)
- [การใช้งาน LangChain4j ในโมดูลนี้](../../../02-prompt-engineering)
- [รูปแบบหลัก](../../../02-prompt-engineering)
- [การใช้ทรัพยากร Azure ที่มีอยู่](../../../02-prompt-engineering)
- [ภาพหน้าจอของแอปพลิเคชัน](../../../02-prompt-engineering)
- [สำรวจรูปแบบต่าง ๆ](../../../02-prompt-engineering)
  - [ความกระตือรือร้นต่ำ vs สูง](../../../02-prompt-engineering)
  - [การดำเนินงานงาน (คำนำเครื่องมือ)](../../../02-prompt-engineering)
  - [โค้ดที่สะท้อนตนเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์เชิงโครงสร้าง](../../../02-prompt-engineering)
  - [แชทแบบหลายรอบ](../../../02-prompt-engineering)
  - [การคิดอย่างเป็นขั้นตอน](../../../02-prompt-engineering)
  - [เอาต์พุตที่ถูกจำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้อย่างแท้จริง](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## สิ่งที่คุณจะได้เรียนรู้

ในโมดูลก่อนหน้านี้ คุณเห็นว่าเมมโมรีช่วยให้ AI สนทนาทำงานได้อย่างไร และใช้ GitHub Models สำหรับการโต้ตอบพื้นฐาน ตอนนี้เราจะเน้นที่วิธีการตั้งคำถาม — คำสั่ง (prompt) เอง — โดยใช้ GPT-5.2 ของ Azure OpenAI วิธีที่คุณจัดโครงสร้างคำสั่งส่งผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ

เราจะใช้ GPT-5.2 เนื่องจากมีการควบคุมการคิด (reasoning control) — คุณสามารถบอกโมเดลว่าต้องใช้การคิดมากน้อยเพียงใดก่อนตอบ นี่ทำให้กลยุทธ์ต่าง ๆ ในการสร้างคำสั่งชัดเจนขึ้นและช่วยให้คุณเข้าใจว่าเมื่อใดควรใช้แต่ละวิธี เรายังได้ประโยชน์จากข้อจำกัดอัตราการใช้งานที่น้อยลงของ Azure สำหรับ GPT-5.2 เมื่อเทียบกับ GitHub Models

## ข้อกำหนดเบื้องต้น

- ผ่านโมดูล 01 แล้ว (ติดตั้งทรัพยากร Azure OpenAI เรียบร้อยแล้ว)
- ไฟล์ `.env` ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure (สร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** ถ้าคุณยังไม่ผ่านโมดูล 01 โปรดทำตามคำแนะนำการติดตั้งที่นั่นก่อน

## เข้าใจการสร้างคำสั่ง (Prompt Engineering)

การสร้างคำสั่ง คือการออกแบบข้อความนำเข้าที่ทำให้ได้ผลลัพธ์ที่คุณต้องการอย่างสม่ำเสมอ มันไม่ใช่แค่การตั้งคำถาม — แต่มันคือการจัดโครงสร้างคำขอเพื่อให้โมเดลเข้าใจอย่างชัดเจนว่าคุณต้องการอะไรและจะส่งมอบอย่างไร

คิดเหมือนการให้คำสั่งกับเพื่อนร่วมงาน "แก้ไขบั๊ก" ฟังดูไม่ชัดเจน แต่ "แก้ไข Null Pointer Exception ใน UserService.java บรรทัดที่ 45 โดยเพิ่มการตรวจสอบค่าว่าง" นั้นเจาะจงชัดเจน โมเดลภาษาก็ทำงานเช่นเดียวกัน — ความเฉพาะเจาะจงและโครงสร้างสำคัญมาก

## การใช้งาน LangChain4j ในโมดูลนี้

โมดูลนี้แสดงรูปแบบการสร้างคำสั่งขั้นสูงโดยใช้ฐาน LangChain4j เดียวกับโมดูลก่อนหน้า โฟกัสที่โครงสร้างคำสั่งและการควบคุมการคิด

<img src="../../../translated_images/th/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*วิธีที่ LangChain4j เชื่อมต่อคำสั่งของคุณกับ Azure OpenAI GPT-5.2*

**การพึ่งพา** – โมดูล 02 ใช้ไลบรารี langchain4j ที่ระบุใน `pom.xml` ดังนี้:
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

**การตั้งค่า OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

โมเดลสนทนาได้รับการตั้งค่าเองเป็น Spring bean โดยใช้ไคลเอนต์ OpenAI Official ที่รองรับจุดสิ้นสุด Azure OpenAI ความแตกต่างหลักจากโมดูล 01 คือวิธีการจัดโครงสร้างคำสั่งที่ส่งให้ `chatModel.chat()` ไม่ใช่การตั้งค่าโมเดลเอง

**ข้อความระบบและข้อความผู้ใช้** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j แยกประเภทข้อความเพื่อความชัดเจน `SystemMessage` เป็นการตั้งพฤติกรรมและบริบทของ AI (เช่น "คุณคือผู้ตรวจสอบโค้ด") ขณะที่ `UserMessage` คือคำขอจริง การแยกนี้ช่วยให้คุณรักษาพฤติกรรม AI ที่สอดคล้องกันในหลายคำถามของผู้ใช้

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/th/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage ให้บริบทที่จะคงอยู่ ส่วน UserMessages คือคำขอแต่ละรายการ*

**MessageWindowChatMemory สำหรับการสนทนาแบบหลายรอบ** – สำหรับรูปแบบการสนทนาแบบหลายรอบ เรานำ `MessageWindowChatMemory` จากโมดูล 01 มาใช้ใหม่ แต่ละเซสชันจะมีเมมโมรีของตัวเองจัดเก็บใน `Map<String, ChatMemory>` รองรับหลายการสนทนาพร้อมกันโดยไม่สับสนบริบท

**แม่แบบคำสั่ง (Prompt Templates)** – จุดสนใจหลักคือการสร้างคำสั่ง ไม่ใช่ API ใหม่ของ LangChain4j รูปแบบแต่ละแบบ (ความกระตือรือร้นต่ำ สูง งาน เป็นต้น) ใช้วิธี `chatModel.chat(prompt)` เดียวกัน แต่จัดคำสั่งอย่างละเอียด XML tag, คำแนะนำ และรูปแบบทั้งหมดเป็นส่วนหนึ่งของข้อความคำสั่ง ไม่ใช่ฟีเจอร์ของ LangChain4j

**การควบคุมการคิด (Reasoning Control)** – ความพยายามของ GPT-5.2 ในการคิดถูกควบคุมผ่านคำแนะนำคำสั่ง เช่น "สูงสุด 2 ขั้นตอนการคิด" หรือ "สำรวจอย่างละเอียด" เหล่านี้คือเทคนิคการสร้างคำสั่ง ไม่ใช่การตั้งค่าของ LangChain4j ไลบรารีเพียงแค่ส่งคำสั่งของคุณให้โมเดลเท่านั้น

ข้อสรุปสำคัญ: LangChain4j ให้โครงสร้างพื้นฐาน (การเชื่อมต่อโมเดลผ่าน [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), เมมโมรี, การจัดการข้อความผ่าน [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)) ส่วนโมดูลนี้สอนวิธีสร้างคำสั่งที่มีประสิทธิภาพภายในโครงสร้างพื้นฐานนั้น

## รูปแบบหลัก

ไม่ใช่ทุกปัญหาที่ต้องใช้วิธีเดียว คำถามบางอย่างต้องการคำตอบเร็ว ๆ บางคำถามต้องการความคิดลึกซึ้ง บางคำถามต้องการแสดงเหตุผลชัดเจน บางคำถามแค่ต้องการผลลัพธ์ โมดูลนี้ครอบคลุม 8 รูปแบบการสร้างคำสั่ง — แต่ละรูปแบบเหมาะสมกับสถานการณ์ต่างกัน คุณจะได้ทดลองทั้งหมดเพื่อเรียนรู้ว่าเมื่อใดแต่ละรูปแบบเหมาะสมที่สุด

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ภาพรวมของ 8 รูปแบบการสร้างคำสั่งและกรณีการใช้งาน*

<img src="../../../translated_images/th/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*ความกระตือรือร้นต่ำ (เร็ว ตรงไปตรงมา) vs ความกระตือรือร้นสูง (ละเอียด รอบคอบ) วิธีการคิด*

**ความกระตือรือร้นต่ำ (เร็วและเน้นประเด็น)** – สำหรับคำถามง่าย ๆ ที่ต้องการคำตอบเร็วและตรงประเด็น โมเดลจะคิดน้อย — สูงสุด 2 ขั้นตอน ใช้สำหรับการคำนวณ, ค้นหา หรือคำถามตรง ๆ

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **ลองใช้กับ GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) แล้วถาม:
> - "ความแตกต่างระหว่างรูปแบบสร้างคำสั่งแบบความกระตือรือร้นต่ำกับสูงคืออะไร?"
> - "แท็ก XML ในคำสั่งช่วยโครงสร้างการตอบของ AI อย่างไร?"
> - "เมื่อไรควรใช้รูปแบบสะท้อนตนเองกับคําสั่งโดยตรง?"

**ความกระตือรือร้นสูง (ลึกซึ้งและละเอียด)** – สำหรับปัญหาซับซ้อนที่ต้องการการวิเคราะห์เชิงลึก โมเดลจะสำรวจละเอียดและแสดงเหตุผลอย่างละเอียด ใช้สำหรับออกแบบระบบ, การตัดสินใจสถาปัตยกรรม หรือการวิจัยซับซ้อน

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินงานงาน (ความคืบหน้าเป็นขั้นตอนทีละขั้น)** – สำหรับการทำงานหลายขั้นตอน โมเดลวางแผนล่วงหน้า, เล่าเหตุการณ์ของแต่ละขั้นตอนระหว่างทำงาน และสรุปท้าย ใช้สำหรับการย้ายข้อมูล, การปรับใช้งาน หรือกระบวนการหลายขั้นตอนใด ๆ

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

การสร้างคำสั่งแบบ Chain-of-Thought ขอให้โมเดลแสดงกระบวนการคิดของมันชัดเจน ช่วยเพิ่มความแม่นยำสำหรับงานซับซ้อน การแยกขั้นตอนทำช่วยให้มนุษย์และ AI เข้าใจตรรกะได้ดีขึ้น

> **🤖 ทดลองถามกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับรูปแบบนี้:
> - "ฉันจะปรับรูปแบบการดำเนินงานงานสำหรับกระบวนการที่ใช้เวลานานอย่างไร?"
> - "แนวทางปฏิบัติที่ดีที่สุดสำหรับการจัดโครงสร้างคำนำเครื่องมือในแอปพลิเคชันโปรดักชันคืออะไร?"
> - "ฉันจะจับและแสดงความคืบหน้าระหว่างขั้นตอนใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*กระบวนการ วางแผน → ดำเนินการ → สรุป สำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตนเอง** – สำหรับการสร้างโค้ดคุณภาพการผลิต โมเดลจะสร้างโค้ด, ตรวจสอบตามเกณฑ์คุณภาพ, และปรับปรุงซ้ำ ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*วงจรปรับปรุงแบบทำซ้ำ — สร้าง, ประเมิน, ระบุปัญหา, ปรับปรุง, ทำซ้ำ*

**การวิเคราะห์เชิงโครงสร้าง** – สำหรับการประเมินที่สม่ำเสมอ โมเดลตรวจสอบโค้ดโดยใช้กรอบงานที่กำหนดไว้ล่วงหน้า (ความถูกต้อง, แนวทางปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) ใช้สำหรับรีวิวโค้ดหรือประเมินคุณภาพ

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 ทดลองถามกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับการวิเคราะห์เชิงโครงสร้าง:
> - "ฉันจะปรับกรอบการวิเคราะห์สำหรับรีวิวโค้ดประเภทต่าง ๆ ได้อย่างไร?"
> - "วิธีที่ดีที่สุดในการแยกแยะและทำงานกับเอาต์พุตเชิงโครงสร้างอย่างเป็นโปรแกรมคืออะไร?"
> - "ฉันจะทำอย่างไรให้ระดับความรุนแรงสอดคล้องกันในหลายเซสชันรีวิว?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*กรอบงาน 4 หมวดหมู่สำหรับการรีวิวโค้ดที่สม่ำเสมอพร้อมระดับความรุนแรง*

**แชทแบบหลายรอบ** – สำหรับการสนทนาที่ต้องการบริบท โมเดลจำข้อความก่อนหน้าและต่อยอด ใช้สำหรับช่วงช่วยเหลือแบบโต้ตอบหรือถามตอบที่ซับซ้อน

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/th/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*วิธีเก็บบริบทการสนทนาในหลายรอบจนถึงขีดจำกัด token*

**การคิดอย่างเป็นขั้นตอน** – สำหรับปัญหาที่ต้องการตรรกะชัดเจน โมเดลแสดงเหตุผลอย่างชัดเจนสำหรับแต่ละขั้นตอน ใช้สำหรับปัญหาคณิตศาสตร์, ปริศนาเชิงตรรกะ หรือเมื่อคุณต้องเข้าใจกระบวนการคิด

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*การแยกปัญหาออกเป็นขั้นตอนตรรกะที่ชัดเจน*

**เอาต์พุตที่ถูกจำกัด** – สำหรับคำตอบที่ต้องการรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเข้มงวด ใช้สำหรับสรุปหรือเมื่อคุณต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*บังคับใช้รูปแบบ ความยาว และโครงสร้างที่เฉพาะเจาะจง*

## การใช้ทรัพยากร Azure ที่มีอยู่

**ตรวจสอบการติดตั้ง:**

ตรวจสอบว่าไฟล์ `.env` อยู่ในไดเรกทอรีรากและมีข้อมูลรับรอง Azure (สร้างในโมดูล 01):
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มแอปพลิเคชันทั้งหมดด้วย `./start-all.sh` จากโมดูล 01 แล้ว โมดูลนี้จะทำงานอยู่ที่พอร์ต 8083 คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่างและไปที่ http://localhost:8083 ได้เลย

**ตัวเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

คอนเทนเนอร์ dev รวม Extension Spring Boot Dashboard ที่ให้หน้าต่างใช้งานแบบกราฟิกจัดการแอป Spring Boot ทั้งหมด คุณจะพบได้ที่ Activity Bar ด้านซ้ายของ VS Code (มองหาสัญลักษณ์ Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอป Spring Boot ทั้งหมดใน workspace
- เริ่ม/หยุดแอปด้วยคลิกเดียว
- ดูล็อกแอปแบบเรียลไทม์
- ตรวจสอบสถานะแอป

คลิกปุ่มเล่นข้าง "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มทุกโมดูลพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มแอปเว็บทั้งหมด (โมดูล 01-04):

**Bash:**
```bash
cd ..  # จากไดเรกทอรีรูท
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # จากไดเรกทอรีราก
.\start-all.ps1
```

หรือเริ่มแค่โมดูลนี้เท่านั้น:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

ทั้งสองสคริปต์จะโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ในโฟลเดอร์รากโดยอัตโนมัติ และจะสร้าง JAR หากยังไม่มี

> **หมายเหตุ:** หากคุณต้องการคอมไพล์โมดูลทั้งหมดด้วยตนเองก่อนเริ่ม:
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

เปิด http://localhost:8083 ในเบราว์เซอร์ของคุณ

**หยุดแอปพลิเคชัน:**

**Bash:**
```bash
./stop.sh  # เฉพาะโมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # โมดูลทั้งหมด
```

**PowerShell:**
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอของแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักแสดงทั้ง 8 รูปแบบการสร้างคำสั่งพร้อมคุณสมบัติและกรณีการใช้งาน*

## สำรวจรูปแบบต่าง ๆ

เว็บอินเทอร์เฟซจะให้คุณทดลองใช้กลยุทธ์การตั้งคำถามที่แตกต่างกัน แต่ละรูปแบบแก้ปัญหาต่างกัน — ลองใช้เพื่อดูว่าแต่ละวิธีเหมาะสมเมื่อใด

### ความกระตือรือร้นต่ำ vs สูง

ลองถามคำถามง่าย ๆ เช่น "15% ของ 200 คือเท่าไหร่?" โดยใช้ความกระตือรือร้นต่ำ คุณจะได้คำตอบทันทีตรงไปตรงมา จากนั้นถามคำถามซับซ้อนเช่น "ออกแบบกลยุทธ์แคชสำหรับ API ที่มีผู้ใช้งานสูง" โดยใช้ความกระตือรือร้นสูง แล้วสังเกตว่าโมเดลจะช้าลงและแสดงเหตุผลอย่างละเอียด รุ่นเดียวกัน โครงสร้างคำถามเหมือนกัน — แต่ prompt บอกโมเดลว่าคิดมากแค่ไหนก่อนตอบ
<img src="../../../translated_images/th/low-eagerness-demo.898894591fb23aa0.webp" alt="สาธิตความกระตือรือร้นต่ำ" width="800"/>

*การคำนวณอย่างรวดเร็วด้วยเหตุผลขั้นต่ำ*

<img src="../../../translated_images/th/high-eagerness-demo.4ac93e7786c5a376.webp" alt="สาธิตความกระตือรือร้นสูง" width="800"/>

*กลยุทธ์การแคชอย่างครอบคลุม (2.8MB)*

### การดำเนินงานของงาน (คําเกริ่นเครื่องมือ)

เวิร์กโฟลว์หลายขั้นตอนได้รับประโยชน์จากการวางแผนล่วงหน้าและการบรรยายความคืบหน้า โมเดลจะสรุปสิ่งที่จะทำ บรรยายแต่ละขั้นตอน แล้วสรุปผลลัพธ์

<img src="../../../translated_images/th/tool-preambles-demo.3ca4881e417f2e28.webp" alt="สาธิตการดำเนินงานของงาน" width="800"/>

*การสร้าง REST endpoint พร้อมการบรรยายทีละขั้นตอน (3.9MB)*

### โค้ดที่สะท้อนตนเอง

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะสร้างโค้ดแล้วหยุด โมเดลจะสร้างประเมินตามเกณฑ์คุณภาพ ระบุจุดอ่อน และปรับปรุง คุณจะเห็นมันทำซ้ำจนโค้ดเป็นไปตามมาตรฐานการผลิต

<img src="../../../translated_images/th/self-reflecting-code-demo.851ee05c988e743f.webp" alt="สาธิตโค้ดที่สะท้อนตนเอง" width="800"/>

*บริการตรวจสอบอีเมลสมบูรณ์ (5.2MB)*

### การวิเคราะห์อย่างมีโครงสร้าง

การตรวจโค้ดต้องมีกรอบการประเมินที่สม่ำเสมอ โมเดลวิเคราะห์โค้ดโดยใช้หมวดหมู่ที่กำหนดไว้ (ความถูกต้อง แนวทางปฏิบัติ ประสิทธิภาพ ความปลอดภัย) พร้อมระดับความรุนแรง

<img src="../../../translated_images/th/structured-analysis-demo.9ef892194cd23bc8.webp" alt="สาธิตการวิเคราะห์อย่างมีโครงสร้าง" width="800"/>

*การตรวจโค้ดด้วยกรอบงาน*

### การสนทนาหลายรอบ

ถามว่า "Spring Boot คืออะไร?" แล้วตามด้วย "แสดงตัวอย่างให้ฉันดู" โมเดลจะจำคำถามแรกแล้วให้ตัวอย่าง Spring Boot ที่เจาะจง ถ้าไม่มีความจำ คำถามที่สองจะกว้างเกินไป

<img src="../../../translated_images/th/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="สาธิตการสนทนาหลายรอบ" width="800"/>

*การรักษาบริบทระหว่างคำถาม*

### การให้เหตุผลทีละขั้นตอน

เลือกปัญหาคณิตศาสตร์แล้วลองใช้ทั้งการให้เหตุผลทีละขั้นตอนและความกระตือรือร้นต่ำ ความกระตือรือร้นต่ำจะให้คำตอบทันที - เร็วแต่ไม่ชัดเจน การให้เหตุผลทีละขั้นตอนจะโชว์ทุกการคำนวณและการตัดสินใจ

<img src="../../../translated_images/th/step-by-step-reasoning-demo.12139513356faecd.webp" alt="สาธิตการให้เหตุผลทีละขั้นตอน" width="800"/>

*ปัญหาคณิตศาสตร์พร้อมขั้นตอนชัดเจน*

### ผลลัพธ์ที่จำกัด

เมื่อคุณต้องการรูปแบบหรือจำนวนคำที่เฉพาะเจาะจง รูปแบบนี้จะบังคับให้ปฏิบัติตามอย่างเคร่งครัด ลองสร้างสรุปที่มีคำจำนวน 100 คำในรูปแบบหัวข้อย่อย

<img src="../../../translated_images/th/constrained-output-demo.567cc45b75da1633.webp" alt="สาธิตผลลัพธ์ที่จำกัด" width="800"/>

*สรุปการเรียนรู้ของเครื่องด้วยการควบคุมรูปแบบ*

## สิ่งที่คุณกำลังเรียนรู้จริงๆ

**ความพยายามในการให้เหตุผลเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมความพยายามในการคำนวณผ่านพรอมต์ ความพยายามต่ำหมายถึงตอบเร็วพร้อมการสำรวจน้อย ความพยายามสูงหมายถึงโมเดลใช้เวลาในการคิดอย่างลึกซึ้ง คุณกำลังเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน - อย่าเสียเวลาในคำถามง่าย ๆ แต่ก็อย่าเร่งการตัดสินใจที่ซับซ้อนเกินไป

**โครงสร้างนำทางพฤติกรรม**

สังเกตแท็ก XML ในพรอมต์ไหม? มันไม่ใช่แค่ประดับ โมเดลจะปฏิบัติตามคำสั่งที่มีโครงสร้างได้แม่นยำกว่าข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือตรรกะซับซ้อน โครงสร้างช่วยโมเดลติดตามตำแหน่งที่อยู่และขั้นตอนถัดไป

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="โครงสร้างพรอมต์" width="800"/>

*กายวิภาคของพรอมต์ที่มีโครงสร้างดีพร้อมส่วนชัดเจนและการจัดระเบียบแบบ XML*

**คุณภาพด้วยการประเมินตนเอง**

รูปแบบที่สะท้อนตนเองทำงานโดยการชี้แจงเกณฑ์คุณภาพ แทนที่จะหวังว่าโมเดลจะ "ทำถูก" คุณบอกมันอย่างชัดเจนว่า "ถูกต้อง" คืออะไร: ตรรกะถูกต้อง การจัดการข้อผิดพลาด ประสิทธิภาพ ความปลอดภัย โมเดลจึงประเมินผลลัพธ์ของตนเองและปรับปรุงได้ ซึ่งเปลี่ยนการสร้างโค้ดจากการเสี่ยงโชคเป็นกระบวนการ

**บริบทมีขีดจำกัด**

การสนทนาหลายรอบทำงานโดยรวมข้อความประวัติทุกคำขอ แต่มีขีดจำกัด - ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อการสนทนาเติบโต คุณจะต้องมีกลยุทธ์เพื่อรักษาบริบทที่เกี่ยวข้องโดยไม่เกินขีดจำกัด โมดูลนี้แสดงให้เห็นว่า memory ทำงานอย่างไร; ต่อไปคุณจะเรียนรู้ว่าเมื่อใดควรสรุป เมื่อใดควรลืม และเมื่อใดควรดึงข้อมูล

## ขั้นตอนต่อไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**การนำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารฉบับนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้องได้ เอกสารฉบับต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เป็นทางการ สำหรับข้อมูลสำคัญ แนะนำให้ใช้บริการแปลโดยมืออาชีพที่เป็นมนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->