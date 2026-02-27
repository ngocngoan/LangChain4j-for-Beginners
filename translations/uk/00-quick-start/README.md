# Module 00: Швидкий старт

## Зміст

- [Вступ](../../../00-quick-start)
- [Що таке LangChain4j?](../../../00-quick-start)
- [Залежності LangChain4j](../../../00-quick-start)
- [Необхідні умови](../../../00-quick-start)
- [Налаштування](../../../00-quick-start)
  - [1. Отримайте свій токен GitHub](../../../00-quick-start)
  - [2. Встановіть свій токен](../../../00-quick-start)
- [Запуск прикладів](../../../00-quick-start)
  - [1. Базовий чат](../../../00-quick-start)
  - [2. Шаблони підказок](../../../00-quick-start)
  - [3. Виклики функцій](../../../00-quick-start)
  - [4. Питання та відповіді з документами (Easy RAG)](../../../00-quick-start)
  - [5. Відповідальний ШІ](../../../00-quick-start)
- [Що показує кожен приклад](../../../00-quick-start)
- [Наступні кроки](../../../00-quick-start)
- [Вирішення проблем](../../../00-quick-start)

## Вступ

Цей швидкий старт створений, щоб якнайшвидше допомогти вам почати працювати з LangChain4j. Він охоплює абсолютні основи створення додатків зі штучним інтелектом за допомогою LangChain4j і моделей GitHub. У наступних модулях ви використаєте Azure OpenAI з LangChain4j для створення більш складних додатків.

## Що таке LangChain4j?

LangChain4j — це бібліотека Java, яка спрощує створення додатків із використанням ШІ. Замість роботи з HTTP-клієнтами та парсингом JSON ви працюєте з чистими Java API.

«Ланцюжок» у LangChain означає послідовне з'єднання кількох компонентів — ви можете зв’язати підказку з моделлю та парсером, або кілька викликів ШІ, де один результат слугує вхідними даними для наступного. Цей швидкий старт зосереджений на фундаменті, перш ніж перейти до більш складних ланцюжків.

<img src="../../../translated_images/uk/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Зв’язування компонентів у LangChain4j — будівельні блоки поєднуються для створення потужних робочих процесів ШІ*

Ми використовуємо три основні компоненти:

**ChatModel** — інтерфейс для взаємодії з моделями ШІ. Викликаєте `model.chat("prompt")` і отримуєте відповідь у вигляді рядка. Ми використовуємо `OpenAiOfficialChatModel`, який працює з сумісними з OpenAI кінцевими точками, такими як моделі GitHub.

**AiServices** — створює типобезпечні інтерфейси сервісів ШІ. Визначаєте методи, анотуєте їх `@Tool`, і LangChain4j керує оркестрацією. ШІ автоматично викликає ваші методи Java за потреби.

**MessageWindowChatMemory** — підтримує історію розмови. Без неї кожен запит є незалежним. З нею ШІ запам’ятовує попередні повідомлення і зберігає контекст у кількох ходах.

<img src="../../../translated_images/uk/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архітектура LangChain4j — основні компоненти працюють разом, щоб забезпечити потужність ваших додатків ШІ*

## Залежності LangChain4j

