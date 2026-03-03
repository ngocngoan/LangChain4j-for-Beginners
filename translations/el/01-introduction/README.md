# Module 01: Ξεκινώντας με το LangChain4j

## Περιεχόμενα

- [Οδηγός Βίντεο](../../../01-introduction)
- [Τι θα μάθετε](../../../01-introduction)
- [Προαπαιτούμενα](../../../01-introduction)
- [Κατανόηση του Κεντρικού Προβλήματος](../../../01-introduction)
- [Κατανόηση των Tokens](../../../01-introduction)
- [Πώς Λειτουργεί η Μνήμη](../../../01-introduction)
- [Πώς Χρησιμοποιεί Αυτό το LangChain4j](../../../01-introduction)
- [Ανάπτυξη Υποδομής Azure OpenAI](../../../01-introduction)
- [Εκτέλεση της Εφαρμογής Τοπικά](../../../01-introduction)
- [Χρήση της Εφαρμογής](../../../01-introduction)
  - [Chat χωρίς Κατάσταση (Αριστερό Πάνελ)](../../../01-introduction)
  - [Chat με Κατάσταση (Δεξί Πάνελ)](../../../01-introduction)
- [Επόμενα Βήματα](../../../01-introduction)

## Οδηγός Βίντεο

Παρακολουθήστε αυτή τη ζωντανή παρουσίαση που εξηγεί πώς να ξεκινήσετε με αυτό το module:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Τι θα μάθετε

Στο γρήγορο ξεκίνημα, χρησιμοποιήσατε τα GitHub Models για να στέλνετε προτροπές, να καλείτε εργαλεία, να δημιουργείτε ένα RAG pipeline και να δοκιμάζετε τους κανόνες ασφαλείας. Αυτά τα demos έδειξαν τι είναι δυνατό — τώρα μεταβαίνουμε στο Azure OpenAI και GPT-5.2 και αρχίζουμε να χτίζουμε εφαρμογές παραγωγικού τύπου. Αυτό το module εστιάζει σε συστήματα συνομιλίας AI που θυμούνται το πλαίσιο και διατηρούν κατάσταση — τις έννοιες που τα demos στο γρήγορο ξεκίνημα χρησιμοποιούσαν παρασκηνιακά αλλά δεν εξήγησαν.

Θα χρησιμοποιήσουμε το GPT-5.2 της Azure OpenAI σε όλο τον οδηγό επειδή οι προηγμένες δυνατότητες λογικής του κάνουν πιο εμφανή τη συμπεριφορά των διαφορετικών προτύπων. Όταν προσθέσετε μνήμη, θα δείτε καθαρά τη διαφορά. Αυτό καθιστά πιο εύκολο να κατανοήσετε τι φέρνει το κάθε συστατικό στην εφαρμογή σας.

Θα δημιουργήσετε μία εφαρμογή που επιδεικνύει και τα δύο πρότυπα:

**Chat χωρίς Κατάσταση** - Κάθε αίτημα είναι ανεξάρτητο. Το μοντέλο δεν θυμάται προηγούμενα μηνύματα. Αυτό είναι το πρότυπο που χρησιμοποιήσατε στο γρήγορο ξεκίνημα.

**Chat με Κατάσταση** - Κάθε αίτημα περιλαμβάνει το ιστορικό συνομιλίας. Το μοντέλο διατηρεί το πλαίσιο σε πολλαπλούς γύρους συνομιλίας. Αυτό είναι που απαιτούν οι παραγωγικές εφαρμογές.

## Προαπαιτούμενα

- Συνδρομή Azure με πρόσβαση στο Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Σημείωση:** Το Java, Maven, Azure CLI και Azure Developer CLI (azd) είναι προεγκατεστημένα στο παρεχόμενο devcontainer.

> **Σημείωση:** Αυτό το module χρησιμοποιεί το GPT-5.2 στο Azure OpenAI. Η ανάπτυξη διαμορφώνεται αυτόματα μέσω του `azd up` - μην τροποποιείτε το όνομα του μοντέλου στον κώδικα.

## Κατανόηση του Κεντρικού Προβλήματος

Τα γλωσσικά μοντέλα είναι χωρίς κατάσταση. Κάθε κλήση API είναι ανεξάρτητη. Αν στείλετε "Το όνομά μου είναι Ιωάννης" και μετά ρωτήσετε "Ποιο είναι το όνομά μου;", το μοντέλο δεν έχει ιδέα ότι μόλις συστηθήκατε. Αντιμετωπίζει κάθε αίτημα σαν να είναι η πρώτη συνομιλία που έχετε ποτέ.

