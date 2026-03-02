# Modul 03: RAG (Retrieval-Augmented Generation)

## Sadržaj

- [Video vodič](../../../03-rag)
- [Što ćete naučiti](../../../03-rag)
- [Preduvjeti](../../../03-rag)
- [Razumijevanje RAG-a](../../../03-rag)
  - [Koji RAG pristup koristi ovaj vodič?](../../../03-rag)
- [Kako radi](../../../03-rag)
  - [Obrada dokumenata](../../../03-rag)
  - [Izrada ugradnji (embeddinga)](../../../03-rag)
  - [Semantičko pretraživanje](../../../03-rag)
  - [Generiranje odgovora](../../../03-rag)
- [Pokretanje aplikacije](../../../03-rag)
- [Korištenje aplikacije](../../../03-rag)
  - [Učitajte dokument](../../../03-rag)
  - [Postavite pitanja](../../../03-rag)
  - [Provjerite izvore](../../../03-rag)
  - [Eksperimentirajte s pitanjima](../../../03-rag)
- [Ključni pojmovi](../../../03-rag)
  - [Strategija dijeljenja u dijelove](../../../03-rag)
  - [Rezultati sličnosti](../../../03-rag)
  - [Pohrana u memoriji](../../../03-rag)
  - [Upravljanje kontekstnim prozorom](../../../03-rag)
- [Kada je RAG važan](../../../03-rag)
- [Sljedeći koraci](../../../03-rag)

## Video vodič

Pogledajte ovu uživo snimljenu sesiju koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Što ćete naučiti

U prethodnim modulima naučili ste kako voditi razgovore s AI-jem i učinkovito strukturirati svoje upute. No postoji temeljno ograničenje: jezični modeli znaju samo ono što su naučili tijekom treninga. Ne mogu odgovarati na pitanja o politikama vaše tvrtke, dokumentaciji vaših projekata ili informacijama koje nisu obrađene tijekom njihovog treninga.

RAG (Retrieval-Augmented Generation) rješava taj problem. Umjesto da pokušavate modelu "podučiti" svoje informacije (što je skupo i nepraktično), dajete mu mogućnost pretraživanja vaših dokumenata. Kada netko postavi pitanje, sustav pronalazi relevantne informacije i uključuje ih u upit. Model zatim odgovara temeljem tog dohvaćenog konteksta.

Zamislite RAG kao davanje modelu referentne biblioteke. Kada postavite pitanje, sustav:

1. **Upit korisnika** - Postavite pitanje  
2. **Ugradnja (embedding)** - Pretvara vaše pitanje u vektor  
3. **Vektorsko pretraživanje** - Pronalaženje sličnih dijelova dokumenata  
4. **Sastavljanje konteksta** - Dodavanje relevantnih dijelova u upit  
5. **Odgovor** - LLM generira odgovor na temelju konteksta

Ovo temelji odgovore modela na vašim stvarnim podacima umjesto oslanjanja samo na znanje iz treninga ili nagađanja odgovora.

## Preduvjeti

