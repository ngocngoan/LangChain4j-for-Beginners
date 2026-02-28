# Module 01: Pagsisimula sa LangChain4j

## Table of Contents

- [Video Walkthrough](../../../01-introduction)
- [What You'll Learn](../../../01-introduction)
- [Prerequisites](../../../01-introduction)
- [Understanding the Core Problem](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Works](../../../01-introduction)
- [How This Uses LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run the Application Locally](../../../01-introduction)
- [Using the Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Next Steps](../../../01-introduction)

## Video Walkthrough

Panoorin ang live na sesyon na ito na nagpapaliwanag kung paano magsimula sa module na ito: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## What You'll Learn

Kung nakumpleto mo ang quick start, nakita mo kung paano magpadala ng mga prompt at makatanggap ng mga tugon. Ito ang pundasyon, ngunit ang mga totoong aplikasyon ay nangangailangan ng higit pa. Itinuturo ng module na ito kung paano bumuo ng conversational AI na nakakaalala ng konteksto at nagpapanatili ng estado - ang pinagkaiba ng isang one-off na demo at isang handang aplikasyon para sa produksyon.

Gagamitin natin ang GPT-5.2 ng Azure OpenAI sa buong gabay na ito dahil sa mga advanced na kakayahan nito sa pangangatwiran na nagpapalinaw ng pag-uugali ng iba't ibang pattern. Kapag nagdagdag ka ng memorya, malinaw mong makikita ang pagkakaiba. Pinapadali nito ang pag-unawa kung ano ang dinadala ng bawat bahagi sa iyong aplikasyon.

Magbubuo ka ng isang aplikasyon na nagpapakita ng parehong pattern:

**Stateless Chat** - Bawat kahilingan ay independiyente. Walang memorya ng model sa mga naunang mensahe. Ito ang pattern na ginamit mo sa quick start.

**Stateful Conversation** - Bawat kahilingan ay may kasamang kasaysayan ng pag-uusap. Pinapanatili ng modelo ang konteksto sa maraming pag-ikot. Ito ang kailangan ng mga aplikasyon para sa produksyon.

## Prerequisites

- Azure subscription na may access sa Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI at Azure Developer CLI (azd) ay naka-pre-install na sa ibinigay na devcontainer.

> **Note:** Ginagamit ng module na ito ang GPT-5.2 sa Azure OpenAI. Ang deployment ay awtomatikong naka-configure gamit ang `azd up` - huwag baguhin ang pangalan ng modelo sa code.

## Understanding the Core Problem

Ang mga language model ay stateless. Bawat tawag sa API ay independiyente. Kung magpadala ka ng "My name is John" at pagkatapos ay magtanong ng "What's my name?", wala itong ideya na ipinakilala mo ang iyong sarili. Tinuturing ng modelo ang bawat kahilingan na parang ito ang unang pag-uusap mo kailanman.

Ayos ito para sa simpleng Q&A ngunit walang silbi para sa mga totoong aplikasyon. Kailangan ng mga customer service bot na maalala ang sinasabi mo sa kanila. Kailangan ng mga personal assistant ng konteksto. Anumang multi-turn na pag-uusap ay nangangailangan ng memorya.

<img src="../../../translated_images/tl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Ang pagkakaiba sa pagitan ng stateless (mga independiyenteng tawag) at stateful (may kamalayan sa konteksto) na mga pag-uusap*

## Understanding Tokens

Bago pumasok sa mga pag-uusap, mahalagang maunawaan ang mga tokens - ang mga pangunahing yunit ng teksto na pinoproseso ng mga language model:

<img src="../../../translated_images/tl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Halimbawa kung paano hinahati ang teksto sa mga tokens - "I love AI!" ay nagiging 4 na magkakahiwalay na yunit na pinoproseso*

Ang mga tokens ang ginagamit ng mga AI model para sukatin at iproseso ang teksto. Maaaring maging tokens ang mga salita, bantas, at maging ang mga spaces. May limitasyon ang iyong modelo kung gaano karaming tokens ang kaya nitong iproseso nang sabay-sabay (400,000 para sa GPT-5.2, na may hanggang 272,000 input tokens at 128,000 output tokens). Ang pag-unawa sa tokens ay tumutulong sa'yo para pamahalaan ang haba ng pag-uusap at ang mga gastos.

## How Memory Works

Nilulutas ng chat memory ang problemang stateless sa pamamagitan ng pagpapanatili ng kasaysayan ng pag-uusap. Bago ipadala ang iyong kahilingan sa modelo, ipinapasok ng framework ang mga kaugnay na naunang mensahe. Kapag tinanong mo ang "What's my name?", ang sistema ay ipinapadala ang buong kasaysayan ng pag-uusap, kaya nakikita ng modelo na sinabi mo dati ang "My name is John."

Nagbibigay ang LangChain4j ng mga implementasyon ng memorya na awtomatikong humahawak nito. Pinipili mo kung ilang mga mensahe ang itatago at pinamamahalaan ng framework ang context window.

<img src="../../../translated_images/tl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*Pinapanatili ng MessageWindowChatMemory ang sliding window ng mga kamakailang mensahe, awtomatikong binubura ang mga luma*

## How This Uses LangChain4j

Pinalalawak ng module na ito ang quick start sa pamamagitan ng pag-integrate ng Spring Boot at pagdagdag ng memorya sa pag-uusap. Ganito ang pagkakaugnay ng mga bahagi:

**Dependencies** - Magdagdag ng dalawang LangChain4j na libraries:

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

Binabasa ng builder ang mga kredensyal mula sa mga environment variable na itinatakda ng `azd up`. Ang pagtatakda ng `baseUrl` sa iyong Azure endpoint ay nagpapaandar ng OpenAI client para sa Azure OpenAI.

**Conversation Memory** - Subaybayan ang kasaysayan ng chat gamit ang MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Gumawa ng memorya gamit ang `withMaxMessages(10)` para panatilihin ang huling 10 mensahe. Magdagdag ng mga mensahe mula sa user at AI gamit ang typed wrappers: `UserMessage.from(text)` at `AiMessage.from(text)`. Kunin ang kasaysayan gamit ang `memory.messages()` at ipadala ito sa modelo. Nag-iimbak ang serbisyo ng magkahiwalay na memory instances para sa bawat ID ng pag-uusap, na nagpapahintulot sa maraming user na mag-chat nang sabay-sabay.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) at itanong:
> - "Paano pinipili ng MessageWindowChatMemory kung aling mga mensahe ang tatanggalin kapag puno na ang window?"
> - "Pwede ba akong gumamit ng custom memory storage gamit ang database sa halip na in-memory?"
> - "Paano ako magdadagdag ng summarization para basi kompresin ang lumang kasaysayan ng pag-uusap?"

