# Modulul 02: Ingineria Prompturilor cu GPT-5.2

## Cuprins

- [Ce Vei Învăța](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea Ingineriei Prompturilor](../../../02-prompt-engineering)
- [Fundamentele Ingineriei Prompturilor](../../../02-prompt-engineering)
  - [Promptare Zero-Shot](../../../02-prompt-engineering)
  - [Promptare Few-Shot](../../../02-prompt-engineering)
  - [Lanț de Gândire](../../../02-prompt-engineering)
  - [Promptare Bazată pe Rol](../../../02-prompt-engineering)
  - [Șabloane pentru Prompturi](../../../02-prompt-engineering)
- [Modele Avansate](../../../02-prompt-engineering)
- [Utilizarea Resurselor Azure Existente](../../../02-prompt-engineering)
- [Capturi de Ecran ale Aplicației](../../../02-prompt-engineering)
- [Explorarea Modelelor](../../../02-prompt-engineering)
  - [Entuziasm Scăzut vs Ridicat](../../../02-prompt-engineering)
  - [Executarea Sarcinilor (Preludii ale Uneltelor)](../../../02-prompt-engineering)
  - [Cod cu Auto-Reflecție](../../../02-prompt-engineering)
  - [Analiză Structurată](../../../02-prompt-engineering)
  - [Chat Multi-Turn](../../../02-prompt-engineering)
  - [Raționament Pas cu Pas](../../../02-prompt-engineering)
  - [Output Constrâns](../../../02-prompt-engineering)
- [Ce Înveți Cu Adevărat](../../../02-prompt-engineering)
- [Pașii Următori](../../../02-prompt-engineering)

## Ce Vei Învăța

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="Ce Vei Învăța" width="800"/>

În modulul anterior, ai văzut cum memoria permite AI-ului conversațional și ai folosit Modele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebările — pe prompturile în sine — utilizând GPT-5.2 din Azure OpenAI. Modul în care îți structurezi prompturile afectează dramatic calitatea răspunsurilor pe care le primești. Începem cu o revizuire a tehnicilor fundamentale de prompting, apoi trecem la opt modele avansate care valorifică pe deplin capabilitățile GPT-5.2.

Vom folosi GPT-5.2 pentru că introduce controlul raționamentului - poți spune modelului cât să gândească înainte de a răspunde. Aceasta face ca diferitele strategii de prompting să fie mai evidente și te ajută să înțelegi când să folosești fiecare abordare. De asemenea, vom beneficia de limite de rată mai puține în Azure pentru GPT-5.2 comparativ cu Modelele GitHub.

## Prerechizite

- Modulul 01 completat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu credentiale Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai terminat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea Ingineriei Prompturilor

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Ce Este Ingineria Prompturilor?" width="800"/>

Ingineria prompturilor este despre proiectarea unui text de intrare care să-ți ofere constant rezultatele de care ai nevoie. Nu este doar despre a pune întrebări - este despre structurarea cererilor astfel încât modelul să înțeleagă exact ce vrei și cum să livreze.

Gândește-te ca și cum ai da instrucțiuni unui coleg. „Repară bugul” este vag. „Repară excepția null pointer în UserService.java linia 45 adăugând o verificare null” este specific. Modelele de limbaj funcționează la fel - specificitatea și structura contează.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Cum se Potrivește LangChain4j" width="800"/>

LangChain4j oferă infrastructura — conexiunile cu modelele, memoria și tipurile de mesaje — în timp ce modelele de prompturi sunt doar texte structurate cu grijă pe care le trimiți prin acea infrastructură. Blocurile cheie sunt `SystemMessage` (care stabilește comportamentul și rolul AI-ului) și `UserMessage` (care poartă cererea ta efectivă).

## Fundamentele Ingineriei Prompturilor

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Prezentare Generală a Cinci Modele de Inginerie a Prompturilor" width="800"/>

