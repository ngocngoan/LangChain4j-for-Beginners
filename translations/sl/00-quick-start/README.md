# Modul 00: Hiter začetek

## Kazalo

- [Uvod](../../../00-quick-start)
- [Kaj je LangChain4j?](../../../00-quick-start)
- [Odvisnosti LangChain4j](../../../00-quick-start)
- [Predpogoji](../../../00-quick-start)
- [Nastavitev](../../../00-quick-start)
  - [1. Pridobite svoj GitHub žeton](../../../00-quick-start)
  - [2. Nastavite svoj žeton](../../../00-quick-start)
- [Zagon primerov](../../../00-quick-start)
  - [1. Osnovni klepet](../../../00-quick-start)
  - [2. Vzorci pozivov](../../../00-quick-start)
  - [3. Klic funkcij](../../../00-quick-start)
  - [4. Vprašanja in odgovori o dokumentih (Easy RAG)](../../../00-quick-start)
  - [5. Odgovorna AI](../../../00-quick-start)
- [Kaj posamezen primer prikazuje](../../../00-quick-start)
- [Naslednji koraki](../../../00-quick-start)
- [Reševanje težav](../../../00-quick-start)

## Uvod

Ta hiter začetek je namenjen, da vas čim prej spravi v pogon z LangChain4j. Pokriva absolutne osnove ustvarjanja AI aplikacij z LangChain4j in GitHub modeli. V naslednjih modulih boste prešli na Azure OpenAI in GPT-5.2 ter se poglobili v vsak koncept.

## Kaj je LangChain4j?

LangChain4j je Java knjižnica, ki poenostavlja ustvarjanje aplikacij, ki temeljijo na AI. Namesto da bi se ukvarjali z HTTP odjemalci in JSON razčlenjevanjem, delate s čistimi Java API-ji.

"Veriga" v LangChain pomeni povezovanje več komponent - lahko povežete poziv z modelom in z razčlenjevalnikom ali povežete več AI klicev zaporedoma, kjer en izhod služi kot naslednji vhod. Ta hiter začetek se osredotoča na osnovna načela, preden raziskujete bolj zapletene verige.

<img src="../../../translated_images/sl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Povezovanje komponent v LangChain4j - gradniki se povezujejo, da ustvarijo močne AI delovne tokove*

Uporabili bomo tri osnovne komponente:

**ChatModel** - Vmesnik za interakcijo z AI modeli. Pokličete `model.chat("prompt")` in dobite niz odgovora. Uporabljamo `OpenAiOfficialChatModel`, ki deluje z OpenAI-kompatibilnimi končnimi točkami, kot so GitHub modeli.

**AiServices** - Ustvari tipno varne vmesnike AI storitev. Definirajte metode, jih označite z `@Tool`, LangChain4j pa poskrbi za orkestracijo. AI samodejno kliče vaše Java metode, ko je to potrebno.

**MessageWindowChatMemory** - Ohranja zgodovino pogovora. Brez tega je vsak zahtevek neodvisen. Z njim se AI spomni prejšnjih sporočil in vzdržuje kontekst skozi več krogov.

<img src="../../../translated_images/sl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arhitektura LangChain4j - osnovne komponente sodelujejo za pogon vaših AI aplikacij*

## Odvisnosti LangChain4j

