# Module 05: โปรโตคอลบริบทของโมเดล (MCP)

## สารบัญ

- [คุณจะได้เรียนรู้อะไร](../../../05-mcp)
- [MCP คืออะไร?](../../../05-mcp)
- [MCP ทำงานอย่างไร](../../../05-mcp)
- [โมดูลแบบตัวแทนปฏิบัติการ](../../../05-mcp)
- [การรันตัวอย่าง](../../../05-mcp)
  - [ข้อกำหนดเบื้องต้น](../../../05-mcp)
- [เริ่มต้นอย่างรวดเร็ว](../../../05-mcp)
  - [การจัดการไฟล์ (Stdio)](../../../05-mcp)
  - [ตัวแทนผู้ควบคุม](../../../05-mcp)
    - [การรันเดโม](../../../05-mcp)
    - [ตัวควบคุมทำงานอย่างไร](../../../05-mcp)
    - [กลยุทธ์การตอบสนอง](../../../05-mcp)
    - [ความเข้าใจผลลัพธ์](../../../05-mcp)
    - [คำอธิบายฟีเจอร์ของโมดูลตัวแทนปฏิบัติการ](../../../05-mcp)
- [แนวคิดสำคัญ](../../../05-mcp)
- [ยินดีด้วย!](../../../05-mcp)
  - [ขั้นตอนถัดไป?](../../../05-mcp)

## คุณจะได้เรียนรู้อะไร

คุณได้สร้าง AI สำหรับการสนทนา เชี่ยวชาญในการใช้ prompt สร้างคำตอบอิงตามเอกสาร และสร้างตัวแทนที่ใช้เครื่องมือต่างๆ แต่เครื่องมือทั้งหมดนั้นถูกสร้างขึ้นเองสำหรับแอปพลิเคชันเฉพาะของคุณ หากคุณสามารถให้ AI ของคุณเข้าถึงระบบนิเวศของเครื่องมือที่ได้มาตรฐานซึ่งใครๆ ก็สร้างและแบ่งปันได้ล่ะ? ในโมดูลนี้ คุณจะได้เรียนรู้วิธีทำเช่นนั้นด้วยโปรโตคอลบริบทของโมเดล (MCP) และโมดูลตัวแทนปฏิบัติการของ LangChain4j ก่อนอื่นเราจะแสดงตัวอ่านไฟล์ MCP ง่ายๆ และจากนั้นจะแสดงวิธีที่มันผสานรวมกับเวิร์กโฟลว์ตัวแทนปฏิบัติการขั้นสูงโดยใช้รูปแบบตัวแทนผู้ควบคุม (Supervisor Agent)

## MCP คืออะไร?

โปรโตคอลบริบทของโมเดล (MCP) ให้สิ่งนั้นเลย — วิธีมาตรฐานที่แอป AI จะค้นหาและใช้เครื่องมือภายนอก แทนที่จะเขียนการเชื่อมโยงแบบกำหนดเองสำหรับแต่ละแหล่งข้อมูลหรือบริการ คุณเชื่อมต่อกับเซิร์ฟเวอร์ MCP ที่เปิดเผยความสามารถในรูปแบบที่สม่ำเสมอ ตัวแทน AI ของคุณจึงสามารถค้นหาและใช้เครื่องมือเหล่านี้ได้โดยอัตโนมัติ

ไดอะแกรมด้านล่างแสดงความแตกต่าง — หากไม่มี MCP ทุกการเชื่อมต่อจะต้องเดินสายแบบจุดต่อจุดเฉพาะเจาะจง; แต่กับ MCP โปรโตคอลเดียวเชื่อมแอปของคุณกับเครื่องมือใดก็ได้:

<img src="../../../translated_images/th/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*ก่อน MCP: การเชื่อมต่อจุดต่อจุดซับซ้อน หลัง MCP: โปรโตคอลเดียว ความเป็นไปได้ไม่มีที่สิ้นสุด*

MCP แก้ปัญหาพื้นฐานในการพัฒนา AI คือ ทุกการเชื่อมต่อเป็นแบบกำหนดเอง ต้องการเข้าถึง GitHub? เขียนโค้ดเอง ต้องการอ่านไฟล์? โค้ดเอง ต้องการสอบถามฐานข้อมูล? โค้ดเอง และการเชื่อมต่อเหล่านี้ไม่มีใครทำงานร่วมกันกับแอป AI อื่นๆ

