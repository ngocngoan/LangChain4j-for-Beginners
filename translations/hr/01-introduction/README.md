# Modul 01: Uvod u LangChain4j

## Sadržaj

- [Video vodič](../../../01-introduction)
- [Što ćete naučiti](../../../01-introduction)
- [Preduvjeti](../../../01-introduction)
- [Razumijevanje osnovnog problema](../../../01-introduction)
- [Razumijevanje tokena](../../../01-introduction)
- [Kako radi memorija](../../../01-introduction)
- [Kako se koristi LangChain4j](../../../01-introduction)
- [Implementacija Azure OpenAI infrastrukture](../../../01-introduction)
- [Pokretanje aplikacije lokalno](../../../01-introduction)
- [Korištenje aplikacije](../../../01-introduction)
  - [Bezstatični chat (lijeva ploča)](../../../01-introduction)
  - [Statični chat (desna ploča)](../../../01-introduction)
- [Sljedeći koraci](../../../01-introduction)

## Video vodič

Pogledajte ovu snimku uživo koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Što ćete naučiti

Ako ste završili brzi početak, vidjeli ste kako poslati upite i dobiti odgovore. To je temelj, no stvarne aplikacije trebaju više. Ovaj modul vas uči kako izgraditi konverzacijski AI koji pamti kontekst i održava stanje - razliku između jednokratnog demo primjera i aplikacije spremne za produkciju.

Kroz ovaj vodič koristit ćemo Azure OpenAI GPT-5.2 zbog njegovih naprednih sposobnosti zaključivanja koje jasno pokazuju ponašanje različitih uzoraka. Kad dodate memoriju, jasno ćete vidjeti razliku. To olakšava razumijevanje što svaki dio donosi vašoj aplikaciji.

Izgradit ćete jednu aplikaciju koja demonstrira oba uzorka:

**Bezstatični chat** – Svaki zahtjev je neovisan. Model nema memoriju prethodnih poruka. To je uzorak koji ste koristili u brzom početku.

**Statički razgovor** – Svaki zahtjev uključuje povijest razgovora. Model održava kontekst kroz više izmjena. Ovo je što zahtijevaju produkcijske aplikacije.

## Preduvjeti

- Azure pretplata s pristupom Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Napomena:** Java, Maven, Azure CLI i Azure Developer CLI (azd) su unaprijed instalirani u pruženom devcontaineru.

> **Napomena:** Ovaj modul koristi GPT-5.2 na Azure OpenAI. Implementacija se automatski konfigurira pomoću `azd up` – nemojte mijenjati naziv modela u kodu.

## Razumijevanje osnovnog problema

Jezični modeli su bezstatični. Svaki API poziv je neovisan. Ako pošaljete "Moje ime je John" i zatim pitate "Kako se zovem?", model nema ideju da ste se upravo predstavili. Svaki zahtjev tretira kao da je prvi razgovor koji ste ikad vodili.

To je u redu za jednostavna pitanja i odgovore, ali beskorisno za stvarne aplikacije. Chatboti za korisničku podršku moraju pamtiti što ste im rekli. Osobni asistenti trebaju kontekst. Svaki višekratni razgovor zahtijeva memoriju.

<img src="../../../translated_images/hr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Razlika između bezstatičnih (neovisnih poziva) i statićnih (svjesnih konteksta) razgovora*

## Razumijevanje tokena

Prije nego što zaronimo u razgovore, važno je razumjeti tokene – osnovne jedinice teksta koje jezični modeli obrađuju:

<img src="../../../translated_images/hr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Primjer kako se tekst dijeli na tokene – "Volim AI!" postaje 4 zasebne jedinice za obradu*

Tokeni su način na koji AI modeli mjere i obrađuju tekst. Riječi, interpunkcija, pa čak i razmaci mogu biti tokeni. Vaš model ima ograničenje koliko tokena može obraditi odjednom (400.000 za GPT-5.2, s do 272.000 ulaznih i 128.000 izlaznih tokena). Razumijevanje tokena pomaže vam upravljati duljinom razgovora i troškovima.

## Kako radi memorija

Memorija u chatu rješava problem bezstatičnosti održavanjem povijesti razgovora. Prije nego što pošaljete zahtjev modelu, okvir dodaje relevantne prethodne poruke. Kad pitate "Kako se zovem?", sustav zapravo šalje cijelu povijest razgovora, omogućujući modelu da vidi da ste ranije rekli "Moje ime je John."

LangChain4j nudi implementacije memorije koje to rade automatski. Vi birate koliko poruka zadržati, a okvir upravlja kontekstnim prozorom.

<img src="../../../translated_images/hr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory održava pomični prozor nedavnih poruka, automatski uklanjajući stare*

## Kako se koristi LangChain4j

Ovaj modul proširuje brzi početak integrirajući Spring Boot i dodajući memoriju razgovora. Ovako dijelovi funkcioniraju zajedno:

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

**Chat model** – Konfigurirajte Azure OpenAI kao Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder učitava vjerodajnice iz varijabli okoline postavljenih pomoću `azd up`. Postavljanje `baseUrl` na vaš Azure endpoint omogućava OpenAI klijentu rad s Azure OpenAI.

