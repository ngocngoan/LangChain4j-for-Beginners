# Modulul 02: Ingineria prompturilor cu GPT-5.2

## Cuprins

- [Prezentare video](../../../02-prompt-engineering)
- [Ce vei învăța](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea Ingineriei Prompturilor](../../../02-prompt-engineering)
- [Fundamentele Ingineriei Prompturilor](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Șabloane de Prompturi](../../../02-prompt-engineering)
- [Modele avansate](../../../02-prompt-engineering)
- [Rulează aplicația](../../../02-prompt-engineering)
- [Capturi de ecran ale aplicației](../../../02-prompt-engineering)
- [Explorarea modelelor](../../../02-prompt-engineering)
  - [Eageritate scăzută vs ridicată](../../../02-prompt-engineering)
  - [Executarea sarcinilor (preambuluri pentru instrumente)](../../../02-prompt-engineering)
  - [Cod cu auto-reflecție](../../../02-prompt-engineering)
  - [Analiză structurată](../../../02-prompt-engineering)
  - [Chat în mai multe runde](../../../02-prompt-engineering)
  - [Raționament pas cu pas](../../../02-prompt-engineering)
  - [Ieșire constrânsă](../../../02-prompt-engineering)
- [Ce înveți cu adevărat](../../../02-prompt-engineering)
- [Pașii următori](../../../02-prompt-engineering)

## Prezentare video

Urmărește această sesiune live care explică cum să începi cu acest modul:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## Ce vei învăța

Diagrama următoare oferă o privire de ansamblu asupra subiectelor cheie și abilităților pe care le vei dezvolta în acest modul — de la tehnicile de rafinare a prompturilor până la fluxul de lucru pas cu pas pe care îl vei urma.

<img src="../../../translated_images/ro/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

În modulele anterioare, ai explorat interacțiuni de bază LangChain4j cu GitHub Models și ai văzut cum memoria permite AI conversațional cu Azure OpenAI. Acum ne vom concentra pe cum formulezi întrebările — prompturile propriu-zise — folosind GPT-5.2 de la Azure OpenAI. Modul în care structurezi prompturile afectează dramatic calitatea răspunsurilor primite. Începem cu o revizuire a tehnicilor fundamentale de prompting, apoi trecem la opt modele avansate care valorifică pe deplin capacitățile GPT-5.2.

Vom folosi GPT-5.2 deoarece introduce controlul raționamentului - poți spune modelului cât să gândească înainte să răspundă. Aceasta face diversele strategii de prompting mai evidente și te ajută să înțelegi când să folosești fiecare abordare. Vom beneficia și de mai puține limite de rată la Azure pentru GPT-5.2 comparativ cu modelele GitHub.

## Prerechizite

- Modulul 01 finalizat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare din acolo.

## Înțelegerea Ingineriei Prompturilor

În esență, ingineria prompturilor este diferența dintre instrucțiuni vagi și precise, după cum ilustrează comparația de mai jos.

<img src="../../../translated_images/ro/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Ingineria prompturilor ține de proiectarea textului de intrare care îți oferă în mod constant rezultatele dorite. Nu este doar despre a pune întrebări - este despre structurarea cererilor astfel încât modelul să înțeleagă exact ce dorești și cum să livreze.

Gândește-te la asta ca la a da instrucțiuni unui coleg. "Remediază bug-ul" este vag. "Rezolvă excepția null pointer în UserService.java linia 45 prin adăugarea unui control pentru null" este specific. Modelele lingvistice funcționează la fel - specificitatea și structura contează.

Diagrama de mai jos arată cum se încadrează LangChain4j în acest tablou — conectând modelele tale de prompturi la model prin blocuri de construcție SystemMessage și UserMessage.

<img src="../../../translated_images/ro/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j oferă infrastructura — conexiuni la modele, memorie și tipuri de mesaje — în timp ce modelele de prompturi sunt doar texte atent structurate pe care le trimiți prin acea infrastructură. Blocurile cheie sunt `SystemMessage` (care setează comportamentul și rolul AI-ului) și `UserMessage` (care conține cererea ta efectivă).

