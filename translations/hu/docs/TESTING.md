# LangChain4j Alkalmazások Tesztelése

## Tartalomjegyzék

- [Gyors Kezdés](../../../docs)
- [Mit Fednek Le a Tesztek](../../../docs)
- [A Tesztek Futtatása](../../../docs)
- [Tesztek Futtatása VS Code-ban](../../../docs)
- [Tesztelési Minták](../../../docs)
- [Tesztelési Filozófia](../../../docs)
- [Következő Lépések](../../../docs)

Ez az útmutató végigvezet a teszteken, amelyek bemutatják, hogyan lehet AI alkalmazásokat tesztelni API kulcsok vagy külső szolgáltatások igénye nélkül.

## Gyors Kezdés

Futtasd az összes tesztet egyetlen parancssal:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/hu/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Sikeres tesztfuttatás, minden teszt hibátlanul teljesítve*

## Mit Fednek Le a Tesztek

Ez a kurzus a **unit tesztekre** koncentrál, amelyek lokálisan futnak. Minden teszt egy adott LangChain4j koncepciót mutat be elszigetelten.

<img src="../../../translated_images/hu/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Tesztelési piramis, amely bemutatja az egység tesztek (gyorsak, elszigeteltek), integrációs tesztek (valódi komponensek) és end-to-end tesztek közti egyensúlyt. Ez a képzés az egységtesztelést fedi le.*

| Modul | Tesztek | Fókusz | Kulcs fájlok |
|--------|-------|-------|-----------|
| **00 - Gyors Kezdés** | 6 | Prompt sablonok és változó behelyettesítés | `SimpleQuickStartTest.java` |
| **01 - Bevezetés** | 8 | Beszélgetés memória és állapotkezelés | `SimpleConversationTest.java` |
| **02 - Prompt Mérnökség** | 12 | GPT-5.2 minták, lelkesedési szintek, strukturált kimenet | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentumfeldolgozás, beágyazások, hasonlóság keresés | `DocumentServiceTest.java` |
| **04 - Eszközök** | 12 | Függvényhívások és eszköz láncolás | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol stdio transporttal | `SimpleMcpTest.java` |

## A Tesztek Futtatása

**Futtasd az összes tesztet a gyökérből:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Futtass teszteket egy adott modulra:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Vagy a root könyvtárból
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Vagy a rootból
mvn --% test -pl 01-introduction
```

**Futtass egyetlen teszt osztályt:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Futtass egy adott teszt metódust:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#meg kell őrizni a beszélgetés előzményeit
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#meg kell őrizni a beszélgetés előzményeit
```


## Tesztek Futtatása VS Code-ban

Ha Visual Studio Code-ot használsz, a Test Explorer grafikus felületet biztosít a tesztek futtatásához és hibakereséséhez.

<img src="../../../translated_images/hu/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer, amely mutatja a tesztfa struktúrát, minden Java teszt osztállyal és egyedi teszt metódussal*

**Tesztek futtatása VS Code-ban:**

1. Nyisd meg a Test Explorer-t az Activity Bar-ban a lombik ikonra kattintva
2. Bontsd ki a tesztfát, hogy lásd az összes modult és teszt osztályt
3. Kattints egy tesztnél a lejátszás gombra az egyéni futtatáshoz
4. Kattints a "Run All Tests" gombra az összes futtatásához
5. Jobb kattintás egy teszten, válaszd a "Debug Test" lehetőséget töréspont beállításhoz és kód lépésenkénti követéséhez

A Test Explorer zöld pipát mutat a sikeres teszteknél és részletes hibaüzeneteket nyújt, ha egy teszt megbukik.

## Tesztelési Minták

### Minta 1: Prompt Sablonok Tesztelése

A legegyszerűbb minta prompt sablonokat tesztel, anélkül, hogy AI modellt hívna meg. Ellenőrzöd, hogy a változó behelyettesítés helyes és a promptok a várt formátumban vannak.

<img src="../../../translated_images/hu/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Prompt sablonok tesztelése, amely bemutatja a változó behelyettesítés folyamatát: sablon helyőrzőkkel → értékek hozzárendelve → formázott kimenet ellenőrizve*

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

Ez a teszt a `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` fájlban található.

**Futtasd:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#tesztPromptSablonFormázás
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#tesztPromptSablonFormázás
```


### Minta 2: Nyelvi Modellek Mockolása

Beszélgetés logika tesztelésekor használd a Mockito-t, hogy hamis modelleket hozz létre, melyek előre meghatározott válaszokat adnak. Ez a teszteket gyorssá, ingyenessé és determinisztikussá teszi.

<img src="../../../translated_images/hu/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Összehasonlítás, hogy miért előnyösebb a mock használata tesztekhez: gyors, ingyenes, determinisztikus és nem igényel API kulcsot*

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
        assertThat(history).hasSize(6); // 3 felhasználói + 3 MI üzenet
    }
}
```

Ez a minta az `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` fájlban jelenik meg. A mock biztosítja az állandó viselkedést, így ellenőrizheted, hogy a memória kezelés megfelelő.

