# Moduli 01: Kuanzisha na LangChain4j

## Jedwali la Maudhui

- [Utajifunza Nini](../../../01-introduction)
- [Mahitaji ya Awali](../../../01-introduction)
- [Kuelewa Tatizo Kuu](../../../01-introduction)
- [Kuelewa Tokeni](../../../01-introduction)
- [Jinsi Kumbukumbu Inavyofanya Kazi](../../../01-introduction)
- [Jinsi Hii Inavyotumia LangChain4j](../../../01-introduction)
- [Kuweka Miundombinu ya Azure OpenAI](../../../01-introduction)
- [Kendesha Programu Kwenye Kompyuta Lokali](../../../01-introduction)
- [Kutumia Programu](../../../01-introduction)
  - [Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)](../../../01-introduction)
  - [Mazungumzo Yenye Hali (Paneli ya Kulia)](../../../01-introduction)
- [Hatua Zinazofuata](../../../01-introduction)

## Utajifunza Nini

Ukiwa umemaliza mwanzo wa haraka, ulibaini jinsi ya kutuma maelekezo na kupata majibu. Hilo ndilo msingi, lakini programu halisi zinahitaji zaidi. Moduli hii inakufundisha jinsi ya kujenga AI ya mazungumzo inayokumbuka muktadha na kudumisha hali - tofauti kati ya maonyesho ya mara moja na programu tayari kwa uzalishaji.

Tutatumia GPT-5.2 ya Azure OpenAI katika mwongozo huu wote kwa sababu uwezo wake wa hali ya juu wa kufikiri hufanya tabia za mifumo tofauti ziwe wazi zaidi. Unapoongeza kumbukumbu, utaona tofauti hiyo waziwazi. Hii inafanya iwe rahisi kuelewa kile kila kipengele kinacholeta kwenye programu yako.

Utajenga programu moja inayonyesha mifumo yote miwili:

**Mazungumzo Yasiyo na Hali** - Kila ombi ni huru. Mfano hauna kumbukumbu ya ujumbe wa awali. Hii ni mfumo uliotumia katika mwanzo wa haraka.

**Mazungumzo Yenye Hali** - Kila ombi linajumuisha historia ya mazungumzo. Mfano hudumisha muktadha katika mizunguko mingi. Hii ndio programu zinazohitajika kwa uzalishaji.

## Mahitaji ya Awali

- Usajili wa Azure wenye upatikanaji wa Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Kumbuka:** Java, Maven, Azure CLI, na Azure Developer CLI (azd) vimesakinishwa awali katika devcontainer iliyotolewa.

> **Kumbuka:** Moduli hii inatumia GPT-5.2 kwenye Azure OpenAI. Uwekaji umewekwa moja kwa moja kupitia `azd up` - usibadilishe jina la mfano katika msimbo.

## Kuelewa Tatizo Kuu

Modeli za lugha haziwezi kuhifadhi hali. Kila wito wa API ni huru. Ukituma "Jina langu ni John" kisha ukauliza "Ninaitwaje?", mfano haufahamu kwa sasa kwamba umejijulisha mwenyewe. Hutumia kila ombi kama mazungumzo ya kwanza kabisa uliyokuwa nayo.

Hii ni sawa kwa maswali na majibu rahisi lakini haifai kwa programu halisi. Bots wa huduma kwa wateja wanahitaji kukumbuka ulicho sema. Msaidizi wa kibinafsi anahitaji muktadha. Mazungumzo yoyote yenye mizunguko mingi yanahitaji kumbukumbu.

<img src="../../../translated_images/sw/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Tofauti kati ya mazungumzo yasiyo na hali (miito huru) na yenye hali (yanaojua muktadha)*

## Kuelewa Tokeni

Kabla ya kuingia kwenye mazungumzo, ni muhimu kuelewa tokeni - vitengo vya msingi vya maandishi ambavyo modeli za lugha hutumia:

<img src="../../../translated_images/sw/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Mfano wa jinsi maandishi yanavyo gawanywa kuwa tokeni - "Ninapenda AI!" inakuwa vitengo 4 vya kuendesha*

