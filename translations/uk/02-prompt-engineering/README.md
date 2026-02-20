# Модуль 02: Інженерія підказок з GPT-5.2

## Зміст

- [Чому ви навчитеся](../../../02-prompt-engineering)
- [Вимоги](../../../02-prompt-engineering)
- [Розуміння інженерії підказок](../../../02-prompt-engineering)
- [Основи інженерії підказок](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Шаблони підказок](../../../02-prompt-engineering)
- [Розширені шаблони](../../../02-prompt-engineering)
- [Використання існуючих ресурсів Azure](../../../02-prompt-engineering)
- [Знімки екрана застосунку](../../../02-prompt-engineering)
- [Огляд шаблонів](../../../02-prompt-engineering)
  - [Низький vs високий рівень ініціативи](../../../02-prompt-engineering)
  - [Виконання завдань (попередні тексти інструментів)](../../../02-prompt-engineering)
  - [Саморефлексивний код](../../../02-prompt-engineering)
  - [Структурований аналіз](../../../02-prompt-engineering)
  - [Багатокроковий чат](../../../02-prompt-engineering)
  - [Покрокове міркування](../../../02-prompt-engineering)
  - [Обмежений вивід](../../../02-prompt-engineering)
- [Що ви насправді вивчаєте](../../../02-prompt-engineering)
- [Наступні кроки](../../../02-prompt-engineering)

## Чому ви навчитеся

<img src="../../../translated_images/uk/what-youll-learn.c68269ac048503b2.webp" alt="Чому ви навчитеся" width="800"/>

У попередньому модулі ви побачили, як пам’ять забезпечує можливості розмовного ШІ, та використовували моделі GitHub для базових взаємодій. Тепер ми зосередимося на тому, як ви ставите запитання — власне на підказках — використовуючи GPT-5.2 від Azure OpenAI. Те, як ви структуруєте свої підказки, суттєво впливає на якість відповідей. Ми почнемо з огляду базових технік підказування, а потім перейдемо до восьми розширених шаблонів, які максимально використовують можливості GPT-5.2.

Ми використовуємо GPT-5.2, тому що він вводить контроль міркувань — ви можете вказати моделі, скільки мислення виконувати перед відповіддю. Це робить різні стратегії підказування більш очевидними і допомагає зрозуміти, коли використовувати ту чи іншу. Також ми скористаємось меншою кількістю обмежень по швидкості в GPT-5.2 в Azure у порівнянні з моделями GitHub.

## Вимоги

- Завершено Модуль 01 (розгорнуто ресурси Azure OpenAI)
- Файл `.env` у кореневому каталозі з обліковими даними Azure (створений `azd up` у Модулі 01)

> **Примітка:** Якщо ви ще не завершили Модуль 01, спочатку виконайте інструкції з розгортання там.

## Розуміння інженерії підказок

<img src="../../../translated_images/uk/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Що таке інженерія підказок?" width="800"/>

Інженерія підказок — це розробка вхідного тексту, який послідовно здобуває потрібний вам результат. Це не просто ставити питання — це структуроване формулювання запитів так, щоб модель точно розуміла, що ви хочете і як це надати.

Уявіть, що ви даєте інструкції колезі. «Виправте баг» – це нечітко. «Виправте виняток null pointer у UserService.java на рядку 45, додавши перевірку на null» — це конкретно. Мовні моделі працюють так само — важливі конкретність і структура.

<img src="../../../translated_images/uk/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Як працює LangChain4j" width="800"/>

LangChain4j забезпечує інфраструктуру — з’єднання з моделями, пам’ять і типи повідомлень — а шаблони підказок — це лише ретельно структурований текст, що проходить цією інфраструктурою. Ключові будівельні блоки — це `SystemMessage` (що задає поведінку і роль ШІ) та `UserMessage` (що містить ваш фактичний запит).

## Основи інженерії підказок

<img src="../../../translated_images/uk/five-patterns-overview.160f35045ffd2a94.webp" alt="Огляд п’яти шаблонів інженерії підказок" width="800"/>

