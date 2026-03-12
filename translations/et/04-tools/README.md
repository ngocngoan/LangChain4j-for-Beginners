# Moodul 04: Tehisintellekti agendid tööriistadega

## Sisukord

- [Video juhend](../../../04-tools)
- [Mida õpite](../../../04-tools)
- [Eeldused](../../../04-tools)
- [Tehisintellekti agentide mõistmine tööriistadega](../../../04-tools)
- [Kuidas tööriistakutse töötab](../../../04-tools)
  - [Tööriistade definitsioonid](../../../04-tools)
  - [Otsuste tegemine](../../../04-tools)
  - [Täideviimine](../../../04-tools)
  - [Vastusvõime genereerimine](../../../04-tools)
  - [Arhitektuur: Spring Boot automaatühendus](../../../04-tools)
- [Tööriistade aheldamine](../../../04-tools)
- [Rakenduse käivitamine](../../../04-tools)
- [Rakenduse kasutamine](../../../04-tools)
  - [Proovi lihtsat tööriista kasutust](../../../04-tools)
  - [Testi tööriistade aheldamist](../../../04-tools)
  - [Vaata vestluse kulgu](../../../04-tools)
  - [Katseta erinevate päringutega](../../../04-tools)
- [Põhikontseptsioonid](../../../04-tools)
  - [ReAct muster (põhjendamine ja tegutsemine)](../../../04-tools)
  - [Tööriistade kirjeldused on olulised](../../../04-tools)
  - [Seansi haldus](../../../04-tools)
  - [Veadega toimetulek](../../../04-tools)
- [Saadaval olevad tööriistad](../../../04-tools)
- [Millal kasutada tööriistapõhiseid agente](../../../04-tools)
- [Tööriistad vs RAG](../../../04-tools)
- [Järgmised sammud](../../../04-tools)

## Video juhend

Vaadake seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## Mida õpite

Seni olete õppinud, kuidas tehisintellektiga vestelda, vormistada päringuid efektiivselt ning siduda vastuseid oma dokumentidega. Kuid on olemas põhiepitsus: keelemudelid suudavad genereerida ainult teksti. Nad ei saa ilma abiliste ja tööriistadeta ilmaennustust kontrollida, arvutusi teha, andmebaase pärida ega välissüsteemidega suhelda.

Tööriistad muudavad selle. Kui mudelile anda võimalus kutsuda funktsioone, muudate temast tekstigeneraatori asemel agendi, kes saab tegutseda. Mudel otsustab, millal tal tööriista vaja on, millist tööriista kasutada ja milliseid parameetreid edasi anda. Teie kood täidab funktsiooni ja tagastab tulemuse. Mudel integreerib selle tulemuse oma vastusesse.

## Eeldused

- On läbitud [Moodul 01 - Sissejuhatus](../01-introduction/README.md) (Azure OpenAI ressursid paigaldatud)
- Soovitatavalt on läbitud varasemad moodulid (see moodul viitab [RAG kontseptsioonidele Moodulis 03](../03-rag/README.md) tööriistade ja RAG võrdluses)
- Juurkausta `.env` fail Azure volitustega (loodud `azd up` käsuga Moodul 01 jooksul)

> **Märkus:** Kui te pole Moodulit 01 läbinud, järgige esmalt seal olevaid juurutusjuhiseid.

## Tehisintellekti agentide mõistmine tööriistadega

> **📝 Märkus:** Selles moodulis tähendab "agentide" all tehisintellektipõhiseid abistajaid, kellel on tööriistakutsete võimekus. See erineb **Agentic AI** mustritest (iseseisvad agendid planeerimise, mälu ja mitmeastmelise mõtlemisega), mida käsitleme [Moodulis 05: MCP](../05-mcp/README.md).

Ilma tööriistadeta suudab keelemudel genereerida teksti ainult oma treeningandmete põhjal. Küsi temalt ilmateadet ja ta peab arvama. Kui anda tööriistad, saab ta kutsuda ilma API-t, teha arvutusi või pärida andmebaasi — ning neid tegelikke tulemusi oma vastusesse kanda.

<img src="../../../translated_images/et/what-are-tools.724e468fc4de64da.webp" alt="Ilma tööriistadeta vs tööriistadega" width="800"/>

*Ilma tööriistadeta mudel ainult arvab — tööriistadega saab ta kutsuda API-sid, teha arvutusi ja tagastada reaalajas andmeid.*

