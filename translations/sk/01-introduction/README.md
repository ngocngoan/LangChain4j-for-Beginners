# Modul 01: Začíname s LangChain4j

## Obsah

- [Video prehľad](../../../01-introduction)
- [Čo sa naučíte](../../../01-introduction)
- [Predpoklady](../../../01-introduction)
- [Porozumenie základnému problému](../../../01-introduction)
- [Porozumenie tokenom](../../../01-introduction)
- [Ako funguje pamäť](../../../01-introduction)
- [Ako toto používa LangChain4j](../../../01-introduction)
- [Nasadenie infraštruktúry Azure OpenAI](../../../01-introduction)
- [Spustenie aplikácie lokálne](../../../01-introduction)
- [Používanie aplikácie](../../../01-introduction)
  - [Stateless chat (ľavý panel)](../../../01-introduction)
  - [Stateful chat (pravý panel)](../../../01-introduction)
- [Ďalšie kroky](../../../01-introduction)

## Video prehľad

Pozrite si túto živú reláciu, ktorá vysvetľuje, ako začať s týmto modulom:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Čo sa naučíte

Ak ste dokončili rýchly štart, videli ste, ako posielať podnety a dostávať odpovede. To je základ, ale skutočné aplikácie potrebujú viac. Tento modul vás naučí, ako vytvoriť konverzačnú AI, ktorá si pamätá kontext a udržuje stav - rozdiel medzi jednorazovou ukážkou a aplikáciou pripravenou na produkciu.

Používame GPT-5.2 od Azure OpenAI v celom tomto návode, pretože jeho pokročilé schopnosti uvažovania robia správanie rôznych vzorov zreteľnejším. Keď pridáte pamäť, jasne uvidíte rozdiel. To uľahčuje pochopiť, čo každý komponent prináša vašej aplikácii.

Vytvoríte jednu aplikáciu, ktorá demonštruje oba vzory:

**Stateless chat** - Každý požiadavok je nezávislý. Model si nepamätá predchádzajúce správy. Toto je vzor, ktorý ste použili v rýchlom štarte.

**Stateful konverzácia** - Každý požiadavok obsahuje históriu konverzácie. Model udržiava kontext v priebehu viacerých výmen. Toto je to, čo produkčné aplikácie vyžadujú.

## Predpoklady

- Azure predplatné s prístupom k Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) sú predinštalované v poskytnutom devcontaineri.

> **Poznámka:** Tento modul používa GPT-5.2 na Azure OpenAI. Nasadenie je nakonfigurované automaticky cez `azd up` - neupravujte názov modelu v kóde.

## Porozumenie základnému problému

Jazykové modely sú bezstavové. Každé volanie API je nezávislé. Ak pošlete "Volám sa John" a potom sa opýtate "Ako sa volám?", model nemá pojmy, že ste sa práve predstavili. Každý požiadavok spracúva, akoby to bol váš prvý rozhovor.

Pre jednoduché otázky a odpovede je to v poriadku, no pre skutočné aplikácie je to nepoužiteľné. Boti na zákaznícku podporu potrebujú pamätať si, čo ste im povedali. Osobní asistenti potrebujú kontext. Každá viackolová konverzácia vyžaduje pamäť.

<img src="../../../translated_images/sk/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Rozdiel medzi bezstavovými (nezávislými volaniami) a stavovými (s kontextom) konverzáciami*

## Porozumenie tokenom

Predtým, než sa pustíte do konverzácií, je dôležité pochopiť tokeny - základné jednotky textu, ktoré jazykové modely spracúvajú:

<img src="../../../translated_images/sk/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Príklad, ako sa text rozkladá na tokeny - "I love AI!" sa stáva 4 samostatnými spracovacími jednotkami*

Tokeny sú spôsob, ako AI modely merajú a spracúvajú text. Slová, interpunkcia a dokonca medzery môžu byť tokeny. Váš model má limit, koľko tokenov dokáže spracovať naraz (400 000 pre GPT-5.2, s až 272 000 vstupnými tokenmi a 128 000 výstupnými tokenmi). Porozumenie tokenom vám pomôže riadiť dĺžku konverzácie a náklady.

## Ako funguje pamäť

Pamäť chatu rieši problém bezstavovosti tým, že udržiava históriu konverzácie. Pred odoslaním vášho požiadavku modelu rámec pridá relevantné predchádzajúce správy. Keď sa opýtate "Ako sa volám?", systém v skutočnosti posiela celú históriu konverzácie, takže model vidí, že ste predtým povedali "Volám sa John."

LangChain4j poskytuje implementácie pamäte, ktoré to riešia automaticky. Vy si vyberiete, koľko správ chcete uchovať, a rámec spravuje kontextové okno.

<img src="../../../translated_images/sk/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory udržiava posuvné okno nedávnych správ, automaticky vyraďujúc staré*

## Ako toto používa LangChain4j

Tento modul rozširuje rýchly štart integráciou Spring Boot a pridaním pamäte konverzácie. Takto spolu jednotlivé časti fungujú:

**Závislosti** - Pridajte dve knižnice LangChain4j:

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

**Chat Model** - Nakonfigurujte Azure OpenAI ako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder číta prihlasovacie údaje z premenných prostredia nastavených pomocou `azd up`. Nastavenie `baseUrl` na váš Azure endpoint umožňuje klientovi OpenAI pracovať s Azure OpenAI.

