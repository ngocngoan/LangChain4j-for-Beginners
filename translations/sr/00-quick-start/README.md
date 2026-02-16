# Модул 00: Брз почетак

## Садржај

- [Увод](../../../00-quick-start)
- [Шта је LangChain4j?](../../../00-quick-start)
- [Зависности LangChain4j](../../../00-quick-start)
- [Претпоставке](../../../00-quick-start)
- [Подешавање](../../../00-quick-start)
  - [1. Набавите свој GitHub токен](../../../00-quick-start)
  - [2. Поставите свој токен](../../../00-quick-start)
- [Покретање примера](../../../00-quick-start)
  - [1. Основни ћаскање](../../../00-quick-start)
  - [2. Обрасци упита](../../../00-quick-start)
  - [3. Позивање функција](../../../00-quick-start)
  - [4. Питања и одговори о документима (RAG)](../../../00-quick-start)
  - [5. Одговорни AI](../../../00-quick-start)
- [Шта сваки пример показује](../../../00-quick-start)
- [Следећи кораци](../../../00-quick-start)
- [Решавање проблема](../../../00-quick-start)

## Увод

Овај брзи почетак има за циљ да вам омогући да што брже започнете рад са LangChain4j. Обухвата апсолутне основе изградње AI апликација помоћу LangChain4j и GitHub модела. У наредним модулима ћете користити Azure OpenAI са LangChain4j да градите напредније апликације.

## Шта је LangChain4j?

LangChain4j је Java библиотека која поједностављује израду апликација које користе вештачку интелигенцију. Уместо да радите са HTTP клијентима и парсирањем JSON-а, радићете са чистим Java API-јима.

„Ланац“ у LangChain означава повезивање више компоненти - можете повезати упит са моделом и са парсером, или повезати више AI позива где излаз једног иде као улаз у други. Овај брзи старт се фокусира на основе пре него што истражи сложеније ланце.

<img src="../../../translated_images/sr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Повезивање компоненти у LangChain4j - блокови који се спајају да створе моћне AI радне токове*

Узимаћемо у обзир три основне компоненте:

**ChatLanguageModel** - Интерфејс за интеракцију са AI моделима. Позовите `model.chat("упит")` и добијете одговор као текст. Користимо `OpenAiOfficialChatModel` који ради са OpenAI-компатибилним крајњим тачкама као што су GitHub модели.

**AiServices** - Креира тип-безбедне AI сервис интерфејсе. Дефинишете методе, означите их са `@Tool`, а LangChain4j се брине о оркестрацији. AI аутоматски позива ваше Java методе када је потребно.

**MessageWindowChatMemory** - Чува историју разговора. Без ње, сваки захтев је независан. Са њом, AI памти претходне поруке и одржава контекст током више корака.

<img src="../../../translated_images/sr/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура LangChain4j - основне компоненте које заједно омогућавају рад ваших AI апликација*

## Зависности LangChain4j

