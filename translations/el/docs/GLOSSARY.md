# Ορολογικός Λεξιλόγος LangChain4j

## Περιεχόμενα

- [Βασικές Έννοιες](../../../docs)
- [Συστατικά LangChain4j](../../../docs)
- [Έννοιες AI/ML](../../../docs)
- [Φράγματα Ασφαλείας](../../../docs)
- [Μηχανική Προτροπής](../../../docs)
- [RAG (Ανάκτηση-Ενισχυμένη Γενιά)](../../../docs)
- [Πράκτορες και Εργαλεία](../../../docs)
- [Agentic Module](../../../docs)
- [Πρωτόκολλο Πλαισίου Μοντέλου (MCP)](../../../docs)
- [Υπηρεσίες Azure](../../../docs)
- [Δοκιμές και Ανάπτυξη](../../../docs)

Γρήγορη αναφορά για όρους και έννοιες που χρησιμοποιούνται σε όλο το μάθημα.

## Βασικές Έννοιες

**AI Agent** - Σύστημα που χρησιμοποιεί AI για να συλλογιστεί και να ενεργεί αυτόνομα. [Module 04](../04-tools/README.md)

**Chain** - Ακολουθία λειτουργιών όπου η έξοδος τροφοδοτεί το επόμενο βήμα.

**Chunking** - Διαχωρισμός εγγράφων σε μικρότερα κομμάτια. Τυπικά: 300-500 tokens με επικάλυψη. [Module 03](../03-rag/README.md)

**Context Window** - Μέγιστος αριθμός tokens που μπορεί να επεξεργαστεί ένα μοντέλο. GPT-5.2: 400K tokens (έως 272K εισόδου, 128K εξόδου).

**Embeddings** - Αριθμητικοί διανύσματα που αντιπροσωπεύουν το νόημα του κειμένου. [Module 03](../03-rag/README.md)

**Function Calling** - Το μοντέλο δημιουργεί δομημένα αιτήματα για κλήση εξωτερικών λειτουργιών. [Module 04](../04-tools/README.md)

**Hallucination** - Όταν τα μοντέλα παράγουν λανθασμένες αλλά πιθανές πληροφορίες.

**Prompt** - Είσοδος κειμένου σε γλωσσικό μοντέλο. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Αναζήτηση με βάση το νόημα χρησιμοποιώντας embeddings, όχι λέξεις-κλειδιά. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: χωρίς μνήμη. Stateful: διατηρεί το ιστορικό συνομιλίας. [Module 01](../01-introduction/README.md)

**Tokens** - Βασικές μονάδες κειμένου που επεξεργάζονται τα μοντέλα. Επηρεάζουν κόστη και περιορισμούς. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Διαδοχική εκτέλεση εργαλείων όπου η έξοδος πληροφορεί την επόμενη κλήση. [Module 04](../04-tools/README.md)

## Συστατικά LangChain4j

**AiServices** - Δημιουργεί τύπου ασφαλή interfaces για AI υπηρεσίες.

**OpenAiOfficialChatModel** - Ενοποιημένος πελάτης για μοντέλα OpenAI και Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Δημιουργεί embeddings χρησιμοποιώντας τον επίσημο πελάτη OpenAI (υποστηρίζει και OpenAI και Azure OpenAI).

**ChatModel** - Βασικό interface για γλωσσικά μοντέλα.

**ChatMemory** - Διατηρεί το ιστορικό συνομιλίας.

**ContentRetriever** - Βρίσκει σχετικά κομμάτια εγγράφων για RAG.

**DocumentSplitter** - Διαχωρίζει έγγραφα σε κομμάτια.

**EmbeddingModel** - Μετατρέπει κείμενο σε αριθμητικά διανύσματα.

**EmbeddingStore** - Αποθηκεύει και ανακτά embeddings.

**MessageWindowChatMemory** - Διατηρεί παράθυρο ολίσθησης με πρόσφατα μηνύματα.

**PromptTemplate** - Δημιουργεί επαναχρησιμοποιήσιμες προτροπές με θέσεις `{{variable}}`.

**TextSegment** - Κομμάτι κειμένου με μεταδεδομένα. Χρησιμοποιείται σε RAG.

**ToolExecutionRequest** - Αντιπροσωπεύει αίτημα εκτέλεσης εργαλείου.

**UserMessage / AiMessage / SystemMessage** - Τύποι μηνυμάτων συνομιλίας.

## Έννοιες AI/ML

