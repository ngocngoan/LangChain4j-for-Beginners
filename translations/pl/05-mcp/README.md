# Moduł 05: Protokół Kontekstu Modelu (MCP)

## Spis treści

- [Czego się nauczysz](../../../05-mcp)
- [Czym jest MCP?](../../../05-mcp)
- [Jak działa MCP](../../../05-mcp)
- [Moduł Agentowy](../../../05-mcp)
- [Uruchamianie przykładów](../../../05-mcp)
  - [Wymagania wstępne](../../../05-mcp)
- [Szybki start](../../../05-mcp)
  - [Operacje na plikach (Stdio)](../../../05-mcp)
  - [Agent Nadzorujący](../../../05-mcp)
    - [Uruchamianie demonstracji](../../../05-mcp)
    - [Jak działa Supervisor](../../../05-mcp)
    - [Strategie odpowiedzi](../../../05-mcp)
    - [Zrozumienie wyjścia](../../../05-mcp)
    - [Wyjaśnienie funkcji modułu agentowego](../../../05-mcp)
- [Kluczowe pojęcia](../../../05-mcp)
- [Gratulacje!](../../../05-mcp)
  - [Co dalej?](../../../05-mcp)

## Czego się nauczysz

Zbudowałeś konwersacyjnego AI, opanowałeś promptowanie, opierałeś odpowiedzi na dokumentach i stworzyłeś agentów z narzędziami. Ale wszystkie te narzędzia były niestandardowo tworzone dla twojej konkretnej aplikacji. Co jeśli mógłbyś dać swojemu AI dostęp do standaryzowanego ekosystemu narzędzi, które każdy może tworzyć i udostępniać? W tym module nauczysz się, jak to zrobić, używając Protokołu Kontekstu Modelu (MCP) i modułu agentowego LangChain4j. Najpierw zaprezentujemy prosty czytnik plików MCP, a następnie pokażemy, jak łatwo integruje się on z zaawansowanymi agentowymi przepływami pracy z użyciem wzorca Agenta Nadzorującego.

## Czym jest MCP?

Protokół Kontekstu Modelu (MCP) zapewnia dokładnie to - standardowy sposób, w jaki aplikacje AI mogą odkrywać i korzystać z zewnętrznych narzędzi. Zamiast pisać niestandardowe integracje dla każdego źródła danych lub usługi, łączysz się z serwerami MCP, które udostępniają swoje możliwości w spójnym formacie. Twój agent AI może wtedy automatycznie odkrywać i używać tych narzędzi.

Poniższy diagram pokazuje różnicę — bez MCP każda integracja wymaga niestandardowego, punkt-punktowego połączenia; z MCP jeden protokół łączy twoją aplikację z dowolnym narzędziem:

<img src="../../../translated_images/pl/mcp-comparison.9129a881ecf10ff5.webp" alt="Porównanie MCP" width="800"/>

*Przed MCP: złożone integracje punkt-punkt. Po MCP: jeden protokół, nieskończone możliwości.*

MCP rozwiązuje podstawowy problem w rozwoju AI: każda integracja jest niestandardowa. Chcesz uzyskać dostęp do GitHub? Niestandardowy kod. Chcesz czytać pliki? Niestandardowy kod. Chcesz zapytać bazę danych? Niestandardowy kod. I żadna z tych integracji nie działa z innymi aplikacjami AI.

MCP standaryzuje to. Serwer MCP udostępnia narzędzia z jasnymi opisami i schematami parametrów. Każdy klient MCP może się połączyć, odkryć dostępne narzędzia i z nich korzystać. Zbuduj raz, używaj wszędzie.

Poniższy diagram ilustruje tę architekturę — pojedynczy klient MCP (twoja aplikacja AI) łączy się z wieloma serwerami MCP, z których każdy udostępnia swój zestaw narzędzi przez standardowy protokół:

<img src="../../../translated_images/pl/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektura MCP" width="800"/>

*Architektura Protokołu Kontekstu Modelu - standaryzowane odkrywanie narzędzi i ich wykonywanie*

## Jak działa MCP

Pod maską MCP używa wielowarstwowej architektury. Twoja aplikacja Java (klient MCP) odkrywa dostępne narzędzia, wysyła żądania JSON-RPC przez warstwę transportową (Stdio lub HTTP), a serwer MCP wykonuje operacje i zwraca wyniki. Następny diagram rozkłada na warstwy ten protokół:

<img src="../../../translated_images/pl/mcp-protocol-detail.01204e056f45308b.webp" alt="Szczegóły protokołu MCP" width="800"/>

*Jak działa MCP „pod maską” — klienci odkrywają narzędzia, wymieniają wiadomości JSON-RPC i wykonują operacje przez warstwę transportową.*

**Architektura klient-serwer**

MCP używa modelu klient-serwer. Serwery dostarczają narzędzia - odczytywanie plików, zapytania do baz danych, wywołania API. Klienci (twoja aplikacja AI) łączą się z serwerami i korzystają z ich narzędzi.

Aby używać MCP z LangChain4j, dodaj następującą zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Odkrywanie narzędzi**

Gdy twój klient łączy się z serwerem MCP, pyta „Jakie masz narzędzia?” Serwer odpowiada listą dostępnych narzędzi, każde z opisami i schematami parametrów. Twój agent AI może wtedy zdecydować, które narzędzia z użyje na podstawie żądań użytkownika. Poniższy diagram przedstawia to powitanie — klient wysyła zapytanie `tools/list`, a serwer zwraca dostępne narzędzia z opisami i schematami parametrów:

<img src="../../../translated_images/pl/tool-discovery.07760a8a301a7832.webp" alt="Odkrywanie narzędzi MCP" width="800"/>

*AI odkrywa dostępne narzędzia podczas uruchamiania — od tej pory wie, jakie możliwości są dostępne i może zdecydować, których użyć.*

**Mechanizmy transportu**

MCP obsługuje różne mechanizmy transportowe. Dwie opcje to Stdio (do komunikacji z lokalnym procesem potomnym) i Streamable HTTP (dla zdalnych serwerów). Ten moduł demonstruje transport Stdio:

<img src="../../../translated_images/pl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mechanizmy transportu" width="800"/>

*Mechanizmy transportu MCP: HTTP dla zdalnych serwerów, Stdio dla procesów lokalnych*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Do procesów lokalnych. Twoja aplikacja uruchamia serwer jako proces potomny i komunikuje się przez standardowe wejście/wyjście. Przydatne do dostępu do systemu plików lub narzędzi linii poleceń.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

Serwer `@modelcontextprotocol/server-filesystem` udostępnia następujące narzędzia, wszystkie odseparowane tylko do określonych katalogów:

| Narzędzie | Opis |
|------|-------------|
| `read_file` | Odczyt zawartości jednego pliku |
| `read_multiple_files` | Odczyt wielu plików w jednym wywołaniu |
| `write_file` | Tworzenie lub nadpisywanie pliku |
| `edit_file` | Dokonywanie celowanych edycji find-and-replace |
| `list_directory` | Wypisywanie plików i katalogów na podanej ścieżce |
| `search_files` | Rekurencyjne wyszukiwanie plików pasujących do wzorca |
| `get_file_info` | Pobranie metadanych pliku (rozmiar, znaczniki czasu, uprawnienia) |
| `create_directory` | Tworzenie katalogu (wraz z katalogami nadrzędnymi) |
| `move_file` | Przenoszenie lub zmiana nazwy pliku albo katalogu |

Poniższy diagram pokazuje, jak transport Stdio działa w czasie działania — twoja aplikacja Java uruchamia serwer MCP jako proces potomny i komunikują się one przez potoki stdin/stdout, bez użycia sieci i HTTP:

<img src="../../../translated_images/pl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Przepływ transportu Stdio" width="800"/>

*Transport Stdio w praktyce — twoja aplikacja uruchamia serwer MCP jako proces potomny i komunikuje się przez potoki stdin/stdout.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) i zapytaj:
> - „Jak działa transport Stdio i kiedy używać go zamiast HTTP?”
> - „Jak LangChain4j zarządza cyklem życia uruchamianych procesów serwera MCP?”
> - „Jakie są implikacje bezpieczeństwa udostępniania AI dostępu do systemu plików?”

## Moduł Agentowy

