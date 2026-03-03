# Testování aplikací LangChain4j

## Obsah

- [Rychlý start](../../../docs)
- [Co testy pokrývají](../../../docs)
- [Spouštění testů](../../../docs)
- [Spouštění testů ve VS Code](../../../docs)
- [Testovací vzory](../../../docs)
- [Filozofie testování](../../../docs)
- [Další kroky](../../../docs)

Tato příručka vás provede testy, které ukazují, jak testovat AI aplikace bez potřeby API klíčů nebo externích služeb.

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
  
Když všechny testy projdou, měli byste vidět výstup podobný následujícímu screenshotu — testy proběhnou bez chyb.

<img src="../../../translated_images/cs/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Úspěšné spuštění testů, kde všechny testy prošly bez chyb*

## Co testy pokrývají

Tento kurz se zaměřuje na **jednotkové testy**, které běží lokálně. Každý test ukazuje konkrétní koncept LangChain4j izolovaně. Níže uvedená testovací pyramida ukazuje, kam jednotkové testy zapadají — tvoří rychlý, spolehlivý základ, na kterém je postavena zbytek vaší testovací strategie.

<img src="../../../translated_images/cs/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testovací pyramida ukazující vyváženost mezi jednotkovými testy (rychlé, izolované), integračními testy (reálné komponenty) a end-to-end testy. Tento kurz pokrývá jednotkové testování.*

| Modul | Testy | Zaměření | Klíčové soubory |
|--------|-------|----------|-----------------|
| **00 - Rychlý start** | 6 | Šablony promptů a substituce proměnných | `SimpleQuickStartTest.java` |
| **01 - Úvod** | 8 | Paměť konverzace a stavový chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Vzory GPT-5.2, úrovně ochoty, strukturovaný výstup | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Zpracování dokumentů, embeddingy, vyhledávání podobnosti | `DocumentServiceTest.java` |
| **04 - Nástroje** | 12 | Vytváření funkcí a řetězení nástrojů | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protokol modelového kontextu s stdio transportem | `SimpleMcpTest.java` |

## Spouštění testů

**Spustit všechny testy z kořenové složky:**

**Bash:**  
```bash
mvn test
```
  
**PowerShell:**  
```powershell
mvn --% test
```
  

**Spustit testy pro konkrétní modul:**

**Bash:**  
```bash
cd 01-introduction && mvn test
# Nebo z kořene
mvn test -pl 01-introduction
```
  
**PowerShell:**  
```powershell
cd 01-introduction; mvn --% test
# Nebo z kořene
mvn --% test -pl 01-introduction
```


**Spustit jednu testovací třídu:**

**Bash:**  
```bash
mvn test -Dtest=SimpleConversationTest
```
  
**PowerShell:**  
```powershell
mvn --% test -Dtest=SimpleConversationTest
```
  

**Spustit konkrétní testovací metodu:**

**Bash:**  
```bash
mvn test -Dtest=SimpleConversationTest#měl by udržovat historii konverzace
```
  
**PowerShell:**  
```powershell
mvn --% test -Dtest=SimpleConversationTest#měl by udržovat historii konverzace
```
  

## Spouštění testů ve VS Code

Pokud používáte Visual Studio Code, Test Explorer poskytuje grafické rozhraní pro spouštění a ladění testů.

<img src="../../../translated_images/cs/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer ve VS Code ukazuje strom testů se všemi Java testovacími třídami a jednotlivými testovacími metodami*

**Pro spuštění testů ve VS Code:**

1. Otevřete Test Explorer kliknutím na ikonu zkumavky v aktivitním panelu  
2. Rozbalte strom testů, abyste viděli všechny moduly a testovací třídy  
3. Klikněte na tlačítko pro přehrání vedle libovolného testu pro jeho individuální spuštění  
4. Klikněte na "Run All Tests" pro spuštění celé sady  
5. Pravým klikem na test vyberte "Debug Test" pro nastavení breakpointů a krokování kódem

Test Explorer zobrazuje zelené fajfky u úspěšných testů a poskytuje podrobné zprávy o chybách, jsou-li testy neúspěšné.

## Testovací vzory

### Vzor 1: Testování šablon promptů

Nejjednodušší vzor testuje šablony promptů bez volání AI modelu. Ověřujete, že substituce proměnných funguje správně a prompt je formátován očekávaným způsobem.

<img src="../../../translated_images/cs/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testování šablon promptů, ukazující průtok substituce hodnot: šablona s místy pro vložení hodnot → aplikovaná hodnota → ověřený formátovaný výstup*

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
  
Tento test je v souboru `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Spuštění:**

**Bash:**  
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testFormátováníŠablonyVýzvy
```
  
