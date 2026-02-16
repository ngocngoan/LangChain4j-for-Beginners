# Module 00: เริ่มต้นอย่างรวดเร็ว

## สารบัญ

- [บทนำ](../../../00-quick-start)
- [LangChain4j คืออะไร?](../../../00-quick-start)
- [การพึ่งพา LangChain4j](../../../00-quick-start)
- [ข้อกำหนดเบื้องต้น](../../../00-quick-start)
- [การตั้งค่า](../../../00-quick-start)
  - [1. รับ Token GitHub ของคุณ](../../../00-quick-start)
  - [2. ตั้งค่า Token ของคุณ](../../../00-quick-start)
- [รันตัวอย่าง](../../../00-quick-start)
  - [1. แชทพื้นฐาน](../../../00-quick-start)
  - [2. รูปแบบคำสั่ง](../../../00-quick-start)
  - [3. การเรียกฟังก์ชัน](../../../00-quick-start)
  - [4. ถาม-ตอบเอกสาร (RAG)](../../../00-quick-start)
  - [5. ปัญญาประดิษฐ์ที่รับผิดชอบ](../../../00-quick-start)
- [แต่ละตัวอย่างแสดงอะไรบ้าง](../../../00-quick-start)
- [ขั้นตอนถัดไป](../../../00-quick-start)
- [การแก้ไขปัญหา](../../../00-quick-start)

## บทนำ

การเริ่มต้นอย่างรวดเร็วนี้มีจุดประสงค์เพื่อให้คุณสามารถเริ่มต้นใช้งาน LangChain4j ได้อย่างรวดเร็วที่สุด ครอบคลุมพื้นฐานที่สุดในการสร้างแอปพลิเคชัน AI ด้วย LangChain4j และ GitHub Models ในโมดูลถัดไป คุณจะใช้ Azure OpenAI ร่วมกับ LangChain4j เพื่อสร้างแอปพลิเคชันที่ซับซ้อนมากขึ้น

## LangChain4j คืออะไร?

LangChain4j คือไลบรารีภาษา Java ที่ทำให้การสร้างแอปพลิเคชัน AI ง่ายขึ้น แทนที่จะต้องจัดการกับ HTTP client และการแปลง JSON คุณจะทำงานกับ API ของ Java ที่สะอาดกว่า

“Chain” ใน LangChain หมายถึงการเชื่อมต่อองค์ประกอบหลายอย่างเข้าด้วยกัน — คุณอาจเชื่อมต่อ prompt กับโมเดลกับ parser หรือเชื่อมต่อการเรียก AI หลายครั้งที่เอาท์พุตของตัวหนึ่งไปเป็นอินพุตของตัวถัดไป การเริ่มต้นอย่างรวดเร็วนี้เน้นที่พื้นฐานก่อนสำรวจเชนที่ซับซ้อนกว่า

<img src="../../../translated_images/th/langchain-concept.ad1fe6cf063515e1.webp" alt="แนวคิดการเชื่อมต่อ LangChain4j" width="800"/>

*การเชื่อมต่อองค์ประกอบใน LangChain4j - บล็อกสร้างสรรค์เชื่อมต่อกันเพื่อสร้างเวิร์คโฟลว์ AI ที่ทรงพลัง*

เราจะใช้สามองค์ประกอบหลัก:

**ChatLanguageModel** - อินเทอร์เฟซสำหรับการโต้ตอบกับโมเดล AI เรียก `model.chat("prompt")` แล้วรับสตริงตอบกลับ เราใช้ `OpenAiOfficialChatModel` ที่ทำงานร่วมกับ endpoints ที่เข้ากันได้กับ OpenAI เช่น GitHub Models

**AiServices** - สร้างอินเทอร์เฟซบริการ AI ที่ปลอดภัยในแง่ชนิดข้อมูล กำหนดเมธอด, ประกาศว่าเป็น `@Tool` แล้ว LangChain4j จะจัดการ orchestration โดยอัตโนมัติ AI จะเรียกใช้เมธอด Java ของคุณเมื่อจำเป็น

**MessageWindowChatMemory** - รักษาประวัติการสนทนา หากไม่มีตัวนี้แต่ละคำขอจะเป็นอิสระแต่ละคำขอ แต่เมื่อใช้ ตัว AI จะจำข้อความก่อนหน้าและรักษาบริบทตลอดการสนทนาหลายรอบ

<img src="../../../translated_images/th/architecture.eedc993a1c576839.webp" alt="สถาปัตยกรรม LangChain4j" width="800"/>

*สถาปัตยกรรม LangChain4j - องค์ประกอบหลักทำงานร่วมกันเพื่อขับเคลื่อนแอป AI ของคุณ*

## การพึ่งพา LangChain4j

