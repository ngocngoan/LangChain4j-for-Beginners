# Modulis 02: Promptų inžinerija su GPT-5.2

## Turinys

- [Ko išmoksite](../../../02-prompt-engineering)
- [Reikalavimai](../../../02-prompt-engineering)
- [Promptų inžinerijos supratimas](../../../02-prompt-engineering)
- [Kaip čia naudojamas LangChain4j](../../../02-prompt-engineering)
- [Pagrindiniai modeliai](../../../02-prompt-engineering)
- [Esamų Azure išteklių naudojimas](../../../02-prompt-engineering)
- [Programos ekrano nuotraukos](../../../02-prompt-engineering)
- [Modelių tyrinėjimas](../../../02-prompt-engineering)
  - [Mažas vs didelis entuziazmas](../../../02-prompt-engineering)
  - [Užduoties vykdymas (įrankių įžangos)](../../../02-prompt-engineering)
  - [Savianalizės kodas](../../../02-prompt-engineering)
  - [Struktūrizuota analizė](../../../02-prompt-engineering)
  - [Daugiažingsnė pokalbio sesija](../../../02-prompt-engineering)
  - [Žingsnis po žingsnio samprotavimas](../../../02-prompt-engineering)
  - [Apribotas išvestis](../../../02-prompt-engineering)
- [Ko iš tikrųjų išmokstate](../../../02-prompt-engineering)
- [Kitos žingsniai](../../../02-prompt-engineering)

## Ko išmoksite

Antrame modulyje pamatysite, kaip atmintis leidžia bendravimo dirbtiniam intelektui ir kaip naudoti GitHub modelius pagrindinėms sąveikoms. Dabar sutelksime dėmesį į tai, kaip užduoti klausimus – pačius promptus – naudojant Azure OpenAI GPT-5.2. Promptų struktūra reikšmingai lemia gaunamų atsakymų kokybę.

Naudosime GPT-5.2, nes jis įveda samprotavimo kontrolę – galite modelį nurodyti, kiek mąstymo atlikti prieš atsakant. Tai padaro skirtingas prompting strategijas labiau pastebimas ir padeda suprasti, kada naudoti kurią. Taip pat pasinaudosime Azure mažesniais spartos apribojimais GPT-5.2 atžvilgiu, palyginti su GitHub modeliais.

## Reikalavimai

- Baigtas Modulis 01 (įdiegti Azure OpenAI ištekliai)  
- Šakniniame kataloge esantis `.env` failas su Azure prisijungimo duomenimis (sukurtas naudojant `azd up` 1 modulyje)

> **Pastaba:** Jei nesate pabaigę Modulio 01, pirmiausia atlikite ten pateiktas diegimo instrukcijas.

## Promptų inžinerijos supratimas

Promptų inžinerija – tai įvesties teksto dizainas, kuris nuosekliai duoda reikiamus rezultatus. Tai ne tik klausimų uždavimas – tai užklausų struktūrizavimas, kad modelis tiksliai suprastų, ko norite ir kaip tai pateikti.

Įsivaizduokite, kad duodate instrukcijas kolegai. „Pataisyk klaidą“ yra neaišku. „Pataisyk null pointer išimtį faile UserService.java eilutėje 45 pridėdamas nulio patikrinimą“ yra konkrečiau. Kalbos modeliai veikia taip pat – svarbi specifika ir struktūra.

## Kaip čia naudojamas LangChain4j

Šis modulis demonstruoja pažangius promptų modelius, naudojant tą patį LangChain4j pagrindą iš ankstesnių modulių, daugiausia dėmesio skiriant promptų struktūrai ir samprotavimo kontrolei.

<img src="../../../translated_images/lt/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Kaip LangChain4j jungia jūsų promptus prie Azure OpenAI GPT-5.2*

**Priklausomybės** – Modulis 02 naudoja šias langchain4j priklausomybes, apibrėžtas `pom.xml`:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
  
