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
  - [2. Vzory promptů](../../../00-quick-start)
  - [3. Volání funkcí](../../../00-quick-start)
  - [4. Otázky a odpovědi k dokumentům (RAG)](../../../00-quick-start)
  - [5. Odpovědná AI](../../../00-quick-start)
- [Co každý příklad ukazuje](../../../00-quick-start)
- [Další kroky](../../../00-quick-start)
- [Řešení problémů](../../../00-quick-start)

## Úvod

Tento rychlý start je určen k co nejrychlejšímu seznámení s LangChain4j. Pokrývá naprosté základy tvorby AI aplikací s LangChain4j a GitHub Models. V dalších modulech použijete Azure OpenAI s LangChain4j k vývoji pokročilejších aplikací.

## Co je LangChain4j?

LangChain4j je Java knihovna, která zjednodušuje vytváření AI aplikací. Místo práce s HTTP klienty a JSON parsováním pracujete s čistými Java API.

Slovo „chain“ v LangChain znamená propojování více komponent – například můžete propojit prompt s modelem a parserem, nebo spojit více AI volání, kde výstup jednoho je vstupem dalšího. Tento rychlý start se zaměřuje na základy, než se pustíte do složitějších řetězců.

<img src="../../../translated_images/cs/langchain-concept.ad1fe6cf063515e1.webp" alt="Koncept propojení LangChain4j" width="800"/>

*Propojování komponent v LangChain4j – stavební bloky spojující se pro vytvoření výkonných AI workflow*

Použijeme tři hlavní komponenty:

**ChatLanguageModel** – Rozhraní pro interakce s AI modelem. Zavolejte `model.chat("prompt")` a získejte textovou odpověď. Používáme `OpenAiOfficialChatModel`, která pracuje s endpointy kompatibilními s OpenAI, jako GitHub Models.

**AiServices** – Vytváří typově bezpečná rozhraní AI služeb. Definujte metody, anotujte je `@Tool` a LangChain4j se postará o orchestraci. AI automaticky volá vaše Java metody podle potřeby.

**MessageWindowChatMemory** – Udržuje historii konverzace. Bez této komponenty je každý dotaz nezávislý. S ní si AI pamatuje předchozí zprávy a udržuje kontext v průběhu několika kol.

<img src="../../../translated_images/cs/architecture.eedc993a1c576839.webp" alt="Architektura LangChain4j" width="800"/>

*Architektura LangChain4j – hlavní komponenty spolupracují na pohánění vašich AI aplikací*

## Závislosti LangChain4j

