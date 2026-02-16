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
  - [4. Pitanja i odgovori o dokumentu (RAG)](../../../00-quick-start)
  - [5. Odgovorni AI](../../../00-quick-start)
- [Što pokazuje svaki primjer](../../../00-quick-start)
- [Sljedeći koraci](../../../00-quick-start)
- [Rješavanje problema](../../../00-quick-start)

## Uvod

Ovaj brzi početak je osmišljen da vas što brže uvede u rad s LangChain4j. Pokriva apsolutne osnove izrade AI aplikacija s LangChain4j i GitHub Modelima. U narednim modulima koristit ćete Azure OpenAI s LangChain4j za izgradnju naprednijih aplikacija.

## Što je LangChain4j?

LangChain4j je Java biblioteka koja pojednostavljuje izradu aplikacija potpomognutih umjetnom inteligencijom. Umjesto da radite s HTTP klijentima i parsiranjem JSON-a, radite s čistim Java API-jima.

"Chain" u LangChain odnosi se na povezivanje više komponenti - možete povezati prompt s modelom, zatim s parserom, ili povezati više AI poziva gdje izlaz jednog ulazi kao ulaz u sljedeći. Ovaj brzi početak fokusira se na osnove prije nego što istražite složenije lance.

<img src="../../../translated_images/hr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Koncept povezivanja" width="800"/>

*Povezivanje komponenti u LangChain4j - gradivne jedinice koje se spajaju da kreiraju snažne AI radne tokove*

Koristit ćemo tri ključne komponente:

**ChatLanguageModel** - Sučelje za interakcije s AI modelima. Pozovite `model.chat("prompt")` i dobijete odgovor kao string. Koristimo `OpenAiOfficialChatModel` koji radi s OpenAI kompatibilnim endpointima poput GitHub Modela.

**AiServices** - Kreira tip-sigurna AI servisna sučelja. Definirajte metode, označite ih s `@Tool`, i LangChain4j upravlja orkestracijom. AI automatski poziva vaše Java metode kad je potrebno.

**MessageWindowChatMemory** - Održava povijest razgovora. Bez toga svaki zahtjev je neovisan. S njim AI pamti prethodne poruke i održava kontekst kroz više koraka.

<img src="../../../translated_images/hr/architecture.eedc993a1c576839.webp" alt="LangChain4j Arhitektura" width="800"/>

*LangChain4j arhitektura - ključne komponente koje zajedno pokreću vaše AI aplikacije*

## Ovisnosti LangChain4j

