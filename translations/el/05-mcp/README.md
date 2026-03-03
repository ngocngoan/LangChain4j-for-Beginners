# Module 05: Πρωτόκολλο Πλαισίου Μοντέλου (MCP)

## Περιεχόμενα

- [Τι Θα Μάθετε](../../../05-mcp)
- [Τι είναι το MCP;](../../../05-mcp)
- [Πώς Λειτουργεί το MCP](../../../05-mcp)
- [Το Agentic Module](../../../05-mcp)
- [Εκτέλεση Παραδειγμάτων](../../../05-mcp)
  - [Προαπαιτούμενα](../../../05-mcp)
- [Γρήγορη Εκκίνηση](../../../05-mcp)
  - [Ενέργειες Αρχείων (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Εκτέλεση της Επίδειξης](../../../05-mcp)
    - [Πώς Λειτουργεί ο Supervisor](../../../05-mcp)
    - [Στρατηγικές Απάντησης](../../../05-mcp)
    - [Κατανόηση του Αποτελέσματος](../../../05-mcp)
    - [Εξήγηση Χαρακτηριστικών Agentic Module](../../../05-mcp)
- [Κύριες Έννοιες](../../../05-mcp)
- [Συγχαρητήρια!](../../../05-mcp)
  - [Τι Ακολουθεί;](../../../05-mcp)

## Τι Θα Μάθετε

Έχετε κατασκευάσει συνομιλητική τεχνητή νοημοσύνη, έχετε κυριαρχήσει στα prompts, έχετε θεμελιώσει απαντήσεις σε έγγραφα και έχετε δημιουργήσει agents με εργαλεία. Αλλά όλα αυτά τα εργαλεία ήταν προσαρμοσμένα για την ειδική σας εφαρμογή. Τι θα γινόταν αν μπορούσατε να δώσετε στην AI σας πρόσβαση σε ένα τυποποιημένο οικοσύστημα εργαλείων που ο καθένας μπορεί να δημιουργήσει και να μοιραστεί; Σε αυτό το module, θα μάθετε πώς να το κάνετε ακριβώς αυτό με το Πρωτόκολλο Πλαισίου Μοντέλου (MCP) και το agentic module του LangChain4j. Πρώτα παρουσιάζουμε έναν απλό MCP αναγνώστη αρχείων και μετά δείχνουμε πώς ενσωματώνεται εύκολα σε προηγμένες agentic ροές εργασίας χρησιμοποιώντας το πρότυπο Supervisor Agent.

## Τι είναι το MCP;

Το Πρωτόκολλο Πλαισίου Μοντέλου (MCP) παρέχει ακριβώς αυτό - έναν πρότυπο τρόπο για εφαρμογές AI να ανακαλύπτουν και να χρησιμοποιούν εξωτερικά εργαλεία. Αντί να γράφετε προσαρμοσμένες ενσωματώσεις για κάθε πηγή δεδομένων ή υπηρεσία, συνδέεστε σε MCP servers που εκθέτουν τις δυνατότητές τους σε συνεπή μορφή. Ο agent AI σας μπορεί τότε να ανακαλύψει και να χρησιμοποιήσει αυτά τα εργαλεία αυτόματα.

Το παρακάτω διάγραμμα δείχνει τη διαφορά — χωρίς MCP, κάθε ενσωμάτωση απαιτεί προσαρμοσμένο σύνδεσμο σημεία προς σημεία· με MCP, ένα πρωτόκολλο συνδέει την εφαρμογή σας με οποιοδήποτε εργαλείο:

<img src="../../../translated_images/el/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Πριν από το MCP: Πολύπλοκες ενσωματώσεις σημεία προς σημεία. Μετά το MCP: Ένα πρωτόκολλο, ατελείωτες δυνατότητες.*

Το MCP λύνει ένα θεμελιώδες πρόβλημα στην ανάπτυξη AI: κάθε ενσωμάτωση είναι προσαρμοσμένη. Θέλετε να αποκτήσετε πρόσβαση στο GitHub; Προσαρμοσμένος κώδικας. Θέλετε να διαβάσετε αρχεία; Προσαρμοσμένος κώδικας. Θέλετε να κάνετε ερωτήματα σε μια βάση δεδομένων; Προσαρμοσμένος κώδικας. Και καμία από αυτές τις ενσωματώσεις δεν δουλεύει με άλλες εφαρμογές AI.

Το MCP το τυποποιεί αυτό. Ένας MCP server εκθέτει εργαλεία με σαφείς περιγραφές και σχήματα. Κάθε MCP client μπορεί να συνδεθεί, να ανακαλύψει διαθέσιμα εργαλεία και να τα χρησιμοποιήσει. Φτιάξτε μια φορά, χρησιμοποιήστε παντού.

Το παρακάτω διάγραμμα απεικονίζει αυτή την αρχιτεκτονική — ένας MCP client (η εφαρμογή AI σας) συνδέεται με πολλούς MCP servers, καθένας εκθέτει το δικό του σύνολο εργαλείων μέσω του τυποποιημένου πρωτοκόλλου:

<img src="../../../translated_images/el/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Αρχιτεκτονική Πρωτοκόλλου Πλαισίου Μοντέλου - τυποποιημένη ανακάλυψη και εκτέλεση εργαλείων*

## Πώς Λειτουργεί το MCP

Στο παρασκήνιο, το MCP χρησιμοποιεί μια στρωματοποιημένη αρχιτεκτονική. Η Java εφαρμογή σας (ο MCP client) ανακαλύπτει διαθέσιμα εργαλεία, στέλνει αιτήματα JSON-RPC μέσω ενός στρώματος μεταφοράς (Stdio ή HTTP), και ο MCP server εκτελεί λειτουργίες και επιστρέφει αποτελέσματα. Το παρακάτω διάγραμμα αναλύει κάθε στρώμα αυτού του πρωτοκόλλου:

<img src="../../../translated_images/el/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Πώς λειτουργεί το MCP στο παρασκήνιο — οι πελάτες ανακαλύπτουν εργαλεία, ανταλλάσσουν μηνύματα JSON-RPC και εκτελούν λειτουργίες μέσω ενός στρώματος μεταφοράς.*

**Αρχιτεκτονική Server-Client**

Το MCP χρησιμοποιεί μοντέλο client-server. Οι servers παρέχουν εργαλεία - ανάγνωση αρχείων, ερωτήματα βάσεων δεδομένων, κλήσεις API. Οι clients (η εφαρμογή AI σας) συνδέονται στους servers και χρησιμοποιούν τα εργαλεία τους.

Για να χρησιμοποιήσετε το MCP με το LangChain4j, προσθέστε αυτή την εξάρτηση Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Ανακάλυψη Εργαλείων**

Όταν ο client σας συνδεθεί σε έναν MCP server, ρωτάει "Ποια εργαλεία έχετε;" Ο server απαντά με μια λίστα διαθέσιμων εργαλείων, το καθένα με περιγραφές και σχήματα παραμέτρων. Ο agent AI σας μπορεί να αποφασίσει ποια εργαλεία θα χρησιμοποιήσει βασισμένος στα αιτήματα του χρήστη. Το παρακάτω διάγραμμα δείχνει αυτήν την ανταλλαγή — ο client στέλνει ένα αίτημα `tools/list` και ο server επιστρέφει τα διαθέσιμα εργαλεία με περιγραφές και σχήματα παραμέτρων:

<img src="../../../translated_images/el/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Η AI ανακαλύπτει τα διαθέσιμα εργαλεία κατά την εκκίνηση — τώρα γνωρίζει ποιες δυνατότητες είναι διαθέσιμες και μπορεί να αποφασίσει ποια να χρησιμοποιήσει.*

**Μηχανισμοί Μεταφοράς**

Το MCP υποστηρίζει διαφορετικούς μηχανισμούς μεταφοράς. Οι δύο επιλογές είναι Stdio (για επικοινωνία τοπικών υπο-διαδικασιών) και Streamable HTTP (για απομακρυσμένους servers). Αυτό το module παρουσιάζει τη μεταφορά Stdio:

<img src="../../../translated_images/el/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Μηχανισμοί μεταφοράς MCP: HTTP για απομακρυσμένους servers, Stdio για τοπικές διαδικασίες*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Για τοπικές διαδικασίες. Η εφαρμογή σας δημιουργεί έναν server ως υπο-διαδικασία και επικοινωνεί μέσω standard input/output. Χρήσιμο για πρόσβαση στο σύστημα αρχείων ή εργαλεία γραμμής εντολών.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Ο server `@modelcontextprotocol/server-filesystem` εκθέτει τα παρακάτω εργαλεία, όλα περιορισμένα στους φακέλους που καθορίζετε:

| Εργαλείο | Περιγραφή |
|------|-------------|
| `read_file` | Ανάγνωση του περιεχομένου ενός αρχείου |
| `read_multiple_files` | Ανάγνωση πολλών αρχείων με μία κλήση |
| `write_file` | Δημιουργία ή αντικατάσταση αρχείου |
| `edit_file` | Επιλεκτικές επεξεργασίες εύρεσης και αντικατάστασης |
| `list_directory` | Λίστα αρχείων και φακέλων σε μια διαδρομή |
| `search_files` | Αναδρομική αναζήτηση αρχείων που ταιριάζουν σε πρότυπο |
| `get_file_info` | Λήψη μεταδεδομένων αρχείου (μέγεθος, χρονικά σημεία, δικαιώματα) |
| `create_directory` | Δημιουργία φακέλου (συμπεριλαμβανομένων γονικών φακέλων) |
| `move_file` | Μετακίνηση ή μετονομασία αρχείου ή φακέλου |

Το παρακάτω διάγραμμα δείχνει πώς λειτουργεί η μεταφορά Stdio κατά την εκτέλεση — η Java εφαρμογή σας δημιουργεί τον MCP server ως υπο-διαδικασία και επικοινωνούν μέσω stdin/stdout pipes, χωρίς δίκτυο ή HTTP:

<img src="../../../translated_images/el/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Μεταφορά Stdio σε δράση — η εφαρμογή σας δημιουργεί τον MCP server ως υπο-διαδικασία και επικοινωνεί μέσω stdin/stdout pipes.*

> **🤖 Δοκιμάστε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) και ρωτήστε:
> - "Πώς λειτουργεί η μεταφορά Stdio και πότε πρέπει να την χρησιμοποιήσω αντί για HTTP;"
> - "Πώς διαχειρίζεται το LangChain4j τον κύκλο ζωής των υπο-διαδικασιών MCP server;"
> - "Ποιες είναι οι επιπτώσεις ασφάλειας από την παροχή πρόσβασης AI στο σύστημα αρχείων;"

