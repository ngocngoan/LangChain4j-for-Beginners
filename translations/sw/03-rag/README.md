# Moduli 03: RAG (Uundaji ulioboreshwa kwa Utafutaji)

## Orodha ya Yaliyomo

- [Utangazaji wa Video](../../../03-rag)
- [UTa Jifunze](../../../03-rag)
- [Mahitaji ya Awali](../../../03-rag)
- [Kuelewa RAG](../../../03-rag)
  - [Ni Njia Gani ya RAG Inayotumiwa Katika Mafunzo Haya?](../../../03-rag)
- [Inavyofanya Kazi](../../../03-rag)
  - [Uchakataji wa Nyaraka](../../../03-rag)
  - [Kutengeneza Mwingiliano](../../../03-rag)
  - [Utafutaji wa Kimaana](../../../03-rag)
  - [Uundaji wa Jibu](../../../03-rag)
- [Endesha Programu](../../../03-rag)
- [Kutumia Programu](../../../03-rag)
  - [Pakia Nyaraka](../../../03-rag)
  - [Uliza Maswali](../../../03-rag)
  - [Angalia Marejeleo ya Chanzo](../../../03-rag)
  - [Jaribu Maswali](../../../03-rag)
- [Mafundisho Muhimu](../../../03-rag)
  - [Mkakati wa Kugawanya Sehemu](../../../03-rag)
  - [Alama za Mfanano](../../../03-rag)
  - [Hifadhi Ndani ya Kumbukumbu](../../../03-rag)
  - [Usimamizi wa Dirisha la Muktadha](../../../03-rag)
- [Wakati RAG Inapokuwa Muhimu](../../../03-rag)
- [Hatua Inayofuata](../../../03-rag)

## Utangazaji wa Video

Tazama kipindi hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG na LangChain4j - Kipindi cha Moja kwa Moja" width="800"/></a>

## Uta Jifunze

Katika moduli zilizopita, ulijifunza jinsi ya kuzungumza na AI na kupanga maelekezo yako kwa ufanisi. Lakini kuna kizuizi cha msingi: mifano ya lugha inajua tu kile walichojifunza wakati wa mafunzo. Hawaiwezi kujibu maswali kuhusu sera za kampuni yako, nyaraka za mradi wako, au taarifa yoyote ambayo hawakufunzwa.

RAG (Uundaji ulioboreshwa kwa Utafutaji) hutatua tatizo hili. Badala ya kujaribu kufundisha mfano taarifa zako (ambayo ni ghali na si ya vitendo), unamruhusu kuweza kutafuta kupitia nyaraka zako. Wakati mtu anauliza swali, mfumo hupata taarifa zinazohusiana na kuziweka kwenye maelekezo. Mfano hupatia jibu kulingana na muktadha uliopatikana.

Fikiria RAG kama kumpa mfano maktaba ya marejeleo. Wakati unapouliza swali, mfumo:

1. **Swali la Mtumiaji** - Unauliza swali
2. **Uundaji wa Mwingiliano** - Hubadilisha swali lako kuwa vekta
3. **Utafutaji wa Vekta** - Hupata vipande vya nyaraka vinavyofanana
4. **Ukusanyaji wa Muktadha** - Huongeza vipande vinavyohusiana kwenye maelekezo
5. **Jibu** - LLM hutengeneza jibu kulingana na muktadha

Hii huweka majibu ya mfano katika data halisi zako badala ya kutegemea alama za mafunzo au kugundua majibu.

## Mahitaji ya Awali

