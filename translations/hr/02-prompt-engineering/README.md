# Modul 02: Inženjering upita s GPT-5.2

## Sadržaj

- [Što ćete naučiti](../../../02-prompt-engineering)
- [Preduvjeti](../../../02-prompt-engineering)
- [Razumijevanje inženjeringa upita](../../../02-prompt-engineering)
- [Kako ovo koristi LangChain4j](../../../02-prompt-engineering)
- [Osnovni obrasci](../../../02-prompt-engineering)
- [Korištenje postojećih Azure resursa](../../../02-prompt-engineering)
- [Snimke zaslona aplikacije](../../../02-prompt-engineering)
- [Istraživanje obrazaca](../../../02-prompt-engineering)
  - [Niska vs visoka želja](../../../02-prompt-engineering)
  - [Izvršavanje zadataka (Uvodni dijelovi alata)](../../../02-prompt-engineering)
  - [Samoreflektirajući kod](../../../02-prompt-engineering)
  - [Strukturirana analiza](../../../02-prompt-engineering)
  - [Višekratni razgovor](../../../02-prompt-engineering)
  - [Razmišljanje korak-po-korak](../../../02-prompt-engineering)
  - [Ograničeni izlaz](../../../02-prompt-engineering)
- [Što zapravo učite](../../../02-prompt-engineering)
- [Sljedeći koraci](../../../02-prompt-engineering)

## Što ćete naučiti

U prethodnom modulu ste vidjeli kako memorija omogućuje konverzacijski AI i koristili ste GitHub modele za osnovne interakcije. Sada ćemo se usredotočiti na to kako postavljate pitanja – same upite – koristeći GPT-5.2 usluge Azure OpenAI. Način na koji strukturirate upite dramatično utječe na kvalitetu dobivenih odgovora.

Koristit ćemo GPT-5.2 jer uvodi kontrolu razmišljanja – možete modelu odrediti koliko treba razmišljati prije odgovora. To čini različite strategije upita jasno vidljivima i pomaže vam razumjeti kada koristiti koji pristup. Također ćemo iskoristiti manje ograničenja brzine Azure usluge za GPT-5.2 u odnosu na GitHub modele.

## Preduvjeti

