# Modul 04: AI-agenter med verktøy

## Innholdsfortegnelse

- [Hva du vil lære](../../../04-tools)
- [Forutsetninger](../../../04-tools)
- [Forståelse av AI-agenter med verktøy](../../../04-tools)
- [Hvordan verktøykall fungerer](../../../04-tools)
  - [Verktøydefinisjoner](../../../04-tools)
  - [Beslutningstaking](../../../04-tools)
  - [Utførelse](../../../04-tools)
  - [Generering av svar](../../../04-tools)
  - [Arkitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Kjedede verktøy](../../../04-tools)
- [Kjør applikasjonen](../../../04-tools)
- [Bruk av applikasjonen](../../../04-tools)
  - [Prøv enkel verktøybruk](../../../04-tools)
  - [Test kjedede verktøy](../../../04-tools)
  - [Se samtaleflyt](../../../04-tools)
  - [Eksperimenter med forskjellige forespørsler](../../../04-tools)
- [Nøkkelkonsepter](../../../04-tools)
  - [ReAct-mønsteret (Resonnement og handling)](../../../04-tools)
  - [Verktøybeskrivelser er viktige](../../../04-tools)
  - [Øktstyring](../../../04-tools)
  - [Feilhåndtering](../../../04-tools)
- [Tilgjengelige verktøy](../../../04-tools)
- [Når man bør bruke verktøybaserte agenter](../../../04-tools)
- [Verktøy vs RAG](../../../04-tools)
- [Neste steg](../../../04-tools)

## Hva du vil lære

Så langt har du lært hvordan man har samtaler med AI, strukturerer prompt effektivt, og forankrer svarene i dokumentene dine. Men det finnes fortsatt en grunnleggende begrensning: språkmodeller kan bare generere tekst. De kan ikke sjekke været, utføre beregninger, forespørre databaser eller samhandle med eksterne systemer.

Verktøy endrer dette. Ved å gi modellen tilgang til funksjoner den kan kalle, forvandler du den fra en tekstgenerator til en agent som kan utføre handlinger. Modellen bestemmer når den trenger et verktøy, hvilket verktøy som skal brukes, og hvilke parametere som skal sendes. Koden din utfører funksjonen og returnerer resultatet. Modellen inkluderer dette resultatet i svaret sitt.

## Forutsetninger

