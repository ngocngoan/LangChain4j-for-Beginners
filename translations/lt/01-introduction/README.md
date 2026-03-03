# Modulis 01: Pradžia su LangChain4j

## Turinys

- [Vaizdo įrašo apžvalga](../../../01-introduction)
- [Ką išmoksi](../../../01-introduction)
- [Reikalavimai](../../../01-introduction)
- [Pagrindinės problemos supratimas](../../../01-introduction)
- [Tokenų supratimas](../../../01-introduction)
- [Kaip veikia atmintis](../../../01-introduction)
- [Kaip tai naudoja LangChain4j](../../../01-introduction)
- [Azure OpenAI infrastruktūros diegimas](../../../01-introduction)
- [Programos paleidimas lokaliai](../../../01-introduction)
- [Programos naudojimas](../../../01-introduction)
  - [Beprekių pokalbis (kairysis skydelis)](../../../01-introduction)
  - [Su preke pokalbis (dešinysis skydelis)](../../../01-introduction)
- [Kiti žingsniai](../../../01-introduction)

## Vaizdo įrašo apžvalga

Žiūrėkite šią tiesioginę sesiją, kurioje paaiškinama, kaip pradėti darbą su šiuo moduliu:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Pradžia su LangChain4j - tiesioginė sesija" width="800"/></a>

## Ką išmoksi

Greitojo starto metu naudoji GitHub modelius siųsti užklausas, kviesti įrankius, kurti RAG srautą ir testuoti apsaugos mechanizmus. Šie demonstraciniai pavyzdžiai parodė, kas įmanoma — dabar pereisime prie Azure OpenAI ir GPT-5.2 bei pradėsime kurti gamybinius aplikacijas. Šis modulis orientuotas į pokalbių AI, kuris įsimena kontekstą ir palaiko būseną — tai yra koncepcijos, kurias greitojo starto demonstracijos naudojo užkulisiuose, bet nepaaiškino.

Visame vadove naudosime Azure OpenAI GPT-5.2, nes jo pažangios loginio mąstymo galimybės aiškiau parodo skirtingų modelių elgseną. Kai pridėsite atmintį, skirtumas bus akivaizdus. Tai palengvina supratimą, ką kiekviena sudedamoji dalis atneša jūsų aplikacijai.

Sukursite vieną aplikaciją, kuri demonstruos abu modelius:

**Beprekis pokalbis** – Kiekviena užklausa yra nepriklausoma. Modelis neužsimena apie ankstesnes žinutes. Tai buvo modelis, kurį naudojote greitame paleidyme.

**Su preke pokalbis** – Kiekviena užklausa apima pokalbio istoriją. Modelis palaiko kontekstą per kelis žingsnius. Tai yra tai, ko reikalauja gamybinės aplikacijos.

## Reikalavimai

- Azure prenumerata su prieiga prie Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Pastaba:** Java, Maven, Azure CLI bei Azure Developer CLI (azd) yra iš anksto įdiegti pateiktame kūrimo konteineryje.

> **Pastaba:** Šis modulis naudoja GPT-5.2 Azure OpenAI. Diegimas yra automatiškai konfigūruojamas naudojant `azd up` – nekeiskite modelio pavadinimo kode.

## Pagrindinės problemos supratimas

Kalbų modeliai yra beprekiai. Kiekvienas API kvietimas yra nepriklausomas. Jei parašysite „Mano vardas John“ ir po to paklausite „Koks mano vardas?“, modelis neturi jokios informacijos, jog ką tik prisistatėte. Jis traktuoja kiekvieną užklausą tarsi jis būtų pirmas kartas, kai kalbatės.

Tai tinka paprastiems klausimų-atsakymų atvejams, bet yra nenaudinga tikroms aplikacijoms. Klientų aptarnavimo robotai turi įsiminti, ką jiems pasakėte. Asmeniniai asistentai turi turėti kontekstą. Bet koks daugiapakopis pokalbis reikalauja atminties.

Žemiau pateiktame paveiksle palyginamos dvi strategijos — kairėje beprekis kvietimas, kuris pamiršta jūsų vardą; dešinėje – su preke kvietimas, kuriam ChatMemory palaiko atmintį ir prisimena.

<img src="../../../translated_images/lt/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Beprekiai ir Su preke pokalbiai" width="800"/>

*Skirtumas tarp beprekių (nepriklausomų kvietimų) ir su preke (konteksto palaikymo) pokalbių*

