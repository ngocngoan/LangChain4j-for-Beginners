# Модул 02: Инжењеринг промпта са GPT-5.2

## Садржај

- [Шта ћете научити](../../../02-prompt-engineering)
- [Претпоставке](../../../02-prompt-engineering)
- [Разумевање инжењеринга промпта](../../../02-prompt-engineering)
- [Основе инжењеринга промпта](../../../02-prompt-engineering)
  - [Zero-Shot промптинг](../../../02-prompt-engineering)
  - [Few-Shot промптинг](../../../02-prompt-engineering)
  - [Ланац мисли](../../../02-prompt-engineering)
  - [Промптинг заснован на улогама](../../../02-prompt-engineering)
  - [Шаблони промпта](../../../02-prompt-engineering)
- [Напредни шаблони](../../../02-prompt-engineering)
- [Коришћење постојећих Azure ресурса](../../../02-prompt-engineering)
- [Снимци екрана апликације](../../../02-prompt-engineering)
- [Истраживање шаблона](../../../02-prompt-engineering)
  - [Ниска и висока жељност](../../../02-prompt-engineering)
  - [Извршење задатка (уводници за алате)](../../../02-prompt-engineering)
  - [Саморефлектијући код](../../../02-prompt-engineering)
  - [Структурирана анализа](../../../02-prompt-engineering)
  - [Вишекратно ћаскање](../../../02-prompt-engineering)
  - [Разложење корак по корак](../../../02-prompt-engineering)
  - [Ограничен излаз](../../../02-prompt-engineering)
- [Шта заиста учите](../../../02-prompt-engineering)
- [Следећи кораци](../../../02-prompt-engineering)

## Шта ћете научити

<img src="../../../translated_images/sr/what-youll-learn.c68269ac048503b2.webp" alt="Шта ћете научити" width="800"/>

У претходном модулу сте видели како меморија омогућава конверзацијску вештачку интелигенцију и користили GitHub Models за основне интеракције. Сада ћемо се фокусирати на то како постављате питања — самим промптовима — користећи Azure OpenAI GPT-5.2. Начин на који структурирате своје промптове драматично утиче на квалитет одговора које добијате. Започињемо прегледом основних техника промптовања, а затим прелазимо на осам напредних образаца који у потпуности користе могућности GPT-5.2.

Користићемо GPT-5.2 јер уводи контролу размишљања - можете рећи моделу колико да мисли пре него што одговори. Ово чини различите стратегије промптовања јаснијим и помаже вам да разумете када да користите који приступ. Такође ћемо имати користи од мањих ограничења брзине Azure-а за GPT-5.2 у поређењу са GitHub Models.

## Претпоставке

- Завршен Модул 01 (деплоирани Azure OpenAI ресурси)
- `.env` фајл у коренском директоријуму са Azure акредитивима (направљен `azd up` у Модулу 01)

> **Белешка:** Ако нисте завршили Модул 01, прво пратите упутства за деплои.

## Разумевање инжењеринга промпта

<img src="../../../translated_images/sr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Шта је инжењеринг промпта?" width="800"/>

Инжењеринг промпта се односи на дизајнирање улазног текста који вам доследно даје потребне резултате. Није само питање постављања питања - ради се о структуирању упита тако да модел тачно разуме шта желите и како да то испоручи.

Замислите то као да давате упутства колеги. "Поправи грешку" је нејасно. "Поправи NullPointerException у UserService.java линија 45 додавањем провере null" је прецизно. Језички модели раде на исти начин - прецизност и структура су важни.

<img src="../../../translated_images/sr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Како LangChain4j уклапа" width="800"/>

LangChain4j пружа инфраструктуру — везе са моделом, меморију и типове порука — док су образци промпта само пажљиво структуиран текст који шаљете кроз ту инфраструктуру. Кључни градивни блокови су `SystemMessage` (који поставља понашање и улогу АИ) и `UserMessage` (који носи ваш стварни захтев).

