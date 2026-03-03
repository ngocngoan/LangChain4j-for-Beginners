# Module 04: Wakala wa AI Wenye Vifaa

## Jedwali la Yaliyomo

- [Utajifunza Nini](../../../04-tools)
- [Mahitaji ya Awali](../../../04-tools)
- [Kuelewa Wakala wa AI Wenye Vifaa](../../../04-tools)
- [Jinsi Kufanya Simu kwa Vifaa Inavyofanya Kazi](../../../04-tools)
  - [Ufafanuzi wa Vifaa](../../../04-tools)
  - [Uamuzi](../../../04-tools)
  - [Utekelezaji](../../../04-tools)
  - [Uundaji wa Jibu](../../../04-tools)
  - [Miundombinu: Uunganishaji wa Spring Boot Auto-Wiring](../../../04-tools)
- [Kuhusisha Vifaa Mfululizo](../../../04-tools)
- [Endesha Programu](../../../04-tools)
- [Kutumia Programu](../../../04-tools)
  - [Jaribu Matumizi Rahisi ya Vifaa](../../../04-tools)
  - [Jaribu Kuhusisha Vifaa](../../../04-tools)
  - [Tazama Mtiririko wa Mazungumzo](../../../04-tools)
  - [Fanya Jaribio na Maombi Mbalimbali](../../../04-tools)
- [Mafundisho Muhimu](../../../04-tools)
  - [Mfumo wa ReAct (Kufikiria na Kutenda)](../../../04-tools)
  - [Maelezo ya Vifaa ni Muhimu](../../../04-tools)
  - [Usimamizi wa Kikao](../../../04-tools)
  - [Utambuzi wa Makosa](../../../04-tools)
- [Vifaa Vilivyopo](../../../04-tools)
- [Wakati wa Kutumia Wakala Wenye Vifaa](../../../04-tools)
- [Vifaa dhidi ya RAG](../../../04-tools)
- [Hatua zinazofuata](../../../04-tools)

## Utajifunza Nini

Hadi sasa, umejifunza jinsi ya kuendesha mazungumzo na AI, kupanga maelekezo kwa ufanisi, na kuweka majibu msingi katika nyaraka zako. Lakini bado kuna kikomo muhimu: mifano ya lugha inaweza kuzalisha maneno tu. Haiwezi kuangalia hali ya hewa, kufanya mahesabu, kuuliza hifadhidata, au kuingiliana na mifumo ya nje.

Vifaa hubadilisha hili. Kwa kumpa mfano fursa ya kupiga simu kwa kazi fulani, unamgeuza kutoka kizalishaji cha maneno kuwa wakala anayeweza kuchukua hatua. Mfano huamua lini anahitaji kifaa, kifaa gani cha kutumia, na ni vigezo gani vya kupitisha. Msimbo wako unatekeleza kazi na kurudisha matokeo. Mfano huhusisha matokeo hayo katika jibu lake.

## Mahitaji ya Awali

- Kumaliza [Module 01 - Utangulizi](../01-introduction/README.md) (Rasilimali za Azure OpenAI zimewekwa)
- Kumaliza moduli zilizopita inapendekezwa (moduli hii inataja [dhana za RAG kutoka Module 03](../03-rag/README.md) katika kulinganisha Vifaa dhidi ya RAG)
- Faili `.env` katika saraka kuu yenye ruhusa za Azure (iliyoanzishwa na `azd up` katika Module 01)

> **Kumbuka:** Ikiwa hujakamilisha Module 01, fuata maagizo ya usambazaji huko kwanza.

## Kuelewa Wakala wa AI Wenye Vifaa

> **📝 Kumbuka:** Neno "wakala" katika moduli hii linamaanisha wasaidizi wa AI walioimarishwa kwa uwezo wa kupiga simu za kifaa. Hii ni tofauti na mifumo ya **Agentic AI** (wakala huru wenye mipango, kumbukumbu, na kufikiria hatua nyingi) ambayo tutajadili katika [Module 05: MCP](../05-mcp/README.md).

Bila vifaa, mfano wa lugha unaweza tu kuzalisha maandishi kutoka kwa data yake ya mafunzo. Uliza hali ya hewa ya sasa, basi huweza tu kubahatisha. Mpe vifaa, na anaweza kupiga API ya hali ya hewa, kufanya mahesabu, au kuuliza hifadhidata — kisha kuunganisha matokeo halisi hayo katika jibu lake.

<img src="../../../translated_images/sw/what-are-tools.724e468fc4de64da.webp" alt="Bila Vifaa dhidi ya Kwa Vifaa" width="800"/>

