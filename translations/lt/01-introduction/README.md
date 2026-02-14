# Modulis 01: Pradžia su LangChain4j

## Turinys

- [Ką Išmoksite](../../../01-introduction)
- [Reikalavimai](../../../01-introduction)
- [Pagrindinės Problemos Supratimas](../../../01-introduction)
- [Žetonų Supratimas](../../../01-introduction)
- [Kaip Veikia Atmintis](../../../01-introduction)
- [Kaip Tai Naudoja LangChain4j](../../../01-introduction)
- [Azure OpenAI Infrastruktūros Diegimas](../../../01-introduction)
- [Programos Paleidimas Vietoje](../../../01-introduction)
- [Programos Naudojimas](../../../01-introduction)
  - [Stateless Chat (Kairysis Skydelis)](../../../01-introduction)
  - [Stateful Chat (Dešinysis Skydelis)](../../../01-introduction)
- [Kiti Žingsniai](../../../01-introduction)

## Ką Išmoksite

Jei baigėte greitą pradžią, matėte, kaip siųsti užklausas ir gauti atsakymus. Tai yra pagrindas, bet tikroms programoms reikia daugiau. Šis modulis mokys jus kurti pokalbių AI, kuris prisimena kontekstą ir palaiko būseną - skirtumas tarp vienkartinės demonstracijos ir gamybai paruoštos programos.

Šioje instrukcijoje visą laiką naudosime Azure OpenAI GPT-5.2, nes jo pažangios samprotavimo galimybės daro skirtingų modelių elgesį aiškesnį. Pridėjus atmintį, skirtumas bus aiškiai matomas. Tai padeda suprasti, ką kiekvienas komponentas prideda jūsų programai.

Jūs sukursite vieną programą, kuri demonstruoja abu modelius:

**Stateless Chat** – Kiekvienas užklausa yra nepriklausoma. Modelis neprisimena ankstesnių žinučių. Tai yra modelis, kurį naudojote greitoje pradžioje.

**Stateful Conversation** – Kiekviena užklausa apima pokalbio istoriją. Modelis palaiko kontekstą per kelis sukimus. Tai reikalauja gamybos programos.

## Reikalavimai

- Azure prenumerata su Azure OpenAI prieiga
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Pastaba:** Java, Maven, Azure CLI ir Azure Developer CLI (azd) yra iš anksto įdiegti pateiktame devcontainer.

> **Pastaba:** Šis modulis naudoja GPT-5.2 Azure OpenAI. Diegimas konfigūruojamas automatiškai per `azd up` – nekeiskite modelio vardo kode.

## Pagrindinės Problemos Supratimas

Kalbos modeliai yra stateless. Kiekvienas API kvietimas yra nepriklausomas. Jei pasakote "Mano vardas John", o paskui paklausiate "Koks mano vardas?", modelis neturi jokios idėjos, kad jūs ką tik prisistatėte. Jis traktuoja kiekvieną užklausą taip, tarsi tai būtų pirmasis jūsų pokalbis.

Tai tinka paprastiems klausimams ir atsakymams, bet yra nenaudinga tikroms programoms. Klientų aptarnavimo botams reikia prisiminti, ką jiems pasakėte. Asmeniniai asistentai reikalauja konteksto. Bet kuris pokalbis su keliais sukimais reikalauja atminties.

<img src="../../../translated_images/lt/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Skirtumas tarp stateless (nepriklausomų kreipinių) ir stateful (kontekstą suprantančių) pokalbių*

## Žetonų Supratimas

Prieš pradedant pokalbius svarbu suprasti žetonus – pagrindinius teksto vienetus, kuriuos apdoroja kalbos modeliai:

<img src="../../../translated_images/lt/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Pavyzdys, kaip tekstas suskaidomas į žetonus – "I love AI!" tampa 4 atskiromis apdorojimo vienetais*