Chociaż MCP dostarcza standaryzowanych narzędzi, moduł agentowy LangChain4j zapewnia deklaratywny sposób budowania agentów, którzy orkiestrują te narzędzia. Adnotacja `@Agent` i `AgenticServices` pozwalają definiować zachowanie agenta przez interfejsy zamiast kodu imperatywnego.

W tym module poznasz wzorzec **Agenta Nadzorującego** — zaawansowane podejście agentowe, w którym agent „nadzorujący” dynamicznie decyduje, których pod-agentów wywołać na podstawie żądań użytkownika. Połączymy oba koncepcje, dając jednemu z naszych pod-agentów możliwość dostępu do plików przez MCP.

Aby używać modułu agentowego, dodaj następującą zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Uwaga:** Moduł `langchain4j-agentic` używa osobnej właściwości wersji (`langchain4j.mcp.version`), ponieważ jest wydawany według innego harmonogramu niż główne biblioteki LangChain4j.

> **⚠️ Eksperymentalne:** Moduł `langchain4j-agentic` jest **eksperymentalny** i może ulec zmianie. Stabilnym sposobem budowania asystentów AI pozostaje `langchain4j-core` z niestandardowymi narzędziami (Moduł 04).

## Uruchamianie przykładów

### Wymagania wstępne

- Ukończony [Moduł 04 - Narzędzia](../04-tools/README.md) (ten moduł opiera się na koncepcjach niestandardowych narzędzi i porównuje je z narzędziami MCP)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Module 01)
- Java 21+, Maven 3.9+
- Node.js 16+ i npm (dla serwerów MCP)

> **Uwaga:** Jeśli jeszcze nie skonfigurowałeś zmiennych środowiskowych, zobacz [Moduł 01 - Wprowadzenie](../01-introduction/README.md) z instrukcjami wdrożenia (`azd up` tworzy plik `.env` automatycznie), lub skopiuj `.env.example` do `.env` w katalogu głównym i wypełnij wartości.

## Szybki start

**Używając VS Code:** Kliknij prawym przyciskiem myszy dowolny plik demo w Eksploratorze i wybierz **„Uruchom Java”**, lub skorzystaj z konfiguracji uruchamiania w panelu Run and Debug (upewnij się, że plik `.env` jest wcześniej skonfigurowany z poświadczeniami Azure).

**Używając Maven:** Alternatywnie możesz uruchomić z linii poleceń za pomocą poniższych przykładów.

### Operacje na plikach (Stdio)

To demonstruje narzędzia bazujące na lokalnym procesie potomnym.

**✅ Nie są potrzebne wymagania wstępne** - serwer MCP uruchamia się automatycznie.

**Użycie skryptów startowych (zalecane):**

Skrypty startowe automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Używając VS Code:** Kliknij prawym przyciskiem na `StdioTransportDemo.java` i wybierz **„Uruchom Java”** (upewnij się, że plik `.env` jest skonfigurowany).

Aplikacja automatycznie uruchamia serwer MCP systemu plików i odczytuje lokalny plik. Zauważ jak zarządzanie procesem potomnym jest wyprowadzane za Ciebie.

**Oczekiwane wyjście:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Nadzorujący

Wzorzec **Agenta Nadzorującego** to **elastyczna** forma agentowego AI. Supervisor używa LLM, aby autonomicznie zdecydować, których agentów wywołać na podstawie żądania użytkownika. W następnym przykładzie łączymy dostęp do plików przez MCP z agentem LLM, aby stworzyć nadzorowany przepływ pracy: odczyt pliku → raport.

W demonstracji `FileAgent` odczytuje plik przy użyciu narzędzi MCP systemu plików, a `ReportAgent` generuje ustrukturyzowany raport z krótkim streszczeniem (1 zdanie), 3 kluczowymi punktami i rekomendacjami. Supervisor orkiestruje ten przepływ automatycznie:

<img src="../../../translated_images/pl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Wzorzec Agenta Nadzorującego" width="800"/>

*Supervisor używa swojego LLM, aby zdecydować, których agentów wywołać i w jakiej kolejności — nie ma potrzeby zakodowanego routera.*

Oto jak wygląda konkretny przepływ pracy naszej ścieżki plik→raport:

<img src="../../../translated_images/pl/file-report-workflow.649bb7a896800de9.webp" alt="Przepływ pracy plik-do-raportu" width="800"/>

