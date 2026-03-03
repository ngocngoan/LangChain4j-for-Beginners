# Модул 00: Брзи почетак

## Садржај

- [Увод](../../../00-quick-start)
- [Шта је LangChain4j?](../../../00-quick-start)
- [Зависности LangChain4j](../../../00-quick-start)
- [Предуслови](../../../00-quick-start)
- [Подешавање](../../../00-quick-start)
  - [1. Узмите свој GitHub токен](../../../00-quick-start)
  - [2. Поставите свој токен](../../../00-quick-start)
- [Покрените примере](../../../00-quick-start)
  - [1. Основни ћаскање](../../../00-quick-start)
  - [2. Обрасци за упите](../../../00-quick-start)
  - [3. Позивање функција](../../../00-quick-start)
  - [4. Питања и одговори на документима (Easy RAG)](../../../00-quick-start)
  - [5. Одговорни AI](../../../00-quick-start)
- [Шта сваки пример показује](../../../00-quick-start)
- [Следећи кораци](../../../00-quick-start)
- [Решавање проблема](../../../00-quick-start)

## Увод

Овај брзи почетак је намењен да вас што брже покрене са LangChain4j. Обухвата основе прављења AI апликација са LangChain4j и GitHub моделима. У наредним модулима прелазите на Azure OpenAI и GPT-5.2 и дубље се бавите сваким концептом.

## Шта је LangChain4j?

LangChain4j је Java библиотека која поједностављује прављење апликација са вештачком интелигенцијом. Уместо да се бавите HTTP клијентима и парсирањем JSON-а, радите са чистим Java API-јима.

„Ланац“ у LangChain означава повезивање више компоненти заједно – можете повезати упит са моделом, затим са парсером, или повезати више AI позива где резултат једног иде као улаз за други. Овај брзи почетак је усредсређен на основе пре него што истражите сложеније ланце.

<img src="../../../translated_images/sr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j концепт ланца" width="800"/>

*Повезивање компоненти у LangChain4j – градивни блокови се спајају да створе моћне AI токове рада*

Користићемо три основне компоненте:

**ChatModel** – Интерфејс за интеракцију са AI моделом. Позовете `model.chat("промпт")` и добијете одговор у виду низа. Користимо `OpenAiOfficialChatModel` који ради са OpenAI-компатибилним крајњим тачкама као што су GitHub модели.

**AiServices** – Креира типско безбедне AI сервисне интерфејсе. Дефинишете методе, означите их са `@Tool`, и LangChain4j се бави оркестрацијом. AI аутоматски позива ваше Java методе кад је потребно.

**MessageWindowChatMemory** – Одржава историју разговора. Без ове компоненте сваки захтев је независан. Са њом, AI памти претходне поруке и одржава контекст кроз више корака.

<img src="../../../translated_images/sr/architecture.eedc993a1c576839.webp" alt="Архитектура LangChain4j" width="800"/>

*Архитектура LangChain4j – основне компоненте које раде заједно да покрећу ваше AI апликације*

## Зависности LangChain4j

