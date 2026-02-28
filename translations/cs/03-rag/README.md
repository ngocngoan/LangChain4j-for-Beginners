# Modul 03: RAG (Generování podporované vyhledáváním)

## Obsah

- [Video průvodce](../../../03-rag)
- [Co se naučíte](../../../03-rag)
- [Požadavky](../../../03-rag)
- [Porozumění RAG](../../../03-rag)
  - [Který přístup RAG tento tutoriál používá?](../../../03-rag)
- [Jak to funguje](../../../03-rag)
  - [Zpracování dokumentů](../../../03-rag)
  - [Vytváření embeddingů](../../../03-rag)
  - [Sémantické vyhledávání](../../../03-rag)
  - [Generování odpovědí](../../../03-rag)
- [Spuštění aplikace](../../../03-rag)
- [Použití aplikace](../../../03-rag)
  - [Nahrání dokumentu](../../../03-rag)
  - [Pokládání dotazů](../../../03-rag)
  - [Kontrola zdrojových referencí](../../../03-rag)
  - [Experimentování s otázkami](../../../03-rag)
- [Klíčové koncepty](../../../03-rag)
  - [Strategie dělení na kusy](../../../03-rag)
  - [Míry podobnosti](../../../03-rag)
  - [Ukládání v paměti](../../../03-rag)
  - [Správa kontextového okna](../../../03-rag)
- [Kdy je RAG důležité](../../../03-rag)
- [Další kroky](../../../03-rag)

## Video průvodce

Sledujte tuto živou relaci, která vysvětluje, jak začít s tímto modulem: [RAG s LangChain4j - Živá relace](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Co se naučíte

V předchozích modulech jste se naučili, jak vést rozhovory s AI a efektivně strukturovat své prompt. Ale existuje základní omezení: jazykové modely znají jen to, co se naučily během tréninku. Nemohou odpovídat na otázky ohledně politik vaší firmy, dokumentace projektů nebo jiné informace, na které nebyly školeny.

RAG (generování podporované vyhledáváním) tento problém řeší. Místo toho, abyste model učili své informace (což je drahé a nepraktické), dáte mu možnost vyhledávat ve svých dokumentech. Když někdo položí otázku, systém najde relevantní informace a zahrne je do promptu. Model pak odpovídá založeně na tomto získaném kontextu.

Představte si RAG jako takovou referenční knihovnu pro model. Když položíte otázku, systém:

1. **Dotaz uživatele** - Položíte otázku
2. **Embedding** - Převede otázku na vektor
3. **Vektorové vyhledávání** - Najde podobné části dokumentů
4. **Sestavení kontextu** - Přidá relevantní části do promptu
5. **Odpověď** - LLM vygeneruje odpověď založenou na kontextu

To umožňuje, aby odpovědi modelu byly založeny na vašich skutečných datech, místo aby spoléhal jen na své tréninkové znalosti nebo vymýšlel odpovědi.

## Požadavky

- Dokončený [Modul 00 - Rychlý start](../00-quick-start/README.md) (pro příklad Easy RAG zmíněný výše)
- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasazeny zdroje Azure OpenAI včetně modelu embedding `text-embedding-3-small`)
- `.env` soubor v kořenovém adresáři s údaji Azure (vytvořený příkazem `azd up` v Modulu 01)

> **Poznámka:** Pokud jste modul 01 nedokončili, postupujte nejdříve podle pokynů k nasazení tam. Příkaz `azd up` nasadí jak chatovací model GPT, tak embedding model používaný v tomto modulu.

## Porozumění RAG

Následující diagram ilustruje základní koncept: místo spoléhání se pouze na tréninková data modelu, RAG mu poskytuje referenční knihovnu vašich dokumentů, ke které může přistupovat před generováním každé odpovědi.

<img src="../../../translated_images/cs/what-is-rag.1f9005d44b07f2d8.webp" alt="Co je RAG" width="800"/>

*Tento diagram ukazuje rozdíl mezi standardním LLM (který odhaduje na základě tréninkových dat) a RAG-vylepšeným LLM (který nejprve konzultuje vaše dokumenty).*

Takto jsou jednotlivé části propojené v celém procesu. Uživatelská otázka prochází čtyřmi fázemi — embedding, vektorové vyhledávání, sestavení kontextu a generování odpovědi — přičemž každý krok staví na tom předchozím:

<img src="../../../translated_images/cs/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektura RAG" width="800"/>

*Tento diagram ukazuje end-to-end RAG pipeline — uživatelský dotaz prochází embeddingem, vektorovým vyhledáváním, sestavením kontextu a generováním odpovědi.*

