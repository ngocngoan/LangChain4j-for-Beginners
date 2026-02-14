# Modul 02: Inženiring pozivov z GPT-5.2

## Kazalo

- [Kaj se boste naučili](../../../02-prompt-engineering)
- [Pogoji za začetek](../../../02-prompt-engineering)
- [Razumevanje inženiringa pozivov](../../../02-prompt-engineering)
- [Kako to uporablja LangChain4j](../../../02-prompt-engineering)
- [Glavni vzorci](../../../02-prompt-engineering)
- [Uporaba obstoječih Azure virov](../../../02-prompt-engineering)
- [Posnetki zaslona aplikacije](../../../02-prompt-engineering)
- [Raziščite vzorce](../../../02-prompt-engineering)
  - [Nizka proti visoki pripravljenosti](../../../02-prompt-engineering)
  - [Izvajanje nalog (uvodi orodij)](../../../02-prompt-engineering)
  - [Samoreflektirajoča koda](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Večkrožni pogovor](../../../02-prompt-engineering)
  - [Korak za korakom razmišljanje](../../../02-prompt-engineering)
  - [Omejen izhod](../../../02-prompt-engineering)
- [Kaj se v resnici učite](../../../02-prompt-engineering)
- [Naslednji koraki](../../../02-prompt-engineering)

## Kaj se boste naučili

V prejšnjem modulu ste videli, kako pomnilnik omogoča konverzacijski AI in uporabljali modele GitHub za osnovne interakcije. Zdaj se bomo osredotočili na to, kako postavljate vprašanja - torej same pozive - z uporabo Azure OpenAI GPT-5.2. Način, kako strukturirate svoje pozive, močno vpliva na kakovost odgovorov, ki jih prejmete.

Uporabljali bomo GPT-5.2, ker uvaja nadzor razmišljanja - lahko modelu poveste, koliko premisleka naj opravi pred odgovorom. To naredi različne strategije pozivanja bolj jasne in vam pomaga razumeti, kdaj uporabiti posamezen pristop. Prav tako bomo izkoristili manj omejitev hitrosti pri GPT-5.2 v Azure v primerjavi z GitHub modeli.

## Pogoji za začetek

- Dokončan Modul 01 (deployed Azure OpenAI viri)
- Datoteka `.env` v korenski mapi z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če niste dokončali Modula 01, najprej sledite navodilom za namestitev tam.

## Razumevanje inženiringa pozivov

Inženiring pozivov pomeni oblikovanje vhodnega besedila, ki vam dosledno prinaša želene rezultate. Ne gre zgolj za postavljanje vprašanj - gre za strukturiranje zahtev, da model natančno razume, kaj želite in kako to dostaviti.

Pomislite nanj kot na dajanje navodil sodelavcu. "Popravi napako" je nejasno. "Popravi napako null pointer exception v UserService.java na vrstici 45 z dodajanjem preverjanja na null" je specifično. Jezikovni modeli delujejo enako - pomembna sta specifičnost in struktura.

## Kako to uporablja LangChain4j

Ta modul prikazuje napredne vzorce pozivanja z uporabo iste osnove LangChain4j kot prejšnji moduli, s poudarkom na strukturi pozivov in nadzoru razmišljanja.

<img src="../../../translated_images/sl/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Kako LangChain4j povezuje vaše pozive z Azure OpenAI GPT-5.2*

**Odvisnosti** – Modul 02 uporablja naslednje odvisnosti langchain4j, definirane v `pom.xml`:
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

**Konfiguracija OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) 

Model klepeta je ročno konfiguriran kot Spring bean z uporabo uradnega OpenAI klienta, ki podpira Azure OpenAI končne točke. Ključna razlika od Modula 01 je, kako strukturiramo pozive, ki jih pošljemo `chatModel.chat()`, ne pa nastavitev modela samega.

**Sistemska in uporabniška sporočila** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j loči vrste sporočil za jasnost. `SystemMessage` nastavi vedenje in kontekst AI-ja (npr. "Si pregledovalec kode"), medtem ko `UserMessage` vsebuje dejansko zahtevo. Ta ločitev omogoča ohranjanje doslednega vedenja AI-ja pri različnih uporabniških poizvedbah.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/sl/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage zagotavlja trajen kontekst, medtem ko UserMessages vsebujejo posamezne zahteve*

**MessageWindowChatMemory za večkrožne pogovore** – Za vzorec večkrožnega pogovora ponovno uporabimo `MessageWindowChatMemory` iz Modula 01. Vsaka seja dobi svojo instanco pomnilnika, shranjeno v `Map<String, ChatMemory>`, kar omogoča več sočasnih pogovorov brez mešanja kontekstov.