- Završeni [Modul 00 - Brzi početak](../00-quick-start/README.md) (za primjer Easy RAG koji se koristi gore)
- Završeni [Modul 01 - Uvod](../01-introduction/README.md) (postavljeni Azure OpenAI resursi, uključujući model ugradnje `text-embedding-3-small`)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana naredbom `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite upute za implementaciju tamo. Naredba `azd up` implementira i GPT chat model i model ugradnje koji koristi ovaj modul.

## Razumijevanje RAG-a

Dijagram ispod ilustrira osnovni koncept: umjesto da se oslanja samo na podatke treninga modela, RAG mu daje referentnu biblioteku vaših dokumenata kojima može pristupiti prije generiranja svakog odgovora.

<img src="../../../translated_images/hr/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ovaj dijagram prikazuje razliku između standardnog LLM-a (koji nagađa na temelju trening podataka) i LLM-a s RAG-om (koji prvo konzultira vaše dokumente).*

Evo kako dijelovi međusobno povezuju od kraja do kraja. Korisničko pitanje ide kroz četiri faze — ugradnja, vektorsko pretraživanje, sastavljanje konteksta te generiranje odgovora — gdje se svaka faza gradi na prethodnoj:

<img src="../../../translated_images/hr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ovaj dijagram prikazuje kompletan RAG pipeline — korisnički upit prolazi kroz ugradnju, vektorsko pretraživanje, sastavljanje konteksta i generiranje odgovora.*

Ostatak ovog modula detaljno prolazi kroz svaku fazu s kôdom koji možete pokrenuti i mijenjati.

### Koji RAG pristup koristi ovaj vodič?

LangChain4j nudi tri načina za implementaciju RAG-a, svaki s različitom razinom apstrakcije. Dijagram ispod ih uspoređuje jedan pokraj drugoga:

<img src="../../../translated_images/hr/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ovaj dijagram uspoređuje tri LangChain4j RAG pristupa — Easy, Native i Advanced — prikazujući njihove ključne komponente i kada koristiti koji.*

| Pristup | Što radi | Kompromis |
|---|---|---|
| **Easy RAG** | Automatski povezuje sve putem `AiServices` i `ContentRetriever`. Oznaka sučelja, dodavanje retrievera i LangChain4j rukuje ugradnjom, pretraživanjem i sastavljanjem prompta iza kulisa. | Minimalan kod, ali ne vidite što se događa u svakom koraku. |
| **Native RAG** | Vi izravno pozivate model za ugradnju, tražite u spremištu, gradite prompt i sami generirate odgovor — jedan eksplicitan korak po korak. | Više koda, ali svaka faza je vidljiva i moguće ju je mijenjati. |
| **Advanced RAG** | Koristi `RetrievalAugmentor` framework s prilagodljivim transformatorima upita, usmjerivačima, reprioritizatorima i injektorima sadržaja za produkcijske cjevovode. | Maksimalna fleksibilnost, ali znatno veća složenost. |

**Ovaj vodič koristi Native pristup.** Svaki korak RAG pipelinea — ugradnja upita, pretraživanje u vektorskom spremištu, sastavljanje konteksta i generiranje odgovora — izričito je napisan u [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ovo je namjerno: kao resurs za učenje važnije je da vidite i razumijete svaku fazu nego da je kôd što sažetiji. Kad se upoznate s time kako dijelovi funkcioniraju, možete prijeći na Easy RAG za brze prototipove ili Advanced RAG za produkcijske sustave.

> **💡 Već ste vidjeli Easy RAG u akciji?** Modul [Brzi početak](../00-quick-start/README.md) uključuje primjer dokumentacijskog Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) koji koristi Easy RAG pristup — LangChain4j automatski rukuje ugradnjom, pretraživanjem i sastavljanjem prompta. Ovaj modul ide korak dalje produbljujući pipeline tako da sami možete vidjeti i kontrolirati svaki korak.

<img src="../../../translated_images/hr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ovaj dijagram prikazuje Easy RAG pipeline iz `SimpleReaderDemo.java`. Usporedite ga s Native pristupom korištenim u ovom modulu: Easy RAG skriva ugradnju, dohvaćanje i sastavljanje prompta iza `AiServices` i `ContentRetriever` — učitate dokument, prikačite retriever i dobijete odgovore. Native pristup u ovom modulu otvara pipeline tako da vi pozivate svaku fazu (ugradi, pretraži, sastavi kontekst, generiraj) sami, dajući vam potpunu vidljivost i kontrolu.*

## Kako radi

RAG pipeline u ovom modulu razlaže se na četiri faze koje se izvode u nizu svaki put kad korisnik postavi pitanje. Prvo se učitani dokument **parsira i dijeli na dijelove** koji su upravljive veličine. Ti dijelovi zatim se pretvaraju u **vektorske ugradnje (embeddinge)** i spremaju tako da se mogu matematički uspoređivati. Kada upit stigne, sustav izvodi **semantičko pretraživanje** da pronađe najrelevantnije dijelove, i na kraju ih prosljeđuje kao kontekst LLM-u za **generiranje odgovora**. Sljedeći odjeljci detaljno prolaze kroz svaku fazu s pravim kodom i dijagramima. Pogledajmo prvi korak.

### Obrada dokumenata

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kada učitate dokument, sustav ga parsira (PDF ili običan tekst), prikači metapodatke poput imena datoteke, a zatim ga podijeli na dijelove — manje segmente koji komotno stanu u modelov kontekstni prozor. Ti dijelovi se lagano preklapaju kako se ne bi izgubio kontekst na granicama.

```java
// Parsiraj učitanu datoteku i umotaj je u LangChain4j dokument
Document document = Document.from(content, metadata);

// Podijeli na dijelove od 300 tokena s preklapanjem od 30 tokena
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Dijagram ispod vizualno prikazuje kako to funkcionira. Primijetite kako svaki dio dijeli neke tokene sa susjedima — 30-token overlapa osigurava da nijedan važan kontekst ne "padne kroz pukotine":