Tento rychlý start používá dvě závislosti Maven v souboru [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

Modul `langchain4j-open-ai-official` poskytuje třídu `OpenAiOfficialChatModel`, která se připojuje k API kompatibilním s OpenAI. GitHub Models používá stejný formát API, takže není potřeba žádný speciální adaptér – stačí nasměrovat základní URL na `https://models.github.ai/inference`.

## Požadavky

**Používáte Dev Container?** Java a Maven už jsou nainstalované. Potřebujete jen GitHub Personal Access Token.

**Lokální vývoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (návod níže)

> **Poznámka:** Tento modul používá `gpt-4.1-nano` z GitHub Models. Neměňte jméno modelu v kódu – je to nastaveno pro spolupráci s dostupnými modely GitHubu.

## Nastavení

### 1. Získejte svůj GitHub token

1. Přejděte na [GitHub Nastavení → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klikněte na „Generate new token“
3. Zadejte popisný název (např. „LangChain4j Demo“)
4. Nastavte dobu platnosti (doporučeno 7 dní)
5. V sekci „Account permissions“ najděte „Models“ a nastavte na „Read-only“
6. Klikněte na „Generate token“
7. Zkopírujte a uložte token – už ho znovu neuvidíte

### 2. Nastavte svůj token

**Možnost 1: Použití VS Code (doporučeno)**

Pokud používáte VS Code, přidejte token do souboru `.env` v kořenovém adresáři projektu:

Pokud soubor `.env` neexistuje, zkopírujte `.env.example` do `.env` nebo si vytvořte nový `.env` v kořenovém adresáři.

**Ukázka souboru `.env`:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Poté stačí kliknout pravým tlačítkem na libovolný demo soubor (např. `BasicChatDemo.java`) v Průzkumníku a vybrat **"Run Java"**, nebo použít konfigurace spuštění v panelu Run and Debug.

**Možnost 2: Použití Terminálu**

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

**Ve VS Code:** Klikněte pravým tlačítkem na libovolný demo soubor v Průzkumníku a vyberte **"Run Java"**, nebo použijte konfigurace spuštění v Run and Debug (nezapomeňte nejdříve přidat token do `.env`).

**Pomocí Maven:** Alternativně můžete spustit z příkazové řádky:

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

AI automaticky volá vaše Java metody podle potřeby.

### 4. Otázky a odpovědi k dokumentům (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pokládejte otázky ohledně obsahu v `document.txt`.

### 5. Odpovědná AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Ukažte si, jak bezpečnostní filtry AI blokují škodlivý obsah.

## Co každý příklad ukazuje

**Základní chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začněte zde, abyste viděli LangChain4j v nejjednodušší podobě. Vytvoříte `OpenAiOfficialChatModel`, pošlete prompt pomocí `.chat()`, a dostanete odpověď. Toto ukazuje základy: jak inicializovat modely s vlastními endpointy a API klíči. Jakmile tento vzor pochopíte, vše další na něm staví.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) a zeptejte se:
> - „Jak přepnout z GitHub Models na Azure OpenAI v tomto kódu?“
> - „Jaké další parametry mohu nastavit v OpenAiOfficialChatModel.builder()?“
> - „Jak přidám streamované odpovědi místo čekání na kompletní odpověď?“

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teď když víte, jak s modelem komunikovat, pojďme prozkoumat, co mu říkáte. Toto demo používá stejný model, ale ukazuje pět různých vzorů promptů. Vyzkoušejte zero-shot (přímé instrukce), few-shot (učení na příkladech), chain-of-thought (postupné uvažování) a role-based prompting (nastavení kontextu rolí). Uvidíte, jak stejný model dává dramaticky odlišné výsledky podle toho, jak je dotaz formulován.

Demo také ukazuje prompt šablony, které jsou silným nástrojem pro vytváření znovupoužitelných promptů s proměnnými.
Níže uvedený příklad ukazuje prompt s použitím LangChain4j `PromptTemplate` k vyplnění proměnných. AI odpoví na základě zadané destinace a aktivity.

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
> - „Jaký je rozdíl mezi zero-shot a few-shot promptováním a kdy použít který?“
> - „Jak parametr temperature ovlivňuje odpovědi modelu?“
> - „Jaké jsou techniky, jak zabránit prompt injection útokům v produkci?“
> - „Jak mohu vytvořit znovupoužitelné objekty PromptTemplate pro běžné vzory?“

**Integrace nástrojů** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tady LangChain4j skutečně sílí. Použijete `AiServices` k vytvoření AI asistenta, který může volat vaše Java metody. Stačí anotovat metody `@Tool("popis")` a LangChain4j se postará o zbytek – AI automaticky rozhoduje, kdy který nástroj použít podle požadavků uživatele. Ukazuje to volání funkcí, klíčovou techniku pro vytváření AI schopné podniknout akce, nejen odpovědět na otázky.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) a zeptejte se:
> - „Jak funguje anotace @Tool a co s ní LangChain4j dělá na pozadí?“
> - „Může AI volat více nástrojů za sebou k řešení složitých problémů?“
> - „Co se stane, když nástroj vyhodí výjimku – jak mám řešit chyby?“
> - „Jak integrovat skutečné API místo této kalkulačky?“