**Predloge pozivov** – Resnični poudarek je tukaj na inženiringu pozivov, ne na novih LangChain4j API-jih. Vsak vzorec (nizka pripravljenost, visoka pripravljenost, izvajanje nalog itd.) uporablja isto metodo `chatModel.chat(prompt)` z natančno strukturiranimi pozivnimi nizi. XML oznake, navodila in oblikovanje so vsi del besedila poziva, ne LangChain4j funkcij.

**Nadzor razmišljanja** – Razmišljanje GPT-5.2 nadzorujemo prek navodil v pozivih, kot so "največ 2 koraka razmišljanja" ali "temeljito razišči". To so tehnike inženiringa pozivov, ne konfiguracije LangChain4j. Knjižnica le dostavi vaše pozive modelu.

Ključna spoznanja: LangChain4j zagotavlja infrastrukturo (povezavo do modela preko [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), pomnilnik, upravljanje sporočil preko [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), ta modul pa vas uči, kako v tej infrastrukturi oblikovati učinkovite pozive.

## Glavni vzorci

Ne vsa vprašanja zahtevajo isti pristop. Nekatera potrebujejo hitre odgovore, druga globoko premišljevanje. Nekatera zahtevajo vidno razmišljanje, druga samo rezultate. Ta modul pokriva osem vzorcev pozivanja – vsakega optimiziranega za drugačne scenarije. Preizkusili jih boste vse, da boste razumeli, kdaj kateri pristop deluje najbolje.

<img src="../../../translated_images/sl/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pregled osmih vzorcev inženiringa pozivov ter njihovih primerov uporabe*

<img src="../../../translated_images/sl/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Nizka pripravljenost (hitro, neposredno) proti visoki pripravljenosti (temeljito, raziskovalno) pri razmišljanju*

**Nizka pripravljenost (hitro in osredotočeno)** – Za preprosta vprašanja, kjer želite hitre in neposredne odgovore. Model opravi minimalno razmišljanje – največ 2 koraka. Uporabite to za izračune, poizvedbe ali preprosta vprašanja.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Preizkusi z GitHub Copilot:** Odpri [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) in vprašaj:
> - "Kakšna je razlika med nizko in visoko pripravljenostjo pri pozivih?"
> - "Kako XML oznake v pozivih pomagajo strukturirati AI-jeve odgovore?"
> - "Kdaj naj uporabljam samoreflektirajoče vzorce in kdaj neposredna navodila?"

**Visoka pripravljenost (globoko in temeljito)** – Za zahtevne probleme, kjer želite celovito analizo. Model temeljito raziskuje in prikazuje podrobno razmišljanje. Uporabite to za načrtovanje sistemov, arhitekturne odločitve ali zahtevne raziskave.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvajanje nalog (napredek korak za korakom)** – Za večkorakovne delovne procese. Model najprej poda načrt, nato pripoveduje o vsakem koraku med izvajanjem in na koncu povzame. Uporabite to za migracije, implementacije ali večkorakovne postopke.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Pozivanje "Chain-of-Thought" izrecno zahteva modelu, naj pokaže postopek razmišljanja, s čimer izboljša natančnost pri zapletenih nalogah. Razčlenitev korak za korakom pomaga tako ljudem kot AI-ju razumeti logiko.

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) Chat:** Povprašaj o tem vzorcu:
> - "Kako bi prilagodil vzorec izvajanja nalog za dolgotrajne operacije?"
> - "Kakšne so najboljše prakse za strukturiranje uvodov orodij v proizvodnih aplikacijah?"
> - "Kako zajeti in prikazati vmesne napredke uporabniku v UI?"

<img src="../../../translated_images/sl/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Načrtuj → Izvedi → Povzemi potek dela za večkoradne naloge*

**Samoreflektirajoča koda** – Za generiranje kode proizvodne kakovosti. Model generira kodo, preverja njeno kvaliteto in jo iterativno izboljšuje. Uporabite to pri gradnji novih funkcij ali storitev.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativni cikel izboljšav – generiraj, ocenjuj, identificiraj težave, izboljšuj, ponavljaj*

