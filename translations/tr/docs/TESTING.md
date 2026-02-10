# LangChain4j Uygulamalarını Test Etme

## İçindekiler

- [Hızlı Başlangıç](../../../docs)
- [Testlerin Kapsadığı Konular](../../../docs)
- [Testleri Çalıştırma](../../../docs)
- [VS Code'da Testleri Çalıştırma](../../../docs)
- [Test Desenleri](../../../docs)
- [Test Felsefesi](../../../docs)
- [Sonraki Adımlar](../../../docs)

Bu rehber, API anahtarı veya dış servis gerektirmeden yapay zeka uygulamalarını nasıl test edeceğinizi gösteren testlere sizi yönlendirir.

## Hızlı Başlangıç

Tüm testleri tek bir komut ile çalıştırın:

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/tr/test-results.ea5c98d8f3642043.webp" alt="Başarılı Test Sonuçları" width="800"/>

*Tüm testlerin sıfır başarısızlıkla geçtiğini gösteren başarılı test çalıştırması*

## Testlerin Kapsadığı Konular

Bu kurs, yerel olarak çalışan **birim testlere** odaklanır. Her test, LangChain4j'nin belirli bir kavramını izole şekilde gösterir.

<img src="../../../translated_images/tr/testing-pyramid.2dd1079a0481e53e.webp" alt="Test Piramidi" width="800"/>

*Birim testlerin (hızlı, izole), entegrasyon testlerinin (gerçek bileşenler) ve uçtan uca testlerin dengesi gösteren test piramidi. Bu eğitim birim testi kapsamaktadır.*

| Modül | Testler | Odak | Ana Dosyalar |
|--------|-------|-------|-----------|
| **00 - Hızlı Başlangıç** | 6 | İstem şablonları ve değişken yerleştirme | `SimpleQuickStartTest.java` |
| **01 - Giriş** | 8 | Konuşma hafızası ve durumlu sohbet | `SimpleConversationTest.java` |
| **02 - İstem Mühendisliği** | 12 | GPT-5.2 desenleri, istek seviyeleri, yapılandırılmış çıktı | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | Doküman işleme, gömme, benzerlik araması | `DocumentServiceTest.java` |
| **04 - Araçlar** | 12 | Fonksiyon çağırma ve araç zinciri | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Stdio transport ile Model Context Protocol | `SimpleMcpTest.java` |

## Testleri Çalıştırma

**Tüm testleri ana dizinden çalıştırın:**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**Belirli bir modül için testleri çalıştırın:**

**Bash:**
```bash
cd 01-introduction && mvn test
# Veya kökten
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# Veya kökten
mvn --% test -pl 01-introduction
```

**Tek bir test sınıfını çalıştırın:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**Belirli bir test metodunu çalıştırın:**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#Konuşma geçmişi korunmalı mı
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#Konuşma Geçmişi Korunmalı mı
```

## VS Code'da Testleri Çalıştırma

Visual Studio Code kullanıyorsanız, Test Explorer testleri çalıştırmak ve hata ayıklamak için grafiksel arayüz sağlar.

<img src="../../../translated_images/tr/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Gezgini" width="800"/>

*VS Code Test Gezgini, tüm Java test sınıflarını ve bireysel test metodlarını gösteren test ağacını gösteriyor*

**VS Code'da testleri çalıştırmak için:**

1. Aktivite Çubuğundaki beher simgesine tıklayarak Test Gezgini'ni açın
2. Tüm modülleri ve test sınıflarını görmek için test ağacını genişletin
3. Herhangi bir testi tek tek çalıştırmak için oynat düğmesine tıklayın
4. Tüm testi çalıştırmak için "Tüm Testleri Çalıştır" seçeneğine tıklayın
5. Herhangi bir testi sağ tıklayıp "Testi Hata Ayıkla" seçeneği ile kesme noktası koyup kodu adım adım izleyin

Test Gezgini, geçen testler için yeşil onay işaretleri gösterir ve başarısız olanlara ayrıntılı hata mesajları sağlar.

## Test Desenleri

### Desen 1: İstem Şablonlarını Test Etme

En basit desen, herhangi bir yapay zeka modeli çağırmadan istem şablonlarını test eder. Değişken yerleştirmesinin doğru çalıştığını ve istemlerin beklenildiği gibi biçimlendiğini doğrularsınız.

<img src="../../../translated_images/tr/prompt-template-testing.b902758ddccc8dee.webp" alt="İstem Şablonu Testi" width="800"/>

*İstem şablonlarını test ederken değişken yerleştirme akışını gösteriyor: yer tutuculu şablon → değerler uygulanıyor → biçimlendirilmiş çıktı doğrulanıyor*

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

Bu test `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` içinde bulunur.

**Çalıştırmak için:**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#testİstekŞablonuBiçimlendirme
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testİstekŞablonuBiçimlendirme
```

### Desen 2: Dil Modellerini Taklit Etme

Konuşma mantığını test ederken, önceden belirlenmiş yanıtlar döndüren sahte modeller oluşturmak için Mockito kullanın. Bu testlerin hızlı, ücretsiz ve deterministik olmasını sağlar.

<img src="../../../translated_images/tr/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Sahte ve Gerçek API Karşılaştırması" width="800"/>

*Testte neden sahte nesnelerin tercih edildiğini gösteriyor: hızlı, ücretsiz, deterministik ve API anahtarı gerektirmezler*

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
        assertThat(history).hasSize(6); // 3 kullanıcı + 3 Yapay Zeka mesajı
    }
}
```

Bu desen `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` içinde bulunur. Sahte nesne, tutarlı davranışı sağlar böylece hafıza yönetiminin doğru çalıştığını doğrulayabilirsiniz.

### Desen 3: Konuşma İzolasyonunu Test Etme

Konuşma hafızası birden çok kullanıcıyı ayrı tutmalıdır. Bu test, konuşmaların bağlamlarını karıştırmadığını doğrular.

<img src="../../../translated_images/tr/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Konuşma İzolasyonu" width="800"/>

*Farklı kullanıcılar için ayrı hafıza depoları göstererek bağlam karışıklığını önleyen konuşma izolasyon testi*

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

Her konuşma kendi bağımsız geçmişini tutar. Üretim sistemlerinde bu izolasyon çok kullanıcılı uygulamalar için kritiktir.

### Desen 4: Araçları Bağımsız Test Etme

Araçlar, yapay zekanın çağırabileceği fonksiyonlardır. Yapay zekanın kararlarından bağımsız olarak düzgün çalıştıklarını doğrudan test edin.

<img src="../../../translated_images/tr/tools-testing.3e1706817b0b3924.webp" alt="Araç Testi" width="800"/>

*Yapay zeka çağrısı olmadan sahte araç yürütmesi göstererek iş mantığını doğrulayan bağımsız araç testi*

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

Bu testler `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` dosyasından gelir ve yapay zeka katılımı olmadan araç mantığını doğrular. Zincirleme örnek, bir aracın çıktısının diğerinin girdisi olarak nasıl kullanıldığını gösterir.

### Desen 5: Bellek İçi RAG Testi

RAG sistemleri genellikle vektör veritabanları ve gömme servisleri gerektirir. Bellek içi desen, harici bağımlılık olmadan tüm iş akışını test etmenizi sağlar.

<img src="../../../translated_images/tr/rag-testing.ee7541b1e23934b1.webp" alt="Bellek İçi RAG Testi" width="800"/>

*Veritabanı gerektirmeden doküman ayrıştırma, gömme saklama ve benzerlik aramasını gösteren bellek içi RAG testi iş akışı*

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

Bu test `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` içinde bulunur. Bellekte bir doküman oluşturur ve parçalara ayırma ile meta veri işleme doğrulaması yapar.

### Desen 6: MCP Entegrasyon Testi

MCP modülü, stdio transport kullanarak Model Context Protocol entegrasyonunu test eder. Bu testler uygulamanızın MCP sunucularını alt süreç olarak başlatıp iletişim kurabildiğini doğrular.

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` içindeki testler MCP istemci davranışını doğrular.

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

