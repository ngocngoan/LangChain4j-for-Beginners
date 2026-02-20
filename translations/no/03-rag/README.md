# Modul 03: RAG (Retrieval-Augmented Generation)

## Innholdsfortegnelse

- [Hva du vil lære](../../../03-rag)
- [Forstå RAG](../../../03-rag)
- [Forutsetninger](../../../03-rag)
- [Hvordan det fungerer](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Opprette embeddings](../../../03-rag)
  - [Semantisk søk](../../../03-rag)
  - [Svargenerering](../../../03-rag)
- [Kjør applikasjonen](../../../03-rag)
- [Bruke applikasjonen](../../../03-rag)
  - [Last opp et dokument](../../../03-rag)
  - [Still spørsmål](../../../03-rag)
  - [Sjekk kildehenvisninger](../../../03-rag)
  - [Eksperimenter med spørsmål](../../../03-rag)
- [Nøkkelbegreper](../../../03-rag)
  - [Chunking-strategi](../../../03-rag)
  - [Likhetspoeng](../../../03-rag)
  - [In-memory lagring](../../../03-rag)
  - [Håndtering av kontekstvindu](../../../03-rag)
- [Når RAG er viktig](../../../03-rag)
- [Neste steg](../../../03-rag)

## Hva du vil lære

I de forrige modulene lærte du hvordan du kan ha samtaler med AI og strukturere promptene dine effektivt. Men det finnes en grunnleggende begrensning: språkmodeller kan bare svare på det de lærte under treningen. De kan ikke svare på spørsmål om bedriftens retningslinjer, prosjektets dokumentasjon eller annen informasjon de ikke har blitt trent på.

RAG (Retrieval-Augmented Generation) løser dette problemet. I stedet for å prøve å lære modellen din informasjon (noe som er kostbart og upraktisk), gir du den muligheten til å søke gjennom dokumentene dine. Når noen stiller et spørsmål, finner systemet relevant informasjon og inkluderer det i prompten. Modellen svarer deretter basert på den innhentede konteksten.

Tenk på RAG som å gi modellen et referansebibliotek. Når du stiller et spørsmål, gjør systemet:

1. **Brukerforespørsel** – Du stiller et spørsmål  
2. **Embedding** – Konverterer spørsmålet ditt til en vektor  
3. **Vektorsøk** – Finner lignende dokumentbiter  
4. **Kontekstsammensetning** – Legger relevante biter til prompten  
5. **Respons** – LLM genererer et svar basert på konteksten  

Dette forankrer modellens svar i dine faktiske data i stedet for å stole på treningskunnskap eller å finne på svar.

## Forstå RAG

Diagrammet nedenfor illustrerer kjerneprinsippet: i stedet for bare å stole på modellens treningsdata, gir RAG den et referansebibliotek av dine dokumenter å konsultere før hver generert respons.

<img src="../../../translated_images/no/what-is-rag.1f9005d44b07f2d8.webp" alt="Hva er RAG" width="800"/>

Slik henger delene sammen fra ende til annen. Et spørsmål fra en bruker går gjennom fire trinn — embedding, vektorsøk, kontekstsammensetning og svargenerering — der hvert trinn bygger på det forrige:

<img src="../../../translated_images/no/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

Resten av denne modulen går gjennom hvert trinn i detalj, med kode du kan kjøre og modifisere.

## Forutsetninger

- Fullført Modul 01 (Azure OpenAI-ressurser deployert)
- `.env`-fil i rotkatalogen med Azure-legitimasjon (laget av `azd up` i Modul 01)

> **Merk:** Hvis du ikke har fullført Modul 01, følg deployeringsinstruksjonene der først.


## Hvordan det fungerer

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du laster opp et dokument, analyserer systemet det (PDF eller ren tekst), legger til metadata som filnavn, og splitter det deretter i biter — mindre deler som passer komfortabelt inn i modellens kontekstvindu. Disse bitene overlapper litt slik at du ikke mister kontekst i grensene.

