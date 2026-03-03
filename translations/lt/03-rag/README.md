# Modulis 03: RAG (Retrieval-Augmented Generation)

## Turinys

- [Vaizdo įrašo peržiūra](../../../03-rag)
- [Ko išmoksite](../../../03-rag)
- [Reikalavimai](../../../03-rag)
- [RAG supratimas](../../../03-rag)
  - [Kurią RAG metodiką naudoja šis vadovas?](../../../03-rag)
- [Kaip tai veikia](../../../03-rag)
  - [Dokumento apdorojimas](../../../03-rag)
  - [Emboldavimo kūrimas](../../../03-rag)
  - [Semantinė paieška](../../../03-rag)
  - [Atsakymo generavimas](../../../03-rag)
- [Paleisti programą](../../../03-rag)
- [Programos naudojimas](../../../03-rag)
  - [Įkelti dokumentą](../../../03-rag)
  - [Užduoti klausimus](../../../03-rag)
  - [Patikrinti šaltinių nuorodas](../../../03-rag)
  - [Eksperimentuoti su klausimais](../../../03-rag)
- [Pagrindinės sąvokos](../../../03-rag)
  - [Dalių segmentavimo strategija](../../../03-rag)
  - [Panašumo balai](../../../03-rag)
  - [Atminties saugykla](../../../03-rag)
  - [Konteksto lango valdymas](../../../03-rag)
- [Kada RAG yra svarbus](../../../03-rag)
- [Kiti žingsniai](../../../03-rag)

## Vaizdo įrašo peržiūra

Žiūrėkite šį tiesioginį seansą, kuriame paaiškinama, kaip pradėti dirbti su šiuo moduliu:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Ko išmoksite

Ankstesniuose moduliuose sužinojote, kaip bendrauti su DI ir tinkamai struktūruoti savo užklausas. Tačiau yra esminė riba: kalbos modeliai žino tik tai, ką išmoko mokymosi metu. Jie negali atsakyti į klausimus apie jūsų įmonės politiką, jūsų projekto dokumentaciją ar bet kokią informaciją, kurios nebuvo mokomi.

RAG (Retrieval-Augmented Generation) sprendžia šią problemą. Vietoj to, kad modelį mokytumėte jūsų informacijos (kas yra brangu ir nepraktiška), jūs suteikiate jam galimybę ieškoti jūsų dokumentuose. Kai kažkas užduoda klausimą, sistema randa susijusią informaciją ir įtraukia ją į užklausą. Modelis tada atsako remdamasis tuo gautu kontekstu.

Galvokite apie RAG kaip apie nuorodų biblioteką modeliui. Kai užduodate klausimą, sistema:

1. **Naudotojo užklausa** – jūs užduodate klausimą  
2. **Emboldavimas** – jūsų klausimas paverčiamas vektoriaus forma  
3. **Vektorinė paieška** – randamos panašios dokumentų dalys  
4. **Konteksto surinkimas** – į užklausą įtraukiamos atitinkamos dalys  
5. **Atsakymas** – LLM sugeneruoja atsakymą remdamasis kontekstu  

Tai pagrindžia modelio atsakymus jūsų tikrais duomenimis, o ne remiasi mokymosi žiniomis ar sugalvotais atsakymais.

## Reikalavimai

- Įveiktas [Modulis 00 - Greitas pradėjimas](../00-quick-start/README.md) (lengvam RAG pavyzdžiui šiame modulyje)
- Įveiktas [Modulis 01 - Įvadas](../01-introduction/README.md) (paleistos Azure OpenAI paskyros, įskaitant embedding modelį `text-embedding-3-small`)
- `.env` failas šakniniame kataloge su Azure kredencialais (sukurtas komandą `azd up` atlikus Modulyje 01)

> **Pastaba:** Jei dar neįveikėte Modulio 01, pirmiausia sekite ten pateiktas diegimo instrukcijas. Komanda `azd up` diegia tiek GPT pokalbių modelį, tiek embedding modelį, naudojamą šiame modulyje.

## RAG supratimas

Toliau pateiktame diagramoje iliustruojama pagrindinė idėja: vietoje tik modelio mokymosi duomenų naudojimo, RAG suteikia jam nuorodų biblioteką jūsų dokumentų, kuriuos jis pagal poreikį pasikonsultuoja prieš generuodamas kiekvieną atsakymą.

