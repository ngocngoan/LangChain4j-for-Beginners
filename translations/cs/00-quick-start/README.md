# Modul 00: Rychlý start

## Obsah

- [Úvod](../../../00-quick-start)
- [Co je LangChain4j?](../../../00-quick-start)
- [Závislosti LangChain4j](../../../00-quick-start)
- [Požadavky](../../../00-quick-start)
- [Nastavení](../../../00-quick-start)
  - [1. Získejte svůj GitHub token](../../../00-quick-start)
  - [2. Nastavte svůj token](../../../00-quick-start)
- [Spusťte příklady](../../../00-quick-start)
  - [1. Základní chat](../../../00-quick-start)
  - [2. Vzory promptů](../../../00-quick-start)
  - [3. Volání funkcí](../../../00-quick-start)
  - [4. Otázky a odpovědi k dokumentům (Easy RAG)](../../../00-quick-start)
  - [5. Odpovědné AI](../../../00-quick-start)
- [Co každý příklad ukazuje](../../../00-quick-start)
- [Další kroky](../../../00-quick-start)
- [Řešení problémů](../../../00-quick-start)

## Úvod

Tento rychlý start je určen k tomu, abyste co nejrychleji začali pracovat s LangChain4j. Pokrývá základní principy budování AI aplikací s LangChain4j a GitHub Models. V následujících modulech použijete Azure OpenAI s LangChain4j k vytváření pokročilejších aplikací.

## Co je LangChain4j?

LangChain4j je Java knihovna, která zjednodušuje vytváření AI poháněných aplikací. Místo zabývání se HTTP klienty a parsováním JSON pracujete s čistými Java API.

„Řetězec“ v LangChain odkazuje na propojení více komponent – můžete propojit prompt s modelem a následně s parserem, nebo řetězit více AI volání, kde výstup jednoho slouží jako vstup pro další. Tento rychlý start se zaměřuje na základy před tím, než prozkoumáte složitější řetězce.

<img src="../../../translated_images/cs/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Propojení komponent v LangChain4j – stavební bloky se spojují pro vytváření výkonných AI workflow*

Použijeme tři hlavní komponenty:

**ChatModel** – Rozhraní pro interakci s AI modelem. Zavolejte `model.chat("prompt")` a získejte textovou odpověď. Používáme `OpenAiOfficialChatModel`, který pracuje s OpenAI-kompatibilními endpointy, jako jsou GitHub Models.

**AiServices** – Vytváří typově bezpečné rozhraní AI služeb. Definujte metody, anotujte je `@Tool` a LangChain4j se postará o orchestraci. AI automaticky volá vaše Java metody, když je to potřeba.

**MessageWindowChatMemory** – Udržuje historii konverzace. Bez toho je každý požadavek nezávislý. S tímto si AI pamatuje předchozí zprávy a udržuje kontext přes více kol.

<img src="../../../translated_images/cs/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektura LangChain4j – hlavní komponenty spolupracují na pohánění vašich AI aplikací*

## Závislosti LangChain4j

Tento rychlý start používá tři Maven závislosti v [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` poskytuje třídu `OpenAiOfficialChatModel`, která se připojuje k OpenAI-kompatibilním API. GitHub Models používá stejný formát API, takže není potřeba žádný speciální adaptér – stačí nastavit základní URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` zajišťuje automatické dělení dokumentů, vytváření embeddingů a vyhledávání, takže můžete stavět RAG aplikace bez ruční konfigurace každého kroku.

## Požadavky

**Používáte Dev Container?** Java a Maven jsou již nainstalovány. Potřebujete pouze GitHub Personal Access Token.

**Lokální vývoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instrukce níže)

> **Poznámka:** Tento modul používá `gpt-4.1-nano` z GitHub Models. Neměňte název modelu v kódu – je nakonfigurován tak, aby fungoval s dostupnými modely GitHubu.

## Nastavení

### 1. Získejte svůj GitHub token

