# Module 01: LangChain4jతో మొదలు పెట్టడం

## Table of Contents

- [మీకు నేర్చుకోవాల్సినవి](../../../01-introduction)
- [ముందస్తు అవశ్యకతలు](../../../01-introduction)
- [మూల సమస్య అర్థం చేసుకోవడం](../../../01-introduction)
- [టోకెన్స్ అర్థం చేసుకోవడం](../../../01-introduction)
- [మెమరీ ఎలా పనిచేస్తుంది](../../../01-introduction)
- [ఇది LangChain4jని ఎలా ఉపయోగిస్తుంది](../../../01-introduction)
- [Azure OpenAI ఇన్‌ఫ్రాస్ట్రక్చర్‌ను డిప్లాయ్ చేయడం](../../../01-introduction)
- [యాప్‌ను లోకల్‌గా నడపడం](../../../01-introduction)
- [యాప్ వినియోగం](../../../01-introduction)
  - [Stateless చాట్ (ఎడమ ప్యానెల్)](../../../01-introduction)
  - [Stateful చాట్ (కుడి ప్యానెల్)](../../../01-introduction)
- [తరువాతి దశలు](../../../01-introduction)

## మీకు నేర్చుకోవాల్సినవి

మీరు క్విక్ స్టార్ట్ పూర్తి చేసినట్లయితే, మీరు కింద పంపిన ప్రాంప్ట్స్ ఎలా పంపించాలో మరియు స్పందనలు ఎలా అందుకోవాలో చూసారు. అది బేసిక్, కానీ నిజమైన యాప్స్‌కు ఇంకా అవసరమవుతుంది. ఈ మాడ్యూల్ మీకు కాంటెక్స్‌ను గుర్తుంచుకునే మరియు స్థితిని పట్టు చేసే సంభాషణాత్మక AI ని ఎలా తయారు చేయాలో నేర్పిస్తుంది - ఇది ఒక ఆఫ్ డెమోతో ప్రొడక్షన్-్రెడీ యాప్‌ మధ్య తేడా.

మేము ఈ గైడ్‌లో Azure OpenAI యొక్క GPT-5.2ని ఉపయోగిస్తారు, ఎందుకంటే దీని అధునాతన తర్క శక్తులు వివిధ ప్యాటర్న్స్ ప్రవర్తనను స్పష్టంగా చూపిస్తాయి. మీరు మెమరీని జత చేస్తే, తేడా బాగా కనపడును. ఇది ప్రతి భాగం మీ యాప్‌కు ఏమి తీసుకు వస్తుందో అర్థం చేసుకోవడం సులభం చేస్తుంది.

మీరు ఒక యాప్‌ను తయారు చేయబోతున్నారు ఇది రెండు ప్యాటర్న్స్ ను చూపిస్తుంది:

**Stateless Chat** - ప్రతి అభ్యర్థన స్వతంత్రం. మోడల్ గత సందేశాలను గుర్తుంచుకోదు. ఇది మీరు క్విక్ స్టార్ట్‌లో ఉపయోగించిందే ప్యాటర్న్.

**Stateful Conversation** - ప్రతి అభ్యర్థనలో సంభాషణ చరిత్ర ఉంటుందీ. మోడల్ పలు మలుపులలో కాంటెక్స్ ని పట్టు కుంటుంది. ఇది ప్రొడక్షన్ యాప్స్‌కు అవసరం.

## ముందస్తు అవశ్యకతలు

- Azure సబ్‌స్క్రిప్షన్ మరియు Azure OpenAI యాక్సెస్
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **గమనిక:** Java, Maven, Azure CLI మరియు Azure Developer CLI (azd) ప్రీ-ఇన్‌స్టాల్ చేయబడి devcontainer లో అందుబాటులో ఉన్నాయి.

> **గమనిక:** ఈ మాడ్యూల్ Azure OpenAIపై GPT-5.2ను ఉపయోగిస్తుంది. `azd up` ద్వారా డిప్లాయ్ ఆటోమేటిక్‌గా జరుగుతుంది. కోడ్‌లో మోడల్ పేరు మార్చవద్దు.

## మూల సమస్య అర్థం చేసుకోవడం

