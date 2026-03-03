# Модуль 00: Быстрый старт

## Содержание

- [Введение](../../../00-quick-start)
- [Что такое LangChain4j?](../../../00-quick-start)
- [Зависимости LangChain4j](../../../00-quick-start)
- [Требования](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Получите ваш токен GitHub](../../../00-quick-start)
  - [2. Установите токен](../../../00-quick-start)
- [Запустите примеры](../../../00-quick-start)
  - [1. Основной чат](../../../00-quick-start)
  - [2. Шаблоны запросов](../../../00-quick-start)
  - [3. Вызов функций](../../../00-quick-start)
  - [4. Вопросы и ответы по документам (Easy RAG)](../../../00-quick-start)
  - [5. Ответственный ИИ](../../../00-quick-start)
- [Что демонстрирует каждый пример](../../../00-quick-start)
- [Дальнейшие шаги](../../../00-quick-start)
- [Устранение неполадок](../../../00-quick-start)

## Введение

Этот быстрый старт предназначен для того, чтобы вы могли как можно быстрее начать работать с LangChain4j. Он охватывает самые основы создания AI-приложений с помощью LangChain4j и моделей GitHub. В следующих модулях вы переключитесь на Azure OpenAI и GPT-5.2 и углубитесь в каждый концепт.

## Что такое LangChain4j?

LangChain4j — это Java-библиотека, упрощающая создание приложений с искусственным интеллектом. Вместо работы с HTTP-клиентами и разбором JSON вы используете чистые Java API.

«Chain» в LangChain означает цепочку компонентов — вы можете связать вместе запрос, модель и парсер, или связать несколько вызовов ИИ, где вывод одного становится входом для следующего. Этот быстрый старт сосредоточен на основах перед изучением более сложных цепочек.

<img src="../../../translated_images/ru/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Компоненты в LangChain4j соединяются в цепочку — строительные блоки создают мощные AI-процессы*

Мы используем три основных компонента:

**ChatModel** — интерфейс для взаимодействия с AI-моделью. Вызываете `model.chat("prompt")` и получаете ответ в виде строки. Мы используем `OpenAiOfficialChatModel`, который работает с API, совместимыми с OpenAI, такими как модели GitHub.

**AiServices** — создает типобезопасные интерфейсы AI-сервисов. Определяете методы, аннотируете их `@Tool`, и LangChain4j управляет оркестрацией. ИИ автоматически вызывает ваши Java-методы при необходимости.

**MessageWindowChatMemory** — хранит историю разговора. Без него каждый запрос независим. С ним ИИ запоминает предыдущие сообщения и сохраняет контекст через несколько ходов.

<img src="../../../translated_images/ru/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Архитектура LangChain4j — основные компоненты работают вместе, чтобы запустить ваши AI-приложения*

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

Модуль `langchain4j-open-ai-official` предоставляет класс `OpenAiOfficialChatModel`, который подключается к API, совместимым с OpenAI. Модели GitHub используют тот же формат API, поэтому специальный адаптер не нужен — просто укажите базовый URL `https://models.github.ai/inference`.

Модуль `langchain4j-easy-rag` обеспечивает автоматическое разделение документов, создание векторных представлений и поиск, чтобы вы могли создавать RAG-приложения без ручной настройки каждого шага.

## Требования

**Используете Dev Container?** Java и Maven уже установлены. Вам нужен только персональный токен доступа GitHub.

**Локальная разработка:**
- Java 21+, Maven 3.9+
- Персональный токен доступа GitHub (инструкции ниже)

> **Примечание:** Этот модуль использует модель `gpt-4.1-nano` из моделей GitHub. Не изменяйте имя модели в коде — оно настроено для работы с доступными моделями GitHub.

## Настройка

### 1. Получите ваш токен GitHub

