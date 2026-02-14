# Moodul 01: LangChain4j-ga alustamine

## Sisukord

- [Mida Sa Õpid](../../../01-introduction)
- [Eeltingimused](../../../01-introduction)
- [Tuuma Probleemi Mõistmine](../../../01-introduction)
- [Tokenite Mõistmine](../../../01-introduction)
- [Kuidas Mälu Töötab](../../../01-introduction)
- [Kuidas See Kasutab LangChain4j-d](../../../01-introduction)
- [Azure OpenAI Infrastruktuuri Paigaldamine](../../../01-introduction)
- [Rakenduse Käivitamine Kohalikult](../../../01-introduction)
- [Rakenduse Kasutamine](../../../01-introduction)
  - [Olekuta Vestlus (Vasak Paneel)](../../../01-introduction)
  - [Olekuline Vestlus (Parem Paneel)](../../../01-introduction)
- [Järgmised Sammud](../../../01-introduction)

## Mida Sa Õpid

Kui sa tegid kiire stardiga läbi, nägid, kuidas saata prompt'e ja saada vastuseid. See on alus, aga tõelised rakendused vajavad rohkem. See moodul õpetab sulle, kuidas ehitada vestluslikku tehisintellekti, mis meenutab konteksti ja hoiab olekut – vahe ühekorralise demo ja tootmiseks valmis rakenduse vahel.

Selles juhendis kasutame Azure OpenAI GPT-5.2, sest selle arenenud järeldamisvõime muudab erinevate mustrite käitumise selgemaks. Kui lisad mälu, näed selgelt erinevust. See teeb lihtsamaks mõista, mida iga komponent sinu rakendusele juurde annab.

Sa ehitad ühe rakenduse, mis demonstreerib mõlemat mustrit:

**Olekuta Vestlus** – Iga päring on sõltumatu. Mudelil pole varasemate sõnumite mälu. See on mustrit, mida kasutasid kiire stardi juures.

**Olekuline Vestlus** – Iga päring sisaldab vestluse ajalugu. Mudel hoiab konteksti mitme käigu vältel. Seda nõuavad tootmisrakendused.

## Eeltingimused

- Azure tellimus koos Azure OpenAI ligipääsuga
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Märkus:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) on eelinstallitud antud devcontaineris.

> **Märkus:** See moodul kasutab GPT-5.2 Azure OpenAI peal. Paigaldus on automaatselt konfigureeritud `azd up` kaudu – ära muuda mudeli nime koodis.

## Tuuma Probleemi Mõistmine

Keelemudelid on olekuta. Iga API kõne on sõltumatu. Kui sa saadad "Minu nimi on John" ja siis küsid "Mis mu nimi on?", mudelil pole aimu, et sa just ennast tutvustasid. Ta käsitleb iga päringut nagu see oleks sinu esimene vestlus üldse.

See sobib lihtsate K&V puhul, aga on kasutu tõeliste rakenduste jaoks. Klienditeenindusbotid peavad mäletama, mida sa neile ütlesid. Isiklikud assistendid vajavad konteksti. Igas mitmekäigulises vestluses on mälu vajalik.

<img src="../../../translated_images/et/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Olekuta vs Olekuline Vestlus" width="800"/>

*Vahe olekuta (sõltumatud kõned) ja olekulise (kontekstiteadliku) vestluse vahel*

## Tokenite Mõistmine

Enne vestlustesse sukeldumist on oluline mõista tokenid – põhilised tekstitükid, mida keelemudelid töötlevad:

<img src="../../../translated_images/et/token-explanation.c39760d8ec650181.webp" alt="Tokeni Selgitus" width="800"/>

*Näide, kuidas tekst jaguneb tokeniteks – "Ma armastan AI-d!" muutub neljaks eraldiseisvaks töötlusüksuseks*

