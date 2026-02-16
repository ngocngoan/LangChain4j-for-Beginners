# Module 02: การออกแบบ Prompt กับ GPT-5.2

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [สิ่งที่ต้องมีเบื้องต้น](../../../02-prompt-engineering)
- [ความเข้าใจในการออกแบบ Prompt](../../../02-prompt-engineering)
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
  - [โค้ดที่สะท้อนตนเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์แบบมีโครงสร้าง](../../../02-prompt-engineering)
  - [แชทแบบหลายรอบ](../../../02-prompt-engineering)
  - [การให้เหตุผลทีละขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่ถูกจำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้จริงๆ](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## สิ่งที่คุณจะได้เรียนรู้

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

ในโมดูลก่อนหน้านี้ คุณได้เห็นว่าหน่วยความจำช่วยให้ AI สนทนาได้อย่างไร และใช้ GitHub Models สำหรับการโต้ตอบขั้นพื้นฐาน ตอนนี้เราจะเน้นที่วิธีการถามคำถาม — คำสั่ง prompt เอง — โดยใช้ GPT-5.2 ของ Azure OpenAI การจัดโครงสร้าง prompt ของคุณส่งผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เราจะเริ่มจากการทบทวนเทคนิคการ prompt พื้นฐาน จากนั้นไปยังแปดรูปแบบขั้นสูงที่ใช้ประโยชน์เต็มที่จากความสามารถของ GPT-5.2

เราใช้ GPT-5.2 เพราะมันแนะนำการควบคุมการให้เหตุผล — คุณสามารถบอกโมเดลได้ว่าจะให้คิดมากแค่ไหนก่อนตอบ วิธีนี้ทำให้กลยุทธ์การ prompt แตกต่างกันชัดเจนขึ้นและช่วยให้คุณเข้าใจเวลาใช้งานแต่ละวิธี นอกจากนี้ยังได้ประโยชน์จากข้อจำกัดอัตราการใช้ GPT-5.2 ใน Azure ที่น้อยกว่า GitHub Models

## สิ่งที่ต้องมีเบื้องต้น

- ผ่านการทำโมดูล 01 (ทรัพยากร Azure OpenAI ถูกปรับใช้)
- มีไฟล์ `.env` ในไดเรกทอรีหลัก พร้อมข้อมูลรับรอง Azure (สร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** ถ้าคุณยังไม่ได้ทำโมดูล 01 กรุณาทำตามคำแนะนำการปรับใช้ที่นั่นก่อน

## ความเข้าใจในการออกแบบ Prompt

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

การออกแบบ prompt คือการสร้างข้อความเข้า (input) ที่ช่วยให้คุณได้ผลลัพธ์ที่ต้องการอย่างสม่ำเสมอ ไม่ใช่แค่การถามคำถาม — แต่เป็นการจัดโครงสร้างคำขอเพื่อให้โมเดลเข้าใจอย่างชัดเจนว่าคุณต้องการอะไรและจะส่งมอบอย่างไร

คิดเหมือนกับการให้คำสั่งเพื่อนร่วมงาน “แก้ไขข้อผิดพลาด” นั้นคลุมเครือ แต่ “แก้ไข exception null pointer ใน UserService.java บรรทัด 45 โดยเพิ่มการตรวจสอบ null” นั้นชัดเจน โมเดลภาษาเองก็เป็นแบบเดียวกัน — ความเฉพาะเจาะจงและโครงสร้างสำคัญมาก

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ให้โครงสร้างพื้นฐาน — การเชื่อมต่อโมเดล หน่วยความจำ และประเภทข้อความ — ในขณะที่รูปแบบ prompt เป็นเพียงข้อความที่ออกแบบมาอย่างระมัดระวังซึ่งส่งผ่านโครงสร้างพื้นฐานนั้น ก้อนหลักคือ `SystemMessage` (ซึ่งกำหนดพฤติกรรมและบทบาทของ AI) และ `UserMessage` (ซึ่งเป็นคำขอของคุณจริงๆ)

## พื้นฐานการออกแบบ Prompt

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ก่อนลงลึกสู่รูปแบบขั้นสูงในโมดูลนี้ เรามาทบทวนเทคนิคการ prompt พื้นฐานห้ารูปแบบก่อน นี่คือก้อนเครื่องมือพื้นฐานที่นักออกแบบ prompt ทุกคนควรรู้ หากคุณเคยทำโมดูล [Quick Start](../00-quick-start/README.md#2-prompt-patterns) มาแล้ว คุณจะเคยเห็นมันในงาน — นี่คือกรอบแนวคิดเบื้องหลัง

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้คำสั่งตรงๆ กับโมเดลโดยไม่มีตัวอย่าง โมเดลพึ่งพาการฝึกสอนเพื่อเข้าใจและดำเนินงานตามคำสั่ง วิธีนี้เหมาะกับงานตรงไปตรงมาที่พฤติกรรมที่คาดหวังชัดเจน

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรงโดยไม่มีตัวอย่าง — โมเดลสรุปงานจากคำสั่งนั้นเพียงอย่างเดียว*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// การตอบกลับ: "บวก"
```

**เมื่อใช้:** การจัดประเภทง่ายๆ คำถามตรง การแปล หรือทุกงานที่โมเดลสามารถจัดการได้โดยไม่ต้องมีคำแนะนำเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่ต้องการให้โมเดลทำตาม โมเดลเรียนรู้รูปแบบ input-output ที่คาดหวังจากตัวอย่างเหล่านั้น และนำไปใช้กับ input ใหม่ วิธีนี้ช่วยเพิ่มความสม่ำเสมอสำหรับงานที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*เรียนรู้จากตัวอย่าง — โมเดลระบุรูปแบบและใช้กับ input ใหม่*

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

**เมื่อใช้:** การจัดประเภทเฉพาะ การจัดรูปแบบอย่างสม่ำเสมอ งานเฉพาะโดเมน หรือเมื่อผล zero-shot ไม่เสถียร

### Chain of Thought

ขอให้โมเดลแสดงเหตุผลทีละขั้นตอน แทนที่จะตอบทันที โมเดลจะแยกปัญหาและทำงานผ่านแต่ละส่วนอย่างชัดเจน วิธีนี้ช่วยเพิ่มความแม่นยำในงานคณิตศาสตร์ ตรรกะ และการให้เหตุผลหลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*ให้เหตุผลทีละขั้นตอน — แยกปัญหาที่ซับซ้อนเป็นขั้นตอนตรรกะที่ชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// แบบจำลองแสดง: 15 - 8 = 7 จากนั้น 7 + 12 = 19 แอปเปิล
```

**เมื่อใช้:** ปัญหาคณิตศาสตร์ ปริศนาตรรกะ การดีบัก หรือทุกงานที่การแสดงกระบวนการให้เหตุผลช่วยเพิ่มความแม่นยำและความน่าเชื่อถือ

### Role-Based Prompting

กำหนดบุคลิกหรือบทบาทให้ AI ก่อนถามคำถาม วิธีนี้ให้บริบทที่กำหนดโทนเสียง ความลึก และจุดสนใจของคำตอบ “สถาปนิกซอฟต์แวร์” ให้คำแนะนำต่างจาก “นักพัฒนาระดับต้น” หรือ “ผู้ตรวจสอบความปลอดภัย”

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*กำหนดบริบทและบทบาท — คำถามเดียวได้รับคำตอบแตกต่างกันตามบทบาทที่ได้รับ*

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

**เมื่อใช้:** การตรวจทานโค้ด การสอน การวิเคราะห์เฉพาะโดเมน หรือเมื่อคุณต้องการคำตอบที่เหมาะกับระดับความเชี่ยวชาญหรือมุมมองเฉพาะ

### Prompt Templates

สร้าง prompt ที่ใช้ซ้ำได้โดยมีตัวแปรแทนที่ แทนที่จะเขียน prompt ใหม่ทุกครั้ง กำหนดแม่แบบครั้งเดียวและใส่ค่าต่างกัน คลาส `PromptTemplate` ของ LangChain4j ทำให้ง่ายด้วยไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt ใช้ซ้ำได้พร้อมตัวแปรแทนที่ — แม่แบบหนึ่ง ใช้งานหลากหลาย*

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

**เมื่อใช้:** คำถามที่ต้องทำซ้ำพร้อมข้อมูลต่างกัน การประมวลผลเป็นชุด การสร้างโฟลว์งาน AI ที่ใช้ซ้ำ หรือตัวอย่างที่โครงสร้าง prompt เท่าเดิมแต่ข้อมูลเปลี่ยน

---

พื้นฐานทั้งห้ารูปแบบนี้ให้ชุดเครื่องมือที่มั่นคงสำหรับงาน prompt ส่วนใหญ่ โมดูลนี้สร้างต่อจากนั้นด้วย **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมการให้เหตุผล การประเมินตนเอง และความสามารถในการสร้างผลลัพธ์เชิงโครงสร้างของ GPT-5.2

## รูปแบบขั้นสูง

เมื่อครอบคลุมพื้นฐานแล้ว มาเข้าสูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ปัญหาทั้งหมดไม่จำเป็นต้องใช้วิธีเดียวกัน บางคำถามต้องการคำตอบเร็ว บางคำถามต้องการการคิดลึก บางคำถามต้องการเห็นเหตุผล บางคำถามแค่ต้องการผลลัพธ์เดียว แต่ละรูปแบบด้านล่างนี้เหมาะกับสถานการณ์ต่างกัน — และการควบคุมเหตุผลของ GPT-5.2 ทำให้ความแตกต่างชัดเจนขึ้นอีก

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ภาพรวมแปดรูปแบบการออกแบบ prompt และกรณีการใช้งาน*

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*การควบคุมการให้เหตุผลของ GPT-5.2 ให้คุณกำหนดได้ว่าโมเดลจะคิดมากแค่ไหน — ตั้งแต่ตอบตรงเร็ว ไปจนถึงสำรวจอย่างลึกซึ้ง*

<img src="../../../translated_images/th/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*ความกระตือรือร้นต่ำ (เร็ว ตรงไปตรงมา) vs ความกระตือรือร้นสูง (ละเอียด สำรวจ)*

**ความกระตือรือร้นต่ำ (รวดเร็ว & เน้นตรงประเด็น)** — สำหรับคำถามง่ายที่คุณต้องการคำตอบเร็วและตรงไปตรงมา โมเดลใช้เหตุผลน้อยที่สุด — สูงสุด 2 ขั้นตอน ใช้กับการคำนวณ การค้นหา หรือคำถามตรงๆ

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

> 💡 **สำรวจด้วย GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) และถาม:
> - "ความแตกต่างระหว่างรูปแบบ prompt ความกระตือรือร้นต่ำและสูงคืออะไร?"
> - "แท็ก XML ใน prompt ช่วยจัดโครงสร้างการตอบของ AI อย่างไร?"
> - "เมื่อใดควรใช้รูปแบบสะท้อนตนเอง กับคำสั่งตรง?"

**ความกระตือรือร้นสูง (ลึกซึ้ง & รอบคอบ)** — สำหรับปัญหาซับซ้อนที่ต้องการการวิเคราะห์ครบถ้วน โมเดลสำรวจอย่างละเอียดและแสดงเหตุผลอย่างละเอียด ใช้กับการออกแบบระบบ การตัดสินใจด้านสถาปัตยกรรม หรือการวิจัยซับซ้อน

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินงานตามงาน (ความคืบหน้าทีละขั้นตอน)** — สำหรับโฟลว์งานหลายขั้นตอน โมเดลจะเสนอแผนงานล่วงหน้า บรรยายแต่ละขั้นตอนขณะทำงาน จากนั้นสรุป ใช้กับการย้ายระบบ การทำงานจริง หรือกระบวนการหลายขั้นตอน

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

การให้เหตุผลแบบ Chain-of-Thought ขอให้โมเดลแสดงกระบวนการให้เหตุผล ช่วยเพิ่มความแม่นยำของงานซับซ้อน การแยกย่อยทีละขั้นตอนช่วยให้มนุษย์และ AI เข้าใจตรรกะ

> **🤖 ทดลองกับแชท [GitHub Copilot](https://github.com/features/copilot):** ถามเกี่ยวกับรูปแบบนี้:
> - "ฉันจะปรับรูปแบบการดำเนินงานตามงานสำหรับงานที่ใช้เวลานานอย่างไร?"
> - "แนวทางปฏิบัติที่ดีที่สุดในการจัดโครงสร้างคำอธิบายเครื่องมือในแอปพลิเคชันจริงคืออะไร?"
> - "จะจับและแสดงความคืบหน้ากลางทางใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*วางแผน → ดำเนินงาน → สรุป สำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตนเอง** — สำหรับการสร้างโค้ดคุณภาพสำหรับผลิตจริง โมเดลจะสร้างโค้ดตามมาตรฐานการผลิต พร้อมจัดการข้อผิดพลาดอย่างเหมาะสม ใช้เมื่อต้องสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*วงจรปรับปรุงแบบวนซ้ำ - สร้าง ประเมิน ระบุปัญหา ปรับปรุง ทำซ้ำ*

**การวิเคราะห์แบบมีโครงสร้าง** — สำหรับการประเมินสม่ำเสมอ โมเดลตรวจโค้ดโดยใช้กรอบการทำงานตายตัว (ความถูกต้อง แนวปฏิบัติ ประสิทธิภาพ ความปลอดภัย การดูแลรักษา) ใช้ในการตรวจโค้ดหรือประเมินคุณภาพ

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

> **🤖 ทดลองกับแชท [GitHub Copilot](https://github.com/features/copilot):** ถามเกี่ยวกับการวิเคราะห์แบบมีโครงสร้าง:
> - "ฉันจะปรับกรอบการวิเคราะห์สำหรับการตรวจโค้ดในประเภทต่างๆ ได้อย่างไร?"
> - "วิธีที่ดีที่สุดในการแยกวิเคราะห์และทำงานกับผลลัพธ์โครงสร้างทางโปรแกรมคืออะไร?"
> - "ฉันทำอย่างไรให้ระดับความรุนแรงคงที่ในแต่ละเซสชันตรวจโค้ด?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*กรอบงานสำหรับตรวจโค้ดอย่างต่อเนื่องพร้อมระดับความรุนแรง*

**แชทแบบหลายรอบ** — สำหรับการสนทนาที่ต้องการบริบท โมเดลจำข้อความก่อนหน้าและสร้างจากนั้น ใช้กับการช่วยเหลือแบบโต้ตอบหรือ Q&A ซับซ้อน

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

*บริบทการสนทนาสะสมหลายรอบจนถึงขีดจำกัดโทเค็น*

**การให้เหตุผลทีละขั้นตอน** — สำหรับปัญหาที่ต้องเห็นตรรกะ โมเดลจะแสดงเหตุผลอย่างชัดเจนทุกขั้นตอน ใช้กับปัญหาคณิตศาสตร์ ปริศนาตรรกะ หรือเมื่อต้องการเข้าใจกระบวนการคิด

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

**ผลลัพธ์ที่ถูกจำกัด** — สำหรับคำตอบที่ต้องการรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเคร่งครัด ใช้กับบทสรุปหรือเมื่อต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

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

**ตรวจสอบการปรับใช้:**

ตรวจสอบว่าไฟล์ `.env` มีในไดเรกทอรีหลัก พร้อมข้อมูลรับรอง Azure (สร้างในโมดูล 01):
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มแอปพลิเคชันทั้งหมดแล้วด้วย `./start-all.sh` จากโมดูล 01 โมดูลนี้จะรันที่พอร์ต 8083 เรียบร้อยแล้ว คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่าง และไปที่ http://localhost:8083 ได้เลย

**ตัวเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

ใน dev container มีส่วนขยาย Spring Boot Dashboard ซึ่งให้ส่วนติดต่อภาพสำหรับจัดการแอปพลิเคชัน Spring Boot ทั้งหมด คุณจะพบใน Activity Bar ทางซ้ายของ VS Code (มองหาไอคอน Spring Boot)
จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดที่มีใน workspace
- เริ่ม/หยุดแอปพลิเคชันด้วยคลิกเดียว
- ดูบันทึกของแอปพลิเคชันแบบเรียลไทม์
- ตรวจสอบสถานะแอปพลิเคชัน

เพียงคลิกปุ่มเล่นข้างๆ "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: ใช้สคริปต์ shell**

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

สคริปต์ทั้งสองโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ที่โฟลเดอร์รากโดยอัตโนมัติ และจะสร้าง JAR หากยังไม่มีอยู่

> **หมายเหตุ:** ถ้าคุณต้องการสร้างโมดูลทั้งหมดด้วยตนเองก่อนเริ่ม:
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
./stop.sh  # มีเพียงโมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักที่แสดงรูปแบบ prompt engineering ทั้ง 8 พร้อมลักษณะและกรณีการใช้งาน*

## การสำรวจรูปแบบ

อินเทอร์เฟซเว็บช่วยให้คุณทดลองกับกลยุทธ์การ prompt ต่างๆ แต่ละรูปแบบจะแก้ปัญหาที่ต่างกัน — ลองใช้ดูเพื่อดูว่ารูปแบบไหนเหมาะเมื่อใด

### ความกระตือรือร้นต่ำ vs สูง

ถามคำถามง่ายๆ อย่าง "15% ของ 200 คือเท่าไหร่?" ด้วยความกระตือรือร้นต่ำ คุณจะได้รับคำตอบทันทีและตรงไปตรงมา ตอนนี้ถามคำถามซับซ้อนอย่าง "ออกแบบกลยุทธ์การแคชสำหรับ API ที่มีผู้ใช้เยอะ" ด้วยความกระตือรือร้นสูง ดูว่าโมเดลจะช้าลงและให้เหตุผลอย่างละเอียดอย่างไร โมเดลเดียวกัน โครงสร้างคำถามเดียวกัน — แต่ prompt บอกว่าควรคิดมากแค่ไหน

<img src="../../../translated_images/th/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*การคำนวณอย่างรวดเร็วพร้อมเหตุผลน้อยที่สุด*

<img src="../../../translated_images/th/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*กลยุทธ์การแคชอย่างละเอียด (2.8MB)*

### การดำเนินการงาน (คำนำเครื่องมือ)

เวิร์กโฟลว์หลายขั้นตอนได้ประโยชน์จากการวางแผนล่วงหน้าและบรรยายความคืบหน้า โมเดลจะสรุปสิ่งที่จะทำ บรรยายแต่ละขั้นตอน แล้วสรุปผลลัพธ์

<img src="../../../translated_images/th/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*สร้าง REST endpoint พร้อมบรรยายขั้นตอนทีละขั้น (3.9MB)*

### โค้ดที่สะท้อนตนเอง

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะสร้างโค้ดแล้วหยุด โมเดลจะสร้างประเมินตามเกณฑ์คุณภาพ ระบุจุดอ่อน และปรับปรุง คุณจะเห็นมันวนซ้ำจนโค้ดถึงมาตรฐานการผลิต

<img src="../../../translated_images/th/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*บริการตรวจสอบอีเมลครบถ้วน (5.2MB)*

### การวิเคราะห์ที่มีโครงสร้าง

การตรวจสอบโค้ดต้องการกรอบการประเมินที่สม่ำเสมอ โมเดลวิเคราะห์โค้ดโดยใช้ประเภทคงที่ (ความถูกต้อง, แนวทางปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) พร้อมระดับความรุนแรง

<img src="../../../translated_images/th/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*การตรวจสอบโค้ดตามกรอบงาน*

### การสนทนาแบบหลายรอบ

ถาม "Spring Boot คืออะไร?" แล้วตามด้วย "แสดงตัวอย่างให้ดู" ทันที โมเดลจะจำคำถามแรกและให้ตัวอย่าง Spring Boot เฉพาะคุณ หากไม่มีความจำ คำถามที่สองจะมีความกำกวมเกินไป

<img src="../../../translated_images/th/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*การเก็บบริบทข้ามคำถาม*

### การให้เหตุผลทีละขั้นตอน

เลือกปัญหาคณิตศาสตร์และลองใช้ทั้งการให้เหตุผลทีละขั้นตอนและความกระตือรือร้นต่ำ ความกระตือรือร้นต่ำจะตอบแค่คำตอบ — เร็วแต่ไม่โปร่งใส การให้เหตุผลทีละขั้นตอนจะแสดงการคำนวณและการตัดสินใจทุกขั้นตอน

<img src="../../../translated_images/th/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*ปัญหาคณิตศาสตร์พร้อมขั้นตอนที่ชัดเจน*

### การจำกัดผลลัพธ์

เมื่อคุณต้องการรูปแบบหรือจำนวนคำที่เจาะจง รูปแบบนี้บังคับให้ปฏิบัติตามอย่างเคร่งครัด ลองสร้างสรุปที่มีคำครบ 100 คำในรูปแบบหัวข้อย่อย

<img src="../../../translated_images/th/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*สรุปการเรียนรู้เครื่องโดยควบคุมรูปแบบ*

## สิ่งที่คุณกำลังเรียนรู้จริงๆ

**ความพยายามในการให้เหตุผลเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมความพยายามทางคณิตศาสตร์ผ่าน prompt ความพยายามต่ำหมายถึงตอบสนองเร็วด้วยการสำรวจน้อย ความพยายามสูงหมายถึงโมเดลใช้เวลาคิดอย่างลึกซึ้ง คุณเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน — อย่าเสียเวลากับคำถามง่ายๆ แต่ก็อย่ารีบตัดสินใจเรื่องซับซ้อนเกินไป

**โครงสร้างชี้นำพฤติกรรม**

สังเกตแท็ก XML ใน prompt ไหม? มันไม่ใช่แค่ตกแต่ง โมเดลทำตามคำสั่งที่มีโครงสร้างได้แม่นยำกว่าข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือเหตุผลซับซ้อน โครงสร้างช่วยให้โมเดลติดตามตำแหน่งและขั้นตอนถัดไป

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*โครงสร้าง prompt ที่ชัดเจนพร้อมส่วนแบ่งและการจัดระเบียบแบบ XML*

**คุณภาพผ่านการประเมินตนเอง**

รูปแบบสะท้อนตนเองทำงานโดยการระบุเกณฑ์คุณภาพชัดเจน แทนที่จะหวังว่าโมเดล "ทำถูก" คุณบอกมันว่า "ถูก" คืออะไร: ตรรกะถูกต้อง, การจัดการความผิดพลาด, ประสิทธิภาพ, ความปลอดภัย จากนั้นโมเดลสามารถประเมินผลลัพธ์ของตนเองและปรับปรุง นี่เปลี่ยนการสร้างโค้ดจากการเสี่ยงโชคเป็นกระบวนการ

**บริบทมีขอบเขต**

การสนทนาแบบหลายรอบทำงานโดยการรวมประวัติข้อความในแต่ละคำขอ แต่มีขีดจำกัด — ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อบทสนทนายาวขึ้น คุณจะต้องมีกลยุทธ์เพื่อเก็บบริบทที่เกี่ยวข้องโดยไม่เกินขีดจำกัด โมดูลนี้จะแสดงการทำงานของความจำ; ต่อไปคุณจะได้เรียนรู้ว่าเมื่อไรควรสรุป, ลืม และดึงข้อมูล

## ขั้นตอนต่อไป

**โมดูลถัดไป:** [03-rag - RAG (การสร้างข้อมูลเสริมจากการค้นคืน)](../03-rag/README.md)

---

**การนำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่กรุณาทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่มีความสำคัญ แนะนำให้ใช้บริการแปลโดยมนุษย์มืออาชีพ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->