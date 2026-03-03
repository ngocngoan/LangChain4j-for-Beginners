# Słownik LangChain4j

## Spis treści

- [Podstawowe pojęcia](../../../docs)
- [Komponenty LangChain4j](../../../docs)
- [Pojęcia AI/ML](../../../docs)
- [Zabezpieczenia](../../../docs)
- [Inżynieria promptów](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenci i narzędzia](../../../docs)
- [Moduł Agentic](../../../docs)
- [Protokół kontekstu modelu (MCP)](../../../docs)
- [Usługi Azure](../../../docs)
- [Testowanie i rozwój](../../../docs)

Szybkie odniesienie do terminów i pojęć używanych w całym kursie.

## Podstawowe pojęcia

**Agent AI** - System wykorzystujący AI do autonomicznego rozumowania i działania. [Moduł 04](../04-tools/README.md)

**Chain** - Sekwencja operacji, gdzie wyjście jest podawane do następnego kroku.

**Chunking** - Dzielenie dokumentów na mniejsze fragmenty. Typowe: 300-500 tokenów z nakładką. [Moduł 03](../03-rag/README.md)

**Okno kontekstu** - Maksymalna liczba tokenów, które model może przetworzyć. GPT-5.2: 400 tys. tokenów (do 272 tys. na wejściu, 128 tys. na wyjściu).

**Wektory osadzeń (embeddings)** - Numeryczne wektory reprezentujące znaczenie tekstu. [Moduł 03](../03-rag/README.md)

**Wywołanie funkcji** - Model generuje ustrukturyzowane żądania do wywołania funkcji zewnętrznych. [Moduł 04](../04-tools/README.md)

**Halucynacje** - Gdy modele generują niepoprawne, ale wiarygodne informacje.

**Prompt** - Tekst wejściowy do modelu językowego. [Moduł 02](../02-prompt-engineering/README.md)

**Wyszukiwanie semantyczne** - Wyszukiwanie według znaczenia z użyciem embeddings, a nie słów kluczowych. [Moduł 03](../03-rag/README.md)

**Stanowy vs bezstanowy** - Bezstanowy: brak pamięci. Stanowy: utrzymuje historię rozmowy. [Moduł 01](../01-introduction/README.md)

**Tokeny** - Podstawowe jednostki tekstu przetwarzane przez modele. Wpływa na koszty i limity. [Moduł 01](../01-introduction/README.md)

**Łańcuchowanie narzędzi** - Wykonywanie narzędzi po kolei, gdzie wyjście informuje kolejne wywołanie. [Moduł 04](../04-tools/README.md)

## Komponenty LangChain4j

**AiServices** - Tworzy bezpieczne typowo interfejsy usług AI.

**OpenAiOfficialChatModel** - Zunifikowany klient dla modeli OpenAI i Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Tworzy embeddings z użyciem oficjalnego klienta OpenAI (obsługuje OpenAI i Azure OpenAI).

**ChatModel** - Główny interfejs modeli językowych.

**ChatMemory** - Utrzymuje historię rozmowy.

**ContentRetriever** - Znajduje istotne fragmenty dokumentów do RAG.

**DocumentSplitter** - Dzieli dokumenty na fragmenty.

**EmbeddingModel** - Konwertuje tekst na numeryczne wektory.

**EmbeddingStore** - Przechowuje i pobiera embeddings.

**MessageWindowChatMemory** - Utrzymuje przesuwane okno ostatnich wiadomości.

**PromptTemplate** - Tworzy wielokrotnego użytku prompt z miejscami na zmienne `{{variable}}`.

**TextSegment** - Fragment tekstu z metadanymi. Używany w RAG.

**ToolExecutionRequest** - Reprezentuje żądanie wykonania narzędzia.

**UserMessage / AiMessage / SystemMessage** - Typy wiadomości konwersacji.

## Pojęcia AI/ML

**Uczenie Few-Shot** - Dostarczanie przykładów w promptach. [Moduł 02](../02-prompt-engineering/README.md)

**Duży Model Językowy (LLM)** - Modele AI trenowane na ogromnych zbiorach tekstów.

