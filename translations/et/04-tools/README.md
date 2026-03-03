# Moodul 04: AI agendid tööriistadega

## Sisukord

- [Mida sa õpid](../../../04-tools)
- [Eeldused](../../../04-tools)
- [AI agentide mõistmine tööriistadega](../../../04-tools)
- [Kuidas tööriistakõned töötavad](../../../04-tools)
  - [Tööriistade määratlused](../../../04-tools)
  - [Otsuste tegemine](../../../04-tools)
  - [Täideviimine](../../../04-tools)
  - [Vastuse genereerimine](../../../04-tools)
  - [Arhitektuur: Spring Boot automaatühendus](../../../04-tools)
- [Tööriistade aheldamine](../../../04-tools)
- [Rakenduse käivitamine](../../../04-tools)
- [Rakenduse kasutamine](../../../04-tools)
  - [Proovi lihtsat tööriistakasutust](../../../04-tools)
  - [Testi tööriistade aheldamist](../../../04-tools)
  - [Vaata vestluse kulgu](../../../04-tools)
  - [Katseta erinevate päringutega](../../../04-tools)
- [Olulised mõisted](../../../04-tools)
  - [ReAct muster (mõtlemine ja tegutsemine)](../../../04-tools)
  - [Tööriista kirjelduse tähtsus](../../../04-tools)
  - [Sessioonihaldus](../../../04-tools)
  - [Vigade käitlemine](../../../04-tools)
- [Saadaval olevad tööriistad](../../../04-tools)
- [Millal kasutada tööriistapõhiseid agente](../../../04-tools)
- [Tööriistad vs RAG](../../../04-tools)
- [Järgmised sammud](../../../04-tools)

## Mida sa õpid

Nüüdseks oled õppinud, kuidas AI-ga vestelda, kuidas tõhusalt korraldada juhiseid ja siduda vastuseid oma dokumentidega. Kuid on üks põhimõtteline piirang: keelemudelid suudavad genereerida ainult teksti. Nad ei saa ilma kontrollida ilma, teha arvutusi, pärida andmebaase ega suhelda väliste süsteemidega.

Tööriistad muudavad selle. Andes mudelile juurdepääsu funktsioonidele, mida ta saab kutsuda, muudad ta tekstigeneraatorist agendiks, kes suudab tegutseda. Mudel otsustab, millal tal tööriista vaja on, millist tööriista kasutada ja millised parameetrid edasi anda. Sinu kood täidab funktsiooni ja tagastab tulemuse. Mudel lisab selle tulemuse oma vastusesse.

## Eeldused

- Läbitud [Moodul 01 - Sissejuhatus](../01-introduction/README.md) (Azure OpenAI ressursid paigaldatud)
- Soovitavalt läbitud varasemad moodulid (see moodul viitab [RAG mõistetele Moodulist 03](../03-rag/README.md) võrdluses Tööriistad vs RAG)
- Juurutuskaustas olemas `.env` fail Azure volitustega (loodud käsklusega `azd up` Moodulis 01)

> **Märkus:** Kui sa pole veel läbinud Moodulit 01, järgi esmalt seal olevaid paigaldusjuhiseid.

## AI agentide mõistmine tööriistadega

> **📝 Märkus:** Selles moodulis tähendab "agentide" mõiste AI assistente, keda on täiustatud tööriistade kutsumise võimalustega. See erineb **Agentic AI** mustritest (autonoomsed agendid planeerimise, mäluga ja mitmeastmelise mõtlemisega), mida käsitleme [Moodulis 05: MCP](../05-mcp/README.md).

Ilma tööriistadeta suudab keelemudel ainult oma treeningandmetest teksti genereerida. Küsi praeguse ilma kohta ja ta peab oletama. Anna talle tööriistad ja ta saab kutsuda ilma API-d, teha arvutusi või pärida andmebaasi — ning seejärel koota need tõelised tulemused oma vastusesse.

<img src="../../../translated_images/et/what-are-tools.724e468fc4de64da.webp" alt="Tööriistadeta vs Tööriistadega" width="800"/>

*Ilma tööriistadeta mudel ainult oletab — tööriistadega saab ta kutsuda API-sid, teha arvutusi ja tagastada reaalajas andmeid.*

