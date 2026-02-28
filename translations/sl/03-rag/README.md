# Modul 03: RAG (Retrieval-Augmented Generation)

## Kazalo vsebine

- [Video vodnik](../../../03-rag)
- [Kaj se boste naučili](../../../03-rag)
- [Zahteve](../../../03-rag)
- [Razumevanje RAG](../../../03-rag)
  - [Kateri RAG pristop uporablja ta vodič?](../../../03-rag)
- [Kako deluje](../../../03-rag)
  - [Obdelava dokumentov](../../../03-rag)
  - [Ustvarjanje vdelav](../../../03-rag)
  - [Semantično iskanje](../../../03-rag)
  - [Generiranje odgovorov](../../../03-rag)
- [Zagon aplikacije](../../../03-rag)
- [Uporaba aplikacije](../../../03-rag)
  - [Nalaganje dokumenta](../../../03-rag)
  - [Postavljanje vprašanj](../../../03-rag)
  - [Preverjanje virov](../../../03-rag)
  - [Eksperimentiranje z vprašanji](../../../03-rag)
- [Ključni koncepti](../../../03-rag)
  - [Strategija razdeljevanja na koščke](../../../03-rag)
  - [Ocene podobnosti](../../../03-rag)
  - [Shranjevanje v pomnilnik](../../../03-rag)
  - [Upravljanje kontekstnega okna](../../../03-rag)
- [Kdaj je RAG pomemben](../../../03-rag)
- [Naslednji koraki](../../../03-rag)

## Video vodnik

Oglejte si to neposredno predavanje, ki razlaga, kako začeti s tem modulom: [RAG z LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Kaj se boste naučili

V prejšnjih modulih ste se naučili, kako voditi pogovore z AI in učinkovito strukturirati svoje pozive. Toda obstaja temeljna omejitev: jezikovni modeli poznajo le to, kar so se naučili med usposabljanjem. Ne morejo odgovarjati na vprašanja o pravilnikih vašega podjetja, dokumentaciji vašega projekta ali kakršnih koli informacijah, na katerih niso bili usposobljeni.

RAG (Retrieval-Augmented Generation) rešuje ta problem. Namesto da bi modelu poskušali "naučiti" vaših informacij (kar je drago in nepraktično), mu omogočite, da išče po vaših dokumentih. Ko nekdo postavi vprašanje, sistem poišče relevantne informacije in jih vključi v poziv. Model nato odgovori na podlagi tega pridobljenega konteksta.

RAG si predstavljajte kot to, da modelu daste referenčno knjižnico. Ko postavite vprašanje, sistem:

1. **Uporabniški poizvedba** – postavite vprašanje  
2. **Vdelava** – vaše vprašanje pretvori v vektor  
3. **Vektorsko iskanje** – najde podobne koščke dokumentov  
4. **Sestavljanje konteksta** – doda relevantne koščke v poziv  
5. **Odgovor** – LLM generira odgovor na podlagi konteksta  

To utemelji odgovore modela na vaših dejanskih podatkih, namesto da bi se zanašal na znanje usposabljanja ali si izmislil odgovore.

## Zahteve

- Dokončan [Modul 00 - Hiter začetek](../00-quick-start/README.md) (za zgled Easy RAG, omenjen zgoraj)  
- Dokončan [Modul 01 - Uvod](../01-introduction/README.md) (deplojani Azure OpenAI viri, vključno z modelom za vdelave `text-embedding-3-small`)  
- Datoteka `.env` v vrhnji mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)  

> **Opomba:** Če Modula 01 še niste končali, najprej sledite navodilom za namestitev tam. Ukaz `azd up` deploja tako GPT klepetalni model kot tudi model za vdelave, ki ga uporablja ta modul.

## Razumevanje RAG

Spodnja shema prikazuje osnovni koncept: namesto da se zanaša le na podatke usposabljanja modela, mu RAG omogoča dostop do referenčne knjižnice vaših dokumentov, ki jo pregleduje pred generiranjem vsakega odgovora.

<img src="../../../translated_images/sl/what-is-rag.1f9005d44b07f2d8.webp" alt="Kaj je RAG" width="800"/>

