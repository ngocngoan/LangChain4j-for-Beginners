# LangChain4j Uygulamalarını Test Etme

## İçindekiler

- [Hızlı Başlangıç](../../../docs)
- [Testlerin Kapsadığı Konular](../../../docs)
- [Testleri Çalıştırma](../../../docs)
- [VS Code'da Testleri Çalıştırma](../../../docs)
- [Test Şablonları](../../../docs)
- [Test Felsefesi](../../../docs)
- [Sonraki Adımlar](../../../docs)

Bu rehber, API anahtarları veya harici servisler gerektirmeden AI uygulamalarını nasıl test edeceğinizi gösteren testleri adım adım anlatır.

## Hızlı Başlangıç

Tüm testleri tek bir komutla çalıştırın:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

Tüm testler geçtiğinde, aşağıdaki ekran görüntüsündeki gibi hata almadan testlerin çalıştığını görmelisiniz.

<img src="../../../translated_images/tr/test-results.ea5c98d8f3642043.webp" alt="Başarılı Test Sonuçları" width="800"/>

*Hata almadan tüm testlerin geçtiğini gösteren başarılı test çalıştırması*

## Testlerin Kapsadığı Konular

Bu eğitim, yerel olarak çalışan **birim testlere** odaklanır. Her test, LangChain4j'nin belirli bir kavramını izole şekilde gösterir. Aşağıdaki test piramidi, birim testlerin nerede yer aldığını gösterir — test stratejinizin hızlı, güvenilir temelini oluştururlar.

<img src="../../../translated_images/tr/testing-pyramid.2dd1079a0481e53e.webp" alt="Test Piramidi" width="800"/>

*Birim testler (hızlı, izole), entegrasyon testleri (gerçek bileşenler) ve uçtan uca testler arasındaki dengeyi gösteren test piramidi. Bu eğitim birim testi kapsamaktadır.*

| Modül | Testler | Odak | Ana Dosyalar |
|--------|-------|-------|-----------|
| **00 - Hızlı Başlangıç** | 6 | İstek şablonları ve değişken yer değiştirme | `SimpleQuickStartTest.java` |
| **01 - Giriş** | 8 | Konuşma belleği ve durumlu sohbet | `SimpleConversationTest.java` |
| **02 - İstek Mühendisliği** | 12 | GPT-5.2 şablonları, istek istekliliği, yapılandırılmış çıktı | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Doküman işleme, gömme (embedding), benzerlik araması | `DocumentServiceTest.java` |
| **04 - Araçlar** | 12 | Fonksiyon çağırma ve araç zincirleme | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Kontekst Protokolü (MCP) stdio taşıma ile | `SimpleMcpTest.java` |

## Testleri Çalıştırma

**Kök dizinden tüm testleri çalıştır:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Belirli bir modülün testlerini çalıştır:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Ya da kök dizinden
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Ya da kök dizinden
mvn --% test -pl 01-introduction
```

**Tek bir test sınıfını çalıştır:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Belirli bir test metodunu çalıştır:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#Konuşma geçmişi korunmalı mı
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#Konuşma geçmişi korunmalı mı
```

## VS Code'da Testleri Çalıştırma

Visual Studio Code kullanıyorsanız, Test Gezgini testleri çalıştırmak ve hata ayıklamak için grafiksel bir arayüz sağlar.

<img src="../../../translated_images/tr/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Gezgini" width="800"/>

*Tüm Java test sınıfları ve tekil test metotlarını gösteren VS Code Test Gezgini test ağacı*

**VS Code'da testleri çalıştırmak için:**

1. Aktivite Çubuğundaki beher simgesine tıklayarak Test Gezgini'ni açın
2. Tüm modülleri ve test sınıflarını görmek için test ağacını genişletin
3. Herhangi bir testi tek başına çalıştırmak için yanındaki oynat düğmesine tıklayın
4. Tüm testleri çalıştırmak için "Tüm Testleri Çalıştır" seçeneğine tıklayın
5. Herhangi bir teste sağ tıklayarak "Testi Hata Ayıkla" ile kesme noktaları ayarlayıp kodda adım adım ilerleyin

Test Gezgini geçen testler için yeşil onay işaretleri gösterir ve başarısızlık durumunda detaylı hata mesajları sağlar.

## Test Şablonları

### Şablon 1: İstek Şablonlarını Test Etme

En basit şablon, herhangi bir AI modelini çağırmadan istek şablonlarını test eder. Değişken yer değiştirmesinin doğru çalıştığını ve isteklerin doğru formatlandığını doğrularsınız.

<img src="../../../translated_images/tr/prompt-template-testing.b902758ddccc8dee.webp" alt="İstek Şablonu Testi" width="800"/>

*Değişken yer değiştirme akışını gösteren istek şablonu testi: yer tutuculu şablon → uygulanan değerler → doğrulanmış biçimlendirilmiş çıktı*

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

Bu test `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` dosyasında yer alır.

**Çalıştırmak için:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testPromptŞablonFormatlama
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testİstemŞablonuBiçimlendirmesi
```

### Şablon 2: Dil Modellerini Taklit Etme (Mocking)

Sohbet mantığını test ederken, önceden belirlenmiş yanıtları dönen sahte (mock) modeller oluşturmak için Mockito kullanın. Bu testleri hızlı, ücretsiz ve deterministik (kesin) yapar.

<img src="../../../translated_images/tr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock ve Gerçek API Karşılaştırması" width="800"/>

*Testler için neden mock tercih edildiğini gösteren karşılaştırma: hızlı, ücretsiz, deterministik ve API anahtarı gerektirmezler*

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
        assertThat(history).hasSize(6); // 3 kullanıcı + 3 yapay zeka mesajı
    }
}
```

Bu şablon `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` dosyasında bulunur. Mock, bellek yönetiminin doğru çalıştığını doğrulamanız için tutarlı davranış sağlar.

### Şablon 3: Konuşma İzolasyonunu Test Etme

Konuşma belleği, birden çok kullanıcıyı ayrı tutmalıdır. Bu test, konuşmaların bağlamlarını karıştıramayacağını doğrular.

<img src="../../../translated_images/tr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Konuşma İzolasyonu" width="800"/>

*Bağlam karışıklığını önlemek için farklı kullanıcılar için ayrı bellek depolarını gösteren konuşma izolasyon testi*

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

Her konuşma kendi bağımsız geçmişini tutar. Üretim sistemlerinde bu izolasyon, çok kullanıcılı uygulamalar için kritik önemdedir.

### Şablon 4: Araçları Bağımsız Test Etme

Araçlar, AI'nın çağırabileceği fonksiyonlardır. Bunları doğrudan test ederek AI kararlarından bağımsız çalıştıklarından emin olun.

<img src="../../../translated_images/tr/tools-testing.3e1706817b0b3924.webp" alt="Araçların Testi" width="800"/>

*İş mantığını doğrulamak için AI çağrısı olmadan mock araç yürütmesini gösteren araçların bağımsız testi*

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

Bu testler `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` dosyasından gelir. Zincirleme örneği, bir aracın çıktısının diğerinin girdisi olarak nasıl beslediğini gösterir.

### Şablon 5: Bellek İçi RAG Testi

RAG sistemleri geleneksel olarak vektör tabanlı veri tabanları ve gömme servisleri gerektirir. Bellek içi şablon, tüm hattı harici bağımlılıklar olmadan test etmenizi sağlar.

<img src="../../../translated_images/tr/rag-testing.ee7541b1e23934b1.webp" alt="Bellek İçi RAG Testi" width="800"/>

*Veritabanı gerektirmeden doküman ayrıştırma, gömme depolama ve benzerlik araması gösteren bellek içi RAG test iş akışı*

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

Bu test `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` dosyasından gelir; bellek içinde bir doküman oluşturur ve parçalama ile meta veri işlemini doğrular.

### Şablon 6: MCP Entegrasyon Testi

MCP modülü stdio taşıma kullanarak Model Kontekst Protokolü entegrasyonunu test eder. Bu testler, uygulamanızın MCP sunucularını alt süreç olarak başlatıp iletişim kurabildiğini doğrular.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` dosyasındaki testler MCP istemci davranışını doğrular.

**Çalıştırmak için:**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## Test Felsefesi

AI'yı değil, kendi kodunuzu test edin. Testleriniz, isteklerin nasıl oluşturulduğunu, belleğin nasıl yönetildiğini ve araçların nasıl çalıştırıldığını kontrol ederek yazdığınız kodu doğrulamalı. AI yanıtları değişir ve test doğrulamalarının parçası olmamalıdır. Soru şu olmalı: "İstek şablonum değişkenleri doğru şekilde değiştiriyor mu?" değil, "AI doğru cevap mı veriyor?"

Dil modelleri için mock kullanın. Bunlar dış bağımlılıklar olup yavaş, pahalı ve deterministik olmayan kaynaklardır. Mocking, testleri saniyeler yerine milisaniyeler içinde hızlı, ücretsiz ve her seferinde aynı sonucu veren deterministik hale getirir.

Testleri bağımsız tutun. Her test kendi verisini kurmalı, diğer testlere bağlı olmamalı ve kendi temizliğini yapmalı. Testler çalıştırma sırasından bağımsız olarak geçmelidir.

Mutlu yolun ötesinde uç durumları test edin. Boş girdiler, çok büyük girdiler, özel karakterler, geçersiz parametreler ve sınır koşulları deneyin. Bunlar genellikle normal kullanımda ortaya çıkmayan hataları ortaya çıkarır.

Anlamlı isimler kullanın. `shouldMaintainConversationHistoryAcrossMultipleMessages()` ile `test1()` karşılaştırın. İlki tam olarak ne test edildiğini söyler, hata ayıklamayı çok kolaylaştırır.

## Sonraki Adımlar

Test şablonlarını anladıysanız, her modüle derinlemesine dalın:

- **[00 - Hızlı Başlangıç](../00-quick-start/README.md)** - İstek şablonu temelleriyle başlayın
- **[01 - Giriş](../01-introduction/README.md)** - Konuşma belleği yönetimini öğrenin
- **[02 - İstek Mühendisliği](../02/prompt-engineering/README.md)** - GPT-5.2 istek şablonları ustası olun
- **[03 - RAG](../03-rag/README.md)** - Geri getirme artırılmış üretim sistemleri oluşturun
- **[04 - Araçlar](../04-tools/README.md)** - Fonksiyon çağırma ve araç zincirleri uygulayın
- **[05 - MCP](../05-mcp/README.md)** - Model Kontekst Protokolü entegre edin

Her modülün README dosyası burada test edilen kavramların ayrıntılı açıklamalarını içerir.

---

**Geçiş:** [← Ana Sayfaya Geri](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek herhangi bir yanlış anlama veya hatalı yorumlamadan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->