Žetonai yra tai, kaip AI modeliai matuoja ir apdoroja tekstą. Žodžiai, skyryba ir net tarpai gali būti žetonai. Jūsų modelis turi ribą, kiek žetonų gali apdoroti vienu metu (400,000 GPT-5.2, su iki 272,000 įėjimo žetonų ir 128,000 išėjimo). Žetonų supratimas padeda valdyti pokalbių ilgį ir kaštus.

## Kaip Veikia Atmintis

Pokalbių atmintis sprendžia stateless problemą palaikydama pokalbio istoriją. Prieš siųsdamas jūsų užklausą modeliui, karkasas priduria atitinkamas ankstesnes žinutes. Kai klausiate "Koks mano vardas?", sistema iš tikrųjų siunčia visą pokalbio istoriją, leidžiant modeliui matyti, kad anksčiau pasakėte "Mano vardas John."

LangChain4j teikia atminties įgyvendinimus, kurie tai valdo automatiškai. Jūs pasirinksite, kiek žinučių laikyti, o karkasas valdys konteksto langą.

<img src="../../../translated_images/lt/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory palaiko slenkantį langą su naujausiomis žinutėmis, automatiškai meta senas*

## Kaip Tai Naudoja LangChain4j

Šis modulis išplečia greitą pradžią integruodamas Spring Boot ir pridedant pokalbių atmintį. Štai kaip elementai dera:

**Priklausomybės** – Pridėkite dvi LangChain4j bibliotekas:

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

Builderis skaito kredencialus iš aplinkos kintamųjų, nustatytų `azd up`. Nustatymas `baseUrl` į jūsų Azure galutinį tašką leidžia OpenAI klientui veikti su Azure OpenAI.

**Pokalbių atmintis** – Sekite pokalbio istoriją su MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Sukurkite atmintį su `withMaxMessages(10)` – saugokite paskutines 10 žinučių. Pridėkite naudotojo ir AI žinutes su tipinėmis apvijomis: `UserMessage.from(text)` ir `AiMessage.from(text)`. Gaukite istoriją su `memory.messages()` ir siųskite modeliui. Servisas saugo atskiras atminties instancijas pagal pokalbio ID, leidžiant keliems naudotojams diskutuoti vienu metu.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ir paklauskite:
> - "Kaip MessageWindowChatMemory nusprendžia, kurias žinutes mesti, kai langas pilnas?"
> - "Ar galiu įgyvendinti savo atminties saugyklą naudojant duomenų bazę vietoje atminties?"
> - "Kaip pridėčiau santrauką, kad suspaustų seną pokalbio istoriją?"

Stateless pokalbių galinis taškas visiškai apeina atmintį – tiesiog `chatModel.chat(prompt)` kaip greitoje pradžioje. Stateful galinis taškas prideda žinutes į atmintį, gauna istoriją ir įtraukia tą kontekstą į kiekvieną užklausą. Tos pačios modelio konfigūracijos, skirtingi modeliai.

## Azure OpenAI Infrastruktūros Diegimas

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

> **Pastaba:** Jei susiduriate su laiko viršijimo klaida (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), tiesiog paleiskite `azd up` dar kartą. Azure resursai gali vis dar būti diegiami fone, o pakartojimas leidžia diegimui užbaigti pasiekus galutinę būseną.

Tai:
1. Diegs Azure OpenAI resursą su GPT-5.2 ir text-embedding-3-small modeliais
2. Automatiškai sugeneruos `.env` failą projekto šakniniame kataloge su kredencialais
3. Nustatys visus reikalingus aplinkos kintamuosius

**Turite diegimo problemų?** Žr. [Infrastruktūros README](infra/README.md) dėl išsamios problemų sprendimo, įskaitant potinklio pavadinimų konfliktus, rankinius Azure Portal diegimo žingsnius ir modelių konfigūracijos gaires.

