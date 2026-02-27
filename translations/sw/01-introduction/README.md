# Moduli 01: Kuanzia na LangChain4j

## Jedwali la Yaliyomo

- [Video ya Maelezo](../../../01-introduction)
- [Nini Utajifunza](../../../01-introduction)
- [Mambo ya Kuwa Nayo Kabla](../../../01-introduction)
- [Kuelewa Tatizo Kuu](../../../01-introduction)
- [Kuelewa V Tokens](../../../01-introduction)
- [Jinsi Kumbukumbu Inavyofanya Kazi](../../../01-introduction)
- [Jinsi Hii Inavyotumia LangChain4j](../../../01-introduction)
- [Kuweka Miundombinu ya Azure OpenAI](../../../01-introduction)
- [Kuendesha Programu Kwenye Kigeni](../../../01-introduction)
- [Kutumia Programu](../../../01-introduction)
  - [Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)](../../../01-introduction)
  - [Mazungumzo Yenye Hali (Paneli ya Kulia)](../../../01-introduction)
- [Hatua Zifuatazo](../../../01-introduction)

## Video ya Maelezo

Tazama kikao hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii: [Kuanzia na LangChain4j - Kikao cha Moja kwa Moja](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Nini Utajifunza

Kama umekamilisha kuanza kwa haraka, uliweza kuona jinsi ya kutuma maoni na kupata majibu. Hiyo ni msingi, lakini programu halisi zinahitaji zaidi. Moduli hii inakufundisha jinsi ya kujenga AI ya mazungumzo inayokumbuka muktadha na kudumisha hali - tofauti kati ya maonyesho ya mara moja na programu inayotegemewa kwa uzalishaji.

Tutatumia GPT-5.2 ya Azure OpenAI katika mwongozo huu wote kwa sababu uwezo wake wa hoja za hali ya juu huonyesha tabia za mifumo tofauti kwa uwazi zaidi. Unapoongeza kumbukumbu, utaona jinsi tofauti hiyo ilivyo dhahiri. Hii inafanya iwe rahisi kuelewa kile kila kipengele kinaileta kwenye programu yako.

Utajenga programu moja inayoonyesha mifumo yote miwili:

**Mazungumzo Yasiyo na Hali** - Kila ombi ni huru. Mfano hauna kumbukumbu ya ujumbe wa awali. Huu ni mfumo uliotumia katika kuanza kwa haraka.

**Mazungumzo Yenye Hali** - Kila ombi linajumuisha historia ya mazungumzo. Mfano huhifadhi muktadha kwa mzunguko mingi. Hii ndio inahitajika kwa programu za uzalishaji.

## Mambo ya Kuwa Nayo Kabla

- Usajili wa Azure ulio na upatikanaji wa Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Kumbuka:** Java, Maven, Azure CLI na Azure Developer CLI (azd) vimeshatanguliwa kufunga katika devcontainer iliyotolewa.

> **Kumbuka:** Moduli hii inatumia GPT-5.2 kwenye Azure OpenAI. Usambazaji umewekwa moja kwa moja kupitia `azd up` - usibadili jina la mfano katika msimbo.

## Kuelewa Tatizo Kuu

Mifano ya lugha haina hali. Kila simu ya API ni huru. Ukipeleka "Jina langu ni John" kisha ukauliza "Jina langu ni nani?", mfano huna wazo ulijitambulisha tu. Hutendea kila ombi kana kwamba ni mazungumzo yako ya kwanza kabisa.

Hii ni sawa kwa maswali na majibu rahisi lakini haina maana kwa programu halisi. Bot za huduma kwa wateja zinahitaji kukumbuka ulichoambiwa. Msaidizi binafsi anahitaji muktadha. Mazungumzo yenye mzunguko mingi yanahitaji kumbukumbu.

<img src="../../../translated_images/sw/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Mazungumzo Yasiyo na Hali dhidi ya Yenye Hali" width="800"/>

*Tofauti kati ya mazungumzo yasiyo na hali (simu huru) na yenye hali (yanayojua muktadha)*

## Kuelewa Tokens

Kabla ya kuingia kwenye mazungumzo, ni muhimu kuelewa tokens - vitengo vya msingi vya maandishi vinavyoshughulikiwa na mifano ya lugha:

<img src="../../../translated_images/sw/token-explanation.c39760d8ec650181.webp" alt="Maelezo ya Token" width="800"/>

*Mfano wa jinsi maandishi yanavyovunjwa kuwa tokens - "I love AI!" hutendeka kuwa vitengo 4 tofauti vya usindikaji*

Tokens ndizo jinsi mifano ya AI hupima na kushughulikia maandishi. Maneno, alama za uandishi, hata nafasi zinaweza kuwa tokens. Mfano wako una kikomo cha tokens ngapi kinaweza kushughulikiwa kwa wakati mmoja (400,000 kwa GPT-5.2, kwa hadi tokens 272,000 za ingizo na 128,000 za matokeo). Kuelewa tokens kunakusaidia kusimamia urefu wa mazungumzo na gharama.

## Jinsi Kumbukumbu Inavyofanya Kazi

Kumbukumbu ya mazungumzo hutatua tatizo la ukosefu wa hali kwa kudumisha historia ya mazungumzo. Kabla ya kutuma ombi lako kwa mfano, mfumo huongezea ujumbe wa awali muhimu. Unapoomba "Jina langu ni nani?", mfumo kuwemo historia yote ya mazungumzo, kumruhusu mfano kuona ulisema awali "Jina langu ni John."

LangChain4j hutoa utekelezaji wa kumbukumbu zinazofanya kazi hii moja kwa moja. Unaamua ni ujumbe wangapi kuhifadhi na mfumo husimamia dirisha la muktadha.

<img src="../../../translated_images/sw/memory-window.bbe67f597eadabb3.webp" alt="Dhana ya Dirisha la Kumbukumbu" width="800"/>

*MessageWindowChatMemory huhifadhi dirisha linaloelea la ujumbe wa hivi karibuni, automatic kwa kuondoa zile za zamani*

## Jinsi Hii Inavyotumia LangChain4j

Moduli hii inapanua kuanza kwa haraka kwa kuunganisha Spring Boot na kuongeza kumbukumbu ya mazungumzo. Hivi ndivyo vipande vinavyoungana:

**Mategemeo** - Ongeza maktaba mbili za LangChain4j:

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

**Mfano wa Mazungumzo** - Sanidi Azure OpenAI kama bean ya Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder husoma taarifa za uthibitisho kutoka kwa mabadiliko ya mazingira yaliyowekwa na `azd up`. Kuanzisha `baseUrl` kwa kiungo chako cha Azure hufanya mteja wa OpenAI kufanya kazi na Azure OpenAI.

**Kumbukumbu ya Mazungumzo** - Fuata historia ya mazungumzo na MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tengeneza kumbukumbu ukiwa na `withMaxMessages(10)` kuhifadhi jumbe 10 za mwisho. Ongeza ujumbe wa mtumiaji na AI kwa makapoti yaliyo na type: `UserMessage.from(text)` na `AiMessage.from(text)`. Toa historia kwa `memory.messages()` na uitume kwa mfano. Huduma huhifadhi kumbukumbu tofauti kwa kila ID ya mazungumzo, ikiruhusu watumiaji wengi kuzungumza kwa wakati mmoja.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) na uliza:
> - "MessageWindowChatMemory huamua vipi ni ujumbe gani waondokane nao wakati dirisha limejaa?"
> - "Nawezaje kutekeleza uhifadhi wa kumbukumbu wa kawaida kutumia hifadhidata badala ya kumbukumbu ya ndani?"
> - "Ningeongezaje muhtasari wa kubana historia za zamani za mazungumzo?"

