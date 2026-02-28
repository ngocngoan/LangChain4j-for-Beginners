# Moodul 01: LangChain4j-ga alguse tegemine

## Sisukord

- [Video juhend](../../../01-introduction)
- [Mida sa õpid](../../../01-introduction)
- [Eeldused](../../../01-introduction)
- [Põhiprobleemi mõistmine](../../../01-introduction)
- [Tokenite mõistmine](../../../01-introduction)
- [Kuidas mälu toimib](../../../01-introduction)
- [Kuidas see kasutab LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktuuri juurutamine](../../../01-introduction)
- [Rakenduse kohalik käivitamine](../../../01-introduction)
- [Rakenduse kasutamine](../../../01-introduction)
  - [Olemetu vestlus (vasak paan)](../../../01-introduction)
  - [Oluline vestlus (parem paan)](../../../01-introduction)
- [Järgmised sammud](../../../01-introduction)

## Video juhend

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Mida sa õpid

Kui sa lõpetasid kiire alguse, nägid, kuidas saata käske ja saada vastuseid. See on alus, kuid tõelised rakendused vajavad rohkem. See moodul õpetab sulle, kuidas luua vestluslikku tehisintellekti, mis mäletab konteksti ja hoiab olekut – see on vahe ühe korra demos ja tootmiskõlbuliku rakenduse vahel.

Selle juhendi jooksul kasutame Azure OpenAI GPT-5.2, sest selle täiustatud mõtlemisvõimed muudavad erinevate mustrite käitumise selgemaks. Kui lisad mälu, näed vahet selgelt. See muudab lihtsamaks mõista, mida iga komponent su rakendusele toob.

Sa ehitad ühe rakenduse, mis näitab mõlemat mustrit:

**Olemetu vestlus** – iga päring on sõltumatu. Mudelil ei ole mälu varasematest sõnumitest. See on muster, mida kasutasid kiiralguses.

**Oluline vestlus** – iga päring sisaldab vestlusajaloo konteksti. Mudel hoiab konteksti mitme vahetuse jooksul. See on see, mida tootmisrakendused vajavad.

## Eeldused

- Azure tellimus koos Azure OpenAI ligipääsuga
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Märkus:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) on eelinstalleeritud kaasasolevas arenduskonteineris.

> **Märkus:** See moodul kasutab GPT-5.2 Azure OpenAI-s. Juurutamine on automaatselt seadistatud `azd up` poolt – ära muuda mudeli nime koodis.

## Põhiprobleemi mõistmine

Keelemudelid on olemitud. Iga API päring on iseseisev. Kui sa saadad "Minu nimi on John" ja siis küsid "Mis mu nimi on?", ei tea mudel, et sa just end tutvustasid. Ta käsitleb iga päringut nagu see oleks esimest korda vestlust tegemas.

See sobib lihtsa küsimuste ja vastuste jaoks, kuid on tõelistel rakendustel mõttetu. Klienditeenindusbotid peavad meeles pidama, mida sa neile rääkisid. Isiklikud assistendid vajavad konteksti. Iga mitme vahetusega vestlus vajab mälu.

<img src="../../../translated_images/et/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Vahe olemitu (iseseisvad päringud) ja olulise (kontekstiteadlike) vestluste vahel*

## Tokenite mõistmine

Enne vestlustesse süvenemist on oluline mõista tokeneid – tekstielemendid, mida keelemudelid töötlevad:

<img src="../../../translated_images/et/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Näide, kuidas tekst jaotatakse tokeniteks – "I love AI!" muutub 4 eraldi töötlemisühikuks*

Tokenite abil mõõdavad ja töötlevad AI mudelid teksti. Sõnad, kirjavahemärgid ja isegi tühikud võivad olla tokenid. Sinu mudelil on maksimumarv tokeneid, mida ta korraga töödelda suudab (GPT-5.2 puhul 400 000, millest kuni 272 000 on sisend ja 128 000 väljund). Tokenite mõistmine aitab sul juhtida vestluse pikkust ja kulusid.

## Kuidas mälu toimib

Vestlusmälul lahendab olemitu probleemi, hoides vestlusajalugu. Enne, kui saata mudelile päring, lisab raamistik eelmised asjakohased sõnumid ette. Kui sa küsid "Mis mu nimi on?", saadab süsteem tegelikult kogu vestlusajaloo, nii näeb mudel, et sa ütlesid enne "Minu nimi on John."

LangChain4j pakub mälulahendusi, mis seda automaatselt haldavad. Sa valid, mitu sõnumit hoida ja raamistik juhib konteksti akent.

<img src="../../../translated_images/et/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory hoiab libisevat akent viimaste sõnumitega, automaatselt eemaldades vanu*

## Kuidas see kasutab LangChain4j

See moodul lisab kiirele algusele Spring Boot integratsiooni ja vestlusmäluga toe. Nii sobituvad osad kokku:

**Sõltuvused** – Lisa kaks LangChain4j teeki:

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

**Vestlusmudel** – seadista Azure OpenAI Spring bean-ina ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder loeb tõendused keskkonnamuutujatest, mis on seatud `azd up` abil. `baseUrl` seadmine Azure lõpp-punktile paneb OpenAI kliendi Azure OpenAI-ga töötama.

