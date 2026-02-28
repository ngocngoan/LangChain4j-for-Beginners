# Moodul 04: AI agendid tööriistadega

## Sisukord

- [Mida sa õpid](../../../04-tools)
- [Eeltingimused](../../../04-tools)
- [Arusaamine AI agentidest tööriistadega](../../../04-tools)
- [Kuidas tööriistade kutsumine töötab](../../../04-tools)
  - [Tööriista määratlused](../../../04-tools)
  - [Otsuste tegemine](../../../04-tools)
  - [Teostus](../../../04-tools)
  - [Vastuse genereerimine](../../../04-tools)
  - [Arhitektuur: Spring Boot automaatühendus](../../../04-tools)
- [Tööriistade ketistamine](../../../04-tools)
- [Rakenduse käivitamine](../../../04-tools)
- [Rakenduse kasutamine](../../../04-tools)
  - [Proovi lihtsat tööriistakasutust](../../../04-tools)
  - [Testi tööriistade ketistamist](../../../04-tools)
  - [Vaata vestluse kulgu](../../../04-tools)
  - [Katseta erinevate päringutega](../../../04-tools)
- [Põhikontseptsioonid](../../../04-tools)
  - [ReAct mustril (Põhjendamine ja Tegutsemine)](../../../04-tools)
  - [Tööriista kirjeldused on olulised](../../../04-tools)
  - [Seansi haldamine](../../../04-tools)
  - [Vigade käsitlemine](../../../04-tools)
- [Saadaval tööriistad](../../../04-tools)
- [Millal kasutada tööriistapõhiseid agente](../../../04-tools)
- [Tööriistad vs RAG](../../../04-tools)
- [Järgmised sammud](../../../04-tools)

## Mida sa õpid

Seniks oled õppinud, kuidas suhelda tehisintellektiga, kuidas eelseadeid efektiivselt struktureerida ja kuidas vastuseid oma dokumentide põhjal tuvastada. Kuid on üks põhiline piirang: keelemudelid suudavad ainult teksti genereerida. Nad ei saa ilma lisatööriistadeta ilmaennustust kontrollida, arvutusi teha, andmebaase pärida ega välissüsteemidega suhelda.

Tööriistad muudavad selle. Mudelile funktsioonide kasutamise võimaldamisega muudad ta tekstigeneraatorist agentiks, kes saab tegutseda. Mudel otsustab, millal tal tööriista vaja on, millist kasutada ja milliseid parameetreid saata. Sinu kood käivitab funktsiooni ja tagastab tulemuse. Mudel kasutab seda tulemust oma vastuses.

## Eeltingimused

- Moodul 01 läbimine (Azure OpenAI ressursid deployitud)
- Juurekataloogis `.env` fail Azure autentimisandmetega (loodud `azd up`-ga Moodulis 01)

> **Märkus:** Kui sa pole Moodulit 01 veel lõpetanud, järgi eelnevalt sealset paigaldusjuhendit.

## Arusaamine AI agentidest tööriistadega

> **📝 Märkus:** Selles moodulis viitab "agendid" termin AI assistentidele, kellel on tööriistade kasutamise võimekus. See erineb **Agentic AI** mustritest (autonoomsed agendid plaanimise, mäluga ja mitmeastmelise põhjendamisega), mida käsitleme [Moodulis 05: MCP](../05-mcp/README.md).

Ilma tööriistadeta saab keelemudel vaid oma koolitusandmete põhjal teksti genereerida. Kui küsid praeguse ilma kohta, tuleb tal seda oletada. Kui annad talle tööriistad, saab ta kutsuda ilma API-d, teha arvutusi või pärida andmebaasi — ja uuritud tulemused manustatakse vastusesse.

<img src="../../../translated_images/et/what-are-tools.724e468fc4de64da.webp" alt="Ilma tööriistadeta vs tööriistadega" width="800"/>

*Ilma tööriistadeta model võib vaid oletada — tööriistadega saab kutsuda API-sid, teha arvutusi ning tagastada reaalajas andmeid.*

Tööriistadega varustatud AI agent järgib **Põhjendamise ja Tegutsemise (ReAct)** mustrit. Mudel ei vasta lihtsalt — ta mõtleb, mida tal vaja on, teostab tööriista kutsumise, vaatab tulemust ja otsustab, kas tegutseda uuesti või anda lõplik vastus:

1. **Põhjenda** — Agent analüüsib kasutaja küsimust ja otsustab, mis informatsiooni tal vaja on
2. **Tegi** — Agent valib õige tööriista, genereerib õiged argumendid ja kutsub selle välja
3. **Tähele pane** — Agent saab tööriista väljundi ning hindab tulemust
4. **Korda või vasta** — Vajadusel kordab agent, muidu koostab vastuse loomulikus keeles

<img src="../../../translated_images/et/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Muster" width="800"/>

*ReAct tsükkel — agent põhjendab, mida teha, tegutseb tööriista kutsumisega, jälgib tulemust ja kordab, kuni saab lõpliku vastuse.*

See protsess käib automaatselt. Sa määratled tööriistad ja nende kirjeldused. Mudel haldab ise otsust, millal ja kuidas neid kasutada.

## Kuidas tööriistade kutsumine töötab

### Tööriista määratlused

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Sa defineerid funktsioonid selgete kirjelduste ja parameetrite spetsiifikatsioonidega. Mudel näeb neid kirjelduseid süsteemipromptis ja mõistab, mida iga tööriist teeb.

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

// Assistent on Spring Booti poolt automaatselt ühendatud:
// - ChatModel bean
// - Kõik @Tool meetodid @Component klassidest
// - ChatMemoryProvider sessiooni haldamiseks
```

Järgmises diagrammis on iga annotatsioon lahti võetud ja näidatud, kuidas iga tükk aitab AI-l aru saada, millal tööriista kutsuda ja milliseid argumente anda:

<img src="../../../translated_images/et/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Tööriista määratluse anatoomia" width="800"/>

*Tööriista definitsiooni anatoomia — @Tool ütleb AI-le, millal kasutada, @P kirjeldab iga parameetrit ja @AiService ühendab kõik käivitamisel.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ja küsi:
> - "Kuidas integreerida päris ilma API nagu OpenWeatherMap kas nii, et mock-andmeid ei kasutaks?"
> - "Mis teeb tööriista kirjelduse heaks ja aitab AI-l seda õigesti kasutada?"
> - "Kuidas käidelda API vigu ja kasutuslimiite tööriistade rakendustes?"

### Otsuste tegemine

Kui kasutaja küsib "Kuidas on ilm Seattle's?", ei vali mudel juhuslikult tööriista. Ta võrdleb kasutaja kavatsust iga olemasoleva tööriista kirjeldusega, hinnangustab neid sobivuse suhtes ja valib parima vastet. Seejärel genereerib struktuurse funktsioonikutsu õige parameetritega — antud juhul seades `location` väärtuseks `"Seattle"`.

Kui ükski tööriist ei sobi, vastab mudel omaenda teadmiste põhjal. Kui sobivaid tööriistu on mitu, valib kõige spetsiifilisema.

<img src="../../../translated_images/et/decision-making.409cd562e5cecc49.webp" alt="Kuidas AI otsustab, millist tööriista kasutada" width="800"/>

*Mudel hindab kõiki saadavalolevaid tööriistu vastavalt kasutaja kavatsusele ja valib parima — seetõttu on tähtis kirjutada selged ja spetsiifilised tööriistade kirjelduse.*

### Teostus

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot ühendab automaatselt deklaratiivse `@AiService` liidese kõigi registreeritud tööriistadega ja LangChain4j teostab tööriista kutsed automaatselt. Tagatipuks kulgeb tööriista kutse kuue etapi kaudu — kasutajast loomulikus keeles küsimusest kuni loomuliku keele vastuseni:

<img src="../../../translated_images/et/tool-calling-flow.8601941b0ca041e6.webp" alt="Tööriista kutsumise voog" width="800"/>

*Lõpp-lõpuni voog — kasutaja esitab küsimuse, mudel valib tööriista, LangChain4j teeb käivituse ja mudel lõimib tulemuse loomulikusse vastusesse.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ja küsi:
> - "Kuidas töötab ReAct muster ja miks see on AI agentide jaoks tõhus?"
> - "Kuidas agent otsustab, millist tööriista kasutada ja millises järjekorras?"
> - "Mis juhtub, kui tööriista teostamine ebaõnnestub - kuidas vigasid korrektselt hallata?"

### Vastuse genereerimine

Mudel saab ilmaandmed ja vormindab need kasutajale loomulikus keeles vastuseks.

### Arhitektuur: Spring Boot automaatühendus

See moodul kasutab LangChain4j Spring Boot integratsiooni deklaratiivsete `@AiService` liidestega. Käivitamisel avastab Spring Boot kõik `@Component` annotatsiooniga klassid, mis sisaldavad `@Tool` meetodeid, sinu `ChatModel` komponendi ja `ChatMemoryProvider` — ning ühendab need üheks `Assistant` liideseks ilma igasuguse käsitsi seadistamiseta.

<img src="../../../translated_images/et/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automaatühenduse arhitektuur" width="800"/>

*@AiService liides ühendab ChatModeli, tööriistakomponendid ja mäluteenuse — Spring Boot haldab kogu ühenduse automaatselt.*

Selle lähenemise peamised eelised:

- **Spring Boot automaatühendus** — ChatModel ja tööriistad manustatakse automaatselt
- **@MemoryId muster** — Automaatne sessioonipõhine mälu haldamine
- **Üks eksemplar** — Assistant luuakse ühe korra ja seda taaskasutatakse parema jõudluse jaoks
- **Tüübiturvaline käivitamine** — Java meetodeid kutsutakse otse tüübikonversioonidega
- **Mitme vooru orkestreerimine** — Haldb tööriistade ketistamist automaatselt
- **Null boilerplate'i** — Pole vaja käsitsi `AiServices.builder()` kutsuda ega mälu HashMapi haldada

Alternatiivsed lähenemised (käsitsi `AiServices.builder()`) nõuavad rohkem koodi ja jätavad Spring Boot integreerimise eelised kasutamata.

## Tööriistade ketistamine

**Tööriistade ketistamine** — Tööriistapõhiste agentide tõeline jõud tuleb välja, kui üks küsimus vajab mitut tööriista. Küsi "Kuidas on ilm Seattle's Fahrenheitis?" ja agent ühendab automaatselt kaks tööriista: esmalt kutsub `getCurrentWeather` temperatuuri saamiseks Celsiuse kraadides, seejärel annab selle väärtuse edasi `celsiusToFahrenheit` teisenduseks — kõik ühe vestlusvooru jooksul.

<img src="../../../translated_images/et/tool-chaining-example.538203e73d09dd82.webp" alt="Tööriistade ketistamise näide" width="800"/>

*Tööriistade ketistamine praktikas — agent kutsub esmalt getCurrentWeather, siis edastab Celsius tulemuse celsiusToFahrenheit'ile ja annab ühendatud vastuse.*

Näiteks näeb see rakenduses välja nii — agent ketistab kaks tööriistakutset ühe vestlusvooru jooksul:

<a href="images/tool-chaining.png"><img src="../../../translated_images/et/tool-chaining.3b25af01967d6f7b.webp" alt="Tööriistade ketistamine" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tegeliku rakenduse väljund — agent ketistab automaatselt getCurrentWeather → celsiusToFahrenheit ühes voorus.*

**Ilmastustatus veaga** — Küsi ilma kohta linnas, mis ei ole mock-andmete seas. Tööriist tagastab veateate ja tehisintellekt selgitab, et ta ei saa aidata, selle asemel, et käivituda. Tööriistad tagavad vigade ohutu käsitlemise.

<img src="../../../translated_images/et/error-handling-flow.9a330ffc8ee0475c.webp" alt="Veakäsitluse voog" width="800"/>

*Kui tööriist ebaõnnestub, haarab agent vea ja vastab abistava selgitusega, selle asemel et kukkuda.*

See toimub ühes vestlusvoorus. Agent korraldab mitmekordseid tööriistakutseid iseseisvalt.

## Rakenduse käivitamine

**Kontrolli paigaldust:**

Veendu, et `.env` fail on juurekataloogis koos Azure autentimisandmetega (loodud Moodulis 01):
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käivita rakendus:**

> **Märkus:** Kui oled juba kõik rakendused käivitanud käsuga `./start-all.sh` Moodulist 01, siis see moodul töötab juba pordil 8084. Võid käivitamiskäsud vahele jätta ja minna otse aadressile http://localhost:8084.

**Variant 1: Spring Boot Dashboardi kasutamine (Soovitatav VS Code kasutajatele)**

Arenduskonteineris on Spring Boot Dashboard laiendus, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code vasakul servas asuva tegevusriba ikoonist (otsige Spring Boot ikooni).

Spring Boot Dashboardist saad:
- Vaadata kõiki tööruumis olevaid Spring Boot rakendusi
- Alustada/peatada rakendusi ühe klikiga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse seisundit

Lihtsalt klõpsa "tools" kõrval olevat käivitamisnuppu selle mooduli käivitamiseks või alusta kõiki mooduleid korraga.

<img src="../../../translated_images/et/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Variant 2: Shell-skriptide kasutamine**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juure kaustast
.\start-all.ps1
```

