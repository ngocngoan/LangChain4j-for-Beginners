# Modul 03: RAG (Retrieval-Augmented Generation)

## Obsah

- [Video Prehľad](../../../03-rag)
- [Čo sa Naučíte](../../../03-rag)
- [Predpoklady](../../../03-rag)
- [Pochopenie RAG](../../../03-rag)
  - [Ktorý RAG Prístup Tento Tutoriál Používa?](../../../03-rag)
- [Ako To Funguje](../../../03-rag)
  - [Spracovanie Dokumentov](../../../03-rag)
  - [Vytváranie Embeddingov](../../../03-rag)
  - [Sémantické Vyhľadávanie](../../../03-rag)
  - [Generovanie Odpovedí](../../../03-rag)
- [Spustenie Aplikácie](../../../03-rag)
- [Používanie Aplikácie](../../../03-rag)
  - [Nahranie Dokumentu](../../../03-rag)
  - [Kladenie Otázok](../../../03-rag)
  - [Kontrola Zdrojových Referencií](../../../03-rag)
  - [Experimentovanie s Otázkami](../../../03-rag)
- [Kľúčové Koncepty](../../../03-rag)
  - [Stratégia Členenia](../../../03-rag)
  - [Skóre Podobnosti](../../../03-rag)
  - [Ukladanie v Pamäti](../../../03-rag)
  - [Správa Kontextového Okna](../../../03-rag)
- [Kedy RAG Záleží](../../../03-rag)
- [Ďalšie Kroky](../../../03-rag)

## Video Prehľad

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG s LangChain4j - Živá relácia" width="800"/></a>

## Čo sa Naučíte

V predchádzajúcich moduloch ste sa naučili viesť konverzácie s AI a efektívne štruktúrovať svoje prompt-y. Ale existuje základné obmedzenie: jazykové modely vedia len to, čo sa naučili počas tréningu. Nevedia odpovedať na otázky o politikách vašej firmy, dokumentácii projektov alebo informáciách, ktoré neboli súčasťou ich tréningu.

RAG (Retrieval-Augmented Generation) tento problém rieši. Namiesto toho, aby ste model učili vaše informácie (čo je nákladné a nepraktické), dáte mu možnosť prehľadávať vaše dokumenty. Keď niekto položí otázku, systém nájde relevantné informácie a zahrnie ich do promptu. Model potom odpovie na základe tohto získaného kontextu.

Predstavte si RAG ako poskytnutie referenčnej knižnice modelu. Keď sa opýtate otázku, systém:

1. **Používateľský Dotaz** – Položíte otázku  
2. **Embedding** – Prevedie vašu otázku na vektor  
3. **Vektorové Vyhľadávanie** – Nájde podobné časti dokumentu  
4. **Zostavenie Kontextu** – Pridá relevantné časti do promptu  
5. **Odpoveď** – LLM generuje odpoveď na základe kontextu

Týmto spôsobom sú odpovede modelu zakotvené vo vašich skutočných dátach namiesto spoliehania sa na znalosti z tréningu alebo vymýšľania odpovedí.

## Predpoklady

- Dokončený [Modul 00 - Rýchly Začiatok](../00-quick-start/README.md) (pre príklad Easy RAG, na ktorý sa neskôr v tomto module odkazuje)
- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasadené Azure OpenAI zdroje vrátane embedding modelu `text-embedding-3-small`)
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený príkazom `azd up` v Module 01)

> **Poznámka:** Ak ste nedokončili Modul 01, najprv postupujte podľa tam uvedených inštrukcií na nasadenie. Príkaz `azd up` nasadí chatovací GPT model aj embedding model používaný v tomto module.

## Pochopenie RAG

Nižšie uvedený diagram ilustruje základný koncept: namiesto spoléhaniu sa len na tréningové dáta modelu, RAG mu poskytuje referenčnú knižnicu vašich dokumentov, do ktorej môže nazrieť pred generovaním každej odpovede.

