# Modul 04: AI-Agenter med Verktyg

## Innehållsförteckning

- [Videogenomgång](../../../04-tools)
- [Det du kommer att lära dig](../../../04-tools)
- [Förkunskaper](../../../04-tools)
- [Att förstå AI-agenter med verktyg](../../../04-tools)
- [Hur verktygsanrop fungerar](../../../04-tools)
  - [Verktygsdefinitioner](../../../04-tools)
  - [Beslutsfattande](../../../04-tools)
  - [Utförande](../../../04-tools)
  - [Generering av svar](../../../04-tools)
  - [Arkitektur: Spring Boot Auto-Wiring](../../../04-tools)
- [Verktygskedjning](../../../04-tools)
- [Kör applikationen](../../../04-tools)
- [Använda applikationen](../../../04-tools)
  - [Prova enkel verktygsanvändning](../../../04-tools)
  - [Testa verktygskedjning](../../../04-tools)
  - [Se konversationsflöde](../../../04-tools)
  - [Experimentera med olika förfrågningar](../../../04-tools)
- [Nyckelbegrepp](../../../04-tools)
  - [ReAct-mönstret (Reasoning and Acting)](../../../04-tools)
  - [Verktygsbeskrivningar är viktiga](../../../04-tools)
  - [Sessionshantering](../../../04-tools)
  - [Felhanteirng](../../../04-tools)
- [Tillgängliga verktyg](../../../04-tools)
- [När man ska använda verktygsbaserade agenter](../../../04-tools)
- [Verktyg vs RAG](../../../04-tools)
- [Nästa steg](../../../04-tools)

## Videogenomgång

Titta på denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Det du kommer att lära dig

Hittills har du lärt dig hur man har samtal med AI, strukturerar prompts effektivt och förankrar svar i dina dokument. Men det finns fortfarande en grundläggande begränsning: språkmodeller kan bara generera text. De kan inte kolla vädret, utföra beräkningar, fråga databaser eller interagera med externa system.

Verktyg förändrar detta. Genom att ge modellen tillgång till funktioner som den kan anropa förvandlas den från en textgenerator till en agent som kan vidta åtgärder. Modellen bestämmer när den behöver ett verktyg, vilket verktyg som ska användas och vilka parametrar som ska skickas. Din kod kör funktionen och returnerar resultatet. Modellen inkorporerar sedan detta resultat i sitt svar.

## Förkunskaper

- Genomförd [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-resurser distribuerade)
- Tidigare moduler rekommenderas (denna modul refererar till [RAG-koncept från Modul 03](../03-rag/README.md) i jämförelsen Verktyg vs RAG)
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Notera:** Om du inte har genomfört Modul 01, följ deploymentsinstruktionerna där först.

## Att förstå AI-agenter med verktyg

> **📝 Notera:** Termen "agenter" i denna modul syftar på AI-assistenter förbättrade med verktygsanropsfunktioner. Detta skiljer sig från **Agentic AI**-mönster (autonoma agenter med planering, minne och flerstegsresonerande) som vi kommer att täcka i [Modul 05: MCP](../05-mcp/README.md).

Utan verktyg kan en språkmodell bara generera text från sin träningsdata. Fråga den om det aktuella vädret, och den måste gissa. Ge den verktyg, så kan den anropa ett väder-API, utföra beräkningar eller fråga en databas — och sedan väva in dessa verkliga resultat i sitt svar.

<img src="../../../translated_images/sv/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Utan verktyg kan modellen bara gissa — med verktyg kan den anropa API:er, göra beräkningar och returnera realtidsdata.*

En AI-agent med verktyg följer ett **Reasoning and Acting (ReAct)**-mönster. Modellen svarar inte bara — den tänker på vad den behöver, agerar genom att anropa ett verktyg, observerar resultatet och avgör sedan om den ska agera igen eller leverera det slutgiltiga svaret:

1. **Reason** — Agenten analyserar användarens fråga och bestämmer vilken information den behöver
2. **Act** — Agenten väljer rätt verktyg, genererar korrekta parametrar och anropar det
3. **Observe** — Agenten tar emot verktygets output och utvärderar resultatet
4. **Repeat or Respond** — Om mer data behövs, loopar agenten tillbaka; annars formulerar den ett svar i naturligt språk

<img src="../../../translated_images/sv/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct-cykeln — agenten resonerar om vad den ska göra, agerar genom att anropa ett verktyg, observerar resultatet, och loopar tills den kan leverera ett slutgiltigt svar.*