- Umekamilisha [Moduli 00 - Anza Haraka](../00-quick-start/README.md) (kwa mfano rahisi wa RAG ulioelezwa hapo juu)
- Umekamilisha [Moduli 01 - Utangulizi](../01-introduction/README.md) (rasilimali za Azure OpenAI zilizoanzishwa, pamoja na mfano wa uundaji wa `text-embedding-3-small`)
- Faili `.env` iliyoko kwenye saraka ya mzizi yenye taarifa za Azure (iliyoundwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujakamilisha Moduli 01, fuata maelekezo ya usambazaji hapo kwanza. Amri ya `azd up` hueneza mfano wa mazungumzo wa GPT na mfano wa uundaji wa uingizaji unaotumika katika moduli hii.

## Kuelewa RAG

Mchoro hapa chini unaonyesha dhana kuu: badala ya kutegemea data ya mafunzo ya mfano peke yake, RAG humpa maktaba ya marejeleo ya nyaraka zako za kushauri kabla ya kuandaa kila jibu.

<img src="../../../translated_images/sw/what-is-rag.1f9005d44b07f2d8.webp" alt="Nini RAG" width="800"/>

*Mchoro huu unaonyesha tofauti kati ya LLM ya kawaida (inayotabiri kutoka data ya mafunzo) na LLM iliyoboreshwa na RAG (inayoshauri nyaraka zako kwanza).*

Hapa ni jinsi sehemu zinavyounganishwa mwishowe. Swali la mtumiaji hupitia hatua nne — uundaji wa mwingiliano, utafutaji wa vekta, ukusanyaji wa muktadha, na uundaji wa jibu — kila moja ikijenga juu ya lile lililotangulia:

<img src="../../../translated_images/sw/rag-architecture.ccb53b71a6ce407f.webp" alt="Marekebisho ya RAG" width="800"/>

*Mchoro huu unaonyesha bomba la RAG kuanzia mwanzo hadi mwisho — swali la mtumiaji hupitia uundaji, utafutaji wa vekta, ukusanyaji wa muktadha, na uundaji wa jibu.*

Sehemu iliyobaki ya moduli hii inafanyia kazi kila hatua kwa undani, ukiambatana na msimbo unaoweza kuendesha na kurekebisha.

### Ni Njia Gani ya RAG Inayotumiwa Katika Mafunzo Haya?

LangChain4j hutoa njia tatu za kutekeleza RAG, kila moja ikiwa na kiwango tofauti cha ubunifu. Mchoro hapa chini unaonyesha kulinganisha yao upande kwa upande:

<img src="../../../translated_images/sw/rag-approaches.5b97fdcc626f1447.webp" alt="Njia Tatu za RAG katika LangChain4j" width="800"/>

*Mchoro huu unaonyesha njia tatu za RAG za LangChain4j — Rahisi, Asili, na Zaidi — zikionyesha vipengele muhimu na wakati wa kutumia kila moja.*

| Njia | Inafanya Nini | Hasara |
|---|---|---|
| **RAG Rahisi** | Huunganisha kila kitu moja kwa moja kupitia `AiServices` na `ContentRetriever`. Unatoa maelezo ya interface, unaongeza kivutio, na LangChain4j hushughulikia uundaji, utafutaji, na ukusanyaji wa maelekezo nyuma ya pazia. | Msimbo mdogo, lakini huoni kinachofanyika kila hatua. |
| **RAG Asili** | Unaita mfano wa uundaji, unatafuta kwenye hifadhi, unajenga maelekezo, na unatengeneza jibu mwenyewe — kila hatua kwa wazi. | Msimbo mwingi, lakini kila hatua inaonekana na inaweza kubadilishwa. |
| **RAG Zaidi** | Inatumia mfumo wa `RetrievalAugmentor` wenye mabadiliko ya kuuliza, marutoa, washauri upya, na waingiza maudhui kwa mstari wa uzalishaji. | Uwezo mkubwa wa kubadilika, lakini inakuwa na yawa mgumu zaidi. |

**Mafunzo haya yanatumia Njia ya Asili.** Kila hatua ya bomba la RAG — uundaji wa mwingiliano wa swali, utafutaji kwenye hifadhi ya vekta, ukusanyaji wa muktadha, na uundaji wa jibu — imeandikwa wazi katika [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Hii ni makusudi: kama chanzo cha kujifunza, ni muhimu zaidi kuona na kuelewa kila hatua kuliko kupunguza msimbo. Ukijisikiona unafahamu vizuri jinsi sehemu zinavyounganishwa, unaweza kwenda kwa RAG Rahisi kwa prototipu za haraka au RAG Zaidi kwa mifumo ya uzalishaji.

> **💡 Umeshumia RAG Rahisi?** Moduli ya [Anza Haraka](../00-quick-start/README.md) ina mfano wa Maswali na Majibu kwenye Nyaraka ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) unaotumia njia ya RAG Rahisi — LangChain4j hushughulikia uundaji, utafutaji, na ukusanyaji kwa njia ya moja kwa moja. Moduli hii inaongeza kwa kufungua bomba hilo ili uweze kuona na kudhibiti kila hatua mwenyewe.

<img src="../../../translated_images/sw/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Bomba la RAG Rahisi - LangChain4j" width="800"/>

*Mchoro huu unaonyesha bomba la RAG Rahisi kutoka `SimpleReaderDemo.java`. Linganisha na njia ya Asili inayotumika katika moduli hii: RAG Rahisi huficha uundaji, utafutaji, na ukusanyaji wa maelekezo nyuma ya `AiServices` na `ContentRetriever` — unapakia nyaraka, unaongeza kivutio, na unapata majibu. Njia ya Asili katika moduli hii inaungua bomba hili ili uita kila hatua (undaji, utafutaji, ukusanyaji wa muktadha, uundaji) mwenyewe, ikikupa muonekano kamili na udhibiti.*

## Inavyofanya Kazi

Bomba la RAG katika moduli hii hugawanyika katika hatua nne zinazofanyika kwa mfululizo kila mtu anapouliza swali. Kwanza, nyaraka zilizopakuliwa **hutambuliwa na kugawanywa** katika vipande vinavyoweza kudhibitiwa. Vipande hivyo hubadilishwa kuwa **mwitikio wa vekta** na kuhifadhiwa ili ziweze kulinganishwa kihisabati. Wakati swali linapofika, mfumo hufanya **utafutaji wa kimaana** kupata vipande vinavyohusiana zaidi, na hatimaye hupitisha vipande hivyo kama muktadha kwa LLM kwa **uundaji wa jibu**. Sehemu zifuatazo zinatembelea kila hatua zikifuatiwa na msimbo halisi na michoro. Tuchunguze hatua ya kwanza.

### Uchakataji wa Nyaraka

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Unapopakua nyaraka, mfumo huichambua (PDF au maandishi ya kawaida), huongeza meta data kama jina la faili, na kisha huigawanya katika vipande — vipande vidogo vinavyopitwa vizuri na dirisha la muktadha la mfano. Vipande hivyo hubeba sehemu ndogo ya kuungana ili usipoteze muktadha kwenye mipaka.

```java
// Tafsiri faili lililopakiwa na libalilie katika Hati ya LangChain4j
Document document = Document.from(content, metadata);

// Gawanya kuwa vipande vya tokeni 300 na kuingiliana tokeni 30
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Mchoro hapa chini unaonyesha jinsi hii inavyofanya kazi kwa kuona. Angalia jinsi kila kipande kinashirikiana baadhi ya tokeni na maziwa yanayowazunguka — kuungana kwa tokeni 30 kunahakikisha hakuna muktadha muhimu unaopotea kati ya mwanya:

<img src="../../../translated_images/sw/document-chunking.a5df1dd1383431ed.webp" alt="Mgawanyo wa Nyaraka" width="800"/>

*Mchoro huu unaonyesha nyaraka ikigawanywa katika vipande vya tokeni 300 na kuungana kwa tokeni 30, ikihifadhi muktadha katika mipaka ya kipande.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) na uliza:
> - "LangChain4j hugawanya vipi nyaraka katika vipande na kwa nini kuungana ni muhimu?"
> - "Ni ukubwa gani bora wa kipande kwa aina tofauti za nyaraka na kwa nini?"
> - "Nashughulikiaje nyaraka zilizo katika lugha nyingi au zenye muundo maalum?"

### Kutengeneza Mwingiliano

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Kipande kila kitatengenezwa hadi kuwekwa katika mwelekeo wa nambari unaoitwa mwitikio wa mwingiliano — kimsingi ni mtafsiri wa maana kwenda kwenye nambari. Mfano wa uundaji hauwezi kuwa "mwerevu" kama mfano wa mazungumzo; hawezi kufuata maelekezo, kufikiria, au kujibu maswali. Kinachoweza kufanya ni kuweka maandishi katika anga ya kihesabu ambapo maana zinazofanana zinaishia karibu — "gari" karibu na "automobile," "sera ya kurejesha fedha" karibu na "rudisha pesa yangu." Fikiria mfano wa mazungumzo kama mtu unayemweleza; mfano wa uundaji ni mfumo bora wa kuweka faili.

<img src="../../../translated_images/sw/embedding-model-concept.90760790c336a705.webp" alt="Dhana ya Mfano wa Mwingiliano" width="800"/>

*Mchoro huu unaonyesha jinsi mfano wa uundaji wa mwingiliano unavyobadilisha maandishi kuwa vekta za nambari, ukiweka maana zinazofanana — kama "gari" na "automobile" — karibu katika anga la vekta.*

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

Mchoro wa darasa hapa chini unaonyesha mizunguko miwili tofauti katika bomba la RAG na madarasa ya LangChain4j yanayotekeleza. Mtiririko wa **kuchukua** (unafanyika mara moja wakati wa kupakua) hugawanya nyaraka, hugawa vipande, na kuhifadhi kupitia `.addAll()`. Mtiririko wa **kuuliza** (unahaririwa kila mtu anapouliza) hufanya mwingiliano wa swali, hutafuta hifadhi kupitia `.search()`, na hupilisha muktadha uliolingana kwa mfano wa mazungumzo. Mizunguko yote hukutana kwenye interface ya pamoja `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/sw/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Madarasa ya LangChain4j RAG" width="800"/>

*Mchoro huu unaonyesha mizunguko miwili katika bomba la RAG — kuchukua na kuuliza — na jinsi zinavyounganishwa kupitia EmbeddingStore ya pamoja.*

Mara mwitikio ulohifadhiwa, maudhui yanayofanana huunganishwa pamoja kwa asili ndani ya anga la vekta. Uonyeshaji hapa chini unaonyesha jinsi nyaraka kuhusu mada zinazohusiana zinavyokaa pamoja katika muktadha wa vekta, jambo linalowezesha utafutaji wa kimaana:

<img src="../../../translated_images/sw/vector-embeddings.2ef7bdddac79a327.webp" alt="Anga la Mwingiliano wa Vekta" width="800"/>

*Uonyeshaji huu unaonyesha jinsi nyaraka zinazohusiana zinavyokusanyika pamoja katika anga la vekta la 3D, na mada kama Nyaraka za Kiufundi, Sheria za Biashara, na Maswali yanayoulizwa Mara kwa Mara kuunda makundi tofauti.*

Wakati mtumiaji anatafuta, mfumo hufuata hatua nne: kuwaweka vekta wa nyaraka mara moja, kuunda mwitikio wa swali kila kutafuta, kulinganisha vekta ya swali dhidi ya vekta zilizohifadhiwa kwa kutumia mfanano wa konusin, na kurudisha vipande vinavyoongoza kulingana na alama za juu (top-K). Mchoro hapa chini unafuatilia kila hatua na madarasa ya LangChain4j yanayohusika:

<img src="../../../translated_images/sw/embedding-search-steps.f54c907b3c5b4332.webp" alt="Hatua za Utafutaji wa Mwingiliano" width="800"/>

*Mchoro huu unaonyesha mchakato wa hatua nne wa utafutaji wa mwitikio: kuwawekea nyaraka, kuwawekea swali, kulinganisha vekta kwa mfanano wa konusin, na kurudisha matokeo top-K.*

### Utafutaji wa Kimaana

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Unapouliza swali, swali lako pia hutengenezwa kuwa mwitikio. Mfumo hulinganisha mwitikio wa swali lako dhidi ya mwitikio wa vipande vyote vya nyaraka. Hupata vipande vyenye maana zinazofanana zaidi - si maneno yanayolingana tu, bali mfanano halisi wa maana.

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

Mchoro hapa chini unaonyesha tofauti kati ya utafutaji wa maneno na utafutaji wa kimaana. Utafutaji wa maneno kwa neno "gari" hukosa kipande kuhusu "magari na malori," lakini utafutaji wa kimaana unafahamu maana ni sawa na kurudisha kama mechi yenye alama za juu:

<img src="../../../translated_images/sw/semantic-search.6b790f21c86b849d.webp" alt="Utafutaji wa Kimaana" width="800"/>

*Mchoro huu unaonyesha tofauti kati ya utafutaji kwa maneno na utafutaji wa kimaana, unaonyesha jinsi utafutaji wa kimaana unavyopata maudhui yanayohusiana kifikra hata kama maneno halisi yatofautiana.*

Chini ya pazia, mfanano hupimwa kwa kutumia mfanano wa konusin — kimsingi unauliza "je, mishale hii miwili inaelekea katika mwelekeo mmoja?" Vipande viwili vinaweza kutumia maneno tofauti kabisa, lakini ikiwa maana zao ni sawa vekta zao zinaelekea upande mmoja na kupata alama karibu na 1.0:

<img src="../../../translated_images/sw/cosine-similarity.9baeaf3fc3336abb.webp" alt="Mfanano wa Konusin" width="800"/>
*Michoro hii inaonyesha ufananishi wa kosaini kama pembe kati ya vekta za uingizaji — vekta zilizo na mwelekeo sawa zaidi hupata alama karibu na 1.0, ikionyesha ufananishi wa maana wa juu zaidi.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) na uliza:
> - "Utafutaji wa ufananishi unavyofanya kazi na uingizaji na ni nini huhesabu alama?"
> - "Kipimo gani cha ufananishi ninapaswa kutumia na kinavyoathiri matokeo?"
> - "Ninawezaje kushughulikia hali ambapo hakuna nyaraka zinazofaa zilizopatikana?"

