# Тестирање LangChain4j апликација

## Садржај

- [Брзи почетак](../../../docs)
- [Шта тестови покривају](../../../docs)
- [Покретање тестова](../../../docs)
- [Покретање тестова у VS Code-у](../../../docs)
- [Обрасци тестирања](../../../docs)
- [Филозофија тестирања](../../../docs)
- [Следећи кораци](../../../docs)

Овај водич вас води кроз тестове који показују како тестирати AI апликације без захтева за API кључеве или спољне сервисе.

## Брзи почетак

Покрените све тестове једном команом:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/sr/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Успешно извршавање тестова показује све тестове са нула неуспелих*

## Шта тестови покривају

Овај курс се фокусира на **јединчане тестове** који се извршавају локално. Сваки тест демонстрира одређену LangChain4j концепцију у изолацији.

<img src="../../../translated_images/sr/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Тестирачка пирамида која показује равнотежу између јединчаних тестова (брзих, изолованих), интеграционих тестова (правих компонената) и тестова од краја до краја. Ова обука покрива јединчано тестирање.*

| Модул | Тестови | Фокус | Кључне датотеке |
|--------|-------|-------|-----------|
| **00 - Брзи почетак** | 6 | Шаблони упита и замена променљивих | `SimpleQuickStartTest.java` |
| **01 - Увод** | 8 | Меморија разговора и стање у ћаскању | `SimpleConversationTest.java` |
| **02 - Промпт инжењеринг** | 12 | GPT-5.2 обрасци, нивои жељности, структурисани излаз | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Унос докумената, уграђивања, претрага сличности | `DocumentServiceTest.java` |
| **04 - Алати** | 12 | Позив функција и ланци алата | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол модела контекста са Stdio транспортом | `SimpleMcpTest.java` |

## Покретање тестова

**Покрените све тестове из корена:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Покрените тестове за одређени модул:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Или из корена
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Или из корена
mvn --% test -pl 01-introduction
```

**Покрените једну класу тестова:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Покрените одређену тест методу:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#треба одржавати историју разговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#требаОдржаватиИсторијуРазговора
```

## Покретање тестова у VS Code-у

Ако користите Visual Studio Code, Test Explorer пружа графички интерфејс за покретање и дебаговање тестова.

<img src="../../../translated_images/sr/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer приказује тест стабло са свим Java тест класама и појединачним тест методама*

**Да бисте покренули тестове у VS Code-у:**

1. Отворите Test Explorer кликом на иконицу колбице у Activity Bar-у
2. Проширите тест стабло да видите све модуле и тест класе
3. Кликните на дугме за репродукцију поред било ког теста да га покренете појединачно
4. Кликните на "Run All Tests" да извршите цео скупов тестова
5. Десни клик на било који тест и изаберите "Debug Test" да поставите тачке прекида и кроз код корачате

Test Explorer показује зелене ознаке за успешне тестове и пружа детаљне поруке о грешкама када тестови не успеју.

## Обрасци тестирања

### Образац 1: Тестирање шаблона упита

Најједноставнији образац тестира шаблоне упита без позива било ког AI модела. Проверавате да ли замена променљивих ради исправно и да ли су упити форматирани како се очекује.

<img src="../../../translated_images/sr/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Тестирање шаблона упита показује ток замене променљивих: шаблон са резервисаним местима → примена вредности → верификује се форматирани излаз*

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

Овај тест се налази у `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Покрените га:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#тестирањеФорматирањаШаблонаПоруке
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестирањеФорматирањаШаблонаУпита
```

### Образац 2: Мокирање језичких модела

Када тестирате логику разговора, користите Mockito да креирате лажне моделе који враћају предодређене одговоре. Ово чини тестове брзим, бесплатним и детерминистичким.

<img src="../../../translated_images/sr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Поређење које показује зашто су моки пожељни за тестирање: брзи су, бесплатни, детерминистички и не захтевају API кључеве*

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
        assertThat(history).hasSize(6); // 3 поруке корисника + 3 поруке вештачке интелигенције
    }
}
```

Овај образац се појављује у `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок осигурава конзистентно понашање како бисте проверили да ли меморија ради исправно.

