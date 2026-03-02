# Module 00: Szybki start

## Spis treści

- [Wprowadzenie](../../../00-quick-start)
- [Co to jest LangChain4j?](../../../00-quick-start)
- [Zależności LangChain4j](../../../00-quick-start)
- [Wymagania wstępne](../../../00-quick-start)
- [Konfiguracja](../../../00-quick-start)
  - [1. Pobierz token GitHub](../../../00-quick-start)
  - [2. Ustaw swój token](../../../00-quick-start)
- [Uruchom przykłady](../../../00-quick-start)
  - [1. Podstawowy czat](../../../00-quick-start)
  - [2. Wzorce promptów](../../../00-quick-start)
  - [3. Wywoływanie funkcji](../../../00-quick-start)
  - [4. Pytania i odpowiedzi na dokumenty (Easy RAG)](../../../00-quick-start)
  - [5. Odpowiedzialna AI](../../../00-quick-start)
- [Co pokazuje każdy przykład](../../../00-quick-start)
- [Kolejne kroki](../../../00-quick-start)
- [Rozwiązywanie problemów](../../../00-quick-start)

## Wprowadzenie

Ten szybki start ma na celu jak najszybsze uruchomienie LangChain4j. Obejmuje on absolutne podstawy budowania aplikacji AI z użyciem LangChain4j i modeli GitHub. W kolejnych modułach przejdziesz do Azure OpenAI i GPT-5.2 oraz zagłębisz się w każdy koncept.

## Co to jest LangChain4j?

LangChain4j to biblioteka Java, która upraszcza tworzenie aplikacji zasilanych AI. Zamiast zajmować się klientami HTTP i parsowaniem JSON, pracujesz z czystymi API w Javie.

"Chain" w LangChain oznacza łączenie razem wielu komponentów – możesz łączyć prompt z modelem i z parserem lub łączyć wiele wywołań AI, gdzie wyjście jednego jest wejściem kolejnego. Ten szybki start skupia się na podstawach przed eksploracją bardziej złożonych łańcuchów.

<img src="../../../translated_images/pl/langchain-concept.ad1fe6cf063515e1.webp" alt="Koncepcja łańcuchowania LangChain4j" width="800"/>

*Łączenie komponentów w LangChain4j – połączenie bloków budujących potężne przepływy pracy AI*

Będziemy używać trzech podstawowych komponentów:

**ChatModel** – interfejs do interakcji z modelem AI. Wywołaj `model.chat("prompt")` i otrzymaj odpowiedź w postaci tekstu. Używamy `OpenAiOfficialChatModel`, który działa z punktami końcowymi kompatybilnymi z OpenAI, takimi jak modele GitHub.

**AiServices** – tworzy typowane interfejsy usług AI. Definiujesz metody, oznaczasz je za pomocą `@Tool`, a LangChain4j zajmuje się ich orkiestracją. AI automatycznie wywołuje Twoje metody Java kiedy jest to potrzebne.

**MessageWindowChatMemory** – utrzymuje historię rozmowy. Bez niego każde żądanie jest niezależne. Z nim AI pamięta poprzednie wiadomości i utrzymuje kontekst przez wiele tur.

<img src="../../../translated_images/pl/architecture.eedc993a1c576839.webp" alt="Architektura LangChain4j" width="800"/>

*Architektura LangChain4j – rdzeniowe komponenty współpracujące, aby zasilać Twoje aplikacje AI*

## Zależności LangChain4j

Ten szybki start używa trzech zależności Maven w pliku [`pom.xml`](../../../00-quick-start/pom.xml):

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

Moduł `langchain4j-open-ai-official` dostarcza klasę `OpenAiOfficialChatModel`, która łączy się z API kompatybilnymi z OpenAI. Modele GitHub używają tego samego formatu API, więc nie jest potrzebny specjalny adapter – wystarczy wskazać URL bazowy na `https://models.github.ai/inference`.

Moduł `langchain4j-easy-rag` zapewnia automatyczne dzielenie dokumentów, osadzanie i wyszukiwanie, dzięki czemu możesz budować aplikacje RAG bez manualnej konfiguracji każdego kroku.

## Wymagania wstępne

**Używasz kontenera deweloperskiego?** Java i Maven są już zainstalowane. Potrzebujesz tylko tokena dostępu osobistego GitHub.

**Lokalny rozwój:**
- Java 21+, Maven 3.9+
- Token dostępu osobistego GitHub (instrukcje poniżej)

> **Uwaga:** Ten moduł używa `gpt-4.1-nano` z modeli GitHub. Nie zmieniaj nazwy modelu w kodzie – jest skonfigurowany do współpracy z dostępnymi modelami GitHub.

## Konfiguracja

### 1. Pobierz token GitHub

