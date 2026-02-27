# Модул 00: Брзи почетак

## Садржај

- [Увод](../../../00-quick-start)
- [Шта је LangChain4j?](../../../00-quick-start)
- [LangChain4j зависности](../../../00-quick-start)
- [Услови](../../../00-quick-start)
- [Подешавање](../../../00-quick-start)
  - [1. Добијте свој GitHub токен](../../../00-quick-start)
  - [2. Поставите свој токен](../../../00-quick-start)
- [Покрени примере](../../../00-quick-start)
  - [1. Основни чет](../../../00-quick-start)
  - [2. Обрасци упита](../../../00-quick-start)
  - [3. Позивање функција](../../../00-quick-start)
  - [4. Питања и одговори за документе (Easy RAG)](../../../00-quick-start)
  - [5. Одговорни AI](../../../00-quick-start)
- [Шта сваки пример показује](../../../00-quick-start)
- [Следећи кораци](../../../00-quick-start)
- [Отстрањивање проблема](../../../00-quick-start)

## Увод

Овај брзи почетак има за циљ да вас што брже упути у коришћење LangChain4j. Обухвата апсолутне основе изградње AI апликација са LangChain4j и GitHub моделима. У наредним модулима ћете користити Azure OpenAI са LangChain4j за изградњу напреднијих апликација.

## Шта је LangChain4j?

LangChain4j је Java библиотека која поједностављује изградњу AI-подржаних апликација. Уместо да радите са HTTP клијентима и парсирањем JSON-а, радите са чистим Java API-јима.

„Ланац“ у LangChain означава повезивање више компоненти - можете повезати упит (prompt) са моделом, па са парсером, или повезати више AI позива где један излаз иде у следећи улаз. Овај брзи почетак се фокусира на основе пре него што истражимо сложеније ланце.

<img src="../../../translated_images/sr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Повезивање компоненти у LangChain4j - грађевински блокови се спајају како би направили моћне AI токове рада*

Користићемо три основне компоненте:

**ChatModel** - Интерфејс за интеракцију са AI моделом. Позовите `model.chat("prompt")` и добијете одговор као стринг. Користимо `OpenAiOfficialChatModel` који ради са OpenAI-компатибилним ентпојнтима као што су GitHub модели.

**AiServices** - Креира тип-безбедне AI сервисне интерфејсе. Дефинишите методе, означите их са `@Tool`, а LangChain4j се брине о оркестрацији. AI аутоматски позива ваше Java методе када је потребно.

**MessageWindowChatMemory** - Чува историју разговора. Без тога, сваки захтев је независан. Са тим, AI памти претходне поруке и одржава контекст кроз више корака.

<img src="../../../translated_images/sr/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура LangChain4j - основне компоненте које сарађују да покрену ваше AI апликације*

## LangChain4j зависности

