# Module 03: RAG (Retrieval-Augmented Generation)

## Inhoudsopgave

- [Wat je zult leren](../../../03-rag)
- [Begrijpen van RAG](../../../03-rag)
- [Vereisten](../../../03-rag)
- [Hoe het werkt](../../../03-rag)
  - [Documentverwerking](../../../03-rag)
  - [Embeddings maken](../../../03-rag)
  - [Semantisch zoeken](../../../03-rag)
  - [Antwoordgeneratie](../../../03-rag)
- [De applicatie uitvoeren](../../../03-rag)
- [De applicatie gebruiken](../../../03-rag)
  - [Een document uploaden](../../../03-rag)
  - [Vragen stellen](../../../03-rag)
  - [Controleer bronverwijzingen](../../../03-rag)
  - [Experimenteer met vragen](../../../03-rag)
- [Belangrijke begrippen](../../../03-rag)
  - [Chunking-strategie](../../../03-rag)
  - [Gelijkenisscores](../../../03-rag)
  - [In geheugenopslag](../../../03-rag)
  - [Contextvensterbeheer](../../../03-rag)
- [Wanneer RAG relevant is](../../../03-rag)
- [Volgende stappen](../../../03-rag)

## Wat je zult leren

In de vorige modules heb je geleerd hoe je gesprekken voert met AI en hoe je je prompts effectief structureert. Maar er is een fundamentele beperking: taalmodellen weten alleen wat ze tijdens de training hebben geleerd. Ze kunnen geen vragen beantwoorden over het beleid van jouw bedrijf, je projectdocumentatie of enige informatie waarop ze niet getraind zijn.

RAG (Retrieval-Augmented Generation) lost dit probleem op. In plaats van het model jouw informatie te proberen te leren (wat duur en onpraktisch is), geef je het de mogelijkheid om je documenten te doorzoeken. Wanneer iemand een vraag stelt, vindt het systeem relevante informatie en neemt die op in de prompt. Het model beantwoordt dan op basis van die opgehaalde context.

Beschouw RAG als het geven van een referentiebibliotheek aan het model. Wanneer je een vraag stelt, doet het systeem het volgende:

1. **Gebruikersvraag** - Je stelt een vraag  
2. **Embedding** - Zet je vraag om in een vector  
3. **Vectozzoektocht** - Vindt vergelijkbare documentfragmenten  
4. **Contextsamenstelling** - Voegt relevante fragmenten toe aan de prompt  
5. **Antwoord** - LLM genereert een antwoord op basis van de context  

Dit verankert de reacties van het model in jouw daadwerkelijke gegevens in plaats van te vertrouwen op zijn trainingskennis of het verzinnen van antwoorden.

## Begrijpen van RAG

Het onderstaande diagram illustreert het kernconcept: in plaats van alleen te vertrouwen op de trainingsdata van het model, geeft RAG het een referentiebibliotheek van jouw documenten om te raadplegen voordat het elk antwoord genereert.

<img src="../../../translated_images/nl/what-is-rag.1f9005d44b07f2d8.webp" alt="Wat is RAG" width="800"/>

Zo sluiten de onderdelen van begin tot eind op elkaar aan. De vraag van een gebruiker doorloopt vier fasen — embedding, vectozzoektocht, contextsamenstelling en antwoordgeneratie — waarbij elke fase voortbouwt op de vorige:

<img src="../../../translated_images/nl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architectuur" width="800"/>

De rest van deze module loopt elke fase in detail door, met code die je kunt uitvoeren en aanpassen.

## Vereisten

