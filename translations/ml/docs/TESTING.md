# LangChain4j ആപ്ലിക്കേഷനുകളുടെ പരിശോധന

## ഉള്ളടക്ക പട്ടിക

- [വേഗത്തിലുള്ള തുടക്കം](../../../docs)
- [പരീക്ഷണങ്ങൾ എന്ത് ഉൾക്കൊണ്ടിരിക്കുന്നു](../../../docs)
- [പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കൽ](../../../docs)
- [VS കോഡിൽ പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കൽ](../../../docs)
- [പരീക്ഷണ മാതൃകകൾ](../../../docs)
- [പരിശോധന തത്ത്വം](../../../docs)
- [അടുത്ത ഘട്ടങ്ങൾ](../../../docs)

ഈ ഗൈഡ് API കീകൾ അല്ലെങ്കിൽ പുറത്തെ സേവനങ്ങൾ ആവാശ്യമില്ലാതെ എങ്ങനെ AI ആപ്ലിക്കേഷനുകൾ പരിശോധിക്കാമെന്ന് കാണിക്കുന്ന പരീക്ഷണങ്ങളിലൂടെ നിങ്ങളെ നയിക്കുന്നു.

## വേഗത്തിലുള്ള തുടക്കം

ഓർഡ് കമാൻഡ് ഉപയോഗിച്ച് എല്ലാ പരീക്ഷണങ്ങളും നടത്തുക:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

എല്ലാ പരീക്ഷണങ്ങളും വിജയകരമായി പാസായാൽ താഴെയുള്ള സ്ക്രീൻഷാട്ടിലെ പോലെ ഔട്ട്പുട്ട് കാണിക്കും — പരാജയങ്ങളില്ലാതെ പരീക്ഷണങ്ങൾ പ്രവർത്തിക്കും.

<img src="../../../translated_images/ml/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*സകല പരീക്ഷണങ്ങളും പരാജയങ്ങളില്ലാതെ പാസായതായി കാണിക്കുന്ന വിജയകരമായ പരീക്ഷണ നടപ്പാക്കൽ*

## പരീക്ഷണങ്ങൾ എന്ത് ഉൾക്കൊണ്ടിരിക്കുന്നു

ഈ കോഴ്‌സ് **യൂണിറ്റ് പരീക്ഷണങ്ങൾ** എന്നവയിൽ കേന്ദ്രീകരിക്കുന്നു, അവ ലൊക്കലായി പ്രവർത്തിക്കുന്നു. ഓരോ പരീക്ഷണവും വ്യക്തമായ ഒരു LangChain4j ആശയം വേർതിരിച്ചുവച്ച് കാണിക്കുന്നു. താഴെയുള്ള പരീക്ഷണ പിരമിഡ് കാണിക്കുന്നത് യൂണിറ്റ് പരിശോധനകൾ എവിടെയാണ് അനുയോജ്യമായ സ്ഥാനം എന്നതാണ് — ഇത് നിങ്ങൾ നിർമ്മിക്കുന്ന മറ്റു പരീക്ഷണ തന്ത്രങ്ങൾക്ക് വേഗതയേകുകയും വിശ്വസനീയമായ അടിസ്ഥാനം നല്കുകയും ചെയ്യുന്നു.

<img src="../../../translated_images/ml/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*യൂണിറ്റ് പരീക്ഷണങ്ങൾ (വേഗം, വേർതിരിഞ്ഞ) – ഇന്റഗ്രേഷൻ പരീക്ഷണങ്ങൾ (യഥാർത്ഥ ഘടകങ്ങൾ), എൻഡ്-ടു-എൻഡ് പരീക്ഷണങ്ങൾ എന്നവർ തമ്മിലുള്ള സാദൃശ്യം കാണിക്കുന്ന പരീക്ഷണ പിരമിഡ്. ഈ പരിശീലനത്തിൽ യൂണിറ്റ് ടെസ്റ്റിംഗ് ഉൾകാണിക്കുന്നു.*

