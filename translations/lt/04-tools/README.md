# Modulis 04: DI Agentai su Įrankiais

## Turinys

- [Ko Išmoksite](../../../04-tools)
- [Reikalavimai](../../../04-tools)
- [Suprasti DI Agentus su Įrankiais](../../../04-tools)
- [Kaip Veikia Įrankių Iškvietimas](../../../04-tools)
  - [Įrankių Apibrėžimai](../../../04-tools)
  - [Sprendimų Priėmimas](../../../04-tools)
  - [Vykdymas](../../../04-tools)
  - [Atsako Generavimas](../../../04-tools)
  - [Architektūra: Spring Boot Automatinis Sujungimas](../../../04-tools)
- [Įrankių Grandinėlės](../../../04-tools)
- [Paleiskite Programą](../../../04-tools)
- [Naudojant Programą](../../../04-tools)
  - [Išbandykite Paprastą Įrankių Naudojimą](../../../04-tools)
  - [Išbandykite Įrankių Grandinėlę](../../../04-tools)
  - [Žiūrėti Pokalbio Srautą](../../../04-tools)
  - [Eksperimentuokite su Skirtingais Užklausimais](../../../04-tools)
- [Pagrindinės Sąvokos](../../../04-tools)
  - [ReAct Šablonas (Priežasties Glaudinimas ir Veikimas)](../../../04-tools)
  - [Įrankių Aprašymai Yra Svarbūs](../../../04-tools)
  - [Sesijų Valdymas](../../../04-tools)
  - [Klaidų Tvarkymas](../../../04-tools)
- [Galimi Įrankiai](../../../04-tools)
- [Kada Naudoti Agentus su Įrankiais](../../../04-tools)
- [Įrankiai vs RAG](../../../04-tools)
- [Kiti Žingsniai](../../../04-tools)

## Ko Išmoksite

Iki šiol jūs išmokote, kaip bendrauti su DI, efektyviai struktūruoti užklausas ir pagrįsti atsakymus savo dokumentais. Tačiau yra esminė apribojimas: kalbos modeliai gali tik generuoti tekstą. Jie negali patikrinti oro sąlygų, atlikti skaičiavimų, užklausti duomenų bazių ar sąveikauti su išorinėmis sistemomis.

Įrankiai tai keičia. Pateikdami modeliui prieigą prie kviečiamų funkcijų, jūs paverčiate jį iš teksto generatoriaus į agentą, kuris gali imtis veiksmų. Modelis nusprendžia, kada jam reikia įrankio, kurį įrankį naudoti ir kokius parametrus perduoti. Jūsų kodas vykdo funkciją ir grąžina rezultatą. Modelis integruoja tą rezultatą į savo atsakymą.

## Reikalavimai

- Atliktas [Modulis 01 - Įvadas](../01-introduction/README.md) (diegti Azure OpenAI ištekliai)
- Rekomenduojama atlikti ankstesnius modulius (šis modulis nuorodo į [RAG sąvokas iš Modulio 03](../03-rag/README.md) palyginime Įrankiai vs RAG)
- `.env` failas šakniniame kataloge su Azure kredencialais (sukurtas `azd up` 1 modulyje)

> **Pastaba:** Jei neatlikote Modulio 01, pirmiausia atlikite diegimo instrukcijas ten.

## Suprasti DI Agentus su Įrankiais

> **📝 Pastaba:** Šio modulio terminas "agentai" reiškia DI asistentus, patobulintus su įrankių kvietimo galimybėmis. Tai skiriasi nuo **Agentinio DI** modelių (autonomiški agentai su planavimu, atmintimi ir daugiapakopių sprendimų priėmimu), kuriuos aptarsime [Modulyje 05: MCP](../05-mcp/README.md).

Be įrankių kalbos modelis gali tik generuoti tekstą iš savo mokymosi duomenų. Paklauskite jo apie dabartines oro sąlygas, jis turi spėlioti. Duokite įrankius, ir jis gali iškviesti orų API, atlikti skaičiavimus arba užklausti duomenų bazę — tada įpinti tuos tikrus rezultatus į savo atsakymą.

<img src="../../../translated_images/lt/what-are-tools.724e468fc4de64da.webp" alt="Be įrankių vs Su įrankiais" width="800"/>

*Be įrankių modelis tik spėlioja — su įrankiais jis gali iškviesti API, atlikti skaičiavimus ir pateikti realaus laiko duomenis.*

