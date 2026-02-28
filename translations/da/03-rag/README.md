# Modul 03: RAG (Retrieval-Augmented Generation)

## Indholdsfortegnelse

- [Video Gennemgang](../../../03-rag)
- [Hvad Du Vil Lære](../../../03-rag)
- [Forudsætninger](../../../03-rag)
- [Forståelse af RAG](../../../03-rag)
  - [Hvilken RAG-tilgang Bruger Denne Tutorial?](../../../03-rag)
- [Hvordan Det Virker](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Oprettelse af Embeddings](../../../03-rag)
  - [Semantisk Søgning](../../../03-rag)
  - [Svar Generering](../../../03-rag)
- [Kør Applikationen](../../../03-rag)
- [Brug af Applikationen](../../../03-rag)
  - [Upload et Dokument](../../../03-rag)
  - [Stil Spørgsmål](../../../03-rag)
  - [Tjek Kildehenvisninger](../../../03-rag)
  - [Eksperimenter med Spørgsmål](../../../03-rag)
- [Nøglebegreber](../../../03-rag)
  - [Chunking Strategi](../../../03-rag)
  - [Lighedsscores](../../../03-rag)
  - [I Hukommelsen Opbevaring](../../../03-rag)
  - [Håndtering af Kontekstvindue](../../../03-rag)
- [Hvornår RAG Er Vigtigt](../../../03-rag)
- [Næste Skridt](../../../03-rag)

## Video Gennemgang

Se denne live session, der forklarer, hvordan du kommer i gang med dette modul:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG med LangChain4j - Live Session" width="800"/></a>

## Hvad Du Vil Lære

I de tidligere moduler lærte du, hvordan man har samtaler med AI og strukturerer dine prompts effektivt. Men der er en grundlæggende begrænsning: sprogmodeller kender kun det, de lærte under træningen. De kan ikke besvare spørgsmål om din virksomheds politikker, din projektdokumentation eller nogen information, de ikke er trænet på.

RAG (Retrieval-Augmented Generation) løser dette problem. I stedet for at prøve at lære modellen dine oplysninger (hvilket er dyrt og upraktisk), giver du den mulighed for at søge i dine dokumenter. Når nogen stiller et spørgsmål, finder systemet relevant information og inkluderer det i prompten. Modellen svarer derefter baseret på den hentede kontekst.

Tænk på RAG som at give modellen et referencebibliotek. Når du stiller et spørgsmål, gør systemet:

1. **Brugerforespørgsel** - Du stiller et spørgsmål  
2. **Embedding** - Konverterer dit spørgsmål til en vektor  
3. **Vektorsøgning** - Finder lignende dokumentstykker  
4. **Kontekstsamling** - Tilføjer relevante stykker til prompten  
5. **Svar** - LLM genererer et svar baseret på konteksten  

Dette forankrer modellens svar i dine faktiske data i stedet for at stole på dens træningsviden eller finde på svar.

## Forudsætninger

- Gennemført [Modul 00 - Hurtig Start](../00-quick-start/README.md) (for det nemme RAG-eksempel nævnt ovenfor)  
- Gennemført [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-ressourcer distribueret, inklusive embedding-modellen `text-embedding-3-small`)  
- `.env` fil i rodmappen med Azure-legitimationsoplysninger (oprettet via `azd up` i Modul 01)  

> **Bemærk:** Hvis du ikke har gennemført Modul 01, følg først implementeringsinstruktionerne dér. Kommandoen `azd up` implementerer både GPT chat-modellen og embedding-modellen, som dette modul bruger.

## Forståelse af RAG

Diagrammet nedenfor illustrerer kernekonceptet: i stedet for kun at stole på modellens træningsdata, giver RAG den et referencebibliotek af dine dokumenter, som den kan konsultere, inden den genererer hvert svar.

<img src="../../../translated_images/da/what-is-rag.1f9005d44b07f2d8.webp" alt="Hvad er RAG" width="800"/>

*Dette diagram viser forskellen mellem en standard LLM (der gætter ud fra træningsdata) og en RAG-forstærket LLM (der først konsulterer dine dokumenter).*

Her er, hvordan delene forbindes end-to-end. En brugers spørgsmål flyder gennem fire faser — embedding, vektorsøgning, kontekstsamling og svar generering — hver bygger oven på den foregående:

<img src="../../../translated_images/da/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

*Dette diagram viser den end-to-end RAG pipeline — en brugerforespørgsel flyder gennem embedding, vektorsøgning, kontekstsamling og svar generering.*

Resten af dette modul gennemgår hver fase i detaljer med kode, som du kan køre og modificere.

### Hvilken RAG-tilgang Bruger Denne Tutorial?

LangChain4j tilbyder tre måder at implementere RAG på, hver med et forskelligt abstraktionsniveau. Diagrammet nedenfor sammenligner dem side om side:

<img src="../../../translated_images/da/rag-approaches.5b97fdcc626f1447.webp" alt="Tre RAG Tilgange i LangChain4j" width="800"/>

*Dette diagram sammenligner de tre LangChain4j RAG-tilgange — Easy, Native og Advanced — og viser deres nøglekomponenter og hvornår man skal bruge hver.*

| Tilgang | Hvad Den Gør | Kompromis |
|---|---|---|
| **Easy RAG** | Kobler alt automatisk gennem `AiServices` og `ContentRetriever`. Du annoterer et interface, tilknytter en retriever, og LangChain4j håndterer embedding, søgning og promptsamling bag kulisserne. | Minimal kode, men du ser ikke, hvad der sker i hvert trin. |
| **Native RAG** | Du kalder embedding-modellen, søger i lageret, bygger prompten og genererer svaret selv — ét eksplicit trin ad gangen. | Mere kode, men hvert trin er synligt og kan ændres. |
| **Advanced RAG** | Bruger `RetrievalAugmentor`-framework med udskiftelige forespørgselsomformere, routings, re-rankers og indholdsindsprøjtere til produktionsklar pipeline. | Maksimal fleksibilitet, men betydeligt mere kompleksitet. |

**Denne tutorial bruger den Native tilgang.** Hvert trin i RAG-pipelinen — embedding af forespørgslen, søgning i vektorbutikken, samling af kontekst og generering af svar — er skrevet eksplicit i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Det er med vilje: som læringsressource er det vigtigere, at du ser og forstår hvert trin, end at koden minimeres. Når du er komfortabel med, hvordan delene passer sammen, kan du skifte til Easy RAG for hurtige prototyper eller Advanced RAG til produktsystemer.

> **💡 Har du allerede set Easy RAG i aktion?** [Hurtig Start-modulet](../00-quick-start/README.md) indeholder et Document Q&A-eksempel ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)), der bruger Easy RAG-tilgangen — LangChain4j håndterer embedding, søgning og promptsamling automatisk. Dette modul tager næste skridt ved at åbne pipelinen, så du selv kan se og kontrollere hvert trin.

<img src="../../../translated_images/da/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dette diagram viser Easy RAG-pipelinen fra `SimpleReaderDemo.java`. Sammenlign med den Native tilgang, der bruges i dette modul: Easy RAG skjuler embedding, hentning og promptsamling bag `AiServices` og `ContentRetriever` — du læser et dokument ind, tilknytter en retriever og får svar. Native-tilgangen i dette modul bryder pipelinen op, så du selv kalder hvert trin (embed, søg, saml kontekst, generer), hvilket giver fuld synlighed og kontrol.*

## Hvordan Det Virker

RAG-pipelinen i dette modul opdeles i fire faser, der køres sekventielt hver gang en bruger stiller et spørgsmål. Først bliver et uploadet dokument **parset og opdelt i stykker** i håndterbare bidder. Disse bidder bliver derefter konverteret til **vektor-embeddings** og gemt, så de kan sammenlignes matematisk. Når en forespørgsel kommer, udfører systemet en **semantisk søgning** for at finde de mest relevante bidder og til sidst giver dem som kontekst til LLM'en til **svar generering**. Sektionerne nedenfor gennemgår hver fase med den faktiske kode og diagrammer. Lad os se på det første trin.

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du uploader et dokument, parser systemet det (PDF eller almindelig tekst), tilknytter metadata såsom filnavn og deler det derefter op i bidder — mindre stykker, der passer komfortabelt i modellens kontekstvindue. Disse bidder overlapper lidt, så du ikke mister kontekst ved grænserne.