**PowerShell:**  
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testováníFormátováníŠablonyVýzvy
```
  

### Vzor 2: Mockování jazykových modelů

Při testování logiky konverzace použijte Mockito k vytvoření falešných modelů, které vrací předem dané odpovědi. Díky tomu jsou testy rychlé, zdarma a deterministické.

<img src="../../../translated_images/cs/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Porovnání ukazující, proč jsou mockety preferovány při testování: jsou rychlé, zdarma, deterministické a nevyžadují API klíče*

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
  
Tento vzor je použit v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zajišťuje konzistentní chování, takže lze ověřit správné řízení paměti.

### Vzor 3: Testování izolace konverzace

Paměť konverzace musí udržovat uživatele oddělené. Tento test ověřuje, že se konverzace nekombinují.

<img src="../../../translated_images/cs/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testování izolace konverzace ukazuje oddělené úložiště paměti pro různé uživatele, aby nedocházelo ke křížení kontextů*

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
  
Každá konverzace si udržuje vlastní nezávislou historii. V produkčních systémech je tato izolace kritická pro multi-uživatelské aplikace.

### Vzor 4: Testování nástrojů nezávisle

Nástroje jsou funkce, které AI může volat. Testujte je přímo, abyste zajistili správnost bez ohledu na rozhodnutí AI.

<img src="../../../translated_images/cs/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testování nástrojů samostatně ukazující provádění mock nástrojů bez volání AI pro ověření obchodní logiky*

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
  
Tyto testy z `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ověřují logiku nástrojů bez zapojení AI. Příklad řetězení ukazuje, jak výstup jednoho nástroje jde jako vstup do dalšího.

### Vzor 5: Testování RAG v paměti

RAG systémy tradičně vyžadují vektorové databáze a embedding služby. Vzor v paměti vám umožňuje testovat celý proces bez externích závislostí.

<img src="../../../translated_images/cs/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Testovací workflow RAG v paměti ukazující parsování dokumentu, ukládání embeddingů a vyhledávání podle podobnosti bez potřeby databáze*

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
  
Tento test z `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` vytvoří dokument v paměti a ověřuje rozdělování na části a zpracování metadat.

### Vzor 6: Integrační testování MCP

Modul MCP testuje integraci Model Context Protokolu pomocí stdio transportu. Tyto testy ověřují, že vaše aplikace může spouštět MCP servery jako podprocesy a komunikovat s nimi.

Testy v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validují chování klienta MCP.

**Spuštění:**

**Bash:**  
```bash
cd 05-mcp && mvn test
```
  
**PowerShell:**  
```powershell
cd 05-mcp; mvn --% test
```
  

## Filozofie testování

Testujte svůj kód, ne AI. Vaše testy by měly ověřovat kód, který píšete, kontrolovat, jak jsou prompt šablony konstruovány, jak je řízena paměť a jak se vykonávají nástroje. Odpovědi AI se liší a neměly by být součástí testovacích ověření. Ptejte se, zda vaše šablona správně nahrazuje proměnné, ne jestli AI dává správnou odpověď.

Používejte mocky pro jazykové modely. Jsou to externí závislosti, které jsou pomalé, drahé a nedeterministické. Mockování dělá testy rychlými na milisekundy místo sekund, zdarma bez nákladů na API a deterministickými se stejným výsledkem pokaždé.

Udržujte testy nezávislé. Každý test by měl nastavit vlastní data, nespoléhat na jiné testy a sám si po sobě uklidit. Testy by měly projít bez ohledu na pořadí spouštění.

Testujte okrajové případy nad rámec šťastné cesty. Zkoušejte prázdné vstupy, velmi velké vstupy, speciální znaky, neplatné parametry a hranice. Tyto často odhalí chyby, které běžné použití nevykáže.

Používejte popisná jména. Porovnejte `shouldMaintainConversationHistoryAcrossMultipleMessages()` a `test1()`. První přesně říká, co se testuje, což usnadňuje ladění chyb.

## Další kroky

Nyní, když rozumíte testovacím vzorům, ponořte se hlouběji do každého modulu:

- **[00 - Rychlý start](../00-quick-start/README.md)** - Začněte se základy šablon promptů  
- **[01 - Úvod](../01-introduction/README.md)** - Naučte se řízení paměti konverzace  
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Ovládněte vzory pro GPT-5.2  
- **[03 - RAG](../03-rag/README.md)** - Vytvářejte retrieval-augmented generation systémy  
- **[04 - Nástroje](../04-tools/README.md)** - Implementujte volání funkcí a řetězce nástrojů  
- **[05 - MCP](../05-mcp/README.md)** - Integrujte Model Context Protocol  

Každý modul má svůj README s podrobnými vysvětleními konceptů, které zde byly testovány.

---

**Navigace:** [← Zpět na hlavní stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neručíme za jakékoli nedorozumění nebo nesprávné výklady vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->