DI agentas su įrankiais laikosi **Priežasties ir Veikimo (ReAct)** šablono. Modelis ne tik atsako — jis mąsto, ko jam reikia, veikia iškviesdamas įrankį, stebi rezultatą ir tada nusprendžia, ar veikti dar kartą, ar pateikti galutinį atsakymą:

1. **Mąstyk** — agentas analizuoja vartotojo klausimą ir nustato, kokios informacijos reikia
2. **Veik** — agentas pasirenka tinkamą įrankį, generuoja tinkamus parametrus ir iškviečia jį
3. **Stebėk** — agentas gauna įrankio išvestį ir įvertina rezultatą
4. **Kartok arba Atsakyk** — jei reikia daugiau duomenų, agentas sugrįžta į ciklą; priešingu atveju, sudaro natūralios kalbos atsakymą

<img src="../../../translated_images/lt/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Šablonas" width="800"/>

*ReAct ciklas — agentas mąsto, ką daryti, veikia kviesdamas įrankį, stebi rezultatą ir kartoja, kol gali pateikti galutinį atsakymą.*

Tai vyksta automatiškai. Jūs apibrėžiate įrankius ir jų aprašymus. Modelis sprendžia, kada ir kaip juos naudoti.

## Kaip Veikia Įrankių Iškvietimas