```java
// Analyser den uploadede fil og pak den ind i et LangChain4j Document
Document document = Document.from(content, metadata);

// Opdel i 300-token segmenter med 30-token overlapning
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet nedenfor viser, hvordan dette fungerer visuelt. Bemærk hvordan hver bid deler nogle tokens med sine naboer — den 30-token overlappelse sikrer, at vigtig kontekst ikke går tabt mellem stykkerne:

<img src="../../../translated_images/da/document-chunking.a5df1dd1383431ed.webp" alt="Dokument Opdeling" width="800"/>

*Dette diagram viser et dokument, der opdeles i 300-token-bidder med 30-token overlappelse, hvilket bevarer konteksten ved bid-grænser.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spørg:  
> - "Hvordan deler LangChain4j dokumenter i bidder, og hvorfor er overlap vigtigt?"  
> - "Hvad er den optimale bid-størrelse for forskellige dokumenttyper, og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere sprog eller med særlig formatering?"

### Oprettelse af Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hver bid bliver konverteret til en numerisk repræsentation kaldet en embedding — dybest set en mening-til-tal-konverter. Embedding-modellen er ikke "intelligent" som en chatmodel; den kan ikke følge instruktioner, ræsonnere eller besvare spørgsmål. Det den kan, er at kortlægge tekst ind i et matematisk rum, hvor lignende betydninger lander tæt på hinanden — "bil" tæt på "automobil," "refunderingspolitik" tæt på "returnér mine penge." Tænk på en chatmodel som en person, du kan tale med; en embedding-model er et ultra-godt arkivsystem.

<img src="../../../translated_images/da/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Koncept" width="800"/>

*Dette diagram viser, hvordan en embedding-model konverterer tekst til numeriske vektorer og placerer lignende betydninger — som "bil" og "automobil" — tæt på hinanden i vektorrummet.*

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
  
Klassediagrammet nedenfor viser de to separate strømme i en RAG-pipeline og de LangChain4j klasser, der implementerer dem. **Indtagelsesstrømmen** (kører én gang ved upload) deler dokumentet op, embedder stykkerne og gemmer dem via `.addAll()`. **Forespørgselsstrømmen** (kører hver gang en bruger spørger) embedder spørgsmålet, søger i lageret via `.search()` og sender den matchede kontekst til chatmodellen. Begge strømme mødes ved det delte interface `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/da/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klasser" width="800"/>

*Dette diagram viser de to strømme i en RAG-pipeline — indtagelse og forespørgsel — og hvordan de forbindes via et delt EmbeddingStore.*

Når embeddings er gemt, grupperer lignende indhold sig naturligt tæt sammen i vektorrummet. Visualiseringen nedenfor viser, hvordan dokumenter om relaterede emner ender som nærliggende punkter, hvilket muliggør semantisk søgning:

<img src="../../../translated_images/da/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor-Embeddings Rum" width="800"/>

*Denne visualisering viser, hvordan relaterede dokumenter klynges sammen i 3D-vektorrum med emner som Teknisk Dokumentation, Forretningsregler og FAQ'er, der danner distinkte grupper.*

Når en bruger søger, følger systemet fire trin: embedder dokumenterne én gang, embedder forespørgslen ved hvert opslag, sammenligner forespørgselsvektoren med alle lagrede vektorer ved hjælp af cosinus-lighed, og returnerer de top-K højst scorende bidder. Diagrammet nedenfor gennemgår hvert trin og de involverede LangChain4j-klasser:

<img src="../../../translated_images/da/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Søgningstrin" width="800"/>

*Dette diagram viser fire-trins processen for embedding-søgning: embed dokumenter, embed forespørgsel, sammenlign vektorer med cosinus-lighed, og returner top-K resultater.*

### Semantisk Søgning

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørgsmål, bliver dit spørgsmål også embedget. Systemet sammenligner spørgsmålets embedding med alle dokumentbidders embeddings. Det finder bidder med den mest lignende betydning — ikke kun nøgleord, men faktisk semantisk lighed.

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
  
Diagrammet nedenfor sætter semantisk søgning op imod traditionel nøgleordssøgning. En nøgleordssøgning efter "køretøj" overser et stykke om "biler og lastbiler", men semantisk søgning forstår, at de betyder det samme og returnerer det som et højt scorende match:

<img src="../../../translated_images/da/semantic-search.6b790f21c86b849d.webp" alt="Semantisk Søgning" width="800"/>

*Dette diagram sammenligner nøgleordsbaseret søgning med semantisk søgning og viser, hvordan semantisk søgning henter konceptuelt relateret indhold, selv når præcise nøgleord adskiller sig.*

Under motorhjelmen måles lighed ved hjælp af cosinus-lighed — i bund og grund det samme som at spørge: "Peges disse to pile i samme retning?" To bidder kan bruge helt forskellige ord, men hvis de betyder det samme, peger deres vektorer i samme retning og scorer tæt på 1,0:

<img src="../../../translated_images/da/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosinus Lighed" width="800"/>
*Dette diagram illustrerer cosinus-lighed som vinklen mellem indlejringsvektorer — mere justerede vektorer scorer tættere på 1,0, hvilket indikerer højere semantisk lighed.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spørg:
> - "Hvordan fungerer søgning efter lighed med indlejringer, og hvad bestemmer scoren?"
> - "Hvilken lighedstærskel skal jeg bruge, og hvordan påvirker det resultaterne?"
> - "Hvordan håndterer jeg tilfælde, hvor der ikke findes relevante dokumenter?"

### Besvarelsesgenerering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante stykker samles i en struktureret prompt, der inkluderer eksplicitte instruktioner, den hentede kontekst og brugerens spørgsmål. Modellen læser disse specifikke stykker og svarer baseret på disse oplysninger — den kan kun bruge det, der er foran den, hvilket forhindrer hallucination.

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

Diagrammet nedenfor viser denne samling i praksis — de højest scorende stykker fra søgetrinnet indsættes i prompt-skabelonen, og `OpenAiOfficialChatModel` genererer et velbegrundet svar:

<img src="../../../translated_images/da/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dette diagram viser, hvordan de højest scorende stykker samles i en struktureret prompt, hvilket giver modellen mulighed for at generere et velbegrundet svar ud fra dine data.*

## Kør applikationen

**Bekræft udrulning:**

Sørg for, at `.env`-filen findes i rodmappen med Azure-legitimationsoplysninger (oprettet i Modul 01):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Bemærk:** Hvis du allerede har startet alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8081. Du kan springe startkommandoerne over nedenfor og gå direkte til http://localhost:8081.

**Mulighed 1: Brug af Spring Boot Dashboard (anbefalet til VS Code-brugere)**

Dev-containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at administrere alle Spring Boot-applikationer. Du finder den i Aktivitetslinjen til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i workspace
- Starte/stoppe applikationer med et enkelt klik
- Se applikationslogs i realtid
- Overvåge applikationsstatus

Klik blot på afspilningsknappen ud for "rag" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dette skærmbillede viser Spring Boot Dashboard i VS Code, hvor du visuelt kan starte, stoppe og overvåge applikationer.*

**Mulighed 2: Brug af shell-scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodbiblioteket
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

Begge scripts indlæser automatisk miljøvariabler fra rodens `.env`-fil og bygger JAR-filerne, hvis de ikke findes.

> **Bemærk:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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
./stop.sh  # Kun denne modul
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modul
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Brug af applikationen

Applikationen tilbyder en webgrænseflade til dokumentupload og spørgsmål.

<a href="images/rag-homepage.png"><img src="../../../translated_images/da/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skærmbillede viser RAG-applikationsgrænsefladen, hvor du kan uploade dokumenter og stille spørgsmål.*

### Upload et dokument

Start med at uploade et dokument - TXT-filer fungerer bedst til test. En `sample-document.txt` er tilgængelig i denne mappe, som indeholder information om LangChain4j-funktioner, RAG-implementering og bedste praksis - perfekt til at teste systemet.

Systemet behandler dit dokument, opdeler det i stykker og laver indlejringer for hvert stykke. Dette sker automatisk ved upload.

### Stil spørgsmål

Stil nu specifikke spørgsmål om dokumentets indhold. Prøv noget faktuelt, der tydeligt fremgår af dokumentet. Systemet søger efter relevante stykker, inkluderer dem i prompten og genererer et svar.

### Tjek kildehenvisninger