MCP มาตรฐานนี้ เซิร์ฟเวอร์ MCP เปิดเผยเครื่องมือพร้อมคำอธิบายและ schemas ชัดเจน ลูกค้า MCP ใดๆ ก็สามารถเชื่อมต่อ ค้นหาเครื่องมือที่มี และใช้งานได้ สร้างครั้งเดียว ใช้ทุกที่

ไดอะแกรมด้านล่างแสดงสถาปัตยกรรมนี้ — ลูกค้า MCP ตัวเดียว (แอป AI ของคุณ) เชื่อมต่อกับเซิร์ฟเวอร์ MCP หลายตัว แต่ละตัวเปิดเผยชุดเครื่องมือผ่านโปรโตคอลมาตรฐาน:

<img src="../../../translated_images/th/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*สถาปัตยกรรมโปรโตคอลบริบทของโมเดล — ค้นหาและใช้งานเครื่องมือแบบมาตรฐาน*

## MCP ทำงานอย่างไร

เบื้องหลัง MCP ใช้สถาปัตยกรรมแบบหลายชั้น แอป Java ของคุณ (ลูกค้า MCP) ค้นหาเครื่องมือที่มี ส่งคำขอ JSON-RPC ผ่านเลเยอร์ส่งข้อมูล (Stdio หรือ HTTP) และเซิร์ฟเวอร์ MCP ดำเนินการและส่งผลลัพธ์กลับ ไดอะแกรมด้านล่างแจกแจงแต่ละเลเยอร์ของโปรโตคอลนี้:

<img src="../../../translated_images/th/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*วิธีการทำงาน MCP เบื้องหลัง — ลูกค้าค้นหาเครื่องมือ ส่งข้อความ JSON-RPC และดำเนินการผ่านเลเยอร์ส่งข้อมูล*

**สถาปัตยกรรมเซิร์ฟเวอร์-ลูกค้า**

MCP ใช้รูปแบบเซิร์ฟเวอร์-ลูกค้า เซิร์ฟเวอร์ให้บริการเครื่องมือ — อ่านไฟล์ สอบถามฐานข้อมูล เรียก API ลูกค้า (แอป AI ของคุณ) เชื่อมต่อกับเซิร์ฟเวอร์และใช้เครื่องมือเหล่านั้น

หากต้องการใช้ MCP กับ LangChain4j ให้เพิ่ม dependency Maven นี้:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**การค้นหาเครื่องมือ**

เมื่อไคลเอนต์ของคุณเชื่อมต่อกับเซิร์ฟเวอร์ MCP มันจะถามว่า "คุณมีเครื่องมืออะไรบ้าง?" เซิร์ฟเวอร์ตอบด้วยรายการเครื่องมือที่มี พร้อมคำอธิบายและ schemas พารามิเตอร์ ตัวแทน AI ของคุณจึงตัดสินใจว่าจะใช้เครื่องมือใดตามคำขอของผู้ใช้ ไดอะแกรมด้านล่างแสดงการแลกเปลี่ยนนี้ — ไคลเอนต์ส่งคำขอ `tools/list` และเซิร์ฟเวอร์ส่งกลับเครื่องมือพร้อมคำอธิบายและ schemas พารามิเตอร์:

<img src="../../../translated_images/th/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI ค้นพบเครื่องมือที่มีตอนเริ่มต้น — รู้ความสามารถและตัดสินใจเลือกใช้*

**กลไกการส่งข้อมูล**

MCP รองรับกลไกการส่งข้อมูลหลายแบบ ตัวเลือกสองอย่างคือ Stdio (สำหรับการสื่อสารกับโปรเซสในเครื่อง) และ Streamable HTTP (สำหรับเซิร์ฟเวอร์ระยะไกล) โมดูลนี้สาธิตการส่งข้อมูลแบบ Stdio:

<img src="../../../translated_images/th/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*กลไกการส่งข้อมูล MCP: HTTP สำหรับเซิร์ฟเวอร์ระยะไกล, Stdio สำหรับโปรเซสในเครื่อง*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

สำหรับโปรเซสในเครื่อง แอปของคุณสร้างเซิร์ฟเวอร์เป็นโปรเซสลูกและสื่อสารผ่านอินพุต/เอาต์พุตมาตรฐาน เหมาะสำหรับการเข้าถึงไฟล์ซิสเต็มหรือเครื่องมือบรรทัดคำสั่ง

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

