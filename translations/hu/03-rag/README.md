# Modul 03: RAG (Keresés-alapú generálás)

## Tartalomjegyzék

- [Videó bemutató](../../../03-rag)
- [Mit fogsz tanulni](../../../03-rag)
- [Előfeltételek](../../../03-rag)
- [A RAG megértése](../../../03-rag)
  - [Melyik RAG megközelítést használja ez az oktatóanyag?](../../../03-rag)
- [Hogyan működik](../../../03-rag)
  - [Dokumentumfeldolgozás](../../../03-rag)
  - [Beágyazások létrehozása](../../../03-rag)
  - [Szemantikus keresés](../../../03-rag)
  - [Válaszgenerálás](../../../03-rag)
- [Futtasd az alkalmazást](../../../03-rag)
- [Az alkalmazás használata](../../../03-rag)
  - [Dokumentum feltöltése](../../../03-rag)
  - [Kérdések feltevése](../../../03-rag)
  - [Forrás hivatkozások ellenőrzése](../../../03-rag)
  - [Kísérletezés kérdésekkel](../../../03-rag)
- [Kulcsfogalmak](../../../03-rag)
  - [Darabolási stratégia](../../../03-rag)
  - [Hasonlósági pontszámok](../../../03-rag)
  - [Memóriában tárolás](../../../03-rag)
  - [Kontekstus ablak kezelése](../../../03-rag)
- [Mikor fontos a RAG](../../../03-rag)
- [Következő lépések](../../../03-rag)

## Videó bemutató

