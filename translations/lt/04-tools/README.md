# Modulis 04: DI agentai su įrankiais

## Turinys

- [Ko išmoksite](../../../04-tools)
- [Reikalavimai](../../../04-tools)
- [Supratimas apie DI agentus su įrankiais](../../../04-tools)
- [Kaip veikia įrankių kvietimas](../../../04-tools)
  - [Įrankių apibrėžimai](../../../04-tools)
  - [Sprendimų priėmimas](../../../04-tools)
  - [Vykdymas](../../../04-tools)
  - [Atsakymo generavimas](../../../04-tools)
  - [Architektūra: Spring Boot automatinis susiejimas](../../../04-tools)
- [Įrankių grandinimas](../../../04-tools)
- [Paleiskite programą](../../../04-tools)
- [Programos naudojimas](../../../04-tools)
  - [Išbandykite paprastą įrankio naudojimą](../../../04-tools)
  - [Išbandykite įrankių grandinimą](../../../04-tools)
  - [Peržiūrėkite pokalbio eigą](../../../04-tools)
  - [Eksperimentuokite su įvairiais užklausimais](../../../04-tools)
- [Pagrindinės sąvokos](../../../04-tools)
  - [ReAct modelis (mąstymas ir veikimas)](../../../04-tools)
  - [Svarbios įrankių aprašų reikšmės](../../../04-tools)
  - [Sesijos valdymas](../../../04-tools)
  - [Klaidų valdymas](../../../04-tools)
- [Turimi įrankiai](../../../04-tools)
- [Kada naudoti įrankiais pagrįstus agentus](../../../04-tools)
- [Įrankiai vs RAG](../../../04-tools)
- [Tolimesni žingsniai](../../../04-tools)

## Ko išmoksite

Iki šiol išmokote, kaip kalbėtis su DI, kaip efektyviai struktūruoti užklausas ir kaip remtis savo dokumentais atsakymuose. Tačiau egzistuoja esminė ribotybė: kalbos modeliai gali tik generuoti tekstą. Jie negali patikrinti oro sąlygų, atlikti skaičiavimų, užklausti duomenų bazių ar bendrauti su išorinėmis sistemomis.

Įrankiai keičia šią situaciją. Suteikdami modeliui galimybę iškviesti funkcijas, jūs paverčiate jį iš tekstų generatoriaus į agentą, kuris gali imtis veiksmų. Modelis nusprendžia, kada jam reikia įrankio, kurį įrankį naudoti ir kokius parametrus perduoti. Jūsų kodas vykdo funkciją ir grąžina rezultatą. Modelis įtraukia tą rezultatą į savo atsakymą.

## Reikalavimai

- Baigtas Modulis 01 (išdėstyti Azure OpenAI resursai)
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas vykdant `azd up` Module 01 metu)

> **Pastaba:** Jei dar nebaigėte Modulio 01, pirmiausia sekite ten pateiktas diegimo instrukcijas.

## Supratimas apie DI agentus su įrankiais

> **📝 Pastaba:** Terminas „agentai“ šiame modulyje reiškia DI asistentus, papildytus įrankių kvietimo galimybe. Tai skiriasi nuo **Agentic AI** modelių (autonomiški agentai su planavimu, atmintimi ir daugiapakopiu mąstymu), kurie bus aptarti [Module 05: MCP](../05-mcp/README.md).

Be įrankių kalbos modelis gali tik generuoti tekstą remdamasis savo mokymo duomenimis. Paklauskite apie esamą orą — modelis tik spėlioja. Suteikite jam įrankių — jis gali iškviesti oro sąlygų API, atlikti skaičiavimus ar užklausti duomenų bazę — ir įterpti tikrus rezultatus į atsakymą.

<img src="../../../translated_images/lt/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Be įrankių modelis tik spėlioja — su įrankiais jis gali kviesti API, atlikti skaičiavimus ir pateikti realaus laiko duomenis.*

DI agentas su įrankiais naudoja **Mąstymo ir Veikimo (ReAct)** modelį. Modelis ne tik atsako — jis apmąsto, ko jam reikia, veikia iškeldamas įrankį, stebi rezultatą ir tada nusprendžia, ar veikti toliau, ar pateikti galutinį atsakymą:

1. **Mąsto** — Agentas analizuoja vartotojo klausimą ir nustato, kokios informacijos reikia
2. **Veikia** — Agentas pasirenka tinkamą įrankį, generuoja reikiamus parametrus ir jį iškviečia
3. **Stebi** — Agentas gauna įrankio išvestį ir įvertina rezultatą
4. **Kartojasi arba atsako** — Jei reikia daugiau duomenų, agentas grįžta prie pradžios; kitaip suformuluoja natūralios kalbos atsakymą

<img src="../../../translated_images/lt/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct ciklas — agentas sprendžia, ką daryti, veikia iškeldamas įrankį, stebi rezultatą ir kartojasi, kol pateikia galutinį atsakymą.*

Tai vyksta automatiškai. Jūs apibrėžiate įrankius ir jų aprašus. Modelis tvarko sprendimų priėmimą, kada ir kaip juos naudoti.

## Kaip veikia įrankių kvietimas

### Įrankių apibrėžimai

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Jūs apibrėžiate funkcijas su aiškiais aprašais ir parametrų specifikacijomis. Modelis mato šiuos aprašus savo sistemos užklausoje ir supranta, ką kiekvienas įrankis daro.

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

// Asistentas automatiškai sujungiamas su Spring Boot:
// - ChatModel komponentu
// - Visais @Tool metodais iš @Component klasių
// - ChatMemoryProvider sesijos valdymui
```

Žemiau pateiktame diagramoje išskiriama kiekviena anotacija ir parodyta, kaip kiekvienas elementas padeda DI suprasti, kada kviesti įrankį ir kokius argumentus pateikti:

<img src="../../../translated_images/lt/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Įrankio apibrėžimo anatomija — @Tool nurodo DI, kada jį naudoti, @P aprašo kiekvieną parametrą, o @AiService automatiškai susieja viską paleidimo metu.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ir užduokite klausimus:
> - „Kaip integruočiau tikrą oro sąlygų API, pavyzdžiui, OpenWeatherMap, vietoje modelio sugeneruotų duomenų?“
> - „Kas lemia gerą įrankio aprašą, kuris padeda DI jį tinkamai naudoti?“
> - „Kaip tvarkyti API klaidas ir ribojimus įrankių įgyvendinimuose?“

### Sprendimų priėmimas

Kai vartotojas klausia „Kokios oro sąlygos Sietle?“, modelis neatsitiktinai pasirenka įrankį. Jis lygina vartotojo ketinimą su kiekvieno įrankio aprašymu, vertina jų tinkamumą ir pasirenka geriausiai atitinkantį. Tada generuoja struktūruotą funkcijos kvietimą su tais parametrais — šiuo atveju nustatant `location` reikšmę į `"Seattle"`.

Jei joks įrankis neatitinka vartotojo užklausos, modelis atsako remdamasis savo žiniomis. Jei keli įrankiai atitinka, pasirenkamas specifinis.

<img src="../../../translated_images/lt/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Modelis įvertina kiekvieną galimą įrankį pagal vartotojo ketinimą ir pasirenka geriausiai atitinkantį — todėl svarbu rašyti aiškius, konkrečius įrankių aprašus.*

### Vykdymas

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot automatiškai susieja deklaratyvų `@AiService` sąsają su visais įregistruotais įrankiais, o LangChain4j vykdo įrankių kvietimus automatiškai. Užkulisiuose pilnas įrankio kvietimas praeina per šešis etapus — nuo vartotojo natūralios kalbos klausimo iki natūralios kalbos atsakymo:

<img src="../../../translated_images/lt/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Pilnas procesas — vartotojas pateikia klausimą, modelis pasirenka įrankį, LangChain4j jį vykdo, o modelis įtraukia rezultatą į atsakymą.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ir užduokite klausimus:
> - „Kaip veikia ReAct modelis ir kodėl jis efektyvus DI agentams?“
> - „Kaip agentas nusprendžia, kurį įrankį naudoti ir kokia tvarka?“
> - „Kas nutinka, jei įrankio vykdymas nepavyksta – kaip patikimai tvarkyti klaidas?“

### Atsakymo generavimas

Modelis gauna oro duomenis ir suformuoja juos į natūralios kalbos atsakymą vartotojui.

### Architektūra: Spring Boot automatinis susiejimas

Šis modulis naudoja LangChain4j Spring Boot integraciją su deklaratyviais `@AiService` sąsajomis. Paleidimo metu Spring Boot aptinka kiekvieną `@Component`, turintį `@Tool` metodus, jūsų `ChatModel` komponentą ir `ChatMemoryProvider` — tada jungia juos į vieną `Assistant` sąsają be jokios boilerplate.

<img src="../../../translated_images/lt/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService sąsaja sujungia ChatModel, įrankių komponentus ir atminties tiekėją — Spring Boot tvarko visą susiejimą automatiškai.*

Svarbiausi šio metodo privalumai:

- **Spring Boot automatinis susiejimas** — ChatModel ir įrankiai įtraukiami automatiškai
- **@MemoryId modelis** — automatinis sesijomis pagrįstas atminties valdymas
- **Vienas egzempliorius** — Asistentas sukuriamas vieną kartą ir pakartotinai naudojamas efektyvumui gerinti
- **Tipų saugus vykdymas** — Java metodai kviečiami tiesiogiai su tipų konvertavimu
- **Daugiasluoksnė orkestracija** — Automatinis įrankių grandinavimas
- **Be boilerplate** — Nereikia rankinių `AiServices.builder()` iškvietimų ar atminties HashMap

Alternatyvūs metodai (rankinis `AiServices.builder()`) reikalauja daugiau kodo ir neturi Spring Boot integracijos privalumų.

## Įrankių grandinimas

**Įrankių grandinimas** — tikroji įrankiais paremtų agentų galia atsiskleidžia, kai vienas klausimas reikalauja kelių įrankių. Paklauskite „Kokia temperatūra Sietle Farenheito laipsniais?“ agentas automatiškai susieja du įrankius: pirmiausia iškviečia `getCurrentWeather`, kad gautų temperatūrą Celsijais, tada perduoda tą reikšmę į `celsiusToFahrenheit` konvertavimui — visa tai viename pokalbio etape.

<img src="../../../translated_images/lt/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Įrankių grandinimas veikia — agentas pirmiausia kviečia getCurrentWeather, tada duoda Celsius rezultatą celsiusToFahrenheit ir pateikia sujungtą atsakymą.*

Štai kaip tai atrodo veiksiančioje programoje — agentas viename pokalbio etape grandina du įrankių kvietimus:

<a href="images/tool-chaining.png"><img src="../../../translated_images/lt/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Realus programos rezultatas — agentas automatiškai grandina getCurrentWeather → celsiusToFahrenheit viename etape.*

**Graceful Failures (sklandus klaidų valdymas)** — paklauskite orų miesto, kuris nėra modelio duomenyse. Įrankis grąžina klaidos pranešimą, o DI paaiškina, kad negali padėti, vietoje to, kad sugriūtų. Įrankiai klaidas tvarko saugiai.

<img src="../../../translated_images/lt/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Kai įrankis sugenda, agentas pagavo klaidą ir atsako naudingai paaiškindamas, o ne stringa.*

Visa tai vyksta viename pokalbio etape. Agentas savarankiškai koordinuoja kelis įrankių kvietimus.

## Paleiskite programą

**Patikrinkite diegimą:**

Įsitikinkite, kad pagrindiniame kataloge yra `.env` failas su Azure kredencialais (sukurtas Modulyje 01):
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` iš Modulio 01, šis modulis jau veikia per 8084 prievadą. Galite praleisti žemiau esančias paleidimo komandas ir tiesiogiai nueiti į http://localhost:8084.

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