AI agent tööriistadega järgib **Mõtlemise ja Tegutsemise (ReAct)** mustrit. Mudel ei vastagi lihtsalt — ta mõtleb, mida tal vaja on, tegutseb tööriista kutsumisega, jälgib tulemust ja otsustab, kas tegutseda uuesti või anda lõplik vastus:

1. **Mõtle** — Agent analüüsib kasutaja küsimust ja määrab, mida ta infot vajab
2. **Tegutse** — Agent valib õige tööriista, genereerib õiged parameetrid ja kutsub selle
3. **Jälgi** — Agent saab tööriista väljundi ja hindab tulemust
4. **Korda või vasta** — Kui on vaja rohkem andmeid, kordab agent tsüklit; muidu koostab loomuliku keele vastuse

<img src="../../../translated_images/et/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Muster" width="800"/>

*ReAct tsükkel — agent mõtleb, mida teha, kutsub tööriista, jälgib tulemust ja kordab, kuni saab lõpliku vastuse anda.*

See toimub automaatselt. Sa defineerid tööriistad ja nende kirjeldused. Mudel tegeleb otsustamisega, millal ja kuidas neid kasutada.

## Kuidas tööriistakutsed töötavad

### Tööriistade määratlused

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Sa defineerid funktsioonid selgete kirjelduste ja parameetrite spetsifikatsioonidega. Mudel näeb neid kirjeldusi oma süsteemi juhises ja mõistab, mida iga tööriist teeb.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Teie ilma päringu loogika
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistent on Spring Booti poolt automaatselt ühendatud järgmistega:
// - ChatModel'i bean
// - Kõik @Tool meetodid @Component klassidest
// - ChatMemoryProvider sessiooni haldamiseks
```

Järgmine diagramm kirjeldab iga annotatsiooni ja näitab, kuidas iga osa aitab AI-l mõista, millal tööriista kutsuda ja milliseid argumente edasi anda:

<img src="../../../translated_images/et/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Tööriistade määratluse anatoomia" width="800"/>

*Tööriistade määratluse anatoomia — @Tool ütleb AI-le, millal kasutada, @P kirjeldab iga parameetrit ja @AiService ühendab kõik käivitamisel.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ja küsi:
> - "Kuidas integreerida päris ilma API, näiteks OpenWeatherMap, mitte proovandmetega?"
> - "Mis teeb hea tööriistakirjelduse, mis aitab AI-l seda õigesti kasutada?"
> - "Kuidas käitled API vigu ja kasutuspiiranguid tööriistade rakendustes?"

### Otsuste tegemine

Kui kasutaja küsib "Milline on ilm Seattle'is?", ei vali mudel tööriista juhuslikult. Ta võrdleb kasutaja kavatsust iga tööriistakirjeldusega, millele tal ligipääs on, hindab neid olulisuse järgi ja valib parima vaste. Seejärel genereerib struktuurse funktsioonikutsungi õige parameetritega — antud juhul seab `location` väärtuseks `"Seattle"`.

Kui ükski tööriist ei sobi kasutaja päringule, vastab mudel oma teadmiste põhjal. Kui sobivaid tööriistu on mitu, valib ta kõige spetsiifilisema.

<img src="../../../translated_images/et/decision-making.409cd562e5cecc49.webp" alt="Kuidas AI otsustab, millist tööriista kasutada" width="800"/>

*Mudel hindab kõiki saadavalolevaid tööriistu kasutaja kavatsuse suhtes ja valib parima vaste — seetõttu on selgete ja konkreetsete tööriistakirjelduste kirjutamine oluline.*

### Täideviimine

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot ühendab deklareeriva `@AiService` liidese kõigi registreeritud tööriistadega automaatselt ning LangChain4j täidab tööriistakutsed automaatselt. Tagatipuks kulgeb tööriistakutse kuue etapi kaupa — kasutaja loomuliku keele küsimusest kuni loomuliku keele vastuseni:

<img src="../../../translated_images/et/tool-calling-flow.8601941b0ca041e6.webp" alt="Tööriistakutse voo diagramm" width="800"/>

*Lõpp-lõpuks voog — kasutaja küsib küsimuse, mudel valib tööriista, LangChain4j täidab selle ja mudel lisab tulemuse loomulikus vastuses.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ja küsi:
> - "Kuidas töötab ReAct muster ja miks see on AI agentide jaoks efektiivne?"
> - "Kuidas otsustab agent, millist tööriista kasutada ja millises järjekorras?"
> - "Mis juhtub, kui tööriista täideviimine ebaõnnestub — kuidas vigu tõhusalt käidelda?"

### Vastuse genereerimine

Mudel saab ilmaandmed ja vormindab need kasutajale loomulikus keeles vastuseks.

### Arhitektuur: Spring Boot automaatühendus

See moodul kasutab LangChain4j Spring Boot integreerimist deklareerivate `@AiService` liidestega. Käivitamisel avastab Spring Boot kõik `@Component` komponendid, mis sisaldavad `@Tool` meetodeid, sinu `ChatModel` bean'i ja `ChatMemoryProvider` — ning ühendab need kõik üheks `Assistant` liideseks ilma liigse korduseta.

<img src="../../../translated_images/et/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automaatühenduse arhitektuur" width="800"/>

*@AiService liides seob kokku ChatModeli, tööriistakomponendid ja mäluteenuse — Spring Boot haldab kogu ühendamise automaatselt.*

Selle lähenemise peamised eelised:

- **Spring Boot automaatühendus** — ChatModel ja tööriistad süstitakse automaatselt
- **@MemoryId muster** — Automaatne sessioonipõhine mälu haldus
- **Üksikud eksemplarid** — Assistent luuakse korra ja taaskasutatakse parema jõudluse saamiseks
- **Tüübikindel täideviimine** — Java meetodid kutsutakse otse tüübikonversiooniga
- **Mitme sammu orkestreerimine** — Haldab tööriistade aheldamist automaatselt
- **Null korduskood** — Ei mingeid käsitsi `AiServices.builder()` kutsumisi ega mälukaarti

Alternatiivsed lähenemised (käsitsi `AiServices.builder()`) nõuavad rohkem koodi ja ei kasuta Spring Boot integratsiooni eeliseid.

## Tööriistade aheldamine

**Tööriistade aheldamine** — Tõeline võimsus tööriistapõhistel agentidel avaldub siis, kui üks küsimus nõuab mitme tööriista kasutamist. Küsi "Milline on ilm Seattle'is Fahrenheiti kraadides?" ning agent aheldab automaatselt kaks tööriista: esmalt kutsub ta `getCurrentWeather`, et saada temperatuur Celsiuse kraadides, seejärel kannab selle väärtuse üle `celsiusToFahrenheit` teisendamiseks — kõik ühe vestluskorra jooksul.

<img src="../../../translated_images/et/tool-chaining-example.538203e73d09dd82.webp" alt="Tööriistade aheldamise näide" width="800"/>

*Tööriistade aheldamine töös — agent kutsub esmalt getCurrentWeather, seejärel suunab Celsiuse tulemuse celsiusToFahrenheit'i ja annab kombineeritud vastuse.*

**Õrnad tõrked** — Küsi ilma mõnes linnas, mida ei ole proovandmetes. Tööriist tagastab veateate ja AI selgitab, et ta ei saa aidata, selle asemel, et kogu rakendus kokku joosta. Tööriistad ebaõnnestuvad turvaliselt. Järgmine diagramm võrdleb kahte lähenemist — korraliku vigade haldamisega püüab agent erandi kinni ja vastab abistavalt, ilma selleta jookseb rakendus kokku:

<img src="../../../translated_images/et/error-handling-flow.9a330ffc8ee0475c.webp" alt="Vigade käitlemise voog" width="800"/>

*Kui tööriist ebaõnnestub, püüab agent vea kinni ja vastab abivalmilt, mitte ei lase rakendusel kokku jooksu.*

See toimub ühe vestluskorra jooksul. Agent orkestreerib mitut tööriistakutset autonomaatselt.

## Rakenduse käivitamine

**Kontrolli juurutust:**

Veendu, et juurutuskaustas on `.env` fail Azure volitustega (loodud Moodulis 01). Käivita see mooduli kaustast (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendust:**

> **Märkus:** Kui oled juba käivitanud kõik rakendused käsuga `./start-all.sh` juurkaustast (nagu Moodulis 01 kirjeldatud), jookseb see moodul juba pordil 8084. Võid allolevad käivitamiskäsud vahele jätta ja minna otse aadressile http://localhost:8084.

**Variant 1: Kasutades Spring Boot Dashboard'i (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Selle leiad vasakpoolse tegevusriba ikoonist (otsige Spring Boot ikooni).

Dashboardilt saad:
- Vaadata kõiki töölaua Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Jälgida rakenduste logisid reaalajas
- Jälgida rakenduse olekut

Klõpsa "tools" kõrval olevale mängu nuppule, et alustada seda moodulit, või käivita kõik moodulid korraga.

Nii näeb Spring Boot Dashboard VS Code'is välja:

<img src="../../../translated_images/et/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code'is — alusta, peata ja jälgi kõiki mooduleid ühest kohast*

**Variant 2: Kasutades shell-skripte**

Alusta kõiki veebi rakendusi (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juure kataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurkataloogist
.\start-all.ps1
```

