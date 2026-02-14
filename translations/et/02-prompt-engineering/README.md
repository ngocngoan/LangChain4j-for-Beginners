# Moodul 02: Päringu inseneritöö GPT-5.2-ga

## Sisukord

- [Mida sa õpid](../../../02-prompt-engineering)
- [Eeltingimused](../../../02-prompt-engineering)
- [Päringu inseneritöö mõistmine](../../../02-prompt-engineering)
- [Kuidas see kasutab LangChain4j](../../../02-prompt-engineering)
- [Põhimustrid](../../../02-prompt-engineering)
- [Olemasolevate Azure ressursside kasutamine](../../../02-prompt-engineering)
- [Rakenduse ekraanipildid](../../../02-prompt-engineering)
- [Mustrite uurimine](../../../02-prompt-engineering)
  - [Madal vs kõrge innukus](../../../02-prompt-engineering)
  - [Ülesande täitmine (tööriistade eessõnad)](../../../02-prompt-engineering)
  - [Isepeegeldav kood](../../../02-prompt-engineering)
  - [Struktureeritud analüüs](../../../02-prompt-engineering)
  - [Mitme käiguline vestlus](../../../02-prompt-engineering)
  - [Järkjärguline põhjendamine](../../../02-prompt-engineering)
  - [Piiratud väljund](../../../02-prompt-engineering)
- [Mida sa tegelikult õpid](../../../02-prompt-engineering)
- [Järgmised sammud](../../../02-prompt-engineering)

## Mida sa õpid

Eelnevas moodulis nägid, kuidas mälu võimaldab vestluslikku tehisintellekti ja kasutasid GitHubi mudeleid baastasemel suhtluseks. Nüüd keskendume sellele, kuidas sa küsimusi esitad – ehk päringutele endile – kasutades Azure OpenAI GPT-5.2. See, kuidas sa päringud struktureerid, mõjutab oluliselt saadavate vastuste kvaliteeti.

Kasutame GPT-5.2, sest see toob sisse põhjendamise kontrolli – sa saad mudelile öelda, kui palju mõelda enne vastamist. See teeb erinevad päringataktikad selgemaks ja aitab mõista, millal millist lähenemist kasutada. Samuti kasutame Azure’i väiksemaid piiranguid GPT-5.2 jaoks võrreldes GitHubi mudelitega.

## Eeltingimused

- Moodul 01 lõpetatud (Azure OpenAI ressursid juurutatud)
- Juurekataloogis olemas `.env` fail Azure tunnustega (loodud `azd up` käsuga Moodul 01-s)

> **Märkus:** Kui sa pole Moodulit 01 lõpetanud, järgi seal olevaid juurutusjuhiseid esmalt.

## Päringu inseneritöö mõistmine

Päringu inseneritöö tähendab sisendteksti kujundamist nii, et see tagaks järjepidevalt soovitud tulemused. See ei ole lihtsalt küsimuste esitamine – vaid taotluste struktuuri loomine nii, et mudel mõistaks täpselt, mida soovid ja kuidas seda esitada.

Kujuta ette, et annad juhiseid kolleegile. "Paranda viga" on ebaselge. "Paranda UserService.java faile reas 45 nullviite erind, lisades nullkontrolli" on konkreetne. Keelemudelid töötavad samamoodi – täpsus ja struktuur loevad.

## Kuidas see kasutab LangChain4j

See moodul demonstreerib edasijõudnud päringumustreid, kasutades sama LangChain4j alust kui eelnevates moodulites, keskendudes päringute struktuurile ja põhjendamise kontrollile.

<img src="../../../translated_images/et/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j voog" width="800"/>

*Kuidas LangChain4j ühendab sinu päringud Azure OpenAI GPT-5.2-ga*

**Sõltuvused** – Moodul 02 kasutab `pom.xml`-is määratletud järgmisi langchain4j sõltuvusi:  
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
  