Zbytek modulu podrobně prochází každou fázi s kódem, který můžete spustit a upravit.

### Který přístup RAG tento tutoriál používá?

LangChain4j nabízí tři způsoby implementace RAG, každý s jinou úrovní abstrakce. Níže uvedený diagram je porovnává vedle sebe:

<img src="../../../translated_images/cs/rag-approaches.5b97fdcc626f1447.webp" alt="Tři přístupy RAG v LangChain4j" width="800"/>

*Tento diagram porovnává tři LangChain4j přístupy k RAG — Easy, Native a Advanced — ukazující jejich klíčové komponenty a kdy který používat.*

| Přístup | Co dělá | Kompromis |
|---|---|---|
| **Easy RAG** | Automaticky propojíte vše přes `AiServices` a `ContentRetriever`. Anotujete rozhraní, připojíte retriever a LangChain4j se postará o embedding, vyhledávání a sestavení promptu na pozadí. | Minimální kód, ale nevidíte, co se děje v každém kroku. |
| **Native RAG** | Voláte embedding model, prohledáváte store, stavíte prompt a generujete odpověď sami — po jednotlivých explicitních krocích. | Více kódu, ale každá fáze je viditelná a upravitelná. |
| **Advanced RAG** | Používá framework `RetrievalAugmentor` s možnostmi připojení transformátorů dotazů, routerů, re-rankerů a injektorů obsahu pro produkční pipeline. | Maximální flexibilita, ale silně složitější. |

**Tento tutoriál používá Native přístup.** Každý krok RAG pipeline — embedding dotazu, vyhledání vektorů, sestavení kontextu a generování odpovědi — je explicitně napsán v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Je to záměrné: jako učební materiál je důležitější, abyste viděli a rozuměli každé fázi, než aby byl kód co nejkratší. Jakmile budete mít práci s jednotlivými částmi zažitou, můžete přejít na Easy RAG pro rychlé prototypy, nebo Advanced RAG pro produkční systémy.

> **💡 Už jste viděli Easy RAG v akci?** Modul [Rychlý start](../00-quick-start/README.md) obsahuje příklad otázky a odpovědi s dokumentem ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), který používá Easy RAG — LangChain4j automaticky zajišťuje embedding, vyhledávání a sestavení promptu. Tento modul jde dál a otevírá pipeline tak, abyste mohli vidět a ovládat každou fázi sami.

<img src="../../../translated_images/cs/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Tento diagram ukazuje Easy RAG pipeline z `SimpleReaderDemo.java`. Porovnejte to s Native přístupem v tomto modulu: Easy RAG skrývá embedding, retrieval a sestavení promptu za `AiServices` a `ContentRetriever`, načtete dokument, připojíte retriever a máte odpovědi. Native přístup v tomto modulu tuto pipeline otevírá, takže sami voláte každou fázi (embed, search, assemble context, generate) a máte plnou viditelnost a kontrolu.*

## Jak to funguje

RAG pipeline v tomto modulu je rozložena do čtyř fází, které běží v sekvenci vždy, když uživatel položí otázku. Nejprve je nahraný dokument **parsován a rozdělen do kousků** vhodné velikosti. Tyto kousky se potom převedou na **vektorové embeddingy** a uloží, aby je bylo možné matematicky porovnávat. Když přijde dotaz, systém provede **sémantické vyhledávání** pro nalezení nejrelevantnějších kusů, a nakonec je předá jako kontext LLM k **generování odpovědi**. Níže projdeme každou fázi s reálným kódem a diagramy. Začneme prvním krokem.

### Zpracování dokumentů

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Když nahrajete dokument, systém jej parsuje (PDF nebo prostý text), připojí metadata jako název souboru a pak jej rozdělí do kousků — menších částí, které pohodlně zapadnou do kontextového okna modelu. Tyto kousky se mírně překrývají, aby se neztrácel kontext na hranicích.

