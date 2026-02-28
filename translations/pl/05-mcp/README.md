# Moduł 05: Protokół Kontekstu Modelu (MCP)

## Spis treści

- [Czego się nauczysz](../../../05-mcp)
- [Co to jest MCP?](../../../05-mcp)
- [Jak działa MCP](../../../05-mcp)
- [Moduł agentowy](../../../05-mcp)
- [Uruchamianie przykładów](../../../05-mcp)
  - [Wymagania wstępne](../../../05-mcp)
- [Szybki start](../../../05-mcp)
  - [Operacje na plikach (Stdio)](../../../05-mcp)
  - [Agent nadzorujący](../../../05-mcp)
    - [Uruchamianie demo](../../../05-mcp)
    - [Jak działa Supervisor](../../../05-mcp)
    - [Strategie odpowiedzi](../../../05-mcp)
    - [Zrozumienie wyników](../../../05-mcp)
    - [Wyjaśnienie funkcji modułu agentowego](../../../05-mcp)
- [Kluczowe pojęcia](../../../05-mcp)
- [Gratulacje!](../../../05-mcp)
  - [Co dalej?](../../../05-mcp)

## Czego się nauczysz

Zbudowałeś konwersacyjne AI, opanowałeś promptowanie, osadziłeś odpowiedzi w dokumentach i stworzyłeś agentów z narzędziami. Ale wszystkie te narzędzia były specjalnie tworzone dla Twojej konkretnej aplikacji. A co, gdybyś mógł dać swojemu AI dostęp do standardowego ekosystemu narzędzi, które każdy może tworzyć i udostępniać? W tym module nauczysz się właśnie tego, korzystając z Protokół Kontekstu Modelu (MCP) oraz modułu agentowego LangChain4j. Najpierw pokażemy prosty czytnik plików MCP, a następnie jak łatwo go zintegrować z zaawansowanymi przepływami agentowymi używając wzorca Agenta Nadzorującego.

## Co to jest MCP?

Protokół Kontekstu Modelu (MCP) zapewnia dokładnie to - standardowy sposób, aby aplikacje AI mogły odkrywać i korzystać z zewnętrznych narzędzi. Zamiast pisać niestandardowe integracje dla każdego źródła danych czy usługi, łączysz się z serwerami MCP, które udostępniają swoje możliwości w spójnym formacie. Twój agent AI może automatycznie odkrywać i używać tych narzędzi.

<img src="../../../translated_images/pl/mcp-comparison.9129a881ecf10ff5.webp" alt="Porównanie MCP" width="800"/>

*Przed MCP: złożone integracje punkt-punkt. Po MCP: jeden protokół, nieskończone możliwości.*

MCP rozwiązuje podstawowy problem w rozwoju AI: każda integracja jest niestandardowa. Chcesz mieć dostęp do GitHub? Niestandardowy kod. Chcesz czytać pliki? Niestandardowy kod. Chcesz zapytać bazę danych? Niestandardowy kod. I żadna z tych integracji nie działa z innymi aplikacjami AI.

MCP to ujednolica. Serwer MCP udostępnia narzędzia z jasnym opisem i schematami. Każdy klient MCP może się połączyć, odkryć dostępne narzędzia i z nich korzystać. Zbuduj raz, używaj wszędzie.

<img src="../../../translated_images/pl/mcp-architecture.b3156d787a4ceac9.webp" alt="Architektura MCP" width="800"/>

*Architektura Protokół Kontekstu Modelu – standardowe odkrywanie i wykonywanie narzędzi*

## Jak działa MCP

<img src="../../../translated_images/pl/mcp-protocol-detail.01204e056f45308b.webp" alt="Szczegóły protokołu MCP" width="800"/>

*Jak działa MCP w tle — klienci odkrywają narzędzia, wymieniają wiadomości JSON-RPC i wykonują operacje przez warstwę transportu.*

**Architektura serwer-klient**

MCP korzysta z modelu klient-serwer. Serwery dostarczają narzędzia - czytanie plików, zapytywanie baz danych, wywoływanie API. Klienci (Twoja aplikacja AI) łączą się z serwerami i korzystają z ich narzędzi.