<img src="../../../translated_images/lt/what-is-rag.1f9005d44b07f2d8.webp" alt="Kas yra RAG" width="800"/>

*Ši schema parodo skirtumą tarp standartinio LLM (kuris spėja remdamasis mokymosi duomenimis) ir RAG papildyto LLM (kuris pirmiausia pasikonsultuoja su jūsų dokumentais).*

Štai kaip atskiri komponentai sujungiami bendram procesui. Naudotojo klausimas eina per keturis etapus — embedding, vektorinių paiešką, konteksto surinkimą ir atsakymo generavimą — kiekvienas etapas statomas ant ankstesnio:

<img src="../../../translated_images/lt/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG architektūra" width="800"/>

*Ši schema parodo RAG pilną procesą — naudotojo užklausa eina per embedding, vektorinę paiešką, konteksto surinkimą ir atsakymo generavimą.*

Likusi modulio dalis detaliai apžvelgia kiekvieną etapą su kodo pavyzdžiais, kuriuos galite paleisti ir keisti.

### Kurią RAG metodiką naudoja šis vadovas?

LangChain4j siūlo tris būdus įgyvendinti RAG, kiekvienas su skirtingu abstrakcijos lygiu. Toliau pateikta schema juos lygina šalia vienas kito:

<img src="../../../translated_images/lt/rag-approaches.5b97fdcc626f1447.webp" alt="Trys RAG metodikos LangChain4j" width="800"/>

*Ši schema palygina tris LangChain4j RAG metodikas — Easy, Native ir Advanced — rodydama pagrindinius komponentus ir kada jas naudoti.*

| Metodika | Ką ji daro | Kompromisas |
|---|---|---|
| **Easy RAG** | Automatiškai sujungia viską per `AiServices` ir `ContentRetriever`. Jūs aprašote sąsają, pridedate retrieverį ir LangChain4j už nugaros atlieka embedding, paiešką ir užklausos surinkimą. | Mažai kodo, bet nematote kas vyksta žingsnis po žingsnio. |
| **Native RAG** | Patys iškviečiate embedding modelį, ieškote saugykloje, kuriate užklausą ir generuojate atsakymą – žingsnis po žingsnio. | Daugiau kodo, tačiau kiekvienas etapas matomas ir valdomas. |
| **Advanced RAG** | Naudoja `RetrievalAugmentor` sistemą su pritaikomais užklausų transformatoriais, maršrutizatoriais, persvertėjais ir turinio injektoriais gamybiniams sprendimams. | Didžiausias lankstumas, bet ženkliai sudėtingiau. |

**Šis vadovas naudoja Native metodiką.** Kiekvienas RAG proceso žingsnis — užklausos embeddingas, vektorinė paieška, konteksto surinkimas ir atsakymo generavimas — detaliai parašytas faile [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Tai yra tyčia: kaip mokomoji medžiaga svarbiau, kad matytumėte ir suprastumėte kiekvieną etapą, o ne kad kodas būtų kuo trumpesnis. Kai gerai suprasite procesą, galėsite pereiti prie Easy RAG greitiems prototipams arba Advanced RAG gamybiniams projektams.

> **💡 Jau matėte Easy RAG veikimą?** Greito pradžios modulyje ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) yra pavyzdys, kuris naudoja Easy RAG metodiką – LangChain4j automatiškai atlieka embeddingą, paiešką ir užklausos surinkimą. Šis modulis imasi kito žingsnio - atveria tą procesą ir leidžia jums matyti bei valdyti kiekvieną etapą.

Toliau pateikta schema parodo Easy RAG procesą iš Greito pradžios pavyzdžio. Pastebėkite, kaip „AiServices“ ir „EmbeddingStoreContentRetriever“ slepia sudėtingumą – jūs įkeliat dokumentą, prijungiate retrieverį ir gaunate atsakymus. Native metodika šiame modulyje išplečia kiekvieną iš šių paslėptų žingsnių:

<img src="../../../translated_images/lt/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Procesas - LangChain4j" width="800"/>

*Ši schema rodo Easy RAG procesą iš `SimpleReaderDemo.java`. Lyginkite su Native metodika šiame modulyje: Easy RAG užklausos embeddingą, paiešką ir surinkimą slepia po „AiServices“ ir „ContentRetriever“ – jūs įkeliat dokumentą, pridedate retrieverį ir gaunate atsakymus. Native metodika čia atveria šį procesą, kad patys iškviesite kiekvieną etapą (embed, search, assemble context, generate), suteikdama pilną matomumą ir kontrolę.*

