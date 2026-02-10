# Module 01: Pagsisimula sa LangChain4j

## Table of Contents

- [Ano ang Matututunan Mo](../../../01-introduction)
- [Mga Kinakailangan](../../../01-introduction)
- [Pag-unawa sa Pangunahing Suliranin](../../../01-introduction)
- [Pag-unawa sa Mga Token](../../../01-introduction)
- [Paano Gumagana ang Memorya](../../../01-introduction)
- [Paano Ito Gumagamit ng LangChain4j](../../../01-introduction)
- [I-deploy ang Azure OpenAI Infrastructure](../../../01-introduction)
- [Patakbuhin ang Aplikasyon nang Lokal](../../../01-introduction)
- [Paggamit ng Aplikasyon](../../../01-introduction)
  - [Stateless Chat (Kaliwang Panel)](../../../01-introduction)
  - [Stateful Chat (Kanang Panel)](../../../01-introduction)
- [Mga Susunod na Hakbang](../../../01-introduction)

## Ano ang Matututunan Mo

Kung natapos mo ang mabilisang simulang gabay, nakita mo kung paano magpadala ng prompts at tumanggap ng mga sagot. Iyan ang pundasyon, ngunit ang totoong mga aplikasyon ay nangangailangan ng higit pa. Tinuturuan ka ng module na ito kung paano bumuo ng conversational AI na nakakaalala ng konteksto at nagpapanatili ng estado - ang kaibahan ng isang one-off demo at isang production-ready na aplikasyon.

Gagamitin natin ang GPT-5.2 ng Azure OpenAI sa buong gabay na ito dahil ang advanced na kakayahan nito sa pangangatwiran ay nagpapalinaw ng pagkakaiba ng mga iba't ibang pattern. Kapag nagdagdag ka ng memorya, makikita mo nang malinaw ang pagkakaiba. Mas madaling maunawaan kung ano ang naidudulot ng bawat bahagi sa iyong aplikasyon.

Bubuuin mo ang isang aplikasyon na nagpapakita ng parehong pattern:

**Stateless Chat** - Bawat kahilingan ay independyente. Walang naaalala ang modelo tungkol sa mga naunang mensahe. Ito ang pattern na ginamit mo sa mabilisang simulang gabay.

**Stateful Conversation** - Kasama sa bawat kahilingan ang kasaysayan ng pag-uusap. Pinananatili ng modelo ang konteksto sa maraming pag-ikot. Ito ang kailangan ng mga production na aplikasyon.

## Mga Kinakailangan

- Azure subscription na may access sa Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Ang Java, Maven, Azure CLI, at Azure Developer CLI (azd) ay naka-install na sa ibinigay na devcontainer.

> **Note:** Ang module na ito ay gumagamit ng GPT-5.2 sa Azure OpenAI. Ang deployment ay awtomatikong nakasetup gamit ang `azd up` - huwag baguhin ang pangalan ng modelo sa code.

## Pag-unawa sa Pangunahing Suliranin

Ang mga language model ay stateless. Bawat API call ay independyente. Kung ipadala mo ang "My name is John" at pagkatapos ay itanong "What’s my name?", wala itong ideya na ipinakilala mo ang sarili mo lang. Tinatrato nito ang bawat kahilingan na parang ito ang unang pag-uusap mo kailanman.

Ayos lang ito para sa simpleng tanong at sagot pero hindi ito kapaki-pakinabang para sa mga totoong aplikasyon. Kailangan ng mga customer service bot na maalala ang sinabi mo sa kanila. Kailangan ng mga personal assistant ang konteksto. Ang anumang multi-turn na pag-uusap ay nangangailangan ng memorya.

<img src="../../../translated_images/tl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ang kaibahan ng stateless (mga independyenteng tawag) at stateful (may kamalayang konteksto) na mga pag-uusap*

## Pag-unawa sa Mga Token

Bago sumabak sa mga pag-uusap, mahalagang maunawaan ang mga token - ang mga pangunahing yunit ng teksto na pinoproseso ng mga language model:

<img src="../../../translated_images/tl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Halimbawa kung paano hinahati ang teksto sa mga token - ang "I love AI!" ay nagiging 4 na hiwalay na yunit ng pagproseso*

