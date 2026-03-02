# Модул 02: Инжењеринг упита са GPT-5.2

## Садржај

- [Видео водич](../../../02-prompt-engineering)
- [Шта ћете научити](../../../02-prompt-engineering)
- [Претпоставке](../../../02-prompt-engineering)
- [Разумевање инжењеринга упита](../../../02-prompt-engineering)
- [Основе инжењеринга упита](../../../02-prompt-engineering)
  - [Zero-Shot упити](../../../02-prompt-engineering)
  - [Few-Shot упити](../../../02-prompt-engineering)
  - [Џеп размишљања](../../../02-prompt-engineering)
  - [Роло-базирани упити](../../../02-prompt-engineering)
  - [Шаблони упита](../../../02-prompt-engineering)
- [Напредни обрасци](../../../02-prompt-engineering)
- [Коришћење постојећих Azure ресурса](../../../02-prompt-engineering)
- [Снимци екрана апликације](../../../02-prompt-engineering)
- [Истраживање образаца](../../../02-prompt-engineering)
  - [Низак против високог жаруљивости](../../../02-prompt-engineering)
  - [Извршење задатка (почетни текстови алата)](../../../02-prompt-engineering)
  - [Саморефлексивни код](../../../02-prompt-engineering)
  - [Структурирана анализа](../../../02-prompt-engineering)
  - [Вишекратни чет](../../../02-prompt-engineering)
  - [Размишљање корак по корак](../../../02-prompt-engineering)
  - [Ограничени излаз](../../../02-prompt-engineering)
- [Шта заправо учите](../../../02-prompt-engineering)
- [Следећи кораци](../../../02-prompt-engineering)

## Видео водич

Погледајте ову уживо сесију која објашњава како започети са овим модулом:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Шта ћете научити

<img src="../../../translated_images/sr/what-youll-learn.c68269ac048503b2.webp" alt="Шта ћете научити" width="800"/>

У претходном модулу видели сте како меморија омогућава конверзацијски AI и користили GitHub моделe за основне интеракције. Сада ћемо се фокусирати на то како постављате питања — саме упите — користећи Azure OpenAI GPT-5.2. Начин на који структурирате упите драматично утиче на квалитет добијених одговора. Почињемо прегледом основних техника упита, а затим прелазимо на осам напредних образаца који у потпуности искориштавају могућности GPT-5.2.

Користићемо GPT-5.2 јер он уводи контролу размишљања - можете модели рећи колико треба размишљати пре одговора. Ово чини различите стратегије упита јаснијим и помаже вам да разумете када да користите који приступ. Такође ћемо имати користи од мањих ограничења Azure-а на GPT-5.2 у поређењу са GitHub моделима.

## Претпоставке

- Завршен модул 01 (деплојовани Azure OpenAI ресурси)
- `.env` фајл у коренском директоријуму са Azure подацима за пријаву (креиран командом `azd up` у Модулу 01)

> **Напомена:** Ако нисте завршили Модул 01, прво пратите упутства за деплоирање у том модулу.

## Разумевање инжењеринга упита

<img src="../../../translated_images/sr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Шта је инжењеринг упита?" width="800"/>

Инжењеринг упита је дизајнирање улазног текста који доследно даје резултате које желите. Није само питање питања - ради се о структуирању захтева тако да модел разуме тачно шта желите и како да то испоручи.

Замислите као да дајете упутства колеги. "Поправи грешку" је нејасно. "Поправи null pointer exception у UserService.java на линији 45 додавањем провере null" је прецизно. Језички модели функционишу на исти начин – специфичност и структура су важни.

<img src="../../../translated_images/sr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Како LangChain4j одговара" width="800"/>

LangChain4j пружа инфраструктуру — везе са моделима, меморију и типове порука — док су обрасци упита само пажљиво структуиран текст који шаљете кроз ту инфраструктуру. Кључне грађевинске јединице су `SystemMessage` (који подешава понашање и улогу AI-а) и `UserMessage` (који носи ваш стварни захтев).

## Основе инжењеринга упита

<img src="../../../translated_images/sr/five-patterns-overview.160f35045ffd2a94.webp" alt="Преглед пет образаца инжењеринга упита" width="800"/>

