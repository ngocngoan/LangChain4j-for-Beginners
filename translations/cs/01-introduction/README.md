# Modul 01: Začínáme s LangChain4j

## Obsah

- [Video průvodce](../../../01-introduction)
- [Co se naučíte](../../../01-introduction)
- [Požadavky](../../../01-introduction)
- [Pochopení základního problému](../../../01-introduction)
- [Pochopení tokenů](../../../01-introduction)
- [Jak funguje paměť](../../../01-introduction)
- [Jak to používá LangChain4j](../../../01-introduction)
- [Nasazení infrastruktury Azure OpenAI](../../../01-introduction)
- [Spuštění aplikace lokálně](../../../01-introduction)
- [Používání aplikace](../../../01-introduction)
  - [Bezstavový chat (levý panel)](../../../01-introduction)
  - [Stavový chat (pravý panel)](../../../01-introduction)
- [Další kroky](../../../01-introduction)

## Video průvodce

Podívejte se na tuto živou relaci, která vysvětluje, jak začít s tímto modulem:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Co se naučíte

V rychlém startu jste použili GitHub Modely k odesílání promptů, volání nástrojů, sestavení RAG pipeline a testování ochranných mechanismů. Tyto ukázky předvedly, co je možné — nyní přecházíme k Azure OpenAI a GPT-5.2 a začínáme stavět aplikace pro produkční použití. Tento modul se zaměřuje na konverzační AI, která si pamatuje kontext a udržuje stav — koncepty, které ty rychlé starty používaly za scénou, ale nevysvětlily.

Budeme používat Azure OpenAI GPT-5.2 po celou dobu tohoto průvodce, protože jeho pokročilé schopnosti rozumění umožňují lépe vidět rozdíly v chování různých vzorů. Když přidáte paměť, jasně uvidíte rozdíl. To usnadňuje pochopení toho, co každá komponenta přináší do vaší aplikace.

Postavíte jednu aplikaci, která demonstruje oba vzory:

**Bezstavový chat** - Každý požadavek je nezávislý. Model si nepamatuje předchozí zprávy. Toto je vzor použitý v rychlém startu.

**Stavová konverzace** - Každý požadavek zahrnuje historii konverzace. Model udržuje kontext napříč více koly komunikace. Toto vyžadují produkční aplikace.

## Požadavky

- Azure předplatné s přístupem k Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) jsou předinstalované v dodaném devcontaineru.

> **Poznámka:** Tento modul používá GPT-5.2 na Azure OpenAI. Nasazení je automaticky nakonfigurováno pomocí `azd up` - neupravujte název modelu v kódu.

## Pochopení základního problému

Jazykové modely jsou bezstavové. Každé API volání je nezávislé. Pokud pošlete "Jmenuji se John" a pak se zeptáte "Jak se jmenuji?", model nemá ponětí, že jste se právě představili. Zachází s každým požadavkem, jako by to byla první konverzace, kterou jste s ním kdy měli.

To je v pořádku pro jednoduché otázky a odpovědi, ale k ničemu pro skutečné aplikace. Chatboti zákaznické podpory si musí pamatovat, co jste jim řekli. Osobní asistenti potřebují kontext. Každá vícekroková konverzace vyžaduje paměť.

Následující diagram kontrastuje oba přístupy — vlevo bezstavové volání, které zapomíná vaše jméno; vpravo stavové volání podporované ChatMemory, která si jméno pamatuje.

<img src="../../../translated_images/cs/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Rozdíl mezi bezstavovými (nezávislými voláními) a stavovými (uvědomělými o kontextu) konverzacemi*

## Pochopení tokenů

Než se ponoříme do konverzací, je důležité pochopit tokeny - základní jednotky textu, které jazykové modely zpracovávají:

<img src="../../../translated_images/cs/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Příklad jak je text rozdělen do tokenů - "Miluji AI!" se stává 4 samostatné jednotky zpracování*

Tokeny jsou měřítkem a jednotkami zpracování u AI modelů. Slova, interpunkce a dokonce mezery mohou být tokeny. Váš model má limit, kolik tokenů může zpracovat najednou (400 000 u GPT-5.2, s až 272 000 vstupními a 128 000 výstupními tokeny). Pochopení tokenů pomáhá řídit délku konverzace a náklady.

## Jak funguje paměť

Paměť chatu řeší bezstavový problém tím, že udržuje historii konverzace. Před odesláním vašeho požadavku modelu framework předřadí relevantní předchozí zprávy. Když se zeptáte "Jak se jmenuji?", systém ve skutečnosti pošle celou historii konverzace, což umožní modelu vidět, že jste dříve řekli "Jmenuji se John."

LangChain4j poskytuje implementace paměti, které toto automaticky zvládají. Vy si vyberete, kolik zpráv chcete uchovat, a framework spravuje kontextové okno. Níže je diagram, který ukazuje, jak MessageWindowChatMemory udržuje posuvné okno posledních zpráv.

<img src="../../../translated_images/cs/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory udržuje posuvné okno posledních zpráv, automaticky odhazuje staré*

## Jak to používá LangChain4j

Tento modul rozšiřuje rychlý start integrací Spring Boot a přidáním paměti konverzace. Tady je, jak k sobě části zapadají:

**Závislosti** - Přidejte dvě knihovny LangChain4j:

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

**Chatovací model** - Nakonfigurujte Azure OpenAI jako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder načítá přihlašovací údaje z proměnných prostředí nastavených `azd up`. Nastavení `baseUrl` na váš Azure endpoint umožňuje klientu OpenAI pracovat s Azure OpenAI.

