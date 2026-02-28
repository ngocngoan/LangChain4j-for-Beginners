# Moduli 01: Kuanzisha na LangChain4j

## Jedwali la Maudhui

- [Maelezo ya Video](../../../01-introduction)
- [Utajifunza Nini](../../../01-introduction)
- [Masharti ya Awali](../../../01-introduction)
- [Kuelewa Tatizo Kuu](../../../01-introduction)
- [Kuelewa Tokeni](../../../01-introduction)
- [Jinsi Kumbukumbu Inavyofanya Kazi](../../../01-introduction)
- [Jinsi Hii Inavyotumia LangChain4j](../../../01-introduction)
- [Weka Miundombinu ya Azure OpenAI](../../../01-introduction)
- [Endesha Programu Kwenye Kompyuta Binafsi](../../../01-introduction)
- [Kutumia Programu](../../../01-introduction)
  - [Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)](../../../01-introduction)
  - [Mazungumzo Yanayohifadhi Hali (Paneli ya Kulia)](../../../01-introduction)
- [Hatua Zijazo](../../../01-introduction)

## Maelezo ya Video

Tazama kipindi hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Utajifunza Nini

Ukiwa umemaliza mwanzilishi wa haraka, uligundua jinsi ya kutuma maelekezo na kupata majibu. Hiyo ndiyo msingi, lakini programu halisi zinahitaji zaidi. Moduli hii inakufundisha jinsi ya kujenga AI ya mazungumzo inayokumbuka muktadha na kudumisha hali - tofauti kati ya maonyesho ya mara moja na programu zinazotumika kwa uzalishaji.

Tutatumia GPT-5.2 ya Azure OpenAI katika mwongozo huu wote kwa sababu uwezo wake wa kuzingatia hoja kwa undani hufanya tabia ya mifumo tofauti ionekane zaidi. Unapoongeza kumbukumbu, utaona tofauti wazi. Hii inasaidia kuelewa vizuri kile kila kipengele kinachotoa kwa programu yako.

Utatengeneza programu moja inayonyesha mifumo yote miwili:

**Mazungumzo Yasiyo na Hali** - Kila ombi ni la pekee. Mfano hauna kumbukumbu ya ujumbe uliotumwa awali. Hii ndiyo mfano ulio tumia katika mwanzilishi wa haraka.

**Mazungumzo Yanayohifadhi Hali** - Kila ombi linajumuisha historia ya mazungumzo. Mfano huhifadhi muktadha katika mizunguko mingi. Hii ndiyo programu zinazotumika kwa uzalishaji zinavyohitaji.

## Masharti ya Awali

- Usajili wa Azure wenye ufikiaji wa Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Kumbuka:** Java, Maven, Azure CLI na Azure Developer CLI (azd) vimeshatumwa hapo awali kwenye devcontainer iliyotolewa.

> **Kumbuka:** Moduli hii inatumia GPT-5.2 kwenye Azure OpenAI. Uwekaji unafanywa moja kwa moja kupitia `azd up` - usibadili jina la modeli katika msimbo.

## Kuelewa Tatizo Kuu

Mifano ya lugha haina hali. Kila ombi la API ni huru. Ikiwa utatuma "Jina langu ni John" kisha ukauliza "Jina langu ni nani?", mfano hauna wazo kwamba umejitambulisha. Hutibu kila ombi kana kwamba ni mazungumzo ya kwanza kabisa uliyowahi kuwa nayo.

Hii ni sawa kwa maswali na majibu rahisi lakini haifai kwa programu halisi. Roboti za huduma kwa wateja zinahitaji kukumbuka uliyowaambia. Msaidizi binafsi anahitaji muktadha. Mazungumzo yoyote yenye mizunguko mingi yanahitaji kumbukumbu.

<img src="../../../translated_images/sw/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Tofauti kati ya mazungumzo yasiyo na hali (miito huru) na mazungumzo yanayohifadhi hali (yanayojua muktadha)*

## Kuelewa Tokeni

Kabla ya kuingia katika mazungumzo, ni muhimu kuelewa tokeni - vitengo vya msingi vya maandishi ambavyo mifano ya lugha huchakata:

<img src="../../../translated_images/sw/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Mfano wa jinsi maandishi yanavyovunjwa kuwa tokeni - "Ninapenda AI!" inakuwa vitengo vinne tofauti vya kuchakata*

