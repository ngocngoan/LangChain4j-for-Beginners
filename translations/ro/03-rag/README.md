# Modulul 03: RAG (Generare Augmentată cu Recuperare)

## Cuprins

- [Prezentare video](../../../03-rag)
- [Ce vei învăța](../../../03-rag)
- [Prerechizite](../../../03-rag)
- [Înțelegerea RAG](../../../03-rag)
  - [Ce abordare RAG folosește acest tutorial?](../../../03-rag)
- [Cum funcționează](../../../03-rag)
  - [Procesarea documentelor](../../../03-rag)
  - [Crearea embedding-urilor](../../../03-rag)
  - [Căutare semantică](../../../03-rag)
  - [Generarea răspunsului](../../../03-rag)
- [Rulează aplicația](../../../03-rag)
- [Utilizarea aplicației](../../../03-rag)
  - [Încarcă un document](../../../03-rag)
  - [Pune întrebări](../../../03-rag)
  - [Verifică referințele sursă](../../../03-rag)
  - [Experimentează cu întrebări](../../../03-rag)
- [Concepte cheie](../../../03-rag)
  - [Strategia de fragmentare](../../../03-rag)
  - [Scoruri de similitudine](../../../03-rag)
  - [Stocare în memorie](../../../03-rag)
  - [Gestionarea ferestrei de context](../../../03-rag)
- [Când contează RAG](../../../03-rag)
- [Pașii următori](../../../03-rag)

## Prezentare video

Urmărește această sesiune live care explică cum să începi cu acest modul:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG cu LangChain4j - Sesiune Live" width="800"/></a>

## Ce vei învăța

În modulele anterioare, ai învățat cum să ai conversații cu AI și să îți structurezi prompturile eficient. Dar există o limitare fundamentală: modelele de limbaj cunosc doar ceea ce au învățat în timpul antrenamentului. Nu pot răspunde la întrebări despre politicile companiei tale, documentația proiectului tău sau orice informație pentru care nu au fost instruiți.

RAG (Generare Augmentată cu Recuperare) rezolvă această problemă. În loc să încerci să înveți modelul informațiile tale (ceea ce este costisitor și nepractic), îi oferi abilitatea de a căuta prin documentele tale. Când cineva pune o întrebare, sistemul găsește informația relevantă și o include în prompt. Modelul răspunde apoi pe baza contextului astfel recuperat.

Gândește-te la RAG ca și cum i-ai oferi modelului o bibliotecă de referință. Când pui o întrebare, sistemul:

1. **Interogarea utilizatorului** - Pui o întrebare  
2. **Embedding** - Convertește întrebarea într-un vector  
3. **Căutare vectorială** - Găsește fragmente de document similare  
4. **Asamblarea contextului** - Adaugă fragmente relevante la prompt  
5. **Răspuns** - LLM generează un răspuns bazat pe context  

Aceasta ancorează răspunsurile modelului în datele tale reale, în loc să se bazeze pe cunoștințele de antrenament sau să inventeze răspunsuri.

## Prerechizite

- Finalizat [Modulul 00 - Start rapid](../00-quick-start/README.md) (pentru exemplul Easy RAG referit mai târziu în acest modul)  
- Finalizat [Modulul 01 - Introducere](../01-introduction/README.md) (resurse Azure OpenAI implementate, inclusiv modelul `text-embedding-3-small`)  
- Fișier `.env` în directorul principal cu credențialele Azure (creat de `azd up` în Modulul 01)  

> **Notă:** Dacă nu ai terminat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo. Comanda `azd up` implementează atât modelul de chat GPT, cât și modelul de embedding folosit de acest modul.

## Înțelegerea RAG

Diagrama de mai jos ilustrează conceptul de bază: în loc să se bazeze doar pe datele de antrenament ale modelului, RAG îi oferă o bibliotecă de referință a documentelor tale pentru a consulta înainte de a genera fiecare răspuns.

<img src="../../../translated_images/ro/what-is-rag.1f9005d44b07f2d8.webp" alt="Ce este RAG" width="800"/>

*Această diagramă prezintă diferența dintre un LLM standard (care ghicește din datele de antrenament) și un LLM îmbunătățit cu RAG (care consultă mai întâi documentele tale).*

Iată cum se leagă piesele cap-la-cap. Întrebarea utilizatorului trece prin patru etape — embedding, căutare vectorială, asamblare a contextului și generare a răspunsului — fiecare construind pe cea anterioară:

<img src="../../../translated_images/ro/rag-architecture.ccb53b71a6ce407f.webp" alt="Arhitectura RAG" width="800"/>

