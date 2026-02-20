# 03 modulis: RAG (Retrieval-Augmented Generation)

## Turinys

- [Ką išmoksite](../../../03-rag)
- [Supratimas apie RAG](../../../03-rag)
- [Reikalingos žinios](../../../03-rag)
- [Kaip tai veikia](../../../03-rag)
  - [Dokumentų apdorojimas](../../../03-rag)
  - [Įterpinių kūrimas](../../../03-rag)
  - [Semantinė paieška](../../../03-rag)
  - [Atsakymų generavimas](../../../03-rag)
- [Paleisti programą](../../../03-rag)
- [Naudojant programą](../../../03-rag)
  - [Įkelti dokumentą](../../../03-rag)
  - [Užduoti klausimus](../../../03-rag)
  - [Patikrinti šaltinių nuorodas](../../../03-rag)
  - [Eksperimentuoti su klausimais](../../../03-rag)
- [Pagrindinės sąvokos](../../../03-rag)
  - [Dalijimo strategija](../../../03-rag)
  - [Panašumo balai](../../../03-rag)
  - [Atminties saugykla](../../../03-rag)
  - [Konteksto lango valdymas](../../../03-rag)
- [Kada svarbus RAG](../../../03-rag)
- [Kiti žingsniai](../../../03-rag)

## Ką išmoksite

Ankstesniuose moduliuose išmokote bendrauti su dirbtiniu intelektu ir efektyviai struktūruoti savo užklausas. Tačiau yra esminė riba: kalbos modeliai žino tik tai, ką išmoko mokymosi metu. Jie negali atsakyti į klausimus apie jūsų įmonės politiką, projekto dokumentaciją ar kitą informaciją, kurios nebuvo apmokyti.

RAG (Retrieval-Augmented Generation) sprendžia šią problemą. Vietoje to, kad bandytumėte perduoti modelį savo informacija (kas yra brangu ir nepraktiška), suteikiate jam galimybę ieškoti per jūsų dokumentus. Kai kas nors užduoda klausimą, sistema suranda susijusią informaciją ir įtraukia ją į užklausą. Modelis tada atsako remdamasis tuo atkurtu kontekstu.

Galvokite apie RAG kaip apie atskirą nuorodų biblioteką modeliui. Kai užduodate klausimą, sistema:

1. **Vartotojo užklausa** – jūs užduodate klausimą  
2. **Įterpinys** – jūsų klausimas paverčiamas vektoriumi  
3. **Vektorinė paieška** – randami panašūs dokumentų fragmentai  
4. **Konteksto surinkimas** – susiję fragmentai pridedami į užklausą  
5. **Atsakymas** – LLM generuoja atsakymą remdamasis kontekstu  

Tai padeda įtvirtinti modelio atsakymus jūsų realiuose duomenyse, o ne pasikliauti jo mokymosi žiniomis ar kurti atsakymus iš galvos.

## Supratimas apie RAG

Toliau pateiktas diagrama iliustruoja pagrindinę idėją: vietoje to, kad modelis remtųsi tik savo mokymosi duomenimis, RAG suteikia jam jūsų dokumentų biblioteką, kurią jis gali pasitikrinti prieš generuodamas kiekvieną atsakymą.

<img src="../../../translated_images/lt/what-is-rag.1f9005d44b07f2d8.webp" alt="Kas yra RAG" width="800"/>

Čia parodyta, kaip dalys susijungia nuo pradžios iki pabaigos. Vartotojo klausimas pereina keturias stadijas – įterpinys, vektorinė paieška, konteksto surinkimas ir atsakymo generavimas – kiekviena iš jų statosi ant ankstesnės:

<img src="../../../translated_images/lt/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG architektūra" width="800"/>

Likusi modulio dalis detaliai paaiškina kiekvieną etapą su kodu, kurį galite paleisti ir keisti.

## Reikalingos žinios

- Baigtas 01 modulis (Azure OpenAI ištekliai įdiegti)  
- `.env` failas pagrindiniame kataloge su Azure kredencialais (sukurtas `azd up` 01 modulyje)  

