# Moodul 03: RAG (otsingupõhine genereerimine)

## Sisukord

- [Mida sa õpid](../../../03-rag)
- [RAG mõistmine](../../../03-rag)
- [Eeldused](../../../03-rag)
- [Kuidas see töötab](../../../03-rag)
  - [Dokumentide töötlemine](../../../03-rag)
  - [Manuste loomine](../../../03-rag)
  - [Semantiline otsing](../../../03-rag)
  - [Vastuste genereerimine](../../../03-rag)
- [Rakenduse käivitamine](../../../03-rag)
- [Rakenduse kasutamine](../../../03-rag)
  - [Dokumendi üleslaadimine](../../../03-rag)
  - [Küsimuste esitamine](../../../03-rag)
  - [Allikaviidete kontrollimine](../../../03-rag)
  - [Eksperimenteerimine küsimustega](../../../03-rag)
- [Olulised mõisted](../../../03-rag)
  - [Tükkideks jagamise strateegia](../../../03-rag)
  - [Sarnasuste skoorid](../../../03-rag)
  - [Mälupõhine salvestus](../../../03-rag)
  - [Kontekstiakna haldamine](../../../03-rag)
- [Millal RAG-i kasutada](../../../03-rag)
- [Järgmised sammud](../../../03-rag)

## Mida sa õpid

Eelmistes moodulites õppisid sa, kuidas suhelda tehisintellektiga ja kuidas tõhusalt oma päringuid struktureerida. Kuid on üks põhiline piirang: keelemodellid teavad ainult seda, mida nad on treeningu ajal õppinud. Nad ei saa vastata küsimustele, mis puudutavad sinu ettevõtte poliitikaid, sinu projekti dokumentatsiooni ega mingit teavet, mis neile ei ole ette antud.

RAG (otsingupõhine genereerimine) lahendab selle probleemi. Selle asemel, et proovida mudelit sinu infole õpetada (mis on kulukas ja ebaefektiivne), annad sa talle võimaluse otsida läbi oma dokumentide. Kui keegi esitab küsimuse, leiab süsteem asjakohased andmed ja lisab need päringusse. Mudel vastab seejärel selle leitud konteksti põhjal.

Mõtle RAG-ile kui mudeli viite raamatukogule. Kui sa küsid küsimust, teeb süsteem järgmist:

1. **Kasutaja päring** – Sa esitad küsimuse  
2. **Manustamine** – Päring teisendatakse vektoriks  
3. **Vektorotsing** – Leidub sarnased dokumenditükid  
4. **Konteksti kokkupanek** – Asjakohased tükid lisatakse päringusse  
5. **Vastus** – LLM genereerib vastuse leitud konteksti põhjal  

See muudab mudeli vastused sinu tõelistele andmetele tuginevaks, mitte ainult treeninguinfole või väljamõeldud vastusteks.

## RAG mõistmine

Joonis allpool illustreerib põhikontseptsiooni: RAG annab mudelile lisaks ainult treeningandmete kasutamisele ligipääsu sinu dokumentide viiteraamatukogule, mida ta võib enne iga vastuse genereerimist kasutada.

<img src="../../../translated_images/et/what-is-rag.1f9005d44b07f2d8.webp" alt="Mis on RAG" width="800"/>

Siin on, kuidas komponendid omavahel lõpust lõpuni seotakse. Kasutaja küsimus läbib neli etappi — manustamise, vektorotsingu, konteksti kokkupaneku ja vastuse genereerimise — igaüks järgneb eelnevale:

<img src="../../../translated_images/et/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektuur" width="800"/>

Järgmised peatükid selgitavad iga etappi üksikasjalikult koos käideldava ja modifitseeritava koodiga.

## Eeldused

- Moodul 01 lõpetatud (Azure OpenAI ressursid paigaldatud)  
- Juurekataloogis `.env` fail, mis sisaldab Azure autentimist (loodud käsklusega `azd up` Moodulis 01)  

