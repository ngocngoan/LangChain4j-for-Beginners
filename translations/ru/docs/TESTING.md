# Тестирование приложений LangChain4j

## Содержание

- [Быстрый старт](../../../docs)
- [Что покрывают тесты](../../../docs)
- [Запуск тестов](../../../docs)
- [Запуск тестов в VS Code](../../../docs)
- [Паттерны тестирования](../../../docs)
- [Философия тестирования](../../../docs)
- [Следующие шаги](../../../docs)

Это руководство проведет вас через тесты, демонстрирующие, как тестировать AI-приложения без необходимости использования API ключей или внешних сервисов.

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

Когда все тесты пройдут успешно, вы увидите вывод, похожий на скриншот ниже — тесты выполнены без ошибок.

<img src="../../../translated_images/ru/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успешное выполнение тестов со всеми пройденными тестами без сбоев*

## Что покрывают тесты

Этот курс фокусируется на **модульных тестах**, которые выполняются локально. Каждый тест демонстрирует конкретную концепцию LangChain4j в изоляции. Ниже приведена пирамида тестирования, показывающая, где размещаются модульные тесты — они образуют быстрый, надежный фундамент, на котором строится остальная стратегия тестирования.

<img src="../../../translated_images/ru/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Пирамида тестирования, показывающая баланс между модульными тестами (быстрые, изолированные), интеграционными тестами (реальные компоненты) и сквозными тестами. Это обучение охватывает модульное тестирование.*

| Модуль | Тесты | Фокус | Ключевые файлы |
|--------|-------|-------|----------------|
| **00 - Быстрый старт** | 6 | Шаблоны подсказок и подстановка переменных | `SimpleQuickStartTest.java` |
| **01 - Введение** | 8 | Память диалогов и состояние чата | `SimpleConversationTest.java` |
| **02 - Инженерия подсказок** | 12 | Паттерны GPT-5.2, уровни готовности, структурированный вывод | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ингестия документов, эмбеддинги, поиск по похожести | `DocumentServiceTest.java` |
| **04 - Инструменты** | 12 | Вызовы функций и цепочки инструментов | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол Model Context с передачей через stdio | `SimpleMcpTest.java` |

## Запуск тестов

**Запуск всех тестов из корня:**

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
# Или от корня
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Или от корня
mvn --% test -pl 01-introduction
```

**Запуск одного класса тестов:**

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
mvn test -Dtest=SimpleConversationTest#необходимо сохранять историю разговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#следуетСохранятьИсториюРазговора
```

## Запуск тестов в VS Code

Если вы используете Visual Studio Code, Test Explorer предоставляет графический интерфейс для запуска и отладки тестов.

<img src="../../../translated_images/ru/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer в VS Code с деревом тестов, показывающим все Java-классы тестов и отдельные тестовые методы*

**Чтобы запустить тесты в VS Code:**

1. Откройте Test Explorer, нажав на иконку с колбой в панели активности
2. Разверните дерево тестов, чтобы увидеть все модули и классы тестов
3. Нажмите кнопку воспроизведения рядом с любым тестом, чтобы запустить его отдельно
4. Нажмите "Run All Tests" для запуска всего набора
5. Кликните правой кнопкой по любому тесту и выберите "Debug Test" для установки точек останова и пошагового выполнения кода

Test Explorer показывает зеленые галочки для прошедших тестов и предоставляет подробные сообщения об ошибках при неудачном прохождении.

## Паттерны тестирования

### Паттерн 1: Тестирование шаблонов подсказок

Самый простой паттерн тестирует шаблоны подсказок без вызова какой-либо AI-модели. Вы проверяете, что подстановка переменных работает правильно, и подсказки отформатированы как ожидалось.

<img src="../../../translated_images/ru/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестирование шаблонов подсказок, показывающее поток подстановки переменных: шаблон с заполнителями → примененные значения → проверенный форматированный вывод*

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

**Запустите его:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#форматированиеШаблонаТеста
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестированиеФорматированияШаблонаПодсказки
```

### Паттерн 2: Мокинг языковых моделей

При тестировании логики разговоров используйте Mockito для создания поддельных моделей, которые возвращают заранее определённые ответы. Это делает тесты быстрыми, бесплатными и детерминированными.

<img src="../../../translated_images/ru/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Сравнение, показывающее, почему моки предпочтительнее для тестирования: они быстрые, бесплатные, детерминированные и не требуют API ключей*

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
        assertThat(history).hasSize(6); // 3 сообщения пользователя + 3 сообщения ИИ
    }
}
```

