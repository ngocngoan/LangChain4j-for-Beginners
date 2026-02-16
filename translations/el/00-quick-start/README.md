# Ενότητα 00: Γρήγορη Έναρξη

## Περιεχόμενα

- [Εισαγωγή](../../../00-quick-start)
- [Τι είναι το LangChain4j;](../../../00-quick-start)
- [Εξαρτήσεις LangChain4j](../../../00-quick-start)
- [Απαιτήσεις](../../../00-quick-start)
- [Εγκατάσταση](../../../00-quick-start)
  - [1. Λάβε το GitHub Token σου](../../../00-quick-start)
  - [2. Ορισμός του Token σου](../../../00-quick-start)
- [Εκτέλεση των Παραδειγμάτων](../../../00-quick-start)
  - [1. Βασική Συνομιλία](../../../00-quick-start)
  - [2. Πρότυπα Prompt](../../../00-quick-start)
  - [3. Κλήση Συναρτήσεων](../../../00-quick-start)
  - [4. Ερωτήσεις & Απαντήσεις Εγγράφων (RAG)](../../../00-quick-start)
  - [5. Υπεύθυνη Τεχνητή Νοημοσύνη](../../../00-quick-start)
- [Τι Δείχνει Κάθε Παράδειγμα](../../../00-quick-start)
- [Επόμενα Βήματα](../../../00-quick-start)
- [Αντιμετώπιση Προβλημάτων](../../../00-quick-start)

## Εισαγωγή

Αυτή η γρήγορη εκκίνηση στοχεύει να σε βοηθήσει να ξεκινήσεις με το LangChain4j όσο το δυνατόν γρηγορότερα. Καλύπτει τα απόλυτα βασικά για την κατασκευή εφαρμογών ΤΝ με LangChain4j και GitHub Models. Στις επόμενες ενότητες θα χρησιμοποιήσεις το Azure OpenAI με το LangChain4j για να δημιουργήσεις πιο σύνθετες εφαρμογές.

## Τι είναι το LangChain4j;

Το LangChain4j είναι μια βιβλιοθήκη Java που απλοποιεί την κατασκευή εφαρμογών με τεχνητή νοημοσύνη. Αντί να ασχολείσαι με HTTP clients και parsing JSON, δουλεύεις με καθαρές Java API.

Το «αλυσίδα» στο LangChain αναφέρεται στην αλληλοσύνδεση πολλαπλών στοιχείων - μπορείς να συνδέσεις ένα prompt με ένα μοντέλο, ένα parser ή να συνδέσεις πολλαπλές κλήσεις AI όπου η έξοδος της μίας τροφοδοτεί την επόμενη είσοδο. Αυτή η γρήγορη εκκίνηση εστιάζει στα θεμελιώδη πριν εξερευνήσει πιο πολύπλοκες αλυσίδες.

<img src="../../../translated_images/el/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Αλυσίδωση στοιχείων στο LangChain4j - τα δομικά στοιχεία συνδέονται για να δημιουργήσουν ισχυρά ροές εργασίας AI*

Θα χρησιμοποιήσουμε τρία βασικά στοιχεία:

**ChatLanguageModel** - Η διεπαφή για αλληλεπίδραση με το μοντέλο AI. Καλείς `model.chat("prompt")` και λαμβάνεις μια αλφαριθμητική απάντηση. Χρησιμοποιούμε το `OpenAiOfficialChatModel` που δουλεύει με endpoints συμβατά με OpenAI, όπως τα GitHub Models.

**AiServices** - Δημιουργεί τύπου-safe διεπαφές υπηρεσιών AI. Ορίζεις μεθόδους, τις επισημαίνεις με `@Tool`, και το LangChain4j διαχειρίζεται την ορχήστρωση. Το AI καλεί αυτόματα τις Java μεθόδους σου όταν χρειάζεται.

