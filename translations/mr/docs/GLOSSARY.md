# LangChain4j शब्दसंग्रह

## अनुक्रमणिका

- [मूल संकल्पना](../../../docs)
- [LangChain4j घटक](../../../docs)
- [AI/ML संकल्पना](../../../docs)
- [गार्डरेल्स](../../../docs)
- [प्रॉम्प्ट अभियांत्रिकी](../../../docs)
- [RAG (पुनर्प्राप्त-समृद्ध पिढी)](../../../docs)
- [एजंट आणि साधने](../../../docs)
- [एजंटिक मॉड्यूल](../../../docs)
- [मॉडेल संदर्भ प्रोटोकॉल (MCP)](../../../docs)
- [Azure सेवा](../../../docs)
- [चाचणी आणि विकास](../../../docs)

कोर्समध्ये वापरल्या गेलेल्या संज्ञा आणि संकल्पनांसाठी जलद संदर्भ.

## Core Concepts

**AI Agent** - AI वापरून स्वयंचलितपणे विचार करतो व कार्य करतो असा प्रणाली. [Module 04](../04-tools/README.md)

**Chain** - ऑपरेशन्सची सलग मालिका जिथे आउटपुट पुढल्या टप्प्यासाठी वापरला जातो.

**Chunking** - दस्तऐवज लहान तुकड्यांमध्ये विभागणे. सामान्यतः: 300-500 टोकन्स ओव्हरलॅपसह. [Module 03](../03-rag/README.md)

**Context Window** - एखादा मॉडेल प्रक्रिया करू शकतो तेव्हढे टोकन्सचा कमाल मर्यादा. GPT-5.2: 400K टोकन्स.

**Embeddings** - मजकूराच्या अर्थाचे संख्यात्मक वेक्टर. [Module 03](../03-rag/README.md)

**Function Calling** - मॉडेल बाह्य फंक्शन्स कॉल करण्यासाठी संरचित विनंत्या तयार करते. [Module 04](../04-tools/README.md)

**Hallucination** - जेव्हा मॉडेल चुका असलेली पण तर्कसंगत माहिती निर्माण करतात.

**Prompt** - भाषेच्या मॉडेलसाठी मजकूर इनपुट. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - कीवर्ड न वापरता embeddings च्या वापराने अर्थानुसार शोध. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: कोणतीही स्मृती नाही. Stateful: संभाषणाचा इतिहास ठेवतो. [Module 01](../01-introduction/README.md)

**Tokens** - मूलभूत मजकूर घटक ज्यावर मॉडेल प्रक्रिया करतात. खर्च आणि मर्यादा यावर परिणाम होतो. [Module 01](../01-introduction/README.md)

**Tool Chaining** - सलग साधने चालविणे ज्यात आउटपुट पुढच्या कॉलसाठी माहिती पुरवते. [Module 04](../04-tools/README.md)

## LangChain4j Components

**AiServices** - प्रकार-सुरक्षित AI सेवा इंटरफेस तयार करतो.

**OpenAiOfficialChatModel** - OpenAI आणि Azure OpenAI मॉडेलसाठी एकसंध क्लायंट.

**OpenAiOfficialEmbeddingModel** - OpenAI अधिकृत क्लायंटने embeddings तयार करतो (OpenAI आणि Azure OpenAI दोन्ही समर्थित).

**ChatModel** - भाषावली मॉडेलसाठी मुख्य इंटरफेस.

**ChatMemory** - संभाषणाचा इतिहास राखतो.

**ContentRetriever** - RAG साठी संबंधित दस्तऐवजांचे तुकडे शोधतो.

**DocumentSplitter** - दस्तऐवज तुकड्यांत विभागतो.

**EmbeddingModel** - मजकूराला संख्यात्मक वेक्टरमध्ये परिवर्तित करतो.

**EmbeddingStore** - embeddings संचयित आणि पुनर्प्राप्त करतो.

**MessageWindowChatMemory** - अलीकडील संदेशांसाठी स्लायडिंग विंडो राखतो.

**PromptTemplate** - `{{variable}}` प्लेसहोल्डरसह पुनर्वापरयोग्य प्रॉम्प्ट तयार करतो.

**TextSegment** - मेटाडेटासह मजकूराचा तुकडा. RAG मध्ये वापरला जातो.

**ToolExecutionRequest** - साधन संचालन विनंतीचे प्रतिनिधित्व करतो.

**UserMessage / AiMessage / SystemMessage** - संभाषण संदेश प्रकार.

## AI/ML Concepts

