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

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula sa module na ito:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

Sa mga naunang mga module, natutunan mo kung paano makipag-usap sa AI at paano mahusay na ayusin ang iyong mga prompt. Ngunit may pangunahing limitasyon: Ang mga language model ay alam lamang ang mga natutunan nila sa panahon ng pagsasanay. Hindi nila kayang sagutin ang mga tanong tungkol sa mga patakaran ng iyong kumpanya, dokumentasyon ng iyong proyekto, o anumang impormasyon na hindi kasama sa kanilang pagsasanay.

Nilulutas ng RAG (Retrieval-Augmented Generation) ang problemang ito. Sa halip na subukang turuan ang modelo ng iyong impormasyon (na mahal at hindi praktikal), binibigyan mo ito ng kakayahang maghanap sa iyong mga dokumento. Kapag may nagtatanong, hinahanap ng sistema ang mga kaugnay na impormasyon at isinasama ito sa prompt. Ang modelo ay sasagot base sa nakuha nitong konteksto.

Isipin ang RAG na parang pagbibigay sa modelo ng isang sanggunian na aklatan. Kapag nagtanong ka:

1. **User Query** - Nagbibigay ka ng tanong
2. **Embedding** - Kinokonvert ang tanong mo sa vector
3. **Vector Search** - Hinahanap ang mga kahalintulad na bahagi ng dokumento
4. **Context Assembly** - Idinadagdag ang mga kaugnay na bahagi sa prompt
5. **Response** - Gumagawa ang LLM ng sagot base sa konteksto

Ito ay nagtatakda ng mga sagot ng modelo base sa aktwal mong data sa halip na umasa lamang sa kanyang kaalaman sa pagsasanay o paggawa ng mga sagot.

## Prerequisites

- Nakumpleto ang [Module 00 - Quick Start](../00-quick-start/README.md) (para sa Easy RAG na halimbawa na binanggit sa kalaunan sa module na ito)
- Nakumpleto ang [Module 01 - Introduction](../01-introduction/README.md) (mga Azure OpenAI resources na na-deploy, kabilang ang `text-embedding-3-small` embedding model)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Note:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubilin sa pag-deploy doon. Ang command na `azd up` ay nagde-deploy ng GPT chat model at embedding model na ginagamit ng module na ito.

## Understanding RAG

Ipinapakita ng diagram sa ibaba ang pangunahing konsepto: sa halip na umasa lamang sa data ng pagsasanay ng modelo, binibigyan ng RAG ito ng isang sangguniang aklatan ng iyong mga dokumento na maaaring tingnan bago bumuo ng bawat sagot.

<img src="../../../translated_images/tl/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Ipinapakita ng diagram na ito ang pagkakaiba ng isang karaniwang LLM (na humuhula mula sa training data) at isang RAG-enhanced LLM (na kumukonsulta muna sa iyong mga dokumento).*

Ganito ang pagkakaugnay ng mga bahagi mula simula hanggang wakas. Dumadaan ang tanong ng user sa apat na yugto — embedding, vector search, context assembly, at answer generation — bawat isa ay nakasalalay sa naunang hakbang:

<img src="../../../translated_images/tl/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Ipinapakita ng diagram na ito ang end-to-end na RAG pipeline — dumadaan ang user query sa embedding, vector search, context assembly, at answer generation.*

Ang natitirang bahagi ng module ay naglalakad sa bawat yugto nang detalyado, kasama ang code na pwede mong patakbuhin at i-modify.

### Which RAG Approach Does This Tutorial Use?

Nag-aalok ang LangChain4j ng tatlong paraan upang ipatupad ang RAG, bawat isa ay may iba't ibang antas ng abstraction. Ikinukumpara sila sa diagram sa ibaba nang magkaharap:

<img src="../../../translated_images/tl/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Ipinapakita ng diagram na ito ang tatlong LangChain4j RAG na paraan — Easy, Native, at Advanced — na nagpapakita ng kanilang mga pangunahing bahagi at kung kailan gamitin ang bawat isa.*

