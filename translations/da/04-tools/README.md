# Modul 04: AI-agenter med værktøjer

## Indholdsfortegnelse

- [Hvad du vil lære](../../../04-tools)
- [Forudsætninger](../../../04-tools)
- [Forståelse af AI-agenter med værktøjer](../../../04-tools)
- [Hvordan værktøjskald fungerer](../../../04-tools)
  - [Værktøjsdefinitioner](../../../04-tools)
  - [Beslutningstagning](../../../04-tools)
  - [Udførelse](../../../04-tools)
  - [Generering af svar](../../../04-tools)
  - [Arkitektur: Spring Boot auto-wiring](../../../04-tools)
- [Kædning af værktøjer](../../../04-tools)
- [Kør applikationen](../../../04-tools)
- [Brug af applikationen](../../../04-tools)
  - [Prøv enkel værktøjsbrug](../../../04-tools)
  - [Test værktøjskædning](../../../04-tools)
  - [Se samtaleflow](../../../04-tools)
  - [Eksperimenter med forskellige forespørgsler](../../../04-tools)
- [Nøglebegreber](../../../04-tools)
  - [ReAct-mønster (Begrundelse og handling)](../../../04-tools)
  - [Beskrivelser af værktøjer betyder noget](../../../04-tools)
  - [Sessionstyring](../../../04-tools)
  - [Fejlhåndtering](../../../04-tools)
- [Tilgængelige værktøjer](../../../04-tools)
- [Hvornår man bruger værktøjsbaserede agenter](../../../04-tools)
- [Værktøjer vs RAG](../../../04-tools)
- [Næste skridt](../../../04-tools)

## Hvad du vil lære

Indtil nu har du lært, hvordan man fører samtaler med AI, strukturerer prompts effektivt og forankrer svar i dine dokumenter. Men der er stadig en grundlæggende begrænsning: sproglige modeller kan kun generere tekst. De kan ikke tjekke vejret, udføre beregninger, forespørge databaser eller interagere med eksterne systemer.

Værktøjer ændrer dette. Ved at give modellen adgang til funktioner, den kan kalde, forvandler du den fra en tekstgenerator til en agent, der kan tage handlinger. Modellen beslutter, hvornår den har brug for et værktøj, hvilket værktøj der skal bruges, og hvilke parametre der skal gives. Din kode udfører funktionen og returnerer resultatet. Modellen indarbejder dette resultat i sit svar.

## Forudsætninger

- Færdiggjort [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-ressourcer implementeret)
- Anbefalet at have færdiggjort tidligere moduler (dette modul refererer til [RAG-konceptet fra Modul 03](../03-rag/README.md) i sammenligningen med værktøjer vs RAG)
- `.env` fil i rodmappen med Azure legitimationsoplysninger (oprettet af `azd up` i Modul 01)

> **Bemærk:** Hvis du ikke har gennemført Modul 01, så følg først implementeringsvejledningen der.

## Forståelse af AI-agenter med værktøjer

> **📝 Bemærk:** Begrebet "agenter" i dette modul refererer til AI-assistenter forbedret med evnen til at kalde værktøjer. Dette er anderledes end **Agentic AI** mønstre (autonome agenter med planlægning, hukommelse og ræsonnering over flere trin), som vi gennemgår i [Modul 05: MCP](../05-mcp/README.md).

Uden værktøjer kan en sprogmodel kun generere tekst baseret på dens træningsdata. Spørg den om vejret lige nu, og den må gætte. Giv den værktøjer, og den kan kalde et vejr-API, udføre beregninger eller forespørge en database — og så væve de reelle resultater ind i sit svar.

<img src="../../../translated_images/da/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Uden værktøjer kan modellen kun gætte — med værktøjer kan den kalde API'er, udføre beregninger og returnere realtidsdata.*

En AI-agent med værktøjer følger et **Reasoning and Acting (ReAct)**-mønster. Modellen svarer ikke bare — den tænker over, hvad den har brug for, handler ved at kalde et værktøj, observerer resultatet og beslutter så, om den skal handle igen eller give det endelige svar:

1. **Ræsonnere** — Agenten analyserer brugerens spørgsmål og bestemmer, hvilken information den behøver
2. **Handle** — Agenten vælger det rette værktøj, genererer de korrekte parametre og kalder det
3. **Observere** — Agenten modtager værktøjets output og evaluerer resultatet
4. **Gentage eller svare** — Hvis der er brug for mere data, går agenten tilbage til start; ellers formulerer den et naturligt sprog-svar

<img src="../../../translated_images/da/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-cyklussen — agenten ræsonnerer over, hvad der skal gøres, handler ved at kalde et værktøj, observerer resultatet og gentager indtil den kan levere det endelige svar.*

