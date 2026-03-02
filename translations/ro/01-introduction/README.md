# Modul 01: Primii Pași cu LangChain4j

## Cuprins

- [Prezentare Video](../../../01-introduction)
- [Ce Vei Învăța](../../../01-introduction)
- [Precondiții](../../../01-introduction)
- [Înțelegerea Problemei de Bază](../../../01-introduction)
- [Înțelegerea Token-urilor](../../../01-introduction)
- [Cum Funcționează Memoria](../../../01-introduction)
- [Cum Folosește Acesta LangChain4j](../../../01-introduction)
- [Implementarea Infrastructurii Azure OpenAI](../../../01-introduction)
- [Rularea Aplicației Local](../../../01-introduction)
- [Folosirea Aplicației](../../../01-introduction)
  - [Chat Fără Stare (Panoul Din Stânga)](../../../01-introduction)
  - [Chat Cu Stare (Panoul Din Dreapta)](../../../01-introduction)
- [Pașii Următori](../../../01-introduction)

## Prezentare Video

Urmărește această sesiune live care explică cum să începi cu acest modul:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Ce Vei Învăța

Dacă ai parcurs începutul rapid, ai văzut cum să trimiți prompturi și să primești răspunsuri. Aceasta este fundația, dar aplicațiile reale au nevoie de mai mult. Acest modul te învață cum să construiești AI conversațional care își amintește contextul și menține starea - diferența între un demo de ocazie și o aplicație pregătită pentru producție.

Vom folosi GPT-5.2 de la Azure OpenAI pe parcursul acestui ghid deoarece capacitățile sale avansate de raționament fac comportamentul diferitelor modele mai evident. Când adaugi memoria, vei vedea clar diferența. Acest lucru face mai ușoară înțelegerea a ceea ce aduce fiecare componentă aplicației tale.

Vei construi o aplicație care demonstrează ambele modele:

**Chat fără stare** - Fiecare cerere este independentă. Modelul nu are memorie a mesajelor anterioare. Acesta este modelul pe care l-ai folosit în începutul rapid.

**Conversație cu stare** - Fiecare cerere include istoricul conversației. Modelul menține contextul peste mai multe schimburi. Acesta este ce necesită aplicațiile de producție.

## Precondiții

- Abonament Azure cu acces la Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notă:** Java, Maven, Azure CLI și Azure Developer CLI (azd) sunt preinstalate în devcontainer-ul furnizat.

> **Notă:** Acest modul folosește GPT-5.2 pe Azure OpenAI. Implementarea este configurată automat prin `azd up` - nu modifica numele modelului în cod.

## Înțelegerea Problemei de Bază

Modelele de limbaj sunt fără stare. Fiecare apel API este independent. Dacă trimiți „Numele meu este John” și apoi întrebi „Care este numele meu?”, modelul nu are habar că tocmai ți-ai spus numele. Tratează fiecare cerere ca și cum ar fi prima conversație pe care ai avut-o vreodată.

Aceasta este bine pentru întrebări și răspunsuri simple, dar inutil pentru aplicații reale. Boții de serviciu clienți trebuie să-și amintească ce le-ai spus. Asistenții personali au nevoie de context. Orice conversație pe mai multe tururi necesită memorie.

<img src="../../../translated_images/ro/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Diferența dintre conversațiile fără stare (apeluri independente) și cele cu stare (conștiente de context)*

## Înțelegerea Token-urilor

Înainte de a intra în conversații, este important să înțelegi token-urile - unitățile de bază de text pe care modelele de limbaj le procesează:

<img src="../../../translated_images/ro/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exemplu despre cum textul este descompus în token-uri - „I love AI!” devine 4 unități procesate separat*

Token-urile sunt modul în care modelele AI măsoară și procesează textul. Cuvintele, semnele de punctuație și chiar spațiile pot fi token-uri. Modelul tău are o limită a numărului de token-uri pe care le poate procesa simultan (400.000 pentru GPT-5.2, cu până la 272.000 token-uri de intrare și 128.000 token-uri de ieșire). Înțelegerea token-urilor te ajută să gestionezi lungimea conversației și costurile.

## Cum Funcționează Memoria

Memoria de chat rezolvă problema lipsei de stare menținând istoricul conversației. Înainte de a trimite cererea către model, cadrul adaugă la început mesajele relevante anterioare. Când întrebi „Care este numele meu?”, sistemul trimite de fapt întreg istoricul conversației, permițând modelului să vadă că ai spus anterior „Numele meu este John.”

LangChain4j oferă implementări de memorie care gestionează asta automat. Alegi câte mesaje să reții, iar cadrul gestionează fereastra de context.

<img src="../../../translated_images/ro/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory menține o fereastră glisantă a mesajelor recente, eliminând automat pe cele vechi*

## Cum Folosește Acesta LangChain4j

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

