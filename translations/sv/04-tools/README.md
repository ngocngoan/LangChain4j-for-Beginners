# Modul 04: AI-agenter med verktyg

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../04-tools)
- [Förutsättningar](../../../04-tools)
- [Förstå AI-agenter med verktyg](../../../04-tools)
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
  - [Se konversationsflödet](../../../04-tools)
  - [Experimentera med olika förfrågningar](../../../04-tools)
- [Nyckelkoncept](../../../04-tools)
  - [ReAct-mönstret (Resonera och agera)](../../../04-tools)
  - [Verktygsbeskrivningars betydelse](../../../04-tools)
  - [Sessionshantering](../../../04-tools)
  - [Felsökning](../../../04-tools)
- [Tillgängliga verktyg](../../../04-tools)
- [När man ska använda verktygsbaserade agenter](../../../04-tools)
- [Verktyg vs RAG](../../../04-tools)
- [Nästa steg](../../../04-tools)

## Vad du kommer att lära dig

Hittills har du lärt dig hur man konverserar med AI, strukturerar prompts effektivt och grundar svar i dina dokument. Men det finns fortfarande en grundläggande begränsning: språkmodeller kan bara generera text. De kan inte kolla vädret, utföra beräkningar, fråga databaser eller interagera med externa system.

Verktyg ändrar detta. Genom att ge modellen tillgång till funktioner den kan anropa förvandlar du den från en textgenerator till en agent som kan vidta åtgärder. Modellen bestämmer när den behöver ett verktyg, vilket verktyg den ska använda, och vilka parametrar som ska skickas. Din kod kör funktionen och returnerar resultatet. Modellen inkorporerar det resultatet i sitt svar.

## Förutsättningar

- Slutförd Modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotmappen med Azure-uppgifter (skapad av `azd up` i Modul 01)

> **Notera:** Om du inte har slutfört Modul 01, följ först distributionsinstruktionerna där.

## Förstå AI-agenter med verktyg

> **📝 Notera:** Termen "agenter" i denna modul avser AI-assistenter utökade med verktygsanropsfunktioner. Detta skiljer sig från **Agentic AI**-mönstren (autonoma agenter med planering, minne och flerstegsresonemang) som vi kommer att täcka i [Modul 05: MCP](../05-mcp/README.md).

Utan verktyg kan en språkmodell bara generera text från sin träningsdata. Fråga efter aktuellt väder, och den måste gissa. Ge den verktyg, och den kan anropa en väder-API, utföra beräkningar eller fråga en databas — och sedan väva in dessa verkliga resultat i sitt svar.

<img src="../../../translated_images/sv/what-are-tools.724e468fc4de64da.webp" alt="Utan verktyg vs Med verktyg" width="800"/>

*Utan verktyg kan modellen bara gissa — med verktyg kan den anropa API:er, köra beräkningar och leverera realtidsdata.*

En AI-agent med verktyg följer ett **Reasoning and Acting (ReAct)**-mönster. Modellen svarar inte bara — den tänker över vad den behöver, agerar genom att anropa ett verktyg, observerar resultatet och beslutar sedan om den ska agera igen eller ge det slutgiltiga svaret:

1. **Resonera** — Agenten analyserar användarens fråga och avgör vilken information som behövs
2. **Agera** — Agenten väljer rätt verktyg, genererar korrekta parametrar och anropar det
3. **Observera** — Agenten tar emot verktygets utdata och utvärderar resultatet
4. **Upprepa eller svara** — Om mer data behövs går agenten tillbaka i loopen; annars formulerar den ett naturligt språk-svar

<img src="../../../translated_images/sv/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct-mönstret" width="800"/>

*ReAct-cykeln — agenten resonerar kring vad som ska göras, agerar genom att anropa ett verktyg, observerar resultatet och loopar tills det kan leverera ett slutgiltigt svar.*

Detta sker automatiskt. Du definierar verktygen och deras beskrivningar. Modellen hanterar beslutsfattandet om när och hur de ska användas.

## Hur verktygsanrop fungerar

### Verktygsdefinitioner

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Du definierar funktioner med tydliga beskrivningar och parameter-specifikationer. Modellen ser dessa beskrivningar i sin systemprompt och förstår vad varje verktyg gör.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Din väderuppslagslogik
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

