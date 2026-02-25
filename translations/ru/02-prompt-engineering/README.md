# Модуль 02: Инженерия подсказок с GPT-5.2

## Содержание

- [Что вы узнаете](../../../02-prompt-engineering)
- [Требования](../../../02-prompt-engineering)
- [Понимание инженерии подсказок](../../../02-prompt-engineering)
- [Основы инженерии подсказок](../../../02-prompt-engineering)
  - [Подсказки без примеров (Zero-Shot)](../../../02-prompt-engineering)
  - [Подсказки с несколькими примерами (Few-Shot)](../../../02-prompt-engineering)
  - [Цепочка рассуждений (Chain of Thought)](../../../02-prompt-engineering)
  - [Ролевые подсказки (Role-Based Prompting)](../../../02-prompt-engineering)
  - [Шаблоны подсказок (Prompt Templates)](../../../02-prompt-engineering)
- [Продвинутые шаблоны](../../../02-prompt-engineering)
- [Использование существующих ресурсов Azure](../../../02-prompt-engineering)
- [Скриншоты приложения](../../../02-prompt-engineering)
- [Изучение шаблонов](../../../02-prompt-engineering)
  - [Низкая и высокая настойчивость](../../../02-prompt-engineering)
  - [Выполнение задач (предисловия инструментов)](../../../02-prompt-engineering)
  - [Саморефлексирующий код](../../../02-prompt-engineering)
  - [Структурированный анализ](../../../02-prompt-engineering)
  - [Многоходовой чат](../../../02-prompt-engineering)
  - [Пошаговое рассуждение](../../../02-prompt-engineering)
  - [Ограниченный вывод](../../../02-prompt-engineering)
- [Что вы действительно изучаете](../../../02-prompt-engineering)
- [Следующие шаги](../../../02-prompt-engineering)

## Что вы узнаете

<img src="../../../translated_images/ru/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

В предыдущем модуле вы увидели, как память обеспечивает работу разговорного ИИ, и использовали модели GitHub для базовых взаимодействий. Теперь мы сосредоточимся на том, как задавать вопросы — собственно подсказки — используя GPT-5.2 от Azure OpenAI. Способ, которым вы строите подсказки, существенно влияет на качество ответов. Начнем с обзора базовых техник подсказок, затем перейдем к восьми продвинутым шаблонам, которые максимально используют возможности GPT-5.2.

Мы используем GPT-5.2, потому что он вводит управление рассуждениями — вы можете указать модели, насколько глубоко думать перед ответом. Это делает разные стратегии подсказок более очевидными и помогает понять, когда использовать каждый подход. Кроме того, Azure предоставляет меньше ограничений по частоте запросов для GPT-5.2 по сравнению с моделями GitHub.

## Требования

- Пройден модуль 01 (развернуты ресурсы Azure OpenAI)
- Файл `.env` в корневой директории с учетными данными Azure (создан командой `azd up` в модуле 01)

> **Примечание:** Если вы не закончили модуль 01, сначала следуйте инструкциям по развертыванию там.

## Понимание инженерии подсказок

<img src="../../../translated_images/ru/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Инженерия подсказок — это создание входного текста, который стабильно дает нужные вам результаты. Это не просто задавать вопросы — это структурировать запросы так, чтобы модель точно понимала, чего вы хотите и как это предоставить.

Представьте, что вы даете инструкции коллеге. «Исправь ошибку» — это расплывчато. «Исправь исключение null pointer в UserService.java на строке 45, добавив проверку на null» — конкретно. Языковые модели работают так же — важны конкретика и структура.

<img src="../../../translated_images/ru/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j предоставляет инфраструктуру — соединения моделей, память и типы сообщений — а шаблоны подсказок — это просто тщательно структурированный текст, который вы отправляете через эту инфраструктуру. Ключевые строительные блоки — `SystemMessage` (задающий поведение и роль ИИ) и `UserMessage` (которое содержит ваш фактический запрос).

## Основы инженерии подсказок

<img src="../../../translated_images/ru/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Прежде чем переходить к продвинутым шаблонам этого модуля, давайте рассмотрим пять основных методов подсказок. Это фундаментальные элементы, которые должен знать каждый инженер подсказок. Если вы уже проходили [модуль Быстрый старт](../00-quick-start/README.md#2-prompt-patterns), вы видели их в действии — здесь концептуальная основа.

### Подсказки без примеров (Zero-Shot Prompting)

Самый простой метод: дайте модели прямую инструкцию без примеров. Модель полностью опирается на своё обучение, чтобы понять и выполнить задачу. Хорошо работает для простых запросов, где ожидаемое поведение очевидно.

