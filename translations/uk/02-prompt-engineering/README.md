# Модуль 02: Інженерія запитів з GPT-5.2

## Зміст

- [Огляд відео](../../../02-prompt-engineering)
- [Чому ви навчитесь](../../../02-prompt-engineering)
- [Попередні вимоги](../../../02-prompt-engineering)
- [Розуміння інженерії запитів](../../../02-prompt-engineering)
- [Основи інженерії запитів](../../../02-prompt-engineering)
  - [Zero-Shot Запити](../../../02-prompt-engineering)
  - [Few-Shot Запити](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Рольовий підхід до запитів](../../../02-prompt-engineering)
  - [Шаблони запитів](../../../02-prompt-engineering)
- [Розширені патерни](../../../02-prompt-engineering)
- [Використання існуючих ресурсів Azure](../../../02-prompt-engineering)
- [Знімки екрану додатків](../../../02-prompt-engineering)
- [Вивчення патернів](../../../02-prompt-engineering)
  - [Низька vs Висока готовність](../../../02-prompt-engineering)
  - [Виконання завдань (підготовчі тексти інструментів)](../../../02-prompt-engineering)
  - [Код із самоаналізом](../../../02-prompt-engineering)
  - [Структурований аналіз](../../../02-prompt-engineering)
  - [Багатократний діалог](../../../02-prompt-engineering)
  - [Покрокове міркування](../../../02-prompt-engineering)
  - [Обмежений вихід](../../../02-prompt-engineering)
- [Що ви справді вивчаєте](../../../02-prompt-engineering)
- [Наступні кроки](../../../02-prompt-engineering)

## Огляд відео

Перегляньте цю живу сесію, яка пояснює, як розпочати роботу з цим модулем:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Чому ви навчитесь

<img src="../../../translated_images/uk/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

У попередньому модулі ви побачили, як пам’ять дозволяє вести розмови з ШІ і використали моделі GitHub для базових взаємодій. Тепер ми зосередимось на тому, як ви ставите питання — самі запити — використовуючи GPT-5.2 від Azure OpenAI. Те, як ви структуруєте ваші запити, драматично впливає на якість відповідей, які ви отримуєте. Ми почнемо з огляду основних технік створення запитів, а потім перейдемо до восьми розширених патернів, що повністю використовують можливості GPT-5.2.

Ми будемо використовувати GPT-5.2, тому що він вводить контроль міркувань — ви можете вказати моделі, скільки роздумів виконувати перед відповідаю. Це робить різні стратегії запитів більш помітними і допомагає зрозуміти, коли використовувати кожен підхід. Також ми отримаємо вигоду від менших обмежень на швидкість в Azure для GPT-5.2 порівняно з моделями GitHub.

## Попередні вимоги

- Завершений Модуль 01 (розгорнуті ресурси Azure OpenAI)
- Файл `.env` у кореневій директорії з обліковими даними Azure (створено за допомогою `azd up` у Модулі 01)

> **Примітка:** Якщо ви не завершили Модуль 01, спочатку дотримуйтесь інструкцій з розгортання там.

## Розуміння інженерії запитів

<img src="../../../translated_images/uk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Інженерія запитів полягає у створенні вхідного тексту, який постійно забезпечує потрібні вам результати. Це не просто задавання питань — це структурування запитів так, щоб модель точно розуміла, чого ви хочете і як надати це.

Уявіть, що ви даєте інструкції колезі. «Виправи баг» — це нечітко. «Виправити NullPointerException у UserService.java на рядку 45, додавши перевірку null» — це конкретно. Мовні моделі працюють так само — важливі конкретика і структура.

<img src="../../../translated_images/uk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j забезпечує інфраструктуру — підключення моделей, пам’ять та типи повідомлень — а патерни запитів — це просто структурований текст, який ви надсилаєте через цю інфраструктуру. Ключовими будівельними блоками є `SystemMessage` (який задає поведінку та роль AI) та `UserMessage` (який містить ваш реальний запит).

## Основи інженерії запитів

<img src="../../../translated_images/uk/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Перед тим як заглибитись у розширені патерни цього модуля, давайте оглянемо п’ять основних технік запитів. Це будівельні блоки, які повинен знати кожен інженер запитів. Якщо ви вже працювали з [модулем швидкого старту](../00-quick-start/README.md#2-prompt-patterns), ви бачили їх у дії — ось концептуальна основа.

### Zero-Shot Запити

Найпростіший підхід: дайте моделі прямий наказ без прикладів. Модель повністю покладається на своє навчання, щоб зрозуміти та виконати завдання. Це добре працює для простих запитів, де очікувана поведінка очевидна.