**Few-Shot Learning** - प्रॉम्प्टमध्ये उदाहरणे पुरवणे. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - प्रचंड मजकूर डेटा वापरून प्रशिक्षित AI मॉडेल.

**Reasoning Effort** - विचार करण्याच्या खोलीसाठी GPT-5.2 मधील पॅरामिटर. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - आउटपुटमध्ये randomness नियंत्रित करते. कमी=निर्धारिक, जास्त=सर्जनशील.

**Vector Database** - embeddings साठी खास डेटाबेस. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - उदाहरणांशिवाय कार्य करणे. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - बहुस्तरीय सुरक्षा पध्दत जी application-लेव्हल गार्डरेल्स आणि provider सुरक्षा फिल्टर्स एकत्र करते.

**Hard Block** - गंभीर कंटेंट उल्लंघनांसाठी provider HTTP 400 त्रुटी फेकतो.

**InputGuardrail** - LLM पर्यंत पोहोचण्याआधी वापरकर्त्याचा इनपुट तपासण्यासाठी LangChain4j इंटरफेस. हानिकारक प्रॉम्प्ट आधीच ब्लॉक करून खर्च व विलंब कमी करतो.

**InputGuardrailResult** - गार्डरेल पडताळणीचे परतवणूक प्रकार: `success()` किंवा `fatal("reason")`.

**OutputGuardrail** - वापरकर्त्याला परत देण्याआधी AI प्रतिक्रिया तपासण्यासाठी इंटरफेस.

**Provider Safety Filters** - AI प्रदात्यांकडून तयार केलेले API स्तरावरील कंटेंट फिल्टर्स (उदा. GitHub मॉडेल्स) जे उल्लंघने पकडतात.

**Soft Refusal** - मॉडेल आदरपूर्वक नकार देते परंतु त्रुटी नाही फेकते.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - अचूकतेसाठी पायरी-वार विचार प्रक्रिया.

**Constrained Output** - विशिष्ट स्वरूप किंवा रचना बंधनकारक करणे.

**High Eagerness** - GPT-5.2 मधील तंतोतंत विचारसक्रियतेचा नमुना.

**Low Eagerness** - GPT-5.2 मधील जलद उत्तर देण्याचा नमुना.

**Multi-Turn Conversation** - विनिमयांमध्ये संदर्भ राखणे.

**Role-Based Prompting** - प्रणाली संदेशाद्वारे मॉडेल व्यक्तिमत्व निश्चित करणे.

**Self-Reflection** - मॉडेल स्वतःच्या आउटपुटचे मूल्यमापन व सुधारणा करते.

**Structured Analysis** - निश्चित मूल्यमापन चौकट.

**Task Execution Pattern** - योजना → अंमलबजावणी → सारांश.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - लोड → तुकडा करा → एम्बेड करा → संचयित करा.

**In-Memory Embedding Store** - चाचणीसाठी नॉन-पर्सिस्टंट साठवण.

**RAG** - पुनर्प्राप्ती आणि निर्माण यांचा संगम करून उत्तरे आधारभूत करतो.

**Similarity Score** - सारखेपणाचे मापन (0-1).

**Source Reference** - पुनर्प्राप्त कंटेंटचे मेटाडेटा.

## Agents and Tools - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Java पद्धतींना AI-कॉल करण्यायोग्य साधन म्हणून चिन्हांकित करते.

**ReAct Pattern** - विचार करा → कृती करा → निरीक्षण करा → पुनरावृत्ती करा.

**Session Management** - वेगवेगळ्या वापरकर्त्यांसाठी वेगळे संदर्भ.

**Tool** - AI एजंट कॉल करू शकणारे फंक्शन.

**Tool Description** - साधनाचा उद्देश आणि पॅरामिटर्सचे दस्तऐवजीकरण.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - डिक्लेयरेटिव्ह वर्तन व्याख्येसह इंटरफेसना AI एजंट म्हणून चिन्हांकित करते.

**Agent Listener** - `beforeAgentInvocation()` आणि `afterAgentInvocation()` द्वारे एजंट कार्यान्वयनाची देखरेख करण्यासाठी हुक.

**Agentic Scope** - सामायिक स्मृती जिथे एजंट आउटपुट `outputKey` वापरून साठवतात ज्याचा खालील एजंट वापर करतात.

**AgenticServices** - `agentBuilder()` आणि `supervisorBuilder()` वापरून एजंट तयार करण्यासाठी फॅक्टरी.

**Conditional Workflow** - शर्तींवर आधारित विविध विशेषज्ञ एजंटांकडे मार्गनिर्देशन.