| മോഡ്യൂൾ | പരീക്ഷണങ്ങൾ | ശ്രദ്ധ നൽക്കേണ്ടത് | പ്രധാന ഫയലുകൾ |
|--------|-------------|----------------|-----------------|
| **00 - വേഗത്തിലുള്ള തുടക്കം** | 6 | പ്രോംപ്റ്റ് ടെംപ്ലേറ്റുകളും ഭേദഗതി പദവങ്ങളുമ് | `SimpleQuickStartTest.java` |
| **01 - പരിചയം** | 8 | സംഭാഷണ ഓർമയും സ്റ്റേറ്റ്‌ഫുൾ ചാറ്റും | `SimpleConversationTest.java` |
| **02 - പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്** | 12 | GPT-5.2 മാതൃകകൾ, ഉത്സാഹത നിലകൾ, ഘടനയിലുളള ഔട്ട്പുട്ട് | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ഡോക്യുമെന്റ് ഇൻജെക്ഷൻ, എംബെഡിംഗുകൾ, സാമിത്ത്യമുള്ള തിരച്ചിൽ | `DocumentServiceTest.java` |
| **04 - ഉപകരണങ്ങൾ** | 12 | ഫംഗ്ഷൻ കോൾ ചെയ്യൽ, ഉപകരണം ചൈനിങ് | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ സ്റ്റ്ഡിയോ ട്രാൻസ്പോർട്ട് ഉപയോഗിച്ച് | `SimpleMcpTest.java` |

## പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കൽ

**റൂട്ട് നിന്ന് എല്ലാ പരീക്ഷണങ്ങളും നടത്തുക:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**ഏതെങ്കിലും പ്രത്യേക മോഡ്യൂളിനുള്ള പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കുക:**

**Bash:**
```bash
cd 01-introduction && mvn test
# അല്ലെങ്കിൽ റൂട്ട്‌ഇനു നിന്നുമായി
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# അല്ലെങ്കിൽ റൂട്ട് മുതൽ
mvn --% test -pl 01-introduction
```

