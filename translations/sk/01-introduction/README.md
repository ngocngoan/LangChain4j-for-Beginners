# Modul 01: Začíname s LangChain4j

## Obsah

- [Video prehľad](../../../01-introduction)
- [Čo sa naučíte](../../../01-introduction)
- [Predpoklady](../../../01-introduction)
- [Pochopenie základného problému](../../../01-introduction)
- [Pochopenie tokenov](../../../01-introduction)
- [Ako funguje pamäť](../../../01-introduction)
- [Ako to používa LangChain4j](../../../01-introduction)
- [Nasadenie infraštruktúry Azure OpenAI](../../../01-introduction)
- [Spustenie aplikácie lokálne](../../../01-introduction)
- [Používanie aplikácie](../../../01-introduction)
  - [Stateless Chat (ľavý panel)](../../../01-introduction)
  - [Stateful Chat (pravý panel)](../../../01-introduction)
- [Ďalšie kroky](../../../01-introduction)

## Video prehľad

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Čo sa naučíte

V rýchlom štarte ste použili GitHub Models na posielanie promptov, volanie nástrojov, vytváranie RAG pipeline a testovanie ochranných opatrení. Tieto demo ukázali, čo je možné — teraz prejdeme na Azure OpenAI a GPT-5.2 a začneme stavať aplikácie na produkčnej úrovni. Tento modul sa sústreďuje na konverzačné AI, ktoré si pamätá kontext a udržiava stav — koncepty, ktoré použili rýchle demo, ale neboli vysvetlené.

Po celý tento návod budeme používať GPT-5.2 z Azure OpenAI, pretože jeho pokročilé schopnosti uvažovania robia správanie rôznych vzorov jasnejším. Keď pridáte pamäť, jasne uvidíte rozdiel. To uľahčuje pochopenie, čo každý komponent prináša vašej aplikácii.

Postavíte jednu aplikáciu, ktorá demonštruje oba vzory:

**Stateless Chat** — Každý dotaz je nezávislý. Model si nepamätá predchádzajúce správy. Toto je vzor, ktorý ste použili v rýchlom štarte.

**Stateful Conversation** — Každý dotaz obsahuje históriu konverzácie. Model udržiava kontext počas viacerých výmen. Toto je to, čo produkčné aplikácie vyžadujú.

## Predpoklady

- Predplatné Azure s prístupom k Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) sú predinštalované v poskytnutom devcontaineri.

> **Poznámka:** Tento modul používa GPT-5.2 na Azure OpenAI. Nasadenie je nakonfigurované automaticky cez `azd up` - nemodifikujte názov modelu v kóde.

## Pochopenie základného problému

Jazykové modely sú stateless. Každé API volanie je nezávislé. Ak pošlete "Volám sa John" a potom sa opýtate "Ako sa volám?", model nemá tušenie, že ste sa práve predstavili. Každý požiadavok spracováva, akoby to bola vaša prvá konverzácia.

To je v poriadku pre jednoduché otázky a odpovede, ale na reálne aplikácie nepoužiteľné. Chatboty zákazníckej podpory si musia pamätať, čo ste im povedali. Osobní asistenti potrebujú kontext. Každá viackolová konverzácia vyžaduje pamäť.

Nasledujúci diagram porovnáva oba prístupy — vľavo stateless volanie, ktoré zabúda vaše meno; vpravo stateful volanie podporované ChatMemory, ktoré si ho pamätá.

<img src="../../../translated_images/sk/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Rozdiel medzi stateless (nezávislé volania) a stateful (kontextovo uvedomelé) konverzácie*

## Pochopenie tokenov

Predtým, než sa pustíme do konverzácií, je dôležité pochopiť tokeny — základné jednotky textu, ktoré jazykové modely spracovávajú:

<img src="../../../translated_images/sk/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Príklad, ako sa text rozkladá na tokeny - "I love AI!" sa stáva 4 samostatné spracovateľské jednotky*

Tokeny sú spôsob, akým AI modely merajú a spracovávajú text. Slová, interpunkcia a dokonca aj medzery môžu byť tokeny. Váš model má limit, koľko tokenov môže spracovať naraz (400 000 pre GPT-5.2, s až 272 000 vstupnými tokenmi a 128 000 výstupnými tokenmi). Pochopenie tokenov vám pomôže spravovať dĺžku konverzácie a náklady.

## Ako funguje pamäť

Chat pamäť rieši problém stateless tým, že udržiava históriu konverzácie. Pred odoslaním požiadavku modelu framework doplní relevantné predchádzajúce správy. Keď sa opýtate "Ako sa volám?", systém skutočne pošle celú históriu konverzácie, čo umožní modelu vidieť, že ste predtým povedali "Volám sa John."

LangChain4j poskytuje implementácie pamäte, ktoré to automaticky zvládajú. Vy si vyberiete, koľko správ si chcete ponechať a framework spravuje kontextové okno. Nasledujúci diagram ukazuje, ako MessageWindowChatMemory udržiava posuvné okno nedávnych správ.

<img src="../../../translated_images/sk/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory udržiava posuvné okno nedávnych správ, automaticky vyhadzujúc staršie*

## Ako to používa LangChain4j

Tento modul rozširuje rýchly štart integráciou Spring Boot a pridávaním pamäte pre konverzácie. Takto do seba zapadajú komponenty:

**Závislosti** — Pridajte dve knižnice LangChain4j:

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

