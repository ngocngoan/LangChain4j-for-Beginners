# Модуль 02: Розробка запитів з GPT-5.2

## Зміст

- [Відео-пояснення](../../../02-prompt-engineering)
- [Чому ви навчитесь](../../../02-prompt-engineering)
- [Вимоги](../../../02-prompt-engineering)
- [Розуміння інженерії запитів](../../../02-prompt-engineering)
- [Основи інженерії запитів](../../../02-prompt-engineering)
  - [Zero-Shot Запити](../../../02-prompt-engineering)
  - [Few-Shot Запити](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Запити](../../../02-prompt-engineering)
  - [Шаблони запитів](../../../02-prompt-engineering)
- [Розширені патерни](../../../02-prompt-engineering)
- [Використання існуючих ресурсів Azure](../../../02-prompt-engineering)
- [Знімки екрана додатку](../../../02-prompt-engineering)
- [Дослідження патернів](../../../02-prompt-engineering)
  - [Низький vs Високий рівень активності](../../../02-prompt-engineering)
  - [Виконання задачі (Вступи інструментів)](../../../02-prompt-engineering)
  - [Саморефлексуючий код](../../../02-prompt-engineering)
  - [Структурований аналіз](../../../02-prompt-engineering)
  - [Багатоходове спілкування](../../../02-prompt-engineering)
  - [Покрокове логічне міркування](../../../02-prompt-engineering)
  - [Обмежений вивід](../../../02-prompt-engineering)
- [Що ви справді вивчаєте](../../../02-prompt-engineering)
- [Наступні кроки](../../../02-prompt-engineering)

## Відео-пояснення

Дивіться цю живу сесію, що пояснює, як почати роботу з цим модулем: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Чому ви навчитесь

<img src="../../../translated_images/uk/what-youll-learn.c68269ac048503b2.webp" alt="Що ви навчитесь" width="800"/>

У попередньому модулі ви побачили, як пам’ять дає змогу розмовному ШІ і використовували GitHub Models для базових взаємодій. Тепер ми зосередимося на тому, як ви ставите запитання — самих запитах — за допомогою GPT-5.2 від Azure OpenAI. Те, як ви структуруєте свої запити, істотно впливає на якість відповідей. Ми починаємо з огляду фундаментальних технік запиту, а потім перейдемо до восьми розширених патернів, що повністю використовують можливості GPT-5.2.

Ми використовуємо GPT-5.2, бо він вводить контроль міркувань — ви можете сказати моделі, скільки часу виділити на роздуми перед відповіддю. Це робить стратегії запитів більш помітними і допомагає зрозуміти, коли який варіант краще застосувати. Також скористаємося меншою кількістю обмежень у Azure для GPT-5.2 у порівнянні з GitHub Models.

## Вимоги

- Завершений Модуль 01 (розгорнуті ресурси Azure OpenAI)
- Файл `.env` у кореневій теці з обліковими даними Azure (створений за допомогою `azd up` у Модулі 01)

> **Примітка:** Якщо ви не виконали Модуль 01, спочатку дотримуйтесь інструкцій там.

## Розуміння інженерії запитів

<img src="../../../translated_images/uk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Що таке інженерія запитів?" width="800"/>

Інженерія запитів — це створення вхідного тексту, який послідовно дає потрібні результати. Це не просто ставити запитання — це структурувати запити так, щоб модель точно зрозуміла, що ви хочете і як це надати.

Подумайте про це як про інструкції колезі. «Виправ помилку» — надто загально. «Виправ null pointer exception у UserService.java на рядку 45, додавши перевірку на null» — конкретно. Мовні моделі працюють так само — важлива конкретність і структура.

<img src="../../../translated_images/uk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Як підходить LangChain4j" width="800"/>

LangChain4j надає інфраструктуру — підключення до моделей, пам’ять і типи повідомлень — тоді як патерни запитів — це просто ретельно структурований текст, який ви надсилаєте через цю інфраструктуру. Ключові будівельні блоки — це `SystemMessage` (встановлює поведінку та роль ШІ) і `UserMessage` (містить ваше фактичне прохання).