Ang stateless chat endpoint ay hindi gumagamit ng memorya - diretso lang `chatModel.chat(prompt)` tulad sa quick start. Ang stateful endpoint ay nagdadagdag ng mga mensahe sa memorya, kinukuha ang kasaysayan, at isinama ang konteksto sa bawat kahilingan. Parehong configuration ng modelo, magkaibang mga pattern.

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

> **Note:** Kung makaranas ng timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), isa lang ulit patakbuhin ang `azd up`. Maaaring patuloy pang nagpoprovide ang Azure resources sa background, at ang pag-ulit ay nagpapahintulot matapos ang deployment kapag umabot na ang mga resources sa terminal na estado.

Ito ay:
1. Magde-deploy ng Azure OpenAI resource na may GPT-5.2 at text-embedding-3-small models
2. Awtomatikong gagawa ng `.env` file sa root ng proyekto kasama ang mga kredensyal
3. Isaayos lahat ng kailangang environment variables

**May problema sa deployment?** Tingnan ang [Infrastructure README](infra/README.md) para sa detalyadong troubleshooting kabilang ang mga conflict sa subdomain name, manu-manong deployment gamit ang Azure Portal, at mga gabay sa configuration ng modelo.

**Siguraduhing matagumpay ang deployment:**

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, atbp.
```

> **Note:** Awtomatikong ginagawa ng `azd up` ang `.env` file. Kung kailangan mo itong i-update sa hinaharap, maaari mo itong i-edit nang manu-mano o i-regenerate sa pamamagitan ng pagtakbo ng:
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

Tiyaking nandito ang `.env` file sa root directory na may Azure credentials:

**Bash:**
```bash
cat ../.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Dapat ipakita ang AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Simulan ang mga aplikasyon:**