### Įrankių Apibrėžimai

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Jūs apibrėžiate funkcijas su aiškiais aprašymais ir parametrų specifikacijomis. Modelis mato šiuos aprašymus savo sistemos užklausoje ir supranta, ką kiekvienas įrankis daro.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Jūsų orų paieškos logika
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistentas automatiškai prijungiamas Spring Boot su:
// - ChatModel komponentu
// - Visais @Tool metodais iš @Component klasių
// - ChatMemoryProvider sesijų valdymui
```

Žemiau esantis diagrama išskaido kiekvieną anotaciją ir rodo, kaip kiekvienas elementas padeda DI suprasti, kada iškviesti įrankį ir kokius argumentus perduoti:

<img src="../../../translated_images/lt/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Įrankių Apibrėžimų Anatomija" width="800"/>

*Įrankio apibrėžimo anatomija — @Tool nurodo DI, kada jį naudoti, @P aprašo kiekvieną parametrą, o @AiService susieja viską paleidimo metu.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ir paklauskite:
> - "Kaip integruočiau tikrą orų API, pvz., OpenWeatherMap, vietoje imituotų duomenų?"
> - "Kas apibrėžia gerą įrankio aprašymą, kuris padeda DI teisingai jį naudoti?"
> - "Kaip valdyti API klaidas ir kvotų apribojimus įrankių įgyvendinime?"

### Sprendimų Priėmimas

Kai vartotojas klausia „Kokia orų prognozė Sietle?“, modelis neatsitiktinai pasirenka įrankį. Jis palygina vartotojo ketinimą su kiekvienu įrankio aprašymu, įvertina kiekvieno aktualumą ir pasirenka geriausiai atitinkantį. Tada generuoja struktūruotą funkcijos kvietimą su tinkamais parametrais — šiuo atveju nustatydamas `location` į `"Seattle"`.

Jei nė vienas įrankis atitinka vartotojo užklausą, modelis grįžta prie atsakymo iš savo žinių. Jei keli įrankiai tinka, jis pasirenka konkretiausią.

<img src="../../../translated_images/lt/decision-making.409cd562e5cecc49.webp" alt="Kaip DI Pasirenka Įrankį" width="800"/>

*Modelis įvertina kiekvieną turimą įrankį pagal vartotojo ketinimą ir pasirenka geriausią atitikimą — todėl aiškių, konkrečių įrankių aprašymų rašymas yra svarbus.*

### Vykdymas

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatiškai sujungia deklaratyvią `@AiService` sąsają su visais užregistruotais įrankiais, o LangChain4j vykdo įrankių kvietimus automatiškai. Užkulisiuose pilnas įrankio kvietimo procesas praeina per šešis etapus — nuo vartotojo natūralios kalbos klausimo iki natūralios kalbos atsakymo:

<img src="../../../translated_images/lt/tool-calling-flow.8601941b0ca041e6.webp" alt="Įrankių Kvietimo Srautas" width="800"/>

*Visas procesas — vartotojas užduoda klausimą, modelis pasirenka įrankį, LangChain4j jį vykdo, o modelis apjungia rezultatą į natūralų atsakymą.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ir paklauskite:
> - "Kaip veikia ReAct šablonas ir kodėl jis veiksmingas DI agentams?"
> - "Kaip agentas nusprendžia, kurį įrankį naudoti ir kokia tvarka?"
> - "Kas nutinka, jei įrankio vykdymas nepavyksta - kaip saugiai tvarkyti klaidas?"

### Atsako Generavimas

Modelis gauna orų duomenis ir formatuoja juos natūraliu kalbos atsakymu vartotojui.

### Architektūra: Spring Boot Automatinis Sujungimas

Šis modulis naudoja LangChain4j Spring Boot integraciją su deklaratyviomis `@AiService` sąsajomis. Paleidimo metu Spring Boot aptinka kiekvieną `@Component`, turintį `@Tool` metodus, jūsų `ChatModel` komponentą ir `ChatMemoryProvider` — tada sujungia juos į vieną `Assistant` sąsają be jokių boilerplate rašymo.

<img src="../../../translated_images/lt/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Automatinio Sujungimo Architektūra" width="800"/>

*@AiService sąsaja sujungia ChatModel, įrankių komponentus ir atminties tiekėją — Spring Boot automatiškai atlieka visą sujungimą.*

Svarbiausios šio požiūrio naudos:

- **Spring Boot automatinis sujungimas** — ChatModel ir įrankiai automatiškai įpurškiami
- **@MemoryId šablonas** — Automatinis sesijinis atminties valdymas
- **Viena instancija** — Assistant sukuriamas vieną kartą ir naudojamas pakartotinai geresniam našumui
- **Tipui saugus vykdymas** — Java metodai kviečiami tiesiogiai su tipų konvertavimu
- **Daugkartinio sukinėjimo orkestracija** — Automatinis įrankių grandinavimas
- **Nėra boilerplate** — Nereikia rankinių `AiServices.builder()` kvietimų ar atminties HashMap

Alternatyvūs metodai (rankinis `AiServices.builder()`) reikalauja daugiau kodo ir neprisijungia prie Spring Boot integracijos privalumų.

## Įrankių Grandinėlės

**Įrankių Grandinėlės** — tikroji agentų su įrankiais galia pasireiškia, kai vienam klausimui reikia kelių įrankių. Paklauskite „Kokia orų prognozė Sietle pagal Farenheito skalę?“ ir agentas automatiškai sujungia du įrankius: pirmiausia jis kviečia `getCurrentWeather`, kad gautų temperatūrą Celsijumi, tada perduoda tą reikšmę į `celsiusToFahrenheit` konvertavimui — visa tai viename pokalbio žingsnyje.

<img src="../../../translated_images/lt/tool-chaining-example.538203e73d09dd82.webp" alt="Įrankių Grandinėlės Pavyzdys" width="800"/>

*Įrankių grandinėlė veikia — agentas pirmiausia kviečia getCurrentWeather, tada perduoda Celsijaus rezultatą į celsiusToFahrenheit ir pateikia sujungtą atsakymą.*

**Lankstus Klaidos Tvarkymas** — Paklauskite apie orus mieste, kuris nėra imituotuose duomenyse. Įrankis grąžina klaidos pranešimą, o DI paaiškina, kad negali padėti vietoje to, kad sugriūtų. Įrankiai nesugenda pavojingai. Žemiau pateikta diagrama lygina abu požiūrius — su tinkamu klaidų tvarkymu agentas pagauna išimtį ir atsako naudingai, o be jo – programa sugriūna:

<img src="../../../translated_images/lt/error-handling-flow.9a330ffc8ee0475c.webp" alt="Klaidų Tvarkymo Srautas" width="800"/>

*Kai įrankis nesėkmingas, agentas pagauna klaidą ir atsako su naudingais paaiškinimais vietoje programos gedimo.*

Tai vyksta per vieną pokalbio žingsnį. Agentas autonomiškai organizuoja kelis įrankių kvietimus.

## Paleiskite Programą

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas yra šakniniame kataloge su Azure kredencialais (sukurtas Modulyje 01). Paleiskite iš šio modulio katalogo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` iš šakninio katalogo (kaip aprašyta 1 modulyje), šis modulis jau veikia 8084 prievade. Galite praleisti paleidimo komandas žemiau ir tiesiog nueiti į http://localhost:8084.

**1 Variantė: Naudojant Spring Boot Dashboard (Rekomenduojama VS Code naudotojams)**

Dev konteineryje yra Spring Boot Dashboard plėtinys, suteikiantis vizualią sąsają valdyti visas Spring Boot programas. Jį rasite kairėje VS Code veiklos juostoje (pažymėta Spring Boot ikona).