```java
// Analyser den opplastede filen og pakk den inn i et LangChain4j-dokument
Document document = Document.from(content, metadata);

// Del i 300-token biter med 30-token overlapp
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet nedenfor viser hvordan dette fungerer visuelt. Merk hvordan hver bit deler noen tokens med naboene — 30-tokens overlapp sikrer at ingen viktig kontekst går tapt:

<img src="../../../translated_images/no/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentdeling i biter" width="800"/>

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spør:  
> - "Hvordan deler LangChain4j dokumenter i biter og hvorfor er overlapp viktig?"  
> - "Hva er optimal bit-størrelse for ulike dokumenttyper, og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere språk eller med spesiell formatering?"

### Opprette embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hver bit konverteres til en numerisk representasjon kalt en embedding – i praksis et matematisk fingeravtrykk som fanger meningen i teksten. Lik tekst produserer like embeddings.

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
  
Klassediagrammet nedenfor viser hvordan disse LangChain4j-komponentene henger sammen. `OpenAiOfficialEmbeddingModel` konverterer tekst til vektorer, `InMemoryEmbeddingStore` holder vektorene sammen med den originale `TextSegment`-dataen, og `EmbeddingSearchRequest` styrer hentingsparametere som `maxResults` og `minScore`:

<img src="../../../translated_images/no/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG-klasser" width="800"/>

Når embeddings er lagret, klustrer lignende innhold naturlig sammen i vektorrommet. Visualiseringen nedenfor viser hvordan dokumenter om relaterte emner ender opp som nærliggende punkter, noe som gjør semantisk søk mulig:

<img src="../../../translated_images/no/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor-embedding-rom" width="800"/>

### Semantisk søk

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørsmål, konverteres også spørsmålet ditt til en embedding. Systemet sammenligner spørsmålets embedding med alle embeddings fra dokumentbitene. Det finner bitene med mest lignende betydning – ikke bare matchende søkeord, men faktisk semantisk likhet.

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
  
Diagrammet nedenfor kontrasterer semantisk søk med tradisjonelt søk på nøkkelord. Et søk etter "kjøretøy" savner en bit om "biler og lastebiler," men semantisk søk forstår at de betyr det samme og returnerer det som en høy-score match:

<img src="../../../translated_images/no/semantic-search.6b790f21c86b849d.webp" alt="Semantisk søk" width="800"/>

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spør:  
> - "Hvordan fungerer likhetssøk med embeddings og hva bestemmer scoren?"  
> - "Hvilken likhetsterskel bør jeg bruke og hvordan påvirker det resultater?"  
> - "Hvordan håndterer jeg tilfeller der ingen relevante dokumenter finnes?"

### Svargenerering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante bitene settes sammen til en strukturert prompt som inkluderer eksplisitte instrukser, den innhentede konteksten og brukerens spørsmål. Modellen leser disse spesifikke bitene og svarer basert på informasjonen — den kan bare bruke det som ligger foran den, noe som forhindrer hallusinasjoner.

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
  
Diagrammet nedenfor viser denne sammensetningen i praksis — de høyest scorerte bitene fra søketrinnet injiseres i promptmalen, og `OpenAiOfficialChatModel` genererer et forankret svar:

<img src="../../../translated_images/no/context-assembly.7e6dd60c31f95978.webp" alt="Kontekstsammensetning" width="800"/>

## Kjør applikasjonen

**Verifiser deployering:**

Sjekk at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (laget under Modul 01):  
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Start applikasjonen:**

> **Merk:** Hvis du allerede startet alle applikasjoner med `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8081. Du kan hoppe over startkommandoene under og gå direkte til http://localhost:8081.

**Alternativ 1: Bruk Spring Boot Dashboard (anbefalt for VS Code-brukere)**

Dev-containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i aktivitetslinjen til venstre i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:  
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet  
- Starte/stoppe applikasjoner med et klikk  
- Se applikasjonslogger i sanntid  
- Overvåke applikasjonsstatus  

Klikk på avspillingsknappen ved siden av "rag" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Bruk shell-skript**

Start alle nettapplikasjoner (moduler 01–04):

**Bash:**  
```bash
cd ..  # Fra rotkatalogen
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Fra rotkatalogen
.\start-all.ps1
```
  
Eller start bare denne modulen:

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
  
Begge skriptene laster automatisk miljøvariabler fra root `.env`-filen og bygger JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før start:
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
  
Åpne http://localhost:8081 i nettleseren din.

**For å stoppe:**  

**Bash:**  
```bash
./stop.sh  # Kun denne modulen
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Kun denne modulen
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```
  

## Bruke applikasjonen

Applikasjonen tilbyr et webgrensesnitt for opplasting av dokumenter og spørsmål.

<a href="images/rag-homepage.png"><img src="../../../translated_images/no/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG applikasjonsgrensesnitt" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG-applikasjonsgrensesnittet – last opp dokumenter og still spørsmål*

