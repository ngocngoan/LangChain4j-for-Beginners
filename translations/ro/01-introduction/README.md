# Modul 01: Început cu LangChain4j

## Cuprins

- [Ce vei învăța](../../../01-introduction)
- [Precondiții](../../../01-introduction)
- [Înțelegerea problemei esențiale](../../../01-introduction)
- [Înțelegerea token-urilor](../../../01-introduction)
- [Cum funcționează memoria](../../../01-introduction)
- [Cum folosește acesta LangChain4j](../../../01-introduction)
- [Deplasează infrastructura Azure OpenAI](../../../01-introduction)
- [Rulează aplicația local](../../../01-introduction)
- [Folosirea aplicației](../../../01-introduction)
  - [Chat fără stare (panoul din stânga)](../../../01-introduction)
  - [Chat cu stare (panoul din dreapta)](../../../01-introduction)
- [Următorii pași](../../../01-introduction)

## Ce vei învăța

Dacă ai completat începutul rapid, ai văzut cum să trimiți prompturi și să obții răspunsuri. Aceasta este fundația, dar aplicațiile reale au nevoie de mai mult. Acest modul te învață cum să construiești AI conversațional care își amintește contextul și menține starea – diferența între o demonstrație ocazională și o aplicație pregătită pentru producție.

Vom folosi GPT-5.2 de la Azure OpenAI pe tot parcursul acestui ghid, deoarece capacitățile sale avansate de raționare fac comportamentul diferitelor tipare mai evident. Când adaugi memorie, vei vedea clar diferența. Acest lucru face mai ușor să înțelegi ce aduce fiecare componentă aplicației tale.

Vei construi o aplicație care demonstrează ambele tipare:

**Chat fără stare** - Fiecare cerere este independentă. Modelul nu are memorie a mesajelor anterioare. Acesta este tiparul folosit în începutul rapid.

**Conversație cu stare** - Fiecare cerere include istoricul conversației. Modelul menține contextul pe parcursul mai multor schimburi. Acesta este ceea ce cer aplicațiile de producție.

## Precondiții

- Abonament Azure cu acces la Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notă:** Java, Maven, Azure CLI și Azure Developer CLI (azd) sunt preinstalate în devcontainer-ul furnizat.

> **Notă:** Acest modul folosește GPT-5.2 pe Azure OpenAI. Implementarea este configurată automat prin `azd up` - nu modificați numele modelului în cod.

## Înțelegerea problemei esențiale

Modelele de limbaj sunt fără stare. Fiecare apel API este independent. Dacă trimiți „Numele meu este John” și apoi întrebi „Cum mă numesc?”, modelul nu știe că tocmai te-ai prezentat. Tratează fiecare cerere ca și cum ar fi prima conversație pe care ai avut-o vreodată.

Acest lucru este bine pentru întrebări și răspunsuri simple, dar inutil pentru aplicații reale. Boții de serviciu clienți trebuie să-și amintească ce le-ai spus. Asistenții personali au nevoie de context. Orice conversație multi-turn necesită memorie.

<img src="../../../translated_images/ro/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversații fără stare vs cu stare" width="800"/>

*Diferența dintre conversațiile fără stare (apeluri independente) și cele cu stare (conștiente de context)*

## Înțelegerea token-urilor

Înainte de a începe conversațiile, este important să înțelegi token-urile – unitățile de bază de text pe care le procesează modelele de limbaj:

<img src="../../../translated_images/ro/token-explanation.c39760d8ec650181.webp" alt="Explicația token-ului" width="800"/>

*Exemplu despre cum textul este împărțit în token-uri - "I love AI!" devine 4 unități separate de procesare*

Token-urile sunt modul în care modelele AI măsoară și procesează textul. Cuvintele, punctuația și chiar spațiile pot fi token-uri. Modelul tău are o limită a câtor token-uri poate procesa odată (400.000 pentru GPT-5.2, cu până la 272.000 token-uri de intrare și 128.000 token-uri de ieșire). Înțelegerea token-urilor te ajută să gestionezi lungimea conversației și costurile.

## Cum funcționează memoria

Memoria chat rezolvă problema lipsei stării, menținând istoricul conversației. Înainte de a trimite cererea către model, cadrul adaugă mesajele relevante anterioare. Când întrebi „Cum mă numesc?”, sistemul trimite de fapt întregul istoric al conversației, permițând modelului să vadă că ai spus anterior „Numele meu este John.”

LangChain4j oferă implementări de memorie care gestionează acest aspect automat. Alegi câte mesaje să păstrezi, iar cadrul gestionează fereastra de context.

<img src="../../../translated_images/ro/memory-window.bbe67f597eadabb3.webp" alt="Concept fereastră de memorie" width="800"/>

*MessageWindowChatMemory menține o fereastră glisantă cu mesajele recente, eliminând automat mesajele vechi*

## Cum folosește acesta LangChain4j

Acest modul extinde începutul rapid prin integrarea Spring Boot și adăugarea memoriei conversației. Iată cum se potrivesc piesele:

**Dependențe** - Adaugă două biblioteci LangChain4j:

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
  