Dette sker automatisk. Du definerer værktøjerne og deres beskrivelser. Modellen håndterer beslutningstagningen om, hvornår og hvordan de skal bruges.

## Hvordan værktøjskald fungerer

### Værktøjsdefinitioner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definerer funktioner med klare beskrivelser og parameter-specifikationer. Modellen ser disse beskrivelser i sit system-prompt og forstår, hvad hvert værktøj gør.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Din vejrsøgning logik
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistenten er automatisk koblet af Spring Boot med:
// - ChatModel bean
// - Alle @Tool metoder fra @Component klasser
// - ChatMemoryProvider til sessionstyring
```


Diagrammet nedenfor bryder hver annotation ned og viser, hvordan hver del hjælper AI med at forstå, hvornår værktøjet skal kaldes og hvilke argumenter der skal gives:

<img src="../../../translated_images/da/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomi af en værktøjsdefinition — @Tool fortæller AI, hvornår det skal bruges, @P beskriver hver parameter, og @AiService forbinder det hele sammen ved opstart.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) og spørg:
> - "Hvordan integrerer jeg et rigtigt vejr-API som OpenWeatherMap i stedet for mock-data?"
> - "Hvad gør en god værktøjsbeskrivelse, der hjælper AI med at bruge det korrekt?"
> - "Hvordan håndterer jeg API-fejl og begrænsninger i værktøjsimplementeringerne?"

### Beslutningstagning

Når en bruger spørger "Hvad er vejret i Seattle?", vælger modellen ikke tilfældigt et værktøj. Den sammenligner brugerens intention med hver værktøjsbeskrivelse, den har adgang til, scorer relevans og vælger det bedste match. Den genererer derefter et struktureret funktionskald med de rigtige parametre — i dette tilfælde ved at sætte `location` til `"Seattle"`.

Hvis intet værktøj matcher brugerens forespørgsel, falder modellen tilbage til at svare ud fra sin egen viden. Hvis flere værktøjer matcher, vælger den det mest specifikke.

<img src="../../../translated_images/da/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modellen evaluerer hvert tilgængeligt værktøj mod brugerens intention og vælger det bedste match — derfor er det vigtigt at skrive klare, specifikke værktøjsbeskrivelser.*

### Udførelse

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot forbinder automatisk den deklarative `@AiService` interface med alle registrerede værktøjer, og LangChain4j udfører værktøjskald automatisk. Bag kulisserne strømmer et komplet værktøjskald gennem seks faser — fra brugerens spørgsmål i naturligt sprog helt tilbage til et svar i naturligt sprog:

<img src="../../../translated_images/da/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Den end-to-end flow — brugeren stiller et spørgsmål, modellen vælger et værktøj, LangChain4j udfører det, og modellen væver resultatet ind i et naturligt svar.*

Hvis du har kørt [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i Modul 00, har du allerede set dette mønster i aktion — `Calculator`-værktøjerne blev kaldt på samme måde. Sekvensdiagrammet nedenfor viser præcis, hvad der skete under demonstrationen:

<img src="../../../translated_images/da/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Værktøjskald-løkken fra Quick Start-demonstrationen — `AiServices` sender besked og værktøjsskemaer til LLM, LLM svarer med et funktionskald som `add(42, 58)`, LangChain4j udfører lokal `Calculator`-metode og sender resultatet tilbage til det endelige svar.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) og spørg:
> - "Hvordan fungerer ReAct-mønsteret, og hvorfor er det effektivt for AI-agenter?"
> - "Hvordan beslutter agenten, hvilket værktøj der skal bruges og i hvilken rækkefølge?"
> - "Hvad sker der, hvis et værktøj fejler - hvordan håndterer jeg fejl robust?"

### Generering af svar

Modellen modtager vejrdata og formaterer det til et naturligt sprog-svar til brugeren.

### Arkitektur: Spring Boot auto-wiring

Dette modul bruger LangChain4j's Spring Boot-integration med deklarative `@AiService` interfaces. Ved opstart opdager Spring Boot hver `@Component`, der indeholder `@Tool` metoder, din `ChatModel` bean og `ChatMemoryProvider` — og kobler det hele sammen i et enkelt `Assistant` interface uden boilerplate.

<img src="../../../translated_images/da/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService interfacet binder ChatModel, værktøjskomponenter og hukommelsesudbyder sammen — Spring Boot håndterer al koblingen automatisk.*

Her er hele anmodningslivscyklussen som sekvensdiagram — fra HTTP-anmodningen gennem controller, service og auto-wired proxy, hele vejen til værktøjsgennemførelse og tilbage:

<img src="../../../translated_images/da/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Den komplette Spring Boot anmodningslivscyklus — HTTP-anmodning strømmer gennem controller og service til auto-wired Assistant proxy, som orkestrerer LLM og værktøjskald automatisk.*

Vigtige fordele ved denne tilgang:

- **Spring Boot auto-wiring** — ChatModel og værktøjer injiceres automatisk
- **@MemoryId mønster** — Automatisk sessionsbaseret hukommelsesstyring
- **Enkelt instans** — Assistant oprettes én gang og genbruges for bedre ydeevne
- **Typesikker udførelse** — Java-metoder kaldes direkte med typekonvertering
- **Multi-turn orkestrering** — Håndterer automatisk kædning af værktøjer
- **Ingen boilerplate** — Ingen manuelle `AiServices.builder()` kald eller hukommelses-HashMap

Alternative tilgange (manuel `AiServices.builder()`) kræver mere kode og mangler Spring Boot-integrationsfordele.

## Kædning af værktøjer

**Kædning af værktøjer** — Den reelle styrke ved værktøjsbaserede agenter viser sig, når et enkelt spørgsmål kræver flere værktøjer. Spørg "Hvad er vejret i Seattle i Fahrenheit?" og agenten kæder automatisk to værktøjer sammen: først kalder den `getCurrentWeather` for at få temperaturen i Celsius, og derefter sender den den værdi til `celsiusToFahrenheit` for konvertering — alt i én samtalerunde.

<img src="../../../translated_images/da/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Kædning af værktøjer i aktion — agenten kalder først getCurrentWeather, sender så Celsius-resultatet videre til celsiusToFahrenheit og leverer et samlet svar.*

**Elegant fejlhåndtering** — Spørg efter vejret i en by, som ikke findes i mock-dataene. Værktøjet returnerer en fejlmeddelelse, og AI forklarer, at den ikke kan hjælpe i stedet for at crashe. Værktøjer fejler sikkert. Diagrammet nedenfor sammenligner de to tilgange — med korrekt fejlhåndtering opfanger agenten undtagelsen og svarer nyttigt, uden den crasher hele applikationen:

<img src="../../../translated_images/da/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Når et værktøj fejler, opfanger agenten fejlen og svarer med en hjælpsom forklaring i stedet for at crashe.*

Dette sker i en enkelt samtalerunde. Agenten orkestrerer flere værktøjskald autonomt.

## Kør applikationen

**Bekræft implementering:**

Sørg for, at `.env` filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01). Kør dette fra modulmappen (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```