Aby korzystać z MCP z LangChain4j, dodaj tę zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Odkrywanie narzędzi**

Gdy klient łączy się z serwerem MCP, pyta "Jakie narzędzia posiadasz?" Serwer odpowiada listą dostępnych narzędzi, każde z opisem i schematami parametrów. Twój agent AI może wtedy zdecydować, których narzędzi użyć na podstawie zapytań użytkownika.

<img src="../../../translated_images/pl/tool-discovery.07760a8a301a7832.webp" alt="Odkrywanie narzędzi MCP" width="800"/>

*AI odkrywa dostępne narzędzia podczas uruchamiania — teraz wie, jakie możliwości są dostępne i może wybrać, których użyć.*

**Mechanizmy transportu**

MCP wspiera różne mechanizmy transportu. Ten moduł demonstruje transport Stdio dla procesów lokalnych:

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

<img src="../../../translated_images/pl/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Przepływ transportu Stdio" width="800"/>

*Transport Stdio w akcji — aplikacja uruchamia serwer MCP jako podproces i komunikuje się przez potoki stdin/stdout.*

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) i zapytaj:
> - "Jak działa transport Stdio i kiedy powinienem go używać zamiast HTTP?"
> - "Jak LangChain4j zarządza cyklem życia uruchamianych procesów serwerów MCP?"
> - "Jakie są konsekwencje bezpieczeństwa przy udzielaniu AI dostępu do systemu plików?"

## Moduł agentowy

Podczas gdy MCP dostarcza standardyzowane narzędzia, moduł **agentowy** LangChain4j umożliwia deklaratywne budowanie agentów, którzy orkiestrują te narzędzia. Adnotacja `@Agent` i `AgenticServices` pozwalają definiować zachowanie agenta poprzez interfejsy zamiast kodu imperatywnego.

W tym module poznasz wzorzec **Agenta Nadzorującego** — zaawansowane podejście agentowe, w którym agent „nadzorujący” dynamicznie decyduje, których pod-agentów wywołać na podstawie żądań użytkownika. Połączymy oba koncepty, dając jednemu z naszych pod-agentów możliwość dostępu do plików przez MCP.

Aby korzystać z modułu agentowego, dodaj tę zależność Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Eksperymentalne:** Moduł `langchain4j-agentic` jest **eksperymentalny** i może ulec zmianie. Stabilnym sposobem budowy asystentów AI pozostaje `langchain4j-core` z własnymi narzędziami (Moduł 04).

## Uruchamianie przykładów

### Wymagania wstępne

- Java 21+, Maven 3.9+
- Node.js 16+ i npm (dla serwerów MCP)
- Zmienna środowiskowa skonfigurowana w pliku `.env` (w katalogu głównym):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (tak jak w modułach 01-04)

> **Uwaga:** Jeśli jeszcze nie ustawiłeś zmiennych środowiskowych, zobacz [Moduł 00 - Szybki start](../00-quick-start/README.md) po instrukcje lub skopiuj `.env.example` do `.env` w katalogu głównym i wypełnij wartości.

## Szybki start

**Korzystając z VS Code:** Po prostu kliknij prawym przyciskiem myszy dowolny plik demo w Eksploratorze i wybierz **"Run Java"** lub użyj konfiguracji uruchamiania z panelu Uruchom i debuguj (upewnij się, że dodałeś swój token do pliku `.env`).

**Korzystając z Maven:** Alternatywnie możesz uruchomić z linii poleceń za pomocą poniższych przykładów.

### Operacje na plikach (Stdio)

Demonstruje lokalne narzędzia oparte na podprocesach.

**✅ Brak wymagań wstępnych** - serwer MCP jest uruchamiany automatycznie.

**Korzystanie ze skryptów startowych (zalecane):**

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

**Korzystając z VS Code:** Kliknij prawym przyciskiem na `StdioTransportDemo.java` i wybierz **"Run Java"** (upewnij się, że plik `.env` jest skonfigurowany).