Αυτό είναι εντάξει για απλές ερωτήσεις-απαντήσεις αλλά άχρηστο για πραγματικές εφαρμογές. Τα bots υποστήριξης πελατών πρέπει να θυμούνται τι τους είπατε. Οι προσωπικοί βοηθοί χρειάζονται πλαίσιο. Κάθε συνομιλία με πολλούς γύρους απαιτεί μνήμη.

Το παρακάτω διάγραμμα αντιπαραβάλλει τις δύο προσεγγίσεις — αριστερά, μια κλήση χωρίς κατάσταση που ξεχνά το όνομά σας· δεξιά, μια κλήση με κατάσταση υποστηριζόμενη από το ChatMemory που το θυμάται.

<img src="../../../translated_images/el/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Η διαφορά μεταξύ συνομιλιών χωρίς κατάσταση (ανεξάρτητες κλήσεις) και με κατάσταση (με επίγνωση πλαισίου)*

## Κατανόηση των Tokens

Πριν βουτήξετε σε συνομιλίες, είναι σημαντικό να κατανοήσετε τα tokens - τις βασικές μονάδες κειμένου που επεξεργάζονται τα γλωσσικά μοντέλα:

<img src="../../../translated_images/el/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Παράδειγμα πώς το κείμενο χωρίζεται σε tokens - "I love AI!" γίνεται 4 ξεχωριστές μονάδες επεξεργασίας*

Τα tokens είναι ο τρόπος με τον οποίο τα μοντέλα AI μετρούν και επεξεργάζονται το κείμενο. Λέξεις, σημεία στίξης και ακόμη και κενά μπορεί να είναι tokens. Το μοντέλο σας έχει όριο στο πόσα tokens μπορεί να επεξεργαστεί ταυτόχρονα (400.000 για το GPT-5.2, με έως 272.000 tokens εισόδου και 128.000 tokens εξόδου). Η κατανόηση των tokens βοηθάει στη διαχείριση του μήκους της συνομιλίας και των κόστους.

## Πώς Λειτουργεί η Μνήμη

Η μνήμη συνομιλίας επιλύει το πρόβλημα χωρίς κατάσταση διατηρώντας το ιστορικό της συνομιλίας. Πριν στείλετε το αίτημά σας στο μοντέλο, το πλαίσιο προσθέτει τα σχετικά προηγούμενα μηνύματα. Όταν ρωτάτε "Ποιο είναι το όνομά μου;", το σύστημα στέλνει όλο το ιστορικό συνομιλίας, επιτρέποντας στο μοντέλο να δει ότι είπατε προηγουμένως "Το όνομά μου είναι Ιωάννης."

Το LangChain4j παρέχει υλοποιήσεις μνήμης που χειρίζονται αυτό αυτόματα. Επιλέγετε πόσα μηνύματα θέλετε να κρατάτε και το πλαίσιο διαχειρίζεται το παράθυρο πλαισίου. Το παρακάτω διάγραμμα δείχνει πώς το MessageWindowChatMemory διατηρεί ένα ολισθαίνων παράθυρο πρόσφατων μηνυμάτων.

<img src="../../../translated_images/el/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*Το MessageWindowChatMemory διατηρεί ένα ολισθαίνων παράθυρο πρόσφατων μηνυμάτων, απομακρύνοντας αυτόματα τα παλιά*

## Πώς Χρησιμοποιεί Αυτό το LangChain4j

Αυτό το module επεκτείνει το γρήγορο ξεκίνημα ενσωματώνοντας το Spring Boot και προσθέτοντας μνήμη συνομιλίας. Δείτε πώς συνδέονται τα κομμάτια:

**Εξαρτήσεις** - Προσθέστε δύο βιβλιοθήκες LangChain4j:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat Model** - Διαμορφώστε το Azure OpenAI ως Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Ο builder διαβάζει τα διαπιστευτήρια από μεταβλητές περιβάλλοντος που ρυθμίζονται από το `azd up`. Η ρύθμιση `baseUrl` στο endpoint του Azure κάνει τον OpenAI client να λειτουργεί με το Azure OpenAI.

**Μνήμη Συνομιλίας** - Παρακολουθήστε το ιστορικό συνομιλίας με MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Δημιουργήστε τη μνήμη με `withMaxMessages(10)` για να κρατήσει τα τελευταία 10 μηνύματα. Προσθέστε μηνύματα χρήστη και AI με typed wrappers: `UserMessage.from(text)` και `AiMessage.from(text)`. Ανακτήστε το ιστορικό με `memory.messages()` και στείλτε το στο μοντέλο. Η υπηρεσία αποθηκεύει ξεχωριστές εμφανίσεις μνήμης ανά ID συνομιλίας, επιτρέποντας σε πολλούς χρήστες να συνομιλούν ταυτόχρονα.

