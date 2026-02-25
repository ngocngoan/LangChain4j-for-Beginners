# Module 02: การออกแบบคำสั่ง (Prompt Engineering) กับ GPT-5.2

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [ทำความเข้าใจกับการออกแบบคำสั่ง](../../../02-prompt-engineering)
- [พื้นฐานการออกแบบคำสั่ง](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [รูปแบบขั้นสูง](../../../02-prompt-engineering)
- [การใช้ทรัพยากร Azure ที่มีอยู่](../../../02-prompt-engineering)
- [ภาพหน้าจอของแอปพลิเคชัน](../../../02-prompt-engineering)
- [สำรวจรูปแบบต่าง ๆ](../../../02-prompt-engineering)
  - [ความกระตือรือร้นต่ำ vs สูง](../../../02-prompt-engineering)
  - [การดำเนินงานตามงาน (Tool Preambles)](../../../02-prompt-engineering)
  - [โค้ดที่สะท้อนตนเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์อย่างมีโครงสร้าง](../../../02-prompt-engineering)
  - [แชทหลายรอบ](../../../02-prompt-engineering)
  - [การให้เหตุผลทีละขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่จำกัดรูปแบบ](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้อย่างแท้จริง](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## สิ่งที่คุณจะได้เรียนรู้

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="สิ่งที่คุณจะได้เรียนรู้" width="800"/>

ในโมดูลก่อนหน้านี้ คุณได้เห็นว่าความจำช่วยให้ AI สนทนาทำงานได้อย่างไร และได้ใช้ GitHub Models สำหรับการโต้ตอบพื้นฐาน ตอนนี้เราจะมุ่งเน้นที่วิธีการร้องถามคำถาม — คำสั่ง (prompt) เอง — โดยใช้ GPT-5.2 ของ Azure OpenAI วิธีที่คุณจัดโครงสร้างคำสั่งส่งผลกระทบอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เราจะเริ่มจากการทบทวนเทคนิคพื้นฐานของการสร้างคำสั่ง จากนั้นเข้าสู่แปดรูปแบบขั้นสูงที่ใช้ประโยชน์จากความสามารถของ GPT-5.2 ได้อย่างเต็มที่

เราจะใช้ GPT-5.2 เพราะมันนำเสนอการควบคุมการให้เหตุผล — คุณสามารถบอกโมเดลได้ว่าจะให้มันคิดมากแค่ไหนก่อนตอบคำถาม ซึ่งทำให้กลยุทธ์การสร้างคำสั่งแต่ละแบบชัดเจนขึ้นและช่วยให้คุณเข้าใจว่าเมื่อใดควรใช้แต่ละวิธี นอกจากนี้ เรายังได้รับประโยชน์จากข้อจำกัดอัตราการเรียกใช้งานที่น้อยลงของ Azure สำหรับ GPT-5.2 เมื่อเทียบกับ GitHub Models

## ข้อกำหนดเบื้องต้น

- ทำโมดูล 01 เสร็จสิ้นแล้ว (มีการปรับใช้ทรัพยากร Azure OpenAI)
- ไฟล์ `.env` อยู่ในไดเรกทอรีรากพร้อมข้อมูลประจำตัว Azure (ถูกสร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** หากคุณยังไม่ได้ทำโมดูล 01 กรุณาทำตามคำแนะนำการปรับใช้ในนั้นก่อน

## ทำความเข้าใจกับการออกแบบคำสั่ง

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="การออกแบบคำสั่งคืออะไร?" width="800"/>

การออกแบบคำสั่งคือการออกแบบข้อความป้อนข้อมูลที่จะทำให้คุณได้รับผลลัพธ์ที่ต้องการอย่างสม่ำเสมอ มันไม่ใช่แค่การถามคำถามเท่านั้น — แต่มันคือการจัดโครงสร้างคำขอเพื่อให้โมเดลเข้าใจอย่างชัดเจนว่าคุณต้องการอะไรและจะแสดงผลอย่างไร

คิดว่าเหมือนการให้คำแนะนำแก่เพื่อนร่วมงาน "แก้บั๊ก" มันทั่วไปเกินไป แต่ "แก้ไข Null Pointer Exception ในไฟล์ UserService.java บรรทัดที่ 45 โดยการเพิ่มการตรวจสอบค่าว่าง" นั้นเฉพาะเจาะจง โมเดลภาษาเองก็ทำงานในลักษณะเดียวกัน — ความเฉพาะเจาะจงและโครงสร้างมีความสำคัญ

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ทำงานอย่างไร" width="800"/>

LangChain4j ให้โครงสร้างพื้นฐาน — การเชื่อมต่อกับโมเดล, ความจำ และประเภทของข้อความ — ในขณะที่รูปแบบคำสั่งเป็นเพียงข้อความที่จัดโครงสร้างอย่างระมัดระวังที่คุณส่งผ่านโครงสร้างพื้นฐานนั้น ก้อนสร้างหลักคือ `SystemMessage` (กำหนดพฤติกรรมและบทบาทของ AI) และ `UserMessage` (ถือคำขอของคุณจริงๆ)

## พื้นฐานการออกแบบคำสั่ง

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="ภาพรวมห้ารูปแบบการออกแบบคำสั่ง" width="800"/>

ก่อนจะเจาะลึกไปยังรูปแบบขั้นสูงในโมดูลนี้ ให้เราทบทวนเทคนิคการสร้างคำสั่งพื้นฐานห้ารูปแบบ เหล่านี้คือก้อนสร้างพื้นฐานที่วิศวกรคำสั่งทุกคนควรรู้ หากคุณเคยทำโมดูล [เริ่มต้นอย่างรวดเร็ว (Quick Start)](../00-quick-start/README.md#2-prompt-patterns) คุณจะเห็นสิ่งเหล่านี้ในทางปฏิบัติ — นี่คือกรอบแนวคิดเบื้องหลัง

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้โมเดลคำสั่งโดยตรงโดยไม่มีตัวอย่าง โมเดลจะพึ่งพาการฝึกฝนทั้งหมดในการเข้าใจและดำเนินงานตามงาน วิธีนี้เหมาะกับคำขอที่ตรงไปตรงมาซึ่งพฤติกรรมที่คาดหวังชัดเจน

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรง ๆ โดยไม่มีตัวอย่าง — โมเดลสรุปงานจากคำสั่งเพียงอย่างเดียว*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// การตอบกลับ: "เชิงบวก"
```

**เมื่อใดควรใช้:** การจัดหมวดหมู่ที่ง่าย, คำถามตรงๆ, การแปล หรือภารกิจใด ๆ ที่โมเดลสามารถจัดการได้โดยไม่ต้องมีคำแนะนำเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่คุณต้องการให้โมเดลทำตาม โมเดลจะเรียนรู้รูปแบบป้อนเข้า-ส่งออกที่คาดหวังจากตัวอย่างของคุณและใช้กับป้อนเข้าที่ใหม่ ๆ วิธีนี้ช่วยเพิ่มความสม่ำเสมออย่างมากสำหรับงานที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*การเรียนรู้จากตัวอย่าง — โมเดลระบุรูปแบบและใช้กับข้อมูลใหม่*

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

**เมื่อใดควรใช้:** การจัดหมวดหมู่แบบกำหนดเอง, การจัดรูปแบบที่สม่ำเสมอ, งานเฉพาะโดเมน หรือเมื่อผลลัพธ์ zero-shot ไม่เสถียร

### Chain of Thought

ขอให้โมเดลแสดงเหตุผลของมันทีละขั้นตอน แทนที่จะตอบตรงไปตรงมา โมเดลจะแยกปัญหาและวิเคราะห์แต่ละส่วนอย่างชัดเจน วิธีนี้ช่วยเพิ่มความถูกต้องสำหรับงานคณิตศาสตร์, ตรรกะ และการให้เหตุผลขั้นตอนหลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*เหตุผลทีละขั้นตอน — แยกปัญหาซับซ้อนออกเป็นขั้นตอนตรรกะชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// แบบจำลองแสดง: 15 - 8 = 7 จากนั้น 7 + 12 = 19 แอปเปิ้ล
```

**เมื่อใดควรใช้:** ปัญหาคณิตศาสตร์, ปริศนาตรรกะ, แก้ไขบั๊ก หรือทุกงานที่การแสดงกระบวนการคิดช่วยเพิ่มความถูกต้องและความน่าเชื่อถือ

### Role-Based Prompting

ตั้งบุคลิกหรือบทบาทสำหรับ AI ก่อนที่จะถามคำถาม วิธีนี้ช่วยกำหนดบริบทที่ส่งผลต่อโทนเสียง, ความลึก และจุดสนใจของคำตอบ “สถาปนิกซอฟต์แวร์” ให้คำแนะนำที่แตกต่างจาก “นักพัฒนาระดับจูเนียร์” หรือ “ผู้ตรวจสอบความปลอดภัย”

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*การตั้งบริบทและบุคลิก — คำถามเดียวกันได้คำตอบที่ต่างกันขึ้นอยู่กับบทบาทที่กำหนด*

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

**เมื่อใดควรใช้:** การทบทวนโค้ด, การติวเตอร์, การวิเคราะห์เฉพาะโดเมน หรือเมื่อคุณต้องการคำตอบที่เหมาะสมกับระดับความเชี่ยวชาญหรือมุมมองเฉพาะ

### Prompt Templates

สร้างคำสั่งที่นำกลับมาใช้ใหม่ได้โดยมีที่ว่างสำหรับตัวแปร แทนการเขียนคำสั่งใหม่ทุกครั้ง กำหนดเทมเพลตครั้งเดียวแล้วเติมค่าที่แตกต่างกัน คลาส `PromptTemplate` ของ LangChain4j ทำให้ง่ายด้วยไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*คำสั่งที่นำกลับมาใช้ใหม่ได้พร้อมช่องตัวแปร — เทมเพลตเดียวใช้ได้หลายครั้ง*

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

**เมื่อใดควรใช้:** คำถามซ้ำด้วยอินพุตที่แตกต่างกัน, การประมวลผลเป็นชุด, การสร้างเวิร์กโฟลว์ AI ที่นำกลับมาใช้ได้ หรือทุกกรณีที่โครงสร้างคำสั่งเหมือนเดิมแต่ข้อมูลเปลี่ยน

---

พื้นฐานทั้งห้านี้ให้คุณมีชุดเครื่องมือที่มั่นคงสำหรับภารกิจคำสั่งส่วนใหญ่ ส่วนที่เหลือของโมดูลนี้จะต่อยอดด้วย **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมการให้เหตุผลของ GPT-5.2, การประเมินตนเอง, และความสามารถในการสร้างผลลัพธ์ที่มีโครงสร้าง

## รูปแบบขั้นสูง

เมื่อเข้าใจพื้นฐานแล้ว มาดูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ไม่ใช่ทุกปัญหาจะต้องใช้วิธีเดียวกัน บางคำถามต้องการคำตอบฉับไว บางคำถามต้องการการคิดอย่างลึกซึ้ง บางคำถามต้องการให้เห็นกระบวนการคิด และบางคำถามต้องการแค่ผลลัพธ์ รูปแบบแต่ละแบบด้านล่างถูกปรับแต่งให้เหมาะกับสถานการณ์ที่ต่างกัน — และการควบคุมการให้เหตุผลของ GPT-5.2 ทำให้ความแตกต่างยิ่งเห็นชัดขึ้น

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="แปดรูปแบบการออกแบบคำสั่ง" width="800"/>

*ภาพรวมของแปดรูปแบบวิศวกรรมคำสั่งและกรณีใช้งาน*

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="การควบคุมเหตุผลกับ GPT-5.2" width="800"/>

*การควบคุมการให้เหตุผลของ GPT-5.2 ให้คุณระบุได้ว่าโมเดลควรคิดเท่าไร — ตั้งแต่คำตอบรวดเร็วตรงไปตรงมาจนถึงการสำรวจอย่างลึกซึ้ง*

**ความกระตือรือร้นต่ำ (รวดเร็วและมีเป้าหมาย)** - สำหรับคำถามง่าย ๆ ที่คุณต้องการคำตอบเร็วและตรง โมเดลทำการคิดน้อยที่สุด — สูงสุด 2 ขั้นตอน ใช้สำหรับการคำนวณ, การค้นหาข้อมูล หรือคำถามตรงๆ

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

> 💡 **สำรวจด้วย GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) แล้วถาม:
> - "ความแตกต่างระหว่างรูปแบบความกระตือรือร้นต่ำกับสูงในการออกแบบคำสั่งคืออะไร?"
> - "แท็ก XML ในคำสั่งช่วยโครงสร้างคำตอบของ AI อย่างไร?"
> - "เมื่อไหร่ควรใช้รูปแบบการสะท้อนตนเองกับคำสั่งตรง ๆ?"

**ความกระตือรือร้นสูง (ลึกซึ้งและละเอียดถี่ถ้วน)** - สำหรับปัญหาซับซ้อนที่คุณต้องการการวิเคราะห์อย่างครอบคลุม โมเดลจะสำรวจอย่างละเอียดและแสดงเหตุผลอย่างละเอียด ใช้กับการออกแบบระบบ, การตัดสินใจสถาปัตยกรรม หรือการวิจัยที่ซับซ้อน

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินงานตามงาน (ความก้าวหน้าทีละขั้นตอน)** - สำหรับเวิร์กโฟลว์หลายขั้นตอน โมเดลจะให้แผนล่วงหน้าก่อน, บรรยายแต่ละขั้นตอนขณะทำงาน จากนั้นสรุปผล ใช้ในงานย้ายข้อมูล, การติดตั้งใช้งาน หรือกระบวนการหลายขั้นตอนใดๆ

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

การใช้คำสั่งแบบ Chain-of-Thought ช่วยขอให้โมเดลแสดงกระบวนการคิด ช่วยเพิ่มความถูกต้องสำหรับงานที่ซับซ้อน การแยกขั้นตอนช่วยทั้งมนุษย์และ AI เข้าใจกระบวนการตรรกะ

> **🤖 ลองกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับรูปแบบนี้:
> - "ฉันจะปรับรูปแบบการดำเนินงานตามงานสำหรับกระบวนการที่ใช้เวลานานอย่างไร?"
> - "แนวทางที่ดีที่สุดสำหรับจัดโครงสร้าง tool preambles ในแอปพลิเคชันจริงคืออะไร?"
> - "ฉันจะจับและแสดงความคืบหน้าระหว่างทางใน UI ได้อย่างไร?"

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="รูปแบบการดำเนินงานตามงาน" width="800"/>

*แผน → ดำเนินการ → สรุป สำหรับเวิร์กโฟลว์หลายขั้นตอน*

**โค้ดที่สะท้อนตนเอง** - สำหรับสร้างโค้ดคุณภาพระดับการผลิต โมเดลสร้างโค้ดพร้อมมาตรฐานการผลิตและจัดการข้อผิดพลาดอย่างเหมาะสม ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="วงจรการสะท้อนตนเอง" width="800"/>

*แก้ไขปรับปรุงแบบวนลูป - สร้าง, ประเมิน, หาจุดบกพร่อง, ปรับปรุง, ทำซ้ำ*

**การวิเคราะห์อย่างมีโครงสร้าง** - สำหรับการประเมินอย่างสม่ำเสมอ โมเดลรีวิวโค้ดโดยใช้กรอบงานที่ตั้งไว้แน่นอน (ความถูกต้อง, แนวทางปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย, การบำรุงรักษา) ใช้สำหรับรีวิวโค้ดหรือการประเมินคุณภาพ

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

> **🤖 ลองกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** ถามเกี่ยวกับการวิเคราะห์อย่างมีโครงสร้าง:
> - "ฉันจะปรับแต่งกรอบการวิเคราะห์สำหรับการรีวิวโค้ดประเภทต่าง ๆ ได้อย่างไร?"
> - "วิธีที่ดีที่สุดในการแยกวิเคราะห์และปฏิบัติตามผลลัพธ์ที่มีโครงสร้างในเชิงโปรแกรมคืออะไร?"
> - "ฉันจะรับประกันระดับความรุนแรงที่สม่ำเสมอในแต่ละรอบรีวิวอย่างไร?"

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="รูปแบบการวิเคราะห์อย่างมีโครงสร้าง" width="800"/>

*กรอบงานสำหรับการรีวิวโค้ดอย่างสม่ำเสมอพร้อมระดับความรุนแรง*

**แชทหลายรอบ** - สำหรับการสนทนาที่ต้องมีบริบท โมเดลจดจำข้อความก่อนหน้าและต่อยอดจากนั้น ใช้สำหรับเซสชันช่วยเหลือแบบโต้ตอบหรือถามตอบที่ซับซ้อน

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/th/context-memory.dff30ad9fa78832a.webp" alt="ความจำบริบท" width="800"/>

*บริบทการสนทนาสะสมในหลายรอบจนถึงขีดจำกัดโทเค็น*

**การให้เหตุผลทีละขั้นตอน** - สำหรับปัญหาที่ต้องการตรรกะที่มองเห็นได้ โมเดลแสดงเหตุผลอย่างชัดเจนสำหรับแต่ละขั้นตอน ใช้กับปัญหาคณิตศาสตร์, ปริศนาตรรกะ หรือเมื่อคุณต้องการเข้าใจกระบวนการคิด

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

*แยกปัญหาออกเป็นขั้นตอนตรรกะที่ชัดเจน*

**ผลลัพธ์ที่จำกัดรูปแบบ** - สำหรับคำตอบที่มีข้อกำหนดรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเคร่งครัด ใช้สำหรับบทสรุปหรือเมื่อต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

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

*บังคับใช้รูปแบบ, ความยาว, และโครงสร้างที่เฉพาะเจาะจง*

## การใช้ทรัพยากร Azure ที่มีอยู่

**ตรวจสอบการปรับใช้:**

ตรวจสอบว่าไฟล์ `.env` มีอยู่ในไดเรกทอรีรากพร้อมข้อมูลประจำตัว Azure (สร้างขึ้นในโมดูล 01):
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณได้เริ่มแอปพลิเคชันทั้งหมดแล้วโดยใช้ `./start-all.sh` จากโมดูล 01 โมดูลนี้จะทำงานอยู่บนพอร์ต 8083 คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่างและไปที่ http://localhost:8083 ได้เลย

**ตัวเลือก 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

คอนเทนเนอร์สำหรับนักพัฒนารวมส่วนขยาย Spring Boot Dashboard ซึ่งเป็นอินเทอร์เฟซแบบภาพสำหรับจัดการแอปพลิเคชัน Spring Boot ทั้งหมด คุณสามารถหาได้ในแถบกิจกรรมทางซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอปพลิเคชัน Spring Boot ทั้งหมดในพื้นที่ทำงาน
- เริ่ม/หยุดแอปพลิเคชันด้วยการคลิกเพียงครั้งเดียว
- ดูบันทึกแอปพลิเคชันแบบเรียลไทม์
- ตรวจสอบสถานะของแอปพลิเคชัน
เพียงคลิกปุ่มเล่นถัดจาก "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

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

หรือต้องการเริ่มเพียงโมดูลนี้:

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

สคริปต์ทั้งสองจะโหลดตัวแปรสิ่งแวดล้อมจากไฟล์ `.env` ที่โฟลเดอร์หลักโดยอัตโนมัติ และจะสร้างไฟล์ JAR หากยังไม่มี

> **หมายเหตุ:** หากคุณต้องการสร้างทุกโมดูลด้วยตนเองก่อนเริ่ม:
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

เปิด http://localhost:8083 ในเว็บเบราว์เซอร์ของคุณ

**วิธีหยุด:**

**Bash:**
```bash
./stop.sh  # โมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # เฉพาะโมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอของแอปพลิเคชัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักแสดงรูปแบบ prompt engineering ทั้ง 8 แบบพร้อมลักษณะและกรณีการใช้งาน*

## สำรวจรูปแบบต่าง ๆ

อินเทอร์เฟซเว็บช่วยให้คุณทดลองใช้กลยุทธ์การ prompting ต่าง ๆ แต่ละรูปแบบแก้ปัญหาที่แตกต่างกัน ลองใช้ดูว่าควรใช้วิธีใดในสถานการณ์ไหน

> **หมายเหตุ: การสตรีมกับไม่สตรีม** — ในแต่ละหน้ารูปแบบจะมีปุ่มสองปุ่ม: **🔴 Stream Response (Live)** และตัวเลือก **Non-streaming** การสตรีมใช้ Server-Sent Events (SSE) เพื่อแสดงโทเค็นแบบเรียลไทม์ขณะที่โมเดลผลิตคำตอบ จึงเห็นความคืบหน้าได้ทันที ส่วนตัวเลือกไม่สตรีมจะรอจนได้รับคำตอบทั้งหมดก่อนจึงแสดงผล สำหรับ prompt ที่ต้องการตรึกตรองลึกซึ้ง (เช่น High Eagerness, Self-Reflecting Code) การเรียกใช้แบบไม่สตรีมอาจใช้เวลานานมาก — บางครั้งนานเป็นนาที — โดยไม่มีฟีดแบ็กใด ๆ **ใช้การสตรีมเมื่อทดลอง prompt ที่ซับซ้อน** เพื่อให้เห็นโมเดลทำงานและหลีกเลี่ยงความเข้าใจผิดว่าเซสชันหมดเวลาแล้ว
>
> **หมายเหตุ: ความต้องการเบราว์เซอร์** — ฟีเจอร์สตรีมใช้ Fetch Streams API (`response.body.getReader()`) ซึ่งต้องใช้เบราว์เซอร์เต็มรูปแบบ (Chrome, Edge, Firefox, Safari) และ **ไม่** ใช้งานได้ใน Simple Browser ของ VS Code เนื่องจาก webview ไม่รองรับ ReadableStream API หากใช้ Simple Browser ปุ่มแบบไม่สตรีมยังใช้งานได้ตามปกติ — มีผลกระทบเฉพาะปุ่มสตรีมเท่านั้น เปิด `http://localhost:8083` บนเบราว์เซอร์ภายนอกเพื่อประสบการณ์เต็มรูปแบบ

### ความมุ่งมั่นต่ำกับสูง

ถามคำถามง่าย ๆ เช่น "15% ของ 200 คืออะไร?" โดยใช้ Low Eagerness คุณจะได้รับคำตอบแบบทันทีและตรงไปตรงมา แต่ถามคำถามซับซ้อน เช่น "ออกแบบกลยุทธ์การแคชสำหรับ API ที่มีการเข้าชมสูง" โดยใช้ High Eagerness คลิก **🔴 Stream Response (Live)** และดูรายละเอียดตรรกะของโมเดลปรากฏขึ้นทีละโทเค็น โมเดลเดียวกัน โครงสร้างคำถามเดียวกัน — แต่ prompt บอกว่าคิดมากแค่ไหน

### การดำเนินงานตามงาน (Tool Preambles)

เวิร์กโฟลว์หลายขั้นตอนได้รับประโยชน์จากการวางแผนล่วงหน้าและบรรยายความคืบหน้า โมเดลจะสรุปว่าจะทำอะไร บรรยายแต่ละขั้นตอน และสรุปผลลัพธ์

### โค้ดที่สะท้อนตนเอง

ลอง “สร้างบริการตรวจสอบอีเมล” แทนที่จะสร้างโค้ดแล้วหยุด โมเดลจะสร้างโค้ด ประเมินตามเกณฑ์คุณภาพ หาแนวโน้มอ่อนแอ และปรับปรุง คุณจะเห็นมันทำซ้ำจนโค้ดพร้อมใช้ในโปรดักชัน

### การวิเคราะห์เชิงโครงสร้าง

การตรวจสอบโค้ดต้องใช้กรอบการประเมินที่สม่ำเสมอ โมเดลวิเคราะห์โค้ดโดยใช้หมวดหมู่คงที่ (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) พร้อมระดับความรุนแรง

### แชทหลายรอบ

ถาม “Spring Boot คืออะไร?” แล้วตามด้วย “แสดงตัวอย่างให้ดู” โมเดลจำคำถามแรกและให้ตัวอย่าง Spring Boot เฉพาะคุณ ถ้าไม่มีหน่วยความจำ คำถามที่สองจะกว้างเกินไป

### การวิเคราะห์ขั้นตอนทีละขั้น

เลือกโจทย์คณิตศาสตร์และลองทั้ง Step-by-Step Reasoning กับ Low Eagerness Low eagerness ให้คำตอบเร็วแต่ไม่แสดงวิธีคิด ส่วน step-by-step แสดงทุกการคำนวณและการตัดสินใจ

### การบีบอัดเอาต์พุต

เมื่อคุณต้องการรูปแบบหรือจำนวนคำเฉพาะ รูปแบบนี้จะบังคับให้ปฏิบัติตามอย่างเคร่งครัด ลองสร้างสรุปที่มีคำอย่างแม่นยำ 100 คำในรูปแบบหัวข้อย่อย

## สิ่งที่คุณกำลังเรียนรู้จริง ๆ

**ความพยายามในการตรึกตรองเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมความพยายามในการคำนวณผ่าน prompt ของคุณ ความพยายามต่ำแปลว่าตอบเร็วและสำรวจน้อย ความพยายามสูงแปลว่าโมเดลใช้เวลาในการคิดอย่างลึกซึ้ง คุณกำลังเรียนรู้ที่จะตรงความพยายามกับความซับซ้อนของงาน — อย่าเสียเวลาคำถามง่าย ๆ แต่ก็อย่าเร่งรีบการตัดสินใจซับซ้อน

**โครงสร้างช่วยกำกับพฤติกรรม**

สังเกตแท็ก XML ใน prompt ใช่ไหม? มันไม่ใช่ของตกแต่ง โมเดลปฏิบัติตามคำสั่งที่มีโครงสร้างได้แม่นยำกว่าข้อความเสรี เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือเหตุผลซับซ้อน โครงสร้างช่วยให้โมเดลติดตามได้ว่ากำลังอยู่ตรงไหนและขั้นตอนถัดไปคืออะไร

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ส่วนประกอบของ prompt ที่มีโครงสร้างดีพร้อมส่วนต่าง ๆ ที่ชัดเจนและจัดระเบียบแบบ XML*

**คุณภาพด้วยการประเมินตนเอง**

รูปแบบสะท้อนตนเองทำงานด้วยการกำหนดเกณฑ์คุณภาพอย่างชัดเจน แทนที่จะหวังว่าโมเดล “ทำถูกต้อง” คุณบอกมันชัดเจนว่า “ถูกต้อง” หมายถึงอะไร: ตรรกะถูกต้อง, การจัดการข้อผิดพลาด, ประสิทธิภาพ, ความปลอดภัย โมเดลจึงสามารถประเมินเอาต์พุตของตนเองและปรับปรุงได้ นี่เปลี่ยนการสร้างโค้ดจากการลุ้นเป็นกระบวนการ

**บริบทมีขีดจำกัด**

บทสนทนาหลายรอบทำงานโดยรวมประวัติข้อความในแต่ละคำขอ แต่มีขีดจำกัด — ทุกโมเดลมีจำนวนโทเค็นสูงสุด เมื่อบทสนทนาโตขึ้น คุณต้องมีกลยุทธ์เก็บบริบทสำคัญโดยไม่เกินขีดจำกัด โมดูลนี้สอนวิธีทำงานกับหน่วยความจำ; ต่อไปคุณจะเรียนรู้ว่าเมื่อใดควรสรุป เมื่อใดควรลืม และเมื่อใดควรเรียกคืน

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**การนำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถือเป็นแหล่งข้อมูลที่ถูกต้องและน่าเชื่อถือ สำหรับข้อมูลสำคัญ แนะนำให้ใช้บริการแปลโดยมืออาชีพที่เป็นมนุษย์ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->