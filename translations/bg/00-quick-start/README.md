# Module 00: Бърз старт

## Съдържание

- [Въведение](../../../00-quick-start)
- [Какво е LangChain4j?](../../../00-quick-start)
- [Зависимости на LangChain4j](../../../00-quick-start)
- [Предварителни изисквания](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Вземете своя GitHub токен](../../../00-quick-start)
  - [2. Задайте своя токен](../../../00-quick-start)
- [Стартиране на примерите](../../../00-quick-start)
  - [1. Основен чат](../../../00-quick-start)
  - [2. Шаблони за подсказки](../../../00-quick-start)
  - [3. Извикване на функции](../../../00-quick-start)
  - [4. Въпроси и отговори по документ (RAG)](../../../00-quick-start)
  - [5. Отговорен AI](../../../00-quick-start)
- [Какво показва всеки пример](../../../00-quick-start)
- [Следващи стъпки](../../../00-quick-start)
- [Отстраняване на неизправности](../../../00-quick-start)

## Въведение

Този бърз старт е предназначен да ви помогне да започнете работа с LangChain4j възможно най-бързо. Той обхваща абсолютните основи на изграждането на AI приложения с LangChain4j и GitHub модели. В следващите модули ще използвате Azure OpenAI с LangChain4j за създаване на по-сложни приложения.

## Какво е LangChain4j?

LangChain4j е Java библиотека, която опростява създаването на AI базирани приложения. Вместо да се справяте с HTTP клиенти и JSON парсинг, вие работите с чисти Java API-та.

„Верижка“ в LangChain се отнася до свързването на няколко компонента – можете да свържете подсказка с модел, след това с парсър, или да свържете няколко AI повиквания, където един изход захранва следващия вход. Този бърз старт се фокусира върху основите преди да разгледа по-сложни вериги.

<img src="../../../translated_images/bg/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Свързване на компоненти в LangChain4j – строителни блокове, които се свързват за създаване на мощни AI процеси*

Ще използваме три основни компоненти:

**ChatLanguageModel** – Интерфейс за взаимодействие с AI моделите. Извиквате `model.chat("prompt")` и получавате отговор като низ. Използваме `OpenAiOfficialChatModel`, който работи с OpenAI-съвместими крайни точки като GitHub модели.

**AiServices** – Създава типобезопасни AI интерфейси за услуги. Дефинирате методи, анотирате ги с `@Tool` и LangChain4j управлява оркестрирането. AI автоматично извиква вашите Java методи при нужда.

**MessageWindowChatMemory** – Поддържа история на разговора. Без него всяка заявка е независима. С него AI помни предишните съобщения и поддържа контекст през няколко хода.

<img src="../../../translated_images/bg/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура на LangChain4j – основни компоненти работят заедно, за да захранват вашите AI приложения*

## Зависимости на LangChain4j

