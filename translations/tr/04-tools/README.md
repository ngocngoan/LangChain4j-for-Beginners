# Modül 04: Araçlarla AI Ajanları

## İçindekiler

- [Öğrenecekleriniz](../../../04-tools)
- [Önkoşullar](../../../04-tools)
- [Araçlara Sahip AI Ajanlarını Anlamak](../../../04-tools)
- [Araç Çağrısı Nasıl Çalışır](../../../04-tools)
  - [Araç Tanımları](../../../04-tools)
  - [Karar Verme](../../../04-tools)
  - [Yürütme](../../../04-tools)
  - [Yanıt Oluşturma](../../../04-tools)
  - [Mimari: Spring Boot Otomatik Bağlama](../../../04-tools)
- [Araç Zincirleme](../../../04-tools)
- [Uygulamayı Çalıştırmak](../../../04-tools)
- [Uygulamayı Kullanmak](../../../04-tools)
  - [Basit Araç Kullanımını Deneyin](../../../04-tools)
  - [Araç Zincirlemeyi Test Edin](../../../04-tools)
  - [Konuşma Akışını Görün](../../../04-tools)
  - [Farklı İsteklerle Deneyler Yapın](../../../04-tools)
- [Ana Kavramlar](../../../04-tools)
  - [ReAct Deseni (Muhakeme ve Hareket)](../../../04-tools)
  - [Araç Tanımları Önemlidir](../../../04-tools)
  - [Oturum Yönetimi](../../../04-tools)
  - [Hata Yönetimi](../../../04-tools)
- [Mevcut Araçlar](../../../04-tools)
- [Araç Tabanlı Ajanları Ne Zaman Kullanmalı](../../../04-tools)
- [Araçlar ve RAG](../../../04-tools)
- [Sonraki Adımlar](../../../04-tools)

## Öğrenecekleriniz

Şimdiye kadar AI ile nasıl sohbet edileceğini, istemleri etkili şekilde nasıl yapılandıracağınızı ve yanıtları belgelerinizle nasıl dayandıracağınızı öğrendiniz. Ancak hâlâ temel bir sınırlama var: dil modelleri yalnızca metin üretebilir. Hava durumunu kontrol edemez, hesaplama yapamaz, veritabanlarını sorgulayamaz veya dış sistemlerle etkileşime geçemezler.

Araçlar bunu değiştirir. Modele çağırabileceği fonksiyonlar erişimi vererek, onu yalnızca metin üreticisinden hareket alabilen bir ajana dönüştürürsünüz. Model, ne zaman araca ihtiyaç duyduğuna, hangi aracı kullanacağına ve hangi parametreleri geçeceğine karar verir. Kodunuz fonksiyonu yürütür ve sonucu döner. Model bu sonucu yanıtına entegre eder.

## Önkoşullar

- Modül 01 tamamlandı (Azure OpenAI kaynakları dağıtıldı)
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01'de `azd up` tarafından oluşturuldu)

> **Not:** Modül 01'i tamamlamadıysanız, önce oradaki dağıtım talimatlarını takip edin.

## Araçlara Sahip AI Ajanlarını Anlamak

> **📝 Not:** Bu modüldeki "ajanlar" terimi, araç çağırma yetenekleriyle geliştirilmiş AI asistanlarını ifade eder. Bu, [Modül 05: MCP](../05-mcp/README.md) modülünde ele alacağımız **Agentic AI** desenlerinden (planlama, bellek ve çok adımlı muhakeme gibi özerk ajanlar) farklıdır.

Araçlar olmadan bir dil modeli yalnızca eğitim verisinden metin üretebilir. Mevcut hava durumunu sorarsanız, tahminde bulunmak zorunda kalır. Araçlar verildiğinde, bir hava durumu API'sini çağırabilir, hesaplama yapabilir veya veri tabanını sorgulayabilir — ve bu gerçek sonuçları yanıtına dahil eder.

<img src="../../../translated_images/tr/what-are-tools.724e468fc4de64da.webp" alt="Araçsız vs Araçlı" width="800"/>

*Araç olmadan model sadece tahmin eder — araçlarla API çağırabilir, hesap yapabilir ve gerçek zamanlı veri dönebilir.*