Tehisintellekti agent tööriistadega järgib **Reasoning and Acting (ReAct)** mustrit. Mudel ei vastagi lihtsalt — ta mõtleb, mida vajab, kutsub tööriista, jälgib tulemust ning otsustab siis, kas tegutseda edasi või anda lõplik vastus:

1. **Põhjenda** — Agent analüüsib kasutaja küsimust ja tuvastab vajaliku info
2. **Tegu** — Agent valib õige tööriista, genereerib korrektseid parameetreid ja kutsub selle
3. **Jälgi** — Agent saab tööriista väljundi ja hindab tulemust
4. **Korda või vasta** — Kui vaja on rohkem andmeid, kordab agent tsüklit; vastasel juhul koostab loomuliku keele vastuse

<img src="../../../translated_images/et/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct muster" width="800"/>

*ReAct tsükkel — agent põhjendab, mida teha, kutsub tööriista, jälgib tulemust ja kordab, kuni suudab anda lõpliku vastuse.*

See toimub automaatselt. Te ise määratlete tööriistad ja nende kirjeldused. Mudel teeb otsuse, millal ja kuidas neid kasutada.

## Kuidas tööriistakutse töötab

### Tööriistade definitsioonid

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Te määratlete funktsioonid selgete kirjelduste ja parameetrispetsifikatsioonidega. Mudel näeb neid kirjeldusi oma süsteemi promptis ja mõistab, mida iga tööriist teeb.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Teie ilmaotsingu loogika
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent on Spring Booti poolt automaatselt ühendatud järgmistega:
// - ChatModel komponent
// - Kõik @Tool meetodid @Component klassidest
// - ChatMemoryProvider sessioonihalduseks
```

Järgmine diagramm lahtiseletab iga annotatsiooni ja näitab, kuidas iga osa aitab tehisintellektil mõista, millal tööriista kutsuda ja milliseid argumente edasi anda:

<img src="../../../translated_images/et/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Tööriista definitsiooni anatoomia" width="800"/>

*Tööriista definitsiooni anatoomia — @Tool ütleb AI-le, millal seda kasutada, @P kirjeldab iga parameetrit ning @AiService ühendab kõik käivitamisel.*

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ja küsi:
> - "Kuidas integreerida päris ilma API nagu OpenWeatherMap, mitte tehisandmeid?"
> - "Mis on hea tööriistakirjelduse omadused, mis aitavad AI-l seda korrektselt kasutada?"
> - "Kuidas käidelda API-vigu ja piiranguid tööriistade rakendamisel?"

### Otsuste tegemine

Kui kasutaja küsib: "Milline on ilm Seattle'is?", ei vali mudel tööriista juhuslikult. Ta võrdleb kasutaja kavatsust iga tööriista kirjeldusega, hindab iga relevantsust ja valib parima vaste. Ta genereerib struktureeritud funktsioonikutse õigetel parameetritel — antud juhul seatakse `location` väärtuseks `"Seattle"`.

Kui ükski tööriist ei sobi kasutaja päringuga, vastab mudel oma teadmiste põhjal. Kui sobivaid tööriistu on mitu, valib kõige spetsiifilisema.

<img src="../../../translated_images/et/decision-making.409cd562e5cecc49.webp" alt="Kuidas AI otsustab, millist tööriista kasutada" width="800"/>

*Mudel hindab iga saadavaloleva tööriista kasutaja kavatsuse vastu ja valib parima sobivuse — seepärast on oluline kirjutada selge ja konkreetne tööriistakirjeldus.*

### Täideviimine

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot ühendab automaatselt deklaratiivse `@AiService` liidese kõigi registreeritud tööriistadega ning LangChain4j täidab tööriistakutsed automaatselt. Tagatipuks voolab komplektne tööriistakutse kuue etapi kaudu — kasutaja loomulikust keeles küsimusest tagasi loomuliku keele vastuseni:

<img src="../../../translated_images/et/tool-calling-flow.8601941b0ca041e6.webp" alt="Tööriistakutse voog" width="800"/>

*Täielik voog — kasutaja esitab küsimuse, mudel valib tööriista, LangChain4j täidab selle ja mudel lõimib tulemuse loomulikuks vastuseks.*

Kui käivitasite [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) Moodulis 00, nägite seda mustrit töös — `Calculator` tööriistad kutsuti täpselt samamoodi. Järgmine järjestusdiagramm näitab täpselt, mis sel ajal toimus:

<img src="../../../translated_images/et/tool-calling-sequence.94802f406ca26278.webp" alt="Tööriistakutsede järjestusdiagramm" width="800"/>

*Tööriistakutse tsükkel Quick Start demos — `AiServices` saadab teie sõnumi ja tööriistade skeemid LLM-ile, LLM vastab funktsioonikutsega nagu `add(42, 58)`, LangChain4j täidab kohapeal `Calculator` meetodi ja tagastab tulemuse lõplikuks vastuseks.*

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ja küsi:
> - "Kuidas ReAct muster toimib ja miks on see tehisintellekti agentidele efektiivne?"
> - "Kuidas agent otsustab, millist tööriista kasutada ja millises järjekorras?"
> - "Mis juhtub, kui tööriista täideviimine ebaõnnestub - kuidas vigadega kindlalt toime tulla?"

### Vastuse genereerimine

Mudel saab ilmateabe ja vormindab selle kasutajale loomulikus keeles vastuseks.

### Arhitektuur: Spring Boot automaatühendus

See moodul kasutab LangChain4j Spring Boot integreerimist deklaratiivsete `@AiService` liidestega. Käivitamisel leiab Spring Boot kõik `@Component`-id, mis sisaldavad `@Tool` meetodeid, teie `ChatModel` beani ja `ChatMemoryProvider` — ning ühendab need kõik ühte `Assistant` liidesesse ilma täiendava koodita.

<img src="../../../translated_images/et/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automaatühenduse arhitektuur" width="800"/>

*@AiService liides ühendab ChatModeli, tööriista komponendid ja mälupakkuja — Spring Boot haldab automaatselt kogu ühenduse.*

Järgnevalt on kogu päringu elutsükkel järjestusdiagrammina — HTTP päringust kontrolleri, teenuse ja automaatselt ühendatud proksi kaudu tööriistakutse täideviimiseni ja tagasi:

<img src="../../../translated_images/et/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot tööriistakutsete järjestus" width="800"/>

*Täielik Spring Boot päringu elutsükkel — HTTP päring voolab läbi kontrolleri ja teenuse automaatselt ühendatud Assistant proksisse, kes orkestreerib LLM-i ja tööriistakutsed iseseisvalt.*

Selle lähenemise peamised eelised:

- **Spring Boot automaatühendus** — ChatModel ja tööriistad süstitakse automaatselt
- **@MemoryId muster** — Automaatne seansipõhine mälu haldus
- **Üks instants** — Assistant luuakse korra ja kasutatakse uuesti parema jõudluse jaoks
- **Tüübiturvaline täideviimine** — Java meetodid kutsutakse otse tüübi teisendusega
- **Mitme sammuga orkestreerimine** — Haldbab tööriistade aheldamist automaatselt
- **Null täiendavat koodi** — Ei ole vaja käsitsi kutsuda `AiServices.builder()` või mälu HashMapi

Alternatiivsed lähenemised (käsitsi `AiServices.builder()`) vajavad rohkem koodi ja jäävad ilma Spring Boot integratsiooni eelistest.

## Tööriistade aheldamine

**Tööriistade aheldamine** — Tööriistapõhiste agentide tõeline võimsus tuleb ilmsiks siis, kui üks küsimus vajab mitut tööriista. Küsi: "Milline on Seattle'i ilm Fahrenheitides?" ja agent aheldab automaatselt kaks tööriista: esmalt kutsub `getCurrentWeather`, et saada temperatuur Celsiuses, seejärel edastab selle väärtuse `celsiusToFahrenheit` konverteerimiseks — kõik ühe vestluse jooksul.

<img src="../../../translated_images/et/tool-chaining-example.538203e73d09dd82.webp" alt="Tööriistade aheldamise näide" width="800"/>

*Tööriistade aheldamine töös — agent kutsub esmalt getCurrentWeather, seejärel suunab Celsiuse tulemuse celsiusToFahrenheit-i ja annab kokkuvõtva vastuse.*

**Viisakas vigade käsitlemine** — Küsi ilma linna kohta, mis ei ole tehisandmetes. Tööriist tagastab veateate ning tehisintellekt seletab, et ei saa aidata, selle asemel et kokku jooksu teha. Tööriistad ebaõnnestuvad turvaliselt. Järgmine diagramm võrdleb kahte lähenemist — korraliku vigade käitlemisega agent tabab erandi ja vastab abivalmilt, ilma selleta kukub kogu rakendus kokku:

<img src="../../../translated_images/et/error-handling-flow.9a330ffc8ee0475c.webp" alt="Vigade käsitlemise voog" width="800"/>

*Kui tööriist ebaõnnestub, tabab agent vea ja vastab abivalmilt, selle asemel et rakendus kokku jookseks.*

See juhtub ühe vestlusvooru jooksul. Agent orkestreerib automaatselt mitut tööriistakutset.

## Rakenduse käivitamine

**Kontrollige juurutust:**

Veenduge, et juurkaustas on olemas `.env` fail Azure volitustega (loodud Moodulis 01). Käivitage see mooduli kaustast (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Rakenduse käivitamine:**

> **Märkus:** Kui olete juba käivitanud kõik rakendused käsuga `./start-all.sh` juurkaustast (nagu kirjeldatud Moodulis 01), töötab see moodul porti 8084. Võite käivituskäsklused vahele jätta ja minna otse aadressile http://localhost:8084.

**Variant 1: Spring Boot Dashboardi kasutamine (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiate selle VS Code’i vasakpoolse tööriistariba ikoonilt, otsides Spring Boot ikooni.

Spring Boot Dashboardi kaudu saate:
- Näha kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klõpsuga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduse olekut
Lihtsalt klõpsake selle mooduli käivitamiseks nupu "tools" kõrval olevat esitamisnuppu või käivitage kõik moodulid korraga.

Siin on, kuidas Spring Boot Dashboard VS Code'is välja näeb:

<img src="../../../translated_images/et/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code'is — käivitage, peatage ja jälgige kõiki mooduleid ühest kohast*

**Valik 2: Shell-skriptide kasutamine**

Käivitage kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurestikust
.\start-all.ps1
```

