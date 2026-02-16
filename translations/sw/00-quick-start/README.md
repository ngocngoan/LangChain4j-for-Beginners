# Module 00: Kuanzia Haraka

## Jedwali la Yaliyomo

- [Utangulizi](../../../00-quick-start)
- [LangChain4j ni Nini?](../../../00-quick-start)
- [Mategemeo ya LangChain4j](../../../00-quick-start)
- [Mahitaji ya Msingi](../../../00-quick-start)
- [Mazingira](../../../00-quick-start)
  - [1. Pata Tokeni Yako ya GitHub](../../../00-quick-start)
  - [2. Weka Tokeni Yako](../../../00-quick-start)
- [Endesha Mifano](../../../00-quick-start)
  - [1. Mazungumzo ya Msingi](../../../00-quick-start)
  - [2. Mifumo ya Maelekezo](../../../00-quick-start)
  - [3. Kupiga Msimbo wa Kazi](../../../00-quick-start)
  - [4. Maswali na Majibu ya Nyaraka (RAG)](../../../00-quick-start)
  - [5. AI Inayewajibika](../../../00-quick-start)
- [Kila Mfano Unaonyeshaje](../../../00-quick-start)
- [Hatua Zifuatazo](../../../00-quick-start)
- [Kutatua Matatizo](../../../00-quick-start)

## Utangulizi

Kuanzia haraka hii imelenga kukupeleka haraka kwenye matumizi ya LangChain4j. Inashughulikia msingi kabisa wa kujenga programu za AI kwa kutumia LangChain4j na Modeli za GitHub. Katika moduli zinazofuata utatumia Azure OpenAI pamoja na LangChain4j kujenga programu za hali ya juu zaidi.

## LangChain4j ni Nini?

LangChain4j ni maktaba ya Java inayorahisisha ujenzi wa programu zinazotumia nguvu za AI. Badala ya kushughulikia wateja wa HTTP na uchambuzi wa JSON, unatumia API za Java zilizo safi.

"Nanga" katika LangChain inamaanisha kuunganisha vipengele vingi pamoja - unaweza kuunganisha maelekezo kwa mfano, kisha kwa kichambuzi, au kuunganisha simu nyingi za AI ambapo toleo la moja linaingiza data kwa ingizo la linalofuata. Kuanzia haraka hii inalenga misingi kabla ya kuchunguza nanga ngumu zaidi.

<img src="../../../translated_images/sw/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Kuunganisha vipengele ndani ya LangChain4j - vipande vya msingi vinavyoungana kuunda mtiririko mzuri wa AI*

Tutatumia vipengele vitatu vikuu:

**ChatLanguageModel** - Muingiliano wa mawasiliano na mfano wa AI. Piga `model.chat("prompt")` na upate mfuatano wa majibu. Tunatumia `OpenAiOfficialChatModel` ambayo hufanya kazi na vifungua vyenye ukaribu wa OpenAI kama Modeli za GitHub.

**AiServices** - Huunda interfaz salama za huduma za AI. Eleza njia, ziweke alama na `@Tool`, na LangChain4j hutunziza usimamizi. AI huajibu kwa njia za Java kiotomatiki unapo hitaji.

**MessageWindowChatMemory** - Huhifadhi kumbukumbu za mazungumzo. Bila hii, kila ombi ni huru. Na hii, AI huzingatia ujumbe wa awali na huendelea na muktadha katika zamu nyingi.

<img src="../../../translated_images/sw/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Miundo ya LangChain4j - vipengele vikuu vinavyofanya kazi pamoja kuimarisha programu zako za AI*

## Mategemeo ya LangChain4j

