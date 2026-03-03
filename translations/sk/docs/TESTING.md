# Testovanie aplikácií LangChain4j

## Obsah

- [Rýchly štart](../../../docs)
- [Čo testy pokrývajú](../../../docs)
- [Spustenie testov](../../../docs)
- [Spustenie testov vo VS Code](../../../docs)
- [Vzory testovania](../../../docs)
- [Filozofia testovania](../../../docs)
- [Ďalšie kroky](../../../docs)

Táto príručka vás prevedie testami, ktoré ukazujú, ako testovať AI aplikácie bez nutnosti API kľúčov alebo externých služieb.

## Rýchly štart

Spustite všetky testy jediným príkazom:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Keď všetky testy prebehnú úspešne, mali by ste vidieť výstup ako na obrázku nižšie — testy prebiehajú bez zlyhaní.

<img src="../../../translated_images/sk/test-results.ea5c98d8f3642043.webp" alt="Úspešné výsledky testov" width="800"/>

*Úspešné spustenie testov ukazujúce, že všetky testy prešli bez zlyhaní*

## Čo testy pokrývajú

Tento kurz sa zameriava na **jednotkové testy**, ktoré sa spúšťajú lokálne. Každý test predvádza určitý koncept LangChain4j izolovane. Nižšie je testovacia pyramída, ktorá ukazuje, kde jednotkové testy zapadajú — tvoria rýchly a spoľahlivý základ, na ktorom sa buduje zvyšok vašej testovacej stratégie.

<img src="../../../translated_images/sk/testing-pyramid.2dd1079a0481e53e.webp" alt="Testovacia pyramída" width="800"/>

*Testovacia pyramída ukazujúca rovnováhu medzi jednotkovými testami (rýchle, izolované), integračnými testami (skutočné komponenty) a end-to-end testami. Tento kurz sa zaoberá jednotkovým testovaním.*

| Modul | Testy | Zameranie | Kľúčové súbory |
|--------|-------|-----------|----------------|
| **00 - Rýchly štart** | 6 | Šablóny promptov a nahradzovanie premenných | `SimpleQuickStartTest.java` |
| **01 - Úvod** | 8 | Pamäť konverzácie a stavový chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2 vzory, úrovne ochoty, štruktúrovaný výstup | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Načítanie dokumentov, embeddingy, vyhľadávanie podobností | `DocumentServiceTest.java` |
| **04 - Nástroje** | 12 | Volanie funkcií a reťazenie nástrojov | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol s transportom stdio | `SimpleMcpTest.java` |

## Spustenie testov

**Spustenie všetkých testov z koreňového adresára:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Spustenie testov pre konkrétny modul:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Alebo z koreňa
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Alebo z koreňa
mvn --% test -pl 01-introduction
```

**Spustenie jedného testovacieho triedy:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Spustenie konkrétnej testovacej metódy:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#mal by udržiavať históriu rozhovoru
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#mal by udržiavať históriu konverzácie
```

## Spustenie testov vo VS Code

Ak používate Visual Studio Code, Test Explorer poskytuje grafické rozhranie pre spúšťanie a ladenie testov.

<img src="../../../translated_images/sk/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer ukazujúci strom testov so všetkými Java testovacími triedami a jednotlivými testovacími metódami*

**Ako spustiť testy vo VS Code:**

1. Otvorte Test Explorer kliknutím na ikonu skúmavky v paneli aktivít
2. Rozbaľte strom testov, aby ste videli všetky moduly a testovacie triedy
3. Kliknite na tlačidlo prehrávania vedľa ľubovoľného testu pre jeho samostatné spustenie
4. Kliknite na "Run All Tests" pre spustenie celého balíka testov
5. Kliknite pravým tlačidlom na ktorýkoľvek test a vyberte "Debug Test" pre nastavenie breakpointov a krokovanie kódu

Test Explorer zobrazuje zelené zaškrtnutia pri úspešných testoch a poskytuje podrobné oznámenia o zlyhaniach, keď testy zlyhajú.

## Vzory testovania

### Vzor 1: Testovanie šablón promptov

Najjednoduchší vzor testuje šablóny promptov bez volania AI modelu. Overujete, že nahradzovanie premenných funguje správne a prompty sú formátované podľa očakávania.

<img src="../../../translated_images/sk/prompt-template-testing.b902758ddccc8dee.webp" alt="Testovanie šablón promptov" width="800"/>

*Testovanie šablón promptov ukazujúce proces nahradzovania premenných: šablóna s zástupcami → aplikované hodnoty → overený formátovaný výstup*

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

Tento test sa nachádza v súbore `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Spustite ho:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testFormátovaniaŠablónyVýzvy
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#test formátovania šablóny výzvy
```

### Vzor 2: Mockovanie jazykových modelov

Pri testovaní logiky konverzácie používajte Mockito na vytvorenie falošných modelov, ktoré vracajú preddefinované odpovede. Toto robí testy rýchlymi, bezplatnými a deterministickými.

<img src="../../../translated_images/sk/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Porovnanie mock vs skutočné API" width="800"/>

