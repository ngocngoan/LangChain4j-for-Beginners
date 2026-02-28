# Modul 01: Začetek z LangChain4j

## Vsebina

- [Video vodič](../../../01-introduction)
- [Kaj boste izvedeli](../../../01-introduction)
- [Predpogoji](../../../01-introduction)
- [Razumevanje osnovnega problema](../../../01-introduction)
- [Razumevanje tokenov](../../../01-introduction)
- [Kako deluje spomin](../../../01-introduction)
- [Kako to uporablja LangChain4j](../../../01-introduction)
- [Namestitev infrastrukture Azure OpenAI](../../../01-introduction)
- [Zagon aplikacije lokalno](../../../01-introduction)
- [Uporaba aplikacije](../../../01-introduction)
  - [Brezdržavni klepet (levi panel)](../../../01-introduction)
  - [Zdravi klepet (desni panel)](../../../01-introduction)
- [Nadaljnji koraki](../../../01-introduction)

## Video vodič

Oglejte si to v živo oddajo, ki pojasnjuje, kako začeti z modulom: [Začetek z LangChain4j - V živo](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Kaj boste izvedeli

Če ste zaključili hiter začetek, ste videli, kako pošiljati pozive in prejemati odgovore. To je osnova, vendar prave aplikacije potrebujejo več. Ta modul vas uči, kako zgraditi pogovorni AI, ki si zapomni kontekst in ohranja stanje - razlika med enkratno predstavitvijo in aplikacijo, pripravljen na produkcijo.

V celotnem priročniku bomo uporabljali Azure OpenAI GPT-5.2, ker njegove napredne sposobnosti razmišljanja naredijo obnašanje različnih vzorcev bolj očitno. Ko dodate spomin, boste jasno videli razliko. To olajša razumevanje, kaj vsak komponent prispeva vaši aplikaciji.

Zgradili boste eno aplikacijo, ki prikazuje oba vzorca:

**Brezdržavni klepet** - Vsak zahtevek je neodvisen. Model se ne spominja prejšnjih sporočil. To je vzorec, ki ste ga uporabili v hitrem začetku.

**Zdravi pogovor** - Vsak zahtevek vključuje zgodovino pogovora. Model ohranja kontekst skozi več krogov. To zahtevajo produkcijske aplikacije.

## Predpogoji

- Azure naročnina z dostopom do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opomba:** Java, Maven, Azure CLI in Azure Developer CLI (azd) so v prej omenjenem devcontainer že nameščeni.

> **Opomba:** Ta modul uporablja GPT-5.2 na Azure OpenAI. Namestitev je samodejno konfigurirana preko `azd up` – ne spreminjajte imena modela v kodi.

## Razumevanje osnovnega problema

Jezikovni modeli so brezdržavni. Vsak klic API-ja je neodvisen. Če pošljete "Moje ime je John" in nato vprašate "Kako mi je ime?", model nima pojma, da ste se pravkar predstavili. Vsak zahtevek obravnava, kot da je prvi pogovor, kar ste ga imeli.

To je v redu za preprosta vprašanja in odgovore, a neuporabno za resne aplikacije. Roboti za podporo strankam si morajo zapomniti, kaj ste jim povedali. Osebni pomočniki potrebujejo kontekst. Vsak pogovor z več krogi zahteva spomin.

<img src="../../../translated_images/sl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Brezdržavni proti zdravi pogovori" width="800"/>

*Razlika med brezdržavnimi (neodvisnimi klici) in zdravimi (zavedajočimi se konteksta) pogovori*

## Razumevanje tokenov

Preden se poglobite v pogovore, je pomembno razumeti tokene – osnovne enote besedila, s katerimi jezikovni modeli razpolagajo:

<img src="../../../translated_images/sl/token-explanation.c39760d8ec650181.webp" alt="Razlaga tokenov" width="800"/>

*Primer, kako se besedilo razdeli na tokene – "I love AI!" postane 4 ločene enote za obdelavo*

Tokeni so način, kako AI modeli merijo in obdelujejo besedilo. Besede, ločila in celo presledki so lahko tokeni. Vaš model ima omejitev, koliko tokenov lahko obdeluje naenkrat (400.000 za GPT-5.2, z do 272.000 vhodnimi tokeni in 128.000 izhodnimi tokeni). Razumevanje tokenov pomaga upravljati dolžino pogovora in stroške.

## Kako deluje spomin

Klepetni spomin rešuje problem brezdržavnosti z ohranjanjem zgodovine pogovora. Preden pošljete svoj zahtevek modelu, okvir doda relevantna prejšnja sporočila. Ko vprašate "Kako mi je ime?", sistem pošlje celotno zgodovino pogovora, kar omogoči modelu, da vidi, da ste prej rekli "Moje ime je John."

LangChain4j ponuja implementacije spomina, ki to upravljajo samodejno. Izberete, koliko sporočil želite shraniti, okvir pa vodi kontekstno okno.

<img src="../../../translated_images/sl/memory-window.bbe67f597eadabb3.webp" alt="Koncept okna spomina" width="800"/>

*MessageWindowChatMemory ohranja drseče okno nedavnih sporočil in samodejno odstranjuje starejša*

## Kako to uporablja LangChain4j

Ta modul razširja hiter začetek z integracijo Spring Boot in dodajanjem spomina za pogovore. Tako se deli povežejo:

**Odvisnosti** – Dodajte dve knjižnici LangChain4j:

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

**Klepetni model** – Konfigurirajte Azure OpenAI kot Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Graditelj prebere poverilnice iz okoljskih spremenljivk, nastavljenih z `azd up`. Nastavitev `baseUrl` na vaš Azure endpoint omogoči delovanje OpenAI klienta z Azure OpenAI.

**Spomin pogovora** – Spremljajte zgodovino klepeta z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Ustvarite spomin z `withMaxMessages(10)` za ohranjanje zadnjih 10 sporočil. Dodajte sporočila uporabnika in AI s tipiziranimi ovoji: `UserMessage.from(text)` in `AiMessage.from(text)`. Zgodovino pridobite z `memory.messages()` in jo pošljite modelu. Storitev hrani ločene primere spomina za vsak ID pogovora, kar omogoča več uporabnikom hkrati klepetati.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) in vprašajte:
> - "Kako MessageWindowChatMemory odloča, katera sporočila odstrani, ko je okno polno?"
> - "Ali lahko implementiram lastno shranjevanje spomina z uporabo baze podatkov namesto v pomnilniku?"
> - "Kako bi dodal povzetek za stiskanje stare zgodovine pogovorov?"

