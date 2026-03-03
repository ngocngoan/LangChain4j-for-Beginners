# Modul 03: RAG (Generiranje potpomognuto pretraživanjem)

## Sadržaj

- [Video vodič](../../../03-rag)
- [Što ćete naučiti](../../../03-rag)
- [Preduvjeti](../../../03-rag)
- [Razumijevanje RAG-a](../../../03-rag)
  - [Koji RAG pristup koristi ovaj vodič?](../../../03-rag)
- [Kako to radi](../../../03-rag)
  - [Obrada dokumenata](../../../03-rag)
  - [Stvaranje embeddinga](../../../03-rag)
  - [Semantičko pretraživanje](../../../03-rag)
  - [Generiranje odgovora](../../../03-rag)
- [Pokrenite aplikaciju](../../../03-rag)
- [Korištenje aplikacije](../../../03-rag)
  - [Učitajte dokument](../../../03-rag)
  - [Postavite pitanja](../../../03-rag)
  - [Provjerite izvore referenci](../../../03-rag)
  - [Eksperimentirajte s pitanjima](../../../03-rag)
- [Ključni pojmovi](../../../03-rag)
  - [Strategija dijeljenja na dijelove](../../../03-rag)
  - [Ocjene sličnosti](../../../03-rag)
  - [Pohrana u memoriji](../../../03-rag)
  - [Upravljanje kontekstnim prozorom](../../../03-rag)
- [Kada je RAG važan](../../../03-rag)
- [Sljedeći koraci](../../../03-rag)

## Video vodič

Pogledajte ovu uživo sesiju koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG s LangChain4j - uživo sesija" width="800"/></a>

## Što ćete naučiti

U prethodnim modulima naučili ste kako voditi razgovore s AI-jem i kako učinkovitije strukturirati svoje upite. Ali postoji temeljno ograničenje: jezični modeli znaju samo ono što su naučili tijekom treninga. Oni ne mogu odgovoriti na pitanja o pravilnicima vaše tvrtke, vašoj projektnoj dokumentaciji ili bilo kojim informacijama na kojima nisu bili trenirani.

RAG (Generiranje potpomognuto pretraživanjem) rješava ovaj problem. Umjesto da pokušavate modelu "podučiti" vaše informacije (što je skupo i nepraktično), dajete mu mogućnost da pretražuje vaše dokumente. Kad netko postavi pitanje, sustav pronalazi relevantne informacije i uključuje ih u upit. Model tada odgovara na temelju tog dohvaćenog konteksta.

Zamislite RAG kao davanje modelu referentne knjižnice. Kad postavite pitanje, sustav:

1. **Korisnički upit** - Vi postavite pitanje  
2. **Embedding** - Pretvara vaše pitanje u vektor  
3. **Pretraživanje vektora** - Pronalazi slične dijelove dokumenta  
4. **Sastavljanje konteksta** - Dodaje relevantne dijelove u upit  
5. **Odgovor** - LLM generira odgovor temeljen na kontekstu

Ovo utemeljuje odgovore modela u vašim stvarnim podacima, umjesto da se oslanja samo na znanje iz treninga ili smišlja odgovore.

## Preduvjeti

- Završeni [Modul 00 - Brzi početak](../00-quick-start/README.md) (za Easy RAG primjer spomenut kasnije u ovom modulu)  
- Završeni [Modul 01 - Uvod](../01-introduction/README.md) (Azure OpenAI resursi su postavljeni, uključujući embedding model `text-embedding-3-small`)  
- `.env` datoteka u glavnom direktoriju s Azure akreditivima (stvorena naredbom `azd up` u Modulu 01)  

> **Napomena:** Ako niste završili Modul 01, prvo pratite tamo upute za postavljanje. Naredba `azd up` postavlja i GPT chat model i embedding model koji koristi ovaj modul.

## Razumijevanje RAG-a

Dijagram ispod ilustrira osnovni koncept: umjesto oslanjanja samo na podatke modela iz treninga, RAG mu daje referentnu knjižnicu vaših dokumenata koje konzultira prije generiranja svakog odgovora.

<img src="../../../translated_images/hr/what-is-rag.1f9005d44b07f2d8.webp" alt="Što je RAG" width="800"/>

*Ovaj dijagram prikazuje razliku između standardnog LLM-a (koji nagađa iz trening podataka) i LLM-a s RAG poboljšanjem (koji prvo konzultira vaše dokumente).*

Evo kako se dijelovi povezuju od početka do kraja. Korisničko pitanje prolazi kroz četiri faze — embedding, pretraživanje vektora, sastavljanje konteksta i generiranje odgovora — gdje se svaka nadovezuje na prethodnu:

<img src="../../../translated_images/hr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektura" width="800"/>

*Ovaj dijagram prikazuje cjelokupni RAG pipeline — korisnički upit prolazi kroz embedding, pretraživanje vektora, sastavljanje konteksta i generiranje odgovora.*

Ostatak ovog modula detaljno prolazi kroz svaku fazu, s kodom koji možete pokrenuti i mijenjati.

### Koji RAG pristup koristi ovaj vodič?

LangChain4j nudi tri načina za implementaciju RAG-a, svaki s različitom razinom apstrakcije. Dijagram ispod ih uspoređuje jedan uz drugog:

<img src="../../../translated_images/hr/rag-approaches.5b97fdcc626f1447.webp" alt="Tri RAG pristupa u LangChain4j" width="800"/>

*Ovaj dijagram uspoređuje tri LangChain4j RAG pristupa — Easy, Native i Advanced — pokazujući njihove ključne komponente i kada koristiti koji.*

| Pristup | Što radi | Kompromis |
|---|---|---|
| **Easy RAG** | Automatizira sve kroz `AiServices` i `ContentRetriever`. Vi označavate sučelje, dodajete retriever i LangChain4j upravlja embeddingom, pretraživanjem i sastavljanjem upita u pozadini. | Minimalni kod, ali ne vidite što se događa u svakoj fazi. |
| **Native RAG** | Vi sami pozivate embedding model, pretražujete pohranu, gradite upit i generirate odgovor — jedan jasan korak po korak. | Više koda, ali svaka faza je vidljiva i može se mijenjati. |
| **Advanced RAG** | Koristi `RetrievalAugmentor` framework s priključnim transformatorima upita, ruterima, re-rankerima i injektorima sadržaja za produkcijske pipelineove. | Maksimalna fleksibilnost, ali znatno veća složenost. |

**Ovaj vodič koristi Native pristup.** Svaki korak RAG pipelinea — embedded upita, pretraživanje vektorske pohrane, sastavljanje konteksta i generiranje odgovora — eksplicitno je napisan u [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). To je namjerno: kao edukativni materijal, važnije je da vidite i razumijete svaku fazu nego da kod bude čošćen. Kad savladate kako se dijelovi uklapaju, možete preći na Easy RAG za brze prototipe ili Advanced RAG za produkcijske sustave.

> **💡 Već ste vidjeli Easy RAG u akciji?** Modul [Brzi početak](../00-quick-start/README.md) uključuje primjer Pitanja i Odgovora s dokumentima ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) koji koristi Easy RAG pristup — LangChain4j automatski upravlja embeddingom, pretraživanjem i sastavljanjem upita. Ovaj modul je sljedeći korak koji otvara pipeline da biste mogli vidjeti i kontrolirati svaku fazu sami.

Dijagram ispod prikazuje Easy RAG pipeline iz spomenutog Brzog početka. Primijetite kako `AiServices` i `EmbeddingStoreContentRetriever` skrivaju svu kompleksnost — učitate dokument, dodate retriever i dobijete odgovore. Native pristup u ovom modulu otkriva svaku od tih skrivenih faza:

<img src="../../../translated_images/hr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG pipeline - LangChain4j" width="800"/>

*Ovaj dijagram prikazuje Easy RAG pipeline iz `SimpleReaderDemo.java`. Usporedite ga s Native pristupom korištenim u ovom modulu: Easy RAG skriva embedding, dohvaćanje i sastavljanje upita iza `AiServices` i `ContentRetriever` — vi učitate dokument, dodate retriever i dobivate odgovore. Native pristup u ovom modulu otkriva pipeline tako da sami pozivate svaku fazu (embedding, pretraživanje, sastavljanje konteksta, generiranje), što vam daje potpunu vidljivost i kontrolu.*

## Kako to radi

RAG pipeline u ovom modulu razlaže se na četiri faze koje se izvršavaju redom svaki put kad korisnik postavi pitanje. Prvo se učitani dokument **parsira i dijeli na dijelove** koji su upravljive veličine. Ti se dijelovi zatim pretvaraju u **vektorske embeddinge** i pohranjuju tako da se mogu matematički uspoređivati. Kad stigne upit, sustav izvršava **semantičko pretraživanje** za pronalazak najrelevantnijih dijelova, a zatim ih daje kao kontekst LLM-u za **generiranje odgovora**. Sljedeći odjeljci vode vas kroz svaku fazu s konkretnim kodom i dijagramima. Pogledajmo prvi korak.