Detta sker automatiskt. Du definierar verktygen och deras beskrivningar. Modellen tar hand om beslutsfattandet om när och hur de ska användas.

## Hur verktygsanrop fungerar

### Verktygsdefinitioner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definierar funktioner med klara beskrivningar och paramspecifikationer. Modellen ser dessa beskrivningar i sitt systemprompt och förstår vad varje verktyg gör.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Din logik för väderuppslagning
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistenten är automatiskt kopplad av Spring Boot med:
// - ChatModel bean
// - Alla @Tool-metoder från @Component-klasser
// - ChatMemoryProvider för sessionshantering
```
  
Diagrammet nedan bryter ner varje annotation och visar hur varje del hjälper AI att förstå när verktyget ska anropas och vilka argument som ska skickas:

<img src="../../../translated_images/sv/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomin av en verktygsdefinition — @Tool berättar för AI när det ska användas, @P beskriver varje parameter, och @AiService kopplar ihop allt vid uppstart.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) och fråga:
> - "Hur skulle jag integrera ett riktigt väder-API som OpenWeatherMap istället för mock-data?"
> - "Vad gör en bra verktygsbeskrivning som hjälper AI att använda det korrekt?"
> - "Hur hanterar jag API-fel och rate limits i verktygsimplementationer?"

### Beslutsfattande

När en användare frågar "Hur är vädret i Seattle?" väljer modellen inte verktyg slumpmässigt. Den jämför användarens intention med varje verktygsbeskrivning den har tillgång till, poängsätter varje för relevans och väljer bästa match. Den genererar sedan ett strukturerat funktionsanrop med rätt parametrar – i detta fall sätter den `location` till `"Seattle"`.

Om inget verktyg matchar användarens förfrågan faller modellen tillbaka på att svara från sin egen kunskap. Om flera verktyg matchar väljer den det mest specifika.

<img src="../../../translated_images/sv/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modellen utvärderar varje tillgängligt verktyg mot användarens intention och väljer bästa match – därför är det viktigt att skriva tydliga, specifika verktygsbeskrivningar.*

### Utförande

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot auto-wires den deklarativa `@AiService`-interfacet med alla registrerade verktyg, och LangChain4j exekverar verktygsanrop automatiskt. Bakom kulisserna flyter ett komplett verktygsanrop genom sex steg – från användarens fråga i naturligt språk hela vägen tillbaka till ett svar i naturligt språk:

<img src="../../../translated_images/sv/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Slutet-till-slut-flöde – användaren ställer en fråga, modellen väljer ett verktyg, LangChain4j exekverar det, och modellen väver in resultatet i ett naturligt svar.*

Om du körde [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i Modul 00 har du redan sett detta mönster i action – `Calculator`-verktygen anropades på samma sätt. Sekvensdiagrammet nedan visar exakt vad som hände under huven under den demonstrationen:

<img src="../../../translated_images/sv/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Verktygsanropsloopen från Quick Start-demonstrationen – `AiServices` skickar ditt meddelande och verktygsscheman till LLM, LLM svarar med ett funktionsanrop som `add(42, 58)`, LangChain4j exekverar `Calculator`-metoden lokalt och matar sedan tillbaka resultatet för slutgiltigt svar.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) och fråga:
> - "Hur fungerar ReAct-mönstret och varför är det effektivt för AI-agenter?"
> - "Hur bestämmer agenten vilket verktyg som ska användas och i vilken ordning?"
> - "Vad händer om ett verktygsanrop misslyckas – hur ska jag hantera fel robust?"

### Generering av svar

Modellen tar emot väderdata och formaterar det till ett svar i naturligt språk till användaren.

### Arkitektur: Spring Boot Auto-Wiring

Denna modul använder LangChain4js Spring Boot-integration med deklarativa `@AiService`-interface. Vid uppstart upptäcker Spring Boot varje `@Component` som innehåller `@Tool`-metoder, din `ChatModel` bean och `ChatMemoryProvider` — och kopplar ihop allt till ett enda `Assistant`-interface utan behov av boilerplate.

<img src="../../../translated_images/sv/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService-interfacet knyter ihop ChatModel, verktygskomponenter och minnesleverantör – Spring Boot sköter all koppling automatiskt.*

Här är hela förfrågningslivscykeln som ett sekvensdiagram – från HTTP-förfrågan genom controller, service och auto-wired proxy, hela vägen till verktygsutförandet och tillbaka:

<img src="../../../translated_images/sv/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Den kompletta Spring Boot-förfrågningslivscykeln – HTTP-förfrågan går genom controller och service till den auto-wirade Assistant-proxyn, som orchestrerar LLM och verktygsanrop automatiskt.*

Viktiga fördelar med detta tillvägagångssätt:

- **Spring Boot auto-wiring** — ChatModel och verktyg injiceras automatiskt
- **@MemoryId-mönstret** — Automatisk sessionsbaserad minneshantering
- **Enda instans** — Assistant skapas en gång och återanvänds för bättre prestanda
- **Typsäker exekvering** — Java-metoder anropas direkt med typkonvertering
- **Multi-turn orchestration** — Hanterar verktygskedjning automatiskt
- **Ingen boilerplate** — Inga manuella `AiServices.builder()`-anrop eller minnes-HashMap

Alternativa tillvägagångssätt (manuella `AiServices.builder()`) kräver mer kod och saknar Spring Boot-integrationsfördelarna.

## Verktygskedjning

**Verktygskedjning** – Den verkliga kraften med verktygsbaserade agenter syns när en enda fråga kräver flera verktyg. Fråga "Hur är vädret i Seattle i Fahrenheit?" och agenten kedjar automatiskt två verktyg: först anropar den `getCurrentWeather` för att hämta temperaturen i Celsius, sedan skickar den det värdet till `celsiusToFahrenheit` för konvertering – allt i en enda samtalsrunda.

<img src="../../../translated_images/sv/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Verktygskedjning i praktiken – agenten anropar först getCurrentWeather, sedan skickar Celsius-resultatet till celsiusToFahrenheit och levererar ett sammansatt svar.*

**Felfria hanteringar** – Fråga efter vädret i en stad som inte finns i mock-datan. Verktyget returnerar ett felmeddelande, och AI förklarar att den inte kan hjälpa istället för att krascha. Verktyg misslyckas säkert. Diagrammet nedan jämför de två tillvägagångssätten – med korrekt felhantering fångar agenten undantaget och svarar hjälpsamt, utan det kraschar hela applikationen:

<img src="../../../translated_images/sv/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*När ett verktyg misslyckas fångar agenten felet och svarar med en hjälpsam förklaring istället för att krascha.*

Detta sker i en enda samtalsrunda. Agenten orchestrerar flera verktygsanrop autonomt.

## Kör applikationen

**Verifiera distribution:**

Säkerställ att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapad under Modul 01). Kör detta från modulkatalogen (`04-tools/`):

**Bash:**  
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Starta applikationen:**

> **Notera:** Om du redan startade alla applikationer med `./start-all.sh` från rotkatalogen (som beskrivs i Modul 01) körs denna modul redan på port 8084. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8084.

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderat för VS Code-användare)**

Dev-container inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i Aktivitetsfältet till vänster i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:  
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan  
- Starta/stopp applikationer med ett klick  
- Visa applikationsloggar i realtid  
- Övervaka applikationsstatus  
Klicka helt enkelt på play-knappen bredvid "tools" för att starta denna modul, eller starta alla moduler samtidigt.

Så här ser Spring Boot Dashboard ut i VS Code:

<img src="../../../translated_images/sv/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard i VS Code — starta, stoppa och övervaka alla moduler från en plats*

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotkatalogen
.\start-all.ps1
```