| Approach | What It Does | Trade-off |
|---|---|---|
| **Easy RAG** | Awtomatikong kinakabit ang lahat sa pamamagitan ng `AiServices` at `ContentRetriever`. Nag-annotate ka lang ng interface, nagdadagdag ng retriever, at ang LangChain4j ang humahandle ng embedding, paghahanap, at pagbuo ng prompt sa likod ng mga eksena. | Minimal na code, ngunit hindi mo nakikita ang nangyayari sa bawat hakbang. |
| **Native RAG** | Tinatawagan mo ang embedding model, naghahanap sa store, binubuo ang prompt, at gumagawa ng sagot ng manu-mano — isa-isang hakbang. | Mas maraming code, ngunit makikita at maima-modify ang bawat yugto. |
| **Advanced RAG** | Ginagamit ang `RetrievalAugmentor` framework na may pluggable query transformers, routers, re-rankers, at content injectors para sa production-grade pipelines. | Pinakamataas na flexibility, ngunit napakakomplikado. |

**Ang tutorial na ito ay gumagamit ng Native na paraan.** Isinusulat nang malinaw sa [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ang bawat hakbang ng RAG pipeline — embedding ng query, paghahanap sa vector store, pagbuo ng konteksto, at pag-generate ng sagot. Ito ay sinadya: bilang isang mapagkukunan sa pag-aaral, mas mahalaga na makita at maunawaan mo ang bawat yugto kaysa paikliin ang code. Kapag komportable ka na sa pag-intindi ng mga bahagi, maaari kang lumipat sa Easy RAG para sa mabilisang prototype o Advanced RAG para sa production systems.

> **💡 Naranasan mo na ba ang Easy RAG?** Ang [Quick Start module](../00-quick-start/README.md) ay naglalaman ng Document Q&A na halimbawa ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) na gumagamit ng Easy RAG approach — awtomatikong inaalagaan ng LangChain4j ang embedding, paghahanap, at pagbuo ng prompt. Dinadala ka ng module na ito sa susunod na hakbang sa pamamagitan ng pagbubukas ng pipeline upang makita at kontrolin mo ang bawat yugto.

Ipinapakita ng diagram sa ibaba ang Easy RAG pipeline mula sa halimbawa ng Quick Start. Pansinin kung paano itinatago ng `AiServices` at `EmbeddingStoreContentRetriever` ang lahat ng komplikasyon — naglo-load ka ng dokumento, nagdadagdag ng retriever, at nakakakuha ng mga sagot. Binubuksan ng Native na paraan sa module na ito ang mga nakatagong hakbang:

<img src="../../../translated_images/tl/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Ipinapakita ng diagram na ito ang Easy RAG pipeline mula sa `SimpleReaderDemo.java`. Ihambing ito sa Native na paraan na ginagamit sa module na ito: Itinatago ng Easy RAG ang embedding, retrieval, at pagbuo ng prompt sa likod ng `AiServices` at `ContentRetriever` — naglo-load ka ng dokumento, nagdadagdag ng retriever, at nakakakuha ng mga sagot. Binubuksan ng Native na paraan sa module na ito ang pipeline kaya ikaw ang tumatawag sa bawat yugto (embed, search, assemble context, generate), na nagbibigay sa iyo ng buong visibility at kontrol.*

## How It Works