### Uandishi wa Majibu

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Vikundi vinavyohusiana zaidi vinaunganishwa kuwa maelekezo yaliyopangwa pamoja yanayojumuisha maelekezo wazi, muktadha uliochukuliwa, na swali la mtumiaji. Mfano husoma vikundi hivi maalum na kujibu kwa kutumia taarifa hiyo — inaweza kutumia tu kile kilicho mbele yake, kinachoepuka kudanganywa.

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

Michoro ifuatayo inaonyesha mchakato huu ukiendelea — vikundi vyenye alama za juu kutoka hatua ya utafutaji vinaingizwa kwenye kiolezo cha maelekezo, na `OpenAiOfficialChatModel` huunda jibu lililogusa ardhini:

<img src="../../../translated_images/sw/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Michoro hii inaonyesha jinsi vikundi vinavyopata alama za juu vinavyounganishwa kuwa maelekezo yaliyopangwa, kuruhusu mfano kutoa jibu lililogusa ardhini kutoka kwa data yako.*

## Endesha Programu

**Hakikisha utoaji kazi:**

Hakikisha faili `.env` ipo kwenye saraka kuu na maelezo ya Azure (iliyoundwa wakati wa Moduli 01):

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendesha kwenye lango la 8081. Unaweza kuruka amri za kuanza hapa chini na kwenda moja kwa moja http://localhost:8081.

