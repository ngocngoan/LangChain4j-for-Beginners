# Modulul 01: Începutul cu LangChain4j

## Cuprins

- [Parcurgere Video](../../../01-introduction)
- [Ce Vei Învăța](../../../01-introduction)
- [Cerinte Prealabile](../../../01-introduction)
- [Înțelegerea Problemei de Bază](../../../01-introduction)
- [Înțelegerea Token-urilor](../../../01-introduction)
- [Cum Funcționează Memoria](../../../01-introduction)
- [Cum Folosește Acesta LangChain4j](../../../01-introduction)
- [Deplasează Infrastructura Azure OpenAI](../../../01-introduction)
- [Rulează Aplicația Local](../../../01-introduction)
- [Folosirea Aplicației](../../../01-introduction)
  - [Chat Stateless (Panoul Stâng)](../../../01-introduction)
  - [Chat Stateful (Panoul Drept)](../../../01-introduction)
- [Pașii Următori](../../../01-introduction)

## Parcurgere Video

Urmărește această sesiune live care explică cum să începi cu acest modul:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Începerea cu LangChain4j - Sesiune Live" width="800"/></a>

## Ce Vei Învăța

În startul rapid, ai folosit Modelele GitHub pentru a trimite prompturi, a apela instrumente, a construi un pipeline RAG și a testa guardrails. Acele demo-uri au arătat ce este posibil — acum trecem la Azure OpenAI și GPT-5.2 și începem să construim aplicații de tip producție. Acest modul se concentrează pe AI conversațional care ține minte contextul și menține starea — conceptele pe care acele demo-uri quick start le-au folosit în spate, dar fără să le explice.

Vom folosi GPT-5.2 de la Azure OpenAI pe tot parcursul acestui ghid deoarece capacitățile sale avansate de raționament fac comportamentul diferitelor tipare mai evident. Când adaugi memorie, vei vedea clar diferența. Acest lucru face mai ușoară înțelegerea a ceea ce fiecare componentă aduce aplicației tale.

Vei construi o aplicație care demonstrează ambele tipare:

**Chat Stateless** - Fiecare cerere este independentă. Modelul nu are memorie a mesajelor anterioare. Acesta este tiparul folosit în quick start.

**Conversație Stateful** - Fiecare cerere include istoricul conversației. Modelul menține contextul pe mai multe runde. Acesta este ceea ce cer aplicațiile de producție.

## Cerinte Prealabile

- Abonament Azure cu acces la Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Notă:** Java, Maven, Azure CLI și Azure Developer CLI (azd) sunt preinstalate în devcontainer-ul furnizat.

> **Notă:** Acest modul folosește GPT-5.2 pe Azure OpenAI. Deploy-ul este configurat automat prin `azd up` - nu modifica numele modelului în cod.

## Înțelegerea Problemei de Bază

Modelele de limbaj sunt stateless. Fiecare apel API este independent. Dacă trimiți „Numele meu este John” și apoi întrebi „Care este numele meu?”, modelul nu are idee că tocmai te-ai prezentat. Tratatează fiecare cerere ca și cum ar fi prima conversație pe care o ai vreodată.

Aceasta este în regulă pentru Q&A simple, dar inutil pentru aplicații reale. Boții de servicii clienți trebuie să-și amintească ce le-ai spus. Asistenții personali au nevoie de context. Orice conversație cu mai multe runde necesită memorie.

Diagrama următoare contrastează cele două abordări — în stânga, un apel stateless care uită numele tău; în dreapta, un apel stateful susținut de ChatMemory care reține acest lucru.

<img src="../../../translated_images/ro/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversații Stateless vs Stateful" width="800"/>

*Diferența între conversațiile stateless (apeluri independente) și cele stateful (sensibile la context)*

## Înțelegerea Token-urilor

Înainte de a intra în conversații, este important să înțelegi token-urile — unitățile de bază de text pe care modelele de limbaj le procesează:

<img src="../../../translated_images/ro/token-explanation.c39760d8ec650181.webp" alt="Explicație Token" width="800"/>

*Exemplu de cum textul este împărțit în token-uri - „I love AI!” devine 4 unități separate de procesare*

Token-urile sunt modul în care modelele AI măsoară și procesează textul. Cuvintele, punctuația și chiar spațiile pot fi token-uri. Modelul tău are o limită a numărului de token-uri pe care le poate procesa simultan (400,000 pentru GPT-5.2, cu până la 272,000 token-uri de intrare și 128,000 token-uri de ieșire). Înțelegerea token-urilor te ajută să gestionezi lungimea conversației și costurile.

## Cum Funcționează Memoria

Memoria pentru chat rezolvă problema stateless prin menținerea istoricului conversației. Înainte de a trimite cererea către model, cadrul de lucru adaugă în față mesajele anterioare relevante. Când întrebi „Care este numele meu?”, sistemul trimite de fapt întreg istoricul conversației, permițând modelului să vadă că anterior ai spus „Numele meu este John.”

LangChain4j oferă implementări de memorie care gestionează acest lucru automat. Alegi câte mesaje să reții și cadrul de lucru gestionează fereastra de context. Diagrama de mai jos arată cum MessageWindowChatMemory menține o fereastră glisantă a mesajelor recente.

<img src="../../../translated_images/ro/memory-window.bbe67f597eadabb3.webp" alt="Conceptul Ferestrei de Memorie" width="800"/>

*MessageWindowChatMemory menține o fereastră glisantă a mesajelor recente, eliminând automat cele vechi*

