# Modul 00: Rýchly štart

## Obsah

- [Úvod](../../../00-quick-start)
- [Čo je LangChain4j?](../../../00-quick-start)
- [Závislosti LangChain4j](../../../00-quick-start)
- [Predpoklady](../../../00-quick-start)
- [Nastavenie](../../../00-quick-start)
  - [1. Získajte svoj GitHub token](../../../00-quick-start)
  - [2. Nastavte svoj token](../../../00-quick-start)
- [Spustenie príkladov](../../../00-quick-start)
  - [1. Základný chat](../../../00-quick-start)
  - [2. Vzory výziev](../../../00-quick-start)
  - [3. Volanie funkcií](../../../00-quick-start)
  - [4. Otázky a odpovede k dokumentom (Easy RAG)](../../../00-quick-start)
  - [5. Zodpovedná AI](../../../00-quick-start)
- [Čo každý príklad ukazuje](../../../00-quick-start)
- [Ďalšie kroky](../../../00-quick-start)
- [Riešenie problémov](../../../00-quick-start)

## Úvod

Tento rýchly štart je určený na to, aby vás čo najrýchlejšie uviedol do práce s LangChain4j. Pokrýva úplné základy vytvárania AI aplikácií s LangChain4j a GitHub Modelmi. V nasledujúcich moduloch prejdete na Azure OpenAI a GPT-5.2 a hlbšie sa ponoríte do každého konceptu.

## Čo je LangChain4j?

LangChain4j je Java knižnica, ktorá zjednodušuje tvorbu aplikácií poháňaných AI. Namiesto práce s HTTP klientmi a parsovaním JSON pracujete s čistými Java API.

„Reťaz“ v LangChain odkazuje na spájanie viacerých komponentov – napríklad spájať výzvu k modelu, potom k parseru, alebo spojiť viac AI volaní, kde výstup jedného slúži ako vstup pre ďalšie. Tento rýchly štart sa sústreďuje na základy predtým, než preskúmate zložitejšie reťazce.

<img src="../../../translated_images/sk/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Spájanie komponentov v LangChain4j – stavebné bloky sa spájajú na vytvorenie výkonných AI pracovných tokov*

Použijeme tri základné komponenty:

**ChatModel** – Rozhranie pre interakciu s AI modelom. Zavolajte `model.chat("prompt")` a získajte odpoveď ako reťazec. Používame `OpenAiOfficialChatModel`, ktorý pracuje s OpenAI-kompatibilnými koncovými bodmi ako GitHub Models.

**AiServices** – Vytvára typovo bezpečné rozhrania AI služieb. Definujte metódy, označte ich anotáciou `@Tool`, a LangChain4j sa postará o orchestráciu. AI automaticky volá vaše Java metódy, keď je to potrebné.

**MessageWindowChatMemory** – Uchováva históriu konverzácie. Bez toho je každá požiadavka nezávislá. S týmto si AI pamätá predchádzajúce správy a udržiava kontext cez viacero kol.

<img src="../../../translated_images/sk/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektúra LangChain4j – základné komponenty spolupracujú na poháňanie vašich AI aplikácií*

## Závislosti LangChain4j

