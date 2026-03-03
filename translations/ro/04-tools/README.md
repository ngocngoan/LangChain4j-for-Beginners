# Modulul 04: Agenți AI cu unelte

## Cuprins

- [Ce vei învăța](../../../04-tools)
- [Prerechizite](../../../04-tools)
- [Înțelegerea agenților AI cu unelte](../../../04-tools)
- [Cum funcționează apelarea uneltelor](../../../04-tools)
  - [Definiții pentru unelte](../../../04-tools)
  - [Luarea deciziilor](../../../04-tools)
  - [Executarea](../../../04-tools)
  - [Generarea răspunsului](../../../04-tools)
  - [Arhitectură: Auto-conectare Spring Boot](../../../04-tools)
- [Lanțuirea uneltelor](../../../04-tools)
- [Rulează aplicația](../../../04-tools)
- [Utilizarea aplicației](../../../04-tools)
  - [Încearcă utilizarea simplă a unei unelte](../../../04-tools)
  - [Testează lanțuirea uneltelor](../../../04-tools)
  - [Vezi fluxul conversației](../../../04-tools)
  - [Experimentează cu cereri diferite](../../../04-tools)
- [Concepte cheie](../../../04-tools)
  - [Tiparul ReAct (Raționament și Acțiune)](../../../04-tools)
  - [Importanța descrierilor uneltelor](../../../04-tools)
  - [Gestionarea sesiunii](../../../04-tools)
  - [Gestionarea erorilor](../../../04-tools)
- [Unelte disponibile](../../../04-tools)
- [Când să folosești agenți bazati pe unelte](../../../04-tools)
- [Unelte vs RAG](../../../04-tools)
- [Pașii următori](../../../04-tools)

## Ce vei învăța

Până acum, ai învățat cum să porți conversații cu AI, să structurezi prompturi eficient și să ancorezi răspunsurile în documentele tale. Însă există o limitare fundamentală: modelele de limbaj pot genera doar text. Nu pot verifica vremea, face calcule, interoga baze de date sau interacționa cu sisteme externe.

Uneltele schimbă acest lucru. Oferind modelului acces la funcții pe care le poate apela, îl transformi din generator de text într-un agent care poate întreprinde acțiuni. Modelul decide când are nevoie de o unealtă, ce unealtă să folosească și ce parametri să trimită. Codul tău execută funcția și returnează rezultatul. Modelul încorporează acest rezultat în răspunsul său.

## Prerechizite

- Finalizarea [Modulului 01 - Introducere](../01-introduction/README.md) (resurse Azure OpenAI implementate)
- Recomandat să fi terminat modulele anterioare (acest modul face referire la [conceptele RAG din Modulul 03](../03-rag/README.md) în comparația Unelte vs RAG)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai terminat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea agenților AI cu unelte

> **📝 Notă:** Termenul „agenți” în acest modul se referă la asistenți AI îmbunătățiți cu capabilități de apelare de unelte. Acest lucru este diferit de tiparele **Agentic AI** (agenți autonomi cu planificare, memorie și raționament în mai mulți pași) pe care le vom acoperi în [Modulul 05: MCP](../05-mcp/README.md).

Fără unelte, un model de limbaj poate doar genera text din datele sale de antrenament. Întreabă-l despre vremea curentă, și trebuie să ghicească. Oferă-i unelte și poate apela un API de vreme, face calcule sau interoga o bază de date — apoi împletește acele rezultate reale în răspunsul său.

<img src="../../../translated_images/ro/what-are-tools.724e468fc4de64da.webp" alt="Fără unelte vs cu unelte" width="800"/>

*Fără unelte modelul doar ghicește — cu unelte poate apela API-uri, efectua calcule și returna date în timp real.*

Un agent AI cu unelte urmează un tipar **Raționare și Acțiune (ReAct)**. Modelul nu doar răspunde — gândește la ce are nevoie, acționează apelând o unealtă, observă rezultatul și apoi decide dacă mai acționează sau livrează răspunsul final:

1. **Raționează** — agentul analizează întrebarea utilizatorului și determină ce informații îi sunt necesare
2. **Acționează** — agentul selectează unealta potrivită, generează parametrii corecți și o apelează
3. **Observă** — agentul primește rezultatul uneltei și îl evaluează
4. **Repetă sau Răspunde** — dacă sunt necesare mai multe date, agentul revine la pasul 1; altfel compune un răspuns în limbaj natural

<img src="../../../translated_images/ro/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Tiparul ReAct" width="800"/>

*Ciclul ReAct — agentul raționează ce să facă, acționează apelând o unealtă, observă rezultatul și repetă până poate livra răspunsul final.*

Acest proces se întâmplă automat. Tu definești uneltele și descrierile lor. Modelul se ocupă de luarea deciziilor despre când și cum să le folosească.

## Cum funcționează apelarea uneltelor

### Definiții pentru unelte

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definiți funcții cu descrieri clare și specificații pentru parametri. Modelul vede aceste descrieri în promptul său de sistem și înțelege ce face fiecare unealtă.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logica dvs. pentru căutarea vremii
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistentul este conectat automat de Spring Boot cu:
// - Bean-ul ChatModel
// - Toate metodele @Tool din clasele @Component
// - ChatMemoryProvider pentru gestionarea sesiunii
```
  
Diagrama de mai jos detaliază fiecare adnotare și arată cum fiecare piesă ajută AI-ul să înțeleagă când să apeleze unealta și ce argumente să transmită:

<img src="../../../translated_images/ro/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia definițiilor uneltelor" width="800"/>

*Anatomia definiției unei unelte — @Tool spune AI-ului când să o folosească, @P descrie fiecare parametru, iar @AiService conectează totul la pornire.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) și întreabă:
> - „Cum aș integra un API real de vreme ca OpenWeatherMap în loc de datele mock?”
> - „Ce face o descriere bună de unealtă care ajută AI să o folosească corect?”
> - „Cum gestionez erorile API și limitele de rată în implementările uneltelor?”

### Luarea deciziilor

Când un utilizator întreabă „Cum e vremea în Seattle?”, modelul nu alege o unealtă aleator. Compară intenția utilizatorului cu fiecare descriere de unealtă la care are acces, evaluează relevanța fiecăreia și selectează cea mai bună potrivire. Apoi generează un apel de funcție structurat cu parametrii corecți — în acest caz, setează `location` la `"Seattle"`.

Dacă nicio unealtă nu corespunde cererii utilizatorului, modelul răspunde din propria sa cunoaștere. Dacă mai multe unelte sunt potrivite, alege pe cea mai specifică.

<img src="../../../translated_images/ro/decision-making.409cd562e5cecc49.webp" alt="Cum decide AI ce unealtă să folosească" width="800"/>

*Modelul evaluează fiecare unealtă disponibilă față de intenția utilizatorului și alege cea mai bună potrivire — de aceea contează să scrii descrieri clare, specifice.*

### Executarea

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot leagă automat interfața declarativă `@AiService` cu toate uneltele înregistrate, iar LangChain4j execută apelurile către unelte automat. În spate, un apel complet către unealtă parcurge șase etape — de la întrebarea în limbaj natural a utilizatorului până la răspunsul final în limbaj natural:

<img src="../../../translated_images/ro/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxul apelării uneltei" width="800"/>

*Fluxul complet — utilizatorul pune o întrebare, modelul selectează o unealtă, LangChain4j o execută iar modelul încorporează rezultatul într-un răspuns natural.*

Dacă ai rulat [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) în Modulul 00, ai văzut deja acest tipar în acțiune — uneltele `Calculator` au fost apelate la fel. Diagrama de secvență de mai jos arată exact ce s-a întâmplat în spate în timpul acelei demonstrații:

<img src="../../../translated_images/ro/tool-calling-sequence.94802f406ca26278.webp" alt="Diagramă de secvență pentru apelarea uneletei" width="800"/>

*Bucle de apelare a uneltei din demo-ul Quick Start — `AiServices` trimite mesajul și schemele uneltelor la LLM, LLM răspunde cu un apel de funcție ca `add(42, 58)`, LangChain4j execută metoda `Calculator` local și aduce rezultatul pentru răspunsul final.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) și întreabă:
> - „Cum funcționează tiparul ReAct și de ce este eficient pentru agenții AI?”
> - „Cum decide agentul ce unealtă să folosească și în ce ordine?”
> - „Ce se întâmplă dacă execuția unei unelte eșuează – cum gestionez erorile robust?”

### Generarea răspunsului

Modelul primește datele despre vreme și le formatează într-un răspuns în limbaj natural pentru utilizator.

### Arhitectură: Auto-conectare Spring Boot

Acest modul folosește integrarea LangChain4j cu Spring Boot și interfețele declarative `@AiService`. La pornire, Spring Boot descoperă fiecare `@Component` cu metode `@Tool`, bean-ul tău `ChatModel` și `ChatMemoryProvider` — apoi le conectează pe toate într-o singură interfață `Assistant` fără cod redundant.

<img src="../../../translated_images/ro/spring-boot-wiring.151321795988b04e.webp" alt="Arhitectură Auto-Conectare Spring Boot" width="800"/>

*Interfața @AiService leagă împreună ChatModel, componentele uneltelor și provider-ul de memorie — Spring Boot gestionează automat toate conexiunile.*

Iată întreg ciclul de viață al unei cereri ilustrat printr-o diagramă de secvență — de la cererea HTTP, prin controller, service și proxy auto-conectat, până la execuția uneltei și înapoi:

<img src="../../../translated_images/ro/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Diagramă de secvență apelare unealtă Spring Boot" width="800"/>

*Ciclul complet al cererii Spring Boot — cererea HTTP trece prin controller și service către proxy-ul Assistant auto-conectat, care orchestrează LLM-ul și apelurile uneltelor automat.*

Beneficii cheie ale acestei abordări:

- **Auto-conectare Spring Boot** — ChatModel și uneltele injectate automat
- **Tiparul @MemoryId** — Gestionarea automată a memoriei bazate pe sesiuni
- **Instanță unică** — Assistant creat o singură dată și reutilizat pentru performanță mai bună
- **Executare tip-safe** — Apeluri directe de metode Java cu conversie de tipuri
- **Orchestrare multi-turn** — Gestionează automat lanțuirea uneltelor
- **Fără cod redundant** — Niciun apel manual `AiServices.builder()` sau HashMap de memorie

Abordările alternative (manuale cu `AiServices.builder()`) necesită mai mult cod și pierd beneficiile integrării Spring Boot.

## Lanțuirea uneltelor

**Lanțuirea uneltelor** — Puterea reală a agenților bazati pe unelte apare când o singură întrebare necesită mai multe unelte. Întreabă „Care e vremea în Seattle în Fahrenheit?” iar agentul leagă automat două unelte: mai întâi apelează `getCurrentWeather` pentru temperatura în Celsius, apoi transmite acea valoare către `celsiusToFahrenheit` pentru conversie — toate într-un singur tur de conversație.

<img src="../../../translated_images/ro/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplu de lanțuire unelte" width="800"/>

*Lanțuirea uneltelor în acțiune — agentul apelează mai întâi getCurrentWeather, apoi pasează rezultatul în Celsius către celsiusToFahrenheit și oferă un răspuns combinat.*

**Eșecuri grațioase** — Cere vremea într-un oraș care nu există în datele mock. Unealta returnează un mesaj de eroare, iar AI explică de ce nu poate ajuta, în loc să se prăbușească. Uneltele eșuează în siguranță. Diagrama de mai jos contrastează cele două abordări — cu gestionare corectă a erorilor, agentul prinde excepția și răspunde util, iar fără ea întreaga aplicație se blochează:

<img src="../../../translated_images/ro/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flux de gestionare erori" width="800"/>

*Când o unealtă eșuează, agentul prinde eroarea și răspunde cu o explicație utilă în loc să se blocheze.*

Acest lucru se întâmplă într-un singur tur de conversație. Agentul orchestrează autonom mai multe apeluri către unelte.

## Rulează aplicația

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în Modulul 01). Rulează acest comandă din directorul modulului (`04-tools/`):

**Bash:**  
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din directorul rădăcină (așa cum e descris în Modulul 01), acest modul rulează deja pe portul 8084. Poți sări peste comenzile de start de mai jos și să accesezi direct http://localhost:8084.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O vei găsi în bara de activități din stânga VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard poți:
- Vizualiza toate aplicațiile Spring Boot din workspace
- Porni/opri aplicații cu un singur clic
- Vedea jurnalele aplicației în timp real
- Monitoriza starea aplicațiilor

Apasă pe butonul play de lângă „tools” pentru a porni acest modul, sau pornește toate modulele simultan.

Așa arată Spring Boot Dashboard în VS Code:

<img src="../../../translated_images/ro/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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

Sau porniți doar acest modul:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Ambele scripturi încarcă automat variabilele de mediu din fișierul rădăcină `.env` și vor construi JAR-urile dacă acestea nu există.

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

Deschideți http://localhost:8084 în browserul dumneavoastră.

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

## Utilizarea aplicației

Aplicația oferă o interfață web unde puteți interacționa cu un agent AI care are acces la unelte pentru vreme și conversia temperaturii. Iată cum arată interfața — include exemple rapide de pornire și un panou de chat pentru trimiterea cererilor:

<a href="images/tools-homepage.png"><img src="../../../translated_images/ro/tools-homepage.4b4cd8b2717f9621.webp" alt="Interfața uneltelor agentului AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfața uneltelor agentului AI - exemple rapide și interfață de chat pentru interacțiunea cu uneltele*

### Încercați utilizarea simplă a uneltei

Începeți cu o cerere simplă: "Convertește 100 de grade Fahrenheit în Celsius". Agentul recunoaște că are nevoie de unealta de conversie a temperaturii, o apelează cu parametrii corecți și returnează rezultatul. Observați cât de natural se simte — nu ați specificat ce unealtă să folosească sau cum să o apeleze.

### Testați concatenarea uneltelor

Acum încercați ceva mai complex: "Care este vremea în Seattle și convertește-o în Fahrenheit?" Urmăriți cum agentul lucrează în pași. Mai întâi obține vremea (care returnează în Celsius), recunoaște că trebuie să convertească în Fahrenheit, apelează unealta de conversie și combină ambele rezultate într-un singur răspuns.

### Vedeți fluxul conversației

Interfața de chat păstrează istoricul conversației, permițându-vă să aveți interacțiuni în mai multe runde. Puteți vedea toate întrebările și răspunsurile anterioare, făcând ușor de urmărit conversația și de înțeles cum agentul construiește context de-a lungul schimburilor multiple.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ro/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversație cu mai multe apeluri de unealtă" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversație multi-turn care arată conversii simple, consultări de vreme și concatenarea uneltelor*

### Experimentați cu cereri diferite

Încercați diverse combinații:
- Consultări meteo: "Care este vremea în Tokyo?"
- Conversii de temperatură: "Cât este 25°C în Kelvin?"
- Cereri combinate: "Verifică vremea în Paris și spune-mi dacă este peste 20°C"

Observați cum agentul interpretează limbajul natural și îl mapează la apeluri adecvate ale uneltelor.

## Concepte cheie

### Modelul ReAct (Raționament și Acțiune)

Agentul alternează între raționament (deciderea ce trebuie făcut) și acțiune (utilizarea uneltelor). Acest model permite rezolvarea autonomă a problemelor, nu doar răspunsuri la instrucțiuni.

### Descrierile uneltelor contează

Calitatea descrierilor uneltelor afectează direct cât de bine le folosește agentul. Descrierile clare și specifice ajută modelul să înțeleagă când și cum să apeleze fiecare unealtă.

### Managementul sesiunilor

Anotarea `@MemoryId` permite gestionarea automată a memoriei bazate pe sesiuni. Fiecare ID de sesiune primește propria instanță `ChatMemory` gestionată de bean-ul `ChatMemoryProvider`, astfel încât mai mulți utilizatori pot interacționa simultan cu agentul fără ca conversațiile lor să se amestece. Diagrama următoare arată cum mai mulți utilizatori sunt direcționați către depozite de memorie izolate pe baza ID-urilor lor de sesiune:

<img src="../../../translated_images/ro/session-management.91ad819c6c89c400.webp" alt="Gestionarea sesiúnilor cu @MemoryId" width="800"/>

*Fiecare ID de sesiune corespunde unui istoric de conversație izolat — utilizatorii nu văd niciodată mesajele altora.*

### Gestionarea erorilor

Uneltele pot eșua — API-urile pot expira, parametrii pot fi invalidi, serviciile externe pot cădea. Agenții de producție au nevoie de gestionare a erorilor astfel încât modelul să poată explica problemele sau să încerce alternative în loc să se prăbușească aplicația. Când o unealtă aruncă o excepție, LangChain4j o interceptă și trimite mesajul de eroare înapoi modelului, care poate explica problema în limbaj natural.

## Unelte disponibile

Diagrama de mai jos arată ecosistemul larg de unelte pe care le puteți construi. Acest modul demonstrează unelte pentru vreme și temperatură, dar același model `@Tool` funcționează pentru orice metodă Java — de la interogări de baze de date până la procesarea plăților.

<img src="../../../translated_images/ro/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecosistemul uneltelor" width="800"/>

*Orice metodă Java anotată cu @Tool devine disponibilă AI-ului — modelul se extinde la baze de date, API-uri, email, operațiuni pe fișiere și altele.*

## Când să folosiți agenți cu unelte

Nu toate cererile necesită unelte. Decizia depinde dacă AI trebuie să interacționeze cu sisteme externe sau poate răspunde din propria cunoștință. Ghidul următor rezumă când uneltele adaugă valoare și când sunt inutile:

<img src="../../../translated_images/ro/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Când să folosești unelte" width="800"/>

*Ghid rapid de decizie — uneltele sunt pentru date în timp real, calcule și acțiuni; cunoștințe generale și sarcini creative nu necesită.*

## Unelte vs RAG

Modulele 03 și 04 extind ambele ce poate face AI-ul, dar în moduri fundamental diferite. RAG oferă modelului acces la **cunoaștere** prin recuperarea de documente. Uneltele oferă modelului abilitatea de a lua **acțiuni** apelând funcții. Diagrama de mai jos compară cele două abordări una lângă alta — de la cum operează fiecare flux de lucru până la compromisurile dintre ele:

<img src="../../../translated_images/ro/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparație unelte vs RAG" width="800"/>

*RAG recuperează informații din documente statice — Uneltele execută acțiuni și preiau date dinamice, în timp real. Multe sisteme de producție combină ambele.*

În practică, multe sisteme de producție combină ambele abordări: RAG pentru a funda răspunsurile în documentația ta și Uneltele pentru a prelua date live sau a efectua operațiuni.

## Pași următori

**Modul următor:** [05-mcp - Protocolul Contextului Modelului (MCP)](../05-mcp/README.md)

---

**Navigare:** [← Anterior: Modul 03 - RAG](../03-rag/README.md) | [Înapoi la principal](../README.md) | [Următor: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventuale neînțelegeri sau interpretări greșite care pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->