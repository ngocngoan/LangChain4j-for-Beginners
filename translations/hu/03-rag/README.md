# 03. modul: RAG (Retrieval-Augmented Generation)

## Tartalomjegyzék

- [Videós bemutató](../../../03-rag)
- [Amit megtanulsz](../../../03-rag)
- [Előfeltételek](../../../03-rag)
- [A RAG megértése](../../../03-rag)
  - [Melyik RAG megközelítést használja ez az oktatóanyag?](../../../03-rag)
- [Hogyan működik](../../../03-rag)
  - [Dokumentumfeldolgozás](../../../03-rag)
  - [Beágyazások létrehozása](../../../03-rag)
  - [Szemantikus keresés](../../../03-rag)
  - [Válaszgenerálás](../../../03-rag)
- [Az alkalmazás futtatása](../../../03-rag)
- [Az alkalmazás használata](../../../03-rag)
  - [Dokumentum feltöltése](../../../03-rag)
  - [Kérdések feltevése](../../../03-rag)
  - [Hivatkozások ellenőrzése](../../../03-rag)
  - [Kísérletezés kérdésekkel](../../../03-rag)
- [Kulcsfogalmak](../../../03-rag)
  - [Darabolási stratégia](../../../03-rag)
  - [Hasonlósági pontszámok](../../../03-rag)
  - [Memóriában tárolás](../../../03-rag)
  - [Kontekstuális ablak kezelése](../../../03-rag)
- [Mikor fontos a RAG](../../../03-rag)
- [Következő lépések](../../../03-rag)

## Videós bemutató

Nézd meg ezt az élő előadást, amely elmagyarázza, hogyan kezdhetsz neki ennek a modulnak:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Amit megtanulsz

Az előző modulokban megtanultad, hogyan folytass beszélgetést a mesterséges intelligenciával és hogyan építsd fel hatékonyan a promptokat. De van egy alapvető korlát: a nyelvi modellek csak azt tudják, amit a tanításuk során megtanultak. Nem tudnak válaszolni olyan kérdésekre, melyek céged szabályzataira, projekt dokumentációjára vagy olyan információkra vonatkoznak, amik nem szerepeltek a betanításukban.

A RAG (Retrieval-Augmented Generation) megoldja ezt a problémát. Ahelyett, hogy megpróbálnád megtanítani a modellnek a saját adataidat (ami drága és nem praktikus), lehetővé teszed számára, hogy átkutassa azokat a dokumentumaidat. Amikor valaki kérdez, a rendszer megtalálja a releváns információt, és beilleszti azt a promptba. A modell úgy válaszol, hogy figyelembe veszi a lekért kontextust.

Gondolj a RAG-ra úgy, mint egy referencia könyvtárra a modell számára. Ha kérdezel valamit, a rendszer:

1. **Felhasználói kérdés** – felteszed a kérdést  
2. **Beágyazás** – a kérdés vektorrá alakul  
3. **Vektoros keresés** – hasonló dokumentumdarabokat talál  
4. **Kontextus összeszerelése** – a releváns darabokat hozzáadja a prompthoz  
5. **Válasz** – az LLM a kontextus alapján válaszol

Ezáltal a modell válaszai a te valós adataidra alapozódnak, nem csak a betanítási tudásra vagy kitalált válaszokra.

## Előfeltételek

- Befejezett [00. modul – Gyors kezdés](../00-quick-start/README.md) (a későbbiekben hivatkozott Easy RAG példa miatt)  
- Befejezett [01. modul – Bevezetés](../01-introduction/README.md) (Azure OpenAI erőforrások telepítve, beleértve a `text-embedding-3-small` beágyazási modellt)  
- `.env` fájl a gyökérkönyvtárban Azure-hitelesítő adatokkal (a Module 01-ben futtatott `azd up` által létrehozva)

> **Megjegyzés:** Ha nem fejezted be az 01. modult, előbb kövesd ott a telepítési utasításokat. Az `azd up` parancs telepíti mind a GPT csevegő modellt, mind a beágyazó modellt, amelyet ez a modul használ.

## A RAG megértése

Az alábbi ábra szemlélteti az alapelvet: a modell tanítási adatára támaszkodás helyett a RAG egy referencia könyvtárat ad neki a dokumentumaidról, amit minden válaszgenerálás előtt felkereshet.