Või alusta ainult seda moodulit:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkaustas `.env` failist ja ehitavad JAR-failid, kui neid pole olemas.

> **Märkus:** Kui eelistad ehitada kõik moodulid käsitsi enne käivitamist:
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

**Seiskamiseks:**

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

Rakendus pakub veebiliidest, kus saad suhelda AI agendiga, kellel on ligipääs ilma- ja temperatuuri teisendamise tööriistadele. Liides näeb välja selline — seal on kiire alustamise näited ja vestlusaken päringute saatmiseks:
<a href="images/tools-homepage.png"><img src="../../../translated_images/et/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agentide Tööriistade Liides" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agentide Tööriistade liides – kiireid näiteid ja vestlusliides tööriistadega suhtlemiseks*

### Proovi Lihtsat Tööriista Kasutust

Alusta lihtsa päringuga: „Muuda 100 kraadi Fahrenheiti Celsiuseks“. Agent mõistab, et on vaja temperatuuri konverteerimise tööriista, kutsub seda õige parameetritega ja tagastab tulemuse. Pane tähele, kui loomulik see tundub – sa ei määranud, millist tööriista kasutada ega kuidas seda kutsuda.

### Testi Tööriistade Järjestamist

Proovi nüüd midagi keerulisemat: „Mis ilm on Seattle’is ja muuda see Fahrenheiti kraadideks?“ Vaata, kuidas agent samm-sammult tegutseb. Esmalt hangib ilmateate (mis tagastab kraadid Celsiuses), märkab, et peab need Fahrenheiti ümber arvutama, kutsub konverteerimistööriista ning ühendab mõlemad vastused üheks.

