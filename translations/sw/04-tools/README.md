# Moduli 04: Wakala wa AI wenye Zana

## Jedwali la Maudhui

- [Utajifunza Nini](../../../04-tools)
- [Mahitaji ya Awali](../../../04-tools)
- [Kuelewa Wakala wa AI wenye Zana](../../../04-tools)
- [Jinsi Kufanya Kitozi cha Zana Kazi](../../../04-tools)
  - [Maelezo ya Zana](../../../04-tools)
  - [Uamuzi](../../../04-tools)
  - [Utekelezaji](../../../04-tools)
  - [Uundaji wa Jibu](../../../04-tools)
  - [Miundo: Spring Boot Auto-Wiring](../../../04-tools)
- [Mnyororo wa Zana](../../../04-tools)
- [Endesha Programu](../../../04-tools)
- [Kutumia Programu](../../../04-tools)
  - [Jaribu Matumizi Rahisi ya Zana](../../../04-tools)
  - [Jaribu Mnyororo wa Zana](../../../04-tools)
  - [Tazama Mtiririko wa Mazungumzo](../../../04-tools)
  - [Jaribu Maombi Tofauti](../../../04-tools)
- [Mafundisho Muhimu](../../../04-tools)
  - [Mfumo wa ReAct (Kufikiri na Kutenda)](../../../04-tools)
  - [Maelezo ya Zana ni Muhimu](../../../04-tools)
  - [Usimamizi wa Kikao](../../../04-tools)
  - [Udhibiti wa Makosa](../../../04-tools)
- [Zana Zinazopatikana](../../../04-tools)
- [Wakati wa Kutumia Wakala wa Zana](../../../04-tools)
- [Zana dhidi ya RAG](../../../04-tools)
- [Hatua Zifuatazo](../../../04-tools)

## Utajifunza Nini

Hadi sasa, umejifunza jinsi ya kuwa na mazungumzo na AI, kupanga maelekezo kwa ufanisi, na kuimarisha majibu katika nyaraka zako. Lakini bado kuna kizuizi muhimu: mifano ya lugha inaweza tu kuzaa maandishi. Haiwezi kuangalia hali ya hewa, kufanya hesabu, kuuliza kwenye hifadhidata, au kuingiliana na mifumo ya nje.

Zana hubadilisha hili. Kwa kumpa mfano ufikiaji wa kazi anazoweza kuitisha, unamgeuza kutoka kwa kizalishaji cha maandishi kuwa wakala anayeweza kuchukua hatua. Mfano huamua linapohitaji zana gani, zana ipi itumike, na ni vigezo gani vitapitishwa. Msimbo wako hufanya kazi na kuirudisha matokeo. Mfano hujumuisha matokeo hayo katika jibu lake.

## Mahitaji ya Awali

- Kumaliza Moduli 01 (Rasilimali za Azure OpenAI zimewekwa)
- Faili `.env` katika saraka kuu yenye nyaraka za Azure (iliyoanzishwa na `azd up` katika Moduli 01)

> **Kumbuka:** Ikiwa hujakamilisha Moduli 01, fuata maelekezo ya uanzishaji hapo kwanza.

## Kuelewa Wakala wa AI wenye Zana

> **📝 Kumbuka:** Neno "wakala" katika moduli hii linamaanisha wasaidizi wa AI walioboreshwa na uwezo wa kuitisha zana. Hii ni tofauti na mifumo ya **Agentic AI** (wakala huru wenye upangaji, kumbukumbu, na akili ya hatua nyingi) tutakayozungumzia katika [Moduli 05: MCP](../05-mcp/README.md).

Bila zana, mfano wa lugha unaweza tu kuzalisha maandishi kutoka kwa mafunzo yake. Uliza hali ya hewa ya sasa, basi hujaribu tu. Mpe zana, na anaweza kuitisha API ya hali ya hewa, kufanya hesabu, au kuuliza katika hifadhidata — kisha kuweka matokeo halisi katika jibu lake.

<img src="../../../translated_images/sw/what-are-tools.724e468fc4de64da.webp" alt="Bila Zana dhidi ya Kuwa na Zana" width="800"/>

*Bila zana mfano hujaribu tu — kwa zana anaweza kuitisha API, kufanya hesabu, na kurudisha data ya wakati halisi.*

Wakala wa AI wenye zana hufuata mfumo wa **Kufikiri na Kutenda (ReAct)**. Mfano haujibu tu — anafikiria kinachohitaji, hutenda kwa kuitisha zana, hatazamaji matokeo, na kisha huaamua kama atarudia au kutoa jibu la mwisho:

1. **Fikiria** — Wakala anachambua swali la mtumiaji na kubaini taarifa anazohitaji
2. **Tenda** — Wakala huchagua zana sahihi, huunda vigezo sahihi, na huitisha
3. **Tazama** — Wakala anapokea matokeo ya zana na kuthibitisha
4. **Rudia au Jibu** — Ikiwa data zaidi inahitajika, hujirudia; vinginevyo, huunda jibu la lugha ya asili

<img src="../../../translated_images/sw/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Mfumo wa ReAct" width="800"/>

*Mzunguko wa ReAct — wakala hufikiri kinachotakiwa kufanya, hutenda kwa kuitisha zana, hutazama matokeo, na kurudia mpaka kutoa jibu la mwisho.*

Hii hutokea moja kwa moja. Unaeleza zana na maelezo yao. Mfano hushughulikia uamuzi lini na jinsi ya kuzitumia.

## Jinsi Kufanya Kitozi cha Zana Kazi

### Maelezo ya Zana

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Unaeleza kazi na maelezo wazi pamoja na vigezo. Mfano hufahamu maelezo haya kwenye maelekezo yake ya mfumo na kuelewa kila zana hufanya nini.

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

// Msaidizi ameunganishwa kiotomatiki na Spring Boot na:
// - Kifungu cha ChatModel
// - Njia zote za @Tool kutoka kwa madarasa ya @Component
// - ChatMemoryProvider kwa usimamizi wa kikao
```

Mchoro chini unaelezea kila alama na jinsi kila sehemu husaidia AI kuelewa lini kuitisha zana na vigezo vya kupitisha:

<img src="../../../translated_images/sw/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Miundo ya Maelezo ya Zana" width="800"/>

*Miundo ya maelezo ya zana — @Tool inasema AI lini itumike, @P inaeleza kila kigezo, na @AiService huunganisha yote wakati wa kuanzisha.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) na uliza:
> - "Nawezaje kuingiza API halisi ya hali ya hewa kama OpenWeatherMap badala ya data ya mfano?"
> - "Nini kinachoifanya maelezo ya zana kuwa mazuri ili kusaidia AI kuitumia kwa usahihi?"
> - "Ninashughulikiaje makosa ya API na vikwazo vya idadi ya maombi katika utekelezaji wa zana?"

### Uamuzi

Mara mtumiaji anauliza "Hali ya hewa Seattle ikoje?", mfano hachagui zana kiholela. Hunlinganisha nia ya mtumiaji dhidi ya maelezo ya kila zana anazopata, hupanga alama kwa umuhimu, na huchagua bora zaidi. Kisha huunda wito wa kazi ulio na vigezo vinavyofaa — katika hili, kuweka `location` kuwa `"Seattle"`.

Ikiwa hakuna zana inayolingana na maombi, mfano hurudi kujibu kwa maarifa yake mwenyewe. Ikiwa zana nyingi zinahusiana, huchagua zana yenye maelezo maalum zaidi.

<img src="../../../translated_images/sw/decision-making.409cd562e5cecc49.webp" alt="Jinsi AI Huuamua Zana Gani Itumike" width="800"/>

*Mfano hutathmini zana zote zinazopatikana dhidi ya nia ya mtumiaji na huchagua bora zaidi — ndio maana maelezo ya zana kuwa wazi na maalum ni muhimu.*

### Utekelezaji

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot huunganisha moja kwa moja interface ya `@AiService` yenye zana zote zilizosajiliwa, na LangChain4j hufanya miito ya zana moja kwa moja. Nyuma ya pazia, mchakato kamili wa miito ya zana huenda hatua sita — kuanzia swali la lugha ya asili la mtumiaji hadi jibu la lugha ya asili:

<img src="../../../translated_images/sw/tool-calling-flow.8601941b0ca041e6.webp" alt="Mtiririko wa Miito ya Zana" width="800"/>

*Mtiririko kamili — mtumiaji anauliza swali, mfano huchagua zana, LangChain4j hufanya kazi, na mfano hujumuisha matokeo katika jibu la asili.*

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) na uliza:
> - "Mfumo wa ReAct hufanya kazi vipi na kwa nini ni mzuri kwa wakala wa AI?"
> - "Wakala huchagua zana gani kutumika na kwa mpangilio gani?"
> - "Nini hutokea kama utekelezaji wa zana dhaifu - jinsi gani nashughulikia makosa vyema?"

### Uundaji wa Jibu

Mfano hupokea data ya hali ya hewa na kuiunda kuwa jibu la lugha ya asili kwa mtumiaji.

### Miundo: Spring Boot Auto-Wiring

Moduli hii inatumia muunganisho wa LangChain4j na Spring Boot kwa interface za `@AiService` zenye tamko. Wakati wa kuanzisha Spring Boot hugundua kila `@Component` yenye mbinu za `@Tool`, bean yako ya `ChatModel`, na `ChatMemoryProvider` — kisha huziunganisha zote kwa interface moja ya `Assistant` bila njia za ziada za mikono.

<img src="../../../translated_images/sw/spring-boot-wiring.151321795988b04e.webp" alt="Miundo ya Spring Boot Auto-Wiring" width="800"/>

*Interface ya @AiService inaunganisha ChatModel, vipengele vya zana, na mtoaji kumbukumbu — Spring Boot huweka waya zote moja kwa moja.*

Manufaa muhimu ya njia hii:

- **Spring Boot auto-wiring** — ChatModel na zana huingizwa moja kwa moja
- **Mfumo wa @MemoryId** — Usimamizi wa kumbukumbu wa kikao kiotomatiki
- **Kipindi kimoja** — Msaidizi huundwa mara moja na kutumika tena kwa utendaji bora
- **Utekelezaji salama kwa aina** — Mbinu za Java huitwa moja kwa moja kwa uongofu wa aina
- **Msimamizi wa mizunguko mingi** — Hushughulikia mnyororo wa zana kiotomatiki
- **Hakuna njia za ziada** — Hakuna wito wa mkono wa `AiServices.builder()` au ramani za kumbukumbu `HashMap`

Njia mbadala (mkono `AiServices.builder()`) zinahitaji msimbo zaidi na hazileti faida za muunganiko wa Spring Boot.

## Mnyororo wa Zana

**Mnyororo wa Zana** — Nguvu halisi ya wakala wa zana huonekana wakati swali moja linahitaji zana nyingi. Uliza "Hali ya hewa Seattle ikoje kwa Fahrenheit?" na wakala huwa mnyororo wa zana mbili moja kwa moja: kwanza huitisha `getCurrentWeather` kupata joto kwa Celsius, kisha hupitisha thamani hiyo kwa `celsiusToFahrenheit` kwa mabadiliko — yote hii katika mzunguko mmoja wa mazungumzo.

<img src="../../../translated_images/sw/tool-chaining-example.538203e73d09dd82.webp" alt="Mfano wa Mnyororo wa Zana" width="800"/>

*Mnyororo wa zana ukiwa kivitendo — wakala huanza na getCurrentWeather, kisha hupitisha matokeo ya Celsius kwa celsiusToFahrenheit, na kutoa jibu lililounganishwa.*

Hivi ndivyo inavyoonekana katika programu inayoendesha — wakala hunyorosha miito miwili ya zana katika mzunguko mmoja:

<a href="images/tool-chaining.png"><img src="../../../translated_images/sw/tool-chaining.3b25af01967d6f7b.webp" alt="Mnyororo wa Zana" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Matokeo halisi ya programu — wakala hufanya mnyororo wa getCurrentWeather → celsiusToFahrenheit katika mzunguko mmoja.*

**Mafanikio kwa Hali ya Amani** — Uliza hali ya hewa katika mji usipo kwenye data ya mfano. Zana hreturna ujumbe wa kosa, na AI inaeleza haiwezi kusaidia badala ya kuanguka. Zana hushindwa kwa usalama.

<img src="../../../translated_images/sw/error-handling-flow.9a330ffc8ee0475c.webp" alt="Mtiririko wa Kudhibiti Makosa" width="800"/>

*Wakati zana inashindwa, wakala hunasa kosa na kujibu kwa maelezo yenye msaada badala ya kuanguka.*

Hii hutokea katika mzunguko mmoja wa mazungumzo. Wakala huepuka makosa kwa kuendesha miito mingi ya zana kiotomatiki.

## Endesha Programu

**Thibitisha uanzishaji:**

Hakikisha faili `.env` ipo katika saraka kuu yenye nyaraka za Azure (iliyoanzishwa wakati wa Moduli 01):
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

> **Kumbuka:** Ikiwa tayari umeanzisha programu zote kwa kutumia `./start-all.sh` kutoka Moduli 01, moduli hii tayari inaendesha kwenye bandari 8084. Unaweza kuruka amri za kuanzisha hapa chini na kwenda moja kwa moja http://localhost:8084.

**Chaguo 1: Kutumia Dashibodi ya Spring Boot (Inapendekezwa kwa watumiaji wa VS Code)**

Konteina ya maendeleo ina kiendelezi cha Spring Boot Dashboard kinachotoa kiolesura cha kuona na kusimamia programu zote za Spring Boot. Unaweza kukipata kwenye Ukanda wa Shughuli upande wa kushoto wa VS Code (tazama ikoni ya Spring Boot).

Kutoka Dashibodi ya Spring Boot, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye mazingira ya kazi
- Anzisha/zaimisha programu kwa bonyeza mara moja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fuata hali ya programu

Bonyeza tu kitufe cha kucheza kando ya "tools" kuanzisha moduli hii, au anzisha moduli zote kwa pamoja.

<img src="../../../translated_images/sw/dashboard.9b519b1a1bc1b30a.webp" alt="Dashibodi ya Spring Boot" width="400"/>

**Chaguo 2: Kutumia script za shell**

Anzisha programu zote za wavuti (moduli 01-04):

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

Script zote mbili hupakua vigezo vya mazingira kutoka faili `.env` ya mizizi kiotomatiki na zitajenga JARs ikiwa hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote mwenyewe kabla ya kuanzisha:
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

**Kusitisha:**

**Bash:**
```bash
./stop.sh  # Hii moduli tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Hii moduli tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Kutumia Programu