**Pamäť konverzácie** - Sledujte históriu chatu pomocou MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvorte pamäť s `withMaxMessages(10)` na uchovanie posledných 10 správ. Pridávajte správy používateľa a AI s typovanými obalmi: `UserMessage.from(text)` a `AiMessage.from(text)`. Históriu získate pomocou `memory.messages()` a posielate ju modelu. Služba ukladá samostatné pamäte pre každý ID konverzácie, čo umožňuje viacerým používateľom chatovať súčasne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a opýtajte sa:
> - "Ako MessageWindowChatMemory rozhoduje, ktoré správy vyhodiť, keď je okno plné?"
> - "Môžem implementovať vlastné ukladanie pamäte pomocou databázy namiesto pamäte v RAM?"
> - "Ako by som pridal sumarizáciu na kompresiu starej histórie konverzácie?"

Stateless chat endpoint úplne vynecháva pamäť - len `chatModel.chat(prompt)`, ako v rýchlom štarte. Stateful endpoint pridáva správy do pamäti, získava históriu a zahŕňa tento kontext s každým požiadavkom. Rovnaké nastavenie modelu, rôzne vzory.

## Nasadenie infraštruktúry Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Vyberte predplatné a lokalitu (odporúča sa eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vyberte predplatné a lokalitu (odporúča sa eastus2)
```

> **Poznámka:** Ak narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoducho znova spustite `azd up`. Azure zdroje môžu byť stále v procese vytvárania na pozadí, a opakovanie umožní nasadenie dokončiť, keď zdroje dosiahnu konečný stav.

Toto vykoná:
1. Nasadenie zdroja Azure OpenAI s modelmi GPT-5.2 a text-embedding-3-small
2. Automatické vytvorenie súboru `.env` v koreňovom adresári projektu s prihlasovacími údajmi
3. Nastavenie všetkých požadovaných premenných prostredia

**Máte problémy s nasadením?** Pozrite si [README infraštruktúry](infra/README.md) pre podrobné riešenie problémov vrátane konfliktov mien subdomén, manuálneho nasadenia cez Azure Portal a návodov na konfiguráciu modelu.

**Skontrolujte úspešnosť nasadenia:**

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, atď.
```

> **Poznámka:** Príkaz `azd up` automaticky generuje súbor `.env`. Ak ho budete chcieť neskôr upraviť, môžete to urobiť manuálne alebo ho znovu vygenerovať spustením:
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

Uistite sa, že súbor `.env` existuje v koreňovom adresári s prihlasovacími údajmi Azure:

**Bash:**
```bash
cat ../.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Malo by zobraziť AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spustite aplikácie:**

**Možnosť 1: Použitie Spring Boot Dashboard (odporúčané pre používateľov VS Code)**

Dev container obsahuje rozšírenie Spring Boot Dashboard, ktoré poskytuje vizuálne rozhranie na správu všetkých Spring Boot aplikácií. Nájdete ho v Activity Bar na ľavej strane VS Code (ikonka Spring Boot).

Pomocou Spring Boot Dashboard môžete:
- Vidieť všetky dostupné Spring Boot aplikácie v pracovnom priestore
- Jedným kliknutím spustiť/zastaviť aplikácie
- Prezerať logy aplikácií v reálnom čase
- Monitorovať stav aplikácií

Jednoducho kliknite na tlačidlo pre spustenie vedľa "introduction" na spustenie tohto modulu, alebo spustite všetky moduly naraz.

<img src="../../../translated_images/sk/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnosť 2: Použitie shell skriptov**

Spustite všetky web aplikácie (moduly 01-04):

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

Oba skripty automaticky načítajú premenné prostredia zo súboru `.env` v koreňovom adresári a ak JAR súbory neexistujú, zostavia ich.

> **Poznámka:** Ak chcete všetky moduly zostaviť manuálne pred spustením:
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

Otvorte v prehliadači http://localhost:8080.

**Na zastavenie:**

**Bash:**
```bash
./stop.sh  # Len tento modul
# Alebo
cd .. && ./stop-all.sh  # Všetky moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Iba tento modul
# Alebo
cd ..; .\stop-all.ps1  # Všetky moduly
```

## Používanie aplikácie

Aplikácia poskytuje webové rozhranie s dvoma chat implementáciami vedľa seba.

<img src="../../../translated_images/sk/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard zobrazujúci možnosti Jednoduchý chat (bezstavový) a Konverzačný chat (stavový)*

### Stateless chat (ľavý panel)

Vyskúšajte toto ako prvé. Povedzte "Volám sa John" a hneď sa opýtajte "Ako sa volám?" Model si nepamätá, pretože každá správa je nezávislá. Toto demonštruje základný problém integrácie jazykových modelov - žiadny kontext konverzácie.

<img src="../../../translated_images/sk/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI si nepamätá vaše meno z predchádzajúcej správy*

### Stateful chat (pravý panel)

Teraz vyskúšajte rovnakú postupnosť tu. Povedzte "Volám sa John" a potom "Ako sa volám?" Tentokrát si model pamätá. Rozdiel robí MessageWindowChatMemory - udržiava históriu konverzácie a zahŕňa ju s každým požiadavkom. Takto funguje produkčná konverzačná AI.

<img src="../../../translated_images/sk/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si pamätá vaše meno z predchádzajúcej časti konverzácie*

Oba panely používajú rovnaký model GPT-5.2. Jediný rozdiel je pamäť. To jasne ukazuje, čo pamäť prináša vašej aplikácii a prečo je nevyhnutná pre reálne použitie.

## Ďalšie kroky

**Ďalší modul:** [02-prompt-engineering - Inžinierstvo podnetov s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigácia:** [← Predchádzajúci: Modul 00 - Rýchly štart](../00-quick-start/README.md) | [Späť na hlavnú stránku](../README.md) | [Ďalší: Modul 02 - Inžinierstvo podnetov →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Upozornenie**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, majte prosím na pamäti, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pri kritických informáciách sa odporúča profesionálny ľudský preklad. Nezodpovedáme za žiadne nedorozumenia alebo nesprávne výklady vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->