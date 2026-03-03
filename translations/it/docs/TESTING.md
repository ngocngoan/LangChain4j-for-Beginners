# Testing LangChain4j Applications

## Table of Contents

- [Quick Start](../../../docs)
- [What the Tests Cover](../../../docs)
- [Running the Tests](../../../docs)
- [Running Tests in VS Code](../../../docs)
- [Testing Patterns](../../../docs)
- [Testing Philosophy](../../../docs)
- [Next Steps](../../../docs)

Questa guida ti accompagna attraverso i test che dimostrano come testare applicazioni AI senza richiedere chiavi API o servizi esterni.

## Quick Start

Esegui tutti i test con un unico comando:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Quando tutti i test passano, dovresti vedere un output simile allo screenshot qui sotto — i test vengono eseguiti con zero errori.

<img src="../../../translated_images/it/test-results.ea5c98d8f3642043.webp" alt="Risultati Test Riusciti" width="800"/>

*Esecuzione di test riuscita che mostra tutti i test superati senza errori*

## What the Tests Cover

Questo corso si concentra su **test unitari** che vengono eseguiti localmente. Ogni test dimostra un concetto specifico di LangChain4j in isolamento. La piramide dei test mostrata sotto indica dove si collocano i test unitari — formano la base veloce e affidabile su cui si costruisce il resto della strategia di testing.

<img src="../../../translated_images/it/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramide dei Test" width="800"/>

*Piramide dei test che mostra l’equilibrio tra test unitari (veloci, isolati), test di integrazione (componenti reali) e test end-to-end. Questa formazione copre il testing unitario.*

| Modulo | Test | Focus | File Chiave |
|--------|-------|-------|-------------|
| **00 - Quick Start** | 6 | Template prompt e sostituzione variabili | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Memoria conversazionale e chat con stato | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Pattern GPT-5.2, livelli di eagerness, output strutturato | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestione documenti, embeddings, ricerca per similarità | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Chiamata funzioni e concatenamento strumenti | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol con trasporto Stdio | `SimpleMcpTest.java` |

## Running the Tests

**Esegui tutti i test dalla root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Esegui i test di un modulo specifico:**

**Bash:**
```bash
cd 01-introduction && mvn test
# O dalla radice
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# O dalla radice
mvn --% test -pl 01-introduction
```

**Esegui una singola classe di test:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Esegui un metodo di test specifico:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#dovrebbeMantenereLaCronologiaDellaConversazione
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#dovrebbeMantenereLaCronologiaDellaConversazione
```

## Running Tests in VS Code

Se usi Visual Studio Code, il Test Explorer offre un’interfaccia grafica per eseguire e fare il debug dei test.

<img src="../../../translated_images/it/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer di VS Code che mostra l’albero dei test con tutte le classi di test Java e i singoli metodi di test*

**Per eseguire i test in VS Code:**

1. Apri il Test Explorer cliccando sull’icona del becher nella Activity Bar
2. Espandi l’albero dei test per vedere tutti i moduli e le classi di test
3. Clicca il pulsante play accanto a qualsiasi test per eseguirlo singolarmente
4. Clicca su "Run All Tests" per eseguire l’intera suite
5. Clicca col tasto destro su un test e scegli "Debug Test" per impostare breakpoint e fare step attraverso il codice

Il Test Explorer mostra spunte verdi per i test superati e fornisce messaggi dettagliati in caso di fallimento.

## Testing Patterns

### Pattern 1: Testing Prompt Templates

Il pattern più semplice testa i template dei prompt senza chiamare alcun modello AI. Verifichi che la sostituzione delle variabili funzioni correttamente e che i prompt siano formattati come previsto.

<img src="../../../translated_images/it/prompt-template-testing.b902758ddccc8dee.webp" alt="Testing Template Prompt" width="800"/>

*Testing dei template di prompt che mostra il flusso di sostituzione variabili: template con segnaposto → applicazione dei valori → output formattato verificato*

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

Questo test si trova in `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Eseguilo:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testareLaFormattazioneDelModelloDiPrompt
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatting
```

### Pattern 2: Mocking Language Models

Quando testi la logica di conversazione, usa Mockito per creare modelli finti che restituiscono risposte predeterminate. Questo rende i test veloci, gratuiti e deterministici.

<img src="../../../translated_images/it/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Confronto Mock vs API reale" width="800"/>

*Confronto che mostra perché si preferiscono i mock per il testing: sono veloci, gratuiti, deterministici e non richiedono chiavi API*

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
        assertThat(history).hasSize(6); // 3 messaggi utente + 3 messaggi AI
    }
}
```

