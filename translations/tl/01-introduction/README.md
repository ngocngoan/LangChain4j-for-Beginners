# Module 01: Pagsisimula sa LangChain4j

## Table of Contents

- [Pagsusuri sa Video](../../../01-introduction)
- [Ano ang Matututunan Mo](../../../01-introduction)
- [Mga Kinakailangan](../../../01-introduction)
- [Pag-unawa sa Pangunahing Suliranin](../../../01-introduction)
- [Pag-unawa sa Tokens](../../../01-introduction)
- [Paano Gumagana ang Memorya](../../../01-introduction)
- [Paano Ito Gumagamit ng LangChain4j](../../../01-introduction)
- [I-deploy ang Azure OpenAI Infrastructure](../../../01-introduction)
- [Patakbuhin ang Aplikasyon nang Lokal](../../../01-introduction)
- [Paggamit ng Aplikasyon](../../../01-introduction)
  - [Stateless Chat (Kaliwang Panel)](../../../01-introduction)
  - [Stateful Chat (Kanan Panel)](../../../01-introduction)
- [Mga Susunod na Hakbang](../../../01-introduction)

## Video Walkthrough

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula sa module na ito:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

Sa mabilis na pagsisimula, ginamit mo ang GitHub Models upang magpadala ng mga prompt, tumawag ng mga tool, bumuo ng RAG pipeline, at subukan ang mga guardrails. Ipinakita ng mga demo na iyon ang mga posibilidad — ngayon ay lilipat tayo sa Azure OpenAI at GPT-5.2 at magsisimulang bumuo ng mga aplikasyon na parang production. Ang module na ito ay nakatuon sa conversational AI na nakakatanda ng konteksto at nagpapanatili ng estado — mga konseptong ginamit ng mga quick start demos ngunit hindi ipinaliwanag.

Gagamitin natin ang Azure OpenAI's GPT-5.2 sa buong gabay na ito dahil ang mga advanced na kakayahan nito sa pangangatwiran ay nagpapakita nang malinaw ng mga pag-uugali ng iba't ibang pattern. Kapag nagdagdag ka ng memorya, makikita mo nang malinaw ang pagkakaiba. Mas madali itong maintindihan kung ano ang idinudulot ng bawat bahagi sa iyong aplikasyon.

Gagawa ka ng isang aplikasyon na nagpapakita ng parehong pattern:

**Stateless Chat** - Bawat kahilingan ay independyente. Walang alaala ang modelo ng mga naunang mensahe. Ito ang pattern na ginamit mo sa quick start.

**Stateful Conversation** - Kasama sa bawat kahilingan ang kasaysayan ng pag-uusap. Pinananatili ng modelo ang konteksto sa maraming pag-ikot. Ito ang kailangan ng mga production application.

## Prerequisites

- Azure subscription na may access sa Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Nakainstall na ang Java, Maven, Azure CLI at Azure Developer CLI (azd) sa ipinakitang devcontainer.

> **Note:** Ginagamit ng module na ito ang GPT-5.2 sa Azure OpenAI. Ang deployment ay awtomatikong naka-configure gamit ang `azd up` - huwag baguhin ang pangalan ng modelo sa code.

## Understanding the Core Problem

Ang mga language model ay stateless. Bawat API call ay independyente. Kung magpapadala ka ng "My name is John" at pagkatapos ay magtatanong ng "What's my name?", wala itong ideya na ipinakilala mo na ang iyong pangalan. Itinuturing nitong bawat kahilingan ay parang una mong pag-uusap.

Ayos lang ito para sa simpleng tanong at sagot pero useless para sa tunay na aplikasyon. Kailangan ng mga customer service bots na maalala ang sinasabi mo. Kailangan ng mga personal assistant ng konteksto. Ang anumang multi-turn na pag-uusap ay nangangailangan ng memorya.

Ang sumusunod na diagram ay nagpapakita ng kaibahan ng dalawang pamamaraan — sa kaliwa, isang stateless call na nakakalimot ng iyong pangalan; sa kanan, isang stateful call gamit ang ChatMemory na nakakatanda nito.

