# Moduli 03: RAG (Ufumbuzi wa Uzalishaji Unaojumuisha Utafutaji)

## Jedwali la Yaliyomo

- [Muhtasari wa Video](../../../03-rag)
- [Utajifunza Nini](../../../03-rag)
- [Mahitaji ya Awali](../../../03-rag)
- [Kuelewa RAG](../../../03-rag)
  - [Ni Mbinu Gani ya RAG Inayotumika Katika Mafunzo Haya?](../../../03-rag)
- [Jinsi Inavyofanya Kazi](../../../03-rag)
  - [Usindikaji wa Nyaraka](../../../03-rag)
  - [Kutengeneza Embedding](../../../03-rag)
  - [Utafutaji wa Semantic](../../../03-rag)
  - [Uzalishaji wa Majibu](../../../03-rag)
- [Endesha Programu](../../../03-rag)
- [Matumizi ya Programu](../../../03-rag)
  - [Pakia Nyaraka](../../../03-rag)
  - [Uliza Maswali](../../../03-rag)
  - [Kagua Marejeleo ya Chanzo](../../../03-rag)
  - [Jaribu Maswali](../../../03-rag)
- [Mafundisho Muhimu](../../../03-rag)
  - [Mbinu ya Kugawanya Vipande](../../../03-rag)
  - [Alama za Ulinganifu](../../../03-rag)
  - [Uhifadhi wa Kwenye Kumbukumbu](../../../03-rag)
  - [Usimamizi wa Dirisha la Muktadha](../../../03-rag)
- [Wakati RAG Ina Faida](../../../03-rag)
- [Hatua Zifuatazo](../../../03-rag)

## Muhtasari wa Video

