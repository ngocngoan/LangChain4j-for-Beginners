# Testarea aplicațiilor LangChain4j

## Cuprins

- [Pornire rapidă](../../../docs)
- [Ce acoperă testele](../../../docs)
- [Rularea testelor](../../../docs)
- [Rularea testelor în VS Code](../../../docs)
- [Modele de testare](../../../docs)
- [Filosofia testării](../../../docs)
- [Pașii următori](../../../docs)

Acest ghid te poartă prin testele care demonstrează cum să testezi aplicațiile AI fără a necesita chei API sau servicii externe.

## Pornire rapidă

Rulează toate testele cu o singură comandă:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ro/test-results.ea5c98d8f3642043.webp" alt="Rezultate test reușit" width="800"/>

*Executare de test reușită arătând toate testele trecute fără eșecuri*

## Ce acoperă testele

Acest curs se concentrează pe **teste unitare** care rulează local. Fiecare test demonstrează un concept specific LangChain4j izolat.

<img src="../../../translated_images/ro/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramida testării" width="800"/>

*Piramida testării arătând echilibrul dintre testele unitare (rapide, izolate), testele de integrare (componente reale) și testele end-to-end. Acest training acoperă testarea unitară.*

| Modul | Teste | Focus | Fișiere cheie |
|--------|-------|-------|-----------|
| **00 - Pornire rapidă** | 6 | Șabloane de prompt și substituirea variabilelor | `SimpleQuickStartTest.java` |
| **01 - Introducere** | 8 | Memorie conversațională și chat cu stare | `SimpleConversationTest.java` |
| **02 - Ingineria promptului** | 12 | Modele GPT-5.2, niveluri de entuziasm, output structurat | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestie documente, încorporări, căutare similaritate | `DocumentServiceTest.java` |
| **04 - Unelte** | 12 | Apelare funcții și concatenarea uneltelor | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protocol Model Context cu transport stdio | `SimpleMcpTest.java` |

## Rularea testelor

**Rulează toate testele din rădăcină:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Rulează testele pentru un modul specific:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Sau din rădăcină
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Sau din rădăcină
mvn --% test -pl 01-introduction
```

**Rulează o singură clasă de test:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Rulează o metodă specifică de test:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ar trebui să păstreze istoricul conversației
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ar trebui să păstreze istoricul conversației
```

## Rularea testelor în VS Code

Dacă folosești Visual Studio Code, Test Explorer oferă o interfață grafică pentru rularea și depanarea testelor.

<img src="../../../translated_images/ro/vscode-testing.f02dd5917289dced.webp" alt="Explorator Teste VS Code" width="800"/>

*Exploratorul de teste VS Code arătând arborele testelor cu toate clasele și metodele de test Java*

**Pentru a rula testele în VS Code:**

1. Deschide Test Explorer făcând clic pe pictograma de eprubetă din bara de activități
2. Extinde arborele testelor pentru a vedea toate modulele și clasele de test
3. Apasă butonul de redare lângă orice test pentru a-l rula individual
4. Apasă „Run All Tests” pentru a executa întreaga suită
5. Click dreapta pe orice test și selectează „Debug Test” pentru a seta puncte de întrerupere și a parcurge codul

Exploratorul de teste afișează bifa verde pentru testele trecute și oferă mesaje detaliate pentru eșecuri când acestea apar.

## Modele de testare

### Model 1: Testarea șabloanelor de prompt

Cel mai simplu model testează șabloanele de prompt fără a apela vreun model AI. Verifici că substituția variabilelor funcționează corect și că prompturile sunt formatate corespunzător.

<img src="../../../translated_images/ro/prompt-template-testing.b902758ddccc8dee.webp" alt="Testarea șabloanelor de prompt" width="800"/>

*Testarea șabloanelor de prompt arătând fluxul substituției variabilelor: șablon cu locuri rezervate → valori aplicate → output formatat verificat*

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

Acest test se află în `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Rulează-l:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testareaFormatariiSabloanelorPrompt
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testareFormatTemplatePrompt
```

### Model 2: Simularea modelelor lingvistice

Când testezi logica conversațională, folosește Mockito pentru a crea modele false care returnează răspunsuri prestabilite. Acest lucru face testele rapide, gratuite și deterministe.

<img src="../../../translated_images/ro/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Comparație între mock și API real" width="800"/>

