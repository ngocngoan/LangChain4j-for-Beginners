# LangChain4j शब्दावली

## विषय सूची

- [मुख्य अवधारणाहरू](../../../docs)
- [LangChain4j कम्पोनेन्टहरू](../../../docs)
- [AI/ML अवधारणाहरू](../../../docs)
- [गार्डरेलहरू](../../../docs)
- [प्रम्प्ट इन्जिनियरिङ](../../../docs)
- [RAG (सहायता गरिएको पुनःप्राप्ति)](../../../docs)
- [एजेन्टहरू र उपकरणहरू](../../../docs)
- [एजेन्टिक मोड्युल](../../../docs)
- [मोडेल सन्दर्भ प्रोटोकॉल (MCP)](../../../docs)
- [एजुर सेवाहरू](../../../docs)
- [परीक्षण र विकास](../../../docs)

कोर्सभर प्रयोग भएका शब्द र अवधारणाहरूको छिटो सन्दर्भ।

## मुख्य अवधारणाहरू

**AI Agent** - AI प्रयोग गरी स्वायत्त रूपमा तर्क गर्ने र क्रिया गर्ने प्रणाली। [Module 04](../04-tools/README.md)

**Chain** - श्रृंखला अपरेसनहरू जहाँ आउटपुट अर्को चरणमा फिड हुन्छ।

**Chunking** - दस्तावेजहरूलाई साना टुक्रामा विभाजन गर्ने। सामान्य: ३००-५०० टोकनहरू ओभरल्यापसहित। [Module 03](../03-rag/README.md)

**Context Window** - मोडेलले प्रक्रिया गर्न सक्ने अधिकतम टोकन संख्या। GPT-5.2: ४००K टोकन।

**Embeddings** - पाठको अर्थ जनाउने संख्यात्मक भेक्टरहरू। [Module 03](../03-rag/README.md)

**Function Calling** - मोडेलले बाह्य फंक्शनहरू कल गर्न संरचित अनुरोधहरू सिर्जना गर्छ। [Module 04](../04-tools/README.md)

**Hallucination** - मोडेलहरूले गलत तर सम्भावित देखिने जानकारी उत्पादन गर्दा।