### Образац 3: Тестирање изолације разговора

Меморија разговора мора држати више корисника одвојено. Овај тест проверава да се разговори не мешају у контексту.

<img src="../../../translated_images/sr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Тестирање изолације разговора приказује одвојене меморијске складиште за различите кориснике како би се спречило мешање контекста*

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

Сваки разговор држи своју независну историју. У производним системима ова изолација је критична за апликације са више корисника.

### Образац 4: Тестирање алата независно

Алати су функције које AI може позвати. Тестирајте их директно да бисте осигурали да раде исправно без обзира на одлуке AI.

<img src="../../../translated_images/sr/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Тестирање алата независно показује извршење мок алата без позива AI-ја ради верификације логике пословања*

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

Ови тестови из `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` валидирају логику алата без укључивања AI. Пример ланца показује како излаз једног алата улази као улаз другог.

### Образац 5: Тестирање RAG у меморији

RAG системи традиционално захтевају векторске базе и услуге уграђивања. Образац у меморији вам омогућава да тестирате цео ток без спољних зависности.

<img src="../../../translated_images/sr/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Радни ток тестирања RAG у меморији приказује парсирање докумената, чување уграђивања и претрагу сличности без потребе за базом података*

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

Овај тест из `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` креира документ у меморији и проверава подређивање и руковање метаподацима.

### Образац 6: MCP интеграционо тестирање

MCP модул тестира интеграцију Протокола модела контекста користећи stdio транспорт. Ови тестови проверавају да ли ваша апликација може да покреће и комуницира са MCP серверима као потпроцесима.

Тестови у `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` валидирају понашање MCP клијента.

**Покрените их:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Филозофија тестирања

Тестирајте свој код, а не AI. Ваши тестови треба да верификују код који пишете проверавајући како се конструишу упити, како се управља меморијом и како се извршавају алати. AI одговори варирају и не треба да буду део тврдњи у тестовима. Запитајте се да ли ваш шаблон упита исправно замењује променљиве, а не да ли AI даје тачан одговор.

Користите моке за језичке моделе. Они су спољне зависности које су споре, скупе и недетерминистичке. Мокирање чини тестове брзим са милисекундама уместо секунди, бесплатним без трошкова API-ја, и детерминистичким са истим резултатом сваки пут.

Држите тестове независним. Сваки тест треба да подеси своје податке, да не зависи од других тестова и да очисти након себе. Тестови треба да пролазе без обзира на редослед извршавања.

Тестирајте и крајње случајеве ван нормалног тока. Покушајте празне уносе, веома велике уносе, посебне знакове, неважеће параметре и граничне услове. Ово често открива грешке које нормална употреба не открива.

Користите описна имена. Упоредите `shouldMaintainConversationHistoryAcrossMultipleMessages()` са `test1()`. Прво вам тачно говори шта се тестира, олакшавајући откривање грешака.

## Следећи кораци

Сада када разумете обрасце тестирања, дубље се посветите сваком модулу:

- **[00 - Брзи почетак](../00-quick-start/README.md)** - Почните са основама шаблона упита
- **[01 - Увод](../01-introduction/README.md)** - Научите управљање меморијом разговора
- **[02 - Промпт инжењеринг](../02/prompt-engineering/README.md)** - Усавршите GPT-5.2 образце упита
- **[03 - RAG](../03-rag/README.md)** - Направите системе засноване на преузимању и генерисању
- **[04 - Алати](../04-tools/README.md)** - Имплементирајте позиве функција и ланце алата
- **[05 - MCP](../05-mcp/README.md)** - Интегришите Протокол модела контекста

Сваки модул има README са детаљним објашњењима концепата тестираних овде.

---

**Навигација:** [← Назад на почетну](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:  
Овај документ је преведен уз помоћ AI сервиса за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде прецизан, имајте у виду да аутоматски преводи могу да садрже грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални људски превод. Није нам одговорност за било какве неспоразуме или погрешне тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->