> **Märkus:** Kui sa pole Moodulit 01 lõpetanud, järgi esmalt seal toodud paigaldusjuhiseid.

## Kuidas see töötab

### Dokumentide töötlemine

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kui sa laadid dokumendi üles, süsteem analüüsib selle (PDF või tavaline tekst), seob metainfot nagu failinimi ja jagab selle tükikesteks — väiksemateks osadeks, mis mahuvad mugavalt mudeli kontekstiaknasse. Need tükid kattuvad veidi, et servades konteksti ei kaoks.

```java
// Paranda üles laaditud fail ja paki see LangChain4j dokumendi sisse
Document document = Document.from(content, metadata);

// Jaga 300-märgilisteks tükkideks 30-märgilise kattuvusega
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Joonis allpool näitab, kuidas see töötab visuaalselt. Märka, kuidas iga tükk jagab mõningaid märke naabriga — 30-märgiline kattuvus tagab, et oluline kontekst ei jää kahe vahele:

<img src="../../../translated_images/et/document-chunking.a5df1dd1383431ed.webp" alt="Dokumendi tükkideks jagamine" width="800"/>

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ja küsi:
> - "Kuidas LangChain4j jagab dokumendid tükikesteks ja miks on kattuvus oluline?"
> - "Mis on optimaalne tüki suurus eri dokumenditüüpide jaoks ja miks?"
> - "Kuidas käsitleda dokumente mitmes keeles või eripärase vormindusega?"

### Manuste loomine

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Iga tükk teisendatakse numbrilisse kujutisse, mida nimetatakse manuseks – see on matemaatiline sõrmejälg, mis tabab teksti tähendust. Sarnased tekstid annavad sarnaseid manuseid.

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
  
Klassidiagramm allpool näitab, kuidas need LangChain4j komponendid on ühendatud. `OpenAiOfficialEmbeddingModel` teisendab teksti vektoriteks, `InMemoryEmbeddingStore` salvestab vektorid koos nende originaalse `TextSegment` andmetega, ja `EmbeddingSearchRequest` kontrollib päringu parameetreid nagu `maxResults` ja `minScore`:

<img src="../../../translated_images/et/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klassid" width="800"/>

Kui manused on salvestatud, grupeerub sarnane sisu loomulikult vektorruumis. Joonis allpool näitab, kuidas seotud teemad kogunevad lähestikku asetsevateks punktideks – see võimaldab semantilist otsingut:

<img src="../../../translated_images/et/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor-manhuskeskkond" width="800"/>

### Semantiline otsing

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kui sa esitad küsimuse, muudetakse ka küsimus manuseks. Süsteem võrdleb sinu küsimuse manust kõigi dokumendi tükkide manustega. Ta leiab tükid, mille tähendus on kõige sarnasem – mitte ainult sõnalised võtmesõnad, vaid tegelik semantiline sarnasus.

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
  
Joonis allpool võrdleb semantilist otsingut tavapärase võtmesõnade otsinguga. Võtmesõnade otsing sõnale "vehicle" jätab tähelepanuta tüki "cars and trucks", aga semantiline otsing mõistab, et nad tähendavad sama asja ning tagastab selle kõrge skooriga vasteks:

<img src="../../../translated_images/et/semantic-search.6b790f21c86b849d.webp" alt="Semantiline otsing" width="800"/>

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ja küsi:
> - "Kuidas töötab sarnasusotsing manuste puhul ja mis määrab skoori?"
> - "Millist sarnasuse lävendit kasutada ja kuidas see tulemusi mõjutab?"
> - "Kuidas käituda olukordades, kus sobivaid dokumente ei leita?"

### Vastuste genereerimine

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kõige asjakohasemad tükid kogutakse üles struktureeritud päringusse, mis sisaldab selgesõnalisi juhiseid, leitud konteksti ja kasutaja küsimust. Mudel loeb need kindlaksmääratud tükid ja vastab selle info põhjal — ta saab kasutada ainult seda, mis on talle ette antud, mis aitab vältida väljamõtlemist.

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
  
Joonis allpool näitab seda kokkupanekut tegevuses — kõige kõrgema skooriga tükid otsingust manustatakse päringutemplisse ja `OpenAiOfficialChatModel` genereerib põhjendatud vastuse:

<img src="../../../translated_images/et/context-assembly.7e6dd60c31f95978.webp" alt="Konteksti kokkupanek" width="800"/>

## Rakenduse käivitamine

**Paigalduse kontroll:**

Veendu, et `.env` fail on juurekataloogis koos Azure autentimisega (loodud Mooduli 01 jooksul):  
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Rakenduse käivitamine:**

> **Märkus:** Kui oled juba käivitanud kõik rakendused käsuga `./start-all.sh` Moodulis 01, töötab see moodul juba pordil 8081. Võid alljärgnevad käivituskäsud vahele jätta ja minna otse aadressile http://localhost:8081.

**Valik 1: Spring Boot Dashboard kasutamine (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code Vasakult tegevusribalt (otsides Spring Boot ikooni).

Spring Boot Dashboard abil saad:
- Näha kõiki töölaual olevaid Spring Boot rakendusi  
- Käivitada/peatada rakendusi ühe klikiga  
- Vaadata rakenduste logisid reaalajas  
- Jälgida rakenduse olekut  

Lihtsalt vajuta mängunuppu "rag" kõrval selle mooduli käivitamiseks või alusta korraga kõigist moodulitest.

<img src="../../../translated_images/et/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Shell skriptide kasutamine**

Käivita kõik veebirakendused (moodulid 01-04):

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
  
Või käivita ainult see moodul:

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
  
Mõlemad skriptid loevad automaatselt keskkonnamuutujad juure `.env` failist ja ehitavad JAR-id, kui need puuduvad.

> **Märkus:** Kui eelistad enne käivitamist mooduleid käsitsi kompileerida:  
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


Ava brauseris http://localhost:8081

**Selle peatamiseks:**

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

Rakendus pakub veebiliidest dokumentide üleslaadimiseks ja küsimuste esitamiseks.

<a href="images/rag-homepage.png"><img src="../../../translated_images/et/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG rakenduse liides" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG rakenduse liides – laadi dokumendid üles ja esita küsimusi*

### Dokumendi üleslaadimine

Alusta dokumendi üleslaadimisest – testimiseks sobivad kõige paremini TXT-failid. Selles kaustas on olemas `sample-document.txt`, mis sisaldab infot LangChain4j funktsioonide, RAG-i rakenduse ja parimate praktikate kohta – ideaalne süsteemi testimiseks.

Süsteem töötleb su dokumendi, jagab selle tükkideks ja loob iga tüki jaoks manused. See toimub automaatselt, kui sa faili üles laadid.

### Küsimuste esitamine

Nüüd esita täpseid küsimusi dokumendi sisu kohta. Proovi midagi faktipõhist, mis on dokumendis selgelt välja toodud. Süsteem otsib asjakohased tükid välja, lisab need päringusse ja genereerib vastuse.

### Allikaviidete kontrollimine

Pane tähele, et iga vastus sisaldab allikaviiteid koos sarnasuse skooridega. Need skoorid (0 kuni 1) näitavad, kui asjakohane iga tükk sinu küsimusele oli. Kõrgemad skoorid tähendavad paremaid vasteid. See võimaldab vastust allikmaterjaliga kontrollida.

<a href="images/rag-query-results.png"><img src="../../../translated_images/et/rag-query-results.6d69fcec5397f355.webp" alt="RAG päringu tulemused" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Päringu tulemused, kus on vastus koos allikaviidete ja asjakohasuse skooridega*

### Eksperimenteerimine küsimustega

Proovi erinevat tüüpi küsimusi:
- Täpsed faktid: "Mis on peamine teema?"  
- Võrdlused: "Mis vahe on X ja Y vahel?"  
- Kokkuvõtted: "Kokkuvõtlikult, mis on võtmeasjad Z kohta?"  

Vaata, kuidas asjakohasus skoorid muutuvad sõltuvalt sellest, kui hästi su küsimus dokumendi sisuga haakub.

## Olulised mõisted

### Tükkideks jagamise strateegia

Dokumendid jagatakse 300-märgiseks tükkideks, millel on 30 märgiline kattuvus. See tasakaal tagab, et igal tükil on piisavalt konteksti mõtestamiseks, samal ajal püsivad tükid väiksena, et päringusse mahuaks mitu tükki.

### Sarnasuste skoorid

Iga leitud tükk on seotud sarnasuse skooriga vahemikus 0 kuni 1, mis näitab, kui tihedalt see vastab kasutaja küsimusele. Joonis allpool visualiseerib skooride vahemikud ja kuidas süsteem kasutab neid tulemuste filtreerimiseks:

<img src="../../../translated_images/et/similarity-scores.b0716aa911abf7f0.webp" alt="Sarnasuse skoorid" width="800"/>

Skoorid jäävad vahemikku 0 kuni 1:  
- 0.7–1.0: Väga asjakohane, täpne vaste  
- 0.5–0.7: Asjakohane, hea kontekst  
- Alla 0.5: Filtreeritud välja, liiga erinev  

Süsteem tagastab ainult tükid, mille skoor ületab minimaalse läve, et tagada kvaliteet.

### Mälupõhine salvestus

See moodul kasutab lihtsuse mõttes mälupõhist salvestust. Kui rakendus taaskäivitatakse, kaovad üleslaaditud dokumendid. Tootmissüsteemid kasutavad püsivaid vektoriandmebaase nagu Qdrant või Azure AI Search.

### Kontekstiakna haldamine

Igal mudelil on maksimaalne kontekstiakna suurus. Sa ei saa lisada kõiki tükkisid suurest dokumendist. Süsteem toob välja N kõige asjakohasemat tükki (vaikimisi 5), et piire järgida ja samal ajal piisavat konteksti täpsete vastuste jaoks pakkuda.

## Millal RAG-i kasutada

RAG ei ole alati õige valik. Järgmine juhend aitab sul otsustada, millal RAG lisab väärtust võrreldes lihtsamate lähenemistega — nagu sisu otse päringusse lisamine või mudeli enda teadmiste kasutamine:

<img src="../../../translated_images/et/when-to-use-rag.1016223f6fea26bc.webp" alt="Millal kasutada RAG-i" width="800"/>

**Kasuta RAG-i, kui:**
- Vastamine küsimustele seoses konfidentsiaalsete dokumentidega  
- Teave muutub sageli (poliitikad, hinnad, spetsifikatsioonid)  
- Täpsus nõuab allika tsiteerimist  
- Sisu on liiga mahukas, et see mahuks ühte prompti  
- Vajate kontrollitavaid, põhjendatud vastuseid  

**Ärge kasutage RAG-i, kui:**  
- Küsimused nõuavad üldteadmisi, mis mudelil juba olemas on  
- Vajate reaalajas andmeid (RAG töötab üleslaaditud dokumentidel)  
- Sisu on piisavalt väike, et seda saab otse promptidesse lisada  

## Järgmised sammud  

**Järgmine moodul:** [04-tools - AI agendid tööriistadega](../04-tools/README.md)  

---  

**Navigeerimine:** [← Eelmine: Moodul 02 - Promptide loomine](../02-prompt-engineering/README.md) | [Tagasi põhilehele](../README.md) | [Järgmine: Moodul 04 - Tööriistad →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellekti tõlkesüsteemi [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle algses keeles on käsitletav autoriteetse allikana. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta ühegi arusaamatuse või valesti mõistmise eest, mis võivad tuleneda selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->