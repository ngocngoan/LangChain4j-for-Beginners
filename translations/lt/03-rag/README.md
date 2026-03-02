# Modulis 03: RAG (Retrieval-Augmented Generation)

## Turinys

- [Video vedlys](../../../03-rag)
- [Ko išmoksite](../../../03-rag)
- [Reikalingos sąlygos](../../../03-rag)
- [RAG supratimas](../../../03-rag)
  - [Kurią RAG metodiką naudoja šis vadovas?](../../../03-rag)
- [Kaip tai veikia](../../../03-rag)
  - [Dokumento apdorojimas](../../../03-rag)
  - [Įterpinių kūrimas](../../../03-rag)
  - [Semantinė paieška](../../../03-rag)
  - [Atsakymo generavimas](../../../03-rag)
- [Paleisti programą](../../../03-rag)
- [Naudojimasis programa](../../../03-rag)
  - [Įkelti dokumentą](../../../03-rag)
  - [Uždavinėti klausimus](../../../03-rag)
  - [Tikrinti šaltinių nuorodas](../../../03-rag)
  - [Eksperimentuoti su klausimais](../../../03-rag)
- [Pagrindinės sąvokos](../../../03-rag)
  - [Dalių strategija](../../../03-rag)
  - [Panašumo balai](../../../03-rag)
  - [Atminties saugykla](../../../03-rag)
  - [Konteksto lango valdymas](../../../03-rag)
- [Kada RAG svarbus](../../../03-rag)
- [Tolimesni žingsniai](../../../03-rag)

## Video vedlys

Peržiūrėkite šią tiesioginę sesiją, kuri paaiškina, kaip pradėti dirbti su šiuo moduliu:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Ko išmoksite

Ankstesniuose moduliuose išmokote, kaip kalbėtis su DI ir efektyviai struktūruoti savo užklausas. Tačiau yra fundamentali apribojimas: kalbos modeliai žino tik tai, ką išmoksta treniruočių metu. Jie negali atsakyti į klausimus apie jūsų įmonės politikas, jūsų projekto dokumentaciją ar bet kokią informaciją, kurios mokyti nebuvo.

RAG (retrieval-augmented generation) išsprendžia šią problemą. Vietoje bandymo mokyti modelį jūsų informacija (kas yra brangu ir nepraktiška), jūs suteikiate jam galimybę ieškoti per jūsų dokumentus. Kai kas nors užduoda klausimą, sistema randa aktualią informaciją ir įtraukia ją į užklausą. Modelis atsako remdamasis tuo rasta kontekstu.

Įsivaizduokite RAG kaip nuorodų biblioteką modeliui. Kai užduodate klausimą, sistema:

1. **Vartotojo užklausa** – jūs klausiate
2. **Įterpimas (embedding)** – jūsų klausimas paverčiamas vektoriumi
3. **Vektorinė paieška** – randami panašūs dokumentų fragmentai
4. **Konteksto surinkimas** – į užklausą pridedami susiję fragmentai
5. **Atsakymas** – LLM generuoja atsakymą remdamasis kontekstu

Tai pagrindžia modelio atsakymus jūsų tikrais duomenimis vietoje pasikliaujant treniruočių žiniomis ar spėlionėmis.

## Reikalingos sąlygos

- Baigtas [Modulis 00 - Greitas pradžia](../00-quick-start/README.md) (Easy RAG pavyzdžiui, minėtam aukščiau)
- Baigtas [Modulis 01 - Įvadas](../01-introduction/README.md) (išdiegti Azure OpenAI ištekliai, įskaitant modelį `text-embedding-3-small`)
- `.env` failas šakninėje direktorijoje su Azure prisijungimo duomenimis (sukurtas komandą `azd up` Modulyje 01)

> **Pastaba:** jei neužbaigėte Modulio 01, pirmiausia sekite ten pateiktas diegimo instrukcijas. Komanda `azd up` išdiegia tiek GPT pokalbių modelį, tiek šio modulio naudojamą įterpimų modelį.

## RAG supratimas

Žemiau pateiktas diagramas iliustruoja pagrindinę idėją: vietoje to, kad remtumėtės tik modelio treniruočių duomenimis, RAG suteikia jam jūsų dokumentų nuorodų biblioteką, kurią jis pasitikrina prieš generuodamas kiekvieną atsakymą.