Aplikacja automatycznie uruchamia serwer MCP systemu plików i czyta lokalny plik. Zauważ, jak zarządzanie podprocesem jest realizowane za Ciebie.

**Oczekiwane wyjście:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agent nadzorujący

Wzorzec **Agenta Nadzorującego** to **elastyczna** forma agentowego AI. Nadzorca używa LLM, aby autonomicznie zdecydować, których agentów wywołać na podstawie żądania użytkownika. W następnym przykładzie połączymy dostęp do plików MCP z agentem LLM, tworząc nadzorowany przepływ „czytania pliku → raport”.

W demo, `FileAgent` czyta plik za pomocą narzędzi MCP systemu plików, a `ReportAgent` generuje strukturalny raport z podsumowaniem (1 zdanie), 3 kluczowymi punktami i rekomendacjami. Nadzorca automatycznie orkiestruje ten przepływ:

<img src="../../../translated_images/pl/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Wzorzec Agenta Nadzorującego" width="800"/>

*Nadzorca używa swojego LLM, aby zdecydować, których agentów wywołać i w jakiej kolejności — bez potrzeby sztywnego trasowania.*

Tak wygląda konkretny przepływ dla naszego procesu od pliku do raportu:

<img src="../../../translated_images/pl/file-report-workflow.649bb7a896800de9.webp" alt="Przepływ plik do raportu" width="800"/>

*FileAgent odczytuje plik przez narzędzia MCP, potem ReportAgent przekształca surową zawartość w strukturalny raport.*

Każdy agent zapisuje swoje wyjście w **Agentic Scope** (wspólna pamięć), co pozwala agentom dalszego przetwarzania na dostęp do wcześniejszych wyników. To pokazuje, jak narzędzia MCP integrują się bezproblemowo z przepływami agentowymi — Nadzorca nie musi wiedzieć *jak* pliki są czytane, tylko że `FileAgent` może to zrobić.

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

**Korzystając z VS Code:** Kliknij prawym na `SupervisorAgentDemo.java` i wybierz **"Run Java"** (upewnij się, że plik `.env` jest skonfigurowany).

#### Jak działa Supervisor

```java
// Krok 1: FileAgent odczytuje pliki za pomocą narzędzi MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Posiada narzędzia MCP do operacji na plikach
        .build();

// Krok 2: ReportAgent generuje strukturalne raporty
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor zarządza przepływem pracy plik → raport
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Zwróć końcowy raport
        .build();

// Supervisor decyduje, których agentów wywołać na podstawie żądania
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Strategie odpowiedzi

Konfigurując `SupervisorAgent`, określasz, jak ma sformułować ostateczną odpowiedź użytkownikowi po wykonaniu zadań przez pod-agentów.

<img src="../../../translated_images/pl/response-strategies.3d0cea19d096bdf9.webp" alt="Strategie odpowiedzi" width="800"/>

*Trzy strategie formułowania odpowiedzi przez Supervisor — wybierz, czy chcesz wyjście ostatniego agenta, syntetyczne podsumowanie, czy najlepszy wynik.*

Dostępne strategie to:

| Strategia | Opis |
|----------|-------------|
| **LAST** | Supervisor zwraca wynik ostatniego wywołanego pod-agenta lub narzędzia. Przydaje się, gdy ostatni agent w przepływie jest zaprojektowany na ostateczną, kompletną odpowiedź (np. „Agent Podsumowujący” w pipeline badań). |
| **SUMMARY** | Supervisor używa własnego modelu językowego (LLM), aby syntetyzować podsumowanie całej interakcji i odpowiedzi pod-agentów, a następnie zwraca to podsumowanie jako odpowiedź końcową. Zapewnia czystą, zagregowaną odpowiedź dla użytkownika. |
| **SCORED** | System stosuje wewnętrzny LLM do oceny zarówno ostatniej odpowiedzi (LAST), jak i podsumowania (SUMMARY) w kontekście oryginalnego żądania, zwracając wynik z wyższą oceną. |

Zobacz [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dla pełnej implementacji.

> **🤖 Wypróbuj z [GitHub Copilot](https://github.com/features/copilot) Chat:** Otwórz [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) i zapytaj:
> - "Jak Supervisor decyduje, których agentów wywołać?"
> - "Jaka jest różnica między wzorcem Supervisor a wzorcem Sequential workflow?"
> - "Jak mogę dostosować zachowanie planowania Supervisor?"

#### Zrozumienie wyników

Po uruchomieniu demo zobaczysz uporządkowany przewodnik, jak Supervisor orkiestruje wielu agentów. Oto co oznacza każda sekcja:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Nagłówek** przedstawia koncepcję przepływu – ukierunkowany pipeline od czytania pliku do generowania raportu.

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

**Diagram przepływu** pokazuje przepływ danych między agentami. Każdy agent ma konkretną rolę:
- **FileAgent** czyta pliki za pomocą narzędzi MCP i zapisuje surową zawartość w `fileContent`
- **ReportAgent** korzysta z tej zawartości i tworzy strukturalny raport w `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Żądanie użytkownika** pokazuje zadanie. Supervisor analizuje to i decyduje się wywołać FileAgent → ReportAgent.

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

