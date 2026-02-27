# Modul 03: RAG (Retrieval-Augmented Generation)

## Innehållsförteckning

- [Video Genomgång](../../../03-rag)
- [Det Du Kommer Lära Dig](../../../03-rag)
- [Förkunskaper](../../../03-rag)
- [Förstå RAG](../../../03-rag)
  - [Vilken RAG Metod Använder Denna Guide?](../../../03-rag)
- [Hur Det Fungerar](../../../03-rag)
  - [Dokumentbearbetning](../../../03-rag)
  - [Skapa Embeddings](../../../03-rag)
  - [Semantisk Sökning](../../../03-rag)
  - [Svarsgenerering](../../../03-rag)
- [Kör Applikationen](../../../03-rag)
- [Använda Applikationen](../../../03-rag)
  - [Ladda Upp Ett Dokument](../../../03-rag)
  - [Ställ Frågor](../../../03-rag)
  - [Granska Källreferenser](../../../03-rag)
  - [Experimentera med Frågor](../../../03-rag)
- [Nyckelkoncept](../../../03-rag)
  - [Chunking-Strategi](../../../03-rag)
  - [Likhetspoäng](../../../03-rag)
  - [Minneslagring](../../../03-rag)
  - [Hantera Kontextfönster](../../../03-rag)
- [När RAG är Viktigt](../../../03-rag)
- [Nästa Steg](../../../03-rag)

## Video Genomgång

