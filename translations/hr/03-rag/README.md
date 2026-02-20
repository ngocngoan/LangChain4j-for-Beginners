# Modul 03: RAG (Generiranje s početnim dohvatom)

## Sadržaj

- [Što ćete naučiti](../../../03-rag)
- [Razumijevanje RAG-a](../../../03-rag)
- [Preduvjeti](../../../03-rag)
- [Kako to funkcionira](../../../03-rag)
  - [Obrada dokumenata](../../../03-rag)
  - [Stvaranje ugrađenosti](../../../03-rag)
  - [Semantičko pretraživanje](../../../03-rag)
  - [Generiranje odgovora](../../../03-rag)
- [Pokrenite aplikaciju](../../../03-rag)
- [Korištenje aplikacije](../../../03-rag)
  - [Učitajte dokument](../../../03-rag)
  - [Postavljajte pitanja](../../../03-rag)
  - [Provjerite izvore](../../../03-rag)
  - [Eksperimentirajte s pitanjima](../../../03-rag)
- [Ključni pojmovi](../../../03-rag)
  - [Strategija dijeljenja na dijelove](../../../03-rag)
  - [Skorovi sličnosti](../../../03-rag)
  - [Pohrana u memoriji](../../../03-rag)
  - [Upravljanje kontekstnim prozorom](../../../03-rag)
- [Kada je RAG važan](../../../03-rag)
- [Sljedeći koraci](../../../03-rag)

## Što ćete naučiti

U prethodnim modulima naučili ste kako voditi razgovore s AI-jem i učinkovito strukturirati svoje upite. No postoji temeljno ograničenje: jezični modeli znaju samo ono što su naučili tijekom treninga. Ne mogu odgovoriti na pitanja o politikama vaše tvrtke, dokumentaciji vašeg projekta ili bilo kojim informacijama na kojima nisu trenirani.

RAG (Generiranje s početnim dohvatom) rješava ovaj problem. Umjesto da pokušavate model naučiti vaše informacije (što je skupo i nepraktično), dajete mu mogućnost pretraživanja kroz vaše dokumente. Kad netko postavi pitanje, sustav pronalazi relevantne informacije i uključuje ih u upit. Model tada odgovara na temelju tog dohvaćenog konteksta.

Zamislite RAG kao davanje modelu referentne knjižnice. Kad postavite pitanje, sustav:

1. **Korisnički upit** – Vi postavite pitanje  
2. **Ugrađivanje** – Pretvara vaše pitanje u vektor  
3. **Vektorsko pretraživanje** – Pronalazi slične dijelove dokumenata  
4. **Sastavljanje konteksta** – Dodaje relevantne dijelove u upit  
5. **Odgovor** – LLM generira odgovor temeljen na kontekstu

Ovo utemeljuje modelove odgovore u vašim stvarnim podacima umjesto da se oslanja na znanje sa treninga ili izmišlja odgovore.

## Razumijevanje RAG-a

Donji dijagram ilustrira osnovni koncept: umjesto oslanjanja samo na podatke za trening modela, RAG mu daje referentnu knjižnicu vaših dokumenata koju konsultira prije nego generira svaki odgovor.

<img src="../../../translated_images/hr/what-is-rag.1f9005d44b07f2d8.webp" alt="Što je RAG" width="800"/>

Evo kako se dijelovi povezuju od početka do kraja. Korisničko pitanje prolazi kroz četiri faze — ugrađivanje, vektorsko pretraživanje, sastavljanje konteksta i generiranje odgovora — pri čemu svaka faza nadograđuje prethodnu:

<img src="../../../translated_images/hr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG arhitektura" width="800"/>

Ostatak ovog modula detaljno prolazi kroz svaku fazu, s kodom koji možete pokrenuti i mijenjati.

## Preduvjeti

- Završeni Modul 01 (deployani Azure OpenAI resursi)  
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana naredbom `azd up` u Modulu 01)

> **Napomena:** Ako niste završili Modul 01, prvo slijedite tamošnje upute za postavljanje.

## Kako to funkcionira

### Obrada dokumenata

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kad učitate dokument, sustav ga parsira (PDF ili čist tekst), dodaje metapodatke poput naziva datoteke te ga zatim razbija u dijelove – manje dijelove koji se udobno uklapaju u kontekstni prozor modela. Ti dijelovi se djelomično preklapaju kako se na rubovima ne bi izgubio kontekst.

