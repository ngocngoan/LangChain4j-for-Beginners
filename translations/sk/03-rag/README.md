# Modul 03: RAG (Retrieval-Augmented Generation)

## Obsah

- [Video prehliadka](../../../03-rag)
- [Čo sa naučíte](../../../03-rag)
- [Predpoklady](../../../03-rag)
- [Pochopenie RAG](../../../03-rag)
  - [Ktorý prístup RAG tento tutoriál používa?](../../../03-rag)
- [Ako to funguje](../../../03-rag)
  - [Spracovanie dokumentov](../../../03-rag)
  - [Vytváranie embeddingov](../../../03-rag)
  - [Sémantické vyhľadávanie](../../../03-rag)
  - [Generovanie odpovedí](../../../03-rag)
- [Spustenie aplikácie](../../../03-rag)
- [Používanie aplikácie](../../../03-rag)
  - [Nahratie dokumentu](../../../03-rag)
  - [Kladenie otázok](../../../03-rag)
  - [Overenie zdrojových referencií](../../../03-rag)
  - [Experimentovanie s otázkami](../../../03-rag)
- [Kľúčové koncepty](../../../03-rag)
  - [Strategia chunkovania](../../../03-rag)
  - [Skóre podobnosti](../../../03-rag)
  - [Ukladanie v pamäti](../../../03-rag)
  - [Správa kontextového okna](../../../03-rag)
- [Kedy je RAG dôležité](../../../03-rag)
- [Ďalšie kroky](../../../03-rag)

