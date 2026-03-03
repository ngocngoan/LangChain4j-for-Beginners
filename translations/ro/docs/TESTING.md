# Testarea aplicațiilor LangChain4j

## Cuprins

- [Început rapid](../../../docs)
- [Ce acoperă testele](../../../docs)
- [Rularea testelor](../../../docs)
- [Rularea testelor în VS Code](../../../docs)
- [Modele de testare](../../../docs)
- [Filosofia testării](../../../docs)
- [Pașii următori](../../../docs)

Acest ghid te parcurge prin testele care demonstrează cum să testezi aplicații AI fără a necesita chei API sau servicii externe.

## Început rapid

Rulează toate testele cu o singură comandă:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Când toate testele trec, ar trebui să vezi un output ca în captura de ecran de mai jos — testele rulează fără niciun eșec.

<img src="../../../translated_images/ro/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Executarea testelor cu succes arătând toate testele trecute fără niciun eșec*

## Ce acoperă testele

Acest curs se concentrează pe **teste unitare** care rulează local. Fiecare test demonstrează un concept specific LangChain4j izolat. Piramida de testare de mai jos arată unde se înscriu testele unitare — ele formează fundația rapidă și de încredere pe care se construiește restul strategiei tale de testare.

<img src="../../../translated_images/ro/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramida de testare arătând echilibrul între testele unitare (rapide, izolate), testele de integrare (componente reale) și testele end-to-end. Această instruire acoperă testarea unitară.*

| Modul | Teste | Focalizare | Fișiere principale |
|--------|-------|-------|-----------|
| **00 - Început rapid** | 6 | Șabloane prompt și substituția variabilelor | `SimpleQuickStartTest.java` |
| **01 - Introducere** | 8 | Memorie conversație și chat cu stare | `SimpleConversationTest.java` |
| **02 - Ingineria prompturilor** | 12 | Modele GPT-5.2, niveluri de entuziasm, output structurat | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestie documente, embedding-uri, căutare după similaritate | `DocumentServiceTest.java` |
| **04 - Unelte** | 12 | Apelarea funcțiilor și concatenarea uneltelor | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protocol de context model cu transport Stdio | `SimpleMcpTest.java` |

## Rularea testelor

**Rulează toate testele de la rădăcină:**

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

**Rulează o metodă de test specifică:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#ar trebui să mențină istoricul conversației
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#ar trebui să mențină istoricul conversației
```

## Rularea testelor în VS Code

Dacă folosești Visual Studio Code, Test Explorer oferă o interfață grafică pentru rularea și depanarea testelor.

<img src="../../../translated_images/ro/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer din VS Code arătând arborele testelor cu toate clasele de test Java și metodele individuale*

**Pentru a rula testele în VS Code:**

1. Deschide Test Explorer făcând clic pe iconița cu eprubetă din bara de activități
2. Extinde arborele testelor pentru a vedea toate modulele și clasele de test
3. Fă clic pe butonul de redare de lângă orice test pentru a-l rula individual
4. Fă clic pe „Run All Tests” pentru a executa întreaga suită
5. Click dreapta pe orice test și selectează „Debug Test” pentru a seta breakpoint-uri și a parcurge codul

Test Explorer arată bife verzi pentru testele trecute și oferă mesaje detaliate de eșec când testele nu trec.

## Modele de testare

### Model 1: Testarea șabloanelor prompt

Cel mai simplu model testează șabloanele prompt fără a apela niciun model AI. Verifici că substituția variabilelor funcționează corect și prompturile sunt formatate conform așteptărilor.

<img src="../../../translated_images/ro/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testarea șabloanelor prompt arătând fluxul de substituție a variabilelor: șablon cu locuri de completat → valori aplicate → output formatat verificat*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testareaFormatariiSablonuluiPrompt
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testareaFormatuluiTemplateuluiPrompt
```

### Model 2: Falsificarea modelelor de limbaj

Când testezi logica conversației, folosește Mockito pentru a crea modele false care returnează răspunsuri prestabilite. Acest lucru face ca testele să fie rapide, gratuite și deterministe.