### Vaata Vestluse Voogu

Vestlusliides hoiab vestluse ajalugu, võimaldades sul pidada mitme sammu pikkuseid vahetusi. Näed kõiki varasemaid päringuid ja vastuseid, mis teeb lihtsaks vestluse jälgimise ja selle mõistmise, kuidas agent mitme vahetuse jooksul konteksti ehitab.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/et/tools-conversation-demo.89f2ce9676080f59.webp" alt="Vestlus Mitme Tööriista Kutsuga" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mitmesammu vestlus, mis näitab lihtsaid teisendusi, ilmateate otsinguid ja tööriistade järjestamist*

### Katseta Erinevaid Päringuid

Proovi erinevaid kombinatsioone:
- Ilmateated: „Mis ilm on Tokyos?“
- Temperatuuri teisendused: „Mis on 25°C kelvinites?“
- Ühendatud päringud: „Kontrolli Pariisi ilma ja ütle, kas see on üle 20°C“

Pane tähele, kuidas agent tõlgendab loomulikku keelt ja kaardistab selle sobivate tööriistakõnedeni.

## Põhimõtted

### ReAct Muster (Mõtlemine ja Tegutsemine)

Agent vaheldub mõtlemise (otsustab, mida teha) ja tegutsemise (kasutab tööriistu) vahel. See mustrit võimaldab autonoomset probleemilahendust, mitte ainult käskluste täitmist.

### Tööriistade Kirjeldused on Olulised

