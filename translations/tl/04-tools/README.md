# Module 04: AI Agents na may Mga Tool

## Table of Contents

- [Ano ang Matututuhan Mo](../../../04-tools)
- [Mga Kinakailangan](../../../04-tools)
- [Pag-unawa sa AI Agents na may Mga Tool](../../../04-tools)
- [Paano Gumagana ang Pagtawag ng Tool](../../../04-tools)
  - [Mga Depinisyon ng Tool](../../../04-tools)
  - [Pagbuo ng Desisyon](../../../04-tools)
  - [Pagpapatupad](../../../04-tools)
  - [Pagbuo ng Tugon](../../../04-tools)
  - [Arkitektura: Spring Boot Auto-Wiring](../../../04-tools)
- [Pagsasangkot ng Mga Tool](../../../04-tools)
- [Patakbuhin ang Aplikasyon](../../../04-tools)
- [Paggamit ng Aplikasyon](../../../04-tools)
  - [Subukan ang Simpleng Paggamit ng Tool](../../../04-tools)
  - [Subukan ang Pagsasangkot ng Mga Tool](../../../04-tools)
  - [Tingnan ang Daloy ng Usapan](../../../04-tools)
  - [Magsagawa ng Eksperimento sa Iba't Ibang Kahilingan](../../../04-tools)
- [Mga Pangunahing Konsepto](../../../04-tools)
  - [ReAct Pattern (Pangangatwiran at Paggawa)](../../../04-tools)
  - [Mahalaga ang Mga Paglalarawan ng Tool](../../../04-tools)
  - [Pamamahala ng Session](../../../04-tools)
  - [Paghawak ng Error](../../../04-tools)
- [Mga Available na Tool](../../../04-tools)
- [Kailan Gamitin ang Mga Agent na Base sa Tool](../../../04-tools)
- [Mga Tools vs RAG](../../../04-tools)
- [Mga Susunod na Hakbang](../../../04-tools)

## Ano ang Matututuhan Mo

Sa ngayon, natutunan mo kung paano makipag-usap sa AI, maayos na istruktura ng mga prompt, at paglagyan ng pundasyon ang mga sagot gamit ang iyong mga dokumento. Ngunit may isang pangunahing limitasyon pa rin: ang mga language model ay makakagawa lamang ng teksto. Hindi nila kayang suriin ang panahon, magsagawa ng kalkulasyon, mag-query ng mga database, o kumonekta sa mga panlabas na sistema.

Binabago ito ng mga tool. Sa pagbibigay ng access sa model sa mga function na maaari nitong tawagan, pinapalitan mo ito mula sa pagiging tagabuo ng teksto tungo sa isang agent na maaaring kumilos. Pinipili ng model kung kailan kailangan nito ng tool, aling tool ang gagamitin, at anong mga parameter ang ipapasa. Ipinapatupad ng iyong code ang function at ibinabalik ang resulta. Isinasama ng model ang resulta sa kanyang sagot.

## Mga Kinakailangan

- Nakumpleto ang [Module 01 - Panimula](../01-introduction/README.md) (na-deploy na ang mga Azure OpenAI resources)
- Inirerekomendang nakumpleto ang mga naunang module (tumutukoy ang module na ito sa [RAG concepts mula sa Module 03](../03-rag/README.md) sa paghahambing ng Tools vs RAG)
- `.env` file sa root directory na may Azure credentials (nilikha ng `azd up` sa Module 01)

> **Tandaan:** Kung hindi mo pa natatapos ang Module 01, sundin muna ang mga tagubilin sa deployment doon.

## Pag-unawa sa AI Agents na may Mga Tool

> **📝 Tandaan:** Ang salitang "agents" sa module na ito ay tumutukoy sa mga AI assistant na pinalakas gamit ang kakayahang tumawag ng mga tool. Naiiba ito sa mga **Agentic AI** patterns (mga autonomous agent na may plano, memorya, at multi-step reasoning) na tatalakayin natin sa [Module 05: MCP](../05-mcp/README.md).

Kung walang mga tool, ang language model ay makakagawa lamang ng teksto mula sa training data nito. Kung tatanungin mo tungkol sa kasalukuyang panahon, huhulaan lang nito. Kung bibigyan mo ng mga tool, maaari nitong tawagan ang isang weather API, magsagawa ng kalkulasyon, o mag-query ng database — tapos bisa nitong isama ang tunay na resulta sa kanyang sagot.

<img src="../../../translated_images/tl/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Kung walang mga tool, huhulaan lang ng model — gamit ang mga tool, makatawag ito ng API, magsagawa ng kalkulasyon, at magbigay ng real-time na datos.*

Ang AI agent na may mga tool ay sumusunod sa **Reasoning and Acting (ReAct)** pattern. Hindi lang basta sumasagot ang model — pinag-iisipan nito kung ano ang kailangan, kumikilos sa pamamagitan ng pagtawag sa tool, sinusuri ang resulta, at pagkatapos ay nagpapasya kung uulitin pa o magbibigay na ng huling sagot:

1. **Makatuwiran** — Inaanalisa ng agent ang tanong ng gumagamit at tinitiyak kung anong impormasyon ang kailangan
2. **Kumilos** — Pinipili ng agent ang tamang tool, binubuo ang tamang mga parameter, at tinatawagan ito
3. **Obserbahan** — Tinatanggap ng agent ang output ng tool at sinusuri ang resulta
4. **Ulitin o Tumugon** — Kung kailangan pa ng karagdagang data, bumabalik ang agent; kung hindi, bumubuo ito ng sagot sa natural na wika

<img src="../../../translated_images/tl/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*Ang siklo ng ReAct — pinag-iisipan ng agent kung ano ang gagawin, kumikilos sa pagtawag ng tool, tinatanaw ang resulta, at inuulit hanggang makapagbigay ng huling sagot.*

Awtomatikong nangyayari ito. Ikaw ang nagde-define ng mga tool at ng mga paglalarawan nito. Ang model ang humahawak sa pagbuo ng desisyon kung kailan at paano gamitin ang mga ito.

## Paano Gumagana ang Pagtawag ng Tool

### Mga Depinisyon ng Tool

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Nagde-define ka ng mga function na may malinaw na mga paglalarawan at mga espesipikasyon ng parameter. Nakikita ng model ang mga paglalarawang ito sa kanyang system prompt at naiintindihan kung ano ang ginagawa ng bawat tool.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Ang iyong lohika para sa pagtingin ng panahon
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Ang Assistant ay awtomatikong nakakabit ng Spring Boot sa:
// - ChatModel bean
// - Lahat ng @Tool na mga pamamaraan mula sa mga @Component na klase
// - ChatMemoryProvider para sa pamamahala ng sesyon
```

Ipinapaliwanag ng diagram sa ibaba ang bawat anotasyon at ipinapakita kung paano nakakatulong ang bawat bahagi upang maintindihan ng AI kung kailan tatawagin ang tool at anong mga argumento ang ipapasa:

<img src="../../../translated_images/tl/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomiya ng depinisyon ng tool — sinasabi ng @Tool sa AI kung kailan ito gagamitin, inilalarawan ng @P ang bawat parameter, at ina-wire ng @AiService ang lahat pag-startup.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) at itanong:
> - "Paano ko isasama ang totoong weather API tulad ng OpenWeatherMap imbes na mock data?"
> - "Ano ang wastong paglalarawan ng tool na tumutulong sa AI na gamitin ito nang tama?"
> - "Paano ko haharapin ang mga API errors at rate limits sa implementations ng tool?"

### Pagbuo ng Desisyon

Kapag tinanong ang "Ano ang panahon sa Seattle?", hindi basta random na pumipili ang model ng tool. Kinukumpara nito ang intensyon ng gumagamit laban sa bawat paglalarawan ng tool na mayroon ito, binibigay ang marka ng kaugnayan, at pinipili ang pinakamainam. Pagkatapos nito, bumubuo ito ng nakaistrukturang function call na may tamang mga parameter — sa kasong ito, itinatakda ang `location` sa `"Seattle"`.

Kung walang tool na tugma sa kahilingan ng gumagamit, bumabalik ang model sa pagsagot base sa sariling kaalaman nito. Kung maraming tugma, pinipili ang pinaka-especific na tool.

<img src="../../../translated_images/tl/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*Sinusuri ng model ang bawat available na tool batay sa intensyon ng gumagamit at pinipili ang pinakamahusay — kaya mahalagang gumawa ng malinaw at espesipikong paglalarawan ng tool.*

### Pagpapatupad

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Awtomatikong ini-wire ng Spring Boot ang deklaratibong `@AiService` interface sa lahat ng nairehistrong mga tool, at awtomatikong pinapatupad ng LangChain4j ang pagtawag sa mga tool. Sa likod nito, dumadaan ang buong pagtawag ng tool sa anim na yugto — mula sa natural na tanong ng gumagamit hanggang sa natural na sagot:

<img src="../../../translated_images/tl/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*Ang end-to-end flow — nagtatanong ang user, pumipili ang model ng tool, pinapatupad ng LangChain4j ito, at isinasama ng model ang resulta sa sagot.*

Kung pinatakbo mo ang [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) sa Module 00, nakita mo na ang pattern na ito sa aksyon — tinawag sa parehong paraan ang mga `Calculator` tool. Ipinapakita ng sequence diagram sa ibaba ang mga nangyari sa likod nito:

<img src="../../../translated_images/tl/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Ang loop ng pagtawag ng tool mula sa Quick Start demo — pinapadala ng `AiServices` ang iyong mensahe at mga schema ng tool sa LLM, sumasagot ang LLM ng function call tulad ng `add(42, 58)`, pinapatupad ng LangChain4j ang `Calculator` method locally, at ibinabalik ang resulta para sa huling sagot.*

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) at itanong:
> - "Paano gumagana ang ReAct pattern at bakit epektibo ito para sa AI agents?"
> - "Paano pinipili ng agent kung anong tool ang gagamitin at sa anong pagkakasunod?"
> - "Ano ang nangyayari kung pumalpak ang pagpapatupad ng tool - paano dapat robust na harapin ang mga error?"

### Pagbuo ng Tugon

Tinatanggap ng model ang datos ng panahon at ini-format ito bilang isang natural na sagot para sa gumagamit.

### Arkitektura: Spring Boot Auto-Wiring

Ginagamit ng module na ito ang Spring Boot integration ng LangChain4j na may deklaratibong `@AiService` interfaces. Sa pagsisimula, natutuklasan ng Spring Boot ang bawat `@Component` na may mga `@Tool` method, ang iyong `ChatModel` bean, at ang `ChatMemoryProvider` — pagkatapos ay ini-wire ang lahat sa isang `Assistant` interface nang walang manu-manong boilerplate.

<img src="../../../translated_images/tl/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*Pinagsasama ng @AiService interface ang ChatModel, mga tool component, at memory provider — awtomatikong inaasikaso ng Spring Boot ang wiring.*

Narito ang buong lifecycle ng kahilingan bilang sequence diagram — mula sa HTTP request, controller, service, at auto-wired proxy, hanggang sa pagpapatupad ng tool at pabalik:

<img src="../../../translated_images/tl/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Ang kumpletong lifecycle ng kahilingan sa Spring Boot — dumadaan ang HTTP request sa controller at service papunta sa auto-wired Assistant proxy, na inaayos ang LLM at pagtawag sa mga tool nang awtomatiko.*

Mga pangunahing benepisyo ng pamamaraang ito:

- **Spring Boot auto-wiring** — awtomatikong ini-inject ang ChatModel at mga tool  
- **@MemoryId pattern** — awtomatikong pamamahala ng memory sa session  
- **Isang instansya lang** — assistant na nilikha isang beses at muling ginagamit para sa mas mahusay na performance  
- **Type-safe na pagpapatupad** — Java methods na tinatawag direkta na may type conversion  
- **Multi-turn orchestration** — awtomatikong humahawak sa pagsasangkot ng mga tool  
- **Walang boilerplate** — walang manu-manong `AiServices.builder()` calls o memory HashMap  

Mas maraming code ang kinakailangan at nawawala ang mga benepisyo ng Spring Boot integration sa mga alternatibong pamamaraan (manu-manong `AiServices.builder()`).

## Pagsasangkot ng Mga Tool

**Pagsasangkot ng Tool** — Ang tunay na lakas ng mga agent na base sa tool ay nakikita kapag ang isang tanong ay nangangailangan ng maraming tool. Kung tanungin mo "Ano ang panahon sa Seattle sa Fahrenheit?" awtomatikong nagsasangkot ang agent ng dalawang tool: una, tinatawagan nito ang `getCurrentWeather` para makuha ang temperatura sa Celsius, tapos ipinapasa ang resulta sa `celsiusToFahrenheit` para sa conversion — lahat ito sa isang ikot ng pag-uusap.

<img src="../../../translated_images/tl/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Ang pagsasangkot ng tool sa aksyon — unang tinatawagan ng agent ang getCurrentWeather, pagkatapos ay ipinapasa ang Celsius na resulta sa celsiusToFahrenheit, at naghahatid ng pinagsamang sagot.*

**Maginhawang Pagkabigo** — Kapag nagtanong tungkol sa panahon sa lungsod na wala sa mock data, nagbabalik ang tool ng mensahe ng error, at ipinaliwanag ng AI na hindi nito kaya magbigay ng tulong sa halip na mag-crash. Ligtas ang pagkabigo ng mga tool. Itong diagram ay nagpapakita ng paghahambing ng dalawang paraan — sa tamang paghawak ng error, nahuhuli ng agent ang exception at nagbibigay ng makatulong na sagot, habang kung wala nito ay nagka-crash ang buong aplikasyon:

<img src="../../../translated_images/tl/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Kapag pumalpak ang tool, nahuhuli ng agent ang error at tumutugon nang may makatulong na paliwanag sa halip na mag-crash.*

Nangyayari ito sa isang pag-ikot ng pag-uusap. Awtomatikong inaayos ng agent ang maraming pagtawag sa tool.

## Patakbuhin ang Aplikasyon

**Suriin ang deployment:**

Tiyaking may `.env` file sa root directory na may Azure credentials (nilikha noong Module 01). Patakbuhin ito mula sa module directory (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang aplikasyon:**

> **Tandaan:** Kung sinimulan mo na ang lahat ng aplikasyon gamit ang `./start-all.sh` mula sa root directory (tulad ng inilalarawan sa Module 01), ang module na ito ay tumatakbo na sa port 8084. Maaari mong laktawan ang mga start commands sa ibaba at direktang pumunta sa http://localhost:8084.

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot aplikasyon. Makikita mo ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang icon ng Spring Boot).

Mula sa Spring Boot Dashboard, maaari kang:
- Makita ang lahat ng available na Spring Boot application sa workspace  
- Simulan/patigilin ang mga aplikasyon sa isang click lang  
- Tingnan ang mga log ng application nang real-time  
- Subaybayan ang status ng aplikasyon  

I-click lang ang play button sa tabi ng "tools" para simulan ang module na ito, o patakbuhin nang sabay-sabay ang lahat ng module.

Ganito ang hitsura ng Spring Boot Dashboard sa VS Code:

<img src="../../../translated_images/tl/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard sa VS Code — simulan, patigilin, at subaybayan ang lahat ng module mula sa isang lugar*

**Opsyon 2: Paggamit ng shell scripts**

Simulan ang lahat ng web application (mga module 01-04):

**Bash:**
```bash
cd ..  # Mula sa ugat na direktoryo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa root na direktoryo
.\start-all.ps1
```

O simulan lamang ang module na ito:

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

Parehong awtomatikong naglo-load ng mga environment variable mula sa root `.env` file ang mga script at bubuuin ang mga JAR kung wala pa ang mga ito.

> **Tandaan:** Kung mas gusto mong manu-manong buuin lahat ng mga module bago magsimula:
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

Buksan ang http://localhost:8084 sa iyong browser.

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Para lang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng isang web interface kung saan maaari kang makipag-ugnayan sa isang AI agent na may access sa mga tool para sa panahon at pagkonvert ng temperatura. Ganito ang hitsura ng interface — kasama ang mga mabilisang halimbawa at panel ng chat para sa pagpapadala ng mga kahilingan:

<a href="images/tools-homepage.png"><img src="../../../translated_images/tl/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Ang AI Agent Tools interface - mabilisang mga halimbawa at chat interface para sa pakikipag-ugnayan sa mga tool*

### Subukan ang Simpleng Paggamit ng Tool

Magsimula sa isang simpleng kahilingan: "I-convert ang 100 degrees Fahrenheit sa Celsius". Nakikilala ng agent na kailangan niya ang tool para sa pagkonvert ng temperatura, tinatawagan ito gamit ang tamang mga parameter, at ibinabalik ang resulta. Pansinin kung gaano ito likas ang pakiramdam - hindi mo kinailangang tukuyin kung aling tool ang gagamitin o paano ito tatawagin.

### Subukan ang Pagsunod-sunod ng Tool

Subukang gawin ang isang mas kumplikadong tanong: "Ano ang panahon sa Seattle at i-convert ito sa Fahrenheit?" Panoorin ang agent na gawin ito ng hakbang-hakbang. Una niyang kinukuha ang panahon (na nagbabalik ng Celsius), nakikilala na kailangan niyang i-convert sa Fahrenheit, tinatawagan ang tool ng conversion, at pinagsasama ang parehong resulta sa isang sagot.

### Tingnan ang Daloy ng Usapan

Pinapanatili ng chat interface ang kasaysayan ng usapan, na nagpapahintulot sa iyo na magkaroon ng multi-turn na pakikipag-ugnayan. Makikita mo ang lahat ng mga naunang tanong at sagot, na nagpapadali upang subaybayan ang usapan at maunawaan kung paano bumubuo ng konteksto ang agent sa maraming palitan.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/tl/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Multi-turn na usapan na nagpapakita ng simpleng mga conversion, pagtingin ng panahon, at pagsunod ng tool*

### Mag-eksperimento sa Iba’t Ibang Mga Kahilingan

Subukan ang iba’t ibang kumbinasyon:
- Mga pagtingin sa panahon: "Ano ang panahon sa Tokyo?"
- Mga conversion ng temperatura: "Ano ang 25°C sa Kelvin?"
- Pagsamahin na mga tanong: "Suriin ang panahon sa Paris at sabihin sa akin kung ito ay higit sa 20°C"

Pansinin kung paano ini-interpret ng agent ang natural na wika at iniaangkop ito sa tamang pagtawag ng mga tool.

## Pangunahing Mga Konsepto

### ReAct Pattern (Pangangatwiran at Pagkilos)

Nagpapalitan ang agent sa pagitan ng pag-iisip (pagdedesisyon kung ano ang gagawin) at pagkilos (paggamit ng mga tool). Pinapayagan ng pattern na ito ang autonomous na paglutas ng problema sa halip na simpleng pagsagot sa mga utos.

### Mahalaga ang Mga Deskripsyon ng Tool

Ang kalidad ng mga deskripsyon ng iyong mga tool ay direktang nakakaapekto kung gaano kahusay gamitin ito ng agent. Malinaw at tiyak na mga deskripsyon ang tumutulong sa modelo na maintindihan kung kailan at paano tatawagin ang bawat tool.

### Pamamahala ng Session

Pinapagana ng anotasyong `@MemoryId` ang awtomatikong pamamahala ng memorya batay sa session. Ang bawat session ID ay nakakakuha ng sarili nitong `ChatMemory` instance na pinamamahalaan ng `ChatMemoryProvider` bean, kaya maraming gumagamit ang maaaring makipag-ugnayan sa agent nang sabay-sabay nang hindi nagsasalangan ang kanilang mga usapan. Ipinapakita ng sumusunod na diagram kung paano niruruta ang maraming gumagamit sa mga hiwalay na memory store batay sa kanilang session IDs:

<img src="../../../translated_images/tl/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Ang bawat session ID ay nagmamapa sa isang hiwalay na kasaysayan ng pag-uusap — hindi kailanman nakikita ng mga gumagamit ang mga mensahe ng isa't isa.*

### Paghandle ng Error

Maaaring pumalpak ang mga tool — mag-timeout ang mga API, maaaring mali ang mga parameter, bumagsak ang mga external na serbisyo. Kailangan ng production agents ang paghandle ng error upang maipaliwanag ng modelo ang mga problema o subukan ang mga alternatibo sa halip na pabagsakin ang buong aplikasyon. Kapag naisagawa ng isang tool ang isang exception, hinuhuli ito ng LangChain4j at ipinapasok ang mensahe ng error pabalik sa modelo, na maaaring ipaliwanag ang problema sa natural na wika.

## Mga Available na Tool

Ipinapakita ng diagram sa ibaba ang malawak na ecosystem ng mga tool na maaari mong buuin. Ipinapakita ng module na ito ang mga tool para sa panahon at temperatura, ngunit ang parehong `@Tool` na pattern ay gumagana para sa anumang Java method — mula sa mga query sa database hanggang sa pagproseso ng pagbabayad.

<img src="../../../translated_images/tl/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Anumang Java method na may anotasyong @Tool ay nagiging available sa AI — pinalalawak ang pattern sa mga database, API, email, mga operasyon sa file, at iba pa.*

## Kailan Gagamit ng Mga Tool-Based na Agent

Hindi lahat ng kahilingan ay nangangailangan ng mga tool. Nakasalalay ang desisyon kung kailangan ba ng AI na makipag-ugnayan sa mga external na sistema o kaya ay sasagot mula sa sariling kaalaman. Pinagsasama-sama sa sumusunod na gabay kung kailan may halaga ang mga tool at kailan hindi kinakailangan:

<img src="../../../translated_images/tl/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Isang mabilis na gabay sa desisyon — para sa real-time na data, mga kalkulasyon, at aksyon ang mga tool; hindi kinakailangan para sa pangkalahatang kaalaman at malikhaing gawain.*

## Mga Tool kumpara sa RAG

Parehong pinapalawak ng Mga Module 03 at 04 ang kaya ng AI, ngunit sa mga pangunahing magkaibang paraan. Binibigyan ng RAG ang modelo ng access sa **kaalaman** sa pamamagitan ng pagkuha ng mga dokumento. Ang mga Tool naman ay nagbibigay sa modelo ng kakayahang gumawa ng **mga aksyon** sa pamamagitan ng pagtawag ng mga function. Ikinukumpara ng diagram sa ibaba ang dalawang paraang ito nang magkatabi — mula sa kung paano gumagana ang bawat workflow hanggang sa mga trade-offs sa pagitan nila:

<img src="../../../translated_images/tl/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*Kinukuha ng RAG ang impormasyon mula sa mga static na dokumento – Ang Mga Tool ay nagsasagawa ng mga aksyon at kumukuha ng dynamic, real-time na data. Maraming production system ang pinagsasama pareho.*

Sa pagsasanay, maraming production system ang pinagsasama ang parehong mga pamamaraan: RAG para mapagtibay ang mga sagot gamit ang iyong dokumentasyon, at Mga Tool para kumuha ng live na data o magsagawa ng mga operasyon.

## Susunod na Mga Hakbang

**Susunod na Module:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Navigation:** [← Nakaraan: Module 03 - RAG](../03-rag/README.md) | [Bumalik sa Pangunahing Pahina](../README.md) | [Susunod: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami na maging tumpak, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-katumpakan. Ang orihinal na dokumento sa kanyang sariling wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->