<img src="../../../translated_images/lt/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ši diagrama parodo skirtumą tarp standartinio LLM (spėjantis iš treniruočių duomenų) ir RAG patobulinto LLM (pirmiausia pasitariantį jūsų dokumentus).*

Štai kaip dalys sujungiamos nuo pradžios iki pabaigos. Vartotojo klausimas keliauja per keturis etapus – įterpimą, vektorų paiešką, konteksto surinkimą ir atsakymo generavimą – kiekvienas žingsnis statomas ant ankstesnio:

<img src="../../../translated_images/lt/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ši diagrama atvaizduoja pilną RAG srautą – vartotojo užklausa eina per įterpimą, vektorinę paiešką, konteksto surinkimą ir atsakymo generavimą.*

Likusi šio modulio dalis išsamiai aptaria kiekvieną etapą su kodu, kurį galite paleisti ir modifikuoti.

### Kurią RAG metodiką naudoja šis vadovas?

LangChain4j siūlo tris būdus RAG įgyvendinimui, kiekvienas su skirtingu abstrakcijos lygiu. Žemiau pateiktame diagramoje jie palyginti šalia šalia:

<img src="../../../translated_images/lt/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ši diagrama lygina tris LangChain4j RAG metodus – Easy, Native ir Advanced – svetimi jų pagrindinius komponentus ir kada naudoti.*

| Metodika | Ką atlieka | Kompromisas |
|---|---|---|
| **Easy RAG** | Viską automatiškai sujungia per `AiServices` ir `ContentRetriever`. Aprašote sąsają, priskiriate retriverį, o LangChain4j pats tvarko įterpimą, paiešką ir užklausos surinkimą. | Minimalus kodas, bet nematote, kas vyksta kiekviename žingsnyje. |
| **Native RAG** | Jūs pats kviečiate įterpimų modelį, ieškote saugykloje, kuriate užklausą ir generuojate atsakymą – žingsnis po žingsnio ir aiškiai. | Daugiau kodo, bet kiekvienas etapas matomas ir modifikuojamas. |
| **Advanced RAG** | Naudoja `RetrievalAugmentor` sistemą su pritaikomais užklausų transformatoriais, maršrutizatoriais, reitinguotojais ir turinio įterpėjais gamybinėms sistemoms. | Maksimali lankstumas, bet žymiai sudėtingesnis. |

**Šis vadovas naudoja Native metodiką.** Kiekvienas RAG srauto žingsnis – užklausos įterpimas, vektorinė paieška, konteksto surinkimas ir atsakymo generavimas – parašytas aiškiai faile [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Tai sąmoninga: kaip mokymosi išteklius svarbiau, kad pamatytumėte ir suprastumėte kiekvieną etapą, negu, kad kodas būtų maksimaliai minimalus. Kai suprasite, kaip dalys dera, galėsite pereiti prie Easy RAG greitiems prototipams arba Advanced RAG gamybinėms sistemoms.

> **💡 Jau matėte Easy RAG veikiant?** [Greito starto modulyje](../00-quick-start/README.md) yra Dokumentų Q&A pavyzdys ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), kuris naudoja Easy RAG – LangChain4j automatiškai tvarko įterpimą, paiešką ir užklausos surinkimą. Šis modulis žengia toliau ir atidaro tą srautą, kad galėtumėte pamatyti ir valdyti kiekvieną etapą patys.

<img src="../../../translated_images/lt/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ši diagrama rodo Easy RAG srautą iš `SimpleReaderDemo.java`. Palyginkite su Native metodu, naudojamu šiame modulyje: Easy RAG slepia įterpimą, paiešką ir užklausos surinkimą už `AiServices` ir `ContentRetriever` – jūs įkeliate dokumentą, priskiriate retriverį ir gaunate atsakymus. Native metodas šio modulio atveju atveria tą srautą, kad galėtumėte patys kvieti kiekvieną etapą (įterpti, ieškoti, surinkti kontekstą, generuoti), suteikdamas pilną matomumą ir valdymą.*

## Kaip tai veikia