**OpenAiOfficialChatModel konfigūracija** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Pokalbio modelis rankiniu būdu konfigūruojamas kaip Spring bean naudojant oficialų OpenAI klientą, kuris palaiko Azure OpenAI galinius taškus. Pagrindinis skirtumas nuo Modulio 01 yra ne modelio nustatyme, o kaip struktūrizuojami promptai, siunčiami `chatModel.chat()`.

**Sistemos ir vartotojo žinutės** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j atskiria žinučių tipus aiškumui. `SystemMessage` nustato DI elgesį ir kontekstą (pvz., „Jūs esate kodo peržiūros specialistas“), o `UserMessage` – faktinį užklausą. Ši atskirtis leidžia palaikyti nuoseklų DI elgesį skirtingų vartotojų užklausų metu.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/lt/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage suteikia nuolatinį kontekstą, o UserMessages užpildo pavienes užklausas*

**MessageWindowChatMemory daugiažingsniams pokalbiams** – Daugiažingsniam pokalbio modelio atvejui naudojame `MessageWindowChatMemory` iš Modulio 01. Kiekviena sesija turi savo atminties egzempliorių saugomą `Map<String, ChatMemory>`, leidžiant kelis vienu metu vykstančius pokalbius be konteksto susimaišymo.

**Promptų šablonai** – Šioje dalyje pagrindinis dėmesys skiriamas promptų inžinerijai, o ne LangChain4j naujoms API. Kiekvienas modelis (mažas entuziazmas, didelis entuziazmas, užduoties vykdymas ir kt.) naudoja tą patį `chatModel.chat(prompt)` metodą, tačiau su kruopščiai struktūrizuotais promptų tekstais. XML žymos, instrukcijos ir formatavimas yra dalis prompto teksto, o ne LangChain4j funkcijos.

**Samprotavimo kontrolė** – GPT-5.2 samprotavimo sunkumas valdomas per prompto instrukcijas, tokias kaip „maksimaliai 2 samprotavimo žingsniai“ arba „išsamiai išnagrinėk“. Tai promptų inžinerijos technikos, o ne LangChain4j konfigūracijos. Biblioteka tiesiog pristato jūsų promptus modeliui.

