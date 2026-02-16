# Modül 00: Hızlı Başlangıç

## İçindekiler

- [Giriş](../../../00-quick-start)
- [LangChain4j Nedir?](../../../00-quick-start)
- [LangChain4j Bağımlılıkları](../../../00-quick-start)
- [Ön Koşullar](../../../00-quick-start)
- [Kurulum](../../../00-quick-start)
  - [1. GitHub Token'ınızı Alın](../../../00-quick-start)
  - [2. Token'ınızı Ayarlayın](../../../00-quick-start)
- [Örnekleri Çalıştırma](../../../00-quick-start)
  - [1. Temel Sohbet](../../../00-quick-start)
  - [2. İleti Şablonları](../../../00-quick-start)
  - [3. Fonksiyon Çağırma](../../../00-quick-start)
  - [4. Belge Soru&Cevap (RAG)](../../../00-quick-start)
  - [5. Sorumlu Yapay Zeka](../../../00-quick-start)
- [Her Örnek Ne Gösteriyor](../../../00-quick-start)
- [Sonraki Adımlar](../../../00-quick-start)
- [Sorun Giderme](../../../00-quick-start)

## Giriş

Bu hızlı başlangıç, sizi LangChain4j ile mümkün olan en hızlı şekilde çalışır hale getirmek için hazırlanmıştır. LangChain4j ve GitHub Modelleri ile yapay zeka uygulamaları oluşturmanın en temel konularını kapsar. Sonraki modüllerde Azure OpenAI'yi LangChain4j ile kullanarak daha gelişmiş uygulamalar inşa edeceksiniz.

## LangChain4j Nedir?

LangChain4j, yapay zeka destekli uygulamalar oluşturmayı basitleştiren bir Java kitaplığıdır. HTTP istemcileri ve JSON ayrıştırmayla uğraşmak yerine, temiz Java API'leri ile çalışırsınız.

LangChain içindeki "zincir", birden fazla bileşeni birbirine bağlamayı ifade eder - örneğin bir iletiyi modele, modeli ayrıştırıcıya bağlayabilir veya bir çıktı diğer girdiyi besleyen birden fazla yapay zeka çağrısını zincirleyebilirsiniz. Bu hızlı başlangıç, daha karmaşık zincirler keşfedilmeden önce temel konulara odaklanır.

<img src="../../../translated_images/tr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Zincirleme Kavramı" width="800"/>

*LangChain4j'deki bileşenlerin zincirlenmesi - yapı taşları güçlü yapay zeka iş akışları oluşturmak için bağlanır*

Üç temel bileşen kullanacağız:

**ChatLanguageModel** - Yapay zeka modeli ile etkileşim için arayüz. `model.chat("prompt")` çağrısı yapar ve bir yanıt dizgesi alırsınız. OpenAI ile uyumlu uç noktalarda çalışan `OpenAiOfficialChatModel` kullanıyoruz; GitHub Modelleri aynı API biçimini kullandığından özel bir adaptör gerekmez, sadece temel URL `https://models.github.ai/inference` olarak ayarlanmalı.

**AiServices** - Tür güvenli AI servis arayüzleri oluşturur. Metotları tanımlayıp `@Tool` ile notasyon yaparsınız, LangChain4j orkestrasyonu halleder. Yapay zeka ihtiyaç duydukça Java metotlarınızı otomatik çağırır.

**MessageWindowChatMemory** - Konuşma geçmişini korur. Olmazsa her istek bağımsızdır. Varken Yapay Zeka önceki mesajları hatırlar ve çoklu tur boyunca bağlamı sürdürür.

<img src="../../../translated_images/tr/architecture.eedc993a1c576839.webp" alt="LangChain4j Mimari" width="800"/>

*LangChain4j mimarisi - temel bileşenler birlikte çalışarak yapay zeka uygulamalarınızı güçlendirir*

## LangChain4j Bağımlılıkları

Bu hızlı başlangıç [`pom.xml`](../../../00-quick-start/pom.xml) dosyasında iki Maven bağımlılığı kullanır:

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
```

`langchain4j-open-ai-official` modülü, OpenAI uyumlu API'lere bağlanan `OpenAiOfficialChatModel` sınıfını sağlar. GitHub Modelleri aynı API formatını kullandığından özel bir adaptöre gerek yoktur - sadece temel URL `https://models.github.ai/inference` olarak işaretlenmelidir.

## Ön Koşullar

**Dev Container mı kullanıyorsunuz?** Java ve Maven zaten yüklü. Yalnızca bir GitHub Kişisel Erişim Token'ına ihtiyacınız var.

**Yerel Geliştirme:**
- Java 21+, Maven 3.9+
- GitHub Kişisel Erişim Token'ı (aşağıdaki talimatlar)

> **Not:** Bu modül GitHub Modellerinden `gpt-4.1-nano` kullanıyor. Koddaki model adını değiştirmeyin - GitHub'ın mevcut modelleri ile çalışacak şekilde yapılandırılmıştır.

## Kurulum

### 1. GitHub Token'ınızı Alın

1. [GitHub Ayarlar → Kişisel Erişim Tokenları](https://github.com/settings/personal-access-tokens) sayfasına gidin
2. "Yeni token oluştur" butonuna tıklayın
3. Açıklayıcı bir ad verin (örneğin, "LangChain4j Demo")
4. Süreyi ayarlayın (7 gün önerilir)
5. "Hesap izinleri" altında "Models" öğesini bulun ve "Sadece okunabilir" olarak ayarlayın
6. "Token oluştur" butonuna tıklayın
7. Token'ı kopyalayın ve kaydedin - bir daha göremeyeceksiniz

### 2. Token'ınızı Ayarlayın

**Seçenek 1: VS Code Kullanarak (Önerilen)**

VS Code kullanıyorsanız, token'ınızı proje kökündeki `.env` dosyasına ekleyin:

Eğer `.env` dosyası yoksa, `.env.example` dosyasını `.env` olarak kopyalayın veya proje kökünde yeni bir `.env` dosyası oluşturun.

**Örnek `.env` dosyası:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env dosyasında
GITHUB_TOKEN=your_token_here
```

Sonra Explorer'daki herhangi bir demo dosyasına (örneğin `BasicChatDemo.java`) sağ tıklayarak **"Run Java"** seçeneğini seçebilir veya Çalıştır ve Hata Ayıkla panelinden başlatma yapılandırmalarını kullanabilirsiniz.

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

**VS Code Kullanımı:** Explorer'daki herhangi bir demo dosyasına sağ tıklayın ve **"Run Java"** seçeneğini seçin veya Çalıştır ve Hata Ayıkla panelinden yapılandırmaları kullanın (önce token'ınızı `.env` dosyasına eklediğinizden emin olun).

**Maven Kullanımı:** Alternatif olarak komut satırından çalıştırabilirsiniz:

### 1. Temel Sohbet

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. İleti Şablonları

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Sıfır örnek, birkaç örnek, zincirli düşünce ve rol tabanlı istemleri gösterir.

### 3. Fonksiyon Çağırma

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

Yapay zeka gerektiğinde Java metotlarınızı otomatik çağırır.

### 4. Belge Soru&Cevap (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` içeriği hakkında sorular sorun.

### 5. Sorumlu Yapay Zeka

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Yapay zeka güvenlik filtrelerinin zararlı içeriği nasıl engellediğini görün.

## Her Örnek Ne Gösteriyor

**Temel Sohbet** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Buradan başlayın, LangChain4j'nin en basit halini görün. Bir `OpenAiOfficialChatModel` oluşturup `.chat()` ile istem gönderir ve yanıt alırsınız. Bu temel, özel uç noktalar ve API anahtarları ile modelleri nasıl başlatacağınızı gösterir. Bu deseni anladığınızda diğer her şey üzerine inşa edilir.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) dosyasını açın ve sorun:
> - "Bu kodda GitHub Modelleri'nden Azure OpenAI'ye nasıl geçiş yaparım?"
> - "OpenAiOfficialChatModel.builder() içinde hangi diğer parametreleri yapılandırabilirim?"
> - "Tam yanıtı beklemek yerine akış yanıtları nasıl eklerim?"

**İleti Mühendisliği** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Artık modele nasıl konuşulacağını bildiğinize göre, ona ne söylediğinizi keşfedelim. Bu demo aynı model kurulumu ile beş farklı ileti şablonunu gösterir. Doğrudan talimatlar için sıfır örnek, örneklerden öğrenen birkaç örnek, akıl yürütme adımlarını gösteren zincirli düşünce ve bağlam kuran rol tabanlı istemleri deneyin. Aynı modelin isteğinizi nasıl çerçevelediğinize bağlı olarak tamamen farklı sonuçlar verdiğini göreceksiniz.

Demo ayrıca değişkenlerle tekrar kullanılabilir istemler oluşturmanın güçlü bir yolu olan istem şablonlarını da gösterir.
Aşağıdaki örnek, LangChain4j `PromptTemplate` kullanarak değişkenleri dolduran bir ileti şablonunu gösterir. Yapay zeka verilen varış noktası ve aktiviteye göre yanıt verecektir.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) dosyasını açın ve sorun:
> - "Sıfır örnek ile birkaç örnek istem arasındaki fark nedir ve hangisini ne zaman kullanmalıyım?"
> - "Sıcaklık parametresi modelin yanıtlarını nasıl etkiler?"
> - "Üretimde istem enjeksiyon saldırılarını önlemek için bazı teknikler nelerdir?"
> - "Yaygın desenler için tekrar kullanılabilir PromptTemplate nesneleri nasıl oluşturabilirim?"

**Araç Entegrasyonu** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Burada LangChain4j güçlü hale gelir. `AiServices` kullanarak Java metotlarınızı çağırabilen bir yapay zeka asistanı oluşturacaksınız. Metotları `@Tool("tanım")` ile işaretlemeniz yeterli, LangChain4j gerisini halleder - Yapay Zeka kullanıcının isteğine göre hangi aracı kullanacağına otomatik karar verir. Bu, sadece soruları cevaplamakla kalmayıp hareket alabilen yapay zeka oluşturmanın anahtarı olan fonksiyon çağırmayı gösterir.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) dosyasını açın ve sorun:
> - "@Tool notasyonu nasıl çalışır ve LangChain4j arka planda onunla ne yapar?"
> - "Yapay zeka karmaşık problemleri çözmek için birden fazla aracı sırayla kullanabilir mi?"
> - "Bir araç hata fırlatırsa ne olur - hataları nasıl yönetmeliyim?"
> - "Bu hesap makinesi örneği yerine gerçek bir API nasıl entegre ederim?"

**Belge Soru&Cevap (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Burada RAG'in (erişim artırılmış üretim) temelini göreceksiniz. Modelin eğitim verilerine dayanmak yerine, [`document.txt`](../../../00-quick-start/document.txt) içeriğini yükler ve istemde dahil edersiniz. Yapay zeka belgeye dayanarak yanıt verir, genel bilgiye değil. Kendi verilerinizle çalışan sistemler inşa etmenin ilk adımıdır.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Not:** Bu basit yaklaşım tüm belgeyi isteme yükler. Büyük dosyalar (>10KB) için bağlam sınırlarını aşarsınız. Modül 03, üretim RAG sistemleri için parçalama ve vektör araması konularını kapsar.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) dosyasını açın ve sorun:
> - "RAG yapay zeka halüsinasyonlarını modelin eğitim verisi kullanımına kıyasla nasıl önler?"
> - "Bu basit yaklaşım ile vektör gömme kullanarak sorgulama arasındaki fark nedir?"
> - "Bu yöntemi çoklu belgeler veya daha büyük bilgi tabanları için nasıl ölçeklendiririm?"
> - "Yapay zekanın sadece sağlanan bağlamı kullanmasını sağlamak için istem yapısını düzenlemenin en iyi uygulamaları nelerdir?"

**Sorumlu Yapay Zeka** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Derin savunmalı yapay zeka güvenliği oluşturun. Bu demo birlikte çalışan iki koruma katmanını gösterir:

**Bölüm 1: LangChain4j Giriş Koruyucuları** - Tehlikeli istemler LLM'ye ulaşmadan engellenir. Yasaklı anahtar kelimeleri veya desenleri kontrol eden özel koruyucular oluşturun. Bunlar kodunuzda çalıştığından hızlı ve ücretsizdir.

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

**Bölüm 2: Sağlayıcı Güvenlik Filtreleri** - GitHub Modellerinde, koruyucularınızın gözden kaçırabileceği şeyleri yakalayan yerleşik filtreler vardır. Ağır ihlaller için sert engeller (HTTP 400 hataları) ve yapay zekanın nazikçe reddettiği yumuşak reddetmeler göreceksiniz.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) dosyasını açın ve sorun:
> - "InputGuardrail nedir ve kendi koruyucumu nasıl oluştururum?"
> - "Sert blok ile yumuşak reddetme arasındaki fark nedir?"
> - "Neden hem koruyucuları hem de sağlayıcı filtrelerini birlikte kullanmalıyım?"

## Sonraki Adımlar

**Sonraki Modül:** [01-giriş - LangChain4j ve Azure üzerinde gpt-5 ile Başlangıç](../01-introduction/README.md)

---

**Gezinme:** [← Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 01 - Giriş →](../01-introduction/README.md)

---

## Sorun Giderme

### İlk Defa Maven Derlemesi

**Sorun:** İlk `mvn clean compile` veya `mvn package` uzun sürer (10-15 dakika)

**Neden:** Maven, ilk derlemede tüm proje bağımlılıklarını (Spring Boot, LangChain4j kütüphaneleri, Azure SDK'ları vb.) indirir.

**Çözüm:** Bu normal bir durumdur. Sonraki derlemeler çok daha hızlı olacaktır çünkü bağımlılıklar yerel olarak önbelleğe alınır. İndirme süresi ağ hızınıza bağlıdır.
### PowerShell Maven Komut Söz Dizimi

**Sorun**: Maven komutları `Unknown lifecycle phase ".mainClass=..."` hatasıyla başarısız oluyor

**Neden**: PowerShell, `=` işaretini değişken atama operatörü olarak yorumlayarak Maven özellik söz dizimini bozuyor

**Çözüm**: Maven komutundan önce durdurma-analiz operatörü `--%` kullanın:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operatörü, PowerShell'e kalan tüm argümanları yorumlamadan doğrudan Maven'a geçmesini söyler.

### Windows PowerShell Emoji Görüntüleme

**Sorun**: PowerShell'de AI yanıtları emojiler yerine çöplük karakterler (ör. `????` veya `â??`) gösteriyor

**Neden**: PowerShell'in varsayılan kodlaması UTF-8 emoji desteği sağlamıyor

**Çözüm**: Java uygulamalarını çalıştırmadan önce bu komutu kullanın:
```cmd
chcp 65001
```

Bu, terminalde UTF-8 kodlamasını zorunlu kılar. Alternatif olarak, daha iyi Unicode desteği olan Windows Terminal'i kullanabilirsiniz.

### API Çağrılarını Hata Ayıklama

**Sorun**: Kimlik doğrulama hataları, hız sınırları veya AI modelinden beklenmedik yanıtlar

**Çözüm**: Örneklerde `.logRequests(true)` ve `.logResponses(true)` kullanılarak API çağrıları konsolda gösterilir. Bu, kimlik doğrulama hatalarını, hız sınırlarını veya beklenmeyen yanıtları çözmenize yardımcı olur. Üretimde bu bayrakları kaldırarak günlük gürültüsünü azaltın.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba gösterilse de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı nedeniyle ortaya çıkabilecek herhangi bir yanlış anlama veya yorumlama nedeniyle sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->