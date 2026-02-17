# Модуль 02: Инжиниринг запросов с GPT-5.2

## Содержание

- [Чему вы научитесь](../../../02-prompt-engineering)
- [Требования](../../../02-prompt-engineering)
- [Понимание инжиниринга запросов](../../../02-prompt-engineering)
- [Основы инжиниринга запросов](../../../02-prompt-engineering)
  - [Zero-Shot запросы](../../../02-prompt-engineering)
  - [Few-Shot запросы](../../../02-prompt-engineering)
  - [Цепочка рассуждений](../../../02-prompt-engineering)
  - [Ролевой инжиниринг запросов](../../../02-prompt-engineering)
  - [Шаблоны запросов](../../../02-prompt-engineering)
- [Продвинутые шаблоны](../../../02-prompt-engineering)
- [Использование существующих ресурсов Azure](../../../02-prompt-engineering)
- [Скриншоты приложения](../../../02-prompt-engineering)
- [Изучение шаблонов](../../../02-prompt-engineering)
  - [Низкая vs высокая настойчивость](../../../02-prompt-engineering)
  - [Выполнение задач (преамбулы инструментов)](../../../02-prompt-engineering)
  - [Саморефлексирующий код](../../../02-prompt-engineering)
  - [Структурированный анализ](../../../02-prompt-engineering)
  - [Многоходовой чат](../../../02-prompt-engineering)
  - [Пошаговое рассуждение](../../../02-prompt-engineering)
  - [Ограниченный вывод](../../../02-prompt-engineering)
- [Что вы на самом деле изучаете](../../../02-prompt-engineering)
- [Следующие шаги](../../../02-prompt-engineering)

## Чему вы научитесь

<img src="../../../translated_images/ru/what-youll-learn.c68269ac048503b2.webp" alt="Чему вы научитесь" width="800"/>

В предыдущем модуле вы увидели, как память позволяет создавать разговорный ИИ, и использовали модели GitHub для базового взаимодействия. Теперь мы сосредоточимся на том, как вы задаёте вопросы — на самих запросах — используя GPT-5.2 от Azure OpenAI. То, как вы структурируете запросы, существенно влияет на качество ответов. Мы начнем с обзора основных техник создания запросов, затем перейдем к восьми продвинутым шаблонам, которые полностью раскрывают возможности GPT-5.2.

Мы используем GPT-5.2, потому что он вводит управление рассуждениями — вы можете задавать модели, сколько думать перед ответом. Это делает различные стратегии запросов более заметными и помогает понять, когда использовать каждую из них. Также мы получаем выгоду от меньших ограничений по частоте вызовов в Azure для GPT-5.2 по сравнению с моделями GitHub.

## Требования

- Завершённый модуль 01 (развернуты ресурсы Azure OpenAI)
- Файл `.env` в корневой директории с учётными данными Azure (создан командой `azd up` в модуле 01)

> **Примечание:** Если вы не завершили модуль 01, сначала следуйте инструкциям по развертыванию там.

## Понимание инжиниринга запросов

<img src="../../../translated_images/ru/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Что такое инжиниринг запросов?" width="800"/>

Инжиниринг запросов — это проектирование входного текста, который последовательно даёт нужные вам результаты. Это не просто задавать вопросы — это структурировать запросы так, чтобы модель точно понимала, что вы хотите и как это предоставить.

Думайте об этом как о даче указаний коллеге. «Исправь баг» — это расплывчато. «Исправь исключение null pointer в UserService.java на строке 45, добавив проверку на null» — это конкретно. Языковые модели работают так же — важна конкретика и структура.

<img src="../../../translated_images/ru/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Как подходит LangChain4j" width="800"/>

LangChain4j предоставляет инфраструктуру — подключения к моделям, память и типы сообщений — в то время как шаблоны запросов — это просто тщательно структурированный текст, который вы отправляете через эту инфраструктуру. Ключевые строительные блоки — `SystemMessage` (которое задаёт поведение и роль ИИ) и `UserMessage` (которое несёт ваш фактический запрос).

