# Тестування додатків LangChain4j

## Зміст

- [Швидкий старт](../../../docs)
- [Що охоплюють тести](../../../docs)
- [Запуск тестів](../../../docs)
- [Запуск тестів у VS Code](../../../docs)
- [Шаблони тестування](../../../docs)
- [Філософія тестування](../../../docs)
- [Наступні кроки](../../../docs)

Цей посібник проведе вас через тести, які демонструють, як тестувати AI-додатки без необхідності ключів API чи зовнішніх сервісів.

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

Коли всі тести проходять, ви побачите вивід, як на скриншоті нижче — тести виконуються без жодних помилок.

<img src="../../../translated_images/uk/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успішне виконання тестів, що показує проходження всіх тестів без помилок*

## Що охоплюють тести

Цей курс зосереджений на **блокових тестах**, які запускаються локально. Кожен тест демонструє окрему концепцію LangChain4j. Нижче показано тестову піраміду — саме блокові тести утворюють швидку, надійну основу, на якій будується ваша тестова стратегія.

<img src="../../../translated_images/uk/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Тестова піраміда, що показує баланс між блоковими тестами (швидкі, ізольовані), інтеграційними тестами (реальні компоненти) і end-to-end тестами. Це навчання охоплює саме блокове тестування.*

| Модуль | Тести | Фокус | Ключові файли |
|--------|-------|-------|---------------|
| **00 - Швидкий старт** | 6 | Шаблони підказок та заміщення змінних | `SimpleQuickStartTest.java` |
| **01 - Вступ** | 8 | Пам'ять розмови та стан чату | `SimpleConversationTest.java` |
| **02 - Інженерія підказок** | 12 | Патерни GPT-5.2, рівні готовності, структурований вивід | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Інгестування документів, ембеддинги, пошук за схожістю | `DocumentServiceTest.java` |
| **04 - Інструменти** | 12 | Виклики функцій і ланцюжки інструментів | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол Model Context з транспортом stdio | `SimpleMcpTest.java` |

## Запуск тестів

**Запустіть усі тести з кореня:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Запуск тестів для конкретного модуля:**

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

**Запуск одного класу тестів:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Запуск конкретного тестового методу:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#слідЗберігатиІсторіюРозмови
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#слід зберігати історію розмови
```

## Запуск тестів у VS Code

Якщо ви користуєтеся Visual Studio Code, Test Explorer пропонує графічний інтерфейс для запуску та налагодження тестів.

<img src="../../../translated_images/uk/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer у VS Code, що показує дерево тестів із усіма Java класами та окремими тестовими методами*

**Щоб запускати тести у VS Code:**

1. Відкрийте Test Explorer, натиснувши іконку колби на панелі активності
2. Розгорніть дерево тестів, щоб побачити всі модулі та класи тестів
3. Натисніть кнопку відтворення поруч із будь-яким тестом, щоб запустити його окремо
4. Натисніть «Run All Tests», щоб виконати весь набір
5. Клацніть правою кнопкою миші по тесту та виберіть «Debug Test», щоб встановити брекпойнти та проходити крок за кроком

Test Explorer показує зелені позначки для пройдених тестів і надає докладні повідомлення про помилки, якщо тести не проходять.

## Шаблони тестування

### Шаблон 1: Тестування шаблонів підказок

Найпростіший шаблон тестує шаблони підказок без виклику будь-якої AI-моделі. Ви перевіряєте, що заміщення змінних працює правильно, а підказки форматуються очікувано.

<img src="../../../translated_images/uk/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестування шаблонів підказок із потоком заміщення змінних: шаблон із заповнювачами → застосовані значення → перевірений форматований вивід*

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

Цей тест знаходиться у `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Запустіть його:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#тестуванняФорматуванняШаблонуПідказки
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестуванняФорматуванняШаблонуПідказки
```

### Шаблон 2: Мокінг мовних моделей

При тестуванні логіки розмови використовуйте Mockito для створення підроблених моделей, які повертають заздалегідь визначені відповіді. Це робить тести швидкими, безкоштовними і детермінованими.

<img src="../../../translated_images/uk/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Порівняння, що показує, чому для тестування переважно використовувати моки: вони швидкі, безкоштовні, детерміновані та не потребують ключів API*

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
        assertThat(history).hasSize(6); // 3 повідомлення від користувача + 3 повідомлення від ШІ
    }
}
```

