# Testowanie aplikacji LangChain4j

## Spis treści

- [Szybki start](../../../docs)
- [Zakres testów](../../../docs)
- [Uruchamianie testów](../../../docs)
- [Uruchamianie testów w VS Code](../../../docs)
- [Wzorce testowania](../../../docs)
- [Filozofia testowania](../../../docs)
- [Następne kroki](../../../docs)

Ten przewodnik przeprowadzi Cię przez testy pokazujące, jak testować aplikacje AI bez konieczności posiadania kluczy API lub korzystania z usług zewnętrznych.

## Szybki start

Uruchom wszystkie testy jednym poleceniem:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/pl/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Pomyślne wykonanie testów pokazujące, że wszystkie testy przeszły bez błędów*

## Zakres testów

Ten kurs skupia się na **testach jednostkowych**, które uruchamiane są lokalnie. Każdy test demonstruje konkretną koncepcję LangChain4j w izolacji.

<img src="../../../translated_images/pl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramida testów pokazująca równowagę między testami jednostkowymi (szybkie, izolowane), testami integracyjnymi (prawdziwe komponenty) oraz testami end-to-end. To szkolenie dotyczy testów jednostkowych.*

| Moduł | Testy | Zawartość | Kluczowe pliki |
|--------|-------|-------|-----------|
| **00 - Szybki start** | 6 | Szablony promptów i podstawianie zmiennych | `SimpleQuickStartTest.java` |
| **01 - Wprowadzenie** | 8 | Pamięć rozmowy i kontekstowe czaty | `SimpleConversationTest.java` |
| **02 - Inżynieria promptów** | 12 | Wzorce GPT-5.2, poziomy gotowości, strukturalny output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestowanie dokumentów, osadzenia, wyszukiwanie podobieństw | `DocumentServiceTest.java` |
| **04 - Narzędzia** | 12 | Wywoływanie funkcji i łączenie narzędzi | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol ze transportem stdio | `SimpleMcpTest.java` |

## Uruchamianie testów

**Uruchom wszystkie testy z katalogu głównego:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Uruchom testy dla konkretnego modułu:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Lub z katalogu root
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Lub z katalogu głównego
mvn --% test -pl 01-introduction
```

**Uruchom pojedynczą klasę testową:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Uruchom pojedynczą metodę testową:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#czy utrzymać historię rozmowy
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#powinnoUtrzymywaćHistorięRozmowy
```

## Uruchamianie testów w VS Code

Jeśli korzystasz z Visual Studio Code, Test Explorer udostępnia graficzny interfejs do uruchamiania i debugowania testów.

<img src="../../../translated_images/pl/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer w VS Code pokazujący drzewo testów ze wszystkimi klasami testowymi w Javie oraz pojedynczymi metodami testowymi*

**Aby uruchomić testy w VS Code:**

1. Otwórz Test Explorer klikając ikonę probówki na pasku działań
2. Rozwiń drzewo testów, aby zobaczyć wszystkie moduły i klasy testowe
3. Kliknij przycisk odtwarzania obok dowolnego testu, aby uruchomić go osobno
4. Kliknij "Run All Tests", aby wykonać całą serię testów
5. Kliknij prawym przyciskiem myszy na test i wybierz "Debug Test", aby ustawić punkty przerwania i przechodzić przez kod

Test Explorer pokazuje zielone ptaszki dla testów, które przeszły, i szczegółowe komunikaty błędów dla testów niezaliczonych.

## Wzorce testowania

### Wzorzec 1: Testowanie szablonów promptów

Najprostszy wzorzec testuje szablony promptów bez wywoływania modeli AI. Sprawdzasz, że podstawianie zmiennych działa poprawnie i prompty są sformatowane zgodnie z oczekiwaniami.

<img src="../../../translated_images/pl/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testowanie szablonów promptów pokazujące przepływ podstawiania zmiennych: szablon z miejscami na dane → zastosowanie wartości → weryfikacja sformatowanego wyniku*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

Ten test znajduje się w `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Uruchom go:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testFormatowaniaSzablonuPodpowiedzi
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testowanie formatowania szablonu zapytania
```

### Wzorzec 2: Mockowanie modeli językowych

Podczas testowania logiki rozmowy używaj Mockito do tworzenia fałszywych modeli, które zwracają przewidywalne odpowiedzi. Dzięki temu testy są szybkie, darmowe i deterministyczne.

<img src="../../../translated_images/pl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Porównanie pokazujące, dlaczego mocki są preferowane do testowania: są szybkie, darmowe, deterministyczne i nie wymagają kluczy API*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3 wiadomości użytkownika + 3 wiadomości AI
    }
}
```

Ten wzorzec występuje w pliku `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock gwarantuje spójne zachowanie, dzięki czemu możesz zweryfikować poprawność zarządzania pamięcią.