- Voltooide Module 01 (Azure OpenAI resources gedeployed)  
- `.env` bestand in de hoofdmap met Azure referenties (gemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet voltooid hebt, volg dan eerst de implementatie-instructies daar.

## Hoe het werkt

### Documentverwerking

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Wanneer je een document uploadt, parseert het systeem dit (PDF of platte tekst), voegt metadata toe zoals bestandsnaam en splitst het vervolgens op in chunks — kleinere stukken die comfortabel in het contextvenster van het model passen. Deze chunks overlappen enigszins zodat je geen context aan de randen verliest.

```java
// Parse het geüploade bestand en verpakt het in een LangChain4j Document
Document document = Document.from(content, metadata);

// Splits in stukken van 300 tokens met een overlap van 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Het onderstaande diagram laat visueel zien hoe dit werkt. Merk op dat elk chunk enkele tokens deelt met zijn buren — de overlap van 30 tokens zorgt ervoor dat geen belangrijke context verloren gaat:

<img src="../../../translated_images/nl/document-chunking.a5df1dd1383431ed.webp" alt="Document opsplitsing in chunks" width="800"/>

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) en vraag:  
> - "Hoe verdeelt LangChain4j documenten in chunks en waarom is overlap belangrijk?"  
> - "Wat is de optimale chunk-grootte voor verschillende documenttypen en waarom?"  
> - "Hoe ga ik om met documenten in meerdere talen of met speciale opmaak?"

### Embeddings maken

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Elk chunk wordt omgezet in een numerieke representatie die embedding heet - in essentie een mathematisch 'vingerafdruk' die de betekenis van de tekst vastlegt. Vergelijkbare teksten produceren vergelijkbare embeddings.

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
  
Het klasse-diagram hieronder toont hoe deze LangChain4j-componenten schakelen. `OpenAiOfficialEmbeddingModel` zet tekst om in vectoren, `InMemoryEmbeddingStore` slaat de vectoren samen met hun originele `TextSegment` gegevens op, en `EmbeddingSearchRequest` regelt retrievalparameters zoals `maxResults` en `minScore`:

<img src="../../../translated_images/nl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klassen" width="800"/>

Zodra embeddings zijn opgeslagen, clustert vergelijkbare inhoud natuurlijk samen in de vectorruimte. De visualisatie hieronder toont hoe documenten over gerelateerde onderwerpen zich als nabijgelegen punten groeperen, wat semantische zoekmogelijkheden mogelijk maakt:

<img src="../../../translated_images/nl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embedding Ruimte" width="800"/>

### Semantisch zoeken

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Wanneer je een vraag stelt, wordt jouw vraag ook omgezet in een embedding. Het systeem vergelijkt de embedding van jouw vraag met alle embeddings van documentchunks. Het vindt de chunks met de meest vergelijkbare betekenissen - niet alleen overeenkomstige zoekwoorden, maar daadwerkelijke semantische gelijkenis.

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
  
Het diagram hieronder vergelijkt semantisch zoeken met traditioneel zoekwoord zoeken. Een zoekwoordzoektocht naar "voertuig" mist een chunk over "auto's en vrachtwagens", maar semantisch zoeken begrijpt dat ze hetzelfde betekenen en geeft het terug als een hoog scorende match:

<img src="../../../translated_images/nl/semantic-search.6b790f21c86b849d.webp" alt="Semantisch Zoeken" width="800"/>

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) en vraag:  
> - "Hoe werkt gelijkeniszoektocht met embeddings en wat bepaalt de score?"  
> - "Welke gelijkenisdrempel moet ik gebruiken en hoe beïnvloedt dat de resultaten?"  
> - "Hoe ga ik om met situaties waarin geen relevante documenten worden gevonden?"

### Antwoordgeneratie

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De meest relevante chunks worden samengevoegd tot een gestructureerde prompt die expliciete instructies, de opgehaalde context en de gebruikersvraag bevat. Het model leest die specifieke chunks en beantwoordt op basis van die informatie — het kan alleen gebruiken wat voor het ligt ligt, wat hallucinaties voorkomt.

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
  
Het diagram hieronder toont deze assemblage in actie — de hoogst scorende chunks uit de zoekstap worden geïnjecteerd in de prompttemplate, en `OpenAiOfficialChatModel` genereert een verankerd antwoord:

<img src="../../../translated_images/nl/context-assembly.7e6dd60c31f95978.webp" alt="Contextsamenstelling" width="800"/>

## De applicatie uitvoeren

**Verifieer de inzet:**  

Controleer dat het `.env` bestand bestaat in de hoofdmap met Azure referenties (gemaakt tijdens Module 01):  
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```
  
**Start de applicatie:**  

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` vanaf Module 01, draait deze module al op poort 8081. Je kunt de startcommando's hieronder dan overslaan en rechtstreeks naar http://localhost:8081 gaan.

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-applicaties te beheren. Je vindt deze in de Actiebalk links in VS Code (zoek naar het Spring Boot icoon).

Vanaf het Spring Boot Dashboard kun je:  
- Alle beschikbare Spring Boot-applicaties in de workspace zien  
- Applicaties starten/stoppen met één klik  
- Applicatielogs realtime bekijken  
- Applicatiestatus monitoren  

Klik gewoon op de afspeelknop naast "rag" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**  
```bash
cd ..  # Vanuit de rootdirectory
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Vanuit de root directory
.\start-all.ps1
```
  
Of start alleen deze module:

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
  
Beide scripts laden automatisch omgevingsvariabelen uit het `.env` bestand in de hoofdmap en bouwen de JARs als ze nog niet bestaan.

> **Opmerking:** Als je liever alle modules handmatig bouwt voordat je opstart:  
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
  

Open http://localhost:8081 in je browser.

**Om te stoppen:**

**Bash:**  
```bash
./stop.sh  # Alleen deze module
# Of
cd .. && ./stop-all.sh  # Alle modules
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Alleen deze module
# Of
cd ..; .\stop-all.ps1  # Alle modules
```
  

## De applicatie gebruiken

De applicatie biedt een webinterface voor documentupload en het stellen van vragen.

<a href="images/rag-homepage.png"><img src="../../../translated_images/nl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Applicatie Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*De RAG applicatie-interface - upload documenten en stel vragen*

### Een document uploaden

Begin met het uploaden van een document — TXT-bestanden zijn het beste voor testen. Er is een `sample-document.txt` aanwezig in deze map met informatie over LangChain4j-functies, RAG-implementatie en best practices — perfect om het systeem te testen.

Het systeem verwerkt je document, splitst het in chunks en maakt embeddings voor elke chunk. Dit gebeurt automatisch bij uploaden.

### Vragen stellen

Stel nu specifieke vragen over de inhoud van het document. Probeer iets feitelijks dat duidelijk in het document staat. Het systeem zoekt relevante chunks, voegt deze toe aan de prompt en genereert een antwoord.

### Controleer bronverwijzingen

Let op dat elk antwoord bronverwijzingen bevat met gelijkenisscores. Deze scores (0 tot 1) tonen hoe relevant elk chunk was voor jouw vraag. Hogere scores betekenen betere overeenkomsten. Zo kun je het antwoord verifiëren tegen de bronmaterialen.

<a href="images/rag-query-results.png"><img src="../../../translated_images/nl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Resultaten van Vraag" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Resultaten van query's met antwoord, bronverwijzingen en relevantiescores*

### Experimenteer met vragen

Probeer verschillende soorten vragen:  
- Specifieke feiten: "Wat is het hoofdonderwerp?"  
- Vergelijkingen: "Wat is het verschil tussen X en Y?"  
- Samenvattingen: "Vat de belangrijkste punten over Z samen"  

Bekijk hoe de relevantiescores veranderen afhankelijk van hoe goed je vraag overeenkomt met de documentinhoud.

## Belangrijke begrippen

### Chunking-strategie

Documenten worden opgesplitst in 300-token chunks met 30 tokens overlap. Deze balans zorgt ervoor dat elk chunk genoeg context heeft om betekenisvol te zijn, maar klein genoeg blijft om meerdere chunks in een prompt op te nemen.

### Gelijkenisscores

Elke opgehaalde chunk wordt geleverd met een gelijkenisscore tussen 0 en 1 die aangeeft hoe goed het aansluit bij de vraag van de gebruiker. Het onderstaande diagram visualiseert de scorebereiken en hoe het systeem ze gebruikt om resultaten te filteren:

<img src="../../../translated_images/nl/similarity-scores.b0716aa911abf7f0.webp" alt="Gelijkenisscores" width="800"/>

Scores lopen van 0 tot 1:  
- 0,7-1,0: Zeer relevant, exacte match  
- 0,5-0,7: Relevant, goede context  
- Lager dan 0,5: Uitgefilterd, te verschillend  

Het systeem haalt alleen chunks op boven de minimale drempel om kwaliteit te waarborgen.

### In geheugenopslag

Deze module gebruikt opslag in het geheugen voor eenvoud. Wanneer je de applicatie herstart, gaan geüploade documenten verloren. Productiesystemen gebruiken persistente vectordatabases zoals Qdrant of Azure AI Search.

### Contextvensterbeheer

Elk model heeft een maximale contextvenstergrootte. Je kunt niet elk chunk uit een groot document opnemen. Het systeem haalt de top-N meest relevante chunks op (standaard 5) om binnen de limieten te blijven en toch genoeg context te bieden voor accurate antwoorden.

## Wanneer RAG relevant is

RAG is niet altijd de juiste aanpak. De beslissingsgids hieronder helpt je bepalen wanneer RAG waarde toevoegt versus wanneer eenvoudigere benaderingen — zoals inhoud direct in de prompt opnemen of vertrouwen op de ingebouwde kennis van het model — voldoende zijn:

<img src="../../../translated_images/nl/when-to-use-rag.1016223f6fea26bc.webp" alt="Wanneer RAG te gebruiken" width="800"/>

**Gebruik RAG wanneer:**
- Vragen beantwoorden over propriëtaire documenten  
- Informatie verandert vaak (beleid, prijzen, specificaties)  
- Nauwkeurigheid vereist bronvermelding  
- Inhoud is te groot om in één prompt te passen  
- Je hebt verifieerbare, onderbouwde antwoorden nodig  

**Gebruik RAG niet wanneer:**  
- Vragen algemene kennis vereisen die het model al heeft  
- Real-time data nodig is (RAG werkt op geüploade documenten)  
- Inhoud klein genoeg is om direct in prompts op te nemen  

## Volgende stappen  

**Volgend module:** [04-tools - AI Agents with Tools](../04-tools/README.md)  

---  

**Navigatie:** [← Vorige: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Terug naar hoofdmenu](../README.md) | [Volgende: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->