Svarbiausia išvada: LangChain4j suteikia infrastruktūrą (modelio jungtį per [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), atmintį, žinučių valdymą per [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), o šis modulis moko, kaip kurti efektyvius promptus naudojant šią infrastruktūrą.

## Pagrindiniai modeliai

Ne visiems klausimams reikia tokio paties požiūrio. Kai kuriems klausimams reikia greitų atsakymų, kitiems – gilaus mąstymo. Vieniems reikia matomo samprotavimo, kitiems – tiesiog rezultatų. Šis modulis apima aštuonis promptų modelius – kiekvienas optimizuotas skirtingoms situacijoms. Eksperimentuosite su visais, kad suprastumėte, kada veikia kuris.

<img src="../../../translated_images/lt/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Aštuoni promptų inžinerijos modelių apžvalga ir jų panaudojimo atvejai*

<img src="../../../translated_images/lt/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Mažas entuziazmas (greitas, tiesioginis) prieš Didelis entuziazmas (išsamus, tyrinėjančius) samprotavimo metodai*

**Mažas entuziazmas (greita ir fokusuota)** – Paprastiems klausimams, kai norite greito ir tiesioginio atsakymo. Modelis atlieka minimalų samprotavimą – maksimaliai 2 žingsnius. Naudokite skaičiavimams, duomenų paieškai ar paprastiems klausimams.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Išbandykite su GitHub Copilot:** Atidarykite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ir paklauskite:  
> - „Kuo skiriasi mažo ir didelio entuziazmo promptų modeliai?“  
> - „Kaip XML žymos promptuose padeda struktūrizuoti DI atsakymą?“  
> - „Kada naudoti savianalizės modelius, o kada tiesiogines instrukcijas?“

**Didelis entuziazmas (gilus ir išsamus)** – Kompleksinėms problemoms, kai reikia išsamios analizės. Modelis išsamiai nagrinėja ir rodo detalius samprotavimus. Naudokite sistemų projektavimui, architektūros sprendimams ar sudėtingiems tyrimams.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Užduoties vykdymas (žingsnis po žingsnio progresas)** – Daugiažingsniams procesams. Modelis pateikia išankstinį planą, pasakoja apie kiekvieną žingsnį ir pateikia santrauką. Naudokite migracijoms, įgyvendinimams ar bet kokiam daugiažingsniam procesui.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Chain-of-Thought promptai aiškiai nurodo modeliui parodyti samprotavimo procesą, gerindami tikslumą sudėtingose užduotyse. Žingsnis po žingsnio analizė padeda tiek žmonėms, tiek DI suprasti logiką.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Paklauskite apie šį modelį:  
> - „Kaip pritaikyti užduoties vykdymo modelį ilgai trunkančioms operacijoms?“  
> - „Kokios geriausios praktikos įrankių įžangų struktūrizavime produkcinėse programose?“  
> - „Kaip fiksuoti ir rodyti tarpinio progreso atnaujinimus vartotojo sąsajoje?“

<img src="../../../translated_images/lt/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planavimas → Vykdymas → Santrauka daugiažingsniams darbams*

**Savianalizės kodas** – Naujos funkcijos ar servisų kūrimui. Modelis generuoja kodą, tikrina pagal kokybės kriterijus ir tobulina iteratyviai. Naudokite, kai kuriate produkcinės kokybės kodus.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/lt/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iteratyvus tobulinimas – generavimas, vertinimas, problemų nustatymas, tobulinimas, kartojimas*

**Struktūrizuota analizė** – Nuosekliai įvertinti. Modelis peržiūri kodą pagal fiksuotą rėmą (teisingumas, praktikos, našumas, saugumas). Naudokite kodo peržiūroms ar kokybės įvertinimams.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Paklauskite apie struktūrizuotą analizę:  
> - „Kaip pritaikyti analizės rėmą skirtingų tipų kode?“  
> - „Kokia geriausia praktika analizuotą struktūrizuotą išvestį apdoroti programiškai?“  
> - „Kaip užtikrinti nuoseklias sunkumo lygius skirtingose peržiūrų sesijose?“

<img src="../../../translated_images/lt/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Keturių kategorijų sistema nuoseklioms kodo peržiūroms su sunkumo lygiais*

**Daugiažingsnė pokalbio sesija** – Pokalbiams, kuriems reikalingas kontekstas. Modelis prisimena ankstesnes žinutes ir vysto jas. Naudokite interaktyvioms pagalbos sesijoms ar sudėtingiems klausimams–atsakymams.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/lt/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kaip pokalbio kontekstas kaupiasi per kelis žingsnius iki ženklų limito*

**Žingsnis po žingsnio samprotavimas** – Problemoms, kurioms reikia matomo logikos aiškinimo. Modelis parodo aiškų samprotavimą kiekviename žingsnyje. Naudokite matematikos uždaviniams, loginėms dėlionėms ar kai reikia suprasti mąstymo procesą.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/lt/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Problemos suskaidymas į aiškius loginius žingsnius*

**Apribotas išvestis** – Atsakymams su specifiniais formato reikalavimais. Modelis griežtai laikosi formato ir apimties taisyklių. Naudokite santraukoms arba kai reikia preciziškos išvesties struktūros.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/lt/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Specifinių formato, apimties ir struktūros reikalavimų užtikrinimas*

## Esamų Azure išteklių naudojimas

**Patikrinkite diegimą:**

Įsitikinkite, kad šakniniame kataloge yra `.env` failas su Azure prisijungimo duomenimis (sukurtas Modulio 01 metu):  
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Paleiskite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudodami `./start-all.sh` Modulyje 01, šis modulis jau veikia prievade 8083. Galite praleisti žemiau nurodytas paleidimo komandas ir eiti tiesiai į http://localhost:8083.

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code naudotojams)**

