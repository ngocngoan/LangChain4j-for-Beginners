# Modul 03: RAG (Generiranje z iskanjem informacij)

## Kazalo vsebine

- [Video vodnik](../../../03-rag)
- [Kaj se boste naučili](../../../03-rag)
- [Predpogoji](../../../03-rag)
- [Razumevanje RAG](../../../03-rag)
  - [Kateri RAG pristop uporablja ta vodič?](../../../03-rag)
- [Kako deluje](../../../03-rag)
  - [Obdelava dokumentov](../../../03-rag)
  - [Ustvarjanje vdelav](../../../03-rag)
  - [Semantično iskanje](../../../03-rag)
  - [Generiranje odgovorov](../../../03-rag)
- [Zagon aplikacije](../../../03-rag)
- [Uporaba aplikacije](../../../03-rag)
  - [Naloži dokument](../../../03-rag)
  - [Postavite vprašanja](../../../03-rag)
  - [Preverite vire](../../../03-rag)
  - [Eksperimentirajte z vprašanji](../../../03-rag)
- [Ključni koncepti](../../../03-rag)
  - [Strategija razdelitve](../../../03-rag)
  - [Ocene podobnosti](../../../03-rag)
  - [Shranjevanje v pomnilniku](../../../03-rag)
  - [Upravljanje kontekstnega okna](../../../03-rag)
- [Kdaj je RAG pomemben](../../../03-rag)
- [Naslednji koraki](../../../03-rag)

## Video vodnik

Oglejte si to v živo predavanje, ki pojasnjuje, kako začeti z modulom:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG z LangChain4j - v živo" width="800"/></a>

## Kaj se boste naučili

V prejšnjih modulih ste se naučili, kako voditi pogovore z AI in učinkovito strukturirati vaše pozive. Toda obstaja temeljna omejitev: jezikovni modeli poznajo le tisto, kar so se naučili med usposabljanjem. Ne morejo odgovoriti na vprašanja o politikah vašega podjetja, dokumentaciji vaših projektov ali kakršnih koli informacijah, na katerih niso bili usposobljeni.

RAG (Generiranje z iskanjem informacij) rešuje ta problem. Namesto da bi modelu skušali naučiti vaše informacije (kar je drago in nepraktično), mu daste možnost, da poišče po vaših dokumentih. Ko nekdo postavi vprašanje, sistem najde ustrezne informacije in jih vključi v poziv. Model nato odgovori na podlagi tega pridobljenega konteksta.

Pomislite na RAG kot na to, da modelu daste referenčno knjižnico. Ko postavite vprašanje, sistem:

1. **Uporabniški poizvedba** - Postavite vprašanje  
2. **Vdelava** - Vaše vprašanje pretvori v vektor  
3. **Iskanje po vektorjih** - Najde podobne dele dokumentov  
4. **Sestavljanje konteksta** - Doda ustrezne dele v poziv  
5. **Odgovor** - LLM generira odgovor na podlagi konteksta

To zakorenini odzive modela v dejanske podatke, namesto da bi se zanašal na svoje znanje iz usposabljanja ali izmišljeval odgovore.

## Predpogoji

- Zaključen [Modul 00 – Hiter začetek](../00-quick-start/README.md) (za primer Easy RAG, omenjen zgoraj)  
- Zaključen [Modul 01 – Uvod](../01-introduction/README.md) (uvrščeni Azure OpenAI viri, vključno z vdelavnim modelom `text-embedding-3-small`)  
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z ukazom `azd up` v Modulu 01)

> **Opomba:** Če niste zaključili Modula 01, sledite najprej navodilom za uvajanje tam. Ukaz `azd up` namesti tako model za GPT klepet kot tudi vdelavni model, ki ga uporablja ta modul.

## Razumevanje RAG