Ta hiter začetek uporablja tri Maven odvisnosti v [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
Modul `langchain4j-open-ai-official` zagotavlja razred `OpenAiOfficialChatModel`, ki se povezuje z OpenAI-kompatibilnimi API-ji. GitHub modeli uporabljajo enak API format, zato ni potrebnega posebnega vmesnika - samo usmerite osnovni URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` zagotavlja avtomatsko razdeljevanje dokumentov, vdelavo in iskanje, tako da lahko ustvarite RAG aplikacije brez ročne konfiguracije posameznih korakov.

## Predpogoji

**Uporabljate razvojno posodo?** Java in Maven sta že nameščena. Potrebujete samo osebni dostopni žeton za GitHub.

**Lokalni razvoj:**
- Java 21+, Maven 3.9+
- Osebni žeton za dostop do GitHub (navodila spodaj)

> **Opomba:** Ta modul uporablja `gpt-4.1-nano` iz GitHub Modelov. Ne spreminjajte imena modela v kodi - konfiguriran je za delo z razpoložljivimi GitHub modeli.

## Nastavitev

### 1. Pridobite svoj GitHub žeton

1. Pojdite na [GitHub Nastavitve → Osebni dostopni žetoni](https://github.com/settings/personal-access-tokens)  
2. Kliknite "Generate new token"  
3. Nastavite opisno ime (npr. "LangChain4j Demo")  
4. Nastavite veljavnost (priporočeno 7 dni)  
5. Pod "Dovoljenja računa" poiščite "Models" in nastavite na "Samo za branje"  
6. Kliknite "Generate token"  
7. Kopirajte in shranite svoj žeton - ponovno ga ne boste videli

### 2. Nastavite svoj žeton

**Možnost 1: Uporaba VS Code (priporočeno)**

Če uporabljate VS Code, dodajte svoj žeton v `.env` datoteko v korenu projekta:

Če `.env` datoteka ne obstaja, kopirajte `.env.example` v `.env` ali ustvarite novo `.env` datoteko v korenu projekta.

**Primer `.env` datoteke:**  
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Nato lahko preprosto kliknete z desno tipko miške na katerokoli datoteko z demonstracijo (npr. `BasicChatDemo.java`) v Explorerju in izberete **"Run Java"** ali uporabite zagonske konfiguracije iz plošče Run and Debug.

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
  
## Zagon primerov

**Uporaba VS Code:** Preprosto kliknite z desno tipko na katerokoli datoteko z demonstracijo v Explorerju in izberite **"Run Java"** ali uporabite zagonske konfiguracije iz Run and Debug plošče (prepričajte se, da ste prej dodali svoj žeton v `.env` datoteko).

**Uporaba Maven:** Alternativno lahko zaženete iz ukazne vrstice:

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
  
AI samodejno kliče vaše Java metode, ko je to potrebno.

### 4. Vprašanja in odgovori o dokumentih (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Postavljajte vprašanja o vaših dokumentih z uporabo Easy RAG s samodejno vdelavo in iskanjem.

### 5. Odgovorna AI

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
Oglejte si, kako varnostni filtri AI blokirajo škodljivo vsebino.

## Kaj posamezen primer prikazuje

**Osnovni klepet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tukaj, da vidite LangChain4j v najbolj osnovni obliki. Ustvarili boste `OpenAiOfficialChatModel`, poslali poziv z `.chat()` in prejeli odgovor kot niz. To prikazuje temelje: kako inicializirati modele s prilagojenimi končnimi točkami in API ključem. Ko razumete ta vzorec, se vse ostalo gradi na njem.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```
  
> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) in vprašajte:
> - "Kako bi v tej kodi preklopil z GitHub Modelov na Azure OpenAI?"
> - "Katere druge parametre lahko konfiguriram v OpenAiOfficialChatModel.builder()?"
> - "Kako dodam pretočne odgovore namesto, da čakam na celoten odgovor?"

**Inženiring pozivov** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Zdaj, ko veste, kako govoriti z modelom, poglejmo, kaj mu rečete. Ta demo uporablja enako nastavitev modela, a prikazuje pet različnih vzorcev pozivov. Preizkusite zero-shot pozive za neposredna navodila, few-shot pozive, ki se učijo iz primerov, chain-of-thought pozive, ki razkrivajo korake razmišljanja, in role-based pozive, ki postavijo kontekst. Videli boste, kako enak model da drastično različne rezultate glede na to, kako oblikujete zahtevo.

Demo tudi prikazuje predloge pozivov, ki so močan način za ustvarjanje ponovno uporabnih pozivov z uporabo spremenljivk.  
Spodnji primer prikazuje poziv z uporabo LangChain4j `PromptTemplate` za izpolnitev spremenljivk. AI bo odgovoril na podlagi danega cilja in dejavnosti.

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
  
> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) in vprašajte:
> - "Kaj je razlika med zero-shot in few-shot pozivanjem in kdaj naj uporabim katero?"
> - "Kako parameter temperature vpliva na odgovore modela?"
> - "Katere tehnike obstajajo za preprečevanje napadov z vbrizgavanjem pozivov v produkciji?"
> - "Kako lahko ustvarim ponovno uporabne objekte PromptTemplate za pogoste vzorce?"

**Integracija orodij** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j postane močan. Uporabili boste `AiServices` za ustvarjanje AI asistenta, ki lahko kliče vaše Java metode. Preprosto označite metode z `@Tool("opis")` in LangChain4j poskrbi za ostalo - AI sam odloča, kdaj uporabiti katero orodje glede na vprašanja uporabnika. To prikazuje klic funkcij, ključno tehniko za gradnjo AI, ki lahko izvaja dejanja, ne samo odgovarja na vprašanja.

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
  
> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) in vprašajte:
> - "Kako deluje oznaka @Tool in kaj LangChain4j počne z njo v ozadju?"
> - "Ali AI lahko kliče več orodij zaporedoma za reševanje zapletenih problemov?"
> - "Kaj se zgodi, če orodje vrže izjemo - kako naj upravljam napake?"
> - "Kako bi integriral pravi API namesto tega primera kalkulatorja?"

**Vprašanja in odgovori o dokumentih (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tukaj boste videli RAG (generiranje s podprtim iskanjem) z uporabo LangChain4jjevega pristopa "Easy RAG". Dokumenti se naložijo, samodejno razdelijo in vdelajo v pomnilniški shranjevalnik, nato pa vsebinsko iskanje zagotavlja relevantne dele AI ob času poizvedbe. AI odgovarja na podlagi vaših dokumentov, ne na splošnem znanju.

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
  
> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) in vprašajte:
> - "Kako RAG preprečuje AI halucinacije glede na uporabo učnih podatkov modela?"
> - "Kaj je razlika med tem enostavnim pristopom in lastno RAG verigo?"
> - "Kako bi to razširil za upravljanje več dokumentov ali večjih podatkovnih baz?"

**Odgovorna AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Ustvarjajte varnost AI z večplastno obrambo. Ta demo prikazuje dve plasti zaščite, ki delujeta skupaj:

**1. del: LangChain4j vnosne varovalke** - Blokirajo nevarne pozive, preden pridejo do LLM. Ustvarite lastne varovalke, ki preverjajo prepovedane ključne besede ali vzorce. Te se izvajajo v vaši kodi, zato so hitre in brezplačne.

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
  
**2. del: Varnostni filtri ponudnika** - GitHub modeli imajo vgrajene filtre, ki ujamejo tisto, kar bi vaše varovalke lahko spregledale. Videli boste surove blokade (napake HTTP 400) za hude kršitve in mehke zavrnitve, kjer AI vljudno zavrne.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) in vprašajte:
> - "Kaj je InputGuardrail in kako ustvarim svojega?"
> - "Kaj je razlika med surovo blokado in mehko zavrnitvijo?"
> - "Zakaj uporabljati tako varovalke kot filtre ponudnika skupaj?"

## Naslednji koraki

**Naslednji modul:** [01-uvod - Začetek z LangChain4j](../01-introduction/README.md)

---

**Navigacija:** [← Nazaj na glavno](../README.md) | [Naprej: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Reševanje težav

### Prvič gradnja z Maven

**Težava:** Začetni `mvn clean compile` ali `mvn package` traja dolgo (10-15 minut)

**Vzrok:** Maven mora prvič prenesti vse odvisnosti projekta (Spring Boot, LangChain4j knjižnice, Azure SDK-je itd.).

**Rešitev:** To je normalno vedenje. Naslednje gradnje bodo veliko hitrejše, saj se odvisnosti shranijo lokalno. Čas prenosa je odvisen od vaše hitrosti internetne povezave.

### Sintaksa Maven ukaza v PowerShell

**Težava:** Maven ukazi spodletijo z napako `Unknown lifecycle phase ".mainClass=..."`
**Vzrok**: PowerShell interpretira `=` kot operator dodeljevanja spremenljivke, kar prekine Maven sintakso lastnosti

**Rešitev**: Pred Maven ukazom uporabite operator za ustavitev razčlenjevanja `--%`:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` pove PowerShellu, da vse preostale argumente dobesedno posreduje Maven-u brez razlage.

### Prikaz emojijev v Windows PowerShell

**Težava**: Odgovori AI prikazujejo neberljive znake (npr. `????` ali `â??`) namesto emojijev v PowerShell-u

**Vzrok**: Privzeta kodna tabela PowerShell-a ne podpira UTF-8 emojijev

**Rešitev**: Pred zaganjanjem Java aplikacij zaženite ta ukaz:
```cmd
chcp 65001
```

To prisili uporabo UTF-8 kodiranja v terminalu. Alternativno lahko uporabite Windows Terminal, ki ima boljšo podporo za Unicode.

### Razhroščevanje klicev API-ja

**Težava**: Napake pri preverjanju pristnosti, omejitve stopenj ali nepričakovani odzivi iz AI modela

**Rešitev**: Primeri vključujejo `.logRequests(true)` in `.logResponses(true)`, da se prikažejo klici API v konzoli. To pomaga pri odpravljanju napak preverjanja pristnosti, omejitev stopenj ali nepričakovanih odzivov. V proizvodnji odstranite te zastavice, da zmanjšate število zapisov.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo storitve za prevajanje z umetno inteligenco [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku velja za avtoritativni vir. Za pomembne informacije priporočamo strokovni človeški prevod. Nismo odgovorni za morebitna nesporazumevanja ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->