Овај брзи почетак користи три Maven зависности у [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Модул `langchain4j-open-ai-official` обезбеђује класу `OpenAiOfficialChatModel` која се повезује на OpenAI-компатибилне API-је. GitHub модели користе исти формат API-ја, тако да није потребан посебан адаптер – само усмерите базну URL адресу на `https://models.github.ai/inference`.

Модул `langchain4j-easy-rag` обезбеђује аутоматско раздвајање, уграђивање и претрагу докумената, тако да можете изградити RAG апликације без ручног конфигурисања сваког корака.

## Предуслови

**Користите ли Dev Container?** Јава и Maven су већ инсталирани. Потребан вам је само GitHub Personal Access Token.

**Локални развој:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (упутство доле)

> **Напомена:** Овај модул користи `gpt-4.1-nano` из GitHub модела. Немојте мењати име модела у коду – конфигурисан је да ради са расположивим моделима GitHub-а.

## Подешавање

### 1. Узмите свој GitHub токен

1. Идите на [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Кликните на "Generate new token"
3. Поставите описно име (нпр. "LangChain4j Demo")
4. Поставите рок важности (препоручује се 7 дана)
5. Под „Account permissions“ пронађите „Models“ и подесите на „Read-only“
6. Кликните на "Generate token"
7. Копирајте и сачувајте токен – више га нећете видети

### 2. Поставите свој токен

**Опција 1: Користећи VS Code (препоручено)**

Ако користите VS Code, додајте свој токен у `.env` фајл у корену пројекта:

Ако `.env` фајл не постоји, копирајте `.env.example` у `.env` или направите нови `.env` фајл у корену пројекта.

**Пример `.env` фајла:**
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Онда можете једноставно десним кликом на било који демо фајл (нпр. `BasicChatDemo.java`) у Explorer-у и одабрати **"Run Java"** или користити конфигурације за покретање из Run and Debug панела.

**Опција 2: Користећи терминал**

Поставите токен као променљиву окружења:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Покрените примере

**Користећи VS Code:** Једноставно десним кликом на било који демо фајл у Explorer-у одаберите **"Run Java"** или користите конфигурације из Run and Debug панела (обавезно сте претходно додали токен у `.env`).

**Користећи Maven:** Алтернативно, можете покренути из командне линије:

### 1. Основни ћаскање

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Обрасци за упите

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Приказује zero-shot, few-shot, chain-of-thought и упите засноване на улогама.

### 3. Позивање функција

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI аутоматски позива ваше Java методе кад је потребно.

### 4. Питања и одговори на документима (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Постављајте питања о вашим документима користећи Easy RAG са аутоматским уграђивањем и претрагом.

### 5. Одговорни AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Погледајте како AI филтери за безбедност блокирају штетни садржај.

## Шта сваки пример показује

**Основни ћаскање** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Започните овде да видите LangChain4j у најједноставнијој форми. Креираћете `OpenAiOfficialChatModel`, послаћете упит `.chat()` и добити одговор. Ово показује темеље: како инициализовати моделе са прилагођеним крајњим тачкама и API кључевима. Када разумете овај образац, све остало се гради на томе.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Ćаскањем:** Отворите [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и питајте:
> - "Како бих пребацио са GitHub модела на Azure OpenAI у овом коду?"
> - "Које друге параметре могу да конфигуришем у OpenAiOfficialChatModel.builder()?"
> - "Како да додам стриминг одговоре уместо да чекам комплетан одговор?"

**Инжењеринг упита** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сад када знате како да разговарате са моделом, истражимо шта му говорите. Овај демо користи исту конфигурацију модела али показује пет различитих образаца упита. Испробајте zero-shot упите за директне инструкције, few-shot упите који уче из примера, chain-of-thought упите који откривају кораке резоновања, и упите засноване на улогама који постављају контекст. Видео ћете како исти модел даје драстично различите резултате у зависности од начина формулације захтева.

Демо такође приказује предлошке за упите, који су моћан начин да направите поновљиве упите са променљивима.
Испод је пример упита користећи LangChain4j `PromptTemplate` за попуњавање променљивих. AI ће одговорити на основу дате дестинације и активности.

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

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Ćаскањем:** Отворите [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и питајте:
> - "Која је разлика између zero-shot и few-shot упита и када треба користити који?"
> - "Како параметар temperature утиче на одговоре модела?"
> - "Које су неке технике за спречавање напада уписивањем упита (prompt injection) у продукцији?"
> - "Како да направим поновљиве PromptTemplate објекте за честе обрасце?"

**Интеграција алата** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Овде LangChain4j постаје моћан. Користићете `AiServices` да направите AI асистента који може позивати ваше Java методе. Само означите методе са `@Tool("опис")` и LangChain4j се бави осталим – AI аутоматски одлучује када ће користити који алат на основу онога што корисник тражи. Ово приказује позивање функција, кључну технику за прављење AI који може извршавати акције, а не само одговарати на питања.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Ćаскањем:** Отворите [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и питајте:
> - "Како ради @Tool анотација и шта LangChain4j ради са њом иза сцене?"
> - "Може ли AI позивати више алата узастопно да реши сложене проблеме?"
> - "Шта се дешава ако алат баци изузетак – како треба да руковам грешкама?"
> - "Како бих интегрисао прави API уместо овог примера калкулатора?"

**Питања и одговори на документима (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Овде ћете видети RAG (retrieval-augmented generation) користећи LangChain4j приступ "Easy RAG". Документи се учитавају, аутоматски раздвајају и уграђују у меморијску базу, затим претраживач садржаја доставља релевантне делове AI-ју у тренутку упита. AI одговара на основу ваших докумената, не своје опште базе знања.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Ćаскањем:** Отворите [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и питајте:
> - "Како RAG спречава AI халуцинације у поређењу са коришћењем података о обучавању модела?"
> - "Која је разлика између овог једноставног приступа и прилагођеног RAG процеса?"
> - "Како бих ово скалирао за рад са више докумената или већим базама знања?"

**Одговорни AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Градите AI безбедност са одбраном у дубини. Овај демо приказује два слоја заштите који раде заједно:

**Део 1: LangChain4j Guardrails за унос** – Блокира опасне упите пре него што дођу до LLM-а. Креирајте прилагођене грејдрајлове који проверавају забрањене кључне речи или обрасце. Они раде у вашем коду, тако да су брзи и бесплатни.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Део 2: Филтери безбедности провајдера** – GitHub модели имају уграђене филтере који хватају оно што ваши грејдрајлови пропусте. Видећете оштре блокаде (HTTP 400 грешке) за озбиљне прекршаје и благе одбијања где AI љубазно одбија.

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Ćаскањем:** Отворите [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и питајте:
> - "Шта је InputGuardrail и како направити свој?"
> - "Која је разлика између оштре блокаде и блажег одбијања?"
> - "Зашто користити и грејдрајлове и филтере провајдера заједно?"

## Следећи кораци

**Следећи модул:** [01-introduction - Почетак рада са LangChain4j](../01-introduction/README.md)

---

**Навигација:** [← Назад на Главну](../README.md) | [Следеће: Модул 01 - Увод →](../01-introduction/README.md)

---

## Решавање проблема

### Прва Maven компилација

**Проблем:** Почетни `mvn clean compile` или `mvn package` траје дуго (10-15 минута)

**Узрок:** Maven мора да преузме све зависности пројекта (Spring Boot, LangChain4j библиотеке, Azure SDK итд.) при првој компилацији.

**Решење:** Ово је нормално понашање. Наредне компилације ће бити много брже јер се зависности кеширају локално. Време преузимања зависи од брзине ваше мреже.

### Синтакса Maven команди у PowerShell-у

**Проблем:** Maven команде не успевају са грешком `Unknown lifecycle phase ".mainClass=..."`
**Узрок**: PowerShell тумачи `=` као оператор додељивања вредности променљивој, што нарушава Maven синтаксу својстава

**Решење**: Користите оператор за заустављање парсирања `--%` пре Maven команде:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` каже PowerShell-у да све остале аргументе проследи Maven-у буквално, без тумачења.

### Приказ емоџија у Windows PowerShell

**Проблем**: AI одговори приказују бесмислене знакове (нпр. `????` или `â??`) уместо емоџија у PowerShell-у

**Узрок**: Подразумевани енкодирање PowerShell-а не подржава UTF-8 емоџије

**Решење**: Покрените ову команду пре извршавања Java апликација:
```cmd
chcp 65001
```

Ово приморава UTF-8 енкодирање у терминалу. Алтернативно, користите Windows Terminal који има бољу подршку за Unicode.

### Дебаговање API позива

**Проблем**: Грешке при аутентификацији, ограничења брзине или неочекивани одговори од AI модела

**Решење**: Примери укључују `.logRequests(true)` и `.logResponses(true)` да би приказали API позиве у конзоли. Ово помаже при решавању проблема са аутентификацијом, ограничењима брзине или неочекиваним одговорима. Уклоните ове заставице у продукцији да бисте смањили буку у логовима.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:  
Овај документ је преведен помоћу AI услуге за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако настојимо да превод буде тачан, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални превод од стране људског стручњака. Не преузимамо одговорност за било каква неспоразума или погрешна тумачења која проистекну из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->