# Module 00: เริ่มต้นอย่างรวดเร็ว

## สารบัญ

- [บทนำ](../../../00-quick-start)
- [LangChain4j คืออะไร?](../../../00-quick-start)
- [การพึ่งพาของ LangChain4j](../../../00-quick-start)
- [ข้อกำหนดเบื้องต้น](../../../00-quick-start)
- [การตั้งค่า](../../../00-quick-start)
  - [1. รับ Token GitHub ของคุณ](../../../00-quick-start)
  - [2. ตั้งค่า Token ของคุณ](../../../00-quick-start)
- [รันตัวอย่าง](../../../00-quick-start)
  - [1. แชทพื้นฐาน](../../../00-quick-start)
  - [2. รูปแบบคำสั่ง](../../../00-quick-start)
  - [3. การเรียกฟังก์ชัน](../../../00-quick-start)
  - [4. ถามตอบเอกสาร (Easy RAG)](../../../00-quick-start)
  - [5. AI ที่รับผิดชอบ](../../../00-quick-start)
- [แต่ละตัวอย่างแสดงอะไรบ้าง](../../../00-quick-start)
- [ขั้นตอนถัดไป](../../../00-quick-start)
- [การแก้ปัญหา](../../../00-quick-start)

## บทนำ

Quickstart นี้ถูกออกแบบมาเพื่อให้คุณเริ่มต้นใช้งาน LangChain4j ได้อย่างรวดเร็วที่สุด ครอบคลุมพื้นฐานของการสร้างแอปพลิเคชัน AI ด้วย LangChain4j และ GitHub Models ในโมดูลถัดไปคุณจะใช้ Azure OpenAI กับ LangChain4j เพื่อสร้างแอปพลิเคชันที่ซับซ้อนมากขึ้น

## LangChain4j คืออะไร?

LangChain4j คือไลบรารี Java ที่ช่วยให้ง่ายต่อการสร้างแอปพลิเคชันที่ขับเคลื่อนด้วย AI แทนที่จะจัดการกับไคลเอนต์ HTTP และการแยกวิเคราะห์ JSON คุณจะทำงานกับ API Java ที่สะอาด

คำว่า "chain" ใน LangChain หมายถึงการเชื่อมต่อหลายส่วนเข้าด้วยกัน — คุณอาจเชื่อมต่อ prompt กับ model กับ parser หรือเชื่อมต่อการเรียก AI หลาย ๆ ครั้งที่เอาท์พุตหนึ่งเป็นอินพุตของถัดไป quick start นี้เน้นพื้นฐานก่อนที่จะสำรวจ chain ที่ซับซ้อนมากขึ้น

<img src="../../../translated_images/th/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*การเชื่อมต่อส่วนประกอบใน LangChain4j — บล็อกตัวต่อเชื่อมต่อกันเพื่อสร้างเวิร์กโฟลว์ AI ที่ทรงพลัง*

เราจะใช้สามส่วนประกอบหลัก:

**ChatModel** - อินเทอร์เฟซสำหรับการโต้ตอบกับโมเดล AI เรียกใช้ `model.chat("prompt")` และรับสตริงตอบกลับ เราใช้ `OpenAiOfficialChatModel` ที่ทำงานกับ endpoints ที่รองรับ OpenAI เช่น GitHub Models

**AiServices** - สร้างอินเทอร์เฟซบริการ AI ที่ปลอดภัยด้วยประเภท กำหนดเมธอด ติดแอนโนเทชันด้วย `@Tool` และ LangChain4j จะจัดการการประสานงาน AI จะเรียกเมธอด Java ของคุณโดยอัตโนมัติเมื่อจำเป็น

**MessageWindowChatMemory** - เก็บประวัติการสนทนา หากไม่มีส่วนนี้แต่ละคำขอจะแยกจากกัน แต่เมื่อมี AI จะจดจำข้อความก่อนหน้าและรักษาบริบทผ่านหลาย ๆ รอบ

<img src="../../../translated_images/th/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*สถาปัตยกรรม LangChain4j - ส่วนประกอบหลักทำงานร่วมกันเพื่อขับเคลื่อนแอป AI ของคุณ*

## การพึ่งพาของ LangChain4j

