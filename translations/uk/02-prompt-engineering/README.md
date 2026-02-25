# Модуль 02: Розробка запитів з GPT-5.2

## Зміст

- [Чому ви навчитеся](../../../02-prompt-engineering)
- [Передумови](../../../02-prompt-engineering)
- [Розуміння розробки запитів](../../../02-prompt-engineering)
- [Основи розробки запитів](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Розширені шаблони](../../../02-prompt-engineering)
- [Використання існуючих ресурсів Azure](../../../02-prompt-engineering)
- [Скріншоти додатку](../../../02-prompt-engineering)
- [Вивчення шаблонів](../../../02-prompt-engineering)
  - [Низька vs висока готовність](../../../02-prompt-engineering)
  - [Виконання завдань (підготовчі тексти інструментів)](../../../02-prompt-engineering)
  - [Саморефлексія коду](../../../02-prompt-engineering)
  - [Структурований аналіз](../../../02-prompt-engineering)
  - [Багатокрокове спілкування](../../../02-prompt-engineering)
  - [Покрокове міркування](../../../02-prompt-engineering)
  - [Обмежений вихід](../../../02-prompt-engineering)
- [Що ви насправді вивчаєте](../../../02-prompt-engineering)
- [Наступні кроки](../../../02-prompt-engineering)

## Чому ви навчитеся

<img src="../../../translated_images/uk/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

У попередньому модулі ви побачили, як пам’ять підтримує розмовний AI, і використовували моделі GitHub для базових взаємодій. Тепер ми зосередимося на тому, як ви ставите запитання — тобто самих запитах — з використанням GPT-5.2 у Azure OpenAI. Те, як ви структуруєте свої запити, суттєво впливає на якість отриманих відповідей. Спочатку ми переглянемо основні методики розробки запитів, а потім перейдемо до восьми розширених шаблонів, що повністю використовують можливості GPT-5.2.

Ми використовуватимемо GPT-5.2, бо він вводить керування міркуваннями — ви можете вказати моделі, скільки часу думати перед відповіддю. Це робить різні стратегії розробки запитів більш помітними і допомагає зрозуміти, коли застосовувати кожен підхід. Також перевагою є менша кількість обмежень швидкості в Azure для GPT-5.2 порівняно з моделями GitHub.

## Передумови

- Завершений Модуль 01 (розгорнуті ресурси Azure OpenAI)
- Файл `.env` у кореневій теці з обліковими даними Azure (створений командою `azd up` у Модулі 01)

> **Примітка:** Якщо ви не завершили Модуль 01, спочатку виконайте інструкції з розгортання там.

## Розуміння розробки запитів

<img src="../../../translated_images/uk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Розробка запитів полягає у створенні вхідного тексту, який послідовно дає потрібні результати. Це не просто про запитання — це про структурування запитів так, щоб модель чітко розуміла, що ви хочете і як це надати.

Уявіть, що ви даєте інструкції колезі. "Виправити помилку" — це загально. "Виправити виняток null pointer у UserService.java на рядку 45, додавши перевірку null" — конкретно. Мовні моделі працюють так само — важлива конкретність і структура.

<img src="../../../translated_images/uk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j забезпечує інфраструктуру — з’єднання з моделями, пам’ять і типи повідомлень, тоді як шаблони запитів — це просто ретельно структурований текст, який ви надсилаєте цією інфраструктурою. Ключові будівельні блоки — `SystemMessage` (встановлює поведінку та роль ШІ) і `UserMessage` (містить ваш фактичний запит).

## Основи розробки запитів

<img src="../../../translated_images/uk/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Перед зануренням у розширені шаблони цього модуля давайте переглянемо п’ять основних технік розробки запитів. Це будівельні блоки, які має знати кожен інженер запитів. Якщо ви вже пройшли [швидкий старт](../00-quick-start/README.md#2-prompt-patterns), ви бачили їх у дії — ось концептуальна база за ними.

### Zero-Shot Prompting

Найпростіший підхід: дайте моделі пряму інструкцію без прикладів. Модель повністю покладається на своє навчання, щоб зрозуміти і виконати завдання. Це працює добре для простих запитів, де очікувана поведінка очевидна.

