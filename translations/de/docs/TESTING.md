# Testen von LangChain4j-Anwendungen

## Inhaltsverzeichnis

- [Schnellstart](../../../docs)
- [Was die Tests abdecken](../../../docs)
- [Tests ausführen](../../../docs)
- [Tests in VS Code ausführen](../../../docs)
- [Testmuster](../../../docs)
- [Testphilosophie](../../../docs)
- [Nächste Schritte](../../../docs)

Dieser Leitfaden führt Sie durch die Tests, die demonstrieren, wie KI-Anwendungen getestet werden können, ohne API-Schlüssel oder externe Dienste zu benötigen.

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

<img src="../../../translated_images/de/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Erfolgreiche Testausführung, bei der alle Tests ohne Fehler bestanden wurden*

## Was die Tests abdecken

Dieser Kurs konzentriert sich auf **Unit-Tests**, die lokal ausgeführt werden. Jeder Test demonstriert ein spezifisches LangChain4j-Konzept isoliert.

<img src="../../../translated_images/de/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Testpyramide, die das Gleichgewicht zwischen Unit-Tests (schnell, isoliert), Integrationstests (reale Komponenten) und End-to-End-Tests zeigt. Diese Schulung behandelt Unit-Tests.*

| Modul | Tests | Fokus | Wichtige Dateien |
|--------|-------|-------|-----------|
| **00 - Schnellstart** | 6 | Prompt-Vorlagen und Variablensubstitution | `SimpleQuickStartTest.java` |
| **01 - Einführung** | 8 | Gesprächsspeicher und zustandsbehafteter Chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | GPT-5.2-Muster, Eifer-Level, strukturierte Ausgabe | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Dokumentenaufnahme, Einbettungen, Ähnlichkeitssuche | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Funktionsaufrufe und Tool-Verkettung | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol mit Stdio-Transport | `SimpleMcpTest.java` |

## Tests ausführen

**Alle Tests vom Root-Verzeichnis ausführen:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Tests für ein bestimmtes Modul ausführen:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Oder vom Stammverzeichnis
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Oder vom Stammverzeichnis
mvn --% test -pl 01-introduction
```

**Eine einzelne Testklasse ausführen:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Eine bestimmte Testmethode ausführen:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#sollteGesprächsverlaufBeibehalten
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#sollteKonversationsverlaufBeibehalten
```

## Tests in VS Code ausführen

Wenn Sie Visual Studio Code verwenden, bietet der Test-Explorer eine grafische Oberfläche zum Ausführen und Debuggen von Tests.

<img src="../../../translated_images/de/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test-Explorer mit dem Testbaum, der alle Java-Testklassen und einzelne Testmethoden anzeigt*

**Um Tests in VS Code auszuführen:**

1. Öffnen Sie den Test-Explorer, indem Sie auf das Kolben-Symbol in der Aktivitätsleiste klicken
2. Erweitern Sie den Testbaum, um alle Module und Testklassen zu sehen
3. Klicken Sie auf die Wiedergabetaste neben einem Test, um ihn einzeln auszuführen
4. Klicken Sie auf "Run All Tests", um die komplette Suite auszuführen
5. Klicken Sie mit der rechten Maustaste auf einen Test und wählen Sie "Debug Test", um Breakpoints zu setzen und Schritt für Schritt durch den Code zu gehen

Der Test-Explorer zeigt grüne Häkchen für bestandene Tests und liefert detaillierte Fehlermeldungen bei Testfehlern.

## Testmuster

### Muster 1: Testen von Prompt-Vorlagen

Das einfachste Muster testet Prompt-Vorlagen, ohne ein KI-Modell aufzurufen. Sie überprüfen, dass die Variablensubstitution korrekt funktioniert und die Prompts wie erwartet formatiert sind.

<img src="../../../translated_images/de/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Testen von Prompt-Vorlagen mit Substitutionsfluss: Vorlage mit Platzhaltern → Werte angewandt → formatierte Ausgabe überprüft*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#Testen der Prompt-Vorlagenformatierung
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatierung
```

### Muster 2: Mocking von Sprachmodellen

Beim Testen der Gesprächslogik verwenden Sie Mockito, um gefälschte Modelle zu erstellen, die vorgegebene Antworten zurückgeben. Dies macht Tests schnell, kostenlos und deterministisch.

<img src="../../../translated_images/de/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Vergleich, warum Mocks für Tests bevorzugt werden: schnell, kostenlos, deterministisch und benötigen keine API-Schlüssel*

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
        assertThat(history).hasSize(6); // 3 Nutzer- + 3 KI-Nachrichten
    }
}
```

Dieses Muster findet sich in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Das Mock stellt konsistentes Verhalten sicher, damit die Speicherverwaltung richtig überprüft werden kann.

