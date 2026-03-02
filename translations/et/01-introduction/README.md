# Moodul 01: LangChain4j-ga alustamine

## Sisukord

- [Video ülevaade](../../../01-introduction)
- [Mida õpid](../../../01-introduction)
- [Eeldused](../../../01-introduction)
- [Põhiprobleemi mõistmine](../../../01-introduction)
- [Tokenite mõistmine](../../../01-introduction)
- [Kuidas mälu töötab](../../../01-introduction)
- [Kuidas see kasutab LangChain4j-d](../../../01-introduction)
- [Azure OpenAI infrastruktuuri juurutamine](../../../01-introduction)
- [Rakenduse kohalik käivitamine](../../../01-introduction)
- [Rakenduse kasutamine](../../../01-introduction)
  - [Olemitu vestlus (vasak paneel)](../../../01-introduction)
  - [Olemusega vestlus (parem paneel)](../../../01-introduction)
- [Järgmised sammud](../../../01-introduction)

## Video ülevaade

Vaata seda otseülekannet, mis selgitab, kuidas selle mooduliga alustada:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j-ga alustamine - otseülekanne" width="800"/></a>

## Mida õpid

Kui sa läbisid kiire alguse, nägid, kuidas saata ettepanekuid ja saada vastuseid. See on alus, kuid tõelised rakendused vajavad enamat. See moodul õpetab sind looma vestlus-AI, mis mäletab konteksti ja hoiab olekut – erinevust ühekordsest demosest ja tootmiskõlbulikust rakendusest.

Me kasutame kogu juhendis Azure OpenAI GPT-5.2, sest selle arenenud järeldamisvõimed teevad eri mustrite käitumise selgemaks. Kui lisad mälu, näed ilmselgelt erinevust. See muudab lihtsamaks mõista, mida iga komponent sinu rakendusele annab.

Sa ehitad ühe rakenduse, mis demonstreerib mõlemaid mustreid:

**Olemitu vestlus** – iga päring on iseseisev. Mudelil puudub mälu varasematest sõnumitest. See on mustritüüp, mida kasutasid kiire algusega.

**Olemusega vestlus** – iga päring sisaldab vestluse ajalugu. Mudel hoiab konteksti läbi mitme käigu. Seda nõuavad tootmisrakendused.

## Eeldused

- Azure tellimus koos Azure OpenAI ligipääsuga
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Märkus:** Java, Maven, Azure CLI ja Azure Developer CLI (azd) on eelnevalt paigaldatud antud devcontaineris.

> **Märkus:** See moodul kasutab GPT-5.2 Azure OpenAI peal. Juurutamine toimub automaatselt `azd up` abil – ärge muutke mudeli nime koodis.

## Põhiprobleemi mõistmine

Keelemudelid on olemitud. Iga API kõne on iseseisev. Kui sa saadad "Minu nimi on John" ja siis küsid "Mis on minu nimi?", ei tea mudel, et just äsja end tutvustasid. Iga päringut koheldakse, nagu oleks see sinu esimene vestlus.

See sobib lihtsate küsimuste ja vastuste jaoks, kuid on kasutu tõeliste rakenduste puhul. Klienditeeninduse botid peavad meeles pidama, mida neile räägiti. Isiklikud assistendid vajavad konteksti. Iga mitmekäiguline vestlus nõuab mälu.

<img src="../../../translated_images/et/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Olemitu vs oleme vestlused" width="800"/>

*Vahe olemitu (iseseisvad kõned) ja olemusega (kontekstiteadlikud) vestluste vahel*

## Tokenite mõistmine

Enne vestlustesse sukeldumist on tähtis mõista toiminguid ehk tokeneid – tekstist koosnevaid põhielemente, mida keelemudelid töödeldes kasutavad:

<img src="../../../translated_images/et/token-explanation.c39760d8ec650181.webp" alt="Tokeni seletus" width="800"/>

*Näide, kuidas tekst jaotatakse tokeniteks – "I love AI!" muutub 4 eraldi töötlemise üksuseks*

