# Modul 00: Rychlý start

## Obsah

- [Úvod](../../../00-quick-start)
- [Co je LangChain4j?](../../../00-quick-start)
- [Závislosti LangChain4j](../../../00-quick-start)
- [Požadavky](../../../00-quick-start)
- [Nastavení](../../../00-quick-start)
  - [1. Získejte svůj GitHub token](../../../00-quick-start)
  - [2. Nastavte svůj token](../../../00-quick-start)
- [Spuštění příkladů](../../../00-quick-start)
  - [1. Základní chat](../../../00-quick-start)
  - [2. Vzory pro promptování](../../../00-quick-start)
  - [3. Volání funkcí](../../../00-quick-start)
  - [4. Dotazy k dokumentům (Easy RAG)](../../../00-quick-start)
  - [5. Odpovědná AI](../../../00-quick-start)
- [Co každý příklad ukazuje](../../../00-quick-start)
- [Další kroky](../../../00-quick-start)
- [Řešení problémů](../../../00-quick-start)

## Úvod

Tento rychlý start je určen k tomu, abyste co nejrychleji začali pracovat s LangChain4j. Pokrývá absolutní základy vytváření AI aplikací s LangChain4j a GitHub Models. V dalších modulech přejdete na Azure OpenAI a GPT-5.2 a hlouběji se ponoříte do jednotlivých konceptů.

## Co je LangChain4j?

LangChain4j je knihovna pro Javu, která zjednodušuje vytváření aplikací poháněných AI. Místo práce s HTTP klienty a parsováním JSON pracujete s čistými Java API.

„Řetězení“ (chain) v LangChain znamená propojení více komponent – můžete propojit prompt s modelem a následujícím parsrem, nebo několik AI volání, kde výstup jednoho se stane vstupem dalšího. Tento rychlý start se zaměřuje na základy před tím, než se pustíte do složitějších řetězců.

<img src="../../../translated_images/cs/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Řetězení komponent v LangChain4j – stavební bloky se spojují a vytvářejí silné AI workflow*

Použijeme tři základní komponenty:

**ChatModel** – rozhraní pro interakce s AI modelem. Zavolejte `model.chat("prompt")` a získejte odpověď jako řetězec. Používáme `OpenAiOfficialChatModel`, který pracuje s OpenAI kompatibilními endpointy, jako jsou GitHub Models.

**AiServices** – vytváří typově bezpečná rozhraní AI služeb. Definujte metody, označte je `@Tool` a LangChain4j se postará o orchestraci. AI automaticky volá vaše Java metody, když je to potřeba.

**MessageWindowChatMemory** – uchovává historii konverzace. Bez ní je každý požadavek nezávislý. S ní si AI pamatuje předchozí zprávy a udržuje kontext napříč několika výměnami.

<img src="../../../translated_images/cs/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektura LangChain4j – základní komponenty spolupracují, aby poháněly vaše AI aplikace*

## Závislosti LangChain4j