**Memorija razgovora** – Pratite povijest chata s MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Kreirajte memoriju s `withMaxMessages(10)` da zadržite zadnjih 10 poruka. Dodajte poruke korisnika i AI-ja s tipiziranim omotačima: `UserMessage.from(text)` i `AiMessage.from(text)`. Dohvatite povijest s `memory.messages()` i pošaljite je modelu. Servis pohranjuje zasebne instance memorije za svaki ID razgovora, što omogućava da više korisnika chat-a istovremeno.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i pitajte:
> - "Kako MessageWindowChatMemory odlučuje koje poruke ukloniti kad je prozor pun?"
> - "Mogu li implementirati prilagođenu pohranu memorije koristeći bazu podataka umjesto memorije u RAM-u?"
> - "Kako bih dodao sažimanje za komprimiranje stare povijesti razgovora?"

Bezstatična chat točka preskače memoriju – samo `chatModel.chat(prompt)` kao u brzom početku. Statična točka dodaje poruke u memoriju, dohvaća povijest i uključuje taj kontekst u svaki zahtjev. Ista konfiguracija modela, različiti uzorci.

## Implementacija Azure OpenAI infrastrukture

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

> **Napomena:** Ako dobijete grešku o vremenskom ograničenju (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednostavno pokrenite `azd up` ponovno. Azure resursi možda još uvijek postavljaju usluge u pozadini, a ponovni pokušaj omogućava dovršetak implementacije kad resursi dođu do konačnog stanja.

Ovo će:
1. Implementirati Azure OpenAI resurs s GPT-5.2 i modelima text-embedding-3-small
2. Automatski generirati `.env` datoteku u korijenu projekta s vjerodajnicama
3. Postaviti sve potrebne varijable okoline

**Imate problema s implementacijom?** Pogledajte [README za infrastrukturu](infra/README.md) za detaljno otklanjanje problema uključujući sukobe imena poddomena, ručne korake implementacije preko Azure Portala i smjernice za konfiguraciju modela.

**Provjerite je li implementacija uspješna:**

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebao bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

> **Napomena:** Naredba `azd up` automatski generira `.env` datoteku. Ako je kasnije trebate ažurirati, možete ili urediti `.env` datoteku ručno ili je regenerirati pokretanjem:
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

**Provjerite implementaciju:**

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama:

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikacije:**

**Opcija 1: Korištenje Spring Boot nadzorne ploče (preporučeno za korisnike VS Code-a)**

Dev container uključuje ekstenziju Spring Boot Dashboard, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ju pronaći u Activity Bar-u na lijevoj strani VS Code-a (potražite ikonu Spring Boot).

Iz Spring Boot Dashboarda možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokretati/pauzirati aplikacije jednim klikom
- Pratiti logove aplikacija u stvarnom vremenu
- Nadgledati status aplikacije

Jednostavno kliknite gumb za reprodukciju pokraj "introduction" kako biste pokrenuli ovaj modul, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Obje skripte automatski učitavaju varijable okoline iz `.env` datoteke u korijenu i izgradit će JAR-ove ako ne postoje.

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

Otvorite http://localhost:8080 u pregledniku.

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

Aplikacija pruža web sučelje s dvije implementacije chata jedna pored druge.

<img src="../../../translated_images/hr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Nadzorna ploča koja prikazuje opcije Jednostavnog chata (bezstatični) i Konverzacijskog chata (statični)*

### Bezstatični chat (lijeva ploča)

Isprobajte prvo ovo. Recite "Moje ime je John" i zatim odmah pitajte "Kako se zovem?" Model se neće sjećati jer je svaka poruka neovisna. Ovo demonstrira osnovni problem s integracijom jezičnih modela – nema konteksta razgovora.

<img src="../../../translated_images/hr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI se ne sjeća vašeg imena iz prethodne poruke*

### Statični chat (desna ploča)

Sada pokušajte isti slijed ovdje. Recite "Moje ime je John" i zatim "Kako se zovem?" Ovaj put se pamti. Razlika je MessageWindowChatMemory – održava povijest razgovora i uključuje je u svaki zahtjev. Tako funkcionira produkcijski konverzacijski AI.

<img src="../../../translated_images/hr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI se sjeća vašeg imena ranije u razgovoru*

Obje ploče koriste isti GPT-5.2 model. Jedina razlika je memorija. To jasno pokazuje što memorija donosi vašoj aplikaciji i zašto je bitna za stvarne slučajeve korištenja.

## Sljedeći koraci

**Sljedeći modul:** [02-prompt-engineering - Prompt inženjerstvo s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prethodno: Modul 00 - Brzi početak](../00-quick-start/README.md) | [Natrag na početnu](../README.md) | [Dalje: Modul 02 - Prompt inženjerstvo →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:  
Ovaj je dokument preveden pomoću AI servisa za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba se smatrati službenim i ovlaštenim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakve nesporazume ili pogrešna tumačenja proizašla iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->