# Modului 04: Agenți AI cu instrumente

## Cuprins

- [Ce vei învăța](../../../04-tools)
- [Prerechizite](../../../04-tools)
- [Înțelegerea Agenților AI cu instrumente](../../../04-tools)
- [Cum funcționează apelarea instrumentelor](../../../04-tools)
  - [Definiții ale instrumentelor](../../../04-tools)
  - [Luarea deciziilor](../../../04-tools)
  - [Execuție](../../../04-tools)
  - [Generarea răspunsului](../../../04-tools)
  - [Arhitectură: Configurare automată Spring Boot](../../../04-tools)
- [Lanțuire de instrumente](../../../04-tools)
- [Rulează aplicația](../../../04-tools)
- [Folosirea aplicației](../../../04-tools)
  - [Încearcă utilizarea simplă a instrumentelor](../../../04-tools)
  - [Testează lanțuirea instrumentelor](../../../04-tools)
  - [Vezi fluxul conversației](../../../04-tools)
  - [Experimentează cu cereri diferite](../../../04-tools)
- [Concepte cheie](../../../04-tools)
  - [Modelul ReAct (Raționare și Acțiune)](../../../04-tools)
  - [Descrierile instrumentelor contează](../../../04-tools)
  - [Managementul sesiunii](../../../04-tools)
  - [Gestionarea erorilor](../../../04-tools)
- [Instrumente disponibile](../../../04-tools)
- [Când să folosești agenți bazati pe instrumente](../../../04-tools)
- [Instrumente vs RAG](../../../04-tools)
- [Pașii următori](../../../04-tools)

## Ce vei învăța

Până acum, ai învățat cum să porți conversații cu AI, să structurezi prompturi eficient și să ancorezi răspunsurile în documentele tale. Dar există o limitare fundamentală: modelele de limbaj pot genera doar text. Nu pot verifica vremea, face calcule, interoga baze de date sau interacționa cu sisteme externe.

Instrumentele schimbă acest lucru. Dând modelului acces la funcții pe care le poate apela, îl transformi din generator de text într-un agent care poate lua acțiuni. Modelul decide când are nevoie de un instrument, care instrument să folosească și ce parametri să transmită. Codul tău execută funcția și returnează rezultatul. Modelul încorporează acel rezultat în răspunsul său.

## Prerechizite

- Completat [Modul 01 - Introducere](../01-introduction/README.md) (resurse Azure OpenAI implementate)
- Completarea modulelor anterioare este recomandată (acest modul face referire la [conceput RAG din Modulul 03](../03-rag/README.md) în comparația Instrumente vs RAG)
- Fișier `.env` în directorul rădăcină cu acreditările Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai terminat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo.

## Înțelegerea Agenților AI cu instrumente

> **📝 Notă:** Termenul „agenți” în acest modul se referă la asistenți AI îmbunătățiți cu capabilități de apelare a instrumentelor. Aceasta este diferit de pattern-urile **Agentic AI** (agenți autonomi cu planificare, memorie și raționament în pași multipli) pe care le vom acoperi în [Modulul 05: MCP](../05-mcp/README.md).

Fără instrumente, un model de limbaj poate doar genera text din datele sale de antrenament. Întreabă-l despre vremea actuală și trebuie să ghicească. Dă-i instrumente și poate apela o API de vreme, poate face calcule sau interoga o bază de date — apoi integrează acele rezultate reale în răspunsul său.

<img src="../../../translated_images/ro/what-are-tools.724e468fc4de64da.webp" alt="Fără instrumente vs Cu instrumente" width="800"/>

*Fără instrumente modelul poate doar ghici — cu instrumente poate apela API-uri, executa calcule și returna date în timp real.*

Un agent AI cu instrumente urmează un model **Raționare și Acțiune (ReAct)**. Modelul nu răspunde doar — gândește ce are nevoie, acționează apelând un instrument, observă rezultatul și apoi decide dacă acționează din nou sau oferă răspunsul final:

1. **Raționează** — Agentul analizează întrebarea utilizatorului și determină ce informații sunt necesare
2. **Acționează** — Agentul selectează instrumentul potrivit, generează parametrii corecți și îl apelează
3. **Observă** — Agentul primește rezultatul instrumentului și îl evaluează
4. **Repetă sau răspunde** — Dacă este nevoie de mai multe date, agentul revine la pasul anterior; altfel, compune un răspuns în limbaj natural

<img src="../../../translated_images/ro/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Modelul ReAct" width="800"/>

