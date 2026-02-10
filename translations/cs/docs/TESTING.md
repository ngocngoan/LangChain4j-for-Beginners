# Testování aplikací LangChain4j

## Obsah

- [Rychlý start](../../../docs)
- [Co testy pokrývají](../../../docs)
- [Spouštění testů](../../../docs)
- [Spouštění testů ve VS Code](../../../docs)
- [Testovací vzory](../../../docs)
- [Testovací filozofie](../../../docs)
- [Další kroky](../../../docs)

Tento průvodce vás provede testy, které ukazují, jak testovat AI aplikace bez potřeby API klíčů nebo externích služeb.

## Rychlý start

Spusťte všechny testy jediným příkazem:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/cs/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Úspěšné spuštění testů zobrazující všechny testy bez jediného selhání*

## Co testy pokrývají

Tento kurz se zaměřuje na **jednotkové testy**, které běží lokálně. Každý test demonstruje konkrétní koncept LangChain4j izolovaně.

<img src="../../../translated_images/cs/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testovací pyramida ukazující rovnováhu mezi jednotkovými testy (rychlé, izolované), integračními testy (skutečné komponenty) a end-to-end testy. Toto školení pokrývá jednotkové testování.*

| Modul | Testy | Zaměření | Klíčové soubory |
|--------|-------|----------|-----------------|
| **00 - Rychlý start** | 6 | Šablony promptů a dosazování proměnných | `SimpleQuickStartTest.java` |
| **01 - Úvod** | 8 | Paměť konverzace a stavový chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Vzory GPT-5.2, úrovně ochoty, strukturovaný výstup | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Zpracování dokumentů, embeddingy, hledání podobnosti | `DocumentServiceTest.java` |
| **04 - Nástroje** | 12 | Volání funkcí a řetězení nástrojů | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol se stdio transportem | `SimpleMcpTest.java` |

## Spouštění testů

**Spuštění všech testů z kořene:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Spuštění testů konkrétního modulu:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Nebo z root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Nebo z root
mvn --% test -pl 01-introduction
```

**Spuštění jedné testovací třídy:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Spuštění konkrétní testovací metody:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#měla by uchovávat historii konverzace
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#měl by udržovat historii konverzace
```

## Spouštění testů ve VS Code

Pokud používáte Visual Studio Code, Test Explorer poskytuje grafické rozhraní pro spouštění a ladění testů.

<img src="../../../translated_images/cs/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer zobrazující strom testů se všemi Java testovacími třídami a jednotlivými testovacími metodami*

**Jak spustit testy ve VS Code:**

1. Otevřete Test Explorer kliknutím na ikonku zkumavky v liště aktivit
2. Rozbalte strom testů pro zobrazení všech modulů a testovacích tříd
3. Klikněte na tlačítko přehrávání vedle libovolného testu pro jeho individuální spuštění
4. Klikněte na "Run All Tests" pro spuštění celé sady
5. Pravým tlačítkem klikněte na jakýkoli test a zvolte "Debug Test" pro nastavení breakpointů a krokování kódem

Test Explorer zobrazuje zelené fajfky u úspěšně prošlých testů a poskytuje podrobné zprávy o selháních, pokud testy selžou.

## Testovací vzory

### Vzor 1: Testování šablon promptů

Nejjednodušší vzor testuje šablony promptů bez volání jakéhokoli AI modelu. Ověřujete, že dosazení proměnných funguje správně a prompt je formátován dle očekávání.

<img src="../../../translated_images/cs/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testování šablon promptů ukazující tok dosazování proměnných: šablona s místy pro proměnné → aplikované hodnoty → ověřený formátovaný výstup*

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

Tento test je umístěn v `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Spuštění:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testFormátováníŠablonyVýzvy
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testFormátováníŠablonyVýzvy
```

### Vzor 2: Mockování jazykových modelů

Při testování logiky konverzace použijte Mockito pro vytvoření falešných modelů, které vrací předem určené odpovědi. Díky tomu jsou testy rychlé, zdarma a deterministické.