*Această diagramă arată fluxul complet al pipeline-ului RAG — o interogare a utilizatorului trece prin embedding, căutare vectorială, asamblarea contextului și generarea răspunsului.*

Restul modulului parcurge fiecare etapă în detaliu, cu cod pe care îl poți rula și modifica.

### Ce abordare RAG folosește acest tutorial?

LangChain4j oferă trei metode de implementare a RAG, fiecare cu un nivel diferit de abstractizare. Diagrama de mai jos le compară unul lângă altul:

<img src="../../../translated_images/ro/rag-approaches.5b97fdcc626f1447.webp" alt="Trei abordări RAG în LangChain4j" width="800"/>

*Această diagramă compară cele trei abordări LangChain4j pentru RAG — Easy, Native și Advanced — arătând componentele cheie și când să le folosești.*

| Abordare | Ce face | Compromis |
|---|---|---|
| **Easy RAG** | Leagă totul automat prin `AiServices` și `ContentRetriever`. Anotezi o interfață, atașezi un retriever, iar LangChain4j se ocupă de embedding, căutare și asamblarea promptului în fundal. | Minimal de cod, dar nu vezi ce se întâmplă la fiecare pas. |
| **Native RAG** | Apelezi modelul de embedding, cauți în magazin, construiești promptul și generezi răspunsul singur — pas cu pas explicit. | Mai mult cod, dar fiecare etapă este vizibilă și modificabilă. |
| **Advanced RAG** | Folosește cadrul `RetrievalAugmentor` cu transformatoare, rutări, re-clasificatoare și injectoare de conținut pentru pipeline-uri la nivel de producție. | Flexibilitate maximă, dar complexitate mult mai mare. |

**Tutorialul folosește abordarea Native.** Fiecare pas al pipeline-ului RAG — embedarea interogării, căutarea în vector store, asamblarea contextului și generarea răspunsului — este scris explicit în [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Acest lucru este intenționat: ca resursă de învățare, este mai important să vezi și să înțelegi fiecare etapă decât să minimizezi codul. Odată ce ești confortabil cu cum se leagă piesele, poți trece la Easy RAG pentru prototipuri rapide sau Advanced RAG pentru sisteme de producție.

> **💡 Ai văzut deja Easy RAG în acțiune?** Modulul [Quick Start](../00-quick-start/README.md) include un exemplu de Q&A pe documente ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) care folosește abordarea Easy RAG — LangChain4j se ocupă automat de embedding, căutare și asamblarea promptului. Acest modul face pasul următor, deschizând acel pipeline pentru ca tu să vezi și să controlezi fiecare etapă.

Diagrama de mai jos arată pipeline-ul Easy RAG din acel exemplu Quick Start. Observă cum `AiServices` și `EmbeddingStoreContentRetriever` ascund întreaga complexitate — încarci un document, atașezi un retriever și primești răspunsuri. Abordarea Native din acest modul deschide fiecare dintre acei pași ascunși:

<img src="../../../translated_images/ro/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Această diagramă arată pipeline-ul Easy RAG din `SimpleReaderDemo.java`. Compară-l cu abordarea Native folosită în acest modul: Easy RAG ascunde embedding-ul, recuperarea și asamblarea promptului în spatele `AiServices` și `ContentRetriever` — încarci un document, atașezi un retriever și primești răspunsuri. Abordarea Native din acest modul deschide pipeline-ul astfel încât să apelezi fiecare etapă (embed, search, asamblează context, generează) manual, oferindu-ți vizibilitate și control complet.*

## Cum funcționează

Pipeline-ul RAG din acest modul se împarte în patru etape care rulează secvențial de fiecare dată când un utilizator pune o întrebare. Mai întâi, un document încărcat este **parsat și fragmentat** în bucăți gestionabile. Aceste fragmente sunt apoi convertite în **embedding-uri vectoriale** și stocate pentru a putea fi comparate matematic. Când sosește o interogare, sistemul efectuează o **căutare semantică** pentru a găsi cele mai relevante fragmente și în final le transmite ca context LLM-ului pentru **generarea răspunsului**. Secțiunile următoare parcurg fiecare etapă cu codul efectiv și diagrame. Să începem cu primul pas.

### Procesarea documentelor

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Când încarci un document, sistemul îl parsează (PDF sau text simplu), atașează metadata precum numele fișierului, apoi îl împarte în fragmente — bucăți mai mici care încă încap confortabil în fereastra de context a modelului. Aceste fragmente se suprapun ușor pentru a nu pierde context la granițe.