*Bila vifaa, mfano unaweza tu kubahatisha — kwa vifaa anaweza kupiga API, kuendesha mahesabu, na kutoa data ya wakati halisi.*

Wakala wa AI mwenye vifaa hufuata mfumo wa **Reasoning and Acting (ReAct)**. Mfano haujibu tu — hujiuliza anahitaji nini, huchukua hatua kwa kupiga simu ya kifaa, huangalia matokeo, halafu huamua kama atafanya tena au kutoa jibu la mwisho:

1. **Kufikiria** — Wakala anachambua swali la mtumiaji na kuamua taarifa anazohitaji
2. **Kutenda** — Wakala huchagua kifaa sahihi, hutengeneza vigezo sahihi, na hupiga simu
3. **Kuchunguza** — Wakala hupokea matokeo ya kifaa na kutathmini matokeo
4. **Kurudia au Kujibu** — Ikiwa data zaidi zinahitajika, wakala anarudia; vinginevyo, huandika jibu la lugha ya asili

<img src="../../../translated_images/sw/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Mfumo wa ReAct" width="800"/>

*Mzunguko wa ReAct — wakala hufikiria atakachofanya, hutenda kwa kupiga simu ya kifaa, huchunguza matokeo, na kuendelea hadi aweze kutoa jibu la mwisho.*

Hii hufanyika moja kwa moja. Unafafanua vifaa na maelezo yao. Mfano unashughulikia uamuzi wa lini na jinsi ya kuvitumia.

## Jinsi Kufanya Simu kwa Vifaa Inavyofanya Kazi

### Ufafanuzi wa Vifaa

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Unafafanua kazi zilizo na maelezo wazi na vipimo vya vigezo. Mfano unaona maelezo haya katika hali ya mfumo wake na kuelewa kile kila kifaa kinachofanya.

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

