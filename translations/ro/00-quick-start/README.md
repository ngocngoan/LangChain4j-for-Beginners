# Modulul 00: Pornire Rapidă

## Cuprins

- [Introducere](../../../00-quick-start)
- [Ce este LangChain4j?](../../../00-quick-start)
- [Dependențe LangChain4j](../../../00-quick-start)
- [Prerechizite](../../../00-quick-start)
- [Configurare](../../../00-quick-start)
  - [1. Obțineți Tokenul GitHub](../../../00-quick-start)
  - [2. Setează Tokenul](../../../00-quick-start)
- [Rulați Exemplele](../../../00-quick-start)
  - [1. Chat de bază](../../../00-quick-start)
  - [2. Modele de Prompt](../../../00-quick-start)
  - [3. Apelarea Funcțiilor](../../../00-quick-start)
  - [4. Întrebări și Răspunsuri pe Documente (Easy RAG)](../../../00-quick-start)
  - [5. Inteligență Artificială Responsabilă](../../../00-quick-start)
- [Ce Arată Fiecare Exemplu](../../../00-quick-start)
- [Pașii Următori](../../../00-quick-start)
- [Depanare](../../../00-quick-start)

## Introducere

Acest ghid de pornire rapidă este conceput să vă ajute să începeți să utilizați LangChain4j cât mai repede posibil. Acoperă elementele de bază absolute ale construirii aplicațiilor AI cu LangChain4j și Modelele GitHub. În modulele următoare veți folosi Azure OpenAI cu LangChain4j pentru a construi aplicații mai avansate.

## Ce este LangChain4j?

LangChain4j este o bibliotecă Java care simplifică dezvoltarea aplicațiilor asistate de AI. În loc să lucrați cu clienți HTTP și parsare JSON, utilizați API-uri Java clare.

„Lanțul” din LangChain se referă la conectarea în serie a mai multor componente - puteți conecta un prompt la un model, apoi la un parser, sau să legați mai multe apeluri AI împreună unde o ieșire servește ca intrare pentru următorul pas. Această inițiere rapidă se concentrează pe elementele fundamentale înainte de a explora lanțuri mai complexe.

<img src="../../../translated_images/ro/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceptul de lanț LangChain4j" width="800"/>

*Componente care se leagă în LangChain4j - blocurile de construcție se conectează pentru a crea fluxuri AI puternice*

Vom folosi trei componente de bază:

**ChatModel** - Interfața pentru interacțiunile cu modelul AI. Apelați `model.chat("prompt")` și primiți un șir de răspuns. Folosim `OpenAiOfficialChatModel` care funcționează cu endpoint-uri compatibile OpenAI, cum ar fi Modelele GitHub.

**AiServices** - Creează interfețe de servicii AI tipizate. Definiți metode, le anotați cu `@Tool`, iar LangChain4j se ocupă de orchestrare. AI-ul apelează automat metodele Java când este necesar.

**MessageWindowChatMemory** - Menține istoricul conversației. Fără aceasta, fiecare cerere este independentă. Cu ea, AI-ul își amintește mesajele anterioare și păstrează contextul pe mai multe cicluri de conversație.

<img src="../../../translated_images/ro/architecture.eedc993a1c576839.webp" alt="Arhitectura LangChain4j" width="800"/>

*Arhitectura LangChain4j - componentele de bază care lucrează împreună pentru a alimenta aplicațiile tale AI*

## Dependențe LangChain4j