<img src="../../../translated_images/tl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Pagkakaiba ng stateless (independiyenteng tawag) at stateful (may kontekstong pag-uusap)*

## Understanding Tokens

Bago tumalon sa mga pag-uusap, mahalagang maintindihan ang tokens - ang mga pangunahing yunit ng teksto na pinoproseso ng mga language model:

<img src="../../../translated_images/tl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Halimbawa kung paano hinahati ang teksto sa mga tokens - "I love AI!" ay nahahati sa 4 na magkakahiwalay na yunit*

Ang mga tokens ang sukatan at proseso ng AI models sa teksto. Puwedeng maging tokens ang mga salita, bantas, at kahit mga spaces. May limitasyon ang iyong modelo kung ilang mga tokens ang pwede nitong iproseso nang sabay (400,000 para sa GPT-5.2, na may hanggang 272,000 input tokens at 128,000 output tokens). Ang pag-unawa sa tokens ay tumutulong sa pamamahala ng haba ng pag-uusap at mga gastos.

## How Memory Works

Nilulutas ng chat memory ang problemang stateless sa pamamagitan ng pagpapanatili ng kasaysayan ng pag-uusap. Bago ipadala ang iyong kahilingan sa modelo, ang framework ay inilalagay muna ang mga relevant na naunang mensahe. Kapag tinanong mo "What's my name?", ipinapadala ng sistema ang buong kasaysayan ng pag-uusap, kaya nakikita ng modelo na sinabi mo kanina "My name is John."

Nagbibigay ang LangChain4j ng mga implementasyon ng memory na nag-aasikaso nito nang awtomatiko. Pinipili mo kung ilang mga mensahe ang itatago at inaasikaso ng framework ang context window. Ipinapakita ng diagram sa ibaba kung paano pinapanatili ng MessageWindowChatMemory ang sliding window ng mga kamakailang mensahe.

<img src="../../../translated_images/tl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*Pinapanatili ng MessageWindowChatMemory ang sliding window ng mga kamakailang mensahe, awtomatikong tine-tanggal ang mga luma*

## How This Uses LangChain4j

Pinapalawig ng module na ito ang quick start sa pamamagitan ng pagsasama ng Spring Boot at pagdagdag ng conversation memory. Ganito ang pagsasama-sama ng mga bahagi:

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

Binabasa ng builder ang mga kredensyal mula sa environment variables na nakaset ng `azd up`. Ang pagtatakda ng `baseUrl` sa iyong Azure endpoint ay nagagamit ang OpenAI client sa Azure OpenAI.

**Conversation Memory** - Subaybayan ang chat history gamit ang MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Gumawa ng memorya gamit ang `withMaxMessages(10)` para itago ang huling 10 mensahe. Magdagdag ng user at AI messages gamit ang typed wrappers: `UserMessage.from(text)` at `AiMessage.from(text)`. Kunin ang history gamit ang `memory.messages()` at ipadala ito sa modelo. Ang serbisyo ay nagtatalaga ng hiwalay na memory instance bawat conversation ID, na nagpapahintulot sa maraming user na mag-chat nang sabay-sabay.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) at itanong:
> - "Paano pinipili ng MessageWindowChatMemory kung aling mga mensahe ang tatanggalin kapag puno na ang window?"
> - "Puwede ba akong mag-implement ng custom memory storage gamit ang database imbes na in-memory?"
> - "Paano ako magdagdag ng summarization para i-compress ang lumang kasaysayan ng pag-uusap?"

Ang stateless chat endpoint ay hindi gumagamit ng memorya - `chatModel.chat(prompt)` lang tulad ng quick start. Ang stateful endpoint ay nagdaragdag ng mga mensahe sa memorya, kumukuha ng kasaysayan, at isinasama ang kontekstong iyon sa bawat kahilingan. Parehong configuration ng modelo, magkaibang mga pattern.

## Deploy Azure OpenAI Infrastructure

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

> **Note:** Kung makatagpo ka ng timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), patakbuhin lang muli ang `azd up`. Maaaring nagpo-provision pa ang mga Azure resources sa background, at ang muling pagsubok ay nagpapahintulot sa deployment na matapos kapag naabot na ng mga resources ang terminal state.