<img src="../../../translated_images/sk/what-is-rag.1f9005d44b07f2d8.webp" alt="Čo je RAG" width="800"/>

*Tento diagram ukazuje rozdiel medzi štandardným LLM (ktorý tipuje na základe tréningových dát) a LLM obohateným o RAG (ktorý sa najprv obracia na vaše dokumenty).*

Tu je, ako sú kusy prepojené od začiatku do konca. Otázka používateľa prechádza štyrmi fázami — embedding, vektorové vyhľadávanie, zostavenie kontextu a generovanie odpovede — pričom každá nadväzuje na predchádzajúcu:

<img src="../../../translated_images/sk/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektúra RAG" width="800"/>

*Tento diagram ukazuje kompletný RAG proces — používateľský dotaz prechádza embeddingom, vektorovým vyhľadávaním, zostavením kontextu a generovaním odpovede.*

Zvyšok tohto modulu podrobne prechádza každú etapu s kódom, ktorý môžete spustiť a upraviť.

### Ktorý RAG Prístup Tento Tutoriál Používa?

LangChain4j ponúka tri spôsoby implementácie RAG, každý s inou úrovňou abstrakcie. Nižšie uvedený diagram ich porovnáva vedľa seba:

<img src="../../../translated_images/sk/rag-approaches.5b97fdcc626f1447.webp" alt="Tri RAG Prístupy v LangChain4j" width="800"/>

*Tento diagram porovnáva tri LangChain4j RAG prístupy — Easy, Native a Advanced — ukazujúc ich kľúčové komponenty a kedy ich použiť.*

| Prístup | Čo robí | Kompromis |
|---|---|---|
| **Easy RAG** | Automaticky prepája všetko cez `AiServices` a `ContentRetriever`. Vy označíte rozhranie, pripojíte retriever a LangChain4j sa za vás stará o embedding, vyhľadávanie a zostavenie promptu. | Minimálny kód, ale nevidíte každý krok. |
| **Native RAG** | Voláte embedding model, vyhľadávate v úložisku, staviate prompt a generujete odpoveď sami, krok po kroku. | Viac kódu, ale každá fáza je viditeľná a upraviteľná. |
| **Advanced RAG** | Používa framework `RetrievalAugmentor` s rozšíriteľnými transformátormi dotazov, routermi, re-rankermi a injektormi obsahu pre produkčné pipeline. | Maximálna flexibilita, ale výrazne zložitejšie. |

**Tento tutoriál používa prístup Native.** Každý krok RAG pipeline — embedding dotazu, vyhľadávanie vo vektorovom úložisku, zostavenie kontextu a generovanie odpovede — je explicitne napísaný v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Je to zámerné: ako zdroj učenia je dôležitejšie, aby ste videli a pochopili každý krok, než aby bol kód minimalizovaný. Keď sa s tým cítite komfortne, môžete prejsť na Easy RAG pre rýchle prototypy alebo Advanced RAG pre produkčné systémy.

> **💡 Už ste videli Easy RAG v praxi?** Modul [Rýchly Začiatok](../00-quick-start/README.md) obsahuje príklad Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) používajúci Easy RAG — LangChain4j automaticky zabezpečuje embedding, vyhľadávanie a zostavenie promptu. Tento modul ide ďalej a rozoberá túto pipeline tak, aby ste každý krok videli a mohli ovládať sami.

Nižšie uvedený diagram zobrazuje pipeline Easy RAG z príkladu Rýchleho Začiatku. Všimnite si, ako `AiServices` a `EmbeddingStoreContentRetriever` skrývajú všetku zložitosť — načítate dokument, pripojíte retriever a dostanete odpovede. Native prístup v tomto module rozoberá každý z týchto skrytých krokov:

<img src="../../../translated_images/sk/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Tento diagram ukazuje pipeline Easy RAG z `SimpleReaderDemo.java`. Porovnajte to s Native prístupom použitým v tomto module: Easy RAG skrýva embedding, vyhľadávanie a zostavenie promptu za `AiServices` a `ContentRetriever` — načítate dokument, pripojíte retriever a dostanete odpovede. Native prístup rozoberá túto pipeline, takže voláte každý krok (embed, vyhľadanie, zostavenie kontextu, generovanie) sami, čo vám dáva plnú viditeľnosť a kontrolu.*

## Ako To Funguje

RAG pipeline v tomto module sa skladá zo štyroch fáz, ktoré sa spúšťajú po sebe vždy, keď používateľ položí otázku. Najskôr je nahratý dokument **parsovaný a rozdelený na časti** vhodnej veľkosti. Tieto časti sa následne prevedú na **vektorové embeddingy** a uložia sa for matematickú porovnateľnosť. Keď príde dopyt, systém vykoná **sémantické vyhľadávanie**, aby našiel najrelevantnejšie časti, a nakoniec ich odovzdá ako kontext do LLM pre **generovanie odpovede**. Nižšie si prejdeme každú fázu s reálnym kódom a diagramami. Pozrime sa najskôr na prvý krok.

### Spracovanie Dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Keď nahráte dokument, systém ho spracuje (PDF alebo čistý text), pripojí metadata ako názov súboru a potom ho rozdelí na časti — menšie kúsky, ktoré sa pohodlne zmestia do kontextového okna modelu. Tieto časti sa čiastočne prekrývajú, aby sa nezmizol kontext na hraniciach.

```java
// Analyzujte nahraný súbor a zabaľte ho do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdeľte na 300-tokenové časti s 30-tokenovým prekrytím
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Nižšie uvedený diagram vizuálne ukazuje, ako to funguje. Všimnite si, že každá časť zdieľa niektoré tokeny so susedmi — prekrytie 30 tokenov zabezpečuje, že sa nestratí žiadny dôležitý kontext medzi časťami:

<img src="../../../translated_images/sk/document-chunking.a5df1dd1383431ed.webp" alt="Členenie Dokumentu" width="800"/>

*Tento diagram ukazuje rozdelenie dokumentu na 300-tokenové časti s prekrytím 30 tokenov, čím sa zachováva kontext na hraniciach častí.*

> **🤖 Vyskúšajte chat s [GitHub Copilot](https://github.com/features/copilot):** Otvorte [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a opýtajte sa:
> - "Ako LangChain4j rozdeľuje dokumenty na časti a prečo je prekrytie dôležité?"
> - "Aká je optimálna veľkosť častí pre rôzne typy dokumentov a prečo?"
> - "Ako spracovať dokumenty v rôznych jazykoch alebo so špeciálnym formátovaním?"

### Vytváranie Embeddingov

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každá časť sa prevedie na číselné vyjadrenie nazývané embedding — v podstate prevodník významu na čísla. Embedding model nie je „inteligentný“ ako chat model; nedokáže nasledovať inštrukcie, argumentovať ani odpovedať na otázky. Vie však premietnuť text do matematického priestoru, kde podobné významy ležia blízko seba — „auto“ blízko „automobil“, „refundácia“ blízko „vrátenie peňazí“. Predstavte si chat model ako osobu, s ktorou môžete hovoriť; embedding model je ultra dobrý systém na usporiadanie súborov.

Nižšie uvedený diagram vizualizuje tento koncept — text vstupuje, vychádzajú číselné vektory a podobné významy produkujú vektory blízko seba:

<img src="../../../translated_images/sk/embedding-model-concept.90760790c336a705.webp" alt="Koncept embedding modelu" width="800"/>

*Tento diagram ukazuje, ako embedding model prevádza text na číselné vektory, pričom podobné významy — ako "auto" a "automobil" — sú blízko seba vo vektorovom priestore.*

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
  
Nižšie uvedený diagram tried ukazuje dva samostatné toky v RAG pipeleine a LangChain4j triedy, ktoré ich implementujú. **Tok ingestie** (beží raz pri nahrávaní) rozdeľuje dokument, vytvára embeddingy častí a ukladá ich cez `.addAll()`. **Tok dotazu** (beží vždy, keď používateľ položí otázku) vytvára embedding otázky, vyhľadáva v úložisku cez `.search()` a odovzdáva nájdený kontext do chat modelu. Obidva toky sa spájajú v zdieľanom rozhraní `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sk/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Triedy" width="800"/>

