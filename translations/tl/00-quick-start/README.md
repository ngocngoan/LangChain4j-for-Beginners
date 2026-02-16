# Module 00: Mabilisang Simula

## Table of Contents

- [Panimula](../../../00-quick-start)
- [Ano ang LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Mga Kinakailangan](../../../00-quick-start)
- [Pagsasaayos](../../../00-quick-start)
  - [1. Kunin ang Iyong GitHub Token](../../../00-quick-start)
  - [2. Itakda ang Iyong Token](../../../00-quick-start)
- [Patakbuhin ang mga Halimbawa](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Mga Pattern ng Prompt](../../../00-quick-start)
  - [3. Pagtawag ng Function](../../../00-quick-start)
  - [4. Dokumento Q&A (RAG)](../../../00-quick-start)
  - [5. Responsable AI](../../../00-quick-start)
- [Ano ang Ipinapakita ng Bawat Halimbawa](../../../00-quick-start)
- [Mga Susunod na Hakbang](../../../00-quick-start)
- [Paglutas ng Problema](../../../00-quick-start)

## Panimula

Ang mabilisang simula na ito ay nilalayong mapasimula ka agad na gumamit ng LangChain4j nang mabilis hangga't maaari. Saklaw nito ang mga pinaka-pangunahing kaalaman sa paggawa ng mga AI application gamit ang LangChain4j at GitHub Models. Sa mga susunod na module gagamit ka ng Azure OpenAI kasama ang LangChain4j upang makabuo ng mas advanced na mga aplikasyon.

## Ano ang LangChain4j?

Ang LangChain4j ay isang Java library na nagpapadali sa paggawa ng mga AI-powered na application. Sa halip na asikasuhin ang HTTP clients at JSON parsing, gumagana ka sa malilinaw na Java APIs. 

Ang "chain" sa LangChain ay tumutukoy sa pagsasama-sama ng maraming mga bahagi - maaaring pagdugtungin mo ang isang prompt sa isang modelo papunta sa isang parser, o pagdugtungin ang maraming tawag sa AI kung saan ang isang output ay nagsisilbing input sa susunod. Nakatuon ang mabilisang simula na ito sa mga batayan bago tuklasin ang mas komplikadong chain.

<img src="../../../translated_images/tl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Pagsasama-sama ng mga bahagi sa LangChain4j - nag-uugnay ang mga block upang makalikha ng makapangyarihang AI workflows*

Gagamit tayo ng tatlong pangunahing bahagi:

**ChatLanguageModel** - Ang interface para sa pakikipag-ugnayan sa AI model. Tawagan ang `model.chat("prompt")` at makakuha ng sagot na string. Ginagamit natin ang `OpenAiOfficialChatModel` na gumagana sa mga OpenAI-compatible na endpoints tulad ng GitHub Models.

**AiServices** - Gumagawa ng type-safe AI service interfaces. Magdefine ng mga method, lagyan ng anotasyong `@Tool`, at inorganisa ito ng LangChain4j. Tawagan ng AI ang iyong mga Java method nang awtomatiko kapag kailangan.

**MessageWindowChatMemory** - Pinananatili ang kasaysayan ng usapan. Kung wala ito, bawat request ay hiwalay. Sa paggamit nito, natatandaan ng AI ang mga naunang mensahe at napapanatili ang konteksto sa iba't ibang turn.

<img src="../../../translated_images/tl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arkitektura ng LangChain4j - mga pangunahing bahagi na nagtutulungan para paandarin ang iyong mga AI application*

## LangChain4j Dependencies

