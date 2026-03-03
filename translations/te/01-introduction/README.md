# మոդ్యూల్ 01: LangChain4j తో ప్రారంభించడం

## విషయాల పట్టిక

- [వీడియో వాక్త్రూథ్](../../../01-introduction)
- [మీరు ఏం నేర్చుకుంటారు](../../../01-introduction)
- [ఆవశ్యకతలు](../../../01-introduction)
- [కోర్ సమస్యను అవగాహన చేసుకోవడం](../../../01-introduction)
- [టోకెన్లను అర్థం చేసుకోవడం](../../../01-introduction)
- [మెమరీ ఎలా పనిచేస్తుంది](../../../01-introduction)
- [ఇది LangChain4j ను ఎలా ఉపయోగిస్తుంది](../../../01-introduction)
- [Azure OpenAI ఇంఫ్రాస్ట్రక్చర్ ని డిప్లాయ్ చేయడం](../../../01-introduction)
- [ఆప్లికేషన్ ని లోకల్ గా రన్ చేయడం](../../../01-introduction)
- [ఆప్లికేషన్ ఉపయోగించడం](../../../01-introduction)
  - [Stateless చాట్ (ఎడమ ప్యానెల్)](../../../01-introduction)
  - [Stateful చాట్ (కుడి ప్యానెల్)](../../../01-introduction)
- [తదుపరి దశలు](../../../01-introduction)

## వీడియో వాక్త్రూథ్

ఈ మోడ్యూల్ తో ఎలా ప్రారంభించాలో వివరిస్తున్న లైవ్ సెషన్ ను వీక్షించండి:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## మీరు ఏం నేర్చుకుంటారు

క్విక్ స్టార్ట్ లో, మీరు GitHub Models ఉపయోగించి ప్రాంప్ట్స్ పంపడం, టూల్స్ ను పిలవడం, RAG పైపు లైన్ నిర్మించడం, మరియు గార్డ్‌రైల్స్ ను పరీక్షించడం జరిగింది. ఆ డెమోలు సాధ్యమయ్యే దేన్ని చూపించాయి — ఇప్పుడు మనం Azure OpenAI మరియు GPT-5.2 కి మారి ప్రొడక్షన్ తరహా ఆప్లికేషన్లు తయారు చేయడం ప్రారంభిద్దాం. ఈ మోడ్యూల్ సంభాషణాత్మక AI పై దృష్టి సారిస్తుంది, ఇది సందర్భాన్ని గుర్తుంచుకుని స్థితిని నిర్వహిస్తుంది — అవి ఆ క్విక్ స్టార్ట్ డెమోలు వెనుక ఉపయోగించిన కాన్సెప్టులు కానీ వివరించలేదు.

ఈ గైడ్ లో మొత్తం Azure OpenAI యొక్క GPT-5.2 ని ఉపయోగిస్తాము ఎందుకంటే దీని ఆవిష్కృతమైన తర్క సామర్థ్యాలు వేర్వేరు నమూనాల ప్రవర్తన స్పష్టంగా తెలియజేస్తాయి. మీరు మెమరీ కలుపుకున్నప్పుడు, తేడా స్పష్టంగా కనిపిస్తుంది. ఇది ప్రతి భాగం మీ ఆప్లికేషన్ కు ఏం తెస్తుందో అర్థం చేసుకోవడం సులభంగా చేస్తుంది.

మీరు ఇద్దరు నమూనాలను చూపించే ఒక ఆప్లికేషన్ నిర్మించబోతున్నారు:

**Stateless చాట్** - ప్రతి అభ్యర్థన స్వతంత్రం. మోడల్ గత సందేశాలను గుర్తుంచుకోదు. ఇది మీరు క్విక్ స్టార్ట్ లో ఉపయోగించిన నమూనా.

**Stateful సంభాషణ** - ప్రతి అభ్యర్థనలో సంభాషణ చరిత్ర ఉంటుంది. మోడల్ అనేక మార్లు జరిగిన సంభాషణలో సందర్భాన్ని నిర్వహిస్తుంది. ఇది ప్రొడక్షన్ ఆప్లికేషన్లు అవసరం చేసేదే.

## ఆవశ్యకతలు