Овај брзи почетак користи три Maven зависности у [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` модул обезбеђује класу `OpenAiOfficialChatModel` која се повезује на OpenAI-компатибилне API-је. GitHub модели користе исти формат API-ја, па није потребан никакав посебан адаптер — само усмерите базну URL адресу на `https://models.github.ai/inference`.

`langchain4j-easy-rag` модул обезбеђује аутоматско делење докумената, уграђивање и преузимање тако да можете правити RAG апликације без ручног конфигурисања сваког корака.

## Услови

**Користите ли Dev Container?** Јава и Maven су већ инсталирани. Потребан вам је само GitHub Personal Access Token.

**Локални развој:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (упутство у наставку)

> **Напомена:** Овај модул користи `gpt-4.1-nano` из GitHub модела. Немојте мењати назив модела у коду - конфигурисано је да ради са доступним GitHub моделима.

## Подешавање

### 1. Добијте свој GitHub токен

1. Идите на [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Кликните на „Generate new token“
3. Поставите описно име (нпр. „LangChain4j Demo“)
4. Подесите истекање (препоручено 7 дана)
5. У „Account permissions“ пронађите „Models“ и подесите на „Read-only“
6. Кликните „Generate token“
7. Копирајте и сачувајте свој токен - нећете га видети поново

### 2. Поставите свој токен

**Опција 1: Коришћење VS Code-а (препоручено)**

Ако користите VS Code, додајте свој токен у `.env` фајл у корену пројекта:

Ако `.env` фајл не постоји, копирајте `.env.example` у `.env` или направите нови `.env` фајл у корену пројекта.

**Пример `.env` фајла:**
```bash
# У /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Затим можете једноставно десним кликом на било који демонстрациони фајл (нпр. `BasicChatDemo.java`) у Explorer-у изабрати **"Run Java"** или користити конфигурације за покретање из Run and Debug панела.

**Опција 2: Коришћење терминала**

Поставите токен као променљиву окружења:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Покрени примере

**Користећи VS Code:** Једноставно десним кликом на било који демонстрациони фајл у Explorer-у изаберите **"Run Java"**, или користите конфигурације за покретање из Run and Debug панела (претходно додајте токен у `.env`).

**Користећи Maven:** Алтернативно, можете покренути из командне линије:

### 1. Основни чет

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Обрасци упита

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Приказује zero-shot, few-shot, chain-of-thought, и улогом засновано покретање упита.

### 3. Позивање функција

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI аутоматски позива ваше Java методе када је потребно.

### 4. Питања и одговори за документе (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Постављајте питања о вашим документима користећи Easy RAG са аутоматским уграђивањем и преузимањем.

### 5. Одговорни AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Погледајте како AI филтри за безбедност блокирају штетни садржај.

## Шта сваки пример показује

**Основни чет** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Почните овде да бисте видели LangChain4j у најосновнијем облику. Креираћете `OpenAiOfficialChatModel`, послати упит са `.chat()`, и добити одговор. Ово показује основу: како иницијализовати моделе са прилагођеним ентпојнтима и API кључевима. Када разумете овај образац, све остало се надограђује на њему.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворите [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и питајте:
> - "Како да пребацим са GitHub модела на Azure OpenAI у овом коду?"
> - "Које друге параметре могу да конфигуришем у OpenAiOfficialChatModel.builder()?"
> - "Како додати стриминг одговоре уместо чекања на комплетан одговор?"

**Инжењеринг упита** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Сада када знате како да разговарате са моделом, хајде да истражимо шта му говорите. Овај демо користи исто подешавање модела али показује пет различитих образаца упита. Испробајте zero-shot упите за директна упутства, few-shot упите који уче из примера, chain-of-thought упите који показују разлоге корака, и улогом засноване упите који постављају контекст. Видећете како исти модел даје драматично различите резултате у зависности од тога како формулишете ваш захтев.

Демо такође показује шаблоне упита, које су моћан начин за креирање поново коришћених упита са променљивима. Испод је пример упита који користи LangChain4j `PromptTemplate` за попуњавање променљивих. AI ће одговорити на основу дате дестинације и активности.

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

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворите [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и питајте:
> - "Која је разлика између zero-shot и few-shot упита и када треба користити који?"
> - "Како параметар temperature утиче на одговоре модела?"
> - "Које су технике за спречавање prompt injection напада у продукцији?"
> - "Како да направим поново коришћене PromptTemplate објекте за уобичајене обрасце?"

**Интеграција алата** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Овде LangChain4j постаје моћан. Користићете `AiServices` да направите AI асистента који може да позива ваше Java методе. Само означите методе са `@Tool("description")` и LangChain4j се брине о свему - AI аутоматски одлучује када да користи који алат у зависности од тога шта корисник пита. Ово показује позивање функција, кључну технику за изградњу AI који може да предузима акције, а не само одговара на питања.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворите [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и питајте:
> - "Како функционише @Tool анотација и шта LangChain4j ради са њом у позадини?"
> - "Може ли AI да позове више алата у низу за решавање сложених проблема?"
> - "Шта се дешава ако алат баци изузетак - како треба да обрађујем грешке?"
> - "Како бих интегрисао прави API уместо овог примера калкулатора?"

**Питања и одговори за документе (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Овде ћете видети RAG (retrieval-augmented generation) користећи LangChain4j приступ „Easy RAG“. Документи се учитавају, аутоматски деле и убијају у унутрашњу меморију, а потом преузимач садржаја доставља релевантне делове AI-ју у тренутку упита. AI одговара на основу ваших докумената, а не опште базе знања.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворите [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и питајте:
> - "Како RAG спречава AI халуцинације у поређењу са употребом података за обуку модела?"
> - "Која је разлика између овог лаког приступа и прилагођеног RAG процеса?"
> - "Како бих ово скалирао за рад са више докумената или већим базама знања?"

**Одговорни AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Изградите безбедност AI модела са одбраном у дубини. Овај демо приказује два заштитна нивоа која раде заједно:

**Део 1: LangChain4j улазне заштитне мреже (Input Guardrails)** - Блокирају опасне упите пре него што стигну до LLM-а. Креирајте прилагођене заштитне мреже које проверавају забрањене кључне речи или образце. Оне се извршавају у вашем коду, па су брзе и бесплатне.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Део 2: Провајдерски филтри безбедности** - GitHub модели имају уграђене филтере који хватају оно што ваше заштитне мреже могу пропустити. Видећете тврде блокаде (HTTP 400 грешке) за озбиљне повреде и мекане одбијене одговоре где AI љубазно одбија.

> **🤖 Испробајте уз [GitHub Copilot](https://github.com/features/copilot) Chat:** Отворите [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и питајте:
> - "Шта је InputGuardrail и како да направим свој?"
> - "Која је разлика између тврде блокаде и меканог одбијања?"
> - "Зашто користити и заштитне мреже и провајдерске филтере заједно?"

## Следећи кораци

**Следећи модул:** [01-introduction - Почетак рада са LangChain4j и gpt-5 на Azure](../01-introduction/README.md)

---

**Навигација:** [← Назад на Главну](../README.md) | [Следеће: Модул 01 - Увод →](../01-introduction/README.md)

---

## Отстрањивање проблема

### Први Maven билд

**Проблем:** Почетна команда `mvn clean compile` или `mvn package` дуго траје (10-15 минута)

**Разлог:** Maven треба да преузме све зависности пројекта (Spring Boot, LangChain4j библиотеке, Azure SDK-ове итд.) при првом билду.

**Решење:** Ово је нормално понашање. Накнадни билдови ће бити много бржи јер ће зависности бити кеширане локално. Време преузимања зависи од брзине ваше мреже.

### PowerShell Maven команда синтакса

**Проблем:** Maven команде не успевају са грешком `Unknown lifecycle phase ".mainClass=..."`
**Uzrok**: PowerShell tumači `=` kao operator dodele vrednosti varijabli, što prekida Maven sintaksu svojstava

**Rešenje**: Koristite operator za zaustavljanje parsiranja `--%` pre Maven komande:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` govori PowerShell-u da prosledi sve preostale argumente bukvalno Mavenu bez tumačenja.

### Prikaz emodžija u Windows PowerShell

**Problem**: AI odgovori prikazuju besmislene karaktere (npr. `????` ili `â??`) umesto emodžija u PowerShell-u

**Uzrok**: Podrazumevano kodiranje PowerShell-a ne podržava UTF-8 emodžije

**Rešenje**: Pokrenite ovu komandu pre izvršavanja Java aplikacija:
```cmd
chcp 65001
```

Ovo forsira UTF-8 kodiranje u terminalu. Alternativno, koristite Windows Terminal koji ima bolju Unicode podršku.

### Otklanjanje grešaka u API pozivima

**Problem**: Greške autentifikacije, ograničenja brzine ili neočekivani odgovori od AI modela

**Rešenje**: Primeri uključuju `.logRequests(true)` i `.logResponses(true)` da bi se API pozivi prikazali u konzoli. Ovo pomaže u rešavanju problema sa autentifikacijom, ograničenjima brzine ili neočekivanim odgovorima. Uklonite ove zastavice u produkciji da smanjite buku u logu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:
Овај документ је преведен коришћењем AI преводилачке услуге [Co-op Translator](https://github.com/Azure/co-op-translator). Док тежимо прецизности, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Изворни документ на његовом изворном језику треба сматрати ауторитетом. За критичне информације препоручује се професионалан превод од стране људи. Нисмо одговорни за било каква неспоразума или погрешна тумачења која проистекну из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->