# Moduł 00: Szybki Start

## Spis treści

- [Wprowadzenie](../../../00-quick-start)
- [Czym jest LangChain4j?](../../../00-quick-start)
- [Zależności LangChain4j](../../../00-quick-start)
- [Wymagania wstępne](../../../00-quick-start)
- [Konfiguracja](../../../00-quick-start)
  - [1. Uzyskaj swój token GitHub](../../../00-quick-start)
  - [2. Ustaw swój token](../../../00-quick-start)
- [Uruchom przykłady](../../../00-quick-start)
  - [1. Podstawowy czat](../../../00-quick-start)
  - [2. Wzorce promptów](../../../00-quick-start)
  - [3. Wywołanie funkcji](../../../00-quick-start)
  - [4. Pytania i odpowiedzi na dokumentach (Easy RAG)](../../../00-quick-start)
  - [5. Odpowiedzialna AI](../../../00-quick-start)
- [Co pokazuje każdy przykład](../../../00-quick-start)
- [Następne kroki](../../../00-quick-start)
- [Rozwiązywanie problemów](../../../00-quick-start)

## Wprowadzenie

Ten szybki start ma na celu jak najszybsze uruchomienie LangChain4j. Omawia absolutne podstawy tworzenia aplikacji AI z LangChain4j i modelami GitHub. W kolejnych modułach użyjesz Azure OpenAI z LangChain4j, aby budować bardziej zaawansowane aplikacje.

## Czym jest LangChain4j?

LangChain4j to biblioteka Java upraszczająca tworzenie aplikacji opartych na AI. Zamiast zajmować się klientami HTTP i analizą JSON pracujesz z czystymi API w Javie.

„Chain” (łańcuch) w LangChain odnosi się do łączenia ze sobą wielu komponentów – możesz połączyć prompt z modelem, parserem lub łączyć wiele wywołań AI, gdzie jedno wyjście zasila kolejne wejście. Ten szybki start koncentruje się na podstawach przed eksploracją bardziej złożonych łańcuchów.

<img src="../../../translated_images/pl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Łączenie komponentów w LangChain4j – bloki konstrukcyjne łączą się, tworząc potężne przepływy AI*

Użyjemy trzech podstawowych komponentów:

**ChatModel** – interfejs do interakcji z modelem AI. Wywołaj `model.chat("prompt")` i otrzymaj odpowiedź w postaci tekstu. Używamy `OpenAiOfficialChatModel`, który działa z punktami końcowymi kompatybilnymi z OpenAI, takimi jak modele GitHub.

**AiServices** – tworzy typowo bezpieczne interfejsy usług AI. Definiujesz metody, oznaczasz je adnotacją `@Tool`, a LangChain4j zajmuje się orkiestracją. AI automatycznie wywołuje twoje metody Java, gdy jest to potrzebne.

**MessageWindowChatMemory** – utrzymuje historię rozmowy. Bez tego każde żądanie jest niezależne. Z nim AI pamięta poprzednie wiadomości i utrzymuje kontekst przez wiele tur.

<img src="../../../translated_images/pl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektura LangChain4j – podstawowe komponenty współpracujące razem, aby napędzać twoje aplikacje AI*

## Zależności LangChain4j

Ten szybki start używa trzech zależności Maven w [`pom.xml`](../../../00-quick-start/pom.xml):

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

Moduł `langchain4j-open-ai-official` dostarcza klasę `OpenAiOfficialChatModel`, która łączy się z API kompatybilnym z OpenAI. Modele GitHub używają tego samego formatu API, więc nie jest potrzebny specjalny adapter – wystarczy skierować bazowy URL na `https://models.github.ai/inference`.

Moduł `langchain4j-easy-rag` zapewnia automatyczne dzielenie dokumentów, osadzanie i wyszukiwanie, dzięki czemu możesz tworzyć aplikacje RAG bez ręcznej konfiguracji każdego kroku.

## Wymagania wstępne

**Używasz Dev Containera?** Java i Maven są już zainstalowane. Potrzebujesz tylko token osobisty GitHub.

**Lokalny rozwój:**
- Java 21+, Maven 3.9+
- Token osobisty GitHub (instrukcje poniżej)

> **Uwaga:** Ten moduł używa `gpt-4.1-nano` z modeli GitHub. Nie modyfikuj nazwy modelu w kodzie – jest skonfigurowany do pracy z dostępnymi modelami GitHub.

## Konfiguracja

### 1. Uzyskaj swój token GitHub