```java
// Parcurge fișierul încărcat și îl înfășoară într-un Document LangChain4j
Document document = Document.from(content, metadata);

// Împarte în bucăți de 300 de tokeni cu o suprapunere de 30 de tokeni
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagrama de mai jos arată cum funcționează vizual acest proces. Observă cum fiecare fragment share-uiește unele tokenuri cu vecinii săi — suprapunerea de 30 de tokenuri asigură că nu se pierde context important între fragmente:

<img src="../../../translated_images/ro/document-chunking.a5df1dd1383431ed.webp" alt="Fragmentarea documentelor" width="800"/>

*Această diagramă arată un document fragmentat în bucăți de 300 de tokenuri cu suprapunere de 30 de tokenuri, păstrând contextul la granițele fragmentelor.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) și întreabă:  
> - "Cum împarte LangChain4j documentele în fragmente și de ce este importantă suprapunerea?"  
> - "Care este dimensiunea optimă a fragmentului pentru diferite tipuri de documente și de ce?"  
> - "Cum gestionez documentele în mai multe limbi sau cu formatare specială?"

### Crearea embedding-urilor

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Fiecare fragment este convertit într-o reprezentare numerică numită embedding — practic un convertor de semnificații în numere. Modelul de embedding nu este „inteligent” ca un model de chat; nu poate urma instrucțiuni, raționa sau răspunde la întrebări. Ce poate face este să mapeze textul într-un spațiu matematic unde semnificații similare stau aproape unele de altele — „mașină” este aproape de „automobil”, „politică de rambursare” este aproape de „returnează banii”. Gândește-te la modelul de chat ca la o persoană cu care poți vorbi; modelul de embedding este un sistem ultra-bun de arhivare.

Diagrama de mai jos vizualizează acest concept — textul intră, vectori numerici ies, iar semnificațiile similare produc vectori apropiați:

<img src="../../../translated_images/ro/embedding-model-concept.90760790c336a705.webp" alt="Concept Model Embedding" width="800"/>

*Această diagramă arată cum un model de embedding convertește textul în vectori numerici, plasând semnificații similare — precum „mașină” și „automobil” — apropiate în spațiul vectorial.*

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

Diagrama claselor de mai jos arată cele două fluxuri separate într-un pipeline RAG și clasele LangChain4j care le implementează. Fluxul de **ingestie** (rulează o singură dată la încărcare) împarte documentul, creează embedding-urile fragmentelor și le stochează prin `.addAll()`. Fluxul de **interogare** (rulează la fiecare întrebare) embed-ează întrebarea, caută în magazin prin `.search()` și transmite contextul găsit modelului de chat. Ambele fluxuri se întâlnesc în interfața comună `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/ro/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Clase LangChain4j RAG" width="800"/>

*Această diagramă arată cele două fluxuri într-un pipeline RAG — ingestie și interogare — și cum se leagă printr-un EmbeddingStore comun.*

Odată ce embedding-urile sunt stocate, conținutul similar se grupează natural în spațiul vectorial. Vizualizarea de mai jos arată cum documentele despre subiecte înrudite devin puncte apropiate, ceea ce face posibilă căutarea semantică:

<img src="../../../translated_images/ro/vector-embeddings.2ef7bdddac79a327.webp" alt="Spațiul embedding-urilor vectoriale" width="800"/>

*Această vizualizare arată cum documentele înrudite se grupează împreună în spațiul vectorial 3D, cu grupuri distincte precum Documentație Tehnică, Reguli de Afaceri și Întrebări Frecvente.*

Când un utilizator caută, sistemul urmează patru pași: embed-ează o singură dată documentele, embed-ează interogarea la fiecare căutare, compară vectorul interogării cu toți vectorii stocați folosind similitudinea cosinus și returnează primele K fragmente cu cel mai înalt scor. Diagrama de mai jos parcurge fiecare pas și clasele LangChain4j implicate:

<img src="../../../translated_images/ro/embedding-search-steps.f54c907b3c5b4332.webp" alt="Pașii căutării embedding" width="800"/>

*Această diagramă arată procesul căutării embedding în patru pași: embed-ează documentele, embed-ează interogarea, compară vectorii cu similitudinea cosinus și returnează primele K rezultate.*

### Căutare semantică

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Când pui o întrebare, întrebarea ta devine la rândul ei un embedding. Sistemul compară embedding-ul întrebării tale cu embedding-urile tuturor fragmentelor de document. Găsește cele mai asemănătoare fragmente — nu doar potriviri de cuvinte-cheie, ci similitudine semantică reală.

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