**ഒരു ടെസ്റ്റ് ക്ലാസ് മാത്രം പ്രവർത്തിപ്പിക്കുക:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**ഒരു പ്രത്യേക ടെസ്റ്റ് മെഥഡ് പ്രവർത്തിപ്പിക്കുക:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#സംവാദ ചരിത്രം നിലനിർത്തേണ്ടതുണ്ടോ
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#സംഭാഷണ ചരിത്രം നിലനിർത്തണം
```

## VS കോഡിൽ പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കൽ

നിങ്ങൾ Visual Studio Code ഉപയോഗിച്ചാൽ, ടെസ്റ്റ് എക്സ്പ്ലോറർ പരീക്ഷണങ്ങൾ പ്രവർത്തിപ്പിക്കാനും ഡീബഗ് ചെയ്യാനും ഗ്രാഫിക്കൽ ഇന്റർഫേസ് നൽകുന്നു.

<img src="../../../translated_images/ml/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code ടെസ്റ്റ് എക്സ്പ്ലോറർ മുഴുവൻ ജാവ ടെസ്റ്റ് ക്ലാസുകളും വ്യക്തിഗത ടെസ്റ്റ് മെഥഡുകളും ഉൾക്കൊള്ളുന്ന ടെസ്റ്റ് ട്രീ കാണിക്കുന്നു*

**VS കോഡിൽ ടെസ്റ്റുകൾ പ്രവർത്തിപ്പിക്കാൻ:**

1. ഒരലയാളിയുള്ള അഭിനിവേശ ബാറിൽ കീമികോഷikon ക്ലിക്ക് ചെയ്ത് ടെസ്റ്റ് എക്സ്പ്ലോറർ തുറക്കുക
2. എല്ലാ മോഡ്യൂളുകളും ടെസ്റ്റ് ക്ലാസുകളും കാണാൻ ട്രീ വലിപ്പിപ്പിക്കുക
3. പ്രത്യേകമായി പ്രവർത്തിപ്പിക്കാൻ ഏതെങ്കിലും ടെസ്റ്റിന്റെ പക്കൽ പ്ലേ ബട്ടൺ ക്ലിക്ക് ചെയ്യുക
4. മുഴുവൻ സ്യൂട്ട് പ്രവർത്തിപ്പിക്കാൻ "Run All Tests" ക്ലിക്ക് ചെയ്യുക
5. ഏതെങ്കിലും ടെസ്റ്റ് റൈറ്റ്ക്ലിക്ക് ചെയ്ത് "Debug Test" തിരഞ്ഞെടുക്കുക, ബ്രേക്ക് പോയിന്റുകൾ സജ്ജമാക്കി കോഡിലൂടെ ഘട്ടം ചവിട്ടുന്നത്.

ടെസ്റ്റ് എക്സ്പ്ലോറർ വിജയിച്ച ടെസ്റ്റുക്കൾക്ക് പച്ച ടിക്കുകൾ കാണിക്കുകയും പരാജയപ്പെട്ടപ്പോൾ വിശദമായ പരാജയ സന്ദേശങ്ങൾ നൽകുകയും ചെയ്യുന്നു.

## പരിശോധന മാതൃകകൾ

### മാതൃക 1: പ്രോംപ്റ്റ് ടെംപ്ലേറ്റുകൾ പരിശോധിക്കൽ

ഏറ്റവും ലളിതമായ മാതൃക പ്രോംപ്റ്റ് ടെംപ്ലേറ്റുകൾ AI മോഡൽ ഉപയോഗിക്കാതെ പരിശോധിക്കുന്നു. വ്യത്യസ്ത പദങ്ങളുടെ സബ്സ്റ്റ്യൂഷൻ ശരിയാണോ എന്നും പ്രോംപ്റ്റുകൾ പ്രതീക്ഷിച്ചതുപോലെ ഫോർമാറ്റ് ചെയ്തിട്ടുളളതാണോ എന്നും നിങ്ങൾ உறപ്പാക്കുന്നു.

<img src="../../../translated_images/ml/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*പ്രോംപ്റ്റ് ടെംപ്ലേറ്റ് ടെസ്റ്റിംഗ് കാണിക്കുന്ന സബ്സ്റ്റ്യൂഷൻ പ്രവാഹം: പ്ലേസ്ഹോൾഡറുകൾ ഉള്ള ടെംപ്ലേറ്റ് → മൂല്യങ്ങൾ പ്രയോഗിച്ചു → ഫോർമാറ്റ് ചെയ്ത ഔട്ട്പുട്ട് പരിശോദിച്ചു*

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

ഈ ടെസ്റ്റ് `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` എന്ന സ്ഥലം ഉപയോഗിക്കുന്നു.

**നടത്തുക:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#ടെസ്റ്റ്‌പ്രാമ്പ്‌റൈലെറ്റ്‌ഫോർമാറ്റിംഗ്
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#പരിശോധന പ്രോംപ്റ്റ് ടെംപ്ലേറ്റ് ഫോർമാറ്റിംഗ്
```

### മാതൃക 2: ഭാഷാ മോഡലുകൾ മോക്ക് ചെയ്യൽ

സംഭാഷണ ലജിക് പരിശോധിക്കുമ്പോൾ, മുൻകൂട്ടി നിശ്ചിത പ്രതികരണങ്ങൾ നൽകുന്ന ഫേക്ക് മോഡലുകൾ സൃഷ്ടിക്കാൻ Mockito ഉപയോഗിക്കുക. ഇത് പരീക്ഷണം വേഗത്തിലും സൗജന്യത്തിലും നിശ്ചിതാനുസൃതവുമാക്കി മാറ്റുന്നു.

