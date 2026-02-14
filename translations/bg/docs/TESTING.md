# Тестване на LangChain4j приложения

## Съдържание

- [Бърз старт](../../../docs)
- [Какво покриват тестовете](../../../docs)
- [Стартиране на тестовете](../../../docs)
- [Стартиране на тестове във VS Code](../../../docs)
- [Шаблони за тестване](../../../docs)
- [Философия на тестването](../../../docs)
- [Следващи стъпки](../../../docs)

Това ръководство ви превежда през тестовете, които демонстрират как да тествате AI приложения без необходимост от API ключове или външни услуги.

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

<img src="../../../translated_images/bg/test-results.ea5c98d8f3642043.webp" alt="Успешни резултати от тестове" width="800"/>

*Успешно изпълнение на тестове, показващо всички тестове с нулев брой грешки*

## Какво покриват тестовете

Този курс се фокусира върху **юнит тестове**, които се изпълняват локално. Всеки тест демонстрира конкретна концепция на LangChain4j изолирано.

<img src="../../../translated_images/bg/testing-pyramid.2dd1079a0481e53e.webp" alt="Пирамида на тестването" width="800"/>

*Пирамида на тестването, показваща баланса между юнит тестове (бързи, изолирани), интеграционни тестове (реални компоненти) и крайни тестове. Това обучение покрива юнит тестване.*

| Модул | Тестове | Фокус | Ключови файлове |
|--------|-------|-------|-----------|
| **00 - Бърз старт** | 6 | Шаблони за подканване и подмяна на променливи | `SimpleQuickStartTest.java` |
| **01 - Увод** | 8 | Памет за разговор и състояние на чат | `SimpleConversationTest.java` |
| **02 - Проектиране на промпти** | 12 | GPT-5.2 шаблони, нива на готовност, структурирана продукция | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Въвеждане на документи, вграждания, търсене по сходство | `DocumentServiceTest.java` |
| **04 - Инструменти** | 12 | Извикване на функции и свързване на инструменти | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол за контекст на модел с Stdio транспорт | `SimpleMcpTest.java` |

## Стартиране на тестовете

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

**Стартиране на един тест клас:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Стартиране на конкретен тест метод:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#трябва да се поддържа история на разговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#трябва да се поддържа история на разговора
```

## Стартиране на тестове във VS Code

Ако използвате Visual Studio Code, Test Explorer предоставя графичен интерфейс за стартиране и отстраняване на грешки на тестове.

<img src="../../../translated_images/bg/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer показва дървото на тестовете с всички Java тестови класове и отделни тестови методи*

**За да стартирате тестове във VS Code:**

1. Отворете Test Explorer чрез иконата на епруветка в лентата с активности
2. Разгънете дървото на тестовете, за да видите всички модули и тестови класове
3. Кликнете върху бутона за пускане до всеки тест, за да го стартирате индивидуално
4. Кликнете "Run All Tests", за да изпълните цялата група
5. С десен бутон върху всеки тест и изберете "Debug Test" за поставяне на прекъсвачи и стъпково изпълнение

Test Explorer показва зелени отметки за преминали тестове и предоставя подробни съобщения за грешки при неуспехи.

## Шаблони за тестване

### Шаблон 1: Тестване на шаблони за подканване

Най-простият шаблон тества шаблони за подканване без да извиква AI модел. Проверявате, че подмяната на променливи работи правилно и подканите са форматирани както се очаква.

<img src="../../../translated_images/bg/prompt-template-testing.b902758ddccc8dee.webp" alt="Тестване на шаблон за подканване" width="800"/>

*Тестване на шаблони за подканване, показващо поток на подмяна на променливи: шаблон с плейсхолдъри → приложени стойности → проверен форматиран изход*

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

Този тест се намира в `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Стартирайте го:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#тестване на форматирането на шаблона за подканяне
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестване на форматирането на шаблона за подканване
```

### Шаблон 2: Моделиране (Mocking) на езикови модели

Когато тествате логика на разговор, използвайте Mockito, за да създадете фалшиви модели, които връщат предварително определени отговори. Това прави тестовете бързи, безплатни и детерминистични.

<img src="../../../translated_images/bg/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Сравнение на Mock и реално API" width="800"/>

*Сравнение, което показва защо моковете са предпочитани за тестване: те са бързи, безплатни, детерминистични и не изискват API ключове*

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
        assertThat(history).hasSize(6); // 3 потребителски + 3 AI съобщения
    }
}
```

