# Modul 02: Ingineria Prompturilor cu GPT-5.2

## Cuprins

- [Parcurgere Video](../../../02-prompt-engineering)
- [Ce vei învăța](../../../02-prompt-engineering)
- [Precondiții](../../../02-prompt-engineering)
- [Înțelegerea Ingineriei Prompturilor](../../../02-prompt-engineering)
- [Fundamentele Ingineriei Prompturilor](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Șabloane de Prompturi](../../../02-prompt-engineering)
- [Modele Avansate](../../../02-prompt-engineering)
- [Utilizarea Resurselor Azure Existente](../../../02-prompt-engineering)
- [Capturi de Ecran ale Aplicației](../../../02-prompt-engineering)
- [Explorarea Modelelor](../../../02-prompt-engineering)
  - [Entuziasm Scăzut vs Ridicat](../../../02-prompt-engineering)
  - [Executarea Sarcinilor (Preludii pentru Unelte)](../../../02-prompt-engineering)
  - [Cod cu Auto-Reflectare](../../../02-prompt-engineering)
  - [Analiză Structurată](../../../02-prompt-engineering)
  - [Chat Multi-Rundă](../../../02-prompt-engineering)
  - [Raționament Pas cu Pas](../../../02-prompt-engineering)
  - [Ieșire Constrânsă](../../../02-prompt-engineering)
- [Ce Înveți cu Adevărat](../../../02-prompt-engineering)
- [Pașii Următori](../../../02-prompt-engineering)

## Parcurgere Video

Urmărește această sesiune live care explică cum să începi cu acest modul:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Ce vei învăța

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="Ce vei învăța" width="800"/>

În modulul anterior, ai văzut cum memoria permite inteligența conversatională și ai folosit Modele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebările — prompturile în sine — folosind GPT-5.2 de la Azure OpenAI. Modul în care structurezi prompturile afectează dramatic calitatea răspunsurilor pe care le primești. Începem cu o revizuire a tehnicilor fundamentale de prompting, apoi trecem la opt modele avansate care valorifică pe deplin capabilitățile GPT-5.2.

Vom folosi GPT-5.2 pentru că introduce controlul raționamentului - poți să-i spui modelului cât să gândească înainte de a răspunde. Acest lucru face ca diferitele strategii de prompting să fie mai evidente și te ajută să înțelegi când să folosești fiecare abordare. Beneficiem și de limite mai puține de rată din Azure pentru GPT-5.2 comparativ cu Modelele GitHub.

## Precondiții

- Modulul 01 completat (resurse Azure OpenAI implementate)
- Fișierul `.env` în directorul rădăcină cu acreditările Azure (creat prin `azd up` în Modulul 01)

> **Notă:** Dacă nu ai terminat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea Ingineriei Prompturilor

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Ce este Ingineria Prompturilor?" width="800"/>

Ingineria prompturilor înseamnă proiectarea unui text de intrare care să-ți ofere constant rezultatele de care ai nevoie. Nu este doar despre a pune întrebări – este despre a structura cererile astfel încât modelul să înțeleagă exact ce vrei și cum să livreze.

Gândește-te la asta ca la a da instrucțiuni unui coleg. „Repară bug-ul” este vag. „Repară excepția null pointer în UserService.java la linia 45 prin adăugarea unei verificări de null” este specific. Modelele de limbaj funcționează la fel – specificitatea și structura contează.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Cum se Potrivește LangChain4j" width="800"/>

LangChain4j oferă infrastructura — conexiuni la modele, memorie și tipuri de mesaje — în timp ce modelele de prompturi sunt doar texte atent structurate pe care le trimiți prin acea infrastructură. Elementele cheie sunt `SystemMessage` (care setează comportamentul și rolul AI-ului) și `UserMessage` (care conține cererea ta efectivă).

## Fundamentele Ingineriei Prompturilor

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Prezentare Generală a Cinci Modele de Inginerie a Prompturilor" width="800"/>

