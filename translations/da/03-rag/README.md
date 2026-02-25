# Modul 03: RAG (Retrieval-Augmented Generation)

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../03-rag)
- [Forståelse af RAG](../../../03-rag)
- [Forudsætninger](../../../03-rag)
- [Sådan Fungerer Det](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Oprettelse af Embeddings](../../../03-rag)
  - [Semantisk Søgning](../../../03-rag)
  - [Svar Generering](../../../03-rag)
- [Kør Applikationen](../../../03-rag)
- [Brug af Applikationen](../../../03-rag)
  - [Upload et Dokument](../../../03-rag)
  - [Stil Spørgsmål](../../../03-rag)
  - [Tjek Kildereferencer](../../../03-rag)
  - [Eksperimenter med Spørgsmål](../../../03-rag)
- [Nøglebegreber](../../../03-rag)
  - [Chunking-Strategi](../../../03-rag)
  - [Lighedsscores](../../../03-rag)
  - [Hukommelseslager](../../../03-rag)
  - [Håndtering af Kontekstvindue](../../../03-rag)
- [Hvornår RAG Er Vigtigt](../../../03-rag)
- [Næste Skridt](../../../03-rag)

## Hvad Du Vil Lære

I de tidligere moduler lærte du, hvordan man fører samtaler med AI og strukturerer dine prompts effektivt. Men der er en grundlæggende begrænsning: sproglige modeller kender kun det, de har lært under træningen. De kan ikke besvare spørgsmål om din virksomheds politikker, din projektdokumentation eller nogen information, de ikke blev trænet på.

RAG (Retrieval-Augmented Generation) løser dette problem. I stedet for at forsøge at lære modellen din information (hvilket er dyrt og upraktisk), giver du den mulighed for at søge gennem dine dokumenter. Når nogen stiller et spørgsmål, finder systemet relevant information og inkluderer det i prompten. Modellen svarer derefter baseret på den hentede kontekst.

Tænk på RAG som at give modellen et referencebibliotek. Når du stiller et spørgsmål, gør systemet:

1. **Brugerforespørgsel** - Du stiller et spørgsmål  
2. **Embedding** - Konverterer dit spørgsmål til en vektor  
3. **Vektorsøgning** - Finder lignende dokumentstykker  
4. **Kontekstsammensætning** - Tilføjer relevante stykker til prompten  
5. **Respons** - LLM genererer et svar baseret på konteksten  

Det forankrer modellens svar i dine faktiske data i stedet for at stole på dens træningsviden eller finde på svar.

## Forståelse af RAG

Diagrammet nedenfor illustrerer det centrale koncept: i stedet for kun at stole på modellens træningsdata, giver RAG den et referencebibliotek af dine dokumenter at konsultere, før den genererer hvert svar.

<img src="../../../translated_images/da/what-is-rag.1f9005d44b07f2d8.webp" alt="Hvad er RAG" width="800"/>

Sådan hænger delene sammen end-to-end. Et brugerspørgsmål gennemgår fire trin — embedding, vektorsøgning, kontekstsammensætning og svargenerering — hver bygget oven på det foregående:

<img src="../../../translated_images/da/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

Resten af dette modul gennemgår hvert trin i detaljer, med kode du kan køre og ændre.

## Forudsætninger

- Fuldført Modul 01 (Azure OpenAI-ressourcer deployeret)  
- `.env`-fil i rodmappen med Azure-legitimationsoplysninger (oprettet af `azd up` i Modul 01)  

> **Note:** Hvis du ikke har fuldført Modul 01, følg først deployeringsinstruktionerne der.

## Sådan Fungerer Det

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du uploader et dokument, parser systemet det (PDF eller ren tekst), tilknytter metadata som filnavn, og deler det derefter op i chunks — mindre dele, der passer komfortabelt i modellens kontekstvindue. Disse chunks overlapper let, så du ikke mister kontekst ved grænserne.

