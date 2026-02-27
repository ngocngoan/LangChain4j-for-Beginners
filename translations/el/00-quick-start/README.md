# Module 00: Γρήγορη Εκκίνηση

## Περιεχόμενα

- [Εισαγωγή](../../../00-quick-start)
- [Τι είναι το LangChain4j;](../../../00-quick-start)
- [Εξαρτήσεις LangChain4j](../../../00-quick-start)
- [Προαπαιτούμενα](../../../00-quick-start)
- [Ρύθμιση](../../../00-quick-start)
  - [1. Λάβετε το GitHub Token σας](../../../00-quick-start)
  - [2. Ορίστε το Token σας](../../../00-quick-start)
- [Εκτέλεση Παραδειγμάτων](../../../00-quick-start)
  - [1. Βασική Συνομιλία](../../../00-quick-start)
  - [2. Πρότυπα Ερωτήσεων](../../../00-quick-start)
  - [3. Κλήση Συναρτήσεων](../../../00-quick-start)
  - [4. Ερωτήσεις-Απαντήσεις Εγγράφων (Easy RAG)](../../../00-quick-start)
  - [5. Υπεύθυνη Τεχνητή Νοημοσύνη](../../../00-quick-start)
- [Τι Δείχνει Κάθε Παράδειγμα](../../../00-quick-start)
- [Επόμενα Βήματα](../../../00-quick-start)
- [Αντιμετώπιση Προβλημάτων](../../../00-quick-start)

## Εισαγωγή

Αυτή η γρήγορη εκκίνηση έχει σκοπό να σας βάλει σε λειτουργία με το LangChain4j όσο το δυνατόν πιο γρήγορα. Καλύπτει τα απόλυτα βασικά για την κατασκευή εφαρμογών AI με το LangChain4j και τα GitHub Models. Στα επόμενα modules θα χρησιμοποιήσετε το Azure OpenAI με το LangChain4j για να δημιουργήσετε πιο προηγμένες εφαρμογές.

## Τι είναι το LangChain4j;

Το LangChain4j είναι μια βιβλιοθήκη Java που απλοποιεί την κατασκευή εφαρμογών με τεχνητή νοημοσύνη. Αντί να ασχολείστε με HTTP clients και parsing JSON, εργάζεστε με καθαρές Java APIs.

Το "chain" στο LangChain αναφέρεται στην αλυσίδωση πολλών συστατικών - μπορεί να συνδέσετε ένα prompt σε ένα μοντέλο, σε έναν parser, ή να αλυσιδώσετε πολλές κλήσεις AI, όπου η έξοδος της μίας τροφοδοτεί την είσοδο της επόμενης. Αυτή η γρήγορη εκκίνηση εστιάζει στα βασικά πριν εξερευνήσουμε πιο πολύπλοκες αλυσίδες.

<img src="../../../translated_images/el/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Σύνδεση συστατικών στο LangChain4j - τα δομικά στοιχεία συνδέονται για να δημιουργήσουν ισχυρές ροές εργασίας AI*

Θα χρησιμοποιήσουμε τρία βασικά συστατικά:

**ChatModel** - Η διεπαφή για αλληλεπιδράσεις με μοντέλα AI. Καλείτε `model.chat("prompt")` και λαμβάνετε μια συμβολοσειρά απάντησης. Χρησιμοποιούμε το `OpenAiOfficialChatModel` που δουλεύει με endpoints συμβατά με OpenAI όπως τα GitHub Models.

**AiServices** - Δημιουργεί τύπου type-safe διεπαφές υπηρεσιών AI. Ορίζετε μεθόδους, τις σχολιάζετε με `@Tool`, και το LangChain4j διαχειρίζεται τον συντονισμό. Η AI καλεί αυτόματα τις Java μεθόδους σας όταν χρειάζεται.

**MessageWindowChatMemory** - Διατηρεί το ιστορικό συνομιλίας. Χωρίς αυτό, κάθε αίτημα είναι ανεξάρτητο. Με αυτό, η AI θυμάται προηγούμενα μηνύματα και διατηρεί το πλαίσιο μέσω πολλαπλών γύρων.

<img src="../../../translated_images/el/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Αρχιτεκτονική LangChain4j - τα βασικά συστατικά συνεργάζονται για να τροφοδοτήσουν τις εφαρμογές AI σας*

## Εξαρτήσεις LangChain4j