Spodnja shema prikazuje osnovni koncept: namesto da bi se zanašal samo na podatke iz usposabljanja, RAG modelu ponudi referenčno knjižnico vaših dokumentov, ki jih lahko pregleda pred generiranjem vsakega odgovora.

<img src="../../../translated_images/sl/what-is-rag.1f9005d44b07f2d8.webp" alt="Kaj je RAG" width="800"/>

*Ta shema prikazuje razliko med standardnim LLM (ki ugiba na podlagi podatkov iz usposabljanja) in RAG-podprtimi LLM (ki najprej pregledajo vaše dokumente).*

Tako se posamezni deli povežejo v celoto. Uporabnikovo vprašanje poteka skozi štiri faze — vdelava, iskanje po vektorjih, sestavljanje konteksta in generiranje odgovora — pri čemer se vsaka faza gradi na prejšnji:

<img src="../../../translated_images/sl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektura" width="800"/>

*Ta shema prikazuje celoten RAG proces — uporabnikova poizvedba poteka skozi vdelavo, iskanje po vektorjih, sestavljanje konteksta in generiranje odgovorov.*

Preostanek modula podrobno obravnava vsako fazo, s kodo, ki jo lahko zaženete in spreminjate.

### Kateri RAG pristop uporablja ta vodič?

LangChain4j ponuja tri načine za implementacijo RAG, vsak z drugačno stopnjo abstrakcije. Spodnja shema jih primerja vzporedno:

<img src="../../../translated_images/sl/rag-approaches.5b97fdcc626f1447.webp" alt="Trije RAG pristopi v LangChain4j" width="800"/>

*Ta shema primerja tri LangChain4j RAG pristope — Easy, Native in Advanced — prikazuje njihove ključne komponente in kdaj jih uporabiti.*

| Pristop | Kaj počne | Kompromis |
|---|---|---|
| **Easy RAG** | Avtomatsko poveže vse preko `AiServices` in `ContentRetriever`. Označite vmesnik, priložite iskalnik in LangChain4j samodejno upravlja z vdelavo, iskanjem in sestavljanjem poziva. | Minimalna količina kode, a ne vidite podrobnosti vsake faze. |
| **Native RAG** | Sam kličete vdelavni model, iščete v skladišču, gradite poziv in generirate odgovor — korak za korakom eksplicitno. | Več kode, a vsaka faza je vidna in spremenljiva. |
| **Advanced RAG** | Uporablja `RetrievalAugmentor` okvir s priključnimi transformatorji poizvedb, usmerjevalniki, ponovno razvrstilniki in vbrizgavanjem vsebine za produkcijske sisteme. | Največja prilagodljivost, a precej večja kompleksnost. |

**Ta vodič uporablja Native pristop.** Vsak korak v RAG procesu — vdelava poizvedbe, iskanje po vektorskem skladišču, sestavljanje konteksta in generiranje odgovora — je jasno napisan v [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). To je namerno: kot učni material je pomembneje, da vidite in razumete vsak korak, kot da bi bila koda čim bolj kratka. Ko boste udobno razumeli povezavo posameznih delov, lahko nadaljujete na Easy RAG za hitre prototipe ali Advanced RAG za produkcijske sisteme.

> **💡 Že videli Easy RAG v akciji?** Modul [Hiter začetek](../00-quick-start/README.md) vključuje primer Dokument Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), ki uporablja Easy RAG pristop — LangChain4j samodejno upravlja vdelavo, iskanje in sestavljanje poziva. Ta modul naredi naslednji korak z razbijanjem tega procesa, da lahko vidite in nadzorujete vsak korak sami.

<img src="../../../translated_images/sl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG potek - LangChain4j" width="800"/>

*Ta shema prikazuje Easy RAG potek iz `SimpleReaderDemo.java`. Primerjajte s Native pristopom, ki se uporablja v tem modulu: Easy RAG skriva vdelavo, iskanje in sestavljanje poziva za `AiServices` in `ContentRetriever` — naložite dokument, priložite iskalnik in dobite odgovore. Native pristop v tem modulu razbije ta proces, da kličete vsako fazo (vdelava, iskanje, sestavljanje konteksta, generiranje) sami, kar daje popoln pregled in nadzor.*