Цей шаблон застосовується в `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок гарантує послідовну поведінку, щоб ви могли перевірити коректність управління пам’яттю.

### Шаблон 3: Тестування ізоляції розмов

Пам’ять розмови має розділяти користувачів. Цей тест перевіряє, що контексти розмов не змішуються.

<img src="../../../translated_images/uk/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестування ізоляції розмов, що показує окремі сховища пам’яті для різних користувачів щоб запобігти змішуванню контекстів*

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

Кожна розмова підтримує свою власну незалежну історію. В продакшн-системах ця ізоляція критична для багатокористувацьких застосунків.

### Шаблон 4: Тестування інструментів окремо

Інструменти — це функції, які може викликати AI. Тестуйте їх напряму, щоб переконатися, що вони працюють правильно незалежно від рішень AI.

<img src="../../../translated_images/uk/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестування інструментів окремо, показуючи мок-інструмент, що виконується без викликів AI для перевірки бізнес-логіки*

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

Ці тести з `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` перевіряють логіку інструментів без участі AI. Приклад ланцюжка показує, як вихід одного інструменту передається як вхід іншому.

### Шаблон 5: Тестування RAG в пам’яті

Системи RAG зазвичай потребують векторних баз даних та сервісів ембеддингів. Візерунок “в пам’яті” дає змогу тестувати весь конвеєр без зовнішніх залежностей.

<img src="../../../translated_images/uk/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Робочий процес тестування RAG в пам’яті, що показує парсинг документів, зберігання ембеддингів і пошук за схожістю без потреби у базі даних*

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

Цей тест з `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` створює документ в пам’яті та перевіряє нарізку на частини і обробку метаданих.

### Шаблон 6: Інтеграційне тестування MCP

Модуль MCP тестує інтеграцію Model Context Protocol з транспортом stdio. Ці тести перевіряють, чи ваш додаток може запускати і взаємодіяти з MCP-серверами як підпроцесами.

Тести в `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` перевіряють поведінку клієнта MCP.

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

Тестуйте ваш код, а не AI. Ваші тести повинні перевіряти код, що ви пишете, контролюючи, як будуються підказки, як керується пам’ять і як виконуються інструменти. Відповіді AI варіюються і не повинні бути частиною тверджень тестів. Запитуйте себе, чи правильно ваш шаблон підказки заміщує змінні, а не чи дає AI правильну відповідь.

Використовуйте моки для мовних моделей. Це зовнішні залежності, які працюють повільно, дорого і недетерміновано. Мокінг робить тести швидкими (мілісекунди замість секунд), безкоштовними (без витрат на API) та детермінованими (однаковий результат кожного разу).

Підтримуйте тести незалежними. Кожен тест повинен налаштовувати власні дані, не покладатися на інші тести і прибирати за собою. Тести мають проходити незалежно від порядку виконання.

Тестуйте крайні випадки, а не лише успішні шляхи. Спробуйте пусті вхідні дані, дуже великі вхідні дані, спеціальні символи, недійсні параметри та граничні умови. Вони часто виявляють баги, які при нормальному використанні не проявляються.

Використовуйте описові назви. Порівняйте `shouldMaintainConversationHistoryAcrossMultipleMessages()` і `test1()`. Перша точно повідомляє, що тестується, що значно полегшує налагодження помилок.

## Наступні кроки

Тепер, коли ви розумієте шаблони тестування, зануртесь глибше в кожен модуль:

- **[00 - Швидкий старт](../00-quick-start/README.md)** — Почніть із основ шаблонів підказок
- **[01 - Вступ](../01-introduction/README.md)** — Вивчіть управління пам’яттю розмов
- **[02 - Інженерія підказок](../02/prompt-engineering/README.md)** — Опануйте патерни GPT-5.2
- **[03 - RAG](../03-rag/README.md)** — Створюйте системи звернення з доповненням
- **[04 - Інструменти](../04-tools/README.md)** — Реалізуйте виклики функцій і ланцюжки інструментів
- **[05 - MCP](../05-mcp/README.md)** — Інтегруйте Model Context Protocol

README кожного модуля надає докладне пояснення концепцій, що тестуються тут.

---

**Навігація:** [← Назад до головної](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:  
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичний переклад може містити помилки чи неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критичної інформації рекомендується звертатися до професійного людського перекладу. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->