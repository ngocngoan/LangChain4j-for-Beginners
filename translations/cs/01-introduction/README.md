# Modul 01: Začínáme s LangChain4j

## Obsah

- [Video procházka](../../../01-introduction)
- [Co se naučíte](../../../01-introduction)
- [Požadavky](../../../01-introduction)
- [Porozumění základnímu problému](../../../01-introduction)
- [Porozumění tokenům](../../../01-introduction)
- [Jak funguje paměť](../../../01-introduction)
- [Jak toto používá LangChain4j](../../../01-introduction)
- [Nasazení infrastruktury Azure OpenAI](../../../01-introduction)
- [Spuštění aplikace lokálně](../../../01-introduction)
- [Použití aplikace](../../../01-introduction)
  - [Stateless Chat (levý panel)](../../../01-introduction)
  - [Stateful Chat (pravý panel)](../../../01-introduction)
- [Další kroky](../../../01-introduction)

## Video procházka

Sledujte tuto živou relaci, která vysvětluje, jak začít s tímto modulem: [Začínáme s LangChain4j - živá relace](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Co se naučíte

Pokud jste dokončili rychlý start, viděli jste, jak posílat příkazy a získávat odpovědi. To je základ, ale skutečné aplikace potřebují více. Tento modul vás naučí, jak budovat konverzační AI, která si pamatuje kontext a udržuje stav – rozdíl mezi jednorázovou ukázkou a produkční aplikací.

V průběhu tohoto průvodce budeme používat Azure OpenAI GPT-5.2, protože jeho pokročilé schopnosti rozumu činí chování různých vzorů jasnějším. Když přidáte paměť, budete jasně vidět rozdíl. To usnadňuje pochopit, co každý komponent přináší vaší aplikaci.

Postavíte jednu aplikaci, která demonstruje oba vzory:

**Stateless Chat** – Každý požadavek je nezávislý. Model si nepamatuje předchozí zprávy. Tento vzor jste použili v rychlém startu.

**Stateful Conversation** – Každý požadavek zahrnuje historii konverzace. Model udržuje kontext napříč více cykly. To je, co produkční aplikace vyžadují.

## Požadavky

- Předplatné Azure s přístupem k Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Poznámka:** Java, Maven, Azure CLI a Azure Developer CLI (azd) jsou předinstalovány v poskytovaném vývojovém kontejneru.

> **Poznámka:** Tento modul používá GPT-5.2 na Azure OpenAI. Nasazení je automaticky konfigurováno přes `azd up` – neměňte název modelu v kódu.

## Porozumění základnímu problému

Jazykové modely jsou bezstavové. Každé API volání je nezávislé. Pokud pošlete "Jmenuji se John" a pak se zeptáte "Jak se jmenuji?", model neví, že jste se právě představil. Každý požadavek zpracovává, jako by to byla první konverzace, kterou jste kdy vedli.

To je v pořádku pro jednoduché otázky a odpovědi, ale k ničemu pro skutečné aplikace. Zákaznické boty potřebují pamatovat, co jim jste řekli. Osobní asistenti potřebují kontext. Každá vícestupňová konverzace vyžaduje paměť.

<img src="../../../translated_images/cs/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Rozdíl mezi bezstavovými (nezávislými voláními) a stavovými (vědomými kontextu) konverzacemi*

## Porozumění tokenům

Než se pustíte do konverzací, je důležité porozumět tokenům – základním jednotkám textu, které jazykové modely zpracovávají:

<img src="../../../translated_images/cs/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Příklad, jak se text rozděluje na tokeny – "Miluji AI!" se stává 4 samostatnými jednotkami zpracování*

Tokeny jsou, jak AI modely měří a zpracovávají text. Slova, interpunkce a dokonce i mezery mohou být tokeny. Váš model má limit, kolik tokenů může zpracovat najednou (400 000 pro GPT-5.2, až 272 000 vstupních tokenů a 128 000 výstupních tokenů). Porozumění tokenům vám pomáhá řídit délku konverzace a náklady.

## Jak funguje paměť

Paměť v chatu řeší bezstavový problém uchováváním historie konverzace. Před odesláním požadavku modelu rámec přidává relevantní předchozí zprávy. Když se zeptáte "Jak se jmenuji?", systém skutečně posílá celou historii konverzace, což umožňuje modelu vidět, že jste předtím řekl "Jmenuji se John."

LangChain4j poskytuje implementace paměti, které toto automaticky zpracovávají. Vyvolíte, kolik zpráv chcete uložit, a rámec spravuje kontextové okno.

<img src="../../../translated_images/cs/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory udržuje posuvné okno posledních zpráv, automaticky odstraňuje staré*

## Jak toto používá LangChain4j

Tento modul rozšiřuje rychlý start integrací Spring Boot a přidáním paměti konverzace. Takto jednotlivé části fungují dohromady:

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

**Chat Model** – Nakonfigurujte Azure OpenAI jako Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder načítá přihlašovací údaje z proměnných prostředí nastavených příkazem `azd up`. Nastavení `baseUrl` na váš Azure endpoint umožňuje klientovi OpenAI pracovat s Azure OpenAI.

**Paměť konverzace** – Sledujte historii chatu s MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Vytvořte paměť s `withMaxMessages(10)`, aby se uchovalo posledních 10 zpráv. Přidejte uživatelské a AI zprávy s typovými obaly: `UserMessage.from(text)` a `AiMessage.from(text)`. Historii získáte pomocí `memory.messages()` a posíláte ji modelu. Služba ukládá samostatné instance paměti podle ID konverzace, což umožňuje současné chatování více uživatelů.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chatem:** Otevřete [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) a zeptejte se:
> - "Jak MessageWindowChatMemory rozhoduje, které zprávy odstranit, když je okno plné?"
> - "Mohu implementovat vlastní úložiště paměti pomocí databáze místo paměti v paměti?"
> - "Jak bych přidal shrnutí pro kompresi staré historie konverzace?"

Endpoint stateless chatu paměť úplně přeskočí – jednoduše `chatModel.chat(prompt)` jako v rychlém startu. Stateful endpoint přidává zprávy do paměti, získává historii a zahrnuje tento kontext s každým požadavkem. Stejná konfigurace modelu, různé vzory.

## Nasazení infrastruktury Azure OpenAI

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

> **Poznámka:** Pokud narazíte na chybu timeoutu (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), jednoduše znovu spusťte `azd up`. Zdroje Azure mohou být stále v procesu nasazování na pozadí, a opětovné spuštění umožní dokončení nasazení, jakmile zdroje dosáhnou konečného stavu.

Tímto:
1. Nasadíte Azure OpenAI zdroj s modely GPT-5.2 a text-embedding-3-small
2. Automaticky vygenerujete soubor `.env` v kořenové složce projektu s přihlašovacími údaji
3. Nastavíte všechny potřebné proměnné prostředí

**Máte potíže s nasazením?** Podívejte se do [infrastrukturního README](infra/README.md) pro podrobné řešení problémů včetně konfliktů názvů subdomén, manuálních kroků nasazení v Azure Portálu a návodu ke konfiguraci modelu.

**Ověření úspěšnosti nasazení:**

**Bash:**
```bash
cat ../.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY atd.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Mělo by zobrazit AZURE_OPENAI_ENDPOINT, API_KEY, atd.
```

> **Poznámka:** Příkaz `azd up` automaticky generuje `.env` soubor. Pokud jej potřebujete později upravit, můžete buď ručně upravit `.env` soubor, nebo jej znovu vygenerovat pomocí:
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

Ujistěte se, že soubor `.env` je v kořenovém adresáři s přihlašovacími údaji Azure:

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

Dev kontejner obsahuje rozšíření Spring Boot Dashboard, které poskytuje vizuální rozhraní pro správu všech Spring Boot aplikací. Najdete jej na liště aktivit na levé straně VS Code (hledat ikonu Spring Boot).

Z Spring Boot Dashboard můžete:
- Vidět všechny dostupné Spring Boot aplikace v workspace
- Spustit / zastavit aplikace jedním kliknutím
- Prohlížet logy aplikací v reálném čase
- Monitorovat stav aplikace

Jednoduše klikněte na tlačítko pro spuštění u "introduction" pro spuštění tohoto modulu, nebo spusťte všechny moduly najednou.

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
cd ..  # Ze základního adresáře
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

Oba skripty automaticky načtou proměnné prostředí z kořenového `.env` souboru a zkompilují JARy, pokud neexistují.

> **Poznámka:** Pokud dáváte přednost manuálnímu sestavení všech modulů před spuštěním:
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

## Použití aplikace

Aplikace poskytuje webové rozhraní se dvěma implementacemi chatu vedle sebe.

<img src="../../../translated_images/cs/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Panel zobrazující možnosti Simple Chat (bezstavový) a Conversational Chat (stavový)*

### Stateless Chat (levý panel)

Vyzkoušejte to nejprve. Zeptejte se "Jmenuji se John" a hned potom "Jak se jmenuji?" Model si nebude pamatovat, protože každá zpráva je nezávislá. Toto demonstruje základní problém integrace jazykových modelů – žádný kontext konverzace.

<img src="../../../translated_images/cs/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI si nepamatuje vaše jméno z předchozí zprávy*

### Stateful Chat (pravý panel)

Nyní vyzkoušejte stejnou sekvenci zde. Zeptejte se "Jmenuji se John" a pak "Jak se jmenuji?" Tentokrát si to pamatuje. Rozdíl je v MessageWindowChatMemory – udržuje historii konverzace a zahrnuje ji do každého požadavku. Takto funguje produkční konverzační AI.

<img src="../../../translated_images/cs/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI si pamatuje vaše jméno z dřívější části konverzace*

Oba panely používají stejný model GPT-5.2. Jediný rozdíl je v paměti. To jasně ukazuje, co paměť přináší vaší aplikaci a proč je zásadní pro reálné případy použití.

## Další kroky

**Další modul:** [02-prompt-engineering - Návrh příkazů s GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigace:** [← Předchozí: Modul 00 - Rychlý start](../00-quick-start/README.md) | [Zpět na hlavní stránku](../README.md) | [Další: Modul 02 - Návrh příkazů →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění nebo chybné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->