**Orkiestracja Supervisor** pokazuje 2-etapowy przepływ w praktyce:
1. **FileAgent** odczytuje plik przez MCP i zapisuje zawartość
2. **ReportAgent** otrzymuje zawartość i generuje raport strukturalny

Supervisor podjął te decyzje **samodzielnie** na podstawie żądania użytkownika.

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

Przykład demonstruje kilka zaawansowanych funkcji modułu agentowego. Przyjrzyjmy się bliżej Agentic Scope i Agent Listeners.

**Agentic Scope** pokazuje wspólną pamięć, gdzie agenci zapisywali swoje wyniki używając `@Agent(outputKey="...")`. To pozwala:
- Późniejszym agentom na dostęp do wyników wcześniejszych agentów
- Supervisorowi na syntetyzowanie ostatecznej odpowiedzi
- Tobie na inspekcję, co każdy agent wyprodukował

<img src="../../../translated_images/pl/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Wspólna Pamięć" width="800"/>

*Agentic Scope pełni rolę pamięci współdzielonej — FileAgent zapisuje `fileContent`, ReportAgent odczytuje ją i zapisuje `report`, a Twój kod odczytuje wynik końcowy.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Surowe dane pliku od FileAgent
String report = scope.readState("report");            // Strukturalny raport od ReportAgent
```

**Agent Listeners** umożliwiają monitorowanie i debugowanie wykonania agentów. Krok po kroku wyjście, które widzisz w demo, pochodzi ze słuchacza agenta, który jest przyłączony do każdego wywołania agenta:
- **beforeAgentInvocation** - Wywoływane, gdy Supervisor wybiera agenta, pozwalając zobaczyć, który agent został wybrany i dlaczego  
- **afterAgentInvocation** - Wywoływane, gdy agent kończy działanie, pokazując jego wynik  
- **inheritedBySubagents** - Gdy prawda, słuchacz monitoruje wszystkich agentów w hierarchii  

<img src="../../../translated_images/pl/agent-listeners.784bfc403c80ea13.webp" alt="Cykl życia słuchaczy agentów" width="800"/>

*Słuchacze agentów łączą się z cyklem życia wykonania — monitorują, kiedy agenci startują, kończą lub napotykają błędy.*

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
        return true; // Rozpropaguj do wszystkich podagentów
    }
};
```
  
Poza wzorcem Supervisor, moduł `langchain4j-agentic` oferuje kilka potężnych wzorców i funkcji workflow:

<img src="../../../translated_images/pl/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Wzorce workflow agentów" width="800"/>

*Pięć wzorców workflow do orkiestracji agentów — od prostych sekwencyjnych potoków po zatwierdzanie z udziałem człowieka.*