Tento rýchly štart používa tri Maven závislosti v [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` poskytuje triedu `OpenAiOfficialChatModel`, ktorá sa pripája k OpenAI-kompatibilným API. GitHub Models používa rovnaký formát API, takže nie je potrebný žiaden špeciálny adaptér – stačí nastaviť základnú URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` poskytuje automatické delenie dokumentov, vkladanie do vektorov (embedding) a vyhľadávanie, takže môžete vytvárať RAG aplikácie bez manuálneho nastavovania každého kroku.

## Predpoklady

**Používate Dev Container?** Java a Maven sú už nainštalované. Potrebujete len GitHub Osobný Prístupový Token.

**Lokálny vývoj:**
- Java 21+, Maven 3.9+
- GitHub Osobný Prístupový Token (inštrukcie nižšie)

> **Poznámka:** Tento modul používa `gpt-4.1-nano` z GitHub Models. Nemodifikujte názov modelu v kóde – je nakonfigurovaný tak, aby fungoval s dostupnými modelmi GitHubu.

## Nastavenie

### 1. Získajte svoj GitHub token

1. Prejdite na [GitHub Nastavenia → Osobné prístupové tokeny](https://github.com/settings/personal-access-tokens)
2. Kliknite na „Generate new token“
3. Nastavte popisný názov (napr. „LangChain4j Demo“)
4. Nastavte platnosť (odporúčaných 7 dní)
5. V časti „Account permissions“ nájdite „Models“ a nastavte na „Read-only“
6. Kliknite na „Generate token“
7. Skopírujte a uložte si token – už ho znova neuvidíte

### 2. Nastavte svoj token

**Možnosť 1: Použitie VS Code (odporúčané)**

Ak používate VS Code, pridajte svoj token do súboru `.env` v koreňovom adresári projektu:

Ak `.env` súbor neexistuje, skopírujte `.env.example` do `.env` alebo vytvorte nový `.env` súbor v koreňovom adresári.

**Príklad `.env` súboru:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Potom môžete jednoducho kliknúť pravým tlačidlom na ktorýkoľvek demo súbor (napr. `BasicChatDemo.java`) v Prieskumníku a vybrať **„Run Java“** alebo použiť spúšťacie konfigurácie z panela Run and Debug.

**Možnosť 2: Použitie terminálu**

Nastavte token ako premennú prostredia:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Spustenie príkladov

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na ktorýkoľvek demo súbor v Prieskumníku a vyberte **„Run Java“**, alebo použite spúšťacie konfigurácie z panela Run and Debug (predtým si nezabudnite pridať token do `.env` súboru).

**Použitie Maven:** Alternatívne môžete spustiť príklady príkazovým riadkom:

### 1. Základný chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Vzory výziev

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ukazuje zero-shot, few-shot, chain-of-thought a role-based prompting.

### 3. Volanie funkcií

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automaticky volá vaše Java metódy podľa potreby.

### 4. Otázky a odpovede k dokumentom (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pýtajte sa otázky o svojich dokumentoch pomocou Easy RAG s automatickým vkladaním a vyhľadávaním.

### 5. Zodpovedná AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Pozrite sa, ako bezpečnostné filtre AI blokujú škodlivý obsah.

## Čo každý príklad ukazuje

**Základný chat** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tu, aby ste videli LangChain4j v jeho najjednoduchšej podobe. Vytvoríte `OpenAiOfficialChatModel`, odošlete výzvu pomocou `.chat()` a získate späť odpoveď. Toto demonštruje základy: ako inicializovať modely s vlastnými koncovými bodmi a API kľúčmi. Keď pochopíte tento vzor, všetko ostatné na ňom staviate.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) a spýtajte sa:
> - „Ako by som v tomto kóde prepol z GitHub Models na Azure OpenAI?“
> - „Aké ďalšie parametre môžem nakonfigurovať v OpenAiOfficialChatModel.builder()?“
> - „Ako pridať streamovanie odpovedí namiesto čakania na kompletnú odpoveď?“

**Prompt Engineering** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, keď viete, ako komunikovať s modelom, preskúmajme, čo mu hovoríte. Toto demo používa rovnaké nastavenie modelu, ale ukazuje päť rôznych vzorov zadávania výziev. Vyskúšajte zero-shot výzvy pre priame inštrukcie, few-shot výzvy, ktoré sa učia z príkladov, chain-of-thought výzvy, ktoré odkrývajú kroky uvažovania, a role-based výzvy, ktoré nastavujú kontext. Uvidíte, ako rovnaký model dáva výrazne odlišné výsledky v závislosti od toho, ako rámujete svoju požiadavku.

Demo tiež demonštruje šablóny výziev, ktoré sú mocným spôsobom, ako vytvárať znovupoužiteľné výzvy s premennými.
Nižšie je príklad výzvy pomocou `PromptTemplate` z LangChain4j na vyplnenie premenných. AI odpovie na základe poskytnutého cieľa a aktivity.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) a spýtajte sa:
> - „Aký je rozdiel medzi zero-shot a few-shot výzvami a kedy použiť ktorý? “
> - „Ako parameter teploty ovplyvňuje odpovede modelu?“
> - „Aké sú techniky na zabránenie útokom prompt injection v produkcii?“
> - „Ako môžem vytvoriť znovupoužiteľné objekty PromptTemplate pre bežné vzory?“

**Integrácia nástrojov** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j získava na sile. Použijete `AiServices` na vytvorenie AI asistenta, ktorý môže volať vaše Java metódy. Stačí označiť metódy anotáciou `@Tool("popis")` a LangChain4j sa postará o zvyšok – AI automaticky rozhodne, kedy použiť ktorý nástroj podľa požiadaviek používateľa. Toto demonštruje volanie funkcií, kľúčovú techniku na vytváranie AI, ktorá môže konať, nie len odpovedať na otázky.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) a spýtajte sa:
> - „Ako funguje anotácia @Tool a čo s ňou robí LangChain4j na pozadí?“
> - „Môže AI volať niekoľko nástrojov za sebou na riešenie zložitých problémov?“
> - „Čo sa stane, ak nástroj vyhodí výnimku – ako mám riešiť chyby?“
> - „Ako by som integroval skutočné API namiesto tohto príkladu kalkulačky?“

