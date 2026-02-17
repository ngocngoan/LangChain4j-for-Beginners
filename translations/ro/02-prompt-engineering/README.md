# Modului 02: Ingineria Prompturilor cu GPT-5.2

## Cuprins

- [Ce Vei Învața](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea Ingineriei Prompturilor](../../../02-prompt-engineering)
- [Fundamentele Ingineriei Prompturilor](../../../02-prompt-engineering)
  - [Promptare Zero-Shot](../../../02-prompt-engineering)
  - [Promptare Few-Shot](../../../02-prompt-engineering)
  - [Lanț de Gândire](../../../02-prompt-engineering)
  - [Promptare Bazată pe Rol](../../../02-prompt-engineering)
  - [Șabloane de Prompturi](../../../02-prompt-engineering)
- [Modele Avansate](../../../02-prompt-engineering)
- [Utilizarea Resurselor Azure Existente](../../../02-prompt-engineering)
- [Capturi de Ecran ale Aplicației](../../../02-prompt-engineering)
- [Explorarea Modelelor](../../../02-prompt-engineering)
  - [Dorință Scăzută vs Ridicată](../../../02-prompt-engineering)
  - [Executarea Sarcinilor (Preludii pentru Unelte)](../../../02-prompt-engineering)
  - [Cod Auto-Reflectiv](../../../02-prompt-engineering)
  - [Analiză Structurată](../../../02-prompt-engineering)
  - [Chat Multi-Tură](../../../02-prompt-engineering)
  - [Raționament Pas cu Pas](../../../02-prompt-engineering)
  - [Ieșire Restricționată](../../../02-prompt-engineering)
- [Ce Învățați cu Adevărat](../../../02-prompt-engineering)
- [Pașii Următori](../../../02-prompt-engineering)

## Ce Vei Învața

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="Ce Vei Învața" width="800"/>

În modulul anterior, ai văzut cum memoria permite AI conversațional și ai folosit Modelele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebări — prompturile în sine — folosind GPT-5.2 al Azure OpenAI. Modul în care îți structurezi prompturile afectează dramatic calitatea răspunsurilor primite. Începem cu o revizuire a tehnicilor fundamentale de promptare, apoi trecem la opt modele avansate care exploatează pe deplin capabilitățile GPT-5.2.

Vom folosi GPT-5.2 deoarece introduce controlul raționamentului — poți spune modelului cât să gândească înainte de a răspunde. Aceasta face ca diferite strategii de promptare să fie mai evidente și te ajută să înțelegi când să folosești fiecare abordare. Vom beneficia și de limite de rată mai puține pe Azure pentru GPT-5.2 comparativ cu Modelele GitHub.

## Prerechizite

- Modulul 01 finalizat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea Ingineriei Prompturilor

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Ce este Ingineria Prompturilor?" width="800"/>

Ingineria prompturilor înseamnă proiectarea textului de intrare care îți oferă constant rezultatele de care ai nevoie. Nu este doar despre a pune întrebări – este despre structurarea cererilor astfel încât modelul să înțeleagă exact ce dorești și cum să livreze.

Gândește-te la asta ca și cum ai da indicații unui coleg. „Rezolvă bug-ul” este vag. „Rezolvă excepția null pointer în UserService.java linia 45 adăugând o verificare nulă” este specific. Modelele de limbaj funcționează la fel — specificitatea și structura contează.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Cum se Potrivește LangChain4j" width="800"/>

LangChain4j oferă infrastructura — conexiunile cu modelul, memoria și tipurile de mesaje — în timp ce modelele de prompt sunt doar text structurat atent pe care îl trimiți prin acea infrastructură. Piesele de bază sunt `SystemMessage` (care setează comportamentul și rolul AI-ului) și `UserMessage` (care conține cererea ta efectivă).

## Fundamentele Ingineriei Prompturilor

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Prezentare Generală a Cinci Modele de Inginerie a Prompturilor" width="800"/>

