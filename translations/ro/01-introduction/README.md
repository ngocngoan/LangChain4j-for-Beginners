# Modulul 01: Primii pași cu LangChain4j

## Cuprins

- [Parcurgere video](../../../01-introduction)
- [Ce veți învăța](../../../01-introduction)
- [Precondiții](../../../01-introduction)
- [Înțelegerea problemei de bază](../../../01-introduction)
- [Înțelegerea token-urilor](../../../01-introduction)
- [Cum funcționează memoria](../../../01-introduction)
- [Cum utilizează acest modul LangChain4j](../../../01-introduction)
- [Implementați infrastructura Azure OpenAI](../../../01-introduction)
- [Rulați aplicația local](../../../01-introduction)
- [Folosirea aplicației](../../../01-introduction)
  - [Chat fără stare (Panoul din stânga)](../../../01-introduction)
  - [Conversație cu stare (Panoul din dreapta)](../../../01-introduction)
- [Pașii următori](../../../01-introduction)

## Parcurgere video

Urmăriți această sesiune live care explică cum să începeți cu acest modul: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Ce veți învăța

Dacă ați terminat ghidul rapid, ați văzut cum să trimiteți prompturi și să obțineți răspunsuri. Aceasta este fundația, dar aplicațiile reale au nevoie de mai mult. Acest modul vă învață cum să construiți AI conversațional care își amintește contextul și menține starea - diferența dintre un demo ocazional și o aplicație gata pentru producție.

Vom folosi GPT-5.2 de la Azure OpenAI pe tot parcursul acestui ghid, deoarece capacitățile sale avansate de raționament fac mai clar comportamentul diferitelor modele. Când adăugați memorie, veți vedea clar diferența. Acest lucru face mai ușor să înțelegeți ce aduce fiecare componentă aplicației dumneavoastră.

Veți construi o aplicație care demonstrează ambele modele:

**Chat fără stare** - Fiecare cerere este independentă. Modelul nu memorează mesajele anterioare. Acesta este modelul folosit în ghidul rapid.

**Conversație cu stare** - Fiecare cerere include istoricul conversației. Modelul menține contextul pe parcursul mai multor schimburi. Acesta este ceea ce cer aplicațiile de producție.

## Precondiții

- Abonament Azure cu acces la Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notă:** Java, Maven, Azure CLI și Azure Developer CLI (azd) sunt preinstalate în devcontainer-ul furnizat.

> **Notă:** Acest modul folosește GPT-5.2 pe Azure OpenAI. Implementarea este configurată automat prin `azd up` - nu modificați numele modelului în cod.

## Înțelegerea problemei de bază

Modelele de limbaj sunt fără stare. Fiecare apel API este independent. Dacă trimiteți "Mă numesc John" și apoi întrebați "Cum mă numesc?", modelul nu are habar că tocmai v-ați prezentat. Tratează fiecare cerere ca și cum ar fi prima conversație pe care o aveți vreodată.

Acest lucru este acceptabil pentru întrebări și răspunsuri simple, dar inutil pentru aplicații reale. Roboții de serviciu clienți trebuie să-și amintească ce le-ați spus. Asistenții personali au nevoie de context. Orice conversație cu mai multe schimburi necesită memorie.

<img src="../../../translated_images/ro/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversații Stateless vs Stateful" width="800"/>

*Diferența între conversațiile fără stare (apeluri independente) și cele cu stare (conștiente de context)*

## Înțelegerea token-urilor

Înainte de a începe conversațiile, este important să înțelegeți token-urile - unitățile de bază de text pe care modelele lingvistice le procesează:

<img src="../../../translated_images/ro/token-explanation.c39760d8ec650181.webp" alt="Explicație Token" width="800"/>

*Exemplu de cum textul este împărțit în token-uri - "I love AI!" devine 4 unități separate de procesare*

Token-urile sunt modul prin care modelele AI măsoară și procesează textul. Cuvintele, semnele de punctuație și chiar spațiile pot fi token-uri. Modelul dvs. are o limită a numărului de token-uri pe care le poate procesa odată (400.000 pentru GPT-5.2, cu până la 272.000 token-uri de intrare și 128.000 token-uri de ieșire). Înțelegerea token-urilor vă ajută să gestionați lungimea conversației și costurile.

## Cum funcționează memoria

Memoria chatului rezolvă problema fără starea menținând istoricul conversației. Înainte de a trimite cererea către model, cadrul adaugă înainte mesajele relevante anterioare. Când întrebați "Cum mă numesc?", sistemul trimite de fapt întreg istoricul conversației, permițând modelului să vadă că anterior ați spus "Mă numesc John."

LangChain4j oferă implementări de memorie care gestionează acest lucru automat. Alegeți câte mesaje să păstrați și cadrul gestionează fereastra de context.

<img src="../../../translated_images/ro/memory-window.bbe67f597eadabb3.webp" alt="Conceptul Ferestrei de Memorie" width="800"/>

*MessageWindowChatMemory menține o fereastră glisantă cu mesajele recente, eliminând automat pe cele vechi*

## Cum utilizează acest modul LangChain4j

Acest modul extinde ghidul rapid prin integrarea Spring Boot și adăugarea memoriei conversației. Iată cum se leagă componentele:

**Dependențe** - Adăugați două biblioteci LangChain4j:

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

