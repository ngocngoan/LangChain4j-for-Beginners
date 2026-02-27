# Modul 01: Kom godt i gang med LangChain4j

## Indholdsfortegnelse

- [Video Gennemgang](../../../01-introduction)
- [Hvad du lærer](../../../01-introduction)
- [Forudsætninger](../../../01-introduction)
- [Forståelse af kerneproblemet](../../../01-introduction)
- [Forståelse af tokens](../../../01-introduction)
- [Hvordan hukommelse fungerer](../../../01-introduction)
- [Hvordan dette bruger LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI infrastruktur](../../../01-introduction)
- [Kør applikationen lokalt](../../../01-introduction)
- [Brug af applikationen](../../../01-introduction)
  - [Stateless Chat (Venstre panel)](../../../01-introduction)
  - [Stateful Chat (Højre panel)](../../../01-introduction)
- [Næste skridt](../../../01-introduction)

## Video Gennemgang

Se denne live session, som forklarer, hvordan du kommer i gang med dette modul: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Hvad du lærer

Hvis du gennemførte quick start, så du, hvordan du sender forespørgsler og modtager svar. Det er fundamentet, men rigtige applikationer kræver mere. Dette modul lærer dig at bygge konversationel AI, som husker kontekst og vedligeholder tilstand - forskellen på en engangs-demo og en produktionsklar applikation.

Vi bruger Azure OpenAI’s GPT-5.2 gennem hele denne guide, fordi dens avancerede ræsonneringsevner gør adfærden af forskellige mønstre tydeligere. Når du tilføjer hukommelse, vil du klart kunne se forskellen. Det gør det lettere at forstå, hvad hver komponent bidrager med til din applikation.

Du bygger én applikation, der demonstrerer begge mønstre:

**Stateless Chat** – Hver forespørgsel er uafhængig. Modellen har ingen hukommelse af tidligere beskeder. Det er det mønster, du brugte i quick start.

**Stateful Conversation** – Hver forespørgsel inkluderer samtalehistorik. Modellen vedligeholder kontekst over flere samtaletrin. Det er det, produktionsapplikationer kræver.

## Forudsætninger

- Azure-abonnement med adgang til Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI og Azure Developer CLI (azd) er forudinstalleret i den medfølgende devcontainer.

> **Note:** Dette modul bruger GPT-5.2 på Azure OpenAI. Deployment konfigureres automatisk via `azd up` - ændr ikke modelnavnet i koden.

## Forståelse af kerneproblemet

Sprogmodeller er stateless. Hver API-kald er uafhængigt. Hvis du sender "Mit navn er John" og derefter spørger "Hvad er mit navn?", har modellen ingen idé om, at du lige præsenterede dig. Den behandler hver forespørgsel, som var det den første samtale, du nogensinde har haft.

Det går an til simple spørgsmål og svar, men er ubrugeligt til rigtige applikationer. Kundeservice-bots skal huske, hvad du har fortalt dem. Personlige assistenter har brug for kontekst. Enhver samtale med flere trin kræver hukommelse.

<img src="../../../translated_images/da/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Forskellen mellem stateless (uafhængige kald) og stateful (kontekstbevidste) samtaler*

## Forståelse af tokens

Før du dykker ned i samtaler, er det vigtigt at forstå tokens - de grundlæggende enheder af tekst, som sprogmodeller behandler:

<img src="../../../translated_images/da/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Eksempel på, hvordan tekst bliver brudt op i tokens - "I love AI!" bliver til 4 separate behandlingsenheder*

Tokens er, hvordan AI-modeller måler og behandler tekst. Ord, tegnsætning og endda mellemrum kan være tokens. Din model har en grænse for, hvor mange tokens den kan behandle på én gang (400.000 for GPT-5.2, med op til 272.000 input tokens og 128.000 output tokens). At forstå tokens hjælper dig med at styre samtalens længde og omkostninger.

## Hvordan hukommelse fungerer

Chat-hukommelse løser det stateless problem ved at vedligeholde samtalehistorik. Før du sender din forespørgsel til modellen, tilføjer frameworket relevante tidligere beskeder forrest. Når du spørger "Hvad er mit navn?", sender systemet faktisk hele samtalehistorikken, så modellen kan se, at du tidligere sagde "Mit navn er John."

LangChain4j tilbyder hukommelsesimplementeringer, der håndterer dette automatisk. Du vælger, hvor mange beskeder der skal gemmes tilbage, og frameworket administrerer kontekstvinduet.

<img src="../../../translated_images/da/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory vedligeholder et glidende vindue af nylige beskeder og fjerner automatisk gamle*

## Hvordan dette bruger LangChain4j

Dette modul udvider quick start med integration af Spring Boot og tilføjelse af samtalehukommelse. Sådan hænger delene sammen:

**Afhængigheder** – Tilføj to LangChain4j biblioteker:

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

**Chat Model** – Konfigurer Azure OpenAI som en Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder’en læser legitimationsoplysninger fra miljøvariabler sat af `azd up`. At sætte `baseUrl` til din Azure-endpoint får OpenAI-klienten til at fungere med Azure OpenAI.

**Samtalehukommelse** – Spor chat-historik med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Opret hukommelse med `withMaxMessages(10)` for at bevare de sidste 10 beskeder. Tilføj bruger- og AI-beskeder med typed wrappers: `UserMessage.from(text)` og `AiMessage.from(text)`. Hent historik med `memory.messages()` og send den til modellen. Servicen gemmer separate hukommelsesinstanser pr. samtale-ID, så flere brugere kan chatte samtidig.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) og spørg:
> - "Hvordan beslutter MessageWindowChatMemory, hvilke beskeder der skal fjernes, når vinduet er fyldt?"
> - "Kan jeg implementere brugerdefineret hukommelseslagring med en database i stedet for in-memory?"
> - "Hvordan kan jeg tilføje opsummering for at komprimere gammel samtalehistorik?"

Den stateless chat endepunkt springer hukommelse fuldstændigt over - bare `chatModel.chat(prompt)` som i quick start. Det stateful endepunkt tilføjer beskeder til hukommelsen, henter historik og inkluderer den kontekst ved hver forespørgsel. Samme modelkonfiguration, forskellige mønstre.

## Deploy Azure OpenAI infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Vælg abonnement og placering (eastus2 anbefales)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vælg abonnement og placering (eastus2 anbefales)
```

> **Note:** Hvis du oplever en timeout fejl (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), så kør blot `azd up` igen. Azure-ressourcer kan stadig være under deployment i baggrunden, og genforsøg tillader deployment at afslutte, når ressourcerne når en terminal tilstand.

Dette vil:
1. Deploye Azure OpenAI ressourcen med GPT-5.2 og text-embedding-3-small modeller
2. Automatisk generere `.env` fil i projektets rod med legitimationsoplysninger
3. Opsætte alle nødvendige miljøvariabler

**Har du problemer med deployment?** Se [Infrastructure README](infra/README.md) for detaljeret fejlsøgning, herunder konflikter med subdomænenavne, manuelt Azure Portal deployment trin og vejledning til modelkonfiguration.

**Verificer at deployment lykkedes:**

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY osv.
```

> **Note:** `azd up` kommandoen genererer automatisk `.env` filen. Hvis du skal opdatere den senere, kan du enten redigere `.env` filen manuelt eller regenerere den ved at køre:
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

**Verificer deployment:**

Sørg for, at `.env` filen findes i rodmappen med Azure legitimationsoplysninger:

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

Devcontaineren inkluderer Spring Boot Dashboard udvidelsen, som giver et visuelt interface til at administrere alle Spring Boot applikationer. Den findes i Aktivitetsbjælken til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot applikationer i workspace
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationens status

Klik blot på play-knappen ved siden af "introduction" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodmappen
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

Begge scripts loader automatisk miljøvariabler fra rodens `.env` fil og bygger JAR-filerne, hvis de ikke findes.

> **Note:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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
./stop.sh  # Dette modul kun
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

Applikationen tilbyder en webgrænseflade med to chat-implementeringer side om side.

<img src="../../../translated_images/da/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard, der viser både Simple Chat (stateless) og Conversational Chat (stateful) muligheder*

### Stateless Chat (Venstre panel)

Prøv denne først. Skriv "Mit navn er John" og spørg derefter straks "Hvad er mit navn?" Modellen husker det ikke, fordi hver besked er uafhængig. Dette demonstrerer kerneproblemet med basale sprogmodel-integrationer - ingen samtalekontekst.

<img src="../../../translated_images/da/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI husker ikke dit navn fra forrige besked*

### Stateful Chat (Højre panel)

Prøv nu samme sekvens her. Skriv "Mit navn er John" og derefter "Hvad er mit navn?" Denne gang husker den. Forskellen er MessageWindowChatMemory - den vedligeholder samtalehistorik og inkluderer den i hver forespørgsel. Sådan fungerer produktionsklar samtale-AI.

<img src="../../../translated_images/da/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI husker dit navn fra tidligere i samtalen*

Begge paneler bruger samme GPT-5.2 model. Den eneste forskel er hukommelse. Det viser klart, hvad hukommelse bidrager med til din applikation, og hvorfor det er essentielt for reelle anvendelsessituationer.

## Næste skridt

**Næste modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Forrige: Modul 00 - Quick Start](../00-quick-start/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->