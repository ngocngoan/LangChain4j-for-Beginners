# Moduł 05: Protokół Kontekstu Modelu (MCP)

## Spis treści

- [Czego się nauczysz](../../../05-mcp)
- [Co to jest MCP?](../../../05-mcp)
- [Jak działa MCP](../../../05-mcp)
- [Moduł Agentowy](../../../05-mcp)
- [Uruchamianie przykładów](../../../05-mcp)
  - [Wymagania wstępne](../../../05-mcp)
- [Szybki start](../../../05-mcp)
  - [Operacje na plikach (Stdio)](../../../05-mcp)
  - [Agent Nadzorujący](../../../05-mcp)
    - [Uruchamianie demo](../../../05-mcp)
    - [Jak działa Supervisor](../../../05-mcp)
    - [Jak FileAgent odkrywa narzędzia MCP w czasie działania](../../../05-mcp)
    - [Strategie odpowiedzi](../../../05-mcp)
    - [Zrozumienie wyniku](../../../05-mcp)
    - [Wyjaśnienie funkcji modułu agentowego](../../../05-mcp)
- [Kluczowe pojęcia](../../../05-mcp)
- [Gratulacje!](../../../05-mcp)
  - [Co dalej?](../../../05-mcp)

## Czego się nauczysz

Zbudowałeś konwersacyjne AI, opanowałeś prompty, osadziłeś odpowiedzi w dokumentach i stworzyłeś agentów z narzędziami. Ale wszystkie te narzędzia były tworzone na zamówienie dla Twojej konkretnej aplikacji. A co, gdybyś mógł dać swojemu AI dostęp do ustandaryzowanego ekosystemu narzędzi, które każdy może tworzyć i udostępniać? W tym module nauczysz się, jak to zrobić za pomocą Model Context Protocol (MCP) i modułu agentowego LangChain4j. Najpierw przedstawimy prosty czytnik plików MCP, a potem pokażemy, jak łatwo integruje się on z zaawansowanymi przepływami agentów za pomocą wzorca Supervisor Agent.

## Co to jest MCP?

Model Context Protocol (MCP) zapewnia dokładnie to — standardowy sposób, aby aplikacje AI mogły odkrywać i korzystać z zewnętrznych narzędzi. Zamiast pisać niestandardowe integracje dla każdego źródła danych lub usługi, łączysz się z serwerami MCP, które udostępniają swoje możliwości w spójnym formacie. Twój agent AI może potem automatycznie wykrywać i używać tych narzędzi.

Poniższy diagram pokazuje różnicę — bez MCP każda integracja wymaga niestandardowego połączenia punkt-punkt; z MCP jeden protokół łączy Twoją aplikację z dowolnym narzędziem:

<img src="../../../translated_images/pl/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Porównanie" width="800"/>

*Przed MCP: złożone integracje punkt-punkt. Po MCP: jeden protokół, nieskończone możliwości.*

MCP rozwiązuje podstawowy problem w rozwoju AI: każda integracja jest niestandardowa. Chcesz uzyskać dostęp do GitHub? Niestandardowy kod. Chcesz czytać pliki? Niestandardowy kod. Chcesz zapytać bazę danych? Niestandardowy kod. I żadna z tych integracji nie działa z innymi aplikacjami AI.

MCP standaryzuje to. Serwer MCP udostępnia narzędzia z jasnymi opisami i schematami. Każdy klient MCP może się podłączyć, odkryć dostępne narzędzia i używać ich. Budujesz raz, używasz wszędzie.

Poniższy diagram ilustruje tę architekturę — jeden klient MCP (Twoja aplikacja AI) łączy się z wieloma serwerami MCP, z których każdy udostępnia własny zestaw narzędzi przez standardowy protokół:

<img src="../../../translated_images/pl/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektura MCP" width="800"/>

*Architektura Model Context Protocol — standaryzowane odkrywanie i wykonywanie narzędzi*

## Jak działa MCP

