# Moodul 03: RAG (Retrieval-Augmented Generation)

## Sisukord

- [Videokäitlus](../../../03-rag)
- [Mida sa õpid](../../../03-rag)
- [Eeltingimused](../../../03-rag)
- [RAG mõistmine](../../../03-rag)
  - [Millist RAG lähenemist see õpetus kasutab?](../../../03-rag)
- [Kuidas see töötab](../../../03-rag)
  - [Dokumendi töötlemine](../../../03-rag)
  - [Mõistete loomine](../../../03-rag)
  - [Semantiline otsing](../../../03-rag)
  - [Vastuse genereerimine](../../../03-rag)
- [Rakenduse käivitamine](../../../03-rag)
- [Rakenduse kasutamine](../../../03-rag)
  - [Dokumendi üleslaadimine](../../../03-rag)
  - [Küsimuste esitamine](../../../03-rag)
  - [Allikaviidete kontrollimine](../../../03-rag)
  - [Küsimustega eksperimenteerimine](../../../03-rag)
- [Põhimõisted](../../../03-rag)
  - [Tükeldamisstrateegia](../../../03-rag)
  - [Sarnasusskoorid](../../../03-rag)
  - [Mälusalvestus](../../../03-rag)
  - [Kontekstiakna haldamine](../../../03-rag)
- [Millal RAG on oluline](../../../03-rag)
- [Järgmised sammud](../../../03-rag)

## Videokäitlus