Перед тим, як зануритися у розширені шаблони цього модуля, давайте розглянемо п’ять базових технік підказування. Це будівельні блоки, які має знати кожен інженер підказок. Якщо ви вже працювали з [модулем Quick Start](../00-quick-start/README.md#2-prompt-patterns), ви бачили це на практиці — ось концептуальна база за ними.

### Zero-Shot Prompting

Найпростіший підхід: дайте моделі пряме завдання без прикладів. Модель цілком покладається на своє навчання, щоб зрозуміти і виконати завдання. Це добре підходить для простих запитів, де очікувана поведінка очевидна.

<img src="../../../translated_images/uk/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Пряме завдання без прикладів — модель робить висновок про завдання лише з інструкції*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Відповідь: "Позитивно"
```

**Коли використовувати:** Прості класифікації, прямі запитання, переклади чи будь-яке завдання, яке модель може виконати без додаткових вказівок.

### Few-Shot Prompting

Надайте приклади, щоб показати патерн, якого ви хочете від моделі. Модель вивчає очікуваний формат введення-виводу з ваших прикладів і застосовує його до нових вхідних даних. Це суттєво підвищує послідовність для завдань, де потрібний формат або поведінка неочевидні.

<img src="../../../translated_images/uk/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Вивчення з прикладів — модель знаходить патерн і застосовує його до нових входів*

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

**Коли використовувати:** Кастомні класифікації, послідовне форматування, предметно-специфічні завдання або коли zero-shot результати не послідовні.

### Chain of Thought

Запросіть модель показати покрокове міркування. Замість того, щоб одразу давати відповідь, модель розбиває проблему і працює над кожною частиною явно. Це покращує точність у задачах з математикою, логікою та багатокроковим міркуванням.

<img src="../../../translated_images/uk/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Покрокове міркування — розбивка складних проблем на явні логічні кроки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показує: 15 - 8 = 7, тоді 7 + 12 = 19 яблук
```

**Коли використовувати:** Математичні задачі, логічні головоломки, дебагінг або будь-яке завдання, де показ міркувань покращує точність та довіру.

### Role-Based Prompting

Задайте персону або роль для ШІ перед тим, як задати питання. Це дає контекст, що формує тон, глибину та фокус відповіді. «Архітектор програмного забезпечення» дає інші поради, ніж «молодший розробник» чи «експерт з безпеки».

<img src="../../../translated_images/uk/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Задання контексту і персони — однакове питання отримує різні відповіді залежно від призначеної ролі*

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

**Коли використовувати:** Код-рев’ю, навчання, предметно-специфічний аналіз або коли потрібні відповіді, пристосовані до певного рівня експертизи або перспективи.

### Шаблони підказок

Створюйте повторно використовувані підказки з змінними-заповнювачами. Замість того, щоб писати нову підказку кожного разу, визначте шаблон один раз і заповнюйте різними значеннями. Клас `PromptTemplate` LangChain4j полегшує це за допомогою синтаксису `{{variable}}`.

<img src="../../../translated_images/uk/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони підказок" width="800"/>

*Повторне використання підказок зі змінними — один шаблон, багато використань*

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

**Коли використовувати:** Повторні запити з різними вхідними даними, пакетна обробка, побудова повторно використовуваних AI-воркфлоу або будь-який сценарій, де структура підказки одна, а дані змінюються.

---

Ці п’ять основ дають міцний набір інструментів для більшості завдань підказування. Решта модуля будується на них за допомогою **восьми розширених шаблонів**, що використовують контроль міркувань, самооцінку та структурований вивід GPT-5.2.

## Розширені шаблони

Опісля базових прийомів перейдемо до восьми розширених шаблонів, які роблять цей модуль унікальним. Не всі проблеми потребують однакового підходу. Деякі питання вимагають швидких відповідей, інші — глибокого мислення. Деякі потребують явного міркування, інші — лише результатів. Кожен шаблон нижче оптимізований для певного сценарію — а контроль міркувань GPT-5.2 робить ці різниці ще помітнішими.

<img src="../../../translated_images/uk/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Вісім шаблонів підказок" width="800"/>

*Огляд восьми шаблонів інженерії підказок та їхніх випадків використання*

<img src="../../../translated_images/uk/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контроль міркувань зі GPT-5.2" width="800"/>

*Контроль міркувань GPT-5.2 дозволяє вказати, скільки мислення має вести модель — від швидких прямих відповідей до глибоких досліджень*

**Низький рівень ініціативи (Швидко і сфокусовано)** — для простих питань, де потрібні швидкі прямі відповіді. Модель робить мінімум міркувань — максимум 2 кроки. Використовуйте для обчислень, пошуку чи прямолінійних запитань.

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

> 💡 **Попробуйте з GitHub Copilot:** Відкрийте [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) і запитайте:
> - "У чому різниця між шаблонами підказок з низьким і високим рівнем ініціативи?"
> - "Як XML-теги в підказках допомагають структурувати відповідь ШІ?"
> - "Коли слід використовувати шаблони з самоаналізом, а коли з прямими інструкціями?"

**Високий рівень ініціативи (Глибоко і ретельно)** — для складних проблем, де потрібен комплексний аналіз. Модель досліджує питання детально і показує докладне міркування. Використовуйте для проектування систем, архітектурних рішень або складних досліджень.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Виконання завдань (покроковий прогрес)** — для багатокрокових процесів. Модель надає план спереду, описує кожен крок під час роботи, а потім дає підсумок. Використовуйте для міграцій, впроваджень чи будь-яких багатокрокових процесів.

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

Chain-of-Thought підказування явно просить модель показати процес міркування, що покращує точність у складних завданнях. Покроковий розбір допомагає як людям, так і ШІ зрозуміти логіку.

> **🤖 Спробуйте з [GitHub Copilot](https://github.com/features/copilot) Chat:** Запитайте про цей шаблон:
> - "Як адаптувати шаблон виконання завдань для довготривалих операцій?"
> - "Які найкращі практики структуризації попередніх текстів інструментів у виробничих застосунках?"
> - "Як зафіксувати і відобразити проміжні оновлення прогресу в інтерфейсі?"

<img src="../../../translated_images/uk/task-execution-pattern.9da3967750ab5c1e.webp" alt="Шаблон виконання завдань" width="800"/>

*План → Виконання → Підсумок для багатокрокових завдань*

**Саморефлексивний код** — для генерації коду виробничої якості. Модель створює код відповідно до виробничих стандартів з правильною обробкою помилок. Використовуйте для побудови нових функцій або сервісів.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Цикл саморефлексії" width="800"/>

*Ітеративний цикл покращення — генеруйте, оцінюйте, визначайте проблеми, покращуйте, повторюйте*

**Структурований аналіз** — для послідовної оцінки. Модель переглядає код за фіксованою схемою (коректність, практики, продуктивність, безпека, підтримуваність). Використовуйте для код-рев’ю чи оцінки якості.

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
> - "Як налаштувати фреймворк аналізу для різних типів код-рев’ю?"
> - "Який найкращий спосіб програмно розбирати і використовувати структурований вивід?"
> - "Як забезпечити послідовність рівнів серйозності у різних сесіях рев’ю?"

<img src="../../../translated_images/uk/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Шаблон структурованого аналізу" width="800"/>

*Фреймворк для послідовних код-рев’ю з рівнями серйозності*

**Багатокроковий чат** — для розмов, що потребують контексту. Модель пам’ятає попередні повідомлення і відштовхується від них. Використовуйте для інтерактивної допомоги чи складних Q&A.

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

*Як контекст розмови накопичується за кілька кроків до досягнення ліміту токенів*

**Покрокове міркування** — для завдань, що потребують явної логіки. Модель показує докладне міркування для кожного кроку. Використовуйте для математичних задач, логічних головоломок або коли потрібно зрозуміти хід думок.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/uk/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Покроковий шаблон" width="800"/>

*Розбивка проблем на явні логічні кроки*

**Обмежений вивід** — для відповідей зі специфічними вимогами до формату. Модель строго дотримується правил формату і довжини. Використовуйте для підсумків чи коли потрібна точна структура виводу.

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

<img src="../../../translated_images/uk/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Шаблон обмеженого виводу" width="800"/>

*Дотримання конкретних вимог до формату, довжини та структури*

## Використання існуючих ресурсів Azure

**Перевірте розгортання:**

Переконайтеся, що файл `.env` існує у кореневому каталозі з обліковими даними Azure (створений під час Модуля 01):
```bash
cat ../.env  # Має показувати AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запустіть застосунок:**

> **Примітка:** Якщо ви вже запускали всі застосунки за допомогою `./start-all.sh` з Модуля 01, цей модуль вже працює на порті 8083. Ви можете пропустити команди запуску нижче і перейти безпосередньо на http://localhost:8083.

**Варіант 1: Використання Spring Boot Dashboard (Рекомендується для користувачів VS Code)**

Розробницький контейнер включає розширення Spring Boot Dashboard, яке надає візуальний інтерфейс для керування усіма Spring Boot застосунками. Ви знайдете його на панелі активностей ліворуч у VS Code (іконка Spring Boot).

За допомогою Spring Boot Dashboard ви можете:
- Побачити всі доступні Spring Boot застосунки у робочій області
- Запускати/зупиняти застосунки одним кліком
- Переглядати логи застосунків у реальному часі
- Контролювати стан застосунків
Просто натисніть кнопку відтворення поруч із "prompt-engineering", щоб почати цей модуль, або запустіть усі модулі одразу.

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

Обидва скрипти автоматично завантажують змінні середовища з кореневого файлу `.env` і збирають JAR-файли, якщо вони відсутні.

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

*Головна панель керування, що показує всі 8 шаблонів prompt engineering із їх характеристиками та випадками використання*

## Дослідження шаблонів

Веб-інтерфейс дозволяє експериментувати з різними стратегіями формулювання запитів. Кожен шаблон вирішує різні завдання — спробуйте їх, щоб побачити, коли кожен підхід працює найкраще.

### Низька vs Висока активність

Задайте просте питання, наприклад «Що таке 15% від 200?» з використанням Низької активності. Ви отримаєте миттєву, прямолінійну відповідь. Тепер запитайте щось складне, наприклад «Розробіть стратегію кешування для API з високим навантаженням», використовуючи Високу активність. Спостерігайте, як модель уповільнюється та надає детальне обґрунтування. Та сама модель, та сама структура питання — але підказка вказує, скільки думок слід прикласти.

<img src="../../../translated_images/uk/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Швидкий розрахунок із мінімальним розмірковуванням*

<img src="../../../translated_images/uk/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Комплексна стратегія кешування (2.8MB)*

### Виконання завдань (Преамбули інструментів)

Багатокрокові робочі процеси виграють від планування на початку та супровідного опису прогресу. Модель описує, що вона робитиме, розповідає про кожен крок, а потім підсумовує результати.

<img src="../../../translated_images/uk/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Створення REST-ендоінту з покроковим супроводом (3.9MB)*

### Код із самоаналізом

Спробуйте «Створити сервіс перевірки електронної пошти». Замість просто генерації коду та зупинки, модель генерує, оцінює за критеріями якості, виявляє слабкі місця і поліпшує. Ви побачите, як вона ітерує, доки код не відповідатиме виробничим стандартам.

<img src="../../../translated_images/uk/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Повний сервіс валідації електронної пошти (5.2MB)*

### Структурований аналіз

Огляди коду потребують послідовних фреймворків оцінки. Модель аналізує код за фіксованими категоріями (коректність, практики, продуктивність, безпека) із рівнями серйозності.

<img src="../../../translated_images/uk/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Код-рев'ю на основі фреймворку*

### Багатокроковий чат

Запитайте «Що таке Spring Boot?» потім одразу ж запитайте «Покажи приклад». Модель пам’ятає ваше перше питання і дає вам саме приклад Spring Boot. Без пам’яті друге питання було б надто загальним.

<img src="../../../translated_images/uk/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Збереження контексту між питаннями*

### Покрокове розумування

Виберіть математичну задачу й спробуйте вирішити її як із Покроковим розумуванням, так і з Низькою активністю. Низька активність просто дає відповідь — швидко, але непрозоро. Покрокове показує кожний розрахунок і прийняття рішення.

<img src="../../../translated_images/uk/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математична задача з явними кроками*

### Обмежений вивід

Коли вам потрібні конкретні формати або кількість слів, цей шаблон забезпечує суворе дотримання. Спробуйте згенерувати резюме з рівно 100 слів у форматі маркованого списку.

<img src="../../../translated_images/uk/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резюме машинного навчання з контролем формату*

## Чого ви справді навчаєтеся

**Зусилля в розумуванні змінює все**

GPT-5.2 дає вам змогу контролювати обчислювальні зусилля через ваші промпти. Низькі зусилля означають швидкі відповіді з мінімальним дослідженням. Високі — модель витрачає час на глибокі роздуми. Ви вчитеся підбирати зусилля відповідно до складності завдання — не витрачайте час на прості питання, але й не поспішайте з складними рішеннями.

**Структура керує поведінкою**

Зауважте XML-теги у промптах? Вони не декоративні. Моделі надійніше слідують за структурованими інструкціями, ніж за вільним текстом. Коли вам потрібні багатокрокові процеси або складна логіка, структура допомагає моделі відстежувати, де вона знаходиться і що буде далі.

<img src="../../../translated_images/uk/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Будова добре структурованого промпта з чіткими розділами та організацією в стилі XML*

**Якість через самооцінку**

Шаблони із самоаналізом працюють, роблячи критерії якості явними. Замість того, щоб сподіватися, що модель "зробить правильно", ви точно вказуєте, що означає "правильно": правильна логіка, обробка помилок, продуктивність, безпека. Модель може оцінювати свій вивід і покращувати його. Це перетворює генерацію коду з лотереї у процес.

**Контекст обмежений**

Багатокрокові розмови працюють завдяки включенню історії повідомлень у кожен запит. Але існує ліміт — у кожної моделі є максимальна кількість токенів. Зі зростанням розмов вам потрібні стратегії збереження релевантного контексту, не долаючи цей поріг. Цей модуль показує, як працює пам’ять; пізніше ви дізнаєтеся, коли підсумовувати, коли забувати і коли відновлювати.

## Наступні кроки

**Наступний модуль:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навігація:** [← Попередній: Модуль 01 - Вступ](../01-introduction/README.md) | [Назад до головного](../README.md) | [Далі: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоч ми і прагнемо до точності, будь ласка, враховуйте, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ його рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний переклад людиною. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->