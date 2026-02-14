# Модул 02: Рад са упитима помоћу GPT-5.2

## Садржај

- [Шта ћете научити](../../../02-prompt-engineering)
- [Претпоставке](../../../02-prompt-engineering)
- [Разумевање рада са упитима](../../../02-prompt-engineering)
- [Како се овде користи LangChain4j](../../../02-prompt-engineering)
- [Основни обрасци](../../../02-prompt-engineering)
- [Коришћење постојећих Azure ресурса](../../../02-prompt-engineering)
- [Снимци екрана апликације](../../../02-prompt-engineering)
- [Истраживање образаца](../../../02-prompt-engineering)
  - [Ниска и висока жеља](../../../02-prompt-engineering)
  - [Извршење задатка (предговори алата)](../../../02-prompt-engineering)
  - [Саморефлектирајући код](../../../02-prompt-engineering)
  - [Структурирана анализа](../../../02-prompt-engineering)
  - [Вишекратни разговор](../../../02-prompt-engineering)
  - [Размишљање корак по корак](../../../02-prompt-engineering)
  - [Ограничен излаз](../../../02-prompt-engineering)
- [Шта заправо учите](../../../02-prompt-engineering)
- [Следећи кораци](../../../02-prompt-engineering)

## Шта ћете научити

У претходном модулу видели сте како меморија омогућава конверзацијалну АИ и како се користе GitHub модели за основне интеракције. Сада ћемо се фокусирати на то како постављате питања – самим упитима – користећи Azure OpenAI GPT-5.2. Начин на који структурирате своје упите драстично утиче на квалитет одговора које добијате.

Користићемо GPT-5.2 зато што уводи контролу размишљања – можете рећи моделу колико треба да размишља пре одговора. Ово разјашњава различите стратегије постављања упита и помаже вам да разумете када користити који приступ. Такође ћемо имати користи од мањих ограничења брзине Azure-а за GPT-5.2 у поређењу са GitHub моделима.

## Претпоставке

- Завршен Модул 01 (Azure OpenAI ресурси су распоређени)
- `.env` фајл у коренском директоријуму са Azure акредитивима (направљен командом `azd up` у Модулу 01)

> **Белешка:** Ако нисте завршили Модул 01, прво пратите упутства за распоређивање тамо.

## Разумевање рада са упитима

Рад са упитима је дизајнирање улазног текста који вам доследно омогућава жељене резултате. Није само постављање питања – већ структурирање захтева тако да модел тачно разуме шта желите и како то да пружи.

Замислите да давате упутства колеги. "Поправи грешку" је нејасно. "Поправи null pointer exception у датотеци UserService.java на линији 45 додавањем провере за null" је прецизно. Језички модели функционишу исто - прецизност и структура су важни.

## Како се овде користи LangChain4j

Овај модул демонстрира напредне образце рада са упитима користећи исти LangChain4j темељ из претходних модула, са фокусом на структуру упита и контролу размишљања.

<img src="../../../translated_images/sr/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Како LangChain4j повезује ваше упите са Azure OpenAI GPT-5.2*