Tokeni ndizo AI hutumia kupima na kuchakata maandishi. Maneno, alama za uandishi, na hata nafasi zinaweza kuwa tokeni. Mfano wako una kikomo cha tokeni kiasi gani kinaweza kuchakatwa mara moja (400,000 kwa GPT-5.2, ikiwa ni hadi tokeni 272,000 za kuingiza na 128,000 za kutoa). Kuelewa tokeni kunakusaidia kudhibiti urefu wa mazungumzo na gharama.

## Jinsi Kumbukumbu Inavyofanya Kazi

Kumbukumbu ya mazungumzo inatatua tatizo la kutohifadhi hali kwa kudumisha historia ya mazungumzo. Kabla ya kutuma ombi lako kwa mfano, mfumo huongeza ujumbe wa zamani wenye umuhimu. Unapouliza "Ninaitwaje?", mfumo husambaza historia yote ya mazungumzo, ikiruhusu mfano kuona kwamba umekuwa ukisema "Jina langu ni John."

LangChain4j hutoa utekelezaji wa kumbukumbu zinazoshughulikia hili moja kwa moja. Unachagua ni ujumbe wangapi kuhifadhi na mfumo hudhibiti dirisha la muktadha.

<img src="../../../translated_images/sw/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory hudumisha dirisha linaloelea la ujumbe za hivi karibuni, likiacha zile za zamani moja kwa moja*

## Jinsi Hii Inavyotumia LangChain4j

Moduli hii inaongeza mwanzo wa haraka kwa kuunganisha Spring Boot na kuongeza kumbukumbu ya mazungumzo. Hivi ndivyo sehemu zinavyoendana:

**Vitegemezi** - Ongeza maktaba mbili za LangChain4j:

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

**Mfano wa Mazungumzo** - Sanidi Azure OpenAI kama "bean" ya Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Mojengaji husoma sifa kutoka kwa mabadiliko ya mazingira yaliyowekwa na `azd up`. Kuweka `baseUrl` kwa kiunganishi chako cha Azure hufanya mteja wa OpenAI kufanya kazi na Azure OpenAI.

**Kumbukumbu ya Mazungumzo** - Fuatilia historia ya mazungumzo kwa MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Tengeneza kumbukumbu kwa `withMaxMessages(10)` ili kuhifadhi ujumbe 10 za mwisho. Ongeza ujumbe wa mtumiaji na AI kwa vifungashio vyeusi: `UserMessage.from(text)` na `AiMessage.from(text)`. Pata historia na `memory.messages()` na uitume kwa mfano. Huduma inahifadhi kumbukumbu tofauti kwa kila kitambulisho cha mazungumzo, ikiruhusu watumiaji wengi kuzungumza kwa wakati mmoja.