Ovaj brzi početak koristi dvije Maven ovisnosti u [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modul `langchain4j-open-ai-official` pruža klasu `OpenAiOfficialChatModel` koja se povezuje s OpenAI-kompatibilnim API-jem. GitHub Models koristi isti API format, pa nije potreban poseban adapter - samo usmjerite bazni URL na `https://models.github.ai/inference`.

## Preduvjeti

**Koristite li Dev Container?** Java i Maven već su instalirani. Trebate samo GitHub Personal Access Token.

**Lokalni razvoj:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (upute dolje)

> **Napomena:** Ovaj modul koristi `gpt-4.1-nano` iz GitHub Modela. Nemojte mijenjati ime modela u kodu - konfigurirano je za rad s dostupnim GitHub modelima.

## Postavljanje

### 1. Nabavite svoj GitHub token

1. Idite na [GitHub Postavke → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Kliknite "Generate new token"
3. Postavite opisni naziv (npr. "LangChain4j Demo")
4. Postavite rok trajanja (preporučeno 7 dana)
5. Pod "Account permissions", pronađite "Models" i odaberite "Read-only"
6. Kliknite "Generate token"
7. Kopirajte i spremite svoj token - neće biti vidljiv ponovno

### 2. Postavite svoj token

**Opcija 1: Korištenje VS Code (preporučeno)**

Ako koristite VS Code, dodajte svoj token u `.env` datoteku u korijenu projekta:

Ako `.env` datoteka ne postoji, kopirajte `.env.example` u `.env` ili kreirajte novu `.env` datoteku u korijenu projekta.

**Primjer `.env` datoteke:**
```bash
# U /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Nakon toga jednostavno desnim klikom na bilo koju demo datoteku (npr. `BasicChatDemo.java`) u Exploreru odaberete **"Run Java"** ili koristite konfiguracije za pokretanje iz panela Run and Debug.

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

**Korištenje VS Code:** Jednostavno desnim klikom na bilo koju demo datoteku u Exploreru odaberite **"Run Java"**, ili koristite konfiguracije za pokretanje u Run and Debug panelu (provjerite da ste prvo dodali svoj token u `.env` datoteku).

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

### 2. Uzorci prompta

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Prikazuje zero-shot, few-shot, chain-of-thought i role-based promptove.

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

### 4. Pitanja i odgovori o dokumentu (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Postavljajte pitanja o sadržaju u `document.txt`.

### 5. Odgovorni AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vidite kako AI sigurnosni filteri blokiraju štetni sadržaj.

## Što pokazuje svaki primjer

**Osnovni chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Započnite ovdje da vidite LangChain4j u najjednostavnijem obliku. Kreirat ćete `OpenAiOfficialChatModel`, poslati prompt s `.chat()`, i dobiti odgovor. Ovo pokazuje temelj: kako inicijalizirati modele s prilagođenim endpointima i API ključevima. Nakon što shvatite ovaj obrazac, sve ostalo se na njega nadograđuje.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) i pitajte:
> - "Kako da prebacim s GitHub Modela na Azure OpenAI u ovom kodu?"
> - "Koje druge parametre mogu konfigurirati u OpenAiOfficialChatModel.builder()?"
> - "Kako dodati streaming odgovore umjesto čekanja na kompletan odgovor?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Sad kad znate kako komunicirati s modelom, istražimo što mu govorite. Ovaj demo koristi isti model, ali prikazuje pet različitih uzoraka prompta. Isprobajte zero-shot promptove za izravne upute, few-shot promptove koji uče na primjerima, chain-of-thought promptove koji otkrivaju razmišljanje i role-based promptove koji postavljaju kontekst. Vidjet ćete kako isti model daje drastično različite rezultate ovisno o načinu formulacije zahtjeva.

Demo također pokazuje prompt template, koji su moćan način za kreiranje ponovljivih promptova s varijablama.
Primjer ispod prikazuje prompt koristeći LangChain4j `PromptTemplate` za popunjavanje varijabli. AI će odgovoriti na temelju zadanog odredišta i aktivnosti.

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

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) i pitajte:
> - "Koja je razlika između zero-shot i few-shot promptanja i kada koristiti koji?"
> - "Kako parametar temperature utječe na odgovore modela?"
> - "Koje tehnike postoje za sprječavanje prompt injection napada u produkciji?"
> - "Kako mogu kreirati ponovljive PromptTemplate objekte za uobičajene uzorke?"

**Integracija alata** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Ovdje LangChain4j postaje moćan. Koristit ćete `AiServices` za kreiranje AI asistenta koji može pozivati vaše Java metode. Samo označite metode s `@Tool("opis")` i LangChain4j upravlja ostalim - AI automatski odlučuje kada koristiti koji alat na temelju korisničkog upita. Ovo pokazuje pozivanje funkcija, ključnu tehniku za izgradnju AI koji može poduzimati akcije, ne samo odgovarati na pitanja.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i pitajte:
> - "Kako radi @Tool oznaka i što LangChain4j radi s njom u pozadini?"
> - "Može li AI pozvati više alata u nizu da riješi složene probleme?"
> - "Što se događa ako alat baci iznimku - kako trebam rukovati pogreškama?"
> - "Kako bih integrirao stvarni API umjesto ovog primjera kalkulatora?"

**Pitanja i odgovori o dokumentu (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Ovdje ćete vidjeti temelj RAG (retrieval-augmented generation). Umjesto oslanjanja na podatke o treningu modela, učitate sadržaj iz [`document.txt`](../../../00-quick-start/document.txt) i uključite ga u prompt. AI odgovara na temelju vašeg dokumenta, a ne svoje opće baze znanja. Ovo je prvi korak prema izgradnji sustava koji mogu raditi s vašim vlastitim podacima.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Napomena:** Ovaj jednostavan pristup učitava cijeli dokument u prompt. Za velike datoteke (>10KB) prekoračit ćete ograničenja konteksta. Modul 03 pokriva dijeljenje na dijelove i vektorsku pretragu za produkcijske RAG sustave.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) i pitajte:
> - "Kako RAG sprječava AI halucinacije u usporedbi s korištenjem podataka za trening modela?"
> - "Koja je razlika između ovog jednostavnog pristupa i korištenja vektorskih embeddinga za dohvat?"
> - "Kako da skaliram ovo za obradu više dokumenata ili veće baze znanja?"
> - "Koje su najbolje prakse za strukturiranje prompta kako bi AI koristio samo dani kontekst?"

**Odgovorni AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Izgradite sigurnost AI-a s obrambenim slojevima. Ovaj demo prikazuje dva sloja zaštite koja rade zajedno:

**Dio 1: LangChain4j Input Guardrails** - Blokira opasne promptove prije nego što dođu do LLM-a. Kreirajte prilagođene guardrail-e koji provjeravaju zabranjene ključne riječi ili obrasce. Ovi se izvršavaju u vašem kodu, tako da su brzi i besplatni.

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

**Dio 2: Sigurnosni filteri dobavljača** - GitHub Models ima ugrađene filtere koji hvataju ono što bi vaši guardrail-i mogli propustiti. Vidjet ćete tvrde blokade (HTTP 400 greške) za teška kršenja i mekane odbijanja gdje AI pristojno odbija.

> **🤖 Isprobajte s [GitHub Copilot](https://github.com/features/copilot) chatom:** Otvorite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i pitajte:
> - "Što je InputGuardrail i kako napraviti svoj?"
> - "Koja je razlika između tvrde blokade i mekog odbijanja?"
> - "Zašto koristiti guardrail-e i filtere dobavljača zajedno?"

## Sljedeći koraci

**Sljedeći modul:** [01-introduction - Početak rada s LangChain4j i gpt-5 na Azure](../01-introduction/README.md)

---

**Navigacija:** [← Natrag na početnu](../README.md) | [Sljedeće: Modul 01 - Uvod →](../01-introduction/README.md)

---

## Rješavanje problema

### Prvo Maven buildanje

**Problem**: Početni `mvn clean compile` ili `mvn package` traje dugo (10-15 minuta)

**Uzrok**: Maven mora preuzeti sve ovisnosti projekta (Spring Boot, LangChain4j biblioteke, Azure SDK-ove, itd.) pri prvom buildanju.

**Rješenje**: Ovo je normalno ponašanje. Sljedeća buildanja bit će znatno brža jer se ovisnosti keširaju lokalno. Vrijeme preuzimanja ovisi o brzini vaše mreže.
### PowerShell Maven naredbeni sintaksis

**Problem**: Maven naredbe ne uspijevaju s pogreškom `Unknown lifecycle phase ".mainClass=..."`

**Uzrok**: PowerShell tumači `=` kao operator dodjele varijable, što razbija Maven sintaksu svojstava

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

### Prikaz emotikona u Windows PowerShellu

**Problem**: AI odgovori prikazuju nečitljive znakove (npr. `????` ili `â??`) umjesto emotikona u PowerShellu

**Uzrok**: Zadano kodiranje PowerShella ne podržava UTF-8 emotikone

**Rješenje**: Pokrenite ovu naredbu prije izvršavanja Java aplikacija:
```cmd
chcp 65001
```

Ovo prisiljava korištenje UTF-8 kodiranja u terminalu. Alternativno, koristite Windows Terminal koji ima bolju Unicode podršku.

### Otklanjanje pogrešaka kod API poziva

**Problem**: Pogreške autentikacije, ograničenja brzine ili neočekivani odgovori od AI modela

**Rješenje**: Primjeri uključuju `.logRequests(true)` i `.logResponses(true)` za prikaz API poziva u konzoli. Ovo pomaže u otkrivanju pogrešaka autentikacije, ograničenja brzine ili neočekivanih odgovora. Uklonite ove zastavice u produkciji kako biste smanjili buku u zapisima.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj dokument preveden je pomoću AI prevodilačke usluge [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatizirani prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku smatra se autoritativnim izvorom. Za ključne informacije preporučujemo stručni ljudski prijevod. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešna tumačenja koja proizađu iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->