# Pagsusuri ng mga LangChain4j Application

## Talaan ng Nilalaman

- [Mabilisang Pagsisimula](../../../docs)
- [Saklaw ng mga Pagsusuri](../../../docs)
- [Pagsasagawa ng mga Pagsusuri](../../../docs)
- [Pagsasagawa ng mga Pagsusuri sa VS Code](../../../docs)
- [Mga Pattern ng Pagsusuri](../../../docs)
- [Pilosopiya ng Pagsusuri](../../../docs)
- [Mga Susunod na Hakbang](../../../docs)

Ang gabay na ito ay dadalhin ka sa mga pagsusuri na nagpapakita kung paano magsuri ng mga AI application nang hindi nangangailangan ng mga API key o panlabas na serbisyo.

## Mabilisang Pagsisimula

Patakbuhin lahat ng pagsusuri gamit ang isang utos:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/tl/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Matagumpay na pagsasagawa ng pagsusuri na nagpapakita ng lahat ng pagsusuri ay pumasa na walang pagkabigo*

## Saklaw ng mga Pagsusuri

Ang kursong ito ay nakatuon sa **unit tests** na tumatakbo nang lokal. Bawat pagsusuri ay nagpapakita ng isang partikular na konsepto ng LangChain4j nang hiwalay.

<img src="../../../translated_images/tl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramide ng pagsusuri na nagpapakita ng balanse sa pagitan ng unit tests (mabilis, hiwalay), integration tests (mga totoong bahagi), at end-to-end tests. Ang pagsasanay na ito ay sumasaklaw sa unit testing.*

| Module | Mga Pagsusuri | Pagtutok | Pangunahing Files |
|--------|--------------|----------|-------------------|
| **00 - Quick Start** | 6 | Mga prompt template at pagpapalit ng variable | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Memorya ng pag-uusap at stateful chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Mga pattern ng GPT-5.2, antas ng kasigasigan, istrakturadong output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Pagkain ng dokumento, embeddings, paghahanap ng pagkakatulad | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Pagtawag ng function at pag-chain ng mga tool | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol gamit ang Stdio transport | `SimpleMcpTest.java` |

## Pagsasagawa ng mga Pagsusuri

**Patakbuhin lahat ng pagsusuri mula sa root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Patakbuhin ang mga pagsusuri para sa isang partikular na module:**

**Bash:**
```bash
cd 01-introduction && mvn test
# O mula sa ugat
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# O mula sa ugat
mvn --% test -pl 01-introduction
```

**Patakbuhin ang isang klase ng pagsusuri lamang:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Patakbuhin ang isang partikular na method ng pagsusuri:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#dapatPanatilihinAngKasaysayanNgPag-uusap
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#dapatPanatilihinAngKasaysayanNgUsapan
```

## Pagsasagawa ng Mga Pagsusuri sa VS Code

Kung gumagamit ka ng Visual Studio Code, nagbibigay ang Test Explorer ng grapikal na interface para sa pagpapatakbo at pag-debug ng mga pagsusuri.

<img src="../../../translated_images/tl/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test Explorer na nagpapakita ng puno ng pagsusuri na may lahat ng Java test classes at indibidwal na mga test methods*

**Para magpatakbo ng mga pagsusuri sa VS Code:**

1. Buksan ang Test Explorer sa pamamagitan ng pag-click sa beaker icon sa Activity Bar
2. Palawakin ang puno ng pagsusuri upang makita lahat ng mga module at test classes
3. I-click ang play button sa tabi ng anumang pagsusuri upang patakbuhin ito nang paisa-isa
4. I-click ang "Run All Tests" upang isakatuparan ang buong suite
5. I-right-click ang anumang pagsusuri at piliin ang "Debug Test" para mag-set ng breakpoints at mag-step sa code

Ipinapakita ng Test Explorer ang mga berdeng tsekmark para sa mga pumasa at nagbibigay ng detalyadong mensahe ng pagkabigo kapag may pumalya.

## Mga Pattern ng Pagsusuri

### Pattern 1: Pagsusuri ng Prompt Templates

Ang pinakasimpleng pattern ay sumusuri sa mga prompt template nang hindi tinatawagan ang anumang AI model. Tinitiyak mo na gumagana nang tama ang pagpapalit ng variable at ang mga prompt ay naka-format ayon sa inaasahan.

<img src="../../../translated_images/tl/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Pagsusuri ng mga prompt template na nagpapakita ng daloy ng pagpapalit ng variable: template na may placeholder → inilapat na mga halaga → beripikadong naka-format na output*

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

Ang pagsusuring ito ay nasa `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Patakbuhin ito:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#pagsubokSaPormatNgPromptTemplate
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#pagsubokSaPag-formatNgTemplateNgPrompt
```

### Pattern 2: Pagmomock ng mga Language Model

Kapag sinusuri ang logic ng pag-uusap, gumamit ng Mockito para gumawa ng pekeng modelo na nagbibigay ng mga naunang response. Ginagawa nitong mabilis, libre, at deterministic ang mga pagsusuri.

<img src="../../../translated_images/tl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Paghahambing na nagpapakita kung bakit mas gusto ang mga mock para sa pagsusuri: mabilis sila, libre, deterministic, at hindi nangangailangan ng API key*

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
        assertThat(history).hasSize(6); // 3 mensahe mula sa user + 3 mensahe mula sa AI
    }
}
```

