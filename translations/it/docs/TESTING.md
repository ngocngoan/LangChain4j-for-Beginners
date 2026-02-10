# Testing LangChain4j Applications

## Indice

- [Inizio rapido](../../../docs)
- [Cosa coprono i test](../../../docs)
- [Esecuzione dei test](../../../docs)
- [Esecuzione dei test in VS Code](../../../docs)
- [Modelli di test](../../../docs)
- [Filosofia del testing](../../../docs)
- [Passi successivi](../../../docs)

Questa guida ti accompagna attraverso i test che dimostrano come testare applicazioni AI senza richiedere chiavi API o servizi esterni.

## Inizio rapido

Esegui tutti i test con un solo comando:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/it/test-results.ea5c98d8f3642043.webp" alt="Risultati del test riuscito" width="800"/>

*Esecuzione del test riuscita che mostra tutti i test superati senza errori*

## Cosa coprono i test

Questo corso si concentra su **test unitari** che vengono eseguiti localmente. Ogni test dimostra un concetto specifico di LangChain4j in isolamento.

<img src="../../../translated_images/it/testing-pyramid.2dd1079a0481e53e.webp" alt="Piramide del testing" width="800"/>

*Piramide del testing che mostra l'equilibrio tra test unitari (veloci, isolati), test di integrazione (componenti reali) e test end-to-end. Questa formazione copre il test unitario.*

| Modulo | Test | Focus | File chiave |
|--------|-------|-------|-------------|
| **00 - Inizio rapido** | 6 | Modelli prompt e sostituzione variabili | `SimpleQuickStartTest.java` |
| **01 - Introduzione** | 8 | Memoria della conversazione e chat con stato | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Pattern GPT-5.2, livelli di prontezza, output strutturato | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Ingestione documenti, embedding, ricerca per similarità | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Richiami di funzione e concatenamento strumenti | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol con trasporto Stdio | `SimpleMcpTest.java` |

## Esecuzione dei test

**Esegui tutti i test dalla root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Esegui i test per un modulo specifico:**

**Bash:**
```bash
cd 01-introduction && mvn test
# O dalla radice
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Oppure dalla radice
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

## Esecuzione dei test in VS Code

Se usi Visual Studio Code, il Test Explorer fornisce un'interfaccia grafica per eseguire e fare debug dei test.

<img src="../../../translated_images/it/vscode-testing.f02dd5917289dced.webp" alt="Test Explorer di VS Code" width="800"/>

*Test Explorer di VS Code che mostra l'albero dei test con tutte le classi di test Java e metodi di test individuali*

**Per eseguire i test in VS Code:**

1. Apri il Test Explorer cliccando sull'icona della provetta nella barra attività
2. Espandi l'albero dei test per vedere tutti i moduli e le classi di test
3. Clicca il pulsante play accanto a un qualsiasi test per eseguirlo singolarmente
4. Clicca "Run All Tests" per eseguire l'intera suite
5. Clicca col tasto destro su un test e seleziona "Debug Test" per impostare breakpoint e eseguire passo passo il codice

Il Test Explorer mostra un segno di spunta verde per i test superati e fornisce messaggi dettagliati in caso di fallimento.

## Modelli di test

### Modello 1: Testare modelli prompt

Il modello più semplice testa i modelli prompt senza chiamare alcun modello AI. Verifichi che la sostituzione delle variabili funzioni correttamente e che i prompt siano formattati come previsto.

<img src="../../../translated_images/it/prompt-template-testing.b902758ddccc8dee.webp" alt="Test dei modelli prompt" width="800"/>

*Testing dei modelli prompt che mostra il flusso di sostituzione delle variabili: modello con segnaposto → valori applicati → output formattato verificato*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatting
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testFormatoTemplatePrompt
```

### Modello 2: Mockare modelli linguistici

Quando testi la logica della conversazione, usa Mockito per creare modelli finti che restituiscono risposte predeterminate. Questo rende i test veloci, gratuiti e deterministici.

<img src="../../../translated_images/it/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Confronto mock vs API reale" width="800"/>

*Confronto che mostra perché i mock sono preferiti per i test: sono veloci, gratuiti, deterministici e non richiedono chiavi API*

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