Diagrama de mai jos contrastează căutarea semantică cu căutarea tradițională pe cuvinte-cheie. O căutare pe cuvântul „vehicul” ratează un fragment despre „mașini și camioane”, dar căutarea semantică înțelege că înseamnă același lucru și îl returnează ca o potrivire cu scor ridicat:

<img src="../../../translated_images/ro/semantic-search.6b790f21c86b849d.webp" alt="Căutare semantică" width="800"/>

*Această diagramă compară căutarea bazată pe cuvinte-cheie cu cea semantică, arătând cum căutarea semantică returnează conținut conceptual înrudit chiar și când cuvintele exacte diferă.*
La bază, similaritatea este măsurată folosind similaritatea cosinus — practic întrebând „indică aceste două săgeți în aceeași direcție?” Două bucăți pot folosi cuvinte complet diferite, dar dacă înseamnă același lucru vectorii lor indică aceeași direcție și obțin un scor aproape de 1.0:

<img src="../../../translated_images/ro/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similaritate cosinus" width="800"/>

*Acest diagramă ilustrează similaritatea cosinus ca unghiul dintre vectorii de embedding — vectorii mai aliniați obțin scoruri mai apropiate de 1.0, indicând o similaritate semantică mai mare.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) și întreabă:
> - "Cum funcționează căutarea similarității cu embeddings și ce determină scorul?"
> - "Ce prag de similaritate ar trebui să folosesc și cum afectează rezultatele?"
> - "Cum gestionez cazurile când nu se găsesc documente relevante?"

### Generarea răspunsului

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Cele mai relevante bucăți sunt asamblate într-un prompt structurat care include instructiuni explicite, contextul recuperat și întrebarea utilizatorului. Modelul citește acele bucăți specifice și răspunde pe baza acelor informații — poate folosi doar ce are în față, ceea ce previne halucinațiile.

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

Diagrama de mai jos arată această asamblare în acțiune — cele mai bine cotate bucăți din pasul de căutare sunt injectate în șablonul de prompt, iar `OpenAiOfficialChatModel` generează un răspuns fundamentat:

<img src="../../../translated_images/ro/context-assembly.7e6dd60c31f95978.webp" alt="Asamblare context" width="800"/>

*Această diagramă arată cum cele mai bine cotate bucăți sunt asamblate într-un prompt structurat, permițând modelului să genereze un răspuns fundamentat pe datele tale.*

