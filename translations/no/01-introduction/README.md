# Modul 01: Komme i gang med LangChain4j

## Innholdsfortegnelse

- [Videogjennomgang](../../../01-introduction)
- [Hva du vil lære](../../../01-introduction)
- [Forutsetninger](../../../01-introduction)
- [Forstå kjerneproblemet](../../../01-introduction)
- [Forstå tokens](../../../01-introduction)
- [Hvordan minne fungerer](../../../01-introduction)
- [Hvordan dette bruker LangChain4j](../../../01-introduction)
- [Distribuer Azure OpenAI-infrastruktur](../../../01-introduction)
- [Kjør applikasjonen lokalt](../../../01-introduction)
- [Bruke applikasjonen](../../../01-introduction)
  - [Stateless Chat (Venstre panel)](../../../01-introduction)
  - [Stateful Chat (Høyre panel)](../../../01-introduction)
- [Neste steg](../../../01-introduction)

## Videogjennomgang

Se denne direktesendingen som forklarer hvordan du kommer i gang med denne modulen: [Komme i gang med LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Hva du vil lære

Hvis du fullførte hurtigstarten, så du hvordan du sender prompt og får svar. Det er grunnlaget, men ekte applikasjoner trenger mer. Denne modulen lærer deg hvordan du bygger konversasjons-AI som husker kontekst og opprettholder tilstand - forskjellen mellom en engangsdemo og en produksjonsklar applikasjon.

Vi bruker Azure OpenAI sin GPT-5.2 gjennom hele denne guiden fordi dens avanserte resonneringsevner gjør oppførselen til forskjellige mønstre tydeligere. Når du legger til minne, vil du klart se forskjellen. Dette gjør det enklere å forstå hva hver komponent tilfører applikasjonen din.

Du skal bygge én applikasjon som demonstrerer begge mønstrene:

**Stateless Chat** - Hver forespørsel er uavhengig. Modellen har ikke noe minne om tidligere meldinger. Dette er mønsteret du brukte i hurtigstarten.

**Stateful Conversation** - Hver forespørsel inkluderer samtalehistorikk. Modellen opprettholder kontekst over flere runder. Dette er det produksjonsapplikasjoner krever.

## Forutsetninger

- Azure-abonnement med tilgang til Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Merk:** Java, Maven, Azure CLI og Azure Developer CLI (azd) er forhåndsinstallert i den medfølgende devcontaineren.

> **Merk:** Denne modulen bruker GPT-5.2 på Azure OpenAI. Distribusjonen konfigureres automatisk via `azd up` - ikke endre modellnavnet i koden.

## Forstå kjerneproblemet

Språkmodeller er stateless. Hver API-kall er uavhengig. Hvis du skriver "Mitt navn er John" og så spør "Hva er navnet mitt?", har ikke modellen peiling på at du nettopp introduserte deg. Den behandler hver forespørsel som om det var den første samtalen du noensinne har hatt.

Dette fungerer greit for enkle spørsmål og svar, men er ubrukelig for ekte applikasjoner. Kundeserviceboter må huske hva du fortalte dem. Personlige assistenter trenger kontekst. Alle samtaler med flere runder krever minne.

<img src="../../../translated_images/no/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Forskjellen mellom stateless (uavhengige kall) og stateful (kontekstbevisste) samtaler*

## Forstå tokens

Før du dykker inn i samtaler, er det viktig å forstå tokens - de grunnleggende tekstenhetene som språkmodeller prosesserer:

<img src="../../../translated_images/no/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Eksempel på hvordan tekst splittes i tokens - "I love AI!" blir til 4 separate prosesseringsenheter*

Tokens er hvordan AI-modeller måler og prosesserer tekst. Ord, tegnsetting og til og med mellomrom kan være tokens. Modellen din har en grense på hvor mange tokens den kan prosessere om gangen (400 000 for GPT-5.2, med opptil 272 000 input-tokens og 128 000 output-tokens). Å forstå tokens hjelper deg å administrere samtalelengde og kostnader.

## Hvordan minne fungerer

Chat-minne løser problemet med stateless ved å opprettholde samtalehistorikk. Før du sender forespørselen til modellen, legger rammen automatisk til relevante tidligere meldinger foran. Når du spør "Hva er navnet mitt?", sender systemet faktisk hele samtalehistorikken, slik at modellen ser at du tidligere sa "Mitt navn er John."

LangChain4j tilbyr minneimplementasjoner som håndterer dette automatisk. Du velger hvor mange meldinger som skal beholdes, og rammen styrer kontekstvinduet.

<img src="../../../translated_images/no/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory opprettholder et glidende vindu av nylige meldinger, og fjerner automatisk gamle*

## Hvordan dette bruker LangChain4j

Denne modulen bygger videre på hurtigstarten ved å integrere Spring Boot og legge til samtaleminne. Slik passer bitene sammen:

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

**Chatmodell** - Konfigurer Azure OpenAI som en Spring-bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builderen leser legitimasjon fra miljøvariabler satt av `azd up`. Å sette `baseUrl` til din Azure-endepunkt gjør at OpenAI-klienten fungerer med Azure OpenAI.

**Samtaleminne** - Spor chatthistorikk med MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Lag minne med `withMaxMessages(10)` for å beholde de siste 10 meldingene. Legg til bruker- og AI-meldinger med typede wrappers: `UserMessage.from(text)` og `AiMessage.from(text)`. Hent historikk med `memory.messages()` og send den til modellen. Tjenesten lagrer separate minneinstanser per samtale-ID, noe som tillater flere brukere å chatte samtidig.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) og spør:
> - "Hvordan bestemmer MessageWindowChatMemory hvilke meldinger som skal fjernes når vinduet er fullt?"
> - "Kan jeg implementere egendefinert minnelagring ved å bruke en database i stedet for minne?"
> - "Hvordan kan jeg legge til oppsummering for å komprimere gammel samtalehistorikk?"