Kuanzia haraka hii inatumia utegemezi wa Maven mbili kwenye [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Moduli ya `langchain4j-open-ai-official` hutoa darasa la `OpenAiOfficialChatModel` linalounganisha na API za ukaribu wa OpenAI. Modeli za GitHub zinatumia muundo sawa wa API, hivyo hakuna hitaji la kiambatisho maalum - tuelekeze URL msingi kwa `https://models.github.ai/inference`.

## Mahitaji ya Msingi

**Unatumia Dev Container?** Java na Maven tayari vimesakinishwa. Unahitaji Tokeni ya Upatikanaji Binafsi ya GitHub pekee.

**Maendeleo ya Kwenye Mitaa:**
- Java 21+, Maven 3.9+
- Tokeni ya Upatikanaji Binafsi ya GitHub (maelekezo chini)

> **Kumbuka:** Moduli hii inatumia `gpt-4.1-nano` kutoka Modeli za GitHub. Usibadilishe jina la mfano katika msimbo - limesanidiwa kufanya kazi na modeli zinazopatikana GitHub.

## Mazingira

### 1. Pata Tokeni Yako ya GitHub

1. Nenda kwenye [Mipangilio ya GitHub → Tokeni za Upatikanaji Binafsi](https://github.com/settings/personal-access-tokens)
2. Bonyeza "Generate new token"
3. Weka jina la kufafanua (mfano, "Onyesho la LangChain4j")
4. Weka muda wa kumalizika (siku 7 zinapendekezwa)
5. Chini ya "Account permissions", tafuta "Models" na weka "Read-only"
6. Bonyeza "Generate token"
7. Nakili na uhifadhi tokeni yako - hautaiona tena

### 2. Weka Tokeni Yako

**Chaguo 1: Kutumia VS Code (Inapendekezwa)**

Ikiwa unatumia VS Code, ongeza tokeni yako kwenye faili `.env` kwenye mzizi wa mradi:

Ikiwa faili `.env` haipo, nakili `.env.example` kuwa `.env` au unda faili mpya `.env` kwenye mzizi wa mradi.

**Mfano wa faili `.env`:**
```bash
# Ndani ya /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Kisha unaweza bonyeza kulia kwenye faili lolote la maonyesho (mfano, `BasicChatDemo.java`) kwenye Explorer na uchague **"Run Java"** au tumia mipangilio ya kuanzisha kutoka kwenye paneli ya Run and Debug.

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

**Kutumia VS Code:** Bonyeza kulia kwenye faili lolote la maonyesho kwenye Explorer na uchague **"Run Java"**, au tumia mipangilio ya kuanzisha kutoka kwenye paneli ya Run and Debug (hakikisha umeongeza tokeni yako kwenye faili `.env` kwanza).

**Kutumia Maven:** Vinginevyo, unaweza kuendesha kutoka mstari wa amri:

### 1. Mazungumzo ya Msingi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Mifumo ya Maelekezo

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Inaonyesha maelekezo ya zero-shot, few-shot, chain-of-thought, na role-based.

### 3. Kupiga Msimbo wa Kazi

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI hujipigia njia za Java kiotomatiki inapohitajika.

### 4. Maswali na Majibu ya Nyaraka (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Uliza maswali kuhusu maudhui katika `document.txt`.

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

## Kila Mfano Unaonyeshaje

**Mazungumzo ya Msingi** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Anza hapa kuona LangChain4j kwa urahisi wake kabisa. Utaunda `OpenAiOfficialChatModel`, utatuma maelekezo kwa `.chat()`, na upate jibu. Hii inaonyesha msingi: jinsi ya kuanzisha modeli kwa vitu maalum na funguo za API. Ukifahamu mtindo huu, kila kitu kingine kinajengwa juu yake.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) na uliza:
> - "Nitabadilisha vipi kutoka Modeli za GitHub kwenda Azure OpenAI katika msimbo huu?"
> - "Ni vigezo gani vingine ninavyoweza kuandaa katika OpenAiOfficialChatModel.builder()?"
> - "Ninawezaje kuongeza majibu ya moja kwa moja badala ya kusubiri jibu kamili?"

**Uhandisi wa Maelekezo** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sasa unajua jinsi ya kuzungumza na mfano, hebu tuchunguze unachomwambia. Onyesho hili linatumia usanidi mmoja wa mfano lakini linaonyesha mifumo mitano tofauti ya maelekezo. Jaribu maelekezo ya zero-shot kwa maagizo moja kwa moja, few-shot yanayojifunza kutoka mifano, chain-of-thought kueleza hatua za hoja, na role-based kuweka muktadha. Utaona jinsi mfano mmoja unavyotoa matokeo tofauti kabisa kulingana na jinsi unavyowasilisha ombi lako.

Demo pia inaonyesha templates za maelekezo, njia yenye nguvu ya kutengeneza maelekezo yanayoweza kutumika tena na vigezo.
Mfano ufuatao unaonyesha maelekezo ukitumia LangChain4j `PromptTemplate` kujaza vigezo. AI itajibu kulingana na mahali na shughuli zilizotolewa.

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
> - "Nini tofauti kati ya maelekezo ya zero-shot na few-shot, na nitumie lini kila moja?"
> - "Vigezo vya joto vinaathirije majibu ya mfano?"
> - "Ni mbinu gani za kuzuia mashambulizi ya sindano ya maelekezo katika utengenezaji?"
> - "Ninawezaje kutengeneza vitu vya PromptTemplate vinavyoweza kutumika tena kwa mifumo ya kawaida?"

**Ushirikishwaji wa Zana** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hapa ndipo LangChain4j inaanza kuonyesha nguvu zake. Utatumia `AiServices` kuunda msaidizi wa AI anayeweza kupiga simu njia za Java zako. Tuweke alama njia na `@Tool("description")` na LangChain4j hutatua mengine - AI huamua kiotomatiki lini kutumia kila chombo kulingana na mteja anavyoomba. Hii inaonyesha kupiga simu za kazi, mbinu muhimu ya kujenga AI inayoweza kuchukua hatua, siyo kujibu maswali tu.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) na uliza:
> - "Vipengele vya @Tool vinavyofanya kazi vipi na LangChain4j hufanya nini nyuma ya pazia?"
> - "Je, AI inaweza kupiga zana nyingi mfululizo kutatua matatizo magumu?"
> - "Nifanyeje ikiwa chombo kitatoa kosa - ni jinsi gani namshughulikia makosa?"
> - "Nitang'anisha API halisi vipi badala ya mfano wa kifaa hiki cha kalkuleta?"

**Maswali na Majibu ya Nyaraka (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hapa utaona msingi wa RAG (uzalishaji unaoimarishwa kwa utafutaji). Badala ya kutegemea data za mafunzo za mfano, unapakia maudhui kutoka [`document.txt`](../../../00-quick-start/document.txt) na kuingiza kwenye maelekezo. AI hutoa majibu kulingana na nyaraka yako, si maarifa yake ya jumla. Hii ni hatua ya kwanza kuelekea kujenga mifumo inayofanya kazi na data zako mwenyewe.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Kumbuka:** Njia hii rahisi inapakia nyaraka yote kwenye maelekezo. Kwa faili kubwa (>10KB), utaivuka mipaka ya muktadha. Moduli 03 itahusu kugawanya na utafutaji wa vector kwa mifumo ya RAG ya uzalishaji.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) na uliza:
> - "RAG inazuia vipi mawazo ya uwongo ya AI ikilinganishwa na kutumia data za mafunzo za mfano?"
> - "Tofauti gani kati ya njia hii rahisi na kutumia embeddings za vector kwa utafutaji?"
> - "Nitapanuaje hili kushughulikia nyaraka nyingi au misingi mikubwa ya maarifa?"
> - "Ni mbinu gani bora za kuunda maelekezo kuhakikisha AI inatumia muktadha uliotolewa pekee?"

**AI Inayewajibika** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Jenga usalama wa AI kwa kinga nyingi. Demo hii inaonyesha ngazi mbili za ulinzi zinazofanya kazi pamoja:

**Sehemu 1: LangChain4j Input Guardrails** - Zuia maelekezo hatarishi kabla hayajafikia LLM. Tengeneza guardrails maalum zinazoangalia maneno au mifumo isiyoruhusiwa. Hizi hufanyika kwa msimbo wako, hivyo ni haraka na haina gharama.

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

**Sehemu 2: Vichujio vya Usalama vya Mtoa Huduma** - Modeli za GitHub zina vichujio vilivyojengwa vinavyokamata kile ambacho guardrails zako zinaweza kukosa. Utaona vizuizi vikali (makosa ya HTTP 400) kwa ukiukaji mkubwa na kukataa kwa heshima ambapo AI hushindwa kuendelea.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) na uliza:
> - "Ni nini InputGuardrail na ninawezaje kutengeneza yangu?"
> - "Tofauti gani kati ya kizuizi kikali na kukataa kwa heshima?"
> - "Kwa nini nitumie guardrails na vichujio vya mtoa huduma pamoja?"

## Hatua Zifuatazo

**Moduli Ifuatayo:** [01-introduction - Kuanzia na LangChain4j na gpt-5 kwenye Azure](../01-introduction/README.md)

---

**Uelekezaji:** [← Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 01 - Utangulizi →](../01-introduction/README.md)

---

## Kutatua Matatizo

### Ujenzi wa Kwanza kwa Maven

**Tatizo**: Mvutano wa awali wa `mvn clean compile` au `mvn package` unachukua muda mrefu (dakika 10-15)

**Sababu**: Maven inahitaji kupakua utegemezi wote wa mradi (Spring Boot, maktaba za LangChain4j, SDK za Azure, nk) kwenye ujenzi wa kwanza.

**Suluhisho**: Hali hii ni ya kawaida. Majengo yanayofuata yatakuwa ya haraka zaidi kwa sababu utegemezi unahifadhiwa ndani ya kompyuta. Muda wa kupakua unategemea kasi ya mtandao wako.
### Syntax ya Amri ya PowerShell Maven

**Tatizo**: Amri za Maven zinashindwa na kosa `Unknown lifecycle phase ".mainClass=..."`

**Sababu**: PowerShell huelewa `=` kama kiendeshaji cha kuweka thamani ya variable, na kuvunja syntax ya mali za Maven

**Suluhisho**: Tumia kiendeshaji cha kusimamisha uchambuzi `--%` kabla ya amri ya Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Kiendeshaji `--%` kinaeleza PowerShell kupitisha hoja zote zilizobaki kwa herufi halisi kwa Maven bila uchambuzi.

### Onyesho la Emoji la Windows PowerShell

**Tatizo**: Majibu ya AI yanaonyesha herufi za takataka (mfano, `????` au `â??`) badala ya emoji kwenye PowerShell

**Sababu**: Usanidi wa kawaida wa PowerShell hauungi mkono emoji za UTF-8

**Suluhisho**: Endesha amri hii kabla ya kuendesha programu za Java:
```cmd
chcp 65001
```

Hii inalazimisha usimbaji wa UTF-8 kwenye terminal. Vinginevyo, tumia Windows Terminal ambayo ina msaada bora wa Unicode.

### Kurekebisha Makosa ya Mitoaji ya API

**Tatizo**: Makosa ya uthibitishaji, vizingiti vya kiwango, au majibu yasiyotegemewa kutoka kwa mfano wa AI

**Suluhisho**: Mifano inajumuisha `.logRequests(true)` na `.logResponses(true)` kuonyesha mitoaji ya API kwenye koni. Hii husaidia kutatua makosa ya uthibitishaji, vizingiti vya kiwango, au majibu yasiyotegemewa. Ondoa alama hizi katika uzalishaji ili kupunguza kelele za kumbukumbu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kutokuwajibika**:
Hati hii imetafsiriwa kwa kutumia huduma ya kutafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi usahihi, tafadhali fahamu kuwa tafsiri zilizofanywa kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na wanadamu inashauriwa. Hatuwezi kuwajibika kwa uelewa au tafsiri potofu kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->