Araçlara sahip bir AI ajanı **Muhakeme ve Hareket (ReAct)** deseni izler. Model sadece yanıt vermez — neye ihtiyacı olduğunu düşünür, bir araç çağırarak hareket eder, sonucu gözlemler ve sonra tekrar hareket edip etmeyeceğine ya da nihai yanıtı vermeye karar verir:

1. **Muhakeme** — Ajan kullanıcının sorusunu analiz eder ve hangi bilgiye ihtiyaç duyduğunu belirler
2. **Hareket** — Ajan doğru aracı seçer, doğru parametreleri oluşturur ve çağırır
3. **Gözlemle** — Ajan aracın çıktısını alır ve sonucu değerlendirir
4. **Tekrarla veya Yanıtla** — Daha fazla veri gerekiyorsa döngüye geri döner; aksi halde doğal dil yanıtı oluşturur

<img src="../../../translated_images/tr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Deseni" width="800"/>

*ReAct döngüsü — ajan ne yapacağını düşünür, araç çağırarak hareket eder, sonucu gözlemler ve nihai yanıtı verebilmek için döngüye girer.*

Bu otomatik olur. Siz araçları ve açıklamalarını tanımlarsınız. Model onları ne zaman ve nasıl kullanacağına karar verir.

## Araç Çağrısı Nasıl Çalışır