- Azure OpenAI యాక్సెస్ ఉన్న Azure సబ్స్క్రిప్షన్
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **గమనిక:** Java, Maven, Azure CLI మరియు Azure Developer CLI (azd) ప్రస్తుత devcontainer లో ముందుగానే ఇన్‌స్టాల్ చేయబడ్డాయి.

> **గమనిక:** ఈ మోడ్యూల్ Azure OpenAI కోసం GPT-5.2 ని ఉపయోగిస్తుంది. డిప్లాయ్ మెంట్ `azd up` ద్వారా ఆటోమేటిక్ గా కాన్ఫిగర్ చేయబడుతుంది — కోడ్ లో మోడల్ పేరును మార్చవద్దు.

## కోర్ సమస్య అవగాహన చేసుకోవడం

భాషా మోడళ్లు Stateless అవుతాయి. ప్రతి API కాల్ స్వతంత్రంగా ఉంటుంది. మీరు "నా పేరు జాన్" అంటే, ఆపై "నా పేరు ఏమిటి?" అడిగితే, మోడల్ మీ పేరు ఇప్పుడే చెప్పినట్లు గుర్తించదు. ఇది ప్రతి అభ్యర్థనను మొట్టమొదటి సంభాషణగా చూస్తుంది.

ఇది సాధారణ ప్రశ్నల కోసం సరైనది కానీ నిజమైన ఆప్లికేషన్లకు ఉపయోగం లేదు. కస్టమర్ సర్వీస్ బోట్లు మీరు చెప్పినది గుర్తుంచుకోవాలి. వ్యక్తిగత సహాయకారి సందర్భాన్ని అవసరం పడతారు. ఏదైనా బహుమార్మిక సంభాషణకు మెమరీ అవసరం.

కింది చిత్రంలో రెండు పద్ధతులను పోల్చారు — ఎడమ వైపున, మీ పేరును మర్చిపోయే Stateless కాల్; కుడి వైపున, ChatMemory తో మద్దతు పొందిన Stateful కాల్ అది గుర్తుంచుకుంటుంది.

<img src="../../../translated_images/te/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (స్వతంత్ర కాల్స్) మరియు Stateful (సందర్భం గుర్తుంచుకున్న) సంభాషణల మధ్య తేడా*

## టోకెన్లను అర్థం చేసుకోవడం

సంభాషణల్లో ముంచెయ్యక ముందు, టోకెన్లను అర్థం చేసుకోవటం ముఖ్యం - భాషా మోడళ్లు ప్రాసెస్ చేసే మూలభూత పాఠ యూనిట్లు:

<img src="../../../translated_images/te/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*పాఠ్యాన్ని టోకెన్లలో ఎలా విడగొడతాడని ఉదాహరణ - "I love AI!" నాలుగు ప్రాసెసింగ్ యూనిట్లుగా మారుతుంది*

టోకెన్లు AI మోడళ్లు పాఠ్యాన్ని కొలుస్తాయి మరియు ప్రాసెస్ చేస్తాయి. పదాలు, పంక్చువేషన్ మరియు ఖాళీలు కూడా టోకెన్లు కావచ్చు. మీ మోడల్ ఒకేసారి ఎంత టోకెన్లు ప్రాసెస్ చేయగలదో (GPT-5.2 కి 400,000 టోకెన్లు, అందులో 272,000 ఇన్పుట్ టోకెన్లు, 128,000 అవుట్‌పుట్ టోకెన్లు) పరిమితి ఉంది. టోకెన్లు అర్థం చేసుకోవడం సంభాషణ పొడవు మరియు ఖర్చులను నియంత్రించడానికి సహాయపడుతుంది.

## మెమరీ ఎలా పనిచేస్తుంది

చాట్ మెమరీ Stateless సమస్యను పరిష్కరిస్తుంది సంభాషణ చరిత్రను నిర్వహించడం ద్వారా. మీ అభ్యర్థనను మోడల్ కి పంపే ముందు, ఫ్రేమ్‌వర్క్ సంబంధిత పూర్వ సందేశాలను ముందువైపు జత చేస్తుంది. మీరు "నా పేరు ఏమిటి?" అడగగానే, సిస్టమ్ మొత్తం సంభాషణ చరిత్ర పంపుతుంది, అందువలన మోడల్ మీరు "నా పేరు జాన్" అని ముందుగా చెప్పినట్లు చూస్తుంది.

