# Modulul 03: RAG (Generare Augmentată prin Recuperare)

## Cuprins

- [Parcurgere Video](../../../03-rag)
- [Ce Vei Învăța](../../../03-rag)
- [Precondiții](../../../03-rag)
- [Înțelegerea RAG](../../../03-rag)
  - [Ce Abordare RAG Folosește Acest Tutorial?](../../../03-rag)
- [Cum Funcționează](../../../03-rag)
  - [Procesarea Documentelor](../../../03-rag)
  - [Crearea Embedding-urilor](../../../03-rag)
  - [Căutare Semantică](../../../03-rag)
  - [Generarea Răspunsurilor](../../../03-rag)
- [Rularea Aplicației](../../../03-rag)
- [Utilizarea Aplicației](../../../03-rag)
  - [Încărcarea unui Document](../../../03-rag)
  - [Adresarea de Întrebări](../../../03-rag)
  - [Verificarea Referințelor Sursei](../../../03-rag)
  - [Experimentarea cu Întrebări](../../../03-rag)
- [Concepte Cheie](../../../03-rag)
  - [Strategia de Segmentare](../../../03-rag)
  - [Scoruri de Similaritate](../../../03-rag)
  - [Stocare în Memorie](../../../03-rag)
  - [Managementul Ferestrei de Context](../../../03-rag)
- [Când Contează RAG](../../../03-rag)
- [Pașii Următori](../../../03-rag)

## Parcurgere Video

