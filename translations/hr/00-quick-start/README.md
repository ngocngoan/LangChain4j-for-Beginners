# Modul 00: Brzi početak

## Sadržaj

- [Uvod](../../../00-quick-start)
- [Što je LangChain4j?](../../../00-quick-start)
- [Ovisnosti LangChain4j](../../../00-quick-start)
- [Preduvjeti](../../../00-quick-start)
- [Postavljanje](../../../00-quick-start)
  - [1. Nabavite svoj GitHub token](../../../00-quick-start)
  - [2. Postavite svoj token](../../../00-quick-start)
- [Pokrenite primjere](../../../00-quick-start)
  - [1. Osnovni chat](../../../00-quick-start)
  - [2. Uzorci prompta](../../../00-quick-start)
  - [3. Pozivanje funkcija](../../../00-quick-start)
  - [4. Pitanja i odgovori o dokumentima (Easy RAG)](../../../00-quick-start)
  - [5. Odgovorni AI](../../../00-quick-start)
- [Što svaki primjer prikazuje](../../../00-quick-start)
- [Sljedeći koraci](../../../00-quick-start)
- [Rješavanje problema](../../../00-quick-start)

## Uvod

Ovaj brzi početak namijenjen je da vas što brže uvede u rad s LangChain4j. Obuhvaća apsolutne osnove izgradnje AI aplikacija s LangChain4j i GitHub modelima. U sljedećim modulima prelazite na Azure OpenAI i GPT-5.2 te dublje istražujete svaki koncept.

## Što je LangChain4j?

LangChain4j je Java biblioteka koja pojednostavljuje izradu aplikacija pokretanih umjetnom inteligencijom. Umjesto da se bavite HTTP klijentima i parsiranjem JSON-a, radite s čistim Java API-jima.

"Chain" u LangChain odnosi se na povezivanje više komponenti – možete povezati prompt s modelom, zatim s parserom ili povezati više AI poziva gdje jedan izlaz služi kao sljedeći ulaz. Ovaj brzi početak fokusira se na osnove prije istraživanja složenijih lanaca.

<img src="../../../translated_images/hr/langchain-concept.ad1fe6cf063515e1.webp" alt="Koncept povezivanja u LangChain4j" width="800"/>

*Povezivanje komponenti u LangChain4j – gradivni blokovi koji se spajaju u moćne AI radne tijekove*

Koristit ćemo tri glavne komponente:

**ChatModel** - Sučelje za interakciju s AI modelima. Pozovite `model.chat("prompt")` i dobit ćete odgovor kao niz znakova. Koristimo `OpenAiOfficialChatModel` koji radi s OpenAI-kompatibilnim krajnjim točkama poput GitHub modela.

**AiServices** - Stvara tip-sigurna AI sučelja za usluge. Definirajte metode, označite ih s `@Tool` i LangChain4j upravlja orkestracijom. AI automatski poziva vaše Java metode kad je potrebno.

**MessageWindowChatMemory** - Održava povijest razgovora. Bez ovoga, svaki zahtjev je neovisan. S njim, AI pamti prethodne poruke i održava kontekst kroz više okreta.

<img src="../../../translated_images/hr/architecture.eedc993a1c576839.webp" alt="Arhitektura LangChain4j" width="800"/>

*Arhitektura LangChain4j – ključne komponente koje zajedno pokreću vaše AI aplikacije*

## Ovisnosti LangChain4j