Tazama kipindi hiki moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii: [RAG na LangChain4j - Kikao Hai](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Utajifunza Nini

Katika modulii za awali, ulijifunza jinsi ya kufanya mazungumzo na AI na kupanga maelekezo yako kwa ufanisi. Lakini kuna kifungu muhimu: modeli za lugha zinajua tu kile walichojifunza wakati wa mafunzo. Haziwezi kujibu maswali kuhusu sera za kampuni yako, nyaraka za mradi wako, au taarifa nyingine ambazo hazikufunzwa.

RAG (Ufumbuzi wa Uzalishaji Unaojumuisha Utafutaji) hutatua tatizo hili. Badala ya kujaribu kufundisha modeli taarifa zako (ambayo ni gharama kubwa na haiwezekani), unaiwezesha kutafuta ndani ya nyaraka zako. Mtu anapouliza swali, mfumo hupata taarifa zinazohusiana na kuziweka ndani ya maelekezo. Kisha modeli hujibu kulingana na muktadha huo uliopatikana.

Fikiria RAG kama kumpa modeli maktaba ya rejea. Unapouliza swali, mfumo hufanya yafuatayo:

1. **Ugonjwa wa Mtumiaji** - Unouliza swali
2. **Embedding** - Hubadilisha swali lako kuwa vekta
3. **Utafutaji wa Vekta** - Hupata vipande vya nyaraka vinavyofanana
4. **Ukusanyaji wa Muktadha** - Huongeza vipande vinavyohusiana kwenye maelekezo
5. **Majibu** - LLM hutengeneza jibu kulingana na muktadha

Hii huwathibitishia majibu ya modeli kulingana na data yako halisi badala ya kutegemea maarifa ya mafunzo au kubuni majibu.

## Mahitaji ya Awali

- Umekamilisha [Moduli 00 - Anza Haraka](../00-quick-start/README.md) (kwa mfano wa Easy RAG uliotajwa hapo juu)
- Umekamilisha [Moduli 01 - Utangulizi](../01-introduction/README.md) (rasilimali za Azure OpenAI zimewekwa, pamoja na modeli ya `text-embedding-3-small`)
- Faili `.env` kwenye directory kuu yenye vibali vya Azure (imeundwa na `azd up` kwenye Moduli 01)

> **Kumbuka:** Ikiwa hujakamilisha Moduli 01, fuata maelekezo ya usambazaji huko kwanza. Amri ya `azd up` huweka pamoja modeli ya GPT chat na modeli ya embedding inayotumika na moduli hii.

## Kuelewa RAG

Mchoro hapa chini unaonyesha dhana kuu: badala ya kutegemea data ya mafunzo ya modeli peke yake, RAG humpa maktaba ya rejea ya nyaraka zako kushauriana kabla ya kuzalisha jibu kila mara.

<img src="../../../translated_images/sw/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Mchoro huu unaonyesha tofauti kati ya LLM ya kawaida (inayotabiri kutoka data ya mafunzo) na LLM iliyoboreshwa na RAG (inayoshiriki na nyaraka zako kwanza).*

Hivi ndivyo vipande vinavyounganishwa mfululizo. Swali la mtumiaji hupitia hatua nne — embedding, utafutaji wa vekta, ukusanyaji wa muktadha, na uzalishaji wa majibu — kila moja ikijengwa kwa msingi wa iliyopita:

<img src="../../../translated_images/sw/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Mchoro huu unaonyesha mchakato wa RAG mtawazo — swali la mtumiaji linapita kupitia embedding, utafutaji wa vekta, ukusanyaji wa muktadha, na uzalishaji wa majibu.*

Sehemu nyingine za moduli hii zinaelezea kila hatua kwa undani, na msimbo unaoweza kuendesha na kurekebisha.

### Ni Mbinu Gani ya RAG Inayotumika Katika Mafunzo Haya?

LangChain4j hutoa njia tatu za kutekeleza RAG, kila moja kwa kiwango tofauti cha uboreshaji. Mchoro hapa chini unaonyesha ukilinganifu wao kando kando:

<img src="../../../translated_images/sw/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Mchoro huu unaonyesha njia tatu za RAG za LangChain4j — Rahisi (Easy), Asili (Native), na Zaidi (Advanced) — unaonyesha vipengele vyao kuu na wakati wa kutumia kila moja.*

| Mbinu | Kinachofanya | Mbadala |
|---|---|---|
| **Easy RAG** | Hutatua kila kitu moja kwa moja kupitia `AiServices` na `ContentRetriever`. Unabainisha interface, unashinikiza retriever, na LangChain4j hushughulikia embedding, utafutaji, na ukusanyaji wa maelekezo nyuma ya pazia. | Msimbo kidogo, lakini huwezi kuona kinachoendelea kila hatua. |
| **Native RAG** | Unaita modeli ya embedding, unatafuta kwenye duka, unajenga maelekezo, na unazalisha jibu mwenyewe — hatua moja kwa moja kwa wakati. | Msimbo mwingi, lakini kila hatua inaonekana na inaweza kubadilishwa. |
| **Advanced RAG** | Inatumia mfumo wa `RetrievalAugmentor` na transformers, routers, re-rankers, na injectors za maudhui kwa njia za uzalishaji. | Uhuru mkubwa zaidi, lakini ugumu mkubwa. |

**Mafunzo haya yanatumia mbinu ya Native.** Kila hatua ya mchakato wa RAG — kuingiza swali, kutafuta kwenye duka la vekta, kukusanya muktadha, na kuzalisha jibu — imeandikwa wazi katika [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Hii ni kwa makusudi: kama rasilimali ya kujifunza, ni muhimu zaidi uone na kuelewa kila hatua kuliko msimbo kuandikwa kidogo. Ukipoelewa jinsi sehemu zinavyounganishwa, unaweza kuhamia Easy RAG kwa majaribio ya haraka au Advanced RAG kwa mifumo ya uzalishaji.

> **💡 Umeona tayari Easy RAG ikifanya kazi?** Moduli ya [Quick Start](../00-quick-start/README.md) ina mfano wa Maswali & Majibu ya Nyaraka ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) inayotumia njia ya Easy RAG — LangChain4j inashughulikia embedding, utafutaji, na ukusanyaji wa maelekezo moja kwa moja. Moduli hii inafuata hatua ya pili kwa kufungua mchakato huo ili uone na kudhibiti kila hatua mwenyewe.

<img src="../../../translated_images/sw/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Mchoro huu unaonyesha mchakato wa Easy RAG kutoka `SimpleReaderDemo.java`. Linganisha na mbinu ya Native inayotumika hapa: Easy RAG hujificha embedding, upatikanaji, na ukusanyaji wa maelekezo nyuma ya `AiServices` na `ContentRetriever` — unapakia nyaraka, unaambatanisha retriever, na kupata majibu. Mbinu ya Native katika moduli hii inafungua mchakato huo ili uite kila hatua (embed, tafuta, kusanya muktadha, zalishe) mwenyewe, ukikupa muonekano kamili na udhibiti.*

## Jinsi Inavyofanya Kazi

Mchakato wa RAG katika moduli hii unagawanyika katika hatua nne zinazofuatana kila mtu anapouliza swali. Kwanza, nyaraka iliyopakuliwa **inasindika na kugawanywa vipande** vidogo. Vipande hivyo hubadilishwa kuwa **embedding za vekta** na kuhifadhiwa ili kuzilinganisha kihisabati. Wakati swali linapofika, mfumo hufanya **tafsutaji ya semantic** ili kupata vipande muhimu zaidi, na hatimaye hivipitisha kama muktadha kwa LLM kwa ajili ya **uzalishaji wa jibu**. Sehemu hapa chini zinaelezea kila hatua na msimbo halisi na michoro. Hebu tuangalie hatua ya kwanza.

### Usindikaji wa Nyaraka

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Unapopakua nyaraka, mfumo huchambua (PDF au maandishi ya kawaida), huambatisha metadata kama jina la faili, na kisha kuigawanya vipande — vipande vidogo vinavyoweza kushikamana vizuri ndani ya dirisha la muktadha la modeli. Vipande hivyo vina ukikanyo kidogo ili usipoteze muktadha pembezoni mwa kila kipande.

```java
// Changanua faili lililopakuliwa na uifunge ndani ya Hati ya LangChain4j
Document document = Document.from(content, metadata);

// Gawanya katika vipande vyenye tokeni 300 na kukaribiana kwa tokeni 30
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Mchoro hapa chini unaonyesha jinsi hii inavyofanya kazi kimaso. Angalia jinsi vipande vina tokens zinazoashiria na majenzi zao — kukikanya kwa token 30 huhakikisha muktadha muhimu haupotei kati:

<img src="../../../translated_images/sw/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Mchoro huu unaonyesha nyaraka ikigawanywa vipande vya token 300 na kipenezeo cha token 30, kuhifadhi muktadha kang’amuzi wa mipaka.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) na uliza:
> - "Jinsi gani LangChain4j hugawanya nyaraka vipande na kwa nini ukikanyo ni muhimu?"
> - "Ukubwa bora wa kipande kwa aina tofauti za nyaraka ni gani na kwa nini?"
> - "Nashughulikia vipi nyaraka zilizo katika lugha mbalimbali au zenye muundo maalum?"

### Kutengeneza Embedding

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Kipande kila kinaletwa kuwa mwakilishi wa nambari unaoitwa embedding — kimsingi ni kitabadilisha maana kuwa nambari. Modeli ya embedding si "mwerevu" kama modeli ya chat; haiwezi kufuata maelekezo, kuzingatia, au kujibu maswali. Inachofanya ni kutumia maandishi kuingia ndani ya muktadha wa hisabati ambapo maana zinazofanana zinakaribiana — "gari" karibu na "automobile", "sera ya kurudishiwa fedha" karibu na "rudi pesa yangu." Fikiria modeli ya chat kama mtu unaeza kuzungumza naye; modeli ya embedding ni mfumo mzuri sana wa kuainisha habari.

<img src="../../../translated_images/sw/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Mchoro huu unaonyesha jinsi modeli ya embedding hubadilisha maandishi kuwa vekta za nambari, ikiweka maana zinazohusiana — kama "gari" na "automobile" — karibu na kila mmoja katika muktadha wa vekta.*

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

Mchoro wa madarasa hapa chini unaonyesha njia mbili tofauti katika mchakato wa RAG na madarasa ya LangChain4j yanayotekeleza. Mtiririko wa **kuingiza** (unakimbia mara moja wakati wa pakia) hugawanya nyaraka, hufanya embedding za vipande, na kuziweka kupitia `.addAll()`. Mtiririko wa **kuuliza** (unakimbia kila mtu anapouliza) hufanya embedding ya swali, hutafuta duka kupitia `.search()`, na hupitisha muktadha unaofanana kwa modeli ya chat. Mitiririko yote hukutana katika interface ya pamoja `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sw/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Mchoro huu unaonyesha mitiririko miwili katika mchakato wa RAG — ya kuingiza na kuuliza — na jinsi zinavyounganishwa kupitia EmbeddingStore ya pamoja.*

Mara embeddings zikiwekwa, maudhui yanayofanana huwa karibu kihisabati ndani ya muktadha wa vekta. Muonekano hapa chini unaonyesha jinsi nyaraka zinazohusiana huja kama pointi karibu, jambo linalowezesha utafutaji wa semantic:

<img src="../../../translated_images/sw/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Muonekano huu unaonyesha jinsi nyaraka zinazohusiana huungana kando-kando katika muktadha wa vekta wa 3D, na mada kama Nyaraka za Kiufundi, Sheria za Biashara, na Maswali ya Mara kwa Mara zikifanya makundi tofauti.*

Mtu anapofanya utafutaji, mfumo hufuata hatua nne: kufanya embedding ya nyaraka mara moja, kufanya embedding ya swali kila utafutaji, kulinganisha vekta ya swali na vekta zote zilizohifadhiwa kwa kutumia cosine similarity, na kurudisha vipande  vya Juu-K vilivyo na alama za juu zaidi. Mchoro hapa chini unaelezea kila hatua na madarasa ya LangChain4j yanayohusika:

<img src="../../../translated_images/sw/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Mchoro huu unaonyesha hatua nne za utafutaji wa embedding: kuweka embedding nyaraka, embedding swali, kulinganisha vekta kwa cosine similarity, na kurudisha matokeo ya Juu-K.*

### Utafutaji wa Semantic

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Unapouliza swali, swali lako pia hubadilishwa kuwa embedding. Mfumo hulinganisha embedding ya swali lako na embeddings zote za vipande vya nyaraka. Hupata vipande vyenye maana zinazofanana zaidi - si tu maneno yanayolingana lakini maana halisi inayofanana.

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

Mchoro hapa chini unaonyesha tofauti kati ya utafutaji wa semantic na utafutaji wa maneno ya funguo. Utafutaji wa neno funguo "gari" haupati kipande kuhusu "magari na malori," lakini utafutaji wa semantic unaelewa maana sawa na kurudisha kama mechi yenye alama za juu:

<img src="../../../translated_images/sw/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Mchoro huu unaonyesha utafutaji unaotegemea maneno ya funguo ukilinganisha na utafutaji wa semantic, unaonyesha jinsi utafutaji wa semantic unavyopata maudhui yanayohusiana kifikra hata ikiwa maneno halisi ni tofauti.*

Kwa kina, ulinganifu hutumiwa kwa kutumia cosine similarity — kimsingi inajiuliza "je, mishale hii miwili inaelekea upande mmoja?" Vipande viwili vinaweza kutumia maneno tofauti kabisa, lakini kama maana ni ile ile vekta zao zinaelekea upande mmoja na kupata alama karibu na 1.0:

<img src="../../../translated_images/sw/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Mchoro huu unaonyesha cosine similarity kama pembe kati ya vekta za embedding — vekta zilizoendana zaidi hupata alama karibu na 1.0, zikionyesha ulinganifu mkubwa wa semantic.*
> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) na uliza:
> - "Utafutaji wa ufananishi hufanya kazi vipi na embeddings na ni nini huchangia alama?"
> - "Kipimo gani cha ufananishi ni bora kutumia na kinaathirije matokeo?"
> - "Ninawezaje kushughulikia hali ambapo hakuna nyaraka muhimu zilizopatikana?"