```java
// Analyzujte nahraný soubor a zabalte jej do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdělte do bloků po 300 tokenech s 30-tokenovým překryvem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagram níže to vizuálně ukazuje. Všimněte si, jak každý kousek sdílí část tokenů se sousedními — překrytí o 30 tokenů zajišťuje, že neztratíte důležitý kontext mezi kusy:

<img src="../../../translated_images/cs/document-chunking.a5df1dd1383431ed.webp" alt="Dělení dokumentu na kousky" width="800"/>

*Tento diagram ukazuje dělení dokumentu do 300-tokenových částí s 30-tokenovým překrytím, zachovávající kontext na hranicích.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a zeptejte se:
> - „Jak LangChain4j rozděluje dokumenty na kousky a proč je překrytí důležité?“
> - „Jaká je optimální velikost kousku pro různé typy dokumentů a proč?“
> - „Jak zpracovávat dokumenty ve více jazycích nebo se speciálním formátováním?“

### Vytváření embeddingů

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každý kousek se převede na číselnou reprezentaci nazývanou embedding — v podstatě převod významu na čísla. Embedding model není „inteligentní“ jako chatovací model; nedokáže sledovat instrukce, uvažovat nebo odpovídat na otázky. Co ale umí, je namapovat text do matematického prostoru, kde blízké významy končí poblíž sebe — „auto“ poblíž „automobil“, „refundace“ poblíž „vrácení peněz“. Chatovací model si představte jako člověka, se kterým můžete mluvit; embedding model je ultra-dobrý systém pro uložení a hledání.

<img src="../../../translated_images/cs/embedding-model-concept.90760790c336a705.webp" alt="Koncept embedding modelu" width="800"/>

*Tento diagram ukazuje, jak embedding model převádí text na číselné vektory, umisťující podobné významy — jako „auto“ a „automobil“ — blízko sebe v prostoru vektorů.*

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

Třídový diagram níže ukazuje dva samostatné toky v RAG pipeline a třídy LangChain4j, které je implementují. **Ingestní tok** (běží jednou při nahrání) rozděluje dokument, embeduje kousky a ukládá je přes `.addAll()`. **Dotazový tok** (běží vždy, když uživatel něco položí) embeduje otázku, vyhledává v úložišti přes `.search()` a předává nalezený kontext chatovacímu modelu. Oba toky se potkávají na sdíleném rozhraní `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/cs/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j třídy pro RAG" width="800"/>

*Tento diagram ukazuje oba toky v RAG pipeline — ingestní a dotazový — a jak se propojují přes sdílené `EmbeddingStore`.*

Jakmile jsou embeddingy uloženy, podobný obsah se přirozeně shlukuje v prostoru vektorů. Vizualizace níže ukazuje, jak se dokumenty o příbuzných tématech seskupují poblíž sebe, což umožňuje sémantické vyhledávání:

<img src="../../../translated_images/cs/vector-embeddings.2ef7bdddac79a327.webp" alt="Prostor vektorových embeddingů" width="800"/>

*Tato vizualizace ukazuje, jak se tematicky příbuzné dokumenty shlukují v trojrozměrném vektorovém prostoru, s tématy jako Technická dokumentace, Podniková pravidla a FAQ tvořící samostatné skupiny.*

Když uživatel vyhledává, systém následuje čtyři kroky: embeduje dokumenty jednou, embeduje dotaz při každém vyhledávání, porovná vektor dotazu se všemi uloženými vektory pomocí kosinové podobnosti a vrátí nejlepších K nejrelevantnějších kusů. Diagram níže ukazuje každý krok a třídy LangChain4j, které jsou zapojeny:

<img src="../../../translated_images/cs/embedding-search-steps.f54c907b3c5b4332.webp" alt="Kroky embedding vyhledávání" width="800"/>

*Tento diagram ukazuje čtyřkrokový proces vyhledávání embeddingu: embedování dokumentů, embedování dotazu, porovnání vektorů kosinovou podobností a vrácení top-K výsledků.*

### Sémantické vyhledávání

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Když položíte otázku, vaše otázka se také převede na embedding. Systém porovná embedding vaší otázky se všemi embeddingy částí dokumentu. Najde části s nejpodobnějším významem — nejen podle klíčových slov, ale skutečnou sémantickou podobnost.

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

Diagram níže porovnává sémantické vyhledávání s tradičním vyhledáváním podle klíčových slov. Vyhledávání podle klíčového slova „vozidlo“ minulo část o „auty a nákladními vozy“, ale sémantické vyhledávání chápe, že znamenají totéž, a vrací to jako vysoce relevantní shodu:

<img src="../../../translated_images/cs/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhledávání" width="800"/>

*Tento diagram porovnává vyhledávání založené na klíčových slovech se sémantickým vyhledáváním, ukazující, jak sémantické vyhledávání vybírá konceptuálně příbuzný obsah i když se přesná klíčová slova liší.*

Pod kapotou je podobnost měřena pomocí kosinové podobnosti — v podstatě se ptáme „směřují tyto dva šipky stejným směrem?“ Dva úseky mohou používat úplně jiná slova, ale pokud znamenají totéž, jejich vektory směřují stejným směrem a skóre je blízké 1,0:

<img src="../../../translated_images/cs/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinová podobnost" width="800"/>

*Tento diagram ilustruje kosinovou podobnost jako úhel mezi embeddingovými vektory — vektory více sladěné mají skóre blížící se 1,0, což znamená vyšší sémantickou podobnost.*
> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a zeptejte se:
> - "Jak funguje vyhledávání podobnosti s embeddingy a co určuje skóre?"
> - "Jaký práh podobnosti bych měl použít a jak to ovlivňuje výsledky?"
> - "Jak řešit případy, kdy nejsou nalezeny žádné relevantní dokumenty?"

### Generování odpovědí

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Nejrelevantnější části dokumentu jsou sestaveny do strukturovaného promptu, který obsahuje explicitní instrukce, získaný kontext a otázku uživatele. Model čte právě tyto konkrétní části a odpovídá na základě těchto informací — může použít pouze to, co má před sebou, což zabraňuje halucinacím.

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

Diagram níže ukazuje tuto sestavu v akci — nejlépe hodnocené části z kroků vyhledávání jsou vloženy do šablony promptu a `OpenAiOfficialChatModel` generuje odpověď založenou na datech:

<img src="../../../translated_images/cs/context-assembly.7e6dd60c31f95978.webp" alt="Sestavení kontextu" width="800"/>

*Tento diagram ukazuje, jak jsou nejlépe hodnocené části sestaveny do strukturovaného promptu, což umožňuje modelu generovat podloženou odpověď z vašich dat.*

## Spuštění aplikace

**Ověření nasazení:**

Ujistěte se, že v kořenovém adresáři existuje soubor `.env` s přihlašovacími údaji do Azure (vytvořené během Modulu 01):

**Bash:**
```bash
cat ../.env  # Měl by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8081. Můžete přeskočit níže uvedené příkazy pro spuštění a přejít přímo na http://localhost:8081.

**Možnost 1: Použití Spring Boot Dashboard (Doporučeno pro uživatele VS Code)**

Vývojový kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v Activity Baru vlevo ve VS Code (hledáte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Spustit/zastavit aplikace jedním kliknutím
- Sledovat logy aplikací v reálném čase
- Monitorovat stav aplikací

Jednoduše klikněte na tlačítko přehrávání vedle "rag" pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tento snímek obrazovky ukazuje Spring Boot Dashboard ve VS Code, kde můžete vizuálně spouštět, zastavovat a sledovat aplikace.*

**Možnost 2: Použití shell skriptů**

Spustit všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z kořenového adresáře
.\start-all.ps1
```

Nebo spustit jen tento modul:

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

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořenovém adresáři a pokud JAR soubory neexistují, zkompilují je.

> **Poznámka:** Pokud preferujete ruční sestavení všech modulů před spuštěním:
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

Otevřete http://localhost:8081 ve vašem prohlížeči.

**Pro zastavení:**

**Bash:**
```bash
./stop.sh  # Pouze tento modul
# Nebo
cd .. && ./stop-all.sh  # Všechny moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Pouze tento modul
# Nebo
cd ..; .\stop-all.ps1  # Všechny moduly
```

## Používání aplikace

Aplikace nabízí webové rozhraní pro nahrávání dokumentů a pokládání otázek.

<a href="images/rag-homepage.png"><img src="../../../translated_images/cs/rag-homepage.d90eb5ce1b3caa94.webp" alt="Rozhraní aplikace RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento snímek obrazovky ukazuje rozhraní aplikace RAG, kde nahráváte dokumenty a kladete otázky.*

### Nahrání dokumentu

Začněte nahráním dokumentu - pro testování nejlépe fungují soubory TXT. V tomto adresáři je k dispozici `sample-document.txt`, který obsahuje informace o funkcích LangChain4j, implementaci RAG a nejlepších postupech – ideální pro testování systému.

Systém zpracuje váš dokument, rozdělí ho na části a vytvoří embeddingy pro každý segment. To probíhá automaticky při nahrání.

### Pokládání otázek

Nyní položte konkrétní otázky ohledně obsahu dokumentu. Zkuste faktografické otázky, které jsou jasně uvedené v dokumentu. Systém vyhledá relevantní části, zahrne je do promptu a vygeneruje odpověď.

### Kontrola zdrojových odkazů

