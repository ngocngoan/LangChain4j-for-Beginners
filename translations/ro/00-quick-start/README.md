# Modulul 00: Pornire Rapidă

## Cuprins

- [Introducere](../../../00-quick-start)
- [Ce este LangChain4j?](../../../00-quick-start)
- [Dependențe LangChain4j](../../../00-quick-start)
- [Precondiții](../../../00-quick-start)
- [Configurare](../../../00-quick-start)
  - [1. Obțineți tokenul GitHub](../../../00-quick-start)
  - [2. Setează tokenul](../../../00-quick-start)
- [Rulare Exemple](../../../00-quick-start)
  - [1. Chat de bază](../../../00-quick-start)
  - [2. Modele de prompturi](../../../00-quick-start)
  - [3. Apelarea funcțiilor](../../../00-quick-start)
  - [4. Întrebări și răspunsuri document (Easy RAG)](../../../00-quick-start)
  - [5. AI responsabilă](../../../00-quick-start)
- [Ce arată fiecare exemplu](../../../00-quick-start)
- [Pașii următori](../../../00-quick-start)
- [Rezolvarea problemelor](../../../00-quick-start)

## Introducere

Acest ghid rapid are scopul de a vă ajuta să începeți să utilizați LangChain4j cât mai repede posibil. Acoperă elementele absolut de bază pentru construirea aplicațiilor AI cu LangChain4j și Modele GitHub. În următoarele module veți trece la Azure OpenAI și GPT-5.2 și veți aprofunda fiecare concept.

## Ce este LangChain4j?

LangChain4j este o bibliotecă Java care simplifică construirea aplicațiilor alimentate de AI. În loc să lucrați cu clienți HTTP și parsarea JSON, folosiți API-uri clare Java.

„Chain” în LangChain se referă la conectarea în lanț a mai multor componente – puteți conecta un prompt la un model, apoi la un parser, sau puteți conecta mai multe apeluri AI unde ieșirea unuia devine intrarea următorului. Această pornire rapidă se concentrează pe fundamente înainte de a explora lanțuri mai complexe.

<img src="../../../translated_images/ro/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceptul de conectare în lanț LangChain4j" width="800"/>

*Conectarea componentelor în LangChain4j - blocuri de bază conectate pentru a crea fluxuri AI puternice*

Vom folosi trei componente de bază:

**ChatModel** – Interfața pentru interacțiuni cu modelul AI. Apelați `model.chat("prompt")` și primiți un răspuns sub formă de text. Folosim `OpenAiOfficialChatModel` care funcționează cu endpoint-uri compatibile OpenAI, cum sunt Modelele GitHub.

**AiServices** – Creează interfețe sigure pe tipuri pentru servicii AI. Definiți metode, le notați cu `@Tool` și LangChain4j se ocupă de orchestrare. AI-ul vă apelează automat metodele Java când este nevoie.

**MessageWindowChatMemory** – Menține istoricul conversației. Fără acesta, fiecare cerere este independentă. Cu acesta, AI-ul își amintește mesajele anterioare și păstrează contextul pe parcursul mai multor interacțiuni.

<img src="../../../translated_images/ro/architecture.eedc993a1c576839.webp" alt="Arhitectura LangChain4j" width="800"/>

*Arhitectura LangChain4j - componentele principale care lucrează împreună pentru a alimenta aplicațiile tale AI*

## Dependențe LangChain4j

Această pornire rapidă folosește trei dependențe Maven în [`pom.xml`](../../../00-quick-start/pom.xml):

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
  
Modulul `langchain4j-open-ai-official` oferă clasa `OpenAiOfficialChatModel` care se conectează la API-uri compatibile OpenAI. Modelele GitHub folosesc același format de API, deci nu este necesar un adaptor special - trebuie doar să indicați URL-ul de bază spre `https://models.github.ai/inference`.

Modulul `langchain4j-easy-rag` oferă divizarea automată a documentelor, embedding și recuperare, astfel încât să puteți construi aplicații RAG fără a configura manual fiecare pas.

## Precondiții

**Folosiți containerul de dezvoltare?** Java și Maven sunt deja instalate. Aveți nevoie doar de un Token de acces personal GitHub.

**Dezvoltare locală:**  
- Java 21+, Maven 3.9+  
- Token de Acces Personal GitHub (instrucțiuni mai jos)

