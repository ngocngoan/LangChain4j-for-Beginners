# Υποδομή Azure για LangChain4j - Ξεκινώντας

## Περιεχόμενα

- [Προαπαιτούμενα](../../../../01-introduction/infra)
- [Αρχιτεκτονική](../../../../01-introduction/infra)
- [Δημιουργημένοι Πόροι](../../../../01-introduction/infra)
- [Γρήγορη Εκκίνηση](../../../../01-introduction/infra)
- [Διαμόρφωση](../../../../01-introduction/infra)
- [Εντολές Διαχείρισης](../../../../01-introduction/infra)
- [Βελτιστοποίηση Κόστους](../../../../01-introduction/infra)
- [Παρακολούθηση](../../../../01-introduction/infra)
- [Αντιμετώπιση Προβλημάτων](../../../../01-introduction/infra)
- [Ενημέρωση Υποδομής](../../../../01-introduction/infra)
- [Καθαρισμός](../../../../01-introduction/infra)
- [Δομή Αρχείων](../../../../01-introduction/infra)
- [Συστάσεις Ασφαλείας](../../../../01-introduction/infra)
- [Πρόσθετοι Πόροι](../../../../01-introduction/infra)

Αυτός ο κατάλογος περιέχει την υποδομή Azure ως κώδικα (IaC) χρησιμοποιώντας Bicep και Azure Developer CLI (azd) για την ανάπτυξη πόρων Azure OpenAI.

## Προαπαιτούμενα

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (έκδοση 2.50.0 ή νεότερη)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (έκδοση 1.5.0 ή νεότερη)
- Συνδρομή Azure με δικαιώματα για δημιουργία πόρων

## Αρχιτεκτονική

**Απλοποιημένη Ρύθμιση Τοπικής Ανάπτυξης** - Αναπτύξτε μόνο Azure OpenAI, τρέξτε όλες τις εφαρμογές τοπικά.

Η υποδομή αναπτύσσει τους ακόλουθους πόρους Azure:

### Υπηρεσίες Τεχνητής Νοημοσύνης
- **Azure OpenAI**: Γνωστικές υπηρεσίες με δύο αναπτύξεις μοντέλων:
  - **gpt-5.2**: Μοντέλο ολοκλήρωσης συνομιλίας (ικανότητα 20K TPM)
  - **text-embedding-3-small**: Μοντέλο ενσωμάτωσης για RAG (ικανότητα 20K TPM)

### Τοπική Ανάπτυξη
Όλες οι εφαρμογές Spring Boot τρέχουν τοπικά στον υπολογιστή σας:
- 01-introduction (θύρα 8080)
- 02-prompt-engineering (θύρα 8083)
- 03-rag (θύρα 8081)
- 04-tools (θύρα 8084)

## Δημιουργημένοι Πόροι

| Τύπος Πόρου | Πρότυπο Ονόματος Πόρου | Σκοπός |
|--------------|----------------------|---------|
| Ομάδα Πόρων | `rg-{environmentName}` | Περιέχει όλους τους πόρους |
| Azure OpenAI | `aoai-{resourceToken}` | Φιλοξενία μοντέλου AI |

> **Σημείωση:** Το `{resourceToken}` είναι μια μοναδική συμβολοσειρά που παράγεται από το αναγνωριστικό συνδρομής, το όνομα περιβάλλοντος και την τοποθεσία

## Γρήγορη Εκκίνηση

### 1. Ανάπτυξη Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

Όταν ζητηθεί:
- Επιλέξτε τη συνδρομή Azure
- Επιλέξτε μια τοποθεσία (συνιστάται: `eastus2` για διαθεσιμότητα GPT-5.2)
- Επιβεβαιώστε το όνομα περιβάλλοντος (προεπιλογή: `langchain4j-dev`)

Αυτό θα δημιουργήσει:
- Πόρο Azure OpenAI με GPT-5.2 και text-embedding-3-small
- Λεπτομέρειες σύνδεσης στην έξοδο

