# Модул 00: Бърз старт

## Съдържание

- [Въведение](../../../00-quick-start)
- [Какво е LangChain4j?](../../../00-quick-start)
- [Зависимости на LangChain4j](../../../00-quick-start)
- [Предварителни условия](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Вземете своя GitHub Token](../../../00-quick-start)
  - [2. Задайте своя токен](../../../00-quick-start)
- [Стартиране на примерите](../../../00-quick-start)
  - [1. Основен чат](../../../00-quick-start)
  - [2. Шаблони за подсказки](../../../00-quick-start)
  - [3. Извикване на функции](../../../00-quick-start)
  - [4. Въпроси и отговори по документи (Easy RAG)](../../../00-quick-start)
  - [5. Отговорен AI](../../../00-quick-start)
- [Какво показва всеки пример](../../../00-quick-start)
- [Следващи стъпки](../../../00-quick-start)
- [Отстраняване на проблеми](../../../00-quick-start)

## Въведение

Този бърз старт е предназначен да ви помогне бързо да започнете работа с LangChain4j. Той обхваща абсолютните основи за изграждане на AI приложения с LangChain4j и GitHub Models. В следващите модули ще използвате Azure OpenAI с LangChain4j, за да създавате по-сложни приложения.

## Какво е LangChain4j?

LangChain4j е Java библиотека, която улеснява изграждането на AI-захранвани приложения. Вместо да работите с HTTP клиенти и парсинг на JSON, вие работите с чисти Java API.

"Chain" в LangChain се отнася до свързване на няколко компонента – например може да свържете подсказка към модел към парсър, или да свържете няколко AI извиквания, където изходът на едното е вход за следващото. Този бърз старт се фокусира върху основите, преди да разгледаме по-сложни вериги.

<img src="../../../translated_images/bg/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Свързване на компоненти в LangChain4j - строителни блокове, които се свързват за създаване на мощни AI работни потоци*

Ще използваме три основни компонента:

**ChatModel** - Интерфейс за взаимодействие с AI модели. Извиквате `model.chat("prompt")` и получавате отговор като низ. Използваме `OpenAiOfficialChatModel`, който работи с OpenAI-совместими крайни точки като GitHub Models.

**AiServices** - Създава типобезопасни интерфейси за AI услуги. Дефинирате методи, анотирате ги с `@Tool`, а LangChain4j се грижи за оркестрацията. AI автоматично извиква вашите Java методи при необходимост.

**MessageWindowChatMemory** - Запазва историята на разговора. Без него заявките са независими. С него AI помни предишните съобщения и поддържа контекст през няколко обмена.

<img src="../../../translated_images/bg/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура на LangChain4j - основни компоненти, работещи заедно за захранване на вашите AI приложения*

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

Модулът `langchain4j-open-ai-official` предоставя класа `OpenAiOfficialChatModel`, който се свързва с OpenAI-съвместими API. GitHub Models използват същия формат, затова не е нужен специален адаптер – просто насочете базовия URL към `https://models.github.ai/inference`.

Модулът `langchain4j-easy-rag` предлага автоматично разделяне на документи, вграждане и извличане, така че можете да създавате RAG приложения без ръчно конфигуриране на всяка стъпка.

## Предварителни условия

**Използвате ли Dev Container?** Java и Maven вече са инсталирани. Не ви трябва нищо друго освен GitHub Personal Access Token.

**Локална разработка:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (инструкции по-долу)

> **Забележка:** Този модул използва `gpt-4.1-nano` от GitHub Models. Не променяйте името на модела в кода – конфигуриран е да работи с наличните модели на GitHub.

## Настройка

### 1. Вземете своя GitHub Token