<img src="../../../translated_images/hu/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ez az ábra megmutatja a különbséget egy hagyományos LLM (ami a tanítási adaton alapuló találgatást végez) és a RAG-alapú LLM között (ami először a dokumentumaidat nézi meg).*

Íme, hogyan kapcsolódnak össze a részek az elejétől a végéig. Egy felhasználói kérdés négy lépésen megy keresztül — beágyazás, vektoros keresés, kontextus összeszerelés és válaszgenerálás — mindegyik az előzőre épít:

<img src="../../../translated_images/hu/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ez az ábra megmutatja a teljes RAG folyamatot — a felhasználói kérdés beágyazáson, vektor keresésen, kontextus összeszerelésen, majd válaszgeneráláson megy keresztül.*

A modul többi része részletesen végigvezeti az egyes lépéseket, futtatható és módosítható kóddal.

### Melyik RAG megközelítést használja ez az oktatóanyag?

A LangChain4j háromféleképpen kínálja a RAG megvalósítását, mindegyik más absztrakciós szinten. Az alábbi ábra összehasonlítja őket egymás mellett:

<img src="../../../translated_images/hu/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ez az ábra összeveti a három LangChain4j RAG megközelítést — Egyszerű, Natív és Fejlett — megmutatva fő komponenseiket és az alkalmazásukhoz javasolt eseteket.*

| Megközelítés | Mit csinál | Hátrány |
|---|---|---|
| **Egyszerű RAG** | Minden mind automatikusan kapcsolódik az `AiServices` és a `ContentRetriever` segítségével. Egy interfészt annotálsz, retrievert csatolsz, és a LangChain4j kezeli a beágyazást, keresést és a prompt összeállítását a háttérben. | Minimális kód, de nem látod mi történik lépésről lépésre. |
| **Natív RAG** | Te hívod a beágyazó modellt, keresed az áruházban, építed a promptot és generálod a válaszokat — minden egyes lépés explicit módon. | Több kód, de minden lépés látható és módosítható. |
| **Fejlett RAG** | A `RetrievalAugmentor` keretrendszert használja, csatlakoztatható kérdés transzformerekkel, routerekkel, újrarangsorolók és tartalombeesztő elemekkel gyártási szintű munkafolyamatokhoz. | Maximális rugalmasság, de lényegesen nagyobb komplexitás. |

**Ez az oktatóanyag a Natív megközelítést használja.** A RAG folyamat minden lépése — a kérdés beágyazása, a vektoros tároló keresése, a kontextus összeszerelése és a válaszgenerálás — explicit módon ki van írva a [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) fájlban. Ez szándékos: tanulási forrásként fontosabb, hogy minden lépést lásd és megérts, mint hogy a kód minimális legyen. Ha már magabiztos vagy, hogyan illeszkednek össze az elemek, áttérhetsz az Egyszerű RAG-ra gyors prototípusokhoz vagy Fejlett RAG-ra gyártási rendszerekhez.

> **💡 Már láttad az Egyszerű RAG működését?** A [Gyors kezdés modul](../00-quick-start/README.md) tartalmaz egy Dokumentum Kérdezz-Felelek példát ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), amely az Egyszerű RAG megközelítést használja — a LangChain4j automatikusan kezeli a beágyazást, keresést és prompt összeállítást. Ez a modul egy lépéssel tovább megy, és lebontja ezt a folyamatot, így láthatod és vezérelheted az egyes lépéseket magad.

Az alábbi ábra szemlélteti az Egyszerű RAG folyamatot abból a Gyors kezdés példából. Figyeld meg, hogyan rejti el az `AiServices` és az `EmbeddingStoreContentRetriever` az egész komplexitást — betöltesz egy dokumentumot, csatolsz egy retrievert, és kapsz válaszokat. A Natív megközelítés ennek a rejtett folyamatnak a felbontása:

<img src="../../../translated_images/hu/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ez az ábra az Egyszerű RAG folyamatot mutatja a `SimpleReaderDemo.java`-ból. Hasonlítsd össze a Natív megközeléssel, amit ebben a modulban használsz: az Egyszerű RAG elrejti a beágyazást, a keresést és a prompt összeállítást az `AiServices` és `ContentRetriever` mögött — feltöltesz egy dokumentumot, csatolsz egy retrievert és kapsz válaszokat. Ebben a modulban a Natív megközelítés felbontja ezt a folyamatot, így te hívhatod meg a lépéseket (beágyazás, keresés, kontextus összeszerelés, válaszgenerálás) saját magad, teljes láthatóságot és irányítást kapva.*

