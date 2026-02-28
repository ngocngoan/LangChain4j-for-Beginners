# Modul 03: RAG (Retrieval-Augmented Generation)

## Innholdsfortegnelse

- [Video Gjennomgang](../../../03-rag)
- [Hva Du Vil Lære](../../../03-rag)
- [Forutsetninger](../../../03-rag)
- [Forstå RAG](../../../03-rag)
  - [Hvilken RAG-tilnærming Bruker Denne Veiledningen?](../../../03-rag)
- [Hvordan Det Fungerer](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Opprettelse av Embeddings](../../../03-rag)
  - [Semantisk Søk](../../../03-rag)
  - [Svar Generering](../../../03-rag)
- [Kjør Applikasjonen](../../../03-rag)
- [Bruke Applikasjonen](../../../03-rag)
  - [Last Opp et Dokument](../../../03-rag)
  - [Still Spørsmål](../../../03-rag)
  - [Sjekk Kilder](../../../03-rag)
  - [Eksperimenter med Spørsmål](../../../03-rag)
- [Nøkkelbegreper](../../../03-rag)
  - [Chunking-strategi](../../../03-rag)
  - [Likhetspoeng](../../../03-rag)
  - [In-memory Lagring](../../../03-rag)
  - [Kontekstvindu-håndtering](../../../03-rag)
- [Når RAG Er Viktig](../../../03-rag)
- [Neste Steg](../../../03-rag)

## Video Gjennomgang