### Obrada dokumenata

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kad učitate dokument, sustav ga parsira (PDF ili plain text), dodaje metapodatke poput naziva datoteke, a zatim dijeli na dijelove — manje dijelove koji stanu u kontekstni prozor modela. Ti se dijelovi lagano preklapaju kako se ne bi izgubio kontekst na granicama.

```java
// Parsirajte prenesenu datoteku i zamotajte je u LangChain4j Dokument
Document document = Document.from(content, metadata);

// Podijelite na dijelove od 300 tokena s preklapanjem od 30 tokena
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Dijagram ispod prikazuje kako to vizualno funkcionira. Primijetite kako svaki dio dijeli neke tokene s susjedima — preklapanje od 30 tokena osigurava da se nijedan važan kontekst ne izgubi u prijelazima:

<img src="../../../translated_images/hr/document-chunking.a5df1dd1383431ed.webp" alt="Dijeljenje dokumenta na dijelove" width="800"/>

*Ovaj dijagram prikazuje dokument podijeljen u dijelove od 300 tokena s preklapanjem od 30 tokena, čime se čuva kontekst na granicama dijelova.*

> **🤖 Isprobajte uz [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i pitajte:  
> - "Kako LangChain4j dijeli dokumente na dijelove i zašto je preklapanje važno?"  
> - "Koja je optimalna veličina dijelova za različite vrste dokumenata i zašto?"  
> - "Kako postupiti s dokumentima na više jezika ili sa specialnim formatiranjem?"

### Stvaranje embeddinga

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Svaki se dio pretvara u numeričku reprezentaciju zvanu embedding — u osnovi pretvarač značenja u brojeve. Embedding model nije "inteligentan" poput chat modela; ne može slijediti upute, rezonirati niti odgovarati na pitanja. Ono što može jest mapirati tekst u matematički prostor gdje slična značenja završavaju blizu jedno drugom — "auto" blizu "vozilo", "pravila povrata" blizu "vrati novac". Zamislite chat model kao osobu s kojom razgovarate, a embedding model kao iznimno dobar sustav arhiviranja.

Dijagram ispod vizualizira ovaj koncept — ulazi tekst, izlaze numerički vektori, a slična značenja proizvode vektore koji su blizu:

<img src="../../../translated_images/hr/embedding-model-concept.90760790c336a705.webp" alt="Koncept embedding modela" width="800"/>

*Ovaj dijagram prikazuje kako embedding model pretvara tekst u numeričke vektore, stavljajući slična značenja — kao "auto" i "vozilo" — blizu jedno drugom u vektorskom prostoru.*

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
  
Dijagram klase ispod prikazuje dva odvojena tijeka u RAG pipelineu i LangChain4j klase koje ih implementiraju. **Tijek unosa** (izvršava se jednom prilikom učitavanja) dijeli dokument, stvara embedding dijelova i pohranjuje ih pomoću `.addAll()`. **Tijek upita** (izvršava se svaki put kad korisnik pita) embedira pitanje, pretražuje pohranu preko `.search()`, i prosljeđuje pronađeni kontekst chat modelu. Oba tijeka se spajaju na zajedničko sučelje `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/hr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG klase" width="800"/>

*Ovaj dijagram prikazuje dva tijeka u RAG pipelineu — unos i upit — i kako se povezuju kroz zajedničko EmbeddingStore sučelje.*

Kad se embeddingi pohrane, sličan sadržaj prirodno se grupira u vektorskom prostoru. Vizualizacija ispod pokazuje kako dokumenti o srodnim temama završavaju kao bliski točke, što čini semantičko pretraživanje mogućim:

<img src="../../../translated_images/hr/vector-embeddings.2ef7bdddac79a327.webp" alt="Prostor vektorskih embeddinga" width="800"/>

*Ova vizualizacija prikazuje kako se povezani dokumenti grupiraju u 3D vektorskom prostoru, s temama poput Tehničke dokumentacije, Poslovnih pravila i Često postavljanih pitanja stvarajući odvojene skupine.*

Kad korisnik pretražuje, sustav slijedi četiri koraka: embedira dokumente jednom, embedira upit za svako pretraživanje, uspoređuje vektor upita sa svim pohranjenim vektorima koristeći kosinusnu sličnost i vraća top-K dijelova s najvišim rezultatima. Dijagram ispod vodi kroz svaki korak i uključene LangChain4j klase:

<img src="../../../translated_images/hr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Koraci pretraživanja embeddinga" width="800"/>

*Ovaj dijagram prikazuje proces pretraživanja embeddingom u četiri koraka: embediranje dokumenata, embediranje upita, usporedbu vektora kosinusnom sličnosti i vraćanje top-K rezultata.*