## Cum Folosește Acesta LangChain4j

Acest modul extinde quick start-ul prin integrarea Spring Boot și adăugarea memoriei conversației. Iată cum se leagă piesele:

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

**Modelul Chat** - Configurează Azure OpenAI ca bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder-ul citește acreditările din variabilele de mediu setate de `azd up`. Setarea `baseUrl` la endpoint-ul Azure face ca clientul OpenAI să funcționeze cu Azure OpenAI.

**Memoria Conversației** - Urmărește istoricul chat-ului cu MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crează memoria cu `withMaxMessages(10)` pentru a păstra ultimele 10 mesaje. Adaugă mesaje de utilizator și AI cu wrapper-e tipizate: `UserMessage.from(text)` și `AiMessage.from(text)`. Recuperează istoricul cu `memory.messages()` și trimite-l modelului. Serviciul păstrează instanțe de memorie separate per ID conversație, permițând mai multor utilizatori să chateze simultan.

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) și întreabă:
> - „Cum decide MessageWindowChatMemory ce mesaje elimină când fereastra este plină?”
> - „Pot implementa o stocare personalizată a memoriei folosind o bază de date în loc de memorie internă?”
> - „Cum aș adăuga sumarizare pentru a comprima istoricul vechi al conversației?”

Endpoint-ul stateless omite complet memoria - doar `chatModel.chat(prompt)`, ca în quick start. Endpoint-ul stateful adaugă mesaje în memorie, recuperează istoricul și include contextul cu fiecare cerere. Aceeași configurare model, tipare diferite.

## Deplasează Infrastructura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selectați abonamentul și locația (recomandat eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selectați abonamentul și locația (eastus2 recomandat)
```

> **Notă:** Dacă întâmpini o eroare de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), rulează pur și simplu `azd up` din nou. Resursele Azure pot fi încă în curs de aprovizionare în fundal și reluarea comenzii permite finalizarea deploy-ului odată ce resursele ajung într-o stare terminală.

Aceasta va:
1. Deplasa resursa Azure OpenAI cu modelele GPT-5.2 și text-embedding-3-small
2. Genera automat fișierul `.env` în rădăcina proiectului cu acreditările
3. Configura toate variabilele de mediu necesare

**Ai probleme cu deploy-ul?** Consultă [README-ul pentru Infrastructură](infra/README.md) pentru depanare detaliată incluzând conflicte de nume subdomeniu, pași manuali de deploy în Azure Portal și ghidaj pentru configurarea modelului.

**Verifică dacă deploy-ul a reușit:**

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Notă:** Comanda `azd up` generează automat fișierul `.env`. Dacă trebuie să îl actualizezi ulterior, poți fie să îl editezi manual, fie să-l regenerezi rulând:
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

## Rulează Aplicația Local

**Verifică deploy-ul:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu acreditările Azure. Rulează din directorul modulului (`01-introduction/`):

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

Dev container-ul include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O vei găsi în bara de activități din stânga VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în workspace
- Porni/opri aplicații cu un singur click
- Vizualiza log-urile aplicației în timp real
- Monitoriza starea aplicațiilor

Pur și simplu apasă butonul de play de lângă „introduction” pentru a porni acest modul sau pornește toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` rădăcină și vor compila JAR-urile dacă nu există.

> **Notă:** Dacă preferi să construiești manual toate modulele înainte să pornești:
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

Deschide în browser http://localhost:8080.

**Pentru oprire:**

**Bash:**
```bash
./stop.sh  # Acest modul numai
# Sau
cd .. && ./stop-all.sh  # Toate modulele
```

**PowerShell:**
```powershell
.\stop.ps1  # Numai acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Folosirea Aplicației

Aplicația oferă o interfață web cu două implementări de chat afișate alăturat.

<img src="../../../translated_images/ro/home-screen.121a03206ab910c0.webp" alt="Ecranul Principal al Aplicației" width="800"/>

*Dashboard care arată opțiunile Simple Chat (stateless) și Conversational Chat (stateful)*

### Chat Stateless (Panoul Stâng)

Încearcă asta prima dată. Spune „Numele meu este John” și apoi imediat „Care este numele meu?” Modelul nu își va aminti deoarece fiecare mesaj este independent. Aceasta demonstrează problema de bază a integrării simple a modelului de limbaj - fără context conversațional.

<img src="../../../translated_images/ro/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Stateless" width="800"/>

*AI nu își amintește numele tău din mesajul anterior*

### Chat Stateful (Panoul Drept)

Acum încearcă aceeași secvență aici. Spune „Numele meu este John” și apoi „Care este numele meu?” De data aceasta modelul își amintește. Diferența este MessageWindowChatMemory - aceasta menține istoricul conversației și îl include în fiecare cerere. Așa funcționează AI-ul conversațional în producție.

<img src="../../../translated_images/ro/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Stateful" width="800"/>

*AI își amintește numele tău din conversația anterioară*

Ambele panouri folosesc același model GPT-5.2. Singura diferență este memoria. Acest lucru clarifică ce aduce memorie aplicației tale și de ce este esențială în cazurile reale de utilizare.

## Pașii Următori

**Următorul Modul:** [02-prompt-engineering - Ingineria Prompt-urilor cu GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigare:** [← Anterior: Modul 00 - Quick Start](../00-quick-start/README.md) | [Înapoi la Principal](../README.md) | [Următorul: Modul 02 - Ingineria Prompt-urilor →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere automată AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventuale neînțelegeri sau interpretări greșite apărute în urma utilizării acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->