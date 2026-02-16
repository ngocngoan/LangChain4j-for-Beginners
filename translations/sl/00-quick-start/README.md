# Modul 00: Hiter začetek

## Kazalo

- [Uvod](../../../00-quick-start)
- [Kaj je LangChain4j?](../../../00-quick-start)
- [Povezave LangChain4j](../../../00-quick-start)
- [Predpogoji](../../../00-quick-start)
- [Nastavitev](../../../00-quick-start)
  - [1. Pridobite GitHub žeton](../../../00-quick-start)
  - [2. Nastavite svoj žeton](../../../00-quick-start)
- [Zaženite primere](../../../00-quick-start)
  - [1. Osnovni klepet](../../../00-quick-start)
  - [2. Vzorci pozivov](../../../00-quick-start)
  - [3. Klic funkcij](../../../00-quick-start)
  - [4. Vprašanja in odgovori o dokumentih (RAG)](../../../00-quick-start)
  - [5. Odgovorna umetna inteligenca](../../../00-quick-start)
- [Kaj vsak primer prikazuje](../../../00-quick-start)
- [Naslednji koraki](../../../00-quick-start)
- [Reševanje težav](../../../00-quick-start)

## Uvod

Ta hitri začetek je namenjen, da vas čim prej spravi v pogon z LangChain4j. Pokriva osnovne gradnike za ustvarjanje AI aplikacij z LangChain4j in GitHub modeli. V naslednjih modulih boste z uporabo Azure OpenAI in LangChain4j gradili bolj kompleksne aplikacije.

## Kaj je LangChain4j?

LangChain4j je Java knjižnica, ki poenostavi gradnjo AI-podprtih aplikacij. Namesto da bi se ukvarjali z HTTP odjemalci in JSON razčlenjevanjem, delate z jasnimi Java API-ji.

»Veriga« v LangChain pomeni povezovanje več komponent – lahko povežete poziv (prompt) z modelom in nato z razčlenjevalnikom ali povežete več AI klicev skupaj, kjer izhod enega služi kot vhod za naslednjega. Ta hitri začetek se osredotoča na osnove, preden razišče bolj kompleksne verige.

<img src="../../../translated_images/sl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Povezovanje komponent v LangChain4j – gradniki se povezujejo za ustvarjanje zmogljivih AI delovnih tokov*

Uporabili bomo tri osnovne komponente:

**ChatLanguageModel** – vmesnik za interakcijo z AI modeli. Pokličite `model.chat("prompt")` in dobite odgovor v obliki niza. Uporabljamo `OpenAiOfficialChatModel`, ki deluje z OpenAI-kompatibilnimi končnimi točkami, kot so GitHub modeli.

**AiServices** – ustvari tipno varne vmesnike za AI storitve. Definirajte metode, jih označite z `@Tool` in LangChain4j poskrbi za orkestracijo. AI samodejno kliče vaše Java metode, ko je potrebno.

**MessageWindowChatMemory** – ohranja zgodovino pogovora. Brez tega je vsak zahtevek samostojen. Z njim si AI zapomni prejšnja sporočila in vzdržuje kontekst skozi več krogov.

<img src="../../../translated_images/sl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arhitektura LangChain4j – jedrne komponente, ki skupaj poganjajo vaše AI aplikacije*

## Povezave LangChain4j

