# Модул 02: Рад са упутствима уз GPT-5.2

## Садржај

- [Шта ћете научити](../../../02-prompt-engineering)
- [Претпоставке](../../../02-prompt-engineering)
- [Разумевање рада са упутствима](../../../02-prompt-engineering)
- [Основе рада са упутствима](../../../02-prompt-engineering)
  - [Ништа-пример (Zero-Shot) упућивање](../../../02-prompt-engineering)
  - [Неколико примера (Few-Shot) упућивање](../../../02-prompt-engineering)
  - [Ланац размишљања](../../../02-prompt-engineering)
  - [Упућивање засновано на улози](../../../02-prompt-engineering)
  - [Предлошци упустава](../../../02-prompt-engineering)
- [Напредни обрасци](../../../02-prompt-engineering)
- [Коришћење постојећих Azure ресурса](../../../02-prompt-engineering)
- [Снимци екрана апликације](../../../02-prompt-engineering)
- [Истраживање образаца](../../../02-prompt-engineering)
  - [Низак насртај у односу на висок насртај](../../../02-prompt-engineering)
  - [Извршење задатка (Уводници алата)](../../../02-prompt-engineering)
  - [Саморазматрајући код](../../../02-prompt-engineering)
  - [Структурирана анализа](../../../02-prompt-engineering)
  - [Вишетурно ћаскање](../../../02-prompt-engineering)
  - [Разматрање корак по корак](../../../02-prompt-engineering)
  - [Ограничени излаз](../../../02-prompt-engineering)
- [Шта заправо учите](../../../02-prompt-engineering)
- [Следећи кораци](../../../02-prompt-engineering)

## Шта ћете научити

<img src="../../../translated_images/sr/what-youll-learn.c68269ac048503b2.webp" alt="Шта ћете научити" width="800"/>

У претходном модулу сте видели како меморија омогућава конверзациони AI и користили сте GitHub моделе за основне интеракције. Сада ћемо се фокусирати на то како постављате питања — сам процес упућивања — користећи Azure OpenAI GPT-5.2. Начин на који структурирате своје упуте драматично утиче на квалитет одговора које добијате. Започињемо прегледом основних техника упућивања, а затим пређемо на осам напредних образаца који у потпуности користе могућности GPT-5.2.

Користићемо GPT-5.2 јер уводи контролу размишљања — можете рећи моделу колико треба да размишља пре одговора. Ово чини различите стратегије упућивања јаснијим и помаже вам да разумете када употребити који приступ. Такође ћемо имати користи од мање ограничења броја упита код GPT-5.2 на Azure у односу на GitHub моделе.

## Претпоставке

- Завршен Модул 01 (размештени Azure OpenAI ресурси)
- Фајл `.env` у коренском директоријуму са Azure акредитивима (направљен помоћу `azd up` у Модулу 01)

> **Напомена:** Ако нисте завршили Модул 01, најпре пратите упутства за размештање тамо.

## Разумевање рада са упутствима

<img src="../../../translated_images/sr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Шта је рад са упутствима?" width="800"/>

Рад са упутствима је о дизајнирању улазног текста који конзистентно даје резултате који су вам потребни. Није само постављање питања — већ структуирање захтева тако да модел тачно разуме шта желите и како да то испоручи.

Замислите то као да дајете упутства колеги. „Поправи грешку“ је нејасно. „Поправи изузетак null pointer у UserService.java на линији 45 додавањем провере на null“ је специфично. Језички модели функционишу на исти начин — специфичност и структура су важни.

<img src="../../../translated_images/sr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Како ЛангЦхаин4џ одговара" width="800"/>

LangChain4j пружа инфраструктуру — везе са моделом, меморију и типове порука — док су обрасци упућивања само пажљиво структурисан текст који шаљете кроз ту инфраструктуру. Кључни елементи су `SystemMessage` (који поставља понашање и улогу AI) и `UserMessage` (који носи ваш стварни захтев).

## Основе рада са упутствима

<img src="../../../translated_images/sr/five-patterns-overview.160f35045ffd2a94.webp" alt="Преглед пет образаца рада са упутствима" width="800"/>

