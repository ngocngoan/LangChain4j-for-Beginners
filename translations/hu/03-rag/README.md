# 03. Modul: RAG (Retrieval-Augmented Generation)

## Tartalomjegyzék

- [Mit fogsz megtanulni](../../../03-rag)
- [A RAG megértése](../../../03-rag)
- [Előfeltételek](../../../03-rag)
- [Hogyan működik](../../../03-rag)
  - [Dokumentum feldolgozása](../../../03-rag)
  - [Beágyazások létrehozása](../../../03-rag)
  - [Szemantikus keresés](../../../03-rag)
  - [Válasz generálás](../../../03-rag)
- [Az alkalmazás futtatása](../../../03-rag)
- [Az alkalmazás használata](../../../03-rag)
  - [Dokumentum feltöltése](../../../03-rag)
  - [Kérdések feltevése](../../../03-rag)
  - [Forrás hivatkozások ellenőrzése](../../../03-rag)
  - [Kísérletezés kérdésekkel](../../../03-rag)
- [Kulcsfogalmak](../../../03-rag)
  - [Darabokra bontási stratégia](../../../03-rag)
  - [Hasonlósági pontszámok](../../../03-rag)
  - [Memóriabeli tárolás](../../../03-rag)
  - [Kontekstus ablak kezelése](../../../03-rag)
- [Mikor fontos a RAG](../../../03-rag)
- [Következő lépések](../../../03-rag)

## Mit fogsz megtanulni

Az előző modulokban megtanultad, hogyan folytass beszélgetéseket AI-val és hogyan strukturáld hatékonyan a promptjaidat. De van egy alapvető korlát: a nyelvi modellek csak azokat az ismereteket tudják, amelyeket a tréningjük során tanultak meg. Nem tudnak válaszolni a céged szabályzataival, projekt dokumentációjával vagy bármilyen olyan információval kapcsolatos kérdésekre, amelyeket nem tanítottak nekik.

A RAG (Retrieval-Augmented Generation) megoldja ezt a problémát. Ahelyett, hogy megpróbálnád megtanítani az információdat a modellnek (ami költséges és gyakran nem megvalósítható), képessé teszed arra, hogy keresgéljen a dokumentumaid között. Amikor valaki kérdez, a rendszer megtalálja a releváns információkat, és ezeket beilleszti a promptba. A modell pedig az így lekért kontextus alapján válaszol.

Gondolj a RAG-ra úgy, mint egy hivatkozási könyvtár biztosítására a modell számára. Amikor kérdést teszel fel, a rendszer:

1. **Felhasználói kérdés** – Te felteszel egy kérdést  
2. **Beágyazás** – A kérdést vektorrá alakítja  
3. **Vektor keresés** – Megkeresi a hasonló dokumentumdarabokat  
4. **Kontekstus összeállítása** – A releváns darabokat hozzáadja a prompthoz  
5. **Válasz** – Az LLM a kontextus alapján válaszol  

Ezáltal a modell válaszai a valós adatodon alapulnak, nem csak a tréning során tanult ismeretekre hagyatkoznak vagy kitalált válaszokat adnak.

## A RAG megértése

Az alábbi ábra szemlélteti az alapvető koncepciót: ahelyett, hogy kizárólag a modell tréningadataira támaszkodna, a RAG egy hivatkozási könyvtárat ad neki a dokumentumaidból, amelyhez minden válasz generálása előtt hozzáfér.

<img src="../../../translated_images/hu/what-is-rag.1f9005d44b07f2d8.webp" alt="Mi az a RAG" width="800"/>

Így kapcsolódnak össze a lépések végponttól végpontig. A felhasználói kérdés négy szakaszon megy keresztül — beágyazás, vektor keresés, kontextus összeállítás és válasz generálás — mindegyik az előzőre épülve:

<img src="../../../translated_images/hu/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG architektúra" width="800"/>

A modul további része részletesen végigvezeti mind a négy lépést, futtatható és módosítható kódpéldákkal.

## Előfeltételek

- Az 01. Modul befejezése (Azure OpenAI erőforrások telepítve)  
- Gyökérkönyvtárban `.env` fájl az Azure hitelesítő adatokat tartalmazva (az `azd up` parancs hozza létre az 01. modulban)