Tokeneid kasutavad tehisintellektimudelid teksti mõõtmiseks ja töötlemiseks. Sõnad, kirjavahemärgid ja isegi tühikud võivad olla tokenid. Sinu mudelil on piirang, kui palju tokeneid korraga töödelda saab (GPT-5.2 puhul 400 000, sisendtokeneid kuni 272 000 ja väljundtokeneid kuni 128 000). Tokenite mõistmine aitab sul vestluse pikkust ja kulusid paremini hallata.

## Kuidas Mälu Töötab

Vestlusmälu lahendab olekuta probleemi, hoides vestluse ajaloo alles. Enne päringu mudelile saatmist lisab süsteem asjakohased varasemad sõnumid ette. Kui sa küsid "Mis mu nimi on?", saadetakse tegelikult kogu vestluse ajalugu, võimaldades mudelil näha, et sa ütlesid varem "Minu nimi on John."

LangChain4j pakub mälu teostusi, mis seda automaatselt haldavad. Sina valid, kui palju sõnumeid hoida ja raamistik haldab kontekstiakna.

<img src="../../../translated_images/et/memory-window.bbe67f597eadabb3.webp" alt="Mälu Aknakonseptsioon" width="800"/>

*MessageWindowChatMemory haldab liugurit viimasest sõnumite hulgast, automaatselt kustutades vanemaid*

## Kuidas See Kasutab LangChain4j-d

See moodul täiendab kiiret starti, integreerides Spring Booti ja lisades vestlusmäluga. Siin on, kuidas osad kokku sobivad:

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

**Vestlusmudel** – Konfigureeri Azure OpenAI Spring bean'ina ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder loeb mandaadid keskkonnamuutujatest, mida seab `azd up`. `baseUrl` seadmine sinu Azure lõpp-punktile paneb OpenAI kliendi tööle Azure OpenAI-ga.

**Vestlusmälu** – Jälgi vestluse ajalugu MessageWindowChatMemory abil ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Loo mälu `withMaxMessages(10)` abil, et hoida viimaseid 10 sõnumit. Lisa kasutaja ja AI sõnumeid tüübipõhiste wrapperitega: `UserMessage.from(text)` ja `AiMessage.from(text)`. Kogu ajalugu saad `memory.messages()` kaudu ning saada mudelile. Teenus hoiab mälusid eraldi vestluse ID-de järgi, võimaldades mitmel kasutajal samaaegselt vestelda.

