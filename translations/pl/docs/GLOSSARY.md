# Słownik LangChain4j

## Spis treści

- [Podstawowe pojęcia](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [Pojęcia AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Inżynieria promptów](../../../docs)
- [RAG (Generowanie wspomagane wyszukiwaniem)](../../../docs)
- [Agenci i narzędzia](../../../docs)
- [Moduł Agentic](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Usługi Azure](../../../docs)
- [Testowanie i rozwój](../../../docs)

Szybkie odniesienie do terminów i pojęć używanych w całym kursie.

## Podstawowe pojęcia

**AI Agent** - System wykorzystujący AI do autonomicznego rozumowania i działania. [Moduł 04](../04-tools/README.md)

**Chain** - Sekwencja operacji, gdzie wynik jest wejściem do kolejnego kroku.

**Chunking** - Dzielenie dokumentów na mniejsze fragmenty. Typowo: 300–500 tokenów z nakładką. [Moduł 03](../03-rag/README.md)

**Context Window** - Maksymalna liczba tokenów, które model może przetworzyć. GPT-5.2: 400K tokenów.

**Embeddings** - Wektory numeryczne reprezentujące znaczenie tekstu. [Moduł 03](../03-rag/README.md)

**Function Calling** - Model generuje strukturalne żądania wywołania funkcji zewnętrznych. [Moduł 04](../04-tools/README.md)

**Hallucination** - Kiedy modele generują niepoprawne, ale przekonujące informacje.

**Prompt** - Tekst wejściowy do modelu językowego. [Moduł 02](../02-prompt-engineering/README.md)

**Semantic Search** - Wyszukiwanie według znaczenia za pomocą embeddings, nie słów kluczowych. [Moduł 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: bez pamięci. Stateful: utrzymuje historię rozmowy. [Moduł 01](../01-introduction/README.md)

**Tokens** - Podstawowe jednostki tekstu, które przetwarzają modele. Wpływa na koszty i limity. [Moduł 01](../01-introduction/README.md)

**Tool Chaining** - Sekwencyjne wykonywanie narzędzi, gdzie wynik informuje kolejne wywołanie. [Moduł 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Tworzy bezpieczne typowo interfejsy usług AI.

**OpenAiOfficialChatModel** - Zunifikowany klient dla modeli OpenAI i Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Tworzy embeddings używając oficjalnego klienta OpenAI (wspiera OpenAI i Azure OpenAI).

**ChatModel** - Podstawowy interfejs dla modeli językowych.

**ChatMemory** - Utrzymuje historię rozmowy.

**ContentRetriever** - Znajduje odpowiednie fragmenty dokumentów dla RAG.

**DocumentSplitter** - Dzieli dokumenty na fragmenty.

**EmbeddingModel** - Zamienia tekst na wektory numeryczne.

**EmbeddingStore** - Przechowuje i pobiera embeddings.

**MessageWindowChatMemory** - Utrzymuje przesuwane okno ostatnich wiadomości.

**PromptTemplate** - Tworzy ponownie używalne prompt’y z miejscami na `{{zmienna}}`.

**TextSegment** - Fragment tekstu z metadanymi. Używany w RAG.

**ToolExecutionRequest** - Reprezentuje żądanie wykonania narzędzia.

**UserMessage / AiMessage / SystemMessage** - Typy wiadomości rozmowy.

## Pojęcia AI/ML

**Few-Shot Learning** - Podawanie przykładów w promptach. [Moduł 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Modele AI trenowane na ogromnych zbiorach tekstów.

**Reasoning Effort** - Parametr GPT-5.2 kontrolujący głębokość rozumowania. [Moduł 02](../02-prompt-engineering/README.md)

**Temperature** - Kontroluje losowość outputu. Niskie=deterministyczne, wysokie=kreatywne.

**Vector Database** - Specjalna baza danych dla embeddings. [Moduł 03](../03-rag/README.md)

**Zero-Shot Learning** - Wykonywanie zadań bez przykładów. [Moduł 02](../02-prompt-engineering/README.md)

## Guardrails - [Moduł 00](../00-quick-start/README.md)

**Defense in Depth** - Wielowarstwowe podejście bezpieczeństwa łączące guardrails na poziomie aplikacji z filtrami bezpieczeństwa dostawcy.

**Hard Block** - Dostawca zgłasza błąd HTTP 400 przy poważnych naruszeniach treści.

**InputGuardrail** - Interfejs LangChain4j do walidacji wejścia użytkownika przed dotarciem do LLM. Oszczędza koszt i opóźnienia blokując szkodliwe prompt’y wcześnie.

**InputGuardrailResult** - Typ zwracany walidacji guardrail: `success()` lub `fatal("przyczyna")`.

**OutputGuardrail** - Interfejs do walidacji odpowiedzi AI przed zwrotem użytkownikowi.

**Provider Safety Filters** - Wbudowane filtry treści od dostawców AI (np. GitHub Models) wykrywające naruszenia na poziomie API.

**Soft Refusal** - Model grzecznie odmawia odpowiedzi bez zgłaszania błędu.

## Inżynieria promptów - [Moduł 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Rozumowanie krok po kroku dla lepszej dokładności.

**Constrained Output** - Wymuszanie określonego formatu lub struktury.

**High Eagerness** - Wzorzec GPT-5.2 na dokładne rozumowanie.

**Low Eagerness** - Wzorzec GPT-5.2 na szybkie odpowiedzi.

**Multi-Turn Conversation** - Utrzymywanie kontekstu przez wiele wymian.

**Role-Based Prompting** - Ustawianie persony modelu przez wiadomości systemowe.

**Self-Reflection** - Model ocenia i poprawia swój output.

**Structured Analysis** - Stały schemat ewaluacji.

**Task Execution Pattern** - Plan → Wykonaj → Podsumuj.

## RAG (Generowanie wspomagane wyszukiwaniem) - [Moduł 03](../03-rag/README.md)

**Document Processing Pipeline** - Ładowanie → dzielenie → osadzanie → przechowywanie.

**In-Memory Embedding Store** - Niezachowująca historii pamięć do testów.

**RAG** - Łączy wyszukiwanie z generowaniem dla ugruntowania odpowiedzi.

**Similarity Score** - Miara (0-1) podobieństwa semantycznego.

**Source Reference** - Metadane o pozyskanej treści.

## Agenci i narzędzia - [Moduł 04](../04-tools/README.md)

**@Tool Annotation** - Oznacza metody Java jako narzędzia wywoływane przez AI.

**ReAct Pattern** - Rozumuj → Działaj → Obserwuj → Powtarzaj.

**Session Management** - Oddzielne konteksty dla różnych użytkowników.

**Tool** - Funkcja, którą agent AI może wywołać.

**Tool Description** - Dokumentacja celu i parametrów narzędzia.

## Moduł Agentic - [Moduł 05](../05-mcp/README.md)

**@Agent Annotation** - Oznacza interfejsy jako agentów AI z deklaratywną definicją zachowania.

**Agent Listener** - Hak do monitorowania wykonania agenta przez `beforeAgentInvocation()` i `afterAgentInvocation()`.

**Agentic Scope** - Wspólna pamięć, gdzie agenci przechowują wyniki pod `outputKey` do wykorzystania przez downstream.

**AgenticServices** - Fabryka do tworzenia agentów przez `agentBuilder()` i `supervisorBuilder()`.

**Conditional Workflow** - Trasowanie na różne specjalistyczne agenty na podstawie warunków.

**Human-in-the-Loop** - Wzorzec workflow dodający punkty kontrolne człowieka do zatwierdzania lub weryfikacji treści.

**langchain4j-agentic** - Zależność Maven dla deklaratywnego budowania agentów (eksperymentalne).

**Loop Workflow** - Iteracja wykonania agenta aż do spełnienia warunku (np. wynik jakości ≥ 0.8).

**outputKey** - Parametr adnotacji agenta określający, gdzie w Agentic Scope są zapisywane wyniki.

**Parallel Workflow** - Równoległe uruchomienie wielu agentów do niezależnych zadań.

**Response Strategy** - Sposób, w jaki supervisor formułuje odpowiedź końcową: LAST, SUMMARY lub SCORED.

**Sequential Workflow** - Wykonywanie agentów po kolei, gdzie output przekazywany jest do kolejnego kroku.

**Supervisor Agent Pattern** - Zaawansowany wzorzec agentic, gdzie supervisor LLM dynamicznie decyduje, których sub-agentów wywołać.

## Model Context Protocol (MCP) - [Moduł 05](../05-mcp/README.md)

**langchain4j-mcp** - Zależność Maven dla integracji MCP w LangChain4j.

**MCP** - Model Context Protocol: standard łączący aplikacje AI z zewnętrznymi narzędziami. Buduj raz, używaj wszędzie.

**MCP Client** - Aplikacja łącząca się z MCP serwerami, by odkrywać i używać narzędzi.

**MCP Server** - Usługa udostępniająca narzędzia przez MCP z jasnym opisem i schematami parametrów.

**McpToolProvider** - Komponent LangChain4j opakowujący MCP tools do użycia w usługach AI i agentach.

**McpTransport** - Interfejs komunikacji MCP. Implementacje: Stdio i HTTP.

**Stdio Transport** - Transport procesu lokalnego przez stdin/stdout. Przydatny do dostępu do systemu plików lub narzędzi CLI.

**StdioMcpTransport** - Implementacja LangChain4j uruchamiająca serwer MCP jako proces podrzędny.

**Tool Discovery** - Klient pyta serwer o dostępne narzędzia z opisami i schematami.

## Usługi Azure - [Moduł 01](../01-introduction/README.md)

**Azure AI Search** - Chmurowe wyszukiwanie z możliwością wektorową. [Moduł 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Wdrażanie zasobów Azure.

**Azure OpenAI** - Usługa AI Microsoft dla przedsiębiorstw.

**Bicep** - Język infrastruktury jako kod dla Azure. [Przewodnik infrastruktury](../01-introduction/infra/README.md)

**Deployment Name** - Nazwa wdrożenia modelu w Azure.

**GPT-5.2** - Najnowszy model OpenAI z kontrolą rozumowania. [Moduł 02](../02-prompt-engineering/README.md)

## Testowanie i rozwój - [Przewodnik testowania](TESTING.md)

**Dev Container** - Konteneryzowane środowisko programistyczne. [Konfiguracja](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Darmowy plac zabaw dla modeli AI. [Moduł 00](../00-quick-start/README.md)

**In-Memory Testing** - Testy z pamięcią tymczasową.

**Integration Testing** - Testowanie z rzeczywistą infrastrukturą.

**Maven** - Narzędzie automatyzacji budowy Java.

**Mockito** - Framework do mockowania w Javie.

**Spring Boot** - Framework aplikacyjny Java. [Moduł 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:  
Niniejszy dokument został przetłumaczony przy użyciu usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mimo że dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym powinien być traktowany jako autorytatywne źródło. W przypadku informacji o kluczowym znaczeniu zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->