## Kaip tai veikia

Šio modulio RAG procesas suskaidytas į keturis etapus, kurie vykdomi seka kiekvieną kartą, kai naudotojas užduoda klausimą. Pirmiausia įkeltas dokumentas yra **išanalizuojamas ir suskaidomas į dalis**. Tos dalys paverčiamos į **vektorinius embeddingus** ir saugomos, kad būtų galima jas matematiškai palyginti. Kai atkeliauja užklausa, sistema atlieka **semantinę paiešką**, kad rastų aktualiausias dalis ir galiausiai perduoda jas kaip kontekstą LLM atsakymų generavimui. Toliau pateiktos sekcijos detaliai aptaria kiekvieną etapą su faktiniu kodu ir schemomis. Pažiūrėkime pirmą žingsnį.

### Dokumento apdorojimas

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kai įkeliat dokumentą, sistema jį analizuoja (PDF arba paprastas tekstas), prideda metaduomenis, tokius kaip failo pavadinimas, ir paskui suskaido į dalis – mažesnius gabalus, kurie patogiai tilptų į modelio konteksto langą. Šios dalys šiek tiek persidengia, kad kontekstas ties ribomis neprarastų svarbios informacijos.

```java
// Išanalizuokite įkeltą failą ir suvyniokite jį į LangChain4j dokumentą
Document document = Document.from(content, metadata);

// Padalykite į 300 žodžių skiltis su 30 žodžių persidengimu
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Toliau pateikta schema vizualizuoja šį procesą. Pastebėkite, kaip kiekviena dalis dalinasi truputį simbolių su kaimyninėmis – 30 simbolių persidengimas užtikrina, kad svarbus kontekstas neprapuola per plyšius:

<img src="../../../translated_images/lt/document-chunking.a5df1dd1383431ed.webp" alt="Dokumento dalijimas į dalis" width="800"/>

*Ši schema rodo, kaip dokumentas skaidomas į 300 simbolių dalis su 30 simbolių persidengimu, išlaikant kontekstą ribose.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atverkite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ir paklauskite:  
> - "Kaip LangChain4j dalija dokumentus į dalis ir kodėl persidengimas yra svarbus?"  
> - "Koks optimalus dalies dydis skirtingiems dokumentų tipams ir kodėl?"  
> - "Kaip tvarkyti daugiakalbius dokumentus arba özelėtu formatu?"

### Embeddingų kūrimas

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Kiekviena dalis paverčiama skaitmenine reprezentacija, vadinama embeddingu – iš esmės tai yra teksto prasmės atvaizdavimas į skaičius. Embedding modelis nėra „išmanus“ kaip pokalbių modelis; jis negali vykdyti nurodymų, mąstyti ar atsakyti į klausimus. Jo užduotis – tekstą atvaizduoti į matematinę erdvę taip, kad panašios prasmės būtų arti viena kitos – „automobilis“ arti „mašina“, „grąžinimo politika“ arti „susigrąžink pinigus“. Galvokite apie pokalbių modelį kaip žmogų, su kuriuo galite kalbėtis; embedding modelis yra itin geras dokumentų rūšiavimo sistema.

Toliau pateikta schema vizualizuoja šią koncepciją: įeina tekstas, išeina skaitmeniniai vektoriai, ir panašios prasmės generuoja vektorius, artimus vienas kitam:

<img src="../../../translated_images/lt/embedding-model-concept.90760790c336a705.webp" alt="Embedding modelio koncepcija" width="800"/>

*Ši schema rodo, kaip embedding modelis paverčia tekstą į skaitmeninius vektorius, kurios panašios reikšmės – pvz., „automobilis“ ir „mašina“ – vektorių erdvėje yra arti viena kitos.*

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

Toliau pateikta klasių schema rodo du atskirus srautus RAG procese ir LangChain4j klases, kurios juos įgyvendina. **Duomenų įkėlimo srautas** (įvyksta vieną kartą įkeliant) skaido dokumentą, atlieka embeddingą ir saugo duomenis per `.addAll()`. **Klausimų srautas** (veikia kiekvieną kartą užduodant klausimą) atlieka užklausos embeddingą, ieško per `.search()` ir perduoda atitinkamą kontekstą pokalbių modeliui. Abu srautai sujungiami per bendrą sąsają `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/lt/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klasės" width="800"/>

