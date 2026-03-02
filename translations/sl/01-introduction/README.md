# Modul 01: Začetek z LangChain4j

## Kazalo

- [Video prikaz](../../../01-introduction)
- [Kaj boste izvedeli](../../../01-introduction)
- [Pogoj za začetek](../../../01-introduction)
- [Razumevanje osnovnega problema](../../../01-introduction)
- [Razumevanje tokenov](../../../01-introduction)
- [Kako deluje pomnilnik](../../../01-introduction)
- [Kako to uporablja LangChain4j](../../../01-introduction)
- [Postavitev Azure OpenAI infrastrukture](../../../01-introduction)
- [Zagon aplikacije lokalno](../../../01-introduction)
- [Uporaba aplikacije](../../../01-introduction)
  - [Brezstanje pogovora (levi panel)](../../../01-introduction)
  - [Stanjevni pogovor (desni panel)](../../../01-introduction)
- [Naslednji koraki](../../../01-introduction)

## Video prikaz

Oglejte si to v živo predavanje, ki pojasnjuje, kako začeti z modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Začetek z LangChain4j - V živo" width="800"/></a>

## Kaj boste izvedeli

Če ste zaključili hitri začetek, ste videli, kako poslati pozive in dobiti odgovore. To je osnova, vendar resne aplikacije potrebujejo več. Ta modul vas uči, kako graditi pogovorno AI, ki si zapomni kontekst in ohranja stanje - razlika med enkratnim prikazom in aplikacijo, pripravljeno za produkcijo.

V tem vodiču bomo uporabljali Azure OpenAI GPT-5.2, ker njegove napredne sposobnosti razmišljanja naredijo vedenje različnih vzorcev bolj očitno. Ko dodate pomnilnik, boste jasno videli razliko. To olajša razumevanje, kaj posamezna komponenta prinaša vaši aplikaciji.

Zgradili boste eno aplikacijo, ki prikazuje oba vzorca:

**Brezstanje pogovora** - Vsak zahtevek je neodvisen. Model nima spomina prejšnjih sporočil. To je vzorec, ki ste ga uporabili v hitrem začetku.

**Stanjevni pogovor** - Vsak zahtevek vključuje zgodovino pogovora. Model ohranja kontekst skozi več zank. To zahtevajo produkcijske aplikacije.

## Pogoj za začetek

- Azure naročnina z dostopom do Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opomba:** Java, Maven, Azure CLI in Azure Developer CLI (azd) so prednameščeni v priloženem devcontainerju.

> **Opomba:** Ta modul uporablja GPT-5.2 na Azure OpenAI. Namestitev je samodejno konfigurirana preko `azd up` - ne spreminjajte imena modela v kodi.

## Razumevanje osnovnega problema

Jezični modeli so brezstanje. Vsak API klic je neodvisen. Če pošljete "Moje ime je John" in nato vprašate "Kako je moje ime?", model nima pojma, da ste se pravkar predstavili. Vsak zahtevek obravnava, kot da je prvi pogovor, ki ste ga kdaj imeli.

To je v redu za preprosta vprašanja in odgovore, vendar neuporabno za resne aplikacije. Botom za podporo uporabnikom je treba zapomniti, kaj ste jim povedali. Osebni pomočniki potrebujejo kontekst. Vsak večkratni pogovor zahteva pomnilnik.

<img src="../../../translated_images/sl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Brezstanje proti stanjevnim pogovorom" width="800"/>

*Razlika med brezstanje (neodvisni klici) in stanjevnim (ozaveščen o kontekstu) pogovorom*

## Razumevanje tokenov

Preden se poglobite v pogovore, je pomembno razumeti tokene - osnovne enote besedila, ki jih procesirajo jezikovni modeli:

<img src="../../../translated_images/sl/token-explanation.c39760d8ec650181.webp" alt="Razlaga tokenov" width="800"/>

*Primer, kako je besedilo razdeljeno na tokene - "I love AI!" postane 4 ločene enote za obdelavo*

Tokeni so način, kako AI modeli merijo in procesirajo besedilo. Besede, ločila in celo presledki so lahko tokeni. Vaš model ima omejitev, koliko tokenov lahko procesuira hkrati (400.000 za GPT-5.2, z do 272.000 vhodnih in 128.000 izhodnih tokenov). Razumevanje tokenov vam pomaga upravljati dolžino pogovora in stroške.

## Kako deluje pomnilnik

Pogovorni pomnilnik rešuje problem brezstanja z ohranjanjem zgodovine pogovora. Preden pošljete zahtevek modelu, okvir doda ustrezna prejšnja sporočila. Ko vprašate "Kako je moje ime?", sistem v resnici pošlje celotno zgodovino pogovora, kar modelu omogoči, da vidi, da ste prej rekli "Moje ime je John."

LangChain4j ponuja implementacije pomnilnika, ki to obvladujejo samodejno. Izberete, koliko sporočil želite zadržati, okvir pa upravlja kontekstno okno.

<img src="../../../translated_images/sl/memory-window.bbe67f597eadabb3.webp" alt="Koncept pomnilniškega okna" width="800"/>

*MessageWindowChatMemory ohranja drsno okno nedavnih sporočil in samodejno odstranjuje stare*

## Kako to uporablja LangChain4j

Ta modul nadgrajuje hitri začetek z integracijo Spring Boot in dodajanjem pogovornega pomnilnika. Tako se sestavine povežejo:

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

**Pogovorni model** - Konfigurirajte Azure OpenAI kot Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Graditelj bere poverilnice iz okoljskih spremenljivk, ki jih nastavi `azd up`. Nastavitev `baseUrl` na vaš Azure končni naslov omogoča OpenAI klientu delo z Azure OpenAI.

