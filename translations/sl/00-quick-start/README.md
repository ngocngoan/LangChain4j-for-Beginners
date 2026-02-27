# Modul 00: Hiter začetek

## Vsebina

- [Uvod](../../../00-quick-start)
- [Kaj je LangChain4j?](../../../00-quick-start)
- [Odvisnosti LangChain4j](../../../00-quick-start)
- [Predpogoji](../../../00-quick-start)
- [Namestitev](../../../00-quick-start)
  - [1. Pridobite svoj GitHub žeton](../../../00-quick-start)
  - [2. Nastavite svoj žeton](../../../00-quick-start)
- [Zaženite primere](../../../00-quick-start)
  - [1. Osnovni klepet](../../../00-quick-start)
  - [2. Vzorci pozivov](../../../00-quick-start)
  - [3. Klicanje funkcij](../../../00-quick-start)
  - [4. Vprašanja in odgovori o dokumentu (Easy RAG)](../../../00-quick-start)
  - [5. Odgovorna umetna inteligenca](../../../00-quick-start)
- [Kaj prikazuje vsak primer](../../../00-quick-start)
- [Naslednji koraki](../../../00-quick-start)
- [Reševanje težav](../../../00-quick-start)

## Uvod

Ta hiter začetek je namenjen, da vas čim prej spravi v pogon z LangChain4j. Pokriva najosnovnejše zgradbe AI aplikacij z LangChain4j in GitHub modeli. V naslednjih modulih boste uporabljali Azure OpenAI z LangChain4j za izdelavo bolj naprednih aplikacij.

## Kaj je LangChain4j?

LangChain4j je Java knjižnica, ki poenostavi izdelavo aplikacij z umetno inteligenco. Namesto, da se ukvarjate z HTTP klienti in razčlenjevanjem JSON, delate s čistimi Java API-ji.

"Veriga" v LangChain pomeni povezovanje več komponent – lahko povežete poziv z modelom in parserjem ali povežete več AI klicev, kjer izhod enega služi kot vhod naslednjemu. Ta hiter začetek se osredotoča na temelje, preden raziščemo bolj kompleksne verige.

<img src="../../../translated_images/sl/langchain-concept.ad1fe6cf063515e1.webp" alt="Koncept verižnega povezovanja LangChain4j" width="800"/>

*Verižna povezava komponent v LangChain4j - gradniki se združujejo za ustvarjanje zmogljivih AI potekov dela*

Uporabili bomo tri osnovne komponente:

**ChatModel** - Vmesnik za interakcijo z AI modeli. Pokličite `model.chat("prompt")` in dobite odgovor v obliki niza. Uporabljamo `OpenAiOfficialChatModel`, ki deluje z OpenAI-kompatibilnimi končnimi točkami, kot so GitHub modeli.

**AiServices** - Ustvari tipno varne vmesnike za AI storitve. Določite metode, jih označite z `@Tool` in LangChain4j poskrbi za orkestracijo. AI samodejno kliče vaše Java metode, ko je to potrebno.

**MessageWindowChatMemory** - Ohranja zgodovino pogovora. Brez tega je vsak zahtevek neodvisen. Z njim AI pomni pretekla sporočila in ohranja kontekst skozi več krogov pogovora.

<img src="../../../translated_images/sl/architecture.eedc993a1c576839.webp" alt="Arhitektura LangChain4j" width="800"/>

*Arhitektura LangChain4j - osnovne komponente sodelujejo za poganjanje vaših AI aplikacij*

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

