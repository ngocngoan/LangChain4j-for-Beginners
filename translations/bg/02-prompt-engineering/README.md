# Модул 02: Инженеринг на заявки с GPT-5.2

## Съдържание

- [Какво ще научите](../../../02-prompt-engineering)
- [Предварителни изисквания](../../../02-prompt-engineering)
- [Разбиране на инженерството на заявки](../../../02-prompt-engineering)
- [Основи на инженерството на заявки](../../../02-prompt-engineering)
  - [Zero-Shot заявки](../../../02-prompt-engineering)
  - [Few-Shot заявки](../../../02-prompt-engineering)
  - [Верига от мисли](../../../02-prompt-engineering)
  - [Заявки базирани на роля](../../../02-prompt-engineering)
  - [Шаблони за заявки](../../../02-prompt-engineering)
- [Разширени модели](../../../02-prompt-engineering)
- [Използване на съществуващи ресурси в Azure](../../../02-prompt-engineering)
- [Екранни снимки на приложението](../../../02-prompt-engineering)
- [Разглеждане на шаблоните](../../../02-prompt-engineering)
  - [Ниска срещу висока настойчивост](../../../02-prompt-engineering)
  - [Изпълнение на задача (Въведения към инструменти)](../../../02-prompt-engineering)
  - [Саморефлектиращ код](../../../02-prompt-engineering)
  - [Структуриран анализ](../../../02-prompt-engineering)
  - [Мултиобменен чат](../../../02-prompt-engineering)
  - [Стъпка по стъпка разсъждение](../../../02-prompt-engineering)
  - [Ограничен изход](../../../02-prompt-engineering)
- [Какво наистина научавате](../../../02-prompt-engineering)
- [Следващи стъпки](../../../02-prompt-engineering)

## Какво ще научите

<img src="../../../translated_images/bg/what-youll-learn.c68269ac048503b2.webp" alt="Какво ще научите" width="800"/>

В предишния модул видяхте как паметта позволява разговорен AI и използвахте GitHub модели за основни интеракции. Сега ще се съсредоточим върху начина, по който задавате въпроси — самите заявки — използвайки GPT-5.2 на Azure OpenAI. Начинът, по който структурирате заявките си, влияе драстично върху качеството на отговорите, които получавате. Започваме с преглед на основните техники за заявки, след което преминаваме към осем разширени модела, които използват пълноценно възможностите на GPT-5.2.

Ще използваме GPT-5.2, защото той въвежда контрол на разсъжденията — можете да кажете на модела колко да мисли преди да отговори. Това прави различните стратегии за заявки по-явни и ви помага да разберете кога да използвате всяко от тях. Също така ще се възползваме от по-малките ограничения за скорост при Azure в сравнение с GitHub модели.

## Предварителни изисквания

- Завършен Модул 01 (развити Azure OpenAI ресурси)
- Файл `.env` в кореновата директория с Azure данни за достъп (създаден от `azd up` в Модул 01)

> **Бележка:** Ако не сте завършили Модул 01, първо следвайте инструкциите за разгръщане там.

## Разбиране на инженерството на заявки

<img src="../../../translated_images/bg/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Какво е инженерство на заявки?" width="800"/>

Инженерството на заявки е проектирането на входен текст, който последователно да ви дава нужните резултати. Не става само въпрос за задаване на въпроси — а за структурирането на исканията така, че моделът да разбере точно какво искате и как да го предостави.

Представете си, че давате инструкции на колега. „Поправи грешката“ е неопределено. „Поправи NullPointerException в UserService.java на ред 45, като добавиш проверка за null“ е конкретно. Езиковите модели работят по същия начин — конкретността и структурата имат значение.

<img src="../../../translated_images/bg/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Как LangChain4j се вписва" width="800"/>

LangChain4j осигурява инфраструктурата — връзки към моделите, памет и типове съобщения — докато шаблоните за заявки са просто внимателно структурирани текстове, които изпращате през тази инфраструктура. Ключовите градивни елементи са `SystemMessage` (който задава поведението и ролята на AI) и `UserMessage` (който носи вашето реално искане).

