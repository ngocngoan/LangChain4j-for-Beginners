# Modul 01: Začetek z LangChain4j

## Kazalo vsebine

- [Video predstavitev](../../../01-introduction)
- [Kaj se boste naučili](../../../01-introduction)
- [Predpogoji](../../../01-introduction)
- [Razumevanje osnovnega problema](../../../01-introduction)
- [Razumevanje tokenov](../../../01-introduction)
- [Kako deluje pomnilnik](../../../01-introduction)
- [Kako to uporablja LangChain4j](../../../01-introduction)
- [Implementacija infrastrukture Azure OpenAI](../../../01-introduction)
- [Zagon aplikacije lokalno](../../../01-introduction)
- [Uporaba aplikacije](../../../01-introduction)
  - [Brezstaten klepet (levo okno)](../../../01-introduction)
  - [Staten klepet (desno okno)](../../../01-introduction)
- [Naslednji koraki](../../../01-introduction)

## Video predstavitev

Oglejte si to v živo posneto sejo, ki pojasnjuje, kako začeti z modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Začetek z LangChain4j - V živo seja" width="800"/></a>

## Kaj se boste naučili

V hitrem začetku ste uporabili GitHub modele za pošiljanje pozivov, klic orodij, zgradnjo RAG cevovoda in testiranje varovanih mehanizmov. Te predstavitve so pokazale, kaj je mogoče — zdaj preklopimo na Azure OpenAI in GPT-5.2 ter začnemo graditi aplikacije v produkcijskem slogu. Ta modul se osredotoča na pogovorno AI, ki si zapomni kontekst in ohranja stanje — koncepti, ki so jih uporabili hitri začetni primeri, a niso razloženi.

V celotnem vodniku bomo uporabljali Azure OpenAI GPT-5.2, saj njegove napredne zmožnosti sklepanja naredijo vedenje različnih vzorcev jasneje vidno. Ko dodate pomnilnik, boste jasno videli razliko. To olajša razumevanje, kaj posamezna komponenta prinaša vaši aplikaciji.

Zgradili boste eno aplikacijo, ki prikazuje oba vzorca:

**Brezstaten klepet** – Vsak zahtevek je neodvisen. Model nima pomnilnika prejšnjih sporočil. To je vzorec, ki ste ga uporabili v hitrem začetku.

**Staten pogovor** – Vsak zahtevek vključuje zgodovino pogovora. Model ohranja kontekst skozi več krogov. To je tisto, kar zahtevajo produkcijske aplikacije.

## Predpogoji

- Azure naročnina z dostopom do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opomba:** Java, Maven, Azure CLI in Azure Developer CLI (azd) so v predhodno nameščenem devcontainerju.

> **Opomba:** Ta modul uporablja GPT-5.2 na Azure OpenAI. Implementacija je samodejno konfigurirana preko `azd up` - ne spreminjajte imena modela v kodi.

## Razumevanje osnovnega problema

Jezikovni modeli so brezstatični. Vsak API klic je neodvisen. Če pošljete "Moje ime je John" in nato vprašate "Kako je moje ime?", model nima pojma, da ste se pravkar predstavili. Vsak zahtevek obravnava, kot da je prvi pogovor, ki ste ga kdaj imeli.

To je v redu za preprosta vprašanja in odgovore, a nepomembno za resne aplikacije. Pomočniki v storitvah za stranke morajo si zapomniti, kaj ste jim povedali. Osebni asistenti potrebujejo kontekst. Vsak večkrogovni pogovor zahteva pomnilnik.

Naslednja shema primerja oba pristopa — na levi brezstaten klic, ki pozabi vaše ime; na desni staten klic s ChatMemory, ki si ga zapomni.

<img src="../../../translated_images/sl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Brezstatični proti statičnim pogovorom" width="800"/>

*Razlika med brezstatičnimi (neodvisnimi klici) in statičnimi (zavedajočimi se konteksta) pogovori*