Ang pattern na ito ay makikita sa `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Tinitiyak ng mock ang consistent na pag-uugali upang ma-verify mong gumagana nang tama ang memory management.

### Pattern 3: Pagsusuri ng Pag-iisa ng Pag-uusap

Dapat mapanatili ng conversation memory na hiwalay ang maraming gumagamit. Tinutiyak ng pagsusuring ito na hindi nagtatagpo ang mga konteksto ng mga pag-uusap.

<img src="../../../translated_images/tl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Pagsusuri ng pag-iisa ng pag-uusap na nagpapakita ng magkahiwalay na memory store para sa iba't ibang gumagamit upang maiwasan ang pagsasama ng konteksto*

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

Bawat pag-uusap ay nagpapanatili ng sarili nitong independiyenteng kasaysayan. Sa mga production system, mahalaga ang pag-iisang ito para sa mga multi-user na aplikasyon.

### Pattern 4: Pagsusuri ng Mga Tool nang Hiwa-hiwalay

Ang mga tool ay mga function na maaaring tawagan ng AI. Suriin sila nang diretso upang matiyak na gumagana nang tama kahit ano pa man ang desisyon ng AI.

<img src="../../../translated_images/tl/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Pagsusuri ng mga tool nang hiwalay na nagpapakita ng pagtakbo ng mock tool nang walang tawag sa AI upang beripikahin ang business logic*

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

Ang mga pagsusuring ito mula sa `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ay nagpapatunay ng logic ng tool nang walang involvement ng AI. Ang halimbawa ng pag-chain ay nagpapakita kung paano ang output ng isang tool ay nagiging input ng iba.

### Pattern 5: In-Memory RAG Testing

Tradisyonal na nangangailangan ang RAG systems ng vector databases at embedding services. Pinapayagan ka ng in-memory pattern na suriin ang buong pipeline nang walang panlabas na dependency.

<img src="../../../translated_images/tl/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*In-memory RAG testing workflow na nagpapakita ng pag-parse ng dokumento, pag-imbak ng embedding, at paghahanap ng pagkakatulad nang hindi nangangailangan ng database*

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

Ang pagsusuring ito mula sa `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ay lumilikha ng dokumento sa memorya at tinitiyak ang chunking at paghawak ng metadata.

### Pattern 6: MCP Integration Testing

Sinusuri ng MCP module ang integrasyon ng Model Context Protocol gamit ang stdio transport. Tinitiyak ng mga pagsusuring ito na kaya ng iyong application na mag-spawn at makipag-komunikasyon sa mga MCP server bilang subprocess.

Ang mga pagsusuri sa `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ay nagpapatunay ng pag-uugali ng MCP client.

**Patakbuhin ang mga ito:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Pilosopiya ng Pagsusuri

Suriin ang iyong code, hindi ang AI. Dapat patotohanan ng iyong mga pagsusuri ang code na sinusulat mo sa pamamagitan ng pagsuri kung paano itinatayo ang mga prompt, kung paano pinangangasiwaan ang memorya, at kung paano gumagana ang mga tool. Nag-iiba-iba ang mga sagot ng AI kaya hindi ito dapat bahagi ng mga assertion sa pagsusuri. Tanungin ang iyong sarili kung tama bang napapalitan ang mga variable sa prompt template, hindi kung tama ang sagot ng AI.

Gumamit ng mocks para sa mga language model. Sila ay panlabas na dependency na mabagal, mahal, at hindi deterministic. Ginagawang mabilis ng mocking ang mga pagsusuri na nasa milliseconds sa halip na segundo, libre nang walang gastos sa API, at deterministic na palaging pareho ang resulta.

Panatilihin ang pagiging malaya ng mga pagsusuri. Dapat ang bawat pagsusuri ay naghahanda ng sarili nitong data, hindi umaasa sa ibang pagsusuri, at naglilinis pagkatapos gawin. Dapat pumasa ang mga pagsusuri kahit ano pa man ang pagkakasunod-sunod ng pagpapatakbo.

Suriin ang mga edge case lampas sa masayang daan. Subukan ang mga walang laman na input, napakalalaking input, espesyal na mga karakter, hindi wastong mga parameter, at mga boundary condition. Madalas nitong naipapakita ang mga bug na hindi lumalabas sa normal na gamit.

Gumamit ng mga mapanglarawang pangalan. Ihambing ang `shouldMaintainConversationHistoryAcrossMultipleMessages()` sa `test1()`. Ang una ay nagsasabi nang eksakto kung ano ang sinusuri, ginagawang mas madali ang pag-debug ng mga pagkabigo.

## Mga Susunod na Hakbang

Ngayon na nauunawaan mo na ang mga pattern ng pagsusuri, sumisid nang mas malalim sa bawat module:

- **[00 - Quick Start](../00-quick-start/README.md)** - Magsimula sa mga batayan ng prompt template
- **[01 - Introduction](../01-introduction/README.md)** - Matutunan ang pamamahala ng memorya ng pag-uusap
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Maging bihasa sa mga pattern ng GPT-5.2 prompting
- **[03 - RAG](../03-rag/README.md)** - Bumuo ng retrieval-augmented generation systems
- **[04 - Tools](../04-tools/README.md)** - Ipatupad ang pagtawag ng function at pag-chain ng mga tool
- **[05 - MCP](../05-mcp/README.md)** - Isama ang Model Context Protocol

Ang README ng bawat module ay nagbibigay ng detalyadong paliwanag ng mga konseptong nasusuri dito.

---

**Navigation:** [← Bumalik sa Pangunahing Pahina](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang AI na serbisyo sa pagsasalin na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami para sa katumpakan, pakatandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o kamalian. Ang orihinal na dokumento sa sariling wika nito ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaintindihan o maling interpretasyon na nagmumula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->