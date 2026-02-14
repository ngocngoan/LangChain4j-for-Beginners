# LangChain4j Речник

## Садржај

- [Основни појмови](../../../docs)
- [Компоненте LangChain4j](../../../docs)
- [AI/ML појмови](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Агенти и алати](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure услуге](../../../docs)
- [Тестирање и развој](../../../docs)

Брз преглед термина и појмова коришћених кроз целокупни курс.

## Основни појмови

**AI Agent** - Систем који користи вештачку интелигенцију за размишљање и аутономно деловање. [Модул 04](../04-tools/README.md)

**Chain** - Низ операција где излаз улази у следећи корак.

**Chunking** - Расцепка докумената на мање делове. Типично: 300-500 токена са преклапањем. [Модул 03](../03-rag/README.md)

**Context Window** - Максималан број токена које модел може да обради. GPT-5.2: 400K токена.

**Embeddings** - Нумерички вектори који представљају значење текста. [Модул 03](../03-rag/README.md)

**Function Calling** - Модел генерише структуиране захтеве за позив спољашњих функција. [Модул 04](../04-tools/README.md)

**Hallucination** - Када модели генеришу нетачне али вероватне информације.

**Prompt** - Текстуални унос за језички модел. [Модул 02](../02-prompt-engineering/README.md)