Dev konteineryje yra Spring Boot Dashboard plėtinys, suteikiantis grafinę sąsają visiems Spring Boot programoms valdyti. Jį rasite kairiojoje VS Code veiksmų juostoje (ieškokite Spring Boot ikonėlės).

Per Spring Boot Dashboard galite:  
- Matyti visas darbo vietos Spring Boot programas  
- Vienu paspaudimu paleisti/stabdyti programas  
- Realiu laiku stebėti programos žurnalus  
- Sekti programos būseną

Tiesiog spustelėkite paleidimo mygtuką prie „prompt-engineering“, kad paleistumėte šį modulį, arba paleiskite visas modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: Naudojant komandų eilutės skriptus**

Paleiskite visas žiniatinklio programas (modulius 01–04):

**Bash:**  
```bash
cd ..  # Iš šakninių katalogų
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
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Abu skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų dar nėra.

> **Pastaba:** Jei norite visus modulius sukompiliuoti rankiniu būdu prieš paleidimą:  
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
  
Atidarykite http://localhost:8083 naršyklėje.

**Norint sustabdyti:**

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


## Programos ekrano nuotraukos

<img src="../../../translated_images/lt/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Pagrindinis valdymo pultas, rodantis visus 8 promptų inžinerijos modelius su jų charakteristikomis ir panaudojimo atvejais*

## Modelių tyrinėjimas

Žiniatinklio sąsaja leidžia eksperimentuoti su skirtingomis prompting strategijomis. Kiekvienas modelis sprendžia skirtingas problemas – išbandykite juos, kad pamatytumėte, kada kuris efektyviausias.

### Mažas vs Didelis entuziazmas

Užduokite paprastą klausimą, pavyzdžiui, „Kiek yra 15% iš 200?“ naudodami Mažą entuziazmą. Gaunate momentinį, tiesioginį atsakymą. Dabar užduokite sudėtingesnį klausimą, pvz., „Sukurkite kešavimo strategiją didelio srauto API“ naudodami Didelį entuziazmą. Stebėkite, kaip modelis sulėtėja ir pateikia išsamius samprotavimus. Tas pats modelis, ta pati klausimo struktūra – bet promptas nurodo, kiek mąstyti.
<img src="../../../translated_images/lt/low-eagerness-demo.898894591fb23aa0.webp" alt="Žemas entuziazmas Demo" width="800"/>

*Greitas skaičiavimas su minimaliu samprotavimu*

<img src="../../../translated_images/lt/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Aukštas entuziazmas Demo" width="800"/>

*Išsamus kešo valdymo strategija (2.8MB)*

### Užduoties vykdymas (įrankių pradmenys)

Daugiapakopiai darbo eiga pasitarnauja išankstinis planavimas ir pažangos pasakojimas. Modelis aprašo, ką darys, pasakoja kiekvieną žingsnį, tada apibendrina rezultatus.

<img src="../../../translated_images/lt/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Užduoties vykdymo demo" width="800"/>

*Kuriamas REST galinis taškas su žingsnis po žingsnio pasakojimu (3.9MB)*

### Savianalizinis kodas

Išbandykite „Sukurti el. pašto validavimo paslaugą“. Vietoj to, kad tiesiog generuotų kodą ir sustotų, modelis generuoja, vertina pagal kokybės kriterijus, identifikuoja silpnybes ir gerina. Matysite, kaip jis toliau tobulėja, kol kodas atitinka gamybos standartus.

<img src="../../../translated_images/lt/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Savianalizinis kodo demo" width="800"/>

*Pilna el. pašto validavimo paslauga (5.2MB)*

### Struktūrizuota analizė

Kodo peržiūroms reikalingos nuoseklios vertinimo sistemos. Modelis analizuoja kodą naudodamas fiksuotas kategorijas (teisingumas, praktikos, našumas, saugumas) su sunkumo lygiais.

<img src="../../../translated_images/lt/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Struktūrizuotos analizės demo" width="800"/>

*Kodo peržiūra pagal sistemą*

### Daugkartinės pokalbio raundai

Paklauskite „Kas yra Spring Boot?“ ir tuoj pat pridėkite „Parodyk man pavyzdį“. Modelis prisimena jūsų pirmą klausimą ir pateikia konkrečią Spring Boot pavyzdį. Be atminties antras klausimas būtų per abstraktus.

<img src="../../../translated_images/lt/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Daugkartinio pokalbio demo" width="800"/>

*Konteksto išsaugojimas tarp klausimų*

### Žingsnis po žingsnio samprotavimas

Pasirinkite matematikos uždavinį ir išbandykite tiek Žingsnis po žingsnio samprotavimą, tiek Žemą entuziazmą. Žemas entuziazmas pateikia tik atsakymą – greitai, bet neaiškiai. Žingsnis po žingsnio rodo visus skaičiavimus ir sprendimus.

<img src="../../../translated_images/lt/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Žingsnis po žingsnio samprotavimo demo" width="800"/>

*Matematikos uždavinys su aiškiais žingsniais*

### Ribotas išvesties formatas

Kai reikia specifinių formatų ar žodžių skaičiaus, ši schema užtikrina griežtą reikalavimų laikymąsi. Išbandykite sukurti santrauką, turinčią tiksliai 100 žodžių su punktų formatu.

<img src="../../../translated_images/lt/constrained-output-demo.567cc45b75da1633.webp" alt="Riboto išvesties demo" width="800"/>

*Mašininio mokymosi santrauka su formato kontrole*

## Ką jūs iš tikrųjų mokotės

**Samprotavimo pastangos keičia viską**

GPT-5.2 leidžia jums valdyti skaičiavimo pastangas per užklausas. Mažos pastangos reiškia greitus atsakymus su minimaliu tyrimu. Didelės pastangos reiškia, kad modelis praleidžia daugiau laiko giliau mąstydamas. Jūs mokotės suderinti pastangas su užduoties sudėtingumu – nešvaistykite laiko paprastiems klausimams, bet ir neskubėkite priimti sudėtingų sprendimų.

**Struktūra nukreipia elgesį**

Pastebėjote XML žymes užklausose? Jos nėra dekoratyvios. Modeliai labiau pasitiki struktūruotomis instrukcijomis nei laisvu tekstu. Kai reikia daugiapakopių procesų ar sudėtingos logikos, struktūra padeda modeliui sekti, kur jis yra ir kas bus toliau.

<img src="../../../translated_images/lt/prompt-structure.a77763d63f4e2f89.webp" alt="Užklausos struktūra" width="800"/>

*Gerai struktūruotos užklausos anatominis vaizdas su aiškiomis dalimis ir XML stiliaus organizacija*

**Kokybė per savarankišką vertinimą**

Savianalizės modeliai veikia, aiškiai nurodydami kokybės kriterijus. Vietoj vilties, kad modelis „padarys teisingai“, jūs tiksliai pasakote, ką reiškia „teisingai“: teisinga logika, klaidų valdymas, našumas, saugumas. Modelis tada gali įvertinti savo išvestį ir pagerinti ją. Tai paverčia kodo generavimą iš loterijos į procesą.

**Kontekstas yra ribotas**

Daugiaraujai pokalbiai veikia įtraukdami pranešimų istoriją į kiekvieną užklausą. Tačiau yra riba – kiekvienas modelis turi maksimalų žodžių skaičių. Augant pokalbiams, jums reikės strategijų išlaikyti svarbią kontekstą neviršijant ribos. Ši modulis parodys, kaip veikia atmintis; vėliau mokysitės, kada apibendrinti, kada pamiršti ir kada atkurti informaciją.

## Kitas žingsnis

**Kitas Modulis:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 01 – Įvadas](../01-introduction/README.md) | [Atgal į pagrindinį](../README.md) | [Toliau: Modulis 03 – RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Kritiniais atvejais rekomenduojama naudoti profesionalų žmogaus vertimą. Mes neatsakome už jokią neteisingą supratimą ar klaidingą interpretavimą, kylančius iš šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->