Questo modello appare in `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Il mock garantisce un comportamento consistente così da poter verificare che la gestione della memoria funzioni correttamente.

### Modello 3: Testare l'isolamento delle conversazioni

La memoria della conversazione deve mantenere separati più utenti. Questo test verifica che le conversazioni non mescolino i contesti.

<img src="../../../translated_images/it/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Isolamento della conversazione" width="800"/>

*Test dell'isolamento della conversazione che mostra archivi memoria separati per utenti differenti per evitare mescolamenti di contesto*

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

Ogni conversazione mantiene la propria storia indipendente. Nei sistemi in produzione, questo isolamento è critico per applicazioni multi-utente.

### Modello 4: Testare strumenti indipendentemente

Gli strumenti sono funzioni che l'AI può chiamare. Testali direttamente per assicurarti che funzionino correttamente indipendentemente dalle decisioni AI.

<img src="../../../translated_images/it/tools-testing.3e1706817b0b3924.webp" alt="Test degli strumenti" width="800"/>

*Test degli strumenti indipendenti che mostra l'esecuzione di uno strumento mock senza chiamate AI per verificare la logica di business*

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

Questi test da `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` convalidano la logica degli strumenti senza coinvolgimento AI. L'esempio di concatenamento mostra come l'output di uno strumento alimenta l'input di un altro.

### Modello 5: Test RAG in memoria

I sistemi RAG tradizionalmente richiedono database vettoriali e servizi di embedding. Il modello in memoria ti permette di testare l'intera pipeline senza dipendenze esterne.

<img src="../../../translated_images/it/rag-testing.ee7541b1e23934b1.webp" alt="Test RAG in memoria" width="800"/>

*Flusso di lavoro RAG in memoria che mostra parsing documenti, memorizzazione embedding e ricerca di similarità senza richiedere un database*

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

Questo test da `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` crea un documento in memoria e verifica il chunking e la gestione dei metadati.

### Modello 6: Test di integrazione MCP

Il modulo MCP testa l'integrazione del Model Context Protocol usando il trasporto stdio. Questi test verificano che la tua applicazione possa avviare e comunicare con server MCP come processi figli.

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

## Filosofia del testing

Testa il tuo codice, non l'AI. I tuoi test dovrebbero validare il codice che scrivi controllando come i prompt sono costruiti, come la memoria è gestita e come gli strumenti vengono eseguiti. Le risposte AI variano e non dovrebbero far parte delle asserzioni di test. Chiediti se il tuo modello prompt sostituisce correttamente le variabili, non se l'AI dà la risposta giusta.

Usa i mock per i modelli linguistici. Sono dipendenze esterne lente, costose e non deterministiche. Il mocking rende i test veloci in pochi millisecondi invece di secondi, gratuiti senza costi API e deterministici con lo stesso risultato ogni volta.

Mantieni i test indipendenti. Ogni test dovrebbe impostare i propri dati, non dipendere da altri test e ripulire dopo di sé. I test dovrebbero passare indipendentemente dall'ordine di esecuzione.

Testa casi limite oltre il percorso ottimale. Prova input vuoti, input molto grandi, caratteri speciali, parametri non validi e condizioni limite. Questi spesso rivelano bug che l'uso normale non espone.

Usa nomi descrittivi. Confronta `shouldMaintainConversationHistoryAcrossMultipleMessages()` con `test1()`. Il primo ti dice esattamente cosa viene testato, facilitando molto il debug in caso di errori.

## Passi successivi

Ora che hai capito i modelli di test, approfondisci ciascun modulo:

- **[00 - Inizio rapido](../00-quick-start/README.md)** - Inizia con le basi dei modelli prompt
- **[01 - Introduzione](../01-introduction/README.md)** - Impara la gestione della memoria delle conversazioni
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Padroneggia i pattern di prompting GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Costruisci sistemi di generazione potenziata da retrieval
- **[04 - Tools](../04-tools/README.md)** - Implementa chiamate di funzione e concatenamento strumenti
- **[05 - MCP](../05-mcp/README.md)** - Integra il Model Context Protocol

Il README di ogni modulo fornisce spiegazioni dettagliate dei concetti qui testati.

---

**Navigazione:** [← Indietro al principale](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica AI [Co-op Translator](https://github.com/Azure/co-op-translator). Pur impegnandoci per garantire l’accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda la traduzione professionale umana. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->