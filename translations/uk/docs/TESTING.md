# Тестування додатків LangChain4j

## Зміст

- [Швидкий старт](../../../docs)
- [Що охоплюють тести](../../../docs)
- [Запуск тестів](../../../docs)
- [Запуск тестів у VS Code](../../../docs)
- [Шаблони тестування](../../../docs)
- [Філософія тестування](../../../docs)
- [Наступні кроки](../../../docs)

Цей посібник проведе вас через тести, які демонструють, як тестувати AI-додатки без необхідності у ключах API чи зовнішніх сервісах.

## Швидкий старт

Запустіть усі тести однією командою:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/uk/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успішне виконання тестів із усіма тестами, що пройшли без помилок*

## Що охоплюють тести

Цей курс зосереджений на **unit тестах**, які запускаються локально. Кожен тест демонструє конкретну концепцію LangChain4j у ізоляції.

<img src="../../../translated_images/uk/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Піраміда тестування, що показує баланс між unit тестами (швидкі, ізольовані), інтеграційними тестами (реальні компоненти) та end-to-end тестами. Це навчання охоплює unit тестування.*

| Модуль | Тести | Фокус | Ключові файли |
|--------|-------|-------|---------------|
| **00 - Швидкий старт** | 6 | Шаблони запитів та заміна змінних | `SimpleQuickStartTest.java` |
| **01 - Вступ** | 8 | Пам’ять розмов та станова чат-сесія | `SimpleConversationTest.java` |
| **02 - Проєктування запитів** | 12 | Шаблони GPT-5.2, рівні охоплення, структурований вивід | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Обробка документів, вбудовування, пошук схожості | `DocumentServiceTest.java` |
| **04 - Інструменти** | 12 | Виклик функцій та ланцюжки інструментів | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол контексту моделі зі stdio транспортом | `SimpleMcpTest.java` |

## Запуск тестів

**Запустіть усі тести з кореня проєкту:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Запустіть тести для конкретного модуля:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Або з кореня
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Або з кореня
mvn --% test -pl 01-introduction
```

**Запустіть один клас тестів:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Запустіть конкретний метод тесту:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#слід зберігати історію розмов
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#слід зберігати історію розмови
```

## Запуск тестів у VS Code

Якщо ви використовуєте Visual Studio Code, Test Explorer надає графічний інтерфейс для запуску і налагодження тестів.

<img src="../../../translated_images/uk/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer показує дерево тестів із усіма Java класами тестів та окремими методами*

**Щоб запустити тести у VS Code:**

1. Відкрийте Test Explorer, натиснувши іконку пробірки в Activity Bar
2. Розгорніть дерево тестів, щоб побачити всі модулі і класи тестів
3. Натисніть кнопку відтворення поруч з будь-яким тестом, щоб запустити його окремо
4. Натисніть "Run All Tests" для запуску всього набору
5. Клацніть правою кнопкою на будь-якому тесті та виберіть "Debug Test" для встановлення точок зупину та покрокового виконання коду

Test Explorer показує зелені галочки для пройдених тестів та надає детальні повідомлення про помилки при проваленнях.

## Шаблони тестування

### Шаблон 1: Тестування шаблонів запитів

Найпростіший шаблон тестує шаблони запитів без виклику будь-якої AI-моделі. Ви перевіряєте, що заміна змінних працює правильно, а запити відформатовані як очікується.

<img src="../../../translated_images/uk/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестування шаблонів запитів, що показує потік заміни змінних: шаблон із заповнювачами → застосовані значення → перевірений відформатований вивід*

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

Цей тест розташований у `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Запустіть його:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#форматуванняШаблонуЗапитуТесту
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#перевіркаФорматуванняШаблонуЗапиту
```

### Шаблон 2: Мокінг мовних моделей

При тестуванні логіки розмови використовуйте Mockito для створення фейкових моделей, що повертають заздалегідь визначені відповіді. Це робить тести швидкими, безкоштовними і детермінованими.

<img src="../../../translated_images/uk/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Порівняння, що показує, чому мок-об’єкти кращі для тестування: вони швидкі, безкоштовні, детерміновані і не потребують ключів API*

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
        assertThat(history).hasSize(6); // 3 повідомлення користувача + 3 повідомлення ШІ
    }
}
```

Цей шаблон представлений у `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок забезпечує послідовну поведінку, тож ви можете перевірити правильність керування пам’яттю.

