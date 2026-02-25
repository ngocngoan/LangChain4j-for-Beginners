# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [What You'll Learn](../../../03-rag)
- [Understanding RAG](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## What You'll Learn

Sa mga nakaraang modules, natutunan mo kung paano makipag-usap sa AI at paano ayusin ng maayos ang iyong mga prompts. Ngunit may isang pangunahing limitasyon: ang mga language model ay alam lamang ang kanilang natutunan sa panahon ng training. Hindi nila masasagot ang mga tanong tungkol sa mga polisiya ng iyong kumpanya, mga dokumento ng iyong proyekto, o anumang impormasyon na hindi nila natutunan.

Nilulutas ng RAG (Retrieval-Augmented Generation) ang problema na ito. Sa halip na subukan turuan ang model ng iyong impormasyon (na mahal at hindi praktikal), binibigyan mo ito ng kakayahang maghanap sa loob ng iyong mga dokumento. Kapag may nagtanong, ang sistema ay hinahanap ang may-kaugnayang impormasyon at isinama ito sa prompt. Ang model ay sasagot batay sa naka-retrieve na konteksto.

Isipin ang RAG bilang pagbibigay ng model ng isang sanggunian na aklatan. Kapag nagtanong ka, ang sistema ay:

1. **User Query** - Magtatanong ka
2. **Embedding** - Kinokonvert ang tanong mo sa vector
3. **Vector Search** - Hahanapin ang mga magkaparehong bahagi ng dokumento
4. **Context Assembly** - Isinasama ang mga may-kaugnayang bahagi sa prompt
5. **Response** - Gumagawa ng sagot ang LLM batay sa konteksto

Ito ay naglalagay ng sagot ng model sa iyong aktwal na datos sa halip na umiasa lang sa naunang training o gumawa ng imbento na sagot.

## Understanding RAG

Ipinapakita sa diagram sa ibaba ang pangunahing konsepto: sa halip na umasa lang sa training data ng model, binibigyan ng RAG ito ng sangguniang aklatan ng iyong mga dokumento para konsultahin bago bawat sagot.

<img src="../../../translated_images/tl/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Ganito nag-uugnay ang mga bahagi mula sa simula hanggang dulo. Ang tanong ng user ay dumadaan sa apat na yugto — embedding, vector search, context assembly, at answer generation — bawat isa ay nakabatay sa nauna:

<img src="../../../translated_images/tl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

Ang natitirang bahagi ng module na ito ay maglalakad sa bawat yugto nang detalyado, kasama ang code na maaari mong patakbuhin at baguhin.

## Prerequisites

- Natapos ang Module 01 (Azure OpenAI resources deployed)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubilin doon para sa deployment.

## How It Works

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kapag nag-upload ka ng dokumento, pini-parse ng sistema ito (PDF o plain text), nilalagyan ng metadata tulad ng pangalan ng file, pagkatapos hinahati ito sa mga chunks — mga maliliit na bahagi na kasya sa context window ng model. Ang mga chunks ay nag-o-overlap ng bahagya para hindi mawala ang konteksto sa mga hangganan.

```java
// I-parse ang in-upload na file at balutin ito sa LangChain4j Document
Document document = Document.from(content, metadata);

// Hatiin sa 300-token na mga bahagi na may 30-token na overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Ipinapakita ng diagram sa ibaba kung paano ito gumagana sa biswal na paraan. Mapapansin mo kung paano ang bawat chunk ay may mga tokens na pinaghahatian sa mga kalapit nito — ang 30-token overlap ay nagsisigurong walang mahalagang konteksto na mawawala:

<img src="../../../translated_images/tl/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) at itanong:
> - "Paano hinahati ng LangChain4j ang mga dokumento sa chunks at bakit mahalaga ang overlap?"
> - "Ano ang pinakamainam na laki ng chunk para sa iba't ibang uri ng dokumento at bakit?"
> - "Paano ko haharapin ang mga dokumento sa iba't ibang wika o may espesyal na formatting?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Ang bawat chunk ay kinokonvert sa numerikal na representasyon na tinatawag na embedding - isang matematikal na fingerprint na sumasalamin sa kahulugan ng teksto. Ang magkakatulad na teksto ay nagreresulta sa magkakatulad na embeddings.

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

Ipinapakita ng class diagram sa ibaba kung paano nag-uugnay ang mga LangChain4j na bahagi na ito. Ang `OpenAiOfficialEmbeddingModel` ay kailangang mag-convert ng teksto sa vectors, ang `InMemoryEmbeddingStore` ay naglalaman ng vectors kasabay ng kanilang orihinal na `TextSegment` data, at ang `EmbeddingSearchRequest` ang nagkokontrol ng retrieval parameters gaya ng `maxResults` at `minScore`:

<img src="../../../translated_images/tl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Kapag na-store na ang embeddings, natural na nagsasama-sama ang mga kaugnay na nilalaman sa vector space. Ang visual sa ibaba ay nagpapakita kung paano nagiging malapit ang mga dokumento na may kaugnayang paksa, na siyang nagpapagana ng semantic search:

<img src="../../../translated_images/tl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kapag nagtanong ka, ang tanong mo rin ay nagiging embedding. Kinukumpara ng sistema ang embedding ng tanong mo laban sa lahat ng embeddings ng mga chunks ng dokumento. Hinahanap nito ang mga chunks na may pinakamalapit na kahulugan — hindi lamang sa mga tugmang keyword kundi sa tunay na semantic similarity.

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

Ikinukumpara ng diagram sa ibaba ang semantic search sa tradisyonal na keyword search. Ang keyword search para sa "vehicle" ay hindi nakakita ng chunk tungkol sa "cars and trucks," pero nauunawaan ng semantic search na magkapareho ang ibig sabihin at ibinabalik ito bilang mataas na puntos na tugma:

<img src="../../../translated_images/tl/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) at itanong:
> - "Paano gumagana ang similarity search gamit ang embeddings at ano ang nagtatakda ng score?"
> - "Ano ang dapat na similarity threshold at paano ito nakakaapekto sa mga resulta?"
> - "Paano haharapin ang mga kaso kung walang makitang kaugnay na dokumento?"

### Answer Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ang mga pinaka-kaugnay na chunks ay pinagsasama sa isang istrukturadong prompt na may kasamang mga malinaw na tagubilin, ang na-retrieve na konteksto, at ang tanong ng user. Binabasa ng model ang mga partikular na chunks na iyon at sumasagot base sa impormasyong iyon — kaya lamang nitong gamitin ang nasa harap nito, na pumipigil sa hallucination.

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

Ipinapakita ng diagram sa ibaba ang aksyon ng pagsama-sama — ang mga top-scoring chunks mula sa search step ay inilalagay sa prompt template, at nililikha ng `OpenAiOfficialChatModel` ang grounded na sagot:

<img src="../../../translated_images/tl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Run the Application

**I-verify ang deployment:**

Siguraduhing mayroon ang `.env` file sa root directory na may Azure credentials (nilikha sa Module 01):
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang application:**

> **Note:** Kung sinimulan mo na ang lahat ng applications gamit ang `./start-all.sh` mula sa Module 01, tumatakbo na ang module na ito sa port 8081. Pwede mong laktawan ang mga start commands sa ibaba at pumunta diretso sa http://localhost:8081.

**Option 1: Gamit ang Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan ang lahat ng magagamit na Spring Boot applications sa workspace
- Simulan/ihinto ang applications gamit ang isang klik
- Tingnan ang mga application logs nang real-time
- Bantayan ang estado ng application

I-click lamang ang play button sa tabi ng "rag" para simulan ang module na ito, o simulan ang lahat ng modules nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Gamit ang shell scripts**

Simulan lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa punong direktoryo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa root na direktoryo
.\start-all.ps1
```

O simulan lang ang module na ito:

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

Awtomatikong kino-load ng parehong scripts ang environment variables mula sa root `.env` file at magbu-build ng JARs kung wala pa.

> **Note:** Kung nais mong i-build ang lahat ng modules nang manu-mano bago magsimula:
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

Buksan ang http://localhost:8081 sa iyong browser.

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Para lamang sa module na ito
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Para lang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```


## Using the Application

Nagbibigay ang application ng web interface para sa pag-upload ng dokumento at pagtatanong.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ang interface ng RAG application - mag-upload ng dokumento at magtanong*

### Upload a Document

Magsimula sa pag-upload ng dokumento - ang mga TXT files ay pinakamainam para sa testing. Mayroong ibinigay na `sample-document.txt` sa direktoryong ito na naglalaman ng impormasyon tungkol sa mga feature ng LangChain4j, implementasyon ng RAG, at mga best practices - perpekto para sa pagsubok ng sistema.

Pina-proseso ng sistema ang iyong dokumento, hinahati ito sa chunks, at gumagawa ng embeddings para sa bawat chunk. Nangyayari ito nang awtomatiko kapag nag-upload ka.

### Ask Questions

Ngayon ay magtanong ng mga tiyak na tanong tungkol sa nilalaman ng dokumento. Subukan ang mga pactual na malinaw na nakasaad. Hinahanap ng sistema ang mga may-kaugnayang chunks, isinama ang mga ito sa prompt, at bumubuo ng sagot.

### Check Source References

Mapapansin mong bawat sagot ay may kasamang mga sanggunian ng pinagmulan at similarity scores. Ipinapakita ng mga scores na ito (mula 0 hanggang 1) kung gaano ka-relevant ang bawat chunk sa tanong mo. Mas mataas na score ay mas magandang tugma. Pinapayagan kang beripikahin ang sagot laban sa orihinal na materyal.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mga resulta ng query na nagpapakita ng sagot kasama ang mga sanggunian at relevance scores*

### Experiment with Questions

Subukan ang iba't ibang uri ng mga tanong:
- Mga tiyak na katotohanan: "Ano ang pangunahing paksa?"
- Mga paghahambing: "Ano ang pagkakaiba ng X at Y?"
- Mga buod: "I-summarize ang mga pangunahing punto tungkol sa Z"

Panoorin kung paano nagbabago ang relevance scores batay sa kung gaano kahusay ang tanong mo ay tumutugma sa nilalaman ng dokumento.

## Key Concepts

### Chunking Strategy

Hinahati ang mga dokumento sa 300-token na chunks na may 30 token na overlap. Ang balanse na ito ay nagsisigurong bawat chunk ay may sapat na konteksto para maging makahulugan habang nananatiling maliit para maisama ang maraming chunks sa isang prompt.

### Similarity Scores

Bawat nakuha na chunk ay may similarity score mula 0 hanggang 1 na nagpapakita kung gaano kalapit ito sa tanong ng user. Ipinapakita ng diagram sa ibaba ang mga ranges ng score at paano ito ginagamit ng sistema para i-filter ang mga resulta:

<img src="../../../translated_images/tl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Ang mga score ay mula 0 hanggang 1:
- 0.7-1.0: Napaka-relevant, exact match
- 0.5-0.7: Relevant, magandang konteksto
- Baba 0.5: Ipinagsasala, masyadong di magkatugma

Kinu-kuha lang ng sistema ang mga chunks na lampas sa minimum na threshold para sa kalidad.

### In-Memory Storage

Ginagamit ng module na ito ang in-memory storage para sa pagiging simple. Kapag nirestart mo ang application, mawawala ang nai-upload na mga dokumento. Sa production, gumagamit ang mga sistema ng persistent vector databases tulad ng Qdrant o Azure AI Search.

### Context Window Management

Bawat model ay may maximum na context window. Hindi mo pwedeng isama lahat ng chunk mula sa malaking dokumento. Kinukuha ng sistema ang top N na pinaka-kaugnay na chunks (by default 5) para manatili sa limitasyon sabay bigyan ng sapat na konteksto para sa tumpak na sagot.

## When RAG Matters

Hindi palaging angkop ang RAG. Ang gabay sa pagpapasya sa ibaba ay tutulong sa iyo malaman kung kailan nagdadagdag ng halaga ang RAG kumpara sa mas simpleng paraan — gaya ng pagsasama ng content direkta sa prompt o pag-asa sa built-in na kaalaman ng model:

<img src="../../../translated_images/tl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Gamitin ang RAG kapag:**
- Pagsagot sa mga tanong tungkol sa mga proprietary na dokumento  
- Madalas magbago ang impormasyon (mga polisiya, presyo, espesipikasyon)  
- Kinakailangan ng katumpakan ng pagkukunan ng impormasyon  
- Napakalaki ng nilalaman upang magkasya sa isang prompt lamang  
- Kailangan ng mga nasusuring, naka-ugat na tugon  

**Huwag gumamit ng RAG kapag:**  
- Ang mga tanong ay nangangailangan ng pangkalahatang kaalaman na hawak na ng modelo  
- Kailangan ang real-time na datos (gumagana ang RAG sa mga in-upload na dokumento)  
- Maliit lang ang nilalaman na maaring isama nang direkta sa mga prompt  

## Mga Susunod na Hakbang  

**Susunod na Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)  

---  

**Navigasyon:** [← Nakaraan: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Bumalik sa Pangunahing](../README.md) | [Susunod: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagaman aming pinagsisikapang maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mga mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng isang tao. Hindi kami mananagot sa anumang mga hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng salin na ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->