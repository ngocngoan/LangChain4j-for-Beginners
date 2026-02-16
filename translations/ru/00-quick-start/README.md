# Модуль 00: Быстрый старт

## Содержание

- [Введение](../../../00-quick-start)
- [Что такое LangChain4j?](../../../00-quick-start)
- [Зависимости LangChain4j](../../../00-quick-start)
- [Требования](../../../00-quick-start)
- [Настройка](../../../00-quick-start)
  - [1. Получите свой GitHub токен](../../../00-quick-start)
  - [2. Установите свой токен](../../../00-quick-start)
- [Запуск примеров](../../../00-quick-start)
  - [1. Базовый чат](../../../00-quick-start)
  - [2. Шаблоны подсказок](../../../00-quick-start)
  - [3. Вызов функций](../../../00-quick-start)
  - [4. Вопросы и ответы по документам (RAG)](../../../00-quick-start)
  - [5. Ответственный ИИ](../../../00-quick-start)
- [Что показывает каждый пример](../../../00-quick-start)
- [Следующие шаги](../../../00-quick-start)
- [Устранение неполадок](../../../00-quick-start)

## Введение

Этот быстрый старт предназначен, чтобы помочь вам как можно быстрее начать работу с LangChain4j. Он охватывает самые базовые аспекты создания AI-приложений с LangChain4j и GitHub Models. В следующих модулях вы будете использовать Azure OpenAI с LangChain4j для создания более сложных приложений.

## Что такое LangChain4j?

LangChain4j — это Java-библиотека, которая упрощает создание AI-приложений. Вместо работы с HTTP клиентами и разбором JSON вы используете чистые Java API.

Слово «chain» (цепочка) в LangChain означает последовательное соединение нескольких компонентов — вы можете связать подсказку с моделью, модель — с парсером, или объединить несколько вызовов AI, где вывод одного становится вводом для следующего. Этот быстрый старт сосредоточен на основах перед изучением более сложных цепочек.

<img src="../../../translated_images/ru/langchain-concept.ad1fe6cf063515e1.webp" alt="Концепция цепочек в LangChain4j" width="800"/>

*Связывание компонентов в LangChain4j — строительные блоки создают мощные AI-работочие процессы*

Мы будем использовать три основных компонента:

**ChatLanguageModel** — интерфейс для взаимодействия с AI-моделью. Вызовите `model.chat("prompt")` и получите строку ответа. Мы используем `OpenAiOfficialChatModel`, который работает с OpenAI-совместимыми конечными точками, такими как GitHub Models.

**AiServices** — создает типобезопасные интерфейсы AI-сервисов. Определяйте методы, аннотируйте их `@Tool`, и LangChain4j берет на себя оркестрацию. AI автоматически вызывает ваши Java-методы по мере необходимости.

**MessageWindowChatMemory** — поддерживает историю разговора. Без неё каждый запрос независим. С ней AI помнит предыдущие сообщения и сохраняет контекст на протяжении нескольких ходов.

<img src="../../../translated_images/ru/architecture.eedc993a1c576839.webp" alt="Архитектура LangChain4j" width="800"/>

*Архитектура LangChain4j — основные компоненты работают вместе, обеспечивая работу ваших AI-приложений*

## Зависимости LangChain4j