Înainte să intrăm în modelele avansate din acest modul, hai să revizuim cinci tehnici fundamentale de prompting. Acestea sunt pietrele de temelie pe care orice inginer de prompturi ar trebui să le cunoască. Dacă ai parcurs deja [modulul Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — iată cadrul conceptual din spatele lor.

### Zero-Shot Prompting

Cea mai simplă abordare: dai modelului o instrucțiune directă fără exemple. Modelul se bazează în totalitate pe antrenamentul său pentru a înțelege și executa sarcina. Funcționează bine pentru solicitări directe unde comportamentul așteptat este evident.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: "Pozitiv"
```

**Când să folosești:** Clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidaj suplimentar.

### Few-Shot Prompting

Oferă exemple care demonstrează modelul pe care vrei să-l urmeze. Modelul învață formatul așteptat de intrare-ieșire din exemplele tale și îl aplică la intrări noi. Acest lucru îmbunătățește dramatic consistența pentru sarcini în care formatul sau comportamentul dorit nu sunt evidente.

<img src="../../../translated_images/ro/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Învățare din exemple — modelul identifică tiparul și îl aplică la intrări noi*

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

**Când să folosești:** Clasificări personalizate, formatare constantă, sarcini specifice domeniului sau când rezultatele zero-shot sunt inconsistente.

### Chain of Thought

Cere modelului să-și arate raționamentul pas cu pas. În loc să sară direct la un răspuns, modelul descompune problema și o parcurge pe fiecare parte explicit. Acest lucru îmbunătățește acuratețea la sarcini matematice, logice și de raționament în pași multipli.

<img src="../../../translated_images/ro/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

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

**Când să folosești:** Probleme de matematică, puzzle-uri logice, depanare sau orice sarcină unde arătarea procesului de raționare îmbunătățește acuratețea și încrederea.

### Role-Based Prompting

Setează o persoană sau un rol pentru AI înainte să-i pui întrebarea. Aceasta oferă context care modelează tonul, profunzimea și focalizarea răspunsului. Un „arhitect software” oferă sfaturi diferite față de un „dezvoltator junior” sau un „auditor de securitate”.

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

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

**Când să folosești:** Revizuiri de cod, tutoriat, analize specifice domeniului sau când ai nevoie de răspunsuri personalizate pe nivelul de expertiză sau perspectivă.

### Șabloane de Prompturi

Creează prompturi reutilizabile cu variabile. În loc să scrii un prompt nou de fiecare dată, definești un șablon o dată și completezi cu valori diferite. Clasa `PromptTemplate` din LangChain4j face asta ușor cu sintaxa `{{variable}}`.

<img src="../../../translated_images/ro/prompt-templates.14bfc37d45f1a933.webp" alt="Șabloane de Prompturi" width="800"/>

*Prompturi reutilizabile cu variabile — un șablon, multe utilizări*

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

**Când să folosești:** Interogări repetate cu intrări diferite, procesare batch, construirea de fluxuri AI reutilizabile sau orice scenariu unde structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamente îți oferă un set solid de unelte pentru majoritatea sarcinilor de prompting. Restul modulului construiește pe aceste baze cu **opt modele avansate** care valorifică controlul raționamentului, auto-evaluarea și capabilitățile de ieșire structurată ale GPT-5.2.

## Modele Avansate

Odată acoperite fundamentele, să trecem la cele opt modele avansate care fac acest modul unic. Nu toate problemele au nevoie de aceeași abordare. Unele întrebări necesită răspunsuri rapide, altele o gândire profundă. Unele cer un raționament vizibil, altele doar rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului al GPT-5.2 face diferențele și mai evidente.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Opt Modele de Prompting" width="800"/>

*Prezentare generală a celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controlul Raționamentului cu GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici cât de mult să „gândească” modelul — de la răspunsuri rapide directe până la explorări profunde*

**Entuziasm Scăzut (Rapid & Focalizat)** - Pentru întrebări simple când dorești răspunsuri rapide și directe. Modelul face raționament minim — maxim 2 pași. Folosește-l pentru calcule, căutări sau întrebări directe.

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
> - „Care este diferența dintre modelele de prompting cu entuziasm scăzut și entuziasm ridicat?”
> - „Cum ajută tag-urile XML din prompturi la structurarea răspunsului AI?”
> - „Când ar trebui să folosesc modelele de auto-reflecție față de instrucțiunea directă?”

**Entuziasm Ridicat (Profund & Amănunțit)** - Pentru probleme complexe când dorești o analiză cuprinzătoare. Modelul explorează în detaliu și arată raționamentul explicit. Folosește-l pentru proiectarea sistemelor, decizii arhitecturale sau cercetare complexă.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea Sarcinilor (Progres Pas cu Pas)** - Pentru fluxuri de lucru în mai mulți pași. Modelul oferă un plan înainte, povestește fiecare pas pe măsură ce lucrează, apoi dă un rezumat. Folosește-l pentru migrații, implementări sau orice proces în mai multe etape.

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

Prompting-ul Chain-of-Thought cere explicit modelului să-și arate procesul de raționare, îmbunătățind acuratețea pentru sarcini complexe. Descompunerea pas cu pas ajută atât oamenii, cât și AI să înțeleagă logica.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre acest model:
> - „Cum aș adapta modelul de executare a sarcinilor pentru operații de durată lungă?”
> - „Care sunt bunele practici pentru structurarea preludiilor uneltelor în aplicații de producție?”
> - „Cum pot captura și afișa actualizări intermediare de progres într-o interfață?”

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Modelul Executării Sarcinilor" width="800"/>

*Flux de lucru Planifică → Execută → Rezumă pentru sarcini în mai mulți pași*

**Cod cu Auto-Reflectare** - Pentru generarea codului de calitate pentru producție. Modelul generează cod urmând standarde de producție cu gestionare corectă a erorilor. Folosește-l când construiești funcții sau servicii noi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclul de Auto-Reflecție" width="800"/>

*Circuit iterativ de îmbunătățire – generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză Structurată** - Pentru evaluare consecventă. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate, mentenabilitate). Folosește acest model pentru recenzii de cod sau evaluări de calitate.

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
> - „Cum asigur nivele consistente de severitate în diferite sesiuni de revizuire?”

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Modelul Analizei Structurate" width="800"/>

*Cadrul pentru recenzii consecvente ale codului cu nivele de severitate*

**Chat Multi-Rundă** - Pentru conversații care necesită context. Modelul își amintește mesajele anterioare și construiește pe baza lor. Folosește-l pentru sesiuni interactive de ajutor sau întrebări complexe și răspunsuri.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ro/context-memory.dff30ad9fa78832a.webp" alt="Memoria Contextuală" width="800"/>

*Cum se acumulează contextul conversației peste mai multe runde până la limita de tokeni*

**Raționament Pas cu Pas** - Pentru probleme ce necesită logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește-l pentru probleme de matematică, puzzle-uri logice sau când ai nevoie să înțelegi procesul de gândire.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Modelul Pas cu Pas" width="800"/>

*Descompunerea problemelor în pași logici expliciți*

**Ieșire Constrânsă** - Pentru răspunsuri cu cerințe specifice de format. Modelul respectă strict regulile de format și lungime. Folosește-l pentru rezumate sau când ai nevoie de o structură precisă a ieșirii.

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

<img src="../../../translated_images/ro/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Modelul Ieșirii Constrânse" width="800"/>

*Aplicarea cerințelor specifice de format, lungime și structură*

## Utilizarea Resurselor Azure Existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în timpul Modulului 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de pornire de mai jos și să accesezi direct http://localhost:8083.
**Opțiunea 1: Utilizarea Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în Bara de activități din partea stângă a VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard, poți:
- Să vezi toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Să pornești/oprești aplicațiile cu un singur clic
- Să vizualizezi jurnalele aplicațiilor în timp real
- Să monitorizezi starea aplicațiilor

Pur și simplu fă clic pe butonul de redare de lângă „prompt-engineering” pentru a porni acest modul sau pornește toate modulele odată.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opțiunea 2: Utilizarea scripturilor shell**

Pornește toate aplicațiile web (modulele 01-04):

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

Sau pornește doar acest modul:

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` de la rădăcină și vor construi JAR-urile dacă acestea nu există.

> **Notă:** Dacă preferi să construiești manual toate modulele înainte de a le porni:
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

Accesează http://localhost:8083 în browserul tău.

**Pentru a opri:**

**Bash:**
```bash
./stop.sh  # Doar acest modul
# Sau
cd .. && ./stop-all.sh  # Toate modulele
```

**PowerShell:**
```powershell
.\stop.ps1  # Numai acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Capturi de ecran ale aplicației

<img src="../../../translated_images/ro/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Tabloul de bord principal care arată toate cele 8 tipare de inginerie a prompturilor cu caracteristicile și cazurile lor de utilizare*

## Explorarea tiparelor

Interfața web îți permite să experimentezi cu diferite strategii de promptare. Fiecare tipar rezolvă probleme diferite - încearcă-le pentru a vedea când strălucește fiecare abordare.

> **Notă: Streaming vs Non-Streaming** — Fiecare pagină a tiparului oferă două butoane: **🔴 Stream Response (Live)** și o opțiune **Non-streaming**. Streaming-ul folosește Server-Sent Events (SSE) pentru a afișa token-uri în timp real pe măsură ce modelul le generează, astfel că vezi progresul imediat. Opțiunea non-streaming așteaptă întreaga răspuns înainte de afișare. Pentru prompturi care declanșează raționamente complexe (de exemplu, High Eagerness, Self-Reflecting Code), apelul non-streaming poate dura foarte mult timp — uneori minute — fără feedback vizibil. **Folosește streaming când experimentezi cu prompturi complexe** ca să vezi modelul lucrând și să eviți impresia că cererea a expirat.
>
> **Notă: Cerință de browser** — Funcția de streaming folosește API-ul Fetch Streams (`response.body.getReader()`), care necesită un browser complet (Chrome, Edge, Firefox, Safari). Nu funcționează în Simple Browser-ul integrat în VS Code, deoarece webview-ul său nu suportă API-ul ReadableStream. Dacă folosești Simple Browser, butoanele non-streaming vor funcționa normal — doar cele de streaming sunt afectate. Deschide `http://localhost:8083` într-un browser extern pentru experiența completă.

### Eagerness scăzut vs Eagerness ridicat

Pune o întrebare simplă precum „Care este 15% din 200?” folosind Low Eagerness. Vei primi un răspuns instant și direct. Acum pune ceva complex ca „Proiectează o strategie de caching pentru un API cu trafic ridicat” folosind High Eagerness. Fă clic pe **🔴 Stream Response (Live)** și urmărește raționamentul detaliat al modelului apărând token cu token. Același model, aceeași structură a întrebării - dar promptul îi spune câtă gândire să facă.

### Executarea sarcinilor (Preambule de instrumente)

Fluxurile multi-pași beneficiază de planificare inițială și narațiune a progresului. Modelul descrie ce va face, povestește fiecare pas, apoi rezumă rezultatele.

### Cod auto-reflectiv

Încearcă „Creează un serviciu de validare a emailurilor”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează conform criteriilor de calitate, identifică punctele slabe și îmbunătățește. Vei vedea iterațiile până când codul corespunde standardelor de producție.

### Analiză structuratată

Revizuirile de cod necesită cadre de evaluare consistente. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

### Chat multi-turn

Întreabă „Ce este Spring Boot?” apoi imediat „Arată-mi un exemplu”. Modelul își amintește prima întrebare și îți dă un exemplu Spring Boot specific. Fără memorie, a doua întrebare ar fi prea vagă.

### Raționament pas cu pas

Alege o problemă de matematică și încearc-o cu ambele metode, Raționament pas cu pas și Low Eagerness. Low eagerness oferă doar răspunsul - rapid, dar opac. Raționamentul pas cu pas îți arată fiecare calcul și decizie.

### Ieșire constrânsă

Când ai nevoie de formate specifice sau număr fix de cuvinte, acest tipar impune respectarea strictă. Încearcă să generezi un rezumat cu exact 100 de cuvinte, în format de puncte.

## Ce înveți cu adevărat

**Efortul de raționament schimbă totul**

GPT-5.2 îți permite să controlezi efortul computațional prin prompturi. Efort scăzut înseamnă răspunsuri rapide, cu explorare minimă. Efort ridicat înseamnă că modelul ia timp să gândească profund. Înveți să potrivești efortul cu complexitatea sarcinii - nu pierde timp pe întrebări simple, dar nici nu grăbi decizii complexe.

**Structura ghidează comportamentul**

Observi etichetele XML în prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât textul liber. Când ai nevoie de procese multi-pași sau logică complexă, structura ajută modelul să urmărească unde este și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia unui prompt bine structurat, cu secțiuni clare și organizare în stil XML*

**Calitate prin auto-evaluare**

Tiparele auto-reflective funcționează prin explicitarea criteriilor de calitate. În loc să speri că modelul „face bine”, îi spui exact ce înseamnă „bine”: logică corectă, gestionare a erorilor, performanță, securitate. Modelul poate evalua apoi propriul output și se poate îmbunătăți. Asta transformă generarea de cod din loterie în proces.

**Contextul este finit**

Conversațiile multi-turn funcționează incluzând istoricul mesajelor la fiecare cerere. Dar există o limită - fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, vei avea nevoie de strategii ca să păstrezi contextul relevant fără să depășești acea limită. Acest modul îți arată cum funcționează memoria; mai târziu vei învăța când să rezumi, când să uiți și când să recuperezi.

## Pașii următori

**Următorul modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:  
Acest document a fost tradus utilizând serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autoritară. Pentru informații critice, este recomandată traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->