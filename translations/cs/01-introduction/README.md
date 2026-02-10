# Modul 01: Začínáme s LangChain4j

## Obsah

- [Co se naučíte](../../../01-introduction)
- [Požadavky](../../../01-introduction)
- [Pochopení hlavního problému](../../../01-introduction)
- [Pochopení tokenů](../../../01-introduction)
- [Jak funguje paměť](../../../01-introduction)
- [Jak to používá LangChain4j](../../../01-introduction)
- [Nasazení Azure OpenAI infrastruktury](../../../01-introduction)
- [Spuštění aplikace lokálně](../../../01-introduction)
- [Použití aplikace](../../../01-introduction)
  - [Bezustavý chat (levý panel)](../../../01-introduction)
  - [Stavový chat (pravý panel)](../../../01-introduction)
- [Další kroky](../../../01-introduction)

## Co se naučíte

Pokud jste dokončili rychlý start, viděli jste, jak posílat prompta a získávat odpovědi. To je základ, ale skutečné aplikace potřebují víc. Tento modul vás naučí, jak vytvořit konverzační AI, která si pamatuje kontext a udržuje stav – rozdíl mezi jednorázovou ukázkou a aplikací připravenou do produkce.

Během této příručky budeme používat Azure OpenAI GPT-5.2, protože jeho pokročilé schopnosti uvažování dělají chování různých vzorců přehlednější. Když přidáte paměť, rozdíl bude jasně viditelný. To usnadní pochopení, co každá komponenta přináší vaší aplikaci.

Postavíte jednu aplikaci, která demonstruje oba vzory:

**Bezustavý chat** – Každý požadavek je nezávislý. Model si nepamatuje předchozí zprávy. Toto je vzor, který jste použili v rychlém startu.

**Stavová konverzace** – Každý požadavek zahrnuje historii konverzace. Model si udržuje kontext přes více výměn. To je to, co požadují produkční aplikace.

## Požadavky

- Předplatné Azure s přístupem k Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) jsou předinstalovány v poskytnutém devcontaineru.

> **Poznámka:** Tento modul používá GPT-5.2 na Azure OpenAI. Nasazení je nakonfigurováno automaticky pomocí `azd up` – neměňte název modelu v kódu.

## Pochopení hlavního problému

Jazykové modely jsou bezstavové. Každé API volání je nezávislé. Pokud pošlete "Jmenuji se John" a pak se zeptáte "Jak se jmenuji?", model nemá tušení, že jste se právě představili. Každý požadavek zpracovává, jako by to byla první konverzace, kterou jste kdy vedli.

To je v pořádku pro jednoduché dotazy a odpovědi, ale k ničemu pro skutečné aplikace. Chatboti zákaznické podpory musí pamatovat, co jste jim řekli. Osobní asistenti potřebují kontext. Každá vícekolová konverzace vyžaduje paměť.

<img src="../../../translated_images/cs/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Bezustavé vs Stavové konverzace" width="800"/>

*Rozdíl mezi bezstavými (nezávislými voláními) a stavovými (vědomými kontextu) konverzacemi*

## Pochopení tokenů

Než se ponoříte do konverzací, je důležité pochopit tokeny – základní jednotky textu, které jazykové modely zpracovávají:

<img src="../../../translated_images/cs/token-explanation.c39760d8ec650181.webp" alt="Vysvětlení tokenu" width="800"/>

*Příklad, jak se text rozkládá na tokeny – "I love AI!" se stane 4 samostatnými zpracovatelnými jednotkami*

Tokeny jsou způsob, jak modely AI měří a zpracovávají text. Slova, interpunkce a dokonce i mezery mohou být tokeny. Váš model má limit na počet tokenů, které může zpracovat najednou (400 000 pro GPT-5.2, s maximálně 272 000 vstupními tokeny a 128 000 výstupními tokeny). Pochopení tokenů vám pomůže řídit délku konverzace a náklady.

## Jak funguje paměť

Paměť chatu řeší problém bezstavovosti tím, že si udržuje historii konverzace. Před odesláním vašeho požadavku modelu rámec předřadí relevantní předchozí zprávy. Když se zeptáte "Jak se jmenuji?", systém ve skutečnosti pošle celou historii konverzace, což umožní modelu vidět, že jste předtím řekli "Jmenuji se John."

LangChain4j poskytuje implementace paměti, které toto automaticky zajišťují. Vy určíte, kolik zpráv si pamatujete, a rámec spravuje velikost kontextového okna.

<img src="../../../translated_images/cs/memory-window.bbe67f597eadabb3.webp" alt="Koncept paměťového okna" width="800"/>

*MessageWindowChatMemory udržuje posuvné okno posledních zpráv, automaticky odstraňuje staré*

## Jak to používá LangChain4j

Tento modul rozšiřuje rychlý start o integraci Spring Boot a přidání paměti konverzace. Zde je, jak kousky do sebe zapadají:

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

Builder načítá přihlašovací údaje ze systémových proměnných nastavených příkazem `azd up`. Nastavením `baseUrl` na váš Azure endpoint umožní klientovi OpenAI pracovat s Azure OpenAI.

