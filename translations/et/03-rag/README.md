# Moodul 03: RAG (taastepõhine loomine)

## Sisukord

- [Video juhendamine](../../../03-rag)
- [Mida sa õpid](../../../03-rag)
- [Eeldused](../../../03-rag)
- [RAG mõistmine](../../../03-rag)
  - [Millist RAG lähenemist see juhend kasutab?](../../../03-rag)
- [Kuidas see töötab](../../../03-rag)
  - [Dokumendi töötlemine](../../../03-rag)
  - [Manuste loomine](../../../03-rag)
  - [Semantiline otsing](../../../03-rag)
  - [Vastuste genereerimine](../../../03-rag)
- [Rakenduse käivitamine](../../../03-rag)
- [Rakenduse kasutamine](../../../03-rag)
  - [Dokumendi üleslaadimine](../../../03-rag)
  - [Küsimuste esitamine](../../../03-rag)
  - [Allikaviidete kontrollimine](../../../03-rag)
  - [Katsetamine küsimustega](../../../03-rag)
- [Põhikontseptsioonid](../../../03-rag)
  - [Tükeldamisstrateegia](../../../03-rag)
  - [Sarnasuskoefitsiendid](../../../03-rag)
  - [Mälusalvestus](../../../03-rag)
  - [Kontekstivälja haldamine](../../../03-rag)
- [Millal RAG on oluline](../../../03-rag)
- [Järgmised sammud](../../../03-rag)

## Video juhendamine

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Mida sa õpid

Eelnevates moodulites õppisid, kuidas vestelda tehisintellektiga ja oma päringuid efektiivselt struktureerida. Kuid on üks põhipiirang: keelemudelid teavad ainult seda, mida neile treeningu ajal õpetati. Nad ei saa vastata küsimustele sinu ettevõtte poliitikate, projektdokumentatsiooni või muu info kohta, mida neile ei ole õpetatud.

RAG (taastepõhine loomine) lahendab selle probleemi. Selle asemel, et pead mudelit sinu infole õpetama (mis on kallis ja ebaefektiivne), annad mudelile võimaluse otsida sinu dokumentidest. Kui keegi esitab küsimuse, leiab süsteem asjakohase info ja lisab selle päringusse. Mudel vastab siis selle taasesitatud konteksti põhjal.

Mõtle RAG-le kui viitetuumale mudeli jaoks. Kui sa küsid küsimust, teeb süsteem:

1. **Kasutaja päring** – sa esitad küsimuse
2. **Embedimine** – teisendab sinu küsimuse vektoriks
3. **Vektoripäring** – leiab sarnased dokumendi tükid
4. **Konteksti kokku panemine** – lisab vastavad tükid päringusse
5. **Vastus** – LLM genereerib vastuse põhinedes kontekstile

See seab mudeli vastused sinu tegelikele andmetele, mitte ei sõltu ainult treeningteadmistest või ei leiuta vastuseid ise.

## Eeldused

- Läbitud [Moodul 00 - Kiire algus](../00-quick-start/README.md) (lihtsa RAG näite jaoks, mida selles moodulis hiljem käsitletakse)
- Läbitud [Moodul 01 - Sissejuhatus](../01-introduction/README.md) (kinnitatud Azure OpenAI ressursid, sh `text-embedding-3-small` manustumudel)
- Juurekaustas olemas `.env` fail Azure mandaadiga (loodud `azd up` käsklusega Moodulis 01)

> **Märkus:** Kui sa ei ole veel Moodulit 01 lõpetanud, järgi esmalt seal olevaid paigaldusjuhiseid. Käsk `azd up` käivitab nii GPT vestlusmudeli kui ka manustumudeli, mida see moodul kasutab.

## RAG mõistmine

Järgmine diagramm illustreerib põhimõtet: selle asemel, et toetuda ainult mudeli treeningandmetele, annab RAG mudelile viiteraamatu sinu dokumentidest, mida ta saab enne vastuse genereerimist kasutada.

<img src="../../../translated_images/et/what-is-rag.1f9005d44b07f2d8.webp" alt="Mis on RAG" width="800"/>

