# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [What is LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [What Each Example Shows](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

Ang quickstart na ito ay nilalayong mabilis kang makapagsimula at makagamit ng LangChain4j. Saklaw nito ang mga pinaka-pangunahing kaalaman sa paggawa ng AI na mga aplikasyon gamit ang LangChain4j at GitHub Models. Sa mga susunod na module gagamitin mo ang Azure OpenAI kasama ang LangChain4j para gumawa ng mas advanced na mga application.

## What is LangChain4j?

Ang LangChain4j ay isang Java library na nagpapadali ng paggawa ng mga AI-powered na application. Sa halip na makipag-ugnayan sa HTTP clients at JSON parsing, gagamit ka ng malinis na Java APIs.

Ang "chain" sa LangChain ay tumutukoy sa pagsasama-sama ng maraming mga bahagi—maaari kang mag-chain ng prompt sa isang modelo patungo sa parser, o mag-chain ng maraming AI na tawag kung saan ang output ng isa ay magiging input ng susunod. Nakatuon ang quickstart na ito sa mga pangunahing konsepto bago sumubok ng mas kumplikadong chains.

<img src="../../../translated_images/tl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Pagsasama-sama ng mga bahagi sa LangChain4j - nagtutulungan ang mga bloke upang makabuo ng malalakas na AI workflows*

Gagamit tayo ng tatlong pangunahing bahagi:

**ChatModel** - Ang interface para sa pakikipag-ugnayan sa AI model. Tumawag ng `model.chat("prompt")` at makakakuha ng sagot na string. Ginagamit natin ang `OpenAiOfficialChatModel` na gumagana sa mga OpenAI-compatible endpoints gaya ng GitHub Models.

**AiServices** - Gumagawa ng type-safe na AI service interfaces. Magdefine ng mga metodo, lagyan ng anotasyon gamit ang `@Tool`, at ang LangChain4j ang bahala sa orchestration. Awtomatikong tinatawagan ng AI ang iyong Java methods kapag kinakailangan.

**MessageWindowChatMemory** - Nagpapanatili ng kasaysayan ng usapan. Kung wala ito, ang bawat request ay hiwalay. Sa tulong nito, naaalala ng AI ang mga naunang mensahe at pinapanatili ang konteksto sa maraming palitan.

<img src="../../../translated_images/tl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arkitektura ng LangChain4j - nagtutulungan ang mga pangunahing bahagi upang patakbuhin ang iyong mga AI na aplikasyon*

## LangChain4j Dependencies

Gumagamit ang quick start na ito ng tatlong Maven dependencies sa [`pom.xml`](../../../00-quick-start/pom.xml):

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Ang `langchain4j-open-ai-official` module ay nagbibigay ng `OpenAiOfficialChatModel` class na kumokonekta sa mga OpenAI-compatible API. Gumagamit ang GitHub Models ng parehong format ng API, kaya hindi kailangan ng espesyal na adapter — ituturo mo lang ang base URL sa `https://models.github.ai/inference`.

Ang `langchain4j-easy-rag` module naman ay nagbibigay ng awtomatikong paghahati ng dokumento, embedding, at retrieval para makabuo ka ng mga RAG application nang hindi mano-manong kinokonpigurang bawat hakbang.

## Prerequisites

**Gumagamit ng Dev Container?** Nakainstall na ang Java at Maven. Kailangan mo lang ng GitHub Personal Access Token.

**Local Development:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (mga tagubilin sa ibaba)

> **Tandaan:** Ginagamit ng module na ito ang `gpt-4.1-nano` mula sa GitHub Models. Huwag baguhin ang pangalan ng modelo sa code — nakakonpigurasyon ito upang gumana sa mga available na modelo ng GitHub.

## Setup

### 1. Get Your GitHub Token

