# Module 00: Швидкий старт

## Зміст

- [Вступ](../../../00-quick-start)
- [Що таке LangChain4j?](../../../00-quick-start)
- [Залежності LangChain4j](../../../00-quick-start)
- [Вимоги](../../../00-quick-start)
- [Налаштування](../../../00-quick-start)
  - [1. Отримайте ваш GitHub токен](../../../00-quick-start)
  - [2. Встановіть ваш токен](../../../00-quick-start)
- [Запуск прикладів](../../../00-quick-start)
  - [1. Базовий чат](../../../00-quick-start)
  - [2. Шаблони промптів](../../../00-quick-start)
  - [3. Виклик функцій](../../../00-quick-start)
  - [4. Запитання та відповіді по документах (Easy RAG)](../../../00-quick-start)
  - [5. Відповідальний ШІ](../../../00-quick-start)
- [Що показує кожний приклад](../../../00-quick-start)
- [Наступні кроки](../../../00-quick-start)
- [Вирішення проблем](../../../00-quick-start)

## Вступ

Цей швидкий старт призначений, щоб якомога швидше познайомити вас із LangChain4j. Він охоплює абсолютно базові речі для створення AI-застосунків з LangChain4j та GitHub Models. У наступних модулях ви перейдете на Azure OpenAI та GPT-5.2 і глибше вивчатимете кожну концепцію.

## Що таке LangChain4j?

LangChain4j — це Java бібліотека, яка спрощує створення застосунків із підтримкою AI. Замість роботи з HTTP клієнтами та парсингом JSON ви працюєте з чистими Java API.

"Chain" у LangChain означає з'єднання кількох компонентів в одну ланцюжок — ви можете поєднати промпт з моделлю, потім із парсером, або з'єднати кілька AI викликів, де вихід одного стає вхідними даними для наступного. Цей швидкий старт зосереджений на основах, перед тим як перейти до складніших ланцюжків.

<img src="../../../translated_images/uk/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Зʼєднання компонентів у LangChain4j – будівельні блоки обʼєднуються для створення потужних AI робочих процесів*

Ми використовуємо три ключові компоненти:

**ChatModel** — інтерфейс для взаємодії з AI моделями. Викликайте `model.chat("prompt")` і отримуйте рядок-відповідь. Ми використовуємо `OpenAiOfficialChatModel`, який працює з сумісними з OpenAI кінцевими точками, як GitHub Models.

**AiServices** — створює типобезпечні інтерфейси AI-сервісів. Визначайте методи, анотуйте їх за допомогою `@Tool`, і LangChain4j керує оркестрацією. AI автоматично викликає ваші Java методи за потреби.

**MessageWindowChatMemory** — зберігає історію розмови. Без цього кожен запит — незалежний. З ним AI пам’ятає попередні повідомлення і підтримує контекст кількох ходів.

<img src="../../../translated_images/uk/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архітектура LangChain4j — основні компоненти працюють разом, щоб забезпечити ваші AI-застосунки*

## Залежності LangChain4j