Pod maską MCP korzysta z warstwowej architektury. Twoja aplikacja Java (klient MCP) odkrywa dostępne narzędzia, wysyła żądania JSON-RPC przez warstwę transportową (Stdio lub HTTP), a serwer MCP wykonuje operacje i zwraca wyniki. Poniższy diagram przedstawia każdą warstwę tego protokołu:

<img src="../../../translated_images/pl/mcp-protocol-detail.01204e056f45308b.webp" alt="Szczegóły protokołu MCP" width="800"/>

*Jak działa MCP pod spodem — klienci odkrywają narzędzia, wymieniają komunikaty JSON-RPC i wykonują operacje przez warstwę transportową.*

**Architektura klient-serwer**

MCP korzysta z modelu klient-serwer. Serwery dostarczają narzędzia - czytanie plików, zapytania do baz danych, wywołania API. Klienci (Twoja aplikacja AI) łączą się z serwerami i korzystają z ich narzędzi.

Aby używać MCP z LangChain4j, dodaj tę zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Odkrywanie narzędzi**

Kiedy Twój klient łączy się z serwerem MCP, pyta "Jakie masz narzędzia?" Serwer odpowiada listą dostępnych narzędzi, każde z opisem i schematami parametrów. Twój agent AI może wtedy zdecydować, których narzędzi użyć na podstawie żądań użytkownika. Poniższy diagram przedstawia ten proces — klient wysyła żądanie `tools/list`, a serwer zwraca dostępne narzędzia z opisami i schematami parametrów:

<img src="../../../translated_images/pl/tool-discovery.07760a8a301a7832.webp" alt="Odkrywanie narzędzi MCP" width="800"/>

*AI odkrywa dostępne narzędzia przy starcie — teraz zna dostępne możliwości i może zdecydować, których użyć.*

**Mechanizmy transportu**

MCP obsługuje różne mechanizmy transportu. Dwie opcje to Stdio (dla lokalnej komunikacji podprocesów) i Streamable HTTP (dla serwerów zdalnych). Ten moduł demonstruje transport Stdio:

<img src="../../../translated_images/pl/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mechanizmy transportu" width="800"/>

*Mechanizmy transportu MCP: HTTP dla serwerów zdalnych, Stdio dla procesów lokalnych*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Dla procesów lokalnych. Twoja aplikacja uruchamia serwer jako podproces i komunikuje się przez standardowe wejście/wyjście. Przydatne do dostępu do systemu plików lub narzędzi wiersza poleceń.

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

Serwer `@modelcontextprotocol/server-filesystem` udostępnia następujące narzędzia, wszystkie z ograniczeniami do określonych katalogów:

| Narzędzie | Opis |
|-----------|-------|
| `read_file` | Odczytaj zawartość pojedynczego pliku |
| `read_multiple_files` | Odczytaj wiele plików w jednym wywołaniu |
| `write_file` | Utwórz lub nadpisz plik |
| `edit_file` | Dokonaj ukierunkowanych zamian find-and-replace |
| `list_directory` | Wypisz pliki i katalogi pod ścieżką |
| `search_files` | Rekurencyjnie wyszukaj pliki pasujące do wzorca |
| `get_file_info` | Pobierz metadane pliku (rozmiar, znaczniki czasowe, uprawnienia) |
| `create_directory` | Utwórz katalog (wraz z katalogami nadrzędnymi) |
| `move_file` | Przenieś lub zmień nazwę pliku albo katalogu |

Poniższy diagram pokazuje, jak transport Stdio działa w czasie działania — Twoja aplikacja Java uruchamia serwer MCP jako proces potomny i komunikują się przez rury stdin/stdout, bez użycia sieci czy HTTP:

<img src="../../../translated_images/pl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Przepływ transportu Stdio" width="800"/>

*Transport Stdio w działaniu — aplikacja uruchamia serwer MCP jako proces potomny i komunikuje się przez rury stdin/stdout.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) i zapytaj:
> - "Jak działa transport Stdio i kiedy powinienem używać go zamiast HTTP?"
> - "Jak LangChain4j zarządza cyklem życia uruchamianych procesów serwerów MCP?"
> - "Jakie są implikacje bezpieczeństwa udzielania AI dostępu do systemu plików?"

