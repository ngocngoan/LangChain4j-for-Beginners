# LangChain4j Alkalmazások Tesztelése

## Tartalomjegyzék

- [Gyors Kezdet](../../../docs)
- [Mit Fednek Le a Tesztek](../../../docs)
- [A Tesztek Futtatása](../../../docs)
- [Tesztek Futtatása VS Code-ban](../../../docs)
- [Tesztelési Minták](../../../docs)
- [Tesztelési Filozófia](../../../docs)
- [Következő Lépések](../../../docs)

Ez az útmutató végigvezet a teszteken, amelyek bemutatják, hogyan lehet AI alkalmazásokat tesztelni API kulcsok vagy külső szolgáltatások használata nélkül.

## Gyors Kezdet

Futtasd az összes tesztet egyetlen parancs segítségével:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Ha minden teszt sikeresen lefut, az alábbi képernyőképen láthatóhoz hasonló kimenetet kell látnod — tesztek nulla hibával futnak.

<img src="../../../translated_images/hu/test-results.ea5c98d8f3642043.webp" alt="Sikeres Teszteredmények" width="800"/>

*Sikeres tesztfuttatás, amely mutatja, hogy minden teszt átment, nulla hibával*

## Mit Fednek Le a Tesztek

Ez a kurzus **egységtesztekre** fókuszál, amelyek helyben futnak. Minden teszt egy egyedi LangChain4j koncepciót mutat be elszigetelten. Az alábbi tesztelési piramis megmutatja, hol helyezkednek el az egységtesztek — ők alkotják a gyors, megbízható alapot, amelyre a többi tesztelési stratégia épül.

<img src="../../../translated_images/hu/testing-pyramid.2dd1079a0481e53e.webp" alt="Tesztelési Piramis" width="800"/>

*Tesztelési piramis, amely mutatja az egységtesztek (gyors, elszigetelt), integrációs tesztek (valós komponensek) és end-to-end tesztek egyensúlyát. Ez a képzés az egységtesztelést fedi le.*

| Modul | Tesztek | Fókusz | Kulcsfájlok |
|--------|-------|-------|-----------|
| **00 - Gyors Kezdet** | 6 | Parancssablonok és változó behelyettesítés | `SimpleQuickStartTest.java` |
| **01 - Bevezetés** | 8 | Beszélgetés memória és állapot-alapú chat | `SimpleConversationTest.java` |
| **02 - Prompt Mérnökség** | 12 | GPT-5.2 minták, buzgósági szintek, strukturált kimenet | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentum-beolvasás, beágyazások, hasonlóság keresés | `DocumentServiceTest.java` |
| **04 - Eszközök** | 12 | Függvényhívás és eszközláncolás | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol stdio átvitel használatával | `SimpleMcpTest.java` |

## A Tesztek Futtatása

**Futtasd az összes tesztet a gyökérkönyvtárból:**

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
# Vagy a gyökérből
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Vagy a gyökérből
mvn --% test -pl 01-introduction
```

**Futtass egyedi teszt osztályt:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Futtass egy konkrét tesztmetódust:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#meg kell tartani a beszélgetés előzményeit
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#meg kell őrizni a beszélgetési előzményeket
```

## Tesztek Futtatása VS Code-ban

Ha Visual Studio Code-ot használsz, a Test Explorer grafikus felületet nyújt a tesztek futtatásához és hibakereséséhez.

<img src="../../../translated_images/hu/vscode-testing.f02dd5917289dced.webp" alt="VS Code Teszt Felfedező" width="800"/>

*VS Code Teszt Felfedező, amely mutatja a tesztfát az összes Java tesztosztállyal és egyedi tesztmetódussal*

**Tesztek futtatása VS Code-ban:**

1. Nyisd meg a Test Explorer-t az Activity Bar-ban a lombik ikonra kattintva
2. Bontsd ki a tesztfát, hogy lásd az összes modult és teszt osztályt
3. Kattints a lejátszás gombra bármelyik teszt mellett az egyedüli futtatáshoz
4. Kattints a "Run All Tests"-re az egész tesztcsomag futtatásához
5. Jobb klikk bármely teszten és válaszd a "Debug Test" opciót a breakpointok beállításához és lépésekhez a kódban

A Test Explorer zöld pipákat mutat a sikeres teszteknél, illetve részletes hibajelentést, ha egy teszt megbukik.

## Tesztelési Minták

### Minta 1: Parancssablonok Tesztelése

A legegyszerűbb minta a parancssablonokat teszteli AI modell hívás nélkül. Ellenőrzöd, hogy a változó helyettesítés helyesen működik-e és a promptok az elvárt formátumban vannak-e.

<img src="../../../translated_images/hu/prompt-template-testing.b902758ddccc8dee.webp" alt="Parancssablon Tesztelés" width="800"/>

*Parancssablon tesztelés, amely mutatja a változó helyettesítés folyamatát: sablon helyőrzőkkel → értékekkel kiegészítve → formázott kimenet ellenőrizve*

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

Beszélgetési logika tesztelésekor használd a Mockito-t hogy hamis modelleket hozz létre, amelyek előre meghatározott válaszokat adnak. Ez gyorsabbá, ingyenessé és determinisztikussá teszi a teszteket.

<img src="../../../translated_images/hu/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock és Valós API Összehasonlítás" width="800"/>

*Összehasonlítás, hogy miért előnyösek a mock-ok a teszteléshez: gyorsak, ingyenesek, determinisztikusak, nincs szükség API kulcsokra*

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