Пре уласка у напредне обрасце у овом модулу, хајде да прегледамо пет основних техника упућивања. Они су темељни конструктивни блокови које сваки инжењер упућивања треба да зна. Ако сте већ радили кроз [брзи почетак модул](../00-quick-start/README.md#2-prompt-patterns), ове сте видели у акцији — овде је концептуални оквир иза њих.

### Ништа-пример (Zero-Shot) упућивање

Најједноставнији приступ: дати моделу директно упутство без примера. Модел се ослања искључиво на своју обуку да разуме и изврши задатак. Ово добро функционише за једноставне захтеве где је очекивано понашање очигледно.

<img src="../../../translated_images/sr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Ништа-пример упућивање" width="800"/>

*Директно упутство без примера — модел закључује задатак само на основу упутства*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Одговор: "Позитивно"
```

**Када користити:** Једноставне класификације, директна питања, преводи или било који задатак који модел може обрадити без додатних упутстава.

### Неколико примера (Few-Shot) упућивање

Пружите примере који показују образац који желите да модел прати. Модел учи очекивани формат улаз-излаз из ваших примера и примењује га на нове улазе. Ово драматично побољшава конзистентност за задатке где жељени формат или понашање нису очигледни.

<img src="../../../translated_images/sr/few-shot-prompting.9d9eace1da88989a.webp" alt="Неколико примера упућивање" width="800"/>

*Учити из примера — модел идентификује образац и примењује га на нове улазе*

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

**Када користити:** Прилагођене класификације, доследан форматирање, доменски специфични задаци или када резултати ништа-пример упућивања нису конзистентни.

### Ланац размишљања

Замolite модел да прикаже свој процес размишљања корак по корак. Уместо да одмах да одговор, модел раздваја проблем и ради кроз сваки део јасно. Ово побољшава прецизност у математици, логици и више корака разматрања.

<img src="../../../translated_images/sr/chain-of-thought.5cff6630e2657e2a.webp" alt="Ланац размишљања упућивање" width="800"/>

*Разматрање корак по корак — распадање сложених проблема на јасне логичке кораке*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модел показује: 15 - 8 = 7, затим 7 + 12 = 19 јабука
```

**Када користити:** Математички проблеми, логичке загонетке, отклањање грешака или било који задатак где показивање процеса размишљања побољшава прецизност и поверење.

### Упућивање засновано на улози

Поставите личност или улогу AI пре постављања питања. Ово пружа контекст који обликује тон, дубину и фокус одговора. „Софтверски архитекта“ даје другачије савете од „млађег програмера“ или „ревизора безбедности“.

<img src="../../../translated_images/sr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Упућивање засновано на улози" width="800"/>

*Постављање контекста и личности — иста питања добијају различите одговоре у зависности од додељене улоге*

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

**Када користити:** Прегледи кода, туторинг, доменске анализе или када су вам потребни одговори прилагођени одређеном нивоу експертизе или перспективи.

### Предлошци упустава

Креирајте поново употребљиве упуте са променљивим параметрима. Уместо да сваки пут пишете ново упутство, дефинишите предложак једном и попуните различите вредности. LangChain4j класа `PromptTemplate` то олакшава са `{{variable}}` синтаксом.

<img src="../../../translated_images/sr/prompt-templates.14bfc37d45f1a933.webp" alt="Предлошци упустава" width="800"/>

*Поново употребљиви упути са променљивим параметрима — један предложак, много употреба*

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

**Када користити:** Понављани упити са различитим улазима, обрада у групи, израда поново употребљивих AI радних токова или било која ситуација где структура упуства остаје иста, али се подаци мењају.

---

Ових пет основа вам даје јаки алат за већину задатака упућивања. Остатак овог модула гради на њима са **осам напредних образаца** који користе контролу размишљања, самоевалуацију и могућности структурираног излаза GPT-5.2.

## Напредни обрасци

Након покривања основа, пређимо на осам напредних образаца који овај модул чине јединственим. Ни сви проблеми не захтевају исти приступ. Једна питања захтевају брзе одговоре, друга дубоко размишљање. Нека треба видљив процес размишљања, друго само резултат. Сваки од следећих образаца је оптимизован за другачију ситуацију — а контрола размишљања GPT-5.2 овај ефекат чини још израженијим.

<img src="../../../translated_images/sr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Осам образаца рада са упутствима" width="800"/>

*Преглед осам образаца рада са упутствима и њихових случајева употребе*

<img src="../../../translated_images/sr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контрола размишљања са GPT-5.2" width="800"/>

*Контрола размишљања GPT-5.2 вам дозвољава да одредите колико модел треба да размишља — од брзих директних одговора до дубоког истраживања*

<img src="../../../translated_images/sr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Поређење напора размишљања" width="800"/>

*Низак насртај (брзо, директно) у односу на висок насртај (детаљно, истраживачко) приступ размишљању*

**Низак насртај (брзо и фокусирано)** - За једноставна питања кад желите брзе, директне одговоре. Модел ради минимално размишљање - максимум 2 корака. Користите за калкулације, проналажења или једноставна питања.

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

> 💡 **Истражите са GitHub Copilot:** Отворите [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и питајте:
> - "Која је разлика између образаца упућивања са ниским и високим насртајем?"
> - "Како XML тагови у упутствима помажу у структуирању AI одговора?"
> - "Када треба да користим обрасце саморазматрања у односу на директна упутства?"

**Висок насртај (дубоко и детаљно)** - За комплексне проблеме где желите свеобухватну анализу. Модел темељно истражује и приказује детаљно разматрање. Користите за дизајн система, архитектонске одлуке или сложена истраживања.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Извршење задатка (прогрес корак по корак)** - За вишекорачне радне токове. Модел прво даје план, потом приказује сваки корак док ради, а на крају даје резиме. Користите за миграције, имплементације или било који процес са више корака.

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

Ланац-размишљања упућивање јасно тражи од модела да покаже свој процес разматрања, што побољшава прецизност код сложених задатака. Корак по корак разлагање помаже и људима и AI да разумеју логику.

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Питајте о овом обрасцу:
> - "Како бих прилагодио образац извршења задатка за дугорочне операције?"
> - "Које су најбоље праксе за структуирање уводника алата у продукционим апликацијама?"
> - "Како могу ухватити и приказати међукораке напретка у корисничком интерфејсу?"

<img src="../../../translated_images/sr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Образац извршења задатка" width="800"/>

*Планирање → извршење → резимирање радног тока за вишекорачне задатке*

**Саморазматрајући код** - За генерисање кода производног квалитета. Модел генерише код према производним стандардима са правилним руковањем грешкама. Користите када градите нове функционалности или сервисе.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Циклус саморазматрања" width="800"/>

*Итеративни циклус унапређења - генериши, евалуирај, идентификуј проблеме, побољшај, понови*

**Структурирана анализа** - За доследну евалуацију. Модел прегледа код користећи фиксни оквир (исправност, праксе, перформансе, безбедност, одрживост). Користите за прегледе кода или процене квалитета.

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

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Чатом:** Питајте о структурираној аналази:
> - "Како могу прилагодити оквир анализе за различите типове прегледа кода?"
> - "Који је најбољи начин за парсирање и програмирано реаговање на структурирани излаз?"
> - "Како осигурати доследне нивое озбиљности у различитим сесијама прегледа?"

<img src="../../../translated_images/sr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Образац структуриране анализе" width="800"/>

*Оквир за доследне прегледе кода са нивоима озбиљности*

**Вишетурно ћаскање** - За разговоре који захтевају контекст. Модел памти претходне поруке и гради на њима. Користите за интерактивне сесије помоћи или сложено питања и одговоре.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sr/context-memory.dff30ad9fa78832a.webp" alt="Меморија контекста" width="800"/>

*Како се контекст разговора акумулира кроз више турнуса док се не достигне лимит токена*

**Разматрање корак по корак** - За проблеме који захтевају видљиву логику. Модел приказује јасно разматрање за сваки корак. Користите за математичке проблеме, логичке загонетке или када је важно разумети процес размишљања.

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

*Разлагање проблема на јасне логичке кораке*

**Ограничени излаз** - За одговоре са специфичним захтевима формата. Модел строго прати правила формата и дужине. Користите за резиме или када вам је потребна прецизна структура излаза.

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

<img src="../../../translated_images/sr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Образац ограниченог извоза" width="800"/>

*Примена специфичних захтева формата, дужине и структуре*

## Коришћење постојећих Azure ресурса

**Проверите размештање:**

Обавезно да фајл `.env` постоји у коренском директоријуму са Azure акредитивима (направљен током Модула 01):
```bash
cat ../.env  # Треба да прикаже AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Покрените апликацију:**

> **Напомена:** Ако сте већ покренули све апликације помоћу `./start-all.sh` из Модула 01, овај модул већ ради на порту 8083. Можете прескочити команде покретања испод и одмах отићи на http://localhost:8083.

**Опција 1: Коришћење Spring Boot контролне табле (препоручено за кориснике VS Code)**

Дев контејнер укључује екстензију Spring Boot Dashboard, која пружа визуелни интерфејс за управљање свим Spring Boot апликацијама. Можете је пронаћи у Activity Bar-у са леве стране VS Code-а (потражите Spring Boot иконицу).
Из Spring Boot контролне табле, можете:
- Видети све доступне Spring Boot апликације у радном простору
- Покренути/запрети апликације једним кликом
- Пратити логове апликације у реалном времену
- Пратити статус апликације

Једноставно кликните на дугме за репродукцију поред "prompt-engineering" да бисте покренули овај модул, или покрените све модуле одједном.

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

Оба скрипта аутоматски учитавају променљиве окружења из коренског `.env` фајла и изградиће JAR фајлове ако не постоје.

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

**За заустављање:**

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

## Снимци екрана апликација

<img src="../../../translated_images/sr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Главна контролна табла која приказује свих 8 образаца prompt engineering-а са њиховим карактеристикама и применама*

## Истраживање образаца

Веб интерфејс вам омогућава да експериментишете са различитим стратегијама упућивања. Сваки образац решава различите проблеме - испробајте их да видите када сваки приступ највише долази до изражаја.

### Низак у односу на висок ниво ангажованости

Поставите једноставно питање као што је "Колико је 15% од 200?" користећи низак ниво ангажованости. Добићете тренутни, директан одговор. Сада поставите нешто сложеније као "Осмислите стратегију кеширања за API са високим саобраћајем" користећи висок ниво ангажованости. Пратите како модел успорава и пружа детаљно образложење. Исти модел, иста структура питања - али упит му говори колико треба да размишља.

<img src="../../../translated_images/sr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Брза калкулација са минималним образложењем*

<img src="../../../translated_images/sr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Опсежна стратегија кеширања (2.8MB)*

### Извођење задатака (Уводне речи за алате)

Вишестепени радни токови имају користи од претходног планирања и приповедања напретка. Модел описује шта ће урадити, приповеда сваки корак, а затим сумира резултате.

<img src="../../../translated_images/sr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Креирање REST крајње тачке са корак-по-корак приповедањем (3.9MB)*

### Код који се самопроцењује

Пробајте "Креирај сервис за валидацију е-поште". Уместо да само генерише код и заустави се, модел генерише, процењује према критеријумима квалитета, идентификује слабости и унапређује. Видећете како итеративно ради док код не достигне стандарде за производњу.

<img src="../../../translated_images/sr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Комплетан сервис за валидацију е-поште (5.2MB)*

### Структурирана анализа

Прегледи кода захтевају доследне оквире за процену. Модел анализира код користећи фиксне категорије (исправност, праксе, перформансе, безбедност) са нивоима озбиљности.

<img src="../../../translated_images/sr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Преглед кода заснован на оквиру*

### Вишекратни разговор

Питајте "Шта је Spring Boot?" а затим одмах наставите са "Покажи ми пример". Модел памти ваше прво питање и даје вам конкретан пример Spring Boot-а. Без меморије, друго питање било би превише општe.

<img src="../../../translated_images/sr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Очување контекста кроз питања*

### Размишљање корак по корак

Изаберите математички проблем и пробајте га и са Размишљањем корак по корак и са Низком ангажованошћу. Низак ниво ангажованости пружа само одговор - брзо али нејасно. Размишљање корак по корак приказује сваки израчун и одлуку.

<img src="../../../translated_images/sr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Математички проблем са изричитим корацима*

### Ограничен излаз

Када вам требају специфични формати или број речи, овај образац намета строго придржавање. Испробајте да генеришете резиме са тачно 100 речи у формату на трафле.

<img src="../../../translated_images/sr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Резиме машинског учења са контролом формата*

## Шта заиста учите

**Ниво напора урачунавања мења све**

GPT-5.2 вам омогућава контролу рачунарског напора преко ваших упита. Мали напор значи брзе одговоре са минималним истраживањем. Већи напор значи да модел узима време за дубоко размишљање. Учите да прилагодите напор сложености задатка - немојте губити време на једноставна питања, али ни журити при сложеним одлукама.

**Структура усмерава понашање**

Приметили сте XML ознаке у упитима? Оне нису декоративне. Модели поузданије прате структурисана упутства него слободан текст. Када вам требају вишестепени процеси или сложена логика, структура помаже моделу да прати где се налази и шта следи.

<img src="../../../translated_images/sr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомија добро структурисаног упита са јасним одељцима и XML стил организацијом*

**Квалитет кроз самоевалуацију**

Обрасци са саморефлексијом раде тако што чине критеријуме квалитета експлицитним. Уместо да се надамо да модел "то ради исправно", кажете му тачно шта значи "исправно": исправна логика, руковање грешкама, перформансе, безбедност. Модел онда може проценити свој излаз и унапредити га. Ово претвара генерисање кода из лотарије у процес.

**Контекст је ограничен**

Вишекратни разговори функционишу тако што сваки захтев укључује историју порука. Али постоји лимит - сваки модел има максималан број токена. Како разговори расту, требаће вам стратегије да сачувате релевантан контекст без преласка лимита. Овај модул вам показује како меморија ради; касније ћете научити када да правите резиме, када да заборавите и када да враћате информације.

## Следећи кораци

**Следећи модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигација:** [← Претходни: Модул 01 - Увод](../01-introduction/README.md) | [Назад на почетак](../README.md) | [Следећи: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:
Овај документ је преведен коришћењем AI услуге за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да буде прецизно, молимо вас да имате у виду да аутоматизовани преводи могу садржати грешке или нетачности. Изворни документ на његовом оригиналном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални људски превод. Не сносимо одговорност за било какве неспоразуме или погрешне тумачења проистекле из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->