**OpenAiOfficialChatModel konfigureerimine** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Vestlusmudelit seadistatakse käsitsi Springi bean-ina OpenAI Official kliendi abil, mis toetab Azure OpenAI otspunkte. Põhiline erinevus Moodul 01-st on selles, kuidas me struktureerime `chatModel.chat()`-ile saadetavaid päringuid, mitte mudeli seadistus.

**Süsteemi ja kasutaja sõnumid** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j eristab sõnumitüüpe selguse huvides. `SystemMessage` määrab tehisintellekti käitumise ja konteksti (nt "Sa oled koodi ülevaataja"), samas kui `UserMessage` sisaldab tegelikku päringut. See eristus võimaldab hoida ühtlast AI käitumist erinevate kasutajapäringute puhul.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/et/message-types.93e0779798a17c9d.webp" alt="Sõnumitüüpide arhitektuur" width="800"/>

*SystemMessage annab püsiva konteksti, UserMessage'd sisaldavad üksikuid päringuid*

**MessageWindowChatMemory mitmekäiguliseks vestluseks** – Mitmekäiguliste vestluste mustri puhul taaskasutame Moodul 01-st `MessageWindowChatMemory`-d. Iga sessioon saab oma mälukoe, mis hoitakse `Map<String, ChatMemory>`-s, võimaldades mitut samaaegset vestlust ilma konteksti segamiseta.

**Päringu mallid** – Tegelik fookus on päringu inseneritööl, mitte uutel LangChain4j API-del. Iga muster (madal innukus, kõrge innukus, ülesande täitmine jm) kasutab sama `chatModel.chat(prompt)` meetodit, kuid hoolikalt struktureeritud päringustringidega. XML-tähed, juhised ja vormindus on kõik osa päringutekstist, mitte LangChain4j omadused.

**Põhjendamise kontroll** – GPT-5.2 põhjendamise pingutust juhitakse päringu juhistega nagu "maksimaalselt 2 põhjendusastet" või "uurida põhjalikult". Need on päringu inseneritöö tehnikad, mitte LangChain4j seadistused. Teek lihtsalt edastab sinu päringud mudelile.

Peamine sõnum: LangChain4j pakub infrastruktuuri (mudeli ühendus [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), mälu ja sõnumite haldus [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) kaudu), samal ajal kui see moodul õpetab sind looma tõhusaid päringuid selle infrastruktuuris.

## Põhimustrid

Kõik probleemid ei vaja sama lähenemist. Mõned küsimused vajavad kiireid vastuseid, teised süvateadmist. Mõned vajavad nähtavat põhjendamist, teised ainult tulemusi. See moodul katab kaheksa päringu mustrit – igaüks optimeeritud erinevateks olukordadeks. Proovi kõiki, et teada saada, millal milline lähenemine töötab kõige paremini.

<img src="../../../translated_images/et/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Kaheksa päringu mustrit" width="800"/>

*Ülevaade kaheksast päringu inseneritöö mustrist ja nende kasutusjuhtudest*

<img src="../../../translated_images/et/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Põhjendamise pingutuse võrdlus" width="800"/>

*Madal innukus (kiire, otsene) vs kõrge innukus (põhjalik, uuriv) põhjendamisstrateegiad*

