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
  - [2. Modele de prompt](../../../00-quick-start)
  - [3. Apelarea funcțiilor](../../../00-quick-start)
  - [4. Întrebări și răspunsuri pe documente (RAG)](../../../00-quick-start)
  - [5. Inteligență Artificială Responsabilă](../../../00-quick-start)
- [Ce arată fiecare exemplu](../../../00-quick-start)
- [Pașii următori](../../../00-quick-start)
- [Depanare](../../../00-quick-start)

## Introducere

Acest ghid rapid este conceput să vă pună în funcțiune cu LangChain4j cât mai repede posibil. Acoperă elementele de bază absolute ale construirii aplicațiilor AI cu LangChain4j și Modelele GitHub. În modulele următoare veți folosi Azure OpenAI împreună cu LangChain4j pentru a construi aplicații mai avansate.

## Ce este LangChain4j?

LangChain4j este o bibliotecă Java care simplifică construirea aplicațiilor alimentate de AI. În loc să lucrați cu clienți HTTP și parsare JSON, folosiți API-uri Java curate.

"Chain" în LangChain se referă la concatenarea mai multor componente - puteți lega un prompt de un model, apoi de un parser, sau puteți lega mai multe apeluri AI unde ieșirea unuia devine intrarea următorului. Această introducere rapidă se concentrează pe elementele fundamentale înainte de a explora lanțuri mai complexe.

<img src="../../../translated_images/ro/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceptul de concatenare LangChain4j" width="800"/>

*Concatenarea componentelor în LangChain4j - blocurile constructorului se conectează pentru a crea fluxuri AI puternice*

Vom folosi trei componente de bază:

**ChatLanguageModel** - Interfața pentru interacțiunea cu modelul AI. Apelați `model.chat("prompt")` și primiți un șir de răspuns. Folosim `OpenAiOfficialChatModel` care funcționează cu endpoint-uri compatibile OpenAI, precum Modelele GitHub.

**AiServices** - Creează interfețe de servicii AI tip-sigure. Definiți metode, le anotați cu `@Tool`, iar LangChain4j se ocupă de orchestrare. AI apelează automat metodele Java atunci când este necesar.

**MessageWindowChatMemory** - Menține istoricul conversației. Fără acesta, fiecare cerere este independentă. Cu el, AI își amintește mesajele anterioare și păstrează contextul pe mai multe runde.

<img src="../../../translated_images/ro/architecture.eedc993a1c576839.webp" alt="Arhitectura LangChain4j" width="800"/>

*Arhitectura LangChain4j - componentele de bază care lucrează împreună pentru a alimenta aplicațiile tale AI*

## Dependențe LangChain4j

