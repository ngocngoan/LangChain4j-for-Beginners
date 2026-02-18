# Module 02: การออกแบบพรอมต์ด้วย GPT-5.2

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [ทำความเข้าใจกับการออกแบบพรอมต์](../../../02-prompt-engineering)
- [พื้นฐานการออกแบบพรอมต์](../../../02-prompt-engineering)
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
  - [การวิเคราะห์ที่มีโครงสร้าง](../../../02-prompt-engineering)
  - [แชทหลายรอบ](../../../02-prompt-engineering)
  - [การให้เหตุผลทีละขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่จำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้จริงๆ](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## สิ่งที่คุณจะได้เรียนรู้

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="สิ่งที่คุณจะได้เรียนรู้" width="800"/>

ในโมดูลก่อนหน้า คุณได้เห็นว่าเมมโมรีช่วยให้ AI สนทนาได้อย่างไร และได้ใช้ GitHub Models สำหรับการโต้ตอบพื้นฐาน ตอนนี้เราจะเน้นที่วิธีการตั้งคำถาม — หรือที่เรียกว่าการออกแบบพรอมต์ — โดยใช้ GPT-5.2 จาก Azure OpenAI วิธีที่คุณจัดโครงสร้างพรอมต์นั้นส่งผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เริ่มต้นด้วยการทวนเทคนิคพื้นฐานของการออกแบบพรอมต์ แล้วจึงเข้าสู่แปดรูปแบบขั้นสูงที่ใช้ประโยชน์เต็มที่จากความสามารถของ GPT-5.2

เราจะใช้ GPT-5.2 เพราะมันมีการควบคุมการให้เหตุผล — คุณสามารถบอกโมเดลได้ว่าจะให้คิดมากน้อยแค่ไหนก่อนตอบ นี่ทำให้กลยุทธ์การออกแบบพรอมต์ต่างๆ ชัดเจนขึ้นและช่วยให้คุณเข้าใจว่าเมื่อไหร่ควรใช้แต่ละวิธี นอกจากนี้ยังได้ประโยชน์จากข้อจำกัดการใช้งานที่น้อยลงของ Azure สำหรับ GPT-5.2 เมื่อเทียบกับ GitHub Models

## ข้อกำหนดเบื้องต้น

- ผ่านการทำโมดูล 01 แล้ว (ทรัพยากร Azure OpenAI ถูกติดตั้งแล้ว)
- มีไฟล์ `.env` ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure (สร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** หากคุณยังไม่ทำโมดูล 01 ให้ปฏิบัติตามคำแนะนำการติดตั้งที่นั่นก่อน

## ทำความเข้าใจกับการออกแบบพรอมต์

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="การออกแบบพรอมต์คืออะไร?" width="800"/>

การออกแบบพรอมต์คือการออกแบบข้อความนำเข้าที่ทำให้ได้ผลลัพธ์ที่คุณต้องการอย่างสม่ำเสมอ ไม่ใช่แค่การตั้งคำถาม แต่เป็นการจัดโครงสร้างคำขอเพื่อให้โมเดลเข้าใจอย่างชัดเจนว่าคุณต้องการอะไร และจะตอบอย่างไร

ลองคิดเหมือนให้คำแนะนำกับเพื่อนร่วมงาน "แก้บั๊ก" นั้นกว้างเกินไป "แก้ข้อผิดพลาด null pointer ใน UserService.java บรรทัดที่ 45 โดยเพิ่มการตรวจสอบ null" นั้นเจาะจง โมเดลภาษาใช้งานแบบเดียวกัน — ความเจาะจงและโครงสร้างมีความสำคัญ

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ทำงานอย่างไร" width="800"/>

LangChain4j ให้โครงสร้างพื้นฐาน — การเชื่อมต่อโมเดล เมมโมรี และประเภทข้อความ — ขณะที่รูปแบบพรอมต์เป็นเพียงข้อความที่จัดโครงสร้างอย่างระมัดระวังส่งผ่านโครงสร้างพื้นฐานนี้ บล็อกหลักคือ `SystemMessage` (ซึ่งตั้งพฤติกรรมและบทบาทของ AI) และ `UserMessage` (ซึ่งเป็นคำขอจริงของคุณ)

## พื้นฐานการออกแบบพรอมต์

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="ภาพรวมรูปแบบการออกแบบพรอมต์ 5 แบบ" width="800"/>

ก่อนที่จะเข้าสู่รูปแบบขั้นสูงในโมดูลนี้ มาทบทวนเทคนิคการตั้งพรอมต์พื้นฐานห้ารูปแบบก่อน นี่คือบล็อกเครื่องมือที่นักออกแบบพรอมต์ทุกคนควรรู้ หากคุณเคยทำผ่าน [โมดูลเริ่มต้นอย่างรวดเร็ว](../00-quick-start/README.md#2-prompt-patterns) มาก่อน คุณจะได้เห็นสิ่งเหล่านี้ในทางปฏิบัติ — นี่คือกรอบแนวคิดเบื้องหลัง

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้คำสั่งตรงไปยังโมเดลโดยไม่มีตัวอย่าง โมเดลพึ่งพาการฝึกฝนเพื่อเข้าใจและดำเนินงานตามคำสั่ง วิธีนี้เหมาะสำหรับคำขอที่ตรงไปตรงมาตรงที่พฤติกรรมที่ต้องการชัดเจน

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรงโดยไม่มีตัวอย่าง — โมเดลเข้าใจงานจากคำสั่งโดยตรง*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// การตอบสนอง: "บวก"
```

**เมื่อไหร่ใช้:** การจัดประเภทง่ายๆ คำถามตรงๆ การแปล หรือภารกิจที่โมเดลสามารถจัดการได้โดยไม่ต้องมีคำแนะนำเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่ต้องการให้โมเดลปฏิบัติตาม โมเดลเรียนรู้รูปแบบอินพุต-เอาต์พุตจากตัวอย่างของคุณและใช้กับอินพุตใหม่ ช่วยเพิ่มความสม่ำเสมอสำหรับงานที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*เรียนรู้จากตัวอย่าง — โมเดลระบุรูปแบบแล้วใช้กับอินพุตใหม่*

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

**เมื่อไหร่ใช้:** การจัดประเภทที่กำหนดเอง ฟอร์แมตที่สม่ำเสมอ งานเฉพาะทาง หรือเมื่อผลลัพธ์แบบ zero-shot ไม่สม่ำเสมอ

### Chain of Thought

ขอให้โมเดลแสดงการให้เหตุผลทีละขั้นตอน แทนที่จะตอบโดยตรง โมเดลจะแบ่งปัญหาและทำงานผ่านแต่ละส่วนอย่างชัดเจน ช่วยเพิ่มความแม่นยำในงานคณิตศาสตร์ ตรรกะ และการให้เหตุผลหลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*การให้เหตุผลทีละขั้นตอน — แบ่งปัญหาซับซ้อนเป็นขั้นตอนตรรกะชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// แบบจำลองแสดงว่า: 15 - 8 = 7 แล้ว 7 + 12 = 19 แอปเปิ้ล
```

**เมื่อไหร่ใช้:** ปัญหาคณิตศาสตร์ ปริศนาตรรกะ การดีบัก หรือภารกิจที่การแสดงเหตุผลช่วยเพิ่มความแม่นยำและความน่าเชื่อถือ

### Role-Based Prompting

กำหนดบุคลิกภาพหรือบทบาทให้ AI ก่อนถามคำถาม สิ่งนี้ให้บริบทที่กำหนดโทนเสียง ความลึก และจุดสนใจของคำตอบ "สถาปนิกซอฟต์แวร์" จะให้คำแนะนำแตกต่างจาก "นักพัฒนาระดับจูเนียร์" หรือ "ผู้ตรวจสอบความปลอดภัย"

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*กำหนดบริบทและบุคลิก — คำถามเดียวกันให้คำตอบต่างกันขึ้นกับบทบาทที่กำหนด*

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

**เมื่อไหร่ใช้:** การตรวจสอบโค้ด การติวเตอร์ การวิเคราะห์เฉพาะทาง หรือเมื่อต้องการคำตอบที่ปรับให้เหมาะกับระดับความเชี่ยวชาญหรือมุมมองที่แตกต่าง

### Prompt Templates

สร้างพรอมต์ที่ใช้ซ้ำได้พร้อมตัวแปรเว้นที่ไว้ แทนที่จะเขียนพรอมต์ใหม่ทุกครั้ง กำหนดเทมเพลตเพียงครั้งเดียวแล้วเติมค่าต่างกัน คลาส `PromptTemplate` ของ LangChain4j ทำให้ง่ายด้วยไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*พรอมต์ใช้ซ้ำได้พร้อมตัวแปรเว้นที่ — เทมเพลตเดียว ใช้งานหลากหลาย*

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

**เมื่อไหร่ใช้:** การสอบถามซ้ำด้วยอินพุตต่างกัน การประมวลผลชุด การสร้างเวิร์กโฟลว์ AI ที่ใช้ซ้ำได้ หรือสถานการณ์ที่โครงสร้างพรอมต์เหมือนเดิมแต่ข้อมูลเปลี่ยน

---

พื้นฐานทั้งห้านี้ให้ชุดเครื่องมือที่มั่นคงสำหรับงานการออกแบบพรอมต์ส่วนใหญ่ ส่วนที่เหลือของโมดูลนี้จะขยายต่อด้วย **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมการให้เหตุผล การประเมินตนเอง และความสามารถส่งออกแบบมีโครงสร้างของ GPT-5.2

## รูปแบบขั้นสูง

เมื่อครอบคลุมพื้นฐานแล้ว มาดูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ปัญหาทั้งหมดไม่จำเป็นต้องใช้วิธีเหมือนกัน บางคำถามต้องการคำตอบเร็ว บางคำถามต้องคิดลึก บางคำถามต้องการเหตุผลที่เห็นได้ชัดเจน บางคำถามต้องการแค่ผลลัพธ์ รูปแบบแต่ละแบบด้านล่างได้รับการปรับแต่งสำหรับสถานการณ์ที่แตกต่าง — และการควบคุมเหตุผลของ GPT-5.2 ทำให้ความแตกต่างเหล่านี้ชัดเจนยิ่งขึ้น

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="แปดรูปแบบการออกแบบพรอมต์" width="800"/>

*ภาพรวมของแปดรูปแบบการออกแบบพรอมต์และกรณีการใช้งาน*

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="การควบคุมการให้เหตุผลกับ GPT-5.2" width="800"/>

*การควบคุมการให้เหตุผลของ GPT-5.2 ช่วยให้คุณระบุได้ว่าโมเดลจะคิดมากน้อยแค่ไหน — ตั้งแต่ตอบตรงไปตรงมาเร็วๆ ถึงการสำรวจอย่างลึกซึ้ง*

**ความกระตือรือร้นต่ำ (รวดเร็ว & มุ่งเน้น)** - สำหรับคำถามง่ายๆ ที่คุณต้องการคำตอบเร็วและตรง โมเดลจะให้เหตุผลน้อยที่สุด - ไม่เกิน 2 ขั้นตอน ใช้สำหรับการคำนวณ การสืบค้น หรือคำถามตรงไปตรงมา

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
> - "รูปแบบความกระตือรือร้นต่ำกับความกระตือรือร้นสูงแตกต่างกันอย่างไร?"
> - "แท็ก XML ในพรอมต์ช่วยจัดโครงสร้างคำตอบของ AI อย่างไร?"
> - "เมื่อไหร่ควรใช้รูปแบบการสะท้อนตนเองกับคำสั่งตรงๆ?"

**ความกระตือรือร้นสูง (ลึกซึ้ง & ละเอียดถี่ถ้วน)** - สำหรับปัญหาซับซ้อนที่ต้องการวิเคราะห์อย่างครบถ้วน โมเดลจะคิดอย่างละเอียดและแสดงรายละเอียดการให้เหตุผล ใช้สำหรับการออกแบบระบบ การตัดสินใจด้านสถาปัตยกรรม หรือการวิจัยที่ซับซ้อน

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินงานตามงาน (ความก้าวหน้าแบบทีละขั้นตอน)** - สำหรับเวิร์กโฟลว์หลายขั้นตอน โมเดลจะให้แผนงานล่วงหน้า เล่าขั้นตอนต่างๆ ขณะทำ แล้วสรุปภาพรวม ใช้สำหรับการย้ายระบบ การนำไปใช้ หรือกระบวนการหลายขั้นตอนใดๆ

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

การใช้ Chain-of-Thought จะขอให้โมเดลแสดงขั้นตอนการให้เหตุผลอย่างชัดเจน ช่วยเพิ่มความแม่นยำสำหรับงานซับซ้อน การแบ่งลงเป็นขั้นตอนทีละส่วนช่วยให้ทั้งมนุษย์และ AI เข้าใจตรรกะ

> **🤖 ทดลองกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับรูปแบบนี้:
> - "ฉันจะปรับรูปแบบการดำเนินงานตามงานสำหรับกระบวนการที่ใช้เวลานานอย่างไร?"
> - "แนวทางปฏิบัติที่ดีที่สุดสำหรับการจัดโครงสร้าง tool preambles ในแอปพลิเคชันจริงคืออะไร?"
> - "ฉันจะจับและแสดงความคืบหน้าระหว่างขั้นตอนใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="รูปแบบการดำเนินงานตามงาน" width="800"/>

*เวิร์กโฟลว์ วางแผน → ดำเนินการ → สรุปสำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตัวเอง** - สำหรับการสร้างโค้ดคุณภาพระดับโปรดักชัน โมเดลจะสร้างโค้ดที่เป็นไปตามมาตรฐานโปรดักชัน พร้อมจัดการข้อผิดพลาดอย่างเหมาะสม ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="วงจรการสะท้อนตนเอง" width="800"/>

*วงจรปรับปรุงแบบวนซ้ำ - สร้าง ประเมิน ระบุปัญหา ปรับปรุง ทำซ้ำ*

**การวิเคราะห์ที่มีโครงสร้าง** - สำหรับการประเมินที่สม่ำเสมอ โมเดลจะตรวจสอบโค้ดด้วยกรอบงานตายตัว (ความถูกต้อง การปฏิบัติงาน ประสิทธิภาพ ความปลอดภัย ความสามารถในการบำรุงรักษา) ใช้สำหรับการตรวจโค้ดหรือประเมินคุณภาพ

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

> **🤖 ทดลองกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับการวิเคราะห์ที่มีโครงสร้าง:
> - "ฉันจะปรับแต่งกรอบงานวิเคราะห์สำหรับการตรวจโค้ดประเภทต่างๆ อย่างไร?"
> - "วิธีที่ดีที่สุดในการแยกวิเคราะห์และประมวลผลผลลัพธ์ที่มีโครงสร้างโปรแกรมมิ่งคืออะไร?"
> - "ฉันจะรับประกันระดับความรุนแรงที่สม่ำเสมอผ่านการตรวจสอบที่ต่างกันได้อย่างไร?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="รูปแบบการวิเคราะห์ที่มีโครงสร้าง" width="800"/>

*กรอบงานสำหรับการตรวจโค้ดสม่ำเสมอพร้อมระดับความรุนแรง*

**แชทหลายรอบ** - สำหรับการสนทนาที่ต้องการบริบท โมเดลจะจดจำข้อความก่อนหน้าและสร้างผลลัพธ์ต่อเนื่อง ใช้สำหรับการช่วยเหลือที่ต้องโต้ตอบหรือ Q&A ที่ซับซ้อน

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/th/context-memory.dff30ad9fa78832a.webp" alt="เมมโมรีบริบท" width="800"/>

*บริบทการสนทนาสะสมผ่านหลายรอบจนถึงขีดจำกัดโทเค็น*

**การให้เหตุผลทีละขั้นตอน** - สำหรับปัญหาที่ต้องการตรรกะที่มองเห็นได้ โมเดลจะแสดงเหตุผลอย่างชัดเจนในแต่ละขั้นตอน ใช้สำหรับปัญหาคณิตศาสตร์ ปริศนาตรรกะ หรือเมื่อต้องการเข้าใจกระบวนการคิด

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="รูปแบบทีละขั้นตอน" width="800"/>

*แยกปัญหาเป็นขั้นตอนตรรกะแจ้งชัด*

**ผลลัพธ์ที่จำกัด** - สำหรับคำตอบที่ต้องเป็นไปตามรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเข้มงวด ใช้สำหรับบทสรุปหรือเมื่อต้องการผลลัพธ์ที่มีโครงสร้างแม่นยำ

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

<img src="../../../translated_images/th/constrained-output-pattern.0ce39a682a6795c2.webp" alt="รูปแบบผลลัพธ์ที่จำกัด" width="800"/>

*บังคับใช้ข้อกำหนดรูปแบบ ความยาว และโครงสร้างเฉพาะ*

## การใช้ทรัพยากร Azure ที่มีอยู่

**ตรวจสอบการติดตั้ง:**

ตรวจสอบว่าไฟล์ `.env` อยู่ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure (สร้างขึ้นในระหว่างโมดูล 01):
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มใช้งานแอปพลิเคชันทั้งหมดด้วย `./start-all.sh` จากโมดูล 01 แล้ว โมดูลนี้จะรันบนพอร์ต 8083 อยู่แล้ว คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่างและไปที่ http://localhost:8083 ได้เลย

**ตัวเลือก 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

คอนเทนเนอร์เดฟมีส่วนขยาย Spring Boot Dashboard ที่ให้หน้าต่างอินเทอร์เฟซสำหรับจัดการแอปพลิเคชัน Spring Boot ทั้งหมด คุณจะพบได้ในแถบกิจกรรมด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดที่มีในเวิร์กสเปซ
- เริ่ม/หยุดแอปด้วยคลิกเดียว
- ดูบันทึกแอปพลิเคชันแบบเรียลไทม์
- ตรวจสอบสถานะแอปพลิเคชัน
เพียงคลิกปุ่มเล่นถัดจาก "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มแอปพลิเคชันเว็บทั้งหมด (โมดูล 01-04):

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

ทั้งสองสคริปต์จะโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ที่โฟลเดอร์รากโดยอัตโนมัติ และจะสร้าง JARs หากยังไม่มี

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
.\stop.ps1  # โมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักแสดงรูปแบบ prompt engineering ทั้ง 8 แบบพร้อมลักษณะและกรณีการใช้งาน*

## สำรวจรูปแบบ

อินเทอร์เฟซเว็บช่วยให้คุณทดลองใช้กลยุทธ์ prompting ต่างๆ รูปแบบแต่ละแบบแก้ปัญหาที่แตกต่างกัน - ลองใช้ดูเพื่อดูว่าแต่ละแนวทางโดดเด่นเมื่อใด

### ความกระตือรือร้นต่ำ vs สูง

ถามคำถามง่ายๆ เช่น "15% ของ 200 คืออะไร?" โดยใช้ความกระตือรือร้นต่ำ คุณจะได้รับคำตอบทันทีและตรงไปตรงมา ตอนนี้ถามคำถามซับซ้อน เช่น "ออกแบบกลยุทธ์การแคชสำหรับ API ที่มีการใช้งานสูง" โดยใช้ความกระตือรือร้นสูง ดูโมเดลชะลอและให้เหตุผลอย่างละเอียด โมเดลเดียวกัน โครงสร้างคำถามเดียวกัน - แต่ prompt บอกมันว่าต้องคิดมากแค่ไหน

<img src="../../../translated_images/th/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*การคำนวณอย่างรวดเร็วด้วยเหตุผลน้อยที่สุด*

<img src="../../../translated_images/th/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*กลยุทธ์แคชอย่างครอบคลุม (2.8MB)*

### การดำเนินงานงาน (Tool Preambles)

เวิร์กโฟลว์หลายขั้นตอนได้ประโยชน์จากการวางแผนล่วงหน้าและการบรรยายความคืบหน้า โมเดลสรุปสิ่งที่จะทำ บรรยายแต่ละขั้นตอน จากนั้นสรุปผลลัพธ์

<img src="../../../translated_images/th/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*การสร้าง endpoint REST พร้อมบรรยายทีละขั้นตอน (3.9MB)*

### โค้ดที่สะท้อนตัวเอง

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะสร้างโค้ดและหยุด โมเดลจะสร้าง ประเมินตามเกณฑ์คุณภาพ ระบุจุดอ่อน และปรับปรุง คุณจะเห็นมันวนซ้ำจนโค้ดตรงตามมาตรฐานการผลิต

<img src="../../../translated_images/th/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*บริการตรวจสอบอีเมลครบถ้วน (5.2MB)*

### การวิเคราะห์แบบมีโครงสร้าง

การตรวจโค้ดต้องการกรอบการประเมินที่สอดคล้องกัน โมเดลวิเคราะห์โค้ดโดยใช้หมวดหมู่คงที่ (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) พร้อมระดับความรุนแรง

<img src="../../../translated_images/th/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*การตรวจโค้ดตามกรอบงาน*

### แชทหลายรอบ

ถามว่า "Spring Boot คืออะไร?" แล้วตามด้วย "แสดงตัวอย่างให้ดู" ทันที โมเดลจดจำคำถามแรกและให้ตัวอย่าง Spring Boot โดยเฉพาะ หากไม่มีหน่วยความจำ คำถามที่สองจะคลุมเครือเกินไป

<img src="../../../translated_images/th/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*การรักษาบริบทข้ามคำถาม*

### เหตุผลทีละขั้นตอน

เลือกโจทย์คณิตศาสตร์และลองทั้งแบบเหตุผลทีละขั้นตอนกับความกระตือรือร้นต่ำ ความกระตือรือร้นต่ำแค่ตอบคำตอบอย่างรวดเร็วแต่ไม่ชัดเจน ทีละขั้นตอนแสดงทุกการคำนวณและการตัดสินใจ

<img src="../../../translated_images/th/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*โจทย์คณิตศาสตร์พร้อมขั้นตอนชัดเจน*

### ข้อจำกัดการส่งออก

เมื่อคุณต้องการรูปแบบเฉพาะหรือจำนวนคำ รูปแบบนี้บังคับให้ปฏิบัติตามอย่างเคร่งครัด ลองสร้างสรุปโดยมีจำนวนคำ 100 คำพอดีในรูปแบบสรุปแบบหัวข้อย่อย

<img src="../../../translated_images/th/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*สรุปการเรียนรู้ของเครื่องพร้อมควบคุมรูปแบบ*

## สิ่งที่คุณกำลังเรียนรู้จริงๆ

**ความพยายามในการให้เหตุผลเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมปริมาณการคำนวณผ่าน prompt ของคุณ ความพยายามต่ำหมายถึงการตอบกลับรวดเร็วพร้อมการสำรวจขั้นต่ำ ความพยายามสูงหมายถึงโมเดลใช้เวลาคิดอย่างลึกซึ้ง คุณกำลังเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน - อย่าเสียเวลาถามคำถามง่ายๆ แต่ก็อย่าเร่งการตัดสินใจที่ซับซ้อนเช่นกัน

**โครงสร้างชี้นำพฤติกรรม**

สังเกตแท็ก XML ใน prompt? พวกมันไม่ใช่แค่การตกแต่ง โมเดลปฏิบัติตามคำสั่งที่มีโครงสร้างได้ไวกว่าแบบข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือตรรกะซับซ้อน โครงสร้างช่วยให้โมเดลติดตามตำแหน่งและสิ่งที่จะทำต่อไป

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*โครงสร้าง prompt ที่ดีพร้อมส่วนชัดเจนและการจัดระเบียบแบบ XML*

**คุณภาพผ่านการประเมินตนเอง**

รูปแบบการสะท้อนตนเองทำงานโดยการทำให้เกณฑ์คุณภาพชัดเจน แทนที่จะหวังว่าโมเดล "ทำถูกต้อง" คุณบอกมันชัดเจนว่า "ถูกต้อง" หมายถึงอะไร: ตรรกะถูกต้อง, การจัดการข้อผิดพลาด, ประสิทธิภาพ, ความปลอดภัย โมเดลจึงประเมินผลของตัวเองและปรับปรุงได้ นี่เปลี่ยนการสร้างโค้ดจากการสุ่มเป็นกระบวนการ

**บริบทมีขอบเขตจำกัด**

การสนทนาหลายรอบทำงานโดยรวมประวัติข้อความกับแต่ละคำขอ แต่มีข้อจำกัด - ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อการสนทนาโตขึ้น คุณจะต้องมีกลยุทธ์เก็บบริบทที่เกี่ยวข้องโดยไม่ถึงขีดจำกัด โมดูลนี้จะแสดงการทำงานของหน่วยความจำ; หลังจากนี้คุณจะเรียนรู้เมื่อสรุป, ลบความทรงจำ และดึงข้อมูล

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**นำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ การแปลโดยผู้เชี่ยวชาญมืออาชีพจึงเป็นสิ่งที่แนะนำ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดใด ๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->