Sinu tööriistade kirjelduste kvaliteet mõjutab otse, kui hästi agent neid kasutab. Selged, täpsed kirjeldused aitavad mudelil mõista, millal ja kuidas iga tööriista kutsuda.

### Seansi Halda

`@MemoryId` annotatsioon võimaldab automaatset seanssipõhist mälu haldamist. Iga seansi ID saab oma `ChatMemory` eksemplari, mida haldab `ChatMemoryProvider` bean, nii et mitmed kasutajad saavad korraga agendiga suhelda ilma, et nende vestlused seguneksid. Järgmine diagramm näitab, kuidas mitu kasutajat suunatakse isoleeritud mälupoodidesse nende seansi ID alusel:

<img src="../../../translated_images/et/session-management.91ad819c6c89c400.webp" alt="@MemoryId-ga Seansi Halduse" width="800"/>

*Iga seansi ID kaardistub eraldatud vestluse ajalukku – kasutajad ei näe kunagi teineteise sõnumeid.*

### Vigade Käsitlemine

Tööriistad võivad ebaõnnestuda – API-d aeguvad, parameetrid võivad olla vigased, välised teenused võivad lakkada töötamast. Tootlusagentidel on vaja vigade käsitlemist, et mudel saaks probleeme selgitada või proovida alternatiive selle asemel, et kogu rakendus kokku jookseks. Kui tööriist viskab erindi, püüab LangChain4j selle kinni ja edastab veateate mudelile, mis seejärel saab probleemi loomulikus keeles selgitada.

## Saadaval Tööriistad

Järgmine diagramm näitab laia tööriistade ökosüsteemi, mida saad ehitada. See moodul demonstreerib ilma ja temperatuuri tööriistu, kuid sama `@Tool` mustrit saab kasutada mis tahes Java meetodi puhul – alates andmebaaspäringutest kuni maksete töötlemiseni.

<img src="../../../translated_images/et/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tööriistade Ökosüsteem" width="800"/>

*Iga Java meetod, mida tähistatakse @Tool’iga saab kättesaadavaks tehisintellekti jaoks – muster laieneb andmebaasidele, API-dele, meilile, failitöötlusele ja enamale.*

## Millal Kasutada Tööriistapõhiseid Agente

Iga päring ei vaja tööriistu. Otsus sõltub sellest, kas tehisintellektil on vaja suhelda väliste süsteemidega või saab vastata omaenda teadmiste põhjal. Järgmine juhend kokkuvõtlikult näitab, millal tööriistad lisavad väärtust ja millal on need mittevajalikud:

<img src="../../../translated_images/et/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Millal Kasutada Tööriistu" width="800"/>

*Kiire otsustamise juhend – tööriistad on mõeldud reaalajas andmete, arvutuste ja toimingute jaoks; üldteadmised ja loomingulised ülesanded ei vaja neid.*

## Tööriistad vs RAG

Moodulid 03 ja 04 laiendavad mõlemad AI võimalusi, kuid põhimõtteliselt erinevalt. RAG annab mudelile ligipääsu **teadmistele** dokumentide otsimise kaudu. Tööriistad annavad mudelile võime **teha toiminguid** funktsioonide kutsumise kaudu. Järgmine diagramm võrdleb neid kahte lähenemist kõrvuti – alates sellest, kuidas iga töövoog töötab kuni kompromissideni nende vahel:

<img src="../../../translated_images/et/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tööriistad vs RAG Võrdlus" width="800"/>

*RAG hangib infot staatilistest dokumentidest – Tööriistad teostavad toiminguid ja tõmbavad dünaamilisi, reaalajas andmeid. Paljud tootmissüsteemid ühendavad mõlemad.*

Praktikas ühendavad paljud tootmissüsteemid mõlemad lähenemised: RAG vastuste aluseks dokumentatsioonis ja tööriistad live-andmete toomiseks või toimingute tegemiseks.

## Järgmised Sammud

**Järgmine Moodul:** [05-mcp - Mudeli konteksti protokoll (MCP)](../05-mcp/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 03 - RAG](../03-rag/README.md) | [Tagasi Algusesse](../README.md) | [Järgmine: Moodul 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüdleme täpsuse poole, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste ega valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->