**Modelul de chat** - Configurează Azure OpenAI ca bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```
  
Builder-ul citește credențialele din variabilele de mediu setate de `azd up`. Setarea `baseUrl` cu endpoint-ul tău Azure face ca clientul OpenAI să funcționeze cu Azure OpenAI.

**Memoria conversației** - Urmărește istoricul chat-ului cu MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
Creează memoria cu `withMaxMessages(10)` pentru a păstra ultimele 10 mesaje. Adaugă mesaje de utilizator și AI cu învelitori tipizate: `UserMessage.from(text)` și `AiMessage.from(text)`. Recuperează istoricul cu `memory.messages()` și trimite-l către model. Serviciul stochează instanțe de memorie separate pe ID de conversație, permițând mai multor utilizatori să converseze simultan.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) și întreabă:
> - "Cum decide MessageWindowChatMemory ce mesaje să elimine când fereastra este plină?"
> - "Pot implementa un stocaj personalizat al memoriei folosind o bază de date în loc de memorie în RAM?"
> - "Cum aș adăuga sumarizare pentru a comprima istoricul vechi al conversației?"

Endpoint-ul de chat fără stare ocolește complet memoria - doar `chatModel.chat(prompt)` ca în începutul rapid. Endpoint-ul cu stare adaugă mesaje în memorie, recuperează istoricul și include acel context la fiecare cerere. Aceeași configurație de model, tipare diferite.

## Deplasează infrastructura Azure OpenAI

**Bash:**  
```bash
cd 01-introduction
azd up  # Selectați abonamentul și locația (eastus2 recomandat)
```
  
**PowerShell:**  
```powershell
cd 01-introduction
azd up  # Selectați abonamentul și locația (eastus2 recomandat)
```
  
> **Notă:** Dacă întâmpini o eroare de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), doar rulează din nou `azd up`. Resursele Azure pot fi încă în proces de configurare în fundal, iar reîncercarea permite finalizarea implementării odată ce resursele ajung într-o stare terminală.

Aceasta va:  
1. Implementa resursa Azure OpenAI cu modelele GPT-5.2 și text-embedding-3-small  
2. Va genera automat fișierul `.env` în rădăcina proiectului cu credențiale  
3. Va seta toate variabilele de mediu necesare

**Ai probleme cu implementarea?** Consultă [README-ul infrastructurii](infra/README.md) pentru depanare detaliată, inclusiv conflicte la numele subdomeniului, pași pentru implementare manuală din Azure Portal și ghid de configurare a modelului.

**Verifică dacă implementarea a reușit:**

**Bash:**  
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```
  
> **Notă:** Comanda `azd up` generează automat fișierul `.env`. Dacă ai nevoie să îl actualizezi ulterior, poți edita manual fișierul `.env` sau îl poți regenera rulând:  
>
> **Bash:**  
> ```bash
> cd ..
> bash .azd-env.sh
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```
  
## Rulează aplicația local

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu credențialele Azure:

**Bash:**  
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Pornește aplicațiile:**

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în bara de activități din stânga VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard poți:  
- Vizualiza toate aplicațiile Spring Boot disponibile în spațiul de lucru  
- Porni/opri aplicațiile cu un singur click  
- Vizualiza jurnalele aplicațiilor în timp real  
- Monitoriza statusul aplicațiilor  

Apasă pur și simplu butonul play lângă „introduction” pentru a porni acest modul sau pornește toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 01-introduction
./start.sh
```
  
**PowerShell:**  
```powershell
cd 01-introduction
.\start.ps1
```
  
Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor genera JAR-urile dacă acestea nu există.

> **Notă:** Dacă preferi să compilzi manual toate modulele înainte de a porni:  
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
  
Accesează http://localhost:8080 în browserul tău.

**Pentru a opri:**

**Bash:**  
```bash
./stop.sh  # Numai acest modul
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

Aplicația oferă o interfață web cu două implementări de chat afișate una lângă alta.

<img src="../../../translated_images/ro/home-screen.121a03206ab910c0.webp" alt="Ecran principal al aplicației" width="800"/>

*Tablou de bord care afișează opțiunile Simple Chat (fără stare) și Conversational Chat (cu stare)*

### Chat fără stare (panoul din stânga)

Încearcă asta prima dată. Spune „Numele meu este John” și apoi imediat întreabă „Cum mă numesc?” Modelul nu va ține minte pentru că fiecare mesaj este independent. Aceasta demonstrează problema esențială a integrării simple a modelelor de limbaj – fără context conversațional.

<img src="../../../translated_images/ro/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo chat fără stare" width="800"/>

*AI nu îți amintește numele din mesajul anterior*

### Chat cu stare (panoul din dreapta)

Acum încearcă aceeași secvență aici. Spune „Numele meu este John” și apoi „Cum mă numesc?” De data aceasta își amintește. Diferența este MessageWindowChatMemory – menține istoricul conversației și îl include la fiecare cerere. Așa funcționează AI conversațional în producție.

<img src="../../../translated_images/ro/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo chat cu stare" width="800"/>

*AI își amintește numele tău din conversația anterioară*

Ambele panouri folosesc același model GPT-5.2. Singura diferență este memoria. Astfel, este clar ce aduce memoria aplicației tale și de ce este esențială pentru cazuri reale.

## Următorii pași

**Următorul modul:** [02-prompt-engineering - Ingineria prompturilor cu GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigare:** [← Anterior: Modul 00 - Început rapid](../00-quick-start/README.md) | [Înapoi la principal](../README.md) | [Următor: Modul 02 - Ingineria prompturilor →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să țineți cont că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un translator uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->