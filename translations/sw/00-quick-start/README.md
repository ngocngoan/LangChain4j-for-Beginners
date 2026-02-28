# Module 00: Anza Haraka

## Orodha ya Yaliyomo

- [Utangulizi](../../../00-quick-start)
- [LangChain4j ni Nini?](../../../00-quick-start)
- [Mtegemezi wa LangChain4j](../../../00-quick-start)
- [Vitagizo](../../../00-quick-start)
- [Usanidi](../../../00-quick-start)
  - [1. Pata Tokeni Yako ya GitHub](../../../00-quick-start)
  - [2. Weka Tokeni Yako](../../../00-quick-start)
- [Endesha Mifano](../../../00-quick-start)
  - [1. Chat Msingi](../../../00-quick-start)
  - [2. Mifumo ya Prompt](../../../00-quick-start)
  - [3. Kupiga Simu ya Kifunction](../../../00-quick-start)
  - [4. Maswali na Majibu ya Hati (Easy RAG)](../../../00-quick-start)
  - [5. AI Inayewajibika](../../../00-quick-start)
- [Kila Mfano Unaonyesha Nini](../../../00-quick-start)
- [Hatua Zifuatazo](../../../00-quick-start)
- [Kutatua Matatizo](../../../00-quick-start)

## Utangulizi

Anza haraka hii imelenga kukupeleka haraka kutumia LangChain4j. Inashughulikia misingi kabisa ya kujenga programu za AI kwa kutumia LangChain4j na GitHub Models. Katika moduli zinazofuata utatumia Azure OpenAI na LangChain4j kujenga programu za hali ya juu zaidi.

## LangChain4j ni Nini?

LangChain4j ni maktaba ya Java inayorahisisha ujenzi wa programu zinazotumia AI. Badala ya kushughulika na wateja wa HTTP na uchambuzi wa JSON, unatumia APIs safi za Java.

"Namba" katika LangChain inamaanisha kuunganisha sehemu nyingi pamoja - unaweza kuunganisha prompt kwa mfano, kisha kwa parser, au kuunganisha simu za AI nyingi ambapo pato la moja linaingia kama ingizo la ijayo. Anza haraka hii inalenga misingi kabla ya kuchunguza nambahati za ugumu zaidi.

<img src="../../../translated_images/sw/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kuunganisha sehemu za LangChain4j - vipande vya ujenzi vinaunganishwa kuunda mtiririko mzuri wa AI*

Tutatumia sehemu kuu tatu:

**ChatModel** - Kiolesura cha maingiliano ya mfano wa AI. Itaje `model.chat("prompt")` na upate jibu la maandishi. Tunatumia `OpenAiOfficialChatModel` inayofanya kazi na vituo zinazofanana na OpenAI kama GitHub Models.

**AiServices** - Hutoa interfaces salama katika aina za huduma za AI. Tambua njia, ziandike na `@Tool`, na LangChain4j huendesha mchakato mzima. AI huita moja kwa moja njia zako za Java inapohitajika.

**MessageWindowChatMemory** - Huweka kumbukumbu ya mazungumzo. Bila hii, kila ombi ni huru. Ukiwa nayo, AI inakumbuka ujumbe wa awali na inaendelea na muktadha kwenye zamu nyingi.

<img src="../../../translated_images/sw/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Muundo wa LangChain4j - sehemu kuu zinazoshirikiana kuendesha programu zako za AI*

## Mtegemezi wa LangChain4j

Anza haraka hii inatumia utegemezi tatu wa Maven katika [`pom.xml`](../../../00-quick-start/pom.xml):

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

Moduli `langchain4j-open-ai-official` hutoa darasa la `OpenAiOfficialChatModel` linalounganisha na API zinazofanana na OpenAI. GitHub Models hutumia muundo huo huo wa API, kwa hivyo hakuna hitaji la kiunganishi maalum - tuelekeze URL ya msingi kwa `https://models.github.ai/inference`.

