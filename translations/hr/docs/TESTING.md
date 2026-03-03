# Testiranje LangChain4j aplikacija

## Sadržaj

- [Brzi početak](../../../docs)
- [Što testovi pokrivaju](../../../docs)
- [Pokretanje testova](../../../docs)
- [Pokretanje testova u VS Code](../../../docs)
- [Obrasci testiranja](../../../docs)
- [Filozofija testiranja](../../../docs)
- [Daljnji koraci](../../../docs)

Ovaj vodič vodi vas kroz testove koji pokazuju kako testirati AI aplikacije bez potrebe za API ključevima ili vanjskim servisima.

## Brzi početak

Pokrenite sve testove s jednom naredbom:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Kada svi testovi prođu, trebali biste vidjeti izlaz poput donje snimke zaslona — testovi se izvršavaju bez grešaka.

<img src="../../../translated_images/hr/test-results.ea5c98d8f3642043.webp" alt="Uspješni rezultati testiranja" width="800"/>

*Uspješno izvršavanje testova koje pokazuje da svi testovi prolaze bez grešaka*

## Što testovi pokrivaju

Ovaj tečaj se fokusira na **jedinične testove** koji se izvode lokalno. Svaki test prikazuje određeni LangChain4j koncept u izolaciji. Testna piramida ispod prikazuje gdje se jedinični testovi uklapaju — oni čine brzu, pouzdanu osnovu na kojoj se gradi ostatak vaše test strategije.

<img src="../../../translated_images/hr/testing-pyramid.2dd1079a0481e53e.webp" alt="Testna piramida" width="800"/>

*Testna piramida koja prikazuje ravnotežu između jediničnih testova (brzi, izolirani), integracijskih testova (stvarne komponente) i end-to-end testova. Ova obuka pokriva jedinično testiranje.*

| Modul | Testovi | Fokus | Ključne datoteke |
|--------|-------|-------|-----------|
| **00 - Brzi početak** | 6 | Predlošci promptova i zamjena varijabli | `SimpleQuickStartTest.java` |
| **01 - Uvod** | 8 | Memorija razgovora i chat s održavanjem stanja | `SimpleConversationTest.java` |
| **02 - Inženjering promptova** | 12 | GPT-5.2 obrasci, razine spremnosti, strukturirani izlaz | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Unos dokumenata, ugradnje, pretraživanje sličnosti | `DocumentServiceTest.java` |
| **04 - Alati** | 12 | Pozivanje funkcija i povezivanje alata | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol sa stdio transportom | `SimpleMcpTest.java` |

## Pokretanje testova

**Pokreni sve testove iz korijena:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Pokreni testove za određeni modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ili iz korijena
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ili iz root-a
mvn --% test -pl 01-introduction
```

**Pokreni jednu test klasu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Pokreni određenu test metodu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#trebaOdržavatiPovijestRazgovora
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#trebaOdržavatiPovijestRazgovora
```

## Pokretanje testova u VS Code

Ako koristite Visual Studio Code, Test Explorer pruža grafičko sučelje za pokretanje i otklanjanje pogrešaka u testovima.

<img src="../../../translated_images/hr/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer prikazuje stablo testova sa svim Java test klasama i pojedinačnim test metodama*

**Za pokretanje testova u VS Code:**

1. Otvorite Test Explorer klikom na ikonu epruvete u Activity Bar-u
2. Proširite stablo testova da vidite sve module i test klase
3. Kliknite gumb za reprodukciju pored bilo kojeg testa za pojedinačno pokretanje
4. Kliknite "Run All Tests" za pokretanje cijelog skupa
5. Desni klik na bilo koji test i odaberite "Debug Test" za postavljanje breakpoints i korak po korak praćenje koda

Test Explorer prikazuje zelene kvačice za prolazne testove i pruža detaljne poruke o greškama kada testovi zakažu.

## Obrasci testiranja

### Obrasc 1: Testiranje predložaka promptova

Najjednostavniji obrazac testira predloške promptova bez poziva bilo kojeg AI modela. Provjeravate da li zamjena varijabli radi ispravno i da su promptovi formatirani kako se očekuje.

<img src="../../../translated_images/hr/prompt-template-testing.b902758ddccc8dee.webp" alt="Testiranje predložaka promptova" width="800"/>

*Testiranje predložaka promptova koje pokazuje tijek zamjene varijabli: predložak s rezerviranim mjestima → primjenjene vrijednosti → provjera formatiranog izlaza*

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

Ovaj test se nalazi u `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Pokrenite ga:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testirajFormatiranjePredloškaUpita
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testiranjeFormatiranjaPredloškaUpita
```

### Obrasc 2: Mockiranje jezičnih modela

Pri testiranju logike razgovora, koristite Mockito za stvaranje lažnih modela koji vraćaju unaprijed određene odgovore. Time testovi postaju brzi, besplatni i deterministički.

<img src="../../../translated_images/hr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Usporedba lažnog i stvarnog API-ja" width="800"/>