## Fundamentele Ingineriei Prompturilor

Cele cinci tehnici de bază prezentate mai jos formează fundamentul ingineriei eficiente a prompturilor. Fiecare abordează un aspect diferit al modului în care comunici cu modelele lingvistice.

<img src="../../../translated_images/ro/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Înainte să explorăm modelele avansate din acest modul, să revizuim cele cinci tehnici fundamentale de prompting. Acestea sunt blocurile de construcție pe care orice inginer de prompturi ar trebui să le cunoască. Dacă ai parcurs deja modulul [Quick Start](../00-quick-start/README.md#2-prompt-patterns), le-ai văzut în acțiune — iată cadrul conceptual din spatele lor.

### Zero-Shot Prompting

Abordarea cea mai simplă: oferă modelului o instrucțiune directă fără exemple. Modelul se bazează exclusiv pe antrenamentul său pentru a înțelege și executa sarcina. Funcționează bine pentru cereri simple unde comportamentul așteptat este evident.

<img src="../../../translated_images/ro/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrucțiune directă fără exemple — modelul deduce sarcina doar din instrucțiune*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Răspuns: "Pozitiv"
```

**Când să folosești:** Clasificări simple, întrebări directe, traduceri sau orice sarcină pe care modelul o poate gestiona fără ghidaj suplimentar.

### Few-Shot Prompting

Oferă exemple care demonstrează tiparul pe care vrei modelul să-l urmeze. Modelul învață formatul de intrare-ieșire așteptat din exemplele tale și îl aplică la intrări noi. Aceasta îmbunătățește dramatic consistența pentru sarcini unde formatul sau comportamentul dorit nu sunt evidente.

<img src="../../../translated_images/ro/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Învățarea din exemple — modelul identifică tiparul și îl aplică la intrări noi*

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

### Chain of Thought

Cere modelului să arate raționamentul său pas cu pas. În loc sări direct la un răspuns, modelul descompune problema și lucrează prin fiecare parte explicit. Aceasta îmbunătățește acuratețea la probleme matematice, logice și de raționament în mai mulți pași.

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

**Când să folosești:** Probleme de matematică, puzzle-uri logice, depanare sau orice sarcină unde afișarea procesului de raționament îmbunătățește acuratețea și încrederea.

### Role-Based Prompting

Setează o persona sau un rol pentru AI înainte de a pune întrebarea ta. Aceasta oferă context care modelează tonul, profunzimea și accentul răspunsului. Un „arhitect software” oferă sfaturi diferite de un „dezvoltator junior” sau un „auditor de securitate”.

<img src="../../../translated_images/ro/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Setarea contextului și a personajului — aceeași întrebare primește răspunsuri diferite în funcție de rolul atribuit*

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

**Când să folosești:** Revizuiri de cod, meditații, analize specifice domeniului sau când ai nevoie de răspunsuri adaptate unui anumit nivel de expertiză sau perspectivă.

### Șabloane de Prompturi

Creează prompturi reutilizabile cu variabile. În loc să scrii un prompt nou de fiecare dată, definește o șablon o singură dată și completează valori diferite. Clasa `PromptTemplate` din LangChain4j face acest lucru ușor cu sintaxa `{{variable}}`.

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

**Când să folosești:** Interogări repetitive cu intrări diferite, procesare în lot, construirea de fluxuri AI reutilizabile sau orice scenariu unde structura promptului rămâne aceeași, dar datele se schimbă.

---

Aceste cinci fundamentale îți oferă un set solid de unelte pentru majoritatea sarcinilor de prompting. Restul modulului se bazează pe ele cu **opt modele avansate** care valorifică controlul raționamentului, auto-evaluarea și capacitățile de ieșire structurate ale GPT-5.2.

## Modele avansate

Odată acoperite fundamentele, să trecem la cele opt modele avansate care fac acest modul unic. Nu toate problemele necesită aceeași abordare. Unele întrebări au nevoie de răspunsuri rapide, altele de gândire profundă. Unele cer raționament vizibil, altele doar rezultate. Fiecare model de mai jos este optimizat pentru un scenariu diferit — iar controlul raționamentului din GPT-5.2 face diferențele și mai evidente.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Privire de ansamblu asupra celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

GPT-5.2 adaugă o dimensiune suplimentară acestor modele: *controlul raționamentului*. Glisorul de mai jos arată cum poți ajusta efortul de gândire al modelului — de la răspunsuri rapide și directe la analize profunde și detaliate.

<img src="../../../translated_images/ro/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*Controlul raționamentului GPT-5.2 îți permite să specifici cât trebuie să gândească modelul — de la răspunsuri rapide și directe la explorare profundă*

**Eageritate scăzută (rapid și concentrat)** - Pentru întrebări simple unde vrei răspunsuri rapide și directe. Modelul face raționament minim - maxim 2 pași. Folosește asta pentru calcule, căutări sau întrebări directe.

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
> - "Care este diferența dintre modelele de prompt cu eageritate scăzută și cele cu eageritate ridicată?"
> - "Cum ajută etichetele XML din prompturi la structurarea răspunsului AI?"
> - "Când ar trebui să folosesc modelele de auto-reflecție versus instrucțiunea directă?"

**Eageritate ridicată (profund și temeinic)** - Pentru probleme complexe unde dorești o analiză cuprinzătoare. Modelul explorează temeinic și arată raționamente detaliate. Folosește asta pentru proiectarea sistemelor, decizii de arhitectură sau cercetări complexe.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea sarcinilor (progres pas cu pas)** - Pentru fluxuri de lucru în mai mulți pași. Modelul oferă un plan inițial, povestește fiecare pas pe măsură ce îl execută, apoi oferă un rezumat. Folosește asta pentru migrații, implementări sau orice proces în mai mulți pași.

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

Prompting-ul Chain-of-Thought cere explicit modelului să-și arate procesul de raționament, îmbunătățind acuratețea pentru sarcini complexe. Descompunerea pas cu pas ajută atât oamenii, cât și AI-ul să înțeleagă logica.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Întreabă despre acest model:
> - "Cum aș adapta modelul de executare a sarcinii pentru operațiuni de durată lungă?"
> - "Care sunt cele mai bune practici pentru structurarea preambulurilor instrumentelor în aplicații de producție?"
> - "Cum pot captura și afișa actualizări de progres intermediare într-o interfață?"

Diagrama de mai jos ilustrează acest flux Plan → Execută → Rezumă.

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Flux Plan → Execută → Rezumă pentru sarcini în pași multipli*

**Cod cu auto-reflecție** - Pentru generarea de cod de calitate de producție. Modelul generează cod conform standardelor de producție cu gestionare corespunzătoare a erorilor. Folosește asta când construiești funcționalități sau servicii noi.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Diagrama următoare arată acest ciclu iterativ de îmbunătățire — generează, evaluează, identifică punctele slabe și rafinează până când codul atinge standardele de producție.

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ciclu iterativ de îmbunătățire - generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză structurată** - Pentru evaluare consistentă. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate, mentenabilitate). Folosește asta pentru code review-uri sau evaluări de calitate.

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
> - "Cum pot personaliza cadrul de analiză pentru diferite tipuri de revizuiri de cod?"
> - "Care este cea mai bună metodă de a parsa și acționa pe baza ieșirii structurate programatic?"
> - "Cum asigur un nivel consistent de severitate în sesiuni diferite de revizuire?"

Diagrama următoare arată cum acest cadru structurat organizează o revizuire de cod în categorii consistente cu nivele de severitate.

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Cadru pentru revizuiri consistente ale codului cu nivele de severitate*

**Chat în mai multe runde** - Pentru conversații care necesită context. Modelul își amintește mesajele anterioare și le construiește pe ele. Folosește asta pentru sesiuni interactive de asistență sau Q&A complexe.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Diagrama de mai jos vizualizează cum se acumulează contextul conversației cu fiecare tură și cum se raportează la limita de tokeni a modelului.

<img src="../../../translated_images/ro/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Cum se acumulează contextul conversației peste mai multe runde până la atingerea limitei de tokeni*
**Raționament Pas cu Pas** - Pentru probleme care necesită logică vizibilă. Modelul arată raționamentul explicit pentru fiecare pas. Folosește asta pentru probleme de matematică, puzzle-uri logice sau când ai nevoie să înțelegi procesul de gândire.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Diagrama de mai jos ilustrează cum modelul descompune problemele în pași logici expliciți, numerotați.

<img src="../../../translated_images/ro/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Tipar Pas cu Pas" width="800"/>

*Descompunerea problemelor în pași logici expliciți*

**Rezultat Constrâns** - Pentru răspunsuri cu cerințe specifice de format. Modelul urmează cu strictețe regulile de format și lungime. Folosește asta pentru rezumate sau când ai nevoie de o structură precisă a rezultatului.

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

Diagrama următoare arată cum constrângerile ghidează modelul să producă un rezultat care respectă cu strictețe cerințele tale de format și lungime.

<img src="../../../translated_images/ro/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Tipar Rezultat Constrâns" width="800"/>

*Impunerea cerințelor specifice de format, lungime și structură*

## Rulează Aplicația

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (create în Modulul 01). Rulează de aici din directorul modulului (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din directorul rădăcină (așa cum este descris în Modulul 01), acest modul este deja pornit pe portul 8083. Poți sări peste comenzile de start de mai jos și să accesezi direct http://localhost:8083.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în Bara de Activități din partea stângă a VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard poți:
- Să vezi toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Să pornești/oprești aplicațiile cu un singur click
- Să vizualizezi log-urile aplicațiilor în timp real
- Să monitorizezi starea aplicațiilor

Pur și simplu click pe butonul de redare de lângă "prompt-engineering" pentru a porni acest modul, sau pornește toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard în VS Code — pornește, oprește și monitorizează toate modulele dintr-un singur loc*

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor construi JAR-urile dacă nu există.

> **Notă:** Dacă preferi să construiești manual toate modulele înainte de pornire:
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

## Capturi de Ecran ale Aplicației

Iată interfața principală a modulului de inginerie a prompturilor, unde poți experimenta toate cele opt tipare una lângă alta.

<img src="../../../translated_images/ro/dashboard-home.5444dbda4bc1f79d.webp" alt="Panoul Principal" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Panoul principal afișând toate cele 8 tipare de inginerie a prompturilor cu caracteristicile și cazurile lor de utilizare*

## Explorarea Tiparelor

Interfața web îți permite să experimentezi cu diferite strategii de promptare. Fiecare tipar rezolvă probleme diferite - încearcă-le pentru a vedea când strălucește fiecare abordare.

> **Notă: Streaming vs Non-Streaming** — Fiecare pagină de tipar oferă două butoane: **🔴 Stream Response (Live)** și o opțiune **Non-streaming**. Streaming folosește Server-Sent Events (SSE) pentru a afișa tokenii în timp real pe măsură ce modelul îi generează, astfel vezi progresul imediat. Opțiunea non-streaming așteaptă răspunsul complet înainte de afișare. Pentru prompturi care declanșează raționamente profunde (de ex. High Eagerness, Self-Reflecting Code), apelul non-streaming poate dura foarte mult – uneori minute – fără feedback vizibil. **Folosește streaming atunci când experimentezi cu prompturi complexe** pentru a vedea modelul în acțiune și a evita impresia că cererea a expirat.
>
> **Notă: Cerință Browser** — Funcția de streaming folosește Fetch Streams API (`response.body.getReader()`) care necesită un browser complet (Chrome, Edge, Firefox, Safari). NU funcționează în Simple Browser-ul integrat în VS Code, deoarece webview-ul său nu suportă API-ul ReadableStream. Dacă folosești Simple Browser, butoanele non-streaming vor funcționa normal — doar butoanele de streaming sunt afectate. Deschide `http://localhost:8083` într-un browser extern pentru experiența completă.

