# Kupima Programu za LangChain4j

## Yaliyomo

- [Anza Haraka](../../../docs)
- [Kile Vipimo Vinavyogusa](../../../docs)
- [Kuendesha Vipimo](../../../docs)
- [Kuendesha Vipimo Katika VS Code](../../../docs)
- [Mifumo ya Kupima](../../../docs)
- [Falsafa ya Kupima](../../../docs)
- [Hatua Zinazofuata](../../../docs)

Mwongozo huu unakuongoza kupitia vipimo vinavyoonyesha jinsi ya kupima programu za AI bila kuhitaji funguo za API au huduma za nje.

## Anza Haraka

Endesha vipimo vyote kwa amri moja:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/sw/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*Uendeshaji wa mafanikio wa vipimo unaoonyesha vipimo vyote vikiwa vinafaulu bila kushindwa*

## Kile Vipimo Vinavyogusa

Kozi hii inalenga katika **vipimo vya kitengo** vinavyotekelezwa mahali hapa. Kila kipimo kinaonyesha dhana maalum ya LangChain4j pekee.

<img src="../../../translated_images/sw/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*Piramidi ya kupima ikionyesha uwiano kati ya vipimo vya kitengo (haraka, vipangwa), vipimo vya muunganiko (vipande halisi), na vipimo vya mwisho-mwisho. Mafunzo haya yanahusu kupima kitengo.*

| Moduli | Vipimo | Lengo | Faili Muhimu |
|--------|-------|-------|-----------|
| **00 - Anza Haraka** | 6 | Mifano ya maelekezo na kubadilisha vigezo | `SimpleQuickStartTest.java` |
| **01 - Utangulizi** | 8 | Kumbukumbu ya mazungumzo na mazungumzo yenye hali | `SimpleConversationTest.java` |
| **02 - Uhandisi wa Maelekezo** | 12 | Mifumo ya GPT-5.2, viwango vya hamu, matokeo yaliyopangwa | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Uingizaji wa hati, kuweka alama, utafutaji wa ufanano | `DocumentServiceTest.java` |
| **04 - Zana** | 12 | Kupiga nyaraka na mfuatano wa zana | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Itifaki ya Muktadha wa Mfano na usafirishaji wa Stdio | `SimpleMcpTest.java` |

## Kuendesha Vipimo

**Endesha vipimo vyote kutoka mzizi:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Endesha vipimo kwa moduli maalum:**

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

**Endesha darasa moja la kipimo:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Endesha njia moja ya kipimo:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#inapaswaKuwekaHifadhiYaMazungumzo
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#inapaswaKuendeleaKumbukaMaongezi
```

## Kuendesha Vipimo Katika VS Code

Ikiwa unatumia Visual Studio Code, Test Explorer hutoa kiolesura cha picha kwa ajili ya kuendesha na kufuatilia vipimo.

<img src="../../../translated_images/sw/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*Test Explorer ya VS Code ikionyesha mti wa vipimo pamoja na madarasa yote ya vipimo vya Java na njia binafsi za vipimo*

**Kuendesha vipimo katika VS Code:**

1. Fungua Test Explorer kwa kubofya alama ya beka kwenye Ukanda wa Shughuli
2. Panua mti wa vipimo kuona moduli zote na madarasa ya vipimo
3. Bonyeza kitufe cha kucheza kando ya kipimo chochote kuikimbia pekee
4. Bonyeza "Run All Tests" kuendesha seti yote
5. Bofya kulia kipimo chochote na chagua "Debug Test" kuweka maeneo ya kusimamisha na hatua kwa hatua kupitia msimbo

Test Explorer inaonyesha alama za tik ya kijani kwa vipimo vinavyofaulu na hutoa ujumbe wa kina wa kushindwa wakati vipimo vinaanguka.

## Mifumo ya Kupima

### Mfano 1: Kupima Mifano ya Maelekezo

Mfano rahisi zaidi hupima mifano ya maelekezo bila kupiga simu kwa mfano wowote wa AI. Unahakikisha kuwa kubadilisha vigezo hufanya kazi sawasawa na maelekezo yameandaliwa kama inavyotarajiwa.

<img src="../../../translated_images/sw/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*Kupima mifano ya maelekezo ikionyesha mtiririko wa kubadilisha vigezo: kiolezo chenye sehemu za kujaza → thamani zimewekwa → matokeo yaliyopangwa yamehakikiwa*

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

**Kiendeshe:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#jaribuUmbizoLaKiolezoChaKidokezo
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#jaribuMpangilioWaKiolezoChaOmbi
```

### Mfano 2: Kuiga Mifano ya Lugha

Unapopima mantiki ya mazungumzo, tumia Mockito kuunda mifano bandia inayorudisha majibu yaliyopangwa. Hii hufanya vipimo kuwa vya haraka, bure, na vinavyotarajiwa.

<img src="../../../translated_images/sw/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*Ulinganisho unaoonyesha kwanini kuiga ni bora kwa vipimo: ni haraka, bure, vinavyotarajiwa, na havihitaji funguo za API*

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

Mfano huu unaonekana katika `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`. Kuiga huhakikisha utendaji unaoendelea hivyo unaweza kuthibitisha usimamizi wa kumbukumbu unafanya kazi vizuri.

### Mfano 3: Kupima Kutengwa kwa Mazungumzo