Se denne livesesjonen som forklarer hvordan du kommer i gang med denne modulen: [RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Hva Du Vil Lære

I de tidligere modulene lærte du hvordan du kan ha samtaler med AI og strukturere promptene dine effektivt. Men det finnes en grunnleggende begrensning: språkmodeller vet bare det de lærte under treningen. De kan ikke svare på spørsmål om selskapets retningslinjer, prosjekt- dokumentasjon, eller annen informasjon de ikke ble trent på.

RAG (Retrieval-Augmented Generation) løser dette problemet. I stedet for å prøve å lære modellen din informasjon (noe som er kostbart og upraktisk), gir du den muligheten til å søke i dokumentene dine. Når noen stiller et spørsmål, finner systemet relevant informasjon og inkluderer den i prompten. Modellen svarer deretter basert på den hentede konteksten.

Tenk på RAG som å gi modellen et referansebibliotek. Når du stiller et spørsmål, gjør systemet:

1. **Brukerspørsmål** - Du stiller et spørsmål  
2. **Embedding** - Konverterer spørsmålet til en vektor  
3. **Vektsøk** - Finner lignende dokumentbiter  
4. **Kontekstsammensetning** - Legger til relevante biter i prompten  
5. **Respons** - LLM genererer et svar basert på konteksten  

Dette forankrer modellens svar i dine faktiske data i stedet for å stole på treningsdata eller å dikte opp svar.

## Forutsetninger

- Fullført [Modul 00 - Rask Start](../00-quick-start/README.md) (for eksempelet Easy RAG nevnt ovenfor)  
- Fullført [Modul 01 - Introduksjon](../01-introduction/README.md) (Azure OpenAI-ressurser distribuert, inkludert `text-embedding-3-small` embedding-modellen)  
- `.env`-fil i rotkatalog med Azure-legitimasjon (opprettet via `azd up` i Modul 01)  

> **Merk:** Hvis du ikke har fullført Modul 01, følg distribusjonsinstruksjonene der først. Kommandoen `azd up` distribuerer både GPT chat-modellen og embedding-modellen som brukes i denne modulen.

## Forstå RAG

Diagrammet under illustrerer kjernkonseptet: i stedet for bare å stole på modellens treningsdata, gir RAG den et referansebibliotek av dokumentene dine å konsultere før hvert svar genereres.

<img src="../../../translated_images/no/what-is-rag.1f9005d44b07f2d8.webp" alt="Hva er RAG" width="800"/>

*Dette diagrammet viser forskjellen mellom en standard LLM (som gjetter basert på treningsdata) og en RAG-forsterket LLM (som først konsulterer dokumentene dine).*

Slik kobles delene sammen i ende-til-ende. Et brukerspørsmål går gjennom fire faser — embedding, vektorsøk, kontekstsammensetning og svargenerering — der hver bygger på forrige:

<img src="../../../translated_images/no/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

*Dette diagrammet viser ende-til-ende RAG-pipelinen — et brukerspørsmål går gjennom embedding, vektorsøk, kontekstsammensetning og svargenerering.*

Resten av denne modulen går gjennom hver fase detaljert, med kode du kan kjøre og modifisere.

### Hvilken RAG-tilnærming Bruker Denne Veiledningen?

LangChain4j tilbyr tre måter å implementere RAG på, hver med ulikt abstraksjonsnivå. Diagrammet under sammenligner dem side om side:

<img src="../../../translated_images/no/rag-approaches.5b97fdcc626f1447.webp" alt="Tre RAG-tilnærminger i LangChain4j" width="800"/>

*Dette diagrammet sammenligner de tre LangChain4j RAG-tilnærmingene — Easy, Native og Advanced — og viser nøkkelkomponentene og når de bør brukes.*

| Tilnærming | Hva Den Gjør | Avveining |
|---|---|---|
| **Easy RAG** | Koble alt automatisk gjennom `AiServices` og `ContentRetriever`. Du annoterer et grensesnitt, kobler til en retriever, og LangChain4j håndterer embedding, søk og prompt-sammensetning bak kulissene. | Minimalt med kode, men du ser ikke hva som skjer i hver fase. |
| **Native RAG** | Du kaller embedding-modellen, søker i lagringen, bygger prompten og genererer svaret selv — ett eksplisitt steg om gangen. | Mer kode, men hver fase er synlig og kan endres. |
| **Advanced RAG** | Bruker `RetrievalAugmentor`-rammeverket med pluggbare spørringstransformatorer, routere, re-rankere og innholdsinnsprøytere for produksjonsklare pipelines. | Maksimal fleksibilitet, men betydelig mer kompleksitet. |

**Denne veiledningen bruker Native-tilnærmingen.** Hvert steg i RAG-pipelinen — embedding av spørsmålet, søk i vektorlagring, sammensetning av kontekst og generering av svar — er skrevet eksplisitt i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Dette er bevisst: som et læringsverktøy er det viktigere at du ser og forstår hver fase enn at koden er kortfattet. Når du føler deg komfortabel med hvordan delene passer sammen, kan du gå videre til Easy RAG for raske prototyper eller Advanced RAG for produksjonssystemer.

> **💡 Har du allerede sett Easy RAG i aksjon?** [Rask Start-modulen](../00-quick-start/README.md) inkluderer et Dokument Q&A-eksempel ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) som bruker Easy RAG-tilnærmingen — LangChain4j håndterer embedding, søk og prompt-sammensetning automatisk. Denne modulen tar neste steg ved å åpne opp den pipelinen slik at du kan se og kontrollere hver fase selv.

<img src="../../../translated_images/no/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dette diagrammet viser Easy RAG-pipelinen fra `SimpleReaderDemo.java`. Sammenlign dette med Native-tilnærmingen brukt i denne modulen: Easy RAG skjuler embedding, innhenting og promptsammensetning bak `AiServices` og `ContentRetriever` — du laster et dokument, kobler til en retriever og får svar. Native-tilnærmingen i denne modulen åpner opp pipelinen slik at du kaller hvert steg (embed, søk, sett sammen kontekst, generer) selv, som gir full synlighet og kontroll.*

## Hvordan Det Fungerer

RAG-pipelinen i denne modulen deles opp i fire sekvensielle trinn som kjøres hver gang en bruker stiller et spørsmål. Først blir et opplastet dokument **parslet og delt i biter** som er håndterbare. Disse bitene blir så konvertert til **vektor-embeddings** og lagret slik at de kan sammenlignes matematisk. Når et spørsmål kommer inn, utfører systemet et **semantisk søk** for å finne de mest relevante bitene, og sender dem til slutt som kontekst til LLM-en for **svargenerering**. Nedenfor går vi gjennom hvert steg med faktisk kode og diagrammer. La oss se på første steg.

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du laster opp et dokument, blir det parslet (PDF eller ren tekst), metadata som filnavn legges til, og så deles det opp i chunker — mindre biter som passer komfortabelt inn i modellens kontekstvindu. Disse chunkene overlapper litt for å ikke miste kontekst ved grensene.