Nézd meg ezt az élő bemutatót, amely elmagyarázza, hogyan kezdj neki ennek a modulnak: [RAG LangChain4j-vel - Élő bemutató](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Mit fogsz tanulni

Az előző modulokban megtanultad, hogyan folytass párbeszédet az AI-val és hogyan strukturáld hatékonyan a promptokat. De van egy alapvető korlát: a nyelvi modellek csak azt tudják, amit a tanítás alatt megtanultak. Nem tudnak válaszolni olyan kérdésekre, amelyek a céged szabályzataira, projekt dokumentációjára vagy olyan információkra vonatkoznak, amiket nem tanítottak be nekik.

A RAG (Retriever-Augmented Generation) ezt a problémát oldja meg. Ahelyett, hogy megpróbálnád megtanítani a modellnek az információidat (ami költséges és nem praktikus), lehetőséget adsz neki, hogy átvizsgálja a dokumentumaidat. Ha valaki kérdez, a rendszer megkeresi a releváns információkat és beilleszti a promptba. A modell így a lekért kontextus alapján válaszol.

Gondolj a RAG-re úgy, mint egy hivatkozási könyvtár adására a modellnek. Amikor kérdeznek:

1. **Felhasználói kérdés** – Felteszed a kérdést
2. **Beágyazás** – A kérdést vektorrá alakítja
3. **Vektor alapú keresés** – Megkeresi a hasonló dokumentumdarabokat
4. **Kontekstus összeállítás** – A releváns darabokat hozzáadja a prompthoz
5. **Válasz** – A LLM a kontextus alapján választ generál

Ez a modell válaszait valós adatodon alapozza, ahelyett, hogy csak a tanítási tudására hagyatkozna vagy kitalált válaszokat adna.

## Előfeltételek

- Teljesített [00-as modul - Gyors kezdés](../00-quick-start/README.md) (az Easy RAG példáért, amit fent említettünk)
- Teljesített [01-es modul - Bevezetés](../01-introduction/README.md) (Azure OpenAI erőforrások telepítve, beleértve a `text-embedding-3-small` beágyazó modellt)
- `.env` fájl a gyökérkönyvtárban az Azure hitelesítő adatokkal (a `azd up` parancs készítette a 01-es modulban)

> **Megjegyzés:** Ha még nem teljesítetted az 01-es modult, először ott kövesd a telepítési utasításokat. Az `azd up` parancs telepíti mind a GPT csevegő modellt, mind a beágyazó modellt, amit ez a modul használ.

## A RAG megértése

Az alábbi ábra szemlélteti az alapötletet: ahelyett, hogy csak a modell tanítási adatain alapoznánk, a RAG egy hivatkozási könyvtárként adja a dokumentumaidat a modellnek, hogy minden válasz generálása előtt konzultáljon vele.

<img src="../../../translated_images/hu/what-is-rag.1f9005d44b07f2d8.webp" alt="Mi a RAG" width="800"/>

*Ez az ábra a hagyományos LLM-et (amely a tanítási adatokból tippel) és a RAG-alapú LLM-et (amely először átvizsgálja a dokumentumokat) mutatja be.*

Így kapcsolódnak össze a részek végponttól végpontig. A felhasználói kérdés négy fázison megy át — beágyazás, vektoros keresés, kontextus összeállítás és válaszgenerálás — mindegyik az előzőre épül:

<img src="../../../translated_images/hu/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architektúra" width="800"/>

*Ez az ábra az egész RAG folyamatot mutatja — a felhasználói kérdés beágyazást, vektoros keresést, kontextus összeállítást és válaszgenerálást hajt végre.*

A modul további része részletesen végigvezeti mindegyik lépést kóddal, amit te is futtathatsz és módosíthatsz.

### Melyik RAG megközelítést használja ez az oktatóanyag?

A LangChain4j háromféle módot kínál a RAG megvalósítására, mindegyik eltérő absztrakciós szinttel. Az alábbi ábra összehasonlítja őket egymás mellett:

<img src="../../../translated_images/hu/rag-approaches.5b97fdcc626f1447.webp" alt="Három RAG Megközelítés a LangChain4j-ben" width="800"/>

*Ez az ábra a három LangChain4j RAG megközelítést hasonlítja össze: Easy, Native és Advanced, bemutatva kulcsfontosságú elemeiket és az alkalmazási területeket.*

| Megközelítés | Mit csinál | Kiszolgáltatás |
|---|---|---|
| **Easy RAG** | Mindent automatikusan vezet be az `AiServices` és `ContentRetriever` segítségével. Annotálsz egy interfészt, hozzáadsz egy keresőt, LangChain4j kezeli a beágyazást, keresést és prompt összeállítást a háttérben. | Minimális kód, de nem látod a lépéseket. |
| **Native RAG** | Te hívod meg a beágyazó modellt, keresed az adattárban, építed a promptot és generálod a választ — minden lépést explicit módon. | Több kód, de minden fázis látható és módosítható. |
| **Advanced RAG** | A `RetrievalAugmentor` keretrendszert használja plug-in lekérdező transzformerekkel, routerekkel, újrarendezőkkel és tartalom injektorokkal, termelési környezetre. | Maximális rugalmasság, de jelentősen összetettebb. |

**Ez az oktatóanyag a Native megközelítést használja.** A RAG pipeline minden lépése — a kérdés beágyazása, a vektor adatbázis keresése, a kontextus összeállítása és a válaszgenerálás — explicit módon meg van írva a [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) fájlban. Ez szándékos: tanulási forrásként fontosabb, hogy lásd és megértsd a lépéseket, mint hogy a kódot minimalizáljuk. Ha már komfortos vagy az egésszel, átléphetsz az Easy RAG-re gyors prototípus készítéshez vagy az Advanced RAG-re gyártásra.

> **💡 Már láttad az Easy RAG-t működés közben?** A [Gyors kezdés modul](../00-quick-start/README.md) tartalmaz egy Dokumentum kérdés-válasz példát ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), amely az Easy RAG megközelítést használja — a LangChain4j automatikusan kezeli a beágyazást, keresést és prompt összeállítást. Ez a modul tovább lép azzal, hogy megnyitja a folyamatot, így te magad is látod és irányítod az egyes lépéseket.

<img src="../../../translated_images/hu/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ez az ábra a `SimpleReaderDemo.java` Easy RAG pipeline-ját mutatja. Hasonlítsd össze a Native megközelítéssel, amit ebben a modulban használsz: az Easy RAG elrejti a beágyazást, visszakeresést és prompt összeállítást az `AiServices` és `ContentRetriever` mögött — betöltesz egy dokumentumot, hozzárendelsz egy keresőt, és kapsz válaszokat. A Native megközelítés lebontja ezt a pipeline-t, így minden lépést te hívhatsz meg (beágyazás, keresés, kontextus összeállítás, generálás), teljes láthatóságot és irányítást adva.*

## Hogyan működik