**Paměť konverzace** - Sledujte historii chatu s MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvořte paměť pomocí `withMaxMessages(10)` pro uchování posledních 10 zpráv. Přidejte uživatelské a AI zprávy s typovanými wrappery: `UserMessage.from(text)` a `AiMessage.from(text)`. Historii získáte voláním `memory.messages()` a tu pošlete modelu. Servis uchovává oddělené instance paměti podle ID konverzace, což umožňuje více uživatelům simultánně chatovat.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chatem:** Otevřete [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a zeptejte se:
> - "Jak MessageWindowChatMemory rozhoduje, které zprávy zahodit, když je okno plné?"
> - "Mohu implementovat vlastní úložiště paměti pomocí databáze místo paměti v RAM?"
> - "Jak bych přidal shrnutí (sumarizaci) pro kompresi staré historie konverzace?"

Bezstavový chat endpoint paměť zcela přeskočí - pouze `chatModel.chat(prompt)`, jako v rychlém startu. Stavový endpoint přidává zprávy do paměti, získává historii a zahrnuje tento kontext do každého požadavku. Stejné nastavení modelu, různé vzory.

## Nasazení infrastruktury Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Vyberte předplatné a umístění (doporučeno eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vyberte předplatné a umístění (doporučeno eastus2)
```

> **Poznámka:** Pokud narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoduše spusťte `azd up` znovu. Azure zdroje mohou být stále v procesu provisioning na pozadí a opakování umožní dokončení nasazení, jakmile dosáhnou konečného stavu.

Toto provede:
1. Nasazení Azure OpenAI s GPT-5.2 a modely text-embedding-3-small
2. Automatické vytvoření `.env` souboru v kořenu projektu s přihlašovacími údaji
3. Nastavení všech požadovaných proměnných prostředí

**Máte problémy s nasazením?** Podívejte se do [Infrastructure README](infra/README.md) na podrobné řešení problémů, včetně konfliktů názvů poddomén, manuálních kroků nasazení přes Azure Portal a návodu na konfiguraci modelů.

**Ověřte úspěšnost nasazení:**

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY atd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, atd.
```

> **Poznámka:** Příkaz `azd up` automaticky generuje `.env` soubor. Pokud ho budete chtít později upravit, můžete buď `.env` soubor editovat ručně, nebo jej znovu vygenerovat spuštěním:
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


## Spuštění aplikace lokálně

**Ověřte nasazení:**

Ujistěte se, že `.env` soubor existuje v kořenovém adresáři s Azure přihlašovacími údaji. Spusťte to z adresáře modulu (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Spuštění aplikací:**

**Možnost 1: Použití Spring Boot Dashboard (doporučeno pro uživatele VS Code)**  

Dev kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete ho v liště aktivit vlevo ve VS Code (ikona Spring Boot).

Ve Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v workspace
- Spouštět/ukončovat aplikace jedním kliknutím
- Sledovat logy aplikací v reálném čase
- Monitorovat stav aplikací

Jednoduše klikněte na tlačítko přehrávání vedle „introduction“ pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard ve VS Code — spouštějte, ukončujte a sledujte všechny moduly na jednom místě*

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Ze kořenového adresáře
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Z kořenového adresáře
.\start-all.ps1
```

Nebo spusťte jen tento modul:

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

Oba skripty automaticky načtou proměnné prostředí z kořenového `.env` souboru a vytvoří JARy, pokud neexistují.

> **Poznámka:** Pokud chcete všechny moduly nejdříve sestavit ručně před spuštěním:
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

Otevřete ve svém prohlížeči http://localhost:8080.

**Pro zastavení:**

**Bash:**
```bash
./stop.sh  # Tento modul pouze
# Nebo
cd .. && ./stop-all.sh  # Všechny moduly
```

**PowerShell:**
```powershell
.\stop.ps1  # Pouze tento modul
# Nebo
cd ..; .\stop-all.ps1  # Všechny moduly
```


## Používání aplikace

Aplikace poskytuje webové rozhraní se dvěma implementacemi chatu vedle sebe.

<img src="../../../translated_images/cs/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Přehledová obrazovka ukazující jak Jednoduchý chat (bezstavový), tak Konverzační chat (stavový)*

### Bezstavový chat (levý panel)

Vyzkoušejte nejdříve toto. Řekněte „Jmenuji se John“ a hned poté „Jak se jmenuji?“ Model si nebude pamatovat, protože každá zpráva je nezávislá. To demonstruje základní problém integrace jazykového modelu – žádný kontext konverzace.

<img src="../../../translated_images/cs/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI si nepamatuje vaše jméno z předchozí zprávy*

### Stavový chat (pravý panel)

Nyní vyzkoušejte stejnou sekvenci zde. Řekněte „Jmenuji se John“ a pak „Jak se jmenuji?“ Tentokrát si to pamatuje. Rozdíl je MessageWindowChatMemory - udržuje historii konverzace a zahrnuje ji v každém požadavku. Takto funguje produkční konverzační AI.

<img src="../../../translated_images/cs/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si pamatuje vaše jméno z dřívější konverzace*

Oba panely používají stejný GPT-5.2 model. Jediný rozdíl je paměť. To jasně ukazuje, co paměť přináší vaší aplikaci a proč je nezbytná pro skutečné užití.

## Další kroky

**Další modul:** [02-prompt-engineering - Návrh promptů s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigace:** [← Předcházející: Modul 00 - Rychlý start](../00-quick-start/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 02 - Návrh promptů →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za závazný zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nejsme odpovědni za jakékoli nedorozumění nebo mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->