**Chaguo la 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kontena la maendeleo linaongeza upanuzi wa Spring Boot Dashboard, unaotoa kiolesura cha kuona na kusimamia programu zote za Spring Boot. Unaweza kuliona kwenye Kipengele cha Shughuli upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zinazopatikana kwenye eneo la kazi
- Anzisha/acha programu kwa bonyeza mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fuata hali ya programu

Bofya kitufe cha kuchezwa kando ya "rag" kuanza moduli hii, au anza moduli zote mara moja.

<img src="../../../translated_images/sw/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Picha ya skrini hii inaonyesha Spring Boot Dashboard ndani ya VS Code, ambapo unaweza kuanza, kuacha, na kufuatilia programu kwa njia ya kuona.*

**Chaguo la 2: Kutumia shell scripts**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anzisha moduli hii pekee:

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

Mabano yote hulakia moja kwa moja vigezo vya mazingira kutoka kwa faili `.env` ya saraka kuu na yatajenga JAR ikiwa hayapo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanzisha:
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

Fungua http://localhost:8081 katika kivinjari chako.

**Kuwaacha:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Huu moduli peke yake
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Kutumia Programu

Programu hutoa kiolesura cha wavuti kwa ajili ya kupakia nyaraka na kuuliza maswali.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sw/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Picha hii inaonyesha kiolesura cha programu ya RAG ambapo unapakiza nyaraka na kuuliza maswali.*

