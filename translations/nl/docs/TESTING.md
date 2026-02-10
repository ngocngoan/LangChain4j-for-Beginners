# Testen van LangChain4j-toepassingen

## Inhoudsopgave

- [Snel aan de slag](../../../docs)
- [Wat de tests behandelen](../../../docs)
- [Tests uitvoeren](../../../docs)
- [Tests uitvoeren in VS Code](../../../docs)
- [Testpatronen](../../../docs)
- [Testfilosofie](../../../docs)
- [Volgende stappen](../../../docs)

Deze gids leidt je door de tests die laten zien hoe je AI-applicaties test zonder API-sleutels of externe services.

## Snel aan de slag

Voer alle tests uit met één opdracht:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/nl/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Succesvolle tests met alle tests geslaagd zonder fouten*

## Wat de tests behandelen

Deze cursus richt zich op **unittests** die lokaal worden uitgevoerd. Elke test demonstreert een specifiek LangChain4j-concept in isolatie.

<img src="../../../translated_images/nl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testpiramide die de balans toont tussen unittests (snel, geïsoleerd), integratietests (echte componenten) en end-to-end tests. Deze training gaat over unittesten.*

| Module | Tests | Focus | Belangrijke bestanden |
|--------|-------|-------|----------------------|
| **00 - Snel aan de slag** | 6 | Promptsjablonen en variabele substitutie | `SimpleQuickStartTest.java` |
| **01 - Introductie** | 8 | Gespreksgeheugen en stateful chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 patronen, eagerness-niveaus, gestructureerde output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Documentinvoer, embeddings, gelijkeniszoektocht | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Functieaanroepen en ketening van tools | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol met Stdio-transport | `SimpleMcpTest.java` |

## Tests uitvoeren

**Alle tests uitvoeren vanaf root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Tests uitvoeren voor een specifieke module:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Of vanaf root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Of vanaf root
mvn --% test -pl 01-introduction
```

**Een enkele testklasse uitvoeren:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Een specifieke testmethode uitvoeren:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#moetGespreksgeschiedenisBewaren
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#conversatiegeschiedenis behouden
```

## Tests uitvoeren in VS Code

Als je Visual Studio Code gebruikt, biedt de Test Explorer een grafische interface voor het uitvoeren en debuggen van tests.

<img src="../../../translated_images/nl/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer die de testboom toont met alle Java-testklassen en individuele testmethoden*

**Tests uitvoeren in VS Code:**

1. Open de Test Explorer door op het bekervormige icoon in de Activiteitenbalk te klikken
2. Vouw de testboom uit om alle modules en testklassen te zien
3. Klik op de afspeelknop naast een test om deze afzonderlijk uit te voeren
4. Klik op "Run All Tests" om de hele suite uit te voeren
5. Klik met de rechtermuisknop op een test en selecteer "Debug Test" om breakpoints te zetten en door de code te stappen

De Test Explorer toont groene vinkjes bij geslaagde tests en geeft gedetailleerde foutmeldingen wanneer tests mislukken.

## Testpatronen

### Patroon 1: Testen van promptsjablonen

Het eenvoudigste patroon test promptsjablonen zonder een AI-model aan te roepen. Je verifieert dat variabele substitutie correct werkt en dat prompts correct zijn opgemaakt.

<img src="../../../translated_images/nl/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testen van promptsjablonen die de variabele substitutiestroom tonen: sjabloon met tijdelijke aanduidingen → waarden toegepast → geformatteerde output gecontroleerd*

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

Deze test bevindt zich in `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Uitvoeren:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptTemplateOpmaak
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateOpmaak
```

### Patroon 2: Mocking van taalmodellen

Bij het testen van gesprekslogica gebruik je Mockito om nepmodellen te maken die vooraf bepaalde antwoorden teruggeven. Dit maakt tests snel, gratis en deterministisch.

