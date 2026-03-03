# Modul 01: Početak rada s LangChain4j

## Sadržaj

- [Video vodič](../../../01-introduction)
- [Što ćete naučiti](../../../01-introduction)
- [Preduvjeti](../../../01-introduction)
- [Razumijevanje osnovnog problema](../../../01-introduction)
- [Razumijevanje tokena](../../../01-introduction)
- [Kako memorija funkcionira](../../../01-introduction)
- [Kako se koristi LangChain4j](../../../01-introduction)
- [Implementacija Azure OpenAI infrastrukture](../../../01-introduction)
- [Pokretanje aplikacije lokalno](../../../01-introduction)
- [Korištenje aplikacije](../../../01-introduction)
  - [Stateless chat (lijevi panel)](../../../01-introduction)
  - [Stateful chat (desni panel)](../../../01-introduction)
- [Sljedeći koraci](../../../01-introduction)

## Video vodič

Pogledajte ovu live sesiju koja objašnjava kako započeti s ovim modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Što ćete naučiti

U brzom početku koristili ste GitHub modele za slanje upita, pozivanje alata, izgradnju RAG pipelinea i testiranje zaštitnih ograda. Ti su demonstracijski primjeri pokazali što je moguće — sada prelazimo na Azure OpenAI i GPT-5.2 te započinjemo izgradnju aplikacija u produkcijskom stilu. Ovaj modul fokusira se na konverzacijski AI koji pamti kontekst i održava stanje — pojmove koje su ti primjeri u brzom početku koristili u pozadini ali nisu objasnili.

Koristit ćemo Azure OpenAI GPT-5.2 kroz ovaj vodič jer njegove napredne sposobnosti rezoniranja čine ponašanje različitih šablona jasnijim. Kada dodate memoriju, jasno ćete vidjeti razliku. To olakšava razumijevanje što svaki komponent donosi vašoj aplikaciji.

Izgradit ćete jednu aplikaciju koja demonstrira oba šablona:

**Stateless chat** - Svaki zahtjev je neovisan. Model nema memoriju prethodnih poruka. To je šablon koji ste koristili u brzom početku.

**Stateful conversation** - Svaki zahtjev uključuje povijest razgovora. Model održava kontekst kroz više okretaja. To je ono što produkcijske aplikacije zahtijevaju.

## Preduvjeti

- Azure pretplata s pristupom Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Napomena:** Java, Maven, Azure CLI i Azure Developer CLI (azd) su predinstalirani u priloženom devcontaineru.

> **Napomena:** Ovaj modul koristi GPT-5.2 na Azure OpenAI. Implementacija se konfigurira automatski putem `azd up` — nemojte mijenjati naziv modela u kodu.

## Razumijevanje osnovnog problema

Jezik modeli su bez stanja (stateless). Svaki API poziv je neovisan. Ako pošaljete "Moje ime je John" i zatim pitate "Kako se zovem?", model nema pojma da ste se upravo predstavili. On tretira svaki zahtjev kao da je prvi razgovor koji ste ikada imali.

To je u redu za jednostavna pitanja i odgovore, ali beskorisno za prave aplikacije. Botovi za korisničku podršku trebaju pamtiti što ste im rekli. Osobni asistenti trebaju kontekst. Svaki višekratni razgovor zahtijeva memoriju.

Sljedeći dijagram prikazuje kontrast dvaju pristupa — lijevo je stateless poziv koji zaboravlja vaše ime; desno je stateful poziv podržan ChatMemory koji ga pamti.

<img src="../../../translated_images/hr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Razlika između stateless (neovisnih poziva) i stateful (svjestan konteksta) razgovora*

## Razumijevanje tokena

Prije nego što zaronite u razgovore, važno je razumjeti tokene - osnovne jedinice teksta koje jezični modeli obrađuju:

<img src="../../../translated_images/hr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Primjer kako se tekst dijeli na tokene - "Volim AI!" postaje 4 zasebne jedinice za obradu*

Tokeni su način na koji AI modeli mjere i obrađuju tekst. Riječi, interpunkcija pa čak i razmaci mogu biti tokene. Vaš model ima ograničenje koliko tokena može obraditi odjednom (400.000 za GPT-5.2, s do 272.000 ulaznih tokena i 128.000 izlaznih tokena). Razumijevanje tokena pomaže u upravljanju duljinom razgovora i troškovima.

## Kako memorija funkcionira

Chat memorija rješava problem stateless tako što održava povijest razgovora. Prije nego što pošaljete zahtjev modelu, okvir prethodno dodaje relevantne prethodne poruke. Kada pitate "Kako se zovem?", sustav zapravo šalje cijelu povijest razgovora, što modelu omogućuje da vidi da ste prije rekli "Moje ime je John."

LangChain4j pruža implementacije memorije koje to automatski upravljaju. Odaberete koliko poruka želite zadržati, a okvir upravlja kontekstualnim prozorom. Dijagram ispod pokazuje kako MessageWindowChatMemory održava klizni prozor nedavnih poruka.

<img src="../../../translated_images/hr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory održava klizni prozor nedavnih poruka, automatski odbacujući stare*

## Kako se koristi LangChain4j

Ovaj modul proširuje brzi početak integrirajući Spring Boot i dodajući memoriju razgovora. Evo kako se dijelovi slažu:

**Ovisnosti** - Dodajte dvije LangChain4j biblioteke:

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