*Ciclul ReAct — agentul raționează ce să facă, acționează apelând un instrument, observă rezultatul și repetă până poate livra răspunsul final.*

Acest lucru se întâmplă automat. Definiți instrumentele și descrierile lor. Modelul se ocupă de luarea deciziilor despre când și cum să le folosească.

## Cum funcționează apelarea instrumentelor

### Definiții ale instrumentelor

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Definiți funcții cu descrieri clare și specificații pentru parametri. Modelul vede aceste descrieri în promptul său de sistem și înțelege ce face fiecare instrument.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logica ta de căutare meteo
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

Diagramul de mai jos descrie fiecare adnotare și arată cum fiecare parte ajută AI să înțeleagă când să apeleze instrumentul și ce argumente să transmită:

<img src="../../../translated_images/ro/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia definițiilor instrumentelor" width="800"/>

*Anatomia unei definiții de instrument — @Tool îi spune AI când să îl folosească, @P descrie fiecare parametru, iar @AiService le leagă pe toate împreună la pornire.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) și întreabă:
> - "Cum aș integra o API reală de vreme precum OpenWeatherMap în locul datelor mock?"
> - "Ce face o descriere bună a instrumentului care ajută AI să îl folosească corect?"
> - "Cum gestionez erorile API și limitele de rată în implementările instrumentelor?"

### Luarea deciziilor

Când un utilizator întreabă „Care e vremea în Seattle?”, modelul nu alege aleator un instrument. Compară intenția utilizatorului cu fiecare descriere de instrument la care are acces, acordă un scor pentru relevanță și selectează cea mai bună potrivire. Apoi generează un apel de funcție structurat cu parametrii corecți — în acest caz, `location` setat la `"Seattle"`.

Dacă niciun instrument nu se potrivește cererii utilizatorului, modelul răspunde din propria sa cunoaștere. Dacă mai multe instrumente coincid, alege pe cel mai specific.

<img src="../../../translated_images/ro/decision-making.409cd562e5cecc49.webp" alt="Cum decide AI ce instrument să folosească" width="800"/>

*Modelul evaluează toate instrumentele disponibile față de intenția utilizatorului și selectează cea mai bună potrivire — de aceea contează să scrii descrieri clare și specifice pentru instrumente.*

### Execuție

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot configurează automat interfața declarativă `@AiService` cu toate instrumentele înregistrate, iar LangChain4j execută apelurile instrumentelor automat. În spate, un apel complet al unui instrument trece prin șase etape — de la întrebarea utilizatorului în limbaj natural până la un răspuns tot în limbaj natural:

<img src="../../../translated_images/ro/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxul apelării instrumentului" width="800"/>

*Fluxul end-to-end — utilizatorul pune o întrebare, modelul selectează un instrument, LangChain4j îl execută, iar modelul integrează rezultatul într-un răspuns natural.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) și întreabă:
> - "Cum funcționează modelul ReAct și de ce este eficient pentru agenții AI?"
> - "Cum decide agentul ce instrument să folosească și în ce ordine?"
> - "Ce se întâmplă dacă execuția unui instrument eșuează - cum ar trebui să gestionez erorile în mod robust?"

### Generarea răspunsului

Modelul primește datele meteo și le formatează într-un răspuns în limbaj natural pentru utilizator.

### Arhitectură: Configurare automată Spring Boot

Acest modul folosește integrarea LangChain4j cu Spring Boot prin interfețe declarative `@AiService`. La pornire, Spring Boot descoperă fiecare `@Component` care conține metode `@Tool`, bean-ul tău `ChatModel` și `ChatMemoryProvider` — apoi le leagă pe toate într-o singură interfață `Assistant` fără boilerplate.

<img src="../../../translated_images/ro/spring-boot-wiring.151321795988b04e.webp" alt="Arhitectura configurării automate Spring Boot" width="800"/>

*Interfața @AiService leagă ChatModel, componentele de instrumente și provider-ul de memorie — Spring Boot se ocupă de întreaga configurare automat.*

Beneficii cheie ale acestei abordări:

- **Configurare automată Spring Boot** — ChatModel și instrumentele sunt injectate automat
- **Pattern @MemoryId** — Management automat al memoriei bazat pe sesiuni
- **Instanță unică** — Assistant creat o singură dată și reutilizat pentru performanță mai bună
- **Execuție tip-safe** — Metode Java apelate direct cu conversie de tip
- **Orchestrare multi-turn** — Gestionează automat lanțuirea instrumentelor
- **Zero boilerplate** — Fără apeluri manuale `AiServices.builder()` sau memorie HashMap

