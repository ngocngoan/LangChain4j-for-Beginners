# Module 04: AI-agenten met tools

## Inhoudsopgave

- [Wat je leert](../../../04-tools)
- [Vereisten](../../../04-tools)
- [Begrip van AI-agenten met tools](../../../04-tools)
- [Hoe tool-aanroepen werken](../../../04-tools)
  - [Tooldefinities](../../../04-tools)
  - [Besluitvorming](../../../04-tools)
  - [Uitvoering](../../../04-tools)
  - [Responsgeneratie](../../../04-tools)
  - [Architectuur: Spring Boot auto-wiring](../../../04-tools)
- [Tool chaining](../../../04-tools)
- [Start de applicatie](../../../04-tools)
- [De applicatie gebruiken](../../../04-tools)
  - [Probeer eenvoudige toolgebruik](../../../04-tools)
  - [Test tool chaining](../../../04-tools)
  - [Bekijk het conversatieverloop](../../../04-tools)
  - [Experimenteer met verschillende verzoeken](../../../04-tools)
- [Belangrijke concepten](../../../04-tools)
  - [ReAct-patroon (Redeneren en Handelen)](../../../04-tools)
  - [Toolbeschrijvingen zijn belangrijk](../../../04-tools)
  - [Sessiebeheer](../../../04-tools)
  - [Foutafhandeling](../../../04-tools)
- [Beschikbare tools](../../../04-tools)
- [Wanneer gebruik je tool-gebaseerde agenten](../../../04-tools)
- [Tools versus RAG](../../../04-tools)
- [Volgende stappen](../../../04-tools)

## Wat je leert

Tot nu toe heb je geleerd hoe je gesprekken met AI voert, prompts effectief structureert en antwoorden baseert op je documenten. Maar er blijft een fundamentele beperking: taalmodellen kunnen alleen tekst genereren. Ze kunnen het weer niet checken, geen berekeningen maken, geen databases raadplegen of interactie hebben met externe systemen.

Tools veranderen dit. Door het model toegang te geven tot functies die het kan aanroepen, verander je het van een tekstgenerator in een agent die acties kan uitvoeren. Het model beslist wanneer het een tool nodig heeft, welke tool te gebruiken en welke parameters door te geven. Jouw code voert de functie uit en geeft het resultaat terug. Het model verwerkt dat resultaat in zijn antwoord.

## Vereisten