Înainte de a intra în modelele avansate din acest modul, să revizuim cinci tehnici fundamentale de promptare. Acestea sunt cărămizile de bază pe care fiecare inginer de prompturi ar trebui să le cunoască. Dacă ai parcurs deja [modulul Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — iată cadrul conceptual din spatele lor.

### Promptare Zero-Shot

Abordarea cea mai simplă: oferă modelului o instrucțiune directă fără exemple. Modelul se bazează complet pe antrenamentul său pentru a înțelege și executa sarcina. Funcționează bine pentru cereri simple unde comportamentul așteptat este evident.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Promptare Zero-Shot" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina doar din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: "Pozitiv"
```

**Când să folosești:** Clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidare suplimentară.

### Promptare Few-Shot

Oferă exemple care demonstrează modelului tiparul pe care vrei să îl urmeze. Modelul învață formatul de intrare-ieșire așteptat din exemplele tale și îl aplică la noi intrări. Aceasta îmbunătățește dramatic consistența pentru sarcini unde formatul sau comportamentul dorit nu sunt evidente.

<img src="../../../translated_images/ro/few-shot-prompting.9d9eace1da88989a.webp" alt="Promptare Few-Shot" width="800"/>

*Învățare din exemple — modelul identifică tiparul și îl aplică noilor intrări*

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

**Când să folosești:** Clasificări personalizate, formatări consistente, sarcini specifice domeniului sau când rezultatele zero-shot sunt inconsistente.

### Lanț de Gândire

Cere modelului să își arate raționamentul pas cu pas. În loc sări direct la un răspuns, modelul descompune problema și parcurge fiecare parte explicit. Aceasta îmbunătățește acuratețea la sarcini de matematică, logică și raționament în mai mulți pași.

<img src="../../../translated_images/ro/chain-of-thought.5cff6630e2657e2a.webp" alt="Promptare Lanț de Gândire" width="800"/>

*Raționament pas cu pas — descompunerea problemelor complexe în pași logici expliciți*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Modelul arată: 15 - 8 = 7, apoi 7 + 12 = 19 mere
```

**Când să folosești:** Probleme de matematică, puzzle-uri logice, depanare sau orice sarcină unde arătarea procesului de raționament crește acuratețea și încrederea.

### Promptare Bazată pe Rol

Stabilește o persoană sau un rol pentru AI înainte de a pune întrebarea. Aceasta oferă context care modelează tonul, profunzimea și focalizarea răspunsului. Un „arhitect software” oferă sfaturi diferite față de un „dezvoltator junior” sau un „auditor de securitate”.

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Promptare Bazată pe Rol" width="800"/>

*Setarea contextului și a persoanei — aceeași întrebare primește răspuns diferit în funcție de rolul atribuit*

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

**Când să folosești:** Revizuiri de cod, tutorat, analiză specifică domeniului sau când ai nevoie de răspunsuri adaptate unui anumit nivel de expertiză sau perspectivă.

### Șabloane de Prompturi

Creează prompturi reutilizabile cu variabile de tip placeholder. În loc să scrii un prompt nou de fiecare dată, definești un șablon o dată și completezi cu valori diferite. Clasa `PromptTemplate` din LangChain4j face asta ușor cu sintaxa `{{variable}}`.

<img src="../../../translated_images/ro/prompt-templates.14bfc37d45f1a933.webp" alt="Șabloane de Prompturi" width="800"/>

*Prompturi reutilizabile cu variabile placeholder — un șablon, multe utilizări*

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

**Când să folosești:** Interogări repetate cu intrări diferite, procesare în loturi, construire de fluxuri AI reutilizabile sau orice scenariu unde structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamente îți oferă un set solid de instrumente pentru majoritatea sarcinilor de promptare. Restul modulului construiește pe ele cu **opt modele avansate** care exploatează controlul raționamentului, autoevaluarea și capabilitățile de ieșire structurată ale GPT-5.2.

## Modele Avansate

Cu fundamentele acoperite, să trecem la cele opt modele avansate care fac acest modul unic. Nu toate problemele au nevoie de aceeași abordare. Unele întrebări cer răspunsuri rapide, altele necesită gândire profundă. Unele cer raționament vizibil, altele doar rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului GPT-5.2 face diferențele și mai pronunțate.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Opt Modele de Promptare" width="800"/>

*Prezentare generală a celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controlul Raționamentului cu GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici câtă gândire să facă modelul — de la răspunsuri rapide și directe, la explorări profunde*

<img src="../../../translated_images/ro/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Compararea Efortului de Raționament" width="800"/>

*Dorință scăzută (rapid, direct) vs dorință ridicată (temeinic, explorator) abordări de raționament*

**Dorință Scăzută (Rapid & Focusat)** - Pentru întrebări simple unde vrei răspunsuri rapide și directe. Modelul face un raționament minim — maximum 2 pași. Folosește-l pentru calcule, căutări sau întrebări directe.

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

> 💡 **Explorează cu GitHub Copilot:** Deschide [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) și întreabă:
> - „Care este diferența dintre modelele de promptare cu dorință scăzută și cu dorință ridicată?”
> - „Cum ajută tag-urile XML din prompturi la structurarea răspunsului AI?”
> - „Când ar trebui să folosesc modelele de auto-reflecție vs instrucțiunile directe?”

**Dorință Ridicată (Profund & Temeinic)** - Pentru probleme complexe unde vrei o analiză completă. Modelul explorează temeinic și arată raționamentul detaliat. Folosește-l pentru designul sistemelor, decizii de arhitectură sau cercetare complexă.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea Sarcinii (Progres Pas cu Pas)** - Pentru fluxuri multi-pas. Modelul oferă un plan inițial, povestește fiecare pas pe măsură ce lucrează, apoi oferă un rezumat. Folosește-l pentru migrații, implementări sau orice proces multi-pas.

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

Promptarea Lanț-ului-de-Gândire cere explicit modelului să-și arate procesul de raționament, crescând acuratețea pentru sarcini complexe. Descompunerea pas cu pas ajută atât oamenii, cât și AI-ul să înțeleagă logica.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre acest model:
> - „Cum aș adapta modelul de execuție a sarcinii pentru operațiuni care durează mult?”
> - „Care sunt cele mai bune practici pentru structurarea preludiilor uneltelor în aplicații de producție?”
> - „Cum pot capta și afișa actualizări intermediare de progres într-o interfață UI?”

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Model de Executare a Sarcinii" width="800"/>

*Flux de lucru: Planifică → Execută → Rezumă pentru sarcini multi-pas*

**Cod Auto-Reflectiv** - Pentru generarea de cod de calitate de producție. Modelul generează cod urmând standardele de producție cu gestionare corectă a erorilor. Folosește-l când construiești funcționalități sau servicii noi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclul de Auto-Reflecție" width="800"/>

*Bucle iterative de îmbunătățire - generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză Structurată** - Pentru evaluări consistente. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate, mentenabilitate). Folosește-l pentru revizuiri de cod sau evaluări de calitate.

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

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre analiza structurată:
> - „Cum pot personaliza cadrul de analiză pentru diferite tipuri de revizuiri de cod?”
> - „Care este cea mai bună metodă de a parsa și acționa asupra ieșirii structurate programatic?”
> - „Cum asigur niveluri consistente de severitate între diferite sesiuni de revizuire?”

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Model de Analiză Structurată" width="800"/>