Kodunuzu, yapay zekayı değil, test edin. Testleriniz, istemlerin nasıl oluşturulduğunu, hafızanın nasıl yönetildiğini ve araçların nasıl yürütüldüğünü doğrulayarak yazdığınız kodu doğrulamalıdır. Yapay zeka yanıtları değişkendir ve test iddialarının parçası olmamalıdır. İstem şablonunuzun değişkenleri doğru şekilde yerleştirip yerleştirmediğini sorun; yapay zekanın doğru cevabı verip vermediğini değil.

Dil modelleri için sahte nesneler kullanın. Bunlar dış bağımlılıklardır, yavaş, pahalı ve deterministik değildir. Sahte nesneler testleri saniyeler yerine milisaniyeler içinde hızlı, API maliyeti olmadan ücretsiz ve her seferinde aynı sonucu veren deterministik yapar.

Testleri bağımsız tutun. Her test kendi verisini kurmalı, diğer testlere güvenmemeli ve kendinden sonra temizlik yapmalıdır. Testler çalıştırma sırasından bağımsız olarak geçmelidir.

Mutlu yolu aşan uç durumları test edin. Boş girişleri, çok büyük girişleri, özel karakterleri, geçersiz parametreleri ve sınır durumlarını deneyin. Bunlar genellikle normal kullanımda ortaya çıkmayan hataları ortaya çıkarır.

Anlamlı isimler kullanın. `shouldMaintainConversationHistoryAcrossMultipleMessages()` ile `test1()`'i karşılaştırın. İlk isim tam olarak neyin test edildiğini söyler, böylece hata ayıklamak çok daha kolay olur.

## Sonraki Adımlar

Artık test desenlerini anladığınıza göre, her modüle daha derin dalın:

- **[00 - Hızlı Başlangıç](../00-quick-start/README.md)** - İstem şablonu temellerine başlayın
- **[01 - Giriş](../01-introduction/README.md)** - Konuşma hafızası yönetimini öğrenin
- **[02 - İstem Mühendisliği](../02/prompt-engineering/README.md)** - GPT-5.2 istem desenlerinde ustalaşın
- **[03 - RAG](../03-rag/README.md)** - Retrieval-augmented generation sistemleri kurun
- **[04 - Araçlar](../04-tools/README.md)** - Fonksiyon çağırma ve araç zincirleme uygulayın
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocol’ü entegre edin

Her modülün README dosyası burada test edilen kavramların detaylı açıklamalarını sağlar.

---

**Geçiş:** [← Ana Sayfaya Dön](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu doküman, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal doküman, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilmektedir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->