*Ta diagram prikazuje razliko med standardnim LLM-jem (ki špekulira na podlagi podatkov za usposabljanje) in LLM-jem z nadgradnjo RAG (ki najprej povpraša vaše dokumente).*

Tako se deli povežejo od začetka do konca. Vprašanje uporabnika potuje skozi štiri faze — vdelava, vektorsko iskanje, sestavljanje konteksta in generiranje odgovora — vsaka zgradi na prejšnji:

<img src="../../../translated_images/sl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arhitektura" width="800"/>

*Ta diagram prikazuje celotni RAG potek — uporabniška poizvedba potuje skozi vdelavo, vektorsko iskanje, sestavljanje konteksta in generiranje odgovora.*

Preostali del tega modula podrobno opisuje posamezne faze, s kodo, ki jo lahko zaženete in prilagodite.

### Kateri RAG pristop uporablja ta vodič?

LangChain4j ponuja tri načine implementacije RAG, vsak na različni ravni abstrakcije. Spodnja shema jih primerja bok ob boku:

<img src="../../../translated_images/sl/rag-approaches.5b97fdcc626f1447.webp" alt="Trije RAG pristopi v LangChain4j" width="800"/>

*Ta diagram primerja tri RAG pristope LangChain4j — Easy, Native in Advanced — prikazuje njihove ključne komponente in kdaj jih uporabiti.*

| Pristop | Kaj naredi | Kompromis |
|---|---|---|
| **Easy RAG** | Vse samodejno poveže preko `AiServices` in `ContentRetriever`. Označite vmesnik, pripnete retriever, LangChain4j pa zadaj upravlja vdelave, iskanje in sestavljanje poziva. | Minimalno kode, vendar ne vidite, kaj se dogaja v vsaki fazi. |
| **Native RAG** | Kličete model za vdelave, iščete v skladišču, sestavljate poziv in sami generirate odgovor — korak za korakom. | Več kode, a vsaka faza je vidna in jo lahko spremenite. |
| **Advanced RAG** | Uporablja okvir `RetrievalAugmentor` s pluggable transformatorji poizvedb, usmerjevalniki, ponovnimi razvrščevalci in injektorji vsebine za proizvodne cevovode. | Največja prilagodljivost, a precejšnja kompleksnost. |

**Ta vodič uporablja Native pristop.** Vsak korak RAG pipelina — vdelava poizvedbe, iskanje v vektorskem skladišču, sestavljanje konteksta in generiranje odgovora — je izrecno napisan v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). To je namenoma: kot učni vir je pomembneje, da vidite in razumete vsako fazo, kot da je koda minimalna. Ko se boste počutili udobno z delom sestavnih delov, se lahko premaknete na Easy RAG za hitre prototipe ali Advanced RAG za proizvodne sisteme.

> **💡 Ste že videli Easy RAG v akciji?** Modul [Hitri začetek](../00-quick-start/README.md) vključuje primer vprašanj in odgovorov z dokumenti ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), ki uporablja Easy RAG pristop — LangChain4j samodejno upravlja vdelave, iskanje in sestavljanje poziva. Ta modul naredi korak naprej tako, da razkrije ta pipeline, da vidite in nadzirate vsako fazo sami.

<img src="../../../translated_images/sl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ta diagram prikazuje Easy RAG pipeline iz `SimpleReaderDemo.java`. Primerjajte ga z Native pristopom, uporabljenim v tem modulu: Easy RAG skriva vdelave, pridobivanje in sestavljanje poziva za `AiServices` in `ContentRetriever` — naložite dokument, pripnete retriever in dobite odgovore. Native pristop tega modula odpre pipeline, da lahko sami kličete vsako fazo (embed, search, sestavljanje konteksta, generiranje), s polnim vpogledom in nadzorom.*

## Kako deluje