A RAG pipeline ebben a modulban négy egymás utáni lépésből áll, amelyek minden egyes kérdéskor lefutnak. Először az feltöltött dokumentum **elemzésre és darabolásra kerül** kezelhető részekre. Ezeket a darabokat aztán **vektor beágyazásokká** alakítják és eltárolják matematikai összehasonlítás céljából. Amikor egy kérdés érkezik, a rendszer **szemantikus keresést** végez a legrelevánsabb darabok megtalálására, majd ezeket kontextusként átadja a LLM-nek **válaszgeneráláshoz**. Az alábbi szakaszok végigvezetnek minden lépésen tényleges kódokkal és ábrákkal. Nézzük az első lépést.

### Dokumentumfeldolgozás

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Amikor feltöltesz egy dokumentumot, a rendszer elemzi azt (PDF vagy sima szöveg), hozzárendel olyan metaadatokat, mint a fájlnév, majd darabokra bontja — kisebb részekre, amik kényelmesen beleférnek a modell kontextusablakába. Ezek a darabok kissé átfedik egymást, hogy ne vesszen el kontextus a határoknál.

```java
// Elemezze a feltöltött fájlt, és csomagolja be egy LangChain4j Dokumentumba
Document document = Document.from(content, metadata);

// Ossza 300 tokenes darabokra 30 token átfedéssel
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Az alábbi ábra szemlélteti ezt vizuálisan. Figyeld meg, hogy minden darab megoszt néhány tokent a szomszédaival — a 30 tokenes átfedés biztosítja, hogy semmilyen fontos kontextus ne vesszen el a darabhatároknál:

<img src="../../../translated_images/hu/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentum darabolás" width="800"/>

*Ez az ábra egy dokumentumot 300 tokenes darabokra oszt fel 30 token átfedéssel, megőrizve a kontextust a darabhatároknál.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat segítségével:** Nyisd meg a [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) fájlt és kérdezd meg:
> - "Hogyan darabolja le a LangChain4j a dokumentumokat és miért fontos az átfedés?"
> - "Mi az optimális darabméret különböző dokumentumtípusoknál és miért?"
> - "Hogyan kezeljem a többnyelvű vagy speciális formázású dokumentumokat?"

### Beágyazások létrehozása

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Minden darabot számszerűsített reprezentációvá, ún. embeddinggé alakítanak — lényegében jelentésből számokká alakítóvá. A beágyazó modell nem „intelligens” úgy, mint egy chat modell; nem követ utasításokat, nem érvel, nem válaszol kérdésekre. Amit viszont tud, hogy a szöveget egy matematikai térbe helyezi, ahol a hasonló jelentések közel vannak egymáshoz — például a „kocsi” közel van az „autó”-hoz, a „visszatérítési szabályzat” közel van a „pénzvisszatérítéshez”. Gondolj a chat modellre, mint egy emberre, akivel beszélgetsz; a beágyazó modell egy szuperhatékony iratrendszer.

<img src="../../../translated_images/hu/embedding-model-concept.90760790c336a705.webp" alt="Beágyazó modell koncepció" width="800"/>

*Ez az ábra megmutatja, hogyan alakít egy beágyazó modell szöveget számszerű vektorokká, ahol a hasonló jelentések – mint „kocsi” és „autó” – közel kerülnek egymáshoz a vektortérben.*

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

Az alábbi osztálydiagram a RAG pipeline két külön áramlását és a LangChain4j osztályokat mutatja be, amelyek megvalósítják őket. A **feldolgozási áramlás** (ami egyszer lefut a feltöltéskor) szétbontja a dokumentumot, beágyazza a darabokat, és elmenti `.addAll()` segítségével. A **lekérdező áramlás** (amely minden kérdéskor lefut) beágyazza a kérdést, megkeresi a tárolót `.search()`-al, és átadja a talált kontextust a chat modellnek. Mindkét áramlat találkozik a közös `EmbeddingStore<TextSegment>` interfésznél:

<img src="../../../translated_images/hu/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG osztályok" width="800"/>

*Ez az ábra mutatja a két áramlatot egy RAG pipeline-ban — feldolgozás és lekérdezés — és hogyan kapcsolódnak a közös EmbeddingStore-on keresztül.*

Miután az embeddingek el vannak tárolva, a hasonló tartalmak természetesen csoportosulnak a vektortérben. Az alábbi vizualizáció mutatja be, hogyan kerülnek a kapcsolódó témájú dokumentumok közel egybe, ami lehetővé teszi a szemantikus keresést:

<img src="../../../translated_images/hu/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor beágyazások térben" width="800"/>

*Ez a vizualizáció azt mutatja, hogyan csoportosulnak a kapcsolódó dokumentumok a 3D vektortérben, különálló csoportokat alkotva például Technikai dokumentációk, Üzleti szabályok és GYIK témákban.*

Amikor a felhasználó keres, a rendszer négy lépést követ: egyszer beágyazza a dokumentumokat, minden kereséskor beágyazza a kérdést, összehasonlítja a kérdés vektorát az összes eltárolt vektorral koszinusz hasonlósággal, és visszaadja a legmagasabb pontszámú top-K darabokat. Az alábbi ábra végigvezeti a lépéseken és a LangChain4j osztályokon:

<img src="../../../translated_images/hu/embedding-search-steps.f54c907b3c5b4332.webp" alt="Beágyazás alapú keresés lépései" width="800"/>

*Ez az ábra mutatja a négy lépéses beágyazás alapú keresési folyamatot: dokumentumok beágyazása, kérdés beágyazása, vektorok összehasonlítása koszinusz hasonlósággal és a legjobb eredmények visszaadása.*

### Szemantikus keresés

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Amikor kérdést teszel fel, az is beágyazásra kerül. A rendszer összehasonlítja a kérdés beágyazását az összes dokumentumdarab beágyazásával. Megtalálja azokat a darabokat, amelyek a leginkább hasonló jelentéssel bírnak — nem csak kulcsszavas egyezés alapján, hanem tényleges szemantikus hasonlóság alapján.

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

Az alábbi ábra összehasonlítja a szemantikus keresést a hagyományos kulcsszavas kereséssel. Egy kulcsszavas keresés a „jármű” szóra kihagyhat egy darabot, ami „autókról és teherautókról” szól, de a szemantikus keresés megérti, hogy ugyanazt jelenti, és magas pontszámmal adja vissza:

<img src="../../../translated_images/hu/semantic-search.6b790f21c86b849d.webp" alt="Szemantikus keresés" width="800"/>

*Ez az ábra bemutatja, hogyan ad vissza a szemantikus keresés fogalmilag kapcsolódó tartalmat még akkor is, ha az pontos kulcsszavak eltérnek a kulcsszavas kereséshez képest.*

A háttérben a hasonlóságot koszinusz hasonlósággal mérik — lényegében azt kérdezik, „mutatnak-e ezek az irányvektorok ugyanabba az irányba?” Két darab teljesen különböző szavakat is használhat, de ha ugyanazt jelenti, vektoruk közel azonos irányba mutat és pontszámuk közel van az 1.0-hoz:

<img src="../../../translated_images/hu/cosine-similarity.9baeaf3fc3336abb.webp" alt="Koszinusz hasonlóság" width="800"/>

*Ez az ábra szemlélteti a koszinusz hasonlóságot, amely a beágyazó vektorok közötti szöget méri — minél jobban párhuzamosak a vektorok, annál közelebb van a pontszám 1.0-hoz, ami magasabb szemantikus hasonlóságot jelent.*
> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chat-et:** Nyisd meg az [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) fájlt, és kérdezd meg:
> - "Hogyan működik a hasonlóságkeresés beágyazásokkal, és mi határozza meg a pontszámot?"
> - "Milyen hasonlósági küszöböt használjak, és hogyan befolyásolja az eredményeket?"
> - "Hogyan kezeljem azokat az eseteket, amikor nem találok releváns dokumentumokat?"

### Válaszgenerálás

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

A legrelevánsabb darabok összeállnak egy strukturált promptba, amely tartalmazza a kifejezett utasításokat, a lekért kontextust és a felhasználó kérdését. A modell ezeket a konkrét darabokat olvassa, és ezek alapján válaszol — csak azt használhatja fel, ami előtte van, ez megakadályozza a kitalálást.

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

Az alábbi ábra ezt az összeállítást mutatja be működés közben — a keresési lépésből származó legmagasabb pontszámú darabok beillesztődnek a prompt sablonba, és az `OpenAiOfficialChatModel` egy megalapozott választ generál:

<img src="../../../translated_images/hu/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ez az ábra megmutatja, hogyan szerveződnek a legjobb pontszámú darabok egy strukturált promptba, lehetővé téve a modell számára, hogy megalapozott választ generáljon az adataidból.*

## Az alkalmazás futtatása

**Használatellenőrzés:**

Győződj meg róla, hogy a `.env` fájl létezik a gyökérkönyvtárban Azure hitelesítő adatokkal (a 01-es modul során létrehozva):

**Bash:**
```bash
cat ../.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jeleníteni az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Az alkalmazás indítása:**