เซิร์ฟเวอร์ `@modelcontextprotocol/server-filesystem` เปิดเผยเครื่องมือต่อไปนี้ ซึ่งทั้งหมดถูกจำกัดในไดเรกทอรีที่คุณกำหนด:

| เครื่องมือ | คำอธิบาย |
|------|-------------|
| `read_file` | อ่านเนื้อหาของไฟล์เดียว |
| `read_multiple_files` | อ่านหลายไฟล์ในการเรียกครั้งเดียว |
| `write_file` | สร้างหรือเขียนทับไฟล์ |
| `edit_file` | แก้ไขค้นหาและแทนที่แบบเจาะจง |
| `list_directory` | แสดงรายการไฟล์และไดเรกทอรีในเส้นทาง |
| `search_files` | ค้นหาไฟล์แบบซ้ำในโฟลเดอร์ย่อยที่ตรงกับรูปแบบ |
| `get_file_info` | ดึงข้อมูลเมตาของไฟล์ (ขนาด, เวลาสร้าง, สิทธิ์) |
| `create_directory` | สร้างไดเรกทอรี (รวมไดเรกทอรีย่อยของโฟลเดอร์พ่อแม่) |
| `move_file` | ย้ายหรือเปลี่ยนชื่อไฟล์หรือไดเรกทอรี |

ไดอะแกรมด้านล่างแสดงวิธีการทำงานของ Stdio เมื่อรันเวลาจริง — แอป Java ของคุณสร้างเซิร์ฟเวอร์ MCP เป็นโปรเซสลูกและสื่อสารผ่านท่อ stdin/stdout ไม่มีการเชื่อมต่อเครือข่ายหรือ HTTP:

<img src="../../../translated_images/th/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*การส่งข้อมูลแบบ Stdio กำลังทำงาน — แอปของคุณสร้างเซิร์ฟเวอร์ MCP เป็นโปรเซสลูกและสื่อสารผ่านท่อ stdin/stdout*

> **🤖 ลองใช้กับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) และถามว่า:
> - "การส่งข้อมูลแบบ Stdio ทำงานอย่างไรและเมื่อไหร่ควรใช้แทน HTTP?"
> - "LangChain4j จัดการวงจรชีวิตของโปรเซสเซิร์ฟเวอร์ MCP อย่างไร?"
> - "ข้อควรระวังเรื่องความปลอดภัยในการให้ AI เข้าถึงไฟล์ซิสเต็มมีอะไรบ้าง?"

## โมดูลแบบตัวแทนปฏิบัติการ

ในขณะที่ MCP มอบเครื่องมือมาตรฐาน LangChain4j's **agentic module** ให้วิธีแบบประกาศเพื่อสร้างตัวแทนที่ควบคุมเครื่องมือเหล่านั้น การใช้ annotation `@Agent` และ `AgenticServices` ช่วยให้คุณกำหนดพฤติกรรมตัวแทนผ่านอินเทอร์เฟซแทนเขียนโค้ดลำดับขั้น

ในโมดูลนี้ คุณจะสำรวจรูปแบบ **Supervisor Agent** — วิธีการ AI แบบตัวแทนขั้นสูงที่ตัวแทน "ผู้ควบคุม" จะตัดสินใจแบบไดนามิกว่าจะเรียกตัวแทนใดย่อยตามคำขอผู้ใช้ เราจะรวมแนวคิดทั้งสองโดยให้อำนาจตัวแทนย่อยตัวหนึ่งในการเข้าถึงไฟล์ด้วย MCP

หากต้องการใช้โมดูล agentic ให้เพิ่ม dependency Maven นี้:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **หมายเหตุ:** โมดูล `langchain4j-agentic` ใช้ property รุ่นแยกต่างหาก (`langchain4j.mcp.version`) เพราะออกเวอร์ชันต่างกำหนดเวลาจากไลบรารีหลักของ LangChain4j

> **⚠️ กำลังทดสอบ:** โมดูล `langchain4j-agentic` ยังถือเป็น **เวอร์ชันทดลอง** และอาจเปลี่ยนแปลง วิธีสร้างผู้ช่วย AI ที่เสถียรยังคงเป็น `langchain4j-core` กับเครื่องมือแบบกำหนดเอง (โมดูล 04)