## Kako deluje

RAG potek v tem modulu je razdeljen na štiri zaporedne faze, ki se izvajajo vsakič, ko uporabnik postavi vprašanje. Najprej se naloženi dokument **analizira in razdeli na kose**. Ti kosi se nato pretvorijo v **vektorske vdelave** in shranijo, da jih je mogoče matematično primerjati. Ko pride poizvedba, sistem opravi **semantično iskanje** za najdbo najbolj ustreznih kosov, in jih na koncu posreduje kot kontekst LLM-ju za **generiranje odgovora**. Spodnji razdelki skozi vsak korak pojasnjujejo s pravo kodo in diagrami. Začnimo s prvim korakom.

### Obdelava dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Ko naložite dokument, ga sistem analizira (PDF ali navaden tekst), pripne metapodatke, kot je ime datoteke, in ga nato razdeli na kose — manjše dele, ki se lahko udobno prilegajo v kontekstno okno modela. Ti kosi se rahlo prekrivajo, da ne izgubite konteksta na mejah.

```java
// Razčleni naloženo datoteko in jo zavij v LangChain4j dokument
Document document = Document.from(content, metadata);

// Razdeli na 300-token dele z 30-token prekrivanjem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Spodnji diagram prikazuje, kako to deluje vizualno. Opazite, da si vsak kos deli nekaj tokenov s sosedi — 30-token prekrivanje zagotavlja, da ne izgine pomemben kontekst med robovi:

<img src="../../../translated_images/sl/document-chunking.a5df1dd1383431ed.webp" alt="Razdelitev dokumenta na kose" width="800"/>

*Ta diagram prikazuje dokument, razdeljen v 300-token kose s 30-token prekrivanjem, kar ohranja kontekst na mejah kosov.*

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) in vprašajte:  
> - "Kako LangChain4j razdeli dokumente na kose in zakaj je prekrivanje pomembno?"  
> - "Kakšna je optimalna velikost kosa za različne vrste dokumentov in zakaj?"  
> - "Kako ravnam z dokumenti v več jezikih ali s posebno obliko?"

### Ustvarjanje vdelav

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Vsak kos se pretvori v numerično predstavitev, imenovano vdelava — bistveno pretvornik pomena v številke. Vdelavni model ni "inteligenten" kot klepetalni model; ne more slediti navodilom, sklepati ali odgovarjati na vprašanja. Lahko pa besedilo preslika v matematični prostor, kjer so podobni pomeni blizu drug drugega — "avto" blizu "vozilo", "pravila vračila" blizu "vrni mi denar". Pomislite na klepetalni model kot na osebo, s katero se lahko pogovarjate; vdelavni model je izredno dober organizacijski sistem.

<img src="../../../translated_images/sl/embedding-model-concept.90760790c336a705.webp" alt="Koncept vdelavnega modela" width="800"/>

*Ta diagram prikazuje, kako model vdelave pretvori besedilo v numerične vektorje, pri čemer so podobni pomeni — kot "avto" in "vozilo" — blizu drug drugega v vektorskem prostoru.*

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

Spodnji razredni diagram prikazuje dva ločena poteka v RAG procesu in LangChain4j razrede, ki ju izvajajo. **Potesni potek** (izvede se ob nalaganju) deli dokument, vdeluje kose in jih shranjuje preko `.addAll()`. **Poizvedbeni potek** (izvede se vsakič, ko uporabnik vpraša) vdeluje vprašanje, išče v skladišču preko `.search()` in posreduje najdeni kontekst klepetalnemu modelu. Oba poteka se srečata na deljenem vmesniku `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG razredi" width="800"/>