```java
// Parsiraj prenesenu datoteku i omotaj je u LangChain4j Dokument
Document document = Document.from(content, metadata);

// Podijeli na dijelove od 300 tokena s preklapanjem od 30 tokena
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Dijagram dolje prikazuje kako to radi vizualno. Primijetite kako svaki dio dijeli neke tokene sa svojim susjedima — preklapanje od 30 tokena osigurava da nijedan važan kontekst ne padne između:

<img src="../../../translated_images/hr/document-chunking.a5df1dd1383431ed.webp" alt="Dijeljenje dokumenata na dijelove" width="800"/>

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) i pitajte:  
> - "Kako LangChain4j dijeli dokumente na dijelove i zašto je preklapanje važno?"  
> - "Koja je optimalna veličina dijelova za različite vrste dokumenata i zašto?"  
> - "Kako rukovati dokumentima na više jezika ili sa posebnom formatiranjem?"

### Stvaranje ugrađenosti

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Svaki dio se pretvara u numerički prikaz zvan embedding – ustvari matematički otisak koji hvata značenje teksta. Sličan tekst proizvodi slične embeddinge.

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
  
Dijagram klasa dolje pokazuje kako se ove komponente LangChain4j povezuju. `OpenAiOfficialEmbeddingModel` pretvara tekst u vektore, `InMemoryEmbeddingStore` drži vektore uz njihove originalne `TextSegment` podatke, a `EmbeddingSearchRequest` upravlja parametrima dohvaćanja, poput `maxResults` i `minScore`:

<img src="../../../translated_images/hr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Klase LangChain4j RAG" width="800"/>

Nakon pohrane embeddinga, sličan sadržaj se prirodno grupira u prostoru vektora. Vizualizacija dolje pokazuje kako dokumenti o povezanim temama završavaju kao susjedne točke, što omogućuje semantičko pretraživanje:

<img src="../../../translated_images/hr/vector-embeddings.2ef7bdddac79a327.webp" alt="Prostor vektorskih embeddinga" width="800"/>

### Semantičko pretraživanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kada postavite pitanje, i ono se pretvara u embedding. Sustav uspoređuje embedding vašeg pitanja s embeddingima svih dijelova dokumenata. Pronalazi dijelove s najsličnijim značenjem – ne samo po ključnim riječima, nego stvarnu semantičku sličnost.

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
  
Dijagram dolje kontrastira semantičko s pretraživanjem po ključnim riječima. Pretraživanje po ključnim riječima za "vozilo" propustit će dio o "automobilima i kamionima", ali semantičko pretraživanje razumije da znače isto i vrati ga kao visoko rangiranu podudarnost:

<img src="../../../translated_images/hr/semantic-search.6b790f21c86b849d.webp" alt="Semantičko pretraživanje" width="800"/>

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) i pitajte:  
> - "Kako radi pretraživanje po sličnosti s embeddingima i što određuje skor?"  
> - "Koju granicu sličnosti trebam koristiti i kako to utječe na rezultate?"  
> - "Kako postupiti kad se ne pronađu relevantni dokumenti?"

### Generiranje odgovora

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najrelevantniji dijelovi se sastavljaju u strukturirani upit koji uključuje eksplicitne upute, dohvaćeni kontekst i pitanje korisnika. Model čita te konkretne dijelove i odgovara na temelju te informacije — može koristiti samo ono što je pred njim, što sprječava izmišljanje.

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
  
Dijagram dolje prikazuje taj sastav u akciji — najviši rangirani dijelovi iz koraka pretraživanja ubacuju se u predložak upita, a `OpenAiOfficialChatModel` generira odgovoran odgovor:

<img src="../../../translated_images/hr/context-assembly.7e6dd60c31f95978.webp" alt="Sastavljanje konteksta" width="800"/>

## Pokrenite aplikaciju

**Provjerite deploy:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana u Modulu 01):  
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije pomoću `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8081. Možete preskočiti naredbe za pokretanje i ići direktno na http://localhost:8081.

**Opcija 1: Korištenje Spring Boot Dashboarda (preporučeno za korisnike VS Code-a)**

Razvojni kontejner uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Nalazi se u Activity Baru s lijeve strane VS Code-a (potražite ikonu Spring Boota).

Iz Spring Boot Dashboarda možete:  
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru  
- Pokrenuti/zaustaviti aplikacije jednim klikom  
- Prikazivati logove aplikacije u stvarnom vremenu  
- Pratiti status aplikacija  

Jednostavno kliknite gumb za pokretanje pored “rag” da započnete ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
  
Obje skripte automatski učitavaju varijable okoline iz `.env` datoteke u korijenu i izgradit će JAR-ove ako ne postoje.

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
  
Otvorite http://localhost:8081 u svom pregledniku.

**Da zaustavite:**  

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

Aplikacija pruža web sučelje za učitavanje dokumenata i postavljanje pitanja.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hr/rag-homepage.d90eb5ce1b3caa94.webp" alt="Sučelje RAG aplikacije" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Sučelje RAG aplikacije – učitajte dokumente i postavljajte pitanja*

### Učitajte dokument