## Hogyan működik

Ebben a modulban a RAG folyamat négy, egymás után futó lépésből áll, melyeket a rendszer minden kérdésnél lefuttat. Először egy feltöltött dokumentumot **feldolgoz és darabol** kisebb, kezelhető darabokra. Ezeket a darabokat aztán **vektoros beágyazásokká** alakítja és elmenti, hogy matematikailag összehasonlíthatók legyenek. Amikor megérkezik egy kérdés, a rendszer **szemantikus keresést** végez, hogy megtalálja a legrelevánsabb darabokat, majd ezeket a darabokat adja át kontextusként az LLM-nek a **válaszgeneráláshoz**. Az alábbi részeket végigvesszük kóddal és ábrákkal. Nézzük az első lépést.

### Dokumentumfeldolgozás

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Amikor feltöltesz egy dokumentumot, a rendszer feldolgozza (PDF vagy sima szöveg), hozzáad metaadatokat, mint például a fájlnév, majd darabokra bontja — kisebb részekre, amelyek kényelmesen beférnek a modell kontextusablakába. Ezek a darabok kissé átfedik egymást, hogy a határokon ne vesszen el fontos kontextus.

```java
// A feltöltött fájl elemzése és becsomagolása LangChain4j Dokumentumba
Document document = Document.from(content, metadata);

// 300 tokenes darabokra bontás 30 tokenes átfedéssel
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Az alábbi ábra vizuálisan mutatja, hogyan működik ez. Figyeld meg, hogy minden darab megoszt néhány token-t a szomszédjaival — a 30 tokenes átfedés biztosítja, hogy ne vesszen el fontos kontextus a darabhatároknál:

<img src="../../../translated_images/hu/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ez az ábra egy dokumentum 300 tokenes darabokra bontását mutatja 30 tokenes átfedéssel, megőrizve a kontextust a darabhatároknál.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Csevegést:** Nyisd meg a [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) fájlt és kérdezd meg:  
> - "Hogyan darabolja fel a LangChain4j a dokumentumokat darabokra és miért fontos az átfedés?"  
> - "Mi az optimális darabméret különböző dokumentumtípusokhoz és miért?"  
> - "Hogyan kezelem a többnyelvű vagy speciális formázású dokumentumokat?"

### Beágyazások létrehozása

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Minden darabot egy számszerűsített reprezentációvá alakítanak, amit embeddingnek nevezünk — lényegében jelentésből számokra konvertáló eszköz. A beágyazó modell nem „intelligens” úgy, mint egy csevegőmodell; nem tud utasításokat követni, érvelni vagy kérdésekre válaszolni. Amit tud, hogy a szöveget egy olyan matematikai térbe helyezi, ahol a hasonló jelentések egymáshoz közel kerülnek — például a „car” és az „automobile”, vagy a „refund policy” és a „return my money”. Gondolj egy csevegőmodellre, mint egy emberre, akivel beszélgetsz; egy embedding modell pedig egy szuperjó irattár.

Az alábbi ábra vizualizálja ezt a koncepciót — szöveg megy be, numerikus vektorok jönnek ki, és hasonló jelentések közeli vektorokat eredményeznek:

<img src="../../../translated_images/hu/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ez az ábra szemlélteti, hogyan konvertál egy embedding modell szöveget számszerű vektorokká, ahol a hasonló jelentések — például „car” és „automobile” — egymáshoz közeli helyet foglalnak el a vektortérben.*

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
  
Az osztálydiagram megmutatja a RAG folyamat két különálló áramlatát és a LangChain4j osztályokat, amelyek megvalósítják azokat. A **betöltő áramlat** (feltöltéskor egyszer lefut) darabolja a dokumentumot, beágyazza a darabokat és eltárolja őket `.addAll()` hívással. A **lekérdező áramlat** (minden egyes kérdésnél fut) beágyazza a kérdést, `.search()` segítségével keresi a tárolóban, majd a megtalált kontextust átadja a csevegőmodellnek. Mindkét áramlat az `EmbeddingStore<TextSegment>` közös interfészen keresztül kapcsolódik:

<img src="../../../translated_images/hu/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ez az ábra megmutatja a RAG két áramlatát — betöltést és lekérdezést — és azok kapcsolódását a közös EmbeddingStore-on keresztül.*

Ha a beágyazásokat eltárolták, a hasonló tartalmak természetesen együtt csoportosulnak a vektortérben. Az alábbi vizualizáció mutatja, hogyan rendeződnek a kapcsolódó témájú dokumentumok szomszédos pontokká, ami lehetővé teszi a szemantikus keresést:

<img src="../../../translated_images/hu/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ez a vizualizáció mutatja, hogyan csoportosulnak a kapcsolódó dokumentumok 3D vektortérben, olyan témák mellett, mint Műszaki dokumentáció, Üzleti szabályok és GYIK, amelyek elkülönült csoportokat alkotnak.*

Amikor egy felhasználó keres, a rendszer négy lépést követ: egyszer beágyazza a dokumentumokat, minden keresésnél beágyazza a kérdést, a kérdés vektorát összehasonlítja az összes eltárolt vektorral koszinusz hasonlóság alapján, és a legmagasabb pontszámú top-K darabot adja vissza. Az alábbi ábra végigviszi ezeket a lépéseket és a LangChain4j osztályokat:

<img src="../../../translated_images/hu/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ez az ábra a négy lépéses beágyazás-keresés folyamatot mutatja: dokumentumok beágyazása, kérdés beágyazása, vektorok összehasonlítása koszinusz hasonlósággal, és a top-K eredmények visszaadása.*

### Szemantikus keresés

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Amikor kérdést teszel fel, a kérdésed is beágyazásra kerül. A rendszer összehasonlítja a kérdés beágyazását az összes dokumentumdarab beágyazásával. Megtalálja a leginkább hasonló jelentésű darabokat — nem csak a kulcsszó egyezéseket, hanem a tényleges szemantikus hasonlóságot.

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
  
Az alábbi ábra összehasonlítja a szemantikus keresést a hagyományos kulcsszavas kereséssel. Egy kulcsszavas keresés a „vehicle” szóra nem talál meg egy „cars and trucks” darabot, míg a szemantikus keresés megérti, hogy ez ugyanazt jelenti, és magas pontszámmal adja vissza:

<img src="../../../translated_images/hu/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ez az ábra bemutatja a kulcsszavas keresést és a szemantikus keresést, megmutatva, hogyan hoz vissza a szemantikus keresés fogalmilag kapcsolódó tartalmat még akkor is, ha a pontos kulcsszavak eltérnek.*
A háttérben a hasonlóságot koszinusz hasonlóság méri — gyakorlatilag azt kérdezi, "ez a két nyíl ugyanabba az irányba mutat-e?" Két töredék teljesen eltérő szavakat használhat, de ha ugyanazt jelenti, a vektorjaik ugyanabba az irányba mutatnak és az értékük 1,0-hoz közeli:

<img src="../../../translated_images/hu/cosine-similarity.9baeaf3fc3336abb.webp" alt="Koszinusz hasonlóság" width="800"/>

*Ez az ábra a koszinusz hasonlóságot mutatja be, mint az embedding vektorok közötti szöget — a jobban összehangolt vektorok közelebb állnak az 1,0-hoz, ami magasabb szemantikai hasonlóságot jelez.*

> **🤖 Próbáld ki a [GitHub Copilot](https://github.com/features/copilot) Chattel:** Nyisd meg a [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) fájlt és kérdezd meg:
> - "Hogyan működik a hasonlóság keresés embeddingekkel és mi határozza meg az értékelést?"
> - "Milyen hasonlósági küszöböt használjak és ez hogyan befolyásolja az eredményeket?"
> - "Hogyan kezelem, ha nem talál releváns dokumentumokat?"

### Válaszgenerálás

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

A legrelevánsabb töredékeket egy strukturált promptba szerkesztik, mely tartalmazza az explicit utasításokat, a lekért kontextust és a felhasználó kérdését. A modell ezeket a konkrét töredékeket olvassa be, és ezek alapján válaszol — csak a rendelkezésére álló információkat használhatja, ami megakadályozza a téves válaszokat.

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

Az alábbi ábra ezt a szerkesztést mutatja — a keresési lépésből a legjobb pontszámú töredékeket a prompt sablonba illesztik, és az `OpenAiOfficialChatModel` egy megalapozott választ generál:

<img src="../../../translated_images/hu/context-assembly.7e6dd60c31f95978.webp" alt="Kontekstus összeállítása" width="800"/>

*Ez az ábra azt mutatja, hogyan szervezik össze a legjobb pontszámú töredékeket egy strukturált promptba, amely a modell számára alapozott választ enged meg a saját adataidból.*

## Futtasd az alkalmazást

**Ellenőrizd az üzembe helyezést:**

Győződj meg arról, hogy a `.env` fájl létezik a gyökérkönyvtárban az Azure hitelesítő adatokkal (a 01-es modulban létrehozva). Futtasd ezt a modul könyvtárából (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Meg kell jelenítenie az AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT értékeket
```

