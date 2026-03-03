# Moodul 01: LangChain4j-ga alustamine

## Sisukord

- [Video juhendamine](../../../01-introduction)
- [Mida sa õpid](../../../01-introduction)
- [Eeltingimused](../../../01-introduction)
- [Põhiprobleemi mõistmine](../../../01-introduction)
- [Tokonite mõistmine](../../../01-introduction)
- [Kuidas mälu töötab](../../../01-introduction)
- [Kuidas see kasutab LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI infrastruktuur](../../../01-introduction)
- [Rakenduse käivitamine lokaalselt](../../../01-introduction)
- [Rakenduse kasutamine](../../../01-introduction)
  - [Seisunditu vestlus (vasak paneel)](../../../01-introduction)
  - [Seisundiga vestlus (parem paneel)](../../../01-introduction)
- [Järgmised sammud](../../../01-introduction)

## Video juhendamine

Vaata seda otseülekande salvestust, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Mida sa õpid

Kiirlahenduses kasutasid GitHubi mudeleid, et saata päringuid, kutsuda tööriistu, ehitada RAG-torustikku ja testida turvameetmeid. Need demo'd näitasid, mis on võimalik — nüüd liigume üle Azure OpenAI ja GPT-5.2 peale ning hakkame ehitama tootmistasemel rakendusi. See moodul keskendub dialoogipõhisele tehisintellektile, mis mäletab konteksti ja hoiab seisundit — need on mõisted, mida kiirlahenduse demo'd taustal kasutasid, kuid ei selgitanud.

Kogu juhendis kasutame Azure OpenAI GPT-5.2 mudelit, kuna selle täiustatud mõtlemisvõime teeb erinevate mustrite käitumise paremini nähtavaks. Mälu lisamisel näed selgelt erinevust. See muudab lihtsamaks mõista, mida iga komponent sinu rakendusele lisab.

Sa ehitad ühe rakenduse, mis demonstreerib mõlemat mustrit:

**Seisunditu vestlus** - Iga päring on iseseisev. Mudelil puudub mälu eelnevate sõnumite kohta. See on see mustrilahendus, mida kasutasid kiirlahenduses.

**Seisundiga vestlus** - Iga päring sisaldab vestluse ajalugu. Mudel hoiab konteksti mitme vahetuse ulatuses. Selle nõuavad tootmisrakendused.

## Eeltingimused

- Azure tellimus koos Azure OpenAI ligipääsuga
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Märkus:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) on eelinstalleeritud pakutud devcontaineris.

> **Märkus:** See moodul kasutab GPT-5.2 Azure OpenAI-st. Deploy konfiguratsioon toimub automaatselt `azd up` kaudu — ära muuda mudeli nime koodis.

## Põhiprobleemi mõistmine

Keelemudelid on seisunditud. Iga API kõne on iseseisev. Kui sa saadad "Minu nimi on John" ja siis küsid "Mis on minu nimi?", mudelil pole aimugi, et sa just end tutvustasid. Ta käsitleb iga päringut nagu esimese vestlusena, mis sul kunagi olnud on.

See sobib lihtsatele küsimustele ja vastustele, kuid on päris rakendustes kasutuskõlbmatu. Klienditeenindusbotid peavad mäletama, mida neile öeldi. Isiklikud assistendid vajavad konteksti. Igas mitme vahetusega vestluses on mälu vajalik.

Järgnev diagramm võrdleb kahte lähenemist — vasakul seisunditu kõne, kes unustab su nime; paremal seisundiga kõne, mida toetab ChatMemory ja mis mäletab seda.

<img src="../../../translated_images/et/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Vahe seisunditu (iseseisvad kõned) ja seisundiga (kontekstiteadlikud) vestluste vahel*

## Tokonite mõistmine

Enne vestlustesse süvenemist on oluline mõista tokeneid – põhilisi tekstiplokke, mida keelemudelid töötlevad:

<img src="../../../translated_images/et/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Näide, kuidas tekst lagundatakse tokeniteks – "I love AI!" jaguneb neljaks eraldi töötlemise ühikuks*

Tokendid on see, kuidas tehisintellekt mudelid mõõdavad ja töötlevad teksti. Sõnad, kirjavahemärgid ja isegi tühikud võivad olla token'iteks. Sinu mudelil on piir, kui palju tokeneid ta korraga suudab töödelda (GPT-5.2 puhul 400 000, seesama jaotatuna kuni 272 000 sisendtokeniks ja 128 000 väljundtokeniks). Tokonite mõistmine aitab sul hallata vestluse pikkust ja kulusid.

## Kuidas mälu töötab

Vestluse mälu lahendab seisunditu probleemi, säilitades vestluse ajaloo. Enne, kui päring mudelile saadetakse, lisab raamistik ette asjakohased varasemad sõnumid. Kui sa küsid "Mis on mu nimi?", saadab süsteem tegelikult kogu vestluse ajaloo, mis võimaldab mudelil näha, et sa eespool ütlesid "Minu nimi on John".

LangChain4j pakub mälu teostusi, mis teevad seda automaatselt. Sa valid, kui palju sõnumeid säilitada, ja raamistik haldab konteksti akent. Allolev diagramm näitab, kuidas MessageWindowChatMemory haldab liugakent viimaste sõnumite jaoks.

<img src="../../../translated_images/et/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory hoiab liugakent viimaste sõnumite jaoks, automaatselt vanu välja visates*

## Kuidas see kasutab LangChain4j

See moodul laiendab kiirlahendust, integreerides Spring Boot'i ja lisades vestluse mälu. Siin on kuidas komponendid kokku sobivad:

**Sõltuvused** - Lisa kaks LangChain4j teeki:

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

**Vestlusmudel** - Konfigureeri Azure OpenAI Spring bean'ina ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder loeb tõestust andmed keskkonnamuutujatest, mida seab `azd up`. `baseUrl` seadmine sinu Azure lõpp-punktile muudab OpenAI kliendi töövõimeliseks Azure OpenAI-ga.

**Vestluse mälu** - Jälgi vestluse ajalugu MessageWindowChatMemory abil ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Loo mälu `withMaxMessages(10)` abil, et hoida viimased 10 sõnumit. Lisa kasutaja ja AI sõnumid tüübitud mähistega: `UserMessage.from(text)` ja `AiMessage.from(text)`. Ajalugu taasta `memory.messages()` abil ja saada mudelile. Teenus salvestab eraldi mälu eksemplarid iga vestluse ID jaoks, võimaldades mitmel kasutajal korraga vestelda.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat'iga:** Ava [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja küsi:
> - "Kuidas MessageWindowChatMemory otsustab, millised sõnumid akna täitumisel välja visata?"
> - "Kas ma saan rakendada kohandatud mälu salvestamist, kasutades andmebaasi mälus hoidmise asemel?"
> - "Kuidas ma lisaksin vanade vestluste ajaloo kokkuvõtmise?"

Seisunditu vestluse lõpp-punkt jätab mälu täielikult vahele — lihtne `chatModel.chat(prompt)` nagu kiirlahenduses. Seisundiga lõpp-punkt lisab sõnumid mällu, taasesitab ajaloo ja kaasab selle konteksti iga päringuga. Sama mudeli seadistus, erinevad mustrid.

## Deploy Azure OpenAI infrastruktuur

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

> **Märkus:** Kui kohtad ajapiirangu tõrget (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), käivita lihtsalt uuesti `azd up`. Azure ressursid võivad taustal hetkel seadistamisel olla ja korduskatse võimaldab deploy lõpule viia, kui ressursid jõuavad lõplikku olekusse.

See sooritab:
1. Deploy Azure OpenAI ressurss GPT-5.2 ja text-embedding-3-small mudelitega
2. Automaatse `.env` faili genereerimise projekti juurkausta koos tõestust andmetega
3. Kõik vajalike keskkonnamuutujate seadistamise