## Το Agentic Module

Ενώ το MCP παρέχει τυποποιημένα εργαλεία, το **agentic module** του LangChain4j παρέχει έναν δηλωτικό τρόπο για να χτίσετε agents που ορχηστρώνουν αυτά τα εργαλεία. Η `@Agent` επισήμανση και οι `AgenticServices` σας επιτρέπουν να ορίζετε τη συμπεριφορά του agent μέσω διεπαφών αντί για επιτακτικό κώδικα.

Σε αυτό το module, θα εξερευνήσετε το πρότυπο **Supervisor Agent** — μια προηγμένη προσέγγιση agentic AI όπου ένας "επόπτης" agent αποφασίζει δυναμικά ποιοι υπο-agent να καλέσει με βάση τα αιτήματα του χρήστη. Θα συνδυάσουμε αυτές τις δύο έννοιες δίνοντας σε έναν από τους υπο-agent μας δυνατότητες πρόσβασης αρχείων με το MCP.

Για να χρησιμοποιήσετε το agentic module, προσθέστε αυτή την εξάρτηση Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Σημείωση:** Το module `langchain4j-agentic` χρησιμοποιεί ξεχωριστό property έκδοσης (`langchain4j.mcp.version`) επειδή εκδίδεται σε διαφορετικό χρονοδιάγραμμα από τις βασικές βιβλιοθήκες LangChain4j.

