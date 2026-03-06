# Moduli 04: Wakala wa AI Wenye Vifaa

## Orodha ya Maudhui

- [Utajifunza Nini](../../../04-tools)
- [Mahitaji ya Awali](../../../04-tools)
- [Kuelewa Wakala wa AI Wenye Vifaa](../../../04-tools)
- [Jinsi Kupigia Simu Vifaa Kunavyofanya Kazi](../../../04-tools)
  - [Ufafanuzi wa Vifaa](../../../04-tools)
  - [Kufanya Maamuzi](../../../04-tools)
  - [Utekelezaji](../../../04-tools)
  - [Uundaji wa Majibu](../../../04-tools)
  - [Mwangalizi: Spring Boot Auto-Wiring](../../../04-tools)
- [Kufuata Vifaa Mfululizo](../../../04-tools)
- [Endesha Programu](../../../04-tools)
- [Kutumia Programu](../../../04-tools)
  - [Jaribu Matumizi Rahisi ya Kifaa](../../../04-tools)
  - [Jaribu Kufuata Vifaa Mfululizo](../../../04-tools)
  - [Tazama Mtiririko wa Mazungumzo](../../../04-tools)
  - [Jaribu Maombi Tofauti](../../../04-tools)
- [Mafunzo Muhimu](../../../04-tools)
  - [Mhimili wa ReAct (Kufikiria na Kutenda)](../../../04-tools)
  - [Maelezo ya Vifaa ni Muhimu](../../../04-tools)
  - [Usimamizi wa Kikao](../../../04-tools)
  - [Kushughulikia Makosa](../../../04-tools)
- [Vifaa Vilivyopo](../../../04-tools)
- [Lini Kutumia Wakala Wenye Vifaa](../../../04-tools)
- [Vifaa vs RAG](../../../04-tools)
- [Hatua Zifuatazo](../../../04-tools)

## What You'll Learn

Mpaka sasa, umejifunza jinsi ya kuendesha mazungumzo na AI, kuweka muundo mzuri wa maagizo, na kuweka majibu msingi katika nyaraka zako. Lakini bado kuna kizuizi cha msingi: mifano ya lugha inaweza tu kuzalisha maandishi. Haiwezi kuangalia hali ya hewa, kufanya mahesabu, kuuliza hifadhidata, au kuingiliana na mifumo ya nje.

Vifaa hubadilisha hili. Kwa kumpa mfano ufikiaji wa kazi anazoweza kuitisha, unabadilisha kutoka kuwa mzalishaji wa maandishi kuwa wakala anayeweza kuchukua hatua. Mfano huamua ni lini anahitaji kifaa, kifaa gani cha kutumia, na ni vigezo gani atavitumie. Msimbo wako unatekeleza kazi na kurudisha matokeo. Mfano huingiza matokeo hayo katika jibu lake.

## Prerequisites

- Umekamilisha [Moduli 01 - Utangulizi](../01-introduction/README.md) (Rasilimali za Azure OpenAI zimewekwa)
- Moduli zilizopita zilizopendekezwa kukamilishwa (moduli hii inahusisha [madhumuni ya RAG kutoka Moduli 03](../03-rag/README.md) katika kulinganisha Vifaa vs RAG)
- Faili `.env` katika mzizi wa saraka yenye nyaraka za Azure (imetengenezwa na `azd up` katika Moduli 01)

> **Kumbuka:** Kama bado hujakamilisha Moduli 01, fuata maelekezo ya uanzishaji huko kwanza.

## Understanding AI Agents with Tools

> **📝 Kumbuka:** Neno "wakala" katika moduli hii linarejelea wasaidizi wa AI walioboresha kwa uwezo wa kuitisha vifaa. Hii ni tofauti na mifumo ya **Agentic AI** (wakala huru wenye upangaji, kumbukumbu, na kufikiria kwa hatua nyingi) ambayo tutaifunika katika [Moduli 05: MCP](../05-mcp/README.md).