<img src="../../../translated_images/uk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Прямий наказ без прикладів — модель робить висновки про завдання лише з інструкції*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Відповідь: "Позитивна"
```

**Коли використовувати:** Прості класифікації, прямі питання, переклади або будь-яке завдання, яке модель може виконати без додаткових пояснень.

### Few-Shot Запити

Надайте приклади, які демонструють патерн, який ви хочете, щоб модель виконувала. Модель вивчає очікуваний формат вводу-виводу з ваших прикладів і застосовує його до нових даних. Це значно покращує послідовність для завдань, де потрібний формат або поведінка неочевидні.

<img src="../../../translated_images/uk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Вивчення за прикладами — модель визначає патерн і застосовує його до нових вводів*

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

**Коли використовувати:** Індивідуальні класифікації, послідовне форматування, завдання специфічні до домену або коли результати zero-shot непослідовні.

### Chain of Thought

Запросіть модель показати своє міркування крок за кроком. Замість того, щоб одразу давати відповідь, модель розбиває проблему і явно працює над кожною частиною. Це підвищує точність у математиці, логіці і багатокрокових задачах.

<img src="../../../translated_images/uk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Покрокове міркування — розподіл складних проблем на явні логічні кроки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показує: 15 - 8 = 7, потім 7 + 12 = 19 яблук
```

**Коли використовувати:** Математичні задачі, логічні головоломки, налагодження або будь-яке завдання, де показ міркувань покращує точність і довіру.

### Рольовий підхід до запитів

Задайте персону або роль для ШІ перед тим, як поставити питання. Це дає контекст, що формує тон, глибину та фокус відповіді. «Архітектор програмного забезпечення» дає інші поради, ніж «молодший розробник» або «аудитор безпеки».

<img src="../../../translated_images/uk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Встановлення контексту і персони — одне питання отримує різні відповіді в залежності від призначеної ролі*

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

**Коли використовувати:** Рецензії коду, навчання, аналіз за доменом або коли потрібно відповіді, підігнані під конкретний рівень експертизи чи перспективу.

### Шаблони запитів

Створюйте повторно використовувані запити з змінними. Замість того, щоб щоразу писати новий запит, визначте шаблон один раз і підставляйте різні значення. Клас `PromptTemplate` у LangChain4j робить це легко за допомогою синтаксису `{{variable}}`.

<img src="../../../translated_images/uk/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Повторно використовувані запити зі змінними — один шаблон, багато використань*

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

**Коли використовувати:** Повторювані запити з різними вхідними даними, пакетна обробка, побудова багаторазових робочих процесів на ШІ або будь-які сценарії, де структура запиту постійна, а дані змінюються.

---

Ці п’ять основ забезпечують вам надійний набір інструментів для більшості завдань. Решта цього модуля базується на них у вигляді **восьми розширених патернів**, які використовують контроль міркувань, самооцінку та структурований вихід GPT-5.2.

## Розширені патерни

Після огляду основ перейдемо до восьми розширених патернів, що роблять цей модуль унікальним. Не всі проблеми потребують однакового підходу. Одні питання потребують швидких відповідей, інші — глибокого аналізу. Одні потребують видимого міркування, інші — лише результату. Кожен патерн оптимізований під різний сценарій — і контроль міркувань GPT-5.2 робить ці відмінності ще яскравішими.

<img src="../../../translated_images/uk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Огляд восьми патернів інженерії запитів та їх сфери застосування*

<img src="../../../translated_images/uk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Контроль міркувань GPT-5.2 дозволяє вказувати, скільки роздумів модель має зробити — від швидких прямих відповідей до глибокого дослідження*

**Низька готовність (Швидко та Фокусовано)** — Для простих питань, де потрібні швидкі, прямі відповіді. Модель виконує мінімальні роздуми — не більше 2 кроків. Використовуйте для обчислень, пошуку або простих питань.

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
> - "Чим відрізняються патерни з низькою та високою готовністю?"
> - "Як допомагають XML-теги в запитах структурувати відповідь ШІ?"
> - "Коли слід використовувати патерни самоаналізу, а коли прямі інструкції?"

**Висока готовність (Глибоко та Ретельно)** — Для складних проблем, які потребують всебічного аналізу. Модель детально досліджує і показує розгорнуті міркування. Використовуйте для системного дизайну, архітектурних рішень або складних досліджень.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Виконання завдань (Покроковий прогрес)** — Для багатокрокових робочих процесів. Модель надає план наперед, описує кожен крок під час роботи, а потім дає підсумок. Використовуйте для міграцій, реалізацій або будь-якого багатокрокового процесу.

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

Chain-of-Thought запити явно просять модель показати процес міркування, що підвищує точність для складних завдань. Покроковий розбір допомагає як людям, так і ШІ краще розуміти логіку.