*See diagramm näitab erinevust tavapärase LLM-i (mis juhindub treeningandmetest) ning RAG-toega LLM-i vahel (mis esmalt otsib sinu dokumente).*

Nii on tükid omavahel seotud. Kasutaja päring läbib neli sammu — manustamine, vektoripäring, konteksti kokku panemine ja vastuse genereerimine — igaüks toetub eelnevale:

<img src="../../../translated_images/et/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektuur" width="800"/>

*See diagramm näitab RAG täielikku torujuhtme — kasutaja päring läbib manustamise, vektoripäringu, konteksti kokkupaneku ja vastuse genereerimise.*

Ülejäänud moodul kirjeldab iga etappi detailsemalt, koos koodinäidete ja diagrammidega.

### Millist RAG lähenemist see juhend kasutab?

LangChain4j pakub kolme võimalust RAG-i rakendamiseks, igaühel erinev abstraktsioonitase. Allolev diagramm võrdleb neid kõrvuti:

<img src="../../../translated_images/et/rag-approaches.5b97fdcc626f1447.webp" alt="Kolm RAG lähenemist LangChain4j-s" width="800"/>

*See diagramm võrdleb kolme LangChain4j RAG lähenemist — Lihtne, Natiivne ja Täiustatud — näidates nende peamisi komponente ja kasutusolukordi.*

| Lähenemine | Mida see teeb | Kompromiss |
|---|---|---|
| **Lihtne RAG** | Seob kõik automaatselt `AiServices` ja `ContentRetriever` kaudu. Sa märgid liidese, lisad taastele, ja LangChain4j haldab manustamist, otsingut ja päringu kokkupanekut tagaplaanil. | Vähe koodi, aga sa ei näe täpselt, mis igas etapis toimub. |
| **Natiivne RAG** | Sa kutsud ise manustusmudelit, otsid andmestikus, ehitad päringu ja genereerid vastuse — sammhaaval ja selgelt. | Rohkem koodi, aga iga etapp on nähtav ja kohandatav. |
| **Täiustatud RAG** | Kasutab `RetrievalAugmentor` raamistikku, kus on võimalik lisada päringumuundureid, marsruutereid, järjestajaid ja konteksti lisajaid tootmisklassi torujuhtmeks. | Kõrgeim paindlikkus, kuid ka märkimisväärne keerukus. |

**See juhend kasutab Natiivset lähenemist.** Iga RAG torujuhtme samm — päringu manustamine, vektoripäringu tegemine, konteksti koostamine ja vastuse genereerimine — on selgelt kirjas [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) failis. See on teadlik valik: õppematerjalina on tähtsam, et näeksid ja mõistaksid iga etappi, kui et kood oleks vähendatud. Kui oled senise tööga mugav, võid kiirete prototüüpide jaoks minna Lihtsa RAG juurde või tootmisprojektide jaoks Täiustatud RAG-i.

> **💡 Oled Lihtsat RAG-i juba näinud?** [Kiire alguse moodul](../00-quick-start/README.md) sisaldab dokumendi küsimuste ja vastuste näidet ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), mis kasutab Lihtsat RAG-i — LangChain4j haldab manustamist, otsingut ja päringu kokkupanekut automaatselt. See moodul võtab järgmise sammu, avades selle torujuhtme nii, et sa näed ja juhid iga etappi ise.

Allolev diagramm näitab Lihtsa RAG torujuhet eelmainitud Kiire alguse näitest. Märka, kuidas `AiServices` ja `EmbeddingStoreContentRetriever` peidavad kogu keerukuse — laed dokumendi, lisad taastaja ja saad vastused. Selle mooduli Natiivne lähenemine avab neid peidetud samme:

<img src="../../../translated_images/et/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Lihtne RAG torujuhe - LangChain4j" width="800"/>

*See diagramm näitab Lihtsa RAG torujuhet `SimpleReaderDemo.java` failist. Võrdle seda Natiivse lähenemisega selles moodulis: Lihtne RAG peidab manustamise, taaste ja päringu kokkupaneku `AiServices` ja `ContentRetriever` taha — laed dokumendi, lisad taastaja ja saad vastused. Selle mooduli Natiivne lähenemine avab selle torujuhtme, nii et sa kutsud iga etappi (manusta, otsi, kogu kontekst, genereeri) ise, saades täieliku ülevaate ja kontrolli.*