### Muster 3: Testen der Gesprächs-Isolation

Der Gesprächsspeicher muss mehrere Benutzer getrennt halten. Dieser Test prüft, dass Gespräche keine Kontexte vermischen.

<img src="../../../translated_images/de/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Testen der Gesprächs-Isolation mit separaten Speichern für verschiedene Benutzer, um Kontextvermischung zu verhindern*

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

Jedes Gespräch führt seine eigene unabhängige Historie. In produktiven Systemen ist diese Isolation für Multi-User-Anwendungen entscheidend.

### Muster 4: Tools unabhängig testen

Tools sind Funktionen, die die KI aufrufen kann. Testen Sie diese direkt, um sicherzustellen, dass sie unabhängig von KI-Entscheidungen korrekt funktionieren.

<img src="../../../translated_images/de/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Unabhängiges Testen von Tools mit Mock-Ausführung ohne KI-Aufrufe zur Überprüfung der Geschäftslogik*

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

Diese Tests aus `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` validieren die Tool-Logik ohne KI-Beteiligung. Das Verkettungsbeispiel zeigt, wie die Ausgabe eines Tools in den Input eines anderen fließt.

### Muster 5: In-Memory RAG-Test

RAG-Systeme benötigen traditionell Vektor-Datenbanken und Einbettungsdienste. Das In-Memory-Muster erlaubt es, die gesamte Pipeline ohne externe Abhängigkeiten zu testen.

<img src="../../../translated_images/de/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-Memory-RAG-Testablauf mit Dokumentenparsing, Einbettungsspeicherung und Ähnlichkeitssuche ohne Datenbank*

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

Dieser Test aus `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` erstellt ein Dokument im Speicher und überprüft Chunking und Metadatenhandhabung.

### Muster 6: MCP-Integrationstests

Das MCP-Modul testet die Integration des Model Context Protocols unter Verwendung des stdio-Transports. Diese Tests prüfen, ob Ihre Anwendung MCP-Server als Unterprozesse starten und mit ihnen kommunizieren kann.

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

Testen Sie Ihren Code, nicht die KI. Ihre Tests sollten den von Ihnen geschriebenen Code validieren, indem sie überprüfen, wie Prompts aufgebaut werden, wie Speicher verwaltet wird und wie Tools ausgeführt werden. KI-Antworten variieren und sollten kein Teil von Testaussagen sein. Fragen Sie sich, ob Ihre Prompt-Vorlage Variablen korrekt ersetzt, nicht, ob die KI die richtige Antwort gibt.

Verwenden Sie Mocks für Sprachmodelle. Diese sind externe Abhängigkeiten, die langsam, teuer und nicht deterministisch sind. Mocking macht Tests schnell (Millisekunden statt Sekunden), kostenlos (keine API-Kosten) und deterministisch (immer dasselbe Ergebnis).

Halten Sie Tests unabhängig. Jeder Test sollte seine eigenen Daten einrichten, sich nicht auf andere Tests verlassen und hinter sich aufräumen. Tests sollen unabhängig von der Ausführungsreihenfolge bestehen.

Testen Sie Randfälle jenseits des Happy Paths. Probieren Sie leere Eingaben, sehr große Eingaben, Sonderzeichen, ungültige Parameter und Grenzwerte. Diese decken oft Fehler auf, die bei normaler Nutzung nicht sichtbar sind.

Verwenden Sie beschreibende Namen. Vergleichen Sie `shouldMaintainConversationHistoryAcrossMultipleMessages()` mit `test1()`. Der erste Name sagt genau, was getestet wird, was die Fehlersuche erleichtert.

## Nächste Schritte

Jetzt, wo Sie die Testmuster verstehen, tauchen Sie tiefer in jedes Modul ein:

- **[00 - Schnellstart](../00-quick-start/README.md)** - Beginnen Sie mit Grundlagen zu Prompt-Vorlagen
- **[01 - Einführung](../01-introduction/README.md)** - Lernen Sie Gesprächsspeicherverwaltung
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Meistern Sie GPT-5.2-Promptmuster
- **[03 - RAG](../03-rag/README.md)** - Bauen Sie Retrieval-Augmented Generation Systeme
- **[04 - Tools](../04-tools/README.md)** - Implementieren Sie Funktionsaufrufe und Tool-Ketten
- **[05 - MCP](../05-mcp/README.md)** - Integrieren Sie das Model Context Protocol

Die README-Datei jedes Moduls bietet detaillierte Erklärungen der hier getesteten Konzepte.

---

**Navigation:** [← Zurück zur Hauptseite](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mithilfe des KI-Übersetzungsdienstes [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache ist als maßgebliche Quelle zu betrachten. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->