<img src="../../../translated_images/nl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Vergelijking die laat zien waarom mocks de voorkeur hebben voor testen: snel, gratis, deterministisch en geen API-sleutels nodig*

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
        assertThat(history).hasSize(6); // 3 gebruikers + 3 AI-berichten
    }
}
```

Dit patroon verschijnt in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. De mock zorgt voor consistente gedrag zodat je kunt verifiëren dat geheugengebruik correct werkt.

### Patroon 3: Testen van gespreksisolatie

Het gespreksgeheugen moet meerdere gebruikers gescheiden houden. Deze test verifieert dat gesprekken geen context mengen.

<img src="../../../translated_images/nl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testen van gespreksisolatie met afzonderlijke geheugens voor verschillende gebruikers om contextvermenging te voorkomen*

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

Elk gesprek onderhoudt zijn eigen onafhankelijke geschiedenis. In productiesystemen is deze isolatie cruciaal voor multi-user applicaties.

### Patroon 4: Tools onafhankelijk testen

Tools zijn functies die de AI kan aanroepen. Test ze rechtstreeks om te verzekeren dat ze correct werken, ongeacht AI-beslissingen.

<img src="../../../translated_images/nl/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Tools onafhankelijk testen, waarbij mock-uitvoering van tools wordt getoond zonder AI-aanroepen om de bedrijfslogica te verifiëren*

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

Deze tests uit `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` valideren de logica van tools zonder AI-betrokkenheid. Het kettingvoorbeeld laat zien hoe de output van de ene tool de input voor een andere voedt.

### Patroon 5: In-memory RAG testen

RAG-systemen vereisen traditioneel vectordatabases en embeddingdiensten. Het in-memory patroon laat je de hele pijplijn testen zonder externe afhankelijkheden.

<img src="../../../translated_images/nl/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG testworkflow die documentparsering, embeddingopslag en gelijkeniszoektocht toont zonder een database te vereisen*

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

Deze test uit `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` maakt een document in het geheugen en verifieert chunking en metadata-afhandeling.

### Patroon 6: MCP integratietesten

De MCP-module test de Model Context Protocol-integratie met stdio transport. Deze tests verifiëren dat je applicatie MCP-servers als subprocessen kan opstarten en ermee communiceren.

De tests in `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` valideren het gedrag van de MCP-client.

**Uitvoeren:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testfilosofie

Test je code, niet de AI. Je tests moeten de code die je schrijft valideren door te controleren hoe prompts worden opgebouwd, hoe geheugen wordt beheerd en hoe tools worden uitgevoerd. AI-reacties variëren en mogen geen onderdeel zijn van testasserties. Vraag jezelf af of je promptsjabloon variabelen correct vervangt, niet of de AI het juiste antwoord geeft.

Gebruik mocks voor taalmodellen. Dit zijn externe afhankelijkheden die traag, duur en niet-deterministisch zijn. Mocking maakt tests snel (milliseconden in plaats van seconden), gratis (geen API-kosten) en deterministisch (altijd hetzelfde resultaat).

Houd tests onafhankelijk. Elke test moet zijn eigen gegevens opzetten, niet afhangen van andere tests, en zichzelf opruimen. Tests moeten slagen ongeacht de uitvoeringsvolgorde.

Test randgevallen voorbij het 'happy path'. Probeer lege invoer, zeer grote invoer, speciale tekens, ongeldige parameters en grensvoorwaarden. Deze onthullen vaak bugs die normaal gebruik niet exposeert.

Gebruik beschrijvende namen. Vergelijk `shouldMaintainConversationHistoryAcrossMultipleMessages()` met `test1()`. De eerste vertelt precies wat getest wordt, wat het debuggen van fouten veel makkelijker maakt.

## Volgende stappen

Nu je de testpatronen begrijpt, duik dieper in elke module:

- **[00 - Snel aan de slag](../00-quick-start/README.md)** - Begin met de basis van promptsjablonen
- **[01 - Introductie](../01-introduction/README.md)** - Leer gespreksgeheugenbeheer
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Beheers GPT-5.2 promptingpatronen
- **[03 - RAG](../03-rag/README.md)** - Bouw retrieval-augmented generatie systemen
- **[04 - Tools](../04-tools/README.md)** - Implementeer functieaanroepen en toolketens
- **[05 - MCP](../05-mcp/README.md)** - Integreer Model Context Protocol

De README van elke module biedt gedetailleerde uitleg over de hier geteste concepten.

---

**Navigatie:** [← Terug naar hoofdmenu](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de originele taal moet als de gezaghebbende bron worden beschouwd. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->