> **Megjegyzés:** Ha már mindent elindítottál a `./start-all.sh` parancsával a 01-es modulból, ez a modul már fut a 8081-es porton. A lent megadott indítóparancsokat átugorhatod, és közvetlenül a http://localhost:8081 oldalra léphetsz.

**1. opció: Spring Boot Dashboard használata (ajánlott VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiegészítőt, amely vizuális felületet biztosít minden Spring Boot alkalmazás kezelése érdekében. Megtalálod a VS Code bal oldali Aktivitássávjában (keress rá a Spring Boot ikonra).

A Spring Boot Dashboard segítségével:
- Megtekintheted az elérhető Spring Boot alkalmazásokat a munkaterületen
- Egy kattintással indíthatod vagy állíthatod le az alkalmazásokat
- Valós időben nézheted az alkalmazás naplóit
- Figyelheted az alkalmazás állapotát

Egyszerűen kattints a lejátszás gombra a "rag" modul mellett az indításhoz, vagy indíts el egyszerre minden modult.

<img src="../../../translated_images/hu/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ez a képernyőkép a Spring Boot Dashboardot mutatja VS Code-ban, ahol vizuálisan indíthatod, leállíthatod és figyelheted az alkalmazásokat.*

**2. opció: Shell scriptek használata**

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

Vagy csak ezt a modult:

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

Mindkét script automatikusan betölti a környezeti változókat a gyökérkönyvtári `.env` fájlból, és lefordítja a JAR-okat, ha még nem léteznek.

> **Megjegyzés:** Ha inkább manuálisan fordítanád le az összes modult indítás előtt:
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

Nyisd meg böngésződben a http://localhost:8081 oldalt.

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

Az alkalmazás webes felületet biztosít dokumentum feltöltésére és kérdezésre.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hu/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ez a képernyőkép a RAG alkalmazás felületét mutatja, ahol dokumentumokat tölthetsz fel és kérdéseket tehetsz fel.*

### Dokumentum feltöltése

Kezdj egy dokumentum feltöltésével – teszteléshez a TXT fájlok a legmegfelelőbbek. Ebben a könyvtárban található egy `sample-document.txt`, amely a LangChain4j funkcióiról, a RAG megvalósításáról és legjobb gyakorlatokról tartalmaz információkat – tökéletes a rendszer kipróbálásához.

A rendszer feldolgozza a dokumentumot, darabjaira bontja, majd minden darabhoz létrehozza a beágyazásokat. Ez automatikusan történik feltöltéskor.

### Kérdések feltevése

Ezután tegyél fel konkrét kérdéseket a dokumentum tartalmára vonatkozóan. Olyan tényalapú kérdéseket próbálj ki, amelyek egyértelműen szerepelnek a dokumentumban. A rendszer megkeresi a releváns darabokat, beilleszti őket a promptba, és válaszokat generál.

### Ellenőrizd a forrásokat

Figyeld meg, hogy minden válasz tartalmaz forrásokat hasonlósági pontszámokkal együtt. Ezek a pontszámok (0 és 1 közöttiek) megmutatják, mennyire volt releváns az adott darab a kérdésedhez. A magasabb pontszám jobb egyezést jelöl. Ezzel ellenőrizheted a választ a forrástartalom alapján.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hu/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ez a képernyőkép lekérdezési eredményeket mutat a generált válasszal, forrásokkal és az egyes lekért darabok relevancia pontszámaival.*

### Kísérletezz kérdésekkel

Próbálj ki különböző típusú kérdéseket:
- Konkrét tények: "Mi a fő témája?"
- Összehasonlítások: "Mi a különbség X és Y között?"
- Összefoglalók: "Foglald össze a fő pontokat Z-ről"

Figyeld, hogyan változnak a relevancia pontszámok attól függően, mennyire egyezik a kérdésed a dokumentum tartalmával.

## Kulcsfogalmak

### Darabolási stratégia

A dokumentumokat 300 tokenes darabokra bontjuk 30 tokenes átfedéssel. Ez az egyensúly biztosítja, hogy a darabok elegendő kontextust tartalmazzanak ahhoz, hogy értelmesek legyenek, ugyanakkor elég kicsik maradjanak ahhoz, hogy több darab beférjen a promptba.

### Hasonlósági pontszámok

Minden lekért darab egy 0 és 1 közötti hasonlósági pontszámmal jön, amely azt mutatja meg, mennyire egyezett a felhasználó kérdésével. Az alábbi ábra megjeleníti a pontszám tartományokat és azt, hogyan használja őket a rendszer a találatok szűrésére:

<img src="../../../translated_images/hu/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ez az ábra a 0 és 1 közötti pontszám tartományokat mutatja 0.5 minimális küszöbbel, amely kiszűri a nem releváns darabokat.*

A pontszámok tartománya:
- 0.7-1.0: Nagyon releváns, pontos egyezés
- 0.5-0.7: Releváns, jó kontextus
- 0.5 alatt: Kiszűrt, túl eltérő

A rendszer csak azokat a darabokat adja vissza, amelyek a minimális küszöb felett vannak, hogy biztosítsa a minőséget.

A beágyazások jól működnek, ha a jelentés tisztán csoportosítható, de nekik is vannak gyenge pontjaik. Az alábbi ábra bemutatja a gyakori hibamódokat — a túl nagy darabok homályos vektorokat eredményeznek, a túl kicsik kevés kontextust adnak, a többértelmű kifejezések több klaszterre mutatnak, és a pontos egyezésű keresések (azonosítók, cikkszámok) egyáltalán nem működnek beágyazásokkal:

<img src="../../../translated_images/hu/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ez az ábra a gyakori beágyazási hibamódokat mutatja: túl nagy darabok, túl kicsik, többértelmű kifejezések, és pontos egyezések, mint az azonosítók.*

### Memóriabeli tárolás

Ez a modul egyszerűség kedvéért memóriabeli tárolást használ. Az alkalmazás újraindításakor a feltöltött dokumentumok elvesznek. Termelési rendszerek tartós vektoralapú adatbázisokat használnak, mint például a Qdrant vagy az Azure AI Search.

### Kontextus ablak kezelése

Minden modellnek van egy maximális kontextusablaka. Nem tudsz minden darabot beilleszteni egy nagy dokumentumból. A rendszer kiválasztja az N legrelevánsabb darabot (alapértelmezett 5), hogy a korlátok között maradjon, miközben elegendő kontextust biztosít a pontos válaszokhoz.

## Mikor fontos a RAG?

A RAG nem mindig a megfelelő megközelítés. Az alábbi döntési útmutató segít eldönteni, mikor ad hozzáértéket a RAG, és mikor elegendő egyszerűbb megoldás — például a tartalom közvetlen beillesztése a promptba vagy a modell beépített tudása:

<img src="../../../translated_images/hu/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ez az ábra egy döntési útmutatót mutat, hogy mikor érdemes RAG-et használni, és mikor elég az egyszerűbb megoldás.*

**Használd a RAG-et, ha:**
- Szerzői jogvédelem alatt álló dokumentumokról kérdezel
- Az információk gyakran változnak (szabályzatok, árak, specifikációk)
- A pontosság forrásmegjelölést igényel
- A tartalom túl nagy ahhoz, hogy egy promptba beférjen
- Ellenőrizhető, megalapozott válaszokra van szükség

**Ne használd a RAG-et, ha:**
- A kérdések általános tudást igényelnek, amit a modell már tud
- Valós idejű adatra van szükség (a RAG feltöltött dokumentumokon működik)
- A tartalom elég kicsi ahhoz, hogy közvetlenül beilleszd a promptba

## Következő lépések

**Következő modul:** [04-tools - AI ügynökök eszközökkel](../04-tools/README.md)

---

**Navigáció:** [← Előző: 02-es modul - Prompt tervezés](../02-prompt-engineering/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 04-es modul - Eszközök →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:  
Ez a dokumentum az AI fordító szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum a saját nyelvén tekintendő hivatalos forrásnak. Fontos információk esetén szakmai, emberi fordítás igénylése javasolt. Nem vállalunk felelősséget az ebből a fordításból eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->