**Wysiłek rozumowania** - Parametr GPT-5.2 kontrolujący głębokość myślenia. [Moduł 02](../02-prompt-engineering/README.md)

**Temperatura** - Kontroluje losowość wyjścia. Niska=deterministyczne, wysoka=kreatywne.

**Baza danych wektorowych** - Specjalistyczna baza dla embeddings. [Moduł 03](../03-rag/README.md)

**Uczenie Zero-Shot** - Wykonywanie zadań bez przykładów. [Moduł 02](../02-prompt-engineering/README.md)

## Zabezpieczenia - [Moduł 00](../00-quick-start/README.md)

**Obrona w głębokości** - Wielowarstwowe podejście do bezpieczeństwa łączące zabezpieczenia na poziomie aplikacji z filtrami bezpieczeństwa dostawcy.

**Twardy blok** - Dostawca zwraca błąd HTTP 400 przy poważnych naruszeniach treści.

**InputGuardrail** - Interfejs LangChain4j do walidacji danych wejściowych użytkownika zanim dotrą do LLM. Oszczędza koszty i latencję blokując szkodliwe prompt-y na wczesnym etapie.

**InputGuardrailResult** - Typ zwracany walidacji guardrail: `success()` lub `fatal("powód")`.

**OutputGuardrail** - Interfejs do walidacji odpowiedzi AI przed zwróceniem użytkownikom.

**Filtry bezpieczeństwa dostawcy** - Wbudowane filtry treści od dostawców AI (np. GitHub Models) wykrywające naruszenia na poziomie API.

**Miękka odmowa** - Model grzecznie odmawia odpowiedzi bez wyrzucania błędu.

## Inżynieria promptów - [Moduł 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Rozumowanie krok po kroku dla lepszej dokładności.

**Wymuszone wyjście** - Wymuszanie konkretnego formatu lub struktury.

**Wysoka wnikliwość** - Wzorzec GPT-5.2 dla dokładnego rozumowania.

**Niska wnikliwość** - Wzorzec GPT-5.2 dla szybkich odpowiedzi.

**Konwersacja wieloetapowa** - Utrzymywanie kontekstu przez wymiany.

**Promptowanie oparte na roli** - Ustawianie persony modelu za pomocą komunikatów systemowych.

**Autoanaliza** - Model ocenia i poprawia swoje wyjście.

**Strukturalna analiza** - Stały szablon ewaluacji.

**Wzorzec wykonania zadania** - Planowanie → Wykonanie → Podsumowanie.

## RAG (Retrieval-Augmented Generation) - [Moduł 03](../03-rag/README.md)

**Proces przetwarzania dokumentów** - Ładuj → dziel na fragmenty → osadź → przechowuj.

**In-memory Embedding Store** - Pamięć tymczasowa do testów.

**RAG** - Łączy wyszukiwanie z generowaniem by ugruntować odpowiedzi.

**Współczynnik podobieństwa** - Miarę (0-1) podobieństwa semantycznego.

**Odniesienie źródłowe** - Metadane o pobranej zawartości.

## Agenci i narzędzia - [Moduł 04](../04-tools/README.md)

**Adnotacja @Tool** - Oznacza metody Java jako narzędzia wywoływane przez AI.

**Wzorzec ReAct** - Rozumuj → Działaj → Obserwuj → Powtórz.

**Zarządzanie sesją** - Oddzielne konteksty dla różnych użytkowników.

**Narzędzie (Tool)** - Funkcja, którą agent AI może wywołać.

**Opis narzędzia** - Dokumentacja celu i parametrów narzędzia.

## Moduł Agentic - [Moduł 05](../05-mcp/README.md)

**Adnotacja @Agent** - Oznacza interfejsy jako agentów AI z deklaratywną definicją zachowania.

**Agent Listener** - Hook do monitorowania wykonania agenta przez `beforeAgentInvocation()` i `afterAgentInvocation()`.

**Zakres Agentic** - Wspólna pamięć, w której agenci przechowują wyniki używając `outputKey` do konsumpcji przez innych agentów.