LangChain4j మెమరీ అమలు లను ఆటోమేటిక్ గా నిర్వహిస్తుంది. మీరు ఎన్ని సందేశాలు నిల్వ చేయాలో ఎంచుకోండి, ఫ్రేమ్‌వర్క్ సందర్భం విండో ని నిర్వహిస్తుంది. కింది చిత్రం MessageWindowChatMemory ఇటీవల సందేశాల ఒక స్లయిడింగ్ విండో ని ఎలా నిర్వహిస్తుందో చూపిస్తుంది.

<img src="../../../translated_images/te/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ఇటీవల సందేశాల ఒక స్లయిడింగ్ విండో ని నిర్వహిస్తుంది, పాతవి ఆటోమేటిక్ గా తొలగిస్తూ*

## ఇది LangChain4j ను ఎలా ఉపయోగిస్తుంది

ఈ మోడ్యూల్ క్విక్ స్టార్ట్ ని విస్తరించి Spring Boot ఐక్యీకరణతో కలిసి సంభాషణ మెమరీ ని జోడిస్తుంది. భాగాలు ఎలా కలిసిపోతున్నాయో:

**డిపెండెన్సీలు** - రెండు LangChain4j లైబ్రరీలు జోడించండి:

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

**చాట్ మోడల్** - Azure OpenAI ని Spring బీన్ ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)) గా కాన్ఫిగర్ చేయండి:

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

బిల్డర్ `azd up` ద్వారా సెట్ చేసిన ఎన్విరాన్మెంట్ వేరియబుల్స్ నుండి క్రెడెన్షియల్స్ చదవుతుంది. `baseUrl` ను మీ Azure ఎండ్పాయింట్ గా సెట్ చేస్తే OpenAI క్లయింట్ Azure OpenAI తో పనిచేస్తుంది.

**సంభాషణ మెమరీ** - MessageWindowChatMemory తో చాట్ చరిత్ర ట్రాక్ చేయండి ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` తో చివరి 10 సందేశాలు నిల్వ చేయండి. typed wrappers తో యూజర్ మరియు AI సందేశాలు జోడించండి: `UserMessage.from(text)`, `AiMessage.from(text)`. చరిత్రను `memory.messages()` తో పొందండి, మోడల్ కు పంపండి. సర్వీస్ ప్రతి సంభాషణ ID కి ప్రత్యేక మెమరీ ఇన్ స్టాన్స్ ట్రాక్ చేస్తుంది, అంటే ఎన్నో యూజర్లకు ఒకేసారి చాట్ చేయడం సాధ్యం.

> **🤖 GitHub Copilot చాట్ తో ప్రయత్నించండి:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ను ఓపెన్ చేసి అడగండి:
> - "MessageWindowChatMemory విండో పూర్తయ్యినపుడు ఏ సందేశాలు తొలగించాలో ఎలా నిర్ణయిస్తుంది?"
> - "నేను మేమరీ నిల్వ కోసం ఇన్ మెమరీ కాకుండా డేటాబేస్ ఉపయోగించి కస్టమ్ మెమరీ అమలు చేయగలనా?"
> - "పాత సంభాషణ చరిత్రను సంక్షిప్తం చేయడానికి సారాంశాన్ని ఎలా జోడిస్తాను?"

Stateless చాట్ ఎండ్‌పాయింట్ మెమరీని పూర్తిగా వదిలేస్తుంది - కేవలం `chatModel.chat(prompt)` సినిమా క్విక్ స్టార్ట్ లాంటి. Stateful ఎండ్‌పాయింట్ మెసేజ్లు మెమరీకి జోడిస్తుంది, చరిత్ర పొందుతుంది, ప్రతి అభ్యర్థనతో ఆ సందర్భాన్ని కలుపుతుంది. అదే మోడల్ కాన్ఫిగరేషన్, వేర్వేరు నమూనాలు.

## Azure OpenAI ఇంఫ్రాస్ట్రక్చర్‌ను డిప్లాయ్ చేయండి

**Bash:**
```bash
cd 01-introduction
azd up  # సభ్యత్వాన్ని మరియు లొకేషన్ ను ఎంచుకోండి (eastus2 సిఫార్సు చేయబడింది)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # సభ్యత్వం మరియు స్థలాన్ని (eastus2 సిఫార్సు చేసినది) ఎంచుకోండి
```

> **గమనిక:** ఒక టైమౌట్ లోపం (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) వస్తే, `azd up` ని మళ్లీ నడపండి. Azure వనరులు ఇంకా ప్రొవిజనింగ్ లో ఉండొచ్చు, మళ్లీ ప్రయత్నించడం వనరులు తుది స్థితికి చేరుకున్నప్పుడు డిప్లాయ్ మెంట్ పూర్తి అవుతుంది.

ఇది:
1. GPT-5.2 మరియు text-embedding-3-small మోడళ్లు ఉన్న Azure OpenAI వనరును డిప్లాయ్ చేస్తుంది
2. ప్రాజెక్ట్ రూట్ లో క్రెడెన్షియల్స్ తో `.env` ఫైల్ ఆటోమేటిక్ గా జనరేట్ చేస్తుంది
3. అన్ని అవసరమైన ఎన్విరాన్మెంట్ వేరియబుల్స్ సెట్ చేస్తుంది

**డిప్లాయ్ మెంట్ సమస్యలు వస్తున్నాయా?** [Infrastructure README](infra/README.md) చూడండి, ఇందులో సబ్డొమైన్ పేర్ల గందరగోళాలు, Azure Portal లో మాన్యువల్ డిప్లాయ్ మెంట్ దశలు, మరియు మోడల్ కాన్ఫిగరేషన్ సూచనలు ఉన్నాయి.

**డిప్లాయ్ మెంట్ విజయవంతం అయ్యిందా అని ధృవీకరించండి:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, మొదలైనవి చూపించాలి.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, మొదలైనవి చూపించాలి.
```

