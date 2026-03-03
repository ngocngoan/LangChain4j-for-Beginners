# Modul 03: RAG (Retrieval-Augmented Generation)

## Innehållsförteckning

- [Video Walkthrough](../../../03-rag)
- [Vad du kommer att lära dig](../../../03-rag)
- [Förutsättningar](../../../03-rag)
- [Förstå RAG](../../../03-rag)
  - [Vilket RAG-tillvägagångssätt använder denna handledning?](../../../03-rag)
- [Hur det fungerar](../../../03-rag)
  - [Dokumentbearbetning](../../../03-rag)
  - [Skapa embeddings](../../../03-rag)
  - [Semantisk sökning](../../../03-rag)
  - [Svarsgenerering](../../../03-rag)
- [Kör applikationen](../../../03-rag)
- [Använda applikationen](../../../03-rag)
  - [Ladda upp ett dokument](../../../03-rag)
  - [Ställ frågor](../../../03-rag)
  - [Kontrollera källreferenser](../../../03-rag)
  - [Experimentera med frågor](../../../03-rag)
- [Nyckelkoncept](../../../03-rag)
  - [Chunking-strategi](../../../03-rag)
  - [Likhetspoäng](../../../03-rag)
  - [I-minneslagring](../../../03-rag)
  - [Hantera kontextfönster](../../../03-rag)
- [När RAG är viktigt](../../../03-rag)
- [Nästa steg](../../../03-rag)

## Video Walkthrough

Se denna livesession som förklarar hur du kommer igång med denna modul:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Vad du kommer att lära dig

I de tidigare modulerna lärde du dig hur du kan föra konversationer med AI och strukturera dina prompts effektivt. Men det finns en grundläggande begränsning: språkmodeller vet bara det de lärde sig under träningen. De kan inte svara på frågor om ditt företags policyer, din projekt-dokumentation eller någon information de inte tränades på.

RAG (Retrieval-Augmented Generation) löser detta problem. Istället för att försöka lära modellen din information (vilket är dyrt och opraktiskt), ger du den möjligheten att söka igenom dina dokument. När någon ställer en fråga hittar systemet relevant information och inkluderar den i prompten. Modellen svarar sedan baserat på den hämtade kontexten.

Tänk på RAG som att ge modellen ett referensbibliotek. När du ställer en fråga gör systemet:

1. **Användarfråga** - Du ställer en fråga  
2. **Embedding** - Omvandlar din fråga till en vektor  
3. **Vektorsökning** - Hittar liknande dokumentbitar  
4. **Kontextsammansättning** - Lägger till relevanta bitar i prompten  
5. **Svar** - LLM genererar ett svar baserat på kontexten  

Detta grundar modellens svar i dina faktiska data istället för att förlita sig på dess träningskunskap eller hitta på svar.

## Förutsättningar

- Avklarat [Modul 00 - Snabbstart](../00-quick-start/README.md) (för det lätta RAG-exemplet som refereras senare i denna modul)  
- Avklarat [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-resurser distribuerade, inklusive embeddingmodellen `text-embedding-3-small`)  
- `.env`-fil i rotkatalogen med Azure-uppgifter (skapad av `azd up` i Modul 01)  

> **Notera:** Om du inte har avklarat Modul 01, följ distributionsinstruktionerna där först. Kommandot `azd up` distribuerar både GPT chat-modellen och embeddingmodellen som används i denna modul.

## Förstå RAG

Diagrammet nedan illustrerar kärnbegreppet: istället för att bara förlita sig på modellens träningsdata ger RAG den ett referensbibliotek med dina dokument att konsultera innan varje svar genereras.

<img src="../../../translated_images/sv/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Detta diagram visar skillnaden mellan en standard-LLM (som gissar utifrån träningsdata) och en RAG-förbättrad LLM (som konsulterar dina dokument först).*

Så här kopplas delarna ihop från början till slut. En användares fråga passerar genom fyra steg — embedding, vektorsökning, kontextsammansättning och svarsgenerering — där varje steg bygger på det föregående:

<img src="../../../translated_images/sv/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Detta diagram visar den kompletta RAG-pipelinen — en användarfråga går igenom embedding, vektorsökning, kontextsammansättning och svarsgenerering.*

Resten av denna modul går igenom varje steg i detalj, med kod du kan köra och modifiera.

### Vilket RAG-tillvägagångssätt använder denna handledning?

LangChain4j erbjuder tre sätt att implementera RAG, var och en med olika nivåer av abstraktion. Diagrammet nedan jämför dem sida vid sida:

<img src="../../../translated_images/sv/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Detta diagram jämför de tre LangChain4j RAG-tillvägagångssätten — Easy, Native och Advanced — visar deras nyckelkomponenter och när man ska använda varje.*

| Tillvägagångssätt | Vad det gör | Avvägning |
|---|---|---|
| **Easy RAG** | Kopplar allt automatiskt via `AiServices` och `ContentRetriever`. Du annoterar ett interface, fäster en retriever, och LangChain4j hanterar embedding, sökning och promptsammanställning bakom kulisserna. | Minimal kod, men du ser inte vad som händer i varje steg. |
| **Native RAG** | Du kallar embeddingmodellen, söker i lagret, bygger prompten och genererar svaret själv — ett tydligt steg i taget. | Mer kod, men varje steg är synligt och modifierbart. |
| **Advanced RAG** | Använder `RetrievalAugmentor`-ramverket med pluggbara söktransformatorer, routrar, omrangörer och innehållsinjektorer för produktionspipeline. | Maximal flexibilitet, men avsevärt mer komplexitet. |

**Denna handledning använder Native-tillvägagångssättet.** Varje steg i RAG-pipelinen — embedda frågan, söka i vektorlager, sammanställa kontext och generera svar — skrivs explicit i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Detta är avsiktligt: som inlärningsresurs är det viktigare att du ser och förstår varje steg än att koden är nedbantad. När du känner dig bekväm med hur delarna passar ihop kan du gå vidare till Easy RAG för snabba prototyper eller Advanced RAG för produktionssystem.

> **💡Redan sett Easy RAG i aktion?** [Snabbstart-modulen](../00-quick-start/README.md) inkluderar ett exempel på dokument Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) som använder Easy RAG-tillvägagångssättet — LangChain4j hanterar embedding, sökning och promptsammanställning automatiskt. Denna modul tar nästa steg genom att bryta ned den pipelinen så att du kan se och styra varje steg själv.

Diagrammet nedan visar Easy RAG-pipelinen från det exemplet i Snabbstarten. Lägg märke till hur `AiServices` och `EmbeddingStoreContentRetriever` döljer all komplexitet — du laddar ett dokument, fäster en retriever och får svar. Native-tillvägagångssättet i denna modul öppnar upp varje dolt steg:

<img src="../../../translated_images/sv/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Detta diagram visar Easy RAG-pipelinen från `SimpleReaderDemo.java`. Jämför med Native-tillvägagångssättet som används i denna modul: Easy RAG döljer embedding, hämtning och promptsammanställning bakom `AiServices` och `ContentRetriever` — du laddar ett dokument, fäster en retriever och får svar. Native-tillvägagångssättet i denna modul öppnar upp den pipelinen så att du kallar varje steg (embedda, söka, sammanställa kontext, generera) själv, vilket ger full insyn och kontroll.*

## Hur det fungerar

RAG-pipelinen i denna modul delas upp i fyra steg som körs i sekvens varje gång en användare ställer en fråga. Först **parsas och chunkas** ett uppladdat dokument i hanterbara delar. Dessa bitar konverteras sedan till **vektorembeddings** och lagras för att kunna jämföras matematisk. När en fråga kommer in gör systemet en **semantisk sökning** för att hitta de mest relevanta bitarna och slutligen skickas dessa som kontext till LLM för **svarsgenerering**. Sektionerna nedan går igenom varje steg med faktisk kod och diagram. Börja med första steget.