> **Megjegyzés:** Ha még nem végezted el az 01. modult, először kövesd ott a telepítési útmutatót.

## Hogyan működik

### Dokumentum feldolgozása

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Amikor feltöltesz egy dokumentumot, a rendszer feldolgozza azt (PDF vagy sima szöveg formátumot), rögzíti a metaadatokat, például a fájl nevét, majd darabokra bontja — kisebb részekre, amelyek kényelmesen beleférnek a modell kontextus ablakába. Ezek a darabok enyhén átfedik egymást, hogy a határoknál ne veszítsünk el kontextust.

```java
// Elemezze a feltöltött fájlt, és csomagolja be egy LangChain4j Dokumentumba
Document document = Document.from(content, metadata);

// Ossza fel 300 tokenes darabokra, 30 tokenes átfedéssel
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Az alábbi ábra vizuálisan mutatja be a működést. Figyeld meg, hogy minden darab megoszt néhány token-t a szomszédjaival — a 30 token átfedés biztosítja, hogy ne vesszen el fontos kontextus:

<img src="../../../translated_images/hu/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentum darabolás" width="800"/>

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat segítségével:** Nyisd meg a [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) fájlt, és kérdezd:
> - „Hogyan bontja darabokra a LangChain4j a dokumentumokat, és miért fontos az átfedés?”
> - „Melyik a legoptimálisabb darabméret különböző dokumentumtípusok esetén, és miért?”
> - „Hogyan kezeljem a többnyelvű vagy speciális formázású dokumentumokat?”

### Beágyazások létrehozása

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Minden darabot számszerű reprezentációvá, úgynevezett beágyazássá alakítunk — ez lényegében egy matematikai ujjlenyomat, amely megragadja a szöveg jelentését. Hasonló szövegek hasonló beágyazásokat eredményeznek.

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
  
Az osztálydiagram mutatja, hogyan kapcsolódnak ezek a LangChain4j komponensek. Az `OpenAiOfficialEmbeddingModel` alakítja a szöveget vektorokká, az `InMemoryEmbeddingStore` tárolja a vektorokat az eredeti `TextSegment` adatokkal együtt, az `EmbeddingSearchRequest` pedig a lekérés paramétereit — mint a `maxResults` és a `minScore` — kezeli:

<img src="../../../translated_images/hu/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG osztályok" width="800"/>

Miután a beágyazásokat tároltuk, a hasonló tartalmak természetesen egymáshoz közeli pontokra csoportosulnak a vektortérben. Az alábbi vizualizáció mutatja, hogy a kapcsolódó témájú dokumentumok hogyan helyezkednek el egymás közelében, ami lehetővé teszi a szemantikus keresést:

<img src="../../../translated_images/hu/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor beágyazások tér" width="800"/>

### Szemantikus keresés

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Amikor kérdést teszel fel, az is beágyazássá alakul. A rendszer összehasonlítja a kérdésed beágyazását az összes dokumentumdarab beágyazásával. Megkeresi a leginkább hasonló jelentésű darabokat — nem csak kulcsszó egyezést, hanem valódi szemantikai hasonlóságot.

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
  
Az alábbi ábra megmutatja a szemantikus és a hagyományos kulcsszavas keresés közti különbséget. Egy kulcsszó keresés a „vehicle” szóra kihagyhat egy „autók és teherautók” darabot, de a szemantikus keresés megérti, hogy ugyanazt jelenti, és magas pontszámmal visszaadja:

<img src="../../../translated_images/hu/semantic-search.6b790f21c86b849d.webp" alt="Szemantikus keresés" width="800"/>

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat segítségével:** Nyisd meg a [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) fájlt, és kérdezd:
> - „Hogyan működik a hasonlósági keresés a beágyazásokkal és mi határozza meg a pontszámot?”
> - „Milyen hasonlósági küszöböt használjak és hogyan befolyásolja az eredményeket?”
> - „Hogyan kezeljem, ha nem találok releváns dokumentumot?”

### Válasz generálás

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

A legrelevánsabb darabokat összeszerelik egy strukturált promptba, amely tartalmaz explicit utasításokat, a lekért kontextust és a felhasználó kérdését. A modell ezeket a konkrét darabokat olvassa el, és az alapján válaszol — csak az elérhető információt használhatja, ami megakadályozza a tévesztéseket (hallucinációt).

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
  
Az alábbi ábra mutatja ezt az összeállítást - a keresési lépés legmagasabb pontszámú darabjai bekerülnek a prompt sablonba, és az `OpenAiOfficialChatModel` ebből egy megalapozott választ generál:

<img src="../../../translated_images/hu/context-assembly.7e6dd60c31f95978.webp" alt="Kontekstus összeállítás" width="800"/>

## Az alkalmazás futtatása

**Ellenőrizd a telepítést:**

Győződj meg róla, hogy a gyökérkönyvtárban megtalálható `.env` fájl az Azure hitelesítő adatokat tartalmazza (az 01. modul során készült):  
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```
  
