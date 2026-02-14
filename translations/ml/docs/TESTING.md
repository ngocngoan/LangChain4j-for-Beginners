# LangChain4j ആപ്ലിക്കേഷനുകൾ ടെസ്റ്റുചെയ്യൽ

## ഉള്ളടക്കങ്ങളുടെ പട്ടിക

- [ക്വിക്ക് സ്റ്റാർട്ട്](../../../docs)
- [ടെസ്റ്റുകൾ 무엇് കവരുന്നു](../../../docs)
- [ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കൽ](../../../docs)
- [VS കോഡിൽ ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കൽ](../../../docs)
- [ടെസ്റ്റിംഗ് പാറ്റേൺസ്](../../../docs)
- [ടെസ്റ്റിംഗ് തത്ത്വം](../../../docs)
- [അടുത്ത ഘട്ടങ്ങൾ](../../../docs)

API കീകൾ അല്ലെങ്കിൽ ബാഹ്യ സേവനങ്ങൾ ആവശ്യമില്ലാതെ AI ആപ്ലിക്കേഷനുകൾ എങ്ങിനെ ടെസ്റ്റ് ചെയ്യാമെന്നും കാണിക്കുന്ന ടെസ്റ്റുകൾ വഴി ഈ ഗൈഡ് നിങ്ങളെ നടത്തുന്നു.

## ക്വിക്ക് സ്റ്റാർട്ട്

ഏറ്റവും ലളിതമായ കമാൻഡുമായി എല്ലാ ടെസ്റ്റുകളും റൺ ചെയ്യുക:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ml/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*എല്ലാ ടെസ്റ്റുകളും പരാജയമില്ലാതെ വിജയകരമായി പ്രവർത്തിക്കുന്ന ഫലം*

## ടെസ്റ്റുകൾ 무엇് കവരുന്നു

ഈ കോഴ്‌സ് പ്രാദേശികമായി പ്രവർത്തിക്കുന്ന **യുണിറ്റ് ടെസ്റ്റുകൾ**-ൽ ശ്രദ്ധ കേന്ദ്രീകരിക്കുന്നു. ഓരോ ടെസ്റ്റും ഒരു പ്രത്യേക LangChain4j ആശയം വേർതിരിച്ച് കാണിക്കുന്നു.

<img src="../../../translated_images/ml/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*ഉയർന്ന വേഗതയിൽ, വേർതിരിച്ചുള്ള, യുണിറ്റ് ടെസ്റ്റുകളും യഥാർത്ഥ ഘടകങ്ങളോടുള്ള ഇൻറഗ്രേഷൻ ടെസ്റ്റുകളും, അവസാനഭാഗം വരെ ടെസ്റ്റുകളും തമ്മിലുള്ള സമത്വം കാണിക്കുന്ന ടെസ്റ്റിംഗ് പിരമിഡ്. ഈ പരിശീലനം യുണിറ്റ് ടെസ്റ്റിംഗിനെയാണ് ഉൾക്കൊള്ളുന്നത്.*

| മോഡ്യൂൾ | ടെസ്റ്റുകൾ | പ്രധാന ശ്രദ്ധ | പ്രധാന ഫയലുകൾ |
|--------|-------|-------------|-----------|
| **00 - ക്വിക്ക് സ്റ്റാർട്ട്** | 6 | പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റുകളും വേരിയബിൾ പ്രതിസന്ധി | `SimpleQuickStartTest.java` |
| **01 - ആമുഖം** | 8 | സംവാദ മെമ്മറി, അവസ്ഥാപരമായ ചാറ്റ് | `SimpleConversationTest.java` |
| **02 - പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്** | 12 | GPT-5.2 പാറ്റേൺസ്, ആവേശനില, ഘടിത ഔട്ട്പുട്ട് | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ഡോക്യുമെന്റ് ഇൻജെക്ഷൻ, എംബെഡ്ഡിംഗ്, സമാനതാന്വേഷണം | `DocumentServiceTest.java` |
| **04 - ടൂൾസ്** | 12 | ഫംഗ്ഷൻ കോളിംഗ്, ടൂൾ ചെയിനിംഗ് | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ stdio ട്രാൻസ്പോർട്ടോടുകൂടെ | `SimpleMcpTest.java` |

## ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കൽ

