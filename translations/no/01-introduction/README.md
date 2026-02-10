# Modul 01: Komme i gang med LangChain4j

## Innholdsfortegnelse

- [Hva du vil lære](../../../01-introduction)
- [Forutsetninger](../../../01-introduction)
- [Forstå kjernen i problemet](../../../01-introduction)
- [Forstå tokens](../../../01-introduction)
- [Hvordan minne fungerer](../../../01-introduction)
- [Hvordan dette bruker LangChain4j](../../../01-introduction)
- [Distribuer Azure OpenAI-infrastruktur](../../../01-introduction)
- [Kjør applikasjonen lokalt](../../../01-introduction)
- [Bruke applikasjonen](../../../01-introduction)
  - [Stateless Chat (venstre panel)](../../../01-introduction)
  - [Stateful Chat (høyre panel)](../../../01-introduction)
- [Neste steg](../../../01-introduction)

## Hva du vil lære

Hvis du fullførte hurtigstarten, så du hvordan du sender forespørsler og får svar. Det er grunnlaget, men reelle applikasjoner trenger mer. Denne modulen lærer deg hvordan du bygger samtale-AI som husker kontekst og opprettholder tilstand - forskjellen mellom en engangs demo og en produksjonsklar applikasjon.

Vi vil bruke Azure OpenAI sin GPT-5.2 gjennom hele denne veiledningen fordi dens avanserte resonnementsevner gjør oppførselen til forskjellige mønstre mer tydelig. Når du legger til minne, vil du klart se forskjellen. Dette gjør det enklere å forstå hva hver komponent tilfører applikasjonen din.

Du skal bygge en applikasjon som demonstrerer begge mønstrene:

**Stateless Chat** - Hver forespørsel er uavhengig. Modellen har ingen hukommelse av tidligere meldinger. Dette er mønsteret du brukte i hurtigstarten.

**Stateful Conversation** - Hver forespørsel inkluderer samtalehistorikk. Modellen opprettholder kontekst over flere runder. Dette er det produksjonsapplikasjoner krever.

## Forutsetninger

- Azure-abonnement med tilgang til Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Merk:** Java, Maven, Azure CLI og Azure Developer CLI (azd) er forhåndsinstallert i den medfølgende devcontaineren.

> **Merk:** Denne modulen bruker GPT-5.2 på Azure OpenAI. Distribusjonen konfigureres automatisk via `azd up` - ikke endre modellnavnet i koden.

## Forstå kjernen i problemet

Språkmodeller er stateless. Hver API-kall er uavhengig. Hvis du sender "Mitt navn er John" og så spør "Hva heter jeg?", har modellen ingen anelse om at du nettopp introduserte deg. Den behandler hver forespørsel som om det er din første samtale noensinne.

Dette er greit for enkel spørsmål-og-svar, men ubrukelig for reelle applikasjoner. Kundeserviceboter må huske hva du fortalte dem. Personlige assistenter trenger kontekst. Enhver samtale med flere runder krever minne.

<img src="../../../translated_images/no/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Forskjellen mellom stateless (uavhengige kall) og stateful (kontekstbevisste) samtaler*

## Forstå tokens

Før du dykker ned i samtaler, er det viktig å forstå tokens - de grunnleggende enhetene av tekst som språkmodeller behandler:

<img src="../../../translated_images/no/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Eksempel på hvordan tekst deles opp i tokens - "Jeg elsker AI!" blir til 4 separate prosesseringsenheter*

Tokens er hvordan AI-modeller måler og behandler tekst. Ord, tegnsetting og til og med mellomrom kan være tokens. Modellen din har en grense for hvor mange tokens den kan prosessere samtidig (400 000 for GPT-5.2, med opptil 272 000 input tokens og 128 000 output tokens). Å forstå tokens hjelper deg å håndtere samtalelengde og kostnader.

## Hvordan minne fungerer

Chat-minne løser det stateless-problemet ved å opprettholde samtalehistorikk. Før du sender forespørselen til modellen, legger rammen til relevante tidligere meldinger foran. Når du spør "Hva heter jeg?", sender systemet faktisk hele samtalehistorikken, slik at modellen kan se at du tidligere sa "Mitt navn er John."

LangChain4j tilbyr minneimplementasjoner som håndterer dette automatisk. Du velger hvor mange meldinger som skal beholdes og rammen styrer kontekstvinduet.

<img src="../../../translated_images/no/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory opprettholder et skyvevindu av nylige meldinger og dropper automatisk gamle*

## Hvordan dette bruker LangChain4j

Denne modulen utvider hurtigstarten ved å integrere Spring Boot og legge til samtaleminne. Slik passer delene sammen:

**Avhengigheter** - Legg til to LangChain4j-biblioteker:

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

