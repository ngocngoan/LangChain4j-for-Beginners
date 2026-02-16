# Moduł 00: Szybki start

## Spis treści

- [Wprowadzenie](../../../00-quick-start)
- [Czym jest LangChain4j?](../../../00-quick-start)
- [Zależności LangChain4j](../../../00-quick-start)
- [Wymagania wstępne](../../../00-quick-start)
- [Konfiguracja](../../../00-quick-start)
  - [1. Pobierz swój token GitHub](../../../00-quick-start)
  - [2. Ustaw swój token](../../../00-quick-start)
- [Uruchom przykłady](../../../00-quick-start)
  - [1. Podstawowy czat](../../../00-quick-start)
  - [2. Wzorce promptów](../../../00-quick-start)
  - [3. Wywoływanie funkcji](../../../00-quick-start)
  - [4. Pytania i odpowiedzi na dokumentach (RAG)](../../../00-quick-start)
  - [5. Odpowiedzialna AI](../../../00-quick-start)
- [Co pokazuje każdy przykład](../../../00-quick-start)
- [Kolejne kroki](../../../00-quick-start)
- [Rozwiązywanie problemów](../../../00-quick-start)

## Wprowadzenie

Ten szybki start ma na celu jak najszybsze uruchomienie LangChain4j. Obejmuje absolutne podstawy budowania aplikacji AI przy użyciu LangChain4j i modeli GitHub. W kolejnych modułach użyjesz Azure OpenAI z LangChain4j do tworzenia bardziej zaawansowanych aplikacji.

## Czym jest LangChain4j?

LangChain4j to biblioteka Java, która upraszcza tworzenie aplikacji zasilanych AI. Zamiast zajmować się klientami HTTP i analizą JSON, pracujesz z czystym API w Javie.

„Łańcuch” w LangChain odnosi się do łączenia wielu komponentów — możesz połączyć prompt z modelem, z parserem lub łączyć wiele wywołań AI, gdzie jedno wyjście zasila kolejne wejście. Ten szybki start skupia się na podstawach, zanim przejdziemy do bardziej złożonych łańcuchów.

<img src="../../../translated_images/pl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Łączenie komponentów w LangChain4j – klocki budujące tworzą potężne przepływy pracy AI*

Użyjemy trzech podstawowych komponentów:

**ChatLanguageModel** – interfejs do interakcji z modelem AI. Wywołaj `model.chat("prompt")` i otrzymaj odpowiedź w postaci tekstu. Używamy `OpenAiOfficialChatModel`, który współpracuje z punktami końcowymi zgodnymi z OpenAI, takimi jak modele GitHub.

**AiServices** – tworzy typowo-bezpieczne interfejsy usług AI. Definiujesz metody, oznaczasz je adnotacją `@Tool`, a LangChain4j zajmuje się orchestracją. AI automatycznie wywołuje Twoje metody w Javie wtedy, gdy jest to potrzebne.

**MessageWindowChatMemory** – utrzymuje historię rozmowy. Bez tego każde żądanie jest niezależne. Z tym AI pamięta poprzednie wiadomości i utrzymuje kontekst przez wiele tur.

<img src="../../../translated_images/pl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Architektura LangChain4j – podstawowe komponenty współpracujące, aby zasilać Twoje aplikacje AI*

## Zależności LangChain4j

Ten szybki start używa dwóch zależności Maven w pliku [`pom.xml`](../../../00-quick-start/pom.xml):

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

Moduł `langchain4j-open-ai-official` dostarcza klasę `OpenAiOfficialChatModel`, która łączy się z API kompatybilnymi z OpenAI. Modele GitHub używają tego samego formatu API, więc nie jest potrzebny specjalny adapter — wystarczy wskazać bazowy URL na `https://models.github.ai/inference`.

## Wymagania wstępne

**Korzystasz z Dev Container?** Java i Maven są już zainstalowane. Potrzebujesz tylko tokena dostępu osobistego GitHub.

**Lokalny rozwój:**
- Java 21+, Maven 3.9+
- Token dostępu osobistego GitHub (instrukcje poniżej)

> **Uwaga:** Ten moduł korzysta z `gpt-4.1-nano` z modeli GitHub. Nie modyfikuj nazwy modelu w kodzie — jest skonfigurowany do pracy z dostępnymi tam modelami.

## Konfiguracja

### 1. Pobierz swój token GitHub

