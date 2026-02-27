# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
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

## Video Walkthrough

Panoorin ang live session na ito na naglalaman ng paliwanag kung paano simulan ang module na ito: [RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## What You'll Learn

Sa mga naunang module, natutunan mo kung paano makipag-usap sa AI at ayusin nang maayos ang iyong mga prompt. Ngunit may pangunahing limitasyon: ang mga language model ay alam lamang ang mga natutunan nila sa panahon ng pagsasanay. Hindi nila masasagot ang mga tanong tungkol sa mga patakaran ng iyong kumpanya, dokumentasyon ng iyong proyekto, o anumang impormasyon na hindi nila natutunan.

Nilulutas ng RAG (Retrieval-Augmented Generation) ang problemang ito. Sa halip na subukang turuan ang modelo ng iyong impormasyon (na magastos at hindi praktikal), binibigyan mo ito ng kakayahang maghanap sa iyong mga dokumento. Kapag may nagtanong, hahanapin ng sistema ang may-katuturang impormasyon at isasama ito sa prompt. Sasagot ang modelo base sa nakuhang konteksto.

Isipin ang RAG bilang pagbibigay sa modelo ng isang reference library. Kapag nagtanong ka, ang sistema ay:

1. **User Query** - Nagtanong ka ng isang tanong
2. **Embedding** - Kinoconvert ang iyong tanong sa vector
3. **Vector Search** - Hinahanap ang mga document chunk na magkakatulad
4. **Context Assembly** - Idinadagdag ang mga kaugnay na chunks sa prompt
5. **Response** - Gumagawa ang LLM ng sagot base sa konteksto

Ito ay nagpapatatag ng mga sagot ng modelo sa iyong aktwal na datos sa halip na umasa lamang sa kaalaman mula sa pagsasanay o gumawa ng sagot.

## Prerequisites

- Natapos ang [Module 00 - Quick Start](../00-quick-start/README.md) (para sa Easy RAG example na tinukoy sa itaas)
- Natapos ang [Module 01 - Introduction](../01-introduction/README.md) (na-deploy ang Azure OpenAI resources, kasama ang `text-embedding-3-small` embedding model)
- `.env` file sa root directory na may Azure credentials (nalikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga instruksyon sa deployment doon. Nagde-deploy ang `azd up` command ng parehong GPT chat model at embedding model na gamit sa module na ito.

## Understanding RAG

Ipinapakita ng diagram sa ibaba ang pangunahing konsepto: sa halip na umasa lamang sa data ng pagsasanay ng modelo, binibigyan ng RAG ito ng reference library ng iyong mga dokumento upang konsultahin bago gumawa ng sagot.

<img src="../../../translated_images/tl/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ipinapakita ng diagram na ito ang pagkakaiba sa pagitan ng karaniwang LLM (na naghuhula mula sa training data) at ng RAG-enhanced LLM (na kumukonsulta muna sa iyong mga dokumento).*

Ganito kumonekta ang mga bahagi mula simula hanggang dulo. Dumadaloy ang tanong ng gumagamit sa apat na yugto — embedding, vector search, context assembly, at answer generation — na ang bawat isa ay sumusunod sa naunang hakbang:

<img src="../../../translated_images/tl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ipinapakita ng diagram na ito ang end-to-end na RAG pipeline — dumadaloy ang user query sa embedding, vector search, context assembly, at answer generation.*

Ang natitirang bahagi ng module na ito ay magsasaad ng bawat yugto nang detalyado, kasama ang code na maaari mong patakbuhin at baguhin.

### Which RAG Approach Does This Tutorial Use?

Nag-aalok ang LangChain4j ng tatlong paraan upang ipatupad ang RAG, bawat isa ay may ibang antas ng abstraction. Ikinukumpara ang mga ito sa diagram sa ibaba nang magkahiwalay:

<img src="../../../translated_images/tl/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ikinukumpara ng diagram na ito ang tatlong LangChain4j RAG approaches — Easy, Native, at Advanced — na ipinapakita ang kanilang mga pangunahing sangkap at kung kailan gamitin ang bawat isa.*

| Approach | Ano ang Ginagawa Nito | Trade-off |
|---|---|---|
| **Easy RAG** | Awtomatikong kinokonekta ang lahat sa pamamagitan ng `AiServices` at `ContentRetriever`. Nagraranggo ka lamang ng interface, nagdadagdag ng retriever, at ang LangChain4j ang nag-aasikaso ng embedding, paghahanap, at pagpupulong ng prompt sa likod ng eksena. | Minimal na code, ngunit hindi mo nakikita ang nangyayari sa bawat hakbang. |
| **Native RAG** | Tinatawag mo ang embedding model, naghahanap sa store, bumubuo ng prompt, at gumagawa ng sagot nang direkta — isang malinaw na hakbang bawat pagkakataon. | Mas maraming code, ngunit bawat yugto ay makikita at maaaring baguhin. |
| **Advanced RAG** | Ginagamit ang `RetrievalAugmentor` framework na may mga pluggable query transformers, routers, re-rankers, at content injectors para sa mga produksyong pipeline. | Pinakamataas na flexibility, ngunit mas komplikado. |

**Ang tutorial na ito ay gumagamit ng Native approach.** Ang bawat hakbang ng RAG pipeline — pag-embed sa query, paghahanap sa vector store, pagpupulong ng konteksto, at paggawa ng sagot — ay nakasulat nang malinaw sa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Ito ay sinasadya: bilang isang learning resource, mas mahalaga na makita at maintindihan mo ang bawat yugto kaysa ipaikli ang code. Kapag komportable ka na sa pagkakaugnay ng mga bahagi, maaari kang lumipat sa Easy RAG para sa mabilisang prototype o sa Advanced RAG para sa mga sistema sa produksiyon.

> **💡 Nakita mo na ba ang Easy RAG sa aksyon?** Kasama sa [Quick Start module](../00-quick-start/README.md) ang Document Q&A example ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) na gumagamit ng Easy RAG approach — ang LangChain4j ang nangangalaga sa embedding, paghahanap, at pagpupulong ng prompt nang awtomatiko. Ang module na ito ay sumusunod sa susunod na hakbang sa pamamagitan ng paglabas ng pipeline na iyon upang makita at makontrol mo ang bawat yugto.

<img src="../../../translated_images/tl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ipinapakita ng diagram na ito ang Easy RAG pipeline mula sa `SimpleReaderDemo.java`. Ihambing ito sa Native approach na ginamit sa module na ito: itinatago ng Easy RAG ang embedding, retrieval, at prompt assembly sa likod ng `AiServices` at `ContentRetriever` — naglo-load ka ng dokumento, naglalakip ng retriever, at humihingi ng mga sagot. Ang Native approach sa module na ito ay binubuksan ang pipeline upang tawagin mo mismo ang bawat yugto (embed, search, assemble context, generate), na nagbibigay ng buong visibility at kontrol.*

## How It Works

Ang RAG pipeline sa module na ito ay nahahati sa apat na yugto na tumatakbo sunod-sunod sa tuwing magtatanong ang isang user. Una, ang na-upload na dokumento ay **pinoproseso at pinaghahati-hati** sa mga piraso na kayang hawakan. Ang mga pirasong ito ay kino-convert sa **vector embeddings** at itinatago upang maikumpara sa matematikal na paraan. Kapag dumating ang query, nagsasagawa ang sistema ng **semantic search** upang mahanap ang pinaka-nauugnay na mga chunk, at sa wakas ay ipinapasa ang mga ito bilang konteksto sa LLM para sa **paggawa ng sagot**. Ang mga seksyon sa ibaba ay naglalakad sa bawat yugto kasama ang aktwal na code at mga diagram. Tingnan muna natin ang unang hakbang.

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kapag nag-upload ka ng dokumento, pinoproseso ito ng sistema (PDF o plain text), nilalagyan ng metadata tulad ng filename, at pagkatapos ay hinahati sa mga chunk — mas maliliit na piraso na kayang-kayang maipasok sa context window ng modelo. Ang mga chunk na ito ay bahagyang nag-o-overlap upang hindi mawala ang konteksto sa mga hangganan.

```java
// I-parse ang na-upload na file at balutin ito sa isang LangChain4j Document
Document document = Document.from(content, metadata);

// Hatiin sa mga 300-token na bahagi na mayroong 30-token na overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Ipinapakita ng diagram sa ibaba kung paano ito gumagana nang biswal. Pansinin kung paano nagkakapareho ang mga token ng bawat chunk sa mga kapitbahay nito — ang 30-token overlap ay nagsisiguro na walang mahalagang konteksto ang matutulo sa pagitan:

<img src="../../../translated_images/tl/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ipinapakita ng diagram na ito ang paghati ng dokumento sa 300-token chunks na may 30-token overlap, na pinapangalagaan ang konteksto sa mga hangganan ng chunk.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) at itanong:
> - "Paano hinahati ng LangChain4j ang mga dokumento sa mga chunk at bakit mahalaga ang overlap?"
> - "Ano ang pinakamainam na laki ng chunk para sa iba't ibang uri ng dokumento at bakit?"
> - "Paano ko pinamamahalaan ang mga dokumento sa maraming lengguwahe o may espesyal na pormat?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Ang bawat chunk ay kino-convert sa isang numerical representation na tinatawag na embedding — isang uri ng meaning-to-numbers converter. Ang embedding model ay hindi "matalino" tulad ng chat model; hindi ito sumusunod sa mga utos, hindi nakakapangatuwiran, at hindi sumasagot ng mga tanong. Ang kaya nitong gawin ay i-map ang teksto sa isang matematikal na espasyo kung saan ang magkaugnay na mga kahulugan ay magkakalapit — "car" malapit sa "automobile," "refund policy" malapit sa "return my money." Isipin ang chat model bilang isang tao na maaari mong kausapin; ang embedding model naman ay isang napakahusay na filing system.

<img src="../../../translated_images/tl/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ipinapakita ng diagram na ito kung paano kino-convert ng embedding model ang teksto sa mga numerical vector, na inilalagay ang magkatulad na kahulugan — tulad ng "car" at "automobile" — na magkakalapit sa vector space.*

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

Ipinapakita ng class diagram sa ibaba ang dalawang hiwalay na daloy sa RAG pipeline at ang mga klaseng LangChain4j na nagpapatupad ng mga ito. Ang **ingestion flow** (tumatakbo isang beses sa oras ng upload) ay naghahati sa dokumento, nag-eembed ng mga chunks, at nagtatago sa pamamagitan ng `.addAll()`. Ang **query flow** (tumatakbo sa bawat tanong ng user) ay nag-eembed sa tanong, naghahanap sa store sa pamamagitan ng `.search()`, at ipinapasa ang tugmang konteksto sa chat model. Nagtutugma ang dalawang daloy sa shared `EmbeddingStore<TextSegment>` interface:

<img src="../../../translated_images/tl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ipinapakita ng diagram na ito ang dalawang daloy sa RAG pipeline — ingestion at query — at kung paano sila nagkakonekta sa pamamagitan ng shared na EmbeddingStore.*

Kapag naitago na ang embeddings, natural na nagkakaroon ng clustering ng magkaugnay na nilalaman sa vector space. Ipinapakita ng biswal sa ibaba kung paano nauuwi ang mga dokumento tungkol sa magkakaugnay na paksa bilang magkalapit na puntos, na siyang nagpapagana sa semantic search:

<img src="../../../translated_images/tl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ipinapakita ng biswal na ito kung paano nagkakluster ang mga magkaugnay na dokumento sa 3D vector space, na may mga paksang tulad ng Technical Docs, Business Rules, at FAQs na bumubuo ng mga natatanging grupo.*

Kapag naghahanap ang user, sinusunod ng sistema ang apat na hakbang: i-embed ang mga dokumento nang isang beses, i-embed ang query sa bawat paghahanap, ikumpara ang query vector laban sa lahat ng itinatag na vector gamit ang cosine similarity, at ibalik ang nangungunang-K mga highest-scoring chunks. Ipinapakita ng diagram sa ibaba ang bawat hakbang at mga klaseng LangChain4j na sangkot:

<img src="../../../translated_images/tl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ipinapakita ng diagram na ito ang apat na hakbang ng embedding search process: embed ang mga dokumento, embed ang query, ikumpara ang mga vector gamit ang cosine similarity, at ibalik ang top-K na resulta.*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kapag nagtanong ka, ang iyong tanong ay nagiging embedding din. Kinukumpara ng sistema ang embedding ng iyong tanong sa lahat ng embedding ng mga document chunk. Hinahanap nito ang mga chunk na pinaka-magkakatulad ang kahulugan — hindi lang tumutugma sa mga keyword, kundi tunay na semantic similarity.

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

Inihahambing ng diagram sa ibaba ang semantic search sa tradisyunal na keyword search. Ang keyword search para sa "vehicle" ay hindi nakahanap ng chunk tungkol sa "cars and trucks," ngunit nauunawaan ng semantic search na pareho ang ibig sabihin nito at ibinabalik ito bilang mataas ang iskor na tugma:

<img src="../../../translated_images/tl/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ipinapakita ng diagram na ito ang pagkakaiba ng keyword-based search at semantic search, na nagpapakita kung paano kinukuha ng semantic search ang mga konseptong magkakaugnay kahit naiiba ang eksaktong mga keyword.*

Sa ilalim, tinataya ang similarity gamit ang cosine similarity — parang tinatanong "pareho ba ang direksyon ng dalawang arrow na ito?" Puwedeng ganap na magkaibang salita ang dalawang chunk, ngunit kung pareho ang ibig sabihin, ang mga vector nila ay nakaturo sa parehong direksyon at ang iskor ay malapit sa 1.0:

<img src="../../../translated_images/tl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Ipinapakita ng diagram na ito ang cosine similarity bilang anggulo sa pagitan ng embedding vectors — mas magkapareho ang oryentasyon ng vectors, mas malapit ang score sa 1.0, na naglalarawan ng mas mataas na semantic similarity.*
> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) at itanong:
> - "Paano gumagana ang similarity search gamit ang embeddings at ano ang nagtatalaga ng score?"
> - "Anong similarity threshold ang dapat kong gamitin at paano ito nakakaapekto sa mga resulta?"
> - "Paano ko haharapin ang mga kaso kung saan walang natagpuang relevant na dokumento?"

### Pagbuo ng Sagot

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ang mga pinaka-nauugnay na chunks ay pinagsasama sa isang istrukturadong prompt na naglalaman ng malinaw na mga tagubilin, ang narekober na konteksto, at ang tanong ng user. Binabasa ng modelo ang mga partikular na chunks na iyon at sumasagot base sa impormasyong iyon — maaari lamang nitong gamitin ang nasa harap nito, na pumipigil sa hallucination.

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

Ipinapakita ng diagram sa ibaba ang prosesong ito — ang mga pinaka mataas ang score na chunks mula sa search step ay inilalagay sa prompt template, at ang `OpenAiOfficialChatModel` ay bumubuo ng makatotohanang sagot:

<img src="../../../translated_images/tl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ipinapakita ng diagram na ito kung paano pinagsasama ang mga pinaka mataas ang score na chunks sa isang istrukturadong prompt, na nagpapahintulot sa modelo na makabuo ng makatotohanang sagot mula sa iyong data.*

## Patakbuhin ang Aplikasyon

**Kumpirmahin ang deployment:**

Siguraduhing may `.env` file sa root directory na may Azure credentials (nilikha noong Module 01):

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa Module 01, ang module na ito ay tumatakbo na sa port 8081. Maaari mong laktawan ang mga simulaang command sa ibaba at pumunta nang direkta sa http://localhost:8081.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot applications. Matatagpuan ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan ang lahat ng available na Spring Boot applications sa workspace
- Simulan/hinto ang mga aplikasyon gamit ang isang click lang
- Tingnan ang mga logs ng aplikasyon nang real-time
- Subaybayan ang status ng aplikasyon

I-click lang ang play button sa tabi ng "rag" para simulan ang module na ito, o simulan lahat ng mga module nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ipinapakita ng screenshot na ito ang Spring Boot Dashboard sa VS Code, kung saan maaari mong simulan, tigilan, at subaybayan ang mga aplikasyon nang biswal.*

**Opsyon 2: Paggamit ng shell scripts**

Simulan ang lahat ng web applications (mga module 01-04):

**Bash:**
```bash
cd ..  # Mula sa ugat na direktoryo
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

Awtomatikong nilalagay ng parehong script ang mga environment variables mula sa root `.env` file at magbuo ng mga JAR kung wala pa ito.

> **Tandaan:** Kung gusto mong buuin nang manu-mano ang lahat ng modules bago magsimula:
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

Buksan ang http://localhost:8081 sa iyong browser.

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Sa modulong ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng web interface para sa pag-upload ng dokumento at pagtatanong.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ipinapakita ng screenshot na ito ang interface ng RAG application kung saan nag-upload ka ng mga dokumento at nagtatanong.*

### Mag-upload ng Dokumento

Magsimula sa pag-upload ng dokumento - pinakaakma ang mga TXT file para sa pagsusulit. Mayroong `sample-document.txt` na ibinigay sa direktoryong ito na naglalaman ng impormasyon tungkol sa mga feature ng LangChain4j, implementasyon ng RAG, at mga pinakamahusay na praktis - perpekto para subukan ang sistema.

Pinoproseso ng sistema ang iyong dokumento, hinahati ito sa mga chunks, at lumilikha ng embeddings para sa bawat chunk. Nangyayari ito nang awtomatiko kapag nag-upload ka.

### Magtanong

Ngayon magtanong ng mga partikular na tanong tungkol sa nilalaman ng dokumento. Subukan ang mga factual na tanong na malinaw na nakasaad sa dokumento. Naghahanap ang sistema ng mga kaugnay na chunks, isinama ang mga ito sa prompt, at bumubuo ng sagot.

### Suriin ang Mga Sanggunian

Mapapansin na bawat sagot ay may kasamang mga sanggunian ng pinagmulan na may mga similarity score. Ipinapakita ng mga score na ito (0 hanggang 1) kung gaano ka-relevant ang bawat chunk sa iyong tanong. Mas mataas ang score, mas maganda ang tugma. Pinapahintulutan ka nitong beripikahin ang sagot batay sa source na materyal.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ipinapakita ng screenshot na ito ang resulta ng query kasama ang nabuo na sagot, mga sanggunian mula sa pinagmulan, at mga relevance score para sa bawat narekober na chunk.*

### Subukan ang Iba't Ibang Tanong

Subukan ang iba't ibang uri ng tanong:
- Mga espesipikong katotohanan: "Ano ang pangunahing paksa?"
- Paghahambing: "Ano ang pagkakaiba ng X at Y?"
- Buod: "Ibuod ang mga pangunahing puntos tungkol sa Z"

Pansinin kung paano nagbabago ang relevance scores base sa kung gaano kahusay tumutugma ang iyong tanong sa nilalaman ng dokumento.

## Mga Pangunahing Konsepto

### Chunking Strategy

Hinahati ang mga dokumento sa 300-token na mga chunk na may 30 token na overlap. Ang balanse na ito ay tinitiyak na bawat chunk ay may sapat na konteksto upang maging makahulugan habang nananatiling sapat na maliit upang maisama ang maramihang chunks sa isang prompt.

### Similarity Scores

Bawat narekober na chunk ay may kasamang similarity score mula 0 hanggang 1 na nagpapakita kung gaano kalapit ito sa tanong ng user. Ipinapakita ng diagram sa ibaba ang mga range ng score at kung paano ginagamit ng sistema ang mga ito upang i-filter ang mga resulta:

<img src="../../../translated_images/tl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ipinapakita ng diagram na ito ang mga range ng score mula 0 hanggang 1, na may minimum threshold na 0.5 na nagfi-filter ng mga hindi relevant na chunk.*

Ang mga score ay naglalaro mula 0 hanggang 1:
- 0.7-1.0: Napakahalaga, eksaktong tugma
- 0.5-0.7: Kaugnay, magandang konteksto
- Mababa sa 0.5: Na-filter, masyadong malayo ang pagkakatugma

Kinukuha lang ng sistema ang mga chunk na lampas sa minimum threshold para matiyak ang kalidad.

Maganda ang performance ng embeddings kapag malinis ang pagkaka-cluster ng kahulugan, ngunit meron itong mga blind spot. Ipinapakita ng diagram sa ibaba ang mga karaniwang failure mode — ang mga chunks na masyadong malaki ay nagbubunga ng malabong vectors, ang mga chunks na masyadong maliit ay kulang sa konteksto, ang mga ambiguous terms ay tumutukoy sa maraming cluster, at ang eksaktong pagtutugma ng lookup (mga ID, part numbers) ay hindi gumagana sa embeddings:

<img src="../../../translated_images/tl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ipinapakita ng diagram na ito ang mga karaniwang failure mode ng embedding: mga sobrang laki o sobrang laki na chunks, mga ambiguous na termino na tumutukoy sa maraming cluster, at mga eksaktong pagtingin tulad ng mga ID.*

### In-Memory Storage

Gumagamit ang modulong ito ng in-memory storage para sa pagiging simple. Kapag ni-restart mo ang aplikasyon, mawawala ang mga na-upload na dokumento. Gumagamit ang mga production system ng persistent vector databases tulad ng Qdrant o Azure AI Search.

### Context Window Management

Bawat modelo ay may maximum na context window. Hindi mo maaaring isama lahat ng chunks mula sa isang malaking dokumento. Kinukuha ng sistema ang top N na pinaka-kaugnay na chunks (default ay 5) upang manatili sa loob ng limitasyon habang nagbibigay ng sapat na konteksto para sa tumpak na mga sagot.

## Kailan Mahalaga ang RAG

Hindi palaging angkop ang RAG. Ang gabay sa pagpapasya sa ibaba ay tumutulong sa iyo matukoy kung kailan may halaga ang RAG kumpara sa mga simpleng paraan — tulad ng pagsasama ng nilalaman direkta sa prompt o pag-asa sa built-in na kaalaman ng modelo:

<img src="../../../translated_images/tl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ipinapakita ng diagram na ito ang gabay sa pagpapasya kung kailan may halaga ang RAG kumpara sa kung kailan sapat ang mga simpleng paraan.*

**Gamitin ang RAG kapag:**
- Sumagot sa mga tanong tungkol sa proprietary na mga dokumento
- Madalas nagbabago ang impormasyon (mga polisiya, presyo, espesipikasyon)
- Nangangailangan ng katumpakan na may pagbanggit ng pinagmulan
- Masyadong malaki ang nilalaman para maisama sa isang prompt
- Kailangan ng maireberipikang, nakabatay na mga sagot

**Huwag gamitin ang RAG kapag:**
- Mga tanong na nangangailangan ng pangkalahatang kaalaman na mayroon na ang modelo
- Kailangan ng real-time na datos (gumagana ang RAG sa mga na-upload na dokumento)
- Maliit lang ang nilalaman kaya maaaring isama direkta sa mga prompt

## Mga Susunod na Hakbang

**Susunod na Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Nakaraan: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Balik sa Pangunahing Pahina](../README.md) | [Susunod: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pahayag ng Paunawa**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat aming nilalayon ang katumpakan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahalagang impormasyon, rekomendado ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->