**റൂട്ട് ഡയറക്ടറിയിൽ നിന്ന് എല്ലാ ടെസ്റ്റുകളും പ്രവർത്തിപ്പിക്കുക:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ഒരു പ്രത്യേക മോഡ്യൂളിന്റെ ടെസ്റ്റുകൾ റൺ ചെയ്യുക:**

**Bash:**
```bash
cd 01-introduction && mvn test
# അല്ലെങ്കിൽ റൂട്ട് മുതൽ
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# അല്ലെങ്കില്‍ റൂട്ട് നിന്ന്
mvn --% test -pl 01-introduction
```

**ഒരു പ്രത്യേക ടെസ്റ്റ് ക്ലാസ് റൺ ചെയ്യുക:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ഒരു പ്രത്യേക ടെസ്റ്റ് മെത്തഡ് റൺ ചെയ്യുക:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#സംഭാഷണ ചരിത്രം നിലനിർത്തേണ്ടതാണ്
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#സംഭാഷണ ചരിത്രം നിലനിർത്തണം
```

## VS കോഡിൽ ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കൽ

Visual Studio Code ഉപയോഗിക്കുന്നുവെങ്കിൽ, Test Explorer ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കുന്നതിനും ഡീബഗ് ചെയ്യുന്നതിനും ഗ്രാഫിക്കൽ ഇന്റർഫേസ് നൽകുന്നു.

<img src="../../../translated_images/ml/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS കോഡ് ടെസ്റ്റ് എക്സ്പ്ലോറർ, എല്ലാ ജാവ ടെസ്റ്റ് ക്ലാസ്സുകളും വ്യക്തിഗത ടെസ്റ്റ് മെത്തഡുകളുമായ ട്രീ കാണിക്കുന്നു*

**VS കോഡിൽ ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കാൻ:**

1. ആക്ടിവിറ്റി ബാറിൽ ബീക്കർ ഐക്കൺ ക്ലിക്ക് ചെയ്ത് Test Explorer തുറക്കുക
2. എല്ലാ മോഡ്യൂളുകളും ടെസ്റ്റ് ക്ലാസ്സുകളും കാണാനായി ടെസ്റ്റ് ട്രീ വലുതാക്കുക
3. ഒരു ടെസ്റ്റ് കൂടെ പ്ലേ ബട്ടൺ ക്ലിക്ക് ചെയ്ത് അതിനെ വ്യത്യസ്തമായി റൺ ചെയ്യുക
4. "Run All Tests" ക്ലിക്ക് ചെയ്ത് മുഴുവൻ സ്യൂട്ട് പ്രവർത്തിപ്പിക്കുക
5. ഏതെങ്കിലും ടെസ്റ്റ് റൈറ്റ് ക്ലിക്ക് ചെയ്ത് "Debug Test" തിരഞ്ഞെടുക്കുക, ബ്രേക്ക്‌പോയിന്റുകൾ സെറ്റ് ചെയ്ത് കോഡ് ഘട്ടം ഘട്ടം പരിശോധിക്കാം

ടെസ്റ്റ് എക്സ്പ്ലോറർ പാസായ ടെസ്റ്റുകൾക്കായി പച്ച ടിക്ക് കാണിക്കുന്നു, പരാജയപ്പെട്ടപ്പോൾ വിശദമായ പരാജയ സന്ദേശങ്ങൾ നൽകുന്നു.

## ടെസ്റ്റിംഗ് പാറ്റേൺസ്

### പാറ്റേൺ 1: പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റുകൾ ടെസ്റ്റുചെയ്യൽ

ഏറ്റവും ലളിതമായ പാറ്റേൺ AI മോഡൽ വിളിക്കാതെ പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റുകൾ ടെസ്റ്റ് ചെയ്യുകയാണ്. വേരിയബിൾ പ്രതിസന്ധി ശരിയായി പ്രവർത്തിക്കുന്നുണ്ടോ എന്നും, പ്രൊംപ്റ്റുകൾ പ്രതീക്ഷിച്ച ഫോർമാറ്റിൽ ആണോ എന്നും നിങ്ങൾ പരിശോധിക്കുന്നു.

<img src="../../../translated_images/ml/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റുകൾ ടെസ്റ്റുചെയ്യുന്നത് variable substitution ഘട്ടം → മൂല്യങ്ങൾ പ്രയോഗിക്കൽ → ഫോർമാറ്റ് ചെയ്ത ഔട്ട്‌പുട്ട് സ്ഥിരീകരിക്കൽ*

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

ഈ ടെസ്റ്റ് `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`-ൽ ഉണ്ട്.

**ഇത് റൺ ചെയ്യുക:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ടെസ്റ്റ് പ്രോംപ്റ്റ് ട ем്പ്‌ലെറ്റ് ഫോർമാറ്റിംഗ്
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#ടെസ്റ്റ് പ്രോംപ്റ്റ് ടെംപ്ലേറ്റിന്റെ രൂപരേഖ
```

