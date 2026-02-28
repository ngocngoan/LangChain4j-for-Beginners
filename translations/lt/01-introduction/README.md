# 01 modulis: Pradžia su LangChain4j

## Turinys

- [Vaizdo įrašo apžvalga](../../../01-introduction)
- [Ką išmoksite](../../../01-introduction)
- [Reikalavimai](../../../01-introduction)
- [Pagrindinės problemos supratimas](../../../01-introduction)
- [Tokenų supratimas](../../../01-introduction)
- [Kaip veikia atmintis](../../../01-introduction)
- [Kaip tai veikia su LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktūros diegimas](../../../01-introduction)
- [Programos paleidimas vietoje](../../../01-introduction)
- [Programos naudojimas](../../../01-introduction)
  - [Be valstybės pokalbis (kairė skiltis)](../../../01-introduction)
  - [Valstybės pokalbis (dešinė skiltis)](../../../01-introduction)
- [Kiti žingsniai](../../../01-introduction)

## Vaizdo įrašo apžvalga

Žiūrėkite šią tiesioginę sesiją, kurioje paaiškinta, kaip pradėti dirbti su šiuo moduliu:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Ką išmoksite

Jei baigėte greitą pradžią, matėte, kaip siųsti užklausas ir gauti atsakymus. Tai yra pagrindas, tačiau tikroms programoms reikia daugiau. Šis modulis moko, kaip kurti pokalbių AI, kuris prisimena kontekstą ir palaiko valstybę – skirtumas tarp vienkartinio demo ir gamybai paruoštos programos.

Viso šio vadovo metu naudosime Azure OpenAI GPT-5.2, nes jo pažangios samprotavimo galimybės aiškiau atskleidžia skirtingų modelių elgesį. Pridėjus atmintį, skirtumas tampa akivaizdus. Tai palengvina suprasti, ką kiekviena dalis atneša jūsų programai.

Sukursite vieną programą, kuri demonstruoja abu modelius:

**Be valstybės pokalbis** – kiekviena užklausa yra nepriklausoma. Modelis neprisimena ankstesnių žinučių. Tai modelis, kurį naudojote greito pradžios metu.

**Valstybės pokalbis** – kiekviena užklausa apima pokalbio istoriją. Modelis palaiko kontekstą per kelis pokalbio posūkius. Tai reikalauja gamybos programos.

## Reikalavimai

- Azure prenumerata su Azure OpenAI prieiga
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Pastaba:** Java, Maven, Azure CLI ir Azure Developer CLI (azd) yra iš anksto įdiegtos suteiktame devcontainer'io aplinkoje.

> **Pastaba:** Šis modulis naudoja GPT-5.2 per Azure OpenAI. Diegimas automatiškai konfigūruojamas per `azd up` – nekeiskite modelio pavadinimo kodo faile.

## Pagrindinės problemos supratimas

Kalbos modeliai yra be valstybės. Kiekvienas API kvietimas yra nepriklausomas. Jei pasakote „Mano vardas John“ ir tada paklausiate „Koks mano vardas?“, modelis nežino, kad ką tik prisistatėte. Jis traktuoja kiekvieną užklausą, kaip pirmąjį pokalbį jūsų gyvenime.

Tai tinka paprastiems klausimų ir atsakymų atvejams, bet niekam kitam. Klientų aptarnavimo robotams reikia prisiminti, ką jiems pasakėte. Asmeniniams asistentams reikia konteksto. Bet kuris daugkartinis pokalbis reikalauja atminties.

<img src="../../../translated_images/lt/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Skirtumas tarp be valstybės (nepriklausomi kvietimai) ir valstybės palaikančių (konteksto suvokiančių) pokalbių*

## Tokenų supratimas

Prieš pradedant pokalbius svarbu suprasti tokenus – pagrindinius teksto vienetus, kuriuos apdoroja kalbos modeliai:

<img src="../../../translated_images/lt/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Pavyzdys, kaip tekstas suskaidomas į tokenus – „I love AI!“ tampa 4 atskiromis apdorojimo vienetais*

