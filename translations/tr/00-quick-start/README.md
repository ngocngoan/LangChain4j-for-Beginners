# Modül 00: Hızlı Başlangıç

## İçindekiler

- [Giriş](../../../00-quick-start)
- [LangChain4j Nedir?](../../../00-quick-start)
- [LangChain4j Bağımlılıkları](../../../00-quick-start)
- [Önkoşullar](../../../00-quick-start)
- [Kurulum](../../../00-quick-start)
  - [1. GitHub Token'ınızı Alın](../../../00-quick-start)
  - [2. Token'ınızı Ayarlayın](../../../00-quick-start)
- [Örnekleri Çalıştırma](../../../00-quick-start)
  - [1. Temel Sohbet](../../../00-quick-start)
  - [2. İstek Kalıpları](../../../00-quick-start)
  - [3. Fonksiyon Çağrısı](../../../00-quick-start)
  - [4. Doküman Soru-Cevap (Easy RAG)](../../../00-quick-start)
  - [5. Sorumlu AI](../../../00-quick-start)
- [Her Örnek Ne Gösteriyor](../../../00-quick-start)
- [Sonraki Adımlar](../../../00-quick-start)
- [Sorun Giderme](../../../00-quick-start)

## Giriş

Bu hızlı başlangıç, LangChain4j ile mümkün olan en kısa sürede çalışmaya başlamanız için tasarlanmıştır. LangChain4j ve GitHub Modelleri ile AI uygulamaları oluşturmanın temellerini kapsar. Sonraki modüllerde Azure OpenAI'yi LangChain4j ile kullanarak daha gelişmiş uygulamalar inşa edeceksiniz.

## LangChain4j Nedir?

LangChain4j, AI destekli uygulamaları oluşturmayı basitleştiren bir Java kütüphanesidir. HTTP istemcileri ve JSON ayrıştırma ile uğraşmak yerine, temiz Java API'leri ile çalışırsınız.

LangChain'deki "chain" (zincir) terimi, birden çok bileşenin birbirine bağlanmasını ifade eder - bir istek kalıbını modele, modele ayrıştırıcıya zincirleyebilir ya da bir çıktının sonraki girdiye aktarıldığı birden fazla AI çağrısını birbirine bağlayabilirsiniz. Bu hızlı başlangıç, daha karmaşık zincirleri keşfetmeden önce temellere odaklanır.

<img src="../../../translated_images/tr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Zincirleme Konsepti" width="800"/>

*LangChain4j’de bileşenlerin zincirlenmesi - güçlü AI iş akışları oluşturmak için yapı taşları birbirine bağlanır*

Üç temel bileşeni kullanacağız:

**ChatModel** - AI model etkileşimleri için arayüzdür. `model.chat("prompt")` çağrısını yapar ve yanıt olarak bir metin alırsınız. OpenAI uyumlu uç noktalarla çalışan `OpenAiOfficialChatModel` sınıfını kullanıyoruz.

**AiServices** - Tür güvenli AI servis arayüzleri oluşturur. Metotlar tanımlayın, bunları `@Tool` ile not edin, LangChain4j orkestrasyonu halleder. AI, gerektiğinde Java metotlarınızı otomatik olarak çağırır.

**MessageWindowChatMemory** - Sohbet geçmişini tutar. Bu olmadan her istek bağımsızdır. Bu ile AI önceki mesajları hatırlar ve çoklu turda bağlam sağlar.

<img src="../../../translated_images/tr/architecture.eedc993a1c576839.webp" alt="LangChain4j Mimarisi" width="800"/>

*LangChain4j mimarisi - AI uygulamalarınızı güçlendirmek için çalışan temel bileşenler*

## LangChain4j Bağımlılıkları

Bu hızlı başlangıç, [`pom.xml`](../../../00-quick-start/pom.xml) dosyasında üç Maven bağımlılığı kullanır:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` modülü, OpenAI uyumlu API'lara bağlanan `OpenAiOfficialChatModel` sınıfını sağlar. GitHub Modellerinin de aynı API biçimini kullandığı için özel bir adaptör gerekmez - base URL `https://models.github.ai/inference` olarak ayarlanmalıdır.

`langchain4j-easy-rag` modülü, belgelerin otomatik ayrılması, gömülmesi ve getirilmesini sağlar; böylece her adımı manuel yapılandırmadan RAG uygulamaları oluşturabilirsiniz.

## Önkoşullar

**Geliştirici Konteyneri mi kullanıyorsunuz?** Java ve Maven zaten yüklü. Sadece bir GitHub Kişisel Erişim Token'ına ihtiyacınız var.

**Yerel Geliştirme:**
- Java 21+, Maven 3.9+
- GitHub Kişisel Erişim Token'ı (aşağıdaki talimatlar)

> **Not:** Bu modül, GitHub Modellerinden `gpt-4.1-nano` modelini kullanır. Kodda model adını değiştirmeyin - GitHub modelleriyle çalışacak şekilde yapılandırılmıştır.

## Kurulum

### 1. GitHub Token'ınızı Alın

1. [GitHub Ayarları → Kişisel Erişim Tokenları](https://github.com/settings/personal-access-tokens) sayfasına gidin
2. "Yeni token oluştur" butonuna tıklayın
3. Anlamlı bir isim verin (örneğin, "LangChain4j Demo")
4. Süre sonu belirleyin (7 gün önerilir)
5. "Hesap izinleri" altında "Modeller" kısmını "Salt okuma" olarak ayarlayın
6. "Token oluştur" butonuna basın
7. Token'ınızı kopyalayın ve kaydedin - tekrar göremezsiniz

### 2. Token'ınızı Ayarlayın

**Seçenek 1: VS Code Kullanarak (Önerilir)**

VS Code kullanıyorsanız, projenin kök dizininde `.env` dosyasına token'ınızı ekleyin:

Eğer `.env` dosyası yoksa, `.env.example` dosyasını `.env` olarak kopyalayabilir ya da yeni bir `.env` dosyası oluşturabilirsiniz.

**Örnek `.env` dosyası:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env içinde
GITHUB_TOKEN=your_token_here
```

Daha sonra, Keşfet'te herhangi bir demo dosyasına (ör. `BasicChatDemo.java`) sağ tıklayarak **"Run Java"** seçeneğini seçebilir veya Çalıştır ve Hata Ayıkla panelindeki başlatma yapılandırmalarını kullanabilirsiniz.

**Seçenek 2: Terminal Kullanarak**

Token'ı ortam değişkeni olarak ayarlayın:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Örnekleri Çalıştırma

**VS Code Kullanıyorsanız:** Keşfet'te herhangi bir demo dosyasına sağ tıklayın ve **"Run Java"** seçin veya Çalıştır ve Hata Ayıkla panelinden başlatma yapılandırmalarını kullanın (öncelikle token'ınızı `.env` dosyasına eklediğinizden emin olun).

**Maven Kullanarak:** Alternatif olarak, komut satırından çalıştırabilirsiniz:

### 1. Temel Sohbet

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. İstek Kalıpları

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Sıfır atış (zero-shot), az atış (few-shot), düşünce zinciri (chain-of-thought) ve rol tabanlı istek kalıplarını gösterir.

### 3. Fonksiyon Çağrısı

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI gerektiğinde Java metodlarınızı otomatik olarak çağırır.

### 4. Doküman Soru-Cevap (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Otomatik gömme ve geri çağırma ile Easy RAG kullanarak belgeleriniz hakkında sorular sorun.

### 5. Sorumlu AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI güvenlik filtrelerinin zararlı içeriği nasıl engellediğini görün.

## Her Örnek Ne Gösteriyor

**Temel Sohbet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j'yi en basit haliyle görmek için buradan başlayın. Bir `OpenAiOfficialChatModel` oluşturur, `.chat()` ile istek gönderir ve yanıt alırsınız. Bu, özel uç noktalar ve API anahtarları ile modellerin nasıl başlatılacağını gösterir. Bu kalıbı anladıktan sonra tüm diğer şeyler üzerine kurulur.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dosyasını açın ve sorun:
> - "Bu kodda GitHub Modellerinden Azure OpenAI'ye nasıl geçerim?"
> - "OpenAiOfficialChatModel.builder() içinde hangi diğer parametreleri yapılandırabilirim?"
> - "Tam yanıtı beklemek yerine akışlı cevapları nasıl eklerim?"

**İstek Mühendisliği** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Bir modelle nasıl konuşacağınızı öğrendiğinize göre, ne söylediğinize bakalım. Bu demo aynı model ayarını kullanır ama beş farklı istek kalıbı gösterir. Doğrudan talimatlar için sıfır atış (zero-shot), örneklerden öğrenen az atış (few-shot), akıl yürütme adımlarını gösteren düşünce zinciri (chain-of-thought) ve bağlam belirleyen rol tabanlı istek kalıplarını deneyin. Aynı modelin isteğinize göre nasıl çok farklı sonuçlar verdiğini göreceksiniz.

Demo ayrıca değişkenlerle yeniden kullanılabilir istek kalıpları oluşturmanın güçlü bir yolu olan istek şablonlarını gösterir.
Aşağıdaki örnek, LangChain4j `PromptTemplate` kullanarak değişkenleri dolduran bir isteği gösterir. AI, verilen hedef ve aktiviteye göre cevap verir.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) dosyasını açın ve sorun:
> - "Sıfır atış ve az atış yapmanın farkı nedir ve hangisini ne zaman kullanmalıyım?"
> - "Sıcaklık parametresi modelin yanıtlarını nasıl etkiler?"
> - "Üretimde istek enjeksiyonu saldırılarını önlemek için hangi teknikler var?"
> - "Yaygın kalıplar için tekrar kullanılabilir PromptTemplate nesneleri nasıl yaratılır?"

**Araç Entegrasyonu** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j’nin gücünün ortaya çıktığı yer burasıdır. `AiServices` kullanarak Java metodlarınızı çağırabilen bir AI asistanı oluşturacaksınız. Metotları sadece `@Tool("açıklama")` ile not edin, gerisini LangChain4j halleder - AI, kullanıcının ne istediğine göre hangi aracı ne zaman kullanacağına karar verir. Bu, sorulara yanıt vermekten öte eylem alan AI inşa etmek için temel bir tekniktir.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dosyasını açın ve sorun:
> - "@Tool notasyonu nasıl çalışır ve LangChain4j arkada ne yapar?"
> - "AI, karmaşık problemleri çözmek için birden çok aracı sırayla çağırabilir mi?"
> - "Bir araç hata fırlatırsa ne olur - hataları nasıl yönetmeliyim?"
> - "Bu hesap makinesi örneği yerine gerçek bir API'yi nasıl entegre ederim?"

**Doküman Soru-Cevap (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Burada LangChain4j’nin "Easy RAG" yaklaşımıyla RAG (getirime dayalı üretim) göreceksiniz. Belgeler yüklenir, otomatik olarak bölünür ve bellekte saklanmak üzere gömülür, sonra içerik getirici sorgu anında ilgili parçaları AI’ya sunar. AI yanıtlarını genel bilgisi değil, belgeleriniz ışığında verir.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dosyasını açın ve sorun:
> - "RAG, AI'nin hayal gücünü modelin eğitim verisi kullanmaya kıyasla nasıl önler?"
> - "Bu kolay yaklaşım ile özel bir RAG boru hattı arasındaki fark nedir?"
> - "Bunu birden fazla belge veya daha büyük bilgi tabanları için nasıl ölçeklendiririm?"

**Sorumlu AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Derinlemesine savunma ile AI güvenliği inşa edin. Bu demo birlikte çalışan iki koruma katmanı gösterir:

**Bölüm 1: LangChain4j Giriş Koruyucuları** - Tehlikeli istekleri LLM'ye ulaşmadan engeller. Yasaklı anahtar kelimeler veya kalıplar için özel koruyucular oluşturun. Bunlar kodunuzda çalışır, hızlı ve ücretsizdir.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Bölüm 2: Sağlayıcı Güvenlik Filtreleri** - GitHub Modelleri, koruyucuların kaçırabileceği durumları yakalayan yerleşik filtrelere sahiptir. Ağır ihlaller için sert bloklar (HTTP 400 hataları) ve AI'nin nazikçe reddettiği yumuşak reddetmeler görürsünüz.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dosyasını açın ve sorun:
> - "InputGuardrail nedir ve kendim nasıl oluştururum?"
> - "Sert blok ile yumuşak reddetmenin farkı nedir?"
> - "Neden koruyucularla sağlayıcı filtreler birlikte kullanılır?"

## Sonraki Adımlar

**Sonraki Modül:** [01-giriş - LangChain4j ve Azure'da gpt-5 ile Başlangıç](../01-introduction/README.md)

---

**Gezinme:** [← Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 01 - Giriş →](../01-introduction/README.md)

---

## Sorun Giderme

### İlk Maven Derlemesi

**Sorun:** İlk `mvn clean compile` veya `mvn package` uzun sürüyor (10-15 dakika)

**Neden:** Maven, ilk derlemede tüm proje bağımlılıklarını (Spring Boot, LangChain4j kütüphaneleri, Azure SDK'ları vb.) indirir.

**Çözüm:** Bu normaldir. Sonraki derlemeler daha hızlı olacak çünkü bağımlılıklar yerel olarak önbelleğe alınır. İndirme süresi ağ hızınıza bağlıdır.

### PowerShell Maven Komut Sözdizimi

**Sorun:** Maven komutları `Unknown lifecycle phase ".mainClass=..."` hatası ile başarısız oluyor
**Neden**: PowerShell, `=` işaretini bir değişken atama operatörü olarak yorumlayarak Maven özellik sözdizimini bozuyor

**Çözüm**: Maven komutundan önce durdurma ayrıştırma operatörü `--%` kullanın:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operatörü, PowerShell'e kalan tüm argümanları yorumlamadan olduğu gibi Maven'a geçirmesini söyler.

### Windows PowerShell Emoji Görüntüleme

**Sorun**: AI yanıtları PowerShell'de emoji yerine bozuk karakterler (örneğin, `????` veya `â??`) gösteriyor

**Neden**: PowerShell'in varsayılan kodlaması UTF-8 emojileri desteklemiyor

**Çözüm**: Java uygulamalarını çalıştırmadan önce bu komutu çalıştırın:
```cmd
chcp 65001
```

Bu, terminalde UTF-8 kodlamasını zorlar. Alternatif olarak, daha iyi Unicode desteği olan Windows Terminal kullanabilirsiniz.

### API Çağrılarını Hata Ayıklama

**Sorun**: Kimlik doğrulama hataları, hız sınırları veya AI modelinden beklenmedik yanıtlar

**Çözüm**: Örneklerde `.logRequests(true)` ve `.logResponses(true)` API çağrılarını konsolda gösterir. Bu, kimlik doğrulama hataları, hız sınırları veya beklenmedik yanıtları çözmenize yardımcı olur. Günlük gürültüsünü azaltmak için üretimde bu bayrakları kaldırın.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi tavsiye edilir. Bu çevirinin kullanımı sonucu oluşabilecek herhangi bir yanlış anlama veya yanlış yorumdan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->