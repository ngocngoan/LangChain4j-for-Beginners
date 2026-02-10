# Δοκιμή Εφαρμογών LangChain4j

## Πίνακας Περιεχομένων

- [Γρήγορη Έναρξη](../../../docs)
- [Τι Καλύπτουν οι Δοκιμές](../../../docs)
- [Εκτέλεση των Δοκιμών](../../../docs)
- [Εκτέλεση Δοκιμών στο VS Code](../../../docs)
- [Πρότυπα Δοκιμής](../../../docs)
- [Φιλοσοφία Δοκιμών](../../../docs)
- [Επόμενα Βήματα](../../../docs)

Αυτός ο οδηγός σας καθοδηγεί μέσα από τις δοκιμές που δείχνουν πώς να δοκιμάζετε εφαρμογές τεχνητής νοημοσύνης χωρίς να απαιτούνται κλειδιά API ή εξωτερικές υπηρεσίες.

## Γρήγορη Έναρξη

Εκτελέστε όλες τις δοκιμές με μια εντολή:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/el/test-results.ea5c98d8f3642043.webp" alt="Επιτυχημένα Αποτελέσματα Δοκιμής" width="800"/>

*Επιτυχημένη εκτέλεση δοκιμών που δείχνει όλες τις δοκιμές να περνούν χωρίς αποτυχίες*

## Τι Καλύπτουν οι Δοκιμές

Αυτό το μάθημα εστιάζει σε **μονάδες δοκιμών** που τρέχουν τοπικά. Κάθε δοκιμή παρουσιάζει μια συγκεκριμένη έννοια του LangChain4j απομονωμένη.

<img src="../../../translated_images/el/testing-pyramid.2dd1079a0481e53e.webp" alt="Πυραμίδα Δοκιμών" width="800"/>

*Πυραμίδα δοκιμών που δείχνει την ισορροπία μεταξύ μονάδων δοκιμών (γρήγορες, απομονωμένες), ενσωματωμένων δοκιμών (πραγματικά στοιχεία), και end-to-end δοκιμών. Αυτή η εκπαίδευση καλύπτει τις μονάδες δοκιμών.*

| Μονάδα | Δοκιμές | Εστίαση | Κύρια Αρχεία |
|--------|---------|---------|--------------|
| **00 - Γρήγορη Έναρξη** | 6 | Πρότυπα prompts και αντικατάσταση μεταβλητών | `SimpleQuickStartTest.java` |
| **01 - Εισαγωγή** | 8 | Μνήμη συνομιλίας και κατάσταση συνομιλίας | `SimpleConversationTest.java` |
| **02 - Μηχανική Προτροπής** | 12 | Πρότυπα GPT-5.2, επίπεδα ενθουσιασμού, δομημένη έξοδος | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Εισαγωγή εγγράφων, ενσωματώσεις, αναζήτηση ομοιότητας | `DocumentServiceTest.java` |
| **04 - Εργαλεία** | 12 | Κλήση συναρτήσεων και αλυσιδωτή χρήση εργαλείων | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Πρωτόκολλο Περιβάλλοντος Μοντέλου με μεταφορά Stdio | `SimpleMcpTest.java` |

## Εκτέλεση των Δοκιμών

**Εκτελέστε όλες τις δοκιμές από τη ρίζα:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Εκτέλεση δοκιμών για συγκεκριμένη μονάδα:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ή από τη ρίζα
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ή από τη ρίζα
mvn --% test -pl 01-introduction
```

**Εκτέλεση μιας μεμονωμένης κλάσης δοκιμών:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Εκτέλεση συγκεκριμένης μεθόδου δοκιμής:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#πρέπει να διατηρείται το ιστορικό συνομιλίας
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#θα πρέπει να διατηρεί το ιστορικό συνομιλίας
```

## Εκτέλεση Δοκιμών στο VS Code

Αν χρησιμοποιείτε το Visual Studio Code, ο εξερευνητής δοκιμών παρέχει ένα γραφικό περιβάλλον για την εκτέλεση και αποσφαλμάτωση δοκιμών.

<img src="../../../translated_images/el/vscode-testing.f02dd5917289dced.webp" alt="Εξερευνητής Δοκιμών VS Code" width="800"/>

*Ο Εξερευνητής Δοκιμών στο VS Code που δείχνει το δέντρο δοκιμών με όλες τις κλάσεις Java και τις μεθόδους δοκιμών*

**Για να εκτελέσετε δοκιμές στο VS Code:**

1. Ανοίξτε τον Εξερευνητή Δοκιμών κάνοντας κλικ στο εικονίδιο φιαλιδίου στη Γραμμή Δραστηριότητας
2. Επεκτείνετε το δέντρο δοκιμών για να δείτε όλες τις μονάδες και κλάσεις δοκιμών
3. Κάντε κλικ στο κουμπί αναπαραγωγής δίπλα σε οποιαδήποτε δοκιμή για να την εκτελέσετε ξεχωριστά
4. Κάντε κλικ στο "Run All Tests" για να εκτελέσετε όλο το σύνολο
5. Κάντε δεξί κλικ σε οποιαδήποτε δοκιμή και επιλέξτε "Debug Test" για να ορίσετε σημεία διακοπής και να εκτελέσετε βήμα-βήμα