**Strukturirana analiza** – Za konsistentno ocenjevanje. Model pregleda kodo z uporabo fiksnega okvira (pravilnost, prakse, zmogljivost, varnost). Uporabite to za preglede kode ali ocenjevanje kakovosti.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) Chat:** Vprašaj o strukturirani analizi:
> - "Kako prilagoditi okvir analize za različne vrste pregledov kode?"
> - "Kako programatsko obdelati in ukrepati po strukturiranem izhodu?"
> - "Kako zagotoviti dosledne ravni resnosti skozi različne pregledne seje?"

<img src="../../../translated_images/sl/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Okvir s štirimi kategorijami za dosledne preglede kode z ravnmi resnosti*

**Večkrožni pogovor** – Za pogovore, ki potrebujejo kontekst. Model si zapomni prejšnja sporočila in gradi nanje. Uporabite to za interaktivno pomoč ali zahtevna vprašanja in odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/sl/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kako se kontekst pogovora nabira skozi več vlog, dokler ne doseže omejitve tokenov*

**Korak za korakom razmišljanje** – Za probleme, kjer je potrebna vidna logika. Model prikazuje izrecno razmišljanje pri vsakem koraku. Uporabite to za matematične probleme, logične uganke ali kadar želite razumeti miselni proces.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Razčlenjevanje problemov na izrecne logične korake*

**Omejen izhod** – Za odgovore s specifičnimi zahtevami glede formata. Model strogo sledi pravilom za format in dolžino. Uporabite to za povzetke ali kadar potrebujete natančno strukturo izhoda.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/sl/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Uvajanje zahtev glede formata, dolžine in strukture*

## Uporaba obstoječih Azure virov

**Preverite nameščanje:**

