# Тестове на приложения LangChain4j

## Съдържание

- [Бърз старт](../../../docs)
- [Какво покриват тестовете](../../../docs)
- [Изпълнение на тестовете](../../../docs)
- [Изпълнение на тестове в VS Code](../../../docs)
- [Тестови модели](../../../docs)
- [Философия на тестване](../../../docs)
- [Следващи стъпки](../../../docs)

Това ръководство ви показва тестовете, които демонстрират как да тествате AI приложения без нужда от API ключове или външни услуги.

## Бърз старт

Стартирайте всички тестове с една команда:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Когато всички тестове преминат, трябва да видите изход като този на екрана по-долу — тестовете стартират без нито една грешка.

<img src="../../../translated_images/bg/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успешно изпълнение на тестове, показващо всички тестове с нулеви грешки*

## Какво покриват тестовете

Този курс се фокусира върху **униктестове**, които се изпълняват локално. Всеки тест демонстрира конкретна концепция на LangChain4j поотделно. Тестовата пирамида по-долу показва къде се вписват униктестовете — те формират бързата, надеждна основа, върху която се градят останалите тестови стратегии.

<img src="../../../translated_images/bg/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Тестова пирамида, показваща баланса между униктестове (бързи, изолирани), интеграционни тестове (реални компоненти) и end-to-end тестове. Това обучение обхваща униктестове.*

| Модул | Тестове | Фокус | Основни файлове |
|--------|-------|-------|-----------|
| **00 - Бърз старт** | 6 | Шаблони на подканите и заместване на променливи | `SimpleQuickStartTest.java` |
| **01 - Въведение** | 8 | Памет за разговори и потребителски чат със състояние | `SimpleConversationTest.java` |
| **02 - Проектиране на подканите** | 12 | Модели GPT-5.2, нива на ентусиазъм, структурирана изходна информация | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Въвеждане на документи, ембединг, търсене по сходство | `DocumentServiceTest.java` |
| **04 - Инструменти** | 12 | Извикване на функции и свързване на инструменти | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол за моделния контекст с stdio транспорт | `SimpleMcpTest.java` |

## Изпълнение на тестовете

**Стартирайте всички тестове от корена:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Стартиране на тестове за конкретен модул:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Или от корена
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Или от корена
mvn --% test -pl 01-introduction
```

**Стартиране само на един тестов клас:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Стартиране само на определен тестов метод:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#трябва да се поддържа история на разговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#трябва да се запази историята на разговора
```

## Изпълнение на тестове в VS Code

Ако използвате Visual Studio Code, Test Explorer осигурява графичен интерфейс за стартиране и дебъгване на тестове.

<img src="../../../translated_images/bg/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer, показващ дървото с всички Java тестови класове и отделните тестови методи*

**За да стартирате тестове в VS Code:**

1. Отворете Test Explorer, като кликнете върху иконата с епруветка в лентата с активности
2. Разгънете тестовото дърво, за да видите всички модули и тестови класове
3. Натиснете бутона за пускане до всеки тест, за да го стартирате поотделно
4. Натиснете "Run All Tests", за да изпълните целия комплект
5. Кликнете с десен бутон върху тест и изберете "Debug Test", за да зададете точки на прекъсване и стъпите през кода

Test Explorer показва зелени отметки за успешни тестове и предоставя подробни съобщения при неуспех.

## Тестови модели

### Модел 1: Тестване на шаблони за подканите

Най-простият модел тества шаблони за подканите без извикване на AI модел. Проверявате дали заместването на променливите работи правилно и дали подканите са форматирани както се очаква.

<img src="../../../translated_images/bg/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестване на шаблони за подканите, показващо процеса на заместване на променливите: шаблон с плейсхолдъри → прилагане на стойности → проверка на форматирания изход*

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

Този тест е в `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Стартирайте го:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#форматиране на тестов шаблон за подкана
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестване на форматирането на шаблона на подканата
```

### Модел 2: Симулиране на езикови модели

При тестване на логиката на разговор използвайте Mockito за създаване на фалшиви модели, които връщат предварително определени отговори. Това прави тестовете бързи, безплатни и детерминирани.