> **🤖 Спробуйте в чаті [GitHub Copilot](https://github.com/features/copilot):** Запитайте про цей патерн:
> - "Як адаптувати патерн виконання завдань для довготривалих операцій?"
> - "Які найкращі практики структурування інструментальних вступів у продуктивних додатках?"
> - "Як фіксувати та відображати проміжні оновлення прогресу у UI?"

<img src="../../../translated_images/uk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Планування → Виконання → Підсумок для багатокрокових завдань*

**Код із самоаналізом** — Для генерації коду виробничої якості. Модель генерує код відповідно до стандартів виробництва з належним обробленням помилок. Використовуйте при створенні нових функцій або сервісів.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ітераційний цикл покращення — генерувати, оцінювати, знаходити проблеми, покращувати, повторювати*

**Структурований аналіз** — Для послідовної оцінки. Модель перевіряє код за фіксованою схемою (коректність, практики, продуктивність, безпека, підтримуваність). Використовуйте для код-рев’ю або оцінки якості.

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

> **🤖 Спробуйте в чаті [GitHub Copilot](https://github.com/features/copilot):** Запитайте про структурований аналіз:
> - "Як налаштувати схему аналізу для різних типів код-рев’ю?"
> - "Який найкращий спосіб програмно розбирати та використовувати структурований вихід?"
> - "Як забезпечити послідовність рівнів серйозності між різними сесіями рев’ю?"

<img src="../../../translated_images/uk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Схема для послідовних код-рев’ю з рівнями серйозності*

**Багатократний діалог** — Для розмов, які потребують контексту. Модель пам’ятає попередні повідомлення і будує на їх основі. Використовуйте для інтерактивної допомоги або складних питань-відповідей.

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

*Як контекст розмови акумулюється за кілька кроків до досягнення ліміту токенів*

**Покрокове міркування** — Для проблем, що потребують видимої логіки. Модель показує явне міркування на кожному кроці. Використовуйте для математики, логіки або коли треба зрозуміти процес думання.

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

*Розбиття проблем на явні логічні кроки*

**Обмежений вихід** — Для відповідей із конкретними вимогами до формату. Модель суворо дотримується правил формату та довжини. Використовуйте для резюме або коли потрібна точна структура виходу.

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

*Забезпечення конкретних вимог до формату, довжини та структури*

## Використання існуючих ресурсів Azure

**Перевірте розгортання:**

Переконайтесь, що файл `.env` існує у кореневій директорії з обліковими даними Azure (створено під час Модуля 01):
```bash
cat ../.env  # Має відображати AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запустіть додаток:**

> **Примітка:** Якщо ви вже запускали всі додатки за допомогою `./start-all.sh` з Модуля 01, цей модуль уже працює на порту 8083. Ви можете пропустити команди запуску нижче та перейти безпосередньо на http://localhost:8083.
**Варіант 1: Використання Spring Boot Dashboard (Рекомендується для користувачів VS Code)**

Контейнер розробника включає розширення Spring Boot Dashboard, яке забезпечує візуальний інтерфейс для керування всіма додатками Spring Boot. Ви можете знайти його на панелі активності зліва у VS Code (шукайте іконку Spring Boot).

З Spring Boot Dashboard ви можете:
- Бачити всі доступні додатки Spring Boot у робочому просторі
- Запускати/зупиняти додатки одним кліком
- Переглядати логи додатків у режимі реального часу
- Моніторити стан додатків

Просто натисніть кнопку відтворення поруч із "prompt-engineering", щоб запустити цей модуль, або запустіть усі модулі одночасно.

<img src="../../../translated_images/uk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Варіант 2: Використання shell-скриптів**

Запустіть усі веб-додатки (модулі 01-04):

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

Обидва скрипти автоматично завантажують змінні середовища з кореневого файлу `.env` і будуть збирати JAR-файли, якщо їх немає.

> **Примітка:** Якщо ви віддаєте перевагу збирати всі модулі вручну перед запуском:
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
./stop.sh  # Лише цей модуль
# Або
cd .. && ./stop-all.sh  # Всі модулі
```

**PowerShell:**
```powershell
.\stop.ps1  # Лише цей модуль
# Або
cd ..; .\stop-all.ps1  # Всі модулі
```

## Знімки екрана додатка

<img src="../../../translated_images/uk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Головна панель, що показує всі 8 шаблонів інженерії промптом із їх характеристиками та випадками використання*

## Ознайомлення з шаблонами

Веб-інтерфейс дозволяє експериментувати з різними стратегіями підказок. Кожен шаблон вирішує різні завдання — спробуйте їх, щоб побачити, коли кожен підхід найбільш ефективний.

> **Примітка: Стрімінг проти нестрімінгу** — Кожна сторінка шаблонів має дві кнопки: **🔴 Stream Response (Live)** та опцію **Non-streaming**. Стрімінг використовує Server-Sent Events (SSE), щоб відображати токени в реальному часі під час генерації моделі, тож ви бачите прогрес одразу. Опція нестрімінгу очікує повної відповіді перед відображенням. Для промптів, що викликають глибоке мислення (наприклад, High Eagerness, Self-Reflecting Code), виклик без стрімінгу може тривати дуже довго — іноді хвилини — без видимого зворотного зв’язку. **Використовуйте стрімінг при експериментуванні зі складними промптами**, щоб бачити процес моделі і уникнути враження, що запит завис.
>
> **Примітка: Вимоги до браузера** — функція стрімінгу використовує Fetch Streams API (`response.body.getReader()`), який підтримують повноцінні браузери (Chrome, Edge, Firefox, Safari). Вбудований Simple Browser у VS Code не підтримує API ReadableStream, тому стрімінг у ньому не працює. Якщо ви користуєтесь Simple Browser, кнопки нестрімінгу працюватимуть нормально — тільки стрімінг кнопки недоступні. Відкрийте `http://localhost:8083` у зовнішньому браузері для повного функціоналу.

### Низька vs Висока Енергійність (Eagerness)

Запитайте просте питання, наприклад «Що таке 15% від 200?» з низькою енергійністю. Ви отримаєте миттєву, пряму відповідь. Тепер поставте складніше запитання, наприклад «Розроби стратегію кешування для високонавантаженого API» з високою енергійністю. Натисніть **🔴 Stream Response (Live)** і спостерігайте, як модель детально пояснює свій процес токен за токеном. Та сама модель, та сама структура запитання — але промпт показує, скільки думки вкладається.

### Виконання завдань (Преамбула інструментів)

Багатокрокові робочі процеси виграють від попереднього планування та коментування прогресу. Модель відображає, що буде робити, коментує кожен крок, а потім підсумовує результати.

### Самоаналіз коду

Спробуйте «Створити сервіс валідації електронної пошти». Замість просто генерації коду та зупинки модель генерує, оцінює за критеріями якості, виявляє слабкі місця та покращує. Ви побачите, як він ітерує, доки код не буде відповідати виробничим стандартам.

### Структурований аналіз

Код-рев’ю потребують послідовної системи оцінки. Модель аналізує код за фіксованими категоріями (коректність, практики, продуктивність, безпека) з рівнями серйозності.

### Багатокроковий чат

Запитайте «Що таке Spring Boot?», а потім одразу «Покажи приклад». Модель пам’ятає ваше перше питання і дає приклад Spring Boot спеціально для нього. Без пам’яті друге питання було б надто загальним.

### Покрокове мислення

Виберіть математичну задачу і спробуйте розв’язати її за допомогою як покрокового мислення, так і з низькою енергійністю. Низька енергійність просто дасть відповідь - швидко, але непрозоро. Покроково ви побачите кожен розрахунок і рішення.

### Обмежений вивід

Коли потрібні специфічні формати або кількість слів, цей шаблон жорстко дотримує таких вимог. Спробуйте згенерувати резюме рівно з 100 слів у форматі маркованого списку.

## Чому це важливо

**Рівень мислення змінює все**

GPT-5.2 дозволяє контролювати обчислювальні зусилля через промпти. Низькі зусилля — швидкі відповіді з мінімальними дослідженнями. Високі зусилля — модель глибоко обдумує питання. Ви навчаєтесь підбирати рівень зусиль до складності завдання — не витрачайте зайвий час на прості питання, але і не квапте складні рішення.

**Структура керує поведінкою**

Помітили XML-теги у промптах? Вони не для краси. Моделі надійніше виконують структуровані інструкції, ніж довільний текст. Коли потрібні багатокрокові процеси або складна логіка, структура допомагає моделі відслідковувати, де вона і що далі.

<img src="../../../translated_images/uk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомія добре структурованого промпта з чіткими секціями та організацією в стилі XML*

**Якість через самооцінку**

Самоаналізуючі шаблони роблять критерії якості явними. Замість того, щоб сподіватися, що модель «зробить правильно», ви чітко вказуєте, що означає «правильно»: коректна логіка, обробка помилок, продуктивність, безпека. Модель потім оцінює власний результат і покращує. Це перетворює генерацію коду з лотереї на процес.

**Контекст обмежений**

Багатокрокові розмови працюють, тому що кожен запит містить історію повідомлень. Але є межа — у кожної моделі є максимальна кількість токенів. Коли розмови ростуть, потрібно стратегії, щоб зберігати актуальний контекст і не стикатися з лімітом. Цей модуль покаже, як працює пам’ять; згодом ви навчитесь, коли підсумовувати, коли забувати, а коли витягувати.

## Наступні кроки

**Наступний модуль:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навігація:** [← Попередній: Модуль 01 - Вступ](../01-introduction/README.md) | [Назад до головної](../README.md) | [Наступний: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу машинного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми докладаємо зусиль для забезпечення точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->