<img src="../../../translated_images/hr/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ovaj dijagram prikazuje kako se dokument dijeli na segmente od 300 tokena s 30 tokena preklapanja, čuvajući kontekst na granicama segmenata.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i pitajte:  
> - "Kako LangChain4j dijeli dokumente na dijelove i zašto je preklapanje važno?"  
> - "Koja je optimalna veličina dijelova za različite vrste dokumenata i zašto?"  
> - "Kako postupati s dokumentima na više jezika ili sa specijalnim formatiranjem?"

### Izrada ugradnji (embeddinga)

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Svaki dio se pretvara u numerički prikaz nazvan embedding — u biti, pretvarač značenja u brojeve. Model ugradnje ne "razmišlja" kao chat model; ne može slijediti naredbe, zaključivati ili odgovarati na pitanja. Ono što može napraviti je preslikati tekst u matematički prostor gdje slična značenja budu blizu — "auto" blizu "automobil," "polica povrata" blizu "vrati mi novac." Razmislite o chat modelu kao o osobi s kojom razgovarate; model ugradnje je super efikasan sustav za arhiviranje.

<img src="../../../translated_images/hr/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ovaj dijagram pokazuje kako model ugradnje pretvara tekst u numeričke vektore, pozicionirajući slična značenja — poput "auto" i "automobil" — blizu jedan drugoga u vektorskom prostoru.*

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

Dijagram razreda ispod prikazuje dvije odvojene struje u RAG pipelineu i LangChain4j klase koje ih implementiraju. **Struja unosa (ingestion flow)** (izvodi se jednom prilikom učitavanja) dijeli dokument, ugrađuje dijelove i sprema ih preko `.addAll()`. **Struja upita (query flow)** (izvodi se svaki put kad korisnik pita) ugrađuje pitanje, pretražuje spremište preko `.search()` i prosljeđuje pronađeni kontekst chat modelu. Obje se struje susreću na zajedničkom sučelju `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/hr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ovaj dijagram prikazuje dvije struje u RAG pipelineu — unos i upit — i kako se povezuju kroz zajednički EmbeddingStore.*

Kada se ugradnje pohrane, slični sadržaji se prirodno grupiraju u vektorskom prostoru. Donji prikaz pokazuje kako se dokumenti o povezanim temama grupiraju u bliže međusobne točke, što omogućuje semantičko pretraživanje:

<img src="../../../translated_images/hr/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ova vizualizacija prikazuje kako se povezani dokumenti grupiraju u 3D vektorskom prostoru, s temama poput Tehničke dokumentacije, Poslovnih pravila i Čestih pitanja oblikujući jasne skupine.*

Kad korisnik pretražuje, sustav slijedi četiri koraka: jednom ugrađuje dokumente, kod svakog pretraživanja ugrađuje upit, uspoređuje vektor upita sa svim pohranjenim vektorima koristeći kosinusnu sličnost i vraća top-K najsličnijih dijelova. Sljedeći dijagram prolazi kroz svaki korak i klase LangChain4j vezane uz njih:

<img src="../../../translated_images/hr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ovaj dijagram prikazuje četverostepeni proces pretraživanja ugradnjom: ugradnja dokumenata, ugradnja upita, usporedba vektora kosinusnom sličnosti i vraćanje top-K rezultata.*

### Semantičko pretraživanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kada postavite pitanje, i ono također postaje ugradnja (embedding). Sustav uspoređuje ugradnju vašeg pitanja sa svim ugradnjama dijelova dokumenta. Pronalazi dijelove s najsličnijim značenjima — ne samo podudarajuće ključne riječi, već stvarnu semantičku sličnost.

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

Dijagram ispod uspoređuje semantičko pretraživanje s tradicionalnim pretraživanjem po ključnim riječima. Pretraživanje po ključnoj riječi "vozilo" promašuje dio o "automobilima i kamionima," ali semantičko pretraživanje razumije da znače isto i vraća ga kao rezultat s visokim rangom:

<img src="../../../translated_images/hr/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ovaj dijagram uspoređuje pretraživanje po ključnim riječima s semantičkim pretraživanjem, pokazujući kako semantičko pretraživanje dohvaća konceptualno povezani sadržaj čak i kad ključne riječi nisu identične.*

U pozadini, sličnost se mjeri kosinusnom sličnosti — u principu se pita "pokazuju li ove dvije strelice u istom smjeru?" Dva dijela mogu koristiti potpuno različite riječi, ali ako znače isto, njihovi vektori pokazuju isti smjer i ocjena im je blizu 1.0:

<img src="../../../translated_images/hr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*Ovaj dijagram ilustrira kosinusnu sličnost kao kut između vektora ugrađivanja — vektori koji su više usklađeni dobivaju ocjenu bližu 1.0, što ukazuje na veću semantičku sličnost.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i pitajte:
> - "Kako funkcionira pretraživanje sličnosti s ugrađivanjima i što određuje ocjenu?"
> - "Koji prag sličnosti trebam koristiti i kako on utječe na rezultate?"
> - "Kako se nositi sa slučajevima kada se ne pronađu relevantni dokumenti?"

### Generiranje odgovora

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantniji dijelovi tekstova sastavljaju se u strukturirani prompt koji uključuje eksplicitne upute, dohvaćeni kontekst i korisnikovo pitanje. Model čita te specifične dijelove i odgovara temeljem tih informacija — može koristiti samo ono što je pred njim, što sprječava halucinacije.

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

Dijagram ispod prikazuje ovu montažu u akciji — dijelovi s najvišim ocjenama iz koraka pretraživanja ubacuju se u predložak prompta, a `OpenAiOfficialChatModel` generira utemeljen odgovor:

<img src="../../../translated_images/hr/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ovaj dijagram pokazuje kako se dijelovi s najvišim ocjenama slažu u strukturirani prompt, dopuštajući modelu da generira utemeljen odgovor iz vaših podataka.*

## Pokrenite aplikaciju

**Provjerite implementaciju:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (izrađena tijekom Modula 01):

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije pomoću `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8081. Možete preskočiti naredbe za pokretanje ispod i direktno otići na http://localhost:8081.