Bila vifaa, mfano wa lugha unaweza tu kuzalisha maandishi kutoka kwenye data ya mafunzo. Muulize hali ya hewa ya sasa, nao wanahitaji kubahatisha. Mpe vifaa, anaweza kuitisha API ya hali ya hewa, kufanya mahesabu, au kuuliza hifadhidata — kisha achanganye matokeo halisi hayo katika jibu lake.

<img src="../../../translated_images/sw/what-are-tools.724e468fc4de64da.webp" alt="Bila Vifaa vs Kwa Vifaa" width="800"/>

*Bila vifaa mfano unaweza tu kubahatisha — kwa vifaa anaweza kuitisha APIs, kufanya mahesabu, na kurudisha data za wakati halisi.*

Wakala wa AI mwenye vifaa hufuata muundo wa **Kufikiria na Kutenda (ReAct)**. Mfano hautaji tu jibu — huangalia anachohitaji, huchukua hatua kwa kuitisha kifaa, hutazama matokeo, na kisha huamua kama ataendelea tena au kutoa jibu la mwisho:

1. **Fikiria** — Wakala huchambua swali la mtumiaji na kuamua taarifa anazohitaji
2. **Tenda** — Wakala huchagua kifaa sahihi, huandika vigezo vinavyofaa, na kuitisha
3. **Tazama** — Wakala hupokea matokeo ya kifaa na kuyapima
4. **Rudia au Jibu** — Kama anahitaji data zaidi, hurudia; vinginevyo, huandika jibu la asili

<img src="../../../translated_images/sw/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Mhimili wa ReAct" width="800"/>

*Mzunguko wa ReAct — wakala hufikiria hatua ya kuchukua, hutenda kwa kuitisha kifaa, hutazama matokeo, na hurudia hadi aweze kutoa jibu la mwisho.*

Hii hufanyika moja kwa moja. Unafafanua vifaa na maelezo yao. Mfano huzishughulikia maamuzi ya lini na jinsi ya kuzitumia.

