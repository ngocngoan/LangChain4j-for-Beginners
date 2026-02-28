# Модуль 00: Быстрый старт

## Содержание

- [Введение](../../../00-quick-start)
- [Что такое LangChain4j?](../../../00-quick-start)
- [Зависимости LangChain4j](../../../00-quick-start)
- [Требования](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Получите свой токен GitHub](../../../00-quick-start)
  - [2. Установите свой токен](../../../00-quick-start)
- [Запуск примеров](../../../00-quick-start)
  - [1. Базовый чат](../../../00-quick-start)
  - [2. Шаблоны подсказок](../../../00-quick-start)
  - [3. Вызов функций](../../../00-quick-start)
  - [4. Вопросы и ответы по документам (Easy RAG)](../../../00-quick-start)
  - [5. Ответственный ИИ](../../../00-quick-start)
- [Что показывает каждый пример](../../../00-quick-start)
- [Следующие шаги](../../../00-quick-start)
- [Устранение неполадок](../../../00-quick-start)

## Введение

Этот быстрый старт предназначен для того, чтобы вы как можно быстрее начали работать с LangChain4j. В нем рассматриваются самые базовые принципы создания AI-приложений с помощью LangChain4j и моделей GitHub. В следующих модулях вы будете использовать Azure OpenAI с LangChain4j для создания более продвинутых приложений.

## Что такое LangChain4j?

LangChain4j — это Java-библиотека, упрощающая создание приложений с поддержкой искусственного интеллекта. Вместо работы с HTTP клиентами и парсингом JSON вы используете чистые Java API.

"Цепочка" в LangChain означает последовательное соединение нескольких компонентов — вы можете связать подсказку с моделью и парсером или соединить несколько вызовов ИИ, где выход одного становится входом следующего. Этот быстрый старт сфокусирован на основах, прежде чем перейти к более сложным цепочкам.

<img src="../../../translated_images/ru/langchain-concept.ad1fe6cf063515e1.webp" alt="Концепция цепочек LangChain4j" width="800"/>

*Связывание компонентов в LangChain4j — строительные блоки соединяются для создания мощных AI рабочих процессов*

Мы используем три основных компонента:

**ChatModel** — интерфейс для взаимодействия с AI-моделью. Вызовите `model.chat("prompt")` и получите строку ответа. Мы используем `OpenAiOfficialChatModel`, который работает с OpenAI-совместимыми конечными точками, такими как GitHub Models.

**AiServices** — создает типобезопасные интерфейсы AI-сервисов. Определите методы, аннотируйте их с `@Tool`, и LangChain4j берет на себя оркестрацию. ИИ автоматически вызывает ваши Java-методы при необходимости.

**MessageWindowChatMemory** — хранит историю беседы. Без этого каждого запроса рассматривается как отдельного. С ним ИИ запоминает предыдущие сообщения и поддерживает контекст в нескольких ходах.

<img src="../../../translated_images/ru/architecture.eedc993a1c576839.webp" alt="Архитектура LangChain4j" width="800"/>

*Архитектура LangChain4j — основные компоненты работают вместе, чтобы обеспечить работу ваших AI-приложений*

## Зависимости LangChain4j

Этот быстрый старт использует три зависимости Maven в [`pom.xml`](../../../00-quick-start/pom.xml):

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

Модуль `langchain4j-open-ai-official` предоставляет класс `OpenAiOfficialChatModel`, который подключается к API, совместимому с OpenAI. GitHub Models использует тот же формат API, поэтому специальный адаптер не нужен — просто укажите базовый URL `https://models.github.ai/inference`.

Модуль `langchain4j-easy-rag` обеспечивает автоматическое разделение документов, встраивание и поиск, что позволяет создавать RAG-приложения без ручной настройки каждого шага.

## Требования

**Используете Dev Container?** Java и Maven уже установлены. Вам нужен только персональный токен доступа GitHub.

**Локальная разработка:**
- Java 21+, Maven 3.9+
- Персональный токен доступа GitHub (инструкции ниже)

> **Примечание:** В этом модуле используется `gpt-4.1-nano` из GitHub Models. Не изменяйте имя модели в коде — оно настроено на работу с доступными моделями GitHub.

## Настройка

### 1. Получите свой токен GitHub