Tokenai yra būdas, kaip AI modeliai matuoja ir apdoroja tekstą. Žodžiai, skyrybos ženklai ir net tarpai gali būti tokenais. Jūsų modelio limitas, kiek tokenų jis gali apdoroti vienu metu (GPT-5.2 – 400 000 tokenų bendras limitas, iki 272 000 įvesties ir 128 000 išvesties tokenų). Supratimas apie tokenus padeda valdyti pokalbio ilgį ir sąnaudas.

## Kaip veikia atmintis

Pokalbių atmintis išsprendžia be valstybės problemą išlaikydama pokalbio istoriją. Prieš išsiunčiant užklausą modeliui, sistema prideda svarbius ankstesnius pranešimus. Kai klausi „Koks mano vardas?“, sistema išsiunčia visą pokalbio istoriją, leidžiančią modeliui matyti, kad anksčiau sakėte „Mano vardas John“.

LangChain4j suteikia atminties įgyvendinimus, kurie tai valdys automatiškai. Jūs nustatote, kiek žinučių laikyti, o sistema valdo konteksto langą.

<img src="../../../translated_images/lt/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory palaiko slenkantį naujausių žinučių langą, automatiškai pašalinant senas*

## Kaip tai veikia su LangChain4j

Šis modulis papildo greitą pradžią integruodamas Spring Boot ir pridėdamas pokalbių atmintį. Štai kaip dalys dera tarpusavyje:

**Priklausomybės** – pridėkite dvi LangChain4j bibliotekas:

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

**Pokalbių modelis** – Konfigūruokite Azure OpenAI kaip Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Kūrėjas gauna kredencialus iš aplinkos kintamųjų, nustatytų `azd up`. Nustatydami `baseUrl` į savo Azure galutinį tašką, OpenAI klientas veikia su Azure OpenAI.

**Pokalbių atmintis** – sekite pokalbio istoriją su MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Sukurkite atmintį su `withMaxMessages(10)`, kad būtų saugoma paskutinė 10 žinučių. Pridėkite vartotojo ir AI žinutes naudodami tipizuotus apvalkalus: `UserMessage.from(text)` ir `AiMessage.from(text)`. Istoriją paimkite per `memory.messages()` ir siųskite modeliui. Service dalis laiko atskiras atminties instancijas kiekvienam pokalbio ID, leidžiant keliems vartotojams pokalbiuoti vienu metu.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ir paklauskite:
> - „Kaip MessageWindowChatMemory nusprendžia, kurios žinutės pašalinamos, kai langas pilnas?“
> - „Ar galiu įgyvendinti savą atminties saugojimą naudojant duomenų bazę, o ne atmintį?“
> - „Kaip galėčiau pridėti santrauką, kad suspaustų seną pokalbio istoriją?“

Be valstybės pokalbių endpointas visiškai praleidžia atmintį – tiesiog `chatModel.chat(prompt)`, kaip greito starto metu. Valstybės endpointas prideda žinutes prie atminties, surenka istoriją ir prideda kontekstą kiekvienai užklausai. Ta pati modelio konfiguracija, skirtingi modeliai.

## Azure OpenAI infrastruktūros diegimas

**Bash:**
```bash
cd 01-introduction
azd up  # Pasirinkite prenumeratą ir vietą (rekomenduojama eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Pasirinkite prenumeratą ir vietą (rekomenduojama eastus2)
```

> **Pastaba:** Jei gaunate laiko išeigos klaidą (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), tiesiog dar kartą paleiskite `azd up`. Azure ištekliai gali vis dar būti diegimo stadijoje fone, o pakartotinis bandymas leidžia diegimui užbaigti, kai ištekliai pasieks galutinę būseną.

Tai atliks:
1. Įdiegti Azure OpenAI išteklius su GPT-5.2 ir text-embedding-3-small modeliais
2. Automatiškai sugeneruoti `.env` failą projekto šakniniame kataloge su kredencialais
3. Nustatyti visus reikalingus aplinkos kintamuosius

**Turite diegimo problemų?** Peržiūrėkite [Infrastruktūros README](infra/README.md) su detaliomis trikčių šalinimo instrukcijomis, įskaitant antrinio domeno konfliktus, rankinį diegimą Azure portale ir modelio konfigūravimo patarimus.