> **⚠️ Πειραματικό:** Το module `langchain4j-agentic` είναι **πειραματικό** και υπόκειται σε αλλαγές. Ο σταθερός τρόπος για να φτιάξετε βοηθούς AI παραμένει το `langchain4j-core` με προσαρμοσμένα εργαλεία (Module 04).

## Εκτέλεση Παραδειγμάτων

### Προαπαιτούμενα

- Ολοκληρωμένο [Module 04 - Εργαλεία](../04-tools/README.md) (αυτό το module βασίζεται στα προσαρμοσμένα εργαλεία και συγκρίνει με εργαλεία MCP)
- Αρχείο `.env` στον ριζικό φάκελο με διαπιστευτήρια Azure (δημιουργήθηκε με `azd up` στο Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ και npm (για MCP servers)

> **Σημείωση:** Αν δεν έχετε ρυθμίσει ακόμα τις περιβαλλοντικές σας μεταβλητές, δείτε το [Module 01 - Εισαγωγή](../01-introduction/README.md) για οδηγίες ανάπτυξης (`azd up` δημιουργεί αυτόματα το αρχείο `.env`), ή αντιγράψτε το `.env.example` σε `.env` στον ριζικό φάκελο και συμπληρώστε τις τιμές σας.

## Γρήγορη Εκκίνηση

**Χρήση VS Code:** Απλά κάντε δεξί κλικ σε οποιοδήποτε αρχείο επίδειξης στον Explorer και επιλέξτε **"Run Java"**, ή χρησιμοποιήστε τις ρυθμίσεις εκκίνησης από τον πίνακα Run and Debug (βεβαιωθείτε πρώτα ότι το αρχείο `.env` έχει ρυθμιστεί με τα διαπιστευτήρια Azure).

**Χρήση Maven:** Εναλλακτικά, μπορείτε να τρέξετε από τη γραμμή εντολών με τα παραδείγματα παρακάτω.

### Ενέργειες Αρχείων (Stdio)

Αυτό επιδεικνύει εργαλεία βασισμένα σε τοπικές υπο-διαδικασίες.

**✅ Χωρίς προαπαιτούμενα** - ο MCP server δημιουργείται αυτόματα.

**Χρήση των Scripts Εκκίνησης (Συνιστάται):**

Τα scripts εκκίνησης φορτώνουν αυτόματα τις περιβαλλοντικές μεταβλητές από το ριζικό αρχείο `.env`:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Χρήση VS Code:** Κάντε δεξί κλικ στο `StdioTransportDemo.java` και επιλέξτε **"Run Java"** (βεβαιωθείτε ότι το `.env` είναι ρυθμισμένο).

Η εφαρμογή δημιουργεί αυτόματα έναν MCP server συστήματος αρχείων και διαβάζει ένα τοπικό αρχείο. Παρατηρήστε πώς γίνεται η διαχείριση της υπο-διαδικασίας για εσάς.

**Αναμενόμενη έξοδος:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

Το **πρότυπο Supervisor Agent** είναι μια **ευέλικτη** μορφή agentic AI. Ένας Supervisor χρησιμοποιεί ένα LLM για να αποφασίζει αυτόνομα ποιοι agents θα κληθούν βάσει του αιτήματος του χρήστη. Στο επόμενο παράδειγμα, συνδυάζουμε πρόσβαση αρχείων μέσω MCP με έναν agent LLM για να δημιουργήσουμε μια επίβλεψη ροής ανάγνωσης αρχείου → αναφορά.

Στην επίδειξη, ο `FileAgent` διαβάζει ένα αρχείο χρησιμοποιώντας εργαλεία συστήματος αρχείων MCP, και ο `ReportAgent` δημιουργεί μια δομημένη αναφορά με εκτελεστική περίληψη (1 πρόταση), 3 βασικά σημεία και συστάσεις. Ο Supervisor ορχηστρώνει αυτή τη ροή αυτόματα:

<img src="../../../translated_images/el/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Ο Supervisor χρησιμοποιεί το LLM του για να αποφασίσει ποιοι agents θα κληθούν και με ποια σειρά — χωρίς ανάγκη κωδικοποιημένου δρομολογίου.*

Έτσι μοιάζει το συγκεκριμένο workflow για τη ροή αρχείου σε αναφορά:

<img src="../../../translated_images/el/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*Ο FileAgent διαβάζει το αρχείο μέσω εργαλείων MCP, μετά ο ReportAgent μετατρέπει το ακατέργαστο περιεχόμενο σε δομημένη αναφορά.*

Κάθε agent αποθηκεύει το αποτέλεσμα του στο **Agentic Scope** (κοινή μνήμη), επιτρέποντας σε κατώτερους agents να έχουν πρόσβαση σε προηγούμενα αποτελέσματα. Αυτό δείχνει πώς τα εργαλεία MCP ενσωματώνονται αρμονικά σε agentic ροές εργασίας — ο Supervisor δεν χρειάζεται να ξέρει *πώς* διαβάζονται τα αρχεία, μόνο ότι ο `FileAgent` μπορεί να το κάνει.

#### Εκτέλεση της Επίδειξης

Τα scripts εκκίνησης φορτώνουν αυτόματα τις περιβαλλοντικές μεταβλητές από το ριζικό `.env`:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Χρήση VS Code:** Δεξί κλικ στο `SupervisorAgentDemo.java` και επιλογή **"Run Java"** (βεβαιωθείτε ότι το `.env` είναι ρυθμισμένο).

#### Πώς Λειτουργεί ο Supervisor

Πριν φτιάξετε agents, πρέπει να συνδέσετε τη μεταφορά MCP σε έναν client και να την τυλίξετε ως `ToolProvider`. Έτσι τα εργαλεία του MCP server γίνονται διαθέσιμα στους agents σας:

```java
// Δημιουργήστε έναν πελάτη MCP από τη μεταφορά
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Περιβάλλετε τον πελάτη ως ToolProvider — αυτό γεφυρώνει τα εργαλεία MCP στο LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Τώρα μπορείτε να εγχύσετε το `mcpToolProvider` σε οποιονδήποτε agent χρειάζεται εργαλεία MCP:

```java
// Βήμα 1: Το FileAgent διαβάζει αρχεία χρησιμοποιώντας εργαλεία MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Διαθέτει εργαλεία MCP για λειτουργίες αρχείων
        .build();

// Βήμα 2: Το ReportAgent δημιουργεί δομημένες αναφορές
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Ο Supervisor συντονίζει τη ροή εργασίας από το αρχείο στην αναφορά
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Επιστροφή της τελικής αναφοράς
        .build();

// Ο Supervisor αποφασίζει ποιοι agents θα κληθούν βάσει του αιτήματος
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Στρατηγικές Απάντησης

Όταν διαμορφώνετε έναν `SupervisorAgent`, καθορίζετε πώς πρέπει να διατυπώνει την τελική απάντησή του στον χρήστη μετά την ολοκλήρωση των εργασιών από τους υπο-agents. Το παρακάτω διάγραμμα δείχνει τις τρεις διαθέσιμες στρατηγικές — το LAST επιστρέφει απευθείας το αποτέλεσμά του τελευταίου agent, το SUMMARY συνθέτει όλα τα αποτελέσματα μέσω ενός LLM, και το SCORED επιλέγει όποιο έχει τη μεγαλύτερη βαθμολογία βάσει του αρχικού αιτήματος:

<img src="../../../translated_images/el/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Τρεις στρατηγικές για το πώς ο Supervisor διατυπώνει την τελική απάντηση — επιλέξτε ανάλογα αν θέλετε το αποτέλεσμα του τελευταίου agent, μια συνοπτική περίληψη ή την καλύτερη βαθμολογημένη επιλογή.*

Οι διαθέσιμες στρατηγικές είναι:

| Στρατηγική | Περιγραφή |
|----------|-------------|
| **LAST** | Ο supervisor επιστρέφει το αποτέλεσμα του τελευταίου υπο-agent ή εργαλείου που κλήθηκε. Αυτό είναι χρήσιμο όταν ο τελικός agent στη ροή εργασίας έχει σχεδιαστεί ειδικά για να παράγει την πλήρη, τελική απάντηση (π.χ., ένας "Summary Agent" σε pipeline έρευνας). |
| **SUMMARY** | Ο supervisor χρησιμοποιεί το δικό του εσωτερικό Γλωσσικό Μοντέλο (LLM) για να συνθέσει μια περίληψη ολόκληρης της αλληλεπίδρασης και όλων των υπο-agent, και επιστρέφει αυτή την περίληψη ως τελική απάντηση. Αυτό παρέχει μια καθαρή, συγκεντρωτική απάντηση στον χρήστη. |
| **SCORED** | Το σύστημα χρησιμοποιεί ένα εσωτερικό LLM για να βαθμολογήσει τόσο την απάντηση LAST όσο και την περίληψη SUMMARY της αλληλεπίδρασης βάσει του αρχικού αιτήματος χρήστη, επιστρέφοντας όποιο αποτέλεσμά έχει υψηλότερη βαθμολογία. |
Δείτε το [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) για την πλήρη υλοποίηση.

> **🤖 Δοκιμάστε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) και ρωτήστε:
> - "Πώς αποφασίζει ο Supervisor ποιον agent να καλέσει;"
> - "Ποια είναι η διαφορά μεταξύ των σχεδίων εργασίας Supervisor και Sequential;"
> - "Πώς μπορώ να προσαρμόσω τη συμπεριφορά σχεδιασμού του Supervisor;"