<img src="../../../translated_images/ru/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Прямая инструкция без примеров — модель выводит задачу только из инструкции*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Ответ: "Положительный"
```

**Когда использовать:** Простая классификация, прямые вопросы, переводы или любая задача, которую модель может выполнять без дополнительного руководства.

### Подсказки с несколькими примерами (Few-Shot Prompting)

Предоставьте примеры, которые демонстрируют нужный моделью шаблон. Модель изучает ожидаемый формат ввода-вывода из ваших примеров и применяет его к новым входным данным. Значительно улучшает последовательность для задач, где желаемый формат или поведение не очевидны.

<img src="../../../translated_images/ru/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Обучение на примерах — модель распознаёт шаблон и применяет его к новым данным*

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

**Когда использовать:** Пользовательская классификация, последовательное форматирование, задачи доменной специфики или если zero-shot результаты нестабильны.

### Цепочка рассуждений (Chain of Thought)

Попросите модель показать рассуждения пошагово. Вместо того, чтобы сразу дать ответ, модель разбивает проблему и явно рассматривает каждый этап. Повышает точность для математики, логики и многозадачного рассуждения.

<img src="../../../translated_images/ru/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Пошаговое рассуждение — разбор сложных задач на явные логические шаги*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модель показывает: 15 - 8 = 7, затем 7 + 12 = 19 яблок
```

**Когда использовать:** Математические задачи, логические головоломки, отладка или любая задача, где показ процесса рассуждения повышает точность и доверие.

### Ролевые подсказки (Role-Based Prompting)

Назначьте ИИ персону или роль перед вопросом. Это задает контекст, который формирует тональность, глубину и фокус ответа. «Архитектор ПО» даст другие советы, чем «младший разработчик» или «аудитор безопасности».

<img src="../../../translated_images/ru/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Установка контекста и персоны — один и тот же вопрос получает разные ответы в зависимости от роли*

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

**Когда использовать:** Код ревью, обучение, доменно-специфический анализ или когда нужны ответы, адаптированные под определенный уровень экспертизы или точку зрения.

### Шаблоны подсказок (Prompt Templates)

Создавайте повторно используемые подсказки с плейсхолдерами для переменных. Вместо написания новой подсказки каждый раз, определите шаблон один раз и подставляйте разные значения. Класс `PromptTemplate` LangChain4j облегчает это с синтаксисом `{{variable}}`.

<img src="../../../translated_images/ru/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Повторно используемые подсказки с переменными — один шаблон, много применений*

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

**Когда использовать:** Повторяющиеся запросы с разными входными данными, пакетная обработка, создание повторно используемых AI-процессов или любые случаи, когда структура подсказки остается неизменной, а данные меняются.

---

Эти пять основ дают прочный инструментарий для большинства задач подсказок. Остальная часть модуля строится на них с помощью **восьми продвинутых шаблонов**, использующих управление рассуждениями GPT-5.2, самооценку и возможности структурированного вывода.

## Продвинутые шаблоны

Покрыв основы, переходим к восьми продвинутым шаблонам, которые делают этот модуль уникальным. Не все задачи требуют одинакового подхода. Одни вопросы требуют быстрых ответов, другие — глубокого анализа. Одним нужен видимый процесс рассуждений, другим — только результат. Каждый шаблон ниже оптимизирован для конкретного сценария — и управление рассуждениями GPT-5.2 делает эти различия еще более явными.

<img src="../../../translated_images/ru/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Обзор восьми шаблонов инженерии подсказок и их применений*

<img src="../../../translated_images/ru/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Управление рассуждениями GPT-5.2 позволяет указать, сколько модель должна думать — от быстрых прямых ответов до глубокого анализа*

**Низкая настойчивость (Быстро и сфокусировано)** — для простых вопросов, где нужны быстрые, прямые ответы. Модель выполняет минимум рассуждений — максимум 2 шага. Используйте для вычислений, поиска или простых вопросов.

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

