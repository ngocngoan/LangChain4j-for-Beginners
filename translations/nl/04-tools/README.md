# Module 04: AI-agenten met tools

## Inhoudsopgave

- [Wat je zult leren](../../../04-tools)
- [Vereisten](../../../04-tools)
- [Inzicht in AI-agenten met tools](../../../04-tools)
- [Hoe tool-aanroep werkt](../../../04-tools)
  - [Tooldefinities](../../../04-tools)
  - [Besluitvorming](../../../04-tools)
  - [Uitvoering](../../../04-tools)
  - [Responsgeneratie](../../../04-tools)
  - [Architectuur: Spring Boot Auto-Wiring](../../../04-tools)
- [Toolketting](../../../04-tools)
- [Start de applicatie](../../../04-tools)
- [Gebruik van de applicatie](../../../04-tools)
  - [Probeer eenvoudig toolgebruik](../../../04-tools)
  - [Test toolketting](../../../04-tools)
  - [Bekijk het gesprek verloop](../../../04-tools)
  - [Experimenteer met verschillende verzoeken](../../../04-tools)
- [Belangrijke concepten](../../../04-tools)
  - [ReAct-patroon (Redeneren en Handelen)](../../../04-tools)
  - [Toolbeschrijvingen zijn belangrijk](../../../04-tools)
  - [Sessiebeheer](../../../04-tools)
  - [Foutafhandeling](../../../04-tools)
- [Beschikbare tools](../../../04-tools)
- [Wanneer tool-gebaseerde agenten te gebruiken](../../../04-tools)
- [Tools versus RAG](../../../04-tools)
- [Volgende stappen](../../../04-tools)

## Wat je zult leren

Tot nu toe heb je geleerd hoe je gesprekken voert met AI, prompts effectief structureert en reacties baseert op je documenten. Maar er is nog een fundamentele beperking: taalmodellen kunnen alleen tekst genereren. Ze kunnen het weer niet checken, geen berekeningen uitvoeren, geen databases doorzoeken of communiceren met externe systemen.

Tools veranderen dit. Door het model toegang te geven tot functies die het kan aanroepen, verander je het van een tekstgenerator in een agent die acties kan uitvoeren. Het model beslist wanneer het een tool nodig heeft, welke tool te gebruiken en welke parameters door te geven. Jouw code voert de functie uit en geeft het resultaat terug. Het model verwerkt dat resultaat in zijn antwoord.

## Vereisten