### Last opp et dokument

Start med å laste opp et dokument – TXT-filer fungerer best for testing. En `sample-document.txt` er tilgjengelig i denne katalogen og inneholder informasjon om LangChain4j-funksjoner, RAG-implementasjon og beste praksis – perfekt for testing av systemet.

Systemet behandler dokumentet ditt, deler det i biter og lager embeddings for hver bit. Dette skjer automatisk når du laster opp.

### Still spørsmål

Nå kan du stille spesifikke spørsmål om dokumentinnholdet. Prøv noe faktabasert som tydelig står i dokumentet. Systemet søker etter relevante biter, inkluderer dem i prompten og genererer et svar.

### Sjekk kildehenvisninger

Legg merke til at hvert svar inkluderer kildehenvisninger med likhetspoeng. Disse poengene (0 til 1) viser hvor relevant hver bit var for spørsmålet ditt. Høyere poeng betyr bedre match. Dette lar deg verifisere svaret mot kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/no/rag-query-results.6d69fcec5397f355.webp" alt="RAG spørsmålresultater" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Spørsmålresultater som viser svar med kildehenvisninger og relevanspoeng*

### Eksperimenter med spørsmål

Prøv forskjellige typer spørsmål:  
- Spesifikke fakta: "Hva er hovedtemaet?"  
- Sammenligninger: "Hva er forskjellen mellom X og Y?"  
- Sammendrag: "Oppsummer hovedpunktene om Z"  

Se hvordan relevanspoengene endres basert på hvor godt spørsmålet ditt matcher dokumentinnholdet.

## Nøkkelbegreper

### Chunking-strategi

Dokumenter deles inn i 300-token biter med 30 tokens overlapp. Denne balansen sikrer at hver bit har nok kontekst til å være meningsfull samtidig som de er små nok til at flere biter kan inkluderes i en prompt.

### Likhetspoeng

Hver hentet bit kommer med et likhetspoeng mellom 0 og 1 som indikerer hvor tett den matcher brukerens spørsmål. Diagrammet nedenfor visualiserer poengintervallene og hvordan systemet bruker dem til å filtrere resultater:

<img src="../../../translated_images/no/similarity-scores.b0716aa911abf7f0.webp" alt="Likhetspoeng" width="800"/>

Poengområder fra 0 til 1:  
- 0,7–1,0: Svært relevant, eksakt match  
- 0,5–0,7: Relevant, god kontekst  
- Under 0,5: Filtrert bort, for ulik  

Systemet henter bare biter over minimumsterskelen for å sikre kvalitet.

### In-memory lagring

Denne modulen bruker in-memory lagring for enkelhetens skyld. Når du starter applikasjonen på nytt, går opplastede dokumenter tapt. Produksjonssystemer bruker persistent vektordatabaser som Qdrant eller Azure AI Search.

### Håndtering av kontekstvindu

Hver modell har et maksimalt kontekstvindu. Du kan ikke inkludere alle biter fra et stort dokument. Systemet henter de N mest relevante bitene (standard 5) for å holde seg innenfor grensen samtidig som det gir nok kontekst for nøyaktige svar.

## Når RAG er viktig

RAG er ikke alltid riktig tilnærming. Veiledningen nedenfor hjelper deg med å avgjøre når RAG tilfører verdi kontra når enklere tilnærminger – som å inkludere innhold direkte i prompten eller stole på modellens innebygde kunnskap – er tilstrekkelig:

<img src="../../../translated_images/no/when-to-use-rag.1016223f6fea26bc.webp" alt="Når bruke RAG" width="800"/>

**Bruk RAG når:**
- Besvare spørsmål om proprietære dokumenter  
- Informasjon endres ofte (retningslinjer, priser, spesifikasjoner)  
- Nøyaktighet krever kildehenvisning  
- Innholdet er for stort til å passe i et enkelt prompt  
- Du trenger verifiserbare, grunnfestede svar  

**Ikke bruk RAG når:**  
- Spørsmål krever generell kunnskap modellen allerede har  
- Sanntidsdata er nødvendig (RAG fungerer på opplastede dokumenter)  
- Innholdet er lite nok til å inkluderes direkte i promptene  

## Neste steg  

**Neste modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)  

---  

**Navigasjon:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbake til hovedmeny](../README.md) | [Neste: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets originale språk skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->