**Chat model** - Konfigurirajte Azure OpenAI kao Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder čita vjerodajnice iz varijabli okoline postavljenih naredbom `azd up`. Postavljanje `baseUrl` na vaš Azure endpoint čini da OpenAI klijent radi s Azure OpenAI.

**Memorija razgovora** - Pratite povijest chata s MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Kreirajte memoriju s `withMaxMessages(10)` da zadržite zadnjih 10 poruka. Dodajte korisničke i AI poruke s tipiziranim omotačima: `UserMessage.from(text)` i `AiMessage.from(text)`. Dohvatite povijest s `memory.messages()` i pošaljite je modelu. Servis pohranjuje odvojene instance memorije po ID-u razgovora, omogućujući istovremeni chat više korisnika.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) i pitajte:
> - "Kako MessageWindowChatMemory odlučuje koje poruke odbaciti kada je prozor pun?"
> - "Mogu li implementirati prilagođeno spremište memorije koristeći bazu podataka umjesto memorije u RAM-u?"
> - "Kako bih dodao sažimanje za kompresiju stare povijesti razgovora?"

Stateless chat endpoint u potpunosti preskače memoriju - samo `chatModel.chat(prompt)` kao u brzom početku. Stateful endpoint dodaje poruke u memoriju, dohvaća povijest i uključuje taj kontekst sa svakim zahtjevom. Ista konfiguracija modela, različiti šabloni.

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

> **Napomena:** Ako naiđete na timeout grešku (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednostavno pokrenite `azd up` ponovno. Azure resursi mogu još biti u procesu postavljanja u pozadini, a ponovni pokušaj omogućuje dovršetak implementacije kad resursi dođu u terminalno stanje.

Ovo će:
1. Implementirati Azure OpenAI resurs s GPT-5.2 i modelima text-embedding-3-small
2. Automatski generirati `.env` datoteku u korijenu projekta s vjerodajnicama
3. Postaviti sve potrebne varijable okoline

**Imate problema s implementacijom?** Pogledajte [Infrastructure README](infra/README.md) za detaljne upute o rješavanju problema uključujući sukobe naziva poddomena, ručne korake implementacije kroz Azure Portal i savjete za konfiguraciju modela.

**Provjerite je li implementacija uspješna:**

**Bash:**
```bash
cat ../.env  # Trebao bi pokazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, itd.
```

> **Napomena:** Naredba `azd up` automatski generira `.env` datoteku. Ako je kasnije trebate ažurirati, možete ili ručno uređivati `.env` ili je ponovno generirati pokretanjem:
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

Provjerite postoji li `.env` datoteka u korijenskom direktoriju s Azure vjerodajnicama. Pokrenite ovo iz direktorija modula (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Trebalo bi prikazati AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Pokrenite aplikacije:**

**Opcija 1: Koristeći Spring Boot Dashboard (preporučeno za korisnike VS Code-a)**

Dev container uključuje Spring Boot Dashboard ekstenziju, koja pruža vizualno sučelje za upravljanje svim Spring Boot aplikacijama. Možete ga pronaći na Activity Bar-u s lijeve strane VS Code-a (potražite ikonu Spring Boot).

Iz Spring Boot Dashboard-a možete:
- Vidjeti sve dostupne Spring Boot aplikacije u radnom prostoru
- Pokrenuti/zaustaviti aplikacije jednim klikom
- Pregledavati zapisnike aplikacija u realnom vremenu
- Pratiti status aplikacija

Samo kliknite tipku za pokretanje pored "introduction" za pokretanje ovog modula, ili pokrenite sve module odjednom.

<img src="../../../translated_images/hr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard u VS Code-u — pokrenite, zaustavite i pratite sve module s jednog mjesta*

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

Obje skripte automatski učitavaju varijable okoline iz `.env` datoteke u korijenu i gradit će JAR-ove ako ne postoje.

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

Aplikacija pruža web sučelje s dvije implementacije chat-a jedna do druge.

<img src="../../../translated_images/hr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Nadzorna ploča prikazuje opcije za Simple Chat (stateless) i Conversational Chat (stateful)*

### Stateless chat (lijevi panel)

Isprobajte ovo prvo. Recite "Moje ime je John" pa odmah zatim "Kako se zovem?" Model se neće sjetiti jer je svaka poruka neovisna. Ovo demonstrira osnovni problem s integracijom jezičnih modela — nema konteksta razgovora.

<img src="../../../translated_images/hr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI se ne sjeća vašeg imena iz prethodne poruke*

### Stateful chat (desni panel)

Sada isprobajte isti niz ovdje. Recite "Moje ime je John" pa zatim "Kako se zovem?" Ovaj put se pamti. Razlika je MessageWindowChatMemory — održava povijest razgovora i uključuje je sa svakim zahtjevom. Ovako funkcionira produkcijski konverzacijski AI.

<img src="../../../translated_images/hr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI se sjeća vašeg imena s početka razgovora*

Oba panela koriste isti GPT-5.2 model. Jedina razlika je memorija. To jasno pokazuje što memorija donosi vašoj aplikaciji i zašto je ključna za stvarne slučajeve korištenja.

## Sljedeći koraci

**Sljedeći modul:** [02-prompt-engineering - Prompt Engineering s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigacija:** [← Prethodni: Modul 00 - Brzi početak](../00-quick-start/README.md) | [Natrag na početak](../README.md) | [Sljedeći: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj je dokument preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba se smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->