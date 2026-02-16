# Modul 00: Rýchly štart

## Obsah

- [Úvod](../../../00-quick-start)
- [Čo je LangChain4j?](../../../00-quick-start)
- [Závislosti LangChain4j](../../../00-quick-start)
- [Požiadavky](../../../00-quick-start)
- [Nastavenie](../../../00-quick-start)
  - [1. Získajte svoj GitHub token](../../../00-quick-start)
  - [2. Nastavte svoj token](../../../00-quick-start)
- [Spustite príklady](../../../00-quick-start)
  - [1. Základný chat](../../../00-quick-start)
  - [2. Vzory promptov](../../../00-quick-start)
  - [3. Volanie funkcií](../../../00-quick-start)
  - [4. Otázky a odpovede na dokument (RAG)](../../../00-quick-start)
  - [5. Zodpovedná AI](../../../00-quick-start)
- [Čo každý príklad ukazuje](../../../00-quick-start)
- [Ďalšie kroky](../../../00-quick-start)
- [Riešenie problémov](../../../00-quick-start)

## Úvod

Tento rýchly štart je určený na to, aby ste čo najrýchlejšie začali pracovať s LangChain4j. Pokrýva úplné základy budovania AI aplikácií s LangChain4j a GitHub modelmi. V ďalších moduloch použijete Azure OpenAI s LangChain4j na vytváranie pokročilejších aplikácií.

## Čo je LangChain4j?

LangChain4j je Java knižnica, ktorá zjednodušuje tvorbu aplikácií poháňaných umelou inteligenciou. Namiesto práce s HTTP klientmi a spracovaním JSON pracujete s čitateľnými Java API.

"Reťaz" v LangChain znamená prepojenie viacerých komponentov - môžete spojiť prompt s modelom a s parserom, alebo prepojiť viaceré AI volania, kde jeden výstup je vstupom pre nasledujúci krok. Tento rýchly štart sa sústreďuje na základy predtým, než sa pustíte do zložitejších reťazcov.

<img src="../../../translated_images/sk/langchain-concept.ad1fe6cf063515e1.webp" alt="Koncept reťazenia v LangChain4j" width="800"/>

*Reťazenie komponentov v LangChain4j – stavebné bloky spojené na vytváranie výkonných AI pracovných tokov*

Použijeme tri základné komponenty:

**ChatLanguageModel** - Rozhranie pre interakciu s AI modelom. Zavolajte `model.chat("prompt")` a získate odpoveď ako reťazec. Používame `OpenAiOfficialChatModel`, ktorý funguje s OpenAI-kompatibilnými koncovými bodmi ako GitHub modely.

**AiServices** - Vytvára typovo bezpečné rozhrania pre AI služby. Definujete metódy, označíte ich anotáciou `@Tool` a LangChain4j sa postará o orchestráciu. AI automaticky volá vaše Java metódy podľa potreby.

**MessageWindowChatMemory** - Udržiava históriu konverzácie. Bez nej je každá požiadavka samostatná. S ňou si AI pamätá predchádzajúce správy a udržiava kontext naprieč viacerými kolami.

<img src="../../../translated_images/sk/architecture.eedc993a1c576839.webp" alt="Architektúra LangChain4j" width="800"/>

*Architektúra LangChain4j – základné komponenty spolupracujúce na poháňanie vašich AI aplikácií*

## Závislosti LangChain4j