**Opcija 1: Koristeći Spring Boot Dashboard (preporučeno za VS Code korisnike)**

Razvojni kontejner uključuje ekstenziju Spring Boot Dashboard koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ga pronaći na Activity Baru s lijeve strane VS Code-a (potražite ikonu Spring Boot-a).

Iz Spring Boot Dashboard-a možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti zapisnike aplikacija u stvarnom vremenu
- Nadzirati status aplikacija

Jednostavno kliknite gumb za reprodukciju pokraj "rag" da pokrenete ovaj modul, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ovaj snimak zaslona prikazuje Spring Boot Dashboard u VS Code-u gdje možete vizualno pokretati, zaustavljati i pratiti aplikacije.*

**Opcija 2: Koristeći skripte ljuske**

Pokrenite sve web aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korijenskog direktorija
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korijenskog direktorija
.\start-all.ps1
```

Ili pokrenite samo ovaj modul:

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

Oba skripta automatski učitavaju varijable okoline iz korijenske `.env` datoteke i izgradit će JAR-ove ako ne postoje.

> **Napomena:** Ako želite ručno izgraditi sve module prije pokretanja:
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

Otvorite http://localhost:8081 u vašem pregledniku.

**Za zaustavljanje:**

**Bash:**
```bash
./stop.sh  # Samo ovaj modul
# Ili
cd .. && ./stop-all.sh  # Svi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ovaj modul
# Ili
cd ..; .\stop-all.ps1  # Svi moduli
```

## Korištenje aplikacije

Aplikacija pruža web sučelje za prijenos dokumenata i postavljanje pitanja.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hr/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ovaj snimak zaslona prikazuje sučelje RAG aplikacije gdje prenosite dokumente i postavljate pitanja.*

### Prenesite dokument

Započnite prijenosom dokumenta - TXT datoteke najbolje funkcioniraju za testiranje. U ovom direktoriju pružen je `sample-document.txt` koji sadrži informacije o značajkama LangChain4j, RAG implementaciji i najboljim praksama - savršeno za testiranje sustava.

Sustav obrađuje vaš dokument, dijeli ga na dijelove i stvara ugrađivanja za svaki dio. To se događa automatski prilikom prijenosa.

### Postavljajte pitanja

Sada postavite specifična pitanja o sadržaju dokumenta. Isprobajte nešto činjenično što je jasno navedeno u dokumentu. Sustav traži relevantne dijelove, uključuje ih u prompt i generira odgovor.

### Provjerite izvore

Primijetite da svaki odgovor uključuje izvore s ocjenama sličnosti. Te ocjene (od 0 do 1) pokazuju koliko je svaki dio bio relevantan za vaše pitanje. Više ocjene znače bolje podudaranje. To vam omogućuje da provjerite odgovor prema izvornom materijalu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hr/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ovaj snimak zaslona prikazuje rezultate upita s generiranim odgovorom, izvorima i ocjenama relevantnosti za svaki dohvaćeni dio.*

### Eksperimentirajte s pitanjima

Isprobajte različite vrste pitanja:
- Specifične činjenice: "Koja je glavna tema?"
- Usporedbe: "Koja je razlika između X i Y?"
- Sažeci: "Sažmi ključne točke o Z"

Promatrajte kako se ocjene relevantnosti mijenjaju ovisno o tome koliko dobro vaše pitanje odgovara sadržaju dokumenta.

## Ključni pojmovi

### Strategija dijeljenja na dijelove

Dokumenti se dijele na dijelove od 300 tokena s preklapanjem od 30 tokena. Ova ravnoteža osigurava da svaki dio ima dovoljno konteksta da bude smislen, a istovremeno ostaje dovoljno malen da u prompt stane više dijelova.

### Ocjene sličnosti

Svaki dohvaćeni dio dolazi s ocjenom sličnosti između 0 i 1 koja označava koliko je blizu pitanja korisnika. Dijagram ispod vizualizira raspon ocjena i način na koji sustav koristi te pragove za filtriranje rezultata:

<img src="../../../translated_images/hr/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ovaj dijagram prikazuje raspon ocjena od 0 do 1, s minimalnim pragom od 0.5 koji filtrira irelevantne dijelove.*

Ocjene se kreću od 0 do 1:
- 0.7-1.0: Visoka relevantnost, točno podudaranje
- 0.5-0.7: Relevantno, dobar kontekst
- Ispod 0.5: Odbačeno, preslabo slično

Sustav dohvaća samo dijelove iznad minimalnog praga kako bi osigurao kvalitetu.

Ugrađivanja dobro funkcioniraju kada se značenje jasno grupira, ali imaju slijepe točke. Dijagram ispod prikazuje uobičajene načine neuspjeha — preveliki dijelovi stvaraju nejasne vektore, premali dijelovi nemaju kontekst, dvosmisleni pojmovi upućuju na više skupina, a pretraživanja po točnom podudaranju (ID-ovi, brojevi dijelova) uopće ne funkcioniraju s ugrađivanjima:

<img src="../../../translated_images/hr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ovaj dijagram prikazuje uobičajene načine neuspjeha ugrađivanja: preveliki dijelovi, premali dijelovi, dvosmisleni pojmovi koji upućuju na više skupina i pretraživanja po točnom podudaranju poput ID-ova.*

### Spremište u memoriji

Ovaj modul koristi spremište u memoriji radi jednostavnosti. Kada ponovno pokrenete aplikaciju, preneseni dokumenti se brišu. Produkcijski sustavi koriste trajne vektorske baze podataka poput Qdranta ili Azure AI Searcha.

### Upravljanje kontekstnim prozorom

Svaki model ima maksimalni kontekstni prozor. Ne možete uključiti svaki dio iz velikog dokumenta. Sustav dohvaća top N najrelevantnijih dijelova (zadano 5) da ostane unutar ograničenja, a pritom osigurava dovoljno konteksta za točne odgovore.

## Kada je RAG važan

RAG nije uvijek pravi pristup. Vodič za odluke ispod pomaže vam odrediti kada RAG donosi vrijednost u usporedbi s jednostavnijim pristupima — poput izravnog uključivanja sadržaja u prompt ili oslanjanja na ugrađeno znanje modela:

<img src="../../../translated_images/hr/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ovaj dijagram prikazuje vodič za odluke kada RAG donosi dodatnu vrijednost, a kada su jednostavniji pristupi dovoljni.*

**Koristite RAG kada:**
- Odgovarate na pitanja o vlasničkim dokumentima
- Informacije se često mijenjaju (politike, cijene, specifikacije)
- Potrebna je točnost sa izvorima
- Sadržaj je prevelik da stane u jedan prompt
- Trebate provjerljive, utemeljene odgovore

**Nemojte koristiti RAG kada:**
- Pitanja zahtijevaju opće znanje koje model već ima
- Potrebni su podaci u stvarnom vremenu (RAG radi na prenesenim dokumentima)
- Sadržaj je dovoljno malen da se uključi izravno u prompt

## Sljedeći koraci

**Sljedeći modul:** [04-tools - AI agenti s alatima](../04-tools/README.md)

---

**Navigacija:** [← Prethodno: Modul 02 - Inženjering prompta](../02-prompt-engineering/README.md) | [Natrag na početak](../README.md) | [Sljedeće: Modul 04 - Alati →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena o odricanju od odgovornosti**:  
Ovaj dokument preveden je korištenjem AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim i autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Ne odgovaramo za bilo kakva nesporazuma ili pogrešne interpretacije proizašle iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->