**Otázky a odpovede k dokumentom (Easy RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tu uvidíte RAG (retrieval-augmented generation) pomocou prístupu LangChain4j „Easy RAG“. Dokumenty sa načítajú, automaticky rozdelia a vložia do pamäťového úložiska, potom vyhľadávač obsahu dodáva relevantné kúsky AI v čase dotazu. AI odpovedá na základe vašich dokumentov, nie všeobecných vedomostí modelu.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) a spýtajte sa:
> - „Ako RAG zabraňuje AI halucináciám v porovnaní s použitím trénovacích dát modelu?“
> - „Aký je rozdiel medzi týmto jednoduchým prístupom a vlastným RAG pipeline?“
> - „Ako by som to škáloval pre viac dokumentov alebo väčšie znalostné bázy?“

**Zodpovedná AI** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Postavte bezpečnosť AI s obranou do hĺbky. Toto demo ukazuje dve vrstvy ochrany pracujúce spoločne:

**Časť 1: LangChain4j vstupné bezpečnostné zábrany (Input Guardrails)** – Blokujú nebezpečné výzvy ešte predtým, než sa dostanú k LLM. Vytvorte vlastné zábrany, ktoré kontrolujú nepovolené kľúčové slová alebo vzory. Bežia vo vašom kóde, takže sú rýchle a zadarmo.

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

**Časť 2: Bezpečnostné filtre poskytovateľa** – GitHub Models majú zabudované filtre, ktoré zachytia to, čo vaše zábrany môžu prehliadnuť. Uvidíte hardbloky (HTTP 400 chyby) pri vážnych porušeniach a mäkké odmietnutia, kde AI zdvorilo odmietne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a spýtajte sa:
> - „Čo je InputGuardrail a ako vytvorím vlastný?“
> - „Aký je rozdiel medzi hard blokom a soft odmietnutím?“
> - „Prečo používať súčasne zábrany a filtre poskytovateľa?“

## Ďalšie kroky

**Ďalší modul:** [01-introduction - Začíname s LangChain4j](../01-introduction/README.md)

---

**Navigácia:** [← Späť na hlavný](../README.md) | [Ďalej: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Riešenie problémov

### Prvé zostavenie Maven

**Problém**: Počiatočný príkaz `mvn clean compile` alebo `mvn package` trvá dlho (10-15 minút)

**Príčina**: Maven potrebuje stiahnuť všetky závislosti projektu (Spring Boot, LangChain4j knižnice, Azure SDK, atď.) pri prvom zostavení.

**Riešenie**: Toto je normálne správanie. Následné zostavenia budú omnoho rýchlejšie, keďže závislosti sú uložené v lokálnej cache. Dĺžka sťahovania závisí od rýchlosti vašej siete.

### Syntax Maven príkazov v PowerShell

**Problém**: Maven príkazy zlyhávajú s chybou `Unknown lifecycle phase ".mainClass=..."`
**Príčina**: PowerShell interpretuje `=` ako operátor priradenia premennej, čo narušuje syntax vlastností Maven

**Riešenie**: Použite operátor zastavenia parsovania `--%` pred príkazom Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` informuje PowerShell, aby všetky nasledujúce argumenty odovzdal Maven doslovne bez interpretácie.

### Zobrazenie emoji vo Windows PowerShell

**Problém**: Odpovede AI zobrazujú nečitateľné znaky (napr. `????` alebo `â??`) namiesto emoji v PowerShell

**Príčina**: Predvolené kódovanie PowerShell nepodporuje UTF-8 emoji

**Riešenie**: Spustite tento príkaz pred vykonaním Java aplikácií:
```cmd
chcp 65001
```

Tým sa v termináli vynúti kódovanie UTF-8. Alternatívou je použiť Windows Terminal, ktorý má lepšiu podporu Unicode.

### Ladenie API volaní

**Problém**: Chyby overenia, limity rýchlosti alebo neočakávané odpovede od AI modelu

**Riešenie**: Príklady obsahujú `.logRequests(true)` a `.logResponses(true)`, aby sa API volania zobrazovali v konzole. To pomáha riešiť chyby overenia, limity rýchlosti alebo neočakávané odpovede. V produkcii tieto príznaky odstráňte, aby ste znížili hluk v logoch.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zrieknutie sa zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, berte prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Originálny dokument v pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pri kritických informáciách sa odporúča využiť profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nepochopenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->