### Dokumentbearbetning

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

När du laddar upp ett dokument parsar systemet det (PDF eller ren text), fäster metadata som filnamn och delar sedan upp det i chunkar — mindre delar som får plats i modellens kontextfönster. Dessa chunkar överlappar något så att du inte tappar kontext vid gränserna.

```java
// Analysera den uppladdade filen och slå in den i ett LangChain4j-dokument
Document document = Document.from(content, metadata);

// Dela upp i 300-tokenbitar med 30-token överlappning
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Diagrammet nedan visar hur detta fungerar visuellt. Lägg märke till hur varje chunk delar vissa tokens med sina grannar — 30 tokens överlapp säkerställer att ingen viktig kontext faller mellan stolarna:

<img src="../../../translated_images/sv/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Detta diagram visar ett dokument som delas upp i 300-token chunkar med 30-token överlapp för att bevara kontext vid chunkgränser.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) och fråga:  
> - "Hur delar LangChain4j dokument i chunkar och varför är överlapp viktigt?"  
> - "Vad är optimal chunkstorlek för olika dokumenttyper och varför?"  
> - "Hur hanterar jag dokument på flera språk eller med specialformatering?"

### Skapa embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Varje chunk konverteras till en numerisk representation kallad embedding — i huvudsak en omvandlare från mening till tal. Embeddingmodellen är inte "intelligent" på samma sätt som en chattmodell; den kan inte följa instruktioner, resonera eller svara på frågor. Vad den kan göra är att mappa text till ett matematiskt utrymme där liknande betydelser hamnar nära varandra — "bil" nära "automobil", "återbetalningspolicy" nära "återfå mina pengar". Tänk på en chattmodell som en person du kan prata med; en embeddingmodell är ett ultrabra arkivsystem.

Diagrammet nedan visualiserar konceptet — text går in, numeriska vektorer kommer ut, och liknande betydelser ger närliggande vektorer:

<img src="../../../translated_images/sv/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Detta diagram visar hur en embeddingmodell konverterar text till numeriska vektorer, där liknande betydelser — som "bil" och "automobil" — placeras nära varandra i vektorutrymmet.*

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
  
Klassdiagrammet nedan visar de två separata flödena i en RAG-pipeline och LangChain4j-klasserna som implementerar dem. **Inlagringsflödet** (körs en gång vid uppladdning) delar dokumentet, embedder chunkarna och lagrar dem via `.addAll()`. **Frågeflödet** (körs varje gång användaren frågar) embedder frågan, söker i lagret via `.search()` och skickar matchande kontext till chattmodellen. Båda flödena möts i det delade `EmbeddingStore<TextSegment>`-gränssnittet:

<img src="../../../translated_images/sv/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Detta diagram visar de två flödena i en RAG-pipeline — inläsning och fråga — och hur de kopplas ihop via ett delat EmbeddingStore.*

När embeddings är lagrade klustras liknande innehåll naturligt tillsammans i vektorutrymmet. Visualiseringen nedan visar hur dokument om relaterade ämnen hamnar som närliggande punkter, vilket möjliggör semantisk sökning:

<img src="../../../translated_images/sv/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Denna visualisering visar hur relaterade dokument klustras i 3D-vektorutrymmet, med ämnen som Teknikdokument, Affärsregler och FAQ som bildar separata grupper.*

När en användare söker följer systemet fyra steg: embedda dokumenten en gång, embedda frågan vid varje sökning, jämföra frågevektorn mot alla lagrade vektorer med kosinuslikhet, och returnera de bäst rankade top-K chunkarna. Diagrammet nedan visar varje steg och involverade LangChain4j-klasser:

<img src="../../../translated_images/sv/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Detta diagram visar fyra-stegs processen för embedding-sökning: embedda dokument, embedda frågan, jämföra vektorer med kosinuslikhet och returnera top-K resultat.*

### Semantisk sökning

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

När du ställer en fråga blir även din fråga en embedding. Systemet jämför din frågas embedding mot alla dokumentchunks embeddings. Det hittar chunkarna med mest liknande betydelse — inte bara matchande nyckelord, utan riktig semantisk likhet.

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
  
Diagrammet nedan ställer semantisk sökning mot traditionell nyckelordssökning. En nyckelordssökning på "fordon" missar en chunk om "bilar och lastbilar", men semantisk sökning förstår att de betyder samma sak och returnerar det som ett högt rankat träff:

<img src="../../../translated_images/sv/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Detta diagram jämför nyckelordsbaserad sökning med semantisk sökning, och visar hur semantisk sökning hämtar konceptuellt relaterat innehåll även när exakta nyckelord skiljer sig åt.*
Under huven mäts likhet med hjälp av kosinuslikhet — i princip genom att fråga "pekar dessa två pilar i samma riktning?" Två textstycken kan använda helt olika ord, men om de betyder samma sak pekar deras vektorer åt samma håll och ger ett poängvärde nära 1,0:

<img src="../../../translated_images/sv/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Denna diagram illustrerar kosinuslikhet som vinkeln mellan inbäddningsvektorer — mer anpassade vektorer får poäng närmare 1,0, vilket indikerar högre semantisk likhet.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) och fråga:
> - "Hur fungerar sökning baserat på likhet med inbäddningar och vad avgör poängen?"
> - "Vilken likhetströskel bör jag använda och hur påverkar det resultaten?"
> - "Hur hanterar jag fall där inga relevanta dokument hittas?"

### Svarsgenerering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevanta textstyckena samlas ihop till en strukturerad prompt som inkluderar tydliga instruktioner, den hämtade kontexten och användarens fråga. Modellen läser dessa specifika delar och svarar baserat på den informationen — den kan bara använda det som finns framför sig, vilket förhindrar hallucination.

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

Diagrammet nedan visar denna sammansättning i praktiken — de textstycken med högst poäng från söksteget injiceras i promptmallen, och `OpenAiOfficialChatModel` genererar ett grundat svar:

<img src="../../../translated_images/sv/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Detta diagram visar hur textstycken med högst poäng sätts ihop till en strukturerad prompt, vilket tillåter modellen att generera ett grundat svar från dina data.*

## Kör applikationen

**Verifiera driftsättning:**

Se till att filen `.env` finns i rotkatalogen med Azure-uppgifter (skapad under Modul 01). Kör detta från modulkatalogen (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan har startat alla applikationer med `./start-all.sh` från rotkatalogen (enligt Modul 01) kör denna modul redan på port 8081. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8081.

**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern innehåller Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar det i aktivitetsfältet på vänster sida i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stoppa applikationer med ett klick
- Visa applikationsloggar i realtid
- Övervaka applikationsstatus

Klicka bara på play-knappen bredvid "rag" för att starta denna modul, eller starta alla moduler på en gång.

<img src="../../../translated_images/sv/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Denna skärmdump visar Spring Boot Dashboard i VS Code där du visuellt kan starta, stoppa och övervaka applikationer.*

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotkatalogen
.\start-all.ps1
```