Või käivita ainult see moodul:

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

Mõlemad skriptid loevad automaatselt keskkonnamuutujad juurekataloogis olevast `.env` failist ja ehitavad JAR-id, kui need puuduvad.

> **Märkus:** Kui soovid moodulid käsitsi enne käivitamist koostada:
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

Ava oma brauseris aadress http://localhost:8084.

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

Rakendus pakub veebiliidest, mille kaudu saad suhelda AI agendiga, kellel on juurdepääs ilma ja temperatuuri teisendamise tööriistadele.

<a href="images/tools-homepage.png"><img src="../../../translated_images/et/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tööriistade liides" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agendi tööriistade liides – kiired näited ja vestlusliides tööriistade kasutamiseks*

### Proovi lihtsat tööriistakasutust
Alusta lihtsast päringust: "Muuda 100 kraadi Fahrenheiti Celsiuseks". Agent tuvastab, et tal on vaja temperatuuri konverteerimise tööriista, kutsub seda õigete parameetritega ja tagastab tulemuse. Pane tähele, kui loomulik see on – sa ei määranud, millist tööriista kasutada ega kuidas seda kutsuda.

### Tööriistade ahelate testimine

Proovi nüüd midagi keerulisemat: "Milline on ilm Seattle's ja muuda see Fahrenheiti kraadideks?" Vaata, kuidas agent selle sammhaaval lahendab. Kõigepealt hangib ta ilma (mis tagastab Celsiuse), tuvastab, et peab Fahrenheiti teisendama, kutsub konverteerimise tööriista ning ühendab mõlemad tulemused üheks vastuseks.