### Araç Tanımları

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Fonksiyonları net açıklamalar ve parametre tanımlarıyla belirtirsiniz. Model bu açıklamaları sistem isteminde görür ve her aracın ne yaptığını anlar.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Hava durumu sorgulama mantığınız
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Asistan Spring Boot tarafından otomatik olarak şu şekilde bağlanır:
// - ChatModel bean'i
// - @Component sınıflarındaki tüm @Tool yöntemleri
// - Oturum yönetimi için ChatMemoryProvider
```

Aşağıdaki diyagram her açıklamayı detaylandırır ve her bir parçanın aracı ne zaman çağıracağını ve hangi argümanların geçileceğini AI'nın nasıl anladığını gösterir:

<img src="../../../translated_images/tr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Araç Tanımlarının Anatomisi" width="800"/>

*Bir araç tanımının anatomisi — @Tool AI'ya ne zaman kullanacağını söyler, @P her parametreyi tanımlar ve @AiService başlatmada hepsini bir araya bağlar.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dosyasını açın ve sorun:
> - "Mock veri yerine gerçek bir hava durumu API'si (OpenWeatherMap gibi) nasıl entegre edilir?"
> - "AI'nın aracı doğru kullanmasına yardımcı olacak iyi bir araç açıklaması nasıl yazılır?"
> - "Araç uygulamalarında API hataları ve hız sınırları nasıl yönetilir?"

### Karar Verme

Kullanıcı "Seattle'da hava nasıl?" diye sorduğunda model rastgele bir araç seçmez. Kullanıcının niyetini erişimi olan her araç tanımıyla karşılaştırır, her birini alaka düzeyine göre puanlar ve en uygun olanı seçer. Sonra doğru parametreleri içeren yapılandırılmış bir fonksiyon çağrısı oluşturur — bu durumda `location` parametresini `"Seattle"` olarak ayarlar.

Kullanıcının isteğine uyan hiçbir araç yoksa model kendi bilgisinden yanıt verir. Birden fazla araç uyuyorsa en spesifik olanı seçer.

<img src="../../../translated_images/tr/decision-making.409cd562e5cecc49.webp" alt="Model Araç Seçimini Nasıl Yapıyor" width="800"/>

*Model her mevcut aracı kullanıcının niyetiyle değerlendirir ve en iyi eşleşeni seçer — bu yüzden net, spesifik araç açıklamaları önemlidir.*

### Yürütme

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot, deklaratif `@AiService` arayüzünü kayıtlı tüm araçlarla otomatik bağlar ve LangChain4j araç çağrılarını otomatik yürütür. Arkada, kullanıcının doğal dil sorusundan doğal dil yanıta uzanan altı aşamalı eksiksiz bir araç çağrısı akışı bulunur:

<img src="../../../translated_images/tr/tool-calling-flow.8601941b0ca041e6.webp" alt="Araç Çağrısı Akışı" width="800"/>

*Baştan sona akış — kullanıcı soru sorar, model bir araç seçer, LangChain4j bunu yürütür ve model sonucu doğal yanıta işler.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dosyasını açın ve sorun:
> - "ReAct deseni nasıl çalışır ve AI ajanları için neden etkilidir?"
> - "Ajan hangi araçları ne sırayla kullanmaya nasıl karar verir?"
> - "Bir araç yürütme başarısız olursa ne olur - hatalar nasıl sağlam şekilde yönetilir?"

### Yanıt Oluşturma

Model hava durumu verisini alır ve kullanıcının anlayacağı doğal dil yanıtına dönüştürür.

### Mimari: Spring Boot Otomatik Bağlama

Bu modül LangChain4j'ın Spring Boot entegrasyonunu deklaratif `@AiService` arayüzleriyle kullanır. Başlangıçta Spring Boot, `@Tool` yöntemleri içeren her `@Component`'i, ChatModel bean'inizi ve ChatMemoryProvider'ı bulur — sonra tümünü tek bir `Assistant` arayüzünde, elle kod yazmadan bağlar.

<img src="../../../translated_images/tr/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Otomatik Bağlama Mimarisi" width="800"/>

*@AiService arayüzü ChatModel, araç bileşenleri ve bellek sağlayıcıyı bir araya getirir — Spring Boot tüm bağlamayı otomatik yapar.*

Bu yaklaşımın temel avantajları:

- **Spring Boot otomatik bağlama** — ChatModel ve araçlar otomatik olarak enjekte edilir
- **@MemoryId deseni** — Oturum bazlı otomatik bellek yönetimi
- **Tek örnek** — Assistant bir kez yaratılır ve performans için tekrar kullanılır
- **Tip güvenli yürütme** — Java metodları doğrudan tür dönüşümüyle çağrılır
- **Çok-turlu orkestrasyon** — Araç zincirlemeyi otomatik yönetir
- **Sıfır kabuk kodu** — Elle `AiServices.builder()` çağrısı veya bellek HashMap gerekmez

Alternatif yaklaşımlar (manuel `AiServices.builder()`) daha fazla kod gerektirir ve Spring Boot entegrasyonu avantajlarından mahrum kalır.

## Araç Zincirleme

**Araç Zincirleme** — araç tabanlı ajanların gerçek gücü, tek bir sorunun birden fazla araç gerektirdiği durumlarda ortaya çıkar. "Seattle'daki hava Fahrenheit cinsinden nasıl?" diye sorarsanız, ajan otomatik olarak iki aracı zincirler: önce `getCurrentWeather` metodunu Celsius olarak sıcaklık almak için çağırır, sonra bu değeri `celsiusToFahrenheit` aracına dönüştürme için gönderir — hepsi tek bir konuşma turunda.

<img src="../../../translated_images/tr/tool-chaining-example.538203e73d09dd82.webp" alt="Araç Zincirleme Örneği" width="800"/>

*Araç zincirleme uygulamada — ajan önce getCurrentWeather'i çağırır, sonra Celsius sonucunu celsiusToFahrenheit'a aktarır ve birleşik yanıtı sunar.*

Çalışan uygulamada durum şöyle görünür — ajan tek bir konuşma turunda iki araç çağrısını zincirler:

<a href="images/tool-chaining.png"><img src="../../../translated_images/tr/tool-chaining.3b25af01967d6f7b.webp" alt="Araç Zincirleme" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Gerçek uygulama çıkışı — ajan otomatik olarak getCurrentWeather → celsiusToFahrenheit zincirini bir turda yapar.*

**Nazik Hatalar** — Mock veride olmayan bir şehir için hava isteyin. Araç hata mesajı döner, AI çöker yerine yardım edemeyeceğini açıklar. Araçlar güvenli şekilde hata verir.

<img src="../../../translated_images/tr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Hata Yönetimi Akışı" width="800"/>

*Bir araç başarısız olduğunda ajan hatayı yakalar ve çökmek yerine yardımcı bir açıklama ile yanıt verir.*

Bu tek bir konuşma turunda gerçekleşir. Ajan birden fazla araç çağrısını kendi başına yönetir.

## Uygulamayı Çalıştırmak

**Dağıtımı doğrulayın:**

Kök dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Eğer Modül 01'den `./start-all.sh` ile tüm uygulamaları zaten başlattıysanız, bu modül zaten 8084 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8084 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanarak (VS Code kullanıcıları için önerilir)**

Geliştirici konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code'da Aktivite Çubuğunda (sol tarafta) Spring Boot simgesini bulabilirsiniz.

Spring Boot Dashboard'dan:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıkla başlatabilir/durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

"tools" yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri birden başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell betikleri kullanarak**

Tüm web uygulamalarını (modüller 01-04) başlatın:

**Bash:**
```bash
cd ..  # Kök dizinden
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Kök dizinden
.\start-all.ps1
```

Ya da sadece bu modülü başlatın:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Her iki betik de ortam değişkenlerini kök `.env` dosyasından otomatik yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlatmadan önce tüm modülleri manuel olarak derlemek isterseniz:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Tarayıcınızda http://localhost:8084 adresini açın.

**Durdurmak için:**

**Bash:**
```bash
./stop.sh  # Sadece bu modül
# Veya
cd .. && ./stop-all.sh  # Tüm modüller
```

**PowerShell:**
```powershell
.\stop.ps1  # Sadece bu modül
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```

## Uygulamayı Kullanmak

Uygulama, hava durumu ve sıcaklık dönüşümü araçlarına erişimi olan bir AI ajanıyla etkileşim kurabileceğiniz web arayüzü sağlar.

<a href="images/tools-homepage.png"><img src="../../../translated_images/tr/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Ajan Araçları Arayüzü" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Ajan Araçları arayüzü - araçlarla etkileşim için hızlı örnekler ve sohbet arayüzü*

### Basit Araç Kullanımını Deneyin
Basit bir istekle başlayın: "100 derece Fahrenheit'ı Celsius'a dönüştür". Ajan, sıcaklık dönüşüm aracına ihtiyaç duyduğunu anlar, doğru parametrelerle çağırır ve sonucu döndürür. Bunun ne kadar doğal hissettirdiğine dikkat edin - hangi aracı kullanacağınızı veya nasıl çağıracağınızı belirtmediniz.

### Araç Zinciri Testi

Şimdi daha karmaşık bir şey deneyin: "Seattle'daki hava durumu nedir ve bunu Fahrenheit'a dönüştür?" Ajanın bunu adım adım nasıl hallettiğini izleyin. İlk olarak hava durumunu alır (Celsius olarak döner), Fahrenheit'a dönüştürmesi gerektiğini anlar, dönüşüm aracını çağırır ve her iki sonucu bir yanıt içinde birleştirir.

### Konuşma Akışını Görüntüle

Sohbet arayüzü, çoklu tur etkileşimlere izin veren konuşma geçmişini tutar. Önceki tüm sorguları ve yanıtları görebilir, böylece konuşmayı takip etmek ve ajanın bağlamı nasıl oluşturduğunu anlamak kolaylaşır.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/tr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Çoklu Araç Çağrıları ile Konuşma" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Basit dönüşümler, hava durumu sorgulamaları ve araç zincirlemesini gösteren çok turlu konuşma*

### Farklı İsteklerle Deney Yapın

Çeşitli kombinasyonları deneyin:
- Hava durumu sorgulamaları: "Tokyo'da hava nasıl?"
- Sıcaklık dönüşümleri: "25°C kaç Kelvin?"
- Birleştirilmiş sorgular: "Paris'teki hava durumunu kontrol et ve 20°C'nin üzerinde mi söyle"

Ajanın doğal dili nasıl yorumlayıp uygun araç çağrılarına dönüştürdüğüne dikkat edin.

## Temel Kavramlar

### ReAct Deseni (Muhakeme ve Hareket)

Ajan, muhakeme yapma (ne yapılacağına karar verme) ve hareket etme (araç kullanma) arasında geçiş yapar. Bu desen, sadece talimatlara cevap vermek yerine otonom problem çözmeyi sağlar.

### Araç Tanımları Önemlidir

Araç tanımlarınızın kalitesi, ajanın bunları ne kadar iyi kullanacağını doğrudan etkiler. Açık ve özel tanımlar, modelin her aracı ne zaman ve nasıl çağıracağını anlamasına yardımcı olur.

### Oturum Yönetimi

`@MemoryId` açıklaması, otomatik oturum tabanlı bellek yönetimini etkinleştirir. Her oturum kimliği, `ChatMemoryProvider` bileşeni tarafından yönetilen kendi `ChatMemory` örneğine sahiptir, böylece birden çok kullanıcı ajanın konuşmaları birbirine karışmadan etkileşime geçebilir.

<img src="../../../translated_images/tr/session-management.91ad819c6c89c400.webp" alt="@MemoryId ile Oturum Yönetimi" width="800"/>

*Her oturum kimliği izole edilmiş bir konuşma geçmişini eşler — kullanıcılar birbirinin mesajlarını asla görmez.*

### Hata Yönetimi

Araçlar başarısız olabilir — API süre aşımı oluşabilir, parametreler geçersiz olabilir, dış servisler kapanabilir. Üretim ajanları, modelin sorunları açıklayabilmesi veya alternatifler deneyebilmesi için hata yönetimine ihtiyaç duyar; böylece tüm uygulama çökmez. Bir araç istisna fırlattığında, LangChain4j bunu yakalar ve hata mesajını modele iletir; model de problemi doğal dilde açıklayabilir.

## Mevcut Araçlar

Aşağıdaki diyagram, oluşturabileceğiniz geniş araç ekosistemini gösterir. Bu modül, hava durumu ve sıcaklık araçlarını gösterir, ancak aynı `@Tool` deseni herhangi bir Java yöntemi için çalışır — veritabanı sorgularından ödeme işlemlerine kadar.

<img src="../../../translated_images/tr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Araç Ekosistemi" width="800"/>

*@Tool ile işaretlenmiş herhangi bir Java yöntemi AI tarafından kullanılabilir — desen veritabanları, API'ler, e-posta, dosya işlemleri ve daha fazlasına genişler.*

## Araç Tabanlı Ajanlar Ne Zaman Kullanılır?

<img src="../../../translated_images/tr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Araçları Ne Zaman Kullanmalı" width="800"/>

*Hızlı karar kılavuzu — araçlar gerçek zamanlı veri, hesaplama ve hareketler içindir; genel bilgi ve yaratıcı görevler için gerekmez.*

**Araçları şu durumlarda kullanın:**
- Gerçek zamanlı veri gerektiğinde (hava, hisse senedi fiyatları, stok durumu)
- Basit matematik ötesi hesaplamalar için
- Veritabanı veya API erişimi gerektiğinde
- E-posta gönderme, bilet oluşturma, kayıt güncelleme gibi eylemler yapmak için
- Birden fazla veri kaynağını birleştirmek için

**Araçları kullanmayın:**
- Sorular genel bilgiyle cevaplanabiliyorsa
- Yanıt tamamen sohbet amaçlı ise
- Araç gecikmesi deneyimi çok yavaşlatacaksa

## Araçlar vs RAG

03 ve 04 modülleri yapay zekanın yapabileceklerini genişletir, ama temelde farklı yollarla. RAG, modele **bilgi** sağlar — dokümanları getirir. Araçlar ise modele **hareket etme** kabiliyeti sunar — fonksiyon çağırır.

<img src="../../../translated_images/tr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Araçlar ve RAG Karşılaştırması" width="800"/>

*RAG statik dokümanlardan bilgi getirir — Araçlar eylem gerçekleştirir ve dinamik, gerçek zamanlı veri çeker. Birçok üretim sistemi her iki yaklaşımı da kombine eder.*

Pratikte, birçok üretim sistemi bu iki yaklaşımdan ikisini birleştirir: RAG yanıtları dokümantasyonunuzda temelendirir; Araçlar ise canlı veri alır veya işlemleri gerçekleştirir.

## Sonraki Adımlar

**Sonraki Modül:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Gezinme:** [← Önceki: Modül 03 - RAG](../03-rag/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba gösterilse de, otomatik çevirilerde hatalar veya yanlışlıklar bulunabilir. Orijinal dilindeki belge yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda ortaya çıkabilecek herhangi bir yanlış anlama veya yanlış yorumdan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->