Hinahati ng RAG pipeline sa module na ito ang proseso sa apat na yugto na sinusunod sa bawat tanong ng user. Una, ang na-upload na dokumento ay **ini-parse at hinahati** sa mga piraso na madaling hawakan. Ang mga bahagi ay kino-convert naman sa mga **vector embeddings** at iniimbak upang maikumpara ng matematika. Kapag dumating ang isang query, nagsasagawa ang sistema ng **semantic search** upang makita ang pinaka-kaugnay na mga bahagi, at sa wakas ipinapasa ang mga ito bilang konteksto sa LLM para sa **paggawa ng sagot**. Dadalhin tayo ng mga seksyon sa ibaba sa bawat yugto na may aktwal na code at mga diagram. Tingnan natin ang unang hakbang.

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Kapag nag-upload ka ng dokumento, ini-parse ito ng sistema (PDF o plain text), nilalagyan ng metadata tulad ng filename, at hinahati sa mga chunk — mas maliliit na bahagi na kasya sa context window ng modelo. May bahagyang pagkakapareho ang mga chunks para hindi mawala ang konteksto sa mga hangganan.

```java
// I-parse ang na-upload na file at balutin ito sa isang LangChain4j Document
Document document = Document.from(content, metadata);

// Hatiin sa 300-token na mga bahagi na may 30-token na overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Ipinapakita ng diagram sa ibaba kung paano ito gumagana sa biswal. Pansinin kung paano nagbabahagi ang bawat chunk ng ilang token sa mga katabing chunk — ang 30-token overlap ay nagsisiguro na walang mahalagang konteksto ang mawawala sa pagitan:

<img src="../../../translated_images/tl/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Ipinapakita ng diagram na ito ang paghahati ng dokumento sa 300-token chunks na may 30-token overlap, na pinananatili ang konteksto sa hangganan ng mga chunk.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) at itanong:
> - "Paano hinahati ng LangChain4j ang mga dokumento sa chunks at bakit mahalaga ang overlap?"
> - "Ano ang optimal na sukat ng chunk para sa iba’t ibang uri ng dokumento at bakit?"
> - "Paano ko pinoproseso ang mga dokumento na nasa iba’t ibang wika o may espesyal na format?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Ang bawat chunk ay kino-convert sa numerikal na representasyon na tinatawag na embedding — isang kahulugan-sa-numero na tagapag-convert. Hindi “matalino” ang embedding model katulad ng chat model; hindi ito sumusunod ng utos, nagrereason, o sumasagot ng mga tanong. Ang kaya niyang gawin ay i-map ang teksto sa isang matematikal na espasyo kung saan ang mga magkaugnay na kahulugan ay malapit sa isa’t isa — “car” malapit sa “automobile,” “refund policy” malapit sa “return my money.” Isipin ang chat model na isang tao na pwede mong kausapin; ang embedding model ay isang napakagaling na filing system.

Ipinapakita ng diagram sa ibaba ang konseptong ito — pumapasok ang teksto, lumalabas ang mga numerikal na vectors, at ang mga magkatulad na kahulugan ay naglalapit sa mga vectors:

<img src="../../../translated_images/tl/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Ipinapakita ng diagram na ito kung paano kino-convert ng embedding model ang teksto sa mga numerikal na vectors, inilalapit ang mga magkakatulad na kahulugan — tulad ng "car" at "automobile" — sa vector space.*

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

Ipinapakita ng class diagram sa ibaba ang dalawang iba’t ibang daloy sa RAG pipeline at ang mga LangChain4j classes na nagpapatupad nito. Ang **ingestion flow** (isang beses kapag na-upload) ay naghahati ng dokumento, nag-e-embed ng chunks, at iniimbak gamit ang `.addAll()`. Ang **query flow** (bawat tanong ng user) ay nag-e-embed ng tanong, naghahanap sa store gamit ang `.search()`, at ipinapasa ang na-match na konteksto sa chat model. Parehong konektado sa shared `EmbeddingStore<TextSegment>` interface:

<img src="../../../translated_images/tl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Ipinapakita ng diagram na ito ang dalawang daloy sa RAG pipeline — ingestion at query — at kung paano sila nag-uugnay sa shared na EmbeddingStore.*

Kapag naimbak na ang embeddings, natural na nagsasama-sama ang magkaugnay na nilalaman sa vector space. Ipinapakita sa visualization sa ibaba kung paano nagiging malapit sa isa’t isa ang mga dokumento tungkol sa may kaugnay na paksa, na siyang nagpapagana ng semantic search:

<img src="../../../translated_images/tl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Ipinapakita ng visualization na ito kung paano nagsasama-sama ang mga magkaugnay na dokumento sa 3D na vector space, kung saan ang mga paksang tulad ng Technical Docs, Business Rules, at FAQs ay bumubuo ng magkakahiwalay na grupo.*

Kapag naghahanap ang user, sumusunod ang sistema sa apat na hakbang: isang beses na i-embed ang mga dokumento, i-embed ang query sa bawat paghahanap, ikumpara ang query vector sa lahat ng nakaimbak na vectors gamit ang cosine similarity, at ibalik ang top-K na may pinakamataas na iskor na chunks. Pinapakita ng diagram sa ibaba ang bawat hakbang at ang mga LangChain4j classes na sangkot:

<img src="../../../translated_images/tl/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Ipinapakita ng diagram na ito ang apat na hakbang sa embedding search process: embed ng dokumento, embed ng query, ikumpara ang vectors gamit ang cosine similarity, at ibalik ang top-K na resulta.*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Kapag nagtanong ka, ang tanong mo ay ginagawa ring embedding. Ikinumpara ng sistema ang embedding ng tanong mo sa lahat ng embedding ng mga chunk ng dokumento. Hinahanap nito ang mga chunk na may pinakakahalintulad na kahulugan - hindi lang tugma sa keyword kundi tunay na semantic similarity.

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

Ikinukumpara ng diagram sa ibaba ang semantic search at tradisyonal na keyword search. Ang keyword search para sa "vehicle" ay hindi nakakakuha ng chunk tungkol sa "cars and trucks," pero naiuunawa ng semantic search na pareho ang ibig sabihin at ibinabalik ito bilang mataas ang scoring na tugma:

<img src="../../../translated_images/tl/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Ipinapakita ng diagram na ito ang pagkumpara ng keyword-based search at semantic search, na nagpapakita kung paano kinukuha ng semantic search ang mga konseptwal na kaugnay na nilalaman kahit iba ang eksaktong keywords.*
Sa ilalim, sinusukat ang pagkakatulad gamit ang cosine similarity — na parang nagtatanong ng "pareho ba ang direksyon ng dalawang palaso?" Maaaring magkaiba ang mga salita sa dalawang bahagi, pero kung pareho ang ibig sabihin, pareho ring nakaturo ang kanilang vectors at malapit sa 1.0 ang iskor:

<img src="../../../translated_images/tl/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Ipinapakita ng diagram na ito ang cosine similarity bilang anggulo sa pagitan ng embedding vectors — mas magkahanay na vectors ay mas malapit ang iskor sa 1.0, na nagsasaad ng mas mataas na semantic similarity.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) at itanong:
> - "Paano gumagana ang similarity search gamit ang embeddings at ano ang nagtatakda ng iskor?"
> - "Anong similarity threshold ang dapat gamitin at paano ito nakakaapekto sa mga resulta?"
> - "Paano ko haharapin ang mga kaso na walang nahanap na kaugnay na dokumento?"

### Pagbuo ng Sagot

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ang pinakamahalagang mga bahagi ay pinagsasama sa isang istrukturadong prompt na may mga tahasang tagubilin, ang nakuha na konteksto, at ang tanong ng gumagamit. Binabasa ng modelo ang mga espesipikong bahagi na iyon at sumasagot base sa impormasyong iyon — limitado lamang ito sa nakaharap sa kanya, na pumipigil sa hallucination.

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

Ipinapakita ng diagram sa ibaba ang pagbuo na ito sa aksyon — ang mga pinakamataas na iskor na chunk mula sa hakbang ng paghahanap ay inilalagay sa template ng prompt, at ang `OpenAiOfficialChatModel` ay bumubuo ng grounded na sagot:

<img src="../../../translated_images/tl/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Ipinapakita ng diagram na ito kung paano pinagsasama ang mga pinakamataas na iskor na chunk sa isang istrukturadong prompt, na nagpapahintulot sa modelo na bumuo ng grounded na sagot mula sa iyong data.*

## Patakbuhin ang Aplikasyon

**I-verify ang deployment:**

Tiyaking may `.env` file sa root directory na may Azure credentials (na nilikha sa Module 01). Patakbuhin ito mula sa module directory (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na lahat ng aplikasyon gamit ang `./start-all.sh` mula sa root directory (tulad ng inilarawan sa Module 01), tumatakbo na ang module na ito sa port 8081. Maaari mong laktawan ang mga utos sa pagsisimula sa ibaba at pumunta na diretso sa http://localhost:8081.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang Spring Boot icon).

Mula sa Spring Boot Dashboard, maaari mong:
- Tingnan lahat ng available na Spring Boot applications sa workspace
- Simulan/ihinto ang mga aplikasyon sa isang click lang
- Tingnan ang mga log ng aplikasyon nang live
- Subaybayan ang estado ng aplikasyon

Pindutin lang ang play button sa tabi ng "rag" para simulan ang module na ito, o simulan lahat ng modules nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Ipinapakita ng screenshot na ito ang Spring Boot Dashboard sa VS Code, kung saan maaari mong simulan, ihinto, at subaybayan ang mga aplikasyon nang visual.*

**Opsyon 2: Paggamit ng shell scripts**

Simulan lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa root na direktoryo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa root directory
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

Awtomatikong niloload ng parehong script ang mga environment variables mula sa root `.env` file at bubuuin ang mga JAR kung wala pa.

> **Tandaan:** Kung nais mong manu-manong buuin ang lahat ng modules bago simulan:
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
./stop.sh  # Sa module na ito lang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng web interface para sa pag-upload ng dokumento at pagtatanong.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ipinapakita ng screenshot na ito ang interface ng RAG application kung saan nag-a-upload ka ng mga dokumento at nagtatanong.*

### Mag-upload ng Dokumento

Magsimula sa pag-upload ng dokumento - mas mainam ang mga TXT files para sa pagsubok. Mayroong `sample-document.txt` sa direktoryong ito na naglalaman ng impormasyon tungkol sa mga tampok ng LangChain4j, implementasyon ng RAG, at mga best practices - perpekto para sa pagsubok ng sistema.

Pinoproseso ng sistema ang iyong dokumento, hinahati ito sa mga bahagi, at lumilikha ng embeddings para sa bawat bahagi. Nangyayari ito nang awtomatiko kapag nag-upload ka.

### Magtanong

Ngayon magtanong ng espesipikong mga tanong tungkol sa nilalaman ng dokumento. Subukan ang mga fact-based na tanong na malinaw na nakasaad sa dokumento. Hinahanap ng sistema ang mga kaugnay na bahagi, isinama ang mga ito sa prompt, at bumubuo ng sagot.

### Suriin ang Mga Sanggunian

Pansinin na bawat sagot ay may mga sanggunian na may kasamang similarity scores. Ipinapakita ng mga iskor na ito (mula 0 hanggang 1) kung gaano kahalaga ang bawat bahagi sa iyong tanong. Mas mataas na iskor ang ibig sabihin ay mas angkop na tugma. Pinahihintulutan kang suriin ang sagot laban sa pinagmulan.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tl/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ipinapakita ng screenshot na ito ang mga resulta ng query kasama ang binuong sagot, mga sanggunian ng pinagmulan, at mga iskor ng kaugnayan para sa bawat nabuong bahagi.*

### Mag-eksperimento sa mga Tanong

Subukan ang iba't ibang uri ng mga tanong:
- Espesipikong mga katotohanan: "Ano ang pangunahing paksa?"
- Pagkukumpara: "Ano ang pagkakaiba ng X at Y?"
- Mga buod: "Buodin ang mga pangunahing punto tungkol sa Z"

Panoorin kung paano nagbabago ang mga iskor ng kaugnayan batay sa kung gaano kahusay ang pagtutugma ng tanong mo sa nilalaman ng dokumento.

## Mga Pangunahing Konsepto

### Estratehiya sa Pagchunk

Hinahati ang mga dokumento sa 300-token na mga bahagi na may 30 tokens na overlap. Tinitiyak ng balanse na ito na bawat bahagi ay may sapat na konteksto upang maging makahulugan habang nananatiling maliit upang maisama ang maraming bahagi sa isang prompt.

### Mga Iskor ng Pagkakatulad

Bawat nakuha na bahagi ay may iskor ng similarity mula 0 hanggang 1 na nagsasaad kung gaano ito kalapit sa tanong ng gumagamit. Ipinapakita ng diagram sa ibaba ang mga saklaw ng iskor at kung paano ginagamit ng sistema ang mga ito para salain ang mga resulta:

<img src="../../../translated_images/tl/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Ipinapakita ng diagram na ito ang mga saklaw ng iskor mula 0 hanggang 1, na may minimum threshold na 0.5 na nagsasala ng mga hindi kaugnay na bahagi.*

Ang mga iskor ay nasa pagitan ng 0 hanggang 1:
- 0.7-1.0: Lubos na kaugnay, eksaktong tugma
- 0.5-0.7: Kaugnay, magandang konteksto
- Mas mababa sa 0.5: Isinala, masyadong hindi magkatulad

Kinukuha ng sistema ang mga bahagi na lampas sa minimum threshold para matiyak ang kalidad.

Maganda ang embeddings kapag malinaw ang pagklaster ng kahulugan, ngunit may mga blind spots ito. Ipinapakita ng diagram sa ibaba ang mga karaniwang pagkukulang — ang mga sobrang laki na bahagi ay gumagawa ng malabong vectors, ang mga sobrang maliit na bahagi ay kulang sa konteksto, ang mga malabo na termino ay tumuturo sa maraming klaster, at ang eksaktong-match na mga lookup (IDs, part numbers) ay hindi gumagana sa embeddings:

<img src="../../../translated_images/tl/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Ipinapakita ng diagram na ito ang mga karaniwang problema sa embedding: sobrang laki o sobrang maliit na bahagi, malabong termino na tumuturo sa maraming klaster, at eksaktong-match na mga lookup tulad ng mga ID.*

### Imbakan sa Memorya

Gumagamit ang module na ito ng pag-imbak sa memorya para sa pagiging simple. Kapag nirestart mo ang aplikasyon, mawawala ang mga na-upload na dokumento. Sa produksyon, gumagamit ang mga sistema ng persistent vector databases tulad ng Qdrant o Azure AI Search.

### Pamamahala ng Window ng Konteksto

Bawat modelo ay may maximum na context window. Hindi mo maaaring isama lahat ng bahagi mula sa isang malaking dokumento. Kinukuha ng sistema ang nangungunang N pinakakaugnay na mga bahagi (default ay 5) upang manatili sa loob ng limitasyon habang nagbibigay ng sapat na konteksto para sa tamang mga sagot.

## Kailan Mahalaga ang RAG

Hindi palaging angkop ang RAG. Ang gabay sa pagpapasya sa ibaba ay tumutulong sa pagtukoy kung kailan nagbibigay halaga ang RAG kumpara sa mga mas simpleng pamamaraan — tulad ng pagsama ng nilalaman nang direkta sa prompt o pag-asa sa built-in na kaalaman ng modelo:

<img src="../../../translated_images/tl/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Ipinapakita ng diagram na ito ang gabay sa pagpapasya kung kailan nagbibigay halaga ang RAG kumpara sa mga simpler na pamamaraan.*

## Susunod na Hakbang

**Susunod na Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Nakaraan: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Bumalik sa Pangunahing Pahina](../README.md) | [Susunod: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, mangyaring tandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling pag-unawa na bunga ng paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->