### Vaata vestluse kulgu

Vestlusliides säilitab vestlusajaloo, võimaldades sul teha mitme sammuga päringuid. Sa näed kõiki varasemaid küsimusi ja vastuseid, mis teeb konteksti jälgimise ning agenti mitme vahetusega tööle hakkamise lihtsaks.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/et/tools-conversation-demo.89f2ce9676080f59.webp" alt="Mitme tööriista kutsetega vestlus" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mitme sammuga vestlus, kus näidatakse lihtsaid teisendusi, ilma päringuid ja tööriistade ahelat*

### Katseta erinevate päringutega

Proovi mitmesuguseid kombinatsioone:
- Ilma päringud: "Milline on ilm Tokyos?"
- Temperatuuri teisendused: "Mis on 25°C kelvinites?"
- Koostatud päringud: "Vaata Pariisi ilma ja ütle, kas temperatuur on üle 20°C"

Pane tähele, kuidas agent tõlgendab loomulikku keelt ja seob selle sobivate tööriistakutsetega.

## Olulised mõisted

### ReAct muster (Põhjendamine ja tegutsemine)

Agent vaheldub põhjendamise (otsustab, mida teha) ja tegutsemise (kasutab tööriistu) vahel. See muster võimaldab autonoomset probleemilahendust, mitte lihtsalt käskude täitmist.

### Tööriistade kirjeldused on olulised

Tööriistade kirjelduste kvaliteet mõjutab otseselt, kui hästi agent neid kasutab. Selged ja konkreetsed kirjeldused aitavad mudelil mõista, millal ja kuidas tööriista kutsuda.

### Sessioonihaldus