> **Pastaba:** Jei nebaigėte 01 modulio, pirmiausia sekite ten pateiktas diegimo instrukcijas.

## Kaip tai veikia

### Dokumentų apdorojimas

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kai įkeliate dokumentą, sistema jį analizuoja (PDF arba paprastas tekstas), prideda metaduomenis, tokius kaip failo pavadinimas, tada dalija į mažesnius fragmentus – taip užtikrinant, kad jie patogiai tilptų modelio konteksto lange. Šie fragmentai šiek tiek persidengia, kad neužsimirštų svarbus kontekstas ribose.

```java
// Išanalizuokite įkeltą failą ir supakuokite jį į LangChain4j dokumentą
Document document = Document.from(content, metadata);

// Padalinkite į 300 ženklų dalis su 30 ženklų persidengimu
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Žemiau pateikta diagrama vizualiai parodo, kaip tai veikia. Pastebėkite, kad kiekvienas fragmentas dalijasi kai kuriomis žymomis su kaimynais – 30 žymų persidengimas užtikrina, kad svyravimų metu neprasprūs svarbi informacija:

<img src="../../../translated_images/lt/document-chunking.a5df1dd1383431ed.webp" alt="Dokumento dalijimas į fragmentus" width="800"/>

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ir užduokite:  
> - „Kaip LangChain4j dalija dokumentus į fragmentus ir kodėl svarbus persidengimas?“  
> - „Koks optimalus fragmento dydis skirtingiems dokumentų tipams ir kodėl?“  
> - „Kaip tvarkyti dokumentus keliomis kalbomis arba su specialiu formatavimu?“

### Įterpinių kūrimas

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Kiekvienas fragmentas paverčiamas skaitmenine išraiška – įterpiniu, kuris yra tarsi matematinis pirštų atspaudas, apimantis teksto prasmę. Panašus tekstas sukuria panašius įterpinius.

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
  
Žemiau pateikta klasės diagrama rodo, kaip šie LangChain4j komponentai susijungia. `OpenAiOfficialEmbeddingModel` paverčia tekstą į vektorius, `InMemoryEmbeddingStore` laiko vektorius kartu su jų pradiniu `TextSegment` duomenų struktūra, o `EmbeddingSearchRequest` valdo atkūrimo parametrus, tokius kaip `maxResults` ir `minScore`:

<img src="../../../translated_images/lt/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klasės" width="800"/>

Kai įterpiniai laikomi saugykloje, panaši turinio grupė natūraliai susikoncentruoja vektorinėje erdvėje. Žemiau pateikta vizualizacija rodo, kaip apie susijusias temas dokumentai išsidėsto artimuose taškuose, leidžiant semantinei paieškai veikti:

<img src="../../../translated_images/lt/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorinių įterpinių erdvė" width="800"/>

### Semantinė paieška

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kai užduodate klausimą, jis taip pat paverčiamas įterpiniu. Sistema palygina jūsų klausimo įterpinį su visų dokumentų fragmentų įterpiniais. Randami fragmentai, kurių prasmė artimiausia – ne tik atitikmenys raktiniams žodžiams, bet tikras semantinis panašumas.

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
  
Žemiau pateikta diagrama kontrastuoja semantinę paiešką su tradicine raktinių žodžių paieška. Raktinių žodžių paieška „vehicle“ nepastebi fragmento apie „cars and trucks“, tačiau semantinė paieška supranta, kad tai tas pats dalykas ir grąžina šį fragmentą kaip aukšto balo atitikmenį:

<img src="../../../translated_images/lt/semantic-search.6b790f21c86b849d.webp" alt="Semantinė paieška" width="800"/>

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ir užduokite:  
> - „Kaip veikia panašumo paieška su įterpiniais ir kas lemia balą?“  
> - „Koks panašumo slenkstis turėtų būti naudojamas ir kaip tai veikia rezultatus?“  
> - „Kaip elgtis, kai nerandama susijusių dokumentų?“

### Atsakymų generavimas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Labiausiai susiję fragmentai surenkami į struktūruotą užklausą, kuri apima aiškias instrukcijas, atkurtą kontekstą ir vartotojo klausimą. Modelis perskaito tuos konkrečius fragmentus ir atsako remdamasis ta informacija – jis gali naudoti tik tai, kas yra prieš jį, kas padeda išvengti išsigalvojimų.

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
  
Žemiau pateikta diagrama rodo šį surinkimą veikiant – aukščiausio balo fragmentai iš paieškos žingsnio įskiepiami į užklausos šabloną ir `OpenAiOfficialChatModel` generuoja pagrįstą atsakymą:

<img src="../../../translated_images/lt/context-assembly.7e6dd60c31f95978.webp" alt="Konteksto surinkimas" width="800"/>

## Paleisti programą

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas egzistuoja pagrindiniame kataloge su Azure kredencialais (sukurtas 01 modulyje):
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Paleiskite programą:**

> **Pastaba:** Jei jau pradėjote visas programas naudodami `./start-all.sh` iš 01 modulo, šis modulis jau veikia prie 8081 porto. Galite praleisti žemiau pateiktas paleidimo komandas ir tiesiog atidaryti http://localhost:8081.

**1 variantas: Naudojant Spring Boot Dashboard (Rekomenduojama VS Code vartotojams)**

Kūrimo konteineryje įdiegta Spring Boot Dashboard plėtinys, suteikiantis vizualią sąsają valdyti visas Spring Boot programas. Jį rasite veiklos juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Iš Spring Boot Dashboard galite:
- Peržiūrėti visas darbo vietoje esančias Spring Boot programas  
- Vienu paspaudimu paleisti arba sustabdyti programas  
- Vaizduoti programų žurnalus realiu laiku  
- Stebėti programų būseną  

Paprasčiausiai spustelėkite „play“ mygtuką prie „rag“ modulio, kad jį paleistumėte, arba pradėkite visus modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot valdymo skydelis" width="400"/>

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (modulius 01-04):

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
cd 03-rag
./start.sh
```
  