*Porovnanie ukazujúce, prečo sú mockingy preferované pri testovaní: sú rýchle, zadarmo, deterministické a nevyžadujú API kľúče*

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
        assertThat(history).hasSize(6); // 3 správy od používateľa + 3 správy od AI
    }
}
```

Tento vzor sa nachádza v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zaručuje konzistentné správanie, takže môžete overiť správne spravovanie pamäte.

### Vzor 3: Testovanie izolácie konverzácie

Pamäť konverzácie musí udržiavať viacerých používateľov oddelene. Tento test overuje, že sa konverzácie nekrižujú.

<img src="../../../translated_images/sk/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Izolácia konverzácie" width="800"/>

*Testovanie izolácie konverzácie ukazujúce samostatné úložiská pamäte pre rôznych používateľov, aby sa predišlo miešaniu kontextov*

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

Každá konverzácia udržiava svoju vlastnú nezávislú históriu. V produkčných systémoch je táto izolácia kľúčová pre aplikácie s viacerými používateľmi.

### Vzor 4: Testovanie nástrojov samostatne

Nástroje sú funkcie, ktoré môže AI volať. Testujte ich priamo, aby ste zabezpečili ich správnu funkčnosť nezávisle od rozhodnutí AI.

<img src="../../../translated_images/sk/tools-testing.3e1706817b0b3924.webp" alt="Testovanie nástrojov" width="800"/>

*Testovanie nástrojov samostatne ukazujúce spustenie mock nástrojov bez volania AI na overenie obchodnej logiky*

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

Tieto testy z `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` overujú logiku nástrojov bez účasti AI. Príklad reťazenia ukazuje, ako výstup jedného nástroja ovplyvňuje vstup ďalšieho.

### Vzor 5: Testovanie RAG v pamäti

RAG systémy tradične vyžadujú vektorové databázy a embedding služby. Vzor v pamäti umožňuje testovať celý pipeline bez externých závislostí.

<img src="../../../translated_images/sk/rag-testing.ee7541b1e23934b1.webp" alt="Testovanie RAG v pamäti" width="800"/>

*Testovanie RAG v pamäti ukazujúce spracovanie dokumentu, ukladanie embeddingov a vyhľadávanie podobností bez potreby databázy*

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

Tento test z `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` vytvára dokument v pamäti a overuje jeho rozklad na kúsky a spracovanie metadát.

### Vzor 6: Integračné testovanie MCP

Modul MCP testuje integráciu protokolu Model Context Protocol pomocou stdio transportu. Tieto testy overujú, že vaša aplikácia dokáže spúšťať a komunikovať s MCP servermi ako podprocesmi.

Testy v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` overujú správanie MCP klienta.

**Spustite ich:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filozofia testovania

Testujte svoj kód, nie AI. Vaše testy by mali validovať kód, ktorý píšete, tým, že kontrolujú, ako sa prompty konštruujú, ako sa spravuje pamäť a ako sa vykonávajú nástroje. AI odpovede sa môžu líšiť a nemali by byť súčasťou testovacích tvrdení. Pýtajte sa, či vaša šablóna promptu správne nahrádza premenné, nie či AI dáva správnu odpoveď.

Používajte mocky pre jazykové modely. Sú to externé závislosti, ktoré sú pomalé, drahé a nedeterministické. Mockovanie robí testy rýchlymi (v milisekundách namiesto sekúnd), bezplatnými (žiadne API náklady) a deterministickými (rovnaký výsledok zakaždým).

Udržujte testy nezávislé. Každý test by mal nastaviť vlastné dáta, nespoliehať sa na iné testy a po sebe upratať. Testy by mali prejsť bez ohľadu na poradie spúšťania.

Testujte hraničné prípady mimo bežných scénarov. Skúste prázdne vstupy, veľmi veľké vstupy, špeciálne znaky, neplatné parametre a hranicné podmienky. Tieto často odhalia chyby, ktoré bežné používanie neodhalí.

Používajte popisné názvy. Porovnajte `shouldMaintainConversationHistoryAcrossMultipleMessages()` a `test1()`. Prvý názov presne vysvetľuje, čo sa testuje, čo výrazne uľahčuje ladenie zlyhaní.

## Ďalšie kroky

Keď už rozumiete testovacím vzorom, ponorte sa hlbšie do jednotlivých modulov:

- **[00 - Rýchly štart](../00-quick-start/README.md)** - Začnite so základmi šablón promptov
- **[01 - Úvod](../01-introduction/README.md)** - Naučte sa spravovať pamäť konverzácie
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Osvojte si vzory pre GPT-5.2 promptovanie
- **[03 - RAG](../03-rag/README.md)** - Budujte systémy pre retrieval-augmented generation
- **[04 - Nástroje](../04-tools/README.md)** - Implementujte volanie funkcií a reťazce nástrojov
- **[05 - MCP](../05-mcp/README.md)** - Integrujte Model Context Protocol

README každého modulu poskytuje podrobné vysvetlenia konceptov testovaných tu.

---

**Navigácia:** [← Späť na hlavné](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrieknutie sa zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladačskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nejasnosti alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->