1. Přejděte do [GitHub Nastavení → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikněte na „Generate new token“
3. Nastavte popisný název (např. „LangChain4j Demo“)
4. Nastavte platnost (doporučeno 7 dní)
5. V části „Account permissions“ najděte „Models“ a nastavte na „Read-only“
6. Klikněte na „Generate token“
7. Zkopírujte a uložte si token – už ho neuvidíte

### 2. Nastavte svůj token

**Možnost 1: Použití VS Code (doporučeno)**

Pokud používáte VS Code, přidejte svůj token do `.env` souboru v kořenovém adresáři projektu:

Pokud `.env` soubor neexistuje, zkopírujte `.env.example` jako `.env` nebo vytvořte nový `.env` soubor v kořenovém adresáři.

**Příklad souboru `.env`:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Pak stačí kliknout pravým tlačítkem na jakýkoliv demo soubor (např. `BasicChatDemo.java`) ve Stromu souborů a vybrat **„Run Java“** nebo použít spouštěcí konfigurace z panelu Run and Debug.

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

## Spusťte příklady

**Použití VS Code:** Stačí kliknout pravým tlačítkem na libovolný demo soubor ve Stromu souborů a vybrat **„Run Java“**, nebo použijte spouštěcí konfigurace v panelu Run and Debug (ujistěte se, že jste nejprve přidali token do `.env`).

**Použití Maven:** Alternativně můžete spustit z příkazového řádku:

### 1. Základní chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Vzory promptů

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ukazuje zero-shot, few-shot, chain-of-thought a role-based prompting.

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

### 4. Otázky a odpovědi k dokumentům (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pokládejte otázky ohledně svých dokumentů pomocí Easy RAG s automatickým embeddingem a vyhledáváním.

### 5. Odpovědné AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Uvidíte, jak bezpečnostní filtry AI blokují škodlivý obsah.

## Co každý příklad ukazuje

**Základní chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začněte zde, abyste viděli LangChain4j v jeho nejjednodušší podobě. Vytvoříte `OpenAiOfficialChatModel`, odešlete prompt pomocí `.chat()` a získáte odpověď. Tento příklad demonstruje základy: jak inicializovat modely s vlastními endpointy a API klíči. Jakmile tento vzor pochopíte, vše ostatní na něj navazuje.

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
> - „Jaké další parametry mohu nakonfigurovat v OpenAiOfficialChatModel.builder()?“
> - „Jak přidám streamování odpovědí místo čekání na kompletní odpověď?“

**Vytváření promptů** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teď když víte, jak mluvit s modelem, prozkoumáme, co mu říkáte. Toto demo používá stejnou konfiguraci modelu, ale ukazuje pět různých vzorů promptů. Vyzkoušejte zero-shot prompt pro přímé instrukce, few-shot prompt, které se učí z příkladů, chain-of-thought prompt, který odhaluje kroky uvažování, a role-based prompt, který nastavuje kontext. Uvidíte, jak stejný model dává dramaticky odlišné výsledky podle toho, jak formulujete požadavek.

Demo také demonstruje šablony promptů, což je silný způsob, jak vytvářet znovupoužitelné prompty s proměnnými.
Níže uvedený příklad ukazuje prompt využívající LangChain4j `PromptTemplate` pro vyplnění proměnných. AI odpoví na základě poskytnuté destinace a aktivity.

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
> - „Jaký je rozdíl mezi zero-shot a few-shot promptováním a kdy každý použít?“
> - „Jak parametr teploty ovlivňuje odpovědi modelu?“
> - „Jaké jsou techniky k zabránění prompt injection útokům v produkci?“
> - „Jak vytvořit znovupoužitelné objekty PromptTemplate pro běžné vzory?“

**Integrace nástrojů** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tady LangChain4j získává sílu. Použijete `AiServices` k vytvoření AI asistenta, který může volat vaše Java metody. Stačí anotovat metody s `@Tool("popis")` a LangChain4j se o vše ostatní postará – AI automaticky rozhoduje, kdy použít který nástroj podle toho, co uživatel požádá. Toto demonstruje volání funkcí, klíčovou techniku pro vytváření AI, která může provádět akce, ne jen odpovídat na otázky.

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
> - „Jak funguje anotace @Tool a co s ní LangChain4j dělá v pozadí?“
> - „Může AI volat více nástrojů za sebou k vyřešení složitých problémů?“
> - „Co se stane, když nástroj vyhodí výjimku – jak mám chybám předcházet?“
> - „Jak bych integroval reálné API místo tohoto příkladu kalkulačky?“

**Otázky a odpovědi k dokumentům (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Zde uvidíte RAG (retrieval-augmented generation) pomocí přístupu LangChain4j „Easy RAG“. Dokumenty se načtou, automaticky rozdělí a vloží do paměťového úložiště, poté obsahový vyhledávač poskytuje relevantní části AI při dotazování. AI odpovídá na základě vašich dokumentů, nikoli obecné znalosti modelu.

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
> - „Jak RAG zabraňuje halucinacím AI ve srovnání s použitím tréninkových dat modelu?“
> - „Jaký je rozdíl mezi tímto jednoduchým přístupem a vlastním RAG pipeline?“
> - „Jak bych škáloval řešení pro více dokumentů nebo větší znalostní bázi?“

**Odpovědné AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Budujte bezpečnost AI s obranou do hloubky. Toto demo ukazuje dvě vrstvy ochrany spolupracující dohromady:

**Část 1: LangChain4j Input Guardrails** – Blokuje nebezpečné prompty dříve, než dosáhnou LLM. Vytvořte vlastní „guardraily“, které kontrolují zakázaná klíčová slova nebo vzory. Tyto běží ve vašem kódu, takže jsou rychlé a zdarma.

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

**Část 2: Poskytovatelé bezpečnostních filtrů** – GitHub Models má vestavěné filtry, které zachytí to, co mohou vaše guardraily přehlédnout. Uvidíte tvrdé blokace (HTTP 400 chyby) při závažných porušeních a měkké odmítnutí, kdy AI zdvořile odmítne.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a zeptejte se:
> - „Co je InputGuardrail a jak vytvořím vlastní?“
> - „Jaký je rozdíl mezi tvrdým blokem a měkkým odmítnutím?“
> - „Proč používat guardraily i filtry poskytovatele dohromady?“

## Další kroky

**Další modul:** [01-introduction - Začínáme s LangChain4j a gpt-5 na Azure](../01-introduction/README.md)

---

**Navigace:** [← Zpět na hlavní stránku](../README.md) | [Další: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Řešení problémů

### První sestavení Maven

**Problém:** První `mvn clean compile` nebo `mvn package` trvá dlouho (10-15 minut)

**Příčina:** Maven musí při prvním sestavení stáhnout všechny závislosti projektu (Spring Boot, LangChain4j knihovny, Azure SDK atd.).

**Řešení:** Toto je normální chování. Následující sestavení budou mnohem rychlejší, protože závislosti jsou lokálně uložené v cache. Doba stahování závisí na rychlosti vašeho připojení.

### Syntaxe Maven příkazů v PowerShell

**Problém:** Maven příkazy končí chybou `Unknown lifecycle phase ".mainClass=..."`
**Příčina**: PowerShell interpretuje `=` jako operátor přiřazení proměnné, což narušuje syntaxi vlastností Maven

**Řešení**: Použijte operátor zastavení parsování `--%` před Maven příkazem:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` říká PowerShellu, aby předal všechny zbývající argumenty doslovně do Maven bez interpretace.

### Zobrazení emoji ve Windows PowerShell

**Problém**: Odpovědi AI zobrazují špatné znaky (např. `????` nebo `â??`) místo emoji v PowerShell

**Příčina**: Výchozí kódování PowerShellu nepodporuje UTF-8 emoji

**Řešení**: Spusťte tento příkaz před spuštěním Java aplikací:
```cmd
chcp 65001
```

Tím se v terminálu vynutí kódování UTF-8. Alternativně použijte Windows Terminal, který má lepší podporu Unicode.

### Ladění API volání

**Problém**: Chyby autentizace, limity rychlosti nebo neočekávané odpovědi od AI modelu

**Řešení**: Příklady obsahují `.logRequests(true)` a `.logResponses(true)` pro zobrazení API volání v konzoli. To pomáhá řešit chyby autentizace, limity rychlosti nebo neočekávané odpovědi. V produkci tyto příznaky odstraňte pro snížení šumu v logu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění nebo nesprávné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->