Этот паттерн используется в `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок обеспечивает согласованное поведение, чтобы вы могли проверить правильность управления памятью.

### Паттерн 3: Тестирование изоляции разговоров

Память разговоров должна хранить данные для нескольких пользователей раздельно. Этот тест проверяет, что разговоры не смешивают контексты.

<img src="../../../translated_images/ru/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестирование изоляции разговоров с раздельными хранилищами памяти для разных пользователей, чтобы избежать смешения контекста*

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

Каждый разговор ведет свою независимую историю. В производственных системах такая изоляция критична для приложений с несколькими пользователями.

### Паттерн 4: Тестирование инструментов независимо

Инструменты — это функции, которые AI может вызывать. Тестируйте их напрямую, чтобы убедиться, что они работают корректно независимо от решений AI.

<img src="../../../translated_images/ru/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестирование инструментов самостоятельно, показывающее выполнение поддельных инструментов без вызовов AI для проверки бизнес-логики*

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

Эти тесты из `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` проверяют логику инструментов без участия AI. Пример цепочки показывает, как вывод одного инструмента подаётся на вход другому.

### Паттерн 5: Тестирование RAG в памяти

Системы RAG традиционно требуют векторных баз данных и сервисов эмбеддингов. Паттерн в памяти позволяет тестировать весь конвейер без внешних зависимостей.

<img src="../../../translated_images/ru/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Рабочий процесс тестирования RAG в памяти, включающий разбор документа, хранение эмбеддингов и поиск по похожести без необходимости базы данных*

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

### Паттерн 6: Интеграционные тесты MCP

Модуль MCP тестирует интеграцию протокола Model Context с использованием передачи через stdio. Эти тесты проверяют, что ваше приложение может запускать и взаимодействовать с MCP-серверами как подпроцессами.

Тесты в `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` проверяют поведение клиента MCP.

**Запустите их:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Философия тестирования

Тестируйте свой код, а не AI. Ваши тесты должны проверять код, который вы пишете, проверяя, как строятся подсказки, как управляется память и как выполняются инструменты. Ответы AI вариативны и не должны быть частью утверждений теста. Спрашивайте себя, правильно ли ваш шаблон подсказки подставляет переменные, а не дает ли AI правильный ответ.

Используйте моки для языковых моделей. Это внешние зависимости, которые медленные, дорогие и недетерминированные. Мокинг делает тесты быстрыми — миллисекунды вместо секунд, бесплатными — без затрат на API, и детерминированными — один и тот же результат каждый раз.

Держите тесты независимыми. Каждый тест должен инициализировать свои данные, не зависеть от других тестов и убирать за собой. Тесты должны проходить независимо от порядка выполнения.

Тестируйте краевые случаи, выходящие за рамки основного сценария. Пробуйте пустые вводы, очень большие вводы, специальные символы, неверные параметры и граничные условия. Они часто выявляют ошибки, которые в нормальном использовании не проявляются.

Используйте описательные имена. Сравните `shouldMaintainConversationHistoryAcrossMultipleMessages()` с `test1()`. Первое точно говорит, что тестируется, что облегчает отладку при сбоях.

## Следующие шаги

Теперь, когда вы понимаете паттерны тестирования, изучите каждый модуль подробнее:

- **[00 - Быстрый старт](../00-quick-start/README.md)** - Начните с основ шаблонов подсказок
- **[01 - Введение](../01-introduction/README.md)** - Изучите управление памятью разговоров
- **[02 - Инженерия подсказок](../02/prompt-engineering/README.md)** - Освойте паттерны подсказок GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Создайте системы дополненной генерации с поиском
- **[04 - Инструменты](../04-tools/README.md)** - Реализуйте вызовы функций и цепочки инструментов
- **[05 - MCP](../05-mcp/README.md)** - Интегрируйте Model Context Protocol

README каждого модуля содержит подробные объяснения рассмотренных здесь концепций.

---

**Навигация:** [← Назад к началу](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса машинного перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, просим учитывать, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для критически важной информации рекомендуется воспользоваться услугами профессионального перевода. Мы не несем ответственности за любые недоразумения или неверные толкования, вызванные использованием данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->