Annetatsioon `@MemoryId` võimaldab automaatset sessiisipõhist mäluhaldust. Iga sessiooni ID saab oma `ChatMemory` eksemplari, mida haldab `ChatMemoryProvider` bean, võimaldades mitmel kasutajal agenti samaaegselt kasutada ilma nende vestluste segunemiseta.

<img src="../../../translated_images/et/session-management.91ad819c6c89c400.webp" alt="Sessioonihaldus koos @MemoryId-ga" width="800"/>

*Iga sessiooni ID on seotud eraldi vestlusajalooga — kasutajad ei näe kunagi teineteise sõnumeid.*

### Veahaldus

Tööriistad võivad ebaõnnestuda — API-d aeguvad, parameetrid võivad olla vigased, välisteenused võivad lakkuda töötamast. Tootmiseks mõeldud agentidel tuleb olla veahaldus, et mudel suudaks probleeme selgitada või proovida alternatiive, mitte et kogu rakendus kokku jooksuks. Kui tööriist viskab erandi, püüab LangChain4j selle kinni ja edastab veateate mudelile, mis seejärel suudab probleemi loomulikus keeles selgitada.

## Saadaval olevad tööriistad

Joonisel allpool on kujutatud lai tööriistade ökosüsteem, mida saad luua. See moodul demonstreerib ilma ja temperatuuri tööriistu, kuid sama `@Tool` muster kehtib mis tahes Java meetodi kohta – olgu need andmebaasi päringud või maksete töötlemine.

<img src="../../../translated_images/et/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tööriistade ökosüsteem" width="800"/>

*Mis tahes Java meetod, millel on annotatsioon @Tool, muutub AI jaoks kättesaadavaks — muster laieneb andmebaasidele, API-dele, meilile, failitoimingutele jpm.*

## Millal kasutada tööriistadel põhinevaid agente

<img src="../../../translated_images/et/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Millal kasutada tööriistu" width="800"/>

*Kiire otsustamisjuhis — tööriistad on mõeldud reaalajas andmete, arvutuste ja tegevuste jaoks; üldteadmiste ja loovate ülesannete jaoks neid pole vaja.*

**Kasuta tööriistu, kui:**
- Vastuseks on vaja reaalajas andmeid (ilm, aktsiahinnad, laoseis)
- Pead tegema arvutusi, mis on lihtsamast matemaatikast keerukamad
- Tuleb pääseda ligi andmebaasidele või API-dele
- Tuleb sooritada tegevusi (saata e-kirju, luua pileteid, uuendada kirjeid)
- Tuleb kombineerida mitut andmeallikat

**Ära kasuta tööriistu, kui:**
- Küsimustele saab vastata üldteadmiste põhjal
- Vastus on puhtalt vestluslik
- Tööriistade latentsus muudaks kogemuse liiga aeglaseks

## Tööriistad vs RAG

Moodulid 03 ja 04 laiendavad AI võimekust, kuid fundamentaalselt erinevalt. RAG annab mudelile juurdepääsu **teadmistele** dokumentide pärimise kaudu. Tööriistad annavad mudelile võime **tegutseda** funktsioonikõnede abil.

<img src="../../../translated_images/et/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tööriistad vs RAG võrdlus" width="800"/>

*RAG hangib infot staatilistest dokumentidest — tööriistad käivitavad tegevusi ja toovad dünaamilisi, reaalajas andmeid. Paljud tootmissüsteemid kasutavad mõlemaid.*

Praktikas kombineerivad paljud tootmissüsteemid mõlemat lähenemist: RAG vastuste kinnitamiseks sinu dokumentatsioonis ja tööriistad elava andmete toomiseks või toimingute tegemiseks.

## Järgmised sammud

**Järgmine moodul:** [05-mcp - Mudelikonteksti protokoll (MCP)](../05-mcp/README.md)

---

**Navigatsioon:** [← Eelmine: Moodul 03 - RAG](../03-rag/README.md) | [Tagasi avalehele](../README.md) | [Järgmine: Moodul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud kasutades AI tõlke teenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me pingutame täpsuse nimel, tuleb arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks lugeda autoriteetseks allikaks. Olulise info puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta tõlke kasutamisest tulenevate arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->