Questo pattern appare in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Il mock garantisce un comportamento consistente così da poter verificare che la gestione della memoria funzioni correttamente.

### Pattern 3: Testing Conversation Isolation

La memoria della conversazione deve mantenere separati più utenti. Questo test verifica che le conversazioni non mescolino i contesti.

<img src="../../../translated_images/it/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolamento della conversazione" width="800"/>

*Testing dell’isolamento della conversazione che mostra archivi di memoria separati per utenti diversi per evitare mescolamento dei contesti*

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

Ogni conversazione mantiene la propria storia indipendente. Nei sistemi di produzione, questo isolamento è critico per applicazioni multiutente.

### Pattern 4: Testing Tools Independently

Gli strumenti sono funzioni che l’AI può chiamare. Testali direttamente per assicurarti che funzionino correttamente indipendentemente dalle decisioni dell’AI.

<img src="../../../translated_images/it/tools-testing.3e1706817b0b3924.webp" alt="Testing Strumenti" width="800"/>

*Testing degli strumenti indipendentemente che mostra esecuzione di tool mock senza chiamate AI per verificare la logica di business*

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

Questi test da `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` convalidano la logica degli strumenti senza coinvolgimento AI. L’esempio di concatenamento mostra come l’output di uno strumento alimenti l’input di un altro.

### Pattern 5: In-Memory RAG Testing

I sistemi RAG tradizionalmente richiedono database vettoriali e servizi di embedding. Il pattern in-memory ti permette di testare l’intera pipeline senza dipendenze esterne.

<img src="../../../translated_images/it/rag-testing.ee7541b1e23934b1.webp" alt="Testing RAG In-Memory" width="800"/>

*Flusso di lavoro RAG in-memory che mostra parsing documenti, memorizzazione embedding e ricerca per similarità senza richiedere un database*

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

Questo test da `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` crea un documento in memoria e verifica chunking e gestione meta-dati.

### Pattern 6: MCP Integration Testing

Il modulo MCP testa l’integrazione del Model Context Protocol usando il trasporto stdio. Questi test verificano che la tua applicazione possa avviare e comunicare con server MCP come sottoprocessi.

I test in `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` convalidano il comportamento del client MCP.

**Eseguili:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Testing Philosophy

Testa il tuo codice, non l’AI. I tuoi test dovrebbero validare il codice che scrivi controllando come i prompt vengono costruiti, come la memoria viene gestita e come gli strumenti vengono eseguiti. Le risposte AI variano e non dovrebbero far parte delle asserzioni di test. Chiediti se il tuo template di prompt sostituisce correttamente le variabili, non se l’AI dà la risposta giusta.

Usa i mock per i modelli linguistici. Sono dipendenze esterne che sono lente, costose e non deterministiche. Il mocking rende i test veloci con millisecondi anziché secondi, gratuiti senza costi API, e deterministici con lo stesso risultato ogni volta.

Mantieni i test indipendenti. Ogni test dovrebbe preparare i propri dati, non dipendere da altri test, e pulire dopo sé stesso. I test dovrebbero passare indipendentemente dall’ordine di esecuzione.

Testa i casi limite oltre il percorso positivo. Prova input vuoti, input molto grandi, caratteri speciali, parametri non validi e condizioni al limite. Questi spesso fanno emergere bug che l’uso normale non rivela.

Usa nomi descrittivi. Confronta `shouldMaintainConversationHistoryAcrossMultipleMessages()` con `test1()`. Il primo ti dice esattamente cosa viene testato, rendendo il debug dei fallimenti molto più facile.

## Next Steps

Ora che conosci i pattern di testing, approfondisci ogni modulo:

- **[00 - Quick Start](../00-quick-start/README.md)** - Inizia con le basi dei template prompt
- **[01 - Introduction](../01-introduction/README.md)** - Impara la gestione della memoria conversazionale
- **[02 - Prompt Engineering](../02/prompt-engineering/README.md)** - Padroneggia i pattern di prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Costruisci sistemi di retrieval-augmented generation
- **[04 - Tools](../04-tools/README.md)** - Implementa chiamate funzioni e catene di strumenti
- **[05 - MCP](../05-mcp/README.md)** - Integra Model Context Protocol

Il README di ogni modulo fornisce spiegazioni dettagliate dei concetti trattati qui.

---

**Navigazione:** [← Torna alla Home](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per l’accuratezza, si prega di considerare che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda la traduzione professionale effettuata da un esperto umano. Non ci assumiamo alcuna responsabilità per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->