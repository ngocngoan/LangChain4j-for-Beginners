# Testovanie aplikácií LangChain4j

## Obsah

- [Rýchly štart](../../../docs)
- [Čo testy pokrývajú](../../../docs)
- [Spustenie testov](../../../docs)
- [Spustenie testov vo VS Code](../../../docs)
- [Testovacie vzory](../../../docs)
- [Filozofia testovania](../../../docs)
- [Ďalšie kroky](../../../docs)

Táto príručka vás prevedie testami, ktoré ukazujú, ako testovať AI aplikácie bez potreby API kľúčov alebo externých služieb.

## Rýchly štart

Spustite všetky testy jedným príkazom:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/sk/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Úspešné vykonanie testov zobrazujúce všetky testy, ktoré prešli bez chýb*

## Čo testy pokrývajú

Tento kurz sa zameriava na **jednotkové testy**, ktoré sa spúšťajú lokálne. Každý test demonštruje konkrétny koncept LangChain4j izolovane.

<img src="../../../translated_images/sk/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testovacia pyramída zobrazujúca rovnováhu medzi jednotkovými testami (rýchle, izolované), integračnými testami (skutočné komponenty) a end-to-end testami. Toto školenie sa zaoberá jednotkovým testovaním.*

| Modul | Testy | Zameranie | Kľúčové súbory |
|--------|-------|----------|-----------|
| **00 - Rýchly štart** | 6 | Šablóny promptov a náhrada premenných | `SimpleQuickStartTest.java` |
| **01 - Úvod** | 8 | Pamäť konverzácie a stavový chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Vzory GPT-5.2, úrovne nadšenia, štruktúrovaný výstup | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Spracovanie dokumentov, embeddingy, vyhľadávanie podobnosti | `DocumentServiceTest.java` |
| **04 - Nástroje** | 12 | Volanie funkcií a reťazenie nástrojov | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol so stdio transportom | `SimpleMcpTest.java` |

## Spustenie testov

**Spustite všetky testy z koreňa:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```


**Spustite testy pre konkrétny modul:**

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


**Spustite jeden testovací triedu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```


**Spustite konkrétnu testovaciu metódu:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#mala by zachovať históriu konverzácie
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#malo by sa udržiavať históriu rozhovoru
```


## Spustenie testov vo VS Code

Ak používate Visual Studio Code, Test Explorer poskytuje grafické rozhranie na spúšťanie a ladenie testov.

<img src="../../../translated_images/sk/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer vo VS Code zobrazujúci strom testov so všetkými Java testovacími triedami a jednotlivými testovacími metódami*

**Ako spustiť testy vo VS Code:**

1. Otvorte Test Explorer kliknutím na ikonu skúmavky v paneli aktivít
2. Rozviňte strom testov, aby ste videli všetky moduly a testovacie triedy
3. Kliknite na tlačidlo pre spustenie vedľa ľubovoľného testu, aby ste ho spustili jednotlivo
4. Kliknite na "Run All Tests" pre spustenie celej sady
5. Kliknite pravým tlačidlom na ľubovoľný test a vyberte "Debug Test", aby ste nastavili body prerušenia a krokovali kódom

Test Explorer zobrazuje zelené značky za prechádzajúce testy a poskytuje podrobné správy o zlyhaní testov.

## Testovacie vzory

### Vzor 1: Testovanie šablón promptov

Najjednoduchší vzor testuje šablóny promptov bez volania akéhokoľvek AI modelu. Overujete, že náhrada premenných funguje správne a prompt je naformátovaný podľa očakávania.

<img src="../../../translated_images/sk/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testovanie šablón promptov zobrazujúce tok náhrady premenných: šablóna s zástupnými znakmi → aplikované hodnoty → overený naformátovaný výstup*

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

Tento test sa nachádza v `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Spustite ho:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#formátovanieŠablónyTestovacejVýzvy
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testovanieFormátovaniaŠablónyVýzvy
```


### Vzor 2: Mockovanie jazykových modelov

Pri testovaní logiky konverzácie použite Mockito na vytvorenie falošných modelov, ktoré vracajú prednastavené odpovede. To robí testy rýchle, zadarmo a deterministické.

<img src="../../../translated_images/sk/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Porovnanie ukazujúce, prečo sú mocky preferované na testovanie: sú rýchle, bezplatné, deterministické a nevyžadujú API kľúče*

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

Tento vzor sa vyskytuje v `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zabezpečuje konzistentné správanie, aby ste mohli overiť správnu správu pamäte.

