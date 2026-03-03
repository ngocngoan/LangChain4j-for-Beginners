# Δοκιμή Εφαρμογών LangChain4j

## Πίνακας Περιεχομένων

- [Γρήγορη Έναρξη](../../../docs)
- [Τι Καλύπτουν οι Δοκιμές](../../../docs)
- [Εκτέλεση των Δοκιμών](../../../docs)
- [Εκτέλεση Δοκιμών στο VS Code](../../../docs)
- [Πρότυπα Δοκιμών](../../../docs)
- [Φιλοσοφία των Δοκιμών](../../../docs)
- [Επόμενα Βήματα](../../../docs)

Αυτός ο οδηγός σας καθοδηγεί μέσα από τις δοκιμές που δείχνουν πώς να δοκιμάσετε εφαρμογές Τεχνητής Νοημοσύνης χωρίς να απαιτούνται API κλειδιά ή εξωτερικές υπηρεσίες.

## Γρήγορη Έναρξη

Τρέξτε όλες τις δοκιμές με μία εντολή:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Όταν όλες οι δοκιμές περάσουν, θα δείτε έξοδο όπως στην παρακάτω οθόνη — οι δοκιμές εκτελούνται χωρίς αποτυχίες.

<img src="../../../translated_images/el/test-results.ea5c98d8f3642043.webp" alt="Αποτελέσματα Επιτυχούς Δοκιμής" width="800"/>

*Επιτυχής εκτέλεση δοκιμών που δείχνει όλες τις δοκιμές να περνούν χωρίς αποτυχίες*

## Τι Καλύπτουν οι Δοκιμές

Αυτή η ενότητα εστιάζει σε **μοναδιαίες δοκιμές** που τρέχουν τοπικά. Κάθε δοκιμή παρουσιάζει μια συγκεκριμένη έννοια του LangChain4j απομονωμένα. Η πυραμίδα δοκιμών παρακάτω δείχνει πού εντάσσονται οι μοναδιαίες δοκιμές — αποτελούν το γρήγορο, αξιόπιστο θεμέλιο πάνω στο οποίο χτίζεται η υπόλοιπη στρατηγική δοκιμών σας.

<img src="../../../translated_images/el/testing-pyramid.2dd1079a0481e53e.webp" alt="Πυραμίδα Δοκιμών" width="800"/>

*Πυραμίδα δοκιμών που δείχνει την ισορροπία μεταξύ μοναδιαίων δοκιμών (γρήγορες, απομονωμένες), δοκιμών ενσωμάτωσης (πραγματικά συστατικά) και δοκιμών από άκρο σε άκρο. Αυτή η εκπαίδευση καλύπτει μοναδιαίες δοκιμές.*

| Ενότητα | Δοκιμές | Εστίαση | Κύρια Αρχεία |
|--------|-------|-------|-----------|
| **00 - Γρήγορη Έναρξη** | 6 | Πρότυπα prompt και αντικατάσταση μεταβλητών | `SimpleQuickStartTest.java` |
| **01 - Εισαγωγή** | 8 | Μνήμη συνομιλίας και κατάσταση συνομιλίας | `SimpleConversationTest.java` |
| **02 - Μηχανική Prompt** | 12 | Πρότυπα GPT-5.2, επίπεδα προθυμίας, δομημένη έξοδος | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Εισαγωγή εγγράφων, ενσωματώσεις, αναζήτηση ομοιότητας | `DocumentServiceTest.java` |
| **04 - Εργαλεία** | 12 | Κλήση λειτουργιών και αλυσίδες εργαλείων | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Πρωτόκολλο Πλαισίου Μοντέλου με μεταφορά stdio | `SimpleMcpTest.java` |

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

**Εκτελέστε δοκιμές για συγκεκριμένο module:**

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