## How Tool Calling Works

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Unafafanua kazi zilizo na maelezo wazi na sifa za vigezo. Mfano huona maelezo haya katika agizo la mfumo wake na kuelewa kila kifaa hufanya nini.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Mantiki yako ya kutafuta hali ya hewa
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Msaidizi anaunganishwa moja kwa moja na Spring Boot na:
// - Kijani cha ChatModel
// - Mbinu zote za @Tool kutoka kwa madarasa ya @Component
// - ChatMemoryProvider kwa usimamizi wa kikao
```

Mchoro hapa chini unavunja kila alama na kuonyesha jinsi kila kipengele kinavyosaidia AI kuelewa ni lini kuitisha kifaa na ni hoja gani za kupitisha:

<img src="../../../translated_images/sw/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anuwai ya Ufafanuzi wa Vifaa" width="800"/>

*Anuwai ya ufafanuzi wa kifaa — @Tool inamwambia AI ni lini kitumie, @P kinaelezea kila parameta, na @AiService hufunga kila kitu pamoja wakati wa kuanzisha.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) na uliza:
> - "Ningejumuishaje API halisi ya hali ya hewa kama OpenWeatherMap badala ya data ya mfano?"
> - "Nini hufanya maelezo ya kifaa kuwa mazuri ambayo husaidia AI kuvitumia kwa usahihi?"
> - "Ningeshughulikiaje makosa ya API na mipaka ya kiwango katika utekelezaji wa vifaa?"

### Decision Making

Mtumiaji anapoomba "Hali ya hewa huko Seattle ikoje?", mfano hauchagui kifaa tu kiholela. Hulinganisha nia ya mtumiaji na kila maelezo ya kifaa alichonacho, hupima kila moja kwa uhusiano, kisha huchagua kilicho bora zaidi. Kisha hutengeneza wito wa kazi ulioratibiwa kwa vigezo sahihi — katika hili, kuweka `location` kwenye `"Seattle"`.

Kama hakuna kifaa kinachofaa kwa ombi la mtumiaji, mfano hurudi kujibu kutoka kwenye maarifa yake. Ikiwa vifaa vingi vinaendana, huchagua kile mahususi zaidi.

<img src="../../../translated_images/sw/decision-making.409cd562e5cecc49.webp" alt="Jinsi AI Huamua Kifaa Cha Kutumia" width="800"/>

*Mfano hupima kila kifaa kinachopatikana dhidi ya nia ya mtumiaji na huchagua bora zaidi — ndio maana uandishi wa maelezo ya kifaa kwa uwazi na udhibitisho ni muhimu.*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot huhusisha moja kwa moja interface ya kiufafanuzi ya `@AiService` na vifaa vyote vilizosajiliwa, na LangChain4j hufanya wito wa vifaa moja kwa moja. Nyuma ya matukio, wito kamili wa kifaa hupitia hatua sita — kutoka kwa swali la asili la lugha ya mtumiaji hadi jibu la asili:

<img src="../../../translated_images/sw/tool-calling-flow.8601941b0ca041e6.webp" alt="Mtiririko wa Wito wa Kifaa" width="800"/>

*Mtiririko wa mwisho hadi mwisho — mtumiaji anauliza swali, mfano huchagua kifaa, LangChain4j hufanya kazi, na mfano huingiza matokeo katika jibu la asili.*

Ulikuwa ukifanya demo ya [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) katika Moduli 00, tayari umeona muundo huu ukienda — vifaa vya `Calculator` vilijitishwa kwa njia ile ile. Mchoro wa mfululizo hapa chini unaonyesha kwa usahihi kilichotokea nyuma ya pazia wakati wa demo hiyo:

<img src="../../../translated_images/sw/tool-calling-sequence.94802f406ca26278.webp" alt="Mchoro wa Mfululizo wa Wito wa Kifaa" width="800"/>

*Mzunguko wa wito wa kifaa kutoka demo ya Quick Start — `AiServices` hutuma ujumbe wako na miundo ya kifaa kwa LLM, LLM hurudisha wito wa kazi kama `add(42, 58)`, LangChain4j hufanya njia ya `Calculator` kwa mtaa, na hurudisha matokeo kwa jibu la mwisho.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) na uliza:
> - "Mhimili wa ReAct hufanya kazi vipi na kwa nini ni mzuri kwa wakala wa AI?"
> - "Wakala huamuaje kifaa cha kutumia na kwa mpangilio gani?"
> - "Nini hutokea ikiwa utekelezaji wa kifaa utakosa - nifanyeje kushughulikia makosa kwa usalama?"

### Response Generation

Mfano hupokea data ya hali ya hewa na kuibadilisha kuwa jibu la lugha ya asili kwa mtumiaji.

### Architecture: Spring Boot Auto-Wiring

Moduli hii hutumia LangChain4j kuunganishwa na Spring Boot kwa kutumia interfaces za kiufafanuzi za `@AiService`. Wakati wa kuanzisha, Spring Boot hutambua kila `@Component` yenye njia za `@Tool`, `ChatModel` yako, na `ChatMemoryProvider` — kisha huvifunga vyote pamoja ndani ya interface moja ya `Assistant` bila kutoa nakala ya ziada.

<img src="../../../translated_images/sw/spring-boot-wiring.151321795988b04e.webp" alt="Mwangalizi wa Spring Boot Auto-Wiring" width="800"/>

*Interface ya @AiService inaunganisha ChatModel, vipengele vya kifaa, na mtoaji wa kumbukumbu — Spring Boot hushughulikia uunganishaji wote moja kwa moja.*

Hapa ni mzunguko kamili wa maombi kama mchoro wa mfululizo — kutoka ombi la HTTP kupitia controller, huduma, na wakala aliyeunganishwa moja kwa moja, hadi utekelezaji wa kifaa na kurudi:

<img src="../../../translated_images/sw/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Mchoro wa Mfululizo wa Wito wa Vifaa wa Spring Boot" width="800"/>

*Mzunguko kamili wa maombi wa Spring Boot — ombi la HTTP huingia kupitia controller na huduma hadi kwa wakala aliyefungwa moja kwa moja, ambaye huendesha LLM na wito za kifaa moja kwa moja.*

Faida kuu za njia hii:

- **Auto-wiring ya Spring Boot** — ChatModel na vifaa huingizwa moja kwa moja
- **Mfumo wa @MemoryId** — Usimamizi wa kumbukumbu ya kikao moja kwa moja
- **Mfano mmoja** — Msaidizi huundwa mara moja na kutumika tena kwa ufanisi zaidi
- **Utekelezaji salama kwa aina** — Njia za Java huitwa moja kwa moja na uongofu wa aina
- **Utawala wa mizunguko kadhaa** — Hushughulikia kufuata vifaa mfululizo kwa moja kwa moja
- **Hakuna nakala ya ziada** — Hakuna wito wa mkono wa `AiServices.builder()` au ramani ya kumbukumbu ya HashMap

Njia mbadala (mkono `AiServices.builder()`) zinahitaji msimbo zaidi na hukosa faida za kuingizwa kwa Spring Boot.

## Tool Chaining

**Kufuata Vifaa Mfululizo** — Nguvu halisi ya wakala wa vifaa huonekana wakati swali moja linahitaji vifaa vingi. Uliza "Hali ya hewa huko Seattle kwa Fahrenheit?" na wakala huungana moja kwa moja na vifaa viwili: kwanza huuita `getCurrentWeather` kupata joto kwa Celsius, kisha hupitisha thamani hiyo kwa `celsiusToFahrenheit` kwa ajili ya uongofu — yote katika zunguko moja la mazungumzo.

<img src="../../../translated_images/sw/tool-chaining-example.538203e73d09dd82.webp" alt="Mfano wa Kufuata Vifaa Mfululizo" width="800"/>

*Kufuata vifaa mfululizo kwa vitendo — wakala huita getCurrentWeather kwanza, kisha hupeleka matokeo ya Celsius kwa celsiusToFahrenheit, na kutoa jibu lililochanganywa.*

**Makosa Safi** — Uliza hali ya hewa katika mji usiopo kwenye data ya mfano. Kifaa hurudisha ujumbe wa kosa, na AI hueleza hawezi kusaidia badala ya kuanguka programu. Vifaa kushindwa kwa usalama. Mchoro hapa chini unaonyesha tofauti mbili — kwa usimamizi sahihi wa makosa, wakala huchukua kosa na kujibu kwa msaada, huku bila hivyo programu nzima ikidondoka:

<img src="../../../translated_images/sw/error-handling-flow.9a330ffc8ee0475c.webp" alt="Mtiririko wa Kushughulikia Makosa" width="800"/>

*Wakati kifaa kinakosa, wakala huchukua kosa na kujibu kwa maelezo ya msaada badala ya kuanguka.*

Hii hufanyika katika zunguko moja la mazungumzo. Wakala huandaa wito za vifaa vingi kwa uhuru.

## Run the Application

**Thibitisha utekelezaji:**

Hakikisha faili `.env` ipo katika mzizi wa saraka na nyaraka za Azure (zimetengenezwa wakati wa Moduli 01). Endesha hii kutoka kwa saraka ya moduli (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka mzizi wa saraka (kama ilivyoelezwa katika Moduli 01), moduli hii tayari inaendesha kwenye bandari 8084. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8084.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kifurushi cha dev kina nyongeza ya Spring Boot Dashboard, inayotoa muingiliano wa kuona kusimamia programu zote za Spring Boot. Unaweza kuipata katika Bar ya Shughuli upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka kwenye Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye eneo la kazi
- Anzisha/zimia programu kwa bonyeza kitufe kimoja
- Tazama kumbukumbu za programu kwa wakati halisi
- Simamia hali ya programu

Bofya kitufe cha kucheza kando na "tools" kuanza moduli hii, au anzisha moduli zote kwa pamoja.

Hivi ndivyo Spring Boot Dashboard inavyoonekana katika VS Code:

<img src="../../../translated_images/sw/dashboard.9b519b1a1bc1b30a.webp" alt="Dashibodi ya Spring Boot" width="400"/>

*Dashibodi ya Spring Boot katika VS Code — anzisha, zimia, na simamia moduli zote kutoka sehemu moja*

**Chaguo 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka ya mizizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka kuu
.\start-all.ps1
```