Овај брзи старт користи две Maven зависности у [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Модул `langchain4j-open-ai-official` пружа класу `OpenAiOfficialChatModel` која се повезује са OpenAI-компатибилним API-јима. GitHub модели користе исти формат API-ја, па није потребан посебан адаптер - само усмерите основну URL адресу на `https://models.github.ai/inference`.

## Претпоставке

**Користите ли развојни контејнер?** Java и Maven су већ инсталирани. Потребан вам је само GitHub Personal Access Token.

**Локални развој:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (упутства у наставку)

> **Напомена:** Овај модул користи `gpt-4.1-nano` из GitHub модела. Немојте мењати име модела у коду - конфигурисано је да ради са доступним GitHub моделима.

## Подешавање

### 1. Набавите свој GitHub токен

1. Идите на [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Кликните „Generate new token“
3. Поставите описно име (нпр. "LangChain4j Demo")
4. Поставите рок важења (препоручено 7 дана)
5. У одељку "Account permissions", пронађите "Models" и подесите на „Read-only“
6. Кликните „Generate token“
7. Копирајте и сачувајте свој токен - нећете га поново видети

### 2. Поставите свој токен

**Опција 1: Користећи VS Code (препоручено)**

Ако користите VS Code, додајте свој токен у `.env` фајл у корену пројекта:

Ако `.env` фајл не постоји, копирајте `.env.example` у `.env` или креирајте нови `.env` фајл у корену пројекта.

**Пример `.env` фајла:**
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Онда можете једноставно десним кликом на било који демо фајл (нпр. `BasicChatDemo.java`) у Експлореру изабрати **"Run Java"**, или користити конфигурације за покретање из панела Run and Debug.

**Опција 2: Користећи терминал**

Поставите токен као системску променљиву:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Покретање примера

**Користећи VS Code:** Једноставно десним кликом на било који демо фајл у Експлореру изаберите **"Run Java"**, или користите конфигурације за покретање из панела Run and Debug (пре тога уверите се да сте додали свој токен у `.env` фајл).

**Користећи Maven:** Алтернативно, можете покренути преко командне линије:

### 1. Основни ћаскање

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Обрасци упита

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Приказује zero-shot, few-shot, chain-of-thought и role-based упите.

### 3. Позивање функција

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI аутоматски позива ваше Java методе када је потребно.

### 4. Питања и одговори о документима (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Постављајте питања о садржају у `document.txt`.

### 5. Одговорни AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Погледајте како AI сигурносни филтери блокирају штетан садржај.

## Шта сваки пример показује

**Основни чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Почните овде да бисте видели LangChain4j у најсавршенијем облику. Креираћете `OpenAiOfficialChatModel`, послати упит са `.chat()`, и добити одговор. Ово показује основу: како да иницијализујете моделе са прилагођеним крајњим тачкама и API кључевима. Када разумете овај образац, све остало се надограђује на њему.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Пробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Отворите [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и питајте:
> - "Како да променим са GitHub модела на Azure OpenAI у овом коду?"
> - "Које друге параметре могу да конфигуришем у OpenAiOfficialChatModel.builder()?"
> - "Како да додам стриминг одговоре уместо чекања комплетног одговора?"

**Инжењеринг упита** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сада када знате како да говорите моделу, истражимо шта му говорите. Овај демо користи исту подешену конфигурацију модела али приказује пет различитих образаца упита. Испробајте zero-shot упите за директне инструкције, few-shot упите који уче из примера, chain-of-thought упите који откривају кораке резоновања, и role-based упите који постављају контекст. Видећете како исти модел даје драстично различите резултате на основу тога како формулишете свој захтев.

Демо такође показује шаблоне упита, који су моћан начин за креирање поновно употребљивих упита са променљивима.
Испод је пример упита који користи LangChain4j `PromptTemplate` да попуни варијабле. AI ће одговорити на основу дате дестинације и активности.

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

> **🤖 Пробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Отворите [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и питајте:
> - "Каква је разлика између zero-shot и few-shot упита и када треба користити који?"
> - "Како параметар temperature утиче на одговоре модела?"
> - "Које су неке технике за спречавање напада инјекције упита у продукцији?"
> - "Како могу да направим поновно употребљиве PromptTemplate објекте за уобичајене обрасце?"

**Интеграција алата** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ово је место где LangChain4j постаје моћан. Користићете `AiServices` да креирате AI асистента који може да позива ваше Java методе. Само означите методе са `@Tool("description")` и LangChain4j се брине о осталом - AI аутоматски одлучује када да користи који алат у зависности од тога шта корисник пита. Ово показује позивање функција, кључну технику за прављење AI који може да предузима акције, а не само да одговара на питања.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Пробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Отворите [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и питајте:
> - "Како ради анотација @Tool и шта LangChain4j ради са њом иза кулиса?"
> - "Може ли AI позвати више алата узастопно да реши сложене проблеме?"
> - "Шта се дешава ако алат баци изузетак - како треба да поступим са грешкама?"
> - "Како бих интегрисао прави API уместо овог примера калкулатора?"

**Питања и одговори о документима (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Овде ћете видети основу RAG (retrieval-augmented generation). Уместо да се ослањате на податке за обуку модела, учитавате садржај из [`document.txt`](../../../00-quick-start/document.txt) и укључујете га у упит. AI одговара на основу вашег документа, а не општих знања. Ово је први корак ка изградњи система који могу радити са вашим сопственим подацима.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Напомена:** Овај једноставан приступ учитава цео документ у упит. За велике фајлове (>10KB) премашићете лимите контекста. Модул 03 обрађује делење и претрагу вектора за продукционе RAG системе.

> **🤖 Пробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Отворите [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и питајте:
> - "Како RAG спречава AI халуцинације у поређењу са коришћењем података из обуке модела?"
> - "Каква је разлика између овог једноставног приступа и коришћења векторских уградњи за претрагу?"
> - "Како бих скалирао ово за рад са више докумената или већим базама знања?"
> - "Које су добре праксе за структуирање упита да се обезбеди да AI користи само пружени контекст?"

**Одговорни AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Изградите сигурност AI са одбраном у дубини. Овај демо показује два слоја заштите која раде заједно:

**Део 1: LangChain4j Input Guardrails** - Блокирају опасне упите пре него што стигну до LLM-а. Креирајте прилагођене „градиле“ које проверавају забрањене кључне речи или обрасце. Оне се извршавају у вашем коду, тако да су брзе и бесплатне.

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

**Део 2: Provider Safety Filters** - GitHub модели имају уграђене филтере који хватају оно што ваше градиле можда пропусти. Видећете чврсте блокаде (HTTP 400 грешке) за озбиљне прекршаје и благе одбијене одговоре где AI љубазно одбија.

> **🤖 Пробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Отворите [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и питајте:
> - "Шта је InputGuardrail и како да направим свој?"
> - "Каква је разлика између чврсте блокаде и благе одбијености?"
> - "Зашто користити и градиле и провајдер филтре заједно?"

## Следећи кораци

**Следећи модул:** [01-introduction - Почетак рада са LangChain4j и gpt-5 на Azure](../01-introduction/README.md)

---

**Навигација:** [← Назад на Главну](../README.md) | [Следећи: Модул 01 - Увод →](../01-introduction/README.md)

---

## Решавање проблема

### Прво компајлирање у Maven-у

**Проблем:** Почетно `mvn clean compile` или `mvn package` траје дуго (10-15 минута)

**Узрок:** Maven мора да преузме све зависности пројекта (Spring Boot, LangChain4j библиотеке, Azure SDK, итд.) при првом компајлирању.

**Решење:** Ово је нормално понашање. Накнадна компајлирања ће бити знатно бржа јер ће зависности бити кеширане локално. Време преузимања зависи од брзине ваше мреже.
### PowerShell Maven Синтакса команде

**Проблем**: Maven команде не успевају са грешком `Unknown lifecycle phase ".mainClass=..."`

**Узрок**: PowerShell тумачи `=` као оператор доделе вредности променљивој, што крши Maven синтаксу за својства

**Решење**: Користите оператор за заустављање парсирања `--%` пре Maven команде:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` говори PowerShell-у да све преостале аргументе проследи Maven-у дословно, без тумачења.

### Приказ емоџија у Windows PowerShell

**Проблем**: AI одговори приказују непојаваљиве знакове (нпр. `????` или `â??`) уместо емоџија у PowerShell-у

**Узрок**: Подразумевано кодирање у PowerShell-у не подржава UTF-8 емоџије

**Решење**: Покрените ову команду пре извођења Java апликација:
```cmd
chcp 65001
```

Ово приморава UTF-8 кодирање у терминалу. Алтернативно, користите Windows Terminal који боље подржава Unicode.

### Отстрањивање грешака у API позивима

**Проблем**: Грешке аутентификације, ограничења брзине или неочекивани одговори од AI модела

**Решење**: Примери укључују `.logRequests(true)` и `.logResponses(true)` да би приказали API позиве у конзоли. Ово помаже у решавању проблема са аутентификацијом, ограничењима или неочекиваним одговорима. Уклоните ове заставице у продукцији да бисте смањили количину логовања.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање**:  
Овај документ је преведен коришћењем AI преводилачке услуге [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо прецизности, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом матерњем језику треба сматрати кључним извором. За критичне информације препоручује се професионалан људски превод. Нисмо одговорни за било каква недостајања у разумевању или погрешне интерпретације које произлазе из употребе овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->