*FileAgent odczytuje plik przez narzędzia MCP, następnie ReportAgent przetwarza surową zawartość w ustrukturyzowany raport.*

Każdy agent zapisuje swoje wyjście w **Agentic Scope** (współdzielona pamięć), pozwalając agentom dalej w przepływie na dostęp do wcześniejszych wyników. To pokazuje, jak narzędzia MCP integrują się płynnie z agentowymi przepływami pracy — Supervisor nie musi wiedzieć *jak* pliki są odczytywane, tylko że `FileAgent` może to zrobić.

#### Uruchamianie demonstracji

Skrypty startowe automatycznie ładują zmienne środowiskowe z pliku `.env` w katalogu głównym:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Używając VS Code:** Kliknij prawym przyciskiem na `SupervisorAgentDemo.java` i wybierz **„Uruchom Java”** (upewnij się, że plik `.env` jest skonfigurowany).

#### Jak działa Supervisor

Przed budową agentów musisz połączyć transport MCP z klientem i opakować go jako `ToolProvider`. Tak narzędzia serwera MCP stają się dostępne dla twoich agentów:

```java
// Utwórz klienta MCP z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Opakuj klienta jako ToolProvider — to łączy narzędzia MCP z LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Teraz możesz wstrzyknąć `mcpToolProvider` do dowolnego agenta, który potrzebuje narzędzi MCP:

```java
// Krok 1: FileAgent odczytuje pliki za pomocą narzędzi MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Posiada narzędzia MCP do operacji na plikach
        .build();

// Krok 2: ReportAgent generuje raporty strukturalne
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordynuje przepływ plik → raport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Zwróć ostateczny raport
        .build();

// Supervisor decyduje, których agentów wywołać na podstawie żądania
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie odpowiedzi

Konfigurując `SupervisorAgent`, określasz, jak powinien sformułować swoją finalną odpowiedź do użytkownika po wykonaniu zadań przez pod-agentów. Poniższy diagram pokazuje trzy dostępne strategie — LAST zwraca bezpośrednio wynik ostatniego agenta, SUMMARY syntetyzuje wszystkie wyjścia przez LLM, a SCORED wybiera wynik lepiej oceniany względem oryginalnego żądania:

<img src="../../../translated_images/pl/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie odpowiedzi" width="800"/>

*Trzy strategie ustalania finalnej odpowiedzi Supervisora — wybierz w zależności czy chcesz ostatnią odpowiedź agenta, podsumowanie syntezowane lub opcję najlepiej ocenianą.*

Dostępne strategie to:

| Strategia | Opis |
|----------|-------------|
| **LAST** | Supervisor zwraca wyjście ostatniego wywołanego pod-agenta lub narzędzia. Jest to przydatne, gdy ostatni agent w przepływie jest specjalnie zaprojektowany, aby wyprodukować kompletną, końcową odpowiedź (np. „Agent Podsumowujący” w pipeline badawczym). |
| **SUMMARY** | Supervisor używa własnego wewnętrznego Modelu Językowego (LLM), aby zsyntetyzować podsumowanie całej interakcji i wszystkich wyjść pod-agentów, a następnie zwraca to podsumowanie jako finalną odpowiedź. Zapewnia to czystą, zagregowaną odpowiedź dla użytkownika. |
| **SCORED** | System używa wewnętrznego LLM do ocenienia zarówno odpowiedzi LAST, jak i SUMMARY względem oryginalnego żądania użytkownika, zwracając wynik, który otrzyma wyższą ocenę. |
Zobacz [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dla pełnej implementacji.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) i zapytaj:
> - "Jak Supervisor decyduje, których agentów wywołać?"
> - "Jaka jest różnica między wzorcami Supervisor a Sequential workflow?"
> - "Jak mogę dostosować zachowanie planowania Supervisora?"

#### Zrozumienie Wyniku