Titta på denna livesession som förklarar hur du kommer igång med denna modul: [RAG med LangChain4j - Livesession](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Det Du Kommer Lära Dig

I de tidigare modulerna lärde du dig hur man har konversationer med AI och strukturerar dina prompts effektivt. Men det finns en grundläggande begränsning: språkmodeller vet bara det de lärde sig under träningen. De kan inte svara på frågor om ditt företags policys, din projektdokumentation eller någon information de inte tränades på.

RAG (Retrieval-Augmented Generation) löser detta problem. Istället för att försöka lära modellen din information (vilket är dyrt och opraktiskt), ger du den förmågan att söka i dina dokument. När någon ställer en fråga hittar systemet relevant information och inkluderar den i prompten. Modellen svarar sedan baserat på den hämtade kontexten.

Tänk på RAG som att ge modellen ett referensbibliotek. När du ställer en fråga gör systemet:

1. **Användarfråga** - Du ställer en fråga  
2. **Embedding** - Omvandlar din fråga till en vektor  
3. **Vektorsökning** - Hittar liknande dokumentbitar  
4. **Konstuksjon av kontext** - Lägger till relevanta bitar i prompten  
5. **Svar** - LLM genererar ett svar baserat på kontexten  

Detta förankrar modellens svar i din faktiska data istället för att förlita sig på dess träningskunskaper eller hitta på svar.

## Förkunskaper

- Avslutat [Modul 00 - Snabbstart](../00-quick-start/README.md) (för Easy RAG-exemplet som refereras ovan)  
- Avslutat [Modul 01 - Introduktion](../01-introduction/README.md) (Azure OpenAI-resurser distribuerade, inklusive inbäddningsmodellen `text-embedding-3-small`)  
- `.env`-fil i rotmappen med Azure-uppgifter (skapad av `azd up` i Modul 01)  

> **Notera:** Om du inte har avslutat Modul 01, följ först installationsinstruktionerna där. Kommandot `azd up` distribuerar både GPT chattmodellen och inbäddningsmodellen som används i denna modul.

## Förstå RAG

Diagrammet nedan illustrerar kärnkonceptet: istället för att bara förlita sig på modellens träningsdata, ger RAG den ett referensbibliotek med dina dokument att konsultera innan varje svar genereras.

<img src="../../../translated_images/sv/what-is-rag.1f9005d44b07f2d8.webp" alt="Vad är RAG" width="800"/>

*Detta diagram visar skillnaden mellan en vanlig LLM (som gissar från träningsdata) och en RAG-förstärkt LLM (som först konsulterar dina dokument).*

Så här kopplas delarna samman från början till slut. En användares fråga går igenom fyra steg — embedding, vektorsökning, kontextsammanställning och svarsgenerering — där varje steg bygger på det föregående:

<img src="../../../translated_images/sv/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Arkitektur" width="800"/>

*Detta diagram visar hela RAG-pipelinen från början till slut — en användarfråga går igenom embedding, vektorsökning, kontextsammanställning och svarsgenerering.*

Resten av denna modul går igenom varje steg i detalj, med kod du kan köra och ändra.

### Vilken RAG Metod Använder Denna Guide?

LangChain4j erbjuder tre sätt att implementera RAG, varje med olika abstraktionsnivå. Diagrammet nedan jämför dem sida vid sida:

<img src="../../../translated_images/sv/rag-approaches.5b97fdcc626f1447.webp" alt="Tre RAG-metoder i LangChain4j" width="800"/>

*Detta diagram jämför de tre LangChain4j RAG-metoderna — Easy, Native och Advanced — och visar deras nyckelkomponenter samt när de används.*

| Metod | Vad Den Gör | Avvägning |
|---|---|---|
| **Easy RAG** | Kopplar allt automatiskt genom `AiServices` och `ContentRetriever`. Du annoterar ett interface, kopplar på en retriever, och LangChain4j hanterar embedding, sökning och prompt-sammansättning i bakgrunden. | Minimal kod, men du ser inte vad som händer i varje steg. |
| **Native RAG** | Du anropar inbäddningsmodellen, söker i lagret, bygger prompten och genererar svaret själv — ett tydligt steg i taget. | Mer kod, men alla steg är synliga och kan ändras. |
| **Advanced RAG** | Använder `RetrievalAugmentor`-ramverket med utbytbara query-transformers, routers, re-rankers och content injectors för produktionsklara pipelines. | Maximal flexibilitet, men betydligt mer komplexitet. |

**Denna guide använder Native-metoden.** Varje steg i RAG-pipelinen — embedda frågan, söka i vektorlagret, montera kontexten och generera svaret — är skrivet explicit i [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Detta är avsiktligt: som en lärresurs är det viktigare att du ser och förstår varje steg än att koden är minimal. När du är bekväm med hur delarna hänger ihop kan du gå vidare till Easy RAG för snabba prototyper eller Advanced RAG för produktionssystem.

> **💡 Har du redan sett Easy RAG i praktiken?** [Snabbstartmodulen](../00-quick-start/README.md) innehåller ett Dokument Q&A-exempel ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) som använder Easy RAG — LangChain4j hanterar embedding, sökning och prompt-komposition automatiskt. Denna modul tar nästa steg genom att öppna den pipelinen så att du själv kan se och styra varje steg.

<img src="../../../translated_images/sv/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Detta diagram visar Easy RAG-pipelinen från `SimpleReaderDemo.java`. Jämför med Native-metoden som används i denna modul: Easy RAG gömmer embedding, hämtning och prompt-sammansättning bakom `AiServices` och `ContentRetriever` — du laddar ett dokument, kopplar på en retriever och får svar. Native-metoden i denna modul öppnar den pipelinen så att du kan anropa varje steg (embed, sök, samla kontext, generera) själv, vilket ger full synlighet och kontroll.*

## Hur Det Fungerar

RAG-pipelinen i denna modul delas upp i fyra steg som körs i följd varje gång en användare ställer en fråga. Först parkeras ett uppladdat dokument och delas upp **i hanterbara delar**. Dessa bitar omvandlas sedan till **vektorinbäddningar** och lagras så att de kan jämföras matematiskt. När en fråga kommer in gör systemet en **semantisk sökning** för att hitta de mest relevanta bitarna, och skickar slutligen dem som kontext till LLM för **svarsgenerering**. Sektionerna nedan går igenom varje steg med den faktiska koden och diagram. Vi börjar med första steget.

### Dokumentbearbetning

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

När du laddar upp ett dokument parsar systemet det (PDF eller vanlig text), kopplar på metadata som filnamn, och delar sedan upp det i bitar — mindre delar som passar bekvämt i modellens kontextfönster. Dessa bitar överlappar lite så att du inte förlorar kontext vid gränserna.

```java
// Analysera den uppladdade filen och kapsla in den i ett LangChain4j-dokument
Document document = Document.from(content, metadata);

// Dela upp i 300-tokendelar med 30-token överlappning
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagrammet nedan visar hur detta fungerar visuellt. Lägg märke till hur varje bit delar några tokens med sina grannar — 30-token överlappning säkerställer att ingen viktig kontext går förlorad mellan bitarna:

<img src="../../../translated_images/sv/document-chunking.a5df1dd1383431ed.webp" alt="Dokumentindelning" width="800"/>

*Detta diagram visar ett dokument som delas upp i 300-token bitar med 30-token överlappning, vilket bevarar kontext vid bitgränserna.*

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) och fråga:  
> - "Hur delar LangChain4j upp dokument i bitar och varför är överlappning viktigt?"  
> - "Vad är optimal bitstorlek för olika dokumenttyper och varför?"  
> - "Hur hanterar jag dokument på flera språk eller med speciell formatering?"

### Skapa Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Varje bit omvandlas till en numerisk representation kallad embedding — i princip en mening-till-nummer-omvandlare. Inbäddningsmodellen är inte "intelligent" som en chattmodell; den kan inte följa instruktioner, resonera eller svara på frågor. Det den kan göra är att kartlägga text till ett matematiskt rum där liknande betydelser hamnar nära varandra — "bil" nära "automobil," "återbetalningspolicy" nära "få tillbaka mina pengar". Tänk på en chattmodell som en person du kan prata med; en inbäddningsmodell är ett superbra arkivsystem.

<img src="../../../translated_images/sv/embedding-model-concept.90760790c336a705.webp" alt="Inbäddningsmodell Koncept" width="800"/>

*Detta diagram visar hur en inbäddningsmodell omvandlar text till numeriska vektorer, där liknande betydelser — som "bil" och "automobil" — placeras nära varandra i vektorutrymmet.*

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

Klassdiagrammet nedan visar de två separata flödena i en RAG-pipeline och de LangChain4j-klasser som implementerar dem. **Inmatningsflödet** (körs en gång vid uppladdning) delar upp dokumentet, embedder bitarna och sparar dem via `.addAll()`. **Frågeflödet** (körs varje gång en användare frågar) embedder frågan, söker i lagret via `.search()`, och skickar matchande kontext till chattmodellen. Båda flödena möts i det gemensamma gränssnittet `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sv/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Klasser" width="800"/>

*Detta diagram visar de två flödena i en RAG-pipeline — inmatning och fråga — och hur de kopplas samman genom ett gemensamt EmbeddingStore.*

När embeddings är lagrade klustras liknande innehåll naturligt tillsammans i vektorutrymmet. Visualiseringen nedan visar hur dokument om relaterade ämnen hamnar nära varandra, vilket gör semantisk sökning möjlig:

<img src="../../../translated_images/sv/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektor Inbäddningsutrymme" width="800"/>

*Denna visualisering visar hur relaterade dokument klustras tillsammans i 3D vektorutrymme, med ämnen som tekniska dokument, affärsregler och FAQ som bildar distinkta grupper.*

När en användare söker följer systemet fyra steg: embedda dokumenten en gång, embedda frågan för varje sökning, jämföra frågevektorn med alla lagrade vektorer med kosinuslikhet, och returnera de top-K högst scorande bitarna. Diagrammet nedan förklarar varje steg och de LangChain4j-klasser som är inblandade:

<img src="../../../translated_images/sv/embedding-search-steps.f54c907b3c5b4332.webp" alt="Steg i Inbäddningssökning" width="800"/>

*Detta diagram visar fyra-stegs processen för inbäddningssökning: embedda dokument, embedda fråga, jämföra vektorer med kosinuslikhet, och returnera top-K resultat.*

### Semantisk Sökning

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

När du ställer en fråga blir även din fråga en embedding. Systemet jämför din frågas embedding mot alla dokumentbitars embeddings. Det hittar de bitar som har mest liknande betydelse – inte bara matchande nyckelord utan faktisk semantisk likhet.

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

Diagrammet nedan jämför semantisk sökning med traditionell nyckelordssökning. En nyckelordssökning på "fordon" missar en bit om "bilar och lastbilar," men semantisk sökning förstår att de betyder samma sak och returnerar den som ett högt rankat resultat:

<img src="../../../translated_images/sv/semantic-search.6b790f21c86b849d.webp" alt="Semantisk Sökning" width="800"/>

*Detta diagram jämför nyckelordsbaserad sökning med semantisk sökning, som visar hur semantisk sökning hämtar konceptuellt relaterat innehåll även när exakta nyckelord skiljer sig.*

Under huven mäts likheten med kosinuslikhet — i huvudsak frågar den "pekar dessa två pilar i samma riktning?" Två bitar kan använda helt olika ord, men om de betyder samma sak pekar deras vektorer åt samma håll och får poäng nära 1.0:

<img src="../../../translated_images/sv/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinuslikhet" width="800"/>

*Detta diagram illustrerar kosinuslikhet som vinkeln mellan inbäddningsvektorer — mer justerade vektorer får poäng närmare 1.0, vilket indikerar högre semantisk likhet.*
> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Öppna [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) och fråga:
> - "Hur fungerar likhetssökning med embeddingar och vad bestämmer poängen?"
> - "Vilken likhetströskel bör jag använda och hur påverkar den resultaten?"
> - "Hur hanterar jag fall där inga relevanta dokument hittas?"

### Svarsgenerering

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

De mest relevanta delarna sätts ihop till en strukturerad prompt som inkluderar explicita instruktioner, det hämtade sammanhanget och användarens fråga. Modellen läser de specifika delarna och svarar baserat på den informationen — den kan bara använda det som finns framför sig, vilket förhindrar hallucination.

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

Diagrammet nedan visar denna sammansättning i praktiken — de högst poängsatta delarna från söksteget injiceras i promptmallen, och `OpenAiOfficialChatModel` genererar ett grundat svar:

<img src="../../../translated_images/sv/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Detta diagram visar hur de högst poängsatta delarna sätts ihop till en strukturerad prompt, vilket gör det möjligt för modellen att generera ett grundat svar från din data.*

## Kör applikationen

**Verifiera distribution:**

Se till att `.env`-filen finns i rotkatalogen med Azure-uppgifter (skapad under Modul 01):

**Bash:**
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Starta applikationen:**

> **Notera:** Om du redan startade alla applikationer med `./start-all.sh` från Modul 01, kör detta modul redan på port 8081. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8081.

**Alternativ 1: Använd Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard extension, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-applikationer. Du hittar den i aktivitetsfältet till vänster i VS Code (sök efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:
- Se alla tillgängliga Spring Boot-applikationer i arbetsytan
- Starta/stoppapplikationer med ett klick
- Visa applogs i realtid
- Övervaka applikationsstatus

Klicka helt enkelt på play-knappen bredvid "rag" för att starta denna modul, eller starta alla moduler på en gång.

<img src="../../../translated_images/sv/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Denna skärmbild visar Spring Boot Dashboard i VS Code, där du visuellt kan starta, stoppa och övervaka applikationer.*

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Från rotmappen
.\start-all.ps1
```

Eller starta endast denna modul:

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

Båda skripten laddar automatiskt miljövariabler från root `.env`-filen och bygger JAR-filerna om de inte finns.

> **Notera:** Om du föredrar att bygga alla moduler manuellt innan start:
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

## Använda applikationen

Applikationen erbjuder ett webbgränssnitt för dokumentuppladdning och frågor.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sv/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Denna skärmbild visar RAG-applikationens gränssnitt där du laddar upp dokument och ställer frågor.*

### Ladda upp ett dokument

Börja med att ladda upp ett dokument — TXT-filer fungerar bäst för testning. En `sample-document.txt` finns i denna katalog som innehåller information om LangChain4j-funktioner, RAG-implementering och bästa praxis — perfekt för att testa systemet.

Systemet bearbetar ditt dokument, delar upp det i delar, och skapar embeddingar för varje del. Detta sker automatiskt vid uppladdning.

### Ställ frågor

Ställ nu specifika frågor om dokumentets innehåll. Prova något faktabaserat som tydligt anges i dokumentet. Systemet söker efter relevanta delar, inkluderar dem i prompten och genererar ett svar.

### Kontrollera källa referenser

Notera att varje svar innehåller källreferenser med likhetspoäng. Dessa poäng (0 till 1) visar hur relevant varje del var för din fråga. Högre poäng betyder bättre matchningar. Detta låter dig verifiera svaret mot källmaterialet.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sv/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Denna skärmbild visar frågeresultat med genererat svar, källreferenser och relevanspoäng för varje hämtad del.*

### Experimentera med frågor

Testa olika typer av frågor:
- Specifika fakta: "Vad är huvudämnet?"
- Jämförelser: "Vad är skillnaden mellan X och Y?"
- Sammanfattningar: "Sammanfatta nyckelpunkterna om Z"

Observera hur relevanspoängen ändras beroende på hur väl din fråga matchar dokumentinnehållet.

## Nyckelkoncept

### Delningsstrategi

Dokument delas upp i 300-token delar med 30 tokens överlappning. Denna balans säkerställer att varje del har tillräckligt med kontext för att vara meningsfull, samtidigt som den är tillräckligt liten för att flera delar ska kunna ingå i en prompt.

### Likhetspoäng

Varje hämtad del har en likhetspoäng mellan 0 och 1 som visar hur väl den matchar användarens fråga. Diagrammet nedan visualiserar poängintervallen och hur systemet använder dem för att filtrera resultat:

<img src="../../../translated_images/sv/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Detta diagram visar poängintervall från 0 till 1, med en minimal tröskel på 0,5 som filtrerar bort irrelevanta delar.*

Poängintervallet är 0 till 1:
- 0,7–1,0: Mycket relevant, exakt matchning
- 0,5–0,7: Relevant, bra kontext
- Under 0,5: Bortfiltrerad, för olik

Systemet hämtar endast delar över minimigränsen för att säkerställa kvalitet.

Embeddingar fungerar bra när betydelser klustras tydligt, men de har svagheter. Diagrammet nedan visar vanliga fel — delar som är för stora ger brusiga vektorer, delar som är för små saknar kontext, tvetydiga termer pekar på flera kluster, och exakta sökningar (ID:n, artikelnummer) fungerar inte alls med embeddingar:

<img src="../../../translated_images/sv/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Detta diagram visar vanliga fel med embeddingar: för stora delar, för små delar, tvetydiga termer som pekar på flera kluster och exakta sökningar som ID:n.*

### Minneslagring

Denna modul använder minneslagring för enkelhetens skull. När du startar om applikationen förloras uppladdade dokument. Produktionssystem använder permanenta vektordatabaser som Qdrant eller Azure AI Search.

### Hantering av kontextfönster

Varje modell har ett maximalt kontextfönster. Du kan inte inkludera varje del från ett stort dokument. Systemet hämtar de bästa N mest relevanta delarna (standard är 5) för att hålla sig inom gränserna samtidigt som tillräckligt med kontext ges för korrekta svar.

## När RAG är viktigt

RAG är inte alltid rätt tillvägagångssätt. Beslutsguiden nedan hjälper dig avgöra när RAG tillför värde jämfört med när enklare metoder — som att inkludera innehåll direkt i prompten eller förlita sig på modellens inbyggda kunskap — räcker:

<img src="../../../translated_images/sv/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Detta diagram visar en beslutsguide för när RAG tillför värde jämfört med när enklare metoder räcker.*

**Använd RAG när:**
- Frågor gäller proprietära dokument
- Information ändras ofta (policyer, priser, specifikationer)
- Noggrannhet kräver källhänvisning
- Innehållet är för stort för att rymmas i en enda prompt
- Du behöver verifierbara, grundade svar

**Använd inte RAG när:**
- Frågor kräver allmän kunskap som modellen redan har
- Realtidsdata behövs (RAG fungerar på uppladdade dokument)
- Innehållet är tillräckligt litet för att inkluderas direkt i promptar

## Nästa steg

**Nästa modul:** [04-tools - AI-agenter med verktyg](../04-tools/README.md)

---

**Navigering:** [← Föregående: Modul 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Tillbaka till huvudsida](../README.md) | [Nästa: Modul 04 - Verktyg →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen observera att automatiska översättningar kan innehålla fel eller inexaktheter. Det ursprungliga dokumentet på dess modersmål ska betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår från användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->