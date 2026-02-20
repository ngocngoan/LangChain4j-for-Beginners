# Modul 03: RAG (Retrieval-Augmented Generation)

## Obsah

- [Co se naučíte](../../../03-rag)
- [Pochopení RAG](../../../03-rag)
- [Požadavky](../../../03-rag)
- [Jak to funguje](../../../03-rag)
  - [Zpracování dokumentů](../../../03-rag)
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
  - [Správa kontextového okna](../../../03-rag)
- [Kdy je RAG důležitý](../../../03-rag)
- [Další kroky](../../../03-rag)

## Co se naučíte

V předchozích modulech jste se naučili vést konverzace s AI a efektivně strukturovat vaše příkazy. Ale existuje základní omezení: jazykové modely znají pouze to, co se naučily během tréninku. Nemohou odpovídat na otázky ohledně interních firemních pravidel, dokumentace vašeho projektu nebo jakýchkoli informací, které nebyly součástí jejich tréninku.

RAG (Retrieval-Augmented Generation) tento problém řeší. Místo snahy učit model vaše informace (což je drahé a nepraktické) mu umožníte vyhledávat ve vašich dokumentech. Když někdo položí otázku, systém najde relevantní informace a přidá je do příkazu. Model poté odpovídá na základě tohoto získaného kontextu.

Představte si RAG jako poskytnutí referenční knihovny modelu. Když položíte otázku, systém:

1. **Uživatelský dotaz** – zeptáte se
2. **Embedding** – převede otázku na vektor
3. **Vektorové vyhledávání** – najde podobné části dokumentu
4. **Sestavení kontextu** – přidá relevantní části do příkazu
5. **Odpověď** – LLM vygeneruje odpověď na základě kontextu

Tím se zakotví odpovědi modelu ve vašich skutečných datech, místo aby se spoléhal na své tréninkové znalosti nebo vymýšlel odpovědi.

## Pochopení RAG

Níže uvedený diagram ilustruje základní koncept: místo spoléhání se pouze na tréninková data modelu, RAG mu poskytuje referenční knihovnu vašich dokumentů, aby je mohl konzultovat před generováním každé odpovědi.

<img src="../../../translated_images/cs/what-is-rag.1f9005d44b07f2d8.webp" alt="Co je RAG" width="800"/>

Zde je, jak se jednotlivé části propojují od začátku do konce. Uživatelská otázka prochází čtyřmi fázemi — embedding, vektorové vyhledávání, sestavení kontextu a generování odpovědi — přičemž každá navazuje na tu předchozí:

<img src="../../../translated_images/cs/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektura RAG" width="800"/>

Zbytek tohoto modulu podrobně projde každou fázi s kódem, který můžete spustit a upravovat.

## Požadavky

- Absolvovaný Modul 01 (nasazené zdroje Azure OpenAI)
- `.env` soubor v kořenovém adresáři s přihlašovacími údaji Azure (vytvořený příkazem `azd up` v Modulu 01)

> **Poznámka:** Pokud jste Modul 01 nedokončili, nejdříve si prosím projděte tamní pokyny k nasazení.

## Jak to funguje

### Zpracování dokumentů

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Když nahrajete dokument, systém ho zpracuje (PDF nebo prostý text), přidá metadata jako název souboru a poté jej rozdělí na části — menší úseky, které pohodlně zapadnou do kontextového okna modelu. Tyto části se mírně překrývají, aby na hranicích nebyl ztracen žádný kontext.