### Pakia Nyaraka

Anza kwa kupakia nyaraka - faili za TXT ndizo bora kwa majaribio. Faili `sample-document.txt` imewekwa kwenye saraka hii inayojumuisha taarifa kuhusu sifa za LangChain4j, utekelezaji wa RAG, na mbinu bora - bora kwa majaribio ya mfumo.

Mfumo unashughulikia nyaraka zako, kuzigawanya katika sehemu ndogo, na kuunda uingizaji kwa kila sehemu. Hii hufanyika moja kwa moja unapopakua.

### Uliza Maswali

Sasa uliza maswali maalum kuhusu maudhui ya nyaraka. Jaribu kitu cha ukweli kilicho wazi katika nyaraka. Mfumo hufanya utafutaji kwa sehemu zinazofaa, huziingiza katika maelekezo, na hutengeneza jibu.

### Angalia Marejeleo ya Chanzo

Angalia jibu kila linajumuisha marejeleo ya chanzo na alama za ufananishi. Alama hizi (kutoka 0 hadi 1) zinaonyesha ni vyema kiasi gani kila sehemu ilihusiana na swali lako. Alama za juu zina maana ya mechi bora. Hii inakuwezesha kuthibitisha jibu dhidi ya chanzo cha taarifa.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sw/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Picha hii inaonyesha matokeo ya utafutaji yenye jibu lililotengenezwa, marejeleo ya chanzo, na alama za umuhimu kwa kila sehemu iliyopatikana.*