<img src="../../../translated_images/uk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Пряма інструкція без прикладів — модель робить висновок про завдання лише з інструкції*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Відповідь: "Позитивно"
```

**Коли використовувати:** Простий класифікації, прямі запитання, переклади або будь-яке завдання, яке модель може виконати без додаткових вказівок.

### Few-Shot Prompting

Надайте приклади, які демонструють патерн, який ви хочете, щоб модель виконувала. Модель вивчає очікуваний формат вхідних та вихідних даних на основі ваших прикладів і застосовує його до нових запитів. Це суттєво покращує послідовність для завдань, де бажаний формат або поведінка неочевидні.

<img src="../../../translated_images/uk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Навчання на прикладах — модель визначає патерн і застосовує його до нових вхідних даних*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Коли використовувати:** Кастомні класифікації, послідовне форматування, специфічні для домену завдання або коли результати Zero-Shot непослідовні.

### Chain of Thought

Попросіть модель показати її міркування покроково. Замість того, щоб одразу давати відповідь, модель розбиває проблему і працює по кожній частині явно. Це покращує точність у математиці, логіці та багатокрокових задачах.

<img src="../../../translated_images/uk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Покрокове міркування — розбиття складних проблем на явні логічні кроки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показує: 15 - 8 = 7, потім 7 + 12 = 19 яблук
```

**Коли використовувати:** Математичні задачі, логічні головоломки, налагодження коду або будь-яке завдання, де виявлення процесу міркування підвищує точність і довіру.

### Role-Based Prompting

Встановіть персону або роль AI перед тим, як задати запитання. Це надає контекст, який формує тон, глибину і фокус відповіді. "Архітектор програмного забезпечення" дає інші поради ніж "молодший розробник" або "аудитор безпеки".

<img src="../../../translated_images/uk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Встановлення контексту і персони — на одне й те саме запитання відповідь буде різною залежно від призначеної ролі*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Коли використовувати:** Огляди коду, навчання, доменні аналізи або коли потрібні відповіді, адаптовані до певного рівня експертизи чи перспективи.

### Prompt Templates

Створюйте повторно використовувані запити з змінними-заповнювачами. Замість того, щоб щоразу писати новий запит, визначте шаблон один раз і заповнюйте різними значеннями. Клас `PromptTemplate` у LangChain4j робить це легко із синтаксисом `{{variable}}`.

<img src="../../../translated_images/uk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Повторно використовувані запити із змінними-заповнювачами — один шаблон, багато використань*

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

**Коли використовувати:** Повторювані запити з різними вхідними даними, пакетна обробка, побудова повторно використовуваних AI-процесів або будь-який сценарій, де структура запиту незмінна, а дані змінюються.

---

Ці п’ять основ дають міцний набір інструментів для більшості завдань розробки запитів. Решта модуля розвиває їх із допомогою **восьми розширених шаблонів**, які використовують керування міркуваннями, самооцінку і структурування вихідних даних GPT-5.2.

## Розширені шаблони

Після вивчення основ перейдемо до восьми розширених шаблонів, які роблять цей модуль унікальним. Не всі задачі потребують однакового підходу. Деякі питання потребують швидких відповідей, інші — глибоких роздумів. Дехто хоче бачити процес міркування, а інші — лише результати. Кожен із цих шаблонів оптимізований для певних сценаріїв — а керування міркуваннями GPT-5.2 робить ці відмінності ще помітнішими.

<img src="../../../translated_images/uk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Огляд восьми шаблонів розробки запитів та їх випадків використання*

<img src="../../../translated_images/uk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Керування міркуваннями GPT-5.2 дозволяє вказати, скільки часу модель має думати — від швидких прямих відповідей до глибокого аналізу*

**Низька готовність (швидко й цілеспрямовано)** — для простих запитань, де потрібна швидка, пряма відповідь. Модель робить мінімум міркувань — максимум 2 кроки. Використовуйте для обчислень, пошуку або простих запитань.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Досліджуйте з GitHub Copilot:** Відкрийте [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) і запитайте:
> - "Чим відрізняються шаблони з низькою та високою готовністю?"
> - "Як XML теги в запитах допомагають структурувати відповідь AI?"
> - "Коли краще використовувати шаблони саморефлексії, а коли прямі інструкції?"

**Висока готовність (глибоко й ретельно)** — для складних задач, де потрібен комплексний аналіз. Модель ґрунтовно досліджує і показує детальні міркування. Використовуйте для системного дизайну, архітектурних рішень або складних досліджень.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Виконання завдань (покроковий прогрес)** — для багатокрокових процесів. Модель надає план наперед, описує кожен крок у процесі і потім підбиває підсумки. Використовуйте для міграцій, імплементацій або будь-яких багатокрокових завдань.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought промптинг явно просить модель показати міркування, що підвищує точність у складних завданнях. Покроковий підхід допомагає і людям, і ШІ зрозуміти логіку.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Запитайте про цей шаблон:
> - "Як адаптувати шаблон виконання завдань для тривалих операцій?"
> - "Які кращі практики структурування підготовчих текстів інструментів у продакшн-застосунках?"
> - "Як відслідковувати і відображати проміжні оновлення прогресу в UI?"

<img src="../../../translated_images/uk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*План → Виконання → Підсумок для багатокрокових задач*