| Wzorzec | Opis | Przypadek użycia |
|---------|-------|------------------|
| **Sekwencyjny** | Wykonuj agentów w kolejności, wyjście przepływa do następnego | Potoki: badanie → analiza → raport |
| **Równoległy** | Uruchamiaj agentów jednocześnie | Niezależne zadania: pogoda + wiadomości + giełda |
| **Pętla** | Iteruj do spełnienia warunku | Ocena jakości: dopóki wynik < 0.8, poprawiaj |
| **Warunkowy** | Kieruj w oparciu o warunki | Klasyfikuj → skieruj do specjalistycznego agenta |
| **Człowiek w pętli** | Dodaj punkty kontrolne dla człowieka | Workflow zatwierdzania, przegląd treści |

## Kluczowe pojęcia

Skoro już zapoznałeś się z MCP i modułem agentic w praktyce, podsumujmy, kiedy stosować każde podejście.

<img src="../../../translated_images/pl/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ekosystem MCP" width="800"/>

*MCP tworzy uniwersalny ekosystem protokołów — dowolny serwer kompatybilny z MCP współpracuje z dowolnym klientem MCP, umożliwiając współdzielenie narzędzi między aplikacjami.*

**MCP** jest idealne, gdy chcesz wykorzystać istniejące ekosystemy narzędzi, budować narzędzia współdzielone przez wiele aplikacji, integrować usługi zewnętrzne ze standardowymi protokołami lub wymieniać implementacje narzędzi bez zmiany kodu.

**Moduł Agentic** najlepiej sprawdza się, gdy chcesz deklaratywnych definicji agentów z użyciem adnotacji `@Agent`, potrzebujesz orkiestracji workflow (sekwencja, pętla, równoległość), preferujesz projektowanie agentów przez interfejsy zamiast kodu imperatywnego lub łączysz wielu agentów, którzy dzielą dane wyjściowe przez `outputKey`.

**Wzorzec agenta Supervisor** błyszczy, gdy workflow nie jest z góry przewidywalny i chcesz, aby LLM decydowało, gdy masz wielu wyspecjalizowanych agentów wymagających dynamicznej orkiestracji, przy budowie systemów konwersacyjnych kierujących do różnych możliwości lub gdy chcesz najbardziej elastyczne, adaptacyjne zachowanie agenta.

<img src="../../../translated_images/pl/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Narzędzia niestandardowe vs narzędzia MCP" width="800"/>

*Kiedy używać niestandardowych metod @Tool a kiedy narzędzi MCP — niestandardowe narzędzia do logiki specyficznej dla aplikacji z pełnym bezpieczeństwem typów, narzędzia MCP do ustandaryzowanych integracji działających w wielu aplikacjach.*

## Gratulacje!

<img src="../../../translated_images/pl/course-completion.48cd201f60ac7570.webp" alt="Ukończenie kursu" width="800"/>

*Twoja podróż edukacyjna przez wszystkie pięć modułów — od podstawowego chatu do systemów agentic opartych na MCP.*

Ukończyłeś kurs LangChain4j dla początkujących. Nauczyłeś się:

- Jak budować konwersacyjne AI z pamięcią (Moduł 01)  
- Wzorce inżynierii promptów dla różnych zadań (Moduł 02)  
- Jak opierać odpowiedzi na twoich dokumentach z RAG (Moduł 03)  
- Tworzyć podstawowych agentów AI (asystentów) z niestandardowymi narzędziami (Moduł 04)  
- Integracji ustandaryzowanych narzędzi z modułami LangChain4j MCP i Agentic (Moduł 05)  

### Co dalej?

Po ukończeniu modułów zapoznaj się z [Testing Guide](../docs/TESTING.md), aby zobaczyć koncepcje testowania LangChain4j w praktyce.

**Oficjalne zasoby:**  
- [Dokumentacja LangChain4j](https://docs.langchain4j.dev/) - Kompleksowe przewodniki i referencje API  
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - Kod źródłowy i przykłady  
- [Samouczki LangChain4j](https://docs.langchain4j.dev/tutorials/) - Samouczki krok po kroku dla różnych zastosowań  

Dziękujemy za ukończenie tego kursu!

---

**Nawigacja:** [← Poprzedni: Moduł 04 - Narzędzia](../04-tools/README.md) | [Powrót do głównego](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Wyłączenie odpowiedzialności**:  
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczeń AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dążymy do jak największej dokładności, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uważany za źródło wiarygodne. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->