Цей швидкий старт використовує три залежності Maven у файлі [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модуль `langchain4j-open-ai-official` надає клас `OpenAiOfficialChatModel`, який підключається до API, сумісних з OpenAI. Моделі GitHub використовують той самий формат API, тому не потрібен спеціальний адаптер — просто вкажіть базовий URL `https://models.github.ai/inference`.

Модуль `langchain4j-easy-rag` забезпечує автоматичне розбиття документів, вставлення векторів та пошук, щоб ви могли створювати RAG-додатки без налаштування кожного кроку вручну.

## Необхідні умови

**Використовуєте Dev Container?** Java та Maven вже встановлені. Вам потрібен лише персональний токен доступу GitHub.

**Локальна розробка:**
- Java 21+, Maven 3.9+
- Персональний токен доступу GitHub (інструкції нижче)

> **Примітка:** цей модуль використовує `gpt-4.1-nano` з моделей GitHub. Не змінюйте назву моделі в коді — вона налаштована на роботу із доступними моделями GitHub.

## Налаштування

### 1. Отримайте свій токен GitHub

1. Перейдіть до [GitHub Налаштування → Персональні токени доступу](https://github.com/settings/personal-access-tokens)
2. Натисніть "Generate new token"
3. Вкажіть описову назву (наприклад, "LangChain4j Demo")
4. Встановіть термін дії (рекомендується 7 днів)
5. У розділі "Account permissions" знайдіть "Models" та встановіть "Read-only"
6. Натисніть "Generate token"
7. Скопіюйте та збережіть токен — більше ви його не побачите

### 2. Встановіть свій токен

**Варіант 1: Використання VS Code (рекомендовано)**

Якщо ви користуєтесь VS Code, додайте токен у файл `.env` у корені проекту:

Якщо файлу `.env` немає, скопіюйте `.env.example` у `.env` або створіть новий файл `.env` у корені проекту.

**Приклад файлу `.env`:**  
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Потім просто клацніть правою кнопкою миші по будь-якому демонстраційному файлу (наприклад, `BasicChatDemo.java`) у Провіднику і виберіть **"Run Java"** або скористайтеся конфігураціями запуску у панелі Run and Debug.

**Варіант 2: Через Терминал**

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

**У VS Code:** Просто клацніть правою кнопкою миші по будь-якому демонстраційному файлу у Провіднику та виберіть **"Run Java"**, або скористайтеся конфігураціями запуску у панелі Run and Debug (пам’ятайте, що спершу треба додати токен у файл `.env`).

**Через Maven:** Також можна запустити з командного рядка:

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
  
Показує нульовий та кілька прикладів, ланцюжки думок і підказки на основі ролі.

### 3. Виклики функцій

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
ШІ автоматично викликає ваші методи Java за потреби.

### 4. Питання та відповіді з документами (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Задавайте питання про свої документи, використовуючи Easy RAG з автоматичним вставленням та пошуком.

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

## Що показує кожен приклад

**Базовий чат** — [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Почніть тут, щоб побачити LangChain4j у найпростішому вигляді. Ви створите `OpenAiOfficialChatModel`, відправите підказку через `.chat()` і отримаєте відповідь. Це демонструє основу: як ініціалізувати моделі з власними кінцевими точками та API-ключами. Як тільки зрозумієте цей паттерн, усе інше побудується на ньому.

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
> - "Як переключитися з моделей GitHub на Azure OpenAI в цьому коді?"  
> - "Які інші параметри можна налаштувати в OpenAiOfficialChatModel.builder()?"  
> - "Як додати потокові відповіді замість очікування повної відповіді?"

**Інженерія підказок** — [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Тепер, коли ви знаєте, як звертатися до моделі, давайте подивимось, що саме ви їй кажете. Ця демонстрація використовує ту саму модель, але показує п’ять різних шаблонів підказок. Спробуйте нульові підказки для прямих інструкцій, кілька прикладів для навчання на прикладах, ланцюг думок для розкриття логіки й підказки на основі ролі, які задають контекст. Ви побачите, як одна і та сама модель дає кардинально різні результати, залежно від формулювання запиту.

Також демонструються шаблони підказок, які є потужним інструментом для створення багаторазових підказок з використанням змінних.  
Нижче наведено приклад підказки з LangChain4j `PromptTemplate` для заповнення змінних. ШІ відповість на основі вказаного місця призначення та діяльності.

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
> - "У чому різниця між нульовими та кількома прикладами, і коли що використовувати?"  
> - "Як параметр temperature впливає на відповіді моделі?"  
> - "Які методи уникнути атак через ін’єкції у підказки у продакшені?"  
> - "Як створювати багаторазові об’єкти PromptTemplate для поширених шаблонів?"

**Інтеграція інструментів** — [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ось де LangChain4j стає потужним. Ви використаєте `AiServices` для створення помічника ШІ, який може викликати ваші методи Java. Просто анотуйте методи за допомогою `@Tool("опис")`, а LangChain4j подбає про решту — ШІ автоматично приймає рішення, коли і який інструмент використовувати, залежно від запиту користувача. Це демонструє виклики функцій — ключову техніку для створення ШІ, що може виконувати дії, а не тільки відповідати на питання.

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
> - "Як працює анотація @Tool і що LangChain4j робить з нею?"  
> - "Чи може ШІ викликати кілька інструментів послідовно для розв’язання складних задач?"  
> - "Що відбувається, якщо інструмент викликає виняток — як обробляти помилки?"  
> - "Як інтегрувати реальне API замість цього прикладу з калькулятором?"

**Питання та відповіді з документами (Easy RAG)** — [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Тут ви побачите RAG (retrieval-augmented generation) за допомогою підходу LangChain4j "Easy RAG". Документи завантажуються, автоматично розбиваються і вставляються в пам’яті, після чого ретрівер видає релевантні фрагменти ШІ під час запиту. ШІ відповідає на основі ваших документів, а не загальних знань.

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
> - "Як RAG запобігає галюцинаціям ШІ в порівнянні з використанням навчальних даних моделі?"  
> - "У чому різниця між цим легким підходом і кастомним конвеєром RAG?"  
> - "Як масштабувати це для роботи з багатьма документами або більшими базами знань?"

**Відповідальний ШІ** — [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Будуйте безпеку ШІ за принципом захисту в глибину. У цій демонстрації показано два рівні захисту, які працюють разом:

**Частина 1: Обмеження вводу LangChain4j** — блокує небезпечні підказки до того, як вони досягнуть LLM. Створюйте власні обмеження, які перевіряють заборонені ключові слова чи патерни. Вони виконуються у вашому коді, тому швидкі та безкоштовні.

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
  
**Частина 2: Фільтри безпеки провайдера** — GitHub Models мають вбудовані фільтри, що ловлять те, що можуть пропустити ваші обмеження. Ви побачите жорсткі блоки (помилки HTTP 400) для серйозних порушень і м’які відмови, коли ШІ ввічливо відмовляється.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Відкрийте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) і запитайте:  
> - "Що таке InputGuardrail і як створити власний?"  
> - "У чому різниця між жорстким блоком і м’якою відмовою?"  
> - "Навіщо використовувати і обмеження вводу, і фільтри провайдера разом?"

## Наступні кроки

**Наступний модуль:** [01-introduction - Початок роботи з LangChain4j та gpt-5 на Azure](../01-introduction/README.md)

---

**Навігація:** [← Повернутися до головної](../README.md) | [Далі: Модуль 01 - Вступ →](../01-introduction/README.md)

---

## Вирішення проблем

### Перша збірка Maven

**Проблема:** Перша команда `mvn clean compile` або `mvn package` виконується довго (10-15 хвилин)

**Причина:** Maven потрібно завантажити всі залежності проєкту (Spring Boot, бібліотеки LangChain4j, Azure SDK тощо) під час першої збірки.

**Вирішення:** Це нормальна поведінка. Наступні збірки будуть набагато швидшими, оскільки залежності кешуються локально. Час завантаження залежить від швидкості вашого інтернет-з’єднання.

### Синтаксис команд Maven у PowerShell

**Проблема:** Команди Maven видають помилку `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell тлумачить `=` як оператор присвоєння змінної, що порушує синтаксис властивостей Maven

**Рішення**: Використовуйте оператор припинення обробки `--%` перед командою Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` вказує PowerShell передати всі наступні аргументи літерально до Maven без інтерпретації.

### Відображення емодзі в Windows PowerShell

**Проблема**: Відповіді ШІ відображаються як некоректні символи (наприклад, `????` або `â??`) замість емодзі у PowerShell

**Причина**: Кодування за замовчуванням у PowerShell не підтримує UTF-8 емодзі

**Рішення**: Запустіть цю команду перед виконанням Java-додатків:
```cmd
chcp 65001
```

Це змушує термінал використовувати кодування UTF-8. Альтернативно, використовуйте Windows Terminal, який має кращу підтримку Unicode.

### Налагодження викликів API

**Проблема**: Помилки автентифікації, обмеження частоти або несподівані відповіді від моделі ШІ

**Рішення**: У прикладах включено `.logRequests(true)` та `.logResponses(true)` для відображення викликів API у консолі. Це допомагає виявити помилки автентифікації, обмеження частоти або несподівані відповіді. Видаліть ці прапорці у продакшені, щоб зменшити шум у логах.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний переклад кваліфікованим фахівцем. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->