Tento rýchly štart používa dve závislosti Maven v súbore [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` poskytuje triedu `OpenAiOfficialChatModel`, ktorá sa pripája k OpenAI-kompatibilným API. GitHub modely používajú rovnaký formát API, takže nie je potrebný žiaden špeciálny adaptér – jednoducho nastavte základnú URL na `https://models.github.ai/inference`.

## Požiadavky

**Používate Dev Container?** Java a Maven sú už nainštalované. Potrebujete iba GitHub Personal Access Token.

**Lokálny vývoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (návod nižšie)

> **Poznámka:** Tento modul používa model `gpt-4.1-nano` z GitHub modelov. Nemodifikujte názov modelu v kóde – je nakonfigurovaný na prácu s dostupnými GitHub modelmi.

## Nastavenie

### 1. Získajte svoj GitHub token

1. Prejdite na [GitHub Nastavenia → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Kliknite na „Generate new token“
3. Zadajte popisný názov (napr. „LangChain4j Demo“)
4. Nastavte vypršanie platnosti (odporúčané 7 dní)
5. V sekcii „Account permissions“ nájdite „Models“ a nastavte na „Read-only“
6. Kliknite „Generate token“
7. Skopírujte a uložte svoj token – už ho neuvidíte

### 2. Nastavte svoj token

**Možnosť 1: Použitie VS Code (odporúčané)**

Ak používate VS Code, pridajte svoj token do súboru `.env` v koreňovom adresári projektu:

Ak súbor `.env` neexistuje, skopírujte `.env.example` na `.env` alebo vytvorte nový súbor `.env` v koreňovom adresári.

**Príklad súboru `.env`:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Potom môžete jednoducho kliknúť pravým tlačidlom myši na akýkoľvek demo súbor (napr. `BasicChatDemo.java`) v Explorera a vybrať **"Run Java"** alebo použiť spúšťacie konfigurácie v paneli Run and Debug.

**Možnosť 2: Použitie Terminálu**

Nastavte token ako premennú prostredia:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Spustite príklady

**Použitie VS Code:** Jednoducho kliknite pravým tlačidlom na ľubovoľný demo súbor v Explorera a vyberte **"Run Java"** alebo použite spúšťacie konfigurácie z panela Run and Debug (predtým musíte pridať svoj token do `.env` súboru).

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

### 4. Otázky a odpovede na dokument (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pýtajte sa otázky o obsahu v `document.txt`.

### 5. Zodpovedná AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Uvidíte, ako bezpečnostné filtre AI blokujú škodlivý obsah.

## Čo každý príklad ukazuje

**Základný chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tu, aby ste videli LangChain4j v jeho najjednoduchšej podobe. Vytvoríte `OpenAiOfficialChatModel`, pošlete prompt s `.chat()` a získate odpoveď. Toto demonštruje základy: ako inicializovať modely s vlastnými koncovými bodmi a API kľúčmi. Keď pochopíte tento vzor, všetko ostatné na ňom stojí.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) a spýtajte sa:
> - "Ako by som prepol z GitHub Modelov na Azure OpenAI v tomto kóde?"
> - "Aké ďalšie parametre môžem konfigurovať v OpenAiOfficialChatModel.builder()?"
> - "Ako pridať streamované odpovede namiesto čakania na kompletnú odpoveď?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, keď viete, ako komunikovať s modelom, preskúmajme, čo mu hovoríte. Táto ukážka používa tú istú konfiguráciu modelu, ale ukazuje päť rôznych vzorov promptovania. Vyskúšajte zero-shot prompty pre priame inštrukcie, few-shot prompty, ktoré sa učia z príkladov, chain-of-thought prompty, ktoré odhaľujú kroky uvažovania, a role-based prompty, ktoré nastavujú kontext. Uvidíte, ako rovnaký model dáva dramaticky odlišné výsledky podľa toho, ako formulujete svoju požiadavku.

Demo tiež demonštruje šablóny promptov, ktoré sú výkonným spôsobom tvorby opakovane použiteľných promptov s premennými.
Nižšie uvedený príklad ukazuje prompt využívajúci LangChain4j `PromptTemplate` na vyplnenie premenných. AI odpovie na základe poskytnutého cieľa a aktivity.

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
> - "Aký je rozdiel medzi zero-shot a few-shot promptovaním a kedy použiť ktorý?"
> - "Ako parametr teploty ovplyvňuje odpovede modelu?"
> - "Aké sú techniky na zabránenie prompt injection útokom v produkcii?"
> - "Ako môžem vytvárať opakovane použiteľné objekty PromptTemplate pre bežné vzory?"

**Integrácia nástrojov** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j získava silu. Použijete `AiServices` na vytvorenie AI asistenta, ktorý môže volať vaše Java metódy. Len označíte metódy anotáciou `@Tool("popis")` a LangChain4j sa postará o zvyšok – AI automaticky rozhodne, kedy použiť ktorý nástroj na základe požiadavky používateľa. Toto demonštruje volanie funkcií, kľúčovú techniku na budovanie AI, ktorá môže vykonávať akcie, nielen odpovedať na otázky.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) a spýtajte sa:
> - "Ako funguje anotácia @Tool a čo LangChain4j robí s ňou za scénou?"
> - "Môže AI volať viacero nástrojov za sebou, aby vyriešila zložité problémy?"
> - "Čo sa stane, ak nástroj vyhodí výnimku – ako mám riešiť chyby?"
> - "Ako by som integroval reálne API namiesto tohto príkladu kalkulačky?"