## Razumevanje tokenov

Preden se poglobite v pogovore, je pomembno razumeti tokene – osnovne enote besedila, ki jih procesirajo jezikovni modeli:

<img src="../../../translated_images/sl/token-explanation.c39760d8ec650181.webp" alt="Razlaga tokenov" width="800"/>

*Primer, kako se besedilo razdeli na tokene – "I love AI!" postane 4 ločene obdelovalne enote*

Tokeni so način, na katerega AI modeli merijo in obdelujejo besedilo. Besede, ločila in celo presledki so lahko tokeni. Vaš model ima omejitev koliko tokenov lahko obdela hkrati (400.000 za GPT-5.2, z do 272.000 vhodnimi in 128.000 izhodnimi tokeni). Razumevanje tokenov pomaga upravljati dolžino pogovora in stroške.

## Kako deluje pomnilnik

Pogovorni pomnilnik rešuje problem brezstatnosti tako, da ohranja zgodovino pogovorov. Preden pošljete zahtevek modelu, okvir doda ustrezna prejšnja sporočila. Ko vprašate "Kako je moje ime?", sistem dejansko pošlje celotno zgodovino pogovora, kar modelu omogoča, da vidi, da ste predhodno rekli "Moje ime je John."

LangChain4j ponuja implementacije pomnilnika, ki to upravljajo samodejno. Izberete koliko sporočil shraniti, okvir pa upravlja okno konteksta. Spodnja shema prikazuje, kako MessageWindowChatMemory ohranja drseče okno nedavnih sporočil.

<img src="../../../translated_images/sl/memory-window.bbe67f597eadabb3.webp" alt="Koncept pomnilniškega okna" width="800"/>

*MessageWindowChatMemory ohranja drseče okno nedavnih sporočil in samodejno odstranjuje stare*

## Kako to uporablja LangChain4j

Ta modul nadgradi hiter začetek z integracijo Spring Boot in dodajanjem pomnilnika pogovora. Tako se deli povežejo:

**Odvisnosti** – Dodajte dve LangChain4j knjižnici:

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

Builder prebere poverilnice iz okoljskih spremenljivk, nastavljenih z `azd up`. Nastavitev `baseUrl` na vašo Azure točko konca omogoča delovanje OpenAI odjemalca z Azure OpenAI.

**Pomnilnik pogovora** – Sledite zgodovini klepeta z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Ustvarite pomnilnik z `withMaxMessages(10)` za hranjenje zadnjih 10 sporočil. Dodajte uporabniška in AI sporočila z ohišji tipa: `UserMessage.from(text)` in `AiMessage.from(text)`. Z zgodovino dostopajte z `memory.messages()` in jo pošljite modelu. Storitev hrani ločene instance pomnilnika za vsak ID pogovora, kar omogoča hkratno klepetanje več uporabnikov.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) in vprašajte:
> - "Kako MessageWindowChatMemory odloči, katera sporočila bo odstranil, ko je okno polno?"
> - "Ali lahko implementiram lastno shrambo pomnilnika z uporabo baze podatkov namesto v pomnilniku?"
> - "Kako bi dodal povzetek za stiskanje stare zgodovine pogovora?"

Brezstatični klepetni endpoint popolnoma preide preko pomnilnika - samo `chatModel.chat(prompt)` kot v hitrem začetku. Staten endpoint doda sporočila v pomnilnik, pridobi zgodovino in vključuje ta kontekst z vsako zahtevo. Enaka konfiguracija modela, različni vzorci.

## Implementacija infrastrukture Azure OpenAI

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

> **Opomba:** Če naletite na napako poteka časa (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), preprosto zaženite `azd up` znova. Azure viri so morda še vedno v postopku provisioniranja v ozadju, zato ponovni poskus omogoči dokončanje implementacije, ko viri dosežejo končno stanje.