### Semantičko pretraživanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kad postavite pitanje, i pitanje se pretvara u embedding. Sustav uspoređuje embedding vašeg pitanja sa embeddingima dijelova dokumenata. Pronalazi dijelove s najsličnijim značenjima — ne samo podudaranje ključnih riječi, već stvarnu semantičku sličnost.

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
  
Dijagram ispod uspoređuje semantičko pretraživanje s tradicionalnim pretraživanjem po ključnim riječima. Pretraživanje po ključnim riječima za "vozilo" ne pronalazi dio o "automobilima i kamionima", ali semantičko pretraživanje razumije da znače isto i vraća ga kao visoko relevantan rezultat:

<img src="../../../translated_images/hr/semantic-search.6b790f21c86b849d.webp" alt="Semantičko pretraživanje" width="800"/>

*Ovaj dijagram uspoređuje pretraživanje po ključnim riječima s semantičkim pretraživanjem, pokazujući kako semantičko pretraživanje dohvaća konceptualno povezani sadržaj čak i kad se ključne riječi ne podudaraju.*
Ispod haube, sličnost se mjeri korištenjem kosinusne sličnosti — praktički pitajući "pokazuju li ove dvije strelice u istom smjeru?" Dva dijela mogu koristiti potpuno različite riječi, ali ako znače isto, njihovi vektori pokazuju u istom smjeru i rezultat je blizak 1.0:

<img src="../../../translated_images/hr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Ovaj dijagram ilustrira kosinusnu sličnost kao kut između vektora ugradnje — bolje poravnati vektori postižu rezultat bliži 1.0, što ukazuje na veću semantičku sličnost.*

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i pitajte:
> - "Kako radi pretraživanje sličnosti s ugradnjama i što određuje rezultat?"
> - "Koju granicu sličnosti trebam koristiti i kako to utječe na rezultate?"
> - "Kako postupiti u slučajevima kada se ne pronađu relevantni dokumenti?"

### Generiranje odgovora

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantniji dijelovi se slažu u strukturirani upit koji uključuje izričite upute, dohvaćeni kontekst i korisničko pitanje. Model čita ti specifični dijelovi i odgovara na temelju tih informacija — može koristiti samo ono što je pred njim, što sprječava halucinacije.

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

Dijagram ispod prikazuje ovaj proces sklapanja — najupečatljiviji dijelovi iz koraka pretraživanja ubacuju se u predložak upita, a `OpenAiOfficialChatModel` generira utemeljen odgovor:

<img src="../../../translated_images/hr/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ovaj dijagram pokazuje kako se najužiji dijelovi slažu u strukturirani upit, što omogućava modelu da generira utemeljen odgovor iz vaših podataka.*

## Pokrenite aplikaciju