การเริ่มต้นอย่างรวดเร็วนี้ใช้การพึ่งพา Maven สองตัวใน [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

โมดูล `langchain4j-open-ai-official` มีคลาส `OpenAiOfficialChatModel` ที่เชื่อมต่อกับ API ที่เข้ากันได้กับ OpenAI GitHub Models ใช้รูปแบบ API เดียวกัน ดังนั้นจึงไม่ต้องมีอแดปเตอร์พิเศษ — เพียงตั้งค่า base URL เป็น `https://models.github.ai/inference`

## ข้อกำหนดเบื้องต้น

**ใช้ Dev Container?** Java และ Maven ติดตั้งมาแล้ว คุณแค่ต้องมี GitHub Personal Access Token

**พัฒนาแบบ Local:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (คำแนะนำด้านล่าง)

> **หมายเหตุ:** โมดูลนี้ใช้ `gpt-4.1-nano` จาก GitHub Models ห้ามแก้ไขชื่อโมเดลในโค้ด — ตั้งค่าร่วมกันให้ทำงานกับโมเดลที่มีใน GitHub

## การตั้งค่า

### 1. รับ Token GitHub ของคุณ

1. ไปที่ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. คลิก "Generate new token"
3. ตั้งชื่อที่อธิบายได้ (เช่น "LangChain4j Demo")
4. ตั้งวันหมดอายุ (แนะนำ 7 วัน)
5. ภายใต้ "Account permissions" หา "Models" แล้วตั้งเป็น "Read-only"
6. คลิก "Generate token"
7. คัดลอกและเก็บ token ของคุณ — คุณจะไม่เห็นอีกครั้ง

### 2. ตั้งค่า Token ของคุณ

**ตัวเลือกที่ 1: ใช้ VS Code (แนะนำ)**

ถ้าใช้ VS Code ให้เพิ่ม token ของคุณในไฟล์ `.env` ที่โฟลเดอร์รากของโปรเจค

ถ้าไฟล์ `.env` ยังไม่มี ให้คัดลอก `.env.example` เป็น `.env` หรืสร้างไฟล์ `.env` ใหม่ในโฟลเดอร์รากโปรเจค

**ตัวอย่างไฟล์ `.env`:**
```bash
# ใน /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

แล้วคุณสามารถคลิกขวาที่ไฟล์ตัวอย่างใดก็ได้ (เช่น `BasicChatDemo.java`) ใน Explorer แล้วเลือก **"Run Java"** หรือใช้ตัวตั้งค่า launch จากแผง Run and Debug

**ตัวเลือกที่ 2: ใช้ Terminal**

ตั้งค่า token เป็น environment variable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## รันตัวอย่าง

**ใช้ VS Code:** คลิกขวาที่ไฟล์ตัวอย่างใน Explorer แล้วเลือก **"Run Java"** หรือใช้ตัวตั้งค่า launch จากแผง Run and Debug (อย่าลืมเพิ่ม token ในไฟล์ `.env` ก่อน)

**ใช้ Maven:** หรือจะรันจาก command line ก็ได้

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

แสดง zero-shot, few-shot, chain-of-thought และ role-based prompting

### 3. การเรียกฟังก์ชัน

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI จะเรียกใช้เมธอด Java ของคุณโดยอัตโนมัติเมื่อจำเป็น

### 4. ถาม-ตอบเอกสาร (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ถามคำถามเกี่ยวกับเนื้อหาใน `document.txt`

### 5. ปัญญาประดิษฐ์ที่รับผิดชอบ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

แสดงการกรองความปลอดภัยของ AI ที่ป้องกันเนื้อหาที่เป็นอันตราย

## แต่ละตัวอย่างแสดงอะไรบ้าง

**แชทพื้นฐาน** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

เริ่มต้นที่นี่เพื่อดู LangChain4j ในรูปแบบที่ง่ายที่สุด คุณจะสร้าง `OpenAiOfficialChatModel` ส่ง prompt ด้วย `.chat()` และรับการตอบกลับ แสดงพื้นฐาน: วิธีเริ่มต้นโมเดลด้วย endpoints และ API keys ที่กำหนดเอง เมื่อเข้าใจรูปแบบนี้แล้ว ทุกอย่างจะสร้างต่อได้

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) และถาม:
> - "จะเปลี่ยนจาก GitHub Models เป็น Azure OpenAI ในโค้ดนี้อย่างไร?"
> - "มีพารามิเตอร์อื่นอะไรที่ฉันสามารถตั้งค่าใน OpenAiOfficialChatModel.builder() ได้บ้าง?"
> - "ฉันจะเพิ่มการตอบกลับแบบสตรีมแทนการรอผลลัพธ์เต็มได้อย่างไร?"

**การออกแบบ Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

เมื่อตอนนี้คุณรู้วิธีพูดกับโมเดลแล้ว ลองดูสิ่งที่คุณพูดกับมัน เดโมนี้ใช้การตั้งค่าโมเดลเหมือนกันแต่แสดงรูปแบบคำสั่งห้ารูปแบบ ลอง zero-shot prompt เพื่อคำสั่งตรง, few-shot prompt เรียนรู้จากตัวอย่าง, chain-of-thought prompt เผยขั้นตอนการคิด และ role-based prompt ตั้งบริบท คุณจะเห็นว่าโมเดลเดียวกันให้ผลลัพธ์แตกต่างมากตามวิธีตั้งคำถาม

เดโมนี้ยังแสดง prompt templates ซึ่งเป็นวิธีที่ทรงพลังในการสร้าง prompt ที่ใช้ซ้ำได้ด้วยตัวแปร
ตัวอย่างด้านล่างแสดง prompt ที่ใช้ LangChain4j `PromptTemplate` เพื่อเติมตัวแปร AI จะตอบตามจุดหมายและกิจกรรมที่ให้มา

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

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) และถาม:
> - "ความแตกต่างระหว่าง zero-shot กับ few-shot prompting คืออะไร และควรใช้เมื่อไร?"
> - "พารามิเตอร์ temperature ส่งผลต่อการตอบสนองของโมเดลอย่างไร?"
> - "มีเทคนิคอะไรบ้างในการป้องกัน prompt injection ในการผลิต?"
> - "ฉันจะสร้างวัตถุ PromptTemplate ที่ใช้ซ้ำสำหรับรูปแบบทั่วไปอย่างไร?"

**การรวมเครื่องมือ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ตรงนี้ LangChain4j โชว์พลัง คุณจะใช้ `AiServices` เพื่อสร้างผู้ช่วย AI ที่เรียกใช้เมธอด Java ของคุณ แค่ใส่ @Tool("description") ในเมธอดแล้ว LangChain4j จะจัดการส่วนที่เหลือ — AI จะตัดสินใจใช้เครื่องมือแต่ละอย่างโดยอัตโนมัติตามคำถามของผู้ใช้ เดโมนี้แสดงการเรียกฟังก์ชัน ซึ่งเป็นเทคนิคสำคัญในการสร้าง AI ที่ทำงานได้ ไม่ใช่แค่ตอบคำถาม

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) และถาม:
> - "@Tool annotation ทำงานอย่างไร และ LangChain4j ทำอะไรกับมันเบื้องหลัง?"
> - "AI สามารถเรียกเครื่องมือหลายตัวตามลำดับเพื่อแก้ปัญหาซับซ้อนได้ไหม?"
> - "ถ้าเครื่องมือเกิดข้อยกเว้น ฉันควรจัดการข้อผิดพลาดอย่างไร?"
> - "ฉันจะรวม API จริงแทนตัวอย่างเครื่องคิดเลขนี้ได้อย่างไร?"

**ถาม-ตอบเอกสาร (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ที่นี่คุณจะเห็นพื้นฐานของการทำ RAG (retrieval-augmented generation) แทนที่จะพึ่งข้อมูลฝึกสอนโมเดล คุณโหลดเนื้อหาจาก [`document.txt`](../../../00-quick-start/document.txt) และใส่ใน prompt AI จะตอบตามเอกสารที่ให้ ไม่ใช่ความรู้ทั่วไป นี่คือก้าวแรกสู่การสร้างระบบที่ทำงานกับข้อมูลของคุณเอง

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **หมายเหตุ:** วิธีง่าย ๆ นี้โหลดเอกสารทั้งฉบับเข้าใน prompt สำหรับไฟล์ใหญ่ (>10KB) จะเกินขีดจำกัดบริบท โมดูล 03 จะสอนการแบ่งชิ้นและค้นหาเวกเตอร์สำหรับระบบ RAG ในการผลิต

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) และถาม:
> - "RAG ป้องกัน AI หลงผิดอย่างไรเมื่อเทียบกับการใช้ข้อมูลฝึกสอนของโมเดล?"
> - "วิธีง่ายนี้ต่างจากการใช้ embeddings เวกเตอร์ในการค้นหาอย่างไร?"
> - "ฉันจะขยายเพื่อรองรับเอกสารหลายฉบับหรือฐานความรู้ที่ใหญ่ขึ้นอย่างไร?"
> - "แนวทางปฏิบัติที่ดีที่สุดในการจัดโครงสร้าง prompt เพื่อให้ AI ใช้บริบทที่ให้ไว้เท่านั้นคืออะไร?"

**ปัญญาประดิษฐ์ที่รับผิดชอบ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

สร้างความปลอดภัย AI ด้วยการป้องกันหลายชั้น เดโมนี้แสดงการทำงานของสองชั้นป้องกันร่วมกัน:

**ส่วนที่ 1: LangChain4j Input Guardrails** — บล็อก prompt อันตรายก่อนถึง LLM สร้าง guardrails แบบกำหนดเองเพื่อตรวจคำต้องห้ามหรือลวดลายต่าง ๆ รันในโค้ดของคุณ จึงรวดเร็วและไม่มีค่าใช้จ่าย

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

**ส่วนที่ 2: ฟิลเตอร์ความปลอดภัยจากผู้ให้บริการ** — GitHub Models มีฟิลเตอร์ในตัวที่จับสิ่งที่ guardrails อาจพลาด คุณจะเห็นบล็อกแข็ง (HTTP 400 error) สำหรับการละเมิดที่รุนแรง และบล็อกนุ่มที่ AI ปฏิเสธอย่างสุภาพ

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) และถาม:
> - "InputGuardrail คืออะไร และฉันสร้างของฉันเองได้อย่างไร?"
> - "ความแตกต่างระหว่างบล็อกแข็งกับการปฏิเสธนุ่มคืออะไร?"
> - "ทำไมต้องใช้ทั้ง guardrails และฟิลเตอร์ผู้ให้บริการด้วยกัน?"

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [01-introduction - เริ่มต้นกับ LangChain4j และ gpt-5 บน Azure](../01-introduction/README.md)

---

**การนำทาง:** [← กลับสู่หน้าหลัก](../README.md) | [ถัดไป: Module 01 - บทนำ →](../01-introduction/README.md)

---

## การแก้ไขปัญหา

### การสร้าง Maven ครั้งแรก

**ปัญหา:** รัน `mvn clean compile` หรือ `mvn package` ครั้งแรกใช้เวลานาน (10-15 นาที)

**สาเหตุ:** Maven ต้องดาวน์โหลดการพึ่งพาทั้งหมดของโปรเจค (Spring Boot, ไลบรารี LangChain4j, SDK Azure ฯลฯ) ในการสร้างครั้งแรก

**วิธีแก้:** นี่เป็นพฤติกรรมปกติ การสร้างครั้งถัดไปจะเร็วขึ้นเพราะมีการแคชการพึ่งพาไว้แล้ว เวลาดาวน์โหลดขึ้นกับความเร็วเน็ตของคุณ
### ไวยากรณ์คำสั่ง PowerShell Maven

**ปัญหา**: คำสั่ง Maven ล้มเหลวพร้อมข้อผิดพลาด `Unknown lifecycle phase ".mainClass=..."`

**สาเหตุ**: PowerShell แปล `=` เป็นตัวดำเนินการกำหนดค่าตัวแปร ทำให้ไวยากรณ์คุณสมบัติ Maven ผิดพลาด

**วิธีแก้ไข**: ใช้ตัวดำเนินการหยุดการแยกวิเคราะห์ `--%` ก่อนคำสั่ง Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

ตัวดำเนินการ `--%` บอก PowerShell ให้ส่งอาร์กิวเมนต์ที่เหลือทั้งหมดต่อไปยัง Maven อย่างตรงตัวโดยไม่แปลความหมาย

### การแสดงอิโมจิบน Windows PowerShell

**ปัญหา**: การตอบกลับ AI แสดงตัวอักษรขยะ (เช่น `????` หรือ `â??`) แทนอิโมจิใน PowerShell

**สาเหตุ**: การเข้ารหัสเริ่มต้นของ PowerShell ไม่รองรับอิโมจิแบบ UTF-8

**วิธีแก้ไข**: รันคำสั่งนี้ก่อนเรียกใช้แอปพลิเคชัน Java:
```cmd
chcp 65001
```

นี่จะบังคับให้ใช้การเข้ารหัส UTF-8 ในเทอร์มินัล อีกทางเลือกหนึ่งคือใช้ Windows Terminal ซึ่งรองรับ Unicode ได้ดีกว่า

### การดีบักการเรียก API

**ปัญหา**: ข้อผิดพลาดการตรวจสอบสิทธิ์, ขีดจำกัดการใช้งาน หรือการตอบกลับที่ไม่คาดคิดจากโมเดล AI

**วิธีแก้ไข**: ตัวอย่างประกอบด้วย `.logRequests(true)` และ `.logResponses(true)` เพื่อแสดงการเรียก API ในคอนโซล ซึ่งช่วยแก้ไขปัญหาการตรวจสอบสิทธิ์, ขีดจำกัดการใช้งาน หรือการตอบกลับที่ไม่คาดคิด ให้ลบธงเหล่านี้ในขั้นตอนใช้งานจริงเพื่อลดเสียงรบกวนในบันทึกข้อมูล

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ถูกแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้มีความแม่นยำ แต่กรุณาทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลสำคัญ แนะนำให้ใช้การแปลโดยผู้เชี่ยวชาญมนุษย์เป็นหลัก เราไม่รับผิดชอบในกรณีที่เกิดความเข้าใจผิดหรือการตีความผิดพลาดใด ๆ จากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->