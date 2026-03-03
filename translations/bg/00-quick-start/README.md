# Module 00: Бърз старт

## Съдържание

- [Въведение](../../../00-quick-start)
- [Какво е LangChain4j?](../../../00-quick-start)
- [Зависимости на LangChain4j](../../../00-quick-start)
- [Предпоставки](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Вземете своя GitHub токен](../../../00-quick-start)
  - [2. Задайте своя токен](../../../00-quick-start)
- [Стартиране на примерите](../../../00-quick-start)
  - [1. Основен чат](../../../00-quick-start)
  - [2. Шаблони за подсказки](../../../00-quick-start)
  - [3. Извикване на функции](../../../00-quick-start)
  - [4. Въпроси и отговори за документи (Easy RAG)](../../../00-quick-start)
  - [5. Отговорен AI](../../../00-quick-start)
- [Какво показва всеки пример](../../../00-quick-start)
- [Следващи стъпки](../../../00-quick-start)
- [Отстраняване на проблеми](../../../00-quick-start)

## Въведение

Този бърз старт има за цел да ви помогне да започнете да работите с LangChain4j възможно най-бързо. Той обхваща абсолютните основи на изграждането на AI приложения с LangChain4j и GitHub модели. В следващите модули ще преминете към Azure OpenAI и GPT-5.2 и ще навлезете по-дълбоко във всеки концепт.

## Какво е LangChain4j?

LangChain4j е Java библиотека, която улеснява изграждането на AI-задвижвани приложения. Вместо да се занимавате с HTTP клиенти и парсване на JSON, работите с чисти Java API-та.

„Верига“ в LangChain означава свързване на множество компоненти заедно – например можете да свържете подсказка към модел към парсър, или да свържете няколко AI повиквания, където един изход се подава като вход на следващото. Този бърз старт се фокусира върху основите, преди да разгледа по-сложни вериги.

<img src="../../../translated_images/bg/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Свързване на компоненти в LangChain4j – основни блокове, които се свързват, за да създадат мощни AI работни потоци*

Ще използваме три основни компонента:

**ChatModel** – интерфейс за взаимодействие с AI модели. Извиквате `model.chat("prompt")` и получавате отговор като низ. Използваме `OpenAiOfficialChatModel`, който работи с OpenAI-съвместими крайни точки като GitHub модели.

**AiServices** – създава типобезопасни AI интерфейси за услуги. Дефинирате методи, анотирате ги с `@Tool` и LangChain4j управлява оркестрацията. AI автоматично извиква вашите Java методи когато е необходимо.

**MessageWindowChatMemory** – поддържа историята на разговора. Без това всяка заявка е независима. С него AI помни предишните съобщения и поддържа контекст през няколко цикъла.

<img src="../../../translated_images/bg/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура на LangChain4j – основни компоненти, които работят заедно, за да задвижат вашите AI приложения*

## Зависимости на LangChain4j