### Uundaji wa Majibu

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Vipande vinavyoendana zaidi vinakusanywa katika ombi lililo na muundo linalojumuisha maelekezo wazi, muktadha uliopatikana, na swali la mtumiaji. Mfano husoma vipande hivyo maalum na kujibu kwa msingi wa taarifa hiyo — unaweza kutumia tu kinacho mbele yake, ambayo huzuia udanganyifu.

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

Mchoro hapa chini unaonyesha mkusanyiko huu ukiwa unaendeshwa — vipande vyenye alama za juu kutoka hatua ya utafutaji huingizwa kwenye kiolezo cha ombi, na `OpenAiOfficialChatModel` hutengeneza jibu lililo na msingi:

<img src="../../../translated_images/sw/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Mchoro huu unaonyesha jinsi vipande vyenye alama za juu vinavyokusanywa katika ombi lenye muundo, kuruhusu mfano kutengeneza jibu lenye msingi kutoka kwa data yako.*

## Endesha Programu

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo katika saraka ya mzizi na taarifa za Azure (zilizotengenezwa wakati wa Moduli 01):

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anza programu:**

> **Kumbuka:** Ikiwa tayari umeanza programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inafanya kazi kwenye bandari 8081. Unaweza kuruka amri za kuanza hapa chini na kwenda moja kwa moja http://localhost:8081.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kontena la maendeleo lina upanuzi wa Spring Boot Dashboard, unaotoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Barra ya Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana kwenye eneo la kazi
- Anza/acha programu kwa bonyeza mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fatilia hali ya programu