Eller starta bara denna modul:

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

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan start:
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

Öppna http://localhost:8084 i din webbläsare.

**För att stoppa:**

**Bash:**
```bash
./stop.sh  # Endast denna modul
# Eller
cd .. && ./stop-all.sh  # Alla moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Endast denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Använda Applikationen

Applikationen tillhandahåller ett webbgränssnitt där du kan interagera med en AI-agent som har tillgång till väder- och temperaturkonverteringsverktyg. Så här ser gränssnittet ut — det inkluderar snabbstartsexempel och en chattpanel för att skicka förfrågningar:

<a href="images/tools-homepage.png"><img src="../../../translated_images/sv/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools-gränssnittet - snabba exempel och chattgränssnitt för att interagera med verktyg*

### Prova Enkel Verktygsanvändning

Börja med en enkel förfrågan: "Konvertera 100 grader Fahrenheit till Celsius". Agenten förstår att den behöver temperaturskonverteringsverktyget, anropar det med rätt parametrar och returnerar resultatet. Notera hur naturligt detta känns – du specificerade inte vilket verktyg som skulle användas eller hur du skulle anropa det.

### Testa Verktygskedjning

Prova nu något mer komplext: "Hur är vädret i Seattle och konvertera det till Fahrenheit?" Se hur agenten arbetar steg för steg. Den hämtar först vädret (som returnerar i Celsius), inser att det behöver konvertera till Fahrenheit, anropar konverteringsverktyget och kombinerar båda resultaten i ett svar.

### Se Samtalsflödet

Chattgränssnittet sparar samtalshistorik, vilket gör att du kan ha flerstegssamtal. Du kan se alla tidigare frågor och svar, vilket gör det enkelt att följa konversationen och förstå hur agenten bygger kontext över flera utbyten.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sv/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flerstegssamtal som visar enkla konverteringar, väderuppslagningar och verktygskedjning*

### Experimentera med Olika Förfrågningar

Testa olika kombinationer:
- Väderuppslagningar: "Hur är vädret i Tokyo?"
- Temperaturkonverteringar: "Vad är 25°C i Kelvin?"
- Kombinerade frågor: "Kolla vädret i Paris och säg om det är över 20°C"

Lägg märke till hur agenten tolkar naturligt språk och mappas till lämpliga verktygsanrop.

## Nyckelkoncept

### ReAct-mönstret (Resonemang och Handling)

Agenten alternerar mellan att resonera (besluta vad som ska göras) och att agera (använda verktyg). Detta mönster möjliggör autonom problemlösning snarare än att bara svara på instruktioner.

### Verktygsbeskrivningar är Viktiga

Kvaliteten på dina verktygsbeskrivningar påverkar direkt hur väl agenten använder dem. Klara, specifika beskrivningar hjälper modellen att förstå när och hur varje verktyg ska anropas.

### Sessionshantering

`@MemoryId`-annoteringen möjliggör automatisk sessionbaserad minneshantering. Varje sessions-ID får en egen `ChatMemory`-instans som hanteras av `ChatMemoryProvider`-bean, så att flera användare kan interagera med agenten samtidigt utan att deras samtal blandas ihop. Följande diagram visar hur flera användare dirigeras till isolerade minneslager baserat på deras session-ID:

<img src="../../../translated_images/sv/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Varje session-ID mappar till en isolerad konversationshistorik — användare ser aldrig varandras meddelanden.*

### Felsökning

Verktyg kan misslyckas — API:er kan tidsbegränsas, parametrar kan vara ogiltiga, externa tjänster kan gå ner. Produktionsagenter behöver felhantering så att modellen kan förklara problem eller försöka alternativ istället för att krascha hela applikationen. När ett verktyg kastar ett undantag fångar LangChain4j det och matar tillbaka felmeddelandet till modellen, som sedan kan förklara problemet på naturligt språk.

## Tillgängliga Verktyg

Diagrammet nedan visar den breda verktygsekosystemet du kan bygga. Denna modul demonstrerar väder- och temperaturverktyg, men samma `@Tool`-mönster fungerar för vilken Java-metod som helst — från databasanrop till betalningshantering.

<img src="../../../translated_images/sv/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Varje Java-metod annoterad med @Tool blir tillgänglig för AI:n — mönstret sträcker sig till databaser, API:er, e-post, filoperationer och mer.*

## När man Ska Använda Verktygsbaserade Agenter

Inte varje förfrågan behöver verktyg. Beslutet handlar om huruvida AI:n behöver interagera med externa system eller kan svara från sin egen kunskap. Följande guide sammanfattar när verktyg tillför värde och när de är onödiga:

<img src="../../../translated_images/sv/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*En snabb beslutsguide — verktyg är för realtidsdata, beräkningar och åtgärder; generell kunskap och kreativa uppgifter behöver dem inte.*

## Verktyg vs RAG

Modulerna 03 och 04 utökar båda vad AI kan göra, men på fundamentalt olika sätt. RAG ger modellen tillgång till **kunskap** genom att hämta dokument. Verktyg ger modellen möjlighet att utföra **åtgärder** genom att anropa funktioner. Diagrammet nedan jämför dessa två tillvägagångssätt sida vid sida — från hur varje arbetsflöde fungerar till avvägningarna mellan dem:

<img src="../../../translated_images/sv/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG hämtar information från statiska dokument — Verktyg utför åtgärder och hämtar dynamisk, realtidsdata. Många produktionssystem kombinerar båda.*

I praktiken kombinerar många produktionssystem båda tillvägagångssätten: RAG för att förankra svar i din dokumentation, och Verktyg för att hämta live-data eller utföra operationer.

## Nästa Steg

**Nästa modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigering:** [← Föregående: Modul 03 - RAG](../03-rag/README.md) | [Tillbaka till huvudmenyn](../README.md) | [Nästa: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål ska betraktas som den auktoritativa källan. För viktig information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår på grund av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->