Au anza tu moduli hii:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Mafaili yote mawili hujipakia kiotomatiki mabadiliko ya mazingira kutoka kwenye faili ya mizizi `.env` na yatajenga JARs ikiwa hayapo.

> **Kumbuka:** Ikiwa ungependa kujenga moduli zote kwa mkono kabla ya kuanza:
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

Fungua http://localhost:8084 kwenye kivinjari chako.

**Kusimamisha:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
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

Programu hutoa kiolesura cha wavuti ambapo unaweza kuingiliana na wakala wa AI ambaye ana ufikiaji wa zana za hali ya hewa na uongofu wa joto. Hivi ndivyo kiolesura kinavyoonekana — kinajumuisha mfano wa kuanza haraka na jopo la mazungumzo kwa ajili ya kutuma maombi:

<a href="images/tools-homepage.png"><img src="../../../translated_images/sw/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Kiolesura cha Zana za Wakala wa AI - mifano ya kuanza haraka na kiolesura cha mazungumzo kwa ajili ya kuingiliana na zana*

### Jaribu Matumizi Rahisi ya Zana

Anza na ombi rahisi: "Geuza nyuzi 100 Fahrenheit kwenda Celsius". Wakala hutambua anahitaji zana ya uongofu joto, anhitishe na vigezo sahihi, na kurudisha matokeo. Tambua jinsi inavyoonekana ya asili sana - hukubainisha zana ya kutumia au jinsi ya kuitumia.