1. Przejdź do [Ustawienia GitHub → Tokeny dostępu osobistego](https://github.com/settings/personal-access-tokens)
2. Kliknij „Wygeneruj nowy token”
3. Ustaw opisową nazwę (np. „LangChain4j Demo”)
4. Ustaw wygaśnięcie (zalecane 7 dni)
5. W sekcji „Uprawnienia konta” znajdź „Models” i ustaw na „Tylko do odczytu”
6. Kliknij „Wygeneruj token”
7. Skopiuj i zapisz swój token – nie będzie go widać ponownie

### 2. Ustaw swój token

**Opcja 1: Użycie VS Code (zalecane)**

Jeśli korzystasz z VS Code, dodaj token do pliku `.env` w katalogu głównym projektu:

Jeśli plik `.env` nie istnieje, skopiuj `.env.example` do `.env` lub utwórz nowy plik `.env` w katalogu głównym.

**Przykładowy plik `.env`:**
```bash
# W /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Następnie wystarczy kliknąć prawym przyciskiem myszy dowolny plik demo (np. `BasicChatDemo.java`) w Eksploratorze i wybrać **„Uruchom Java”** lub użyć konfiguracji uruchamiania z panelu Run and Debug.

**Opcja 2: Użycie terminala**

Ustaw token jako zmienną środowiskową:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Uruchom przykłady

**Używając VS Code:** Po prostu kliknij prawym przyciskiem myszy dowolny plik demo w Eksploratorze i wybierz **„Uruchom Java”**, albo skorzystaj z konfiguracji uruchamiania z panelu Run and Debug (upewnij się, że wcześniej dodałeś token do pliku `.env`).

**Używając Maven:** Alternatywnie możesz uruchomić z linii poleceń:

### 1. Podstawowy czat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Wzorce promptów

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Pokazuje zero-shot, few-shot, chain-of-thought oraz role-based prompting.

### 3. Wywoływanie funkcji

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automatycznie wywołuje Twoje metody Java kiedy jest to potrzebne.

### 4. Pytania i odpowiedzi na dokumenty (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Zadawaj pytania dotyczące dokumentów używając Easy RAG z automatycznym embeddingiem i wyszukiwaniem.

### 5. Odpowiedzialna AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Zobacz, jak filtry bezpieczeństwa AI blokują szkodliwe treści.

## Co pokazuje każdy przykład

**Podstawowy czat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Zacznij tutaj, aby zobaczyć LangChain4j w najprostszej formie. Stworzysz `OpenAiOfficialChatModel`, wyślesz prompt z `.chat()` i otrzymasz odpowiedź. Pokazuje to podstawy: jak inicjalizować modele z niestandardowymi punktami końcowymi i kluczami API. Kiedy zrozumiesz ten wzorzec, wszystko inne na nim się opiera.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) i zapytaj:
> - "Jak przełączyć się z modeli GitHub na Azure OpenAI w tym kodzie?"
> - "Jakie inne parametry mogę konfigurować w OpenAiOfficialChatModel.builder()?"
> - "Jak dodać strumieniowanie odpowiedzi zamiast czekać na pełną odpowiedź?"

**Inżynieria promptów** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, gdy wiesz jak rozmawiać z modelem, zobacz co do niego mówisz. Ten demo używa tego samego ustawienia modelu, ale pokazuje pięć różnych wzorców promptów. Wypróbuj zero-shot dla bezpośrednich instrukcji, few-shot uczące się na przykładach, chain-of-thought pokazujące kroki rozumowania oraz role-based, które ustalają kontekst. Zobaczysz, jak ten sam model daje diametralnie różne rezultaty w zależności od sposobu sformułowania zapytania.

Demo także demonstruje szablony promptów, które są potężnym sposobem tworzenia wielokrotnego użytku promptów ze zmiennymi.
Poniższy przykład pokazuje prompt używający LangChain4j `PromptTemplate` do wypełniania zmiennych. AI odpowie bazując na podanym celu i aktywności.

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

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) i zapytaj:
> - "Jaka jest różnica między zero-shot a few-shot prompting i kiedy używać którego?"
> - "Jak parametr temperature wpływa na odpowiedzi modelu?"
> - "Jakie są techniki zapobiegania atakom typu prompt injection w produkcji?"
> - "Jak tworzyć wielokrotnego użytku obiekty PromptTemplate dla popularnych wzorców?"

**Integracja narzędzi** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j pokazuje swoją moc. Użyjesz `AiServices`, aby stworzyć asystenta AI, który może wywoływać Twoje metody Java. Wystarczy oznaczyć metody `@Tool("opis")`, a LangChain4j zajmie się resztą – AI automatycznie decyduje, kiedy użyć danego narzędzia na podstawie zapytań użytkownika. To demonstruje wywoływanie funkcji, kluczową technikę budowania AI, które może podejmować działania, nie tylko odpowiadać na pytania.

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

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i zapytaj:
> - "Jak działa adnotacja @Tool i co LangChain4j robi z nią w tle?"
> - "Czy AI może wywoływać wiele narzędzi po kolei, aby rozwiązywać złożone problemy?"
> - "Co się stanie, jeśli narzędzie wyrzuci wyjątek – jak obsługiwać błędy?"
> - "Jak zintegrować prawdziwe API zamiast tego przykładu kalkulatora?"

**Pytania i odpowiedzi na dokumenty (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tu zobaczysz RAG (retrieval-augmented generation) wykorzystujące podejście LangChain4j „Easy RAG”. Dokumenty są ładowane, automatycznie dzielone i osadzane w pamięci, następnie wyszukiwarka dostarcza AI odpowiednie fragmenty podczas zapytania. AI odpowiada na podstawie Twoich dokumentów, nie swojej ogólnej wiedzy.

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

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) i zapytaj:
> - "Jak RAG zapobiega halucynacjom AI w porównaniu do korzystania z danych treningowych modelu?"
> - "Jaka jest różnica między tym łatwym podejściem a niestandardową pipeline RAG?"
> - "Jak skalować to, aby obsługiwać wiele dokumentów lub większe bazy wiedzy?"

**Odpowiedzialna AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Buduj bezpieczeństwo AI dzięki obronie wielowarstwowej. W tym demo zobaczysz dwie warstwy ochrony działające razem:

**Część 1: LangChain4j Input Guardrails** – blokują niebezpieczne prompt’y zanim dotrą do LLM. Twórz własne strażniki sprawdzające zakazane słowa kluczowe lub wzorce. Działają one w Twoim kodzie, więc są szybkie i darmowe.

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

**Część 2: Filtry bezpieczeństwa dostawcy** – Modele GitHub mają wbudowane filtry, które łapią to, co może przeoczyć strażnik. Zobaczysz twarde blokady (błędy HTTP 400) dla poważnych naruszeń oraz miękkie odmowy, gdy AI grzecznie odmawia odpowiedzi.

> **🤖 Spróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i zapytaj:
> - "Czym jest InputGuardrail i jak stworzyć własny?"
> - "Jaka jest różnica między twardą blokadą a miękką odmową?"
> - "Dlaczego używać zarówno strażników, jak i filtrów dostawcy razem?"

## Kolejne kroki

**Następny moduł:** [01-introduction - Rozpoczęcie pracy z LangChain4j](../01-introduction/README.md)

---

**Nawigacja:** [← Wróć do głównego](../README.md) | [Dalej: Moduł 01 - Wprowadzenie →](../01-introduction/README.md)

---

## Rozwiązywanie problemów

### Pierwsze kompilacje Maven

**Problem:** Pierwsze `mvn clean compile` lub `mvn package` trwa długo (10-15 minut)

**Przyczyna:** Maven musi pobrać wszystkie zależności projektu (Spring Boot, biblioteki LangChain4j, SDK Azure itp.) przy pierwszej kompilacji.

**Rozwiązanie:** To normalne zachowanie. Kolejne kompilacje będą znacznie szybsze, ponieważ zależności są buforowane lokalnie. Czas pobierania zależy od szybkości Twojego łącza.

### Składnia poleceń Maven w PowerShell

**Problem:** Polecenia Maven kończą się błędem `Unknown lifecycle phase ".mainClass=..."`
**Przyczyna**: PowerShell interpretuje `=` jako operator przypisania zmiennej, co psuje składnię właściwości Mavena

**Rozwiązanie**: Użyj operatora zatrzymania parsowania `--%` przed poleceniem Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` nakazuje PowerShell przekazać wszystkie pozostałe argumenty dosłownie do Mavena bez interpretacji.

### Wyświetlanie emoji w Windows PowerShell

**Problem**: Odpowiedzi AI pokazują znaki zastępcze (np. `????` lub `â??`) zamiast emoji w PowerShell

**Przyczyna**: Domyślne kodowanie PowerShell nie obsługuje emoji w UTF-8

**Rozwiązanie**: Uruchom to polecenie przed uruchomieniem aplikacji Java:
```cmd
chcp 65001
```

To wymusza kodowanie UTF-8 w terminalu. Alternatywnie, użyj Windows Terminal, który lepiej obsługuje Unicode.

### Debugowanie wywołań API

**Problem**: Błędy uwierzytelniania, limity szybkości lub nieoczekiwane odpowiedzi od modelu AI

**Rozwiązanie**: Przykłady zawierają `.logRequests(true)` i `.logResponses(true)` aby wyświetlać wywołania API w konsoli. Pomaga to w rozwiązywaniu błędów uwierzytelniania, limitów szybkości lub nieoczekiwanych odpowiedzi. Usuń te flagi na produkcji, aby zmniejszyć hałas w logach.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony przy użyciu usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dokładamy wszelkich starań, aby zapewnić poprawność tłumaczenia, prosimy mieć na uwadze, że automatyczne przekłady mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym należy uznawać za wiarygodne źródło. W przypadku informacji istotnych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonywanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->