```java
// Analyser den uploadede fil og pak den ind i et LangChain4j-dokument
Document document = Document.from(content, metadata);

// Del i 300-token bidder med 30-token overlapning
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet nedenfor viser, hvordan dette fungerer visuelt. Bemærk hvordan hvert chunk deler nogle tokens med sine naboer — de 30 token overlap sikrer, at ingen vigtig kontekst går tabt imellem:

<img src="../../../translated_images/da/document-chunking.a5df1dd1383431ed.webp" alt="Dokument Chunking" width="800"/>

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spørg:  
> - "Hvordan deler LangChain4j dokumenter op i chunks, og hvorfor er overlap vigtigt?"  
> - "Hvad er den optimale chunk-størrelse for forskellige dokumenttyper, og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere sprog eller med specialformatering?"

### Oprettelse af Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hvert chunk konverteres til en numerisk repræsentation kaldet en embedding - essentielt et matematisk fingeraftryk, der fanger tekstens betydning. Lignende tekst giver lignende embeddings.

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
  
Klassediagrammet nedenfor viser, hvordan disse LangChain4j-komponenter forbindes. `OpenAiOfficialEmbeddingModel` konverterer tekst til vektorer, `InMemoryEmbeddingStore` holder vektorerne sammen med deres originale `TextSegment` data, og `EmbeddingSearchRequest` kontrollerer hentningsparametre som `maxResults` og `minScore`:

<img src="../../../translated_images/da/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klasser" width="800"/>

Når embeddings er lagret, samler lignende indhold sig naturligt i vektor-rummet. Visualiseringen nedenfor viser, hvordan dokumenter om beslægtede emner ender som nære punkter, hvilket gør semantisk søgning mulig:

<img src="../../../translated_images/da/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor Embeddings Rum" width="800"/>

### Semantisk Søgning

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørgsmål, bliver dit spørgsmål også en embedding. Systemet sammenligner dit spørgsmålsembedding med alle dokument-chunks embeddings. Det finder de chunks med de mest lignende betydninger - ikke bare matcher nøgleord, men faktisk semantisk lighed.

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
  
Diagrammet nedenfor kontrasterer semantisk søgning med traditionel nøgleordsøgning. En nøgleordsøgning efter "vehicle" overser et chunk om "biler og lastbiler", men semantisk søgning forstår, at de betyder det samme og returnerer det som et højt vurderet match:

<img src="../../../translated_images/da/semantic-search.6b790f21c86b849d.webp" alt="Semantisk Søgning" width="800"/>

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åbn [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spørg:  
> - "Hvordan fungerer lighedssøgning med embeddings, og hvad bestemmer scoren?"  
> - "Hvilken lighedstærskel bør jeg bruge, og hvordan påvirker det resultater?"  
> - "Hvordan håndterer jeg tilfælde hvor ingen relevante dokumenter findes?"

### Svar Generering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante chunks samles i en struktureret prompt, der inkluderer eksplicitte instruktioner, den hentede kontekst og brugerens spørgsmål. Modellen læser netop disse chunks og svarer baseret på denne information — den kan kun bruge, hvad der er foran den, hvilket forhindrer hallucination.

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
  
Diagrammet nedenfor viser denne samling i praksis — de højest scorende chunks fra søgetrinnet indsættes i promptskabelonen, og `OpenAiOfficialChatModel` genererer et forankret svar:

<img src="../../../translated_images/da/context-assembly.7e6dd60c31f95978.webp" alt="Kontekstsammensætning" width="800"/>

## Kør Applikationen

**Bekræft deployering:**

Sørg for, at `.env`-fil findes i rodmappen med Azure-legitimationsoplysninger (oprettet under Modul 01):  
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Start applikationen:**

> **Note:** Hvis du allerede startede alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8081. Du kan springe startkommandoerne over nedenfor og gå direkte til http://localhost:8081.

**Mulighed 1: Brug af Spring Boot Dashboard (Anbefalet til VS Code-brugere)**

Dev containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver et visuelt interface til at administrere alle Spring Boot applikationer. Du finder den i Aktivitetslinjen til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:  
- Se alle tilgængelige Spring Boot applikationer i workspace  
- Starte/stoppe applikationer med et enkelt klik  
- Se applikationslogs i realtid  
- Overvåge applikationsstatus

Klik blot på play-knappen ved siden af "rag" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug af shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**  
```bash
cd ..  # Fra rodmappe
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Fra rodkatalog
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
  
Begge scripts loader automatisk miljøvariabler fra rod `.env`-filen og bygger JAR-filerne hvis de ikke findes.

> **Note:** Hvis du foretrækker at bygge alle moduler manuelt inden start:  
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


## Brug af Applikationen

Applikationen giver et webinterface til dokumentupload og spørgsmål.

<a href="images/rag-homepage.png"><img src="../../../translated_images/da/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Applikationsinterface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG applikationsinterface - upload dokumenter og stil spørgsmål*

### Upload et Dokument