**Opsyon 1: Gamit ang Spring Boot Dashboard (Inirerekomenda para sa mga gumagamit ng VS Code)**

Kasama sa dev container ang Spring Boot Dashboard extension, na nagbibigay ng visual na interface para pamahalaan lahat ng Spring Boot applications. Makikita ito sa Activity Bar sa kaliwang bahagi ng VS Code (hanapin ang Spring Boot icon).

Mula sa Spring Boot Dashboard, maaari mong:
- Makita lahat ng available na Spring Boot applications sa workspace
- Simulan/hinto ang mga aplikasyon sa isang click lang
- Tingnan ang mga logs ng aplikasyon nang real-time
- I-monitor ang estado ng aplikasyon

I-click lang ang play button sa tabi ng "introduction" para simulan ang module na ito, o simulan lahat ng module ng sabay-sabay.

<img src="../../../translated_images/tl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opsyon 2: Gamit ang mga shell script**

Simulan lahat ng web application (modules 01-04):

**Bash:**
```bash
cd ..  # Mula sa root directory
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

Ang parehong mga script ay awtomatikong naglo-load ng mga environment variable mula sa root `.env` file at magtatayo ng mga JAR kung wala pa.

> **Note:** Kung nais mong gumawa nang manu-mano ng lahat ng module bago simulan:
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
./stop.sh  # Ang modulong ito lamang
# O
cd .. && ./stop-all.sh  # Lahat ng mga module
```

**PowerShell:**
```powershell
.\stop.ps1  # Para lang sa module na ito
# O
cd ..; .\stop-all.ps1  # Lahat ng module
```

## Using the Application

Nagbibigay ang aplikasyon ng web interface na may dalawang implementasyon ng chat na magkatabi.

<img src="../../../translated_images/tl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard na nagpapakita ng pareho'ng Simple Chat (stateless) at Conversational Chat (stateful) na mga opsyon*

### Stateless Chat (Left Panel)

Subukan ito muna. Sabihin ang "My name is John" at agad na itanong "What's my name?" Hindi ito aalalahanin ng modelo dahil ang bawat mensahe ay independiyente. Ipinapakita nito ang pangunahing problema sa integrasyon ng basic language model - walang konteksto ng pag-uusap.

<img src="../../../translated_images/tl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Hindi naaalala ng AI ang pangalan mo mula sa naunang mensahe*

### Stateful Chat (Right Panel)

Ngayon subukan ang parehong pagkakasunod dito. Sabihin ang "My name is John" at pagkatapos ay "What's my name?" Ngayon ay naaalala nito. Ang pinagkaiba ay ang MessageWindowChatMemory - pinapanatili nito ang kasaysayan ng pag-uusap at isinama ito sa bawat kahilingan. Ganito gumagana ang production conversational AI.

<img src="../../../translated_images/tl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Naalaala ng AI ang pangalan mo mula sa naunang bahagi ng pag-uusap*

Parehong gamit ang GPT-5.2 model ang dalawang panel. Ang tanging pagkakaiba ay ang memorya. Pinapalinaw nito kung ano ang naidudulot ng memorya sa iyong aplikasyon at kung bakit mahalaga ito para sa mga totoong kaso ng paggamit.

## Next Steps

**Next Module:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigation:** [← Previous: Module 00 - Quick Start](../00-quick-start/README.md) | [Back to Main](../README.md) | [Next: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi tumpak na impormasyon. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling pantao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling pagpapakahulugan na nagmumula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->