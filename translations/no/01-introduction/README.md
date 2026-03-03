# Module 01: Komme i gang med LangChain4j

## Innholdsfortegnelse

- [Video Gjennomgang](../../../01-introduction)
- [Hva Du Vil Lære](../../../01-introduction)
- [Forutsetninger](../../../01-introduction)
- [Forstå Kjernesproblemet](../../../01-introduction)
- [Forstå Token](../../../01-introduction)
- [Hvordan Minne Fungerer](../../../01-introduction)
- [Hvordan Dette Bruker LangChain4j](../../../01-introduction)
- [Distribuer Azure OpenAI Infrastruktur](../../../01-introduction)
- [Kjør Applikasjonen Lokalt](../../../01-introduction)
- [Bruke Applikasjonen](../../../01-introduction)
  - [Stateless Chat (Venstre Panel)](../../../01-introduction)
  - [Stateful Chat (Høyre Panel)](../../../01-introduction)
- [Neste Steg](../../../01-introduction)

## Video Gjennomgang

Se denne live-økten som forklarer hvordan du kommer i gang med denne modulen:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Hva Du Vil Lære

I hurtigstarten brukte du GitHub-modeller for å sende forespørsler, kalle verktøy, bygge en RAG-pipeline, og teste vernesperrer. De demoene viste hva som er mulig — nå bytter vi til Azure OpenAI og GPT-5.2 og begynner å bygge produksjonsstil applikasjoner. Denne modulen fokuserer på samtale-AI som husker kontekst og opprettholder tilstand — konseptene som hurtigstartdemoene brukte bak kulissene, men ikke forklarte.

Vi bruker Azure OpenAI sin GPT-5.2 gjennom denne veiledningen fordi dens avanserte resonneringsevner gjør at oppførselen til forskjellige mønstre blir mer tydelig. Når du legger til minne, vil du klart se forskjellen. Dette gjør det lettere å forstå hva hver komponent tilfører applikasjonen din.

Du vil bygge en applikasjon som demonstrerer begge mønstrene:

**Stateless Chat** - Hver forespørsel er uavhengig. Modellen har ikke noe minne om tidligere meldinger. Dette er mønsteret du brukte i hurtigstarten.

**Stateful Conversation** - Hver forespørsel inkluderer samtalehistorikk. Modellen opprettholder kontekst over flere utsagn. Dette er hva produksjonsapplikasjoner krever.

## Forutsetninger

- Azure-abonnement med tilgang til Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Merk:** Java, Maven, Azure CLI og Azure Developer CLI (azd) er forhåndsinstallert i den medfølgende devcontaineren.

> **Merk:** Denne modulen bruker GPT-5.2 på Azure OpenAI. Distribusjonen konfigureres automatisk via `azd up` - ikke endre modellnavnet i koden.

## Forstå Kjernesproblemet

Språkmodeller er stateless. Hver API-kall er uavhengig. Hvis du sender "Mitt navn er John" og deretter spør "Hva heter jeg?", har modellen ingen anelse om at du nettopp introduserte deg selv. Den behandler hver forespørsel som om det er den første samtalen du noen gang har hatt.

Dette går fint for enkle spørsmål og svar, men er ubrukelig for ekte applikasjoner. Kundeserviceboter må huske hva du har fortalt dem. Personlige assistenter trenger kontekst. Enhver samtale med flere runder krever minne.

Følgende diagram kontrasterer de to tilnærmingene — til venstre et stateless kall som glemmer navnet ditt; til høyre et stateful kall støttet av ChatMemory som husker det.

<img src="../../../translated_images/no/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Forskjellen mellom stateless (uavhengige kall) og stateful (kontekstbevisste) samtaler*

## Forstå Token

Før du dykker inn i samtaler, er det viktig å forstå tokens - de grunnleggende tekst-enhetene som språkmodeller prosesserer:

<img src="../../../translated_images/no/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Eksempel på hvordan tekst deles opp i tokens - "I love AI!" blir til 4 separate prosesseringsenheter*

Tokens er hvordan AI-modeller måler og prosesserer tekst. Ord, skilletegn, og til og med mellomrom kan være tokens. Modellen din har en grense for hvor mange tokens den kan prosessere samtidig (400,000 for GPT-5.2, med opptil 272,000 input-tokens og 128,000 output-tokens). Å forstå tokens hjelper deg med å styre samtalelengde og kostnader.

## Hvordan Minne Fungerer

Samtale-minne løser det stateless problemet ved å opprettholde samtalehistorikk. Før du sender forespørselen til modellen, legger rammeverket til relevante tidligere meldinger foran. Når du spør "Hva heter jeg?", sender systemet faktisk hele samtalehistorikken, slik at modellen kan se at du tidligere sa "Mitt navn er John."

LangChain4j tilbyr minneimplementasjoner som håndterer dette automatisk. Du velger hvor mange meldinger som skal beholdes, og rammeverket styrer kontekstvinduet. Diagrammet nedenfor viser hvordan MessageWindowChatMemory opprettholder et glidende vindu av nylige meldinger.