Všimněte si, že každá odpověď obsahuje zdrojové reference s hodnocením podobnosti. Tato skóre (0 až 1) ukazují, jak relevantní byla každá část pro vaši otázku. Vyšší skóre znamená lepší shodu. To vám umožňuje ověřit odpověď ve zdrojovém materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/cs/rag-query-results.6d69fcec5397f355.webp" alt="Výsledky dotazu RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento snímek obrazovky ukazuje výsledky dotazu s generovanou odpovědí, zdrojovými odkazy a skóre relevance pro každou nalezenou část.*

### Experimentujte s otázkami

Vyzkoušejte různé typy otázek:
- Konkrétní fakta: "Jaké je hlavní téma?"
- Porovnání: "Jaký je rozdíl mezi X a Y?"
- Shrnutí: "Shrňte klíčové body týkající se Z"

Sledujte, jak se mění skóre relevance podle toho, jak dobře vaše otázka odpovídá obsahu dokumentu.

## Klíčové pojmy

### Strategie dělení na části

Dokumenty jsou rozděleny na části po 300 tokenech s překrytím 30 tokenů. Toto vyvážení zajišťuje, že každá část má dostatek kontextu, aby byla smysluplná, a zároveň dost malou velikost, aby bylo možné zahrnout více částí do promptu.

### Skóre podobnosti

Každá získaná část má skóre podobnosti mezi 0 a 1, které ukazuje, jak úzce odpovídá otázce uživatele. Následující diagram vizualizuje rozsahy skóre a jak systém tato skóre používá k filtrování výsledků:

<img src="../../../translated_images/cs/similarity-scores.b0716aa911abf7f0.webp" alt="Skóre podobnosti" width="800"/>

*Tento diagram ukazuje rozsahy skóre od 0 do 1 s minimálním prahem 0,5, který filtruje irelevantní části.*

Skóre se pohybují od 0 do 1:
- 0.7-1.0: Vysoce relevantní, přesná shoda
- 0.5-0.7: Relevantní, dobrý kontext
- Pod 0.5: Filtrováno, příliš odlišné

Systém vyhledává pouze části nad minimálním prahem, aby zaručil kvalitu.

Embeddingy dobře fungují, když se význam jasně skupinuje, ale mají slepá místa. Diagram níže ukazuje běžné způsoby selhání — příliš velké části produkují neostrý vektor, příliš malé části postrádají kontext, nejednoznačné termíny ukazují na více skupin a přesná hledání (ID, čísla dílů) vůbec nefungují s embeddingy:

<img src="../../../translated_images/cs/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Způsoby selhání embeddingu" width="800"/>

*Tento diagram ukazuje běžné způsoby selhání embeddingů: příliš velké části, příliš malé části, nejednoznačné termíny ukazující na více skupin a přesná hledání jako ID.*

### In-memory úložiště

Tento modul pro jednodušší použití používá úložiště v paměti. Po restartu aplikace jsou nahrané dokumenty ztraceny. Produkční systémy používají persistentní vektorové databáze jako Qdrant nebo Azure AI Search.

### Správa kontextového okna

Každý model má maximální velikost kontextového okna. Nemůžete zahrnout všechny části z velkého dokumentu. Systém získává top N nejrelevantnějších částí (defaultně 5), aby zůstal v limitu a zároveň poskytl dostatek kontextu pro přesné odpovědi.

## Kdy je RAG důležitý

RAG není vždy ta správná volba. Níže uvedený průvodce rozhodováním vám pomůže určit, kdy RAG přidává hodnotu a kdy postačují jednodušší přístupy – jako zahrnutí obsahu přímo do promptu nebo spoléhání se na vestavěné znalosti modelu:

<img src="../../../translated_images/cs/when-to-use-rag.1016223f6fea26bc.webp" alt="Kdy použít RAG" width="800"/>

*Tento diagram ukazuje rozhodovací průvodce, kdy RAG přidává hodnotu oproti jednoduchým přístupům.*

**Použijte RAG, když:**
- Odpovídáte na otázky týkající se proprietárních dokumentů
- Informace se často mění (politiky, ceny, specifikace)
- Přesnost vyžaduje uvedení zdroje
- Obsah je příliš velký na to, aby se vešel do jednoho promptu
- Potřebujete ověřitelné, podložené odpovědi

**Nepoužívejte RAG, když:**
- Otázky vyžadují obecné znalosti, které model již má
- Potřebujete data v reálném čase (RAG funguje na nahraných dokumentech)
- Obsah je dostatečně malý, aby šel přímo do promptů

## Další kroky

**Další modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigace:** [← Předchozí: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neručíme za jakékoli nedorozumění nebo nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->