Tokeni ndizo njia AI hutumia kupima na kuchakata maandishi. Maneno, alama za uandikaji, na hata nafasi zinaweza kuwa tokeni. Mfano wako una kikomo cha tokeni ngapi kinaweza kuchakata kwa mara moja (400,000 kwa GPT-5.2, ikiwa ni pamoja na tokeni 272,000 kwa ingizo na 128,000 kwa matokeo). Kuelewa tokeni husaidia kudhibiti urefu wa mazungumzo na gharama.

## Jinsi Kumbukumbu Inavyofanya Kazi

Kumbukumbu ya mazungumzo inatatua tatizo la kutokuwepo kwa hali kwa kuhifadhi historia ya mazungumzo. Kabla ya kutuma ombi lako kwa mfano, mfumo huongeza ujumbe muhimu wa awali. Unapouliza "Jina langu ni nani?", mfumo hasa hutuma historia yote ya mazungumzo, na hivyo mfano unaona ulifanya maelezo "Jina langu ni John."

LangChain4j hutoa utekelezaji wa kumbukumbu unaofanya hili kiotomatiki. Unachagua jumla ya ujumbe unaotaka kuhifadhi na mfumo huendesha dirisha la muktadha.

<img src="../../../translated_images/sw/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory huhifadhi dirisha linalosogea la ujumbe wa hivi karibuni, kwa moja kwa moja hutoa zile za zamani*

## Jinsi Hii Inavyotumia LangChain4j

Moduli hii inaongeza mwanzilishi wa haraka kwa kuingiza Spring Boot na kuongeza kumbukumbu ya mazungumzo. Hapa jinsi vipande vinavyoambatana:

**Mtegemezi** - Ongeza maktaba mbili za LangChain4j:

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

Mjenzi husoma sifa kutoka kwa vigezo vya mazingira vilivyowekwa na `azd up`. Kuweka `baseUrl` kwa kikokotelenche USBo unachochagua cha Azure kunafanya mteja wa OpenAI kufanya kazi na Azure OpenAI.

**Kumbukumbu ya Mazungumzo** - Fuata historia ya mazungumzo kwa MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tengeneza kumbukumbu kwa `withMaxMessages(10)` kuhifadhi ujumbe 10 wa mwisho. Ongeza ujumbe wa mtumiaji na wa AI kwa njia za aina maalum: `UserMessage.from(text)` na `AiMessage.from(text)`. Pata historia na `memory.messages()` na uitume kwa mfano. Huduma huhifadhi kumbukumbu tofauti kwa kila kitambulisho cha mazungumzo, ikiruhusu watumiaji wengi kuzungumza kwa wakati mmoja.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) na uliza:
> - "Je, MessageWindowChatMemory huamua vipi ni ujumbe gani unaotolewa wakati dirisha limejaa?"
> - "Je, naweza kutengeneza uhifadhi wa kumbukumbu wa aina yangu kwa kutumia database badala ya kumbukumbu ya ndani?"
> - "Ningongezaje muhtasari wa historia ya mazungumzo ili kubana maelezo ya zamani?"

Mwisho wa mazungumzo yasiyo na hali unabypass kumbukumbu kabisa - tu `chatModel.chat(prompt)` kama ilivyokuwa mwanzilishi wa haraka. Mwisho wa mazungumzo yanayohifadhi hali huongeza ujumbe kwenye kumbukumbu, hupata historia, na hujumuisha muktadha huu kwa kila ombi. Sanidi mfano ule ule, mifumo tofauti.

## Weka Miundombinu ya Azure OpenAI

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