Diagrammet nedan bryter ner varje annotation och visar hur varje del hjälper AI:n att förstå när verktyget ska anropas och vilka argument som ska skickas:

<img src="../../../translated_images/sv/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomi av verktygsdefinitioner" width="800"/>

*Anatomi av en verktygsdefinition — @Tool berättar för AI när det ska användas, @P beskriver varje parameter, och @AiService kopplar ihop allt vid uppstart.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) och fråga:
> - "Hur kan jag integrera en riktig väder-API som OpenWeatherMap istället för mock-data?"
> - "Vad gör en bra verktygsbeskrivning som hjälper AI att använda det korrekt?"
> - "Hur hanterar jag API-fel och hastighetsbegränsningar i verktygsimplementeringar?"

### Beslutsfattande

När en användare frågar "Hur är vädret i Seattle?" väljer modellen inte verktyg slumpmässigt. Den jämför användarens avsikt med varje verktygsbeskrivning den har tillgång till, poängsätter varje för relevans och väljer det bästa matchande. Den genererar sedan ett strukturerat funktionsanrop med rätt parametrar — i detta fall sätter den `location` till `"Seattle"`.

Om inget verktyg matchar användarens förfrågan faller modellen tillbaka till att svara från sin egen kunskap. Om flera verktyg matchar väljs det mest specifika.

<img src="../../../translated_images/sv/decision-making.409cd562e5cecc49.webp" alt="Hur AI:n avgör vilket verktyg som ska användas" width="800"/>

*Modellen utvärderar varje tillgängligt verktyg mot användarens avsikt och väljer bästa matchningen — därför är det viktigt med tydliga, specifika verktygsbeskrivningar.*

### Utförande

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot auto-wirear det deklarativa `@AiService` gränssnittet med alla registrerade verktyg, och LangChain4j utför verktygsanrop automatiskt. Bakom kulisserna flödar ett komplett verktygsanrop genom sex steg — från användarens fråga på naturligt språk ända tillbaka till ett svar på naturligt språk:

<img src="../../../translated_images/sv/tool-calling-flow.8601941b0ca041e6.webp" alt="Flöde för verktygsanrop" width="800"/>

*End-to-end-flöde — användaren ställer en fråga, modellen väljer ett verktyg, LangChain4j kör verktyget, och modellen väver in resultatet i ett naturligt svar.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) och fråga:
> - "Hur fungerar ReAct-mönstret och varför är det effektivt för AI-agenter?"
> - "Hur avgör agenten vilket verktyg som ska användas och i vilken ordning?"
> - "Vad händer om ett verktygsanrop misslyckas - hur bör jag hantera fel robust?"

### Generering av svar

Modellen tar emot väderdata och formatterar det till ett naturligt språk-svar för användaren.

### Arkitektur: Spring Boot Auto-Wiring

Denna modul använder LangChain4js Spring Boot-integration med deklarativa `@AiService`-gränssnitt. Vid uppstart upptäcker Spring Boot varje `@Component` som innehåller `@Tool`-metoder, din `ChatModel`-bean och `ChatMemoryProvider` — och kopplar samman allt till ett enda `Assistant`-gränssnitt utan behov av boilerplate.

<img src="../../../translated_images/sv/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring-arkitektur" width="800"/>

*@AiService-gränssnittet kopplar ihop ChatModel, verktygskomponenter och minnesleverantör — Spring Boot hanterar all koppling automatiskt.*

Viktiga fördelar med denna metod:

- **Spring Boot auto-wiring** — ChatModel och verktyg injiceras automatiskt
- **@MemoryId-mönster** — Automatisk sessionbaserad minneshantering
- **En enda instans** — Assistant skapas en gång och återanvänds för bättre prestanda
- **Typ-säker körning** — Java-metoder anropas direkt med typkonvertering
- **Multistegsorkestrering** — Hanterar verktygskedjning automatiskt
- **Ingen boilerplate** — Inga manuella `AiServices.builder()`-anrop eller minnes-HashMap

Alternativa metoder (manuella `AiServices.builder()`) kräver mer kod och saknar Spring Boot-integrationsfördelarna.

## Verktygskedjning