```java
// Analyser den opplastede filen og pakk den inn i et LangChain4j-dokument
Document document = Document.from(content, metadata);

// Del opp i biter på 300 tokens med 30 tokens overlapp
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet under viser hvordan dette fungerer visuelt. Legg merke til hvordan hver chunk deler noen tokens med nabobitene — 30-token overlapp sikrer at ingen viktig kontekst faller mellom sprekkene:

<img src="../../../translated_images/no/document-chunking.a5df1dd1383431ed.webp" alt="Dokument Chunking" width="800"/>

*Dette diagrammet viser et dokument som deles i 300-token chunker med 30-token overlapp, for å bevare konteksten ved chunk-grensene.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spør:  
> - "Hvordan deler LangChain4j dokumenter i chunker og hvorfor er overlapp viktig?"  
> - "Hva er optimal chunk-størrelse for forskjellige dokumenttyper og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere språk eller med spesiell formatering?"

### Opprettelse av Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hver chunk konverteres til en numerisk representasjon kalt en embedding — i praksis en oversetter fra mening til tall. Embedding-modellen er ikke "intelligent" som en chatmodell; den kan ikke følge instrukser, resonnere eller svare på spørsmål. Den kan derimot kartlegge tekst inn i et matematisk rom der lignende betydninger havner nær hverandre — "bil" nær "automobil", "refusjonspolicy" nær "få pengene mine tilbake". Tenk på chatmodellen som en person du kan snakke med; en embedding-modell er et ultra-godt arkivsystem.

<img src="../../../translated_images/no/embedding-model-concept.90760790c336a705.webp" alt="Embedding Modell Konsept" width="800"/>

*Dette diagrammet viser hvordan en embedding-modell konverterer tekst til numeriske vektorer, og plasserer lignende betydninger — som "bil" og "automobil" — nær hverandre i vektorrommet.*

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
  
Klasse-diagrammet under viser de to separate flytene i en RAG-pipeline og LangChain4j-klassene som implementerer dem. **Inntaksflyten** (kjøres ved opplasting) deler dokumentet, embedder chunkene og lagrer dem via `.addAll()`. **Spørringsflyten** (kjøres hver gang en bruker spør) embedder spørsmålet, søker lagringen via `.search()` og sender samsvarende kontekst til chatmodellen. Begge flytene møtes i det delte `EmbeddingStore<TextSegment>`-grensesnittet:

<img src="../../../translated_images/no/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klasser" width="800"/>

*Dette diagrammet viser de to flytene i en RAG-pipeline — inntak og spørring — og hvordan de kobles via en delt EmbeddingStore.*

Når embeddings er lagret, klustrer lignende innhold naturlig sammen i vektorrommet. Visualiseringen under viser hvordan dokumenter om relaterte emner ender opp som nære punkter, noe som muliggjør semantisk søk:

<img src="../../../translated_images/no/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor Embeddings Rom" width="800"/>

*Denne visualiseringen viser hvordan relaterte dokumenter klustrer sammen i 3D-vektorrom, med emner som teknisk dokumentasjon, forretningsregler og FAQ-er som danner distinkte grupper.*

Når en bruker søker, følger systemet fire trinn: embedde dokumentene én gang, embedde spørsmålet for hvert søk, sammenligne spørsmålsvektoren mot alle lagrede vektorer med cosinuslikhet, og returnere topp-K høyest poengsatte chunker. Diagrammet under går gjennom hvert trinn og LangChain4j-klassene som brukes:

<img src="../../../translated_images/no/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Søk Trinn" width="800"/>

*Dette diagrammet viser firetrinns embedding-søk prosessen: embedde dokumenter, embedde spørsmålet, sammenligne vektorer med cosinuslikhet, og returnere topp-K resultater.*

### Semantisk Søk

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørsmål, blir spørsmålet også embeddet. Systemet sammenligner spørsmålets embedding mot alle dokumentchunkenes embeddings. Det finner chunkene med mest lik betydning — ikke bare samsvarende nøkkelord, men egentlig semantisk likhet.

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
  
Diagrammet under kontrasterer semantisk søk med tradisjonelt nøkkelordsøk. Et nøkkelordsøk etter "kjøretøy" savner en chunk om "biler og lastebiler", men semantisk søk forstår at de betyr det samme og returnerer det som et høyt vurdert treff:

<img src="../../../translated_images/no/semantic-search.6b790f21c86b849d.webp" alt="Semantisk Søk" width="800"/>

*Dette diagrammet sammenligner nøkkelordsøk med semantisk søk, og viser hvordan semantisk søk henter konseptuelt relatert innhold selv når nøyaktige nøkkelord er ulike.*

Under panseret måles likhet med cosinuslikhet — som å spørre "peker disse to pilene i samme retning?" To chunker kan bruke helt forskjellige ord, men hvis de betyr det samme peker vektorene likt og skårer nær 1.0:

<img src="../../../translated_images/no/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosinuslikhet" width="800"/>

*Dette diagrammet illustrerer cosinuslikhet som vinkelen mellom embedding-vektorer — mer justerte vektorer skårer nærmere 1.0, noe som indikerer høyere semantisk likhet.*
> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spør:
> - "Hvordan fungerer likhetssøk med embeddinger og hva bestemmer poengsummen?"
> - "Hvilken likhetsterskel bør jeg bruke, og hvordan påvirker den resultatene?"
> - "Hvordan håndterer jeg tilfeller der ingen relevante dokumenter blir funnet?"

### Svar Generering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante delene settes sammen til en strukturert prompt som inkluderer eksplisitte instruksjoner, den hentede konteksten, og brukerens spørsmål. Modellen leser disse spesifikke delene og svarer basert på denne informasjonen — den kan bare bruke det som er foran seg, noe som forhindrer hallusinasjoner.

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

Diagrammet nedenfor viser denne sammensetningen i praksis — de høyest rangerte delene fra søketrinnet settes inn i promptmalen, og `OpenAiOfficialChatModel` genererer et forankret svar:

<img src="../../../translated_images/no/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dette diagrammet viser hvordan de høyest rangerte delene settes sammen til en strukturert prompt, som lar modellen generere et forankret svar basert på dine data.*

## Kjør Applikasjonen

**Bekreft distribusjonen:**

Sørg for at `.env`-filen finnes i rotmappen med Azure-legitimasjon (opprettet under Modul 01):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede startet alle applikasjoner ved å bruke `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8081. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8081.

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Utviklingscontaineren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i Aktivitetslinjen på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stoppe applikasjoner med ett enkelt klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonsstatus

Klikk enkelt på avspillingsknappen ved siden av "rag" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dette skjermbildet viser Spring Boot Dashboard i VS Code, hvor du visuelt kan starte, stoppe og overvåke applikasjoner.*

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (modulene 01-04):

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

Begge skriptene laster automatisk miljøvariabler fra rot `.env`-filen og bygger JAR-filene hvis de ikke finnes.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før oppstart:
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
./stop.sh  # Denne modulen bare
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Dette modul bare
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Bruke Applikasjonen

Applikasjonen tilbyr et webgrensesnitt for dokumentopplasting og spørsmål.

<a href="images/rag-homepage.png"><img src="../../../translated_images/no/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skjermbildet viser RAG-applikasjonens grensesnitt hvor du laster opp dokumenter og stiller spørsmål.*

### Last opp et dokument

Start med å laste opp et dokument – TXT-filer fungerer best for testing. En `sample-document.txt` er tilgjengelig i denne katalogen som inneholder informasjon om LangChain4j-funksjoner, RAG-implementasjon og beste praksis – perfekt for å teste systemet.

Systemet behandler dokumentet ditt, deler det opp i biter, og lager embeddinger for hver bit. Dette skjer automatisk ved opplasting.

### Still spørsmål

Still nå spesifikke spørsmål om dokumentinnholdet. Prøv noe faktuelt som er klart oppgitt i dokumentet. Systemet søker etter relevante biter, inkluderer dem i prompten, og genererer et svar.

### Sjekk kildehenvisninger

