# Module 05: मॉडल संदर्भ प्रोटोकॉल (MCP)

## Table of Contents

- [What You'll Learn](../../../05-mcp)
- [What is MCP?](../../../05-mcp)
- [How MCP Works](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [Running the Examples](../../../05-mcp)
  - [Prerequisites](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Running the Demo](../../../05-mcp)
    - [How the Supervisor Works](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## What You'll Learn

आपने वार्तालापात्मक AI बनाया है, प्रॉम्प्ट में महारत हासिल की है, दस्तावेज़ों में आधारित उत्तर बनाए हैं, और टूल्स के साथ एजेंट बनाए हैं। लेकिन ये सभी टूल आपके विशिष्ट एप्लिकेशन के लिए कस्टम-बिल्ट थे। अगर आप अपनी AI को एक मानकीकृत उपकरणों के पारिस्थितिकी तंत्र का उपयोग करने का एक्सेस दे सकें जिसका कोई भी निर्माण और साझा कर सकता है, तो? इस मॉड्यूल में, आप ये सीखेंगे कि मॉडल संदर्भ प्रोटोकॉल (MCP) और LangChain4j के एजेंटिक मॉड्यूल के साथ इसे कैसे किया जा सकता है। हम पहले एक साधारण MCP फाइल रीडर दिखाएंगे और फिर दिखाएंगे कि इसे कैसे आसानी से सुपरवाइज़र एजेंट पैटर्न का उपयोग करते हुए उन्नत एजेंटिक वर्कफ़्लोज़ में एकीकृत किया जा सकता है।

## What is MCP?

मॉडल संदर्भ प्रोटोकॉल (MCP) यही प्रदान करता है - AI एप्लिकेशन के लिए बाहरी टूल्स का पता लगाने और उपयोग करने का एक मानक तरीका। प्रत्येक डेटा स्रोत या सेवा के लिए कस्टम इंटीग्रेशन लिखने के बजाय, आप MCP सर्वरों से जुड़ते हैं जो अपनी क्षमताओं को एक सुसंगत प्रारूप में प्रकट करते हैं। आपका AI एजेंट तब स्वचालित रूप से इन टूल्स का पता लगा सकता है और उपयोग कर सकता है।

<img src="../../../translated_images/hi/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP से पहले: जटिल प्वाइंट-टू-पॉइंट इंटीग्रेशन। MCP के बाद: एक प्रोटोकॉल, अंतहीन संभावनाएं।*

MCP AI विकास में एक मौलिक समस्या को हल करता है: हर इंटीग्रेशन कस्टम है। GitHub एक्सेस करना चाहते हैं? कस्टम कोड। फाइलें पढ़नी हैं? कस्टम कोड। डेटाबेस क्वेरी करनी है? कस्टम कोड। और ये इंटीग्रेशन कोई अन्य AI एप्लिकेशन के साथ काम नहीं करते।

MCP इसे मानकीकृत करता है। एक MCP सर्वर स्पष्ट विवरण और स्कीमाओं के साथ टूल्स को प्रकट करता है। कोई भी MCP क्लाइंट कनेक्ट कर सकता है, उपलब्ध टूल्स का पता लगा सकता है, और उन्हें उपयोग कर सकता है। एक बार बनाएं, हर जगह उपयोग करें।

<img src="../../../translated_images/hi/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*मॉडल संदर्भ प्रोटोकॉल वास्तुकला - मानकीकृत टूल खोज और निष्पादन*

## How MCP Works

<img src="../../../translated_images/hi/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP कैसे काम करता है — क्लाइंट टूल्स का पता लगाते हैं, JSON-RPC संदेशों का आदान-प्रदान करते हैं, और एक ट्रांसपोर्ट लेयर के माध्यम से ऑपरेशन्स को निष्पादित करते हैं।*

**सर्वर-क्लाइंट वास्तुकला**

MCP एक क्लाइंट-सर्वर मॉडल का उपयोग करता है। सर्वर टूल्स प्रदान करते हैं - फाइलें पढ़ना, डेटाबेस क्वेरी करना, API कॉल करना। क्लाइंट (आपका AI एप्लिकेशन) सर्वरों से जुड़ता है और उनके टूल्स का उपयोग करता है।

LangChain4j के साथ MCP का उपयोग करने के लिए, इस Maven निर्भरता को जोड़ें:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**टूल खोज**

जब आपका क्लाइंट MCP सर्वर से जुड़ता है, तो वह पूछता है "आपके पास कौन से टूल्स हैं?" सर्वर उपलब्ध टूल्स की सूची देता है, प्रत्येक के विवरण और पैरामीटर स्कीमा के साथ। आपका AI एजेंट तब उपयोगकर्ता अनुरोधों के आधार पर कौन से टूल्स का उपयोग करना है, तय कर सकता है।

<img src="../../../translated_images/hi/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI स्टार्टअप पर उपलब्ध टूल्स का पता लगाता है — अब इसे पता है कि कौन-कौन सी क्षमताएं उपलब्ध हैं और वह तय कर सकता है कि किनका उपयोग करना है।*

**ट्रांसपोर्ट मैकेनिज्म**

MCP विभिन्न ट्रांसपोर्ट मैकेनिज्म का समर्थन करता है। यह मॉड्यूल स्थानीय प्रक्रियाओं के लिए Stdio ट्रांसपोर्ट दिखाता है:

<img src="../../../translated_images/hi/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP ट्रांसपोर्ट मैकेनिज्म: रिमोट सर्वरों के लिए HTTP, स्थानीय प्रक्रियाओं के लिए Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

स्थानीय प्रक्रियाओं के लिए। आपका एप्लिकेशन एक सर्वर subprocess के रूप में चलाता है और मानक इनपुट/आउटपुट के माध्यम से संवाद करता है। फाइलसिस्टेम एक्सेस या कमांड-लाइन टूल्स के लिए उपयोगी।

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

<img src="../../../translated_images/hi/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio ट्रांसपोर्ट क्रिया में — आपका एप्लिकेशन MCP सर्वर को एक चाइल्ड प्रोसेस के रूप में जन्म देता है और stdin/stdout पाइप्स के माध्यम से संवाद करता है।*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ आज़माएँ:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) खोलें और पूछें:
> - "Stdio ट्रांसपोर्ट कैसे काम करता है और इसे HTTP की तुलना में कब उपयोग करना चाहिए?"
> - "LangChain4j MCP सर्वर प्रक्रियाओं के जीवनचक्र को कैसे प्रबंधित करता है?"
> - "AI को फाइल सिस्टम एक्सेस देने के सुरक्षा निहितार्थ क्या हैं?"

## The Agentic Module

जबकि MCP मानकीकृत टूल्स प्रदान करता है, LangChain4j का **agentic मॉड्यूल** उन टूल्स को ऑर्केस्ट्रेट करने वाले एजेंट बनाने का एक उद्घोषात्मक तरीका प्रदान करता है। `@Agent` एनोटेशन और `AgenticServices` आपको एजेंट व्यवहार को अनुकरणों के माध्यम से परिभाषित करने देते हैं बजाय अनिवार्य कोड के।

इस मॉड्यूल में आप **Supervisor Agent** पैटर्न का अन्वेषण करेंगे — एक उन्नत एजेंटिक AI दृष्टिकोण जहां "सुपरवाइज़र" एजेंट उपयोगकर्ता अनुरोधों के आधार पर गतिशील रूप से उप-एजेंटों को निष्क्रिय करने का निर्णय लेता है। हम दोनों अवधारणाओं को एक साथ जोड़ेंगे जिससे हमारे उप-एजेंट MCP-संचालित फ़ाइल एक्सेस क्षमताएँ देंगे।

Agentic मॉड्यूल के उपयोग के लिए, इस Maven निर्भरता को जोड़ें:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ प्रयोगात्मक:** `langchain4j-agentic` मॉड्यूल **प्रयोगात्मक** है और परिवर्तन के अधीन है। AI सहायक बनाने का स्थिर तरीका `langchain4j-core` और कस्टम टूल्स (मॉड्यूल 04) है।

## Running the Examples

### Prerequisites

- Java 21+, Maven 3.9+
- Node.js 16+ और npm (MCP सर्वरों के लिए)
- `.env` फ़ाइल में पर्यावरण वेरिएबल सेट (रूट डायरेक्टरी से):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (मॉड्यूल 01-04 की तरह)

> **ध्यान दें:** यदि आपने अभी तक अपने पर्यावरण वेरिएबल्स सेट नहीं किए हैं, तो निर्देशों के लिए देखें [मॉड्यूल 00 - क्विक स्टार्ट](../00-quick-start/README.md), या `.env.example` को `.env` में कॉपी करें और अपनी मान भरें।

## Quick Start

**VS Code का उपयोग करते हुए:** एक्सप्लोरर में किसी भी डेमो फ़ाइल पर राइट-क्लिक करें और **"Run Java"** चुनें, या रन और डिबग पैनल से लॉन्च कॉन्फ़िग्रेशन का उपयोग करें (सुनिश्चित करें कि आपने पहले अपने टोकन को `.env` फ़ाइल में जोड़ा है)।

**Maven का उपयोग करते हुए:** वैकल्पिक रूप से, आप कमांड लाइन से नीचे दिए गए उदाहरण चला सकते हैं।

### File Operations (Stdio)

यह स्थानीय subprocess आधारित टूल्स को प्रदर्शित करता है।

**✅ कोई पूर्वापेक्षाएँ नहीं** - MCP सर्वर स्वचालित रूप से शुरू हो जाता है।

**स्टार्ट स्क्रिप्ट्स का उपयोग (अनुशंसित):**

स्टार्ट स्क्रिप्ट्स रूट `.env` फ़ाइल से पर्यावरण वेरिएबल्स स्वचालित रूप से लोड करती हैं:

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

**VS Code का उपयोग:** `StdioTransportDemo.java` पर राइट-क्लिक करें और **"Run Java"** चुनें (सुनिश्चित करें कि आपकी `.env` फ़ाइल कॉन्फ़िगर है)।

एप्लिकेशन स्वचालित रूप से फाइल सिस्टम MCP सर्वर को जन्म देता है और एक स्थानीय फ़ाइल पढ़ता है। ध्यान दें कि subprocess प्रबंधन आपके लिए कैसे संभाला गया है।

**अपेक्षित आउटपुट:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent पैटर्न** एजेंटिक AI का एक **लचीला** रूप है। एक सुपरवाइज़र LLM का उपयोग स्वायत्त रूप से तय करता है कि उपयोगकर्ता की अनुरोध के आधार पर किन एजेंटों को कॉल करना है। अगले उदाहरण में, हम MCP संचालित फ़ाइल एक्सेस को LLM एजेंट के साथ जोड़ेंगे ताकि एक नियंत्रित फ़ाइल पढ़ने → रिपोर्ट कार्यप्रवाह बनाया जा सके।

डेमो में, `FileAgent` MCP फ़ाइल सिस्टम टूल्स का उपयोग करके फ़ाइल पढ़ता है, और `ReportAgent` एक संरचित रिपोर्ट बनाता है जिसमें एक कार्यकारी सारांश (1 वाक्य), 3 मुख्य बिंदु, और सिफारिशें शामिल हैं। Supervisor इस प्रवाह का स्वतः संचालन करता है:

<img src="../../../translated_images/hi/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*सुपरवाइज़र अपने LLM का उपयोग करता है यह तय करने के लिए कि किन एजेंटों को और किस क्रम में कॉल करना है — कोई हार्डकोडेड राउटिंग आवश्यक नहीं।*

हमारे फ़ाइल-से-रिपोर्ट पाइपलाइन के लिए सटीक वर्कफ़्लो इस प्रकार है:

<img src="../../../translated_images/hi/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent MCP टूल्स के माध्यम से फ़ाइल पढ़ता है, फिर ReportAgent कच्चे कंटेंट को संरचित रिपोर्ट में परिवर्तित करता है।*

प्रत्येक एजेंट अपना आउटपुट **Agentic Scope** (साझा मेमोरी) में संग्रहीत करता है, जिससे डाउनस्ट्रीम एजेंट पूर्व परिणामों तक पहुँच सकते हैं। यह दिखाता है कि MCP टूल्स एजेंटिक वर्कफ़्लोज़ में कैसे निर्बाध रूप से इंटीग्रेट होते हैं — सुपरवाइज़र को यह जानने की जरूरत नहीं कि फाइलें *कैसे* पढ़ी जाती हैं, केवल यह कि `FileAgent` वह काम कर सकता है।

#### Running the Demo

स्टार्ट स्क्रिप्ट्स रूट `.env` फ़ाइल से पर्यावरण वेरिएबल्स को स्वचालित रूप से लोड करती हैं:

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

**VS Code का उपयोग:** `SupervisorAgentDemo.java` पर राइट-क्लिक करें और **"Run Java"** चुनें (सुनिश्चित करें कि आपकी `.env` फ़ाइल कॉन्फ़िगर है)।

#### How the Supervisor Works

```java
// चरण 1: FileAgent MCP उपकरणों का उपयोग करके फाइलों को पढ़ता है
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // फाइल ऑपरेशनों के लिए MCP उपकरण हैं
        .build();

// चरण 2: ReportAgent संरचित रिपोर्ट उत्पन्न करता है
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor फाइल → रिपोर्ट वर्कफ़्लो का समन्वय करता है
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // अंतिम रिपोर्ट लौटाएं
        .build();

// Supervisor अनुरोध के आधार पर कौन से एजेंट को सक्रिय करना है, तय करता है
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

जब आप `SupervisorAgent` को कॉन्फ़िगर करते हैं, तो आप निर्दिष्ट करते हैं कि उप-एजेंट्स के कार्य पूर्ण होने के बाद वह उपयोगकर्ता को अपना अंतिम उत्तर कैसे देगा।

<img src="../../../translated_images/hi/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*सुपरवाइज़र के लिए तीन रणनीतियाँ जो अंतिम उत्तर तैयार करती हैं — आप चुन सकते हैं कि अंतिम एजेंट का आउटपुट, एक संश्लेषित सारांश, या सर्वोत्तम स्कोर विकल्प चाहिए।*

उपलब्ध रणनीतियाँ हैं:

| रणनीति | विवरण |
|----------|-------------|
| **LAST** | सुपरवाइज़र अंतिम उप-एजेंट या टूल के आउटपुट को वापस करता है। यह उपयोगी है जब वर्कफ़्लो का अंतिम एजेंट विशेष रूप से पूर्ण, अंतिम उत्तर प्रदान करने के लिए डिज़ाइन किया गया हो (जैसे, अनुसंधान पाइपलाइन में "सारांश एजेंट")। |
| **SUMMARY** | सुपरवाइज़र अपने आंतरिक भाषा मॉडल (LLM) का उपयोग करके पूरे इंटरैक्शन और सभी उप-एजेंट आउटपुट का सारांश बनाता है, फिर उस सारांश को अंतिम उत्तर के रूप में देता है। यह उपयोगकर्ता को एक साफ, समेकित उत्तर प्रदान करता है। |
| **SCORED** | सिस्टम आंतरिक LLM का उपयोग करके दोनों LAST प्रतिक्रिया और SUMMARY को उपयोगकर्ता के मूल अनुरोध के विरुद्ध स्कोर करता है, और जो भी आउटपुट उच्चतर स्कोर प्राप्त करता है उसे लौटाता है। |

पूर्ण कार्यान्वयन के लिए देखें [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ आज़माएँ:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) खोलें और पूछें:
> - "सुपरवाइज़र किन एजेंट्स को कॉल करना है, यह कैसे तय करता है?"
> - "सुपरवाइज़र और क्रमिक वर्कफ़्लो पैटर्न में क्या अंतर है?"
> - "सुपरवाइज़र की योजना बनाने के व्यवहार को मैं कैसे कस्टमाइज़ कर सकता हूँ?"

#### Understanding the Output

जब आप डेमो चलाते हैं, तो आप देखेंगे कि सुपरवाइज़र कई एजेंट्स को कैसे ऑर्केस्ट्रेट करता है। यहां प्रत्येक अनुभाग का अर्थ है:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**हेडर** कार्यप्रवाह अवधारणा का परिचय देता है: फ़ाइल पढ़ने से रिपोर्ट बनाने तक केंद्रित पाइपलाइन।

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

**वर्कफ़्लो आरेख** एजेंटों के बीच डेटा प्रवाह दिखाता है। प्रत्येक एजेंट की एक विशेष भूमिका है:
- **FileAgent** MCP टूल्स का उपयोग करके फाइलें पढ़ता है और कच्चा कंटेंट `fileContent` में संग्रहीत करता है
- **ReportAgent** उस सामग्री का उपयोग करता है और `report` में एक संरचित रिपोर्ट उत्पन्न करता है

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**उपयोगकर्ता अनुरोध** कार्य दिखाता है। सुपरवाइज़र इसे पार्स करता है और FileAgent → ReportAgent को कॉल करने का निर्णय लेता है।

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

**सुपरवाइज़र ऑर्केस्ट्रेशन** 2-चरणीय प्रवाह को क्रियान्वित दिखाता है:
1. **FileAgent** MCP के माध्यम से फाइल पढ़ता है और सामग्री संग्रहीत करता है
2. **ReportAgent** सामग्री प्राप्त करता है और एक संरचित रिपोर्ट बनाता है

सुपरवाइज़र ने उपयोगकर्ता के अनुरोध के आधार पर ये निर्णय **स्वायत्त रूप से** लिए।

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

#### Explanation of Agentic Module Features

यह उदाहरण एजेंटिक मॉड्यूल की कई उन्नत विशेषताओं को दर्शाता है। आइए एजेंटिक स्कोप और एजेंट लिस्नर्स पर नज़दीकी नज़र डालें।

**Agentic Scope** साझा मेमोरी दिखाता है जहाँ एजेंटों ने `@Agent(outputKey="...")` का उपयोग करके अपने परिणाम संग्रहीत किए। यह अनुमति देता है:
- बाद के एजेंट पहले के आउटपुट तक पहुँच सकें
- सुपरवाइज़र अंतिम उत्तर को संश्लेषित कर सके
- आप जांच सकें कि प्रत्येक एजेंट ने क्या आउटपुट दिया

<img src="../../../translated_images/hi/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope साझा मेमोरी के रूप में कार्य करता है — FileAgent `fileContent` लिखता है, ReportAgent इसे पढ़ता है और `report` लिखता है, और आपका कोड अंतिम परिणाम पढ़ता है।*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent से कच्चा फ़ाइल डेटा
String report = scope.readState("report");            // ReportAgent से संरचित रिपोर्ट
```

**Agent Listeners** एजेंट निष्पादन की निगरानी और डिबगिंग सक्षम करते हैं। डेमो में जो कदम-दर-कदम आउटपुट आप देखते हैं वह एक AgentListener से आता है जो प्रत्येक एजेंट कॉल में हुक करता है:
- **beforeAgentInvocation** - जब Supervisor एजेंट का चयन करता है तो कॉल किया जाता है, जिससे आप देख सकते हैं कि कौन सा एजेंट चुना गया और क्यों
- **afterAgentInvocation** - जब एक एजेंट पूरा हो जाता है तो कॉल किया जाता है, इसका परिणाम दिखाता है
- **inheritedBySubagents** - जब true होता है, तो लिस्नर पदानुक्रम में सभी एजेंट्स की निगरानी करता है

<img src="../../../translated_images/hi/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*एजेंट लिस्नर निष्पादन जीवनचक्र में जुड़े होते हैं — यह निगरानी करते हैं कि एजेंट कब शुरू होते हैं, पूरा करते हैं, या त्रुटियों का सामना करते हैं।*

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
        return true; // सभी उप-एजेंटों तक फैलाएं
    }
};
```

Supervisor पैटर्न के अलावा, `langchain4j-agentic` मॉड्यूल कई शक्तिशाली वर्कफ़्लो पैटर्न और फीचर्स प्रदान करता है:

<img src="../../../translated_images/hi/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*पांच वर्कफ़्लो पैटर्न एजेंट्स का समन्वय करने के लिए — सरल अनुक्रमिक पाइपलाइनों से लेकर मानव-इन-द-लूप अनुमोदन वर्कफ़्लोज़ तक।*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | एजेंटों को क्रम में निष्पादित करें, आउटपुट अगले को जाता है | पाइपलाइन्स: शोध → विश्लेषण → रिपोर्ट |
| **Parallel** | एजेंटों को एक साथ चलाएं | स्वतंत्र कार्य: मौसम + समाचार + स्टॉक |
| **Loop** | तब तक दोहराएं जब तक शर्त पूरी न हो | गुणवत्ता स्कोरिंग: तब तक सुधारें जब तक स्कोर ≥ 0.8 न हो |
| **Conditional** | शर्तों के आधार पर मार्गदर्शन करें | वर्गीकरण → विशेषज्ञ एजेंट को मार्गदर्शन |
| **Human-in-the-Loop** | मानव जांच बिंदु जोड़ें | अनुमोदन वर्कफ़्लोज़, सामग्री समीक्षा |

## महत्वपूर्ण अवधारणाएँ

अब जब आपने MCP और एजेंटिक मॉड्यूल को क्रियान्वित करते हुए देखा है, तो आइए संक्षेप करें कि प्रत्येक दृष्टिकोण का उपयोग कब करना है।

<img src="../../../translated_images/hi/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP एक सार्वभौमिक प्रोटोकॉल पारिस्थितिकी तंत्र बनाता है — कोई भी MCP-संगत सर्वर किसी भी MCP-संगत क्लाइंट के साथ काम करता है, जो एप्लिकेशन के बीच टूल साझा करने में सक्षम बनाता है।*

**MCP** आदर्श है जब आप मौजूदा टूल पारिस्थितिकी तंत्र का लाभ उठाना चाहते हैं, ऐसे टूल बनाना चाहते हैं जिन्हें कई एप्लिकेशन साझा कर सकें, तृतीय-पक्ष सेवाओं को मानक प्रोटोकॉल के साथ एकीकृत करना चाहते हैं, या बिना कोड बदले टूल के कार्यान्वयन को बदलना चाहते हैं।

**एजेंटिक मॉड्यूल** तब सबसे अच्छा काम करता है जब आप `@Agent` एनोटेशन के साथ घोषणात्मक एजेंट परिभाषाएं चाहते हैं, वर्कफ़्लो समन्वयन (अनुक्रमिक, लूप, समानांतर) की जरूरत होती है, इंटरफ़ेस-आधारित एजेंट डिज़ाइन पसंद करते हैं बजाय अनिवार्य कोड के, या आप कई एजेंट्स को एक साथ जोड़ रहे होते हैं जो `outputKey` के माध्यम से आउटपुट साझा करते हैं।

**Supervisor एजेंट पैटर्न** तब उत्कृष्ट होता है जब वर्कफ़्लो पहले से पूर्वानुमेय नहीं होता है और आप LLM को निर्णय लेना चाहते हैं, जब आपके पास कई विशेषीकृत एजेंट होते हैं जिन्हें गतिशील समन्वयन की आवश्यकता होती है, जब आप वार्तालाप प्रणाली बना रहे होते हैं जो विभिन्न क्षमताओं को मार्गदर्शित करती है, या जब आप सबसे लचीले, अनुकूलनीय एजेंट व्यवहार चाहते हैं।

<img src="../../../translated_images/hi/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*कस्टम @Tool मेथड्स कब उपयोग करें बनाम MCP टूल्स — कस्टम टूल्स ऐप-विशिष्ट लॉजिक के लिए पूर्ण प्रकार सुरक्षा के साथ, MCP टूल्स मानकीकृत एकीकरणों के लिए जो एप्लिकेशन में काम करते हैं।*

## बधाई!

<img src="../../../translated_images/hi/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*आपकी सीखने की यात्रा सभी पांच मॉड्यूल से होकर — प्रारंभिक चैट से MCP-संचालित एजेंटिक सिस्टम तक।*

आपने LangChain4j for Beginners कोर्स पूरा किया है। आपने सीखा:

- मेमोरी के साथ वार्तालाप एआई बनाना (मॉड्यूल 01)
- विभिन्न कार्यों के लिए प्रॉम्प्ट इंजीनियरिंग पैटर्न (मॉड्यूल 02)
- अपने दस्तावेज़ों में प्रतिक्रियाओं को आधार करने के लिए RAG (मॉड्यूल 03)
- कस्टम टूल्स के साथ बुनियादी AI एजेंट (सहायक) बनाना (मॉड्यूल 04)
- LangChain4j MCP और एजेंटिक मॉड्यूल के साथ मानकीकृत टूल्स को एकीकृत करना (मॉड्यूल 05)

### अगला क्या है?

मॉड्यूल्स पूरा करने के बाद, LangChain4j परीक्षण अवधारणाओं को जानने के लिए [Testing Guide](../docs/TESTING.md) का अन्वेषण करें।

**आधिकारिक संसाधन:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - व्यापक मार्गदर्शिकाएँ और API संदर्भ
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - स्रोत कोड और उदाहरण
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - विभिन्न उपयोग मामलों के लिए चरण-दर-चरण ट्यूटोरियल

इस कोर्स को पूरा करने के लिए आपका धन्यवाद!

---

**नेविगेशन:** [← पिछला: मॉड्यूल 04 - टूल्स](../04-tools/README.md) | [मुख्य पृष्ठ पर वापस](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या असंगतियाँ हो सकती हैं। मूल दस्तावेज़ को उसकी मूल भाषा में अधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->