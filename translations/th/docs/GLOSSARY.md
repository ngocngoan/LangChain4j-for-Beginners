# LangChain4j คำศัพท์

## สารบัญ

- [แนวคิดหลัก](../../../docs)
- [ส่วนประกอบของ LangChain4j](../../../docs)
- [แนวคิด AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [การออกแบบ Prompt](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [เอเจนต์และเครื่องมือ](../../../docs)
- [โมดูล Agentic](../../../docs)
- [โปรโตคอลบริบทโมเดล (MCP)](../../../docs)
- [บริการ Azure](../../../docs)
- [การทดสอบและพัฒนา](../../../docs)

อ้างอิงอย่างรวดเร็วสำหรับคำศัพท์และแนวคิดที่ใช้ตลอดหลักสูตร

## แนวคิดหลัก

**AI Agent** - ระบบที่ใช้ AI ในการทำเหตุผลและทำงานอย่างอิสระ [Module 04](../04-tools/README.md)

**Chain** - ลำดับของการดำเนินการที่ผลลัพธ์ถูกป้อนเข้าสู่ขั้นตอนถัดไป

**Chunking** - การแบ่งเอกสารเป็นชิ้นเล็ก ๆ โดยทั่วไป: 300-500 โทเค็นพร้อมทับซ้อนกัน [Module 03](../03-rag/README.md)

**Context Window** - จำนวนโทเค็นสูงสุดที่โมเดลสามารถประมวลผลได้ GPT-5.2: 400K โทเค็น

**Embeddings** - เวกเตอร์ตัวเลขที่แสดงความหมายของข้อความ [Module 03](../03-rag/README.md)

**Function Calling** - โมเดลสร้างคำขอแบบมีโครงสร้างเพื่เรียกใช้ฟังก์ชันภายนอก [Module 04](../04-tools/README.md)

**Hallucination** - เมื่อโมเดลสร้างข้อมูลที่ผิดแต่ดูเหมือนเป็นไปได้

**Prompt** - ข้อความเข้าไปยังโมเดลภาษา [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - การค้นหาโดยใช้ความหมายผ่าน embeddings แทนการใช้คำหลัก [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ไม่มีหน่วยความจำ Stateful: รักษาประวัติการสนทนา [Module 01](../01-introduction/README.md)

**Tokens** - หน่วยข้อความพื้นฐานที่โมเดลประมวลผล มีผลต่อค่าใช้จ่ายและขีดจำกัด [Module 01](../01-introduction/README.md)

**Tool Chaining** - การใช้เครื่องมือเป็นลำดับ โดยผลลัพธ์ชี้นำการเรียกครั้งต่อไป [Module 04](../04-tools/README.md)

## ส่วนประกอบของ LangChain4j

**AiServices** - สร้างอินเทอร์เฟซบริการ AI แบบประเภทปลอดภัย

**OpenAiOfficialChatModel** - ไคลเอ็นต์แบบรวมสำหรับโมเดล OpenAI และ Azure OpenAI

**OpenAiOfficialEmbeddingModel** - สร้าง embeddings โดยใช้ไคลเอ็นต์ OpenAI Official (รองรับทั้ง OpenAI และ Azure OpenAI)

**ChatModel** - อินเทอร์เฟซหลักสำหรับโมเดลภาษา

**ChatMemory** - รักษาประวัติการสนทนา

**ContentRetriever** - ค้นหาชิ้นส่วนเอกสารที่เกี่ยวข้องสำหรับ RAG

**DocumentSplitter** - แบ่งเอกสารเป็นชิ้น

**EmbeddingModel** - แปลงข้อความเป็นเวกเตอร์ตัวเลข

**EmbeddingStore** - จัดเก็บและดึง embeddings

**MessageWindowChatMemory** - รักษาหน้าต่างเลื่อนของข้อความล่าสุด

**PromptTemplate** - สร้าง prompt ที่ใช้ซ้ำได้พร้อมตัวแปร {{variable}}

**TextSegment** - ชิ้นข้อความพร้อมข้อมูลเมตา ใช้ใน RAG

**ToolExecutionRequest** - แทนคำขอการใช้งานเครื่องมือ

**UserMessage / AiMessage / SystemMessage** - ประเภทข้อความในการสนทนา

## แนวคิด AI/ML

**Few-Shot Learning** - การให้ตัวอย่างใน prompt [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - โมเดล AI ที่ฝึกบนข้อมูลข้อความจำนวนมาก

**Reasoning Effort** - พารามิเตอร์ GPT-5.2 ควบคุมความลึกของการคิด [Module 02](../02-prompt-engineering/README.md)

**Temperature** - ควบคุมความสุ่มของผลลัพธ์ ต่ำ=แน่นอน สูง=สร้างสรรค์

**Vector Database** - ฐานข้อมูลเฉพาะสำหรับ embeddings [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - การทำงานโดยไม่มีตัวอย่าง [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - แนวทางรักษาความปลอดภัยหลายชั้น ผสม guardrails ในแอปกับฟิลเตอร์ความปลอดภัยของผู้ให้บริการ

**Hard Block** - ผู้ให้บริการเกิด HTTP 400 ข้อผิดพลาดสำหรับเนื้อหาที่ละเมิดรุนแรง

**InputGuardrail** - อินเทอร์เฟซ LangChain4j สำหรับตรวจสอบอินพุตผู้ใช้ก่อนถึง LLM ประหยัดค่าใช้จ่ายและเวลาตอบสนองด้วยการบล็อก prompt ที่เป็นอันตรายตั้งแต่ต้น

**InputGuardrailResult** - ประเภทผลลัพธ์การตรวจสอบ guardrail: `success()` หรือ `fatal("reason")`

**OutputGuardrail** - อินเทอร์เฟซตรวจสอบคำตอบ AI ก่อนส่งกลับผู้ใช้

**Provider Safety Filters** - ฟิลเตอร์เนื้อหาที่มาพร้อมกับผู้ให้บริการ AI (เช่น GitHub Models) ที่ตรวจจับการละเมิดระดับ API

**Soft Refusal** - โมเดลปฏิเสธตอบอย่างสุภาพโดยไม่เกิดข้อผิดพลาด

## การออกแบบ Prompt - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - การใช้เหตุผลทีละขั้นตอนเพื่อความแม่นยำที่ดีกว่า

**Constrained Output** - บังคับรูปแบบหรือโครงสร้างเฉพาะ

**High Eagerness** - รูปแบบ GPT-5.2 สำหรับเหตุผลเชิงลึก

**Low Eagerness** - รูปแบบ GPT-5.2 สำหรับคำตอบรวดเร็ว

**Multi-Turn Conversation** - รักษาบริบทช่วงหลายรอบสนทนา

**Role-Based Prompting** - กำหนดบุคลิกโมเดลผ่านข้อความระบบ

**Self-Reflection** - โมเดลประเมินและปรับปรุงผลลัพธ์ของตนเอง

**Structured Analysis** - กรอบการประเมินที่กำหนดไว้แน่นอน

**Task Execution Pattern** - วางแผน → ดำเนินการ → สรุป

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - โหลด → แบ่ง → สร้าง embedding → เก็บ

**In-Memory Embedding Store** - ที่จัดเก็บแบบไม่ถาวรสำหรับการทดสอบ

**RAG** - ผสมผสานการดึงข้อมูลกับการสร้างข้อความเพื่อตอบให้มีพื้นฐาน

**Similarity Score** - การวัดความเหมือนเชิงความหมาย (0-1)

**Source Reference** - ข้อมูลเมตาเกี่ยวกับเนื้อหาที่ดึงมา

## เอเจนต์และเครื่องมือ - [Module 04](../04-tools/README.md)

**@Tool Annotation** - ทำเครื่องหมายเมธอด Java ให้เป็นเครื่องมือที่ AI เรียกใช้ได้

**ReAct Pattern** - Reason → Act → Observe → Repeat

**Session Management** - แยกบริบทสำหรับผู้ใช้แต่ละคน

**Tool** - ฟังก์ชันที่เอเจนต์ AI สามารถเรียกใช้งานได้

**Tool Description** - เอกสารอธิบายจุดประสงค์และพารามิเตอร์ของเครื่องมือ

## โมดูล Agentic - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - ทำเครื่องหมายอินเทอร์เฟซเป็นเอเจนต์ AI พร้อมนิยามพฤติกรรมเชิงประกาศ

**Agent Listener** - ฮุกสำหรับติดตามการทำงานเอเจนต์ผ่าน `beforeAgentInvocation()` และ `afterAgentInvocation()`

**Agentic Scope** - หน่วยความจำร่วมที่เอเจนต์เก็บผลลัพธ์โดยใช้ `outputKey` เพื่อให้เอเจนต์ถัดไปใช้

**AgenticServices** - โรงงานสร้างเอเจนต์ผ่าน `agentBuilder()` และ `supervisorBuilder()`

**Conditional Workflow** - เส้นทางตามเงื่อนไขไปยังเอเจนต์ผู้เชี่ยวชาญต่าง ๆ

**Human-in-the-Loop** - รูปแบบเวิร์กโฟลว์เติมจุดตรวจสอบโดยมนุษย์สำหรับการอนุมัติหรือทบทวนเนื้อหา

**langchain4j-agentic** - การพึ่งพา Maven สำหรับการสร้างเอเจนต์เชิงประกาศ (ทดลอง)

**Loop Workflow** - ทำซ้ำการทำงานเอเจนต์จนกว่าจะถึงเงื่อนไข เช่น คะแนนคุณภาพ ≥ 0.8

**outputKey** - พารามิเตอร์การทำเครื่องหมายเอเจนต์ระบุที่เก็บผลลัพธ์ใน Agentic Scope

**Parallel Workflow** - รันเอเจนต์หลายตัวพร้อมกันสำหรับงานอิสระ

**Response Strategy** - วิธีที่ผู้ดูแลจัดทำคำตอบสุดท้าย: LAST, SUMMARY หรือ SCORED

**Sequential Workflow** - ดำเนินการเอเจนต์ตามลำดับโดยผลลัพธ์ส่งต่อไปยังขั้นตอนถัดไป

**Supervisor Agent Pattern** - รูปแบบเอเจนต์ขั้นสูงที่ผู้ดูแล LLM ตัดสินใจแบบไดนามิกเลือกเรียกใช้ซับเอเจนต์

## โปรโตคอลบริบทโมเดล (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - การพึ่งพา Maven สำหรับการรวม MCP ใน LangChain4j

**MCP** - Model Context Protocol: มาตรฐานเชื่อมแอป AI กับเครื่องมือภายนอก สร้างครั้งเดียว ใช้ได้ทุกที่

**MCP Client** - แอปที่เชื่อมต่อกับ MCP server เพื่อค้นหาและใช้เครื่องมือ

**MCP Server** - บริการที่เปิดเผยเครื่องมือผ่าน MCP พร้อมคำอธิบายชัดเจนและสคีมากำหนดพารามิเตอร์

**McpToolProvider** - ส่วนประกอบ LangChain4j ที่ห่อเครื่องมือ MCP เพื่อใช้งานในบริการ AI และเอเจนต์

**McpTransport** - อินเทอร์เฟซสำหรับการสื่อสาร MCP ตัวอย่างการใช้งาน ได้แก่ Stdio และ HTTP

**Stdio Transport** - การสื่อสารในขั้นตอนภายในเครื่องผ่าน stdin/stdout เหมาะสำหรับเข้าถึงไฟล์หรือเครื่องมือบรรทัดคำสั่ง

**StdioMcpTransport** - การใช้งาน LangChain4j ที่สร้าง MCP server เป็นกระบวนการย่อย

**Tool Discovery** - ไคลเอนต์สอบถามเซิร์ฟเวอร์หาเครื่องมือที่มีพร้อมคำอธิบายและสคีมา

## บริการ Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - การค้นหาในคลาวด์พร้อมความสามารถเวกเตอร์ [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - เครื่องมือสำหรับปรับใช้ทรัพยากร Azure

**Azure OpenAI** - บริการ AI สำหรับองค์กรของ Microsoft

**Bicep** - ภาษา infrastructure-as-code สำหรับ Azure [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - ชื่อที่ใช้สำหรับปรับใช้โมเดลใน Azure

**GPT-5.2** - โมเดล OpenAI รุ่นล่าสุดที่ควบคุมการทำเหตุผลได้ [Module 02](../02-prompt-engineering/README.md)

## การทดสอบและพัฒนา - [Testing Guide](TESTING.md)

**Dev Container** - สภาพแวดล้อมพัฒนาด้วย container [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - สนามทดลองโมเดล AI ฟรี [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - การทดสอบด้วยที่เก็บในหน่วยความจำ

**Integration Testing** - การทดสอบร่วมกับโครงสร้างพื้นฐานจริง

**Maven** - เครื่องมืออัตโนมัติสร้างโปรเจกต์ Java

**Mockito** - เฟรมเวิร์กสำหรับสร้าง mock ใน Java

**Spring Boot** - เฟรมเวิร์กแอปพลิเคชัน Java [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้มีความถูกต้อง โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาของตนเองถือเป็นแหล่งข้อมูลที่น่าเชื่อถือที่สุด สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลโดยผู้เชี่ยวชาญมนุษย์ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดจากการใช้การแปลฉบับนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->