- Fullført [Modul 01 - Introduksjon](../01-introduction/README.md) (Azure OpenAI-ressurser distribuert)
- Fullførte tidligere moduler anbefales (denne modulen refererer til [RAG-konsepter fra Modul 03](../03-rag/README.md) i sammenligningen Verktøy vs RAG)
- `.env`-fil i rotkatalog med Azure-legitimasjon (laget via `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg distribusjonsinstruksjonene der først.

## Forståelse av AI-agenter med verktøy

> **📝 Merk:** Begrepet "agenter" i denne modulen refererer til AI-assistenter utvidet med verktøykall-funksjonalitet. Dette er forskjellig fra **Agentic AI**-mønstre (autonome agenter med planlegging, minne og flerstegs resonnement) som vi dekker i [Modul 05: MCP](../05-mcp/README.md).

Uten verktøy kan en språkmodell kun generere tekst basert på treningsdataene sine. Spør om dagens vær, og den må gjette. Gi den verktøy, og den kan kalle et vær-API, utføre beregninger eller forespørre en database — og veve disse reelle resultatene inn i svaret sitt.

<img src="../../../translated_images/no/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Uten verktøy kan modellen bare gjette — med verktøy kan den kalle API-er, utføre beregninger og returnere sanntidsdata.*

En AI-agent med verktøy følger et **Resonnement og Handling (ReAct)**-mønster. Modellen svarer ikke bare — den tenker over hva den trenger, handler ved å kalle et verktøy, observerer resultatet, og bestemmer så om den skal handle igjen eller levere det endelige svaret:

1. **Resonner** — Agenten analyserer brukerens spørsmål og avgjør hva slags informasjon den trenger
2. **Handle** — Agenten velger riktig verktøy, genererer korrekte parametere, og kaller det
3. **Observer** — Agenten mottar verktøyets output og evaluerer resultatet
4. **Gjenta eller svar** — Hvis mer data trengs, går agenten tilbake; ellers komponerer den et naturlig språk-svar

<img src="../../../translated_images/no/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-syklusen — agenten resonerer om hva den skal gjøre, handler ved å kalle et verktøy, observerer resultatet, og gjentar til den kan levere endelig svar.*

Dette skjer automatisk. Du definerer verktøyene og beskrivelsene deres. Modellen håndterer beslutningen om når og hvordan de skal brukes.

## Hvordan verktøykall fungerer

### Verktøydefinisjoner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definerer funksjoner med klare beskrivelser og parameter-spesifikasjoner. Modellen ser disse beskrivelsene i systemprompten og forstår hva hvert verktøy gjør.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logikken for vær oppslag
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistenten er automatisk koblet til av Spring Boot med:
// - ChatModel bean
// - Alle @Tool metoder fra @Component klasser
// - ChatMemoryProvider for sesjonsstyring
```

Diagrammet under bryter ned hver annotasjon og viser hvordan hvert element hjelper AI til å forstå når den skal kalle verktøyet og hvilke argumenter som skal sendes:

<img src="../../../translated_images/no/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi til en verktøydefinisjon — @Tool forteller AI når det skal brukes, @P beskriver hver parameter, og @AiService kobler alt sammen ved oppstart.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) og spør:
> - "Hvordan kan jeg integrere et ekte vær-API som OpenWeatherMap i stedet for mock-data?"
> - "Hva kjennetegner en god verktøybeskrivelse som hjelper AI til å bruke det korrekt?"
> - "Hvordan håndterer jeg API-feil og grenseverdier for forespørsler i verktøyimplementasjoner?"

### Beslutningstaking

Når en bruker spør "Hvordan er været i Seattle?", velger ikke modellen tilfeldig et verktøy. Den sammenligner brukerens intensjon med hver verktøybeskrivelse den har tilgang til, vurderer relevansen for hver, og velger den beste matchen. Den genererer så et strukturert funksjonskall med riktige parametere — i dette tilfellet setter den `location` til `"Seattle"`.

Hvis ingen verktøy samsvarer med brukerens forespørsel, faller modellen tilbake på kunnskap den allerede har. Hvis flere verktøy samsvarer, velger den det mest spesifikke.

<img src="../../../translated_images/no/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modellen vurderer alle tilgjengelige verktøy opp mot brukerens intensjon og velger den beste — derfor er det viktig å skrive klare og spesifikke verktøybeskrivelser.*

### Utførelse

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot auto-wirer det deklarative `@AiService`-grensesnittet med alle registrerte verktøy, og LangChain4j utfører verktøykall automatisk. Bak kulissene går et komplett verktøykall gjennom seks faser — fra brukerens spørsmål i naturlig språk helt tilbake til svar i naturlig språk:

<img src="../../../translated_images/no/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Ende-til-ende-flyt — brukeren stiller spørsmål, modellen velger verktøy, LangChain4j utfører det, og modellen vever inn resultatet i naturlig svar.*

Hvis du kjørte [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i Modul 00, har du allerede sett dette mønsteret i praksis — `Calculator`-verktøyene ble kalt på samme måte. Sekvensdiagrammet under viser nøyaktig hva som skjedde i bakgrunnen under den demoen:

<img src="../../../translated_images/no/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Verktøykall-syklusen fra Quick Start-demoen — `AiServices` sender meldingen din og verktøyskjemadata til LLM, LLM svarer med et funksjonskall som `add(42, 58)`, LangChain4j utfører `Calculator`-metoden lokalt, og sender resultatet tilbake for det endelige svaret.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) og spør:
> - "Hvordan fungerer ReAct-mønsteret og hvorfor er det effektivt for AI-agenter?"
> - "Hvordan avgjør agenten hvilket verktøy som skal brukes og i hvilken rekkefølge?"
> - "Hva skjer hvis et verktøykall feiler – hvordan bør jeg robust håndtere feil?"

### Generering av svar

Modellen mottar værdata og formaterer det til et svar i naturlig språk til brukeren.

### Arkitektur: Spring Boot Auto-Wiring

Denne modulen bruker LangChain4js Spring Boot-integrasjon med deklarative `@AiService`-grensesnitt. Ved oppstart oppdager Spring Boot alle `@Component` med `@Tool`-metoder, din `ChatModel`-bean og `ChatMemoryProvider` — og kobler dem sammen i et enkelt `Assistant`-grensesnitt uten boilerplate.

<img src="../../../translated_images/no/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService-grensesnittet binder sammen ChatModel, verktøykomponenter og minneprovider — Spring Boot håndterer all koblingen automatisk.*

Her er den komplette forespørselslivssyklusen som et sekvensdiagram — fra HTTP-forespørsel via controller, service og auto-wiret proxy, helt til verktøykjørt og tilbake:

<img src="../../../translated_images/no/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Den komplette Spring Boot-forespørselslivssyklusen — HTTP-forespørselen går gjennom controller og service til den auto-wirede Assistant-proxyen, som orkestrerer LLM og verktøykall automatisk.*

Nøkkelfordeler med denne tilnærmingen:

- **Spring Boot auto-wiring** — ChatModel og verktøy injiseres automatisk
- **@MemoryId-mønster** — Automatisk øktbasert minnehåndtering
- **Enkeltinstans** — Assistant opprettes én gang og gjenbrukes for bedre ytelse
- **Typesikker utførelse** — Java-metoder kalles direkte med typekonvertering
- **Multi-turn orkestrering** — Håndterer kjedede verktøy automatisk
- **Null boilerplate** — Ingen manuelle `AiServices.builder()`-kall eller minne-HashMap

Alternative tilnærminger (manuell `AiServices.builder()`) krever mer kode og mangler Spring Boot-integrasjonens fordeler.

## Kjedede verktøy

**Kjedede verktøy** — Den virkelige styrken til verktøybaserte agenter viser seg når ett enkelt spørsmål krever flere verktøy. Spør "Hvordan er været i Seattle i Fahrenheit?" og agenten kjeder automatisk to verktøy: først kaller den `getCurrentWeather` for temperaturen i Celsius, deretter sender den den verdien til `celsiusToFahrenheit` for omregning — alt i en enkelt samtalerunde.

<img src="../../../translated_images/no/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Kjedede verktøy i praksis — agenten kaller først getCurrentWeather, sender Celsius-resultatet videre til celsiusToFahrenheit, og leverer et kombinert svar.*

**Greie feil** — Spør om vær i en by som ikke finnes i mock-data. Verktøyet returnerer en feilmelding, og AI forklarer at det ikke kan hjelpe i stedet for å krasje. Verktøy feiler trygt. Diagrammet under sammenligner de to tilnærmingene — med skikkelig feilhåndtering fanger agenten unntaket opp og svarer hjelpsomt, uten krasjer:

<img src="../../../translated_images/no/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Når et verktøy feiler, fanger agenten feilen og svarer med en hjelpsom forklaring i stedet for at applikasjonen krasjer.*

Dette skjer i en enkelt samtalerunde. Agenten orkestrerer flere verktøykall autonomt.

## Kjør applikasjonen

**Verifiser distribusjon:**

Sjekk at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (opprettet i Modul 01). Kjør dette fra modulkatalogen (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede har startet alle applikasjoner med `./start-all.sh` fra rotkatalogen (som beskrevet i Modul 01), kjører denne modulen allerede på port 8084. Du kan hoppe over start-kommandoene under og gå direkte til http://localhost:8084.

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Dev containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir en visuell grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetslinjen til venstre i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk bare på play-knappen ved siden av "tools" for å starte denne modulen, eller start alle moduler samtidig.

Slik ser Spring Boot Dashboard ut i VS Code:

<img src="../../../translated_images/no/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stopp og overvåk alle moduler på ett sted*

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og bygger JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før du starter:
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

Åpne http://localhost:8084 i nettleseren din.

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

Applikasjonen gir en webgrensesnitt der du kan samhandle med en AI-agent som har tilgang til verktøy for vær og temperaturkonvertering. Slik ser grensesnittet ut — det inkluderer raske start-eksempler og et chattpanel for å sende forespørsler:

<a href="images/tools-homepage.png"><img src="../../../translated_images/no/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools-grensesnittet - raske eksempler og chattegrensesnitt for interaksjon med verktøy*

### Prøv Enkel Verktøybruk

Start med en enkel forespørsel: "Konverter 100 grader Fahrenheit til Celsius". Agenten gjenkjenner at den trenger temperaturkonverteringsverktøyet, kaller det med riktige parametere og returnerer resultatet. Legg merke til hvor naturlig dette føles - du spesifiserte ikke hvilket verktøy som skulle brukes eller hvordan det skulle kalles.

### Test Verktøykjede

Prøv nå noe mer komplekst: "Hvordan er været i Seattle og konverter det til Fahrenheit?" Se agenten jobbe gjennom dette trinnvis. Først hentes været (som returnerer i Celsius), agenten gjenkjenner at den må konvertere til Fahrenheit, kaller konverteringsverktøyet og kombinerer begge resultatene i ett svar.

### Se Samtale-flyten

Chattesystemet opprettholder samtalehistorikk, slik at du kan ha flertrinnsinteraksjoner. Du kan se alle tidligere forespørsler og svar, noe som gjør det enkelt å følge samtalen og forstå hvordan agenten bygger kontekst over flere utvekslinger.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/no/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flertrinnssamtale som viser enkle konverteringer, værforespørsler og verktøykjeder*

### Eksperimenter med Ulike Forespørsler

Prøv ulike kombinasjoner:
- Værforespørsler: "Hvordan er været i Tokyo?"
- Temperaturkonverteringer: "Hva er 25°C i Kelvin?"
- Kombinerte forespørsler: "Sjekk været i Paris og si om det er over 20°C"

Legg merke til hvordan agenten tolker naturlig språk og kartlegger det til passende verktøykall.

## Nøkkelkonsepter

### ReAct-mønster (Resonnering og Handling)

Agenten veksler mellom resonnering (å bestemme hva som skal gjøres) og handling (å bruke verktøy). Dette mønsteret muliggjør autonom problemløsning i stedet for bare å svare på instruksjoner.

### Verktøybeskrivelser Har Betydning

Kvaliteten på verktøybeskrivelsene dine påvirker direkte hvor godt agenten bruker dem. Klare, spesifikke beskrivelser hjelper modellen å forstå når og hvordan hvert verktøy skal kalles.

### Øktstyring

`@MemoryId`-annotasjonen muliggjør automatisk øktbasert minnehåndtering. Hver økt-ID får sin egen `ChatMemory`-instans som administreres av `ChatMemoryProvider`-bønnen, slik at flere brukere kan samhandle med agenten samtidig uten å blande samtalene sine. Diagrammet nedenfor viser hvordan flere brukere rutes til isolerte minnelagre basert på økt-IDene sine:

<img src="../../../translated_images/no/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Hver økt-ID kobles til en isolert samtalehistorikk — brukere ser aldri hverandres meldinger.*

### Feilhåndtering

Verktøy kan feile — API-er kan tidsavbrytes, parametere kan være ugyldige, eksterne tjenester kan gå ned. Produksjonsagenter trenger feilhåndtering slik at modellen kan forklare problemer eller prøve alternative løsninger uten at applikasjonen krasjer. Når et verktøy kaster en unntak, fanger LangChain4j det og sender feilmeldingen tilbake til modellen, som så kan forklare problemet på naturlig språk.

## Tilgjengelige Verktøy

Diagrammet nedenfor viser det brede økosystemet av verktøy du kan bygge. Denne modulen demonstrerer vær- og temperaturverktøy, men samme `@Tool`-mønster fungerer for enhver Java-metode — fra databaseforespørsler til betalingstransaksjoner.

<img src="../../../translated_images/no/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Enhver Java-metode annotert med @Tool blir tilgjengelig for AI — mønsteret utvides til databaser, API-er, e-post, filoperasjoner og mer.*

## Når Bruke Verktøybaserte Agenter

Ikke alle forespørsler trenger verktøy. Beslutningen avhenger av om AI-en må samhandle med eksterne systemer eller kan svare ut fra sin egen kunnskap. Veiledningen nedenfor oppsummerer når verktøy tilfører verdi og når de ikke er nødvendige:

<img src="../../../translated_images/no/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*En rask beslutningsguide — verktøy er for sanntidsdata, beregninger og handlinger; generell kunnskap og kreative oppgaver trenger dem ikke.*

## Verktøy vs RAG

Modulene 03 og 04 utvider begge hva AI-en kan gjøre, men på fundamentalt forskjellige måter. RAG gir modellen tilgang til **kunnskap** ved å hente dokumenter. Verktøy gir modellen mulighet til å utføre **handlinger** ved å kalle funksjoner. Diagrammet nedenfor sammenligner disse to tilnærmingene side om side — fra hvordan hver arbeidsflyt opererer til kompromissene mellom dem:

<img src="../../../translated_images/no/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG henter informasjon fra statiske dokumenter — Verktøy utfører handlinger og henter dynamiske, sanntidsdata. Mange produksjonssystemer kombinerer begge.*

I praksis kombinerer mange produksjonssystemer begge tilnærmingene: RAG for å forankre svar i dokumentasjonen din, og Verktøy for å hente live data eller utføre operasjoner.

## Neste Steg

**Neste Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigasjon:** [← Forrige: Modul 03 - RAG](../03-rag/README.md) | [Tilbake til Hovedmeny](../README.md) | [Neste: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på sitt originale språk skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->