Ta hitri začetek uporablja dve Maven odvisnosti v [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` zagotavlja razred `OpenAiOfficialChatModel`, ki se poveže z OpenAI-kompatibilnimi API-ji. GitHub modeli uporabljajo enak format API-ja, zato posebnega adapterja ne potrebujete – samo nastavite osnovni URL na `https://models.github.ai/inference`.

## Predpogoji

**Uporabljate razvojni kontejner?** Java in Maven sta že nameščena. Potrebujete samo GitHub osebni dostopni žeton.

**Lokalni razvoj:**
- Java 21+ in Maven 3.9+
- GitHub osebni dostopni žeton (navodila spodaj)

> **Opomba:** Ta modul uporablja `gpt-4.1-nano` iz GitHub modelov. Ne spreminjajte imena modela v kodi – konfiguriran je za delo z GitHub razpoložljivimi modeli.

## Nastavitev

### 1. Pridobite GitHub žeton

1. Pojdite na [GitHub Nastavitve → Osebni dostopni žetoni](https://github.com/settings/personal-access-tokens)
2. Kliknite "Generiraj nov žeton"
3. Nastavite opisno ime (npr. "LangChain4j Demo")
4. Nastavite potek (priporočeno 7 dni)
5. Pod "Dovoljenja računa" poiščite "Models" in nastavite na "Samo branje"
6. Kliknite "Generiraj žeton"
7. Kopirajte in shranite svoj žeton – ne boste ga več videli

### 2. Nastavite svoj žeton

**Možnost 1: Uporaba VS Code (priporočeno)**

Če uporabljate VS Code, dodajte svoj žeton v datoteko `.env` v korenu projekta:

Če datoteka `.env` ne obstaja, kopirajte `.env.example` v `.env` ali ustvarite novo `.env` datoteko v korenu projekta.

**Primer `.env` datoteke:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Nato lahko preprosto kliknete z desnim klikom na katerokoli demo datoteko (npr. `BasicChatDemo.java`) v Explorerju in izberete **"Run Java"** ali uporabite zagonske konfiguracije v pogledu Zaženi in Odpravi.

**Možnost 2: Uporaba terminala**

Nastavite žeton kot okoljsko spremenljivko:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Zaženite primere

**Uporaba VS Code:** Preprosto kliknite z desnim klikom na katerokoli demo datoteko v Explorerju in izberite **"Run Java"** ali uporabite zagonske konfiguracije v pogledu Zaženi in Odpravi (poskrbite, da ste prej dodali žeton v `.env`).

**Uporaba Maven:** Lahko tudi zaženete iz ukazne vrstice:

### 1. Osnovni klepet

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Vzorci pozivov

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Prikazuje zero-shot, few-shot, chain-of-thought in role-based pozive.

### 3. Klic funkcij

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI samodejno kliče vaše Java metode, ko je potrebno.

### 4. Vprašanja in odgovori o dokumentih (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Postavljajte vprašanja o vsebini v `document.txt`.

### 5. Odgovorna umetna inteligenca

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Oglejte si, kako varnostni filtri AI blokirajo škodljivo vsebino.

## Kaj vsak primer prikazuje

**Osnovni klepet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tukaj, da vidite LangChain4j v njegovi najpreprostejši obliki. Ustvarili boste `OpenAiOfficialChatModel`, poslali poziv z `.chat()` in dobili odgovor. To prikazuje osnovo: kako inicializirati modele s prilagojenimi končnimi točkami in API ključem. Ko razumete ta vzorec, se vse ostalo gradi na njem.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odprite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) in vprašajte:
> - "Kako bi v tej kodi preklopil s GitHub modelov na Azure OpenAI?"
> - "Katere druge parametre lahko nastavim v OpenAiOfficialChatModel.builder()?"
> - "Kako dodam pretakanje odgovorov namesto čakanja na celoten odgovor?"

**Umetnost oblikovanja pozivov** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Zdaj, ko veste, kako govoriti z modelom, poglejmo, kaj mu govorite. Ta demo uporablja isto nastavitev modela, a prikazuje pet različnih vzorcev pozivov. Preizkusite zero-shot pozive za neposredna navodila, few-shot pozive, ki se učijo iz primerov, chain-of-thought pozive, ki razkrivajo korake razmišljanja, in role-based pozive, ki postavijo kontekst. Videli boste, kako isti model zagotovi zelo različne rezultate glede na to, kako oblikujete zahtevo.

Demo tudi prikazuje predloge pozivov (prompt templates), ki so močan način za ustvarjanje ponovno uporabnih pozivov z spremenljivkami.
Spodnji primer prikazuje poziv z uporabo LangChain4j `PromptTemplate` za izpolnitev spremenljivk. AI bo odgovoril glede na navedeno destinacijo in aktivnost.

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

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odprite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) in vprašajte:
> - "Kakšna je razlika med zero-shot in few-shot pozivi, kdaj naj uporabim katerega?"
> - "Kako parameter temperature vpliva na odgovore modela?"
> - "Katere so tehnike za preprečevanje napadov z injektiranjem pozivov v produkciji?"
> - "Kako lahko ustvarim ponovno uporabne objekte PromptTemplate za pogoste vzorce?"

**Integracija orodij** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j postane močan. Uporabili boste `AiServices` za ustvarjanje AI asistenta, ki lahko kliče vaše Java metode. Preprosto označite metode z `@Tool("opis")` in LangChain4j poskrbi za ostalo – AI samodejno odloča, kdaj uporabiti katero orodje glede na zahtevo uporabnika. To prikazuje klic funkcij, ključno tehniko za gradnjo AI, ki lahko izvaja dejanja, ne samo odgovarja na vprašanja.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odprite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) in vprašajte:
> - "Kako deluje oznaka @Tool in kaj LangChain4j počne z njo v ozadju?"
> - "Ali lahko AI pokliče več orodij zaporedoma za reševanje kompleksnih problemov?"
> - "Kaj se zgodi, če orodje vrže izjemo – kako naj upravljam napake?"
> - "Kako bi integriral pravi API namesto tega primera kalkulatorja?"