### Low vs High Eagerness

Pune o întrebare simplă precum „Care este 15% din 200?” folosind Low Eagerness. Vei primi un răspuns rapid, direct. Acum pune ceva complex ca „Proiectează o strategie de caching pentru un API cu trafic mare” folosind High Eagerness. Apasă **🔴 Stream Response (Live)** și urmărește cum apare raționamentul detaliat token-cu-token. Același model, aceeași structură a întrebării – dar promptul îi spune cât de mult să gândească.

### Executarea Sarcinilor (Tool Preambles)

Fluxurile multi-pasu beneficiază de planificare în avans și narare a progresului. Modelul schițează ce va face, povestește fiecare pas, apoi rezumă rezultatele.

### Cod Auto-Reflectiv

Încearcă „Crează un serviciu de validare email”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează după criterii de calitate, identifică punctele slabe și îmbunătățește. Vei vedea cum iterează până când codul îndeplinește standardele de producție.

### Analiză Structurată

Reviziile de cod au nevoie de cadre de evaluare consistente. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

### Chat Multi-Turn

Întreabă „Ce este Spring Boot?” apoi urmează imediat cu „Arată-mi un exemplu”. Modelul își amintește prima întrebare și îți oferă un exemplu specific Spring Boot. Fără memorie, a doua întrebare ar fi prea vagă.