Iš Spring Boot Dashboard galite:
- Peržiūrėti visas darbo aplinkos Spring Boot programas
- Vienu paspaudimu paleisti/stabdyti programas
- Realizuoti programų žurnalų peržiūrą realiu laiku
- Stebėti programų būseną

Tiesiog spustelėkite paleidimo mygtuką prie „tools”, kad pradėtumėte šį modulį, arba paleiskite visus modulius iš karto.

Štai kaip atrodo Spring Boot Dashboard VS Code:

<img src="../../../translated_images/lt/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code — vienoje vietoje paleiskite, sustabdykite ir stebėkite visus modulius.*

**2 Variantė: Naudojant shell skriptus**

Paleiskite visas interneto programas (moduliai 01-04):

**Bash:**
```bash
cd ..  # Iš pagrindinio katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šaknininio katalogo
.\start-all.ps1
```

Arba paleiskite tik šį modulį:

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

Abu skriptai automatiškai įkrauna aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir pastatys JAR, jei jo nėra.

> **Pastaba:** Jei norite visus modulius pastatyti rankiniu būdu prieš paleisdami:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Atidarykite http://localhost:8084 savo naršyklėje.

**Norėdami sustabdyti:**

**Bash:**
```bash
./stop.sh  # Tik šis modulis
# Arba
cd .. && ./stop-all.sh  # Visi moduliai
```

**PowerShell:**
```powershell
.\stop.ps1  # Tik šis modulis
# Arba
cd ..; .\stop-all.ps1  # Visi moduliai
```

## Naudojant Programą

Programa suteikia žiniatinklio sąsają, kurioje galite bendrauti su DI agentu, turinčiu prieigą prie orų ir temperatūros konvertavimo įrankių. Sąsajos pavyzdys – ji apima greito starto pavyzdžius ir pokalbių skydelį užklausoms siųsti:
<a href="images/tools-homepage.png"><img src="../../../translated_images/lt/tools-homepage.4b4cd8b2717f9621.webp" alt="Dirbtinio intelekto agento įrankių sąsaja" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dirbtinio intelekto agento įrankių sąsaja – greiti pavyzdžiai ir pokalbių sąsaja sąveikai su įrankiais*

### Išbandykite paprastą įrankio naudojimą

Pradėkite nuo paprastos užklausos: „Konvertuokite 100 laipsnių Farenheito į Celsijų“. Agentas atpažįsta, kad reikia temperatūros konvertavimo įrankio, iškviečia jį su tinkamais parametrais ir pateikia rezultatą. Atkreipkite dėmesį, kokia natūrali tai yra – jūs nenurodėte, kurį įrankį naudoti ar kaip jį iškviesti.

### Išbandykite įrankių sujungimą

Dabar pabandykite ką nors sudėtingesnio: „Koks oras Sietle ir konvertuokite jį į Farenheitą?“ Stebėkite, kaip agentas atlieka veiksmus etapais. Pirmiausia jis gauna orą (kuris pateikiamas Celsijais), atpažįsta, kad reikia konvertuoti į Farenheitą, iškviečia konvertavimo įrankį ir sujungia abu rezultatus į vieną atsakymą.

### Peržiūrėkite pokalbio eigą

Pokalbių sąsaja įrašo pokalbių istoriją, leidžiantį vykdyti kelių etapų pokalbius. Matysite visas ankstesnes užklausas ir atsakymus, todėl lengva sekti pokalbį ir suprasti, kaip agentas stato kontekstą per kelis mainus.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/lt/tools-conversation-demo.89f2ce9676080f59.webp" alt="Pokalbis su kelių įrankių iškvietimais" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Kelių turų pokalbis, kuriame atliekamos paprastos konversijos, orų paieškos ir įrankių sujungimas*

### Eksperimentuokite su skirtingomis užklausomis

Išbandykite įvairius derinius:
- Orų paieškos: „Koks oras Tokijuje?“
- Temperatūros konvertavimai: „Kiek yra 25°C kelvinuose?“
- Kombinuotos užklausos: „Patikrink orą Paryžiuje ir pasakyk, ar temperatūra viršija 20°C“

Atkreipkite dėmesį, kaip agentas interpretuoja natūralią kalbą ir pritaiko ją tinkamiems įrankių iškvietimams.

## Pagrindinės sąvokos

### ReAct modelis (Mąstymas ir Veiksmas)

