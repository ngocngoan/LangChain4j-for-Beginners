# Modül 00: Hızlı Başlangıç

## İçindekiler

- [Giriş](../../../00-quick-start)
- [LangChain4j Nedir?](../../../00-quick-start)
- [LangChain4j Bağımlılıkları](../../../00-quick-start)
- [Önkoşullar](../../../00-quick-start)
- [Kurulum](../../../00-quick-start)
  - [1. GitHub Token'inizi Alın](../../../00-quick-start)
  - [2. Token'inizi Ayarlayın](../../../00-quick-start)
- [Örnekleri Çalıştırın](../../../00-quick-start)
  - [1. Temel Sohbet](../../../00-quick-start)
  - [2. İstem Kalıpları](../../../00-quick-start)
  - [3. Fonksiyon Çağrısı](../../../00-quick-start)
  - [4. Doküman Soru-Cevap (Kolay RAG)](../../../00-quick-start)
  - [5. Sorumlu Yapay Zeka](../../../00-quick-start)
- [Her Örnek Ne Gösterir](../../../00-quick-start)
- [Sonraki Adımlar](../../../00-quick-start)
- [Sorun Giderme](../../../00-quick-start)

## Giriş

Bu hızlı başlangıç, LangChain4j ile mümkün olan en hızlı şekilde çalışmaya başlamanızı sağlamak için tasarlanmıştır. LangChain4j ve GitHub Modelleri ile AI uygulamaları oluşturmanın en temel noktalarını kapsar. Sonraki modüllerde Azure OpenAI ve GPT-5.2'ye geçecek ve her konsepte daha derinlemesine dalacaksınız.

## LangChain4j Nedir?

LangChain4j, AI destekli uygulamalar oluşturmayı basitleştiren bir Java kütüphanesidir. HTTP istemcileri ve JSON ayrıştırmayla uğraşmak yerine, temiz Java API'leri ile çalışırsınız.

LangChain'deki "zincir", birden çok bileşeni birbirine bağlamayı ifade eder — bir istemi modele, modele ayrıştırıcıya bağlayabilir veya bir çıktı diğer girdiye beslenen ardışık AI çağrıları zinciri oluşturabilirsiniz. Bu hızlı başlangıç, daha karmaşık zincirleri keşfetmeden önce temel kavramlara odaklanır.

<img src="../../../translated_images/tr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Zincirleme Konsepti" width="800"/>

*LangChain4j'de bileşenlerin zincirlenmesi - güçlü AI iş akışları oluşturmak için yapı taşları bağlanıyor*

Üç temel bileşen kullanacağız:

**ChatModel** - AI model etkileşimleri için arayüz. `model.chat("prompt")` çağırarak yanıt dizesi alırsınız. OpenAI uyumlu uç noktalarla çalışan `OpenAiOfficialChatModel` kullanıyoruz, örneğin GitHub Modelleri gibi.

**AiServices** - Tür güvenli AI servis arayüzleri oluşturur. Yöntemlerinizi tanımlayıp `@Tool` ile işaretlersiniz, LangChain4j orkestrasyonu halleder. AI gerektiğinde Java yöntemlerinizi otomatik çağırır.

**MessageWindowChatMemory** - Konuşma geçmişini korur. Bunu kullanmazsanız, her istek bağımsızdır. Kullanırsanız, AI önceki mesajları hatırlar ve birden fazla turda bağlamı sürdürür.

<img src="../../../translated_images/tr/architecture.eedc993a1c576839.webp" alt="LangChain4j Mimarisi" width="800"/>

*LangChain4j mimarisi - AI uygulamalarınızı çalıştırmak için birlikte çalışan temel bileşenler*

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

`langchain4j-open-ai-official` modülü, OpenAI uyumlu API’lara bağlanan `OpenAiOfficialChatModel` sınıfını sağlar. GitHub Modelleri aynı API formatını kullanır, bu yüzden özel bir adaptöre gerek yoktur — sadece temel URL’yi `https://models.github.ai/inference` olarak ayarlayın.

`langchain4j-easy-rag` modülü, otomatik doküman bölme, gömme ve getirme sağlar; böylece RAG uygulamalarını her adımı manuel yapılandırmadan oluşturabilirsiniz.

## Önkoşullar

**Geliştirici Konteyneri Kullanıyor musunuz?** Java ve Maven önceden yüklüdür. Sadece bir GitHub Kişisel Erişim Token’ına ihtiyacınız var.

**Yerel Geliştirme:**
- Java 21+, Maven 3.9+
- GitHub Kişisel Erişim Token’ı (aşağıdaki talimatlara bakınız)

> **Not:** Bu modül GitHub Modelleri’nden `gpt-4.1-nano` kullanır. Kodda model adını değiştirmeyin — GitHub’ın mevcut modellerine göre yapılandırılmıştır.

## Kurulum

### 1. GitHub Token'inizi Alın

1. [GitHub Ayarları → Kişisel Erişim Token’ları](https://github.com/settings/personal-access-tokens) sayfasına gidin
2. "Yeni token oluştur" seçeneğine tıklayın
3. Anlamlı bir isim verin (ör. "LangChain4j Demo")
4. Süre bitişini ayarlayın (7 gün önerilir)
5. "Hesap izinleri" altında "Modeller"i bulun ve "Yalnızca okuma" olarak ayarlayın
6. "Token oluştur" tuşuna basın
7. Token’i kopyalayın ve kaydedin — bir daha göremezsiniz

### 2. Token'inizi Ayarlayın

**Seçenek 1: VS Code Kullanarak (Önerilir)**

VS Code kullanıyorsanız, projenin kök dizinindeki `.env` dosyasına token’inizi ekleyin:

Eğer `.env` dosyası yoksa, `.env.example` dosyasını `.env` olarak kopyalayın veya yeni bir `.env` dosyası oluşturun.

**Örnek `.env` dosyası:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env dosyasında
GITHUB_TOKEN=your_token_here
```

Ardından Explorer’da herhangi bir demo dosyasına (örneğin `BasicChatDemo.java`) sağ tıklayıp **"Java'yı Çalıştır"** seçeneğini seçebilir ya da Çalıştır ve Hata Ayıkla panelindeki başlatma yapılandırmalarını kullanabilirsiniz.

**Seçenek 2: Terminal Kullanarak**

Token’i ortam değişkeni olarak ayarlayın:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Örnekleri Çalıştırın

**VS Code Kullanıyorsanız:** Explorer’da herhangi bir demo dosyasına sağ tıklayıp **"Java'yı Çalıştır"** deyin ya da token’inizi `.env` dosyasına ekledikten sonra Çalıştır ve Hata Ayıkla panelindeki başlatma yapılandırmalarını kullanın.

**Maven Kullanarak:** Alternatif olarak komut satırından çalıştırabilirsiniz:

### 1. Temel Sohbet

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. İstem Kalıpları

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Sıfır atış, birkaç atış, düşünce zinciri ve rol bazlı istemleri gösterir.

### 3. Fonksiyon Çağrısı

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI gerekli olduğunda Java yöntemlerinizi otomatik çağırır.

### 4. Doküman Soru-Cevap (Kolay RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Otomatik gömme ve getirme ile Easy RAG kullanarak belgeleriniz hakkında sorular sorun.

### 5. Sorumlu Yapay Zeka

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI güvenlik filtrelerinin zararlı içeriği nasıl engellediğini görün.

## Her Örnek Ne Gösterir

**Temel Sohbet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Buradan başlayarak LangChain4j'nin en basit haliyle nasıl çalıştığını görün. Bir `OpenAiOfficialChatModel` oluşturup `.chat()` ile istem gönderir, yanıt alırsınız. Bu, özel uç noktalar ve API anahtarları ile model başlatmanın temelini gösterir. Bu kalıbı anladıktan sonra diğer her şey bunun üzerine kurulur.

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
> - "Bu kodda GitHub Modellerinden Azure OpenAI'ye nasıl geçiş yaparım?"
> - "OpenAiOfficialChatModel.builder() içinde hangi diğer parametreleri yapılandırabilirim?"
> - "Tam yanıtı beklemek yerine akışlı yanıt nasıl eklenir?"

**İstem Mühendisliği** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Artık moda nasıl konuşulacağını biliyorsunuz, şimdi ona ne söylediğinizi keşfedelim. Bu demo aynı model kurulumunu kullanır ama beş farklı istem kalıbı gösterir. Doğrudan talimatlar için sıfır atış, örneklerden öğrenmek için birkaç atış, mantık adımlarını gösteren düşünce zinciri ve bağlam ayarlayan rol bazlı istemleri deneyin. Aynı modelin istek şeklini nasıl dramatik şekilde etkilediğini göreceksiniz.

Demo ayrıca değişkenlerle tekrar kullanılabilir istemler oluşturmak için güçlü bir yöntem olan istem şablonlarını gösterir.
Aşağıdaki örnek, LangChain4j `PromptTemplate` kullanarak değişkenleri dolduran bir istemi gösterir. AI sağlanan varış noktası ve aktiviteye göre yanıtlar.

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
> - "Sıfır atış ve birkaç atış istem arasındaki fark nedir ve her biri ne zaman kullanılmalı?"
> - "Sıcaklık parametresi modelin yanıtlarını nasıl etkiler?"
> - "Üretimde istem enjeksiyonu saldırılarını önlemek için hangi teknikler var?"
> - "Yaygın kalıplar için tekrar kullanılabilir PromptTemplate nesneleri nasıl oluşturabilirim?"

**Araç Entegrasyonu** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j’nin gücü burada ortaya çıkar. `AiServices` kullanarak Java yöntemlerinizi çağırabilen bir AI asistanı oluşturacaksınız. Yöntemlere sadece `@Tool("açıklama")` eklemeniz yeterli, LangChain4j gerisini halleder — AI kullanıcının ne istediğine göre hangi aracı ne zaman kullanacağına otomatik karar verir. Bu, sadece soru cevaplamakla kalmayıp, hareket edebilen AI oluşturmak için kilit bir teknik olan fonksiyon çağrısını gösterir.

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
> - "@Tool açıklaması nasıl çalışır ve LangChain4j arka planda bununla ne yapar?"
> - "AI, karmaşık problemleri çözmek için birden çok aracı ardışık olarak çağırabilir mi?"
> - "Bir araç istisna atarsa ne olur - hataları nasıl ele almalıyım?"
> - "Bu hesap makinesi örneği yerine gerçek bir API'yi nasıl entegre ederim?"

**Doküman Soru-Cevap (Kolay RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Burada LangChain4j "Easy RAG" yaklaşımıyla RAG (getirme destekli üretim) görüyorsunuz. Dokümanlar yüklenir, otomatik olarak ayrılır ve bellek içi depolamaya gömülür, ardından içerik getirici sorgu anında AI’ye ilgili parçaları sağlar. AI yanıtları genel bilgisinden değil, belgelerinize dayanır.

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
> - "RAG, modelin eğitim verilerine kıyasla AI halüsinasyonlarını nasıl önler?"
> - "Bu kolay yaklaşım ile özel RAG hattı arasındaki fark nedir?"
> - "Bunu birden fazla doküman veya daha büyük bilgi tabanlarını destekleyecek şekilde nasıl ölçeklendiririm?"

**Sorumlu Yapay Zeka** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Derin savunma ile AI güvenliği inşa edin. Bu demo birlikte çalışan iki koruma katmanını gösterir:

**Bölüm 1: LangChain4j Giriş Koruma Kuralları** - Tehlikeli istemler LLM’ye ulaşmadan önce engellenir. Yasaklı anahtar kelimeleri ya da kalıpları kontrol eden özel kurallar oluşturun. Bunlar kodunuzda çalışır, hızlı ve ücretsizdir.

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

**Bölüm 2: Sağlayıcı Güvenlik Filtreleri** - GitHub Modelleri yerleşik filtrelere sahiptir; kurallarınızın kaçırdığı durumları yakalar. Ciddi ihlaller için sert engeller (HTTP 400 hataları) ve AI’nin kibarca reddettiği yumuşak retler görürsünüz.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dosyasını açın ve sorun:
> - "InputGuardrail nedir ve kendi kurallarımı nasıl oluştururum?"
> - "Sert engelleme ile yumuşak red arasındaki fark nedir?"
> - "Neden hem koruma kuralları hem de sağlayıcı filtreleri birlikte kullanmalıyım?"

## Sonraki Adımlar

**Sonraki Modül:** [01-giriş - LangChain4j ile Başlarken](../01-introduction/README.md)

---

**Geçiş:** [← Ana Sayfaya Dön](../README.md) | [İleri: Modül 01 - Giriş →](../01-introduction/README.md)

---

## Sorun Giderme

### İlk Defa Maven Derleme

**Sorun:** İlk `mvn clean compile` veya `mvn package` uzun sürüyor (10-15 dakika)

**Sebep:** Maven, ilk derlemede tüm proje bağımlılıklarını (Spring Boot, LangChain4j kütüphaneleri, Azure SDK’ları vb.) indiriyor.

**Çözüm:** Bu normaldir. Sonraki derlemeler daha hızlı olur çünkü bağımlılıklar yerelde önbelleğe alınır. İndirme süresi ağ hızınıza bağlıdır.

### PowerShell Maven Komut Sözdizimi

**Sorun:** Maven komutları `Unknown lifecycle phase ".mainClass=..."` hatası veriyor
**Neden**: PowerShell, `=` işaretini bir değişken atama operatörü olarak yorumlar ve bu durum Maven özellik sözdizimini bozar.

**Çözüm**: Maven komutundan önce durdurma-ayrıştırma operatörü `--%` kullanın:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operatörü, PowerShell'e kalan tüm argümanları yorum yapmadan doğrudan Maven'a iletmesini söyler.

### Windows PowerShell Emoji Görüntüleme

**Sorun**: AI yanıtları PowerShell'de emojiler yerine bozuk karakterler (örneğin, `????` veya `â??`) gösteriyor

**Neden**: PowerShell'in varsayılan kodlaması UTF-8 emojilerini desteklemiyor

**Çözüm**: Java uygulamalarını çalıştırmadan önce bu komutu kullanın:
```cmd
chcp 65001
```

Bu, terminalde UTF-8 kodlamasını zorlar. Alternatif olarak, daha iyi Unicode desteği olan Windows Terminal'i kullanabilirsiniz.

### API Çağrılarının Hata Ayıklanması

**Sorun**: AI modelinden kimlik doğrulama hataları, hız sınırları veya beklenmeyen yanıtlar

**Çözüm**: Örneklerde `.logRequests(true)` ve `.logResponses(true)` kullanılmıştır; bu, API çağrılarının konsolda görünmesini sağlar. Bu, kimlik doğrulama hatalarını, hız sınırlarını veya beklenmeyen yanıtları çözmeye yardımcı olur. Üretim ortamında bu bayrakları kaldırarak günlük gürültüsünü azaltın.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluğa özen göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı nedeniyle oluşabilecek yanlış anlamalar veya hatalı yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->