Po uruchomieniu dema zobaczysz uporządkowany przebieg, jak Supervisor orkiestruje wielu agentów. Oto, co oznacza każda sekcja:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Nagłówek** wprowadza koncepcję workflow: skoncentrowany proces od czytania pliku do generowania raportu.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**Diagram Workflow** pokazuje przepływ danych między agentami. Każdy agent ma określoną rolę:
- **FileAgent** czyta pliki za pomocą narzędzi MCP i przechowuje surową zawartość w `fileContent`
- **ReportAgent** konsumuje tę zawartość i tworzy ustrukturyzowany raport w `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Żądanie użytkownika** pokazuje zadanie. Supervisor analizuje je i decyduje o wywołaniu FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Orkiestracja Supervisora** pokazuje 2-etapowy proces w akcji:
1. **FileAgent** czyta plik przez MCP i przechowuje jego zawartość
2. **ReportAgent** otrzymuje zawartość i generuje ustrukturyzowany raport

Supervisor podjął te decyzje **autonomicznie** na podstawie żądania użytkownika.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Wyjaśnienie funkcji modułu Agentic

Przykład demonstruje kilka zaawansowanych funkcji modułu agentic. Przyjrzyjmy się bliżej Agentic Scope i Agent Listenerom.

**Agentic Scope** pokazuje współdzieloną pamięć, w której agenci przechowali swoje wyniki za pomocą `@Agent(outputKey="...")`. Pozwala to na:
- Dostęp kolejnych agentów do wyjść wcześniejszych agentów
- Supervisorowi na syntezę ostatecznej odpowiedzi
- Tobie na sprawdzenie, co wyprodukował każdy agent

Poniższy diagram pokazuje, jak Agentic Scope działa jako współdzielona pamięć w workflow z pliku do raportu — FileAgent zapisuje swój wynik pod kluczem `fileContent`, ReportAgent go odczytuje i zapisuje własny wynik pod `report`:

<img src="../../../translated_images/pl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope działa jako współdzielona pamięć — FileAgent zapisuje `fileContent`, ReportAgent go odczytuje i zapisuje `report`, a Twój kod odczytuje ostateczny wynik.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surowe dane pliku z FileAgent
String report = scope.readState("report");            // Strukturalny raport z ReportAgent
```

**Agent Listeners** umożliwiają monitorowanie i debugowanie działania agentów. Stopniowy wynik, który widzisz w demo, pochodzi z AgentListenera podłączonego do każdego wywołania agenta:
- **beforeAgentInvocation** - Wywoływany, gdy Supervisor wybiera agenta, pozwalając zobaczyć, który agent został wybrany i dlaczego
- **afterAgentInvocation** - Wywoływany po zakończeniu działania agenta, pokazując jego wynik
- **inheritedBySubagents** - Gdy ustawione na true, listener monitoruje wszystkich agentów w hierarchii

Poniższy diagram przedstawia pełen cykl życia Agent Listenerów, w tym sposób obsługi błędów przez `onError` podczas działania agenta:

<img src="../../../translated_images/pl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners podłączają się do cyklu życia wykonywania — monitoruj rozpoczęcie, zakończenie lub błędy agentów.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Rozprzestrzeniać na wszystkich podagentów
    }
};
```

Poza wzorcem Supervisora, moduł `langchain4j-agentic` oferuje kilka zaawansowanych wzorców workflow. Poniższy diagram pokazuje wszystkie pięć — od prostych sekwencyjnych pipeline’ów po workflow z zatwierdzeniem przez człowieka ("human-in-the-loop"):

<img src="../../../translated_images/pl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pięć wzorców workflow do orkiestracji agentów — od prostych sekwencyjnych pipeline’ów po workflow z zatwierdzeniem przez człowieka.*

| Wzorzec | Opis | Przypadek użycia |
|---------|------|------------------|
| **Sequential** | Wykonuj agentów po kolei, wyjście płynie do następnego | Pipeline: badania → analiza → raport |
| **Parallel** | Uruchamiaj agentów jednocześnie | Niezależne zadania: pogoda + wiadomości + akcje |
| **Loop** | Iteruj, aż spełnisz warunek | Ocena jakości: dopracuj aż ocena ≥ 0.8 |
| **Conditional** | Kieruj na podstawie warunków | Klasyfikuj → przesyłaj do specjalistycznego agenta |
| **Human-in-the-Loop** | Dodaj punkty kontrolne dla człowieka | Workflow zatwierdzania, przegląd treści |

## Kluczowe Koncepcje

Skoro już poznałeś MCP i moduł agentic w praktyce, podsumujmy, kiedy używać którego podejścia.

Jedną z największych zalet MCP jest rosnący ekosystem. Poniższy diagram pokazuje, jak uniwersalny protokół łączy Twoją aplikację AI z wieloma serwerami MCP — od dostępu do systemu plików i baz danych po GitHub, e-mail, web scraping i więcej:

<img src="../../../translated_images/pl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP tworzy ekosystem uniwersalnego protokołu — dowolny serwer kompatybilny z MCP działa z dowolnym klientem MCP, umożliwiając współdzielenie narzędzi między aplikacjami.*

**MCP** jest idealny, gdy chcesz korzystać z istniejących ekosystemów narzędzi, budować narzędzia współdzielone między aplikacjami, integrować usługi zewnętrzne z użyciem standardowych protokołów lub wymieniać implementacje narzędzi bez zmiany kodu.

**Moduł Agentic** sprawdza się najlepiej, gdy chcesz deklaratywnie definiować agentów za pomocą adnotacji `@Agent`, potrzebujesz orkiestracji workflow (sekwencyjna, pętla, równoległa), preferujesz projektowanie agentów oparte na interfejsach zamiast kodu imperatywnego, lub łączysz wielu agentów dzielących wyjścia przez `outputKey`.

**Wzorzec Supervisor Agent** błyszczy, gdy workflow nie jest przewidywalny z wyprzedzeniem i chcesz, aby LLM decydował, gdy masz wielu wyspecjalizowanych agentów potrzebujących dynamicznej orkiestracji, budujesz systemy konwersacyjne kierujące do różnych możliwości lub gdy chcesz mieć najbardziej elastyczne, adaptacyjne zachowanie agenta.

Aby pomóc Ci zdecydować między niestandardowymi metodami `@Tool` z Modułu 04 a narzędziami MCP z tego modułu, poniższe porównanie podkreśla kluczowe kompromisy — narzędzia własne dają ścisłą integrację i pełne bezpieczeństwo typów dla logiki specyficznej aplikacji, podczas gdy narzędzia MCP oferują ustandaryzowane, wielokrotnego użytku integracje:

<img src="../../../translated_images/pl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kiedy używać niestandardowych metod @Tool a kiedy narzędzi MCP — narzędzia własne dla logiki specyficznej aplikacji z pełnym bezpieczeństwem typów, narzędzia MCP dla standaryzowanych integracji działających w wielu aplikacjach.*

## Gratulacje!

Przeszedłeś przez wszystkie pięć modułów kursu LangChain4j dla początkujących! Oto pełna ścieżka edukacyjna, którą ukończyłeś — od podstawowego chatbota aż do systemów agentic zasilanych MCP:

<img src="../../../translated_images/pl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Twoja ścieżka edukacyjna przez wszystkie pięć modułów — od podstawowego chatbota po systemy agentic z MCP.*

Ukończyłeś kurs LangChain4j dla początkujących. Nauczyłeś się:

- Jak budować konwersacyjne AI z pamięcią (Moduł 01)
- Wzorów prompt engineering dla różnych zadań (Moduł 02)
- Jak opierać odpowiedzi na Twoich dokumentach za pomocą RAG (Moduł 03)
- Tworzenia podstawowych agentów AI (asystentów) z niestandardowymi narzędziami (Moduł 04)
- Integracji standaryzowanych narzędzi z modułami LangChain4j MCP i Agentic (Moduł 05)

### Co dalej?

Po ukończeniu modułów zapoznaj się z [Testing Guide](../docs/TESTING.md), aby zobaczyć koncepcje testowania LangChain4j w praktyce.

**Oficjalne zasoby:**
- [Dokumentacja LangChain4j](https://docs.langchain4j.dev/) - Kompletne przewodniki i referencje API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kod źródłowy i przykłady
- [Samouczki LangChain4j](https://docs.langchain4j.dev/tutorials/) - Samouczki krok po kroku dla różnych przypadków użycia

Dziękujemy za ukończenie kursu!

---

**Nawigacja:** [← Poprzedni: Moduł 04 - Narzędzia](../04-tools/README.md) | [Powrót do Głównej](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Ten dokument został przetłumaczony przy użyciu usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż staramy się zapewnić dokładność, należy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym należy traktować jako źródło ostateczne. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->