# Module 00: Quick Start

## Table of Contents

- [Panimula](../../../00-quick-start)
- [Ano ang LangChain4j?](../../../00-quick-start)
- [Mga Depensiya ng LangChain4j](../../../00-quick-start)
- [Mga Kinakailangan](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Kunin ang Iyong GitHub Token](../../../00-quick-start)
  - [2. Itakda ang Iyong Token](../../../00-quick-start)
- [Patakbuhin ang mga Halimbawa](../../../00-quick-start)
  - [1. Pangunahing Chat](../../../00-quick-start)
  - [2. Mga Pattern ng Prompt](../../../00-quick-start)
  - [3. Pagtawag sa Function](../../../00-quick-start)
  - [4. Document Q&A (Madaling RAG)](../../../00-quick-start)
  - [5. Responsable na AI](../../../00-quick-start)
- [Ano ang Ipinapakita ng Bawat Halimbawa](../../../00-quick-start)
- [Mga Susunod na Hakbang](../../../00-quick-start)
- [Pag-troubleshoot](../../../00-quick-start)

## Panimula

Ang quickstart na ito ay inilaan upang mapabilis kang makapagsimula gamit ang LangChain4j. Sinasaklaw nito ang pinaka-pangunahing kaalaman sa pagbuo ng mga AI na aplikasyon gamit ang LangChain4j at GitHub Models. Sa mga susunod na module, lilipat ka sa Azure OpenAI at GPT-5.2 at sisisid nang mas malalim sa bawat konsepto.

## Ano ang LangChain4j?

Ang LangChain4j ay isang Java library na nagpapadali sa pagbuo ng mga AI-powered na aplikasyon. Sa halip na harapin ang mga HTTP client at pag-parse ng JSON, gagana ka gamit ang malilinis na Java APIs.

Ang "chain" sa LangChain ay tumutukoy sa pagsasama-sama ng maraming bahagi - maaaring gumawa ka ng chain mula sa prompt papunta sa model papunta sa parser, o pagsamahin ang maraming tawag sa AI kung saan ang output ng isa ay magiging input ng susunod. Nakatuon ang quick start na ito sa mga pangunahing kaalaman bago tuklasin ang mas kumplikadong mga chain.

<img src="../../../translated_images/tl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Pagsasama-sama ng mga bahagi sa LangChain4j - ang mga bloke ng gusali ay nag-uugnay upang lumikha ng makapangyarihang mga workflow ng AI*

Gagamitin namin ang tatlong pangunahing bahagi:

**ChatModel** - Ang interface para sa mga interaksyon ng AI model. Tawagin ang `model.chat("prompt")` at makakuha ng tugon na string. Ginagamit namin ang `OpenAiOfficialChatModel` na gumagana sa mga endpoint na compatible sa OpenAI tulad ng GitHub Models.

**AiServices** - Lumilikha ng type-safe na mga interface ng AI service. Tukuyin ang mga method, lagyan sila ng anotasyon na `@Tool`, at ang LangChain4j ang bahala sa orchestration. Awtomatikong tinatawagan ng AI ang iyong mga Java method kapag kailangan.

**MessageWindowChatMemory** - Pinapanatili ang kasaysayan ng pag-uusap. Kung wala ito, ang bawat request ay hiwalay. Kung mayroon ito, naaalala ng AI ang mga naunang mensahe at pinananatili ang konteksto sa maraming palitan.

<img src="../../../translated_images/tl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arkitektura ng LangChain4j - ang pangunahing mga bahagi na nagtutulungan upang paandarin ang iyong mga AI na aplikasyon*

## Mga Depensiya ng LangChain4j

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

Ang `langchain4j-open-ai-official` module ay nagbibigay ng `OpenAiOfficialChatModel` class na kumokonekta sa mga OpenAI-compatible na API. Ginagamit ng GitHub Models ang parehong format ng API, kaya hindi kailangan ng espesyal na adapter - ituro lang ang base URL sa `https://models.github.ai/inference`.

Ang `langchain4j-easy-rag` module ay nagbibigay ng awtomatikong pag-split, embedding, at retrieval ng dokumento upang makapagtayo ka ng mga RAG application nang hindi mano-manong kinokonpigura ang bawat hakbang.

## Mga Kinakailangan

**Gumagamit ng Dev Container?** Nakainstall na ang Java at Maven. Kailangan mo lang ang isang GitHub Personal Access Token.

**Lokal na Pag-develop:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (mga tagubilin sa ibaba)

> **Tandaan:** Ginagamit ng module na ito ang `gpt-4.1-nano` mula sa GitHub Models. Huwag baguhin ang pangalan ng model sa code - ito ay naka-configure upang gumana sa mga available na modelo ng GitHub.

## Setup

### 1. Kunin ang Iyong GitHub Token