- Završeni Modul 01 (postavljeni Azure OpenAI resursi)
- `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama (kreirana pomoću `azd up` u Modulu 01)

> **Napomena:** Ako niste dovršili Modul 01, prvo slijedite upute za postavljanje tamo.

## Razumijevanje inženjeringa upita

Inženjering upita odnosi se na dizajniranje ulaznog teksta koji dosljedno dobiva željene rezultate. Nije samo u postavljanju pitanja – radi se o strukturiranju zahtjeva tako da model točno razumije što želite i kako to isporučiti.

Razmislite o tome kao da dajete upute kolegi. "Popravi grešku" je nejasno. "Popravi iznimku nulritema u UserService.java na liniji 45 dodavanjem provjere null vrijednosti" je specifično. Jezični modeli rade isto – važna je specifičnost i struktura.

## Kako ovo koristi LangChain4j

Ovaj modul demonstrira napredne obrasce upita koristeći istu LangChain4j osnovu iz prethodnih modula, s fokusom na strukturu upita i kontrolu razmišljanja.

<img src="../../../translated_images/hr/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Kako LangChain4j povezuje vaše upite s Azure OpenAI GPT-5.2*

**Ovisnosti** – Modul 02 koristi sljedeće langchain4j ovisnosti definirane u `pom.xml`:
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

**OpenAiOfficialChatModel konfiguracija** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chat model je ručno konfiguriran kao Spring bean koristeći OpenAI službeni klijent koji podržava Azure OpenAI krajnje točke. Ključna razlika u odnosu na Modul 01 je u načinu na koji strukturiramo upite poslati u `chatModel.chat()`, a ne u samoj postavci modela.

**Sistemske i korisničke poruke** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j odvaja vrste poruka radi jasnoće. `SystemMessage` postavlja ponašanje i kontekst AI-ja (npr. "Vi ste recenzent koda"), dok `UserMessage` sadrži stvarni zahtjev. Ovo odvajanje omogućuje održavanje dosljednog ponašanja AI-ja kroz različite korisničke upite.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/hr/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage pruža trajni kontekst dok UserMessages sadrže pojedinačne zahtjeve*

**MessageWindowChatMemory za višekratni razgovor** – Za obrazac višekratnog razgovora ponovno koristimo `MessageWindowChatMemory` iz Modula 01. Svaka sesija dobiva svoju vlastitu primjenu memorije pohranjenu u `Map<String, ChatMemory>`, što omogućuje paralelne razgovore bez miješanja konteksta.

**Predlošci upita** – Pravi fokus ovdje je inženjering upita, ne novi API-ji LangChain4j. Svaki obrazac (niska želja, visoka želja, izvršavanje zadataka itd.) koristi istu metodu `chatModel.chat(prompt)` ali s pažljivo strukturiranim tekstom upita. XML tagovi, upute i oblikovanje su dio samog teksta upita, a ne funkcionalnosti LangChain4j.

**Kontrola razmišljanja** – Napor razmišljanja GPT-5.2 kontrolira se uputama u upitu poput "najviše 2 koraka razmišljanja" ili "istraži temeljito". To su tehnike inženjeringa upita, ne konfiguracije LangChain4j. Biblioteka samo šalje vaše upite modelu.

Glavni zaključak: LangChain4j osigurava infrastrukturu (povezivanje modela putem [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memoriju, rukovanje porukama putem [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), dok ovaj modul uči kako izrađivati učinkovite upite unutar te infrastrukture.

## Osnovni obrasci

Nisu svi problemi isti pa ne zahtijevaju isti pristup. Neka pitanja zahtijevaju brze odgovore, druga duboko razmišljanje. Neki traže vidljivo razmišljanje, drugi samo rezultate. Ovaj modul pokriva osam obrazaca inženjeringa upita – svaki optimiziran za različite scenarije. Eksperimentirat ćete sa svim kako biste naučili kada koji pristup najbolje funkcionira.

<img src="../../../translated_images/hr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Pregled osam obrazaca inženjeringa upita i njihova primjena*

<img src="../../../translated_images/hr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Niska želja (brzo, direktno) vs Visoka želja (temeljito, istraživački) pristupi razmišljanju*

**Niska želja (Brzo i fokusirano)** – Za jednostavna pitanja kod kojih želite brze i direktne odgovore. Model izvodi minimalno razmišljanje – najviše 2 koraka. Koristite za izračune, pretraživanja ili jednostavna pitanja.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Istražite s GitHub Copilot:** Otvorite [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) i pitajte:
> - "Koja je razlika između obrazaca niske i visoke želje u upitima?"
> - "Kako XML tagovi u upitima pomažu u strukturi odgovora AI-ja?"
> - "Kada trebam koristiti samoreflektirajuće obrasce, a kada izravne upute?"

**Visoka želja (Duboko i temeljito)** – Za složene probleme gdje želite detaljnu analizu. Model temeljito istražuje i prikazuje detaljno razmišljanje. Koristite za dizajn sustava, arhitektonske odluke ili složena istraživanja.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Izvršavanje zadataka (Napredak korak po korak)** – Za višekorake tijekove rada. Model daje unaprijed plan, opisuje svaki korak u tijeku rada, a zatim daje sažetak. Koristite za migracije, implementacije ili bilo koji višekorak proces.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought upiti izričito traže od modela da pokaže svoj proces razmišljanja, što poboljšava točnost za složene zadatke. Detaljna dekompozicija pomaže i ljudima i AI-ju razumjeti logiku.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o ovom obrascu:
> - "Kako bih prilagodio obrazac izvršavanja zadataka za dugotrajne operacije?"
> - "Koje su najbolje prakse za strukturiranje uvodnih dijelova alata u produkcijskim aplikacijama?"
> - "Kako mogu uhvatiti i prikazati međurezultate u korisničkom sučelju?"

<img src="../../../translated_images/hr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plan → Izvrši → Sažmi tijek rada za višekorake zadatke*

**Samoreflektirajući kod** – Za generiranje koda proizvodne kvalitete. Model generira kod, provjerava ga prema kriterijima kvalitete i iterativno poboljšava. Koristite kad gradite nove funkcionalnosti ili servise.

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

<img src="../../../translated_images/hr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativni ciklus poboljšanja – generiraj, procijeni, identificiraj probleme, poboljša, ponovi*

**Strukturirana analiza** – Za dosljednu evaluaciju. Model pregledava kod koristeći fiksni okvir (ispravnost, prakse, performanse, sigurnost). Koristite za recenzije koda ili procjene kvalitete.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Pitajte o strukturiranoj analizi:
> - "Kako mogu prilagoditi okvir analize za različite vrste recenzija koda?"
> - "Koji je najbolji način za parsiranje i programatsko postupanje s strukturiranim izlazom?"
> - "Kako osigurati dosljedne razine ozbiljnosti kroz različite sesije recenzije?"

<img src="../../../translated_images/hr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Okvir s četiri kategorije za dosljedne recenzije koda s razinama ozbiljnosti*

**Višekratni razgovor** – Za razgovore kojima treba kontekst. Model pamti prethodne poruke i nadograđuje ih. Koristite za interaktivne sesije pomoći ili složena pitanja i odgovore.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/hr/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Kako se kontekst razgovora akumulira kroz višestruke okrete sve dok se ne dostigne limit tokena*

**Razmišljanje korak-po-korak** – Za probleme kod kojih je potrebna vidljiva logika. Model prikazuje izričito razmišljanje za svaki korak. Koristite za računske zadatke, logičke zagonetke ili kad trebate razumjeti proces razmišljanja.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Razlaganje problema na izričite logičke korake*

**Ograničeni izlaz** – Za odgovore s posebnih zahtjevima formata. Model strogo slijedi pravila formata i duljine. Koristite za sažetke ili kad trebate preciznu strukturu izlaza.

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

<img src="../../../translated_images/hr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Nametanje specifičnih zahtjeva formata, duljine i strukture*

## Korištenje postojećih Azure resursa

**Provjerite postavljanje:**

Provjerite da `.env` datoteka postoji u korijenskom direktoriju s Azure vjerodajnicama (kreirana tijekom Modula 01):
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikaciju:**

> **Napomena:** Ako ste već pokrenuli sve aplikacije koristeći `./start-all.sh` iz Modula 01, ovaj modul već radi na portu 8083. Možete preskočiti naredbe za pokretanje ispod i odmah otvoriti http://localhost:8083.

**Opcija 1: Korištenje Spring Boot Dashboarda (preporučeno za korisnike VS Codea)**

Dev kontejner uključuje ekstenziju Spring Boot Dashboard koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete je pronaći u Activity Bar s lijeve strane VS Codea (potražite ikonu Spring Boota).

U Spring Boot Dashboardu možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pratiti logove aplikacije u stvarnom vremenu
- Pratiti status aplikacije

Jednostavno kliknite gumb za pokretanje pored "prompt-engineering" da započnete ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opcija 2: Korištenje shell skripti**

Pokrenite sve web aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korijenskog direktorija
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korijenskog direktorija
.\start-all.ps1
```