Või käivitage ainult see moodul:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkataloogis asuvast `.env` failist ning ehitavad JAR-id, kui neid ei eksisteeri.

> **Märkus:** Kui soovite enne käivitamist kõik moodulid käsitsi ehitada:
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

Avage oma brauseris aadress <http://localhost:8084>.

**Peatamiseks:**

**Bash:**
```bash
./stop.sh  # Ainult see moodul
# Või
cd .. && ./stop-all.sh  # Kõik moodulid
```

**PowerShell:**
```powershell
.\stop.ps1  # Ainult see moodul
# Või
cd ..; .\stop-all.ps1  # Kõik moodulid
```

## Rakenduse kasutamine

Rakendus pakub veebiliidest, kus saate suhelda AI agendiga, kellel on juurdepääs ilma- ja temperatuuri teisendamise tööriistadele. Siin on liides — sisaldab kiire algusega näiteid ja vestluspaneeli päringute saatmiseks:

<a href="images/tools-homepage.png"><img src="../../../translated_images/et/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools liides – kiireid näiteid ja vestlusliides tööriistadega suhtlemiseks*

### Proovi lihtsat tööriista kasutust

Alusta lihtsast päringust: "Convert 100 degrees Fahrenheit to Celsius". Agent tuvastab, et tal on vaja temperatuuri teisendamise tööriista, kutsub selle õige parameetritega ja tagastab tulemuse. Märka, kui loomulik see on – sa ei pidanud täpsustama, millist tööriista kasutada või kuidas seda kutsuda.