Înainte să intrăm în modelele avansate din acest modul, să revizuim cinci tehnici fundamentale de prompting. Acestea sunt blocurile de construcție pe care orice inginer de prompturi ar trebui să le cunoască. Dacă ai parcurs deja [modulul Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — iată cadrul conceptual din spatele lor.

### Promptare Zero-Shot

Cea mai simplă abordare: dă modelului o instrucțiune directă fără exemple. Modelul se bazează complet pe antrenamentul său pentru a înțelege și executa sarcina. Funcționează bine pentru cereri simple unde comportamentul așteptat este clar.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Promptare Zero-Shot" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina doar din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: „Pozitiv”
```

**Când să folosești:** Clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidaj suplimentar.

### Promptare Few-Shot

Oferă exemple care demonstrează tiparul pe care vrei ca modelul să-l urmeze. Modelul învață formatul input-output așteptat din exemplele tale și îl aplică pentru intrări noi. Aceasta îmbunătățește dramatic consistența pentru sarcini unde formatul sau comportamentul dorit nu sunt evidente.

<img src="../../../translated_images/ro/few-shot-prompting.9d9eace1da88989a.webp" alt="Promptare Few-Shot" width="800"/>

*Învățare din exemple — modelul identifică tiparul și îl aplică pentru inputuri noi*

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

**Când să folosești:** Clasificări personalizate, formatare consistentă, sarcini specifice domeniului, sau când rezultatele zero-shot sunt inconsistente.

### Lanț de Gândire

Rugă modelul să-și arate raționamentul pas cu pas. În loc să sară direct la un răspuns, modelul descompune problema și lucrează explicit prin fiecare parte. Aceasta îmbunătățește acuratețea pentru probleme de matematică, logică și raționamente multi-pași.

<img src="../../../translated_images/ro/chain-of-thought.5cff6630e2657e2a.webp" alt="Promptarea Lanțului de Gândire" width="800"/>

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

**Când să folosești:** Probleme matematice, puzzle-uri logice, depanare sau orice sarcină unde arătarea procesului de raționament îmbunătățește acuratețea și încrederea.

### Promptare Bazată pe Rol

Setează o persoană sau un rol pentru AI înainte de a pune întrebarea. Aceasta oferă context care modelează tonul, profunzimea și focalizarea răspunsului. Un "arhitect software" oferă sfaturi diferite față de un "dezvoltator junior" sau un "auditor de securitate".

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Promptare Bazată pe Rol" width="800"/>

*Stabilirea contextului și a persoanei — aceeași întrebare primește un răspuns diferit în funcție de rolul atribuit*

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

**Când să folosești:** Recenzii de cod, predare, analize specifice domeniului sau când ai nevoie de răspunsuri adaptate unui anumit nivel de expertiză sau perspectivă.

### Șabloane pentru Prompturi

Creează prompturi reutilizabile cu variabile reprezentate prin plasatoare. În loc să scrii un prompt nou de fiecare dată, definește un șablon o dată și completează cu valori diferite. Clasa `PromptTemplate` din LangChain4j facilitează acest lucru cu sintaxa `{{variable}}`.

<img src="../../../translated_images/ro/prompt-templates.14bfc37d45f1a933.webp" alt="Șabloane pentru Prompturi" width="800"/>

*Prompturi reutilizabile cu plasatoare variabile — un șablon, multe utilizări*

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

**Când să folosești:** Interogări repetitive cu inputuri diferite, procesare în loturi, construirea de fluxuri AI reutilizabile sau orice scenariu unde structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamentale îți oferă un set solid de unelte pentru majoritatea sarcinilor de prompting. Restul acestui modul se bazează pe ele cu **opt modele avansate** care exploatează controlul raționamentului GPT-5.2, auto-evaluarea și capacitățile de output structurat.

## Modele Avansate

Cu fundamentele acoperite, să trecem la cele opt modele avansate care fac acest modul unic. Nu toate problemele necesită aceeași abordare. Unele întrebări cer răspunsuri rapide, altele gândire profundă. Unele cer raționament vizibil, altele doar rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului din GPT-5.2 face diferențele și mai pronunțate.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Opt Modele de Promptare" width="800"/>

*Prezentare generală a celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controlul Raționamentului cu GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici câtă gândire trebuie să facă modelul — de la răspunsuri rapide și directe până la explorări profunde*

**Entuziasm Scăzut (Rapid & Concentrat)** - Pentru întrebări simple unde dorești răspunsuri rapide și directe. Modelul face un raționament minim - maxim 2 pași. Folosește asta pentru calcule, căutări sau întrebări simple.

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
> - „Cum ajută etichetele XML din prompturi la structura răspunsului AI?”
> - „Când ar trebui să folosesc modele de auto-reflecție vs instrucțiuni directe?”

**Entuziasm Ridicat (Profund & Amănunțit)** - Pentru probleme complexe unde dorești o analiză cuprinzătoare. Modelul explorează în profunzime și arată un raționament detaliat. Folosește asta pentru design de sistem, decizii arhitecturale sau cercetări complexe.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea Sarcinii (Progres Pas cu Pas)** - Pentru fluxuri multi-pași. Modelul oferă un plan inițial, povestește fiecare pas pe măsură ce lucrează, apoi oferă un sumar. Folosește asta pentru migrații, implementări sau orice proces multi-pas.

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

Promptarea Chain-of-Thought cere explicit modelului să-și arate procesul de raționament, îmbunătățind acuratețea pentru sarcini complexe. Descompunerea pas cu pas ajută atât oamenii, cât și AI să înțeleagă logica.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre acest model:
> - „Cum aș adapta modelul de executare a sarcinii pentru operațiuni de lungă durată?”
> - „Care sunt cele mai bune practici pentru structurarea preludiilor uneltelor în aplicații de producție?”
> - „Cum pot captura și afișa actualizări intermediare de progres într-o UI?”

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Model de Executare a Sarcinii" width="800"/>

*Flux de lucru Plan → Execută → Sumar pentru sarcini multi-pași*

**Cod cu Auto-Reflecție** - Pentru generarea de cod la calitate de producție. Modelul generează cod urmând standarde de producție cu tratare corespunzătoare a erorilor. Folosește asta când construiești funcții sau servicii noi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclul Auto-Reflecției" width="800"/>

*Buclă iterativă de îmbunătățire - generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză Structurată** - Pentru evaluare consistentă. Modelul revizuiește cod folosind un cadru fix (corectitudine, practici, performanță, securitate, întreținere). Folosește asta pentru revizuiri de cod sau evaluări de calitate.

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
> - „Cum pot personaliza cadrul de analiză pentru tipuri diferite de revizuiri de cod?”
> - „Care este cel mai bun mod de a parsa și acționa pe baza unui output structurat programatic?”
> - „Cum asigur niveluri consistente de severitate în diferite sesiuni de revizuire?”

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Model de Analiză Structurată" width="800"/>

*Cadru pentru revizuiri consistente de cod cu niveluri de severitate*

**Chat Multi-Turn** - Pentru conversații care au nevoie de context. Modelul își amintește mesajele anterioare și construiește pe baza lor. Folosește asta pentru sesiuni interactive de ajutor sau Q&A complexe.

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

*Cum se acumulează contextul conversației peste mai multe schimburi până la limita de tokeni*

**Raționament Pas cu Pas** - Pentru probleme care necesită logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește asta pentru probleme matematice, puzzle-uri logice sau când ai nevoie să înțelegi procesul gândirii.

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

**Output Constrâns** - Pentru răspunsuri cu cerințe specifice de format. Modelul urmează strict regulile de format și lungime. Folosește asta pentru rezumate sau când ai nevoie de o structură precisă a outputului.

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

<img src="../../../translated_images/ro/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Model de Output Constrâns" width="800"/>

*Impunerea cerințelor specifice de format, lungime și structură*

## Utilizarea Resurselor Azure Existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu credentialele Azure (creat în timpul Modulului 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de start de mai jos și să mergi direct la http://localhost:8083.

**Opțiunea 1: Folosirea Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în bara de activități din partea stângă a VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard poți:
- Vedea toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opri aplicații cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor
Pur și simplu faceți clic pe butonul de redare de lângă „prompt-engineering” pentru a începe acest modul sau porniți toate modulele odată.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opțiunea 2: Folosirea scripturilor shell**

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor construi JAR-urile dacă acestea nu există.

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
.\stop.ps1  # Numai acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Capturi de ecran ale aplicației

<img src="../../../translated_images/ro/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Panoul principal care arată toate cele 8 modele de inginerie a prompturilor cu caracteristicile și cazurile lor de utilizare*

## Explorarea modelelor

Interfața web vă permite să experimentați cu diferite strategii de prompting. Fiecare model rezolvă probleme diferite – încercați-le pentru a vedea când strălucește fiecare abordare.

> **Notă: Streaming vs Non-Streaming** — Fiecare pagină a unui model oferă două butoane: **🔴 Stream Response (Live)** și o opțiune **Non-streaming**. Streamingul folosește Server-Sent Events (SSE) pentru a afișa tokenii în timp real pe măsură ce modelul îi generează, astfel încât vedeți progresul imediat. Opțiunea non-streaming așteaptă întregul răspuns înainte de a-l afișa. Pentru prompturile care declanșează raționamente profunde (de exemplu, High Eagerness, Self-Reflecting Code), apelul non-streaming poate dura mult timp — uneori minute — fără feedback vizibil. **Folosiți streaming când experimentați cu prompturi complexe** pentru a vedea cum funcționează modelul și pentru a evita impresia că cererea a expirat.
>
> **Notă: Cerință pentru browser** — Funcția de streaming folosește Fetch Streams API (`response.body.getReader()`) care necesită un browser complet (Chrome, Edge, Firefox, Safari). Nu funcționează în Simple Browser încorporat în VS Code, deoarece webview-ul său nu suportă API-ul ReadableStream. Dacă folosiți Simple Browser, butoanele non-streaming vor funcționa în continuare normal – doar butoanele de streaming sunt afectate. Deschideți `http://localhost:8083` într-un browser extern pentru experiența completă.

### Low vs High Eagerness

Adresați o întrebare simplă, cum ar fi „Care este 15% din 200?” folosind Low Eagerness. Veți primi un răspuns instant, direct. Acum adresați ceva complex, de exemplu „Proiectați o strategie de caching pentru o API cu trafic intens” folosind High Eagerness. Faceți clic pe **🔴 Stream Response (Live)** și urmăriți raționamentul detaliat al modelului care apare token cu token. Același model, aceeași structură a întrebării – dar promptul îi spune cât de mult să gândească.

### Executarea sarcinilor (Preambule pentru instrumente)

Fluxurile de lucru multi-pas beneficiază de planificare prealabilă și de narațiune a progresului. Modelul schițează ce va face, relatează fiecare pas, apoi rezumă rezultatele.

### Cod auto-reflectorizant

Încercați „Creați un serviciu de validare a emailurilor”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează criteriile de calitate, identifică slăbiciunile și îmbunătățește. Veți vedea cum iterează până când codul îndeplinește standardele de producție.

### Analiză structurată

Revizuirile de cod necesită cadre de evaluare consistente. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

### Chat multi-tură

Întrebați „Ce este Spring Boot?” și apoi imediat întrebați „Arată-mi un exemplu”. Modelul își amintește prima întrebare și vă oferă un exemplu de Spring Boot specific. Fără memorie, a doua întrebare ar fi prea vagă.

### Raționament pas cu pas

Alegeți o problemă matematică și încercați atât cu Raționament pas cu pas, cât și cu Low Eagerness. Low eagerness vă oferă doar răspunsul – rapid, dar opac. Raționamentul pas cu pas vă arată fiecare calcul și decizie.

### Ieșire restricționată

Când aveți nevoie de formate sau număr de cuvinte specifice, acest model impune respectarea strictă. Încercați să generați un rezumat cu exact 100 de cuvinte în format punctat.

## Ce învățați cu adevărat

**Efortul de raționare schimbă totul**

GPT-5.2 vă permite să controlați efortul computațional prin prompturile dvs. Efortul scăzut înseamnă răspunsuri rapide cu explorare minimă. Efortul ridicat înseamnă că modelul își ia timp pentru a gândi profund. Învețați să potriviți efortul cu complexitatea sarcinii – nu pierdeți timp cu întrebări simple, dar nici nu grăbiți deciziile complexe.

**Structura ghidează comportamentul**

Observați etichetele XML din prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât textul liber. Când aveți nevoie de procese multi-pas sau logică complexă, structura ajută modelul să știe unde este și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare de tip XML*

**Calitatea prin auto-evaluare**

Modelele auto-reflectorizante funcționează prin explicitarea criteriilor de calitate. În loc să sperați că modelul „face bine”, îi spuneți exact ce înseamnă „bine”: logică corectă, tratarea erorilor, performanță, securitate. Modelul poate astfel să-și evalueze singur outputul și să-l îmbunătățească. Acest lucru transformă generarea de cod dintr-o loterie într-un proces.

**Contextul este finit**

Conversațiile multi-tură funcționează incluzând istoricul mesajelor cu fiecare cerere. Dar există o limită – fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, veți avea nevoie de strategii pentru a păstra context relevant fără a atinge acel plafon. Acest modul vă arată cum funcționează memoria; mai târziu veți învăța când să rezumați, când să uitați și când să recuperați.

## Pașii următori

**Următorul modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să țineți cont că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă o traducere profesională realizată de oameni. Nu ne asumăm responsabilitatea pentru eventuale neînțelegeri sau interpretări greșite ce pot decurge din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->