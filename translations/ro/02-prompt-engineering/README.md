# Modulul 02: Ingineria Prompturilor cu GPT-5.2

## Cuprins

- [Ce vei învăța](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea ingineriei prompturilor](../../../02-prompt-engineering)
- [Fundamentele ingineriei prompturilor](../../../02-prompt-engineering)
  - [Promptare Zero-Shot](../../../02-prompt-engineering)
  - [Promptare Few-Shot](../../../02-prompt-engineering)
  - [Lanț de gândire](../../../02-prompt-engineering)
  - [Promptare bazată pe roluri](../../../02-prompt-engineering)
  - [Șabloane de prompturi](../../../02-prompt-engineering)
- [Modele avansate](../../../02-prompt-engineering)
- [Folosirea resurselor Azure existente](../../../02-prompt-engineering)
- [Capturi de ecran ale aplicației](../../../02-prompt-engineering)
- [Explorarea modelelor](../../../02-prompt-engineering)
  - [Dorinta scăzută vs dorința ridicată](../../../02-prompt-engineering)
  - [Execuția sarcinilor (preambule pentru unelte)](../../../02-prompt-engineering)
  - [Cod auto-reflectiv](../../../02-prompt-engineering)
  - [Analiză structurată](../../../02-prompt-engineering)
  - [Conversații cu mai multe cicluri](../../../02-prompt-engineering)
  - [Raționament pas cu pas](../../../02-prompt-engineering)
  - [Ieșire restricționată](../../../02-prompt-engineering)
- [Ce înveți cu adevărat](../../../02-prompt-engineering)
- [Pașii următori](../../../02-prompt-engineering)

## Ce vei învăța

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

În modulul anterior, ai văzut cum memoria permite AI-ului conversațional și ai folosit Modelele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebări — însuși prompturile — folosind GPT-5.2 de la Azure OpenAI. Modul în care structurezi prompturile afectează dramatic calitatea răspunsurilor pe care le primești. Începem cu o revizuire a tehnicilor fundamentale de promptare, apoi trecem la opt modele avansate care valorifică pe deplin capabilitățile GPT-5.2.

Vom folosi GPT-5.2 deoarece introduce controlul raționamentului — poți spune modelului cât să gândească înainte să răspundă. Aceasta face ca diferitele strategii de promptare să fie mai evidente și te ajută să înțelegi când să folosești fiecare abordare. De asemenea, vom beneficia de limite mai mici de rată în Azure pentru GPT-5.2 comparativ cu Modelele GitHub.

## Prerechizite

- Modulul 01 finalizat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea ingineriei prompturilor

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ingineria prompturilor înseamnă proiectarea textului de intrare care îți oferă în mod consecvent rezultatele dorite. Nu este doar despre a pune întrebări - este despre structurarea cererilor astfel încât modelul să înțeleagă exact ce vrei și cum să livreze.

Gândește-te ca și cum ai da instrucțiuni unui coleg. „Remediază bugul” este vag. „Remediază excepția null pointer în UserService.java linia 45 prin adăugarea unei verificări de null” este specific. Modelele lingvistice funcționează la fel - specificitatea și structura contează.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j oferă infrastructura — conexiunile cu modelul, memoria și tipurile de mesaje — în timp ce modelele de prompturi sunt doar text structurat cu grijă pe care îl trimiți prin această infrastructură. Blocurile cheie sunt `SystemMessage` (care setează comportamentul și rolul AI-ului) și `UserMessage` (care poartă cererea ta efectivă).