1. Pumunta sa [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. I-click ang "Generate new token"
3. Maglagay ng malinaw na pangalan (halimbawa, "LangChain4j Demo")
4. Piliin ang expiration (7 araw ang inirerekomenda)
5. Sa ilalim ng "Account permissions", hanapin ang "Models" at itakda sa "Read-only"
6. I-click ang "Generate token"
7. Kopyahin at i-save ang token - hindi mo na ito makikita muli

### 2. Set Your Token

**Option 1: Using VS Code (Inirerekomenda)**

Kung gumagamit ka ng VS Code, idagdag ang token mo sa `.env` file sa root ng proyekto:

Kung wala ang `.env` file, kopyahin ang `.env.example` bilang `.env` o gumawa ng bagong `.env` file sa root ng proyekto.

**Halimbawa ng `.env` file:**
```bash
# Sa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Pagkatapos ay i-right click lang ang kahit anong demo file (halimbawa, `BasicChatDemo.java`) sa Explorer at piliin ang **"Run Java"** o gamitin ang launch configurations mula sa Run at Debug panel.

**Option 2: Using Terminal**

Itakda ang token bilang environment variable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Run the Examples

**Using VS Code:** I-right click lang ang kahit anong demo file sa Explorer at piliin ang **"Run Java"**, o gamitin ang launch configurations mula sa Run at Debug panel (siguraduhing nailagay mo muna ang token sa `.env` file).

**Using Maven:** Pwede ring patakbuhin mula sa command line:

### 1. Basic Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Patterns

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ipinapakita ang zero-shot, few-shot, chain-of-thought, at role-based prompting.

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Awtomatikong tinatawagan ng AI ang iyong Java methods kapag kailangan.

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Magtanong tungkol sa iyong mga dokumento gamit ang Easy RAG na may awtomatikong embedding at retrieval.

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Tingnan kung paano pinipigilan ng AI safety filters ang mapanganib na nilalaman.

## What Each Example Shows

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Magsimula dito upang makita ang LangChain4j sa pinakapayak na anyo nito. Gumawa ka ng `OpenAiOfficialChatModel`, magpadala ng prompt gamit ang `.chat()`, at makakuha ng sagot. Ipinapakita nito ang pundasyon: paano i-initialize ang mga modelo gamit ang custom endpoints at API keys. Kapag nasanay ka sa pattern na ito, lahat ng iba pang gamit ay bubuo rito.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) at itanong:
> - "Paano ko papalitan mula sa GitHub Models patungong Azure OpenAI sa code na ito?"
> - "Anu-ano pang parameters ang pwede kong i-configure sa OpenAiOfficialChatModel.builder()?"
> - "Paano ako gagawa ng streaming responses sa halip na maghintay ng kompletong sagot?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ngayon na alam mo na kung paano makipag-usap sa modelo, tingnan natin kung ano ang sinasabi mo rito. Ginagamit ng demo na ito ang parehong model setup pero nagpapakita ng limang magkaibang patterns ng prompting. Subukan ang zero-shot prompts para sa direktang utos, few-shot prompts na natututo mula sa mga halimbawa, chain-of-thought prompts na nagpapakita ng mga hakbang ng pag-iisip, at role-based prompts na nagtatakda ng konteksto. Makikita mo kung paano nagbibigay ang parehong modelo ng malalaking pagkakaiba ng resulta batay sa paraan ng pagpapahayag ng hinihiling mo.

Ipinapakita rin ng demo ang prompt templates, na isang makapangyarihang paraan upang gumawa ng reusable na prompts na may mga variables. Ang halimbawa sa ibaba ay nagpapakita ng prompt gamit ang LangChain4j `PromptTemplate` upang lagyan ng laman ang mga variables. Sasagutin ng AI base sa ibinigay na destinasyon at aktibidad.

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

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) at itanong:
> - "Ano ang pinagkaiba ng zero-shot at few-shot prompting, at kailan dapat gamitin ang bawat isa?"
> - "Paano naaapektuhan ng temperature parameter ang mga sagot ng modelo?"
> - "Ano ang mga teknik upang maiwasan ang prompt injection attacks sa production?"
> - "Paano ako makakagawa ng reusable PromptTemplate objects para sa mga karaniwang pattern?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dito nagsisimulang maging makapangyarihan ang LangChain4j. Gagamitin mo ang `AiServices` upang gumawa ng assistant sa AI na maaaring tumawag ng iyong Java methods. Lagyan lang ng anotasyon ang mga methods gamit ang `@Tool("description")` at ang LangChain4j na ang bahala sa natitirang proseso — awtomatikong pinipili ng AI kung kailan gagamitin ang bawat tool base sa tanong ng user. Ipinapakita nito ang function calling, isang mahalagang teknika sa paggawa ng AI na kayang gumawa ng aksyon, hindi lang sumagot.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) at itanong:
> - "Paano gumagana ang @Tool annotation at ano ang ginagawa ng LangChain4j dito sa likod?"
> - "Puwede bang tumawag ang AI ng maraming tools nang sunod-sunod para lutasin ang komplikadong problema?"
> - "Ano ang nangyayari kapag may tool na nag-throw ng exception — paano ko dapat hawakan ang mga error?"
> - "Paano ko i-integrate ang totoong API sa halip na ang calculator example na ito?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Dito makikita mo ang RAG (retrieval-augmented generation) gamit ang “Easy RAG” approach ng LangChain4j. Naglo-load nang mga dokumento, awtomatikong hinahati at ine-embed sa in-memory store, pagkatapos ay may content retriever na nagbibigay ng relevant na bahagi sa AI kapag may query. Sasagot ang AI base sa iyong mga dokumento, hindi lang sa pangkalahatang kaalaman nito.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) at itanong:
> - "Paano pinipigilan ng RAG ang AI hallucinations kumpara sa paggamit ng training data ng modelo?"
> - "Ano ang pinagkaiba ng madaling pamamaraang ito at custom RAG pipeline?"
> - "Paano ko marerehistro ito upang kayanin ang maraming dokumento o mas malalaking knowledge bases?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Gumawa ng AI safety gamit ang defense in depth. Ipinapakita ng demo na ito ang dalawang antas ng proteksyon na nagtutulungan:

**Bahagi 1: LangChain4j Input Guardrails** - Pinipigilan ang mapanganib na mga prompt bago pa makarating sa LLM. Gumawa ng sariling guardrails na nagsusuri ng mga ipinagbabawal na salita o pattern. Nagsisimula ito sa iyong code kaya mabilis at libre.

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

**Bahagi 2: Provider Safety Filters** - May built-in na mga filter ang GitHub Models na nahuhuli ang mga bagay na maaaring makalusot sa iyong guardrails. Makikita mo ang hard blocks (HTTP 400 errors) para sa matitinding paglabag at soft refusals kung saan magalang na tinatanggi ng AI.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) at itanong:
> - "Ano ang InputGuardrail at paano gumawa ng sarili kong guardrail?"
> - "Ano ang pinagkaiba ng hard block at soft refusal?"
> - "Bakit ginagamit ang parehong guardrails at provider filters?"

## Next Steps

**Susunod na Module:** [01-introduction - Pagsisimula gamit ang LangChain4j at gpt-5 sa Azure](../01-introduction/README.md)

---

**Navigation:** [← Babalik sa Main](../README.md) | [Susunod: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**Isyu**: Ang unang `mvn clean compile` o `mvn package` ay matagal (10-15 minuto)

**Sanhi**: Kailangan ni Maven na i-download lahat ng project dependencies (Spring Boot, LangChain4j libraries, Azure SDKs, atbp.) sa unang build.

**Solusyon**: Normal itong behavior. Mas mabilis ang susunod na mga build dahil naka-cache na ang dependencies sa lokal. Depende ang download time sa bilis ng iyong internet.

### PowerShell Maven Command Syntax

**Isyu**: Hindi gumagana ang mga Maven commands na may error na `Unknown lifecycle phase ".mainClass=..."`
**Sanhi**: Tinitingnan ng PowerShell ang `=` bilang operator ng pagtatakda ng variable, na nagiging dahilan ng pagbasag ng syntax ng Maven property

**Solusyon**: Gamitin ang stop-parsing operator na `--%` bago ang utos ng Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Sinasabi ng operator na `--%` sa PowerShell na ipasa ang lahat ng natitirang argumento nang literal sa Maven nang walang interpretasyon.

### Pagpapakita ng Emoji sa Windows PowerShell

**Isyu**: Nagpapakita ang mga sagot ng AI ng mga kakaibang karakter (hal. `????` o `â??`) sa halip na emojis sa PowerShell

**Sanhi**: Hindi sinusuportahan ng default na encoding ng PowerShell ang UTF-8 emojis

**Solusyon**: Patakbuhin ang utos na ito bago magpatakbo ng mga Java na aplikasyon:
```cmd
chcp 65001
```

Pinipilit nito ang UTF-8 encoding sa terminal. Bilang alternatibo, gamitin ang Windows Terminal na mas mahusay ang suporta sa Unicode.

### Pag-debug ng mga Tawag sa API

**Isyu**: Mga error sa authentication, mga limitasyon sa rate, o mga hindi inaasahang tugon mula sa AI na modelo

**Solusyon**: Kasama sa mga halimbawa ang `.logRequests(true)` at `.logResponses(true)` upang ipakita ang mga tawag sa API sa console. Nakakatulong ito sa pag-troubleshoot ng mga error sa authentication, limitasyon sa rate, o mga hindi inaasahang tugon. Alisin ang mga flags na ito sa production upang mabawasan ang kalat sa log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI na serbisyo sa pagsasalin na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagaman nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring may mga pagkakamali o di-tumpak na bahagi. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mga mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang pagkakaintindi o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->