Ili pokrenite samo ovaj modul:

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

Obje skripte automatski učitavaju varijable okoline iz root `.env` datoteke i kompajlirat će JAR-ove ako ne postoje.

> **Napomena:** Ako želite ručno izgraditi sve module prije pokretanja:
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

Otvorite http://localhost:8083 u svom pregledniku.

**Za zaustavljanje:**

**Bash:**
```bash
./stop.sh  # Samo ovaj modul
# Ili
cd .. && ./stop-all.sh  # Svi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ovaj modul
# Ili
cd ..; .\stop-all.ps1  # Svi moduli
```

## Snimke zaslona aplikacije

<img src="../../../translated_images/hr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Glavni panel koji prikazuje svih 8 obrazaca inženjeringa upita s njihovim karakteristikama i primjenama*

## Istraživanje obrazaca

Web sučelje omogućuje vam eksperimentiranje s različitim strategijama upita. Svaki obrazac rješava različite probleme – isprobajte ih da vidite kada koji pristup najbolje funkcionira.

### Niska vs visoka želja

Postavite jednostavno pitanje poput "Koliko je 15% od 200?" koristeći Nisku želju. Dobit ćete trenutačan, direktan odgovor. Sada postavite složeno pitanje poput "Dizajniraj strategiju keširanja za API s velikim prometom" koristeći Visoku želju. Pratite kako se model usporava i daje detaljno razmišljanje. Isti model, ista struktura upita – ali upit mu govori koliko treba razmišljati.
<img src="../../../translated_images/hr/low-eagerness-demo.898894591fb23aa0.webp" alt="Demonstracija niske energičnosti" width="800"/>

*Brzi izračun s minimalnim razmišljanjem*

<img src="../../../translated_images/hr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demonstracija visoke energičnosti" width="800"/>

*Sveobuhvatna strategija predmemoriranja (2,8MB)*

### Izvršenje zadatka (Uvodni alati)

Višekoračni radni tokovi koriste predhodno planiranje i pripovijedanje o napretku. Model izlaže što će učiniti, pripovijeda svaki korak, zatim sažima rezultate.

<img src="../../../translated_images/hr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demonstracija izvršenja zadatka" width="800"/>

*Izrada REST endpointa s pripovijedanjem korak-po-korak (3,9MB)*

### Kod koji se samoprocjenjuje