> **గమనిక:** `azd up` ఆదేశం ఆటోమేటిక్ గా `.env` ఫైల్ సృష్టిస్తుంది. మీరు తర్వాత దీన్ని అప్‌డేట్ చేయాల్సి వస్తే, `.env` ఫైల్ ని మాన్యువల్ గా ఎడిట్ చేయవచ్చు లేదా క్రింది కమాండ్ లు తో రీజనరేట్ చేయవచ్చు:
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


## ఆప్లికేషన్ ను లోకల్ గా రన్ చేయండి

**డిప్లాయ్ మెంట్ ధృవీకరించండి:**

Azure క్రెడెన్షియల్స్ ఉన్న `.env` ఫైల్ ప్రాజెక్ట్ రూట్ లో ఉందని నిర్ధారించండి. మోడ్యూల్ డైరెక్టరీ (`01-introduction/`) నుంచి ఇది నడపండి:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ను చూపించాలి
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT చూపించాలి
```

**ఆప్లికేషన్లు ప్రారంభించండి:**

**ఎంపిక 1: Spring Boot డాష్‌బోర్డ్ ఉపయోగించడం (VS Code వినియోగదారులకు సిఫార్సు)**

Dev container లో Spring Boot డాష్‌బోర్డ్ ఎక్స్‌టెన్షన్ ఉంది, ఇది అన్నీ Spring Boot ఆప్లికేషన్లను విజువల్ ఇంటర్ఫేస్ లో నిర్వహించడానికి సహాయం చేస్తుంది. ఇది VS Code లో ఎడమ వైపు Activity Bar లో(Spring Boot చిహ్నం చూడండి) కనపడుతుంది.

Spring Boot డాష్‌బోర్డ్ నుండి మీరు:
- వర్క్‌స్పేస్ లో అందుబాటులో ఉన్న అన్నీ Spring Boot ఆప్లికేషన్లను చూడొచ్చు
- ఒక్క క్లిక్ తో ఆప్లికేషన్లు స్టార్ట్/స్టాప్ చేయొచ్చు
- రియల్ టైంలో లాగ్స్ చూడొచ్చు
- ఆప్లికేషన్ స్థితిని పర్యవేక్షించొచ్చు

"introduction" పక్కన ఉన్న ప్లే బటన్ క్లిక్ చేసి ఈ మోడ్యూల్ మొదలుపెట్టండి, లేదా ఒక్కసారి అన్ని మోడ్యూల్స్ ప్రారంభించండి.

<img src="../../../translated_images/te/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code లో Spring Boot డాష్‌బోర్డు — ఒక చోట అన్ని మోడ్యూల్స్ ను ప్రారంభించండి, ఆపండి, మరియు పర్యవేక్షించండి*

**ఎంపిక 2: shell స్క్రిప్టులు ఉపయోగించడం**

అన్నీ వెబ్ ఆప్లికేషన్లు (మోడ్యూల్స్ 01-04) మొదలుపెట్టండి:

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

లేదా కేవలం ఈ మోడ్యూల్ మాత్రమే మొదలుపెట్టండి:

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

ఇవి రెండూ ఆటోమేటిక్ గా ప్రాజెక్ట్ రూట్ లో ఉన్న `.env` నుండి వాతావరణ వేరియబుల్స్ లోడ్ చేస్తాయి మరియు JAR లు లేకపోతే బిల్డ్ చేస్తాయి.

> **గమనిక:** ప్రారంభించే ముందు మీరు అన్ని మోడ్యూల్స్ మాన్యువల్ గా బిల్డ్ చేయాలనుకుంటే:
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

http://localhost:8080 ను మీ బ్రౌజర్ లో ఓపెన్ చేయండి.

**నిరుద్యోగం చేయడానికి:**

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


## ఆప్లికేషన్ ఉపయోగించడం

ఈ ఆప్లికేషన్ వెబ్ ఇంటర్ఫేస్ తో రెండు చాట్ అమలు లను పక్కపక్కన అందిస్తుంది.

<img src="../../../translated_images/te/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ఇద్దరూ Simple Chat (stateless) మరియు Conversational Chat (stateful) ఎంపికలను చూపిస్తున్న డాష్‌బోర్డు*

### Stateless చాట్ (ఎడమ ప్యానెల్)

ముందుగా దీన్ని ప్రయత్నించండి. "నా పేరు జాన్" అని అడిగి, వెంటనే "నా పేరు ఏమిటి?" అని అడగండి. మోడల్ గుర్తుంచుకోదు ఎందుకంటే ప్రతి సందేశం స్వతంత్రం. ఇది ప్రాథమిక భాషా మోడల్ ఇంటిగ్రేషన్ యొక్క కోర్ సమస్యను చూపిస్తుంది - సంభాషణ సందర్భం లేదు.

<img src="../../../translated_images/te/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI మీ పెరిగిన సందేశం నుండి పేరు గుర్తించదు*

### Stateful చాట్ (కుడి ప్యానెల్)

ఇప్పుడు అదే సీక్వెన్స్ ఇక్కడ ప్రయత్నించండి. "నా పేరు జాన్" తరువాత "నా పేరు ఏమిటి?" అడగండి. ఈసారి అది గుర్తుంచుకుంటుంది. తేడా MessageWindowChatMemory లో ఉంది - ఇది సంభాషణ చరిత్రను నిర్వహించి ప్రతీ అభ్యర్థనతో పెట్టిస్తుంది. ఇది ప్రొడక్షన్ సంభాషణాత్మక AI ఎలా పనిచేస్తుందో చూపిస్తుంది.

<img src="../../../translated_images/te/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI ముందుగా సంభాషణలో మీ పేరును గుర్తుంచుకుంటుంది*

రెండు ప్యానెల్స్ ఒకే GPT-5.2 మోడల్ ని ఉపయోగిస్తాయి. ఒక్క తేడా మెమరీ. ఇది మీ ఆప్లికేషన్ కు మెమరీ ఏం తెస్తుందో, నిజమైన ఉపయోగాలకు అది ఎందుకు అత్యవసరమో స్పష్టం చేస్తుంది.

## తదుపరి దశలు

**తదుపరి మోడ్యూల్:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**నావిగేషన్:** [← గతది: మోడ్యూల్ 00 - క్విక్ స్టార్ట్](../00-quick-start/README.md) | [మరల వెళ్ళండి](../README.md) | [తదుపరి: మోడ్యూల్ 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్పష్టం**:
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడి ఉంది. మేము ఖచ్చితత్వం కోసం ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటిక్ అనువాదాలలో పొరపాట్లు లేదా అప్రమేయతలు ఉండవచ్చు అని దయచేసి గమనించండి. స్వదేశీ భాషలో ఉన్న అసలు పత్రం అధికారిక మూలంగా పరిగణించాలి. ముఖ్యమైన సమాచారానికి, నిపుణుల మానవ అనువాదాన్ని సూచించబడుతుంది. ఈ అనువాదం వలన ఉద్భవించే ఏ హోమభ్రంశాలు లేదా తప్పు వ్యాఖ్యానాలకు మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->