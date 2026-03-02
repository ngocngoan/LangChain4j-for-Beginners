# LangChain4j rakenduste testimine

## Sisukord

- [Kiire algus](../../../docs)
- [Mida testid katavad](../../../docs)
- [Testide käivitamine](../../../docs)
- [Testide käivitamine VS Code'is](../../../docs)
- [Testimismustrid](../../../docs)
- [Testimise filosoofia](../../../docs)
- [Järgmised sammud](../../../docs)

See juhend juhendab sind läbi testide, mis näitavad, kuidas testida tehisintellekti rakendusi ilma API võtmete või väliste teenusteta.

## Kiire algus

Käivita kõik testid ühe käsuga:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Kui kõik testid läbivad, peaksid nägema väljundit nagu alloleval ekraanipildil — testid jooksid ilma ühegi veata.

<img src="../../../translated_images/et/test-results.ea5c98d8f3642043.webp" alt="Edukas testide tulemus" width="800"/>

*Edasijõudnud testide käivitamine, kus kõik testid läbisid ilma vigadeta*

## Mida testid katavad

See kursus keskendub **ühikutestidele**, mis jooksevad lokaalselt. Iga test demonstreerib konkreetset LangChain4j kontseptsiooni isoleeritult. Allolev testimise püramiid näitab, kus ühikutestid sobituvad — need moodustavad kiire ja usaldusväärse aluse, millele ülejäänud testistrateegia tugineb.

<img src="../../../translated_images/et/testing-pyramid.2dd1079a0481e53e.webp" alt="Testimise püramiid" width="800"/>

*Testimise püramiid näitab tasakaalu ühikutestide (kiired, isoleeritud), integratsioonitestide (päris komponendid) ja lõpuni-testide vahel. See koolitus käsitleb ühikutestimist.*

| Moodul | Testid | Fookus | Olulised failid |
|--------|--------|--------|-----------------|
| **00 - Kiire algus** | 6 | Käsu mallid ja muutujate asendamine | `SimpleQuickStartTest.java` |
| **01 - Sissejuhatus** | 8 | Vestlusmälu ja olekupõhine vestlus | `SimpleConversationTest.java` |
| **02 - Käsu inseneriteadus** | 12 | GPT-5.2 mustrid, innukuse tasemed, struktureeritud väljund | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumendi importimine, vektorid, sarnasuse otsing | `DocumentServiceTest.java` |
| **04 - Tööriistad** | 12 | Funktsioonikutsed ja tööriistade kettimine | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Mudeli konteksti protokoll StdIO transpordiga | `SimpleMcpTest.java` |

## Testide käivitamine

**Käivita kõik testid juurkataloogist:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Käivita testid konkreetse mooduli jaoks:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Või juurest
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Või juurtest
mvn --% test -pl 01-introduction
```

**Käivita üks testiklass:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Käivita konkreetne testimeetod:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#peaks säilitama vestluse ajaloo
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#peaks säilitama vestluse ajaloo
```

## Testide käivitamine VS Code'is

Kui kasutad Visual Studio Code'i, pakub Test Explorer graafilise liidese testide käivitamiseks ja silumiseks.

<img src="../../../translated_images/et/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Exploreri vaade" width="800"/>

*VS Code Test Explorer näitab testipuu kõigi Java testiklasside ja üksikute testimeetoditega*

**Testide käivitamiseks VS Code'is:**

1. Ava Test Explorer, klõpsates tegevusriba klaasikese ikoonile
2. Laienda testipuu, et näha kõiki mooduleid ja testiklasse
3. Klõpsa käivitusnupul mistahes testi kõrval, et seda üksikult käivitada
4. Klõpsa "Run All Tests", et käivitada kõik testid korraga
5. Paremklõpsa mistahes testil ja vali "Debug Test", et seada murdepunkte ja sammuda koodi läbi

Test Explorer näitab rohelisi linnukesi läbitud testide juures ja kuvab detailseid tõrketeateid vigade korral.

## Testimismustrid

### Muster 1: Käsu mallide testimine

Lihtsaim muster testib käsu malli ilma, et kutsutakse AI mudelit. Kontrollid, et muutujate asendamine toimib korrektselt ja käsud formeeritakse ootuspäraselt.

<img src="../../../translated_images/et/prompt-template-testing.b902758ddccc8dee.webp" alt="Käsu malli testimine" width="800"/>

*Käsu mallide testimine näitab muutujate asenduse protsessi: mall koos kohatäidetega → väärtused rakendatud → vormindatud väljund kontrollitud*

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

See test asub failis `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Käivita see:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testiPromptMallimiseVormindamine
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testiPromptMallideFormaatimine
```

### Muster 2: Keelemudelite nõelimine (mockimine)

Vestlusloogika testimisel kasuta Mockito’t võltsmudelite loomiseks, mis tagastavad etteantud vastuseid. See teeb testid kiired, tasuta ja määratletavad.

<img src="../../../translated_images/et/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Reaalne API võrdlus" width="800"/>

*Võrdlus, miks testimiseks eelistatakse mocke: need on kiired, tasuta, määratletavad ja ei vaja API võtmeid*

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
        assertThat(history).hasSize(6); // 3 kasutaja + 3 tehisintellekti sõnumit
    }
}
```

See muster ilmub failis `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock tagab järjepideva käitumise, et kontrollida mälu haldamist korrektselt.

