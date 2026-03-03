# Moduli 00: Mwanzoni Haraka

## Orodha ya Maudhui

- [Utangulizi](../../../00-quick-start)
- [LangChain4j ni Nini?](../../../00-quick-start)
- [Mitegemezo ya LangChain4j](../../../00-quick-start)
- [Masharti ya Awali](../../../00-quick-start)
- [Usanidi](../../../00-quick-start)
  - [1. Pata Tokeni Yako ya GitHub](../../../00-quick-start)
  - [2. Weka Tokeni Yako](../../../00-quick-start)
- [Endesha Mifano](../../../00-quick-start)
  - [1. Mazungumzo ya Msingi](../../../00-quick-start)
  - [2. Mifumo ya Kuamsha](../../../00-quick-start)
  - [3. Kuitikia Kazi](../../../00-quick-start)
  - [4. Maswali na Majibu ya Nyaraka (RAG Rahisi)](../../../00-quick-start)
  - [5. AI Yenye Uwajibikaji](../../../00-quick-start)
- [Kila Mfano Unaonyesha Nini](../../../00-quick-start)
- [Hatua Zifuatazo](../../../00-quick-start)
- [Matatizo na Suluhisho](../../../00-quick-start)

## Utangulizi

Mwanzoni mwende haraka huu umetengenezwa kukuwezesha kuanza na LangChain4j haraka iwezekanavyo. Unafundisha misingi ya ujenzi wa programu za AI kwa kutumia LangChain4j na Modeli za GitHub. Katika moduli zinazofuata utabadilisha kwenda Azure OpenAI na GPT-5.2 na kuingia kwa kina zaidi katika kila dhana.

## LangChain4j ni Nini?

LangChain4j ni maktaba ya Java ambayo inarahisisha ujenzi wa programu zinazotumia AI. Badala ya kujihangaisha na wateja wa HTTP na uchambuzi wa JSON, unafanya kazi na API za Java zisizo na matatizo.

Neno "chain" katika LangChain linarejelea kuunganisha vipengele vingi kwa pamoja - unaweza kuunganisha onyo (prompt) kwa mfano (model) hadi parser, au kuunganisha simu nyingi za AI ambapo matokeo ya moja hutumika kama ingizo la moja inayofuata. Muanzoni hapa unazingatia mambo ya msingi kabla ya kuchunguza minyororo tata zaidi.

<img src="../../../translated_images/sw/langchain-concept.ad1fe6cf063515e1.webp" alt="Nguvu ya Kuunganisha LangChain4j" width="800"/>

*Kuunganisha vipengele ndani ya LangChain4j - vipande vya kujenga vinaunganishwa ili kuunda mchakato wa AI wenye nguvu*

Tutatumia vipengele vitatu vikuu:

**ChatModel** - Kiolesura cha mawasiliano na modeli za AI. Piga `model.chat("prompt")` na upate jibu kama mfululizo wa maneno. Tunatumia `OpenAiOfficialChatModel` ambayo hufanya kazi na vituo vya OpenAI vinavyolingana kama Modeli za GitHub.

**AiServices** - Huunda miunganisho salama ya huduma za AI. Tafsiri njia, ziweke alama za `@Tool`, na LangChain4j inasimamia mchakato. AI huwaita njia zako za Java kiotomatiki inapohitajika.

**MessageWindowChatMemory** - Huhifadhi kumbukumbu za mazungumzo. Bila hili, kila ombi ni huru. Ukiwa nayo, AI hukumbuka ujumbe wa awali na hubsidi muktadha kwa mizunguko mingi.

<img src="../../../translated_images/sw/architecture.eedc993a1c576839.webp" alt="Miundo ya LangChain4j" width="800"/>

*Miundo ya LangChain4j - vipengele vikuu vinavyofanya kazi pamoja kuendesha programu zako za AI*

## Mitegemezo ya LangChain4j