## Fundamentele ingineriei prompturilor

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Înainte de a intra în modelele avansate din acest modul, să revizuim cinci tehnici fundamentale de promptare. Acestea sunt blocurile de bază pe care fiecare inginer de prompturi ar trebui să le cunoască. Dacă ai lucrat deja cu [modulul Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — iată cadrul conceptual din spatele lor.

### Promptare Zero-Shot

Abordarea cea mai simplă: oferă modelului o instrucțiune directă fără exemple. Modelul se bazează complet pe antrenamentul său pentru a înțelege și executa sarcina. Acest lucru funcționează bine pentru cereri simple unde comportamentul așteptat este evident.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina doar din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: "Pozitiv"
```

**Când să folosești:** Clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidaj suplimentar.

### Promptare Few-Shot

Oferă exemple care demonstrează tiparul pe care vrei să îl urmeze modelul. Modelul învață formatul de intrare-ieșire așteptat din exemplele tale și îl aplică la intrări noi. Acest lucru îmbunătățește dramatic consistența pentru sarcinile unde formatul sau comportamentul dorit nu este evident.

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

**Când să folosești:** Clasificări personalizate, formatare consistentă, sarcini specifice domeniului sau când rezultatele zero-shot sunt inconsistente.

### Lanț de gândire

Cere modelului să-și arate raționamentul pas cu pas. În loc să sară direct la un răspuns, modelul descompune problema și parcurge fiecare parte explicit. Aceasta îmbunătățește acuratețea în probleme de matematică, logică și raționamente în mai mulți pași.

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

**Când să folosești:** Probleme de matematică, puzzle-uri logice, depanare sau orice sarcină unde afișarea procesului de raționament îmbunătățește precizia și încrederea.

### Promptare bazată pe roluri

Setează o persoană sau un rol pentru AI înainte de a pune întrebarea. Aceasta oferă context ce modelează tonul, adâncimea și focalizarea răspunsului. Un „arhitect software” oferă sfaturi diferite față de un „dezvoltator junior” sau un „auditor de securitate”.

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setarea contextului și persoanei — aceeași întrebare primește un răspuns diferit în funcție de rolul atribuit*

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

**Când să folosești:** Revizuiri de cod, mentorat, analize specifice domeniului sau când ai nevoie de răspunsuri adaptate la un anumit nivel de expertiză sau perspectivă.

### Șabloane de prompturi

Creează prompturi reutilizabile cu variabile. În loc să scrii un nou prompt de fiecare dată, definește un șablon o singură dată și completează cu valori diferite. Clasa `PromptTemplate` din LangChain4j facilitează acest lucru cu sintaxa `{{variable}}`.

<img src="../../../translated_images/ro/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

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

**Când să folosești:** Interogări repetitive cu intrări diferite, procesare în loturi, construirea de fluxuri AI reutilizabile sau orice scenariu în care structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamente îți oferă un set solid de instrumente pentru majoritatea sarcinilor de promptare. Restul acestui modul construiește pe ele cu **opt modele avansate** care valorifică controlul raționamentului, auto-evaluarea și capabilitățile de ieșire structurată ale GPT-5.2.

## Modele avansate

După ce am acoperit fundamentele, să trecem la cele opt modele avansate ce fac acest modul unic. Nu toate problemele necesită aceeași abordare. Unele întrebări au nevoie de răspunsuri rapide, altele de gândire profundă. Unele necesită raționament vizibil, altele doar rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului din GPT-5.2 face diferențele și mai pronunțate.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prezentare generală a celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici cât de mult să gândească modelul — de la răspunsuri rapide și directe până la explorări profunde*

**Dorinta scăzută (Rapid & Focusat)** - Pentru întrebări simple unde vrei răspunsuri rapide și directe. Modelul face raționament minim - maximum 2 pași. Folosește-l pentru calcule, căutări sau întrebări directe.

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
> - „Care este diferența între modelele de promptare cu dorință scăzută și dorință ridicată?”
> - „Cum ajută etichetele XML din prompturi la structurarea răspunsului AI?”
> - „Când ar trebui să folosesc modelele de auto-reflecție vs instrucțiuni directe?”

**Dorinta ridicată (Profund & Amănunțit)** - Pentru probleme complexe unde vrei o analiză cuprinzătoare. Modelul explorează temeinic și arată raționamentul detaliat. Folosește-l pentru design de sisteme, decizii de arhitectură sau cercetare complexă.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execuția sarcinii (progres pas cu pas)** - Pentru fluxuri de lucru în mai mulți pași. Modelul oferă un plan inițial, povestește fiecare pas pe măsură ce lucrează, apoi oferă un rezumat. Folosește-l pentru migrații, implementări sau orice proces în mai mulți pași.

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

Promptarea de tip lanț de gândire cere explicit modelului să arate procesul său de raționament, îmbunătățind acuratețea pentru sarcini complexe. Descompunerea pas cu pas ajută atât oamenii, cât și AI-ul să înțeleagă logica.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre acest model:
> - „Cum aș adapta modelul de execuție a sarcinii pentru operațiuni de durată lungă?”
> - „Care sunt bunele practici pentru structurarea preambulurilor uneltelor în aplicații de producție?”
> - „Cum pot captura și afișa actualizări intermediare de progres într-o interfață UI?”

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Flux de lucru Plan → Execută → Rezumă pentru sarcini în mai mulți pași*

**Cod auto-reflectiv** - Pentru generarea de cod de calitate pentru producție. Modelul generează cod respectând standardele de producție cu gestionare corectă a erorilor. Folosește-l când construiești noi funcționalități sau servicii.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Buclă iterativă de îmbunătățire - generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză structurată** - Pentru evaluare consistentă. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate, întreținere). Folosește-l pentru revizuiri de cod sau evaluări de calitate.

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
> - „Care este cea mai bună metodă de a analiza și acționa pe ieșirea structurată programatic?”
> - „Cum asigur consistența nivelurilor de severitate între diferite sesiuni de revizuire?”

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Cadrul pentru revizuiri consistente de cod cu niveluri de severitate*

**Conversații cu mai multe cicluri** - Pentru conversații care necesită context. Modelul își amintește mesajele anterioare și construiește pe baza lor. Folosește-l pentru sesiuni interactive de ajutor sau întrebări și răspunsuri complexe.

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

*Cum se acumulează contextul conversației pe mai multe schimburi până la limita de tokeni*

**Raționament pas cu pas** - Pentru probleme care necesită logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește-l pentru probleme de matematică, puzzle-uri logice sau când trebuie să înțelegi procesul de gândire.

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

**Ieșire restricționată** - Pentru răspunsuri cu cerințe specifice de format. Modelul urmează strict regulile de format și lungime. Folosește-l pentru rezumate sau când ai nevoie de o structură precisă a ieșirii.

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

*Aplicarea cerințelor specifice de format, lungime și structură*

## Folosirea resurselor Azure existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în timpul Modulului 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de start de mai jos și să accesezi direct http://localhost:8083.

**Opțiunea 1: Folosirea Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul dev include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în bara de activități din partea stângă a VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard, poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opri aplicațiile cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor
Pur și simplu faceți clic pe butonul de redare de lângă „prompt-engineering” pentru a începe acest modul sau porniți toate modulele odată.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opțiunea 2: Utilizarea scripturilor shell**

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor compila JAR-urile dacă nu există.

> **Notă:** Dacă preferați să compilați manual toate modulele înainte de a începe:
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
./stop.sh  # Numai acest modul
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

*Tabloul principal de bord care arată toate cele 8 modele de prompt engineering cu caracteristicile și cazurile lor de utilizare*

## Explorarea modelelor

Interfața web vă permite să experimentați diferite strategii de solicitare. Fiecare model rezolvă probleme diferite – încercați-le pentru a vedea când strălucește fiecare abordare.

### Diligență scăzută vs ridicată

Adresați o întrebare simplă, cum ar fi „Care este 15% din 200?” folosind Diligență scăzută. Veți primi un răspuns instantaneu, direct. Acum întrebați ceva complex, cum ar fi „Proiectează o strategie de caching pentru o API cu trafic ridicat” folosind Diligență ridicată. Urmăriți cum modelul încetinește și oferă un raționament detaliat. Același model, aceeași structură a întrebării – dar promptul îi spune câtă gândire să depună.

<img src="../../../translated_images/ro/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Calcul rapid cu raționament minim*

<img src="../../../translated_images/ro/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Strategie de caching detaliată (2.8MB)*

### Executarea sarcinilor (Introduceri pentru unelte)

Fluxurile de lucru în mai mulți pași beneficiază de planificare inițială și narare a progresului. Modelul descrie ce va face, narrează fiecare pas, apoi rezumă rezultatele.

<img src="../../../translated_images/ro/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Crearea unui endpoint REST cu narare pas cu pas (3.9MB)*

### Cod auto-reflectiv

Încercați „Creează un serviciu de validare a emailurilor”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează pe baza criteriilor de calitate, identifică slăbiciunile și îmbunătățește. Veți vedea cum iteratează până când codul atinge standardele de producție.

<img src="../../../translated_images/ro/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Serviciu complet de validare a emailurilor (5.2MB)*

### Analiză structurată

Revizuirile de cod au nevoie de cadre de evaluare consistente. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

<img src="../../../translated_images/ro/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revizuire de cod bazată pe un cadru*

### Chat multi-rundă

Întrebați „Ce este Spring Boot?” apoi imediat urmăriți cu „Arată-mi un exemplu”. Modelul își amintește prima întrebare și vă oferă un exemplu specific Spring Boot. Fără memorie, a doua întrebare ar fi prea vagă.

<img src="../../../translated_images/ro/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Păstrarea contextului între întrebări*

### Raționament pas cu pas

Alegeți o problemă de matematică și încercați-o atât cu Raționament pas cu pas cât și cu Diligență scăzută. Diligența scăzută vă oferă doar răspunsul - rapid, dar opac. Raționamentul pas cu pas vă arată fiecare calcul și decizie.

<img src="../../../translated_images/ro/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema de matematică cu pași expliciți*

### Output constrâns

Când aveți nevoie de formate specifice sau număr de cuvinte, acest model aplică o respectare strictă. Încercați să generați un rezumat cu exact 100 de cuvinte în format bullet points.

<img src="../../../translated_images/ro/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Rezumat de machine learning cu control al formatului*

## Ce învățați cu adevărat

**Efortul de raționament schimbă totul**

GPT-5.2 vă permite să controlați efortul computațional prin prompturile dvs. Efortul mic înseamnă răspunsuri rapide cu explorare minimă. Efortul ridicat înseamnă că modelul ia timp să gândească profund. Înveți să potrivești efortul cu complexitatea sarcinii – nu pierdeți timp pe întrebări simple, dar nici nu vă grăbiți în decizii complexe.

**Structura ghidează comportamentul**

Observați etichetele XML din prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât textul liber. Când aveți nevoie de procese în mai mulți pași sau logică complexă, structura ajută modelul să urmărească unde se află și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare stil XML*

**Calitatea prin auto-evaluare**

Modelele auto-reflective funcționează făcând criteriile de calitate explicite. În loc să sperați că modelul „face bine”, îi spuneți exact ce înseamnă „bine”: logică corectă, gestionarea erorilor, performanță, securitate. Modelul își poate evalua apoi singur rezultatul și îl poate îmbunătăți. Aceasta transformă generarea de cod dintr-o loterie într-un proces.

**Contextul este finit**

Conversațiile multi-rundă funcționează prin includerea istoricului mesajelor la fiecare cerere. Dar există o limită - fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, veți avea nevoie de strategii pentru a păstra contextul relevant fără să atingeți acel plafon. Acest modul vă arată cum funcționează memoria; mai târziu veți învăța când să rezumați, când să uitați și când să recuperați.

## Pașii următori

**Următorul modul:** [03-rag - RAG (Generare augmentată prin recuperare)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->