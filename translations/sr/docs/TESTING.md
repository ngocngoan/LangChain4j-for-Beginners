# Тестирање LangChain4j апликација

## Садржај

- [Брзи почетак](../../../docs)
- [Шта тестови обухватају](../../../docs)
- [Покретање тестова](../../../docs)
- [Покретање тестова у VS Code-у](../../../docs)
- [Обрасци тестирања](../../../docs)
- [Филозофија тестирања](../../../docs)
- [Следећи кораци](../../../docs)

Овај водич вас води кроз тестове који показују како тестирати AI апликације без потребе за API кључевима или спољним сервисима.

## Брзи почетак

Покрените све тестове једном командом:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Када сви тестови прођу, требало би да видите излаз као на снимку екрана испод — тестови се извршавају без грешака.

<img src="../../../translated_images/sr/test-results.ea5c98d8f3642043.webp" alt="Успешни резултати тестирања" width="800"/>

*Успешно извршење тестова показујући да сви тестови пролазе без грешака*

## Шта тестови обухватају

Овај курс се фокусира на **јединичне тестове** који се извршавају локално. Сваки тест демонстрира одређени LangChain4j концепт изоловано. Пирамида тестирања испод показује где јединични тестови припадају — они чине брзу, поуздану основу на којој се гради остатак ваше тест стратегије.

<img src="../../../translated_images/sr/testing-pyramid.2dd1079a0481e53e.webp" alt="Пирамида тестирања" width="800"/>

*Пирамида тестирања показује баланс између јединичних тестова (брзи, изоловани), интеграционих тестова (стварне компоненте) и end-to-end тестова. Ова обука покрива јединично тестирање.*

| Модул | Тестови | Фокус | Главне датотеке |
|--------|-------|-------|-----------|
| **00 - Брзи почетак** | 6 | Шаблони упита и замена променљивих | `SimpleQuickStartTest.java` |
| **01 - Увод** | 8 | Меморија конверзације и стање ћаскања | `SimpleConversationTest.java` |
| **02 - Инжењеринг упита** | 12 | GPT-5.2 обрасци, нивои жеље, структурисани излаз | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Унос докумената, уградње, претрага сличности | `DocumentServiceTest.java` |
| **04 - Алати** | 12 | Позив функција и ланци алата | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Протокол контекста модела са Stdio транспортом | `SimpleMcpTest.java` |

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
# Или из root-а
mvn --% test -pl 01-introduction
```

**Покрените једну тест класу:**

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
mvn test -Dtest=SimpleConversationTest#треба_одржавати_историју_разговора
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#требаОдржаватиИсторијуРазговора
```

## Покретање тестова у VS Code-у

Ако користите Visual Studio Code, Test Explorer пружа графички интерфејс за покретање и отклањање грешака у тестовима.

<img src="../../../translated_images/sr/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer приказује стабло тестова са свим Java тест класама и појединачним тест методама*

**За покретање тестова у VS Code-у:**

1. Отворите Test Explorer кликом на икону колбице у Activity Bar-у
2. Проширите стабло тестова да бисте видели све модуле и тест класе
3. Кликните на дугме за репродукцију поред било ког теста да га покренете појединачно
4. Кликните "Run All Tests" да извршите цео скуп
5. Десни клик на било који тест и одаберите "Debug Test" да поставите тачке прекида и корачате кроз код

Test Explorer приказује зелене ознаке за тестове који су прошли и пружа детаљне поруке о грешкама када тестови не успеју.

## Обрасци тестирања

### Образац 1: Тестирање шаблона упита

Најједноставнији образац тестира шаблоне упита без позива било ког AI модела. Ви проверавате да ли замена променљивих ради исправно и да ли су упити форматирани као што се очекује.

<img src="../../../translated_images/sr/prompt-template-testing.b902758ddccc8dee.webp" alt="Тестирање шаблона упита" width="800"/>

*Тестирање шаблона упита које показује ток замене променљивих: шаблон са ознакама места → примењене вредности → потврђен форматирани излаз*

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

Овај тест је у `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Покрените га:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#тестирањеФорматирањаУзоракПоруке
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#тестирањеФорматисањаШаблонаПодударања
```

### Образац 2: Моковање језичких модела

Када тестирате логику конверзације, користите Mockito за креирање лажних модела који враћају унапред одређене одговоре. Ово чини тестове брзим, бесплатним и детерминистичким.

<img src="../../../translated_images/sr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Поређење мокова и стварног API-ја" width="800"/>

