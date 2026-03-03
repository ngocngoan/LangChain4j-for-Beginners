# Module 00: Γρήγορη Έναρξη

## Table of Contents

- [Εισαγωγή](../../../00-quick-start)
- [Τι είναι το LangChain4j;](../../../00-quick-start)
- [Εξαρτήσεις LangChain4j](../../../00-quick-start)
- [Προαπαιτούμενα](../../../00-quick-start)
- [Ρύθμιση](../../../00-quick-start)
  - [1. Αποκτήστε το GitHub Token σας](../../../00-quick-start)
  - [2. Ορίστε το Token σας](../../../00-quick-start)
- [Εκτέλεση Παραδειγμάτων](../../../00-quick-start)
  - [1. Βασική Συνομιλία](../../../00-quick-start)
  - [2. Πρότυπα Υπόδειξης](../../../00-quick-start)
  - [3. Κλήση Συναρτήσεων](../../../00-quick-start)
  - [4. Ερωτήσεις & Απαντήσεις Εγγράφων (Easy RAG)](../../../00-quick-start)
  - [5. Υπεύθυνη Τεχνητή Νοημοσύνη](../../../00-quick-start)
- [Τι Δείχνει Κάθε Παράδειγμα](../../../00-quick-start)
- [Επόμενα Βήματα](../../../00-quick-start)
- [Αντιμετώπιση Προβλημάτων](../../../00-quick-start)

## Introduction

Αυτή η γρήγορη εκκίνηση έχει σκοπό να σας βάλει σε λειτουργία με το LangChain4j το ταχύτερο δυνατό. Καλύπτει τα απολύτως βασικά για την κατασκευή εφαρμογών AI με LangChain4j και GitHub Models. Στα επόμενα modules θα μεταβείτε σε Azure OpenAI και GPT-5.2 και θα εμβαθύνετε σε κάθε έννοια.

## What is LangChain4j?

Το LangChain4j είναι μια βιβλιοθήκη Java που απλοποιεί την κατασκευή εφαρμογών με τεχνητή νοημοσύνη. Αντί να ασχολείστε με HTTP clients και ανάλυση JSON, δουλεύετε με καθαρές Java APIs.

Η "αλυσίδα" στο LangChain αναφέρεται στην αλυσίδωση πολλαπλών συστατικών - μπορεί να αλυσιδωθεί ένα prompt σε ένα μοντέλο σε ένα parser, ή να αλυσιδωθούν πολλές κλήσεις AI όπου η έξοδος της μίας τροφοδοτεί την είσοδο της επόμενης. Αυτή η γρήγορη εκκίνηση εστιάζει στα βασικά πριν εξερευνήσει πιο σύνθετες αλυσίδες.

<img src="../../../translated_images/el/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Αλυσιδωτά συστατικά στο LangChain4j - οικοδομικά στοιχεία που συνδέονται για να δημιουργήσουν ισχυρές ροές εργασίας AI*

Θα χρησιμοποιήσουμε τρία βασικά συστατικά:

**ChatModel** - Η διεπαφή για αλληλεπιδράσεις με το μοντέλο AI. Καλείτε `model.chat("prompt")` και λαμβάνετε μια απάντηση ως string. Χρησιμοποιούμε το `OpenAiOfficialChatModel` που λειτουργεί με endpoints συμβατά με OpenAI όπως τα GitHub Models.

**AiServices** - Δημιουργεί type-safe διεπαφές υπηρεσιών AI. Ορίζετε μεθόδους, τις σχολιάζετε με `@Tool`, και το LangChain4j αναλαμβάνει τον συντονισμό. Το AI καλεί αυτόματα τις Java μεθόδους σας όταν χρειάζεται.

**MessageWindowChatMemory** - Διατηρεί το ιστορικό συνομιλίας. Χωρίς αυτό, κάθε αίτημα είναι ανεξάρτητο. Με αυτό, το AI θυμάται προηγούμενα μηνύματα και διατηρεί το πλαίσιο σε πολλούς γύρους.

<img src="../../../translated_images/el/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Αρχιτεκτονική LangChain4j - βασικά συστατικά που συνεργάζονται για να τροφοδοτήσουν τις εφαρμογές AI σας*

## LangChain4j Dependencies

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

Το module `langchain4j-open-ai-official` παρέχει την κλάση `OpenAiOfficialChatModel` που συνδέεται με APIs συμβατά με το OpenAI. Τα GitHub Models χρησιμοποιούν την ίδια μορφή API, οπότε δεν χρειάζεται ειδικός προσαρμογέας - απλά δείξτε τη βάση URL στο `https://models.github.ai/inference`.

