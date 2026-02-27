# Modul 01: Uvod u LangChain4j

## Sadržaj

- [Video vodič](../../../01-introduction)
- [Što ćete naučiti](../../../01-introduction)
- [Preduvjeti](../../../01-introduction)
- [Razumijevanje osnovnog problema](../../../01-introduction)
- [Razumijevanje tokena](../../../01-introduction)
- [Kako memorija funkcionira](../../../01-introduction)
- [Kako ovo koristi LangChain4j](../../../01-introduction)
- [Postavljanje Azure OpenAI infrastrukture](../../../01-introduction)
- [Pokretanje aplikacije lokalno](../../../01-introduction)
- [Korištenje aplikacije](../../../01-introduction)
  - [Stateless chat (lijevi panel)](../../../01-introduction)
  - [Stateful chat (desni panel)](../../../01-introduction)
- [Sljedeći koraci](../../../01-introduction)

## Video vodič

Pogledajte ovu uživo sesiju koja objašnjava kako započeti s ovim modulom: [Uvod u LangChain4j - Live sesija](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Što ćete naučiti

Ako ste završili brzi početak, vidjeli ste kako poslati upite i dobiti odgovore. To je temelj, ali stvarne aplikacije trebaju više. Ovaj modul vas uči kako izgraditi konverzacijski AI koji pamti kontekst i održava stanje - razliku između jednokratnog demoa i aplikacije spremne za proizvodnju.

Kroz vodič koristimo Azure OpenAI GPT-5.2 jer njegove napredne mogućnosti razmišljanja jasno pokazuju ponašanje različitih obrazaca. Kad dodate memoriju, jasno ćete vidjeti razliku. To olakšava razumijevanje što svaki dio donosi vašoj aplikaciji.

Izgradit ćete jednu aplikaciju koja prikazuje oba obrasca:

**Stateless Chat** – Svaki zahtjev je neovisni. Model nema sjećanje na prethodne poruke. Ovo je obrazac koji ste koristili u brzom početku.

**Stateful Conversation** – Svaki zahtjev uključuje povijest razgovora. Model održava kontekst kroz više koraka. Ovo je ono što proizvodne aplikacije zahtijevaju.

## Preduvjeti

- Azure pretplata s pristupom Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Napomena:** Java, Maven, Azure CLI i Azure Developer CLI (azd) su unaprijed instalirani u pruženom devcontaineru.

> **Napomena:** Ovaj modul koristi GPT-5.2 na Azure OpenAI. Implementacija se konfigurira automatski putem `azd up` - nemojte mijenjati naziv modela u kodu.

## Razumijevanje osnovnog problema

Jezični modeli su stateless. Svaki poziv API-ja je neovisan. Ako pošaljete "Moje ime je John" pa zatim pitate "Kako se zovem?", model nema pojma da ste se upravo predstavili. Svaki zahtjev tretira kao prvi razgovor koji ste ikada imali.

To je okej za jednostavna pitanja i odgovore, ali beskorisno za stvarne aplikacije. Botovi za korisničku podršku moraju pamtiti što ste im rekli. Osobni asistenti trebaju kontekst. Svaki višekratni razgovor zahtijeva memoriju.

<img src="../../../translated_images/hr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Razlika između stateless (neovisnih poziva) i stateful (svjestan konteksta) razgovora*

## Razumijevanje tokena

Prije nego što zaronite u razgovore, važno je razumjeti tokene - osnovne jedinice teksta koje jezični modeli obrađuju:

<img src="../../../translated_images/hr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Primjer kako se tekst razbija na tokene - "Volim AI!" postaje 4 zasebne jedinice za obradu*

Tokeni su način na koji AI modeli mjere i obrađuju tekst. Riječi, interpunkcija, pa čak i razmaci mogu biti tokeni. Vaš model ima ograničenje koliko tokena može obraditi odjednom (400.000 za GPT-5.2, s do 272.000 ulaznih tokena i 128.000 izlaznih tokena). Razumijevanje tokena pomaže vam upravljati duljinom razgovora i troškovima.

## Kako memorija funkcionira

Memorija chata rješava problem stateless modela održavajući povijest razgovora. Prije slanja zahtjeva modelu, okvir prethodi relevantne prethodne poruke. Kad pitate "Kako se zovem?", sustav zapravo šalje cijelu povijest razgovora, dopuštajući modelu da vidi da ste prethodno rekli "Moje ime je John."

LangChain4j pruža implementacije memorije koje to automatski upravljaju. Vi birate koliko poruka želite zadržati, a okvir upravlja kontekstnim prozorom.

<img src="../../../translated_images/hr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory održava klizni prozor nedavnih poruka, automatski izbacujući stare*

## Kako ovo koristi LangChain4j

Ovaj modul proširuje brzi početak integracijom Spring Boota i dodavanjem memorije razgovora. Evo kako dijelovi međusobno funkcioniraju:

**Ovisnosti** – Dodajte dvije LangChain4j biblioteke:

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

**Model chata** – Konfigurirajte Azure OpenAI kao Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder čita vjerodajnice iz varijabli okoline postavljenih putem `azd up`. Postavljanje `baseUrl` na vaš Azure endpoint omogućava klijentu OpenAI da radi s Azure OpenAI.

**Memorija razgovora** – Pratite povijest chata s MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Kreirajte memoriju s `withMaxMessages(10)` za zadržavanje posljednjih 10 poruka. Dodajte poruke korisnika i AI s tipiziranim omotima: `UserMessage.from(text)` i `AiMessage.from(text)`. Dohvatite povijest s `memory.messages()` i pošaljite ju modelu. Servis pohranjuje zasebne instance memorije po ID-u razgovora, omogućavajući više korisnika da razgovaraju istovremeno.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i pitajte:
> - "Kako MessageWindowChatMemory odlučuje koje poruke odbaciti kad je prozor pun?"
> - "Mogu li implementirati prilagođeno spremište memorije koristeći bazu podataka umjesto memorije u RAM-u?"
> - "Kako bih dodao sažimanje za komprimiranje stare povijesti razgovora?"

Endpoint za stateless chat potpuno izostavlja memoriju - samo `chatModel.chat(prompt)` kao u brzom početku. Stateful endpoint dodaje poruke u memoriju, dohvaća povijest i uključuje taj kontekst u svaki zahtjev. Ista konfiguracija modela, različiti obrasci.

## Postavljanje Azure OpenAI infrastrukture

**Bash:**
```bash
cd 01-introduction
azd up  # Odaberite pretplatu i lokaciju (preporučeno eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Odaberite pretplatu i lokaciju (preporučeno eastus2)
```

> **Napomena:** Ako naiđete na grešku tajminga (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednostavno ponovno pokrenite `azd up`. Azure resursi se možda još uvijek postavljaju u pozadini, a ponovni pokušaj omogućuje dovršetak implementacije kad resursi dođu u krajnje stanje.

Ovo će:
1. Implementirati Azure OpenAI resurs s GPT-5.2 i modelima text-embedding-3-small
2. Automatski generirati `.env` datoteku u korijenu projekta s vjerodajnicama
3. Postaviti sve potrebne varijable okoline

**Imate problema s implementacijom?** Pogledajte [README infrastrukture](infra/README.md) za detaljno rješavanje problema uključujući sukobe imena poddomena, ručno postavljanje putem Azure Portala i upute za konfiguraciju modela.

**Provjerite je li implementacija uspješna:**

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

> **Napomena:** `azd up` naredba automatski generira `.env` datoteku. Ako ju kasnije trebate ažurirati, možete ju ili urediti ručno ili ponovno generirati pokretanjem:
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

## Pokretanje aplikacije lokalno

**Provjera implementacije:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama:

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikacije:**

**Opcija 1: Koristeći Spring Boot Dashboard (preporučeno za korisnike VS Code-a)**

Dev container uključuje Spring Boot Dashboard ekstenziju koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ga pronaći u traci aktivnosti s lijeve strane VS Code-a (potražite ikonu Spring Boot).

Iz Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/pauzirati aplikacije jednim klikom
- Pregledati dnevnik aplikacija u stvarnom vremenu
- Pratiti status aplikacija

Jednostavno kliknite gumb za pokretanje pored "introduction" da biste startali ovaj modul ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opcija 2: Koristeći shell skripte**

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Obje skripte automatski učitavaju varijable okoline iz root `.env` datoteke i izgradit će JAR-ove ako još ne postoje.

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

Otvorite http://localhost:8080 u vašem pregledniku.

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

## Korištenje aplikacije

Aplikacija pruža web sučelje s dvije implementacije chata postavljene jedna pored druge.

<img src="../../../translated_images/hr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Nadzorna ploča koja prikazuje opcije Jednostavni Chat (stateless) i Konverzacijski Chat (stateful)*

### Stateless chat (lijevi panel)

Isprobajte ovo prvo. Pitajte "Moje ime je John" i odmah zatim "Kako se zovem?" Model to neće zapamtiti jer je svaka poruka neovisna. Ovo ilustrira osnovni problem kod jednostavne integracije jezičnih modela - nema konteksta razgovora.

<img src="../../../translated_images/hr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI se ne sjeća vašeg imena iz prethodne poruke*

### Stateful chat (desni panel)

Sada isprobajte istu sekvencu ovdje. Pitajte "Moje ime je John" i zatim "Kako se zovem?" Ovog puta se pamti. Razlika je MessageWindowChatMemory - on održava povijest razgovora i uključuje je u svaki zahtjev. Ovakav način rada koristi proizvodni konverzacijski AI.

<img src="../../../translated_images/hr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI se sjeća vašeg imena iz ranijeg dijela razgovora*

Oba panela koriste isti GPT-5.2 model. Jedina razlika je memorija. To jasno pokazuje što memorija donosi vašoj aplikaciji i zašto je ključna za stvarne primjene.

## Sljedeći koraci

**Sljedeći modul:** [02-prompt-engineering - Prompt inženjering s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prethodni: Modul 00 - Brzi početak](../00-quick-start/README.md) | [Natrag na glavni](../README.md) | [Sljedeći: Modul 02 - Prompt inženjering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden pomoću AI prijevodne usluge [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim i autoritativnim izvorom. Za važne informacije preporučuje se profesionalni prijevod od strane čovjeka. Ne snosimo odgovornost za pogrešna tumačenja ili nesporazume proizašle iz upotrebe ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->