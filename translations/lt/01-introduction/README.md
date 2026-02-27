# Modulis 01: Pradžia su LangChain4j

## Turinys

- [Vaizdo įrašo pristatymas](../../../01-introduction)
- [Ko išmoksite](../../../01-introduction)
- [Reikalavimai](../../../01-introduction)
- [Pagrindinės problemos supratimas](../../../01-introduction)
- [Tokenų supratimas](../../../01-introduction)
- [Kaip veikia atmintis](../../../01-introduction)
- [Kaip tai veikia su LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktūros diegimas](../../../01-introduction)
- [Programos paleidimas vietoje](../../../01-introduction)
- [Programos naudojimas](../../../01-introduction)
  - [Bevaldinis pokalbis (kairysis skydelis)](../../../01-introduction)
  - [Valdomas pokalbis (dešinysis skydelis)](../../../01-introduction)
- [Kiti žingsniai](../../../01-introduction)

## Vaizdo įrašo pristatymas

Žiūrėkite šią tiesioginę sesiją, kurioje paaiškinama, kaip pradėti naudotis šiuo moduliu: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Ko išmoksite

Jei užbaigėte greitą pradžią, matėte, kaip siųsti užklausas ir gauti atsakymus. Tai yra pagrindas, tačiau tikrosios programos reikalauja daugiau. Šiame modulyje mokysitės kurti pokalbių AI, kuris prisimena kontekstą ir palaiko būseną – tai skirtumas tarp vienkartinės demonstracijos ir gamybai paruoštos programos.

Viso vadovo metu naudosime Azure OpenAI GPT-5.2, nes jo pažangios samprotavimo galimybės padaro skirtingų modelių elgseną aiškesnę. Pridėjus atmintį, skirtumas tampa akivaizdus. Tai palengvina supratimą, ką kiekviena dalis prideda jūsų programai.

Sukursite vieną programą, kuri demonstruoja abi schemas:

**Bevaldinis pokalbis** – Kiekviena užklausa yra nepriklausoma. Modelis neprisimena ankstesnių žinučių. Tai yra schema, kurią naudojote greitojoje pradžioje.

**Valdomas pokalbis** – Kiekviena užklausa apima pokalbio istoriją. Modelis palaiko kontekstą per kelis pokalbio posūkius. Tai būtina gamybinėms programoms.

## Reikalavimai

- Azure prenumerata su Azure OpenAI prieiga
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Pastaba:** Java, Maven, Azure CLI ir Azure Developer CLI (azd) jau įdiegtos pateiktame devcontainer.

> **Pastaba:** Šis modulis naudoja GPT-5.2 Azure OpenAI. Diegimas automatiškai konfigūruojamas naudojant `azd up` – nekoreguokite modelio pavadinimo kode.

## Pagrindinės problemos supratimas

Kalbos modeliai yra bevaldiniai. Kiekvienas API kvietimas nepriklausomas. Jei pasakote „Mano vardas Jonas“ ir tada klausi „Koks mano vardas?“, modelis nežino, kad ką tik prisistatėte. Jis traktuoja kiekvieną užklausą, tarsi tai būtų pirmasis jūsų pokalbis.

Tai tinka paprastiems klausimams ir atsakymams, bet nenaudinga tikroms programoms. Klientų aptarnavimo botai turi prisiminti, ką jiems pasakėte. Asmeniniai asistentai reikalauja konteksto. Bet koks daugkartinis pokalbis reikalauja atminties.

<img src="../../../translated_images/lt/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Bevaldinio (nepriklausomi kvietimai) ir valdomo (konteksto palaikymas) pokalbių skirtumas*

## Tokenų supratimas

Prieš pradedant pokalbius svarbu suprasti tokenus – pagrindinius teksto vienetus, kuriuos apdoroja kalbos modeliai:

<img src="../../../translated_images/lt/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Teksto, pvz., „I love AI!“, skaidymas į tokenus – gaunami 4 atskiri apdorojimo vienetai*

Tokenai yra būdas AI modeliams matuoti ir apdoroti tekstą. Žodžiai, skyryba ir net tarpai gali būti tokenai. Jūsų modelio limitas, kiek tokenų jis gali apdoroti vienu metu, yra 400 000 (GPT-5.2, iki 272 000 įvesties tokenų ir 128 000 išvesties tokenų). Suprasdami tokenus lengviau reguliuoti pokalbio ilgį ir sąnaudas.

## Kaip veikia atmintis

Pokalbių atmintis sprendžia bevaldinę problemą saugodama pokalbio istoriją. Prieš nusiųsdama užklausą modeliui, sistema prideda aktualias ankstesnes žinutes. Kai klausi „Koks mano vardas?“, sistema iš tiesų siunčia visą pokalbio istoriją, leisdama modeliui matyti, kad anksčiau pasakėte „Mano vardas Jonas“.

LangChain4j teikia atminties įgyvendinimus, kurie tai automatizuoja. Pasirenkate, kiek žinučių laikyti, o sistema tvarko konteksto langą.

<img src="../../../translated_images/lt/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory palaiko slenkamąjį langą su naujausiomis žinutėmis, automatiškai pašalindamas senas*

## Kaip tai veikia su LangChain4j

Šis modulis išplečia greitosios pradžios pavyzdį integruojant Spring Boot ir pridedant pokalbių atmintį. Štai kaip dalys dera:

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

**Pokalbių modelis** – konfigūruokite Azure OpenAI kaip Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder’is paima paskyrų duomenis iš aplinkos kintamųjų, nustatytų `azd up`. `baseUrl` nurodote savo Azure endpoint’ą, kad OpenAI klientas veiktų su Azure OpenAI.

