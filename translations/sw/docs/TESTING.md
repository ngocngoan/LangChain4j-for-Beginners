# Kupima Programu za LangChain4j

## Jedwali la Yaliyomo

- [Kuanza Haraka](../../../docs)
- [Mavazi ya Vipimo](../../../docs)
- [Kukimbia Vipimo](../../../docs)
- [Kukimbia Vipimo katika VS Code](../../../docs)
- [Mifano ya Vipimo](../../../docs)
- [Falsafa ya Kupima](../../../docs)
- [Hatua Zinazofuata](../../../docs)

Mwongozo huu unakuongoza kupitia vipimo vinavyoonyesha jinsi ya kupima programu za AI bila kuhitaji funguo za API au huduma za nje.

## Kuanza Haraka

Kimbia vipimo vyote kwa amri moja:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Unapopitia vipimo vyote bila hitilafu, utaona matokeo kama picha iliyo hapa chini — vipimo vinafanyika bila makosa.

<img src="../../../translated_images/sw/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Utekelezaji wa mtihani uliofanikiwa unaonyesha vipimo vyote vikienda bila makosa*

## Mavazi ya Vipimo

Kozi hii inazingatia **vipimo vya kitengo** vinavyotekelezwa kwa ndani. Kila kipimo kinaonyesha dhana maalum ya LangChain4j pekee. Piramidi ya upimaji hapa chini inaonyesha wapi vipimo vya kitengo vinakaa — vinaunda msingi wa haraka na wa kuaminika ambao mkakati wako wa upimaji hujengwa juu yake.

<img src="../../../translated_images/sw/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramidi ya upimaji inayoonyesha usawa kati ya vipimo vya kitengo (haraka, vya peke), vipimo vya muunganiko (vitu halisi), na vipimo vya mwisho hadi mwisho. Mafunzo haya yanahusu upimaji wa kitengo.*

| Moduli | Vipimo | Lengo | Faili Muhimu |
|--------|--------|-------|--------------|
| **00 - Kuanza Haraka** | 6 | Mifano ya maagizo na uingizaji wa mabadiliko | `SimpleQuickStartTest.java` |
| **01 - Utangulizi** | 8 | Kumbukumbu ya mazungumzo na gumzo linaloendelea | `SimpleConversationTest.java` |
| **02 - Uhandisi wa Maagizo** | 12 | Mifumo ya GPT-5.2, viwango vya hamu, matokeo yaliyoandaliwa | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Kuingiza hati, embeddings, utafutaji wa ufanano | `DocumentServiceTest.java`` |
| **04 - Zana** | 12 | Kuitisha kazi na kuunganishwa kwa zana | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Itifaki ya Muktadha wa Mfano kwa usafirishaji wa stdio | `SimpleMcpTest.java` |

## Kukimbia Vipimo

**Kimbia vipimo vyote kutoka kwenye mzizi:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Kimbia vipimo vya moduli maalum:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Au kutoka mzizi
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Au kutoka mzizi
mvn --% test -pl 01-introduction
```

**Kimbia darasa moja la kipimo:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Kimbia njia moja ya kipimo:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#inapaswaKudumishaHistoriaYaMazungumzo
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#inapaswaKudumishaHistoriaYaMazungumzo
```

## Kukimbia Vipimo katika VS Code

Ikiwa unatumia Visual Studio Code, Test Explorer hutoa kiolesura cha picha kwa ajili ya kukimbia na kufuatilia vipimo.

<img src="../../../translated_images/sw/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer ya VS Code inaonyesha mti wa vipimo na madarasa yote ya vipimo ya Java pamoja na njia binafsi za vipimo*

**Ili kukimbia vipimo katika VS Code:**

1. Fungua Test Explorer kwa kubofya ikoni ya beaker katika Bar ya Shughuli
2. Panua mti wa vipimo kuona moduli zote na madarasa ya vipimo
3. Bofya kitufe cha kucheza pamoja na kipimo chochote kuikimbia chenyewe
4. Bofya "Run All Tests" ili kutekeleza seti yote
5. Bonyeza kitufe cha kulia kwenye kipimo na chagua "Debug Test" kuweka alama za kupumzika na kupitia nambari hatua kwa hatua

Test Explorer inaonyesha tiki za kijani kwa vipimo vinavyopita na kutoa ujumbe wa makosa marefu wakati vipimo vinaposhindwa.

## Mifano ya Vipimo

### Mfano 1: Kupima Mifano ya Maagizo

Mfano rahisi zaidi hupima mifano ya maagizo bila kuitisha mfano wa AI. Unathibitisha kwamba uingizaji wa mabadiliko unafanyika kwa usahihi na maagizo yameundwa kama inavyotarajiwa.

<img src="../../../translated_images/sw/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Kupima mifano ya maagizo ikionyesha mtiririko wa uingizaji wa mabadiliko: mfano wenye sehemu za ujazo → thamani zinaingizwa → matokeo yamehakikishwa*

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

Kipimo hiki kiko katika `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`.

**Kimbia:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#jaribioLaMfumoWaMwanzoWaMaandishi
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#jaribuMpangilioMfumoWaKiolezo
```

### Mfano 2: Kuiga Mifano ya Lugha

Unapopima mantiki ya mazungumzo, tumia Mockito kuunda mifano bandia inayorudisha majibu yaliyobainishwa. Hii hufanya vipimo kuwa vya haraka, bure, na vinavyotarajiwa.