### Testi tööriistade ahelat

Proovi nüüd keerulisemat: "What's the weather in Seattle and convert it to Fahrenheit?" Vaata, kuidas agent samm-sammult töötab. Esiteks hangib ta ilma (mis tagastab Celsius), tunneb ära vajaduse teisendada Fahrenheitiks, kutsub teisendusvahendi ja ühendab mõlemad tulemused ühe vastusena.

### Vaata vestluse kulgu

Vestlusliides säilitab vestluse ajaloo, võimaldades mitmevoorulist suhtlust. Näed kõiki varasemaid päringuid ja vastuseid, mis hõlbustab vestluse jälgimist ja mõistmist, kuidas agent ehitab konteksti mitme vahetuse jooksul.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/et/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mitmevooruline vestlus, mis näitab lihtsaid teisendusi, ilmapäringuid ja tööriistade ahelat*

### Katseta erinevaid päringuid

Proovi erinevaid kombinatsioone:
- Ilmapäringud: "What's the weather in Tokyo?"
- Temperatuuri teisendused: "What is 25°C in Kelvin?"
- Ühispäringud: "Check the weather in Paris and tell me if it's above 20°C"

Märka, kuidas agent tõlgendab loomulikku keelt ja kaardistab selle sobivateks tööriistakutseteks.