**Саморефлексуючий код** — для створення коду виробничої якості. Модель генерує код із урахуванням виробничих стандартів і правильною обробкою помилок. Використовуйте при створенні нових функцій чи сервісів.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ітеративний цикл покращення – генеруй, оцінюй, ідентифікуй проблеми, покращуй, повторюй*

**Структурований аналіз** — для послідовної оцінки. Модель перевіряє код за фреймворком (коректність, практики, продуктивність, безпека, підтримуваність). Використовуйте для оглядів коду або оцінки якості.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Запитайте про структурований аналіз:
> - "Як налаштувати фреймворк аналізу для різних типів оглядів коду?"
> - "Який найкращий спосіб парсити та програмно обробляти структурований вихід?"
> - "Як забезпечити послідовність рівнів важливості в різних сеансах огляду?"

<img src="../../../translated_images/uk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Фреймворк для послідовних оглядів коду із рівнями важливості*

**Багатокрокове спілкування** — для діалогів, що потребують контексту. Модель пам’ятає попередні повідомлення і розвиває їх далі. Використовуйте для інтерактивної допомоги чи складних Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/uk/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Як контекст розмови акумулюється з багатьох ходів досягнення ліміту токенів*

**Покрокове міркування** — для задач, що потребують видимої логіки. Модель демонструє явне міркування для кожного кроку. Використовуйте для математичних задач, логічних головоломок або коли потрібно зрозуміти хід думок.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Розбір складних проблем на явні логічні кроки*

**Обмежений вихід** — для відповідей з конкретними вимогами до формату. Модель жорстко дотримується форматів і обмежень довжини. Використовуйте для резюме або коли потрібна точна структура вихідних даних.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Дотримання конкретних вимог до формату, довжини та структури*

## Використання існуючих ресурсів Azure

**Перевірте розгортання:**

Переконайтеся, що файл `.env` існує в кореневій теці з обліковими даними Azure (створений у Модулі 01):
```bash
cat ../.env  # Має показувати AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запуск застосунку:**

> **Примітка:** Якщо ви вже запускали всі застосунки за допомогою `./start-all.sh` з Модуля 01, цей модуль вже працює на порту 8083. Можете пропустити команди запуску та перейти безпосередньо до http://localhost:8083.

**Варіант 1: Використання Spring Boot Dashboard (рекомендується для користувачів VS Code)**

Контейнер для розробки включає розширення Spring Boot Dashboard, яке надає візуальний інтерфейс для керування усіма застосунками Spring Boot. Ви знайдете його в Activity Bar ліворуч у VS Code (значок Spring Boot).

З Spring Boot Dashboard ви можете:
- Переглядати всі доступні застосунки Spring Boot у робочій області
- Запускати/зупиняти застосунки одним кліком
- Переглядати логи застосунків у режимі реального часу
- Контролювати стан застосунку
Просто натисніть кнопку відтворення поруч із "prompt-engineering", щоб розпочати цей модуль, або запустіть усі модулі одразу.

<img src="../../../translated_images/uk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Варіант 2: Використання shell-скриптів**

Запустіть усі веб-застосунки (модулі 01-04):

**Bash:**
```bash
cd ..  # З кореневого каталогу
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # З кореневого каталогу
.\start-all.ps1
```

Або запустіть лише цей модуль:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Обидва скрипти автоматично завантажують змінні оточення з кореневого файлу `.env` і будуватимуть JAR-файли, якщо їх не існує.

> **Примітка:** Якщо ви віддаєте перевагу вручну зібрати всі модулі перед запуском:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Відкрийте http://localhost:8083 у вашому браузері.

**Щоб зупинити:**

**Bash:**
```bash
./stop.sh  # Тільки цей модуль
# Або
cd .. && ./stop-all.sh  # Всі модулі
```

**PowerShell:**
```powershell
.\stop.ps1  # Лише цей модуль
# Або
cd ..; .\stop-all.ps1  # Всі модулі
```

## Знімки екрана застосунку

<img src="../../../translated_images/uk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Головна панель із усіма 8 шаблонами prompt engineering з їх характеристиками та випадками використання*

## Вивчення шаблонів

Веб-інтерфейс дає змогу експериментувати з різними стратегіями формування запитів. Кожен шаблон розв’язує різні проблеми — спробуйте їх, щоб побачити, коли кожен підхід є найкращим.

> **Примітка: Потокове відтворення проти непотокового** — Кожна сторінка шаблону має дві кнопки: **🔴 Потокова відповідь (У режимі реального часу)** та опцію **Без потоків**. Потокове відтворення використовує Server-Sent Events (SSE) для відображення токенів у режимі реального часу, поки модель їх генерує, тож ви бачите прогрес миттєво. Опція без потоків чекає повної відповіді перед відображенням. Для запитів, що потребують глибокого логічного аналізу (наприклад, Висока Відданість, Самоаналіз коду), виклик без потоків може зайняти дуже багато часу — іноді хвилини — без видимого зворотного зв’язку. **Використовуйте потокове відтворення при експериментах із складними запитами**, щоб бачити, як модель працює, і уникнути враження, що запит завис.
>
> **Примітка: Вимоги до браузера** — Функція потокового відтворення використовує Fetch Streams API (`response.body.getReader()`), який потребує повноцінного браузера (Chrome, Edge, Firefox, Safari). Вона **не працює** у вбудованому Simple Browser VS Code, оскільки його webview не підтримує ReadableStream API. Якщо ви користуєтеся Simple Browser, кнопки без потоків працюватимуть як завжди — лише кнопку потокового відтворення буде недоступно. Відкрийте `http://localhost:8083` у зовнішньому браузері для повного досвіду.