*Поређење које показује зашто су мокови погодни за тестирање: брзи су, бесплатни, детерминистички и не захтевају API кључеве*

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
        assertThat(history).hasSize(6); // 3 корисничке + 3 AI поруке
    }
}
```

Овај образац се налази у `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Мок обезбеђује конзистентно понашање тако да можете проверити исправно управљање меморијом.

### Образац 3: Тестирање изолације конверзације

Меморија конверзације мора одржавати више корисника одвојено. Овај тест проверава да се контексти конверзација не мешају.

<img src="../../../translated_images/sr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Изолација конверзације" width="800"/>

*Тестирање изолације конверзације које показује посебне меморијске трезоре за различите кориснике да би спречило мешање контекста*

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

Свака конверзација одржава своју независну историју. У производним системима, ова изолација је критична за мултикорисничке апликације.

### Образац 4: Тестирање алата самостално

Алати су функције које AI може позвати. Тестирајте их директно да бисте осигурали да раде исправно без обзира на одлуке AI-а.

<img src="../../../translated_images/sr/tools-testing.3e1706817b0b3924.webp" alt="Тестирање алата" width="800"/>

*Тестирање алата независно показује извођење мок алата без позива AI-а ради провере пословне логике*

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

Ови тестови из `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` верификују логику алата без укључења AI-а. Пример ланца показује како излаз једног алата улази у улаз другог.

### Образац 5: Тестирање RAG у меморији

RAG системи традиционално захтевају векторске базе података и услуге за уградњу. Образац из меморије вам дозвољава да тестирате целу пајплајн без спољних зависности.

<img src="../../../translated_images/sr/rag-testing.ee7541b1e23934b1.webp" alt="Тестирање RAG у меморији" width="800"/>

*Ток рада тестирања RAG у меморији који показује парсирање докумената, чување уградњи и претрагу сличности без потребе за базом података*

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

Овај тест из `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` креира документ у меморији и проверава цепање у делове и руковање метаподацима.

### Образац 6: MCP интеграционо тестирање

MCP модул тестира интеграцију Протокола контекста модела користећи stdio транспорт. Ови тестови проверавају да ли ваша апликација може покренути MCP сервере као подпроцесе и комуницирати с њима.

Тестови у `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` верификују понашање MCP клијента.

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

Тестирајте свој код, а не AI. Ваши тестови треба да верификују код који пишете проверавајући како су упити конструисани, како се управља меморијом и како се извршавају алати. AI одговори варирају и не би требало да буду део тврдњи у тестовима. Питајте се да ли ваш шаблон упита правилно замењује променљиве, а не да ли AI даје прави одговор.

Користите мокове за језичке моделе. Они су спољне зависности које су споре, скупе и недетерминистичке. Моковање чини тестове брзим са милисекундама уместо секунди, бесплатним без трошкова API-ја и детерминистичким са истим резултатом сваког пута.

Држите тестове независним. Сваки тест треба да подеси своје податке, не треба да се ослања на друге тестове и треба да очисти после себе. Тестови треба да пролазе без обзира на редослед извршавања.

Тестирајте крајње случајеве ван срећног пута. Покушајте празне уносе, веома велике уносе, специјалне знакове, неважеће параметре и граничне услове. Често ово открива грешке које нормална употреба не изазива.

Користите описне називе. Упоредите `shouldMaintainConversationHistoryAcrossMultipleMessages()` са `test1()`. Први вам тачно говори шта се тестира, што много олакшава отклањање грешака.

## Следећи кораци

Сада када разумете obrasce тестирања, детаљније проучите сваки модул:

- **[00 - Брзи почетак](../00-quick-start/README.md)** - Почните са основама шаблона упита
- **[01 - Увод](../01-introduction/README.md)** - Научите управљање меморијом конверзације
- **[02 - Инжењеринг упита](../02-prompt-engineering/README.md)** - Савладајте GPT-5.2 обрасце упита
- **[03 - RAG](../03-rag/README.md)** - Конструишите системе за генерацију са претрагом
- **[04 - Алати](../04-tools/README.md)** - Имплементирајте позив функција и ланце алата
- **[05 - MCP](../05-mcp/README.md)** - Интегришите Протокол контекста модела

Сваки модул у свом README-у пружа детаљна објашњења концепата тестираних овде.

---

**Навигација:** [← Назад на Главну](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ограничење одговорности**:  
Овај документ је преведен користећи АИ сервис за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да превод буде тачан, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетним извором. За критичне информације препорука је коришћење професионалног људског превода. Не одговарамо за било каква неспоразума или погрешне тумачења настала употребом овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->