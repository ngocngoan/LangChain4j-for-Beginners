# Modul 01: Kom godt i gang med LangChain4j

## Indholdsfortegnelse

- [Video Gennemgang](../../../01-introduction)
- [Hvad du vil lære](../../../01-introduction)
- [Forudsætninger](../../../01-introduction)
- [Forståelse af det centrale problem](../../../01-introduction)
- [Forståelse af tokens](../../../01-introduction)
- [Hvordan hukommelse fungerer](../../../01-introduction)
- [Hvordan dette bruger LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI infrastruktur](../../../01-introduction)
- [Kør applikationen lokalt](../../../01-introduction)
- [Brug af applikationen](../../../01-introduction)
  - [Stateless chat (venstre panel)](../../../01-introduction)
  - [Stateful chat (højre panel)](../../../01-introduction)
- [Næste skridt](../../../01-introduction)

## Video Gennemgang

Se denne live session, som forklarer, hvordan du kommer i gang med dette modul:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Kom godt i gang med LangChain4j - Live Session" width="800"/></a>

## Hvad du vil lære

I quick start brugte du GitHub-modeller til at sende prompts, kalde værktøjer, bygge en RAG pipeline og teste sikkerhedsgitre. De demos viste, hvad der er muligt — nu skifter vi til Azure OpenAI og GPT-5.2 og begynder at bygge produktionsagtige applikationer. Dette modul fokuserer på samtale-AI, som husker kontekst og bevarer tilstand — de koncepter som quick start demos brugte i baggrunden men ikke forklarede.

Vi bruger Azure OpenAI's GPT-5.2 gennem hele guiden, fordi dens avancerede ræsonneringsmuligheder gør adfærden i forskellige mønstre mere tydelig. Når du tilføjer hukommelse, kan du klart se forskellen. Det gør det lettere at forstå, hvad hver komponent bidrager med til din applikation.

Du vil bygge en applikation, som demonstrerer begge mønstre:

**Stateless Chat** - Hver anmodning er uafhængig. Modellen har ingen hukommelse om tidligere beskeder. Dette er mønstret, du brugte i quick start.

**Stateful Conversation** - Hver anmodning inkluderer samtalehistorik. Modellen bevarer kontekst over flere interaktioner. Dette kræves i produktionsapplikationer.

## Forudsætninger

- Azure abonnement med adgang til Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI og Azure Developer CLI (azd) er forhåndsinstalleret i den medfølgende devcontainer.

> **Note:** Dette modul bruger GPT-5.2 på Azure OpenAI. Deployeringen konfigureres automatisk via `azd up` - ændr ikke modelnavnet i koden.

## Forståelse af det centrale problem

Sprogbaserede modeller er stateless. Hver API-anmodning er uafhængig. Hvis du sender "Mit navn er John" og derefter spørger "Hvad er mit navn?", har modellen ingen idé om, at du lige har præsenteret dig. Den behandler hver forespørgsel, som om det er den første samtale, du nogensinde har haft.

Det fungerer fint til simple Q&A, men er ubrugeligt til rigtige applikationer. Kundeservice-bots skal huske, hvad du fortalte dem. Personlige assistenter har brug for kontekst. Enhver samtale med flere omgange kræver hukommelse.

Diagrammet nedenfor kontrasterer de to tilgange — til venstre, et stateless kald, som glemmer dit navn; til højre, et stateful kald med ChatMemory, der husker det.

<img src="../../../translated_images/da/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Forskellen mellem stateless (uafhængige kald) og stateful (kontekstbevidste) samtaler*

## Forståelse af tokens

Før vi dykker ned i samtaler, er det vigtigt at forstå tokens - de grundlæggende enheder af tekst som sprogmodeller behandler:

<img src="../../../translated_images/da/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Eksempel på hvordan tekst brydes i tokens - "I love AI!" bliver til 4 separate behandlingsenheder*

Tokens er, hvordan AI-modeller måler og behandler tekst. Ord, tegnsætning og endda mellemrum kan være tokens. Din model har en grænse for, hvor mange tokens den kan behandle ad gangen (400.000 for GPT-5.2, med op til 272.000 input-tokens og 128.000 output-tokens). At forstå tokens hjælper dig med at styre samtalens længde og omkostninger.

## Hvordan hukommelse fungerer

Chat memory løser det stateless problem ved at bevare samtalehistorik. Inden du sender din forespørgsel til modellen, tilføjer frameworket relevante tidligere beskeder forrest. Når du spørger "Hvad er mit navn?", sender systemet faktisk hele samtalehistorikken, så modellen kan se, at du tidligere sagde "Mit navn er John."

LangChain4j leverer hukommelsesimplementeringer, som håndterer dette automatisk. Du vælger, hvor mange beskeder der skal bevares, og frameworket styrer kontekstvinduet. Diagrammet nedenfor viser, hvordan MessageWindowChatMemory opretholder et glidende vindue med nylige beskeder.

<img src="../../../translated_images/da/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory opretholder et glidende vindue af nylige beskeder, og fjerner automatisk gamle*

## Hvordan dette bruger LangChain4j

Dette modul udvider quick start med integration af Spring Boot og tilføjelse af samtalehukommelse. Sådan passer delene sammen:

**Afhængigheder** - Tilføj to LangChain4j biblioteker:

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

**Chat Model** - Konfigurer Azure OpenAI som en Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builderen læser legitimationsoplysninger fra miljøvariabler sat af `azd up`. Ved at sætte `baseUrl` til din Azure-endpoint får OpenAI-klienten til at fungere med Azure OpenAI.

**Samtalehukommelse** - Spor chat historik med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Opret hukommelse med `withMaxMessages(10)` for at beholde de sidste 10 beskeder. Tilføj bruger- og AI-beskeder med typede wrappers: `UserMessage.from(text)` og `AiMessage.from(text)`. Hent historik med `memory.messages()` og send den til modellen. Servicen lagrer separate hukommelsesinstanser pr. samtale-ID, så flere brugere kan chatte samtidigt.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) og spørg:
> - "Hvordan beslutter MessageWindowChatMemory, hvilke beskeder der skal droppes, når vinduet er fuldt?"
> - "Kan jeg implementere tilpasset hukommelseslagring ved brug af en database i stedet for in-memory?"
> - "Hvordan kan jeg tilføje opsummering for at komprimere gammel samtalehistorik?"

Den stateless chat endpoint springer hukommelsen helt over - bare `chatModel.chat(prompt)` som i quick start. Den stateful endpoint tilføjer beskeder til hukommelsen, henter historik og medtager den kontekst med hver anmodning. Samme modelkonfiguration, forskellige mønstre.

## Deploy Azure OpenAI infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Vælg abonnement og placering (anbefales eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vælg abonnement og placering (eastus2 anbefales)
```

> **Note:** Hvis du støder på timeout-fejl (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), så kør blot `azd up` igen. Azure-ressourcer kan stadig være under provisionering i baggrunden, og et genkald tillader deployeringen at fuldføres, når ressourcerne når en terminal tilstand.

Dette vil:
1. Deployere Azure OpenAI-ressource med GPT-5.2 og text-embedding-3-small modeller
2. Automatisk generere `.env` fil i projektets rod med legitimationsoplysninger
3. Sætte alle nødvendige miljøvariabler op

**Har du problemer med deployment?** Se [Infrastructure README](infra/README.md) for detaljeret fejlfinding inklusive subdomænenavnkonflikter, manuelle Azure Portal deploy-trin og modelkonfigurationsvejledning.

**Bekræft at deployment lykkedes:**

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY osv.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY osv.
```

> **Note:** `azd up` kommandoen genererer automatisk `.env` filen. Hvis du har brug for at opdatere den senere, kan du enten redigere `.env` filen manuelt eller regenerere den ved at køre:
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

## Kør applikationen lokalt

**Bekræft deployment:**

Sørg for at `.env` filen findes i rodmappen med Azure legitimationsoplysninger. Kør dette fra modulets bibliotek (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationerne:**

**Mulighed 1: Brug Spring Boot Dashboard (Anbefalet til VS Code brugere)**

Dev containeren inkluderer Spring Boot Dashboard extension, som giver et visuelt interface til at styre alle Spring Boot applikationer. Du finder det i Aktivitetspanelet til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot applikationer i workspace
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationens status

Klik blot på play-knappen ved siden af "introduction" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stop og overvåg alle moduler fra ét sted*

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra roddirektoriet
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra roddirectory
.\start-all.ps1
```

Eller start kun dette modul:

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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env` fil og bygger JAR-filerne, hvis de ikke findes.

> **Note:** Foretrækker du at bygge alle moduler manuelt før start:
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

Åbn http://localhost:8080 i din browser.

**For at stoppe:**

**Bash:**
```bash
./stop.sh  # Kun denne modul
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modul
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Brug af applikationen

Applikationen tilbyder et webinterface med to chat-implementeringer side om side.

<img src="../../../translated_images/da/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard, der viser både Simple Chat (stateless) og Conversational Chat (stateful) muligheder*

### Stateless chat (venstre panel)

Prøv dette først. Spørg "Mit navn er John" og spørg så straks "Hvad er mit navn?" Modellen vil ikke huske, fordi hver besked er uafhængig. Dette demonstrerer det centrale problem med grundlæggende sprogmodel-integration - ingen samtalekontekst.

<img src="../../../translated_images/da/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI husker ikke dit navn fra den forrige besked*

### Stateful chat (højre panel)

Prøv nu samme sekvens her. Spørg "Mit navn er John" og dernæst "Hvad er mit navn?" Denne gang husker den. Forskellen er MessageWindowChatMemory - den bevarer samtalehistorik og inkluderer den med hver anmodning. Det er sådan produktionssamtale-AI fungerer.

<img src="../../../translated_images/da/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI husker dit navn fra tidligere i samtalen*

Begge paneler bruger samme GPT-5.2 model. Den eneste forskel er hukommelsen. Det gør det klart, hvad hukommelse bringer til din applikation, og hvorfor det er essentielt til virkelige brugssager.

## Næste skridt

**Næste modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Forrige: Modul 00 - Quick Start](../00-quick-start/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi stræber efter nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os ikke ansvar for misforståelser eller fejltolkninger, der opstår ved brug af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->