Ο εξερευνητής δοκιμών δείχνει πράσινα σημάδια επιλογής για επιτυχείς δοκιμές και παρέχει αναλυτικά μηνύματα αποτυχίας όταν αποτυγχάνουν δοκιμές.

## Πρότυπα Δοκιμής

### Πρότυπο 1: Δοκιμή Προτύπων Prompt

Το απλούστερο πρότυπο δοκιμάζει πρότυπα prompts χωρίς να καλεί κάποιο μοντέλο AI. Επαληθεύετε ότι η αντικατάσταση μεταβλητών λειτουργεί σωστά και τα prompts είναι μορφοποιημένα όπως αναμένεται.

<img src="../../../translated_images/el/prompt-template-testing.b902758ddccc8dee.webp" alt="Δοκιμή Προτύπου Prompt" width="800"/>

*Δοκιμές προτύπων prompt που δείχνουν τη ροή αντικατάστασης μεταβλητών: πρότυπο με placeholders → εφαρμογή τιμών → επαλήθευση μορφοποιημένης εξόδου*

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

Αυτή η δοκιμή βρίσκεται στο `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Εκτελέστε την:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#δοκιμήΜορφοποίησηςΠροτύπουΠροτροπής
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#δοκιμήΜορφοποίησηςΠρότυπουΠροτροπής
```

### Πρότυπο 2: Mocking Μοντέλων Γλώσσας

Όταν δοκιμάζετε λογική συνομιλίας, χρησιμοποιήστε το Mockito για να δημιουργήσετε ψεύτικα μοντέλα που επιστρέφουν προκαθορισμένες απαντήσεις. Αυτό κάνει τις δοκιμές γρήγορες, δωρεάν και καθοριστικές.

<img src="../../../translated_images/el/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Σύγκριση Mock vs Πραγματικού API" width="800"/>

*Σύγκριση που δείχνει γιατί προτιμώνται τα mocks για δοκιμές: είναι γρήγορα, δωρεάν, καθοριστικά και δεν απαιτούν κλειδιά API*

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
        assertThat(history).hasSize(6); // 3 μηνύματα χρήστη + 3 μηνύματα ΤΝ
    }
}
```

Αυτό το πρότυπο εμφανίζεται στο `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Το mock εξασφαλίζει συνεπή συμπεριφορά ώστε να μπορείτε να επαληθεύσετε ότι η διαχείριση μνήμης λειτουργεί σωστά.

### Πρότυπο 3: Δοκιμή Απομόνωσης Συνομιλίας

Η μνήμη συνομιλίας πρέπει να κρατά πολλούς χρήστες χωριστά. Αυτή η δοκιμή επαληθεύει ότι οι συνομιλίες δεν αναμειγνύουν το περιβάλλον.

<img src="../../../translated_images/el/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Απομόνωση Συνομιλίας" width="800"/>

*Δοκιμή απομόνωσης συνομιλίας που δείχνει ξεχωριστές αποθηκεύσεις μνήμης για διαφορετικούς χρήστες για αποφυγή ανάμιξης περιεχομένου*

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

Κάθε συνομιλία διατηρεί το δικό της ανεξάρτητο ιστορικό. Σε παραγωγικά συστήματα, αυτή η απομόνωση είναι κρίσιμη για εφαρμογές πολλαπλών χρηστών.

### Πρότυπο 4: Δοκιμή Εργαλείων Ανεξάρτητα

Τα εργαλεία είναι συναρτήσεις που μπορεί να καλέσει το AI. Δοκιμάστε τα απευθείας για να διασφαλίσετε ότι λειτουργούν σωστά ανεξάρτητα από τις αποφάσεις του AI.

<img src="../../../translated_images/el/tools-testing.3e1706817b0b3924.webp" alt="Δοκιμή Εργαλείων" width="800"/>

*Δοκιμή εργαλείων ανεξάρτητα που δείχνει εκτέλεση mock εργαλείων χωρίς κλήσεις AI για επαλήθευση επιχειρηματικής λογικής*

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

Αυτές οι δοκιμές από `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` επικυρώνουν τη λογική εργαλείων χωρίς συμμετοχή AI. Το παράδειγμα αλυσιδωτής κλήσης δείχνει πώς η έξοδος ενός εργαλείου μεταφέρεται στην είσοδο άλλου.

### Πρότυπο 5: Δοκιμή RAG Με Μνήμη

Τα συστήματα RAG παραδοσιακά απαιτούν βάσεις δεδομένων διανυσμάτων και υπηρεσίες ενσωμάτωσης. Το πρότυπο με μνήμη επιτρέπει να δοκιμάσετε ολόκληρο το σύστημα χωρίς εξωτερικές εξαρτήσεις.

