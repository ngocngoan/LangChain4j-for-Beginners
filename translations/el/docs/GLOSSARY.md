# Γλωσσάρι LangChain4j

## Περιεχόμενα

- [Βασικές Έννοιες](../../../docs)
- [Συστατικά LangChain4j](../../../docs)
- [Έννοιες AI/ML](../../../docs)
- [Ρυθμίσεις Προστασίας (Guardrails)](../../../docs)
- [Σχεδιασμός Prompt](../../../docs)
- [RAG (Δημιουργία με Ενισχυμένη Ανάκτηση)](../../../docs)
- [Agents και Εργαλεία](../../../docs)
- [Agentic Module](../../../docs)
- [Πρωτόκολλο Περιβάλλοντος Μοντέλου (MCP)](../../../docs)
- [Υπηρεσίες Azure](../../../docs)
- [Δοκιμές και Ανάπτυξη](../../../docs)

Γρήγορη αναφορά για όρους και έννοιες που χρησιμοποιούνται σε όλο το μάθημα.

## Βασικές Έννοιες

**AI Agent** - Σύστημα που χρησιμοποιεί AI για να λογικεύεται και να ενεργεί αυτόνομα. [Module 04](../04-tools/README.md)

**Chain** - Αλληλουχία λειτουργιών όπου η έξοδος τροφοδοτεί το επόμενο βήμα.

**Chunking** - Διάσπαση εγγράφων σε μικρότερα κομμάτια. Τυπικά: 300-500 tokens με επικάλυψη. [Module 03](../03-rag/README.md)

**Context Window** - Μέγιστος αριθμός tokens που μπορεί να επεξεργαστεί ένα μοντέλο. GPT-5.2: 400K tokens.

**Embeddings** - Αριθμητικοί διανύσματα που αντιπροσωπεύουν το νόημα κειμένου. [Module 03](../03-rag/README.md)

**Function Calling** - Το μοντέλο δημιουργεί δομημένα αιτήματα για κλήση εξωτερικών συναρτήσεων. [Module 04](../04-tools/README.md)

**Hallucination** - Όταν τα μοντέλα παράγουν ανακριβείς αλλά εύλογες πληροφορίες.

**Prompt** - Κείμενο εισόδου σε γλωσσικό μοντέλο. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Αναζήτηση βάσει νοήματος χρησιμοποιώντας embeddings, όχι λέξεις-κλειδιά. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: χωρίς μνήμη. Stateful: διατηρεί ιστορικό συνομιλίας. [Module 01](../01-introduction/README.md)

**Tokens** - Βασικές μονάδες κειμένου που επεξεργάζονται τα μοντέλα. Επηρεάζουν κόστη και όρια. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Ακολουθιακή εκτέλεση εργαλείων όπου η έξοδος ενημερώνει την επόμενη κλήση. [Module 04](../04-tools/README.md)

## Συστατικά LangChain4j

**AiServices** - Δημιουργεί τύπους ασφαλείς διεπαφές υπηρεσιών AI.

**OpenAiOfficialChatModel** - Ενιαίος πελάτης για μοντέλα OpenAI και Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Δημιουργεί embeddings χρησιμοποιώντας τον επίσημο πελάτη OpenAI (υποστηρίζει OpenAI και Azure OpenAI).

**ChatModel** - Βασική διεπαφή για γλωσσικά μοντέλα.

**ChatMemory** - Διατηρεί το ιστορικό συνομιλίας.

**ContentRetriever** - Βρίσκει σχετικά κομμάτια εγγράφων για RAG.

**DocumentSplitter** - Χωρίζει έγγραφα σε κομμάτια.

**EmbeddingModel** - Μετατρέπει κείμενο σε αριθμητικά διανύσματα.

**EmbeddingStore** - Αποθηκεύει και ανακτά embeddings.

**MessageWindowChatMemory** - Διατηρεί παράθυρο πρόσφατων μηνυμάτων.

**PromptTemplate** - Δημιουργεί επαναχρησιμοποιήσιμα prompts με placeholders `{{variable}}`.

**TextSegment** - Κομμάτι κειμένου με μεταδεδομένα. Χρησιμοποιείται στο RAG.

**ToolExecutionRequest** - Αντιπροσωπεύει αίτημα εκτέλεσης εργαλείου.

**UserMessage / AiMessage / SystemMessage** - Τύποι μηνυμάτων συνομιλίας.

## Έννοιες AI/ML

