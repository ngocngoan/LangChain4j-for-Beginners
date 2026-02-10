# Testiranje aplikacij LangChain4j

## Vsebina

- [Hiter začetek](../../../docs)
- [Kaj zajemajo testi](../../../docs)
- [Zagon testov](../../../docs)
- [Zagon testov v VS Code](../../../docs)
- [Vzorce testiranja](../../../docs)
- [Filozofija testiranja](../../../docs)
- [Naslednji koraki](../../../docs)

Ta vodnik vas popelje skozi teste, ki prikazujejo, kako testirati AI aplikacije brez potrebe po API ključih ali zunanjih storitvah.

## Hiter začetek

Zaženite vse teste z eno ukazno vrstico:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/sl/test-results.ea5c98d8f3642043.webp" alt="Uspešni rezultati testov" width="800"/>

*Uspešno izvajanje testov, ki prikazuje, da vsi testi uspešno opravijo brez napak*

## Kaj zajemajo testi

Ta tečaj se osredotoča na **enotske teste**, ki se izvajajo lokalno. Vsak test prikaže specifičen koncept LangChain4j v izolaciji.

<img src="../../../translated_images/sl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testna piramida" width="800"/>

*Testna piramida, ki prikazuje ravnovesje med enotskimi testi (hitro, izolirano), integracijskimi testi (resnične komponente) in end-to-end testi. Ta trening pokriva enotsko testiranje.*

| Modul | Testi | Poudarek | Ključne datoteke |
|--------|-------|-------|-----------|
| **00 - Hiter začetek** | 6 | Predloge pozivov in zamenjava spremenljivk | `SimpleQuickStartTest.java` |
| **01 - Uvod** | 8 | Spomin na pogovor in stanje klepeta | `SimpleConversationTest.java` |
| **02 - Inženiring pozivov** | 12 | Vzorce GPT-5.2, stopnje vneme, strukturiran izhod | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Vnos dokumentov, vdelave, iskanje podobnosti | `DocumentServiceTest.java` |
| **04 - Orodja** | 12 | Klicanje funkcij in povezovanje orodij | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol z uporabo stdio prenosa | `SimpleMcpTest.java` |

## Zagon testov

**Zaženite vse teste iz korenske mape:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Zaženite teste za določen modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ali iz korena
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ali iz korena
mvn --% test -pl 01-introduction
```

**Zaženite en sam test razred:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Zaženite določen testni metod:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#bi morali ohraniti zgodovino pogovora
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#treba ohraniti zgodovino pogovora
```

## Zagon testov v VS Code

Če uporabljate Visual Studio Code, Test Explorer nudi grafični vmesnik za zagon in odpravljanje napak pri testih.

<img src="../../../translated_images/sl/vscode-testing.f02dd5917289dced.webp" alt="Test Explorer v VS Code" width="800"/>

*Test Explorer v VS Code, ki prikazuje drevo testov z vsemi Java testnimi razredi in posameznimi metodami testov*

**Za zagon testov v VS Code:**

1. Odprite Test Explorer tako, da kliknete ikono epruvete v vrstici aktivnosti
2. Razširite drevo testov, da vidite vse module in testne razrede
3. Kliknite gumb za predvajanje ob katerem koli testu, da ga zaženete posamično
4. Kliknite "Run All Tests", da izvedete celoten paket
5. Z desnim klikom na test izberite "Debug Test" za nastavitev točk prekinitve in korak po korak izvajanje kode

Test Explorer prikazuje zelene kljukice za uspešne teste in podrobna sporočila o napakah, ko testi ne uspejo.

## Vzorce testiranja

### Vzorec 1: Testiranje predlog pozivov

Najpreprostejši vzorec testira predloge pozivov brez klica AI modela. Preverite, da zamenjava spremenljivk deluje pravilno in da so pozivi oblikovani kot pričakovano.

<img src="../../../translated_images/sl/prompt-template-testing.b902758ddccc8dee.webp" alt="Testiranje predloge poziva" width="800"/>

*Testiranje predlog pozivov, ki prikazuje potek zamenjave spremenljivk: predloga z mestoma za zamenjavo → uporabljene vrednosti → preverjen oblikovani izhod*

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

Ta test se nahaja v `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Zaženite ga:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#preizkusOblikePredlogePoziva
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testiranjeOblikePredlogeZaPoziv
```

### Vzorec 2: Mockiranje jezikovnih modelov

Pri testiranju logike pogovora uporabite Mockito za ustvarjanje lažnih modelov, ki vračajo vnaprej določene odgovore. To naredi teste hitre, brezplačne in deterministične.

<img src="../../../translated_images/sl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Primerjava lažnih in pravih API" width="800"/>