Tokenid on kui AI mudelite mõõtühikud teksti mõõtmiseks ja töötlemiseks. Sõnad, kirjavahemärgid ja isegi tühikud võivad olla tokenid. Sinu mudelil on piir kui palju tokeneid korraga töödelda saab (GPT-5.2 puhul 400 000, sisendtokeneid kuni 272 000 ja väljundtokeneid kuni 128 000). Tokenite mõistmine aitab sul hallata vestluse pikkust ja kulusid.

## Kuidas mälu töötab

Vestluse mälu lahendab olemituse probleemi, hoides vestluse ajalugu. Enne kui sa päringu mudelile saatad, lülitab raamistik eelnevad sobivad sõnumid ette. Kui küsid "Mis on minu nimi?", saadab süsteem tegelikult kogu vestluse ajaloo, võimaldades mudelil näha, et sa ütlesid äsja "Minu nimi on John."

LangChain4j pakub mälufunktsioone, mis selle automaatselt teevad. Sa valid, mitu sõnumit säilitada ja raamistik haldab kontekstuaakna suurust.

<img src="../../../translated_images/et/memory-window.bbe67f597eadabb3.webp" alt="Mäluakna kontseptsioon" width="800"/>

*MessageWindowChatMemory hoiab libisevat akent hiljutistest sõnumitest, automaatselt vanu kustutades*

## Kuidas see kasutab LangChain4j-d

See moodul laiendab kiire alguse näidet Spring Booti integreerimise ja vestlusmäluga. Siin on, kuidas komponendid omavahel sobituvad:

**Sõltuvused** – lisa kaks LangChain4j teeki:

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

**Vestlusmudel** – konfigureeri Azure OpenAI Spring bean-ina ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder loeb mandaadid keskkonnamuutujatest, mida `azd up` seab. `baseUrl` seadmine oma Azure endpoinile teeb OpenAI kliendi tööle Azure OpenAI-ga.

**Vestlusmälu** – jälgi vestluse ajalugu MessageWindowChatMemory abil ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Loo mälu `withMaxMessages(10)`, et hoida viimased 10 sõnumit. Lisa kasutaja ja AI sõnumid tüübikate wrapperitega: `UserMessage.from(text)` ja `AiMessage.from(text)`. Ajalugu saad `memory.messages()` abil ja seejärel saada mudelile. Teenus salvestab iga vestluse ID jaoks eraldi mälu, võimaldades mitmel kasutajal korraga vestelda.

