# Modul 04: AI-agenter med værktøjer

## Indholdsfortegnelse

- [Hvad du vil lære](../../../04-tools)
- [Forudsætninger](../../../04-tools)
- [Forståelse af AI-agenter med værktøjer](../../../04-tools)
- [Hvordan værktøjsopkald fungerer](../../../04-tools)
  - [Værktøjsdefinitioner](../../../04-tools)
  - [Beslutningstagning](../../../04-tools)
  - [Udførelse](../../../04-tools)
  - [Responsgenerering](../../../04-tools)
  - [Arkitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Værktøjskædning](../../../04-tools)
- [Kør applikationen](../../../04-tools)
- [Brug af applikationen](../../../04-tools)
  - [Prøv simpel værktøjsbrug](../../../04-tools)
  - [Test værktøjskædning](../../../04-tools)
  - [Se samtaleflow](../../../04-tools)
  - [Eksperimenter med forskellige forespørgsler](../../../04-tools)
- [Nøglebegreber](../../../04-tools)
  - [ReAct-mønsteret (Resonering og Handling)](../../../04-tools)
  - [Værktøjsbeskrivelser betyder noget](../../../04-tools)
  - [Sessionshåndtering](../../../04-tools)
  - [Fejlhåndtering](../../../04-tools)
- [Tilgængelige værktøjer](../../../04-tools)
- [Hvornår man skal bruge værktøjsbaserede agenter](../../../04-tools)
- [Værktøjer vs RAG](../../../04-tools)
- [Næste skridt](../../../04-tools)

## Hvad du vil lære

Indtil nu har du lært, hvordan man har samtaler med AI, strukturerer prompts effektivt og forankrer svar i dine dokumenter. Men der er stadig en grundlæggende begrænsning: sprogmodeller kan kun generere tekst. De kan ikke tjekke vejret, udføre beregninger, forespørge databaser eller interagere med eksterne systemer.

Værktøjer ændrer dette. Ved at give modellen adgang til funktioner, den kan kalde, forvandler du den fra en tekstgenerator til en agent, der kan handle. Modellen beslutter, hvornår den har brug for et værktøj, hvilket værktøj der skal bruges, og hvilke parametre der skal overføres. Din kode udfører funktionen og returnerer resultatet. Modellen indarbejder dette resultat i sit svar.

## Forudsætninger

- Gennemført Modul 01 (Azure OpenAI-ressourcer implementeret)
- `.env`-fil i rodmappen med Azure-legitimationsoplysninger (oprettet af `azd up` i Modul 01)

> **Note:** Hvis du ikke har gennemført Modul 01, følg først implementeringsinstruktionerne der.

## Forståelse af AI-agenter med værktøjer

> **📝 Note:** Udtrykket "agenter" i dette modul refererer til AI-assistenter, der er forbedret med værktøjsopkaldsfunktionalitet. Dette er forskelligt fra **Agentic AI**-mønstrene (autonome agenter med planlægning, hukommelse og ræsonnering i flere trin), som vi vil dække i [Modul 05: MCP](../05-mcp/README.md).

Uden værktøjer kan en sprogmodel kun generere tekst ud fra sine træningsdata. Spørg den om det aktuelle vejr, og den må gætte. Giv den værktøjer, og den kan kalde en vejr-API, foretage beregninger eller forespørge en database — og derefter væve disse virkelige resultater ind i sit svar.

<img src="../../../translated_images/da/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Uden værktøjer kan modellen kun gætte — med værktøjer kan den kalde API'er, køre beregninger og returnere data i realtid.*

En AI-agent med værktøjer følger et **Reasoning and Acting (ReAct)**-mønster. Modellen svarer ikke bare — den tænker over, hvad den har brug for, handler ved at kalde et værktøj, observerer resultatet og beslutter derefter, om den skal handle igen eller levere det endelige svar:

1. **Resonér** — Agenten analyserer brugerens spørgsmål og afgør, hvilken information den har brug for
2. **Handl** — Agenten vælger det rigtige værktøj, genererer de korrekte parametre og kalder det
3. **Observer** — Agenten modtager værktøjets output og evaluerer resultatet
4. **Gentag eller svar** — Hvis der er brug for flere data, går agenten tilbage; ellers formulerer den et svar på naturligt sprog