Programu hutoa kiolesura cha wavuti ambacho unaweza kuwasiliana na wakala wa AI aliyepata zana za hali ya hewa na mabadiliko ya joto.

<a href="images/tools-homepage.png"><img src="../../../translated_images/sw/tools-homepage.4b4cd8b2717f9621.webp" alt="Kiolesura cha Zana za Wakala wa AI" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Kiolesura cha Zana za Wakala wa AI - mifano ya haraka na kiolesura cha gumzo cha kuwasiliana na zana*

### Jaribu Matumizi Rahisi ya Zana
Anza na ombi rahisi: "Badilisha 100 digrii Fahrenheit kuwa Celsius". Mwakilishi anatambua anahitaji zana ya kubadilisha joto, anaitumia kwa vigezo sahihi, na kurudisha matokeo. Kumbuka jinsi hii inahisi kuwa ya kawaida - hukufafanua ni zana gani kutumia au jinsi ya kuitumia.

### Jaribu Muunganiko wa Zana

Sasa jaribu kitu kigumu zaidi: "Hali ya hewa iko Seattle na ibadilishe kuwa Fahrenheit?" Tazama jinsi mwakilishi anavyofanya kazi kwa hatua. Kwanza hupata hali ya hewa (ambayo hurudisha Celsius), hutambua anahitaji kubadilisha hadi Fahrenheit, anaitumia zana ya kubadilisha, na kuunganisha matokeo yote kuwa jibu moja.