Legg merke til at hvert svar inkluderer kildehenvisninger med likhetspoeng. Disse poengene (0 til 1) viser hvor relevante hver bit var for spørsmålet ditt. Høyere poeng betyr bedre treff. Dette lar deg verifisere svaret mot kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/no/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skjermbildet viser søkeresultater med generert svar, kildehenvisninger, og relevanspoeng for hver hentede bit.*

### Eksperimenter med spørsmål

Prøv forskjellige typer spørsmål:
- Spesifikke fakta: "Hva er hovedtemaet?"
- Sammenligninger: "Hva er forskjellen mellom X og Y?"
- Sammendrag: "Oppsummer hovedpunktene om Z"

Se hvordan relevanspoengene endrer seg basert på hvor godt spørsmålet ditt samsvarer med dokumentinnholdet.

## Nøkkelkonsepter

### Oppdelingsstrategi

Dokumenter deles opp i 300-token-biter med 30 tokens overlapp. Denne balansen sikrer at hver bit har nok kontekst til å være meningsfull samtidig som de er små nok til at flere kan inkluderes i en prompt.

### Likhetspoeng

Hver hentet bit kommer med et likhetspoeng mellom 0 og 1 som indikerer hvor tett den matcher brukerens spørsmål. Diagrammet nedenfor visualiserer poengområdene og hvordan systemet bruker dem til å filtrere resultater:

<img src="../../../translated_images/no/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dette diagrammet viser poengområder fra 0 til 1, med en minimumsterskel på 0,5 som filtrerer ut irrelevante biter.*

Poengene varierer fra 0 til 1:
- 0,7-1,0: Svært relevant, eksakt treff
- 0,5-0,7: Relevant, god kontekst
- Under 0,5: Filtrert ut, for ulikt

Systemet henter kun biter over minimumsterskelen for å sikre kvalitet.

Embeddinger fungerer godt når mening klumper seg tydelig, men har blinde flekker. Diagrammet nedenfor viser vanlige feilmoduser — biter som er for store gir upresise vektorer, biter som er for små mangler kontekst, tvetydige termer peker til flere klynger, og eksakt-treff søk (ID-er, delenummer) fungerer ikke med embeddinger i det hele tatt:

<img src="../../../translated_images/no/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dette diagrammet viser vanlige feilmoduser for embedding: biter for store, biter for små, tvetydige termer som peker til flere klynger, og eksakt-treff søk som ID-er.*

### In-Memory Lagring

Denne modulen bruker in-memory lagring for enkelhets skyld. Når du starter applikasjonen på nytt, går de opplastede dokumentene tapt. Produksjonssystemer bruker vedvarende vektordatabaser som Qdrant eller Azure AI Search.

### Håndtering av kontekstvinduer

Hver modell har et maksimalt kontekstvindu. Du kan ikke inkludere alle biter fra et stort dokument. Systemet henter de N mest relevante bitene (standard 5) for å holde seg innenfor begrensningene samtidig som det gir nok kontekst for nøyaktige svar.

## Når RAG er Viktig

RAG er ikke alltid riktig tilnærming. Beslutningsguiden nedenfor hjelper deg å avgjøre når RAG tilfører verdi kontra når enklere metoder — som å inkludere innhold direkte i prompten eller stole på modellens innebygde kunnskap — er tilstrekkelig:

<img src="../../../translated_images/no/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dette diagrammet viser en beslutningsguide for når RAG tilfører verdi versus når enklere tilnærminger er tilstrekkelige.*

**Bruk RAG når:**
- Du svarer på spørsmål om proprietære dokumenter
- Informasjonen endres ofte (retningslinjer, priser, spesifikasjoner)
- Nøyaktighet krever kildehenvisning
- Innholdet er for stort til å passe i en enkelt prompt
- Du trenger verifiserbare, forankrede svar

**Ikke bruk RAG når:**
- Spørsmål krever generell kunnskap modellen allerede har
- Sanntidsdata trengs (RAG fungerer på opplastede dokumenter)
- Innholdet er lite nok til å inkluderes direkte i prompten

## Neste Steg

**Neste Modul:** [04-tools - AI-agenter med verktøy](../04-tools/README.md)

---

**Navigasjon:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbake til hovedside](../README.md) | [Neste: Modul 04 - Verktøy →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Originaldokumentet på dets opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->