## Kuidas see töötab

Selles moodulis jaguneb RAG torujuhe neljaks sammuks, mis toimuvad järjest iga kord, kui kasutaja esitab küsimuse. Esiteks analüüsitakse üleslaaditud dokument, mis jagatakse haldamiseks sobivateks tükkideks. Need tükid teisendatakse vektoriteks (manustusteks) ja salvestatakse, et neid saaks matemaatiliselt võrrelda. Päringu korral teeb süsteem semantilise otsingu, et leida kõige asjakohasemad tükid, ning edastab need kontekstina LLM-ile vastuse genereerimiseks. Järgnevad osad selgitavad iga sammu koos koodiga.

Vaatame esmalt dokumendi töötlemist.

### Dokumendi töötlemine

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kui sa dokumenti üles laed, süsteem analüüsib selle (PDF või lihttekst), lisab metainfot nagu failinimi ja seejärel jagab selle osadeks — väiksemateks tükkideks, mis mahuvad mudeli kontekstiväljale mugavalt. Need tükid kattuvad veidi, et piiril ei kaoks oluline kontekst.

```java
// Analüüsi üleslaaditud faili ja mässi see LangChain4j dokumendi sisse
Document document = Document.from(content, metadata);

// Jaga 300-tokenilisteks tükkideks, millel on 30-tokeniline kattuvus
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Allolev diagramm illustreerib seda visuaalselt. Märka, kuidas iga tükk jagab mõningaid tokeneid naaber tükkidega — 30-tokeni kattuvus tagab, et oluline kontekst ei jää vahele:

<img src="../../../translated_images/et/document-chunking.a5df1dd1383431ed.webp" alt="Dokumendi tükeldamine" width="800"/>

*See diagramm näitab dokumenti, mis on jagatud 300 tokeni suurusteks tükkideks, kus on 30-tokeniline kattuvus, säilitades konteksti tükkide piiridel.*

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chat’iga:** Ava [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ja küsi:
> - "Kuidas LangChain4j jagab dokumente tükkideks ja miks on kattuvus oluline?"
> - "Mis on optimaalne tüki suurus erinevate dokumentide jaoks ja miks?"
> - "Kuidas käsitleda dokumente mitmes keeles või erilise vormindusega?"

### Manuste loomine

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Iga tükk teisendatakse numbriliseks kujutiseks, nn manustuseks — tähenduse teisendaja arvudeks. Manustumudel ise ei ole „tark“ nagu vestlusmudel; ta ei suuda juhiseid järgida, loogiliselt põhjendada ega küsimustele vastata. Mida ta teeb, on tekstikildude asukoha määramine matemaatilises ruumis, kus lähedased tähendused paiknevad üksteisele lähedal — „auto“ on lähedal „autole“, „tagasimakse poliitika“ lähedal „raha tagastamisele“. Mõtle vestlusmudelile kui inimesele, kellega rääkida; manustumudel on tõhus ja hea süsteem asjade sortimiseks.

Allolev diagramm illustreerib seda mõistet — tekst läheb sisse, numbrilised vektorid tulevad välja, ning sarnased tähendused tähistatakse vektoriruumi lähedal:

<img src="../../../translated_images/et/embedding-model-concept.90760790c336a705.webp" alt="Manustumudeli kontseptsioon" width="800"/>

*See diagramm näitab, kuidas manustumudel teisendab teksti numbriliseks vektoriks, pannes sarnased tähendused — nagu „auto“ ja „automaat“ — üksteisele lähedale vektoriruumis.*

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

Järgmine klassidiagramm näitab kahte sõltumatut voogu RAG torujuhtmes ja LangChain4j klasse, mis neid realiseerivad. **Sisseimemise voog** (käib ühe korra üleslaadimisel) jagab dokumendi tükkideks, manustab need ja salvestab `.addAll()` kaudu. **Päringuvoog** (igakordse kasutajapäringu ajal) manustab küsimuse, otsib salvestusest `.search()` kaudu ja edastab sobitatud konteksti vestlusmudelile. Mõlemad vood kohtuvad jagatud `EmbeddingStore<TextSegment>` liidesel:

<img src="../../../translated_images/et/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klassid" width="800"/>

*See diagramm näitab kahte RAG voogu — sisseimemist ja päringut — ja nende ühendust ühise EmbeddingStore kaudu.*

Kui manused on salvestatud, koonduvad sarnased sisud loogiliselt vektoriruumi lähedale. Allolev visualisatsioon näitab, kuidas seotud teemad paistavad 3D-ruumis klastritena, mis teeb semantilise otsingu võimalikuks:

<img src="../../../translated_images/et/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektormanuste ruum" width="800"/>

*See visualisatsioon näitab, kuidas seotud dokumendid rühmituvad 3D vektoriruumi, moodustades erinevad grupid nagu tehnilised dokumendid, ärireeglid ja korduma kippuvad küsimused.*

Kui kasutaja otsib, järgib süsteem nelja sammu: manustab dokumendid kord, manustab päringu iga otsingu ajal, võrdleb päringu vektorit kõigi salvestatud vektoritega kosiinussarnasuse järgi ja tagastab tipp-K kõrgeima skooriga tükid. Allolev diagramm kirjeldab iga sammu ja kaasatud LangChain4j klasse:

<img src="../../../translated_images/et/embedding-search-steps.f54c907b3c5b4332.webp" alt="Manustamise otsingu sammud" width="800"/>

*See diagramm näitab nelja sammu manustamise otsimusprotsessis: manusta dokumendid, manusta päring, võrdle vektoreid kosiinussarnasuse alusel ja tagasta tipp-K tulemid.*

### Semantiline otsing

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kui küsid küsimust, teisendatakse sinu küsimus samuti manustuseks. Süsteem võrdleb sinu päringu manustust kõigi dokumendi tükkide manustustega. Ta leiab kõige sarnasema tähendusega tükid — mitte ainult märksõnade kattuvuse põhjal, vaid tõelise semantilise sarnasuse alusel.

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

Allolev diagramm võrdleb semantilist otsingut tavapärase märksõnapõhise otsinguga. Märksõnapõhine „sõiduk“ otsing jätab leidmata tüki „autod ja veoautod“, kuid semantiline otsing mõistab, et need tähendavad sama asja ja tagastab selle kõrge skooriga tulemusena:

<img src="../../../translated_images/et/semantic-search.6b790f21c86b849d.webp" alt="Semantiline otsing" width="800"/>

*See diagramm võrdleb märksõnapõhist otsingut semantilise otsinguga, näidates kuidas viimane leiab mõistepõhiseid seotud sisusid ka siis, kui täpsed märksõnad ei lange kokku.*
Masinapõhiselt mõõdetakse sarnasust kasutades kosinussarnasust — sisuliselt küsides „Kas need kaks noolt osutavad sama suunda?“ Kaks lõiku võivad kasutada täiesti erinevaid sõnu, kuid kui nende tähendus on sama, osutavad nende vektorid samasse suunda ja skoorivad ligikaudu 1.0:

<img src="../../../translated_images/et/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinussarnasus" width="800"/>

*See diagramm illustreerib kosinussarnasust kui nurka manustatud vektorite vahel — rohkem joondunud vektorid skoorivad lähemale 1.0-le, mis näitab suuremat semantilist sarnasust.*

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava faili [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ja küsi:
> - "Kuidas töötab sarnasuse otsing manustega ja mis määrab skoori?"
> - "Millist sarnasuse läve peaksin kasutama ja kuidas see tulemusi mõjutab?"
> - "Kuidas käituda juhtudel, kui asjakohaseid dokumente ei leita?"

### Vastuse Genereerimine

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kõige asjakohasemad lõigud koostatakse struktureeritud küsimuseks, mis sisaldab selgeid juhiseid, päringukonteksti ja kasutaja küsimust. Mudel loeb neid konkreetseid lõike ja vastab selle info põhjal — ta saab kasutada ainult seda, mis tal ees on, mis takistab hallutsinatsioone.

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

Allolev diagramm näitab seda kokkupanekut tegevuses — otsingust saadud kõige paremini skoorivad lõigud süstitakse küsimuse malli ja `OpenAiOfficialChatModel` genereerib kinnitatud vastuse:

<img src="../../../translated_images/et/context-assembly.7e6dd60c31f95978.webp" alt="Konteksti Kokkupanek" width="800"/>

*See diagramm näitab, kuidas kõrgeima skooriga lõigud koostatakse struktureeritud küsimuseks, võimaldades mudelil genereerida kinnitatud vastuse teie andmetest.*

## Rakenduse Käivitamine

**Kontrolli deployd:**

Veendu, et `.env` fail asub juurkataloogis Azure'i mandaatidega (loodud moodulis 01). Käivita see mooduli kataloogist (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT-i, API_KEY-d, DEPLOYMENT-i
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tuleks näidata AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendust:**

> **Märkus:** Kui alustasid juba kõigi rakendustega `./start-all.sh` abil juurkataloogist (nagu kirjeldatud moodulis 01), töötab see moodul juba pordil 8081. Võid alltoodud käsud vahele jätta ja minna otse http://localhost:8081.

**Valik 1: Spring Boot Dashboardi kasutamine (Soovitatav VS Code kasutajatele)**

Arenduscontainer sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset kasutajaliidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle aktiivsusribalt VS Code vasakul pool (otsige Spring Boot ikooni).

Spring Boot Dashboardilt saad:
- Näha kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt kliki "rag" kõrval olevale play-nupule selle mooduli käivitamiseks või alusta korraga kõiki mudeleid.

<img src="../../../translated_images/et/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*See kuvatõmmis näitab Spring Boot Dashboardi VS Codes, kus saad visuaalselt rakendusi käivitada, peatada ja jälgida.*

**Valik 2: Shell skriptide kasutamine**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juure kataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurekataloogist
.\start-all.ps1
```