<img src="../../../translated_images/bg/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Сравнение, което показва защо симулациите са предпочитани за тестване: те са бързи, безплатни, детерминирани и не изискват API ключове*

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
        assertThat(history).hasSize(6); // 3 съобщения от потребител + 3 съобщения от изкуствен интелект
    }
}
```

Този модел се използва в `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Симулаторът гарантира последователно поведение, така че да може да проверите дали управлението на паметта работи правилно.

### Модел 3: Тестване на изолацията на разговори

Паметта за разговори трябва да държи потребителите отделно. Този тест проверява, че разговорите не смесват контексти.

<img src="../../../translated_images/bg/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестване на изолацията на разговори, показващо отделни хранилища за памет за различни потребители, за да се предотврати смесването на контексти*

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

Всеки разговор поддържа своя самостоятелна история. В производствени системи тази изолация е критична за приложения с много потребители.

### Модел 4: Тестване на инструменти поотделно

Инструментите са функции, които AI може да извиква. Тествайте ги директно, за да се уверите, че работят правилно независимо от решенията на AI.

<img src="../../../translated_images/bg/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестване на инструменти поотделно, показващо изпълнението на симулирани инструменти без извиквания към AI за проверка на бизнес логиката*

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

Тези тестове от `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` валидират логиката на инструментите без участие на AI. Примерът с веригата показва как изходът на един инструмент се използва като вход за друг.

### Модел 5: Тестване на RAG в паметта

RAG системите традиционно изискват векторни бази данни и embedding услуги. Моделът в паметта ви позволява да тествате цялата конвейерна линия без външни зависимости.

<img src="../../../translated_images/bg/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Техника за тестване на RAG в паметта, показваща парсване на документи, съхранение на ембединг и търсене по сходство без необходимост от база данни*

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

Този тест от `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` създава документ в паметта и проверява раздробяването му и обработката на метаданни.

### Модел 6: Интеграционно тестване на MCP

MCP модулът тества интеграцията на Model Context Protocol с използване на stdio транспорт. Тези тестове проверяват дали вашето приложение може да стартира и комуникира с MCP сървъри като подпроцеси.

Тестовете в `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` валидират поведението на MCP клиента.

**Стартирайте ги:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Философия на тестване

Тествайте вашия код, а не AI-то. Вашите тестове трябва да валидират кода, който пишете, чрез проверка как са построени подканите, как се управлява паметта и как се изпълняват инструментите. Отговорите на AI варират и не трябва да са част от твърденията на тестовете. Запитайте се дали вашият шаблон за подканата правилно замества променливите, а не дали AI-то дава правилен отговор.

Използвайте симулатори за езикови модели. Те са външни зависимости, които са бавни, скъпи и недетерминирани. Симулирането прави тестовете бързи с милисекунди вместо секунди, безплатни с нулеви API разходи и детерминирани с един и същ резултат всеки път.

Дръжте тестовете независими. Всеки тест трябва да създава собствените си данни, да не разчита на други тестове и да се почиства след себе си. Тестовете трябва да преминават независимо от реда на изпълнение.

Тествайте краените случаи извън щастливия път. Опитайте с празни входове, много големи входове, специални символи, невалидни параметри и гранични условия. Те често разкриват бъгове, които не се появяват при нормално ползване.

Използвайте описателни имена. Сравнете `shouldMaintainConversationHistoryAcrossMultipleMessages()` с `test1()`. Първото казва точно какво се тества, което улеснява намирането на грешки.

## Следващи стъпки

Сега, когато разбирате тестовите модели, потопете се по-дълбоко във всеки модул:

- **[00 - Бърз старт](../00-quick-start/README.md)** - Започнете с основите на шаблоните за подканите
- **[01 - Въведение](../01-introduction/README.md)** - Научете управлението на паметта за разговори
- **[02 - Проектиране на подканите](../02/prompt-engineering/README.md)** - Овладейте моделите за подканите на GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Изградете системи за генерация с повишаване чрез извличане
- **[04 - Инструменти](../04-tools/README.md)** - Имплементирайте извикване на функции и вериги от инструменти
- **[05 - MCP](../05-mcp/README.md)** - Интегрирайте Model Context Protocol

README файловете на всеки модул съдържат подробни обяснения на концепциите, тествани тук.

---

**Навигация:** [← Обратно към главната](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:  
Този документ е преведен с помощта на AI преводаческа услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за никакви недоразумения или погрешни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->