**Otázky a odpovědi k dokumentům (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Zde uvidíte základy RAG (retrieval-augmented generation). Místo spoléhaní se na tréninková data modelu nahráváte obsah z [`document.txt`](../../../00-quick-start/document.txt) a zahrnujete ho do promptu. AI odpovídá na základě vašeho dokumentu, nikoliv obecné znalosti. To je první krok k vytváření systémů, které pracují s vlastními daty.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Poznámka:** Tento jednoduchý přístup načítá celý dokument do promptu. U velkých souborů (>10KB) překročíte limity kontextu. Modul 03 se zabývá dělením na části a vektorovým vyhledáváním pro produkční RAG systémy.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) a zeptejte se:
> - „Jak RAG zabraňuje halucinacím AI ve srovnání s použitím tréninkových dat modelu?“
> - „Jaký je rozdíl mezi tímto jednoduchým přístupem a použitím vektorových embeddingů pro hledání?“
> - „Jak to škálovat na více dokumentů nebo větší znalostní báze?“
> - „Jaké jsou nejlepší postupy pro strukturování promptu, aby AI používala jen poskytnutý kontext?“

**Odpovědná AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Budujte bezpečnost AI s více vrstvami ochrany. Toto demo ukazuje dvě vrstvy ochrany spolupracující dohromady:

**Část 1: Vstupní bezpečnostní pravidla LangChain4j** – Blokují nebezpečné prompty dřív, než dosáhnou LLM. Vytvářejte vlastní pravidla, která kontrolují zakázaná klíčová slova nebo vzory. Běží ve vašem kódu, takže jsou rychlá a zdarma.

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

**Část 2: Bezpečnostní filtry poskytovatele** – GitHub Models mají zabudované filtry, které zachytí to, co vaše pravidla mohou přehlédnout. Uvidíte tvrdé blokace (chyby HTTP 400) u závažných porušení a měkké odmítnutí, kde AI zdvořile odmítá.

> **🤖 Vyzkoušejte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otevřete [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a zeptejte se:
> - „Co je InputGuardrail a jak si vytvořit vlastní?“
> - „Jaký je rozdíl mezi tvrdou blokací a měkkým odmítnutím?“
> - „Proč používat zároveň guardrails i filtry poskytovatele?“

## Další kroky

**Další modul:** [01-introduction - Začínáme s LangChain4j a gpt-5 na Azure](../01-introduction/README.md)

---

**Navigace:** [← Zpět na hlavní stránku](../README.md) | [Další: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Řešení problémů

### První build Maven

**Problém:** První `mvn clean compile` nebo `mvn package` trvá dlouho (10–15 minut)

**Příčina:** Maven musí stáhnout všechny závislosti projektu (Spring Boot, LangChain4j knihovny, Azure SDK atd.) při prvním buildě.

**Řešení:** To je normální chování. Následující buildy budou mnohem rychlejší, protože závislosti jsou uložené lokálně. Délka stahování závisí na rychlosti vaší sítě.
### Syntaxe příkazů PowerShell Maven

**Problém**: Příkazy Maven selhávají s chybou `Unknown lifecycle phase ".mainClass=..."`

**Příčina**: PowerShell interpretuje `=` jako operátor přiřazení proměnné, čímž porušuje syntaxi vlastností Maven

**Řešení**: Použijte operátor zastavení parsování `--%` před příkazem Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` říká PowerShellu, aby všechny zbývající argumenty předal Maven doslovně bez interpretace.

### Zobrazení emoji ve Windows PowerShell

**Problém**: Odpovědi AI zobrazují nesmyslné znaky (např. `????` nebo `â??`) místo emoji v PowerShellu

**Příčina**: Výchozí kódování PowerShellu nepodporuje UTF-8 emoji

**Řešení**: Spusťte tento příkaz před spuštěním Java aplikací:
```cmd
chcp 65001
```

Tím dojde k nucenému nastavení kódování UTF-8 v terminálu. Alternativně můžete použít Windows Terminal, který má lepší podporu Unicode.

### Ladění volání API

**Problém**: Chyby ověřování, limity rychlosti nebo neočekávané odpovědi od AI modelu

**Řešení**: Příklady obsahují `.logRequests(true)` a `.logResponses(true)` pro zobrazení API volání v konzoli. To pomáhá při řešení chyb ověřování, limitů rychlosti nebo neočekávaných odpovědí. Tyto přepínače odstraňte v produkčním provozu, aby se snížil šum v logu.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje využít profesionální lidský překlad. Nebereme žádnou odpovědnost za případné nedorozumění nebo chybné výklady vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->