# Modul 04: AI-agenter med verktøy

## Innholdsfortegnelse

- [Hva du vil lære](../../../04-tools)
- [Forutsetninger](../../../04-tools)
- [Forstå AI-agenter med verktøy](../../../04-tools)
- [Hvordan verktøynummerering fungerer](../../../04-tools)
  - [Verktøydefinisjoner](../../../04-tools)
  - [Beslutningstaking](../../../04-tools)
  - [Utførelse](../../../04-tools)
  - [Svargenerering](../../../04-tools)
  - [Arkitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Kjedning av verktøy](../../../04-tools)
- [Kjør applikasjonen](../../../04-tools)
- [Bruke applikasjonen](../../../04-tools)
  - [Prøv enkel verktøybruk](../../../04-tools)
  - [Test kjeding av verktøy](../../../04-tools)
  - [Se samtaleflyt](../../../04-tools)
  - [Eksperimenter med ulike forespørsler](../../../04-tools)
- [Nøkkelkonsepter](../../../04-tools)
  - [ReAct-mønster (Resonnering og Handling)](../../../04-tools)
  - [Verktøybeskrivelser er viktige](../../../04-tools)
  - [Sesjonsadministrasjon](../../../04-tools)
  - [Feilhåndtering](../../../04-tools)
- [Tilgjengelige verktøy](../../../04-tools)
- [Når bør man bruke verktøybaserte agenter](../../../04-tools)
- [Verktøy vs RAG](../../../04-tools)
- [Neste steg](../../../04-tools)

## Hva du vil lære

Så langt har du lært hvordan du kan ha samtaler med AI, strukturere prompts effektivt, og forankre svar i dine dokumenter. Men det er fortsatt en grunnleggende begrensning: språkmodeller kan kun generere tekst. De kan ikke sjekke været, utføre beregninger, spørre databaser, eller samhandle med eksterne systemer.

Verktøy endrer dette. Ved å gi modellen tilgang til funksjoner den kan kalle, forvandler du den fra en tekstgenerator til en agent som kan ta handlinger. Modellen bestemmer når den trenger et verktøy, hvilket verktøy som skal brukes, og hvilke parametere som skal sendes. Koden din utfører funksjonen og returnerer resultatet. Modellen inkorporerer dette resultatet i sitt svar.

## Forutsetninger