Abordările alternative (manual `AiServices.builder()`) necesită mai mult cod și pierd beneficiile integrării Spring Boot.

## Lanțuire de instrumente

**Lanțuirea instrumentelor** — Puterea reală a agenților bazati pe instrumente se vede când o singură întrebare necesită mai multe instrumente. Întreabă „Care e vremea în Seattle în Fahrenheit?” și agentul lanțuiește automat două instrumente: mai întâi apelează `getCurrentWeather` pentru temperatura în Celsius, apoi trece acea valoare la `celsiusToFahrenheit` pentru conversie — totul într-un singur pas de conversație.

<img src="../../../translated_images/ro/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplu de lanțuire a instrumentelor" width="800"/>

*Lanțuirea instrumentelor în acțiune — agentul apelează mai întâi getCurrentWeather, apoi trece rezultatul în Celsius către celsiusToFahrenheit și oferă un răspuns combinat.*

**Eșecuri grațioase** — Cere vremea într-un oraș care nu este în datele mock. Instrumentul returnează un mesaj de eroare, iar AI explică că nu poate ajuta în loc să se blocheze. Instrumentele eșuează în siguranță. Diagramă de mai jos compară două abordări — cu gestionare corectă a erorilor agentul prinde excepția și răspunde ajutător, fără ea întreaga aplicație se blochează:

<img src="../../../translated_images/ro/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fluxul gestionării erorilor" width="800"/>

*Când un instrument eșuează, agentul prinde eroarea și răspunde cu o explicație utilă în loc să se blocheze.*

Aceasta se întâmplă într-un singur pas de conversație. Agentul orchestrează automat mai multe apeluri de instrumente.