> **🤖 Δοκιμάστε με το [GitHub Copilot](https://github.com/features/copilot) Chat:** Ανοίξτε το [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) και ρωτήστε:
> - "Πώς αποφασίζει το MessageWindowChatMemory ποια μηνύματα θα απορρίψει όταν το παράθυρο είναι γεμάτο;"
> - "Μπορώ να υλοποιήσω αποθήκευση μνήμης με βάση δεδομένων αντί για μνήμη RAM;"
> - "Πώς θα πρόσθετα περίληψη για συμπίεση του παλιού ιστορικού συνομιλίας;"

Το endpoint του chat χωρίς κατάσταση παραλείπει τη μνήμη εντελώς - απλά `chatModel.chat(prompt)` όπως στο γρήγορο ξεκίνημα. Το endpoint με κατάσταση προσθέτει τα μηνύματα στη μνήμη, ανακτά το ιστορικό και συμπεριλαμβάνει αυτό το πλαίσιο με κάθε αίτημα. Ίδια διαμόρφωση μοντέλου, διαφορετικά πρότυπα.

## Ανάπτυξη Υποδομής Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Επιλέξτε συνδρομή και τοποθεσία (προτείνεται eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Επιλέξτε συνδρομή και τοποθεσία (προτείνεται eastus2)
```

> **Σημείωση:** Αν αντιμετωπίσετε σφάλμα χρονικού ορίου (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), απλά εκτελέστε ξανά `azd up`. Οι πόροι Azure μπορεί να εξακολουθούν να αναπτύσσονται στο παρασκήνιο και η επανάληψη επιτρέπει την ολοκλήρωση της ανάπτυξης μόλις οι πόροι φτάσουν σε τελικό στάδιο.

Αυτό θα:
1. Αναπτύξει πόρο Azure OpenAI με τα μοντέλα GPT-5.2 και text-embedding-3-small
2. Δημιουργήσει αυτόματα το αρχείο `.env` στη ρίζα του project με διαπιστευτήρια
3. Ρυθμίσει όλες τις απαιτούμενες μεταβλητές περιβάλλοντος

**Έχετε προβλήματα ανάπτυξης;** Δείτε το [Infrastructure README](infra/README.md) για λεπτομερή αντιμετώπιση προβλημάτων, περιλαμβάνοντας συγκρούσεις ονομάτων υποτομέα, βήματα χειροκίνητης ανάπτυξης στο Azure Portal, και οδηγίες διαμόρφωσης μοντέλων.

**Επαληθεύστε ότι η ανάπτυξη πέτυχε:**

**Bash:**
```bash
cat ../.env  # Πρέπει να εμφανίζει AZURE_OPENAI_ENDPOINT, API_KEY, κ.λπ.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Πρέπει να εμφανίζει το AZURE_OPENAI_ENDPOINT, το API_KEY, κ.λπ.
```

> **Σημείωση:** Η εντολή `azd up` δημιουργεί αυτόματα το αρχείο `.env`. Αν χρειαστεί να το ενημερώσετε αργότερα, μπορείτε είτε να επεξεργαστείτε το `.env` χειροκίνητα είτε να το αναδημιουργήσετε εκτελώντας:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Εκτέλεση της Εφαρμογής Τοπικά

**Επαληθεύστε την ανάπτυξη:**

Βεβαιωθείτε ότι το αρχείο `.env` υπάρχει στη ρίζα με τα διαπιστευτήρια Azure. Εκτελέστε αυτό από τον φάκελο του module (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Πρέπει να εμφανίζει το AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Πρέπει να εμφανίζει AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Εκκίνηση των εφαρμογών:**

**Επιλογή 1: Χρήση του Spring Boot Dashboard (Συνιστάται για χρήστες VS Code)**

Το dev container περιλαμβάνει την επέκταση Spring Boot Dashboard, που παρέχει οπτικό περιβάλλον διαχείρισης όλων των εφαρμογών Spring Boot. Μπορείτε να τη βρείτε στη Λωρίδα Εργασιών αριστερά στο VS Code (αντιστοιχεί στο εικονίδιο Spring Boot).

Από το Spring Boot Dashboard μπορείτε να:
- δείτε όλες τις διαθέσιμες εφαρμογές Spring Boot στο workspace
- ξεκινήσετε/σταματήσετε εφαρμογές με ένα κλικ
- δείτε logs εφαρμογών σε πραγματικό χρόνο
- παρακολουθήσετε την κατάσταση των εφαρμογών

Απλά πατήστε το κουμπί play δίπλα στο "introduction" για να ξεκινήσετε αυτό το module, ή εκκινήστε όλα τα modules ταυτόχρονα.

<img src="../../../translated_images/el/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Το Spring Boot Dashboard στο VS Code — ξεκινήστε, σταματήστε και παρακολουθήστε όλα τα modules από ένα μέρος*

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Και τα δύο scripts φορτώνουν αυτόματα τις μεταβλητές περιβάλλοντος από το αρχείο `.env` στη ρίζα και θα χτίσουν τα JARs αν δεν υπάρχουν.

> **Σημείωση:** Αν προτιμάτε να χτίσετε όλα τα modules χειροκίνητα πριν την εκκίνηση:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Ανοίξτε http://localhost:8080 στον περιηγητή σας.

**Για να σταματήσετε:**

**Bash:**
```bash
./stop.sh  # Μόνο αυτό το μοντέλο
# Ή
cd .. && ./stop-all.sh  # Όλα τα μοντέλα
```

**PowerShell:**
```powershell
.\stop.ps1  # Μόνο αυτό το μοντέλο
# Ή
cd ..; .\stop-all.ps1  # Όλα τα μοντέλα
```

## Χρήση της Εφαρμογής

Η εφαρμογή παρέχει μια web διεπαφή με δύο υλοποιήσεις chat δίπλα-δίπλα.

<img src="../../../translated_images/el/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Πίνακας ελέγχου που δείχνει τις επιλογές Simple Chat (χωρίς κατάσταση) και Conversational Chat (με κατάσταση)*

### Chat χωρίς Κατάσταση (Αριστερό Πάνελ)

Δοκιμάστε πρώτα αυτό. Πείτε "Το όνομά μου είναι Ιωάννης" και έπειτα αμέσως ρωτήστε "Ποιο είναι το όνομά μου;" Το μοντέλο δεν θα θυμάται γιατί κάθε μήνυμα είναι ανεξάρτητο. Αυτό επιδεικνύει το βασικό πρόβλημα με την ενσωμάτωση γλωσσικών μοντέλων — κανένα πλαίσιο συνομιλίας.

<img src="../../../translated_images/el/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Το AI δεν θυμάται το όνομά σας από το προηγούμενο μήνυμα*

### Chat με Κατάσταση (Δεξί Πάνελ)

Τώρα δοκιμάστε την ίδια αλληλουχία εδώ. Πείτε "Το όνομά μου είναι Ιωάννης" και μετά "Ποιο είναι το όνομά μου;" Αυτή τη φορά θυμάται. Η διαφορά είναι το MessageWindowChatMemory - διατηρεί το ιστορικό συνομιλίας και το συμπεριλαμβάνει σε κάθε αίτημα. Έτσι δουλεύει η παραγωγική AI συνομιλίας.

<img src="../../../translated_images/el/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Το AI θυμάται το όνομά σας από νωρίτερα στη συνομιλία*

Και τα δύο πάνελ χρησιμοποιούν το ίδιο μοντέλο GPT-5.2. Η μόνη διαφορά είναι η μνήμη. Αυτό καθιστά σαφές τι φέρνει η μνήμη στην εφαρμογή σας και γιατί είναι απαραίτητη για πραγματικές περιπτώσεις χρήσης.

## Επόμενα Βήματα

**Επόμενο Module:** [02-prompt-engineering - Μηχανική Προτροπών με GPT-5.2](../02-prompt-engineering/README.md)

---

**Πλοήγηση:** [← Προηγούμενο: Module 00 - Γρήγορο Ξεκίνημα](../00-quick-start/README.md) | [Πίσω στην κεντρική σελίδα](../README.md) | [Επόμενο: Module 02 - Μηχανική Προτροπών →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία μετάφρασης με τεχνητή νοημοσύνη [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που καταβάλλουμε προσπάθειες για ακρίβεια, παρακαλούμε να λάβετε υπόψη ότι οι αυτοματοποιημένες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη γλώσσα του πρέπει να θεωρείται ως η αυθεντική πηγή. Για σημαντικές πληροφορίες συνιστάται επαγγελματική μετάφραση από ανθρώπινο μεταφραστή. Δεν φέρουμε ευθύνη για τυχόν παρανοήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->