Isprobajte "Izradi uslugu za provjeru valjanosti e-pošte". Umjesto da samo generira kod i stane, model generira, procjenjuje prema kriterijima kvalitete, prepoznaje slabosti i poboljšava. Vidjet ćete iteracije dok kod ne zadovolji proizvodne standarde.

<img src="../../../translated_images/hr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demonstracija samoprocjenjujućeg koda" width="800"/>

*Potpuna usluga za provjeru valjanosti e-pošte (5,2MB)*

### Struktuirana analiza

Pregledi koda trebaju dosljedne evaluacijske okvire. Model analizira kod koristeći fiksne kategorije (točnost, prakse, performanse, sigurnost) s razinama ozbiljnosti.

<img src="../../../translated_images/hr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demonstracija struktuirane analize" width="800"/>

*Pregled koda temeljen na okvirovima*

### Razgovor s više zamjena

Pitajte "Što je Spring Boot?" zatim odmah slijedite s "Pokaži mi primjer". Model pamti vaše prvo pitanje i daje vam specifičan primjer za Spring Boot. Bez memorije, drugo pitanje bi bilo previše neodređeno.

<img src="../../../translated_images/hr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demonstracija razgovora s više zamjena" width="800"/>

*Očuvanje konteksta kroz pitanja*

### Razmišljanje korak po korak

Izaberite matematički problem i pokušajte ga riješiti korak-po-korak i s niskom energičnošću. Niska energičnost samo daje odgovor - brzo ali nejasno. Korak-po-korak pokazuje svaki izračun i odluku.

<img src="../../../translated_images/hr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demonstracija razmišljanja korak po korak" width="800"/>

*Matematički problem s eksplicitnim koracima*

### Ograničeni ispis

Kad vam trebaju specifični formati ili broj riječi, ovaj obrazac strogo kontrolira pridržavanje. Pokušajte generirati sažetak s točno 100 riječi u obliku popisa.

<img src="../../../translated_images/hr/constrained-output-demo.567cc45b75da1633.webp" alt="Demonstracija ograničenog ispisa" width="800"/>

*Sažetak strojnog učenja s kontrolom formata*

## Što stvarno učite

**Napor razmišljanja mijenja sve**

GPT-5.2 vam omogućuje kontrolu računalnog napora putem vaših upita. Niski napor znači brze odgovore s minimalnim istraživanjem. Visoki napor znači da model troši vremena da duboko razmisli. Učite uskladiti napor s kompleksnošću zadatka - nemojte trošiti vrijeme na jednostavna pitanja, ali ni žuriti sa složenim odlukama.

**Struktura vodi ponašanje**

Primijetite XML oznake u upitima? One nisu ukrasne. Modeli pouzdanije slijede strukturirane upute od slobodnog teksta. Kad trebate višekoračne procese ili složenu logiku, struktura pomaže modelu pratiti gdje je i što slijedi.

<img src="../../../translated_images/hr/prompt-structure.a77763d63f4e2f89.webp" alt="Struktura upita" width="800"/>

*Anatomija dobro strukturiranog upita s jasnim odjeljcima i XML-organizacijom*

**Kvaliteta kroz samoocjenjivanje**

Samoprocjenjujući obrasci funkcioniraju tako da eksplicitno definiraju kriterije kvalitete. Umjesto da se nadate da model "radi ispravno", vi mu točno kažete što "ispravno" znači: ispravna logika, rukovanje pogreškama, performanse, sigurnost. Model tada može procijeniti vlastiti izlaz i poboljšati se. Time generiranje koda postaje proces, a ne lutrija.

**Kontekst je ograničen**

Razgovori s više zamjena funkcioniraju tako što uključuju povijest poruka sa svakim zahtjevom. No postoji limit - svaki model ima maksimalan broj tokena. Kako razgovori rastu, trebate strategije za održavanje relevantnog konteksta bez prekoračenja tog limita. Ovaj modul pokazuje kako memorija funkcionira; kasnije ćete naučiti kad sažeti, kad zaboraviti i kad dohvatiti.

## Daljnji koraci

**Sljedeći modul:** [03-rag - RAG (generiranje potpomognuto dohvatom)](../03-rag/README.md)

---

**Navigacija:** [← Prethodno: Modul 01 - Uvod](../01-introduction/README.md) | [Natrag na glavni izbornik](../README.md) | [Sljedeće: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj dokument preveden je korištenjem AI usluge prevođenja [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalan ljudski prijevod. Ne odgovaramo za bilo kakva nesporazumevanja ili pogrešna tumačenja proizašla iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->