Või alusta ainult seda moodulit:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujaid juurpõhjast `.env` failist ja ehitavad JAR-id, kui need puuduvad.

> **Märkus:** Kui soovid enne käivitust kõik moodulid käsitsi ehitada:
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

Ava brauseris http://localhost:8081.

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

## Rakenduse Kasutamine

Rakendus pakub veebiliidest dokumentide üleslaadimiseks ja küsimiseks.

<a href="images/rag-homepage.png"><img src="../../../translated_images/et/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG rakenduse kasutajaliides" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*See kuvatõmmis näitab RAG rakenduse kasutajaliidest, kus saad üles laadida dokumente ja esitada küsimusi.*

### Dokumendi üleslaadimine

Alusta dokumendi üleslaadimisest – testimisel sobivad kõige paremini TXT failid. Selle kataloogi on lisatud `sample-document.txt`, mis sisaldab infot LangChain4j omaduste, RAG implementatsiooni ja parimate tavade kohta – ideaalne süsteemi testimiseks.

Süsteem töötleb su dokumendi, jagab selle lõikudeks ja loob iga lõigu jaoks manused. See toimub automaatselt kohe peale üleslaadimist.

### Küsimuste Esitamine

Nüüd esita konkreetseid küsimusi dokumenti sisu kohta. Proovi faktilisi küsimusi, mis on dokumendis selgelt välja toodud. Süsteem otsib asjakohased lõigud, lisab need küsimuse konteksti ja genereerib vastuse.