*Ši schema rodo du srautus RAG procese – įkėlimą ir užklausas – ir kaip jie sujungiami per bendrą EmbeddingStore.*

Įrašius embeddingus, panašus turinys natūraliai klasterizuojasi vektorinėje erdvėje. Toliau pateikta vizualizacija parodo, kaip dokumentai apie susijusias temas išsidėsto šalia vienas kito, leidžiant semantinei paieškai veikti efektyviai:

<img src="../../../translated_images/lt/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorinių embeddingų erdvė" width="800"/>

*Ši vizualizacija rodo, kaip susiję dokumentai susikoncentruoja 3D vektorių erdvėje, o temos kaip Techniniai dokumentai, Verslo taisyklės ir DUK formuoja ryškias grupes.*

Kai naudotojas atlieka paiešką, sistema vykdo keturis žingsnius: embeddinguoja dokumentus vieną kartą, embeddinguoja užklausą kiekvieną paiešką, lygina užklausos vektorių su visais saugomais vektoriais naudodama kosinuso panašumo metodą ir grąžina top-K geriausiai įvertintas dalis. Toliau pateikta schema apžvelgia kiekvieną žingsnį ir susijusias LangChain4j klases:

<img src="../../../translated_images/lt/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding paieškos žingsniai" width="800"/>

*Ši schema iliustruoja keturių žingsnių embedding paieškos procesą: dokumentų embeddingą, užklausos embeddingą, vektorių palyginimą naudojant kosinuso panašumą ir top-K rezultatų pateikimą.*

### Semantinė paieška

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kai jūs užduodate klausimą, jūsų klausimas taip pat paverčiamas embeddingu. Sistema lygina jūsų klausimo embeddingą su visų dokumentų dalių embeddingais. Ji randa dalis, kurios turi artimiausias prasmes – ne tik atitinkamus raktinius žodžius, bet autentišką semantinį panašumą.

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

Toliau pateikta schema kontrastuoja semantinę paiešką su tradicine raktinių žodžių paieška. Raktinių žodžių paieška „transportas“ praleidžia dalį apie „automobilius ir sunkvežimius“, tačiau semantinė paieška supranta, kad tai tas pats dalykas ir grąžina ją kaip aukštai vertinamą atitikmenį:

<img src="../../../translated_images/lt/semantic-search.6b790f21c86b849d.webp" alt="Semantinė paieška" width="800"/>

*Ši schema lygina raktinių žodžių pagrindu atliekamą paiešką su semantine paieška, rodydama, kaip semantinė paieška suranda konceptualiai susijusį turinį net jei tikslūs raktiniai žodžiai skiriasi.*
Po gaubtu panašumas matuojamas naudojant kosinuso panašumą — iš esmės klausiama „ar šie du rodyklės nukreiptos ta pačia kryptimi?“ Du tekstų fragmentai gali naudoti visiškai skirtingus žodžius, tačiau jei jie reiškia tą patį, jų vektoriai rodo ta pačia kryptimi ir įvertinimai artimi 1.0:

<img src="../../../translated_images/lt/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinuso Panašumas" width="800"/>

*Šiame diagramoje iliustruojamas kosinuso panašumas kaip kampas tarp įterpimo vektorių — kuo labiau suderinti vektoriai, tuo jų įvertinimai arčiau 1.0, rodantys didesnį semantinį panašumą.*

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ir paklauskite:
> - „Kaip veikia panašumo paieška naudojant įterpimus ir kas lemia įvertinimą?“
> - „Koks panašumo slenkstis turėtų būti naudojamas ir kaip jis veikia rezultatus?“
> - „Kaip elgtis atvejais, kai nerandama susijusių dokumentų?“

### Atsakymų generavimas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Reikšmingiausi tekstų fragmentai surenkami į struktūrizuotą užklausą, kuri apima aiškias instrukcijas, surinktą kontekstą ir vartotojo klausimą. Modelis skaito tuos konkrečius fragmentus ir atsako remdamasis tuo — jis gali naudoti tik tai, kas prieš jį, tai padeda išvengti klaidų (halucinacijų).

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

