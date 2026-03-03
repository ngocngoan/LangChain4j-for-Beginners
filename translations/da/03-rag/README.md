# Modul 03: RAG (Retrieval-Augmented Generation)

## Indholdsfortegnelse

- [Video-gennemgang](../../../03-rag)
- [Hvad du vil lære](../../../03-rag)
- [Forudsætninger](../../../03-rag)
- [Forstå RAG](../../../03-rag)
  - [Hvilken RAG-tilgang bruger denne tutorial?](../../../03-rag)
- [Hvordan det fungerer](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Oprettelse af embeddings](../../../03-rag)
  - [Semantisk søgning](../../../03-rag)
  - [Svar-generering](../../../03-rag)
- [Kør applikationen](../../../03-rag)
- [Brug applikationen](../../../03-rag)
  - [Upload et dokument](../../../03-rag)
  - [Stil spørgsmål](../../../03-rag)
  - [Tjek kildehenvisninger](../../../03-rag)
  - [Eksperimentér med spørgsmål](../../../03-rag)
- [Nøglebegreber](../../../03-rag)
  - [Chunking-strategi](../../../03-rag)
  - [Lighedsscores](../../../03-rag)
  - [I hukommelseslagring](../../../03-rag)
  - [Håndtering af kontekstvindue](../../../03-rag)
- [Hvornår RAG er vigtigt](../../../03-rag)
- [Næste skridt](../../../03-rag)

## Video-gennemgang

Se denne live-session, der forklarer, hvordan du kommer i gang med dette modul:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Hvad du vil lære

I de tidligere moduler lærte du, hvordan du kan have samtaler med AI og strukturere dine prompts effektivt. Men der er en grundlæggende begrænsning: sprogmodeller ved kun, hvad de lærte under træning. De kan ikke svare på spørgsmål om din virksomheds politikker, din projektdokumentation eller anden information, de ikke blev trænet på.

RAG (Retrieval-Augmented Generation) løser dette problem. I stedet for at forsøge at lære modellen din information (hvilket er dyrt og upraktisk), giver du den mulighed for at søge igennem dine dokumenter. Når nogen stiller et spørgsmål, finder systemet relevant information og inkluderer det i prompten. Modellen svarer derefter baseret på den hentede kontekst.

Tænk på RAG som at give modellen et referencebibliotek. Når du stiller et spørgsmål, gør systemet følgende:

1. **Brugerforespørgsel** - Du stiller et spørgsmål  
2. **Embedding** - Konverterer dit spørgsmål til en vektor  
3. **Vektorsøgning** - Finder lignende dokument-bidder  
4. **Kontekstsammensætning** - Tilføjer relevante bidder til prompten  
5. **Svar** - LLM genererer et svar baseret på konteksten

Dette forankrer modellens svar i dine faktiske data i stedet for at stole på dens træningsviden eller finde på svar.

## Forudsætninger

- Færdiggjort [Modul 00 - Quick Start](../00-quick-start/README.md) (til det Easy RAG-eksempel, der refereres til senere i dette modul)  
- Færdiggjort [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-ressourcer implementeret, inklusiv `text-embedding-3-small` embedding-modellen)  
- `.env`-fil i rodmappen med Azure-legitimationsoplysninger (oprettet af `azd up` i Modul 01)

> **Bemærk:** Hvis du ikke har gennemført Modul 01, følg implementeringsinstruktionerne der først. Kommandoen `azd up` implementerer både GPT chatmodellen og embedding-modellen, der bruges i dette modul.

## Forstå RAG

Diagrammet nedenfor illustrerer kernetanken: i stedet for kun at stole på modellens træningsdata, giver RAG den et referencebibliotek af dine dokumenter at konsultere, før den genererer hvert svar.

<img src="../../../translated_images/da/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Dette diagram viser forskellen mellem en standard LLM (der gætter ud fra træningsdata) og en RAG-forbedret LLM (som først konsulterer dine dokumenter).*

Sådan hænger delene sammen fra ende til anden. En brugers spørgsmål gennemløber fire trin — embedding, vektorsøgning, kontekstsammensætning og svar-generering — hvor hver bygger på den forrige:

<img src="../../../translated_images/da/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Dette diagram viser RAG-pipelinen fra ende til anden — en brugerforespørgsel går gennem embedding, vektorsøgning, kontekstsammensætning og svar-generering.*

Resten af dette modul gennemgår hvert trin i detaljer med kode, du kan køre og ændre.

### Hvilken RAG-tilgang bruger denne tutorial?

LangChain4j tilbyder tre måder at implementere RAG på, hver med et forskelligt abstraktionsniveau. Diagrammet nedenfor viser dem side om side:

<img src="../../../translated_images/da/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Dette diagram sammenligner de tre LangChain4j RAG-tilgange — Easy, Native og Advanced — med deres nøglekomponenter og hvornår hver skal bruges.*

| Tilgang | Hvad den gør | Afvejning |
|---|---|---|
| **Easy RAG** | Kobler det hele automatisk via `AiServices` og `ContentRetriever`. Du annoterer et interface, tilknytter en retriever, og LangChain4j håndterer embedding, søgning og prompt-sammensætning bag kulisserne. | Minimal kode, men du ser ikke hvad der sker i hvert trin. |
| **Native RAG** | Du kalder embedding-modellen, søger i butikken, bygger prompten og genererer svaret selv — ét eksplicit trin ad gangen. | Mere kode, men hvert trin er synligt og kan ændres. |
| **Advanced RAG** | Bruger `RetrievalAugmentor`-rammeværket med udskiftelige forespørgsels-transformere, routere, re-rankere og indholdsindsprøjtere til produktionsklare pipelines. | Maksimal fleksibilitet, men betydeligt mere kompleksitet. |

**Denne tutorial bruger Native-tilgangen.** Hvert trin i RAG-pipelinen — embedding af forespørgslen, søgning i vektor-butikken, sammensætning af kontekst og generering af svar — er skrevet eksplicit i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Det er med vilje: som læringsressource er det vigtigere, at du ser og forstår hvert trin, end at koden er minimeret. Når du er fortrolig med, hvordan brikkerne passer sammen, kan du gå videre til Easy RAG for hurtige prototyper eller Advanced RAG til produktionssystemer.

> **💡 Har du allerede set Easy RAG i aktion?** [Quick Start-modulet](../00-quick-start/README.md) indeholder et eksempel med Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), der bruger Easy RAG-tilgangen — LangChain4j håndterer embedding, søgning og prompt-sammensætning automatisk. Dette modul tager næste skridt ved at åbne op for den pipeline, så du kan se og kontrollere hvert trin selv.

Diagrammet nedenfor viser Easy RAG-pipelinen fra det Quick Start-eksempel. Bemærk, hvordan `AiServices` og `EmbeddingStoreContentRetriever` skjuler al kompleksitet — du indlæser et dokument, tilknytter en retriever og får svar. Native-tilgangen i dette modul åbner hvert af disse skjulte trin:

<img src="../../../translated_images/da/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dette diagram viser Easy RAG-pipelinen fra `SimpleReaderDemo.java`. Sammenlign dette med Native-tilgangen, der bruges i dette modul: Easy RAG skjuler embedding, retrieval og prompt-sammensætning bag `AiServices` og `ContentRetriever` — du indlæser et dokument, tilknytter en retriever og får svar. Native-tilgangen i dette modul åbner den pipeline, så du selv kalder hvert trin (embed, søg, saml kontekst, generer), hvilket giver dig fuld synlighed og kontrol.*

## Hvordan det fungerer

RAG-pipelinen i dette modul opdeles i fire trin, som kører i rækkefølge hver gang en bruger stiller et spørgsmål. Først bliver et uploadet dokument **parset og opdelt i bidder** i håndterbare stykker. Disse bidder konverteres derefter til **vektor-embeddings** og gemmes, så de kan sammenlignes matematisk. Når en forespørgsel kommer, udfører systemet en **semantisk søgning** for at finde de mest relevante bidder og sender dem endelig som kontekst til LLM for **svar-generering**. Sektionerne nedenfor gennemgår hvert trin med den faktiske kode og diagrammer. Lad os se på det første trin.

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du uploader et dokument, parser systemet det (PDF eller almindelig tekst), tilknytter metadata såsom filnavn og bryder det så op i bidder — mindre stykker, der passer komfortabelt i modellens kontekstvindue. Disse bidder overlapper lidt, så du ikke mister kontekst ved grænserne.

