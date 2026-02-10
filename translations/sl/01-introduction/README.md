# Modul 01: Začetek z LangChain4j

## Kazalo vsebine

- [Kaj se boste naučili](../../../01-introduction)
- [Predpogoji](../../../01-introduction)
- [Razumevanje temeljne težave](../../../01-introduction)
- [Razumevanje tokenov](../../../01-introduction)
- [Kako deluje pomnilnik](../../../01-introduction)
- [Kako to uporablja LangChain4j](../../../01-introduction)
- [Namestitev infrastrukture Azure OpenAI](../../../01-introduction)
- [Zagon aplikacije lokalno](../../../01-introduction)
- [Uporaba aplikacije](../../../01-introduction)
  - [Stanje brez klepeta (levo okno)](../../../01-introduction)
  - [Stanje s klepetom (desno okno)](../../../01-introduction)
- [Nadaljnji koraki](../../../01-introduction)

## Kaj se boste naučili

Če ste opravili hitri začetek, ste videli, kako poslati pozive in dobiti odgovore. To je osnova, vendar prave aplikacije zahtevajo več. Ta modul vas uči, kako zgraditi pogovorno AI, ki si zapomni kontekst in ohranja stanje - razlika med enkratnim prikazom in aplikacijo, ki je pripravljena za produkcijo.

Uporabili bomo GPT-5.2 Azure OpenAI skozi celotni vodič, ker njegove napredne zmožnosti razmišljanja jasno prikažejo obnašanje različnih vzorcev. Ko dodate pomnilnik, boste jasno videli razliko. Tako je lažje razumeti, kaj vsak del prinaša vaši aplikaciji.

Zgradili boste eno aplikacijo, ki prikazuje oba vzorca:

**Klepet brez stanja** - Vsak zahtevek je neodvisen. Model nima spomina prejšnjih sporočil. To je vzorec, ki ste ga uporabili v hitrem začetku.

**Pogovorni klepet s stanjem** - Vsak zahtevek vključuje zgodovino pogovora. Model ohranja kontekst skozi več zank. To je tisto, kar zahtevajo produkcijske aplikacije.

## Predpogoji

- Azure naročnina z dostopom do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opomba:** Java, Maven, Azure CLI in Azure Developer CLI (azd) so prednameščeni v priloženem devcontainerju.

> **Opomba:** Ta modul uporablja GPT-5.2 na Azure OpenAI. Namestitev je avtomatsko konfigurirana prek `azd up` - ne spreminjajte imena modela v kodi.

## Razumevanje temeljne težave

Jezikovni modeli so brezstanja. Vsak API klic je neodvisen. Če pošljete "Ime mi je John" in nato vprašate "Kako mi je ime?", model nima pojma, da ste se pravkar predstavili. Vsak zahtevek obravnava kot prvi pogovor, ki ste ga imeli.

To je v redu za preprosta vprašanja in odgovore, a brez vrednosti za resne aplikacije. Bots za podporo strankam morajo vedeti, kaj ste jim povedali. Osebni asistenti potrebujejo kontekst. Vsak večzankovni pogovor zahteva pomnilnik.

<img src="../../../translated_images/sl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Razlika med brezstanjskimi (neodvisnimi klici) in s stanjem (zavednimi konteksta) pogovori*

## Razumevanje tokenov

Preden se poglobite v pogovore, je pomembno razumeti tokene - osnovne enote besedila, ki jih jezikovni modeli obdelujejo:

<img src="../../../translated_images/sl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Primer, kako je besedilo razdeljeno v tokene - "I love AI!" postane 4 ločene procesne enote*

Tokeni so način, kako AI modeli merijo in obdelujejo besedilo. Besede, ločila, celo presledki so lahko tokeni. Vaš model ima omejitev, koliko tokenov lahko obdeluje naenkrat (400.000 za GPT-5.2, z do 272.000 vhodnimi in 128.000 izhodnimi tokeni). Razumevanje tokenov pomaga upravljati dolžino pogovora in stroške.

## Kako deluje pomnilnik

Klepetalni pomnilnik rešuje problem brezstanja z ohranjanjem zgodovine pogovora. Preden pošljete zahtevek modelu, okvir doda ustrezna prejšnja sporočila. Ko vprašate "Kako mi je ime?", sistem dejansko pošlje celotno zgodovino pogovora, kar modelu omogoči, da vidi, da ste prej rekli "Ime mi je John."

LangChain4j ponuja izvedbe pomnilnika, ki avtomatsko upravljajo to. Izberete, koliko sporočil želite obdržati, okvir pa upravlja z oknom konteksta.

<img src="../../../translated_images/sl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ohranja drsno okno nedavnih sporočil in samodejno odstranjuje starejša*

## Kako to uporablja LangChain4j

Ta modul razširja hitri začetek z integracijo Spring Boota in dodajanjem pomnilnika za pogovor. Tukaj je, kako deli skupaj delujejo:

**Odvisnosti** - Dodajte dve knjižnici LangChain4j:

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

**Model klepeta** - Konfigurirajte Azure OpenAI kot Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Graditelj bere poverilnice iz okolijskih spremenljivk, nastavljenih z `azd up`. Nastavitev `baseUrl` na vaš Azure končni naslov omogoča, da odjemalec OpenAI deluje z Azure OpenAI.