*Cadrul pentru revizuiri consistente de cod cu niveluri de severitate*

**Chat Multi-Tură** - Pentru conversații care au nevoie de context. Modelul își amintește mesajele anterioare și le construiește. Folosește-l pentru sesiuni interactive de ajutor sau Q&A complexe.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ro/context-memory.dff30ad9fa78832a.webp" alt="Memoria Contextului" width="800"/>

*Cum se acumulează contextul conversației pe mai multe runde până la atingerea limitei de tokeni*

**Raționament Pas cu Pas** - Pentru probleme care necesită logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește-l pentru probleme de matematică, puzzle-uri logice sau când trebuie să înțelegi procesul de gândire.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Model Pas cu Pas" width="800"/>

*Descompunerea problemelor în pași logici expliciți*

**Ieșire Restricționată** - Pentru răspunsuri cu cerințe specifice de format. Modelul respectă strict formatul și regulile privind lungimea. Folosește-l pentru rezumate sau când ai nevoie de o structură precisă a ieșirii.

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

<img src="../../../translated_images/ro/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Model de Ieșire Restricționată" width="800"/>

*Impunerea cerințelor specifice de format, lungime și structură*

## Utilizarea Resurselor Azure Existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în timpul Modulului 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de pornire de mai jos și să accesezi direct http://localhost:8083.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru a gestiona toate aplicațiile Spring Boot. O vei găsi în bara de activități din partea stângă a VS Code (căută pictograma Spring Boot).
Din Spring Boot Dashboard, puteți:
- Vedea toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opr i aplicațiile cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor

Pur și simplu faceți clic pe butonul de redare de lângă "prompt-engineering" pentru a porni acest modul sau porniți toate modulele odată.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opțiunea 2: Folosind scripturi shell**

Porniți toate aplicațiile web (modulele 01-04):

**Bash:**
```bash
cd ..  # Din directorul rădăcină
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Din directorul rădăcină
.\start-all.ps1
```

Sau porniți doar acest modul:

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` de la rădăcină și vor construi JAR-urile dacă nu există.

> **Notă:** Dacă preferați să construiți manual toate modulele înainte de a porni:
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

Deschideți http://localhost:8083 în browserul dvs.

**Pentru a opri:**

**Bash:**
```bash
./stop.sh  # Doar acest modul
# Sau
cd .. && ./stop-all.sh  # Toate modulele
```

**PowerShell:**
```powershell
.\stop.ps1  # Doar acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Capturi de ecran ale aplicației

