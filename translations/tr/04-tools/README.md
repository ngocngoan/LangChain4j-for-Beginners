# Modül 04: Araçlarla AI Ajanları

## İçindekiler

- [Neler Öğreneceksiniz](../../../04-tools)
- [Önkoşullar](../../../04-tools)
- [Araçlarla AI Ajanlarını Anlamak](../../../04-tools)
- [Araç Çağrısı Nasıl Çalışır](../../../04-tools)
  - [Araç Tanımları](../../../04-tools)
  - [Karar Verme](../../../04-tools)
  - [Yürütme](../../../04-tools)
  - [Yanıt Oluşturma](../../../04-tools)
  - [Mimari: Spring Boot Otomatik Bağlama](../../../04-tools)
- [Araç Zincirleme](../../../04-tools)
- [Uygulamayı Çalıştırma](../../../04-tools)
- [Uygulamayı Kullanma](../../../04-tools)
  - [Basit Araç Kullanımını Deneyin](../../../04-tools)
  - [Araç Zincirlemeyi Test Edin](../../../04-tools)
  - [Konuşma Akışını Görün](../../../04-tools)
  - [Farklı İsteklerle Deneyin](../../../04-tools)
- [Temel Kavramlar](../../../04-tools)
  - [ReAct Deseni (Akıl Yürütme ve Hareket)](../../../04-tools)
  - [Araç Tanımları Önemlidir](../../../04-tools)
  - [Oturum Yönetimi](../../../04-tools)
  - [Hata Yönetimi](../../../04-tools)
- [Mevcut Araçlar](../../../04-tools)
- [Araç Tabanlı Ajanları Ne Zaman Kullanmalı](../../../04-tools)
- [Araçlar ve RAG Arasındaki Farklar](../../../04-tools)
- [Sonraki Adımlar](../../../04-tools)

## Neler Öğreneceksiniz

Şu ana kadar AI ile nasıl konuşulacağını, istemleri etkili şekilde nasıl yapılandıracağınızı ve yanıtları belgelerinize nasıl dayandıracağınızı öğrendiniz. Ancak hala temel bir kısıtlama var: dil modelleri sadece metin üretebilir. Hava durumunu kontrol edemez, hesaplama yapamaz, veri tabanlarını sorgulayamaz veya harici sistemlerle etkileşime giremez.

Araçlar bunu değiştirir. Modele çağırabileceği işlevlere erişim vererek, onu sadece metin üreten bir modelden eylem alabilen bir ajan haline dönüştürürsünüz. Model, ne zaman araca ihtiyaç duyduğuna, hangi aracı kullanacağına ve hangi parametreleri ileteceğine karar verir. Kodunuz işlevi yürütür ve sonucu döner. Model bu sonucu yanıtına entegre eder.

## Önkoşullar