Modul `langchain4j-open-ai-official` zagotavlja razred `OpenAiOfficialChatModel`, ki se poveže z OpenAI-kompatibilnimi API-ji. GitHub modeli uporabljajo isti format API-ja, zato posebni adapter ni potreben – samo nastavite osnovni URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` omogoča avtomatsko razdeljevanje dokumentov, vdelavo in poizvedovanje, da lahko gradite RAG aplikacije brez ročne konfiguracije vsakega koraka.

## Predpogoji

**Uporabljate razvojno okolje (Dev Container)?** Java in Maven sta že nameščena. Potrebujete samo GitHub osebni dostopni žeton.

**Lokalni razvoj:**
- Java 21+, Maven 3.9+
- GitHub osebni dostopni žeton (navodila spodaj)

> **Opomba:** Ta modul uporablja `gpt-4.1-nano` iz GitHub modelov. Ne spremenite imena modela v kodi – konfiguriran je za delo z razpoložljivimi modeli GitHuba.

## Namestitev

### 1. Pridobite svoj GitHub žeton

1. Pojdite na [GitHub nastavitve → Osebni dostopni žetoni](https://github.com/settings/personal-access-tokens)
2. Kliknite "Generiraj nov žeton"
3. Nastavite opisni naziv (npr. "LangChain4j Demo")
4. Nastavite rok veljavnosti (priporočeno 7 dni)
5. Pod "Dovoljenja računa" poiščite "Models" in nastavite na "Samo za branje"
6. Kliknite "Generiraj žeton"
7. Kopirajte in shranite svoj žeton - ne boste ga več videli

### 2. Nastavite svoj žeton

**Možnost 1: Uporaba VS Code (priporočeno)**

Če uporabljate VS Code, dodajte žeton v `.env` datoteko v korenu projekta:

Če datoteka `.env` ne obstaja, kopirajte `.env.example` v `.env` ali ustvarite novo `.env` datoteko v korenu projekta.

**Primer `.env` datoteke:**
```bash
# V /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Nato lahko preprosto kliknete desno na katerokoli demo datoteko (npr. `BasicChatDemo.java`) v Raziskovalcu in izberete **"Run Java"** ali uporabite konfiguracije zagona iz panela Zaženi in odpravi napake.

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

**Uporaba VS Code:** Preprosto kliknite desno na katerokoli demo datoteko v Raziskovalcu in izberite **"Run Java"**, ali uporabite konfiguracijo zagona iz panela Zaženi in odpravi napake (poskrbite, da ste žeton najprej dodali v `.env` datoteko).

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

Pokaže zero-shot, few-shot, verigo misli in vloge pri pozivanju.

### 3. Klicanje funkcij

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI samodejno kliče vaše Java metode, ko je to potrebno.

### 4. Vprašanja in odgovori o dokumentu (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Postavljajte vprašanja o svojih dokumentih z uporabo Easy RAG z avtomatsko vdelavo in poizvedovanjem.

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

## Kaj prikazuje vsak primer

**Osnovni klepet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Začnite tukaj, da vidite LangChain4j v najpreprostejši obliki. Ustvarili boste `OpenAiOfficialChatModel`, poslali poziv z `.chat()` in prejeli odgovor. To prikazuje temelje: kako inicializirati modele s prilagojenimi končnimi točkami in API ključi. Ko razumete ta vzorec, se vse ostalo gradi na njem.

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
> - "Kako preklopim iz GitHub modelov na Azure OpenAI v tej kodi?"
> - "Katere druge parametre lahko nastavim v OpenAiOfficialChatModel.builder()?"
> - "Kako dodam pretakanje odgovorov namesto čakanja na popoln odgovor?"

**Inženiring pozivov** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Zdaj ko veste, kako govoriti z modelom, poglejmo, kaj mu rečete. Ta demo uporablja isto nastavitev modela, a pokaže pet različnih vzorcev pozivov. Poskusite zero-shot za neposredna navodila, few-shot, ki se uči iz primerov, verigo misli, ki razkriva logične korake, in pozive z vlogami za nastavitev konteksta. Videli boste, kako isti model daje drastično različne rezultate glede na obliko vašega poziva.

Demo prav tako prikazuje predloge pozivov, ki so močno orodje za ustvarjanje ponovno uporabnih pozivov z spremenljivkami.
Spodnji primer prikazuje poziv z uporabo LangChain4j `PromptTemplate` za izpolnjevanje spremenljivk. AI bo odgovarjal na podlagi podanega cilja in dejavnosti.

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
> - "Kakšna je razlika med zero-shot in few-shot pozivanjem in kdaj uporabljati kateri?"
> - "Kako parameter temperature vpliva na odgovore modela?"
> - "Katere tehnike obstajajo, da preprečimo vdore prek pozivov v produkciji?"
> - "Kako lahko ustvarim ponovno uporabne objekte PromptTemplate za pogoste vzorce?"

**Integracija orodij** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j postane močan. Uporabili boste `AiServices`, da ustvarite pomočnika AI, ki lahko kliče vaše Java metode. Z le označevanjem metod z `@Tool("opis")` LangChain4j poskrbi za ostalo – AI samodejno odloča, kdaj uporabiti katero orodje glede na uporabnikova vprašanja. To prikazuje klicanje funkcij, ključno tehniko za izdelavo AI, ki lahko izvrši dejanja, ne le odgovarja na vprašanja.

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
> - "Ali lahko AI kliče več orodij v zaporedju za reševanje kompleksnih problemov?"
> - "Kaj se zgodi, če orodje povzroči izjemo – kako naj ravnam z napakami?"
> - "Kako bi integriral pravi API namesto tega kalkulatorja kot primer?"