Moduli `langchain4j-easy-rag` hutoa mgawanyiko wa hati kiotomatiki, kuingiza, na utafutaji ili uweze kujenga programu za RAG bila kusanidi kila hatua kwa mkono.

## Vitaguzi

**Unatumia Dev Container?** Java na Maven vimeshatangazwa. Unahitaji tu Tokeni ya Ufikiaji wa Binafsi ya GitHub.

**Maendeleo ya Kwendanenti:**
- Java 21+, Maven 3.9+
- Tokeni ya Ufikiaji wa Binafsi wa GitHub (maelekezo chini)

> **Kumbuka:** Moduli hii inatumia `gpt-4.1-nano` kutoka GitHub Models. Usibadilishe jina la mfano kwenye msimbo - limesanidiwa kufanya kazi na mifano iliyopo ya GitHub.

## Usanidi

### 1. Pata Tokeni Yako ya GitHub

1. Nenda [Mipangilio ya GitHub → Tokeni za Ufikiaji wa Binafsi](https://github.com/settings/personal-access-tokens)
2. Bonyeza "Tengeneza tokeni mpya"
3. Weka jina linaloelezea (kwa mfano, "LangChain4j Demo")
4. Weka wakati wa kumalizika (siku 7 zinapendekezwa)
5. Chini ya "Ruhusa za Akaunti", tafuta "Models" na weka "Soma tu"
6. Bonyeza "Tengeneza tokeni"
7. Nakili na hifadhi tokeni yako - hautai ona tena

### 2. Weka Tokeni Yako

**Chaguo 1: Kutumia VS Code (Inapendekezwa)**

Ikiwa unatumia VS Code, ongeza tokeni yako kwenye faili `.env` kwenye mizizi ya mradi:

Kama faili `.env` halipo, nakili `.env.example` hadi `.env` au tengeneza faili mpya `.env` kwenye mizizi ya mradi.

**Mfano wa faili `.env`:**
```bash
# Ndani ya /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kisha unaweza bonyeza kulia juu ya faili yoyote ya onyesho (mfano, `BasicChatDemo.java`) kwenye Explorer na chagua **"Run Java"** au tumia usanidi wa anzishaji kutoka Jopo la Run and Debug.

**Chaguo 2: Kutumia Terminal**

Weka tokeni kama variable ya mazingira:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Endesha Mifano

**Kutumia VS Code:** Bonyeza kulia faili yoyote ya onyesho kwenye Explorer na chagua **"Run Java"**, au tumia usanidi wa anzishaji kutoka Jopo la Run and Debug (hakikisha umeongeza tokeni yako kwenye faili `.env` kwanza).

**Kutumia Maven:** Mbali na hapo, unaweza kuendesha kutoka mstari wa amri:

### 1. Chat Msingi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Mifumo ya Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Inaonyesha zero-shot, few-shot, chain-of-thought, na prompt za sehemu za majukumu.

### 3. Kupiga Simu ya Kifunction

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI huita moja kwa moja njia zako za Java inapobidi.

### 4. Maswali na Majibu ya Hati (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Uliza maswali kuhusu nyaraka zako kwa kutumia Easy RAG yenye kuingiza na utafutaji kiotomatiki.

### 5. AI Inayewajibika

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Tazama jinsi vichujio vya usalama vya AI vinavyozuia maudhui hatarishi.

## Kila Mfano Unaonyesha Nini

**Chat Msingi** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Anza hapa kuona LangChain4j kwa njia rahisi kabisa. Utaunda `OpenAiOfficialChatModel`, tuma prompt kwa `.chat()`, na upate jibu. Hii inaonyesha msingi: jinsi ya kuanzisha mifano na vituo maalum na funguo za API. Mara tu unapofahamu mtindo huu, kila kitu kingine kinajengwa juu yake.

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
> - "Nitabadilishaje kutoka GitHub Models hadi Azure OpenAI kwenye msimbo huu?"
> - "Ni vigezo gani vingine vinaweza kusanidiwa katika OpenAiOfficialChatModel.builder()?"
> - "Ninawezaje kuongeza majibu yanayotiririka badala ya kusubiri jibu kamili?"

**Uhandisi wa Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sasa kama unajua jinsi ya kuzungumza na mfano, tuchunguze unachomwambia. Onyesho hili linatumia usanidi sawa wa mfano lakini linaonyesha mifumo mitano ya prompt. Jaribu zero-shot kwa maagizo ya moja kwa moja, few-shot inayojifunza kutokana na mifano, chain-of-thought inayofunua hatua za kufikiri, na prompt za sehemu za majukumu zinazoweka muktadha. Utaona jinsi mfano mmoja unavyotoa matokeo tofauti sana kulingana na jinsi unavyoelekeza ombi lako.

Onyesho pia linaonyesha template za prompt, njia yenye nguvu ya kutengeneza prompt zinazotumika tena kwa vigezo.
Mfano hapa chini unaonyesha prompt inayotumia `PromptTemplate` ya LangChain4j kujaza vigezo. AI itajibu kulingana na mahali na shughuli zilizotolewa.

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
> - "Ni tofauti gani kati ya zero-shot na few-shot prompting, na ni lini nitumie kila moja?"
> - "Jinsi gani kidhibiti cha joto kinavyowaathiri majibu ya mfano?"
> - "Ni mbinu gani kuchukua tahadhari dhidi ya mashambulizi ya sindano ya prompt katika uzalishaji?"
> - "Ninawezaje kutengeneza vitu vya PromptTemplate vinavyotumika tena kwa mifumo ya kawaida?"

**Uunganishaji wa Zana** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hapa ndiko LangChain4j inakuwa yenye nguvu. Utatumia `AiServices` kuunda msaidizi wa AI anayeweza kuita njia zako za Java. Tuandike njia na `@Tool("description")` na LangChain4j itaendesha mengine yote - AI huchagua pande zote lini kutumia zana kulingana na mahitaji ya mtumiaji. Hii inaonyesha kupiga simu kwa function, mbinu kuu ya kuunda AI inayochukua hatua, si tu jibu maswali.

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
> - "Anuani ya @Tool inavyofanya kazi na LangChain4j hufanya nini nyuma ya pazia?"
> - "AI inaweza kuitisha zana nyingi kwa mpangilio kutatua matatizo magumu?"
> - "Nini kinatokea kama zana itatoa makosa - ni jinsi gani nipange kushughulikia makosa?"
> - "Ningeiunganisha API halisi tofauti na mfano huu wa kalkuletaje?"

**Maswali na Majibu ya Hati (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hapa utaona RAG (uzi wa matokeo unaotegemea utafutaji) kupitia njia ya "Easy RAG" ya LangChain4j. Nyaraka huzilipwa, kugawanywa kiotomatiki na kuingizwa kwenye duka la kumbukumbu, kisha mtafuta hupeleka vipande vinavyohitajika kwa AI wakati wa kuuliza. AI huitikia kulingana na nyaraka zako, si maarifa yake ya jumla.

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
> - "RAG inazuiaje kuhalucinishaji kwa AI ikilinganishwa na kutumia data ya mafunzo ya mfano?"
> - "Tofauti gani kati ya njia hii rahisi na pipeline iliyobinafsishwa ya RAG?"
> - "Ningepanuaje hili kushughulikia nyaraka nyingi au hifadhi kubwa za maarifa?"

**AI Inayewajibika** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Jenga usalama wa AI kwa kinga kwenye sababu nyingi. Onyesho hili linaonyesha ngazi mbili za ulinzi zinazofanya kazi pamoja:

**Sehemu ya 1: LangChain4j Input Guardrails** - Zuia prompt hatari kabla haijafika kwa LLM. Tengeneza lango za ulinzi maalum zinazokagua maneno au mifumo inayoruhusiwa. Hizi zinakimbia kwenye msimbo wako, hivyo ni haraka na haina gharama.

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

**Sehemu ya 2: Vichujio vya Usalama vya Mtoa Huduma** - GitHub Models ina vichujio vilivyojengwa ambavyo huvizia kile ambacho lango lako la ulinzi linaweza kukosa. Utaona vizuizi thabiti (makosa ya HTTP 400) kwa ukiukaji mkubwa na kukataa upole ambapo AI kinakataa kwa heshima.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) na uliza:
> - "InputGuardrail ni nini na ningeitengenezaje yangu mwenyewe?"
> - "Tofauti kati ya vizuizi thabiti na kukataa kwa upole ni ipi?"
> - "Kwa nini kutumia lango za ulinzi na vichujio vya mtoa huduma pamoja?"

## Hatua Zifuatazo

**Moduli Ifuatayo:** [01-introduction - Kuanzisha na LangChain4j na gpt-5 kwenye Azure](../01-introduction/README.md)

---

**Utengezaji:** [← Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Module 01 - Utangulizi →](../01-introduction/README.md)

---

## Kutatua Matatizo

### Kujenga kwa Mara ya Kwanza kwa Maven

**Tatizo:** `mvn clean compile` au `mvn package` ya kwanza huchukua muda mrefu (dakika 10-15)

**Sababu:** Maven inahitaji kupakua utegemezi wote wa mradi (Spring Boot, maktaba za LangChain4j, Azure SDK, n.k.) katika kujenga kwa mara ya kwanza.

**Msimamo:** Hali hii ni ya kawaida. Majaribio ya baadaye yatakuwa haraka zaidi kwani utegemezi huhifadhiwa ndani. Muda wa kupakua unategemea kasi ya mtandao wako.

### Muundo wa Amri za Maven kwa PowerShell

**Tatizo:** Amri za Maven zinashindwa na kosa `Unknown lifecycle phase ".mainClass=..."`
**Sababu**: PowerShell huchukulia `=` kama mtoaji wa mabadiliko ya thamani (variable assignment operator), na kuvunja muundo wa mali ya Maven

**Suluhisho**: Tumia operator ya kuacha utoaji wa amri `--%` kabla ya amri ya Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` huambia PowerShell kupitisha hoja zote zilizobaki kwa Maven moja kwa moja bila tafsiri.

### Onyesho la Emoji la Windows PowerShell

**Tatizo**: Majibu ya AI yanaonesha herufi zisizoeleweka (mfano, `????` au `â??`) badala ya emoji katika PowerShell

**Sababu**: Usimbaji wa chaguo-msingi wa PowerShell hauwezi kuunga mkono emoji za UTF-8

**Suluhisho**: Endesha amri hii kabla ya kuendesha programu za Java:
```cmd
chcp 65001
```

Hii inalazimisha usimbaji wa UTF-8 kwenye terminal. Vinginevyo, tumia Windows Terminal ambayo ina msaada bora wa Unicode.

### Kufuatilia Simu za API

**Tatizo**: Makosa ya uthibitishaji, mipaka ya kiwango, au majibu yasiyotarajiwa kutoka kwa mfano wa AI

**Suluhisho**: Mifano ina `.logRequests(true)` na `.logResponses(true)` kuonyesha simu za API kwenye console. Hii husaidia kutatua matatizo ya uthibitishaji, mipaka ya kiwango, au majibu yasiyotarajiwa. Ondoa bendera hizi katika uzalishaji ili kupunguza kelele za kumbukumbu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiarifa cha Kutotegemea**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo halali. Kwa taarifa muhimu, tafsiri ya kitaalamu na ya binadamu inahimizwa. Hatubeba dhamana kwa maelewano yoyote au tafsiri isiyo sahihi inayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->