**Εκτελέστε μια μόνο κλάση δοκιμής:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Εκτελέστε συγκεκριμένη μέθοδο δοκιμής:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#πρέπει να διατηρεί το ιστορικό συνομιλίας
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#πρέπει να διατηρεί το ιστορικό συνομιλιών
```

## Εκτέλεση Δοκιμών στο VS Code

Αν χρησιμοποιείτε το Visual Studio Code, ο Εξερευνητής Δοκιμών παρέχει γραφικό περιβάλλον για την εκτέλεση και αποσφαλμάτωση δοκιμών.

<img src="../../../translated_images/el/vscode-testing.f02dd5917289dced.webp" alt="VS Code Εξερευνητής Δοκιμών" width="800"/>

*VS Code Εξερευνητής Δοκιμών που δείχνει το δέντρο δοκιμών με όλες τις κλάσεις δοκιμών Java και μεμονωμένες μεθόδους δοκιμών*

**Για να τρέξετε δοκιμές στο VS Code:**

1. Ανοίξτε τον Εξερευνητή Δοκιμών κάνοντας κλικ στο εικονίδιο του δοκιμαστικού σωλήνα στη γραμμή δραστηριότητας
2. Διευρύνετε το δέντρο δοκιμών για να δείτε όλα τα modules και τις κλάσεις δοκιμών
3. Κάντε κλικ στο κουμπί αναπαραγωγής δίπλα σε οποιαδήποτε δοκιμή για να τη τρέξετε μεμονωμένα
4. Κάντε κλικ στο "Run All Tests" για να εκτελέσετε ολόκληρο το σύνολο
5. Κάντε δεξί κλικ σε οποιαδήποτε δοκιμή και επιλέξτε "Debug Test" για να ορίσετε σημεία διακοπής και να βήμα-βήμα μέσα στον κώδικα

Ο Εξερευνητής Δοκιμών δείχνει πράσινα σημάδια για τις επιτυχημένες δοκιμές και παρέχει λεπτομερή μηνύματα αποτυχίας όταν οι δοκιμές αποτυγχάνουν.

## Πρότυπα Δοκιμών

### Πρότυπο 1: Δοκιμή Προτύπων Prompt

Το απλούστερο πρότυπο δοκιμάζει πρότυπα prompt χωρίς να καλεί κανένα μοντέλο AI. Επαληθεύετε ότι η αντικατάσταση των μεταβλητών λειτουργεί σωστά και τα prompts μορφοποιούνται όπως αναμένεται.

<img src="../../../translated_images/el/prompt-template-testing.b902758ddccc8dee.webp" alt="Δοκιμή Προτύπου Prompt" width="800"/>

*Δοκιμή πρότυπων prompt που δείχνει τη ροή αντικατάστασης μεταβλητών: πρότυπο με θέσεις κρατήματος → εφαρμόζονται τιμές → επιβεβαιώνεται η μορφοποιημένη έξοδος*

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

**Τρέξτε την:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#μορφοποίησηΠροτύπουΠροτροπήςΔοκιμής
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#δοκιμήΜορφοποίησηςΠροτύπουΠροτροπής
```

### Πρότυπο 2: Mocking Μοντέλων Γλώσσας

Όταν δοκιμάζετε τη λογική συνομιλίας, χρησιμοποιήστε το Mockito για να δημιουργήσετε ψεύτικα μοντέλα που επιστρέφουν προκαθορισμένες απαντήσεις. Αυτό κάνει τις δοκιμές γρήγορες, δωρεάν και ντετερμινιστικές.

<img src="../../../translated_images/el/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Σύγκριση Mock vs Πραγματικό API" width="800"/>

*Σύγκριση που δείχνει γιατί προτιμώνται τα mocks για δοκιμές: είναι γρήγορα, δωρεάν, ντετερμινιστικά και δεν απαιτούν API κλειδιά*

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
        assertThat(history).hasSize(6); // 3 μηνύματα χρήστη + 3 μηνύματα AI
    }
}
```

Αυτό το πρότυπο εμφανίζεται στο `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Το mock διασφαλίζει συνεπή συμπεριφορά ώστε να μπορείτε να επαληθεύσετε ότι η διαχείριση μνήμης λειτουργεί σωστά.

### Πρότυπο 3: Δοκιμή Απομόνωσης Συνομιλίας

Η μνήμη συνομιλίας πρέπει να κρατά τους πολλαπλούς χρήστες απομονωμένους. Αυτή η δοκιμή επαληθεύει ότι οι συνομιλίες δεν αναμειγνύουν συμφραζόμενα.

<img src="../../../translated_images/el/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Απομόνωση Συνομιλίας" width="800"/>

*Δοκιμή απομόνωσης συνομιλίας που δείχνει ξεχωριστές αποθήκες μνήμης για διαφορετικούς χρήστες για να αποτρέπει το ανακάτεμα συμφραζομένων*

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

Τα εργαλεία είναι λειτουργίες που το AI μπορεί να καλέσει. Δοκιμάστε τα απευθείας για να βεβαιωθείτε ότι λειτουργούν σωστά ανεξάρτητα από τις αποφάσεις του AI.

<img src="../../../translated_images/el/tools-testing.3e1706817b0b3924.webp" alt="Δοκιμή Εργαλείων" width="800"/>

*Δοκιμή εργαλείων ανεξάρτητα που δείχνει εκτέλεση ψεύτικου εργαλείου χωρίς κλήσεις AI για επαλήθευση επιχειρηματικής λογικής*

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

Αυτές οι δοκιμές από το `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` επαληθεύουν τη λογική των εργαλείων χωρίς την εμπλοκή του AI. Το παράδειγμα σύνδεσης δείχνει πώς η έξοδος ενός εργαλείου τροφοδοτεί την είσοδο άλλου.

### Πρότυπο 5: Δοκιμή RAG Μέσω Μνήμης

Τα συστήματα RAG παραδοσιακά απαιτούν διανυσματικές βάσεις δεδομένων και υπηρεσίες ενσωμάτωσης. Το πρότυπο μέσω μνήμης σας επιτρέπει να δοκιμάσετε ολόκληρη τη ροή χωρίς εξωτερικές εξαρτήσεις.