<img src="../../../translated_images/el/rag-testing.ee7541b1e23934b1.webp" alt="Δοκιμή RAG με μνήμη" width="800"/>

*Ροή εργασίας δοκιμής RAG με μνήμη που δείχνει ανάλυση εγγράφων, αποθήκευση ενσωματώσεων και αναζήτηση ομοιότητας χωρίς να απαιτείται βάση δεδομένων*

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

Αυτή η δοκιμή από το `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` δημιουργεί ένα έγγραφο στη μνήμη και επαληθεύει τη διάσπαση και τη διαχείριση μεταδεδομένων.

### Πρότυπο 6: Δοκιμή Ενσωμάτωσης MCP

Η μονάδα MCP δοκιμάζει την ενσωμάτωση του Πρωτοκόλλου Περιβάλλοντος Μοντέλου χρησιμοποιώντας μεταφορά stdio. Αυτές οι δοκιμές επαληθεύουν ότι η εφαρμογή σας μπορεί να ξεκινήσει και να επικοινωνήσει με διακομιστές MCP ως υποδιαδικασίες.

Οι δοκιμές στο `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` επικυρώνουν τη συμπεριφορά του πελάτη MCP.

**Εκτελέστε τις:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Φιλοσοφία Δοκιμών

Δοκιμάστε τον κώδικά σας, όχι το AI. Οι δοκιμές σας πρέπει να επικυρώνουν τον κώδικα που γράφετε ελέγχοντας πώς κατασκευάζονται τα prompts, πώς διαχειρίζεται η μνήμη και πώς εκτελούνται τα εργαλεία. Οι απαντήσεις του AI ποικίλλουν και δεν πρέπει να αποτελούν μέρος ισχυρισμών δοκιμής. Ρωτήστε αν το πρότυπο prompt σας αντικαθιστά σωστά τις μεταβλητές, όχι αν το AI δίνει τη σωστή απάντηση.

Χρησιμοποιήστε mocks για μοντέλα γλώσσας. Είναι εξωτερικές εξαρτήσεις που είναι αργές, δ απανηρές και μη καθοριστικές. Το mocking κάνει τις δοκιμές γρήγορες με χιλιοστά του δευτερολέπτου αντί για δευτερόλεπτα, δωρεάν χωρίς κόστος API, και καθοριστικές με το ίδιο αποτέλεσμα κάθε φορά.

Κρατήστε τις δοκιμές ανεξάρτητες. Κάθε δοκιμή πρέπει να δημιουργεί τα δικά της δεδομένα, να μην εξαρτάται από άλλες δοκιμές και να καθαρίζει μετά τον εαυτό της. Οι δοκιμές πρέπει να περνούν ανεξάρτητα από τη σειρά εκτέλεσης.

Δοκιμάστε ακραίες περιπτώσεις πέρα από την κύρια διαδρομή. Δοκιμάστε κενές εισόδους, πολύ μεγάλες εισόδους, ειδικούς χαρακτήρες, μη έγκυρες παραμέτρους και οριακές συνθήκες. Αυτές συχνά αποκαλύπτουν σφάλματα που κανονική χρήση δεν εμφανίζει.

Χρησιμοποιήστε περιγραφικά ονόματα. Συγκρίνετε `shouldMaintainConversationHistoryAcrossMultipleMessages()` με `test1()`. Το πρώτο σας λέει ακριβώς τι δοκιμάζεται, κάνοντας την αποσφαλμάτωση αποτυχιών πολύ πιο εύκολη.

## Επόμενα Βήματα

Τώρα που καταλαβαίνετε τα πρότυπα δοκιμών, εμβαθύνετε σε κάθε μονάδα:

- **[00 - Γρήγορη Έναρξη](../00-quick-start/README.md)** - Ξεκινήστε με τα βασικά των προτύπων prompt
- **[01 - Εισαγωγή](../01-introduction/README.md)** - Μάθετε διαχείριση μνήμης συνομιλίας
- **[02 - Μηχανική Προτροπής](../02/prompt-engineering/README.md)** - Εντρυφήστε στα πρότυπα προτροπής GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Δημιουργήστε συστήματα ανάκτησης με ενίσχυση
- **[04 - Εργαλεία](../04-tools/README.md)** - Υλοποιήστε κλήσεις συναρτήσεων και αλυσιδωτές χρήσεις εργαλείων
- **[05 - MCP](../05-mcp/README.md)** - Ενσωματώστε το Πρωτόκολλο Περιβάλλοντος Μοντέλου

Κάθε README μονάδας παρέχει αναλυτικές εξηγήσεις των εννοιών που δοκιμάζονται εδώ.

---

**Πλοήγηση:** [← Πίσω στην Κύρια Σελίδα](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που προσπαθούμε για ακρίβεια, παρακαλούμε να λάβετε υπόψη ότι οι αυτόματες μεταφράσεις μπορεί να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική μετάφραση από ανθρώπους. Δεν ευθυνόμαστε για τυχόν παρανοήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->