## Tokenų supratimas

Prieš pradedant pokalbius, svarbu suprasti tokenus – pagrindinius teksto vienetus, kuriuos apdoroja kalbų modeliai:

<img src="../../../translated_images/lt/token-explanation.c39760d8ec650181.webp" alt="Tokenų paaiškinimas" width="800"/>

*Pavyzdys, kaip tekstas suskaidomas į tokenus – „I love AI!“ tampa 4 atskiromis apdorojimo dalimis*

Tokenai yra kaip AI modeliai matuoja ir apdoroja tekstą. Žodžiai, skyrybos ženklai ir net tarpai gali būti tokenais. Jūsų modeliui yra apribojimas, kiek tokenų jis gali apdoroti vienu metu (400 000 GPT-5.2 atveju, su iki 272 000 įėjimo tokenų ir 128 000 išėjimo tokenų). Suprasti tokenus padeda valdyti pokalbio ilgį ir sąnaudas.

## Kaip veikia atmintis

Pokalbių atmintis sprendžia beprekių problemą, palaikydama pokalbio istoriją. Prieš siųsdamas užklausą modeliui, karkasas prideda svarbias ankstesnes žinutes. Kai klausiate „Koks mano vardas?“, sistema iš tikrųjų siunčia visą pokalbio istoriją, leidžiančią modeliui matyti, kad anksčiau sakėte „Mano vardas John.“

LangChain4j suteikia atminties įgyvendinimus, kurie tai daro automatiškai. Jūs pasirenkate, kiek žinučių išlaikyti, o karkasas valdo konteksto langą. Žemiau esantis paveikslas rodo, kaip MessageWindowChatMemory palaiko slenkantį langą su naujausiomis žinutėmis.

<img src="../../../translated_images/lt/memory-window.bbe67f597eadabb3.webp" alt="Atminties lango koncepcija" width="800"/>

*MessageWindowChatMemory palaiko slenkantį langą su naujausiomis žinutėmis, automatiškai pašalindamas senas*

## Kaip tai naudoja LangChain4j

Šis modulis plečia greitojo paleidimo procesą, integruodamas Spring Boot ir pridedant pokalbių atmintį. Štai kaip komponentai susijungia:

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

**Pokalbių modelis** – sukonfigūruokite Azure OpenAI kaip Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builderis skaito kredencialus iš aplinkos kintamųjų, nustatytų naudojant `azd up`. `baseUrl` nustatymas į jūsų Azure endpointą leidžia OpenAI klientui dirbti su Azure OpenAI.

**Pokalbių atmintis** – sekite pokalbių istoriją su MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Sukurkite atmintį su `withMaxMessages(10)`, kad išlaikytumėte paskutines 10 žinučių. Pridėkite naudotojo ir AI žinutes naudodami tipizuotus apvalkalus: `UserMessage.from(text)` ir `AiMessage.from(text)`. Istoriją gaunate su `memory.messages()` ir siunčiate modeliui. Servisas saugo atskiras atminties instancijas kiekvienam pokalbio ID, leidžiant keliems naudotojams kalbėtis vienu metu.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ir paklauskite:
> - „Kaip MessageWindowChatMemory nusprendžia, kurias žinutes pašalinti, kai langas pilnas?“
> - „Ar galiu įgyvendinti savitą atminties saugojimą naudojant duomenų bazę vietoje atminties?“
> - „Kaip pridėčiau suvestinių funkcionavimą senos pokalbio istorijos glaudinimui?“

Beprekis pokalbio endpointas praleidžia atmintį – tiesiog `chatModel.chat(prompt)`, kaip greitojo starto metu. Su preke endpointas prideda žinutes į atmintį, gauna istoriją ir įtraukia kontekstą prie kiekvienos užklausos. Toks pats modelio konfigūravimas, skirtingos strategijos.

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

> **Pastaba:** Jei gaunate timeout klaidą (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), tiesiog paleiskite `azd up` dar kartą. Azure resursai gali dar būti diegiami fone, o pakartotinai bandant leidžia diegimui baigtis, kai resursai pasiekia galutinę būseną.

Tai atliks:
1. Azure OpenAI resurso su GPT-5.2 ir text-embedding-3-small modeliais diegimą
2. Automatinį `.env` failo generavimą projekto šakniniame kataloge su kredencialais
3. Visų reikiamų aplinkos kintamųjų nustatymą

