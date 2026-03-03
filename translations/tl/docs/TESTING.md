# Pagsusuri ng LangChain4j Applications

## Talaan ng Nilalaman

- [Mabilis na Pagsisimula](../../../docs)
- [Mga Saklaw ng Pagsusuri](../../../docs)
- [Pagsasagawa ng Mga Pagsusuri](../../../docs)
- [Pagsasagawa ng Mga Pagsusuri sa VS Code](../../../docs)
- [Mga Pattern sa Pagsusuri](../../../docs)
- [Pilosopiya sa Pagsusuri](../../../docs)
- [Mga Susunod na Hakbang](../../../docs)

Ang gabay na ito ay nagpapakita kung paanong susuriin ang mga AI application nang hindi nangangailangan ng mga API key o panlabas na serbisyo.

## Quick Start

Patakbuhin lahat ng pagsusuri gamit ang isang utos:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Kapag pumasa ang lahat ng pagsusuri, makikita mo ang output tulad ng screenshot sa ibaba — walang nabigong pagsusuri.

<img src="../../../translated_images/tl/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Matagumpay na pagpapatakbo ng pagsusuri na nagpapakita na lahat ay pumasa na walang pagkabigo*

## Mga Saklaw ng Pagsusuri

Ang kursong ito ay nakatuon sa **unit tests** na tumatakbo nang lokal. Bawat pagsusuri ay nagpapakita ng isang tiyak na konsepto ng LangChain4j nang hiwalay. Ipinapakita ng testing pyramid sa ibaba kung saan nababagay ang unit tests — sila ang mabilis at maaasahang pundasyon para sa natitirang bahagi ng iyong test strategy.

<img src="../../../translated_images/tl/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Pyramid ng pagsusuri na nagpapakita ng balanse sa pagitan ng unit tests (mabilis, hiwalay), integration tests (tunay na mga bahagi), at end-to-end tests. Ang pagsasanay na ito ay tungkol sa unit testing.*

| Module | Mga Pagsusuri | Pokus | Pangunahing Mga File |
|--------|--------------|-------|---------------------|
| **00 - Quick Start** | 6 | Mga prompt template at variable substitution | `SimpleQuickStartTest.java` |
| **01 - Introduction** | 8 | Memory ng pag-uusap at stateful chat | `SimpleConversationTest.java` |
| **02 - Prompt Engineering** | 12 | Mga pattern ng GPT-5.2, antas ng eagerness, naayos na output | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Pagkuha ng dokumento, embeddings, paghahanap ng pagkakatulad | `DocumentServiceTest.java` |
| **04 - Tools** | 12 | Pagtawag ng function at pagkakabit-kabit ng tool | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol gamit ang Stdio transport | `SimpleMcpTest.java` |

## Pagsasagawa ng Mga Pagsusuri

**Patakbuhin lahat ng pagsusuri mula sa root:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Patakbuhin mga pagsusuri para sa isang partikular na module:**

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

**Patakbuhin ang isang klase ng pagsusuri:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Patakbuhin ang isang tiyak na pamamaraan ng pagsusuri:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#dapatPanatilihinAngKasaysayanNgPag-uusap
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#dapatPanatilihinAngKasaysayanNgPag-uusap
```

## Pagsasagawa ng Mga Pagsusuri sa VS Code

Kung gumagamit ka ng Visual Studio Code, ang Test Explorer ay nagbibigay ng grapikal na interface para sa pagpapatakbo at pag-debug ng mga pagsusuri.

<img src="../../../translated_images/tl/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer ng VS Code na nagpapakita ng puno ng pagsusuri kasama ang lahat ng Java test classes at mga indibidwal na test methods*

**Para patakbuhin ang mga pagsusuri sa VS Code:**

1. Buksan ang Test Explorer sa pamamagitan ng pag-click sa icon ng beaker sa Activity Bar
2. Palawakin ang puno ng pagsusuri para makita lahat ng module at test classes
3. I-click ang play button sa tabi ng anumang pagsusuri upang patakbuhin ito nang paisa-isa
4. I-click ang "Run All Tests" para patakbuhin ang buong suite
5. I-right-click ang anumang pagsusuri at piliin ang "Debug Test" upang mag-set ng breakpoints at i-step through ang code

Ipinapakita ng Test Explorer ang mga berdeng checkmark para sa mga pumasa na pagsusuri at nagbibigay ng detalyadong mga mensahe kapag may pumalpak.

## Mga Pattern sa Pagsusuri

### Pattern 1: Pagsusuri ng Prompt Templates

Ang pinakasimpleng pattern ay sinusuri ang mga prompt template nang hindi tumatawag ng AI model. Tinitiyak mo na gumagana nang maayos ang variable substitution at ang mga prompt ay naiuformat nang tama.

<img src="../../../translated_images/tl/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Pagsusuri ng mga prompt template na nagpapakita ng daloy ng variable substitution: template na may placeholders → inilalapat na mga halaga → na-verify na na-format na output*

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
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#pagsubokSaFormatNgPromptTemplate
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#pagsubokNgPromptTemplatePag-aayos
```

### Pattern 2: Pagmomock ng mga Language Model

Kapag sinusuri ang lohika ng pag-uusap, gamitin ang Mockito upang gumawa ng mga pekeng modelo na nagbabalik ng mga naunang idinagdag na tugon. Pinapabilis nito ang mga pagsusuri, libre, at tiyak ang resulta.

<img src="../../../translated_images/tl/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Paghahambing na nagpapakita kung bakit mas gusto ang mocks sa pagsusuri: mabilis sila, libre, tiyak ang resulta, at hindi nangangailangan ng API keys*

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