### Wzorzec 3: Testowanie izolacji rozmowy

Pamięć rozmowy musi utrzymywać oddzielne konteksty dla wielu użytkowników. Ten test weryfikuje, że rozmowy nie mieszają kontekstów.

<img src="../../../translated_images/pl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testowanie izolacji rozmowy pokazujące osobne magazyny pamięci dla różnych użytkowników, aby zapobiec mieszaniu kontekstów*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

Każda rozmowa utrzymuje swoją własną, niezależną historię. W systemach produkcyjnych izolacja ta jest kluczowa dla aplikacji wieloużytkownikowych.

### Wzorzec 4: Testowanie narzędzi niezależnie

Narzędzia to funkcje, które AI może wywołać. Testuj je bezpośrednio, by upewnić się, że działają poprawnie niezależnie od decyzji AI.

<img src="../../../translated_images/pl/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testowanie narzędzi niezależnie pokazujące wykonanie mocka narzędzia bez wywołań AI, aby zweryfikować logikę biznesową*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

Te testy z pliku `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` weryfikują logikę narzędzi bez udziału AI. Przykład łańcuchowania pokazuje, jak wyjście jednego narzędzia jest wejściem dla innego.

### Wzorzec 5: Testowanie RAG w pamięci

Systemy RAG tradycyjnie wymagają baz danych wektorowych i usług osadzania. Wzorzec in-memory pozwala testować cały pipeline bez zależności zewnętrznych.

<img src="../../../translated_images/pl/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Workflow testowania RAG w pamięci pokazujący parsowanie dokumentów, przechowywanie osadzeń i wyszukiwanie podobieństw bez potrzeby bazy danych*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

Ten test z `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` tworzy dokument w pamięci i weryfikuje dzielenie na fragmenty oraz obsługę metadanych.

### Wzorzec 6: Testowanie integracji MCP

Moduł MCP testuje integrację Model Context Protocol z użyciem transportu stdio. Testy te weryfikują, czy Twoja aplikacja potrafi uruchamiać i komunikować się z serwerami MCP jako procesami potomnymi.

Testy z `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` weryfikują zachowanie klienta MCP.

**Uruchom je:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Filozofia testowania

Testuj swój kod, nie AI. Twoje testy powinny walidować kod, który piszesz, sprawdzając sposób konstruowania promptów, zarządzania pamięcią i wykonywania narzędzi. Odpowiedzi AI są zmienne i nie powinny być częścią asercji testowych. Zapytaj raczej, czy Twój szablon promptu poprawnie podstawia zmienne, niż czy AI daje dobrą odpowiedź.

Używaj mocków dla modeli językowych. To zewnętrzne zależności, które są wolne, kosztowne i niedeterministyczne. Mockowanie sprawia, że testy są szybkie – w milisekundach zamiast sekund, darmowe – bez kosztów API, oraz deterministyczne – z takim samym wynikiem za każdym razem.

Utrzymuj testy niezależne. Każdy test powinien sam skonfigurować swoje dane, nie polegać na innych testach i sprzątać po sobie. Testy powinny przechodzić niezależnie od kolejności wykonania.

Testuj przypadki graniczne, nie tylko idealne ścieżki. Sprawdź puste dane, bardzo duże dane, znaki specjalne, nieprawidłowe parametry i warunki brzegowe. Takie testy często ujawniają błędy niewidoczne podczas normalnego użytkowania.

Używaj opisowych nazw. Porównaj `shouldMaintainConversationHistoryAcrossMultipleMessages()` z `test1()`. Pierwsza nazwa dokładnie mówi, co jest testowane, co ułatwia debugowanie błędów.

## Następne kroki

Teraz, gdy znasz wzorce testowania, zagłęb się bardziej w każdy moduł:

- **[00 - Szybki start](../00-quick-start/README.md)** - Zacznij od podstaw szablonów promptów
- **[01 - Wprowadzenie](../01-introduction/README.md)** - Naucz się zarządzania pamięcią rozmów
- **[02 - Inżynieria promptów](../02/prompt-engineering/README.md)** - Opanuj wzorce promptów GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Buduj systemy generacji wspomaganej wyszukiwaniem
- **[04 - Narzędzia](../04-tools/README.md)** - Implementuj wywoływanie funkcji i łańcuchy narzędzi
- **[05 - MCP](../05-mcp/README.md)** - Integruj Model Context Protocol

Pliki README każdego modułu zawierają szczegółowe wyjaśnienia koncepcji testowanych tutaj.

---

**Nawigacja:** [← Powrót do głównego](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy starań, aby tłumaczenie było jak najdokładniejsze, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uważany za źródło wiarygodne. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->