Brezdržavni klepetni endpoint povsem preskoči spomin – samo `chatModel.chat(prompt)` kot v hitrem začetku. Zdravi endpoint dodaja sporočila v spomin, pridobiva zgodovino in vključuje ta kontekst z vsakim zahtevkom. Enaka konfiguracija modela, različni vzorci.

## Namestitev infrastrukture Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Izberite naročnino in lokacijo (priporočeno eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Izberite naročnino in lokacijo (priporočeno eastus2)
```

> **Opomba:** Če prejmete napako timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), preprosto zaženite `azd up` znova. Azure viri se lahko še nameščajo v ozadju, ponovni poskus omogoči dokončanje namestitve, ko viri dosežejo terminalno stanje.

To bo:
1. Namestilo Azure OpenAI vir z GPT-5.2 in modeli text-embedding-3-small
2. Samodejno ustvarilo datoteko `.env` v korenu projekta s poverilnicami
3. Nastavilo vse potrebne okoljske spremenljivke

**Imate težave z namestitvijo?** Oglejte si [README infrastrukture](infra/README.md) za podrobna navodila za odpravljanje težav, vključno s konfliktnimi imeni poddomen, ročnimi koraki namestitve v Azure Portal in navodili za konfiguracijo modela.

**Preverite uspešnost namestitve:**

**Bash:**
```bash
cat ../.env  # Naj bi prikazoval AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj prikaže AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