> **Kumbuka:** Ikiwa unakumbana na kosa la muda kuisha (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jitihada nyingine yako ni kuendesha tena `azd up`. Rasilimali za Azure zinaweza bado kuandaliwa kwa nyuma, na kujaribu tena huruhusu uwekaji kukamilika mara rasilimali zifikie hali ya mwisho.

Hii itafanya:
1. Kuweka rasilimali ya Azure OpenAI na modeli za GPT-5.2 na text-embedding-3-small
2. Kuunda moja kwa moja faili `.env` katika mzizi wa mradi yenye sifa za usajili
3. Kuseti vigezo vyote muhimu vya mazingira

**Kuna matatizo ya kuweka?** Angalia [Infrastructure README](infra/README.md) kwa maelezo ya matatizo, ikijumuisha migongano ya majina ya subdomain, hatua za kuweka kwenye Azure Portal kwa mkono, na mwongozo wa usanidi wa modeli.

**Thibitisha kuwa uwekaji umefanikiwa:**

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

> **Kumbuka:** Amri ya `azd up` hutengeneza moja kwa moja faili `.env`. Ikiwa unahitaji kuibadilisha baadaye, unaweza kuhariri faili `.env` kwa mkono au kuibadilisha tena kwa kuendesha:
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

## Endesha Programu Kwenye Kompyuta Binafsi

**Thibitisha uwekaji:**

Hakikisha faili `.env` ipo katika saraka kuu na sifa za Azure:

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Anza programu:**

**Chaguo 1: Kutumia Spring Boot Dashboard (Inapendekezwa kwa watumiaji wa VS Code)**

Dev container ina kiendelezaji Spring Boot Dashboard, kinachotoa kiolesura cha kuona na kudhibiti programu zote za Spring Boot. Unaweza kukipata kwenye Activity Bar upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye eneo la kazi
- Anzisha au simamisha programu kwa kitufe kimoja
- Tazama kumbukumbu za programu kwa wakati halisi
- Fuatilia hali ya programu

Bonyeza kitufe cha kucheza kando ya "introduction" kuanza moduli hii, au anzisha moduli zote kwa pamoja.

<img src="../../../translated_images/sw/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia shell scripts**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwa saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka ya mizizi
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

Mafaili haya ya script yanaleta vigezo vya mazingira kutoka kwa faili `.env` ya mzizi na yatajenga JARs ikiwa hayapo.

> **Kumbuka:** Ikiwa unapendelea kujenga moduli zote kwa mkono kabla ya kuanza:
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

**Kusitisha:**

**Bash:**
```bash
./stop.sh  # Moduli hii tu
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

Programu hii hutoa kiolesura cha wavuti chenye utekelezaji wa mazungumzo mawili upande kwa upande.

<img src="../../../translated_images/sw/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashibodi inaonyesha chaguzi zote za Mazungumzo Rahisi (yasiyo na hali) na Mazungumzo ya Kuzungumza (yanayohifadhi hali)*

### Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)

Jaribu hii kwanza. Uliza "Jina langu ni John" halafu mara moja uliza "Jina langu ni nani?" Mfano hautakumbuka kwa sababu kila ujumbe ni huru. Hii inaonyesha tatizo kuu la ushirikishaji wa mfano wa lugha wa kawaida - hakuna muktadha wa mazungumzo.

<img src="../../../translated_images/sw/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI haikumbuki jina lako kutoka ujumbe uliotuma awali*

### Mazungumzo Yanayohifadhi Hali (Paneli ya Kulia)

Sasa jaribu mfululizo huo hapa. Uliza "Jina langu ni John" halafu "Jina langu ni nani?" Muda huu anakumbuka. Tofauti ni MessageWindowChatMemory - huhifadhi historia ya mazungumzo na hujumuisha na kila ombi. Hii ndiyo jinsi AI ya mazungumzo kwa uzalishaji inavyofanya kazi.

<img src="../../../translated_images/sw/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI anakumbuka jina lako kutoka mapema katika mazungumzo*

Paneli zote mbili zinatumia mfano ule ule wa GPT-5.2. Tofauti pekee ni kumbukumbu. Hii inaonyesha wazi kile kumbukumbu inachotoa kwa programu yako na kwa nini ni muhimu kwa matumizi halisi.

## Hatua Zijazo

**Moduli Ifuatayo:** [02-prompt-engineering - Uhandisi wa Maagizo na GPT-5.2](../02-prompt-engineering/README.md)

---

**Uelekeo:** [← Awali: Moduli 00 - Mwanzilishi wa Haraka](../00-quick-start/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 02 - Uhandisi wa Maagizo →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyesha Majadiliano**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri zilizotengenezwa kiotomatiki zinaweza kuwa na makosa au kasoro. Hati asilia katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa habari muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inashauriwa. Hatuna dhamana kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->