**Indítsd el az alkalmazást:**

> **Megjegyzés:** Ha már elindítottad az összes alkalmazást az `./start-all.sh`-al a gyökérkönyvtárból (ahogy a 01-es modul leírja), akkor ez a modul már fut a 8081-es porton. Az alábbi indító parancsokat átugorhatod, és közvetlenül a http://localhost:8081 címre léphetsz.

**1. Opció: Spring Boot Dashboard használata (javasolt VS Code felhasználóknak)**

A fejlesztői konténer tartalmazza a Spring Boot Dashboard kiterjesztést, amely vizuális kezelőfelületet biztosít az összes Spring Boot alkalmazáshoz. A VS Code bal oldalán az Activity Bar-ban találod (keresd a Spring Boot ikont).

A Spring Boot Dashboardból:
- Megtekintheted az összes rendelkezésre álló Spring Boot alkalmazást a munkaterületen
- Egy kattintással indíthatsz/leállíthatsz alkalmazásokat
- Valós idejű alkalmazásnaplókat nézhetsz
- Nyomon követheted az alkalmazás állapotát

Egyszerűen kattints a "rag" melletti lejátszás gombra a modul elindításához, vagy indítsd el egyszerre az összes modult.

<img src="../../../translated_images/hu/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ez a képernyőkép a Spring Boot Dashboardot mutatja a VS Code-ban, ahol vizuálisan indíthatsz, állíthatsz le és figyelhetsz alkalmazásokat.*