## Moduł Agentowy

Chociaż MCP dostarcza ustandaryzowane narzędzia, moduł agentowy LangChain4j oferuje deklaratywny sposób budowania agentów, którzy orkiestrują te narzędzia. Adnotacja `@Agent` i `AgenticServices` pozwalają definiować zachowanie agenta przez interfejsy, a nie kod imperatywny.

W tym module poznasz wzorzec **Supervisor Agent** — zaawansowane podejście agentowe, w którym agent „nadzorujący” dynamicznie decyduje, których podagentów wywołać na podstawie żądań użytkownika. Połączymy oba podejścia, dając jednemu z naszych podagentów możliwości dostępu do plików wspierane przez MCP.

Aby używać modułu agentowego, dodaj tę zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Uwaga:** Moduł `langchain4j-agentic` używa osobnej właściwości wersji (`langchain4j.mcp.version`), ponieważ jest wydawany w innym harmonogramie niż główne biblioteki LangChain4j.

> **⚠️ Eksperymentalne:** Moduł `langchain4j-agentic` jest **eksperymentalny** i może się zmieniać. Stabilnym sposobem budowania asystentów AI pozostaje `langchain4j-core` z niestandardowymi narzędziami (Moduł 04).

## Uruchamianie przykładów

### Wymagania wstępne

- Ukończony [Moduł 04 - Narzędzia](../04-tools/README.md) (ten moduł bazuje na koncepcjach niestandardowych narzędzi i porównuje je z narzędziami MCP)
- Plik `.env` w katalogu głównym z poświadczeniami Azure (utworzony przez `azd up` w Moduł 01)
- Java 21+, Maven 3.9+
- Node.js 16+ oraz npm (dla serwerów MCP)

> **Uwaga:** Jeśli nie skonfigurowałeś jeszcze zmiennych środowiskowych, zobacz [Moduł 01 - Wprowadzenie](../01-introduction/README.md) po instrukcje wdrożenia (`azd up` tworzy plik `.env` automatycznie) albo skopiuj `.env.example` do `.env` w katalogu głównym i uzupełnij swoje dane.

## Szybki start

**Używając VS Code:** Po prostu kliknij prawym przyciskiem myszy dowolny plik demo w Explorerze i wybierz **"Run Java"**, lub użyj konfiguracji uruchomienia z panelu Run and Debug (upewnij się najpierw, że plik `.env` jest skonfigurowany z poświadczeniami Azure).

**Używając Maven:** Alternatywnie możesz uruchomić z linii poleceń przy pomocy poniższych przykładów.

### Operacje na plikach (Stdio)

To demonstruje narzędzia oparte na lokalnych podprocesach.

**✅ Nie wymaga wymagań wstępnych** — serwer MCP uruchamiany jest automatycznie.

**Używając skryptów startowych (zalecane):**

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

**Używając VS Code:** Kliknij prawym na `StdioTransportDemo.java` i wybierz **"Run Java"** (upewnij się, że Twój plik `.env` jest skonfigurowany).

Aplikacja automatycznie uruchamia serwer MCP filesystem i czyta lokalny plik. Zwróć uwagę, jak zarządzanie podprocesem jest obsługiwane za Ciebie.

**Oczekiwany wynik:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent Nadzorujący

Wzorzec **Supervisor Agent** to **elastyczna** forma agentowego AI. Supervisor używa LLM, aby autonomicznie zdecydować, których agentów wywołać na podstawie żądania użytkownika. W następnym przykładzie łączymy dostęp do plików wspierany przez MCP z agentem LLM, tworząc nadzorowany przepływ odczytu pliku → raportu.

W demo `FileAgent` czyta plik za pomocą narzędzi MCP filesystem, a `ReportAgent` generuje zorganizowany raport z podsumowaniem wykonawczym (1 zdanie), 3 kluczowymi punktami i rekomendacjami. Supervisor orkiestruje ten przepływ automatycznie:

<img src="../../../translated_images/pl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Wzorzec Agenta Nadzorującego" width="800"/>

*Supervisor używa swojego LLM, by zdecydować, których agentów wywołać i w jakiej kolejności — nie ma potrzeby twardo zakodowanego routingu.*

Tak wygląda konkretny przepływ dla naszego pipeline od pliku do raportu:

<img src="../../../translated_images/pl/file-report-workflow.649bb7a896800de9.webp" alt="Przepływ od pliku do raportu" width="800"/>

*FileAgent czyta plik przez narzędzia MCP, potem ReportAgent przekształca surową treść w uporządkowany raport.*

Poniższy diagram sekwencji śledzi pełną orkiestrację Superwisora — od uruchomienia serwera MCP, przez autonomiczny wybór agentów przez Supervisora, po wywołania narzędzi przez stdio i finalny raport:

<img src="../../../translated_images/pl/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Diagram sekwencji agenta nadzorującego" width="800"/>

*Supervisor autonomicznie wywołuje FileAgent (który wywołuje serwer MCP przez stdio, by odczytać plik), potem wywołuje ReportAgent, aby wygenerować zorganizowany raport — każdy agent zapisuje swój wynik w współdzielonym Agentic Scope.*

Każdy agent zapisuje swój wynik w **Agentic Scope** (współdzielonej pamięci), co pozwala agentom w dalszych etapach korzystać z wcześniejszych wyników. Pokazuje to, jak narzędzia MCP płynnie integrują się z przepływami agentowymi — Supervisor nie musi wiedzieć *jak* pliki są odczytywane, tylko że `FileAgent` potrafi to zrobić.

#### Uruchamianie demo

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

**Używając VS Code:** Kliknij prawym na `SupervisorAgentDemo.java` i wybierz **"Run Java"** (upewnij się, że plik `.env` jest skonfigurowany).

#### Jak działa Supervisor

Przed budową agentów musisz podłączyć transport MCP do klienta i opakować go jako `ToolProvider`. Tak narzędzia serwera MCP stają się dostępne dla Twoich agentów:

```java
// Utwórz klienta MCP z transportu
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Owiń klienta jako ToolProvider — to łączy narzędzia MCP z LangChain4j
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

// Krok 2: ReportAgent generuje uporządkowane raporty
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor koordynuje przepływ pracy plik → raport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Zwróć ostateczny raport
        .build();

// Supervisor decyduje, których agentów wywołać na podstawie żądania
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Jak FileAgent odkrywa narzędzia MCP w czasie działania

Możesz się zastanawiać: **jak `FileAgent` wie, jak korzystać z narzędzi npm filesystem?** Odpowiedź: nie wie — **LLM** dowiaduje się tego w czasie działania na podstawie schematów narzędzi.

Interfejs `FileAgent` jest jedynie **definicją promptu**. Nie zawiera twardo zakodowanej wiedzy o `read_file`, `list_directory` ani innych narzędziach MCP. Oto co dzieje się krok po kroku:
1. **Uruchamianie serwera:** `StdioMcpTransport` uruchamia pakiet npm `@modelcontextprotocol/server-filesystem` jako proces podrzędny  
2. **Odkrywanie narzędzi:** `McpClient` wysyła żądanie JSON-RPC `tools/list` do serwera, który odpowiada nazwami narzędzi, opisami i schematami parametrów (np. `read_file` — *„Odczytaj zawartość całego pliku”* — `{ path: string }`)  
3. **Wstrzykiwanie schematów:** `McpToolProvider` otacza te odkryte schematy i udostępnia je LangChain4j  
4. **Decyzja LLM:** Gdy wywoływana jest metoda `FileAgent.readFile(path)`, LangChain4j wysyła do LLM komunikat systemowy, komunikat użytkownika **i listę schematów narzędzi**. LLM odczytuje opisy narzędzi i generuje wywołanie narzędzia (np. `read_file(path="/some/file.txt")`)  
5. **Wykonanie:** LangChain4j przechwytuje wywołanie narzędzia, przekazuje je przez klienta MCP do procesu podrzędnego Node.js, pobiera wynik i przesyła go z powrotem do LLM  

To ten sam mechanizm [Odkrywania Narzędzi](../../../05-mcp) opisany powyżej, ale zastosowany konkretnie do przepływu pracy agenta. Adnotacje `@SystemMessage` i `@UserMessage` kierują zachowaniem LLM, podczas gdy wstrzyknięty `ToolProvider` zapewnia **możliwości** — LLM łączy oba elementy w czasie wykonywania.

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) i zapytaj:  
> - "Jak ten agent wie, które narzędzie MCP wywołać?"  
> - "Co się stanie, jeśli usunę ToolProvider z budowniczego agenta?"  
> - "Jak schematy narzędzi są przekazywane do LLM?"  

#### Strategie odpowiedzi

Konfigurując `SupervisorAgent`, określasz, jak powinien formułować ostateczną odpowiedź dla użytkownika po zakończeniu zadań przez podagentów. Poniższy diagram pokazuje trzy dostępne strategie — LAST zwraca bezpośrednio wynik ostatniego agenta, SUMMARY syntezuje wszystkie wyniki przez LLM, a SCORED wybiera ten, który uzyska wyższą ocenę względem oryginalnego zapytania:

<img src="../../../translated_images/pl/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Trzy strategie, jak Supervisor formułuje ostateczną odpowiedź — wybierz w zależności od tego, czy chcesz wynik ostatniego agenta, syntetyczne podsumowanie czy najlepszy oceniany wynik.*

Dostępne strategie to:

| Strategia | Opis |
|-----------|------|
| **LAST**  | Supervisor zwraca wynik ostatniego wywołanego podagenta lub narzędzia. Przydatne, gdy ostatni agent w przebiegu pracy jest specjalnie zaprojektowany do wygenerowania kompletnej, ostatecznej odpowiedzi (np. „Agent Podsumowujący” w potoku badawczym). |
| **SUMMARY** | Supervisor używa swojego wewnętrznego modelu językowego (LLM), by zsyntetyzować podsumowanie całej interakcji i wyników podagentów, a następnie zwraca to podsumowanie jako końcową odpowiedź. Zapewnia to czystą, zagregowaną odpowiedź dla użytkownika. |
| **SCORED** | System używa wewnętrznego LLM, by ocenić zarówno odpowiedź LAST, jak i podsumowanie SUMMARY względem oryginalnego zapytania użytkownika, zwracając tę odpowiedź, która uzyska wyższą ocenę. |

Pełną implementację zobacz w pliku [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Wypróbuj z czatem [GitHub Copilot](https://github.com/features/copilot):** Otwórz [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) i zapytaj:  
> - "Jak Supervisor decyduje, których agentów wywołać?"  
> - "Jaka jest różnica między wzorcami Supervisor a Sequential?"  
> - "Jak mogę dostosować zachowanie planowania Supervisora?"  

#### Zrozumienie wyniku

Uruchamiając demo, zobaczysz usystematyzowany przegląd, jak Supervisor orkiestruje wielu agentów. Oto znaczenie poszczególnych sekcji:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Nagłówek** wprowadza koncepcję przepływu pracy: ukierunkowany potok od odczytu pliku do generowania raportu.

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
  
**Diagram przepływu pracy** pokazuje przepływ danych między agentami. Każdy agent ma swoją rolę:  
- **FileAgent** czyta pliki za pomocą narzędzi MCP i zapisuje surową zawartość w `fileContent`  
- **ReportAgent** konsumuje tę zawartość i generuje strukturalny raport w `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Zapytanie użytkownika** pokazuje zadanie. Supervisor analizuje to i decyduje się uruchomić FileAgent → ReportAgent.

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
  
**Orkiestracja Supervisora** pokazuje dwustopniowy przepływ w praktyce:  
1. **FileAgent** odczytuje plik przez MCP i zapisuje zawartość  
2. **ReportAgent** otrzymuje zawartość i generuje strukturalny raport  