- [Modül 01 - Giriş](../01-introduction/README.md) tamamlanmış (Azure OpenAI kaynakları dağıtılmış)
- Önceki modüllerin tamamlanması önerilir (bu modül [Modül 03'teki RAG kavramlarından](../03-rag/README.md) araçlar ve RAG karşılaştırmasında bahseder)
- Kök dizinde Azure kimlik bilgilerini içeren `.env` dosyası (Modül 01'de `azd up` komutu ile oluşturulur)

> **Not:** Modül 01 tamamlanmadıysa, önce oradaki dağıtım talimatlarını takip edin.

## Araçlarla AI Ajanlarını Anlamak

> **📝 Not:** Bu modüldeki "ajanlar" terimi, araç çağırma özellikleriyle geliştirilen AI asistanları anlamına gelir. Bu, [Modül 05: MCP](../05-mcp/README.md) içinde ele alacağımız, planlama, hafıza ve çok adımlı akıl yürütme içeren **Agentic AI** desenlerinden farklıdır.

Araçlar olmadan dil modeli sadece eğitim verisinden metin üretir. Güncel havayı sorarsanız tahminde bulunur. Araçlar verilirse hava durumu API’sini çağırabilir, hesaplama yapabilir veya veri tabanı sorgulayabilir — sonra gerçek sonuçları yanıtına yedirebilir.

<img src="../../../translated_images/tr/what-are-tools.724e468fc4de64da.webp" alt="Araçlar Olmadan vs Araçlarla" width="800"/>

*Araçlar olmadan model sadece tahminde bulunabilir — araçlarla API'leri çağırabilir, hesaplamalar yapabilir ve gerçek zamanlı veri dönebilir.*

Araçlı AI ajanı, **Akıl Yürütme ve Hareket (ReAct)** desenini takip eder. Model sadece yanıt vermez — neye ihtiyacı olduğunu düşünür, araç çağırarak hareket eder, sonucu gözlemler ve tekrar hareket edip etmeyeceğine ya da nihai yanıtı vermeye karar verir:

1. **Akıl Yürüt** — Ajan kullanıcının sorusunu analiz eder, hangi bilgiye ihtiyacı olduğunu belirler
2. **Harekete Geç** — Ajan doğru aracı seçer, uygun parametreleri oluşturur ve çağırır
3. **Gözlemle** — Ajan aracın çıktısını alır ve sonucu değerlendirir
4. **Tekrar Et veya Yanıtla** — Daha fazla veri gerekiyorsa döngü devam eder, aksi halde doğal dil yanıtı oluşturur

<img src="../../../translated_images/tr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Deseni" width="800"/>

*ReAct döngüsü — ajan ne yapacağını düşünür, araç çağırarak hareket eder, sonucu gözlemler ve nihai yanıtı verebilene kadar döngü devam eder.*

Bu süreç otomatik gerçekleşir. Siz araçları ve açıklamalarını tanımlarsınız. Model ne zaman, nasıl kullanılacağına karar verir.

## Araç Çağrısı Nasıl Çalışır

### Araç Tanımları

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Fonksiyonları net açıklamalar ve parametre spesifikasyonları ile tanımlarsınız. Model bu açıklamaları sistem isteminde görür ve her aracın ne yaptığını anlar.

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

// Asistan, Spring Boot tarafından otomatik olarak şu şekilde bağlanır:
// - ChatModel bean'i
// - @Component sınıflarından tüm @Tool yöntemleri
// - Oturum yönetimi için ChatMemoryProvider
```

Aşağıdaki diyagram her açıklamayı parçalar ve AI’nın ne zaman aracı çağıracağını ve hangi argümanları ileteceğini nasıl anladığını gösterir:

<img src="../../../translated_images/tr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Araç Tanımlarının Anatomisi" width="800"/>

*Bir araç tanımının anatomisi — @Tool AI’ya ne zaman kullanılacağını söyler, @P her parametreyi açıklar ve @AiService her şeyi başlangıçta bağlar.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dosyasını açın ve sorun:
> - "Gerçek OpenWeatherMap gibi bir hava durumu API’sı nasıl entegre edilir?"
> - "AI’nın doğru kullanmasını sağlayan iyi bir araç açıklaması nasıl olur?"
> - "API hataları ve oran sınırlamalarını araç uygulamalarında nasıl yönetirim?"

### Karar Verme

Kullanıcı "Seattle'da hava nasıl?" diye sorduğunda, model rastgele araç seçmez. Kullanıcı niyetini eriştiği tüm araç açıklamalarıyla karşılaştırır, alaka düzeyine göre puanlar ve en uygun olanı seçer. Ardından, doğru parametrelerle yapılandırılmış bir işlev çağrısı oluşturur — bu örnekte `location` parametresi `"Seattle"` olarak ayarlanır.

Eğer hiçbir araç kullanıcının isteğiyle eşleşmezse, model kendi bilgisinden yanıt verir. Birden fazla araç uygunsa, en özel olan seçilir.

<img src="../../../translated_images/tr/decision-making.409cd562e5cecc49.webp" alt="AI'nın Hangi Aracı Seçeceğine Karar Vermesi" width="800"/>

*Model, kullanıcının niyetini her mevcut araca göre değerlendirir ve en iyisini seçer — bu yüzden net, spesifik araç açıklamaları yazmak önemlidir.*

### Yürütme

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot, deklaratif `@AiService` arayüzünü kayıtlı tüm araçlarla otomatik bağlar ve LangChain4j araç çağrılarını otomatik yürütür. Arka planda, eksiksiz bir araç çağrısı altı aşamadan geçer — kullanıcının doğal dil sorusundan doğal dil yanıta kadar:

<img src="../../../translated_images/tr/tool-calling-flow.8601941b0ca041e6.webp" alt="Araç Çağrısı Akışı" width="800"/>

*Uçtan uca akış — kullanıcı soru sorar, model araç seçer, LangChain4j yürütür ve model sonucu doğal yanıtına yedirir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dosyasını açın ve sorun:
> - "ReAct deseni nasıl çalışır ve neden AI ajanları için etkilidir?"
> - "Ajan hangi aracı kullanacağına ve sırasına nasıl karar verir?"
> - "Bir araç yürütülmesi başarısız olursa ne olur - hataları sağlam nasıl yönetirim?"

### Yanıt Oluşturma

Model, hava durumu verisini alır ve kullanıcıya doğal dil formatında yanıt oluşturur.

### Mimari: Spring Boot Otomatik Bağlama

Bu modül, LangChain4j’nın Spring Boot entegrasyonunu deklaratif `@AiService` arayüzleriyle kullanır. Başlangıçta Spring Boot, `@Tool` metodları içeren tüm `@Component`'leri, sizin `ChatModel` beaninizi ve `ChatMemoryProvider`’ı keşfeder — sonra hepsini tek bir `Assistant` arayüzünde sıfır kalıp kod ile bağlar.

<img src="../../../translated_images/tr/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Otomatik Bağlama Mimarisi" width="800"/>

*@AiService arayüzü ChatModel, araç bileşenleri ve hafıza sağlayıcıyı bir araya getirir — Spring Boot tüm bağlantıyı otomatik yapar.*

Bu yaklaşımın temel faydaları:

- **Spring Boot otomatik bağlama** — ChatModel ve araçlar otomatik olarak enjekte edilir
- **@MemoryId deseni** — Oturum bazlı hafıza yönetimi otomatik
- **Tek örnek** — Assistant tek seferde oluşturulur ve performans için tekrar kullanılır
- **Tip güvenli yürütme** — Java metotları doğrudan tip dönüşümü ile çağrılır
- **Çok adımlı orkestrasyon** — Araç zincirlemesini otomatik yönetir
- **Sıfır kalıp kodu** — Elle `AiServices.builder()` çağrısı gerekmez ve hafıza için HashMap gerekmez

Alternatif yöntemler (`AiServices.builder()`) daha fazla kod gerektirir ve Spring Boot entegrasyon avantajlarını kaçırır.

## Araç Zincirleme

**Araç Zincirleme** — Araç tabanlı ajanların gerçek gücü, tek bir sorunun birden fazla araç gerektirdiği durumlarda ortaya çıkar. "Seattle'da hava durumu Fahrenheit olarak ne?" diye sorarsanız, ajan otomatik olarak iki aracı zincirler: önce `getCurrentWeather`'ı çağırıp Celsius sıcaklığı alır, sonra bu değeri `celsiusToFahrenheit`'e vererek dönüştürür — hepsi tek bir konuşma dönüşünde.

<img src="../../../translated_images/tr/tool-chaining-example.538203e73d09dd82.webp" alt="Araç Zincirleme Örneği" width="800"/>

*Araç zincirleme uygulamada — ajan önce getCurrentWeather’ı çağırır, sonra Celsius sonucu celsiusToFahrenheit’e gönderir ve birleşik yanıt verir.*

**Kibar Hatalar** — Örnek veride olmayan bir şehir için hava durumu istendiğinde araç hata mesajı döner ve AI hatayı açıklayarak çökmez. Araçlar güvenli şekilde hata verir. Aşağıdaki diyagram iki yaklaşımı karşılaştırır — doğru hata yönetimi ile ajan istisnayı yakalar ve yardımcı yanıt verir, yoksa tüm uygulama çöker:

<img src="../../../translated_images/tr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Hata Yönetimi Akışı" width="800"/>

*Bir araç hata verdiğinde ajan hatayı yakalar ve çökme yerine açıklayıcı bir yanıt verir.*

Bu tek bir konuşma turunda olur. Ajan birden fazla araç çağrısını otomatik orkestre eder.

## Uygulamayı Çalıştırma

**Dağıtımı doğrulayın:**

`.env` dosyasının kök dizinde Azure kimlik bilgileri ile var olduğundan emin olun (Modül 01 sırasında oluşturulur). Modül dizininden (`04-tools/`) çalıştırın:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Kök dizinden `./start-all.sh` ile tüm uygulamaları zaten başlattıysanız (Modül 01'de anlatıldığı gibi), bu modül zaten 8084 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8084 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteynerinde Spring Boot Dashboard uzantısı vardır, bu uzantı tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlar. VS Code sol yanındaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulabilirsiniz.

Dashboard üzerinden:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilir
- Uygulamaları tek tıkla başlat/durdurabilirsiniz
- Gerçek zamanlı uygulama kayıtlarını görüntüleyebilirsiniz
- Uygulama durumunu izleyebilirsiniz

Yalnızca "tools" yanındaki oynatma düğmesine tıklayarak modülü başlatın ya da tüm modülleri aynı anda başlatın.

VS Code’da Spring Boot Dashboard şöyle görünür:

<img src="../../../translated_images/tr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code'daki Spring Boot Dashboard — tüm modülleri tek yerden başlat, durdur ve izle*

**Seçenek 2: Shell script kullanımı**

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

Her iki script de kök `.env` dosyasından ortam değişkenlerini otomatik yükler ve JAR dosyaları yoksa inşa eder.

> **Not:** Başlatmadan önce tüm modülleri elle derlemeyi tercih ederseniz:
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

## Uygulamayı Kullanma

Uygulama, hava durumu ve sıcaklık dönüşüm araçlarına erişimi olan bir AI ajanıyla etkileşime girebileceğiniz web arayüzü sağlar. Arayüz şu şekildedir — hızlı başlangıç örnekleri ve istek göndermek için sohbet paneli içerir:
<a href="images/tools-homepage.png"><img src="../../../translated_images/tr/tools-homepage.4b4cd8b2717f9621.webp" alt="Yapay Zeka Ajan Araçları Arayüzü" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Yapay Zeka Ajan Araçları arayüzü - araçlarla etkileşim için hızlı örnekler ve sohbet arayüzü*

### Basit Araç Kullanımını Deneyin

Basit bir istekle başlayın: "100 derece Fahrenheit'i Celsius'a çevir". Ajan, sıcaklık dönüşüm aracına ihtiyacı olduğunu anlar, doğru parametrelerle çağırır ve sonucu döndürür. Bunun ne kadar doğal hissettirdiğine dikkat edin - hangi aracı kullanmanız gerektiğini veya nasıl çağrılacağını belirtmediniz.

### Araç Zincirleme Testi

Şimdi daha karmaşık bir şey deneyin: "Seattle'daki hava durumu nedir ve bunu Fahrenheit'e çevir?" Ajanın bunu adım adım nasıl çözdüğünü izleyin. İlk olarak hava durumunu alır (Celsius döner), ardından Fahrenheit'e çevirmesi gerektiğini anlar, dönüşüm aracını çağırır ve her iki sonucu tek bir yanıt halinde birleştirir.

### Konuşma Akışını Görün

Sohbet arayüzü, çoklu tur etkileşimlere izin veren konuşma geçmişini korur. Önceki tüm sorguları ve yanıtları görebilirsiniz, böylece konuşmayı takip etmek ve ajanın bağlamı birden çok tur boyunca nasıl oluşturduğunu anlamak kolaydır.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/tr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Birden Fazla Araç Çağrısı ile Konuşma" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Basit dönüşümler, hava durumu sorgulamaları ve araç zincirleme gösteren çok turlu konuşma*

### Farklı İsteklerle Deney Yapın

Çeşitli kombinasyonları deneyin:
- Hava durumu sorgulamaları: "Tokyo'daki hava durumu nedir?"
- Sıcaklık dönüşümleri: "25°C kaç Kelvin eder?"
- Birleşik sorgular: "Paris'teki hava durumunu kontrol et ve 20°C'nin üzerinde olup olmadığını söyle"

Ajanın doğal dili nasıl yorumladığına ve uygun araç çağrılarına nasıl eşlediğine dikkat edin.

## Temel Kavramlar

### ReAct Modeli (Muhakeme ve Hareket)

Ajan, muhakeme (ne yapılacağını kararlaştırma) ve hareket (araçları kullanma) arasında geçiş yapar. Bu model, yalnızca talimatlara yanıt vermek yerine özerk problem çözme sağlar.

### Araç Açıklamaları Önemlidir

Araç açıklamalarınızın kalitesi, ajanın onları ne kadar iyi kullandığını doğrudan etkiler. Açık, spesifik açıklamalar modelin her aracı ne zaman ve nasıl çağıracağını anlamasına yardımcı olur.

### Oturum Yönetimi

`@MemoryId` notasyonu, otomatik oturum tabanlı bellek yönetimini sağlar. Her oturum kimliği, `ChatMemoryProvider` bean'i tarafından yönetilen kendi `ChatMemory` örneğini alır; böylece çoklu kullanıcılar, konuşmaları karışmadan aynı anda ajanla etkileşim kurabilir. Aşağıdaki diyagram, çoklu kullanıcıların oturum kimliklerine göre izole edilmiş bellek depolarına nasıl yönlendirildiğini gösterir:

<img src="../../../translated_images/tr/session-management.91ad819c6c89c400.webp" alt="@MemoryId ile Oturum Yönetimi" width="800"/>

*Her oturum kimliği izole edilmiş bir konuşma geçmişine eşlenir — kullanıcılar birbirlerinin mesajlarını asla görmez.*

### Hata Yönetimi

Araçlar başarısız olabilir — API'ler zaman aşımına uğrayabilir, parametreler geçersiz olabilir, dış servisler kesilebilir. Üretim ajanları, modelin sorunları açıklayabilmesi veya alternatifler denemesi için hata yönetimine ihtiyaç duyar; aksi takdirde tüm uygulama çöker. Bir araç hata fırlattığında, LangChain4j bunu yakalar ve hata mesajını modele besler; model sorunu doğal dilde açıklayabilir.

## Mevcut Araçlar

Aşağıdaki diyagram, oluşturabileceğiniz geniş araç ekosistemini gösterir. Bu modül hava durumu ve sıcaklık araçlarını gösterir, ancak aynı `@Tool` modeli herhangi bir Java yöntemi için geçerlidir — veritabanı sorgularından ödeme işlemeye kadar.

<img src="../../../translated_images/tr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Araç Ekosistemi" width="800"/>

`@Tool` ile açıklanmış herhangi bir Java yöntemi AI tarafından erişilebilir hale gelir — model veritabanları, API'ler, e-posta, dosya işlemleri ve daha fazlasını kapsayacak şekilde genişletilebilir.

## Araç Tabanlı Ajanlar Ne Zaman Kullanılır

Her istek araç gerektirmez. Karar, AI'nin dış sistemlerle etkileşimde bulunması mı yoksa kendi bilgisinden yanıt vermesi mi gerektiğine bağlıdır. Aşağıdaki rehber, araçların ne zaman değer kattığını ve ne zaman gereksiz olduğunu özetler:

<img src="../../../translated_images/tr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Araçları Ne Zaman Kullanmalı" width="800"/>

*Hızlı bir karar rehberi — araçlar gerçek zamanlı veri, hesaplamalar ve işlemler için; genel bilgi ve yaratıcı görevler için gerekmez.*

## Araçlar ve RAG

Modüller 03 ve 04, AI'nin yapabileceklerini her ikisi de genişletir ama temel olarak farklı şekillerde. RAG modele belge getirerek **bilgi** sağlar. Araçlar ise fonksiyon çağırarak **eylem** yapma yeteneği verir. Aşağıdaki diyagram bu iki yaklaşımı yan yana karşılaştırır — her bir iş akışının nasıl çalıştığından aralarındaki takaslara kadar:

<img src="../../../translated_images/tr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Araçlar ve RAG Karşılaştırması" width="800"/>

*RAG statik belgelerden bilgi getirir — Araçlar işlem yapar ve dinamik, gerçek zamanlı veri getirir. Birçok üretim sistemi her ikisini de birleştirir.*

Pratikte, birçok üretim sistemi her iki yaklaşımı birleştirir: RAG, cevapları belgelerle desteklemek için; Araçlar ise canlı veri çekmek veya işlem yapmak için.

## Sonraki Adımlar

**Sonraki Modül:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Gezinme:** [← Önceki: Modül 03 - RAG](../03-rag/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstermemize rağmen, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi ana dilindeki haliyle yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlama veya yorumlamalardan dolayı sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->