**Kui esineb deploy probleeme?** Vaata [Infrastructure README](infra/README.md), kus on põhjalik tõrkeotsing, sealhulgas alamdomeeni nime konfliktid, käsitsi Azure Portali deploy sammud ja mudeli seadistamise juhised.

**Veendu, et deploy õnnestus:**

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY jms.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY jms.
```

> **Märkus:** `azd up` käsu käivitamine genereerib automaatselt `.env` faili. Kui vajad hiljem uuendamist, võid kas käsitsi `.env` faili muuta või uuesti genereerida, käivitades:
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

## Rakenduse lokaal käivitamine

**Veendu deploy õnnestumises:**

Veendu, et `.env` fail asub juurkaustas koos Azure tõestust andmetega. Käivita see mooduli kaustast (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Tuleks näidata AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Alusta rakendusi:**

**Variant 1: Kasutades Spring Boot Dashboardi (Soovitatav VS Code kasutajatele)**

Dev container sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest, et hallata kõiki Spring Boot rakendusi. Leiad selle VS Code vasakpoolsest Activity Bar-ist (otsi Spring Boot ikooni).

Spring Boot Dashboardilt saad:
- Näha kõiki saadaval olevaid Spring Boot rakendusi töökohas
- Alustada/peatada rakendusi ühe klikiga
- Vaadata rakenduse logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt kliki "introduction" kõrval mängimise nupule, et käivitada see moodul või käivita kõik korraga.

<img src="../../../translated_images/et/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code's — käivita, peata ja jälgi kõiki mooduleid ühest kohast*

**Variant 2: Kasutades shell skripte**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurest kataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juura kataloogist
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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurkaustas `.env` failist ja ehitavad JAR failid, kui neid veel pole.

> **Märkus:** Kui tahad ehitada kõik moodulid käsitsi enne käivitamist:
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

Rakendus pakub veebipõhist liidest kahe vestluslahendusega kõrvuti.

<img src="../../../translated_images/et/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Armatuurlaud, mis kuvab nii lihtsa vestluse (seisunditu) kui ka dialoogipõhise vestluse (seisundiga) valikud*

### Seisunditu vestlus (vasak paneel)

Proovi esmalt seda. Küsi "Minu nimi on John" ja siis kohe "Mis on mu nimi?" Mudel ei mäleta, sest iga sõnum on iseseisev. See demonstreerib põhiprobleemi keelemudelite tavaintegratsioonis – puudub vestluse kontekst.

<img src="../../../translated_images/et/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*Tehisintellekt ei mäleta su nime eelmisest sõnumist*

### Seisundiga vestlus (parem paneel)

Nüüd proovi sama jada siin. Küsi "Minu nimi on John" ja seejärel "Mis on mu nimi?" Seekord mäletab. Erinevus on MessageWindowChatMemory - see hoiab vestluse ajaloo ja lisab selle iga päringuga kaasa. Nii töötab tootmisvestluse tehisintellekt.

<img src="../../../translated_images/et/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*Tehisintellekt mäletab su nime varasemast vestlusest*

Mõlemad paneelid kasutavad sama GPT-5.2 mudelit. Erinevus on ainult mälus. See teeb selgeks, mida mälu rakendusele annab ja miks see on tegelikes kasutusjuhtudes oluline.

## Järgmised sammud

**Järgmine moodul:** [02-prompt-engineering - Prompt Engineering GPT-5.2-ga](../02-prompt-engineering/README.md)

---

**Navigatsioon:** [← Eelmine: Moodul 00 – Kiirlahendus](../00-quick-start/README.md) | [Tagasi avalehele](../README.md) | [Järgmine: Moodul 02 – Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud tehisintellekti tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame tagada täpsust, tuleks arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle algkeeles tuleks pidada autoriteetseks allikaks. Tähtsa teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei võta vastutust mis tahes arusaamatuste või vale tõlgenduste eest, mis tulenevad selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->