```java
// Analyser den uploadede fil og pak den ind i et LangChain4j-dokument
Document document = Document.from(content, metadata);

// Del op i 300-token segmenter med 30-token overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet nedenfor viser, hvordan dette fungerer visuelt. Bemærk hvordan hver bid deler nogle tokens med sine naboer — 30-token overlap sikrer, at ingen vigtig kontekst falder mellem sprækkerne:

<img src="../../../translated_images/da/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Dette diagram viser et dokument, der deles op i 300-token bidder med 30-token overlap, hvilket bevarer kontekst ved bid-grænser.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spørg:  
> - "Hvordan splitter LangChain4j dokumenter i bidder og hvorfor er overlap vigtigt?"  
> - "Hvad er den optimale bid-størrelse for forskellige dokumenttyper og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere sprog eller med særlig formatering?"

### Oprettelse af embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hver bid konverteres til en numerisk repræsentation kaldet en embedding — i bund og grund en mening-til-tal-konverter. Embedding-modellen er ikke "intelligent" som en chatmodel; den kan ikke følge instruktioner, ræsonnere eller svare på spørgsmål. Den kan til gengæld kortlægge tekst til et matematisk rum, hvor lignende betydninger lander tæt på hinanden — "bil" tæt på "automobil", "refusionspolitik" tæt på "returner mine penge". Tænk på en chatmodel som en person, du kan tale med; en embedding-model er et ultragodt arkivsystem.

Diagrammet nedenfor visualiserer dette koncept — tekst går ind, numeriske vektorer kommer ud, og lignende betydninger producerer nærliggende vektorer:

<img src="../../../translated_images/da/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Dette diagram viser, hvordan en embedding-model konverterer tekst til numeriske vektorer, hvor lignende betydninger — som "bil" og "automobil" — placeres tæt på hinanden i vektor-rummet.*

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
  
Klassediagrammet nedenfor viser de to separate flows i en RAG-pipeline og de LangChain4j-klasser, der implementerer dem. **Indtagsflowet** (kører én gang ved upload) splitter dokumentet, embedded bidderne og gemmer dem via `.addAll()`. **Forespørgselsflowet** (kører hver gang en bruger spørger) embedded spørgsmålet, søger i butikken via `.search()`, og sender den matchede kontekst til chatmodellen. Begge flows mødes i det delte interface `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/da/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Dette diagram viser de to flows i en RAG-pipeline — indtag og forespørgsel — og hvordan de forbindes gennem en delt EmbeddingStore.*

Når embeddings er gemt, samler lignende indhold sig naturligt i vektor-rummet. Visualiseringen nedenfor viser, hvordan dokumenter om beslægtede emner ender som nærliggende punkter, hvilket muliggør semantisk søgning:

<img src="../../../translated_images/da/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Denne visualisering viser, hvordan relaterede dokumenter samler sig i 3D-vektor-rummet, med emner som Teknisk Docs, Forretningsregler og FAQs, der danner tydelige klynger.*

Når en bruger søger, følger systemet fire trin: embed dokumenterne én gang, embed forespørgslen ved hver søgning, sammenlign forespørgselsvektoren med alle gemte vektorer ved hjælp af cosinus-lighed, og returner de top-K højest scorende bidder. Diagrammet nedenfor gennemgår hvert trin og de involverede LangChain4j-klasser:

<img src="../../../translated_images/da/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Dette diagram viser den firetrins embedding-søgeproces: embed dokumenter, embed forespørgslen, sammenlign vektorer med cosinus-lighed, og returner top-K resultater.*

### Semantisk søgning

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørgsmål, bliver dit spørgsmål også til en embedding. Systemet sammenligner din spørgsmåls embedding med alle dokumentbidders embeddings. Det finder de bidder med mest lignende betydning — ikke bare matchende nøgleord, men faktisk semantisk lighed.

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
  
