# Modul 03: RAG (Generování rozšířené vyhledáváním)

## Obsah

- [Video průvodce](../../../03-rag)
- [Co se naučíte](../../../03-rag)
- [Předpoklady](../../../03-rag)
- [Porozumění RAG](../../../03-rag)
  - [Který přístup RAG tento tutoriál používá?](../../../03-rag)
- [Jak to funguje](../../../03-rag)
  - [Zpracování dokumentu](../../../03-rag)
  - [Vytváření embeddingů](../../../03-rag)
  - [Sémantické vyhledávání](../../../03-rag)
  - [Generování odpovědí](../../../03-rag)
- [Spuštění aplikace](../../../03-rag)
- [Používání aplikace](../../../03-rag)
  - [Nahrání dokumentu](../../../03-rag)
  - [Pokládání otázek](../../../03-rag)
  - [Kontrola zdrojových odkazů](../../../03-rag)
  - [Experimentování s otázkami](../../../03-rag)
- [Klíčové koncepty](../../../03-rag)
  - [Strategie dělení na části](../../../03-rag)
  - [Skóre podobnosti](../../../03-rag)
  - [Ukládání v paměti](../../../03-rag)
  - [Správa okna kontextu](../../../03-rag)
- [Kdy je RAG důležitý](../../../03-rag)
- [Další kroky](../../../03-rag)

## Video průvodce

Sledujte tuto živou relaci, která vysvětluje, jak začít s tímto modulem:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG s LangChain4j - Živá relace" width="800"/></a>

## Co se naučíte

V předchozích modulech jste se naučili, jak vést konverzace s AI a efektivně strukturovat své dotazy. Ale existuje zásadní omezení: jazykové modely vědí pouze to, co se naučily během tréninku. Nemohou odpovídat na otázky týkající se politik vaší firmy, dokumentace vašeho projektu nebo jakýchkoli informací, na kterých nebyly trénovány.

RAG (Generování rozšířené vyhledáváním) tento problém řeší. Místo toho, abyste se model snažili naučit vaše informace (což je nákladné a nepraktické), dáte mu možnost vyhledávat ve vašich dokumentech. Když někdo položí otázku, systém najde relevantní informace a zahrne je do dotazu. Model pak na základě tohoto kontextu odpoví.

Představte si RAG jako poskytnutí referenční knihovny modelu. Když položíte otázku, systém:

1. **Dotaz uživatele** – Vy položíte otázku  
2. **Embedding** – Převod otázky do vektoru  
3. **Vektorové vyhledávání** – Nalezení podobných částí dokumentu  
4. **Sestavení kontextu** – Přidání relevantních částí do dotazu  
5. **Odpověď** – LLM vygeneruje odpověď na základě kontextu  

Tím se modelovy odpovědi opírají o vaše skutečná data namísto spolehání se na znalosti z tréninku nebo vymýšlení odpovědí.

## Předpoklady

- Dokončený [Modul 00 - Rychlý start](../00-quick-start/README.md) (pro příklad Easy RAG zmíněný později v tomto modulu)  
- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasazeny zdroje Azure OpenAI včetně embeddingového modelu `text-embedding-3-small`)  
- `.env` soubor v kořenovém adresáři s Azure přihlašovacími údaji (vytvořený příkazem `azd up` v Modulu 01)  

> **Poznámka:** Pokud jste neabsolvovali Modul 01, nejdříve postupujte podle tamních instrukcí nasazení. Příkaz `azd up` nasadí jak GPT chat model, tak embedding model používaný tímto modulem.

## Porozumění RAG

Níže uvedený diagram ilustruje hlavní koncept: místo spoléhání se pouze na tréninková data modelu dává RAG modelu referenční knihovnu vašich dokumentů, které si může před generováním odpovědi konzultovat.

<img src="../../../translated_images/cs/what-is-rag.1f9005d44b07f2d8.webp" alt="Co je RAG" width="800"/>

*Tento diagram ukazuje rozdíl mezi standardním LLM (který odhaduje na základě tréninkových dat) a LLM rozšířeným o RAG (který se nejprve konzultuje s vašimi dokumenty).*

Zde je zakončení celého propojení. Uživatelská otázka prochází čtyřmi fázemi — embeddingem, vektorovým vyhledáváním, sestavením kontextu a generováním odpovědi — přičemž každá staví na předchozí:

<img src="../../../translated_images/cs/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektura RAG" width="800"/>

