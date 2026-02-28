# Modul 01: Začínáme s LangChain4j

## Obsah

- [Video průvodce](../../../01-introduction)
- [Co se naučíte](../../../01-introduction)
- [Předpoklady](../../../01-introduction)
- [Pochopení základního problému](../../../01-introduction)
- [Pochopení tokenů](../../../01-introduction)
- [Jak funguje paměť](../../../01-introduction)
- [Jak toto používá LangChain4j](../../../01-introduction)
- [Nasazení infrastruktury Azure OpenAI](../../../01-introduction)
- [Spuštění aplikace lokálně](../../../01-introduction)
- [Používání aplikace](../../../01-introduction)
  - [Bezstavový chat (levý panel)](../../../01-introduction)
  - [Stavový chat (pravý panel)](../../../01-introduction)
- [Další kroky](../../../01-introduction)

## Video průvodce

Sledujte tento živý přenos, který vysvětluje, jak začít s tímto modulem:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Co se naučíte

Pokud jste dokončili rychlý start, viděli jste, jak odesílat výzvy a získávat odpovědi. To je základ, ale skutečné aplikace potřebují více. Tento modul vás naučí, jak vytvářet konverzační AI, která si pamatuje kontext a udržuje stav — rozdíl mezi jednorázovou ukázkou a produkční aplikací.

V průběhu tohoto průvodce budeme používat GPT-5.2 od Azure OpenAI, protože jeho pokročilé schopnosti dedukce výrazně zviditelňují chování různých vzorů. Když přidáte paměť, uvidíte rozdíl jasně. To usnadňuje pochopení, co každý komponent přináší vaší aplikaci.

Postavíte jednu aplikaci, která demonstruje oba vzory:

**Bezstavový chat** – Každý požadavek je nezávislý. Model si nepamatuje předchozí zprávy. To je vzor, který jste použili v rychlém startu.

**Stavová konverzace** – Každý požadavek zahrnuje historii konverzace. Model udržuje kontext přes více kol. To je to, co produkční aplikace vyžadují.

## Předpoklady

- Azure předplatné s přístupem k Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) jsou předinstalovány v dodaném vývojovém kontejneru.

> **Poznámka:** Tento modul používá GPT-5.2 na Azure OpenAI. Nasazení je automaticky nakonfigurováno přes `azd up` – neměňte název modelu v kódu.

## Pochopení základního problému

Jazykové modely jsou bezstavové. Každé API volání je nezávislé. Pokud napíšete "Jmenuji se John" a pak se zeptáte "Jak se jmenuji?", model netuší, že jste se právě představil. Každý požadavek bere, jako kdyby to byla vaše první konverzace vůbec.

To je v pořádku pro jednoduché otázky a odpovědi, ale k ničemu pro reálné aplikace. Boti zákaznické podpory si musí pamatovat, co jste jim řekli. Osobní asistenti potřebují kontext. Každá konverzace s více koly vyžaduje paměť.

<img src="../../../translated_images/cs/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Bezstavové vs Stavové konverzace" width="800"/>

*Rozdíl mezi bezstavovými (nezávislými voláními) a stavovými (vědomými kontextu) konverzacemi*

## Pochopení tokenů

Než se pustíte do konverzací, je důležité pochopit tokeny – základní jednotky textu, se kterými jazykové modely pracují:

<img src="../../../translated_images/cs/token-explanation.c39760d8ec650181.webp" alt="Vysvětlení tokenu" width="800"/>

*Příklad, jak je text rozbitý na tokeny – "Miluji AI!" se stává 4 samostatnými jednotkami zpracování*

Tokeny jsou způsob, jakým AI modely měří a zpracovávají text. Slova, interpunkce i mezery mohou být tokeny. Váš model má limit, kolik tokenů může zpracovat najednou (400 000 pro GPT-5.2, s až 272 000 vstupními a 128 000 výstupními tokeny). Pochopení tokenů vám pomůže řídit délku konverzace a náklady.

## Jak funguje paměť

Chatová paměť řeší bezstavový problém tím, že udržuje historii konverzace. Před odesláním požadavku modelu rámec připojí relevantní předchozí zprávy. Když se zeptáte "Jak se jmenuji?", systém v reálu pošle celou historii konverzace, což umožní modelu vidět, že jste předtím řekli "Jmenuji se John."

LangChain4j poskytuje implementace paměti, které toto automaticky zvládají. Vy si zvolíte, kolik zpráv chcete uchovat, a framework spravuje kontextové okno.

<img src="../../../translated_images/cs/memory-window.bbe67f597eadabb3.webp" alt="Koncept paměťového okna" width="800"/>

*MessageWindowChatMemory udržuje posuvné okno s nedávnými zprávami a automaticky vyhazuje staré*

## Jak toto používá LangChain4j

Tento modul rozšiřuje rychlý start integrací Spring Boot a přidáním paměti konverzace. Takto součásti fungují dohromady:

**Závislosti** – Přidejte dvě knihovny LangChain4j:

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

**Chat model** – Nakonfigurujte Azure OpenAI jako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder čte přihlašovací údaje z proměnných prostředí nastavených příkazem `azd up`. Nastavení `baseUrl` na váš Azure endpoint umožňuje klientovi OpenAI pracovat s Azure OpenAI.