Ang mga token ang ginagamit ng mga AI model para sukatin at iproseso ang teksto. Maaari itong mga salita, mga bantas, at maging mga espasyo. May limitasyon ang iyong modelo kung ilang token ang kaya nitong iproseso nang sabay-sabay (400,000 para sa GPT-5.2, hanggang 272,000 input tokens at 128,000 output tokens). Ang pag-unawa sa mga token ay tumutulong sa'yo na pamahalaan ang haba ng pag-uusap at mga gastos.

## Paano Gumagana ang Memorya

Nilulutas ng chat memory ang problema ng pagiging stateless sa pamamagitan ng pagpapanatili ng kasaysayan ng pag-uusap. Bago mo ipadala ang iyong kahilingan sa modelo, ang framework ay inilalagay muna ang mga kaugnay na mga naunang mensahe. Kapag tinanong mo "What’s my name?", totoong ipinapadala ng sistema ang buong kasaysayan ng pag-uusap, kaya nakikita ng modelo na sinabi mo dati na "My name is John."

Nagbibigay ang LangChain4j ng mga implementasyon ng memorya na awtomatikong humahandle nito. Pinipili mo kung ilang mensahe ang ireretain at pinamamahalaan ng framework ang context window.

<img src="../../../translated_images/tl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*Pinapanatili ng MessageWindowChatMemory ang sliding window ng mga kamakailang mensahe, awtomatikong tinatanggal ang mga luma*

## Paano Ito Gumagamit ng LangChain4j

Pinalalawak ng module na ito ang mabilisang start sa pamamagitan ng integrasyon ng Spring Boot at pagdaragdag ng memorya sa pag-uusap. Ganito kumakabit ang mga bahagi:

**Dependencies** - Magdagdag ng dalawang LangChain4j libraries:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Chat Model** - I-configure ang Azure OpenAI bilang Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Binabasa ng builder ang mga kredensyal mula sa environment variables na itinakda ng `azd up`. Ang pagseset ng `baseUrl` sa iyong Azure endpoint ang nagpapaandar ng OpenAI client sa Azure OpenAI.

**Conversation Memory** - Subaybayan ang kasaysayan ng chat gamit ang MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Gumawa ng memory gamit ang `withMaxMessages(10)` para panatilihin ang huling 10 na mensahe. Magdagdag ng mga mensahe mula sa user at AI gamit ang typed wrappers: `UserMessage.from(text)` at `AiMessage.from(text)`. Kunin ang kasaysayan gamit ang `memory.messages()` at ipadala ito sa modelo. Naka-store ang serbisyo ng mga hiwalay na memory instance bawat conversation ID, na nagpapahintulot sa maraming user na makipag-chat nang sabay-sabay.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) at itanong:
> - "Paano pinagpapasya ng MessageWindowChatMemory kung aling mga mensahe ang tatanggalin kapag puno na ang window?"
> - "Pwede ba akong gumawa ng custom memory storage gamit ang database sa halip na in-memory?"
> - "Paano ako magdaragdag ng summarization para i-compress ang lumang kasaysayan ng pag-uusap?"

Ang stateless chat endpoint ay hindi gumagamit ng memorya - `chatModel.chat(prompt)` lang tulad ng quick start. Ang stateful endpoint ay nagdadagdag ng mga mensahe sa memorya, kumukuha ng kasaysayan, at isinasama ang konteksto sa bawat kahilingan. Parehong modelo pero magkaibang pattern.