Žemiau pateikta diagrama rodo šį surinkimo procesą — paieškos žingsnio aukščiausiai įvertinti fragmentai įterpiami į užklausos šabloną, o `OpenAiOfficialChatModel` generuoja pagrįstą atsakymą:

<img src="../../../translated_images/lt/context-assembly.7e6dd60c31f95978.webp" alt="Konteksto Surinkimas" width="800"/>

*Ši diagrama parodo, kaip aukščiausiai įvertinti fragmentai surenkami į struktūrizuotą užklausą, leidžiančią modeliui sukurti pagrįstą atsakymą iš jūsų duomenų.*

## Paleiskite programą

**Patikrinkite diegimą:**

Įsitikinkite, kad šakniniame kataloge yra `.env` failas su Azure akreditacijomis (sukurtas 1 modulyje). Paleiskite šią komandą modulio kataloge (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pradėkite programą:**

> **Pastaba:** Jei jau pradėjote visas programas naudodami `./start-all.sh` iš šaknininio katalogo (kaip aprašyta 1 modulyje), šis modulis jau veikia 8081 prievade. Galite praleisti žemiau pateiktas paleidimo komandas ir eiti tiesiai į http://localhost:8081.

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

Dev konteineryje įdiegta Spring Boot Dashboard plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programas. Jį rasite Activity Bar kairėje VS Code pusėje (ieškokite Spring Boot piktogramos).

Iš Spring Boot Dashboard galite:
- Matyti visas Spring Boot programas darbo aplinkoje
- Vienu paspaudimu paleisti/stabdyti programas
- Peržiūrėti programos žurnalus realiu laiku
- Stebėti programų būseną

Tiesiog spustelėkite paleidimo mygtuką šalia „rag“, kad paleistumėte šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ši ekrano kopija rodo Spring Boot Dashboard VS Code, kur galite vizualiai paleisti, sustabdyti ir stebėti programas.*

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (moduliai 01-04):

**Bash:**
```bash
cd ..  # Iš šakninio katalogo
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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Abu skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite prieš paleidimą rankiniu būdu sukompiluoti visus modulius:
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
cd .. && ./stop-all.sh  # Visi moduliai
```

**PowerShell:**
```powershell
.\stop.ps1  # Tik šis modulis
# Arba
cd ..; .\stop-all.ps1  # Visi moduliai
```

## Programos naudojimas

Programa suteikia žiniatinklio sąsają dokumentų įkėlimui ir klausimams užduoti.

<a href="images/rag-homepage.png"><img src="../../../translated_images/lt/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Programos Sąsaja" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ši ekrano kopija rodo RAG programos sąsają, kur galite įkelti dokumentus ir užduoti klausimus.*

### Įkelkite dokumentą

Pradėkite nuo dokumento įkėlimo — geriausiai tinka TXT failai testavimui. Šiame kataloge pateiktas `sample-document.txt`, kuriame yra informacija apie LangChain4j funkcijas, RAG diegimą ir geriausias praktikas — puikiai tinka sistemai išbandyti.

Sistema apdoroja jūsų dokumentą, jį suskaido į fragmentus ir sukurią įterpimus kiekvienam fragmentui. Tai vyksta automatiškai įkėlus.

### Užduokite klausimus

Dabar užduokite konkrečius klausimus apie dokumentą. Pabandykite užduoti faktinius klausimus, aiškiai pateiktus dokumente. Sistema ieško tinkamų fragmentų, įtraukia juos į užklausą ir generuoja atsakymą.

### Patikrinkite šaltinių nuorodas

Atkreipkite dėmesį, kad kiekvienas atsakymas apima nuorodas į šaltinius su panašumo įvertinimais. Šie įvertinimai (nuo 0 iki 1) rodo, kiek kiekvienas fragmentas buvo susijęs su jūsų klausimu. Aukštesni įvertinimai reiškia geresnį atitikimą. Tai leidžia patikrinti atsakymą pagal šaltinį.

<a href="images/rag-query-results.png"><img src="../../../translated_images/lt/rag-query-results.6d69fcec5397f355.webp" alt="RAG Užklausos Rezultatai" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ši ekrano kopija rodo užklausos rezultatus su sugeneruotu atsakymu, šaltinių nuorodomis ir kiekvieno rasto fragmento aktualumo įvertinimais.*

### Eksperimentuokite su klausimais

Išbandykite skirtingų tipų klausimus:
- Konkretūs faktai: „Kokia pagrindinė tema?“
- Palyginimai: „Kuo skiriasi X ir Y?“
- Santraukos: „Apibendrinkite pagrindines Z temas“

Stebėkite, kaip keičiasi aktualumo įvertinimai, priklausomai nuo to, kaip gerai jūsų klausimas atitinka dokumentų turinį.

## Pagrindinės sąvokos

### Fragmentavimo strategija

Dokumentai skaidomi į 300 žodžių fragmentus su 30 žodžių persidengimu. Šis balansas užtikrina, kad kiekvienas fragmentas turėtų pakankamai konteksto, kad būtų prasmingas, bet išliktų pakankamai mažas, kad būtų galima įtraukti kelis fragmentus į užklausą.

### Panašumo įvertinimai

Kiekvienas rastas fragmentas turi panašumo įvertinimą nuo 0 iki 1, rodantį, kaip gerai jis atitinka vartotojo klausimą. Žemiau pateikta diagrama vaizduoja įvertinimų diapazonus ir kaip sistema juos naudoja filtruodama rezultatus:

<img src="../../../translated_images/lt/similarity-scores.b0716aa911abf7f0.webp" alt="Panašumo Įvertinimai" width="800"/>

*Ši diagrama rodo įvertinimų diapazonus nuo 0 iki 1, su minimaliu 0,5 slenksčiu, kuris atfiltruoja nesusijusius fragmentus.*

Įvertinimų diapazonas nuo 0 iki 1:
- 0,7–1,0: Labai reikšminga, tikslus atitikimas
- 0,5–0,7: Reikšminga, geras kontekstas
- Žemiau 0,5: Atfiltruota, pernelyg nesusiję

Sistema gauna tik fragmentus, kurių įvertinimas viršija minimalų slenkstį, siekiant užtikrinti kokybę.

Įterpimai veikia gerai, kai reikšmės aiškiai susikaupia, tačiau turi aklųjų zonų. Žemiau pateikta diagrama rodo dažniausias nesėkmių priežastis — per dideli fragmentai sukuria miglotus vektorius, per maži fragmentai neturi konteksto, dviprasmiai terminai nukreipia į kelis klasterius, o tikslių atitikimų paieška (ID, dalių numeriai) visiškai neveikia su įterpimais:

<img src="../../../translated_images/lt/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Įterpimų Klaidos Režimai" width="800"/>

*Ši diagrama rodo dažniausias įterpimų nesėkmių formas: per dideli fragmentai, per maži fragmentai, dviprasmiai terminai nukreipiantys į kelis klasterius ir tikslių atitikimų paieška kaip ID.*

### Atminties saugykla

Šis modulis naudoja atminties saugyklą paprastumui. Perkraunant programą, įkelti dokumentai prarandami. Gamybos sistemose naudojamos nuolatinės vektorinių duomenų bazės, tokios kaip Qdrant ar Azure AI Search.

### Konteksto lango valdymas

Kiekvienas modelis turi maksimalią konteksto lango apimtį. Negalite įtraukti kiekvieno fragmento iš didelio dokumento. Sistema parenka svarbiausius N fragmentų (pagal numatytuosius nustatymus 5), kad tilptų į ribas ir suteiktų pakankamą kontekstą tiksliesiems atsakymams.

## Kada svarbus RAG

RAG ne visada yra tinkamiausias metodas. Žemiau pateiktas sprendimo vadovas padeda nuspręsti, kada RAG prideda vertės, o kada pakanka paprastesnių metodų — kaip turinio tiesioginis įtraukimas į užklausą arba modelio įgimtos žinios:

<img src="../../../translated_images/lt/when-to-use-rag.1016223f6fea26bc.webp" alt="Kada naudoti RAG" width="800"/>

*Ši diagrama rodo sprendimo vadovą, kada RAG prideda vertės, o kada pakanka paprastesnių metodų.*

## Tolimesni žingsniai

**Kitas modulis:** [04-tools - AI Agentai su Įrankiais](../04-tools/README.md)

---

**Naršymas:** [← Ankstesnis: 02 modulis - Užklausų konstravimas](../02-prompt-engineering/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: 04 modulis - Įrankiai →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojama naudoti profesionalią žmogaus atliekamą vertimą. Mes neatsakingi už bet kokius nesusipratimus ar neteisingus aiškinimus, kylančius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->