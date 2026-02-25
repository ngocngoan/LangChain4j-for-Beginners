# Modul 03: RAG (Retrieval-Augmented Generation)

## Obsah

- [Čo sa naučíte](../../../03-rag)
- [Pochopenie RAG](../../../03-rag)
- [Predpoklady](../../../03-rag)
- [Ako to funguje](../../../03-rag)
  - [Spracovanie dokumentov](../../../03-rag)
  - [Tvorba embeddingov](../../../03-rag)
  - [Sémantické vyhľadávanie](../../../03-rag)
  - [Generovanie odpovedí](../../../03-rag)
- [Spustenie aplikácie](../../../03-rag)
- [Používanie aplikácie](../../../03-rag)
  - [Nahranie dokumentu](../../../03-rag)
  - [Položenie otázok](../../../03-rag)
  - [Kontrola zdrojových referencií](../../../03-rag)
  - [Experimentovanie s otázkami](../../../03-rag)
- [Kľúčové koncepty](../../../03-rag)
  - [Stratégia rozdeľovania na časti](../../../03-rag)
  - [Skóre podobnosti](../../../03-rag)
  - [Ukladanie v pamäti](../../../03-rag)
  - [Správa kontextového okna](../../../03-rag)
- [Kedy je RAG dôležité](../../../03-rag)
- [Ďalšie kroky](../../../03-rag)

## Čo sa naučíte

V predchádzajúcich moduloch ste sa naučili viesť konverzácie s AI a efektívne štruktúrovať svoje prompt-y. Avšak existuje základné obmedzenie: jazykové modely vedia iba to, čo sa naučili počas tréningu. Nevedia odpovedať na otázky o politikách vašej spoločnosti, dokumentácii k projektom ani na akékoľvek informácie, na ktoré neboli trénované.

RAG (Retrieval-Augmented Generation) tento problém rieši. Namiesto snahy naučiť model vaše informácie (čo je drahé a nepraktické), mu dáte schopnosť vyhľadávať vo vašich dokumentoch. Keď niekto položí otázku, systém nájde relevantné informácie a zahrnie ich do promptu. Model potom odpovedá na základe tohto načítaného kontextu.

Predstavte si RAG ako poskytnutie referenčnej knižnice modelu. Keď položíte otázku, systém:

1. **Užívateľský dotaz** - Položíte otázku  
2. **Embedding** - Prevedie vašu otázku na vektor  
3. **Vyhľadávanie vo vektoroch** - Nájde podobné časti dokumentov  
4. **Skladanie kontextu** - Pridá relevantné časti do promptu  
5. **Odpoveď** - LLM generuje odpoveď na základe kontextu  

Tým sa zakladá odpoveď modelu na vašich skutočných dátach namiesto spoliehania sa na jeho tréningové znalosti alebo vymýšľanie odpovedí.

## Pochopenie RAG

Nižšie uvedený diagram ilustruje základný koncept: namiesto spoliehania sa iba na tréningové údaje modelu, RAG poskytuje referenčnú knižnicu vašich dokumentov, ktorú model konzultuje pred vygenerovaním každej odpovede.

<img src="../../../translated_images/sk/what-is-rag.1f9005d44b07f2d8.webp" alt="Čo je RAG" width="800"/>

Takto sú jednotlivé časti navzájom prepojené od A po Z. Otázka používateľa prechádza štyrmi fázami — embedding, vyhľadávanie vektorov, skladanie kontextu a generovanie odpovede — pričom každá nadväzuje na predchádzajúcu:

<img src="../../../translated_images/sk/rag-architecture.ccb53b71a6ce407f.webp" alt="Architektúra RAG" width="800"/>

Zvyšok tohto modulu podrobne opisuje každú fázu s kódom, ktorý môžete spustiť a upravovať.

## Predpoklady

- Dokončený Modul 01 (nasadené Azure OpenAI zdroje)  
- Súbor `.env` v koreňovom adresári s Azure povereniami (vytvorený príkazom `azd up` v Module 01)  

> **Poznámka:** Ak ste ešte nedokončili Modul 01, najskôr postupujte podľa jeho inštrukcií na nasadenie.

## Ako to funguje

### Spracovanie dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Keď nahrajete dokument, systém ho parsuje (PDF alebo čistý text), pripojí k nemu metadáta, ako je názov súboru, a potom ho rozdelí na časti — menšie kúsky, ktoré sa pohodlne zmestia do kontextového okna modelu. Tieto časti majú mierne prekrývanie, aby sa nezatratili dôležité informácie na hraniciach.