1. Pumunta sa [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. I-click ang "Generate new token"
3. Maglagay ng makabuluhang pangalan (halimbawa, "LangChain4j Demo")
4. Itakda ang expiration (7 araw ang inirerekomenda)
5. Sa ilalim ng "Account permissions", hanapin ang "Models" at itakda ito sa "Read-only"
6. I-click ang "Generate token"
7. Kopyahin at i-save ang iyong token - hindi mo na ito makikita muli

### 2. Itakda ang Iyong Token

**Opsyon 1: Paggamit ng VS Code (Inirerekomenda)**

Kung gumagamit ka ng VS Code, idagdag ang iyong token sa `.env` file sa root ng proyekto:

Kung wala ang `.env` file, kopyahin ang `.env.example` papuntang `.env` o gumawa ng bagong `.env` file sa root ng proyekto.

**Halimbawa ng `.env` file:**
```bash
# Sa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Pagkatapos ay maaari ka nang mag-right click sa kahit anong demo file (hal., `BasicChatDemo.java`) sa Explorer at piliin ang **"Run Java"** o gamitin ang mga launch configuration mula sa Run and Debug panel.

**Opsyon 2: Paggamit ng Terminal**

Itakda ang token bilang environment variable:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Patakbuhin ang mga Halimbawa

**Gamit ang VS Code:** I-right click lang ang kahit anong demo file sa Explorer at piliin ang **"Run Java"**, o gamitin ang launch configurations mula sa Run and Debug panel (siguraduhing nailagay mo muna ang iyong token sa `.env` file).

**Gamit ang Maven:** Bilang alternatibo, maaari mong patakbuhin mula sa command line:

### 1. Pangunahing Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Mga Pattern ng Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ipinapakita ang zero-shot, few-shot, chain-of-thought, at role-based na prompting.

### 3. Pagtawag sa Function

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Awtomatikong tinatawagan ng AI ang iyong mga Java method kapag kailangan.

### 4. Document Q&A (Madaling RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Magtanong tungkol sa iyong mga dokumento gamit ang Easy RAG na may awtomatikong embedding at retrieval.

### 5. Responsable na AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Tingnan kung paano pinipigilan ng AI safety filters ang mapanganib na nilalaman.

## Ano ang Ipinapakita ng Bawat Halimbawa

**Pangunahing Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Magsimula dito upang makita ang LangChain4j sa pinaka-simple nitong anyo. Gumagawa ka ng `OpenAiOfficialChatModel`, magpapadala ng prompt gamit ang `.chat()`, at makakakuha ng tugon. Ipinapakita nito ang pundasyon: kung paano i-initialize ang mga modelo na may custom endpoints at API keys. Kapag naiintindihan mo na ang pattern na ito, ang lahat ay naka-base rito.

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
> - "Paano ako lilipat mula sa GitHub Models papuntang Azure OpenAI sa code na ito?"
> - "Anong ibang mga parameter ang maaari kong i-configure sa OpenAiOfficialChatModel.builder()?"
> - "Paano ako magdadagdag ng streaming responses imbes na maghintay ng kumpletong sagot?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ngayon na alam mo na kung paano makipag-usap sa model, tuklasin natin kung ano ang sinasabi mo rito. Ginagamit ng demo na ito ang parehong setup ng model ngunit ipinapakita ang limang iba't ibang pattern ng prompting. Subukan ang zero-shot prompts para sa direktang utos, few-shot prompts na natututo mula sa mga halimbawa, chain-of-thought prompts na nagpapakita ng mga hakbang sa pag-iisip, at role-based prompts na nagtatakda ng konteksto. Makikita mo kung paano nagkakaroon ng malaking pagkakaiba ang mga resulta depende sa kung paano mo pinapahayag ang iyong kahilingan.

Ipinapakita rin ng demo ang prompt templates, na isang makapangyarihang paraan upang gumawa ng mga reusable prompt na may mga variable.  
Ipinapakita sa ibaba ang halimbawa ng prompt gamit ang LangChain4j `PromptTemplate` para punan ang mga variable. Sasagutin ng AI batay sa ibinigay na destinasyon at aktibidad.

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
> - "Ano ang pagkakaiba ng zero-shot at few-shot prompting, at kailan ko dapat gamitin ang bawat isa?"
> - "Paano nakakaapekto ang temperature parameter sa mga sagot ng model?"
> - "Ano ang mga teknik para maiwasan ang prompt injection attacks sa produksyon?"
> - "Paano ako makakalikha ng reusable PromptTemplate objects para sa mga karaniwang pattern?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dito nagiging makapangyarihan ang LangChain4j. Gagamitin mo ang `AiServices` upang lumikha ng AI assistant na kayang tumawag sa iyong mga Java method. Lagyan lang ng anotasyon ang mga method gamit ang `@Tool("description")` at ang LangChain4j ang bahala sa iba - awtomatikong tinutukoy ng AI kung kailan gagamitin ang bawat tool base sa tanong ng user. Ipinapakita nito ang function calling, isang mahalagang teknik para gumawa ng AI na kayang kumilos, hindi lang sumagot ng tanong.

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
> - "Paano gumagana ang @Tool annotation at ano ang ginagawa ng LangChain4j dito sa likod ng mga eksena?"
> - "Maaari bang tumawag ang AI ng maraming tool ng sunod-sunod para lutasin ang mga kumplikadong problema?"
> - "Ano ang nangyayari kapag may tool na nag-throw ng exception - paano ko dapat harapin ang mga error?"
> - "Paano ko isasama ang totoong API imbes na ang halimbawa ng calculator na ito?"

**Document Q&A (Madaling RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Dito makikita mo ang RAG (retrieval-augmented generation) gamit ang "Easy RAG" na paraan ng LangChain4j. Ang mga dokumento ay niloload, awtomatikong hinahati at ine-embed sa isang in-memory na storage, pagkatapos ay ang content retriever ang nagbibigay ng mga kaugnay na bahagi sa AI kapag nagtatanong. Sumagot ang AI batay sa iyong mga dokumento, hindi sa pangkalahatang kaalaman nito.

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
> - "Paano pinipigilan ng RAG ang mga hallucination ng AI kumpara sa paggamit ng training data ng model?"
> - "Ano ang pagkakaiba ng madaling paraan na ito at ng isang custom na RAG pipeline?"
> - "Paano ko masusukat ito upang mag-handle ng maraming dokumento o mas malalaking knowledge bases?"

**Responsable na AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Gumawa ng kaligtasan sa AI gamit ang multiple layers ng proteksyon. Ipinapakita ng demo na ito ang dalawang antas ng proteksyon na nagtutulungan:

**Bahagi 1: LangChain4j Input Guardrails** - Pinipigilan ang mga mapanganib na prompt bago pa man makarating ito sa LLM. Gumawa ng custom guardrails na sumusuri sa mga ipinagbabawal na keyword o pattern. Isinasagawa ito sa iyong code, kaya mabilis at libre.

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

**Bahagi 2: Provider Safety Filters** - May mga built-in na filter ang GitHub Models na kumukuha ng mga maaaring mapalampas ng guardrails mo. Makikita mo ang mga hard blocks (HTTP 400 errors) para sa malalalang paglabag at soft refusals kung saan magalang na tinatanggihan ng AI.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) at itanong:
> - "Ano ang InputGuardrail at paano ako makagagawa ng sarili ko?"
> - "Ano ang pagkakaiba ng hard block at soft refusal?"
> - "Bakit ginagamit ang parehong guardrails at provider filters nang sabay?"

## Mga Susunod na Hakbang

**Susunod na Module:** [01-introduction - Getting Started with LangChain4j](../01-introduction/README.md)

---

**Navigation:** [← Back to Main](../README.md) | [Susunod: Module 01 - Panimula →](../01-introduction/README.md)

---

## Pag-troubleshoot

### Unang Build ng Maven

**Isyu**: Ang unang `mvn clean compile` o `mvn package` ay tumatagal nang matagal (10-15 minuto)

**Sanhi**: Kailangang i-download ng Maven ang lahat ng dependencies ng proyekto (Spring Boot, LangChain4j libraries, Azure SDKs, atbp.) sa unang build.

**Solusyon**: Normal lang ito. Mas mabilis ang mga susunod na build dahil naka-cache na ang dependencies lokal. Depende ang tagal ng pag-download sa bilis ng iyong network.

### Syntax ng PowerShell Maven Command

**Isyu**: Nabibigo ang mga command ng Maven na may error na `Unknown lifecycle phase ".mainClass=..."`
**Sanhi**: Tinuturing ng PowerShell ang `=` bilang operator ng pagtatalaga ng variable, na sumisira sa syntax ng property ng Maven

**Solusyon**: Gamitin ang stop-parsing operator na `--%` bago ang command ng Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Sinasabi ng operator na `--%` sa PowerShell na ipasa nang literal sa Maven ang lahat ng natitirang argumento nang walang interpretasyon.

### Pagpapakita ng Emoji sa Windows PowerShell

**Isyu**: Nagpapakita ang mga AI na tugon ng mga walang saysay na karakter (hal. `????` o `â??`) sa halip na mga emoji sa PowerShell

**Sanhi**: Hindi sinusuportahan ng default na encoding ng PowerShell ang UTF-8 na mga emoji

**Solusyon**: Patakbuhin ang command na ito bago magpatakbo ng mga Java application:
```cmd
chcp 65001
```

Pinipilit nito ang UTF-8 encoding sa terminal. Bilang alternatibo, gamitin ang Windows Terminal na may mas mahusay na suporta sa Unicode.

### Pag-debug ng Mga Tawag sa API

**Isyu**: Mga error sa authentication, mga limitasyon sa rate, o hindi inaasahang mga tugon mula sa modelo ng AI

**Solusyon**: Kasama sa mga halimbawa ang `.logRequests(true)` at `.logResponses(true)` upang ipakita ang mga tawag sa API sa console. Nakakatulong ito sa pag-troubleshoot ng mga error sa authentication, mga limitasyon sa rate, o hindi inaasahang mga tugon. Alisin ang mga flag na ito sa produksyon upang mabawasan ang ingay ng log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatakwil**:  
Ang dokumentong ito ay isinalin gamit ang serbisyong AI na pagsasalin [Co-op Translator](https://github.com/Azure/co-op-translator). Bagaman aming pinagsisikapang maging tumpak, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tumpak na impormasyon. Ang orihinal na dokumento sa likas nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na magmumula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->