<img src="../../../translated_images/ml/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*പരിശോധനയ്ക്കായി മോക്കുകൾ ഏത് കൊണ്ട് മേൽപ്പെട്ടതാണ് എന്ന് കാണിക്കുന്ന താരതമ്യം: അവ വേഗത, സൗജന്യം, നിശ്ചിതാനുസൃതത എന്നിവ ഉറപ്പാക്കുന്നു, API കീകൾ ആവശ്യമായില്ല.*

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
        assertThat(history).hasSize(6); // 3 ഉപയോക്തൃ + 3 എഐ സന്ദേശങ്ങൾ
    }
}
```

ഈ മാതൃക `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` എന്നിൽ കാണാനാകും. മോക്ക് സ്ഥിരമായ പെരുമാറ്റം ഉറപ്പാക്കുന്നതുകൊണ്ട് ഓർമ ജനം ശരിയായി പ്രവർത്തിക്കുന്നുണ്ടോ എന്ന് പരിശോധിക്കാം.

### മാതൃക 3: സംഭാഷണ വേർതിരിച്ചുവയ്കൽ പരിശോധിക്കൽ

സംഭാഷണ ഓർമ പല ഉപയോക്താക്കളെയും പൂട്ടി വേർതിരിച്ചുവയ്ക്കണം. ഈ ടെസ്റ്റ് സംഭാഷണങ്ങൾ പരസ്പരം കൺടെക്സ്‌റ്റ് മിശ്രിതമാകാതിരിക്കണമെന്ന് ഉറപ്പാക്കുന്നു.

<img src="../../../translated_images/ml/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*വേണ്ടുന്ന അനുഭവം വേർതിരിച്ച് പകർത്തുന്നതാകാൻ ഓരോ ഉപയോക്താവിനും വേറിട്ട ഓർമ്മ സ്റ്റോറുകൾ ഉള്ളതായി കാണിക്കുന്ന സംഭാഷണ വേർതിരിച്ചുവയ്കൽ പരിശോധന*

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

ഓരോ സംഭാഷണവും സ്വതന്ത്രമായ ചരിത്രം സൂക്ഷിക്കുന്നു. ഉത്പാദന സംവിധാനങ്ങളിൽ ഈ വേർതിരിച്ചുവയ്കൽ ബഹു-ഉപയോക്തൃ ആപ്ലിക്കേഷനുകളിൽ അനിവാര്യമാണ്.

### മാതൃക 4: ഉപകരണങ്ങൾ സ്വതന്ത്രമായി പരിശോധിക്കൽ

ഉപകരണങ്ങൾ AI കോൾ ചെയ്യാവുന്ന ഫംഗ്ഷനുകളാണ്. AI തീരുമാനങ്ങളെ ബാധിക്കാതെ അവ ശരിയായി പ്രവർത്തിക്കുന്നുണ്ടോ എന്ന് നേരിട്ട് പരിശോധിക്കുക.

<img src="../../../translated_images/ml/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI കോൾ ഇല്ലാതെ മൊക്ക് ഉപകരണം പ്രവർത്തന ഉദാഹരണങ്ങൾ ഉപയോഗിച്ച് ബിസിനസ് ലജിക് ശരിയാണോ എന്ന് പരിശോധിക്കൽ*

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

`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` യിൽ നിന്നുള്ള ഈ ടെസ്റ്റുകൾ AI പങ്കാളിത്തമില്ലാതെ ഉപകരണ ലജിക് പരിശോധിക്കുന്നു. ചൈനിങ് ഉദാഹരണം ഒരു ഉപകരണത്തിന്റെ ഔട്ട്പുട്ട് മറ്റൊന്നിന്റെ ഇൻപുട്ടായി എങ്ങനെ സേവിക്കും എന്നും കാണിക്കുന്നു.

### മാതൃക 5: ഇൻ-മെമ്മറി RAG പരിശോധന

RAG സിസ്റ്റങ്ങൾ സാധാരണയായി വെക്റ്റർ ഡാറ്റാബേസുകളും എംബെഡിംഗ് സേവനങ്ങളും ആവശ്യപ്പെടുന്നു. ഇൻ-മെമ്മറി മാതൃക പുറത്തെ ആശ്രിതങ്ങളില്ലാതെ മുഴുവൻ പൈപ്പ്ലൈൻ പരീക്ഷിക്കാൻ അനുമതി നൽകുന്നു.

<img src="../../../translated_images/ml/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*ഡാറ്റാബേസ് ആവശ്യപ്പെടാതെ ഡോക്യുമെന്റ് പാഴ്സിംഗ്, എംബെഡിംഗ് സ്റ്റോറേജ്, സാമ്യതാ തിരച്ചിൽ എന്നിവയുടെ ഇൻ-മെമ്മറി RAG പരിശോധന പ്രവാഹം*

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

`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` എന്നിടത്തു നിന്നുള്ള ഈ ടെസ്റ്റ് ഓർമ്മയിൽ ഒരു ഡോക്യുമെന്റ് സൃഷ്ടിച്ച് ചങ്കിംഗ്, മെറ്റഡേറ്റ എൻട്രികൾ ശരിയായി കൈകാര്യം ചെയ്യുന്നതായി പരിശോധിക്കുന്നു.

### മാതൃക 6: MCP ഇന്റഗ്രേഷൻ പരീക്ഷണം

MCP മോഡ്യൂൾ stdio ട്രാൻസ്പോർട്ട് ഉപയോഗിച്ച് മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ ഇന്റഗ്രേഷൻ പരിശോധനകൾ നടത്തുന്നു. ഈ ടെസ്റ്റുകൾ നിങ്ങളുടെ ആപ്ലിക്കേഷൻ MCP സർവറുകൾ subprocess ആയി സ്ഫോണ് ചെയ്യുകയും സംവദിക്കുകയും ചെയ്യാൻ കഴിയും എന്ന് ഉറപ്പാക്കുന്നു.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` എന്നിടത്തെ ടെസ്റ്റുകൾ MCP ക്ലയന്റിന്റെ പെരുമാറ്റം സ്ഥിരീകരിക്കുന്നു.