**Patikrinkite, ar diegimas sėkmingas:**

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir pan.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir pan.
```

> **Pastaba:** Komanda `azd up` automatiškai sugeneruoja `.env` failą. Jei vėliau reikia jį atnaujinti, galite redaguoti `.env` failą rankiniu būdu arba sugeneruoti jį iš naujo paleidę:
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

## Programos paleidimas vietoje

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas su Azure kredencialais yra šakniniame kataloge:

**Bash:**
```bash
cat ../.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programas:**

**1 parinktis: Spring Boot Dashboard naudojimas (rekomenduojama VS Code naudotojams)**

Devcontainer'yje yra Spring Boot Dashboard plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programas. Jį rasite veiklos juostoje kairėje VS Code (pažymėta Spring Boot piktograma).

Iš Spring Boot Dashboard galite:
- Matyti visas prieinamas Spring Boot programas darbo aplinkoje
- Vienu paspaudimu paleisti/užbaigti programas
- Realizuoti programų žurnalų peržiūrą tiesiogiai
- Stebėti programų būklę

Tiesiog spustelėkite paleidimo mygtuką šalia „introduction“, norėdami paleisti šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**2 parinktis: komandinės eilutės scenarijai**

Paleiskite visas žiniatinklio programas (modulius 01-04):

**Bash:**
```bash
cd ..  # Iš pagrindinio katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninių katalogų
.\start-all.ps1
```

Arba paleiskite tik šį modulį:

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

Abu scenarijai automatiškai įkelia aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų dar nėra.

> **Pastaba:** Jei pageidaujate prieš paleidimą visus modulius rankiniu būdu sukompiliuoti:
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

Naršyklėje atidarykite http://localhost:8080.

**Sustabdyti:**

**Bash:**
```bash
./stop.sh  # Tik šis modulis
# Arba
cd .. && ./stop-all.sh  # Visi moduliai
```

**PowerShell:**
```powershell
.\stop.ps1  # Tik šis modulis
# Arba
cd ..; .\stop-all.ps1  # Visi moduliai
```

## Programos naudojimas

Programa suteikia internetinę sąsają su dviem šalia esančiomis pokalbių realizacijomis.

<img src="../../../translated_images/lt/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Pagrindinis skydelis, rodantis Abi paprasto pokalbio (be valstybės) ir pokalbio su būsena (valstybės) pasirinkimus*

### Be valstybės pokalbis (kairė skiltis)

Išbandykite pirmiausia šią opciją. Paklauskite „Mano vardas John“, o tada iškart „Koks mano vardas?“ Modelis neprisimins, nes kiekviena žinutė yra nepriklausoma. Tai demonstruoja pagrindinę problemą su paprastu kalbos modelio integravimu – nėra pokalbio konteksto.

<img src="../../../translated_images/lt/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI neprisimena jūsų vardo iš ankstesnės žinutės*

### Valstybės pokalbis (dešinė skiltis)

Dabar išbandykite tą patį seką čia. Paklauskite „Mano vardas John“, o tada „Koks mano vardas?“ Šį kartą jis prisimena. Skirtumas yra MessageWindowChatMemory – ji palaiko pokalbio istoriją ir įtraukia ją kiekvienai užklausai. Taip veikia gamybiniai pokalbių AI.

<img src="../../../translated_images/lt/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI prisimena jūsų vardą iš ankstesnio pokalbio etapo*

Abi skiltys naudoja tą patį GPT-5.2 modelį. Vienintelis skirtumas – atmintis. Tai aiškiai parodo, ką atmintis suteikia jūsų programai ir kodėl ji būtina tikrose naudojimo situacijose.

## Kiti žingsniai

**Kitas modulis:** [02-prompt-engineering - Skatinimo inžinerija su GPT-5.2](../02-prompt-engineering/README.md)

---

**Naršymas:** [← Ankstesnis: 00 modulis - Greita pradžia](../00-quick-start/README.md) | [Atgal į pradžią](../README.md) | [Kitas: 02 modulis - Skatinimo inžinerija →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų arba netikslumų. Autentišku laikytinas originalus dokumentas jo gimtąja kalba. Svarbiai informacijai rekomenduojamas profesionalus žmogaus vertimas. Mes neatsakome už jokius nesusipratimus ar neteisingus interpretavimus, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->