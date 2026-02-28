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
  - [2. Vzory promptov](../../../00-quick-start)
  - [3. Volanie funkcií](../../../00-quick-start)
  - [4. Otázky a odpovede na dokumenty (Easy RAG)](../../../00-quick-start)
  - [5. Zodpovedné AI](../../../00-quick-start)
- [Čo každý príklad ukazuje](../../../00-quick-start)
- [Ďalšie kroky](../../../00-quick-start)
- [Riešenie problémov](../../../00-quick-start)

## Úvod

Tento rýchly štart je určený na to, aby ste sa s LangChain4j dostali rýchlo do chodu. Pokrýva úplné základy tvorby AI aplikácií s LangChain4j a GitHub Models. V nasledujúcich moduloch budete používať Azure OpenAI s LangChain4j na tvorbu pokročilejších aplikácií.

## Čo je LangChain4j?

LangChain4j je Java knižnica, ktorá zjednodušuje tvorbu aplikácií poháňaných AI. Namiesto riešenia HTTP klientov a parsovania JSON pracujete s čistými Java API.

"Chain" v LangChain znamená spájanie viacerých komponentov – môžete spojiť prompt s modelom a parserom alebo navzájom pospájať viac AI volaní, kde výstup jedného slúži ako vstup pre ďalší. Tento rýchly štart sa zameriava na základy pred skúmaním zložitejších reťazcov.

<img src="../../../translated_images/sk/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Reťazenie komponentov v LangChain4j - stavebné bloky sa prepájajú a vytvárajú výkonné AI pracovné postupy*

Použijeme tri základné komponenty:

**ChatModel** - Rozhranie pre interakciu s AI modelom. Zavolajte `model.chat("prompt")` a získajte odpoveď ako reťazec. Používame `OpenAiOfficialChatModel`, ktorý funguje so službami kompatibilnými s OpenAI, ako sú GitHub Models.

**AiServices** - Vytvára typovo bezpečné rozhrania AI služieb. Definujete metódy, anotujete ich `@Tool` a LangChain4j sa postará o orchestráciu. AI automaticky volá vaše Java metódy, keď je to potrebné.

**MessageWindowChatMemory** - Udržuje históriu konverzácie. Bez neho sú požiadavky nezávislé. S ním si AI pamätá predchádzajúce správy a udržiava kontext cez viacero kôl.

<img src="../../../translated_images/sk/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektúra LangChain4j - základné komponenty spolupracujú na poháňaní vašich AI aplikácií*

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

