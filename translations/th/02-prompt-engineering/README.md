# Module 02: การออกแบบคำสั่งกับ GPT-5.2

## สารบัญ

- [วิดีโอสาธิต](../../../02-prompt-engineering)
- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [ทำความเข้าใจการออกแบบคำสั่ง](../../../02-prompt-engineering)
- [พื้นฐานการออกแบบคำสั่ง](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [แบบแม่แบบคำสั่ง](../../../02-prompt-engineering)
- [รูปแบบขั้นสูง](../../../02-prompt-engineering)
- [การใช้ทรัพยากร Azure ที่มีอยู่](../../../02-prompt-engineering)
- [ภาพหน้าจอแอปพลิเคชัน](../../../02-prompt-engineering)
- [สำรวจรูปแบบต่าง ๆ](../../../02-prompt-engineering)
  - [ความกระตือรือร้นต่ำกับสูง](../../../02-prompt-engineering)
  - [การดำเนินงานตามงาน (คำปรึกษาเครื่องมือ)](../../../02-prompt-engineering)
  - [โค้ดที่สะท้อนตนเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์อย่างมีโครงสร้าง](../../../02-prompt-engineering)
  - [แชทหลายรอบ](../../../02-prompt-engineering)
  - [เหตุผลทีละขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่ถูกจำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้อย่างแท้จริง](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## วิดีโอสาธิต

รับชมเซสชันสดที่อธิบายวิธีเริ่มต้นกับโมดูลนี้: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## สิ่งที่คุณจะได้เรียนรู้

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

ในโมดูลก่อนหน้านี้ คุณได้เห็นว่าหน่วยความจำช่วยให้ AI สนทนาได้อย่างไรและใช้ GitHub Models สำหรับการโต้ตอบพื้นฐาน ตอนนี้เราจะมุ่งเน้นที่วิธีการตั้งคำถาม — คำสั่งเหล่านั้น — โดยใช้ GPT-5.2 ของ Azure OpenAI วิธีที่คุณจัดโครงสร้างคำสั่งจะส่งผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เราเริ่มด้วยการทบทวนเทคนิคการออกแบบคำสั่งพื้นฐาน จากนั้นเราจะเข้าสู่แปดรูปแบบขั้นสูงที่ใช้ประโยชน์จากความสามารถของ GPT-5.2 อย่างเต็มที่

เราจะใช้ GPT-5.2 เพราะมันมีการควบคุมกระบวนการคิด — คุณสามารถบอกโมเดลว่าต้องคิดมากน้อยแค่ไหนก่อนตอบ ทำให้กลยุทธ์การออกแบบคำสั่งชัดเจนขึ้นและช่วยให้คุณเข้าใจว่าเมื่อใดควรใช้วิธีใด นอกจากนี้ เรายังได้ประโยชน์จากข้อจำกัดอัตราการใช้งานที่น้อยกว่าของ GPT-5.2 บน Azure เมื่อเทียบกับ GitHub Models อีกด้วย

## ข้อกำหนดเบื้องต้น

- เสร็จสิ้นโมดูล 01 (ติดตั้งทรัพยากร Azure OpenAI เรียบร้อยแล้ว)
- ไฟล์ `.env` ในไดเรกทอรีหลักที่มีข้อมูลประจำตัว Azure (สร้างโดยคำสั่ง `azd up` ในโมดูล 01)

> **หมายเหตุ:** หากคุณยังไม่ได้ทำโมดูล 01 ให้ทำตามคำแนะนำการติดตั้งในโมดูลนั้นก่อน

## ทำความเข้าใจการออกแบบคำสั่ง

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

การออกแบบคำสั่งคือการออกแบบข้อความป้อนข้อมูลที่ได้ผลลัพธ์ตามที่ต้องการอย่างสม่ำเสมอ ไม่ใช่แค่การถามคำถาม — แต่เป็นการจัดโครงสร้างคำขอให้โมเดลเข้าใจอย่างชัดเจนว่าคุณต้องการอะไรและจะตอบอย่างไร

คิดเหมือนการให้คำสั่งเพื่อนร่วมงาน "แก้บั๊ก" นั้นคลุมเครือ แต่ "แก้ null pointer exception ใน UserService.java บรรทัดที่ 45 ด้วยการตรวจสอบ null" นั้นเจาะจง โมเดลภาษาใช้งานแบบเดียวกัน — ความเฉพาะเจาะจงและโครงสร้างมีความสำคัญ

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ให้โครงสร้างพื้นฐาน — การเชื่อมต่อโมเดล หน่วยความจำ และประเภทข้อความ — ในขณะที่รูปแบบคำสั่งเป็นเพียงข้อความที่จัดโครงสร้างอย่างละเอียดเพื่อส่งผ่านโครงสร้างพื้นฐานนั้น องค์ประกอบสำคัญคือ `SystemMessage` (ตั้งพฤติกรรมและบทบาท AI) และ `UserMessage` (ส่งคำขอของคุณจริง ๆ)

## พื้นฐานการออกแบบคำสั่ง

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ก่อนจะเข้าสู่รูปแบบขั้นสูงในโมดูลนี้ ให้เราทบทวนห้าเทคนิคพื้นฐานของการออกแบบคำสั่ง เหล่านี้เป็นบล็อกสร้างพื้นฐานที่วิศวกรคำสั่งทุกคนควรรู้ หากคุณเคยทำผ่าน [โมดูลเริ่มต้นด่วน](../00-quick-start/README.md#2-prompt-patterns) มาก่อน คุณจะเห็นวิธีการเหล่านี้ในทางปฏิบัติ — นี่คือกรอบแนวคิดเบื้องหลัง

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้คำสั่งตรง ๆ กับโมเดลโดยไม่มีตัวอย่าง โมเดลพึ่งพาการฝึกทั้งหมดในการเข้าใจและดำเนินงาน งานนี้เหมาะกับคำขอที่ชัดเจนและคาดหวังผลลัพธ์ได้อย่างตรงไปตรงมา

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรงโดยไม่มีตัวอย่าง — โมเดลสรุปงานจากคำสั่งเพียงอย่างเดียว*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// คำตอบ: "บวก"
```

**เมื่อใดควรใช้:** งานจัดประเภทง่าย ๆ คำถามตรง ๆ การแปล หรือภารกิจใด ๆ ที่โมเดลจัดการได้โดยไม่ต้องมีคำแนะนำเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่คุณต้องการให้โมเดลทำตาม โมเดลเรียนรู้รูปแบบป้อน-ออกที่คาดหวังจากตัวอย่างของคุณและใช้กับอินพุตใหม่ วิธีนี้ช่วยเพิ่มความสม่ำเสมอสำหรับงานที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*เรียนรู้จากตัวอย่าง — โมเดลระบุรูปแบบและใช้กับอินพุตใหม่*

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

**เมื่อใดควรใช้:** การจัดประเภทเฉพาะงาน การจัดรูปแบบที่สม่ำเสมอ งานในโดเมนเฉพาะ หรือเมื่อผลลัพธ์ zero-shot ไม่สม่ำเสมอ

### Chain of Thought

ขอให้โมเดลแสดงเหตุผลทีละขั้น โมเดลจะแยกปัญหาและทำแต่ละส่วนอย่างชัดเจนแทนที่จะตอบทันที ช่วยเพิ่มความแม่นยำสำหรับการคำนวณ, ตรรกะ, และงานที่ต้องวิเคราะห์หลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*เหตุผลทีละขั้นตอน — แยกปัญหาซับซ้อนเป็นขั้นตอนตรรกะที่ชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ตัวแบบแสดง: 15 - 8 = 7 แล้ว 7 + 12 = 19 แอปเปิล
```

**เมื่อใดควรใช้:** ปัญหาคณิตศาสตร์, ปริศนาตรรกะ, การดีบัก หรือภารกิจที่การแสดงกระบวนการคิดช่วยเพิ่มความแม่นยำและความน่าเชื่อถือ

### Role-Based Prompting

ตั้งตัวตนหรือบทบาทของ AI ก่อนถามคำถาม วิธีนี้สร้างบริบทที่กำหนดน้ำเสียง ความลึก และจุดโฟกัสของคำตอบ "สถาปนิกซอฟต์แวร์" ให้คำแนะนำแตกต่างจาก "นักพัฒนารุ่นเยาว์" หรือ "ผู้ตรวจสอบความปลอดภัย"

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*กำหนดบริบทและบุคลิก — คำถามเดียวกัน จะได้คำตอบแตกต่างตามบทบาทที่กำหนด*

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

**เมื่อใดควรใช้:** การตรวจสอบโค้ด, สอนพิเศษ, การวิเคราะห์โดเมนเฉพาะ, หรือเมื่อคุณต้องการคำตอบที่ปรับแต่งตามระดับความเชี่ยวชาญหรือมุมมองเฉพาะ

### Prompt Templates

สร้างคำสั่งที่ใช้ซ้ำได้โดยมีตัวแปรแทนที่ แทนที่จะเขียนคำสั่งใหม่ทุกครั้ง ให้กำหนดแม่แบบครั้งเดียวแล้วเติมค่าแตกต่างกัน คลาส `PromptTemplate` ของ LangChain4j ช่วยให้ทำได้ง่ายด้วยไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*คำสั่งที่ใช้ซ้ำได้พร้อมตัวแทนที่ตัวแปร — แม่แบบเดียว ใช้งานได้หลายครั้ง*

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

**เมื่อใดควรใช้:** คำถามซ้ำที่มีอินพุตแตกต่างกัน, การประมวลผลเป็นชุด, การสร้างเวิร์กโฟลว์ AI ที่ใช้ซ้ำได้, หรือทุกสถานการณ์ที่โครงสร้างคำสั่งเหมือนเดิมแต่ข้อมูลเปลี่ยนแปลง

---

พื้นฐานทั้งห้านี้ให้เครื่องมือที่แข็งแกร่งสำหรับงานออกแบบคำสั่งส่วนใหญ่ ส่วนที่เหลือของโมดูลนี้จะต่อยอดด้วย **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมกระบวนการคิดของ GPT-5.2 การประเมินตนเอง และความสามารถในการให้ผลลัพธ์ที่มีโครงสร้าง

## รูปแบบขั้นสูง

หลังจากครอบคลุมพื้นฐานแล้ว มาดูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ปัญหาไม่ใช่ทั้งหมดที่ใช้แนวทางเดียวกัน คำถามบางอย่างต้องการคำตอบรวดเร็ว บางอย่างต้องการความคิดลึกซึ้ง บางอย่างต้องการแสดงเหตุผล เมื่อบางอย่างต้องการแค่ผลลัพธ์ รูปแบบแต่ละแบบด้านล่างถูกปรับให้เหมาะกับสถานการณ์ต่าง ๆ — และการควบคุมเหตุผลของ GPT-5.2 ช่วยให้ความแตกต่างเหล่านี้ชัดเจนขึ้น

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ภาพรวมของแปดรูปแบบการออกแบบคำสั่งและกรณีการใช้งาน*

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*การควบคุมกระบวนการคิดของ GPT-5.2 ให้คุณกำหนดได้ว่าโมเดลต้องคิดมากน้อยแค่ไหน — ตั้งแต่ตอบทันทีอย่างรวดเร็วถึงสำรวจอย่างลึกซึ้ง*

**ความกระตือรือร้นต่ำ (รวดเร็วและตรงประเด็น)** – สำหรับคำถามง่าย ๆ ที่ต้องการคำตอบรวดเร็วและตรงไปตรงมา โมเดลจะคิดน้อยที่สุด — สูงสุด 2 ขั้นตอน ใช้วิธีนี้สำหรับการคำนวณ, การค้นหา, หรือคำถามตรง ๆ

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **ลองใช้กับ GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) และถาม:
> - "ความแตกต่างระหว่างรูปแบบความกระตือรือร้นต่ำและสูงคืออะไร?"
> - "แท็ก XML ในคำสั่งช่วยจัดโครงสร้างคำตอบ AI อย่างไร?"
> - "เมื่อไรควรใช้รูปแบบการสะท้อนตนเองกับคำสั่งตรง ๆ?"

**ความกระตือรือร้นสูง (ลึกและละเอียด)** – สำหรับปัญหาที่ซับซ้อนที่ต้องการการวิเคราะห์โดยละเอียด โมเดลจะค้นหาอย่างละเอียดและแสดงเหตุผลอย่างละเอียด ใช้สำหรับการออกแบบระบบ, การตัดสินใจเชิงสถาปัตยกรรม หรือการวิจัยซับซ้อน

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินงานตามงาน (ความก้าวหน้าเป็นขั้นตอน)** – สำหรับเวิร์กโฟลว์หลายขั้นตอน โมเดลเสนอแผนล่วงหน้า รายงานแต่ละขั้นตอนขณะดำเนินงาน แล้วสรุปผล ใช้กับการย้ายระบบ, การดำเนินการ, หรือกระบวนการหลายขั้นตอนอื่น ๆ

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

การออกแบบคำสั่งแบบ Chain-of-Thought ขอให้โมเดลแสดงกระบวนการคิดอย่างชัดเจน ช่วยเพิ่มความแม่นยำสำหรับงานซับซ้อน การแยกเป็นขั้นตอนช่วยให้ทั้งมนุษย์และ AI เข้าใจตรรกะ

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับรูปแบบนี้:
> - "จะปรับรูปแบบการดำเนินงานได้อย่างไรสำหรับงานที่ใช้เวลานาน?"
> - "แนวทางปฏิบัติที่ดีที่สุดสำหรับจัดโครงสร้างคำปรึกษาเครื่องมือในแอปจริงคืออะไร?"
> - "จะจับและแสดงการอัปเดตความก้าวหน้าระหว่างดำเนินการใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*เวิร์กโฟลว์ แผน → ดำเนินงาน → สรุป สำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตนเอง** – สำหรับการสร้างโค้ดคุณภาพระดับผลิต โมเดลสร้างโค้ดตามมาตรฐานการผลิต มีการจัดการข้อผิดพลาดอย่างเหมาะสม ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*วงจรปรับปรุงแบบวนซ้ำ — สร้าง, ประเมิน, ระบุปัญหา, ปรับปรุง, ทำซ้ำ*

**การวิเคราะห์อย่างมีโครงสร้าง** – สำหรับการประเมินอย่างสม่ำเสมอ โมเดลตรวจโค้ดโดยใช้กรอบงานที่กำหนดไว้ล่วงหน้า (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย, การดูแลรักษา) ใช้สำหรับตรวจโค้ดหรือประเมินคุณภาพ

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับการวิเคราะห์อย่างมีโครงสร้าง:
> - "จะปรับแต่งกรอบการวิเคราะห์สำหรับการตรวจโค้ดประเภทต่าง ๆ ได้อย่างไร?"
> - "วิธีที่ดีที่สุดในการแยกวิเคราะห์และทำงานกับผลลัพธ์ที่มีโครงสร้างโปรแกรมคืออะไร?"
> - "จะทำให้ระดับความรุนแรงสอดคล้องกันในหลายเซสชันการตรวจสอบได้อย่างไร?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*กรอบงานสำหรับตรวจโค้ดอย่างสม่ำเสมอพร้อมระดับความรุนแรง*

**แชทหลายรอบ** – สำหรับการสนทนาที่ต้องการบริบท โมเดลจำข้อความก่อนหน้าและต่อยอดจากนั้น ใช้สำหรับการช่วยเหลือแบบโต้ตอบหรือถามตอบที่ซับซ้อน

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

*บริบทการสนทนาสะสมผ่านหลายรอบจนถึงขีดจำกัดโทเค็น*

**เหตุผลทีละขั้นตอน** – สำหรับปัญหาที่ต้องการตรรกะที่เห็นได้ชัด โมเดลแสดงเหตุผลที่ชัดเจนในแต่ละขั้นตอน ใช้กับปัญหาคณิตศาสตร์, ปริศนาตรรกะ, หรือเมื่อต้องการเข้าใจกระบวนการคิด

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

*แยกปัญหาเป็นขั้นตอนตรรกะที่ชัดเจน*

**ผลลัพธ์ที่ถูกจำกัด** – สำหรับคำตอบที่มีข้อกำหนดรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเคร่งครัด ใช้กับบทสรุปหรือเมื่อคุณต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

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

*บังคับใช้รูปแบบ, ความยาว, และข้อกำหนดโครงสร้างเฉพาะ*

## การใช้ทรัพยากร Azure ที่มีอยู่

**ตรวจสอบการติดตั้ง:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` อยู่ในไดเรกทอรีหลักและมีข้อมูลประจำตัว Azure (สร้างในโมดูล 01):
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มทุกแอปพลิเคชันแล้วโดยใช้ `./start-all.sh` จากโมดูล 01 โมดูลนี้จะทำงานแล้วบนพอร์ต 8083 คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่างและไปที่ http://localhost:8083 ได้เลย

**ตัวเลือก 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**
Dev container รวมถึงส่วนขยาย Spring Boot Dashboard ซึ่งให้ส่วนติดต่อแบบกราฟิกเพื่อจัดการแอปพลิเคชัน Spring Boot ทั้งหมด คุณสามารถหาได้ใน Activity Bar ทางด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดที่มีอยู่ใน workspace
- เริ่ม/หยุดแอปพลิเคชันด้วยคลิกเดียว
- ดูล็อกแอปพลิเคชันแบบเรียลไทม์
- ตรวจสอบสถานะแอปพลิเคชัน

เพียงคลิกปุ่มเล่นถัดจาก "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มทุกโมดูลพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มเว็บแอปพลิเคชันทั้งหมด (โมดูล 01-04):

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

หรือเริ่มเพียงโมดูลนี้:

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

ทั้งสองสคริปต์จะโหลดตัวแปรสภาพแวดล้อมโดยอัตโนมัติจากไฟล์ `.env` ที่รูท และจะสร้าง JAR หากยังไม่มีอยู่

> **หมายเหตุ:** หากคุณต้องการสร้างโมดูลทั้งหมดด้วยตนเองก่อนเริ่ม:
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

## ภาพหน้าจอแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักที่แสดงรูปแบบ prompt engineering ทั้ง 8 พร้อมลักษณะและกรณีการใช้งาน*

## การสำรวจรูปแบบ

อินเทอร์เฟซเว็บช่วยให้คุณทดลองใช้กลยุทธ์โปรンプต์ต่างๆ รูปแบบแต่ละแบบแก้ปัญหาที่ต่างกัน — ลองใช้ดูเพื่อดูว่าแต่ละวิธีทำงานเมื่อไหร่

> **หมายเหตุ: สตรีมมิ่ง vs ไม่สตรีมมิ่ง** — หน้าแต่ละรูปแบบมีปุ่มสองปุ่ม: **🔴 Stream Response (Live)** และตัวเลือก **Non-streaming** สตรีมมิ่งใช้ Server-Sent Events (SSE) เพื่อแสดงโทเค็นแบบเรียลไทม์ขณะที่โมเดลกำลังสร้าง ดังนั้นคุณจะเห็นความก้าวหน้าทันที ตัวเลือกแบบไม่สตรีมมิ่งจะรอจนตอบกลับทั้งหมดเสร็จก่อนแสดง สำหรับโปรมต์ที่ต้องใช้การวิเคราะห์ลึก (เช่น High Eagerness, Self-Reflecting Code) การเรียกแบบไม่สตรีมมิ่งอาจใช้เวลานานมาก — บางครั้งเป็นนาที — โดยไม่มีการตอบสนองที่เห็นได้ชัด **ใช้สตรีมมิ่งเมื่อทดลองกับโปรมต์ที่ซับซ้อน** เพื่อดูการทำงานของโมเดลและหลีกเลี่ยงความรู้สึกว่าการร้องขอหมดเวลาลงแล้ว
>
> **หมายเหตุ: ความต้องการของเบราว์เซอร์** — ฟีเจอร์สตรีมมิ่งใช้ Fetch Streams API (`response.body.getReader()`) ซึ่งต้องใช้เบราว์เซอร์เต็มรูปแบบ (Chrome, Edge, Firefox, Safari) ไม่ทำงานใน Simple Browser ในตัวของ VS Code เนื่องจาก webview ไม่รองรับ ReadableStream API หากคุณใช้ Simple Browser ปุ่มแบบไม่สตรีมมิ่งยังใช้งานได้ตามปกติ — ส่วนปุ่มสตรีมมิ่งจะได้รับผลกระทบ เปิด `http://localhost:8083` ในเบราว์เซอร์ภายนอกเพื่อประสบการณ์ครบถ้วน

### Low vs High Eagerness

ถามคำถามง่ายๆ เช่น "15% ของ 200 คืออะไร?" โดยใช้ Low Eagerness คุณจะได้รับคำตอบตรงและทันที ลองถามอะไรที่ซับซ้อนเช่น "ออกแบบกลยุทธ์แคชชิงสำหรับ API ที่มีผู้ใช้สูง" โดยใช้ High Eagerness คลิก **🔴 Stream Response (Live)** และดูเหตุผลเชิงลึกของโมเดลปรากฏทีละโทเค็น โมเดลเดียวกัน โครงสร้างคำถามเดียวกัน — แต่โปรมต์กำหนดว่าคิดมากแค่ไหน

### การดำเนินงานตามงาน (Tool Preambles)

เวิร์กโฟลว์หลายขั้นตอนได้ประโยชน์จากการวางแผนล่วงหน้าและการบรรยายความก้าวหน้า โมเดลจะร่างสิ่งที่จะทำ บรรยายแต่ละขั้นตอน แล้วสรุปผลลัพธ์

### Self-Reflecting Code

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะสร้างโค้ดแล้วหยุด โมเดลจะสร้าง ประเมินตามเกณฑ์คุณภาพ หาจุดอ่อน และปรับปรุง คุณจะเห็นการทำซ้ำจนกว่าโค้ดจะถึงมาตรฐานการผลิต

### การวิเคราะห์เชิงโครงสร้าง

การตรวจสอบโค้ดต้องการกรอบการประเมินที่สม่ำเสมอ โมเดลจะวิเคราะห์โค้ดโดยใช้หมวดหมู่คงที่ (ความถูกต้อง แนวทางปฏิบัติ ประสิทธิภาพ ความปลอดภัย) พร้อมระดับความรุนแรง

### การสนทนาแบบหลายเทิร์น

ถาม "Spring Boot คืออะไร?" แล้วตามด้วย "ขอยกตัวอย่าง" ทันที โมเดลจะจำคำถามแรกและให้ตัวอย่าง Spring Boot โดยเฉพาะ หากไม่มีความจำ คำถามที่สองจะกว้างเกินไป

### การคิดทีละขั้นตอน

เลือกปัญหาคณิตศาสตร์แล้วลองใช้ทั้ง Step-by-Step Reasoning กับ Low Eagerness Low eagerness ให้คำตอบเร็วแต่ไม่ชัดเจน การคิดทีละขั้นตอนจะแสดงการคำนวณและการตัดสินใจทั้งหมด

### ข้อจำกัดผลลัพธ์

เมื่อคุณต้องการรูปแบบหรือจำนวนคำเฉพาะ รูปแบบนี้จะบังคับใช้ข้อกำหนดอย่างเคร่งครัด ลองสร้างสรุปที่มีคำ 100 คำตรงตามรูปแบบหัวข้อย่อย

## สิ่งที่คุณกำลังเรียนรู้จริงๆ

**ความพยายามในการคิดเปลี่ยนทุกสิ่ง**

GPT-5.2 ช่วยให้คุณควบคุมความพยายามในการคำนวณผ่านโปรมต์ของคุณ ความพยายามต่ำหมายถึงการตอบสนองรวดเร็วด้วยการสำรวจน้อย ความพยายามสูงหมายถึงโมเดลใช้เวลาในการคิดลึก คุณกำลังเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน — ไม่เสียเวลากับคำถามง่ายๆ แต่ก็ไม่รีบเร่งการตัดสินใจที่ซับซ้อนเช่นกัน

**โครงสร้างนำพฤติกรรม**

สังเกตแท็ก XML ในโปรมต์ไหม? มันไม่ใช่แค่ของตกแต่ง โมเดลทำตามคำสั่งที่มีโครงสร้างได้แม่นยำกว่าข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือตรรกะซับซ้อน โครงสร้างช่วยให้โมเดลติดตามวิเคราะห์ตำแหน่งและขั้นตอนถัดไปได้

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*โครงสร้างของโปรมต์ที่มีการจัดส่วนที่ชัดเจนและจัดแบบ XML*

**คุณภาพผ่านการประเมินตนเอง**

รูปแบบ self-reflecting ทำงานโดยการระบุเกณฑ์คุณภาพอย่างชัดเจน แทนที่จะหวังว่าโมเดลจะ "ทำถูก" คุณบอกให้มันรู้ว่า "ถูก" หมายถึงอะไร: ตรรกะถูกต้อง การจัดการข้อผิดพลาด ประสิทธิภาพ ความปลอดภัย จากนั้นโมเดลสามารถประเมินผลของตนเองและปรับปรุง ซึ่งเปลี่ยนการสร้างโค้ดจากการเล่นลอตเตอรี่ไปเป็นกระบวนการ

**บริบทมีจำกัด**

การสนทนาแบบหลายเทิร์นทำงานโดยรวมประวัติข้อความในแต่ละคำขอ แต่มีขีดจำกัด — ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อบทสนทนายาวขึ้น คุณจะต้องมีกลยุทธ์เก็บบริบทที่เกี่ยวข้องโดยไม่เกินขีดจำกัด โมดูลนี้แสดงให้เห็นว่าความจำทำงานอย่างไร; ต่อไปคุณจะเรียนรู้เมื่อสรุป เมื่อจะลืม และเมื่อจะเรียกคืน

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**การนำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาแบบอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้ความถูกต้องสูงสุด โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อน เอกสารต้นฉบับในภาษาต้นฉบับควรถูกพิจารณาเป็นแหล่งข้อมูลที่มีอำนาจ สำหรับข้อมูลสำคัญ แนะนำให้ใช้บริการแปลโดยมืออาชีพที่เป็นมนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->