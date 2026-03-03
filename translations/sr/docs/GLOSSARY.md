# LangChain4j Речник

## Садржај

- [Основни појмови](../../../docs)
- [LangChain4j Компоненте](../../../docs)
- [AI/ML Концепти](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agents and Tools](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Services](../../../docs)
- [Testing and Development](../../../docs)

Брза референца за термине и појмове коришћене кроз цео курс.

## Основни појмови

**AI Agent** - Систем који користи вештачку интелигенцију за аутономно резоновање и деловање. [Модул 04](../04-tools/README.md)

**Chain** - Низ операција где излаз служи као улаз за следећи корак.

**Chunking** - Разбијање докумената у мање делове. Типично: 300–500 токена са преклапањем. [Модул 03](../03-rag/README.md)

**Context Window** - Максималан број токена које модел може обрадити. GPT-5.2: 400К токена (до 272К улаз, 128К излаз).

**Embeddings** - Нумерички вектори који представљају значење текста. [Модул 03](../03-rag/README.md)

**Function Calling** - Модел генерише структуиране захтеве за позивање спољних функција. [Модул 04](../04-tools/README.md)

**Hallucination** - Када модели генеришу нетачне али вероватне информације.

**Prompt** - Текстуални улаз за језички модел. [Модул 02](../02-prompt-engineering/README.md)