**Paměť konverzace** – Sledujte historii chatu pomocí MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvořte paměť s `withMaxMessages(10)`, aby se uchovalo posledních 10 zpráv. Přidávejte zprávy uživatele a AI pomocí typovaných wrapperů: `UserMessage.from(text)` a `AiMessage.from(text)`. Historii získáte přes `memory.messages()` a pošlete ji modelu. Služba uchovává samostatné instance paměti podle ID konverzace, což umožňuje více uživatelům chatovat současně.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a zeptejte se:
> - "Jak MessageWindowChatMemory rozhoduje, které zprávy odhodit, když je okno plné?"
> - "Mohu implementovat vlastní ukládání paměti do databáze místo v paměti?"
> - "Jak bych přidal shrnutí k odstranění staré historie konverzace?"

Endpoint bezstavového chatu paměť úplně vynechává – jen `chatModel.chat(prompt)` jako v rychlém startu. Stavový endpoint přidává zprávy do paměti, načítá historii a zahrnuje ji ke každému požadavku. Stejná konfigurace modelu, různé vzory.

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

> **Poznámka:** Pokud narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoduše spusťte `azd up` znovu. Azure prostředky mohou stále probíhat na pozadí a opakování spustí nasazení, jakmile dosáhnou konečného stavu.

Toto provede:
1. Nasazení Azure OpenAI zdroje s modely GPT-5.2 a text-embedding-3-small
2. Automatickou generaci souboru `.env` v kořenu projektu s přihlašovacími údaji
3. Nastavení všech požadovaných proměnných prostředí

**Máte problémy s nasazením?** Podívejte se do [Infrastructure README](infra/README.md) pro podrobné řešení problémů včetně konfliktů názvů subdomény, manuálních kroků v Azure Portálu a pokynů pro konfiguraci modelu.

**Ověřte, že nasazení proběhlo úspěšně:**

**Bash:**
```bash
cat ../.env  # Měl by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, atd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, atd.
```

> **Poznámka:** Příkaz `azd up` automaticky generuje soubor `.env`. Pokud jej později potřebujete aktualizovat, můžete soubor `.env` upravit ručně nebo ho znovu vygenerovat spuštěním:
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

**Ověření nasazení:**

Ujistěte se, že soubor `.env` existuje v kořenovém adresáři s Azure přihlašovacími údaji:

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

Vývojový kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej v Activity Baru na levé straně VS Code (hledat ikonu Spring Boot).

Ve Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace ve workspace
- Spouštět/zastavovat aplikace jedním kliknutím
- Zobrazit logy aplikací v reálném čase
- Monitorovat stav aplikací

Stačí kliknout na tlačítko přehrávání vedle „introduction“ a spustit tento modul, nebo spustit všechny moduly najednou.

<img src="../../../translated_images/cs/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Použití shell skriptů**

Spusťte všechny webové aplikace (moduly 01-04):

**Bash:**
```bash
cd ..  # Z kořenového adresáře
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

Oba skripty automaticky načtou proměnné prostředí ze souboru `.env` v kořenovém adresáři a pokud JAR soubory neexistují, sestaví je.

> **Poznámka:** Pokud raději sestavíte všechny moduly ručně před spuštěním:
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

Otevřete http://localhost:8080 ve svém prohlížeči.

**Pro zastavení:**

**Bash:**
```bash
./stop.sh  # Pouze tento modul
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

Aplikace nabízí webové rozhraní se dvěma chat implementacemi vedle sebe.

<img src="../../../translated_images/cs/home-screen.121a03206ab910c0.webp" alt="Domovská obrazovka aplikace" width="800"/>

*Dashboard ukazující možnosti Simple Chat (bezstavový) a Conversational Chat (stavový)*

### Bezstavový chat (levý panel)

Vyzkoušejte nejdříve toto. Zeptejte se „Jmenuji se John“ a pak hned „Jak se jmenuji?“ Model si nebude pamatovat, protože každá zpráva je nezávislá. To ukazuje základní problém se základní integrací jazykového modelu – žádný kontext konverzace.

<img src="../../../translated_images/cs/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo bezstavového chatu" width="800"/>

*AI si nepamatuje vaše jméno z předchozí zprávy*

### Stavový chat (pravý panel)

Nyní vyzkoušejte stejnou sekvenci zde. Zeptejte se „Jmenuji se John“ a pak „Jak se jmenuji?“ Tentokrát si pamatuje. Rozdíl je v MessageWindowChatMemory – udržuje historii konverzace a zahrnuje ji do každého požadavku. Takto funguje produkční konverzační AI.

<img src="../../../translated_images/cs/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo stavového chatu" width="800"/>

*AI si pamatuje vaše jméno z dřívější části konverzace*

Oba panely používají stejný model GPT-5.2. Jediný rozdíl je v paměti. To jasně ukazuje, co paměť přináší vaší aplikaci a proč je nezbytná pro reálné případy použití.

## Další kroky

**Další modul:** [02-prompt-engineering - Návrh výzev s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigace:** [← Předchozí: Modul 00 - Rychlý start](../00-quick-start/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 02 - Návrh výzev →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Za jakákoliv nedorozumění nebo chybné výklady vyplývající z použití tohoto překladu neneseme odpovědnost.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->