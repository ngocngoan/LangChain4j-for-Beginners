# Модул 02: Проектиране на заявки с GPT-5.2

## Съдържание

- [Какво ще научите](../../../02-prompt-engineering)
- [Предварителни изисквания](../../../02-prompt-engineering)
- [Разбиране на проектирането на заявки](../../../02-prompt-engineering)
- [Основи на проектирането на заявки](../../../02-prompt-engineering)
  - [Безпримерно насочване (Zero-Shot Prompting)](../../../02-prompt-engineering)
  - [Насочване с малко примери (Few-Shot Prompting)](../../../02-prompt-engineering)
  - [Верига на мисълта](../../../02-prompt-engineering)
  - [Насочване базирано на роля](../../../02-prompt-engineering)
  - [Шаблони за заявки](../../../02-prompt-engineering)
- [Разширени шаблони](../../../02-prompt-engineering)
- [Използване на съществуващи ресурси в Azure](../../../02-prompt-engineering)
- [Снимки на приложението](../../../02-prompt-engineering)
- [Изследване на шаблоните](../../../02-prompt-engineering)
  - [Ниска срещу висока настоятелност](../../../02-prompt-engineering)
  - [Изпълнение на задача (Встъпителни части за инструменти)](../../../02-prompt-engineering)
  - [Саморефлектиращ код](../../../02-prompt-engineering)
  - [Структуриран анализ](../../../02-prompt-engineering)
  - [Многостъпков чат](../../../02-prompt-engineering)
  - [Разсъждение стъпка по стъпка](../../../02-prompt-engineering)
  - [Ограничен изход](../../../02-prompt-engineering)
- [Какво наистина научавате](../../../02-prompt-engineering)
- [Следващи стъпки](../../../02-prompt-engineering)

## Какво ще научите

<img src="../../../translated_images/bg/what-youll-learn.c68269ac048503b2.webp" alt="Какво ще научите" width="800"/>

В предишния модул видяхте как паметта позволява разговорен ИИ и използвахте моделите на GitHub за основни взаимодействия. Сега ще се съсредоточим върху това как задавате въпроси — самите заявки — използвайки GPT-5.2 на Azure OpenAI. Начинът, по който структурирате заявките си, драстично влияе върху качеството на получените отговори. Започваме с преглед на основните техники за заявки и след това преминаваме към осем разширени шаблона, които използват пълноценно възможностите на GPT-5.2.

Ще използваме GPT-5.2, защото той въвежда контрол на разсъжденията - можете да кажете на модела колко мислене да направи преди отговора. Това прави различните стратегии за насочване по-явни и ви помага да разберете кога да използвате всеки подход. Също така ще се възползваме от по-малките ограничения на скоростта на GPT-5.2 в Azure в сравнение с моделите на GitHub.

## Предварителни изисквания

- Завършен Модул 01 (разгърнати ресурси Azure OpenAI)
- Файл `.env` в коренната директория с Azure креденшъли (създаден от `azd up` в Модул 01)

> **Забележка:** Ако не сте завършили Модул 01, първо следвайте инструкциите за разгръщане там.

## Разбиране на проектирането на заявки

<img src="../../../translated_images/bg/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Какво е проектиране на заявки?" width="800"/>

Проектирането на заявки се състои в създаване на входен текст, който консистентно ви дава нужните резултати. Това не е просто задаване на въпроси - а структуриране на заявки така, че моделът да разбере точно какво искате и как да го достави.

Мислете за това като даване на инструкции на колега. "Поправи грешката" е неясно. "Поправи грешката null pointer exception в UserService.java на ред 45, като добавиш проверка за null" е специфично. Езиковите модели работят по същия начин - специфичността и структурата имат значение.

<img src="../../../translated_images/bg/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Как LangChain4j се вписва" width="800"/>

LangChain4j осигурява инфраструктурата — връзките с модели, паметта и типовете съобщения — докато шаблоните за заявки са просто внимателно структурирани текстове, които изпращате през тази инфраструктура. Ключовите градивни елементи са `SystemMessage` (който определя поведението и ролята на ИИ) и `UserMessage` (който носи вашето реално искане).