RAG pipeline v tem modulu je razdeljen na štiri faze, ki tečejo zaporedno vsakič, ko uporabnik postavi vprašanje. Najprej se naloženi dokument **analizira in razdeli na koščke**, ki so upravljivi. Nato se ti koščki pretvorijo v **vektorske vdelave** in shranijo, da se lahko matematično primerjajo. Ko pride poizvedba, sistem izvede **semantično iskanje**, da najde najbolj relevantne koščke, in jih na koncu poda kot kontekst LLM-ju za **generiranje odgovora**. Spodnji razdelki vas peljejo skozi vsako fazo z dejansko kodo in diagrami. Poglejmo prvi korak.

### Obdelava dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Ko naložite dokument, ga sistem analizira (PDF ali navaden tekst), pripne metapodatke, kot je ime datoteke, nato pa ga razdeli na koščke — manjše dele, ki udobno sedejo v kontekstno okno modela. Ti koščki se rahlo prekrivajo, da ne izgubite konteksta na mejah.

```java
// Analizirajte naloženo datoteko in jo zavijte v dokument LangChain4j
Document document = Document.from(content, metadata);

// Razdelite na kose po 300 žetonov z 30-žetonskim prekrivanjem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Spodnja shema prikazuje, kako to deluje vizualno. Opazite, da ima vsak košček nekaj tokenov v skupni rabi s sosedi — 30-tokenno prekrivanje zagotavlja, da noben pomemben kontekst ne zaide mimo:

<img src="../../../translated_images/sl/document-chunking.a5df1dd1383431ed.webp" alt="Razdeljevanje dokumenta na koščke" width="800"/>

*Ta diagram prikazuje dokument, razdeljen na 300-tokenovne koščke s 30-tokennim prekrivanjem, kar ohranja kontekst na mejah koščkov.*

> **🤖 Poskusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) in vprašajte:  
> - "Kako LangChain4j razdeli dokumente na koščke in zakaj je prekrivanje pomembno?"  
> - "Kakšna je optimalna velikost koščka za različne vrste dokumentov in zakaj?"  
> - "Kako obravnavam dokumente v več jezikih ali s posebno obliko?"

### Ustvarjanje vdelav

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Vsak košček se pretvori v numerično predstavitev, imenovano vdelava — v bistvu pretvornik pomena v številke. Model za vdelave ni "pameten" kot klepetalni model; ne more slediti navodilom, sklepati ali odgovarjati na vprašanja. Kar lahko naredi, je preslikati besedilo v matematični prostor, kjer podobni pomeni pristanejo blizu — "avto" blizu "vozilo," "politika vračil" blizu "vrnitev denarja." Klepetalni model je kot oseba, s katero se pogovarjate; model za vdelave je vrhunski arhivski sistem.

<img src="../../../translated_images/sl/embedding-model-concept.90760790c336a705.webp" alt="Koncept modela za vdelave" width="800"/>

*Ta diagram prikazuje, kako model za vdelave pretvori besedilo v numerične vektorje, pri čemer podobni pomeni — kot "avto" in "vozilo" — pristanejo blizu drug drugega v vektorskem prostoru.*

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
  
Spodnja diagram razreda prikazuje dva ločena toka v RAG pipeline in LangChain4j razrede, ki ju implementirajo. **Tok vnosa** (teče enkrat ob nalaganju) razdeli dokument, vgradi koščke in jih shrani prek `.addAll()`. **Tok poizvedbe** (teče vsakič, ko uporabnik vpraša) vgradi vprašanje, išče v skladišču prek `.search()` in prenese ujemajoči se kontekst klepetalnemu modelu. Oba toka se srečata na skupnem vmesniku `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG razredi" width="800"/>

*Ta diagram prikazuje dva toka v RAG pipeline — vnos in poizvedbo — in kako se povežeta prek skupnega EmbeddingStore.*

Ko so vdelave shranjene, se podoben vsebinski material naravno združi skupaj v vektorski prostor. Vizualizacija spodaj prikazuje, kako dokumenti o sorodnih temah končajo blizu drug drugega, kar omogoča semantično iskanje:

<img src="../../../translated_images/sl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorski prostor vdelav" width="800"/>

*Ta vizualizacija prikazuje, kako sorodni dokumenti tvorijo gruče v 3D vektorskem prostoru, s temami, kot so Tehnični dokumenti, Poslovna pravila in Pogosta vprašanja.*