**Зависности** – Модул 02 користи следеће langchain4j зависности дефинисане у `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel конфигурација** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Чат модел је ручно конфигурисан као Spring bean користећи OpenAI Official клијента, који подржава Azure OpenAI крајње тачке. Главна разлика у односу на Модул 01 је у начину на који структурирамо упите послате `chatModel.chat()`, а не у самој конфигурацији модела.

**System и User поруке** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j раздваја типове порука ради јасноће. `SystemMessage` поставља понашање и контекст АИ (као "Ти си код рецензент"), док `UserMessage` садржи стварни захтев. Ово раздвајање омогућава одржавање доследног понашања АИ-а код различитих корисничких упита.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/sr/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage обезбеђује упорни контекст док UserMessages садрже појединачне захтеве*

**MessageWindowChatMemory за вишекратни разговор** – За образац вишекратног разговора користимо `MessageWindowChatMemory` из Модула 01. Свака сесија има свој инстанцу меморије смештену у `Map<String, ChatMemory>`, што омогућава вишеструке паралелне разговоре без мешања контекста.

**Шаблони упита** – Реални фокус овде је рад са упитима, а не нове LangChain4j API-је. Сваки образац (ниска жеља, висока жеља, извршење задатка итд.) користи исти метод `chatModel.chat(prompt)`, али са пажљиво структуираним низовима упита. XML тагови, упутства и форматирање су део текста упита, а не функције LangChain4j.

**Контрола размишљања** – Усмерење напора размишљања GPT-5.2 контролише се упутствима у упиту као што су "максимум 2 корака размишљања" или "истражи темељно". Ово су технике инжењеринга упита, а не конфигурације LangChain4j. Либрарија једноставно доставља ваше упите моделу.

Главна поука: LangChain4j пружа инфраструктуру (повезивање модела преко [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), меморију, обраду порука преко [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), док овај модул учи вас како да креирате ефективне упите у тој инфраструктури.

## Основни обрасци

Није сваки проблем исти. Нека питања захтевају брзе одговоре, нека дубоко размишљање. Неки захтевају видљиво размишљање, други само резултате. Овај модул покрива осам образаца рада са упитима – сваки оптимизован за различите сценарије. Испробаћете све да научите када који приступ најбоље функционише.

<img src="../../../translated_images/sr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Преглед осам образаца инжењеринга упита и њихове употребе*

<img src="../../../translated_images/sr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Ниска жеља (брзо, директно) и висока жеља (темљно, истраживачки) приступи размишљању*

**Ниска жеља (брзо и фокусирано)** – За једноставна питања где желите брзе, директне одговоре. Модел ради минимално размишљање – максимум 2 корака. Користите за калкулације, претраге или једноставна питања.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Истражите са GitHub Copilot:** Отворите [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) и питајте:
> - "Која је разлика између образаца са ниском и висококом жељом за рад са упитима?"
> - "Како XML тагови у упитима помажу у структуирању АИ одговора?"
> - "Када да користим обрасце саморефлексије, а када директна упутства?"

**Висока жеља (дубоко и темељно)** – За сложене проблеме где желите свеобухватну анализу. Модел темељно истражује и показује детаљно размишљање. Користите за дизајн система, архитектонске одлуке или сложена истраживања.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Извршење задатка (напредак корак по корак)** – За вишефазне токове рада. Модел даје план унапред, приповеда сваки корак док ради, па потом даје резиме. Користите за миграције, имплементације или било који поступак са више корака.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought упити експлицитно траже од модела да прикаже свој процес размишљања, чиме се побољшава тачност за сложене задатке. Разлагање корак по корак помаже људима и АИ-ју да разумеју логику.

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Четом:** Питајте о овом обрасцу:
> - "Како бих прилагодио образац извршења задатка за операције које трају дуго?"
> - "Које су најбоље праксе за структуирање предговора алата у продукционим апликацијама?"
> - "Како могу да снимим и прикажем посредне извештаје о напретку у корисничком интерфејсу?"

<img src="../../../translated_images/sr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Планирај → Изврши → Сажми радни ток за вишефазне задатке*

**Саморефлектирајући код** – За генерисање кода производног квалитета. Модел генерише код, проверава га према критеријумима квалитета и побољшава га итеративно. Користите када правите нове функције или сервисе.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Итеративна петља побољшања – генериши, процени, откриј проблеме, побољшај, понављај*

**Структурирана анализа** – За доследну евалуацију. Модел прегледа код користећи фиксне оквире (тачност, праксе, перформансе, безбедност). Користите за прегледе кода или процене квалитета.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) Четом:** Питајте о структурираној анализа:
> - "Како могу да прилагодим оквир анализе за различите типове прегледа кода?"
> - "Који је најбољи начин да програмски парсирам и делујем по структурисаном излазу?"
> - "Како обезбедити доследне нивое озбиљности у различитим прегледима?"

<img src="../../../translated_images/sr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Оквир са четири категорије за доследне прегледе кода са нивоима озбиљности*

**Вишекратни разговор** – За разговоре којима треба контекст. Модел памти претходне поруке и надограђује их. Користите за интерактивну помоћ или сложена питања и одговоре.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sr/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Како се контекст разговора акумулира кроз више корака све док не достигне лимит токена*

**Размишљање корак по корак** – За задатке који захтевају видљиву логику. Модел приказује експлицитно размишљање за сваки корак. Користите за математичке проблеме, логичке загонетке или када желите да разумете процес мишљења.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Разлагање проблема у експлицитне логичке кораке*

**Ограничен излаз** – За одговоре са специфичним захтевима формата. Модел строго прати правила формата и дужине. Користите за сажетке или када вам је потребна прецизна структура излаза.

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

<img src="../../../translated_images/sr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Наметaње специфичних захтева за формат, дужину и структуру* 

## Коришћење постојећих Azure ресурса

**Проверите распоређивање:**

Уверите се да `.env` фајл постоји у коренском директоријуму са Azure акредитивима (направљен током Модула 01):
```bash
cat ../.env  # Требало би да приказује AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Покрените апликацију:**