Този бърз старт използва три Maven зависимости в [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модулът `langchain4j-open-ai-official` предоставя класа `OpenAiOfficialChatModel`, който се свързва с OpenAI-съвместими API-та. GitHub модели използват същия API формат, така че не е необходим специален адаптер – просто насочете основния URL към `https://models.github.ai/inference`.

Модулът `langchain4j-easy-rag` предоставя автоматично разделяне на документи, вграждане и извличане, така че да може да изградите RAG приложения без да конфигурирате всеки етап ръчно.

## Предпоставки

**Използвате Dev Container?** Java и Maven вече са инсталирани. Нужно е само да имате GitHub Personal Access Token.

**Локална разработка:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (инструкции по-долу)

> **Важно:** Този модул използва `gpt-4.1-nano` от GitHub модели. Не променяйте името на модела в кода – той е конфигуриран да работи с наличните модели на GitHub.

## Настройка

### 1. Вземете своя GitHub токен

1. Отидете на [GitHub Настройки → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Кликнете „Generate new token“
3. Задайте описателно име (например „LangChain4j Demo“)
4. Изберете срок на валидност (препоръчително 7 дни)
5. Под „Account permissions“ намерете „Models“ и задайте на „Read-only“
6. Кликнете „Generate token“
7. Копирайте и запазете токена си – няма да го видите отново

### 2. Задайте своя токен

**Опция 1: Използване на VS Code (Препоръчително)**

Ако използвате VS Code, добавете токена в `.env` файла в корена на проекта:

Ако файлът `.env` не съществува, копирайте `.env.example` до `.env` или създайте нов `.env` файл в корена на проекта.

**Примерен `.env` файл:**
```bash
# В /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

След това просто кликнете с десния бутон върху който и да е демонстрационен файл (например `BasicChatDemo.java`) в Explorer и изберете **„Run Java“** или използвайте конфигурациите за стартиране от панела Run and Debug.

**Опция 2: С помощта на терминал**

Задайте токена като променлива на средата:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Стартиране на примерите

**С VS Code:** Просто кликнете с десен бутон върху който и да е демонстрационен файл в Explorer и изберете **„Run Java“**, или използвайте конфигурациите за стартиране от панела Run and Debug (преди това трябва да сте добавили токена в `.env` файла).

**С Maven:** Алтернативно, можете да стартирате от командния ред:

### 1. Основен чат

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Шаблони за подсказки

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Показва zero-shot, few-shot, chain-of-thought и подсказки на база роли.

### 3. Извикване на функции

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI автоматично извиква вашите Java методи когато е нужно.

### 4. Въпроси и отговори за документи (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте въпроси за вашите документи, използвайки Easy RAG с автоматично вграждане и извличане.

### 5. Отговорен AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Вижте как филтрите за безопасност на AI блокират вредно съдържание.

## Какво показва всеки пример

**Основен чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Започнете тук, за да видите LangChain4j в най-простия му вид. Ще създадете `OpenAiOfficialChatModel`, ще изпратите подсказка с `.chat()` и ще получите отговор. Това демонстрира основата: как да инициализирате модели с персонализирани крайни точки и API ключове. След като разберете този модел, всичко останало се изгражда върху него.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и попитайте:
> - „Как да премина от GitHub Models към Azure OpenAI в този код?“
> - „Какви други параметри мога да конфигурирам в OpenAiOfficialChatModel.builder()?“
> - „Как да добавя стрийминг на отговорите вместо да чакам цялостния отговор?“

**Инженерство на подсказки** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сега, когато знаете как да говорите с модел, нека разгледаме какво му казвате. Този демонстрационен пример използва същата настройка на модела, но показва пет различни шаблона за подсказки. Опитайте zero-shot подсказки за директни инструкции, few-shot подсказки, които се учат от примери, chain-of-thought подсказки, които разкриват стъпки на разсъждение, и подсказки на база роли, които задават контекст. Ще видите как един и същ модел дава драматично различни резултати в зависимост от това как формулирате заявката си.

Демонстрацията също показва шаблони за подсказки, които са мощен начин за създаване на многократно използваеми подсказки с променливи.
Долният пример показва подсказка, използваща `PromptTemplate` на LangChain4j за попълване на променливи. AI ще отговори въз основа на посочената дестинация и дейност.

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

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и попитайте:
> - „Каква е разликата между zero-shot и few-shot подсказките, и кога да използвам коя?“
> - „Как параметърът temperature влияе на отговорите на модела?“
> - „Какви са някои техники за предотвратяване на атаки с вкарване на подсказки в продукция?“
> - „Как да създам многократно използваеми обекти PromptTemplate за често срещани шаблони?“

**Интеграция на инструменти** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Тук LangChain4j става мощен. Ще използвате `AiServices`, за да създадете AI асистент, който може да извиква вашите Java методи. Просто анотирайте методите с `@Tool("описание")` и LangChain4j се грижи за останалото – AI автоматично решава кога да използва всеки инструмент според това, което потребителят пита. Това демонстрира извикване на функции – ключова техника за изграждане на AI, който може да предприема действия, а не само да отговаря на въпроси.

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

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и попитайте:
> - „Как работи анотацията @Tool и какво прави LangChain4j с нея зад кулисите?“
> - „Може ли AI да извика няколко инструмента последователно, за да реши сложни проблеми?“
> - „Какво се случва, ако инструмент хвърли изключение – как да обработвам грешки?“
> - „Как бих интегрирал истинско API вместо този пример с калкулатор?“

**Въпроси и отговори за документи (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тук ще видите RAG (генериране с извличане) с подхода "Easy RAG" на LangChain4j. Документите се зареждат, автоматично се разделят и вграждат в паметта, след което съдържателен извличач доставя релевантни части към AI по време на запитване. AI отговаря въз основа на вашите документи, а не на общото си знание.

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

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и попитайте:
> - „Как RAG предотвратява халюцинациите на AI в сравнение с използването на тренировъчните данни на модела?“
> - „Каква е разликата между този лесен подход и персонализиран RAG пайплайн?“
> - „Как бих мащабирал това, за да обхване множество документи или по-големи бази знания?“

**Отговорен AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Изградете безопасност на AI с многослойни защити. Този демо пример показва два слоя защита, работещи заедно:

**Част 1: LangChain4j Input Guardrails** – блокира опасни подсказки преди да достигнат до LLM. Създавайте персонализирани guardrails, които проверяват за забранени ключови думи или шаблони. Те се изпълняват във вашия код, така че са бързи и безплатни.

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

**Част 2: Филтри за безопасност на доставчика** – GitHub модели имат вградени филтри, които хващат това, което вашите guardrails могат да пропуснат. Ще видите твърди блокировки (HTTP 400 грешки) при сериозни нарушения и меки откази, при които AI учтиво отказва.

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и попитайте:
> - „Какво представлява InputGuardrail и как да създам свой собствен?“
> - „Каква е разликата между твърд блок и мек отказ?“
> - „Защо да използвам и guardrails, и филтри на доставчика заедно?“

## Следващи стъпки

**Следващ модул:** [01-introduction - Запознаване с LangChain4j](../01-introduction/README.md)

---

**Навигация:** [← Обратно към основното](../README.md) | [Напред: Модул 01 - Въведение →](../01-introduction/README.md)

---

## Отстраняване на проблеми

### Първа компилация с Maven

**Проблем:** Първоначалното `mvn clean compile` или `mvn package` отнема дълго (10–15 минути)

**Причина:** Maven трябва да изтегли всички зависимости на проекта (Spring Boot, LangChain4j библиотеки, Azure SDK и др.) при първата компилация.

**Решение:** Това е нормално поведение. Следващите компилации ще бъдат значително по-бързи, тъй като зависимостите са кеширани локално. Времето за изтегляне зависи от скоростта на вашата мрежа.

### Синтаксис на Maven команди в PowerShell

**Проблем:** Maven командите се провалят с грешка `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell тълкува `=` като оператор за присвояване на променлива, което разваля синтаксиса на Maven свойството

**Решение**: Използвайте операторa за спиране на парсване `--%` преди Maven командата:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Операторът `--%` казва на PowerShell да предаде всички останали аргументи буквално на Maven без тълкуване.

### Показване на емоджита в Windows PowerShell

**Проблем**: Отговорите от AI показват неразбираеми символи (напр. `????` или `â??`) вместо емоджита в PowerShell

**Причина**: По подразбиране енкодингът на PowerShell не поддържа UTF-8 емоджита

**Решение**: Изпълнете тази команда преди стартирането на Java приложения:
```cmd
chcp 65001
```

Това принуждава използването на UTF-8 енкодинг в терминала. Като алтернатива, използвайте Windows Terminal, който има по-добра поддръжка на Unicode.

### Отстраняване на грешки при API повиквания

**Проблем**: Грешки при удостоверяване, ограничения на заявките или неочаквани отговори от AI модела

**Решение**: Примерите включват `.logRequests(true)` и `.logResponses(true)` за показване на API повикванията в конзолата. Това помага при отстраняване на грешки с удостоверяването, ограниченията на броя на заявките или неочаквани отговори. Премахнете тези флагове в продукция, за да намалите шума в логовете.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводаческа услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на своя роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->