<img src="../../../translated_images/ro/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Dashboard-ul principal care afișează toate cele 8 tipare de inginerie a prompturilor cu caracteristicile și cazurile lor de utilizare*

## Explorarea tiparelor

Interfața web vă permite să experimentați cu diferite strategii de prompting. Fiecare tipar rezolvă probleme diferite - încercați-le pentru a vedea când strălucește fiecare abordare.

### Entuziasm scăzut vs ridicat

Adresați o întrebare simplă precum „Care este 15% din 200?” folosind Entuziasm scăzut. Veți primi un răspuns instant, direct. Acum întrebați ceva complex cum ar fi „Proiectați o strategie de caching pentru o API cu trafic intens” folosind Entuziasm ridicat. Urmăriți cum modelul încetinește și oferă un raționament detaliat. Același model, aceeași structură a întrebării - dar promptul îi spune cât să gândească.

<img src="../../../translated_images/ro/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Calcul rapid cu raționament minim*

<img src="../../../translated_images/ro/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategie de caching cuprinzătoare (2.8MB)*

### Executarea sarcinilor (Preambule pentru unelte)

Fluxurile de lucru în mai mulți pași beneficiază de o planificare preliminară și narațiune a progresului. Modelul descrie ce va face, povestește fiecare pas, apoi rezumă rezultatele.

<img src="../../../translated_images/ro/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Crearea unui endpoint REST cu narațiune pas cu pas (3.9MB)*

### Cod auto-reflectiv

Încercați „Creați un serviciu de validare a email-urilor”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează conform criteriilor de calitate, identifică punctele slabe și îmbunătățește. Veți vedea cum iterează până când codul îndeplinește standardele de producție.

<img src="../../../translated_images/ro/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Serviciu complet de validare email (5.2MB)*

### Analiză structurată

Reviziile de cod necesită cadre de evaluare consistente. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

<img src="../../../translated_images/ro/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revizuire de cod bazată pe un cadru*

### Conversație Multi-Turn

Întrebați „Ce este Spring Boot?” apoi imediat urmați cu „Arată-mi un exemplu”. Modelul își amintește prima întrebare și vă oferă un exemplu specific Spring Boot. Fără memorie, a doua întrebare ar fi fost prea vagă.

<img src="../../../translated_images/ro/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Păstrarea contextului între întrebări*

### Raționament pas cu pas

Alegeți o problemă de matematică și încercați-o atât cu Raționament pas cu pas cât și cu Entuziasm scăzut. Entuziasmul scăzut vă oferă doar răspunsul - rapid dar opac. Raționamentul pas cu pas vă arată fiecare calcul și decizie.

<img src="../../../translated_images/ro/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema matematică cu pași expliciți*

### Ieșire constrânsă

Atunci când aveți nevoie de formate specifice sau număr de cuvinte, acest tipar impune respectarea strictă. Încercați să generați un rezumat cu exact 100 de cuvinte în format punctat.

<img src="../../../translated_images/ro/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Rezumat de învățare automată cu control al formatului*

## Ce învățați cu adevărat

**Efortul de raționament schimbă totul**

GPT-5.2 vă permite să controlați efortul computațional prin prompturile dvs. Efortul scăzut înseamnă răspunsuri rapide cu explorare minimă. Efortul ridicat înseamnă că modelul ia timp să gândească profund. Învățați să potriviți efortul cu complexitatea sarcinii - să nu pierdeți vremea pe întrebări simple, dar să nu vă grăbiți deciziile complexe.

**Structura ghidează comportamentul**

Observați etichetele XML din prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât textul liber. Când aveți nevoie de procese în mai mulți pași sau logică complexă, structura ajută modelul să țină evidența unde se află și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare în stil XML*

**Calitatea prin auto-evaluare**

Tiparele auto-reflective funcționează făcând criteriile de calitate explicite. În loc să sperați că modelul „face bine”, îi spuneți exact ce înseamnă „bine”: logică corectă, gestionarea erorilor, performanță, securitate. Modelul poate evalua astfel propria ieșire și se poate îmbunătăți. Astfel generarea codului devine un proces, nu o loterie.

**Contextul este finit**

Conversațiile multi-turn funcționează prin includerea istoricului mesajelor cu fiecare cerere. Dar există o limită - fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, veți avea nevoie de strategii pentru a păstra context relevant fără a atinge limita. Acest modul vă arată cum funcționează memoria; mai târziu veți învăța când să rezumați, când să uitați și când să recuperați.

## Pașii următori

**Următorul modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere automată AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automatizate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea în urma utilizării acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->