Ito ay:
1. Mag-deploy ng Azure OpenAI resource kasama ang GPT-5.2 at text-embedding-3-small models
2. Awtomatikong gagawa ng `.env` file sa root ng proyekto na may mga kredensyal
3. Magse-set up ng lahat ng kinakailangang environment variables

**May problema ba sa deployment?** Tingnan ang [Infrastructure README](infra/README.md) para sa detalyadong troubleshooting tulad ng mga conflict sa pangalan ng subdomain, manwal na steps ng deployment sa Azure Portal, at gabay sa configuration ng modelo.

**Siguraduhin na matagumpay ang deployment:**

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

> **Note:** Ang `azd up` command ay awtomatikong gumagawa ng `.env` file. Kung kailangan mo itong i-update mamaya, maaari mong i-edit ang `.env` file nang manu-mano o i-regenerate ito gamit ang:
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

## Run the Application Locally

**Siguraduhing matagumpay ang deployment:**

Tiyakin na mayroon ang `.env` file sa root directory na may Azure credentials. Patakbuhin ito mula sa module directory (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang mga aplikasyon:**

**Option 1: Gamit ang Spring Boot Dashboard (Inirerekomenda sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual interface para pamahalaan ang lahat ng Spring Boot na aplikasyon. Makikita ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang Spring Boot icon).

Mula sa Spring Boot Dashboard, maaari mong:
- Makita lahat ng available na Spring Boot applications sa workspace
- Simulan/hintuin ang mga aplikasyon sa isang click lamang
- Tingnan ang mga logs ng aplikasyon nang real-time
- Bantayan ang status ng aplikasyon

I-click lang ang play button sa tabi ng "introduction" para simulan ang module na ito, o simulan lahat ng modules ng sabay.

<img src="../../../translated_images/tl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard sa VS Code — simulan, hintuin, at bantayan ang lahat ng module mula sa isang lugar*

**Option 2: Gamit ang shell scripts**

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

Awtomatikong niloload ng parehong script ang environment variables mula sa root `.env` file at ibubuo ang mga JAR kung wala pa.

> **Note:** Kung nais mong manu-manong i-build lahat ng modules bago magsimula:
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

**Para huminto:**

**Bash:**
```bash
./stop.sh  # Module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Tanging module na ito lamang
# O
cd ..; .\stop-all.ps1  # Lahat ng mga module
```

## Using the Application

Nagbibigay ang aplikasyon ng web interface na may dalawang chat implementations na magkatabi.

<img src="../../../translated_images/tl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard na nagpapakita ng parehong Simple Chat (stateless) at Conversational Chat (stateful) na mga opsyon*

### Stateless Chat (Kaliwang Panel)

Subukan mo ito muna. Sabihin ang "My name is John" at pagkatapos ay agad itanong "What's my name?" Hindi ito makatatanda dahil bawat mensahe ay independyente. Ipinapakita nito ang pangunahing problema sa basic na integration ng language model - walang konteksto ng pag-uusap.

<img src="../../../translated_images/tl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Hindi natatandaan ng AI ang iyong pangalan mula sa nakaraang mensahe*

### Stateful Chat (Kanan Panel)

Ngayon subukan mo ang parehong pagkakasunod dito. Sabihin ang "My name is John" at pagkatapos ay "What's my name?" Ngayon ay natatandaan nito. Ang pagkakaiba ay ang MessageWindowChatMemory - pinananatili nito ang kasaysayan ng pag-uusap at isinama ito sa bawat kahilingan. Ganito gumagana ang production conversational AI.

<img src="../../../translated_images/tl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Natandaaan ng AI ang iyong pangalan mula sa nakaraang pag-uusap*

Parehong GPT-5.2 ang ginagamit ng dalawang panel. Ang tanging pinagkaiba ay memorya. Ginagawa nitong malinaw kung ano ang idinudulot ng memorya sa iyong aplikasyon at bakit ito mahalaga para sa tunay na paggamit.

## Next Steps

**Next Module:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagaman sinisikap naming maging tumpak, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tumpak na bahagi. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->