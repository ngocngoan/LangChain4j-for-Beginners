# Testen von LangChain4j-Anwendungen

## Inhaltsverzeichnis

- [Schnellstart](../../../docs)
- [Was die Tests abdecken](../../../docs)
- [Tests ausführen](../../../docs)
- [Tests in VS Code ausführen](../../../docs)
- [Testmuster](../../../docs)
- [Testphilosophie](../../../docs)
- [Nächste Schritte](../../../docs)

Diese Anleitung führt Sie durch die Tests, die zeigen, wie man KI-Anwendungen testet, ohne API-Schlüssel oder externe Dienste zu benötigen.

## Schnellstart

Führen Sie alle Tests mit einem einzigen Befehl aus:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Wenn alle Tests bestanden sind, sollten Sie eine Ausgabe wie im folgenden Screenshot sehen – die Tests laufen ohne Fehler durch.

<img src="../../../translated_images/de/test-results.ea5c98d8f3642043.webp" alt="Erfolgreiche Testergebnisse" width="800"/>

*Erfolgreiche Testausführung, die zeigt, dass alle Tests ohne Fehler bestanden wurden*

## Was die Tests abdecken

Dieser Kurs konzentriert sich auf **Unit-Tests**, die lokal ausgeführt werden. Jeder Test zeigt ein bestimmtes LangChain4j-Konzept isoliert. Die untenstehende Testpyramide zeigt, wo Unit-Tests stehen – sie bilden die schnelle und zuverlässige Grundlage, auf der der Rest Ihrer Teststrategie aufbaut.

<img src="../../../translated_images/de/testing-pyramid.2dd1079a0481e53e.webp" alt="Testpyramide" width="800"/>

*Testpyramide, die das Verhältnis zwischen Unit-Tests (schnell, isoliert), Integrationstests (echte Komponenten) und End-to-End-Tests zeigt. Dieses Training behandelt Unit-Tests.*

| Modul | Tests | Fokus | Wichtige Dateien |
|--------|-------|-------|-----------|
| **00 - Schnellstart** | 6 | Prompt-Vorlagen und Variablensubstitution | `SimpleQuickStartTest.java` |
| **01 - Einführung** | 8 | Gesprächsspeicher und zustandsbehafteter Chat | `SimpleConversationTest.java` |
| **02 - Prompt-Engineering** | 12 | GPT-5.2-Muster, Eifer-Level, strukturierte Ausgabe | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentenintegration, Einbettungen, Ähnlichkeitssuche | `DocumentServiceTest.java` |
| **04 - Werkzeuge** | 12 | Funktionsaufruf und Werkzeugverkettung | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol mit Stdio-Transport | `SimpleMcpTest.java` |

## Tests ausführen

**Führen Sie alle Tests aus dem Root-Verzeichnis aus:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Führen Sie Tests für ein bestimmtes Modul aus:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Oder vom Wurzelverzeichnis aus
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Oder vom Stammverzeichnis aus
mvn --% test -pl 01-introduction
```

**Führen Sie eine einzelne Testklasse aus:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Führen Sie eine bestimmte Testmethode aus:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#Gesprächsverlauf beibehalten
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#Unterhaltungshistorie beibehalten
```

## Tests in VS Code ausführen

Wenn Sie Visual Studio Code verwenden, bietet der Test-Explorer eine grafische Oberfläche zum Ausführen und Debuggen von Tests.

<img src="../../../translated_images/de/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test-Explorer" width="800"/>

*VS Code Test-Explorer zeigt den Testbaum mit allen Java-Testklassen und einzelnen Testmethoden*

**So führen Sie Tests in VS Code aus:**

1. Öffnen Sie den Test-Explorer, indem Sie auf das Reagenzglas-Symbol in der Aktivitätsleiste klicken.
2. Erweitern Sie den Testbaum, um alle Module und Testklassen zu sehen.
3. Klicken Sie auf die Wiedergabetaste neben einem Test, um ihn einzeln auszuführen.
4. Klicken Sie auf „Alle Tests ausführen“, um die gesamte Suite zu starten.
5. Klicken Sie mit der rechten Maustaste auf einen Test und wählen Sie „Test debuggen“, um Haltepunkte zu setzen und den Code schrittweise durchzugehen.

Der Test-Explorer zeigt grüne Häkchen für bestandene Tests und gibt bei Fehlern detaillierte Fehlermeldungen aus.

## Testmuster

### Muster 1: Testen von Promptvorlagen

Das einfachste Muster testet Promptvorlagen ohne Aufruf eines KI-Modells. Sie überprüfen, ob die Variablensubstitution korrekt funktioniert und Prompts wie erwartet formatiert werden.

<img src="../../../translated_images/de/prompt-template-testing.b902758ddccc8dee.webp" alt="Promptvorlagen-Test" width="800"/>

*Testing von Promptvorlagen, das den Fluss der Variablensubstitution zeigt: Vorlage mit Platzhaltern → angewendete Werte → überprüfte formatierte Ausgabe*

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

Dieser Test befindet sich in `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Ausführen:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatierung
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatierung
```

### Muster 2: Mocking von Sprachmodellen

Beim Testen der Gesprächslogik verwenden Sie Mockito, um gefälschte Modelle zu erstellen, die vorab festgelegte Antworten liefern. Das macht Tests schnell, kostenfrei und deterministisch.

<img src="../../../translated_images/de/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Vergleich Mock vs. echte API" width="800"/>

