# Modulul 02: Ingineria Prompturilor cu GPT-5.2

## Cuprins

- [Ce vei învăța](../../../02-prompt-engineering)
- [Prerechizite](../../../02-prompt-engineering)
- [Înțelegerea ingineriei prompturilor](../../../02-prompt-engineering)
- [Cum se folosește LangChain4j](../../../02-prompt-engineering)
- [Modelele de bază](../../../02-prompt-engineering)
- [Utilizarea resurselor Azure existente](../../../02-prompt-engineering)
- [Capturi de ecran ale aplicației](../../../02-prompt-engineering)
- [Explorarea modelelor](../../../02-prompt-engineering)
  - [Anticipare scăzută vs anticipare ridicată](../../../02-prompt-engineering)
  - [Executarea sarcinilor (preludii instrumente)](../../../02-prompt-engineering)
  - [Cod autoreflexiv](../../../02-prompt-engineering)
  - [Analiză structurată](../../../02-prompt-engineering)
  - [Chat multi-tur](../../../02-prompt-engineering)
  - [Raționament pas cu pas](../../../02-prompt-engineering)
  - [Ieșire restricționată](../../../02-prompt-engineering)
- [Ce înveți cu adevărat](../../../02-prompt-engineering)
- [Pașii următori](../../../02-prompt-engineering)

## Ce vei învăța

În modulul anterior, ai văzut cum memoria permite AI conversațional și ai folosit modelele GitHub pentru interacțiuni de bază. Acum ne vom concentra pe modul în care pui întrebările - prompturile în sine - folosind GPT-5.2 de la Azure OpenAI. Modul în care structurezi prompturile influențează dramatic calitatea răspunsurilor pe care le primești.

Vom folosi GPT-5.2 deoarece introduce controlul raționamentului - poți spune modelului cât să gândească înainte să răspundă. Acest lucru face strategiile de promptare mai clare și te ajută să înțelegi când să folosești fiecare abordare. De asemenea, vom beneficia de limitele mai puține de rată ale Azure pentru GPT-5.2 comparativ cu modelele GitHub.

## Prerechizite

- Modulul 01 finalizat (resurse Azure OpenAI implementate)
- Fișier `.env` în directorul rădăcină cu credențialele Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea ingineriei prompturilor

Ingineria prompturilor înseamnă proiectarea textului de intrare care obține în mod consecvent rezultatele de care ai nevoie. Nu este doar despre a pune întrebări - este despre structurarea cererilor astfel încât modelul să înțeleagă exact ce vrei și cum să ofere răspunsul.

Gândește-te la asta ca la darea de instrucțiuni unui coleg. „Remediază bug-ul” e vag. „Remediază excepția de pointer nul în UserService.java linia 45 prin adăugarea unei verificări null” e specific. Modelele de limbaj funcționează la fel - specificitatea și structura contează.

## Cum se folosește LangChain4j

Acest modul demonstrează modele avansate de promptare folosind aceeași fundație LangChain4j din modulele anterioare, cu accent pe structura prompturilor și controlul raționamentului.

<img src="../../../translated_images/ro/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Cum LangChain4j leagă prompturile tale la Azure OpenAI GPT-5.2*

**Dependențe** – Modulul 02 folosește următoarele dependențe langchain4j definite în `pom.xml`:
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

**Configurația OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Modelul chat este configurat manual ca bean Spring folosind clientul oficial OpenAI, care suportă endpoint-urile Azure OpenAI. Diferența cheie față de Modulul 01 este modul în care structurăm prompturile trimise către `chatModel.chat()`, nu configurarea modelului în sine.

**Mesaje sistem și utilizator** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j separă tipurile de mesaje pentru claritate. `SystemMessage` setează comportamentul și contextul AI-ului (de exemplu: „Ești un recenzor de cod”), iar `UserMessage` conține cererea reală. Această separare permite menținerea unui comportament AI consistent pentru diferite întrebări ale utilizatorului.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ro/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage oferă context persistent, în timp ce UserMessages conțin cereri individuale*