*Comparație care arată de ce mock-urile sunt preferate pentru testare: sunt rapide, gratuite, deterministe și nu necesită chei API*

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
        assertThat(history).hasSize(6); // 3 mesaje de la utilizator + 3 mesaje AI
    }
}
```

Acest model apare în `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock-ul asigură comportament consistent pentru a putea verifica managementul memoriei corect.

### Model 3: Testarea izolării conversațiilor

Memoria conversațională trebuie să păstreze utilizatorii separați. Acest test verifică că conversațiile nu amestecă contexte.

<img src="../../../translated_images/ro/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Izolarea conversației" width="800"/>

*Testarea izolării conversației arătând stocări de memorie separate pentru diferiți utilizatori pentru a preveni amestecul contextelor*

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

Fiecare conversație își menține propria istorie independentă. În sistemele de producție această izolare este critica pentru aplicațiile multi-utilizator.

### Model 4: Testarea uneltelor independent

Uneltele sunt funcții pe care AI le poate apela. Testează-le direct pentru a te asigura că funcționează corect indiferent de deciziile AI.

<img src="../../../translated_images/ro/tools-testing.3e1706817b0b3924.webp" alt="Testarea uneltelor" width="800"/>

*Testarea uneltelor independent arătând executarea uneltei simulată fără apeluri AI pentru a verifica logica de business*

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

Aceste teste din `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validează logica uneltelor fără implicarea AI. Exemplul de concatenare arată cum outputul unei unelte intră ca input la alta.

### Model 5: Testarea RAG în memorie

Sistemele RAG tradițional necesită baze de date vectoriale și servicii de încorporare. Modelul în memorie îți permite să testezi întregul flux fără dependențe externe.

<img src="../../../translated_images/ro/rag-testing.ee7541b1e23934b1.webp" alt="Testarea RAG în memorie" width="800"/>

*Fluxul de testare RAG în memorie arătând parsarea documentelor, stocarea încorporărilor și căutarea similarității fără a necesita o bază de date*

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

Acest test din `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` creează un document în memorie și verifică fragmentarea și gestionarea metadatelor.

### Model 6: Testarea integrării MCP

Modulul MCP testează integrarea Model Context Protocol folosind transport stdio. Aceste teste verifică că aplicația ta poate porni și comunica cu serverele MCP ca procese secundare.

Testele din `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validează comportamentul clientului MCP.

**Rulează-le:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filosofia testării

Testează codul tău, nu AI-ul. Testele tale ar trebui să valideze codul pe care îl scrii verificând cum sunt construite prompturile, cum se gestionează memoria și cum se execută uneltele. Răspunsurile AI variază și nu ar trebui să facă parte din aserțiunile testelor. Întreabă-te dacă șablonul de prompt substituie corect variabilele, nu dacă AI oferă răspunsul corect.

Folosește mocks pentru modelele lingvistice. Sunt dependențe externe lente, costisitoare și nedeterministe. Mock-urile fac testele rapide în milisecunde în loc de secunde, gratuite fără costuri API și deterministe cu același rezultat de fiecare dată.

Păstrează testele independente. Fiecare test ar trebui să seteze propriile date, să nu depindă de alte teste și să curețe după sine. Testele ar trebui să treacă indiferent de ordinea execuției.

Testează cazurile-limită dincolo de calea fericită. Încearcă intrări goale, foarte mari, caractere speciale, parametri invalizi și condiții-limită. Acestea dezvăluie adesea bug-uri pe care utilizarea normală nu le expune.

Folosește nume descriptive. Compară `shouldMaintainConversationHistoryAcrossMultipleMessages()` cu `test1()`. Primul îți spune exact ce se testează, ceea ce face depanarea eșecurilor mult mai ușoară.

## Pașii următori

Acum că înțelegi modelele de testare, aprofundează fiecare modul:

- **[00 - Pornire rapidă](../00-quick-start/README.md)** - Începe cu elementele de bază ale șabloanelor de prompt
- **[01 - Introducere](../01-introduction/README.md)** - Învață gestionarea memoriei conversaționale
- **[02 - Ingineria promptului](../02-prompt-engineering/README.md)** - Stăpânește modelele de prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Construiește sisteme de generare augmentată prin recuperare
- **[04 - Unelte](../04-tools/README.md)** - Implementează apelarea funcțiilor și lanțurile de unelte
- **[05 - MCP](../05-mcp/README.md)** - Integrează Protocolul Model Context

Fiecare modul are un README care oferă explicații detaliate ale conceptelor testate aici.

---

**Navigare:** [← Înapoi la Principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite care pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->