> **🤖 Proovi koos [GitHub Copilot](https://github.com/features/copilot) Chatiga:** Ava [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ja küsi:
> - "Kuidas MessageWindowChatMemory otsustab, millised sõnumid kustutada, kui aken on täis?"
> - "Kas ma saan rakendada isiklikku mälu hoidmist andmebaasi kasutades, mitte ainult mälus?"
> - "Kuidas lisada kokkuvõttefunktsioon vana vestluse ajaloo kokkusurumiseks?"

Olemitu vestlus lõpp-punkt eirab mälu – lihtsalt `chatModel.chat(prompt)`, nagu kiire algus. Olemusega vestlus lisab sõnumid mällu, hangib ajaloo ja saadab selle kontekstina iga päringuga. Sama mudelikonfiguratsioon, erinevad mustrid.

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

> **Märkus:** Kui tekib ajapiirangu viga (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), käivitage `azd up` uuesti. Azure ressursid võivad endiselt taustal juurutuda ja kordamine lubab juurutusel lõpule jõuda, kui ressursid jõuavad lõppseisundisse.

See teeb järgmist:
1. Juurutab Azure OpenAI ressursi GPT-5.2 ja text-embedding-3-small mudelitega
2. Genereerib automaatselt projekti juurkausta `.env` faili mandaadiga
3. Seadistab kõik vajalikud keskkonnamuutujad

**Probleeme juurutamisega?** Vaata [Infrastruktuuri README-d](infra/README.md) üksikasjaliku probleemide lahendamise, alamdomeeni nime konfliktide, manuaalse Azure Portali juurutamise ja mudeli konfiguratsiooni juhendamise kohta.

**Kontrolli, kas juurutamine õnnestus:**

**Bash:**
```bash
cat ../.env  # Peaks kuvama AZURE_OPENAI_ENDPOINT, API_KEY jne.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaksite näitama AZURE_OPENAI_ENDPOINTi, API_KEYd jne.
```

> **Märkus:** `azd up` käsk genereerib `.env` faili automaatselt. Kui vajad hiljem selle uuendamist, saad kas muuta `.env` faili käsitsi või genereerida uuesti, käivitades:
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

Veendu, et projektikataloogi juures on `.env` fail Azure mandaadiga:

**Bash:**
```bash
cat ../.env  # Tõrjub AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Peaks näitama AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Käivita rakendused:**

**Valik 1: Kasutades Spring Boot Dashboardi (Soovitatav VS Code kasutajatele)**

Devcontainer sisaldab Spring Boot Dashboard laiendit, mis pakub visuaalset liidest kõigi Spring Boot rakenduste haldamiseks. Leiad selle VS Code'i vasakpoolsel tööriistaribal (otsi Spring Boot ikooni).

Spring Boot Dashboardist saad:
- Näha kõiki töölaua Spring Boot rakendusi
- Käivitada/peatada rakendusi ühe klikiga
- Reaalajas vaadata rakenduste logisid
- Jälgida rakenduste olekut

Lihtsalt klõpsa nupule "introduction" kõrval, et käivitada see moodul või alusta kõiki korraga.

<img src="../../../translated_images/et/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Valik 2: Kasutades shell-skripte**

Käivita kõik veebirakendused (moodulid 01-04):

**Bash:**
```bash
cd ..  # Juurekataloogist
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Juurekataloogist
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

Mõlemad skriptid laevad automaatselt keskkonnamuutujad juurkaustast `.env` failist ja ehitavad JAR-id, kui neid veel pole.

> **Märkus:** Kui soovid eelistada kõigi moodulite manuaalset ehitamist enne käivitamist:
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

Ava oma veebibrauseris http://localhost:8080.

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

Rakendus pakub veebiliidest kahe kõneimplementatsiooniga kõrvuti.

<img src="../../../translated_images/et/home-screen.121a03206ab910c0.webp" alt="Rakenduse avaleht" width="800"/>

*Armatuurlaud näitab nii lihtsat olemitut vestlust kui ka olemusega vestlust*

### Olemitu vestlus (vasak paneel)

Alusta sellest. Küsi „Minu nimi on John“ ja kohe pärast seda „Mis on minu nimi?“ Mudel ei mäleta, sest iga sõnum on iseseisev. See näitab põhiprobleemi, kuidas lihtne keelemudel on ilma vestluse kontekstita.

<img src="../../../translated_images/et/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Olemitu vestluse demo" width="800"/>

*AI ei mäleta eelnevas sõnumis öeldud nime*

### Olemusega vestlus (parem paneel)

Proovi nüüd sama järjestust siin. Küsi „Minu nimi on John“ ja siis „Mis on minu nimi?“ Seekord ta mäletab. Erinevuseks on MessageWindowChatMemory – see hoiab vestluse ajalugu ja lisab selle iga päringuga. Just nii toimib tootmiskõlbulik vestlus-AI.

<img src="../../../translated_images/et/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Olemusega vestluse demo" width="800"/>

*AI mäletab sinu nime varasemast vestlusest*

Mõlemad paneelid kasutavad sama GPT-5.2 mudelit. Erinevus on ainult mälus. See teeb selgeks, mida mälu sinu rakendusele toob ja miks see on tõeliste kasutusjuhtude jaoks hädavajalik.

## Järgmised sammud

**Järgmine moodul:** [02-prompt-engineering - Päringute inseneriteadus GPT-5.2-ga](../02-prompt-engineering/README.md)

---

**Navigeerimine:** [← Eelmine: Moodul 00 - Kiire algus](../00-quick-start/README.md) | [Tagasi avalehele](../README.md) | [Järgmine: Moodul 02 - Päringute inseneriteadus →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud tehisintellekti tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüdleme täpsuse poole, tuleb arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles on autoriteetne allikas. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->