*Tento diagram ukazuje dva toky v RAG pipeline — ingestiu a dotaz — a ich prepojenie cez zdieľané EmbeddingStore.*

Keď sú embeddingy uložené, podobný obsah sa prirodzene zhlukuje vo vektorovom priestore. Nižšie uvedená vizualizácia ukazuje, ako dokumenty o súvisiacich témach končia blízko seba, čo umožňuje sémantické vyhľadávanie:

<img src="../../../translated_images/sk/vector-embeddings.2ef7bdddac79a327.webp" alt="Priestor vektorových embeddingov" width="800"/>

*Táto vizualizácia ukazuje, ako sa súvisiace dokumenty zhlukujú v 3D vektorovom priestore, pričom témy ako Technická dokumentácia, Firemné pravidlá a Často kladené otázky tvorí samostatné skupiny.*

Keď používateľ vyhľadáva, systém vykonáva štyri kroky: embeduje dokumenty raz, embeduje dotaz pri každom vyhľadávaní, porovnáva dotazový vektor so všetkými uloženými vektormi pomocou kosínusovej podobnosti a vracia top-K najlepšie hodnotené časti. Nižšie uvedený diagram prechádza každým krokom a zúčastnenými LangChain4j triedami:

<img src="../../../translated_images/sk/embedding-search-steps.f54c907b3c5b4332.webp" alt="Kroky vyhľadávania embeddingov" width="800"/>

*Tento diagram ukazuje štvorkrokový proces vyhľadávania embeddingov: embedovanie dokumentov, embedovanie dotazu, porovnávanie vektorov s kosínusovou podobnosťou a vrátenie top-K výsledkov.*

### Sémantické Vyhľadávanie

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Keď položíte otázku, vaša otázka sa tiež prevedie na embedding. Systém porovná embedding vašej otázky so všetkými embeddingmi častí dokumentu. Nájde časti s najpodobnejšími významami — nie len kľúčové slová, ale reálnu sémantickú podobnosť.

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
  
Nižšie uvedený diagram kontrastuje sémantické vyhľadávanie s tradičným vyhľadávaním podľa kľúčových slov. Vyhľadávanie podľa kľúčového slova „vozidlo“ prehliadne časť o „autách a nákladiakoch“, ale sémantické vyhľadávanie chápe, že znamenajú to isté a vráti ju ako najlepšie hodnotenú zhodu:

<img src="../../../translated_images/sk/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhľadávanie" width="800"/>

*Tento diagram porovnáva vyhľadávanie založené na kľúčových slovách so sémantickým vyhľadávaním, ukazujúc, ako sémantické vyhľadávanie získava obsah konceptuálne príbuzný, aj keď sa kľúčové slová líšia.*
Pod kapotou sa podobnosť meria pomocou kosínovej podobnosti — v podstate sa pýta "ukazujú tieto dve šípky rovnakým smerom?" Dve časti môžu používať úplne odlišné slová, ale ak znamenajú to isté, ich vektory smerujú rovnako a skóre je blízke 1.0:

<img src="../../../translated_images/sk/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosínová podobnosť" width="800"/>

*Táto schéma znázorňuje kosínovú podobnosť ako uhol medzi vektormi embeddingov — čím sú vektory zosúladené, tým bližšie je skóre k 1.0, čo naznačuje vyššiu sémantickú podobnosť.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a opýtajte sa:
> - "Ako funguje vyhľadávanie podobnosti pomocou embeddingov a čo určuje skóre?"
> - "Aký prah podobnosti mám použiť a ako to ovplyvňuje výsledky?"
> - "Ako riešiť prípady, keď sa nenájdu žiadne relevantné dokumenty?"

