# Module 02: Μηχανική Παρακίνησης με GPT-5.2

## Πίνακας Περιεχομένων

- [Περιήγηση σε Βίντεο](../../../02-prompt-engineering)
- [Τι θα Μάθετε](../../../02-prompt-engineering)
- [Προαπαιτούμενα](../../../02-prompt-engineering)
- [Κατανόηση της Μηχανικής Παρακίνησης](../../../02-prompt-engineering)
- [Βασικά της Μηχανικής Παρακίνησης](../../../02-prompt-engineering)
  - [Zero-Shot Παρακίνηση](../../../02-prompt-engineering)
  - [Few-Shot Παρακίνηση](../../../02-prompt-engineering)
  - [Αλυσίδα Σκέψης](../../../02-prompt-engineering)
  - [Παρακίνηση με Βάση Ρόλο](../../../02-prompt-engineering)
  - [Προτύπα Παρακίνησης](../../../02-prompt-engineering)
- [Προηγμένα Πρότυπα](../../../02-prompt-engineering)
- [Χρήση Υφιστάμενων Πόρων Azure](../../../02-prompt-engineering)
- [Στιγμιότυπα Εφαρμογής](../../../02-prompt-engineering)
- [Εξερεύνηση των Προτύπων](../../../02-prompt-engineering)
  - [Χαμηλή vs Υψηλή Προθυμία](../../../02-prompt-engineering)
  - [Εκτέλεση Εργασιών (Προλόγια Εργαλείων)](../../../02-prompt-engineering)
  - [Αυτοανακλαστικός Κώδικας](../../../02-prompt-engineering)
  - [Δομημένη Ανάλυση](../../../02-prompt-engineering)
  - [Συνομιλία Πολλαπλών Γύρων](../../../02-prompt-engineering)
  - [Λογική Βήμα-Βήμα](../../../02-prompt-engineering)
  - [Περιορισμένη Έξοδος](../../../02-prompt-engineering)
- [Τι Μαθαίνετε Πραγματικά](../../../02-prompt-engineering)
- [Επόμενα Βήματα](../../../02-prompt-engineering)

## Video Walkthrough

Παρακολουθήστε αυτή τη ζωντανή συνεδρία που εξηγεί πώς να ξεκινήσετε με αυτό το module:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Μηχανική Παρακίνησης με LangChain4j - Ζωντανή Συνεδρία" width="800"/></a>

## Τι Θα Μάθετε

<img src="../../../translated_images/el/what-youll-learn.c68269ac048503b2.webp" alt="Τι Θα Μάθετε" width="800"/>

Στο προηγούμενο module, είδατε πώς η μνήμη επιτρέπει τη συνομιλητική AI και χρησιμοποιήσατε GitHub Models για βασικές αλληλεπιδράσεις. Τώρα θα εστιάσουμε στο πώς κάνετε ερωτήσεις — τα ίδια τα prompts — χρησιμοποιώντας το GPT-5.2 του Azure OpenAI. Ο τρόπος που δομείτε τα prompts επηρεάζει δραματικά την ποιότητα των απαντήσεων που λαμβάνετε. Αρχίζουμε με μια ανασκόπηση των βασικών τεχνικών παρακίνησης, και μετά προχωρούμε σε οκτώ προηγμένα πρότυπα που εκμεταλλεύονται πλήρως τις δυνατότητες του GPT-5.2.

Θα χρησιμοποιήσουμε το GPT-5.2 επειδή εισάγει έλεγχο σκέψης - μπορείτε να πείτε στο μοντέλο πόση σκέψη να κάνει πριν απαντήσει. Αυτό καθιστά τις διαφορετικές στρατηγικές παρακίνησης πιο εμφανείς και βοηθά να καταλάβετε πότε να χρησιμοποιείτε κάθε προσέγγιση. Επίσης, θα επωφεληθούμε από τα λιγότερα όρια ρυθμού του Azure για το GPT-5.2 σε σύγκριση με τα GitHub Models.

## Προαπαιτούμενα