## Video prehliadka

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom: [RAG s LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Čo sa naučíte

V predchádzajúcich moduloch ste sa naučili, ako viesť rozhovory s AI a efektívne štruktúrovať svoje prompt-y. Ale existuje zásadné obmedzenie: jazykové modely vedia iba to, čo sa naučili počas tréningu. Nevedia odpovedať na otázky o politikách vašej spoločnosti, dokumentácii projektu alebo akejkoľvek informácii, na ktorú neboli trénované.

RAG (Retrieval-Augmented Generation) tento problém rieši. Namiesto pokusu naučiť model vaše informácie (čo je drahé a nepraktické), mu dáte schopnosť vyhľadávať vo vašich dokumentoch. Keď niekto položí otázku, systém nájde relevantné informácie a zahrnie ich do promptu. Model potom odpovedá na základe tohto získaného kontextu.

Predstavte si RAG ako poskytnutie referenčnej knižnice modelu. Keď sa opýtate otázku, systém:

1. **Používateľský dotaz** – Položíte otázku  
2. **Embedding** – Prevod vašej otázky na vektor  
3. **Vektorové vyhľadávanie** – Nájdenie podobných kúskov dokumentu  
4. **Zostavenie kontextu** – Pridanie relevantných kúskov do promptu  
5. **Odpoveď** – LLM generuje odpoveď na základe kontextu  

Týmto sa odpovede modelu zakladajú na vašich skutočných dátach namiesto spoliehania sa na jeho tréningové vedomosti alebo vytváranie odpovedí.

## Predpoklady

- Dokončený [Modul 00 - Rýchly štart](../00-quick-start/README.md) (pre príklad Easy RAG uvedený vyššie)  
- Dokončený [Modul 01 - Úvod](../01-introduction/README.md) (nasadené zdroje Azure OpenAI, vrátane embedding modelu `text-embedding-3-small`)  
- Súbor `.env` v koreňovom adresári s Azure prihlasovacími údajmi (vytvorený príkazom `azd up` v Module 01)  

> **Poznámka:** Ak ste ešte nedokončili Modul 01, najprv postupujte podľa tamojších inštrukcií na nasadenie. Príkaz `azd up` nasadzuje ako GPT chat model, tak aj embedding model používaný v tomto module.

## Pochopenie RAG

Nižšie uvedený diagram ilustruje základný koncept: namiesto spoliehania sa iba na tréningové dáta modelu, RAG mu dáva referenčnú knižnicu vašich dokumentov, ktoré môže konzultovať pred generovaním každej odpovede.

<img src="../../../translated_images/sk/what-is-rag.1f9005d44b07f2d8.webp" alt="Čo je RAG" width="800"/>

*Tento diagram ukazuje rozdiel medzi štandardným LLM (ktorý odhaduje na základe tréningových dát) a LLM rozšíreným o RAG (ktorý najskôr konzultuje vaše dokumenty).*

Takto sú jednotlivé časti prepojené end-to-end. Otázka používateľa prechádza štyrmi fázami — embeddingom, vektorovým vyhľadávaním, zostavením kontextu a generovaním odpovede — pričom každá vychádza z predchádzajúcej:

<img src="../../../translated_images/sk/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektúra RAG" width="800"/>

*Tento diagram zobrazuje end-to-end RAG pipeline — používateľský dotaz prechádza embeddingom, vektorovým vyhľadávaním, zostavením kontextu a generovaním odpovede.*

Zvyšok tohto modulu podrobne prejde každú fázu s kódom, ktorý môžete spustiť a upraviť.

### Ktorý prístup RAG tento tutoriál používa?

LangChain4j ponúka tri spôsoby implementácie RAG, každý s inou úrovňou abstrakcie. Nižšie uvedený diagram ich porovnáva vedľa seba:

<img src="../../../translated_images/sk/rag-approaches.5b97fdcc626f1447.webp" alt="Tri prístupy RAG v LangChain4j" width="800"/>

*Tento diagram porovnáva tri prístupy RAG v LangChain4j — Easy, Native a Advanced — ukazujúc ich kľúčové komponenty a kedy ich použiť.*

| Prístup | Čo robí | Kompromis |
|---|---|---|
| **Easy RAG** | Všetko automaticky prepája cez `AiServices` a `ContentRetriever`. Anotujete rozhranie, pripojíte retriever a LangChain4j za vás zvládne embedding, vyhľadávanie a zostavenie promptu na pozadí. | Minimálny kód, ale nevidíte, čo sa deje v každom kroku. |
| **Native RAG** | Voláte embedding model, vyhľadávate v úložisku, staviate prompt a generujete odpoveď sami — explicitne krok po kroku. | Viac kódu, ale každá fáza je viditeľná a prispôsobiteľná. |
| **Advanced RAG** | Používa framework `RetrievalAugmentor` s modulárnymi transformátormi dotazov, routermi, re-rankermi a injektormi obsahu pre produkčné pipeline. | Maximálna flexibilita, ale výrazne vyššia zložitosť. |

**Tento tutoriál používa Native prístup.** Každý krok RAG pipeline — embedovanie dotazu, vyhľadávanie vo vektorovom úložisku, zostavenie kontextu a generovanie odpovede — je explicitne napísaný v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Je to zámerné: ako učebný zdroj je dôležitejšie vidieť a pochopiť každý krok, než mať minimalizovaný kód. Keď budete pohodlne vedieť, ako jednotlivé časti spolu fungujú, môžete prejsť na Easy RAG pre rýchle prototypy alebo Advanced RAG pre produkčné systémy.

> **💡 Už ste videli Easy RAG v akcii?** Modul [Rýchly štart](../00-quick-start/README.md) obsahuje príklad Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), ktorý používa Easy RAG prístup — LangChain4j automaticky spracováva embedding, vyhľadávanie a zostavenie promptu. Tento modul robí ďalší krok rozbitím pipeline, aby ste mohli vidieť a ovládať každú fázu sami.

<img src="../../../translated_images/sk/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Tento diagram zobrazuje Easy RAG pipeline z `SimpleReaderDemo.java`. Porovnajte to s Native prístupom použitým v tomto module: Easy RAG skrýva embedding, vyhľadávanie a zostavenie promptu za `AiServices` a `ContentRetriever` — vy nahráte dokument, pripojíte retriever a získate odpovede. Native prístup v tomto module tú pipeline rozbíja, takže voláte každú fázu (embedovať, vyhľadať, zostaviť kontext, generovať) sami, čo vám dáva plnú kontrolu a prehľad.*