```java
// Parsuj nahraný súbor a zabaľ ho do dokumentu LangChain4j
Document document = Document.from(content, metadata);

// Rozdeľ na kúsky po 300 tokenoch s prekrytím 30 tokenov
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Nižšie uvedený diagram vizuálne ukazuje, ako to funguje. Všimnite si, že každá časť zdieľa niektoré tokeny so susedmi — 30-tokenové prekrývanie zabezpečuje, že žiadny dôležitý kontext nepadne do priepasti:

<img src="../../../translated_images/sk/document-chunking.a5df1dd1383431ed.webp" alt="Rozdeľovanie dokumentu na časti" width="800"/>

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) a opýtajte sa:  
> - "Ako LangChain4j rozdeľuje dokumenty na časti a prečo je prekrývanie dôležité?"  
> - "Aká je optimálna veľkosť časti pre rôzne typy dokumentov a prečo?"  
> - "Ako riešim dokumenty v rôznych jazykoch alebo so špeciálnym formátovaním?"

### Tvorba embeddingov

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Každá časť sa prevedie na číselnú reprezentáciu nazývanú embedding — v podstate matematický odtlačok, ktorý zachytáva význam textu. Podobný text produkuje podobné embeddingy.

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
  
Nižšie uvedený diagram tried ukazuje, ako sú tieto komponenty LangChain4j prepojené. `OpenAiOfficialEmbeddingModel` premieňa text na vektory, `InMemoryEmbeddingStore` uchováva vektory spolu s originálnymi dátami `TextSegment` a `EmbeddingSearchRequest` ovláda parametre vyhľadávania ako `maxResults` a `minScore`:

<img src="../../../translated_images/sk/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Triedy LangChain4j RAG" width="800"/>

Keď sa embeddingy uložia, podobný obsah sa prirodzene zoskupí vektorovom priestore. Nižšie vizualizácia ukazuje, ako dokumenty o súvisiacich témach skončia blízko seba, čo umožňuje sémantické vyhľadávanie:

<img src="../../../translated_images/sk/vector-embeddings.2ef7bdddac79a327.webp" alt="Priestor vektorových embeddingov" width="800"/>

### Sémantické vyhľadávanie

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Keď položíte otázku, aj vaša otázka sa prevedie na embedding. Systém porovná embedding vašej otázky so všetkými embeddingami častí dokumentov. Nájde kúsky s najpodobnejšími význammi — nielen podľa kľúčových slov, ale skutočnú sémantickú podobnosť.

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
  
Diagram nižšie porovnáva sémantické vyhľadávanie s tradičným vyhľadávaním podľa kľúčových slov. Vyhľadávanie kľúčového slova „vozidlo“ nevyhľadá časť o „autách a nákladiakoch“, ale sémantické vyhľadávanie chápe, že majú rovnaký význam a vráti ju ako relevantný výsledok s vysokým skóre:

<img src="../../../translated_images/sk/semantic-search.6b790f21c86b849d.webp" alt="Sémantické vyhľadávanie" width="800"/>

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) a opýtajte sa:  
> - "Ako funguje vyhľadávanie podobnosti s embeddingami a čo určuje skóre?"  
> - "Aký prah podobnosti by som mal použiť a ako ovplyvňuje výsledky?"  
> - "Ako riešim situácie, keď sa nenájdu žiadne relevantné dokumenty?"

### Generovanie odpovedí

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantnejšie časti sa zostavia do štruktúrovaného promptu, ktorý zahŕňa explicitné pokyny, načítaný kontext a otázku používateľa. Model číta tie konkrétne časti a odpovedá na základe týchto informácií — môže použiť iba to, čo má pred sebou, čím sa predchádza vymýšľaniu odpovedí.

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
  
Diagram nižšie zobrazuje túto skladbu v akcii — najvyššie skórujúce časti z vyhľadávania sú vložené do šablóny promptu a `OpenAiOfficialChatModel` vygeneruje podloženú odpoveď:

<img src="../../../translated_images/sk/context-assembly.7e6dd60c31f95978.webp" alt="Skladanie kontextu" width="800"/>

## Spustenie aplikácie

**Overte nasadenie:**  

Uistite sa, že súbor `.env` existuje v koreňovom adresári s Azure povereniami (vytvorený počas Modulu 01):  
```bash
cat ../.env  # Mali by sa zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Spustite aplikáciu:**  

> **Poznámka:** Ak ste už všetky aplikácie spustili pomocou `./start-all.sh` z Modulu 01, tento modul už beží na porte 8081. Môžete vynechať spúšťacie príkazy nižšie a prejsť priamo na http://localhost:8081.

**Možnosť 1: Použitie Spring Boot Dashboard (Odporúčané pre používateľov VS Code)**

Dev kontajner obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Baru naľavo vo VS Code (ikonka Spring Boot).

Zo Spring Boot Dashboard môžete:  
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore  
- Spustiť/zastaviť aplikácie jediným kliknutím  
- Prezerať logy aplikácií v reálnom čase  
- Monitorovať stav aplikácií  

Jednoducho kliknite na tlačidlo play vedľa „rag“ na spustenie tohto modulu alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
  
Alebo spustite len tento modul:

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
  
Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` a zostavia JAR súbory, ak neexistujú.

> **Poznámka:** Ak chcete radšej manuálne zostaviť všetky moduly pred spustením:  
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
  
Otvorte v prehliadači http://localhost:8081.

**Na zastavenie:**  

**Bash:**  
```bash
./stop.sh  # Iba tento modul
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