**Pogovorni pomnilnik** - Sledite zgodovini pogovora z MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Ustvarite pomnilnik z `withMaxMessages(10)`, da ohranite zadnjih 10 sporočil. Dodajte uporabniška in AI sporočila s tipiziranimi zavitki: `UserMessage.from(text)` in `AiMessage.from(text)`. Zgodovino pridobite z `memory.messages()` in jo pošljite modelu. Storitev hrani ločene pomnilnike za vsak ID pogovora, kar omogoča več uporabnikom hkratni pogovor.

> **🤖 Preizkusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) in vprašajte:
> - "Kako MessageWindowChatMemory odloči, katera sporočila izpustiti, ko je okno polno?"
> - "Lahko implementiram lasten pomnilnik z uporabo baze podatkov namesto pomnilnika v RAM?"
> - "Kako bi dodal povzetek za stiskanje stare zgodovine pogovora?"

Vhodi brezstanje pogovora popolnoma preskočijo pomnilnik - samo `chatModel.chat(prompt)` kot v hitrem začetku. Stanjevni endpoint doda sporočila v pomnilnik, pridobi zgodovino in vključi ta kontekst pri vsakem zahtevku. Enaka konfiguracija modela, različni vzorci.

## Postavitev Azure OpenAI infrastrukture

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

> **Opomba:** Če naletite na timeout napako (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), preprosto ponovno zaženite `azd up`. Azure viri so morda še vedno v postopku priprave v ozadju, ponovni poskus pa omogoči dokončanje namestitve, ko viri dosežejo končno stanje.

To bo:
1. Namestilo Azure OpenAI vir z modeli GPT-5.2 in text-embedding-3-small
2. Samodejno ustvarilo `.env` datoteko v korenu projekta z poverilnicami
3. Nastavilo vse potrebne okoljske spremenljivke

**Se pojavljajo težave z namestitvijo?** Oglejte si [README za infrastrukturo](infra/README.md) za podrobna navodila za odpravljanje težav, vključno s konfliktnimi imeni poddomen, ročnim nameščanjem v Azure Portal in navodili za konfiguracijo modela.

**Preverite uspešnost namestitve:**

**Bash:**
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Naj prikaže AZURE_OPENAI_ENDPOINT, API_KEY itd.
```

> **Opomba:** Ukaz `azd up` samodejno ustvari `.env` datoteko. Če jo boste kasneje morali posodobiti, jo lahko ročno uredite ali ponovno ustvarite z zagonom:
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

Prepričajte se, da `.env` datoteka obstaja v korenski mapi z Azure poverilnicami:

**Bash:**
```bash
cat ../.env  # Prikazati mora AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Prikazati bi moralo AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacije:**

**Možnost 1: Uporaba Spring Boot Dashboarda (priporočeno za uporabnike VS Code)**

Dev container vključuje razširitev Spring Boot Dashboard, ki nudi vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v vrstici z dejavnostmi na levi strani v VS Code (ikona Spring Boot).

Iz Spring Boot Dashboarda lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zaženete/ustavite aplikacije z enim klikom
- V realnem času si ogledate dnevnike aplikacij
- Spremljate status aplikacij

Preprosto kliknite gumb za predvajanje poleg "introduction" za zagon tega modula ali zaženite vse module hkrati.

<img src="../../../translated_images/sl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba shell skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenske mape
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenskega imenika
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

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in zgradita JAR datoteke, če ne obstajajo.

> **Opomba:** Če želite vse module najprej ročno zgraditi pred zagonom:
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

Aplikacija nudi spletni vmesnik z dvema implementacijama klepeta ena ob drugi.

<img src="../../../translated_images/sl/home-screen.121a03206ab910c0.webp" alt="Glavni zaslon aplikacije" width="800"/>

*Nadzorna plošča prikazuje možnosti Simple Chat (brezstanje) in Conversational Chat (stanjevni)*

### Brezstanje pogovora (levi panel)

Najprej poskusite to. Vprašajte "Moje ime je John", nato takoj vprašajte "Kako je moje ime?" Model si ne bo zapomnil, ker je vsako sporočilo neodvisno. To prikazuje osnovni problem z integracijo jezikovnih modelov – brez konteksta pogovora.

<img src="../../../translated_images/sl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demonstracija brezstanje klepeta" width="800"/>

*AI si ne zapomni vašega imena iz prejšnjega sporočila*

### Stanjevni pogovor (desni panel)

Zdaj poskusite isti vrstni red tukaj. Vprašajte "Moje ime je John" in nato "Kako je moje ime?" Tokrat se spomni. Razlika je MessageWindowChatMemory - ohranja zgodovino pogovora in jo vključuje pri vsakem zahtevku. Tako deluje produkcijski pogovorni AI.

<img src="../../../translated_images/sl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demonstracija stanjevnega klepeta" width="800"/>

*AI si zapomni vaše ime iz prejšnjega pogovora*

Oba panela uporabljata isti model GPT-5.2. Edina razlika je pomnilnik. To jasno pokaže, kaj pomnilnik prinaša vaši aplikaciji in zakaj je nujen za resnične primere uporabe.

## Naslednji koraki

**Naslednji modul:** [02-prompt-engineering - Oblikovanje pozivov z GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prejšnji: Modul 00 - Hiter začetek](../00-quick-start/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 02 - Oblikovanje pozivov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Opozorilo**:  
Ta dokument je bil preveden z uporabo storitve za samodejni prevod [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko samodejni prevodi vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jezik velja za verodostojen vir. Za ključne informacije priporočamo strokovni človeški prevod. Za morebitna nesporazume ali napačne interpretacije, ki nastanejo zaradi uporabe tega prevoda, ne prevzemamo odgovornosti.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->