Eller starta bara denna modul:

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

Båda skripten laddar automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan start:
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

Öppna http://localhost:8081 i din webbläsare.

**För att stoppa:**

**Bash:**
```bash
./stop.sh  # Endast denna modul
# Eller
cd .. && ./stop-all.sh  # Alla moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Endast denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```

## Använda Applikationen

Applikationen erbjuder ett webbgränssnitt för dokumentuppladdning och frågeställning.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sv/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Denna skärmdump visar RAG-applikationens gränssnitt där du laddar upp dokument och ställer frågor.*

### Ladda upp ett dokument

Börja med att ladda upp ett dokument - TXT-filer fungerar bäst för testning. En `sample-document.txt` medföljer i denna katalog och innehåller information om LangChain4j-funktioner, RAG-implementering och bästa praxis - perfekt för att testa systemet.

Systemet bearbetar ditt dokument, delar upp det i mindre delar och skapar inbäddningar för varje del. Detta sker automatiskt när du laddar upp.

### Ställ frågor

Ställ nu specifika frågor om dokumentets innehåll. Prova något faktabaserat som tydligt anges i dokumentet. Systemet söker efter relevanta delar, inkluderar dem i prompten och genererar ett svar.

### Kontrollera källreferenser

Observera att varje svar inkluderar källreferenser med likhetspoäng. Dessa poäng (0 till 1) visar hur relevanta varje del var i förhållande till din fråga. Högre poäng innebär bättre träffar. Detta låter dig verifiera svaret mot källmaterialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sv/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Denna skärmdump visar frågeresultat med det genererade svaret, källreferenser och relevanspoäng för varje hämtat textstycke.*