### Tazama Mtiririko wa Mazungumzo

Kiolesura cha mazungumzo huhifadhi historia ya mazungumzo, kikiruhusu kuzungumza kwa mizunguko mingi. Unaweza kuona maswali na majibu yote ya awali, kurahisisha kufuatilia mazungumzo na kuelewa jinsi mwakilishi anavyojenga muktadha kupitia mizunguko mingi.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/sw/tools-conversation-demo.89f2ce9676080f59.webp" alt="Mazungumzo na Miito ya Zana Nyingi" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Mazungumzo ya mizunguko mingi yanaonyesha ubadilishaji rahisi, utafutaji wa hali ya hewa, na muunganiko wa zana*

### Jaribu Maombi Tofauti

Jaribu mchanganyiko mbalimbali:
- Utafutaji wa hali ya hewa: "Hali ya hewa iko Tokyo?"
- Ubabadilishaji wa joto: "Je, 25°C ni sawa na Kelvin ngapi?"
- Maswali mchanganyiko: "Angalia hali ya hewa Paris na niambie kama ni zaidi ya 20°C"

Tazama jinsi mwakilishi anavyotafsiri lugha ya kawaida na kuibadilisha kwa miito inayofaa ya zana.

## Misingi Muhimu

### Mfano wa ReAct (Kufikiria na Kutenda)

Mwakilishi hubadilishana kati ya kufikiria (kuamua cha kufanya) na kutenda (kutumia zana). Mfano huu unaruhusu kutatua matatizo kwa kujitegemea badala ya kujibu tu maagizo.

### Maelezo ya Zana ni Muhimu