Această introducere rapidă folosește două dependențe Maven în [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulul `langchain4j-open-ai-official` oferă clasa `OpenAiOfficialChatModel` care se conectează la API-uri compatibile cu OpenAI. Modelele GitHub folosesc același format API, deci nu este nevoie de un adaptor special - doar setați URL-ul de bază la `https://models.github.ai/inference`.

## Prerechizite

**Folosiți Containerul Dev?** Java și Maven sunt deja instalate. Aveți nevoie doar de un Token de Acces Personal GitHub.

**Dezvoltare Locală:**
- Java 21+, Maven 3.9+
- Token de Acces Personal GitHub (instrucțiuni mai jos)

> **Notă:** Acest modul folosește `gpt-4.1-nano` de la Modelele GitHub. Nu modificați numele modelului în cod - este configurat să funcționeze cu modelele disponibile de la GitHub.

## Configurare

### 1. Obțineți Tokenul GitHub

1. Accesați [Setări GitHub → Tokenuri de Acces Personal](https://github.com/settings/personal-access-tokens)
2. Click pe "Generate new token"
3. Alegeți un nume descriptiv (ex: "LangChain4j Demo")
4. Stabiliți expirarea (7 zile recomandat)
5. La "Permisiuni cont", găsiți "Models" și setați pe "Read-only"
6. Click pe "Generate token"
7. Copiați și salvați tokenul - nu îl veți mai putea vedea din nou

### 2. Setează Tokenul

**Opțiunea 1: Folosind VS Code (Recomandat)**

Dacă folosiți VS Code, adăugați tokenul în fișierul `.env` din rădăcina proiectului:

Dacă fișierul `.env` nu există, copiați `.env.example` în `.env` sau creați un fișier `.env` nou în rădăcina proiectului.

**Exemplu fișier `.env`:**
```bash
# În /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Apoi puteți face click dreapta pe orice fișier demo (ex: `BasicChatDemo.java`) în Explorer și selectați **"Run Java"** sau folosiți configurațiile din panoul Run and Debug.

**Opțiunea 2: Folosind Terminal**

Setați tokenul ca o variabilă de mediu:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Rulați Exemplele

**Folosind VS Code:** Dați click dreapta pe oricare fișier demo în Explorer și selectați **"Run Java"**, sau folosiți configurațiile din panoul Run and Debug (asigurați-vă că ați adăugat tokenul în `.env` mai întâi).

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

### 2. Modele de prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Arată prompturi zero-shot, few-shot, chain-of-thought și pe rol.

### 3. Apelarea funcțiilor

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI apelează automat metodele Java când este necesar.

### 4. Întrebări și răspunsuri pe documente (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pune întrebări despre conținutul din `document.txt`.

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

## Ce arată fiecare exemplu

**Chat de bază** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Începeți aici pentru a vedea LangChain4j în forma sa cea mai simplă. Veți crea un `OpenAiOfficialChatModel`, veți trimite un prompt cu `.chat()`, și veți primi un răspuns. Acest exemplu demonstrează baza: cum să inițializați modele cu endpoint-uri și chei API personalizate. Odată înțeles acest model, tot restul se construiește pe el.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) și întrebați:
> - "Cum aș schimba de la Modelele GitHub la Azure OpenAI în acest cod?"
> - "Ce alți parametri pot configura în OpenAiOfficialChatModel.builder()?"
> - "Cum adaug streaming pentru răspunsuri în loc să aștept răspunsul complet?"

**Ingineria Prompturilor** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Acum că știți cum să vorbiți cu un model, haideți să explorăm ce îi spuneți. Acest demo folosește aceeași configurație de model, dar arată cinci modele diferite de prompt. Încercați prompturi zero-shot pentru instrucțiuni directe, few-shot care învață din exemple, chain-of-thought care dezvăluie pași de raționament și prompturi pe bază de rol care setează contextul. Veți vedea cum același model oferă rezultate dramatic diferite în funcție de cum formulați cererea.

Demo-ul arată și șabloane de prompt, o metodă puternică pentru a crea prompturi reutilizabile cu variabile.
Exemplul de mai jos arată un prompt folosind `PromptTemplate` din LangChain4j pentru a completa variabile. AI va răspunde pe baza destinației și activității oferite.

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
> - "Care este diferența dintre prompturile zero-shot și few-shot și când ar trebui să le folosesc?"
> - "Cum afectează parametrul temperature răspunsurile modelului?"
> - "Ce tehnici există pentru a preveni atacurile de tip prompt injection în producție?"
> - "Cum pot crea obiecte PromptTemplate reutilizabile pentru modele comune?"

**Integrarea Uneltelor** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aici LangChain4j devine puternic. Veți folosi `AiServices` pentru a crea un asistent AI care poate apela metodele voastre Java. Pur și simplu anotați metodele cu `@Tool("descriere")` și LangChain4j se ocupă de restul - AI decide automat când să folosească fiecare unealtă bazat pe ceea ce cere utilizatorul. Aceasta demonstrează apelarea funcțiilor, o tehnică cheie pentru a construi AI care poate acționa, nu doar răspunde la întrebări.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) și întrebați:
> - "Cum funcționează adnotarea @Tool și ce face LangChain4j cu ea în fundal?"
> - "Poate AI să apeleze mai multe unelte în secvență pentru a rezolva probleme complexe?"
> - "Ce se întâmplă dacă o unealtă aruncă o excepție - cum ar trebui să gestionez erorile?"
> - "Cum aș integra o API reală în locul acestui exemplu cu calculator?"

**Întrebări și răspunsuri pe documente (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aici veți vedea baza RAG (retrieval-augmented generation). În loc să vă bazați pe datele de antrenament ale modelului, încărcați conținutul din [`document.txt`](../../../00-quick-start/document.txt) și îl includeți în prompt. AI răspunde pe baza documentului vostru, nu a cunoștințelor generale. Acesta este primul pas spre sisteme care pot lucra cu datele proprii.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Notă:** Această abordare simplă încarcă întregul document în prompt. Pentru fișiere mari (>10KB), veți depăși limitele de context. Modulul 03 acoperă segmentarea și căutarea vectorială pentru sisteme RAG în producție.

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) și întrebați:
> - "Cum previne RAG halucinațiile AI comparativ cu folosirea datelor din antrenamentul modelului?"
> - "Care este diferența dintre această abordare simplă și utilizarea embedding-urilor vectoriale pentru căutare?"
> - "Cum aș scala asta pentru a gestiona mai multe documente sau baze de cunoștințe mari?"
> - "Care sunt cele mai bune practici pentru structurarea promptului ca să se asigure că AI folosește doar contextul oferit?"

**Inteligență Artificială Responsabilă** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construiți siguranță AI cu apărare în profunzime. Acest demo arată două straturi de protecție care lucrează împreună:

**Partea 1: Gardurile de Protecție Input LangChain4j** - Blochează prompturile periculoase înainte ca acestea să ajungă la LLM. Creați garduri personalizate care verifică cuvinte cheie sau modele interzise. Acestea rulează în codul vostru, deci sunt rapide și gratuite.

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

**Partea 2: Filtrele de Siguranță ale Furnizorului** - Modelele GitHub au filtre încorporate care capturează ce găurile voastre ar putea rata. Veți vedea blocări dure (erori HTTP 400) pentru încălcări grave și refuzuri blânde unde AI refuză politicos.

> **🤖 Încercați cu [GitHub Copilot](https://github.com/features/copilot) Chat:** Deschideți [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) și întrebați:
> - "Ce este InputGuardrail și cum îmi creez propriul?"
> - "Care este diferența dintre blocarea dură și refuzul blând?"
> - "De ce să folosim atât garduri, cât și filtrele furnizorului împreună?"

## Pașii următori

**Următorul Modul:** [01-introduction - Primii pași cu LangChain4j și gpt-5 pe Azure](../01-introduction/README.md)

---

**Navigare:** [← Înapoi la Principal](../README.md) | [Următor: Modulul 01 - Introducere →](../01-introduction/README.md)

---

## Depanare

### Prima Construire Maven

**Problema:** `mvn clean compile` sau `mvn package` durează mult prima dată (10-15 minute)

**Cauza:** Maven trebuie să descarce toate dependențele proiectului (Spring Boot, bibliotecile LangChain4j, SDK-urile Azure etc.) la prima construire.

**Soluția:** Este un comportament normal. Construirile ulterioare vor fi mult mai rapide deoarece dependențele sunt memorate în cache local. Timpul de descărcare depinde de viteza conexiunii la internet.
### Sintaxa comenzii Maven în PowerShell

**Problemă**: comenzile Maven eșuează cu eroarea `Unknown lifecycle phase ".mainClass=..."`

**Cauză**: PowerShell interpretează `=` ca operator de atribuire a variabilei, stricând sintaxa proprietății Maven

**Soluție**: Utilizați operatorul de oprire a interpretării `--%` înaintea comenzii Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorul `--%` spune PowerShell să transmită toate argumentele rămase literal către Maven fără interpretare.

### Afișarea emoji în Windows PowerShell

**Problemă**: Răspunsurile AI afișează caractere stranii (de ex., `????` sau `â??`) în loc de emoji în PowerShell

**Cauză**: Codarea implicită a PowerShell nu suportă emoji UTF-8

**Soluție**: Rulați această comandă înainte de a executa aplicațiile Java:
```cmd
chcp 65001
```

Aceasta forțează codarea UTF-8 în terminal. Alternativ, utilizați Windows Terminal, care are suport Unicode mai bun.

### Depanarea apelurilor API

**Problemă**: erori de autentificare, limite de rată sau răspunsuri neașteptate de la modelul AI

**Soluție**: Exemplele includ `.logRequests(true)` și `.logResponses(true)` pentru afișarea apelurilor API în consolă. Acest lucru ajută la depanarea erorilor de autentificare, limitelor de rată sau răspunsurilor neașteptate. Eliminați aceste opțiuni în producție pentru a reduce zgomotul din jurnal.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa oficială și autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->