Ko uporabnik išče, sistem sledi štirim korakom: enkrat vgradi dokumente, za vsako iskanje vgradi poizvedbo, primerja vektor poizvedbe z vsemi shranjenimi vektorji z uporabo kosinusne podobnosti in vrne top-K najvišje ocenjenih koščkov. Diagram spodaj vodi skozi vsak korak in vpletene LangChain4j razrede:

<img src="../../../translated_images/sl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Koraki iskanja z vdelavami" width="800"/>

*Ta diagram prikazuje štiri korake procesa iskanja z vdelavami: vdelaj dokumente, vdelaj poizvedbo, primerjaj vektorje s kosinusno podobnostjo in vrni top-K rezultate.*

### Semantično iskanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ko postavite vprašanje, postane tudi vaše vprašanje vdelava. Sistem primerja vdelavo vašega vprašanja z vdelavami vseh koščkov dokumentov. Najde koščke z najbolj podobnimi pomeni – ne le ujemajoče ključne besede, temveč dejansko semantično podobnost.

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
  
Spodnja shema primerja semantično iskanje s tradicionalnim iskanjem po ključnih besedah. Iskanje po ključni besedi "vozilo" ne najde koščka o "avtih in tovornjakih", medtem ko semantično iskanje razume, da pomenita isto in ga vrne kot najvišje ocenjenega:

<img src="../../../translated_images/sl/semantic-search.6b790f21c86b849d.webp" alt="Semantično iskanje" width="800"/>

*Ta diagram primerja iskanje po ključnih besedah s semantičnim iskanjem, ki pridobi konceptualno povezano vsebino tudi, kadar se ključne besede razlikujejo.*

Pod pokrovom se podobnost meri s kosinusno podobnostjo — v bistvu sprašuje "ali ti dve puščici kažeta v isto smer?" Dva koščka lahko uporabita popolnoma različne besede, a če pomenita isto, vektorja kažeta v isto smer in ocenita blizu 1.0:

<img src="../../../translated_images/sl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinusna podobnost" width="800"/>

*Ta diagram ilustrira kosinusno podobnost kot kot med vektorji vdelav — bolj usklajeni vektorji dobijo oceno bližje 1.0, kar pomeni večjo semantično podobnost.*
> **🤖 Preizkusi s pogovorom [GitHub Copilot](https://github.com/features/copilot):** Odpri [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) in vprašaj:
> - "Kako deluje iskanje podobnosti z vdelavami in kaj določa oceno?"
> - "Kateri prag podobnosti naj uporabim in kako to vpliva na rezultate?"
> - "Kako ravnam v primerih, ko ni najdenih relevantnih dokumentov?"

### Generiranje odgovorov

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbolj relevantni koščki so združeni v strukturirano pozivno sporočilo, ki vključuje eksplicitna navodila, pridobljeni kontekst in vprašanje uporabnika. Model prebere te specifične koščke in odgovori na podlagi teh informacij — lahko uporabi samo tisto, kar je pred njim, kar preprečuje halucinacije.

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

Spodnja shema prikazuje to sestavljanje v praksi — koščki z najvišjo oceno iz koraka iskanja se vstavijo v predlogo poziva, `OpenAiOfficialChatModel` pa ustvari utemeljen odgovor:

<img src="../../../translated_images/sl/context-assembly.7e6dd60c31f95978.webp" alt="Sestavljanje konteksta" width="800"/>

*Ta shema prikazuje, kako so koščki z najvišjo oceno sestavljeni v strukturiran poziv, kar modelu omogoča ustvarjanje utemeljenega odgovora iz vaših podatkov.*

## Zaženi aplikacijo

**Preveri namestitev:**

Prepričaj se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjeno med Modulom 01):