Agentas keičiasi tarp mąstymo (sprendžiant, ką daryti) ir veikimo (naudojant įrankius). Šis modelis leidžia savarankiškai spręsti problemas, o ne tik vykdyti nurodymus.

### Įrankių aprašymai yra svarbūs

Įrankių aprašymų kokybė tiesiogiai veikia, kaip gerai agentas juos naudoja. Aiškūs ir konkretūs aprašymai padeda modeliui suprasti, kada ir kaip iškviesti kiekvieną įrankį.

### Sesijos valdymas

`@MemoryId` anotacija leidžia automatiškai valdyti sesijai priskirtą atmintį. Kiekvienas sesijos ID gauna savo `ChatMemory` egzempliorių, valdytą per `ChatMemoryProvider` komponentą, todėl keli vartotojai gali vienu metu bendrauti su agentu nesimaišant pokalbiams. Žemiau pateiktoje diagramoje parodyta, kaip keli vartotojai nukreipiami į atskirus atminties saugyklas pagal jų sesijų ID:

<img src="../../../translated_images/lt/session-management.91ad819c6c89c400.webp" alt="Sesijos valdymas su @MemoryId" width="800"/>

*Kiekvienas sesijos ID atitinka atskirą pokalbių istoriją – vartotojai niekada nemato vienas kito žinučių.*

### Klaidos valdymas

Įrankiai gali sugesti – API pavėluoti, parametrai gali būti neteisingi, išorinės paslaugos gali neveikti. Gamybos agentams reikalingas klaidų valdymas, kad modelis galėtų paaiškinti problemas arba bandyti alternatyvas, o ne sugadintų visą programą. Kai įrankis meta išimtį, LangChain4j ją pagavo ir perduoda klaidos pranešimą modeliui, kuris gali tada natūralia kalba paaiškinti problemą.

## Turimi įrankiai

Žemiau pateikta diagrama parodo platų įrankių ekosistemą, kurią galite kurti. Šis modulis demonstruoja orų ir temperatūros įrankius, tačiau tas pats `@Tool` modelis tinka bet kuriam Java metodui – nuo duomenų bazių užklausų iki mokėjimų apdorojimo.

<img src="../../../translated_images/lt/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Įrankių ekosistema" width="800"/>

*Kiekvienas Java metodas, pažymėtas @Tool, tampa prieinamas DI – modelis gali naudoti įrankius, skirtus duomenų bazėms, API, el. paštui, failų operacijoms ir daugeliui kitų.*

## Kada naudoti įrankiais pagrįstus agentus

Ne kiekvienai užklausai reikalingi įrankiai. Sprendimas priklauso nuo to, ar DI turi bendrauti su išorinėmis sistemomis, ar gali atsakyti iš savo žinių. Žemiau pateiktas vadovas apibendrina, kada įrankiai prideda vertės, o kada jų nereikia:

<img src="../../../translated_images/lt/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kada naudoti įrankius" width="800"/>

*Greitas sprendimų vadovas – įrankiai skirti realaus laiko duomenims, skaičiavimams ir veiksmams; bendros žinios ir kūrybiniai uždaviniai jų nereikalauja.*

## Įrankiai prieš RAG

03 ir 04 moduliai abu plečia DI galimybes, bet iš esmės skirtingais būdais. RAG suteikia modeliui prieigą prie **žinių** iš dokumentų. Įrankiai leidžia modeliui imtis **veiksmų** iškviečiant funkcijas. Žemiau pateikta diagrama lygina šiuos du požiūrius šalia vienas kito – kaip veikia kiekvienas srautas ir kokie yra kompromisai:

<img src="../../../translated_images/lt/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Įrankiai prieš RAG palyginimas" width="800"/>

*RAG gauna informaciją iš statinių dokumentų – įrankiai vykdo veiksmus ir gauna dinamiškus, realaus laiko duomenis. Daugelis gamybos sistemų naudoja abu.*

Praktiškai daug gamybos sistemų naudoja abu požiūrius: RAG tvirtina atsakymus jūsų dokumentacijoje, o įrankiai gauna gyvus duomenis arba atlieka operacijas.

## Tolimesni žingsniai

**Kitas modulis:** [05-mcp - Modelio konteksto protokolas (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 03 - RAG](../03-rag/README.md) | [Atgal į Pradžią](../README.md) | [Kitas: Modulis 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turi būti laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojama naudoti profesionalų žmogišką vertimą. Mes neatsakome už bet kokius nesusipratimus ar neteisingą interpretavimą, kylančius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->