*Vergleich, warum Mocks beim Testen bevorzugt werden: sie sind schnell, kostenlos, deterministisch und benötigen keine API-Schlüssel*

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
        assertThat(history).hasSize(6); // 3 Benutzer- + 3 KI-Nachrichten
    }
}
```

Dieses Muster erscheint in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Der Mock sorgt für konsistentes Verhalten, sodass Sie überprüfen können, ob das Speichermanagement korrekt funktioniert.

### Muster 3: Testen der Gesprächs-Isolation

Der Gesprächsspeicher muss mehrere Benutzer getrennt halten. Dieser Test prüft, dass sich Gespräche nicht vermischen.

<img src="../../../translated_images/de/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Gesprächs-Isolation" width="800"/>

*Testen der Gesprächs-Isolation, das separate Speicherbereiche für verschiedene Benutzer zeigt, um Kontextverwechslungen zu vermeiden*

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

Jedes Gespräch führt seine eigene unabhängige Historie. In Produktionssystemen ist diese Isolation für Multi-User-Anwendungen entscheidend.

### Muster 4: Werkzeuge unabhängig testen

Werkzeuge sind Funktionen, die die KI aufrufen kann. Testen Sie sie direkt, um sicherzustellen, dass sie korrekt funktionieren, unabhängig von KI-Entscheidungen.

<img src="../../../translated_images/de/tools-testing.3e1706817b0b3924.webp" alt="Werkzeug-Test" width="800"/>

*Unabhängiges Testen von Werkzeugen, das die Ausführung eines Mock-Werkzeugs ohne KI-Aufrufe zeigt, um die Geschäftslogik zu überprüfen*

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

Diese Tests aus `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validieren die Werkzeuglogik ohne KI-Beteiligung. Das Verkettungsbeispiel zeigt, wie die Ausgabe eines Werkzeugs in die Eingabe eines anderen fließt.

### Muster 5: In-Memory RAG-Test

RAG-Systeme benötigen traditionell Vektordatenbanken und Einbettungsdienste. Das In-Memory-Muster lässt Sie die gesamte Pipeline ohne externe Abhängigkeiten testen.

<img src="../../../translated_images/de/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG-Test" width="800"/>

*In-Memory RAG-Testablauf zeigt Dokumentenparsing, Einbettungsspeicherung und Ähnlichkeitssuche ohne Datenbankbedarf*

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

Dieser Test aus `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` erstellt ein Dokument im Speicher und überprüft Chunking und Metadatenbehandlung.

### Muster 6: MCP-Integrationstest

Das MCP-Modul testet die Integration des Model Context Protocol mit stdio-Transport. Diese Tests prüfen, dass Ihre Anwendung MCP-Server als Unterprozesse starten und mit ihnen kommunizieren kann.

Die Tests in `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` validieren das Verhalten des MCP-Clients.

**Ausführen:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testphilosophie

Testen Sie Ihren Code, nicht die KI. Ihre Tests sollten den von Ihnen geschriebenen Code validieren, indem sie prüfen, wie Prompts konstruiert, wie der Speicher verwaltet und wie Werkzeuge ausgeführt werden. KI-Antworten variieren und sollten nicht Teil der Testannahmen sein. Fragen Sie sich, ob Ihre Promptvorlage Variablen korrekt ersetzt, nicht ob die KI die richtige Antwort gibt.

Nutzen Sie Mocks für Sprachmodelle. Diese sind externe Abhängigkeiten, die langsam, teuer und nicht deterministisch sind. Mocking macht Tests schnell mit Millisekunden statt Sekunden, kostenfrei ohne API-Kosten und deterministisch mit immer gleichem Ergebnis.

Halten Sie Tests unabhängig. Jeder Test sollte seine eigenen Daten einrichten, sich nicht auf andere Tests verlassen und nach sich selbst aufräumen. Tests sollten unabhängig von der Ausführungsreihenfolge bestehen.

Testen Sie auch Grenzfälle über den Happy Path hinaus. Testen Sie leere Eingaben, sehr große Eingaben, Sonderzeichen, ungültige Parameter und Randbedingungen. Diese offenbaren oft Fehler, die im normalen Gebrauch nicht auftreten.

Verwenden Sie beschreibende Namen. Vergleichen Sie `shouldMaintainConversationHistoryAcrossMultipleMessages()` mit `test1()`. Der erste Name sagt genau, was getestet wird, und erleichtert das Debuggen bei Fehlern erheblich.

## Nächste Schritte

Jetzt, wo Sie die Testmuster kennen, vertiefen Sie sich in jedes Modul:

- **[00 - Schnellstart](../00-quick-start/README.md)** - Beginnen Sie mit den Grundlagen der Promptvorlagen
- **[01 - Einführung](../01-introduction/README.md)** - Lernen Sie das Management des Gesprächsspeichers
- **[02 - Prompt-Engineering](../02-prompt-engineering/README.md)** - Meistern Sie GPT-5.2-Prompting-Muster
- **[03 - RAG](../03-rag/README.md)** - Erstellen Sie retrieval-gestützte Generierungssysteme
- **[04 - Werkzeuge](../04-tools/README.md)** - Implementieren Sie Funktionsaufrufe und Werkzeugketten
- **[05 - MCP](../05-mcp/README.md)** - Integrieren Sie das Model Context Protocol

Das README jedes Moduls bietet detaillierte Erklärungen zu den hier getesteten Konzepten.

---

**Navigation:** [← Zurück zur Hauptseite](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir um Genauigkeit bemüht sind, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->