Ubora wa maelezo ya zana unahusiana moja kwa moja na jinsi mwakilishi anazitumia. Maelezo wazi na mahususi husaidia mfano kuelewa lini na jinsi ya kuitumia kila zana.

### Usimamizi wa Kikao

Maelezo ya `@MemoryId` huruhusu usimamizi wa kumbukumbu ya kikao moja kwa moja. Kila kitambulisho cha kikao hupata mfano wake wa `ChatMemory` unasimamiwa na bean ya `ChatMemoryProvider`, hivyo watumiaji wengi wanaweza kuingiliana na mwakilishi bila mizunguko yao kuchanganyika.

<img src="../../../translated_images/sw/session-management.91ad819c6c89c400.webp" alt="Usimamizi wa Kikao na @MemoryId" width="800"/>

*Kila kitambulisho cha kikao kina ramani ya historia ya mazungumzo pekee — watumiaji hawaoni ujumbe wa wengine.*

### Usimamizi wa Makosa

Zana zinaweza kushindwa — APIs hupoteza muda, vigezo vinaweza kuwa batili, huduma za nje zinaweza kushindwa. Mawakili wa uzalishaji wanahitaji usimamizi wa makosa ili mfano uweze kueleza matatizo au kujaribu mbadala badala ya kuzima programu nzima. Zana inapozua hitilafu, LangChain4j huimkia na kuwarudishia ujumbe wa kosa kwa mfano, ambao unaweza kisha kueleza tatizo kwa lugha ya kawaida.

## Zana Zinazopatikana

Mchoro hapo chini unaonyesha mfumo mpana wa zana unazoweza kujenga. Moduli hii inaonyesha zana za hali ya hewa na joto, lakini mfano ule ule wa `@Tool` hufanya kazi kwa njia yoyote ya Java — kutoka kwa maswali ya hifadhidata hadi usindikaji wa malipo.

<img src="../../../translated_images/sw/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Mfumo wa Zana" width="800"/>

*Kila njia ya Java iliyotiwa alama na @Tool inapatikana kwa AI — mfano huu unaendelezwa na hifadhidata, APIs, barua pepe, uendeshaji wa faili, na zaidi.*

## Lini Kutumia Mawakili Wenye Zana

<img src="../../../translated_images/sw/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Lini Kutumia Zana" width="800"/>

*Mwongozo wa maamuzi ya haraka — zana ni kwa data ya wakati halisi, mahesabu, na vitendo; maarifa ya jumla na kazi za ubunifu hazihitaji zana.*

**Tumia zana wakati:**
- Kujibu kuna data ya wakati halisi (hali ya hewa, bei za hisa, hesabu)
- Unahitaji kufanya mahesabu zaidi ya hisabati rahisi
- Kupata hifadhidata au APIs
- Kufanya vitendo (kutuma barua pepe, kuunda tiketi, kusasisha rekodi)
- Kuunganisha vyanzo mbalimbali vya data

**Usitumi zana wakati:**
- Maswali yanaweza kujibiwa kwa maarifa ya jumla
- Jibu ni kwa mazungumzo tu
- Muda wa majibu ya zana ungefanya uzoefu kuwa polepole sana

## Zana vs RAG

Moduli 03 na 04 zote zinaongeza kile AI inaweza kufanya, lakini kwa njia tofauti kabisa. RAG humruhusu mfano kufikia **maarifa** kwa kupata nyaraka. Zana humruhusu mfano kuchukua **vitendo** kwa kuita michakato.

<img src="../../../translated_images/sw/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Mlinganiko wa Zana vs RAG" width="800"/>

*RAG hupata taarifa kutoka kwa nyaraka tulivu — Zana hufanya vitendo na kuleta data halisi, yenye mabadiliko. Mifumo mingi ya uzalishaji huunganisha zote mbili.*

Kwa vitendo, mifumo mingi ya uzalishaji huunganisha mbinu zote mbili: RAG kwa kufafanua majibu katika nyaraka zako, na Zana kwa kupata data ya moja kwa moja au kufanya shughuli.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [05-mcp - Itifaki ya Muktadha wa Mfano (MCP)](../05-mcp/README.md)

---

**Uvinjari:** [← Ya Awali: Moduli 03 - RAG](../03-rag/README.md) | [Rudi Kwenye Mwanzo](../README.md) | [Ifuatayo: Moduli 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kutokujali**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Wakati tunajitahidi kufanikisha usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Hati asilia katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatuwajibiki kwa kueleweshwa vibaya au ufafanuzi usio sahihi unaotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->