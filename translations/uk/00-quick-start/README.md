# Module 00: Швидкий старт

## Зміст

- [Вступ](../../../00-quick-start)
- [Що таке LangChain4j?](../../../00-quick-start)
- [Залежності LangChain4j](../../../00-quick-start)
- [Вимоги](../../../00-quick-start)
- [Налаштування](../../../00-quick-start)
  - [1. Отримайте свій GitHub токен](../../../00-quick-start)
  - [2. Встановіть свій токен](../../../00-quick-start)
- [Запуск прикладів](../../../00-quick-start)
  - [1. Базовий чат](../../../00-quick-start)
  - [2. Шаблони підказок](../../../00-quick-start)
  - [3. Виклик функцій](../../../00-quick-start)
  - [4. Запитання і відповіді по документу (RAG)](../../../00-quick-start)
  - [5. Відповідальний ШІ](../../../00-quick-start)
- [Що показує кожен приклад](../../../00-quick-start)
- [Наступні кроки](../../../00-quick-start)
- [Вирішення проблем](../../../00-quick-start)

## Вступ

Цей швидкий старт призначений, щоб ви якнайшвидше почали працювати з LangChain4j. В ньому розглядаються абсолютні основи створення AI-застосунків з LangChain4j та GitHub Models. У наступних модулях ви використовуватимете Azure OpenAI з LangChain4j для створення більш складних застосунків.

## Що таке LangChain4j?

LangChain4j — це Java-бібліотека, яка спрощує створення застосунків на базі штучного інтелекту. Замість роботи з HTTP-клієнтами та парсингом JSON ви працюєте з чистим Java API.

“Chain” у LangChain означає ланцюжок, де ви поєднуєте кілька компонентів — наприклад, зв’язуєте підказку з моделлю і парсером, або виконуєте кілька викликів ШІ, де вихід одного передається на вхід іншому. Цей швидкий старт зосереджений на базових речах перед вивченням складніших ланцюжків.

<img src="../../../translated_images/uk/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Поєднання компонентів у LangChain4j — будівельні блоки створюють потужні AI-потоки*

Ми використаємо три основні компоненти:

**ChatLanguageModel** — інтерфейс для взаємодії з AI-моделями. Викликайте `model.chat("prompt")` і отримуйте рядкову відповідь. Ми використовуємо `OpenAiOfficialChatModel`, який працює з кінцевими точками сумісними з OpenAI, як GitHub Models.

**AiServices** — створює типобезпечні інтерфейси AI-сервісів. Визначте методи, анотуйте їх `@Tool`, і LangChain4j керує оркестрацією. ШІ автоматично викликає ваші методи Java за потребою.

**MessageWindowChatMemory** — зберігає історію розмови. Без нього кожен запит незалежний. З ним ШІ пам’ятає попередні повідомлення і підтримує контекст через кілька кроків.

<img src="../../../translated_images/uk/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архітектура LangChain4j — основні компоненти працюють разом, щоб живити ваші AI-застосунки*

## Залежності LangChain4j