**Few-Shot Learning** - Παροχή παραδειγμάτων στα prompts. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Μοντέλα AI εκπαιδευμένα σε τεράστιους όγκους κειμένου.

**Reasoning Effort** - Παράμετρος GPT-5.2 που ελέγχει το βάθος σκέψης. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - Ελέγχει την τυχαιότητα της εξόδου. Χαμηλό=ντετερμινιστικό, υψηλό=δημιουργικό.

**Vector Database** - Εξειδικευμένη βάση δεδομένων για embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - Εκτέλεση εργασιών χωρίς παραδείγματα. [Module 02](../02-prompt-engineering/README.md)

## Ρυθμίσεις Προστασίας (Guardrails) - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Πολυεπίπεδη προσέγγιση ασφάλειας που συνδυάζει ρυθμίσεις σε επίπεδο εφαρμογής με φίλτρα παρόχου.

**Hard Block** - Ο πάροχος εκτοξεύει σφάλμα HTTP 400 για σοβαρές παραβάσεις περιεχομένου.

**InputGuardrail** - Διεπαφή LangChain4j για επικύρωση εισόδου χρήστη πριν αυτή φτάσει στο LLM. Εξοικονομεί κόστος και καθυστέρηση μπλοκάροντας επιβλαβή prompts νωρίς.

**InputGuardrailResult** - Τύπος επιστροφής επικύρωσης guardrail: `success()` ή `fatal("reason")`.

**OutputGuardrail** - Διεπαφή για επικύρωση απαντήσεων AI πριν επιστραφούν στους χρήστες.

**Provider Safety Filters** - Ενσωματωμένα φίλτρα περιεχομένου από παρόχους AI (π.χ. GitHub Models) που ανιχνεύουν παραβάσεις σε επίπεδο API.

**Soft Refusal** - Το μοντέλο ευγενικά αρνείται να απαντήσει χωρίς να πετάξει σφάλμα.

## Σχεδιασμός Prompt - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Βήμα-βήμα λογική για καλύτερη ακρίβεια.

**Constrained Output** - Επιβολή συγκεκριμένης μορφής ή δομής.

**High Eagerness** - Πρότυπο GPT-5.2 για λεπτομερή λογική.

**Low Eagerness** - Πρότυπο GPT-5.2 για γρήγορες απαντήσεις.

**Multi-Turn Conversation** - Διατήρηση συμφραζομένων σε ανταλλαγές.

**Role-Based Prompting** - Ορισμός προσωπείου μοντέλου μέσω μηνυμάτων συστήματος.

**Self-Reflection** - Το μοντέλο αξιολογεί και βελτιώνει την έξοδό του.

**Structured Analysis** - Σταθερό πλαίσιο αξιολόγησης.

**Task Execution Pattern** - Σχεδιάζω → Εκτελώ → Περιοδική αναφορά.

## RAG (Δημιουργία με Ενισχυμένη Ανάκτηση) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Φορτώνω → χωρίζω → ενσωματώνω → αποθηκεύω.

**In-Memory Embedding Store** - Μη επίμονη αποθήκευση για δοκιμές.

**RAG** - Συνδυάζει ανάκτηση με δημιουργία για εδραίωση απαντήσεων.

**Similarity Score** - Μέτρο (0-1) σημασιολογικής ομοιότητας.

**Source Reference** - Μεταδεδομένα για το ανακτηθέν περιεχόμενο.

## Agents και Εργαλεία - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Σημειώνει μεθόδους Java ως εργαλεία AI καλούμενα.

**ReAct Pattern** - Λογική → Δράση → Παρατήρηση → Επανάληψη.

**Session Management** - Ξεχωριστά πλαίσια για διαφορετικούς χρήστες.

**Tool** - Συνάρτηση που μπορεί να καλέσει ένας AI agent.

**Tool Description** - Τεκμηρίωση σκοπού και παραμέτρων εργαλείου.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Σημειώνει διεπαφές ως AI agents με δηλωτικό ορισμό συμπεριφοράς.

**Agent Listener** - Σύνδεσμος για παρακολούθηση εκτέλεσης agent μέσω `beforeAgentInvocation()` και `afterAgentInvocation()`.

**Agentic Scope** - Κοινή μνήμη όπου οι agents αποθηκεύουν αποτελέσματα χρησιμοποιώντας `outputKey` για κατανάλωση από άλλους agents.

**AgenticServices** - Κατασκευαστής για δημιουργία agents μέσω `agentBuilder()` και `supervisorBuilder()`.