- Ολοκληρωμένο Module 01 (πόροι Azure OpenAI αναπτυγμένοι)
- Αρχείο `.env` στον ριζικό κατάλογο με διαπιστευτήρια Azure (δημιουργήθηκε από `azd up` στο Module 01)

> **Σημείωση:** Αν δεν έχετε ολοκληρώσει το Module 01, ακολουθήστε πρώτα εκείνες τις οδηγίες ανάπτυξης.

## Κατανόηση της Μηχανικής Παρακίνησης

<img src="../../../translated_images/el/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Τι είναι η Μηχανική Παρακίνησης;" width="800"/>

Η μηχανική παρακίνησης αφορά το σχεδιασμό εισερχόμενου κειμένου που σταθερά σας δίνει τα αποτελέσματα που χρειάζεστε. Δεν είναι μόνο να κάνετε ερωτήσεις - αφορά το πώς δομείτε τις αιτήσεις ώστε το μοντέλο να καταλαβαίνει ακριβώς τι θέλετε και πώς να το παρέχει.

Σκεφτείτε το σαν να δίνετε οδηγίες σε έναν συνάδελφο. "Διόρθωσε το σφάλμα" είναι ασαφές. "Διόρθωσε το null pointer exception στο UserService.java γραμμή 45 προσθέτοντας έλεγχο null" είναι συγκεκριμένο. Τα μοντέλα γλώσσας δουλεύουν με τον ίδιο τρόπο - η ειδικότητα και η δομή έχουν σημασία.

<img src="../../../translated_images/el/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Πώς ταιριάζει το LangChain4j" width="800"/>

Το LangChain4j παρέχει την υποδομή — τις συνδέσεις μοντέλων, τη μνήμη, και τους τύπους μηνυμάτων — ενώ τα πρότυπα παρακίνησης είναι απλά προσεκτικά δομημένο κείμενο που στέλνετε μέσω αυτής της υποδομής. Τα βασικά δομικά στοιχεία είναι το `SystemMessage` (που ορίζει τη συμπεριφορά και ρόλο της AI) και το `UserMessage` (που φέρει το πραγματικό σας αίτημα).

## Βασικά της Μηχανικής Παρακίνησης

<img src="../../../translated_images/el/five-patterns-overview.160f35045ffd2a94.webp" alt="Επισκόπηση Πέντε Προτύπων Μηχανικής Παρακίνησης" width="800"/>