**Chatmodell** - Konfigurer Azure OpenAI som en Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builderen leser legitimasjon fra miljøvariabler satt av `azd up`. Innstilling av `baseUrl` til ditt Azure-endepunkt gjør at OpenAI-klienten fungerer med Azure OpenAI.

**Samtaleminne** - Spor chatthistorikk med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Opprett minne med `withMaxMessages(10)` for å beholde de siste 10 meldingene. Legg til bruker- og AI-meldinger med typede wrappers: `UserMessage.from(text)` og `AiMessage.from(text)`. Hent historikk med `memory.messages()` og send det til modellen. Tjenesten lagrer separate minneinstanser per samtale-ID, som tillater flere brukere å chatte samtidig.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) og spør:
> - "Hvordan avgjør MessageWindowChatMemory hvilke meldinger som skal droppes når vinduet er fullt?"
> - "Kan jeg implementere egendefinert minnelagring ved hjelp av en database i stedet for i minnet?"
> - "Hvordan kan jeg legge til oppsummering for å komprimere gammel samtalehistorikk?"

Den stateless chat-endepunktet hopper over minnet helt - bare `chatModel.chat(prompt)` som i hurtigstarten. Det stateful endepunktet legger til meldinger i minnet, henter historikk og inkluderer denne konteksten i hver forespørsel. Samme modellkonfigurasjon, forskjellige mønstre.

## Distribuer Azure OpenAI-infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Velg abonnement og plassering (eastus2 anbefales)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Velg abonnement og plassering (eastus2 anbefales)
```

> **Merk:** Hvis du støter på en timeout-feil (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kjør bare `azd up` igjen. Azure-ressurser kan fortsatt bli distribuert i bakgrunnen, og ny forsøk gjør at distribusjonen kan fullføres når ressursene når en terminal tilstand.

Dette vil:
1. Distribuere Azure OpenAI-ressurs med GPT-5.2 og text-embedding-3-small modeller
2. Automatisk generere `.env`-fil i prosjektrot med legitimasjon
3. Sette opp alle nødvendige miljøvariabler

**Har du problemer med distribusjon?** Se [Infrastructure README](infra/README.md) for detaljerte feilsøkingstrinn inkludert konflikter med underdomener, manuelle steg i Azure Portal og veiledning for modellkonfigurasjon.

**Verifiser at distribusjonen lykkes:**

**Bash:**
```bash
cat ../.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

> **Merk:** `azd up`-kommandoen genererer `.env`-filen automatisk. Hvis du trenger å oppdatere den senere, kan du enten redigere `.env`-filen manuelt eller generere den på nytt ved å kjøre:
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

## Kjør applikasjonen lokalt

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon:

**Bash:**
```bash
cat ../.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonene:**

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Devcontaineren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetslinjen på venstre side av VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk bare på spill-knappen ved siden av "introduction" for å starte denne modulen, eller start alle modulene samtidig.

<img src="../../../translated_images/no/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rotkatalog
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rotkatalogen
.\start-all.ps1
```

Eller start bare denne modulen:

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

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og bygger JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før oppstart:
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

Åpne http://localhost:8080 i nettleseren din.

**For å stoppe:**

**Bash:**
```bash
./stop.sh  # Kun denne modulen
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Bare denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Bruke applikasjonen

Applikasjonen tilbyr et webgrensesnitt med to chatteimplementasjoner side om side.

<img src="../../../translated_images/no/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som viser både Enkel Chat (stateless) og Samtale-Chat (stateful) alternativer*

### Stateless Chat (venstre panel)

Prøv denne først. Si "Mitt navn er John" og spør så umiddelbart "Hva heter jeg?" Modellen vil ikke huske fordi hver melding er uavhengig. Dette demonstrerer kjernen i problemet med enkel språkmodell-integrasjon - ingen samtalekontekst.

<img src="../../../translated_images/no/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI husker ikke navnet ditt fra forrige melding*

### Stateful Chat (høyre panel)

Prøv samme sekvens her. Si "Mitt navn er John" og så "Hva heter jeg?" Denne gangen husker den. Forskjellen er MessageWindowChatMemory - den opprettholder samtalehistorikk og inkluderer denne med hver forespørsel. Slik fungerer produksjonsklar samtale-AI.

<img src="../../../translated_images/no/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI husker navnet ditt fra tidligere i samtalen*

Begge panelene bruker samme GPT-5.2-modell. Den eneste forskjellen er minne. Dette gjør det tydelig hva minne tilfører applikasjonen din og hvorfor det er essensielt for reelle bruksområder.

## Neste steg

**Neste modul:** [02-prompt-engineering - Prompt Engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasjon:** [← Forrige: Modul 00 - Hurtigstart](../00-quick-start/README.md) | [Tilbake til hovedsiden](../README.md) | [Neste: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved bruk av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på dets opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->