// Msaidizi anaunganishwa kwa moja kwa moja na Spring Boot na:
// - Kijasho cha ChatModel
// - Mbinu zote za @Tool kutoka kwa madarasa ya @Component
// - Mtoa kumbukumbu wa ChatMemory kwa usimamizi wa kikao
```

Mchoro hapa chini unaelezea kila alama na unaonyesha jinsi kila kipengele kinavyosaidia AI kuelewa lini kupiga simu ya kifaa na hoja gani kupitisha:

<img src="../../../translated_images/sw/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia ya Ufafanuzi wa Vifaa" width="800"/>

*Anatomia ya ufafanuzi wa kifaa — @Tool huambia AI lini kiitumiwe, @P huelezea kila kigezo, na @AiService huunganisha kila kitu pamoja wakati wa kuanzisha.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) na uliza:
> - "Ningejumuisha vipi API halisi ya hali ya hewa kama OpenWeatherMap badala ya data bandia?"
> - "Nini kinachafanya maelezo ya kifaa kuwa mazuri kusaidia AI kutumia kwa usahihi?"
> - "Ninashughulikiaje makosa ya API na mipaka ya kiwango katika utekelezaji wa vifaa?"

### Uamuzi

Mtu anapoomba "Hali ya hewa huko Seattle ikoje?", mfano haichague kifaa kwa bahati nasibu. Hulinganisha nia ya mtumiaji dhidi ya kila maelezo ya kifaa alichonacho, huweka alama kwa kila moja kulingana na umuhimu, na huchagua bora zaidi. Kisha hutengeneza simu ya kazi iliyopangwa kwa vigezo sahihi — hapa, kuweka `location` kuwa `"Seattle"`.

Kama hakuna kifaa kinacholingana na ombi la mtumiaji, mfano hurudi kwa kujibu kwa maarifa yake mwenyewe. Ikiwa vifaa vingi vinalingana, huichagua ile iliyo maalum zaidi.

<img src="../../../translated_images/sw/decision-making.409cd562e5cecc49.webp" alt="Jinsi AI Huchagua Kifaa cha Kutumia" width="800"/>

*Mfano hutathmini kila kifaa kinachopatikana dhidi ya nia ya mtumiaji na huchagua bora zaidi — ndiyo maana kuandika maelezo ya kifaa kwa uwazi na kwa usahihi ni muhimu.*

### Utekelezaji

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot inaunganishwa moja kwa moja na kiolesura kinachoeleza `@AiService` na vifaa vyote vilivyojisajili, na LangChain4j hufanya simu za vifaa moja kwa moja. Ndani ya mfumo, simu ya kifaa hufanyika kupitia hatua sita — kutoka kwa swali la mtumiaji kwa lugha ya asili hadi jibu la lugha ya asili:

<img src="../../../translated_images/sw/tool-calling-flow.8601941b0ca041e6.webp" alt="Mtiririko wa Kupiga Simu za Vifaa" width="800"/>

*Mtiririko wa mwisho hadi mwisho — mtumiaji anauliza swali, mfano huchagua kifaa, LangChain4j hufanya kazi, na mfano huhusisha matokeo katika jibu la asili.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) na uliza:
> - "Mfumo wa ReAct unavyofanya kazi na kwa nini ni mzuri kwa wakala wa AI?"
> - "Wakala huaamuaje kifaa gani kutumia na kwa mpangilio gani?"
> - "Kitu kinatokeaje ikiwa utekelezaji wa kifaa unashindwa - ningeshughulikiaje makosa kwa uthabiti?"

### Uundaji wa Jibu

Mfano hupokea data ya hali ya hewa na kuibadilisha kuwa jibu la lugha ya asili kwa mtumiaji.

### Miundombinu: Uunganishaji wa Spring Boot Auto-Wiring

Moduli hii inatumia LangChain4j iliyounganishwa na Spring Boot kwa interface za `@AiService` zilizoelezwa. Wakati wa kuanzisha, Spring Boot hutambua kila `@Component` inayojumuisha njia za `@Tool`, kibin `ChatModel` chako, na `ChatMemoryProvider` — kisha huunganisha vyote kuwa kiolesura kimoja cha `Assistant` bila msongamano wowote wa mkono.

<img src="../../../translated_images/sw/spring-boot-wiring.151321795988b04e.webp" alt="Miundombinu ya Spring Boot Auto-Wiring" width="800"/>

*Kiolesura cha @AiService kinaunda muungano wa ChatModel, vipengele vya kifaa, na mtoaji kumbukumbu — Spring Boot hushughulikia uunganishaji wote moja kwa moja.*

Faida kuu za njia hii:

- **Uunganishaji wa moja kwa moja wa Spring Boot** — ChatModel na vifaa huingizwa moja kwa moja
- **Mfumo wa @MemoryId** — Usimamizi wa kumbukumbu wa kikao wa moja kwa moja
- **Toleo moja tu** — Msaidizi huundwa mara moja na kutumika tena ili kuboresha utendaji
- **Utekelezaji salama kwa aina** — Njia za Java zinapigiwa simu moja kwa moja na uongofu wa aina
- **Udhibiti wa mzunguko wa mazungumzo** — Hushughulikia mfuatano wa vifaa moja kwa moja
- **Hakuna msongamano wa mkono** — Hakuna simu za mikono za `AiServices.builder()` au ramani za kumbukumbu

Njia mbadala (manual `AiServices.builder()`) zinahitaji msimbo zaidi na hazina faida za uunganishaji wa Spring Boot.

## Kuhusisha Vifaa Mfululizo

**Kuhusisha Vifaa Mfululizo** — Nguvu halisi ya wakala anayetumia vifaa huonekana pale swali moja linapohitaji vifaa vingi. Uliza "Hali ya hewa huko Seattle kwa Fahrenheit?" na wakala huhuisha vifaa viwili: kwanza hupiga `getCurrentWeather` kupata joto kwa Celsius, halafu hutumia `celsiusToFahrenheit` kubadilisha — yote katika mzunguko mmoja wa mazungumzo.

<img src="../../../translated_images/sw/tool-chaining-example.538203e73d09dd82.webp" alt="Mfano wa Kuhusisha Vifaa Mfululizo" width="800"/>

*Kuhusisha vifaa mfululizo katika vitendo — wakala hupiga simu ya `getCurrentWeather` kwanza, kisha huingiza matokeo ya Celsius kwenye `celsiusToFahrenheit`, na kutoa jibu la mchanganyiko.*

**Kushindwa kwa Hekima** — Uliza hali ya hewa katika jiji ambalo halipo kwenye data bandia. Kifaa hurudisha ujumbe wa kosa, na AI inaeleza hawezi kusaidia badala ya kuanguka. Vifaa hushindwa kwa usalama. Mchoro huu unaonyesha tofauti ya mbinu mbili — ambapo kushughulikia makosa kwa usahihi, wakala hugundua dhamana na kujibu kwa msaada, ilhali bila hilo programu yote huanguka:

<img src="../../../translated_images/sw/error-handling-flow.9a330ffc8ee0475c.webp" alt="Mtiririko wa Kushughulikia Makosa" width="800"/>

*Ikiwa kifaa kinashindwa, wakala hugundua kosa na kujibu kwa maelezo ya msaada badala ya kuanguka.*

Hii hufanyika katika mzunguko mmoja wa mazungumzo. Wakala huandaa simu nyingi za vifaa kwa uhuru.

## Endesha Programu

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo katika saraka kuu yenye ruhusa za Azure (iliundwa wakati wa Module 01). Endesha hili kutoka katika saraka ya moduli (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanza programu zote kwa kutumia `./start-all.sh` kutoka saraka kuu (kama ilivyoelezwa katika Module 01), moduli hii tayari inazingirwa na bandari 8084. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8084.

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Kifaa cha maendelezo cha dev kina ugani wa Spring Boot Dashboard, unaotoa kiolesura cha kuona kwa usimamizi wa programu zote za Spring Boot. Unaweza kuipata kwenye Bara la Shughuli upande wa kushoto wa VS Code (tazama alama ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot kwenye kazi
- Anzisha/acha programu kwa kubofya mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fuatilia hali ya programu

Bonyeza kitufe cha kucheza karibu na "tools" kuanzisha moduli hii, au anzisha moduli zote kwa pamoja.

Hili ndilo Spring Boot Dashboard linavyoonekana katika VS Code:

<img src="../../../translated_images/sw/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard katika VS Code — anzisha, acha, na fuatilia moduli zote kutoka mahali pamoja*

**Chaguo 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka katika saraka ya mizizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anzisha moduli hii tu:

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

Skripti zote zinapakia moja kwa moja tofauti za mazingira kutoka faili `.env` ya saraka kuu na zitajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanza:
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

Fungua http://localhost:8084 katika kivinjari chako.

**Kumaliza:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Kivuli hiki pekee
# Au
cd ..; .\stop-all.ps1  # Vione vyote
```