Ez a minta megtalálható a `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` fájlban. A mock biztosítja az állandó viselkedést, hogy az emlékezetkezelés helyes működését ellenőrizhesd.

### Minta 3: Beszélgetés Elszigeteltségének Tesztelése

A beszélgetés memóriának külön kell választania a több felhasználót. Ez a teszt ellenőrzi, hogy a beszélgetések nem keverik össze a kontextusokat.

<img src="../../../translated_images/hu/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Beszélgetés Elszigetelés" width="800"/>

*Beszélgetés elszigetelés tesztelése, amely bemutatja, hogy eltérő felhasználók számára külön memóriatárolók vannak, hogy megelőzze a kontextus összekeveredését*

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

Minden beszélgetés saját, független előzményeket tart fenn. Éles rendszerekben ez az elszigetelés kritikus a többfelhasználós alkalmazások esetében.

### Minta 4: Eszközök Független Tesztelése

Az eszközök olyan funkciók, amelyeket az AI hívhat meg. Teszteld őket közvetlenül, hogy biztos legyél benne, hogy jól működnek AI döntések nélkül is.

<img src="../../../translated_images/hu/tools-testing.3e1706817b0b3924.webp" alt="Eszközök Tesztelése" width="800"/>

*Eszközök független tesztelése, amely mutatja a mock eszközök futtatását AI hívások nélkül, hogy ellenőrizze az üzleti logikát*

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

Ezek a tesztek a `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` fájlból valók, amelyek validálják az eszközlogikát AI részvétel nélkül. A láncolási példa azt mutatja, hogyan táplálja egy eszköz kimenete a másik bemenetét.

### Minta 5: Memóriában Futó RAG Tesztelés

A RAG rendszerek hagyományosan vektorigényes adatbázisokat és beágyazási szolgáltatásokat használnak. A memóriában futó minta lehetővé teszi az egész folyamat tesztelését külső függőségek nélkül.

<img src="../../../translated_images/hu/rag-testing.ee7541b1e23934b1.webp" alt="Memóriában Futó RAG Tesztelés" width="800"/>

*Memóriában futó RAG tesztelési munkafolyamat, amely bemutatja a dokumentum feldolgozását, beágyazás tárolást és hasonlóság keresést adatbázis nélkül*

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

Ez a teszt a `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` fájlból kreál a memóriában dokumentumot és ellenőrzi a darabolást és metaadat kezelését.

### Minta 6: MCP Integrációs Tesztelés

Az MCP modul a Model Context Protocol integrációját teszteli stdio átvitel használatával. Ezek a tesztek igazolják, hogy az alkalmazás képes MCP szervereket folyamatként indítani és velük kommunikálni.

Az `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` fájlban lévő tesztek validálják az MCP kliens viselkedését.

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

A kódodat teszteld, ne az AI-t. A tesztjeidnek azt kell ellenőrizniük, hogy a te általad írt kód hogyan működik, például hogy a promptok hogyan épülnek fel, hogyan kezelődik a memória és hogyan működnek az eszközök. Az AI válaszai változóak lehetnek, és nem szabad, hogy a tesztelés tárgyát képezzék. Kérdezd meg inkább, hogy a prompt sablon helyesen helyettesíti-e a változókat, nem pedig azt, hogy az AI jól válaszol-e.

Használj mock-okat a nyelvi modellekhez. Ezek külső függőségek, amelyek lassúak, drágák és nem determinisztikusak. A mockolás gyors, milliszekundumos, ingyenes és mindig azonos eredményt ad.

Tartsd a teszteket függetlennek. Minden teszt állítsa be a saját adatait, ne támaszkodjon más tesztekre, és takarítson maga után. A teszteknek bármilyen futási sorrendben át kell menniük.

Tesztelj szélsőséges eseteket a megszokott útvonalakon túl. Próbálj ki üres bemeneteket, nagyon nagy adatokat, speciális karaktereket, érvénytelen paramétereket és határfeltételeket. Ezek gyakran fednek fel olyan hibákat, amiket a normál használat nem.

Használj leíró neveket. Összehasonlítva a `shouldMaintainConversationHistoryAcrossMultipleMessages()` és a `test1()` neveket: az első pontosan elmondja, hogy mit tesztel, könnyebbé téve a hibák keresését.

## Következő Lépések

Most, hogy megérted a tesztelési mintákat, mélyedj el az egyes modulokban:

- **[00 - Gyors Kezdet](../00-quick-start/README.md)** - Kezdd a parancssablon alapokkal
- **[01 - Bevezetés](../01-introduction/README.md)** - Tanuld meg a beszélgetés memória kezelését
- **[02 - Prompt Mérnökség](../02/prompt-engineering/README.md)** - Sajátítsd el a GPT-5.2 promptolási mintákat
- **[03 - RAG](../03-rag/README.md)** - Építs lekérdezés-kiterjesztett generálási rendszereket
- **[04 - Eszközök](../04-tools/README.md)** - Valósítsd meg a függvényhívást és eszközláncokat
- **[05 - MCP](../05-mcp/README.md)** - Integráld a Model Context Protocol-t

Minden modul README-je részletes magyarázatokat ad a itt tesztelt koncepciókról.

---

**Navigáció:** [← Vissza a Főoldalra](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ezt a dokumentumot az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével fordítottuk le. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások tartalmazhatnak hibákat vagy pontatlanságokat. Az eredeti dokumentum a saját nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén professzionális, emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy helytelen értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->