Αυτή η γρήγορη εκκίνηση χρησιμοποιεί τρεις εξαρτήσεις Maven στο [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Το module `langchain4j-open-ai-official` παρέχει την κλάση `OpenAiOfficialChatModel` που συνδέεται με APIs συμβατά με OpenAI. Τα GitHub Models χρησιμοποιούν το ίδιο format API, οπότε δεν χρειάζεται ειδικός προσαρμογέας - απλά κατευθύνετε το base URL στο `https://models.github.ai/inference`.

Το module `langchain4j-easy-rag` παρέχει αυτόματο διαχωρισμό εγγράφων, ενσωμάτωση και ανάκτηση ώστε να μπορείτε να δημιουργήσετε RAG εφαρμογές χωρίς χειροκίνητη διαμόρφωση κάθε βήματος.

## Προαπαιτούμενα

**Χρησιμοποιείτε Dev Container;** Το Java και το Maven είναι ήδη εγκατεστημένα. Χρειάζεστε μόνο ένα Προσωπικό Access Token GitHub.

**Τοπική Ανάπτυξη:**
- Java 21+, Maven 3.9+
- Προσωπικό Access Token GitHub (οδηγίες παρακάτω)

> **Σημείωση:** Αυτό το module χρησιμοποιεί το `gpt-4.1-nano` από GitHub Models. Μην τροποποιείτε το όνομα του μοντέλου στον κώδικα - έχει ρυθμιστεί να δουλεύει με τα διαθέσιμα μοντέλα του GitHub.

## Ρύθμιση

### 1. Λάβετε το GitHub Token σας

1. Μεταβείτε στα [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Κάντε κλικ στο "Generate new token"
3. Ορίστε ένα περιγραφικό όνομα (π.χ. "LangChain4j Demo")
4. Ορίστε τη λήξη (7 ημέρες προτείνεται)
5. Στο "Account permissions", βρείτε το "Models" και ορίστε σε "Read-only"
6. Κάντε κλικ στο "Generate token"
7. Αντιγράψτε και αποθηκεύστε το token σας - δεν θα το δείτε ξανά

### 2. Ορίστε το Token σας

**Επιλογή 1: Χρήση VS Code (Συνιστάται)**

Αν χρησιμοποιείτε VS Code, προσθέστε το token σας στο αρχείο `.env` στον ριζικό φάκελο του έργου:

Αν το αρχείο `.env` δεν υπάρχει, αντιγράψτε το `.env.example` σε `.env` ή δημιουργήστε ένα νέο `.env` αρχείο στη ρίζα του έργου.

**Παράδειγμα αρχείου `.env`:**
```bash
# Στο /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Μετά μπορείτε απλά να κάνετε δεξί κλικ σε οποιοδήποτε demo αρχείο (π.χ. `BasicChatDemo.java`) στον Explorer και να επιλέξετε **"Run Java"** ή να χρησιμοποιήσετε τις διαμορφώσεις εκκίνησης από το πάνελ Run and Debug.

**Επιλογή 2: Χρήση Τερματικού**

Ορίστε το token ως μεταβλητή περιβάλλοντος:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Εκτέλεση Παραδειγμάτων

**Με VS Code:** Απλά κάντε δεξί κλικ σε οποιοδήποτε demo αρχείο στον Explorer και επιλέξτε **"Run Java"**, ή χρησιμοποιήστε τις διαμορφώσεις εκκίνησης από το πάνελ Run and Debug (βεβαιωθείτε πρώτα ότι έχετε προσθέσει το token σας στο αρχείο `.env`).

**Με Maven:** Εναλλακτικά, μπορείτε να τρέξετε από τη γραμμή εντολών:

### 1. Βασική Συνομιλία

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Πρότυπα Ερωτήσεων

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Δείχνει zero-shot, few-shot, chain-of-thought, και role-based prompting.

### 3. Κλήση Συναρτήσεων

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Η AI καλεί αυτόματα τις Java μεθόδους σας όταν χρειάζεται.

### 4. Ερωτήσεις-Απαντήσεις Εγγράφων (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Κάντε ερωτήσεις σχετικά με τα έγγραφά σας χρησιμοποιώντας το Easy RAG με αυτόματη ενσωμάτωση και ανάκτηση.

### 5. Υπεύθυνη Τεχνητή Νοημοσύνη

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Δείτε πώς τα φίλτρα ασφάλειας της AI μπλοκάρουν επιβλαβές περιεχόμενο.

## Τι Δείχνει Κάθε Παράδειγμα

**Βασική Συνομιλία** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Ξεκινήστε εδώ για να δείτε το LangChain4j στην πιο απλή του μορφή. Θα δημιουργήσετε ένα `OpenAiOfficialChatModel`, θα στείλετε ένα prompt με `.chat()`, και θα πάρετε απάντηση. Αυτό δείχνει τα θεμέλια: πώς να αρχικοποιείτε μοντέλα με προσαρμοσμένα endpoints και κλειδιά API. Μόλις κατανοήσετε αυτό το πρότυπο, όλα τα υπόλοιπα χτίζονται πάνω σε αυτό.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) και ρωτήστε:
> - "Πώς θα άλλαζα από GitHub Models σε Azure OpenAI σε αυτόν τον κώδικα;"
> - "Ποιες άλλες παραμέτρους μπορώ να ρυθμίσω στο OpenAiOfficialChatModel.builder();"
> - "Πώς προσθέτω streaming responses αντί να περιμένω την πλήρη απάντηση;"

**Μηχανική Προτροπής** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Τώρα που ξέρετε πώς να μιλάτε σε ένα μοντέλο, ας εξερευνήσουμε τι του λέτε. Αυτό το demo χρησιμοποιεί την ίδια διαμόρφωση μοντέλου αλλά δείχνει πέντε διαφορετικά πρότυπα προτροπής. Δοκιμάστε zero-shot prompts για άμεσες οδηγίες, few-shot prompts που μαθαίνουν από παραδείγματα, chain-of-thought prompts που αποκαλύπτουν βήματα συλλογισμού, και role-based prompts που ορίζουν πλαίσιο. Θα δείτε πώς το ίδιο μοντέλο δίνει πολύ διαφορετικά αποτελέσματα ανάλογα με το πώς διατυπώνετε το αίτημά σας.

Το demo επίσης δείχνει πρότυπα προτροπών, που είναι ένας ισχυρός τρόπος να δημιουργείτε επαναχρησιμοποιήσιμες προτροπές με μεταβλητές.
Το παρακάτω παράδειγμα δείχνει ένα prompt που χρησιμοποιεί το LangChain4j `PromptTemplate` για να συμπληρώσει μεταβλητές. Η AI θα απαντήσει βάση του παρεχόμενου προορισμού και της δραστηριότητας.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) και ρωτήστε:
> - "Ποια είναι η διαφορά μεταξύ zero-shot και few-shot prompting, και πότε πρέπει να χρησιμοποιώ το καθένα;"
> - "Πώς επηρεάζει η παράμετρος temperature τις απαντήσεις του μοντέλου;"
> - "Ποιες τεχνικές υπάρχουν για να αποτρέψω επιθέσεις prompt injection στην παραγωγή;"
> - "Πώς μπορώ να δημιουργήσω επαναχρησιμοποιήσιμα αντικείμενα PromptTemplate για κοινά πρότυπα;"

**Ενσωμάτωση Εργαλείων** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Εδώ γίνεται το LangChain4j ισχυρό. Θα χρησιμοποιήσετε το `AiServices` για να δημιουργήσετε έναν βοηθό AI που μπορεί να καλεί τις Java μεθόδους σας. Απλά σχολιάστε τις μεθόδους με `@Tool("περιγραφή")` και το LangChain4j αναλαμβάνει το υπόλοιπο - η AI αποφασίζει αυτόματα πότε να χρησιμοποιεί κάθε εργαλείο βάσει του τι ζητά ο χρήστης. Αυτό δείχνει την κλήση συναρτήσεων, μια βασική τεχνική για τη δημιουργία AI που μπορεί να εκτελεί ενέργειες, όχι μόνο να απαντά σε ερωτήσεις.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) και ρωτήστε:
> - "Πώς λειτουργεί το @Tool annotation και τι κάνει το LangChain4j με αυτό πίσω από τα παρασκήνια;"
> - "Μπορεί η AI να καλέσει πολλά εργαλεία διαδοχικά για να λύσει σύνθετα προβλήματα;"
> - "Τι γίνεται αν ένα εργαλείο πετάξει exception - πώς πρέπει να διαχειριστώ τα σφάλματα;"
> - "Πώς θα ενσωματώσω ένα πραγματικό API αντί για αυτό το παράδειγμα υπολογιστή;"

**Ερωτήσεις-Απαντήσεις Εγγράφων (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Εδώ θα δείτε το RAG (retrieval-augmented generation) χρησιμοποιώντας την προσέγγιση "Easy RAG" του LangChain4j. Τα έγγραφα φορτώνονται, διαχωρίζονται και ενσωματώνονται αυτόματα σε μια in-memory αποθήκη, στη συνέχεια ένας ανακτήστης περιεχομένου παρέχει σχετικά κομμάτια στην AI κατά το χρόνο ερώτησης. Η AI απαντά βασισμένη στα έγγραφά σας, όχι στη γενική γνώση της.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) και ρωτήστε:
> - "Πώς το RAG αποτρέπει τις παραισθήσεις της AI σε σύγκριση με τη χρήση των δεδομένων εκπαίδευσης του μοντέλου;"
> - "Ποια η διαφορά μεταξύ αυτής της εύκολης προσέγγισης και μιας εξατομικευμένης RAG pipeline;"
> - "Πώς θα κλιμακώσω αυτό για να χειριστώ πολλαπλά έγγραφα ή μεγαλύτερες βάσεις γνώσης;"

**Υπεύθυνη Τεχνητή Νοημοσύνη** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Δημιουργήστε ασφάλεια AI με άμυνα σε βάθος. Αυτό το demo δείχνει δύο επίπεδα προστασίας που λειτουργούν μαζί:

**Μέρος 1: LangChain4j Input Guardrails** - Μπλοκάρουν επικίνδυνες προτροπές πριν φτάσουν στο LLM. Δημιουργήστε προσαρμοσμένους κανόνες που ελέγχουν για απαγορευμένες λέξεις-κλειδιά ή μοτίβα. Αυτά τρέχουν στον κώδικά σας, είναι γρήγορα και δωρεάν.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Μέρος 2: Φίλτρα Ασφαλείας Παρόχου** - Τα GitHub Models έχουν ενσωματωμένα φίλτρα που πιάνουν ό,τι μπορεί να χάσουν οι guardrails σας. Θα δείτε σκληρά μπλοκαρίσματα (HTTP 400 errors) για σοβαρές παραβάσεις και ήπια απορρίψεις όπου η AI αρνείται ευγενικά.

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) και ρωτήστε:
> - "Τι είναι το InputGuardrail και πώς δημιουργώ το δικό μου;"
> - "Ποια η διαφορά μεταξύ σκληρού μπλοκαρίσματος και ήπιας άρνησης;"
> - "Γιατί να χρησιμοποιώ και guardrails και φίλτρα παρόχου μαζί;"

## Επόμενα Βήματα

**Επόμενο Module:** [01-introduction - Ξεκινώντας με LangChain4j και gpt-5 στο Azure](../01-introduction/README.md)

---

**Πλοήγηση:** [← Πίσω στην Κεντρική Έννοια](../README.md) | [Επόμενο: Module 01 - Εισαγωγή →](../01-introduction/README.md)

---

## Αντιμετώπιση Προβλημάτων

### Πρώτη Φορα Maven Build

**Πρόβλημα:** Η αρχική `mvn clean compile` ή `mvn package` παίρνει πολύ χρόνο (10-15 λεπτά)

**Αιτία:** Το Maven πρέπει να κατεβάσει όλες τις εξαρτήσεις του έργου (Spring Boot, βιβλιοθήκες LangChain4j, SDKs Azure, κτλ) στην πρώτη κατασκευή.

**Λύση:** Αυτό είναι φυσιολογικό. Οι επόμενες κατασκευές θα είναι πολύ πιο γρήγορες καθώς οι εξαρτήσεις αποθηκεύονται τοπικά. Ο χρόνος λήψης εξαρτάται από την ταχύτητα του δικτύου σας.

### Σύνταξη Εντολής Maven σε PowerShell

**Πρόβλημα:** Οι εντολές Maven αποτυγχάνουν με σφάλμα `Unknown lifecycle phase ".mainClass=..."`
**Αιτία**: Το PowerShell ερμηνεύει το `=` ως τελεστή ανάθεσης μεταβλητής, διακόπτοντας τη σύνταξη ιδιοτήτων του Maven

**Λύση**: Χρησιμοποιήστε τον τελεστή διακοπής ανάλυσης `--%` πριν από την εντολή Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Ο τελεστής `--%` λέει στο PowerShell να περάσει όλα τα υπόλοιπα επιχειρήματα κυριολεκτικά στο Maven χωρίς ερμηνεία.

### Εμφάνιση Emoji στα Windows PowerShell

**Πρόβλημα**: Οι απαντήσεις AI εμφανίζουν άχρηστους χαρακτήρες (π.χ., `????` ή `â??`) αντί για emoji στο PowerShell

**Αιτία**: Η προεπιλεγμένη κωδικοποίηση του PowerShell δεν υποστηρίζει emoji UTF-8

**Λύση**: Εκτελέστε αυτή την εντολή πριν τρέξετε εφαρμογές Java:
```cmd
chcp 65001
```

Αυτό αναγκάζει την κωδικοποίηση UTF-8 στο τερματικό. Εναλλακτικά, χρησιμοποιήστε το Windows Terminal που έχει καλύτερη υποστήριξη Unicode.

### Εντοπισμός Σφαλμάτων Κλήσεων API

**Πρόβλημα**: Σφάλματα αυθεντικοποίησης, όρια ρυθμού, ή απρόσμενες απαντήσεις από το μοντέλο AI

**Λύση**: Τα παραδείγματα περιλαμβάνουν `.logRequests(true)` και `.logResponses(true)` για να εμφανίζουν τις κλήσεις API στην κονσόλα. Αυτό βοηθά στον εντοπισμό σφαλμάτων αυθεντικοποίησης, ορίων ρυθμού, ή απρόσμενων απαντήσεων. Αφαιρέστε αυτές τις σημαίες στην παραγωγή για να μειώσετε τον θόρυβο των logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθύνης**:
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία μηχανικής μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που προσπαθούμε για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->