**Semantic Search** - Претрага по значењу уз помоћ embeddings, а не по кључним речима. [Модул 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: без меморије. Stateful: чува историју разговора. [Модул 01](../01-introduction/README.md)

**Tokens** - Основне текстуалне јединице које модели обрађују. Утичу на трошкове и лимите. [Модул 01](../01-introduction/README.md)

**Tool Chaining** - Низкупно извршавање алата где излаз обавештава следећи позив. [Модул 04](../04-tools/README.md)

## LangChain4j Компоненте

**AiServices** - Креира тип-безбедне интерфејсе за AI сервисе.

**OpenAiOfficialChatModel** - Уједињени клијент за OpenAI и Azure OpenAI моделе.

**OpenAiOfficialEmbeddingModel** - Креира embeddings користећи OpenAI Official клијента (подржава и OpenAI и Azure OpenAI).

**ChatModel** - Основни интерфејс за језичке моделе.

**ChatMemory** - Чува историју разговора.

**ContentRetriever** - Проналази релевантне делове докумената за RAG.

**DocumentSplitter** - Разбија документе у делове.

**EmbeddingModel** - Претвара текст у нумеричке векторе.

**EmbeddingStore** - Чува и преузима embeddings.

**MessageWindowChatMemory** - Чува покретни прозор недавно размењених порука.

**PromptTemplate** - Креира поновно употребљиве инструкције са `{{variable}}` променљивим местима.

**TextSegment** - Текстуални део са метаподацима. Користи се у RAG.

**ToolExecutionRequest** - Представља захтев за извршење алата.

**UserMessage / AiMessage / SystemMessage** - Типови порука у разговору.

## AI/ML Концепти

**Few-Shot Learning** - Даје примере у упутствима. [Модул 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI модели обучени на огромним количинама текстуалних података.

**Reasoning Effort** - Параметар GPT-5.2 за контролу дубине размишљања. [Модул 02](../02-prompt-engineering/README.md)

**Temperature** - Контролише насумичност излаза. Низак = детерминистички, висок = креативан.

**Vector Database** - Специјализована база података за embeddings. [Модул 03](../03-rag/README.md)

**Zero-Shot Learning** - Извођење задатака без примера. [Модул 02](../02-prompt-engineering/README.md)

## Guardrails - [Модул 00](../00-quick-start/README.md)

**Defense in Depth** - Мулти-слојни приступ безбедности који комбинује заштитне мере на нивоу апликације са безбедносним филтерима провајдера.

**Hard Block** - Провајдер баца HTTP 400 грешку за озбиљне прекршаје садржаја.

**InputGuardrail** - LangChain4j интерфејс за валидацију корисничких уноса пре него што дођу до LLM-а. Штеди трошкове и кашњење блокирајући штетне упите рано.

**InputGuardrailResult** - Тип повратне вредности за валидацију guardrail-а: `success()` или `fatal("reason")`.

**OutputGuardrail** - Интерфејс за валидацију AI одговора пре враћања корисницима.

**Provider Safety Filters** - Уграђени филтери садржаја AI провајдера (нпр. GitHub Models) који спречавају прекршаје на нивоу API-а.

**Soft Refusal** - Модел пристојно одбија да одговори без бацања грешке.

## Prompt Engineering - [Модул 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Корак-по-корак резоновање за бољу прецизност.

**Constrained Output** - Приморавање специфичног формата или структуре.

**High Eagerness** - GPT-5.2 шаблон за темељно размишљање.

**Low Eagerness** - GPT-5.2 шаблон за брзе одговоре.

**Multi-Turn Conversation** - Одржавање контекста кроз размене.

**Role-Based Prompting** - Постављање персоне модела путем системских порука.

**Self-Reflection** - Модел процењује и унапређује свој излаз.

**Structured Analysis** - Фиксирани оквир за процену.

**Task Execution Pattern** - Планирај → Изврши → Сажми.

## RAG (Retrieval-Augmented Generation) - [Модул 03](../03-rag/README.md)

**Document Processing Pipeline** - Учитавање → раздвајање на делове → уграђивање → складиштење.

**In-Memory Embedding Store** - Неперзистентно складиштење за тестирање.

**RAG** - Комбинује претрагу са генерацијом за утемељене одговоре.

**Similarity Score** - Мера (0-1) семантичке сличности.

**Source Reference** - Мета-подаци о пронађеном садржају.

## Agents and Tools - [Модул 04](../04-tools/README.md)

**@Tool Annotation** - Означава Java методе као алате позивне од стране AI-а.

**ReAct Pattern** - Резонуј → Дели → Опажај → Понови.

**Session Management** - Одвојени контексти за различите кориснике.

**Tool** - Функција коју AI агент може позвати.

**Tool Description** - Документација о сврси алатке и параметрима.

## Agentic Module - [Модул 05](../05-mcp/README.md)

**@Agent Annotation** - Означава интерфејсе као AI агенте са декларативним дефинисањем понашања.

**Agent Listener** - Hook за праћење извршења агента преко `beforeAgentInvocation()` и `afterAgentInvocation()`.

**Agentic Scope** - Заједничка меморија где агенти чувају излазе користећи `outputKey` да би их други агенти користили.

**AgenticServices** - Фабрика за креирање агената користећи `agentBuilder()` и `supervisorBuilder()`.

**Conditional Workflow** - Рутација заснована на условима ка различитим специјалистичким агентима.

**Human-in-the-Loop** - Образац радног тока који додаје људске контролне тачке за одобрење или ревизију садржаја.

**langchain4j-agentic** - Maven зависност за декларативно грађење агената (експериментално).

**Loop Workflow** - Понављање извршења агента док се не испуни услов (нпр. квалитет ≥ 0.8).

**outputKey** - Параметар анотације агента који одређује где се резултати чувају у Agentic Scope.

**Parallel Workflow** - Паралелно извођење више агената за независне задатке.

**Response Strategy** - Како супервизор формулише коначни одговор: LAST, SUMMARY или SCORED.

**Sequential Workflow** - Извршавање агената по реду где излаз тече у следећи корак.

**Supervisor Agent Pattern** - Напредни агентски образац где супервизорски LLM динамички одлучује које под-агенте позвати.

## Model Context Protocol (MCP) - [Модул 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven зависност за MCP интеграцију у LangChain4j.

**MCP** - Model Context Protocol: стандард за повезивање AI апликација са спољним алатима. Направи једном, користи свуда.

**MCP Client** - Апликација која се повезује на MCP сервере да открије и користи алате.

**MCP Server** - Сервис који преко MCP изложи алате са јасним описима и шемама параметара.

**McpToolProvider** - LangChain4j компонента која омотава MCP алате за коришћење у AI сервисима и агентима.

**McpTransport** - Интерфејс за MCP комуникацију. Имплементације укључују Stdio и HTTP.

**Stdio Transport** - Локални транспорт процесом преко stdin/stdout. Корисно за приступ фајл системима или командно-линијским алатима.

**StdioMcpTransport** - LangChain4j имплементација која покреће MCP сервер као подпроцес.

**Tool Discovery** - Клијент пита сервер за доступне алате са описима и шемама.

## Azure Services - [Модул 01](../01-introduction/README.md)

**Azure AI Search** - Облачна претрага са векторским могућностима. [Модул 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Делокидање Azure ресурса.

**Azure OpenAI** - Microsoft-ов ентерприс AI сервис.

**Bicep** - Језик за инфраструктуру као код за Azure. [Водич за инфраструктуру](../01-introduction/infra/README.md)

**Deployment Name** - Назив за деплој модела у Azure.

**GPT-5.2** - Најновији OpenAI модел са контролом резоновања. [Модул 02](../02-prompt-engineering/README.md)

## Testing and Development - [Водич за тестирање](TESTING.md)

**Dev Container** - Контејнеризовано развојно окружење. [Конфигурација](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Бесплатна AI платформа за моделе. [Модул 00](../00-quick-start/README.md)

**In-Memory Testing** - Тестирање са унутрашњом меморијом.

**Integration Testing** - Тестирање са правом инфраструктуром.

**Maven** - Алат за аутоматизацију Java изградње.

**Mockito** - Java фрејмворк за имитацију.

**Spring Boot** - Java фрејмворк за апликације. [Модул 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:
Овај документ је преведен помоћу АИ сервиса за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде прецизан, имајте у виду да аутоматизовани преводи могу садржати грешке или нетачности. Изворни документ на његовом оригиналном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешне тумачења која могу произаћи из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->