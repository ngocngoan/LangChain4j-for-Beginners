# Module 04: AI-agenten met Hulpmiddelen

## Inhoudsopgave

- [Wat je zult leren](../../../04-tools)
- [Vereisten](../../../04-tools)
- [Begrijpen van AI-agenten met hulpmiddelen](../../../04-tools)
- [Hoe het aanroepen van hulpmiddelen werkt](../../../04-tools)
  - [Definities van hulpmiddelen](../../../04-tools)
  - [Besluitvorming](../../../04-tools)
  - [Uitvoering](../../../04-tools)
  - [Genereren van reactie](../../../04-tools)
  - [Architectuur: Spring Boot Auto-Wiring](../../../04-tools)
- [Koppelen van hulpmiddelen](../../../04-tools)
- [De applicatie uitvoeren](../../../04-tools)
- [De applicatie gebruiken](../../../04-tools)
  - [Probeer eenvoudig gebruik van hulpmiddelen](../../../04-tools)
  - [Test koppelingshulpmiddelen](../../../04-tools)
  - [Bekijk het gesprek verloop](../../../04-tools)
  - [Experimenteer met verschillende verzoeken](../../../04-tools)
- [Belangrijke concepten](../../../04-tools)
  - [ReAct-patroon (Redeneren en Handelen)](../../../04-tools)
  - [Omschrijving van hulpmiddelen is belangrijk](../../../04-tools)
  - [Sessiebeheer](../../../04-tools)
  - [Foutafhandeling](../../../04-tools)
- [Beschikbare hulpmiddelen](../../../04-tools)
- [Wanneer agents met hulpmiddelen gebruiken](../../../04-tools)
- [Hulpmiddelen vs RAG](../../../04-tools)
- [Volgende stappen](../../../04-tools)

## Wat je zult leren

Tot nu toe heb je geleerd hoe je gesprekken met AI voert, prompts effectief structureert, en reacties baseert op je documenten. Maar er is nog een fundamentele beperking: taalmodellen kunnen alleen tekst genereren. Ze kunnen het weer niet controleren, berekeningen maken, databases raadplegen of met externe systemen communiceren.

Hulpmiddelen veranderen dit. Door het model toegang te geven tot functies die het kan aanroepen, transformeer je het van een tekstgenerator naar een agent die acties kan ondernemen. Het model beslist wanneer het een hulpmiddel nodig heeft, welk hulpmiddel gebruikt moet worden, en welke parameters doorgegeven moeten worden. Je code voert de functie uit en retourneert het resultaat. Het model verwerkt dat resultaat in zijn reactie.

## Vereisten