### Minta 3: Beszélgetés Izolációjának Tesztelése

A beszélgetés memóriának több felhasználót külön kell tartania. Ez a teszt ellenőrzi, hogy a kontextusok nem keverednek.

<img src="../../../translated_images/hu/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Beszélgetés izolációjának tesztelése, amely külön memóriatárolókat mutat felhasználónként a kontextus összekeveredésének elkerülésére*

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

Minden beszélgetés saját, független történettel rendelkezik. Éles rendszerekben ez az izoláció kritikus a többfelhasználós alkalmazások számára.

### Minta 4: Eszközök Független Tesztelése

Az eszközök olyan függvények, amelyeket az AI hívhat. Teszteld őket közvetlenül, hogy működésük független legyen az AI döntésektől.

<img src="../../../translated_images/hu/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Eszközök független tesztelése, amely bemutatja a mock eszköz végrehajtását AI hívások nélkül, az üzleti logika ellenőrzéséhez*

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

Ezek a tesztek a `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` fájlból valók, amelyek validálják az eszköz logikáját AI nélkül. A láncolás példája megmutatja, hogyan táplálja az egyik eszköz kimenete a másik bemenetét.

### Minta 5: Memóriában Történő RAG Tesztelés

A RAG rendszerek hagyományosan vektor adatbázisokat és beágyazási szolgáltatásokat igényelnek. A memóriában végzett minta lehetővé teszi, hogy a teljes folyamatot külső függőségek nélkül teszteld.

<img src="../../../translated_images/hu/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Memóriában végzett RAG tesztelési folyamat bemutatása, ahol dokumentum elemzés, beágyazás tárolás és hasonlóság keresés történik adatbázis nélkül*

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

Ez a teszt az `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` fájlból állít elő egy dokumentumot memóriában, és ellenőrzi a darabolást és metaadat kezelését.

### Minta 6: MCP Integrációs Tesztelés

Az MCP modul teszteli a Model Context Protocol integrációt stdio transzport használatával. Ezek a tesztek ellenőrzik, hogy az alkalmazás képes-e MCP szervereket szubfolyamatként indítani és kommunikálni velük.

Az `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` tesztek validálják az MCP kliens működését.

**Futtasd őket:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```


## Tesztelési Filozófia

A kódodat teszteld, ne az AI-t. A teszteket arra használd, hogy validáld a kódodat: ellenőrizd, hogyan épülnek fel a promptok, hogyan kezelődik a memória, és miként hajtódnak végre az eszközök. Az AI válaszok változékonyak, és nem lehet belőlük következtetéseket levonni a tesztelések során. Inkább azt teszteld, hogy a prompt sablon helyesen helyettesíti-e be a változókat, ne azt, hogy az AI a helyes választ adja-e.

Használj mockokat a nyelvi modellekhez. Ezek külső függőségek, amelyek lassúak, drágák és nem determinisztikusak. A mockolás gyors teszteket eredményez, milliszekundumok alatt, díjmentesen, és mindig ugyanaz az eredmény jön létre.

Tartsd függetlenül a teszteket. Minden teszt állítsa be a saját adatait, ne függjön más tesztektől, és takarítsa el magát. A teszteknek a futás sorrendjétől függetlenül is sikeresnek kell lenniük.

Tesztelj szélsőséges eseteket is, ne csak a jó utat. Próbálj ki üres bemeneteket, nagyon nagy bemeneteket, speciális karaktereket, érvénytelen paramétereket és szélsőértékeket. Ezek gyakran fedik fel azokat a hibákat, amelyeket a normál használat nem.

Használj leíró neveket. Hasonlítsd össze a `shouldMaintainConversationHistoryAcrossMultipleMessages()` és a `test1()` neveket. Az első pontosan leírja mit tesztel, így a hibakeresés sokkal egyszerűbb.

## Következő Lépések

Most, hogy érintetted a tesztelési mintákat, mélyedj el minden modulban:

- **[00 - Gyors Kezdés](../00-quick-start/README.md)** - Kezdj a prompt sablon alapokkal
- **[01 - Bevezetés](../01-introduction/README.md)** - Tanulj a beszélgetés memória kezeléséről
- **[02 - Prompt Mérnökség](../02-prompt-engineering/README.md)** - Sajátítsd el a GPT-5.2 prompt mintákat
- **[03 - RAG](../03-rag/README.md)** - Építs lekérdezés alapú generálási rendszereket
- **[04 - Eszközök](../04-tools/README.md)** - Valósíts meg függvényhívásokat és eszköz láncokat
- **[05 - MCP](../05-mcp/README.md)** - Integráld a Model Context Protocol-t

Minden modul README-je részletes magyarázatot ad az itt tesztelt koncepciókról.

---

**Navigáció:** [← Vissza a főoldalra](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősség kizárása**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti, anyanyelvi dokumentum tekintendő hiteles forrásnak. Fontos információk esetén szakmai, emberi fordítást javaslunk. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->