**Bash:**
```bash
cat ../.env  # Naj prikazuje AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Prikazati mora AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženi aplikacijo:**

> **Opomba:** Če si že zagnal vse aplikacije z `./start-all.sh` iz Modula 01, ta modul že teče na vratih 8081. Lahko preskočiš spodnje ukaze za zagon in greš neposredno na http://localhost:8081.

**Možnost 1: Uporaba Spring Boot Controlne Plošče (priporočeno za uporabnike VS Code)**

Razvojni kontejner vključuje razširitev Spring Boot Dashboard, ki ponuja vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdeš jo lahko v vrstici aktivnosti na levi strani VS Code (ikona Spring Boot).

Iz Spring Boot kontrolne plošče lahko:
- vidiš vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- z enim klikom zaženeš/ustaviš aplikacije
- gledaš dnevnike aplikacij v realnem času
- spremljaš stanje aplikacije

Preprosto klikni gumb za predvajanje poleg "rag" za zagon tega modula ali pa zaženi vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Controlna Plošča" width="400"/>

*Ta posnetek zaslona prikazuje Spring Boot kontrolno ploščo v VS Code, kjer lahko vizualno zaženeš, ustaviš in spremljaš aplikacije.*

**Možnost 2: Uporaba ukaznih skript**

Zaženi vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenske mape
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenske mape
.\start-all.ps1
```

Ali zaženi samo ta modul:

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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske datoteke `.env` in zgradita JAR-je, če ne obstajajo.

> **Opomba:** Če želiš vse module ročno zgraditi pred zagonom:
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

Odpri http://localhost:8081 v svojem brskalniku.

**Za ustavitev:**

**Bash:**
```bash
./stop.sh  # Samo ta modul
# Ali
cd .. && ./stop-all.sh  # Vsi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ta modul
# Ali
cd ..; .\stop-all.ps1  # Vsi moduli
```

## Uporaba aplikacije

Aplikacija ponuja spletni vmesnik za nalaganje dokumentov in postavljanje vprašanj.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sl/rag-homepage.d90eb5ce1b3caa94.webp" alt="Vmesnik RAG aplikacije" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ta posnetek zaslona prikazuje vmesnik RAG aplikacije, kjer naložiš dokumente in postavljaš vprašanja.*

### Naloži dokument

Začni z nalaganjem dokumenta - za testiranje najbolje delujejo TXT datoteke. V tem imeniku je priložen `sample-document.txt`, ki vsebuje informacije o funkcijah LangChain4j, implementaciji RAG in najboljših praksah - popoln za testiranje sistema.

Sistem obdela tvoj dokument, ga razdeli na koščke in za vsak košček ustvari vdelave. To se zgodi samodejno ob nalaganju.

### Postavi vprašanja

Zdaj postavi specifična vprašanja o vsebini dokumenta. Poskusi nekaj faktografskega, kar je jasno navedeno v dokumentu. Sistem poišče relevantne koščke, jih vključi v poziv in ustvari odgovor.

### Preveri vire

Opazil boš, da vsak odgovor vsebuje sklice na vire skupaj z ocenami podobnosti. Te ocene (od 0 do 1) kažejo, kako relevantni so bili posamezni koščki za tvoje vprašanje. Višje ocene pomenijo boljše ujemanje. Tako lahko preveriš odgovor glede na izvorni material.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sl/rag-query-results.6d69fcec5397f355.webp" alt="Rezultati poizvedbe RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ta posnetek zaslona prikazuje rezultate poizvedbe z generiranim odgovorom, referencami na vire in ocenami relevantnosti za vsak pridobljeni košček.*

### Preizkušaj različna vprašanja

Preizkusi različne vrste vprašanj:
- Specifični podatki: "Kakšna je glavna tema?"
- Primerjave: "Kakšna je razlika med X in Y?"
- Povzetki: "Povzemi ključne točke o Z"

Opazuj, kako se ocene relevantnosti spreminjajo glede na to, kako dobro tvoje vprašanje sovpada z vsebino dokumenta.

## Ključni pojmi

### Strategija deljenja na koščke

Dokumenti so razdeljeni na koščke po 300 žetonov z 30 žetoni prekrivanja. Ta uravnoteženost zagotavlja, da ima vsak košček dovolj konteksta za smiselnost, hkrati pa ostanejo dovolj majhni, da lahko v pozivu uporabimo več koščkov.