### Vzor 3: Testovanie izolácie konverzácie

Pamäť konverzácie musí udržiavať viacerých používateľov oddelených. Tento test overuje, že konverzácie sa nekombinujú.

<img src="../../../translated_images/sk/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testovanie izolácie konverzácie zobrazujúce oddelené úložiská pamäte pre rôznych používateľov, aby sa zabránilo miešaniu kontextu*

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

Každá konverzácia si udržiava svoju vlastnú nezávislú históriu. V produkčných systémoch je táto izolácia kritická pre viac používateľské aplikácie.

### Vzor 4: Testovanie nástrojov samostatne

Nástroje sú funkcie, ktoré môže AI volať. Testujte ich priamo, aby ste zabezpečili ich správnu funkčnosť bez ohľadu na rozhodnutia AI.

<img src="../../../translated_images/sk/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testovanie nástrojov samostatne, zobrazujúce vykonávanie mock nástrojov bez volaní AI na overenie obchodnej logiky*

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

Tieto testy z `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validujú logiku nástrojov bez zapojenia AI. Príklad reťazenia ukazuje, ako výstup jedného nástroja slúži ako vstup do iného.

### Vzor 5: Testovanie RAG v pamäti

RAG systémy tradične vyžadujú vektorové databázy a embeddingové služby. Vzor v pamäti vám umožňuje otestovať celý proces bez externých závislostí.

<img src="../../../translated_images/sk/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Pracovný tok testovania RAG v pamäti zobrazujúci spracovanie dokumentov, ukladanie embeddingov a vyhľadávanie podobností bez potreby databázy*

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

Tento test z `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` vytvára dokument v pamäti a overuje delenie a správu metadát.

### Vzor 6: Integračné testovanie MCP

Modul MCP testuje integráciu Model Context Protocol pomocou stdio transportu. Tieto testy overujú, že vaša aplikácia dokáže spustiť a komunikovať s MCP servermi ako podprocesmi.

Testy v `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validujú správanie MCP klienta.

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

Testujte váš kód, nie AI. Vaše testy by mali overovať kód, ktorý píšete, kontrolou ako sa konštruujú prompty, ako sa spravuje pamäť a ako sa vykonávajú nástroje. Odpovede AI sa líšia a nemali by byť súčasťou testovacích asercií. Pýtajte sa, či váš prompt správne nahradí premenné, nie či AI dáva správnu odpoveď.

Používajte mocky jazykových modelov. Sú to externé závislosti, ktoré sú pomalé, drahé a nedeterministické. Mockovanie robí testy rýchlymi – namiesto sekúnd za milisekundy, bezplatnými bez nákladov na API a deterministickými s rovnakým výsledkom zakaždým.

Udržiavajte testy nezávislé. Každý test by mal nastaviť svoje vlastné dáta, nespoliehať sa na iné testy a po testoch sa upraceť. Testy by mali prechádzať bez ohľadu na poradie spustenia.

Testujte okrajové prípady mimo bežnej cesty. Vyskúšajte prázdne vstupy, veľmi veľké vstupy, špeciálne znaky, neplatné parametre a hraničné podmienky. Tieto často odhaľujú chyby, ktoré bežné používanie neodhalí.

Používajte popisné názvy. Porovnajte `shouldMaintainConversationHistoryAcrossMultipleMessages()` s `test1()`. Prvý názov presne hovorí, čo sa testuje, čo výrazne uľahčuje ladeniu zlyhaní.

## Ďalšie kroky

Teraz, keď rozumiete testovacím vzorom, ponorte sa hlbšie do každého modulu:

- **[00 - Rýchly štart](../00-quick-start/README.md)** - Začnite so základmi šablón promptov
- **[01 - Úvod](../01-introduction/README.md)** - Naučte sa spravovať pamäť konverzácie
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Ovládnite vzory promptovania GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Budujte systémy s retrieval-augmented generation
- **[04 - Nástroje](../04-tools/README.md)** - Implementujte volanie funkcií a reťazenia nástrojov
- **[05 - MCP](../05-mcp/README.md)** - Integrujte Model Context Protocol

Každý README modul obsahuje detailné vysvetlenia konceptov testovaných tu.

---

**Navigácia:** [← Späť na Hlavnú stránku](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Výhrada**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, vezmite prosím na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nezodpovedáme za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->