**Vprašanja in odgovori o dokumentu (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tu boste videli RAG (generacijo z dodatkom pridobivanja) z uporabo LangChain4j pristopa "Easy RAG". Dokumenti se naložijo, avtomatsko razdelijo in vdelajo v shrambo v spominu, nato pa vsebinski iskalnik ponudi relevantne dele AI ob poizvedbi. AI odgovarja na podlagi vaših dokumentov, ne splošnega znanja.

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
> - "Kako RAG preprečuje AI halucinacije v primerjavi z uporabo trenažnih podatkov modela?"
> - "Kakšna je razlika med tem enostavnim pristopom in prilagojeno RAG cevjo?"
> - "Kako bi to razširil za več dokumentov ali večje baze znanja?"

**Odgovorna umetna inteligenca** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Zgradite varnost AI s plastmi obrambe. Ta demo prikazuje dve sloji zaščite, ki delujeta skupaj:

**Del 1: LangChain4j vhodne varovalke** - Blokirajo nevarne pozive preden dosežejo LLM. Ustvarite lastne varovalke, ki preverjajo prepovedane ključne besede ali vzorce. Te tečejo v vaši kodi, zato so hitre in brezplačne.

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

**Del 2: Varnostni filtri ponudnika** - GitHub modeli imajo vgrajene filtre, ki zajamejo, kar varovalke morda spregledajo. Videli boste močne bloke (HTTP 400 napake) pri hudih kršitvah in mehke zavrnitve, kjer AI vljudno zavrne.

> **🤖 Poskusite z [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) in vprašajte:
> - "Kaj je InputGuardrail in kako ustvarim svojo?"
> - "Kakšna je razlika med močnim blokom in mehko zavrnitvijo?"
> - "Zakaj uporabljati varovalke in filtre ponudnika skupaj?"

## Naslednji koraki

**Naslednji modul:** [01-uvod – Začnite z LangChain4j in gpt-5 na Azure](../01-introduction/README.md)

---

**Navigacija:** [← Nazaj na glavno](../README.md) | [Naprej: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Reševanje težav

### Prvi Maven build

**Težava**: Začetni `mvn clean compile` ali `mvn package` traja dolgo (10–15 minut)

**Vzrok**: Maven mora prvič prenesti vse odvisnosti projekta (Spring Boot, LangChain4j knjižnice, Azure SDK-je, itd.)

**Rešitev**: To je normalno. Kasnejši buildi bodo veliko hitrejši, ker so odvisnosti predpomnjene lokalno. Čas prenosa je odvisen od hitrosti vaše povezave.

### Sintaksa Maven ukazov v PowerShell-u

**Težava**: Maven ukazi se končajo z napako `Unknown lifecycle phase ".mainClass=..."`
**Vzrok**: PowerShell obravnava `=` kot operator dodeljevanja spremenljivke, kar prekine sintakso lastnosti Maven

**Rešitev**: Pred ukaz Maven uporabite operator za ustavitev razčlenjevanja `--%`:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` pove PowerShellu, naj vse ostale argumente dobi dobesedno in jih ne interpretira, temveč posreduje Maven.

### Prikaz emodžijev v Windows PowerShellu

**Težava**: Odgovori AI prikazujejo nerazumljive znake (npr. `????` ali `â??`) namesto emodžijev v PowerShellu

**Vzrok**: Privzeta kodna tabela PowerShell ne podpira emodžijev UTF-8

**Rešitev**: Zaženite ta ukaz pred izvajanjem Java aplikacij:
```cmd
chcp 65001
```

To prisili kodiranje UTF-8 v terminalu. Kot alternativo uporabite Windows Terminal, ki bolje podpira Unicode.

### Odpravljanje napak klicev API

**Težava**: Napake avtentikacije, omejitve hitrosti ali nepričakovani odgovori modela AI

**Rešitev**: Primeri vključujejo `.logRequests(true)` in `.logResponses(true)`, ki prikažeta klice API v konzoli. To pomaga pri odpravljanju napak avtentikacije, omejitvah hitrosti ali nepričakovanih odgovorih. Te zastavice odstranite v produkciji za zmanjšanje hrupa dnevnika.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o omejitvi odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za avtomatsko prevajanje AI [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvorni jezik je treba šteti za avtoritativni vir. Za kritične informacije priporočamo strokovni prevod s strani človeka. Nismo odgovorni za kakršne koli nerazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->