Šio modulio RAG srautas skirstomas į keturis etapus, kurie vyksta nuosekliai kiekvieną kartą, kai vartotojas užduoda klausimą. Pirmiausia įkeltas dokumentas yra **išskaidomas ir suskaidomas** į valdomus fragmentus. Tie fragmentai tada paverčiami į **vektorinius įterpinius** ir saugomi, kad būtų galima juos aiškiai palyginti. Kai atvyksta užklausa, sistema atlieka **semantinę paiešką** ieškodama aktualiausių fragmentų, o galiausiai juos perduoda LLM kaip kontekstą **atsakymo generavimui**. Žemiau pateiktose dalyse aptariamas kiekvienas etapas su tikru kodu ir diagramomis. Pažiūrėkime į pirmą žingsnį.

### Dokumento apdorojimas

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kai įkeliate dokumentą, sistema jį analizuoja (PDF ar paprastas tekstas), prideda metaduomenis, tokius kaip failo pavadinimas, ir tada suskaido į fragmentus – mažesnes dalis, kurios patogiai telpa modelio konteksto lange. Šie fragmentai kiek persidengia, kad neprarastumėte konteksto ribose.

```java
// Išanalizuokite įkeltą failą ir apvyniokite jį LangChain4j dokumente
Document document = Document.from(content, metadata);

// Padalinkite į 300 žodžių fragmentus su 30 žodžių persidengimu
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Žemiau pateikta diagrama vizualiai parodo šį procesą. Pastebėkite, kad kiekvienas fragmentas dalinasi keletu žodžių su kaimynais – 30 žodžių persidengimas užtikrina, kad svarbus kontekstas nepraeis nepastebėtas:

<img src="../../../translated_images/lt/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ši diagrama rodo dokumento suskaidymą į 300 žodžių fragmentus su 30 žodžių persidengimu, išlaikant kontekstą ribose.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ir paklauskite:
> - "Kaip LangChain4j skaido dokumentus į fragmentus ir kodėl persidengimas svarbus?"
> - "Koks yra optimalus fragmentų dydis skirtingiems dokumentų tipams ir kodėl?"
> - "Kaip tvarkyti dokumentus keliomis kalbomis ar su specialiu formatavimu?"

### Įterpinių kūrimas

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Kiekvienas fragmentas paverčiamas skaitmeniniu atvaizdu, vadinamu įterpiniu – iš esmės tai yra reikšmės į skaičius konverteris. Įterpimų modelis nėra "protingas" kaip pokalbių modelis; jis negali vykdyti nurodymų, mąstyti ar atsakyti į klausimus. Jis sugeba tik suvesti tekstą į matematinę erdvę, kur panašios reikšmės atsiduria arti – "mašina" šalia "automobilio", "grąžinimo politika" šalia "grąžink mano pinigus". Įsivaizduokite pokalbių modelį kaip žmogų, su kuriuo galite kalbėtis; o įterpimų modelis yra labai gerai veikianti dokumentų sistema.

<img src="../../../translated_images/lt/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ši diagrama rodo, kaip įterpimų modelis paverčia tekstą į skaitmeninius vektorius, išdėstydamas panašias reikšmes – pvz., "mašina" ir "automobilis" – arti viena kitos vektorinėje erdvėje.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

Žemiau pateiktas klasių diagrama rodo du atskirus srautus RAG pipeline ir LangChain4j klases, kurios juos įgyvendina. **Įėjimo srautas** (vykdomas vieną kartą įkėlimo metu) dalija dokumentą, įterpia fragmentus ir saugo naudojant `.addAll()`. **Užklausos srautas** (vykdomas kiekvieną kartą, kai vartotojas užduoda klausimą) įterpia klausimą, ieško saugykloje per `.search()` ir perduoda suderintą kontekstą pokalbių modeliui. Abu srautai jungiasi bendra per `EmbeddingStore<TextSegment>` sąsają:

<img src="../../../translated_images/lt/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ši diagrama parodo du RAG proceso srautus – įėjimą ir užklausą – ir kaip jie jungiasi per bendrą EmbeddingStore.*

Kai įterpiniai yra saugomi, panašus turinys natūraliai klasterizuojasi vektorinėje erdvėje. Žemiau pateikta vizualizacija rodo, kaip susiję dokumentai tampa gretimi taškai, kas ir daro semantinę paiešką įmanomą:

<img src="../../../translated_images/lt/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ši vizualizacija parodo, kaip susiję dokumentai klasterizuojasi 3D vektorinėje erdvėje, kur temos kaip Techniniai dokumentai, Verslo taisyklės ir DUK sudaro atskirus grupavimus.*

Kai vartotojas ieško, sistema vykdo keturis žingsnius: vieną kartą sukurs įterpinius dokumentams, kiekvienai paieškai sukurs užklausos įterpinį, palygins užklausos vektorių su visais saugomais vektoriais panaudodama kosinuso panašumą ir grąžins aukščiausiai įvertintus fragmentus. Žemiau pateikta diagrama apibūdina kiekvieną žingsnį ir LangChain4j klases, dalyvaujančias procese:

<img src="../../../translated_images/lt/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ši diagrama rodo keturių žingsnių įterpinių paieškos procesą: įterpti dokumentus, įterpti užklausą, palyginti vektorius kosinuso panašumu ir grąžinti geriausius rezultatus.*

### Semantinė paieška

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kai užduodate klausimą, jūsų klausimas taip pat paverčiamas įterpiniu. Sistema palygina jūsų klausimo įterpinį su visų dokumentų fragmentų įterpiniais. Ji randa fragmentus, kurių reikšmė yra panaši – ne tik atitikmenis pagal raktinius žodžius, bet ir faktinę semantinę prasmę.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

Žemiau pateikta diagrama lygina semantinę paiešką su tradicine raktinių žodžių paieška. Raktinių žodžių paieška „transporto priemonė“ praleidžia fragmentą apie „automobilius ir sunkvežimius“, tačiau semantinė paieška supranta, kad tai tas pats ir grąžina jį kaip aukštai įvertintą atitikmenį:

<img src="../../../translated_images/lt/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ši diagrama lygina raktinių žodžių paiešką su semantine paieška, parodant, kaip semantinė paieška atrenka konceptualiai susijusį turinį net jei tikslių raktinių žodžių nėra.*

Po gaubtu panašumas matuojamas kosinuso panašumu – iš esmės klausiama „ar šie du rodyklės rodo ta pačia kryptimi?“ Du fragmentai gali turėti visiškai skirtingus žodžius, bet jei jų reikšmė ta pati, jų vektoriai rodo ta kryptimi ir įvertinimas yra arti 1.0:

<img src="../../../translated_images/lt/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*Ši diagrama iliustruoja kosinuso panašumą kaip įstatytų vektorių kampą — labiau sulygiuoti vektoriai gauna balą arčiau 1,0, kas reiškia didesnį semantinį panašumą.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ir paklauskite:
> - "Kaip veikia panašumo paieška naudojant įstatytus vektorius ir kas lemia balą?"
> - "Kokį panašumo slenkstį turėčiau naudoti ir kaip tai veikia rezultatus?"
> - "Kaip elgtis, kai nerandama jokių susijusių dokumentų?"

### Atsakymo generavimas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Labiausiai aktualūs fragmentai surenkami į struktūruotą užklausą, į kurią įtraukiamos aiškios instrukcijos, gautas kontekstas ir vartotojo klausimas. Modelis skaito tuos konkrečius fragmentus ir atsako remdamasis ta informacija — jis gali naudoti tik tai, kas yra priešais, taip išvengiant išgalvojimų.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Žemiau pateikta diagrama rodo šį surinkimo procesą — aukščiausiai įvertinti fragmentai iš paieškos žingsnio įterpiami į užklausos šabloną, o `OpenAiOfficialChatModel` generuoja pagrįstą atsakymą:

<img src="../../../translated_images/lt/context-assembly.7e6dd60c31f95978.webp" alt="Konteksto surinkimas" width="800"/>

*Ši diagrama rodo, kaip aukščiausiai įvertinti fragmentai surenkami į struktūruotą užklausą, leidžiančią modeliui generuoti pagrįstą atsakymą iš jūsų duomenų.*

## Programos paleidimas

**Patikrinkite diegimą:**

Įsitikinkite, kad šakniniame kataloge yra `.env` failas su Azure kredencialais (sukurtas Module 01 metu):

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pradėkite programą:**

> **Pastaba:** Jei jau paleidote visas programas naudojant `./start-all.sh` iš Module 01, šis modulis jau veikia prievade 8081. Galite praleisti žemiau pateiktas paleidimo komandas ir tiesiogiai nueiti į http://localhost:8081.

**1 variantas: Naudojant Spring Boot Dashboard (Rekomenduojama VS Code vartotojams)**

Dev konteineryje yra Spring Boot Dashboard plėtinys, suteikiantis vizualią sąsają valdyti visas Spring Boot programas. Jį rasite Activity Bar kairėje VS Code pusėje (ieškokite Spring Boot piktogramos).

Iš Spring Boot Dashboard galite:
- Matyti visas darbo erdvėje esančias Spring Boot programas
- Vienu paspaudimu paleisti/stabdyti programas
- Realio laiko režimu peržiūrėti programų žurnalus
- Stebėti programų būseną

Paprasčiausiai spustelėkite paleidimo mygtuką šalia „rag“ modulio, kad jį paleistumėte, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Šis ekrano vaizdas rodo Spring Boot Dashboard VS Code, kur galite paleisti, sustabdyti ir stebėti programas vizualiai.*

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (01-04 moduliai):

**Bash:**
```bash
cd ..  # Iš šakninių katalogų
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninio katalogo
.\start-all.ps1
```

Arba paleiskite tik šį modulį:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Abu skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite prieš paleisdami rankiniu būdu sukompiliuoti visus modulius:
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

Naršyklėje atidarykite http://localhost:8081.

**Norėdami sustabdyti:**

**Bash:**
```bash
./stop.sh  # Tik šis modulis
# Arba
cd .. && ./stop-all.sh  # Visos moduliai
```

**PowerShell:**
```powershell
.\stop.ps1  # Šis modulis tik
# Arba
cd ..; .\stop-all.ps1  # Visi moduliai
```

## Programos naudojimas

Programa suteikia žiniatinklio sąsają dokumentų įkėlimui ir klausimų uždavimui.

<a href="images/rag-homepage.png"><img src="../../../translated_images/lt/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG programos sąsaja" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Šis ekrano vaizdas rodo RAG programos sąsają, kur galite įkelti dokumentus ir užduoti klausimus.*

### Įkelkite dokumentą

Pradėkite nuo dokumento įkėlimo — TXT failai geriausiai tinkami testavimui. Šiame kataloge yra `sample-document.txt`, kuriame pateikta informacija apie LangChain4j funkcijas, RAG įgyvendinimą ir geriausias praktikas — puikiai tinka sistemos bandymams.

Sistema apdoroja jūsų dokumentą, suskaido jį į fragmentus ir sukuria įstatytus vektorius kiekvienam fragmentui. Tai vyksta automatiškai, kai įkeliate.

### Užduokite klausimus

Dabar užduokite konkrečius klausimus apie dokumentą. Išbandykite faktinius klausimus, aiškiai nurodytus dokumente. Sistema ieško atitinkančių fragmentų, įtraukia juos į užklausą ir generuoja atsakymą.

### Patikrinkite šaltinių nuorodas

Atkreipkite dėmesį, kad kiekvienas atsakymas turi šaltinių nuorodas su panašumo balais. Šie balai (nuo 0 iki 1) rodo, kiek kiekvienas fragmentas buvo svarbus jūsų klausimui. Aukštesni balai reiškia geresnį atitikimą. Tai leidžia jums patvirtinti atsakymą pagal šaltinį.

<a href="images/rag-query-results.png"><img src="../../../translated_images/lt/rag-query-results.6d69fcec5397f355.webp" alt="RAG užklausos rezultatai" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Šiame ekrano vaizde matyti užklausos rezultatai su sugeneruotu atsakymu, šaltinių nuorodomis ir panašumo balais kiekvienam rastam fragmentui.*

### Eksperimentuokite su klausimais

Išbandykite įvairius klausimų tipus:
- Konkretūs faktai: „Kokia pagrindinė tema?“
- Palyginimai: „Kuo skiriasi X ir Y?“
- Santraukos: „Apibendrinkite svarbiausias Z dalis“

Stebėkite, kaip panašumo balai keičiasi priklausomai nuo to, kaip gerai jūsų klausimas atitinka dokumentų turinį.

## Pagrindinės sąvokos

### Fragmentavimo strategija

Dokumentai skaidomi į 300 simbolių fragmentus su 30 simbolių persidengimu. Šis balansas užtikrina, kad kiekvienas fragmentas turėtų pakankamai konteksto, kad būtų prasmingas, tačiau būtų pakankamai mažas, kad būtų galima įtraukti kelis fragmentus į užklausą.

### Panašumo balai

Kiekvienas rastas fragmentas turi panašumo balą nuo 0 iki 1, kuris rodo, kiek gerai jis atitiko vartotojo klausimą. Žemiau pateikta diagrama vizualizuoja balų ribas ir kaip sistema jas naudoja filtruojant rezultatus:

<img src="../../../translated_images/lt/similarity-scores.b0716aa911abf7f0.webp" alt="Panašumo balai" width="800"/>

*Ši diagrama rodo balų intervalus nuo 0 iki 1, su minimaliu slenksčiu 0,5, kuris atfiltruoja nereikšmingus fragmentus.*

Balų intervalas nuo 0 iki 1:
- 0,7–1,0: Labai aktualūs, tikslus atitikimas
- 0,5–0,7: Aktualūs, geras kontekstas
- Žemiau 0,5: Atfiltruota, pernelyg skirtingi

Sistema pateikia tik fragmentus, kurie atitinka minimalią ribą, siekdama užtikrinti kokybę.

Įstatyti vektoriai gerai veikia, kai prasmės klasteriai yra aiškūs, tačiau turi spragas. Žemiau pateikta diagrama rodo dažniausias klaidų formas — pernelyg dideli fragmentai sukuria neryškius vektorius, per maži fragmentai neturi konteksto, dviprasmiai terminai nukreipia į kelis klasterius, o tikslaus atitikimo paieškos (ID, dalies numeriai) visiškai neveikia su įstatytais vektoriais:

<img src="../../../translated_images/lt/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Įstatytų vektorių klaidų formos" width="800"/>

*Ši diagrama rodo dažniausias įstatytų vektorių klaidų formas: pernelyg didelius fragmentus, pernelyg mažus fragmentus, dviprasmius terminus rodančius į kelis klasterius ir tikslaus atitikimo paieškas, tokias kaip ID.*

### Atminties saugykla

Šis modulis naudoja atminties saugyklą paprastumui. Kai perkraunate programą, įkelti dokumentai prarandami. Produkcijos sistemos naudoja pastovias vektorių duomenų bazes, tokias kaip Qdrant arba Azure AI Search.

### Konteksto lango valdymas

Kiekvienas modelis turi maksimalų konteksto langą. Negalite įtraukti visų fragmentų iš didelio dokumento. Sistema parenka top N aktualiausių fragmentų (pagal nutylėjimą 5), kad liktų ribose, tuo pačiu suteikdama pakankamai konteksto tiksliems atsakymams.

## Kada svarbus RAG

RAG ne visada yra tinkamas sprendimas. Žemiau pateikta sprendimų schema padeda nuspręsti, kada RAG prideda vertės, o kada paprastesni metodai — pavyzdžiui, tiesiog įtraukti turinį į užklausą arba pasikliauti modelio įgimtomis žiniomis — yra pakankami:

<img src="../../../translated_images/lt/when-to-use-rag.1016223f6fea26bc.webp" alt="Kada naudoti RAG" width="800"/>

*Ši diagrama rodo sprendimų vadovą, kada RAG prideda vertės, o kada pakanka paprastesnių metodų.*

**Naudokite RAG, kai:**
- Atsakote į klausimus apie saugomus dokumentus
- Informacija dažnai keičiasi (politikos, kainos, specifikacijos)
- Tikslumui reikia šaltinio nurodymo
- Turinys per didelis įtraukti į vieną užklausą
- Reikia patikimų, pagrįstų atsakymų

**Nenaudokite RAG, kai:**
- Klausimai reikalauja bendrojo modelio žinių
- Reikia realaus laiko duomenų (RAG veikia su įkeltais dokumentais)
- Turinys pakankamai mažas, kad jį būtų galima tiesiogiai įtraukti į užklausas

## Tolimesni žingsniai

**Kitas modulis:** [04-tools - AI agentai su įrankiais](../04-tools/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 02 - Užklausų inžinerija](../02-prompt-engineering/README.md) | [Atgal į pradžią](../README.md) | [Kitas: Modulis 04 - Įrankiai →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus vertimas žmogaus. Mes neatsakome už jokius nesusipratimus ar neteisingas interpretacijas, kilusias naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->