Dev konteineryje yra Spring Boot Dashboard plėtinys, suteikiantis vizualią sąsają valdyti visas Spring Boot programas. Jį rasite veiklos juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Iš Spring Boot Dashboard galite:
- Matyti visas darbaknygėje esančias Spring Boot programas
- Paleisti / sustabdyti programas vienu paspaudimu
- Rodyti programų žurnalus realiu laiku
- Stebėti programų būseną

Tiesiog spauskite paleidimo mygtuką prie „tools“, kad paleistumėte šį modulį, arba paleiskite visus modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: Naudojant komandinės eilutės scenarijus**

Paleiskite visas interneto programas (moduliai 01-04):

**Bash:**
```bash
cd ..  # Iš pagrindinio katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių katalogų
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

Abi scenarijos automatiškai įkelia aplinkos kintamuosius iš pagrindinio `.env` failo ir sukurs JAR jei jų nėra.

> **Pastaba:** Jei norite rankiniu būdu surinkti visus modulius prieš paleidimą:
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

Atidarykite http://localhost:8084 naršyklėje.

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

Programa siūlo internetinę sąsają, kur galite bendrauti su DI agentu, turinčiu prieigą prie oro sąlygų ir temperatūros konvertavimo įrankių.

<a href="images/tools-homepage.png"><img src="../../../translated_images/lt/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*DI agentų įrankių sąsaja - greiti pavyzdžiai ir pokalbio sąsaja darbui su įrankiais*

### Išbandykite paprastą įrankio naudojimą
Pradėkite nuo paprasto prašymo: „Konvertuokite 100 laipsnių Farenheito į Celsijų“. Agentas supranta, kad jam reikalingas temperatūros konvertavimo įrankis, iškviečia jį su tinkamais parametrais ir pateikia rezultatą. Atkreipkite dėmesį, kaip natūraliai tai atrodo – jūs nenurodėte, kurį įrankį naudoti ar kaip jį iškviesti.

### Įrankių grandinavimas

Dabar pabandykite kažką sudėtingesnio: „Koks oras Sietle ir konvertuokite jį į Farenheitą?“ Stebėkite, kaip agentas žingsnis po žingsnio tai atlieka. Jis pirmiausia gauna orų prognozę (kuri pateikiama Celsijais), supranta, kad reikia konvertuoti į Farenheitą, iškviečia konvertavimo įrankį ir abu rezultatus sujungia į vieną atsakymą.

### Peržiūrėkite pokalbio eigą

Pokalbių sąsaja išlaiko pokalbio istoriją, leidžiant jums vykdyti daugiataukius pokalbius. Matote visus ankstesnius klausimus ir atsakymus, todėl lengva sekti pokalbį ir suprasti, kaip agentas kaupia kontekstą per kelis mainus.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/lt/tools-conversation-demo.89f2ce9676080f59.webp" alt="Pokalbis su keliais įrankių iškvietimais" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Daugiataukis pokalbis, rodantis paprastus konvertavimus, orų paieškas ir įrankių grandinavimą*

### Eksperimentuokite su skirtingais prašymais

Išbandykite įvairius derinius:
- Orų paieškos: „Koks oras Tokijuje?“
- Temperatūros konvertavimai: „Kiek yra 25°C Kelvinuose?“
- Sujungti užklausimai: „Patikrink orą Paryžiuje ir pasakyk, ar temperatūra viršija 20°C“

Pastebėkite, kaip agentas interpretuoja natūralią kalbą ir pritaiko ją tinkamiems įrankių iškvietimams.

## Pagrindinės sąvokos

### ReAct modelis (mąstymas ir veikimas)

Agentas turi ciklą tarp mąstymo (sprendimo, ką daryti) ir veikimo (įrankių naudojimo). Šis modelis leidžia autonomiškai spręsti problemas, o ne tik reaguoti į komandas.

### Įrankių aprašymai yra svarbūs

Jūsų įrankių aprašymų kokybė tiesiogiai lemia, kaip gerai agentas juos naudoja. Aiškūs, konkretūs aprašymai padeda modeliui suprasti, kada ir kaip iškviesti kiekvieną įrankį.

### Sesijos valdymas

`@MemoryId` anotacija leidžia automatiškai valdyti atmintį pagal sesiją. Kiekvienas sesijos ID gauna savo `ChatMemory` egzempliorių, kurį tvarko `ChatMemoryProvider` komponentas, todėl daug vartotojų gali bendrauti su agentu vienu metu, nesimaišant pokalbiams.

<img src="../../../translated_images/lt/session-management.91ad819c6c89c400.webp" alt="Sesijos valdymas su @MemoryId" width="800"/>

*Kiekvienas sesijos ID susieja atskirą pokalbio istoriją – vartotojai niekada nemato vienas kito žinučių.*

### Klaidų tvarkymas

Įrankiai gali sugesti – API gali neužsikrauti, parametrai būti netinkami, išorinės paslaugos gali nestrigti. Gamybiniams agentams reikalingas klaidų tvarkymas, kad modelis galėtų paaiškinti problemą arba bandyti alternatyvas vietoj programos gedimo. Kai įrankis meta išimtį, LangChain4j ją pagauna ir perduoda klaidos pranešimą modeliui, kuris tada natūralia kalba gali paaiškinti problemą.

## Galimi įrankiai

Žemiau pateikta schema parodo plačią įrankių ekosistemą, kurią galite sukurti. Šis modulis demonstruoja orų ir temperatūros įrankius, tačiau tas pats `@Tool` modelis veikia bet kokiam Java metodui – nuo duomenų bazių užklausų iki mokėjimų apdorojimo.

<img src="../../../translated_images/lt/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Įrankių ekosistema" width="800"/>

*Bet kuris Java metodas, anotacija @Tool pažymėtas, tampa pasiekiamas dirbtiniam intelektui – šis modelis taikomas duomenų bazėms, API, el. paštui, failų operacijoms ir kt.*

## Kada naudoti įrankius agentams

<img src="../../../translated_images/lt/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Kada naudoti įrankius" width="800"/>

*Greitas sprendimų vadovas – įrankiai skirti duomenims realiu laiku, skaičiavimams ir veiksmams; bendram žinių pateikimui ir kūrybai jų nereikia.*

**Naudokite įrankius, kai:**
- Atsakymui reikia duomenų realiu laiku (oras, akcijų kainos, inventorius)
- Reikia atlikti sudėtingesnius skaičiavimus nei paprasta matematika
- Prisijungiate prie duomenų bazių ar API
- Atlikti veiksmus (siųsti el. laiškus, kurti užklausas, atnaujinti įrašus)
- Apjungiate kelis duomenų šaltinius

**Nenaudokite įrankių, kai:**
- Atsakymas gali būti pateiktas iš bendrųjų žinių
- Atsakymas yra tik pokalbinis
- Įrankių delsimas daro vartotojo patirtį pernelyg lėtą

## Įrankiai ir RAG

03 ir 04 moduliai abu plečia, ką AI gali padaryti, tačiau iš esmės skirtingai. RAG suteikia modeliui galimybę prieiti prie **žinių** paieškant dokumentų. Įrankiai suteikia modeliui galimybę atlikti **veiksmus** iškviečiant funkcijas.

<img src="../../../translated_images/lt/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Įrankiai vs RAG palyginimas" width="800"/>

*RAG paima informaciją iš statinių dokumentų – įrankiai atlieka veiksmus ir gauna dinamiškus, realaus laiko duomenis. Daugelis gamybinių sistemų naudoja abu metodus.*

Praktikoje daugelis gamybinių sistemų derina abu metodus: RAG, kad atsakymai būtų grindžiami jūsų dokumentacija, ir įrankius, kad gauti gyvus duomenis ar atlikti operacijas.

## Kiti žingsniai

**Kitas modulis:** [05-mcp - Modelio konteksto protokolas (MCP)](../05-mcp/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 03 - RAG](../03-rag/README.md) | [Į pradžią](../README.md) | [Kitas: Modulis 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės atsisakymas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatizuoti vertimai gali būti netikslūs arba turėti klaidų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojamas profesionalus žmogaus vertimas. Mes neatsakome už bet kokius nesusipratimus ar neteisingus interpretavimus, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->