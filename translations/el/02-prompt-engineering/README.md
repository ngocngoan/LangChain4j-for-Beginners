# Module 02: Μηχανική Εντολών με GPT-5.2

## Πίνακας Περιεχομένων

- [Τι θα Μάθετε](../../../02-prompt-engineering)
- [Προαπαιτούμενα](../../../02-prompt-engineering)
- [Κατανόηση της Μηχανικής Εντολών](../../../02-prompt-engineering)
- [Βασικές Αρχές Μηχανικής Εντολών](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [Προχωρημένα Προτύπα](../../../02-prompt-engineering)
- [Χρήση Υφιστάμενων Πόρων Azure](../../../02-prompt-engineering)
- [Στιγμιότυπα Εφαρμογής](../../../02-prompt-engineering)
- [Εξερεύνηση των Προτύπων](../../../02-prompt-engineering)
  - [Χαμηλό vs Υψηλό Ενθουσιασμό](../../../02-prompt-engineering)
  - [Εκτέλεση Εργασιών (Προλεγόμενα Εργαλείων)](../../../02-prompt-engineering)
  - [Κώδικας Αυτοαπεικόνισης](../../../02-prompt-engineering)
  - [Δομημένη Ανάλυση](../../../02-prompt-engineering)
  - [Πολλαπλός Γύρος Συνομιλίας](../../../02-prompt-engineering)
  - [Βήμα-βήμα Λογισμός](../../../02-prompt-engineering)
  - [Περιορισμένη Έξοδος](../../../02-prompt-engineering)
- [Τι Πραγματικά Μαθαίνετε](../../../02-prompt-engineering)
- [Επόμενα Βήματα](../../../02-prompt-engineering)

## Τι θα Μάθετε

<img src="../../../translated_images/el/what-youll-learn.c68269ac048503b2.webp" alt="Τι θα Μάθετε" width="800"/>

Στο προηγούμενο module είδατε πώς η μνήμη επιτρέπει τη συνομιλιακή ΤΝ και χρησιμοποιήσατε τα GitHub Models για βασικές αλληλεπιδράσεις. Τώρα θα επικεντρωθούμε στο πώς κάνετε ερωτήσεις — τα ίδια τα prompts — χρησιμοποιώντας το GPT-5.2 της Azure OpenAI. Ο τρόπος που διαμορφώνετε τα prompts επηρεάζει δραματικά την ποιότητα των απαντήσεων που λαμβάνετε. Ξεκινάμε με μια ανασκόπηση των βασικών τεχνικών prompt και μετά προχωράμε σε οκτώ προχωρημένα πρότυπα που αξιοποιούν πλήρως τις δυνατότητες του GPT-5.2.

Θα χρησιμοποιήσουμε το GPT-5.2 επειδή εισάγει έλεγχο της λογικής — μπορείτε να πείτε στο μοντέλο πόσο να σκεφτεί πριν απαντήσει. Αυτό κάνει τις διάφορες στρατηγικές prompt πιο εμφανείς και βοηθά να κατανοήσετε πότε να χρησιμοποιήσετε κάθε προσέγγιση. Επιπλέον, θα επωφεληθούμε από τα λιγότερα όρια ρυθμού του Azure για το GPT-5.2 σε σύγκριση με τα GitHub Models.

## Προαπαιτούμενα

- Ολοκληρωμένο Module 01 (αναπτύχθηκαν πόροι Azure OpenAI)
- Αρχείο `.env` στον ριζικό κατάλογο με διαπιστευτήρια Azure (δημιουργημένο από `azd up` στο Module 01)

> **Σημείωση:** Αν δεν έχετε ολοκληρώσει το Module 01, ακολουθήστε πρώτα τις οδηγίες ανάπτυξης εκεί.

## Κατανόηση της Μηχανικής Εντολών

<img src="../../../translated_images/el/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Τι είναι η Μηχανική Εντολών;" width="800"/>

Η μηχανική εντολών αφορά το σχεδιασμό εισαγωγικού κειμένου που σας δίνει σταθερά τα αποτελέσματα που χρειάζεστε. Δεν πρόκειται μόνο για το να κάνετε ερωτήσεις — αλλά για τη δομή των αιτημάτων ώστε το μοντέλο να καταλάβει ακριβώς τι θέλετε και πώς να το παραδώσει.

Σκεφτείτε το σαν να δίνετε οδηγίες σε έναν συνάδελφο. "Διόρθωσε το σφάλμα" είναι ασαφές. "Διόρθωσε το null pointer exception στο UserService.java στη γραμμή 45 προσθέτοντας έλεγχο null" είναι συγκεκριμένο. Τα γλωσσικά μοντέλα λειτουργούν το ίδιο — η συγκεκριμενοποίηση και η δομή έχουν σημασία.

<img src="../../../translated_images/el/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Πώς ταιριάζει το LangChain4j" width="800"/>

Το LangChain4j παρέχει την υποδομή — συνδέσεις μοντέλων, μνήμη και τύπους μηνυμάτων — ενώ τα πρότυπα prompt είναι απλώς προσεκτικά δομημένο κείμενο που στέλνετε μέσω αυτής της υποδομής. Τα βασικά στοιχεία είναι τα `SystemMessage` (που καθορίζουν τη συμπεριφορά και τον ρόλο της ΤΝ) και `UserMessage` (που φέρουν το αίτημά σας).

## Βασικές Αρχές Μηχανικής Εντολών

<img src="../../../translated_images/el/five-patterns-overview.160f35045ffd2a94.webp" alt="Πέντε Βασικά Πρότυπα Μηχανικής Εντολών" width="800"/>

Πριν εμβαθύνουμε στα προχωρημένα πρότυπα αυτού του module, ας ανασκοπήσουμε πέντε θεμελιώδεις τεχνικές prompt. Αυτά είναι τα δομικά στοιχεία που κάθε μηχανικός prompt πρέπει να γνωρίζει. Αν έχετε ήδη περάσει από το [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), τα έχετε δει σε δράση — εδώ είναι το εννοιολογικό πλαίσιο πίσω τους.

### Zero-Shot Prompting

Η πιο απλή προσέγγιση: δώστε στο μοντέλο μια άμεση εντολή χωρίς παραδείγματα. Το μοντέλο βασίζεται πλήρως στην εκπαίδευσή του για να κατανοήσει και να εκτελέσει την εργασία. Αυτό λειτουργεί καλά για απλά αιτήματα όπου η αναμενόμενη συμπεριφορά είναι προφανής.

<img src="../../../translated_images/el/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Άμεση εντολή χωρίς παραδείγματα — το μοντέλο συμπεραίνει την εργασία μόνο από την εντολή*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Απάντηση: "Θετικό"
```

**Πότε να χρησιμοποιήσετε:** Απλές ταξινομήσεις, άμεσες ερωτήσεις, μεταφράσεις ή οποιαδήποτε εργασία μπορεί να χειριστεί το μοντέλο χωρίς επιπλέον καθοδήγηση.

### Few-Shot Prompting

Δώστε παραδείγματα που δείχνουν το πρότυπο που θέλετε να ακολουθήσει το μοντέλο. Το μοντέλο μαθαίνει τη μορφή εισόδου-εξόδου από τα παραδείγματά σας και την εφαρμόζει σε νέες εισόδους. Αυτό βελτιώνει δραματικά τη συνέπεια σε εργασίες όπου η επιθυμητή μορφή ή συμπεριφορά δεν είναι προφανής.

<img src="../../../translated_images/el/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Μάθηση από παραδείγματα — το μοντέλο αναγνωρίζει το πρότυπο και το εφαρμόζει σε νέες εισόδους*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Πότε να χρησιμοποιήσετε:** Προσαρμοσμένες ταξινομήσεις, συνεπής μορφοποίηση, εργασίες συγκεκριμένου τομέα ή όταν τα αποτελέσματα zero-shot είναι ασυνεπή.

### Chain of Thought

Ζητήστε από το μοντέλο να δείξει τη λογική του βήμα-βήμα. Αντί να πηδήξει κατευθείαν σε μια απάντηση, το μοντέλο αναλύει το πρόβλημα και δουλεύει κάθε μέρος ρητά. Αυτό αυξάνει την ακρίβεια στα μαθηματικά, τη λογική και εργασίες πολλαπλών βημάτων.

<img src="../../../translated_images/el/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Λογισμός βήμα-βήμα — ανάλυση σύνθετων προβλημάτων σε ρητές λογικές ενέργειες*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Το μοντέλο δείχνει: 15 - 8 = 7, μετά 7 + 12 = 19 μήλα
```

**Πότε να χρησιμοποιήσετε:** Μαθηματικά προβλήματα, λογικά παζλ, εντοπισμός σφαλμάτων ή οποιαδήποτε εργασία όπου η εμφάνιση της λογικής βελτιώνει την ακρίβεια και την αξιοπιστία.

### Role-Based Prompting

Ορίστε μια περσόνα ή ρόλο για την ΤΝ πριν κάνετε την ερώτηση. Αυτό παρέχει πλαίσιο που διαμορφώνει τον τόνο, το βάθος και την εστίαση της απάντησης. Ένας "αρχιτέκτονας λογισμικού" δίνει διαφορετικές συμβουλές από έναν "junior developer" ή έναν "ελεγκτή ασφαλείας".

<img src="../../../translated_images/el/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Ορισμός πλαισίου και περσόνας — η ίδια ερώτηση παίρνει διαφορετική απάντηση ανάλογα με τον ανατεθέντα ρόλο*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Πότε να χρησιμοποιήσετε:** Κριτικές κώδικα, διδασκαλία, ανάλυση συγκεκριμένου τομέα ή όταν χρειάζεστε απαντήσεις προσαρμοσμένες σε επίπεδο εμπειρίας ή προοπτική.

### Prompt Templates

Δημιουργήστε ξανάχρησιμοποιήσιμα prompts με μεταβλητές. Αντί να γράφετε νέο prompt κάθε φορά, ορίστε ένα πρότυπο μια φορά και συμπληρώστε διαφορετικές τιμές. Η κλάση `PromptTemplate` του LangChain4j το καθιστά εύκολο με σύνταξη `{{variable}}`.

<img src="../../../translated_images/el/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Ξανάχρησιμοποιήσιμα prompts με μεταβλητές — ένα πρότυπο, πολλές χρήσεις*

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

**Πότε να χρησιμοποιήσετε:** Επαναλαμβανόμενα ερωτήματα με διαφορετικές εισόδους, μαζική επεξεργασία, δημιουργία ξανάχρησιμοποιήσιμων ροών εργασίας ΤΝ ή οποιοδήποτε σενάριο όπου η δομή του prompt μένει ίδια αλλά τα δεδομένα αλλάζουν.

---

Αυτά τα πέντε θεμελιώδη δίνουν ένα σταθερό εργαλείο για τις περισσότερες εργασίες prompt. Το υπόλοιπο του module χτίζεται επάνω τους με **οκτώ προχωρημένα πρότυπα** που εκμεταλλεύονται τον έλεγχο λογισμού, την αυτοαξιολόγηση και τις δομημένες εξόδους του GPT-5.2.

## Προχωρημένα Πρότυπα

Με τις βασικές αρχές καλυμμένες, ας περάσουμε στα οκτώ προχωρημένα πρότυπα που κάνουν αυτό το module μοναδικό. Δεν χρειάζονται όλα τα προβλήματα την ίδια προσέγγιση. Μερικές ερωτήσεις χρειάζονται γρήγορες απαντήσεις, άλλες βαθιά σκέψη. Μερικές χρειάζονται ορατό λογισμό, άλλες μόνο αποτελέσματα. Κάθε πρότυπο παρακάτω είναι βελτιστοποιημένο για διαφορετικό σενάριο — και ο έλεγχος λογισμού του GPT-5.2 κάνει τις διαφορές ακόμα πιο εμφανείς.

<img src="../../../translated_images/el/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Οκτώ Πρότυπα Prompting" width="800"/>

*Επισκόπηση οκτώ προτύπων μηχανικής εντολών και των χρήσεών τους*

<img src="../../../translated_images/el/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Έλεγχος Λογισμού με GPT-5.2" width="800"/>

*Ο έλεγχος λογισμού του GPT-5.2 σας επιτρέπει να ορίσετε πόση σκέψη να κάνει το μοντέλο — από γρήγορες άμεσες απαντήσεις έως βαθιά διερεύνηση*

<img src="../../../translated_images/el/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Σύγκριση Προσπάθειας Λογισμού" width="800"/>

*Χαμηλό ενθουσιασμό (γρήγορο, άμεσο) vs Υψηλό ενθουσιασμό (λεπτομερής, εξερευνητικός)*

**Χαμηλός Ενθουσιασμός (Γρήγορο & Εστιασμένο)** - Για απλές ερωτήσεις όπου θέλετε γρήγορες, άμεσες απαντήσεις. Το μοντέλο κάνει ελάχιστο λογισμό - το πολύ 2 βήματα. Χρησιμοποιήστε το για υπολογισμούς, αναζητήσεις ή απλές ερωτήσεις.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Εξερευνήστε με GitHub Copilot:** Ανοίξτε το [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) και ρωτήστε:
> - "Ποια η διαφορά μεταξύ των προτύπων χαμηλού και υψηλού ενθουσιασμού;"
> - "Πώς βοηθούν οι ετικέτες XML στα prompts στη δομή της απάντησης της ΤΝ;"
> - "Πότε να χρησιμοποιώ πρότυπα αυτοαπεικόνισης έναντι άμεσων εντολών;"

**Υψηλός Ενθουσιασμός (Βαθύς & Λεπτομερής)** - Για σύνθετα προβλήματα όπου θέλετε αναλυτική εξέταση. Το μοντέλο ερευνά λεπτομερώς και δείχνει εκτενή λογισμό. Χρησιμοποιήστε το για σχεδιασμό συστήματος, αρχιτεκτονικές αποφάσεις ή σύνθετη έρευνα.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Εκτέλεση Εργασιών (Πρόοδος Βήμα-βήμα)** - Για ροές εργασιών πολλαπλών βημάτων. Το μοντέλο παρέχει αρχικό σχέδιο, αφηγείται κάθε βήμα καθώς προχωρά, και δίνει περίληψη. Χρησιμοποιήστε το για μεταφορές, υλοποιήσεις ή οποιαδήποτε διαδικασία πολλαπλών βημάτων.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Η προσέγγιση Chain-of-Thought ζητά ρητά από το μοντέλο να εμφανίσει τη λογική του διεργασία, βελτιώνοντας την ακρίβεια σε σύνθετα καθήκοντα. Η ανάλυση βήμα-βήμα βοηθά τόσο τους ανθρώπους όσο και την ΤΝ να κατανοήσουν τη λογική.

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ρωτήστε για αυτό το πρότυπο:
> - "Πώς θα προσαρμόσω το πρότυπο εκτέλεσης εργασιών για μακροχρόνιες λειτουργίες;"
> - "Ποιες είναι οι καλύτερες πρακτικές για τη δομή προλεγόμενων εργαλείων σε εφαρμογές παραγωγής;"
> - "Πώς μπορώ να καταγράψω και να εμφανίσω ενδιάμεσα ενημερώσεις προόδου σε UI;"

<img src="../../../translated_images/el/task-execution-pattern.9da3967750ab5c1e.webp" alt="Πρότυπο Εκτέλεσης Εργασιών" width="800"/>

*Ροή εργασίας Σχεδίασε → Εκτέλεσε → Περίληψη για εργασίες πολλαπλών βημάτων*

**Κώδικας Αυτοαπεικόνισης** - Για δημιουργία κώδικα παραγωγικής ποιότητας. Το μοντέλο παράγει κώδικα που ακολουθεί πρότυπα παραγωγής με σωστή διαχείριση σφαλμάτων. Χρησιμοποιήστε το για κατασκευή νέων λειτουργιών ή υπηρεσιών.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/el/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Κύκλος Αυτοαπεικόνισης" width="800"/>

*Βρόχος επαναληπτικής βελτίωσης - δημιουργία, αξιολόγηση, εντοπισμός προβλημάτων, βελτίωση, επανάληψη*

**Δομημένη Ανάλυση** - Για συνεπή αξιολόγηση. Το μοντέλο εξετάζει κώδικα με σταθερό πλαίσιο (ορθή λειτουργία, πρακτικές, απόδοση, ασφάλεια, συντηρησιμότητα). Χρησιμοποιήστε το για κριτικές κώδικα ή εκτιμήσεις ποιότητας.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ρωτήστε για δομημένη ανάλυση:
> - "Πώς μπορώ να προσαρμόσω το πλαίσιο ανάλυσης για διαφορετικούς τύπους κριτικών κώδικα;"
> - "Ποιος είναι ο καλύτερος τρόπος να αναλύσω και να δράσω σε δομημένη έξοδο προγραμματιστικά;"
> - "Πώς εξασφαλίζω συνεπείς βαθμίδες σοβαρότητας σε διαφορετικές συνεδρίες κριτικής;"

<img src="../../../translated_images/el/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Πρότυπο Δομημένης Ανάλυσης" width="800"/>

*Πλαίσιο για συνεπείς κριτικές κώδικα με επίπεδα σοβαρότητας*

**Πολλαπλός Γύρος Συνομιλίας** - Για συνομιλίες που χρειάζονται πλαίσιο. Το μοντέλο θυμάται προηγούμενα μηνύματα και χτίζει επάνω τους. Χρησιμοποιήστε το για διαδραστικές συνεδρίες βοήθειας ή σύνθετες ερωταπαντήσεις.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/el/context-memory.dff30ad9fa78832a.webp" alt="Μνήμη Πλαισίου" width="800"/>

*Πώς συσσωρεύεται το πλαίσιο συνομιλίας μέσα σε πολλαπλούς γύρους μέχρι το όριο των tokens*

**Βήμα-βήμα Λογισμός** - Για προβλήματα που απαιτούν ορατή λογική. Το μοντέλο δείχνει ρητή λογική για κάθε βήμα. Χρησιμοποιήστε το για μαθηματικά προβλήματα, λογικά παζλ ή όταν θέλετε να κατανοήσετε τη διαδικασία σκέψης.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/el/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Πρότυπο Βήμα-βήμα" width="800"/>

*Ανάλυση προβλημάτων σε ρητές λογικές ενέργειες*

**Περιορισμένη Έξοδος** - Για απαντήσεις με ειδικές απαιτήσεις μορφής. Το μοντέλο ακολουθεί αυστηρά κανόνες μορφής και μήκους. Χρησιμοποιήστε το για περιλήψεις ή όταν χρειάζεστε ακριβή δομή εξόδου.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/el/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Πρότυπο Περιορισμένης Εξόδου" width="800"/>

*Επιβολή συγκεκριμένης μορφής, μήκους και δομής*

## Χρήση Υφιστάμενων Πόρων Azure

**Επαλήθευση ανάπτυξης:**

Βεβαιωθείτε ότι το αρχείο `.env` υπάρχει στον ριζικό κατάλογο με διαπιστευτήρια Azure (δημιουργήθηκε κατά το Module 01):
```bash
cat ../.env  # Θα πρέπει να εμφανίζει το AZURE_OPENAI_ENDPOINT, το API_KEY, το DEPLOYMENT
```

**Εκκίνηση της εφαρμογής:**

> **Σημείωση:** Αν έχετε ήδη ξεκινήσει όλες τις εφαρμογές με `./start-all.sh` από το Module 01, αυτό το module τρέχει ήδη στην πόρτα 8083. Μπορείτε να παραλείψετε τις παρακάτω εντολές εκκίνησης και να πάτε απευθείας στο http://localhost:8083.

**Επιλογή 1: Χρήση Spring Boot Dashboard (Συνιστάται για χρήστες VS Code)**

Το dev container περιλαμβάνει την επέκταση Spring Boot Dashboard, που παρέχει οπτική διεπαφή για τη διαχείριση όλων των εφαρμογών Spring Boot. Μπορείτε να τη βρείτε στη Γραμμή Δραστηριότητας αριστερά στο VS Code (αναζητήστε το εικονίδιο Spring Boot).
Από τον Πίνακα Ελέγχου Spring Boot, μπορείτε να:
- Δείτε όλες τις διαθέσιμες εφαρμογές Spring Boot στο χώρο εργασίας
- Εκκινήστε/σταματήστε εφαρμογές με ένα μόνο κλικ
- Δείτε τα αρχεία καταγραφής εφαρμογών σε πραγματικό χρόνο
- Παρακολουθήστε την κατάσταση της εφαρμογής

Απλά κάντε κλικ στο κουμπί αναπαραγωγής δίπλα στο "prompt-engineering" για να ξεκινήσετε αυτήν τη μονάδα, ή ξεκινήστε όλες τις μονάδες ταυτόχρονα.

<img src="../../../translated_images/el/dashboard.da2c2130c904aaf0.webp" alt="Πίνακας Ελέγχου Spring Boot" width="400"/>

**Επιλογή 2: Χρήση εντολών shell**

Ξεκινήστε όλες τις διαδικτυακές εφαρμογές (μονάδες 01-04):

**Bash:**
```bash
cd ..  # Από τον ριζικό κατάλογο
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Από τον ριζικό κατάλογο
.\start-all.ps1
```

Ή ξεκινήστε μόνο αυτήν τη μονάδα:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Και τα δύο σενάρια φορτώνουν αυτόματα τις μεταβλητές περιβάλλοντος από το αρχικό αρχείο `.env` και θα δημιουργήσουν τα JARs αν δεν υπάρχουν.

> **Σημείωση:** Εάν προτιμάτε να δημιουργήσετε όλες τις μονάδες χειροκίνητα πριν την εκκίνηση:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Ανοίξτε το http://localhost:8083 στον περιηγητή σας.

**Για διακοπή:**

**Bash:**
```bash
./stop.sh  # Μόνο αυτό το module
# Ή
cd .. && ./stop-all.sh  # Όλα τα modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Μόνο αυτό το μονάδα
# Ή
cd ..; .\stop-all.ps1  # Όλες οι μονάδες
```

## Στιγμιότυπα Εφαρμογών

<img src="../../../translated_images/el/dashboard-home.5444dbda4bc1f79d.webp" alt="Αρχική του Πίνακα Ελέγχου" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Ο κύριος πίνακας ελέγχου που δείχνει και τα 8 μοτίβα μηχανικής προτροπής με τα χαρακτηριστικά τους και τις περιπτώσεις χρήσης*

## Εξερεύνηση των Μοτίβων

Η διαδικτυακή διεπαφή σας επιτρέπει να πειραματιστείτε με διαφορετικές στρατηγικές προτροπής. Κάθε μοτίβο λύνει διαφορετικά προβλήματα - δοκιμάστε τα για να δείτε πότε λάμπει η κάθε προσέγγιση.

### Χαμηλή vs Υψηλή Προθυμία

Κάντε μια απλή ερώτηση όπως "Ποιο είναι το 15% του 200;" χρησιμοποιώντας Χαμηλή Προθυμία. Θα λάβετε μια άμεση, απευθείας απάντηση. Τώρα ρωτήστε κάτι πολύπλοκο όπως "Σχεδίασε μια στρατηγική caching για ένα API με μεγάλη κίνηση" χρησιμοποιώντας Υψηλή Προθυμία. Παρακολουθήστε πώς το μοντέλο επιβραδύνει και παρέχει λεπτομερή αιτιολόγηση. Το ίδιο μοντέλο, η ίδια δομή ερώτησης - αλλά η προτροπή του λέει πόσο να σκεφτεί.

<img src="../../../translated_images/el/low-eagerness-demo.898894591fb23aa0.webp" alt="Παρουσίαση Χαμηλής Προθυμίας" width="800"/>

*Γρήγορος υπολογισμός με ελάχιστη αιτιολόγηση*

<img src="../../../translated_images/el/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Παρουσίαση Υψηλής Προθυμίας" width="800"/>

*Πλήρης στρατηγική caching (2.8MB)*

### Εκτέλεση Εργασίας (Προλόγοι Εργαλείων)

Οι πολυβηματικές ροές εργασίας ωφελούνται από τον προγραμματισμό και την αφήγηση προόδου εκ των προτέρων. Το μοντέλο περιγράφει τι θα κάνει, αφηγείται κάθε βήμα και μετά συνοψίζει τα αποτελέσματα.

<img src="../../../translated_images/el/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Παρουσίαση Εκτέλεσης Εργασίας" width="800"/>

*Δημιουργία ενός REST endpoint με αφήγηση βήμα προς βήμα (3.9MB)*

### Αυτο-Ανακλαστικός Κώδικας

Δοκιμάστε "Δημιουργία υπηρεσίας επαλήθευσης email". Αντί απλά να παράγει κώδικα και να σταματά, το μοντέλο παράγει, αξιολογεί με βάση κριτήρια ποιότητας, εντοπίζει αδυναμίες και βελτιώνει. Θα βλέπετε να επαναλαμβάνει μέχρι ο κώδικας να φτάσει σε πρότυπα παραγωγής.

<img src="../../../translated_images/el/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Παρουσίαση Αυτο-Ανακλαστικού Κώδικα" width="800"/>

*Πλήρης υπηρεσία επαλήθευσης email (5.2MB)*

### Δομημένη Ανάλυση

Οι αναθεωρήσεις κώδικα χρειάζονται συνεπή πλαίσια αξιολόγησης. Το μοντέλο αναλύει τον κώδικα χρησιμοποιώντας σταθερές κατηγορίες (σωστότητα, πρακτικές, απόδοση, ασφάλεια) με επίπεδα σοβαρότητας.

<img src="../../../translated_images/el/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Παρουσίαση Δομημένης Ανάλυσης" width="800"/>

*Αναθεώρηση κώδικα βάσει πλαισίου*

### Πολλαπλός Γύρος Συνομιλίας

Ρωτήστε "Τι είναι το Spring Boot;" και αμέσως μετά πείτε "Δείξε μου ένα παράδειγμα". Το μοντέλο θυμάται την πρώτη ερώτησή σας και σας δίνει συγκεκριμένα ένα παράδειγμα Spring Boot. Χωρίς μνήμη, η δεύτερη ερώτηση θα ήταν πολύ γενική.

<img src="../../../translated_images/el/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Παρουσίαση Πολλαπλού Γύρου Συνομιλίας" width="800"/>

*Διατήρηση συμφραζομένων ανάμεσα στις ερωτήσεις*

### Λογισμός Βήμα-Βήμα

Διαλέξτε ένα μαθηματικό πρόβλημα και δοκιμάστε το με Λογισμό Βήμα-Βήμα και Χαμηλή Προθυμία. Η χαμηλή προθυμία απλά δίνει την απάντηση - γρήγορη αλλά αδιαφανή. Ο λογισμός βήμα-βήμα σας δείχνει κάθε υπολογισμό και απόφαση.

<img src="../../../translated_images/el/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Παρουσίαση Λογισμού Βήμα-Βήμα" width="800"/>

*Μαθηματικό πρόβλημα με ρητά βήματα*

### Περιορισμένη Έξοδος

Όταν χρειάζεστε συγκεκριμένες μορφές ή αριθμό λέξεων, αυτό το μοτίβο επιβάλλει αυστηρή συμμόρφωση. Δοκιμάστε να δημιουργήσετε μια περίληψη με ακριβώς 100 λέξεις σε μορφή κουκίδων.

<img src="../../../translated_images/el/constrained-output-demo.567cc45b75da1633.webp" alt="Παρουσίαση Περιορισμένης Εξόδου" width="800"/>

*Περίληψη μηχανικής μάθησης με έλεγχο μορφής*

## Τι Μαθαίνετε Πραγματικά

**Η Προσπάθεια Λογισμού Αλλάζει Τα Πάντα**

Το GPT-5.2 σας επιτρέπει να ελέγχετε τον υπολογιστικό κόπο μέσω των προτροπών σας. Η χαμηλή προσπάθεια σημαίνει γρήγορες απαντήσεις με ελάχιστη εξερεύνηση. Η υψηλή προσπάθεια σημαίνει ότι το μοντέλο παίρνει χρόνο να σκεφτεί βαθιά. Μαθαίνετε να ταιριάζετε την προσπάθεια στην πολυπλοκότητα της εργασίας - μην σπαταλάτε χρόνο σε απλές ερωτήσεις, αλλά ούτε βιάζεστε σε σύνθετες αποφάσεις.

**Η Δομή Καθοδηγεί Συμπεριφορά**

Παρατηρείτε τα XML tags στις προτροπές; Δεν είναι διακοσμητικά. Τα μοντέλα ακολουθούν δομημένες οδηγίες πιο αξιόπιστα από ελεύθερο κείμενο. Όταν χρειάζεστε πολλαπλά βήματα ή σύνθετη λογική, η δομή βοηθά το μοντέλο να παρακολουθεί που βρίσκεται και τι έρχεται μετά.

<img src="../../../translated_images/el/prompt-structure.a77763d63f4e2f89.webp" alt="Δομή Προτροπής" width="800"/>

*Ανατομία μιας καλά δομημένης προτροπής με σαφείς ενότητες και οργάνωση τύπου XML*

**Ποιότητα Μέσω Αυτο-Αξιολόγησης**

Τα μοτίβα αυτο-ανακλαστικής λειτουργίας δουλεύουν κάνοντας τα κριτήρια ποιότητας ρητά. Αντί να ελπίζετε ότι το μοντέλο "το κάνει σωστά", του λέτε ακριβώς τι σημαίνει "σωστά": ορθή λογική, χειρισμός σφαλμάτων, απόδοση, ασφάλεια. Το μοντέλο μπορεί να αξιολογήσει τη δική του έξοδο και να βελτιωθεί. Αυτό μετατρέπει τη δημιουργία κώδικα από λοταρία σε διαδικασία.

**Το Συμφραζόμενο Είναι Πεπερασμένο**

Οι πολύγλωσσες συνομιλίες λειτουργούν με την ενσωμάτωση ιστορικού μηνυμάτων σε κάθε αίτημα. Αλλά υπάρχει όριο - κάθε μοντέλο έχει μέγιστο αριθμό τοκενών. Καθώς οι συνομιλίες μεγαλώνουν, θα χρειαστείτε στρατηγικές για να κρατάτε το σχετικό πλαίσιο χωρίς να φτάνετε στο όριο. Αυτή η μονάδα σας δείχνει πώς λειτουργεί η μνήμη· αργότερα θα μάθετε πότε να συνοψίζετε, πότε να ξεχνάτε και πότε να ανακτάτε.

## Επόμενα Βήματα

**Επόμενη Μονάδα:** [03-rag - RAG (Αναζήτηση-Ενισχυμένη Δημιουργία)](../03-rag/README.md)

---

**Πλοήγηση:** [← Προηγούμενο: Μονάδα 01 - Εισαγωγή](../01-introduction/README.md) | [Πίσω στην Αρχική](../README.md) | [Επόμενο: Μονάδα 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Αν και επιδιώκουμε την ακρίβεια, παρακαλούμε να γνωρίζετε ότι οι αυτοματοποιημένες μεταφράσεις μπορεί να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για οποιεσδήποτε παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->