Diagrammet nedenfor kontrasterer semantisk søgning med traditionel nøgleordssøgning. En nøgleordssøgning på "køretøj" overser en bid om "biler og lastbiler," men semantisk søgning forstår, at de betyder det samme og returnerer den som et højt scorende match:

<img src="../../../translated_images/da/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Dette diagram sammenligner nøgleordsbaseret søgning med semantisk søgning, der viser, hvordan semantisk søgning henter konceptuelt relateret indhold, selv når præcise nøgleord adskiller sig.*
Under overfladen måles lighed ved hjælp af cosinuslighed — i bund og grund spørgsmålet "peger disse to pile i samme retning?" To tekststykker kan bruge helt forskellige ord, men hvis de betyder det samme, peger deres vektorer i samme retning og scorer tæt på 1,0:

<img src="../../../translated_images/da/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Dette diagram illustrerer cosinuslighed som vinklen mellem embeddingsvektorer — jo bedre justerede vektorerne er, desto tættere scorer de på 1,0, hvilket indikerer højere semantisk lighed.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spørg:
> - "Hvordan fungerer søgning efter lighed med embeddings, og hvad bestemmer scoren?"
> - "Hvilken lighedstærskel skal jeg bruge, og hvordan påvirker det resultaterne?"
> - "Hvordan håndterer jeg situationer, hvor der ikke findes relevante dokumenter?"

### Svar Generering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante tekststykker samles i en struktureret prompt, der inkluderer eksplicitte instruktioner, den hentede kontekst og brugerens spørgsmål. Modellen læser disse specifikke tekststykker og svarer baseret på denne information — den kan kun bruge det, der er foran den, hvilket forhindrer hallucination.

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

Diagrammet nedenfor viser denne samling i praksis — de højest scorende tekststykker fra søgningstrinnet indsættes i promptskabelonen, og `OpenAiOfficialChatModel` genererer et funderet svar:

<img src="../../../translated_images/da/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dette diagram viser, hvordan de højest scorende tekststykker samles i en struktureret prompt, så modellen kan generere et funderet svar fra dine data.*

## Kør Applikationen

**Bekræft deployment:**