**Susiduriate su diegimo problemomis?** Peržiūrėkite [Infrastruktūros README](infra/README.md) detaliam trikčių šalinimui, įskaitant potinklio vardų konfliktus, rankinius Azure Portalo diegimo žingsnius ir modelių konfigūracijos rekomendacijas.

**Patikrinkite, ar diegimas sėkmingas:**

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

> **Pastaba:** `azd up` komanda automatiškai generuoja `.env` failą. Jei norite jį atnaujinti vėliau, galite redaguoti `.env` rankiniu būdu arba sugeneruoti dar kartą vykdydami:
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

## Programos paleidimas lokaliai

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas yra šakniniame kataloge su Azure kredencialais. Vykdykite tai iš modulio katalogo (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų parodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programas:**

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code naudotojams)**

Kūrimo konteineryje yra Spring Boot Dashboard plėtinys, kuris suteikia vizualų sąsajos valdymą visoms Spring Boot aplikacijoms. Rasite jį veiklų juostoje kairėje VS Code pusėje (ieškokite Spring Boot ikonos).

Spring Boot Dashboard galite:
- Matyti visas prieinamas Spring Boot programas darbo erdvėje
- Vienu spustelėjimu paleisti/stabdyti programas
- Realiu laiku peržiūrėti programų žurnalus
- Stebėti programų būseną

Tiesiog spustelėkite paleidimo mygtuką šalia „introduction“, kad paleistumėte šį modulį arba paleiskite visus modulius vienu metu.

<img src="../../../translated_images/lt/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard VS Code — paleiskite, sustabdykite ir stebėkite visus modulius vienoje vietoje*

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (modulius 01-04):

**Bash:**
```bash
cd ..  # Iš pagrindinio katalogo
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iš šakninio katalogo
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

Abu skriptai automatiškai įkelia aplinkos kintamuosius iš šakniniame `.env` faile ir sukurs JAR failus, jei jų nėra.

> **Pastaba:** Jei norite rankiniu būdu sukompiliuoti visus modulius prieš paleidimą:
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

Atidarykite naršyklėje http://localhost:8080

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

Programa pateikia internetinę sąsają su dviem pokalbių įgyvendinimais šalia vienas kito.

<img src="../../../translated_images/lt/home-screen.121a03206ab910c0.webp" alt="Programos pradžios ekranas" width="800"/>

*Prietaisų skydelis rodo tiek Paprasto pokalbio (beprekio), tiek Pokalbio su išlaikymu (su preke) galimybes*

### Beprekis pokalbis (kairysis skydelis)

Pabandykite pradžioje šią versiją. Paklauskite „Mano vardas John“ ir tuoj pat „Koks mano vardas?“ Modelis neprisimins, nes kiekviena žinutė yra nepriklausoma. Tai demonstruoja pagrindinę problemą su paprasta kalbos modelio integracija – nėra pokalbio konteksto.

<img src="../../../translated_images/lt/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Beprekio pokalbio demonstracija" width="800"/>

*Dirbtinis intelektas neprisimena jūsų vardo iš ankstesnės žinutės*

### Pokalbis su išlaikymu (dešinysis skydelis)

Dabar išbandykite tą patį seką čia. Paklauskite „Mano vardas John“ ir po to „Koks mano vardas?“ Šį kartą AI prisimena. Skirtumas yra MessageWindowChatMemory – palaiko pokalbio istoriją ir prideda ją prie kiekvienos užklausos. Taip veikia gamybiniai pokalbių AI.

<img src="../../../translated_images/lt/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Pokalbio su išlaikymu demonstracija" width="800"/>

*Dirbtinis intelektas prisimena jūsų vardą iš ankstesnio pokalbio*

Abu skydeliai naudoja tą patį GPT-5.2 modelį. Vienintelis skirtumas – atmintis. Tai aiškiai parodo, ką atmintis atneša jūsų aplikacijai ir kodėl ji būtina tikrame naudojime.

## Kiti žingsniai

**Kitas modulis:** [02-prompt-engineering - Užklausų kūrimas su GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 00 - Greitas startas](../00-quick-start/README.md) | [Atgal į pagrindinį](../README.md) | [Kitas: Modulis 02 - Užklausų kūrimas →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės atsisakymas**:
Šis dokumentas buvo išverstas naudojant KI vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas tikruoju šaltiniu. Dėl svarbios informacijos rekomenduojamas profesionalus žmogaus vertimas. Mes neatsakome už jokius nesusipratimus ar klaidingas interpretacijas, kilusias naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->