1. Przejdź do [Ustawienia GitHub → Tokeny dostępu osobistego](https://github.com/settings/personal-access-tokens)
2. Kliknij „Generate new token”
3. Nadaj opisową nazwę (np. „LangChain4j Demo”)
4. Ustaw czas ważności (zalecane 7 dni)
5. W „Uprawnienia konta” znajdź „Models” i ustaw na „Read-only”
6. Kliknij „Generate token”
7. Skopiuj i zapisz token – nie będzie już ponownie widoczny

### 2. Ustaw swój token

**Opcja 1: Korzystając z VS Code (zalecane)**

Jeśli używasz VS Code, dodaj swój token do pliku `.env` w katalogu głównym projektu:

Jeśli plik `.env` nie istnieje, skopiuj `.env.example` do `.env` lub utwórz nowy plik `.env` w katalogu głównym projektu.

**Przykładowy plik `.env`:**
```bash
# W /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Następnie możesz po prostu kliknąć prawym przyciskiem myszy na dowolny plik demo (np. `BasicChatDemo.java`) w Eksploratorze i wybrać **„Run Java”** lub użyć konfiguracji uruchamiania z panelu Run and Debug.

**Opcja 2: Korzystając z terminala**

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

**Używając VS Code:** Kliknij prawym przyciskiem na dowolny plik demo w Eksploratorze i wybierz **„Run Java”**, lub użyj konfiguracji uruchamiania z panelu Run and Debug (upewnij się wcześniej, że dodałeś token do pliku `.env`).

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

Pokazuje prompt zero-shot, few-shot, chain-of-thought oraz role-based.

### 3. Wywoływanie funkcji

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automatycznie wywołuje Twoje metody w Javie wtedy, gdy jest to potrzebne.

### 4. Pytania i odpowiedzi na dokumentach (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Zadawaj pytania dotyczące zawartości w `document.txt`.

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

**Podstawowy czat** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Zacznij tutaj, aby zobaczyć LangChain4j w najprostszej formie. Stworzysz `OpenAiOfficialChatModel`, wyślesz prompt z `.chat()` i otrzymasz odpowiedź. To pokazuje podstawy: jak inicjalizować modele z własnymi punktami końcowymi i kluczami API. Gdy zrozumiesz ten wzorzec, wszystko inne na nim bazuje.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) i zapytaj:
> - „Jak przełączyć się z modeli GitHub na Azure OpenAI w tym kodzie?”
> - „Jakie inne parametry mogę skonfigurować w OpenAiOfficialChatModel.builder()?”
> - „Jak dodać streaming odpowiedzi zamiast czekać na całą odpowiedź?”

**Inżynieria promptów** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Teraz, gdy wiesz, jak rozmawiać z modelem, przyjrzyjmy się co do niego mówisz. To demo używa tego samego ustawienia modelu, ale pokazuje pięć różnych wzorców promptów. Wypróbuj prompt zero-shot dla bezpośrednich instrukcji, few-shot, które uczą się na przykładach, chain-of-thought ujawniające kroki rozumowania oraz role-based, które ustalają kontekst. Zobaczysz jak ten sam model daje diametralnie różne wyniki, w zależności od postawienia zapytania.

Demo pokazuje także szablony promptów, które są potężnym sposobem na tworzenie wielokrotnego użytku promptów ze zmiennymi.
Poniższy przykład pokazuje prompt z użyciem `PromptTemplate` LangChain4j, który wypełnia zmienne. AI odpowie na podstawie podanego celu i aktywności.

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

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) i zapytaj:
> - „Jaka jest różnica między zero-shot a few-shot promptowaniem i kiedy powinienem ich używać?”
> - „Jak parametr temperature wpływa na odpowiedzi modelu?”
> - „Jakie są techniki zapobiegania atakom prompt injection w produkcji?”
> - „Jak tworzyć wielokrotnie używalne obiekty PromptTemplate dla typowych wzorców?”

**Integracja narzędzi** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Tu LangChain4j staje się potężny. Użyjesz `AiServices`, aby stworzyć asystenta AI, który wywołuje Twoje metody w Javie. Wystarczy oznaczyć metody adnotacją `@Tool("opis")`, a LangChain4j zajmie się resztą — AI automatycznie decyduje, kiedy użyć jakiego narzędzia na podstawie pytań użytkownika. To pokazuje wywoływanie funkcji, kluczową technikę do budowania AI, które wykonuje działania, a nie tylko odpowiada na pytania.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) i zapytaj:
> - „Jak działa adnotacja @Tool i co LangChain4j z nią robi w tle?”
> - „Czy AI może wywołać wiele narzędzi po kolei, aby rozwiązać złożone problemy?”
> - „Co się stanie, gdy narzędzie rzuci wyjątek – jak obsługiwać błędy?”
> - „Jak zintegrowałbym prawdziwe API zamiast tego przykładu kalkulatora?”

**Pytania i odpowiedzi na dokumentach (RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Tutaj zobaczysz podstawy RAG (retrieval-augmented generation). Zamiast polegać na danych treningowych modelu, ładujesz zawartość z [`document.txt`](../../../00-quick-start/document.txt) i włączasz ją do promptu. AI odpowiada na podstawie Twojego dokumentu, a nie ogólnej wiedzy. To pierwszy krok do budowy systemów, które wykorzystują własne dane.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Uwaga:** To proste podejście ładuje cały dokument do promptu. Przy dużych plikach (>10KB) przekroczysz limity kontekstu. Moduł 03 omawia dzielenie na fragmenty i wyszukiwanie wektorowe dla produkcyjnych systemów RAG.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) i zapytaj:
> - „Jak RAG zapobiega halucynacjom AI w porównaniu z użyciem danych treningowych modelu?”
> - „Jaka jest różnica między tym prostym podejściem a używaniem osadzeń wektorowych do wyszukiwania?”
> - „Jak skalować to rozwiązanie, obsługując wiele dokumentów lub większe bazy wiedzy?”
> - „Jakie są najlepsze praktyki strukturyzowania promptu, aby AI korzystało wyłącznie z dostarczonego kontekstu?”

**Odpowiedzialna AI** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Buduj bezpieczeństwo AI przez wielopoziomową ochronę. To demo pokazuje dwie współdziałające warstwy ochrony:

**Część 1: LangChain4j Input Guardrails** – blokują niebezpieczne prompty zanim dotrą do LLM. Twórz własne zabezpieczenia, które sprawdzają zakazane słowa kluczowe lub wzorce. Działają w Twoim kodzie, więc są szybkie i darmowe.

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

**Część 2: Filtry bezpieczeństwa dostawcy** – Modele GitHub mają wbudowane filtry, które wyłapują to, co mogą ominąć Twoje zabezpieczenia. Zobaczysz twarde blokady (błędy HTTP 400) dla poważnych naruszeń oraz miękkie odmowy, gdy AI grzecznie odmawia.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) i zapytaj:
> - „Czym jest InputGuardrail i jak stworzyć własne?”
> - „Jaka jest różnica między twardą blokadą a miękką odmową?”
> - „Dlaczego używać zarówno zabezpieczeń, jak i filtrów dostawcy razem?”

## Kolejne kroki

**Następny moduł:** [01-introduction - Pierwsze kroki z LangChain4j i gpt-5 na Azure](../01-introduction/README.md)

---

**Nawigacja:** [← Wróć do głównego](../README.md) | [Dalej: Moduł 01 - Wprowadzenie →](../01-introduction/README.md)

---

## Rozwiązywanie problemów

### Pierwsze budowanie Maven

**Problem:** Pierwsze `mvn clean compile` lub `mvn package` trwa długo (10-15 minut)

**Przyczyna:** Maven musi pobrać wszystkie zależności projektu (Spring Boot, biblioteki LangChain4j, SDK Azure itd.) podczas pierwszego budowania.

**Rozwiązanie:** To normalne zachowanie. Kolejne budowania będą dużo szybsze, ponieważ zależności są lokalnie pamiętane w pamięci podręcznej. Czas pobierania zależy od szybkości Twojego łącza internetowego.
### Składnia polecenia PowerShell Maven

**Problem**: Polecenia Maven kończą się błędem `Unknown lifecycle phase ".mainClass=..."`

**Przyczyna**: PowerShell interpretuje `=` jako operator przypisania zmiennej, co łamie składnię właściwości Maven

**Rozwiązanie**: Użyj operatora zatrzymującego parsowanie `--%` przed poleceniem Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operator `--%` mówi PowerShell, aby przekazał wszystkie pozostałe argumenty literalnie do Maven, bez interpretacji.

### Wyświetlanie emoji w Windows PowerShell

**Problem**: Odpowiedzi AI pokazują znaki pustakowe (np. `????` lub `â??`) zamiast emoji w PowerShell

**Przyczyna**: Domyślne kodowanie PowerShell nie obsługuje emoji UTF-8

**Rozwiązanie**: Uruchom to polecenie przed uruchomieniem aplikacji Java:
```cmd
chcp 65001
```

To wymusza kodowanie UTF-8 w terminalu. Alternatywnie, użyj Windows Terminal, który ma lepsze wsparcie dla Unicode.

### Debugowanie wywołań API

**Problem**: Błędy uwierzytelniania, limity zapytań lub nieoczekiwane odpowiedzi od modelu AI

**Rozwiązanie**: Przykłady zawierają `.logRequests(true)` oraz `.logResponses(true)`, aby pokazać wywołania API w konsoli. Pomaga to rozwiązywać błędy uwierzytelniania, limity zapytań lub nieoczekiwane odpowiedzi. Usuń te flagi w wersji produkcyjnej, aby zmniejszyć szum w logach.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Dokument ten został przetłumaczony za pomocą usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dokładamy starań, aby tłumaczenie było jak najdokładniejsze, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym powinien być traktowany jako autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->