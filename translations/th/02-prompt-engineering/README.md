# Module 02: การออกแบบ Prompt กับ GPT-5.2

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [ทำความเข้าใจกับการออกแบบ Prompt](../../../02-prompt-engineering)
- [พื้นฐานการออกแบบ Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [รูปแบบขั้นสูง](../../../02-prompt-engineering)
- [การใช้ทรัพยากร Azure ที่มีอยู่](../../../02-prompt-engineering)
- [ภาพหน้าจอแอปพลิเคชัน](../../../02-prompt-engineering)
- [สำรวจรูปแบบ](../../../02-prompt-engineering)
  - [ความกระตือรือร้นต่ำ vs สูง](../../../02-prompt-engineering)
  - [การดำเนินงานตามงาน (Tool Preambles)](../../../02-prompt-engineering)
  - [โค้ดที่สะท้อนตัวเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์เป็นโครงสร้าง](../../../02-prompt-engineering)
  - [แชทหลายรอบ](../../../02-prompt-engineering)
  - [การให้เหตุผลทีละขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่ถูกจำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้อย่างแท้จริง](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## สิ่งที่คุณจะได้เรียนรู้

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="สิ่งที่คุณจะได้เรียนรู้" width="800"/>

ในโมดูลก่อนหน้านี้ คุณได้เห็นว่าหน่วยความจำช่วยให้ AI สร้างบทสนทนาได้อย่างไร และใช้ GitHub Models สำหรับการโต้ตอบพื้นฐาน ตอนนี้เราจะเน้นวิธีที่คุณถามคำถาม — 즉 prompt เอง — โดยใช้ GPT-5.2 ของ Azure OpenAI วิธีที่คุณจัดโครงสร้าง prompt มีผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เราจะเริ่มด้วยการทบทวนเทคนิคพื้นฐานของ prompt จากนั้นเข้าสู่แปดรูปแบบขั้นสูงที่ใช้ประโยชน์จากความสามารถของ GPT-5.2 อย่างเต็มที่

เราเลือกใช้ GPT-5.2 เพราะมันเพิ่มการควบคุมการให้เหตุผล — คุณสามารถบอกโมเดลได้ว่าต้องคิดมากแค่ไหนก่อนตอบ นี่ทำให้กลยุทธ์การ prompt แตกต่างกันอย่างชัดเจนและช่วยให้คุณเข้าใจเมื่อใดควรใช้แต่ละวิธี นอกจากนี้ เรายังได้รับประโยชน์จากข้อจำกัดอัตราการใช้งานที่น้อยกว่าของ GPT-5.2 บน Azure เมื่อเทียบกับ GitHub Models

## ข้อกำหนดเบื้องต้น

- ทำโมดูล 01 เสร็จสมบูรณ์แล้ว (พร้อมใช้งาน Azure OpenAI resources)
- ไฟล์ `.env` ในโฟลเดอร์รากที่มีข้อมูลรับรอง Azure (สร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** หากคุณยังไม่ทำโมดูล 01 กรุณาปฏิบัติตามคำแนะนำการปรับใช้งานที่นั่นก่อน

## ทำความเข้าใจกับการออกแบบ Prompt

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="การออกแบบ Prompt คืออะไร?" width="800"/>

การออกแบบ prompt คือการออกแบบข้อความนำเข้าที่ได้ผลลัพธ์ตามต้องการอย่างสม่ำเสมอ ไม่ใช่แค่ถามคำถามเท่านั้น — แต่เป็นการจัดโครงสร้างคำขอให้โมเดลเข้าใจสิ่งที่คุณต้องการและวิธีการตอบสนองอย่างชัดเจน

คิดว่ามันเหมือนกับการให้คำสั่งเพื่อนร่วมงาน "แก้จุดบกพร่อง" ฟังดูไม่ชัดเจน แต่ "แก้ข้อผิดพลาด null pointer ใน UserService.java บรรทัดที่ 45 โดยเพิ่มการตรวจ null" ชัดเจนมาก โมเดลภาษาก็เหมือนกัน — ความชัดเจนและโครงสร้างมีความสำคัญ

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ทำงานอย่างไร" width="800"/>

LangChain4j ให้โครงสร้างพื้นฐาน — การเชื่อมต่อโมเดล, หน่วยความจำ, และประเภทข้อความ — ในขณะที่รูปแบบ prompt เป็นข้อความที่จัดโครงสร้างอย่างระมัดระวังที่คุณส่งผ่านโครงสร้างนั้น บล็อกหลักคือ `SystemMessage` (ตั้งพฤติกรรมและบทบาท AI) และ `UserMessage` (ส่งคำขอของคุณ)

## พื้นฐานการออกแบบ Prompt

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="ภาพรวม 5 รูปแบบการออกแบบ Prompt" width="800"/>

ก่อนที่จะเจาะลึกในรูปแบบขั้นสูงในโมดูลนี้ ให้ทบทวน 5 เทคนิคพื้นฐานของการออกแบบ prompt เหล่านี้เป็นบล็อกพื้นฐานที่นักออกแบบ prompt ทุกคนควรรู้ หากคุณเคยเรียนโมดูล [Quick Start](../00-quick-start/README.md#2-prompt-patterns) แล้ว คุณจะเห็นภาพรวมแนวคิดเบื้องหลังเหล่านี้

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้คำสั่งโดยตรงกับโมเดลโดยไม่มีตัวอย่าง โมเดลจะอาศัยการฝึกทั้งหมดเพื่อทำความเข้าใจและดำเนินการตามงาน วิธีนี้เหมาะกับคำขอที่ตรงไปตรงมาและพฤติกรรมที่คาดหวังชัดเจน

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรงโดยไม่มีตัวอย่าง — โมเดลอนุมานงานจากคำสั่งเพียงอย่างเดียว*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// การตอบกลับ: "บวก"
```
  
**เมื่อใดควรใช้:** การจำแนกประเภทง่ายๆ, คำถามตรง, การแปล หรือทุกงานที่โมเดลจัดการได้โดยไม่ต้องมีคำแนะนำเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่คุณต้องการให้โมเดลทำตาม โมเดลเรียนรู้รูปแบบการรับเข้า-ส่งออกจากตัวอย่างของคุณและประยุกต์ใช้กับข้อมูลใหม่ วิธีนี้ช่วยเพิ่มความสม่ำเสมอมากสำหรับงานที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*เรียนรู้จากตัวอย่าง — โมเดลระบุรูปแบบและประยุกต์ใช้กับข้อมูลใหม่*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```
  
**เมื่อใดควรใช้:** การจำแนกประเภทที่กำหนดเอง, การจัดรูปแบบที่สอดคล้อง, งานเฉพาะด้าน หรือเมื่อผล zero-shot ไม่สม่ำเสมอ

### Chain of Thought

ขอให้โมเดลแสดงการให้เหตุผลทีละขั้นตอน แทนที่จะตอบทันที โมเดลจะแยกปัญหาและทำงานทีละส่วนอย่างชัดเจน วิธีนี้ช่วยเพิ่มความแม่นยำสำหรับปัญหาคณิตศาสตร์, ตรรกะ และการให้เหตุผลหลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*การให้เหตุผลทีละขั้นตอน — แยกปัญหาซับซ้อนเป็นขั้นตอนตรรกะชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// โมเดลแสดง: 15 - 8 = 7 จากนั้น 7 + 12 = 19 แอปเปิ้ล
```
  
**เมื่อใดควรใช้:** ปัญหาคณิตศาสตร์, เกมปริศนา, การดีบัก หรือทุกงานที่การแสดงเหตุผลช่วยเพิ่มความแม่นยำและความเชื่อมั่น

### Role-Based Prompting

ตั้งค่าสถานะบุคลิกภาพหรือบทบาทสำหรับ AI ก่อนถามคำถาม นี่ช่วยกำหนดบริบทที่ส่งผลต่อโทนเสียง, ความลึกซึ้ง และจุดเน้นของคำตอบ "สถาปนิกซอฟต์แวร์" ให้คำแนะนำต่างจาก "นักพัฒนาระดับจูเนียร์" หรือ "ผู้ตรวจสอบด้านความปลอดภัย"

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*ตั้งบทบาทและบริบท — คำถามเดียวกันได้คำตอบต่างกันขึ้นอยู่กับบทบาทที่กำหนด*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```
  
**เมื่อใดควรใช้:** การตรวจสอบโค้ด, การสอน, การวิเคราะห์เฉพาะโดเมน หรือเมื่อคุณต้องการคำตอบที่เหมาะสมกับระดับความเชี่ยวชาญหรือมุมมองเฉพาะ

### Prompt Templates

สร้าง prompt ที่ใช้ซ้ำได้พร้อมตัวแปรแทนที่ แทนที่จะเขียน prompt ใหม่ทุกครั้ง ให้กำหนดเทมเพลตหนึ่งครั้ง และแทนค่าตัวแปรต่างๆ คลาส `PromptTemplate` ของ LangChain4j ช่วยทำเรื่องนี้ง่ายด้วยไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt ใช้ซ้ำโดยมีตัวแปรแทนที่ — เทมเพลตเดียว ใช้ได้หลายกรณี*

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
  
**เมื่อใดควรใช้:** คำถามซ้ำๆ ที่มีข้อมูลแตกต่างกัน, การประมวลผลชุด, สร้าง workflow AI ที่ใช้ซ้ำได้ หรือทุกสถานการณ์ที่โครงสร้าง prompt เหมือนเดิมแต่ข้อมูลเปลี่ยน

---

พื้นฐานทั้งห้านี้ให้ชุดเครื่องมือที่มั่นคงสำหรับงาน prompt ส่วนที่เหลือของโมดูลนี้จะต่อยอดโดยมี **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมการให้เหตุผลของ GPT-5.2, การประเมินตัวเอง และความสามารถในการส่งออกข้อมูลแบบมีโครงสร้าง

## รูปแบบขั้นสูง

หลังจากทราบพื้นฐานแล้ว มาดูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ปัญหาแต่ละอย่างไม่จำเป็นต้องใช้วิธีเดียวกัน คำถามบางข้ออยากได้คำตอบเร็ว บางข้ออยากได้การวิเคราะห์ลึก บางข้ออยากเห็นเหตุผล บางข้อแค่ผลลัพธ์ รูปแบบแต่ละแบบด้านล่างนี้ได้รับการปรับแต่งสำหรับสถานการณ์ที่แตกต่างกัน — และการควบคุมการให้เหตุผลของ GPT-5.2 ทำให้ความแตกต่างนี้ชัดเจนขึ้นมาก

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="แปดรูปแบบการออกแบบ Prompt" width="800"/>

*ภาพรวมของแปดรูปแบบการออกแบบ prompt และกรณีการใช้งาน*

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="การควบคุมการให้เหตุผลกับ GPT-5.2" width="800"/>

*การควบคุมการให้เหตุผลของ GPT-5.2 ช่วยให้ระบุได้ว่าโมเดลควรคิดมากแค่ไหน — ตั้งแต่ตอบเร็วตรงไปตรงมาจนถึงสำรวจลึก*

<img src="../../../translated_images/th/reasoning-effort.db4a3ba5b8e392c1.webp" alt="การเปรียบเทียบความพยายามให้เหตุผล" width="800"/>

*ความกระตือรือร้นต่ำ (เร็ว ตรงประเด็น) vs ความกระตือรือร้นสูง (ละเอียดและสำรวจลึก) ในการให้เหตุผล*

**ความกระตือรือร้นต่ำ (เร็ว & เน้นประเด็น)** - สำหรับคำถามง่ายๆ ที่คุณต้องการคำตอบเร็วและตรงโมเดลใช้การให้เหตุผลน้อยที่สุด - ไม่เกิน 2 ขั้นตอน ใช้สำหรับการคำนวณ การค้นข้อมูล หรือคำถามตรงๆ

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **ลองใช้กับ GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) และถามว่า:  
> - "ความแตกต่างระหว่างรูปแบบการ prompt ความกระตือรือร้นต่ำและสูงคืออะไร?"  
> - "แท็ก XML ใน prompt ช่วยการจัดโครงสร้างคำตอบ AI อย่างไร?"  
> - "เมื่อใดควรใช้รูปแบบสะท้อนตัวเอง vs คำสั่งตรง?"

**ความกระตือรือร้นสูง (ลึก & รอบคอบ)** - สำหรับปัญหาซับซ้อนที่ต้องการการวิเคราะห์ครบถ้วน โมเดลจะสำรวจอย่างละเอียดและแสดงเหตุผลอย่างละเอียด ใช้สำหรับการออกแบบระบบ, การตัดสินใจสถาปัตยกรรม หรือการวิจัยเชิงลึก

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**การดำเนินงานตามงาน (ความก้าวหน้าทีละขั้นตอน)** - สำหรับ workflow หลายขั้นตอน โมเดลจะเสนอแผนล่วงหน้า บอกเล่าทุกขั้นตอนขณะทำงาน จากนั้นสรุปผล ใช้สำหรับการโยกย้ายระบบ การใช้งาน หรือกระบวนการหลายขั้นตอนใดๆ

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
การ prompt แบบ Chain-of-Thought ช่วยให้โมเดลแสดงกระบวนการให้เหตุผลชัดเจน เพิ่มความแม่นยำสำหรับงานที่ซับซ้อน การแยกเป็นขั้นตอนช่วยทั้งคนและ AI เข้าใจตรรกะ

> **🤖 ลองกับ [GitHub Copilot](https://github.com/features/copilot) แชท:** ถามเกี่ยวกับรูปแบบนี้:  
> - "จะปรับรูปแบบการดำเนินงานสำหรับงานที่ใช้เวลานานอย่างไร?"  
> - "แนวปฏิบัติที่ดีที่สุดสำหรับโครงสร้าง preambles ของเครื่องมือในแอปจริงคืออะไร?"  
> - "จะจับและแสดงความคืบหน้ากลางทางใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="รูปแบบการดำเนินงานตามงาน" width="800"/>

*แผน → ดำเนินการ → สรุป workflow สำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตัวเอง** - สำหรับการสร้างโค้ดคุณภาพผลิต โมเดลสร้างโค้ด ตรวจสอบตามเกณฑ์คุณภาพ และปรับปรุงอย่างต่อเนื่อง ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

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
  
<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="วงจรการสะท้อนตัวเอง" width="800"/>

*วงจรปรับปรุงแบบวนซ้ำ - สร้าง, ประเมิน, หาปัญหา, ปรับปรุง, ทำซ้ำ*

**การวิเคราะห์เป็นโครงสร้าง** - สำหรับการประเมินที่สม่ำเสมอ โมเดลทบทวนโค้ดโดยใช้กรอบที่กำหนดไว้ (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) ใช้สำหรับการตรวจโค้ดหรือประเมินคุณภาพ

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
  
> **🤖 ลองกับ [GitHub Copilot](https://github.com/features/copilot) แชท:** ถามเกี่ยวกับการวิเคราะห์เป็นโครงสร้าง:  
> - "จะปรับกรอบการวิเคราะห์สำหรับประเภทการตรวจโค้ดต่างๆ อย่างไร?"  
> - "วิธีที่ดีที่สุดในการแยกวิเคราะห์และดำเนินการกับผลลัพธ์เป็นโครงสร้างอย่างไร?"  
> - "จะทำให้ระดับความรุนแรงสม่ำเสมอตลอดการตรวจโค้ดต่างๆ ได้อย่างไร?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="รูปแบบการวิเคราะห์เป็นโครงสร้าง" width="800"/>

*กรอบงานสี่หมวดหมู่สำหรับตรวจโค้ดสม่ำเสมอพร้อมระดับความรุนแรง*

**แชทหลายรอบ** - สำหรับบทสนทนาที่ต้องใช้บริบท โมเดลจะจำข้อความก่อนหน้าและสร้างต่อเนื่อง ใช้สำหรับเซสชันช่วยเหลือแบบโต้ตอบหรือ Q&A ที่ซับซ้อน

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/th/context-memory.dff30ad9fa78832a.webp" alt="หน่วยความจำบริบท" width="800"/>

*การสะสมบริบทบทสนทนาในหลายรอบจนถึงขีดจำกัด token*

**การให้เหตุผลทีละขั้นตอน** - สำหรับปัญหาที่ต้องการตรรกะที่มองเห็น โมเดลแสดงเหตุผลอย่างชัดเจนในแต่ละขั้นตอน ใช้สำหรับปัญหาคณิตศาสตร์ เกมตรรกะ หรือเมื่อคุณต้องการเข้าใจกระบวนการคิด

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/th/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="รูปแบบการให้เหตุผลทีละขั้นตอน" width="800"/>

*แยกปัญหาเป็นขั้นตอนตรรกะที่ชัดเจน*

**ผลลัพธ์ที่ถูกจำกัด** - สำหรับคำตอบที่ต้องการรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเคร่งครัด ใช้สำหรับสรุปผลหรือเมื่อคุณต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

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
  
<img src="../../../translated_images/th/constrained-output-pattern.0ce39a682a6795c2.webp" alt="รูปแบบผลลัพธ์ที่ถูกจำกัด" width="800"/>

*บังคับใช้รูปแบบ ความยาว และข้อกำหนดโครงสร้างเฉพาะ*

## การใช้ทรัพยากร Azure ที่มีอยู่

**ตรวจสอบการปรับใช้:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` อยู่ในโฟลเดอร์รากโดยมีข้อมูลรับรอง Azure (สร้างในโมดูล 01):  
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มแอปทั้งหมดด้วย `./start-all.sh` จากโมดูล 01 โมดูลนี้จะรันอยู่แล้วที่พอร์ต 8083 คุณสามารถข้ามคำสั่งสตาร์ทด้านล่างแล้วไปที่ http://localhost:8083 ได้เลย

**ตัวเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

Dev container มีส่วนขยาย Spring Boot Dashboard ซึ่งให้อินเทอร์เฟสแบบภาพสำหรับจัดการแอป Spring Boot ทั้งหมด คุณหาได้ใน Activity Bar ด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)
จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดที่มีในพื้นที่ทำงาน
- เริ่ม/หยุดแอปพลิเคชันได้ด้วยการคลิกเพียงครั้งเดียว
- ดูบันทึกแอปพลิเคชันแบบเรียลไทม์
- ตรวจสอบสถานะของแอปพลิเคชัน

เพียงแค่คลิกปุ่มเล่นข้าง ๆ "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: การใช้สคริปต์เชลล์**

เริ่มแอปพลิเคชันเว็บทั้งหมด (โมดูล 01-04):

**Bash:**
```bash
cd ..  # จากไดเรกทอรีราก
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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

ทั้งสองสคริปต์จะโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ที่รูทโดยอัตโนมัติ และจะสร้าง JAR หากยังไม่มีอยู่

> **หมายเหตุ:** หากคุณต้องการสร้างโมดูลทั้งหมดด้วยตัวเองก่อนเริ่ม:
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

**เพื่อหยุด:**

**Bash:**
```bash
./stop.sh  # เฉพาะโมดูลนี้
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอของแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักแสดงรูปแบบวิศวกรรมคำสั่งทั้งหมด 8 แบบพร้อมลักษณะและกรณีการใช้งาน*

## การสำรวจรูปแบบ

อินเทอร์เฟซเว็บช่วยให้คุณทดลองใช้กลยุทธ์การกระตุ้นต่าง ๆ แต่ละรูปแบบแก้ปัญหาที่แตกต่างกัน — ลองใช้ดูเพื่อดูว่าแต่ละวิธีเหมาะกับสถานการณ์ใด

### ความกระตือรือร้นต่ำ vs สูง

ถามคำถามง่าย ๆ เช่น "15% ของ 200 คืออะไร?" โดยใช้ความกระตือรือร้นต่ำ คุณจะได้รับคำตอบทันทีและตรงไปตรงมา ตอนนี้ถามคำถามซับซ้อน เช่น "ออกแบบกลยุทธ์แคชชิ่งสำหรับ API ที่มีการใช้งานสูง" โดยใช้ความกระตือรือร้นสูง ดูว่ารูปแบบจะชะลอและให้เหตุผลโดยละเอียดอย่างไร รูปแบบเดียวกัน โครงสร้างคำถามเดียวกัน — แต่ prompt บอกว่าต้องคิดมากแค่ไหน

<img src="../../../translated_images/th/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*การคำนวณอย่างรวดเร็วพร้อมเหตุผลน้อยที่สุด*

<img src="../../../translated_images/th/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*กลยุทธ์แคชชิ่งเชิงลึก (2.8MB)*

### การดำเนินงานตามงาน (Tool Preambles)

เวิร์กโฟลว์หลายขั้นตอนได้ประโยชน์จากการวางแผนก่อนล่วงหน้าและการบรรยายความคืบหน้า รูปแบบจะสรุปสิ่งที่จะทำ บรรยายทุกขั้นตอน แล้วสรุปผลลัพธ์

<img src="../../../translated_images/th/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*สร้าง REST endpoint พร้อมบรรยายทีละขั้นตอน (3.9MB)*

### รหัสที่สะท้อนตัวเอง

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะเพียงแค่สร้างโค้ดแล้วยุติ รูปแบบจะสร้างโค้ด ประเมินตามเกณฑ์คุณภาพ กำหนดจุดอ่อน และปรับปรุง คุณจะเห็นมันวนซ้ำจนโค้ดบรรลุมาตรฐานการใช้งานจริง

<img src="../../../translated_images/th/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*บริการตรวจสอบอีเมลครบถ้วน (5.2MB)*

### การวิเคราะห์เชิงโครงสร้าง

การตรวจทานโค้ดต้องการกรอบการประเมินที่สม่ำเสมอ รูปแบบจะวิเคราะห์โค้ดโดยใช้หมวดหมู่คงที่ (ความถูกต้อง, แนวทางปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) พร้อมระดับความรุนแรง

<img src="../../../translated_images/th/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ตรวจทานโค้ดโดยใช้กรอบการทำงาน*

### แชทหลายรอบ

ถามว่า "Spring Boot คืออะไร?" แล้วตามด้วย "ขอยกตัวอย่าง" ทันที รูปแบบจะจดจำคำถามแรกและให้ตัวอย่าง Spring Boot เฉพาะสำหรับคุณ หากไม่มีหน่วยความจำ คำถามที่สองจะคลุมเครือเกินไป

<img src="../../../translated_images/th/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*การรักษาบริบทข้ามคำถาม*

### การให้เหตุผลทีละขั้นตอน

เลือกปัญหาคณิตศาสตร์และลองใช้ทั้งการให้เหตุผลทีละขั้นตอนและความกระตือรือร้นต่ำ ความกระตือรือร้นต่ำจะให้คำตอบเร็วแต่ไม่เปิดเผยรายละเอียด ส่วนการให้เหตุผลทีละขั้นตอนจะแสดงการคำนวณและการตัดสินใจทุกขั้น

<img src="../../../translated_images/th/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*ปัญหาคณิตศาสตร์พร้อมขั้นตอนอธิบาย*

### การบังคับรูปแบบผลลัพธ์

เมื่อคุณต้องการรูปแบบเฉพาะหรือจำนวนคำ รูปแบบนี้จะบังคับให้ปฏิบัติตามอย่างเข้มงวด ลองสร้างสรุปที่มี 100 คำแบบจุดหัวข้อ

<img src="../../../translated_images/th/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*สรุปการเรียนรู้ของเครื่องด้วยการควบคุมรูปแบบ*

## สิ่งที่คุณกำลังเรียนรู้จริง ๆ

**ความพยายามในการให้เหตุผลเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมความพยายามในการคำนวณผ่าน prompt ของคุณ ความพยายามต่ำหมายถึงการตอบสนองอย่างรวดเร็วด้วยการสำรวจน้อย ความพยายามสูงหมายถึงโมเดลใช้เวลาคิดอย่างลึกซึ้ง คุณกำลังเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน — อย่าเสียเวลาคำถามง่าย ๆ แต่ก็อย่ารีบตัดสินใจเรื่องซับซ้อนเกินไป

**โครงสร้างชี้นำพฤติกรรม**

สังเกตแท็ก XML ใน prompt ไหม? มันไม่ใช่แค่การตกแต่ง โมเดลติดตามคำสั่งที่มีโครงสร้างได้แม่นยำกว่าข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือตรรกะซับซ้อน โครงสร้างช่วยให้โมเดลติดตามที่อยู่และสิ่งที่จะทำต่อไปได้

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*โครงสร้างของ prompt ที่ออกแบบดีพร้อมส่วนต่าง ๆ อย่างชัดเจนและการจัดระเบียบแบบ XML*

**คุณภาพผ่านการประเมินตนเอง**

รูปแบบสะท้อนตนเองทำงานโดยการทำให้เกณฑ์คุณภาพชัดเจน แทนที่จะหวังให้โมเดล "ทำถูกต้อง" คุณบอกมันอย่างชัดเจนว่า "ถูกต้อง" หมายถึงอะไรรวมถึงตรรกะที่ถูกต้อง, การจัดการข้อผิดพลาด, ประสิทธิภาพ, ความปลอดภัย โมเดลจึงสามารถประเมินผลลัพธ์ของตัวเองและปรับปรุง นี่ทำให้การสร้างโค้ดเปลี่ยนจากสุ่มเป็นกระบวนการ

**บริบทมีขีดจำกัด**

การสนทนาแบบหลายรอบทำงานด้วยการรวมประวัติข้อความในแต่ละคำขอ แต่มีขีดจำกัด — ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อบทสนทนาเพิ่มขึ้น คุณจะต้องมีกลยุทธ์ในการเก็บบริบทที่เกี่ยวข้องโดยไม่เกินขีดจำกัด โมดูลนี้จะแสดงให้เห็นว่าหน่วยความจำทำงานอย่างไร; ต่อไปคุณจะได้เรียนรู้ว่าเมื่อใดควรสรุป, ลืม, หรือดึงข้อมูล

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**นำทาง:** [← ก่อนหน้า: Module 01 - Introduction](../01-introduction/README.md) | [กลับไปหน้าแรก](../README.md) | [ถัดไป: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้อง แต่อย่างไรก็ตามโปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารฉบับต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ควรใช้การแปลโดยนักแปลมืออาชีพเท่านั้น เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดอันเกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->