### Muster 3: Vestluse isoleerimise testimine

Vestlusmälul peab olema võime hoida erinevate kasutajate andmed eraldi. See test kontrollib, et vestlused ei sega kontekste omavahel.

<img src="../../../translated_images/et/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Vestluse isoleerimine" width="800"/>

*Vestluse isoleerimise testimine, näidates eraldi mäluhoidlaid erinevate kasutajate jaoks, et vältida kontekstide juhtumist*

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

Iga vestlus säilitab oma sõltumatu ajaloo. Tootmissüsteemides on see isoleeritus kriitiline mitme kasutajaga rakenduste jaoks.

### Muster 4: Tööriistade sõltumatu testimine

Tööriistad on AI poolt kutsutavad funktsioonid. Testi neid otse, et tagada nende korrektsus sõltumata AI otsustest.

<img src="../../../translated_images/et/tools-testing.3e1706817b0b3924.webp" alt="Tööriistade testimine" width="800"/>

*Tööriistade sõltumatu testimine, kus mock-tööriistad töötavad ilma AI kutseta, kontrollimaks äri loogikat*

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

Need testid failis `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` valideerivad tööriistade loogikat ilma AI kaasamiseta. Kettimise näide näitab, kuidas ühe tööriista väljund suunatakse teise sisendiks.

### Muster 5: Mälu-põhise RAG testimine

RAG süsteemid vajavad tavaliselt vektorandmebaase ja manustusteenuseid. Mälu-põhine muster lubab testida kogu töövoogu ilma väliste sõltuvusteta.

<img src="../../../translated_images/et/rag-testing.ee7541b1e23934b1.webp" alt="Mälu-põhine RAG testimine" width="800"/>

*Mälu-põhise RAG testimise töövoog, kus toimub dokumendi analüüs, manuste salvestus ja sarnasuse otsing ilma andmebaasi nõudmata*

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

See test failist `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` loob dokumendi mälus ja kontrollib tükkideks jaotust ja metaandmete töötlemist.

### Muster 6: MCP integratsioonitestimine

MCP moodul testib Mudeli konteksti protokolli (Model Context Protocol) integreerimist stdio transpordi abil. Need testid kinnitavad, et sinu rakendus suudab käivitada ning suhelda MCP serveritega alamprotsessidena.

Testid failis `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` valideerivad MCP kliendi käitumist.

**Käivita need:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testimise filosoofia

Testeeri oma koodi, mitte AI-d. Sinu testid peaksid valideerima kirjutatud koodi kontrollides, kuidas käske konstrueeritakse, kuidas mälu hallatakse ja kuidas tööriistad täidetakse. AI vastused varieeruvad ega tohiks olla testide väidetes osa. Küsi endalt, kas sinu käsu mall asendab muutujad õigesti, mitte kas AI annab õige vastuse.

Kasuta mocks’i keelemudelite jaoks. Need on välised sõltuvused, mis on aeglased, kulukad ja määratlematud. Mockimine teeb testid kiired (millisekunditega sekundite asemel), tasuta ilma API kuludeta ja määratletavad iga kord sama tulemusega.

Hoidke testid sõltumatud. Iga test peaks seadistama oma andmed, mitte lootma teistele testidele ja pärast oma tööd koristama. Testid peaksid läbima sõltumata käivitamise järjekorrast.

Testeeri äärejuhtumeid üle normaalse kasutuse. Proovi tühje sisendeid, väga suuri sisendeid, erimärke, sobimatuid parameetreid ja piirtingimusi. Need paljastavad sageli vigu, mida tavaline kasutus ei näita.

Kasuta kirjeldavaid nimesid. Võrdle `shouldMaintainConversationHistoryAcrossMultipleMessages()` ja `test1()`. Esimene ütleb täpselt, mida testitakse, tehnikavigade parandamine on palju lihtsam.

## Järgmised sammud

Nüüd, kui sa mõistad testimismustreid, süüvi iga mooduli põhjalikumalt:

- **[00 - Kiire algus](../00-quick-start/README.md)** - Alusta käsu mallide alustest
- **[01 - Sissejuhatus](../01-introduction/README.md)** - Õpi vestlusmälu haldust
- **[02 - Käsu inseneriteadus](../02/prompt-engineering/README.md)** - Master GPT-5.2 käsu mustrid
- **[03 - RAG](../03-rag/README.md)** - Ehita väljatoomisvõimendusega generaatorisüsteeme
- **[04 - Tööriistad](../04-tools/README.md)** - Rakenda funktsioonikutsed ja tööriistade kettimine
- **[05 - MCP](../05-mcp/README.md)** - Integreeri Model Context Protocol

Iga mooduli README annab üksikasjaliku selgituse siin testitavate kontseptsioonide kohta.

---

**Navigeerimine:** [← Tagasi avalehele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun pange tähele, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud võimalikest arusaamatustest või väärarusaamadest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->