*Ta diagram prikazuje dva poteka v RAG procesu — potek potesnega vnosa in potek poizvedbe — in kako sta povezana prek skupnega EmbeddingStore.*

Ko so vdelave shranjene, se podobna vsebina naravno združuje v vektorskem prostoru. Spodnja vizualizacija prikazuje, kako se dokumenti o sorodnih temah zberejo v bližini, kar omogoča semantično iskanje:

<img src="../../../translated_images/sl/vector-embeddings.2ef7bdddac79a327.webp" alt="Prostor vektorskih vdelav" width="800"/>

*Ta vizualizacija prikazuje, kako sorodni dokumenti tvorijo gruče v 3D vektorskem prostoru, zlasti na področju Tehnična dokumentacija, Poslovna pravila in Pogosta vprašanja.*

Ko uporabnik išče, sistem sledi štirim korakom: ena vdelava dokumentov, vdelava poizvedbe ob vsakem iskanju, primerjava vektorja poizvedbe z vsemi shranjenimi vektorji prek kosinusne podobnosti in vrnitev top-K najvišje ocenjenih kosov. Spodnji diagram ponazarja vsak korak in LangChain4j razrede, ki sodelujejo:

<img src="../../../translated_images/sl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Koraki iskanja po vdelavah" width="800"/>

*Ta diagram prikazuje štiri korake iskanja po vdelavah: vdelava dokumentov, vdelava poizvedbe, primerjava vektorjev s kosinusno podobnostjo in vrnitev top-K rezultatov.*

### Semantično iskanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ko postavite vprašanje, tudi vaše vprašanje postane vdelava. Sistem primerja vdelavo vašega vprašanja z vsemi vdelavami kosov dokumenta. Najde kose z najbolj podobnimi pomeni - ne samo ujemanje ključnih besed, ampak dejansko semantično podobnost.

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

Spodnji diagram primerja semantično iskanje s tradicionalnim iskanjem po ključnih besedah. Iskanje po ključni besedi "vozilo" spregleda kos o "avtih in tovornjakih", a semantično iskanje razume, da pomenijo isto in ga vrne kot visoko ocenjen zadetek:

<img src="../../../translated_images/sl/semantic-search.6b790f21c86b849d.webp" alt="Semantično iskanje" width="800"/>

*Ta diagram primerja iskanje po ključnih besedah s semantičnim iskanjem, ki prikazuje, kako semantično iskanje pridobi konceptualno sorodno vsebino, tudi ko natančne ključne besede niso enake.*

Pod pokrovom se podobnost meri z uporabo kosinusne podobnosti — to je kot bi vprašali "ali ti dve puščici kažeta v isto smer?" Dva kosa lahko uporabita popolnoma različne besede, a če pomenita isto, njuni vektorji kažeta v isto smer in dosegata oceno blizu 1.0:

<img src="../../../translated_images/sl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinusna podobnost" width="800"/>
*Ta diagram prikazuje podobnost kosinusa kot kot med vektorji vgrajevanja — bolj usklajeni vektorji dosegajo oceno bližje 1,0, kar pomeni večjo semantično podobnost.*

> **🤖 Poskusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odpri [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) in vprašaj:
> - "Kako deluje iskanje podobnosti z vgradnjami in kaj določa oceno?"
> - "Katero mejno vrednost podobnosti naj uporabim in kako to vpliva na rezultate?"
> - "Kako ravnam v primerih, ko ni najdenih ustreznih dokumentov?"

### Generiranje odgovora

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbolj relevantni deli so sestavljeni v strukturiran poziv, ki vključuje eksplicitna navodila, pridobljeni kontekst in uporabnikovo vprašanje. Model prebere te specifične dele in odgovori na podlagi teh informacij — lahko uporabi le, kar je pred njim, kar preprečuje halucinacije.

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