Urmărește această sesiune live care explică cum să începi cu acest modul: [RAG cu LangChain4j - Sesiune Live](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Ce Vei Învăța

În modulele precedente, ai învățat cum să porți conversații cu AI-ul și să structurezi eficient prompturile tale. Dar există o limitare fundamentală: modelele de limbaj știu doar ce au învățat în timpul antrenamentului. Ele nu pot răspunde la întrebări despre politicile companiei tale, documentația proiectului tău sau orice informație pe care nu au fost antrenate să o cunoască.

RAG (Generare Augmentată prin Recuperare) rezolvă această problemă. În loc să încerci să îi „predai” modelului informațiile tale (ceea ce este costisitor și nepractic), îi oferi capacitatea de a căuta prin documentele tale. Când cineva pune o întrebare, sistemul găsește informațiile relevante și le include în prompt. Modelul răspunde apoi pe baza contextului recuperat.

Gândește-te la RAG ca oferindu-i modelului o bibliotecă de referință. Când pui o întrebare, sistemul:

1. **Interogarea Utilizatorului** – Pui o întrebare
2. **Embedding** – Transformă întrebarea ta într-un vector
3. **Căutare Vectorială** – Găsește fragmente de documente similare
4. **Asamblarea Contextului** – Adaugă fragmentele relevante în prompt
5. **Răspuns** – LLM generează un răspuns pe baza contextului

Astfel, răspunsurile modelului sunt ancorate în datele tale reale, în loc să se bazeze doar pe cunoștințele din antrenament sau să inventeze răspunsuri.

## Precondiții

- Modulul [00 - Pornire Rapidă](../00-quick-start/README.md) finalizat (pentru exemplul Easy RAG menționat mai sus)
- Modulul [01 - Introducere](../01-introduction/README.md) finalizat (resurse Azure OpenAI desfășurate, inclusiv modelul embedding `text-embedding-3-small`)
- Fișier `.env` în directorul rădăcină cu credențiale Azure (creat de `azd up` în Modulul 01)

> **Notă:** Dacă nu ai finalizat Modulul 01, urmează mai întâi instrucțiunile de implementare de acolo. Comanda `azd up` implementează atât modelul GPT de chat, cât și modelul embedding folosit în acest modul.

## Înțelegerea RAG

Diagrama de mai jos ilustrează conceptul de bază: în loc să se bazeze doar pe datele de antrenament ale modelului, RAG îi oferă o bibliotecă de referință cu documentele tale, pe care să le consulte înainte de a genera fiecare răspuns.

<img src="../../../translated_images/ro/what-is-rag.1f9005d44b07f2d8.webp" alt="Ce este RAG" width="800"/>

*Această diagramă arată diferența dintre un LLM standard (care ghicește din datele de antrenament) și un LLM augmentat cu RAG (care consultă mai întâi documentele tale).*

Iată cum se leagă piesele de la un capăt la altul. Întrebarea unui utilizator trece prin patru etape — embedding, căutare vectorială, asamblare context și generare răspuns — fiecare clădind pe cea precedentă:

<img src="../../../translated_images/ro/rag-architecture.ccb53b71a6ce407f.webp" alt="Arhitectura RAG" width="800"/>

*Această diagramă arată pipeline-ul RAG de la un capăt la altul — o interogare a utilizatorului trece prin embedding, căutare vectorială, asamblare context și generare răspuns.*

Restul acestui modul parcurge fiecare etapă în detaliu, cu cod pe care îl poți rula și modifica.

### Ce Abordare RAG Folosește Acest Tutorial?

LangChain4j oferă trei metode de implementare RAG, fiecare cu un nivel diferit de abstracție. Diagrama de mai jos le compară una lângă alta:

<img src="../../../translated_images/ro/rag-approaches.5b97fdcc626f1447.webp" alt="Trei Abordări RAG în LangChain4j" width="800"/>

*Această diagramă compară cele trei abordări LangChain4j pentru RAG — Easy, Native și Advanced — arătând componentele cheie și când să folosești fiecare.*

| Abordare | Ce Face | Compromis |
|---|---|---|
| **Easy RAG** | Leagă totul automat prin `AiServices` și `ContentRetriever`. Anotezi o interfață, atașezi un retriever și LangChain4j se ocupă de embedding, căutare și asamblarea promptului în fundal. | Cod minim, dar nu vezi ce se întâmplă la fiecare pas. |
| **Native RAG** | Apelezi modelul embedding, cauți în depozit, construiești promptul și generezi răspunsul singur — un pas explicit după altul. | Mai mult cod, dar fiecare etapă este vizibilă și modificabilă. |
| **Advanced RAG** | Folosește cadrul `RetrievalAugmentor` cu transformatori de interogări, routere, re-rankeri și injectoare de conținut pentru pipeline-uri de producție. | Flexibilitate maximă, dar multă complexitate. |

**Acest tutorial folosește abordarea Native.** Fiecare pas din pipeline-ul RAG — embedding-ul interogării, căutarea în magazinul vectorial, asamblarea contextului și generarea răspunsului — este scris explicit în [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Aceasta este intenționat: ca resursă de învățare, este mai important să vezi și să înțelegi fiecare etapă decât să minimalizezi codul. Odată ce ești confortabil cu cum se leagă piesele, poți trece la Easy RAG pentru prototipuri rapide sau Advanced RAG pentru sisteme de producție.

> **💡 Ai văzut deja Easy RAG în acțiune?** Modulul [Pornire Rapidă](../00-quick-start/README.md) include un exemplu Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) care folosește abordarea Easy RAG — LangChain4j se ocupă automat de embedding, căutare și asamblarea promptului. Acest modul face pasul următor, deschizând pipeline-ul astfel încât să poți vedea și controla fiecare etapă singur.

<img src="../../../translated_images/ro/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Această diagramă arată pipeline-ul Easy RAG din `SimpleReaderDemo.java`. Compară-l cu abordarea Native folosită în acest modul: Easy RAG ascunde embedding-ul, recuperarea și asamblarea promptului în `AiServices` și `ContentRetriever` — încarci un document, atașezi un retriever și primești răspunsuri. Abordarea Native din acest modul deschide pipeline-ul astfel încât să apelezi fiecare etapă (embedding, căutare, asamblare context, generare) singur, oferindu-ți vizibilitate și control complet.*

## Cum Funcționează

Pipeline-ul RAG în acest modul se descompune în patru etape care rulează în secvență de fiecare dată când un utilizator pune o întrebare. Mai întâi, un document încărcat este **parsat și segmentat** în bucăți gestionabile. Acelora li se creează apoi **embedding-uri vectoriale** și sunt stocate pentru a putea fi comparate matematic. Când ajunge o interogare, sistemul efectuează o **căutare semantică** pentru a găsi cele mai relevante bucăți și, în final, le pasează ca context modelului LLM pentru **generarea răspunsului**. Secțiunile de mai jos parcurg fiecare etapă cu cod efectiv și diagrame. Să vedem primul pas.

### Procesarea Documentelor

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Când încarci un document, sistemul îl parsează (PDF sau text simplu), atașează metadate precum numele fișierului și apoi îl împarte în fragmente — bucăți mai mici care se încadrează confortabil în fereastra de context a modelului. Fragmentele se suprapun ușor pentru a nu pierde context la margini.

```java
// Analizează fișierul încărcat și încadrează-l într-un Document LangChain4j
Document document = Document.from(content, metadata);

// Împarte în bucăți de 300 de tokeni cu o suprapunere de 30 de tokeni
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagrama de mai jos arată cum funcționează aceasta vizual. Observă cum fiecare fragment împarte câțiva tokeni cu vecinii săi — suprapunerea de 30 tokeni asigură că niciun context important nu se pierde printre fisuri:

<img src="../../../translated_images/ro/document-chunking.a5df1dd1383431ed.webp" alt="Segmentarea Documentului" width="800"/>

*Această diagramă arată un document împărțit în fragmente de 300 de tokeni cu o suprapunere de 30 de tokeni, păstrând contextul la marginea fragmentelor.*

> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) și întreabă:
> - "Cum împarte LangChain4j documentele în fragmente și de ce e importantă suprapunerea?"
> - "Care este dimensiunea optimă a fragmentelor pentru diferite tipuri de documente și de ce?"
> - "Cum gestionez documente în mai multe limbi sau cu formatare specială?"

### Crearea Embedding-urilor

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Fiecare fragment este transformat într-o reprezentare numerică numită embedding — practic un convertor de semnificații în numere. Modelul de embedding nu este „inteligent” ca modelul de chat; nu poate urma instrucțiuni, raționa sau răspunde la întrebări. Ce poate face este să mapaze textul într-un spațiu matematic unde semnificații similare sunt aproape unele de altele — „mașină” aproape de „autovehicul”, „politica de rambursare” aproape de „returnează-mi banii”. Gândește-te la modelul de chat ca la o persoană cu care poți conversa; modelul embedding este un sistem de arhivare ultra-bun.

<img src="../../../translated_images/ro/embedding-model-concept.90760790c336a705.webp" alt="Concept Model Embedding" width="800"/>

*Această diagramă arată cum un model embedding convertește textul în vectori numerici, plasând semnificații similare — precum „mașină” și „autovehicul” — aproape unul de altul în spațiul vectorial.*

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

Diagrama de clasă de mai jos arată cele două fluxuri separate dintr-un pipeline RAG și clasele LangChain4j care le implementează. Fluxul de **ingestie** (rulează o singură dată la încărcare) împarte documentul, creează embedding-ul fragmentelor și le stochează prin `.addAll()`. Fluxul de **interogare** (rulează de fiecare dată când un utilizator întreabă) creează embedding-ul întrebării, caută în depozit prin `.search()` și transmite contextul potrivit către modelul de chat. Ambele fluxuri se întâlnesc la interfața comună `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/ro/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Clase LangChain4j RAG" width="800"/>

*Această diagramă arată cele două fluxuri dintr-un pipeline RAG — ingestie și interogare — și cum se conectează prin intermediul unui EmbeddingStore comun.*

Odată ce embedding-urile sunt stocate, conținutul similar se grupează natural în spațiul vectorial. Vizualizarea de mai jos arată cum documentele despre subiecte înrudite se adună în puncte apropiate, ceea ce face posibilă căutarea semantică:

<img src="../../../translated_images/ro/vector-embeddings.2ef7bdddac79a327.webp" alt="Spațiu Embedding Vectorial" width="800"/>

*Această vizualizare arată cum documentele înrudite se grupează în spațiul vectorial 3D, cu subiecte precum Documentație Tehnică, Reguli de Business și Întrebări Frecvente formând grupuri distincte.*

Când un utilizator caută, sistemul urmează patru pași: face embedding documentelor o singură dată, face embedding întrebării la fiecare căutare, compară vectorul întrebării cu toți vectorii stocați folosind similaritatea cosinus și returnează cele mai bine punctate fragmentelor top-K. Diagrama de mai jos parcurge fiecare pas și clasele LangChain4j implicate:

<img src="../../../translated_images/ro/embedding-search-steps.f54c907b3c5b4332.webp" alt="Pași Căutare Embedding" width="800"/>

*Această diagramă arată procesul în patru pași al căutării embedding: embed documente, embed interogare, compară vectorii prin similaritatea cosinus și returnează rezultatele top-K.*

### Căutare Semantică

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Când pui o întrebare, și întrebarea ta devine un embedding. Sistemul compară embedding-ul întrebării tale cu embedding-urile tuturor fragmentelor de document. Găsește fragmentele cu sensuri cele mai similare — nu doar cuvinte cheie care se potrivesc, ci similitudine semantică reală.

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

Diagrama de mai jos contrastează căutarea semantică cu căutarea tradițională bazată pe cuvinte cheie. O căutare cuvânt cheie pentru „vehicul” ratează un fragment despre „mașini și camioane”, dar căutarea semantică înțelege că acestea înseamnă același lucru și îl returnează ca o potrivire bine punctată:

<img src="../../../translated_images/ro/semantic-search.6b790f21c86b849d.webp" alt="Căutare Semantică" width="800"/>

*Această diagramă compară căutarea bazată pe cuvinte cheie cu căutarea semantică, arătând cum căutarea semantică returnează conținut conceptual legat chiar și când cuvintele cheie exacte diferă.*

În culise, similaritatea se măsoară folosind similaritatea cosinus — practic întrebând „arată acești doi săgeți în aceeași direcție?” Două fragmente pot folosi cuvinte complet diferite, dar dacă înseamnă același lucru vectorii lor arată în aceeași direcție și au scor apropiat de 1,0:

<img src="../../../translated_images/ro/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similaritate Cosinus" width="800"/>

*Această diagramă ilustrează similaritatea cosinus ca unghiul dintre vectorii embedding — vectori mai aliniați au scor mai aproape de 1,0, indicând o semnificație semantică mai mare.*
> **🤖 Încearcă cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschide [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) și întreabă:
> - "Cum funcționează căutarea după similaritate cu embeddings și ce determină scorul?"
> - "Ce prag de similaritate ar trebui să folosesc și cum afectează rezultatele?"
> - "Cum gestionez cazurile în care nu sunt găsite documente relevante?"

### Generarea răspunsului

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Cele mai relevante fragmente sunt asamblate într-un prompt structurat care include instrucțiuni explicite, contextul recuperat și întrebarea utilizatorului. Modelul citește acele fragmente specifice și răspunde pe baza acelor informații — poate folosi doar ceea ce are în față, ceea ce previne halucinațiile.

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

Diagrama de mai jos arată această asamblare în acțiune — fragmentele cu cele mai bune scoruri din pasul de căutare sunt injectate în șablonul de prompt, iar `OpenAiOfficialChatModel` generează un răspuns fundamentat:

<img src="../../../translated_images/ro/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Această diagramă arată cum fragmentele cu cele mai bune scoruri sunt asamblate într-un prompt structurat, permițând modelului să genereze un răspuns fundamentat pe datele tale.*

## Rulează aplicația

**Verifică implementarea:**

Asigură-te că fișierul `.env` există în directorul rădăcină cu credențiale Azure (creat în Modulul 01):

**Bash:**
```bash
cat ../.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Ar trebui să afișeze AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pornește aplicația:**

> **Notă:** Dacă ai pornit deja toate aplicațiile folosind `./start-all.sh` din Modulul 01, acest modul rulează deja pe portul 8081. Poți sări peste comenzile de pornire de mai jos și să accesezi direct http://localhost:8081.

**Opțiunea 1: Folosind Spring Boot Dashboard (Recomandat pentru utilizatorii VS Code)**

Containerul de dezvoltare include extensia Spring Boot Dashboard, care oferă o interfață vizuală pentru gestionarea tuturor aplicațiilor Spring Boot. O poți găsi în bara de activități din partea stângă a VS Code (caută iconița Spring Boot).

Din Spring Boot Dashboard, poți:
- Vizualiza toate aplicațiile Spring Boot disponibile în spațiul de lucru
- Porni/opri aplicațiile cu un singur clic
- Vizualiza log-urile aplicațiilor în timp real
- Monitoriza starea aplicațiilor

Pur și simplu apasă butonul de redare lângă "rag" pentru a porni acest modul sau pornește toate modulele dintr-o dată.

<img src="../../../translated_images/ro/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Acest captură de ecran arată Spring Boot Dashboard în VS Code, unde poți porni, opri și monitoriza aplicațiile vizual.*

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

> **Notă:** Dacă preferi să construiești toate modulele manual înainte de pornire:
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

Deschide în browser http://localhost:8081.

**Pentru a opri:**

**Bash:**
```bash
./stop.sh  # Doar acest modul
# Sau
cd .. && ./stop-all.sh  # Toate modulele
```

**PowerShell:**
```powershell
.\stop.ps1  # Numai acest modul
# Sau
cd ..; .\stop-all.ps1  # Toate modulele
```

## Folosirea aplicației

Aplicația oferă o interfață web pentru încărcarea documentelor și adresarea de întrebări.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ro/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interfața aplicației RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Această captură de ecran prezintă interfața aplicației RAG unde încarci documente și pui întrebări.*

### Încarcă un document

Începe prin a încărca un document - fișierele TXT funcționează cel mai bine pentru testare. Este furnizat un fișier `sample-document.txt` în acest director care conține informații despre funcționalitățile LangChain4j, implementarea RAG și bune practici - perfect pentru testarea sistemului.

Sistemul procesează documentul tău, îl împarte în fragmente și creează embeddings pentru fiecare fragment. Aceasta se întâmplă automat la încărcare.

### Pune întrebări

Acum adresează întrebări specifice despre conținutul documentului. Încearcă ceva factual, care este clar exprimat în document. Sistemul caută fragmente relevante, le include în prompt și generează un răspuns.

### Verifică referințele surselor

Observă că fiecare răspuns include referințe la surse cu scoruri de similaritate. Aceste scoruri (de la 0 la 1) arată cât de relevante au fost fragmentele pentru întrebarea ta. Scorurile mai mari înseamnă potriviri mai bune. Astfel poți verifica răspunsul față de materialul sursă.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ro/rag-query-results.6d69fcec5397f355.webp" alt="Rezultatele interogării RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Această captură de ecran arată rezultatele interogării cu răspunsul generat, referințele sursă și scorurile de relevanță pentru fiecare fragment recuperat.*

### Experimentează cu întrebările

Încearcă diferite tipuri de întrebări:
- Fapte specifice: "Care este subiectul principal?"
- Comparații: "Care este diferența dintre X și Y?"
- Rezumate: "Rezumați punctele cheie despre Z"

Urmărește cum se modifică scorurile de relevanță în funcție de cât de bine se potrivește întrebarea ta cu conținutul documentului.

## Concepte cheie

### Strategia de fragmentare

Documentele sunt împărțite în fragmente de 300 de tokeni cu o suprapunere de 30 de tokeni. Acest echilibru asigură că fiecare fragment are suficient context pentru a fi semnificativ, păstrând totuși fragmentele suficient de mici pentru a putea include mai multe fragmente într-un prompt.

### Scorurile de similaritate

Fiecare fragment recuperat vine cu un scor de similaritate între 0 și 1 care indică cât de bine se potrivește cu întrebarea utilizatorului. Diagrama de mai jos vizualizează intervalele de scor și modul în care sistemul le folosește pentru a filtra rezultatele:

<img src="../../../translated_images/ro/similarity-scores.b0716aa911abf7f0.webp" alt="Scoruri de similaritate" width="800"/>

*Această diagramă arată intervalele de scor de la 0 la 1, cu un prag minim de 0.5 care filtrează fragmentele irelevante.*

Scorurile variază între 0 și 1:
- 0.7-1.0: Foarte relevante, potrivire exactă
- 0.5-0.7: Relevante, context bun
- Sub 0.5: Filtrate, prea disimilare

Sistemul recuperează doar fragmentele de deasupra pragului minim pentru a asigura calitatea.

Embeddings funcționează bine când sensul se grupează clar, dar au puncte oarbe. Diagrama de mai jos arată modurile comune de eșec — fragmente prea mari produc vectori confuzi, fragmente prea mici lipsesc de context, termenii ambigui indică către multiple grupuri, iar căutările după potriviri exacte (ID-uri, numere de piesă) nu funcționează deloc cu embeddings:

<img src="../../../translated_images/ro/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Moduri de eșec ale embedding-urilor" width="800"/>

*Această diagramă arată modurile comune de eșec ale embedding-urilor: fragmente prea mari, fragmente prea mici, termeni ambigui care duc la mai multe grupuri și căutări după potriviri exacte ca ID-uri.*

### Stocare în memorie

Acest modul folosește stocare în memorie pentru simplitate. Când repornești aplicația, documentele încărcate se pierd. Sistemele de producție folosesc baze de date vectoriale persistente precum Qdrant sau Azure AI Search.

### Gestionarea ferestrei de context

Fiecare model are o fereastră maximă de context. Nu poți include toate fragmentele dintr-un document mare. Sistemul recuperează N cele mai relevante fragmente (implicit 5) pentru a rămâne în limite și a oferi suficient context pentru răspunsuri exacte.

## Când contează RAG

RAG nu este întotdeauna abordarea corectă. Ghidul de decizie de mai jos te ajută să determini când RAG adaugă valoare versus când abordările mai simple — cum ar fi includerea conținutului direct în prompt sau utilizarea cunoștințelor încorporate ale modelului — sunt suficiente:

<img src="../../../translated_images/ro/when-to-use-rag.1016223f6fea26bc.webp" alt="Când să folosești RAG" width="800"/>

*Această diagramă arată un ghid de decizie pentru când RAG adaugă valoare versus când abordările mai simple sunt suficiente.*

**Folosește RAG când:**
- Răspunzi la întrebări despre documente proprietare
- Informațiile se schimbă frecvent (politici, prețuri, specificații)
- Acurațea necesită atribuirea sursei
- Conținutul este prea mare pentru a încăpea într-un singur prompt
- Ai nevoie de răspunsuri verificabile și fundamentate

**Nu folosi RAG când:**
- Întrebările necesită cunoștințe generale pe care modelul le are deja
- Ai nevoie de date în timp real (RAG funcționează pe documente încărcate)
- Conținutul este suficient de mic pentru a fi inclus direct în prompturi

## Pașii următori

**Modul următor:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigare:** [← Anterior: Modul 02 - Inginerie de prompt](../02-prompt-engineering/README.md) | [Înapoi la Principal](../README.md) | [Următor: Modul 04 - Unelte →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să fiți conștienți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un traducător uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea în urma utilizării acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->