<img src="../../../translated_images/el/rag-testing.ee7541b1e23934b1.webp" alt="Δοκιμή RAG Μέσω Μνήμης" width="800"/>

*Ροή εργασίας δοκιμής RAG μέσω μνήμης που δείχνει ανάλυση εγγράφων, αποθήκευση ενσωματώσεων και αναζήτηση ομοιότητας χωρίς να απαιτείται βάση δεδομένων*

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

Αυτή η δοκιμή από το `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` δημιουργεί ένα έγγραφο στη μνήμη και επαληθεύει το διαχωρισμό σε κομμάτια και τη διαχείριση μεταδεδομένων.

### Πρότυπο 6: Δοκιμή Ενσωμάτωσης MCP

Το module MCP δοκιμάζει την ενσωμάτωση Πρωτοκόλλου Πλαισίου Μοντέλου χρησιμοποιώντας μεταφορά stdio. Αυτές οι δοκιμές επιβεβαιώνουν ότι η εφαρμογή σας μπορεί να ξεκινήσει και να επικοινωνήσει με MCP servers ως υπο-διαδικασίες.

Οι δοκιμές στο `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` επαληθεύουν τη συμπεριφορά πελάτη MCP.

**Τρέξτε τις:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Φιλοσοφία των Δοκιμών

Δοκιμάστε τον κώδικά σας, όχι το AI. Οι δοκιμές σας πρέπει να επικυρώνουν τον κώδικα που γράφετε ελέγχοντας πώς κατασκευάζονται τα prompts, πώς διαχειρίζεται η μνήμη και πώς εκτελούνται τα εργαλεία. Οι απαντήσεις AI διαφέρουν και δεν θα πρέπει να αποτελούν μέρος των ελέγχων. Ρωτήστε τον εαυτό σας αν το πρότυπο prompt αντικαθιστά σωστά τις μεταβλητές, όχι αν το AI δίνει τη σωστή απάντηση.

Χρησιμοποιήστε mocks για τα μοντέλα γλώσσας. Είναι εξωτερικές εξαρτήσεις που είναι αργές, ακριβές και μη ντετερμινιστικές. Το mocking κάνει τις δοκιμές γρήγορες με χιλιοστά του δευτερολέπτου αντί για δευτερόλεπτα, δωρεάν χωρίς κόστος API, και ντετερμινιστικές με το ίδιο αποτέλεσμα κάθε φορά.

Διατηρήστε τις δοκιμές ανεξάρτητες. Κάθε δοκιμή πρέπει να ετοιμάζει τα δικά της δεδομένα, να μην εξαρτάται από άλλες δοκιμές και να καθαρίζει μετά τη ολοκλήρωση. Οι δοκιμές πρέπει να περνούν ανεξάρτητα από τη σειρά εκτέλεσης.

Δοκιμάστε ακραίες περιπτώσεις πέρα από το ευτυχές μονοπάτι. Δοκιμάστε άδειες εισόδους, πολύ μεγάλες εισόδους, ειδικούς χαρακτήρες, μη έγκυρες παραμέτρους και οριακές συνθήκες. Αυτά συχνά αποκαλύπτουν σφάλματα που η φυσιολογική χρήση δεν εκθέτει.

Χρησιμοποιήστε περιγραφικά ονόματα. Συγκρίνετε το `shouldMaintainConversationHistoryAcrossMultipleMessages()` με το `test1()`. Το πρώτο σας λέει ακριβώς τι ελέγχεται, καθιστώντας την αποσφαλμάτωση των αποτυχιών πολύ πιο εύκολη.

## Επόμενα Βήματα

Τώρα που κατανοείτε τα πρότυπα δοκιμών, εμβαθύνετε σε κάθε module:

- **[00 - Γρήγορη Έναρξη](../00-quick-start/README.md)** - Ξεκινήστε με τα βασικά των προτύπων prompt
- **[01 - Εισαγωγή](../01-introduction/README.md)** - Μάθετε διαχείριση μνήμης συνομιλίας
- **[02 - Μηχανική Prompt](../02-prompt-engineering/README.md)** - Κατακτήστε πρότυπα prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Δημιουργήστε συστήματα ανάκτησης-ενισχυμένης δημιουργίας
- **[04 - Εργαλεία](../04-tools/README.md)** - Εφαρμόστε κλήσεις λειτουργιών και αλυσίδες εργαλείων
- **[05 - MCP](../05-mcp/README.md)** - Ενσωματώστε το Πρωτόκολλο Πλαισίου Μοντέλου

Το README κάθε module παρέχει λεπτομερείς εξηγήσεις για τις έννοιες που δοκιμάζονται εδώ.

---

**Πλοήγηση:** [← Επιστροφή στην Κύρια Σελίδα](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που προσπαθούμε για ακρίβεια, παρακαλούμε να γνωρίζετε ότι οι αυτοματοποιημένες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική μετάφραση από ανθρώπους. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->