**Patikrinkite, ar diegimas pavyko:**

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų būti rodoma AZURE_OPENAI_ENDPOINT, API_KEY ir kt.
```

> **Pastaba:** Komanda `azd up` automatiškai generuoja `.env` failą. Jei reikės vėliau atnaujinti, galite redaguoti `.env` rankiniu būdu ar atnaujinti paleisdami:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Programos Paleidimas Vietoje

**Patikrinkite diegimą:**

Įsitikinkite, kad `.env` failas yra pagrindiniame kataloge su Azure kredencialais:

**Bash:**
```bash
cat ../.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Turėtų rodyti AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Paleiskite programas:**

**1 variantas: Naudojant Spring Boot Dashboard (rekomenduojama VS Code vartotojams)**

Dev konteineryje yra Spring Boot Dashboard plėtinys, kuris suteikia vizualią sąsają valdyti visas Spring Boot programas. Jį rasite Activity Bar kairėje VS Code pusėje (pažymėtą Spring Boot ikona).

Iš Spring Boot Dashboard galite:
- Matyti visas turimas Spring Boot programas darbo zonoje
- Vienu spustelėjimu paleisti/stabdyti programas
- Matyti realiu laiku programų žurnalus
- Stebėti programų būseną

Tiesiog paspauskite paleidimo mygtuką prie "introduction", kad pradėtumėte šį modulį, arba paleiskite visus modulius iš karto.

<img src="../../../translated_images/lt/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**2 variantas: Naudojant shell skriptus**

Paleiskite visas žiniatinklio programas (moduliai 01-04):

**Bash:**
```bash
cd ..  # Iš šakninių katalogų
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

Abi scenarijai automatiškai užkraus aplinkos kintamuosius iš pagrindinio `.env` failo ir jei reikia, sukurs JAR failus.

> **Pastaba:** Jei norite rankiniu būdu pastatyti visus modulius prieš paleidimą:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Atidarykite http://localhost:8080 savo naršyklėje.

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

## Programos Naudojimas

Programa suteikia interneto sąsają su dviem pokalbių įgyvendinimais šalia vienas kito.

<img src="../../../translated_images/lt/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Skydelis su Simple Chat (stateless) ir Conversational Chat (stateful) pasirinkimais*

### Stateless Chat (Kairysis Skydelis)

Išbandykite tai pirmiausia. Paklauskite "Mano vardas John", tada iškart "Koks mano vardas?" Modelis neprisimins, nes kiekviena žinutė yra nepriklausoma. Tai iliustruoja pagrindinę problemą su paprasta kalbos modelio integracija – nėra pokalbio konteksto.

<img src="../../../translated_images/lt/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI neprisimena jūsų vardo iš ankstesnės žinutės*

### Stateful Chat (Dešinysis Skydelis)

Dabar išbandykite tą pačią seką čia. Paklauskite "Mano vardas John" ir tada "Koks mano vardas?" Šį kartą jis prisimena. Skirtumas yra MessageWindowChatMemory – jis palaiko pokalbio istoriją ir prideda ją prie kiekvienos užklausos. Šitaip veikia gamybos pokalbių AI.

<img src="../../../translated_images/lt/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI prisimena jūsų vardą iš ankstesnio pokalbio*

Abu skydeliai naudoja tą patį GPT-5.2 modelį. Vienintelis skirtumas yra atmintis. Tai aiškiai parodo, ką atmintis suteikia jūsų programai ir kodėl ji būtina tikriems naudojimo atvejams.

## Kiti Žingsniai

**Kitas Modulis:** [02-prompt-engineering - Užklausų inžinerija su GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Ankstesnis: Modulis 00 - Greita Pradžia](../00-quick-start/README.md) | [Atgal į Pagrindinį](../README.md) | [Kitas: Modulis 02 - Užklausų Inžinerija →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės atsisakymas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų arba netikslumų. Pirminis dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Dėl svarbios informacijos rekomenduojamas profesionalus žmogaus atliktas vertimas. Mes neatsakome už bet kokius nesusipratimus ar klaidingus aiškinimus, kilusius naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->