> **🤖 Jaribu na [GitHub Copilot](https://github.com/features/copilot) Chat:** Fungua [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) na uliza:
> - "Je, MessageWindowChatMemory huamua vipi ni ujumbe gani waacha wakati dirisha limejaa?"
> - "Ninaweza kutekeleza hifadhi ya kumbukumbu maalum kwa kutumia hifadhidata badala ya kumbukumbu RNG?"
> - "Nitahakikisha vipi kuongezea muhtasari wa kufinyanga historia ya zamani ya mazungumzo?"

Kipengele cha mazungumzo yasiyo na hali hapitishi kumbukumbu kabisa - ni `chatModel.chat(prompt)` kama mwanzo wa haraka. Endpoint ya mazungumzo yenye hali huongeza ujumbe kwenye kumbukumbu, hupata historia, na hujumuisha muktadha huo na kila ombi. Sanidi mfano ule ule, mifumo tofauti.

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

> **Kumbuka:** Ikiwa unakutana na kosa la muda kuisha (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), endelea tu kwa kuendesha `azd up` tena. Rasilimali za Azure zinaweza bado zikiwa zinasawazishwa nyuma, na jaribio la tena huruhusu uwekaji kukamilika mara zinapofikia hali ya mwisho.

Hii itafanya:
1. Kuweka rasilimali ya Azure OpenAI yenye modeli za GPT-5.2 na text-embedding-3-small
2. Kuunda faili `.env` moja kwa moja kwenye mzizi wa mradi na sifa zozote
3. Kuweka mabadiliko yote ya mazingira yanayohitajika

**Kuna shida na ujenzi?** Angalia [Infrastructure README](infra/README.md) kwa kueleza matatizo ya jina la chini, hatua za kuweka mkono kwenye Azure Portal, na mwongozo wa usanidi wa modeli.

**Thibitisha uwekaji umefanikiwa:**

**Bash:**
```bash
cat ../.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Inapaswa kuonyesha AZURE_OPENAI_ENDPOINT, API_KEY, n.k.
```

> **Kumbuka:** Amri `azd up` huunda faili `.env` moja kwa moja. Ikiwa unahitaji kuibadilisha baadaye, unaweza kuhariri faili `.env` au kuunda upya kwa kuendesha:
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

## Kendesha Programu Kwenye Kompyuta Lokali

**Thibitisha uwekaji:**

Hakikisha faili `.env` ipo kwenye saraka ya mzizi na sifa za Azure:

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

Kontena ya maendeleo inaongeza ugani wa Spring Boot Dashboard, unaotoa kiolesura cha kuona kusimamia programu zote za Spring Boot. Unaweza kuipata kwenye Bar ya Shughuli upande wa kushoto wa VS Code (tazama alama ya Spring Boot).

Kutoka Spring Boot Dashboard, unaweza:
- Kuona programu zote za Spring Boot zilizopo kwenye eneo la kazi
- Kuzindua/kuzima programu kwa bonyeza moja tu
- Kutazama kumbukumbu za programu kwa wakati halisi
- Kufuatilia hali ya programu

Bonyeza tu kitufe cha kuendesha kando ya "introduction" kuanza moduli hii, au anzisha moduli zote mara moja.

<img src="../../../translated_images/sw/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Chaguo 2: Kutumia skripti za shell**

Anzisha programu zote za wavuti (moduli 01-04):

**Bash:**
```bash
cd ..  # Kutoka kwa saraka ya mzizi
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kutoka kwenye saraka kuu
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

Skripti zote hujipakiza moja kwa moja mabadiliko ya mazingira kutoka faili la `.env` la mzizi na zitaunda JAR ikiwa hazipo.

> **Kumbuka:** Kama unapendelea kujenga moduli zote kwa mkono kabla ya kuanza:
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

**Kuwazuia:**

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

Programu inatoa kiolesura cha wavuti chenye utekelezaji wa mazungumzo mawili pembeni-pembeni.

<img src="../../../translated_images/sw/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashibodi inaonyesha chaguo la Mazungumzo Rahisi (yasiyo na hali) na Mazungumzo ya Kiasili (yenye hali)*

### Mazungumzo Yasiyo na Hali (Paneli ya Kushoto)

Jaribu kwanza hapa. Uliza "Jina langu ni John" kisha mara moja uliza "Ninaitwaje?" Mfano hatakumbuki kwa sababu kila ujumbe ni huru. Hii inaonyesha tatizo kuu la ushirikiano wa modeli za lugha za msingi - hakuna muktadha wa mazungumzo.

<img src="../../../translated_images/sw/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI haikumbuki jina lako kutoka ujumbe uliopita*

### Mazungumzo Yenye Hali (Paneli ya Kulia)

Sasa jaribu mfululizo huo hapa. Uliza "Jina langu ni John" kisha "Ninaitwaje?" Mara hii inakumbuka. Tofauti ni MessageWindowChatMemory - hudumisha historia ya mazungumzo na huijumuisha katika kila ombi. Hivi ndivyo AI halisi ya mazungumzo inavyofanya kazi.

<img src="../../../translated_images/sw/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI anakumbuka jina lako kutoka hapo awali katika mazungumzo*

Paneli zote mbili zinatumia mfano ule ule wa GPT-5.2. Tofauti pekee ni kumbukumbu. Hii inaonyesha wazi kinachowezeshwa na kumbukumbu katika programu yako na kwa nini ni muhimu kwa matumizi halisi.

## Hatua Zinazofuata

**Moduli Inayofuata:** [02-prompt-engineering - Uhandisi wa Maagizo na GPT-5.2](../02-prompt-engineering/README.md)

---

**Endelea:** [← Iliyopita: Moduli 00 - Mwanzo wa Haraka](../00-quick-start/README.md) | [Rudi Kwenye Kuu](../README.md) | [Ifuatayo: Moduli 02 - Uhandisi wa Maagizo →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kataa**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Hati asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatuwajibiki kwa uelewa au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->