Ovaj brzi početak koristi tri Maven ovisnosti u [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` pruža klasu `OpenAiOfficialChatModel` koja se povezuje s OpenAI-kompatibilnim API-jima. GitHub modeli koriste isti API format, stoga nije potreban poseban adapter - samo usmjerite osnovnu URL adresu na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` pruža automatsko dijeljenje dokumenata, ugradnju i dohvat, tako da možete izgraditi RAG aplikacije bez ručnog podešavanja svakog koraka.

## Preduvjeti

**Koristite li Dev Container?** Java i Maven su već instalirani. Trebate samo GitHub osobni token za pristup.

**Lokalni razvoj:**
- Java 21+, Maven 3.9+
- GitHub osobni token za pristup (upute dolje)

> **Napomena:** Ovaj modul koristi `gpt-4.1-nano` iz GitHub modela. Nemojte mijenjati ime modela u kodu – konfigurirano je za rad s dostupnim GitHub modelima.

## Postavljanje

### 1. Nabavite svoj GitHub token

1. Idite na [GitHub postavke → Osobni tokeni za pristup](https://github.com/settings/personal-access-tokens)
2. Kliknite "Generate new token"
3. Postavite opisni naziv (npr. "LangChain4j Demo")
4. Postavite rok trajanja (preporučeno 7 dana)
5. U odjeljku "Account permissions" pronađite "Models" i postavite na "Read-only"
6. Kliknite "Generate token"
7. Kopirajte i spremite svoj token – više ga nećete vidjeti

### 2. Postavite svoj token

**Opcija 1: Korištenje VS Code (preporučeno)**

Ako koristite VS Code, dodajte token u `.env` datoteku u korijenu projekta:

Ako `.env` datoteka ne postoji, kopirajte `.env.example` u `.env` ili kreirajte novu `.env` datoteku u korijenu projekta.

**Primjer `.env` datoteke:**
```bash
# U /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Zatim jednostavno desnim klikom na bilo koju demo datoteku (npr. `BasicChatDemo.java`) u Exploreru odaberite **"Run Java"** ili koristite konfiguracije za pokretanje u Run and Debug panelu.

**Opcija 2: Korištenje terminala**

Postavite token kao varijablu okoline:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Pokrenite primjere

**Korištenje VS Code:** Jednostavno desni klik na bilo koju demo datoteku u Exploreru i odaberite **"Run Java"**, ili koristite konfiguracije za pokretanje iz Run and Debug panela (uvjerite se da ste prethodno dodali token u `.env` datoteku).

**Korištenje Mavena:** Također možete pokrenuti iz naredbenog retka:

### 1. Osnovni chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Uzorci prompta

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Prikazuje zero-shot, few-shot, chain-of-thought i promptanje temeljeno na ulozi.

### 3. Pozivanje funkcija

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automatski poziva vaše Java metode kad je potrebno.

### 4. Pitanja i odgovori o dokumentima (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Postavite pitanja o svojim dokumentima koristeći Easy RAG s automatskom ugradnjom i dohvatom.

### 5. Odgovorni AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Pogledajte kako sigurnosni filtri AI blokiraju štetni sadržaj.

## Što svaki primjer prikazuje

**Osnovni chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Započnite ovdje da vidite LangChain4j u najjednostavnijem obliku. Kreirat ćete `OpenAiOfficialChatModel`, poslati prompt s `.chat()` i dobiti povratni odgovor. Ovo pokazuje temelj: kako inicijalizirati modele s prilagođenim krajnjim točkama i API ključevima. Kad shvatite ovaj obrazac, sve ostalo se nadograđuje na njega.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) i pitajte:
> - "Kako prelazim s GitHub modela na Azure OpenAI u ovom kodu?"
> - "Koje druge parametre mogu konfigurirati u OpenAiOfficialChatModel.builder()?"
> - "Kako dodati streaming odgovore umjesto čekanja cijelog odgovora?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sad kad znate kako razgovarati s modelom, istražimo što mu govorite. Ovaj demo koristi istu konfiguraciju modela, ali pokazuje pet različitih obrazaca promptanja. Isprobajte zero-shot promptove za izravne upute, few-shot promptove koji uče iz primjera, chain-of-thought promptove koji otkrivaju korake razmišljanja i promptove temeljene na ulozi koji postavljaju kontekst. Vidjet ćete kako isti model daje dramatično različite rezultate ovisno o načinu postavljanja zahtjeva.

Demo također pokazuje predloške prompta (`PromptTemplate`), moćan način za kreiranje ponovno upotrebljivih promptova s varijablama.
Sljedeći primjer prikazuje prompt koji koristi LangChain4j `PromptTemplate` za popunjavanje varijabli. AI će odgovoriti na temelju danog odredišta i aktivnosti.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) i pitajte:
> - "Koja je razlika između zero-shot i few-shot promptanja i kada koristiti svaki?"
> - "Kako parametar temperature utječe na odgovore modela?"
> - "Koje tehnike postoje za sprječavanje napada ubacivanjem promptova u produkciji?"
> - "Kako mogu kreirati ponovo upotrebljive Predloške Promptova za uobičajene obrasce?"

**Integracija alata** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ovdje LangChain4j postaje moćan. Koristit ćete `AiServices` da kreirate AI asistenta koji može pozivati vaše Java metode. Samo označite metode s `@Tool("opis")` i LangChain4j se brine za ostalo – AI automatski odlučuje kada koristiti koji alat na temelju korisničkih zahtjeva. Ovo pokazuje pozivanje funkcija, ključnu tehniku za izgradnju AI koja može poduzimati radnje, ne samo odgovarati na pitanja.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i pitajte:
> - "Kako radi @Tool anotacija i što LangChain4j radi s njom u pozadini?"
> - "Može li AI pozvati više alata u nizu za rješavanje složenih problema?"
> - "Što se događa ako alat baci iznimku – kako bih trebao rukovati greškama?"
> - "Kako bih integrirao pravi API umjesto ovog primjera kalkulatora?"

**Pitanja i odgovori o dokumentima (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ovdje ćete vidjeti RAG (generiranje potpomognuto dohvaćanjem) koristeći LangChain4j pristup "Easy RAG". Dokumenti se učitavaju, automatski dijele i ugrađuju u memorijsko spremište, zatim pretraživač sadržaja isporučuje relevantne dijelove AI tijekom upita. AI odgovara na temelju vaših dokumenata, a ne svoje opće baze znanja.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) i pitajte:
> - "Kako RAG sprječava AI halucinacije u usporedbi s korištenjem podataka o treningu modela?"
> - "Koja je razlika između ovog jednostavnog pristupa i prilagođene RAG cijevi?"
> - "Kako bih ovo skalirao za rad s više dokumenata ili većim bazama znanja?"

**Odgovorni AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Izgradite sigurnost AI s obrambenim slojevima. Ovaj demo prikazuje dva sloja zaštite koji rade zajedno:

**Dio 1: LangChain4j Input Guardrails** - Blokiraju opasne promptove prije nego što dođu do LLM-a. Kreirajte prilagođene zaštitne mehanizme koji provjeravaju zabranjene ključne riječi ili obrasce. Oni se izvršavaju u vašem kodu, pa su brzi i besplatni.

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

**Dio 2: Provajderovi sigurnosni filtri** - GitHub modeli imaju ugrađene filtre koji hvataju ono što vaši guardrailsi mogu propustiti. Vidjet ćete stroge blokade (HTTP 400 greške) za teška kršenja i blage odbijanja gdje AI ljubazno odbija.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i pitajte:
> - "Što je InputGuardrail i kako napraviti vlastiti?"
> - "Koja je razlika između stroge blokade i blagoga odbijanja?"
> - "Zašto koristiti istovremeno guardrails i filtre provajdera?"

## Sljedeći koraci

**Sljedeći modul:** [01-introduction - Početak rada s LangChain4j](../01-introduction/README.md)

---

**Navigacija:** [← Povratak na glavni](../README.md) | [Dalje: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Rješavanje problema

### Prvo pokretanje Mavena

**Problem**: Prvi `mvn clean compile` ili `mvn package` traje dugo (10-15 minuta)

**Uzrok**: Maven treba preuzeti sve ovisnosti projekta (Spring Boot, LangChain4j biblioteke, Azure SDK-ove itd.) prilikom prvog builda.

**Rješenje**: Ovo je normalno. Sljedeći buildovi bit će znatno brži jer se ovisnosti nalaze u lokalnoj predmemoriji. Vrijeme preuzimanja ovisi o brzini vaše mreže.

### Sintaksa PowerShell Maven naredbe

**Problem**: Maven naredbe ne uspijevaju s pogreškom `Unknown lifecycle phase ".mainClass=..."`
**Uzrok**: PowerShell interpretira `=` kao operator dodjele varijabli, što prekida sintaksu Maven svojstava

**Rješenje**: Koristite operator za zaustavljanje parsiranja `--%` prije Maven naredbe:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` govori PowerShellu da sve preostale argumente proslijedi doslovno Maven-u bez interpretacije.

### Prikaz emotikona u Windows PowerShellu

**Problem**: AI odgovori pokazuju čudne znakove (npr. `????` ili `â??`) umjesto emotikona u PowerShellu

**Uzrok**: Zadani kôd znakova PowerShella ne podržava UTF-8 emotikone

**Rješenje**: Pokrenite ovu naredbu prije izvođenja Java aplikacija:
```cmd
chcp 65001
```

Ovo prisiljava UTF-8 kodiranje u terminalu. Alternativno, koristite Windows Terminal koji ima bolju podršku za Unicode.

### Otklanjanje pogrešaka u API pozivima

**Problem**: Greške pri autentikaciji, ograničenja brzine ili neočekivani odgovori s AI modela

**Rješenje**: Primjeri uključuju `.logRequests(true)` i `.logResponses(true)` za prikaz API poziva u konzoli. Ovo pomaže u otklanjanju grešaka pri autentikaciji, ograničenjima brzine ili neočekivanim odgovorima. U proizvodnji uklonite ove oznake kako biste smanjili buku u zapisima.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden korištenjem AI usluge prevođenja [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporučuje se profesionalni ljudski prijevod. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešna tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->