**Pomnilnik pogovorov** - Spremljanje zgodovine klepeta z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Ustvarite pomnilnik z `withMaxMessages(10)`, da obdržite zadnjih 10 sporočil. Dodajte uporabniška in AI sporočila s tipiziranimi ovojniki: `UserMessage.from(text)` in `AiMessage.from(text)`. Pridobite zgodovino z `memory.messages()` in jo pošljite modelu. Storitev shrani ločene instance pomnilnika za vsako ID pogovora, kar omogoča več uporabnikom hkratni klepet.

> **🤖 Preizkusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) in vprašajte:
> - "Kako MessageWindowChatMemory odloča, katera sporočila zavre, ko je okno polno?"
> - "Ali lahko izvedem prilagojeno shranjevanje pomnilnika z uporabo baze podatkov namesto v pomnilniku?"
> - "Kako bi dodal povzemanje za stiskanje stare zgodovine pogovora?"

Brezstanjskemu klepetalnemu končnemu točki pomnilnik v celoti preskoči - samo `chatModel.chat(prompt)` kot v hitrem začetku. S stanjem končni točki dodata sporočila v pomnilnik, pridobita zgodovino in vključita ta kontekst pri vsakem zahtevku. Enaka konfiguracija modela, različni vzorci.

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

> **Opomba:** Če naletite na napako časovne omejitve (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), preprosto ponovno zaženite `azd up`. Azure viri so morda še vedno v pripravi v ozadju, ponovno poskus omogoča, da namestitev dokonča, ko viri dosežejo končno stanje.

To bo:
1. Namestilo Azure OpenAI vir z GPT-5.2 in modeli text-embedding-3-small
2. Samodejno ustvarilo `.env` datoteko v korenu projekta z poverilnicami
3. Nastavilo vse potrebne okoljske spremenljivke

**Imate težave z namestitvijo?** Oglejte si [README infrastrukture](infra/README.md) za podrobno odpravljanje težav, vključno s konfliktnimi imeni poddomen, ročnimi koraki namestitve v Azure Portalu in smernicami za konfiguracijo modela.

**Preverite, da je namestitev uspela:**

**Bash:**
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Prikazati mora AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

> **Opomba:** Ukaz `azd up` samodejno ustvari `.env` datoteko. Če jo morate kasneje posodobiti, jo lahko uredite ročno ali ponovno ustvarite s zagonom:
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

Prepričajte se, da `.env` datoteka obstaja v korenskem imeniku z Azure poverilnicami:

**Bash:**
```bash
cat ../.env  # Naj prikaže AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj pokaže AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacije:**

**Možnost 1: Uporaba Spring Boot Dashboard (priporočeno za uporabnike VS Code)**

Dev container vključuje razširitev Spring Boot Dashboard, ki ponuja vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v Activity Bar na levi strani VS Code (poiščite ikono Spring Boot).

Iz Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V živo spremljate dnevnike aplikacije
- Spremljate stanje aplikacije

Preprosto kliknite gumb za predvajanje zraven "introduction" za začetek tega modula ali zaženite vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba skript za terminal**

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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in zgradita JAR-je, če še ne obstajajo.

> **Opomba:** Če želite vse module ročno zgraditi pred zagonom:
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

Odprite http://localhost:8080 v svojem brskalniku.

**Za zaustavitev:**

**Bash:**
```bash
./stop.sh  # Samo ta modul
# Ali
cd .. && ./stop-all.sh  # Vsi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ta modul
# Ali
cd ..; .\stop-all.ps1  # Vsi moduli
```

## Uporaba aplikacije

Aplikacija ponuja spletni vmesnik z dvema implementacijama klepetalnega okna ena ob drugi.

<img src="../../../translated_images/sl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Nadzorna plošča prikazuje obe možnosti: SimpIen klepet (brez stanja) in Pogovorni klepet (s stanjem)*

### Klepet brez stanja (levo okno)

Najprej preizkusite to. Vprašajte "Ime mi je John" in nato takoj "Kako mi je ime?" Model ne bo zapomnil, ker je vsako sporočilo neodvisno. To ponazarja temeljno težavo z osnovno integracijo jezikovnega modela - brez konteksta pogovora.

<img src="../../../translated_images/sl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ne pomni vašega imena iz prejšnjega sporočila*

### Klepet s stanjem (desno okno)

Zdaj preizkusite enak zaporedje tukaj. Vprašajte "Ime mi je John" in nato "Kako mi je ime?" Tokrat si zapomni. Razlika je v MessageWindowChatMemory - ohranja zgodovino pogovora in jo vključuje pri vsakem zahtevku. Tako deluje produkcijski pogovorni AI.

<img src="../../../translated_images/sl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si zapomni vaše ime iz zgodnejšega pogovora*

Oba okna uporabljata isti GPT-5.2 model. Edina razlika je pomnilnik. To jasno pokaže, kaj pomnilnik prinaša vaši aplikaciji in zakaj je bistven za resnične primere uporabe.

## Nadaljnji koraki

**Naslednji modul:** [02-prompt-engineering - Oblikovanje pozivov z GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prejšnji: Modul 00 - Hitri začetek](../00-quick-start/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 02 - Oblikovanje pozivov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Opozorilo**:
Ta dokument je bil preveden z uporabo storitve za avtomatski prevod AI [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvorni jezik velja za avtoritativni vir. Za kritične informacije priporočamo strokovni človeški prevod. Nismo odgovorni za kakršna koli nerazumevanja ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->