# Module 00: เริ่มต้นอย่างรวดเร็ว

## สารบัญ

- [บทนำ](../../../00-quick-start)
- [LangChain4j คืออะไร?](../../../00-quick-start)
- [การพึ่งพาของ LangChain4j](../../../00-quick-start)
- [ข้อกำหนดเบื้องต้น](../../../00-quick-start)
- [การตั้งค่า](../../../00-quick-start)
  - [1. รับโทเค็น GitHub ของคุณ](../../../00-quick-start)
  - [2. ตั้งค่าโทเค็นของคุณ](../../../00-quick-start)
- [รันตัวอย่าง](../../../00-quick-start)
  - [1. แชทพื้นฐาน](../../../00-quick-start)
  - [2. รูปแบบพรอมต์](../../../00-quick-start)
  - [3. การเรียกฟังก์ชัน](../../../00-quick-start)
  - [4. ถามตอบเอกสาร (Easy RAG)](../../../00-quick-start)
  - [5. AI ที่รับผิดชอบ](../../../00-quick-start)
- [แต่ละตัวอย่างแสดงอะไรบ้าง](../../../00-quick-start)
- [ขั้นตอนถัดไป](../../../00-quick-start)
- [การแก้ไขปัญหา](../../../00-quick-start)

## บทนำ

การเริ่มต้นอย่างรวดเร็วนี้มีไว้เพื่อให้คุณสามารถเริ่มใช้ LangChain4j ได้เร็วที่สุดเท่าที่จะเป็นไปได้ ครอบคลุมพื้นฐานทั้งหมดของการสร้างแอปพลิเคชัน AI ด้วย LangChain4j และ GitHub Models ในโมดูลถัดไปคุณจะเปลี่ยนไปใช้ Azure OpenAI และ GPT-5.2 และเจาะลึกในแต่ละแนวคิด

## LangChain4j คืออะไร?

LangChain4j เป็นไลบรารี Java ที่ทำให้การสร้างแอปพลิเคชันที่ใช้ AI ง่ายขึ้น แทนที่จะต้องจัดการกับ HTTP clients และการแยก JSON คุณจะทำงานกับ API ของ Java ที่สะอาด

"chain" ใน LangChain หมายถึงการเชื่อมต่อองค์ประกอบหลายๆ ตัวเข้าด้วยกัน — คุณอาจเชื่อมต่อพรอมต์กับโมเดลกับตัวแยกวิเคราะห์ หรือเชื่อมต่อการเรียก AI หลายตัวเข้าด้วยกันที่ผลลัพธ์หนึ่งจะเป็นอินพุตสำหรับตัวถัดไป การเริ่มต้นอย่างรวดเร็วนี้เน้นพื้นฐานก่อนที่จะสำรวจเชนที่ซับซ้อนมากขึ้น

<img src="../../../translated_images/th/langchain-concept.ad1fe6cf063515e1.webp" alt="แนวคิดการเชื่อมต่อ LangChain4j" width="800"/>

*การเชื่อมต่อองค์ประกอบใน LangChain4j - บล็อกที่เชื่อมต่อเพื่อสร้างเวิร์กโฟลว์ AI ที่ทรงพลัง*

เราจะใช้สามองค์ประกอบหลัก:

**ChatModel** - อินเทอร์เฟซสำหรับโต้ตอบกับโมเดล AI เรียก `model.chat("prompt")` และรับสตริงตอบกลับ เราใช้ `OpenAiOfficialChatModel` ซึ่งทำงานกับ endpoints ที่เข้ากันได้กับ OpenAI เช่น GitHub Models

**AiServices** - สร้างอินเทอร์เฟซบริการ AI ที่ปลอดภัยตามชนิด กำหนดเมธอด ติดเครื่องหมายด้วย `@Tool` และ LangChain4j จะจัดการการประสานงาน AI จะเรียกใช้เมธอด Java ของคุณโดยอัตโนมัติเมื่อจำเป็น

**MessageWindowChatMemory** - รักษาประวัติการสนทนา ถ้าไม่มีสิ่งนี้แต่ละคำขอจะเป็นอิสระ แต่ถ้ามี AI จะจำข้อความก่อนหน้าและรักษาบริบทในหลายๆ รอบ

<img src="../../../translated_images/th/architecture.eedc993a1c576839.webp" alt="สถาปัตยกรรม LangChain4j" width="800"/>

*สถาปัตยกรรม LangChain4j - องค์ประกอบหลักที่ทำงานร่วมกันเพื่อขับเคลื่อนแอป AI ของคุณ*

## การพึ่งพาของ LangChain4j