Supervisor podjął te decyzje **autonomicznie** na podstawie zapytania użytkownika.

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
  
#### Wyjaśnienie funkcji modułu agentowego

Przykład demonstruje kilka zaawansowanych funkcji modułu agentowego. Przyjrzyjmy się bliżej zakresowi Agentic i Agent Listeners.

**Agentic Scope** pokazuje wspólną pamięć, gdzie agenci zapisali swoje wyniki za pomocą `@Agent(outputKey="...")`. Pozwala to:  
- późniejszym agentom na dostęp do wyników wcześniejszych agentów  
- Supervisorowi na syntezowanie ostatecznej odpowiedzi  
- Tobie na sprawdzenie, co wyprodukował każdy agent  

Poniższy diagram pokazuje, jak Agentic Scope działa jako wspólna pamięć w przepływie od pliku do raportu — FileAgent zapisuje wynik pod kluczem `fileContent`, ReportAgent czyta go i zapisuje własny pod `report`:

<img src="../../../translated_images/pl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope działa jako wspólna pamięć — FileAgent zapisuje `fileContent`, ReportAgent go czyta i zapisuje `report`, a Twój kod odczytuje wynik końcowy.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surowe dane pliku z FileAgent
String report = scope.readState("report");            // Strukturalny raport z ReportAgent
```
  
**Agent Listeners** umożliwiają monitorowanie i debugowanie wykonania agentów. Stopniowy output widoczny w demo pochodzi z AgentListenera, który jest podłączony do każdej invokacji agenta:  
- **beforeAgentInvocation** — wywoływane, gdy Supervisor wybiera agenta, umożliwia sprawdzenie, który agent został wybrany i dlaczego  
- **afterAgentInvocation** — wywoływane po zakończeniu pracy agenta, pokazuje jego wynik  
- **inheritedBySubagents** — jeśli true, listener monitoruje wszystkich agentów w hierarchii  

Poniższy diagram pokazuje pełen cykl życia Agent Listenerów, w tym sposób obsługi błędów przez `onError` podczas wykonywania agenta:

<img src="../../../translated_images/pl/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners są podłączone do cyklu życia wykonania — monitoruj, kiedy agenci startują, kończą lub napotykają błędy.*

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
        return true; // Propaguj do wszystkich pod-agentów
    }
};
```
  
Poza wzorcem Supervisor, moduł `langchain4j-agentic` oferuje kilka potężnych wzorców przepływu pracy. Poniższy diagram pokazuje wszystkie pięć — od prostych potoków sekwencyjnych po workflowy z zatwierdzeniem ludzkim:

<img src="../../../translated_images/pl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Pięć wzorców przepływu pracy do orkiestracji agentów — od prostych potoków sekwencyjnych po zatwierdzające workflowy z udziałem człowieka.*

| Wzorzec | Opis | Przypadek użycia |
|---------|------|------------------|
| **Sequential** | Wykonuj agentów po kolei, wynik przechodzi do następnego | Potoki: badania → analiza → raport |
| **Parallel** | Uruchamiaj agentów równocześnie | Zadania niezależne: pogoda + wiadomości + giełda |
| **Loop** | Iteruj aż spełniony warunek | Ocena jakości: dopracuj aż wynik ≥ 0,8 |
| **Conditional** | Kieruj według warunków | Klasyfikacja → przekazanie do agenta specjalisty |
| **Human-in-the-Loop** | Dodaj punkty zatwierdzenia przez człowieka | Workflowy zatwierdzania, przegląd treści |

## Kluczowe koncepcje

Po zapoznaniu się z MCP i modułem agentic w praktyce, podsumujmy, kiedy użyć którego podejścia.

Jedną z największych zalet MCP jest jego rozwijający się ekosystem. Poniższy diagram pokazuje, jak jeden uniwersalny protokół łączy Twoją aplikację AI z wieloma serwerami MCP — od dostępu do systemu plików i baz danych po GitHub, e-maile, web scraping i więcej:

<img src="../../../translated_images/pl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP tworzy ekosystem z uniwersalnym protokołem — każdy serwer zgodny z MCP działa z każdym klientem MCP, umożliwiając współdzielenie narzędzi między aplikacjami.*

**MCP** jest idealny, gdy chcesz korzystać z istniejących ekosystemów narzędzi, budować narzędzia współdzielone przez wiele aplikacji, integrować usługi firm trzecich za pomocą standardowych protokołów lub wymieniać implementacje narzędzi bez zmiany kodu.

**Moduł Agentic** sprawdza się najlepiej, gdy chcesz deklaratywnie definiować agentów z adnotacjami `@Agent`, potrzebujesz orkiestracji workflow (sekwencyjna, pętla, równoległa), preferujesz projektowanie agentów oparte na interfejsach zamiast kodzie imperatywnym lub łączysz wielu agentów wymieniających wyniki przez `outputKey`.

**Wzorzec Supervisor Agent** błyszczy gdy workflow nie jest z góry przewidywalny i chcesz, by decyzję podejmował LLM, gdy masz wielu wyspecjalizowanych agentów wymagających dynamicznej orkiestracji, gdy budujesz systemy konwersacyjne kierujące do różnych możliwości lub gdy chcesz najbardziej elastyczne, adaptacyjne zachowanie agenta.

Aby pomóc Ci zdecydować między własnymi metodami `@Tool` z Modułu 04 a narzędziami MCP z tego modułu, poniższe porównanie podkreśla kluczowe kompromisy — własne narzędzia dają ścisłe powiązanie i pełne bezpieczeństwo typów dla logiki specyficznej dla aplikacji, a narzędzia MCP oferują standaryzowane, wielokrotnego użytku integracje:

<img src="../../../translated_images/pl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Kiedy używać własnych metod @Tool, a kiedy narzędzi MCP — własne narzędzia dla logiki specyficznej z pełnym bezpieczeństwem typów, narzędzia MCP dla standaryzowanych integracji działających pomiędzy aplikacjami.*

## Gratulacje!

Przeszedłeś przez wszystkie pięć modułów kursu LangChain4j dla początkujących! Oto pełna ścieżka nauki, którą ukończyłeś — od prostego chatu aż po agentowe systemy zasilane MCP:

<img src="../../../translated_images/pl/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Twoja ścieżka nauki przez wszystkie pięć modułów — od podstawowego chatu do systemów agentowych z MCP.*

Ukończyłeś kurs LangChain4j dla początkujących. Nauczyłeś się:

- Jak budować AI konwersacyjne z pamięcią (Moduł 01)  
- Schematy inżynierii promptów dla różnych zadań (Moduł 02)  
- Umocowanie odpowiedzi w Twoich dokumentach za pomocą RAG (Moduł 03)  
- Tworzenia podstawowych agentów AI (asystentów) z własnymi narzędziami (Moduł 04)  
- Integracji standaryzowanych narzędzi z LangChain4j MCP i modułami agentowymi (Moduł 05)  

### Co dalej?

Po ukończeniu modułów zapoznaj się z [Przewodnikiem testowania](../docs/TESTING.md), aby zobaczyć koncepcje testowania LangChain4j w praktyce.

**Oficjalne zasoby:**  
- [Dokumentacja LangChain4j](https://docs.langchain4j.dev/) — kompleksowe przewodniki i referencje API  
- [LangChain4j na GitHub](https://github.com/langchain4j/langchain4j) — kod źródłowy i przykłady  
- [Samouczki LangChain4j](https://docs.langchain4j.dev/tutorials/) — poradniki krok po kroku dla różnych zastosowań  

Dziękujemy za ukończenie tego kursu!

---

**Nawigacja:** [← Poprzedni: Moduł 04 - Narzędzia](../04-tools/README.md) | [Powrót do głównego](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Ten dokument został przetłumaczony przy użyciu usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że staramy się zapewnić dokładność, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w języku źródłowym powinien być traktowany jako źródło autorytatywne. W przypadku istotnych informacji zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->