Această inițiere rapidă folosește trei dependențe Maven în [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulul `langchain4j-open-ai-official` oferă clasa `OpenAiOfficialChatModel` care se conectează la API-uri compatibile OpenAI. Modelele GitHub folosesc același format API, deci nu este nevoie de un adaptor special - doar directionați URL-ul de bază la `https://models.github.ai/inference`.

Modulul `langchain4j-easy-rag` furnizează divizarea automată a documentelor, încorporarea și recuperarea astfel încât să puteți construi aplicații RAG fără să configurați manual fiecare pas.

## Prerechizite

**Folosiți Dev Container?** Java și Maven sunt deja instalate. Aveți nevoie doar de un Token de Acces Personal GitHub.

**Dezvoltare Locală:**
- Java 21+, Maven 3.9+
- Token de Acces Personal GitHub (instrucțiuni mai jos)

> **Notă:** Acest modul folosește `gpt-4.1-nano` de la Modele GitHub. Nu modificați numele modelului în cod - este configurat să funcționeze cu modelele disponibile GitHub.

## Configurare

### 1. Obțineți Tokenul GitHub

1. Accesați [Setări GitHub → Tokenuri de Acces Personal](https://github.com/settings/personal-access-tokens)
2. Faceți clic pe „Generate new token” (Generează token nou)
3. Alegeți un nume descriptiv (ex. „LangChain4j Demo”)
4. Setați expirarea (se recomandă 7 zile)
5. Sub „Permisiuni cont”, găsiți „Models” și setați pe „Doar citire”
6. Apăsați „Generate token”
7. Copiați și salvați tokenul - nu îl veți mai putea vedea

### 2. Setează Tokenul

**Opțiunea 1: Folosind VS Code (Recomandat)**

Dacă folosiți VS Code, adăugați tokenul în fișierul `.env` din rădăcina proiectului:

Dacă nu există `.env`, copiați `.env.example` în `.env` sau creați un fișier `.env` nou în rădăcina proiectului.

**Exemplu fișier `.env`:**
```bash
# În /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Apoi puteți face clic dreapta pe orice fișier demo (ex. `BasicChatDemo.java`) în Explorer și selecta **"Run Java"** sau să folosiți configurațiile de rulare din panoul Run and Debug.

**Opțiunea 2: Folosind Terminalul**

Setați tokenul ca variabilă de mediu:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Rulați Exemplele

**Folosind VS Code:** Pur și simplu faceți clic dreapta pe orice fișier demo în Explorer și selectați **"Run Java"**, sau folosiți configurațiile din Run and Debug (asigurați-vă că ați adăugat tokenul în `.env` mai întâi).

**Folosind Maven:** Alternativ, puteți rula din linia de comandă:

### 1. Chat de bază

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Modele de Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Arată prompturi zero-shot, few-shot, lanț-de-gândire și bazate pe rol.

### 3. Apelarea Funcțiilor

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI-ul apelează automat metodele Java când este necesar.

### 4. Întrebări și Răspunsuri pe Documente (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Puneți întrebări despre documentele voastre folosind Easy RAG cu încorporare și recuperare automate.

### 5. Inteligență Artificială Responsabilă

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vezi cum filtrele de siguranță AI blochează conținutul dăunător.

## Ce Arată Fiecare Exemplu

**Chat de bază** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Începeți aici pentru a vedea LangChain4j în forma sa cea mai simplă. Veți crea un `OpenAiOfficialChatModel`, veți trimite un prompt cu `.chat()` și veți primi înapoi un răspuns. Acest exemplu demonstrează baza: cum să inițializați modelele cu endpoint-uri și chei API personalizate. Odată ce înțelegeți acest model, totul se bazează pe el.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) și întrebați:
> - „Cum aș face trecerea de la Modelele GitHub la Azure OpenAI în acest cod?”
> - „Ce alți parametri pot configura în OpenAiOfficialChatModel.builder()?”
> - „Cum adaug răspunsuri de tip streaming în loc să aștept răspunsul complet?”

**Ingineria Prompturilor** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Acum că știți cum să vorbiți cu un model, să explorăm ce îi spuneți. Acest demo folosește aceeași configurare de model, dar arată cinci tipuri diferite de prompturi. Încercați prompturi zero-shot pentru instrucțiuni directe, few-shot care învață din exemple, lanț-de-gândire care dezvăluie pași de raționament și prompturi bazate pe rol care setează contextul. Veți vedea cum același model oferă rezultate foarte diferite în funcție de cum formulați cererea.

Demo-ul arată și șabloane de prompturi, o metodă puternică pentru a crea prompturi reutilizabile cu variabile.
Exemplul de mai jos arată un prompt ce folosește `PromptTemplate` LangChain4j pentru a completa variabilele. AI-ul va răspunde bazat pe destinația și activitatea furnizate.

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

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) și întrebați:
> - „Care e diferența dintre prompturile zero-shot și few-shot și când ar trebui să utilizez fiecare?”
> - „Cum influențează parametrul temperature răspunsurile modelului?”
> - „Ce tehnici există pentru a preveni atacurile de injecție a prompturilor în producție?”
> - „Cum pot crea obiecte PromptTemplate reutilizabile pentru modele frecvente?”

**Integrarea Uneltelor** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aici LangChain4j devine puternic. Veți folosi `AiServices` pentru a crea un asistent AI care poate apela metodele Java. Doar anotați metodele cu `@Tool("descriere")` și LangChain4j se ocupă de restul - AI-ul decide automat când să utilizeze fiecare unealtă în funcție de ce solicită utilizatorul. Acest exemplu demonstrează apelarea funcțiilor, o tehnică esențială pentru a construi AI care poate acționa, nu doar răspunde.

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

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) și întrebați:
> - „Cum funcționează adnotarea @Tool și ce face LangChain4j cu ea în spate?”
> - „Poate AI-ul să apeleze mai multe unelte în secvență pentru a rezolva probleme complexe?”
> - „Ce se întâmplă dacă o unealtă aruncă o excepție - cum ar trebui să gestionez erorile?”
> - „Cum aș integra o API reală în loc de acest exemplu cu calculatorul?”

**Întrebări și Răspunsuri pe Documente (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aici veți vedea RAG (generare augmentată de recuperare) folosind abordarea „Easy RAG” a LangChain4j. Documentele sunt încărcate, divizate și încorporate automat într-un magazin în memorie, apoi un recuperator de conținut oferă bucăți relevante AI-ului la momentul întrebării. AI-ul răspunde pe baza documentelor, nu pe baza cunoștințelor generale.

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

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) și întrebați:
> - „Cum previne RAG halucinațiile AI comparativ cu folosirea datelor de antrenament ale modelului?”
> - „Care este diferența între această abordare ușoară și un pipeline RAG personalizat?”
> - „Cum aș scala asta pentru mai multe documente sau baze de cunoștințe mai mari?”

**Inteligență Artificială Responsabilă** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construiți siguranța AI cu apărare în profunzime. Acest demo arată două straturi de protecție care funcționează împreună:

**Partea 1: Gardurile de Protecție LangChain4j Input** - Blochează prompturile periculoase înainte să ajungă la LLM. Creați garduri personalizate care verifică cuvinte cheie sau modele interzise. Acestea rulează în codul vostru, deci sunt rapide și gratuite.

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

**Partea 2: Filtrele de Siguranță ale Providerului** - Modelele GitHub au filtre integrate care prind ce ar putea să scape gardurilor voastre. Veți vedea blocări dure (erori HTTP 400) pentru încălcări serioase și refuzuri blânde unde AI refuză politicos.

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) și întrebați:
> - „Ce este InputGuardrail și cum îmi creez propriul gard de protecție?”
> - „Care este diferența între un blocaj dur și un refuz blând?”
> - „De ce să folosesc atât garduri, cât și filtre de provider împreună?”

## Pașii Următori

**Următorul modul:** [01-introduction - Început cu LangChain4j și gpt-5 pe Azure](../01-introduction/README.md)

---

**Navigare:** [← Înapoi la Principal](../README.md) | [Următor: Modulul 01 - Introducere →](../01-introduction/README.md)

---

## Depanare

### Prima Compilare Maven

**Problema:** `mvn clean compile` sau `mvn package` durează mult la prima rulare (10-15 minute)

**Cauză:** Maven trebuie să descarce toate dependențele proiectului (Spring Boot, biblioteci LangChain4j, SDK-uri Azure etc.) la prima compilare.

**Soluție:** Acesta este un comportament normal. Compilările următoare vor fi mult mai rapide deoarece dependențele sunt memorate local. Timpul de descărcare depinde de viteza rețelei.

### Sintaxa Comenzilor Maven în PowerShell

**Problema:** Comenzile Maven eșuează cu eroarea `Unknown lifecycle phase ".mainClass=..."`
**Cauză**: PowerShell interpretează `=` ca un operator de atribuire a variabilei, perturbând sintaxa proprietăților Maven

**Soluție**: Folosiți operatorul stop-parsing `--%` înainte de comanda Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorul `--%` spune PowerShell să transmită toate argumentele rămase literal către Maven fără interpretare.

### Afișarea emoji-urilor în Windows PowerShell

**Problemă**: Răspunsurile AI afișează caractere ilegibile (de exemplu, `????` sau `â??`) în loc de emoji-uri în PowerShell

**Cauză**: Codificarea implicită a PowerShell nu suportă emoji-urile UTF-8

**Soluție**: Rulați această comandă înainte de a executa aplicații Java:
```cmd
chcp 65001
```

Aceasta forțează codificarea UTF-8 în terminal. Alternativ, folosiți Windows Terminal care are suport mai bun pentru Unicode.

### Depanarea apelurilor API

**Problemă**: Erori de autentificare, limite de rată sau răspunsuri neașteptate de la modelul AI

**Soluție**: Exemplele includ `.logRequests(true)` și `.logResponses(true)` pentru a afișa apelurile API în consolă. Acest lucru ajută la depanarea erorilor de autentificare, a limitelor de rată sau a răspunsurilor neașteptate. Scoateți aceste setări în producție pentru a reduce zgomotul din log-uri.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinarea responsabilității**:  
Acest document a fost tradus folosind un serviciu de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă o traducere profesională realizată de un translator uman. Nu ne asumăm nicio responsabilitate pentru eventualele neînțelegeri sau interpretări greșite rezultate din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->