Sørg for, at `.env` filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01). Kør dette fra modulets mappe (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Note:** Hvis du allerede har startet alle applikationer ved at bruge `./start-all.sh` fra rodmappen (som beskrevet i Modul 01), kører dette modul allerede på port 8081. Du kan springe startkommandoerne over nedenfor og gå direkte til http://localhost:8081.

**Mulighed 1: Brug Spring Boot Dashboard (Anbefalet til VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver et visuelt interface til at administrere alle Spring Boot-applikationer. Du finder det i Activity Bar til venstre i VS Code (søg efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i arbejdsområdet
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationers status

Klik blot på play-knappen ud for "rag" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dette screenshot viser Spring Boot Dashboard i VS Code, hvor du visuelt kan starte, stoppe og overvåge applikationer.*

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodmappen
.\start-all.ps1
```

Eller start kun dette modul:

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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env` fil og bygger JAR-filerne, hvis de ikke findes.

> **Note:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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

Åbn http://localhost:8081 i din browser.

**For at stoppe:**

**Bash:**
```bash
./stop.sh  # Dette modul kun
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Dette modul kun
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Brug af Applikationen

Applikationen tilbyder et webinterface til dokumentupload og spørgsmål.

<a href="images/rag-homepage.png"><img src="../../../translated_images/da/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette screenshot viser RAG-applikationens interface, hvor du uploader dokumenter og stiller spørgsmål.*

### Upload et Dokument

Start med at uploade et dokument – TXT-filer virker bedst til test. En `sample-document.txt` er inkluderet i denne mappe, som indeholder information om LangChain4j-funktioner, RAG-implementering og bedste praksis – perfekt til test af systemet.

Systemet behandler dit dokument, splitter det op i tekststykker og skaber embeddings for hvert stykke. Dette sker automatisk ved upload.

### Stil Spørgsmål

Nu kan du stille specifikke spørgsmål om dokumentets indhold. Prøv noget faktuelt, der klart står i dokumentet. Systemet søger efter relevante tekststykker, inkluderer dem i prompten og genererer et svar.

### Tjek Kildehenvisninger

Bemærk, at hvert svar inkluderer kildehenvisninger med lighedsscores. Disse scores (fra 0 til 1) viser, hvor relevant hvert tekststykke var ift. dit spørgsmål. Højere scores betyder bedre matches. Det giver dig mulighed for at efterprøve svaret mod kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/da/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette screenshot viser forespørgselsresultater med det genererede svar, kildehenvisninger og relevansscores for hvert hentet tekststykke.*

### Eksperimentér med Spørgsmål

Prøv forskellige typer spørgsmål:
- Specifikke fakta: "Hvad er hovedemnet?"
- Sammenligninger: "Hvad er forskellen mellem X og Y?"
- Opsummeringer: "Opsummer hovedpunkterne om Z"

Se, hvordan relevansscores ændrer sig baseret på, hvor godt dit spørgsmål matcher dokumentindholdet.

## Nøglebegreber

### Chunking-strategi

Dokumenter deles op i 300-token tekststykker med 30 tokens overlap. Denne balance sikrer, at hvert stykke har nok kontekst til at være meningsfuldt, samtidig med at stykkerne er små nok til at inkludere flere i en prompt.

### Similarity Scores

Hvert hentet tekststykke kommer med en lighedsscore mellem 0 og 1, der angiver, hvor tæt det matcher brugerens spørgsmål. Diagrammet nedenfor visualiserer score-intervallerne og hvordan systemet bruger dem til at filtrere resultater:

<img src="../../../translated_images/da/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dette diagram viser score-intervaller fra 0 til 1 med en minimumstærskel på 0,5, som filtrerer irrelevante tekststykker fra.*

Scores varierer fra 0 til 1:
- 0,7-1,0: Meget relevant, eksakt match
- 0,5-0,7: Relevant, god kontekst
- Under 0,5: Filtreret fra, for forskelligt

Systemet henter kun tekststykker over minimumstærsklen for at sikre kvalitet.

Embeddings fungerer godt, når betydninger danner klare klynger, men har blinde vinkler. Diagrammet nedenfor viser almindelige fejlsituationer — tekststykker, der er for store, skaber upræcise vektorer, tekststykker, der er for små, mangler kontekst, tvetydige termer peger på flere klynger, og eksakte opslag (ID’er, reservedelsnumre) virker ikke med embeddings overhovedet:

<img src="../../../translated_images/da/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dette diagram viser almindelige embeddings-fejlsituationer: tekststykker for store, tekststykker for små, tvetydige termer, der peger på flere klynger, og eksakte opslag som ID’er.*

### In-Memory Storage

Dette modul bruger hukommelsesbaseret lagring for enkelheds skyld. Ved genstart af applikationen mistes uploadede dokumenter. Produktionssystemer anvender persistente vektordatabaser som Qdrant eller Azure AI Search.

### Context Window Management

Hver model har et maksimum for kontekstvindet. Du kan ikke inkludere alle tekststykker fra et stort dokument. Systemet henter de top N mest relevante tekststykker (standard 5) for at holde sig inden for grænserne og samtidig give nok kontekst til præcise svar.

## Hvornår RAG er vigtigt

RAG er ikke altid den rigtige løsning. Beslutningsguiden nedenfor hjælper dig med at afgøre, hvornår RAG tilfører værdi versus hvornår enklere tilgange — som at inkludere indhold direkte i prompten eller stole på modellens indbyggede viden — er tilstrækkelige:

<img src="../../../translated_images/da/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dette diagram viser en beslutningsguide for, hvornår RAG tilfører værdi, og hvornår enklere tilgange er tilstrækkelige.*

## Næste skridt

**Næste Modul:** [04-tools - AI-agenter med værktøjer](../04-tools/README.md)

---

**Navigation:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbage til start](../README.md) | [Næste: Modul 04 - Værktøjer →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi stræber efter nøjagtighed, skal du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritiske oplysninger anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for eventuelle misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->