Spodnji diagram prikazuje to sestavljanje v akciji — najvišje ocenjeni deli iz koraka iskanja so vstavljeni v predlogo poziva, `OpenAiOfficialChatModel` pa generira utemeljen odgovor:

<img src="../../../translated_images/sl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ta diagram prikazuje, kako so najvišje ocenjeni deli sestavljeni v strukturiran poziv, kar modelu omogoča, da generira utemeljen odgovor iz vaših podatkov.*

## Zaženi aplikacijo

**Preveri namestitev:**

Prepričaj se, da datoteka `.env` obstaja v korenski mapi z Azure poverilnicami (ustvarjena med Modulom 01):

**Bash:**
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moral bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženi aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz Modula 01, ta modul že teče na vratih 8081. Lahko preskočite spodnje ukaze za zagon in greste neposredno na http://localhost:8081.

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Razvojno okolje vsebuje razširitev Spring Boot Dashboard, ki zagotavlja vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo lahko v vrstici dejavnosti na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot nadzorne plošče lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Z enim klikom zaženete/ustavite aplikacije
- V realnem času spremljate dnevnike aplikacij
- Nadzorujete stanje aplikacije

Preprosto kliknite gumb za predvajanje zraven "rag" za zagon tega modula ali zaženite vse module hkrati.

<img src="../../../translated_images/sl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ta posnetek zaslona prikazuje Spring Boot nadzorno ploščo v VS Code, kjer lahko vizualno zaženete, ustavite in spremljate aplikacije.*

**Možnost 2: Uporaba shell skript**

Zaženi vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenskega imenika
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

Obe skripti samodejno naložita spremenljivke okolja iz korenske datoteke `.env` in sestavita JAR-je, če še ne obstajajo.

> **Opomba:** Če želite ročno sestaviti vse module pred zagonom:
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

Odprite http://localhost:8081 v vašem brskalniku.

**Za zaustavitev:**

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

Aplikacija omogoča spletni vmesnik za nalaganje dokumentov in zastavljanje vprašanj.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ta posnetek zaslona prikazuje vmesnik RAG aplikacije, kjer nalagate dokumente in postavljate vprašanja.*

### Naloži dokument

Začnite z nalaganjem dokumenta - za testiranje so najbolj primerni TXT fajli. V tem imeniku je na voljo `sample-document.txt`, ki vsebuje informacije o funkcijah LangChain4j, implementaciji RAG in najboljših praksah - popolno za testiranje sistema.

Sistem obdela vaš dokument, ga razdeli na dele in ustvari vgradnje za vsak del. To se zgodi samodejno ob nalaganju.

### Postavljajte vprašanja

Zdaj postavite specifična vprašanja o vsebini dokumenta. Poskusite nekaj dejanskega, kar je jasno zapisano v dokumentu. Sistem išče relevante dele, jih vključi v poziv in generira odgovor.

### Preverite vire

Opazite, da vsak odgovor vključuje reference do virov s podobnostnimi ocenami. Te ocene (od 0 do 1) kažejo, kako relevanten je bil vsak del za vaše vprašanje. Višje ocene pomenijo boljše ujemanje. To vam omogoča, da preverite odgovor glede na izvorno gradivo.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ta posnetek zaslona prikazuje rezultate poizvedbe z generiranim odgovorom, referencami virov in ocenami relevantnosti za vsak pridobljeni del.*

### Eksperimentirajte z vprašanji

Poskusite različne tipe vprašanj:
- Specifična dejstva: "Kakšna je glavna tema?"
- Primerjave: "Kakšna je razlika med X in Y?"
- Povzetki: "Povzemite ključne točke o Z"

Opazujte, kako se ocene relevantnosti spreminjajo glede na to, kako dobro se vaše vprašanje ujema z vsebino dokumenta.

## Ključni koncepti

### Strategija deljenja na dele