**Few-Shot Learning** - Παροχή παραδειγμάτων στις προτροπές. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Μοντέλα AI εκπαιδευμένα σε τεράστιες ποσότητες κειμένου.

**Reasoning Effort** - Παράμετρος GPT-5.2 που ελέγχει το βάθος της σκέψης. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Ελέγχει την τυχαιότητα στην έξοδο. Χαμηλό=ντετερμινιστικό, υψηλό=δημιουργικό.

**Vector Database** - Εξειδικευμένη βάση δεδομένων για embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Εκτέλεση εργασιών χωρίς παραδείγματα. [Module 02](../02-prompt-engineering/README.md)

## Φράγματα Ασφαλείας - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Πολλαπλό επίπεδο ασφαλείας που συνδυάζει φράγματα στο επίπεδο εφαρμογής με φίλτρα ασφαλείας παρόχου.

**Hard Block** - Ο πάροχος επιστρέφει σφάλμα HTTP 400 για σοβαρές παραβιάσεις περιεχομένου.

**InputGuardrail** - Interface LangChain4j για επικύρωση εισόδου χρήστη πριν φτάσει στο LLM. Εξοικονομεί κόστος και καθυστέρηση μπλοκάροντας επιβλαβείς προτροπές νωρίς.

**InputGuardrailResult** - Τύπος επιστροφής για επικύρωση φράγματος: `success()` ή `fatal("reason")`.

**OutputGuardrail** - Interface για επικύρωση απαντήσεων AI πριν επιστραφούν στους χρήστες.

**Provider Safety Filters** - Ενσωματωμένα φίλτρα περιεχομένου από παρόχους AI (π.χ. GitHub Models) που ανιχνεύουν παραβιάσεις στο επίπεδο API.

**Soft Refusal** - Το μοντέλο αρνείται κόσμια να απαντήσει χωρίς να επιστρέψει σφάλμα.

## Μηχανική Προτροπής - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Σταδιακή επιχειρηματολογία για καλύτερη ακρίβεια.

**Constrained Output** - Επιβολή συγκεκριμένης μορφής ή δομής.

**High Eagerness** - Πρότυπο GPT-5.2 για σχολαστική συλλογιστική.

**Low Eagerness** - Πρότυπο GPT-5.2 για γρήγορες απαντήσεις.

**Multi-Turn Conversation** - Διατήρηση πλαισίου σε πολλαπλές ανταλλαγές.

**Role-Based Prompting** - Ορισμός χαρακτήρα μοντέλου μέσω συστημικών μηνυμάτων.

**Self-Reflection** - Το μοντέλο αξιολογεί και βελτιώνει την έξοδο του.

**Structured Analysis** - Σταθερό πλαίσιο αξιολόγησης.

**Task Execution Pattern** - Σχεδίαση → Εκτέλεση → Περίληψη.

## RAG (Ανάκτηση-Ενισχυμένη Γενιά) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Φόρτωση → διαχωρισμός → ενσωμάτωση → αποθήκευση.

**In-Memory Embedding Store** - Μη επίμονος αποθηκευτικός χώρος για δοκιμές.

**RAG** - Συνδυάζει ανάκτηση με παραγωγή για εδραίωση απαντήσεων.

**Similarity Score** - Μέτρο (0-1) σημασιολογικής ομοιότητας.

**Source Reference** - Μεταδεδομένα για ανακτημένο περιεχόμενο.

## Πράκτορες και Εργαλεία - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Σημειώνει μεθόδους Java ως εργαλεία προσβάσιμα από AI.

**ReAct Pattern** - Συλλογισμός → Δράση → Παρατήρηση → Επανάληψη.

**Session Management** - Ξεχωριστά πλαίσια για διαφορετικούς χρήστες.

**Tool** - Συνάρτηση που μπορεί να καλέσει ένας AI πράκτορας.

**Tool Description** - Τεκμηρίωση σκοπού και παραμέτρων εργαλείου.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Σημειώνει interfaces ως AI πράκτορες με δηλωτικό ορισμό συμπεριφοράς.

**Agent Listener** - Hook για παρακολούθηση εκτέλεσης πράκτορα μέσω `beforeAgentInvocation()` και `afterAgentInvocation()`.

**Agentic Scope** - Κοινή μνήμη όπου οι πράκτορες αποθηκεύουν εξόδους χρησιμοποιώντας `outputKey` για κατανάλωση από κατωτέρους πράκτορες.

