# Module 04: Mga Ahente ng AI na may Mga Kasangkapan

## Talaan ng Nilalaman

- [Ano ang Matututuhan Mo](../../../04-tools)
- [Mga Kinakailangan](../../../04-tools)
- [Pag-unawa sa AI Agents na may Mga Kasangkapan](../../../04-tools)
- [Paano Gumagana ang Pagtawag ng Kasangkapan](../../../04-tools)
  - [Mga Kahulugan ng Kasangkapan](../../../04-tools)
  - [Paggawa ng Desisyon](../../../04-tools)
  - [Pagpapatupad](../../../04-tools)
  - [Paggawa ng Tugon](../../../04-tools)
  - [Arkitektura: Spring Boot Auto-Wiring](../../../04-tools)
- [Pagkakadena ng Kasangkapan](../../../04-tools)
- [Patakbuhin ang Aplikasyon](../../../04-tools)
- [Paggamit ng Aplikasyon](../../../04-tools)
  - [Subukan ang Simpleng Paggamit ng Kasangkapan](../../../04-tools)
  - [Subukan ang Pagkakadena ng Kasangkapan](../../../04-tools)
  - [Tingnan ang Daloy ng Usapan](../../../04-tools)
  - [Eksperimento sa Iba't ibang Kahilingan](../../../04-tools)
- [Pangunahing Konsepto](../../../04-tools)
  - [ReAct Pattern (Pangangatwiran at Pagkilos)](../../../04-tools)
  - [Mahalaga ang mga Paglalarawan ng Kasangkapan](../../../04-tools)
  - [Pamamahala ng Sesyon](../../../04-tools)
  - [Pagsalo ng Error](../../../04-tools)
- [Mga Magagamit na Kasangkapan](../../../04-tools)
- [Kailan Gumamit ng Mga Ahenteng Batay sa Kasangkapan](../../../04-tools)
- [Mga Kasangkapan vs RAG](../../../04-tools)
- [Mga Susunod na Hakbang](../../../04-tools)

## Ano ang Matututuhan Mo

Sa ngayon, natutunan mo kung paano makipag-usap sa AI, maayos na istruktura ng mga prompt, at talagang i-ugnay ang mga tugon sa iyong mga dokumento. Ngunit mayroon pa ring isang pangunahing limitasyon: ang mga modelo ng wika ay makakalikha lamang ng teksto. Hindi nila kayang suriin ang panahon, magsagawa ng mga kalkulasyon, mag-query ng mga database, o makipag-ugnayan sa mga panlabas na sistema.

Binabago ito ng mga kasangkapan. Sa pagbibigay ng access sa model ng mga function na maaaring tawagan, ginagawa mong isang ahente na kayang kumilos ang modelo, mula sa pagiging tagagawa lang ng teksto. Pinipili ng modelo kung kailan kailangan nito ng kasangkapan, alin ang gagamitin, at anong mga parameter ang ipapasa. Ang iyong code ang nagpapatupad ng function at ibinabalik ang resulta. Isinasama ng modelo ang resulta sa kanyang tugon.

## Mga Kinakailangan

- Natapos ang [Module 01 - Panimula](../01-introduction/README.md) (nakadeploy na ang Azure OpenAI resources)
- Inirerekomendang natapos ang mga naunang module (tinutukoy ng module na ito ang [mga konsepto ng RAG mula sa Module 03](../03-rag/README.md) sa paghahambing ng Mga Kasangkapan vs RAG)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Tandaan:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubiling pang-deploy doon.

## Pag-unawa sa AI Agents na may Mga Kasangkapan

> **📝 Tandaan:** Ang salitang "agents" sa module na ito ay tumutukoy sa mga AI assistant na pinalakas ng kakayahang tumawag ng mga kasangkapan. Iba ito sa mga pattern ng **Agentic AI** (autonomous agents na may planning, memory, at multi-step reasoning) na tatalakayin natin sa [Module 05: MCP](../05-mcp/README.md).

Kung walang mga kasangkapan, ang isang language model ay kaya lamang gumawa ng teksto mula sa mga data ng pagsasanay nito. Tanungin ito tungkol sa kasalukuyang panahon, at huhulaan lamang nito. Bigyan ito ng mga kasangkapan, at kaya nitong tumawag ng weather API, magsagawa ng kalkulasyon, o mag-query ng database — saka isasama ang totoong resulta sa kanyang tugon.

<img src="../../../translated_images/tl/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Kung walang kasangkapan, hula lang ang modelo — pero kapag may kasangkapan, kaya nitong tumawag ng mga API, magsagawa ng mga kalkulasyon, at magbalik ng real-time na data.*

Ang isang AI agent na may mga kasangkapan ay sumusunod sa **Reasoning and Acting (ReAct)** pattern. Hindi lang basta sumasagot ang modelo — pinag-iisipan nito kung ano ang kailangan, kumikilos sa pamamagitan ng pagtawag ng kasangkapan, tinitingnan ang resulta, at nagpapasya kung kikilos muli o magbibigay ng huling sagot:

1. **Reason** — Sinusuri ng ahente ang tanong ng user at tinutukoy kung anong impormasyon ang kailangan  
2. **Act** — Pinipili ng ahente ang tamang kasangkapan, ginagawa ang tamang mga parameter, at tinatawagan ito  
3. **Observe** — Tinatanggap ng ahente ang output ng kasangkapan at sinusuri ang resulta  
4. **Repeat or Respond** — Kung kailangan pa ng karagdagang datos, uulit ang proseso; kung hindi, bumubuo ng natural na sagot

<img src="../../../translated_images/tl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*Ang siklo ng ReAct — pinag-iisipan ng ahente kung ano ang gagawin, kumikilos sa pagtawag ng kasangkapan, tinitingnan ang resulta, at inuulit hanggang makapagbigay ng huling sagot.*

Ito ay awtomatikong nangyayari. Iyong tinutukoy ang mga kasangkapan at ang kanilang mga paglalarawan. Ang modelo ang humahawak ng paggawa ng desisyon kung kailan at paano gagamitin ang mga ito.

## Paano Gumagana ang Pagtawag ng Kasangkapan

### Mga Kahulugan ng Kasangkapan

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Nagtatakda ka ng mga function na may malinaw na paglalarawan at espesipikasyon ng mga parameter. Nakikita ng modelo ang mga paglalarawang ito sa sistema nitong prompt at naiintindihan kung ano ang ginagawa ng bawat kasangkapan.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Ang iyong lohika sa pagtingin ng panahon
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Ang Assistant ay awtomatikong ikinakabit ng Spring Boot gamit ang:
// - Bean ng ChatModel
// - Lahat ng @Tool na mga pamamaraan mula sa mga klase na may @Component
// - ChatMemoryProvider para sa pamamahala ng session
```
  
Ang diagram sa ibaba ay naghahati-hati sa bawat anotasyon at ipinapakita kung paano nakakatulong ang bawat bahagi para maunawaan ng AI kung kailan tatawagin ang kasangkapan at anong mga argumento ang ipapasa:

<img src="../../../translated_images/tl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomiya ng isang kahulugan ng kasangkapan — ang @Tool ay nagsasabi sa AI kung kailan ito gagamitin, ang @P ay naglalarawan ng bawat parameter, at ang @AiService ay nag-uugnay sa lahat sa simula ng pagpapatakbo.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) at itanong:  
> - "Paano ko isasama ang totoong weather API tulad ng OpenWeatherMap sa halip na mock data?"  
> - "Ano ang nagpapaganda sa paglalarawan ng kasangkapan na tumutulong sa AI na gamitin ito nang tama?"  
> - "Paano ako humahawak ng mga error sa API at rate limits sa implementasyon ng kasangkapan?"

### Paggawa ng Desisyon

Kapag tinanong ng user ng "Ano ang lagay ng panahon sa Seattle?", hindi basta randomly pumipili ang modelo ng kasangkapan. Kinukumpara nito ang intensyon ng user laban sa lahat ng paglalarawan ng kasangkapan na mayroon ito, binibigyan ng iskor ang bawat isa para sa kaugnayan, at pinipili ang pinakaangkop. Gumagawa ito ng nakaistrukturang tawag sa function na may tamang mga parameter — sa kasong ito, itinakda ang `location` sa `"Seattle"`.

Kung wala namang kagyat na kasangkapan na tumutugma sa kahilingan ng user, bumabalik ang modelo sa pagsagot mula sa sarili nitong kaalaman. Kapag may maraming kasangkapan na tumutugma, pinipili ang pinaka-tiyak.

<img src="../../../translated_images/tl/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Sinusuri ng modelo ang bawat magagamit na kasangkapan laban sa intensyon ng user at pinipili ang pinakaangkop — ito ang dahilan kung bakit mahalaga ang pagsusulat ng malinaw at tiyak na mga paglalarawan ng kasangkapan.*

### Pagpapatupad

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Awtomatikong ini-inject ng Spring Boot ang declarative na `@AiService` interface kasama ang lahat ng nairehistrong kasangkapan, at LangChain4j ang nagpapatupad ng mga tawag sa kasangkapan nang awtomatiko. Sa likod ng eksena, dumadaan ang isang kumpletong tawag sa kasangkapan sa anim na yugto — mula sa natural na tanong ng user hanggang sa natural na sagot:

<img src="../../../translated_images/tl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Ang end-to-end na daloy — nagtatanong ang user, pumipili ang modelo ng kasangkapan, pinapatakbo ito ng LangChain4j, at isinisingit ng modelo ang resulta sa natural na tugon.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) at itanong:  
> - "Paano gumagana ang ReAct pattern at bakit ito epektibo para sa mga AI agent?"  
> - "Paano nagdedesisyon ang ahente kung aling kasangkapan ang gagamitin at sa anong pagkakasunod?"  
> - "Ano ang nangyayari kung pumalya ang pagpapatupad ng kasangkapan - paano ko haharapin nang matibay ang mga error?"

### Paggawa ng Tugon

Tinatanggap ng modelo ang datos ng panahon at inaayos ito para maging natural na tugon para sa user.

### Arkitektura: Spring Boot Auto-Wiring

Gamit ng module na ito ang LangChain4j na Spring Boot integration gamit ang declarative `@AiService` interfaces. Sa pagsisimula, natutuklasan ng Spring Boot ang bawat `@Component` na naglalaman ng mga `@Tool` methods, ang iyong `ChatModel` bean, at ang `ChatMemoryProvider` — saka ini-inject ang mga ito lahat sa isang `Assistant` interface nang walang manual na boilerplate.

<img src="../../../translated_images/tl/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*Ang @AiService interface ay nag-uugnay sa ChatModel, mga bahagi ng kasangkapan, at memory provider — awtomatikong hinahandle ng Spring Boot ang lahat ng wiring.*

Pangunahing benepisyo ng ganitong paraan:

- **Spring Boot auto-wiring** — awtomatikong naiinject ang ChatModel at mga kasangkapan  
- **@MemoryId pattern** — awtomatikong pamamahala ng session-based memory  
- **Isang instansya lang** — Assistant ay ginagawa nang isang beses at muling ginagamit para sa mas mahusay na performance  
- **Type-safe execution** — direktang tinatawag ang mga Java method na may type conversion  
- **Multi-turn orchestration** — awtomatikong naghahandle ng tool chaining  
- **Walang boilerplate** — walang manual na `AiServices.builder()` calls o memory HashMap

Kailangan ng mga alternatibong approach (manwal na `AiServices.builder()`) ng mas maraming code at wala ito sa mga benepisyo ng Spring Boot integration.

## Pagkakadena ng Kasangkapan

**Pagkakadena ng Kasangkapan** — Lumalabas ang tunay na kapangyarihan ng mga ahenteng nakadepende sa kasangkapan kapag ang isang tanong ay nangangailangan ng maraming kasangkapan. Kung tatanungin ng "Ano ang lagay ng panahon sa Seattle sa Fahrenheit?", awtomatikong magkakadena ng dalawang kasangkapan ang ahente: una nitong tatawagin ang `getCurrentWeather` para makuha ang temperatura sa Celsius, tapos ipapasa ang halagang iyon sa `celsiusToFahrenheit` para i-convert — lahat nang nasa isang turn ng usapan.

<img src="../../../translated_images/tl/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Pagkakadena ng kasangkapan sa aksyon — unang tinatawagan ng ahente ang getCurrentWeather, saka pinapasa ang resulta sa Celsius papuntang celsiusToFahrenheit, at naghahatid ng pinagsamang sagot.*

**Maayos na Pagkabigo** — Kapag humingi ng lagay ng panahon sa isang lungsod na wala sa mock data, nagbabalik ang kasangkapan ng mensahe ng error, at ipinaliwanag ng AI na hindi ito makakatulong sa halip na mag-crash. Ligtas na bumabagsak ang mga kasangkapan. Ang diagram sa ibaba ay nagpapakita ng pagkakaiba ng dalawang paraan — kapag maayos ang pagsalo ng error, nahuhuli ng ahente ang exception at tumutugon nang makakatulong, samantalang kapag wala, nagcr-crash nang buo ang aplikasyon:

<img src="../../../translated_images/tl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Kapag pumalya ang kasangkapan, nahuhuli ng ahente ang error at sumasagot ng paliwanag na makakatulong sa halip na mag-crash.*

Nangyayari ito sa loob ng isang turn ng usapan. Ang ahente ang nag-o-orchestrate ng maraming tawag sa kasangkapan nang mag-isa.

## Patakbuhin ang Aplikasyon

**Suriin ang deployment:**

Tiyaking may `.env` file sa root directory na may Azure credentials (nilikha noong Module 01). Patakbuhin ito mula sa direktoryo ng module (`04-tools/`):

**Bash:**  
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa root directory (tulad ng nakasaad sa Module 01), tumatakbo na ang module na ito sa port 8084. Maaari mo nang laktawan ang mga start command sa ibaba at pumunta diretso sa http://localhost:8084.

**Opsyon 1: Gamit ang Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot na aplikasyon. Makikita ito sa Activity Bar sa kaliwa ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari mong:  
- Tingnan lahat ng magagamit na Spring Boot application sa workspace  
- Simulan/hinto ang mga aplikasyon sa isang click lang  
- Tingnan ang mga log ng aplikasyon nang real-time  
- Subaybayan ang status ng aplikasyon

I-click lang ang play button katabi ng "tools" para simulan ang module na ito, o simulan lahat ng module nang sabay.

Ganito ang hitsura ng Spring Boot Dashboard sa VS Code:

<img src="../../../translated_images/tl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Ang Spring Boot Dashboard sa VS Code — simulan, itigil, at subaybayan ang lahat ng module mula sa isang lugar*

**Opsyon 2: Gamit ang mga shell script**

Simulan lahat ng web applications (modules 01-04):

**Bash:**  
```bash
cd ..  # Mula sa ugat ng direktoryo
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Mula sa ugat na direktoryo
.\start-all.ps1
```
  
O simulan lang ang module na ito:

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
  
Parehong awtomatikong naglo-load ng environment variables mula sa root `.env` file ang mga script at mag-build ng mga JAR kung wala pa.

> **Tandaan:** Kung nais mo munang manu-manong i-build lahat ng module bago simulan:  
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  

Buksan ang http://localhost:8084 sa iyong browser.

**Para itigil:**

**Bash:**  
```bash
./stop.sh  # Sa module na ito lang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Ang module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```
  

## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng web interface kung saan maaari kang makipag-ugnayan sa AI agent na may access sa mga kasangkapang pangpanahon at pang-konbersyon ng temperatura. Ganito ang hitsura ng interface — may mga halimbawa para sa mabilisang pagsisimula at chat panel para sa pagpapadala ng mga kahilingan:
<a href="images/tools-homepage.png"><img src="../../../translated_images/tl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ang interface ng AI Agent Tools - mabilisang mga halimbawa at chat interface para makipag-interact sa mga tools*

### Subukan ang Simpleng Paggamit ng Tool

Magsimula sa isang simpleng kahilingan: "I-convert ang 100 degrees Fahrenheit sa Celsius". Nakikita ng agent na kailangan nito ang temperature conversion tool, tinatawag ito gamit ang tamang mga parameter, at ibinabalik ang resulta. Mapapansin mo kung gaano ito ka-natural - hindi mo kailangang tukuyin kung anong tool ang gagamitin o kung paano ito tatawagin.

### Subukan ang Pagsasama-samang Paggamit ng Tools

Ngayon subukan ang isang mas komplikadong bagay: "Ano ang lagay ng panahon sa Seattle at i-convert ito sa Fahrenheit?" Panoorin ang agent habang pinoproseso ito ng hakbang-hakbang. Una nitong kinukuha ang lagay ng panahon (na nagreresulta sa Celsius), nakikita na kailangan itong i-convert sa Fahrenheit, tinatawag ang conversion tool, at pinagsasama ang parehong resulta sa isang tugon.

### Tingnan ang Daloy ng Usapan

Pinananatili ng chat interface ang kasaysayan ng usapan, kaya maaari kang magkaroon ng multi-turn na mga interaksyon. Makikita mo ang lahat ng mga naunang tanong at sagot, na nagpapadali sa pagsubaybay sa usapan at pag-unawa kung paano bumubuo ng konteksto ang agent sa maraming palitan.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/tl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Multi-turn na usapan na nagpapakita ng simpleng mga conversion, pagtingin sa lagay ng panahon, at pagsasama-sama ng paggamit ng mga tools*

### Eksperimentuhin ang Iba't Ibang mga Kahilingan

Subukan ang iba't ibang kumbinasyon:
- Pagtingin sa lagay ng panahon: "Ano ang lagay ng panahon sa Tokyo?"
- Temperature conversions: "Ano ang 25°C sa Kelvin?"
- Pinagsamang mga query: "Suriin ang lagay ng panahon sa Paris at sabihin kung higit sa 20°C"

Mapapansin mo kung paano naiintindihan ng agent ang natural na wika at iniugnay ito sa angkop na mga pagtawag sa tool.

## Pangunahing mga Konsepto

### ReAct Pattern (Pag-iisip at Pagsasagawa)

Nagpapalit-palit ang agent sa pagitan ng pag-iisip (pagpapasya kung ano ang gagawin) at pagsasagawa (paggamit ng mga tools). Pinapagana ng pattern na ito ang autonomous na paglutas ng problema sa halip na simpleng pagsunod lang sa mga utos.

### Mahalaga ang Mga Paglalarawan ng Tool

Direktang naaapektuhan ng kalidad ng iyong mga paglalarawan ng tool kung gaano kahusay ito nagagamit ng agent. Malinaw at espesipikong mga paglalarawan ang tumutulong sa modelo na maintindihan kung kailan at paano tatawagin ang bawat tool.

### Pamamahala ng Session

Pinapagana ng anotasyong `@MemoryId` ang awtomatikong pamamahala ng memorya batay sa session. Bawat session ID ay may sariling `ChatMemory` instance na pinamahalaan ng `ChatMemoryProvider` bean, kaya maraming gumagamit ang maaaring makipag-interact sa agent nang sabay-sabay nang hindi nagkakahalo ang kanilang mga usapan. Ipinapakita ng sumusunod na diagram kung paano niruruta ang maraming mga gumagamit sa mga naka-isolasyon na tindahan ng memorya batay sa kanilang session IDs:

<img src="../../../translated_images/tl/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Bawat session ID ay tumutugma sa isang hiwalay na kasaysayan ng usapan — hindi nakikita ng mga user ang mga mensahe ng isa't isa.*

### Paghawak ng Mali

Maaaring pumalya ang mga tools — ma-timeout ang mga API, maaaring maging invalid ang mga parameter, bumagsak ang mga external na serbisyo. Nangangailangan ang mga produktong agent ng paghawak ng mali para maipaliwanag ng modelo ang mga problema o subukang mga alternatibo sa halip na masira ang buong aplikasyon. Kapag nag-throw ng exception ang isang tool, nahuhuli ito ng LangChain4j at ibinabalik ang mensahe ng error sa modelo, na maaari nang ipaliwanag ang problema gamit ang natural na wika.

## Mga Magagamit na Tools

Ipinapakita ng diagram sa ibaba ang malawak na ecosystem ng mga tools na maaari mong gawin. Ipinapakita ng module na ito ang weather at temperature tools, ngunit ang parehong `@Tool` pattern ay gumagana para sa anumang Java method — mula sa mga database query hanggang sa pagproseso ng bayad.

<img src="../../../translated_images/tl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Anumang Java method na may anotasyong @Tool ay nagiging magagamit sa AI — naaabot ng pattern ang mga database, API, email, file operations, at iba pa.*

## Kailan Gagamit ng Mga Tool-Based Agents

Hindi bawat kahilingan ay nangangailangan ng tools. Nakadepende ito kung kailangan ba ng AI na makipag-interact sa mga external na sistema o kaya ay kaya nitong sumagot mula sa sariling kaalaman. Pinagsasama-sama ng sumusunod na gabay kung kailan nagbibigay ng halaga ang tools at kung kailan ito hindi kailangan:

<img src="../../../translated_images/tl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Isang mabilis na gabay sa pagdedesisyon — ang tools ay para sa real-time na data, kalkulasyon, at mga aksyon; ang pangkalahatang kaalaman at malikhaing gawain ay hindi kinakailangan.*

## Tools vs RAG

Parehong pinapalawak ng Modules 03 at 04 ang magagawa ng AI, ngunit sa mga pundamental na magkaibang paraan. Binibigyan ng RAG ang modelo ng access sa **kaalaman** sa pamamagitan ng pagkuha ng mga dokumento. Binibigyan naman ng Tools ang modelo ng kakayahang gumawa ng **mga aksyon** sa pamamagitan ng pagtawag ng mga function. Inihahambing ng diagram sa ibaba ang dalawang approach na ito sa tabi-tabi — mula sa kung paano gumagana ang bawat workflow hanggang sa mga trade-offs sa pagitan nila:

<img src="../../../translated_images/tl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*Kinukuha ng RAG ang impormasyon mula sa mga statikong dokumento — nagsasagawa ang Tools ng mga aksyon at kumukuha ng dinamiko, real-time na data. Maraming production systems ang pinagsasama ang dalawa.*

Sa praktika, maraming production systems ang gumagamit ng parehong approach: RAG para magkaroon ng pundasyon ang sagot sa iyong dokumentasyon, at Tools para kumuha ng live data o magsagawa ng mga operasyon.

## Mga Susunod na Hakbang

**Susunod na Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Nakaraan: Module 03 - RAG](../03-rag/README.md) | [Bumalik sa Pangunahing Pahina](../README.md) | [Susunod: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagsasaalang-alang**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, pakatandaan na ang automated na pagsasalin ay maaaring maglaman ng mga pagkakamali o kamalian. Ang orihinal na dokumento sa kanyang wikang pinagmulan ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->