### Allika Viidete Kontrollimine

Pange tähele, et iga vastus sisaldab allika viiteid koos sarnasuse skooridega. Need skoorid (0 kuni 1) näitavad, kui asjakohane iga lõik sinu küsimusega oli. Kõrgemad skoorid tähendavad paremaid vasteid. Sellega saad vastuse kontrollida allikmaterjali vastu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/et/rag-query-results.6d69fcec5397f355.webp" alt="RAG päringu tulemused" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*See kuvatõmmis näitab päringu tulemusi koos genereeritud vastuse, allika viidete ja iga leitud lõigu asjakohasuse skooriga.*

### Küsitlusega Katsetamine

Proovi erinevat tüüpi küsimusi:
- Konkreetsed faktid: "Mis on peamine teema?"
- Võrdlused: "Mis vahe on X ja Y vahel?"
- Kokkuvõtted: "Kokkuvõtke Z põhiteemad"

Jälgi, kuidas asjakohasuse skoorid muutuvad vastavalt sellele, kui hästi su küsimus dokumenti sobib.

## Põhimõisted

### Lõikude Jagamise Strateegia

Dokumendid jagatakse 300 märgiga lõikudeks, millel on 30 märgiline kattuvus. See tasakaal tagab, et iga lõik sisaldab piisavalt konteksti, et olla tähenduslik, samas jäädes piisavalt väikeseks, et mahutada mitut lõiku ühte küsimusse.

