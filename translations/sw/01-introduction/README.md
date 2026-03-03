# Moduli 01: Kuanzia na LangChain4j

## Jedwali la Yaliyomo

- [Video ya Maelezo](../../../01-introduction)
- [Utajifunza Nini](../../../01-introduction)
- [Mambo ya Kuandaa](../../../01-introduction)
- [Kuelewa Tatizo Kuu](../../../01-introduction)
- [Kuelewa Vitoa-Kumbukumbu](../../../01-introduction)
- [Jinsi Kumbukumbu Inavyofanya Kazi](../../../01-introduction)
- [Jinsi Hii Inavyotumia LangChain4j](../../../01-introduction)
- [Tangaza Miundombinu ya Azure OpenAI](../../../01-introduction)
- [Endesha Programu Hapa Mkononi](../../../01-introduction)
- [Kutumia Programu](../../../01-introduction)
  - [Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)](../../../01-introduction)
  - [Mazungumzo Yenye Hali (Paneli ya Kulia)](../../../01-introduction)
- [Hatua Zifuatazo](../../../01-introduction)

## Video ya Maelezo

Angalia kikao hiki cha moja kwa moja kinachoelezea jinsi ya kuanza na moduli hii:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Kuanzia na LangChain4j - Kikao cha Moja kwa Moja" width="800"/></a>

## Utajifunza Nini

Katika kuanza haraka, ulitumia Modeli za GitHub kutuma maelekezo, kutumia zana, kujenga bomba la RAG, na kupima mipaka ya usalama. Mionyesho hiyo ilionyesha nini kinawezekana — sasa tunabadilisha kuwa Azure OpenAI na GPT-5.2 na kuanza kujenga programu za mtindo wa uzalishaji. Moduli hii inazingatia AI ya mazungumzo inayokumbuka muktadha na kudumisha hali — dhana ambazo maonyesho ya kuanza haraka yalitumia nyuma ya pazia lakini hazikueleza.

Tutatumia GPT-5.2 ya Azure OpenAI katika mwongozo huu wote kwa sababu uwezo wake wa uelewa wa hali ya juu huifanya tabia za mifumo tofauti ionekane wazi zaidi. Utakapoongeza kumbukumbu, utaona tofauti kwa uwazi. Hii inafanya iwe rahisi kuelewa kile kila kipengele kinachochangia kwa programu yako.

Utajenga programu moja inayothibitisha mifumo yote miwili:

**Mazungumzo Yasiyo na Hali** - Kila ombi ni huru. Mfano huna kumbukumbu ya ujumbe wa awali. Huu ndio mfumo uliotumia katika kuanza haraka.

**Mazungumzo Yenye Hali** - Kila ombi linajumuisha historia ya mazungumzo. Mfano hudumisha muktadha katika mizunguko mingi. Hii ndio inahitajika kwa programu za uzalishaji.

## Mambo ya Kuandaa

- Usajili wa Azure wenye ufikiaji wa Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Kumbuka:** Java, Maven, Azure CLI na Azure Developer CLI (azd) vimeshatanguliwa katika devcontainer iliyotolewa.

> **Kumbuka:** Moduli hii inatumia GPT-5.2 kwenye Azure OpenAI. Uenezi umewekwa moja kwa moja kwa kutumia `azd up` - usibadilishe jina la mfano katika msimbo.

## Kuelewa Tatizo Kuu

Modeli za lugha hazina hali. Kila simu ya API ni huru. Ukiwatumia "Jina langu ni John" kisha ukauliza "Jina langu ni nani?", mfano hauna wazo kwamba umejitambulisha. Hutendea kila ombi kana kwamba ndio mazungumzo yako ya kwanza kabisa.

Hii ni sawa kwa maswali rahisi na majibu lakini haifai kwa programu halisi. Bot za huduma kwa wateja zinahitaji kukumbuka ulichosema. Msaidizi wa kibinafsi anahitaji muktadha. Mazungumzo yoyote yenye mizunguko mingi yanahitaji kumbukumbu.

Dijagramu ifuatayo inaonyesha tofauti za mbinu hizo mbili — upande wa kushoto, simu huru inayosahau jina lako; upande wa kulia, simu yenye hali iliyo supported na ChatMemory inayokumbuka.

<img src="../../../translated_images/sw/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Mazungumzo Yasiyo na Hali dhidi ya Yenye Hali" width="800"/>

*Tofauti kati ya mazungumzo yasiyo na hali (simu huru) na yenye hali (yanayojua muktadha)*

## Kuelewa Vitoa-Kumbukumbu

Kabla ya kuingia kwenye mazungumzo, ni muhimu kuelewa vitoa-kumbukumbu - vitengo vya msingi vya maandishi vinavyosindikwa na modeli za lugha:

<img src="../../../translated_images/sw/token-explanation.c39760d8ec650181.webp" alt="Maelezo ya Kitoa-Kumbukumbu" width="800"/>

*Mfano wa jinsi maandishi yanavyogawanywa kuwa vitoa-kumbukumbu - "Napenda AI!" kuwa vitengo 4 tofauti vya usindikaji*

Vitoa-kumbukumbu ndio njia modeli za AI hupima na kusindika maandishi. Maneno, alama za uandishi, hata nafasi zinaweza kuwa vitoa-kumbukumbu. Mfano wako una kikomo cha vitoa-kumbukumbu ambacho kinaweza kusindika kwa wakati mmoja (400,000 kwa GPT-5.2, kwa vitoa-kumbukumbu hadi 272,000 kwa ingizo na 128,000 kwa matokeo). Kuelewa vitoa-kumbukumbu kunakusaidia kusimamia urefu wa mazungumzo na gharama.

## Jinsi Kumbukumbu Inavyofanya Kazi

Kumbukumbu ya mazungumzo inatatua tatizo la kutokuwepo hali kwa kudumisha historia ya mazungumzo. Kabla ya kutuma ombi lako kwa mfano, mfumo huongeza ujumbe wa awali muhimu. Unapouliza "Jina langu ni nani?", mfumo hukutumia historia yote ya mazungumzo, ikiruhusu mfano kuona kwamba ulisema "Jina langu ni John" hapo awali.

LangChain4j hutoa utekelezaji wa kumbukumbu unaofanya hili moja kwa moja. Unaamua ni jumbe ngapi kuzihifadhi na mfumo hushughulikia dirisha la muktadha. Dijagramu hapa chini inaonyesha jinsi MessageWindowChatMemory inavyodumisha dirisha linalopita la jumbe za hivi karibuni.

<img src="../../../translated_images/sw/memory-window.bbe67f597eadabb3.webp" alt="Dhana ya Dirisha la Kumbukumbu" width="800"/>

*MessageWindowChatMemory inadumisha dirisha linalopita la jumbe za hivi karibuni, ikiachia zile za zamani kwa otomati*

## Jinsi Hii Inavyotumia LangChain4j

Moduli hii inaendeleza kuanza haraka kwa kuunganisha Spring Boot na kuongeza kumbukumbu ya mazungumzo. Hapa ni jinsi vipengele vinaungana:

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

Mjenzi husoma sifa za kuingia kutoka kwa mazingira yaliyowekwa na `azd up`. Kuweka `baseUrl` kwa mwisho wa Azure kunafanya mteja wa OpenAI afanye kazi na Azure OpenAI.

**Kumbukumbu ya Mazungumzo** - Fuatilia historia ya mazungumzo kwa MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Unda kumbukumbu na `withMaxMessages(10)` kuhifadhi jumbe 10 za mwisho. Ongeza jumbe za mtumiaji na AI kwa vifuniko vilivyoandikwa: `UserMessage.from(text)` na `AiMessage.from(text)`. Pata historia na `memory.messages()` na uitume kwa mfano. Huduma huhifadhi kumbukumbu tofauti kwa kila mazungumzo kwa ID yake, kuruhusu watumiaji wengi kuongea kwa wakati mmoja.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) na uliza:
> - "Jinsi gani MessageWindowChatMemory huchagua jumbe gani kuondoa wakati dirisha limejaa?"
> - "Je, naweza kutekeleza uhifadhi wa kumbukumbu wa kawaida kwa kutumia hifadhi ya data badala ya kumbukumbu ndani ya programu?"
> - "Ningepaswa kuongeza muhtasari ili kupunguza historia ya zamani ya mazungumzo?"

Kipengele cha mazungumzo yasiyo na hali hakiungi kumbukumbu kabisa - ni `chatModel.chat(prompt)` kama kuanza haraka. Kipengele cha mazungumzo yenye hali kinaongeza jumbe kwenye kumbukumbu, kinapata historia, na kinajumuisha muktadha na kila ombi. Sanidi mfano ule ule, mifumo tofauti.