## Основе инжењеринга промпта

<img src="../../../translated_images/sr/five-patterns-overview.160f35045ffd2a94.webp" alt="Преглед пет образаца инжењеринга промпта" width="800"/>

Пре него што пређемо на напредне шаблоне у овом модулу, хајде да прегледамо пет основних техника промптовања. Ово су градивни блокови које сваки инжењер промпта треба да зна. Ако сте већ прошли кроз [Quick Start модул](../00-quick-start/README.md#2-prompt-patterns), видели сте их у акцији — ево концептуалног оквира иза њих.

### Zero-Shot промптинг

Најједноставнији приступ: дајте моделу директну инструкцију без примера. Модел се у потпуности ослања на своју обуку да разуме и изврши задатак. Ово добро функционише за једноставне захтеве где је очекивано понашање очигледно.

<img src="../../../translated_images/sr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot промптинг" width="800"/>

*Директна инструкција без примера — модел изводи задатак само на основу упутства*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Одговор: „Позитиван“
```

**Када користити:** Једноставне класификације, директна питања, преводи или било који задатак који модел може обрадити без додатних смерница.

### Few-Shot промптинг

Пратите примере који приказују образац који желите да модел прати. Модел учи очекивани формат улаз-излаз из ваших примера и примењује га на нове улазе. Ово драматично побољшава конзистентност за задатке где жељени формат или понашање нису очигледни.

<img src="../../../translated_images/sr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot промптинг" width="800"/>

*Учење из примера — модел препознаје образац и примењује га на нове улазе*

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

**Када користити:** Прилагођене класификације, доследно форматирање, домен-специфични задаци или кад zero-shot резултати нису конзистентни.

### Ланац мисли

Питајте модел да приказује своје размишљање корак по корак. Уместо да одмах да одговор, модел раздваја проблем и јасно пролази кроз сваки део. Ово побољшава тачност код задатака из математике, логике и вишестепеног размишљања.

<img src="../../../translated_images/sr/chain-of-thought.5cff6630e2657e2a.webp" alt="Ланац мисли" width="800"/>

*Размишљање корак по корак — раздваја сложене проблеме у јасне логичке кораке*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модел показује: 15 - 8 = 7, затим 7 + 12 = 19 јабука
```

**Када користити:** Математички задаци, логичке загонетке, отклањање грешака или било који задатак где приказ размишљања побољшава тачност и поверење.

### Промптинг заснован на улогама

Поставите персону или улогу за АИ пре него што поставите питање. Ово додаје контекст који обликује тон, дубину и фокус одговора. „Софтверски архитекта“ даје другачије савете него „јуниор девелопер“ или „аудитор безбедности“.

<img src="../../../translated_images/sr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Промптинг заснован на улогама" width="800"/>

*Постављање контекста и персоне — исто питање добија различите одговоре у зависности од улоге*

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

**Када користити:** Прегледи кода, туторство, домен-специфична анализа или кад су вам потребни одговори прилагођени нивоу експертизе или перспективи.

### Шаблони промпта

Креирајте поновно употребљиве промптове са променљивим местима за убацивање. Уместо да сваки пут пишете нови промпт, дефинишите шаблон једном и попуњавајте различите вредности. LangChain4j класа `PromptTemplate` олакшава то са синтаксом `{{variable}}`.

<img src="../../../translated_images/sr/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони промпта" width="800"/>

*Поновно употребљиви промптови са променљивим местима — један шаблон, много примена*

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

**Када користити:** Поновљена питања са различитим улазима, серијска обрада, изградња поновно употребљивих АИ радних токова или било који сценарио где структура промпта остаје иста, али се подаци мењају.

---

Ових пет основа вам дају чврст алат за већину задатака промптовања. Остатак овог модула надограђује их са **осам напредних шаблона** који користе контролу размишљања GPT-5.2, самоевалуацију и могућности структурираног излаза.

## Напредни шаблони

Са покривеним основама, пређимо на осам напредних шаблона који овај модул чине јединственим. Ни сви проблеми не захтевају исти приступ. Нека питања траже брзе одговоре, друга дубоко размишљање. Нека нужно имају видљиво разматрање, а други само резултате. Сваки од доле наведених образаца је оптимизован за другачији сценарио — а контрола размишљања GPT-5.2 ове разлике чини још израженијим.

<img src="../../../translated_images/sr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Осам образаца промптовања" width="800"/>

*Преглед осам образаца инжењеринга промпта и њихових коришћења*

<img src="../../../translated_images/sr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контрола размишљања са GPT-5.2" width="800"/>

*Контрола размишљања GPT-5.2 вам омогућава да одредите колико модел треба да размишља — од брзих директних одговора до дубоке анализе*

**Ниска жељност (брзо и фокусирано)** - За једноставна питања када желите брзе, директне одговоре. Модел ради минимално размишљање - максимум 2 корака. Користите ово за калкулације, претраге или једноставна питања.

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

> 💡 **Истражите уз GitHub Copilot:** Отворите [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и питајте:
> - "Која је разлика између образаца ниске и високе жељности промптовања?"
> - "Како XML тагови у промптовима помажу у структуирању одговора АИ?"
> - "Када да користим образце за саморазмишљање, а када директне инструкције?"

**Висока жељност (дубоко и темељито)** - За комплексне проблеме када желите свеобухватну анализу. Модел темељно истражује и приказује детаљна размишљања. Користите ово за пројектовање система, архитеконске одлуке или сложено истраживање.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Извршење задатка (прогрес по корацима)** - За вишестепене радне токове. Модел пружа унапред план, описује сваки корак док ради, а затим даје резиме. Користите ово за миграције, имплементације или било који вишестепени процес.

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

Ланац мисли (Chain-of-Thought) промптинг изричито тражи од модела да покаже процес размишљања, побољшавајући тачност за сложене задатке. Разлагање корак по корак помаже и људима и АИ да разумеју логику.

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Питајте о овом шаблону:
> - "Како бих прилагодио образац извршења задатка за дуготрајне операције?"
> - "Које су најбоље праксе за структуирање уводника за алате у продукционим апликацијама?"
> - "Како да ухватим и прикажем интермедијерне напредкове у корисничком интерфејсу?"

<img src="../../../translated_images/sr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Образац извршења задатка" width="800"/>

*Планирај → Изврши → Сумирај радни ток за вишестепене задатке*

**Саморефлектијући код** - За генерисање кода производног квалитета. Модел генерише код који прати продукционе стандарде са одговарајућом обрадом грешака. Користите ово када правите нове функције или сервисе.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Циклус саморазмишљања" width="800"/>

*Итеративна петља побољшања - генериши, евалуирај, идентификуј проблеме, унапреди, понови*

**Структурирана анализа** - За доследну евалуацију. Модел прегледа код користећи фиксни оквир (тачност, праксе, перформансе, безбедност, одрживост). Користите ово за прегледе кода или процене квалитета.

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

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Питајте о структурираној анализи:
> - "Како да прилагодим рамки анализе за различите типове прегледа кода?"
> - "Који је најбољи начин да програмски парсирам и делујем на структуирани излаз?"
> - "Како да осигурам доследне нивое озбиљности кроз различите сесије прегледа?"

<img src="../../../translated_images/sr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Образац структуриране анализе" width="800"/>

*Оквир за доследне прегледе кода са нивоима озбиљности*

**Вишекратно ћаскање** - За разговоре којима је потребан контекст. Модел памти претходне поруке и надовезује се на њих. Користите ово за интерактивне помоћне сесије или комплексна питања и одговоре.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sr/context-memory.dff30ad9fa78832a.webp" alt="Контекст меморије" width="800"/>

*Како се контекст разговорa акумулира кроз више корака све док се не достигне лимит токена*

**Разложење корак по корак** - За проблеме где је потребна видљива логика. Модел приказује јасно размишљање за сваки корак. Користите ово за математичке проблеме, логичке загонетке или када желите разумети размишљање.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Образац корак по корак" width="800"/>

*Разлагање проблема у јасне логичке кораке*

**Ограничен излаз** - За одговоре са специфичним захтевима формата. Модел строго следи правила формата и дужине. Користите ово за резиме или кад вам треба прецизна структура излаза.

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

<img src="../../../translated_images/sr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Образац ограниченог излаза" width="800"/>

*Примена специфичних захтева формата, дужине и структуре*

## Коришћење постојећих Azure ресурса

**Проверите деплој:**

Уверите се да `.env` фајл постоји у коренском директоријуму са Azure акредитивима (направљен током Модула 01):
```bash
cat ../.env  # Треба да прикаже AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Покрените апликацију:**

> **Белешка:** Ако сте већ покренули све апликације користећи `./start-all.sh` из Модула 01, овај модул већ ради на порту 8083. Можете прескочити команде за покретање испод и директно отићи на http://localhost:8083.

**Опција 1: Коришћење Spring Boot управљача (препоручено за кориснике VS Code-а)**

Dev контејнер укључује екстензију Spring Boot управљач, која пружа визуелни интерфејс за управљање свим Spring Boot апликацијама. Можете је пронаћи у Activity Bar-у са леве стране VS Code-а (тражите Spring Boot икону).

Из Spring Boot управљача можете:
- Видећи све доступне Spring Boot апликације у радном простору
- Покретати/заустављати апликације једним кликом
- Прати логове апликација у реалном времену
- Пратити статус апликације
Једноставно кликните на дугме за репродукцију поред „prompt-engineering“ да бисте покренули овај модул, или покрените све модуле одједном.

<img src="../../../translated_images/sr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Опција 2: Коришћење shell скрипти**

Покрените све веб апликације (модуле 01-04):

**Bash:**
```bash
cd ..  # Из коренског директоријума
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Из коренског директоријума
.\start-all.ps1
```

Или покрените само овај модул:

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

Обe скриптe аутоматски учитавају променљиве окружења из коренског `.env` фајла и изградиће JAR фајлове ако не постоје.

> **Напомена:** Ако више волите да ручно изградите све модуле пре покретања:
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

Отворите http://localhost:8083 у вашем прегледачу.

**Да зауставите:**

**Bash:**
```bash
./stop.sh  # Само овај модул
# Или
cd .. && ./stop-all.sh  # Сви модули
```

**PowerShell:**
```powershell
.\stop.ps1  # Само овај модул
# Или
cd ..; .\stop-all.ps1  # Сви модули
```

## Снимци екрана апликације

<img src="../../../translated_images/sr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Главна контрола табла која приказује свих 8 шаблона за prompt engineering са њиховим карактеристикама и применама*

## Истраживање шаблона

Веб интерфејс вам омогућава експериментисање са различитим стратегијама упита. Сваки шаблон решава различите проблеме - испробајте их да видите када је који приступ најбољи.

### Низак насупрот високом ангажману

Поставите једноставно питање као што је „Колико је 15% од 200?“ користећи низак ангажман. Добићете тренутан, директан одговор. Сада поставите нешто сложеније као „Осмислите стратегију кеширања за API са великим саобраћајем“ користећи висок ангажман. Погледајте како модел успорава и пружа детаљну аргументацију. Исти модел, иста структура питања - али упит му говори колико треба размишљати.

<img src="../../../translated_images/sr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Брзо рачунање са минималним размишљањем*

<img src="../../../translated_images/sr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Свеобухватна стратегија кеширања (2.8MB)*

### Извршавање задатка (уводи за алате)

Вишестепени процеси имају користи од унапред планирања и приповедања напретка. Модел наводи шта ће урадити, описује сваки корак, а затим резимира резултате.

<img src="../../../translated_images/sr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Креирање REST крајње тачке са приповедањем корак по корак (3.9MB)*

### Само-рефлексивни код

Пробајте „Креирајте услугу за валидацију е-поште“. Уместо само генерисања кода и заустављања, модел генерише, процењује по квалитету, идентификује слабости и унапређује. Видећете како итеративно ради док код не испуњава стандарде производње.

<img src="../../../translated_images/sr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Комплетна услуга за валидацију е-поште (5.2MB)*

### Структурирана анализа

Прегледи кода захтевају доследне оквире за процену. Модел анализира код користећи фиксне категорије (исправност, праксе, перформансе, безбедност) са нивоима озбиљности.

<img src="../../../translated_images/sr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Преглед кода заснован на оквиру*

### Мулти-круг ћаскања

Питајте „Шта је Spring Boot?“ па одмах наставите са „Покажи ми пример“. Модел памти ваше прво питање и даје пример специфично за Spring Boot. Без меморије, друго питање би било превише неодређено.

<img src="../../../translated_images/sr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Чување контекста кроз питања*

### Разлагање размишљања корак по корак

Изаберите математички задатак и испробајте га са и са корак по корак размишљањем и са ниским ангажманом. Низак ангажман вам даје само одговор - брз али непрозилан. Корак по корак вам показује сваки рачун и одлуку.

<img src="../../../translated_images/sr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математички задатак са експлицитним корацима*

### Ограничен излаз

Када вам требају специфични формати или број речи, овај шаблон строго контролише поштовање захтева. Испробајте генерисање резимеа од тачно 100 речи у облику нумерисаних поена.

<img src="../../../translated_images/sr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резиме машинског учења са контролом формата*

## Шта заправо учите

**Ниво напора у размишљању мења све**

GPT-5.2 вам омогућава да контролишете рачунски напор кроз ваше упите. Низак напор значи брзе одговоре са минималним истраживањем. Висок напор значи да модел узима време за дубоко размишљање. Учите да подесите напор према сложености задатка - не троšите време на једноставна питања, али немојте журити ни код сложених одлука.

**Структура усмерава понашање**

Приметили сте XML тагове у упитима? Они нису декоративни. Модели поузданије прате структурирана упутства него слободан текст. Када су вам потребни мулти-кориснички процеси или сложена логика, структура помаже моделу да прати где се налази и шта следи.

<img src="../../../translated_images/sr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомија добро структурираног уноса са јасним секцијама и XML-стил организацијом*

**Квалитет кроз самоевалуацију**

Шаблони који рефлектују сами себе раде тако што квалитативне критеријуме чине експлицитним. Уместо да се надаш да модел „уради исправно“, ти му тачно кажеш шта „исправно“ значи: тачна логика, руковање грешкама, перформансе, безбедност. Модел онда сам процењује свој излаз и унапређује га. Ово претвара генерисање кода из лутрије у процес.

**Контекст је ограничен**

Вишекратне разговоре остварујете укључивањем историје порука са сваким захтевом. Али постоји ограничење - сваки модел има максималан број токена. Како разговори расту, требаће вам стратегије да задржите релевантан контекст без досезања тог лимита. Овај модул вам показује како меморија функционише; касније ћете научити када да резимирате, када да заборавите и када да вратите.

## Следећи кораци

**Следећи модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигација:** [← Претходно: Модул 01 - Увод](../01-introduction/README.md) | [Назад на Главну](../README.md) | [Следеће: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Искључење одговорности**:  
Овај документ је преведен коришћењем AI сервиса за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо прецизности, молимо вас да имате у виду да аутоматизовани преводи могу садржати грешке или нетачности. Првобитни документ на матерњем језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални превод од стране стручног лингвисте. Нисмо одговорни за било каква непоразумевања или неправилна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->