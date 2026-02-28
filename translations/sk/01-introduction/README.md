# Modul 01: Začíname s LangChain4j

## Obsah

- [Video prehliadka](../../../01-introduction)
- [Čo sa naučíte](../../../01-introduction)
- [Predpoklady](../../../01-introduction)
- [Pochopenie hlavného problému](../../../01-introduction)
- [Pochopenie tokenov](../../../01-introduction)
- [Ako funguje pamäť](../../../01-introduction)
- [Ako toto používa LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI infraštruktúry](../../../01-introduction)
- [Spustenie aplikácie lokálne](../../../01-introduction)
- [Použitie aplikácie](../../../01-introduction)
  - [Bezstavový chat (ľavý panel)](../../../01-introduction)
  - [Stavový chat (pravý panel)](../../../01-introduction)
- [Ďalšie kroky](../../../01-introduction)

## Video prehliadka

Sledujte tento živý záznam, ktorý vysvetľuje, ako začať s týmto modulom: [Začíname s LangChain4j - živé vysielanie](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Čo sa naučíte

Ak ste dokončili rýchly štart, videli ste, ako posielať prompta a dostať odpovede. To je základ, ale skutočné aplikácie potrebujú viac. Tento modul vás naučí, ako vytvoriť konverzačné AI, ktoré si pamätá kontext a udržiava stav - rozdiel medzi jednorazovým demo a produkčne pripravenou aplikáciou.

Budeme používať Azure OpenAI GPT-5.2 počas celého návodu, pretože jeho pokročilé schopnosti uvažovania robia správanie rôznych vzorov zreteľnejším. Keď pridáte pamäť, jasne uvidíte rozdiel. To uľahčuje pochopenie, čo každý komponent prináša do vašej aplikácie.

Postavíte jednu aplikáciu, ktorá demonštruje oba vzory:

**Bezstavový chat** - Každý požiadavok je nezávislý. Model si nepamätá predchádzajúce správy. Toto je vzor, ktorý ste použili v rýchlom starte.

**Stavová konverzácia** - Každý požiadavok obsahuje históriu konverzácie. Model udržiava kontext cez viacero kôl komunikácie. Toto je to, čo produkčné aplikácie vyžadujú.

## Predpoklady

- Azure predplatné s prístupom k Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) sú predinštalované v poskytnutom devcontainery.

> **Poznámka:** Tento modul používa GPT-5.2 na Azure OpenAI. Deployment sa konfiguruje automaticky cez `azd up` – nemeníte názov modelu v kóde.

## Pochopenie hlavného problému

Jazykové modely sú bezstavové. Každé volanie API je nezávislé. Ak pošlete „Volám sa John“ a potom sa opýtate „Ako sa volám?“, model netuší, že ste sa práve predstavili. Každý požiadavok spracúva, akoby to bola vaša prvá konverzácia.

To je v poriadku pre jednoduché otázky a odpovede, ale na skutočné aplikácie je to nepoužiteľné. Helpdesk boti potrebujú pamätať, čo ste im povedali. Osobní asistenti potrebujú kontext. Každá viackolová konverzácia vyžaduje pamäť.

<img src="../../../translated_images/sk/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Rozdiel medzi bezstavovou (nezávislé volania) a stavovou (s uvedomením kontextu) konverzáciou*

## Pochopenie tokenov

Pred zabořením sa do konverzácií je dôležité porozumieť tokenom - základným jednotkám textu, ktoré jazykové modely spracúvajú:

<img src="../../../translated_images/sk/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Príklad, ako sa text rozkladá na tokeny – "I love AI!" sa stáva 4 samostatnými jednotkami na spracovanie*

Tokény sú spôsob, akým AI modely merajú a spracovávajú text. Slová, interpunkcia a dokonca aj medzery môžu byť tokeny. Váš model má limit, koľko tokenov dokáže spracovať naraz (400 000 pre GPT-5.2, s až 272 000 vstupnými tokenmi a 128 000 výstupnými tokenmi). Pochopenie tokenov vám pomáha spravovať dĺžku konverzácie a náklady.

## Ako funguje pamäť

Pamäť chatu rieši problém bezstavovosti tým, že udržiava históriu konverzácie. Pred odoslaním vášho požiadavku modelu framework pripojí relevantné predchádzajúce správy. Keď sa opýtate „Ako sa volám?“, systém v skutočnosti pošle celú históriu konverzácie, takže model vidí, že ste predtým povedali „Volám sa John.“

LangChain4j poskytuje implementácie pamäte, ktoré to automaticky riešia. Vy si vyberiete, koľko správ sa má uchovávať, a framework spravuje kontextové okno.

<img src="../../../translated_images/sk/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory udržiava posuvné okno nedávnych správ, automaticky odstraňuje staré*

## Ako toto používa LangChain4j

Tento modul rozširuje rýchly štart integráciou Spring Boot a pridaním pamäte konverzácie. Takto do seba jednotlivé časti zapadajú:

**Závislosti** – Pridajte dve knižnice LangChain4j:

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

**Chat model** – Nakonfigurujte Azure OpenAI ako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder číta prihlasovacie údaje z environmentálnych premenných nastavených `azd up`. Nastavenie `baseUrl` na váš Azure endpoint umožňuje klientovi OpenAI pracovať s Azure OpenAI.