- Voltooid [Module 01 - Introductie](../01-introduction/README.md) (Azure OpenAI-resources gedeployed)
- Voltooiing van voorgaande modules aanbevolen (deze module verwijst naar [RAG concepten uit Module 03](../03-rag/README.md) in de vergelijking Hulpmiddelen vs RAG)
- `.env`-bestand in de hoofdmap met Azure-gegevens (aangemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst de deploy-instructies daar.

## Begrijpen van AI-agenten met hulpmiddelen

> **📝 Opmerking:** De term "agenten" in deze module verwijst naar AI-assistenten die zijn uitgebreid met hulpmiddelenaanroepmogelijkheden. Dit is anders dan de **Agentic AI**-patronen (autonome agenten met planning, geheugen en redeneren in meerdere stappen) die we behandelen in [Module 05: MCP](../05-mcp/README.md).

Zonder hulpmiddelen kan een taalmodel alleen tekst genereren vanuit zijn trainingsdata. Vraag het om het huidige weer, en het moet gokken. Geef het hulpmiddelen, en het kan een weer-API aanroepen, berekeningen uitvoeren, of een database raadplegen — en die echte resultaten verwerken in zijn reactie.

<img src="../../../translated_images/nl/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Zonder hulpmiddelen kan het model alleen raden — met hulpmiddelen kan het API’s aanroepen, berekeningen maken, en realtime data teruggeven.*

Een AI-agent met hulpmiddelen volgt een **Reasoning and Acting (ReAct)** patroon. Het model reageert niet alleen — het denkt na over wat het nodig heeft, handelt door een hulpmiddel aan te roepen, observeert het resultaat, en beslist daarna of het opnieuw moet handelen of het eindantwoord moet geven:

1. **Redeneer** — De agent analyseert de vraag van de gebruiker en bepaalt welke informatie nodig is
2. **Handel** — De agent kiest het juiste hulpmiddel, genereert correcte parameters en roept het aan
3. **Observeer** — De agent ontvangt de output van het hulpmiddel en beoordeelt het resultaat
4. **Herhaal of reageer** — Is meer data nodig, dan gaat de agent terug naar stap 1; anders formuleert hij een natuurlijk antwoord

<img src="../../../translated_images/nl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*De ReAct-cyclus — de agent redeneert over wat te doen, handelt door een hulpmiddel aan te roepen, observeert het resultaat en herhaalt dit totdat het eindantwoord klaar is.*

Dit gebeurt automatisch. Je definieert de hulpmiddelen en hun omschrijvingen. Het model regelt zelf de besluitvorming over wanneer en hoe ze te gebruiken.

## Hoe het aanroepen van hulpmiddelen werkt

### Definities van hulpmiddelen

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Je definieert functies met duidelijke omschrijvingen en parameterspecificaties. Het model ziet deze omschrijvingen in zijn systeem-prompt en begrijpt wat elk hulpmiddel doet.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Je weerzoeklogica
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent wordt automatisch aangesloten door Spring Boot met:
// - ChatModel bean
// - Alle @Tool-methoden van @Component-klassen
// - ChatMemoryProvider voor sessiebeheer
```
  
Het diagram hieronder breekt elke annotatie uit en laat zien hoe elk onderdeel de AI helpt te begrijpen wanneer het hulpmiddel aangeroepen moet worden en welke argumenten doorgegeven:

<img src="../../../translated_images/nl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomie van een hulpmiddel-definitie — @Tool vertelt de AI wanneer het te gebruiken, @P beschrijft elke parameter, en @AiService koppelt alles bij opstart.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) en vraag:
> - "Hoe integreer ik een echte weer-API zoals OpenWeatherMap in plaats van mock data?"
> - "Wat maakt een goede hulpmiddel-omschrijving die de AI helpt om het correct te gebruiken?"
> - "Hoe behandel ik API-fouten en limieten in implementaties van hulpmiddelen?"

### Besluitvorming

Als een gebruiker vraagt: "Wat is het weer in Seattle?", kiest het model niet willekeurig een hulpmiddel. Het vergelijkt de intentie van de gebruiker met elke hulpmiddelomschrijving waar het toegang toe heeft, geeft een score op relevantie, en selecteert de beste match. Het genereert vervolgens een gestructureerde function call met de juiste parameters — in dit geval `location` met waarde `"Seattle"`.

Als geen hulpmiddel bij het verzoek past, valt het model terug op antwoorden uit eigen kennis. Als meerdere hulpmiddelen passen, kiest het de meest specifieke.

<img src="../../../translated_images/nl/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Het model evalueert elk beschikbaar hulpmiddel ten opzichte van de intentie van de gebruiker en selecteert de beste match — daarom is het schrijven van duidelijke, specifieke omschrijvingen belangrijk.*

### Uitvoering

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot verbindt automatisch de declaratieve `@AiService` interface met alle geregistreerde hulpmiddelen, en LangChain4j voert hulpmiddel-aanroepen automatisch uit. Achter de schermen doorloopt een volledige hulpmiddel-aanroep zes fasen — van de natuurlijke taal vraag van de gebruiker tot het teruggeven van een antwoord in natuurlijke taal:

<img src="../../../translated_images/nl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*De end-to-end flow — de gebruiker stelt een vraag, het model selecteert een hulpmiddel, LangChain4j voert het uit, en het model verwerkt het resultaat tot een natuurlijke respons.*

Als je de [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) in Module 00 hebt uitgevoerd, heb je dit patroon al gezien — de `Calculator` hulpmiddelen werden op dezelfde wijze aangeroepen. Het onderstaande sequentiediagram toont precies wat er achter de schermen gebeurde tijdens die demo:

<img src="../../../translated_images/nl/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*De hulpmiddel-aanroeplus van de Quick Start demo — `AiServices` stuurt je bericht en hulpmiddel-schema’s naar de LLM, de LLM antwoordt met een functie-aanroep zoals `add(42, 58)`, LangChain4j voert de `Calculator`-methode lokaal uit en geeft het resultaat weer voor het eindantwoord.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) en vraag:
> - "Hoe werkt het ReAct-patroon en waarom is het effectief voor AI-agenten?"
> - "Hoe bepaalt de agent welk hulpmiddel gebruikt wordt en in welke volgorde?"
> - "Wat gebeurt er als de uitvoering van een hulpmiddel mislukt — hoe handel ik fouten robuust af?"

### Genereren van reactie

Het model ontvangt de weerdata en formatteert dit tot een natuurlijke taalreactie voor de gebruiker.

### Architectuur: Spring Boot Auto-Wiring

Deze module gebruikt de Spring Boot-integratie van LangChain4j met declaratieve `@AiService` interfaces. Bij het opstarten ontdekt Spring Boot elke `@Component` met `@Tool` methoden, je `ChatModel` bean, en de `ChatMemoryProvider` — en verbindt ze automatisch aan een enkele `Assistant` interface zonder extra code.

<img src="../../../translated_images/nl/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*De @AiService interface koppelt samen de ChatModel-, hulpmiddelcomponenten, en geheugenprovider — Spring Boot verzorgt automatisch alle verbindingen.*

Hier is de volledige levenscyclus van een verzoek als sequentiediagram — van HTTP-verzoek via controller, service, en automatisch verbonden proxy, helemaal tot de hulpmiddeluitvoering en terug:

<img src="../../../translated_images/nl/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*De complete Spring Boot levenscyclus van een verzoek — HTTP-verzoek stroomt via controller en service naar de automatisch verbonden Assistant proxy, die LLM en hulpmiddel-aanroepen automatisch coördineert.*

Belangrijkste voordelen van deze aanpak:

- **Spring Boot auto-wiring** — ChatModel en hulpmiddelen worden automatisch geïnjecteerd
- **@MemoryId patroon** — Automatisch sessie-gebaseerd geheugenbeheer
- **Één enkele instantie** — Assistant wordt één keer gecreëerd en hergebruikt voor betere prestaties
- **Typeveilige uitvoering** — Java-methoden worden rechtstreeks aangeroepen met typeconversie
- **Multi-turn orchestratie** — Verwerkt automatisch koppelingen van hulpmiddelen
- **Nul boilerplate** — Geen handmatige `AiServices.builder()` oproepen of geheugen HashMap

Alternatieve benaderingen (handmatige `AiServices.builder()`) vereisen meer code en missen Spring Boot integratie voordelen.

## Koppelen van hulpmiddelen

**Koppelen van hulpmiddelen** — De echte kracht van agents met hulpmiddelen komt naar voren wanneer een enkele vraag meerdere hulpmiddelen vereist. Vraag "Wat is het weer in Seattle in Fahrenheit?" en de agent koppelt automatisch twee hulpmiddelen: eerst roept hij `getCurrentWeather` aan om de temperatuur in Celsius op te halen, vervolgens geeft hij die waarde door aan `celsiusToFahrenheit` voor conversie — alles in één gespreksturn.

<img src="../../../translated_images/nl/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Koppeling van hulpmiddelen in actie — de agent roept eerst getCurrentWeather aan, leidt de Celsius-waarde door naar celsiusToFahrenheit, en levert een gecombineerd antwoord.*

**Graceful failuren** — Vraag om het weer in een stad die niet in de mock data staat. Het hulpmiddel geeft een foutmelding en de AI legt uit dat het niet kan helpen in plaats van te crashen. Hulpmiddelen falen veilig. Het onderstaande diagram vergelijkt beide benaderingen — bij correcte foutafhandeling vangt de agent de exceptie op en reageert behulpzaam, terwijl anders de hele applicatie crasht:

<img src="../../../translated_images/nl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Als een hulpmiddel faalt, vangt de agent de fout op en geeft een behulpzame uitleg in plaats van te crashen.*

Dit gebeurt in één gespreksturn. De agent regelt zelfstandig meerdere hulpmiddel-aanroepen.

## De applicatie uitvoeren

**Verifieer deployment:**

Controleer dat het `.env` bestand in de hoofdmap bestaat met Azure-gegevens (aangemaakt tijdens Module 01). Voer dit uit vanuit de modulemap (`04-tools/`):

**Bash:**  
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```
  
**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` vanuit de hoofdmap (zoals beschreven in Module 01), draait deze module al op poort 8084. Je kunt de startcommando’s hieronder dan overslaan en direct naar http://localhost:8084 gaan.

**Optie 1: Met Spring Boot Dashboard (aanbevolen voor VS Code gebruikers)**

De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-applicaties te beheren. Je vindt deze in de Activity Bar aan de linkerkant van VS Code (zoek het Spring Boot icoon).

In het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de workspace zien
- Applicaties starten/stoppen met één klik
- Logbestanden realtime bekijken
- Applicatiestatus monitoren

Klik gewoon op de play-knop naast "tools" om deze module te starten, of start alle modules tegelijk.

Dit is hoe het Spring Boot Dashboard eruitziet in VS Code:

<img src="../../../translated_images/nl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Het Spring Boot Dashboard in VS Code — start, stop en monitor alle modules vanuit één plek*

**Optie 2: Met shell scripts**

Start alle webapplicaties (modules 01–04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de hoofdmap
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

Beide scripts laden automatisch omgevingsvariabelen vanuit het root `.env` bestand en zullen de JAR-bestanden bouwen als ze nog niet bestaan.

> **Opmerking:** Als je er de voorkeur aan geeft om alle modules handmatig te bouwen voordat je begint:
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

**Om te stoppen:**

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

## De applicatie gebruiken

De applicatie biedt een webinterface waar je kunt communiceren met een AI-agent die toegang heeft tot weer- en temperatuurconversietools. Zo ziet de interface eruit — inclusief snelle voorbeelden en een chatpaneel om verzoeken te versturen:

<a href="images/tools-homepage.png"><img src="../../../translated_images/nl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*De AI Agent Tools-interface - snelle voorbeelden en chatinterface voor interactie met tools*

### Probeer eenvoudige toolgebruik

Begin met een eenvoudige vraag: "Converteer 100 graden Fahrenheit naar Celsius". De agent herkent dat hij de temperatuurconversietool nodig heeft, roept deze aan met de juiste parameters en geeft het resultaat terug. Merk op hoe natuurlijk dit aanvoelt - je hoefde niet te specificeren welke tool te gebruiken of hoe deze aan te roepen.

### Test toolketting

Probeer nu iets complexers: "Wat is het weer in Seattle en converteer het naar Fahrenheit?" Kijk hoe de agent dit in stappen afhandelt. Hij haalt eerst het weer op (dat in Celsius wordt geretourneerd), herkent dat het naar Fahrenheit geconverteerd moet worden, roept de conversietool aan en combineert beide resultaten in één antwoord.

### Zie het gesprekverloop

De chatinterface houdt de gespreksgeschiedenis bij, zodat je meerstapsinteracties kunt voeren. Je kunt alle eerdere vragen en antwoorden zien, wat het gemakkelijk maakt om het gesprek te volgen en te begrijpen hoe de agent context opbouwt over meerdere uitwisselingen.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/nl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Meerstapsgesprek met eenvoudige conversies, weersopzoeken en toolkettingen*

### Experimenteer met verschillende verzoeken

Probeer verschillende combinaties:
- Weeropzoeken: "Wat is het weer in Tokio?"
- Temperatuurconversies: "Wat is 25°C in Kelvin?"
- Gecombineerde vragen: "Check het weer in Parijs en vertel me of het boven de 20°C is"

Merk op hoe de agent natuurlijke taal interpreteert en dit omzet in passende tool-aanroepen.

## Belangrijke concepten

### ReAct-patroon (Redeneren en Handelen)

De agent wisselt af tussen redeneren (beslissen wat te doen) en handelen (gebruik maken van tools). Dit patroon maakt autonome probleemoplossing mogelijk in plaats van alleen reageren op instructies.

### Toolbeschrijvingen zijn belangrijk

De kwaliteit van je toolbeschrijvingen bepaalt hoe goed de agent ze gebruikt. Duidelijke, specifieke beschrijvingen helpen het model begrijpen wanneer en hoe elke tool moet worden aangeroepen.

### Sessiebeheer

De `@MemoryId` annotatie maakt automatische sessiegebaseerde geheugenbeheer mogelijk. Elke sessie-ID krijgt zijn eigen `ChatMemory` instantie die wordt beheerd door de `ChatMemoryProvider` bean, zodat meerdere gebruikers tegelijkertijd met de agent kunnen communiceren zonder dat hun gesprekken door elkaar lopen. De volgende diagram toont hoe meerdere gebruikers worden gerouteerd naar geïsoleerde geheugenopslag op basis van hun sessie-ID's:

<img src="../../../translated_images/nl/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Elke sessie-ID correspondeert met een geïsoleerde gespreksgeschiedenis — gebruikers zien nooit elkaars berichten.*

### Foutafhandeling

Tools kunnen falen — API's kunnen time-outs krijgen, parameters kunnen ongeldig zijn, externe diensten kunnen uitvallen. Productie-agents hebben foutafhandeling nodig zodat het model problemen kan uitleggen of alternatieven kan proberen in plaats van de hele applicatie te laten crashen. Wanneer een tool een uitzondering gooit, vangt LangChain4j deze op en geeft het foutbericht terug aan het model, dat zo het probleem in natuurlijke taal kan uitleggen.

## Beschikbare tools

De onderstaande diagram toont het brede ecosysteem van tools die je kunt bouwen. Deze module demonstreert weer- en temperatuurtools, maar hetzelfde `@Tool` patroon werkt voor elke Java-methode — van databasequeries tot betalingsverwerking.

<img src="../../../translated_images/nl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Elke Java-methode met @Tool wordt beschikbaar voor de AI — het patroon strekt zich uit tot databases, API's, e-mail, bestandsbewerkingen en meer.*

## Wanneer gebruik je tool-gebaseerde agents

Niet elke aanvraag heeft tools nodig. De beslissing hangt af van of de AI moet communiceren met externe systemen of het antwoord uit eigen kennis kan halen. De volgende gids vat samen wanneer tools waarde toevoegen en wanneer ze overbodig zijn:

<img src="../../../translated_images/nl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Een snelle beslissingsgids — tools zijn voor real-time data, berekeningen en acties; algemene kennis en creatieve taken hebben ze niet nodig.*

## Tools vs RAG

Modules 03 en 04 breiden beide uit wat de AI kan doen, maar op fundamenteel verschillende manieren. RAG geeft het model toegang tot **kennis** door documenten op te halen. Tools geven het model de mogelijkheid **acties** uit te voeren door functies aan te roepen. De onderstaande diagram vergelijkt deze twee benaderingen naast elkaar — van hun workflows tot de afwegingen ertussen:

<img src="../../../translated_images/nl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG haalt informatie op uit statische documenten — Tools voeren acties uit en halen dynamische, real-time data op. Veel productiesystemen combineren beide.*

In de praktijk combineren veel productiesystemen beide benaderingen: RAG voor het onderbouwen van antwoorden op basis van documentatie, en Tools voor het ophalen van live data of uitvoeren van handelingen.

## Volgende stappen

**Volgende module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigatie:** [← Vorige: Module 03 - RAG](../03-rag/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onjuistheden kunnen bevatten. Het originele document in de oorspronkelijke taal wordt als de gezaghebbende bron beschouwd. Voor belangrijke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->