**MessageWindowChatMemory pentru conversații multi-tur** – Pentru modelele de conversație multi-tur, reutilizăm `MessageWindowChatMemory` din Modulul 01. Fiecare sesiune primește propria instanță de memorie stocată într-un `Map<String, ChatMemory>`, permițând conversații multiple simultane fără amestec de context.

**Template-uri de prompt** – Concentrarea reală aici este ingineria prompturilor, nu noile API-uri LangChain4j. Fiecare model (anticipare scăzută, anticipare ridicată, executarea sarcinilor etc.) folosește aceeași metodă `chatModel.chat(prompt)`, dar cu stringuri de prompt atent structurate. Etichetele XML, instrucțiunile și formatarea fac parte din textul promptului, nu din funcționalitățile LangChain4j.

**Controlul raționamentului** – Efortul de raționament al GPT-5.2 este controlat prin instrucțiuni de prompt de tipul „maximum 2 pași de raționament” sau „explorează temeinic”. Acestea sunt tehnici de inginerie a prompturilor, nu configurații LangChain4j. Biblioteca doar livrează prompturile către model.

Concluzia principală: LangChain4j oferă infrastructura (conexiunea la model prin [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memoria, gestionarea mesajelor prin [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), iar acest modul te învață cum să creezi prompturi eficiente în cadrul acelei infrastructuri.

## Modelele de bază

Nu toate problemele necesită aceeași abordare. Unele întrebări cer răspunsuri rapide, altele gândire profundă. Unele cer raționament vizibil, altele doar rezultatele. Acest modul acoperă opt modele de promptare - fiecare optimizat pentru scenarii diferite. Te vei juca cu toate pentru a învăța când funcționează cel mai bine fiecare.

<img src="../../../translated_images/ro/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prezentarea celor opt modele de inginerie a prompturilor și cazurile lor de utilizare*

<img src="../../../translated_images/ro/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Anticipare scăzută (rapid, direct) vs anticipare ridicată (temeinic, exploratoriu) la raționament*

**Anticipare scăzută (rapid și concentrat)** – Pentru întrebări simple unde dorești răspunsuri rapide și directe. Modelul face raționament minim - maximum 2 pași. Folosește-l pentru calcule, căutări sau întrebări directe.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explorează cu GitHub Copilot:** Deschide [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) și întreabă:
> - „Care este diferența dintre modelele de promptare cu anticipare scăzută și cele cu anticipare ridicată?”
> - „Cum ajută etichetele XML în prompturi la structura răspunsului AI?”
> - „Când ar trebui să folosesc modelele autoreflexive și când instrucțiunea directă?”

**Anticipare ridicată (profund și temeinic)** – Pentru probleme complexe unde vrei o analiză cuprinzătoare. Modelul explorează temeinic și arată raționamentul detaliat. Folosește-l pentru design de sisteme, decizii de arhitectură sau cercetare complexă.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Executarea sarcinilor (progres pas cu pas)** – Pentru fluxuri de lucru în mai mulți pași. Modelul oferă un plan inițial, povestește fiecare pas în timp ce lucrează, apoi oferă un rezumat. Folosește-l pentru migrații, implementări sau orice proces în mai mulți pași.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Promptarea Chain-of-Thought cere explicit modelului să arate procesul său de raționament, îmbunătățind acuratețea pentru sarcini complexe. Defalcarea pas cu pas ajută atât oamenii, cât și AI-ul să înțeleagă logica.

> **🤖 Încearcă cu chatul [GitHub Copilot](https://github.com/features/copilot):** Întreabă despre acest model:
> - „Cum aș adapta modelul de execuție a sarcinii pentru operațiuni de durată lungă?”
> - „Care sunt bunele practici pentru structurarea preludiilor instrumentelor în aplicații de producție?”
> - „Cum pot captura și afișa actualizări intermediare de progres în UI?”

<img src="../../../translated_images/ro/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planificare → Execuție → Rezumat pentru sarcini în mai mulți pași*

**Cod autoreflexiv** – Pentru generarea de cod de calitate de producție. Modelul generează cod, îl verifică față de criterii de calitate și îl îmbunătățește iterativ. Folosește-l când construiești funcționalități sau servicii noi.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ro/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Buclă de îmbunătățire iterativă – generează, evaluează, identifică probleme, îmbunătățește, repetă*

**Analiză structurată** – Pentru evaluări consecvente. Modelul revizuiește codul folosind un cadru fix (corectitudine, practici, performanță, securitate). Folosește-l pentru recenzii de cod sau evaluări de calitate.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Încearcă cu chatul [GitHub Copilot](https://github.com/features/copilot):** Întreabă despre analiza structurată:
> - „Cum pot personaliza cadrul de analiză pentru tipuri diferite de recenzii de cod?”
> - „Care este cea mai bună metodă de a analiza și acționa programatic pe baza ieșirii structurate?”
> - „Cum asigur niveluri consecvente de severitate în sesiuni diferite de recenzii?”

<img src="../../../translated_images/ro/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Cadrul cu patru categorii pentru recenzii de cod consecvente cu niveluri de severitate*

**Chat multi-tur** – Pentru conversații care necesită context. Modelul își amintește mesajele anterioare și le construiește pe ele. Folosește-l pentru sesiuni interactive de ajutor sau întrebări complexe.

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

**Raționament pas cu pas** – Pentru probleme care necesită logică vizibilă. Modelul arată un raționament explicit pentru fiecare pas. Folosește-l pentru probleme de matematică, puzzle-uri logice sau când vrei să înțelegi procesul de gândire.

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

**Ieșire restricționată** – Pentru răspunsuri cu cerințe specifice de format. Modelul respectă strict regulile de format și lungime. Folosește-l pentru rezumate sau când ai nevoie de o structură precisă a rezultatului.

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

## Utilizarea resurselor Azure existente

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu credențialele Azure (creat în timpul Modulului 01):
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8083. Poți sări peste comenzile de pornire de mai jos și să accesezi direct http://localhost:8083.

**Opțiunea 1: Folosind Spring Boot Dashboard (recomandat utilizatorilor VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O găsești în bara de activitate din stânga VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard, poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în workspace
- Porni/opri aplicații cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor

Pur și simplu apasă butonul de play lângă „prompt-engineering” pentru a porni acest modul, sau pornește toate modulele odată.

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
.\stop.ps1  # Numai acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Capturi de ecran ale aplicației

<img src="../../../translated_images/ro/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Panoul principal care arată toate cele 8 modele de inginerie a prompturilor cu caracteristicile și cazurile de utilizare*

## Explorarea modelelor

Interfața web îți permite să experimentezi cu diferite strategii de promptare. Fiecare model rezolvă probleme diferite – încearcă-le ca să vezi când strălucește fiecare abordare.

### Anticipare scăzută vs anticipare ridicată

Pune o întrebare simplă, precum „Cât este 15% din 200?” folosind anticipare scăzută. Vei primi un răspuns instant, direct. Acum pune ceva complex, de exemplu „Proiectează o strategie de caching pentru o API cu trafic ridicat” folosind anticipare ridicată. Privește cum modelul încetinește și oferă un raționament detaliat. Același model, aceeași structură a întrebării - dar promptul îi spune cât să gândească.
<img src="../../../translated_images/ro/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo de entuziasm redus" width="800"/>

*Calcul rapid cu raționament minim*

<img src="../../../translated_images/ro/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo de entuziasm ridicat" width="800"/>

*Strategie complexă de caching (2.8MB)*

### Executarea Sarcinilor (Prezentări pentru Unelte)

Fluxurile de lucru multi-pas beneficiază de planificare anticipată și narare a progresului. Modelul schițează ce va face, povestește fiecare pas, apoi rezumă rezultatele.

<img src="../../../translated_images/ro/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo Executarea Sarcinilor" width="800"/>

*Crearea unui endpoint REST cu narare pas-cu-pas (3.9MB)*

### Cod care se Auto-Reflectă

Încearcă „Creează un serviciu de validare email”. În loc să genereze doar cod și să se oprească, modelul generează, evaluează după criterii de calitate, identifică slăbiciunile și îmbunătățește. Vei vedea cum iterează până când codul atinge standardele de producție.

<img src="../../../translated_images/ro/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo Cod care se Auto-Reflectă" width="800"/>

*Serviciu complet de validare email (5.2MB)*

### Analiză Structurată

Recenziile de cod necesită cadre consistente de evaluare. Modelul analizează codul folosind categorii fixe (corectitudine, practici, performanță, securitate) cu niveluri de severitate.

<img src="../../../translated_images/ro/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo Analiză Structurată" width="800"/>

*Recenzie a codului bazată pe cadru*

### Conversații Multi-Rundă

Întreabă „Ce este Spring Boot?” apoi imediat continuă cu „Arată-mi un exemplu”. Modelul își amintește prima întrebare și îți oferă un exemplu specific Spring Boot. Fără memorie, a doua întrebare ar fi prea vagă.

<img src="../../../translated_images/ro/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo Conversații Multi-Rundă" width="800"/>

*Păstrarea contextului între întrebări*

### Raționament Pas-cu-Pas

Alege o problemă de matematică și încearcă atât raționamentul pas-cu-pas cât și entuziasmul redus. Entuziasmul redus îți oferă doar răspunsul — rapid, dar opac. Raționamentul pas-cu-pas îți arată fiecare calcul și decizie.

<img src="../../../translated_images/ro/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo Raționament Pas-cu-Pas" width="800"/>

*Problemă de matematică cu pași clari*

### Ieșire Restricționată

Când ai nevoie de formate sau număr fix de cuvinte, acest tipar impune respectarea strictă. Încearcă să generezi un rezumat cu exact 100 de cuvinte în format listă cu puncte.

<img src="../../../translated_images/ro/constrained-output-demo.567cc45b75da1633.webp" alt="Demo Ieșire Restricționată" width="800"/>

*Rezumat de machine learning cu control de format*

## Ce Înveți Cu Adevărat

**Efortul de Raționament Schimbă Totul**

GPT-5.2 îți permite să controlezi efortul computațional prin prompturi. Efort redus înseamnă răspunsuri rapide cu explorare minimă. Efort ridicat înseamnă că modelul ia timp să gândească profund. Înveți să potrivești efortul cu complexitatea sarcinii — să nu pierzi vremea pe întrebări simple, dar nici să nu te grăbești în decizii complexe.

**Structura Ghidează Comportamentul**

Observi tagurile XML din prompturi? Nu sunt decorative. Modelele urmează instrucțiuni structurate mai fiabil decât text liber. Când ai nevoie de procese multi-pas sau logică complexă, structura ajută modelul să știe unde se află și ce urmează.

<img src="../../../translated_images/ro/prompt-structure.a77763d63f4e2f89.webp" alt="Structura Promptului" width="800"/>

*Anatomia unui prompt bine structurat cu secțiuni clare și organizare în stil XML*

**Calitatea Prin Auto-Evaluare**

Tiparele care se auto-reflectă funcționează prin explicarea clară a criteriilor de calitate. În loc să speri că modelul „face bine”, îi spui exact ce înseamnă „bine”: logică corectă, gestionare a erorilor, performanță, securitate. Modelul poate apoi să-și evalueze propriul output și să-l îmbunătățească. Astfel, generarea codului devine un proces, nu o loterie.

**Contextul Este Finite**

Conversațiile multi-rundă funcționează prin includerea istoriei mesajelor în fiecare cerere. Dar există o limită — fiecare model are un număr maxim de tokeni. Pe măsură ce conversațiile cresc, vei avea nevoie de strategii pentru a păstra context relevant fără să atingi plafonul. Acest modul îți arată cum funcționează memoria; mai târziu vei învăța când să rezumi, când să uiți și când să recuperezi.

## Pașii Următori

**Următorul Modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigare:** [← Anterior: Modul 01 - Introducere](../01-introduction/README.md) | [Înapoi la Principal](../README.md) | [Următorul: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere automată AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări eronate rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->