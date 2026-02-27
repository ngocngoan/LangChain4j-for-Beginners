# Modulul 02: Ingineria Prompterelor cu GPT-5.2

## Cuprins

- [Prezentare video](../../../02-prompt-engineering)
- [Ce vei învăța](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea ingineriei prompterelor](../../../02-prompt-engineering)
- [Fundamentele ingineriei prompterelor](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Șabloane de prompturi](../../../02-prompt-engineering)
- [Modele avansate](../../../02-prompt-engineering)
- [Folosește resurse Azure existente](../../../02-prompt-engineering)
- [Capturi de ecran ale aplicației](../../../02-prompt-engineering)
- [Explorarea modelelor](../../../02-prompt-engineering)
  - [Entuziasm scăzut vs. entuziasm ridicat](../../../02-prompt-engineering)
  - [Executarea sarcinii (Preambule pentru unelte)](../../../02-prompt-engineering)
  - [Cod auto-reflectorizant](../../../02-prompt-engineering)
  - [Analiză structurată](../../../02-prompt-engineering)
  - [Chat multi-turn](../../../02-prompt-engineering)
  - [Raționament pas cu pas](../../../02-prompt-engineering)
  - [Output restricționat](../../../02-prompt-engineering)
- [Ce înveți cu adevărat](../../../02-prompt-engineering)
- [Pașii următori](../../../02-prompt-engineering)

## Prezentare video