1. Отидете на [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Натиснете "Generate new token"
3. Задайте описателно име (например "LangChain4j Demo")
4. Задайте срок на валидност (препоръчително 7 дни)
5. Под "Account permissions", намерете "Models" и задайте на "Read-only"
6. Натиснете "Generate token"
7. Копирайте и запазете токена си – няма да го видите отново

### 2. Задайте своя токен

**Опция 1: Използване на VS Code (препоръчително)**

Ако използвате VS Code, добавете токена си във файла `.env` в корена на проекта:

Ако `.env` файлът не съществува, копирайте `.env.example` като `.env` или създайте нов `.env` файл в корена на проекта.

**Пример за `.env` файл:**
```bash
# В /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

След това просто кликнете с десен бутон върху някой демонстрационен файл (например `BasicChatDemo.java`) в Explorer и изберете **"Run Java"** или използвайте конфигурациите за стартиране в панела Run and Debug.

**Опция 2: Чрез терминал**

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

**Използване на VS Code:** Просто кликнете с десен бутон върху някой демонстрационен файл в Explorer и изберете **"Run Java"**, или използвайте конфигурациите за изпълнение от Run and Debug панела (уверете се, че сте добавили токена във `.env` файла).

**Използване на Maven:** Алтернативно, можете да стартирате чрез командния ред:

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

Показва zero-shot, few-shot, chain-of-thought и role-based подсказки.

### 3. Извикване на функции

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI автоматично извиква вашите Java методи при необходимост.

### 4. Въпроси и отговори по документи (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте въпроси за вашите документи с Easy RAG, включващ автоматично вграждане и извличане.

### 5. Отговорен AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Вижте как филтрите за безопасност блокират вредно съдържание.

## Какво показва всеки пример

**Основен чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Започнете тук, за да видите LangChain4j в най-елементарната му форма. Създайте `OpenAiOfficialChatModel`, изпратете подсказка с `.chat()` и получите отговор. Това демонстрира основата: как да инициализирате модели с персонализирани крайни точки и API ключове. След като разберете този модел, всичко останало е надграждане.

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
> - "Как да превключа от GitHub Models към Azure OpenAI в този код?"
> - "Какви други параметри мога да конфигурирам в OpenAiOfficialChatModel.builder()?"
> - "Как да добавя поточни отговори вместо да чакам пълния отговор?"

**Проектиране на подсказки** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сега, след като знаете как да говорите с модел, нека разгледаме какво му казвате. Този демонстрационен пример използва същата настройка на модела, но показва пет различни начина за съставяне на подсказки. Опитайте zero-shot подсказки за директни инструкции, few-shot подсказки, които се учат от примери, chain-of-thought подсказки, които показват стъпки на мислене, и role-based подсказки, които задават контекст. Ще видите как един и същ модел дава коренно различни резултати в зависимост от начина на формулиране на заявката.

Демонстрацията също така показва шаблони за подсказки, които са мощен начин за създаване на многократно използваеми подсказки с променливи.
По-долу примерът показва подсказка, използваща LangChain4j `PromptTemplate` за запълване на променливи. AI ще отговори на базата на зададената дестинация и дейност.

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
> - "Каква е разликата между zero-shot и few-shot подсказването и кога да използвам всяко от тях?"
> - "Как параметърът температура влияе на отговорите на модела?"
> - "Какви техники има за предотвратяване на prompt injection атаки в продукция?"
> - "Как мога да създам многократно използваеми обекти PromptTemplate за общи модели?"

**Интеграция на инструменти** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Тук LangChain4j показва своята мощ. Ще използвате `AiServices` за създаване на AI асистент, който може да извиква вашите Java методи. Просто анотирайте методите с `@Tool("описание")` и LangChain4j поема останалото – AI автоматично решава кога да използва кой инструмент според заявките на потребителя. Това демонстрира извикване на функции – ключова техника за изграждане на AI, който може да предприема действия, а не само да отговаря на въпроси.

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
> - "Как работи анотацията @Tool и какво прави LangChain4j с нея зад кулисите?"
> - "Може ли AI да извиква няколко инструмента последователно, за да решава сложни проблеми?"
> - "Какво се случва, ако инструмент хвърля изключение – как да обработвам грешки?"
> - "Как бих интегрирал реално API вместо този пример с калкулатора?"

**Въпроси и отговори по документи (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тук ще видите RAG (retrieval-augmented generation) с "Easy RAG" подхода на LangChain4j. Документите се зареждат, автоматично се разделят и вграждат в паметта, след което откривателят на съдържание предоставя релевантни части на AI при заявка. AI отговаря на база вашите документи, а не само на общите си знания.

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
> - "Как RAG предотвратява халюцинациите на AI в сравнение с използването само на тренировъчните данни на модела?"
> - "Каква е разликата между този лесен подход и персонализиран RAG pipeline?"
> - "Как бих разширил това, за да обработвам множество документи или по-големи бази знания?"

**Отговорен AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Създавайте безопасност в AI с многостепенна защита. Този демонстрационен пример показва два слоя защита, работещи заедно:

**Част 1: LangChain4j Input Guardrails** – Блокират опасни подсказки преди да достигнат до LLM. Създавайте свои собствени guardrails, които проверяват за забранени ключови думи или модели. Те се изпълняват във вашия код, затова са бързи и безплатни.

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

**Част 2: Филтри за безопасност от доставчика** – GitHub Models имат вградени филтри, които хващат това, което вашите guardrails пропускат. Ще видите твърди блокировки (HTTP 400 грешки) при сериозни нарушения и меки откази, при които AI учтиво отказва.

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и попитайте:
> - "Какво е InputGuardrail и как да създам свой собствен?"
> - "Каква е разликата между твърд блок и мек отказ?"
> - "Защо да използвам и guardrails, и филтри от доставчика заедно?"

## Следващи стъпки

**Следващ модул:** [01-introduction - Започване с LangChain4j и gpt-5 на Azure](../01-introduction/README.md)

---

**Навигация:** [← Обратно към Главната](../README.md) | [Следващо: Модул 01 - Въведение →](../01-introduction/README.md)

---

## Отстраняване на проблеми

### Първо изграждане с Maven

**Проблем:** Първоначалното `mvn clean compile` или `mvn package` отнема дълго (10-15 минути)

**Причина:** Maven трябва да изтегли всички зависимости на проекта (Spring Boot, LangChain4j библиотеки, Azure SDK и др.) при първото изграждане.

**Решение:** Това е нормално поведение. Следващите изграждания ще бъдат много по-бързи, тъй като зависимостите са кеширани локално. Времето за изтегляне зависи от вашата скорост на интернет.

### Синтаксис на Maven команди в PowerShell

**Проблем:** Maven командите не се изпълняват с грешка `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell интерпретира `=` като оператор за присвояване на променлива, което нарушава синтаксиса на Maven свойствата

**Решение**: Използвайте оператора за спиране на парсването `--%` преди Maven командата:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Операторът `--%` казва на PowerShell да предаде всички останали аргументи дословно на Maven без интерпретация.

### Показване на емоджита в Windows PowerShell

**Проблем**: AI отговорите показват неразбираеми символи (например `????` или `â??`) вместо емоджита в PowerShell

**Причина**: По подразбиране PowerShell използва кодировка, която не поддържа UTF-8 емоджита

**Решение**: Стартирайте тази команда преди изпълнението на Java приложения:
```cmd
chcp 65001
```

Това налага UTF-8 кодировка в терминала. Като алтернатива използвайте Windows Terminal, който има по-добра поддръжка на Unicode.

### Отстраняване на грешки при API повиквания

**Проблем**: Грешки при удостоверяване, ограничение на скоростта или неочаквани отговори от AI модела

**Решение**: Примерите включват `.logRequests(true)` и `.logResponses(true)`, за да покажат API повикванията в конзолата. Това помага при отстраняване на грешки с удостоверяването, ограничение на скоростта или неочаквани отговори. Премахнете тези флагове в продукция, за да намалите шума в логовете.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводаческа услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия първоначален език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален превод от човек. Ние не носим отговорност за каквито и да е недоразумения или погрешни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->