1. Перейдите в [Настройки GitHub → Персональные токены доступа](https://github.com/settings/personal-access-tokens)
2. Нажмите «Generate new token»
3. Установите описательное имя (например, «LangChain4j Demo»)
4. Установите срок действия (рекомендуется 7 дней)
5. В разделе «Account permissions» найдите «Models» и установите «Read-only»
6. Нажмите «Generate token»
7. Скопируйте и сохраните токен — вы больше не увидите его

### 2. Установите свой токен

**Вариант 1: Использование VS Code (рекомендуется)**

Если вы используете VS Code, добавьте токен в файл `.env` в корне проекта:

Если файла `.env` нет, скопируйте `.env.example` в `.env` или создайте новый `.env` в корне проекта.

**Пример файла `.env`:**
```bash
# В /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

После этого вы можете просто щелкнуть правой кнопкой мыши на любом демонстрационном файле (например, `BasicChatDemo.java`) в обозревателе и выбрать **«Run Java»** или использовать конфигурации запуска в панели Run and Debug.

**Вариант 2: Через терминал**

Установите токен как переменную окружения:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Запуск примеров

**В VS Code:** Просто щелкните правой кнопкой по любому демонстрационному файлу в обозревателе и выберите **«Run Java»**, или используйте конфигурации запуска в панели Run and Debug (предварительно добавьте токен в файл `.env`).

**Через Maven:** Также можно запустить из командной строки:

### 1. Базовый чат

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Шаблоны подсказок

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Показывает zero-shot, few-shot, цепочку рассуждений и подсказки на основе ролей.

### 3. Вызов функций

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ИИ автоматически вызывает ваши Java-методы при необходимости.

### 4. Вопросы и ответы по документам (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте вопросы по вашим документам, используя Easy RAG с автоматическим встраиванием и поиском.

### 5. Ответственный ИИ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Посмотрите, как фильтры безопасности ИИ блокируют вредоносный контент.

## Что показывает каждый пример

**Базовый чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Начните здесь, чтобы увидеть LangChain4j в самом простом виде. Вы создадите `OpenAiOfficialChatModel`, отправите подсказку с `.chat()` и получите ответ. Это демонстрирует основу: как инициализировать модели с пользовательскими конечными точками и ключами API. Когда вы поймете этот паттерн, всё остальное строится на его основе.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и спросите:
> - «Как переключиться с GitHub Models на Azure OpenAI в этом коде?»
> - «Какие другие параметры можно настроить в OpenAiOfficialChatModel.builder()?»
> - «Как добавить потоковые ответы вместо ожидания полного ответа?»

**Инженерия подсказок** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Теперь, когда вы умеете общаться с моделью, изучим, что именно вы ей говорите. Этот пример использует ту же модель, но показывает пять различных паттернов подсказок. Попробуйте zero-shot для прямых инструкций, few-shot для обучения на примерах, цепочку рассуждений для пошаговых выводов и подсказки на основе ролей для установки контекста. Вы увидите, как одна и та же модель дает кардинально разные результаты в зависимости от формулировки запроса.

Демонстрация также показывает шаблоны подсказок — мощный способ создавать повторно используемые подсказки с переменными.
Ниже пример подсказки с использованием LangChain4j `PromptTemplate` для заполнения переменных. ИИ ответит на основе заданного пункта назначения и активности.

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

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) и спросите:
> - «В чем разница между zero-shot и few-shot подсказками и когда использовать каждую?»
> - «Как параметр temperature влияет на ответы модели?»
> - «Какие есть техники для защиты от атак с подменой подсказок в продакшене?»
> - «Как создавать повторно используемые объекты PromptTemplate для часто используемых паттернов?»

**Интеграция инструментов** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Здесь LangChain4j становится мощным. Вы используете `AiServices` для создания AI-помощника, который может вызывать ваши Java-методы. Просто аннотируйте методы `@Tool("описание")`, и LangChain4j всё сделает за вас — ИИ автоматически решает, когда использовать каждый инструмент, основываясь на вопросах пользователя. Это демонстрирует вызов функций — ключевую технику для создания ИИ, который не только отвечает на вопросы, но и выполняет действия.

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

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и спросите:
> - «Как работает аннотация @Tool и что LangChain4j делает с ней за кулисами?»
> - «Может ли ИИ вызвать несколько инструментов последовательно для решения сложных задач?»
> - «Что происходит, если инструмент выбрасывает исключение — как обрабатывать ошибки?»
> - «Как интегрировать реальный API вместо этого примера с калькулятором?»

**Вопросы и ответы по документам (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Здесь вы увидите RAG (retrieval-augmented generation) с использованием подхода LangChain4j "Easy RAG". Документы загружаются, автоматически разбиваются, встраиваются в оперативную память, а модуль поиска предоставляет релевантные части ИИ при запросе. ИИ отвечает на основе ваших документов, а не общей обученной модели.

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

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и спросите:
> - «Как RAG предотвращает галлюцинации ИИ по сравнению с использованием обучающих данных модели?»
> - «В чем разница между этим простым подходом и пользовательским RAG pipeline?»
> - «Как масштабировать это для работы с множеством документов или большими базами знаний?»

**Ответственный ИИ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Создайте безопасность ИИ с защитой в глубину. Этот пример показывает два слоя защиты, работающих вместе:

**Часть 1: LangChain4j Input Guardrails** — блокирует опасные подсказки до их передачи в LLM. Создавайте свои собственные правила, проверяющие запрещённые ключевые слова или шаблоны. Они выполняются в вашем коде, поэтому быстрые и бесплатные.

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

**Часть 2: Фильтры безопасности провайдера** — GitHub Models имеет встроенные фильтры, которые ловят то, что могли пропустить ваши guardrails. Вы увидите жёсткие блокировки (ошибки HTTP 400) при серьёзных нарушениях и мягкие отказы, когда ИИ вежливо отказывается.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и спросите:
> - «Что такое InputGuardrail и как создать свой собственный?»
> - «В чем разница между жесткой блокировкой и мягким отказом?»
> - «Почему использовать одновременно guardrails и фильтры провайдера?»

## Следующие шаги

**Следующий модуль:** [01-introduction - Начало работы с LangChain4j и gpt-5 на Azure](../01-introduction/README.md)

---

**Навигация:** [← Назад к Главной](../README.md) | [Далее: Модуль 01 - Введение →](../01-introduction/README.md)

---

## Устранение неполадок

### Первая сборка Maven

**Проблема:** Изначальный `mvn clean compile` или `mvn package` занимает много времени (10-15 минут)

**Причина:** Maven должен загрузить все зависимости проекта (Spring Boot, библиотеки LangChain4j, Azure SDK и т. д.) при первой сборке.

**Решение:** Это нормальное поведение. Последующие сборки будут значительно быстрее, поскольку зависимости кэшируются локально. Время загрузки зависит от скорости вашего интернета.

### Синтаксис команды Maven в PowerShell

**Проблема:** Команды Maven выдают ошибку `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell интерпретирует `=` как оператор присваивания переменной, нарушая синтаксис свойств Maven

**Решение**: Используйте оператор прекращения разбора `--%` перед командой Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` говорит PowerShell передавать все последующие аргументы буквально Maven без интерпретации.

### Отображение эмодзи в Windows PowerShell

**Проблема**: В ответах ИИ вместо эмодзи отображаются некорректные символы (например, `????` или `â??`) в PowerShell

**Причина**: Кодировка PowerShell по умолчанию не поддерживает UTF-8 эмодзи

**Решение**: Выполните эту команду перед запуском Java-приложений:
```cmd
chcp 65001
```

Это принудительно включит кодировку UTF-8 в терминале. Альтернативно, используйте Windows Terminal с лучшей поддержкой Юникода.

### Отладка вызовов API

**Проблема**: Ошибки аутентификации, ограничения по количеству запросов или неожиданные ответы от модели ИИ

**Решение**: В примерах используются `.logRequests(true)` и `.logResponses(true)`, чтобы показывать вызовы API в консоли. Это помогает выявлять ошибки аутентификации, ограничения и неожиданные ответы. Уберите эти флаги в продакшене для уменьшения шума в логах.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с помощью сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, пожалуйста, учтите, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на языке оригинала следует считать авторитетным источником. Для важной информации рекомендуется обращаться к профессиональному переводу, выполненному человеком. Мы не несем ответственности за любые недоразумения или искажения смысла, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->