Urmărește această sesiune live care explică cum să începi cu acest modul: [Ingineria promtperelor cu LangChain4j - Sesiune live](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Ce vei învăța

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

În modulul anterior, ai văzut cum memoria susține AI conversațional și ai folosit Modele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebări — prompterele în sine — folosind GPT-5.2 de la Azure OpenAI. Felul în care structurezi prompturile afectează dramatic calitatea răspunsurilor pe care le primești. Începem cu o revizuire a tehnicilor fundamentale de prompting, apoi trecem la opt modele avansate care valorifică pe deplin capacitățile GPT-5.2.

Vom folosi GPT-5.2 deoarece introduce controlul raționamentului - poți spune modelului cât să gândească înainte de a răspunde. Aceasta face ca diferitele strategii de prompting să fie mai evidente și te ajută să înțelegi când să folosești fiecare abordare. De asemenea, vom beneficia de limite de rată mai mici în Azure pentru GPT-5.2 comparativ cu Modelele GitHub.

## Prerechizite

- Modulul 01 finalizat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea ingineriei prompterelor

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ingineria prompterelor înseamnă să proiectezi textul de intrare astfel încât să obții constant rezultatele de care ai nevoie. Nu este doar despre a pune întrebări — este vorba despre structurarea cererilor pentru ca modelul să înțeleagă exact ce dorești și cum să livreze.

Gândește-te ca și cum ai da instrucțiuni unui coleg. „Remediază bugul” este vag. „Remediază excepția de pointer nul în UserService.java linia 45 adăugând un control pentru nul” este specific. Modelele de limbaj funcționează la fel — specificitatea și structura contează.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j asigură infrastructura — conexiunile către modele, memoria și tipurile de mesaje — în timp ce modelele de prompturi sunt doar texte atent structurate pe care le trimiți prin acea infrastructură. Elementele de bază sunt `SystemMessage` (care stabilește comportamentul și rolul AI) și `UserMessage` (care conține cererea ta efectivă).

## Fundamentele ingineriei prompterelor

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Înainte de a intra în modelele avansate din acest modul, să revizuim cinci tehnici fundamentale de prompting. Acestea sunt cărămizile de bază pe care fiecare inginer de prompturi ar trebui să le cunoască. Dacă ai lucrat deja prin [modulul Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — aici este cadrul conceptual din spatele lor.

### Zero-Shot Prompting

Cea mai simplă abordare: dai modelului o instrucțiune directă fără exemple. Modelul se bazează complet pe antrenamentul său pentru a înțelege și executa sarcina. Funcționează bine pentru cereri simple unde comportamentul așteptat este evident.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina doar din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: "Pozitiv"
```

**Când să folosești:** clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidaj suplimentar.

### Few-Shot Prompting

Oferă exemple care demonstrează tiparul pe care vrei să îl urmeze modelul. Modelul învață formatul așteptat intrare-ieșire din exemplele tale și îl aplică pentru intrări noi. Aceasta îmbunătățește dramatic consistența pentru sarcini unde formatul sau comportamentul dorit nu este evident.

<img src="../../../translated_images/ro/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Învățare din exemple — modelul identifică tiparul și îl aplică la noi intrări*

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

**Când să folosești:** clasificări personalizate, formatare consistentă, sarcini specifice domeniului, sau când rezultatele zero-shot sunt inconsistente.

### Chain of Thought

Rugă modelul să-și arate raționamentul pas cu pas. În loc să sară direct la un răspuns, modelul descompune problema și lucrează explicit fiecare parte. Aceasta îmbunătățește acuratețea la probleme de matematică, logică și raționament în pași multipli.

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

**Când să folosești:** probleme de matematică, puzzle-uri logice, depanare, sau orice sarcină unde afișarea procesului de raționament îmbunătățește acuratețea și încrederea.

### Role-Based Prompting

Stabilește o persoană sau un rol pentru AI înainte să pui întrebarea. Aceasta oferă context care modelează tonul, profunzimea și focalizarea răspunsului. Un „arhitect software” oferă sfaturi diferite față de un „dezvoltator junior” sau un „auditor de securitate”.

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Stabilirea contextului și a rolului — aceeași întrebare primește răspuns diferit în funcție de rolul atribuit*

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

**Când să folosești:** revizii de cod, mentorat, analiză specifică domeniului, sau când ai nevoie de răspunsuri adaptate unui anumit nivel de expertiză sau perspectivă.

### Șabloane de prompturi

Creează prompturi reutilizabile cu înlocuitori de variabile. În loc să scrii un prompt nou de fiecare dată, definești un șablon o dată și completezi valori diferite. Clasa `PromptTemplate` din LangChain4j facilitează acest lucru folosind sintaxa `{{variable}}`.

<img src="../../../translated_images/ro/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompturi reutilizabile cu înlocuitori de variabile — un șablon, multe utilizări*

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

**Când să folosești:** interogări repetate cu intrări diferite, procesare în lot, construirea fluxurilor AI reutilizabile sau orice scenariu în care structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamentale îți oferă un kit solid pentru majoritatea sarcinilor de prompting. Restul modulului adaugă **opt modele avansate** care valorifică controlul raționamentului de la GPT-5.2, autoevaluarea și capacitățile de output structurat.

## Modele avansate

Cu fundamentele acoperite, să trecem la cele opt modele avansate care fac acest modul unic. Nu toate problemele necesită aceeași abordare. Unele întrebări cer răspunsuri rapide, altele gândire profundă. Unele au nevoie de raționament vizibil, altele doar de rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului din GPT-5.2 accentuează diferențele.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prezentare generală a celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici cât de mult să gândească modelul — de la răspunsuri rapide și directe la explorări profunde*

**Entuziasm scăzut (Rapid și focalizat)** - Pentru întrebări simple unde vrei răspunsuri rapide, directe. Modelul face raționament minim — maxim 2 pași. Folosește-l pentru calcule, căutări sau întrebări fără complicații.

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
> - "Care este diferența dintre modelele de prompting cu entuziasm scăzut și entuziasm ridicat?"
> - "Cum ajută etichetele XML din prompturi la structurarea răspunsului AI?"
> - "Când ar trebui să folosesc modele de auto-reflecție versus instrucțiuni directe?"

**Entuziasm ridicat (Profund și riguros)** - Pentru probleme complexe unde dorești o analiză cuprinzătoare. Modelul explorează temeinic și afișează raționament detaliat. Utilizează-l pentru proiectarea sistemelor, decizii arhitecturale sau cercetări complexe.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea sarcinii (Progres pas cu pas)** - Pentru fluxuri de lucru în mai mulți pași. Modelul oferă un plan preliminar, relatează fiecare pas pe măsură ce îl execută și dă un rezumat. Folosește-l pentru migrații, implementări sau orice proces cu mai mulți pași.

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

Promptul Chain-of-Thought solicită modelului să arate explicit procesul de raționament, îmbunătățind acuratețea pentru sarcini complexe. Dezvoltarea pas cu pas ajută atât oamenii, cât și AI să înțeleagă logica.

> **🤖 Încearcă cu chat-ul [GitHub Copilot](https://github.com/features/copilot):** Întreabă despre acest model:
> - "Cum aș adapta modelul de executare a sarcinii pentru operații îndelungate?"
> - "Care sunt cele mai bune practici pentru structurarea preambulului uneltelor în aplicații de producție?"
> - "Cum pot captura și afișa actualizări intermediare de progres într-o interfață utilizator?"

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Executare → Rezumat pentru sarcini în mai mulți pași*

**Cod auto-reflectorizant** - Pentru generarea de cod de calitate de producție. Modelul generează cod conform standardelor de producție cu tratarea corespunzătoare a erorilor. Folosește-l când construiești funcționalități sau servicii noi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Circuit iterativ de îmbunătățire - generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză structurată** - Pentru evaluare consistentă. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate, mentenabilitate). Folosește-l pentru revizii de cod sau evaluări de calitate.

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

> **🤖 Încearcă cu chat-ul [GitHub Copilot](https://github.com/features/copilot):** Întreabă despre analiza structurată:
> - "Cum pot personaliza cadrul de analiză pentru diferite tipuri de revizii de cod?"
> - "Care este cea mai bună metodă de a analiza și acționa pe baza unui output structurat programatic?"
> - "Cum asigur niveluri consistente de severitate în diverse sesiuni de revizie?"

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Cadrul pentru revizii consistente ale codului cu nivele de severitate*

**Chat multi-turn** - Pentru conversații care necesită context. Modelul reține mesajele anterioare și construiește pe baza lor. Utilizează-l pentru sesiuni interactive de ajutor sau Q&A complexe.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ro/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Cum se acumulează contextul conversației pe mai multe tururi până la limita de tokeni*

**Raționament pas cu pas** - Pentru probleme care cer logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește-l pentru probleme de matematică, puzzle-uri logice sau când trebuie să înțelegi procesul de gândire.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Descompunerea problemelor în pași logici expliciți*

**Output restricționat** - Pentru răspunsuri cu cerințe specifice de format. Modelul respectă strict regulile de format și lungime. Folosește-l pentru rezumate sau când ai nevoie de o structură precisă a outputului.

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

<img src="../../../translated_images/ro/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Impunerea cerințelor specifice de format, lungime și structură*

## Folosește resurse Azure existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în Modulul 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de pornire de mai jos și să accesezi direct http://localhost:8083.

**Opțiunea 1: Folosește Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**
Containătorul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în Bara de Activități din partea stângă a VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard, poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opri aplicațiile cu un singur click
- Vizualiza jurnalele aplicației în timp real
- Monitoriza starea aplicației

Apasă pur și simplu butonul de redare lângă „prompt-engineering” pentru a porni acest modul, sau pornește toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opțiunea 2: Folosind scripturi shell**

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` de la rădăcină și vor construi fișierele JAR dacă acestea nu există.

> **Notă:** Dacă preferi să construiești manual toate modulele înainte de a porni:
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

Deschide http://localhost:8083 în browserul tău.

**Pentru oprire:**

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

*Panoul principal de control care afișează toate cele 8 modele de prompt engineering cu caracteristicile și cazurile lor de utilizare*

## Explorarea modelelor

Interfața web îți permite să experimentezi diferite strategii de promptare. Fiecare model rezolvă probleme diferite – încearcă-le pentru a vedea când strălucește fiecare abordare.

> **Notă: Streaming vs Non-Streaming** — Fiecare pagină de model oferă două butoane: **🔴 Stream Response (Live)** și o opțiune **Non-streaming**. Streaming folosește Server-Sent Events (SSE) pentru a afișa tokenii în timp real pe măsură ce modelul îi generează, astfel vezi progresul imediat. Opțiunea non-streaming așteaptă întregul răspuns înainte de afișare. Pentru prompturi care declanșează un raționament aprofundat (de ex. High Eagerness, Self-Reflecting Code), apelul non-streaming poate dura foarte mult – uneori minute – fără feedback vizibil. **Folosește streaming când experimentezi cu prompturi complexe** pentru a vedea modelul în acțiune și pentru a evita impresia că cererea a expirat.
>
> **Notă: Cerință Browser** — Funcția de streaming folosește Fetch Streams API (`response.body.getReader()`) care necesită un browser complet (Chrome, Edge, Firefox, Safari). Nu funcționează în Simple Browser încorporat în VS Code, deoarece webview-ul său nu suportă ReadableStream API. Dacă folosești Simple Browser, butoanele non-streaming vor funcționa normal — doar streamingul este afectat. Deschide `http://localhost:8083` într-un browser extern pentru experiența completă.

### Low vs High Eagerness

Pune o întrebare simplă precum „Care este 15% din 200?” folosind Low Eagerness. Vei primi un răspuns instant și direct. Acum pune ceva complex, ca „Proiectează o strategie de caching pentru un API cu trafic intens” folosind High Eagerness. Apasă **🔴 Stream Response (Live)** și urmărește raționamentul detaliat al modelului care apare token cu token. Același model, aceeași structură a întrebării – dar promptul îi spune cât să gândească.

### Execuția sarcinilor (Preambule pentru unelte)

Fluxurile de lucru cu mai mulți pași beneficiază de o planificare inițială și narațiune a progresului. Modelul schițează ce va face, povestește fiecare pas, apoi sumarizează rezultatele.

### Cod auto-reflectant

Încearcă „Creează un serviciu de validare email”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează conform criteriilor de calitate, identifică slăbiciuni și îmbunătățește. Vei vedea cum iterează până codul îndeplinește standardele de producție.

### Analiză structurată

Revizuirile de cod necesită cadre consistente de evaluare. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

### Chat multi-turn

Întreabă „Ce este Spring Boot?” apoi imediat „Arată-mi un exemplu”. Modelul își amintește prima întrebare și îți oferă un exemplu specific Spring Boot. Fără memorie, a doua întrebare ar fi prea vagă.

### Raționament pas cu pas

Alege o problemă de matematică și încearc-o atât cu Raționament pas cu pas, cât și cu Low Eagerness. Low eagerness oferă doar răspunsul – rapid dar opac. Pas cu pas îți arată fiecare calcul și decizie.

### Output constrâns

Când ai nevoie de formate specifice sau număr fix de cuvinte, acest model impune respectarea strictă. Încearcă să generezi un rezumat cu exact 100 de cuvinte în format bullet points.

## Ce înveți cu adevărat

**Efortul de raționament schimbă totul**

GPT-5.2 îți permite să controlezi efortul computațional prin prompturile tale. Efort redus înseamnă răspunsuri rapide cu explorare minimă. Efort ridicat înseamnă că modelul își ia timp să gândească în profunzime. Înveți să potrivești efortul cu complexitatea sarcinii – să nu pierzi timp pe întrebări simple, dar nici să grăbești deciziile complexe.

**Structura ghidează comportamentul**

Observi etichetele XML din prompturi? Nu sunt decorative. Modelele respectă instrucțiunile structurate mult mai fiabil decât textele libere. Când ai nevoie de procese în pași sau logică complexă, structura ajută modelul să urmărească unde se află și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare în stil XML*

**Calitatea prin auto-evaluare**

Modelele auto-reflectante funcționează prin explicitarea criteriilor de calitate. În loc să speri că modelul „face bine”, îi spui exact ce înseamnă „bine”: logică corectă, tratament al erorilor, performanță, securitate. Modelul poate apoi să își evalueze singur output-ul și să se îmbunătățească. Acest proces transformă generarea de cod dintr-o loterie într-un proces controlat.

**Contextul este finit**

Conversațiile multi-turn funcționează prin includerea istoricului mesajelor la fiecare cerere. Dar există o limită – fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, vei avea nevoie de strategii pentru a păstra contextul relevant fără a atinge plafonul. Acest modul arată cum funcționează memoria; mai târziu vei învăța când să sumarizezi, când să uiți și când să recuperezi.

## Pașii următori

**Următorul modul:** [03-rag - RAG (Generare augmentată prin recuperare)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->