## Ako to funguje

RAG pipeline v tomto module sa delí na štyri fázy, ktoré bežia v poradí zakaždým, keď používateľ položí otázku. Najprv sa **rozparsuje a rozdelí na kúsky** nahraný dokument. Tieto kúsky sa potom prevedú na **vektorové embeddingy** a uloží sa ich reprezentácia, aby sa dali matematicky porovnávať. Keď príde dotaz, systém vykoná **sémantické vyhľadávanie** na nájdenie najrelevantnejších kúskov a nakoniec ich odovzdá ako kontext LLM na **generovanie odpovede**. Nižšie uvedené sekcie prejdú každú fázu na základe skutočného kódu a diagramov. Pozrime sa na prvý krok.

### Spracovanie dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Keď nahráte dokument, systém ho rozparsuje (PDF alebo čistý text), pripojí metaúdaje ako názov súboru a potom rozdelí na kúsky — menšie časti, ktoré pohodlne vojdú do kontextového okna modelu. Tieto kúsky sa mierne prekrývajú, aby sa nezstratil kontext na hraniciach.

```java
// Analyzujte nahraný súbor a zabaľte ho do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdeľte na kúsky po 300 tokenoch s prekrytím 30 tokenov
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```


Nižšie uvedený diagram vizuálne ukazuje, ako to funguje. Všimnite si, že každý chunk zdieľa niekoľko tokenov so susednými chunkmi — 30-tokenové prekrývanie zabezpečuje, že žiadny dôležitý kontext nepadne medzi škáry:

<img src="../../../translated_images/sk/document-chunking.a5df1dd1383431ed.webp" alt="Chunkovanie dokumentu" width="800"/>

*Tento diagram ukazuje dokument rozdelený na chunki s dĺžkou 300 tokenov a prekrývaním 30 tokenov, čím sa zachováva kontext na hraniciach chunkov.*

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a spýtajte sa:  
> - "Ako LangChain4j rozdeľuje dokumenty na chunki a prečo je prekrývanie dôležité?"  
> - "Aká je optimálna veľkosť chunkov pre rôzne typy dokumentov a prečo?"  
> - "Ako spracovať dokumenty v niekoľkých jazykoch alebo so špeciálnym formátovaním?"

### Vytváranie embeddingov

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každý chunk sa prevedie na číselnú reprezentáciu nazývanú embedding — v podstate prevodník významu na čísla. Embedding model nie je „inteligentný“ ako chat model; nedokáže sledovať inštrukcie, uvažovať ani odpovedať na otázky. Vie však namapovať text do matematického priestoru, kde podobné významy sú blízko seba — napríklad „auto“ blízko „automobil“, „refund politika“ blízko „vraťte mi peniaze“. Chat model je ako osoba, s ktorou môžete hovoriť; embedding model je ultra-dobrý systém na archiváciu.

<img src="../../../translated_images/sk/embedding-model-concept.90760790c336a705.webp" alt="Koncept embedding modelu" width="800"/>

*Tento diagram ukazuje, ako embedding model prevádza text na numerické vektory, pričom podobné významy — ako "auto" a "automobil" — sú blízko seba vo vektorovom priestore.*

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