- Fullført [Modul 01 - Introduksjon](../01-introduction/README.md) (Azure OpenAI-ressurser deployert)
- Tidligere moduler anbefales fullført (denne modulen refererer til [RAG-konsepter fra Modul 03](../03-rag/README.md) i sammenligning av Verktøy vs RAG)
- `.env`-fil i rotmappen med Azure-legitimasjon (opprettet med `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg deploy-instruksjonene der først.

## Forstå AI-agenter med verktøy

> **📝 Merk:** Begrepet "agenter" i denne modulen refererer til AI-assistenter utvidet med verktøykall-funksjonalitet. Dette skiller seg fra **Agentic AI**-mønstrene (autonome agenter med planlegging, hukommelse og flerstegs resonnement) som vi vil dekke i [Modul 05: MCP](../05-mcp/README.md).

Uten verktøy kan en språkmodell bare generere tekst basert på treningsdataene sine. Spør den om været nå, og den må gjette. Gi den verktøy, og den kan kalle en vær-API, utføre beregninger, eller hente data fra en database — for så å veve disse sanne resultatene inn i svaret.

<img src="../../../translated_images/no/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Uten verktøy kan modellen bare gjette — med verktøy kan den kalle APIer, kjøre beregninger, og returnere sanntidsdata.*

En AI-agent med verktøy følger et **Reasoning and Acting (ReAct)**-mønster. Modellen bare svarer ikke — den tenker på hva den trenger, handler ved å kalle et verktøy, observerer resultatet, og bestemmer så om den skal handle igjen eller levere det endelige svaret:

1. **Resonner** — Agenten analyserer brukerens spørsmål og avgjør hvilken informasjon som trengs
2. **Handle** — Agenten velger riktig verktøy, genererer korrekte parametere, og kaller det
3. **Observer** — Agenten mottar verktøyets utdata og evaluerer resultatet
4. **Gjenta eller Svar** — Hvis mer data trengs, går agenten tilbake i løkken; ellers komponerer den et naturlig språk-svar

<img src="../../../translated_images/no/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-syklusen — agenten resonerer om hva som skal gjøres, handler ved å kalle et verktøy, observerer resultatet, og gjentar til den kan levere det endelige svaret.*

Dette skjer automatisk. Du definerer verktøyene og deres beskrivelser. Modellen håndterer beslutningen om når og hvordan de skal brukes.

## Hvordan verktøynummerering fungerer

### Verktøydefinisjoner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definerer funksjoner med klare beskrivelser og parameterversjoner. Modellen ser disse beskrivelsene i system-prompten sin og forstår hva hvert verktøy gjør.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Logikken din for væroppslag
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistenten er automatisk koblet til av Spring Boot med:
// - ChatModel bean
// - Alle @Tool-metoder fra @Component-klasser
// - ChatMemoryProvider for sesjonsstyring
```

Diagrammet under bryter ned hver annotasjon og viser hvordan hver del hjelper AI med å forstå når verktøyet skal kalles og hvilke argumenter som skal sendes:

<img src="../../../translated_images/no/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi av en verktøydefinisjon — @Tool forteller AI når det skal brukes, @P beskriver hver parameter, og @AiService kobler alt sammen ved oppstart.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) og spør:
> - "Hvordan kan jeg integrere en ekte vær-API som OpenWeatherMap istedenfor mock-data?"
> - "Hva kjennetegner en god verktøybeskrivelse som hjelper AI med å bruke det riktig?"
> - "Hvordan håndterer jeg API-feil og ratelimiting i verktøyimplementasjoner?"

### Beslutningstaking

Når en bruker spør "Hva er været i Seattle?", velger modellen ikke verktøy tilfeldig. Den sammenligner brukerens intensjon med hver verktøybeskrivelse den har tilgang til, vurderer relevansen, og velger det beste treffet. Den genererer så et strukturert funksjonskall med riktige parametere — i dette tilfellet setter den `location` til `"Seattle"`.

Hvis ingen verktøy matcher brukerens forespørsel, faller modellen tilbake på å svare ut fra egen kunnskap. Hvis flere verktøy matcher, velger den det mest spesifikke.

<img src="../../../translated_images/no/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modellen vurderer hvert tilgjengelig verktøy opp mot brukerens intensjon og velger det beste – derfor er det viktig å skrive klare og spesifikke verktøybeskrivelser.*

### Utførelse

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot kobler automatisk den deklarative `@AiService`-grensesnittet med alle registrerte verktøy, og LangChain4j utfører verktøykall automatisk. Bak kulissene flyter et komplett verktøykall gjennom seks steg — fra brukerens naturlige språklige spørsmål hele veien tilbake til et naturlig språk-svar:

<img src="../../../translated_images/no/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Ende-til-ende flyt — brukeren stiller et spørsmål, modellen velger et verktøy, LangChain4j utfører det, og modellen vever resultatet inn i et naturlig svar.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) og spør:
> - "Hvordan fungerer ReAct-mønsteret og hvorfor er det effektivt for AI-agenter?"
> - "Hvordan avgjør agenten hvilket verktøy som skal brukes og i hvilken rekkefølge?"
> - "Hva skjer hvis et verktøykall feiler — hvordan bør jeg håndtere feil robust?"

### Svargenerering

Modellen mottar værdata og formaterer det til et naturlig språk-svar til brukeren.

### Arkitektur: Spring Boot Auto-Wiring

Denne modulen bruker LangChain4j sin Spring Boot-integrasjon med deklarative `@AiService`-grensesnitt. Ved oppstart oppdager Spring Boot alle `@Component` som inneholder `@Tool`-metoder, `ChatModel` bean, og `ChatMemoryProvider` — og kobler alt sammen til ett `Assistant`-grensesnitt uten noe rutinearbeid.

<img src="../../../translated_images/no/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService-grensesnittet knytter sammen ChatModel, verktøykomponenter, og memory provider — Spring Boot håndterer all koblingen automatisk.*

Nøkkelfordeler med denne tilnærmingen:

- **Spring Boot auto-wiring** — ChatModel og verktøy injiseres automatisk
- **@MemoryId-mønster** — Automatisk minnehåndtering per sesjon
- **Én enkelt instans** — Assistant opprettes én gang og gjenbrukes for bedre ytelse
- **Type-sikker utførelse** — Java-metoder kalles direkte med typkonvertering
- **Orkestrering av multi-turn** — Håndterer verktøykjeder automatisk
- **Null boilerplate** — Ingen manuelle `AiServices.builder()`-kall eller minne-hashmap

Alternative tilnærminger (manuelle `AiServices.builder()`) krever mer kode og går glipp av Spring Boot-integrasjonsfordelene.

## Kjedning av verktøy

**Kjedning av verktøy** — Den virkelige kraften til verktøybaserte agenter vises når ett spørsmål krever flere verktøy. Spør "Hva er været i Seattle i Fahrenheit?" og agenten kjeder automatisk to verktøy: først kaller den `getCurrentWeather` for å hente temperaturen i Celsius, deretter sender den verdien til `celsiusToFahrenheit` for konvertering — alt i en enkelt samtalerunde.

<img src="../../../translated_images/no/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Kjedning av verktøy i praksis — agenten kaller først getCurrentWeather, pipler deretter Celsius-resultatet inn i celsiusToFahrenheit, og leverer et kombinert svar.*

**Graceful Failures** — Be om vær i en by som ikke finnes i mock-dataene. Verktøyet returnerer en feilmelding, og AI forklarer at den ikke kan hjelpe i stedet for å krasje. Verktøy feiler trygt. Diagrammet under sammenligner de to tilnærmingene — med riktig feilhåndtering fanger agenten unntaket og svarer hjelpsomt, uten krasj, mens uten dette krasjer hele applikasjonen:

<img src="../../../translated_images/no/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Når et verktøy feiler, fanger agenten feilen og svarer med en hjelpsom forklaring i stedet for å krasje.*

Dette skjer i en enkelt samtalerunde. Agenten orkestrerer flere verktøykall autonomt.

## Kjør applikasjonen

**Bekreft deploy:**

Sørg for at `.env`-filen finnes i rotmappen med Azure-legitimasjon (opprettet under Modul 01). Kjør dette fra modulkatalogen (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede har startet alle applikasjoner med `./start-all.sh` fra rotmappen (som beskrevet i Modul 01), kjører denne modulen allerede på port 8084. Du kan hoppe over startkommandoene under og gå direkte til http://localhost:8084.

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Dev-containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetspanelet på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk på play-knappen ved siden av "tools" for å starte denne modulen, eller start alle moduler samtidig.

Slik ser Spring Boot Dashboard ut i VS Code:

<img src="../../../translated_images/no/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stopp og overvåk alle moduler fra ett sted*

**Alternativ 2: Bruke shell-scripts**

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

Eller start kun denne modulen:

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

Begge skriptene laster automatisk miljøvariabler fra rotens `.env`-fil og vil bygge JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før start:
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
.\stop.ps1  # Bare denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Bruke applikasjonen

Applikasjonen tilbyr et webgrensesnitt hvor du kan interagere med en AI-agent som har tilgang til verktøy for vær- og temperaturkonvertering. Slik ser grensesnittet ut — det inkluderer hurtigstart-eksempler og et chatpanel for å sende forespørsler:
<a href="images/tools-homepage.png"><img src="../../../translated_images/no/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools-grensesnittet - raske eksempler og chatgrensesnitt for samhandling med verktøy*

### Prøv enkel verktøybruk

Start med en enkel forespørsel: "Konverter 100 grader Fahrenheit til Celsius". agenten gjenkjenner at den trenger temperaturkonverteringsverktøyet, kaller det med riktige parametere, og returnerer resultatet. Legg merke til hvor naturlig dette føles - du spesifiserte ikke hvilket verktøy som skulle brukes eller hvordan det skulle kalles.

### Test verktøykjede

Prøv nå noe mer komplekst: "Hvordan er været i Seattle og konverter det til Fahrenheit?" Se hvordan agenten jobber gjennom dette i trinn. Den henter først været (som returnerer Celsius), gjenkjenner at den må konvertere til Fahrenheit, kaller konverteringsverktøyet, og kombinerer begge resultater i ett svar.

### Se samtaleflyt

Chatgrensesnittet opprettholder samtalehistorikk, slik at du kan ha flerpartsinteraksjoner. Du kan se alle tidligere spørsmål og svar, noe som gjør det enkelt å følge samtalen og forstå hvordan agenten bygger kontekst over flere utvekslinger.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/no/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flerpartsamtale som viser enkle konverteringer, værspørringer og verktøykjede*

### Eksperimenter med ulike forespørsler

Prøv ulike kombinasjoner:
- Værspørringer: "Hvordan er været i Tokyo?"
- Temperaturkonverteringer: "Hva er 25°C i Kelvin?"
- Kombinerte spørsmål: "Sjekk været i Paris og fortell meg om det er over 20°C"

Legg merke til hvordan agenten tolker naturlig språk og kobler det til hensiktsmessige verktøyskall.

## Nøkkelbegreper

### ReAct-mønster (Resonnering og handling)

Agenten veksler mellom å resonnere (bestemme hva som skal gjøres) og å handle (bruke verktøy). Dette mønsteret muliggjør autonom problemløsning i stedet for bare å svare på instruksjoner.

### Verktøybeskrivelser betyr noe

Kvaliteten på beskrivelsene av verktøyene dine påvirker direkte hvor godt agenten bruker dem. Klare, spesifikke beskrivelser hjelper modellen å forstå når og hvordan hvert verktøy skal kalles.

### Øktstyring

`@MemoryId`-annotasjonen muliggjør automatisk minnehåndtering basert på økter. Hver økt-ID får sin egen `ChatMemory`-instans som administreres av `ChatMemoryProvider`-beanen, slik at flere brukere kan samhandle med agenten samtidig uten at samtalene blandes sammen. Følgende diagram viser hvordan flere brukere blir rutet til isolerte minnelagre basert på sine økt-IDer:

<img src="../../../translated_images/no/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Hver økt-ID kobles til en isolert samtalehistorikk — brukere ser aldri hverandres meldinger.*

### Feilhåndtering

Verktøy kan feile — API-er kan tidsavbrytes, parametere kan være ugyldige, eksterne tjenester kan gå ned. Produksjonsagenter trenger feilhåndtering slik at modellen kan forklare problemer eller prøve alternativer i stedet for å krasje hele applikasjonen. Når et verktøy kaster et unntak, fanger LangChain4j det og sender feilmeldingen tilbake til modellen, som deretter kan forklare problemet på naturlig språk.

## Tilgjengelige verktøy

Diagrammet nedenfor viser det brede økosystemet av verktøy du kan bygge. Denne modulen demonstrerer vær- og temperaturverktøy, men samme `@Tool`-mønster fungerer for alle Java-metoder — fra databaseforespørsler til betalingsbehandling.

<img src="../../../translated_images/no/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Enhver Java-metode annotert med @Tool blir tilgjengelig for AI — mønsteret strekker seg til databaser, API-er, e-post, filoperasjoner og mer.*

## Når bruke verktøybaserte agenter

Ikke alle forespørsler trenger verktøy. Beslutningen avhenger av om AI-en må samhandle med eksterne systemer eller kan svare fra sin egen kunnskap. Følgende guide oppsummerer når verktøy tilfører verdi og når de ikke er nødvendig:

<img src="../../../translated_images/no/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*En rask beslutningsguide — verktøy er for sanntidsdata, beregninger og handlinger; generell kunnskap og kreative oppgaver trenger ikke verktøy.*

## Verktøy vs RAG

Modul 03 og 04 utvider begge hva AI kan gjøre, men på fundamentalt forskjellige måter. RAG gir modellen tilgang til **kunnskap** ved å hente dokumenter. Verktøy gir modellen mulighet til å utføre **handlinger** ved å kalle funksjoner. Diagrammet nedenfor sammenligner disse to tilnærmingene side om side — fra hvordan hver arbeidsflyt fungerer til kompromissene mellom dem:

<img src="../../../translated_images/no/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG henter informasjon fra statiske dokumenter — Verktøy utfører handlinger og henter dynamiske, sanntidsdata. Mange produksjonssystemer kombinerer begge.*

I praksis kombinerer mange produksjonssystemer begge tilnærminger: RAG for å forankre svar i dokumentasjonen din, og Verktøy for å hente live data eller utføre operasjoner.

## Neste steg

**Neste modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigasjon:** [← Forrige: Modul 03 - RAG](../03-rag/README.md) | [Tilbake til Hovedside](../README.md) | [Neste: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->