*Usporedba koja pokazuje zašto su mockovi preferirani za testiranje: brzi, besplatni, deterministički i ne zahtijevaju API ključeve*

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
        assertThat(history).hasSize(6); // 3 korisnička + 3 AI poruke
    }
}
```

Ovaj obrazac se pojavljuje u `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock osigurava dosljedno ponašanje kako biste mogli provjeriti ispravno upravljanje memorijom.

### Obrasc 3: Testiranje izolacije razgovora

Memorija razgovora mora držati korisnike odvojene. Ovaj test provjerava da se konteksti ne miješaju u razgovorima.

<img src="../../../translated_images/hr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Izolacija razgovora" width="800"/>

*Testiranje izolacije razgovora prikazuje odvojene memorijske jedinice za različite korisnike kako bi se spriječilo miješanje konteksta*

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

Svaki razgovor održava vlastitu neovisnu povijest. U produkcijskim sustavima ova izolacija je ključna za aplikacije s više korisnika.

### Obrasc 4: Testiranje alata neovisno

Alati su funkcije koje AI može pozvati. Testirajte ih izravno kako biste osigurali da rade ispravno neovisno o AI odlukama.

<img src="../../../translated_images/hr/tools-testing.3e1706817b0b3924.webp" alt="Testiranje alata" width="800"/>

*Testiranje alata neovisno prikazuje izvođenje mock alata bez poziva AI-ja radi provjere logike poslovanja*

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

Ovi testovi iz `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` potvrđuju logiku alata bez uključenja AI-ja. Primjer povezivanja pokazuje kako izlaz jednog alata ulazi u drugi.

### Obrasc 5: Testiranje RAG-a u memoriji

RAG sustavi tradicionalno zahtijevaju vektorske baze podataka i servise za ugradnje. Uz obrazac u memoriji možete testirati cijeli proces bez vanjskih ovisnosti.

<img src="../../../translated_images/hr/rag-testing.ee7541b1e23934b1.webp" alt="Testiranje RAG-a u memoriji" width="800"/>

*Radni proces testiranja RAG-a u memoriji prikazuje parsiranje dokumenata, pohranu ugradnji i pretraživanje sličnosti bez potrebe za bazom podataka*

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

Ovaj test iz `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` stvara dokument u memoriji i provjerava segmentaciju i rukovanje metapodacima.

### Obrasc 6: Integracijsko testiranje MCP-a

MCP modul testira integraciju Model Context Protocola koristeći stdio transport. Ovi testovi potvrđuju da vaša aplikacija može pokretati i komunicirati s MCP serverima kao podprocesima.

Testovi u `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` potvrđuju ponašanje MCP klijenta.

**Pokrenite ih:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filozofija testiranja

Testirajte svoj kod, ne AI. Vaši bi testovi trebali potvrditi kod koji pišete tako što provjeravaju kako se konstruiraju promptovi, kako se upravlja memorijom i kako se izvršavaju alati. Odgovori AI-ja variraju i ne bi trebali biti dio testnih tvrdnji. Pitajte se je li vaš predložak prompta ispravno zamijenio varijable, a ne daje li AI pravi odgovor.

Koristite mockove za jezične modele. Oni su vanjske ovisnosti koje su spore, skupe i nedeterminističke. Mockiranje čini testove brzim, sa stotinama milisekundi umjesto sekundi, besplatnim bez troškova API-ja i determinističkim s istim rezultatom svaki put.

Držite testove neovisnima. Svaki test bi trebao sam postaviti svoje podatke, ne oslanjati se na druge testove i čisto ih očistiti. Testovi bi trebali prolaziti bez obzira na redoslijed izvršavanja.

Testirajte rubne slučajeve osim ugodnog toka. Isprobajte prazne ulaze, vrlo velike ulaze, posebne znakove, nevaljane parametre i granične uvjete. Oni često otkrivaju greške koje normalna upotreba ne pokazuje.

Koristite deskriptivne nazive. Usporedite `shouldMaintainConversationHistoryAcrossMultipleMessages()` s `test1()`. Prvi vam točno govori što se testira, što olakšava otklanjanje pogrešaka.

## Daljnji koraci

Sada kada razumijete obrasce testiranja, dublje zaronite u svaki modul:

- **[00 - Brzi početak](../00-quick-start/README.md)** - Počnite s osnovama predložaka promptova
- **[01 - Uvod](../01-introduction/README.md)** - Naučite upravljanje memorijom razgovora
- **[02 - Inženjering promptova](../02/prompt-engineering/README.md)** - Ovladavanje GPT-5.2 obrascima promptova
- **[03 - RAG](../03-rag/README.md)** - Izgradite sustave za generiranje uz pojačanu pretraživost
- **[04 - Alati](../04-tools/README.md)** - Implementirajte pozivanje funkcija i lančane alate
- **[05 - MCP](../05-mcp/README.md)** - Integrirajte Model Context Protocol

README svakog modula pruža detaljna objašnjenja koncepata prikazanih ovdje.

---

**Navigacija:** [← Povratak na glavni](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o odricanju od odgovornosti**:  
Ovaj dokument preveo je AI servis za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba se smatrati službenim i autoritativnim izvorom. Za kritične informacije preporučujemo profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakva nesporazuma ili pogrešna tumačenja koja proizlaze iz upotrebe ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->