<img src="../../../translated_images/cs/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Porovnání ukazující, proč jsou mocky preferované pro testování: jsou rychlé, zdarma, deterministické a nevyžadují API klíče*

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
        assertThat(history).hasSize(6); // 3 zprávy uživatele + 3 zprávy AI
    }
}
```

Tento vzor se vyskytuje v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zajišťuje konzistentní chování, takže můžete ověřit správnou správu paměti.

### Vzor 3: Testování izolace konverzace

Paměť konverzace musí udržovat uživatele oddělené. Tento test ověřuje, že se konverzace nekombinují.

<img src="../../../translated_images/cs/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testování izolace konverzace ukazující samostatné paměťové úložiště pro různé uživatele, aby nedocházelo ke smíchání kontextu*

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

Každá konverzace udržuje vlastní nezávislou historii. V produkčních systémech je tato izolace klíčová pro multi-uživatelské aplikace.

### Vzor 4: Testování nástrojů nezávisle

Nástroje jsou funkce, které AI může volat. Testujte je přímo, abyste se ujistili, že fungují správně bez ohledu na rozhodnutí AI.

<img src="../../../translated_images/cs/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testování nástrojů samostatně ukazující mockované spuštění nástroje bez volání AI pro ověření obchodní logiky*

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

Tyto testy z `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ověřují logiku nástrojů bez zapojení AI. Příklad řetězení ukazuje, jak výstup jednoho nástroje slouží jako vstup do dalšího.

### Vzor 5: Testování RAG v paměti

RAG systémy tradičně vyžadují vektorové databáze a embedding služby. Vzor v paměti umožňuje testovat celý proces bez externích závislostí.

<img src="../../../translated_images/cs/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Průběh testování RAG v paměti ukazující parsování dokumentů, ukládání embeddingů a hledání podobnosti bez nutnosti databáze*

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

Tento test z `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` vytvoří dokument v paměti a ověří dělení na části a zpracování metadat.

### Vzor 6: Integrační testování MCP

Modul MCP testuje integraci Model Context Protocol pomocí stdio transportu. Tyto testy ověřují, že vaše aplikace může spouštět a komunikovat s MCP servery jako podprocesy.

Testy v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ověřují chování MCP klienta.

**Spuštění:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testovací filozofie

Testujte svůj kód, ne AI. Vaše testy by měly ověřovat kód, který píšete, kontrolou jak jsou vytvářeny prompty, jak je spravována paměť a jak se provádějí nástroje. Odpovědi AI se liší a neměly by být součástí testovacích tvrzení. Zeptejte se sami sebe, zda vaše šablona promptu správně dosazuje proměnné, ne zda AI dává správnou odpověď.

Používejte mocky pro jazykové modely. Jsou to externí závislosti, které jsou pomalé, drahé a nedeterministické. Mockování dělá testy rychlými na milisekundy místo sekund, zdarma bez nákladů na API a deterministickými se stejným výsledkem pokaždé.

Udržujte testy nezávislé. Každý test by měl sám nastavovat data, nespoléhat na jiné testy a po sobě uklízet. Testy by měly projít bez ohledu na pořadí spuštění.

Testujte okrajové případy mimo běžnou cestu. Zkoušejte prázdné vstupy, velmi velké vstupy, speciální znaky, neplatné parametry a hranice. Tyto často odhalí chyby, které normální používání neukáže.

Používejte popisné názvy. Porovnejte `shouldMaintainConversationHistoryAcrossMultipleMessages()` s `test1()`. První přesně říká, co se testuje, což usnadňuje ladění selhání.

## Další kroky

Nyní, když rozumíte testovacím vzorům, ponořte se hlouběji do jednotlivých modulů:

- **[00 - Rychlý start](../00-quick-start/README.md)** - Začněte se základy šablon promptů
- **[01 - Úvod](../01-introduction/README.md)** - Naučte se správě paměti konverzace
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Ovládněte vzory GPT-5.2 promptování
- **[03 - RAG](../03-rag/README.md)** - Vytvářejte systémy s retrieval-augmented generation
- **[04 - Nástroje](../04-tools/README.md)** - Implementujte volání funkcí a řetězení nástrojů
- **[05 - MCP](../05-mcp/README.md)** - Integrace Model Context Protocol

README každého modulu poskytuje podrobné vysvětlení konceptů testovaných zde.

---

**Navigace:** [← Zpět na hlavní stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo mylné výklady vzniklé z používání tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->