Prepričajte se, da na korenskem mestu obstaja datoteka `.env` z Azure poverilnicami (ustvarjeno med Modulom 01):
```bash
cat ../.env  # Prikazati bi morali AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz Modula 01, ta modul že teče na vratih 8083. Lahko preskočite spodnje ukaze za zagon in neposredno odprete http://localhost:8083.

**Možnost 1: Uporaba Spring Boot Dashboard (priporočeno za uporabnike VS Code)**

Razvojno okolje vsebuje razširitev Spring Boot Dashboard, ki nudi vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo v neaktivnostni vrstici na levi strani VS Code (ikonka Spring Boot).

Iz Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Enostavno zaženete/ustavite aplikacije s klikom
- V realnem času spremljate dnevnike aplikacij
- Nadzirate stanje aplikacij

Preprosto kliknite gumb za predvajanje poleg "prompt-engineering" za zagon tega modula ali zaženite vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba ukaznih skript**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Oba skripta samodejno naložita okoljske spremenljivke iz korenske datoteke `.env` in zgradita JAR-je, če ti še ne obstajajo.

> **Opomba:** Če želite ročno zgraditi vse module pred zagonom:
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

Odprite http://localhost:8083 v brskalniku.

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

## Posnetki zaslona aplikacije

<img src="../../../translated_images/sl/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavna nadzorna plošča z osmimi vzorci pozivov in njihovimi značilnostmi ter primeri uporabe*

## Raziščite vzorce

Spletni vmesnik vam omogoča preizkušanje različnih strategij pozivanja. Vsak vzorec rešuje različne težave - preizkusite jih, da vidite, kdaj kateri pristop najbolj deluje.

### Nizka proti visoki pripravljenosti

Vprašajte enostavno vprašanje, npr. "Koliko je 15 % od 200?" z nizko pripravljenostjo. Prejeli boste takojšen, direkten odgovor. Zdaj pa vprašajte nekaj zahtevnejšega, npr. "Oblikuj strategijo predpomnjenja za API z veliko obremenitvijo" z visoko pripravljenostjo. Opazujte, kako model upočasni in poda podrobno razlago. Isto model, ista struktura vprašanja – vendar poziv določa, koliko razmišljanja naj opravi.
<img src="../../../translated_images/sl/low-eagerness-demo.898894591fb23aa0.webp" alt="Demo nizke vneme" width="800"/>

*Hitra računanja z minimalnim razmišljanjem*

<img src="../../../translated_images/sl/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demo visoke vneme" width="800"/>

*Celovita strategija predpomnjenja (2,8 MB)*

### Izvajanje nalog (Tool Preambles)

Večstopenjski poteki dela imajo koristi od predhodnega načrtovanja in pripovedovanja napredka. Model opiše, kaj bo naredil, razloži vsak korak in nato povzame rezultate.

<img src="../../../translated_images/sl/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demo izvajanja nalog" width="800"/>

*Ustvarjanje REST končne točke z opisovanjem korak za korakom (3,9 MB)*

### Samoreflektivna koda

Poskusite "Ustvari storitev za preverjanje e-poštnega naslova". Namesto da bi le generiral kodo in se ustavil, model ustvari, oceni glede na kriterije kakovosti, prepozna slabosti in izboljša. Videli boste, kako ponavlja, dokler koda ne doseže proizvodnih standardov.

<img src="../../../translated_images/sl/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demo samoreflektivne kode" width="800"/>

*Popolna storitev za preverjanje e-pošte (5,2 MB)*

### Strukturna analiza

Pregledi kode potrebujejo dosledne okvirje ocenjevanja. Model analizira kodo z uporabo fiksnih kategorij (pravilnost, prakse, učinkovitost, varnost) z različnimi stopnjami resnosti.

<img src="../../../translated_images/sl/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demo strukturne analize" width="800"/>

*Pregled kode na osnovi okvirja*

### Pogovor z več koraki

Vprašajte "Kaj je Spring Boot?" in takoj sledite s "Pokaži mi primer". Model si zapomni vaše prvo vprašanje in vam posebej poda primer Spring Boot. Brez spomina bi bilo drugo vprašanje preveč nejasno.

<img src="../../../translated_images/sl/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demo pogovora z več koraki" width="800"/>

*Ohranjanje konteksta med vprašanji*

### Razmišljanje korak za korakom

Izberite matematično nalogo in jo poskusite rešiti z Razmišljanjem korak za korakom in z Nizko vnemo. Nizka vnema vam poda samo odgovor - hitro, a nejasno. Razmišljanje korak za korakom vam pokaže vsak izračun in odločitev.

<img src="../../../translated_images/sl/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demo razmišljanja korak za korakom" width="800"/>

*Matematična naloga s podrobnimi koraki*

### Omejen izhod

Ko potrebujete specifične formate ali število besed, ta vzorec zagotavlja strog spoštovanje pravil. Poskusite ustvariti povzetek z natanko 100 besedami v obliki točk.

<img src="../../../translated_images/sl/constrained-output-demo.567cc45b75da1633.webp" alt="Demo omejenega izhoda" width="800"/>

*Povzetek strojnega učenja z nadzorom formata*

## Kaj se pravzaprav učite

**Razmišljanje spremeni vse**

GPT-5.2 vam omogoča nadzor nad računsko težo prek vaših pozivov. Nizka teža pomeni hitre odgovore z minimalnim raziskovanjem. Visoka teža pomeni, da model vzame čas za globoko razmišljanje. Naučite se uskladiti trud z zahtevnostjo naloge - ne izgubljajte časa pri enostavnih vprašanjih, a tudi ne hitite pri zapletenih odločitvah.

**Struktura vodi vedenje**

Opazili ste XML oznake v pozivih? Niso dekorativne. Modeli bolj zanesljivo sledijo strukturnim navodilom kot prostemu besedilu. Ko potrebujete večstopenjske postopke ali kompleksno logiko, struktura pomaga modelu slediti, kje je in kaj sledi.

<img src="../../../translated_images/sl/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura poziva" width="800"/>

*Analiza dobro strukturiranega poziva z jasnimi odseki in XML-stil organizacijo*

**Kakovost skozi samoocenjevanje**

Vzorce samorefleksije delujejo tako, da eksplicitno opredelijo merila kakovosti. Namesto da bi upali, da bo model "pravilno naredil", mu natančno poveste, kaj pomeni "pravilen": pravilna logika, ravnanje z napakami, učinkovitost, varnost. Model tako lahko oceni svoj izhod in se izboljša. To spremeni generiranje kode iz loterije v proces.

**Kontekst je končen**

Večkrožni pogovori delujejo tako, da vključujejo zgodovino sporočil v vsakem zahtevku. Toda obstaja meja - vsak model ima največje število tokenov. Ko pogovori rastejo, boste potrebovali strategije za ohranitev relevantnega konteksta, ne da bi dosegli to omejitev. Ta modul vam pokaže, kako deluje pomnilnik; kasneje boste spoznali, kdaj povzeti, kdaj pozabiti in kdaj ponovno pridobiti.

## Naslednji koraki

**Naslednji modul:** [03-rag - RAG (Generiranje z nadgradnjo iskanja)](../03-rag/README.md)

---

**Navigacija:** [← Prejšnji: Modul 01 - Uvod](../01-introduction/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Opozorilo**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v izvirnem jeziku velja za avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Za morebitna nesporazume ali napačne razlage, ki bi izhajale iz uporabe tega prevoda, ne odgovarjamo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->