<img src="../../../translated_images/no/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory opprettholder et glidende vindu av nylige meldinger, og fjerner automatisk gamle*

## Hvordan Dette Bruker LangChain4j

Denne modulen utvider hurtigstarten ved å integrere Spring Boot og legge til samtale-minne. Slik passer delene sammen:

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

**Chat Modell** - Konfigurer Azure OpenAI som en Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builderen leser legitimasjon fra miljøvariabler satt av `azd up`. Å sette `baseUrl` til Azure-endepunktet ditt gjør at OpenAI-klienten fungerer med Azure OpenAI.

**Samtale-minne** - Spor chatthistorikk med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Opprett minnet med `withMaxMessages(10)` for å beholde de siste 10 meldingene. Legg til bruker- og AI-meldinger med typer: `UserMessage.from(text)` og `AiMessage.from(text)`. Hent historikk med `memory.messages()` og send den til modellen. Tjenesten lagrer separate minneinstanser per samtale-ID, slik at flere brukere kan chatte samtidig.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) og spør:
> - "Hvordan bestemmer MessageWindowChatMemory hvilke meldinger som skal droppes når vinduet er fullt?"
> - "Kan jeg implementere egendefinert minnelagring ved å bruke en database i stedet for minne?"
> - "Hvordan kan jeg legge til oppsummering for å komprimere gammel samtalehistorikk?"

Stateless chat-endepunktet hopper over minne helt - bare `chatModel.chat(prompt)` som i hurtigstart. Stateful endepunkt legger til meldinger i minnet, henter historikk, og inkluderer den konteksten med hver forespørsel. Samme modellkonfigurasjon, forskjellige mønstre.

## Distribuer Azure OpenAI Infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Velg abonnement og plassering (eastus2 anbefalt)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Velg abonnement og plassering (anbefalt østus2)
```

> **Merk:** Hvis du får en tidsavbrudds-feil (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kjør bare `azd up` på nytt. Azure-ressurser kan fortsatt provisjoneres i bakgrunnen, og en ny forsøk lar distribusjonen fullføres når ressursene når en terminal tilstand.

Dette vil:
1. Distribuere Azure OpenAI-ressurs med GPT-5.2 og text-embedding-3-small modeller
2. Automatisk generere `.env`-fil i prosjektrot med legitimasjon
3. Sette opp alle nødvendige miljøvariabler

**Har du problemer med distribusjon?** Se [Infrastructure README](infra/README.md) for detaljert feilsøking inkludert subdomene-navnkonflikter, manuelle trinn for Azure Portal distribusjon, og modellkonfigurasjonsveiledning.

**Verifiser at distribusjonen lyktes:**

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

> **Merk:** `azd up`-kommandoen genererer automatisk `.env`-filen. Hvis du trenger å oppdatere den senere, kan du enten redigere `.env`-filen manuelt eller generere den på nytt ved å kjøre:
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

## Kjør Applikasjonen Lokalt

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotmappen med Azure-legitimasjon. Kjør dette fra modulmappen (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonene:**

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code brukere)**

Devcontaineren inkluderer Spring Boot Dashboard-utvidelsen som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner det i Aktivitetslinjen på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett enkelt klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk bare på play-knappen ved siden av "introduction" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stopp og overvåk alle moduler fra ett sted*

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rotkatalogen
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

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og vil bygge JARs hvis de ikke eksisterer.

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
.\stop.ps1  # Kun denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Bruke Applikasjonen

Applikasjonen gir et webgrensesnitt med to chat-implementasjoner side om side.

<img src="../../../translated_images/no/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som viser både Enkel Chat (stateless) og Samtale Chat (stateful) alternativer*

### Stateless Chat (Venstre Panel)

Prøv denne først. Spør "Mitt navn er John" og deretter umiddelbart "Hva heter jeg?" Modellen vil ikke huske fordi hver melding er uavhengig. Dette demonstrerer kjernesproblemet med grunnleggende språkmodell-integrasjon - ingen samtalekontekst.

<img src="../../../translated_images/no/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI husker ikke navnet ditt fra forrige melding*

### Stateful Chat (Høyre Panel)

Prøv nå samme sekvens her. Spør "Mitt navn er John" og deretter "Hva heter jeg?" Denne gangen husker den. Forskjellen er MessageWindowChatMemory - den opprettholder samtalehistorikk og inkluderer den med hver forespørsel. Slik fungerer AI i produksjonssamtaler.

<img src="../../../translated_images/no/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI husker navnet ditt fra tidligere i samtalen*

Begge paneler bruker samme GPT-5.2-modell. Den eneste forskjellen er minnet. Dette gjør det tydelig hva minne tilfører applikasjonen din og hvorfor det er essensielt for ekte bruksområder.

## Neste Steg

**Neste Modul:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasjon:** [← Forrige: Module 00 - Quick Start](../00-quick-start/README.md) | [Tilbake til Hoved](../README.md) | [Neste: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på det opprinnelige språket skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->