**Model Chat** - Configurați Azure OpenAI ca un bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder-ul citește acreditările din variabilele de mediu setate de `azd up`. Setarea `baseUrl` către endpoint-ul Azure face clientul OpenAI să funcționeze cu Azure OpenAI.

**Memoria conversației** - Urmăriți istoricul chat-ului cu MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Creați memoria cu `withMaxMessages(10)` pentru a păstra ultimele 10 mesaje. Adăugați mesaje de utilizator și AI cu învelitori tipizate: `UserMessage.from(text)` și `AiMessage.from(text)`. Recuperați istoricul cu `memory.messages()` și îl trimiteți modelului. Serviciul stochează instanțe separate de memorie pentru fiecare ID de conversație, permițând mai multor utilizatori să converseze simultan.

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) și întrebați:
> - "Cum decide MessageWindowChatMemory ce mesaje să elimine când fereastra este plină?"
> - "Pot implementa stocare personalizată a memoriei folosind o bază de date în loc de memorie internă?"
> - "Cum aș adăuga sumarizare pentru a comprima istoricul vechi al conversației?"

Endpoint-ul pentru chat fără stare ocolește complet memoria - doar `chatModel.chat(prompt)` ca în ghidul rapid. Endpoint-ul cu stare adaugă mesaje la memorie, recuperează istoricul și include acest context la fiecare cerere. Aceeași configurare de model, modele diferite.

## Implementați infrastructura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selectați abonamentul și locația (recomandat eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selectați abonamentul și locația (se recomandă eastus2)
```

> **Notă:** Dacă întâmpinați o eroare de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), rulați pur și simplu `azd up` din nou. Resursele Azure pot fi încă în proces de aprovizionare în fundal, iar retry-ul permite finalizarea implementării când resursele ating o stare terminală.

Aceasta va:
1. Implementa resursa Azure OpenAI cu modelele GPT-5.2 și text-embedding-3-small
2. Va genera automat fișierul `.env` în rădăcina proiectului cu acreditările
3. Va configura toate variabilele de mediu necesare

**Aveți probleme cu implementarea?** Consultați [README-ul infrastructurii](infra/README.md) pentru depanare detaliată, inclusiv conflicte de nume de subdomenii, pași manuali pentru implementare în Azure Portal și ghid pentru configurarea modelului.

**Verificați dacă implementarea a reușit:**

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Notă:** Comanda `azd up` generează automat fișierul `.env`. Dacă aveți nevoie să-l actualizați mai târziu, puteți edita manual fișierul `.env` sau îl puteți regenera rulând:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## Rulați aplicația local

**Verificați implementarea:**

Asigurați-vă că fișierul `.env` există în directorul rădăcină și conține acreditările Azure:

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Porniți aplicațiile:**

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Dev container-ul include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O puteți găsi în bara de activități din partea stângă a VS Code (căutați pictograma Spring Boot).

Din Spring Boot Dashboard puteți:
- Vizualiza toate aplicațiile Spring Boot disponibile în workspace
- Porni/opri aplicațiile cu un singur click
- Vizualiza în timp real jurnalele aplicațiilor
- Monitoriza starea aplicației

Pur și simplu apăsați butonul play de lângă "introduction" pentru a porni acest modul sau porniți toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor construi JAR-urile dacă nu există.

> **Notă:** Dacă preferați să construiți manual toate modulele înainte de pornire:
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

Deschideți http://localhost:8080 în browser.

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

Aplicația oferă o interfață web cu două implementări de chat afișate una lângă alta.

<img src="../../../translated_images/ro/home-screen.121a03206ab910c0.webp" alt="Ecran principal al aplicației" width="800"/>

*Tabloul de bord afișând opțiunile Chat Simplu (fără stare) și Chat Conversațional (cu stare)*

### Chat fără stare (Panoul din stânga)

Încercați mai întâi aici. Spuneți "Mă numesc John" și apoi imediat întrebați "Cum mă numesc?" Modelul nu va ține minte pentru că fiecare mesaj este independent. Acest lucru demonstrează problema de bază a integrării simple a modelelor de limbaj - lipsa contextului conversației.

<img src="../../../translated_images/ro/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat fără stare" width="800"/>

*AI nu-și amintește numele dvs. din mesajul anterior*

### Conversație cu stare (Panoul din dreapta)

Acum încercați aceeași secvență aici. Spuneți "Mă numesc John" și apoi "Cum mă numesc?" De această dată își amintește. Diferența este MessageWindowChatMemory - menține istoricul conversației și îl include cu fiecare cerere. Așa funcționează AI conversațional de producție.

<img src="../../../translated_images/ro/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat cu stare" width="800"/>

*AI își amintește numele dvs. din conversația anterioară*

Ambele panouri folosesc același model GPT-5.2. Singura diferență este memoria. Acest lucru evidențiază clar ce aduce memoria aplicației dvs. și de ce este esențială pentru cazurile reale de utilizare.

## Pașii următori

**Următorul modul:** [02-prompt-engineering - Ingineria prompturilor cu GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigare:** [← Anterior: Modulul 00 - Quick Start](../00-quick-start/README.md) | [Înapoi la Principal](../README.md) | [Următorul: Modulul 02 - Ingineria Prompturilor →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinarea responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm nicio responsabilitate pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->