**Provjerite implementaciju:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01). Pokrenite ovo iz direktorija modula (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije korištenjem `./start-all.sh` iz korijenskog direktorija (kako je opisano u Modulu 01), ovaj modul već radi na portu 8081. Možete preskočiti naredbe za pokretanje ispod i direktno otići na http://localhost:8081.

**Opcija 1: Korištenje Spring Boot nadzorne ploče (Preporučeno za korisnike VS Codea)**

Razvojno okruženje uključuje ekstenziju Spring Boot Dashboards koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći na traci aktivnosti lijevo u VS Codeu (potražite ikonu Spring Boot).

Iz Spring Boot nadzorne ploče možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokretati i zaustavljati aplikacije jednim klikom
- Pregledavati zapisnike aplikacije u stvarnom vremenu
- Nadzirati status aplikacije

Jednostavno kliknite gumb za pokretanje pored "rag" da pokrenete ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ovaj snimak zaslona prikazuje Spring Boot nadzornu ploču u VS Codeu, gdje možete vizualno pokretati, zaustavljati i nadzirati aplikacije.*

**Opcija 2: Korištenje shell skripti**

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

Obje skripte automatski učitavaju varijable okoline iz `.env` datoteke u korijenu i izgradit će JAR datoteke ako ne postoje.

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

**Zaustavljanje:**

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

Započnite prijenosom dokumenta - TXT datoteke najbolje funkcioniraju za testiranje. U ovom direktoriju nalazi se `sample-document.txt` koji sadrži informacije o značajkama LangChain4j, implementaciji RAG-a i najboljim praksama - savršeno za testiranje sustava.

Sustav obrađuje vaš dokument, dijeli ga na dijelove i stvara ugradnje za svaki dio. To se događa automatski kada prenesete dokument.

### Postavljajte pitanja

Sada postavite specifična pitanja o sadržaju dokumenta. Isprobajte nešto faktualno što je jasno istaknuto u dokumentu. Sustav traži relevantne dijelove, uključuje ih u upit i generira odgovor.

### Provjerite izvore

Primijetite da svaki odgovor uključuje izvore s rezultatima sličnosti. Ti rezultati (od 0 do 1) pokazuju koliko je svaki dio bio relevantan za vaše pitanje. Viši rezultati znače bolje podudaranje. To vam omogućuje da provjerite točnost odgovora prema izvornom materijalu.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hr/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ovaj snimak zaslona prikazuje rezultate upita s generiranim odgovorom, referencama izvora i ocjenama relevantnosti za svaki dohvaćeni dio.*

### Eksperimentirajte s pitanjima

Isprobajte različite vrste pitanja:
- Specifične činjenice: "Koja je glavna tema?"
- Usporedbe: "Koja je razlika između X i Y?"
- Sažetke: "Sažmi ključne točke o Z"

Promatrajte kako se ocjene relevantnosti mijenjaju ovisno o tome koliko dobro vaše pitanje odgovara sadržaju dokumenta.

## Ključni pojmovi

### Strategija dijeljenja na dijelove

Dokumenti se dijele na dijelove od 300 tokena s preklapanjem od 30 tokena. Ovaj balans osigurava da svaki dio ima dovoljno konteksta da bude značajan, a istovremeno ostaje dovoljno mali da se u upit može uključiti više dijelova.

### Rezultati sličnosti

Svaki dohvaćeni dio dolazi s ocjenom sličnosti između 0 i 1 koja pokazuje koliko je blisko povezan s pitanjem korisnika. Dijagram ispod vizualizira raspon rezultata i kako sustav koristi te vrijednosti za filtriranje rezultata:

<img src="../../../translated_images/hr/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ovaj dijagram prikazuje raspon rezultata od 0 do 1, s minimalnom granicom od 0.5 koja filtrira irelevantne dijelove.*

Ocjene se kreću od 0 do 1:
- 0,7-1,0: Vrlo relevantno, točno podudaranje
- 0,5-0,7: Relevantno, dobar kontekst
- Ispod 0,5: Filtrirano, previše različito

Sustav dohvaća samo dijelove iznad minimalne granice kako bi osigurao kvalitetu.

Ugradnje dobro funkcioniraju kada se značenje jasno grupira, ali imaju i slabosti. Dijagram ispod prikazuje česte uzroke neuspjeha — dijelovi preveliki daju nejasne vektore, dijelovi premali nemaju kontekst, dvosmisleni pojmovi upućuju na više grupa, a točno podudaranje pretraživanja (ID-ovi, brojevi dijelova) uopće ne funkcionira s ugradnjama:

<img src="../../../translated_images/hr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ovaj dijagram prikazuje česte načine neuspjeha ugradnji: dijelovi preveliki, dijelovi premali, dvosmisleni pojmovi koji upućuju na više grupa, te točno podudaranje pretraživanja poput ID-ova.*

### Pohrana u memoriji

Ovaj modul koristi pohranu u memoriji radi jednostavnosti. Kada ponovno pokrenete aplikaciju, preneseni dokumenti se gube. Produkcijski sustavi koriste trajne vektorske baze podataka kao što su Qdrant ili Azure AI Search.

### Upravljanje kontekstnim prozorom

Svaki model ima maksimalni kontekstni prozor. Ne možete uključiti svaki dio iz velikog dokumenta. Sustav dohvaća prvih N najrelevantnijih dijelova (zadano 5) kako bi ostao unutar ograničenja, a istovremeno pružio dovoljan kontekst za točne odgovore.

## Kada je RAG važan

RAG nije uvijek pravi pristup. Vodič za odluke ispod pomaže vam utvrditi kad RAG donosi dodatnu vrijednost u odnosu na jednostavnije pristupe — poput uključivanja sadržaja izravno u upit ili oslanjanja na ugrađeno znanje modela:

<img src="../../../translated_images/hr/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ovaj dijagram prikazuje vodič za odluke kada RAG donosi vrijednost u odnosu na jednostavnije pristupe.*

## Sljedeći koraci

**Sljedeći modul:** [04-tools - AI agenti s alatima](../04-tools/README.md)

---

**Navigacija:** [← Prethodno: Modul 02 - Prompt inženjering](../02-prompt-engineering/README.md) | [Natrag na početak](../README.md) | [Sljedeće: Modul 04 - Alati →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj je dokument preveden korištenjem AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili kriva tumačenja proizašla iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->