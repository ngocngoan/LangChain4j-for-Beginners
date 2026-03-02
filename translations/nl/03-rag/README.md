# Module 03: RAG (Retrieval-Augmented Generation)

## Inhoudsopgave

- [Video-rondleiding](../../../03-rag)
- [Wat Je Zal Leren](../../../03-rag)
- [Vereisten](../../../03-rag)
- [RAG Begrijpen](../../../03-rag)
  - [Welke RAG Benadering Gebruikt Deze Tutorial?](../../../03-rag)
- [Hoe Het Werkt](../../../03-rag)
  - [Documentverwerking](../../../03-rag)
  - [Embeddings Maken](../../../03-rag)
  - [Semantisch Zoeken](../../../03-rag)
  - [Antwoord Generatie](../../../03-rag)
- [De Applicatie Uitvoeren](../../../03-rag)
- [De Applicatie Gebruiken](../../../03-rag)
  - [Een Document Uploaden](../../../03-rag)
  - [Stel Vragen](../../../03-rag)
  - [Controleer Bronneverwijzingen](../../../03-rag)
  - [Experimenteer met Vragen](../../../03-rag)
- [Belangrijke Concepten](../../../03-rag)
  - [Chunking Strategie](../../../03-rag)
  - [Similariteit Scores](../../../03-rag)
  - [In-Memory Opslag](../../../03-rag)
  - [Contextvenster Beheer](../../../03-rag)
- [Wanneer RAG Belangrijk Is](../../../03-rag)
- [Volgende Stappen](../../../03-rag)

## Video-rondleiding

Bekijk deze live sessie die uitlegt hoe je aan de slag gaat met deze module:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Wat Je Zal Leren

In de vorige modules heb je geleerd hoe je gesprekken met AI kunt voeren en je prompts effectief kunt structureren. Maar er is een fundamentele beperking: taalmodellen weten alleen wat ze tijdens de training hebben geleerd. Ze kunnen geen vragen beantwoorden over het beleid van jouw bedrijf, je projectdocumentatie, of informatie waarop ze niet getraind zijn.

RAG (Retrieval-Augmented Generation) lost dit probleem op. In plaats van het model je informatie te proberen te leren (wat duur en onpraktisch is), geef je het de mogelijkheid om in je documenten te zoeken. Wanneer iemand een vraag stelt, vindt het systeem relevante informatie en voegt die toe aan de prompt. Het model beantwoordt vervolgens de vraag op basis van die opgehaalde context.

Zie RAG als het geven van een referentiebibliotheek aan het model. Wanneer je een vraag stelt, doet het systeem het volgende:

1. **Gebruikersvraag** - Je stelt een vraag  
2. **Embedding** - Zet je vraag om naar een vector  
3. **Vector Zoeken** - Vindt vergelijkbare document-chunks  
4. **Context Samenstellen** - Voegt relevante chunks toe aan de prompt  
5. **Antwoord** - Het LLM genereert een antwoord op basis van de context

Dit geeft het model reacties die gebaseerd zijn op jouw daadwerkelijke data in plaats van te vertrouwen op zijn trainingskennis of het verzinnen van antwoorden.

## Vereisten

- Voltooide [Module 00 - Quick Start](../00-quick-start/README.md) (voor het Easy RAG voorbeeld genoemd hierboven)  
- Voltooide [Module 01 - Inleiding](../01-introduction/README.md) (Azure OpenAI resources gedeployed, inclusief het `text-embedding-3-small` embeddingmodel)  
- `.env` bestand in de hoofdmap met Azure-gegevens (gemaakt door `azd up` in Module 01)

> **Opmerking:** Als je Module 01 nog niet hebt voltooid, volg dan eerst daar de deployment-instructies. De `azd up` opdracht deployed zowel het GPT chatmodel als het embeddingmodel dat door deze module wordt gebruikt.

## RAG Begrijpen

De onderstaande afbeelding illustreert het kernconcept: in plaats van alleen te vertrouwen op de trainingsdata van het model, geeft RAG het een referentiebibliotheek van jouw documenten om te raadplegen voordat het elk antwoord genereert.

<img src="../../../translated_images/nl/what-is-rag.1f9005d44b07f2d8.webp" alt="Wat is RAG" width="800"/>

*Deze afbeelding toont het verschil tussen een standaard LLM (die raadt vanuit trainingsdata) en een RAG-verrijkte LLM (die eerst jouw documenten raadpleegt).*

Dit is hoe de onderdelen end-to-end zijn verbonden. De vraag van een gebruiker doorloopt vier fasen — embedding, vector zoeken, context samenstellen, en antwoord genereren — elk bouwt voort op de vorige:

<img src="../../../translated_images/nl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architectuur" width="800"/>

*Deze afbeelding toont de end-to-end RAG-pijplijn — een gebruikersvraag doorloopt embedding, vector zoeken, context samenstellen en antwoordgeneratie.*

De rest van deze module bespreekt elke fase in detail, met code die je kunt uitvoeren en aanpassen.

### Welke RAG Benadering Gebruikt Deze Tutorial?

LangChain4j biedt drie manieren om RAG te implementeren, elk met een ander abstractieniveau. De onderstaande afbeelding vergelijkt ze naast elkaar:

<img src="../../../translated_images/nl/rag-approaches.5b97fdcc626f1447.webp" alt="Drie RAG Benaderingen in LangChain4j" width="800"/>

*Deze afbeelding vergelijkt de drie LangChain4j RAG-benaderingen — Easy, Native en Advanced — met hun belangrijkste componenten en wanneer ze te gebruiken.*

| Benadering | Wat Het Doet | Afweging |
|---|---|---|
| **Easy RAG** | Verbindt alles automatisch via `AiServices` en `ContentRetriever`. Je annoteert een interface, koppelt een retriever en LangChain4j regelt embedding, zoeken en promptbouw achter de schermen. | Minimale code, maar je ziet niet wat er bij elke stap gebeurt. |
| **Native RAG** | Je roept zelf het embeddingmodel aan, zoekt in de opslag, bouwt de prompt en genereert het antwoord — één expliciete stap per keer. | Meer code, maar elke fase is zichtbaar en aanpasbaar. |
| **Advanced RAG** | Gebruikt het `RetrievalAugmentor` framework met pluginbare query transformers, routers, re-rankers, en content injectors voor productieklare pijplijnen. | Maximale flexibiliteit, maar aanzienlijk meer complexiteit. |

**Deze tutorial gebruikt de Native benadering.** Elke stap van de RAG-pijplijn — het embedden van de query, zoeken in de vectoren, het samenstellen van de context, en het genereren van het antwoord — is expliciet uitgewerkt in [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Dit is bewust: als leerresource is het belangrijker dat je elke fase ziet en begrijpt dan dat de code zo compact mogelijk is. Zodra je comfortabel bent met hoe de onderdelen samenhangen, kun je overstappen naar Easy RAG voor snelle prototypes of Advanced RAG voor productiesystemen.

> **💡 Easy RAG al in actie gezien?** De [Quick Start module](../00-quick-start/README.md) bevat een Document Q&A voorbeeld ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) die de Easy RAG benadering gebruikt — LangChain4j regelt embedding, zoeken en promptopbouw automatisch. Deze module neemt de volgende stap door die pijplijn open te breken zodat je elke fase zelf kan zien en aansturen.

<img src="../../../translated_images/nl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pijplijn - LangChain4j" width="800"/>

*Deze afbeelding toont de Easy RAG-pijplijn uit `SimpleReaderDemo.java`. Vergelijk dit met de Native aanpak in deze module: Easy RAG verbergt embedding, zoeken en promptopbouw achter `AiServices` en `ContentRetriever` — je laad een document, koppelt een retriever, en krijgt antwoorden. De Native benadering in deze module breekt die pijplijn open zodat je iedere stap (embedden, zoeken, context samenstellen, genereren) zelf uitvoert, waardoor je volledige zichtbaarheid en controle hebt.*

## Hoe Het Werkt

De RAG-pijplijn in deze module is onderverdeeld in vier fasen die opeenvolgend worden uitgevoerd telkens wanneer een gebruiker een vraag stelt. Eerst wordt een geüpload document **geparsed en opgedeeld** in hanteerbare stukken. Die stukken worden vervolgens omgezet in **vector embeddings** en opgeslagen zodat ze mathematisch vergeleken kunnen worden. Wanneer een query binnenkomt, voert het systeem een **semantische zoekopdracht** uit om de meest relevante stukken te vinden, en geeft deze tenslotte mee als context aan de LLM voor **antwoordgeneratie**. De onderstaande secties lopen elke fase door met echte code en diagrammen. Laten we beginnen met de eerste stap.

### Documentverwerking

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Wanneer je een document uploadt, parseert het systeem het (PDF of platte tekst), koppelt metadata zoals de bestandsnaam, en splitst het dan in stukken — kleinere stukken die comfortabel in het contextvenster van het model passen. Deze stukken overlappen licht zodat je geen context verliest op de grenzen.

