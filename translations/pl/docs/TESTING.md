# Testowanie aplikacji LangChain4j

## Spis treści

- [Szybki start](../../../docs)
- [Co obejmują testy](../../../docs)
- [Uruchamianie testów](../../../docs)
- [Uruchamianie testów w VS Code](../../../docs)
- [Wzorce testowania](../../../docs)
- [Filozofia testowania](../../../docs)
- [Kolejne kroki](../../../docs)

Ten przewodnik przeprowadzi Cię przez testy, które pokazują, jak testować aplikacje AI bez konieczności posiadania kluczy API lub korzystania z zewnętrznych usług.

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

Kiedy wszystkie testy przejdą pomyślnie, powinieneś zobaczyć wynik podobny do zrzutu ekranu poniżej — testy uruchomione bez żadnych błędów.

<img src="../../../translated_images/pl/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Pomyslne wykonanie testów pokazujące wszystkie testy z zerową liczbą błędów*

## Co obejmują testy

Ten kurs koncentruje się na **testach jednostkowych** uruchamianych lokalnie. Każdy test demonstruje konkretną koncepcję LangChain4j w izolacji. Piramida testów poniżej pokazuje, gdzie mieszczą się testy jednostkowe — stanowią one szybkie, niezawodne fundamenty, na których opiera się reszta Twojej strategii testowania.

<img src="../../../translated_images/pl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramida testowania pokazująca równowagę między testami jednostkowymi (szybkie, izolowane), testami integracyjnymi (prawdziwe komponenty) i testami end-to-end. To szkolenie dotyczy testów jednostkowych.*

| Moduł | Testy | Skupienie | Kluczowe pliki |
|--------|-------|-------|-----------|
| **00 - Szybki start** | 6 | Szablony promptów i podstawienie zmiennych | `SimpleQuickStartTest.java` |
| **01 - Wprowadzenie** | 8 | Pamięć konwersacji i stanowy chat | `SimpleConversationTest.java` |
| **02 - Inżynieria promptów** | 12 | Wzorce GPT-5.2, poziomy chęci, strukturalny output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Wprowadzanie dokumentów, osadzenia, wyszukiwanie podobieństw | `DocumentServiceTest.java` |
| **04 - Narzędzia** | 12 | Wywoływanie funkcji i łączenie narzędzi | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Protokół kontekstowy modelu z transportem stdio | `SimpleMcpTest.java` |

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

**Uruchom testy dla wybranego modułu:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Lub z katalogu głównego
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
mvn test -Dtest=SimpleConversationTest#powinienZachowaćHistorięRozmowy
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#powinnoZachowaćHistorięRozmowy
```

## Uruchamianie testów w VS Code

Jeśli korzystasz z Visual Studio Code, Test Explorer oferuje graficzny interfejs do uruchamiania i debugowania testów.

<img src="../../../translated_images/pl/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer w VS Code pokazujący drzewo testów ze wszystkimi klasami testowymi Java i poszczególnymi metodami testowymi*

**Aby uruchomić testy w VS Code:**

1. Otwórz Test Explorer klikając ikonę probówki na pasku aktywności
2. Rozwiń drzewo testów, aby zobaczyć wszystkie moduły i klasy testowe
3. Kliknij przycisk odtwarzania obok dowolnego testu, aby uruchomić go indywidualnie
4. Kliknij "Run All Tests", aby uruchomić cały zestaw
5. Kliknij prawym przyciskiem myszy dowolny test i wybierz "Debug Test", aby ustawić punkty przerwań i krokować po kodzie

Test Explorer pokazuje zielone znaczniki dla przechodzących testów i dostarcza szczegółowe komunikaty o błędach, gdy testy nie przejdą.

## Wzorce testowania

### Wzorzec 1: Testowanie szablonów promptów

Najprostszy wzorzec testuje szablony promptów bez wywoływania jakiegokolwiek modelu AI. Sprawdzasz, czy podstawienie zmiennych działa poprawnie oraz czy prompty są formatowane zgodnie z oczekiwaniami.

<img src="../../../translated_images/pl/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testowanie szablonów promptów pokazujące przepływ podstawiania zmiennych: szablon z miejscami na wartości → zastosowanie wartości → weryfikacja sformatowanego outputu*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testowanieFormatowaniaSzablonuPodpowiedzi
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testowanieFormatowaniaSzablonuPromptu
```

### Wzorzec 2: Mockowanie modeli językowych

Testując logikę konwersacji, użyj Mockito, aby stworzyć fałszywe modele, które zwracają z góry ustalone odpowiedzi. Dzięki temu testy są szybkie, darmowe i deterministyczne.