> **Opomba:** Ukaz `azd up` samodejno ustvari `.env` datoteko. Če jo želite kasneje posodobiti, jo lahko ročno uredite ali znova ustvarite z izvajanjem:
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

## Zagon aplikacije lokalno

**Preverite namestitev:**

Prepričajte se, da datoteka `.env` obstaja v korenskem imeniku z Azure poverilnicami:

**Bash:**
```bash
cat ../.env  # Prikazati naj bi AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj prikazuje AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacije:**

**Možnost 1: Uporaba Spring Boot Dashboard (priporočeno za uporabnike VS Code)**

Dev container vključuje razširitev Spring Boot Dashboard, ki omogoča vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v Activity Bar na levi strani VS Code (ikona Spring Boot).

Iz Spring Boot Dashboarda lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času spremljate dnevnike aplikacij
- Spremljate status aplikacij

Preprosto kliknite gumb za zagon ob "introduction", da začnete ta modul, ali zaženite vse module hkrati.

<img src="../../../translated_images/sl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba ukaznih skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenske mape
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenske mape
.\start-all.ps1
```

Ali zaženite samo ta modul:

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

Oba skripta samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in sestavita JAR-je, če še ne obstajajo.

> **Opomba:** Če želite pred zagonom ročno sestaviti vse module:
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

Odprite http://localhost:8080 v vašem brskalniku.

**Zaustavitev:**

**Bash:**
```bash
./stop.sh  # Samo modul
# Ali
cd .. && ./stop-all.sh  # Vsi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ta modul
# Ali
cd ..; .\stop-all.ps1  # Vse module
```

## Uporaba aplikacije

Aplikacija nudi spletni vmesnik z dvema implementacijama klepeta ena ob drugi.

<img src="../../../translated_images/sl/home-screen.121a03206ab910c0.webp" alt="Zaslon domače strani aplikacije" width="800"/>

*Nadzorna plošča prikazuje obe možnosti: Enostavni klepet (brezdržavni) in Pogovorni klepet (zdravi)*

### Brezdržavni klepet (levi panel)

Poskusite najprej to. Vprašajte "Moje ime je John" in nato takoj "Kako mi je ime?" Model si ne bo zapomnil, ker je vsako sporočilo neodvisno. To prikazuje osnovni problem z osnovno integracijo jezikovnega modela - brez konteksta pogovora.

<img src="../../../translated_images/sl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo brezdržavnega klepeta" width="800"/>

*AI si ne zapomni vašega imena iz prejšnjega sporočila*

### Zdravi klepet (desni panel)

Sedaj poskusite isto zaporedje tukaj. Vprašajte "Moje ime je John" in nato "Kako mi je ime?" Tokrat si zapomni. Razlika je MessageWindowChatMemory - ohranja zgodovino pogovora in jo vključuje v vsak zahtevek. Tako deluje produkcijski pogovorni AI.

<img src="../../../translated_images/sl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo zdravega klepeta" width="800"/>

*AI si spomni vaše ime iz prej v pogovoru*

Oba panela uporabljata isti model GPT-5.2. Edina razlika je spomin. To jasno pokaže, kaj spomin prinaša vaši aplikaciji in zakaj je ključen za resnične primere uporabe.

## Nadaljnji koraki

**Naslednji modul:** [02-prompt-inženiring - Prompt inženiring z GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prejšnji: Modul 00 - Hiter začetek](../00-quick-start/README.md) | [Nazaj na uvod](../README.md) | [Naprej: Modul 02 - Prompt inženiring →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o omejitvi odgovornosti**:  
Dokument je bil preveden z uporabo storitve za avtomatski prevod AI [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko samodejni prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku velja za avtoritativni vir. Za ključne informacije priporočamo strokovni prevod s strani človeka. Za morebitne nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda, ne odgovarjamo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->