Този шаблон се използва в `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мокът осигурява последователно поведение, за да можете да проверите правилното управление на паметта.

### Шаблон 3: Тестване на изолацията на разговори

Паметта за разговори трябва да държи отделните потребители разделени. Този тест проверява дали разговорите не смесват контексти.

<img src="../../../translated_images/bg/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Изолация на разговор" width="800"/>

*Тестване на изолацията на разговори, показващо отделни хранилища на памет за различни потребители, за да се предотврати смесването на контексти*

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

Всеки разговор поддържа своя независима история. В производствени системи тази изолация е критична за многопотребителни приложения.

### Шаблон 4: Тестване на инструменти независимо

Инструментите са функции, които AI може да извиква. Тествайте ги директно, за да сте сигурни, че работят правилно независимо от решенията на AI.

<img src="../../../translated_images/bg/tools-testing.3e1706817b0b3924.webp" alt="Тестване на инструменти" width="800"/>

*Тестване на инструменти независимо, показващо изпълнението на мокнат инструмент без AI извиквания за проверка на бизнес логиката*

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

Тези тестове от `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` валидират логиката на инструментите без участие на AI. Примерът с веригите показва как изходът от един инструмент се подава като вход за друг.

### Шаблон 5: Тестване на RAG в паметта

RAG системите обикновено изискват векторни бази данни и услуги за вграждания. Шаблонът с паметта позволява да тествате целия процес без външни зависимости.

<img src="../../../translated_images/bg/rag-testing.ee7541b1e23934b1.webp" alt="Тестване на RAG в паметта" width="800"/>

*Работен процес на RAG в паметта, показващ парсване на документи, съхранение на вграждания и търсене по сходство без изискване за база данни*

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

Този тест от `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` създава документ в паметта и проверява разбиването на части и обработката на метаданни.

### Шаблон 6: Интеграционно тестване на MCP

Модулът MCP тества интеграцията на Протокола за контекст на модел, използвайки stdio транспорт. Тези тестове проверяват дали вашето приложение може да стартира и комуникира с MCP сървъри като подпроцеси.

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

## Философия на тестването

Тествайте вашия код, а не AI. Вашите тестове трябва да валидират написания от вас код, като проверяват как се конструират подканите, как се управлява паметта и как се изпълняват инструментите. Отговорите на AI варират и не трябва да са част от твърденията в тестовете. Попитайте се дали шаблонът на подканата правилно замества променливите, а не дали AI дава правилния отговор.

Използвайте мокуни за езикови модели. Те са външни зависимости, които са бавни, скъпи и недетерминистични. Мокирането прави тестовете бързи с милисекунди вместо секунди, безплатни без разходи за API и детерминистични с еднакъв резултат всеки път.

Дръжте тестовете независими. Всеки тест трябва да създава собствените си данни, да не разчита на други тестове и да почиства след себе си. Тестовете трябва да минават независимо от реда на изпълнение.

Тествайте граничните случаи, извън нормалния сценарий. Опитайте празни входове, много големи входове, специални символи, невалидни параметри и гранични условия. Те често откриват бъгове, които нормалната употреба не изявява.

Използвайте описателни имена. Сравнете `shouldMaintainConversationHistoryAcrossMultipleMessages()` с `test1()`. Първото ви казва точно какво се тества, което улеснява отстраняването на грешки при неуспехи.

## Следващи стъпки

Сега, когато разбирате шаблоните за тестване, разгледайте всеки модул по-задълбочено:

- **[00 - Бърз старт](../00-quick-start/README.md)** - Започнете с основите на шаблоните за подканване
- **[01 - Увод](../01-introduction/README.md)** - Научете управление на паметта за разговори
- **[02 - Проектиране на промпти](../02-prompt-engineering/README.md)** - Усвойте шаблоните на GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Изградете системи за генериране с прикачено търсене
- **[04 - Инструменти](../04-tools/README.md)** - Имплементирайте извикване на функции и вериги от инструменти
- **[05 - MCP](../05-mcp/README.md)** - Интегрирайте Протокола за контекст на модел

README файловете на всеки модул предоставят подробни обяснения на концепциите, тествани тук.

---

**Навигация:** [← Обратно към Основното](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводаческа услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на езика, на който е написан, трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за недоразумения или грешни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->