Vaadake seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada: [RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Mida sa õpid

Eelnevates moodulites õppisid, kuidas vestelda tehisintellektiga ja korraldada oma päringuid tõhusalt. Kuid on üks põhimõtteline piirang: keelereguleerijad teavad vaid seda, mida nad on treeningu ajal õppinud. Nad ei saa vastata küsimustele teie ettevõtte poliitikate, teie projekti dokumentatsiooni või muude nende treeningus mitteolevate andmete kohta.

RAG (Retrieval-Augmented Generation) lahendab selle probleemi. Selle asemel, et proovida mudelit teie infole õpetada (mis on kallis ja ebapraktiline), annate mudelile võimaluse otsida läbi teie dokumentide. Kui keegi esitab küsimuse, leiab süsteem asjakohase info ja lisab selle päringusse. Mudel vastab siis selle leitud konteksti põhjal.

Mõelge RAG-ile kui mudelile, kellel on viideteraamatukogu. Kui te küsite küsimuse, teeb süsteem:

1. **Kasutaja päring** – te esitate küsimuse
2. **Embedimine** – teisendab teie küsimuse vektoriks
3. **Vektorotsing** – leiab sarnased dokumendi tükid
4. **Konteksti kokkupanek** – lisab asjakohased tükid päringusse
5. **Vastus** – LLM genereerib vastuse konteksti põhjal

See kinnitab mudeli vastused teie tegelikele andmetele, selle asemel, et tugineda vaid treeningu teadmistel või leiutada vastuseid.

## Eeltingimused

- Läbitud [Moodul 00 - Kiire algus](../00-quick-start/README.md) (selleks, et kasutada ülal viidatud Easy RAG näidet)
- Läbitud [Moodul 01 - Sissejuhatus](../01-introduction/README.md) (paigaldatud Azure OpenAI ressursid, sealhulgas `text-embedding-3-small` embedimismudel)
- `.env` fail juurkaustas koos Azure kaustaga (loodud käsuga `azd up` Moodulis 01)

> **Märkus:** Kui te pole Moodulit 01 lõpetanud, järgige seal esmalt paigaldusjuhiseid. `azd up` käsk paigaldab nii GPT vestlusmudeli kui ka selle mooduli kasutatava embedimismudeli.

## RAG mõistmine

Allolev diagramm illustreerib põhimõttelist mõtet: selle asemel, et tugineda vaid mudeli treeningandmetele, annab RAG mudelile teie dokumentide viideteraamatu, kuhu ta saab iga vastuse genereerimise eel pöörduda.

<img src="../../../translated_images/et/what-is-rag.1f9005d44b07f2d8.webp" alt="Mis on RAG" width="800"/>

*See diagramm näitab erinevust tavapärase LLM-i (mis arvab treeningandmete põhjal) ja RAG-ga täiustatud LLM-i vahel (mis konsulteerib esmalt teie dokumentidega).*

Nii on osad lõimunult ühendatud lõpust lõpuni. Kasutaja küsimus läbib neli etappi — embedimine, vektorotsing, konteksti kogumine ja vastuse genereerimine — igaüks toetub eelnevale:

<img src="../../../translated_images/et/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektuur" width="800"/>

*See diagramm näitab RAG lõpust lõpuni tööd – kasutaja päring läbib embedimise, vektorotsingu, konteksti kokkupaneku ja vastuse loomise.*

Ülejäänud moodulis käsitletakse iga etappi põhjalikult koos koodiga, mida saab jooksutada ja muuta.

### Millist RAG lähenemist see õpetus kasutab?

LangChain4j pakub kolme viisi RAG rakendamiseks, igaüks erineval abstraktsiooni tasemel. Allolev diagramm võrdleb neid kõrvuti:

<img src="../../../translated_images/et/rag-approaches.5b97fdcc626f1447.webp" alt="Kolm RAG lähenemist LangChain4j-s" width="800"/>

*See diagramm võrdleb kolme LangChain4j RAG lähenemist – Easy, Native ja Advanced – näidates nende põhikomponente ja kasutusolukordi.*

| Lähenemine | Mida see teeb | Kompromiss |
|---|---|---|
| **Easy RAG** | Kõik on automaatselt ühendatud `AiServices`-i ja `ContentRetriever`-i kaudu. Te annoteerite liidese, lisate retriiveri ja LangChain4j haldab embedimist, otsingut ja päringu koostamist tagaplaanil. | Vähe koodi, aga ei näe iga sammu toimimist. |
| **Native RAG** | Te kutsute embedimismudeli, otsite andmebaasist, ehitate päringu ja genereerite vastuse ise – üks samm korraga, selgelt. | Rohkem koodi, kuid iga faas on nähtav ja muudetav. |
| **Advanced RAG** | Kasutab `RetrievalAugmentor` raamistikku koos plug-in päringumuutjate, marsruutijate, uuesti järjestajate ja sisu süstijatega tootmisklassi torujuhtmete jaoks. | Maksimaalne paindlikkus, kuid oluliselt kõrgem keerukus. |

**See õpetus kasutab Native lähenemist.** Iga samm RAG torustikus — päringu embedimine, vektorpõhine otsing, konteksti kokku panemine ja vastuse genereerimine — on kirjas selgelt ja otse [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). See on teadlik valik: õppematerjalina on olulisem, et näeksite ja mõistaksite kõiki etappe, kui et kood oleks minimeeritud. Kui olete aru saanud, kuidas osad omavahel sobivad, saate edasi liikuda Easy RAG-lt kiireks prototüüpimiseks või Advanced RAG-le tootmissüsteemide jaoks.

> **💡 Olete juba näinud Easy RAG-i?** [Kiire alguse moodul](../00-quick-start/README.md) sisaldab Dokumentide KÜ näidet ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), mis kasutab Easy RAG lähenemist – LangChain4j haldab embedimist, otsingut ja päringu kokkupanekut automaatselt. See moodul teeb järgmise sammu, murdes selle torustiku lahti, nii et saate iga sammu näha ja kontrollida.

<img src="../../../translated_images/et/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG torustik - LangChain4j" width="800"/>

*See diagramm näitab Easy RAG torustikku moodulis `SimpleReaderDemo.java`. Võrrelge seda Native lähenemisega selles moodulis: Easy RAG peidab embedimise, leidmise ja päringu koostamise `AiServices` ja `ContentRetriever` taha – te laete dokumendi üles, lisate retriiveri ja saate vastuseid. Native lähenemine selles moodulis avab torustiku nii, et te kutsute iga sammu (embedi, otsing, konteksti kokkupanek, genereerimine) ise, andes täieliku nähtavuse ja kontrolli.*

## Kuidas see töötab