**Otázky a odpovede na dokument (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tu uvidíte základy RAG (retrieval-augmented generation). Namiesto spoliehania sa na tréningové dáta modelu načítate obsah zo súboru [`document.txt`](../../../00-quick-start/document.txt) a zahrniete ho do promptu. AI odpovedá na základe vášho dokumentu, nie na základe všeobecných znalostí. Toto je prvý krok k budovaniu systémov, ktoré môžu pracovať s vašimi vlastnými údajmi.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Poznámka:** Tento jednoduchý prístup načítava celý dokument do promptu. Pri veľkých súboroch (>10KB) prekročíte limity kontextu. Modul 03 pokrýva delenie na časti a vyhľadávanie vektorov pre produkčné RAG systémy.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) a spýtajte sa:
> - "Ako RAG zabraňuje halucináciám AI v porovnaní s použitím tréningových dát modelu?"
> - "Aký je rozdiel medzi týmto jednoduchým prístupom a použitím vektorových embeddingov pre vyhľadávanie?"
> - "Ako by som toto škáloval na viacero dokumentov alebo väčšie znalostné bázy?"
> - "Aké sú najlepšie praktiky pre štruktúrovanie promptu tak, aby AI používala iba poskytnutý kontext?"

**Zodpovedná AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Budujte bezpečnosť AI s obranou do hĺbky. Táto ukážka ukazuje dve vrstvy ochrany pracujúce spoločne:

**Časť 1: LangChain4j vstupné zábrany (Input Guardrails)** - Blokujú nebezpečné prompty ešte predtým, než sa dostanú k LLM. Vytvorte vlastné zábrany, ktoré kontrolujú zakázané kľúčové slová alebo vzory. Bežia vo vašom kóde, takže sú rýchle a zadarmo.

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

**Časť 2: Filter bezpečnosti poskytovateľa** - GitHub modely majú zabudované filtre, ktoré zachytávajú to, čo vaša ochrana môže prehliadnuť. Uvidíte tvrdé bloky (chyby HTTP 400) pri závažných porušeniach a jemné odmietnutia, keď AI zdvorilo odmietne.

> **🤖 Vyskúšajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorte [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) a spýtajte sa:
> - "Čo je InputGuardrail a ako si vytvorím vlastný?"
> - "Aký je rozdiel medzi tvrdým blokom a jemným odmietnutím?"
> - "Prečo používať spolu zábrany a filtre poskytovateľa?"

## Ďalšie kroky

**Ďalší modul:** [01-introduction - Začíname s LangChain4j a gpt-5 na Azure](../01-introduction/README.md)

---

**Navigácia:** [← Späť na úvod](../README.md) | [Ďalej: Modul 01 - Úvod →](../01-introduction/README.md)

---

## Riešenie problémov

### Prvé zostavenie Maven

**Problém:** Počiatočné `mvn clean compile` alebo `mvn package` trvá dlho (10-15 minút)

**Príčina:** Maven musí pri prvom zostavení stiahnuť všetky závislosti projektu (Spring Boot, knižnice LangChain4j, Azure SDK a pod.).

**Riešenie:** Je to bežné správanie. Následné zostavenia budú oveľa rýchlejšie, pretože závislosti sú uložené lokálne. Čas sťahovania závisí od rýchlosti vašej siete.
### Syntax príkazu PowerShell Maven

**Problém**: Príkazy Maven zlyhajú s chybou `Unknown lifecycle phase ".mainClass=..."`

**Príčina**: PowerShell interpretuje `=` ako operátor priradenia premennej, čo narušuje syntaktiku vlastností Maven

**Riešenie**: Použite operátor zastavenia analýzy `--%` pred príkazom Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operátor `--%` hovorí PowerShellu, aby všetky zostávajúce argumenty jednoducho preniesol do Maven bez interpretácie.

### Zobrazenie emoji vo Windows PowerShell

**Problém**: Odpovede AI zobrazujú nezmyselné znaky (napr. `????` alebo `â??`) namiesto emoji v PowerShell

**Príčina**: Predvolené kódovanie PowerShell nepodporuje UTF-8 emoji

**Riešenie**: Pred spustením Java aplikácií spustite tento príkaz:
```cmd
chcp 65001
```

Tým sa v termináli vynúti kódovanie UTF-8. Alternatívne môžete použiť Windows Terminal, ktorý má lepšiu podporu Unicode.

### Ladenie API volaní

**Problém**: Autentifikačné chyby, limity rýchlosti alebo neočakávané odpovede od AI modelu

**Riešenie**: Príklady obsahujú `.logRequests(true)` a `.logResponses(true)` na zobrazenie API volaní v konzole. Pomáha to riešiť problémy s autentifikáciou, limitmi alebo neočakávanými odpoveďami. V produkcii tieto príznaky odstráňte, aby ste znížili množstvo logov.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o odmietnutí zodpovednosti**:  
Tento dokument bol preložený pomocou služieb automatického prekladu AI [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, berte prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nezodpovedáme za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->