### Generovanie odpovedí

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantnejšie časti sa zostavujú do štruktúrovaného promptu, ktorý obsahuje explicitné inštrukcie, získaný kontext a otázku používateľa. Model číta tieto konkrétne časti a odpovedá na základe týchto informácií — môže použiť iba to, čo má pred sebou, čím sa zabraňuje halucináciám.

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

Nižšie uvedená schéma ukazuje tento proces zostavovania v akcii — časti s najvyšším skóre z vyhľadávacieho kroku sa vložia do promptu a `OpenAiOfficialChatModel` vytvorí podloženú odpoveď:

<img src="../../../translated_images/sk/context-assembly.7e6dd60c31f95978.webp" alt="Zostavenie kontextu" width="800"/>

*Táto schéma ukazuje, ako sa najlepšie časti zostavujú do štruktúrovaného promptu, čo umožňuje modelu generovať podloženú odpoveď z vašich dát.*

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure povereniami (vytvorené počas modulu 01). Spustite to z adresára modulu (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z koreňového adresára (ako je popísané v Module 01), tento modul už beží na porte 8081. Môžete preskočiť spúšťacie príkazy nižšie a ísť priamo na http://localhost:8081.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Vývojársky kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v paneli aktivít na ľavej strane VS Code (hľadajte ikonu Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Spustiť/zastaviť aplikácie jedným kliknutím
- Zobraziť denníky aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo play vedľa "rag" pre spustenie tohto modulu, alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Tento snímok obrazovky zobrazuje Spring Boot Dashboard vo VS Code, kde môžete vizuálne spúšťať, zastavovať a monitorovať aplikácie.*

**Možnosť 2: Použitie shell skriptov**

Spustite všetky webové aplikácie (moduly 01-04):

**Bash:**
```bash
cd ..  # Z koreňového adresára
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z koreňového adresára
.\start-all.ps1
```

Alebo spustite iba tento modul:

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

Obidva skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a zostavia JAR, ak ešte neexistuje.

> **Poznámka:** Ak chcete pred spustením manuálne zostaviť všetky moduly:
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

Otvorte http://localhost:8081 vo vašom prehliadači.

**Pre zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Len tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie na nahrávanie dokumentov a kladenie otázok.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sk/rag-homepage.d90eb5ce1b3caa94.webp" alt="Rozhranie aplikácie RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento snímok obrazovky zobrazuje rozhranie aplikácie RAG, kde môžete nahrávať dokumenty a klásť otázky.*

### Nahranie dokumentu

Začnite nahraním dokumentu — na testovanie najlepšie fungujú TXT súbory. V tomto adresári je poskytovaný súbor `sample-document.txt`, ktorý obsahuje informácie o funkciách LangChain4j, implementácii RAG a najlepších postupoch — ideálne pre testovanie systému.

Systém spracuje váš dokument, rozdelí ho na časti a vytvorí embeddingy pre každú časť. To sa deje automaticky po nahraní.

### Kladenie otázok

Teraz položte konkrétne otázky o obsahu dokumentu. Skúste niečo faktické, čo je jasne uvedené v dokumente. Systém vyhľadá relevantné časti, zahrnie ich do promptu a vygeneruje odpoveď.

### Kontrola zdrojových odkazov

Všimnite si, že každá odpoveď obsahuje zdrojové odkazy s hodnotami podobnosti. Tieto skóre (od 0 do 1) ukazujú, ako relevantná bola každá časť voči vašej otázke. Vyššie skóre znamená lepšie zhody. To vám umožní overiť odpoveď voči zdrojovému materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sk/rag-query-results.6d69fcec5397f355.webp" alt="Výsledky dotazu RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Tento snímok obrazovky zobrazuje výsledky dotazu s vygenerovanou odpoveďou, zdrojovými odkazmi a skóre relevantnosti pre každú získanú časť.*

### Experimentujte s otázkami

Vyskúšajte rôzne typy otázok:
- Konkrétne fakty: "Aká je hlavná téma?"
- Porovnania: "Aký je rozdiel medzi X a Y?"
- Zhrnutia: "Zhrňte kľúčové body o Z"

Sledujte, ako sa skóre relevantnosti mení na základe toho, ako dobre vaša otázka zodpovedá obsahu dokumentu.

## Kľúčové koncepty

### Stratégia delenia na časti (Chunking)

Dokumenty sa rozdeľujú na časti po 300 tokenoch s prekrytím 30 tokenov. Tento kompromis zabezpečuje, že každá časť má dostatok kontextu na zmysluplnosť, pritom je dostatočne malá, aby sa mohlo zahrnúť viac častí do promptu.

### Skóre podobnosti

Každá získaná časť prichádza so skóre podobnosti od 0 do 1, ktoré ukazuje, ako veľmi zodpovedá otázke používateľa. Nižšie uvedená schéma vizualizuje rozsahy skóre a ako ich systém používa na filtrovanie výsledkov:

<img src="../../../translated_images/sk/similarity-scores.b0716aa911abf7f0.webp" alt="Skóre podobnosti" width="800"/>

*Táto schéma zobrazuje rozsahy skóre od 0 do 1, s minimálnym prahom 0,5, ktorý filtruje irelevantné časti.*

Skóre sa pohybujú od 0 do 1:
- 0,7-1,0: Vysoko relevantné, presná zhoda
- 0,5-0,7: Relevantné, dobrý kontext
- Pod 0,5: Filtrované, príliš odlišné

Systém získava iba časti nad minimálnym prahom, aby zaistil kvalitu.

Embeddingy dobre fungujú, keď sa význam zreteľne zoskupuje, ale majú slabiny. Nižšie uvedená schéma ukazuje bežné chyby — príliš veľké časti produkujú nejasné vektory, príliš malé časti postrádajú kontext, dvojsmyselné termíny vedú do viacerých klastrov a presné vyhľadávanie (ID, čísla dielov) s embeddingmi vôbec nefunguje:

<img src="../../../translated_images/sk/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Režimy zlyhania embeddingov" width="800"/>

*Táto schéma znázorňuje bežné režimy zlyhania embeddingov: príliš veľké časti, príliš malé časti, dvojsmyselné výrazy vedúce k viacerým klastrom a presné vyhľadávanie ako ID.*

### Ukladanie v pamäti (In-Memory Storage)

Tento modul používa pre jednoduchosť ukladanie v pamäti. Po reštarte aplikácie sa nahrané dokumenty stratia. Produkčné systémy používajú perzistentné úložiská vektorov ako Qdrant alebo Azure AI Search.

### Správa kontextového okna

Každý model má maximálnu veľkosť kontextového okna. Nie je možné zahrnúť všetky časti veľkého dokumentu. Systém načíta najrelevantnejších N častí (predvolene 5), aby sa zmestil do limitov a poskytol dostatok kontextu na presné odpovede.

## Kedy je RAG dôležitý

RAG nie je vždy správny prístup. Nižšie uvedený rozhodovací diagram vám pomôže určiť, kedy RAG prináša hodnotu a kedy sú jednoduchšie prístupy — ako zahrnutie obsahu priamo do promptu alebo spoliehanie sa na zabudované vedomosti modelu — postačujúce:

<img src="../../../translated_images/sk/when-to-use-rag.1016223f6fea26bc.webp" alt="Kedy použiť RAG" width="800"/>

*Táto schéma ukazuje rozhodovací diagram, kedy RAG pridáva hodnotu a kedy sú postačujúce jednoduchšie prístupy.*

## Ďalšie kroky

**Ďalší modul:** [04-tools - AI agenti s nástrojmi](../04-tools/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Späť na hlavné](../README.md) | [Ďalší: Modul 04 - Nástroje →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->