## Kutumia Programu

Programu hutoa kiolesura cha mtandao ambacho unaweza kuingiliana nacho na wakala wa AI anaefikia vifaa vya hali ya hewa na ubadilishaji wa joto. Hili ndilo kiolesura kinavyoonekana — linajumuisha mifano ya kuanza haraka na paneli ya mazungumzo ya kutuma maombi:
<a href="images/tools-homepage.png"><img src="../../../translated_images/sw/tools-homepage.4b4cd8b2717f9621.webp" alt="Kiolesura cha Zana za AI Agent" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Kiolesura cha Zana za AI Agent - mifano ya haraka na kiolesura cha mazungumzo kwa ajili ya kuingiliana na zana*

### Jaribu Matumizi Rahisi ya Zana

Anza na ombi rahisi: "Badilisha digrii 100 Fahrenheit kuwa Celsius". Wakala anatambua inahitaji zana ya uongofu wa joto, anaiita kwa vigezo sahihi, na kurejesha matokeo. Angalia jinsi inavyohisi asili - hukutaja ni zana gani ya kutumia au jinsi ya kuiita.

### Jaribu Muunganisho wa Zana

Sasa jaribu kitu kigumu zaidi: "Hali ya hewa ikoje Seattle na ibadilishe kuwa Fahrenheit?" Tazama wakala akifanya kazi hii kwa hatua. Anapata kwanza hali ya hewa (ambayo hurudisha Celsius), anatambua inahitaji kubadilisha kuwa Fahrenheit, anaiita zana ya uongofu, na kuunganisha matokeo yote kuwa jibu moja.

### Tazama Mtiririko wa Mazungumzo

Kiolesura cha mazungumzo kinahifadhi historia ya mazungumzo, kikiraidia unavyoendesha mazungumzo yenye mizunguko mingi. Unaweza kuona maswali na majibu yote ya awali, kufanya iwe rahisi kufuatilia mazungumzo na kuelewa jinsi wakala anavyojenga muktadha juu ya mabadilishano mengi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sw/tools-conversation-demo.89f2ce9676080f59.webp" alt="Mazungumzo na Miito Mingi ya Zana" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mazungumzo yenye mizunguko mingi yanaonyesha uongofu rahisi, utafutaji wa hali ya hewa, na muunganisho wa zana*

### Jaribu Maombi Tofauti

Jaribu mchanganyiko mbalimbali:
- Utafutaji wa hali ya hewa: "Hali ya hewa ikoje Tokyo?"
- Uongofu wa joto: "25°C ni sawa na Kelvin ngapi?"
- Maswali yaliyounganishwa: "Angalia hali ya hewa Paris na niambie kama iko juu ya 20°C"

Angalia jinsi wakala anavyotafsiri lugha ya asili na kuihusisha na miito sahihi ya zana.

## Misingi Muhimu

### Mchoro wa ReAct (Kutafakari na Kutenda)

Wakala anabadilisha kati ya kutafakari (kuamua cha kufanya) na kutenda (kutumia zana). Mchoro huu unamwezesha kutatua matatizo kwa kujitegemea badala ya kujibu maagizo tu.