> **Notă:** Acest modul folosește `gpt-4.1-nano` de la Modelele GitHub. Nu modificați numele modelului în cod - este configurat să funcționeze cu modelele disponibile pe GitHub.

## Configurare

### 1. Obțineți tokenul GitHub

1. Accesați [Setări GitHub → Tokenuri de Acces Personal](https://github.com/settings/personal-access-tokens)  
2. Click pe „Generați token nou”  
3. Dați-i un nume descriptiv (ex., „LangChain4j Demo”)  
4. Stabiliți expirarea (7 zile recomandat)  
5. La „Permisiuni cont”, găsiți „Models” și setați pe „Read-only”  
6. Click pe „Generate token”  
7. Copiați și salvați tokenul – nu îl veți mai vedea din nou  

### 2. Setează tokenul

**Opțiunea 1: Folosind VS Code (recomandat)**

Dacă folosiți VS Code, adăugați tokenul în fișierul `.env` din rădăcina proiectului:

Dacă fișierul `.env` nu există, copiați `.env.example` în `.env` sau creați un fișier `.env` nou în rădăcina proiectului.

**Exemplu fișier `.env`:**  
```bash
# În /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Apoi puteți face click dreapta pe orice fișier demo (ex., `BasicChatDemo.java`) în Explorer și selecta **„Run Java”** sau să folosiți configurațiile de lansare din panoul Run and Debug.

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
  

## Rulare Exemple

**Folosind VS Code:** Pur și simplu faceți click dreapta pe orice fișier demo în Explorer și selectați **„Run Java”**, sau folosiți configurațiile din Run and Debug (asigurați-vă că ați adăugat tokenul în fișierul `.env` înainte).

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
  

### 2. Modele de prompturi

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```
  
Arată zero-shot, few-shot, chain-of-thought și prompting bazat pe rol.

### 3. Apelarea funcțiilor

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
AI apelează automat metodele Java când este nevoie.

### 4. Întrebări și răspunsuri document (Easy RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Puneți întrebări despre documentele voastre folosind Easy RAG cu embedding și recuperare automată.

### 5. AI responsabilă

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```
  
Vezi cum filtrele de siguranță AI blochează conținutul dăunător.

## Ce arată fiecare exemplu

**Chat de bază** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Începeți aici pentru a vedea LangChain4j în cea mai simplă formă. Veți crea un `OpenAiOfficialChatModel`, veți trimite un prompt cu `.chat()` și veți primi un răspuns. Acesta demonstrează fundația: cum să inițializați modelele cu endpoint-uri personalizate și chei API. Odată ce înțelegeți acest model, totul se construiește pe el.

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
> - „Cum aș comuta de la Modele GitHub la Azure OpenAI în acest cod?”  
> - „Ce alți parametri pot configura în OpenAiOfficialChatModel.builder()?”  
> - „Cum adaug răspunsuri în streaming în loc să aștept răspunsul complet?”

**Inginerie Prompturi** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Acum că știți cum să vorbiți cu un model, să explorăm ce îi spuneți. Acest demo folosește aceeași configurare dar arată cinci modele diferite de prompting. Încercați zero-shot pentru instrucțiuni directe, few-shot care învață din exemple, chain-of-thought care dezvăluie pașii de raționament și prompting bazat pe rol pentru a seta contextul. Veți vedea cum același model dă rezultate foarte diferite în funcție de modul în care formulați cererea.

Demo arată și template-uri de prompturi, o metodă puternică pentru a crea prompturi reutilizabile cu variabile.  
Exemplul de mai jos arată un prompt folosind `PromptTemplate` din LangChain4j pentru a completa variabilele. AI va răspunde bazat pe destinația și activitatea oferite.

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
> - „Care este diferența între zero-shot și few-shot prompting și când ar trebui să le folosesc?”  
> - „Cum afectează parametrul temperature răspunsurile modelului?”  
> - „Care sunt tehnicile pentru a preveni atacurile de tip prompt injection în producție?”  
> - „Cum pot crea obiecte PromptTemplate reutilizabile pentru tipare comune?”

**Integrare Unelte** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aici LangChain4j devine puternic. Folosiți `AiServices` pentru a crea un asistent AI care poate apela metodele Java. Notați metodele cu `@Tool("descriere")` și LangChain4j se ocupă de restul – AI-ul decide automat când să folosească fiecare unealtă în funcție de cererile utilizatorului. Aceasta demonstrează apelarea funcțiilor, o tehnică cheie pentru a construi AI care poate lua acțiuni, nu doar răspunde la întrebări.

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
> - „Cum funcționează notația @Tool și ce face LangChain4j cu ea în fundal?”  
> - „Poate AI să apeleze mai multe unelte în secvență pentru a rezolva probleme complexe?”  
> - „Ce se întâmplă dacă o unealtă aruncă o excepție – cum ar trebui să gestionez erorile?”  
> - „Cum aș integra o API reală în locul acestui exemplu de calculator?”

**Întrebări și răspunsuri document (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aici veți vedea RAG (retrieval-augmented generation) folosind abordarea „Easy RAG” a LangChain4j. Documentele sunt încărcate, împărțite automat și integrate într-un depozit în memorie, apoi un recuperator de conținut furnizează bucățile relevante AI-ului la interogare. AI-ul răspunde bazat pe documentele voastre, nu pe cunoștințele generale.

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
> - „Cum previne RAG halucinațiile AI în comparație cu folosirea datelor de antrenament ale modelului?”  
> - „Care este diferența între această metodă ușoară și o pipeline RAG personalizată?”  
> - „Cum aș scala asta pentru a gestiona mai multe documente sau baze de cunoștințe mai mari?”

**AI responsabilă** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construiți siguranță AI cu apărare în profunzime. Acest demo arată două nivele de protecție care lucrează împreună:

**Partea 1: LangChain4j Input Guardrails** – Blochează prompturile periculoase înainte să ajungă la LLM. Creați bariere personalizate care verifică cuvinte cheie sau tipare interzise. Acestea rulează în codul vostru, deci sunt rapide și fără cost.

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
  
**Partea 2: Filtre de siguranță ale providerului** – Modelele GitHub au filtre integrate care prind ceea ce barierele voastre pot rata. Veți vedea blocări dure (erori HTTP 400) pentru încălcări grave și refuzuri blânde unde AI-ul refuză politicos.

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) și întrebați:  
> - „Ce este InputGuardrail și cum creez unul propriu?”  
> - „Care este diferența între o blocare dură și un refuz blând?”  
> - „De ce să folosesc împreună bariere și filtre ale providerului?”

## Pașii următori

**Următorul modul:** [01-introduction - Început cu LangChain4j](../01-introduction/README.md)

---

**Navigare:** [← Înapoi la Principal](../README.md) | [Următorul: Modul 01 - Introducere →](../01-introduction/README.md)

---

## Rezolvarea problemelor

### Prima compilare Maven

**Problemă:** Primul `mvn clean compile` sau `mvn package` durează mult (10-15 minute)  

**Cauză:** Maven trebuie să descarce toate dependențele proiectului (Spring Boot, biblioteci LangChain4j, SDK-uri Azure etc.) la prima compilare.

**Soluție:** Comportament normal. Compilările următoare vor fi mult mai rapide deoarece dependențele sunt memorate local. Timpul de descărcare depinde de viteza rețelei.

### Sintaxa comenzii Maven în PowerShell

**Problemă:** Comenzile Maven eșuează cu eroarea `Unknown lifecycle phase ".mainClass=..."`
**Cauză**: PowerShell interpretează `=` ca operator de atribuire a variabilei, întrerupând sintaxa proprietăților Maven

**Soluție**: Folosiți operatorul de oprire a analizării `--%` înaintea comenzii Maven:

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

**Problemă**: Răspunsurile AI afișează caractere ciudate (de exemplu, `????` sau `â??`) în loc de emoji-uri în PowerShell

**Cauză**: Codificarea implicită a PowerShell nu suportă emoji-urile UTF-8

**Soluție**: Rulați această comandă înainte de a executa aplicațiile Java:
```cmd
chcp 65001
```

Aceasta forțează codificarea UTF-8 în terminal. Alternativ, folosiți Windows Terminal care are suport mai bun pentru Unicode.

### Depanarea apelurilor API

**Problemă**: Erori de autentificare, limite de rată sau răspunsuri neașteptate de la modelul AI

**Soluție**: Exemplele includ `.logRequests(true)` și `.logResponses(true)` pentru a afișa apelurile API în consolă. Aceasta ajută la depanarea erorilor de autentificare, limitelor de rată sau răspunsurilor neașteptate. Eliminați aceste opțiuni în producție pentru a reduce zgomotul din loguri.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventuale neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->