**നടത്തുക:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## പരിശോധന തത്ത്വം

നിങ്ങളുടെ കോഡ് പരിശോധിക്കുക, AI അല്ല. നിങ്ങളുടെ പരീക്ഷണങ്ങൾ പ്രോംപ്റ്റുകൾ എങ്ങനെ സൃഷ്ടിക്കപ്പെടുന്നു, ഓർമ്മ എങ്ങനെ കൈകാര്യം ചെയ്യുന്നു, ഉപകരണങ്ങൾ എങ്ങനെ പ്രവർത്തിക്കുന്നു എന്നിവ പരിശോധിച്ച് എഴുത്തു കോഡ് ശരിയാണെന്ന് ഉറപ്പാക്കണം. AI പ്രതികരണങ്ങൾ വ്യത്യസ്തമായിരിക്കും, അവ പരിശോധനാ പ്രസ്താവനകളിൽ ഉൾപ്പെടുത്തിക്കരുത്. പ്രോംപ്റ്റ് ടെംപ്ലേറ്റ് ശരിയാണോ എന്നു മാത്രം പരിശോധിക്കുക, AI ശരിയായ ഉത്തരമാണ് നൽകുകയോ എന്നു അവലോകനം ചെയ്യരുത്.

ഭാഷാ മോഡലുകൾക്ക് മോക്കുകൾ ഉപയോഗിക്കുക. അവ പുറം ആശ്രിതങ്ങളാണ്, മന്ദഗതിയുള്ളവ, ചെലവേറിയവ, അനിശ്ചിതപരമായവ. മോക്കിംഗ് പരീക്ഷണങ്ങൾ സെക്കന്റുകൾക്ക് പകരം അല്പം മില്ലി സെക്കന്റുകളിൽ വേഗത്തിലും സൗജന്യത്തിലും, ഒരുപോലാനുള്ള ഫലത്തോടെ നടത്താൻ സഹായിക്കുന്നു.

പരീക്ഷണങ്ങൾ സ്വതന്ത്രമാക്കുക. ഓരോ പരീക്ഷണവും സ്വന്തം ഡാറ്റ ഒരുക്കണം, മറ്റുള്ള പരീക്ഷണങ്ങളിൽ ആശ്രയിച്ചിരിക്കരുത്, പരീക്ഷണം കഴിഞ്ഞ് സ്വയം ക്ലീൻഅപ്പ് ചെയ്‌തെടുക്കണം. പരീക്ഷണങ്ങൾ എദ്ദേഹയുമായി പ്രവർത്തിക്കുന്നു.