Πριν βουτήξουμε στα προηγμένα πρότυπα αυτού του module, ας εξετάσουμε πέντε θεμελιώδεις τεχνικές παρακίνησης. Αυτά είναι τα δομικά στοιχεία που κάθε μηχανικός παρακίνησης πρέπει να γνωρίζει. Αν έχετε ήδη περάσει από το [Quick Start module](../00-quick-start/README.md#2-prompt-patterns), τα έχετε δει σε δράση — εδώ είναι το εννοιολογικό πλαίσιο πίσω από αυτά.

### Zero-Shot Παρακίνηση

Η απλούστερη προσέγγιση: δώστε στο μοντέλο μια άμεση εντολή χωρίς παραδείγματα. Το μοντέλο βασίζεται εξ ολοκλήρου στην εκπαίδευσή του για να κατανοήσει και να εκτελέσει την εργασία. Λειτουργεί καλά για απλές αιτήσεις όπου η αναμενόμενη συμπεριφορά είναι προφανής.

<img src="../../../translated_images/el/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Παρακίνηση" width="800"/>

*Άμεση εντολή χωρίς παραδείγματα — το μοντέλο συμπεραίνει την εργασία από μόνο την εντολή*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Απόκριση: "Θετική"
```

**Πότε να χρησιμοποιείται:** Απλές ταξινομήσεις, άμεσες ερωτήσεις, μεταφράσεις ή οποιαδήποτε εργασία που το μοντέλο μπορεί να χειριστεί χωρίς επιπλέον οδηγήσεις.

### Few-Shot Παρακίνηση

Παρέχετε παραδείγματα που δείχνουν το πρότυπο που θέλετε να ακολουθήσει το μοντέλο. Το μοντέλο μαθαίνει το αναμενόμενο μορφότυπο εισόδου-εξόδου από τα παραδείγματα και το εφαρμόζει σε νέες εισόδους. Αυτό βελτιώνει δραματικά τη συνέπεια σε εργασίες όπου το επιθυμητό format ή συμπεριφορά δεν είναι προφανή.

<img src="../../../translated_images/el/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Παρακίνηση" width="800"/>

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

**Πότε να χρησιμοποιείται:** Προσαρμοσμένες ταξινομήσεις, συνεπής μορφοποίηση, εργασίες ειδικού τομέα ή όταν τα zero-shot αποτελέσματα είναι ασυνεπή.

### Αλυσίδα Σκέψης

Ζητήστε από το μοντέλο να δείξει τη λογική του βήμα-βήμα. Αντί να πηδήξει κατευθείαν σε απάντηση, το μοντέλο αναλύει το πρόβλημα και δουλεύει κάθε μέρος ρητά. Αυτό βελτιώνει την ακρίβεια σε μαθηματικά, λογική και εργασίες πολλών βημάτων.

<img src="../../../translated_images/el/chain-of-thought.5cff6630e2657e2a.webp" alt="Αλυσίδα Σκέψης Παρακίνησης" width="800"/>

*Λογική βήμα-βήμα — διαχωρισμός σύνθετων προβλημάτων σε ρητά λογικά βήματα*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Το μοντέλο δείχνει: 15 - 8 = 7, μετά 7 + 12 = 19 μήλα
```

**Πότε να χρησιμοποιείται:** Μαθηματικά προβλήματα, λογικά παιχνίδια, αποσφαλμάτωση ή οποιαδήποτε εργασία όπου η εμφάνιση της διαδικασίας σκέψης βελτιώνει την ακρίβεια και την εμπιστοσύνη.

### Παρακίνηση με Βάση Ρόλο

Ορίστε μια προσωπικότητα ή ρόλο για την AI πριν κάνετε την ερώτησή σας. Αυτό παρέχει πλαίσιο που διαμορφώνει τον τόνο, το βάθος, και την εστίαση της απάντησης. Ένας "αρχιτέκτονας λογισμικού" δίνει διαφορετικές συμβουλές από έναν "νεότερο προγραμματιστή" ή έναν "ελεγκτή ασφαλείας".

<img src="../../../translated_images/el/role-based-prompting.a806e1a73de6e3a4.webp" alt="Παρακίνηση με Βάση Ρόλο" width="800"/>

*Ορισμός πλαισίου και προσωποποιίας — η ίδια ερώτηση παίρνει διαφορετική απάντηση ανάλογα με τον καθορισμένο ρόλο*

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

**Πότε να χρησιμοποιείται:** Κριτικές κώδικα, διδασκαλία, ανάλυση ειδικών τομέων, ή όταν χρειάζεστε απαντήσεις προσαρμοσμένες σε συγκεκριμένο επίπεδο εμπειρίας ή οπτική.

### Προτύπα Παρακίνησης

Δημιουργήστε επαναχρησιμοποιούμενα prompts με μεταβλητές θέσεις. Αντί να γράφετε ένα νέο prompt κάθε φορά, ορίστε ένα πρότυπο μια φορά και γεμίστε με διαφορετικές τιμές. Η κλάση `PromptTemplate` του LangChain4j το κάνει εύκολο με τη σύνταξη `{{variable}}`.

<img src="../../../translated_images/el/prompt-templates.14bfc37d45f1a933.webp" alt="Προτύπα Παρακίνησης" width="800"/>

*Επαναχρησιμοποιούμενα prompts με μεταβλητές θέσεις — ένα πρότυπο, πολλές χρήσεις*

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

**Πότε να χρησιμοποιείται:** Επαναλαμβανόμενα ερωτήματα με διαφορετικές εισόδους, μαζική επεξεργασία, κατασκευή επαναχρησιμοποιήσιμων ροών εργασίας AI, ή οποιαδήποτε περίπτωση όπου η δομή prompt παραμένει ίδια αλλά τα δεδομένα αλλάζουν.

---

Αυτά τα πέντε θεμελιώδη σας προσφέρουν ένα σταθερό εργαλείο για τις περισσότερες εργασίες παρακίνησης. Το υπόλοιπο αυτού του module χτίζεται πάνω τους με **οκτώ προηγμένα πρότυπα** που εκμεταλλεύονται τον έλεγχο σκέψης, την αυτοαξιολόγηση και τις δυνατότητες δομημένης εξόδου του GPT-5.2.

## Προηγμένα Πρότυπα

Με τα βασικά καλυμμένα, ας προχωρήσουμε στα οκτώ προηγμένα πρότυπα που κάνουν αυτό το module μοναδικό. Όχι όλα τα προβλήματα χρειάζονται την ίδια προσέγγιση. Μερικές ερωτήσεις θέλουν γρήγορες απαντήσεις, άλλες βαθιά σκέψη. Μερικές χρειάζονται ορατή λογική, άλλες μόνο αποτελέσματα. Κάθε πρότυπο παρακάτω είναι βελτιστοποιημένο για διαφορετικό σενάριο — και ο έλεγχος σκέψης του GPT-5.2 κάνει τις διαφορές ακόμα πιο εμφανείς.

<img src="../../../translated_images/el/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Οκτώ Πρότυπα Παρακίνησης" width="800"/>

*Επισκόπηση των οκτώ προτύπων μηχανικής παρακίνησης και των περιπτώσεων χρήσης τους*

<img src="../../../translated_images/el/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Έλεγχος Σκέψης με GPT-5.2" width="800"/>

*Ο έλεγχος σκέψης του GPT-5.2 σας επιτρέπει να ορίσετε πόση σκέψη πρέπει να κάνει το μοντέλο — από γρήγορες απαντήσεις έως βαθιά εξερεύνηση*

**Χαμηλή Προθυμία (Γρήγορο & Εστιασμένο)** - Για απλές ερωτήσεις που θέλετε γρήγορες, άμεσες απαντήσεις. Το μοντέλο κάνει ελάχιστη σκέψη - μέχρι 2 βήματα. Χρησιμοποιήστε το για υπολογισμούς, αναζητήσεις ή απλές ερωτήσεις.

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

> 💡 **Εξερευνήστε με GitHub Copilot:** Ανοίξτε [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) και ρωτήστε:
> - "Ποια είναι η διαφορά μεταξύ των προτύπων χαμηλής και υψηλής προθυμίας παρακίνησης;"
> - "Πώς βοηθούν οι ετικέτες XML στα prompts στη δομή της απάντησης της AI;"
> - "Πότε πρέπει να χρησιμοποιώ μοτίβα αυτο-αντανάκλασης αντί για άμεσες εντολές;"

**Υψηλή Προθυμία (Βαθιά & Ενδελεχής)** - Για σύνθετα προβλήματα που θέλετε ολοκληρωμένη ανάλυση. Το μοντέλο εξερευνά λεπτομερώς και δείχνει αναλυτική λογική. Χρησιμοποιήστε το για σχεδιασμό συστημάτων, αρχιτεκτονικές αποφάσεις, ή σύνθετη έρευνα.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Εκτέλεση Εργασιών (Πρόοδος Βήμα-Βήμα)** - Για ροές εργασίας πολλών βημάτων. Το μοντέλο παρέχει αρχικό πλάνο, αφηγείται κάθε βήμα καθώς δουλεύει, και μετά δίνει μια σύνοψη. Χρησιμοποιήστε το για μεταφορές, υλοποιήσεις, ή οποιαδήποτε διαδικασία πολλών βημάτων.

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

Η παρακίνηση Chain-of-Thought ζητά ρητά από το μοντέλο να δείξει τη διαδικασία λογικής του, βελτιώνοντας την ακρίβεια σε σύνθετες εργασίες. Η ανάλυση βήμα-βήμα βοηθά τόσο τους ανθρώπους όσο και την AI να κατανοήσουν τη λογική.

> **🤖 Δοκιμάστε με [GitHub Copilot](https://github.com/features/copilot) Chat:** Ρωτήστε για αυτό το πρότυπο:
> - "Πώς θα προσαρμόσω το πρότυπο εκτέλεσης εργασίας για μακροχρόνιες διεργασίες;"
> - "Ποιες είναι οι βέλτιστες πρακτικές για τη δομή των προλόγων εργαλείων σε εφαρμογές παραγωγής;"
> - "Πώς μπορώ να καταγράψω και να εμφανίσω ενδιάμεσα ενημερώσεις προόδου σε UI;"

<img src="../../../translated_images/el/task-execution-pattern.9da3967750ab5c1e.webp" alt="Πρότυπο Εκτέλεσης Εργασιών" width="800"/>

*Σχεδιασμός → Εκτέλεση → Σύνοψη ροής εργασίας για εργασίες πολλών βημάτων*

**Αυτοανακλαστικός Κώδικας** - Για παραγωγή κώδικα ποιότητας. Το μοντέλο παράγει κώδικα ακολουθώντας πρότυπα παραγωγής με σωστό χειρισμό σφαλμάτων. Χρησιμοποιήστε το όταν δημιουργείτε νέες λειτουργίες ή υπηρεσίες.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/el/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Κύκλος Αυτο-Αντανάκλασης" width="800"/>

*Βρόχος επαναλαμβανόμενης βελτίωσης - δημιουργία, αξιολόγηση, εντοπισμός προβλημάτων, βελτίωση, επανάληψη*

**Δομημένη Ανάλυση** - Για συνεπή αξιολόγηση. Το μοντέλο ανασκοπεί κώδικα χρησιμοποιώντας ένα σταθερό πλαίσιο (ορθότητα, πρακτικές, απόδοση, ασφάλεια, διατηρησιμότητα). Χρησιμοποιήστε το για κριτικές κώδικα ή αξιολογήσεις ποιότητας.

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
> - "Ποιος είναι ο καλύτερος τρόπος να αναλύω και να ενεργώ βάσει δομημένης εξόδου προγραμματιστικά;"
> - "Πώς διασφαλίζω συνεπή επίπεδα σοβαρότητας σε διαφορετικές συνεδρίες ανασκόπησης;"

<img src="../../../translated_images/el/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Πρότυπο Δομημένης Ανάλυσης" width="800"/>

*Πλαίσιο για συνεπείς κριτικές κώδικα με επίπεδα σοβαρότητας*

**Συνομιλία Πολλαπλών Γύρων** - Για συνομιλίες που χρειάζονται πλαίσιο. Το μοντέλο θυμάται προηγούμενα μηνύματα και χτίζει πάνω σε αυτά. Χρησιμοποιήστε το για διαδραστικές συνεδρίες βοήθειας ή σύνθετη Q&A.

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

*Πώς συσσωρεύεται το πλαίσιο συνομιλίας σε πολλαπλούς γύρους μέχρι να φτάσει το όριο tokens*

**Λογική Βήμα-Βήμα** - Για προβλήματα που απαιτούν ορατή λογική. Το μοντέλο δείχνει ρητή λογική για κάθε βήμα. Χρησιμοποιήστε το για μαθηματικά προβλήματα, λογικούς γρίφους, ή όταν θέλετε να κατανοήσετε τη σκέψη.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/el/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Πρότυπο Βήμα-Βήμα" width="800"/>

*Διαχωρισμός προβλημάτων σε ρητά λογικά βήματα*

**Περιορισμένη Έξοδος** - Για απαντήσεις με συγκεκριμένες απαιτήσεις μορφής. Το μοντέλο ακολουθεί αυστηρά κανόνες μορφής και μήκους. Χρησιμοποιήστε το για περιλήψεις ή όταν χρειάζεστε ακριβή δομή εξόδου.

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

*Επιβολή συγκεκριμένων απαιτήσεων μορφής, μήκους και δομής*

## Χρήση Υφιστάμενων Πόρων Azure

**Επαλήθευση ανάπτυξης:**

Βεβαιωθείτε ότι υπάρχει το αρχείο `.env` στον ριζικό κατάλογο με διαπιστευτήρια Azure (δημιουργήθηκε κατά το Module 01):
```bash
cat ../.env  # Πρέπει να εμφανίσει AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Εκκίνηση της εφαρμογής:**

> **Σημείωση:** Αν έχετε ήδη ξεκινήσει όλες τις εφαρμογές χρησιμοποιώντας το `./start-all.sh` από το Module 01, αυτό το module τρέχει ήδη στην πόρτα 8083. Μπορείτε να παραλείψετε τις εντολές εκκίνησης παρακάτω και να μεταβείτε απευθείας στο http://localhost:8083.
**Επιλογή 1: Χρήση του Spring Boot Dashboard (Συνιστάται για χρήστες VS Code)**

Το δοχείο ανάπτυξης περιλαμβάνει την επέκταση Spring Boot Dashboard, η οποία παρέχει μια οπτική διεπαφή για τη διαχείριση όλων των εφαρμογών Spring Boot. Μπορείτε να το βρείτε στη Γραμμή Δραστηριοτήτων στην αριστερή πλευρά του VS Code (αναζητήστε το εικονίδιο Spring Boot).

Από το Spring Boot Dashboard, μπορείτε να:
- Δείτε όλες τις διαθέσιμες εφαρμογές Spring Boot στον χώρο εργασίας
- Ξεκινήσετε/σταματήσετε εφαρμογές με ένα κλικ
- Δείτε τα αρχεία καταγραφής των εφαρμογών σε πραγματικό χρόνο
- Παρακολουθείτε την κατάσταση της εφαρμογής

Απλώς κάντε κλικ στο κουμπί αναπαραγωγής δίπλα στο "prompt-engineering" για να ξεκινήσετε αυτό το module, ή ξεκινήστε όλα τα modules ταυτόχρονα.

<img src="../../../translated_images/el/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Επιλογή 2: Χρήση shell scripts**

Ξεκινήστε όλες τις web εφαρμογές (modules 01-04):

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

Ή ξεκινήστε μόνο αυτό το module:

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

Και τα δύο scripts φορτώνουν αυτόματα μεταβλητές περιβάλλοντος από το αρχείο `.env` στη ρίζα και θα χτίσουν τα JARs αν δεν υπάρχουν.

> **Σημείωση:** Εάν προτιμάτε να χτίσετε όλα τα modules χειροκίνητα πριν τα ξεκινήσετε:
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

**Για να σταματήσετε:**

**Bash:**
```bash
./stop.sh  # Μόνο αυτό το μονάδα
# Ή
cd .. && ./stop-all.sh  # Όλες οι μονάδες
```

**PowerShell:**
```powershell
.\stop.ps1  # Μόνο αυτό το μοντέλο
# Ή
cd ..; .\stop-all.ps1  # Όλα τα μοντέλα
```

## Στιγμιότυπα Εφαρμογών

<img src="../../../translated_images/el/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Η κύρια οθόνη ελέγχου που δείχνει τα 8 μοτίβα prompt engineering με τα χαρακτηριστικά και τις χρήσεις τους*

## Εξερεύνηση των Μοτίβων

Η web διεπαφή σας επιτρέπει να πειραματιστείτε με διαφορετικές στρατηγικές prompt. Κάθε μοτίβο λύνει διαφορετικά προβλήματα – δοκιμάστε τα για να δείτε πότε ξεχωρίζει η κάθε προσέγγιση.

> **Σημείωση: Streaming vs Μη Streaming** — Κάθε σελίδα μοτίβου προσφέρει δύο κουμπιά: **🔴 Stream Response (Live)** και μια **Μη-Streaming** επιλογή. Το streaming χρησιμοποιεί Server-Sent Events (SSE) για να δείχνει τα tokens σε πραγματικό χρόνο καθώς το μοντέλο τα δημιουργεί, ώστε να βλέπετε άμεσα την πρόοδο. Η μη-streaming επιλογή περιμένει ολόκληρη την απάντηση πριν την εμφανίσει. Για prompts που απαιτούν βαθιά σκέψη (π.χ., High Eagerness, Self-Reflecting Code), η μη-streaming κλήση μπορεί να διαρκέσει πολύ ώρα — μερικές φορές λεπτά — χωρίς ορατή ανατροφοδότηση. **Χρησιμοποιήστε streaming όταν πειραματίζεστε με πολύπλοκα prompts** ώστε να δείτε το μοντέλο να λειτουργεί και να αποφύγετε την εντύπωση ότι η αίτηση έχει λήξει.
>
> **Σημείωση: Απαίτηση Περιηγητή** — Η λειτουργία streaming χρησιμοποιεί το Fetch Streams API (`response.body.getReader()`) που απαιτεί πλήρη περιηγητή (Chrome, Edge, Firefox, Safari). Δεν λειτουργεί στον ενσωματωμένο Simple Browser του VS Code, καθώς το webview του δεν υποστηρίζει το ReadableStream API. Εάν χρησιμοποιείτε τον Simple Browser, τα κουμπιά μη-streaming θα λειτουργούν κανονικά — μόνο τα κουμπιά streaming επηρεάζονται. Ανοίξτε το `http://localhost:8083` σε εξωτερικό περιηγητή για την πλήρη εμπειρία.

### Χαμηλή vs Υψηλή Επιμονή (Low vs High Eagerness)

Κάντε μια απλή ερώτηση όπως "Τι είναι το 15% του 200;" χρησιμοποιώντας Χαμηλή Επιμονή. Θα λάβετε άμεση, απευθείας απάντηση. Τώρα ρωτήστε κάτι σύνθετο όπως "Σχεδίασε μια στρατηγική caching για ένα API με υψηλή επισκεψιμότητα" χρησιμοποιώντας Υψηλή Επιμονή. Κάντε κλικ στο **🔴 Stream Response (Live)** και δείτε τη λεπτομερή σκέψη του μοντέλου να εμφανίζεται token-ανά-token. Ίδιο μοντέλο, ίδια δομή ερώτησης – αλλά το prompt καθορίζει πόση σκέψη απαιτείται.

### Εκτέλεση Εργασιών (Tool Preambles)

Οι πολύ-βηματικές ροές εργασίας ωφελούνται από προγραμματισμό εκ των προτέρων και αφήγηση προόδου. Το μοντέλο περιγράφει τι θα κάνει, αφηγείται κάθε βήμα και συνοψίζει τα αποτελέσματα.

### Κώδικας Αυτοανάλυσης (Self-Reflecting Code)

Δοκιμάστε "Δημιουργία υπηρεσίας επαλήθευσης email". Αντί απλώς να δημιουργήσει κώδικα και να σταματήσει, το μοντέλο παράγει, αξιολογεί κριτήρια ποιότητας, εντοπίζει αδυναμίες και βελτιώνει. Θα το δείτε να επαναλαμβάνει τη διαδικασία μέχρι ο κώδικας να πληροί τα πρότυπα παραγωγής.

### Δομημένη Ανάλυση

Οι ανασκοπήσεις κώδικα απαιτούν συνεπή πλαίσια αξιολόγησης. Το μοντέλο αναλύει τον κώδικα χρησιμοποιώντας σταθερές κατηγορίες (ορθότητα, πρακτικές, απόδοση, ασφάλεια) με επίπεδα σοβαρότητας.

### Συνομιλία Πολλαπλών Γύρων (Multi-Turn Chat)

Ρωτήστε "Τι είναι το Spring Boot;" και αμέσως μετά "Δείξε μου ένα παράδειγμα". Το μοντέλο θυμάται την πρώτη ερώτηση και δίνει συγκεκριμένο παράδειγμα Spring Boot. Χωρίς μνήμη, η δεύτερη ερώτηση θα ήταν πολύ αόριστη.

### Σκέψη Βήμα-Βήμα

Επιλέξτε ένα μαθηματικό πρόβλημα και δοκιμάστε το με Σκέψη Βήμα-Βήμα και Χαμηλή Επιμονή. Η χαμηλή επιμονή δίνει μόνο την απάντηση — γρήγορα αλλά ασαφώς. Το βήμα-βήμα δείχνει όλους τους υπολογισμούς και τις αποφάσεις.

### Περιορισμένη Έξοδος

Όταν χρειάζεστε συγκεκριμένες μορφές ή αριθμούς λέξεων, αυτό το μοτίβο επιβάλλει αυστηρή τήρηση. Δοκιμάστε να δημιουργήσετε μια περίληψη με ακριβώς 100 λέξεις σε μορφή κουκκίδων.

## Τι Μαθαίνετε Πραγματικά

**Η προσπάθεια λογικής αλλάζει τα πάντα**

Το GPT-5.2 σας επιτρέπει να ελέγχετε την υπολογιστική προσπάθεια μέσω των prompts. Χαμηλή προσπάθεια σημαίνει γρήγορες απαντήσεις με ελάχιστη εξερεύνηση. Υψηλή προσπάθεια σημαίνει ότι το μοντέλο αφιερώνει χρόνο για βαθιά σκέψη. Μαθαίνετε να ταιριάζετε την προσπάθεια με την πολυπλοκότητα της εργασίας – μην χάνετε χρόνο σε απλές ερωτήσεις, αλλά μην βιάζεστε σε σύνθετες αποφάσεις.

**Η δομή καθοδηγεί τη συμπεριφορά**

Παρατηρήσατε τις ετικέτες XML στα prompts; Δεν είναι διακοσμητικές. Τα μοντέλα ακολουθούν δομημένες οδηγίες πιο αξιόπιστα από ελεύθερο κείμενο. Όταν χρειάζεστε πολύ-βηματικές διαδικασίες ή σύνθετη λογική, η δομή βοηθά το μοντέλο να παρακολουθεί πού βρίσκεται και τι ακολουθεί.

<img src="../../../translated_images/el/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Ανατομία ενός καλά δομημένου prompt με σαφή τμήματα και οργάνωση τύπου XML*

**Ποιότητα μέσω αυτοαξιολόγησης**

Τα μοτίβα αυτοανάλυσης λειτουργούν κάνοντας ρητά τα κριτήρια ποιότητας. Αντί να ελπίζετε ότι το μοντέλο "θα το κάνει σωστά", του λέτε ακριβώς τι σημαίνει "σωστά": ορθή λογική, διαχείριση σφαλμάτων, απόδοση, ασφάλεια. Το μοντέλο μπορεί τότε να αξιολογήσει τη δική του έξοδο και να βελτιωθεί. Αυτό μετατρέπει τη δημιουργία κώδικα από λόττο σε διαδικασία.

**Το πλαίσιο είναι πεπερασμένο**

Οι συνομιλίες πολλαπλών γύρων λειτουργούν περιλαμβάνοντας το ιστορικό μηνυμάτων σε κάθε αίτηση. Αλλά υπάρχει όριο - κάθε μοντέλο έχει μέγιστο αριθμό tokens. Καθώς οι συνομιλίες μεγαλώνουν, θα χρειαστείτε στρατηγικές για να κρατάτε το σχετικό πλαίσιο χωρίς να ξεπερνάτε αυτό το όριο. Το module αυτό σας δείχνει πώς λειτουργεί η μνήμη· αργότερα θα μάθετε πότε να συνοψίζετε, πότε να ξεχνάτε και πότε να ανακτάτε.

## Επόμενα Βήματα

**Επόμενο Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Πλοήγηση:** [← Προηγούμενο: Module 01 - Εισαγωγή](../01-introduction/README.md) | [Πίσω στην Αρχική](../README.md) | [Επόμενο: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθυνών**:
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας υπηρεσία αυτόματης μετάφρασης AI [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που επιδιώκουμε ακρίβεια, παρακαλούμε να λάβετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν σφάλματα ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->