Dokumenti so razdeljeni na dele po 300 znakov z 30 znaki prekrivanja. Ta ravnovesje zagotavlja, da ima vsak del dovolj konteksta, da je smiseln, hkrati pa je dovolj majhen, da lahko več delov vključimo v poziv.

### Ocene podobnosti

Vsak pridobljeni del ima oceno podobnosti med 0 in 1, ki kaže, kako tesno se ujema z uporabnikovim vprašanjem. Spodnji diagram prikazuje razpon ocen in kako sistem uporablja te vrednosti za filtriranje rezultatov:

<img src="../../../translated_images/sl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ta diagram prikazuje razpon ocen od 0 do 1, z minimalno mejo 0,5, ki izloči nerelevantne dele.*

Ocene segajo od 0 do 1:
- 0,7-1,0: Zelo relevantno, natančno ujemanje
- 0,5-0,7: Relevantno, dober kontekst
- Pod 0,5: Izločeno, preveč različni

Sistem pridobi le dele nad minimalno mejo za zagotavljanje kakovosti.

Vgradnje dobro delujejo, ko se pomeni jasno razvrstijo, vendar imajo slepe točke. Spodnji diagram prikazuje pogoste načine neuspeha — deli, ki so preveliki, ustvarjajo nejasne vektorje, deli, ki so premajhni, nimajo konteksta, dvoumni izrazi kažejo na več skupin, in natančna iskanja (ID-ji, številke delov) sploh ne delujejo z vgradnjami:

<img src="../../../translated_images/sl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ta diagram prikazuje pogoste načine neuspeha vgradnje: deli, ki so preveliki, deli, ki so premajhni, dvoumni izrazi, ki kažejo na več skupin, in natančna iskanja, kot so ID-ji.*

### Shramba v pomnilniku

Ta modul uporablja pomnilniško shrambo zaradi preprostosti. Ko aplikacijo znova zaženete, so naloženi dokumenti izgubljeni. Produkcijski sistemi uporabljajo trajne vektorske podatkovne zbirke, kot so Qdrant ali Azure AI Search.

### Upravljanje kontekstnega okna

Vsak model ima maksimalno kontekstno okno. Ne morete vključiti vseh delov velikega dokumenta. Sistem pridobi top N najbolj relevantnih delov (privzeto 5), da ostane znotraj omejitev in hkrati zagotovi dovolj konteksta za natančne odgovore.

## Kdaj je RAG pomemben

RAG ni vedno pravi pristop. Spodnji vodič za odločanje vam pomaga ugotoviti, kdaj RAG doda vrednost in kdaj zadostujejo preprostejši pristopi — kot je vključitev vsebine neposredno v poziv ali zanašanje na vgrajeno znanje modela:

<img src="../../../translated_images/sl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ta diagram prikazuje vodič za odločanje, kdaj RAG doda vrednost in kdaj so preprostejši pristopi zadostni.*

**Uporabite RAG, kadar:**
- Odgovarjate na vprašanja o lastniških dokumentih
- Informacije se pogosto spreminjajo (politike, cene, specifikacije)
- Natančnost zahteva navedbo vira
- Vsebina je prevelika, da bi jo vključili v en poziv
- Potrebujete preverljive, utemeljene odgovore

**Ne uporabljajte RAG, kadar:**
- Vprašanja zahtevajo splošno znanje, ki ga model že ima
- Potrebni so podatki v realnem času (RAG deluje na naloženih dokumentih)
- Vsebina je dovolj majhna, da jo lahko vključite neposredno v pozive

## Naslednji koraki

**Naslednji modul:** [04-tools - AI agenti z orodji](../04-tools/README.md)

---

**Navigacija:** [← Prejšnji: Modul 02 - Inženiring pozivov](../02-prompt-engineering/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 04 - Orodja →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za avtomatski prevod [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jezik je treba šteti za verodostojen vir. Za pomembne informacije priporočamo strokovni prevod s strani človeka. Nismo odgovorni za morebitne nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->