**Human-in-the-Loop** - मानवी मान्यता किंवा सामग्री पुनरावलोकनासाठी मानवी तपासणी बिंदू असलेली कार्यप्रवाह रचना.

**langchain4j-agentic** - डिक्लेयरेटिव्ह एजंट बिल्डिंगसाठी मावेन अवलंबित्व (प्रायोगिक).

**Loop Workflow** - एका शर्तीपर्यंत (उदा. गुणवत्ता स्कोअर ≥ 0.8) एजंट कार्यान्वित करणे पुनरावृत्ती.

**outputKey** - एजंट अ‍ॅनोटेशन पॅरामिटर जे Agentic Scope मध्ये निकाल कुठे साठवायचा ते निर्दिष्ट करतो.

**Parallel Workflow** - स्वतंत्र कामांसाठी एकाच वेळी अनेक एजंट चालविणे.

**Response Strategy** - पर्यवेक्षक अंतिम उत्तर कसे तयार करतो: LAST, SUMMARY, किंवा SCORED.

**Sequential Workflow** - एजंट सलग चालविणे जिथे आउटपुट पुढच्या टप्प्यासाठी जातो.

**Supervisor Agent Pattern** - प्रगत एजंटिक नमुना जिथे पर्यवेक्षक LLM डायनॅमिक पद्धतीने कोणत्या उप-एजंटना कॉल करायचे ते ठरवतो.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j मध्ये MCP एकत्रिकरणासाठी मावेन अवलंबित्व.

**MCP** - मॉडल संदर्भ प्रोटोकॉल: AI अ‍ॅप्सना बाह्य साधनांशी जोडण्यासाठी मानक. एकदा तयार करा, सर्व ठिकाणी वापरा.

**MCP Client** - MCP सर्व्हरशी जोडणारे आणि साधने वापरणारे अनुप्रयोग.

**MCP Server** - साधने MCP द्वारे स्पष्ट वर्णन आणि पॅरामिटर स्कीमासह प्रदर्शित करणारी सेवा.

**McpToolProvider** - LangChain4j घटक जो MCP साधने AI सेवा आणि एजंटसाठी वापरण्यासाठी वाळवितो.

**McpTransport** - MCP संवादासाठी इंटरफेस. अंमलबजावणींमध्ये Stdio आणि HTTP समाविष्ट.

**Stdio Transport** - स्थानिक प्रक्रिया ट्रान्सपोर्ट stdin/stdout द्वारे. फाइलसिस्टम प्रवेश किंवा कमांड-लाइन साधनांसाठी उपयुक्त.

**StdioMcpTransport** - LangChain4j अंमलबजावणी जी MCP सर्व्हर सबप्रोसेस म्हणून सुरू करते.

**Tool Discovery** - क्लायंट उपलब्ध साधनांसाठी वर्णन आणि स्कीमासह सर्व्हरला क्वेरी पाठवतो.

## Azure Services - [Module 01](../01-introduction/README.md)

**Azure AI Search** - वेक्टर क्षमतांसह क्लाउड शोध. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure संसाधने तैनात करते.

**Azure OpenAI** - मायक्रोसॉफ्टच्या एंटरप्राइझ AI सेवा.

**Bicep** - Azure इन्फ्रास्ट्रक्चर-एज-कोड भाषा. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Azure मधील मॉडेल तैनात करण्यासाठी नाव.

**GPT-5.2** - तर्कसंगत नियंत्रणासह नवीनतम OpenAI मॉडेल. [Module 02](../02-prompt-engineering/README.md)

## Testing and Development - [Testing Guide](TESTING.md)

**Dev Container** - कंटेनरयुक्त विकास वातावरण. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - मुक्त AI मॉडेल प्लेग्राऊंड. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - इन-मेमरी साठवणीसह चाचणी.

**Integration Testing** - खरी इन्फ्रास्ट्रक्चरसह चाचणी.

**Maven** - Java बिल्ड ऑटोमेशन साधन.

**Mockito** - Java मॉकिंग फ्रेमवर्क.

**Spring Boot** - Java अनुप्रयोग फ्रेमवर्क. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**संदर्भ हेतु सूचनाः**
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित केला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी, स्वयंचलित भाषांतरांमध्ये चुका किंवा असमर्थता असू शकते याबाबत कृपया जागरूक राहा. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतर करणे शिफारसीय आहे. या भाषांतराच्या वापरामुळे होणाऱ्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थ लावण्याबद्दल आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->