### Jaribu Maswali Mbalimbali

Jaribu aina tofauti za maswali:
- Ukweli maalum: "Mada kuu ni nini?"
- Mlinganyo: "Tofauti kati ya X na Y ni nini?"
- Muhtasari: "Fupisha mambo muhimu kuhusu Z"

Tazama jinsi alama za umuhimu zinavyobadilika kulingana na jinsi swali lako linavyolingana na maudhui ya nyaraka.

## Dhana Muhimu

### Mikakati ya Kugawanya Sehemu

Nyaraka hugawanywa katika sehemu zilizo na alama 300 na mgongano wa alama 30. Usawa huu unahakikisha kila sehemu ina muktadha wa kutosha kuwa na maana huku ikiendelea kuwa ndogo vya kutosha kuruhusu sehemu nyingi kuingizwa kwenye maelekezo.

### Alama za Ufananishi

Kila sehemu inayopatikana huambatana na alama ya ufananishi kati ya 0 na 1 inayoonyesha jinsi ilivyo karibu na swali la mtumiaji. Michoro ifuatayo inaonyesha viwango vya alama na jinsi mfumo unavyovitumia kuweka sifa za matokeo:

<img src="../../../translated_images/sw/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Michoro hii inaonyesha viwango vya alama kutoka 0 hadi 1, pamoja na kiwango cha chini cha 0.5 kinachoseleza sehemu zisizo husika.*