> **Белешка:** Ако сте већ покренули све апликације користећи `./start-all.sh` из Модула 01, овај модул већ ради на порту 8083. Можете прескочити команде за покретање и директно отићи на http://localhost:8083.

**Опција 1: Коришћење Spring Boot Dashboard-а (препоручено за кориснике VS Code-а)**

Dev контејнер укључује проширење Spring Boot Dashboard које пружа визуелни интерфејс за управљање свим Spring Boot апликацијама. Можете га наћи у траци активности са леве стране VS Code-а (тражите иконицу Spring Boot-а).

Из Spring Boot Dashboard-а можете:
- видети све доступне Spring Boot апликације у радном простору
- покретати/стајати апликације једним кликом
- гледати логове апликација у реалном времену
- пратити статус апликације

Једноставно кликните на дугме за покретање поред "prompt-engineering" да бисте покренули овај модул, или покрените све модуле одједном.

<img src="../../../translated_images/sr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Опција 2: Коришћење шеловских скрипти**

Покрените све веб апликације (модули 01-04):

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

Обе скрипте аутоматски учитавају променљиве околине из `.env` фајла у корену и градиће JAR фајлове ако не постоје.

> **Белешка:** Ако желите да ручно саставите све модуле пре покретања:
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

**Да бисте зауставили:**

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

*Главни контролни панел који приказује свих 8 образаца рада са упитима са њиховим карактеристикама и наменама*

## Истраживање образаца

Веб интерфејс вам омогућава да експериментишете са различитим стратегијама постављања упита. Сваки образац решава разне проблеме – испробајте их да видите када који приступ најбоље функцинише.

### Ниска и висока жеља

Поставите једноставно питање као што је „Колико је 15% од 200?“ користећи Ниску жељу. Добићете тренутан, директан одговор. Сада питајте нешто сложено као „Дизајнирај стратегију кеширања за API са великим оптерећењем“ користећи Високу жељу. Посматрајте како модел успорава и пружа детаљно размишљање. Исти модел, иста структура питања – али упит му каже колико да размишља.
<img src="../../../translated_images/sr/low-eagerness-demo.898894591fb23aa0.webp" alt="Демо са ниским ентузијазмом" width="800"/>

*Брза калкулација са минималним размишљањем*

<img src="../../../translated_images/sr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Демо са високим ентузијазмом" width="800"/>

*Комплетна стратегија кеширања (2.8MB)*

### Извођење задатака (уводи алата)

Вишестепени токови рада имају користи од претходног планирања и приповедања о напретку. Модел наводи шта ће урадити, приповеда сваки корак, а затим сумира резултате.

<img src="../../../translated_images/sr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Демо извођења задатка" width="800"/>

*Креирање REST ендпоинта са приповедањем корак по корак (3.9MB)*