*Tento diagram zobrazuje end-to-end pipeline RAG — uživatelský dotaz prochází embeddingem, vektorovým vyhledáváním, sestavováním kontextu a generováním odpovědi.*

Zbytek tohoto modulu detailně prochází každou fázi s kódem, který můžete spustit a upravovat.

### Který přístup RAG tento tutoriál používá?

LangChain4j nabízí tři způsoby implementace RAG, každý s jinou úrovní abstrakce. Diagram níže je porovnává vedle sebe:

<img src="../../../translated_images/cs/rag-approaches.5b97fdcc626f1447.webp" alt="Tři přístupy RAG v LangChain4j" width="800"/>

*Tento diagram porovnává tři LangChain4j RAG přístupy — Easy, Native a Advanced — ukazující jejich klíčové komponenty a kdy který použít.*

| Přístup | Co dělá | Kompromis |
|---|---|---|
| **Easy RAG** | Automaticky propojí vše přes `AiServices` a `ContentRetriever`. Anotujete rozhraní, připojíte retriever a LangChain4j za scénou řeší embedding, vyhledávání a sestavování promptu. | Minimální kód, ale nevidíte co se děje v jednotlivých krocích. |
| **Native RAG** | Voláte embedding model, vyhledáváte ve skladu, sestavujete prompt a generujete odpověď sami — krok po kroku explicitně. | Více kódu, ale každá fáze je viditelná a upravitelná. |
| **Advanced RAG** | Používá framework `RetrievalAugmentor` s připojitelnými transformátory dotazů, směrovači, přeskupovači a injektory obsahu pro produkční pipeline. | Maximální flexibilita, ale výrazně větší složitost. |

**Tento tutoriál používá Native přístup.** Každý krok pipeline RAG — embedding dotazu, vyhledávání vektorů, sestavení kontextu a generování odpovědi — je explicitně napsán v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Toto je záměrné: jako učební materiál je důležitější, aby jste každou fázi viděli a pochopili, než aby byl kód minimalizován. Jakmile budete rozumět jak jednotlivé díly zapadají do sebe, můžete přejít na Easy RAG pro rychlé prototypy nebo Advanced RAG pro produkční systémy.

> **💡 Už jste viděli Easy RAG v akci?** Modul [Rychlý start](../00-quick-start/README.md) obsahuje příklad Dokument Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), který používá Easy RAG — LangChain4j automaticky řeší embedding, vyhledávání a sestavování promptu. Tento modul jde o krok dál a rozebírá tu pipeline tak, aby jste mohli každou fázi vidět a ovládat sami.

Diagram níže ukazuje Easy RAG pipeline z tohoto příkladu Rychlého startu. Všimněte si, jak `AiServices` a `EmbeddingStoreContentRetriever` skrývají veškerou složitost — načtete dokument, připojíte retriever a získáte odpovědi. Native přístup v tomto modulu tyto skryté kroky otevírá:

<img src="../../../translated_images/cs/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG pipeline - LangChain4j" width="800"/>

*Tento diagram ukazuje Easy RAG pipeline z `SimpleReaderDemo.java`. Porovnejte to s Native přístupem použitým v tomto modulu: Easy RAG skrývá embedding, vyhledávání a sestavování promptu za `AiServices` a `ContentRetriever` — načtete dokument, připojíte retriever a dostanete odpovědi. Native přístup v tomto modulu tu pipeline otevírá, takže si sami voláte každou fázi (embed, search, assemble context, generate) a máte plnou kontrolu.*

## Jak to funguje

Pipeline RAG v tomto modulu se dělí na čtyři fáze, které běží v pořadí při každé uživatelově otázce. Nejprve je nahraný dokument **parsován a rozdělen na kousky** vhodné velikosti. Tyto kousky jsou pak převedeny na **vektorové embeddingy** a uloženy tak, aby je bylo možné matematicky porovnávat. Když přijde dotaz, systém provede **sémantické vyhledávání**, aby našel nejrelevantnější kousky, a nakonec je použije jako kontext pro LLM při **generování odpovědi**. Níže jsou jednotlivé fáze podrobně popsány s kódem a diagramy. Začneme prvním krokem.

### Zpracování dokumentu

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Když nahrajete dokument, systém ho parsuje (PDF nebo prostý text), připojí metadata jako název souboru a pak ho rozlomí na kousky — menší části, které pohodlně pasují do kontextového okna modelu. Tyto kousky se mírně překrývají, aby nedošlo ke ztrátě kontextu na hranicích.