## Основи інженерії запитів

<img src="../../../translated_images/uk/five-patterns-overview.160f35045ffd2a94.webp" alt="Огляд п’яти патернів інженерії запитів" width="800"/>

Перш ніж заглибитися у розширені патерни цього модуля, давайте оновимо знання про п’ять базових методів запитів. Це будівельні блоки, які повинен знати кожен інженер запитів. Якщо ви вже пройшли [модуль швидкого старту](../00-quick-start/README.md#2-prompt-patterns), ви бачили їх у дії — нижче концептуальна основа.

### Zero-Shot Запити

Найпростіший підхід: дайте моделі пряму інструкцію без прикладів. Модель повністю покладається на своє навчання, щоб зрозуміти і виконати завдання. Це добре працює для простих запитів, де очікувана поведінка очевидна.

<img src="../../../translated_images/uk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Запити" width="800"/>

*Пряма інструкція без прикладів — модель виводить завдання лише на основі інструкції*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Відповідь: "Позитивна"
```

**Коли використовувати:** прості класифікації, прямі запитання, переклади чи будь-які задачі, які модель може виконати без додаткових підказок.

### Few-Shot Запити

Надайте приклади, що демонструють патерн, який ви хочете, щоб модель наслідувала. Модель вчиться формату вхідних та вихідних даних з ваших прикладів і застосовує його до нових запитів. Це суттєво покращує послідовність для завдань, де бажаний формат або поведінка неочевидні.

<img src="../../../translated_images/uk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Запити" width="800"/>

*Вивчення з прикладів — модель ідентифікує патерн і застосовує його до нових вхідних даних*

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

**Коли використовувати:** кастомні класифікації, послідовне форматування, галузеві завдання або коли zero-shot результати непослідовні.

### Chain of Thought

Попросіть модель показати міркування покроково. Замість того, щоб одразу дати відповідь, модель розбиває проблему і працює над кожною частиною окремо. Це покращує точність у задачах з математикою, логікою та багатоетапним мисленням.

<img src="../../../translated_images/uk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Запити" width="800"/>

*Покрокове мислення — розбиття складних проблем на чіткі логічні кроки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показує: 15 - 8 = 7, потім 7 + 12 = 19 яблук
```

**Коли використовувати:** математичні задачі, логічні головоломки, налагодження або будь-які завдання, де показ процесу міркування підвищує точність і довіру.

### Role-Based Запити

Встановіть персонажа або роль для ШІ перед тим, як ставити запитання. Це додає контекст, який формує тон, глибину і спрямованість відповіді. «Програмний архітектор» дає іншу пораду, ніж «молодший розробник» або «аудитор з безпеки».

<img src="../../../translated_images/uk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Запити" width="800"/>

*Встановлення контексту і ролі — одне і те саме питання отримує різні відповіді залежно від призначеної ролі*

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

**Коли використовувати:** код-рев’ю, навчання, галузевий аналіз або коли потрібні відповіді, адаптовані до певного рівня експертизи чи точки зору.

### Шаблони запитів

Створюйте багаторазові запити з змінними плейсхолдерами. Замість написання нового запиту щоразу, визначте шаблон один раз і заповнюйте різні значення. Клас `PromptTemplate` в LangChain4j робить це зручно за допомогою синтаксису `{{variable}}`.

<img src="../../../translated_images/uk/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони запитів" width="800"/>

*Багаторазові запити із змінними плейсхолдерами — один шаблон, багато використань*

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

**Коли використовувати:** повторні запити з різними вхідними даними, пакетна обробка, створення повторно використовуваних AI-потоків або будь-який випадок, коли структура запиту залишається сталою, але дані змінюються.

---

Ці п’ять основ дають вам міцний набір інструментів для більшості завдань із запитів. Решта цього модуля ґрунтується на них із використанням **восьми розширених патернів**, що задіюють керування міркуваннями GPT-5.2, самооцінку та структурований вивід.

## Розширені патерни

Після засвоєння основ перейдемо до восьми розширених патернів, які роблять цей модуль унікальним. Не всі задачі потребують однакового підходу. Деякі питання потребують швидких відповідей, інші — глибокого аналізу. Деякі — видимого процесу міркування, інші — просто результатів. Кожен патерн оптимізований для різних сценаріїв — і керування міркуваннями GPT-5.2 ще більше підкреслює різницю.

<img src="../../../translated_images/uk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Вісім патернів запитів" width="800"/>

*Огляд восьми патернів інженерії запитів і їх використання*

<img src="../../../translated_images/uk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Керування міркуваннями з GPT-5.2" width="800"/>

*Контроль міркувань GPT-5.2 дозволяє вказати, скільки часу модель має роздумувати — від швидких прямих відповідей до глибокого дослідження*

**Низький рівень активності (Швидко і цілеспрямовано)** — для простих питань, коли потрібні швидкі, прямі відповіді. Модель виконує мінімум міркувань — максимум 2 кроки. Використовуйте для обчислень, пошуку або очевидних питань.

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

> 💡 **Досліджуйте з GitHub Copilot:** Відкрийте [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) та запитайте:
> - "У чому різниця між патернами низької і високої активності?"
> - "Як XML-теги в запитах допомагають структурувати відповідь ШІ?"
> - "Коли слід використовувати патерни самоаналізу, а коли — прямі інструкції?"

**Високий рівень активності (Глибоко і ретельно)** — для складних завдань, що потребують комплексного аналізу. Модель проводить ретельне дослідження і показує деталізоване мислення. Використовуйте для дизайну систем, архітектурних рішень чи складних досліджень.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Виконання задачі (Покроковий прогрес)** — для багатоступеневих робочих процесів. Модель надає план спереду, розповідає про кожен крок під час роботи, а потім дає підсумок. Використовуйте для міграцій, імплементацій або будь-якого багатоетапного процесу.

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

Chain-of-Thought запити явно просять модель показати процес міркування, що підвищує точність у складних завданнях. Покрокове розбиття допомагає і людям, і ШІ краще зрозуміти логіку.

> **🤖 Спробуйте у чаті [GitHub Copilot](https://github.com/features/copilot):** Запитайте про цей патерн:
> - "Як адаптувати патерн виконання задачі для довготривалих операцій?"
> - "Які найкращі практики для структурування вступів інструментів у продуктивних застосунках?"
> - "Як відслідковувати і відображати проміжні оновлення прогресу в UI?"

<img src="../../../translated_images/uk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Патерн виконання задачі" width="800"/>

*План → Виконання → Підсумок для багатоетапних завдань*

**Саморефлексуючий код** — для генерації коду виробничої якості. Модель генерує код відповідно до стандартів із належною обробкою помилок. Використовуйте при побудові нових функцій або сервісів.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Цикл самоаналізу" width="800"/>

*Ітеративний цикл вдосконалення — генерувати, оцінювати, виявляти проблеми, покращувати, повторювати*

**Структурований аналіз** — для послідовної оцінки. Модель перевіряє код за фіксованою схемою (коректність, практики, продуктивність, безпека, підтримуваність). Використовуйте для код-рев’ю або оцінок якості.

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

> **🤖 Спробуйте у чаті [GitHub Copilot](https://github.com/features/copilot):** Запитайте про структурований аналіз:
> - "Як налаштувати схему аналізу для різних типів код-рев’ю?"
> - "Який найкращий спосіб парсити і діяти за структурованим виводом програмно?"
> - "Як забезпечити послідовний рівень серйозності в різних сесіях рев’ю?"

<img src="../../../translated_images/uk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Патерн структурованого аналізу" width="800"/>

*Схема для послідовних код-рев’ю з рівнями серйозності*

**Багатоходове спілкування** — для діалогів, що потребують контексту. Модель пам’ятає попередні повідомлення і базується на них. Використовуйте для інтерактивних сесій допомоги або складних Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/uk/context-memory.dff30ad9fa78832a.webp" alt="Пам’ять контексту" width="800"/>

*Як накопичується контекст розмови через кілька ходів до досягнення ліміту токенів*

**Покрокове логічне міркування** — для задач із видимою логікою. Модель показує явні міркування для кожного кроку. Використовуйте для математичних задач, логічних головоломок або коли потрібно розуміти думковий процес.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Патерн покрокового міркування" width="800"/>

*Розбиття проблем на чіткі логічні кроки*

**Обмежений вивід** — для відповідей із специфічними вимогами до формату. Модель строго дотримується формату і обмежень по довжині. Використовуйте для резюме або коли потрібна точна структура виводу.

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

<img src="../../../translated_images/uk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Патерн обмеженого виводу" width="800"/>

*Забезпечення конкретних вимог до формату, довжини і структури*

## Використання існуючих ресурсів Azure

**Перевірте розгортання:**

Переконайтеся, що файл `.env` існує у кореневій теці з обліковими даними Azure (створений під час Модуля 01):
```bash
cat ../.env  # Повинно показувати AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запуск додатку:**

> **Примітка:** Якщо ви вже запускали всі додатки за допомогою `./start-all.sh` з Модуля 01, цей модуль уже працює на порту 8083. Ви можете пропустити команди запуску нижче і перейти безпосередньо на http://localhost:8083.

**Варіант 1: Використання Spring Boot Dashboard (рекомендується для користувачів VS Code)**
Dev-контейнер включає розширення Spring Boot Dashboard, яке надає візуальний інтерфейс для керування всіма додатками Spring Boot. Ви можете знайти його на панелі дій зліва в VS Code (шукайте значок Spring Boot).

З Spring Boot Dashboard ви можете:
- Бачити всі доступні додатки Spring Boot у робочій області
- Запускати/зупиняти додатки одним кліком
- Переглядати логи додатка в реальному часі
- Моніторити статус додатка

Просто натисніть кнопку запуску поруч із "prompt-engineering", щоб запустити цей модуль, або запустіть усі модулі одночасно.

<img src="../../../translated_images/uk/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Варіант 2: Використання shell-скриптів**

Запустіть усі веб-додатки (модулі 01-04):

**Bash:**
```bash
cd ..  # З кореневої директорії
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

Обидва скрипти автоматично завантажують змінні середовища з кореневого файлу `.env` та збирають JAR-файли, якщо вони не існують.

> **Примітка:** Якщо Ви віддаєте перевагу збирати всі модулі вручну перед запуском:
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

Відкрийте http://localhost:8083 у своєму браузері.

**Щоб зупинити:**

**Bash:**
```bash
./stop.sh  # Лише цей модуль
# Або
cd .. && ./stop-all.sh  # Всі модулі
```

**PowerShell:**
```powershell
.\stop.ps1  # Тільки цей модуль
# Або
cd ..; .\stop-all.ps1  # Всі модулі
```

## Скриншоти додатку

<img src="../../../translated_images/uk/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Головна панель, що показує всі 8 патернів prompt engineering з їх характеристиками та випадками використання*

## Ознайомлення з патернами

Веб-інтерфейс дозволяє експериментувати з різними стратегіями prompt-ів. Кожен патерн розв’язує різні проблеми — спробуйте їх, щоб побачити, коли який підхід найкращий.

> **Примітка: Стрімінг проти нестрімінгу** — На кожній сторінці паттерну є дві кнопки: **🔴 Stream Response (Live)** та опція **Без стрімінгу**. Стрімінг використовує Server-Sent Events (SSE), щоб показувати токени в реальному часі під час генерування моделі, отже ви бачите прогрес негайно. Опція без стрімінгу чекає повної відповіді перед відображенням. Для промптів, що викликають глибоке мислення (наприклад, High Eagerness, Self-Reflecting Code), виклик без стрімінгу може тривати дуже довго — іноді хвилинами — без видимого фідбеку. **Використовуйте стрімінг при експериментах зі складними промптами**, щоб бачити, як модель працює, і уникнути враження, що запит не відповідає.
>
> **Примітка: Вимоги до браузера** — Функція стрімінгу використовує Fetch Streams API (`response.body.getReader()`), що потребує повноцінного браузера (Chrome, Edge, Firefox, Safari). Вона **не працює** у вбудованому Simple Browser VS Code, бо його вебпереглядач не підтримує ReadableStream API. Якщо ви користуєтесь Simple Browser, кнопки без стрімінгу все одно працюватимуть нормально — лише кнопки для стрімінгу недоступні. Відкрийте `http://localhost:8083` у зовнішньому браузері для повного досвіду.

### Низький проти високого бажання (Low vs High Eagerness)

Задайте просте питання, наприклад: "Що таке 15% від 200?" з Low Eagerness. Ви отримаєте миттєву, прямолінійну відповідь. Тепер задайте складніше: "Розробіть стратегію кешування для високонавантаженого API" з High Eagerness. Натисніть **🔴 Stream Response (Live)** і спостерігайте детальне міркування моделі, що з’являється токен за токеном. Та сама модель, та сама структура запитання — але prompt вказує, скільки думати.

### Виконання завдань (Tool Preambles)

Багатоступеневі робочі процеси виграють завдяки попередньому плануванню та опису прогресу. Модель окреслює, що робитиме, описує кожен крок і підсумовує результати.

### Саморефлексуючий код

Спробуйте "Створити сервіс валідації електронної пошти". Замість простої генерації коду і зупинки модель генерує, оцінює за критеріями якості, ідентифікує слабкі місця та покращує. Ви побачите, як вона ітерує, поки код не відповідатиме виробничим стандартам.

### Структурований аналіз

Код-рев’ю потребує послідовних рамок оцінки. Модель аналізує код за фіксованими категоріями (коректність, практики, продуктивність, безпека) із рівнями серйозності.

### Багатокроковий чат

Запитайте "Що таке Spring Boot?", а потім одразу "Покажи приклад". Модель пам’ятає перше питання і дає саме приклад Spring Boot. Без пам’яті друге питання було б надто загальним.

### Покрокове міркування

Обирайте математичну задачу і спробуйте її розв’язати як з Покроковим міркуванням, так і з Low Eagerness. Low Eagerness швидко дає відповідь — але непрозору. Покрокове показує кожен розрахунок і рішення.

### Обмежений вихід

Коли потрібні конкретні формати або кількість слів, цей патерн забезпечує суворе дотримання. Спробуйте створити резюме з рівно 100 слів у вигляді пунктів.

## Чому ви справді вчитесь

**Зусилля мислення змінює все**

GPT-5.2 дає можливість контролювати обчислювальні зусилля через ваші промпти. Низькі зусилля означають швидкі відповіді з мінімальним дослідженням. Високі — коли модель глибоко думає. Ви навчаєтесь підбирати зусилля згідно зі складністю завдання — не марнувати час на прості питання, але й не поспішати з складними рішеннями.

**Структура керує поведінкою**

Помітили XML-теги в промптах? Вони не просто прикраса. Моделі надійніше виконують структуровані інструкції, ніж вільний текст. Коли потрібні багатокрокові процеси або складна логіка, структура допомагає моделі зрозуміти, де вона зараз і що буде далі.

<img src="../../../translated_images/uk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомія гарно структурованого промпта з чіткими секціями та XML-організацією*

**Якість через самооцінку**

Патерни саморефлексії працюють, роблячи критерії якості явними. Замість надії, що модель "зробить правильно", ви точно вказуєте, що означає "правильно": коректна логіка, обробка помилок, продуктивність, безпека. Модель може оцінювати свій власний вихід і покращуватися. Це перетворює генерацію коду з лотереї у процес.

**Контекст обмежений**

Багатокрокові розмови працюють шляхом включення історії повідомлень у кожен запит. Але є межа – кожна модель має максимальну кількість токенів. З ростом розмов потрібно стратегії збереження релевантного контексту без досягнення ліміту. Цей модуль показує, як працює пам’ять; пізніше ви дізнаєтеся, коли підсумовувати, коли забувати, а коли витягувати.

## Наступні кроки

**Наступний модуль:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навігація:** [← Попередній: Модуль 01 - Вступ](../01-introduction/README.md) | [Назад до Головної](../README.md) | [Вперед: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->