Start med at uploade et dokument - TXT-filer fungerer bedst til test. En `sample-document.txt` er inkluderet i denne mappe, som indeholder information om LangChain4j funktioner, RAG-implementering og best practices - perfekt til at teste systemet.

Systemet behandler dit dokument, deler det op i chunks og laver embeddings for hvert chunk. Dette sker automatisk ved upload.

### Stil Spørgsmål

Stil nu specifikke spørgsmål om dokumentets indhold. Prøv noget faktuelt, der klart står i dokumentet. Systemet søger efter relevante chunks, inkluderer dem i prompten og genererer et svar.

### Tjek Kildereferencer

Bemærk, at hvert svar inkluderer kildereferencer med lighedsscores. Disse scores (0 til 1) viser, hvor relevant hvert chunk var for dit spørgsmål. Højere scores betyder bedre matches. Det gør, at du kan verificere svaret mod kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/da/rag-query-results.6d69fcec5397f355.webp" alt="RAG Forespørgselsresultater" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Forespørgselsresultater viser svar med kildereferencer og relevansscores*

### Eksperimenter med Spørgsmål

Prøv forskellige typer spørgsmål:  
- Specifikke facts: "Hvad er hovedemnet?"  
- Sammenligninger: "Hvad er forskellen mellem X og Y?"  
- Opsummeringer: "Opsummer hovedpunkterne om Z"  

Se, hvordan relevansscores ændrer sig afhængig af, hvor godt dit spørgsmål matcher dokumentindholdet.

## Nøglebegreber

### Chunking-Strategi

Dokumenter opdeles i 300-token chunks med 30 tokens overlap. Denne balance sikrer, at hvert chunk har nok kontekst til at være meningsfyldt, samtidig med at det er lille nok til at kunne inkludere flere chunks i en prompt.

### Lighedsscores

Hvert hentet chunk leveres med en lighedsscore mellem 0 og 1, der angiver, hvor tæt det matcher brugerens spørgsmål. Diagrammet nedenfor visualiserer scoreområder og hvordan systemet bruger dem til at filtrere resultater:

<img src="../../../translated_images/da/similarity-scores.b0716aa911abf7f0.webp" alt="Lighedsscores" width="800"/>

Scores spænder fra 0 til 1:  
- 0,7-1,0: Meget relevant, præcist match  
- 0,5-0,7: Relevant, god kontekst  
- Under 0,5: Filtreret fra, for forskelligt  

Systemet henter kun chunks over minimumstersklen for at sikre kvalitet.

### Hukommelseslager

Dette modul bruger in-memory lagring af enkelheds skyld. Når du genstarter applikationen, mistes uploadede dokumenter. Produktionssystemer bruger persistente vektordatabaser som Qdrant eller Azure AI Search.

### Håndtering af Kontekstvindue

Hver model har et maksimum for kontekstvindue. Du kan ikke inkludere alle chunks fra et stort dokument. Systemet henter de top N mest relevante chunks (standard 5) for at holde sig inden for grænserne, mens det giver nok kontekst til præcise svar.

## Hvornår RAG Er Vigtigt

RAG er ikke altid den rette tilgang. Beslutningsguiden nedenfor hjælper dig med at afgøre, hvornår RAG tilfører værdi versus hvornår enklere metoder — som at inkludere indhold direkte i prompten eller stole på modellens indbyggede viden — er tilstrækkelige:

<img src="../../../translated_images/da/when-to-use-rag.1016223f6fea26bc.webp" alt="Hvornår man skal bruge RAG" width="800"/>

**Brug RAG når:**
- Besvare spørgsmål om proprietære dokumenter  
- Information ændrer sig ofte (politikker, priser, specifikationer)  
- Nøjagtighed kræver kildehenvisning  
- Indholdet er for stort til at passe i en enkelt prompt  
- Du har brug for verificerbare, velbegrundede svar  

**Brug ikke RAG når:**  
- Spørgsmål kræver generel viden, som modellen allerede har  
- Realtidsdata er nødvendigt (RAG fungerer på uploadede dokumenter)  
- Indholdet er lille nok til at inkludere direkte i prompts  

## Næste skridt

**Næste modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi stræber efter nøjagtighed, bedes du være opmærksom på, at automatiske oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på dets modersmål bør betragtes som den autoritative kilde. For vigtig information anbefales professionel menneskelig oversættelse. Vi er ikke ansvarlige for eventuelle misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->