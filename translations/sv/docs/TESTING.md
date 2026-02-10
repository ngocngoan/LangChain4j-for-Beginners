# Testning av LangChain4j-applikationer

## Innehållsförteckning

- [Snabbstart](../../../docs)
- [Vad testerna täcker](../../../docs)
- [Köra testerna](../../../docs)
- [Köra tester i VS Code](../../../docs)
- [Testningsmönster](../../../docs)
- [Testningsfilosofi](../../../docs)
- [Nästa steg](../../../docs)

Den här guiden leder dig genom tester som visar hur du testar AI-applikationer utan att kräva API-nycklar eller externa tjänster.

## Snabbstart

Kör alla tester med ett enda kommando:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/sv/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Lyckad testkörning som visar alla tester passerar utan fel*

## Vad testerna täcker

Den här kursen fokuserar på **enhetstester** som körs lokalt. Varje test demonstrerar ett specifikt LangChain4j-koncept i isolering.

<img src="../../../translated_images/sv/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testpyramid som visar balansen mellan enhetstester (snabba, isolerade), integrationstester (riktiga komponenter) och end-to-end-tester. Denna träning täcker enhetstestning.*

| Modul | Tester | Fokus | Nyckelfiler |
|--------|-------|-------|-----------|
| **00 - Snabbstart** | 6 | Mallar för prompt och variabelsubstitution | `SimpleQuickStartTest.java` |
| **01 - Introduktion** | 8 | Konversationsminne och stateful chatt | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2-mönster, ivernivåer, strukturerad output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentinmatning, inbäddningar, likhetssökning | `DocumentServiceTest.java` |
| **04 - Verktyg** | 12 | Funktionsanrop och verktygskedjor | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Modellkontextprotokoll med Stdio-transport | `SimpleMcpTest.java` |

## Köra testerna

**Kör alla tester från roten:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Kör tester för en specifik modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Eller från root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Eller från rot
mvn --% test -pl 01-introduction
```

**Kör en enskild testklass:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Kör en specifik testmetod:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#börBehållaSamtalshistorik
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#skaBibehållaKonversationshistorik
```

## Köra tester i VS Code

Om du använder Visual Studio Code erbjuder Test Explorer ett grafiskt gränssnitt för att köra och felsöka tester.

<img src="../../../translated_images/sv/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer som visar testträdet med alla Java-testklasser och individuella testmetoder*

**För att köra tester i VS Code:**

1. Öppna Test Explorer genom att klicka på bägareikonen i Aktivitetsfältet
2. Expandera testträdet för att se alla moduler och testklasser
3. Klicka på play-knappen bredvid ett test för att köra det individuellt
4. Klicka på "Run All Tests" för att köra hela testsviten
5. Högerklicka på ett test och välj "Debug Test" för att sätta brytpunkter och stega i koden

Test Explorern visar gröna bockar för godkända tester och ger detaljerade felmeddelanden när tester misslyckas.

## Testningsmönster

### Mönster 1: Testa Prompt-mallar

Det enklaste mönstret testar prompt-mallar utan att anropa någon AI-modell. Du verifierar att variabelsubstitution fungerar korrekt och att prompts är formaterade som förväntat.

<img src="../../../translated_images/sv/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testning av prompt-mallar som visar flöde av variabelsubstitution: mall med platshållare → värden applicerade → formaterad output verifierad*

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

Det här testet finns i `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Kör det:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatering
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatering
```

### Mönster 2: Mocka språkmodeller

Vid testning av konversationslogik används Mockito för att skapa falska modeller som returnerar förutbestämda svar. Detta gör testerna snabba, kostnadsfria och deterministiska.

<img src="../../../translated_images/sv/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Jämförelse som visar varför mockar är att föredra för testning: de är snabba, gratis, deterministiska och kräver inga API-nycklar*

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
        assertThat(history).hasSize(6); // 3 användare + 3 AI-meddelanden
    }
}
```

Det här mönstret förekommer i `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mocken säkerställer konsekvent beteende så att du kan verifiera att minneshantering fungerar korrekt.