> 💡 **Попробуйте с GitHub Copilot:** Откройте [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и спросите:
> - «В чем разница между шаблонами подсказок с низкой и высокой настойчивостью?»
> - «Как теги XML в подсказках помогают структурировать ответ ИИ?»
> - «Когда стоит использовать шаблоны саморефлексии, а когда прямые инструкции?»

**Высокая настойчивость (Глубоко и тщательно)** — для сложных задач, требующих всестороннего анализа. Модель исследует подробно и показывает детальные рассуждения. Используйте для системного дизайна, архитектурных решений или комплексных исследований.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Выполнение задач (Пошаговый прогресс)** — для многоэтапных процессов. Модель сначала предоставляет план, рассказывает о каждом шаге в процессе, затем подводит итог. Применяйте для миграций, внедрения или любых многошаговых процессов.

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

Подсказки цепочки рассуждений (Chain of Thought) явным образом просят модель показать процесс рассуждения, что улучшает точность для сложных задач. Пошаговое разбиение помогает и людям, и ИИ понять логику.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Спросите про этот шаблон:
> - «Как адаптировать шаблон выполнения задач для длительных операций?»
> - «Какие лучшие практики для структурирования предисловий инструментов в продуктивных приложениях?»
> - «Как захватывать и показывать промежуточный прогресс в UI?»

<img src="../../../translated_images/ru/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*План → Выполнение → Резюме рабочего процесса многоступенчатых задач*

**Саморефлексирующий код** — для генерации кода производственного качества. Модель создаёт код с соблюдением стандартов производства и правильной обработкой ошибок. Используйте при создании новых функций или сервисов.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ru/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Цикл итеративного улучшения — генерация, оценка, выявление проблем, улучшение, повтор*

**Структурированный анализ** — для последовательной оценки. Модель проводит обзор кода по фиксированной схеме (корректность, практики, производительность, безопасность, сопровождаемость). Используйте для код-ревью или оценки качества.

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
> - «Как настроить фреймворк анализа для разных типов ревью кода?»
> - «Как лучше парсить и программно использовать структурированный вывод?»
> - «Как обеспечить единообразие уровней серьезности в разных сессиях ревью?»

<img src="../../../translated_images/ru/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Фреймворк для последовательного ревью кода с уровнями серьезности*

**Многоходовой чат** — для разговоров, требующих контекста. Модель запоминает предыдущие сообщения и строит ответы на их основе. Используйте для интерактивной поддержки или сложных Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ru/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Как контекст разговора накапливается за несколько ходов до достижения лимита токенов*

**Пошаговое рассуждение** — для задач, требующих видимой логики. Модель показывает явные рассуждения на каждом шаге. Используйте для математики, логических задач или если нужно понять ход мыслей.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ru/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Разбиение задачи на явные логические шаги*

**Ограниченный вывод** — для ответов с жесткими требованиями к формату. Модель строго соблюдает правила формата и длины. Используйте для резюме или когда нужен точный структурированный вывод.

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

<img src="../../../translated_images/ru/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Обеспечение соблюдения требований к формату, длине и структуре*

## Использование существующих ресурсов Azure

**Проверьте развертывание:**

Убедитесь, что файл `.env` существует в корне с учетными данными Azure (создан в модуле 01):
```bash
cat ../.env  # Должен показать AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Запустите приложение:**

> **Примечание:** Если вы уже запускали все приложения командой `./start-all.sh` из модуля 01, этот модуль уже работает на порту 8083. Можете пропустить запуск ниже и сразу перейти на http://localhost:8083.

**Вариант 1: Использование Spring Boot Dashboard (рекомендуется для пользователей VS Code)**

Dev контейнер включает расширение Spring Boot Dashboard, которое предоставляет визуальный интерфейс для управления всеми приложениями Spring Boot. Вы найдете его на панели активности слева в VS Code (значок Spring Boot).

Из Spring Boot Dashboard вы можете:
- Просматривать все доступные приложения Spring Boot в рабочей области
- Запускать/останавливать приложения одним кликом
- Смотреть логи приложений в реальном времени
- Контролировать статус приложений
Просто нажмите кнопку воспроизведения рядом с "prompt-engineering", чтобы запустить этот модуль, или запустите все модули сразу.

<img src="../../../translated_images/ru/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Вариант 2: Использование shell скриптов**

Запустите все веб-приложения (модули 01-04):

**Bash:**
```bash
cd ..  # Из корневой директории
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

Оба скрипта автоматически загружают переменные окружения из корневого файла `.env` и соберут JAR-файлы, если их нет.

> **Примечание:** Если вы предпочитаете собрать все модули вручную перед запуском:
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

Откройте http://localhost:8083 в вашем браузере.

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

*Главная панель с отображением всех 8 паттернов prompt engineering с их характеристиками и сценариями использования*

## Исследование паттернов

Веб-интерфейс позволяет экспериментировать с разными стратегиями подсказок. Каждый паттерн решает разные задачи — попробуйте их, чтобы понять, когда каждый из них эффективен.

> **Примечание: Потоковый vs Непотоковый** — На каждой странице паттерна есть две кнопки: **🔴 Потоковый ответ (вживую)** и опция **Непотоковый**. Потоковый режим использует Server-Sent Events (SSE) для отображения токенов в реальном времени по мере генерации модели, так что вы видите прогресс сразу. Непотоковый вариант ждет полного ответа перед отображением. Для подсказок с глубокой логикой (например, High Eagerness, Self-Reflecting Code) непотоковый вызов может занимать очень много времени — иногда минуты — без видимой обратной связи. **Используйте потоковый режим при экспериментах с сложными подсказками**, чтобы видеть работу модели и избежать ощущения таймаута.
>
> **Примечание: Требования к браузеру** — Потоковая функция использует Fetch Streams API (`response.body.getReader()`), который требует полноценный браузер (Chrome, Edge, Firefox, Safari). Она **не работает** во встроенном Simple Browser VS Code, поскольку его веб-просмотрщик не поддерживает ReadableStream API. Если вы используете Simple Browser, непотоковые кнопки будут работать как обычно — только потоковые кнопки не работают. Откройте `http://localhost:8083` во внешнем браузере для полного функционала.

### Низкая и высокая степень вовлеченности (Eagerness)

Задайте простой вопрос, например "Что такое 15% от 200?" используя Low Eagerness. Вы получите мгновенный, прямой ответ. Теперь спросите что-то сложное, например "Разработай стратегию кэширования для API с высокой нагрузкой", используя High Eagerness. Нажмите **🔴 Потоковый ответ (вживую)** и наблюдайте, как появляется подробный анализ токен за токеном. Та же модель, та же структура вопроса — но подсказка говорит ей, сколько думать.

### Выполнение задач (предисловия инструментов)

Многоэтапные рабочие процессы выигрывают от предварительного планирования и описания прогресса. Модель описывает, что собирается сделать, объясняет каждый шаг, а затем подводит итоги.

### Саморефлексирующий код

Попробуйте "Создать сервис проверки email". Вместо того, чтобы просто сгенерировать код и остановиться, модель генерирует, оценивает по критериям качества, выявляет слабые места и улучшает. Вы увидите, как она повторяет итерации, пока код не будет соответствовать производственным стандартам.

### Структурированный анализ

Код-ревью требует единых критериев оценки. Модель анализирует код по фиксированным категориям (корректность, практика, производительность, безопасность) с уровнями серьезности.

### Многоходовой чат

Спросите "Что такое Spring Boot?" и сразу же добавьте "Покажи пример". Модель помнит ваш первый вопрос и дает пример именно по Spring Boot. Без памяти второй вопрос был бы слишком общим.

### Пошаговое рассуждение

Выберите задачу по математике и попробуйте решить её как с Пошаговым рассуждением, так и с Low Eagerness. Низкая вовлеченность просто дает ответ — быстро, но без объяснений. Пошаговое рассуждение показывает все вычисления и решения.

### Ограниченный вывод

Когда нужны конкретные форматы или количество слов, этот паттерн строго их соблюдает. Попробуйте сгенерировать резюме ровно на 100 слов в формате списка.

## Чему вы действительно учитесь

**Усилия рассуждения меняют всё**

GPT-5.2 позволяет управлять вычислительными усилиями через подсказки. Низкие усилия — быстрые ответы с минимальным исследованием. Высокие усилия — модель тратит время на глубокое обдумывание. Вы учитесь подбирать усилия под сложность задачи — не тратьте время на простые вопросы, но и не спешите с комплексными решениями.

**Структура управляет поведением**

Замечали XML-теги в подсказках? Они не для красоты. Модели надежнее следуют структурированным инструкциям, чем свободному тексту. Если нужны многошаговые процессы или сложная логика, структура помогает модели отслеживать, на каком она шаге и что дальше.

<img src="../../../translated_images/ru/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомия хорошо структурированной подсказки с четкими секциями и XML-подобной организацией*

**Качество через самооценку**

Саморефлексирующие паттерны работают, делая критерии качества явными. Вместо того, чтобы надеяться, что модель "сделает правильно", вы четко говорите, что значит "правильно": корректная логика, обработка ошибок, производительность, безопасность. Модель может оценить собственный результат и улучшить его. Это превращает генерацию кода из лотереи в процесс.

**Контекст ограничен**

Многоходовой чат работает, включая историю сообщений в каждом запросе. Но есть лимит — у каждой модели максимальное количество токенов. По мере роста беседы нужно применять стратегии сохранения релевантного контекста, не превышая лимит. Этот модуль объясняет, как работает память, а позже вы узнаете, когда суммировать, когда забывать и когда извлекать информацию.

## Следующие шаги

**Следующий модуль:** [03-rag - RAG (генерация с расширением за счет поиска)](../03-rag/README.md)

---

**Навигация:** [← Предыдущий: Модуль 01 - Введение](../01-introduction/README.md) | [Назад к главному](../README.md) | [Следующий: Модуль 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с помощью сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, обратите внимание, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на исходном языке следует считать авторитетным источником. Для важной информации рекомендуется использовать профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->