<img src="../../../translated_images/da/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-cyklussen — agenten resonnerer om, hvad den skal gøre, handler ved at kalde et værktøj, observerer resultatet og gentager, indtil den kan levere det endelige svar.*

Dette sker automatisk. Du definerer værktøjerne og deres beskrivelser. Modellen håndterer beslutninger om, hvornår og hvordan de skal bruges.

## Hvordan værktøjsopkald fungerer

### Værktøjsdefinitioner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definerer funktioner med klare beskrivelser og parameter-specifikationer. Modellen ser disse beskrivelser i sit systemprompt og forstår, hvad hvert værktøj gør.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Din vejropslagslogik
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent er automatisk tilknyttet af Spring Boot med:
// - ChatModel bean
// - Alle @Tool metoder fra @Component klasser
// - ChatMemoryProvider til sessionsstyring
```

Diagrammet nedenfor gennemgår hver annotation og viser, hvordan hvert element hjælper AI med at forstå, hvornår værktøjet skal kaldes, og hvilke argumenter der skal overføres:

<img src="../../../translated_images/da/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi af en værktøjsdefinition — @Tool fortæller AI, hvornår det skal bruges, @P beskriver hver parameter, og @AiService kobler det hele sammen ved opstart.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) og spørg:
> - "Hvordan integrerer jeg en rigtig vejr-API som OpenWeatherMap i stedet for mock-data?"
> - "Hvad udgør en god værktøjsbeskrivelse, der hjælper AI med korrekt brug?"
> - "Hvordan håndterer jeg API-fejl og grænsebegrænsninger i værktøjsimplementeringer?"

### Beslutningstagning

Når en bruger spørger "Hvordan er vejret i Seattle?", vælger modellen ikke tilfældigt et værktøj. Den sammenligner brugerens hensigt med hver værktøjsbeskrivelse, den har adgang til, scorer dem for relevans og vælger det bedste match. Derefter genererer den et struktureret funktionsopkald med de rigtige parametre — i dette tilfælde sætter den `location` til `"Seattle"`.

Hvis intet værktøj matcher brugerens forespørgsel, falder modellen tilbage til at svare ud fra sin egen viden. Hvis flere værktøjer matcher, vælger den det mest specifikke.

<img src="../../../translated_images/da/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modellen vurderer hvert tilgængeligt værktøj i forhold til brugerens hensigt og vælger det bedste match — derfor er det vigtigt at skrive klare, specifikke værktøjsbeskrivelser.*

### Udførelse

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot autowirer det deklarative `@AiService` interface med alle registrerede værktøjer, og LangChain4j udfører værktøjsopkald automatisk. Bag kulisserne gennemløber et komplet værktøjsopkald seks faser — fra brugerens spørgsmål på naturligt sprog til et svar også på naturligt sprog:

<img src="../../../translated_images/da/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Hele flowet — brugeren stiller et spørgsmål, modellen vælger et værktøj, LangChain4j udfører det, og modellen væver resultatet ind i et naturligt svar.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) og spørg:
> - "Hvordan fungerer ReAct-mønstret og hvorfor er det effektivt for AI-agenter?"
> - "Hvordan beslutter agenten, hvilket værktøj der skal bruges og i hvilken rækkefølge?"
> - "Hvad sker der, hvis et værktøjskald fejler - hvordan håndterer jeg fejl robust?"

### Responsgenerering

Modellen modtager vejrudsigten og formaterer den til et svar på naturligt sprog til brugeren.

### Arkitektur: Spring Boot Auto-Wiring

Dette modul bruger LangChain4js Spring Boot-integration med deklarative `@AiService`-interfaces. Ved opstart opdager Spring Boot hver `@Component`, der indeholder `@Tool`-metoder, din `ChatModel` bean og `ChatMemoryProvider` — og kobler dem alle sammen i et enkelt `Assistant` interface uden brug af boilerplate.

<img src="../../../translated_images/da/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService interfacet binder ChatModel, værktøjskomponenter og hukommelsesudbyder sammen — Spring Boot håndterer alle tilkoblingerne automatisk.*

Nøglefordele ved denne tilgang:

- **Spring Boot autowiring** — ChatModel og værktøjer injiceres automatisk
- **@MemoryId mønster** — Automatisk sessionbaseret hukommelsesstyring
- **Én enkelt instans** — Assistant oprettes én gang og genbruges for bedre ydeevne
- **Typsikker udførelse** — Java-metoder kaldes direkte med typekonvertering
- **Multi-turn orkestrering** — Håndterer værktøjskædning automatisk
- **Ingen boilerplate** — Ingen manuelle `AiServices.builder()`-kald eller hukommelses-HashMap

Alternative tilgange (manuelt `AiServices.builder()`) kræver mere kode og mangler Spring Boot-integrationsfordelene.

## Værktøjskædning

**Værktøjskædning** — Den reelle styrke ved værktøjsbaserede agenter viser sig, når et enkelt spørgsmål kræver flere værktøjer. Spørg "Hvordan er vejret i Seattle i Fahrenheit?" og agenten kæder automatisk to værktøjer sammen: først kaldes `getCurrentWeather` for at få temperaturen i Celsius, og derefter overføres den værdi til `celsiusToFahrenheit` for konvertering — alt i en enkelt samtalerunde.

<img src="../../../translated_images/da/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Værktøjskædning i praksis — agenten kalder først getCurrentWeather, sender derefter Celsius-resultatet til celsiusToFahrenheit og leverer et samlet svar.*

Sådan ser det ud i den kørende applikation — agenten kæder to værktøjsopkald sammen i en enkelt samtalerunde:

<a href="images/tool-chaining.png"><img src="../../../translated_images/da/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Faktisk applikationsoutput — agenten kæder automatisk getCurrentWeather → celsiusToFahrenheit i én runde.*

**Elegant fejlhåndtering** — Spørg om vejret i en by, der ikke findes i mock-dataene. Værktøjet returnerer en fejlmeddelelse, og AI forklarer, at det ikke kan hjælpe, i stedet for at gå ned. Værktøjer fejler sikkert.

<img src="../../../translated_images/da/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Når et værktøj fejler, opfanger agenten fejlen og svarer med en hjælpsom forklaring fremfor at gå ned.*

Dette sker i en enkelt samtalerunde. Agenten orkestrerer flere værktøjsopkald autonomt.

## Kør applikationen

**Bekræft implementering:**

Sørg for, at `.env`-filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Note:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8084. Du kan springe startkommandoerne over og gå direkte til http://localhost:8084.

**Mulighed 1: Brug af Spring Boot Dashboard (Anbefalet til VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som tilbyder en visuel grænseflade til administration af alle Spring Boot-applikationer. Den findes i aktivitetslinjen til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i workspace
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationsstatus

Klik blot på play-knappen ved siden af "tools" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug af shell scripts**

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env`-fil og bygger JAR-filerne, hvis de ikke findes.