### Raționament Pas cu Pas

Alege o problemă de matematică și încearc-o atât cu Raționament Pas cu Pas cât și cu Low Eagerness. Low eagerness îți dă doar răspunsul – rapid, dar opac. Raționamentul pas cu pas îți arată toate calculele și deciziile.

### Rezultat Constrâns

Când ai nevoie de formate specifice sau un număr fix de cuvinte, acest tipar impune respectarea strictă. Încearcă să generezi un rezumat cu exact 100 de cuvinte în format punctat.

## Ce Înveți Cu Adevărat

**Efortul de Raționament Schimbă Totul**

GPT-5.2 îți permite să controlezi efortul computațional prin prompturi. Efortul mic înseamnă răspunsuri rapide cu explorare minimă. Efortul mare înseamnă că modelul ia timp să gândească profund. Înveți să potrivești efortul cu complexitatea sarcinii – nu pierde timp cu întrebări simple, dar nu grăbi deciziile complexe.

**Structura Ghidează Comportamentul**

Observi etichetele XML în prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât textul liber. Când ai nevoie de procese multi-pasu sau logică complexă, structura ajută modelul să știe unde este și ce urmează. Diagrama de mai jos descompune un prompt bine structurat, arătând cum tag-urile ca `<system>`, `<instructions>`, `<context>`, `<user-input>` și `<constraints>` organizează instrucțiunile în secțiuni clare.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Structura Promptului" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare de tip XML*

**Calitatea Prin Auto-Evaluare**

Tiparele auto-reflective funcționează prin explicitarea criteriilor de calitate. În loc să speri că modelul „face bine”, îi spui exact ce înseamnă „bine”: logică corectă, tratament al erorilor, performanță, securitate. Modelul își poate evalua apoi propriul output și-l poate îmbunătăți. Astfel, generarea de cod devine un proces, nu o loterie.

**Contextul Este Limitat**

Conversațiile multi-turn funcționează prin includerea istoricului mesajelor la fiecare cerere. Dar există o limită – fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, vei avea nevoie de strategii ca să păstrezi context relevant fără să atingi plafonul. Acest modul îți arată cum funcționează memoria; mai târziu vei învăța când să rezumi, când să uiți și când să recuperezi.

## Pașii Următori

**Următorul ModuI:** [03-rag - RAG (Generare Augmentată prin Recuperare)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->