### പാറ്റേൺ 2: ഭാഷാ മോഡലുകൾ മോക്കിംഗ്

സംവാദ ലാജിക് ടെസ്റ്റ് ചെയ്യുമ്പോൾ, Mockito ഉപയോഗിച്ച് നിശ്ചിത പ്രതികരണങ്ങൾ നൽകുന്ന മോഷ്ടിച്ച മോഡലുകൾ സൃഷ്ടിക്കുക. ഇത് ടെസ്റ്റുകൾ വേഗത്തിലുള്ളതും സൗജന്യവും നിർണായകവുമാക്കി മാറ്റുന്നു.

<img src="../../../translated_images/ml/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*ടെസ്റ്റിംഗിന് മോകുകൾ മുൻഗണന ലഭിക്കുന്നതു എന്തെന്ന വിശദീകരണം: വേഗം, സൗജന്യം, നിർണായകത, API കീകൾ ആവശ്യമില്ല*

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
        assertThat(history).hasSize(6); // 3 ഉപയോക്തൃ + 3 AI സന്ദേശങ്ങൾ
    }
}
```

ഈ പാറ്റേൺ `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`-ൽ കാണാം. മോക്ക് സ്ഥിരതയുള്ള പെരുമാറ്റം ഉറപ്പാക്കുന്നു, അതിനാൽ മെമ്മറി മാനേജ്മെന്റ് ശരിയായി പ്രവർത്തിക്കുന്നുവെന്ന് നിങ്ങൾ പരിശോധിക്കാം.

### പാറ്റേൺ 3: Conversation Isolation ടെസ്റ്റുചെയ്യൽ

സംവാദ മെമ്മറി പല ഉപഭോക്താക്കളെ വേർതിരിച്ച് സൂക്ഷിക്കണം. ഈ ടെസ്റ്റ് സംവാദങ്ങൾ പരസ്പരം	context മിശ്രിതമാകാതെ വച്ചിരിക്കുന്നുവോ എന്ന് പരിശോധിക്കുന്നു.

<img src="../../../translated_images/ml/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Conversation isolation ടെസ്റ്റിംഗ്, വിവിധ ഉപയോക്താക്കൾക്കായി വേറിട്ട മെമ്മറി സ്റ്റോറുകൾ context മിശ്രിതം തടയാൻ*

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

ഓരോ സംവാദത്തിനും സ്വതന്ത്രമായ ചരിത്രം ഉണ്ട്. പ്രൊഡക്ഷൻ സിസ്റ്റങ്ങളിൽ, ഇത് മൾട്ടി-ഉപയോക്തൃ ആപ്ലിക്കേഷനുകൾക്കായും വളരെ പ്രധാനമാണ്.

### പാറ്റേൺ 4: ടൂളുകൾ സ്വതന്ത്രമായി ടെസ്റ്റ് ചെയ്യൽ

ടൂളുകൾ AI വിളിക്കാൻ ഉപയോഗിക്കുന്ന ഫംഗ്ഷനുകളാണ്. AI തീരുമാനം ഇല്ലാതെയുള്ള ബിസിനസ് ലാജിക്ക് ശരിയായി പ്രവർത്തിക്കുന്നുണ്ടോ എന്ന് നേരിട്ട് ടെസ്റ്റ് ചെയ്യുക.

<img src="../../../translated_images/ml/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI കോൾ ഇല്ലാതെ മോക് ടൂൾ പ്രവർത്തനം ടെസ്റ്റ് ചെയ്തത് ബിസിനസ് ലാജിക്ക് പരിശോധിക്കുന്നതിന്*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`-ൽ വരുന്ന ഈ ടെസ്റ്റുകൾ AI പങ്കാളിത്തമില്ലാതെ ടൂൾ ലാജിക്ക് ശരിയാണെന്ന് എളുപ്പത്തിൽ പരിശോധിക്കുന്നു. ചെയിനിംഗ് ഉദാഹരണം ഒരു ടൂളിന്റെ ഔട്ട്പുട്ട് മറ്റൊരാളുടെ ഇന്‍പുട്ട് ആകുന്നു എന്നുതന്നെയാണ്.