```java
// Analyzujte nahraný soubor a zabalte jej do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdělte na úseky o 300 tokenech s překrytím 30 tokenů
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Níže uvedený diagram ukazuje, jak to vizuálně funguje. Všimněte si, že každý kousek sdílí několik tokenů se sousedy — překrytí 30 tokenů zajišťuje, že žádný důležitý kontext nepropadne mezi díly:

<img src="../../../translated_images/cs/document-chunking.a5df1dd1383431ed.webp" alt="Dělení dokumentu na části" width="800"/>

*Tento diagram ukazuje dokument rozdělený na 300-tokenové části s 30-tokenovým překrytím, zachovávající kontext na hranicích částí.*

> **🤖 Zkuste s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a zeptejte se:  
> - "Jak LangChain4j dělí dokumenty do částí a proč je překrytí důležité?"  
> - "Jaká je optimální velikost části pro různé typy dokumentů a proč?"  
> - "Jak řešit dokumenty ve vícejazyčných verzích nebo se speciálním formátováním?"  

### Vytváření embeddingů

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každý kousek se převede na číselnou reprezentaci zvanou embedding — v podstatě převod významu na čísla. Embeddingový model není „inteligentní“ jako chat model; nemůže následovat instrukce, uvažovat ani odpovídat na otázky. Co ale dělá, je namapovat text do matematického prostoru, kde podobné významy jsou blízko sebe — „auto“ poblíž „automobil“, „politika vrácení peněz“ poblíž „vrátit peníze“. Představte si chat model jako člověka, se kterým můžete mluvit; embedding model je ultra dobrý souborový systém.

Diagram níže vizualizuje tento koncept — text jde dovnitř, číselné vektory vycházejí ven a podobné významy produkují blízké vektory:

<img src="../../../translated_images/cs/embedding-model-concept.90760790c336a705.webp" alt="Koncept embeddingového modelu" width="800"/>

*Tento diagram ukazuje, jak embeddingový model převádí text na číselné vektory, přičemž podobné významy — jako „auto“ a „automobil“ — jsou blízko sebe ve vektorovém prostoru.*

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
  
Třídní diagram níže ukazuje dva samostatné toky pipeline RAG a třídy LangChain4j, které je implementují. **Ingestion tok** (běží jednou při nahrání) rozděluje dokument, embeduje kousky a ukládá je přes `.addAll()`. **Query tok** (běží při každé otázce) embeduje otázku, vyhledává ve skladu přes `.search()` a předává odpovídající kontext chat modelu. Oba toky se spojují přes sdílené rozhraní `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/cs/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Třídy RAG LangChain4j" width="800"/>

*Tento diagram ukazuje dva toky v pipeline RAG — ingestion a query — a jak se propojují přes sdílený EmbeddingStore.*

Jakmile jsou embeddingy uloženy, podobný obsah se přirozeně sdružuje ve vektorovém prostoru. Vizualizace níže ukazuje, jak dokumenty o příbuzných tématech skončí jako sousední body, což umožňuje sémantické vyhledávání:

<img src="../../../translated_images/cs/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorový prostor embeddingů" width="800"/>

*Vizualizace ukazuje, jak se příbuzné dokumenty sdružují v 3D vektorovém prostoru, s tématy jako Technická dokumentace, Obchodní pravidla a FAQ tvořící oddělené skupiny.*

Když uživatel vyhledává, systém následuje čtyři kroky: jednou embeduje dokumenty, při každém vyhledávání embeduje dotaz, porovnává vektor dotazu se všemi uloženými vektory pomocí kosinové podobnosti a vrací top-K nejlépe hodnocených částí. Diagram níže ukazuje každý krok a třídy LangChain4j zapojené do procesu:

<img src="../../../translated_images/cs/embedding-search-steps.f54c907b3c5b4332.webp" alt="Kroky vyhledávání embeddingů" width="800"/>

*Tento diagram ukazuje čtyřkrokový proces vyhledávání embeddingů: embedování dokumentů, embedování dotazu, porovnávání vektorů pomocí kosinové podobnosti a vracení top-K výsledků.*

### Sémantické vyhledávání

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Když položíte otázku, vaše otázka se také převede do embeddingu. Systém porovná embedding vaší otázky se všemi embeddingy částí dokumentu. Najde části s nejpodobnějšími významy — nejen shoda klíčových slov, ale skutečná sémantická podobnost.

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
  