Započnite učitavanjem dokumenta – za testiranje najbolje rade TXT datoteke. U ovom direktoriju nalazi se primjer `sample-document.txt` koji sadrži informacije o značajkama LangChain4j, implementaciji RAG-a i najboljim praksama – savršeno za isprobavanje sustava.

Sustav obrađuje dokument, dijeli ga u dijelove i stvara embeddinge za svaki dio. To se događa automatski nakon učitavanja.

### Postavljajte pitanja

Sad postavite specifična pitanja o sadržaju dokumenta. Pokušajte nešto što je činjenicom jasno navedeno u dokumentu. Sustav traži relevantne dijelove, uključuje ih u upit i generira odgovor.

### Provjerite izvore

Primijetite da svaki odgovor uključuje izvore s relevantnim skorovima. Ti skorovi (0 do 1) pokazuju koliko je relevantan svaki dio u odnosu na vaše pitanje. Viši skorovi znače bolje podudaranje. Ovo vam omogućava da provjerite odgovor u izvoru.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hr/rag-query-results.6d69fcec5397f355.webp" alt="Rezultati upita RAG-a" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rezultati upita prikazuju odgovor s izvorima i ocjenom relevantnosti*

### Eksperimentirajte s pitanjima

Isprobajte različite vrste pitanja:  
- Specifične činjenice: "Koja je glavna tema?"  
- Usporedbe: "Koja je razlika između X i Y?"  
- Sažeci: "Sažmi ključne točke o Z"  

Promatrajte kako se skorovi relevantnosti mijenjaju ovisno o tome koliko dobro vaše pitanje odgovara sadržaju dokumenta.

## Ključni pojmovi

### Strategija dijeljenja na dijelove

Dokumenti se dijele u dijelove od 300 tokena s preklapanjem od 30 tokena. Ova ravnoteža osigurava da svaki dio ima dovoljno konteksta da bude smislen, a opet je dovoljno malen da se u upit može uključiti više dijelova.

### Skorovi sličnosti

Svaki dohvaćeni dio dolazi sa skorom sličnosti između 0 i 1 koji pokazuje koliko je blizu podudaranja s pitanjem korisnika. Dijagram dolje vizualizira raspon skora i kako sustav koristi te vrijednosti za filtriranje rezultata:

<img src="../../../translated_images/hr/similarity-scores.b0716aa911abf7f0.webp" alt="Skorovi sličnosti" width="800"/>

Skorovi se kreću od 0 do 1:  
- 0.7-1.0: Izuzetno relevantno, točno podudaranje  
- 0.5-0.7: Relevantno, dobar kontekst  
- Ispod 0.5: Filtrirano, previše različito  

Sustav dohvaća samo dijelove iznad minimalnog praga radi osiguravanja kvalitete.

### Pohrana u memoriji

Ovaj modul koristi pohranu u memoriji radi jednostavnosti. Pri ponovnom pokretanju aplikacije učitani dokumenti se gube. Produkcijski sustavi koriste postojane vektorske baze podataka poput Qdrant ili Azure AI Search.

### Upravljanje kontekstnim prozorom

Svaki model ima maksimalni kontekstni prozor. Ne možete uključiti svaki dio velikog dokumenta. Sustav dohvaća top N najrelevantnijih dijelova (zadano 5) kako bi ostao unutar limita, a da pritom pruži dovoljno konteksta za točne odgovore.

## Kada je RAG važan

RAG nije uvijek ispravan pristup. Vodič za odluku dolje pomaže vam odrediti kada RAG donosi vrijednost za razliku od jednostavnijih pristupa — poput izravnog uključivanja sadržaja u upit ili oslanjanja na ugrađeno znanje modela:

<img src="../../../translated_images/hr/when-to-use-rag.1016223f6fea26bc.webp" alt="Kada koristiti RAG" width="800"/>

**Koristite RAG kada:**
- Odgovaranje na pitanja o vlasničkim dokumentima
- Informacije se često mijenjaju (politike, cijene, specifikacije)
- Točnost zahtijeva navođenje izvora
- Sadržaj je prevelik da stane u jedan upit
- Potrebni su vam provjerljivi, utemeljeni odgovori

**Nemojte koristiti RAG kada:**
- Pitanja zahtijevaju opće znanje koje model već ima
- Potrebni su podaci u stvarnom vremenu (RAG radi na učitanim dokumentima)
- Sadržaj je dovoljno mali da se može uključiti izravno u upite

## Sljedeći koraci

**Sljedeći modul:** [04-tools - AI agenti s alatima](../04-tools/README.md)

---

**Navigacija:** [← Prethodni: Modul 02 - Prompt inženjering](../02-prompt-engineering/README.md) | [Natrag na početak](../README.md) | [Sljedeći: Modul 04 - Alati →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument je preveden korištenjem AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakva nesporazuma ili pogrešna tumačenja nastala korištenjem ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->