### പാറ്റേൺ 5: ഇൻ-മെമ്മറി RAG ടെസ്റ്റിംഗ്

RAG സിസ്റ്റങ്ങൾ സാധാരണയായി വെക്ടർ ഡാറ്റാബേസും എംബെഡ്ഡിംഗ് സേവനങ്ങളും ആവശ്യമാണ്. ഇൻ-മെമ്മറി പാറ്റേൺ മാർഗ്ഗത്തിൽ, നിങ്ങൾ പൈപ്പ്ലൈൻ മുഴുവനും ബാഹ്യ ആശ്രിതത്വങ്ങളില്ലാതെ ടെസ്റ്റ് ചെയ്യാം.

<img src="../../../translated_images/ml/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ഡാറ്റാബേസ് ആവശ്യമില്ലാതെ ഡോക്യുമെന്റ് പാർസിംഗ്, എംബെഡ്ഡിംഗ് സ്ടോറേജ്, സമാനതാന്വേഷണം Workflow*

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

`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`-ിൽ നിന്നുള്ള ഈ ടെസ്റ്റ്, മെമ്മറിയിൽ ഡോക്യുമെന്റ് സൃഷ്ടിക്കുകയും ചങ്കിംഗ് മെറ്റാഡാറ്റ കൈകാര്യം ചെയ്യുന്നതിനും സാധിക്കുന്നു എന്ന് പരിശോധിക്കുന്നു.

### പാറ്റേൺ 6: MCP ഇൻറഗ്രേഷൻ ടെസ്റ്റിംഗ്

