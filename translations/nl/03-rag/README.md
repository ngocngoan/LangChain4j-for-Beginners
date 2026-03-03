# Module 03: RAG (Retrieval-Augmented Generation)

## Inhoudsopgave

- [Video Walkthrough](../../../03-rag)
- [Wat Je Zal Leren](../../../03-rag)
- [Vereisten](../../../03-rag)
- [RAG Begrijpen](../../../03-rag)
  - [Welke RAG Benadering Gebruikt Deze Tutorial?](../../../03-rag)
- [Hoe Het Werkt](../../../03-rag)
  - [Documentverwerking](../../../03-rag)
  - [Embeddings Maken](../../../03-rag)
  - [Semantische Zoekopdracht](../../../03-rag)
  - [Antwoordsynthese](../../../03-rag)
- [De Applicatie Uitvoeren](../../../03-rag)
- [De Applicatie Gebruiken](../../../03-rag)
  - [Upload een Document](../../../03-rag)
  - [Stel Vragen](../../../03-rag)
  - [Controleer Bronverwijzingen](../../../03-rag)
  - [Experimenteer met Vragen](../../../03-rag)
- [Belangrijke Concepten](../../../03-rag)
  - [Chunking Strategie](../../../03-rag)
  - [Gelijkenisscores](../../../03-rag)
  - [In-Memory Opslag](../../../03-rag)
  - [Contextvensterbeheer](../../../03-rag)
- [Wanneer RAG Relevant Is](../../../03-rag)
- [Volgende Stappen](../../../03-rag)

## Video Walkthrough

Bekijk deze live sessie die uitlegt hoe te beginnen met deze module:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG met LangChain4j - Live Sessie" width="800"/></a>

## Wat Je Zal Leren

In de vorige modules leerde je hoe je gesprekken met AI voert en je prompts effectief structureert. Maar er is een fundamentele beperking: taalmodellen kennen alleen wat ze tijdens het trainen leerden. Ze kunnen geen vragen beantwoorden over het beleid van jouw bedrijf, je projectdocumentatie of informatie waar ze niet op getraind zijn.

RAG (Retrieval-Augmented Generation) lost dit probleem op. In plaats van het model jouw informatie te proberen leren (wat duur en onpraktisch is), geef je het de mogelijkheid om door je documenten te zoeken. Wanneer iemand een vraag stelt, vindt het systeem relevante informatie en voegt die toe aan de prompt. Het model geeft vervolgens een antwoord op basis van die opgehaalde context.

Zie RAG als het geven van een referentiebibliotheek aan het model. Wanneer je een vraag stelt, doet het systeem:

1. **Gebruikersvraag** – Je stelt een vraag  
2. **Embedding** – Zet je vraag om in een vector  
3. **Vectorzoekopdracht** – Vindt vergelijkbare documentstukken  
4. **Context Samenstelling** – Voegt relevante stukken toe aan de prompt  
5. **Respons** – Het LLM genereert een antwoord gebaseerd op de context  

Dit zorgt ervoor dat de antwoorden van het model zijn geworteld in je eigen data in plaats van te vertrouwen op de kennis uit de training of zelf verzonnen antwoorden.

## Vereisten

- Voltooide [Module 00 - Snelle Start](../00-quick-start/README.md) (voor het Easy RAG voorbeeld dat later in deze module wordt genoemd)  
- Voltooide [Module 01 - Introductie](../01-introduction/README.md) (Azure OpenAI bronnen gedeployed, inclusief het `text-embedding-3-small` embedding model)  
- `.env` bestand in de rootmap met Azure referenties (gemaakt door `azd up` in Module 01)  

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst de deploy-instructies daar. Het commando `azd up` deployt zowel het GPT chatmodel als het embeddingmodel dat in deze module gebruikt wordt.

## RAG Begrijpen

De onderstaande afbeelding illustreert het kernconcept: in plaats van alleen te vertrouwen op de trainingsdata van het model, geeft RAG het een referentiebibliotheek van jouw documenten om te raadplegen voordat het een antwoord genereert.

<img src="../../../translated_images/nl/what-is-rag.1f9005d44b07f2d8.webp" alt="Wat is RAG" width="800"/>

*Dit diagram toont het verschil tussen een standaard LLM (dat gokt op basis van trainingsdata) en een RAG-versterkt LLM (dat eerst je documenten raadpleegt).*

Hier zie je hoe de onderdelen end-to-end met elkaar verbonden zijn. De vraag van een gebruiker doorloopt vier fasen — embedding, vectorzoekopdracht, contextsamenstelling en antwoordsynthese — die elk voortbouwen op de vorige:

<img src="../../../translated_images/nl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architectuur" width="800"/>

*Dit diagram toont de end-to-end RAG pipeline — een gebruikersvraag doorloopt embedding, vectorzoekopdracht, contextsamenstelling en antwoordsynthese.*

De rest van deze module behandelt elke fase in detail, met code die je kunt uitvoeren en aanpassen.

### Welke RAG Benadering Gebruikt Deze Tutorial?

LangChain4j biedt drie manieren om RAG te implementeren, elk met een ander abstractieniveau. De onderstaande afbeelding vergelijkt ze naast elkaar:

<img src="../../../translated_images/nl/rag-approaches.5b97fdcc626f1447.webp" alt="Drie RAG Benaderingen in LangChain4j" width="800"/>

*Dit diagram vergelijkt de drie LangChain4j RAG benaderingen — Easy, Native, en Advanced — en toont hun belangrijkste componenten en wanneer je ze gebruikt.*

| Benadering | Wat Het Doet | Afweging |
|---|---|---|
| **Easy RAG** | Verbindt alles automatisch via `AiServices` en `ContentRetriever`. Je annoteert een interface, koppelt een retriever, en LangChain4j verzorgt embedding, zoeken en prompt samenstelling achter de schermen. | Minimale code, maar je ziet niet wat er in elke stap gebeurt. |
| **Native RAG** | Je roept zelf het embeddingmodel aan, zoekt in de opslag, bouwt de prompt en genereert het antwoord — expliciet stap voor stap. | Meer code, maar elke fase is zichtbaar en aanpasbaar. |
| **Advanced RAG** | Gebruikt het `RetrievalAugmentor` framework met inplugbare querytransformators, routers, herordenaars en inkjes van content voor productieklare pipelines. | Maximale flexibiliteit, maar aanzienlijk complexer. |

**Deze tutorial gebruikt de Native benadering.** Elke stap van de RAG pipeline — embedding van de query, zoeken in de vectoropslag, samenstellen van de context en antwoorden genereren — wordt expliciet geschreven in [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Dit is bewust: als leerbron is het belangrijker dat je elke stap ziet en begrijpt dan dat de code zo kort mogelijk is. Zodra je vertrouwd bent met hoe de onderdelen samenwerken, kun je overstappen op Easy RAG voor snelle prototypes of Advanced RAG voor productiesystemen.

> **💡 Easy RAG al eens gezien?** De [Snelle Start module](../00-quick-start/README.md) bevat een Document Q&A voorbeeld ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) dat Easy RAG gebruikt — LangChain4j verzorgt embedding, zoeken en prompt assemblage automatisch. Deze module gaat een stap verder door die pipeline open te breken zodat je elke fase ziet en zelf beheert.

De onderstaande afbeelding toont de Easy RAG pipeline van dat Quick Start voorbeeld. Let op hoe `AiServices` en `EmbeddingStoreContentRetriever` alle complexiteit verbergen — je laadt een document, koppelt een retriever, en krijgt antwoorden. De Native benadering in deze module breekt elk van die verborgen stappen open:

<img src="../../../translated_images/nl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dit diagram toont de Easy RAG pipeline uit `SimpleReaderDemo.java`. Vergelijk dit met de Native benadering in deze module: Easy RAG verbergt embedding, ophalen en promptassemblage achter `AiServices` en `ContentRetriever` — je laadt een document, koppelt een retriever en krijgt antwoorden. De Native benadering breekt die pipeline open zodat je elke fase zelf aanroept (embed, zoek, context samenstellen, genereer), wat vol zichtbaarheid en controle geeft.*

## Hoe Het Werkt

De RAG pipeline in deze module is opgedeeld in vier fasen die achtereenvolgens lopen elke keer dat een gebruiker een vraag stelt. Eerst wordt een geüpload document **geparsed en gefragmenteerd** in hanteerbare stukken. Die stukken worden vervolgens omgezet in **vector-embeddings** en opgeslagen zodat ze wiskundig vergeleken kunnen worden. Wanneer een query binnenkomt, voert het systeem een **semantische zoekopdracht** uit om de meest relevante stukken te vinden, en tenslotte geeft het die als context mee aan het LLM voor **antwoordsynthese**. Hieronder lopen we elke fase door met de werkelijke code en diagrammen. Laten we eerst stap één bekijken.