<img src="../../../translated_images/pl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Porównanie pokazujące, dlaczego do testowania preferuje się mocki: są szybkie, darmowe, deterministyczne i nie wymagają kluczy API*

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
        assertThat(history).hasSize(6); // 3 wiadomości od użytkownika + 3 wiadomości od AI
    }
}
```

Ten wzorzec pojawia się w `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Mock zapewnia stałe zachowanie, dzięki czemu możesz zweryfikować poprawność zarządzania pamięcią.

### Wzorzec 3: Testowanie izolacji konwersacji

Pamięć konwersacji musi utrzymywać oddzielnie wielu użytkowników. Ten test weryfikuje, że konwersacje nie mieszają kontekstów.

<img src="../../../translated_images/pl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testowanie izolacji konwersacji pokazujące oddzielne magazyny pamięci dla różnych użytkowników, aby zapobiec mieszaniu kontekstów*

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

Każda konwersacja zachowuje swoją niezależną historię. W systemach produkcyjnych ta izolacja jest kluczowa dla aplikacji wieloużytkownikowych.

### Wzorzec 4: Testowanie narzędzi niezależnie

Narzędzia to funkcje, które AI może wywoływać. Testuj je bezpośrednio, aby upewnić się, że działają poprawnie, niezależnie od decyzji AI.

<img src="../../../translated_images/pl/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Testowanie narzędzi niezależnie pokazujące wykonanie mockowanego narzędzia bez wywołań AI, w celu weryfikacji logiki biznesowej*

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

Te testy z `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` walidują logikę narzędzi bez udziału AI. Przykład łączenia pokazuje, jak output jednego narzędzia zasila input drugiego.

### Wzorzec 5: Testowanie RAG w pamięci

Systemy RAG tradycyjnie wymagają baz wektorowych i usług osadzeń. Wzorzec w pamięci pozwala na testowanie całej pipeline bez zewnętrznych zależności.

<img src="../../../translated_images/pl/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Przepływ testowania RAG w pamięci pokazujący analiza dokumentu, przechowywanie osadzeń i wyszukiwanie podobieństw bez potrzeby bazy danych*

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

Moduł MCP testuje integrację Protokołu Kontekstu Modelu używając transportu stdio. Testy weryfikują, że Twoja aplikacja może uruchamiać i komunikować się z serwerami MCP jako podprocesami.

Testy w `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` potwierdzają zachowanie klienta MCP.

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

Testuj swój kod, nie AI. Twoje testy powinny weryfikować kod, który piszesz, sprawdzając jak konstruowane są prompt’y, jak zarządzana jest pamięć i jak wykonywane są narzędzia. Odpowiedzi AI są zmienne i nie powinny być częścią asercji testowych. Pytaj siebie, czy Twój szablon prompta poprawnie podstawia zmienne, a nie czy AI daje właściwą odpowiedź.

Używaj mocków dla modeli językowych. To zewnętrzne zależności, które są wolne, kosztowne i niedeterministyczne. Mockowanie sprawia, że testy są szybkie (milisekundy zamiast sekund), darmowe (bez kosztów API) i deterministyczne (ten sam wynik za każdym razem).

Utrzymuj testy niezależne. Każdy test powinien samodzielnie przygotować dane, nie polegać na innych testach i sprzątać po sobie. Testy powinny przechodzić niezależnie od kolejności uruchomienia.

Testuj przypadki brzegowe poza szczęśliwą ścieżką. Próbuj pustych danych, bardzo dużych wejść, znaków specjalnych, niepoprawnych parametrów i warunków granicznych. To często odkrywa błędy, których normalne użycie nie ujawnia.

Używaj opisowych nazw. Porównaj `shouldMaintainConversationHistoryAcrossMultipleMessages()` z `test1()`. Pierwsza nazwa mówi dokładnie, co jest testowane, co znacznie ułatwia debugowanie błędów.

## Kolejne kroki

Teraz, gdy rozumiesz wzorce testowania, zagłęb się w każdy moduł:

- **[00 - Szybki start](../00-quick-start/README.md)** - Zacznij od podstaw szablonów promptów
- **[01 - Wprowadzenie](../01-introduction/README.md)** - Naucz się zarządzania pamięcią konwersacji
- **[02 - Inżynieria promptów](../02/prompt-engineering/README.md)** - Opanuj wzorce promptów GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Buduj systemy generacji rozszerzonej o wyszukiwanie
- **[04 - Narzędzia](../04-tools/README.md)** - Implementuj wywoływanie funkcji i łańcuchy narzędzi
- **[05 - MCP](../05-mcp/README.md)** - Integruj Protokół Kontekstu Modelu

README każdego modułu zawiera szczegółowe wyjaśnienia koncepcji testowanych tutaj.

---

**Nawigacja:** [← Wróć do głównej](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pomimo naszych starań o dokładność, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w języku źródłowym powinien być uważany za źródło autorytatywne. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia ludzkiego. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->