To bo:
1. Namestilo Azure OpenAI sredstvo z GPT-5.2 in modeli text-embedding-3-small
2. Samodejno ustvarilo `.env` datoteko v korenu projekta s poverilnicami
3. Nastavilo vse potrebne okoljske spremenljivke

**Imate težave z implementacijo?** Oglejte si [Infrastructure README](infra/README.md) za podrobno odpravljanje težav, vključno s konflikti imen poddomen, ročno implementacijo v Azure Portal in navodili za konfiguracijo modela.

**Preverite uspešnost implementacije:**

**Bash:**
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj prikaže AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

> **Opomba:** Ukaz `azd up` samodejno ustvari `.env` datoteko. Če jo želite kasneje posodobiti, jo lahko ročno uredite ali ponovno ustvarite z izvajanjem:
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

**Preverite implementacijo:**

Prepričajte se, da `.env` datoteka obstaja v korenski mapi z Azure poverilnicami. Zaženite to v mapi modula (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Prikazati bi moralo AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacije:**

**Možnost 1: Uporaba Spring Boot nadzorne plošče (priporočeno za uporabnike VS Code)**

Dev container vključuje razširitev Spring Boot Dashboard, ki omogoča vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v Aktivnostni vrstici na levi strani VS Code (ikona Spring Boot).

Iz Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- Ogledate dnevniške datoteke aplikacij v realnem času
- Spremljate stanje aplikacije

Preprosto kliknite gumb za predvajanje ob "introduction" za zagon tega modula ali zaženite vse module skupaj.

<img src="../../../translated_images/sl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot nadzorna plošča" width="400"/>

*Spring Boot nadzorna plošča v VS Code — začnite, ustavite in spremljajte vse module iz enega mesta*

**Možnost 2: Uporaba shell skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenskega imenika
.\start-all.ps1
```

Ali zaženite zgolj ta modul:

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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in zgradita JAR datoteke, če ne obstajajo.

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

Odprite http://localhost:8080 v vašem brskalniku.

**Za ustavitev:**

**Bash:**
```bash
./stop.sh  # Ta modul samo
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

Aplikacija zagotavlja spletni vmesnik z dvema klepetalnima implementacijama ena ob drugi.

<img src="../../../translated_images/sl/home-screen.121a03206ab910c0.webp" alt="Glavni zaslon aplikacije" width="800"/>

*Nadzorna plošča prikazuje obe možnosti: Preprost klepet (brezstaten) in Pogovorni klepet (staten)*

### Brezstaten klepet (levo okno)

Preizkusite najprej to. Vprašajte "Moje ime je John" in nato takoj "Kako je moje ime?" Model se ne bo spomnil, ker sta sporočili neodvisni. To prikazuje osnovni problem integracije jezikovnega modela - ni konteksta pogovora.

<img src="../../../translated_images/sl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo brezstatnega klepeta" width="800"/>

*AI se ne spomni vašega imena iz prejšnjega sporočila*

### Staten klepet (desno okno)

Zdaj poskusite isti postopek tukaj. Vprašajte "Moje ime je John" in nato "Kako je moje ime?" Tokrat se spomni. Razlika je MessageWindowChatMemory - ohranja zgodovino pogovora in jo vključuje v vsak zahtevek. Tako deluje produkcijska pogovorna AI.

<img src="../../../translated_images/sl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo statnega klepeta" width="800"/>

*AI se spomni vašega imena iz večjega dela pogovora*

Oba okna uporabljata isti GPT-5.2 model. Edina razlika je pomnilnik. To jasno pokaže, kaj pomnilnik prinaša vaši aplikaciji in zakaj je ključnega pomena za resnične primere uporabe.

## Naslednji koraki

**Naslednji modul:** [02-prompt-engineering - Oblikovanje pozivov z GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prejšnji: Modul 00 - Hiter začetek](../00-quick-start/README.md) | [Nazaj na glavno](../README.md) | [Naprej: Modul 02 - Oblikovanje pozivov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jeziku velja za avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Nismo odgovorni za morebitne nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->