- Afgeronde [Module 01 - Introductie](../01-introduction/README.md) (Azure OpenAI-resources gedeployed)
- Aanbevolen om eerdere modules afgerond te hebben (deze module verwijst naar [RAG-concepten uit Module 03](../03-rag/README.md) in de vergelijking Tools vs RAG)
- `.env` bestand in de rootmap met Azure-gegevens (gemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst de deploy-instructies daar.

## Inzicht in AI-agenten met tools

> **📝 Opmerking:** De term "agenten" in deze module verwijst naar AI-assistenten die uitgebreid zijn met mogelijkheden voor tool-aanroep. Dit verschilt van de **Agentic AI** patronen (autonome agenten met planning, geheugen en meerstaps redeneren) die we behandelen in [Module 05: MCP](../05-mcp/README.md).

Zonder tools kan een taalmodel alleen tekst genereren op basis van zijn trainingsgegevens. Vraag het naar het huidige weer en het moet raden. Geef het tools en het kan een weer-API aanroepen, berekeningen uitvoeren of een database raadplegen — en die echte resultaten verwerken in zijn antwoord.

<img src="../../../translated_images/nl/what-are-tools.724e468fc4de64da.webp" alt="Zonder Tools vs Met Tools" width="800"/>

*Zonder tools kan het model alleen raden — met tools kan het API's aanroepen, berekeningen draaien en real-time data teruggeven.*

Een AI-agent met tools volgt een **Redeneren en Handelen (ReAct)** patroon. Het model reageert niet alleen — het denkt na over wat het nodig heeft, handelt door een tool aan te roepen, observeert het resultaat en beslist vervolgens of het opnieuw moet handelen of het eindantwoord moet geven:

1. **Redeneer** — De agent analyseert de vraag van de gebruiker en bepaalt welke informatie nodig is
2. **Handel** — De agent selecteert de juiste tool, genereert de juiste parameters, en roept deze aan
3. **Observeer** — De agent ontvangt de output van de tool en evalueert het resultaat
4. **Herhaal of reageer** — Als meer data nodig is, gaat de agent terug naar stap 1; anders formuleert hij een antwoord in natuurlijke taal

<img src="../../../translated_images/nl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct-patroon" width="800"/>

*De ReAct-cyclus — de agent redeneert over wat te doen, handelt door een tool aan te roepen, observeert het resultaat, en herhaalt dit totdat het een eindantwoord kan geven.*

Dit gebeurt automatisch. Jij definieert de tools en hun beschrijvingen. Het model regelt de besluitvorming over wanneer en hoe ze te gebruiken.

## Hoe tool-aanroep werkt

### Tooldefinities

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Je definieert functies met duidelijke beschrijvingen en parameterspecificaties. Het model ziet deze beschrijvingen in zijn systeemprompt en begrijpt wat elke tool doet.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Uw weeropzoeklogica
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistant wordt automatisch verbonden door Spring Boot met:
// - ChatModel bean
// - Alle @Tool-methoden van @Component-klassen
// - ChatMemoryProvider voor sessiebeheer
```

De onderstaande afbeelding legt elke annotatie uit en toont hoe elk onderdeel de AI helpt te begrijpen wanneer de tool aan te roepen is en welke argumenten door te geven:

<img src="../../../translated_images/nl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie van Tooldefinities" width="800"/>

*Anatomie van een tooldefinitie — @Tool vertelt de AI wanneer het te gebruiken, @P beschrijft elke parameter, en @AiService zorgt bij startup voor de verbinding.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) en vraag:
> - "Hoe zou ik een echte weer-API zoals OpenWeatherMap integreren in plaats van mockdata?"
> - "Wat maakt een goede toolbeschrijving die de AI helpt deze correct te gebruiken?"
> - "Hoe ga ik om met API-fouten en limieten in toolimplementaties?"

### Besluitvorming

Wanneer een gebruiker vraagt: "Wat is het weer in Seattle?", kiest het model niet willekeurig een tool. Het vergelijkt de intentie van de gebruiker met elke beschikbare toolbeschrijving, scoort de relevantie en selecteert de beste match. Vervolgens genereert het een gestructureerde functieaanroep met de juiste parameters — in dit geval `location` op `"Seattle"` gezet.

Als geen tool bij het verzoek past, beantwoordt het model uit eigen kennis. Bij meerdere matches kiest het de meest specifieke tool.

<img src="../../../translated_images/nl/decision-making.409cd562e5cecc49.webp" alt="Hoe de AI bepaalt welke tool te gebruiken" width="800"/>

*Het model beoordeelt elke beschikbare tool op relevantie voor de gebruikersintentie en selecteert de beste match — daarom is duidelijke, specifieke toolbeschrijving belangrijk.*

### Uitvoering

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot verbindt automatisch de declaratieve `@AiService` interface met alle geregistreerde tools, en LangChain4j voert tool-aanroepen automatisch uit. Achter de schermen doorloopt een volledige tool-aanroep zes fasen — van de natuurlijke taalvraag van de gebruiker tot het natuurlijke taalantwoord:

<img src="../../../translated_images/nl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool-aanroepstroom" width="800"/>

*De end-to-end flow — de gebruiker stelt een vraag, het model selecteert een tool, LangChain4j voert deze uit, en het model verwerkt het resultaat tot een natuurlijk antwoord.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) en vraag:
> - "Hoe werkt het ReAct-patroon en waarom is het effectief voor AI-agenten?"
> - "Hoe beslist de agent welke tool te gebruiken en in welke volgorde?"
> - "Wat gebeurt er als de uitvoering van een tool faalt - hoe kan ik robuuste foutafhandeling implementeren?"

### Responsgeneratie

Het model ontvangt de weerdata en formatteert dit als een natuurlijk taalantwoord voor de gebruiker.

### Architectuur: Spring Boot Auto-Wiring

Deze module gebruikt LangChain4j's Spring Boot-integratie met declaratieve `@AiService` interfaces. Bij het opstarten ontdekt Spring Boot elke `@Component` die `@Tool` methoden bevat, jouw `ChatModel` bean en de `ChatMemoryProvider` — en verbindt ze tot een enkele `Assistant` interface zonder bijkomende code.

<img src="../../../translated_images/nl/spring-boot-wiring.151321795988b04e.webp" alt="Architectuur van Spring Boot Auto-Wiring" width="800"/>

*De @AiService interface verbindt ChatModel, toolcomponenten en het geheugenprovider — Spring Boot handelt alle koppelingen automatisch af.*

Belangrijke voordelen van deze aanpak:

- **Spring Boot auto-wiring** — ChatModel en tools automatisch geïnjecteerd
- **@MemoryId patroon** — Automatisch sessie-gebaseerd geheugenbeheer
- **Één enkele instantie** — Assistant wordt eenmalig gemaakt en hergebruikt voor betere prestaties
- **Type-safe uitvoering** — Java-methoden direct aangeroepen met conversie van types
- **Multi-turn orkestratie** — Beheert toolketting automatisch
- **Geen boilerplate** — Geen handmatige `AiServices.builder()` calls of geheugen-HashMaps

Alternatieve benaderingen (handmatig `AiServices.builder()`) vereisen meer code en missen de voordelen van Spring Boot-integratie.

## Toolketting

**Toolketting** — De echte kracht van tool-gebaseerde agenten komt naar voren wanneer een enkele vraag meerdere tools vereist. Vraag "Wat is het weer in Seattle in Fahrenheit?" en de agent ketent automatisch twee tools aan elkaar: eerst roept hij `getCurrentWeather` aan om de temperatuur in Celsius te krijgen, daarna voert hij die waarde door naar `celsiusToFahrenheit` voor conversie — allemaal binnen één gesprek.

<img src="../../../translated_images/nl/tool-chaining-example.538203e73d09dd82.webp" alt="Toolketting voorbeeld" width="800"/>

*Toolketting in actie — de agent roept eerst getCurrentWeather aan, vervolgens wordt het Celsius-resultaat doorgestuurd naar celsiusToFahrenheit, en levert een gecombineerd antwoord.*

**Gracieus falen** — Vraag voor weersinformatie in een stad die niet in de mockdata staat. De tool geeft een foutmelding terug, en de AI legt uit dat het niet kan helpen in plaats van te crashen. Tools falen veilig. De onderstaande afbeelding vergelijkt de twee benaderingen — met correcte foutafhandeling vangt de agent de uitzondering op en reageert behulpzaam, zonder reden voor crash:

<img src="../../../translated_images/nl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Foutafhandelingsstroom" width="800"/>

*Als een tool faalt, vangt de agent de fout op en reageert met een behulpzame uitleg in plaats van te crashen.*

Dit gebeurt in één gespreksturn. De agent orkestreert zelf meerdere tool-aanroepen.

## Start de applicatie

**Verifieer de deployment:**

Zorg ervoor dat het `.env` bestand aanwezig is in de rootmap met Azure-gegevens (gemaakt tijdens Module 01). Voer dit uit vanuit de modulemap (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt via `./start-all.sh` vanuit de rootmap (zoals beschreven in Module 01), draait deze module al op poort 8084. Je kunt de startcommando's hieronder overslaan en direct http://localhost:8084 openen.

**Optie 1: Gebruik Spring Boot Dashboard (aanbevolen voor VS Code gebruikers)**

De devcontainer bevat de Spring Boot Dashboard extensie die een visuele interface biedt om alle Spring Boot apps te beheren. Je vindt deze in de Actie-balk (links in VS Code) (zoek het Spring Boot icoon).

Via het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot apps in de workspace zien
- Applicaties met één klik starten/stoppen
- Applicatielogs realtime bekijken
- Applicatiestatus monitoren

Klik op de afspeelknop naast "tools" om deze module te starten, of start alle modules tegelijk.

Zo ziet het Spring Boot Dashboard eruit in VS Code:

<img src="../../../translated_images/nl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Het Spring Boot Dashboard in VS Code — start, stop en monitor alle modules vanaf één plek*

**Optie 2: Gebruik shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofddirectory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de rootmap
.\start-all.ps1
```

Of start alleen deze module:

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

Beide scripts laden automatisch environment variables uit het root `.env` bestand en bouwen de JARs indien deze nog niet bestaan.

> **Opmerking:** Wil je alle modules handmatig bouwen vóór je start:
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

Open http://localhost:8084 in je browser.

**Stoppen:**

**Bash:**
```bash
./stop.sh  # Alleen deze module
# Of
cd .. && ./stop-all.sh  # Alle modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Alleen deze module
# Of
cd ..; .\stop-all.ps1  # Alle modules
```

## Gebruik van de applicatie

De applicatie biedt een webinterface waarin je kunt communiceren met een AI-agent die toegang heeft tot weer- en temperatuurconversietools. Zo ziet de interface eruit — met snelstartvoorbeelden en een chat-paneel om verzoeken te verzenden:
<a href="images/tools-homepage.png"><img src="../../../translated_images/nl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools-interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*De AI Agent Tools-interface - snelle voorbeelden en chatinterface voor interactie met tools*

### Probeer Eenvoudig Toolgebruik

Begin met een eenvoudige opdracht: "Converteer 100 graden Fahrenheit naar Celsius". De agent herkent dat hij de temperatuurconversietool nodig heeft, roept deze aan met de juiste parameters en geeft het resultaat terug. Merk op hoe natuurlijk dit aanvoelt - je hebt niet opgegeven welke tool je moest gebruiken of hoe je deze moest aanroepen.

### Test Toolketens

Probeer nu iets complexers: "Wat is het weer in Seattle en zet het om naar Fahrenheit?" Bekijk hoe de agent dit stap voor stap afhandelt. Eerst haalt hij het weer op (in Celsius), herkent dat hij moet converteren naar Fahrenheit, roept de conversietool aan en combineert beide resultaten tot één antwoord.

### Bekijk het Gespreksverloop

De chatinterface bewaart de gesprekshistorie, zodat je meerdere beurten kunt voeren. Je kunt alle vorige vragen en antwoorden zien, wat het makkelijk maakt het gesprek te volgen en te begrijpen hoe de agent context opbouwt over meerdere uitwisselingen.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/nl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Gesprek met Meerdere Tool-aanroepen" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Meerdere beurten gesprek met eenvoudige conversies, weeropvragingen en toolketens*

### Experimenteer met Verschillende Verzoeken

Probeer diverse combinaties:
- Weeropvragingen: "Hoe is het weer in Tokio?"
- Temperatuurconversies: "Wat is 25°C in Kelvin?"
- Gecombineerde vragen: "Controleer het weer in Parijs en vertel me of het boven de 20°C is"

Merk op hoe de agent natuurlijke taal interpreteert en koppelt aan passende tool-aanroepen.

## Belangrijke Concepten

### ReAct-patroon (Redeneren en Handelen)

De agent wisselt af tussen redeneren (beslissen wat te doen) en handelen (tools gebruiken). Dit patroon maakt autonome probleemoplossing mogelijk in plaats van alleen reactief instructies opvolgen.

### Toolbeschrijvingen Zijn Belangrijk

De kwaliteit van je toolbeschrijvingen beïnvloedt direct hoe goed de agent ze gebruikt. Duidelijke, specifieke beschrijvingen helpen het model te begrijpen wanneer en hoe elke tool aan te roepen.

### Sessiebeheer

De `@MemoryId` annotatie maakt automatische sessiegebaseerde geheugentoewijzing mogelijk. Elke sessie-ID krijgt zijn eigen `ChatMemory` instantie beheerd door de `ChatMemoryProvider` bean, zodat meerdere gebruikers gelijktijdig met de agent kunnen communiceren zonder dat gesprekken door elkaar lopen. Het onderstaande diagram toont hoe gebruikers worden doorgestuurd naar geïsoleerde geheugens op basis van hun sessie-IDs:

<img src="../../../translated_images/nl/session-management.91ad819c6c89c400.webp" alt="Sessiebeheer met @MemoryId" width="800"/>

*Elke sessie-ID correspondeert met een geïsoleerde gesprekshistorie — gebruikers zien nooit elkaars berichten.*

### Foutafhandeling

Tools kunnen falen — API's lopen vast, parameters kunnen ongeldig zijn, externe diensten kunnen uitvallen. Productieagenten hebben foutafhandeling nodig zodat het model problemen kan uitleggen of alternatieven kan proberen in plaats van de hele applicatie te laten crashen. Wanneer een tool een uitzondering gooit, vangt LangChain4j deze op en stuurt het foutbericht terug naar het model, dat het probleem dan in natuurlijke taal kan uitleggen.

## Beschikbare Tools

Het onderstaande diagram toont het brede ecosysteem van tools die je kunt bouwen. Deze module demonstreert weer- en temperatuurtools, maar hetzelfde `@Tool`-patroon werkt voor elke Java-methode — van databasevragen tot betalingsverwerking.

<img src="../../../translated_images/nl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Toolecosysteem" width="800"/>

*Elke Java-methode geannoteerd met @Tool wordt beschikbaar voor de AI — het patroon strekt zich uit tot databases, API’s, e-mail, bestandsbewerkingen en meer.*

## Wanneer Toolgebaseerde Agenten Te Gebruiken

Niet elk verzoek vereist tools. De beslissing hangt af van of de AI met externe systemen moet interageren of vanuit eigen kennis kan antwoorden. De volgende gids vat samen wanneer tools waarde toevoegen en wanneer ze overbodig zijn:

<img src="../../../translated_images/nl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Wanneer Tools te Gebruiken" width="800"/>

*Een snelle beslissingsgids — tools zijn voor realtime data, berekeningen en acties; algemene kennis en creatieve taken hebben ze niet nodig.*

## Tools vs RAG

Module 03 en 04 breiden beiden uit wat de AI kan, maar op fundamenteel verschillende manieren. RAG geeft het model toegang tot **kennis** door documenten op te halen. Tools geven het model de mogelijkheid om **acties** uit te voeren via functie-aanroepen. Het onderstaande diagram vergelijkt deze benaderingen naast elkaar — van de werking tot de afwegingen:

<img src="../../../translated_images/nl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Vergelijking" width="800"/>

*RAG haalt informatie op uit statische documenten — Tools voeren acties uit en halen dynamische, realtime data op. Veel productiesystemen combineren beide.*

In de praktijk combineren veel productiesystemen beide benaderingen: RAG om antwoorden in documentatie te verankeren, en Tools om live data op te halen of operaties uit te voeren.

## Volgende Stappen

**Volgende Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigatie:** [← Vorige: Module 03 - RAG](../03-rag/README.md) | [Terug naar Hoofd](../README.md) | [Volgende: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onjuistheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->