### Низька vs Висока Відданість

Поставте просте запитання на кшталт "Які 15% від 200?" у режимі Низької Відданості. Ви отримаєте швидку, прямолінійну відповідь. Тепер поставте складне, наприклад "Спроєктуй стратегію кешування для високонавантаженого API" із Високою Відданістю. Натисніть **🔴 Потокова відповідь (У режимі реального часу)** і спостерігайте, як модель детально розмірковує, показуючи токен за токеном. Та сама модель, та сама структура запиту — але промпт визначає, скільки зусиль докласти для роздумів.

### Виконання завдань (преамбули інструментів)

Багатокрокові робочі процеси виграють від попереднього планування і коментарів прогресу. Модель описує, що вона зробить, прослідковує кожен крок, а потім підсумовує результати.

### Самоаналіз коду

Спробуйте "Створити сервіс перевірки електронної пошти". Замість просто згенерувати код і зупинитись, модель генерує, оцінює відповідно до критеріїв якості, ідентифікує слабкі місця і покращує. Ви побачите, як вона ітерує, доки код не відповідає виробничим стандартам.

### Структурний аналіз

Код-рев’ю потребують послідовних рамок оцінки. Модель аналізує код у фіксованих категоріях (коректність, практики, продуктивність, безпека) зі ступенями тяжкості.

### Багатокроковий чат

Запитайте "Що таке Spring Boot?" і одразу ж продовжте "Покажи мені приклад". Модель запам’ятовує ваше перше питання і надає конкретний приклад Spring Boot саме для нього. Без пам’яті друге питання було б надто розпливчастим.

### Крок за кроком із роздумами

Виберіть математичну задачу і спробуйте її розв’язати як із підходом Крок за кроком, так і з Низькою Відданістю. Низька відданість дає відповідь швидко, але без деталізації. Підхід крок за кроком показує всі обчислення і прийняття рішень.

### Обмежений вивід

Якщо вам потрібні специфічні формати або чисельність слів, цей шаблон забезпечує суворе дотримання. Спробуйте згенерувати резюме з рівно 100 слів у форматі маркованого списку.

## Чому ви насправді вчитеся

**Рівень зусиль роздумів змінює все**

GPT-5.2 дає вам керувати обчислювальними зусиллями через ваші запити. Низькі зусилля означають швидкі відповіді з мінімальним дослідженням. Високі — це коли модель витрачає час на глибокі роздуми. Ви вчитеся підбирати зусилля відповідно до складності завдання — не витрачайте час на прості питання, але і не поспішайте з складними рішеннями.

**Структура керує поведінкою**

Помітили XML-теги у промптах? Вони не для краси. Моделі надійніше слідують структурованим інструкціям, ніж вільному тексту. Коли потрібні багатокрокові процеси чи складна логіка, структура допомагає моделі розуміти, на якому етапі вона є і що буде далі.

<img src="../../../translated_images/uk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомія добре структурованого промпту з чіткими розділами та організацією у стилі XML*

**Якість через самооцінку**

Шаблони з самоаналізом працюють завдяки чітким критеріям якості. Замість того, щоб сподіватися, що модель "зробить все правильно", ви чітко вказуєте, що означає "правильно": коректна логіка, обробка помилок, продуктивність, безпека. Модель тоді може оцінити власний вихід і покращити його. Це перетворює генерацію коду з лотереї у процес.

**Контекст обмежений**

Багатокрокові розмови працюють завдяки включенню історії повідомлень у кожен запит. Але є ліміт — у кожної моделі максимальна кількість токенів. Коли розмір розмови зростає, потрібні стратегії збереження релевантного контексту без перевищення ліміту. Цей модуль показує, як працює пам’ять; пізніше ви дізнаєтесь, коли підсумовувати, коли забувати, а коли витягувати.

## Наступні кроки

**Наступний модуль:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навігація:** [← Попередній: Модуль 01 - Вступ](../01-introduction/README.md) | [Назад до головної](../README.md) | [Наступний: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ його рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->