```java
// Rozparsovat nahraný soubor a zabalit jej do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdělit na části o 300 tokenech s překryvem 30 tokenů
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Níže uvedený diagram ukazuje tuto funkci vizuálně. Všimněte si, jak každá část sdílí některé tokeny se sousedy — překrytí 30 tokenů zajišťuje, že žádný důležitý kontext neupadne mezi trhliny:

<img src="../../../translated_images/cs/document-chunking.a5df1dd1383431ed.webp" alt="Dělení dokumentu na části" width="800"/>

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a zeptejte se:
> - „Jak LangChain4j rozděluje dokumenty na části a proč je překrytí důležité?“
> - „Jaká je optimální velikost části pro různé typy dokumentů a proč?“
> - „Jak zvládám dokumenty v několika jazycích nebo se speciálním formátováním?“

### Vytváření embeddingů

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každá část je převedena na číselnou reprezentaci zvanou embedding – matematiký otisk, který zachycuje význam textu. Podobný text produkuje podobné embeddingy.

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

Následující třídní diagram ukazuje, jak se tyto komponenty LangChain4j propojují. `OpenAiOfficialEmbeddingModel` převádí text na vektory, `InMemoryEmbeddingStore` ukládá vektory spolu s jejich původními daty `TextSegment` a `EmbeddingSearchRequest` řídí parametry vyhledávání jako `maxResults` a `minScore`:

<img src="../../../translated_images/cs/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Třídy LangChain4j RAG" width="800"/>

Jakmile jsou embeddingy uloženy, podobný obsah se přirozeně shlukuje vektorovém prostoru. Vizualizace níže ukazuje, jak dokumenty k souvisejícím tématům končí jako blízké body, což umožňuje sémantické vyhledávání:

<img src="../../../translated_images/cs/vector-embeddings.2ef7bdddac79a327.webp" alt="Prostor vektorových embeddingů" width="800"/>

### Sémantické vyhledávání

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Když položíte otázku, také ona se převede na embedding. Systém porovná embedding vaší otázky se všemi embeddingy částí dokumentů. Najde části s nejpodobnějším významem – nejen shodné klíčové slovo, ale skutečnou sémantickou podobnost.

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

Diagram níže porovnává sémantické vyhledávání s tradičním vyhledáváním podle klíčových slov. Vyhledávání klíčového slova "vozidlo" přehlédne část o „auty a nákladních vozech“, ale sémantické vyhledávání chápe, že mají stejný význam, a vrátí ji jako vysoce hodnocenou shodu:

<img src="../../../translated_images/cs/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhledávání" width="800"/>

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a zeptejte se:
> - „Jak funguje vyhledávání podle podobnosti s embeddingy a co určuje skóre?“
> - „Jaký práh podobnosti bych měl použít a jak ovlivňuje výsledky?“
> - „Jak řeším případy, kdy se nenajdou žádné relevantní dokumenty?“

### Generování odpovědí

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Nej relevantnější části se sestaví do strukturovaného promptu, který obsahuje explicitní instrukce, získaný kontext a uživatelskou otázku. Model tyto konkrétní části přečte a odpoví na základě těchto informací — může použít jen to, co před ním je, což zabraňuje halucinacím.

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

Diagram níže ukazuje toto sestavení v akci — nejlépe hodnocené části z vyhledávání jsou vloženy do šablony promptu a `OpenAiOfficialChatModel` generuje zakotvenou odpověď:

<img src="../../../translated_images/cs/context-assembly.7e6dd60c31f95978.webp" alt="Sestavení kontextu" width="800"/>

## Spuštění aplikace

**Ověření nasazení:**

Zkontrolujte, že v kořenovém adresáři existuje `.env` soubor s přihlašovacími údaji Azure (vytvořeno během Modulu 01):
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikace:**

> **Poznámka:** Pokud jste již spustili všechny aplikace pomocí `./start-all.sh` z Modulu 01, tento modul již běží na portu 8081. Můžete přeskočit níže uvedené příkazy a rovnou přejít na http://localhost:8081.

**Volba 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**

Vývojové kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v bočním panelu vlevo ve VS Code (hledáte ikonu Spring Boot).

Ze Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace ve workspace
- Jedním kliknutím spustit / zastavit aplikace
- Zobrazovat logy aplikace v reálném čase
- Monitorovat stav aplikace

Stačí kliknout na tlačítko play u „rag“ a spustit tento modul, nebo spustit všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Volba 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze základního adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Ze kořenového adresáře
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

Oba skripty automaticky načtou proměnné prostředí z kořenového `.env` souboru a pokud JAR soubory neexistují, postaví je.

> **Poznámka:** Pokud chcete zkompilovat všechny moduly manuálně před spuštěním:
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

Otevřete http://localhost:8081 ve vašem prohlížeči.

**Jak zastavit:**

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

Aplikace poskytuje webové rozhraní pro nahrávání dokumentů a pokládání otázek.

<a href="images/rag-homepage.png"><img src="../../../translated_images/cs/rag-homepage.d90eb5ce1b3caa94.webp" alt="Rozhraní aplikace RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rozhraní aplikace RAG – nahrávejte dokumenty a pokládejte otázky*

### Nahrání dokumentu

Začněte nahráním dokumentu – pro testování nejlépe fungují TXT soubory. V tomto adresáři je k dispozici `sample-document.txt`, který obsahuje informace o funkcích LangChain4j, implementaci RAG a osvědčených postupech – ideální pro testování systému.

Systém váš dokument zpracuje, rozdělí ho na části a vytvoří embeddingy pro každou část. Toto probíhá automaticky po nahrání.

### Pokládání otázek

Teď položte specifické otázky ohledně obsahu dokumentu. Zkuste něco faktického, co je jasně uvedeno v dokumentu. Systém vyhledá relevantní části, zahrne je do promptu a vygeneruje odpověď.

### Kontrola zdrojových odkazů

Všimněte si, že každá odpověď obsahuje odkazy na zdroje se skóre podobnosti. Toto skóre (od 0 do 1) ukazuje, jak relevantní byla každá část pro vaši otázku. Vyšší skóre znamená lepší shodu. To vám umožňuje ověřit odpověď vůči zdrojovému materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/cs/rag-query-results.6d69fcec5397f355.webp" alt="Výsledky dotazů RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Výsledky dotazu zobrazující odpověď se zdrojovými odkazy a skóre relevance*

### Experimentování s otázkami

Vyzkoušejte různé typy otázek:
- Specifická fakta: „Jaké je hlavní téma?“
- Porovnání: „Jaký je rozdíl mezi X a Y?“
- Shrnutí: „Shrňte klíčové body o Z“

Sledujte, jak se skóre relevance mění podle toho, jak dobře vaše otázka odpovídá obsahu dokumentu.

## Klíčové koncepty

### Strategie dělení na části

Dokumenty jsou děleny na části o velikosti 300 tokenů s překrytím 30 tokenů. Tento poměr zajišťuje, že každá část má dostatek kontextu, aby byla smysluplná, a zároveň je dostatečně malá, aby se do promptu vešlo více částí.

### Skóre podobnosti

Každá získaná část je doprovázena skóre podobnosti mezi 0 a 1, které ukazuje, jak úzce odpovídá otázce uživatele. Následující diagram vizualizuje rozsahy skóre a jak je systém využívá k filtrování výsledků:

<img src="../../../translated_images/cs/similarity-scores.b0716aa911abf7f0.webp" alt="Skóre podobnosti" width="800"/>

Skóre se pohybují od 0 do 1:
- 0,7 - 1,0: Vysoce relevantní, přesná shoda
- 0,5 - 0,7: Relevantní, dobrý kontext
- Pod 0,5: Filtrováno, příliš odlišné

Systém získává pouze části nad minimálním prahem, aby zajistil kvalitu.

### Ukládání v paměti

Tento modul používá pro jednoduchost ukládání v paměti. Když aplikaci restartujete, nahrané dokumenty se ztratí. Produkční systémy využívají perzistentní vektorové databáze jako Qdrant nebo Azure AI Search.

### Správa kontextového okna

Každý model má maximální kontextové okno. Nemůžete zahrnout všechny části z velkého dokumentu. Systém získává top N nejrelevantnějších částí (výchozí 5), aby zůstal v rámci limitu a zároveň poskytl dostatek kontextu pro přesné odpovědi.

## Kdy je RAG důležitý

RAG není vždy vhodný přístup. Následující průvodce rozhodnutím vám pomůže určit, kdy RAG přidává hodnotu oproti jednodušším přístupům – jako je zahrnutí obsahu přímo do promptu nebo spoléhat se na vestavěné znalosti modelu:

<img src="../../../translated_images/cs/when-to-use-rag.1016223f6fea26bc.webp" alt="Kdy použít RAG" width="800"/>

**Použijte RAG, když:**
- Odpovídání na otázky týkající se proprietárních dokumentů
- Informace se často mění (zásady, ceny, specifikace)
- Přesnost vyžaduje uvedení zdroje
- Obsah je příliš rozsáhlý na to, aby se vešel do jednoho promptu
- Potřebujete ověřitelné, podložené odpovědi

**Nepoužívejte RAG, když:**
- Otázky vyžadují obecné znalosti, které model již má
- Jsou potřeba data v reálném čase (RAG pracuje s nahranými dokumenty)
- Obsah je dostatečně malý, aby mohl být přímo zahrnut do promptů

## Další kroky

**Další modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigace:** [← Předchozí: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nebereme žádnou odpovědnost za jakékoli nedorozumění nebo chybné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->