### Ocene podobnosti

Vsak pridobljeni košček ima oceno podobnosti med 0 in 1, ki nakazuje, kako zelo se ujema z uporabnikovim vprašanjem. Spodnja shema prikazuje obseg ocen in kako jih sistem uporablja za filtriranje rezultatov:

<img src="../../../translated_images/sl/similarity-scores.b0716aa911abf7f0.webp" alt="Ocene podobnosti" width="800"/>

*Ta shema prikazuje obsege ocen od 0 do 1, s minimalnim pragom 0,5, ki odfiltrira nerelevantne koščke.*

Ocene se gibljejo med 0 in 1:
- 0.7-1.0: Zelo relevantno, točno ujemanje
- 0.5-0.7: Relevantno, dober kontekst
- Pod 0.5: Filtrirano, preveč različni

Sistem pridobiva samo koščke nad minimalnim pragom, da zagotovi kakovost.

Vdelave delujejo dobro, ko se pomen čisto združi, vendar imajo slepe točke. Spodnja shema prikazuje pogoste načine nepopolnosti — koščki so preveliki in proizvajajo nejasne vektorje, premajhni koščki nimajo konteksta, dvoumni izrazi kažejo na več grozdov, iskanje popolnih ujemanj (ID-ji, številke delov) pa sploh ne deluje z vdelavami:

<img src="../../../translated_images/sl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Načini neuspeha vdelav" width="800"/>

*Ta shema prikazuje pogoste načine neuspeha vdelav: preveliki koščki, premajhni koščki, dvoumni izrazi, ki kažejo na več grozdov, in iskanja popolnih ujemanj, kot so ID-ji.*

### Shranjevanje v pomnilniku

Ta modul uporablja shranjevanje v pomnilniku zaradi preprostosti. Ko ponovno zaženeš aplikacijo, naloženi dokumenti se izgubijo. Produkcijski sistemi uporabljajo trajne vektorske baze, kot so Qdrant ali Azure AI Search.

### Upravljanje kontekstnega okna

Vsak model ima največjo velikost kontekstnega okna. Ne moreš vključiti vseh koščkov iz velikega dokumenta. Sistem pridobi najrelevantnejše N koščkov (privzeto 5), da ostane znotraj omejitev in hkrati zagotovi dovolj konteksta za točne odgovore.

## Kdaj je RAG pomemben

RAG ni vedno prava izbira. Vodnik za odločanje spodaj ti pomaga ugotoviti, kdaj RAG prinaša vrednost in kdaj so zadostni enostavnejši pristopi — kot je vključevanje vsebine neposredno v poziv ali zanašanje na vgrajeno znanje modela:

<img src="../../../translated_images/sl/when-to-use-rag.1016223f6fea26bc.webp" alt="Kdaj uporabiti RAG" width="800"/>

*Ta shema prikazuje vodnik za odločanje, kdaj RAG prinaša vrednost in kdaj so enostavnejši pristopi zadostni.*

**Uporabi RAG, ko:**
- odgovarjaš na vprašanja o lastniških dokumentih
- se informacije pogosto spreminjajo (politike, cene, specifikacije)
- je potrebna natančnost z navedbo vira
- je vsebina prevelika, da bi jo lahko vključili v en poziv
- potrebuješ preverljive, utemeljene odgovore

**Ne uporabljaš RAG, ko:**
- vprašanja zahtevajo splošno znanje, ki ga model že ima
- so potrebni podatki v realnem času (RAG deluje na naloženih dokumentih)
- je vsebina dovolj majhna, da jo lahko vključiš neposredno v pozive

## Naslednji koraki

**Naslednji modul:** [04-tools - Agent AI z orodji](../04-tools/README.md)

---

**Navigacija:** [← Prejšnji: Modul 02 - Oblikovanje pozivov](../02-prompt-engineering/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 04 - Orodja →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za točnost, vas prosimo, da upoštevate, da avtomatski prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v izvirnem jeziku je treba šteti za avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Za kakršne koli nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda, ne prevzemamo odgovornosti.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->