- Module 01 voltooid (Azure OpenAI-resources gedeployed)
- `.env`-bestand in de hoofdmap met Azure-referenties (aangemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt afgerond, volg daar eerst de deploy-instructies.

## Begrip van AI-agenten met tools

> **📝 Opmerking:** De term "agenten" in deze module verwijst naar AI-assistenten met mogelijkheden om tools aan te roepen. Dit is anders dan de **Agentic AI**-patronen (autonome agenten met planning, geheugen en meervoudige redeneerstappen) die we behandelen in [Module 05: MCP](../05-mcp/README.md).

Zonder tools kan een taalmodel alleen tekst genereren uit zijn trainingsdata. Vraag het naar het huidige weer en het moet raden. Geef het tools en het kan een weer-API aanroepen, berekeningen uitvoeren of een database raadplegen — en die echte resultaten verwerken in het antwoord.

<img src="../../../translated_images/nl/what-are-tools.724e468fc4de64da.webp" alt="Zonder Tools versus Met Tools" width="800"/>

*Zonder tools kan het model alleen gissen — met tools kan het API's aanroepen, berekeningen uitvoeren en realtime data teruggeven.*

Een AI-agent met tools volgt een **Reasoning and Acting (ReAct)**-patroon. Het model reageert niet alleen — het denkt na over wat het nodig heeft, handelt door een tool aan te roepen, observeert het resultaat en beslist dan of het opnieuw moet handelen of het definitieve antwoord geeft:

1. **Redeneer** — De agent analyseert de vraag van de gebruiker en bepaalt welke informatie nodig is
2. **Handel** — De agent kiest de juiste tool, maakt de juiste parameters aan en roept deze aan
3. **Observeer** — De agent ontvangt de output van de tool en evalueert het resultaat
4. **Herhaal of antwoord** — Als meer data nodig is, keert de agent terug naar stap 1; anders formuleert hij een natuurlijk taalantwoord

<img src="../../../translated_images/nl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct-patroon" width="800"/>

*De ReAct-cyclus — de agent redeneert over wat te doen, handelt door een tool aan te roepen, observeert het resultaat en herhaalt totdat het het definitieve antwoord kan geven.*

Dit gebeurt automatisch. Jij definieert de tools en hun beschrijvingen. Het model verzorgt de besluitvorming over wanneer en hoe ze te gebruiken.

## Hoe tool-aanroepen werken

### Tooldefinities

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Je definieert functies met duidelijke beschrijvingen en parameter-specificaties. Het model ziet deze beschrijvingen in zijn systeem-prompt en begrijpt wat elke tool doet.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Jouw weeropzoekslogica
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent is automatisch verbonden door Spring Boot met:
// - ChatModel bean
// - Alle @Tool methoden van @Component klassen
// - ChatMemoryProvider voor sessiebeheer
```

Het diagram hieronder breekt elke annotatie af en toont hoe elk onderdeel de AI helpt te begrijpen wanneer de tool aangeroepen moet worden en welke argumenten doorgegeven:

<img src="../../../translated_images/nl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomie van Tooldefinities" width="800"/>

*Anatomie van een tooldefinitie — @Tool vertelt de AI wanneer die gebruikt moet worden, @P beschrijft elke parameter, en @AiService zorgt bij het opstarten voor alle verbindingen.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) en vraag:
> - "Hoe integreer ik een echte weer-API zoals OpenWeatherMap in plaats van mockdata?"
> - "Wat maakt een goede toolbeschrijving die de AI helpt om het correct te gebruiken?"
> - "Hoe ga ik om met API-fouten en limieten in toolimplementaties?"

### Besluitvorming

Als een gebruiker vraagt "Hoe is het weer in Seattle?", kiest het model niet willekeurig een tool. Het vergelijkt de intentie van de gebruiker met elke toolbeschrijving waar het toegang toe heeft, scoort ze op relevantie en kiest de beste match. Vervolgens genereert het een gestructureerde functie-aanroep met de juiste parameters — in dit geval zet het `location` op `"Seattle"`.

Als geen enkele tool aansluit bij het verzoek, vertrouwt het model op zijn eigen kennis. Als meerdere tools passen, kiest het de meest specifieke.

<img src="../../../translated_images/nl/decision-making.409cd562e5cecc49.webp" alt="Hoe de AI beslist welke tool te gebruiken" width="800"/>

*Het model evalueert elke beschikbare tool op basis van de intentie van de gebruiker en kiest de beste match — daarom is het schrijven van duidelijke, specifieke toolbeschrijvingen belangrijk.*

### Uitvoering

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot wired automatisch de declaratieve `@AiService` interface met alle geregistreerde tools, en LangChain4j voert tool-aanroepen automatisch uit. Achter de schermen doorloopt een volledige tool-aanroep zes fases — van de natuurlijke taalvraag van de gebruiker tot en met het natuurlijke taalantwoord:

<img src="../../../translated_images/nl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool-aanroepflow" width="800"/>

*De end-to-end flow — de gebruiker stelt een vraag, het model kiest een tool, LangChain4j voert deze uit, en het model verwerkt het resultaat in een natuurlijk antwoord.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) en vraag:
> - "Hoe werkt het ReAct-patroon en waarom is het effectief voor AI-agenten?"
> - "Hoe bepaalt de agent welke tool te gebruiken en in welke volgorde?"
> - "Wat gebeurt er als een tool-aanroep faalt - hoe kan ik fouten robuust afhandelen?"

### Responsgeneratie

Het model ontvangt de weerdata en formatteert deze in een natuurlijk taalantwoord voor de gebruiker.

### Architectuur: Spring Boot auto-wiring

Deze module maakt gebruik van LangChain4j's integratie met Spring Boot en declaratieve `@AiService`-interfaces. Bij het opstarten ontdekt Spring Boot elke `@Component` met `@Tool`-methoden, jouw `ChatModel`-bean en de `ChatMemoryProvider` — en verbindt alles in één `Assistant` interface zonder handmatige configuratie.

<img src="../../../translated_images/nl/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architectuur" width="800"/>

*De @AiService interface verbindt de ChatModel, toolcomponenten en memory provider — Spring Boot regelt de wiring automatisch.*

Belangrijke voordelen van deze aanpak:

- **Spring Boot auto-wiring** — ChatModel en tools worden automatisch geïnjecteerd
- **@MemoryId-patroon** — Automatisch sessie-gebaseerd geheugenbeheer
- **Één instantie** — Assistant wordt één keer gemaakt en hergebruikt voor betere prestaties
- **Type-veilige uitvoering** — Java-methoden worden direct aangeroepen met typeconversie
- **Meervoudige beurten orkestratie** — Behandelt tool chaining automatisch
- **Geen boilerplate** — Geen handmatige `AiServices.builder()`-aanroepen of geheugen-hashmaps

Alternatieve benaderingen (handmatig `AiServices.builder()`) vereisen meer code en missen de voordelen van Spring Boot-integratie.

## Tool chaining

**Tool chaining** — De echte kracht van tool-gebaseerde agenten komt naar voren als één vraag meerdere tools nodig heeft. Vraag "Hoe is het weer in Seattle in Fahrenheit?" en de agent koppelt automatisch twee tools: eerst roept hij `getCurrentWeather` aan om de temperatuur in Celsius te krijgen, daarna geeft hij die waarde door aan `celsiusToFahrenheit` voor conversie — alles in één gespreksturn.

<img src="../../../translated_images/nl/tool-chaining-example.538203e73d09dd82.webp" alt="Voorbeeld van tool chaining" width="800"/>

*Tool chaining in actie — de agent roept eerst getCurrentWeather aan, voert de Celsius-waarde door naar celsiusToFahrenheit, en geeft een gecombineerd antwoord.*

Dit ziet er in de actieve applicatie zo uit — de agent ketent twee tool-aanroepen in één gespreksturn:

<a href="images/tool-chaining.png"><img src="../../../translated_images/nl/tool-chaining.3b25af01967d6f7b.webp" alt="Tool chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Werkelijke output van de applicatie — de agent ketent automatisch getCurrentWeather → celsiusToFahrenheit in één beurt.*

**Graceful Failures** — Vraag naar het weer in een stad die niet in de mock data zit. De tool geeft een foutmelding terug, en de AI legt uit dat hij niet kan helpen in plaats van te crashen. Tools falen veilig.

<img src="../../../translated_images/nl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Foutafhandelingsflow" width="800"/>

*Als een tool faalt, vangt de agent de fout op en reageert met een behulpzame uitleg in plaats van te crashen.*

Dit gebeurt in één gespreksturn. De agent orkestreert meerdere tool-aanroepen zelfstandig.

## Start de applicatie

**Verifieer de deployment:**

Zorg dat het `.env`-bestand bestaat in de hoofdmap met Azure-gegevens (aangemaakt tijdens Module 01):
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` uit Module 01, draait deze module al op poort 8084. Je kunt de startcommando's hieronder overslaan en direct naar http://localhost:8084 gaan.

**Optie 1: Gebruik de Spring Boot Dashboard (aanbevolen voor VS Code-gebruikers)**

De devcontainer bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-applicaties te beheren. Je vindt hem in de Activiteitenbalk links in VS Code (zoek het Spring Boot-pictogram).

Met het Spring Boot-dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de workspace zien
- Applicaties starten/stoppen met één klik
- Applicatielogs realtime bekijken
- Applicatiestatus monitoren

Klik simpelweg op de playknop naast "tools" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik shell-scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanaf de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanaf de hoofdmap
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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env`-bestand en bouwen de JARs als ze niet bestaan.

> **Opmerking:** Als je alle modules handmatig wilt bouwen voor het starten:
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

De applicatie biedt een webinterface waar je met een AI-agent kunt interacteren die toegang heeft tot weer- en temperatuurconversietools.

<a href="images/tools-homepage.png"><img src="../../../translated_images/nl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*De AI Agent Tools-interface - snelle voorbeelden en chatinterface om met tools te interacteren*

### Probeer eenvoudige toolgebruik
Begin met een eenvoudige vraag: "Converteer 100 graden Fahrenheit naar Celsius". De agent herkent dat hij de temperatuurconversietool nodig heeft, roept deze aan met de juiste parameters en geeft het resultaat terug. Merk op hoe natuurlijk dit aanvoelt - je hebt niet gespecificeerd welke tool te gebruiken of hoe deze aan te roepen.

### Test Tool Chaining

Probeer nu iets complexers: "Wat is het weer in Seattle en converteer het naar Fahrenheit?" Kijk hoe de agent dit in stappen afhandelt. Hij haalt eerst het weer op (dat terugkeert in Celsius), herkent dat hij naar Fahrenheit moet converteren, roept de conversietool aan en combineert beide resultaten tot één antwoord.

### Zie het Gespreksverloop

De chatinterface bewaart de gespreksgeschiedenis, waardoor je multi-turn interacties kunt voeren. Je ziet alle voorgaande vragen en antwoorden, wat het gemakkelijk maakt het gesprek te volgen en te begrijpen hoe de agent context opbouwt over meerdere uitwisselingen.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/nl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Gesprek met Meerdere Tool-aanroepen" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Meerledig gesprek toont eenvoudige conversies, weeropvragingen en tool chaining*

### Experimenteer met Verschillende Verzoeken

Probeer diverse combinaties:
- Weeropvragingen: "Wat is het weer in Tokio?"
- Temperatuurconversies: "Wat is 25°C in Kelvin?"
- Gecombineerde vragen: "Check het weer in Parijs en vertel me of het boven de 20°C is"

Merk op hoe de agent natuurlijke taal interpreteert en koppelt aan geschikte tool-aanroepen.

## Belangrijke Concepten

### ReAct-patroon (Redeneren en Handelen)

De agent wisselt af tussen redeneren (beslissen wat te doen) en handelen (gebruik maken van tools). Dit patroon maakt autonome probleemoplossing mogelijk in plaats van alleen reageren op instructies.

### Toolbeschrijvingen Zijn Belangrijk

De kwaliteit van je toolbeschrijvingen beïnvloedt direct hoe goed de agent deze gebruikt. Duidelijke, specifieke beschrijvingen helpen het model te begrijpen wanneer en hoe elke tool aan te roepen.

### Sessiebeheer

De `@MemoryId`-annotatie maakt automatische sessiegerichte geheugenbeheer mogelijk. Elke sessie-ID krijgt een eigen `ChatMemory`-instantie beheerd door de `ChatMemoryProvider`-bean, zodat meerdere gebruikers tegelijk met de agent kunnen praten zonder dat gesprekken door elkaar lopen.

<img src="../../../translated_images/nl/session-management.91ad819c6c89c400.webp" alt="Sessiebeheer met @MemoryId" width="800"/>

*Elke sessie-ID correspondeert met een geïsoleerde gespreksgeschiedenis — gebruikers zien elkaars berichten nooit.*

### Foutafhandeling

Tools kunnen falen — API's timen uit, parameters kunnen ongeldig zijn, externe services kunnen offline gaan. Productieagents moeten foutafhandeling hebben zodat het model problemen kan uitleggen of alternatieven kan proberen in plaats van de hele applicatie te laten crashen. Wanneer een tool een uitzondering gooit, vangt LangChain4j deze en geeft het foutbericht terug aan het model, dat dan het probleem in natuurlijke taal kan uitleggen.

## Beschikbare Tools

De onderstaande diagram toont het brede ecosysteem van tools die je kunt bouwen. Deze module demonstreert weer- en temperatuurtools, maar hetzelfde `@Tool`-patroon werkt voor elke Java-methode — van database-queries tot betalingsverwerking.

<img src="../../../translated_images/nl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosysteem" width="800"/>

*Elke Java-methode die met @Tool is geannoteerd wordt beschikbaar voor de AI — het patroon strekt zich uit tot databases, API's, e-mail, bestandsoperaties en meer.*

## Wanneer Gebruik Je Tool-Based Agents

<img src="../../../translated_images/nl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Wanneer Tools Gebruiken" width="800"/>

*Een snelle beslissingsgids — tools zijn voor realtime data, berekeningen en acties; algemene kennis en creatieve taken hebben ze niet nodig.*

**Gebruik tools wanneer:**
- Antwoorden realtime data vereisen (weer, aandelenkoersen, voorraad)
- Je berekeningen moet uitvoeren die verder gaan dan simpele wiskunde
- Toegang tot databases of API’s nodig is
- Acties moeten worden uitgevoerd (e-mails verzenden, tickets aanmaken, records bijwerken)
- Meerdere gegevensbronnen gecombineerd moeten worden

**Gebruik geen tools wanneer:**
- Vragen beantwoord kunnen worden met algemene kennis
- Het antwoord puur conversational is
- Tool-latentie de ervaring te traag maakt

## Tools versus RAG

Module 03 en 04 breiden beide uit wat de AI kan doen, maar op fundamenteel verschillende manieren. RAG geeft het model toegang tot **kennis** door documenten op te halen. Tools geven het model de mogelijkheid om **acties uit te voeren** door functies aan te roepen.

<img src="../../../translated_images/nl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Vergelijking" width="800"/>

*RAG haalt informatie op uit statische documenten — Tools voeren acties uit en halen dynamische, realtime data op. Veel productiesystemen combineren beide.*

In de praktijk combineren veel productiesystemen beide benaderingen: RAG om antwoorden te onderbouwen met jouw documentatie, en Tools om live data op te halen of operaties uit te voeren.

## Volgende Stappen

**Volgende Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigatie:** [← Vorige: Module 03 - RAG](../03-rag/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet als de gezaghebbende bron worden beschouwd. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor enige misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->