**Model de chat** - Configurează Azure OpenAI ca bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder-ul citește acreditările din variabilele de mediu setate de `azd up`. Setarea `baseUrl` la endpoint-ul tău Azure face ca clientul OpenAI să funcționeze cu Azure OpenAI.

**Memorie conversație** - Urmărește istoricul chatului cu MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Creează memoria cu `withMaxMessages(10)` pentru a păstra ultimele 10 mesaje. Adaugă mesaje utilizatorului și AI folosind înfășurări tipizate: `UserMessage.from(text)` și `AiMessage.from(text)`. Recuperează istoricul cu `memory.messages()` și trimite-l modelului. Serviciul stochează instanțe separate de memorie per ID de conversație, permițând mai multor utilizatori să comunice simultan.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) și întreabă:
> - "Cum decide MessageWindowChatMemory ce mesaje să elimine când fereastra este plină?"
> - "Pot implementa stocarea memoriei personalizate folosind o bază de date în loc de memorie în ram?"
> - "Cum aș adăuga sumarizare pentru a comprima istoricul conversațiilor vechi?"

Punctul final de chat fără stare evită complet utilizarea memoriei - doar `chatModel.chat(prompt)` ca în începutul rapid. Endpoint-ul cu stare adaugă mesaje în memorie, recuperează istoricul și include acel context în fiecare cerere. Aceeași configurație a modelului, modele diferite.

## Implementarea Infrastructurii Azure OpenAI

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

> **Notă:** Dacă întâlnești o eroare de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), rulează pur și simplu `azd up` din nou. Resursele Azure pot fi încă în curs de provisioning în fundal, iar reîncercarea permite implementarea să se finalizeze odată ce resursele ajung într-o stare terminală.

Aceasta va:
1. Implementa resursa Azure OpenAI cu modelele GPT-5.2 și text-embedding-3-small
2. Va genera automat fișierul `.env` în rădăcina proiectului cu acreditările
3. Va configura toate variabilele de mediu necesare

**Ai probleme cu implementarea?** Consultă [README-ul Infrastructure](infra/README.md) pentru depanare detaliată, inclusiv conflicte de nume subdomenii, pași manuali de implementare în Portalul Azure și ghiduri pentru configurarea modelelor.

**Verifică dacă implementarea a avut succes:**

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Notă:** Comanda `azd up` generează automat fișierul `.env`. Dacă trebuie să-l actualizezi ulterior, poți fie să-l editezi manual, fie să-l regenerezi rulând:
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

## Rularea Aplicației Local

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure:

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicațiile:**

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat utilizatorilor VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru a gestiona toate aplicațiile Spring Boot. O poți găsi în bara de activități din stânga în VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard poți:
- Vedea toate aplicațiile Spring Boot disponibile în workspace
- Porni/opri aplicații cu un singur clic
- Vizualiza jurnalele aplicațiilor în timp real
- Monitoriza starea aplicațiilor

Dă clic pe butonul play de lângă „introduction” pentru a porni acest modul, sau pornește toate modulele odată.

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` de la rădăcină și vor construi JAR-urile dacă nu există.

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

Deschide http://localhost:8080 în browser.

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

## Folosirea Aplicației

Aplicația oferă o interfață web cu două implementări de chat afișate una lângă alta.

<img src="../../../translated_images/ro/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Panou de control care afișează opțiunile Chat Simplu (fără stare) și Chat Conversațional (cu stare)*

### Chat Fără Stare (Panoul Din Stânga)

Încearcă asta prima dată. Spune „Numele meu este John” și apoi imediat întreabă „Care este numele meu?” Modelul nu își va aminti pentru că fiecare mesaj este independent. Aceasta demonstrează problema fundamentală a integrării simple a modelelor de limbaj - niciun context al conversației.

<img src="../../../translated_images/ro/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI nu își amintește numele tău din mesajul anterior*

### Chat Cu Stare (Panoul Din Dreapta)

Acum încearcă aceeași secvență aici. Spune „Numele meu este John” și apoi „Care este numele meu?” De data aceasta își amintește. Diferența este MessageWindowChatMemory - menține istoricul conversației și îl include cu fiecare cerere. Așa funcționează AI conversațional în producție.

<img src="../../../translated_images/ro/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI îți amintește numele din conversația anterioară*

Ambele panouri folosesc același model GPT-5.2. Singura diferență este memoria. Acest lucru face clar ce aduce memoria aplicației tale și de ce este esențială pentru cazurile reale de utilizare.

## Pașii Următori

**Următorul Modul:** [02-prompt-engineering - Ingineria Prompturilor cu GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigare:** [← Anterior: Modul 00 - Început Rapid](../00-quick-start/README.md) | [Înapoi la Principal](../README.md) | [Următorul: Modul 02 - Ingineria Prompturilor →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un traducător uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite survenite în urma utilizării acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->