**Chat model** — Nakonfigurujte Azure OpenAI ako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder číta prihlasovacie údaje z environmentálnych premenných nastavených `azd up`. Nastavenie `baseUrl` na váš Azure endpoint spôsobí, že OpenAI klient bude pracovať s Azure OpenAI.

**Pamäť konverzácie** — Sledujte históriu chatu pomocou MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvorte pamäť s `withMaxMessages(10)` pre uloženie posledných 10 správ. Pridávajte správy používateľa a AI s typovanými wrappermi: `UserMessage.from(text)` a `AiMessage.from(text)`. Históriu získate cez `memory.messages()` a odošlete ju modelu. Služba uchováva samostatné inštancie pamäte pre každý ID konverzácie, čo umožňuje viacerým používateľom chatovať súčasne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a spýtajte sa:
> - "Ako MessageWindowChatMemory rozhoduje, ktoré správy zahodiť, keď je okno plné?"
> - "Môžem implementovať vlastné ukladanie pamäte pomocou databázy namiesto pamäte v RAM?"
> - "Ako by som pridal zhrnutie na kompresiu starej histórie konverzácie?"

Stateless chat endpoint úplne preskočí pamäť — len `chatModel.chat(prompt)` ako v rýchlom štarte. Stateful endpoint pridáva správy do pamäte, získava históriu a zahŕňa tento kontext pri každom požiadavku. Rovnaká konfigurácia modelu, rôzne vzory.

## Nasadenie infraštruktúry Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Vyberte predplatné a umiestnenie (odporúča sa eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vyberte predplatné a umiestnenie (odporúčané eastus2)
```

> **Poznámka:** Ak narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoducho spustite `azd up` znova. Azure zdroje môžu byť stále v procese provisioning, a opakovanie umožní nasadeniu dokončiť sa, keď zdroje dosiahnu konečný stav.

Toto vykoná:
1. Nasadí Azure OpenAI zdroj s GPT-5.2 a modelmi text-embedding-3-small
2. Automaticky vygeneruje `.env` súbor v koreňovom priečinku projektu s prihlasovacími údajmi
3. Nastaví všetky požadované environmentálne premenné

**Máte problémy s nasadením?** Pozrite si [Infrastructure README](infra/README.md) pre podrobné riešenie problémov vrátane konfliktov názvov subdomén, manuálneho nasadenia cez Azure Portal a usmernenia k konfigurácii modelov.

**Overte úspešnosť nasadenia:**

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by sa zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

> **Poznámka:** Príkaz `azd up` automaticky generuje `.env` súbor. Ak ho budete chcieť neskôr aktualizovať, môžete buď manuálne upraviť `.env` súbor, alebo ho znovu vygenerovať spustením:
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

**Overte nasadenie:**

Uistite sa, že `.env` súbor existuje v koreňovom adresári s Azure prihlasovacími údajmi. Spustite toto z adresára modulu (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Malo by zobrazovať AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustite aplikácie:**

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev container obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (ikonka Spring Boot).

Zo Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spustiť/zastaviť aplikácie
- V reálnom čase sledovať logy aplikácie
- Monitorovať stav aplikácií

Jednoducho kliknite na tlačidlo play vedľa „introduction“ na spustenie tohto modulu, alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard vo VS Code — spustite, zastavte a monitorujte všetky moduly z jedného miesta*

**Možnosť 2: Použitie shell skriptov**

Spustite všetky webové aplikácie (moduly 01-04):

**Bash:**
```bash
cd ..  # Z koreňového adresára
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Zo koreňového adresára
.\start-all.ps1
```

Alebo spustite len tento modul:

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

Oba skripty automaticky načítajú environmentálne premenné z koreňového `.env` súboru a vytvoria JAR súbory, ak neexistujú.

> **Poznámka:** Ak chcete pred spustením manuálne zostaviť všetky moduly:
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

Otvorte http://localhost:8080 vo vašom prehliadači.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Iba tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Tento modul iba
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie s dvoma chat implementáciami vedľa seba.

<img src="../../../translated_images/sk/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard ukazujúci možnosti Simple Chat (stateless) a Conversational Chat (stateful)*

### Stateless Chat (ľavý panel)

Vyskúšajte toto ako prvé. Povedzte "Volám sa John" a ihneď sa opýtajte "Ako sa volám?" Model si nepamätá, pretože každá správa je nezávislá. Toto demonštruje základný problém s integráciou jazykových modelov — žiadny kontext konverzácie.

<img src="../../../translated_images/sk/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI si nepamätá vaše meno zo predchádzajúcej správy*

### Stateful Chat (pravý panel)

Teraz vyskúšajte tú istú sekvenciu tu. Povedzte "Volám sa John" a potom "Ako sa volám?" Tentokrát si to pamätá. Rozdiel je MessageWindowChatMemory — udržiava históriu konverzácie a zahrňuje ju do každého požiadavku. Takto funguje produkčné konverzačné AI.

<img src="../../../translated_images/sk/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si pamätá vaše meno z predchádzajúcej konverzácie*

Oba panely používajú rovnaký model GPT-5.2. Jediný rozdiel je pamäť. To jasne ukazuje, čo pamäť prináša vašej aplikácii a prečo je nevyhnutná pre reálne použitie.

## Ďalšie kroky

**Ďalší modul:** [02-prompt-engineering - Inžinierstvo promptov s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 00 - Rýchly štart](../00-quick-start/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 02 - Inžinierstvo promptov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, prosím berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre dôležité informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo chybnú interpretáciu vyplývajúcu z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->