Tento rychlý start používá tři závislosti Maven v [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

Modul `langchain4j-open-ai-official` poskytuje třídu `OpenAiOfficialChatModel`, která se připojuje k OpenAI kompatibilním API. GitHub Models používá stejný formát API, takže není potřeba žádný speciální adaptér – stačí nastavit základní URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` zajišťuje automatické dělení dokumentů, vytváření embeddingů a vyhledávání, takže můžete tvořit RAG aplikace bez manuální konfigurace každého kroku.

## Požadavky

**Používáte Dev Container?** Java a Maven jsou už nainstalované. Potřebujete jen GitHub Personal Access Token.

**Lokální vývoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (návod níže)

> **Poznámka:** Tento modul používá `gpt-4.1-nano` z GitHub Models. Neměňte název modelu v kódu – je nastaven pro práci s dostupnými modely na GitHubu.

## Nastavení

### 1. Získejte svůj GitHub token

1. Přejděte na [GitHub Nastavení → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikněte na „Generate new token“ (vygenerovat nový token)
3. Zadejte popisný název (např. "LangChain4j Demo")
4. Nastavte expiraci (doporučeno 7 dní)
5. V sekci „Account permissions“ najděte „Models“ a nastavte na „Read-only“
6. Klikněte na „Generate token“
7. Zkopírujte a uložte váš token – znovu ho neuvidíte

### 2. Nastavte svůj token

**Možnost 1: Použití VS Code (doporučeno)**

Pokud používáte VS Code, přidejte svůj token do souboru `.env` v kořenovém adresáři projektu:

Pokud soubor `.env` neexistuje, zkopírujte `.env.example` do `.env` nebo vytvořte nový `.env` v kořenové složce projektu.

**Příklad souboru `.env`:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Pak můžete jednoduše kliknout pravým tlačítkem na jakýkoliv demo soubor (např. `BasicChatDemo.java`) v Průzkumníku a vybrat **„Run Java”** nebo použít konfigurační profily v panelu Run and Debug.

**Možnost 2: Použití terminálu**

Nastavte token jako proměnnou prostředí:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Spuštění příkladů

**Ve VS Code:** Stačí kliknout pravým tlačítkem na libovolný demo soubor v Průzkumníku a zvolit **„Run Java”**, nebo použít spouštěcí konfigurace v panelu Run and Debug (nejprve musíte přidat token do `.env`).

**Pomocí Maven:** Alternativně spusťte z příkazové řádky:

### 1. Základní chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Vzory pro promptování

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ukažte zero-shot, few-shot, chain-of-thought a role-based promptování.

### 3. Volání funkcí

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automaticky volá vaše Java metody, když je to potřeba.

### 4. Dotazy k dokumentům (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pokládejte dotazy k dokumentům pomocí Easy RAG s automatickým embeddingem a vyhledáváním.

### 5. Odpovědná AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Ukažte, jak AI bezpečnostní filtry blokují škodlivý obsah.

## Co každý příklad ukazuje

**Základní chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začněte zde, abyste viděli LangChain4j v jeho nejjednodušší podobě. Vytvoříte `OpenAiOfficialChatModel`, odešlete prompt s `.chat()` a získáte odpověď. Tento příklad ukazuje základy: jak inicializovat modely s vlastním endpointem a API klíčem. Jakmile tuto šablonu pochopíte, vše ostatní na ní staví.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) a zeptejte se:
> - „Jak přepnu z GitHub Models na Azure OpenAI v tomto kódu?“
> - „Jaké další parametry mohu konfigurovat v OpenAiOfficialChatModel.builder()?“
> - „Jak přidám streamingové odpovědi místo čekání na kompletní odpověď?“

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nyní, když víte, jak s modelem mluvit, prozkoumejme, co mu říkáte. Toto demo používá stejnou konfiguraci modelu, ale ukazuje pět různých vzorů promptování. Vyzkoušejte zero-shot prompty pro přímé instrukce, few-shot prompty, které se učí z příkladů, chain-of-thought prompty odkrývající kroky uvažování, a role-based prompty, které nastavují kontext. Uvidíte, jak stejný model poskytuje dramaticky odlišné výsledky podle toho, jak si formulujete dotaz.

Demo také ukazuje prompt šablony, což je mocný způsob, jak tvořit znovupoužitelné prompty s proměnnými.
Níže uvedený příklad ukazuje prompt využívající LangChain4j `PromptTemplate` pro vyplnění proměnných. AI odpoví podle dané destinace a aktivity.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) a zeptejte se:
> - „Jaký je rozdíl mezi zero-shot a few-shot promptováním a kdy každý z nich použít?“
> - „Jak parametr temperature ovlivňuje odpovědi modelu?“
> - „Jaké jsou techniky prevence prompt injection útoků v produkci?“
> - „Jak mohu vytvořit znovupoužitelné PromptTemplate objekty pro běžné vzory?“

**Integrace nástrojů** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Zde LangChain4j ukazuje svoji sílu. Použijete `AiServices` k vytvoření AI asistenta, který může volat vaše Java metody. Stačí označit metody `@Tool("popis")` a LangChain4j se postará o zbytek – AI automaticky rozhodne, kdy použít který nástroj podle dotazu uživatele. Toto ukazuje volání funkcí, klíčovou techniku pro budování AI, která umí nejen odpovídat, ale také provádět akce.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) a zeptejte se:
> - „Jak funguje anotace @Tool a co s ní LangChain4j dělá na pozadí?“
> - „Může AI volat více nástrojů za sebou, aby vyřešila složité problémy?“
> - „Co když nástroj vyhodí výjimku – jak mám chytat chyby?“
> - „Jak bych integroval reálné API místo tohoto příkladu kalkulačky?“

**Dotazy k dokumentům (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Zde uvidíte RAG (retrieval-augmented generation) pomocí „Easy RAG“ přístupu LangChain4j. Dokumenty se načítají, automaticky se dělí a embedují do paměťové databáze, poté obsahový vyhledávač dodá relevantní části AI v době dotazu. AI odpovídá na základě vašich dokumentů, ne jen obecné znalosti.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) a zeptejte se:
> - „Jak RAG zabraňuje halucinacím AI ve srovnání s využitím tréninkových dat modelu?“
> - „Jaký je rozdíl mezi tímto jednoduchým přístupem a vlastním RAG pipeline?“
> - „Jak bych škáloval tento přístup pro více dokumentů nebo větší znalostní báze?“

**Odpovědná AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Budujte bezpečnost AI s obranou do hloubky. Toto demo ukazuje dvě vrstvy ochrany, které spolupracují:

**Část 1: LangChain4j Input Guardrails** – Blokují nebezpečné prompty dřív, než dorazí k LLM. Vytvářejte vlastní strážní pravidla, která kontrolují zakázaná klíčová slova nebo vzory. Běží ve vašem kódu, takže jsou rychlá a zdarma.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Část 2: Bezpečnostní filtry poskytovatele** – GitHub Models má zabudované filtry, které zachytí to, co vaše guardrails mohou přehlédnout. Uvidíte tvrdé blokace (chyby HTTP 400) u vážných porušení i měkká odmítnutí, kde AI zdvořile odmítá.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a zeptejte se:
> - „Co je InputGuardrail a jak vytvořit vlastní?“
> - „Jaký je rozdíl mezi tvrdým blokem a měkkým odmítnutím?“
> - „Proč používat guardrails i filtry poskytovatele společně?“

## Další kroky

**Další modul:** [01-introduction - Začínáme s LangChain4j](../01-introduction/README.md)

---

**Navigace:** [← Zpět na hlavní stránku](../README.md) | [Další: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Řešení problémů

### První sestavení pomocí Maven

**Problém**: První `mvn clean compile` nebo `mvn package` trvá dlouho (10-15 minut)

**Příčina**: Maven si při prvním sestavení stáhne všechny závislosti projektu (Spring Boot, knihovny LangChain4j, Azure SDK atd.).

**Řešení**: Je to normální. Následující sestavení budou výrazně rychlejší, protože závislosti jsou uloženy v mezipaměti lokálně. Doba stahování závisí na rychlosti vašeho připojení.

### Syntaxe příkazů Maven v PowerShell

**Problém**: Maven příkazy končí chybou `Unknown lifecycle phase ".mainClass=..."`
**Příčina**: PowerShell interpretuje `=` jako operátor přiřazení proměnné, což narušuje syntaxi vlastností Maven

**Řešení**: Použijte před příkaz Maven operátor ukončení parsování `--%`:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` říká PowerShellu, aby předal všechny následující argumenty doslovně do Maven bez interpretace.

### Zobrazování emoji ve Windows PowerShell

**Problém**: Odpovědi AI zobrazují v PowerShellu nesmyslné znaky (např. `????` nebo `â??`) místo emoji

**Příčina**: Výchozí kódování PowerShellu nepodporuje UTF-8 emoji

**Řešení**: Spusťte tento příkaz před spuštěním Java aplikací:
```cmd
chcp 65001
```

Tím se v terminálu vynutí kódování UTF-8. Alternativně použijte Windows Terminal, který má lepší podporu Unicode.

### Ladění volání API

**Problém**: Chybové hlášky autentizace, limity volání nebo neočekávané odpovědi od modelu AI

**Řešení**: Příklady obsahují `.logRequests(true)` a `.logResponses(true)`, které zobrazují volání API v konzoli. To pomáhá při řešení chyb autentizace, limitů volání nebo neočekávaných odpovědí. V produkci tyto příznaky odstraňte, aby byla konzole méně zahlcená.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby či nepřesnosti. Původní dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro kritické informace doporučujeme profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění či mylné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->