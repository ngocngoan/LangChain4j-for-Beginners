# Testiranje aplikacij LangChain4j

## Kazalo vsebine

- [Hitri začetek](../../../docs)
- [Kaj pokrivajo testi](../../../docs)
- [Zagon testov](../../../docs)
- [Zagon testov v VS Code](../../../docs)
- [Vzorec testiranja](../../../docs)
- [Filozofija testiranja](../../../docs)
- [Naslednji koraki](../../../docs)

Ta vodič vas vodi skozi teste, ki prikazujejo, kako testirati AI aplikacije brez potrebe po API ključih ali zunanjih storitvah.

## Hitri začetek

Zaženite vse teste z eno samo ukazno vrstico:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Ko vsi testi uspešno opravijo, bi morali videti izhod, podoben spodnjemu posnetku zaslona — testi se izvajajo brez napak.

<img src="../../../translated_images/sl/test-results.ea5c98d8f3642043.webp" alt="Uspešni rezultati testov" width="800"/>

*Uspešno izvršeni testi, ki kažejo vse teste z nič napakami*

## Kaj pokrivajo testi

Ta tečaj se osredotoča na **enotske teste**, ki se izvajajo lokalno. Vsak test prikaže določen koncept LangChain4j izolirano. Spodnja piramida testiranja prikazuje, kje enotni testi sodijo — tvorijo hitro, zanesljivo osnovo, na kateri temelji preostala testna strategija.

<img src="../../../translated_images/sl/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramida testiranja" width="800"/>

*Piramida testiranja, ki prikazuje ravnotežje med enotskimi testi (hitro, izolirano), integracijskimi testi (pravi komponenti) in end-to-end testi. Ta usposabljanje pokriva enotsko testiranje.*

| Modul | Testi | Osredotočenost | Ključne datoteke |
|--------|-------|---------------|------------------|
| **00 - Hitri začetek** | 6 | Predloge pozivov in zamenjava spremenljivk | `SimpleQuickStartTest.java` |
| **01 - Uvod** | 8 | Spomin pogovora in stanje klepeta | `SimpleConversationTest.java` |
| **02 - Inženiring pozivov** | 12 | Vzorce GPT-5.2, stopnje vneme, strukturiran izhod | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Vnos dokumentov, vgrajene predstavitve, iskanje podobnosti | `DocumentServiceTest.java` |
| **04 - Orodja** | 12 | Kličem funkcije in verižim orodja | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protokol modelnega konteksta s stdio prenosom | `SimpleMcpTest.java` |

## Zagon testov

**Zaženi vse teste iz korena:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Zaženi teste za določen modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ali iz root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ali iz korena
mvn --% test -pl 01-introduction
```

**Zaženi posamezen testni razred:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Zaženi specifično testno metodo:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#naj ohrani zgodovino pogovora
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#naj vzdržuje zgodovino pogovora
```

## Zagon testov v VS Code

Če uporabljate Visual Studio Code, Test Explorer nudi grafični vmesnik za zagon in odpravljanje napak testov.

<img src="../../../translated_images/sl/vscode-testing.f02dd5917289dced.webp" alt="Test Explorer v VS Code" width="800"/>

*Test Explorer v VS Code prikazuje drevo testov z vsemi Java testnimi razredi in posameznimi testnimi metodami*

**Za zagon testov v VS Code:**

1. Odprite Test Explorer s klikom na ikono stekleničke na vrstici aktivnosti
2. Razširite drevo testov, da vidite vse module in testne razrede
3. Kliknite gumb za predvajanje poleg katerega koli testa, da ga zaženete posamično
4. Kliknite "Run All Tests" za izvajanje celotne zbirke
5. Z desnim klikom na kateri koli test izberite "Debug Test" za nastavitev preloma in korak skozi kodo

Test Explorer prikazuje zelene kljukice za uspešno opravljene teste in daje podrobna sporočila o napakah, ko testi spodletijo.

## Vzorec testiranja

### Vzorec 1: Testiranje predlog pozivov

Najpreprostejši vzorec preizkuša predloge pozivov brez klica na AI model. Preverite, ali zamenjava spremenljivk deluje pravilno in ali so pozivi oblikovani kot pričakovano.

<img src="../../../translated_images/sl/prompt-template-testing.b902758ddccc8dee.webp" alt="Testiranje predloge poziva" width="800"/>

*Testiranje predlog pozivov, ki prikazuje potek zamenjave spremenljivk: predloga z označevalci → uporabljene vrednosti → preverjen oblikovan izhod*

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