<img src="../../../translated_images/ro/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Comparație arătând de ce se preferă mocks pentru testare: sunt rapide, gratuite, deterministe și nu necesită chei API*

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
        assertThat(history).hasSize(6); // 3 mesaje utilizator + 3 mesaje AI
    }
}
```

Acest model apare în `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock-ul asigură un comportament constant astfel încât să verifici că gestionarea memoriei funcționează corect.

### Model 3: Testarea izolării conversației

Memoria conversației trebuie să păstreze utilizatorii separați. Acest test verifică că conversațiile nu amestecă contexte.

<img src="../../../translated_images/ro/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testarea izolării conversației arătând magazine de memorie separate pentru utilizatori diferiți, prevenind amestecul de contexte*

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

Fiecare conversație își păstrează propriul istoric independent. În sistemele de producție, această izolare este critică pentru aplicațiile multi-utilizator.

### Model 4: Testarea uneltelor independent

Uneltele sunt funcții pe care AI le poate apela. Testează-le direct pentru a te asigura că funcționează corect indiferent de deciziile AI.

<img src="../../../translated_images/ro/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testarea uneltelor independent arătând execuția uneltelor mock fără apeluri AI pentru a verifica logica de business*

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

Aceste teste din `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validează logica uneltelor fără implicarea AI. Exemplul cu concatenarea arată cum output-ul unei unelte alimentază input-ul alteia.

### Model 5: Testarea RAG în memorie

Sistemele RAG necesită în mod tradițional baze de date vectoriale și servicii de embedding-uri. Modelul în memorie îți permite să testezi întregul pipeline fără dependențe externe.

<img src="../../../translated_images/ro/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Fluxul de testare RAG în memorie arătând parsarea documentelor, stocarea embedding-urilor și căutarea după similaritate fără a necesita bază de date*

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

Acest test din `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` creează un document în memorie și verifică segmentarea și gestionarea metadata.

### Model 6: Testarea integrării MCP

Modulul MCP testează integrarea Protocolului Contextului Model folosind transportul stdio. Aceste teste verifică dacă aplicația ta poate genera și comunica cu servere MCP ca subprocese.

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

Testează codul tău, nu AI-ul. Testele tale ar trebui să valideze codul pe care îl scrii verificând cum sunt construite prompturile, cum este gestionată memoria și cum execută uneltele. Răspunsurile AI variază și nu ar trebui să facă parte din aserțiunile testelor. Întreabă-te dacă șablonul tău de prompt substituie corect variabilele, nu dacă AI oferă răspunsul corect.

Folosește mocks pentru modelele de limbaj. Sunt dependențe externe care sunt lente, costisitoare și nedeterministe. Falsificarea face testele rapide în milisecunde în loc de secunde, gratuite fără costuri API și deterministe cu același rezultat de fiecare dată.

Păstrează testele independente. Fiecare test ar trebui să seteze propriile date, să nu se bazeze pe alte teste și să se curețe după sine. Testele ar trebui să treacă indiferent de ordinea execuției.

Testează cazuri limită dincolo de calea fericită. Încearcă inputuri goale, inputuri foarte mari, caractere speciale, parametri invalizi și condiții limită. Acestea dezvăluie de multe ori bug-uri pe care utilizarea normală nu le expune.

Folosește nume descriptive. Compară `shouldMaintainConversationHistoryAcrossMultipleMessages()` cu `test1()`. Primul îți spune exact ce se testează, făcând depanarea eșecurilor mult mai ușoară.

## Pașii următori

Acum că înțelegi modelele de testare, aprofundează fiecare modul:

- **[00 - Început rapid](../00-quick-start/README.md)** - Începe cu bazele șabloanelor prompt
- **[01 - Introducere](../01-introduction/README.md)** - Învață gestionarea memoriei conversației
- **[02 - Ingineria prompturilor](../02/prompt-engineering/README.md)** - Stăpânește modelele de prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Construiește sisteme de generare augmentată prin recuperare
- **[04 - Unelte](../04-tools/README.md)** - Implementează apelarea funcțiilor și lanțuri de unelte
- **[05 - MCP](../05-mcp/README.md)** - Integrează Protocolul Contextului Model

README-urile fiecărui modul oferă explicații detaliate ale conceptelor testate aici.

---

**Navigare:** [← Înapoi la principal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->