**Paměť konverzace** – Sledujte historii chatu pomocí MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvořte paměť s `withMaxMessages(10)`, která uchovává posledních 10 zpráv. Přidávejte zprávy uživatele a AI s typovými obálkami: `UserMessage.from(text)` a `AiMessage.from(text)`. Historii načtěte přes `memory.messages()` a pošlete ji modelu. Služba ukládá oddělené instance paměti podle ID konverzace, což umožňuje současný chat více uživatelů.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chatem:** Otevřete [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a zeptejte se:
> - "Jak MessageWindowChatMemory rozhoduje, které zprávy odstranit, když je okno plné?"
> - "Mohu implementovat vlastní ukládání paměti pomocí databáze místo v paměti?"
> - "Jak přidat shrnutí pro zhuštění staré historie konverzace?"

Bezustavý chat endpoint paměť zcela přeskočí – použije jen `chatModel.chat(prompt)`, jako v rychlém startu. Stavový endpoint přidává zprávy do paměti, načítá historii a zahrnuje tento kontext do každého požadavku. Stejná konfigurace modelu, odlišné vzory.

## Nasazení Azure OpenAI infrastruktury

**Bash:**
```bash
cd 01-introduction
azd up  # Vyberte předplatné a lokaci (doporučeno eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Vyberte předplatné a umístění (doporučeno eastus2)
```

> **Poznámka:** Pokud narazíte na chybu časového limitu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoduše spusťte `azd up` znovu. Zdroje Azure se mohou stále provisionovat na pozadí a opakování umožní dokončení nasazení jakmile zdroje dosáhnou konečného stavu.

To udělá:
1. Nasadí Azure OpenAI zdroj s GPT-5.2 a modely text-embedding-3-small
2. Automaticky vytvoří soubor `.env` v kořenovém adresáři projektu s přihlašovacími údaji
3. Nastaví všechny potřebné proměnné prostředí

**Máte problémy s nasazením?** Pro podrobné řešení problémů viz [Infrastructure README](infra/README.md), včetně konfliktů názvů subdomén, manuálního nasazení přes Azure Portal a pokynů k nastavení modelů.

**Ověřte, zda nasazení proběhlo úspěšně:**

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY atd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY atd.
```

> **Poznámka:** Příkaz `azd up` automaticky vytvoří soubor `.env`. Pokud ho budete později chtít aktualizovat, můžete buď upravit `.env` ručně nebo jej znovu vygenerovat příkazem:
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

Ujistěte se, že v kořenovém adresáři existuje soubor `.env` s Azure přihlašovacími údaji:

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

Dev kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej v postranním panelu na levé straně VS Code (ikona Spring Boot).

Ve Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v pracovním prostoru
- Jedním kliknutím spustit/zastavit aplikace
- Sledovat logy aplikace v reálném čase
- Monitorovat stav aplikace

Stačí kliknout na tlačítko přehrávání vedle "introduction" pro spuštění tohoto modulu nebo spustit všechny moduly najednou.

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

Oba skripty automaticky načítají proměnné prostředí ze souboru `.env` v kořeni a zkompilují JARy, pokud neexistují.

> **Poznámka:** Pokud raději chcete před spuštěním ručně sestavit všechny moduly:
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

Otevřete http://localhost:8080 ve vašem prohlížeči.

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

## Použití aplikace

Aplikace nabízí webové rozhraní se dvěma implementacemi chatu vedle sebe.

<img src="../../../translated_images/cs/home-screen.121a03206ab910c0.webp" alt="Úvodní obrazovka aplikace" width="800"/>

*Dashboard zobrazující možnosti Jednoduchý chat (bezstavý) a Konverzační chat (stavový)*

### Bezustavý chat (levý panel)

Vyzkoušejte nejprve tento. Zeptejte se "Jmenuji se John" a pak hned "Jak se jmenuji?" Model si nepamatuje, protože každá zpráva je nezávislá. Toto demonstruje hlavní problém základní integrace jazykového modelu – žádný kontext konverzace.

<img src="../../../translated_images/cs/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo bezstavého chatu" width="800"/>

*AI si nepamatuje vaše jméno z předchozí zprávy*

### Stavový chat (pravý panel)

Nyní vyzkoušejte stejnou sekvenci zde. Zeptejte se "Jmenuji se John" a pak "Jak se jmenuji?" Tentokrát si pamatuje. Rozdíl je v MessageWindowChatMemory – ta udržuje historii konverzace a zahrnuje ji do každého požadavku. Takto funguje produkční konverzační AI.

<img src="../../../translated_images/cs/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo stavového chatu" width="800"/>

*AI si pamatuje vaše jméno z dřívější části konverzace*

Oba panely používají stejný model GPT-5.2. Jediný rozdíl je paměť. To jasně ukazuje, co paměť do vaší aplikace přináší a proč je nezbytná pro reálné případy použití.

## Další kroky

**Další modul:** [02-prompt-engineering - Prompt Engineering s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigace:** [← Předchozí: Modul 00 - Rychlý start](../00-quick-start/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za autoritativní zdroj. Pro zásadní informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění či mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->