### Код који самостално рефлектује

Покушајте "Креирај услугу за валидацију е-поште". Уместо да само генерише код и стане, модел генерише, оцењује по критеријумима квалитета, идентификује слабости и унапређује. Видећете како итера до кода који испуњава производне стандарде.

<img src="../../../translated_images/sr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Демо саморефлектујућег кода" width="800"/>

*Потпуна услуга за валидацију е-поште (5.2MB)*

### Структурисана анализа

Ревизије кода захтевају доследне оквире за оцену. Модел анализира код користећи фиксне категорије (тачност, пракса, перформансе, безбедност) са нивоима тежине.

<img src="../../../translated_images/sr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Демо структурисане анализе" width="800"/>

*Ревизија кода заснована на оквиру*

### Разговор са више корака

Питајте "Шта је Spring Boot?" па одмах потом "Прикажи пример". Модел памти ваше прво питање и даје вам пример специфично за Spring Boot. Без меморије, то друго питање било би прегенерално.

<img src="../../../translated_images/sr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Демо разговора са више корака" width="800"/>

*Очување контекста кроз питања*

### Размишљање корак по корак

Изаберите задатак из математике и испробајте га и са Размишљањем корак по корак и са ниским ентузијазмом. Ниски ентузијазам даје само одговор - брзо али непрозирно. Размишљање корак по корак приказује сваки рачунски корак и одлуку.

<img src="../../../translated_images/sr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Демо размишљања корак по корак" width="800"/>

*Математички задатак са јасним корацима*

### Ограничени излаз

Када вам требају специфични формати или број речи, овај образац осигурава строго поштовање. Покушајте да генеришете резиме са тачно 100 речи у облику спискова.

<img src="../../../translated_images/sr/constrained-output-demo.567cc45b75da1633.webp" alt="Демо ограниченог излаза" width="800"/>

*Резиме машинског учења са контролом формата*

## Оно што заиста учите

**Напор размишљања мења све**

GPT-5.2 вам омогућава да контролишете рачунски напор кроз своје упите. Мали напор значи брзе одговоре са минималним истраживањем. Велики напор значи да модел узима време за дубоко размишљање. Учите како да ускладите напор са сложеношћу задатка – не губите време на једноставна питања, али не журите са сложеним одлукама.

**Структура води понашање**

Примећујете XML тагове у упитима? Они нису украсни. Модели много поузданије прате структуиане инструкције него слободан текст. Када вам требају вишестепени процеси или сложена логика, структура помаже моделу да прати где је и шта следи.

<img src="../../../translated_images/sr/prompt-structure.a77763d63f4e2f89.webp" alt="Структура упита" width="800"/>

*Анатомија добро структуираног упита са јасним секцијама и организацијом у стилу XML-а*

**Квалитет кроз самоевалуацију**

Обрасци који саморефлектују раде тако што јасно дефинишу критеријуме квалитета. Уместо да се надају да ће модел "урадити исправно", ви му тачно кажете шта "исправно" значи: исправна логика, руковање грешкама, перформансе, безбедност. Модел онда може сам да оцени свој излаз и унапреди се. Ово претвара генерисање кода из лутрије у процес.

**Контекст је ограничен**

Разговори са више корака раде тако што сваки захтев садржи историју порука. Али постоји ограничење – сваки модел има максималан број токена. Како разговори расту, биће вам потребне стратегије да сачувате релевантан контекст без преласка тог максимума. Овај модул вам показује како памћење ради; касније ћете научити када да правите резиме, када да заборавите и када да преузмете.

## Следећи кораци

**Следећи модул:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Навигација:** [← Претходно: Модул 01 - Увод](../01-introduction/README.md) | [Назад на почетак](../README.md) | [Следећи: Модул 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање одговорности**:
Овај документ је преведен коришћењем АИ услуге за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде тачан, молимо имајте у виду да аутоматизовани преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетом. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било какве неспоразуме или погрешне тумачења настала употребом овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->