Bofya kitufe cha kucheza kando ya "rag" kuanza moduli hii, au anzisha moduli zote kwa wakati mmoja.

<img src="../../../translated_images/sw/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Picha ya skrini hii inaonyesha Spring Boot Dashboard katika VS Code, ambapo unaweza kuanza, kuacha, na kufuatilia programu kwa njia ya kuona.*

**Chaguo 2: Kutumia skiripti za shell**

Anza programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anza moduli hii pekee:

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

Skiripti zote mbili hujichomekea moja kwa moja vigezo vya mazingira kutoka faili la `.env` la mzizi na zitajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mikono kabla ya kuanza:
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

Fungua http://localhost:8081 kwenye kivinjari chako.

**Kuacha:**

**Bash:**
```bash
./stop.sh  # Hii moduli tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Moduli hii tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Kutumia Programu

Programu hutoa kiolesura cha wavuti kwa kupakia nyaraka na kuuliza maswali.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sw/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Picha ya skrini hii inaonyesha kiolesura cha programu ya RAG ambapo unapakiza nyaraka na kuuliza maswali.*

### Pakia Nyaraka

Anza kwa kupakia nyaraka - faili za TXT ni bora kwa majaribio. `sample-document.txt` imetolewa katika saraka hii ambayo ina taarifa kuhusu vipengele vya LangChain4j, utekelezaji wa RAG, na mbinu bora - bora kwa kujaribu mfumo.

Mfumo huandaa nyaraka zako, kuzivunja vipande, na kuunda embeddings kwa kila kipande. Hii hufanyika kiotomatiki unapopakua.

### Uliza Maswali

Sasa uliza maswali maalum kuhusu yaliyomo kwenye nyaraka. Jaribu kitu cha kikadirio kilicho wazi kwenye nyaraka. Mfumo huita vipande vinavyohusiana, hujumuisha kwenye ombi, na hutengeneza jibu.

### Angalia Marejeleo ya Chanzo

Tambua kila jibu linajumuisha marejeleo ya chanzo na alama za ufananishi. Alama hizi (0 hadi 1) zinaonyesha jinsi kila kipande kilivyohusiana na swali lako. Alama za juu zina maana za muafaka mzuri. Hii inakuwezesha kuthibitisha jibu dhidi ya nyaraka za chanzo.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sw/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Picha ya skrini hii inaonyesha matokeo ya maswali pamoja na jibu lililotengenezwa, marejeleo ya chanzo, na alama za ufananishi kwa kila kipande kilichopatikana.*

### Jaribu Maswali Tofauti

Jaribu aina tofauti za maswali:
- Ukweli maalum: "Mada kuu ni ipi?"
- Mlinganisho: "Tofauti kati ya X na Y ni gani?"
- Muhtasari: "Fupisha vidokezo muhimu kuhusu Z"

Tazama jinsi alama za ufananishi zinavyobadilika kulingana na jinsi swali lako linaendana na yaliyomo kwenye nyaraka.

## Misingi Muhimu

### Mkakati wa Kugawanya Vipande

Nyaraka hugawanywa katika vipande vya tokeni 300 na mkusanyiko wa tokeni 30. Mlinganisho huu huhakikisha kila kipande kina muktadha wa kutosha kuwa na maana huku kikibaki kidogo kabisa ili kuweza kujumuisha vipande vingi katika ombi.

### Alama za Ufananishi

Kila kipande kinachopatikana huambatana na alama ya ufananishi kati ya 0 na 1 inayoonyesha jinsi kinavyolingana na swali la mtumiaji. Mchoro hapa chini unaonyesha anuwai ya alama na jinsi mfumo unavyozitumia kuchuja matokeo:

<img src="../../../translated_images/sw/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Mchoro huu unaonyesha anuwai za alama kutoka 0 hadi 1, na kiwango cha chini cha 0.5 kinachochuja vipande visivyo husika.*

Alama zinatoka 0 hadi 1:
- 0.7-1.0: Muhimu sana, muafaka halisi
- 0.5-0.7: Muhimu, muktadha mzuri
- Chini ya 0.5: Kuchujwa, haijafanana

Mfumo hupata tu vipande vinavyozidi kiwango cha chini ili kuhakikisha ubora.

Embeddings hufanya kazi vizuri wakati maana zinakusanyika vizuri, lakini zina makosa katika maeneo fulani. Mchoro hapa chini unaonyesha njia zinazowekwa makosa mara nyingi — vipande vikubwa sana hutoa vekta zisizo wazi, vipande vidogo sana havina muktadha, maneno yenye maana mbili huashiria makundi mengi, na utafutaji wa muafaka halisi (IDs, nambari za sehemu) haifanyi kazi na embeddings kabisa:

<img src="../../../translated_images/sw/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Mchoro huu unaonyesha njia za makosa ya kawaida za embedding: vipande vikubwa sana, vipande vidogo sana, maneno yenye maana mbili yanayoashiria makundi mengi, na utafutaji wa muafaka halisi kama IDs.*

### Uhifadhi wa Ndani ya Kumbukumbu

Moduli hii hutumia uhifadhi wa ndani ya kumbukumbu ili kurahisisha. Unaporudia kuanzisha programu, nyaraka zilizopakuliwa hupotea. Mifumo ya uzalishaji hutumia hifadhidata za vekta zinazoendelea kama Qdrant au Azure AI Search.

### Usimamizi wa Dirisha la Muktadha

Kila mfano una dirisha kubwa la muktadha. Huwezi kujumuisha kila kipande kutoka kwenye nyaraka kubwa. Mfumo huchukua vipande N vilivyo muhimu zaidi (kawaida 5) ili kubaki ndani ya mipaka huku ukitoa muktadha wa kutosha kwa majibu sahihi.

## Wakati RAG Inapofaa

RAG si kila mara ndio njia sahihi. Mwongozo wa maamuzi hapa chini unakusaidia kubaini ni lini RAG hutoa thamani versus njia rahisi - kama kujumuisha maudhui moja kwa moja kwenye ombi au kutegemea maarifa yaliyojengwa ndani ya mfano - zinatosha:

<img src="../../../translated_images/sw/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Mchoro huu unaonyesha mwongozo wa maamuzi wa ni lini RAG hutoa thamani versus ni lini njia rahisi zinatosha.*

**Tumia RAG wakati:**
- Kujibu maswali kuhusu nyaraka miliki
- Taarifa hubadilika mara kwa mara (sera, bei, maalum)
- Usahihi unahitaji marejeleo ya chanzo
- Maudhui ni makubwa mno kuweza kuwekwa kwenye ombi moja
- Unahitaji majibu yanayothibitishwa, yenye msingi

**Usitumie RAG wakati:**
- Maswali yanahitaji maarifa ya jumla ambayo mfano tayari ana
- Data ya wakati halisi inahitajika (RAG hufanya kazi kwenye nyaraka zilizopakuliwa)
- Maudhui ni madogo vya kutosha kujumuishwa moja kwa moja katika ombi

## Hatua Zifuatazo

**Moduli Ifuatayo:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Uelekezaji:** [← Iliyotangulia: Moduli 02 - Ufundi wa OMBI](../02-prompt-engineering/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 04 - Zana →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kukana**:  
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Nyaraka ya awali katika lugha yake asilia ndiyo chanzo kinachotegemewa. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na watu inashauriwa. Hatutawajibika kwa kutoelewana au tafsiri potofu itakayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->