Bemærk, at hvert svar inkluderer kildehenvisninger med lighedsscores. Disse scores (0 til 1) viser, hvor relevante de enkelte stykker var for dit spørgsmål. Højere scores betyder bedre match. Det lader dig verificere svaret mod kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/da/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skærmbillede viser søgeresultater med det genererede svar, kildehenvisninger og relevansscores for hvert hentet stykke.*

### Eksperimentér med spørgsmål

Prøv forskellige typer spørgsmål:
- Specifikke fakta: "Hvad er hovedemnet?"
- Sammenligninger: "Hvad er forskellen på X og Y?"
- Resuméer: "Opsummer nøglepunkterne om Z"

Se, hvordan relevansscores ændrer sig afhængigt af, hvor godt dit spørgsmål matcher dokumentets indhold.

## Centrale begreber

### Chunking-strategi

Dokumenter opdeles i 300-token-stykker med 30 tokens overlappende. Denne balance sikrer, at hvert stykke har nok kontekst til at være meningsfuldt, samtidig med at det er småt nok til at kunne inkludere flere stykker i en prompt.

### Lighedsscores

Hvert hentet stykke leveres med en lighedsscore mellem 0 og 1, der angiver, hvor tæt det matcher brugerens spørgsmål. Diagrammet nedenfor visualiserer scoringsområderne og hvordan systemet bruger dem til at filtrere resultater:

<img src="../../../translated_images/da/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dette diagram viser scoreområder fra 0 til 1, med en minimumsgrænse på 0,5, som filtrerer irrelevante stykker fra.*

Scores spænder fra 0 til 1:
- 0,7-1,0: Meget relevant, præcist match
- 0,5-0,7: Relevant, god kontekst
- Under 0,5: Filtreret væk, for dissimilært

Systemet henter kun stykker over minimumstærsklen for at sikre kvalitet.

Indlejringer fungerer godt, når betydning grupperes tydeligt, men de har blinde vinkler. Diagrammet nedenfor viser de almindelige fejltilstande — stykker der er for store giver uklare vektorer, stykker der er for små mangler kontekst, tvetydige termer peger på flere klynger, og eksakt-match opslag (ID'er, varenummer) fungerer slet ikke med indlejringer:

<img src="../../../translated_images/da/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dette diagram viser almindelige fejltilstande ved indlejringer: for store stykker, for små stykker, tvetydige termer som peger på flere klynger, og eksakt-match opslag som ID'er.*

### Hukommelsesbaseret lagring

Dette modul bruger hukommelsesbaseret lagring for enkelhedens skyld. Når du genstarter applikationen, mistes uploadede dokumenter. Produktionssystemer bruger vedvarende vektordatabaser som Qdrant eller Azure AI Search.

### Håndtering af kontekstvindue

Hver model har et maksimalt kontekstvindue. Du kan ikke inkludere alle stykker fra et stort dokument. Systemet henter de top N mest relevante stykker (standard 5) for at holde sig inden for grænserne og samtidig give tilstrækkelig kontekst til præcise svar.

## Hvornår RAG er relevant

RAG er ikke altid den rigtige løsning. Beslutningsguiden nedenfor hjælper dig med at afgøre, hvornår RAG tilføjer værdi, kontra hvornår enklere metoder — som at inkludere indhold direkte i prompten eller stole på modellens indbyggede viden — er tilstrækkelige:

<img src="../../../translated_images/da/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dette diagram viser en beslutningsguide for, hvornår RAG tilføjer værdi, og hvornår enklere tilgange er tilstrækkelige.*

**Brug RAG, når:**
- Du besvarer spørgsmål om proprietære dokumenter
- Information ændrer sig ofte (politikker, priser, specifikationer)
- Præcision kræver kildehenvisning
- Indholdet er for stort til at passe i én prompt
- Du har brug for verificerbare, velbegrundede svar

**Brug ikke RAG, når:**
- Spørgsmål kræver generel viden, som modellen allerede har
- Der er behov for realtidsdata (RAG arbejder med uploadede dokumenter)
- Indholdet er lille nok til at inkluderes direkte i prompts

## Næste skridt

**Næste modul:** [04-tools - AI-agenter med værktøjer](../04-tools/README.md)

---

**Navigation:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbage til hoved](../README.md) | [Næste: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->