Цей швидкий старт використовує три Maven залежності у файлі [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модуль `langchain4j-open-ai-official` надає клас `OpenAiOfficialChatModel`, який підключається до API сумісних з OpenAI. GitHub Models використовує той самий формат API, тож спеціальний адаптер не потрібен — просто вкажіть базовий URL `https://models.github.ai/inference`.

Модуль `langchain4j-easy-rag` забезпечує автоматичне розбиття документів, ембеддинг і пошук, щоб ви могли створювати RAG-застосунки без ручного налаштування кожного кроку.

## Вимоги

**Використовуєте Dev Container?** Java та Maven уже встановлені. Вам потрібен лише GitHub Personal Access Token.

**Локальна розробка:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (інструкції нижче)

> **Примітка:** Цей модуль використовує `gpt-4.1-nano` від GitHub Models. Не змінюйте ім’я моделі в коді — вона налаштована для роботи з доступними моделями GitHub.

## Налаштування

### 1. Отримайте ваш GitHub токен

1. Перейдіть на [GitHub Налаштування → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Натисніть «Generate new token»
3. Вкажіть описове ім’я (наприклад, "LangChain4j Demo")
4. Встановіть термін дії (рекомендовано 7 днів)
5. У розділі «Account permissions» знайдіть «Models» і встановіть «Read-only»
6. Натисніть «Generate token»
7. Скопіюйте та збережіть ваш токен — більше побачити його не зможете

### 2. Встановіть ваш токен

**Варіант 1: Використання VS Code (рекомендується)**

Якщо ви користуєтеся VS Code, додайте ваш токен у файл `.env` у корені проєкту:

Якщо файлу `.env` не існує, скопіюйте `.env.example` у `.env` або створіть новий файл `.env` у корені проекту.

**Приклад файлу `.env`:**
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Після цього ви можете просто клацнути правою кнопкою на будь-якому демонстраційному файлі (наприклад, `BasicChatDemo.java`) у Проводнику і вибрати **"Run Java"** або використати конфігурації запуску у панелі Run and Debug.

**Варіант 2: Використання терміналу**

Встановіть токен як змінну середовища:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Запуск прикладів

**Використання VS Code:** Просто клікніть правою кнопкою на будь-якому демонстраційному файлі у Проводнику і виберіть **"Run Java"**, або використайте конфігурації запуску з панелі Run and Debug (переконайтеся, що токен доданий у `.env`).

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

### 2. Шаблони промптів

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Показує zero-shot, few-shot, chain-of-thought та рольові шаблони промптів.

### 3. Виклик функцій

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI автоматично викликає ваші Java методи за необхідності.

### 4. Запитання та відповіді по документах (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте питання щодо ваших документів, користуючись Easy RAG із автоматичним ембеддінгом та пошуком.

### 5. Відповідальний ШІ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Подивіться, як фільтри безпеки ШІ блокують шкідливий контент.

## Що показує кожний приклад

**Базовий чат** — [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Почніть тут, щоб побачити LangChain4j у найпростішому вигляді. Ви створите `OpenAiOfficialChatModel`, надішлете промпт методом `.chat()` і отримаєте відповідь. Це демонструє основу: як ініціалізувати моделі з кастомними кінцевими точками та API ключами. Як тільки зрозумієте цей патерн, все інше будуватиметься на ньому.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) і запитайте:
> - "Як переключитися з GitHub Models на Azure OpenAI у цьому коді?"
> - "Які ще параметри можна налаштувати в OpenAiOfficialChatModel.builder()?"
> - "Як додати потокові відповіді замість очікування повної відповіді?"

**Інженерія промптів** — [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Тепер, коли ви знаєте, як спілкуватися з моделлю, давайте розглянемо, що саме їй говорити. Цей демо-приклад використовує ту ж модель, але показує п’ять різних патернів промптів. Спробуйте zero-shot промпти для прямих інструкцій, few-shot, що навчаються на прикладах, chain-of-thought — щоб демонструвати кроки мислення, і рольові промпти, що встановлюють контекст. Ви побачите, як одна й та сама модель дає кардинально різні результати залежно від формулювання запиту.

Демонстрація також показує шаблони промптів — потужний спосіб створювати багаторазові промпти з змінними.
Нижче наведено приклад промпта з використанням LangChain4j `PromptTemplate` для заповнення змінних. ШІ відповідатиме, ґрунтуючись на наданому пункті призначення та дії.

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
> - "У чому різниця між zero-shot і few-shot промптами, і коли який використовувати?"
> - "Як параметр temperature впливає на відповіді моделі?"
> - "Які є техніки для запобігання атак промпт-інжекцій у продакшні?"
> - "Як створювати багаторазові обʼєкти PromptTemplate для загальних патернів?"

**Інтеграція інструментів** — [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ось де LangChain4j стає потужним. Ви використаєте `AiServices` для створення AI асистента, який може викликати ваші Java методи. Просто анотуйте методи `@Tool("опис")` і LangChain4j потурбується про решту — AI автоматично вирішує, коли використовувати кожен інструмент згідно з тим, що просить користувач. Це демонструє виклик функцій — ключову техніку для створення AI, що може не тільки відповідати на питання, а й виконувати дії.

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

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) і запитайте:
> - "Як працює анотація @Tool і що LangChain4j робить з нею за лаштунками?"
> - "Чи може AI викликати кілька інструментів послідовно для розв’язання складних проблем?"
> - "Що станеться, якщо інструмент кине виключення — як обробляти помилки?"
> - "Як інтегрувати реальне API замість цього прикладу калькулятора?"

**Запитання та відповіді по документах (Easy RAG)** — [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тут ви побачите RAG (retrieval-augmented generation) за допомогою підходу LangChain4j "Easy RAG". Документи завантажуються, автоматично розбиваються і вбудовуються в пам’ять, а потім пошуковик доставляє релевантні частини до AI під час запиту. AI відповідає на основі ваших документів, а не загальних знань.

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

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) і запитайте:
> - "Як RAG запобігає галюцинаціям AI у порівнянні з використанням тренувальних даних моделі?"
> - "У чому різниця між цим легким підходом і власним RAG пайплайном?"
> - "Як масштабувати це для роботи з кількома документами або більшими базами знань?"

**Відповідальний ШІ** — [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Побудуйте безпеку AI за принципом defense in depth. Цей приклад показує два рівні захисту, які працюють разом:

**Частина 1: Вхідні обмеження LangChain4j** — Блокуйте небезпечні промпти, перш ніж вони дійдуть до LLM. Створюйте власні обмеження, що перевіряють заборонені ключові слова або шаблони. Вони працюють у вашому коді, тому швидкі і безкоштовні.

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

**Частина 2: Фільтри безпеки провайдера** — GitHub Models має вбудовані фільтри, що ловлять те, що ваші обмеження можуть пропустити. Ви побачите жорстке блокування (помилки HTTP 400) для серйозних порушень і м’які відмови, коли AI ввічливо відмовляється.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) і запитайте:
> - "Що таке InputGuardrail і як створити власний?"
> - "У чому різниця між жорстким блокуванням та м’якою відмовою?"
> - "Чому використовувати обмеження і фільтри провайдера разом?"

## Наступні кроки

**Наступний модуль:** [01-introduction - Початок роботи з LangChain4j](../01-introduction/README.md)

---

**Навігація:** [← Назад до головної](../README.md) | [Далі: Модуль 01 - Вступ →](../01-introduction/README.md)

---

## Вирішення проблем

### Перший збір Maven

**Проблема:** Початковий `mvn clean compile` або `mvn package` триває довго (10-15 хвилин)

**Причина:** Maven має завантажити всі залежності проєкту (Spring Boot, бібліотеки LangChain4j, Azure SDK тощо) під час першої збірки.

**Рішення:** Це нормальна поведінка. Наступні збірки будуть значно швидшими, бо залежності кешуються локально. Час завантаження залежить від швидкості вашого інтернету.

### Синтаксис Maven команд у PowerShell

**Проблема:** Maven команди падають з помилкою `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell інтерпретує `=` як оператор присвоєння змінної, що порушує синтаксис властивостей Maven

**Рішення**: Використовуйте оператор припинення розбору `--%` перед командою Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` вказує PowerShell передавати всі наступні аргументи буквально Maven без інтерпретації.

### Відображення емодзі у Windows PowerShell

**Проблема**: Відповіді AI показують нечитаємі символи (наприклад, `????` або `â??`) замість емодзі у PowerShell

**Причина**: За замовчуванням кодування PowerShell не підтримує UTF-8 емодзі

**Рішення**: Виконайте цю команду перед запуском Java-додатків:
```cmd
chcp 65001
```

Це примусить термінал використовувати кодування UTF-8. Альтернативно, використовуйте Windows Terminal, який має кращу підтримку Unicode.

### Налагодження викликів API

**Проблема**: Помилки автентифікації, обмеження частоти або несподівані відповіді від AI-моделі

**Рішення**: Приклади містять `.logRequests(true)` і `.logResponses(true)` для відображення викликів API у консолі. Це допомагає усунути помилки автентифікації, обмеження частоти або несподівані відповіді. Видаліть ці прапорці у продакшн, щоб зменшити шум у логах.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, просимо враховувати, що автоматичні переклади можуть містити помилки або неточності. Оригінал документа мовою оригіналу слід вважати авторитетним джерелом. Для критичної інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->