1. Przejdź do [GitHub Ustawienia → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Kliknij „Generate new token”
3. Ustaw opisową nazwę (np. „LangChain4j Demo”)
4. Ustaw wygaśnięcie (zalecane 7 dni)
5. W sekcji „Account permissions” znajdź „Models” i ustaw na „Read-only”
6. Kliknij „Generate token”
7. Skopiuj i zapisz token – nie zobaczysz go ponownie

### 2. Ustaw swój token

**Opcja 1: Użycie VS Code (zalecane)**

Jeśli używasz VS Code, dodaj swój token do pliku `.env` w katalogu głównym projektu:

Jeśli plik `.env` nie istnieje, skopiuj `.env.example` do `.env` lub stwórz nowy plik `.env` w katalogu głównym projektu.

**Przykładowy plik `.env`:**
```bash
# W /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Następnie możesz po prostu kliknąć prawym przyciskiem myszy dowolny plik demo (np. `BasicChatDemo.java`) w Eksploratorze i wybrać **„Run Java”** lub użyć konfiguracji uruchamiania z panelu Run and Debug.

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

**Używając VS Code:** Kliknij prawym przyciskiem myszy dowolny plik demo w Eksploratorze i wybierz **„Run Java”**, lub użyj konfiguracji uruchamiania z panelu Run and Debug (upewnij się, że najpierw dodałeś token do pliku `.env`).

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

Pokazuje promptowanie zero-shot, few-shot, chain-of-thought i oparte na roli.

### 3. Wywołanie funkcji

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automatycznie wywołuje twoje metody Java, gdy jest to potrzebne.

### 4. Pytania i odpowiedzi na dokumentach (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Zadaj pytania dotyczące swoich dokumentów używając Easy RAG z automatycznym osadzaniem i wyszukiwaniem.

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

Zacznij tutaj, aby zobaczyć LangChain4j w najprostszej formie. Stworzysz `OpenAiOfficialChatModel`, wyślesz prompt z `.chat()` i otrzymasz odpowiedź. To pokazuje fundament: jak zainicjalizować modele z własnymi punktami końcowymi i kluczami API. Gdy to zrozumiesz, reszta opiera się na tym wzorcu.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) i zapytaj:
> - „Jak przełączyć się z modeli GitHub na Azure OpenAI w tym kodzie?”
> - „Jakie inne parametry mogę konfigurować w OpenAiOfficialChatModel.builder()?”
> - „Jak dodać strumieniowe odpowiedzi zamiast czekać na pełną odpowiedź?”

**Inżynieria promptów** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, gdy wiesz jak rozmawiać z modelem, poznaj co do niego mówisz. Ten demo używa tej samej konfiguracji modelu, ale pokazuje pięć różnych wzorców promptów. Wypróbuj zero-shot dla bezpośrednich instrukcji, few-shot uczące na przykładach, chain-of-thought pokazujące kroki rozumowania oraz promptowanie oparte na roli, które ustawiają kontekst. Zobaczysz, jak ten sam model daje zupełnie inne wyniki w zależności od sposobu formułowania zapytania.

Demo pokazuje także szablony promptów, które są potężnym sposobem tworzenia wielokrotnego użytku promptów z zmiennymi.
Poniższy przykład pokazuje prompt używający `PromptTemplate` LangChain4j do podstawiania zmiennych. AI odpowie na podstawie podanego miejsca docelowego i aktywności.

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

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) i zapytaj:
> - „Jaka jest różnica między promptowaniem zero-shot a few-shot i kiedy używać którego?”
> - „Jak parametr temperature wpływa na odpowiedzi modelu?”
> - „Jakie są techniki zapobiegania atakom typu prompt injection w produkcji?”
> - „Jak tworzyć wielokrotnego użytku obiekty PromptTemplate dla typowych wzorców?”

**Integracja narzędzi** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

To właśnie tutaj LangChain4j pokazuje swoją moc. Użyjesz `AiServices`, aby stworzyć asystenta AI, który może wywoływać twoje metody Java. Po prostu oznacz metody adnotacją `@Tool("opis")`, a LangChain4j zajmie się resztą – AI automatycznie decyduje, kiedy użyć poszczególnych narzędzi na podstawie zapytań użytkownika. To pokazuje wywołanie funkcji, kluczową technikę budowy AI, które może podejmować działania, nie tylko odpowiadać na pytania.

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

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i zapytaj:
> - „Jak działa adnotacja @Tool i co LangChain4j robi z nią w tle?”
> - „Czy AI może wywołać wiele narzędzi w sekwencji, aby rozwiązać złożone problemy?”
> - „Co się stanie, jeśli narzędzie rzuci wyjątek – jak obsługiwać błędy?”
> - „Jak zintegrować prawdziwe API zamiast tego przykładu kalkulatora?”

**Pytania i odpowiedzi na dokumentach (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tutaj zobaczysz RAG (retrieval-augmented generation) używając podejścia LangChain4j „Easy RAG”. Dokumenty są ładowane, automatycznie dzielone i osadzane w pamięci, a następnie wyszukiwarka dostarcza odpowiednie fragmenty AI w czasie zapytania. AI odpowiada na podstawie twoich dokumentów, a nie wiedzy ogólnej.

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

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) i zapytaj:
> - „Jak RAG zapobiega halucynacjom AI w porównaniu z używaniem danych treningowych modelu?”
> - „Jaka jest różnica między tym łatwym podejściem a niestandardową pipeliną RAG?”
> - „Jak rozwinąć to, by obsłużyć wiele dokumentów lub większe bazy wiedzy?”

**Odpowiedzialna AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Buduj bezpieczeństwo AI z obroną w głębi. Demo pokazuje dwie warstwy ochrony współpracujące ze sobą:

**Część 1: Guardrails na wejściu LangChain4j** – blokują niebezpieczne promptów zanim dotrą do LLM. Twórz własne reguły sprawdzające niedozwolone słowa kluczowe lub wzorce. Działają w twoim kodzie, więc są szybkie i darmowe.

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

**Część 2: Filtry bezpieczeństwa dostawcy** – Modele GitHub mają wbudowane filtry, które łapią to, co mogą ominąć twoje reguły. Zobaczysz twarde blokady (błędy HTTP 400) dla poważnych naruszeń oraz miękkie odmowy, gdy AI grzecznie odmawia.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i zapytaj:
> - „Czym jest InputGuardrail i jak stworzyć własny?”
> - „Jaka jest różnica między twardą blokadą a miękką odmową?”
> - „Dlaczego używać jednocześnie guardrails i filtrów dostawcy?”

## Następne kroki

**Następny moduł:** [01-introduction - Pierwsze kroki z LangChain4j i gpt-5 na Azure](../01-introduction/README.md)

---

**Nawigacja:** [← Wróć do Głównej](../README.md) | [Dalej: Moduł 01 - Wprowadzenie →](../01-introduction/README.md)

---

## Rozwiązywanie problemów

### Pierwsze budowanie Maven

**Problem**: Początkowe `mvn clean compile` lub `mvn package` zajmuje dużo czasu (10-15 minut)

**Przyczyna**: Maven musi pobrać wszystkie zależności projektu (Spring Boot, biblioteki LangChain4j, Azure SDK itp.) przy pierwszym budowaniu.

**Rozwiązanie**: To normalne zachowanie. Kolejne budowania będą dużo szybsze, ponieważ zależności są buforowane lokalnie. Czas pobierania zależy od prędkości twojej sieci.

### Składnia poleceń Maven w PowerShell

**Problem**: Polecenia Maven zwracają błąd `Unknown lifecycle phase ".mainClass=..."`
**Przyczyna**: PowerShell interpretuje `=` jako operator przypisania zmiennej, co powoduje błąd składni właściwości Maven

**Rozwiązanie**: Użyj operatora zatrzymującego parsowanie `--%` przed poleceniem Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` informuje PowerShell, aby wszystkie pozostałe argumenty przekazał dosłownie do Mavena bez interpretacji.

### Wyświetlanie emoji w Windows PowerShell

**Problem**: Odpowiedzi AI wyświetlają nieczytelne znaki (np. `????` lub `â??`) zamiast emoji w PowerShell

**Przyczyna**: Domyślne kodowanie PowerShell nie obsługuje emoji UTF-8

**Rozwiązanie**: Uruchom to polecenie przed wykonaniem aplikacji Java:
```cmd
chcp 65001
```

To wymusza kodowanie UTF-8 w terminalu. Alternatywnie, użyj Windows Terminal, który ma lepsze wsparcie dla Unicode.

### Debugowanie wywołań API

**Problem**: Błędy uwierzytelniania, limity zapytań lub nieoczekiwane odpowiedzi od modelu AI

**Rozwiązanie**: Przykłady zawierają `.logRequests(true)` oraz `.logResponses(true)`, które pokazują wywołania API w konsoli. Pomaga to diagnozować błędy uwierzytelniania, limity zapytań lub nieoczekiwane odpowiedzi. Usuń te flagi w środowisku produkcyjnym, aby zmniejszyć ilość logów.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony przy użyciu automatycznej usługi tłumaczeniowej [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do jak największej dokładności, prosimy mieć na uwadze, że tłumaczenia automatyczne mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym należy uważać za wiarygodne i autorytatywne źródło informacji. W przypadku informacji istotnych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->