**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```
  
Abu skriptai automatiškai pakrauna aplinkos kintamuosius iš pagrindinio `.env` failo ir sukompiliuos JAR failus, jei jų nėra.

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
  
Atidarykite http://localhost:8081 naršyklėje.

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
  
## Naudojant programą

Programa suteikia internetinę sąsają dokumentų įkėlimui ir klausimų uždavimui.

<a href="images/rag-homepage.png"><img src="../../../translated_images/lt/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG programos sąsaja" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG programos sąsaja – įkelkite dokumentus ir užduokite klausimus*

### Įkelti dokumentą

Pradėkite įkeldami dokumentą – bandymams tinka TXT failai. Šiame kataloge pateiktas `sample-document.txt`, kuriame yra informacija apie LangChain4j funkcionalumus, RAG įgyvendinimą ir gerąją praktiką – puikiai tinka sistemos testavimui.

Sistema apdoroja jūsų dokumentą, dalija jį į fragmentus ir sukuria įterpinius kiekvienam fragmentui. Tai vyksta automatiškai įkeliant dokumentą.

### Užduoti klausimus

Dabar užduokite konkrečius klausimus apie dokumentų turinį. Bandykite užduoti faktinius klausimus, aiškiai nurodytus dokumente. Sistema ieško susijusių fragmentų, įtraukia juos į užklausą ir sukuria atsakymą.

### Patikrinti šaltinių nuorodas

Atkreipkite dėmesį, kad kiekvienas atsakymas pateikia šaltinių nuorodas su panašumo balais. Šie balai (nuo 0 iki 1) rodo, kiek fragmentas buvo aktualus jūsų klausimui. Aukštesni balai reiškia geresnius atitikimus. Tai leidžia patikrinti atsakymą su šaltininiu turiniu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/lt/rag-query-results.6d69fcec5397f355.webp" alt="RAG užklausos rezultatai" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Užklausos rezultatai su atsakymu, šaltinių nuorodomis ir aktualumo balais*

### Eksperimentuoti su klausimais

Išbandykite įvairius klausimų tipus:  
- Konkretūs faktai: „Kokia yra pagrindinė tema?“  
- Palyginimai: „Kuo skiriasi X ir Y?“  
- Santraukos: „Apibendrinkite pagrindines Z temas“  

Stebėkite, kaip keičiasi aktualumo balai priklausomai nuo to, kaip gerai jūsų klausimas atitinka dokumentų turinį.

## Pagrindinės sąvokos

### Dalijimo strategija

Dokumentai dalijami į 300 žymių fragmentus su 30 žymių persidengimu. Toks balansas užtikrina, kad kiekvienas fragmentas turi pakankamai konteksto būti prasmingas, bet tuo pačiu yra pakankamai mažas, kad į užklausą tilptų keli fragmentai.

### Panašumo balai

Kiekvienas rastas fragmentas turi panašumo balą nuo 0 iki 1, rodantį, kiek jis atitinka vartotojo klausimą. Toliau pateikta diagrama vizualizuoja balų intervalus ir kaip sistema juos naudoja rezultatų filtravimui:

<img src="../../../translated_images/lt/similarity-scores.b0716aa911abf7f0.webp" alt="Panašumo balai" width="800"/>

Balai svyruoja nuo 0 iki 1:
- 0.7–1.0: Labai aktualu, tikslus atitikmuo  
- 0.5–0.7: Aktualu, geras kontekstas  
- Žemiau 0.5: Atfiltruota, per daug skiriasi  

Sistemos vykdo paiešką tik aukščiau minimalaus slenksčio, siekdamos užtikrinti kokybę.

### Atminties saugykla

Šis modulis naudoja atminties saugyklą paprastumui. Perkraunant programą įkelti dokumentai prarandami. Gamybos sistemos naudoja nuolatines vektorines duomenų bazes, tokias kaip Qdrant arba Azure AI Search.

### Konteksto lango valdymas

Kiekvienas modelis turi maksimalų konteksto langą. Negalite įtraukti visų fragmentų iš didelio dokumento. Sistema ištraukia top N aktualiausių fragmentų (pagal nutylėjimą 5), kad tilptų į ribas ir suteiktų pakankamai konteksto tiksliems atsakymams.

## Kada svarbus RAG

RAG ne visada yra tinkamiausias sprendimas. Žemiau pateiktas sprendimų vadovas padeda nuspręsti, kada RAG pridės vertės ir kada paprastesni metodai – kaip turinio tiesioginis įtraukimas į užklausą ar pasikliovimas modelio integruotomis žiniomis – yra pakankami:

<img src="../../../translated_images/lt/when-to-use-rag.1016223f6fea26bc.webp" alt="Kada naudoti RAG" width="800"/>

**Naudokite RAG, kai:**
- Atsakymas į klausimus apie konfidencialius dokumentus
- Informacija dažnai keičiasi (politikos, kainos, specifikacijos)
- Tikslumas reikalauja šaltinio nurodymo
- Turinys yra per didelis, kad tilptų į vieną užklausą
- Reikia patikimų, pagrįstų atsakymų

**Nenaudokite RAG, kai:**
- Klausimai reikalauja bendrųjų žinių, kurias modelis jau turi
- Reikalingi realaus laiko duomenys (RAG veikia su įkeltais dokumentais)
- Turinys yra pakankamai mažas, kad būtų galima įtraukti tiesiogiai į užklausas

## Tolimesni žingsniai

**Kitas modulis:** [04-tools - AI Agentai su įrankiais](../04-tools/README.md)

---

**Naršymas:** [← Ankstesnis: Modulis 02 - Užklausų inžinerija](../02-prompt-engineering/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 04 - Įrankiai →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų arba netikslumų. Pradinė dokumento versija gimtąja kalba turėtų būti laikoma autoritetingu šaltiniu. Dėl svarbios informacijos rekomenduojama naudoti profesionalaus žmogaus vertimą. Mes neatsakome už bet kokius nesusipratimus ar neteisingą interpretavimą, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->