1. Перейдите в [Настройки GitHub → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Нажмите «Generate new token»
3. Задайте описательное имя (например, «LangChain4j Demo»)
4. Установите срок действия (рекомендуется 7 дней)
5. В разделе «Account permissions» найдите «Models» и установите «Read-only»
6. Нажмите «Generate token»
7. Скопируйте и сохраните токен — его больше не покажут

### 2. Установите токен

**Вариант 1: Использование VS Code (рекомендуется)**

Если вы используете VS Code, добавьте токен в файл `.env` в корне проекта:

Если файл `.env` отсутствует, скопируйте `.env.example` в `.env` или создайте новый файл `.env` в корне проекта.

**Пример файла `.env`:**
```bash
# В /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Затем просто кликните правой кнопкой по любому демо-файлу (например, `BasicChatDemo.java`) в Проводнике и выберите **«Run Java»** или используйте конфигурации запуска из панели Run and Debug.

**Вариант 2: Использование терминала**

Установите переменную окружения с токеном:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Запуск примеров

**Использование VS Code:** Просто кликните правой кнопкой по любому демо-файлу в Проводнике и выберите **«Run Java»**, либо используйте конфигурации запуска из панели Run and Debug (предварительно добавьте токен в `.env`).

**Использование Maven:** Или запустите из командной строки:

### 1. Основной чат

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Шаблоны запросов

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Демонстрирует нулевой-shot, few-shot, chain-of-thought и ролевой подходы к запросам.

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

Задавайте вопросы по вашим документам с помощью Easy RAG с автоматическим созданием векторных представлений и поиском.

### 5. Ответственный ИИ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Увидьте, как фильтры безопасности ИИ блокируют вредоносный контент.

## Что демонстрирует каждый пример

**Основной чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Начните здесь, чтобы увидеть LangChain4j в самом простом виде. Вы создадите `OpenAiOfficialChatModel`, отправите запрос через `.chat()` и получите ответ. Это демонстрирует основу: как инициализировать модели с помощью пользовательских конечных точек и API-ключей. Поняв этот шаблон, вы сможете строить всё остальное на его базе.

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
> - «Как переключиться с моделей GitHub на Azure OpenAI в этом коде?»
> - «Какие другие параметры я могу настроить в OpenAiOfficialChatModel.builder()?»
> - «Как добавить потоковые ответы вместо ожидания полного ответа?»

**Инжиниринг запросов** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Теперь, когда вы знаете, как общаться с моделью, давайте изучим, что вы ей говорите. В этом демо используется та же настройка модели, но показаны пять различных паттернов запросов. Попробуйте нулевой-shot для прямых инструкций, few-shot — когда модель обучается на примерах, chain-of-thought — для вывода промежуточных рассуждений, и ролевой подход — для заданного контекста. Вы увидите, как один и тот же модель даёт кардинально разные результаты в зависимости от формулировки запроса.

Демо также демонстрирует шаблоны запросов, мощный способ создавать переиспользуемые запросы с переменными.
Ниже пример использования `PromptTemplate` LangChain4j для подстановки переменных. ИИ отвечает, основываясь на указанном пункте назначения и активности.

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
> - «В чем разница между нулевым-shot и few-shot запросами и когда использовать каждый?»
> - «Как параметр температуры влияет на ответы модели?»
> - «Какие техники помогают предотвратить атаки с внедрением запросов в продакшене?»
> - «Как создавать переиспользуемые объекты PromptTemplate для распространённых паттернов?»

**Интеграция инструментов** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Вот где LangChain4j становится мощным. Вы используете `AiServices` для создания помощника на базе ИИ, который может вызывать ваши Java-методы. Просто аннотируйте методы `@Tool("описание")`, и LangChain4j выполнит остальное — ИИ сам решает, когда использовать каждый инструмент, исходя из запроса пользователя. Это демонстрирует вызов функций, ключевой приём создания ИИ, который может совершать действия, а не только отвечать на вопросы.

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
> - «Как работает аннотация @Tool и что LangChain4j с ней делает?»
> - «Может ли ИИ вызывать несколько инструментов подряд для решения сложных задач?»
> - «Что происходит, если инструмент выбрасывает исключение — как обрабатывать ошибки?»
> - «Как интегрировать реальное API вместо этого калькулятора?»

**Вопросы и ответы по документам (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Здесь вы увидите RAG (retrieval-augmented generation) с использованием подхода LangChain4j «Easy RAG». Документы загружаются, автоматически разбиваются на части и преобразуются в векторные представления, которые хранятся в оперативной памяти, затем компонент поиска подаёт AI релевантные фрагменты при запросе. ИИ отвечает на основе ваших документов, а не общей обучающей информации.

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
> - «В чем разница между этим простым подходом и кастомной RAG-конвейером?»
> - «Как масштабировать это для обработки нескольких документов или больших баз знаний?»

**Ответственный ИИ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Создавайте безопасность ИИ с многоуровневой защитой. В этом демо показано, как работают вместе два уровня защиты:

**Часть 1: LangChain4j Input Guardrails** — блокируют опасные запросы до того, как они попадут в LLM. Создавайте свои собственные guardrails, проверяющие запрещённые ключевые слова или паттерны. Они работают в вашем коде, так что быстро и бесплатно.

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

**Часть 2: Фильтры безопасности поставщика** — у моделей GitHub встроены фильтры, которые ловят то, что могут пропустить ваши guardrails. Вы увидите жёсткие блокировки (ошибки HTTP 400) при серьезных нарушениях и мягкие отказы, когда ИИ вежливо отказывается.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и спросите:
> - «Что такое InputGuardrail и как создать свой?»
> - «В чем разница между жёсткой блокировкой и мягким отказом?»
> - «Зачем использовать вместе guardrails и фильтры поставщика?»

## Дальнейшие шаги

**Следующий модуль:** [01-introduction - Начало работы с LangChain4j](../01-introduction/README.md)

---

**Навигация:** [← Назад к основному](../README.md) | [Далее: Модуль 01 - Введение →](../01-introduction/README.md)

---

## Устранение неполадок

### Первый запуск сборки Maven

**Проблема:** Первый запуск `mvn clean compile` или `mvn package` занимает много времени (10–15 минут)

**Причина:** Maven должен скачать все зависимости проекта (Spring Boot, библиотеки LangChain4j, Azure SDK и т.д.) при первой сборке.

**Решение:** Это нормальное поведение. Последующие сборки будут намного быстрее, так как зависимости уже будут кэшированы локально. Время загрузки зависит от скорости вашего интернета.

### Синтаксис команд Maven в PowerShell

**Проблема:** Команды Maven завершаются ошибкой `Unknown lifecycle phase ".mainClass=..."`
**Причина**: PowerShell интерпретирует `=` как оператор присваивания переменной, что нарушает синтаксис свойства Maven

**Решение**: Используйте оператор остановки разбора `--%` перед командой Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` сообщает PowerShell передавать все последующие аргументы буквально Maven без интерпретации.

### Отображение эмодзи в Windows PowerShell

**Проблема**: В ответах ИИ вместо эмодзи отображаются мусорные символы (например, `????` или `â??`) в PowerShell

**Причина**: Кодировка по умолчанию PowerShell не поддерживает UTF-8 эмодзи

**Решение**: Выполните эту команду перед запуском Java-приложений:
```cmd
chcp 65001
```

Это принудительно включает кодировку UTF-8 в терминале. Альтернативно используйте Windows Terminal, который лучше поддерживает Unicode.

### Отладка вызовов API

**Проблема**: Ошибки аутентификации, ограничения по частоте запросов или неожиданные ответы от модели ИИ

**Решение**: В примерах используются `.logRequests(true)` и `.logResponses(true)` для отображения вызовов API в консоли. Это помогает выявлять ошибки аутентификации, ограничения по частоте запросов или неожиданные ответы. Уберите эти флаги в продуктивной среде, чтобы уменьшить количество записей в логе.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с помощью ИИ-сервиса перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на то, что мы стремимся к точности, имейте в виду, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его родном языке следует считать авторитетным источником. Для критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->