Mstari wa mazungumzo yasiyo na hali hupitia bila kumbukumbu - ni kama `chatModel.chat(prompt)` kama katika kuanza kwa haraka. Mstari wa mazungumzo yenye hali huongeza ujumbe kumbukumbini, hutoa historia, na kujumuisha muktadha huu kwenye kila ombi. Sanidi mfano sawa, mifumo tofauti.

## Kuweka Miundombinu ya Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Chagua usajili na eneo (eastus2 inapendekezwa)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Chagua usajili na eneo (eastus2 inapendekezwa)
```

> **Kumbuka:** Ikiwa unakutana na kosa la muda uliopita (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), endelea tu run `azd up` tena. Rasilimali za Azure zinaweza bado kusanidiwa nyuma, na kurudia huruhusu usambazaji kumalizika mara tu rasilimali zifikie hali ya mwisho.

Hii itafanya:
1. Kuweka rasilimali ya Azure OpenAI na modeli za GPT-5.2 na text-embedding-3-small
2. Kuanzisha kiotomatiki faili `.env` kwenye mzizi wa mradi na taarifa za uthibitisho
3. Kuweka mabadiliko yote ya mazingira yanayohitajika

**Kuwa na matatizo ya usambazaji?** Tazama [Infrastructure README](infra/README.md) kwa maelezo ya matatizo pamoja na migogoro ya majina ya subdomain, hatua za kusambaza Azure Portal kwa mkono, na mwongozo wa usanidi wa modeli.

**Thibitisha usambazaji umefanikiwa:**

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

> **Kumbuka:** Amri ya `azd up` hutengeneza moja kwa moja faili `.env`. Ikiwa unahitaji kuiboresha baadaye, unaweza kuhariri faili `.env` kwa mkono au kuibadilisha kwa kukimbia:
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

## Kuendesha Programu Kwenye Kigeni

**Thibitisha usambazaji:**

Hakikisha faili `.env` ipo kwenye saraka kuu na taarifa za Azure:

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anzisha programu:**

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Dev container inaongezea ugani wa Spring Boot Dashboard, inayotoa kidirisha cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Bar ya Shughuli upande wa kushoto wa VS Code (angalia alama ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye eneo la kazi
- Anzisha/acha programu kwa bonyeza papo hapo
- Tazama rekodi za programu kwa wakati halisi
- Fuata hali ya programu

Bonyeza kitufe cha kuanza kando ya "introduction" kuanzisha moduli hii, au anzisha moduli zote kwa pamoja.

<img src="../../../translated_images/sw/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka katika saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mzizi
.\start-all.ps1
```