భాషా మోడల్స్ stateless గా ఉంటాయి. ప్రతి API కాల్ స్వతంత్రం. మీరు "నా పేరు జాన్" అని పంపించి, తర్వాత "నా పేరు ఏంటి?" అడగగానే, మోడల్ మీరు నేను పరిచయం చేశాను అనేది తెలియదు. ఇది ప్రతి అభ్యర్థనను మీరు ఇప్పటివరకు చేసే మొదటి సంభాషణగా తీసుకుంటుంది.

సాధారణ ప్రశ్నోత్తరాలకు ఇది సరైంది, కానీ నిజమైన యాప్స్‌కి ఉపయుక్తం కాదు. కస్టమర్ సర్వీస్ బాట్స్ మీరు చెప్పినదాన్ని గుర్తుంచుకోవాలి. వ్యక్తిగత సహాయకులు కాంటెక్స్ అవసరం. ఏ మల্টీ-టర్న్ సంభాషణకైనా మెమరీ అవసరం.

<img src="../../../translated_images/te/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (స్వతంత్ర కాల్స్) మరియు Stateful (కాంటెక్స్-అవగాహన) సంభాషణల మధ్య తేడా*

## టోకెన్స్ అర్థం చేసుకోవడం

సంభాషణల్లోకి దూరం కాకముందు, టోకెన్స్ గురించి తెలుసుకోవడం ముఖ్యం - భాషా మోడల్స్ ప్రాసెస్ చేసే మౌలిక టెక్స్ట్ యూనిట్స్:

<img src="../../../translated_images/te/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ఎలా టెక్స్ట్‌ను టోకెన్స్‌గా విడగొడతారు ఉదాహరణ - "I love AI!" 4 ప్రత్యేక ప్రాసెసింగ్ యూనిట్స్‌గా మారుతుంది*

టోకెన్స్ AI మోడల్స్ టెక్స్ట్‌ను ఎలా కొలిచుతాయో మరియు ప్రాసెస్ చేస్తాయో సూచిస్తాయి. పదాలు, పంక్తిచిహ్నాలు, ఇంతకు మించి ఖాళీలు కూడా టోకెన్స్ అయి ఉండవచ్చు. మీ మోడల్ ఒకసారి ప్రాసెస్ చేయగలిగే టోకెన్స్ పరిమితి ఉంది (GPT-5.2 కోసం 400,000, అందులో 272,000 ఇన్‌పుట్ టోకెన్స్ మరియు 128,000 అవుట్పుట్ టోకెన్స్). టోకెన్స్ అర్థం చేసుకోవడం సంభాషణల పొడవు మరియు ఖర్చులను సమర్థవంతంగా నిర్వహించడాన్ని సహాయపడుతుంది.

## మెమరీ ఎలా పనిచేస్తుంది

చాట్ మెమరీ stateless సమస్యను సంభాషణ చరిత్రను పట్టు కొనేంద్వారా పరిష్కరిస్తుంది. మీ అభ్యర్థనను మోడల్‌కి పంపించే ముందు, ఫ్రేమ్‌వర్క్ సంబంధిత పూర్వ సందేశాలను ముందుగా జత చేస్తుంది. మీరు "నా పేరు ఏంటి?" అడిగినపుడు, వ్యవస్థ మొత్తం సంభాషణ చరిత్రను పంపుతుంది, కాబట్టి మోడల్ మీరు "నా పేరు జాన్" అనే విషయం తెలుసుకుంటుంది.

LangChain4j మెమరీ అమలులను ఆటోమాటిక్‌గా నిర్వహిస్తుంది. మీరు ఎన్ని సందేశాలను ఉంచాలో ఎంపిక చేసుకోవచ్చు, ఫ్రేమ్‌వర్క్ కాంటెక్స్ విండోను నిర్వహిస్తుంది.

<img src="../../../translated_images/te/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory తాజా సందేశాల స్లైడింగ్ విండోను నిర్వహిస్తుంది, పాతవాటిని ఆటోమాటిక్‌గా తీసేస్తుంది*

## ఇది LangChain4jని ఎలా ఉపయోగిస్తుంది