Det stateless chat-endepunktet hopper over minne helt - bare `chatModel.chat(prompt)` som i hurtigstarten. Det stateful endepunktet legger til meldinger i minnet, henter historikk og inkluderer den konteksten med hver forespørsel. Samme modellkonfigurasjon, forskjellige mønstre.

## Distribuer Azure OpenAI-infrastruktur

**Bash:**
```bash
cd 01-introduction
azd up  # Velg abonnement og plassering (eastus2 anbefales)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Velg abonnement og område (eastus2 anbefales)
```

> **Merk:** Hvis du får en timeout-feil (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), kjør bare `azd up` på nytt. Azure-ressurser kan fortsatt bli provisionert i bakgrunnen, og et nytt forsøk tillater distribusjonen å fullføres når ressursene når en terminal tilstand.

Dette vil:
1. Distribuere Azure OpenAI-ressurs med GPT-5.2 og text-embedding-3-small modeller
2. Automatisk generere `.env` fil i prosjektets rot med legitimasjon
3. Sette opp alle nødvendige miljøvariabler

**Har du problemer med distribusjon?** Se [Infrastructure README](infra/README.md) for detaljert feilsøking inkl. konflikt med underdomener, manuell Azure Portal-distribusjon, og veiledning for modellkonfigurasjon.

**Bekreft at distribusjonen lyktes:**

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, osv.
```

> **Merk:** Kommandoen `azd up` genererer automatisk `.env` filen. Hvis du trenger å oppdatere den senere, kan du enten redigere `.env` filen manuelt eller regenerere den ved å kjøre:
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

**Bekreft distribusjonen:**

Sørg for at `.env` filen finnes i rotkatalogen med Azure-legitimasjon:

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonene:**

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Devcontaineren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetslinjen på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk bare på play-knappen ved siden av "introduction" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (moduler 01–04):

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

Begge skriptene laster automatisk miljøvariabler fra rot `.env` fil og bygger JAR-filene hvis de ikke finnes.

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
cd .. && ./stop-all.sh  # Alle modulene
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Bruke applikasjonen

Applikasjonen tilbyr et webgrensesnitt med to chat-implementasjoner side om side.

<img src="../../../translated_images/no/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard som viser både Enkel Chat (stateless) og Konversasjonschat (stateful) alternativer*

### Stateless Chat (Venstre panel)

Prøv denne først. Skriv "Mitt navn er John" og spør deretter umiddelbart "Hva er navnet mitt?" Modellen vil ikke huske fordi hver melding er uavhengig. Dette demonstrerer kjerneproblemet med grunnleggende språkmodellintegrasjon - ingen samtalekontekst.

<img src="../../../translated_images/no/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI husker ikke navnet ditt fra forrige melding*

### Stateful Chat (Høyre panel)

Prøv nå samme sekvens her. Skriv "Mitt navn er John" og så "Hva er navnet mitt?" Denne gangen husker den. Forskjellen er MessageWindowChatMemory - den opprettholder samtalehistorikk og inkluderer den med hver forespørsel. Slik fungerer produksjonsklar konversasjons-AI.

<img src="../../../translated_images/no/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI husker navnet ditt fra tidligere i samtalen*

Begge panelene bruker samme GPT-5.2 modell. Den eneste forskjellen er minne. Dette gjør det tydelig hva minnet tilfører applikasjonen og hvorfor det er essensielt for ekte brukstilfeller.

## Neste steg

**Neste modul:** [02-prompt-engineering - Prompt engineering med GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigasjon:** [← Forrige: Modul 00 - Hurtigstart](../00-quick-start/README.md) | [Tilbake til hoved](../README.md) | [Neste: Modul 02 - Prompt engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi tilstreber nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->