**Verktygskedjning** — Den verkliga kraften hos verktygsbaserade agenter visar sig när en enda fråga kräver flera verktyg. Fråga "Hur är vädret i Seattle i Fahrenheit?" och agenten kedjar automatiskt två verktyg: först anropas `getCurrentWeather` för att få temperaturen i Celsius, sedan skickas den värdet till `celsiusToFahrenheit` för konvertering — allt i en enda konversationsvändning.

<img src="../../../translated_images/sv/tool-chaining-example.538203e73d09dd82.webp" alt="Exempel på verktygskedjning" width="800"/>

*Verktygskedjning i praktiken — agenten anropar först getCurrentWeather, sedan skickas Celsius-resultatet till celsiusToFahrenheit, och ett kombinerat svar levereras.*

Så här ser det ut i den körande applikationen — agenten kedjar två verktygsanrop i en konversationsvändning:

<a href="images/tool-chaining.png"><img src="../../../translated_images/sv/tool-chaining.3b25af01967d6f7b.webp" alt="Verktygskedjning" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Faktiskt applikationsutdata — agenten kedjar automatiskt getCurrentWeather → celsiusToFahrenheit i en enda vändning.*

**Felfri hantering** — Fråga efter vädret i en stad som inte finns i mock-datan. Verktyget returnerar ett felmeddelande, och AI:n förklarar att den inte kan hjälpa till istället för att krascha. Verktyg misslyckas säkert.

<img src="../../../translated_images/sv/error-handling-flow.9a330ffc8ee0475c.webp" alt="Flöde för felhantering" width="800"/>

*När ett verktyg misslyckas fångar agenten felet och svarar med en hjälpsam förklaring istället för att krascha.*

Detta sker i en enda konversationsvändning. Agenten orkestrerar flera verktygsanrop självständigt.

## Kör applikationen

**Verifiera distribution:**

Kontrollera att `.env`-filen finns i rotmappen med Azure-uppgifter (skapad under Modul 01):
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startat alla applikationer med `./start-all.sh` från Modul 01, kör denna modul redan på port 8084. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8084.

**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-extensionen, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar den i aktivitetsfältet på vänster sida i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stopp applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka på play-knappen bredvid "tools" för att starta denna modul, eller starta alla moduler samtidigt.

<img src="../../../translated_images/sv/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

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

Båda skripten laddar automatiskt miljövariabler från rotmappen `.env` och bygger JAR-filerna om de inte finns.

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


## Använda applikationen

Applikationen tillhandahåller ett webbgränssnitt där du kan interagera med en AI-agent som har tillgång till verktyg för väder och temperaturkonvertering.

<a href="images/tools-homepage.png"><img src="../../../translated_images/sv/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Gränssnitt för AI Agent Tools - snabba exempel och chattgränssnitt för att interagera med verktyg*

### Prova enkel verktygsanvändning
Börja med en enkel begäran: "Konvertera 100 grader Fahrenheit till Celsius". Agenten förstår att den behöver temperaturkonverteringsverktyget, anropar det med rätt parametrar och returnerar resultatet. Lägg märke till hur naturligt detta känns – du specificerade inte vilket verktyg som skulle användas eller hur det skulle anropas.

### Testa Kedjning av Verktyg

Prova nu något mer komplext: "Vad är vädret i Seattle och konvertera det till Fahrenheit?" Se hur agenten arbetar igenom detta steg för steg. Den hämtar först vädret (som returnerar Celsius), förstår att den behöver konvertera till Fahrenheit, anropar konverteringsverktyget och kombinerar båda resultaten till ett svar.

### Se Konversationsflödet

Chattgränssnittet behåller konversationshistoriken, vilket möjliggör interaktioner över flera steg. Du kan se alla tidigare frågor och svar, vilket gör det enkelt att följa konversationen och förstå hur agenten bygger kontext över flera utbyten.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sv/tools-conversation-demo.89f2ce9676080f59.webp" alt="Konversation med flera verktygsanrop" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Flerstegs-konversation som visar enkla konverteringar, väderuppslag och kedjning av verktyg*

### Experimentera med Olika Begäranden

Prova olika kombinationer:
- Väderuppslag: "Hur är vädret i Tokyo?"
- Temperaturkonverteringar: "Vad är 25°C i Kelvin?"
- Kombinerade frågor: "Kolla vädret i Paris och säg om det är över 20°C"