#### Κατανόηση του Αποτελέσματος

Όταν εκτελέσετε το demo, θα δείτε μια δομημένη περιήγηση για το πώς ο Supervisor οργανώνει πολλούς agents. Ακολουθεί τι σημαίνει κάθε ενότητα:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Η κεφαλίδα** εισάγει το concept της ροής εργασίας: μια στοχευμένη διαδικασία από την ανάγνωση αρχείων μέχρι τη δημιουργία αναφορών.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**Διάγραμμα Ροής Εργασίας** δείχνει τη ροή των δεδομένων μεταξύ των agents. Κάθε agent έχει συγκεκριμένο ρόλο:  
- **FileAgent** διαβάζει αρχεία χρησιμοποιώντας εργαλεία MCP και αποθηκεύει το ακατέργαστο περιεχόμενο στο `fileContent`  
- **ReportAgent** χρησιμοποιεί αυτό το περιεχόμενο και παράγει μια δομημένη αναφορά στο `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Αίτημα Χρήστη** δείχνει την εργασία. Ο Supervisor το αναλύει και αποφασίζει να καλέσει FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Οργάνωση από τον Supervisor** δείχνει τη ροή 2 βημάτων σε δράση:  
1. **FileAgent** διαβάζει το αρχείο μέσω MCP και αποθηκεύει το περιεχόμενο  
2. **ReportAgent** λαμβάνει το περιεχόμενο και δημιουργεί τη δομημένη αναφορά

Ο Supervisor πήρε αυτές τις αποφάσεις **αυτόνομα** βάσει του αιτήματος του χρήστη.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### Επεξήγηση των Χαρακτηριστικών του Agentic Module

Το παράδειγμα παρουσιάζει αρκετά προηγμένα χαρακτηριστικά του agentic module. Ας δούμε πιο προσεκτικά το Agentic Scope και τα Agent Listeners.

**Agentic Scope** δείχνει τη κοινή μνήμη όπου οι agents αποθηκεύουν τα αποτελέσματά τους χρησιμοποιώντας `@Agent(outputKey="...")`. Αυτό επιτρέπει:  
- Μετέπειτα agents να έχουν πρόσβαση σε εξόδους προηγούμενων agents  
- Στον Supervisor να συνθέτει μια τελική απάντηση  
- Σε εσάς να ελέγχετε τι έχει παράγει κάθε agent

Το παρακάτω διάγραμμα δείχνει πώς λειτουργεί το Agentic Scope ως κοινή μνήμη στη ροή εργασίας από αρχείο σε αναφορά — ο FileAgent γράφει την έξοδο με το κλειδί `fileContent`, ο ReportAgent το διαβάζει και γράφει τη δική του έξοδο στο `report`:

<img src="../../../translated_images/el/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Το Agentic Scope λειτουργεί ως κοινή μνήμη — ο FileAgent γράφει το `fileContent`, ο ReportAgent το διαβάζει και γράφει το `report`, και ο κώδικάς σας διαβάζει το τελικό αποτέλεσμα.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Ακατέργαστα δεδομένα αρχείου από το FileAgent
String report = scope.readState("report");            // Δομημένη αναφορά από το ReportAgent
```
  