## การรันตัวอย่าง

### ข้อกำหนดเบื้องต้น

- ได้ทำ [Module 04 - เครื่องมือ](../04-tools/README.md) เสร็จ (โมดูลนี้สร้างต่อจากแนวคิดเครื่องมือกำหนดเองและเปรียบเทียบกับเครื่องมือ MCP)
- มีไฟล์ `.env` ในโฟลเดอร์หลักที่มีข้อมูลรับรอง Azure (สร้างโดย `azd up` ใน Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ และ npm (สำหรับเซิร์ฟเวอร์ MCP)

> **หมายเหตุ:** หากยังไม่ได้ตั้งค่าสภาพแวดล้อม ดู [Module 01 - บทนำ](../01-introduction/README.md) สำหรับวิธีติดตั้ง (`azd up` สร้างไฟล์ `.env` อัตโนมัติ) หรือคัดลอก `.env.example` เป็น `.env` ในโฟลเดอร์หลักและกรอกค่าของคุณ

## เริ่มต้นอย่างรวดเร็ว

**ใช้งาน VS Code:** คลิกขวาที่ไฟล์ตัวอย่างใน Explorer แล้วเลือก **"Run Java"** หรือใช้การตั้งค่าการรันในแผง Run and Debug (ตรวจสอบว่าไฟล์ `.env` ตั้งค่าข้อมูลรับรอง Azure แล้ว)

**ใช้งาน Maven:** หรือรันจากบรรทัดคำสั่งโดยใช้ตัวอย่างด้านล่าง

### การจัดการไฟล์ (Stdio)

แสดงตัวอย่างเครื่องมือที่ใช้โปรเซสย่อยในเครื่อง

**✅ ไม่ต้องมีข้อกำหนดล่วงหน้า** — เซิร์ฟเวอร์ MCP จะถูกสร้างขึ้นอัตโนมัติ

**ใช้สคริปต์เริ่มต้น (แนะนำ):**

สคริปต์เริ่มต้นโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ในโฟลเดอร์หลักอัตโนมัติ:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**ใช้ VS Code:** คลิกขวาที่ `StdioTransportDemo.java` และเลือก **"Run Java"** (ตรวจสอบว่าไฟล์ `.env` ตั้งค่าไว้แล้ว)

แอปจะสร้างเซิร์ฟเวอร์ MCP สำหรับไฟล์ซิสเต็มโดยอัตโนมัติและอ่านไฟล์ภายในเครื่อง ดูการจัดการโปรเซสย่อยที่ทำให้คุณไม่ต้องดูแลเอง

**ผลลัพธ์ที่คาดหวัง:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### ตัวแทนผู้ควบคุม

รูปแบบ **Supervisor Agent** เป็นรูปแบบที่ **ยืดหยุ่น** ของ agentic AI ตัวควบคุมใช้ LLM เพื่อเลือกตัวแทนย่อยที่จะเรียกใช้อัตโนมัติตามคำขอผู้ใช้ ในตัวอย่างถัดไป เรารวมการเข้าถึงไฟล์ด้วย MCP กับตัวแทน LLM เพื่อสร้างเวิร์กโฟลว์อ่านไฟล์ → สร้างรายงานที่ถูกควบคุมโดยผู้ควบคุม

ในเดโม `FileAgent` อ่านไฟล์โดยใช้เครื่องมือระบบไฟล์ MCP และ `ReportAgent` สร้างรายงานเป็นโครงสร้างพร้อมสรุปผู้บริหาร (1 ประโยค), 3 ประเด็นสำคัญ และข้อเสนอแนะ ตัวควบคุมจัดการเวิร์กโฟลว์นี้โดยอัตโนมัติ:

<img src="../../../translated_images/th/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*ตัวควบคุมใช้ LLM ของตนตัดสินใจเรียกตัวแทนไหนและเรียงลำดับอย่างไร — ไม่ต้องกำหนดเส้นทางแบบตายตัว*

นี่คือเวิร์กโฟลว์จริงของ pipeline อ่านไฟล์เป็นรายงาน:

<img src="../../../translated_images/th/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent อ่านไฟล์ผ่านเครื่องมือ MCP จากนั้น ReportAgent เปลี่ยนเนื้อหาดิบเป็นรายงานแบบมีโครงสร้าง*

แต่ละตัวแทนเก็บผลลัพธ์ใน **Agentic Scope** (หน่วยความจำร่วม) ทำให้ตัวแทนที่ตามมาเข้าถึงผลลัพธ์ก่อนหน้าได้ แสดงให้เห็นว่าเครื่องมือ MCP ผสานเข้ากับเวิร์กโฟลว์ตัวแทนได้อย่างไร — ตัวควบคุมไม่ต้องรู้ *วิธี* อ่านไฟล์ เพียงรู้ว่า `FileAgent` ทำได้

#### การรันเดโม

สคริปต์เริ่มต้นโหลดตัวแปรสภาพแวดล้อมจากไฟล์ `.env` ในโฟลเดอร์หลักอัตโนมัติ:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**ใช้ VS Code:** คลิกขวาที่ `SupervisorAgentDemo.java` และเลือก **"Run Java"** (ตรวจสอบว่าไฟล์ `.env` ตั้งค่าไว้)

#### ตัวควบคุมทำงานอย่างไร

ก่อนสร้างตัวแทน ต้องเชื่อมต่อ MCP transport เป็นลูกค้าและห่อหุ้มเป็น `ToolProvider` เพื่อให้เครื่องมือของเซิร์ฟเวอร์ MCP ใช้งานได้กับตัวแทนของคุณ:

```java
// สร้างไคลเอนต์ MCP จากทรานสปอร์ต
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// ห่อหุ้มไคลเอนต์เป็น ToolProvider — สิ่งนี้เชื่อมเครื่องมือ MCP เข้ากับ LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

ตอนนี้คุณสามารถฉีด `mcpToolProvider` เข้าไปในตัวแทนใดก็ได้ที่ต้องการเครื่องมือ MCP:

```java
// ขั้นตอนที่ 1: FileAgent อ่านไฟล์โดยใช้เครื่องมือ MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // มีเครื่องมือ MCP สำหรับการจัดการไฟล์
        .build();

// ขั้นตอนที่ 2: ReportAgent สร้างรายงานที่มีโครงสร้าง
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor ควบคุมกระบวนการไฟล์ → รายงาน
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // ส่งคืนรายงานสุดท้าย
        .build();

// Supervisor ตัดสินใจเรียกใช้งานเอเจนต์ตามคำขอ
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### กลยุทธ์การตอบสนอง

เมื่อคุณกำหนดค่า `SupervisorAgent` คุณระบุวิธีที่มันจะสร้างคำตอบสุดท้ายให้ผู้ใช้หลังจากตัวแทนย่อยทำงานเสร็จ ไดอะแกรมด้านล่างแสดง 3 กลยุทธ์ที่ใช้ได้ — LAST ส่งผลลัพธ์ของตัวแทนสุดท้ายโดยตรง, SUMMARY รวบรวมผลลัพธ์ทั้งหมดผ่าน LLM, และ SCORED เลือกผลคะแนนสูงสุดเทียบกับคำขอเดิม:

<img src="../../../translated_images/th/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*สามกลยุทธ์สำหรับการสร้างคำตอบสุดท้ายโดยผู้ควบคุม — เลือกตามต้องการว่าจะเอาผลลัพธ์ตัวแทนสุดท้าย, สรุปที่สังเคราะห์, หรือผลลัพธ์ที่ให้คะแนนดีที่สุด*

กลยุทธ์ที่มีคือ:

| กลยุทธ์ | คำอธิบาย |
|----------|-------------|
| **LAST** | ตัวควบคุมส่งผลลัพธ์จากตัวแทนย่อยหรือเครื่องมือที่เรียกใช้ล่าสุด เหมาะกับเมื่อตัวแทนสุดท้ายในเวิร์กโฟลว์ถูกออกแบบมาเพื่อสร้างคำตอบสมบูรณ์แบบสุดท้าย (เช่น “ตัวแทนสรุป” ใน pipeline งานวิจัย) |
| **SUMMARY** | ตัวควบคุมใช้ LLM ภายในของตัวเองสรุปการโต้ตอบทั้งหมดและผลลัพธ์ตัวแทนย่อยทั้งหมด จากนั้นส่งคืนสรุปนั้นเป็นคำตอบสุดท้าย ให้คำตอบที่รวมและชัดเจนแก่ผู้ใช้ |
| **SCORED** | ระบบใช้ LLM ภายในให้คะแนนทั้งผลตอบกลับ LAST และ SUMMARY เทียบกับคำขอผู้ใช้ต้นฉบับ และส่งคืนผลลัพธ์ที่มีคะแนนสูงกว่า |
ดูได้ที่ [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) สำหรับโค้ดเต็มรูปแบบ

> **🤖 ทดลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) แล้วถาม:
> - "ผู้ควบคุม (Supervisor) ตัดสินใจเลือกเอเจนต์ใดทำงานอย่างไร?"
> - "ความแตกต่างระหว่างรูปแบบการทำงานแบบ Supervisor กับ Sequential คืออะไร?"
> - "ฉันจะปรับแต่งพฤติกรรมการวางแผนของ Supervisor ได้อย่างไร?"