**Pokalbių atmintis** – sekite pokalbio istoriją su MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Sukurkite atmintį su `withMaxMessages(10)`, kad išlaikytumėte paskutines 10 žinučių. Pridėkite naudotojo ir AI žinutes su tipais: `UserMessage.from(text)` ir `AiMessage.from(text)`. Gaukite istoriją per `memory.messages()` ir siųskite modeliui. Servisas saugo atskiras atmintis pagal pokalbio ID, leidžiant kelis vartotojus bendrauti vienu metu.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atverkite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ir paklauskite:
> - „Kaip MessageWindowChatMemory nusprendžia, kurios žinutės pašalinamos, kai langas yra pilnas?“
> - „Ar galiu įgyvendinti custom atmintį naudojant duomenų bazę vietoje atminties operacijų? “
> - „Kaip pridėčiau santraukų kūrimą, kad suspausti seną pokalbio istoriją?“

Bevaldinis pokalbių galinis taškas visiškai neprisijungia prie atminties – tiesiog `chatModel.chat(prompt)` kaip greitojoje pradžioje. Valdomas galinis taškas priduria žinutes į atmintį, gauna istoriją ir siunčia ją su kiekvienu užklausimu. Modelis tas pats, tik skirtingos schemos.

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

> **Pastaba:** Jei atsiranda laiko viršijimo klaida (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), tiesiog vėl paleiskite `azd up`. Azure ištekliai gali dar būti diegimo būsenoje, ir kartojimas leidžia baigti diegimą, kai ištekliai pasiekia galutinę būseną.

Tai atliks:
1. Diegs Azure OpenAI išteklius su GPT-5.2 ir text-embedding-3-small modeliais
2. Automatiškai sugeneruos `.env` failą projekto šakniniame kataloge su paskyros duomenimis
3. Nustatys visus reikiamus aplinkos kintamuosius

**Turite problemų su diegimu?** Peržiūrėkite [Infrastruktūros README](infra/README.md) su išsamia problemų sprendimo informacija, įskaitant subdomenų konfliktus, Azure Portal rankinio diegimo žingsnius ir modelių konfigūracijos gaires.

**Patikrinkite, ar diegimas pavyko:**

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

> **Pastaba:** Komanda `azd up` automatiškai sukuria `.env` failą. Jei vėliau norėsite jį atnaujinti, galite redaguoti `.env` failą rankiniu būdu arba sugeneruoti iš naujo paleisdami:
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

**Variantas 1: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

Dev container įtraukia Spring Boot Dashboard plėtinį, kuris suteikia vizualų sąsają valdyti visas Spring Boot programas. Jį rasite Activity Bar kairėje VS Code pusėje (pažymėta Spring Boot ikona).

Iš Spring Boot Dashboard galite:
- Peržiūrėti visas Spring Boot programas dirbamojoje aplinkoje
- Vienu paspaudimu paleisti/stabdyti programas
- Realiais laiko matyti programų įrašus
- Stebėti programų būseną

Tiesiog paspauskite paleidimo mygtuką prie „introduction“, kad pradėtumėte šį modulį, arba paleiskite visus modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Variantas 2: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (01-04 moduliai):

**Bash:**
```bash
cd ..  # Iš šaknininio katalogo
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

Abu skriptai automatiškai įkrauna aplinkos kintamuosius iš šakniniame kataloge esančio `.env` failo ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite prieš paleidimą visus modulius sukompiliuoti rankiniu būdu:
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

Atverkite http://localhost:8080 naršyklėje.

**Norėdami sustabdyti:**

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

Programa pateikia internetinę sąsają su dviem pokalbių implementacijomis šalia viena kitos.

<img src="../../../translated_images/lt/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Skydelis su paprasto pokalbio (bevaldinis) ir pokalbio su būsena (valdomas) pasirinkimais*

### Bevaldinis pokalbis (kairysis skydelis)

Išbandykite pirmiausia čia. Paklauskite „Mano vardas Jonas“ ir iškart po to „Koks mano vardas?“ Modelis neprisimins, nes kiekviena žinutė yra nepriklausoma. Tai demonstruoja pagrindinę kalbos modelių integravimo problemą - nėra pokalbio konteksto.

<img src="../../../translated_images/lt/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI neprisimena jūsų vardo iš ankstesnės žinutės*

### Valdomas pokalbis (dešinysis skydelis)

Dabar išbandykite tą pačią seką čia. Paklauskite „Mano vardas Jonas“ ir tada „Koks mano vardas?“ Šį kartą AI prisimena. Skirtumas yra MessageWindowChatMemory – jis palaiko pokalbio istoriją ir įtraukia ją kiekvienai užklausai. Taip veikia gamybinis pokalbių AI.

<img src="../../../translated_images/lt/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI prisimena jūsų vardą iš ankstesnio pokalbio momento*

Abi pusės naudoja tą patį GPT-5.2 modelį. Vienintelis skirtumas – atmintis. Tai aiškiai parodo, ką atmintis duoda jūsų programai ir kodėl ji būtina tikruose naudojimo atvejuose.

## Kiti žingsniai

**Kitas modulis:** [02-prompt-engineering - Prompt Engineering su GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 00 - Greitoji pradžia](../00-quick-start/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, turėkite omenyje, kad automatiniai vertimai gali turėti klaidų arba netikslumų. Originalus dokumentas gimtąja kalba turėtų būti laikomas pagrindiniu ir autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudotis profesionaliu žmogaus atliktu vertimu. Mes neatsakome už jokius nesusipratimus ar klaidingas interpretacijas, atsiradusias naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->