**MessageWindowChatMemory** - Διατηρεί το ιστορικό συνομιλίας. Χωρίς αυτό, κάθε αίτημα είναι ανεξάρτητο. Με αυτό, το AI θυμάται προηγούμενα μηνύματα και διατηρεί το πλαίσιο διαλόγου μέσα σε πολλαπλές στροφές.

<img src="../../../translated_images/el/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Αρχιτεκτονική LangChain4j - βασικά στοιχεία που συνεργάζονται για να τροφοδοτήσουν τις εφαρμογές AI σου*

## Εξαρτήσεις LangChain4j

Αυτή η γρήγορη εκκίνηση χρησιμοποιεί δύο εξαρτήσεις Maven στο [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Το module `langchain4j-open-ai-official` παρέχει την κλάση `OpenAiOfficialChatModel` που συνδέεται με APIs συμβατά με OpenAI. Τα GitHub Models χρησιμοποιούν την ίδια μορφή API, οπότε δεν χρειάζεται ειδικός προσαρμογέας - αρκεί να δώσεις ως βάση τη διεύθυνση URL `https://models.github.ai/inference`.

## Απαιτήσεις

**Χρησιμοποιείς Dev Container;** Το Java και το Maven είναι ήδη εγκατεστημένα. Χρειάζεσαι μόνο ένα Personal Access Token από το GitHub.

**Τοπική Ανάπτυξη:**
- Java 21+, Maven 3.9+
- Personal Access Token GitHub (οδηγίες παρακάτω)

> **Σημείωση:** Αυτή η ενότητα χρησιμοποιεί το `gpt-4.1-nano` από το GitHub Models. Μην τροποποιήσεις το όνομα του μοντέλου στο κώδικα - έχει ρυθμιστεί να δουλεύει με τα διαθέσιμα μοντέλα του GitHub.

## Εγκατάσταση

### 1. Λάβε το GitHub Token σου

1. Πήγαινε στα [GitHub Ρυθμίσεις → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Κάνε κλικ στο "Generate new token"
3. Δώσε ένα περιγραφικό όνομα (π.χ. "LangChain4j Demo")
4. Ορίστε ημερομηνία λήξης (συνιστάται 7 ημέρες)
5. Στις "Δικαιώματα λογαριασμού", βρες το "Models" και όρισε "Read-only"
6. Κάνε κλικ στο "Generate token"
7. Αντέγραψε και αποθήκευσε το token σου - δεν θα το ξαναδείς

### 2. Ορισμός του Token σου

**Επιλογή 1: Χρήση VS Code (Συνιστάται)**

Αν χρησιμοποιείς το VS Code, πρόσθεσε το token σου στο αρχείο `.env` στη ρίζα του έργου:

Αν δεν υπάρχει το `.env`, κάνε αντιγραφή από το `.env.example` ή δημιούργησε ένα νέο `.env` στη ρίζα του έργου.

**Παράδειγμα αρχείου `.env`:**
```bash
# Στο /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Μετά, μπορείς απλά να κάνεις δεξί κλικ σε οποιοδήποτε αρχείο demo (π.χ. `BasicChatDemo.java`) στον Explorer και να επιλέξεις **"Run Java"** ή να χρησιμοποιήσεις τις ρυθμίσεις εκκίνησης από τον πίνακα Run and Debug.

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

## Εκτέλεση των Παραδειγμάτων

**Με VS Code:** Απλώς κάνε δεξί κλικ σε οποιοδήποτε αρχείο demo στον Explorer και επίλεξε **"Run Java"**, ή χρησιμοποίησε τις ρυθμίσεις εκκίνησης από τον πίνακα Run and Debug (σιγουρέψου ότι έχεις προσθέσει το token στο αρχείο `.env` πρώτα).

**Με Maven:** Εναλλακτικά, μπορείς να τρέξεις από τη γραμμή εντολών:

### 1. Βασική Συνομιλία

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Πρότυπα Prompt

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

Το AI καλεί αυτόματα τις Java μεθόδους σου όταν χρειάζεται.

### 4. Ερωτήσεις & Απαντήσεις Εγγράφων (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Κάνε ερωτήσεις για το περιεχόμενο του `document.txt`.

### 5. Υπεύθυνη Τεχνητή Νοημοσύνη

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Δες πώς τα φίλτρα ασφαλείας AI μπλοκάρουν επιβλαβές περιεχόμενο.

## Τι Δείχνει Κάθε Παράδειγμα

**Βασική Συνομιλία** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Ξεκίνα εδώ για να δεις το LangChain4j στην πιο απλή του μορφή. Θα δημιουργήσεις ένα `OpenAiOfficialChatModel`, θα στείλεις ένα prompt με `.chat()` και θα λάβεις απάντηση. Αυτό δείχνει το θεμέλιο: πώς να αρχικοποιείς μοντέλα με προσαρμοσμένα endpoints και κλειδιά API. Μόλις καταλάβεις αυτό το πρότυπο, τα υπόλοιπα χτίζονται πάνω σε αυτό.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Δοκίμασε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Άνοιξε το [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) και ρώτησε:
> - "Πώς θα άλλαζα από τα GitHub Models στο Azure OpenAI σε αυτόν τον κώδικα;"
> - "Ποιοι άλλοι παράμετροι μπορώ να ρυθμίσω στο OpenAiOfficialChatModel.builder();"
> - "Πώς προσθέτω streaming απαντήσεις αντί να περιμένω την ολοκληρωμένη απάντηση;"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Τώρα που ξέρεις πώς να μιλάς σε ένα μοντέλο, ας εξερευνήσουμε τι λες σε αυτό. Αυτό το demo χρησιμοποιεί την ίδια ρύθμιση μοντέλου αλλά δείχνει πέντε διαφορετικά πρότυπα prompt. Δοκίμασε zero-shot prompts για άμεσες οδηγίες, few-shot prompts που μαθαίνουν από παραδείγματα, chain-of-thought prompts που αποκαλύπτουν βήματα συλλογισμού, και role-based prompts που ορίζουν πλαίσιο. Θα δεις πώς το ίδιο μοντέλο δίνει δραματικά διαφορετικά αποτελέσματα ανάλογα με το πώς διαμορφώνεις το αίτημά σου.

Το demo παρουσιάζει επίσης πρότυπα prompt, που είναι μια ισχυρή μέθοδος για τη δημιουργία επαναχρησιμοποιήσιμων prompts με μεταβλητές.
Το παρακάτω παράδειγμα δείχνει ένα prompt που χρησιμοποιεί το LangChain4j `PromptTemplate` για να συμπληρώσει μεταβλητές. Το AI θα απαντήσει βάσει του προορισμού και της δραστηριότητας που παρέχονται.

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

> **🤖 Δοκίμασε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Άνοιξε το [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) και ρώτησε:
> - "Ποια είναι η διαφορά μεταξύ zero-shot και few-shot prompting, και πότε πρέπει να χρησιμοποιώ κάθε ένα;"
> - "Πώς η παράμετρος θερμοκρασίας επηρεάζει τις απαντήσεις του μοντέλου;"
> - "Ποιες είναι κάποιες τεχνικές για να αποτρέψω επιθέσεις prompt injection σε παραγωγή;"
> - "Πώς μπορώ να δημιουργήσω επαναχρησιμοποιήσιμα αντικείμενα PromptTemplate για κοινά πρότυπα;"

**Ενσωμάτωση Εργαλείων** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Εδώ το LangChain4j γίνεται ισχυρό. Θα χρησιμοποιήσεις το `AiServices` για να δημιουργήσεις έναν AI βοηθό που μπορεί να καλεί τις Java μεθόδους σου. Απλώς επισημαίνεις μεθόδους με `@Tool("περιγραφή")` και το LangChain4j φροντίζει τα υπόλοιπα - το AI αποφασίζει αυτόματα πότε να χρησιμοποιήσει κάθε εργαλείο βάσει όσων ζητά ο χρήστης. Αυτό δείχνει την κλήση συναρτήσεων, μια βασική τεχνική για τη δημιουργία AI που μπορεί να παίρνει ενέργειες, όχι μόνο να απαντά σε ερωτήσεις.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Δοκίμασε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Άνοιξε το [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) και ρώτησε:
> - "Πώς δουλεύει η annotation @Tool και τι κάνει το LangChain4j με αυτήν πίσω από τις σκηνές;"
> - "Μπορεί το AI να καλέσει πολλαπλά εργαλεία σε σειρά για να λύσει σύνθετα προβλήματα;"
> - "Τι συμβαίνει αν ένα εργαλείο πετάξει εξαίρεση - πώς πρέπει να διαχειριστώ σφάλματα;"
> - "Πώς θα ενσωμάτωνα ένα πραγματικό API αντί για αυτό το παράδειγμα αριθμομηχανής;"

**Ερωτήσεις & Απαντήσεις Εγγράφων (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Εδώ θα δεις το θεμέλιο του RAG (αναζήτηση-ενισχυμένη παραγωγή). Αντί να βασίζεσαι στα δεδομένα εκπαίδευσης του μοντέλου, φορτώνεις περιεχόμενο από το [`document.txt`](../../../00-quick-start/document.txt) και το συμπεριλαμβάνεις στο prompt. Το AI απαντά βάσει του εγγράφου σου, όχι της γενικής γνώσης του. Αυτό είναι το πρώτο βήμα για τη δημιουργία συστημάτων που μπορούν να δουλεύουν με τα δικά σου δεδομένα.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Σημείωση:** Αυτή η απλή προσέγγιση φορτώνει ολόκληρο το έγγραφο μέσα στο prompt. Για μεγάλα αρχεία (>10KB), θα ξεπεράσεις τα όρια του context. Η Ενότητα 03 καλύπτει τον κατακερματισμό και αναζήτηση vector για παραγωγικά RAG συστήματα.

> **🤖 Δοκίμασε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Άνοιξε το [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) και ρώτησε:
> - "Πώς το RAG αποτρέπει τις παραισθήσεις AI σε σύγκριση με τη χρήση των δεδομένων εκπαίδευσης του μοντέλου;"
> - "Ποια η διαφορά μεταξύ αυτής της απλής προσέγγισης και της χρήσης vector embeddings για ανάκτηση;"
> - "Πώς θα κλιμάκωνα αυτό για να χειρίζομαι πολλαπλά έγγραφα ή μεγαλύτερες βάσεις γνώσης;"
> - "Ποιες είναι οι βέλτιστες πρακτικές για τη δομή του prompt ώστε να διασφαλιστεί ότι το AI χρησιμοποιεί μόνο το παρεχόμενο πλαίσιο;"

**Υπεύθυνη Τεχνητή Νοημοσύνη** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Δημιούργησε ασφάλεια για την AI με πολυεπίπεδη άμυνα. Αυτό το demo δείχνει δύο στρώματα προστασίας που συνεργάζονται:

**Μέρος 1: LangChain4j Περιφράξεις Εισόδου** - Μπλοκάρουν επικίνδυνα prompts πριν φτάσουν στο LLM. Δημιουργείς προσαρμοσμένες φρουρές που ελέγχουν για απαγορευμένες λέξεις ή μοτίβα. Τρέχουν στον κώδικά σου, οπότε είναι γρήγορες και δωρεάν.

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

**Μέρος 2: Φίλτρα Ασφαλείας Παρόχου** - Τα GitHub Models διαθέτουν ενσωματωμένα φίλτρα που πιάνουν ό,τι μπορεί να ξεφύγει από τις φρουρές σου. Θα δεις σκληρές μπλοκάρεις (σφάλματα HTTP 400) για σοβαρές παραβιάσεις και απαλές άρνησεις όπου το AI αρνείται ευγενικά.

> **🤖 Δοκίμασε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Άνοιξε το [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) και ρώτησε:
> - "Τι είναι το InputGuardrail και πώς δημιουργώ τη δική μου;"
> - "Ποια η διαφορά μεταξύ σκληρού μπλοκαρίσματος και απαλής άρνησης;"
> - "Γιατί να χρησιμοποιώ και τις φρουρές και τα φίλτρα παρόχου μαζί;"

## Επόμενα Βήματα

**Επόμενη Ενότητα:** [01-εισαγωγή - Έναρξη με LangChain4j και gpt-5 στο Azure](../01-introduction/README.md)

---

**Πλοήγηση:** [← Πίσω στην Αρχική](../README.md) | [Επόμενο: Ενότητα 01 - Εισαγωγή →](../01-introduction/README.md)

---

## Αντιμετώπιση Προβλημάτων

### Πρώτη Φόρτωση Maven

**Πρόβλημα**: Η αρχική εντολή `mvn clean compile` ή `mvn package` παίρνει πολύ χρόνο (10-15 λεπτά)

**Αιτία**: Το Maven χρειάζεται να κατεβάσει όλες τις εξαρτήσεις του έργου (Spring Boot, βιβλιοθήκες LangChain4j, SDKs Azure κ.λπ.) στην πρώτη κατασκευή.

**Λύση**: Αυτό είναι φυσιολογική συμπεριφορά. Οι επόμενες κατασκευές θα είναι πολύ πιο γρήγορες αφού οι εξαρτήσεις έχουν αποθηκευτεί τοπικά. Ο χρόνος λήψης εξαρτάται από την ταχύτητα του δικτύου σου.
### Σύνταξη Εντολών Maven σε PowerShell

**Πρόβλημα**: Οι εντολές Maven αποτυγχάνουν με το σφάλμα `Unknown lifecycle phase ".mainClass=..."`

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

Ο τελεστής `--%` λέει στο PowerShell να περάσει όλα τα υπόλοιπα ορίσματα κυριολεκτικά στο Maven χωρίς ερμηνεία.

### Εμφάνιση Emoji σε Windows PowerShell

**Πρόβλημα**: Οι απαντήσεις AI εμφανίζουν άχρηστους χαρακτήρες (π.χ., `????` ή `â??`) αντί για emojis στο PowerShell

**Αιτία**: Η προεπιλεγμένη κωδικοποίηση του PowerShell δεν υποστηρίζει emojis UTF-8

**Λύση**: Εκτελέστε αυτήν την εντολή πριν από την εκτέλεση εφαρμογών Java:
```cmd
chcp 65001
```

Αυτό αναγκάζει την κωδικοποίηση UTF-8 στο τερματικό. Εναλλακτικά, χρησιμοποιήστε το Windows Terminal που έχει καλύτερη υποστήριξη Unicode.

### Αποσφαλμάτωση Κλήσεων API

**Πρόβλημα**: Σφάλματα πιστοποίησης, όρια ρυθμού ή απρόσμενες απαντήσεις από το μοντέλο AI

**Λύση**: Τα παραδείγματα περιλαμβάνουν `.logRequests(true)` και `.logResponses(true)` για να εμφανίζουν τις κλήσεις API στην κονσόλα. Αυτό βοηθά στην αντιμετώπιση σφαλμάτων πιστοποίησης, ορίων ρυθμού ή απρόσμενων απαντήσεων. Αφαιρέστε αυτές τις σημαίες στην παραγωγή για να μειώσετε τον θόρυβο στα αρχεία καταγραφής.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που επιδιώκουμε την ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στην μητρική του γλώσσα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν ευθυνόμαστε για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->