ఈ మాడ్యూల్ క్విక్ స్టార్ట్‌ను విస్తరించి Spring Bootని ఇంటిగ్రేట్ చేసి సంభాషణ మెమరీని జత చేస్తుంది. భాగాలు ఇలా కలవుతాయి:

**ఆధారాలు** - రెండు LangChain4j లైబ్రరీలను జత చేయండి:

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

**చాట్ మోడల్** - Azure OpenAIని Spring బీన్‌గా కాన్ఫిగర్ చేయండి ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

బిల్డర్ `azd up` ద్వారా సెట్ చేసిన ఎన్విరాన్మెంట్ వేరియబుల్స్ నుండి క్రెడెన్షియల్స్‌ను చదవును. `baseUrl` తో మీ Azure ఎండ్‌పాయింట్ సెట్ చేస్తే OpenAI క్లయింట్ Azure OpenAIతో పనిచేస్తుంది.

**సంభాషణ మెమరీ** - MessageWindowChatMemory ద్వారా చాట్ చరిత్రను ట్రాక్ చేయండి ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)`తో మెమరీని క్రియేట్ చేయండి, తద్వారా చివరి 10 సందేశాలను ఉంచుతుంది. Typed wrappers తో యూజర్ మరియు AI సందేశాలను జత చేయండి: `UserMessage.from(text)`, `AiMessage.from(text)`. చరిత్రను `memory.messages()` ద్వారా తీసుకుని మోడల్‌కు పంపండి. సర్వీస్ సంభాషణ ID ప్రాతిపదికన వేరే వేరే మెమరీ ఇన్‌స్టాన్సులను నిల్వ చేస్తుంది, ఇది బహుళ యూజర్లకు ఒకేసారి చాట్ చేయడానికి అనుమతిస్తుంది.

> **🤖 GitHub Copilotతో ప్రయత్నించండి:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) తెరిచి అడగండి:
> - "MessageWindowChatMemory విండో పూర్తి అయినప్పుడు ఎలాంటి సందేశాలను పోగొట్టాలని ఎలా నిర్ణయిస్తుంది?"
> - "ఇన్-మెమరీ కాకుండా డేటాబేస్ ఉపయోగించి కస్టమ్ మెమరీ నిల్వ ఎలా అమలు చేయవచ్చు?"
> - "పాత సంభాషణ చరిత్రను సారాంశం చేయడానికి ఎలా జత చేయాలి?"

Stateless చాట్ ఎండ్‌పాయింట్ మెమరీని పూర్తిగా తప్పిస్తుంది - కేవలం `chatModel.chat(prompt)` విధంగా క్విక్ స్టార్ట్‌లా ఉంటుంది. Stateful ఎండ్‌పాయింట్ సందేశాలను మెమరీలో జత చేస్తుంది, చరిత్రను తెచ్చుకుంటుంది, ప్రతీ అభ్యర్థన వద్ద ఆ కాంటెక్స్ తో సహా పంపుతుంది. అదే మోడల్ కాన్ఫిగరేషన్, తేడా ప్యాటర్న్స్.

## Azure OpenAI ఇన్‌ఫ్రాస్ట్రక్చర్‌ను డిప్లాయ్ చేయడం

**Bash:**
```bash
cd 01-introduction
azd up  # సభ్యత్వం మరియు స్థానం (eastus2 సిఫార్సు) ఎంచుకోండి
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # సబ్‌స్క్రిప్షన్ మరియు స్థలాన్ని ఎంచుకోండి (eastus2 సిఫార్సు చేయబడింది)
```

> **గమనిక:** మీరు టైమ్-అవుట్ error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) చూడగానే, కేవలం `azd up` మళ్ళీ నడిపించండి. Azure రిసోర్సులు ఇంకా బ్యాక్‌గ్రౌండ్‌లో ప్రొవిజనింగ్‌లో ఉండొచ్చు, పునఃప్రయత్నం వలన డిప్లాయ్ పూర్తవుతుంది.

ఇది చేస్తుంది:
1. Azure OpenAI రిసోర్స్‌ను GPT-5.2 మరియు text-embedding-3-small మోడల్స్ తో డిప్లాయ్ చేయడం
2. ప్రాజెక్ట్ రూట్‌లో క్రెడెన్షియల్స్‌తో ఆటోమేటిక్‌గా `.env` ఫైల్ జనరేట్ చేయడం
3. అవసరమైన అన్నీ ఎన్విరాన్మెంట్ వేరియబుల్స్ సెట్ చేయడం

**డిప్లాయ్ సమస్యలుంటే?** [Infrastructure README](infra/README.md) చూడండి, దీనిలో సబ్‌డొమైన్ నేమ్ కాన్ఫ్లిక్టులు, మాన్యువల్ Azure పోర్టల్ ద్వారా డిప్లాయ్‌మెంట్ స్టెప్స్ మరియు మోడల్ కాన్ఫిగరేషన్ గురించి వివరాలు ఉన్నాయి.

**డిప్లాయ్ విజయవంతమైందో లేదో నిర్ధారించుకోండి:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY మొదలగునవి చూపించాలి.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, మొదలైన వాటిని చూపించాలి.
```