## I-deploy ang Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # Piliin ang subscription at lokasyon (inirerekomenda ang eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Piliin ang subscription at lokasyon (inirerekomenda ang eastus2)
```

> **Note:** Kung makatagpo ka ng timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ulitin lang ang `azd up`. Posibleng nagpapatuloy pa ang provisioning ng Azure resources sa background, at kapag nag-retry, makukumpleto ang deployment kapag naabot ng mga resources ang terminal na estado.

Gagawin nito:
1. Ia-deploy ang Azure OpenAI resource na may GPT-5.2 at text-embedding-3-small na mga modelo
2. Awtomatikong gagawa ng `.env` file sa root ng proyekto na may mga kredensyal
3. Isaayos ang lahat ng kinakailangang environment variables

**May problema sa deployment?** Tingnan ang [Infrastructure README](infra/README.md) para sa detalyadong troubleshooting kabilang ang mga subdomain name conflicts, manwal na mga hakbang sa Azure Portal deployment, at gabay sa configuration ng modelo.

**Siguraduhing matagumpay ang deployment:**

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

> **Note:** Awtomatikong ginagawa ng `azd up` command ang `.env` file. Kung kailangan mong i-update ito pagkatapos, maaari mo itong i-edit nang manu-mano o i-regenerate sa pamamagitan ng:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Patakbuhin ang Aplikasyon nang Lokal

**Siguraduhing matagumpay ang deployment:**

Tiyaking nandiyan ang `.env` file sa root directory na may mga kredensyal ng Azure:

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang mga aplikasyon:**

**Opsyon 1: Gamit ang Spring Boot Dashboard (Inirerekomenda para sa mga VS Code users)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan lahat ng Spring Boot applications. Makikita mo ito sa Activity Bar sa kaliwang gilid ng VS Code (hanapin ang Spring Boot icon).

Sa Spring Boot Dashboard, maaari mong:
- Tingnan lahat ng available na Spring Boot applications sa workspace
- Simulan/hintuin ang mga aplikasyon gamit ang isang click lang
- Tingnan ang mga logs ng aplikasyon realtime
- I-monitor ang estado ng aplikasyon

I-click lang ang play button sa tabi ng "introduction" para simulan ang module na ito, o patakbuhin lahat ng module all at once.

<img src="../../../translated_images/tl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Gamit ang shell scripts**

Simulan lahat ng web applications (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa root na direktoryo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Mula sa root directory
.\start-all.ps1
```

O simulan lang ang module na ito:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Awtomatiko itong maglo-load ng environment variables mula sa root `.env` file at bubuuin ang mga JAR kung wala pa.

> **Note:** Kung nais mong manu-manong i-build lahat ng modules bago patakbuhin:
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

Buksan ang http://localhost:8080 sa iyong browser.

**Para ihinto:**

**Bash:**
```bash
./stop.sh  # Ang modulong ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Sa module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng web interface na may dalawang implementasyon ng chat na magkatabi.

<img src="../../../translated_images/tl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard na nagpapakita ng parehong Simple Chat (stateless) at Conversational Chat (stateful) na mga opsyon*

### Stateless Chat (Kaliwang Panel)

Subukan mo muna ito. Itanong ang "My name is John" at pagkatapos ay agad itanong ang "What’s my name?" Hindi nito maalala dahil independyente ang bawat mensahe. Ipinapakita nito ang pangunahing problema sa basic na integrasyon ng language model - walang konteksto ng pag-uusap.

<img src="../../../translated_images/tl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Hindi natatandaan ng AI ang iyong pangalan mula sa nakaraang mensahe*

### Stateful Chat (Kanang Panel)

Ngayon subukan mo ang parehong sekwensya dito. Itanong ang "My name is John" at pagkatapos "What’s my name?" Sa pagkakataong ito, natatandaan nito. Ang pagkakaiba ay ang MessageWindowChatMemory - pinananatili nito ang kasaysayan ng pag-uusap at isinasama ito sa bawat kahilingan. Ganito gumagana ang production conversational AI.

<img src="../../../translated_images/tl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Natatandaan ng AI ang iyong pangalan mula sa naunang pag-uusap*

Parehong GPT-5.2 ang ginagamit ng dalawang panel. Ang pagkakaiba lang ay memorya. Maliwanag dito kung ano ang naidudulot ng memorya sa iyong aplikasyon at kung bakit mahalaga ito sa totoong gamit.

## Mga Susunod na Hakbang

**Susunod na Module:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Nakaraan: Module 00 - Quick Start](../00-quick-start/README.md) | [Bumalik sa Main](../README.md) | [Susunod: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang serbisyong AI na pagsasalin [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsisikap kaming maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o kamalian. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling pantao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring mangyari mula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->