# Module 01: Pagsisimula sa LangChain4j

## Talaan ng Nilalaman

- [Paglalakad sa Video](../../../01-introduction)
- [Mga Matututunan Mo](../../../01-introduction)
- [Mga Kailangan Bago Magsimula](../../../01-introduction)
- [Pag-unawa sa Pangunahing Suliranin](../../../01-introduction)
- [Pag-unawa sa mga Token](../../../01-introduction)
- [Paano Gumagana ang Memorya](../../../01-introduction)
- [Paano Ito Gumagamit ng LangChain4j](../../../01-introduction)
- [I-deploy ang Azure OpenAI Infrastructure](../../../01-introduction)
- [Patakbuhin ang Aplikasyon nang Lokal](../../../01-introduction)
- [Paggamit ng Aplikasyon](../../../01-introduction)
  - [Stateless Chat (Kaliwang Panel)](../../../01-introduction)
  - [Stateful Chat (Kanan na Panel)](../../../01-introduction)
- [Mga Susunod na Hakbang](../../../01-introduction)

## Paglalakad sa Video

Panoorin ang live session na ito na nagpapaliwanag kung paano magsimula gamit ang module na ito:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Mga Matututunan Mo

Kung natapos mo na ang quick start, nakita mo kung paano magpadala ng mga prompt at kumuha ng mga tugon. Ito ang pundasyon, ngunit ang totoong mga aplikasyon ay nangangailangan ng higit pa. Itinuturo ng module na ito kung paano bumuo ng conversational AI na nakakaalala ng konteksto at nagpapanatili ng estado — ang pagkakaiba ng isang one-off demo at isang aplikasyon na handa na para sa produksyon.

Gagamitin natin ang Azure OpenAI GPT-5.2 sa buong gabay na ito dahil ang mga advanced na kakayahan nito sa pangangatwiran ay nagpapalinaw ng pag-uugali ng iba't ibang mga pattern. Kapag nagdagdag ka ng memorya, makikita mo nang malinaw ang pagkakaiba. Ginagawa nitong mas madali ang pag-unawa kung ano ang dinadala ng bawat bahagi sa iyong aplikasyon.

Bubuuin mo ang isang aplikasyon na nagpapakita ng parehong mga pattern:

**Stateless Chat** - Bawat kahilingan ay independyente. Walang memorya ang modelo ng mga naunang mensahe. Ito ang pattern na ginamit mo sa quick start.

**Stateful Conversation** - Kasama sa bawat kahilingan ang kasaysayan ng pag-uusap. Pinapanatili ng modelo ang konteksto sa maraming pag-uusap. Ito ang kailangan ng mga aplikasyon sa produksyon.

## Mga Kailangan Bago Magsimula

- Azure subscription na may access sa Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Tandaan:** Nakainstall na ang Java, Maven, Azure CLI at Azure Developer CLI (azd) sa ibinigay na devcontainer.

> **Tandaan:** Ginagamit ng module na ito ang GPT-5.2 sa Azure OpenAI. Ang deployment ay naka-configure nang awtomatiko gamit ang `azd up` — huwag baguhin ang pangalan ng modelo sa code.

## Pag-unawa sa Pangunahing Suliranin

Ang mga language model ay stateless. Ang bawat tawag sa API ay independyente. Kung ipapadala mo ang "My name is John" at pagkatapos ay tatanungin mo "What's my name?", wala sa modelo ang alam na ipinakilala mo ang iyong sarili. Tinatrato nito ang bawat kahilingan na parang ito ang unang pag-uusap mo kailanman.

Ayos lang ito para sa simpleng Q&A ngunit walang silbi para sa totoong mga aplikasyon. Kailangan ng mga customer service bots na maalala ang mga sinabi mo sa kanila. Kailangan ng mga personal assistant ang konteksto. Anumang multi-turn na pag-uusap ay nangangailangan ng memorya.

<img src="../../../translated_images/tl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ang pagkakaiba ng stateless (independiyenteng mga tawag) at stateful (may kamalayan sa konteksto) na mga pag-uusap*

## Pag-unawa sa mga Token

Bago sumabak sa mga pag-uusap, mahalagang maunawaan ang mga token — mga batayang yunit ng teksto na pinoproseso ng mga language model:

<img src="../../../translated_images/tl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Halimbawa kung paano hinahati ang teksto sa mga token — "I love AI!" ay nagiging 4 na magkahiwalay na yunit ng proseso*

Ang mga token ang paraan ng pagsukat at pagproseso ng teksto ng mga AI model. Mga salita, bantas, at maging mga puwang ay maaaring mga token. May limitasyon ang iyong modelo sa dami ng mga token na kayang iproseso nang sabay-sabay (400,000 para sa GPT-5.2, na may hanggang 272,000 input tokens at 128,000 output tokens). Nakakatulong ang pag-unawa sa mga token para pamahalaan ang haba ng pag-uusap at mga gastos.

## Paano Gumagana ang Memorya

Nilulutas ng chat memory ang stateless na problema sa pamamagitan ng pagpapanatili ng kasaysayan ng pag-uusap. Bago ipadala ang iyong kahilingan sa modelo, idinadagdag ng framework ang mga kaugnay na nakaraang mensahe. Kapag tinanong mo "What's my name?", ang sistema ay aktwal na nagpapadala ng buong kasaysayan ng pag-uusap, kaya nakikita ng modelo na sinabi mo dati na "My name is John."

Nagbibigay ang LangChain4j ng mga implementasyon ng memorya na awtomatikong humahawak nito. Pinipili mo kung gaano karaming mga mensahe ang itatago at pinangangasiwaan ng framework ang konteksto sa window.

<img src="../../../translated_images/tl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*Pinapanatili ng MessageWindowChatMemory ang sliding window ng mga kamakailang mensahe, awtomatikong tinatanggal ang mga luma*

## Paano Ito Gumagamit ng LangChain4j

Pinalalawak ng module na ito ang quick start sa pamamagitan ng pag-integrate ng Spring Boot at pagdagdag ng memorya ng pag-uusap. Ganito ang pagkakaayos ng mga bahagi:

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

Binabasa ng builder ang mga kredensyal mula sa mga environment variable na na-set ng `azd up`. Ang pagseset ng `baseUrl` sa iyong Azure endpoint ang nagpapagana ng OpenAI client para sa Azure OpenAI.

**Conversation Memory** - Subaybayan ang kasaysayan ng chat gamit ang MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Lumikha ng memorya gamit ang `withMaxMessages(10)` upang itago ang huling 10 mensahe. Magdagdag ng mga mensahe ng user at AI gamit ang typed wrappers: `UserMessage.from(text)` at `AiMessage.from(text)`. Kunin ang kasaysayan gamit ang `memory.messages()` at ipadala ito sa modelo. Nag-iimbak ang serbisyo ng hiwalay na mga instance ng memorya para sa bawat conversation ID, na nagpapahintulot sa maraming mga user na makipag-chat nang sabay-sabay.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) at itanong:
> - "Paano pinipili ng MessageWindowChatMemory kung aling mga mensahe ang tatanggalin kapag puno na ang window?"
> - "Puwede ba akong gumawa ng custom na memory storage gamit ang database imbes na in-memory?"
> - "Paano ako magdagdag ng summarization para paliitin ang lumang kasaysayan ng pag-uusap?"

Ang stateless chat endpoint ay hindi gumagamit ng memorya — diretso lang `chatModel.chat(prompt)` tulad ng sa quick start. Ang stateful endpoint naman ay nagdadagdag ng mga mensahe sa memorya, kumukuha ng kasaysayan, at isinasama ang kontekstong iyon sa bawat kahilingan. Parehong magkaparehong configuration ng modelo, magkaibang mga pattern.

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

> **Tandaan:** Kung makatagpo ng timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ulitin lang ang `azd up`. Maaaring nagpapatuloy pa ang Azure resources sa background, at ang pag-uulit ay nagpapahintulot sa deployment na matapos kapag ang mga resources ay umabot na sa terminal state.

Ito ay:
1. Magde-deploy ng Azure OpenAI resource na may GPT-5.2 at text-embedding-3-small models
2. Awtomatikong gagawa ng `.env` file sa root ng proyekto na may mga kredensyal
3. Ise-set up ang lahat ng kailangang environment variables