Quick start นี้ใช้การพึ่งพาของ Maven 3 ตัวใน [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

โมดูล `langchain4j-open-ai-official` มีคลาส `OpenAiOfficialChatModel` ที่เชื่อมต่อกับ API ที่รองรับ OpenAI GitHub Models ใช้รูปแบบ API เดียวกัน ดังนั้นไม่ต้องมีตัวปรับพิเศษ — เพียงตั้งค่า base URL ไปที่ `https://models.github.ai/inference`

โมดูล `langchain4j-easy-rag` ให้การแยกอัตโนมัติ การฝังและการเรียกคืนเอกสาร เพื่อให้คุณสร้างแอป RAG ได้โดยไม่ต้องกำหนดค่าทีละขั้นตอนด้วยตนเอง

## ข้อกำหนดเบื้องต้น

**ใช้ Dev Container?** Java และ Maven ติดตั้งเรียบร้อยแล้ว คุณเพียงต้องมี GitHub Personal Access Token

**การพัฒนาในเครื่อง:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (คำแนะนำด้านล่าง)

> **หมายเหตุ:** โมดูลนี้ใช้ `gpt-4.1-nano` จาก GitHub Models อย่าแก้ไขชื่อโมเดลในโค้ด — ตั้งค่าให้ทำงานกับโมเดลที่ GitHub มีอยู่แล้ว

## การตั้งค่า

### 1. รับ Token GitHub ของคุณ

1. ไปที่ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. คลิก "Generate new token"
3. ตั้งชื่อที่บ่งบอกชัดเจน (เช่น "LangChain4j Demo")
4. ตั้งวันหมดอายุ (แนะนำ 7 วัน)
5. ใน "Account permissions" หา "Models" และตั้งเป็น "Read-only"
6. คลิก "Generate token"
7. คัดลอกและเก็บ token ของคุณ — คุณจะไม่เห็นมันอีก

### 2. ตั้งค่า Token ของคุณ

**ตัวเลือก 1: ใช้ VS Code (แนะนำ)**

ถ้าคุณใช้ VS Code ให้เพิ่ม token ในไฟล์ `.env` ที่โฟลเดอร์รากของโปรเจกต์:

ถ้าไม่มีไฟล์ `.env` ให้คัดลอก `.env.example` เป็น `.env` หรือสร้างไฟล์ `.env` ใหม่ที่โฟลเดอร์ราก

**ตัวอย่างไฟล์ `.env`:**
```bash
# ใน /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

จากนั้นคุณสามารถคลิกขวาที่ไฟล์ตัวอย่างใดก็ได้ (เช่น `BasicChatDemo.java`) ใน Explorer แล้วเลือก **"Run Java"** หรือใช้การตั้งค่ารันจากแผง Run and Debug

**ตัวเลือก 2: ใช้เทอร์มินัล**

ตั้งค่า token เป็นตัวแปรสภาพแวดล้อม:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## รันตัวอย่าง

**ใช้ VS Code:** คลิกขวาที่ไฟล์ตัวอย่างใดก็ได้ใน Explorer แล้วเลือก **"Run Java"** หรือใช้การตั้งค่ารันจากแผง Run and Debug (แน่ใจว่าเพิ่ม token ใน `.env` ก่อน)

**ใช้ Maven:** หรือรันจากบรรทัดคำสั่ง:

### 1. แชทพื้นฐาน

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. รูปแบบคำสั่ง

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

แสดงตัวอย่าง zero-shot, few-shot, chain-of-thought และ prompt ตามบทบาท

### 3. การเรียกฟังก์ชัน

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI จะเรียกเมธอด Java ของคุณโดยอัตโนมัติเมื่อจำเป็น

### 4. ถามตอบเอกสาร (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ถามคำถามเกี่ยวกับเอกสารของคุณโดยใช้ Easy RAG ที่ฝังและเรียกคืนข้อมูลอัตโนมัติ

### 5. AI ที่รับผิดชอบ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ดูว่าฟิลเตอร์ความปลอดภัย AI จะบล็อกเนื้อหาที่เป็นอันตรายอย่างไร

## แต่ละตัวอย่างแสดงอะไรบ้าง

**แชทพื้นฐาน** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

เริ่มต้นที่นี่เพื่อดู LangChain4j ในแบบง่ายสุด คุณจะสร้าง `OpenAiOfficialChatModel` ส่ง prompt ด้วย `.chat()` และรับตอบกลับ แสดงพื้นฐาน: วิธีเริ่มต้นโมเดลด้วย endpoints และคีย์ API ที่กำหนดเอง เมื่อคุณเข้าใจแบบนี้ ทุกอย่างจะต่อยอดจากมัน

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) และถาม:
> - "ฉันจะสลับจาก GitHub Models ไป Azure OpenAI ในโค้ดนี้อย่างไร?"
> - "มีพารามิเตอร์อื่น ๆ อะไรที่ฉันตั้งค่าใน OpenAiOfficialChatModel.builder() ได้บ้าง?"
> - "จะเพิ่มการตอบสนองแบบ streaming โดยไม่ต้องรอการตอบกลับทั้งหมดอย่างไร?"

**การออกแบบคำสั่ง** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ตอนนี้ที่คุณรู้วิธีพูดกับโมเดลแล้ว ลองสำรวจว่าคุณพูดอะไรกับมัน ตัวอย่างนี้ใช้การตั้งค่าโมเดลเหมือนเดิมแต่แสดงรูปแบบ prompt ห้ารูปแบบ ลอง zero-shot สำหรับคำสั่งตรง, few-shot ที่เรียนจากตัวอย่าง, chain-of-thought เผยขั้นตอนเหตุผล และ prompt ตามบทบาทที่ตั้งบริบท คุณจะเห็นว่าโมเดลเดียวกันให้ผลลัพธ์ต่างกันอย่างมากขึ้นอยู่กับวิธีการกรอบคำสั่ง

ตัวอย่างนี้ยังแสดง template prompt ซึ่งเป็นวิธีทรงพลังในการสร้าง prompt ที่ใช้ซ้ำได้โดยมีตัวแปร
ตัวอย่างด้านล่างแสดง prompt โดยใช้ LangChain4j `PromptTemplate` เพื่อเติมตัวแปร AI จะตอบตามจุดหมายและกิจกรรมที่กำหนด

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) และถาม:
> - "ความแตกต่างระหว่าง zero-shot และ few-shot prompting คืออะไร และควรใช้เมื่อไหร่?"
> - "พารามิเตอร์ temperature มีผลต่อการตอบของโมเดลอย่างไร?"
> - "มีกลวิธีอะไรบ้างในการป้องกัน prompt injection attacks ใน production?"
> - "ฉันจะสร้าง PromptTemplate ที่ใช้ซ้ำได้สำหรับรูปแบบทั่วไปอย่างไร?"

**การรวมเครื่องมือ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ตรงนี้ LangChain4j แสดงพลัง คุณจะใช้ `AiServices` เพื่อสร้างผู้ช่วย AI ที่สามารถเรียกเมธอด Java ของคุณได้ แค่ติดแอนโนเทชันเมธอดด้วย `@Tool("คำอธิบาย")` แล้ว LangChain4j จัดการที่เหลือ — AI จะตัดสินใจอัตโนมัติว่าเมื่อไรใช้เครื่องมือแต่ละอย่างตามที่ผู้ใช้ถาม แสดงการเรียกฟังก์ชัน วิธีสำคัญในการสร้าง AI ที่ทำงานได้ ไม่ใช่แค่ตอบคำถาม

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) และถาม:
> - "แอนโนเทชัน @Tool ทำงานอย่างไร และ LangChain4j จัดการอย่างไรเบื้องหลัง?"
> - "AI สามารถเรียกใช้เครื่องมือหลายตัวตามลำดับเพื่อแก้ปัญหาซับซ้อนได้ไหม?"
> - "ถ้าเครื่องมือเกิด exception ควรจัดการข้อผิดพลาดอย่างไร?"
> - "ฉันจะรวม API จริงแทนตัวอย่างเครื่องคิดเลขนี้อย่างไร?"

**ถามตอบเอกสาร (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ที่นี่คุณจะเห็น RAG (retrieval-augmented generation) โดยใช้แนวทาง "Easy RAG" ของ LangChain4j เอกสารถูกโหลด แยก และฝังอัตโนมัติลงในที่เก็บข้อมูลในหน่วยความจำ แล้วตัวดึงเนื้อหาจะให้ชิ้นส่วนที่เกี่ยวข้องแก้ AI ในเวลาสอบถาม AI ตอบตามเอกสารของคุณ ไม่ใช่ความรู้ทั่วไปของโมเดล

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) และถาม:
> - "RAG ป้องกัน AI หลงผิดได้อย่างไรเมื่อเทียบกับการใช้ข้อมูลฝึกสอนของโมเดล?"
> - "ความแตกต่างระหว่างวิธีง่ายนี้กับสายงาน RAG ที่กำหนดเองคืออะไร?"
> - "ฉันจะขยายเพื่อรองรับเอกสารหลายฉบับหรือฐานความรู้ขนาดใหญ่อย่างไร?"

**AI ที่รับผิดชอบ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

สร้างความปลอดภัย AI ด้วยการป้องกันลึกซ้อน ตัวอย่างนี้แสดงสองชั้นของการป้องกันที่ทำงานร่วมกัน:

**ส่วนที่ 1: LangChain4j Input Guardrails** - บล็อก prompt อันตรายก่อนถึง LLM สร้าง guardrail กำหนดเองเพื่อตรวจสอบคำหรือรูปแบบต้องห้าม รันในโค้ดของคุณจึงรวดเร็วและไม่มีค่าใช้จ่าย

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**ส่วนที่ 2: ฟิลเตอร์ความปลอดภัยของผู้ให้บริการ** - GitHub Models มีฟิลเตอร์ในตัวที่จับสิ่งที่ guardrail อาจพลาด คุณจะเห็นการบล็อกแบบรุนแรง (HTTP 400 errors) สำหรับการละเมิดที่ร้ายแรง และการปฏิเสธแบบนุ่มนวลที่ AI ปฏิเสธอย่างสุภาพ

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) และถาม:
> - "InputGuardrail คืออะไร และฉันจะสร้างของฉันเองได้อย่างไร?"
> - "ความแตกต่างระหว่าง hard block กับ soft refusal คืออะไร?"
> - "ทำไมต้องใช้ทั้ง guardrails และฟิลเตอร์ของผู้ให้บริการร่วมกัน?"

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [01-introduction - เริ่มต้นกับ LangChain4j และ gpt-5 บน Azure](../01-introduction/README.md)

---

**การนำทาง:** [← กลับสู่หน้าหลัก](../README.md) | [ถัดไป: Module 01 - บทนำ →](../01-introduction/README.md)

---

## การแก้ปัญหา

### การคอมไพล์ Maven ครั้งแรก

**ปัญหา:** `mvn clean compile` หรือ `mvn package` ครั้งแรกใช้เวลานาน (10-15 นาที)

**สาเหตุ:** Maven ต้องดาวน์โหลดการพึ่งพาทั้งหมดของโปรเจกต์ (Spring Boot, ไลบรารี LangChain4j, SDK ของ Azure ฯลฯ) ในการคอมไพล์ครั้งแรก

**วิธีแก้:** พฤติกรรมปกติ บิลด์ครั้งต่อไปจะเร็วขึ้นมากเพราะแคชการพึ่งพาไว้แล้ว เวลาดาวน์โหลดขึ้นกับความเร็วอินเทอร์เน็ตของคุณ

### ไวยากรณ์คำสั่ง Maven ใน PowerShell

**ปัญหา:** คำสั่ง Maven ล้มเหลวพร้อมข้อผิดพลาด `Unknown lifecycle phase ".mainClass=..."`
**สาเหตุ**: PowerShell แปลเครื่องหมาย `=` เป็นตัวดำเนินการกำหนดค่าตัวแปร ทำให้ไวยากรณ์คุณสมบัติของ Maven ผิดพลาด

**วิธีแก้ไข**: ใช้ตัวดำเนินการหยุดการแยกวิเคราะห์ `--%` ก่อนคำสั่ง Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

ตัวดำเนินการ `--%` บอก PowerShell ให้ส่งอาร์กิวเมนต์ที่เหลือทั้งหมดไปยัง Maven โดยตรงโดยไม่ต้องแปลความหมาย

### การแสดงอิโมจิบน Windows PowerShell

**ปัญหา**: การตอบกลับ AI แสดงเป็นตัวอักษรขยะ (เช่น `????` หรือ `â??`) แทนอิโมจิใน PowerShell

**สาเหตุ**: การเข้ารหัสเริ่มต้นของ PowerShell ไม่รองรับอิโมจิ UTF-8

**วิธีแก้ไข**: รันคำสั่งนี้ก่อนการใช้งาน Java:
```cmd
chcp 65001
```

วิธีนี้บังคับให้ใช้การเข้ารหัส UTF-8 ในเทอร์มินัล หรือคุณสามารถใช้ Windows Terminal ซึ่งรองรับ Unicode ได้ดีกว่า

### การดีบักการเรียก API

**ปัญหา**: ข้อผิดพลาดการตรวจสอบสิทธิ์ ขีดจำกัดอัตรา หรือการตอบสนองที่ไม่คาดคิดจากโมเดล AI

**วิธีแก้ไข**: ตัวอย่างมี `.logRequests(true)` และ `.logResponses(true)` เพื่อแสดงการเรียก API ในคอนโซล ช่วยในการแก้ไขปัญหาข้อผิดพลาดการตรวจสอบสิทธิ์ ขีดจำกัดอัตรา หรือการตอบสนองที่ไม่คาดคิด ควรลบธงเหล่านี้ออกในสภาพแวดล้อมการผลิตเพื่อลดเสียงรบกวนในล็อก

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าจะพยายามให้มีความถูกต้อง แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลสำคัญ ขอแนะนำให้ใช้การแปลโดยผู้เชี่ยวชาญทางด้านภาษามนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->