Alama zinaanzia 0 hadi 1:
- 0.7-1.0: Muhimu sana, mechi kamili
- 0.5-0.7: Muhimu, muktadha mzuri
- Chini ya 0.5: Zimetolewa, mfanano ni mdogo sana

Mfumo hupata sehemu tu zilizo juu ya kiwango cha chini kuhakikisha ubora.

Uingizaji hufanya kazi vizuri wakati maana inajumuika kwa usafi, lakini kuna mapungufu. Michoro ifuatayo inaonyesha aina za makosa ya kawaida — sehemu kubwa sana hutengeneza vekta zisizo wazi, sehemu ndogo sana hazina muktadha, maneno yenye maana mbili huashiria makundi mengi, na utafutaji wa mechi kamili (IDs, nambari za sehemu) hauendani kabisa na uingizaji:

<img src="../../../translated_images/sw/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Michoro hii inaonyesha hali za kawaida za kugonga mwamba kwa uingizaji: sehemu kubwa sana, sehemu ndogo sana, maneno yenye maana mbili yanayoashiria makundi mengi, na utafutaji wa mechi kamili kama IDs.*

### Uhifadhi wa Kumbukumbu

Moduli hii inatumia uhifadhi wa kumbukumbu kwa urahisi. Unapoanzisha programu tena, nyaraka zilizo pakizwa hupotea. Mifumo ya uzalishaji hutumia hifadhidata za vekta zenye kudumu kama Qdrant au Azure AI Search.

### Usimamizi wa Dirisha la Muktadha

Kila mfano una kipimo cha juu cha dirisha la muktadha. Huwezi kuingiza kila sehemu kutoka kwa nyaraka kubwa. Mfumo hukusanya sehemu N zilizo na umuhimu zaidi (chaguo-msingi 5) ili kubaki ndani ya mipaka huku ukitoa muktadha wa kutosha kwa majibu sahihi.

## Wakati RAG Inafaa

RAG si kila mara ndiyo njia sahihi. Mwongozo wa maamuzi hapa chini unakusaidia kubaini wakati RAG inaleta thamani dhidi ya njia rahisi — kama kuingiza maudhui moja kwa moja kwenye maelekezo au kuamini maarifa yaliyojengwa ndani ya mfano — kufikia matokeo yanayofaa:

<img src="../../../translated_images/sw/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Michoro hii inaonyesha mwongozo wa maamuzi wa wakati RAG inaleta thamani dhidi ya wakati njia rahisi zinatosha.*

**Tumia RAG wakati:**
- Kujibu maswali kuhusu nyaraka za kipekee
- Taarifa hubadilika mara kwa mara (sera, bei, maalum)
- Usahihi unahitaji rejea za chanzo
- Maudhui ni makubwa mno kufikia katika maelekezo moja
- Unahitaji majibu yenye uthibitisho na msingi wa kweli

**Usitumie RAG wakati:**
- Maswali yanahitaji maarifa ya jumla ambayo mfano tayari ana
- Takwimu za wakati halisi zinahitajika (RAG hufanya kazi kwenye nyaraka zilizopakuliwa)
- Maudhui ni madogo vya kutosha kuingizwa moja kwa moja katika maelekezo

## Hatua Zifuatazo

**Moduli Ifuatayo:** [04-tools - Mawakala wa AI wenye Zana](../04-tools/README.md)

---

**Mwongozo:** [← Zamani: Moduli 02 - Uhandisi wa Maelekezo](../02-prompt-engineering/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 04 - Zana →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Taarifa ya Kutoa Mwajiri**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Wakati tunajitahidi kufikia usahihi, tafadhali fahamu kuwa tafsiri za moja kwa moja zinaweza kuwa na makosa au upungufu wa usahihi. Nyaraka asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu na ya binadamu inashauriwa. Hatutawajibika kwa kutoelewana au tafsiri zisizo sahihi zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->