Gumagamit ang mabilisang simula na ito ng dalawang Maven dependencies sa [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Ang `langchain4j-open-ai-official` na module ang nagbibigay ng `OpenAiOfficialChatModel` class na kumokonekta sa mga OpenAI-compatible API. Ginagamit ng GitHub Models ang parehong format ng API, kaya hindi kailangan ng espesyal na adapter - ituro lang ang base URL sa `https://models.github.ai/inference`.

## Mga Kinakailangan

**Gumagamit ng Dev Container?** Nakainstall na ang Java at Maven. Kailangan mo lang ng GitHub Personal Access Token.

**Local Development:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (mga tagubilin sa ibaba)

> **Tandaan:** Ginagamit ng module na ito ang `gpt-4.1-nano` mula sa GitHub Models. Huwag baguhin ang pangalan ng modelo sa code - ito ay nakakonpigura upang gumana sa mga available na modelo ng GitHub.

## Pagsasaayos

### 1. Kunin ang Iyong GitHub Token

1. Pumunta sa [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. I-click ang "Generate new token"
3. Magtakda ng isang maikling pangalan (hal., "LangChain4j Demo")
4. Itakda ang expiration (inirerekomenda ang 7 araw)
5. Sa ilalim ng "Account permissions", hanapin ang "Models" at itakda sa "Read-only"
6. I-click ang "Generate token"
7. Kopyahin at itago ang iyong token - hindi mo na ito makikita muli

### 2. Itakda ang Iyong Token

**Opsyon 1: Sa VS Code (Inirerekomenda)**

Kung gumagamit ka ng VS Code, idagdag ang iyong token sa `.env` file sa project root:

Kung wala pang `.env` file, kopyahin ang `.env.example` bilang `.env` o gumawa ng bagong `.env` file sa root ng proyekto.

**Halimbawa ng `.env` file:**
```bash
# Nasa /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Pagkatapos, maaari kang mag-right-click ng kahit anong demo file (hal., `BasicChatDemo.java`) sa Explorer at piliin ang **"Run Java"** o gamitin ang launch configurations mula sa Run and Debug panel.

**Opsyon 2: Sa Terminal**

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

**Sa VS Code:** Mag-right-click lang sa kahit anong demo file sa Explorer at piliin ang **"Run Java"**, o gamitin ang launch configurations mula sa Run and Debug panel (siguraduhing nailagay mo muna ang iyong token sa `.env` file).

**Gamit ang Maven:** Bilang alternatibo, maaari kang magpatakbo mula sa command line:

### 1. Basic Chat

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

### 3. Pagtawag ng Function

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Awtomatikong tinatawagan ng AI ang iyong mga Java method kapag kailangan.

### 4. Dokumento Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Magtanong tungkol sa nilalaman ng `document.txt`.

### 5. Responsable AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Tingnan kung paano hinaharang ng AI safety filters ang mapanganib na nilalaman.

## Ano ang Ipinapakita ng Bawat Halimbawa

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Simulan dito upang makita ang pinakapayak na gamit ng LangChain4j. Gumawa ka ng `OpenAiOfficialChatModel`, magpadala ng prompt gamit ang `.chat()`, at makakuha ng sagot. Ipinapakita nito ang pundasyon: paano mag-initialize ng mga modelo na may custom na endpoints at API keys. Kapag naintindihan mo ito, lalong tatangkilikin ang iba pang bahagi.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) at itanong:
> - "Paano ako lilipat mula sa GitHub Models papuntang Azure OpenAI sa code na ito?"
> - "Anu-ano pang mga parameter ang maaari kong i-configure sa OpenAiOfficialChatModel.builder()?"
> - "Paano ako magdadagdag ng streaming responses sa halip na maghintay ng buong sagot?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Ngayon na alam mo na kung paano makipag-usap sa isang modelo, tuklasin natin kung ano ang sinasabi mo rito. Ginagamit ng demo na ito ang parehong setup ng modelo ngunit nagpapakita ng limang iba't ibang pattern ng prompting. Subukan ang zero-shot prompts para sa direktang mga tagubilin, few-shot prompts na natututo mula sa mga halimbawa, chain-of-thought prompts na nagpapakita ng mga hakbang ng pag-iisip, at role-based prompts na nagse-set ng konteksto. Makikita mo kung paano nagbibigay ang parehong modelo ng iba’t ibang resulta depende sa iyong pag-frame ng tanong.

Ipinapakita rin ng demo ang mga prompt template, na isang makapangyarihang paraan upang gumawa ng mga reusable prompts na may mga variable.
Ang halimbawa sa ibaba ay nagpapakita ng prompt gamit ang LangChain4j `PromptTemplate` para punan ang mga variable. Sasagot ang AI base sa ibinigay na destinasyon at aktibidad.

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
> - "Paano nakakaapekto ang temperature parameter sa mga sagot ng modelo?"
> - "Anu-ano ang mga teknika para maiwasan ang prompt injection attacks sa production?"
> - "Paano ako makakagawa ng reusable PromptTemplate objects para sa mga karaniwang pattern?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dito nagiging makapangyarihan ang LangChain4j. Gagamitin mo ang `AiServices` upang lumikha ng AI assistant na maaaring tumawag ng iyong mga Java method. Lagyan lang ng annotation na `@Tool("description")` ang mga method at ang LangChain4j na ang bahala - awtomatikong pinipili ng AI kung kailan gagamitin ang bawat tool base sa hinihingi ng user. Ipinapakita nito ang function calling, isang mahalagang teknik para gumawa ng AI na kayang gumawa ng aksyon, hindi lang sumagot ng tanong.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) at itanong:
> - "Paano gumagana ang @Tool annotation at ano ang ginagawa ng LangChain4j dito sa likod ng eksena?"
> - "Puwede bang tumawag ang AI ng maraming tools sunod-sunod para lutasin ang komplikadong problema?"
> - "Ano ang nangyayari kapag may tool na nag-throw ng exception - paano ko dapat hawakan ang mga error?"
> - "Paano ko isasama ang totoong API sa halip na ang calculator example na ito?"

**Document Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Dito makikita mo ang pundasyon ng RAG (retrieval-augmented generation). Sa halip na umasa sa training data ng modelo, nag-load ka ng nilalaman mula sa [`document.txt`](../../../00-quick-start/document.txt) at isinasama ito sa prompt. Sumagot ang AI base sa iyong dokumento, hindi sa karaniwang kaalaman nito. Ito ang unang hakbang patungo sa paggawa ng mga sistema na kayang gumana gamit ang sariling iyong datos.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Tandaan:** Ang simpleng paraan na ito ay inilalagay ang buong dokumento sa prompt. Para sa malalaking file (>10KB), lalampas ka sa limitasyon ng konteksto. Tatalakayin sa Module 03 ang chunking at vector search para sa production RAG systems.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) at itanong:
> - "Paano napipigilan ng RAG ang AI hallucinations kumpara sa paggamit ng training data ng modelo?"
> - "Ano ang pagkakaiba ng simpleng paraan na ito at ang paggamit ng vector embeddings para sa retrieval?"
> - "Paano ko mapapalawak ito para hawakan ang maraming dokumento o mas malalaking knowledge base?"
> - "Ano ang mga pinakamahusay na praktis sa pag-istraktura ng prompt para siguraduhing ginagamit lang ng AI ang ibinigay na konteksto?"

**Responsable AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Gumawa ng AI safety gamit ang defense in depth. Ipinapakita ng demo na ito ang dalawang layer ng proteksyon na nagtutulungan:

**Bahagi 1: LangChain4j Input Guardrails** - Hinaharang ang mapanganib na mga prompt bago pa sila makarating sa LLM. Gumawa ng custom guardrails na sumusuri sa mga ipinagbabawal na keyword o pattern. Pinapatakbo ito sa iyong code, kaya mabilis at libre.

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

**Bahagi 2: Provider Safety Filters** - May built-in filters ang GitHub Models na nahuhuli ang maaaring hindi maabutan ng guardrails mo. Makikita mo ang hard blocks (HTTP 400 errors) para sa malulubhang paglabag at soft refusals kung saan magalang na tinatanggihan ng AI.

> **🤖 Subukan gamit ang [GitHub Copilot](https://github.com/features/copilot) Chat:** Buksan ang [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) at itanong:
> - "Ano ang InputGuardrail at paano ako gagawa ng sarili kong guardrail?"
> - "Ano ang pagkakaiba ng hard block at soft refusal?"
> - "Bakit dapat pagsabayin ang guardrails at provider filters?"

## Mga Susunod na Hakbang

**Susunod na Module:** [01-introduction - Pagsisimula sa LangChain4j at gpt-5 sa Azure](../01-introduction/README.md)

---

**Navigation:** [← Balik sa Main](../README.md) | [Susunod: Module 01 - Panimula →](../01-introduction/README.md)

---

## Paglutas ng Problema

### Unang Beses na Maven Build

**Isyu**: Unang `mvn clean compile` o `mvn package` ay tumatagal nang matagal (10-15 minuto)

**Sanhi**: Kailangang i-download ng Maven ang lahat ng project dependencies (Spring Boot, LangChain4j libraries, Azure SDKs, atbp.) sa unang build.

**Solusyon**: Normal ito. Mas mabilis ang mga susunod na build dahil naka-cache na ang dependencies sa lokal. Nakadepende ang oras ng pag-download sa bilis ng iyong koneksyon sa internet.
### PowerShell Maven Command Syntax

**Isyu**: Nabibigo ang mga Maven command na may error na `Unknown lifecycle phase ".mainClass=..."`

**Dahilan**: Tinuturing ng PowerShell ang `=` bilang operator ng pag-assign ng variable, kaya nasisira ang syntax ng property ng Maven

**Solusyon**: Gamitin ang stop-parsing operator na `--%` bago ang Maven command:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Sinasabi ng operator na `--%` sa PowerShell na ipasa ang lahat ng natitirang argumento nang literal sa Maven nang walang interpretasyon.

### Windows PowerShell Emoji Display

**Isyu**: Ang mga sagot ng AI ay nagpapakita ng mga sirang karakter (e.g., `????` o `â??`) sa halip na emojis sa PowerShell

**Dahilan**: Hindi sinusuportahan ng default encoding ng PowerShell ang UTF-8 emojis

**Solusyon**: Patakbuhin ang command na ito bago mag-execute ng mga Java application:
```cmd
chcp 65001
```

Pinipilit nito ang UTF-8 encoding sa terminal. Bilang alternatibo, gamitin ang Windows Terminal na may mas mahusay na suporta sa Unicode.

### Pag-de-debug ng Mga Tawag sa API

**Isyu**: Mga error sa authentication, rate limits, o hindi inaasahang mga tugon mula sa AI model

**Solusyon**: Kasama sa mga halimbawa ang `.logRequests(true)` at `.logResponses(true)` para ipakita ang mga tawag sa API sa console. Nakakatulong ito para ma-troubleshoot ang mga error sa authentication, rate limits, o hindi inaasahang mga tugon. Alisin ang mga flags na ito sa production upang mabawasan ang ingay sa log.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paunawa**:
Ang dokumentong ito ay isinalin gamit ang serbisyong pagsasalin ng AI na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o di-tumpak na impormasyon. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->