การเริ่มต้นอย่างรวดเร็วนี้ใช้การพึ่งพา Maven สามตัวใน [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
โมดูล `langchain4j-open-ai-official` ให้คลาส `OpenAiOfficialChatModel` ที่เชื่อมต่อกับ API ที่เข้ากันได้กับ OpenAI GitHub Models ใช้รูปแบบ API เดียวกัน จึงไม่ต้องมีอะแดปเตอร์พิเศษ — เพียงตั้งค่า URL พื้นฐานไปที่ `https://models.github.ai/inference`

โมดูล `langchain4j-easy-rag` ให้การแยกเอกสาร การฝังตัว และการเรียกคืนอัตโนมัติ เพื่อให้คุณสร้างแอป RAG ได้โดยไม่ต้องตั้งค่าทีละขั้นตอนเอง

## ข้อกำหนดเบื้องต้น

**ใช้ Dev Container?** Java และ Maven ติดตั้งพร้อมใช้งานแล้ว คุณแค่ต้องมี GitHub Personal Access Token

**การพัฒนาท้องถิ่น:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (คำแนะนำด้านล่าง)

> **หมายเหตุ:** โมดูลนี้ใช้ `gpt-4.1-nano` จาก GitHub Models อย่าปรับชื่อโมเดลในโค้ด — โมเดลนี้ตั้งค่าให้ทำงานกับโมเดลที่ GitHub มีอยู่ได้แล้ว

## การตั้งค่า

### 1. รับโทเค็น GitHub ของคุณ

1. ไปที่ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)  
2. คลิก "Generate new token"  
3. ตั้งชื่อที่อธิบายได้ (เช่น "LangChain4j Demo")  
4. ตั้งวันหมดอายุ (แนะนำ 7 วัน)  
5. ใน "Account permissions" ค้นหา "Models" และตั้งเป็น "Read-only"  
6. คลิก "Generate token"  
7. คัดลอกและบันทึกโทเค็นของคุณ — คุณจะไม่เห็นอีก

### 2. ตั้งค่าโทเค็นของคุณ

**ตัวเลือก 1: ใช้ VS Code (แนะนำ)**

หากคุณใช้ VS Code ให้เพิ่มโทเค็นของคุณในไฟล์ `.env` ที่รูทโฟลเดอร์โปรเจค

ถ้าไฟล์ `.env` ไม่มี ให้คัดลอก `.env.example` เป็น `.env` หรือสร้างไฟล์ `.env` ใหม่ในรูทโปรเจค

**ตัวอย่างไฟล์ `.env`:**  
```bash
# ใน /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
จากนั้นคุณสามารถคลิกขวาที่ไฟล์เดโมใดๆ (เช่น `BasicChatDemo.java`) ใน Explorer แล้วเลือก **"Run Java"** หรือใช้การตั้งค่า launch ผ่านแผง Run and Debug

**ตัวเลือก 2: ใช้ Terminal**

ตั้งค่าตัวแปรสภาพแวดล้อมสำหรับโทเค็น:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```
  
## รันตัวอย่าง

**ใช้ VS Code:** คลิกขวาที่ไฟล์เดโมใน Explorer แล้วเลือก **"Run Java"** หรือใช้การตั้งค่า launch ผ่าน Run and Debug (ตรวจสอบให้แน่ใจว่าได้เพิ่มโทเค็นในไฟล์ `.env` แล้ว)

**ใช้ Maven:** หรือรันจาก command line:

### 1. แชทพื้นฐาน

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```
  
### 2. รูปแบบพรอมต์

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

### 4. ถามตอบเอกสาร (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
ถามคำถามเกี่ยวกับเอกสารของคุณโดยใช้ Easy RAG ที่มีการฝังและเรียกคืนข้อมูลอัตโนมัติ

### 5. AI ที่รับผิดชอบ

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
ดูว่าฟิลเตอร์ความปลอดภัย AI บล็อกเนื้อหาที่เป็นอันตรายอย่างไร

## แต่ละตัวอย่างแสดงอะไรบ้าง

**แชทพื้นฐาน** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

เริ่มต้นที่นี่เพื่อดู LangChain4j ในแบบพื้นฐานที่สุด คุณจะสร้าง `OpenAiOfficialChatModel` ส่งพรอมต์ด้วย `.chat()` และรับคำตอบกลับ สิ่งนี้แสดงรากฐาน: วิธีเริ่มโมเดลด้วย endpoints และคีย์ API ที่กำหนดเอง เมื่อเข้าใจรูปแบบนี้ ทุกอย่างอื่นจะต่อยอดได้

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) แล้วถาม:  
> - "จะเปลี่ยนจาก GitHub Models เป็น Azure OpenAI ในโค้ดนี้ได้อย่างไร?"  
> - "พารามิเตอร์ตัวไหนอีกที่สามารถตั้งค่าใน OpenAiOfficialChatModel.builder() ได้?"  
> - "จะเพิ่มการสตรีมคำตอบแทนการรอคำตอบเต็มได้อย่างไร?"

**การออกแบบพรอมต์ (Prompt Engineering)** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

เมื่อรู้วิธีคุยกับโมเดลแล้ว มาสำรวจสิ่งที่คุณพูดกัน เดโมนี้ใช้การตั้งค่าโมเดลเดียวกันแต่แสดงรูปแบบพรอมต์ 5 แบบ ลอง zero-shot สำหรับคำสั่งตรงๆ, few-shot ที่เรียนรู้จากตัวอย่าง, chain-of-thought ที่เผยขั้นตอนตรรกะ และ role-based ที่ตั้งบริบท คุณจะเห็นว่าโมเดลเดียวกันให้ผลลัพธ์ต่างกันมากตามที่คุณจัดกรอบคำขอ

เดโมนี้ยังสาธิต prompt templates วิธีสร้างพรอมต์ซ้ำได้ด้วยตัวแปร  
ตัวอย่างด้านล่างแสดงพรอมต์โดยใช้ LangChain4j `PromptTemplate` เพื่อเติมตัวแปร AI จะตอบตามปลายทางและกิจกรรมที่ให้มา

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
  
> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) แล้วถาม:  
> - "ความแตกต่างระหว่าง zero-shot กับ few-shot prompting คืออะไร และควรใช้เมื่อไร?"  
> - "พารามิเตอร์ temperature มีผลกับคำตอบของโมเดลอย่างไร?"  
> - "เทคนิคใดบ้างที่ช่วยป้องกัน prompt injection ในการใช้งานจริง?"  
> - "จะสร้าง PromptTemplate ที่ใช้ซ้ำได้สำหรับรูปแบบทั่วไปอย่างไร?"

**การรวมเครื่องมือ (Tool Integration)** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

นี่คือจุดที่ LangChain4j แข็งแกร่ง คุณจะใช้ `AiServices` สร้างผู้ช่วย AI ที่เรียกใช้เมธอด Java ได้ แค่ติดเครื่องหมายเมธอดด้วย `@Tool("description")` และ LangChain4j จัดการส่วนที่เหลือ — AI ตัดสินใจเองเมื่อเลือกใช้เครื่องมือใดตามที่ผู้ใช้ถาม นี่แสดงการเรียกฟังก์ชัน เทคนิคสำคัญสำหรับสร้าง AI ที่ทำงานได้ ไม่ใช่แค่ตอบคำถาม

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
  
> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) แล้วถาม:  
> - "@Tool annotation ทำงานอย่างไร และ LangChain4j ทำอะไรกับมันเบื้องหลัง?"  
> - "AI จะเรียกใช้เครื่องมือหลายตัวเรียงกันเพื่อแก้ปัญหาซับซ้อนได้ไหม?"  
> - "ถ้าเครื่องมือเกิดข้อผิดพลาดควรจัดการอย่างไร?"  
> - "จะผสานรวม API จริงแทนตัวอย่างเครื่องคิดเลขนี้ได้อย่างไร?"

**ถามตอบเอกสาร (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ที่นี่คุณจะเห็น RAG (retrieval-augmented generation) โดยใช้วิธี "Easy RAG" ของ LangChain4j เอกสารถูกโหลด แยกและฝังข้อมูลอัตโนมัติลงในที่เก็บข้อมูลในหน่วยความจำ แล้ว content retriever จะส่งชิ้นส่วนข้อมูลที่เกี่ยวข้องไปยัง AI ขณะถาม คำตอบมาจากเอกสารของคุณ ไม่ใช่ความรู้ทั่วไปของโมเดล

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
  
> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) แล้วถาม:  
> - "RAG ป้องกัน AI hallucination ต่างจากการใช้ข้อมูลเทรนโมเดลอย่างไร?"  
> - "ความแตกต่างระหว่างวิธีง่ายนี้กับ pipeline RAG แบบกำหนดเองคืออะไร?"  
> - "จะขยายระบบนี้ให้รองรับเอกสารหลายรายการหรือตัวฐานความรู้ที่ใหญ่ขึ้นได้อย่างไร?"

**AI ที่รับผิดชอบ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

สร้างความปลอดภัย AI ด้วยการป้องกันหลายชั้น เดโมนี้แสดงสองชั้นของการป้องกันที่ทำงานร่วมกัน:

**ส่วนที่ 1: LangChain4j Input Guardrails** - บล็อกพรอมต์อันตรายก่อนถึง LLM สร้าง guardrails แบบกำหนดเองเพื่อตรวจจับคำหรือรูปแบบต้องห้าม รันในโค้ดคุณจึงรวดเร็วและฟรี

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
  
**ส่วนที่ 2: Provider Safety Filters** - GitHub Models มีฟิลเตอร์ในตัวที่จับสิ่งที่ guardrails อาจพลาด คุณจะเห็นการบล็อกแบบรุนแรง (HTTP 400 errors) สำหรับการละเมิดที่รุนแรง และการปฏิเสธแบบอ่อนโยนที่ AI ปฏิเสธอย่างสุภาพ

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) แล้วถาม:  
> - "InputGuardrail คืออะไร และจะสร้างของตัวเองอย่างไร?"  
> - "ความแตกต่างระหว่างการบล็อกแบบรุนแรงและการปฏิเสธแบบอ่อนโยนคืออะไร?"  
> - "ทำไมจึงใช้ทั้ง guardrails และฟิลเตอร์ของ provider ร่วมกัน?"

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [01-introduction - เริ่มต้นใช้งาน LangChain4j](../01-introduction/README.md)

---

**นำทาง:** [← กลับสู่หน้าแรก](../README.md) | [ถัดไป: Module 01 - บทนำ →](../01-introduction/README.md)

---

## การแก้ไขปัญหา

### การสร้าง Maven ครั้งแรก

**ปัญหา:** คำสั่ง `mvn clean compile` หรือ `mvn package` ครั้งแรกใช้เวลานาน (10-15 นาที)

**สาเหตุ:** Maven ต้องดาวน์โหลดการพึ่งพาทั้งหมดของโปรเจค (Spring Boot, ไลบรารี LangChain4j, SDK ของ Azure ฯลฯ) ในการสร้างครั้งแรก

**วิธีแก้:** นี่เป็นพฤติกรรมปกติ การสร้างในครั้งถัดไปจะเร็วขึ้นมากเนื่องจากการพึ่งพาถูกเก็บไว้ในเครื่อง เวลาดาวน์โหลดขึ้นกับความเร็วเน็ตเวิร์กของคุณ

### ไวยากรณ์คำสั่ง Maven บน PowerShell

**ปัญหา:** คำสั่ง Maven ล้มเหลวพร้อมข้อผิดพลาด `Unknown lifecycle phase ".mainClass=..."`
**สาเหตุ**: PowerShell แปลความหมาย `=` เป็นตัวดำเนินการกำหนดค่าตัวแปร ซึ่งทำให้ไวยากรณ์ของคุณสมบัติ Maven เสียหาย

**วิธีแก้ไข**: ใช้ตัวดำเนินการหยุดการแยกวิเคราะห์ `--%` ก่อนคำสั่ง Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

ตัวดำเนินการ `--%` บอก PowerShell ให้ส่งอาร์กิวเมนต์ที่เหลือทั้งหมดไปยัง Maven ตามตัวอักษรโดยไม่ต้องแปลความหมาย

### การแสดงอิโมจิใน Windows PowerShell

**ปัญหา**: การตอบกลับของ AI แสดงเป็นตัวอักษรขยะ (เช่น `????` หรือ `â??`) แทนอิโมจิใน PowerShell

**สาเหตุ**: การเข้ารหัสเริ่มต้นของ PowerShell ไม่รองรับอิโมจิแบบ UTF-8

**วิธีแก้ไข**: รันคำสั่งนี้ก่อนเรียกใช้แอปพลิเคชัน Java:
```cmd
chcp 65001
```

สิ่งนี้บังคับให้ใช้การเข้ารหัสแบบ UTF-8 ในเทอร์มินัล หรืออีกทางเลือกหนึ่งคือใช้ Windows Terminal ซึ่งรองรับ Unicode ได้ดีกว่า

### การดีบักการเรียก API

**ปัญหา**: ข้อผิดพลาดการตรวจสอบสิทธิ์ ข้อจำกัดอัตราการใช้งาน หรือการตอบสนองที่ไม่คาดคิดจากโมเดล AI

**วิธีแก้ไข**: ตัวอย่างรวมคำสั่ง `.logRequests(true)` และ `.logResponses(true)` เพื่อแสดงการเรียก API ในคอนโซล ช่วยให้ตรวจสอบข้อผิดพลาดการตรวจสอบสิทธิ์ ข้อจำกัดอัตราการใช้งาน หรือการตอบสนองที่ไม่คาดคิดได้ ลบแฟล็กเหล่านี้ในสภาพแวดล้อมผลิตเพื่อลดเสียงรบกวนจากบันทึกล็อก

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) ซึ่งแม้เราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อน เอกสารต้นฉบับในภาษาดั้งเดิมถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่มีความสำคัญ ขอแนะนำให้ใช้บริการแปลโดยผู้เชี่ยวชาญมืออาชีพ เราไม่มีความรับผิดชอบต่อความเข้าใจผิดหรือความคลาดเคลื่อนที่อาจเกิดขึ้นจากการใช้แปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->