*Primerjava, ki prikazuje, zakaj so mocki za testiranje boljši: so hitri, brezplačni, deterministični in ne zahtevajo API ključev*

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
        assertThat(history).hasSize(6); // 3 sporočila uporabnika + 3 sporočila AI
    }
}
```

Ta vzorec najdete v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zagotavlja dosledno vedenje, da lahko preverite pravilno upravljanje spomina.

### Vzorec 3: Testiranje izolacije pogovora

Spomin na pogovor mora ohranjati več uporabnikov ločeno. Ta test preveri, da se pogovori ne mešajo.

<img src="../../../translated_images/sl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Izolacija pogovora" width="800"/>

*Testiranje izolacije pogovora, ki prikazuje ločene shrambe spomina za različne uporabnike, da se prepreči mešanje kontekstov*

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

Vsak pogovor vzdržuje svojo neodvisno zgodovino. V produkcijskih sistemih je ta izolacija ključna za aplikacije z več uporabniki.

### Vzorec 4: Neodvisno testiranje orodij

Orodja so funkcije, ki jih AI lahko pokliče. Testirajte jih neposredno, da zagotovite, da delujejo pravilno ne glede na odločitve AI.

<img src="../../../translated_images/sl/tools-testing.3e1706817b0b3924.webp" alt="Testiranje orodij" width="800"/>

*Neodvisno testiranje orodij, ki prikazuje izvajanje lažnih orodij brez klicev AI za preverjanje poslovne logike*

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

Ti testi iz `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` preverjajo logiko orodij brez sodelovanja AI. Primer verižnega povezovanja prikazuje, kako izhod enega orodja napaja vnos drugega.

### Vzorec 5: Testiranje RAG v pomnilniku

Sistemi RAG običajno zahtevajo vektorske baze podatkov in storitve za vdelave. Vzorec v pomnilniku omogoča testiranje celotne pipeline brez zunanjih odvisnosti.

<img src="../../../translated_images/sl/rag-testing.ee7541b1e23934b1.webp" alt="Testiranje RAG v pomnilniku" width="800"/>

*Delovni tok testiranja RAG v pomnilniku, ki prikazuje razčlenjevanje dokumentov, shranjevanje vdelav in iskanje podobnosti brez potrebe po bazi podatkov*

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

Ta test iz `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ustvari dokument v pomnilniku in preveri deljenje na kose ter ravnanje z metapodatki.

### Vzorec 6: Testiranje integracije MCP

Modul MCP testira integracijo Model Context Protocol s pomočjo stdio prenosa. Ti testi preverjajo, da vaša aplikacija lahko zažene in komunicira z MCP strežniki kot podprocesi.

Testi v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` preverjajo vedenje MCP odjemalca.

**Zaženite jih:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filozofija testiranja

Testirajte svojo kodo, ne AI. Vaši testi naj potrjujejo kodo, ki jo napišete, tako da preverjajo, kako so pozivi konstruirani, kako se upravlja spomin in kako delujejo orodja. Odgovori AI se razlikujejo in ne bi smeli biti del trditev testov. Vprašajte se, ali vaša predloga poziva pravilno nadomešča spremenljivke, ne ali AI poda pravilen odgovor.

Uporabljajte mocke za jezikovne modele. So zunanje odvisnosti, ki so počasne, drage in nedeterministične. Mockiranje naredi teste hitre, znotraj milisekund namesto sekund, brezplačne brez stroškov API in deterministične z istim rezultatom vsakič.

Ohranjajte teste neodvisne. Vsak test naj nastavi svoje podatke, naj se ne zanese na druge teste in se po koncu sam očisti. Testi naj uspejo ne glede na vrstni red izvajanja.

Testirajte robne primere onkraj "srečne poti". Preizkusite prazne vnose, zelo velike vnose, posebne znake, neveljavne parametre in mejne pogoje. Ti pogosto razkrijejo napake, ki jih običajna uporaba ne pokaže.

Uporabljajte opisna imena. Primerjajte `shouldMaintainConversationHistoryAcrossMultipleMessages()` z `test1()`. Prvi vam natančno pove, kaj se testira, kar olajša odpravljanje napak pri neuspehu.

## Naslednji koraki

Zdaj, ko razumete vzorce testiranja, se poglobite v posamezen modul:

- **[00 - Hiter začetek](../00-quick-start/README.md)** - Začnite s temelji predlog pozivov
- **[01 - Uvod](../01-introduction/README.md)** - Spoznajte upravljanje spomina pogovora
- **[02 - Inženiring pozivov](../02/prompt-engineering/README.md)** - Obvladujte vzorce pozivov GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Gradite sisteme za generacijo z iskanjem informacij
- **[04 - Orodja](../04-tools/README.md)** - Implementirajte klicanje funkcij in verižna orodja
- **[05 - MCP](../05-mcp/README.md)** - Integrirajte Model Context Protocol

Vsak modulov README vsebuje podrobna pojasnila konceptov, ki jih tukaj testiramo.

---

**Navigacija:** [← Nazaj na glavno](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo zagotoviti natančnost, upoštevajte, da avtomatski prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v materinem jeziku velja za avtoritativni vir. Za pomembne informacije priporočamo strokoven človeški prevod. Nismo odgovorni za morebitne nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->