### Jaribu Mfuatano wa Zana

Sasa jaribu jambo changamano zaidi: "Je, hali ya hewa iko Seattle na geuza kwenda Fahrenheit?" Tazama wakala akiendesha hatua kwa hatua. Kwanza hupata hali ya hewa (ambayo hurudisha Celsius), hutambua anahitaji kugeuza kwenda Fahrenheit, anwita zana ya uongofu, na kuunganisha matokeo yote kuwa jibu moja.

### Tazama Mtiririko wa Mazungumzo

Kiolesura cha mazungumzo kinahifadhi historia ya mazungumzo, kukuwezesha kuwa na mwingiliano wa mizunguko mingi. Unaweza kuona maswali yote ya awali na majibu, kufanya iwe rahisi kufuatilia mazungumzo na kuelewa jinsi wakala anavyojenga muktadha kupitia mizunguko mingi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sw/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mizunguko mingi ya mazungumzo ikionyesha uongofu rahisi, upataji wa hali ya hewa, na mfuatano wa zana*

### Jaribu Maombi Mbalimbali

Jaribu mchanganyiko mbalimbali:
- Upataji wa hali ya hewa: "Je, hali ya hewa iko Tokyo?"
- Uongofu wa joto: "25°C ni kiasi gani Kelvin?"
- Maswali mchanganyiko: "Angalia hali ya hewa Paris na niambie kama iko juu ya 20°C"

Tambua jinsi wakala anavyotafsiri lugha ya kawaida na kuiunganisha na miito ya zana inayofaa.

## Dhana Muhimu

### Mfumuko wa ReAct (Kufikiria na Kutenda)

Wakala hubadilisha kati ya kufikiria (kuamua cha kufanya) na kutenda (kutumia zana). Mfumo huu unawawezesha kutatua matatizo kwa uhuru badala ya kujibu tu maagizo.

### Maelezo ya Zana ni Muhimu

Ubora wa maelezo yako ya zana huathiri moja kwa moja jinsi wakala anavyotumia zana hizo. Maelezo wazi na maalum husaidia mfano kuelewa lini na jinsi ya kuitisha kila zana.

### Usimamizi wa Kikao