```java
// Analyseer het geüploade bestand en wikkel het in een LangChain4j Document
Document document = Document.from(content, metadata);

// Splits in stukken van 300 tokens met een overlap van 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
De afbeelding hieronder laat visueel zien hoe dit werkt. Let op hoe elk stuk enkele tokens deelt met zijn buren — de overlap van 30 tokens zorgt ervoor dat geen belangrijke context tussen de mazen valt:

<img src="../../../translated_images/nl/document-chunking.a5df1dd1383431ed.webp" alt="Document Opsplitsing in Stukken" width="800"/>

*Deze afbeelding toont een document dat is gesplitst in stukken van 300 tokens met 30 tokens overlap, waardoor context aan de stukgrenzen behouden blijft.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) en vraag:
> - "Hoe splitst LangChain4j documenten in stukken en waarom is overlap belangrijk?"
> - "Wat is de optimale chunk-grootte voor verschillende documenttypes en waarom?"
> - "Hoe ga ik om met documenten in meerdere talen of met speciale opmaak?"

### Embeddings Maken

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Elk stuk wordt omgezet in een numerieke representatie, een zogenaamde embedding — in feite een betekenis-naar-cijfers omvormer. Het embeddingmodel is niet "intelligent" zoals een chatmodel; het kan geen instructies opvolgen, redeneren of vragen beantwoorden. Wat het wel kan, is tekst mappen in een wiskundige ruimte waar gelijke betekenissen dicht bij elkaar liggen — "auto" dicht bij "voertuig," "terugbetalingsbeleid" dicht bij "mijn geld terug". Zie een chatmodel als een persoon waarmee je kan praten; een embeddingmodel is een supergoed archiefsysteem.

<img src="../../../translated_images/nl/embedding-model-concept.90760790c336a705.webp" alt="Concept van Embedding Model" width="800"/>

*Deze afbeelding toont hoe een embeddingmodel tekst omzet in numerieke vectoren, waarbij vergelijkbare betekenissen — zoals "auto" en "voertuig" — dicht bij elkaar in vectorruimte worden geplaatst.*

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
  
De klassendiagram hieronder laat de twee gescheiden stromen in een RAG-pijplijn zien en de LangChain4j klassen die ze implementeren. De **ingest-stroom** (loopt eenmaal bij upload) splitst het document, embedt de stukken, en slaat ze op via `.addAll()`. De **query-stroom** (loopt elke keer bij een gebruikersvraag) embedt de vraag, zoekt in de opslag via `.search()`, en geeft de gevonden context door aan het chatmodel. Beide stromingen komen samen bij de gedeelde interface `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/nl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klassen" width="800"/>

*Deze afbeelding toont de twee stromen in een RAG-pijplijn — ingestie en query — en hoe ze worden verbonden via een gedeelde EmbeddingStore.*

Zodra embeddings zijn opgeslagen, clustert vergelijkbare inhoud vanzelf dicht bij elkaar in vectorruimte. De visualisatie hieronder toont hoe documenten over gerelateerde onderwerpen als nabijgelegen punten eindigen, wat semantisch zoeken mogelijk maakt:

<img src="../../../translated_images/nl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Ruimte" width="800"/>

*Deze visualisatie toont hoe gerelateerde documenten samenklonteren in 3D-vectorruimte, met onderwerpen zoals Technische Documenten, Bedrijfsregels en FAQ’s die aparte groepen vormen.*

Wanneer een gebruiker zoekt, volgt het systeem vier stappen: embed de documenten eenmaal, embed de query bij elke zoekopdracht, vergelijk de queryvector met alle opgeslagen vectoren via cosinus-similariteit, en retourneer de top-K hoogst scorende stukken. De afbeelding hieronder doorloopt elke stap en de gebruikte LangChain4j klassen:

<img src="../../../translated_images/nl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Stappen van Embedding-zoekopdracht" width="800"/>

*Deze afbeelding toont het vier-stappen proces van embedding-zoeken: documenten embedden, de query embedden, vectoren vergelijken met cosinus-similariteit, en de top-K resultaten retourneren.*

### Semantisch Zoeken

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Wanneer je een vraag stelt, wordt je vraag ook een embedding. Het systeem vergelijkt de embedding van je vraag met die van alle documentstukken. Het vindt de stukken met de meest vergelijkbare betekenissen - niet alleen zoekwoorden die overeenkomen, maar daadwerkelijke semantische gelijkenis.

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
  
De afbeelding hieronder vergelijkt semantisch zoeken met traditioneel zoekwoord zoeken. Een zoekwoordzoektocht naar "voertuig" mist een stuk over "auto's en vrachtwagens," maar semantisch zoeken begrijpt dat ze hetzelfde betekenen en geeft het terug als een hoog scorende match:

<img src="../../../translated_images/nl/semantic-search.6b790f21c86b849d.webp" alt="Semantisch Zoeken" width="800"/>

*Deze afbeelding vergelijkt zoekwoord-gebaseerd zoeken met semantisch zoeken, en toont hoe semantisch zoeken conceptueel verwante inhoud ophaalt, zelfs als exacte zoekwoorden verschillen.*

Onder de motorkap wordt similariteit gemeten via cosinus-similariteit — in feite de vraag "steken deze twee pijlen dezelfde kant op?" Twee stukken kunnen volkomen andere woorden gebruiken, maar als ze hetzelfde betekenen wijzen hun vectoren dezelfde kant op en scoren ze dicht bij 1,0:

<img src="../../../translated_images/nl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosinus Similariteit" width="800"/>
*Deze diagram illustreert cosinusgelijkenis als de hoek tussen embeddingvectoren — beter uitgelijnde vectoren scoren dichter bij 1,0, wat duidt op hogere semantische gelijkenis.*

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) en vraag:
> - "Hoe werkt similarity search met embeddings en wat bepaalt de score?"
> - "Welke similarity drempel moet ik gebruiken en hoe beïnvloedt dit de resultaten?"
> - "Hoe ga ik om met gevallen waarin er geen relevante documenten worden gevonden?"

### Antwoord Genereren

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De meest relevante stukken worden samengevoegd tot een gestructureerde prompt die expliciete instructies, de opgehaalde context en de vraag van de gebruiker bevat. Het model leest die specifieke stukken en geeft een antwoord op basis van die informatie — het kan alleen gebruiken wat er voor ligt, wat hallucinaties voorkomt.

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

De onderstaande diagram toont deze samenstelling in actie — de hoogst scorende stukken uit de zoekstap worden in de prompt-sjabloon geïnjecteerd, en het `OpenAiOfficialChatModel` genereert een gegrond antwoord:

<img src="../../../translated_images/nl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Deze diagram toont hoe de hoogst scorende stukken worden samengevoegd tot een gestructureerde prompt, waardoor het model een gegrond antwoord uit je data kan genereren.*

## Applicatie Uitvoeren

**Controleren van deployment:**

Zorg dat het `.env` bestand in de hoofdmap aanwezig is met Azure-gegevens (aangemaakt tijdens Module 01):

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicatie:**

> **Let op:** Als je alle applicaties al hebt gestart met `./start-all.sh` uit Module 01, draait deze module al op poort 8081. Je kunt de onderstaande startcommando’s overslaan en direct gaan naar http://localhost:8081.

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De dev-container bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt het in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot icoon).

Vanuit het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de werkruimte zien
- Applicaties starten/stoppen met één klik
- Applicatielogs in realtime bekijken
- Applicatiestatus monitoren

Klik simpelweg op de playknop naast "rag" om deze module te starten, of start alle modules tegelijkertijd.

<img src="../../../translated_images/nl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Deze screenshot toont het Spring Boot Dashboard in VS Code, waar je applicaties visueel kunt starten, stoppen en monitoren.*

**Optie 2: Gebruik shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
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

Beide scripts laden automatisch omgevingsvariabelen uit het `.env` bestand in de root en bouwen de JARs indien deze nog niet bestaan.

> **Let op:** Als je alle modules liever handmatig bouwt voordat je start:
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

## De Applicatie Gebruiken

De applicatie biedt een webinterface voor het uploaden van documenten en het stellen van vragen.

<a href="images/rag-homepage.png"><img src="../../../translated_images/nl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Deze screenshot toont de RAG applicatie-interface waar je documenten uploadt en vragen stelt.*

### Upload een Document

Begin met het uploaden van een document – TXT-bestanden werken het beste voor testen. Er is een `sample-document.txt` beschikbaar in deze map die informatie bevat over LangChain4j functies, RAG-implementatie en best practices – perfect om het systeem te testen.

Het systeem verwerkt je document, splitst het in stukken (chunks) en maakt embeddings voor elk stuk. Dit gebeurt automatisch bij het uploaden.

### Stel Vragen

Stel nu gerichte vragen over de inhoud van het document. Probeer iets feitelijks te vragen dat duidelijk in het document staat. Het systeem zoekt relevante stukken, voegt deze toe aan de prompt en genereert een antwoord.

### Controleer Bronnen