*Rozhranie aplikácie RAG - nahrajte dokumenty a pýtajte sa otázky*

### Nahranie dokumentu

Začnite nahraním dokumentu — pre testovanie najlepšie fungujú súbory TXT. Tento adresár obsahuje `sample-document.txt`, ktorý obsahuje informácie o funkciách LangChain4j, implementácii RAG a najlepších postupoch — ideálne na testovanie systému.

Systém spracuje váš dokument, rozdelí ho na časti a vytvorí embeddingy pre každú časť. Toto sa deje automaticky po nahraní.

### Položenie otázok

Teraz položte konkrétne otázky týkajúce sa obsahu dokumentu. Skúste niečo faktické, čo je jasne uvedené v dokumente. Systém vyhľadá relevantné časti, zahrnie ich do promptu a vygeneruje odpoveď.

### Kontrola zdrojových referencií

Všimnite si, že každá odpoveď obsahuje zdrojové referencie so skóre podobnosti. Toto skóre (od 0 do 1) ukazuje, ako relevantná bola každá časť k vašej otázke. Vyššie skóre znamená lepšiu zhoda. To vám umožňuje overiť odpoveď voči zdrojovému materiálu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sk/rag-query-results.6d69fcec5397f355.webp" alt="Výsledky dopytu RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Výsledky dopytu zobrazujú odpoveď so zdrojovými referenciami a skóre relevantnosti*

### Experimentovanie s otázkami

Skúste rôzne typy otázok:  
- Konkrétne fakty: „Aká je hlavná téma?“  
- Porovnania: „Aký je rozdiel medzi X a Y?“  
- Zhrnutia: „Zhrňte kľúčové body o Z“  

Sledujte, ako sa skóre relevantnosti mení podľa toho, ako dobre vaša otázka zodpovedá obsahu dokumentu.

## Kľúčové koncepty

### Stratégia rozdeľovania na časti

Dokumenty sa rozdeľujú na časti po 300 tokenov s 30-tokenovým prekrývaním. Tento kompromis zabezpečuje, že každá časť má dostatok kontextu, aby bola zmysluplná, a zároveň je dostatočne malá na to, aby sa ich do promptu zmestilo viac.

### Skóre podobnosti

Každá načítaná časť má skóre podobnosti medzi 0 a 1, ktoré indikuje, ako veľmi zodpovedá používateľovej otázke. Nižšie uvedený diagram vizualizuje rozsahy skóre a ako ich systém používa na filtrovanie výsledkov:

<img src="../../../translated_images/sk/similarity-scores.b0716aa911abf7f0.webp" alt="Skóre podobnosti" width="800"/>

Skóre sa pohybuje medzi:  
- 0.7-1.0: Vysoko relevantné, presná zhoda  
- 0.5-0.7: Relevatné, dobrý kontext  
- Nižšie ako 0.5: Filtrované, príliš odlišné  

Systém načítava iba časti s hodnotou nad minimálnym prahom, aby zabezpečil kvalitu.

### Ukladanie v pamäti

Tento modul používa ukladanie v pamäti pre jednoduchú implementáciu. Po reštarte aplikácie sa nahrané dokumenty stratia. Produkčné systémy používajú trvalé vektorové databázy ako Qdrant alebo Azure AI Search.

### Správa kontextového okna

Každý model má maximálnu veľkosť kontextového okna. Nemôžete zahrnúť všetky časti veľkého dokumentu. Systém načíta top N najrelevantnejších častí (predvolene 5), aby ostal v limite a zároveň poskytol dostatok kontextu na presné odpovede.

## Kedy je RAG dôležité

RAG nie je vždy správnym prístupom. Nižšie uvedený sprievodca rozhodovaním vám pomôže určiť, kedy RAG prináša pridanú hodnotu oproti jednoduchším prístupom — ako je zahrnutie obsahu priamo do promptu alebo spoliehanie sa na vstavané znalosti modelu:

<img src="../../../translated_images/sk/when-to-use-rag.1016223f6fea26bc.webp" alt="Kedy použiť RAG" width="800"/>

**Použite RAG, keď:**
- Odpovedanie na otázky o vlastníckych dokumentoch
- Informácie sa často menia (zásady, ceny, špecifikácie)
- Presnosť vyžaduje uvedenie zdroja
- Obsah je príliš veľký na to, aby sa zmestil do jedného promptu
- Potrebujete overiteľné, podložené odpovede

**Nepoužívajte RAG, keď:**
- Otázky vyžadujú všeobecné vedomosti, ktoré model už má
- Potrebujete aktuálne údaje v reálnom čase (RAG pracuje s nahratými dokumentmi)
- Obsah je dostatočne malý na to, aby bol priamo zahrnutý v promptoch

## Ďalšie kroky

**Ďalší modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Späť na hlavný](../README.md) | [Ďalší: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, prosím, majte na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu nenesieme zodpovednosť.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->