### Maelezo ya Zana ni Muhimu

Ubora wa maelezo ya zana zako unavyoathiri jinsi wakala anazitumia vizuri. Maelezo wazi yanayolenga husaidia mfano kuelewa lini na jinsi ya kuitia zana moja baada ya nyingine.

### Usimamizi wa Kikao

Alama `@MemoryId` huruhusu usimamizi wa kumbukumbu wa kikao moja kwa moja. Kila kitambulisho cha kikao kinapata mfano wake wa `ChatMemory` unaosimamiwa na bean ya `ChatMemoryProvider`, hivyo watumiaji wengi wanaweza kuingiliana na wakala kwa wakati mmoja bila mazungumzo yao kuchanganyika. Mchoro ufuatao unaonyesha jinsi watumiaji wengi wanavyoelekezwa kwenda kumbukumbu za mazungumzo zilizo wazi kulingana na kitambulisho chao cha kikao:

<img src="../../../translated_images/sw/session-management.91ad819c6c89c400.webp" alt="Usimamizi wa Kikao kwa @MemoryId" width="800"/>

*Kila kitambulisho cha kikao kinaonyesha historia ya mazungumzo ya pekee — watumiaji hawawoni ujumbe wa wengine.*

### Usimamizi wa Makosa

Zana zinaweza kushindikana — API hutaumika kwa wakati, vigezo vinaweza kuwa batili, huduma za nje huweza kushindwa. Wakala wa uzalishaji wanahitaji usimamizi wa makosa ili mfano uweze kueleza matatizo au kujaribu mbadala badala ya kusababisha programu yote kuanguka. Wakati zana inapotupa makosa, LangChain4j inaikamata na kurudisha ujumbe wa kosa kwa mfano, ambaye anaweza kueleza tatizo kwa lugha ya asili.

## Zana Zinazopatikana

Mchoro ufuatao unaonyesha mfumo mpana wa zana unazoweza kujenga. Moduli hii inaonyesha zana za hali ya hewa na joto, lakini mfano wa `@Tool` unafanya kazi kwa njia yoyote ya Java — kutoka kwa maswali ya hifadhidata hadi usindikaji wa malipo.

<img src="../../../translated_images/sw/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Mfumo wa Zana" width="800"/>

*Njia yoyote ya Java iliyoandikwa na @Tool inakuwa inapatikana kwa AI — mfumo huu unajumuisha hifadhidata, API, barua pepe, operesheni za faili, na zaidi.*

## Wakati wa Kutumia Maajenti Wenye Zana

Siyo kila ombi linahitaji zana. Uamuzi unategemea kama AI inahitaji kuingiliana na mifumo ya nje au inaweza kujibu kutoka kwa maarifa yake mwenyewe. Mwongozo ufuatao unasisitiza lini zana zinapoongeza thamani na lini hazihitajiki:

<img src="../../../translated_images/sw/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Wakati wa Kutumia Zana" width="800"/>

*Mwongozo wa uamuzi wa haraka — zana ni kwa ajili ya data ya wakati halisi, mahesabu, na vitendo; maarifa ya jumla na kazi za ubunifu hazihitaji.*

## Zana dhidi ya RAG

Moduli 03 na 04 zote zinaongeza kile AI inaweza kufanya, lakini kwa njia tofauti kabisa. RAG humruhusu mfano kupata **maarifa** kwa kuchukua nyaraka. Zana zinampa uwezo wa kufanya **matendo** kwa kuita kazi. Mchoro ufuatao unaonyesha mbinu hizi mbili kando kwa kando — kutoka jinsi kila mtiririko wa kazi unavyofanya kazi hadi ubaguzi kati yao:

<img src="../../../translated_images/sw/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Mlinganyo wa Zana dhidi ya RAG" width="800"/>

*RAG huchukua taarifa kutoka kwa nyaraka za takatifu — Zana hufanya vitendo na kuchukua data ya wakati halisi, yenye mabadiliko. Mifumo mingi ya uzalishaji huunganisha zote mbili.*

Kwa vitendo, mifumo mingi ya uzalishaji huchanganya mbinu hizi mbili: RAG kwa kuimarisha majibu kwenye nyaraka zako, na Zana kwa kuchukua data hai au kufanya operesheni.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [05-mcp - Itifaki ya Muktadha wa Mfano (MCP)](../05-mcp/README.md)

---

**Uelekezaji:** [← Iliyotangulia: Moduli 03 - RAG](../03-rag/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kukana**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au ukosefu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya mtaalamu wa binadamu inashauriwa. Hatubebii dhamana kwa kutoelewana au tafsiri potofu inayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->