Diagram níže porovnává sémantické vyhledávání s tradičním vyhledáváním podle klíčových slov. Vyhledávání podle klíčového slova „vehicle“ minulo část „cars and trucks“, ale sémantické vyhledávání chápe, že to znamená totéž, a vrátí ji jako vysoce hodnocenou shodu:

<img src="../../../translated_images/cs/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhledávání" width="800"/>

*Tento diagram porovnává vyhledávání založené na klíčových slovech se sémantickým vyhledáváním, ukazující, jak sémantické vyhledávání získává konceptuálně související obsah i když se přesná klíčová slova liší.*
Pod kapotou je podobnost měřena pomocí kosinové podobnosti — v podstatě se ptáme „ukazují tyto dvě šipky stejným směrem?“ Dva textové úseky mohou používat zcela odlišná slova, ale pokud znamenají totéž, jejich vektory ukazují stejným směrem a skórují blízko 1.0:

<img src="../../../translated_images/cs/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Tento diagram ilustruje kosinovou podobnost jako úhel mezi embeddingovými vektory — více zarovnané vektory dosahují skóre blížícího se 1.0, což znamená vyšší sémantickou podobnost.*

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a zeptejte se:
> - „Jak funguje vyhledávání podobnosti s embeddingy a co určuje skóre?“
> - „Jaký práh podobnosti bych měl použít a jak to ovlivňuje výsledky?“
> - „Jak řešit situace, kdy nejsou nalezeny žádné relevantní dokumenty?“

### Generování odpovědí

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Nejrelevantnější úseky jsou sestaveny do strukturovaného promptu, který obsahuje explicitní instrukce, získaný kontext a otázku uživatele. Model čte tyto konkrétní úseky a odpovídá na základě těchto informací — může použít pouze to, co má před sebou, což zabraňuje halucinacím.

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

Následující diagram ukazuje tuto sestavu v akci — nejlépe hodnocené úseky z vyhledávací fáze jsou vloženy do šablony promptu a `OpenAiOfficialChatModel` generuje podloženou odpověď:

<img src="../../../translated_images/cs/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Tento diagram ukazuje, jak jsou nejlépe hodnocené úseky sestaveny do strukturovaného promptu, což umožňuje modelu generovat podloženou odpověď z vašich dat.*

## Spuštění aplikace

**Ověření nasazení:**