Nižšie uvedený diagram tried zobrazuje dva samostatné toky v RAG pipeline a triedy LangChain4j, ktoré ich implementujú. **Tok ingestovania** (beží raz pri nahrávaní) rozdelí dokument, embeduje chunky a uloží ich cez `.addAll()`. **Tok dotazu** (beží vždy keď používateľ položí otázku) embeduje otázku, vyhľadá vo store cez `.search()` a predá zodpovedajúci kontext chat modelu. Oba toky sa spájajú na spoločnom rozhraní `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sk/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG triedy" width="800"/>

*Tento diagram ukazuje dva toky v RAG pipeline — ingestovanie a dotaz — a ich prepojenie cez spoločný EmbeddingStore.*

Keď sú embeddingy uložené, podobný obsah sa prirodzene zhlukuje vo vektorovom priestore. Vizualizácia nižšie ukazuje, ako sa dokumenty o príbuzných témach nachádzajú blízko seba, čo umožňuje sémantické vyhľadávanie:

<img src="../../../translated_images/sk/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorový priestor embeddingov" width="800"/>

*Táto vizualizácia ukazuje, ako sa príbuzné dokumenty zhlukujú v 3D vektorovom priestore, pričom témy ako Technická dokumentácia, Obchodné pravidlá a Často kladené otázky tvoria samostatné skupiny.*

Keď používateľ vyhľadáva, systém sleduje štyri kroky: embeduje dokumenty raz, embeduje dotaz pri každom vyhľadávaní, porovnáva vektor dotazu so všetkými uloženými vektormi pomocou kosínovej podobnosti a vracia top-K najlepších kúskov. Diagram nižšie prechádza každým krokom a triedami LangChain4j, ktoré sú zapojené:

<img src="../../../translated_images/sk/embedding-search-steps.f54c907b3c5b4332.webp" alt="Kroky vyhľadávania embeddingov" width="800"/>

*Tento diagram ukazuje štvorstupňový proces vyhľadávania embeddingov: embedovanie dokumentov, embedovanie dotazu, porovnávanie vektorov pomocou kosínovej podobnosti a vrátenie top-K výsledkov.*

### Sémantické vyhľadávanie

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Keď položíte otázku, vaša otázka sa tiež prevedie na embedding. Systém porovná embedding vašej otázky so všetkými embeddingami chunks dokumentu. Nájde chunki s najpodobnejšími významami — nie len hľadajúce zhodné kľúčové slová, ale skutočnú sémantickú podobnosť.

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


Nižšie uvedený diagram kontrastuje sémantické vyhľadávanie s tradičným vyhľadávaním podľa kľúčových slov. Vyhľadávanie podľa kľúčového slova „vozidlo“ prehliadne chunk o „autách a nákladiakoch“, ale sémantické vyhľadávanie chápe, že znamenajú to isté a vráti ho ako vysoko hodnotený výsledok:

<img src="../../../translated_images/sk/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhľadávanie" width="800"/>

*Tento diagram porovnáva vyhľadávanie podľa kľúčových slov so sémantickým vyhľadávaním, ktoré vracia obsah koncepčne príbuzný, aj keď presné kľúčové slová sa líšia.*

Pod kapotou sa podobnosť meria pomocou kosínovej podobnosti — v podstate sa pýta „ukazujú tieto dve šípky rovnakým smerom?“ Dva chunki môžu použiť úplne odlišné slová, ale ak znamenajú to isté, ich vektory ukazujú rovnakým smerom a skóre je blízke 1.0:

<img src="../../../translated_images/sk/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosínová podobnosť" width="800"/>

*Tento diagram ilustruje kosínovú podobnosť ako uhol medzi vektorovými embeddingami — viac zarovnané vektory dosahujú skóre bližšie k 1.0, čo indikuje vyššiu sémantickú podobnosť.*
> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a spýtajte sa:
> - „Ako funguje vyhľadávanie podobnosti s embeddings a čo určuje skóre?“
> - „Aký prah podobnosti by som mal použiť a ako ovplyvňuje výsledky?“
> - „Ako riešim prípady, keď sa nenájdu žiadne relevantné dokumenty?“

### Generovanie odpovede

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantnejšie úseky sú zostavené do štruktúrovaného promptu, ktorý obsahuje explicitné pokyny, získaný kontext a otázku používateľa. Model číta práve tieto konkrétne úseky a odpovedá na základe týchto informácií — môže použiť len to, čo má pred sebou, čo zabraňuje vymýšľaniu (hallucinácii).

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

Nižšie uvedený diagram ukazuje túto skladbu v akcii — najlepšie skórujúce časti z kroku vyhľadávania sú vložené do šablóny promptu a `OpenAiOfficialChatModel` generuje podloženú odpoveď:

<img src="../../../translated_images/sk/context-assembly.7e6dd60c31f95978.webp" alt="Zostavenie kontextu" width="800"/>

*Tento diagram ukazuje, ako sú najlepšie skórujúce časti zostavené do štruktúrovaného promptu, čo umožňuje modelu generovať podloženú odpoveď z vašich dát.*

## Spustenie aplikácie

**Overenie nasadenia:**

Uistite sa, že v koreňovom adresári existuje `.env` súbor s prihlasovacími údajmi pre Azure (vytvorený počas Modulu 01):

**Bash:**
```bash
cat ../.env  # Mala by zobrazovať AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mal by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustenie aplikácie:**