## Rulează aplicația

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure (creat în Modulul 01). Rulează din directorul modulului (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din directorul rădăcină (așa cum s-a descris în Modulul 01), acest modul rulează deja pe portul 8084. Poți sări peste comenzile de start de mai jos și merge direct la http://localhost:8084.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul dev include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O găsești în bara de activități din stânga în VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard poți:
- Vezi toate aplicațiile Spring Boot disponibile în workspace
- Porni/opri aplicațiile cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor

Pur și simplu apasă butonul play de lângă „tools” pentru a porni acest modul, sau pornește toate modulele simultan.

Iată cum arată Spring Boot Dashboard în VS Code:

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

Sau pornește doar acest modul:

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` rădăcină și vor construi JAR-urile dacă nu există.

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

Deschide http://localhost:8084 în browserul tău.

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

## Folosirea aplicației

Aplicația oferă o interfață web unde poți interacționa cu un agent AI care are acces la instrumente pentru vreme și conversie de temperatură. Iată cum arată interfața — include exemple rapide de început și un panou de chat pentru trimiterea cererilor:
<a href="images/tools-homepage.png"><img src="../../../translated_images/ro/tools-homepage.4b4cd8b2717f9621.webp" alt="Interfața Instrumentelor Agentului AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interfața Instrumentelor Agentului AI - exemple rapide și interfață de chat pentru interacțiunea cu instrumentele*

### Încearcă Utilizarea Simplă a Instrumentelor

Începe cu o solicitare simplă: „Convertește 100 de grade Fahrenheit în Celsius”. Agentul recunoaște că are nevoie de instrumentul de conversie a temperaturii, îl apelează cu parametrii corecți și returnează rezultatul. Observă cât de natural se simte - nu ai specificat ce instrument să folosești sau cum să-l apelezi.

### Testează Lanțurile de Instrumente

Acum încearcă ceva mai complex: „Cum este vremea în Seattle și convertește-o în Fahrenheit?” Urmărește cum agentul parcurge acest proces pas cu pas. Mai întâi obține vremea (care returnează Celsius), recunoaște că trebuie să convertească în Fahrenheit, apelează instrumentul de conversie și combină ambele rezultate într-un singur răspuns.

### Vezi Fluxul Conversației

Interfața de chat păstrează istoricul conversațiilor, permițându-ți să ai interacțiuni pe mai multe runde. Poți vedea toate întrebările și răspunsurile anterioare, făcând ușor de urmărit conversația și de înțeles cum construiește agentul contextul pe parcursul mai multor schimburi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ro/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversație cu Apeluri Multiple la Instrumente" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversație pe mai multe ture care arată conversii simple, verificări meteo și lanțuri de instrumente*

### Experimentează cu Solicitări Diferite

Încearcă diferite combinații:
- Verificări meteo: „Cum este vremea în Tokyo?”
- Conversii de temperatură: „Ce este 25°C în Kelvin?”
- Întrebări combinate: „Verifică vremea în Paris și spune-mi dacă este peste 20°C”

Observă cum agentul interpretează limbajul natural și îl mapează la apeluri adecvate ale instrumentelor.

## Concepte Cheie

### Tiparul ReAct (Raționament și Acțiune)

Agentul alternează între a raționa (a decide ce să facă) și a acționa (a folosi instrumentele). Acest tipar permite rezolvarea autonomă a problemelor, nu doar răspunsuri la instrucțiuni.

### Descrierile Instrumentelor Contează

Calitatea descrierilor instrumentelor afectează direct cât de bine le folosește agentul. Descrierile clare și specifice ajută modelul să înțeleagă când și cum să apeleze fiecare instrument.

### Gestionarea Sesiunii

Anotarea `@MemoryId` permite gestionarea automată a memoriei bazate pe sesiuni. Fiecare ID de sesiune primește propria instanță `ChatMemory` gestionată de bean-ul `ChatMemoryProvider`, astfel încât mai mulți utilizatori pot interacționa simultan cu agentul fără ca conversațiile lor să se amestece. Diagrama următoare arată cum mai mulți utilizatori sunt direcționați către stocări de memorie izolate în funcție de ID-urile sesiunilor lor:

<img src="../../../translated_images/ro/session-management.91ad819c6c89c400.webp" alt="Gestionarea Sesiunii cu @MemoryId" width="800"/>

*Fiecare ID de sesiune corespunde unui istoric de conversație izolat — utilizatorii nu văd niciodată mesajele celorlalți.*

### Gestionarea Erorilor

Instrumentele pot eșua — API-urile pot expira, parametrii pot fi invalizi, serviciile externe pot ceda. Agenții din producție au nevoie de gestionare a erorilor astfel încât modelul să poată explica problemele sau să încerce alternative în loc să se blocheze întregul sistem. Când un instrument aruncă o excepție, LangChain4j o prinde și transmite mesajul de eroare modelului, care apoi poate explica problema în limbaj natural.

## Instrumente Disponibile

Diagrama de mai jos arată ecosistemul larg de instrumente pe care le poți construi. Acest modul demonstrează instrumente pentru vreme și temperatură, dar același tipar `@Tool` funcționează pentru orice metodă Java — de la interogări în baze de date până la procesarea plăților.

<img src="../../../translated_images/ro/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecosistemul Instrumentelor" width="800"/>

*Orice metodă Java anotată cu @Tool devine disponibilă pentru AI — tiparul se extinde la baze de date, API-uri, email, operațiuni pe fișiere și altele.*

## Când să Folosești Agenți Bazati pe Instrumente

Nu toate solicitările necesită instrumente. Decizia depinde dacă AI-ul trebuie să interacționeze cu sisteme externe sau poate răspunde din propria sa cunoaștere. Ghidul următor rezumă când instrumentele adaugă valoare și când sunt inutile:

<img src="../../../translated_images/ro/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Când să Folosești Instrumente" width="800"/>

*Un ghid rapid de decizie — instrumentele sunt pentru date în timp real, calcule și acțiuni; cunoștințele generale și sarcinile creative nu au nevoie de ele.*

## Instrumente vs RAG

Modulele 03 și 04 extind ambele ceea ce poate face AI-ul, dar în moduri fundamental diferite. RAG oferă modelului acces la **cunoștințe** prin recuperarea de documente. Instrumentele oferă modelului abilitatea de a lua **acțiuni** apelând funcții. Diagrama de mai jos compară aceste două abordări alăturat — de la modul în care funcționează fiecare flux de lucru până la compromisurile dintre ele:

<img src="../../../translated_images/ro/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparație Instrumente vs RAG" width="800"/>

*RAG preia informații din documente statice — Instrumentele execută acțiuni și aduc date dinamice, în timp real. Multe sisteme de producție combină ambele.*

În practică, multe sisteme de producție combină ambele abordări: RAG pentru fundamentarea răspunsurilor în documentația ta, și Instrumentele pentru a prelua date live sau a efectua operațiuni.

## Pașii Următori

**Următorul Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigare:** [← Anterior: Modul 03 - RAG](../03-rag/README.md) | [Înapoi la Principal](../README.md) | [Următorul: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind un serviciu de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să țineți cont că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru orice neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->