Пре него што заронимо у напредне образце у овом модулу, погледајмо пет основних техника упита. Ово су грађевински блокови које сваки инжењер упита треба да зна. Ако сте већ радили [Quick Start модул](../00-quick-start/README.md#2-prompt-patterns), већ сте их видели у пракси — ево концептуалног оквира иза њих.

### Zero-Shot упити

Најједноставнији приступ: дати моделу директно упутство без примера. Модел се ослања у потпуности на своју обуку да разуме и изврши задатак. Ово добро функционише за једноставне захтеве где је очекивано понашање очигледно.

<img src="../../../translated_images/sr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot упити" width="800"/>

*Директно упутство без примера — модел изводи задатак само из упутства*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Одговор: "Позитивно"
```

**Кад користити:** Једноставне класификације, директна питања, преводи или било који задатак који модел може обрадити без додатних смерница.

### Few-Shot упити

Пружите примере који показују образац који желите да модел следи. Модел учи очекивани формат улаз-излаз из ваших примера и примењује га на нове улазе. Ово драматично побољшава конзистентност код задатака где формат или понашање нису очигледни.

<img src="../../../translated_images/sr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot упити" width="800"/>

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

**Кад користити:** Прилагођене класификације, конзистентно форматирање, задаци специфични за домен, или када zero-shot резултати нису конзистентни.

### Џеп размишљања

Замолите модел да прикаже свој поступак размишљања корак по корак. Уместо да одмах да одговор, модел разлаже проблем и ради кроз сваки део експлицитно. Ово побољшава тачност код математике, логике и вишестепених задатака.

<img src="../../../translated_images/sr/chain-of-thought.5cff6630e2657e2a.webp" alt="Џеп размишљања" width="800"/>

*Размишљање корак по корак — разлагање сложених проблема у јасне логичке кораке*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Модел приказује: 15 - 8 = 7, затим 7 + 12 = 19 јабука
```

**Кад користити:** Математички проблеми, логичке загонетке, отклањање грешака, или било који задатак где приказ размишљања побољшава тачност и поверење.

### Роло-базирани упити

Подесите личност или улогу AI-а пре него што поставите питање. Ово пружа контекст који обликује тон, дубину и фокус одговора. "Софтверски архитект" даје другачије савете од "млађег програмера" или "сигурносног ревизора".

<img src="../../../translated_images/sr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Роло-базирани упити" width="800"/>

*Подешавање контекста и личности — исто питање добија различит одговор у зависности од додељене улоге*

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

**Кад користити:** Прегледи кода, туторство, анализа специфична за домен, или када су вам потребни одговори прилагођени одређеном нивоу експертизе или перспективи.

### Шаблони упита

Креирајте поновно употребљиве упите са променљивим местима за убацивање. Уместо да сваки пут пишете нови упит, дефинишите шаблон једном и попуњавајте другачије вредности. LangChain4j класа `PromptTemplate` то олакшава са синтаксом `{{variable}}`.

<img src="../../../translated_images/sr/prompt-templates.14bfc37d45f1a933.webp" alt="Шаблони упита" width="800"/>

*Поновно употребљиви упити са променљивим местима — један шаблон, много употреба*

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

**Кад користити:** Понављани упити са различитим улазима, пакетна обрада, израда поновно употребљивих AI токова рада, или било који сценарио где структура упита остаје иста, али се подаци мењају.

---

Ових пет основа пружа вам солидан алат за већину задатака са упитима. Остатак овог модула надограђује их са **осам напредних образаца** који искоришћавају GPT-5.2 контролу размишљања, самоевалуацију и могућности структурираног излаза.

## Напредни обрасци

Након што смо покрили основе, прелазимо на осам напредних образаца који овај модул чине јединственим. Нису сви проблеми исти и не захтевају исти приступ. Нека питања захтевају брзе одговоре, друга дубоко размишљање. Нека требају видљиво размишљање, а нека само резултати. Сваки образац испод је оптимизован за различит сценарио — а GPT-5.2 контрола размишљања чини разлике још израженијим.

<img src="../../../translated_images/sr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Осам образаца упита" width="800"/>

*Преглед осам образаца инжењеринга упита и њихове употребе*

<img src="../../../translated_images/sr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Контрола размишљања са GPT-5.2" width="800"/>

*GPT-5.2 контрола размишљања вам омогућава да одредите колико модел треба да размишља — од брзих директних одговора до дубоке анализе*

**Низак жар (Брзо и фокусирано)** - За једноставна питања где желите брзе, директне одговоре. Модел врши минимално размишљање - максимум 2 корака. Користите ово за прорачуне, претраге или једноставна питања.

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

> 💡 **Истражите уз GitHub Copilot:** Отворите [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и питате:
> - "Која је разлика између образаца упита са ниским и високим жаром?"
> - "Како XML тагови у упитима помажу у структуирању одговора AI-а?"
> - "Када треба користити образце са саморазматрaњем у односу на директна упутства?"

**Висок жар (Дубоко и темељно)** - За сложене проблеме где желите свеобухватну анализу. Модел темељно истражује и приказује детаљно размишљање. Користите ово за дизајн система, архитетонске одлуке или сложена истраживања.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Извршење задатка (Прогрес корак по корак)** - За вишестепене токове посла. Модел пружа предлог плана, приповеда сваки корак током рада, а затим даје резиме. Користите за миграције, имплементације или било који вишестепени процес.

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

Chain-of-Thought упит јасно тражи од модела да прикаже свој процес размишљања, побољшавајући тачност код сложених задатака. Рачлањавање корак по корак помаже људима и AI-у да разумеју логику.

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Питајте о овом образцу:
> - "Како бих прилагодио образац извршења задатка за дуготрајне операције?"
> - "Које су најбоље праксе за структурирање почетних текстова алата у продукцији?"
> - "Како могу да снимим и прикажем унапред напредак у UI-ју?"

<img src="../../../translated_images/sr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Образац извршења задатка" width="800"/>

*Планирање → Извођење → Резимирање тока рада за вишестепене задатке*

**Саморефлектујући код** - За генерисање кода продукционог квалитета. Модел генерише код пратећи продукционе стандарде са одговарајућом обрадом грешака. Користите када градите нове функције или сервисе.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Циклус саморефлексије" width="800"/>

*Итеративна петља побољшања - генериши, оцењуј, идентификуј проблеме, побољшај, понови*

**Структурирана анализа** - За конзистентну евалуацију. Модел прегледа код користећи фиксни оквир (тачност, праксе, перформансе, безбедност, одрживост). Користите за прегледе кода или процене квалитета.

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

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Питајте о структурираној анализи:
> - "Како могу прилагодити оквир анализе за различите типове прегледа кода?"
> - "Који је најбољи начин за парсирање и програмацки рад са структурираним излазом?"
> - "Како осигурати доследне нивое озбиљности у различитим сесијама прегледа?"

<img src="../../../translated_images/sr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Образац структуриране анализе" width="800"/>

*Оквир за конзистентне прегледе кода са нивоима озбиљности*

**Вишекратни чет** - За разговоре који захтевају контекст. Модел памти претходне поруке и гради на њима. Користите за интерактивне помоћне сесије или сложена питања и одговоре.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sr/context-memory.dff30ad9fa78832a.webp" alt="Памет контекста" width="800"/>

*Како контекст разговора акумулира кроз више корака све док се не достигне лимит токена*

**Размишљање корак по корак** - За проблеме којима је потребна видљива логика. Модел приказује експлицитно размишљање за сваки корак. Користите за математичке проблеме, логичке загонетке или када желите разумети процес размишљања.

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

**Ограничени излаз** - За одговоре са специфичним захтевима формата. Модел стриктно прати правила формата и дужине. Користите за резиме или када вам треба прецизна структура излаза.

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

*Остварење специфичних захтева за формат, дужину и структуру*

## Коришћење постојећих Azure ресурса

**Проверите деплоирање:**

Осигурајте да `.env` фајл постоји у коренском директоријуму са Azure подацима за пријаву (креиран током Модула 01):
```bash
cat ../.env  # Требало би да приказује AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Покретање апликације:**

> **Напомена:** Ако сте већ покренули све апликације користећи `./start-all.sh` из Модула 01, овај модул већ ради на порту 8083. Можете прескочити наредбе за покретање испод и директно отићи на http://localhost:8083.
**Опција 1: Коришћење Spring Boot Dashboard-а (Препоручено за кориснике VS Code-а)**

_dev container_ укључује Spring Boot Dashboard екстензију, која пружа визуелни интерфејс за управљање свим Spring Boot апликацијама. Можете је пронаћи у Activity Bar-у на левој страни VS Code-а (потражите Spring Boot иконицу).

Из Spring Boot Dashboard-а можете:
- Видети све расположиве Spring Boot апликације у радном простору
- Покретати/пуштати апликације једним кликом
- Пратити логове апликација у реалном времену
- Надгледати статус апликације

Само кликните на дугме за пуштање поред "prompt-engineering" да покренете овај модул, или покрените све модуле одједном.

<img src="../../../translated_images/sr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Опција 2: Коришћење shell скрипти**

Покрените све web апликације (модуле 01-04):

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

Обе скрипте аутоматски учитавају променљиве окружења из коренског `.env` фајла и изградиће JAR фајлове уколико не постоје.

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

*Главни dashboard који приказује свих 8 шаблона у prompt engineering-у са њиховим карактеристикама и примерима употребе*

## Истраживање шаблона

Веб интерфејс вам омогућава да експериментишете са различитим стратегијама састављања упита. Сваки шаблон решава различите проблеме - пробајте их да видите када је који приступ најбољи.

> **Напомена: Стриминг vs Несстриминг** — Свака страница шаблона нуди два дугмета: **🔴 Stream Response (Live)** и опцију **Non-streaming**. Стриминг користи Server-Sent Events (SSE) да приказује токене у реалном времену док модел генерише, тако да видите напредак одмах. Несстриминг опција чека цео одговор пре приказа. За промпт који покрећу дубљи разлози (нпр. High Eagerness, Self-Reflecting Code), несстриминг позив може потрајати веома дуго — понекад минутама — без видљивог повратног одговора. **Користите стриминг када експериментишете са сложеним промптима** да бисте видели како модел ради и избегли утисак да је захтев истекао.
>
> **Напомена: Захтеви за прегледач** — Стриминг функција користи Fetch Streams API (`response.body.getReader()`) који захтева пун прегледач (Chrome, Edge, Firefox, Safari). Не ради у уграђеном Simple Browser-у VS Code-а, јер његов webview не подржава ReadableStream API. Ако користите Simple Browser, несстриминг дугмад ће и даље радити нормално — само су дугмад за стриминг погођена. Отворите `http://localhost:8083` у спољном прегледачу за пун доживљај.

### Ниска vs Висока жеља (Eagerness)

Поставите једноставно питање као „Колико је 15% од 200?“ користећи ниску жељу (Low Eagerness). Добит ћете тренутан, директан одговор. Сада поставите нешто компликовано као „Дизајнирај стратегију кеширања за API са великим саобраћајем“ користећи високу жељу (High Eagerness). Кликните **🔴 Stream Response (Live)** и пратите детаљно размишљање модела који се појављује токен по токен. Исти модел, иста структура питања - али промпт му говори колико дубоко да размишља.

### Извршење задатка (Уводни делови алата)

Вишестепени радни процеси имају користи од предходног планирања и преношења напретка. Модел наводи шта ће урадити, описује сваки корак, па потом сумира резултате.

### Само-рефлектујући код

Пробајте "Направи услугу за валидацију е-поште". Уместо да само генерише код и заустави се, модел генерише, процењује квалитет, идентификује слабости и унапређује. Видећете како понавља док код не достигне стандарде за продукцију.

### Структурирана анализа

Прегледи кода захтевају доследне оквире за процену. Модел анализира код користећи фиксне категорије (тачност, праксе, перформансе, безбедност) са нивоима озбиљности.

### Вишедневни разговор (Multi-Turn Chat)

Поставите „Шта је Spring Boot?“ па одмах наставите са „Покажи пример“. Модел памти ваше прво питање и даје вам пример посебно за Spring Boot. Без меморије, то друго питање би било превише нејасно.

### Рaзмишљање корак по корак

Изаберите математички проблем и пробајте са и са Step-by-Step Reasoning и са Low Eagerness. Ниска жеља само даје одговор - брзо али непрозирно. Размишљање корак по корак вам показује сваки израчун и одлуку.

### Ограничен излаз

Када вам требају прецизни формати или број речи, овај шаблон строго одржава правила. Пробајте да генеришете резиме са тачно 100 речи у облику тачака.

## Шта заправо учите

**Напор размишљања мења све**

GPT-5.2 вам дозвољава да контролишете рачунски напор кроз ваше промпте. Ниски напор значи брзе одговоре са минималном истраживањем. Високи напор значи да модел узима време да дубоко размишља. Учите да усагласите напор са сложеношћу задатка - немојте трошити време на једноставна питања, али немојте ни журити код сложених одлука.

**Структура усмерава понашање**

Приметили сте XML тагове у промптима? Нису декоративни. Модели делују прецизније кад следе структуиране инструкције него слободни текст. Када вам требају вишестепени процеси или сложена логика, структура помаже моделу да прати где је и шта следи.

<img src="../../../translated_images/sr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Анатомија добро структуираног промпта са јасним секцијама и XML-стилом организације*

**Квалитет кроз самоевалуацију**

Само-рефлектујући шаблони раде тако што квалитетне критеријуме чине јасним. Уместо да се надају да модел „уради како треба“, ви му тачно говорите шта значи „како треба“: тачна логика, руковање грешкама, перформансе, безбедност. Модел тада може проценити свој излаз и побољшати га. Ово претвара генерацију кода из лутрије у систематски процес.

**Контекст је ограничен**

Вишедневни разговори функционишу тако што укључују историју порука у сваком захтеву. Али постоји ограничење - сваки модел има максималан број токена. Како разговори расту, биће вам потребне стратегије за чување релевантног контекста без достигања тог лимита. Овај модул вам показује како меморија ради; касније ћете научити када да сумирате, када да заборавите и када да дохватите.

## Следећи кораци

**Следећи модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигација:** [← Претходни: Модул 01 - Увод](../01-introduction/README.md) | [Назад на Почетак](../README.md) | [Следећи: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:  
Овај документ је преведен помоћу АИ преводилачке услуге [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде тачан, имајте у виду да аутоматизовани преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешне интерпретације настале коришћењем овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->