Lägg märke till hur agenten tolkar naturligt språk och kartlägger det till lämpliga verktygsanrop.

## Viktiga Begrepp

### ReAct-mönstret (Resonera och Agera)

Agenten växlar mellan att resonera (besluta vad som ska göras) och agera (använda verktyg). Detta mönster möjliggör autonom problemlösning snarare än bara att svara på instruktioner.

### Verktygsbeskrivningar Är Viktiga

Kvaliteten på dina verktygsbeskrivningar påverkar direkt hur väl agenten använder dem. Klara, specifika beskrivningar hjälper modellen att förstå när och hur varje verktyg ska anropas.

### Sessionshantering

`@MemoryId`-annoteringen möjliggör automatisk sessionbaserad minneshantering. Varje sessions-ID får sin egen `ChatMemory`-instans som hanteras av `ChatMemoryProvider`-bean:en, så att flera användare kan interagera med agenten samtidigt utan att deras konversationer blandas ihop.

<img src="../../../translated_images/sv/session-management.91ad819c6c89c400.webp" alt="Sessionshantering med @MemoryId" width="800"/>

*Varje sessions-ID kopplas till en isolerad konversationshistorik – användare ser aldrig varandras meddelanden.*

### Felhantering

Verktyg kan misslyckas – API:er kan tidsbegränsas, parametrar kan vara ogiltiga, externa tjänster kan gå ner. Produktionsagenter behöver felhantering så att modellen kan förklara problem eller försöka alternativ snarare än att krascha hela applikationen. När ett verktyg kastar ett undantag fångar LangChain4j det och skickar felmeddelandet tillbaka till modellen, som sedan kan förklara problemet på naturligt språk.

## Tillgängliga Verktyg

Diagrammet nedan visar den breda ekosystemet av verktyg du kan bygga. Denna modul demonstrerar väder- och temperaturverktyg, men samma `@Tool`-mönster fungerar för vilken Java-metod som helst – från databasfrågor till betalningshantering.

<img src="../../../translated_images/sv/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Verktygsekosystem" width="800"/>

*Vilken Java-metod som helst märkta med @Tool blir tillgänglig för AI – mönstret sträcker sig till databaser, API:er, e-post, filoperationer och mer.*

## När Ska Man Använda Verktygsbaserade Agenter

<img src="../../../translated_images/sv/when-to-use-tools.51d1592d9cbdae9c.webp" alt="När man ska använda verktyg" width="800"/>

*En snabb beslutsguide – verktyg är för realtidsdata, beräkningar och åtgärder; allmän kunskap och kreativa uppgifter kräver dem inte.*

**Använd verktyg när:**
- Svar kräver realtidsdata (väder, aktiekurser, lagerstatus)
- Du behöver utföra beräkningar utöver enkel matematik
- Åtkomst till databaser eller API:er
- Vid åtgärder (skicka e-post, skapa ärenden, uppdatera poster)
- Kombination av flera datakällor

**Använd inte verktyg när:**
- Frågor kan besvaras med allmän kunskap
- Svaret är rent konversationellt
- Verktygslatens gör upplevelsen för långsam

## Verktyg vs RAG

Modulerna 03 och 04 båda utökar vad AI kan göra, men på fundamentalt olika sätt. RAG ger modellen tillgång till **kunskap** genom att hämta dokument. Verktyg ger modellen förmågan att utföra **åtgärder** genom att anropa funktioner.

<img src="../../../translated_images/sv/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Verktyg vs RAG jämförelse" width="800"/>

*RAG hämtar information från statiska dokument – Verktyg utför åtgärder och hämtar dynamisk, realtidsdata. Många produktionssystem kombinerar båda.*

I praktiken kombinerar många produktionssystem båda angreppssätten: RAG för att förankra svar i din dokumentation och Verktyg för att hämta live-data eller utföra operationer.

## Nästa Steg

**Nästa modul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigering:** [← Föregående: Modul 03 - RAG](../03-rag/README.md) | [Tillbaka till Start](../README.md) | [Nästa: Modul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör man vara medveten om att automatiska översättningar kan innehålla fel eller brister. Originaldokumentet på dess ursprungliga språk ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->