**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már az 01. modulból a `./start-all.sh` segítségével elindítottad az összes alkalmazást, ez a modul már fut a 8081-es porton. Akkor kihagyhatod az alábbi indító parancsokat, és közvetlenül megnyithatod a http://localhost:8081 címet.

**1. lehetőség: Spring Boot Dashboard használata (kifejezetten VS Code felhasználóknak ajánlott)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális felületet biztosít az összes Spring Boot alkalmazás kezelése számára. A VS Code bal oldali tevékenységsávjában található (keresd a Spring Boot ikont).

A Spring Boot Dashboard-ból:  
- Láthatod az összes elérhető Spring Boot alkalmazást a munkaterületen  
- Egy kattintással elindíthatod vagy leállíthatod az alkalmazásokat  
- Valós időben nézheted az alkalmazások naplóit  
- Követheted az alkalmazás állapotát  

Egyszerűen kattints a „rag” mellett a lejátszás gombra a modul elindításához, vagy indítsd el egyszerre az összes modult.

<img src="../../../translated_images/hu/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**2. lehetőség: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (01-04 modulok):

**Bash:**  
```bash
cd ..  # A gyökérkönyvtárból
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # A gyökérkönyvtárból
.\start-all.ps1
```
  
Vagy indítsd csak ezt a modult:

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
  
Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és felépíti a JAR állományokat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan szeretnéd felépíteni az összes modult az indítás előtt:  
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
  
Nyisd meg böngészőben: http://localhost:8081

**Leállításhoz:**

**Bash:**  
```bash
./stop.sh  # Csak ez a modul
# Vagy
cd .. && ./stop-all.sh  # Minden modul
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Csak ez a modul
# Vagy
cd ..; .\stop-all.ps1  # Minden modul
```


## Az alkalmazás használata

Az alkalmazás webes felületet biztosít dokumentum feltöltéshez és kérdezéshez.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hu/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG alkalmazás felület" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*A RAG alkalmazás felülete – dokumentumok feltöltése és kérdések feltevése*

### Dokumentum feltöltése

Kezdd egy dokumentum feltöltésével – a TXT fájlok a legalkalmasabbak teszteléshez. Ebben a könyvtárban van egy `sample-document.txt`, ami tartalmaz információkat a LangChain4j funkcióiról, a RAG megvalósításról, és a legjobb gyakorlati példákról – tökéletes a rendszer kipróbálásához.

A rendszer feldolgozza a dokumentumot, darabokra bontja, és mindegyik darabhoz létrehozza a beágyazásokat. Ez automatikusan megtörténik a feltöltéskor.

### Kérdések feltevése

Most tegyél fel konkrét kérdéseket a dokumentum tartalmára vonatkozóan. Próbálj meg olyat, ami tényeken alapul és világosan benne van a dokumentumban. A rendszer megkeresi a releváns darabokat, beleilleszti őket a promptba, és választ generál.

### Forrás hivatkozások ellenőrzése

Figyeld meg, hogy minden válasz tartalmaz forrás hivatkozásokat hasonlósági pontszámokkal együtt. Ezek a pontszámok (0-tól 1-ig) mutatják, mennyire volt releváns az adott darab a kérdésedhez. A magasabb pontszám jobb találatot jelent. Így ellenőrizheted a választ az eredeti forrás alapján.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hu/rag-query-results.6d69fcec5397f355.webp" alt="RAG lekérdezési eredmények" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Lekérdezési eredmények válasszal, forrás hivatkozásokkal és relevancia pontszámokkal*

### Kísérletezés kérdésekkel