Ang pattern na ito ay makikita sa `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Tinitiyak ng mock ang pare-parehong kilos para ma-verify mo na maayos ang pamamahala ng memorya.

### Pattern 3: Pagsusuri ng Isolation ng Pag-uusap

Dapat panatilihing hiwalay ang memorya ng pag-uusap para sa maraming user. Sinasabing sinusuri nito na hindi nagkakahalo ang mga konteksto ng pag-uusap.

<img src="../../../translated_images/tl/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Pagsusuri ng isolation ng pag-uusap na nagpapakita ng magkakahiwalay na mga imbakan ng memorya para sa iba't ibang user upang maiwasan ang halo ng konteksto*

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

Bawat pag-uusap ay may sariling independiyenteng kasaysayan. Sa mga production system, kritikal ang ganitong isolation para sa mga multi-user application.

### Pattern 4: Pagsusuri ng Mga Tools Nang Nakahiwalay

Ang mga tool ay mga function na pwedeng tawagin ng AI. Subukan ang mga ito nang direkta para matiyak na gumagana sila nang maayos kahit hindi kasali ang AI sa mga desisyon.

<img src="../../../translated_images/tl/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Pagsusuri ng mga tool nang nakahiwalay na nagpapakita ng mock tool execution nang walang tawag sa AI upang ma-verify ang lohika ng negosyo*

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

Ang mga pagsusuring ito mula sa `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` ay nag-validate ng logika ng mga tool nang hindi kailangang tumawag sa AI. Ipinapakita ang halimbawa ng chaining kung paano ang output ng isang tool ay pinapasok sa input ng isa pa.

### Pattern 5: Pagsusuri ng In-Memory RAG

Kadalasang nangangailangan ang RAG ng mga vector database at embedding service. Pinapahintulutan ka ng in-memory pattern na subukan ang buong pipeline nang walang panlabas na dependencies.

<img src="../../../translated_images/tl/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Workflow ng in-memory RAG testing na nagpapakita ng pag-parse ng dokumento, storage ng embedding, at paghahanap ng pagkakatulad nang hindi kailangan ng database*

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

Ang pagsusuring ito mula sa `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` ay lumilikha ng dokumento sa memorya at tine-test ang chunking at metadata handling.

### Pattern 6: MCP Integration Testing

Sinusuri ng module ng MCP ang Model Context Protocol integration gamit ang stdio transport. Tine-test nito kung kaya ng iyong application na mag spawn at makipagkomunikasyon sa MCP servers bilang subprocesses.

Ang mga pagsusuri sa `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` ay nag-va-validate ng ugali ng MCP client.

**Patakbuhin sila:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Pilosopiya sa Pagsusuri

Suriin ang iyong code, hindi ang AI. Ang iyong mga pagsusuri ay dapat magpatunay sa code na sinulat mo sa pamamagitan ng pag-check kung paano ginagawa ang mga prompt, kung paano pinamamahalaan ang memorya, at kung paano gumagana ang mga tool. Nag-iiba-iba ang tugon ng AI kaya hindi ito nararapat sa mga test assertions. Tanungin ang sarili mo kung tama ba ang pagsasa-substitute ng mga variable sa prompt template, hindi kung tama ba ang sagot ng AI.

Gumamit ng mga mock para sa mga language model. Sila ay mga panlabas na dependencies na mabagal, mahal, at hindi tiyak. Pinapabilis ng mocking ang mga pagsusuri na tumatakbo sa millisecond imbes na segundo, libre nang walang gastos sa API, at deterministic na laging pareho ang resulta.

Panatilihing independyente ang mga pagsusuri. Dapat ang bawat pagsusuri ay maghanda ng sarili nitong data, hindi umaasa sa ibang pagsusuri, at naglilinis pagkatapos nito. Dapat pumasa ang mga pagsusuri anuman ang pagkakasunod-sunod ng pagpapatupad.

Suriin ang mga edge case lampas sa masayang ginagamit lang. Subukan ang empty inputs, sobrang laki ng inputs, espesyal na mga karakter, invalid na mga parameter, at mga boundary na kondisyon. Madalas itong magbunyag ng mga bug na hindi nakikita sa normal na paggamit.

Gumamit ng mga deskriptibong pangalan. Ihambing ang `shouldMaintainConversationHistoryAcrossMultipleMessages()` sa `test1()`. Masasabing eksakto kung ano ang susuriin ng una, kaya mas madali ang pag-debug kung may problema.

## Mga Susunod na Hakbang

Ngayong naintindihan mo na ang mga pattern ng pagsusuri, sumisid nang mas malalim sa bawat module:

- **[00 - Quick Start](../00-quick-start/README.md)** - Simulan sa mga batayan ng prompt template
- **[01 - Introduction](../01-introduction/README.md)** - Matutunan ang pamamahala ng memorya ng pag-uusap
- **[02 - Prompt Engineering](../02-prompt-engineering/README.md)** - Masterin ang mga pattern ng GPT-5.2 na prompt
- **[03 - RAG](../03-rag/README.md)** - Gumawa ng retrieval-augmented generation na mga sistema
- **[04 - Tools](../04-tools/README.md)** - Ipatupad ang pagpatawag ng function at pagkakabit-kabit ng tool
- **[05 - MCP](../05-mcp/README.md)** - Mag-integrate ng Model Context Protocol

Nagbibigay ang README ng bawat module ng detalyadong paliwanag tungkol sa mga konseptong sinuri dito.

---

**Navigation:** [← Bumalik sa Pangunahing Pahina](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Paalala**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsisikap kaming maging tumpak, mangyaring tandaan na maaaring may mga pagkakamali o hindi pagkakatugma ang mga awtomatikong salin. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na opisyal na sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na nagmumula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->