**AgenticServices** - Εργοστάσιο δημιουργίας πρακτόρων με `agentBuilder()` και `supervisorBuilder()`.

**Conditional Workflow** - Διαδρομή βάσει συνθηκών προς διαφορετικούς ειδικούς πράκτορες.

**Human-in-the-Loop** - Μοτίβο ροής εργασίας με ανθρώπινους ελέγχους για έγκριση ή ανασκόπηση περιεχομένου.

**langchain4j-agentic** - Εξάρτηση Maven για δηλωτική κατασκευή πρακτόρων (πειραματικό).

**Loop Workflow** - Επανάληψη εκτέλεσης πράκτορα μέχρι να πληρωθεί συνθήκη (π.χ. βαθμολογία ποιότητας ≥ 0.8).

**outputKey** - Παράμετρος annotation πράκτορα που καθορίζει πού αποθηκεύονται τα αποτελέσματα στο Agentic Scope.

**Parallel Workflow** - Εκτέλεση πολλαπλών πρακτόρων ταυτόχρονα για ανεξάρτητες εργασίες.

**Response Strategy** - Πώς ο επιβλέπων διαμορφώνει την τελική απάντηση: LAST, SUMMARY ή SCORED.

**Sequential Workflow** - Εκτέλεση πρακτόρων με σειρά όπου η έξοδος διοχετεύεται στο επόμενο βήμα.

**Supervisor Agent Pattern** - Προχωρημένο μοτίβο agentic όπου ένας επιβλέπων LLM αποφασίζει δυναμικά ποιον υποπράκτορα να καλέσει.

## Πρωτόκολλο Πλαισίου Μοντέλου (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Εξάρτηση Maven για ενσωμάτωση MCP στο LangChain4j.

**MCP** - Πρωτόκολλο Πλαισίου Μοντέλου: πρότυπο για σύνδεση εφαρμογών AI με εξωτερικά εργαλεία. Φτιάξτε μια φορά, χρησιμοποιήστε παντού.

**MCP Client** - Εφαρμογή που συνδέεται με MCP servers για ανακάλυψη και χρήση εργαλείων.

**MCP Server** - Υπηρεσία που εκθέτει εργαλεία μέσω MCP με σαφείς περιγραφές και σχήματα παραμέτρων.

**McpToolProvider** - Συστατικό LangChain4j που περιτυλίγει εργαλεία MCP για χρήση σε AI υπηρεσίες και πράκτορες.

**McpTransport** - Interface για επικοινωνία MCP. Υλοποιήσεις περιλαμβάνουν Stdio και HTTP.

**Stdio Transport** - Τοπική μεταφορά διεργασίας μέσω stdin/stdout. Χρήσιμο για πρόσβαση σε σύστημα αρχείων ή εργαλεία γραμμής εντολών.

**StdioMcpTransport** - Υλοποίηση LangChain4j που δημιουργεί MCP server ως subprocess.

**Tool Discovery** - Ο πελάτης ρωτά τον server για διαθέσιμα εργαλεία με περιγραφές και σχήματα.

## Υπηρεσίες Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Νέφος αναζήτησης με δυνατότητες vector. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Αναπτύσσει πόρους Azure.

**Azure OpenAI** - Επιχειρησιακή υπηρεσία AI της Microsoft.

**Bicep** - Γλώσσα υποδομής ως κώδικας στο Azure. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Όνομα ανάπτυξης μοντέλου στο Azure.

**GPT-5.2** - Τελευταίο μοντέλο OpenAI με έλεγχο συλλογισμού. [Module 02](../02-prompt-engineering/README.md)

## Δοκιμές και Ανάπτυξη - [Testing Guide](TESTING.md)

**Dev Container** - Περιβάλλον ανάπτυξης σε κοντέινερ. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Δωρεάν πλατφόρμα μοντέλων AI. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Δοκιμές με αποθήκευση στην μνήμη.

**Integration Testing** - Δοκιμές με πραγματική υποδομή.

**Maven** - Εργαλείο αυτοματοποίησης κατασκευής Java.

**Mockito** - Πλαίσιο για mock αντικείμενα σε Java.

**Spring Boot** - Πλαίσιο εφαρμογών Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί με τη χρήση της υπηρεσίας αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που προσπαθούμε για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες συνιστάται επαγγελματική μετάφραση από ανθρώπους. Δεν φέρουμε ευθύνη για τυχόν παρανοήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->