**Madal innukus (kiire ja sihipärane)** – Lihtsate küsimuste jaoks, kus soovid kiiresti otsest vastust. Mudel teeb minimaalset põhjendamist – maksimaalselt 2 etappi. Kasuta seda arvutusteks, otsinguteks või lihtsateks küsimusteks.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Uuri koos GitHub Copilotiga:** Ava [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ja küsi:  
> - "Mis vahe on madala ja kõrge innukusega päringu mustritel?"  
> - "Kuidas XML-tähed päringutes aitavad AI vastust struktureerida?"  
> - "Millal peaksin kasutama isepeegeldamise mustreid ja millal otsest juhist?"

**Kõrge innukus (sügav ja põhjalik)** – Komplekssed probleemid, kus soovid põhjalikku analüüsi. Mudel uurib põhjalikult ja näitab detailset põhjendamist. Kasuta seda süsteemi kavandamiseks, arhitektuuriliste otsuste või keerulise uurimistöö jaoks.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Ülesande täitmine (edusammud samm-sammult)** – Mitmesammuliste töövoogude jaoks. Mudel annab esmase plaani, kirjeldab samme töötamise ajal ja annab kokkuvõtte. Kasuta seda migratsioonide, elluviimiste või muu mitme sammu puhul.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Chain-of-Thought (mõttekäigu) päringud paluvad mudelil näidata oma põhjendusprotsessi, parandades täpsust keerulistel ülesannetel. Samm-sammuline lahtijoonistamine aitab nii inimestel kui tehisintellektil loogikat mõista.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Küsi selle mustri kohta:  
> - "Kuidas kohandada ülesande täitmise mustrit pikkade operatsioonide jaoks?"  
> - "Millised on parimad praktikad tööriistade eessõnade struktureerimiseks tootmistasemel rakendustes?"  
> - "Kuidas püüda ja kuvada kasutajaliideses vahepealseid edenemisteateid?"

<img src="../../../translated_images/et/task-execution-pattern.9da3967750ab5c1e.webp" alt="Ülesande täitmise muster" width="800"/>

*Plaani → Tõrjuta → Kokkuvõtte töövoog mitmesammuliste ülesannete jaoks*

**Isepeegeldav kood** – Tootmistasemel koodi genereerimine. Mudel genereerib koodi, kontrollib seda kvaliteedikriteeriumite alusel ja parandab iteratiivselt. Kasuta seda uute funktsioonide või teenuste loomiseks.

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
  
<img src="../../../translated_images/et/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Isepeegeldamise tsükkel" width="800"/>

*Iteratiivne täiustustsükkel – genereeri, hinda, tuvastatud probleemid, paranda, korda*

**Struktureeritud analüüs** – Järjepideva hindamise jaoks. Mudel vaatab koodi läbi fikseeritud raamistiku (õigsus, praktikad, jõudlus, turvalisus). Kasuta seda koodi ülevaatustele või kvaliteedi hindamiseks.

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
  
> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Küsi struktureeritud analüüsi kohta:  
> - "Kuidas kohandada analüüsiraamistikku erinevat tüüpi koodi ülevaadete jaoks?"  
> - "Mis on parim viis struktureeritud väljundi programmeerimiseks tõlgendamiseks ja töötlemiseks?"  
> - "Kuidas tagada järjepidevad kriitilisusastmed erinevate ülevaatussessioonide vahel?"

<img src="../../../translated_images/et/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Struktureeritud analüüsi muster" width="800"/>

*Nelikategoriiline raamistik järjepidevate koodikontrollide ja kriitilisusastega*

**Mitme käiguline vestlus** – Vestluste jaoks, mis vajavad konteksti. Mudel mäletab eelnevaid sõnumeid ja ehitab nende põhjal edasi. Kasuta seda interaktiivseks toeks või keeruliseks Q&A-ks.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/et/context-memory.dff30ad9fa78832a.webp" alt="Konteksti mälu" width="800"/>

*Kuidas vestluse kontekst koguneb mitme käigu jooksul kuni tokenipiirini*

**Järkjärguline põhjendamine** – Probleemidele, mis vajavad nähtavat loogikat. Mudel näitab iga sammu selget põhjendust. Kasuta seda matemaatikaülesannete, loogikapõrsaste või mõtlemisprotsessi mõistmise puhul.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/et/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Järkjärgulise mustri kujutis" width="800"/>

*Probleemide lagundamine selgeteks loogilisteks sammudeks*

**Piiratud väljund** – Vastustele rangete formaadi nõuete puhul. Mudel järgib rangelt vormi ja pikkuse reegleid. Kasuta seda kokkuvõtete jaoks või kui vajad täpset väljundistruktuuri.

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
  
<img src="../../../translated_images/et/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Piiratud väljundi muster" width="800"/>

*Erinõuete, pikkuse ja struktureerimise sundimine*

## Olemasolevate Azure ressursside kasutamine

**Kontrolli juurutust:**

Veendu, et root-kataloogis on `.env`-fail Azure tunnustega (loodud Moodul 01 käigus):  
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_VÕTI, JUURUTUS
```
  
**Alusta rakendust:**

> **Märkus:** Kui alustasid juba kõigi rakenduste käivitamist `./start-all.sh`-ga Moodul 01-st, siis see moodul töötab juba pordil 8083. Võid alljärgnevad käivitamiskäsud vahele jätta ja minna otse http://localhost:8083

**Valik 1: Kasutades Spring Boot Dashboardi (soovitatav VS Code kasutajatele)**

Dev konteiner sisaldab Spring Boot Dashboardi laiendust, mis annab visuaalse liidese kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code’i vasakul küljeribal (otsi Spring Boot ikooni).

Spring Boot Dashboardilt saad:  
- Vaadata kõiki tööruumis olevaid Spring Boot rakendusi  
- Alustada/peatada rakendusi ühe klikiga  
- Vaadata rakenduste logisid reaalajas  
- Jälgida rakenduste olekut

Lihtsalt kliki "prompt-engineering" kõrval olevat mängimise nuppu selle mooduli käivitamiseks või käivita kõik moodulid korraga.

<img src="../../../translated_images/et/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Kasutades shell skripte**

Alusta kõiki veebirakendusi (moodulid 01-04):

**Bash:**  
```bash
cd ..  # Juurkaustast
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Juure kataloogist
.\start-all.ps1
```
  
Või alusta ainult seda moodulit:

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
  
Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurest `.env` failist ja ehitavad JAR-id kui neid veel pole.

> **Märkus:** Kui eelistad ehitada kõik moodulid käsitsi enne käivitamist:  
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
  
Ava http://localhost:8083 oma veebilehitsejas.

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
  
## Rakenduse ekraanipildid

<img src="../../../translated_images/et/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard avaleht" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Põhidashboard, kus kuvatakse kõik 8 päringu inseneritöö mustrit nende omaduste ja kasutusjuhtudega*

## Mustrite uurimine

Veebi liides laseb sul katsetada erinevaid päringustrateegiaid. Iga muster lahendab erinevaid probleeme – proovi neid, et näha, millal milline lähenemine toimib parimalt.

### Madal vs kõrge innukus

Esita lihtne küsimus nagu "Mis on 15% 200-st?" kasutades Madalat innukust. Saad kohese ja otsese vastuse. Nüüd küsi midagi keerulist nagu "Tee kõrge koormusega API jaoks vahemällu salvestamise strateegia" kasutades Kõrget innukust. Vaata, kuidas mudel aeglustab ja annab detailse põhjenduse. Sama mudel, sama küsimuse struktuur – aga päring ütleb, kui palju mõelda.
<img src="../../../translated_images/et/low-eagerness-demo.898894591fb23aa0.webp" alt="Madala innukuse demo" width="800"/>

*Kiire arvutus minimaalse põhjuslikkusega*

<img src="../../../translated_images/et/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Kõrge innukuse demo" width="800"/>

*Terviklik vahemälu strateegia (2.8MB)*

### Ülesande täitmine (tööriistade sissejuhatused)

Mitmeastmelised töövood saavad kasu ette planeerimisest ja edenemise jutustamisest. Mudel kirjeldab, mida teeb, jutustab iga sammu läbi ja võtab tulemused kokku.

<img src="../../../translated_images/et/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Ülesande täitmise demo" width="800"/>

*REST-punkti loomine samm-sammult jutustades (3.9MB)*

### Enesepeegelduv kood

Proovi "Loo e-posti valideerimisteenus". Selle asemel, et lihtsalt koodi genereerida ja peatuda, genereerib mudel, hindab kvaliteedikriteeriumide alusel, tuvastab nõrkused ja parandab. Sa näed kordusi, kuni kood vastab tootmistasemele.

<img src="../../../translated_images/et/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Enesepeegeldava koodi demo" width="800"/>

*Täielik e-posti valideerimisteenus (5.2MB)*

### Struktureeritud analüüs

Koodikontrollid vajavad järjepidevaid hindamisraamistikke. Mudel analüüsib koodi fikseeritud kategooriate (õigsus, praktikad, jõudlus, turvalisus) ja raskusastmete alusel.

<img src="../../../translated_images/et/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Struktureeritud analüüsi demo" width="800"/>

*Raamistiku põhine koodikontroll*

### Mitmekäiguline suhtlus

Küsi "Mis on Spring Boot?" ja kohe pärast seda "Näita mulle näidet". Mudel mäletab su esimest küsimust ja pakub just Spring Booti näidet. Ilma mäluta oleks see teine küsimus liiga üldine.

<img src="../../../translated_images/et/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Mitmekäigulise vestluse demo" width="800"/>

*Konteksti säilitamine küsimuste vahel*

### Samm-sammuline põhjendus

Vali matemaatiline ülesanne ja proovi seda nii samm-sammulise põhjendusega kui ka madala innukusega. Madal innukus annab ainult vastuse – kiire, aga ebaselge. Samm-sammuline näitab iga arvutust ja otsust.

<img src="../../../translated_images/et/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Samm-sammuline põhjenduse demo" width="800"/>

*Matemaatiline ülesanne selgete sammudega*

### Piiratud väljund

Kui vajad konkreetseid vorminguid või sõnade arvu, sunnib see muster neid rangelt järgima. Proovi genereerida kokkuvõte täpselt 100 sõnaga punktide vormis.

<img src="../../../translated_images/et/constrained-output-demo.567cc45b75da1633.webp" alt="Piiratud väljundi demo" width="800"/>

*Masinõppe kokkuvõte vormingu kontrolliga*

## Mida Sa Tõeliselt Õpid

**Põhjendamise pingutus muudab kõike**

GPT-5.2 võimaldab sul juhtida arvutuspingutust oma päringute kaudu. Madal pingutus tähendab kiireid vastuseid minimaalse uurimisega. Kõrge pingutus tähendab, et mudel võtab aega sügavamaks mõtlemiseks. Õpid sobitama pingutust ülesande keerukusega – ära raiska aega lihtsatele küsimustele, aga ära ka kiirusta keeruliste otsuste juures.

**Struktuur juhib käitumist**

Kas märkasid XML-tägit küsimustes? Need pole dekoratiivsed. Mudelid järgivad struktureeritud juhiseid usaldusväärsemalt kui vabavormilist teksti. Kui vajad mitmeastmelisi protsesse või keerukat loogikat, aitab struktuur mudelil jälgida, kus ta parasjagu on ja mis järgmisena tuleb.

<img src="../../../translated_images/et/prompt-structure.a77763d63f4e2f89.webp" alt="Päringu struktuur" width="800"/>

*Hästi struktureeritud päringu anatoomia selgete osade ja XML-tüüpi organiseerimisega*

**Kvaliteet läbi enesehindamise**

Enesepeegelduvad mustrid töötavad, muutes kvaliteedikriteeriumid selgeks. Selle asemel, et loota mudelil "korralikult tegemisele", ütled täpselt, mida "korralik" tähendab: korrektne loogika, veahaldus, jõudlus, turvalisus. Mudel saab siis enda väljundit hinnata ja parandada. See muudab koodigeneratsiooni loteriist protsessiks.

**Kontekst on piiratud**

Mitmekäigulised vestlused toimivad, lisades päringutele sõnumi ajalugu. Kuid on olemas piirang – igal mudelil on maksimaalne tokenite arv. Kui vestlused kasvavad, vajad strateegiaid, kuidas hoida olulist konteksti ilma katusepiiri ületamata. See moodul näitab, kuidas mälu töötab; hiljem õpid, millal kokku võtta, millal unustada ja millal otsida.

## Järgmised Sammud

**Järgmine moodul:** [03-rag - RAG (otsingut toetav genereerimine)](../03-rag/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 01 - Sissejuhatus](../01-introduction/README.md) | [Tagasi peamenüüsse](../README.md) | [Järgmine: Moodul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument oma emakeeles tuleks lugeda autoriteetseks allikaks. Olulise teabe korral soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->