> **Poznámka:** Ak ste už spustili všetky aplikácie pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8081. Môžete vynechať nasledujúce spúšťacie príkazy a prejsť priamo na http://localhost:8081.

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v bočnom panely aktivity na ľavej strane VS Code (hľadajte ikonu Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie vo vašom pracovnom priemere
- Jedným kliknutím spustiť/zastaviť aplikácie
- Zobraziť logy aplikácie v reálnom čase
- Monitorovať stav aplikácie

Jednoducho kliknite na tlačidlo prehrávania vedľa „rag“ na spustenie tohto modulu, alebo spustite naraz všetky moduly.

<img src="../../../translated_images/sk/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Táto snímka obrazovky ukazuje Spring Boot Dashboard vo VS Code, kde môžete vizuálne spúšťať, zastavovať a sledovať aplikácie.*

**Možnosť 2: Použitie shell skriptov**

Spustiť všetky webové aplikácie (moduly 01-04):

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

Alebo spustiť len tento modul:

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

Oba skripty automaticky načítajú environmentálne premenné z koreňového `.env` súboru a zostavia JAR súbory, ak ešte neexistujú.

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

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Iba tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie na nahrávanie dokumentov a kladenie otázok.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sk/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG rozhranie aplikácie" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Táto snímka obrazovky zobrazuje rozhranie RAG aplikácie, kde nahrávate dokumenty a kladiete otázky.*

### Nahratie dokumentu

Začnite nahratím dokumentu - na testovanie najlepšie fungujú TXT súbory. V tomto adresári je poskytnutý `sample-document.txt`, ktorý obsahuje informácie o funkciách LangChain4j, implementácii RAG a najlepších postupoch - ideálne na testovanie systému.

Systém spracuje váš dokument, rozdelí ho na úseky a pre každý úsek vytvorí embeddings. Toto sa deje automaticky po nahraní.

### Kladenie otázok

Teraz položte konkrétne otázky o obsahu dokumentu. Vyskúšajte niečo faktické, čo je v dokumente jasne uvedené. Systém vyhľadá relevantné úseky, zahrnie ich do promptu a vygeneruje odpoveď.

### Kontrola zdrojových referencií

Všimnite si, že každá odpoveď obsahuje zdrojové referencie s hodnotami podobnosti. Tieto skóre (od 0 do 1) ukazujú, ako relevantný bol každý úsek pre vašu otázku. Vyššie skóre znamená lepšie zhody. To vám umožňuje overiť odpoveď oproti originálnemu materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sk/rag-query-results.6d69fcec5397f355.webp" alt="Výsledky dotazu RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Táto snímka ukazuje výsledky dotazu s generovanou odpoveďou, zdrojovými referenciami a skóre relevantnosti pre každý získaný úsek.*

### Experimentovanie s otázkami

Vyskúšajte rôzne druhy otázok:
- Špecifické fakty: „Aká je hlavná téma?“
- Porovnania: „Aký je rozdiel medzi X a Y?“
- Zhrnutia: „Zhrňte kľúčové body o Z“

Sledujte, ako sa mení skóre relevantnosti podľa toho, ako dobre vaša otázka zodpovedá obsahu dokumentu.

## Kľúčové koncepty

### Strategia delenia na úseky

Dokumenty sa delia na úseky po 300 tokenoch so 30 tokenovým prekrývaním. Tento pomer zabezpečuje, že každý úsek má dostatok kontextu, aby bol zmysluplný, a zároveň je dostatočne malý na to, aby sa mohlo do promptu zaradiť viac úsekov.

### Skóre podobnosti

Každý získaný úsek má skóre podobnosti od 0 do 1, ktoré udáva, ako úzko zodpovedá otázke používateľa. Nižšie uvedený diagram vizualizuje rozsahy skóre a ako ich systém používa na filtrovanie výsledkov:

<img src="../../../translated_images/sk/similarity-scores.b0716aa911abf7f0.webp" alt="Skóre podobnosti" width="800"/>

*Tento diagram ukazuje rozsahy skóre od 0 do 1, s minimálnym prahom 0,5, ktorý vylučuje nerelevantné úseky.*

Skóre sa pohybuje od 0 do 1:
- 0,7-1,0: Veľmi relevantné, presná zhoda
- 0,5-0,7: Relevantné, dobrý kontext
- Pod 0,5: Filtrované, príliš odlišné

Systém získava iba úseky nad minimálnym prahom, aby zabezpečil kvalitu.

Embeddings fungujú dobre, keď sa významy zreteľne zhlukujú, ale majú aj slabiny. Nižšie uvedený diagram ukazuje bežné chyby — príliš veľké úseky produkujú nejasné vektory, príliš malé úseky nemajú kontext, nejednoznačné pojmy ukazujú na viacero zhlukov a presné vyhľadávanie (ID, číselné označenia) vôbec nefunguje s embeddings:

<img src="../../../translated_images/sk/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Módy zlyhania embeddingov" width="800"/>

*Tento diagram ukazuje bežné módy zlyhania embeddingov: príliš veľké úseky, príliš malé úseky, nejednoznačné termíny ukazujúce na viaceré zhluky a presné vyhľadávanie ako ID.*

### Ukladanie v pamäti

Tento modul používa na jednoduchosť ukladanie v pamäti. Pri reštarte aplikácie sa nahraté dokumenty stratia. Produkčné systémy používajú perzistentné vektorové databázy ako Qdrant alebo Azure AI Search.

### Správa kontextového okna

Každý model má maximálnu veľkosť kontextového okna. Nemôžete zahrnúť každý úsek z veľkého dokumentu. Systém vyberá top N najrelevantnejších úsekov (predvolené 5), aby zostal v limite a zároveň poskytol dosť kontextu pre presné odpovede.

## Kedy je RAG dôležitý

RAG nie je vždy správny prístup. Nižšie uvedený rozhodovací graf vám pomôže určiť, kedy RAG pridáva hodnotu a kedy sú vhodnejšie jednoduchšie prístupy — ako priamo zahrnutie obsahu do promptu alebo spoliehanie sa na vedomosti modelu:

<img src="../../../translated_images/sk/when-to-use-rag.1016223f6fea26bc.webp" alt="Kedy použiť RAG" width="800"/>

*Tento diagram zobrazuje rozhodovací graf, kedy RAG pridáva hodnotu a kedy sú jednoduchšie prístupy postačujúce.*

**Použite RAG, keď:**
- Odpovedáte na otázky o proprietárnych dokumentoch
- Informácie sa často menia (politiky, ceny, špecifikácie)
- Presnosť vyžaduje atribúciu zdroja
- Obsah je príliš rozsiahly na vloženie do jediného promptu
- Potrebujete overiteľné, podložené odpovede

**Nepoužívajte RAG, keď:**
- Otázky vyžadujú všeobecné znalosti, ktoré model už má
- Potrebujete dáta v reálnom čase (RAG pracuje s nahratými dokumentmi)
- Obsah je dostatočne malý na priame zaradenie do promptov

## Ďalšie kroky

**Ďalší modul:** [04-tools - AI agenti s nástrojmi](../04-tools/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 02 - Inžinierstvo promptov](../02-prompt-engineering/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 04 - Nástroje →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:  
Tento dokument bol preložený pomocou automatizovanej prekladateľskej služby AI [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci usilovne pracujeme na presnosti, prosím, majte na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pri dôležitých informáciách sa odporúča profesionálny ľudský preklad. Nezodpovedáme za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->