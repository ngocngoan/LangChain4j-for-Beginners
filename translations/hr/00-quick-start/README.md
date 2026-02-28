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
  - [2. Uzorci promptova](../../../00-quick-start)
  - [3. Pozivanje funkcija](../../../00-quick-start)
  - [4. Pitanja i odgovori s dokumentima (Easy RAG)](../../../00-quick-start)
  - [5. Odgovorni AI](../../../00-quick-start)
- [Što svaki primjer pokazuje](../../../00-quick-start)
- [Sljedeći koraci](../../../00-quick-start)
- [Rješavanje problema](../../../00-quick-start)

## Uvod

Ovaj brzi početak je namijenjen da vas što brže uvede u rad s LangChain4j-om. Pokriva apsolutne osnove izgradnje AI aplikacija s LangChain4j i GitHub modelima. U sljedećim modulima koristit ćete Azure OpenAI s LangChain4j za izradu naprednijih aplikacija.

## Što je LangChain4j?

LangChain4j je Java knjižnica koja pojednostavljuje izradu AI-pokretanih aplikacija. Umjesto da se bavite HTTP klijentima i parsiranjem JSON-a, radite s čistim Java API-jima.

"Chain" u LangChain odnosi se na povezivanje više komponenti - možete povezivati prompt s modelom, pa potom s parserom, ili povezivati više AI poziva gde jedan izlaz služi kao ulaz za sljedeći. Ovaj brzi početak se usredotočuje na osnove prije nego što istraži složenije lance.

<img src="../../../translated_images/hr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Povezivanje komponenti u LangChain4j - građevni blokovi se povezuju kako bi se kreirali moćni AI tijekovi*

Koristit ćemo tri osnovne komponente:

**ChatModel** - Sučelje za interakcije s AI modelima. Pozovite `model.chat("prompt")` i dobit ćete tekstualni odgovor. Koristimo `OpenAiOfficialChatModel` koji radi s OpenAI-kompatibilnim krajnjim točkama poput GitHub modela.

**AiServices** - Kreira tipove sigurna sučelja za AI usluge. Definirajte metode, označite ih s `@Tool`, a LangChain4j obavlja orkestraciju. AI automatski poziva vaše Java metode kad je potrebno.

**MessageWindowChatMemory** - Održava povijest razgovora. Bez toga je svaki zahtjev neovisni. S njim, AI pamti prethodne poruke i održava kontekst tijekom više krugova.

<img src="../../../translated_images/hr/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arhitektura LangChain4j - osnovne komponente rade zajedno kako bi pokretale vaše AI aplikacije*

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

Modul `langchain4j-open-ai-official` pruža klasu `OpenAiOfficialChatModel` koja se povezuje s OpenAI-kompatibilnim API-jima. GitHub modeli koriste isti API format, pa nije potreban poseban adapter - samo usmjerite bazni URL na `https://models.github.ai/inference`.

Modul `langchain4j-easy-rag` pruža automatsko dijeljenje dokumenata, ugrađivanje i dohvat tako da možete graditi RAG aplikacije bez ručne konfiguracije svakog koraka.

## Preduvjeti

**Koristite Dev Container?** Java i Maven su već instalirani. Potreban vam je samo GitHub Personal Access Token.

**Lokalni razvoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (upute dolje)

> **Napomena:** Ovaj modul koristi `gpt-4.1-nano` iz GitHub modela. Nemojte mijenjati naziv modela u kodu - konfigurirano je za rad s dostupnim GitHub modelima.

## Postavljanje

### 1. Nabavite svoj GitHub token