**Semantic Search** - Претрага по значењу користећи ембединг, а не кључне речи. [Модул 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: без меморије. Stateful: одржава историју разговора. [Модул 01](../01-introduction/README.md)

**Tokens** - Основне јединице текста које модели обрађују. Утичу на трошкове и лимите. [Модул 01](../01-introduction/README.md)

**Tool Chaining** - Секвенцијално извршавање алата где излаз утиче на следећи позив. [Модул 04](../04-tools/README.md)

## Компоненте LangChain4j

**AiServices** - Креира тип-безбедне интерфејсе AI услуга.

**OpenAiOfficialChatModel** - Уједињени клијент за OpenAI и Azure OpenAI моделе.

**OpenAiOfficialEmbeddingModel** - Креира ембединг користећи OpenAI Official клијента (подржава OpenAI и Azure OpenAI).

**ChatModel** - Основни интерфејс за језичке моделе.

**ChatMemory** - Одржава историју разговора.

**ContentRetriever** - Проналази релевантне делове докумената за RAG.

**DocumentSplitter** - Делује документе на делове.

**EmbeddingModel** - Претвара текст у нумеричке векторе.

**EmbeddingStore** - Чува и преузима ембединг.

**MessageWindowChatMemory** - Одржава клизни прозор недавних порука.

**PromptTemplate** - Креира поновно употребљиве упите са `{{variable}}` место држачима.

**TextSegment** - Текстуални део са метаподацима. Коришћен у RAG.

**ToolExecutionRequest** - Представља захтев за извршавање алата.

**UserMessage / AiMessage / SystemMessage** - Типови порука у разговору.

## AI/ML појмови

**Few-Shot Learning** - Пружaње примера у упитима. [Модул 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI модели обучени на великој количини текста.

**Reasoning Effort** - Параметар GPT-5.2 који контролише дубину размишљања. [Модул 02](../02-prompt-engineering/README.md)

**Temperature** - Контролише случајност излаза. Ниско=детерминистичко, високо=креативно.

**Vector Database** - Специјализована база за ембединг. [Модул 03](../03-rag/README.md)

**Zero-Shot Learning** - Извођење задатака без примера. [Модул 02](../02-prompt-engineering/README.md)

## Guardrails - [Модул 00](../00-quick-start/README.md)

**Defense in Depth** - Вишеслојни безбедносни приступ који комбинује контроле на нивоу апликације са безбедносним филтрима провајдера.

**Hard Block** - Провајдер враћа HTTP 400 грешку за тешке повреде садржаја.

**InputGuardrail** - LangChain4j интерфејс за валидацију уноса корисника пре него што стигне до LLM-а. Штеди трошкове и латенцију блокирајући штетне упите у раној фази.

**InputGuardrailResult** - Тип повратне вредности за валидацију guardrail-а: `success()` или `fatal("reason")`.

**OutputGuardrail** - Интерфејс за валидацију AI одговора пре него што се пошаљу корисницима.

**Provider Safety Filters** - Уграђени филтри садржаја од AI провајдера (нпр. GitHub Models) који хватају повреде на API нивоу.

**Soft Refusal** - Модел љубазно одбија да одговори без избацивања грешке.

## Prompt Engineering - [Модул 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Размишљање корак по корак за бољу тачност.

**Constrained Output** - Присиљавање на одређени формат или структуру.

**High Eagerness** - Патерн GPT-5.2 за темељно размишљање.

**Low Eagerness** - Патерн GPT-5.2 за брзе одговоре.

**Multi-Turn Conversation** - Одржавање контекста кроз размене.

**Role-Based Prompting** - Постављање персоне модела путем системских порука.

**Self-Reflection** - Модел процењује и унапређује свој излаз.

**Structured Analysis** - Фиксиран оквир за евалуацију.

**Task Execution Pattern** - Планирај → Изврши → Сажми.

## RAG (Retrieval-Augmented Generation) - [Модул 03](../03-rag/README.md)

**Document Processing Pipeline** - Учитај → расцепи → ембедуј → чувај.

**In-Memory Embedding Store** - Неупорна меморија за тестирање.

**RAG** - Комбинује претрагу и генерацију за проверене одговоре.

**Similarity Score** - Мера (0-1) семантичке сличности.

**Source Reference** - Метаподаци о пронађеном садржају.

## Агенти и алати - [Модул 04](../04-tools/README.md)

**@Tool Annotation** - Означава Java методе као AI позивне алате.

**ReAct Pattern** - Размишљај → Делуј → Посматрај → Понови.

**Session Management** - Одвојени контексти за различите кориснике.

**Tool** - Функција коју AI агент може да позове.

**Tool Description** - Документација о сврси и параметрима алата.

## Agentic Module - [Модул 05](../05-mcp/README.md)

**@Agent Annotation** - Означава интерфејсе као AI агенте са декларативним дефинисањем понашања.

**Agent Listener** - Хук за праћење извршавања агента преко `beforeAgentInvocation()` и `afterAgentInvocation()`.

**Agentic Scope** - Заједничка меморија где агенти чувају излазе користећи `outputKey` за потрошњу од стране других агената.

**AgenticServices** - Фабрика за креирање агената користећи `agentBuilder()` и `supervisorBuilder()`.

**Conditional Workflow** - Рута заснована на условима ка различитим специјалистичким агентима.

**Human-in-the-Loop** - Образац рада који додаје људске контролне тачке за одобрење или преглед садржаја.

**langchain4j-agentic** - Maven зависност за декларативну изградњу агената (експериментално).

**Loop Workflow** - Итеративно извршавање агента док се не испуни услов (нпр. оценa квалитета ≥ 0.8).

**outputKey** - Параметар у annotaciji агента који одређује где се резултати чувају у Agentic Scope.

**Parallel Workflow** - Покретање више агената истовремено за независне задатке.

**Response Strategy** - Како супервизор формулише коначан одговор: LAST, SUMMARY, или SCORED.

**Sequential Workflow** - Извршавање агената по реду где излаз тече у следећи корак.

**Supervisor Agent Pattern** - Напредан agentic шаблон где супервизор LLM динамички одлучује које под-агенте ће позвати.

## Model Context Protocol (MCP) - [Модул 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven зависност за MCP интеграцију у LangChain4j.

**MCP** - Model Context Protocol: стандард за повезивање AI апликација са спољашњим алатима. Направи једном, користи свуда.

**MCP Client** - Апликација која се спаја на MCP сервере да открије и користи алате.

**MCP Server** - Сервис који изложи алате преко MCP уз јасне описе и шеме параметара.

**McpToolProvider** - LangChain4j компонента која омотава MCP алате за употребу у AI услугама и агентима.

**McpTransport** - Интерфејс за MCP комуникацију. Имплементације укључују Stdio и HTTP.

**Stdio Transport** - Локални процесни транспорт преко stdin/stdout. Корисно за приступ фајл систему или командну линију.

**StdioMcpTransport** - LangChain4j имплементација која покреће MCP сервер као подпроцес.

**Tool Discovery** - Клијент пита сервер за расположиве алате са описима и шемама.

## Azure услуге - [Модул 01](../01-introduction/README.md)

**Azure AI Search** - Облачна претрага са могућностима вектора. [Модул 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Деплојмент Azure ресурса.

**Azure OpenAI** - Microsoft-ова ентерпрајз AI услуга.

**Bicep** - Azure језик за инфраструктуру као код. [Инфраструктурни водич](../01-introduction/infra/README.md)

**Deployment Name** - Име за распоређивање модела у Azure.

**GPT-5.2** - Најновији OpenAI модел са контролом размишљања. [Модул 02](../02-prompt-engineering/README.md)

## Тестирање и развој - [Водич за тестирање](TESTING.md)

**Dev Container** - Контејнеризовано развојно окружење. [Конфигурација](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Бесплатно AI игралиште модела. [Модул 00](../00-quick-start/README.md)

**In-Memory Testing** - Тестирање са меморијским складиштем.

**Integration Testing** - Тестирање са стварном инфраструктуром.

**Maven** - Java алатка за аутоматизацију изградње.

**Mockito** - Java фрејмворк за моковање.

**Spring Boot** - Java оквир за апликације. [Модул 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ограничење одговорности**:  
Овај документ је преведен помоћу АИ сервиса за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде тачан, молимо имајте у виду да аутоматизиовани преводи могу садржати грешке или нетачности. Оригинални документ на његовој изворној језику треба сматрати овлашћеним извором. За критичне информације препоручује се професионални људски превод. Не сносимо одговорност за било какве неспоразуме или погрешна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->