## Põhimõisted

### ReAct muster (Põhjendamine ja Tegutsemine)

Agent vaheldub põhjendamise (otsustamine, mida teha) ja tegutsemise (tööriistade kasutamine) vahel. See muster võimaldab autonoomset probleemilahendust, mitte ainult juhiste täitmist.

### Tööriistade kirjeldusel on tähtsus

Tööriistakirjelduse kvaliteet mõjutab otseselt, kui hästi agent neid kasutab. Selged, spetsiifilised kirjeldused aitavad mudelil mõista, millal ja kuidas iga tööriista kutsuda.

### Sessioonihaldus

`@MemoryId` annotatsioon võimaldab automaatset sessioonipõhist mäluhaldust. Iga sessiooni ID jaoks luuakse oma `ChatMemory` instants, mida haldab `ChatMemoryProvider` bean, nii et mitmed kasutajad saavad korraga agendiga suhelda ilma, et nende vestlused seguneksid. Järgnev diagramm näitab, kuidas mitu kasutajat suunatakse eraldatud mälupoodidesse vastavalt nende sessiooni ID-dele:

<img src="../../../translated_images/et/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Iga sessiooni ID vastab eraldatud vestluse ajalool — kasutajad ei näe kunagi teiste sõnumeid.*

### Vea käsitlemine

Tööriistad võivad ebaõnnestuda — API-d aeguvad, parameetrid võivad olla vigased, välisteenused ei tööta. Tootmisagentidele on vajalik veahaldus, et mudel suudaks probleemi selgitada või alternatiive proovida, mitte et terve rakendus kokku jookseks. Kui tööriist viskab erindi, püüab LangChain4j selle kinni ja edastab veateate mudelile, mis võib seejärel probleemi loomulikus keeles selgitada.

## Saadavalolevad tööriistad

Järgmine diagramm näitab laia tööriistade ökosüsteemi, mida saate ehitada. See moodul demonstreerib ilma- ja temperatuuri tööriistu, kuid sama `@Tool` muster toimib mistahes Java meetodi puhul — alates andmebaasipäringutest kuni maksete töötlemiseni.

<img src="../../../translated_images/et/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Mitte ükski Java meetod, mida annotatsiooniga @Tool märgistatakse, ei muutuks saadavaks AI-le — mustrit saab laiendada andmebaaside, API-de, e-posti, failitöötluse ja muu puhul.*

## Millal kasutada tööriistapõhiseid agente

Mitte iga päring ei vaja tööriistu. Otsus sõltub sellest, kas AI-l on vaja suhelda välissüsteemidega või saab vastata oma teadmiste põhjal. Järgmine juhend võtab kokku, millal tööriistad väärtust lisavad ja millal neid pole vaja:

<img src="../../../translated_images/et/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Kiire otsustusjuhend — tööriistad on mõeldud reaalajas andmete, arvutuste ja toimingute jaoks; üldteadmised ja loomingulised ülesanded neid ei vaja.*

## Tööriistad vs RAG

Moodulid 03 ja 04 laiendavad mõlemad, mida AI suudab teha, kuid põhjalikult erineval moel. RAG annab mudelile juurdepääsu **teadmistele** dokumentide abil. Tööriistad annavad mudelile võime **toimingute** tegemiseks funktsioone kutsuda. Järgnev diagramm võrdleb neid kahte lähenemist kõrvuti — alates tööprotsesside toimimisest kuni kompromissideni:

<img src="../../../translated_images/et/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG hangib infot staatilistest dokumentidest — tööriistad täidavad toiminguid ja hangivad dünaamilisi, reaalajas andmeid. Paljud tootmissüsteemid kombineerivad mõlemat.*

Praktikas kasutavad paljud tootmissüsteemid mõlemat lähenemist: RAG kinnitab vastused teie dokumentatsioonile ja tööriistad hangivad elusaid andmeid või teevad toiminguid.

## Järgmised sammud

**Järgmine moodul:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 03 - RAG](../03-rag/README.md) | [Tagasi Pealehele](../README.md) | [Järgmine: Moodul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud AI tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame tagada täpsust, palun arvestage, et automaatses tõlkes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->