Let op dat elk antwoord bronverwijzingen bevat met similarity scores. Deze scores (0 tot 1) geven aan hoe relevant elk stuk was voor je vraag. Hogere scores betekenen betere matches. Hiermee kun je het antwoord verifiëren met het bronmateriaal.

<a href="images/rag-query-results.png"><img src="../../../translated_images/nl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Deze screenshot toont queryresultaten met het gegenereerde antwoord, bronverwijzingen en relevantiescores voor elk opgehaald stuk.*

### Experimenteer met Vragen

Probeer verschillende soorten vragen:
- Specifieke feiten: "Wat is het hoofdthema?"
- Vergelijkingen: "Wat is het verschil tussen X en Y?"
- Samenvattingen: "Vat de belangrijkste punten over Z samen"

Let op hoe de relevantiescores veranderen afhankelijk van hoe goed je vraag bij de documentinhoud past.

## Belangrijke Concepten

### Chunking Strategie

Documenten worden opgesplitst in stukken van 300 tokens met 30 tokens overlap. Deze balans zorgt ervoor dat elk stuk voldoende context heeft om betekenisvol te zijn en tegelijkertijd klein genoeg blijft om meerdere stukken in een prompt te kunnen verwerken.

### Similarity Scores

Elke opgehaalde chunk heeft een similarity score tussen 0 en 1 die aangeeft hoe goed deze bij de vraag van de gebruiker past. De onderstaande diagram visualiseert de scorebereiken en hoe het systeem deze gebruikt om resultaten te filteren:

<img src="../../../translated_images/nl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Deze diagram toont scorebereiken van 0 tot 1, met een minimale drempel van 0,5 die irrelevante stukken eruit filtert.*

Scores variëren van 0 tot 1:
- 0,7-1,0: Zeer relevant, exacte match
- 0,5-0,7: Relevant, goede context
- Lager dan 0,5: Gefilterd, te verschillend

Het systeem haalt alleen stukken op die boven de minimale drempel scoren om kwaliteit te waarborgen.

Embeddings werken goed wanneer betekenissen duidelijk clusteren, maar ze hebben ook blindspots. De onderstaande diagram toont veelvoorkomende faalmodi — stukken die te groot zijn produceren wazige vectoren, stukken die te klein zijn missen context, dubbelzinnige termen wijzen naar meerdere clusters, en exacte zoekopdrachten (IDs, partnummers) werken helemaal niet met embeddings:

<img src="../../../translated_images/nl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Deze diagram toont vaak voorkomende embedding faalmodi: stukken te groot, stukken te klein, dubbelzinnige termen die naar meerdere clusters wijzen, en exacte zoekopdrachten zoals IDs.*

### In-Memory Opslag

Deze module gebruikt in-memory opslag voor eenvoud. Bij het herstarten van de applicatie gaan geüploade documenten verloren. Productiesystemen gebruiken persistente vectordatabases zoals Qdrant of Azure AI Search.

### Contextvensterbeheer

Elk model heeft een maximale contextgrootte. Je kunt niet elk stuk van een groot document opnemen. Het systeem haalt de top N meest relevante stukken op (standaard 5) om binnen de limieten te blijven en toch voldoende context te bieden voor accurate antwoorden.

## Wanneer RAG Relevant Is

RAG is niet altijd de juiste aanpak. De onderstaande beslisboom helpt bepalen wanneer RAG waarde toevoegt versus wanneer eenvoudigere benaderingen — zoals content direct in de prompt opnemen of vertrouwen op ingebouwde kennis van het model — volstaan:

<img src="../../../translated_images/nl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Deze diagram toont een beslisboom wanneer RAG waarde toevoegt versus wanneer eenvoudigere benaderingen volstaan.*

**Gebruik RAG wanneer:**
- Je vragen beantwoordt over propriëtaire documenten
- Informatie vaak verandert (beleid, prijzen, specificaties)
- Nauwkeurigheid vereist bronvermelding
- Content te groot is om in één prompt te passen
- Je verifieerbare, gegronde antwoorden nodig hebt

**Gebruik RAG niet wanneer:**
- Vragen algemene kennis vereisen die het model al bezit
- Je real-time data nodig hebt (RAG werkt met geüploade documenten)
- Content klein genoeg is om direct in prompts op te nemen

## Volgende Stappen

**Volgende Module:** [04-tools - AI Agents met Tools](../04-tools/README.md)

---

**Navigatie:** [← Vorige: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Terug naar Start](../README.md) | [Volgende: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel wij streven naar nauwkeurigheid, kan automatische vertaling fouten of onnauwkeurigheden bevatten. Het oorspronkelijke document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->