**2. Opció: Shell szkriptek használata**

Indítsd el az összes webalkalmazást (01-04-es modulok):

**Bash:**
```bash
cd ..  # A gyökér könyvtárból
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

Mindkét szkript automatikusan betölti a környezeti változókat a gyökér `.env` fájlból, és ha nem léteznek, lefordítja a JAR fájlokat.

> **Megjegyzés:** Ha inkább manuálisan fordítanád le az összes modult indítás előtt:
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

Nyisd meg a http://localhost:8081 címet a böngésződben.

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

Az alkalmazás webes felületet biztosít dokumentum feltöltéshez és kérdések feltevéséhez.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hu/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG alkalmazás felület" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ez a képernyőkép a RAG alkalmazás felületét mutatja, ahol dokumentumokat töltesz fel és kérdéseket teszel fel.*

### Dokumentum feltöltése

Kezdd egy dokumentum feltöltésével — teszteléshez a TXT fájlok a legjobbak. Ebben a könyvtárban található egy `sample-document.txt`, amely információkat tartalmaz a LangChain4j jellemzőiről, RAG megvalósításról és bevált gyakorlatokról — tökéletes a rendszer kipróbálásához.

A rendszer feldolgozza a dokumentumot, felbontja töredékekre, és embeddingeket készít mindegyikhez. Ez automatikusan megtörténik a feltöltéskor.

### Kérdések feltevése

Most tegyél fel konkrét kérdéseket a dokumentum tartalmáról. Próbálj meg valami tényszerűt kérdezni, ami egyértelműen szerepel a dokumentumban. A rendszer megkeresi a releváns töredékeket, beilleszti azokat a promptba, és választ generál.

### Forráshivatkozások ellenőrzése

Figyeld meg, hogy minden válasz tartalmaz forráshivatkozásokat a hasonlósági pontszámokkal. Ezek a pontszámok (0 és 1 között) azt mutatják, milyen releváns volt az adott töredék a kérdésedhez. A magasabb pontszám jobb egyezést jelent. Ez lehetővé teszi, hogy ellenőrizd a választ az eredeti forrás alapján.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hu/rag-query-results.6d69fcec5397f355.webp" alt="RAG lekérdezési eredmények" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ez a képernyőkép a lekérdezési eredményeket mutatja a generált válasszal, forráshivatkozásokkal és relevancia pontszámokkal minden lekért töredékhez.*

### Kísérletezz a kérdésekkel

Próbálj ki különféle kérdéstípusokat:
- Konkrét tények: "Mi a fő téma?"
- Összehasonlítások: "Mi a különbség X és Y között?"
- Összefoglalók: "Foglald össze a Z-vel kapcsolatos kulcspontokat"

Nézd meg, hogyan változnak a relevancia pontszámok az alapján, hogy mennyire egyezik a kérdés a dokumentumtartalommal.

## Alapfogalmak

### Töredékezési stratégia

A dokumentumokat 300 tokenes töredékekre bontják 30 token átfedéssel. Ez a balansz biztosítja, hogy minden töredéknek legyen elég kontextusa az értelmes tartalomhoz, miközben elég kicsi marad ahhoz, hogy több töredéket is bele lehessen foglalni egy promptba.

### Hasonlósági pontszámok

Minden lekért töredékhez tartozik egy 0 és 1 közötti hasonlósági pontszám, amely jelzi, mennyire egyezik a felhasználó kérdésével. Az alábbi ábra vizualizálja a pontszám tartományokat és azt, hogy a rendszer hogyan használja ezeket az eredmények szűrésére:

<img src="../../../translated_images/hu/similarity-scores.b0716aa911abf7f0.webp" alt="Hasonlósági pontszámok" width="800"/>

*Ez az ábra a pontszám tartományokat mutatja 0-tól 1-ig, egy 0,5-ös minimum küszöbbel, ami kiszűri az irreleváns töredékeket.*

A pontszámok 0 és 1 között mozognak:
- 0,7-1,0: Nagyon releváns, pontos egyezés
- 0,5-0,7: Releváns, jó kontextus
- 0,5 alatt: Kiszűrve, túl eltérő

A rendszer csak a minimum küszöböt meghaladó töredékeket veszi figyelembe a minőség biztosításához.

Az embeddingek jól működnek, ha a jelentések tisztán csoportosulnak, de vannak vakfoltjaik. Az alábbi ábra az általános hibamódokat mutatja — a túl nagy töredékek zavaros vektorokat eredményeznek, a túl kicsiknek nincs kontextusa, az ambivalens kifejezések több klaszterre mutathatnak, és a pontos egyezés keresések (azonosítók, cikkszámok) nem működnek embeddingekkel:

<img src="../../../translated_images/hu/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding hibamódok" width="800"/>

*Ez az ábra az embeddingek gyakori hibamódjait mutatja be: túl nagy, túl kicsi töredékek, több klaszterre mutató kifejezések és azonosító típusú keresések, amelyek nem működnek embeddinggel.*

### Memóriában tárolás

Ez a modul a egyszerűség kedvéért memóriában tárol. Ha újraindítod az alkalmazást, az feltöltött dokumentumok elvesznek. Éles rendszerek tartós vektor adatbázisokat használnak, mint a Qdrant vagy az Azure AI Search.

### Kontextusablak kezelése

Minden modellnek van egy maximális kontextusablaka. Nem tudsz minden töredéket belefoglalni egy nagy dokumentumból. A rendszer a top N legrelevánsabb töredéket kéri le (alapértelmezett 5), hogy a határokat tartsa és elég kontextust adjon a pontos válaszokhoz.

## Mikor számít a RAG

A RAG nem mindig a megfelelő megközelítés. Az alábbi döntési segédlet segít eldönteni, mikor érdemes RAG-ot használni, és mikor elegendő az egyszerűbb megoldás — például a tartalom közvetlen promptba illesztése vagy a modell beépített tudásának használata:

<img src="../../../translated_images/hu/when-to-use-rag.1016223f6fea26bc.webp" alt="Mikor használjuk a RAG-ot" width="800"/>

*Ez az ábra egy döntési segédletet mutat be, hogy mikor ad értéket a RAG, és mikor elegendőek az egyszerűbb megoldások.*

## Következő lépések

**Következő modul:** [04-tools - AI ügynökök eszközökkel](../04-tools/README.md)

---

**Navigáció:** [← Előző: 02-es modul - Prompt fejlesztés](../02-prompt-engineering/README.md) | [Vissza a főoldalra](../README.md) | [Következő: 04-es modul - Eszközök →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:  
Ez a dokumentum az AI fordító szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) használatával készült. Bár igyekszünk pontos fordítást biztosítani, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Kritikus információk esetén szakmai, emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből eredő félreértésekért vagy hibás értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->