> **🤖 Proovi GitHub Copilot Chat-iga:** Ava [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja küsi:
> - "Kuidas MessageWindowChatMemory otsustab, milliseid sõnumeid akna täitumisel eemaldada?"
> - "Kas saan rakendada kohandatud mälutalitlust, kasutades andmebaasi mälutalduse asemel?"
> - "Kuidas lisada vanade vestluste kokkusurumiseks kokkuvõtlik funktsioon?"

Olekuta vestluse lõpp-punkt jätab mälu vahele – lihtsalt `chatModel.chat(prompt)` nagu kiires stardis. Olekuline lõpp-punkt lisab sõnumid mällu, hangib ajaloo ja lisab selle konteksti iga päringu juurde. Sama mudeli konfiguratsioon, erinevad mustrid.

## Azure OpenAI Infrastruktuuri Paigaldamine

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

> **Märkus:** Kui saad ajapiirangu veateate (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), lihtsalt käivita `azd up` uuesti. Azure ressursid võivad endiselt olema paigaldamisel taustal, ja proovimine lubab paigalduse lõpule jõuda, kui ressursid jõuavad lõplikku olekusse.

See teeb:
1. Paigaldab Azure OpenAI ressursi GPT-5.2 ja text-embedding-3-small mudelitega
2. Genereerib automaatselt `.env` faili projekti juurkausta mandaatidena
3. Seadistab kõik vajalikud keskkonnamuutujad

**Probleemide korral paigaldusega?** Vaata [Infrastruktuuri README-d](infra/README.md) detailse veaotsingu, alamdomeenide nimeri konfliktide, käsitsi Azure Portali paigalduse juhiste ja mudeli konfigureerimise nõuannete jaoks.

**Kontrolli paigalduse õnnestumist:**

**Bash:**
```bash
cat ../.env  # Tuleb näidata AZURE_OPENAI_ENDPOINT, API_KEY jms.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

> **Märkus:** `azd up` käsk genereerib automaatselt `.env` faili. Kui vajad hiljem uuendamist, võid kas muuta `.env` faili käsitsi või uuesti genereerida käivitades:
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

## Rakenduse Käivitamine Kohalikult

**Kontrolli paigaldust:**

Veendu, et `.env` fail asub juurkataloogis Azure mandaatidena:

**Bash:**
```bash
cat ../.env  # Peaks näitama AZURE_OPENAI_ENDPOINTI, API_VÕTME, JUURUTUST
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käivita rakendused:**

**Variant 1: Spring Boot Dashboardi kasutamine (soovitatav VS Code kasutajatele)**

Dev container sisaldab Spring Boot Dashboard laiendust, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leidub Activity Bar'il VS Code vasakul küljel (Spring Boot ikoon).

Spring Boot Dashboardist saad:
- Näha kõiki tööruumis olevaid Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Vaadata rakenduste logisid reaalajas
- Jälgida rakenduse olekut

Lihtsalt klõpsa mängunupule "introduction" kõrval selle mooduli käivitamiseks või käivita korraga kõik moodulid.

<img src="../../../translated_images/et/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Variant 2: Kasutades shell skripte**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juure kataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurekataloogist
.\start-all.ps1
```

Või ainult see moodul:

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

Mõlemad skriptid laadivad automaatselt keskkonnamuutujad juurest `.env` failist ja ehitavad JAR failid, kui neid veel pole.

> **Märkus:** Kui soovid enne käivitamist kõik moodulid käsitsi ehitada:
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

Ava brauseris http://localhost:8080.

**Lõpetamiseks:**

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

## Rakenduse Kasutamine

Rakendusel on veebiliides kahe vestlusimplementatsiooniga kõrvuti.

<img src="../../../translated_images/et/home-screen.121a03206ab910c0.webp" alt="Rakenduse Avaleht" width="800"/>

*Dashboard, mis kuvab nii Lihtsat Vestlust (olekuta) kui Vestluslikku Vestlust (olekulist)*

### Olekuta Vestlus (Vasak Paneel)

Proovi seda esmalt. Küsi "Minu nimi on John" ja kohe seejärel "Mis mu nimi on?" Mudel ei mäleta, sest iga sõnum on sõltumatu. See demonstreerib põhiprobleemi lihtsa keelemudeli integratsiooniga – puudub vestluse kontekst.

<img src="../../../translated_images/et/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Olekuta Vestluse Demo" width="800"/>

*AI ei mäleta su nime eelmisest sõnumist*

### Olekuline Vestlus (Parem Paneel)

Nüüd proovi sama jada siin. Küsi "Minu nimi on John" ja siis "Mis mu nimi on?" Seekord mäletab. Vahe on MessageWindowChatMemory's – see hoiab vestluse ajaloo ja lisab selle iga päringu juurde. Nii töötab tootmislik vestluslik tehisintellekt.

<img src="../../../translated_images/et/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Olekuline Vestluse Demo" width="800"/>

*AI mäletab su nime varasemast vestlusest*

Mõlemad paneelid kasutavad sama GPT-5.2 mudelit. Ainuke erinevus on mälu olemasolu. See teeb selgeks, mida mälu rakendusele annab ja miks see on reaalseks kasutuseks oluline.

## Järgmised Sammud

**Järgmine Moodul:** [02-prompt-engineering - Promptide Inseneeria GPT-5.2-ga](../02-prompt-engineering/README.md)

---

**Navigatsioon:** [← Eelmine: Moodul 00 - Kiire Start](../00-quick-start/README.md) | [Tagasi Algusesse](../README.md) | [Järgmine: Moodul 02 - Promptide Inseneeria →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud AI tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame tagada täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Algne dokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul on soovitatav kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tingitud arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->