**Zaženi ga:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#preveriObrazecPredlogePoziva
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testiranjeOblikePredlogePoziva
```

### Vzorec 2: Uporaba lažnih jezikovnih modelov

Pri testiranju logike pogovora uporabite Mockito za ustvarjanje lažnih modelov, ki vračajo vnaprej določene odzive. To naredi teste hitre, brezplačne in deterministične.

<img src="../../../translated_images/sl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Primerjava lažnih in pravih API-jev" width="800"/>

*Primerjava, zakaj so lažni modeli za testiranje boljši: so hitri, brezplačni, deterministični in ne potrebujejo API ključev*

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

Ta vzorec najdete v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Lažni model zagotavlja dosledno vedenje, tako da lahko preverite pravilno upravljanje s spominom.

### Vzorec 3: Testiranje izolacije pogovora

Spomin pogovora mora ločevati več uporabnikov. Ta test preverja, da se konteksti pogovorov ne mešajo.

<img src="../../../translated_images/sl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Izolacija pogovora" width="800"/>

*Testiranje izolacije pogovorov, ki prikazuje ločene spominske shrambe za različne uporabnike, da prepreči mešanje konteksta*

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

Vsak pogovor ohranja svojo neodvisno zgodovino. V proizvodnih sistemih je ta izolacija ključna za aplikacije z več uporabniki.

### Vzorec 4: Neodvisno testiranje orodij

Orodja so funkcije, ki jih lahko AI pokliče. Testirajte jih neposredno, da zagotovite, da delujejo pravilno ne glede na odločitve AI.

<img src="../../../translated_images/sl/tools-testing.3e1706817b0b3924.webp" alt="Testiranje orodij" width="800"/>

*Neodvisno testiranje orodij, ki prikazuje izvajanje lažnih orodij brez AI klicev za preverjanje poslovne logike*

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

Ti testi iz `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` preverjajo logiko orodij brez vključitve AI. Primer verižnega povezovanja prikazuje, kako izhod enega orodja napaja vhod drugega.

### Vzorec 5: RAG testiranje v pomnilniku

Sistemi RAG običajno potrebujejo vektorske baze podatkov in storitve za vgrajevanje. Vzorec v pomnilniku vam omogoča, da testirate celoten pipeline brez zunanjih odvisnosti.

<img src="../../../translated_images/sl/rag-testing.ee7541b1e23934b1.webp" alt="RAG testiranje v pomnilniku" width="800"/>

*Delovni potek RAG testiranja v pomnilniku, ki prikazuje razčlenjevanje dokumentov, shranjevanje vgradnje in iskanje podobnosti brez potrebe po bazi podatkov*

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

Ta test iz `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ustvari dokument v pomnilniku in preveri razdeljevanje delcev ter ravnanje z metapodatki.

### Vzorec 6: Integracijsko testiranje MCP

Modul MCP testira integracijo Protokola modelnega konteksta z uporabo stdio prenosa. Ti testi preverjajo, da vaša aplikacija lahko zažene in komunicira z MCP strežniki kot podprocesi.

Testi v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` potrjujejo vedenje MCP odjemalca.

**Zaženi jih:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filozofija testiranja

Testirajte svojo kodo, ne AI. Vaši testi bi morali potrditi kodo, ki jo napišete, tako da preverite, kako so zgrajeni pozivi, kako se upravlja s spominom in kako se izvajajo orodja. Odzivi AI se razlikujejo in ne bi smeli biti del testnih trditev. Vprašajte se, ali vaša predloga poziva pravilno nadomešča spremenljivke, ne ali AI poda pravi odgovor.

Uporabite lažne modele jezikov. So zunanje odvisnosti, ki so počasne, drage in nedeterministične. Lažno modeliranje naredi teste hitre z milisekundami namesto sekund, brezplačne brez stroškov API, in deterministične z istim rezultatom vsakič.

Ohranjajte teste neodvisne. Vsak test naj nastavi svoje podatke, se ne zanaša na druge teste in počisti za sabo. Testi naj uspevajo ne glede na vrstni red izvajanja.

Testirajte robne primere, ki presegajo srečne poti. Poskusite prazne vnose, zelo velike vnose, posebne znake, neveljavne parametre in mejne pogoje. Ti pogosto razkrijejo napake, ki jih običajna uporaba ne pokaže.

Uporabite opisna imena. Primerjajte `shouldMaintainConversationHistoryAcrossMultipleMessages()` z `test1()`. Prvo vam natančno pove, kaj se testira, kar bistveno olajša odpravljanje napak.

## Naslednji koraki

Zdaj, ko razumete vzorce testiranja, se poglobite v vsak modul:

- **[00 - Hitri začetek](../00-quick-start/README.md)** - Začnite z osnovami predlog pozivov
- **[01 - Uvod](../01-introduction/README.md)** - Naučite se upravljanja s spominom pogovorov
- **[02 - Inženiring pozivov](../02-prompt-engineering/README.md)** - Obvladujte vzorce pozivov GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Gradite sisteme za generiranje z izboljšanim iskanjem
- **[04 - Orodja](../04-tools/README.md)** - Uvedite klic funkcij in verige orodij
- **[05 - MCP](../05-mcp/README.md)** - Integrirajte Protokol modelnega konteksta

README vseh modulov vsebuje podrobna pojasnila konceptov, testiranih tukaj.

---

**Navigacija:** [← Nazaj na glavno](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za prevajanje z umetno inteligenco [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirnik dokumenta v njegovem izvirnem jeziku naj velja za avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Za kakršnekoli napačne razlage ali nerazumevanja, ki izhajajo iz uporabe tega prevoda, ne odgovarjamo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->