**AgenticServices** - Fabryka do tworzenia agentów za pomocą `agentBuilder()` i `supervisorBuilder()`.

**Przepływ warunkowy** - Kierowanie według warunków do różnych agentów specjalistycznych.

**Człowiek w pętli (Human-in-the-Loop)** - Wzorzec workflow dodający punkt kontrolny człowieka do zatwierdzenia lub przeglądu treści.

**langchain4j-agentic** - Zależność Maven do deklaratywnego budowania agentów (eksperymentalne).

**Przepływ pętli (Loop Workflow)** - Iteracja wykonywania agenta aż do spełnienia warunku (np. ocena jakości ≥ 0.8).

**outputKey** - Parametr adnotacji agenta określający miejsce przechowywania wyników w Zakresie Agentic.

**Przepływ równoległy** - Uruchamianie wielu agentów jednocześnie do niezależnych zadań.

**Strategia odpowiedzi** - Sposób formułowania ostatecznej odpowiedzi przez nadzorcę: LAST, SUMMARY lub SCORED.

**Przepływ sekwencyjny** - Wykonywanie agentów kolejno, gdzie wyjście przechodzi do następnego kroku.

**Wzorzec agenta nadzorującego (Supervisor Agent Pattern)** - Zaawansowany wzorzec agentic, gdzie nadzorca LLM dynamicznie decyduje, których podagentów wywołać.

## Protokół kontekstu modelu (MCP) - [Moduł 05](../05-mcp/README.md)

**langchain4j-mcp** - Zależność Maven do integracji MCP w LangChain4j.

**MCP** - Model Context Protocol: standard łączenia aplikacji AI z narzędziami zewnętrznymi. Zbuduj raz, używaj wszędzie.

**MCP Client** - Aplikacja łącząca się z serwerem MCP do odkrywania i używania narzędzi.

**MCP Server** - Usługa udostępniająca narzędzia przez MCP z jasnymi opisami i schematami parametrów.

**McpToolProvider** - Komponent LangChain4j owija narzędzia MCP do użycia w usługach i agentach AI.

**McpTransport** - Interfejs komunikacji MCP. Implementacje to Stdio i HTTP.

**Stdio Transport** - Lokalny transport procesu przez stdin/stdout. Przydatny do dostępu do systemu plików lub narzędzi CLI.

**StdioMcpTransport** - Implementacja LangChain4j uruchamiająca serwer MCP jako podproces.

**Odkrywanie narzędzi** - Klient pyta serwer o dostępne narzędzia z opisami i schematami.

## Usługi Azure - [Moduł 01](../01-introduction/README.md)

**Azure AI Search** - Chmurowe wyszukiwanie z możliwością wektorową. [Moduł 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Rozmieszcza zasoby Azure.

**Azure OpenAI** - Usługa AI klasy enterprise Microsoft.

**Bicep** - Język infrastruktury jako kod Azure. [Przewodnik infrastruktury](../01-introduction/infra/README.md)

**Nazwa wdrożenia** - Nazwa wdrożenia modelu w Azure.

**GPT-5.2** - Najnowszy model OpenAI z kontrolą rozumowania. [Moduł 02](../02-prompt-engineering/README.md)

## Testowanie i rozwój - [Przewodnik testowania](TESTING.md)

**Dev Container** - Konteneryzowane środowisko deweloperskie. [Konfiguracja](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Darmowy plac zabaw AI. [Moduł 00](../00-quick-start/README.md)

**Testowanie in-memory** - Testowanie z pamięcią tymczasową.

**Testy integracyjne** - Testowanie z prawdziwą infrastrukturą.

**Maven** - Narzędzie automatyzacji budowania Java.

**Mockito** - Framework do mockowania w Java.

**Spring Boot** - Framework aplikacji Java. [Moduł 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Dokument ten został przetłumaczony przy użyciu automatycznej usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy starań, aby tłumaczenie było precyzyjne, prosimy pamiętać, że tłumaczenia automatyczne mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego języku źródłowym należy uznać za źródło ostateczne. W przypadku istotnych informacji zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->