> **Note:** Foretrækker du at bygge alle moduler manuelt inden start:
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

Åbn http://localhost:8084 i din browser.

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

Applikationen giver en webgrænseflade, hvor du kan interagere med en AI-agent, der har adgang til værktøjer til vejr og temperaturkonvertering.

<a href="images/tools-homepage.png"><img src="../../../translated_images/da/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools-grænseflade – hurtige eksempler og chat-interface til interaktion med værktøjer*

### Prøv simpel værktøjsbrug
Start med en ligetil forespørgsel: "Konverter 100 grader Fahrenheit til Celsius". agenten genkender, at den har brug for temperaturkonverteringsværktøjet, kalder det med de rigtige parametre og returnerer resultatet. Bemærk, hvor naturligt dette føles - du specificerede ikke, hvilket værktøj der skulle bruges, eller hvordan det skulle kaldes.

### Test Kædning af Værktøjer

Prøv nu noget mere komplekst: "Hvordan er vejret i Seattle, og konverter det til Fahrenheit?" Se agenten arbejde igennem dette i trin. Den henter først vejret (som returnerer Celsius), genkender, at det skal konverteres til Fahrenheit, kalder konverteringsværktøjet og kombinerer begge resultater i ét svar.

### Se Samtale Flow

Chatgrænsefladen opretholder samtalehistorik, så du kan have samtaler over flere runder. Du kan se alle tidligere forespørgsler og svar, hvilket gør det nemt at følge samtalen og forstå, hvordan agenten bygger kontekst over flere udvekslinger.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/da/tools-conversation-demo.89f2ce9676080f59.webp" alt="Samtale med flere kald til værktøjer" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flertrins samtale der viser simple konverteringer, vejropslag og kædning af værktøjer*