Mwanzoni hapa unatumia mitegemezo mitatu ya Maven katika [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Moduli `langchain4j-open-ai-official` hutoa darasa la `OpenAiOfficialChatModel` linalounganisha na API zinazolingana na OpenAI. Modeli za GitHub zinatumia muundo wa API uleule, hivyo hakuna hitaji la kiambatisho maalum — tuelekeze URL msingi kwa `https://models.github.ai/inference`.

Moduli `langchain4j-easy-rag` hutoa mgawanyo wa nyaraka, uingizaji, na uhamasishaji wa kawaida ili uweze kujenga programu za RAG bila kusanidi kila hatua kwa mikono.

## Masharti ya Awali

**Unatumia Dev Container?** Java na Maven tayari zimewekwa. Unahitaji tu Tokeni ya Upatikanaji ya Binafsi ya GitHub.

**Maendeleo ya Kiwanda:**
- Java 21+, Maven 3.9+
- Tokeni ya Upatikanaji ya Binafsi ya GitHub (maelekezo yapo chini)

> **Kumbuka:** Moduli hii inatumia `gpt-4.1-nano` kutoka Modeli za GitHub. Usibadilishe jina la modeli kwenye msimbo - limewekwa kufanya kazi na modeli zinazopatikana GitHub.

## Usanidi

### 1. Pata Tokeni Yako ya GitHub

1. Nenda kwenye [Mipangilio ya GitHub → Tokeni za Upatikanaji wa Binafsi](https://github.com/settings/personal-access-tokens)
2. Bonyeza "Generate new token"
3. Weka jina linaloeleweka (mfano, "Hello LangChain4j")
4. Weka muda wa kudumu (inapendekezwa siku 7)
5. Chini ya "Account permissions", pata "Models" na weka kuwa "Read-only"
6. Bonyeza "Generate token"
7. Nakili na hifadhi tokeni yako - hutaiwona tena

### 2. Weka Tokeni Yako

**Chaguo 1: Kutumia VS Code (Inapendekezwa)**

Ikiwa unatumia VS Code, ongeza tokeni yako kwenye faili `.env` kwenye mzizi wa mradi:

Iki faili `.env` haipo, nakili `.env.example` hadi `.env` au tengeneza faili mpya `.env` kwenye mzizi wa mradi.

**Mfano wa faili `.env`:**
```bash
# Katika /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kisha unaweza bonyeza kulia kwenye faili lolote la onyesho (mfano, `BasicChatDemo.java`) kwenye Explorer na chagua **"Run Java"** au tumia mipangilio ya kuanzisha (launch configurations) kutoka kwenye Run and Debug panel.

**Chaguo 2: Kutumia Terminal**

Weka tokeni kama mabadiliko ya mazingira:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Endesha Mifano

**Kutumia VS Code:** Bonyeza kulia kwenye faili lolote la onyesho kwenye Explorer na chagua **"Run Java"**, au tumia mipangilio ya kuanzisha kutoka Run and Debug panel (hakikisha umeweka tokeni kwenye `.env` kwanza).

**Kutumia Maven:** Vinginevyo, unaweza endesha kutoka mstari wa amri:

### 1. Mazungumzo ya Msingi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Mifumo ya Kuamsha

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Inaonyesha zero-shot, few-shot, chain-of-thought, na prompt kulingana na majukumu.

### 3. Kuitikia Kazi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI hutumia njia zako za Java kiotomatiki inapohitajika.

### 4. Maswali na Majibu ya Nyaraka (RAG Rahisi)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Uliza maswali kuhusu nyaraka zako ukitumia RAG Rahisi yenye ufungaji na uhamasishaji wa kiotomatiki.

### 5. AI Yenye Uwajibikaji

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Tazama jinsi vichujio vya usalama wa AI vinavyozuia maudhui hatarishi.

## Kila Mfano Unaonyesha Nini

**Mazungumzo ya Msingi** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Anza hapa kuona LangChain4j kwa urahisi wake zaidi. Utaunda `OpenAiOfficialChatModel`, tuma onyo with `.chat()`, na upate jibu. Hii inaonyesha msingi: jinsi ya kuanzisha modeli kwa vituo maalum na funguo za API. Ukifahamu mfano huu, kila kitu kingine kinajengwa juu yake.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) na uliza:
> - "Ningebadilishaje kutoka Modeli za GitHub kwenda Azure OpenAI kwenye msimbo huu?"
> - "Nini vigezo vingine ninaweza kusanidi kwenye OpenAiOfficialChatModel.builder()?"
> - "Ningewajeongeza majibu yanayotiririka badala ya kusubiri jibu kamili?"

**Uhandisi wa Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sasa unajua jinsi ya kuzungumza na modeli, hebu tuchunguze unavyosema kwake. Demo hii inatumia usanidi ule ule wa modeli lakini inaonyesha mifumo mitano tofauti ya kuamsha (prompt). Jaribu zero-shot kwa maagizo ya moja kwa moja, few-shot ambapo hujifunza kutoka kwa mifano, chain-of-thought inayoonyesha hatua za kufikiri, na prompt kulingana na majukumu yanayoanzisha muktadha. Utaona jinsi modeli ile ile inavyotoa matokeo tofauti sana kulingana na namna unavyounda ombi lako.

Demo pia inaonyesha template za prompt, njia yenye nguvu ya kuunda prompts zinazoweza kutumika tena kwa vigezo.
Mfano hapa chini unaonyesha prompt ukitumia `PromptTemplate` ya LangChain4j kujaza vigezo. AI itajibu kulingana na mahali pa kwenda na shughuli ulizotoa.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) na uliza:
> - "Tofauti kati ya zero-shot na few-shot ni nini, na ni lini niinate?"
> - "Jinsi joto la kiashiria (temperature) linavyoathiri majibu ya modeli?"
> - "Ni mbinu gani zinazotumika kuzuia mashambulizi ya prompt injection katika uzalishaji?"
> - "Ningejengaje vitu vya PromptTemplate vinavyoweza kutumika tena kwa mifano ya kawaida?"

**Uunganishaji wa Zana** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hapa ndipo LangChain4j linakuwa na nguvu zaidi. Utatumia `AiServices` kuunda msaidizi wa AI anaeyewaweza kuita njia zako za Java. Taja tu njia na `@Tool("maelezo")` na LangChain4j inasimamia yote - AI huamua kiotomatiki lini kutumia kila zana kulingana na kinachoulizwa. Hii inaonyesha kuiita kazi, tekniki muhimu ya kujenga AI inayoweza kuchukua hatua, siyo kujibu tu maswali.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) na uliza:
> - "Kiashirio cha @Tool kinafanya kazi vipi na LangChain4j hufanya nini nyuma ya pazia?"
> - "Je, AI inaweza kuita zana nyingi kwa mfululizo kutatua matatizo tata?"
> - "Nini kinatokea kama zana itatoa kosa - namna ya kushughulikia makosa ni gani?"
> - "Ningejumuishaje API halisi badala ya mfano huu wa calculator?"

**Maswali na Majibu ya Nyaraka (RAG Rahisi)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hapa utaona RAG (retrieval-augmented generation) ukitumia mbinu ya LangChain4j "Easy RAG". Nyaraka zinapakiwa, kugawanywa kiotomatiki na kuingizwa kwenye hifadhi ya kumbukumbu, kisha kivutio cha maudhui kinatoa vipande vinavyofaa kwa AI wakati wa kuuliza. AI hujibu kulingana na nyaraka zako, si maarifa yake ya jumla.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) na uliza:
> - "RAG inazuia vizungumzaji wa uongo vya AI vipi ikilinganishwa na kutumia data ya mafunzo ya modeli?"
> - "Tofauti kati ya njia rahisi hii na bomba la RAG maalum ni nini?"
> - "Ningeboreshaje kuhimili nyaraka nyingi au hifadhidata kubwa za maarifa?"

**AI Yenye Uwajibikaji** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Jenga usalama wa AI kwa kinga kwa kina. Demo hii inaonyesha tabaka mbili za ulinzi zinazofanya kazi pamoja:

**Sehemu 1: LangChain4j Input Guardrails** - Zuia maonyo hatarishi kabla hayafikie LLM. Tengeneza kinga maalum zinazotafuta maneno au mifumo isiyoruhusiwa. Hizi zinafanya kazi ndani ya msimbo wako, hivyo ni haraka na haina gharama.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Sehemu 2: Vichujio vya Usalama vya Mtoa Huduma** - Modeli za GitHub zina vichujio vilivyopo vinavyoshika yale kinga zako zinaweza kupitwa navyo. Utaona vizuizi vikali (makosa ya HTTP 400) kwa ukiukaji mkali na kukataa laini ambapo AI hukataa kwa heshima.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) na uliza:
> - "InputGuardrail ni nini na ninawezaje kutengeneza yangu?"
> - "Tofauti kati ya kizuizi kigumu na kukataa laini ni nini?"
> - "Kwa nini tumia kinga na vichujio vya mtoa huduma pamoja?"

## Hatua Zifuatazo

**Moduli Ifuatayo:** [01-introduction - Kuanzisha na LangChain4j](../01-introduction/README.md)

---

**Uelekezaji:** [← Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 01 - Utangulizi →](../01-introduction/README.md)

---

## Matatizo na Suluhisho

### Kujenga Mara ya Kwanza kwa Maven

**Tatizo:** Amri ya awali `mvn clean compile` au `mvn package` huchukua muda mrefu (dakika 10-15)

**Sababu:** Maven inahitaji kupakua mitegemezo yote ya mradi (Spring Boot, maktaba za LangChain4j, SDK za Azure, n.k.) mara ya kwanza.

**Suluhisho:** Hali hii ni ya kawaida. Majaribio yanayofuata yatakuwa haraka zaidi kwani mitegemezo itakuwa imehifadhiwa ndani. Muda wa kupakua unategemea kasi ya mtandao wako.

### Syntax ya Amri za Maven katika PowerShell

**Tatizo:** Amri za Maven zinashindwa na kosa `Unknown lifecycle phase ".mainClass=..."`
**Sababu**: PowerShell hufasiri `=` kama mtaalamu wa kugawa variable, kuharibu sintaksia ya mali za Maven

**Suluhisho**: Tumia opereta ya kuacha kuchambua `--%` kabla ya amri ya Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Opereta `--%` huambia PowerShell kupitisha hoja zote zilizobaki moja kwa moja kwa Maven pasipo tafsiri.

### Onyesho la Emoji la Windows PowerShell

**Tatizo**: Majibu ya AI yanaonyesha herufi zisizotambulika (mfano, `????` au `â??`) badala ya emojis ndani ya PowerShell

**Sababu**: Usimbaji wa PowerShell wa chaguo-msingi hauungi mkono emojis za UTF-8

**Suluhisho**: Endesha amri hii kabla ya kutekeleza programu za Java:
```cmd
chcp 65001
```

Hii inalazimisha usimbaji wa UTF-8 kwenye terminal. Vinginevyo, tumia Windows Terminal ambayo ina msaada bora zaidi wa Unicode.

### Kurekebisha Mapigo ya API

**Tatizo**: Makosa ya uthibitishaji, mipaka ya kiwango, au majibu yasiyotarajiwa kutoka kwa mfano wa AI

**Suluhisho**: Mifano inajumuisha `.logRequests(true)` na `.logResponses(true)` kuonyesha mapigo ya API kwenye konsoli. Hii husaidia kutatua makosa ya uthibitishaji, mipaka ya kiwango, au majibu yasiyotarajiwa. Ondoa bendera hizi katika uzalishaji kupunguza kelele za rekodi.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Majeruhi**:
Hati hii imetafsiriwa kwa kutumia huduma ya utafsiri kwa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au ukosefu wa usahihi. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya mtaalamu wa binadamu inashauriwa. Hatuna dhamana kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->