സന്തോഷകരമായ വഴിയभन्दा അപ്പുറം മൂല്യങ്ങൾ പരീക്ഷിക്കുക. ശൂന്യ ഇൻപുട്ടുകൾ, വളരെ വലുതായ ഇൻപുട്ടുകൾ, പ്രത്യേക ചിഹ്നങ്ങൾ, അസാധുവായ പാരാമീറ്ററുകൾ, അതിരുകൾ തുടങ്ങിയവ പരിശോധിക്കുക. ഇവ സാധാരണ ഉപയോഗത്തിൽ കാണാത്ത പിശകുകൾ കണ്ടെത്താൻ സഹായിക്കാം.

വിവരണപരമായ പേരുകൾ ഉപയോഗിക്കുക. `shouldMaintainConversationHistoryAcrossMultipleMessages()` എന്നത് `test1()`നെക്കാൾ നല്ലത്. ആദ്യത്തെ പേര് പരീക്ഷണം എന്താണെന്ന് സ്പഷ്ടമായി പറയുന്നു, പരാജയങ്ങൾ ന് ഡീബഗ്ഗ് ചെയ്യുന്നത് എളുപ്പമാക്കുന്നു.

## അടുത്ത ഘട്ടങ്ങൾ

ഇപ്പോൾ നിങ്ങൾ പരീക്ഷണ മാതൃകകളും മനസ്സിലാക്കിയതിനാൽ ഓരോ മോഡ്യൂളിലും കൂടുതൽ കാൽവെക്കുക:

- **[00 - വേഗത്തിലുള്ള തുടക്കം](../00-quick-start/README.md)** - പ്രോംപ്റ്റ് ടെംപ്ലേറ്റ് അടിസ്ഥാനങ്ങൾ ആരംഭിക്കുക
- **[01 - പരിചയം](../01-introduction/README.md)** - സംഭാഷണ ഓർമ്മ കൈകാര്യം ചെയ്യൽ പഠിക്കുക
- **[02 - പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](../02-prompt-engineering/README.md)** - GPT-5.2 പ്രോംപ്റ്റിംഗ് മാതൃകകൾ അറിയുക
- **[03 - RAG](../03-rag/README.md)** - റീട്രീവൽ-ഓഗ്‌മെന്റഡ് ജനറേഷന്റെ സിസ്റ്റങ്ങൾ നിർമ്മിക്കുക
- **[04 - ഉപകരണങ്ങൾ](../04-tools/README.md)** - ഫംഗ്ഷൻ കോൾ ചെയ്യലും ഉപകരണം പരമ്പരകളും നടപ്പിലാക്കുക
- **[05 - MCP](../05-mcp/README.md)** - മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോൾ ഇന്റഗ്രേറ്റ് ചെയ്യുക

ഓരോ മോഡ്യൂളിന്റെ README ഇവിടെ പരിശോധിക്കുന്ന ആശയങ്ങൾ വിശദമായി വിശദീകരിക്കുന്നു.

---

**നാവിഗേഷൻ:** [← മെയിനിലേക്കു മടങ്ങുക](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**പരിശോധനാ കുറിപ്പ്**:  
ഈ രേഖ AI വിവർത്തന സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്തതാണ്. ഞങ്ങൾ ശരിയായ വിവർത്തനത്തിനായി ശ്രമിക്കുന്നു എങ്കിലും, യന്ത്രം ചെയ്ത വിവർത്തനങ്ങളിൽ പിഴവുകളും അഥവാ തെറ്റുകളും ഉണ്ടാകാമെന്ന് ദയവായി ശ്രദ്ധിക്കുക. യഥാർത്ഥ രേഖ അതിന്റെ മാതൃഭാഷയിൽ തന്നെ അതിൻറെ പ്രാമാണിക ഉറവിടമായി കണക്കാക്കണം. ഏറ്റവും നിർണായകമായ വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ വിവർത്തനം നിർദ്ദേശിക്കുന്നു. ഈ വിവർത്തനത്തിന്റെ ഉപയോഗം മൂലം ഉണ്ടാകുന്ന ഏതെങ്കിലും തെറ്റിദ്ധാരണകൾക്കോ അല്ലെങ്കിൽ വ്യാഖ്യാന പിശക്‌സിനും ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->