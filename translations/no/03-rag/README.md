# Modul 03: RAG (Hentingsforsterket Generering)

## Innholdsfortegnelse

- [Video gjennomgang](../../../03-rag)
- [Hva du vil lære](../../../03-rag)
- [Forutsetninger](../../../03-rag)
- [Forstå RAG](../../../03-rag)
  - [Hvilken RAG-tilnærming bruker denne veiledningen?](../../../03-rag)
- [Hvordan det fungerer](../../../03-rag)
  - [Dokumentbehandling](../../../03-rag)
  - [Opprette innebygginger](../../../03-rag)
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
  - [Lagring i minnet](../../../03-rag)
  - [Håndtering av kontekstvindu](../../../03-rag)
- [Når RAG er viktig](../../../03-rag)
- [Neste steg](../../../03-rag)

## Video gjennomgang

Se denne liveøkten som forklarer hvordan komme i gang med denne modulen:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG med LangChain4j - Live Session" width="800"/></a>

## Hva du vil lære

I de forrige modulene lærte du hvordan du kan ha samtaler med AI og strukturere innspillene dine effektivt. Men det finnes en grunnleggende begrensning: språkmodeller vet bare det de lærte under treningen. De kan ikke svare på spørsmål om bedriftens retningslinjer, prosjekt-dokumentasjon eller annen informasjon de ikke ble trent på.

RAG (Hentingsforsterket Generering) løser dette problemet. I stedet for å prøve å lære modellen informasjonen din (noe som er kostbart og upraktisk), gir du den mulighet til å søke gjennom dokumentene dine. Når noen stiller et spørsmål, finner systemet relevant informasjon og inkluderer det i innspillet. Modellen svarer da basert på den hentede konteksten.

Tenk på RAG som å gi modellen et referansebibliotek. Når du stiller et spørsmål, gjør systemet følgende:

1. **Brukerspørsmål** – Du stiller et spørsmål  
2. **Innebygging** – Konverterer spørsmålet ditt til en vektor  
3. **Vektorsøk** – Finner lignende dokumentbiter  
4. **Kontekstsamling** – Legger relevante biter til innspillet  
5. **Respons** – LLM genererer svar basert på konteksten  

Dette forankrer modellens svar i dine faktiske data i stedet for å stole på treningskunnskap eller finne på svar.

## Forutsetninger

- Fullført [Modul 00 - Rask start](../00-quick-start/README.md) (for den refererte Easy RAG-eksemplet ovenfor)  
- Fullført [Modul 01 - Introduksjon](../01-introduction/README.md) (Azure OpenAI-ressurser distribuert, inkludert `text-embedding-3-small` innebyggingsmodell)  
- `.env`-fil i rotkatalog med Azure-legitimasjon (opprettet med `azd up` i Modul 01)  

> **Merk:** Hvis du ikke har fullført Modul 01, følg distribusjonsinstruksjonene der først. `azd up` kommandoen distribuerer både GPT chat-modellen og innebyggingsmodellen som brukes i denne modulen.

## Forstå RAG

Diagrammet under illustrerer kjerneideen: i stedet for bare å stole på modellens treningsdata, gir RAG den et referansebibliotek av dokumentene dine å konsultere før hvert svar genereres.

<img src="../../../translated_images/no/what-is-rag.1f9005d44b07f2d8.webp" alt="Hva er RAG" width="800"/>

*Dette diagrammet viser forskjellen mellom en standard LLM (som gjetter fra treningsdata) og en RAG-forsterket LLM (som først konsulterer dokumentene dine).*

Slik kobler bitene seg sammen fra ende til ende. Et brukerspørsmål går gjennom fire trinn — innebygging, vektorsøk, kontekstsamling, og svargenerering — der hvert steg bygger på det forrige:

<img src="../../../translated_images/no/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

*Dette diagrammet viser hele RAG-pipelinen — et brukerspørsmål går gjennom innebygging, vektorsøk, kontekstsamling og svargenerering.*

Resten av denne modulen gjennomgår hvert trinn i detalj, med kode du kan kjøre og endre.

### Hvilken RAG-tilnærming bruker denne veiledningen?

LangChain4j tilbyr tre måter å implementere RAG på, hver med ulik abstraksjonsgrad. Diagrammet nedenfor sammenligner dem side om side:

<img src="../../../translated_images/no/rag-approaches.5b97fdcc626f1447.webp" alt="Tre RAG-tilnærminger i LangChain4j" width="800"/>

*Dette diagrammet sammenligner de tre LangChain4j RAG-tilnærmingene — Easy, Native, og Advanced — og viser deres nøkkelkomponenter og når du bør bruke hver.*

| Tilnærming | Hva den gjør | Avveining |
|---|---|---|
| **Easy RAG** | Kobler alt automatisk gjennom `AiServices` og `ContentRetriever`. Du annoterer et grensesnitt, kobler til en retriever, og LangChain4j håndterer innebygging, søk, og promptsamling i bakgrunnen. | Minimalt med kode, men du ser ikke hva som skjer i hvert trinn. |
| **Native RAG** | Du kaller innebyggingsmodellen, søker i lagringen, bygger prompten, og genererer svaret selv — ett eksplisitt trinn av gangen. | Mer kode, men hvert steg er synlig og kan endres. |
| **Advanced RAG** | Bruker `RetrievalAugmentor`-rammeverket med pluggbare spørringstransformatorer, rutere, re-rankere og innholdsinnsprøytninger for produksjonspipelines. | Maksimal fleksibilitet, men betydelig mer kompleksitet. |

**Denne veiledningen bruker Native-tilnærmingen.** Hvert trinn i RAG-pipelinen — å innebygge spørsmålet, søke i vektorbutikken, samle kontekst og generere svaret — er skrevet ut eksplisitt i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Dette er bevisst: som et læringsmateriale er det viktigere at du ser og forstår hvert trinn enn at koden er minimal. Når du er komfortabel med hvordan delene passer sammen, kan du gå videre til Easy RAG for raske prototyper eller Advanced RAG for produksjonssystemer.

> **💡 Har du allerede sett Easy RAG i praksis?** [Rask start-modulen](../00-quick-start/README.md) inkluderer et dokument Q&A-eksempel ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) som bruker Easy RAG-tilnærmingen — LangChain4j håndterer innebygging, søk og promptsamling automatisk. Denne modulen tar neste steg ved å åpne opp denne pipelinen slik at du kan se og kontrollere hvert steg selv.

<img src="../../../translated_images/no/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dette diagrammet viser Easy RAG-pipelinen fra `SimpleReaderDemo.java`. Sammenlign dette med Native-tilnærmingen som brukes i denne modulen: Easy RAG skjuler innebygging, henting, og promptsamling bak `AiServices` og `ContentRetriever` — du laster opp et dokument, fester en retriever, og får svar. Native-tilnærmingen i denne modulen åpner opp denne pipelinen slik at du kaller hvert steg (innebygge, søke, samle kontekst, generere) selv, og får full synlighet og kontroll.*

## Hvordan det fungerer

RAG-pipelinen i denne modulen deles opp i fire trinn som kjøres i rekkefølge hver gang en bruker stiller et spørsmål. Først blir et opplastet dokument **parset og delt opp i biter**. Disse bitene konverteres så til **vektorinnebygginger** og lagres slik at de kan sammenlignes matematisk. Når et spørsmål kommer inn, utfører systemet et **semantisk søk** for å finne de mest relevante bitene, og sender dem så som kontekst til LLM for **generering av svar**. Seksjonene nedenfor går gjennom hvert trinn med faktisk kode og diagrammer. La oss se på det første steget.

### Dokumentbehandling

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Når du laster opp et dokument, leser systemet det (PDF eller ren tekst), legger til metadata som filnavn, og deler det så opp i biter — mindre deler som passer komfortabelt i modellens kontekstvindu. Disse bitene overlapper litt slik at du ikke mister kontekst ved grensene.