### Sarnasuse Skoorid

Iga leitud lõikuga tuleb kaasas sarnasuse skoor 0 kuni 1, mis näitab, kui lähedalt see kasutaja küsimusele vastab. Alljärgnev diagramm visualiseerib skooride vahemikke ja seda, kuidas süsteem neid tulemuste filtreerimiseks kasutab:

<img src="../../../translated_images/et/similarity-scores.b0716aa911abf7f0.webp" alt="Sarnasuse Skoorid" width="800"/>

*See diagramm näitab skooride vahemikke 0 kuni 1, kus minimaalne lävi on 0.5, mis filtreerib välja asjakohatud lõigud.*

Skoorid jäävad vahemikku 0 kuni 1:
- 0.7-1.0: Väga asjakohane, täpne vaste
- 0.5-0.7: Asjakohane, hea kontekst
- Alla 0.5: Filtreeritud välja, liiga erinev

Süsteem tagastab ainult lõigud, mis ületavad miinimalläve, et tagada kvaliteet.

Manused töötavad hästi, kui tähendused klastrites selged on, kuid neil on ka pimedad kohad. Järgnev diagramm näitab levinud ebaõnnestumise viise — liiga suured lõigud annavad ebaselged vektorid, liiga väikesed lõigud on kontekstist vaesed, mitmetähenduslikud terminid viitavad mitmele klastrile ja täpse vaste otsingud (ID-d, osanumber) ei tööta manustega sugugi:

<img src="../../../translated_images/et/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Manuse ebaõnnestumise režiimid" width="800"/>

*See diagramm näitab levinud manustamise ebaõnnestumise vorme: liiga suured lõigud, liiga väikesed lõigud, mitmetähenduslikud terminid, mis viitavad mitmele klastrile, ning täpsed vasteotsingud nagu ID-d.*

### Mälu Põhine Andmesalvestus

See moodul kasutab lihtsuse huvides mälusalvestust. Rakenduse taaskäivitamisel kaovad üles laaditud dokumendid. Tootmiskeskkonnas kasutatakse püsivaid vektorandmebaase nagu Qdrant või Azure AI Search.

### Konteksti Akna Halduse

Igal mudelil on maksimaalne konteksti aken. Sa ei saa lisada kõiki lõike väga suurest dokumendist. Süsteem tagastab kõige asjakohasemad N lõiku (vaikimisi 5), et jääda piiridesse, kuid pakkuda piisavalt konteksti täpsete vastuste jaoks.

## Millal RAG Loeb

RAG ei ole alati õige lahendus. Järgmine otsustusjuhend aitab sul otsustada, millal RAG lisab väärtust võrreldes lihtsamate lähenemisviisidega — nagu sisu otse küsimusse lisamine või mudeli sisseehitatud teadmiste kasutamine:

<img src="../../../translated_images/et/when-to-use-rag.1016223f6fea26bc.webp" alt="Millal kasutada RAG'i" width="800"/>

*See diagramm näitab otsustusjuhendit, millal RAG lisab väärtust ja millal piisavad lihtsamad lähenemised.*

## Järgmised Sammud

**Järgmine moodul:** [04-tools - Tehisintellekti agendid tööriistadega](../04-tools/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 02 - Küsitluste inseneritöö](../02-prompt-engineering/README.md) | [Tagasi Avalehile](../README.md) | [Järgmine: Moodul 04 - Tööriistad →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüdleme täpsuse poole, palun pange tähele, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Algset dokumenti selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalse inimtõlke teenust. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või väärinterpreteerimiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->