**Vestlusmälu** – jälgi vestlusajalugu MessageWindowChatMemory abil ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Loo mälu koos `withMaxMessages(10)`, et hoida viimaseid 10 sõnumit. Lisa kasutaja ja AI sõnumid tüübitud wrapperitega: `UserMessage.from(text)` ja `AiMessage.from(text)`. Võta ajalugu välja `memory.messages()` ja saada see mudelile. Teenus salvestab erinevad mälud iga vestluse ID kohta, võimaldades mitmel kasutajal samaaegselt vestelda.

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja küsi:
> - "Kuidas MessageWindowChatMemory otsustab, milliseid sõnumeid eemaldada, kui aken on täis?"
> - "Kas saan rakendada kohandatud mälusalvestuse, kasutades andmebaasi mälus hoidmise asemel?"
> - "Kuidas lisada vanade vestluste kokkuvõtet tihendamiseks?"

Olemitu vestlus-päring vahele jätab mälu – lihtsalt `chatModel.chat(prompt)` nagu kiiralguses. Oluline päring lisab sõnumid mällu, võtab ajalugu ja saadab selle igas päringus kaasa. Sama mudeli seadistus, erinevad mustrid.

## Azure OpenAI infrastruktuuri juurutamine

**Bash:**
```bash
cd 01-introduction
azd up  # Valige tellimus ja asukoht (soovitatav on eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Valige tellimus ja asukoht (soovitatav on eastus2)
```

> **Märkus:** Kui ilmneb ajalõpu viga (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), lihtsalt käivita `azd up` uuesti. Azure ressursid võivad alles taustal juurutamisel olla ja uuesti proovimine võimaldab juurutusel lõpetada, kui ressursid jõuavad lõppseisundisse.

See teeb järgmist:
1. Juurutab Azure OpenAI ressursi koos GPT-5.2 ja text-embedding-3-small mudelitega
2. Genereerib automaatselt `.env` faili projekti juurkausta koos tõendustega
3. Seadistab kõik nõutud keskkonnamuutujad

**Probleeme juurutamisega?** Vaata [Infrastruktuuri README](infra/README.md) üksikasjalikuks tõrkeotsinguks, sealhulgas alamdomeeni nime konfliktid, käsitsi Azure Portaali juurutamise sammud ja mudeli seadistusjuhised.

**Kontrolli, kas juurutamine õnnestus:**

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY jms.
```

> **Märkus:** `azd up` käsk genereerib `.env` faili automaatselt. Kui vajad hiljem värskendust, võid kas muuta `.env` faili käsitsi või genereerida selle uuesti:
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

## Rakenduse kohalik käivitamine

**Kontrolli juurutust:**

Veendu, et `.env` fail on juurkaustas Azure tõendustega:

**Bash:**
```bash
cat ../.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käivita rakendused:**

**Valik 1: Spring Boot Dashboardi kasutamine (Soovitatav VS Code’i kasutajatele)**

Arenduskonteiner sisaldab Spring Boot Dashboard laiendust, mis annab visuaalse liidese kõigi Spring Boot rakenduste haldamiseks. Leidub Activity Bar'is vasakul pool VS Code’is (otsitav Spring Boot ikoon).

Spring Boot Dashboardist saad:
- Vaadata kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/kustutada rakendusi ühe klikiga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsa "introduction" kõrval olevale mängi nupule, et käivitada see moodul, või käivita korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Shell skriptide kasutamine**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurkataloogist
.\start-all.ps1
```

Või käivita ainult see moodul:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkausta `.env` failist ja ehitavad JAR-failid, kui neid pole olemas.

> **Märkus:** Kui soovid eelistatult kõik moodulid enne käsitsi ehitada:
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

Ava oma brauseris http://localhost:8080.

**Peatamiseks:**

**Bash:**
```bash
./stop.sh  # Ainult see moodul
# Või
cd .. && ./stop-all.sh  # Kõik moodulid
```

**PowerShell:**
```powershell
.\stop.ps1  # Ainult see moodul
# Või
cd ..; .\stop-all.ps1  # Kõik moodulid
```

## Rakenduse kasutamine

Rakendus pakub veebiliidest kahe vestluse rakendusega kõrvuti.

<img src="../../../translated_images/et/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Armatuurlaud kuvab nii lihtsat vestlust (olemitu) kui ka vestluslikku vestlust (oluline)*

### Olemitu vestlus (vasak paan)

Proovi esmalt seda. Küsi "Minu nimi on John" ja kohe pärast seda "Mis mu nimi on?" Mudel ei mäleta, sest iga sõnum on iseseisev. See demonstreerib põhiprobleemi lihtsa keelemudeli integreerimisel – puudub vestluse kontekst.

<img src="../../../translated_images/et/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ei mäleta su nime eelmisest sõnumist*

### Oluline vestlus (parem paan)

Proovi nüüd sama järjekorda siin. Küsi "Minu nimi on John" ja siis "Mis mu nimi on?" Seekord ta mäletab. Vahe on MessageWindowChatMemory – see hoiab vestluse ajalugu ja lisab selle igasse päringusse. Nii töötab tootmisvestluslik AI.

<img src="../../../translated_images/et/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI mäletab su nime varasemast vestlusest*

Mõlemas paanis kasutatakse sama GPT-5.2 mudelit. Ainult mälu vahe. See teeb selgeks, mida mälu su rakendusele annab ja miks see on tõeliste kasutusjuhtude jaoks hädavajalik.

## Järgmised sammud

**Järgmine moodul:** [02-prompt-engineering - Prompt Engineering with GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 00 - Kiire algus](../00-quick-start/README.md) | [Tagasi pearaamatukokku](../README.md) | [Järgmine: Moodul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades AI tõlke teenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüame täpsust, palun arvestage, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles peaks olema autoriteetne allikas. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta käesoleva tõlke kasutamisest tingitud arusaamatuste ega valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->