### Documentverwerking

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Wanneer je een document uploadt, wordt het geparsed (PDF of platte tekst), worden er metadata toegevoegd zoals de bestandsnaam, en breekt het systeem het op in stukken — kleinere delen die comfortabel in het contextvenster van het model passen. Deze stukken overlappen iets zodat je geen context verliest op de grenzen.

```java
// Analyseer het geüploade bestand en wikkel het in een LangChain4j Document
Document document = Document.from(content, metadata);

// Verdeel in stukken van 300 tokens met 30 tokens overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

De onderstaande afbeelding toont hoe dit visueel werkt. Let op hoe elk stuk wat tokens deelt met zijn buren — de overlap van 30 tokens zorgt ervoor dat er geen belangrijke context tussen de kieren valt:

<img src="../../../translated_images/nl/document-chunking.a5df1dd1383431ed.webp" alt="Document Fragmentatie" width="800"/>

*Dit diagram toont een document dat wordt verdeeld in 300-token stukken met 30-token overlap, waarbij de context bij de stukgrenzen behouden blijft.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) en vraag:
> - "Hoe splits LangChain4j documenten in stukken en waarom is overlap belangrijk?"
> - "Wat is de optimale stukgrootte voor verschillende documenttypen en waarom?"
> - "Hoe ga ik om met documenten in meerdere talen of met speciale opmaak?"

### Embeddings Maken

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Elk stuk wordt omgezet in een numerieke representatie genaamd embedding — eigenlijk een converter van betekenis naar getallen. Het embeddingmodel is niet "intelligent" zoals een chatmodel; het kan geen instructies opvolgen, redeneren of vragen beantwoorden. Wat het wel kan, is tekst mappen naar een wiskundige ruimte waarin vergelijkbare betekenissen dicht bij elkaar liggen — "auto" dichtbij "voertuig", "terugbetalingsbeleid" dichtbij "geld terug". Zie een chatmodel als een persoon met wie je praat; een embeddingmodel is een supersnel archiveringssysteem.

De onderstaande afbeelding visualiseert dit concept — tekst gaat erin, numerieke vectoren komen eruit, en vergelijkbare betekenissen produceren nabijgelegen vectoren:

<img src="../../../translated_images/nl/embedding-model-concept.90760790c336a705.webp" alt="Concept Embedding Model" width="800"/>

*Dit diagram toont hoe een embeddingmodel tekst omzet in numerieke vectoren, waarbij vergelijkbare betekenissen — zoals "auto" en "voertuig" — dicht bij elkaar liggen in vectorruimte.*

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

Het klassendiagram hieronder toont de twee aparte stromen in een RAG pipeline en de LangChain4j klassen die ze implementeren. De **inname-stroom** (loopt één keer bij upload) splitst het document, embedt de stukken en slaat ze op via `.addAll()`. De **query-stroom** (loopt elke keer een gebruiker iets vraagt) embedt de vraag, zoekt in de opslag via `.search()`, en geeft de gevonden context door aan het chatmodel. Beiden komen samen bij de gedeelde `EmbeddingStore<TextSegment>` interface:

<img src="../../../translated_images/nl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klassen" width="800"/>

*Dit diagram toont de twee stromen in een RAG pipeline — inname en query — en hoe ze verbonden zijn via een gedeelde EmbeddingStore.*

Zodra embeddings opgeslagen zijn, clustert gelijkaardige inhoud vanzelf samen in vectorruimte. De onderstaande visualisatie toont hoe documenten over verwante onderwerpen als nabijgelegen punten eindigen, wat semantisch zoeken mogelijk maakt:

<img src="../../../translated_images/nl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Ruimte" width="800"/>

*Deze visualisatie toont hoe gerelateerde documenten clusteren in 3D vectorruimte, met onderwerpen als Technische Documentatie, Bedrijfsregels en FAQ's die aparte groepen vormen.*

Wanneer een gebruiker zoekt, doorloopt het systeem vier stappen: embed de documenten één keer, embed de query bij iedere zoekopdracht, vergelijk de queryvector met alle opgeslagen vectoren via cosinus gelijkenis, en retourneer de top-K hoogst scorende stukken. Het onderstaande diagram licht elke stap en de betrokken LangChain4j klassen toe:

<img src="../../../translated_images/nl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Stappen Embed Ophalen" width="800"/>

*Dit diagram toont het vier-stappen proces van embedding zoeken: embed documenten, embed de query, vergelijk vectoren met cosinus gelijkenis, en retourneer top-K resultaten.*

### Semantische Zoekopdracht

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Wanneer je een vraag stelt, wordt die vraag ook een embedding. Het systeem vergelijkt de embedding van je vraag met die van alle documentstukken. Het vindt de stukken met de meest vergelijkbare betekenissen — niet alleen exacte trefwoorden, maar echte semantische gelijkenis.

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

Het onderstaande diagram vergelijkt semantisch zoeken met traditionele trefwoordzoekopdrachten. Een trefwoordzoekopdracht op "voertuig" mist een stuk over "auto's en vrachtwagens," maar semantisch zoeken begrijpt dat het hetzelfde betekent en geeft het als hoge-score resultaat terug:

<img src="../../../translated_images/nl/semantic-search.6b790f21c86b849d.webp" alt="Semantisch Zoeken" width="800"/>

*Dit diagram vergelijkt trefwoordgebaseerd zoeken met semantisch zoeken, en toont hoe semantisch zoeken conceptueel gerelateerde inhoud terugvindt, zelfs als precieze trefwoorden verschillen.*
Onder de motorkap wordt gelijkenis gemeten met behulp van cosinusgelijkenis — in wezen de vraag gesteld "wijzen deze twee pijlen in dezelfde richting?" Twee stukken kunnen totaal verschillende woorden gebruiken, maar als ze hetzelfde betekenen wijzen hun vectoren dezelfde kant op en scoren ze dicht bij 1,0:

<img src="../../../translated_images/nl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Dit diagram illustreert cosinusgelijkenis als de hoek tussen embedding-vectoren — meer uitgelijnde vectoren scoren dichter bij 1,0, wat duidt op hogere semantische gelijkenis.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) en vraag:
> - "Hoe werkt gelijkenis zoeken met embeddings en wat bepaalt de score?"
> - "Welke gelijkenisdrempel moet ik gebruiken en hoe beïnvloedt dat de resultaten?"
> - "Hoe ga ik om met gevallen waarin geen relevante documenten worden gevonden?"

### Antwoord Generatie

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De meest relevante stukken worden samengevoegd in een gestructureerde prompt die expliciete instructies, de opgehaalde context en de vraag van de gebruiker bevat. Het model leest die specifieke stukken en geeft antwoord op basis van die informatie — het kan alleen gebruiken wat voor zich ligt, wat hallucinaties voorkomt.

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

Het onderstaande diagram toont deze samenstelling in actie — de best scorende stukken van de zoekstap worden ingevoegd in het prompttemplate, en het `OpenAiOfficialChatModel` genereert een gebaseerd antwoord:

<img src="../../../translated_images/nl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dit diagram laat zien hoe de best scorende stukken worden samengevoegd in een gestructureerde prompt, waardoor het model een op feiten gebaseerd antwoord uit jouw data kan genereren.*

## Start de Applicatie

**Controleer de implementatie:**

Zorg dat het `.env`-bestand aanwezig is in de hoofdmap met Azure-inloggegevens (gemaakt tijdens Module 01). Voer dit uit vanuit de modulemap (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Opmerking:** Als je alle applicaties al gestart hebt met `./start-all.sh` vanuit de hoofdmap (zoals beschreven in Module 01), draait deze module al op poort 8081. Je kunt de startcommando’s hieronder overslaan en direct naar http://localhost:8081 gaan.

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De devcontainer bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-applicaties te beheren. Je vindt deze in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot-icoon).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de werkruimte zien
- Applicaties met één klik starten/stoppen
- Applicatielogs realtime bekijken
- Applicatiestatus monitoren

Klik simpelweg op de afspeelknop naast "rag" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Deze schermafbeelding toont het Spring Boot Dashboard in VS Code, waar je applicaties visueel kunt starten, stoppen en monitoren.*

**Optie 2: Gebruik shell-scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanaf de root-directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanaf de root directory
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

Beide scripts laden automatisch de omgevingsvariabelen uit het root `.env`-bestand en bouwen de JARs als deze nog niet bestaan.

> **Opmerking:** Als je liever handmatig alle modules bouwt vóór het starten:
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

## Gebruik van de Applicatie

De applicatie biedt een webinterface voor het uploaden van documenten en het stellen van vragen.

<a href="images/rag-homepage.png"><img src="../../../translated_images/nl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Deze schermafbeelding toont de RAG-applicatie-interface waar je documenten uploadt en vragen stelt.*

### Upload een Document

Begin met het uploaden van een document — TXT-bestanden werken het beste voor testen. Er is een `sample-document.txt` beschikbaar in deze map met informatie over LangChain4j-functionaliteiten, RAG-implementatie en best practices — perfect om het systeem te testen.

Het systeem verwerkt je document, verdeelt het in stukken en maakt embeddings voor elk stuk. Dit gebeurt automatisch bij uploaden.

### Stel Vragen

Stel nu specifieke vragen over de inhoud van het document. Probeer iets feitelijk dat duidelijk in het document staat. Het systeem zoekt naar relevante stukken, voegt deze toe aan de prompt en genereert een antwoord.

### Controleer Bronnen

Let erop dat elk antwoord bronverwijzingen bevat met gelijkenisscores. Deze scores (0 tot 1) tonen hoe relevant elk stuk was voor jouw vraag. Hogere scores betekenen betere overeenkomsten. Dit stelt je in staat het antwoord te verifiëren aan de hand van de bronmaterialen.

<a href="images/rag-query-results.png"><img src="../../../translated_images/nl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Deze schermafbeelding toont queryresultaten met het gegenereerde antwoord, bronverwijzingen en relevantiescores voor elk opgehaald stuk.*

### Experimenteer met Vragen

Probeer verschillende soorten vragen:
- Specifieke feiten: "Wat is het hoofdonderwerp?"
- Vergelijkingen: "Wat is het verschil tussen X en Y?"
- Samenvattingen: "Vat de belangrijkste punten over Z samen"

Kijk hoe de relevantiescores veranderen op basis van hoe goed jouw vraag overeenkomt met de inhoud van het document.

## Kernbegrippen

### Chunking Strategie

Documenten worden opgesplitst in stukken van 300 tokens met 30 tokens overlap. Deze balans zorgt ervoor dat elk stuk voldoende context heeft om betekenisvol te zijn en toch klein genoeg blijft om meerdere stukken in een prompt op te nemen.

### Gelijkenisscores

Elk opgehaald stuk heeft een gelijkenisscore tussen 0 en 1 die aangeeft hoe goed het aansluit op de vraag van de gebruiker. Het onderstaande diagram visualiseert de score-interval en hoe het systeem deze gebruikt om resultaten te filteren:

<img src="../../../translated_images/nl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dit diagram toont scorebereiken van 0 tot 1, met een minimumdrempel van 0,5 die irrelevante stukken filtert.*

Scores variëren van 0 tot 1:
- 0,7-1,0: Zeer relevant, exacte overeenkomst
- 0,5-0,7: Relevant, goede context
- Onder 0,5: Gefilterd, te verschillend

Het systeem haalt alleen stukken boven de minimale drempel op om kwaliteit te garanderen.

Embeddings werken goed wanneer betekenissen duidelijk clusteren, maar ze hebben blinde vlekken. Het onderstaande diagram toont de veelvoorkomende faalwijzen — stukken die te groot zijn produceren vage vectoren, stukken die te klein zijn missen context, dubbelzinnige termen wijzen naar meerdere clusters, en exacte-match zoekacties (IDs, serienummers) werken helemaal niet met embeddings:

<img src="../../../translated_images/nl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dit diagram toont veel voorkomende embedding-fouten: stukken die te groot zijn, stukken die te klein zijn, dubbelzinnige termen die naar meerdere clusters wijzen en exacte-match zoekacties zoals IDs.*

### In-Memory Opslag

Deze module gebruikt voor eenvoud in-memory opslag. Bij het herstarten van de applicatie gaan geüploade documenten verloren. Productiesystemen gebruiken persistente vectordatabases zoals Qdrant of Azure AI Search.

### Context Window Beheer

Elk model heeft een maximale contextomvang. Je kunt niet elk stuk uit een groot document meenemen. Het systeem haalt de top N meest relevante stukken op (standaard 5) om binnen de limieten te blijven en toch voldoende context te bieden voor accurate antwoorden.

## Wanneer RAG Belangrijk is

RAG is niet altijd de juiste aanpak. De onderstaande beslisgids helpt bepalen wanneer RAG waarde toevoegt versus wanneer eenvoudigere benaderingen — zoals het direct opnemen van inhoud in de prompt of vertrouwen op de ingebouwde kennis van het model — volstaan:

<img src="../../../translated_images/nl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dit diagram toont een beslisgids voor wanneer RAG waarde toevoegt tegenover wanneer eenvoudigere benaderingen voldoende zijn.*

## Volgende Stappen

**Volgende Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigatie:** [← Vorige: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel wij streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet als de gezaghebbende bron worden beschouwd. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->