## Основи на проектирането на заявки

<img src="../../../translated_images/bg/five-patterns-overview.160f35045ffd2a94.webp" alt="Преглед на пет шаблона за проектиране на заявки" width="800"/>

Преди да се потопим в разширените шаблони в този модул, нека прегледаме пет основни техники за насочване. Това са строителните блокчета, които всеки инженер по заявки трябва да знае. Ако вече сте преминали през [Quick Start модула](../00-quick-start/README.md#2-prompt-patterns), вече сте ги видели в действие — ето концептуалната рамка зад тях.

### Безпримерно насочване (Zero-Shot Prompting)

Най-простият подход: дайте на модела директна инструкция без примери. Моделът разчита изцяло на обучението си, за да разбере и изпълни задачата. Това работи добре за прости искания, където очакваното поведение е очевидно.

<img src="../../../translated_images/bg/zero-shot-prompting.7abc24228be84e6c.webp" alt="Безпримерно насочване" width="800"/>

*Директна инструкция без примери — моделът изводи задачата само от инструкцията*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Отговор: "Положителен"
```

**Кога да се използва:** Прости класификации, директни въпроси, преводи или всяка задача, която моделът може да изпълни без допълнителни напътствия.

### Насочване с малко примери (Few-Shot Prompting)

Предоставяне на примери, които демонстрират шаблона, който искате моделът да следва. Моделът научава очаквания вход-изход формат от вашите примери и го прилага към нови входове. Това драстично подобрява консистентността за задачи, при които желаната форма или поведение не са очевидни.

<img src="../../../translated_images/bg/few-shot-prompting.9d9eace1da88989a.webp" alt="Насочване с малко примери" width="800"/>

*Учейки се от примери — моделът идентифицира шаблона и го прилага към нови входове*

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

**Кога да се използва:** Персонализирани класификации, консистентно форматиране, конкретни задачи в дадена област или когато безпримерните резултати са непостоянни.

### Верига на мисълта

Помолете модела да покаже разсъжденията си стъпка по стъпка. Вместо да скача директно към отговор, моделът разбива проблема и работи през всяка част напълно. Това подобрява точността при задачи с математика, логика и многостъпкови разсъждения.

<img src="../../../translated_images/bg/chain-of-thought.5cff6630e2657e2a.webp" alt="Верига на мисълта" width="800"/>

*Разсъждение стъпка по стъпка — разбиване на сложни проблеми на явни логически стъпки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Моделът показва: 15 - 8 = 7, после 7 + 12 = 19 ябълки
```

**Кога да се използва:** Математически задачи, логически пъзели, отстраняване на грешки или всяка задача, където показването на процеса на разсъждение подобрява точността и доверието.

### Насочване базирано на роля

Задайте персона или роля за ИИ преди да зададете въпроса си. Това осигурява контекст, който оформя тона, дълбочината и фокуса на отговора. "Софтуерен архитект" дава различни съвети от "младши разработчик" или "аудитиращ сигурността".

<img src="../../../translated_images/bg/role-based-prompting.a806e1a73de6e3a4.webp" alt="Насочване базирано на роля" width="800"/>

*Задаване на контекст и персона — един и същ въпрос получава различен отговор в зависимост от определената роля*

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

**Кога да се използва:** Прегледи на код, обучение, анализ, специфичен за домейна, или когато се нуждаете от отговори, адаптирани към определено ниво на експертиза или перспектива.

### Шаблони за заявки

Създаване на многократно използваеми заявки с променливи места за попълване. Вместо да пишете нова заявка всеки път, дефинирате шаблон веднъж и попълвате различни стойности. Класът `PromptTemplate` на LangChain4j улеснява това с синтаксис `{{variable}}`.

<img src="../../../translated_images/bg/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони за заявки" width="800"/>

*Многократно използваеми заявки с променливи места — един шаблон, много употреби*

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

**Кога да се използва:** Повтарящи се запитвания с различни входове, обработка на партиди, изграждане на многократно използваеми AI работни потоци или всяка ситуация, където структурата на заявката остава същата, но данните се променят.

---

Тези пет основи ви дават стабилен инструментарий за повечето задачи по проектиране на заявки. Останалата част от модула надгражда върху тях с **осем разширени шаблона**, които използват контрола на разсъжденията, самооценката и възможностите за структуриране на изхода на GPT-5.2.

## Разширени шаблони

След като основите са покрити, нека преминем към осемте разширени шаблона, които правят този модул уникален. Не всички проблеми изискват един и същ подход. Някои въпроси искат бързи отговори, други - дълбоко мислене. Някои искат видими разсъждения, други - само резултати. Всеки шаблон по-долу е оптимизиран за различен сценарий — а контролът на разсъжденията на GPT-5.2 прави тези разлики още по-явни.

<img src="../../../translated_images/bg/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Осем шаблона за насочване" width="800"/>

*Преглед на осемте шаблона за проектиране на заявки и техните случаи на употреба*

<img src="../../../translated_images/bg/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контрол на разсъжденията с GPT-5.2" width="800"/>

*Контролът на разсъжденията на GPT-5.2 ви позволява да зададете колко мислене да направи моделът — от бързи директни отговори до дълбоки изследвания*

**Ниска настоятелност (Бързо и фокусирано)** - За прости въпроси, при които искате бързи, директни отговори. Моделът прави минимално разсъждение - максимум 2 стъпки. Използвайте го за изчисления, търсения или праволинейни въпроси.

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

> 💡 **Изследвайте с GitHub Copilot:** Отворете [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и задайте:
> - "Каква е разликата между ниска и висока настоятелност при шаблоните?"
> - "Как XML таговете в заявките помагат за структурирането на отговора на ИИ?"
> - "Кога да използвам шаблони за саморефлексия спрямо директни инструкции?"

**Висока настоятелност (Дълбоко и задълбочено)** - За сложни проблеми, при които искате пълен анализ. Моделът изследва подробно и показва детайлни разсъждения. Използвайте го за системно проектиране, архитектурни решения или комплексни изследвания.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Изпълнение на задача (Процес стъпка по стъпка)** - За многостъпкови работни потоци. Моделът предоставя план предварително, описва всяка стъпка по време на изпълнението, след което прави обобщение. Използвайте го за миграции, реализации или всеки многостъпков процес.

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

Шаблонът „Верига на мисълта“ изрично иска от модела да покаже процеса на разсъждение, което подобрява точността при сложни задачи. Разбиването стъпка по стъпка помага и на хората, и на ИИ да разберат логиката.

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Попитайте за този шаблон:
> - "Как бих адаптирал шаблона за изпълнение на задача за дълготрайни операции?"
> - "Кои са добрите практики за структуриране на встъпителни части за инструменти в производствени приложения?"
> - "Как мога да улавям и показвам междинни актуализации за напредък в UI?"

<img src="../../../translated_images/bg/task-execution-pattern.9da3967750ab5c1e.webp" alt="Шаблон за изпълнение на задача" width="800"/>

*План → Изпълнение → Обобщение на работния поток за многостъпкови задачи*

**Саморефлектиращ код** - За генериране на производствено качествен код. Моделът генерира код според производствените стандарти с правилна обработка на грешки. Използвайте това при изграждане на нови функции или услуги.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bg/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Цикъл на саморефлексия" width="800"/>

*Итеративен цикъл за усъвършенстване - генериране, оценяване, идентифициране на проблеми, подобряване, повторение*

**Структуриран анализ** - За последователна оценка. Моделът преглежда кода, използвайки фиксирана рамка (коректност, практики, производителност, сигурност, поддръжка). Използвайте това за прегледи на код или оценки на качеството.

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

> **🤖 Опитайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Попитайте за структуриран анализ:
> - "Как мога да персонализирам рамката за анализ за различни видове прегледи на код?"
> - "Кой е най-добрият начин за парсване и работа със структуриран изход програмно?"
> - "Как да осигуря последователност на нивата на тежест в различни сесии на преглед?"

<img src="../../../translated_images/bg/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Шаблон за структуриран анализ" width="800"/>

*Рамка за консистентни прегледи на код с нива на тежест*

**Многостъпков чат** - За разговори, които изискват контекст. Моделът помни предишни съобщения и ги надгражда. Използвайте го за интерактивна помощ или сложен Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/bg/context-memory.dff30ad9fa78832a.webp" alt="Памет за контекст" width="800"/>

*Как контекстът на разговора се натрупва през множество стъпки до достигане на лимита на токени*

**Разсъждение стъпка по стъпка** - За задачи, изискващи видима логика. Моделът показва явни разсъждения за всяка стъпка. Използвайте го за математически задачи, логически пъзели или когато трябва да разберете мисловния процес.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bg/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Шаблон стъпка по стъпка" width="800"/>

*Разбиване на проблеми на явни логически стъпки*

**Ограничен изход** - За отговори със специфични изисквания по формат. Моделът строго следва правилата за формат и дължина. Използвайте го за резюмета или когато ви трябва прецизна структура на изхода.

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

<img src="../../../translated_images/bg/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Шаблон ограничен изход" width="800"/>

*Прилагане на специфични изисквания за формат, дължина и структура*

## Използване на съществуващи ресурси в Azure

**Проверете разгръщането:**

Уверете се, че файлът `.env` съществува в коренната директория с Azure креденшъли (създаден по време на Модул 01):
```bash
cat ../.env  # Трябва да показва AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Стартирайте приложението:**

> **Забележка:** Ако вече сте стартирали всички приложения чрез `./start-all.sh` от Модул 01, този модул вече работи на порт 8083. Можете да пропуснете командите за стартиране по-долу и директно да отидете на http://localhost:8083.

**Вариант 1: Използване на Spring Boot Dashboard (Препоръчително за потребители на VS Code)**

Dev контейнерът включва разширението Spring Boot Dashboard, което осигурява визуален интерфейс за управление на всички Spring Boot приложения. Можете да го намерите в Activity Bar от лявата страна на VS Code (потърсете иконата на Spring Boot).

От Spring Boot Dashboard можете:
- Да видите всички налични Spring Boot приложения в работната среда
- Да стартирате/спирате приложения с кликване
- Да разглеждате логовете на приложения в реално време
- Да следите статуса на приложенията
Просто кликнете върху бутона за възпроизвеждане до "prompt-engineering", за да стартирате този модул, или стартирайте всички модули наведнъж.

<img src="../../../translated_images/bg/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Опция 2: Използване на shell скриптове**

Стартиране на всички уеб приложения (модули 01-04):

**Bash:**
```bash
cd ..  # От кореновата директория
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # От главната директория
.\start-all.ps1
```

Или стартирайте само този модул:

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

И двата скрипта автоматично зареждат променливи на средата от кореновия `.env` файл и ще компилират JAR файловете, ако не съществуват.

> **Забележка:** Ако предпочитате да компилирате всички модули ръчно преди стартиране:
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

Отворете http://localhost:8083 в браузъра си.

**За спиране:**

**Bash:**
```bash
./stop.sh  # Само този модул
# Или
cd .. && ./stop-all.sh  # Всички модули
```

**PowerShell:**
```powershell
.\stop.ps1  # Само този модул
# Или
cd ..; .\stop-all.ps1  # Всички модули
```

## Скриншотове на приложението

<img src="../../../translated_images/bg/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Основният табло показва всички 8 модела на prompt engineering с техните характеристики и случаи на употреба*

## Изследване на моделите

Уеб интерфейсът ви позволява да експериментирате с различни стратегии за подканване. Всеки модел решава различни проблеми – опитайте ги, за да видите кога кой подход изпъква.

### Ниска срещу Висока Страст

Задайте прост въпрос като "Колко е 15% от 200?" с Ниска Страст. Ще получите мигновен и директен отговор. Сега задайте нещо сложно като "Проектирайте стратегия за кеширане за API с голям трафик" с Висока Страст. Гледайте как моделът забавя темпото и предоставя подробни обяснения. Същият модел, същата структура на въпроса – но prompt-ът му казва колко да мисли.

<img src="../../../translated_images/bg/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Бързо изчисление с минимално разсъждение*

<img src="../../../translated_images/bg/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Изчерпателна стратегия за кеширане (2.8MB)*

### Изпълнение на задачи (Преамбюли за инструменти)

Многостепенните работни потоци се ползват от предварително планиране и разказване на напредъка. Моделът очертава какво ще направи, разказва всяка стъпка и накрая обобщава резултатите.

<img src="../../../translated_images/bg/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Създаване на REST крайна точка с разказване стъпка по стъпка (3.9MB)*

### Саморефлективен код

Опитайте "Създай услуга за валидация на имейл". Вместо просто да генерира код и да спре, моделът генерира, оценява спрямо критерии за качество, идентифицира слабости и подобрява. Ще видите как итерира, докато кодът отговаря на производствените стандарти.

<img src="../../../translated_images/bg/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Пълна услуга за валидация на имейл (5.2MB)*

### Структурен анализ

Ревютата на код изискват последователни рамки за оценка. Моделът анализира кода, използвайки фиксирани категории (коректност, практики, производителност, сигурност) с нива на тежест.

<img src="../../../translated_images/bg/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Преглед на код, базиран на рамка*

### Многооборотен чат

Попитайте "Какво е Spring Boot?" след което веднага следвайте с "Покажи ми пример". Моделът помни първия ви въпрос и ви дава конкретен пример за Spring Boot. Без памет, вторият въпрос би бил твърде неясен.

<img src="../../../translated_images/bg/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Запазване на контекст между въпросите*

### Разсъждение стъпка по стъпка

Изберете математически проблем и го опитайте както с Разсъждение стъпка по стъпка, така и с Ниска Страст. Ниската страст просто ви дава отговора – бързо, но непрозрачно. Разсъждението стъпка по стъпка ви показва всяко изчисление и решение.

<img src="../../../translated_images/bg/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математически проблем с изрични стъпки*

### Ограничен изход

Когато ви трябват специфични формати или брой думи, този модел налага строго спазване. Опитайте да генерирате резюме с точно 100 думи в списъчен формат.

<img src="../../../translated_images/bg/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резюме за машинно обучение с контрол на формата*

## Какво наистина изучавате

**Усилието при разсъждение променя всичко**

GPT-5.2 ви позволява да контролирате изчислителното усилие чрез вашите prompt-и. Ниското усилие означава бързи отговори с минимално изследване. Високото усилие означава, че моделът отделя време за дълбоко мислене. Учите се да съобразявате усилието с комплексността на задачата – не си губете времето с прости въпроси, но и не бързайте с комплексни решения.

**Структурата насочва поведението**

Забелязвате XML таговете в prompt-ите? Те не са декоративни. Моделите следват структурирани инструкции по-надеждно от свободен текст. Когато имате многостепенни процеси или сложна логика, структурата помага на модела да проследява къде се намира и какво следва.

<img src="../../../translated_images/bg/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомия на добре структурирана подсказка с ясни секции и XML-стил организация*

**Качество чрез самооценка**

Саморефлективните модели работят чрез изрично задаване на критерии за качество. Вместо да се надявате, че моделът "ще го направи правилно", вие му казвате точно какво означава "правилно": коректна логика, обработка на грешки, производителност, сигурност. Моделът може да оцени своя изход и да се подобри. Това превръща генерирането на код от лотария в процес.

**Контекстът е краен**

Многооборотните разговори работят, като включват историята на съобщенията с всяка заявка. Но има лимит – всеки модел има максимален брой токени. С разрастването на разговорите ще са ви нужни стратегии за запазване на релевантния контекст без да достигнете тавана. Този модул ви показва как работи паметта; по-късно ще научите кога да обобщавате, кога да забравяте и кога да извличате.

## Следващи стъпки

**Следващ модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигация:** [← Предишен: Модул 01 - Въведение](../01-introduction/README.md) | [Обратно към Основното](../README.md) | [Следващ: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:  
Този документ е преведен с помощта на AI преводачески услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален превод от човек. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->