> **గమనిక:** `azd up` ఆదేశం ఆటోమేటిక్‌గా `.env` ఫైల్ సృష్టిస్తుంది. తరువాత ఇది అప్డేట్ చేయాలనిపిస్తే, మీరు లేదా `.env` ఫైల్‌ను మానువల్‌గా ఎడిట్ చేయవచ్చు లేదా క్రింది ఆదేశంతో పునఃసృష్టించవచ్చు:
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


## యాప్‌ను లోకల్‌గా నడపడం

**డిప్లాయ్ నిర్ధారించుకోండి:**

Azure క్రెడెన్షియల్స్‌తో `.env` ఫైల్ రూట్ డైరెక్టరీలో ఉందని చూసుకోండి:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENTని చూపించాలి
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT చూపించాలి
```

**యాప్స్ ప్రారంభించండి:**

**ఆప్షన్ 1: Spring Boot డ్యాష్‌బోర్డ్ ఉపయోగించడం (VS Code యూజర్లకు సిఫార్సు)**

Devcontainerలో Spring Boot డ్యాష్‌బోర్డ్ ఎక్స్‌టెన్షన్ ఉంది, ఇది అన్ని Spring Boot యాప్స్‌ను విజువల్ ఇంటర్‌ఫేస్‌లో నిర్వహిస్తుంది. మీరు దీన్ని VS Code Activity Bar ఎడమ వైపున(Spring Boot చిహ్నం గా) కనుగొంటారు.

Spring Boot డ్యాష్‌బోర్డ్ నుండి మీరు:
- వర్క్‌స్పేస్‌లో అందుబాటులో ఉన్న అన్ని Spring Boot యాప్స్ చూడవచ్చు
- ఒక క్లిక్ తో యాప్స్ ప్రారంభించడం/ఆపడం చేయవచ్చు
- యాప్ లాగ్స్‌ని రియల్-టైమ్‌లో చూడండి
- యాప్ స్థితిని పర్యవేక్షించండి

"introduction" పక్కన ఉన్న ప్లే బటన్ క్లిక్ చేయండి లేదా అన్ని మాడ్యూల్స్‌ను ఒకేసారి ప్రారంభించండి.

<img src="../../../translated_images/te/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ఆప్షన్ 2: షెల్ స్క్రిప్ట్స్ ఉపయోగించడం**

అన్నీ వెబ్ యాప్లను (మాడ్యూల్స్ 01-04) ప్రారంభించండి:

**Bash:**
```bash
cd ..  # రూట్ డైరెక్టరీ నుండి
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # రూట్ డైరెక్టరీ నుండి
.\start-all.ps1
```

లేదా కేవలం ఈ మాడ్యూల్ ని ప్రారంభించండి:

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

రెండు స్క్రిప్ట్‌లు ఆటోమేటిగ్గా రూట్ `.env` ఫైల్ నుండి ఎన్విరాన్మెంట్ వేరియబుల్స్‌ను లోడ్ చేస్తాయి మరియు JARలు లేకపోతే వాటిని బిల్డ్ చేస్తాయి.

> **గమనిక:** మీరు మొదటరు మాడ్యూల్స్ అన్ని మాన్యువల్‌గా బిల్డ్ చేశాక ప్రారంభించాలనుకుంటే:
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


బ్రౌజర్‌లో http://localhost:8080 తెరవండి.

**ఆపడానికి:**

**Bash:**
```bash
./stop.sh  # ఈ మాడ్యూల్ మాత్రమే
# లేదా
cd .. && ./stop-all.sh  # అన్ని మాడ్యూల్స్
```

**PowerShell:**
```powershell
.\stop.ps1  # ఈ మాడ్యూల్ మాత్రమే
# లేదా
cd ..; .\stop-all.ps1  # అన్ని మాడ్యూల్స్
```


## యాప్ వినియోగం

యాప్ రెండు చాట్ అమలులను సైడ్-బై-సైడ్ వెబ్ ఇంటర్‌ఫేస్లో అందిస్తుంది.

<img src="../../../translated_images/te/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*సాధారణ చాట్ (stateless) మరియు సంభాషణ చాట్ (stateful) ఆప్షన్లతో డ్యాష్‌బోర్డ్*

### Stateless Chat (ఎడమ ప్యానెల్)

ముందుగా దీన్ని ప్రయత్నించండి. "నా పేరు జాన్" అని అడిగి, తక్షణమే "నా పేరు ఏంటి?" అడగండి. మోడల్ గుర్తుంచుకోదు ఎందుకంటే ప్రతి సందేశం స్వతంత్రం. ఇది ప్రాథమిక భాషా మోడల్ ఇంటిగ్రేషన్ లోని మూల సమస్యను చూపిస్తుంది - సంభాషణ కాంటెక్స్ లేదు.

<img src="../../../translated_images/te/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ముందు సందేశం నుండి మీ పేరును గుర్తు పెట్టుకోవడం లేదు*

### Stateful Chat (కుడి ప్యానెల్)

ఇప్పుడు ఇదే సీక్వెన్స్ ఇక్కడ ప్రయత్నించండి. "నా పేరు జాన్" అడిగి, తరువాత "నా పేరు ఏంటి?" అడగండి. ఇప్పుడు అది గుర్తుంచుకుంటుంది. తేడా MessageWindowChatMemoryలో ఉంది - ఇది సంభాషణ చరిత్రను పట్టు కుంటుంది మరియు ప్రతీ అభ్యర్థనలో జత చేస్తుంది. ఇది ప్రొడక్షన్ సంభాషణ AI ఎలా పనిచేస్తుందో చూపిస్తుంది.

<img src="../../../translated_images/te/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI మీ పేరును సంభాషణ ముందస్తు భాగం నుండి గుర్తు పెట్టుకుంటుంది*

రెండు ప్యానెల్స్ కూడా అదే GPT-5.2 మోడల్‌ని ఉపయోగిస్తాయి. తేడా ఒక్క మెమరీ. ఇది మీ యాప్‌కు మెమరీ ఎలాంటి ఉపయోగం కలిగిస్తుంది మరియు నిజ జీవితంలో ఎందుకు అవసరమో స్పష్టం చేస్తుంది.

## తదుపరి దశలు

**తదుపరి మాడ్యూల్:** [02-prompt-engineering - GPT-5.2తో ప్రాంప్ట్ ఇంజనీరింగ్](../02-prompt-engineering/README.md)

---

**నేవిగేషన్:** [← మునుపటి: Module 00 - Quick Start](../00-quick-start/README.md) | [ముఖ్య పేజీకి తిరుగండి](../README.md) | [తరువాత: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టత**:
ఈ డాక్యూమెంట్‌ను AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము సక్రమత కోసం ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలలో తప్పులు లేదా అస్పష్టతలు ఉండవచ్చు. అసలు డాక్యూమెంట్ దాని స్వదేశ భాషలోనే అధికారిక మూలమైనదిగా భావించబడాలి. కీలకమైన సమాచారాల కోసం, వృత్తిపరమైన మానవ అనువాదం చేయించడం薦ించబడింది. ఈ అనువాదానికి సంబంధించిన గందరగోళం లేదా తప్పుదిట్టుల విషయంలో మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->