Selles moodulis jaguneb RAG torustik neljaks etapiks, mis töötavad järjestikku iga kord, kui kasutaja küsib. Esiteks töödeldakse ja tükeldatakse üles laaditud dokument hallatavateks osadeks. Need tükid teisendatakse siis vektoriteks ehk mõisteteks ja salvestatakse, et neid saaks matemaatiliselt võrrelda. Kui päring jõuab, sooritatakse semantiline otsing, mis leiab kõige asjakohasemad tükid, ja lõpuks antakse need LLM-ile vastuse genereerimiseks kontekstina. Järgnevad sektsioonid näitavad iga etappi koos koodiga ja diagrammidega. Vaatleme kõigepealt esimest sammu.

### Dokumendi töötlemine

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kui laadite dokumendi üles, parsib süsteem selle (PDF või lihttekst), lisab metainfot nagu failinimi, ja seejärel lõikab selle tükkideks — väiksemateks osadeks, mis mahuvad mugavalt mudeli kontekstiaknasse. Tükid kattuvad veidi, et piiril ei kaoks oluline kontekst.

```java
// Analüüsi üles laaditud fail ja seo see LangChain4j dokumendi sisse
Document document = Document.from(content, metadata);

// Jaga 300-sõnalisteks tükkideks 30-sõnalise kattuvusega
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Allolev diagramm illustreerib seda visuaalselt. Märkige, et iga tükk jagab mõningaid tokeneid oma naabritega – 30 tokeni kattumine tagab, et olulised kontekstid ei kuku läbi:

<img src="../../../translated_images/et/document-chunking.a5df1dd1383431ed.webp" alt="Dokumendi tükkideks jagamine" width="800"/>

*See diagramm näitab dokumendi lõhkumist 300-tokenseks tükkideks 30-tokenise kattumisega, säilitades konteksti tükkide piiride juures.*

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Avage [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ja küsige:
> - "Kuidas LangChain4j lõikab dokumendid tükkideks ja miks on kattumine oluline?"
> - "Mis on optimaalne tükisuurus erinevate dokumenditüüpide jaoks ja miks?"
> - "Kuidas käituda dokumentidega, mis on mitmes keeles või erilise formaadiga?"

### Mõistete loomine

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Iga tükk teisendatakse numbriliseks esitluseks, mida nimetatakse embedimiseks – sisuliselt tähenduse muutmiseks numbriteks. Embedimismudel ei ole "intelligentne" nagu vestlusmudel; ta ei oska juhiseid järgida, mõelda ega vastata küsimustele. Kuid ta suudab teksti pindlikku matemaatilisse ruumi mapida selliselt, et sarnased tähendused jäävad kõrvuti – "auto" lähedal "automaobile", "tagastuspoliitika" lähedal "raha tagastamine" mõistele. Mõelge vestlusmudelile kui inimesega suhtlemisele; embedimismudel on väga hea arhiveerimissüsteem.

<img src="../../../translated_images/et/embedding-model-concept.90760790c336a705.webp" alt="Embedimismudeli kontseptsioon" width="800"/>

*See diagramm näitab, kuidas embedimismudel teisendab teksti numbriliseks vektoriks, asetades sarnased tähendused – nagu „auto“ ja „automobil“ – vektorruumis lähedale.*

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

Allolev klassidiagramm näitab kahte eraldi voogu RAG torustikus ja LangChain4j klasse, mis neid rakendavad. **Sisseviimise voog** (jookseb üleslaadimisel) jagab dokumendi, embedib tükid ja salvestab need `.addAll()` kaudu. **Päringu voog** (jookseb iga kasutaja päringu ajal) embedib küsimuse, otsib andmebaasist `.search()` kaudu ja edastab leitud konteksti vestlusmudelile. Mõlemad vood on ühendatud ühise `EmbeddingStore<TextSegment>` liidese kaudu:

<img src="../../../translated_images/et/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klassid" width="800"/>

*See diagramm näitab kahte RAG voogu – sisseviimist ja päringut – ja nende ühendamist ühise EmbeddingStore kaudu.*

Kui embedid on salvestatud, koonduvad sarnased sisud loomulikult vektoriruumi lähedale. Allolev visualisatsioon näitab, kuidas lähedased teemad kogunevad kõrvuti, tehes semantilise otsingu võimalikuks:

<img src="../../../translated_images/et/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorembedide ruum" width="800"/>

*See visualisatsioon näitab, kuidas seotud dokumendid kogunevad 3D vektoriruumi lähestikku, teematena nagu tehnilised dokumendid, ärireeglid ja KKK moodustades eraldiseisvad grupid.*

Kui kasutaja otsib, järgib süsteem nelja sammu: embedib dokumendid üks kord, embedib otsisõna igal otsimisel, võrdleb päringu vektorit kõigi salvestatud vektoritega kosinuselise sarnasuse abil ja tagastab tipptasemel K tükid. Diagramm allpool näitab neid samme ja LangChain4j klasse, mis osalevad:

<img src="../../../translated_images/et/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedimismeetod otsingu sammud" width="800"/>

*See diagramm näitab neljaastmelist embedimise ja otsingu protsessi: dokumentide embedimine, päringu embedimine, vektorite võrdlemine kosinussarnasusega ja tipptulemuste tagastamine.*

### Semantiline otsing

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kui esitate küsimuse, muutub ka teie küsimus embediks. Süsteem võrdleb teie küsimuse embedimist kõigi dokumendi tükikeste embedimistega. Ta leiab kõige sarnasema tähendusega tükid — mitte ainult märksõnade kokkulangevused, vaid tegeliku semantilise sarnasuse alusel.

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

Allolev diagramm võrdleb semantilist otsingut traditsioonilise märksõnapõhise otsinguga. Märksõnapõhine otsing sõnale "sõiduk" jätab läbi jahtimata tükid, mis käsitlevad "autosid ja veoautosid", kuid semantiline otsing mõistab, et tegu on sama mõistega ja tagastab selle kõrge hinnanguga vastusena:

<img src="../../../translated_images/et/semantic-search.6b790f21c86b849d.webp" alt="Semantiline otsing" width="800"/>

*See diagramm võrdleb märksõnapõhist otsingut semantilise otsinguga, näidates, kuidas semantiline otsing leiab kontseptuaalselt seotud sisu, isegi kui täpsed märksõnad erinevad.*

Tegelikult mõõdetakse sarnasust kosinussarnasuse abil – see on nagu küsimus "kas need kaks noolt osutavad samasse suunda?" Kaks tükki võivad sisaldada täiesti erinevaid sõnu, kuid kui nende tähendus on sama, siis nende vektorid on suunatud samasse suunda ja koguvad skoori lähedale 1.0:

<img src="../../../translated_images/et/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinussarnasus" width="800"/>

*See diagramm illustreerib kosinussarnasust kui embedding vektorite vahelist nurka – rohkem sünkroniseeritud vektorid saavad skoori, mis läheneb 1.0-le, mis näitab suuremat semantilist sarnasust.*
> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ja küsi:
> - "Kuidas töötab sarnasuse otsing emeddingute abil ja mis määrab skoori?"
> - "Millist sarnasuse läve tuleks kasutada ja kuidas see tulemusi mõjutab?"
> - "Kuidas käsitleda juhtumeid, kus asjakohaseid dokumente ei leita?"

### Vastuse genereerimine

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kõige asjakohasemad lõigud koondatakse struktureeritud päringuks, mis sisaldab selgesõnalisi juhiseid, leitud konteksti ja kasutaja küsimust. Mudel loeb need konkreetsed lõigud läbi ja vastab selle info põhjal — ta saab kasutada ainult seda, mis on tema ees, mis takistab hallucineerimist.

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

Järgmine diagramm näitab seda koostamisprotsessi tegutsemises — otsingufaasi kõrgeima skooriga lõigud süstitakse päringu mallisse ja `OpenAiOfficialChatModel` genereerib põhjendatud vastuse:

<img src="../../../translated_images/et/context-assembly.7e6dd60c31f95978.webp" alt="Konteksti koostamine" width="800"/>

*See diagramm näitab, kuidas kõrgeima skooriga lõigud koondatakse struktureeritud päringusse, võimaldades mudelil luua põhjendatud vastuse teie andmetest.*

## Rakenduse käivitamine

**Veendu, et juurutus on korrektne:**

Veendu, et juurkaustas oleks olemas `.env` fail Azure volitustega (loodud moodulis 01):

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT-i, API_KEY-d, DEPLOYMENT-i
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Tuleks näidata AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendust:**

> **Märkus:** Kui oled juba kõik rakendused käivitanud skriptiga `./start-all.sh` moodulist 01, siis see moodul jookseb juba pordil 8081. Võid alljärgnevaid käivituskäske vahele jätta ja minna otse aadressile http://localhost:8081.

**Variant 1: Kasutades Spring Boot Dashboardi (soovitatav VS Code kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset kasutajaliidest kõigi Spring Boot rakenduste haldamiseks. Selle leiad VS Code vasakpoolsest Activity Barist (otsi Spring Boot ikooni).

Spring Boot Dashboardi kaudu saad:
- Näha kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klõpsuga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsa mängunupule "rag" kõrval, et käivitada see moodul, või käivita korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*See ekraanipilt näitab Spring Boot Dashboardi VS Code’s, kus saad rakendusi visuaalselt käivitada, peatada ja jälgida.*

**Variant 2: Kasutades shell skripte**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
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

Mõlemad skriptid loevad automaatselt keskkonnamuutujad juurkaustas olevast `.env` failist ja ehitavad JAR-id, kui neid veel ei ole.

> **Märkus:** Kui soovid enne käivitamist kõik moodulid käsitsi ehitada:
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

Ava aadress http://localhost:8081 oma brauseris.

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

## Rakenduse kasutamine

Rakendus pakub veebiliidest dokumentide üleslaadimiseks ja küsimuste esitamiseks.

<a href="images/rag-homepage.png"><img src="../../../translated_images/et/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG rakenduse liides" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*See ekraanipilt näitab RAG rakenduse liidest, kus saad dokumente üles laadida ja küsimusi esitada.*

### Laadi dokument üles

Alusta dokumendi üleslaadimisest — testi jaoks sobivad kõige paremini TXT-failid. Selle kataloogi on lisatud `sample-document.txt`, mis sisaldab infot LangChain4j funktsioonide, RAG-i rakenduse ja parimate tavade kohta — ideaalne testimissüsteem.

Süsteem töötleb su dokumendi, jagab selle lõikudeks ja loob igale lõigule embeddingsi. See toimub automaatselt kohe, kui faili üles laed.

### Esita küsimusi

Nüüd esita dokumendi sisule spetsiifilisi küsimusi. Proovi midagi faktilist, mis on dokumendis selgelt märgitud. Süsteem otsib asjakohased lõigud, kaasab need päringusse ja genereerib vastuse.

### Kontrolli allikaviiteid

Pane tähele, et iga vastus sisaldab allikaviiteid koos sarnasuse skooridega. Need skoorid (0 kuni 1) näitavad, kui asjakohane iga lõik su küsimusega oli. Kõrgemad skoorid tähendavad paremaid vasteid. Sellega saad vastust allikmaterjaliga võrrelda.

<a href="images/rag-query-results.png"><img src="../../../translated_images/et/rag-query-results.6d69fcec5397f355.webp" alt="RAG päringu tulemused" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*See ekraanipilt näitab päringu tulemusi koos saadud vastuse, allikaviidete ja iga leitud lõigu asjakohasuse skooridega.*

### Katseta küsimustega

Proovi erinevat tüüpi küsimusi:
- Spetsiifilised faktid: "Mis on peamine teema?"
- Võrdlused: "Mis vahe on X-l ja Y-l?"
- Kokkuvõtted: "Kokkuvõtle olulisemad punktid Z kohta"

Vaata, kuidas asjakohasuse skoorid muutuvad sõltuvalt sellest, kui hästi sinu küsimus dokumendi sisuga sobib.

## Põhikontseptsioonid

### Lõikude jagamine

Dokumendid jagatakse 300 tokeni pikkusteks lõikudeks, millel on 30 tokeni kattuvus. See tasakaal tagab, et igal lõigul on piisavalt konteksti, et olla sisukas, kuid siiski piisavalt lühike, et prompti mahutada mitu lõiku.

### Sarnasuse skoorid

Iga leitud lõikuga kaasneb sarnasuse skoor vahemikus 0 kuni 1, mis näitab, kui tihedalt ta vastab kasutaja küsimusele. Järgmine diagramm visualiseerib skoorivahemikke ja kuidas süsteem neid tulemuste filtreerimiseks kasutab:

<img src="../../../translated_images/et/similarity-scores.b0716aa911abf7f0.webp" alt="Sarnasuse skoorid" width="800"/>

*See diagramm näitab skoorivahemikke 0 kuni 1, kus minimaalne lävi 0,5 filtreerib välja ebaolulised lõigud.*

Skoorid ulatuvad:
- 0.7-1.0: Väga asjakohane, täpne vaste
- 0.5-0.7: Asjakohane, hea kontekst
- Alla 0.5: Filtreeritud välja, liiga erinev

Süsteem toob välja ainult need lõigud, mis ületavad minimaalse läve, et tagada kvaliteet.

Embeddid toimivad hästi, kui tähendus moodustab selged klastrid, kuid neil on ka nõrkusi. Järgmine diagramm näitab levinumaid tõrkeid — liiga suured lõigud tekitavad häguseid vektoreid, liiga väikesed lõigud ei sisalda piisavalt konteksti, kahtlased terminid viitavad mitmele klastrile ja täpsed otsingud (ID-d, varuosade numbrid) ei tööta üldse embeddingsiga:

<img src="../../../translated_images/et/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embeddingute tõrkemõjud" width="800"/>

*See diagramm näitab levinumaid embeddingsi tõrkemõjusid: liiga suured lõigud, liiga väikesed lõigud, kahtlsed terminid mitme klastri kohta ja täpsed otsingud nagu ID-d.*

### Mälu-põhine salvestus

See moodul kasutab lihtsuse mõttes mälu-põhist salvestust. Rakenduse taaskäivitamisel kaovad üleslaaditud dokumendid. Tootmissüsteemid kasutavad püsivaid vektorbaase nagu Qdrant või Azure AI Search.

### Konteksti akna haldus

Igal mudelil on maksimaalne konteksi aken. Sa ei saa kaasata iga lõiku suurest dokumendist. Süsteem toob välja N kõige asjakohasemat lõiku (vaikimisi 5), et piirides püsida, aga pakkuda piisavalt konteksti täpsete vastuste jaoks.

## Millal RAG loeb

RAG ei ole alati parim lahendus. Järgmine otsustusdiagramm aitab sul määrata, millal RAG annab lisaväärtust ja millal on lihtsamad lahendused — nagu sisu otse prompti kaasamine või mudeli sisseehitatud teadmiste kasutamine — piisavad:

<img src="../../../translated_images/et/when-to-use-rag.1016223f6fea26bc.webp" alt="Millal kasutada RAG" width="800"/>

*See diagramm näitab otsustustabelit, millal RAG lisaväärtust pakub ja millal lihtsamad meetodid on piisavad.*

**Kasuta RAG-i, kui:**
- Vastad küsimustele omaniku dokumentide kohta
- Info muutub tihti (poliitikad, hinnad, spetsifikatsioonid)
- Täpsus nõuab lähteallika viitamist
- Sisu on liiga mahukas, et mahtuda ühte promti
- Vajad tõestatavaid ja põhjendatud vastuseid

**Ära kasuta RAG-i, kui:**
- Küsimused nõuavad üldteadmisi, mida mudel juba tunneb
- Vajalik on reaalajas andmed (RAG töötab üleslaaditud dokumentidega)
- Sisu on piisavalt väike, et see otse prompti panna

## Järgmised sammud

**Järgmine moodul:** [04- tööriistad - AI agentidega tööriistad](../04-tools/README.md)

---

**Navigatsioon:** [← Eelmine: Moodul 02 - Prompti inseneriteadus](../02-prompt-engineering/README.md) | [Tagasi avalehele](../README.md) | [Järgmine: Moodul 04 - Tööriistad →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Eeltingimus**:
See dokument on tõlgitud AI tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi me püüame tagada täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument tema algkeeles tuleks pidada ametlikuks allikaks. Olulise teabe puhul soovitatakse professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->