**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```


**Start applikationen:**

> **Bemærk:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra rodmappen (som beskrevet i Modul 01), kører dette modul allerede på port 8084. Du kan springe start-kommandoerne over og gå direkte til http://localhost:8084.

**Mulighed 1: Brug Spring Boot Dashboard (Anbefalet for VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at administrere alle Spring Boot-applikationer. Du finder den i Activity Bar til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i arbejdsområdet
- Starte/stoppe applikationer med ét klik
- Se applikationslogfiler i realtid
- Overvåge applikationsstatus

Klik simpelthen på play-knappen ved siden af "tools" for at starte dette modul, eller start alle moduler på én gang.

Sådan ser Spring Boot Dashboard ud i VS Code:

<img src="../../../translated_images/da/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — start, stop og overvåg alle moduler fra ét sted*

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappe
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodmappe
.\start-all.ps1
```

Eller start kun denne modul:

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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env` fil og vil bygge JAR-filerne, hvis de ikke findes.

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
.\stop.ps1  # Kun dette modul
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Brug af applikationen

Applikationen giver en webgrænseflade, hvor du kan interagere med en AI-agent, der har adgang til værktøjer til vejrudsigter og temperaturkonvertering. Sådan ser grænsefladen ud — den inkluderer hurtigstart-eksempler og en chatpanel til at sende forespørgsler:

<a href="images/tools-homepage.png"><img src="../../../translated_images/da/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Grænsefladen for AI Agent Tools - hurtigstart-eksempler og chatinterface til at interagere med værktøjer*

### Prøv enkel brug af værktøjer

Start med en simpel anmodning: "Konverter 100 grader Fahrenheit til Celsius". Agenten genkender, at den skal bruge temperaturkonverteringsværktøjet, kalder det med de rigtige parametre og returnerer resultatet. Bemærk hvor naturligt det føles - du specificerede ikke hvilket værktøj der skulle bruges eller hvordan det skulle kaldes.

### Test værktøjskæder

Prøv nu noget mere komplekst: "Hvordan er vejret i Seattle, og konverter det til Fahrenheit?" Se agenten arbejde sig gennem dette i trin. Den henter først vejret (som returnerer Celsius), genkender at den skal konvertere til Fahrenheit, kalder konverteringsværktøjet, og kombinerer begge resultater til ét svar.

### Se samtaleflowet

Chatinterfacet gemmer samtalehistorik, så du kan have interaktioner over flere ture. Du kan se alle tidligere spørgsmål og svar, hvilket gør det nemt at følge samtalen og forstå, hvordan agenten bygger kontekst over flere udvekslinger.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/da/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flersporet samtale, der viser simple konverteringer, vejropslag og kædning af værktøjer*

### Eksperimenter med forskellige forespørgsler

Prøv forskellige kombinationer:
- Vejropslag: "Hvordan er vejret i Tokyo?"
- Temperaturkonverteringer: "Hvad er 25°C i Kelvin?"
- Kombinerede forespørgsler: "Tjek vejret i Paris og sig, om det er over 20°C"

Bemærk hvordan agenten fortolker naturligt sprog og omsætter det til passende kald til værktøjerne.

## Nøglebegreber

### ReAct-mønsteret (Reasoning and Acting)

Agenten veksler mellem at ræsonnere (bestemme hvad der skal gøres) og at handle (bruge værktøjer). Dette mønster muliggør autonom problemløsning fremfor blot at reagere på instruktioner.

### Værktøjsbeskrivelser er vigtige

Kvaliteten af dine værktøjsbeskrivelser påvirker direkte, hvor godt agenten bruger dem. Klare, specifikke beskrivelser hjælper modellen med at forstå, hvornår og hvordan hvert værktøj skal kaldes.

### Session Management

@MemoryId-annotationen muliggør automatisk hukommelsesstyring baseret på session. Hver session ID får sin egen ChatMemory-instans styret af ChatMemoryProvider bean'en, så flere brugere kan interagere med agenten samtidigt uden at deres samtaler blandes sammen. Diagrammet nedenfor viser hvordan flere brugere ledes til isolerede hukommelseslagre baseret på deres session IDs:

<img src="../../../translated_images/da/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Hver session ID kortlægges til en isoleret samtalehistorik — brugere ser aldrig hinandens beskeder.*

### Fejlhåndtering

Værktøjer kan fejle — API'er kan timeoute, parametre kan være ugyldige, eksterne tjenester kan gå ned. Produktions-agenter har behov for fejlhåndtering, så modellen kan forklare problemer eller prøve alternativer i stedet for at få hele applikationen til at crashe. Når et værktøj kaster en undtagelse, fanger LangChain4j den og sender fejlmeddelelsen tilbage til modellen, som så kan forklare problemet på naturligt sprog.

## Tilgængelige værktøjer

Diagrammet nedenfor viser det brede økosystem af værktøjer, du kan bygge. Denne modul demonstrerer vejrudsigts- og temperaturværktøjer, men det samme @Tool-mønster fungerer for enhver Java-metode — fra databaseforespørgsler til betalingsbehandling.

<img src="../../../translated_images/da/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Enhver Java-metode annoteret med @Tool bliver tilgængelig for AI — mønsteret udvides til databaser, API'er, e-mail, filoperationer med mere.*

## Hvornår man skal bruge værktøjsbaserede agenter

Ikke alle forespørgsler behøver værktøjer. Beslutningen afhænger af, om AI skal interagere med eksterne systemer eller kan svare ud fra sin egen viden. Guiden nedenfor opsummerer hvornår værktøjer tilfører værdi og hvornår de er unødvendige:

<img src="../../../translated_images/da/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*En hurtig beslutningsguide — værktøjer bruges til realtidsdata, beregninger og handlinger; generel viden og kreative opgaver behøver dem ikke.*

## Værktøjer vs RAG

Modulerne 03 og 04 udvider begge hvad AI kan gøre, men på fundamentalt forskellige måder. RAG giver modellen adgang til **viden** ved at hente dokumenter. Værktøjer giver modellen evnen til at tage **handlinger** ved at kalde funktioner. Diagrammet nedenfor sammenligner disse to tilgange side om side — fra hvordan hvert workflow fungerer til kompromisserne mellem dem:

<img src="../../../translated_images/da/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG henter information fra statiske dokumenter — Værktøjer udfører handlinger og henter dynamiske, realtidsdata. Mange produktionssystemer kombinerer begge.*

I praksis kombinerer mange produktionssystemer begge tilgange: RAG for at forankre svar i din dokumentation, og Værktøjer for at hente live data eller udføre operationer.

## Næste skridt

**Næste modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Forrige: Modul 03 - RAG](../03-rag/README.md) | [Tilbage til hoved](../README.md) | [Næste: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritiske oplysninger anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->