### Шаблон 3: Тестування ізоляції розмов

Пам’ять розмови має зберігати різних користувачів окремо. Цей тест перевіряє, що розмови не змішують контексти.

<img src="../../../translated_images/uk/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестування ізоляції розмов, що показує окремі сховища пам’яті для різних користувачів, щоб запобігти змішуванню контекстів*

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

Кожна розмова підтримує власну незалежну історію. В продуктивних системах ця ізоляція є критичною для багатокористувацьких додатків.

### Шаблон 4: Тестування інструментів окремо

Інструменти — це функції, які AI може викликати. Тестуйте їх напряму, щоб переконатися, що вони працюють правильно незалежно від рішень AI.

<img src="../../../translated_images/uk/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестування інструментів окремо, що показує виконання мок-інструментів без викликів AI для перевірки бізнес-логіки*

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

Ці тести з `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` перевіряють логіку інструментів без залучення AI. Приклад ланцюжка показує, як вихід одного інструменту подається на вхід іншого.

### Шаблон 5: Тестування RAG в пам’яті

Системи RAG традиційно вимагають векторних баз даних та сервісів вбудовування. Шаблон роботи в пам’яті дозволяє протестувати весь конвеєр без зовнішніх залежностей.

<img src="../../../translated_images/uk/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Процес тестування RAG в пам’яті, що показує розбір документів, збереження вбудовувань та пошук схожості без потреби в базі даних*

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

Цей тест із `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` створює документ в пам’яті та перевіряє розбивку на частини й обробку метаданих.

### Шаблон 6: Інтеграційне тестування MCP

Модуль MCP тестує інтеграцію Model Context Protocol, використовуючи stdio транспорт. Ці тести перевіряють, що ваш додаток може запускати і комунікувати з MCP-серверами як підпроцесами.

Тести в `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` перевіряють поведінку MCP клієнта.

**Запустіть їх:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Філософія тестування

Тестуйте свій код, а не AI. Ваші тести мають перевіряти код, який ви пишете, контролюючи, як формуються запити, як керується пам’ять і як виконуються інструменти. Відповіді AI змінюються і не повинні бути частиною перевірок у тестах. Запитуйте себе, чи шаблон запиту коректно замінює змінні, а не чи дає AI правильну відповідь.

Використовуйте моки для мовних моделей. Вони є зовнішніми залежностями, які повільні, дорогі та недетерміновані. Мокінг робить тести швидкими — у мілісекундах замість секунд, безкоштовними — без витрат API, і детермінованими — з однаковим результатом щоразу.

Тримайте тести незалежними. Кожен тест повинен ініціалізувати власні дані, не залежати від інших тестів і прибирати за собою. Тести мають проходити незалежно від порядку запуску.

Тестуйте крайні випадки поза "щасливим шляхом". Спробуйте порожні вхідні дані, дуже великі дані, спеціальні символи, недійсні параметри та граничні умови. Вони часто виявляють помилки, які звичайне використання не виявляє.

Використовуйте описові імена. Порівняйте `shouldMaintainConversationHistoryAcrossMultipleMessages()` з `test1()`. Перше точно каже, що тестується, полегшуючи відлагодження збоїв.

## Наступні кроки

Тепер, коли ви розумієте шаблони тестування, заглибтесь в кожен модуль:

- **[00 - Швидкий старт](../00-quick-start/README.md)** — Почніть із основ шаблонів запитів
- **[01 - Вступ](../01-introduction/README.md)** — Вивчіть управління пам’яттю розмов
- **[02 - Проєктування запитів](../02/prompt-engineering/README.md)** — Опануйте шаблони запитів GPT-5.2
- **[03 - RAG](../03-rag/README.md)** — Створюйте системи з розширеним пошуком
- **[04 - Інструменти](../04-tools/README.md)** — Реалізуйте виклик функцій і ланцюжки інструментів
- **[05 - MCP](../05-mcp/README.md)** — Інтеграція Model Context Protocol

README кожного модуля містить детальні пояснення концепцій, протестованих тут.

---

**Навігація:** [← Назад до головної](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:  
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоч ми і прагнемо до точності, будь ласка, врахуйте, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ його рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що можуть виникнути внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->