**Vprašanja in odgovori o dokumentih (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tukaj boste videli osnovo RAG (retrieval-augmented generation). Namesto da bi se zanašali na podatke o usposabljanju modela, naložite vsebino iz [`document.txt`](../../../00-quick-start/document.txt) in jo vključite v poziv. AI odgovarja na podlagi vašega dokumenta, ne pa splošnega znanja. To je prvi korak k gradnji sistemov, ki lahko delujejo z vašimi lastnimi podatki.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Opomba:** Ta preprost pristop naloži celoten dokument v poziv. Za velike datoteke (>10KB) boste presegli omejitve konteksta. Modul 03 pokriva razcepanje in vektorsko iskanje za produkcijske RAG sisteme.

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odprite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) in vprašajte:
> - "Kako RAG preprečuje halucinacije AI v primerjavi z uporabo podatkov o usposabljanju modela?"
> - "Kakšna je razlika med tem preprostim pristopom in uporabo vektorskih vstopnic za iskanje?"
> - "Kako bi to razširil za več dokumentov ali večje baze znanja?"
> - "Kakšne so najboljše prakse za strukturiranje poziva, da AI uporablja samo priloženi kontekst?"

**Odgovorna umetna inteligenca** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Ustvarite varnost AI z obrambo v globino. Ta demo prikazuje dve zaščitni plasti, ki delujeta skupaj:

**Del 1: LangChain4j vhodni varnostni mehanizmi (Input Guardrails)** – Blokira nevarne pozive, preden pridejo do LLM. Ustvarite lastne varnostne mehanizme, ki preverjajo prepovedane ključne besede ali vzorce. Ti tečejo v vaši kodi, zato so hitri in brezplačni.

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

**Del 2: Varnostni filtri ponudnika** – GitHub modeli imajo vgrajene filtre, ki ujamejo, kar vaši varnostni mehanizmi morda spregledajo. Videli boste trde bloke (HTTP 400 napake) za resne kršitve in mehke zavrnitve, kjer AI vljudno odkloni.

> **🤖 Preizkusi z [GitHub Copilot](https://github.com/features/copilot) klepetom:** Odprite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) in vprašajte:
> - "Kaj je InputGuardrail in kako ustvarim svojega?"
> - "Kakšna je razlika med trdim blokom in mehko zavrnitvijo?"
> - "Zakaj uporabljati tako varnostne mehanizme kot filtre ponudnika skupaj?"

## Naslednji koraki

**Naslednji modul:** [01-uvod - Začetek z LangChain4j in gpt-5 na Azure](../01-introduction/README.md)

---

**Navigacija:** [← Nazaj na glavno](../README.md) | [Naprej: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Reševanje težav

### Prva Maven gradnja

**Težava:** Začetni `mvn clean compile` ali `mvn package` traja dolgo (10–15 minut)

**Vzrok:** Maven mora naložiti vse odvisnosti projekta (Spring Boot, LangChain4j knjižnice, Azure SDK-je itd.) pri prvi gradnji.

**Rešitev:** To je normalno vedenje. Nadaljnje gradnje bodo veliko hitrejše, ker so odvisnosti shranjene lokalno v predpomnilniku. Čas prenosa je odvisen od hitrosti vaše povezave.
### Ukazna sintaksa PowerShell Maven

**Težava**: Maven ukazi ne uspejo z napako `Unknown lifecycle phase ".mainClass=..."`

**Vzrok**: PowerShell interpretira `=` kot operator dodelitve spremenljivke, kar prekine sintakso Maven lastnosti

**Rešitev**: Uporabite operator za zaustavitev interpretacije `--%` pred Maven ukazom:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` sporoči PowerShell-u, naj vse preostale argumente posreduje Maven-u dobesedno, brez interpretacije.

### Prikaz emojijev v Windows PowerShell

**Težava**: Odgovori AI prikažejo znake smeti (npr. `????` ali `â??`) namesto emojijev v PowerShell

**Vzrok**: Privzeta kodna tabela PowerShell-a ne podpira UTF-8 emojijev

**Rešitev**: Zaženi ta ukaz pred zagonom Java aplikacij:
```cmd
chcp 65001
```

To prisili UTF-8 kodiranje v terminalu. Alternativno uporabite Windows Terminal, ki bolje podpira Unicode.

### Odpravljanje napak API klicev

**Težava**: Napake overjanja, omejitve hitrosti ali nepričakovani odgovori modela AI

**Rešitev**: Primeri vključujejo `.logRequests(true)` in `.logResponses(true)` za prikaz API klicev v konzoli. To pomaga pri odpravljanju napak overjanja, omejitev hitrosti ali nepričakovanih odgovorov. V produkciji odstranite te zastavice, da zmanjšate šum v dnevnikih.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v izvirnem jeziku naj velja kot avtoritativni vir. Za ključne informacije priporočamo strokoven človeški prevod. Nismo odgovorni za morebitna nesporazuma ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->