Το module `langchain4j-easy-rag` παρέχει αυτόματη διαίρεση εγγράφων, ενσωμάτωση και ανάκτηση ώστε να μπορείτε να χτίσετε εφαρμογές RAG χωρίς χειροκίνητη ρύθμιση κάθε βήματος.

## Προαπαιτούμενα

**Χρησιμοποιείτε το Dev Container;** Java και Maven είναι ήδη εγκατεστημένα. Χρειάζεστε μόνο ένα Personal Access Token του GitHub.

**Τοπική Ανάπτυξη:**
- Java 21+, Maven 3.9+
- Personal Access Token από GitHub (οδηγίες παρακάτω)

> **Σημείωση:** Αυτό το module χρησιμοποιεί το `gpt-4.1-nano` από GitHub Models. Μην τροποποιείτε το όνομα μοντέλου στον κώδικα - έχει ρυθμιστεί να λειτουργεί με τα διαθέσιμα μοντέλα του GitHub.

## Ρύθμιση

### 1. Αποκτήστε το GitHub Token σας

1. Μεταβείτε στο [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Πατήστε "Generate new token"
3. Ορίστε ένα περιγραφικό όνομα (π.χ. "LangChain4j Demo")
4. Ορίστε διάρκεια (συνιστώνται 7 ημέρες)
5. Στο "Account permissions", βρείτε το "Models" και ορίστε σε "Read-only"
6. Πατήστε "Generate token"
7. Αντιγράψτε και αποθηκεύστε το token σας - δεν θα το δείτε ξανά

### 2. Ορίστε το Token σας

**Επιλογή 1: Χρήση VS Code (Συνιστάται)**

Αν χρησιμοποιείτε το VS Code, προσθέστε το token στο αρχείο `.env` στη ρίζα του project:

Αν το αρχείο `.env` δεν υπάρχει, αντιγράψτε το `.env.example` σε `.env` ή δημιουργήστε νέο `.env` στη ρίζα του project.

**Παράδειγμα αρχείου `.env`:**
```bash
# Στο /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Στη συνέχεια μπορείτε απλά να κάνετε δεξί κλικ σε οποιοδήποτε demo αρχείο (π.χ. `BasicChatDemo.java`) στον Explorer και να επιλέξετε **"Run Java"** ή να χρησιμοποιήσετε τις ρυθμίσεις εκκίνησης από το πάνελ Run and Debug.

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

**Χρήση VS Code:** Κάντε δεξί κλικ σε οποιοδήποτε demo αρχείο στον Explorer και επιλέξτε **"Run Java"**, ή χρησιμοποιήστε τις ρυθμίσεις εκκίνησης από το πάνελ Run and Debug (βεβαιωθείτε πρώτα ότι έχετε προσθέσει το token στο `.env`).

**Χρήση Maven:** Εναλλακτικά, μπορείτε να τρέξετε από τη γραμμή εντολών:

### 1. Βασική Συνομιλία

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Πρότυπα Υπόδειξης

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Δείχνει zero-shot, few-shot, chain-of-thought και role-based prompting.

### 3. Κλήση Συναρτήσεων

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Το AI καλεί αυτόματα τις Java μεθόδους σας όταν χρειάζεται.

### 4. Ερωτήσεις & Απαντήσεις Εγγράφων (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Κάντε ερωτήσεις σχετικά με τα έγγραφά σας χρησιμοποιώντας Easy RAG με αυτόματη ενσωμάτωση και ανάκτηση.

### 5. Υπεύθυνη Τεχνητή Νοημοσύνη

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Δείτε πώς τα φίλτρα ασφάλειας AI αποκλείουν επιβλαβές περιεχόμενο.

## Τι Δείχνει Κάθε Παράδειγμα

**Βασική Συνομιλία** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Ξεκινήστε εδώ για να δείτε το LangChain4j στην πιο απλή μορφή του. Θα δημιουργήσετε ένα `OpenAiOfficialChatModel`, θα στείλετε ένα prompt με `.chat()` και θα λάβετε απάντηση. Αυτό δείχνει τα θεμέλια: πώς να αρχικοποιείτε μοντέλα με προσαρμοσμένα endpoints και κλειδιά API. Μόλις κατανοήσετε αυτό το μοτίβο, όλα τα υπόλοιπα χτίζονται πάνω του.

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
> - "Ποιοι άλλοι παράμετροι μπορώ να ρυθμίσω στο OpenAiOfficialChatModel.builder();"
> - "Πώς προσθέτω streaming απαντήσεις αντί να περιμένω την πλήρη απάντηση;"

**Μηχανική Υποδείξεων (Prompt Engineering)** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Τώρα που ξέρετε πώς να μιλάτε σε ένα μοντέλο, ας εξερευνήσουμε τι του λέτε. Αυτό το demo χρησιμοποιεί την ίδια ρύθμιση μοντέλου αλλά δείχνει πέντε διαφορετικά πρότυπα υποδείξεων. Δοκιμάστε zero-shot prompts για άμεσες οδηγίες, few-shot prompts που μαθαίνουν από παραδείγματα, chain-of-thought prompts που αποκαλύπτουν βήματα συλλογισμού, και role-based prompts που θέτουν το πλαίσιο. Θα δείτε πώς το ίδιο μοντέλο δίνει ριζικά διαφορετικά αποτελέσματα ανάλογα με το πώς πλαισιώνετε το αίτημά σας.

Το demo επίσης δείχνει prompt templates, μια ισχυρή μέθοδο για δημιουργία επαναχρησιμοποιούμενων prompts με μεταβλητές.
Το παρακάτω παράδειγμα δείχνει ένα prompt που χρησιμοποιεί το LangChain4j `PromptTemplate` για να γεμίσει μεταβλητές. Το AI απαντά με βάση τον προορισμό και τη δραστηριότητα που παρέχονται.

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
> - "Πώς επηρεάζει η παράμετρος θερμοκρασίας τις απαντήσεις του μοντέλου;"
> - "Ποιες τεχνικές υπάρχουν για να αποτρέψω επιθέσεις prompt injection σε παραγωγή;"
> - "Πώς δημιουργώ επαναχρησιμοποιούμενα PromptTemplate αντικείμενα για κοινά πρότυπα;"

**Ένταξη Εργαλείων (Tool Integration)** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Εδώ γίνεται δυναμικό το LangChain4j. Θα χρησιμοποιήσετε το `AiServices` για να δημιουργήσετε έναν βοηθό AI που μπορεί να καλεί τις Java μεθόδους σας. Απλά σχολιάστε τις μεθόδους σας με `@Tool("περιγραφή")` και το LangChain4j κάνει τα υπόλοιπα - το AI αποφασίζει αυτόματα πότε να χρησιμοποιήσει κάθε εργαλείο ανάλογα με το τι ρωτά ο χρήστης. Αυτό δείχνει την κλήση συναρτήσεων, μια βασική τεχνική για την κατασκευή AI που μπορεί να εκτελεί ενέργειες και όχι μόνο να απαντά σε ερωτήσεις.

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
> - "Πώς λειτουργεί το annotation @Tool και τι κάνει το LangChain4j με αυτό πίσω από τις σκηνές;"
> - "Μπορεί το AI να καλέσει πολλαπλά εργαλεία στη σειρά για να λύσει πολύπλοκα προβλήματα;"
> - "Τι συμβαίνει αν ένα εργαλείο ρίξει εξαίρεση - πώς πρέπει να διαχειριστώ τα σφάλματα;"
> - "Πώς θα ενσωμάτωνα ένα πραγματικό API αντί για αυτό το παράδειγμα υπολογιστή;"

**Ερωτήσεις & Απαντήσεις Εγγράφων (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Εδώ θα δείτε το RAG (retrieval-augmented generation) χρησιμοποιώντας την προσέγγιση "Easy RAG" του LangChain4j. Τα έγγραφα φορτώνονται, χωρίζονται αυτόματα και ενσωματώνονται σε μια ενήμερη μνήμη, μετά ένας ανακτών περιεχομένου παρέχει σχετικά κομμάτια στο AI κατά το χρόνο ερώτησης. Το AI απαντά βάσει των εγγράφων σας, όχι της γενικής του γνώσης.

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
> - "Πώς το RAG αποτρέπει τις ψευδαισθήσεις AI συγκριτικά με τη χρήση δεδομένων εκπαίδευσης του μοντέλου;"
> - "Ποια είναι η διαφορά μεταξύ αυτής της εύκολης προσέγγισης και μιας προσαρμοσμένης RAG pipeline;"
> - "Πώς θα κλιμακώσω αυτό για να χειριστώ πολλαπλά έγγραφα ή μεγαλύτερες βάσεις γνώσης;"

**Υπεύθυνη Τεχνητή Νοημοσύνη** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Χτίστε ασφάλεια AI με πολλαπλά επίπεδα προστασίας. Αυτό το demo δείχνει δύο στρώματα προστασίας που συνεργάζονται:

**Μέρος 1: LangChain4j Input Guardrails** - Αποκλείστε επικίνδυνα prompts πριν φτάσουν στο LLM. Δημιουργήστε προσαρμοσμένα guardrails που ελέγχουν για απαγορευμένες λέξεις-κλειδιά ή πρότυπα. Εκτελούνται στον κώδικά σας, άρα είναι γρήγορα και δωρεάν.

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

**Μέρος 2: Provider Safety Filters** - Τα GitHub Models έχουν ενσωματωμένα φίλτρα που πιάνουν ό,τι τα guardrails σας ίσως χάσουν. Θα δείτε σκληρούς αποκλεισμούς (HTTP 400 σφάλματα) για σοβαρές παραβάσεις και μαλακές απορρίψεις όπου το AI αρνείται ευγενικά.

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) και ρωτήστε:
> - "Τι είναι το InputGuardrail και πώς φτιάχνω δικό μου;"
> - "Ποια είναι η διαφορά μεταξύ σκληρού αποκλεισμού και ήπιας απόρριψης;"
> - "Γιατί να χρησιμοποιώ και guardrails και φίλτρα παρόχου μαζί;"

## Επόμενα Βήματα

**Επόμενο Module:** [01-introduction - Ξεκινώντας με το LangChain4j](../01-introduction/README.md)

---

**Πλοήγηση:** [← Πίσω στην Κεντρική](../README.md) | [Επόμενο: Module 01 - Εισαγωγή →](../01-introduction/README.md)

---

## Αντιμετώπιση Προβλημάτων

### Πρώτη Κατασκευή Maven

**Πρόβλημα:** Αρχικό `mvn clean compile` ή `mvn package` διαρκεί πολύ (10-15 λεπτά)

**Αιτία:** Το Maven πρέπει να κατεβάσει όλες τις εξαρτήσεις του project (Spring Boot, βιβλιοθήκες LangChain4j, Azure SDKs, κ.ά.) στην πρώτη κατασκευή.

**Λύση:** Αυτό είναι φυσιολογικό. Οι επόμενες κατασκευές θα είναι πολύ πιο γρήγορες καθώς οι εξαρτήσεις έχουν cache τοπικά. Ο χρόνος λήψης εξαρτάται από την ταχύτητα του δικτύου σας.

### Σύνταξη Εντολών Maven σε PowerShell

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

Ο τελεστής `--%` λέει στο PowerShell να περάσει όλα τα επόμενα ορίσματα κυριολεκτικά στο Maven χωρίς ερμηνεία.

### Εμφάνιση Emoji στα Windows PowerShell

**Πρόβλημα**: Οι απαντήσεις AI εμφανίζουν άχρηστους χαρακτήρες (π.χ., `????` ή `â??`) αντί για emoji στο PowerShell

**Αιτία**: Η προεπιλεγμένη κωδικοποίηση του PowerShell δεν υποστηρίζει τα emoji UTF-8

**Λύση**: Εκτελέστε αυτή την εντολή πριν από την εκτέλεση εφαρμογών Java:
```cmd
chcp 65001
```

Αυτό αναγκάζει την κωδικοποίηση UTF-8 στο τερματικό. Εναλλακτικά, χρησιμοποιήστε το Windows Terminal που έχει καλύτερη υποστήριξη Unicode.

### Αποσφαλμάτωση Κλήσεων API

**Πρόβλημα**: Σφάλματα ταυτοποίησης, όρια ρυθμού ή μη αναμενόμενες απαντήσεις από το μοντέλο AI

**Λύση**: Τα παραδείγματα περιλαμβάνουν `.logRequests(true)` και `.logResponses(true)` για να εμφανίζουν τις κλήσεις API στην κονσόλα. Αυτό βοηθά στην επίλυση σφαλμάτων ταυτοποίησης, ορίων ρυθμού ή μη αναμενόμενων απαντήσεων. Αφαιρέστε αυτές τις σημαίες στην παραγωγή για μείωση του θορύβου στα αρχεία καταγραφής.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που επιδιώκουμε ακρίβεια, παρακαλούμε να σημειώσετε ότι οι αυτοματοποιημένες μεταφράσεις μπορεί να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του αποτελεί την έγκυρη πηγή. Για κρίσιμες πληροφορίες συνιστάται η επαγγελματική μετάφραση από ανθρώπινο μεταφραστή. Δεν φέρουμε καμία ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->