**Conditional Workflow** - Διαδρομή βάσει συνθηκών προς διαφορετικούς εξειδικευμένους agents.

**Human-in-the-Loop** - Πρότυπο ροής εργασίας που προσθέτει ανθρώπινους ελέγχους για έγκριση ή ανασκόπηση περιεχομένου.

**langchain4j-agentic** - Εξάρτηση Maven για δηλωτική κατασκευή agents (πειραματικό).

**Loop Workflow** - Επανάληψη εκτέλεσης agent έως ότου πληρούται συνθήκη (π.χ. βαθμολογία ποιότητας ≥ 0.8).

**outputKey** - Παράμετρος annotation agent που καθορίζει πού αποθηκεύονται τα αποτελέσματα στο Agentic Scope.

**Parallel Workflow** - Εκτέλεση πολλαπλών agents ταυτόχρονα για ανεξάρτητες εργασίες.

**Response Strategy** - Πώς ο supervisor διατυπώνει την τελική απάντηση: LAST, SUMMARY, ή SCORED.

**Sequential Workflow** - Εκτέλεση agents με σειρά όπου η έξοδος ρέει στο επόμενο βήμα.

**Supervisor Agent Pattern** - Προηγμένο agentic πρότυπο όπου ένας supervisor LLM αποφασίζει δυναμικά ποιους υποagents να καλέσει.

## Πρωτόκολλο Περιβάλλοντος Μοντέλου (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Εξάρτηση Maven για ενσωμάτωση MCP στο LangChain4j.

**MCP** - Model Context Protocol: πρότυπο για σύνδεση εφαρμογών AI με εξωτερικά εργαλεία. Φτιάχνεις μια φορά, χρησιμοποιείς παντού.

**MCP Client** - Εφαρμογή που συνδέεται με MCP servers για ανακάλυψη και χρήση εργαλείων.

**MCP Server** - Υπηρεσία που εκθέτει εργαλεία μέσω MCP με σαφείς περιγραφές και σχήματα παραμέτρων.

**McpToolProvider** - Συστατικό LangChain4j που τυλίγει εργαλεία MCP για χρήση σε υπηρεσίες AI και agents.

**McpTransport** - Διεπαφή για επικοινωνία MCP. Υλοποιήσεις περιλαμβάνουν Stdio και HTTP.

**Stdio Transport** - Τοπική μεταφορά διαδικασίας μέσω stdin/stdout. Χρήσιμο για πρόσβαση σε σύστημα αρχείων ή εργαλεία γραμμής εντολών.

**StdioMcpTransport** - Υλοποίηση LangChain4j που εκκινεί MCP server ως υποδιαδικασία.

**Tool Discovery** - Ο πελάτης ρωτά τον server για διαθέσιμα εργαλεία με περιγραφές και σχήματα.

## Υπηρεσίες Azure - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Υπηρεσία αναζήτησης στο cloud με δυνατότητες διανυσμάτων. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Αναπτύσσει πόρους Azure.

**Azure OpenAI** - Επιχειρησιακή υπηρεσία AI της Microsoft.

**Bicep** - Γλώσσα υποδομής ως κώδικα για Azure. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Όνομα για ανάπτυξη μοντέλου στο Azure.

**GPT-5.2** - Νεότερο μοντέλο OpenAI με έλεγχο σκέψης. [Module 02](../02-prompt-engineering/README.md)

## Δοκιμές και Ανάπτυξη - [Οδηγός Δοκιμών](TESTING.md)

**Dev Container** - Περιβάλλον ανάπτυξης σε κοντέινερ. [Διαμόρφωση](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Δωρεάν πλατφόρμα μοντέλων AI. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Δοκιμές με αποθήκευση στη μνήμη.

**Integration Testing** - Δοκιμές με πραγματική υποδομή.

**Maven** - Εργαλείο αυτοματοποίησης κατασκευής Java.

**Mockito** - Πλαίσιο ψευδοποίησης Java.

**Spring Boot** - Πλαίσιο εφαρμογών Java. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που επιδιώκουμε την ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτοματοποιημένες μεταφράσεις μπορεί να περιέχουν λάθη ή ανακρίβειες. Το αρχικό έγγραφο στη γλώσσα του θεωρείται η καθοριστική πηγή. Για κρίσιμες πληροφορίες, συνιστάται η επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για οποιεσδήποτε παρεξηγήσεις ή λανθασμένες ερμηνείες προκύψουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->