Для этого быстрого старта используются две зависимости Maven в [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Модуль `langchain4j-open-ai-official` предоставляет класс `OpenAiOfficialChatModel`, который подключается к OpenAI-совместимым API. GitHub Models использует тот же формат API, поэтому специальный адаптер не нужен — просто укажите базовый URL `https://models.github.ai/inference`.

## Требования

**Используете Dev Container?** Java и Maven уже установлены. Понадобится только личный токен доступа GitHub.

**Локальная разработка:**
- Java 21+, Maven 3.9+
- Личный токен доступа GitHub (инструкции ниже)

> **Примечание:** Этот модуль использует модель `gpt-4.1-nano` из GitHub Models. Не изменяйте имя модели в коде — оно настроено для работы с имеющимися моделями GitHub.

## Настройка

### 1. Получите свой GitHub токен

1. Перейдите в [GitHub Настройки → Личные токены доступа](https://github.com/settings/personal-access-tokens)
2. Нажмите "Generate new token"
3. Задайте описательное имя (например, "LangChain4j Demo")
4. Установите срок действия (рекомендуется 7 дней)
5. В разделе "Разрешения аккаунта" найдите "Models" и установите "Read-only"
6. Нажмите "Generate token"
7. Скопируйте и сохраните токен — увидеть его снова не получится

### 2. Установите свой токен

**Вариант 1: Использование VS Code (рекомендуется)**

Если вы используете VS Code, добавьте токен в файл `.env` в корне проекта:

Если файла `.env` нет, скопируйте `.env.example` в `.env` или создайте новый `.env` в корне проекта.

**Пример файла `.env`:**
```bash
# В /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Теперь вы можете просто кликнуть правой кнопкой по любому файлу с демо (например, `BasicChatDemo.java`) в проводнике и выбрать **"Run Java"** или использовать конфигурации запуска в панели Run and Debug.

**Вариант 2: Через терминал**

Установите токен в переменную окружения:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Запуск примеров

**Использование VS Code:** Просто кликните правой кнопкой по любому демо-файлу в проводнике и выберите **"Run Java"**, или используйте конфигурации из панели Run and Debug (убедитесь, что сначала добавили токен в файл `.env`).

**Использование Maven:** Также можно запустить из командной строки:

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

Показывает zero-shot, few-shot, chain-of-thought и role-based prompting.

### 3. Вызов функций

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI автоматически вызывает ваши Java-методы по мере необходимости.

### 4. Вопросы и ответы по документам (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Задавайте вопросы по содержимому файла `document.txt`.

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

## Что показывает каждый пример

**Базовый чат** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Начните здесь, чтобы увидеть LangChain4j в самом простом виде. Вы создадите `OpenAiOfficialChatModel`, отправите подсказку с помощью `.chat()` и получите ответ. Это демонстрирует основы: как инициализировать модели с пользовательскими конечными точками и API-ключами. Поняв этот шаблон, вы сможете строить всё остальное на его основе.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) и спросите:
> - "Как переключиться с GitHub Models на Azure OpenAI в этом коде?"
> - "Какие другие параметры я могу настроить в OpenAiOfficialChatModel.builder()?"
> - "Как добавить потоковые ответы вместо ожидания полного ответа?"

**Инжиниринг подсказок** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Теперь, когда вы знаете, как общаться с моделью, давайте изучим, что именно вы ей говорите. Это демо использует ту же настройку модели, но показывает пять различных шаблонов подсказок. Попробуйте zero-shot подсказки для прямых инструкций, few-shot — чтобы модель училась на примерах, chain-of-thought — раскрывающую рассуждения, и role-based — устанавливающую контекст. Вы увидите, как один и тот же модель выдает очень разные результаты в зависимости от формулировки запроса.

Демонстрация также показывает шаблоны подсказок, которые позволяют создавать повторно используемые подсказки с переменными. Ниже пример, где используется `PromptTemplate` из LangChain4j для подстановки переменных. AI ответит на основе указанного назначения и активности.

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
> - "В чем разница между zero-shot и few-shot prompting, и когда использовать каждый?"
> - "Как параметр temperature влияет на ответы модели?"
> - "Какие есть методы для предотвращения атак момента вставки (prompt injection) в продакшене?"
> - "Как создавать повторно используемые объекты PromptTemplate для общих шаблонов?"

**Интеграция инструментов** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Здесь LangChain4j становится мощным. Вы используете `AiServices` для создания AI-помощника, который может вызывать ваши Java-методы. Просто аннотируйте методы `@Tool("описание")`, и LangChain4j позаботится об остальном — AI автоматически решит, когда использовать каждый инструмент в зависимости от вопроса пользователя. Это демонстрирует вызов функций — ключевой прием для построения AI, который может выполнять действия, а не только отвечать на вопросы.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) и спросите:
> - "Как работает аннотация @Tool и что LangChain4j делает с ней за кулисами?"
> - "Может ли AI вызывать несколько инструментов последовательно для решения сложных задач?"
> - "Что происходит, если инструмент выбрасывает исключение — как обрабатывать ошибки?"
> - "Как интегрировать реальное API вместо этого примера калькулятора?"

**Вопросы и ответы по документам (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Здесь вы увидите основы RAG (retrieval-augmented generation). Вместо того, чтобы полагаться на обучающие данные модели, вы загружаете содержимое из [`document.txt`](../../../00-quick-start/document.txt) и включаете его в подсказку. AI отвечает на основе вашего документа, а не общего знания. Это первый шаг к созданию систем, которые могут работать с вашими собственными данными.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Примечание:** Этот простой подход загружает весь документ в подсказку. Для больших файлов (>10 КБ) вы превысите лимиты контекста. В Модуле 03 описано разбиение на части и векторный поиск для промышленного применения RAG.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) и спросите:
> - "Как RAG предотвращает галлюцинации AI по сравнению с использованием обучающих данных модели?"
> - "В чем разница между этим простым подходом и использованием векторных эмбеддингов для поиска?"
> - "Как масштабировать это для работы с несколькими документами или большими базами знаний?"
> - "Какие лучшие практики для структурирования подсказки, чтобы AI использовал только предоставленный контекст?"

**Ответственный ИИ** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Создайте безопасность ИИ с многоуровневой защитой. Это демо показывает два уровня защиты, работающих вместе:

**Часть 1: Встроенные фильтры входных данных LangChain4j** — блокируют опасные подсказки до их передачи в LLM. Создавайте свои защитные правила, которые проверяют запрещённые ключевые слова или шаблоны. Они выполняются в вашем коде, поэтому работают быстро и бесплатно.

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

**Часть 2: Фильтры безопасности провайдера** — GitHub Models имеют встроенные фильтры, которые ловят то, что может пропустить ваш код. Вы увидите жесткие блокировки (ошибки HTTP 400) при серьезных нарушениях и мягкие отказы, когда AI вежливо отказывается.

> **🤖 Попробуйте с [GitHub Copilot](https://github.com/features/copilot) Chat:** Откройте [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) и спросите:
> - "Что такое InputGuardrail и как создать свой собственный?"
> - "В чем разница между жесткой блокировкой и мягким отказом?"
> - "Почему использовать и guardrails, и фильтры провайдера вместе?"

## Следующие шаги

**Следующий модуль:** [01-introduction - Начало работы с LangChain4j и gpt-5 на Azure](../01-introduction/README.md)

---

**Навигация:** [← Назад к главной](../README.md) | [Далее: Модуль 01 - Введение →](../01-introduction/README.md)

---

## Устранение неполадок

### Первый запуск сборки Maven

**Проблема:** Первый запуск `mvn clean compile` или `mvn package` занимает много времени (10-15 минут)

**Причина:** Maven должен скачать все зависимости проекта (Spring Boot, библиотеки LangChain4j, Azure SDK и прочие) при первой сборке.

**Решение:** Это нормальное поведение. Последующие сборки будут намного быстрее, так как зависимости кэшируются локально. Время загрузки зависит от скорости вашего интернет-соединения.
### Синтаксис команды Maven в PowerShell

**Проблема**: команды Maven завершаются с ошибкой `Unknown lifecycle phase ".mainClass=..."`

**Причина**: PowerShell интерпретирует `=` как оператор присваивания переменной, нарушая синтаксис свойств Maven

**Решение**: используйте оператор остановки парсинга `--%` перед командой Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Оператор `--%` сообщает PowerShell передать все оставшиеся аргументы буквально Maven без интерпретации.

### Отображение эмодзи в Windows PowerShell

**Проблема**: ответы AI показывают некорректные символы (например, `????` или `â??`) вместо эмодзи в PowerShell

**Причина**: кодировка по умолчанию PowerShell не поддерживает эмодзи UTF-8

**Решение**: выполните эту команду перед запуском Java-приложений:
```cmd
chcp 65001
```

Это принудительно задает UTF-8 кодировку в терминале. Альтернативно используйте Windows Terminal с лучшей поддержкой Unicode.

### Отладка вызовов API

**Проблема**: ошибки аутентификации, ограничения по скорости или неожиданные ответы модели AI

**Решение**: в примерах включены `.logRequests(true)` и `.logResponses(true)` для отображения вызовов API в консоли. Это помогает выявлять ошибки аутентификации, ограничения по скорости или неожиданные ответы. Уберите эти параметры в продакшене, чтобы уменьшить шум в логах.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с помощью службы автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, пожалуйста, имейте в виду, что автоматические переводы могут содержать ошибки или неточности. Исходный документ на оригинальном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется обратиться к профессиональному человеческому переводу. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->