**Pamäť konverzácie** – Sledujte históriu chatu pomocou MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvorte pamäť s `withMaxMessages(10)`, aby sa uchovávalo posledných 10 správ. Pridávajte správy používateľa a AI pomocou typovaných wrapperov: `UserMessage.from(text)` a `AiMessage.from(text)`. Históriu získate metódou `memory.messages()` a pošlete ju modelu. Služba ukladá samostatné inštancie pamäte podľa ID konverzácie, čo umožňuje viacerým používateľom chatovať súčasne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a spýtajte sa:
> - „Ako MessageWindowChatMemory rozhoduje, ktoré správy zahodiť, keď je okno plné?“
> - „Môžem implementovať vlastné úložisko pamäte pomocou databázy namiesto pamäte v RAM?“
> - „Ako by som pridal sumarizáciu na zhutnenie starej histórie konverzácie?“

Bezstavový chat endpoint úplne preskočí pamäť – len `chatModel.chat(prompt)` ako v rýchlom starte. Stavový endpoint pridáva správy do pamäte, načítava históriu a zahŕňa ju s každým požiadavkom. Rovnaká konfigurácia modelu, odlišné vzory.

## Deploy Azure OpenAI infraštruktúry

**Bash:**
```bash
cd 01-introduction
azd up  # Vyberte predplatné a umiestnenie (odporúča sa eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vyberte predplatné a umiestnenie (odporúča sa eastus2)
```

> **Poznámka:** Ak narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoducho spustite `azd up` znova. Azure zdroje sa môžu ešte provisionovať na pozadí a opakovanie spustenia umožní dokončiť nasadenie po dosiahnutí konečného stavu.

Toto vykoná:
1. Nasadenie Azure OpenAI zdroja s modelmi GPT-5.2 a text-embedding-3-small
2. Automatické vygenerovanie súboru `.env` v koreňovom adresári projektu s prihlasovacími údajmi
3. Nastavenie všetkých potrebných environmentálnych premenných

**Máte problém s deploymentom?** Pozrite si [Infrastructure README](infra/README.md) pre podrobné riešenie problémov vrátane konfliktov mien subdomén, manuálneho deployu cez Azure Portal a konfigurácie modelu.

**Overenie úspechu deploymentu:**

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mala by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

> **Poznámka:** Príkaz `azd up` automaticky generuje súbor `.env`. Ak ho potrebujete neskôr upraviť, môžete buď manuálne editovať `.env` alebo ho vygenerovať znova spustením:
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

## Spustenie aplikácie lokálne

**Overte deployment:**

Uistite sa, že koreňový adresár obsahuje `.env` súbor s Azure prihlasovacími údajmi:

**Bash:**
```bash
cat ../.env  # Mal by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustite aplikácie:**

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev container obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v paneli aktivít na ľavej strane VS Code (ikona Spring Boot).

Z Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spustiť/zastaviť aplikácie
- Zobraziť logy aplikácií v reálnom čase
- Monitorovať stav aplikácií

Jednoducho kliknite na tlačidlo prehrávania vedľa "introduction" pre spustenie tohto modulu, alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnosť 2: Použitie shell skriptov**

Spustenie všetkých webových aplikácií (moduly 01-04):

**Bash:**
```bash
cd ..  # Z koreňového adresára
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z koreňového adresára
.\start-all.ps1
```

Alebo spustenie iba tohto modulu:

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

Oba skripty automaticky načítajú environmentálne premenne zo súboru `.env` v koreňovom adresári a zostavia JAR súbory, ak ešte neexistujú.

> **Poznámka:** Ak chcete všetky moduly najskôr manuálne zostaviť pred spustením:
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

Otvorte si v prehliadači http://localhost:8080.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Len tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Použitie aplikácie

Aplikácia poskytuje webové rozhranie s dvoma chat implementáciami vedľa seba.

<img src="../../../translated_images/sk/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard ukazuje možnosti Jednoduchého chatu (bezstavový) a Konverzačného chatu (stavový)*

### Bezstavový chat (ľavý panel)

Vyskúšajte najskôr toto. Napíšte „Volám sa John“ a okamžite sa opýtajte „Ako sa volám?“ Model si to nezapamätá, pretože každá správa je nezávislá. Toto demonštruje základný problém integrácie jazykového modelu – chýba kontext konverzácie.

<img src="../../../translated_images/sk/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI si nepamätá vaše meno z predchádzajúcej správy*

### Stavový chat (pravý panel)

Teraz skúste rovnakú sekvenciu tu. Napíšte „Volám sa John“ a potom „Ako sa volám?“ Tentokrát si to pamätá. Rozdielom je MessageWindowChatMemory – udržiava históriu konverzácie a pridáva ju ku každému požiadavku. Takto funguje produkčné konverzačné AI.

<img src="../../../translated_images/sk/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si pamätá vaše meno zo skoršej časti konverzácie*

Oba panely používajú rovnaký GPT-5.2 model. Jediný rozdiel je pamäť. To jasne ukazuje, čo pamäť prináša do vašej aplikácie a prečo je nevyhnutná pre reálne prípady použitia.

## Ďalšie kroky

**Ďalší modul:** [02-prompt-engineering - Návrh promptov s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 00 - Rýchly štart](../00-quick-start/README.md) | [Späť na hlavné](../README.md) | [Ďalej: Modul 02 - Návrh promptov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Výhrada zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď usilovne usilujeme o presnosť, majte prosím na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne výklady vyplývajúce z používania tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->