Modul `langchain4j-open-ai-official` poskytuje triedu `OpenAiOfficialChatModel`, ktorá sa pripája k API kompatibilným s OpenAI. GitHub Models používa rovnaký formát API, takže nie je potrebný žiadny špeciálny adaptér – stačí nasmerovať základnú URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` poskytuje automatické rozdelenie dokumentov, embedding a vyhľadávanie, aby ste mohli vytvárať RAG aplikácie bez manuálneho nastavovania jednotlivých krokov.

## Predpoklady

**Používate Dev Container?** Java a Maven sú už nainštalované. Potrebujete iba GitHub Personal Access Token.

**Lokálny vývoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (návod nižšie)

> **Poznámka:** Tento modul používa `gpt-4.1-nano` z GitHub Models. Nemodifikujte názov modelu v kóde – je nastavený tak, aby fungoval s dostupnými GitHub modelmi.

## Nastavenie

### 1. Získajte svoj GitHub token

1. Prejdite na [GitHub Nastavenia → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Kliknite na "Generate new token"
3. Zadajte popisný názov (napr. "LangChain4j Demo")
4. Nastavte dátum vypršania (odporúča sa 7 dní)
5. V "Account permissions" nájdite "Models" a nastavte na "Read-only"
6. Kliknite na "Generate token"
7. Skopírujte a uložte si token – už ho neuvidíte

### 2. Nastavte svoj token

**Možnosť 1: Použitie VS Code (odporúčané)**

Ak používate VS Code, pridajte svoj token do súboru `.env` v koreňovom adresári projektu:

Ak súbor `.env` neexistuje, skopírujte `.env.example` do `.env` alebo vytvorte nový `.env` súbor v koreňovom adresári projektu.

**Príklad `.env` súboru:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Potom môžete jednoducho kliknúť pravým tlačidlom na ktorýkoľvek demo súbor (napr. `BasicChatDemo.java`) v Prieskumníkovi a vybrať **"Run Java"** alebo použiť spúšťacie konfigurácie z panela Run and Debug.

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

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na ktorýkoľvek demo súbor v Prieskumníkovi a vyberte **"Run Java"**, alebo použite spúšťacie konfigurácie v Run and Debug paneli (predtým nezabudnite pridať token do `.env` súboru).

**Použitie Maven:** Alternatívne môžete spustiť z príkazového riadku:

### 1. Základný chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Vzory promptov

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Ukazuje zero-shot, few-shot, chain-of-thought a promptovanie podľa roly.

### 3. Volanie funkcií

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automaticky volá vaše Java metódy, keď je to potrebné.

### 4. Otázky a odpovede na dokumenty (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pýtajte sa na svoje dokumenty pomocou Easy RAG s automatickým embeddingom a vyhľadávaním.

### 5. Zodpovedné AI

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

**Základný chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tu, aby ste videli LangChain4j v jeho jednoduchej podobe. Vytvoríte `OpenAiOfficialChatModel`, pošlete prompt cez `.chat()` a získate odpoveď. Tento príklad ukazuje základy: ako inicializovať modely s vlastnými endpointmi a API kľúčmi. Keď tento vzor pochopíte, všetko ostatné sa naň nadväzuje.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) a opýtajte sa:
> - "Ako by som prepol z GitHub Models na Azure OpenAI v tomto kóde?"
> - "Aké ďalšie parametre môžem nastaviť v OpenAiOfficialChatModel.builder()?"
> - "Ako pridať streamovanie odpovedí namiesto čakania na kompletnú odpoveď?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, keď viete, ako komunikovať s modelom, preskúmajme, čo mu hovoríte. Toto demo používa rovnaké nastavenie modelu, ale ukazuje päť rôznych vzorov promptov. Vyskúšajte zero-shot prompty pre priame inštrukcie, few-shot prompty, ktoré sa učia z príkladov, chain-of-thought prompty, ktoré odhaľujú kroky uvažovania, a role-based prompty, ktoré nastavujú kontext. Uvidíte, ako rovnaký model dáva dramaticky odlišné výsledky podľa toho, ako formulujete požiadavku.

Demo tiež ukazuje prompt šablóny, ktoré sú silným nástrojom na tvorbu znovupoužiteľných promptov s premennými.
Nižšie uvedený príklad ukazuje prompt používajúci LangChain4j `PromptTemplate` na vyplnenie premenných. AI odpovie na základe zadaného cieľa a aktivity.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) a opýtajte sa:
> - "Aký je rozdiel medzi zero-shot a few-shot promptovaním a kedy použiť ktorý?"
> - "Ako parametr teploty ovplyvňuje odpovede modelu?"
> - "Aké sú techniky na zabránenie útokom na prompt injection v produkcii?"
> - "Ako vytvoriť znovupoužiteľné objekty PromptTemplate pre bežné vzory?"

**Integrácia nástrojov** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j ukazuje svoju silu. Použijete `AiServices` na vytvorenie AI asistenta, ktorý môže volať vaše Java metódy. Len anotujte metódy s `@Tool("popis")` a LangChain4j sa postará o zvyšok - AI automaticky rozhodne, kedy použiť ktorý nástroj podľa toho, čo používateľ žiada. Ukazuje to volanie funkcií, kľúčovú techniku na tvorbu AI, ktorá dokáže konať, nie len odpovedať na otázky.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) a opýtajte sa:
> - "Ako funguje anotácia @Tool a čo s ňou LangChain4j robí na pozadí?"
> - "Môže AI volať viac nástrojov postupne na vyriešenie zložitých problémov?"
> - "Čo sa stane, ak nástroj vyhodí výnimku – ako mám riešiť chyby?"
> - "Ako by som integroval skutočné API namiesto tohto príkladu kalkulačky?"

**Otázky a odpovede na dokumenty (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tu uvidíte RAG (retrieval-augmented generation) pomocou LangChain4j prístupu "Easy RAG". Dokumenty sa načítajú, automaticky rozdelia a vložia do pamäťového skladu, potom vyhľadávač dodá relevantné kúsky AI pri dotaze. AI odpovedá na základe vašich dokumentov, nie na základe všeobecných vedomostí.

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

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) a opýtajte sa:
> - "Ako RAG zabraňuje halucináciám AI v porovnaní s používaním dát trénovania modelu?"
> - "Aký je rozdiel medzi týmto jednoduchým prístupom a vlastným RAG pipeline?"
> - "Ako by som škáloval toto na viac dokumentov alebo väčšie knowledgové databázy?"

**Zodpovedné AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Budujte bezpečnosť AI obrannými vrstvami. Toto demo ukazuje dve vrstvy ochrany pracujúce spolu:

**Časť 1: LangChain4j Input Guardrails** - Blokuje nebezpečné prompty skôr, než sa dostanú k LLM. Vytvorte vlastné guardrails, ktoré kontrolujú zakázané kľúčové slová alebo vzory. Tieto bežia vo vašom kóde, takže sú rýchle a zadarmo.

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

**Časť 2: Bezpečnostné filtre poskytovateľa** - GitHub Models má zabudované filtre, ktoré chytia to, čo vaše guardrails môžu prehliadnuť. Uvidíte tvrdé bloky (HTTP 400 chyby) pri vážnych porušeniach a jemné odmietnutia, kde AI slušne odmietne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a opýtajte sa:
> - "Čo je InputGuardrail a ako si vytvoriť vlastný?"
> - "Aký je rozdiel medzi tvrdým blokom a jemným odmietnutím?"
> - "Prečo používať guardrails aj filtre poskytovateľa spolu?"

## Ďalšie kroky

**Ďalší modul:** [01-introduction - Začíname s LangChain4j a gpt-5 na Azure](../01-introduction/README.md)

---

**Navigácia:** [← Späť na hlavnú stránku](../README.md) | [Ďalšie: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Riešenie problémov

### Prvé zostavenie Maven

**Problém**: Prvý príkaz `mvn clean compile` alebo `mvn package` trvá dlho (10-15 minút)

**Príčina**: Maven musí pri prvom zostavení stiahnuť všetky závislosti projektu (Spring Boot, LangChain4j knižnice, Azure SDK atď.).

**Riešenie**: Toto je bežné správanie. Nasledujúce zostavenia budú oveľa rýchlejšie, pretože závislosti sú uložené v lokálnej cache. Rýchlosť stiahnutia závisí od rýchlosti vašej siete.

### Syntax PowerShell príkazov Maven

**Problém**: Maven príkazy zlyhávajú s chybou `Unknown lifecycle phase ".mainClass=..."`
**Príčina**: PowerShell interpretuje `=` ako operátor priradenia premennej, čo narušuje syntax vlastností Maven

**Riešenie**: Použite operátor na zastavenie parsovania `--%` pred príkazom Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` hovorí PowerShellu, aby všetky nasledujúce argumenty odovzdal Maven doslovne bez interpretácie.

### Zobrazenie Emoji vo Windows PowerShell

**Problém**: Odpovede AI zobrazujú nezmyselné znaky (napr. `????` alebo `â??`) namiesto emoji vo PowerShelli

**Príčina**: Predvolené kódovanie PowerShellu nepodporuje UTF-8 emoji

**Riešenie**: Spustite tento príkaz pred vykonaním Java aplikácií:
```cmd
chcp 65001
```

Týmto sa v termináli vynúti kódovanie UTF-8. Alternatívne použite Windows Terminal, ktorý má lepšiu podporu Unicode.

### Ladenie API volaní

**Problém**: Chyby autentifikácie, limity rýchlosti alebo neočakávané odpovede od AI modelu

**Riešenie**: Príklady obsahujú `.logRequests(true)` a `.logResponses(true)`, ktoré zobrazujú API volania v konzole. To pomáha riešiť chyby autentifikácie, limity rýchlosti alebo neočakávané odpovede. Tieto príznaky odstráňte v produkcii, aby sa znížil šum v logoch.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o vylúčení zodpovednosti**:  
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, berte prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Považujte pôvodný dokument v jeho materskom jazyku za autoritatívny zdroj. Pre kritické informácie odporúčame profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo mylné interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->