### Experimentera med frågor

Prova olika typer av frågor:
- Specifika fakta: "Vad är huvudsakliga ämnet?"
- Jämförelser: "Vad är skillnaden mellan X och Y?"
- Sammanfattningar: "Sammanfatta huvudpunkterna om Z"

Se hur relevanspoängen ändras beroende på hur väl din fråga matchar dokumentinnehållet.

## Nyckelkoncept

### Delningsstrategi

Dokument delas upp i 300-token-stycken med 30 tokens överlappning. Denna balans säkerställer att varje stycke har tillräcklig kontext för att vara meningsfullt samtidigt som det är tillräckligt litet för att kunna inkluderas flera stycken i en prompt.

### Likhetspoäng

Varje hittat stycke kommer med en likhetspoäng mellan 0 och 1 som indikerar hur nära det matchar användarens fråga. Diagrammet nedan visualiserar poängintervallen och hur systemet använder dem för att filtrera resultat:

<img src="../../../translated_images/sv/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Detta diagram visar poängintervall från 0 till 1, med en minimigräns på 0,5 som filtrerar bort irrelevanta stycken.*

Poängen spänner från 0 till 1:
- 0,7-1,0: Mycket relevant, exakt träff
- 0,5-0,7: Relevant, bra kontext
- Under 0,5: Filtrerat bort, för olik

Systemet hämtar endast stycken över minimigränsen för att säkerställa kvalitet.

Inbäddningar fungerar bra när betydelser grupperas tydligt, men de har blinda fläckar. Diagrammet nedan visar vanliga fel — för stora stycken skapar otydliga vektorer, för små stycken saknar kontext, tvetydiga termer pekar på flera kluster, och sökningar på exakt träff (ID:n, artikelnummer) fungerar inte alls med inbäddningar:

<img src="../../../translated_images/sv/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Detta diagram visar vanliga felriktningar vid inbäddning: för stora stycken, för små stycken, tvetydiga termer som pekar på flera kluster, och sökningar på exakt träff som ID:n.*

### In-Memory Lagring

Denna modul använder in-memory lagring för enkelhetens skull. När du startar om applikationen går uppladdade dokument förlorade. Produktionssystem använder persistenta vektordatabaser som Qdrant eller Azure AI Search.

### Hantering av kontextfönster

Varje modell har ett maximal kontextfönster. Du kan inte inkludera alla stycken från ett stort dokument. Systemet hämtar de topp N mest relevanta stycken (standard 5) för att hålla sig inom gränser samtidigt som tillräcklig kontext ges för exakta svar.

## När RAG är viktigt

RAG är inte alltid rätt angreppssätt. Beslutsguiden nedan hjälper dig avgöra när RAG tillför värde jämfört med när enklare metoder — som att inkludera innehåll direkt i prompten eller lita på modellens inbyggda kunskap — är tillräckliga:

<img src="../../../translated_images/sv/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Detta diagram visar en beslutsguide för när RAG tillför värde jämfört med när enklare metoder är tillräckliga.*

## Nästa steg

**Nästa modul:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigering:** [← Föregående: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tillbaka till huvud](../README.md) | [Nästa: Modul 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen nota att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->