MCP മോഡ്യൂൾ മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ stdio ട്രാൻസ്പോർട്ടിനോടൊപ്പം ഇൻറഗ്രേറ്റ് ചെയ്യുന്നതായി ടെസ്റ്റു ചെയ്യുന്നു. ഈ ടെസ്റ്റുകൾ subprocess ആയി MCP സർവറുകളുമായി ആശയവിനിമയം നടത്തുന്നതും സ്പോൺ ചെയ്ത് പ്രവർത്തിപ്പിക്കുന്നതും പരിശോധിക്കുന്നു.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java`-ലെ ടെസ്റ്റുകൾ MCP ക്ലയന്റ് പെരുമാറ്റം സ്ഥിരീകരിക്കുന്നു.

**ഇവ പ്രവർത്തിപ്പിക്കുക:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## ടെസ്റ്റിംഗ് തത്ത്വം

നിങ്ങളുടെ കോഡ് ടെസ്റ്റ് ചെയ്യുക, AI അല്ല. പ്രൊംപ്റ്റുകൾ എങ്ങിനെ നിർമ്മിക്കപ്പെടുന്നു, മെമ്മറി എങ്ങിനെയാണ് കൈകാര്യം ചെയ്യുന്നത്, ടൂളുകൾ എങ്ങിനെ പ്രവർത്തിക്കുന്നു എന്നതിൽ നിങ്ങളുടെ ടെസ്റ്റുകൾ ഉറപ്പു വരുത്തണം. AI അവബോധങ്ങൾ വ്യത്യാസമുള്ളവയാണ്, അതുകൊണ്ട് ടെസ്റ്റ് അസർഷനുകളിൽ അവ ഉൾപ്പെടുത്തൽ വേണ്ടത് അല്ല. നിങ്ങളുടെ പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റ് വേരിയബിൾസ് ശരിയായി പതിപ്പിത്യമാണോ എന്നറിയുക, AI ശരിയായ ഉത്തരമാണ് നൽകുന്നതോ എന്ന് മാത്രം നോക്കാതെ.

ഭാഷാ മോഡലുകൾക്ക് മോകുകൾ ഉപയോഗിക്കുക. അവ ബാഹ്യ ആശ്രിതങ്ങൾ ആണ്, മോശം വേഗം, ചെലവേറിയതും നിർണായകമല്ലാത്തതും ആണ്. മോകിംഗ് മില്ലിസെക്കന്റുകളിൽ വേഗതകളും സൗജന്യവും നിർണായകവും ആക്കുന്നു.

ടെസ്റ്റുകൾ സ്വതന്ത്രമാക്കുക. ഓരോ ടെസ്റ്റും തന്നെ സ്വന്തം ഡാറ്റ ഒരുക്കണം, മറ്റ് ടെസ്റ്റുകൾ ആശ്രയിക്കാതെ, പ്രവർത്തനക്രമം മാറ്റിയാലും പാസാകണം.

സന്തോഷകരമായ പാതയ്ക്ക് പുറമേ എഡ്ജ് കേസുകളും ടെസ്റ്റ് ചെയ്യുക. ശൂന്യ ഇൻപുട്ടുകൾ, വലുതായ ഇൻപുട്ടുകൾ, പ്രത്യേക അക്ഷരം, അസാധുവായ പാരാമീറ്ററുകൾ, അതിരുകൾ എന്നിവ പരീക്ഷിക്കുക. സാധാരണ ഉപയോഗത്തിൽ കണ്ടെത്താനാകാത്ത ബഗുകൾ ഇതോടെ കണ്ടെത്താനാകും.

വിവരണാത്മക പേരുകൾ ഉപയോഗിക്കുക. `shouldMaintainConversationHistoryAcrossMultipleMessages()` എന്നത് `test1()` നെക്കാള്‍ സ്പষ্টമാണ്. ഇതോടെ തെറ്റുകൾ കണ്ടെത്തലും സുഷ്ടമാണാവും.

## അടുത്ത ഘട്ടങ്ങൾ

ഇപ്പോൾ ടെസ്റ്റിംഗ് പാറ്റേണുകളും മനസ്സിലാക്കിയതിനാൽ, ഓരോ മോഡ്യൂളിനും കൂടുതൽ ആഴത്തിൽ പ്രവേശിക്കുക:

- **[00 - ക്വിക്ക് സ്റ്റാർട്ട്](../00-quick-start/README.md)** - പ്രൊംപ്റ്റ് ടെംപ്ലേറ്റ് അടിസ്ഥാനങ്ങൾ ആരംഭിക്കുക
- **[01 - ആമുഖം](../01-introduction/README.md)** - സംവാദ മെമ്മറി മാനേജ്മെന്റ് പഠിക്കുക
- **[02 - പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](../02-prompt-engineering/README.md)** - GPT-5.2 പ്രൊംപ്റ്റിംഗ് പാറ്റേണുകൾ പഠിക്കുക
- **[03 - RAG](../03-rag/README.md)** - റിട്രീവൽ-ഓഗ്‌മെന്റഡ് ജനറേഷൻ സിസ്റ്റങ്ങൾ നിർമ്മിക്കുക
- **[04 - ടൂൾസ്](../04-tools/README.md)** - ഫംഗ്ഷൻ കോൾ ചെയ്യലും ടൂൾ ചെയിനുകളും നടപ്പിലാക്കുക
- **[05 - MCP](../05-mcp/README.md)** - മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ ഇൻറഗ്രേറ്റ് ചെയ്യുക

ഓരോ മോഡ്യൂളിന്റെയും README ഇവിടെ ടെസ്റ്റ് ചെയ്ത ആശയങ്ങളുടെ വിശദമായ വിശദീകരണങ്ങൾ നൽകുന്നു.

---

**നവിഗേഷൻ:** [← പ്രധാനത്തിലേക്ക് മടങ്ങുക](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**നിഷേധം**:  
ഈ രേഖ AI വിവർത്തന സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്‍തതാണ്. നാം തുൽത്യതയ്ക്ക് ശ്രമിച്ചിട്ടുള്ളതെങ്കിലും, സ്വയംചാലിത വിവർത്തനങ്ങളിൽ പിഴവുകൾ അല്ലെങ്കിൽ തെറ്റായ വിവരങ്ങൾ ഉണ്ടായിരിക്കാം എന്ന് ദയവായി ശ്രദ്ധിക്കുക. അതിന്റെ പോലെയുള്ള ഉറവിട ഭാഷയിലെ യഥാർത്ഥ രേഖയെ ഔദ്യോഗിക ഉറവിടമായി കണക്കാക്കണമെന്ന് അഭ്യർഥിക്കുന്നു. നിർണായക വിവരങ്ങൾക്ക് പ്രൊഫഷണൽ മാനുഷിക വിവർത്തനം ശിപാർശ ചെയ്യപ്പെടുന്നു. ഈ വിവർത്തനത്തിന്‍റെ ഉപയോഗത്തിൽ നിന്നുണ്ടാകാവുന്ന ബോധക്കുറവുകൾക്കും തെറ്റിദ്ധാരണകൾക്കും ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->