Au anzisha moduli hii pekee:

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

Skripti zote zinapakia mabadiliko ya mazingira kutoka faili la mzizi `.env` na zitajenga JARs kama hazipo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanzisha:
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

Fungua http://localhost:8080 kwenye kivinjari chako.

**Kusimamisha:**

**Bash:**
```bash
./stop.sh  # Hii moduli tu
# Au
cd .. && ./stop-all.sh  # Moduli zote
```

**PowerShell:**
```powershell
.\stop.ps1  # Moduli hii tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Kutumia Programu

Programu hutoa kiolesura cha mtandao chenye utekelezaji wa mazungumzo mawili kando-kando.

<img src="../../../translated_images/sw/home-screen.121a03206ab910c0.webp" alt="Skrini ya Nyumbani ya Programu" width="800"/>

*Dashibodi inaonyesha Chatu Rahisi (isiyo na hali) na Chatu ya Mazungumzo (yenye hali)*

### Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)

Jaribu hii kwanza. Uliza "Jina langu ni John" kisha mara moja uliza "Jina langu ni nani?" Mfano hautakumbuka kwa sababu kila ujumbe ni huru. Hii inaonyesha tatizo kuu na ujumuishaji wa mfano wa lugha wa msingi - hakuna muktadha wa mazungumzo.

<img src="../../../translated_images/sw/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo ya Mazungumzo Yasiyo na Hali" width="800"/>

*AI haikumbuki jina lako kutoka ujumbe uliopita*

### Mazungumzo Yenye Hali (Paneli ya Kulia)

Sasa jaribu mfululizo huu hapa. Uliza "Jina langu ni John" kisha "Jina langu ni nani?" Muda huu inakumbuka. Tofauti ni MessageWindowChatMemory - huhifadhi historia ya mazungumzo na kuijumuisha na kila ombi. Hivi ndivyo AI za mazungumzo za uzalishaji zinavyofanya kazi.

<img src="../../../translated_images/sw/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo ya Mazungumzo Yenye Hali" width="800"/>

*AI inakumbuka jina lako kutoka awali katika mazungumzo*

Paneli zote mbili zinatumia mfano sawa wa GPT-5.2. Tofauti pekee ni kumbukumbu. Hii inafafanua wazi kile kumbukumbu inachokuleta kwenye programu yako na kwa nini ni muhimu kwa matumizi halisi.

## Hatua Zifuatazo

**Moduli Inayofuata:** [02-injiniya-la-prompti - Uhandisi wa Prompti na GPT-5.2](../02-prompt-engineering/README.md)

---

**Uelekezaji:** [← Iliyopita: Moduli 00 - Kuanzia Haraka](../00-quick-start/README.md) | [Rudi Kwenye Kuu](../README.md) | [Inayofuata: Moduli 02 - Uhandisi wa Prompti →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiarifu**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokamilika. Hati ya asili katika lugha yake ya mama inapaswa kuchukuliwa kama chanzo halali. Kwa taarifa muhimu, tafsiri ya kina ya mwanadamu mtaalamu inashauriwa. Hatubebwi dhamana kwa maelewano au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->