## Основы инжиниринга запросов

<img src="../../../translated_images/ru/five-patterns-overview.160f35045ffd2a94.webp" alt="Обзор пяти шаблонов инжиниринга запросов" width="800"/>

Прежде чем погружаться в продвинутые шаблоны этого модуля, рассмотрим пять базовых техник создания запросов. Это строительные блоки, которые должен знать каждый инженер запросов. Если вы уже работали с [модулем Быстрый старт](../00-quick-start/README.md#2-prompt-patterns), вы видели их в действии — вот концептуальная основа.

### Zero-Shot запросы

Самый простой подход: дать модели прямую инструкцию без примеров. Модель полностью опирается на своё обучение, чтобы понять и выполнить задачу. Это хорошо работает для простых запросов, где ожидаемое поведение очевидно.

<img src="../../../translated_images/ru/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Запросы" width="800"/>

*Прямая инструкция без примеров — модель выводит задачу только из инструкции*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Ответ: "Положительный"
```

**Когда использовать:** Простейшая классификация, прямые вопросы, переводы или любая задача, которую модель может выполнить без дополнительной помощи.

### Few-Shot запросы

Предоставьте примеры, которые демонстрируют шаблон, который вы хотите, чтобы модель повторила. Модель учится ожидаемому формату ввода-вывода по вашим примерам и применяет его к новым вводным данным. Это значительно улучшает стабильность для задач, где желаемый формат или поведение не очевидны.

<img src="../../../translated_images/ru/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Запросы" width="800"/>

*Обучение на примерах — модель выявляет шаблон и применяет его к новым данным*

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

**Когда использовать:** Кастомные классификации, единообразное форматирование, задачи в специфичных доменах или когда zero-shot результаты нестабильны.

### Цепочка рассуждений

Попросите модель показать своё рассуждение по шагам. Вместо того, чтобы сразу дать ответ, модель разбивает проблему и последовательно проходит по каждой части. Это повышает точность при вычислениях, логике и многоступенчатом рассуждении.

<img src="../../../translated_images/ru/chain-of-thought.5cff6630e2657e2a.webp" alt="Цепочка рассуждений" width="800"/>

*Пошаговое рассуждение — разбивка комплексных задач на чёткие логические этапы*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показывает: 15 - 8 = 7, затем 7 + 12 = 19 яблок
```

**Когда использовать:** Математические задачи, логические загадки, отладка или задачи, где показ рассуждений улучшает точность и доверие.

### Ролевой инжиниринг запросов

Задайте персонализацию или роль ИИ перед тем, как задать вопрос. Это даёт контекст, который формирует тон, глубину и акценты ответа. «Архитектор программного обеспечения» даст другие советы, чем «младший разработчик» или «аудитор безопасности».

<img src="../../../translated_images/ru/role-based-prompting.a806e1a73de6e3a4.webp" alt="Ролевой инжиниринг запросов" width="800"/>

*Установка контекста и роли — один и тот же вопрос даёт разный ответ в зависимости от роли*

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

**Когда использовать:** Ревью кода, обучение, анализ в специфичной области или когда нужны ответы, адаптированные под уровень экспертизы или перспективу.

### Шаблоны запросов

Создавайте повторно используемые запросы с переменными-заполнителями. Вместо того, чтобы каждый раз писать новый запрос, определите шаблон один раз и подставляйте разные значения. Класс `PromptTemplate` в LangChain4j облегчает это с помощью синтаксиса `{{variable}}`.

<img src="../../../translated_images/ru/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблоны запросов" width="800"/>

*Повторно используемые запросы с переменными — один шаблон, множественное использование*

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

**Когда использовать:** Повторяющиеся запросы с разными входными данными, пакетная обработка, построение многоразовых AI рабочих процессов или любая ситуация, когда структура запроса остаётся неизменной, а данные меняются.

---

Эти пять основ дают надёжный набор инструментов для большинства задач с запросами. Остальная часть этого модуля строится на них с помощью **восьми продвинутых шаблонов**, которые используют управление рассуждениями GPT-5.2, самоконтроль и структурированный вывод.

## Продвинутые шаблоны

После рассмотрения основ переходим к восьми продвинутым шаблонам, которые делают этот модуль уникальным. Не все задачи требуют одинакового подхода. Одним вопросам нужны быстрые ответы, другим — глубокое обдумывание. Одным нужен видимый процесс рассуждений, другим — только результат. Каждый нижеописанный шаблон оптимизирован под определённый сценарий — а управление рассуждениями GPT-5.2 делает различия ещё более явными.

<img src="../../../translated_images/ru/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Восемь шаблонов создания запросов" width="800"/>

*Обзор восьми шаблонов инжиниринга запросов и их применений*

<img src="../../../translated_images/ru/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Управление рассуждениями с GPT-5.2" width="800"/>

*Управление рассуждениями GPT-5.2 позволяет вам задавать, сколько модель должна думать — от быстрых прямых ответов до глубокого анализа*

<img src="../../../translated_images/ru/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Сравнение усилий рассуждений" width="800"/>

*Низкая настойчивость (быстро и напрямую) против высокой настойчивости (тщательно и исследовательно)*

**Низкая настойчивость (быстро и сфокусировано)** — для простых вопросов, когда нужны быстрые прямые ответы. Модель делает минимальные рассуждения — максимум 2 шага. Используйте для вычислений, поисков или простых вопросов.

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

> 💡 **Изучите с GitHub Copilot:** Откройте [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и спросите:
> - «В чём разница между шаблонами с низкой и высокой настойчивостью?»
> - «Как XML-теги в запросах помогают структурировать ответ ИИ?»
> - «Когда лучше использовать саморефлексивные шаблоны, а когда прямые инструкции?»

**Высокая настойчивость (глубоко и тщательно)** — для сложных задач, когда нужен всесторонний анализ. Модель исследует подробно и показывает детальные рассуждения. Используйте для проектирования систем, архитектурных решений или сложных исследований.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Выполнение задач (постепенный прогресс)** — для многоступенчатых рабочих процессов. Модель предоставляет план, рассказывает о каждом шаге в процессе, а затем даёт итог. Используйте для миграций, внедрений или любого многоступенчатого процесса.

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

Цепочка рассуждений (Chain-of-Thought) явно требует у модели показать процесс рассуждения, что повышает точность для сложных задач. Пошаговый разбор помогает и людям, и ИИ лучше понять логику.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Спросите про этот шаблон:
> - «Как адаптировать шаблон выполнения задач для долгих операций?»
> - «Какие лучшие практики для структурирования преамбул инструментов в продакшене?»
> - «Как захватывать и показывать промежуточный прогресс в UI?»

<img src="../../../translated_images/ru/task-execution-pattern.9da3967750ab5c1e.webp" alt="Шаблон выполнения задач" width="800"/>

*План → Выполнение → Итог для многоступенчатых задач*

**Саморефлексирующий код** — для генерации кода производственного качества. Модель создаёт код согласно стандартам, с правильной обработкой ошибок. Используйте для создания новых функций или сервисов.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ru/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Цикл саморефлексии" width="800"/>

*Итеративный цикл улучшения — генерировать, оценивать, выявлять проблемы, улучшать, повторять*

**Структурированный анализ** — для последовательной оценки. Модель проверяет код по фиксированной схеме (корректность, практики, производительность, безопасность, поддерживаемость). Используйте для ревью кода или оценки качества.

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

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Спросите про структурированный анализ:
> - «Как адаптировать рамочную схему анализа для разных типов ревью кода?»
> - «Как лучше парсить и программно использовать структурированный вывод?»
> - «Как обеспечить единообразие уровней серьёзности в различных сессиях проверки?»

<img src="../../../translated_images/ru/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Шаблон структурированного анализа" width="800"/>

*Рамочная схема для последовательных ревью кода с уровнями серьёзности*

**Многоходовой чат** — для разговоров, требующих контекста. Модель помнит предыдущие сообщения и строит ответ, опираясь на них. Используйте для интерактивной поддержки или сложных вопросов-ответов.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ru/context-memory.dff30ad9fa78832a.webp" alt="Память контекста" width="800"/>

*Как контекст беседы накапливается за несколько ходов до достижения лимита токенов*

**Пошаговое рассуждение** — для задач, где важна видимая логика. Модель показывает явное рассуждение на каждом этапе. Используйте для математики, логических задач или когда нужно понять ход мысли.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ru/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Пошаговый шаблон" width="800"/>

*Разбиение задач на явные логические шаги*

**Ограниченный вывод** — для ответов с конкретными требованиями к формату. Модель строго придерживается правил формата и длины. Используйте для резюме или когда нужен точный формат вывода.

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

<img src="../../../translated_images/ru/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Шаблон ограниченного вывода" width="800"/>

*Принудительное соблюдение конкретного формата, длины и структуры*

## Использование существующих ресурсов Azure

**Проверьте развертывание:**

Убедитесь, что файл `.env` существует в корневой директории с учётными данными Azure (создан в модуле 01):
```bash
cat ../.env  # Должен показывать AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запустите приложение:**

> **Примечание:** Если вы уже запускали все приложения с помощью `./start-all.sh` из модуля 01, этот модуль уже работает на порту 8083. Вы можете пропустить команды запуска ниже и перейти напрямую на http://localhost:8083.

**Вариант 1: Использование Spring Boot Dashboard (рекомендуется для пользователей VS Code)**

Dev контейнер включает расширение Spring Boot Dashboard, которое предоставляет визуальный интерфейс для управления всеми приложениями Spring Boot. Вы можете найти его в панели активности слева в VS Code (ищите иконку Spring Boot).
С панели Spring Boot Dashboard вы можете:
- Просматривать все доступные приложения Spring Boot в рабочем пространстве
- Запускать/останавливать приложения одним нажатием
- Смотреть логи приложений в реальном времени
- Отслеживать статус приложений

Просто нажмите кнопку запуска рядом с "prompt-engineering", чтобы запустить этот модуль, или запустите все модули сразу.

<img src="../../../translated_images/ru/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Вариант 2: использование shell-скриптов**

Запустите все веб-приложения (модули 01-04):

**Bash:**
```bash
cd ..  # Из корневого каталога
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Из корневого каталога
.\start-all.ps1
```

Или запустите только этот модуль:

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

Оба скрипта автоматически загружают переменные окружения из корневого файла `.env` и соберут JAR, если его нет.

> **Примечание:** Если вы предпочитаете собирать все модули вручную перед запуском:
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

Откройте в браузере http://localhost:8083.

**Для остановки:**

**Bash:**
```bash
./stop.sh  # Только этот модуль
# Или
cd .. && ./stop-all.sh  # Все модули
```

**PowerShell:**
```powershell
.\stop.ps1  # Только этот модуль
# Или
cd ..; .\stop-all.ps1  # Все модули
```

## Скриншоты приложения

<img src="../../../translated_images/ru/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Главная панель с отображением всех 8 шаблонов prompt engineering с их характеристиками и случаями использования*

## Изучение шаблонов

Веб-интерфейс позволяет экспериментировать с различными стратегиями формирования запросов (prompting). Каждый шаблон решает разные задачи — попробуйте их, чтобы понять, когда какой подход эффективен.

### Низкая и высокая степень "ожидания"

Задайте простой вопрос типа «Что такое 15% от 200?» с Низкой степенью ожидания. Вы получите мгновенный прямой ответ. А теперь задайте что-то сложное, например «Разработай стратегию кэширования для API с высоким трафиком» с Высокой степенью ожидания. Посмотрите, как модель замедлится и предоставит подробное обоснование. Та же модель, та же структура вопроса — но prompt говорит ей, сколько времени и усилий тратить на размышления.

<img src="../../../translated_images/ru/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Быстрый расчет с минимальным рассуждением*

<img src="../../../translated_images/ru/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Подробная стратегия кэширования (2.8MB)*

### Выполнение задач (пояснения инструментов)

Многошаговые рабочие процессы выигрывают от предварительного планирования и озвучивания прогресса. Модель описывает, что будет делать, рассказывает о каждом шаге, а потом подводит итоги.

<img src="../../../translated_images/ru/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Создание REST endpoint с пошаговым рассказом (3.9MB)*

### Саморефлексирующий код

Попробуйте «Создать сервис проверки email». Вместо просто генерации кода и остановки, модель создаёт код, оценивает его по критериям качества, выявляет недостатки и улучшает. Вы увидите, как она итеративно дорабатывает код до производственного стандарта.

<img src="../../../translated_images/ru/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Полный сервис валидации email (5.2MB)*

### Структурированный анализ

Код-ревью требует последовательных критериев оценки. Модель анализирует код по фиксированным категориям (корректность, практики, производительность, безопасность) с уровнем строгости.

<img src="../../../translated_images/ru/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ревью кода в рамках заданного фреймворка*

### Многоходовой чат

Спросите «Что такое Spring Boot?», а затем сразу же добавьте «Покажи пример». Модель запоминает первый вопрос и предоставляет конкретный пример Spring Boot. Без памяти второй вопрос был бы слишком расплывчатым.

<img src="../../../translated_images/ru/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Сохранение контекста между вопросами*

### Пошаговое рассуждение

Выберите математическую задачу и попробуйте решить её с помощью как Пошагового рассуждения, так и Низкой степени ожидания. Низкая степень даёт ответ быстро, но без пояснений. Пошаговое рассуждение демонстрирует каждое вычисление и решение.

<img src="../../../translated_images/ru/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математическая задача с детальными шагами*

### Ограниченный вывод

Когда важен определённый формат или количество слов, этот шаблон строго соблюдает требования. Попробуйте создать резюме ровно из 100 слов в виде пунктов.

<img src="../../../translated_images/ru/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резюме по машинному обучению с контролем формата*

## Чему вы действительно учитесь

**Степень размышлений меняет всё**

GPT-5.2 позволяет контролировать вычислительные усилия через prompts. Низкие усилия — быстрые ответы с минимальным исследованием. Высокие — глубокое вдумчивое размышление. Вы учитесь подбирать усилия под сложность задачи — не тратить время на простые вопросы, но не торопиться с сложными.

**Структура направляет поведение**

Заметили XML-теги в запросах? Это не просто украшения. Модели лучше следуют структурированным инструкциям, чем свободному тексту. Для многошаговых процессов и сложной логики структура помогает модели понимать, где она и что дальше.

<img src="../../../translated_images/ru/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомия хорошо структурированного prompt с чёткими секциями и XML-организацией*

**Качество через самооценку**

Саморефлексирующие шаблоны делают критерии качества явными. Вместо того, чтобы надеяться, что модель «сделает правильно», вы точно указываете, что значит «правильно»: корректная логика, обработка ошибок, производительность, безопасность. Модель может самостоятельно оценивать и улучшать результат. Это превращает генерацию кода из лотереи в предсказуемый процесс.

**Контекст ограничен**

Многошаговый чат работает, добавляя историю сообщений к каждому запросу. Но есть лимит — у каждой модели максимальное количество токенов. По мере роста диалогов нужны стратегии сохранения актуального контекста без превышения лимита. В этом модуле вы узнаете, как работает память; позже научитесь, когда обобщать, когда забывать, и когда извлекать информацию.

## Следующие шаги

**Следующий модуль:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигация:** [← Предыдущий: Модуль 01 - Введение](../01-introduction/README.md) | [Назад к началу](../README.md) | [Следующий: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, имейте в виду, что автоматический перевод может содержать ошибки и неточности. Оригинальный документ на исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->