<img src="../../../translated_images/sw/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Ulinganisho unaoonyesha kwa nini kuiga ni bora kwa vipimo: ni haraka, bure, vinavyotarajiwa, na havihitaji funguo za API*

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
        assertThat(history).hasSize(6); // Ujumbe 3 za mtumiaji + 3 za AI
    }
}
```

Mfano huu unaonekana katika `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Kuiga huthibitisha tabia thabiti ili uweze kuthibitisha usimamizi wa kumbukumbu unafanya kazi vizuri.

### Mfano 3: Kupima Upweke wa Mazungumzo

Kumbukumbu ya mazungumzo lazima ihifadhi watumiaji wengi kando. Kipimo hiki kinathibitisha kwamba mazungumzo hayaorodani muktadha.

<img src="../../../translated_images/sw/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Kupima upweke wa mazungumzo kuonyesha maduka ya kumbukumbu tofauti kwa watumiaji tofauti ili kuzuia mchanganyiko wa muktadha*

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

Mazungumzo kila moja yana historia yake huru. Katika mifumo ya uzalishaji, upweke huu ni muhimu kwa programu za watumiaji wengi.

### Mfano 4: Kupima Zana kwa Kujitegemea

Zana ni kazi ambazo AI inaweza kuziita. Zipime moja kwa moja kuhakikisha zinafanya kazi vizuri hata kama AI haijazihusisha.

<img src="../../../translated_images/sw/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Kupima zana kwa kujitegemea kuonyesha utekelezaji wa zana za kuiga bila kuitisha AI ili kuthibitisha mantiki ya biashara*

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

Vipimo hivi kutoka `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` vinathibitisha mantiki ya zana bila ushiriki wa AI. Mfano wa mlolongo unaonyesha jinsi matokeo ya zana moja yanavyoingia kama ingizo kwa nyingine.

### Mfano 5: Kupima RAG Ndani ya Kumbukumbu

Mifumo ya RAG kwa kawaida zinahitaji hifadhidata za vector na huduma za embedding. Mfano wa ndani ya kumbukumbu unakuwezesha kupima mchakato mzima bila tegemezi za nje.

<img src="../../../translated_images/sw/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Mkondo wa upimaji wa RAG ndani ya kumbukumbu unaonyesha uchambuzi wa hati, uhifadhi wa embedding, na utafutaji wa ufanano bila hitaji la hifadhidata*

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

Kipimo hiki kutoka `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` huchagua hati ndani ya kumbukumbu na kuangalia ugawaji na usimamizi wa metadata.

### Mfano 6: Upimaji wa Muunganiko wa MCP

Moduli ya MCP hupima muunganiko wa Itifaki ya Muktadha wa Mfano kwa kutumia usafirishaji wa stdio. Vipimo hivi vinaonyesha programu yako inaweza kuzalisha na kuwasiliana na seva za MCP kama michakato ndogo.

Vipimo katika `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` vinathibitisha tabia ya mteja MCP.

**Vikimbie:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Falsafa ya Kupima

Pima nambari yako, si AI. Vipimo vyako vinapaswa kuthibitisha nambari unayoandika kwa kusohoza jinsi maagizo yanavyotengenezwa, jinsi kumbukumbu inavyosimamiwa, na jinsi zana zinavyotekelezwa. Majibu ya AI hubadilika na hayapaswi kuwa sehemu ya uthibitisho wa kipimo. Jiulize kama mfano wako wa agizo unabadilisha mabadiliko ipasavyo, si kama AI inatoa jibu sahihi.

Tumia kuiga kwa mifano ya lugha. Ni tegemezi za nje ambazo ni polepole, ghali, na hazitajirudi. Kuiga hufanya vipimo kuwa vya haraka kwa milisekunde badala ya sekunde, bure bila gharama za API, na zinatarajiwa na matokeo sawa kila mara.

Hakikisha vipimo vina uhuru. Kila kipimo kinapaswa kujiandaa data zake, kisitegemee vipimo vingine, na kujisafisha baada ya kukimbia. Vipimo vinapaswa kupita bila kujali mpangilio wa utekelezaji.

Pima matukio ya pekee zaidi ya njia ya furaha. Jaribu pembejeo tupu, pembejeo kubwa sana, herufi maalum, vigezo batili, na masharti ya mipaka. Haya mara nyingi huonyesha kasoro ambazo matumizi ya kawaida hayaziwezi kuzijua.

Tumia majina ya kueleza. Linganisha `shouldMaintainConversationHistoryAcrossMultipleMessages()` na `test1()`. Kwanza linaelezea hasa kinachopimwa, na kufanya utambuzi wa kasoro kuwa rahisi sana.

## Hatua Zinazofuata

Sasa unapoelewa mifano ya vipimo, chunguza zaidi moduli kila moja:

- **[00 - Kuanza Haraka](../00-quick-start/README.md)** - Anza na msingi wa mifano ya maagizo
- **[01 - Utangulizi](../01-introduction/README.md)** - Jifunze usimamizi wa kumbukumbu ya mazungumzo
- **[02 - Uhandisi wa Maagizo](../02/prompt-engineering/README.md)** - Jifundie mifumo ya kukuza GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Tengeneza mifumo ya kizazi iliyoboreshwa kwa urejeshaji
- **[04 - Zana](../04-tools/README.md)** - Tekeleza kuitisha kazi na mlolongo wa zana
- **[05 - MCP](../05-mcp/README.md)** - Unganisha Itifaki ya Muktadha wa Mfano

Kila README ya moduli hutoa maelezo ya kina ya dhana zinazopimwa hapa.

---

**Mwelekeo:** [← Rudi Kwenye Kuu](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kiarifi**:  
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za moja kwa moja zinaweza kuwa na makosa au kutokukamilika. Nyaraka ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na mtu inashauriwa. Hatutojibu kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->