### 2. Λήψη Λεπτομερειών Σύνδεσης

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Αυτά εμφανίζονται:
- `AZURE_OPENAI_ENDPOINT`: URL τερματικού σημείου Azure OpenAI
- `AZURE_OPENAI_KEY`: Κλειδί API για αυθεντικοποίηση
- `AZURE_OPENAI_DEPLOYMENT`: Όνομα μοντέλου συνομιλίας (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Όνομα μοντέλου ενσωμάτωσης

### 3. Εκτέλεση Εφαρμογών Τοπικά

Η εντολή `azd up` δημιουργεί αυτόματα ένα αρχείο `.env` στον βασικό κατάλογο με όλες τις απαραίτητες μεταβλητές περιβάλλοντος.

**Συνιστάται:** Ξεκινήστε όλες τις web εφαρμογές:

**Bash:**
```bash
# Από τον ριζικό κατάλογο
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Από τον ριζικό κατάλογο
cd ../..
.\start-all.ps1
```

Ή ξεκινήστε ένα μόνο module:

**Bash:**
```bash
# Παράδειγμα: Εκκινήστε μόνο το εισαγωγικό μονάδα
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Παράδειγμα: Ξεκινήστε μόνο το εισαγωγικό μάθημα
cd ../01-introduction
.\start.ps1
```

Και τα δύο σενάρια φορτώνουν αυτόματα τις μεταβλητές περιβάλλοντος από το αρχείο `.env` στον ρίζα που δημιουργεί η `azd up`.

## Διαμόρφωση

### Προσαρμογή Αναπτύξεων Μοντέλων

Για αλλαγή των αναπτύξεων μοντέλων, επεξεργαστείτε το `infra/main.bicep` και τροποποιήστε την παράμετρο `openAiDeployments`:

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

Διαθέσιμα μοντέλα και εκδόσεις: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Αλλαγή Περιοχών Azure

Για ανάπτυξη σε διαφορετική περιοχή, επεξεργαστείτε το `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Ελέγξτε διαθεσιμότητα GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Για ενημέρωση της υποδομής μετά από αλλαγές στα αρχεία Bicep:

**Bash:**
```bash
# Ανακατασκευή του προτύπου ARM
az bicep build --file infra/main.bicep

# Προεπισκόπηση αλλαγών
azd provision --preview

# Εφαρμογή αλλαγών
azd provision
```

**PowerShell:**
```powershell
# Ανακατασκευή του προτύπου ARM
az bicep build --file infra/main.bicep

# Προεπισκόπηση αλλαγών
azd provision --preview

# Εφαρμογή αλλαγών
azd provision
```

## Καθαρισμός

Για διαγραφή όλων των πόρων:

**Bash:**
```bash
# Διαγραφή όλων των πόρων
azd down

# Διαγραφή όλων, συμπεριλαμβανομένου του περιβάλλοντος
azd down --purge
```

**PowerShell:**
```powershell
# Διαγραφή όλων των πόρων
azd down

# Διαγραφή όλων, συμπεριλαμβανομένου του περιβάλλοντος
azd down --purge
```

**Προειδοποίηση**: Αυτό θα διαγράψει οριστικά όλους τους πόρους Azure.

## Δομή Αρχείων

## Βελτιστοποίηση Κόστους

### Ανάπτυξη/Δοκιμή
Για περιβάλλοντα ανάπτυξης/δοκιμών, μπορείτε να μειώσετε το κόστος:
- Χρήση του Standard tier (S0) για Azure OpenAI
- Ορισμός χαμηλότερης ικανότητας (10K TPM αντί για 20K) στο `infra/core/ai/cognitiveservices.bicep`
- Διαγραφή πόρων όταν δεν χρησιμοποιούνται: `azd down`

### Παραγωγή
Για την παραγωγή:
- Αύξηση ικανότητας OpenAI βάσει χρήσης (50K+ TPM)
- Ενεργοποίηση ζωνικής πλεονασματικότητας για υψηλότερη διαθεσιμότητα
- Εφαρμογή κατάλληλης παρακολούθησης και ειδοποιήσεων κόστους

### Εκτίμηση Κόστους
- Azure OpenAI: Πληρωμή ανά token (εισόδου + εξόδου)
- GPT-5.2: περίπου $3-5 ανά 1 εκ. tokens (ελέγξτε τις τρέχουσες τιμές)
- text-embedding-3-small: περίπου $0.02 ανά 1 εκ. tokens

Υπολογιστής τιμής: https://azure.microsoft.com/pricing/calculator/

## Παρακολούθηση

### Προβολή Μετρικών Azure OpenAI

Μεταβείτε στο Azure Portal → τον πόρο OpenAI σας → Μετρικές:
- Χρήση με βάση τα tokens
- Ρυθμός αιτήσεων HTTP
- Χρόνος απόκρισης
- Ενεργά tokens

## Αντιμετώπιση Προβλημάτων

### Πρόβλημα: Σύγκρουση ονόματος υποτομέα Azure OpenAI

**Μήνυμα σφάλματος:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Αιτία:**
Το όνομα υποτομέα που παράγεται από τη συνδρομή/περιβάλλον σας ήδη χρησιμοποιείται, πιθανώς από προηγούμενη ανάπτυξη που δεν καθαρίστηκε πλήρως.

**Λύση:**
1. **Επιλογή 1 - Χρήση διαφορετικού ονόματος περιβάλλοντος:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **Επιλογή 2 - Χειροκίνητη ανάπτυξη μέσω Azure Portal:**
   - Μεταβείτε στο Azure Portal → Δημιουργία πόρου → Azure OpenAI
   - Επιλέξτε ένα μοναδικό όνομα για τον πόρο σας
   - Αναπτύξτε τα παρακάτω μοντέλα:
     - **GPT-5.2**
     - **text-embedding-3-small** (για RAG modules)
   - **Σημαντικό:** Σημειώστε τα ονόματα αναπτύξεων - πρέπει να ταιριάζουν με τη διαμόρφωση `.env`
   - Μετά την ανάπτυξη, αποκτήστε το τερματικό σας και το κλειδί API από "Κλειδιά και Τερματικό"
   - Δημιουργήστε αρχείο `.env` στη ρίζα του έργου με:
     
     **Παράδειγμα αρχείου `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Οδηγίες ονομασίας αναπτύξεων μοντέλου:**
- Χρησιμοποιείτε απλά, συνεπή ονόματα: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Τα ονόματα αναπτύξεων πρέπει να αντιστοιχούν ακριβώς σε αυτά που ορίζετε στο `.env`
- Συχνό λάθος: Δημιουργία μοντέλου με ένα όνομα αλλά αναφορά διαφορετικού ονόματος στον κώδικα

### Πρόβλημα: GPT-5.2 μη διαθέσιμο στην επιλεγμένη περιοχή

**Λύση:**
- Επιλέξτε περιοχή με πρόσβαση σε GPT-5.2 (π.χ., eastus2)
- Ελέγξτε τη διαθεσιμότητα: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Πρόβλημα: Ανεπαρκές όριο για ανάπτυξη

**Λύση:**
1. Ζητήστε αύξηση ορίου στο Azure Portal
2. Ή χρησιμοποιήστε μικρότερη ικανότητα στο `main.bicep` (π.χ., capacity: 10)

### Πρόβλημα: "Πόρος μη βρέθηκε" κατά το τοπικό τρέξιμο

**Λύση:**
1. Επιβεβαιώστε την ανάπτυξη: `azd env get-values`
2. Ελέγξτε ότι το τερματικό και το κλειδί είναι σωστά
3. Βεβαιωθείτε ότι η ομάδα πόρων υπάρχει στο Azure Portal

### Πρόβλημα: Αποτυχία αυθεντικοποίησης

**Λύση:**
- Επιβεβαιώστε ότι το `AZURE_OPENAI_API_KEY` έχει οριστεί σωστά
- Η μορφή του κλειδιού πρέπει να είναι 32-ψήφια δεκαεξαδική συμβολοσειρά
- Λάβετε νέο κλειδί από το Azure Portal αν χρειάζεται

### Αποτυχία Ανάπτυξης

**Πρόβλημα**: Η εντολή `azd provision` αποτυγχάνει με λάθη ορίου ή ικανότητας

**Λύση**: 
1. Δοκιμάστε διαφορετική περιοχή - δείτε το τμήμα [Αλλαγή Περιοχών Azure](../../../../01-introduction/infra) για οδηγίες
2. Ελέγξτε αν η συνδρομή σας έχει όριο Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Η Εφαρμογή Δεν Συνδέεται

**Πρόβλημα**: Η Java εφαρμογή εμφανίζει σφάλματα σύνδεσης

**Λύση**:
1. Επιβεβαιώστε ότι οι μεταβλητές περιβάλλοντος έχουν εξαχθεί:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. Ελέγξτε σωστή μορφή τερματικού (πρέπει να είναι `https://xxx.openai.azure.com`)
3. Επιβεβαιώστε ότι το κλειδί API είναι το πρωτεύον ή δευτερεύον από το Azure Portal

**Πρόβλημα**: 401 Μη εξουσιοδοτημένο από το Azure OpenAI

**Λύση**:
1. Λάβετε νέο κλειδί API από Azure Portal → Κλειδιά και Τερματικό
2. Εξάγετε ξανά τη μεταβλητή περιβάλλοντος `AZURE_OPENAI_API_KEY`
3. Βεβαιωθείτε ότι οι αναπτύξεις μοντέλων έχουν ολοκληρωθεί (ελέγξτε το Azure Portal)

### Προβλήματα Απόδοσης

**Πρόβλημα**: Αργές χρόνοι απόκρισης

**Λύση**:
1. Ελέγξτε την κατανάλωση token και τυχόν throttling στις μετρικές του Azure Portal
2. Αυξήστε την ικανότητα TPM αν φτάνετε όρια
3. Σκεφτείτε να χρησιμοποιήσετε επίπεδο προσπάθειας συλλογισμού υψηλότερο (χαμηλό/μεσαίο/υψηλό)

## Ενημέρωση Υποδομής

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## Συστάσεις Ασφαλείας

1. **Ποτέ μην κάνετε commit τα κλειδιά API** - Χρησιμοποιήστε μεταβλητές περιβάλλοντος
2. **Χρησιμοποιήστε αρχεία .env τοπικά** - Προσθέστε το `.env` στο `.gitignore`
3. **Ανανεώνετε τα κλειδιά τακτικά** - Δημιουργείτε νέα κλειδιά στο Azure Portal
4. **Περιορίστε την πρόσβαση** - Χρησιμοποιήστε Azure RBAC για έλεγχο πρόσβασης στους πόρους
5. **Παρακολουθείτε τη χρήση** - Ρυθμίστε ειδοποιήσεις κόστους στο Azure Portal

## Πρόσθετοι Πόροι

- [Τεκμηρίωση Υπηρεσίας Azure OpenAI](https://learn.microsoft.com/azure/ai-services/openai/)
- [Τεκμηρίωση Μοντέλου GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Τεκμηρίωση Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Τεκμηρίωση Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Επίσημη Ενσωμάτωση LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Υποστήριξη

Για προβλήματα:
1. Ελέγξτε το [τμήμα αντιμετώπισης προβλημάτων](../../../../01-introduction/infra) παραπάνω
2. Επισκοπήστε την κατάσταση υπηρεσίας Azure OpenAI στο Azure Portal
3. Δημιουργήστε ένα ζήτημα στο αποθετήριο

## Άδεια Χρήσης

Δείτε το αρχείο [LICENSE](../../../../LICENSE) στην ρίζα για λεπτομέρειες.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση Ευθυνών**:  
Το παρόν έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Παρά τις προσπάθειές μας για ακρίβεια, παρακαλούμε να λάβετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα πρέπει να θεωρείται ως η αναγνωρισμένη πηγή. Για κρίσιμες πληροφορίες συνιστάται επαγγελματική ανθρωπογενής μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->