```java
// Analyser den opplastede filen og pakk den inn i et LangChain4j-dokument
Document document = Document.from(content, metadata);

// Del opp i 300-token biter med 30-token overlapp
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagrammet nedenfor viser hvordan dette fungerer visuelt. Legg merke til hvordan hver bit deler noen tokens med naboene sine — 30-token overlapping sikrer at ingen viktig kontekst faller mellom sprekkene:

<img src="../../../translated_images/no/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentdelinger" width="800"/>

*Dette diagrammet viser et dokument som deles opp i 300-token biter med 30-token overlapping, som bevarer kontekst ved bit-grensene.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) og spør:  
> - "Hvordan deler LangChain4j dokumenter inn i biter og hvorfor er overlapping viktig?"  
> - "Hva er optimal bit-størrelse for forskjellige dokumenttyper og hvorfor?"  
> - "Hvordan håndterer jeg dokumenter på flere språk eller med spesiell formatering?"

### Opprette innebygginger

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Hver bit konverteres til en numerisk representasjon kalt en innebygging — i praksis en menings-til-tall-omformer. Innebyggingsmodellen er ikke "intelligent" slik en chatmodell er; den kan ikke følge instruksjoner, resonnere eller svare på spørsmål. Det den kan gjøre er å kartlegge tekst til et matematisk rom hvor lignende betydninger havner nær hverandre — "bil" nær "automobil", "refusjonspolicy" nær "få pengene tilbake". Tenk på en chatmodell som en person du kan snakke med; en innebyggingsmodell er et ultra-godt arkivsystem.

<img src="../../../translated_images/no/embedding-model-concept.90760790c336a705.webp" alt="Konsept for innebyggingsmodell" width="800"/>

*Dette diagrammet viser hvordan en innebyggingsmodell konverterer tekst til numeriske vektorer, og plasserer lignende betydninger — som "bil" og "automobil" — nær hverandre i vektorrommet.*

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

Klasse-diagrammet nedenfor viser de to separate flytene i en RAG-pipeline og LangChain4j-klassene som implementerer dem. **Inntaksflyten** (kjøres én gang ved opplasting) deler dokumentet, innebygger bitene, og lagrer dem via `.addAll()`. **Spørringsflyten** (kjøres hver gang en bruker spør) innebygger spørsmålet, søker i butikken via `.search()`, og gir den matchende konteksten til chatmodellen. Begge flytene møtes i felles `EmbeddingStore<TextSegment>`-grensesnitt:

<img src="../../../translated_images/no/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG-klasser" width="800"/>

*Dette diagrammet viser de to flytene i en RAG-pipeline — inntak og spørring — og hvordan de kobles gjennom en delt EmbeddingStore.*

Når innebyggingene er lagret, samles lignende innhold naturlig sammen i vektorrommet. Visualiseringen nedenfor viser hvordan dokumenter om beslektede temaer ender opp som nærliggende punkter, noe som gjør semantisk søk mulig:

<img src="../../../translated_images/no/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorinnebyggingsrom" width="800"/>

*Denne visualiseringen viser hvordan relaterte dokumenter kluster sammen i 3D vektorrom, med temaer som Tekniske Dokumenter, Forretningsregler, og FAQs som former distinkte grupper.*

Når en bruker søker, følger systemet fire trinn: innebygger dokumentene én gang, innebygger spørsmålet ved hvert søk, sammenligner spørsmålsvektoren med alle lagrede vektorer ved bruk av cosinuslikhet, og returnerer de øverste K høyest scorede bitene. Diagrammet nedenfor går gjennom hvert trinn og LangChain4j-klassene som er involvert:

<img src="../../../translated_images/no/embedding-search-steps.f54c907b3c5b4332.webp" alt="Trinn i innebyggingssøk" width="800"/>

*Dette diagrammet viser den fire-trinns prosessen for innebyggingssøk: innebygge dokumenter, innebygge spørsmålet, sammenligne vektorer med cosinuslikhet, og returnere de øverste K resultatene.*

### Semantisk søk

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Når du stiller et spørsmål, blir også spørsmålet ditt gjort om til en innebygging. Systemet sammenligner spørsmålets innebygging med innebyggingene til alle dokumentbitene. Det finner bitene med mest lik betydning — ikke bare samsvarende nøkkelord, men faktisk semantisk likhet.

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

Diagrammet nedenfor viser kontrasten mellom semantisk søk og tradisjonelt nøkkelordsøk. Et nøkkelordsøk etter "kjøretøy" overser en bit om "biler og lastebiler," men semantisk søk forstår at de betyr det samme og returnerer det som et høyt scorende treff:

<img src="../../../translated_images/no/semantic-search.6b790f21c86b849d.webp" alt="Semantisk søk" width="800"/>

*Dette diagrammet sammenligner nøkkelordbasert søk med semantisk søk, og viser hvordan semantisk søk henter konseptuelt relatert innhold selv når eksakte nøkkelord ikke stemmer.*

Under panseret måles likhet med cosinuslikhet — i praksis spør "peker disse to pilene i samme retning?" To biter kan bruke helt forskjellige ord, men hvis de betyr det samme peker vektorene i samme retning og scorer nær 1,0:

<img src="../../../translated_images/no/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosinuslikhet" width="800"/>
*Dette diagrammet illustrerer cosinuslikhet som vinkelen mellom embeddingsvektorer — bedre justerte vektorer får en score nærmere 1,0, noe som indikerer høyere semantisk likhet.*

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Åpne [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) og spør:
> - "Hvordan fungerer likhetssøk med embeddings og hva bestemmer scoren?"
> - "Hvilken likhetsterskel bør jeg bruke og hvordan påvirker det resultatene?"
> - "Hvordan håndterer jeg tilfeller der ingen relevante dokumenter finnes?"

### Svargenerering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevante delstykker samles i en strukturert prompt som inkluderer eksplisitte instruksjoner, den hentede konteksten og brukerens spørsmål. Modellen leser disse spesifikke delstykkene og svarer basert på den informasjonen — den kan kun bruke det som er foran den, noe som forhindrer hallusinasjoner.

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

Diagrammet nedenfor viser denne sammensettingen i praksis — de høyest rangerte delstykkene fra søkefasen settes inn i promptmalen, og `OpenAiOfficialChatModel` genererer et forankret svar:

<img src="../../../translated_images/no/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dette diagrammet viser hvordan de høyest rangerte delstykkene settes sammen til en strukturert prompt, noe som gjør det mulig for modellen å generere et forankret svar fra dataene dine.*

## Kjør applikasjonen

**Verifiser distribusjon:**

Sørg for at `.env`-filen finnes i rotkatalogen med Azure-legitimasjon (opprettet under Modul 01):

**Bash:**
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bør vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikasjonen:**

> **Merk:** Hvis du allerede startet alle applikasjoner med `./start-all.sh` fra Modul 01, kjører denne modulen allerede på port 8081. Du kan hoppe over startkommandoene nedenfor og gå direkte til http://localhost:8081.

**Alternativ 1: Bruke Spring Boot Dashboard (Anbefalt for VS Code-brukere)**

Dev-containeren inkluderer Spring Boot Dashboard-utvidelsen, som gir et visuelt grensesnitt for å administrere alle Spring Boot-applikasjoner. Du finner den i Aktivitetsfeltet på venstre side i VS Code (se etter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgjengelige Spring Boot-applikasjoner i arbeidsområdet
- Starte/stopp applikasjoner med ett klikk
- Se applikasjonslogger i sanntid
- Overvåke applikasjonens status

Klikk enkelt på avspillingsknappen ved siden av "rag" for å starte denne modulen, eller start alle moduler samtidig.

<img src="../../../translated_images/no/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dette skjermbildet viser Spring Boot Dashboard i VS Code, hvor du visuelt kan starte, stoppe og overvåke applikasjoner.*

**Alternativ 2: Bruke shell-skript**

Start alle webapplikasjoner (modul 01-04):

**Bash:**
```bash
cd ..  # Fra rotkatalog
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