#### การทำความเข้าใจผลลัพธ์

เมื่อคุณรันเดโมนี้ คุณจะเห็นขั้นตอนที่เป็นโครงสร้างของการที่ Supervisor ประสานงานเอเจนต์หลายตัว ดังนี้คือความหมายของแต่ละส่วน:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**หัวข้อ** แนะนำแนวคิดของ workflow: ท่อที่เน้นตั้งแต่การอ่านไฟล์จนถึงการสร้างรายงาน

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**แผนภาพ Workflow** แสดงการไหลของข้อมูลระหว่างเอเจนต์ แต่ละเอเจนต์มีบทบาทเฉพาะ:
- **FileAgent** อ่านไฟล์โดยใช้เครื่องมือ MCP และเก็บเนื้อหาดิบใน `fileContent`
- **ReportAgent** ใช้เนื้อหานั้นและสร้างรายงานที่มีโครงสร้างใน `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**คำขอของผู้ใช้** แสดงงานที่ต้องทำ Supervisor จะวิเคราะห์คำขอนี้และตัดสินใจสั่งทำ FileAgent → ReportAgent

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**การจัดการของ Supervisor** แสดงการทำงาน 2 ขั้นตอนในทางปฏิบัติ:
1. **FileAgent** อ่านไฟล์ผ่าน MCP และเก็บเนื้อหา
2. **ReportAgent** รับเนื้อหาและสร้างรายงานที่มีโครงสร้าง

Supervisor ตัดสินใจเหล่านี้ได้ **โดยอัตโนมัติ** จากคำขอของผู้ใช้

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### คำอธิบายฟีเจอร์ของโมดูล Agentic

ตัวอย่างนี้แสดงฟีเจอร์ขั้นสูงบางอย่างของโมดูล agentic ลองดูการใช้งาน Agentic Scope และ Agent Listeners อย่างละเอียด

**Agentic Scope** คือหน่วยความจำร่วมที่เอเจนต์เก็บผลลัพธ์ของตนโดยใช้ `@Agent(outputKey="...")` ซึ่งอนุญาตให้:
- เอเจนต์ต่อไปเข้าถึงผลลัพธ์ของเอเจนต์ก่อนหน้า
- Supervisor สังเคราะห์คำตอบสุดท้าย
- คุณตรวจสอบสิ่งที่แต่ละเอเจนต์สร้างได้

แผนภาพด้านล่างแสดงการทำงานของ Agentic Scope ในแรมแชร์ที่ใช้จริงใน workflow ไฟล์ถึงรายงาน — FileAgent เขียนผลลัพธ์ภายใต้คีย์ `fileContent`, ReportAgent อ่านและเขียนผลลัพธ์ด้วยคีย์ `report`:

<img src="../../../translated_images/th/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope ทำหน้าที่เป็นแรมแชร์ — FileAgent เขียน `fileContent`, ReportAgent อ่านและเขียน `report`, และโค้ดของคุณอ่านผลลัพธ์สุดท้าย*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // ข้อมูลไฟล์ดิบจาก FileAgent
String report = scope.readState("report");            // รายงานที่มีโครงสร้างจาก ReportAgent
```