## Tangaza Miundombinu ya Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Chagua usajili na eneo (eastus2 inashauriwa)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Chagua usajili na eneo (eastus2 inapendekezwa)
```

> **Kumbuka:** Ikiwa unakutana na kosa la muda kukamilika (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), tumia tena `azd up`. Rasilimali za Azure zinaweza bado kuandaliwa nyuma, na jaribio la pili huruhusu uenezi kukamilika mara rasilimali zifikie hali ya mwisho.

Hii itafanya:
1. Kutangaza rasilimali ya Azure OpenAI pamoja na modeli za GPT-5.2 na text-embedding-3-small
2. Kutengeneza faili `.env` moja kwa moja kwenye mizizi ya mradi na sifa
3. Kuweka mazingira yote yanayohitajika

**Kuna shida za uenezi?** Angalia [README ya Miundombinu](infra/README.md) kwa maelezo ya kutatua matatizo ikiwa ni pamoja na migongano ya majina ya viwanja, hatua za uenezi wa mikono kwenye Azure Portal, na mwongozo wa usanidi wa modeli.

**Thibitisha uenezi ulifanikiwa:**

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

> **Kumbuka:** Amri `azd up` hutengeneza faili `.env` moja kwa moja. Ikiwa unahitaji kuiboresha baadaye, unaweza kuhariri faili `.env` kwa mikono au kuitengeneza upya kwa kuendesha:
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

## Endesha Programu Hapa Mkononi

**Thibitisha uenezi:**

Hakikisha faili `.env` ipo kwenye saraka ya mizizi na sifa za Azure. Endesha hii kutoka kwenye saraka ya moduli (`01-introduction/`):

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

Dev container ina jopo la Spring Boot Dashboard, linalotoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Bar ya Shughuli upande wa kushoto wa VS Code (tafuta ikoni ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwa nafasi ya kazi
- Kuanza/kukomesha programu kwa bonyeza moja
- Kutazama kumbukumbu za programu moja kwa moja
- Kufuatilia hali ya programu

Bonyeza tu kitufe cha kuanzisha karibu na "introduction" kuanza moduli hii, au anzisha moduli zote kwa wakati mmoja.

<img src="../../../translated_images/sw/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Jopo la Spring Boot katika VS Code — anzisha, simamia, na fuatilia moduli zote kutoka mahali pamoja*

**Chaguo 2: Kutumia skiripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwenye saraka ya mizizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwa saraka kuu
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

Skiripti zote huchukua moja kwa moja mazingira kutoka kwenye faili `.env` ya mizizi na hutatua JARs kama hazipo.

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
.\stop.ps1  # Hii moduli tu
# Au
cd ..; .\stop-all.ps1  # Moduli zote
```

## Kutumia Programu

Programu inatoa kiolesura cha wavuti pamoja na utekelezaji wa mazungumzo mawili pembeni kwa pembeni.

<img src="../../../translated_images/sw/home-screen.121a03206ab910c0.webp" alt="Skrini ya Nyumbani ya Programu" width="800"/>

*Jopo linavyoonyesha Chaguo la Mazungumzo Rahisi (yasiyo na hali) na Mazungumzo ya Hali (yenye hali)*

### Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)

Jaribu hii kwanza. Uliza "Jina langu ni John" kisha mara moja uliza "Jina langu ni nani?" Mfano hautakumbuka kwa sababu kila ujumbe ni huru. Hii inaonyesha tatizo kuu la kuunganishwa kwa modeli ya lugha ya msingi - hakuna muktadha wa mazungumzo.

<img src="../../../translated_images/sw/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Mionyesho ya Mazungumzo Yasiyo na Hali" width="800"/>

*AI haikumbuki jina lako kutoka ujumbe wa awali*

### Mazungumzo Yenye Hali (Paneli ya Kulia)

Sasa jaribu mfuatano uleule hapa. Uliza "Jina langu ni John" kisha "Jina langu ni nani?" Muda huu inakumbuka. Tofauti ni MessageWindowChatMemory - inadumisha historia ya mazungumzo na inaanzisha hilo muktadha na kila ombi. Hivi ndivyo AI za mazungumzo za uzalishaji hufanya kazi.

<img src="../../../translated_images/sw/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Mionyesho ya Mazungumzo Yenye Hali" width="800"/>

*AI inakumbuka jina lako kutoka awali katika mazungumzo*

Paneli zote mbili zinatumia mfano ule ule wa GPT-5.2. Tofauti pekee ni kumbukumbu. Hii inaonyesha wazi kile kumbukumbu inachochangia kwa programu yako na kwanini ni muhimu kwa matumizi halisi.

## Hatua Zifuatazo

**Moduli Ifuatayo:** [02-prompt-engineering - Uhandisi wa Maelekezo na GPT-5.2](../02-prompt-engineering/README.md)

---

**Uelekezaji:** [← Zamani: Moduli 00 - Kuanza Haraka](../00-quick-start/README.md) | [Rudi Mma](../README.md) | [Ifuatayo: Moduli 02 - Uhandisi wa Maelekezo →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tangazo la Kukataa**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kuwa tafsiri za moja kwa moja zinaweza kuwa na makosa au ukosefu wa usahihi. Nyaraka ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa habari muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inashauriwa. Hatuna lawama kwa kutokuelewana au tafsiri kubaya kutokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->