**Prompt** - भाषा मोडेलमा टेक्स्ट इनपुट। [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - कीवर्ड होइन, embeddings प्रयोग गरी अर्थअनुसार खोज। [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: मेमोरी छैन। Stateful: संवाद इतिहास राख्छ। [Module 01](../01-introduction/README.md)

**Tokens** - मोडेलहरूले प्रक्रिया गर्ने आधारभूत टेक्स्ट युनिटहरू। लागत र सीमा निर्धारण गर्ने। [Module 01](../01-introduction/README.md)

**Tool Chaining** - उपकरणहरूको क्रमिक कार्यान्वयन जहाँ आउटपुटले अर्को कललाई सूचना दिन्छ। [Module 04](../04-tools/README.md)

## LangChain4j कम्पोनेन्टहरू

**AiServices** - प्रकार-सुरक्षित AI सेवा इन्टरफेसहरू सिर्जना गर्छ।

**OpenAiOfficialChatModel** - OpenAI र Azure OpenAI मोडेलहरूको एउटै क्लाइन्ट।

**OpenAiOfficialEmbeddingModel** - OpenAI Official क्लाइन्ट (OpenAI र Azure OpenAI दुवैलाई समर्थन) प्रयोग गरी embeddings सिर्जना गर्छ।

**ChatModel** - भाषा मोडेलहरूको मुख्य इन्टरफेस।

**ChatMemory** - संवाद इतिहास राख्छ।

**ContentRetriever** - RAG का लागि सान्दर्भिक दस्तावेज टुक्राहरू फेला पार्छ।

**DocumentSplitter** - दस्तावेजहरूलाई टुक्रामा विभाजन गर्छ।

**EmbeddingModel** - पाठलाई संख्यात्मक भेक्टरहरूमा रूपान्तरण गर्छ।

**EmbeddingStore** - embeddings स्टोर र पुनःप्राप्ति के लागि।

**MessageWindowChatMemory** - हालका सन्देशहरूको स्लाइडिङ विन्डो राख्छ।

**PromptTemplate** - {{variable}} प्लेसहोल्डरहरूसहित पुन: प्रयोग हुने प्रम्प्टहरू सिर्जना गर्छ।

**TextSegment** - मेटाडेटा सहितको टेक्स्ट तुकड़ा। RAG मा प्रयोग हुन्छ।

**ToolExecutionRequest** - उपकरण कार्यान्वयन अनुरोध प्रतिनिधित्व गर्छ।

**UserMessage / AiMessage / SystemMessage** - संवाद सन्देश प्रकारहरू।

## AI/ML अवधारणाहरू

**Few-Shot Learning** - प्रम्प्टमा उदाहरणहरू प्रदान गर्ने। [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - विशाल पाठ डाटामा प्रशिक्षित AI मोडेलहरू।

**Reasoning Effort** - GPT-5.2 को सोचाइ गहिराइ नियन्त्रण गर्ने प्यारामिटर। [Module 02](../02-prompt-engineering/README.md)

**Temperature** - आउटपुटको अनियमितता नियन्त्रण गर्छ। कम=निश्चित, उच्च=रचनात्मक।

**Vector Database** - embeddings का लागि विशेषीकृत डेटाबेस। [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - उदाहरण बिना कार्यहरू गर्ने। [Module 02](../02-prompt-engineering/README.md)

## गार्डरेलहरू - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - एप्लिकेसन-स्तर गार्डरेलहरू र प्रदायक सुरक्षा फिल्टरहरूको बहु-स्तरीय सुरक्षा दृष्टिकोण।

**Hard Block** - गम्भीर सामग्री उल्लङ्घनमा प्रदायक HTTP 400 त्रुटि फ्याँक्छ।

**InputGuardrail** - LangChain4j इन्टरफेस जसले LLM पुग्नु अगाडि प्रयोगकर्ता इनपुट मान्य गर्दछ। हानिकारक प्रम्प्टहरू रोक्दै लागत र ढिलोपन बचत गर्छ।

**InputGuardrailResult** - गार्डरेल मान्यताको रिटर्न प्रकार: `success()` वा `fatal("reason")`।

**OutputGuardrail** - AI प्रतिक्रियाहरू प्रयोगकर्तालाई फर्काउनु अघि मान्य गर्ने इन्टरफेस।

**Provider Safety Filters** - AI प्रदायकहरू (जस्तै GitHub Models) बाट निर्मित सामग्री फिल्टरहरू जसले API स्तरमा उल्लङ्घनहरू पत्ता लगाउँछन्।

**Soft Refusal** - मोडेलले शिष्टतापूर्वक जवाफ दिन अस्वीकार गर्छ, त्रुटि नफ्याँक्दै।

## प्रम्प्ट इन्जिनियरिङ - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - राम्रो सटीकताको लागि क्रमबद्ध तर्क।

**Constrained Output** - निश्चित ढाँचा वा संरचना लागू गर्ने।

**High Eagerness** - GPT-5.2 को गहिरो तर्कका लागि ढाँचा।

**Low Eagerness** - GPT-5.2 को छिटो जवाफका लागि ढाँचा।

**Multi-Turn Conversation** - पुनः पुनः सन्दर्भ कायम राख्ने।

**Role-Based Prompting** - प्रणाली सन्देशमार्फत मोडेललाई पात्र निर्धारण गर्ने।

**Self-Reflection** - मोडेलले आफ्नो आउटपुट मूल्याङ्कन र सुधार गर्ने।

**Structured Analysis** - निश्चित मूल्याङ्कन ढाँचा।

**Task Execution Pattern** - योजना → कार्यान्वयन → सारांश।

## RAG (सहायता गरिएको पुनःप्राप्ति) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - लोड → टुक्रा बनाउने → embed → भण्डारण।

**In-Memory Embedding Store** - परीक्षणका लागि अस्थायी भण्डारण।

**RAG** - पुनःप्राप्ति र उत्पादन संयोजन गरी जवाफ आधारभूत बनाउने।

**Similarity Score** - ०-१ सम्मको सार्थकता समानताका मापन।

**Source Reference** - प्राप्त सामग्रीको मेटाडेटा।

## एजेन्टहरू र उपकरणहरू - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Java विधिहरूलाई AI-कल योग्य उपकरणहरू भनेर चिन्हित गर्ने।

**ReAct Pattern** - तर्क → क्रिया → अवलोकन → दोहोर्याउने।

**Session Management** - विभिन्न प्रयोगकर्ताहरूका लागि अलग सन्दर्भहरू।

**Tool** - AI एजेन्टले कल गर्न सक्ने कार्य।

**Tool Description** - उपकरणको उद्देश्य र प्यारामिटरहरूको दस्तावेजीकरण।

## एजेन्टिक मोड्युल - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - इन्टरफेसहरूलाई AI एजेन्टहरू भनेर चिन्हित गर्दै व्यवहार घोषणा गर्ने।

**Agent Listener** - `beforeAgentInvocation()` र `afterAgentInvocation()` मार्फत एजेन्ट कार्यान्वयन अनुगमन गर्ने हुक।

**Agentic Scope** - साझा मेमोरी जहाँ एजेन्टहरूले आउटपुट राख्छन् र अन्य एजेन्टले उपभोग गर्छन् `outputKey` प्रयोग गरी।

**AgenticServices** - `agentBuilder()` र `supervisorBuilder()` प्रयोग गरी एजेन्टहरू निर्माण गर्ने कारखाना।

**Conditional Workflow** - सर्त अनुसार विभिन्न विशेषज्ञ एजेन्टहरूलाई मार्गनिर्देशन।

**Human-in-the-Loop** - मानव अनुमोदन वा समीक्षा थप्ने कार्यप्रवाह ढाँचा।

**langchain4j-agentic** - घोषणात्मक एजेन्ट निर्माणका लागि Maven निर्भरता (प्रयोगात्मक)।

**Loop Workflow** - सर्त पूरा नभएसम्म एजेन्ट दोहोर्याएर सञ्चालन गर्ने (जस्तै गुणस्तर स्कोर ≥ ०.८)।

**outputKey** - एजेन्टिक स्कोपमा परिणामहरू कहाँ भण्डारण गर्ने निर्धारण गर्ने एजेन्ट एनोटेसन प्यारामिटर।

**Parallel Workflow** - स्वतन्त्र कार्यहरूको लागि एकै साथ धेरै एजेन्टहरू चलाउने।

**Response Strategy** - सुपरवाइजरले अन्तिम उत्तर कसरी तयार पार्ने: LAST, SUMMARY, वा SCORED।

**Sequential Workflow** - एजेन्टहरूलाई क्रमबद्ध रूपमा कार्यान्वयन गर्ने जहाँ आउटपुट अर्को चरणसम्म जान्छ।

**Supervisor Agent Pattern** - सुपरवाइजर LLM ले कुन उप-एजेन्ट कल गर्ने निर्णय गर्ने उन्नत एजेन्टिक ढाँचा।

## मोडेल सन्दर्भ प्रोटोकॉल (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j मा MCP एकीकरणका लागि Maven निर्भरता।

**MCP** - मोडेल सन्दर्भ प्रोटोकॉल: AI अनुप्रयोगहरूलाई बाह्य उपकरणहरूसँग जोड्ने मानक। एक पटक बनाउनुहोस्, सबै ठाउँमा प्रयोग गर्नुहोस्।

**MCP Client** - उपकरणहरू पत्ता लगाउन र प्रयोग गर्न MCP सर्भरहरूमा जडान गर्ने अनुप्रयोग।

**MCP Server** - उपकरणहरू स्पष्ट विवरण र प्यारामिटर स्किमासहित MCP मार्फत सेवा गर्ने सेवा।

**McpToolProvider** - LangChain4j कम्पोनेन्ट जसले MCP उपकरणहरू AI सेवाहरू र एजेन्टहरूमा प्रयोग गर्न प्याकेज गर्छ।

**McpTransport** - MCP सञ्चारका लागि इन्टरफेस। कार्यान्वयनहरूमा Stdio र HTTP समावेश छन्।

**Stdio Transport** - stdin/stdout मार्फत स्थानीय प्रक्रिया सञ्चार। फाइलसिस्टम पहुँच वा कमाण्डलाइन उपकरणहरूका लागि उपयोगी।

**StdioMcpTransport** - LangChain4j कार्यान्वयन जसले MCP सर्भरलाई subprocess को रूपमा चलाउँछ।

**Tool Discovery** - क्लाइन्टले उपलब्ध उपकरणहरूको विवरण र स्किमासहित सर्भरसँग सोधपुछ गर्छ।

## एजुर सेवाहरू - [Module 01](../01-introduction/README.md)

**Azure AI Search** - भेक्टर क्षमतासहित क्लाउड खोज। [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - एजुर स्रोतहरू डेप्लोय गर्छ।

**Azure OpenAI** - Microsoft को एन्त्रप्राइज AI सेवा।

**Bicep** - एजुर पूर्वाधार-एस-कोड भाषा। [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - एजुरमा मोडेल डेप्लोयमेन्टको नाम।

**GPT-5.2** - reasoning नियन्त्रणसहितको नयाँतम OpenAI मोडेल। [Module 02](../02-prompt-engineering/README.md)

## परीक्षण र विकास - [Testing Guide](TESTING.md)

**Dev Container** - कन्टेनराइज्ड विकास वातावरण। [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - निःशुल्क AI मोडेल प्लेटफर्म। [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - इन-मेमोरी भण्डारणसँग परीक्षण।

**Integration Testing** - वास्तविक पूर्वाधारसँग परीक्षण।

**Maven** - Java बिल्ड अटोमेसन उपकरण।

**Mockito** - Java मोकीङ्ग फ्रेमवर्क।

**Spring Boot** - Java अनुप्रयोग फ्रेमवर्क। [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी सही अनुवादका लागि प्रयासरत भए तापनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा असंगतिहरू हुन सक्छन्। मूल दस्तावेज जुन यसको स्वदेशी भाषामा छ, त्यसलाई आधिकारिक स्रोत मानिनु पर्छ। महत्वपूर्ण जानकारीको लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यो अनुवाद प्रयोग गर्दा कुनै पनि गलत बुझाइ वा गलत व्याख्याका लागि हामी जिम्मेवार हुने छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->