Kumbukumbu za mazungumzo lazima zihifadhi watumiaji wengi kando. Kipimo hiki kinathibitisha kuwa mazungumzo hayajachanganywa muktadha.

<img src="../../../translated_images/sw/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*Kupima kutengwa kwa mazungumzo kuonyesha hifadhi za kumbukumbu tofauti kwa watumiaji tofauti ili kuzuia mchanganyiko wa muktadha*

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

Kila mazungumzo huhifadhi historia yake huru. Katika mifumo ya utengenezaji, kutengwa hivi ni muhimu kwa programu zinazoendeshwa na watumiaji wengi.

### Mfano 4: Kupima Zana Peke Yake

Zana ni kazi ambazo AI inaweza kupiga simu. Zipime moja kwa moja kuhakikisha zinafanya kazi bila kujali maamuzi ya AI.

<img src="../../../translated_images/sw/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*Kupima zana peke yake ikionyesha utekelezaji wa zana bandia bila simu za AI kuthibitisha mantiki ya biashara*

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

Vipimo hivi kutoka `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` vinathibitisha mantiki ya zana bila kujumuisha AI. Mfano wa mfuatano unaonyesha jinsi pato la zana moja linavyoingiza kwenye ingizo la nyingine.

### Mfano 5: Kupima RAG Ndani ya Kumbukumbu

Mifumo ya RAG kawaida huhitaji hifadhidata za vekta na huduma za kuweka alama. Mfano wa ndani ya kumbukumbu hukuruhusu kupima mchakato mzima bila utegemezi wa nje.

<img src="../../../translated_images/sw/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*Mtiririko wa kupima RAG ndani ya kumbukumbu unaonesha uchambuzi wa hati, uhifadhi wa kuweka alama, na utafutaji wa ufanano bila hitaji la hifadhidata*

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

Kipimo hiki kutoka `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` kinaunda hati ndani ya kumbukumbu na kuthibitisha kugawanya na usimamizi wa metadata.

### Mfano 6: Kupima Muunganiko wa MCP

Moduli ya MCP hupima muunganiko wa Itifaki ya Muktadha wa Mfano kwa kutumia usafirishaji wa stdio. Vipimo hivi vinathibitisha kuwa programu yako inaweza kuanzisha na kuwasiliana na seva za MCP kama mchakato mdogo.

Vipimo katika `05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` vinathibitisha tabia ya mteja MCP.

**Viendeshe:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Falsafa ya Kupima

Pima msimbo wako, si AI. Vipimo vyako vinapaswa kuthibitisha msimbo unaouandika kwa kuangalia jinsi maelekezo yanavyotengenezwa, jinsi kumbukumbu inavyosimamiwa, na jinsi zana zinavyotekelezwa. Majibu ya AI hubadilika na hayapaswi kuwa sehemu ya kuthibitisha kipimo. Jiulize kama kiolezo chako cha maelekezo kinabadilisha vigezo vya usahihi, si kama AI inatoa jibu sahihi.

Tumia kuiga kwa mifano ya lugha. Ni utegemezi wa nje ambao ni polepole, gharama, na usiokuwa na utabiri. Kuiga hufanya vipimo kuwa haraka kwa muda mfupi badala ya sekunde, bure bila gharama za API, na vinavyotarajiwa na matokeo sawa kila wakati.

Hifadhi vipimo kuwa huru. Kila kipimo kinapaswa kuanzisha data yake yenyewe, kisitegemee vipimo vingine, na kusafisha baada yake. Vipimo vinapaswa kufaulu bila kujali mpangilio wa utekelezaji.

Pima kesi za mwisho zaidi za matumizi. Jaribu pembejeo zisizo na kitu, pembejeo kubwa sana, herufi maalum, vigezo batili, na vizingiti. Hivi mara nyingi huonyesha kasoro ambazo matumizi ya kawaida hayaionyeshi.

Tumia majina ya kueleweka. Linganisha `shouldMaintainConversationHistoryAcrossMultipleMessages()` na `test1()`. Ya kwanza inakuambia hasa kinachopimwa, na kufanya ugunduzi wa hitilafu kuwa rahisi zaidi.

## Hatua Zinazofuata

Sasa unapoelewa mifumo ya kupima, chimbukia ndani zaidi kila moduli:

- **[00 - Anza Haraka](../00-quick-start/README.md)** - Anza na misingi ya mifano ya maelekezo
- **[01 - Utangulizi](../01-introduction/README.md)** - Jifunze usimamizi wa kumbukumbu ya mazungumzo
- **[02 - Uhandisi wa Maelekezo](../02-prompt-engineering/README.md)** - Zaidi kujifunza mifumo ya kuamsha GPT-5.2
- **[03 - RAG](../03-rag/README.md)** - Tengeneza mifumo ya kizazi cha kuongeza urejesho
- **[04 - Zana](../04-tools/README.md)** - Tekeleza upigia nyaraka na mfuatano wa zana
- **[05 - MCP](../05-mcp/README.md)** - Unganisha Itifaki ya Muktadha wa Mfano

Kila README ya moduli hutoa maelezo ya kina ya dhana zinazopimwa hapa.

---

**Uelekezaji:** [← Rudi Kwenye Kuu](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Maelezo Muhimu**:  
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kufanikisha usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha kuaminika. Kwa habari muhimu, inashauriwa kutumia huduma za utafsiri wa kitaalamu wa binadamu. Hatuchukulii dhamana kwa kutoelewana au tafsiri potofu zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->