Begge skriptene laster automatisk miljøvariabler fra root `.env`-filen og bygger JAR-filene hvis de ikke eksisterer.

> **Merk:** Hvis du foretrekker å bygge alle moduler manuelt før oppstart:
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

Åpne http://localhost:8081 i nettleseren din.

**For å stoppe:**

**Bash:**
```bash
./stop.sh  # Bare denne modulen
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

<a href="images/rag-homepage.png"><img src="../../../translated_images/no/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skjermbildet viser RAG-applikasjonens grensesnitt hvor du laster opp dokumenter og stiller spørsmål.*

### Last opp et dokument

Start med å laste opp et dokument - TXT-filer fungerer best for testing. En `sample-document.txt` er tilgjengelig i denne mappen og inneholder informasjon om LangChain4j-funksjoner, RAG-implementering og beste praksis - perfekt for å teste systemet.

Systemet behandler dokumentet ditt, deler det opp i delstykker og oppretter embeddings for hver del. Dette skjer automatisk når du laster opp.

### Still spørsmål

Still nå konkrete spørsmål om dokumentets innhold. Prøv noe faglig som klart fremkommer i dokumentet. Systemet søker etter relevante delstykker, inkluderer dem i prompten og genererer et svar.

### Sjekk kildehenvisninger

Legg merke til at hvert svar inkluderer kildehenvisninger med likhetsscorer. Disse scorene (0 til 1) viser hvor relevant hvert delstykke var for spørsmålet ditt. Høyere scorer betyr bedre treff. Dette lar deg verifisere svaret mot kildematerialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/no/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dette skjermbildet viser spørresultater med generert svar, kildehenvisninger og relevanspoeng for hvert hentet delstykke.*

### Eksperimenter med spørsmål

Prøv forskjellige spørsmålsvarianter:
- Spesifikke fakta: "Hva er hovedtemaet?"
- Sammenligninger: "Hva er forskjellen mellom X og Y?"
- Sammendrag: "Oppsummer hovedpunktene om Z"

Se hvordan relevanspoengene endrer seg basert på hvor godt spørsmålet ditt matcher dokumentinnholdet.

## Nøkkelbegreper

### Chunking-strategi

Dokumenter deles opp i 300-token delstykker med 30 tokens overlapp. Denne balansen sikrer at hvert delstykke har nok kontekst til å være meningsfullt, samtidig som det er lite nok til at flere delstykker kan inkluderes i en prompt.

### Likhetsscorer

Hvert hentet delstykke kommer med en likhetsscore mellom 0 og 1 som indikerer hvor tett det matcher brukerens spørsmål. Diagrammet nedenfor visualiserer scoringsområdene og hvordan systemet bruker dem til å filtrere resultater:

<img src="../../../translated_images/no/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dette diagrammet viser scoreområder fra 0 til 1, med en minimumsterskel på 0,5 som filtrerer ut irrelevante delstykker.*

Scorene varierer fra 0 til 1:
- 0,7-1,0: Høyst relevant, eksakt treff
- 0,5-0,7: Relevant, god kontekst
- Under 0,5: Filtrert bort, for ulikt

Systemet henter kun delstykker over minimumsterskelen for å sikre kvalitet.

Embeddings fungerer godt når mening grupperes tydelig, men har blinde soner. Diagrammet nedenfor viser vanlige feilmoduser — delt stykker som er for store gir uklare vektorer, for små mangler kontekst, tvetydige termer peker til flere klynger, og eksakt-treff søk (IDer, delenummer) fungerer ikke med embeddings i det hele tatt:

<img src="../../../translated_images/no/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dette diagrammet viser vanlige feilmoduser for embeddings: delstykker for store, delstykker for små, tvetydige termer som peker til flere klynger, og eksakt-treff søk som IDer.*

### Minnebasert lagring

Denne modulen bruker minnelagring for enkelhetens skyld. Når du starter applikasjonen på nytt, tapes opplastede dokumenter. Produksjonssystemer bruker persistente vektordatabaser som Qdrant eller Azure AI Search.

### Håndtering av kontekstvindu

Hver modell har et maksimalt kontekstvindu. Du kan ikke inkludere alle delstykker fra et stort dokument. Systemet henter de N mest relevante delstykkene (standard 5) for å holde seg innenfor begrensningene samtidig som det gir nok kontekst for nøyaktige svar.

## Når RAG er relevant

RAG er ikke alltid riktig tilnærming. Vedlagte beslutningsguide hjelper deg å avgjøre når RAG gir merverdi versus når enklere metoder — som å inkludere innhold direkte i prompt eller stole på modellens innebygde kunnskap — er tilstrekkelig:

<img src="../../../translated_images/no/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dette diagrammet viser en beslutningsguide for når RAG tilfører verdi versus når enklere metoder er tilstrekkelig.*

**Bruk RAG når:**
- Du skal svare på spørsmål om proprietære dokumenter
- Informasjonen endres ofte (retningslinjer, priser, spesifikasjoner)
- Nøyaktighet krever kildehenvisning
- Innholdet er for stort til å få plass i én enkelt prompt
- Du trenger verifiserbare, forankrede svar

**Ikke bruk RAG når:**
- Spørsmål krever generell kunnskap som modellen allerede har
- Realtime-data er nødvendig (RAG fungerer på opplastede dokumenter)
- Innholdet er lite nok til å inkluderes direkte i prompten

## Neste steg

**Neste modul:** [04-tools - AI-agenter med verktøy](../04-tools/README.md)

---

**Navigasjon:** [← Forrige: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tilbake til hovedmeny](../README.md) | [Neste: Modul 04 - Verktøy →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på dets opprinnelige språk bør betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår som følge av bruken av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->