**Nagkakaproblema sa deployment?** Tingnan ang [Infrastructure README](infra/README.md) para sa detalyadong troubleshooting kabilang ang mga subdomain name conflicts, manu-manong deployment sa Azure Portal, at gabay sa configuration ng modelo.

**I-verify kung matagumpay ang deployment:**

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

> **Tandaan:** Ang `azd up` ay awtomatikong gumagawa ng `.env` file. Kung kailangan mo itong i-update sa ibang pagkakataon, maaari mong i-edit ang `.env` nang mano-mano o muling gawin ito sa pamamagitan ng pagtakbo ng:
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

**I-verify ang deployment:**

Siguraduhing nandito ang `.env` file sa root directory na may Azure credentials:

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```


**Simulan ang mga aplikasyon:**

**Opsyon 1: Paggamit ng Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension na nagbibigay ng visual na interface para pamahalaan ang lahat ng Spring Boot apps. Makikita mo ito sa Activity Bar sa kaliwa ng VS Code (hanapin ang Spring Boot icon).

Mula sa Spring Boot Dashboard, maaari mong:
- Makita ang lahat ng magagamit na Spring Boot applications sa workspace
- Simulan/hinto ang mga apps sa isang click lang
- Tingnan ang mga logs ng app nang real-time
- Subaybayan ang status ng app

Pindutin lang ang play button malapit sa "introduction" para simulan ang module na ito, o i-start lahat ng modules nang sabay-sabay.

<img src="../../../translated_images/tl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Paggamit ng shell scripts**

Simulan ang lahat ng web apps (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa ugat na direktoryo
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


Parehong scripts ay awtomatikong naglo-load ng environment variables mula sa root `.env` file at magbi-build ng JARs kung wala pa.

> **Tandaan:** Kung mas gusto mong idevelop nang mano-mano lahat ng modules bago magsimula:
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

**Para itigil:**

**Bash:**
```bash
./stop.sh  # Module na ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng module
```

**PowerShell:**
```powershell
.\stop.ps1  # Ang module na ito lang
# O
cd ..; .\stop-all.ps1  # Lahat ng modules
```


## Paggamit ng Aplikasyon

Nagbibigay ang aplikasyon ng web interface na may dalawang chat implementations na magkatabi.

<img src="../../../translated_images/tl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard na nagpapakita ng parehong Simple Chat (stateless) at Conversational Chat (stateful) na mga opsyon*

### Stateless Chat (Kaliwang Panel)

Subukan ito muna. Itanong ang "My name is John" at pagkatapos ay agad na itanong "What's my name?" Hindi ito maaalala ng modelo dahil bawat mensahe ay independyente. Ipinapakita nito ang pangunahing problema sa basic na integration ng language model — walang konteksto ng pag-uusap.

<img src="../../../translated_images/tl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Hindi naaalala ng AI ang iyong pangalan mula sa nakaraang mensahe*

### Stateful Chat (Kanan na Panel)

Ngayon subukan ang parehong pagkakasunod dito. Itanong ang "My name is John" at pagkatapos ay "What's my name?" Ngayon ito ay naaalala. Ang pagkakaiba ay ang MessageWindowChatMemory — pinapanatili nito ang kasaysayan ng pag-uusap at isinasama ito sa bawat kahilingan. Ganito gumagana ang production conversational AI.

<img src="../../../translated_images/tl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Naalaala ng AI ang iyong pangalan mula sa naunang pag-uusap*

Parehong gumagamit ang dalawang panel ng parehong GPT-5.2 na modelo. Ang tanging pagkakaiba ay ang memorya. Ito ang nagpapalinaw kung ano ang dala ng memorya sa iyong aplikasyon at kung bakit mahalaga ito para sa tunay na paggamit.

## Mga Susunod na Hakbang

**Susunod na Module:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Nakaraan: Module 00 - Quick Start](../00-quick-start/README.md) | [Bumalik sa Pangunahing Bahagi](../README.md) | [Susunod: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:  
Ang dokumentong ito ay naisalin gamit ang serbisyong AI na pagsasalin na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't sinisikap naming maging tumpak ang pagsasalin, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatumpak. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot para sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->