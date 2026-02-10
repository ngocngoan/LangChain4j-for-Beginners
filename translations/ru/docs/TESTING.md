# Тестирование приложений LangChain4j

## Содержание

- [Быстрый старт](../../../docs)
- [Что охватывают тесты](../../../docs)
- [Запуск тестов](../../../docs)
- [Запуск тестов в VS Code](../../../docs)
- [Шаблоны тестирования](../../../docs)
- [Философия тестирования](../../../docs)
- [Следующие шаги](../../../docs)

Это руководство проведет вас через тесты, демонстрирующие, как тестировать AI-приложения без необходимости API-ключей или внешних сервисов.

## Быстрый старт

Запустите все тесты одной командой:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ru/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успешное выполнение тестов с прохождением всех тестов без ошибок*

## Что охватывают тесты

Этот курс сосредоточен на **юнит-тестах**, которые запускаются локально. Каждый тест демонстрирует конкретную концепцию LangChain4j в изоляции.

<img src="../../../translated_images/ru/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Пирамида тестирования, показывающая баланс между юнит-тестами (быстрыми, изолированными), интеграционными тестами (с реальными компонентами) и end-to-end тестами. В этом обучении рассматривается юнит-тестирование.*

| Модуль | Тесты | Фокус | Основные файлы |
|--------|-------|-------|-----------|
| **00 - Быстрый старт** | 6 | Шаблоны запросов и подстановка переменных | `SimpleQuickStartTest.java` |
| **01 - Введение** | 8 | Память беседы и состояние чата | `SimpleConversationTest.java` |
| **02 - Разработка запросов** | 12 | Шаблоны GPT-5.2, уровни готовности, структурированный вывод | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ингестия документов, эмбеддинги, поиск по схожести | `DocumentServiceTest.java` |
| **04 - Инструменты** | 12 | Вызов функций и цепочка инструментов | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол контекста модели с транспортом stdio | `SimpleMcpTest.java` |

## Запуск тестов

**Запустите все тесты из корня:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Запуск тестов для конкретного модуля:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Или от root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Или от корня
mvn --% test -pl 01-introduction
```

**Запуск отдельного класса тестов:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Запуск конкретного метода теста:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#следуетСохранятьИсториюРазговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#следуетСохранятьИсториюРазговора
```

## Запуск тестов в VS Code

Если вы используете Visual Studio Code, обозреватель тестов предоставляет графический интерфейс для запуска и отладки тестов.

<img src="../../../translated_images/ru/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Обозреватель тестов VS Code, показывающий дерево тестов со всеми классами Java и отдельными методами тестов*

**Чтобы запустить тесты в VS Code:**

1. Откройте обозреватель тестов, нажав на иконку колбы на панели активности.
2. Разверните дерево тестов, чтобы увидеть все модули и классы тестов.
3. Нажмите кнопку воспроизведения рядом с любым тестом, чтобы запустить его отдельно.
4. Нажмите "Run All Tests", чтобы выполнить весь набор.
5. Щелкните правой кнопкой мыши по тесту и выберите "Debug Test" для установки точек останова и поэтапного выполнения кода.

Обозреватель тестов отображает зеленые галочки для успешного прохождения и предоставляет подробные сообщения об ошибках при сбоях.

## Шаблоны тестирования

### Шаблон 1: Тестирование шаблонов запросов

Самый простой шаблон тестирует шаблоны запросов без вызова AI-модели. Вы проверяете, что подстановка переменных работает правильно, и запросы форматируются как ожидалось.

<img src="../../../translated_images/ru/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестирование шаблонов запросов, показывающее процесс подстановки переменных: шаблон с заполнителями → примененные значения → проверенный форматированный вывод*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Этот тест находится в `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Запуск:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#тестированиеФорматированияШаблонаПодсказки
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестированиеФорматированияШаблонаПодсказки
```

### Шаблон 2: Мокирование языковых моделей

При тестировании логики беседы используйте Mockito для создания фиктивных моделей, возвращающих заранее определенные ответы. Это делает тесты быстрыми, бесплатными и детерминированными.

<img src="../../../translated_images/ru/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Сравнение, показывающее, почему для тестирования предпочтительнее моки: они быстрые, бесплатные, детерминированные и не требуют API-ключей*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 сообщения от пользователя + 3 сообщения от ИИ
    }
}
```

