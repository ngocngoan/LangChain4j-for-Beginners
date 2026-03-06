# Modulis 04: DI agentai su įrankiais

## Turinys

- [Ko Išmoksite](../../../04-tools)
- [Prieš sąlygos](../../../04-tools)
- [Supratimas apie DI agentus su įrankiais](../../../04-tools)
- [Kaip veikia įrankių kvietimas](../../../04-tools)
  - [Įrankių apibrėžimai](../../../04-tools)
  - [Sprendimų priėmimas](../../../04-tools)
  - [Vykdymas](../../../04-tools)
  - [Atsakymų generavimas](../../../04-tools)
  - [Architektūra: Spring Boot automatinis sujungimas](../../../04-tools)
- [Įrankių grandinimas](../../../04-tools)
- [Paleiskite programą](../../../04-tools)
- [Naudojant programą](../../../04-tools)
  - [Išbandykite paprastą įrankio naudojimą](../../../04-tools)
  - [Patikrinkite įrankių grandinimą](../../../04-tools)
  - [Peržiūrėkite pokalbio srautą](../../../04-tools)
  - [Eksperimentuokite su skirtingais užklausimais](../../../04-tools)
- [Svarbiausios sąvokos](../../../04-tools)
  - [ReAct modelis (mąstymas ir veikimas)](../../../04-tools)
  - [Įrankių aprašymai yra svarbūs](../../../04-tools)
  - [Sesijos valdymas](../../../04-tools)
  - [Klaidų tvarkymas](../../../04-tools)
- [Galimi įrankiai](../../../04-tools)
- [Kada naudoti įrankiais pagrįstus agentus](../../../04-tools)
- [Įrankiai prieš RAG](../../../04-tools)
- [Kitos veiksmų gairės](../../../04-tools)

## Ko Išmoksite

Iki šiol jūs išmokote kaip kalbėtis su DI, kaip efektyviai struktūruoti užklausas ir pagrįsti atsakymus savo dokumentais. Tačiau vis dar yra pagrindinis apribojimas: kalbos modeliai gali tik generuoti tekstą. Jie negali patikrinti oro sąlygų, atlikti skaičiavimų, užklausinėti duomenų bazių ar bendrauti su išorinėmis sistemomis.

Įrankiai tai keičia. Suteikdami modeliui prieigą prie funkcijų, kurias jis gali iškviesti, jūs paverčiate jį iš teksto generatoriaus į agentą, kuris gali vykdyti veiksmus. Modelis nusprendžia, kada jam reikia įrankio, kurį įrankį naudoti ir kokius parametrus perduoti. Jūsų kodas vykdo funkciją ir grąžina rezultatą. Modelis šį rezultatą įtraukia į savo atsakymą.

## Prieš sąlygos

- Užbaigtas [Modulis 01 - Įvadas](../01-introduction/README.md) (diegtos Azure OpenAI ištekliai)
- Rekomenduojama įveikti ankstesnius modulius (šiame modulyje paminėtos [RAG sąvokos iš Modulio 03](../03-rag/README.md) įrankių ir RAG palyginime)
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas naudojant `azd up` Modulyje 01)

> **Pastaba:** Jei dar neužbaigėte Modulio 01, pirmiausia sekite diegimo instrukcijas ten.

## Supratimas apie DI agentus su įrankiais

> **📝 Pastaba:** Šio modulio terminas „agentai“ reiškia DI asistentus, patobulintus įrankių kvietimo galimybėmis. Tai skiriasi nuo **Agentic AI** modelių (autonomi agentai su planavimu, atmintimi ir daugiamąstekliu mąstymu), apie kuriuos kalbėsime [Modulyje 05: MCP](../05-mcp/README.md).

Be įrankių, kalbos modelis gali tik generuoti tekstą remiantis mokymo duomenimis. Paklauskite apie esamą orą, ir jis tik spės. Suteikite įrankius, ir jis gali iškviesti orų API, atlikti skaičiavimus arba užklausti duomenų bazę — ir tada įpinti šiuos tikrus rezultatus į atsakymą.

<img src="../../../translated_images/lt/what-are-tools.724e468fc4de64da.webp" alt="Be įrankių prieš Įrankius" width="800"/>

*Be įrankių modelis tik spėlioja — su įrankiais jis gali kviesti API, vykdyti skaičiavimus ir pateikti realaus laiko duomenis.*

DI agentas su įrankiais seka **Mąstymo ir Veikimo (ReAct)** modelį. Modelis ne tik atsako — jis galvoja, ko jam reikia, veikia iškviesdamas įrankį, stebi rezultatą ir suveda sprendimą, ar tęsti veiksmą ar pateikti galutinį atsakymą:

1. **Mąstymas** — agentas analizuoja naudotojo klausimą ir nusprendžia, kokios informacijos reikia
2. **Veikimas** — agentas pasirenka tinkamą įrankį, generuoja teisingus parametrus ir jį iškviečia
3. **Stebėjimas** — agentas gauna įrankio išvestį ir įvertina rezultatą
4. **Kartojimas ar Atsakymas** — jei reikia daugiau duomenų, agentas kartoja; kitu atveju sudaro natūralios kalbos atsakymą

<img src="../../../translated_images/lt/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct modelis" width="800"/>

*ReAct ciklas — agentas mąsto, ką daryti, veikia kviesdamas įrankį, stebi rezultatą ir kartoja, kol gali pateikti galutinį atsakymą.*

Tai vyksta automatiškai. Jūs apibrėžiate įrankius ir jų aprašymus. Modelis tvarko sprendimų priėmimą, kada ir kaip juos naudoti.

## Kaip veikia įrankių kvietimas

### Įrankių apibrėžimai

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Jūs apibrėžiate funkcijas su aiškiais aprašymais ir parametrų specifikacijomis. Modelis mato šiuos aprašymus sistemos užklausoje ir supranta, ką kiekvienas įrankis daro.

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

// Pagalbininkas automatiškai sujungiamas per Spring Boot su:
// - ChatModel komponentu
// - Visais @Tool metodais iš @Component klasių
// - ChatMemoryProvider sesijų valdymui
```

Žemiau esantis diagrama paaiškina kiekvieną anotaciją ir parodo, kaip kiekviena dalis padeda DI suprasti, kada iškviesti įrankį ir kokius argumentus pateikti:

<img src="../../../translated_images/lt/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Įrankių apibrėžimų sandara" width="800"/>

*Įrankio apibrėžimo sandara — @Tool nurodo DI, kada jį naudoti, @P aprašo kiekvieną parametrą, o @AiService paleidimo metu sujungia viską.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ir paklauskite:
> - „Kaip integruočiau tikrą orų API, pvz., OpenWeatherMap, vietoje imituotų duomenų?“
> - „Kas sudaro gerą įrankio aprašymą, kuris padeda DI jį tinkamai naudoti?“
> - „Kaip tvarkyti API klaidas ir greičio apribojimus įrankių įgyvendinimuose?“

### Sprendimų priėmimas

Kai naudotojas klausia „Koks oras Sietle?“, modelis neatsirenka įrankio atsitiktinai. Jis lygina naudotojo ketinimą su kiekvienu įrankio aprašymu, įvertina atitiktį ir parenka geriausią variantą. Tada generuoja struktūruotą funkcijos kvietimą su teisingais parametrais — šiuo atveju, nustatydamas `location` kaip `"Seattle"`.

Jei nė vienas įrankis neatitinka naudotojo užklausos, modelis grįžta atsakyti iš savo žinių. Jei tinka keli įrankiai, pasirenkamas konkretiausias.

<img src="../../../translated_images/lt/decision-making.409cd562e5cecc49.webp" alt="Kaip DI nusprendžia, kurį įrankį naudoti" width="800"/>

*Modelis įvertina visus prieinamus įrankius pagal naudotojo ketinimą ir pasirenka geriausią variantą — todėl svarbu rašyti aiškius, specifinius įrankių aprašymus.*

### Vykdymas

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatiškai sujungia deklaracinį `@AiService` interfeisą su visais registruotais įrankiais, ir LangChain4j automatiškai vykdo įrankių kvietimus. Užkulisiuose pilnas įrankio kvietimas eina per šešis etapus — nuo naudotojo klausimo natūralia kalba iki natūralaus kalbos atsakymo:

<img src="../../../translated_images/lt/tool-calling-flow.8601941b0ca041e6.webp" alt="Įrankių kvietimo srautas" width="800"/>

*Visas srautas — naudotojas užduoda klausimą, modelis pasirenka įrankį, LangChain4j jį vykdo, ir modelis įterpia rezultatą į natūralų atsakymą.*

Jei paleidote [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) Modulyje 00, jau matėte šį modelį veikiant — `Calculator` įrankiai buvo kviečiami tokiu pačiu būdu. Žemiau pateiktas sekų diagrama tiksliai parodo, kas vyko užkulisiuose per tą demonstraciją:

<img src="../../../translated_images/lt/tool-calling-sequence.94802f406ca26278.webp" alt="Įrankių kvietimo sekų diagrama" width="800"/>

*Įrankių kvietimo ciklas iš Greito Starto demo — `AiServices` siunčia jūsų žinutę ir įrankių schemas LLM, LLM atsako funkcijos kvietimu kaip `add(42, 58)`, LangChain4j vykdo `Calculator` metodą lokaliai ir grąžina rezultatą galutiniam atsakymui.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ir paklauskite:
> - „Kaip veikia ReAct modelis ir kodėl jis efektyvus DI agentams?“
> - „Kaip agentas nusprendžia, kurį įrankį naudoti ir kokia tvarka?“
> - „Kas nutinka, jeigu įrankio vykdymas nepavyksta — kaip tvarkyti klaidas patikimai?“

### Atsakymų generavimas

Modelis gauna orų duomenis ir suformuoja juos į natūralios kalbos atsakymą naudotojui.

### Architektūra: Spring Boot automatinis sujungimas

Šis modulis naudoja LangChain4j integraciją su Spring Boot ir deklaracinį `@AiService` interfeisą. Paleidimo metu Spring Boot atranda kiekvieną `@Component`, turintį `@Tool` metodus, jūsų `ChatModel` komponentą ir `ChatMemoryProvider` — ir sujungia juos visus į vieną `Assistant` interfeisą be boilerplate kodo.

<img src="../../../translated_images/lt/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot automatinių sujungimų architektūra" width="800"/>

*@AiService interfeisas sujungia ChatModel, įrankių komponentus ir atminties tiekėją — Spring Boot automatiškai tvarko visus sujungimus.*

Štai visos užklausos gyvavimo ciklas sekų diagramoje — nuo HTTP užklausos per valdiklį, servisą ir automatiškai sujungtą proxy iki įrankio vykdymo ir atgal:

<img src="../../../translated_images/lt/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot įrankių kvietimo sekos diagrama" width="800"/>

*Pilnas Spring Boot užklausos gyvavimo ciklas — HTTP užklausa teka per valdiklį ir servisą į automatiškai sujungtą Assistant proxy, kuris organizuoja LLM ir įrankių kvietimus automatiškai.*

Pagrindinės šio požiūrio naudos:

- **Spring Boot automatinis sujungimas** — ChatModel ir įrankiai įšvirkščiami automatiškai
- **@MemoryId modelis** — Automatizuotas sesijos pagrindu valdomas atminties valdymas
- **Vienas egzempliorius** — Assistant sukuriamas vieną kartą ir pakartotinai naudojamas geresniam našumui
- **Tipų saugus vykdymas** — Java metodai kviečiami tiesiogiai su tipų konvertavimu
- **Daugiatūrio orkestracija** — Automatiškai tvarko įrankių grandinimą
- **Nulinis boilerplate** — Nereikia rankinių `AiServices.builder()` kvietimų ar HashMap atminties

Alternatyvūs metodai (rankinis `AiServices.builder()`) reikalauja daugiau kodo ir nepritaiko Spring Boot integracijos privalumų.

## Įrankių grandinimas

**Įrankių grandinimas** — Tikroji įrankiais pagrįstų agentų galia pasireiškia, kai vienam klausimui reikia kelių įrankių. Paklauskite: „Koks oras Sietle pagal Farenheito skalę?“ ir agentas automatiškai sujungia du įrankius: pirmiausia iškviečia `getCurrentWeather`, kad gautų temperatūrą Celsijumi, paskui perduoda šią reikšmę į `celsiusToFahrenheit` konvertavimui — visa tai viename pokalbio cikle.

<img src="../../../translated_images/lt/tool-chaining-example.538203e73d09dd82.webp" alt="Įrankių grandinimo pavyzdys" width="800"/>

*Įrankių grandinimas veikloje — agentas pirmiausia iškviečia getCurrentWeather, paskui perveda rezultatus į celsiusToFahrenheit ir pateikia sujungtą atsakymą.*

**Gražios klaidų tvarkymo situacijos** — Paklauskite apie orą mieste, kuris nėra imituotuose duomenyse. Įrankis grąžina klaidos pranešimą, o DI paaiškina, kad negali padėti, vietoje to, kad sugriūtų. Įrankiai gedimų atveju elgiasi saugiai. Žemiau esanti diagrama palygina dvi situacijas — su tinkamu klaidų tvarkymu agentas pagauna išimtį ir atsako naudingai, o be jos visa programa sugriūna:

<img src="../../../translated_images/lt/error-handling-flow.9a330ffc8ee0475c.webp" alt="Klaidų valdymo srautas" width="800"/>

*Kai įrankis nepavyksta, agentas pagauna klaidą ir vietoje programos gedimo pateikia naudingą paaiškinimą.*

Tai vyksta viename pokalbio cikle. Agentas automatiškai organizuoja kelis įrankių kvietimus.

## Paleiskite programą

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` faile pagrindiniame kataloge yra Azure kredencialai (sukurti Modulyje 01). Paleiskite tai iš modulio katalogo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` pagrindiniame kataloge (kaip aprašyta Modulyje 01), šis modulis jau veikia per 8084 prievadą. Galite praleisti žemiau pateiktus paleidimo veiksmus ir tiesiog nueiti į http://localhost:8084.

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

Dev konteineris įtraukia Spring Boot Dashboard plėtinį, suteikiantį vizualią priemonę valdyti visas Spring Boot programas. Jį rasite šoniniame veiklos juostos skiltyje kairėje VS Code (ieškokite Spring Boot ikonos).

Iš Spring Boot Dashboard galite:
- Matyti visas darbo aplinkoje prieinamas Spring Boot programas
- Vienu paspaudimu paleisti/stabdyti programas
- Žiūrėti programų žurnalus realiu laiku
- Stebėti programų būklę

Tiesiog spustelėkite paleidimo mygtuką šalia „tools“, kad paleistumėte šį moduli, arba paleiskite visus modulius kartu.

Taip Spring Boot Dashboard atrodo VS Code:

<img src="../../../translated_images/lt/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code — paleiskite, stabdykite ir stebėkite visus modulius iš vienos vietos*

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (modulius 01-04):

**Bash:**
```bash
cd ..  # Iš šaknų katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš pagrindinio katalogo
.\start-all.ps1
```

Ar pradėkite tik šį modulį:

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

Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite rankiniu būdu sukurti visus modulius prieš pradėdami:
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

## Programos naudojimas

Programa suteikia žiniatinklio sąsają, kurioje galite bendrauti su DI agentu, turinčiu prieigą prie oro sąlygų ir temperatūros konvertavimo įrankių. Štai kaip atrodo sąsaja — joje yra greitos pradžios pavyzdžių ir pokalbių skydelis prašymams siųsti:

<a href="images/tools-homepage.png"><img src="../../../translated_images/lt/tools-homepage.4b4cd8b2717f9621.webp" alt="DI agente esantys įrankiai" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*DI agento įrankių sąsaja – greiti pavyzdžiai ir pokalbių sąsaja bendraujant su įrankiais*

### Išbandykite paprastą įrankių naudojimą

Pradėkite nuo paprasto prašymo: „Konvertuoti 100 laipsnių Fahrenheito į Celsijų“. Agentas suvokia, kad reikalingas temperatūros konvertavimo įrankis, iškviečia jį su tinkamais parametrais ir pateikia rezultatą. Pastebėkite, kaip natūraliai tai atrodo – jūs nenurodėte, kurį įrankį naudoti ar kaip jį iškviesti.

### Išbandykite įrankių grandinėlę

Dabar pabandykite kažką sudėtingesnio: „Kokia oras Sietle ir konvertuokite jį į Farenheitus?“ Stebėkite, kaip agentas atlieka veiksmus žingsnis po žingsnio. Pirmiausia jis gauna orą (kuris pateikiamas Celsijais), suvokia, kad reikia konvertuoti į Farenheitus, iškviečia konvertavimo įrankį ir sujungia abu rezultatus į vieną atsakymą.

### Matykite pokalbio eigą

Pokalbių sąsaja palaiko pokalbio istoriją, leidžiančią atlikti daugiapakopes sąveikas. Galite matyti visus ankstesnius užklausimus ir atsakymus, todėl lengva sekti pokalbį ir suprasti, kaip agentas kuria kontekstą per kelis mainus.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/lt/tools-conversation-demo.89f2ce9676080f59.webp" alt="Pokalbis su keliais įrankių kvietimais" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Daugiapakopis pokalbis, rodantis paprastus konvertavimus, oro sąlygų užklausimus ir įrankių grandinėlę*

### Eksperimentuokite su įvairiomis užklausomis

Išbandykite įvairias kombinacijas:
- Orų užklausos: „Kokia oras Tokijuje?“
- Temperatūros konvertavimai: „Kiek yra 25 °C kelvinais?“
- Kombinuotos užklausos: „Patikrink orą Paryžiuje ir pasakyk, ar temperatūra viršija 20 °C“

Atkreipkite dėmesį, kaip agentas interpretuoja natūralią kalbą ir pritaiko tinkamus įrankių kvietimus.

## Pagrindinės sąvokos

### ReAct modelis (mąstymas ir veiksmai)

Agentas keičiasi tarp mąstymo (sprendžia, ką daryti) ir veiksmų (naudoja įrankius). Šis modelis leidžia autonomiškai spręsti problemas, o ne tik vykdyti nurodymus.

### Įrankių aprašymai yra svarbūs

Įrankių aprašymų kokybė tiesiogiai veikia, kaip gerai agentas juos naudoja. Aiškūs, konkretūs aprašymai padeda modeliui suprasti, kada ir kaip kvieti kiekvieną įrankį.

### Sesijos valdymas

`@MemoryId` anotacija leidžia automatinį sesijos pagrindu valdomą atminties tvarkymą. Kiekvienas sesijos ID gauna savo `ChatMemory` egzempliorių, kurį valdo `ChatMemoryProvider` komponentas, taigi keli vartotojai gali bendrauti su agentu vienu metu, nepersimaišydami pokalbių. Žemiau pateikta schema rodo, kaip keli vartotojai nukreipiami į atskiras atminties saugyklas pagal jų sesijos ID:

<img src="../../../translated_images/lt/session-management.91ad819c6c89c400.webp" alt="Sesijos valdymas su @MemoryId" width="800"/>

*Kiekvienas sesijos ID atitinka atskirą pokalbio istoriją — vartotojai nemato vieni kitų žinučių.*

### Klaidų tvarkymas

Įrankiai gali sugesti — API gali strigti, parametrai būti klaidingi, išorinės paslaugos nebeveikti. Produkciniai agentai turi turėti klaidų tvarkymą, kad modelis galėtų paaiškinti problemas arba bandyti kitus variantus vietoje visos programos gedimo. Kai įrankis meta išimtį, LangChain4j ją pagavęs perduoda klaidos žinutę atgal modeliui, kuris gali paaiškinti problemą natūralia kalba.

## Turimi įrankiai

Žemiau pateikta schema rodo platų įrankių ekosistemą, kurią galite sukurti. Šis modulis demonstruoja oro sąlygų ir temperatūros įrankius, tačiau tas pats `@Tool` modelis veikia bet kuriame Java metode — nuo duomenų bazių užklausų iki mokėjimų apdorojimo.

<img src="../../../translated_images/lt/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Įrankių ekosistema" width="800"/>

*Bet kuris su @Tool anotacija pažymėtas Java metodas tampa prieinamas DI modeliui — modelis gali naudoti įrankius, duomenų bazes, API, el. paštą, failų operacijas ir daugiau.*

## Kada naudoti įrankiais paremtus agentus

Ne kiekviena užklausa reikalauja įrankių. Sprendimas priklauso nuo to, ar DI turi bendrauti su išorinėmis sistemomis, ar gali atsakyti remdamasis savo žiniomis. Toliau pateikiama pagalba, kada įrankiai naudingi, o kada jų nereikia:

<img src="../../../translated_images/lt/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kada naudoti įrankius" width="800"/>

*Greitas sprendimų pagalbininkas — įrankiai skirti realaus laiko duomenims, skaičiavimams ir veiksmams; bendrąsias žinias ir kūrybinius uždavinius galima spręsti be jų.*

## Įrankiai vs RAG

3 ir 4 moduliai abu išplečia DI galimybes, tačiau fundamentaliai skirtingais būdais. RAG suteikia modeliui prieigą prie **žinių** traukiant dokumentus. Įrankiai suteikia modeliui galimybę imtis **veiksmų** kviečiant funkcijas. Žemiau pateikta schema lygina šiuos du metodus šalia vienas kito — nuo veikimo principų iki kompromisų:

<img src="../../../translated_images/lt/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Įrankiai vs RAG palyginimas" width="800"/>

*RAG traukia informaciją iš statinių dokumentų — įrankiai atlieka veiksmus ir gauna dinamiškus, realaus laiko duomenis. Daugelis produkcinių sistemų naudoja abu variantus.*

Praktikoje daugelis produkcinių sistemų derina abu požiūrius: RAG naudoti atsakymams pagrįsti jūsų dokumentacijoje, o Įrankiai — gyviems duomenims gauti arba operacijoms atlikti.

## Kiti žingsniai

**Kitas modulis:** [05-mcp - Modelio konteksto protokolas (MCP)](../05-mcp/README.md)

---

**Naršymas:** [← Ankstesnis: Modulis 03 - RAG](../03-rag/README.md) | [Grįžti į pagrindinį](../README.md) | [Kitas: Modulis 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus žmogiškasis vertimas. Mes neprisiimame atsakomybės už bet kokius nesusipratimus ar klaidingus interpretavimus, atsiradusius naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->