**Agent Listeners** ช่วยเฝ้าติดตามและดีบักการทำงานของเอเจนต์ เอาต์พุตขั้นตอนที่เห็นในเดโมมาจาก AgentListener ที่เชื่อมกับการเรียกใช้งานเอเจนต์แต่ละครั้ง:
- **beforeAgentInvocation** - เรียกก่อนที่ Supervisor จะเลือกเอเจนต์ เพื่อดูว่าเลือกเอเจนต์ใดและทำไม
- **afterAgentInvocation** - เรียกหลังเอเจนต์ทำงานเสร็จ แสดงผลลัพธ์
- **inheritedBySubagents** - ถ้าเป็นจริง listener จะเฝ้าติดตามเอเจนต์ทั้งหมดในลำดับชั้น

แผนภาพต่อไปนี้แสดง lifecycle ของ Agent Listener ทั้งหมด รวมถึงวิธีที่ `onError` จัดการความล้มเหลวระหว่างการทำเอเจนต์:

<img src="../../../translated_images/th/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners เชื่อมต่อกับวงจรชีวิตการทำงาน — เฝ้าดูเมื่อเอเจนต์เริ่ม ทำเสร็จ หรือเกิดข้อผิดพลาด*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // กระจายไปยังตัวแทนย่อยทั้งหมด
    }
};
```

นอกเหนือจากรูปแบบ Supervisor, โมดูล `langchain4j-agentic` ยังมีรูปแบบ workflow ที่ทรงพลังหลายแบบ แผนภาพด้านล่างแสดงทั้งหมดห้าแบบ — ตั้งแต่ท่อทางเดินแบบ sequential ง่ายๆ ถึง workflow การอนุมัติที่มีมนุษย์เข้ามาเกี่ยวข้อง:

<img src="../../../translated_images/th/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*รูปแบบ workflow ห้าแบบสำหรับการจัดการเอเจนต์ — ตั้งแต่ท่อทางเดินแบบ sequential ง่ายๆ ถึง workflow การอนุมัติที่มีมนุษย์เข้ามาเกี่ยวข้อง*

| รูปแบบ | คำอธิบาย | กรณีการใช้งาน |
|---------|-------------|----------|
| **Sequential** | เรียกเอเจนต์ทีละตัว ผลลัพธ์ไหลไปตัวถัดไป | ท่อการทำงาน: วิจัย → วิเคราะห์ → รายงาน |
| **Parallel** | รันเอเจนต์พร้อมกัน | งานแยกกัน: สภาพอากาศ + ข่าว + หุ้น |
| **Loop** | ทำซ้ำจนกว่าจะถึงเงื่อนไข | การให้คะแนนคุณภาพ: ปรับจนได้คะแนน ≥ 0.8 |
| **Conditional** | เลือกเส้นทางตามเงื่อนไข | แยกประเภท → ส่งต่อให้เอเจนต์ผู้เชี่ยวชาญ |
| **Human-in-the-Loop** | เพิ่มจุดตรวจสอบของมนุษย์ | Workflow การอนุมัติ, ตรวจสอบเนื้อหา |

## แนวคิดสำคัญ

ขณะนี้คุณได้สำรวจ MCP และโมดูล agentic แล้ว มาสรุปกันว่าเมื่อใดควรใช้แต่ละแนวทาง

ข้อได้เปรียบใหญ่ของ MCP คือระบบนิเวศที่เจริญเติบโต แผนภาพด้านล่างแสดงว่าโปรโตคอลสากลเชื่อมแอป AI ของคุณกับเซิร์ฟเวอร์ MCP หลากหลาย — ตั้งแต่เข้าถึงระบบไฟล์และฐานข้อมูล ไปจนถึง GitHub อีเมล เว็บสแครป และอื่นๆ:

<img src="../../../translated_images/th/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP สร้างระบบนิเวศโปรโตคอลสากล — เซิร์ฟเวอร์ MCP ใดๆ ที่เข้ากันได้สามารถทำงานกับไคลเอนต์ MCP ใดก็ได้ ช่วยให้แชร์เครื่องมือข้ามแอปได้ง่าย*

**MCP** เหมาะเมื่อคุณต้องการใช้ระบบนิเวศเครื่องมือที่มีอยู่ สร้างเครื่องมือที่แอปหลายตัวแชร์ได้ รวมบริการภายนอกโดยใช้โปรโตคอลมาตรฐาน หรือเปลี่ยนแปลงการใช้งานเครื่องมือโดยไม่ต้องแก้โค้ด

**โมดูล Agentic** เหมาะตอนที่คุณต้องการนิยามเอเจนต์แบบประกาศด้วย `@Agent` ต้องการจัดการ workflow (แบบ sequential, loop, parallel) ชอบออกแบบเอเจนต์โดยใช้ interface มากกว่าโค้ดเชิงคำสั่ง หรือใช้หลายเอเจนต์ที่แชร์ผลลัพธ์ผ่าน `outputKey`

**รูปแบบ Supervisor Agent** ดีเมื่อ workflow ไม่แน่นอนล่วงหน้า ต้องการให้ LLM ตัดสินใจ มีเอเจนต์ผู้เชี่ยวชาญหลายตัวที่ต้องบริหารงาน dynamical เมื่อต้องสร้างระบบสนทนาที่ส่งคำถามไปยังความสามารถต่างๆ หรือเมื่อต้องการพฤติกรรมเอเจนต์ที่ยืดหยุ่นและปรับตัวได้มากที่สุด

เพื่อช่วยตัดสินใจระหว่างวิธี `@Tool` แบบกำหนดเองใน Module 04 กับเครื่องมือ MCP จากโมดูลนี้ การเปรียบเทียบต่อไปนี้แสดงความแตกต่างหลัก — เครื่องมือกำหนดเองให้ความผูกพันแน่นและความปลอดภัยของชนิดข้อมูลเต็มรูปแบบสำหรับตรรกะเฉพาะแอป ขณะที่เครื่องมือ MCP ให้อินทิเกรชันแบบมาตรฐานและนำกลับมาใช้ใหม่ได้:

<img src="../../../translated_images/th/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*เมื่อใดควรใช้วิธี @Tool แบบกำหนดเอง vs เครื่องมือ MCP — เครื่องมือกำหนดเองสำหรับตรรกะเฉพาะแอปที่มีความปลอดภัยประเภทเต็มรูปแบบ, เครื่องมือ MCP สำหรับการเชื่อมต่อแบบมาตรฐานที่ใช้ได้ข้ามแอป*

## ขอแสดงความยินดี!

คุณได้ผ่านทุกโมดูลทั้งห้าในคอร์ส LangChain4j for Beginners แล้ว! นี่คือภาพรวมเส้นทางการเรียนรู้ที่คุณทำสำเร็จ — ตั้งแต่แชทเบื้องต้นจนถึงระบบ agentic ที่ใช้ MCP:

<img src="../../../translated_images/th/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*เส้นทางการเรียนรู้ของคุณผ่านห้าโมดูล — ตั้งแต่แชทเบื้องต้นจนถึงระบบ agentic ที่ใช้ MCP*

คุณได้จบคอร์ส LangChain4j for Beginners แล้ว คุณได้เรียนรู้:

- วิธีสร้าง AI สนทนาที่มีหน่วยความจำ (Module 01)
- รูปแบบวิศวกรรมพรอมต์สำหรับงานต่างๆ (Module 02)
- การเชื่อมโยงคำตอบกับเอกสารด้วย RAG (Module 03)
- การสร้างเอเจนต์ AI เบื้องต้น (ผู้ช่วย) ด้วยเครื่องมือกำหนดเอง (Module 04)
- การผสานเครื่องมือมาตรฐานด้วย LangChain4j MCP และโมดูล Agentic (Module 05)

### ต่อไปคืออะไร?

หลังจากจบโมดูลแล้ว ลองสำรวจ [คู่มือการทดสอบ](../docs/TESTING.md) เพื่อดูแนวคิดการทดสอบ LangChain4j ในทางปฏิบัติ

**แหล่งข้อมูลอย่างเป็นทางการ:**
- [เอกสาร LangChain4j](https://docs.langchain4j.dev/) - คู่มือครบถ้วนและเอกสาร API
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - โค้ดต้นฉบับและตัวอย่าง
- [บทเรียน LangChain4j](https://docs.langchain4j.dev/tutorials/) - บทเรียนทีละขั้นตอนสำหรับกรณีการใช้งานต่างๆ

ขอบคุณที่เรียนจบคอร์สนี้!

---

**นำทาง:** [← ก่อนหน้า: Module 04 - Tools](../04-tools/README.md) | [กลับสู่หน้าหลัก](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาอัตโนมัติ [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามทำให้มีความถูกต้องสูงสุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่ถูกต้อง หากเป็นข้อมูลที่สำคัญ ขอแนะนำให้ใช้การแปลโดยผู้เชี่ยวชาญด้านภาษา เรายังไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->