### Eksperimenter med Forskellige Forespørgsler

Prøv forskellige kombinationer:
- Vejropslag: "Hvordan er vejret i Tokyo?"
- Temperaturkonverteringer: "Hvad er 25°C i Kelvin?"
- Kombinerede forespørgsler: "Tjek vejret i Paris og fortæl mig, om det er over 20°C"

Bemærk, hvordan agenten fortolker naturligt sprog og kortlægger det til passende kald til værktøjer.

## Nøglebegreber

### ReAct Mønster (Resonnering og Handling)

Agenten skifter mellem resonnering (beslutte hvad der skal gøres) og handling (bruge værktøjer). Dette mønster muliggør autonom problemløsning i stedet for blot at svare på instruktioner.

### Værktøjsbeskrivelser betyder noget

Kvaliteten af dine værktøjsbeskrivelser påvirker direkte, hvor godt agenten bruger dem. Klare, specifikke beskrivelser hjælper modellen med at forstå, hvornår og hvordan hvert værktøj skal kaldes.

### Session Management

`@MemoryId` annoteringen muliggør automatisk session-baseret hukommelsesstyring. Hver session-id får sin egen `ChatMemory` instans, der administreres af `ChatMemoryProvider` bean, så flere brugere kan interagere med agenten samtidigt uden at deres samtaler blandes.

<img src="../../../translated_images/da/session-management.91ad819c6c89c400.webp" alt="Session Management med @MemoryId" width="800"/>

*Hver session-id kortlægges til en isoleret samtalehistorik — brugere ser aldrig hinandens beskeder.*

### Fejlhåndtering

Værktøjer kan fejle — API’er kan tidsudløbe, parametre kan være ugyldige, eksterne tjenester kan være nede. Produktionsagenter har brug for fejlhåndtering, så modellen kan forklare problemer eller prøve alternativer i stedet for at få hele applikationen til at crashe. Når et værktøj kaster en undtagelse, fanger LangChain4j den og sender fejlmeddelelsen tilbage til modellen, som så kan forklare problemet på naturligt sprog.

## Tilgængelige Værktøjer

Diagrammet nedenfor viser det brede økosystem af værktøjer, du kan bygge. Dette modul demonstrerer vejr- og temperaturværktøjer, men det samme `@Tool` mønster virker for enhver Java-metode — fra databaseforespørgsler til betalingsbehandling.

<img src="../../../translated_images/da/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Værktøjsøkosystem" width="800"/>

*Enhver Java-metode annoteret med @Tool bliver tilgængelig for AI — mønsteret udvides til databaser, API’er, e-mail, filoperationer og mere.*

## Hvornår Skal Man Bruge Værktøjsbaserede Agenter

<img src="../../../translated_images/da/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Hvornår man skal bruge værktøjer" width="800"/>

*En hurtig beslutningsguide — værktøjer er til realtidsdata, beregninger og handlinger; generel viden og kreative opgaver behøver dem ikke.*

**Brug værktøjer når:**
- Svaret kræver realtidsdata (vejr, aktiekurser, lagerstatus)
- Du skal udføre beregninger ud over simpel matematik
- Tilgang til databaser eller API’er
- Tage handlinger (sende emails, oprette sager, opdatere poster)
- Kombinere flere datakilder

**Brug ikke værktøjer når:**
- Spørgsmål kan besvares ud fra generel viden
- Svaret er rent samtalemæssigt
- Værktøjslatens ville gøre oplevelsen for langsom

## Værktøjer vs RAG

Modulerne 03 og 04 udvider begge, hvad AI kan gøre, men på fundamentalt forskellige måder. RAG giver modellen adgang til **viden** ved at hente dokumenter. Værktøjer giver modellen evnen til at udføre **handlinger** ved at kalde funktioner.

<img src="../../../translated_images/da/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Værktøjer vs RAG sammenligning" width="800"/>

*RAG henter information fra statiske dokumenter — værktøjer udfører handlinger og henter dynamiske, realtidsdata. Mange produktionssystemer kombinerer begge.*

I praksis kombinerer mange produktionssystemer begge tilgange: RAG til at fundere svar i dokumentation, og Værktøjer til at hente live data eller udføre operationer.

## Næste Skridt

**Næste Modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Forrige: Modul 03 - RAG](../03-rag/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->