Maandishi ya `@MemoryId` huruhusu usimamizi wa kumbukumbu kiotomatiki wa kikao. Kila kitambulisho cha kikao kinapata mfano wake wa `ChatMemory` unaosimamiwa na `ChatMemoryProvider` bean, hivyo watumiaji wengi wanaweza kuingiliana na wakala kwa wakati mmoja bila mazungumzo yao kuchanganyika. Mchoro ufuatao unaonyesha jinsi watumiaji wengi wanavyopokelewa kwenye hifadhi za kumbukumbu zilizo kwenye panya tofauti kulingana na vitambulisho vya vikao:

<img src="../../../translated_images/sw/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Kila kitambulisho cha kikao kinaonyesha historia ya mazungumzo iliyotengwa — watumiaji hawawahi kuona ujumbe wa wengine.*

### Kushughulikia Makosa

Zana zinaweza kushindwa — API zinaweza kuchelewa, vigezo vinaweza kuwa batili, huduma za nje zinaweza kushindwa. Wakala wa uzalishaji wanahitaji kushughulikia makosa ili mfano uweze kueleza matatizo au jaribu mbadala badala ya kuanguka programu yote. Wakati zana inapotupa hitilafu, LangChain4j huzipokea na kujaribu kubaini ujumbe wa kosa na kuurudisha kwenye mfano, ambao unaweza kueleza tatizo kwa lugha ya asili.

## Zana Zinazopatikana

Mchoro hapa chini unaonyesha mfumo mpana wa zana unazoweza kujenga. Moduli hii inaonesha zana za hali ya hewa na joto, lakini mfano huo wa `@Tool` unafanyia kazi yoyote ya Java — kutoka kwa maswali ya hifadhidata hadi usindikaji wa malipo.

<img src="../../../translated_images/sw/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Njia yoyote ya Java yenye alama ya @Tool inapatikana kwa AI — mfano huo unajumuisha hifadhidata, API, barua pepe, kazi za faili, na zaidi.*

## Wakati wa Kutumia Wakala wa Zana

Sio kila ombi linahitaji zana. Uamuzi hutegemea kama AI inahitaji kuwasiliana na mifumo ya nje au inaweza kujibu kutoka maarifa yake mwenyewe. Mwongozo ufuatao unahitimisha wakati zana zinatoa thamani na wakati hazihitajiki:

<img src="../../../translated_images/sw/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Mwongozo wa haraka wa uamuzi — zana ni kwa data ya wakati halisi, hesabu, na vitendo; maarifa ya jumla na kazi za ubunifu hazizihitaji.*

## Zana vs RAG

Moduli 03 na 04 zote zinaongeza kile AI kinaweza kufanya, lakini kwa njia tofauti kabisa. RAG huwapa mfano ufikiaji wa **maarifa** kwa kupata nyaraka. Zana huwapa mfano uwezo wa kuchukua **vitendo** kwa kuwaita kazi. Mchoro hapa chini unaonyesha kulinganisha mbinu hizi mbili upande kwa upande — kutoka jinsi kila mtiririko unavyofanya kazi hadi mapungufu kati yao:

<img src="../../../translated_images/sw/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG hupata taarifa kutoka kwa nyaraka za kudumu — Zana hufanya vitendo na kupata data za wakati halisi. Mifumo mingi ya uzalishaji huunganisha yote mawili.*

Katika utendaji, mifumo mingi ya uzalishaji huunganisha mbinu zote mbili: RAG kwa kuimarisha majibu kwenye nyaraka zako, na Zana kwa kupata data za moja kwa moja au kutekeleza shughuli.

## Hatua Zifuatazo

**Moduli Inayofuata:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Uvinjari:** [← Iliyotangulia: Moduli 03 - RAG](../03-rag/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiadhabu**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya kutafsiri kwa AI [Co-op Translator](https://github.com/Azure/co-op-translator). Wakati tunajitahidi kuwa sahihi, tafadhali fahamu kuwa tafsiri za moja kwa moja zinaweza kuwa na makosa au kasoro. Nyaraka asilia katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa habari muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatubeba wajibu wowote wa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->