Для цього швидкого старту використовуються дві залежності Maven у файлі [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модуль `langchain4j-open-ai-official` надає клас `OpenAiOfficialChatModel`, який підключається до API, сумісних з OpenAI. GitHub Models використовує той же формат API, тому не потрібен спеціальний адаптер — просто вкажіть базовий URL на `https://models.github.ai/inference`.

## Вимоги

**Використовуєте Dev Container?** Java та Maven вже встановлені. Потрібен лише особистий токен доступу GitHub.

**Локальна розробка:**
- Java 21+, Maven 3.9+
- Особистий токен доступу GitHub (інструкції нижче)

> **Примітка:** У цьому модулі використовується `gpt-4.1-nano` з GitHub Models. Не змінюйте ім’я моделі у коді — вона налаштована для роботи з доступними у GitHub моделями.

## Налаштування

### 1. Отримайте свій GitHub токен

1. Перейдіть у [Налаштування GitHub → Особисті токени доступу](https://github.com/settings/personal-access-tokens)
2. Натисніть "Generate new token"
3. Вкажіть описове ім'я (наприклад, "LangChain4j Demo")
4. Встановіть термін дії (рекомендується 7 днів)
5. У "Account permissions" знайдіть "Models" і встановіть "Read-only"
6. Натисніть "Generate token"
7. Скопіюйте й збережіть токен — його більше не буде видно

### 2. Встановіть свій токен

**Варіант 1: Використання VS Code (рекомендується)**

Якщо ви працюєте у VS Code, додайте свій токен до файлу `.env` у корені проекту:

Якщо `.env` файл відсутній, скопіюйте `.env.example` у `.env` або створіть новий `.env` файл у корені проекту.

**Приклад файлу `.env`:**
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Потім просто клацніть правою кнопкою миші на будь-якому демонстраційному файлі (наприклад, `BasicChatDemo.java`) в Провіднику і виберіть **"Run Java"** або використайте конфігурації запуску у панелі Run and Debug.

**Варіант 2: Використання терміналу**

Встановіть токен як змінну оточення:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Запуск прикладів

**Використання VS Code:** Просто клацніть правою кнопкою миші на будь-якому демонстраційному файлі у Провіднику та виберіть **"Run Java"**, або використайте конфігурації запуску у панелі Run and Debug (переконайтеся, що спочатку додали свій токен у файл `.env`).

**Використання Maven:** Альтернативно можна запускати з командного рядка:

### 1. Базовий чат

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Шаблони підказок

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Показує нульовий та кілька прикладів, ланцюг мислення та рольові підказки.

### 3. Виклик функцій

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ШІ автоматично викликає ваші Java методи за потребою.

### 4. Запитання і відповіді по документу (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте питання про вміст `document.txt`.

### 5. Відповідальний ШІ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Перевіряйте, як фільтри безпеки ШІ блокують шкідливий контент.

## Що показує кожен приклад

**Базовий чат** — [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Починайте звідси, щоб побачити LangChain4j у найпростішому вигляді. Ви створите `OpenAiOfficialChatModel`, відправите підказку з `.chat()` і отримаєте відповідь. Це демонструє основу: як ініціалізувати модель з кастомним endpoint і ключем API. Коли зрозумієте цей патерн, все інше базуватиметься на ньому.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) та запитайте:
> - "Як мені переключитись з GitHub Models на Azure OpenAI у цьому коді?"
> - "Які інші параметри можна налаштувати в OpenAiOfficialChatModel.builder()?"
> - "Як додати потокову передачу відповіді замість очікування на повну відповідь?"

**Інженерія підказок** — [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Тепер, коли ви знаєте, як говорити з моделлю, розглянемо, що ви їй говорите. Цей демо-проект використовує ту ж модель, але показує п’ять різних шаблонів підказок. Спробуйте нульовий приклад для прямих інструкцій, декілька прикладів для навчання на прикладах, ланцюг мислення, що відкриває кроки роздумів, та рольові підказки для встановлення контексту. Ви побачите, як одна модель дає зовсім різні результати залежно від формулювання запиту.

Демонстрація також показує шаблони підказок, які є потужним способом створення повторно використовуваних підказок з змінними.
Приклад нижче показує підказку з використанням LangChain4j `PromptTemplate` для заповнення змінних. ШІ відповість на основі заданого місця призначення і активності.

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

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) і запитайте:
> - "У чому різниця між zero-shot та few-shot підказками, і коли що краще використовувати?"
> - "Як параметр temperature впливає на відповіді моделі?"
> - "Які є способи запобігання атак на підказки у продакшені?"
> - "Як створити повторно використовувані об’єкти PromptTemplate для поширених шаблонів?"

**Інтеграція інструментів** — [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ось де LangChain4j стає потужним. Ви використаєте `AiServices` для створення AI асистента, який може викликати ваші Java методи. Просто анотуйте методи `@Tool("опис")` і LangChain4j зробить усе інше — ШІ автоматично вибирає, коли використовувати кожен інструмент залежно від запиту користувача. Це демонструє виклик функцій — ключову техніку для створення AI, який може виконувати дії, а не лише відповідати на запитання.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) і запитайте:
> - "Як працює анотація @Tool і що робить LangChain4j з нею поза сценою?"
> - "Чи може ШІ викликати кілька інструментів послідовно для розв’язання складних задач?"
> - "Що відбувається, якщо інструмент кидає виключення — як краще обробляти помилки?"
> - "Як інтегрувати реальний API замість цього прикладу калькулятора?"

**Запитання і відповіді по документу (RAG)** — [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тут ви побачите основу RAG (retrieval-augmented generation). Замість покладення на тренувальні дані моделі, ви завантажуєте вміст з [`document.txt`](../../../00-quick-start/document.txt) і включаєте його в підказку. ШІ відповідає на основі вашого документу, а не загальних знань. Це перший крок до створення систем, що працюють з вашими власними даними.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Примітка:** Цей простий підхід завантажує весь документ у підказку. Для великих файлів (>10KB) ви перевищите обмеження контексту. Модуль 03 охоплює розбиття на частини і пошук за векторами для продакшн систем RAG.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) і запитайте:
> - "Як RAG запобігає галюцинаціям ШІ в порівнянні з використанням тренувальних даних моделі?"
> - "У чому різниця між цим простим підходом і використанням векторних ембеддингів для пошуку?"
> - "Як масштабувати це для роботи з кількома документами або більшими базами знань?"
> - "Які найкращі практики для структурування підказки, щоб забезпечити використання ШІ лише заданого контексту?"

**Відповідальний ШІ** — [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Забезпечте безпеку ШІ з багаторівневим захистом. У цьому демо показані два рівні захисту, що працюють разом:

**Частина 1: LangChain4j Input Guardrails** — Блокування небезпечних підказок перед тим, як вони потраплять до LLM. Створіть власні правила, що перевіряють заборонені ключові слова чи шаблони. Вони запускаються у вашому коді, тому швидкі й безкоштовні.

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

**Частина 2: Фільтри безпеки провайдера** — GitHub Models має вбудовані фільтри, які ловлять те, що можуть пропустити ваші guardrails. Ви бачите жорсткі блокування (помилки HTTP 400) для серйозних порушень та м’які відмови, де ШІ ввічливо відмовляється.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) та запитайте:
> - "Що таке InputGuardrail і як створити власний?"
> - "Яка різниця між жорстким блоком і м’якою відмовою?"
> - "Чому варто використовувати і guardrails, і фільтри провайдера разом?"

## Наступні кроки

**Наступний модуль:** [01-introduction - Початок роботи з LangChain4j та gpt-5 на Azure](../01-introduction/README.md)

---

**Навігація:** [← Назад до головної](../README.md) | [Далі: Модуль 01 - Вступ →](../01-introduction/README.md)

---

## Вирішення проблем

### Перший збір Maven

**Проблема:** Перший запуск `mvn clean compile` або `mvn package` займає багато часу (10-15 хвилин)

**Причина:** Maven потребує завантаження всіх залежностей проекту (Spring Boot, бібліотеки LangChain4j, Azure SDK тощо) під час першої збірки.

**Вирішення:** Це нормальна поведінка. Наступні збірки будуть значно швидшими, оскільки залежності кешуються локально. Час завантаження залежить від вашої швидкості інтернету.
### Синтаксис команди PowerShell для Maven

**Проблема**: Команди Maven не виконуються з помилкою `Unknown lifecycle phase ".mainClass=..."`

**Причина**: PowerShell інтерпретує `=` як оператор призначення змінної, порушуючи синтаксис властивостей Maven

**Рішення**: Використайте оператор зупинки парсингу `--%` перед командою Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` повідомляє PowerShell передати всі наступні аргументи дослівно Maven без інтерпретації.

### Відображення емодзі у Windows PowerShell

**Проблема**: У відповідях AI замість емодзі відображаються непотрібні символи (наприклад, `????` або `â??`) у PowerShell

**Причина**: За замовчуванням кодування PowerShell не підтримує UTF-8 емодзі

**Рішення**: Виконайте цю команду перед запуском Java-застосунків:
```cmd
chcp 65001
```

Це примусить використовувати кодування UTF-8 у терміналі. Як альтернативу можна використовувати Windows Terminal, який краще підтримує Юнікод.

### Налагодження викликів API

**Проблема**: Помилки автентифікації, обмеження за кількістю запитів або несподівані відповіді від AI-моделі

**Рішення**: У прикладах використані `.logRequests(true)` і `.logResponses(true)` для показу викликів API у консолі. Це допомагає усунути помилки автентифікації, обмеження за частотою або несподівані відповіді. Видаліть ці прапорці у продакшені для скорочення обсягу логів.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ був перекладений за допомогою сервісу штучного інтелекту [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматизовані переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендовано звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння або неправильне тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->