Ujistěte se, že v kořenovém adresáři existuje soubor `.env` s přihlašovacími údaji Azure (vytvořeno během Modulu 01). Spusťte tento příkaz z adresáře modulu (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z kořenového adresáře (jak je popsáno v Modulu 01), tento modul už běží na portu 8081. Můžete přeskočit níže uvedené příkazy a rovnou přejít na http://localhost:8081.

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojové prostředí obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho na panelu aktivit vlevo ve VS Code (vyhledejte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Spouštět/zastavovat aplikace jedním kliknutím
- Zobrazovat protokoly aplikace v reálném čase
- Monitorovat stav aplikace

Stačí kliknout na tlačítko play vedle „rag“ pro spuštění tohoto modulu, nebo spustit všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tento screenshot zobrazuje Spring Boot Dashboard ve VS Code, kde můžete vizuálně spouštět, zastavovat a sledovat aplikace.*

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Z kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze základního adresáře
.\start-all.ps1
```

Nebo spusťte pouze tento modul:

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

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořenovém adresáři a postaví JAR soubory, pokud neexistují.

> **Poznámka:** Pokud chcete před spuštěním stavět všechny moduly ručně:
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

Otevřete v prohlížeči http://localhost:8081.

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

## Použití aplikace

Aplikace nabízí webové rozhraní pro nahrávání dokumentů a kladení otázek.

<a href="images/rag-homepage.png"><img src="../../../translated_images/cs/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento screenshot ukazuje rozhraní RAG aplikace, kde můžete nahrávat dokumenty a klást otázky.*

### Nahrání dokumentu

Začněte nahráním dokumentu — nejlépe fungují TXT soubory pro testování. V tomto adresáři je k dispozici `sample-document.txt`, který obsahuje informace o funkcích LangChain4j, implementaci RAG a osvědčených postupech — ideální pro testování systému.

Systém váš dokument zpracuje, rozdělí na úseky a pro každý úsek vytvoří embeddingy. To probíhá automaticky při nahrání.

### Pokládejte otázky

Teď se můžete zeptat na konkrétní otázky týkající se obsahu dokumentu. Zkuste něco faktického, co je jasně uvedeno v dokumentu. Systém vyhledá relevantní úseky, zahrne je do promptu a vygeneruje odpověď.

### Kontrola zdrojových odkazů

Všimněte si, že každá odpověď obsahuje odkazy na zdroje spolu s hodnotami podobnosti. Tyto skóre (od 0 do 1) ukazují, jak relevantní každý úsek byl pro vaši otázku. Vyšší skóre znamená lepší shodu. To vám umožňuje ověřit odpověď oproti zdrojovému materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/cs/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento screenshot ukazuje výsledky dotazu s vygenerovanou odpovědí, odkazy na zdroje a skóre relevance každého nalezeného úseku.*

### Experimentujte s otázkami

Vyzkoušejte různé druhy otázek:
- Konkrétní fakta: „Jaké je hlavní téma?“
- Porovnání: „Jaký je rozdíl mezi X a Y?“
- Shrnutí: „Shrňte klíčové body o Z“

Sledujte, jak se skóre relevance mění podle toho, jak dobře vaše otázka odpovídá obsahu dokumentu.

## Klíčové koncepty

### Strategie rozdělení do úseků

Dokumenty jsou rozděleny na úseky o 300 tokenech s překrytím 30 tokenů. Tento kompromis zajišťuje, že každý úsek má dostatek kontextu, aby byl smysluplný, ale zároveň jsou úseky dostatečně malé, aby jich bylo možné do promptu vložit více.

### Skóre podobnosti

Každý nalezený úsek má skóre podobnosti mezi 0 a 1, které ukazuje, jak úzce odpovídá uživatelově otázce. Níže uvedený diagram vizualizuje rozsahy skóre a jak systém tyto hodnoty využívá k filtrování výsledků:

<img src="../../../translated_images/cs/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Tento diagram ukazuje rozsahy skóre od 0 do 1, s minimálním prahem 0,5, který filtruje irelevantní úseky.*

Skóre se pohybují od 0 do 1:
- 0,7–1,0: Vysoce relevantní, přesná shoda
- 0,5–0,7: Relevantní, dobrý kontext
- Pod 0,5: Odfiltrováno, příliš odlišné

Systém získává pouze úseky nad minimálním prahem, aby zajistil kvalitu.

Embeddingy fungují dobře, když se význam jasno shlukuje, ale mají své slabiny. Níže uvedený diagram ukazuje běžné způsoby selhání — příliš velké úseky vytvářejí rozmazané vektory, příliš malé úseky postrádají kontext, nejednoznačné termíny směřují do více shluků a přesné vyhledávání (ID, výrobní čísla) s embeddingy vůbec nefunguje:

<img src="../../../translated_images/cs/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Tento diagram ukazuje běžné způsoby selhání embeddingů: úseky příliš velké, úseky příliš malé, nejednoznačné termíny směřující do více shluků a přesné vyhledávání jako ID.*

### Uchovávání v paměti

Tento modul používá jednoduché uchovávání v paměti. Po restartu aplikace jsou nahrané dokumenty ztraceny. Produkční systémy používají perzistentní vektorové databáze jako Qdrant nebo Azure AI Search.

### Řízení kontextového okna

Každý model má maximální kontextové okno. Nemůžete zahrnout všechny úseky z velkého dokumentu. Systém získává top N nejrelevantnějších úseků (výchozí hodnota 5), aby se vešel do limitů a zároveň poskytl dostatek kontextu pro přesné odpovědi.

## Kdy je RAG důležitý

RAG není vždy správný přístup. Následující průvodce rozhodnutím vám pomůže určit, kdy RAG přináší hodnotu a kdy jsou vhodnější jednodušší přístupy — jako je zahrnutí obsahu přímo do promptu nebo spoléhání na vestavěné znalosti modelu:

<img src="../../../translated_images/cs/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Tento diagram ukazuje rozhodovací průvodce, kdy RAG přidává hodnotu a kdy stačí jednodušší přístupy.*

## Další kroky

**Další modul:** [04-tools - AI Agent s nástroji](../04-tools/README.md)

---

**Navigace:** [← Předchozí: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 04 - Nástroje →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědni za žádné nedorozumění nebo chybné výklady vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->