## Rulează aplicația

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu credențiale Azure (create în timpul Modulului 01). Rulează acest lucru din directorul modulului (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din directorul rădăcină (așa cum este descris în Modulul 01), acest modul rulează deja pe portul 8081. Poți sări peste comenzile de pornire de mai jos și merge direct la http://localhost:8081.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în bara de activități din partea stângă a VS Code (caută pictograma Spring Boot).

Din Spring Boot Dashboard, poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opri aplicațiile cu un singur clic
- Vizualiza jurnalele aplicației în timp real
- Monitoriza starea aplicațiilor

Pur și simplu apasă butonul de redare de lângă „rag” pentru a porni acest modul, sau pornește toate modulele simultan.

<img src="../../../translated_images/ro/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Această captură de ecran arată Spring Boot Dashboard în VS Code, unde poți porni, opri și monitoriza aplicațiile vizual.*

**Opțiunea 2: Folosind scripturi shell**

Pornește toate aplicațiile web (modulele 01-04):

**Bash:**
```bash
cd ..  # Din directorul rădăcină
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Din directorul rădăcină
.\start-all.ps1
```

Sau pornește doar acest modul:

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

Ambele scripturi încarcă automat variabilele de mediu din fișierul `.env` din rădăcină și vor construi JAR-urile dacă acestea nu există.

> **Notă:** Dacă preferi să compilezi manual toate modulele înainte de pornire:
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

Deschide http://localhost:8081 în browserul tău.

**Pentru a opri:**

**Bash:**
```bash
./stop.sh  # Numai acest modul
# Sau
cd .. && ./stop-all.sh  # Toate modulele
```

**PowerShell:**
```powershell
.\stop.ps1  # Doar acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Utilizarea aplicației

Aplicația oferă o interfață web pentru încărcarea documentelor și punerea de întrebări.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ro/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfață aplicație RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Această captură de ecran arată interfața aplicației RAG unde încarci documente și pui întrebări.*

### Încarcă un document

Începe prin a încărca un document - fișiere TXT funcționează cel mai bine pentru testare. În acest director este oferit un fișier `sample-document.txt` care conține informații despre caracteristicile LangChain4j, implementarea RAG și bune practici - perfect pentru testarea sistemului.

Sistemul procesează documentul tău, îl împarte în bucăți și creează embeddings pentru fiecare bucată. Acest lucru se întâmplă automat la încărcare.

### Pune întrebări

Acum pune întrebări specifice despre conținutul documentului. Încearcă ceva factual care este clar menționat în document. Sistemul caută bucățile relevante, le include în prompt și generează un răspuns.

### Verifică referințele sursă

Observă că fiecare răspuns include referințe la surse cu scoruri de similaritate. Aceste scoruri (0 până la 1) arată cât de relevantă a fost fiecare bucată pentru întrebarea ta. Scorurile mai mari înseamnă potriviri mai bune. Acest lucru îți permite să verifici răspunsul față de materialul sursă.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ro/rag-query-results.6d69fcec5397f355.webp" alt="Rezultate interogare RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Această captură de ecran arată rezultatele interogărilor cu răspunsul generat, referințele sursă și scorurile de relevanță pentru fiecare bucată recuperată.*

### Experimentează cu întrebări

Încearcă diferite tipuri de întrebări:
- Fapte specifice: "Care este subiectul principal?"
- Comparații: "Care este diferența între X și Y?"
- Rezumate: "Rezumați punctele cheie despre Z"

Urmărește cum se schimbă scorurile de relevanță în funcție de cât de bine se potrivește întrebarea ta cu conținutul documentului.

## Concepute cheie

### Strategia de împărțire în bucăți

Documentele sunt împărțite în bucăți de 300 de tokeni cu o suprapunere de 30 de tokeni. Această balanță asigură că fiecare bucată are suficient context pentru a fi semnificativă, dar rămâne suficient de mică pentru a include mai multe bucăți într-un prompt.

### Scorurile de similaritate

Fiecare bucată recuperată vine cu un scor de similaritate între 0 și 1 care indică cât de bine se potrivește întrebării utilizatorului. Diagrama de mai jos vizualizează intervalele scorurilor și cum le folosește sistemul pentru a filtra rezultatele:

<img src="../../../translated_images/ro/similarity-scores.b0716aa911abf7f0.webp" alt="Scoruri de similaritate" width="800"/>

*Această diagramă arată intervalul scorurilor de la 0 la 1, cu un prag minim de 0.5 care elimină bucățile irelevante.*

Scorurile variază între 0 și 1:
- 0.7-1.0: Foarte relevante, potrivire exactă
- 0.5-0.7: Relevante, context bun
- Sub 0.5: Eliminat, prea puțin similar

Sistemul recuperează doar bucățile deasupra pragului minim pentru a asigura calitatea.

Embeddings funcționează bine când sensurile se grupează clar, dar au puncte oarbe. Diagrama de mai jos arată modurile comune de eșec — bucățile prea mari produc vectori neclari, bucățile prea mici lipsesc contextul, termenii ambigui indică multiple grupuri, iar căutările după potrivire exactă (ID-uri, numere de piese) nu funcționează deloc cu embeddings:

<img src="../../../translated_images/ro/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Moduri de eșec ale embedding-urilor" width="800"/>

*Această diagramă arată modurile comune de eșec ale embedding-urilor: bucăți prea mari, bucăți prea mici, termeni ambigui care indică multiple grupuri și căutări exacte ca ID-urile.*

### Stocare în memorie

Acest modul folosește stocare în memorie pentru simplitate. Când repornești aplicația, documentele încărcate se pierd. Sistemele de producție folosesc baze de date vectoriale persistente precum Qdrant sau Azure AI Search.

### Gestionarea ferestrei de context

Fiecare model are o limită maximă a ferestrei de context. Nu poți include fiecare bucată dintr-un document mare. Sistemul recuperează primele N bucăți cele mai relevante (implicit 5) pentru a rămâne în limite și a oferi suficient context pentru răspunsuri corecte.

## Când contează RAG

RAG nu este întotdeauna abordarea corectă. Ghidul decizional de mai jos te ajută să determini când RAG adaugă valoare versus când abordările mai simple — cum ar fi includerea conținutului direct în prompt sau bazarea pe cunoștințele încorporate ale modelului — sunt suficiente:

<img src="../../../translated_images/ro/when-to-use-rag.1016223f6fea26bc.webp" alt="Când să folosești RAG" width="800"/>

*Această diagramă arată un ghid decizional pentru când RAG adaugă valoare versus când abordările mai simple sunt suficiente.*

## Pașii următori

**Următorul modul:** [04-tools - Agenți AI cu unelte](../04-tools/README.md)

---

**Navigare:** [← Anterior: Modul 02 - Inginerie Prompt](../02-prompt-engineering/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 04 - Unelte →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru precizie, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm nicio răspundere pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->