Próbálj ki különböző típusú kérdéseket:  
- Konkrét tények: „Mi a fő téma?”  
- Összehasonlítások: „Mi a különbség X és Y között?”  
- Összefoglalók: „Foglalja össze a legfontosabb pontokat Z-ről”

Figyeld, hogyan változnak a relevancia pontszámok attól függően, hogy milyen jól illeszkedik a kérdésed a dokumentum tartalmához.

## Kulcsfogalmak

### Darabokra bontási stratégia

A dokumentumokat 300 tokenes darabokra bontjuk, 30 token átfedéssel. Ez az egyensúly biztosítja, hogy minden darab elég kontextust tartalmazzon ahhoz, hogy értelmes legyen, miközben elég kicsi marad ahhoz, hogy több darab is beleférjen egy promptba.

### Hasonlósági pontszámok

Minden lekért darabhoz egy 0 és 1 közötti hasonlósági pontszám tartozik, amely megmutatja, milyen szorosan illeszkedik a felhasználó kérdéséhez. Az alábbi ábra szemlélteti a pontszám tartományokat és azt, hogyan használja a rendszer őket a találatok szűrésére:

<img src="../../../translated_images/hu/similarity-scores.b0716aa911abf7f0.webp" alt="Hasonlósági pontszámok" width="800"/>

Pontszámok tartománya 0-tól 1-ig:  
- 0.7-1.0: Kifejezetten releváns, pontos találat  
- 0.5-0.7: Releváns, jó kontextus  
- 0.5 alatt: Kiszűrt, túl eltérő  

A rendszer csak a minimum küszöböt meghaladó darabokat veszi figyelembe a minőség érdekében.

### Memóriabeli tárolás

Ez a modul egyszerűség miatt memóriabeli tárolást használ. Ha újraindítod az alkalmazást, a feltöltött dokumentumok elvesznek. Éles rendszerek állandó vektoralapú adatbázisokat használnak, például Qdrant vagy Azure AI Search.

### Kontekstus ablak kezelése

Minden modellnek van maximális kontextus ablaka. Nem tudod az összes darabot beilleszteni egy nagy dokumentumból. A rendszer az N legrelevánsabb darabot kéri le (alapértelmezett 5), hogy a korlátokon belül maradjon, miközben elég széles kontextust biztosít a pontos válaszokhoz.

## Mikor fontos a RAG

A RAG nem mindig a megfelelő megközelítés. Az alábbi döntési segédlet segít eldönteni, mikor ér meg a RAG használata többlet értéket, és mikor elegendő egy egyszerűbb megoldás — például közvetlen tartalom beillesztése a promptba vagy a modell beépített tudására támaszkodás:

<img src="../../../translated_images/hu/when-to-use-rag.1016223f6fea26bc.webp" alt="Mikor használd a RAG-ot" width="800"/>

**Használd a RAG-ot amikor:**
- Válaszadás kérdésekre zárt dokumentumokkal kapcsolatban
- Az információ gyakran változik (irányelvek, árak, műszaki adatok)
- A pontosság megköveteli a forrás megjelölését
- A tartalom túl nagy ahhoz, hogy egyetlen promptban elférjen
- Ellenőrizhető, megalapozott válaszokra van szükség

**Ne használd a RAG-et, ha:**
- A kérdésekhez az általános tudásra van szükség, amit a modell már ismer
- Valós idejű adatok szükségesek (a RAG feltöltött dokumentumokon alapul)
- A tartalom elég kicsi ahhoz, hogy közvetlenül a promptokban szerepeljen

## Következő lépések

**Következő modul:** [04-eszközök - AI ügynökök eszközökkel](../04-tools/README.md)

---

**Navigáció:** [← Előző: 02-es modul - Prompt tervezés](../02-prompt-engineering/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 04-es modul - Eszközök →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ezt a dokumentumot a [Co-op Translator](https://github.com/Azure/co-op-translator) mesterséges intelligencia alapú fordító szolgáltatás segítségével fordítottuk le. Bár igyekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások tartalmazhatnak hibákat vagy pontatlanságokat. Az eredeti anyanyelvű dokumentum tekintendő mérvadó forrásnak. Fontos információk esetén javasolt profi, emberi fordítást igénybe venni. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->