1. Idite na [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Kliknite "Generate new token"
3. Postavite opisni naziv (npr. "LangChain4j Demo")
4. Postavite isteka (preporučeno 7 dana)
5. Pod "Account permissions", pronađite "Models" i postavite na "Read-only"
6. Kliknite "Generate token"
7. Kopirajte i spremite token - više ga nećete moći vidjeti

### 2. Postavite svoj token

**Opcija 1: Korištenje VS Code-a (preporučeno)**

Ako koristite VS Code, dodajte token u `.env` datoteku u korijenu projekta:

Ako `.env` datoteka ne postoji, kopirajte `.env.example` u `.env` ili kreirajte novu `.env` datoteku u korijenu projekta.

**Primjer `.env` datoteke:**
```bash
# U /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Zatim jednostavno kliknite desnom tipkom miša na bilo koju demo datoteku (npr. `BasicChatDemo.java`) u Exploreru i odaberite **"Run Java"** ili koristite konfiguracije za pokretanje iz panela Run and Debug.

**Opcija 2: Korištenje terminala**

Postavite token kao varijablu okoliša:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Pokrenite primjere

**Korištenje VS Code-a:** Jednostavno kliknite desnom tipkom na bilo koju demo datoteku u Exploreru i odaberite **"Run Java"**, ili koristite konfiguracije za pokretanje iz panela Run and Debug (provjerite da ste prethodno dodali token u `.env` datoteku).

**Korištenje Mavena:** Alternativno, možete pokrenuti iz komandne linije:

### 1. Osnovni chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Uzorci promptova

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Prikazuje zero-shot, few-shot, lanac razmišljanja i promptove s ulogama.

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

### 4. Pitanja i odgovori s dokumentima (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Postavljajte pitanja o svojim dokumentima koristeći Easy RAG s automatskim ugradbama i dohvatom.

### 5. Odgovorni AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Pogledajte kako AI sigurnosni filteri blokiraju štetan sadržaj.

## Što svaki primjer pokazuje

**Osnovni chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Počnite ovdje da vidite LangChain4j u najjednostavnijem obliku. Kreirat ćete `OpenAiOfficialChatModel`, poslati prompt s `.chat()` i dobiti odgovor. Ovo pokazuje temelj: kako inicijalizirati modele s prilagođenim krajnjim točkama i API ključevima. Kad shvatite ovaj obrazac, sve ostalo se na njemu gradi.

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
> - "Kako bih prešao s GitHub modela na Azure OpenAI u ovom kodu?"
> - "Koje druge parametre mogu konfigurirati u OpenAiOfficialChatModel.builder()?"
> - "Kako dodati streaming odgovore umjesto čekanja kompletnog odgovora?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sad kad znate kako komunicirati s modelom, pogledajmo što mu govorite. Ovaj demo koristi istu postavku modela, ali pokazuje pet različitih obrazaca promptova. Isprobajte zero-shot promptove za direktne upute, few-shot promptove koji uče iz primjera, lanac razmišljanja koji otkriva korake zaključivanja, i promptove s ulogama koji postavljaju kontekst. Vidjet ćete kako isti model daje dramatično različite rezultate ovisno o načinu formulacije zahtjeva.

Demo također pokazuje predloške promptova, što je moćan način stvaranja ponovljivih promptova s varijablama.
Donji primjer prikazuje prompt koristeći LangChain4j `PromptTemplate` za popunjavanje varijabli. AI će odgovoriti na temelju danog odredišta i aktivnosti.

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
> - "Koja je razlika između zero-shot i few-shot promptanja i kada trebam koristiti koji?"
> - "Kako parametar temperature utječe na odgovore modela?"
> - "Koje su tehnike za sprječavanje napada unosom prompta u produkciji?"
> - "Kako mogu kreirati ponovljive PromptTemplate objekte za uobičajene obrasce?"

**Integracija alata** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ovdje LangChain4j postaje moćan. Koristit ćete `AiServices` za kreiranje AI asistenta koji može pozivati vaše Java metode. Samo označite metode s `@Tool("opis")` i LangChain4j obavlja ostatak - AI automatski odlučuje kada koristiti koji alat na temelju zahtjeva korisnika. Ovo pokazuje pozivanje funkcija, ključnu tehniku za izgradnju AI koji može poduzimati akcije, a ne samo odgovarati na pitanja.

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
> - "Kako radi @Tool anotacija i što LangChain4j s njom radi iza scene?"
> - "Može li AI pozivati više alata u nizu za rješavanje kompleksnih problema?"
> - "Što se događa ako alat izbaci iznimku - kako trebaš rukovati greškama?"
> - "Kako bih integrirao pravi API umjesto ovog primjera kalkulatora?"

**Pitanja i odgovori s dokumentima (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ovdje vidite RAG (retrieval-augmented generation) koristeći LangChain4j pristup "Easy RAG". Dokumenti se učitavaju, automatski dijele i ugrade u memorijski spremnik, a zatim dohvatitelj sadržaja isporučuje relevantne dijelove AI-u u vrijeme upita. AI odgovara na temelju vaših dokumenata, ne svoje opće baze znanja.

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
> - "Kako RAG sprječava AI halucinacije u usporedbi s korištenjem podataka za treniranje modela?"
> - "Koja je razlika između ovog lakog pristupa i prilagođenog RAG tijeka?"
> - "Kako bih mogao skalirati ovo za rad s više dokumenata ili većim bazama znanja?"

**Odgovorni AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Izgradite sigurnost AI sustava obrambenim slojevima. Ovaj demo pokazuje dva sloja zaštite koji rade zajedno:

**Dio 1: LangChain4j ulazne zaštite** - Blokira opasne promptove prije nego što dođu do LLM-a. Kreirajte prilagođene zaštite koje provjeravaju zabranjene riječi ili obrasce. Ovi se izvršavaju u vašem kodu, pa su brzi i besplatni.

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

**Dio 2: Sigurnosni filteri providera** - GitHub modeli imaju ugrađene filtere koji hvataju ono što zaštite možda propuste. Vidjet ćete stroge blokade (HTTP 400 pogreške) za ozbiljne prekršaje i blage odbijanja gdje AI ljubazno odbija.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) Chat:** Otvorite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i pitajte:
> - "Što je InputGuardrail i kako mogu kreirati vlastiti?"
> - "Koja je razlika između stroge blokade i blagih odbijanja?"
> - "Zašto koristiti i zaštite i filtre providera zajedno?"

## Sljedeći koraci

**Sljedeći modul:** [01-introduction - Početak rada s LangChain4j i gpt-5 na Azure](../01-introduction/README.md)

---

**Navigacija:** [← Povratak na glavni](../README.md) | [Sljedeće: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Rješavanje problema

### Prvo Maven kompajliranje

**Problem:** Početni `mvn clean compile` ili `mvn package` traje dugo (10-15 minuta)

**Uzrok:** Maven mora preuzeti sve ovisnosti projekta (Spring Boot, LangChain4j knjižnice, Azure SDK-ove itd.) pri prvom kompajliranju.

**Rješenje:** Ovo je normalno ponašanje. Sljedeća kompajliranja bit će mnogo brža jer su ovisnosti spremljene lokalno. Vrijeme preuzimanja ovisi o brzini vaše mreže.

### Sintaksa Maven naredbi u PowerShellu

**Problem:** Maven naredbe ne uspijevaju s greškom `Unknown lifecycle phase ".mainClass=..."`
**Uzrok**: PowerShell tumači `=` kao operator dodjele varijable, što prekida Maven sintaksu svojstava

**Rješenje**: Koristite operator zaustavljanja parsiranja `--%` prije Maven naredbe:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` govori PowerShellu da sve preostale argumente proslijedi doslovno Maven-u bez tumačenja.

### Prikaz Emojija u Windows PowerShellu

**Problem**: AI odgovori prikazuju nečitljive znakove (npr. `????` ili `â??`) umjesto emojija u PowerShellu

**Uzrok**: Zadano kodiranje PowerShella ne podržava UTF-8 emojije

**Rješenje**: Pokrenite ovu naredbu prije izvršavanja Java aplikacija:
```cmd
chcp 65001
```

Ovo forsira UTF-8 kodiranje u terminalu. Alternativno, koristite Windows Terminal koji ima bolju podršku za Unicode.

### Otklanjanje Pogrešaka u API Pozivima

**Problem**: Pogreške autentikacije, ograničenja brzine ili neočekivani odgovori iz AI modela

**Rješenje**: Primjeri uključuju `.logRequests(true)` i `.logResponses(true)` za prikaz API poziva u konzoli. To pomaže pri otklanjanju pogrešaka autentikacije, ograničenja brzine ili neočekivanih odgovora. Maknite ove zastavice u produkciji kako biste smanjili šum u zapisima.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden pomoću AI usluge prijevoda [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim i autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili netočnosti koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->