## Основи на инженерството на заявки

<img src="../../../translated_images/bg/five-patterns-overview.160f35045ffd2a94.webp" alt="Преглед на петте шаблона на инженерство на заявки" width="800"/>

Преди да преминем към разширените модели в този модул, нека прегледаме пет основни техники за заявки. Те са градивните блокове, които всеки инженер на заявки трябва да познава. Ако вече сте минали през [Бърз старт модул](../00-quick-start/README.md#2-prompt-patterns), вече сте ги виждали в действие — ето концептуалната рамка зад тях.

### Zero-Shot заявки

Най-простият подход: да се даде на модела директна инструкция без примери. Моделът разчита изцяло на обучението си, за да разбере и изпълни задачата. Този подход работи добре за прости искания, където очакваното поведение е ясно.

<img src="../../../translated_images/bg/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot заявки" width="800"/>

*Директна инструкция без примери — моделът извлича задачата само от инструкцията*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Отговор: "Положителен"
```

**Кога да се използва:** Прости класификации, директни въпроси, преводи или всяка задача, която моделът може да реши без допълнителни насоки.

### Few-Shot заявки

Давате примери, които демонстрират шаблона, който искате моделът да следва. Моделът научава очаквания формат вход-изход от вашите примери и го прилага върху нови входни данни. Това значително подобрява консистентността за задачи, където желаната форма или поведение не са очевидни.

<img src="../../../translated_images/bg/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot заявки" width="800"/>

*Учейки се от примери — моделът идентифицира шаблона и го прилага върху нови входове*

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

**Кога да се използва:** Персонализирани класификации, последователно форматиране, специфични за домейн задачи или когато резултатите от zero-shot са непоследователни.

### Верига от мисли

Искате от модела да покаже своя процес на мислене стъпка по стъпка. Вместо да скача директно към отговор, моделът разбива проблема и решава всяка част експлицитно. Това повишава точността при задачи по математика, логика и мултистъпково разсъждение.

<img src="../../../translated_images/bg/chain-of-thought.5cff6630e2657e2a.webp" alt="Верига от мисли" width="800"/>

*Стъпка по стъпка разсъждение — разбиване на сложни проблеми в ясни логически стъпки*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Моделът показва: 15 - 8 = 7, след това 7 + 12 = 19 ябълки
```

**Кога да се използва:** Математически задачи, логически пъзели, отстраняване на грешки или всяка задача, където показването на процеса на разсъждение подобрява точността и доверието.

### Заявки базирани на роля

Задавате на AI персона или роля преди да зададете въпрос. Това осигурява контекст, който оформя тона, дълбочината и фокуса на отговора. „Софтуерен архитект“ дава различен съвет от „начинаещ разработчик“ или „сигурностен одитор“.

<img src="../../../translated_images/bg/role-based-prompting.a806e1a73de6e3a4.webp" alt="Заявки базирани на роля" width="800"/>

*Задаване на контекст и персона — един и същ въпрос получава различен отговор в зависимост от зададената роля*

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

**Кога да се използва:** Прегледи на код, обучение, анализи специфични за домейн или когато ви трябват отговори, съобразени с определено ниво на експертиза или гледна точка.

### Шаблони за заявки

Създайте многократно използваеми заявки с променливи на местата. Вместо да пишете нова заявка всеки път, дефинирате шаблон веднъж и попълвате различни стойности. Класът `PromptTemplate` на LangChain4j улеснява това със синтаксиса `{{variable}}`.

<img src="../../../translated_images/bg/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони за заявки" width="800"/>

*Многократно използваеми заявки с променливи — един шаблон, много приложения*

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

**Кога да се използва:** Повтарящи се запитвания с различен вход, обработка на пакети, изграждане на многократно използваеми AI работни потоци или всеки сценарий, където структурата на заявката остава същата, но данните се променят.

---

Тези пет основи ви дават солиден набор от инструменти за повечето задачи със заявки. Останалата част от този модул ги надгражда с **осем разширени модела**, които използват контрола на разсъжденията, самооценката и възможностите за структуриран изход на GPT-5.2.

## Разширени модели

След като основите са разгледани, нека преминем към осемте разширени модела, които правят този модул уникален. Не всички проблеми изискват един и същ подход. Някои въпроси имат нужда от бързи отговори, други — от дълбоко мислене. Някои изискват видими разсъждения, други — само резултати. Всеки от моделите по-долу е оптимизиран за различен сценарий — а контролът на разсъжденията на GPT-5.2 прави тези разлики още по-явни.

<img src="../../../translated_images/bg/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Осем шаблона за заявки" width="800"/>

*Преглед на осемте шаблона за инженерство на заявки и случаите на използване*

<img src="../../../translated_images/bg/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контрол на разсъждението с GPT-5.2" width="800"/>

*Контролът на разсъжденията на GPT-5.2 ви позволява да определите колко да мисли моделът — от бързи директни отговори до дълбоко изследване*

<img src="../../../translated_images/bg/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Сравнение на усилията за разсъждение" width="800"/>

*Ниска настойчивост (бързо, директно) срещу висока настойчивост (задълбочено, изследователско) при подходите за разсъждение*

**Ниска настойчивост (бързо и фокусирано)** - За прости въпроси, когато искате бързи, директни отговори. Моделът прави минимално разсъждение - максимум 2 стъпки. Използвайте го за изчисления, справки или прости въпроси.

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

> 💡 **Изследвайте с GitHub Copilot:** Отворете [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и задайте въпроси:
> - "Каква е разликата между ниска и висока настойчивост в шаблоните за заявки?"
> - "Как XML таговете в заявките помагат за структурирането на отговора на AI?"
> - "Кога да използвам шаблони с самоотразяване вместо директна инструкция?"

**Висока настойчивост (задълбочено и изчерпателно)** - За сложни проблеми, когато искате пълноценен анализ. Моделът изследва в дълбочина и показва подробни разсъждения. Използвайте го за проектиране на системи, архитектурни решения или сложни изследвания.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Изпълнение на задача (прогрес стъпка по стъпка)** - За многократни стъпки в работния процес. Моделът дава план предварително, описва всяка стъпка докато работи, след което прави обобщение. Използвайте го за миграции, реализации или всеки многократен процес.

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

Заявката верига от мисли изрично иска от модела да покаже процеса на разсъждение, подобрявайки точността за сложни задачи. Разбиването на стъпки помага както на хора, така и на AI да разберат логиката.

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Попитайте за този модел:
> - "Как бих адаптирал шаблона за изпълнение на задачи за дълги операции?"
> - "Кои са най-добрите практики за структурирането на въведения към инструменти в продукционни приложения?"
> - "Как мога да заснема и показвам междинни актуализации на прогреса в потребителския интерфейс?"

<img src="../../../translated_images/bg/task-execution-pattern.9da3967750ab5c1e.webp" alt="Модел за изпълнение на задача" width="800"/>

*Работен процес План → Изпълнение → Обобщение за многократни стъпки*

**Саморефлектиращ код** - За генериране на код с качество за продукция. Моделът пише код, следвайки производствените стандарти и с правилна обработка на грешки. Използвайте го, когато изграждате нови функции или услуги.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bg/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Цикъл на саморефлексия" width="800"/>

*Итеративен цикъл за подобрение - генериране, оценка, идентифициране на проблеми, подобрение, повторение*

**Структуриран анализ** - За последователна оценка. Моделът преглежда кода, използвайки фиксирана рамка (коректност, практики, производителност, сигурност, поддръжка). Използвайте го за прегледи на код или оценки на качеството.

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

> **🤖 Изпробвайте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Попитайте за структурирания анализ:
> - "Как да персонализирам рамката за анализ за различни типове прегледи на код?"
> - "Какъв е най-добрият начин да парсвам и да използвам структурирания изход програмно?"
> - "Как да гарантирам последователно ниво на тежест по време на различни прегледни сесии?"

<img src="../../../translated_images/bg/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Модел за структуриран анализ" width="800"/>

*Рамка за последователни прегледи на код с нива на тежест*

**Мултиобменен чат** - За разговори, които имат нужда от контекст. Моделът помни предишни съобщения и ги надгражда. Използвайте го за интерактивни помощни сесии или сложни въпроси и отговори.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/bg/context-memory.dff30ad9fa78832a.webp" alt="Памет на контекста" width="800"/>

*Как контекстът на разговора се натрупва през множество ходове докато достигне лимита на токени*

**Стъпка по стъпка разсъждение** - За проблеми, изискващи видима логика. Моделът показва явен процес на разсъждение за всяка стъпка. Използвайте го за математически задачи, логически пъзели или когато ви трябва да разберете процеса на мислене.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/bg/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Модел стъпка по стъпка" width="800"/>

*Разбиване на проблемите в ясни логически стъпки*

**Ограничен изход** - За отговори със специфични изисквания към формата. Моделът строго спазва правилата за формат и дължина. Използвайте го за обобщения или когато ви трябва прецизна структура на изхода.

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

<img src="../../../translated_images/bg/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Модел с ограничен изход" width="800"/>

*Прилагане на специфични изисквания за формат, дължина и структура*

## Използване на съществуващи ресурси в Azure

**Проверете разгръщането:**

Уверете се, че файлът `.env` съществува в кореновата директория с вашите Azure данни за достъп (създаден по време на Модул 01):
```bash
cat ../.env  # Трябва да покаже AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Стартирайте приложението:**

> **Бележка:** Ако вече сте стартирали всички приложения със `./start-all.sh` от Модул 01, този модул вече работи на порт 8083. Можете да пропуснете стартиращите команди по-долу и да отидете директно на http://localhost:8083.

**Опция 1: Използване на Spring Boot Dashboard (Препоръчително за потребители на VS Code)**

Развойната среда включва разширението Spring Boot Dashboard, което осигурява визуален интерфейс за управление на всички Spring Boot приложения. Можете да го намерите в лентата с активността вляво във VS Code (потърсете иконата с Spring Boot).
От Spring Boot таблото за управление можете да:
- Видите всички налични Spring Boot приложения в работната среда
- Стартирате/спирате приложения с един клик
- Преглеждате логовете на приложенията в реално време
- Следите състоянието на приложенията

Просто кликнете бутона за пускане до "prompt-engineering", за да стартирате този модул, или стартирайте всички модули наведнъж.

<img src="../../../translated_images/bg/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Опция 2: Използване на shell скриптове**

Стартирайте всички уеб приложения (модули 01-04):

**Bash:**
```bash
cd ..  # От основната директория
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # От коренната директория
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

Двата скрипта автоматично зареждат променливите на средата от основния `.env` файл и ще изградят JAR файловете, ако не съществуват.

> **Забележка:** Ако предпочитате да изградите всички модули ръчно преди стартирането:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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

## Скрийншотове от приложението

<img src="../../../translated_images/bg/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Основното табло за управление, показващо всички 8 шаблона за prompt engineering с техните характеристики и случаи на употреба*

## Изследване на шаблоните

Уеб интерфейсът ви позволява да експериментирате с различни стратегии на въвеждане. Всеки шаблон решава различни проблеми - опитайте ги, за да видите кога всеки подход работи най-добре.

### Ниско срещу Високо Настроено Настроение (Eagerness)

Задайте прост въпрос като "Какво е 15% от 200?" с Ниско Настроено Настроение (Low Eagerness). Ще получите незабавен, директен отговор. Сега задайте нещо сложно като "Проектирайте стратегия за кеширане за високонапливащо API" с Високо Настроено Настроение (High Eagerness). Наблюдавайте как моделът забавя и предоставя детайлни обяснения. Същият модел, същата структура на въпроса - но prompt-ът му казва колко много мислене да направи.

<img src="../../../translated_images/bg/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Бързо изчисление с минимално обмисляне*

<img src="../../../translated_images/bg/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Изчерпателна стратегия за кеширане (2.8MB)*

### Изпълнение на задачи (Въведение с инструменти)

Многостепенните работни потоци се възползват от предварително планиране и разказване на напредъка. Моделът описва какво ще направи, разказва всяка стъпка и обобщава резултатите.

<img src="../../../translated_images/bg/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Създаване на REST ентпойнт с разказване стъпка по стъпка (3.9MB)*

### Код с саморефлексия

Опитайте “Създай услуга за валидиране на имейли”. Вместо просто да генерира код и да спре, моделът генерира, оценява спрямо критерии за качество, идентифицира слабости и подобрява. Ще видите как той итеративно подобрява, докато кодът отговаря на производствените стандарти.

<img src="../../../translated_images/bg/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Пълна услуга за валидиране на имейли (5.2MB)*

### Структуриран анализ

Прегледите на кода изискват последователни рамки за оценяване. Моделът анализира кода по фиксирани категории (правилност, практики, производителност, сигурност) със степен на тежест.

<img src="../../../translated_images/bg/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Преглед на код, базиран на рамки*

### Многообменен чат

Попитайте "Какво е Spring Boot?" и веднага след това продължете с "Покажи ми пример". Моделът си спомня първия ви въпрос и ви дава конкретен пример за Spring Boot. Без памет, вторият въпрос щеше да е прекалено общ.

<img src="../../../translated_images/bg/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Запазване на контекста между въпросите*

### Мислене стъпка по стъпка

Изберете математически проблем и го опитайте както със Стъпково Мислене, така и с Ниско Настроено Настроение. Ниското настроено просто ви дава отговора - бързо, но непрозрачно. Стъпковото показва всяко изчисление и решение.

<img src="../../../translated_images/bg/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математически проблем с явни стъпки*

### Ограничен Изход

Когато имате нужда от конкретни формати или брой думи, този шаблон налага строго спазване. Опитайте да генерирате резюме с точно 100 думи в булет формат.

<img src="../../../translated_images/bg/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резюме за машинно обучение с контрол на формата*

## Това, което наистина учите

**Усилието при мислене променя всичко**

GPT-5.2 ви позволява да контролирате изчислителното усилие чрез вашите подканвания. Ниско усилие означава бързи отговори с минимално изследване. Високото означава, че моделът отделя време да мисли дълбоко. Учите се да съобразявате усилието с комплексността на задачата - не губете време за прости въпроси, но и не бързайте с комплексни решения.

**Структурата насочва поведението**

Забелязвате ли XML таговете в подканите? Те не са декоративни. Моделите следват структурирани инструкции по-надеждно от свободен текст. Когато имате нужда от многостъпкови процеси или сложна логика, структурата помага на модела да проследи къде е и какво идва след това.

<img src="../../../translated_images/bg/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомия на добре структуриран prompt с ясни секции и организация в XML стил*

**Качество чрез самооценка**

Шаблоните с саморефлексия работят като правят критериите за качество явни. Вместо да се надявате моделът „да го направи правилно“, вие му казвате точно какво означава „правилно“: коректна логика, обработка на грешки, производителност, сигурност. Моделът може след това да оцени собствения си изход и да се подобри. Това превръща генерирането на код от лотария в процес.

**Контекстът е ограничен**

Многообменните разговори работят като включват историята на съобщенията с всяка заявка. Но има лимит - всеки модел има максимален брой токени. Когато разговорите растат, ще ви трябват стратегии да поддържате релевантен контекст без да достигате този лимит. Този модул ви показва как работи паметта; по-късно ще научите кога да резюмирате, кога да забравяте и кога да извличате.

## Следващи стъпки

**Следващ модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигация:** [← Предишен: Модул 01 - Въведение](../01-introduction/README.md) | [Обратно към Основното](../README.md) | [Следващ: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:  
Този документ е преведен с помощта на AI преводаческия сервис [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия език трябва да се счита за официален източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, възникнали в резултат на използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->