### Mönster 3: Testa konversationsisolering

Konversationsminnet måste hålla flera användare separata. Detta test verifierar att konversationer inte blandar kontexter.

<img src="../../../translated_images/sv/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testning av konversationsisolering som visar separata minneslagringar för olika användare för att förhindra kontextblandning*

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

Varje konversation upprätthåller sin egen oberoende historik. I produktionssystem är denna isolering kritisk för multi-användar-applikationer.

### Mönster 4: Testa verktyg oberoende

Verktyg är funktioner som AI kan anropa. Testa dem direkt för att säkerställa att de fungerar korrekt oavsett AI-beslut.

<img src="../../../translated_images/sv/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testning av verktyg oberoende som visar mock-verktygsexekvering utan AI-anrop för att verifiera affärslogik*

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

Dessa tester från `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validerar verktygslogik utan AI-inblandning. Kedjeexemplet visar hur en verktygs output matas in till en annans input.

### Mönster 5: In-memory RAG-testning

RAG-system kräver traditionellt vektordatabaser och inbäddningstjänster. In-memory-mönstret låter dig testa hela pipelinen utan externa beroenden.

<img src="../../../translated_images/sv/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG-testnings arbetsflöde visar dokumentanalys, inbäddningslagring och likhetssökning utan krav på databas*

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

Det här testet från `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` skapar ett dokument i minnet och verifierar chunking och metadatahantering.

### Mönster 6: MCP-integrations-testning

MCP-modulen testar Model Context Protocols integration via stdio-transport. Dessa tester verifierar att din applikation kan starta och kommunicera med MCP-servrar som subprocesser.

Tester i `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validerar MCP-klientbeteende.

**Kör dem:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testningsfilosofi

Testa din kod, inte AI:n. Dina tester ska validera den kod du skriver genom att kontrollera hur prompts konstrueras, hur minnet hanteras och hur verktyg exekveras. AI-svar varierar och bör inte ingå i testpåståenden. Fråga dig om din promptmall korrekt ersätter variabler, inte om AI ger rätt svar.

Använd mockar för språkmodeller. De är externa beroenden som är långsamma, dyra och icke-deterministiska. Mockning gör tester snabba med millisekunder istället för sekunder, kostnadsfria utan API-kostnader, och deterministiska med samma resultat varje gång.

Håll tester oberoende. Varje test ska skapa sin egen data, inte förlita sig på andra tester, och städa upp efter sig. Tester bör klara sig oavsett körordning.

Testa kantfall utöver glädjeväggen. Prova tomma indata, väldigt stora indata, specialtecken, ogiltiga parametrar, och gränsvärden. Dessa avslöjar ofta buggar som normal användning inte visar.

Använd beskrivande namn. Jämför `shouldMaintainConversationHistoryAcrossMultipleMessages()` med `test1()`. Det första talar om exakt vad som testas, vilket gör felsökning mycket enklare.

## Nästa steg

Nu när du förstår testningsmönstren, fördjupa dig i varje modul:

- **[00 - Snabbstart](../00-quick-start/README.md)** - Börja med grunderna i promptmallar
- **[01 - Introduktion](../01-introduction/README.md)** - Lär dig hantera konversationsminne
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Bemästra GPT-5.2-promptmönster
- **[03 - RAG](../03-rag/README.md)** - Bygg återhämtningsförstärkta genereringssystem
- **[04 - Verktyg](../04-tools/README.md)** - Implementera funktionsanrop och verktygskedjor
- **[05 - MCP](../05-mcp/README.md)** - Integrera Model Context Protocol

Varje moduls README innehåller detaljerade förklaringar av koncepten som testas här.

---

**Navigation:** [← Tillbaka till huvudmenyn](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår genom användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->