Този бърз старт използва две зависимости с Maven в [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модулът `langchain4j-open-ai-official` предоставя класа `OpenAiOfficialChatModel`, който свързва с OpenAI-съвместими API-та. GitHub Models използва същия API формат, така че не е необходим специален адаптер – просто насочете базовия URL към `https://models.github.ai/inference`.

## Предварителни изисквания

**Използвате ли Dev Container?** Java и Maven вече са инсталирани. Трябва ви само личен токен за достъп в GitHub.

**Локална разработка:**
- Java 21+, Maven 3.9+
- Личен токен за достъп в GitHub (инструкции по-долу)

> **Забележка:** Този модул използва `gpt-4.1-nano` от GitHub Models. Не променяйте името на модела в кода – той е конфигуриран да работи с наличните модели на GitHub.

## Настройка

### 1. Вземете своя GitHub токен

1. Отидете на [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Кликнете „Generate new token“
3. Задайте описателно име (напр. „LangChain4j Demo“)
4. Задайте срок на валидност (препоръчително 7 дни)
5. Под „Account permissions“ намерете „Models“ и задайте „Read-only“
6. Кликнете „Generate token“
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

След това можете просто да кликнете с десен бутон върху някой демо файл (напр. `BasicChatDemo.java`) в Explorer и да изберете **"Run Java"** или да използвате конфигурациите за стартиране от панела Run and Debug.

**Опция 2: Използване на терминал**

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

**С VS Code:** Просто кликнете с десен бутон върху някой демо файл в Explorer и изберете **"Run Java"**, или използвайте конфигурациите за стартиране от панела Run and Debug (предварително добавете токена си във файла `.env`).

**С Maven:** Алтернативно, може да стартирате от командния ред:

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

Показва zero-shot, few-shot, chain-of-thought и ролево базирани подсказки.

### 3. Извикване на функции

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI автоматично извиква вашите Java методи при нужда.

### 4. Въпроси и отговори по документ (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте въпроси относно съдържанието в `document.txt`.

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

Започнете тук, за да видите LangChain4j в най-основния му вид. Ще създадете `OpenAiOfficialChatModel`, ще изпратите подсказка с `.chat()` и ще получите отговор. Това демонстрира основата: как да инициализирате модели с персонализирани крайни точки и API ключове. След като разберете този модел, всичко останало върви върху него.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и попитайте:
> - „Как мога да премина от GitHub Models към Azure OpenAI в този код?“
> - „Какви други параметри мога да конфигурирам в OpenAiOfficialChatModel.builder()?“
> - „Как да добавя стрийминг на отговорите вместо да чакам пълния отговор?“

**Инженеринг на подсказки** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сега, когато знаете как да говорите с модел, нека разгледаме какво казвате. Това демо използва същата настройка на модел, но показва пет различни шаблона за подсказки. Опитайте zero-shot подсказки за директни инструкции, few-shot подсказки, които се учат от примери, chain-of-thought подсказки, които разкриват стъпки на разсъждение, и ролево базирани подсказки, които задават контекст. Ще видите как същият модел дава драматично различни резултати в зависимост от формулировката на вашата заявка.

Демонстрацията също показва шаблони за подсказки, които са мощен начин да създавате преизползваеми подсказки с променливи.
Долният пример показва подсказка, използваща LangChain4j `PromptTemplate`, за да попълни променливи. AI ще отговаря на база зададената дестинация и активност.

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

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и попитайте:
> - „Каква е разликата между zero-shot и few-shot подсказване и кога да използвам всяко?“
> - „Как параметърът temperature влияе върху отговорите на модела?“
> - „Какви са някои техники за предотвратяване на атаки с вмъкване на подсказки в продукция?“
> - „Как мога да създам преизползваеми обекти PromptTemplate за често срещани шаблони?“

**Интеграция на инструменти** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Тук LangChain4j става мощен. Ще използвате `AiServices`, за да създадете AI асистент, който може да извиква вашите Java методи. Просто анотирайте методите с `@Tool("описание")` и LangChain4j управлява останалото – AI автоматично решава кога да използва всеки инструмент въз основа на това, което потребителят пита. Това демонстрира извикване на функции, ключова техника за изграждане на AI, който може да предприема действия, не само да отговаря на въпроси.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и попитайте:
> - „Как работи анотацията @Tool и какво прави LangChain4j с нея зад кулисите?“
> - „Може ли AI да извика няколко инструмента последователно, за да реши сложни задачи?“
> - „Какво се случва, ако инструмент изхвърли изключение – как да обработвам грешки?“
> - „Как бих интегрирал реално API вместо примера с калкулатора?“

**Въпроси и отговори по документ (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тук ще видите основата на RAG (retrieval-augmented generation). Вместо да разчитате на трениращите данни на модела, зареждате съдържание от [`document.txt`](../../../00-quick-start/document.txt) и го включвате в подсказката. AI отговаря базирано на вашия документ, а не на общите си знания. Това е първата стъпка към изграждане на системи, които могат да работят с ваши собствени данни.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Забележка:** Този прост подход зарежда целия документ в подсказката. За големи файлове (>10KB) ще надвишите ограничения за контекст. Модул 03 разглежда разбиването на части и векторното търсене за производствени RAG системи.

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и попитайте:
> - „Как RAG предотвратява халюцинации на AI в сравнение с използването на трениращите данни на модела?“
> - „Каква е разликата между този прост подход и използването на векторни ембеддинги за търсене?“
> - „Как бих мащабирал това за работа с множество документи или по-големи бази знания?“
> - „Кои са добрите практики за структуриране на подсказката, за да гарантирам, че AI използва само предоставения контекст?“

**Отговорен AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Изградете безопасност на AI с многостепенна защита. Това демо показва две нива на защита, които работят заедно:

**Част 1: LangChain4j входни предпазни мерки** – Блокиране на опасни подсказки преди да достигнат до LLM. Създайте персонализирани предпазни мерки, които проверяват за забранени ключови думи или шаблони. Те се изпълняват във вашия код, така че са бързи и безплатни.

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

**Част 2: Филтри за безопасност от доставчика** – GitHub Models имат вградени филтри, които улавят това, което предпазните мерки може да пропуснат. Ще видите твърди блокировки (HTTP 400 грешки) при сериозни нарушения и меки отхвърляния, когато AI учтиво отказва.

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворете [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и попитайте:
> - „Какво е InputGuardrail и как да създам свой собствен?“
> - „Каква е разликата между твърд блок и мек отказ?“
> - „Защо да използвам и предпазни мерки, и филтри на доставчика заедно?“

## Следващи стъпки

**Следващ модул:** [01-introduction - Започване с LangChain4j и gpt-5 в Azure](../01-introduction/README.md)

---

**Навигация:** [← Обратно към Основната страница](../README.md) | [Напред: Модул 01 - Въведение →](../01-introduction/README.md)

---

## Отстраняване на неизправности

### Първо компилиране с Maven

**Проблем:** Първоначалното `mvn clean compile` или `mvn package` отнема дълго време (10-15 минути)

**Причина:** Maven трябва да изтегли всички зависимости на проекта (Spring Boot, LangChain4j библиотеки, Azure SDK, и т.н.) при първото компилиране.

**Решение:** Това е нормално поведение. Следващите компилации ще бъдат много по-бързи, тъй като зависимостите се кешират локално. Времето за изтегляне зависи от скоростта на вашата мрежа.
### Синтаксис на PowerShell Maven команда

**Проблем**: Maven командите се провалят с грешка `Unknown lifecycle phase ".mainClass=..."`

**Причина**: PowerShell интерпретира `=` като оператор за задаване на променлива, което разрушава синтаксиса на Maven свойството

**Решение**: Използвайте оператора за спиране на парсинга `--%` преди Maven командата:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Операторът `--%` указва на PowerShell да предаде всички останали аргументи буквално на Maven без интерпретация.

### Отображение на емоджита в Windows PowerShell

**Проблем**: AI отговорите показват непознати символи (например `????` или `â??`) вместо емоджита в PowerShell

**Причина**: По подразбиране кодировката в PowerShell не поддържа UTF-8 емоджита

**Решение**: Изпълнете тази команда преди стартиране на Java приложения:
```cmd
chcp 65001
```

Това принуждава използването на UTF-8 кодировка в терминала. Като алтернатива използвайте Windows Terminal, който има по-добра поддръжка на Unicode.

### Отстраняване на проблеми с API повиквания

**Проблем**: Грешки при удостоверяване, ограничения на честотата или неочаквани отговори от AI модела

**Решение**: Примерите включват `.logRequests(true)` и `.logResponses(true)`, които показват API повикванията в конзолата. Това помага при отстраняване на грешки с удостоверяването, ограниченията и неочакваните отговори. Премахнете тези параметри в продукцията за намаляване на шума в логовете.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводаческа услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->