Этот шаблон используется в `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок гарантирует согласованное поведение, чтобы вы могли проверить корректность управления памятью.

### Шаблон 3: Тестирование изоляции бесед

Память беседы должна хранить пользователей отдельно. Этот тест проверяет, что контексты бесед не смешиваются.

<img src="../../../translated_images/ru/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестирование изоляции бесед, показывающее отдельные хранилища памяти для разных пользователей, чтобы предотвратить смешение контекстов*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Каждая беседа поддерживает свою независимую историю. В производственных системах такая изоляция критична для многопользовательских приложений.

### Шаблон 4: Тестирование инструментов независимо

Инструменты — это функции, которые может вызывать AI. Тестируйте их напрямую, чтобы гарантировать правильную работу независимо от решений AI.

<img src="../../../translated_images/ru/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестирование инструментов отдельно, показывающее выполнение мок-инструментов без вызовов AI для проверки бизнес-логики*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Эти тесты из `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` проверяют логику инструментов без участия AI. Пример цепочки показывает, как вывод одного инструмента поступает на вход другому.

### Шаблон 5: Тестирование RAG в памяти

Системы RAG традиционно требуют векторных баз данных и сервисов эмбеддингов. Паттерн в памяти позволяет тестировать весь процесс без внешних зависимостей.

<img src="../../../translated_images/ru/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Рабочий процесс тестирования RAG в памяти: разбор документа, сохранение эмбеддингов и поиск по схожести без необходимости базы данных*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Этот тест из `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` создает документ в памяти и проверяет разбиение на части и обработку метаданных.

### Шаблон 6: Интеграционное тестирование MCP

Модуль MCP тестирует интеграцию Протокола Контекста Модели с транспортом stdio. Эти тесты проверяют, что ваше приложение может запускать и общаться с MCP-серверами как с подпроцессами.

Тесты находятся в `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` и проверяют поведение MCP-клиента.

**Запуск:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Философия тестирования

Тестируйте свой код, а не AI. Ваши тесты должны проверять написанный вами код, контролируя, как строятся запросы, как управляется память и как работают инструменты. Ответы AI варьируются и не должны быть частью утверждений тестов. Спрашивайте себя, правильно ли шаблон запроса подставляет переменные, а не верен ли ответ AI.

Используйте моки для языковых моделей. Это внешние зависимости, которые медленные, дорогие и недетерминированные. Мокирование делает тесты быстрыми (миллисекунды вместо секунд), бесплатными (без затрат на API) и детерминированными (одинаковый результат каждый раз).

Держите тесты независимыми. Каждый тест должен настраивать свои данные, не зависеть от других тестов и очищать их после выполнения. Тесты должны проходить независимо от порядка запуска.

Тестируйте граничные случаи, выходящие за пределы стандартного сценария. Пробуйте пустые входные данные, очень большие данные, специальные символы, некорректные параметры и граничные условия. Они часто обнаруживают ошибки, которые обычное использование не выявляет.

Используйте описательные имена. Сравните `shouldMaintainConversationHistoryAcrossMultipleMessages()` с `test1()`. Первое название ясно сообщает, что именно тестируется, что облегчает отладку ошибок.

## Следующие шаги

Теперь, когда вы понимаете шаблоны тестирования, изучайте каждый модуль подробнее:

- **[00 - Быстрый старт](../00-quick-start/README.md)** - Начинайте с основ шаблонов запросов
- **[01 - Введение](../01-introduction/README.md)** - Изучайте управление памятью бесед
- **[02 - Разработка запросов](../02/prompt-engineering/README.md)** - Освойте шаблоны GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Создавайте системы генерации с поддержкой поиска
- **[04 - Инструменты](../04-tools/README.md)** - Реализуйте вызовы функций и цепочки инструментов
- **[05 - MCP](../05-mcp/README.md)** - Интегрируйте Протокол Контекста Модели

README каждого модуля подробно объясняет концепции, протестированные здесь.

---

**Навигация:** [← Назад к главному](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:
Этот документ был переведен с помощью AI-сервиса перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, просим учитывать, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его родном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется обратиться к профессиональному человеческому переводу. Мы не несем ответственности за любые недоразумения или неверные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->