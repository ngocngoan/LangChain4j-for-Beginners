# Module 02: การออกแบบ Prompt กับ GPT-5.2

## สารบัญ

- [วิดีโอสอน](../../../02-prompt-engineering)
- [สิ่งที่คุณจะได้เรียนรู้](../../../02-prompt-engineering)
- [ข้อกำหนดเบื้องต้น](../../../02-prompt-engineering)
- [ความเข้าใจการออกแบบ Prompt](../../../02-prompt-engineering)
- [พื้นฐานการออกแบบ Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [รูปแบบขั้นสูง](../../../02-prompt-engineering)
- [รันแอปพลิเคชัน](../../../02-prompt-engineering)
- [ภาพหน้าจอแอปพลิเคชัน](../../../02-prompt-engineering)
- [สำรวจรูปแบบต่าง ๆ](../../../02-prompt-engineering)
  - [ความกระตือรือร้นต่ำ vs สูง](../../../02-prompt-engineering)
  - [การดำเนินการงาน (คำแนะนำเครื่องมือ)](../../../02-prompt-engineering)
  - [โค้ดที่สะท้อนตนเอง](../../../02-prompt-engineering)
  - [การวิเคราะห์อย่างมีโครงสร้าง](../../../02-prompt-engineering)
  - [แชทหลายรอบ](../../../02-prompt-engineering)
  - [การคิดอย่างเป็นขั้นตอน](../../../02-prompt-engineering)
  - [ผลลัพธ์ที่จำกัด](../../../02-prompt-engineering)
- [สิ่งที่คุณกำลังเรียนรู้อย่างแท้จริง](../../../02-prompt-engineering)
- [ขั้นตอนถัดไป](../../../02-prompt-engineering)

## วิดีโอสอน

ชมเซสชันสดนี้ที่อธิบายวิธีเริ่มต้นกับโมดูลนี้:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## สิ่งที่คุณจะได้เรียนรู้

แผนภาพต่อไปนี้แสดงภาพรวมของหัวข้อหลักและทักษะที่คุณจะพัฒนาในโมดูลนี้ — ตั้งแต่เทคนิครับปรุง prompt จนถึงเวิร์กโฟลว์ขั้นตอนต่อขั้นตอนที่คุณจะปฏิบัติตาม

<img src="../../../translated_images/th/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

ในโมดูลก่อนหน้า คุณได้สำรวจการโต้ตอบพื้นฐานของ LangChain4j กับโมเดล GitHub และเห็นว่าหน่วยความจำช่วยให้ AI สนทนากับ Azure OpenAI ได้อย่างไร ตอนนี้เราจะเน้นวิธีการถามคำถาม — ตัว prompt เอง — โดยใช้ GPT-5.2 ของ Azure OpenAI วิธีที่คุณจัดโครงสร้าง prompt มีผลอย่างมากต่อคุณภาพของคำตอบที่ได้รับ เราเริ่มด้วยการทบทวนเทคนิคการออกแบบ prompt พื้นฐาน จากนั้นไปยังแปดรูปแบบขั้นสูงที่ใช้ประโยชน์เต็มที่จากความสามารถของ GPT-5.2

เราจะใช้ GPT-5.2 เพราะมันเพิ่มการควบคุมเหตุผล — คุณสามารถบอกโมเดลได้ว่าต้องคิดมากแค่ไหนก่อนตอบ นี่ทำให้กลยุทธ์การออกแบบ prompt ต่าง ๆ ชัดเจนขึ้นและช่วยให้คุณเข้าใจว่าเมื่อไรควรใช้วิธีใด ทั้งยังได้รับประโยชน์จากการจำกัดอัตราที่น้อยกว่าของ Azure สำหรับ GPT-5.2 เมื่อเทียบกับโมเดล GitHub

## ข้อกำหนดเบื้องต้น

- ทำโมดูล 01 (ติดตั้งทรัพยากร Azure OpenAI) เสร็จแล้ว
- มีไฟล์ `.env` ในไดเรกทอรีหลักพร้อมข้อมูลรับรอง Azure (สร้างโดย `azd up` ในโมดูล 01)

> **หมายเหตุ:** หากยังไม่ได้ทำโมดูล 01 ให้ทำตามคำแนะนำการติดตั้งที่นั่นก่อน

## ความเข้าใจการออกแบบ Prompt

โดยพื้นฐานแล้ว การออกแบบ prompt คือความแตกต่างระหว่างคำสั่งที่คลุมเครือกับคำสั่งที่ชัดเจน ดังที่การเปรียบเทียบด้านล่างแสดงให้เห็น

<img src="../../../translated_images/th/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

การออกแบบ prompt คือการออกแบบข้อความเข้า (input) ที่ให้ผลลัพธ์ตามที่คุณต้องการอย่างสม่ำเสมอ ไม่ใช่แค่การตั้งคำถามเฉย ๆ แต่เป็นการจัดโครงสร้างคำขอเพื่อให้โมเดลเข้าใจอย่างแม่นยำว่าคุณต้องการอะไรและจะส่งมอบอย่างไร

คิดเหมือนกับการให้คำสั่งเพื่อนร่วมงาน "แก้บั๊ก" นั้นคลุมเครือ แต่ "แก้ข้อผิดพลาด null pointer ในไฟล์ UserService.java บรรทัด 45 โดยเพิ่มการตรวจสอบ null" นั้นเจาะจง โมเดลภาษาใช้งานแบบเดียวกัน — ความชัดเจนและโครงสร้างมีความสำคัญ

แผนภาพต่อไปนี้แสดงว่า LangChain4j เข้ากับภาพนี้อย่างไร — เชื่อมต่อรูปแบบ prompt ของคุณกับโมเดลผ่านบล็อกก่อสร้าง SystemMessage และ UserMessage

<img src="../../../translated_images/th/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j จัดเตรียมโครงสร้างพื้นฐาน — การเชื่อมต่อโมเดล หน่วยความจำ และประเภทข้อความ — ในขณะที่รูปแบบ prompt คือข้อความที่ถูกจัดโครงสร้างอย่างรอบคอบซึ่งคุณส่งผ่านโครงสร้างพื้นฐานนั้น บล็อกก่อสร้างหลักคือ `SystemMessage` (ซึ่งตั้งค่าพฤติกรรมและบทบาทของ AI) และ `UserMessage` (ซึ่งถือคำขอจริงของคุณ)

## พื้นฐานการออกแบบ Prompt

เทคนิคหลักห้าข้อที่แสดงด้านล่างเป็นรากฐานของการออกแบบ prompt อย่างมีประสิทธิภาพ แต่ละข้อแก้ไขด้านต่าง ๆ ของวิธีการสื่อสารกับโมเดลภาษา

<img src="../../../translated_images/th/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ก่อนจะดำดิ่งสู่รูปแบบขั้นสูงในโมดูลนี้ มาทบทวนห้าเทคนิคการออกแบบ prompt พื้นฐานกันก่อน ซึ่งเป็นบล็อกก่อสร้างที่นักออกแบบ prompt ทุกคนควรรู้ หากคุณเคยทำ [โมดูลเริ่มต้นอย่างรวดเร็ว](../00-quick-start/README.md#2-prompt-patterns) มาก่อนแล้ว คุณจะเห็นเทคนิคเหล่านี้ถูกใช้งาน — นี่คือกรอบแนวคิดเบื้องหลัง

### Zero-Shot Prompting

วิธีที่ง่ายที่สุด: ให้โมเดลคำสั่งโดยตรงโดยไม่มีตัวอย่าง โมเดลจะพึ่งพาการฝึกฝนของมันทั้งหมดเพื่อเข้าใจและดำเนินการตามงาน วิธีนี้เหมาะสำหรับคำขอตรงไปตรงมาที่พฤติกรรมที่คาดหวังชัดเจน

<img src="../../../translated_images/th/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*คำสั่งตรงโดยไม่มีตัวอย่าง — โมเดลคาดเดางานจากคำสั่งเท่านั้น*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// การตอบกลับ: "บวก"
```

**เมื่อไรควรใช้:** การจัดประเภทง่าย ๆ คำถามโดยตรง การแปล หรือภารกิจใด ๆ ที่โมเดลสามารถจัดการได้โดยไม่มีคำชี้แนะเพิ่มเติม

### Few-Shot Prompting

ให้ตัวอย่างที่แสดงรูปแบบที่คุณต้องการให้โมเดลปฏิบัติตาม โมเดลเรียนรู้รูปแบบอินพุต - เอาต์พุตที่คาดหวังจากตัวอย่างของคุณและใช้กับอินพุตใหม่ วิธีนี้ช่วยปรับปรุงความสม่ำเสมอสำหรับภารกิจที่รูปแบบหรือพฤติกรรมที่ต้องการไม่ชัดเจน

<img src="../../../translated_images/th/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*เรียนรู้จากตัวอย่าง — โมเดลจดจำรูปแบบและใช้กับอินพุตใหม่*

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

**เมื่อไรควรใช้:** การจัดประเภทที่ปรับแต่งเอง การจัดรูปแบบอย่างสม่ำเสมอ ภารกิจเฉพาะโดเมน หรือเมื่อผลลัพธ์ zero-shot ไม่สม่ำเสมอ

### Chain of Thought

ขอให้โมเดลแสดงเหตุผลอย่างเป็นขั้นตอน แทนที่จะไปสู่คำตอบโดยตรง โมเดลจะแยกปัญหาและแก้ทีละส่วนอย่างชัดเจน วิธีนี้ช่วยปรับปรุงความถูกต้องสำหรับงานคณิตศาสตร์ ตรรกะ และเหตุผลหลายขั้นตอน

<img src="../../../translated_images/th/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*เหตุผลทีละขั้นตอน — การแยกปัญหาซับซ้อนเป็นเหตุผลเชิงตรรกะที่ชัดเจน*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// แบบจำลองแสดง: 15 - 8 = 7 แล้ว 7 + 12 = 19 แอปเปิ้ล
```

**เมื่อไรควรใช้:** ปัญหาคณิตศาสตร์ ปริศนาตรรกะ การดีบัก หรือภารกิจใด ๆ ที่การแสดงกระบวนการเหตุผลช่วยเพิ่มความแม่นยำและความเชื่อถือได้

### Role-Based Prompting

ตั้งตัวตนหรือบทบาทแก่ AI ก่อนถามคำถาม นี่เป็นการให้บริบทที่กำหนดโทนเสียง ความลึก และจุดสนใจของคำตอบ "สถาปนิกซอฟต์แวร์" ให้คำแนะนำต่างจาก "นักพัฒนารุ่นเยาว์" หรือ "ผู้ตรวจสอบความปลอดภัย"

<img src="../../../translated_images/th/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*ตั้งบริบทและบุคลิก — คำถามเดียวกันได้รับคำตอบต่างกันตามบทบาทที่กำหนด*

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

**เมื่อไรควรใช้:** การตรวจสอบโค้ด การสอน การวิเคราะห์เฉพาะโดเมน หรือเมื่อคุณต้องการคำตอบที่ปรับแต่งตามระดับความชำนาญหรือมุมมอง

### Prompt Templates

สร้าง prompt ที่ใช้ซ้ำได้ด้วยตัวแปรเป็นที่ว่าง แทนที่จะเขียน prompt ใหม่ทุกครั้ง ให้กำหนดแม่แบบครั้งเดียวแล้วเติมค่าต่าง ๆ คลาส `PromptTemplate` ของ LangChain4j ช่วยให้ง่ายโดยใช้ไวยากรณ์ `{{variable}}`

<img src="../../../translated_images/th/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompt ใช้ซ้ำได้ด้วยตัวแปร — แม่แบบเดียว ใช้งานได้หลายครั้ง*

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

**เมื่อไรควรใช้:** การสืบค้นซ้ำด้วยอินพุตต่างกัน ประมวลผลเป็นชุด การสร้างเวิร์กโฟลว์ AI ที่ใช้ซ้ำได้ หรือทุกสถานการณ์ที่โครงสร้าง prompt ไม่เปลี่ยนแต่ข้อมูลเปลี่ยน

---

พื้นฐานทั้งห้านี้ให้ชุดเครื่องมือมั่นคงสำหรับภารกิจ prompt ส่วนที่เหลือของโมดูลนี้ต่อยอดโดยใช้ **แปดรูปแบบขั้นสูง** ที่ใช้ประโยชน์จากการควบคุมเหตุผลของ GPT-5.2 การประเมินตนเอง และความสามารถผลลัพธ์มีโครงสร้าง

## รูปแบบขั้นสูง

หลังจากครอบคลุมพื้นฐาน เรามาดูแปดรูปแบบขั้นสูงที่ทำให้โมดูลนี้โดดเด่น ปัญหาไม่จำเป็นต้องใช้วิธีเดียวกันทั้งหมด บางคำถามต้องการคำตอบเร็ว บางคำถามต้องการคิดลึกซึ้ง บางคำถามต้องการเหตุผลที่มองเห็นได้ บางคำถามต้องการแค่ผลลัพธ์ แต่ละรูปแบบด้านล่างปรับแต่งสำหรับสถานการณ์ต่าง ๆ — และการควบคุมเหตุผลของ GPT-5.2 ทำให้ความแตกต่างชัดเจนยิ่งขึ้น

<img src="../../../translated_images/th/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ภาพรวมแปดรูปแบบการออกแบบ prompt และกรณีใช้งาน*

GPT-5.2 เพิ่มมิติอีกอย่างให้รูปแบบเหล่านี้: *การควบคุมเหตุผล* สไลเดอร์ด้านล่างแสดงว่าคุณปรับความพยายามในการคิดของโมเดลได้อย่างไร — ตั้งแต่คำตอบรวดเร็วตรงไปตรงมาจนถึงการวิเคราะห์เชิงลึกละเอียดถี่ถ้วน

<img src="../../../translated_images/th/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*การควบคุมเหตุผลของ GPT-5.2 ช่วยให้คุณระบุระดับการคิดของโมเดลได้ — ตั้งแต่คำตอบที่รวดเร็วตรงไปตรงมาจนถึงการสำรวจอย่างละเอียดลึกซึ้ง*

**ความกระตือรือร้นต่ำ (เร็ว & มุ่งเน้น)** - สำหรับคำถามง่าย ๆ ที่ต้องการคำตอบเร็วและตรง โมเดลทำเหตุผลน้อยที่สุด — สูงสุด 2 ขั้นตอน ใช้กับการคำนวณ การค้นหา หรืองานที่ตรงไปตรงมา

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

> 💡 **สำรวจกับ GitHub Copilot:** เปิด [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) และถามว่า:
> - "ความแตกต่างระหว่างรูปแบบ prompting ความกระตือรือร้นต่ำและสูงคืออะไร?"
> - "แท็ก XML ใน prompt ช่วยจัดโครงสร้างการตอบ AI อย่างไร?"
> - "เมื่อไรควรใช้รูปแบบสะท้อนตนเองและเมื่อไรควรใช้คำสั่งตรง?"

**ความกระตือรือร้นสูง (ลึก & ละเอียด)** - สำหรับปัญหาซับซ้อนที่ต้องการการวิเคราะห์รอบด้าน โมเดลจะวิเคราะห์อย่างละเอียดและแสดงเหตุผลอย่างเจาะลึก ใช้กับการออกแบบระบบ การตัดสินใจสถาปัตยกรรม หรือการวิจัยซับซ้อน

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**การดำเนินการงาน (ความก้าวหน้าทีละขั้นตอน)** - สำหรับเวิร์กโฟลว์หลายขั้นตอน โมเดลจะวางแผนล่วงหน้า บรรยายแต่ละขั้นตอนระหว่างทำงาน จากนั้นสรุปผล ใช้กับการย้ายระบบ การติดตั้ง หรือกระบวนการหลายขั้นตอน

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

การ prompting แบบ Chain-of-Thought ขอให้โมเดลแสดงกระบวนการเหตุผลอย่างชัดเจน ช่วยเพิ่มความถูกต้องสำหรับงานซับซ้อน การแยกเป็นขั้นตอนช่วยให้ทั้งมนุษย์และ AI เข้าใจตรรกะ

> **🤖 ลองกับแชท [GitHub Copilot](https://github.com/features/copilot):** ถามเกี่ยวกับรูปแบบนี้ว่า:
> - "จะปรับรูปแบบการดำเนินการงานสำหรับงานที่ใช้เวลานานอย่างไร?"
> - "แนวปฏิบัติที่ดีที่สุดสำหรับการจัดโครงสร้างคำแนะนำเครื่องมือในแอปพลิเคชันจริงคืออะไร?"
> - "จะจับและแสดงความคืบหน้าช่วงกลางใน UI ได้อย่างไร?"

แผนภาพด้านล่างแสดงเวิร์กโฟลว์แบบ วางแผน → ดำเนินการ → สรุป

<img src="../../../translated_images/th/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*เวิร์กโฟลว์ วางแผน → ดำเนินการ → สรุป สำหรับงานหลายขั้นตอน*

**โค้ดที่สะท้อนตนเอง** - สำหรับสร้างโค้ดคุณภาพสำหรับผลิตภัณฑ์ โมเดลสร้างโค้ดที่เป็นไปตามมาตรฐานการผลิตพร้อมการจัดการข้อผิดพลาดอย่างเหมาะสม ใช้เมื่อสร้างฟีเจอร์หรือบริการใหม่

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

แผนภาพด้านล่างแสดงวงจรปรับปรุงอย่างต่อเนื่อง — สร้าง, ประเมิน, ระบุข้อบกพร่อง, ปรับปรุงจนโค้ดเข้าสู่มาตรฐานการผลิต

<img src="../../../translated_images/th/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*วงจรปรับปรุงอย่างต่อเนื่อง - สร้าง, ประเมิน, ระบุปัญหา, ปรับปรุง, ทำซ้ำ*

**การวิเคราะห์อย่างมีโครงสร้าง** - สำหรับการประเมินอย่างสม่ำเสมอ โมเดลตรวจสอบโค้ดโดยใช้กรอบงานที่กำหนดไว้แล้ว (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย, ความง่ายในการดูแลรักษา) ใช้กับการรีวิวโค้ดหรือการประเมินคุณภาพ

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

> **🤖 ลองกับแชท [GitHub Copilot](https://github.com/features/copilot):** ถามเกี่ยวกับการวิเคราะห์อย่างมีโครงสร้างว่า:
> - "จะปรับแต่งกรอบการวิเคราะห์สำหรับการตรวจสอบโค้ดประเภทต่าง ๆ อย่างไร?"
> - "วิธีที่ดีที่สุดในการวิเคราะห์และดำเนินการกับผลลัพธ์ที่มีโครงสร้างทางโปรแกรมคืออะไร?"
> - "จะทำอย่างไรให้ระดับความรุนแรงคงที่ในแต่ละการตรวจสอบโค้ด?"

แผนภาพต่อไปนี้แสดงกรอบงานที่จัดระเบียบการรีวิวโค้ดในหมวดหมู่ที่สม่ำเสมอพร้อมระดับความรุนแรง

<img src="../../../translated_images/th/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*กรอบงานสำหรับการรีวิวโค้ดที่สม่ำเสมอด้วยระดับความรุนแรง*

**แชทหลายรอบ** - สำหรับบทสนทนาที่ต้องการบริบท โมเดลจดจำข้อความก่อนหน้าและพัฒนาต่อยอดจากนั้น ใช้สำหรับการช่วยเหลือเชิงโต้ตอบหรือ Q&A ที่ซับซ้อน

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

แผนภาพด้านล่างแสดงวิธีสะสมบริบทการสนทนากับแต่ละรอบและความสัมพันธ์กับขีดจำกัดโทเค็นของโมเดล

<img src="../../../translated_images/th/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*วิธีที่บริบทการสนทนาสะสมในหลายรอบจนถึงขีดจำกัดโทเค็น*
**Step-by-Step Reasoning** - สำหรับปัญหาที่ต้องการตรรกะที่ชัดเจน โมเดลจะแสดงเหตุผลอย่างชัดเจนในแต่ละขั้นตอน ใช้แบบนี้สำหรับปัญหาคณิตศาสตร์ ปริศนาตรรกะ หรือเมื่อคุณต้องการเข้าใจกระบวนการคิด

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

ภาพด้านล่างแสดงให้เห็นว่าโมเดลแบ่งปัญหาออกเป็นขั้นตอนตรรกะที่ชัดเจนและมีหมายเลขอย่างไร

<img src="../../../translated_images/th/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="รูปแบบขั้นตอนทีละขั้นตอน" width="800"/>

*การแบ่งปัญหาออกเป็นขั้นตอนตรรกะที่ชัดเจน*

**Constrained Output** - สำหรับการตอบที่มีข้อกำหนดรูปแบบเฉพาะ โมเดลจะปฏิบัติตามกฎรูปแบบและความยาวอย่างเคร่งครัด ใช้สิ่งนี้สำหรับการสรุปหรือเมื่อคุณต้องการโครงสร้างผลลัพธ์ที่แม่นยำ

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

แผนภาพต่อไปนี้แสดงให้เห็นว่านโยบายข้อจำกัดจะช่วยควบคุมโมเดลให้ผลิตผลลัพธ์ที่เป็นไปตามรูปแบบและข้อกำหนดความยาวอย่างเข้มงวดอย่างไร

<img src="../../../translated_images/th/constrained-output-pattern.0ce39a682a6795c2.webp" alt="รูปแบบผลลัพธ์ที่จำกัด" width="800"/>

*การบังคับใช้รูปแบบ ความยาว และข้อกำหนดโครงสร้างเฉพาะ*

## เรียกใช้งานแอปพลิเคชัน

**ตรวจสอบการปรับใช้:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` มีอยู่ในไดเรกทอรีรากพร้อมข้อมูลรับรอง Azure (สร้างไว้ในโมดูล 01) เรียกใช้จากไดเรกทอรีโมดูล (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มต้นแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเคยเริ่มแอปพลิเคชันทั้งหมดโดยใช้ `./start-all.sh` จากไดเรกทอรีราก (ตามที่อธิบายในโมดูล 01) โมดูลนี้จะทำงานอยู่แล้วที่พอร์ต 8083 คุณสามารถข้ามคำสั่งเริ่มต้นด้านล่างและไปที่ http://localhost:8083 ได้เลย

**ทางเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

dev container มีส่วนขยาย Spring Boot Dashboard ที่ให้ส่วนติดต่อแบบภาพในการจัดการแอป Spring Boot ทั้งหมด คุณสามารถหามันได้ใน Activity Bar ด้านซ้ายของ VS Code (มองหาสัญลักษณ์ Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอป Spring Boot ทั้งหมดที่มีใน workspace
- เริ่ม/หยุดแอปด้วยคลิกเดียว
- ดูบันทึกแอปแบบเรียลไทม์
- ตรวจสอบสถานะแอป

เพียงคลิกปุ่มเล่นข้าง "prompt-engineering" เพื่อเริ่มโมดูลนี้ หรือเริ่มทุกโมดูลพร้อมกันได้เลย

<img src="../../../translated_images/th/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard ใน VS Code — เริ่ม หยุด และตรวจสอบโมดูลทั้งหมดจากที่เดียว*

**ทางเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มเว็บแอปทั้งหมด (โมดูล 01-04):

**Bash:**
```bash
cd ..  # จากไดเรกทอรีรูท
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # จากไดเรกทอรีหลัก
.\start-all.ps1
```

หรือเริ่มเฉพาะโมดูลนี้:

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

สคริปต์ทั้งสองจะโหลดตัวแปรแวดล้อมจากไฟล์ `.env` ในโฟลเดอร์รากโดยอัตโนมัติ และจะสร้างไฟล์ JAR ถ้าไฟล์ยังไม่มีอยู่

> **หมายเหตุ:** หากคุณต้องการสร้างทุกโมดูลด้วยตนเองก่อนเริ่ม:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

เปิด http://localhost:8083 ในเบราเซอร์ของคุณ

**การหยุด:**

**Bash:**
```bash
./stop.sh  # โมดูลนี้เท่านั้น
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # โมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## ภาพหน้าจอของแอปพลิเคชัน

นี่คือหน้าตาหลักของโมดูล prompt engineering ที่ให้คุณทดลองกับทั้งแปดรูปแบบเคียงข้างกัน

<img src="../../../translated_images/th/dashboard-home.5444dbda4bc1f79d.webp" alt="หน้าแรกของแดชบอร์ด" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*แดชบอร์ดหลักที่แสดงรูปแบบ prompt engineering ทั้ง 8 พร้อมคุณลักษณะและกรณีการใช้งาน*

## สำรวจรูปแบบเหล่านี้

เว็บอินเทอร์เฟซช่วยให้คุณทดลองกับกลยุทธ์การส่ง prompt ต่าง ๆ แต่ละรูปแบบแก้ปัญหาที่แตกต่างกัน — ลองดูว่าแต่ละแนวทางเหมาะเมื่อใด

> **หมายเหตุ: สตรีมมิ่งกับไม่สตรีมมิ่ง** — ทุกหน้า pattern มีปุ่มสองปุ่ม: **🔴 Stream Response (Live)** และตัวเลือก **Non-streaming** สตรีมมิ่งใช้ Server-Sent Events (SSE) เพื่อแสดงโทเคนแบบเรียลไทม์เมื่อโมเดลกำลังสร้างคำตอบ คุณจะเห็นความคืบหน้าทันที ตัวเลือกไม่สตรีมรอฟังคำตอบทั้งหมดก่อนจึงแสดง สำหรับ prompt ที่ต้องการเหตุผลเชิงลึก (เช่น High Eagerness, Self-Reflecting Code) การเรียกแบบไม่สตรีมอาจใช้เวลานานมาก — บางครั้งเป็นนาที — โดยไม่มีการตอบรับใด ๆ **ใช้แบบสตรีมมิ่งเมื่อทดลองกับ prompt ที่ซับซ้อน** เพื่อที่คุณจะเห็นโมเดลทำงานและหลีกเลี่ยงความรู้สึกว่าคำขอหมดเวลาลง
>
> **หมายเหตุ: ความต้องการเบราเซอร์** — ฟีเจอร์สตรีมมิ่งใช้ Fetch Streams API (`response.body.getReader()`) ซึ่งต้องใช้เบราเซอร์เต็มรูปแบบ (Chrome, Edge, Firefox, Safari) ไม่สามารถทำงานใน Simple Browser ที่ฝังใน VS Code ได้ เนื่องจาก webview ของมันไม่รองรับ ReadableStream API หากใช้ Simple Browser ปุ่มไม่สตรีมยังทำงานตามปกติ — มีผลเฉพาะปุ่มสตรีมเท่านั้น เปิด `http://localhost:8083` ในเบราเซอร์ภายนอกเพื่อประสบการณ์เต็มรูปแบบ

### ความเร่งรีบต่ำและสูง (Low vs High Eagerness)

ถามคำถามง่าย ๆ เช่น "15% ของ 200 คืออะไร?" ด้วยโหมด Low Eagerness คุณจะได้คำตอบทันทีและตรงไปตรงมา ลองถามอะไรซับซ้อนมากขึ้น เช่น "ออกแบบกลยุทธ์แคชสำหรับ API ที่มีการเข้าชมสูง" ด้วย High Eagerness คลิก **🔴 Stream Response (Live)** และดูเหตุผลละเอียดของโมเดลทีละโทเคน โมเดลเดียวกัน โครงสร้างคำถามเหมือนกัน — แต่ prompt บอกว่าให้คิดมากแค่ไหน

### การดำเนินงานตามงาน (Tool Preambles)

เวิร์กโฟลว์หลายขั้นตอนได้ประโยชน์จากการวางแผนล่วงหน้าและบรรยายความคืบหน้า โมเดลสรุปสิ่งที่จะทำ บรรยายแต่ละขั้นตอน แล้วสรุปผลลัพธ์

### โค้ดที่สะท้อนตนเอง (Self-Reflecting Code)

ลอง "สร้างบริการตรวจสอบอีเมล" แทนที่จะสร้างโค้ดและหยุด โมเดลจะสร้าง ประเมินตามเกณฑ์คุณภาพ ระบุจุดอ่อน และปรับปรุง คุณจะเห็นมันทำซ้ำจนโค้ดตรงตามมาตรฐานการผลิต

### การวิเคราะห์ที่มีโครงสร้าง (Structured Analysis)

การตรวจสอบโค้ดต้องมีกรอบการประเมินที่สม่ำเสมอ โมเดลวิเคราะห์โค้ดโดยใช้หมวดหมู่ที่กำหนดไว้ (ความถูกต้อง, แนวปฏิบัติ, ประสิทธิภาพ, ความปลอดภัย) พร้อมระดับความรุนแรง

### การแชทหลายตา (Multi-Turn Chat)

ถาม "Spring Boot คืออะไร?" แล้วตามด้วย "ขอให้แสดงตัวอย่าง" โมเดลจะจดจำคำถามแรกและให้ตัวอย่าง Spring Boot เฉพาะเจาะจง ถ้าไม่มีหน่วยความจำ คำถามที่สองจะกว้างเกินไป

### Step-by-Step Reasoning

เลือกปัญหาคณิตศาสตร์และลองทั้ง Step-by-Step Reasoning และ Low Eagerness โหมด Low Eagerness ให้คำตอบอย่างรวดเร็วแต่ไม่แสดงเหตุผล ส่วนแบบ Step-by-step จะโชว์การคำนวณและการตัดสินใจทุกขั้นตอน

### Constrained Output

เมื่อคุณต้องการรูปแบบเฉพาะหรือจำนวนคำที่แน่นอน รูปแบบนี้จะบังคับให้เป็นไปตามนั้นโดยเคร่งครัด ลองสร้างสรุปที่มี 100 คำตามรูปแบบหัวข้อย่อย

## สิ่งที่คุณกำลังเรียนรู้จริง ๆ

**ความพยายามในการให้เหตุผลเปลี่ยนทุกอย่าง**

GPT-5.2 ให้คุณควบคุมความพยายามในการคำนวณผ่าน prompt ของคุณ ความพยายามต่ำหมายถึงตอบสนองเร็วด้วยการสำรวจน้อย ความพยายามสูงหมายถึงโมเดลใช้เวลาคิดเชิงลึก คุณกำลังเรียนรู้ที่จะจับคู่ความพยายามกับความซับซ้อนของงาน — อย่าเสียเวลาคำถามง่าย ๆ แต่ก็อย่ารีบตัดสินใจในเรื่องซับซ้อน

**โครงสร้างชี้นำพฤติกรรม**

สังเกตแท็ก XML ใน prompt ไหม? มันไม่ใช่แค่ของตกแต่ง โมเดลจะปฏิบัติตามคำสั่งที่มีโครงสร้างได้ไวกว่าแบบข้อความอิสระ เมื่อคุณต้องการกระบวนการหลายขั้นตอนหรือเหตุผลซับซ้อน โครงสร้างช่วยให้โมเดลติดตามตำแหน่งและสิ่งที่จะทำต่อไปได้ แผนภาพด้านล่างแสดงการแบ่ง prompt ที่มีโครงสร้างดี โดยใช้แท็กอย่าง `<system>`, `<instructions>`, `<context>`, `<user-input>`, และ `<constraints>` เพื่อจัดระเบียบคำสั่งของคุณเป็นส่วน ๆ อย่างชัดเจน

<img src="../../../translated_images/th/prompt-structure.a77763d63f4e2f89.webp" alt="โครงสร้างของ prompt" width="800"/>

*โครงสร้างของ prompt ที่จัดอย่างดีพร้อมส่วนต่าง ๆ ชัดเจนและการจัดระเบียบแบบ XML*

**คุณภาพผ่านการประเมินตนเอง**

รูปแบบที่สะท้อนตนเองทำงานโดยระบุเกณฑ์คุณภาพอย่างชัดเจน แทนที่จะหวังว่าโมเดลจะ "ทำถูก" คุณบอกมันเลยว่า "ถูก" หมายถึงอะไร: ตรรกะถูกต้อง, จัดการข้อผิดพลาด, ประสิทธิภาพ, ความปลอดภัย โมเดลจึงสามารถประเมินผลลัพธ์ตัวเองและปรับปรุงได้ นี่เปลี่ยนการสร้างโค้ดจากการเสี่ยงโชคเป็นกระบวนการจริงจัง

**บริบทมีขอบเขต**

การสนทนาหลายตาทำงานโดยเก็บประวัติข้อความทุกคำขอ แต่มีขีดจำกัด — ทุกโมเดลมีจำนวนโทเคนสูงสุด เมื่อบทสนทนาโตขึ้น คุณต้องมีกลยุทธ์เก็บบริบทที่เกี่ยวข้องโดยไม่เกินขีดจำกัด โมดูลนี้สอนวิธีการทำงานของหน่วยความจำ; ต่อไปคุณจะเรียนรู้ว่าเมื่อไรควรสรุป, ลืม หรือเรียกคืนข้อมูล

## ขั้นตอนถัดไป

**โมดูลถัดไป:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**การนำทาง:** [← ก่อนหน้า: โมดูล 01 - บทนำ](../01-introduction/README.md) | [กลับสู่หน้าหลัก](../README.md) | [ถัดไป: โมดูล 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้มีความถูกต้อง แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่แม่นยำ เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่ถูกต้อง สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->