**Agent Listeners** επιτρέπουν την παρακολούθηση και αποσφαλμάτωση της εκτέλεσης των agents. Η βήμα-βήμα έξοδος που βλέπετε στο demo προέρχεται από έναν AgentListener που συνδέεται με κάθε κλήση agent:  
- **beforeAgentInvocation** - Καλείται όταν ο Supervisor επιλέγει agent, ώστε να δείτε ποιος agent επιλέχθηκε και γιατί  
- **afterAgentInvocation** - Καλείται μετά την ολοκλήρωση ενός agent, δείχνοντας το αποτέλεσμα  
- **inheritedBySubagents** - Όταν είναι true, ο listener παρακολουθεί όλους τους agents στην ιεραρχία

Το επόμενο διάγραμμα δείχνει ολόκληρο τον κύκλο ζωής των Agent Listeners, συμπεριλαμβανομένου του πώς το `onError` διαχειρίζεται αποτυχίες κατά την εκτέλεση agent:

<img src="../../../translated_images/el/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Οι Agent Listeners συνδέονται με τον κύκλο ζωής εκτέλεσης — παρακολουθούν πότε οι agents ξεκινούν, ολοκληρώνονται ή αντιμετωπίζουν σφάλματα.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Διαδώστε σε όλους τους υποπράκτορες
    }
};
```
  
Πέρα από το μοτίβο Supervisor, το `langchain4j-agentic` module παρέχει πολλά ισχυρά μοτίβα ροής εργασίας. Το παρακάτω διάγραμμα δείχνει τα πέντε — από απλές σειριακές ροές μέχρι εργασίες έγκρισης με συμμετοχή ανθρώπου:

<img src="../../../translated_images/el/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Πέντε μοτίβα ροής εργασίας για τον συντονισμό agents — από απλές σειριακές ροές μέχρι εργασίες έγκρισης με συμμετοχή ανθρώπου.*

| Μοτίβο | Περιγραφή | Περίπτωση Χρήσης |
|---------|-------------|----------|
| **Sequential** | Εκτέλεση agents με σειρά, η έξοδος ρέει στον επόμενο | Ροές εργασίας: έρευνα → ανάλυση → αναφορά |
| **Parallel** | Εκτέλεση agents ταυτόχρονα | Ανεξάρτητες εργασίες: καιρός + νέα + χρηματιστήριο |
| **Loop** | Επανάληψη μέχρι να πληρωθεί συνθήκη | Βαθμολόγηση ποιότητας: βελτίωση μέχρι βαθμός ≥ 0.8 |
| **Conditional** | Δρομολόγηση βάσει συνθηκών | Κατάταξη → δρομολόγηση σε ειδικευμένο agent |
| **Human-in-the-Loop** | Προσθήκη ανθρώπινων σημεία ελέγχου | Ροές εργασίας έγκρισης, αναθεώρηση περιεχομένου |

## Κύριες Έννοιες

Τώρα που έχετε εξερευνήσει το MCP και το agentic module σε δράση, ας συνοψίσουμε πότε να χρησιμοποιήσετε κάθε προσέγγιση.

Ένα από τα μεγαλύτερα πλεονεκτήματα του MCP είναι το αναπτυσσόμενο οικοσύστημά του. Το παρακάτω διάγραμμα δείχνει πώς ένα ενιαίο παγκόσμιο πρωτόκολλο συνδέει την AI εφαρμογή σας με μια μεγάλη ποικιλία MCP servers — από πρόσβαση σε αρχεία και βάσεις δεδομένων μέχρι GitHub, email, web scraping και άλλα:

<img src="../../../translated_images/el/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*Το MCP δημιουργεί ένα οικοσύστημα καθολικού πρωτοκόλλου — κάθε MCP-συμβατός server δουλεύει με κάθε MCP-συμβατό client, επιτρέποντας την κοινή χρήση εργαλείων μεταξύ εφαρμογών.*

**Το MCP** είναι ιδανικό όταν θέλετε να αξιοποιήσετε υπάρχοντα οικοσυστήματα εργαλείων, να φτιάξετε εργαλεία που μοιράζονται πολλές εφαρμογές, να ενσωματώσετε υπηρεσίες τρίτων με τυποποιημένα πρωτόκολλα ή να αντικαταστήσετε υλοποιήσεις εργαλείων χωρίς αλλαγή κώδικα.

**Το Agentic Module** λειτουργεί καλύτερα όταν θέλετε δηλωτικούς ορισμούς agents με `@Agent` annotations, χρειάζεστε οργάνωση ροής εργασίας (σειριακή, βρόχο, παράλληλη), προτιμάτε σχεδιασμό agent βασισμένο σε interface αντί για επιτακτικό κώδικα, ή συνδυάζετε πολλούς agents που μοιράζονται εξόδους μέσω `outputKey`.

**Το μοτίβο Supervisor Agent** ξεχωρίζει όταν η ροή εργασίας δεν είναι προβλέψιμη εκ των προτέρων και θέλετε το LLM να αποφασίσει, όταν έχετε πολλούς εξειδικευμένους agents που χρειάζονται δυναμική οργάνωση, όταν δημιουργείτε συνομιλιακά συστήματα που δρομολογούν σε διαφορετικές δυνατότητες, ή όταν θέλετε την πιο ευέλικτη, προσαρμοστική συμπεριφορά agent.

Για να σας βοηθήσουμε να επιλέξετε μεταξύ των προσαρμοσμένων μεθόδων `@Tool` από το Module 04 και των εργαλείων MCP αυτού του module, η παρακάτω σύγκριση αναδεικνύει τα βασικά ανταγωνιστικά σημεία — τα προσαρμοσμένα εργαλεία προσφέρουν στενή ενοποίηση και πλήρη τύπο ασφαλείας για app-specific λογική, ενώ τα εργαλεία MCP παρέχουν τυποποιημένες, επαναχρησιμοποιήσιμες ενσωματώσεις:

<img src="../../../translated_images/el/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Πότε να χρησιμοποιήσετε προσαρμοσμένες μεθόδους @Tool έναντι εργαλείων MCP — προσαρμοσμένα εργαλεία για λογική ειδική εφαρμογής με πλήρη τύπο ασφαλείας, εργαλεία MCP για τυποποιημένες ενσωματώσεις που δουλεύουν σε πολλές εφαρμογές.*

## Συγχαρητήρια!

Έχετε ολοκληρώσει και τα πέντε modules του μαθήματος LangChain4j for Beginners! Ακολουθεί μια επισκόπηση της πλήρους διαδρομής μάθησης που ολοκληρώσατε — από το βασικό chat μέχρι τα agentic συστήματα με MCP:

<img src="../../../translated_images/el/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Η διαδρομή μάθησής σας μέσω και των πέντε modules — από το βασικό chat μέχρι τα agentic συστήματα με MCP.*

Έχετε ολοκληρώσει το μάθημα LangChain4j for Beginners. Έχετε μάθει:

- Πώς να φτιάχνετε συνομιλιακή AI με μνήμη (Module 01)  
- Μοτίβα μηχανικής prompt για διαφορετικές εργασίες (Module 02)  
- Εδαφοποίηση απαντήσεων στα έγγραφά σας με RAG (Module 03)  
- Δημιουργία βασικών AI agents (βοηθών) με προσαρμοσμένα εργαλεία (Module 04)  
- Ενσωμάτωση τυποποιημένων εργαλείων με τα LangChain4j MCP και Agentic modules (Module 05)

### Τι Ακολουθεί;

Μετά την ολοκλήρωση των modules, εξερευνήστε τον [Οδηγό Δοκιμών](../docs/TESTING.md) για να δείτε τις έννοιες δοκιμών LangChain4j σε δράση.

**Επίσημοι Πόροι:**  
- [Τεκμηρίωση LangChain4j](https://docs.langchain4j.dev/) - Εκτενείς οδηγοί και αναφορά API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Κώδικας πηγής και παραδείγματα  
- [Σεμινάρια LangChain4j](https://docs.langchain4j.dev/tutorials/) - Οδηγίες βήμα-βήμα για διάφορες χρήσεις

Σας ευχαριστούμε που ολοκληρώσατε αυτό το μάθημα!

---

**Πλοήγηση:** [← Προηγούμενο: Module 04 - Tools](../04-tools/README.md) | [Επιστροφή στην Κύρια Σελίδα](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που επιδιώκουμε ακρίβεια, παρακαλούμε να σημειώσετε ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στην αρχική του γλώσσα πρέπει να θεωρείται η έγκυρη πηγή. Για κρίσιμες πληροφορίες συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν ευθυνόμαστε για οποιεσδήποτε παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->