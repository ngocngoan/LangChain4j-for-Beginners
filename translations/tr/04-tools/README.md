# Modül 04: Araçlara Sahip AI Ajanları

## İçindekiler

- [Öğrenecekleriniz](../../../04-tools)
- [Ön Koşullar](../../../04-tools)
- [Araçlara Sahip AI Ajanlarını Anlamak](../../../04-tools)
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
  - [Farklı İsteklerle Deneyler Yapın](../../../04-tools)
- [Temel Kavramlar](../../../04-tools)
  - [ReAct Deseni (Akıl Yürütme ve Eylem)](../../../04-tools)
  - [Araç Açıklamalarının Önemi](../../../04-tools)
  - [Oturum Yönetimi](../../../04-tools)
  - [Hata Yönetimi](../../../04-tools)
- [Mevcut Araçlar](../../../04-tools)
- [Araç Tabanlı Ajanlar Ne Zaman Kullanılır](../../../04-tools)
- [Araçlar ve RAG Karşılaştırması](../../../04-tools)
- [Sonraki Adımlar](../../../04-tools)

## Öğrenecekleriniz

Şimdiye kadar AI ile nasıl sohbet edeceğinizi, istemleri etkili şekilde nasıl yapılandıracağınızı ve yanıtları belgelerinizde nasıl temellendireceğinizi öğrendiniz. Ancak hala temel bir sınırlama var: dil modelleri sadece metin üretebilir. Hava durumunu kontrol edemez, hesaplama yapamaz, veri tabanlarına sorgu gönderemez veya harici sistemlerle etkileşime giremez.

Araçlar bunu değiştirir. Modele çağırabileceği fonksiyonlara erişim vererek, onu metin üreticiden eylem alabilen bir ajana dönüştürürsünüz. Model ne zaman araca ihtiyaç duyduğuna, hangi aracı kullanacağına ve hangi parametreleri geçeceğine kendisi karar verir. Kodunuz fonksiyonu yürütür ve sonucu döner. Model bu sonucu yanıtına entegre eder.

## Ön Koşullar

- Tamamlanmış [Modül 01 - Giriş](../01-introduction/README.md) (Azure OpenAI kaynakları dağıtıldı)
- Önceki modüllerin tamamlanması önerilir (bu modül, Araçlar ve RAG karşılaştırmasında [Modül 03'ten RAG kavramlarını](../03-rag/README.md) referans verir)
- Azure kimlik bilgilerini içeren kök dizinde `.env` dosyası (Modül 01'de `azd up` ile oluşturuldu)

> **Not:** Modül 01'i tamamlamadıysanız, öncelikle oradaki dağıtım talimatlarını izleyin.

## Araçlara Sahip AI Ajanlarını Anlamak

> **📝 Not:** Bu modüldeki "ajanlar" terimi, araç çağırma yetenekleri geliştirilmiş AI asistanlarını ifade eder. Bu, özerk planlama, bellek ve çok adımlı akıl yürütme içeren **Agentic AI** örüntülerinden farklıdır; onlar [Modül 05: MCP](../05-mcp/README.md) içinde ele alınacaktır.

Araç olmadan, dil modeli sadece eğitim verisinden metin üretebilir. Mevcut hava durumunu sorarsanız, tahminde bulunur. Araç verin, hava durumu API'si çağırabilir, hesaplama yapabilir veya veri tabanına sorgu gönderebilir — ve bu gerçek sonuçları yanıtına dahil eder.

<img src="../../../translated_images/tr/what-are-tools.724e468fc4de64da.webp" alt="Araç Olmadan ve Araçlarla" width="800"/>

*Araç olmadan model sadece tahmin eder — araçlarla API çağırabilir, hesaplama yapabilir ve gerçek zamanlı veri döndürebilir.*

Araçlara sahip AI ajanı, **Akıl Yürütme ve Eylem (ReAct)** desenini izler. Model sadece yanıt vermez — neye ihtiyacı olduğunu düşünür, bir aracı çağırarak eylemde bulunur, sonucu gözlemler ve tekrar eylemde bulunup bulunmayacağına veya nihai yanıtı verip vermeyeceğine karar verir:

1. **Akıl Yürütme** — Ajan, kullanıcının sorusunu analiz eder ve hangi bilgiyi gerektiğini belirler
2. **Eylem** — Ajan doğru aracı seçer, uygun parametreleri üretir ve çağırır
3. **Gözlem** — Ajan aracın çıktısını alır ve sonucu değerlendirir
4. **Tekrar veya Yanıt** — Daha fazla veriye ihtiyaç varsa döngüye devam eder; yoksa doğal dil yanıtı oluşturur

<img src="../../../translated_images/tr/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Deseni" width="800"/>

*ReAct döngüsü — ajan ne yapılacağına karar verir, aracı çağırır, sonucu gözlemler ve nihai yanıtı verebilene kadar döngüye devam eder.*

Bu süreç otomatik olarak çalışır. Siz araçları ve açıklamalarını tanımlarsınız. Model onları ne zaman ve nasıl kullanacağına karar verir.

## Araç Çağrısı Nasıl Çalışır

### Araç Tanımları

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Fonksiyonları net açıklamalar ve parametre özellikleriyle tanımlarsınız. Model bu açıklamaları sistem isteminde görür ve her aracın ne yaptığını anlar.

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

// Asistan Spring Boot tarafından otomatik olarak şu şekilde yapılandırılmıştır:
// - ChatModel bileşeni
// - @Component sınıflarından tüm @Tool yöntemleri
// - Oturum yönetimi için ChatMemoryProvider
```


Aşağıdaki diyagram tüm açıklamaları parçalıyor ve her parçanın AI'nın aracı ne zaman çağıracağı ve hangi argümanları geçeceği bilgisini nasıl sağladığını gösteriyor:

<img src="../../../translated_images/tr/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Araç Tanımları Anatomisi" width="800"/>

*Bir araç tanımının anatomisi — @Tool AI'ya ne zaman kullanacağını söyler, @P her parametreyi açıklar, @AiService ise tümü başlangıçta bir araya getirir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) dosyasını açın ve sorun:
> - "Gerçek bir hava durumu API'si olan OpenWeatherMap'i örnek verilere yerine nasıl entegre ederim?"
> - "AI'nın aracı doğru kullanmasına yardımcı olacak iyi bir araç açıklaması nasıl olmalı?"
> - "Araç uygulamalarında API hatalarını ve hız sınırlarını nasıl yönetirim?"

### Karar Verme

Kullanıcı "Seattle'da hava nasıl?" diye sorduğunda, model rastgele bir araç seçmez. Kullanıcının niyetini elindeki araç açıklamalarıyla karşılaştırır, alaka düzeyine göre puan verir ve en uygun aracı seçer. Ardından uygun parametreleri içeren yapılandırılmış bir fonksiyon çağrısı oluşturur — burada `location` parametresi `"Seattle"` olarak ayarlanır.

Hiçbir araç kullanıcının isteğine uymazsa, model kendi bilgisinden yanıt verir. Birden fazla araç uygunsa, en spesifik olanı seçer.

<img src="../../../translated_images/tr/decision-making.409cd562e5cecc49.webp" alt="AI Aracını Seçme Süreci" width="800"/>

*Model her aracı kullanıcı niyetiyle değerlendirir ve en iyi eşleşeni seçer — bu yüzden açık ve spesifik araç açıklamaları yazmak önemlidir.*

### Yürütme

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot, bildirimsel `@AiService` arayüzünü kayıtlı tüm araçlarla otomatik bağlar ve LangChain4j araç çağrılarını otomatik olarak yürütür. Sahne arkasında, kullanıcıdan doğal dil sorudan doğal dil yanıta kadar altı aşamalı kapsamlı bir araç çağrısı akışı vardır:

<img src="../../../translated_images/tr/tool-calling-flow.8601941b0ca041e6.webp" alt="Araç Çağrısı Akışı" width="800"/>

*Uçtan uca akış — kullanıcı soru sorar, model araç seçer, LangChain4j yürütür, model sonucu doğal yanıtına dahil eder.*

Eğer Modül 00'deki [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) çalıştırıldıysa, bu deseni zaten gördünüz — `Calculator` araçları aynı şekilde çağrıldı. Aşağıdaki dizin şeması demo sırasında perde arkasında tam olarak ne olduğunu gösteriyor:

<img src="../../../translated_images/tr/tool-calling-sequence.94802f406ca26278.webp" alt="Araç Çağrısı Sıra Diyagramı" width="800"/>

*Quick Start demosundaki araç çağırma döngüsü — `AiServices` mesajınızı ve araç şemalarını LLM'ye gönderir, LLM `add(42, 58)` gibi fonksiyon çağrısıyla yanıt verir, LangChain4j `Calculator` yöntemini yerel çalıştırır ve sonucu nihai yanıt için geri besler.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) dosyasını açın ve sorun:
> - "ReAct deseni nasıl çalışır ve neden AI ajanları için etkilidir?"
> - "Ajan hangi aracı kullanacağına ve hangi sırada karar verir?"
> - "Bir araç yürütme başarısız olursa ne olur - hataları nasıl sağlam şekilde yönetirim?"

### Yanıt Oluşturma

Model hava durumu verilerini alır ve kullanıcı için doğal dil yanıtı olarak biçimlendirir.

### Mimari: Spring Boot Otomatik Bağlama

Bu modül, LangChain4j'nin Spring Boot entegrasyonunu `@AiService` bildirimsel arayüzlerle kullanır. Başlangıçta Spring Boot `@Tool` yöntemleri içeren her `@Component`i, ChatModel bean'ınızı ve ChatMemoryProvider'ı keşfeder — sonra hepsini sıfır boilerplate ile tek bir `Assistant` arayüzünde bağlar.

<img src="../../../translated_images/tr/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Otomatik Bağlama Mimarisı" width="800"/>

*@AiService arayüzü ChatModel, araç bileşenleri ve bellek sağlayıcıyı birbirine bağlar — Spring Boot tüm bağlantıları otomatik yapar.*

İşte tam istek yaşam döngüsü sıra diyagramı olarak — HTTP isteğinden kontrolcü, servise, otomatik bağlanmış proxy'e, araç yürütmesine ve geri dönüşe kadar:

<img src="../../../translated_images/tr/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Araç Çağırma Sıra Diyagramı" width="800"/>

*Tam Spring Boot istek yaşam döngüsü — HTTP istek kontrolcü ve servis üzerinden otomatik bağlanmış Assistant proxy'sine akar, burada LLM ve araç çağrıları otomatik yönlendirilir.*

Bu yaklaşımın temel avantajları:

- **Spring Boot otomatik bağlama** — ChatModel ve araçlar otomatik enjekte edilir
- **@MemoryId deseni** — Oturum bazlı otomatik bellek yönetimi
- **Tek örnek** — Assistant bir kez oluşturulur, performans için yeniden kullanılır
- **Tip-güvenli yürütme** — Java yöntemleri doğrudan tip dönüşümü ile çağrılır
- **Çok adımlı orkestrasyon** — Araç zincirlemeyi otomatik yönetir
- **Sıfır boilerplate** — Elle `AiServices.builder()` çağrısı veya bellek HashMap gerekmez

Alternatif yaklaşımlar (elle `AiServices.builder()`) daha fazla kod gerektirir ve Spring Boot entegrasyon avantajlarından yoksundur.

## Araç Zincirleme

**Araç Zincirleme** — Araç tabanlı ajanların gerçek gücü, tek bir sorunun birden fazla araç gerektirdiği durumlarda ortaya çıkar. "Seattle'da hava Fahrenheit cinsinden nasıl?" diye sorun ve ajan otomatik olarak iki aracı zincirler: önce `getCurrentWeather` çağrılır ve sıcaklık Santigrat olarak alınır, sonra bu değer `celsiusToFahrenheit` aracına geçirilir — hepsi tek bir konuşma adımında.

<img src="../../../translated_images/tr/tool-chaining-example.538203e73d09dd82.webp" alt="Araç Zincirleme Örneği" width="800"/>

*Araç zincirleme çalışıyor — ajan önce getCurrentWeather'i çağırır, sonra Santigrat sonucu celsiusToFahrenheit'e geçirir ve birleşik yanıtı verir.*

**Kibar Hatalar** — Örnek veride olmayan bir şehrin havasını sorun. Araç hata mesajı döner ve AI çöker yerine yardım edemeyeceğini açıklar. Araçlar güvenle başarısız olur. Aşağıdaki diyagram iki yaklaşımı karşılaştırır — uygun hata yönetimi ile ajan istisnayı yakalar ve yardımcı yanıt verir; yoksa uygulama tamamen çöker:

<img src="../../../translated_images/tr/error-handling-flow.9a330ffc8ee0475c.webp" alt="Hata Yönetimi Akışı" width="800"/>

*Bir araç başarısız olduğunda ajan hatayı yakalar ve çökme yerine açıklayıcı yanıt verir.*

Bu, tek bir konuşma turunda gerçekleşir. Ajan birden fazla araç çağrısını kendi kendine yönetir.

## Uygulamayı Çalıştırma

**Dağıtımı doğrula:**

Azure kimlik bilgileri içeren `.env` dosyasının kök dizinde var olduğundan emin olun (Modül 01 sırasında oluşturuldu). Bu modül dizininden (`04-tools/`) çalıştırın:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlat:**

> **Not:** Eğer tüm uygulamaları kök dizinden `./start-all.sh` ile zaten başlattıysanız (Modül 01'de açıklandığı gibi), bu modül zaten 8084 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayabilir, doğrudan http://localhost:8084 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Kontrol Paneli kullanmak (VS Code kullanıcıları için önerilir)**

Geliştirme konteynerinde, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Kontrol Paneli eklentisi bulunur. VS Code'un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesine bakın) bulabilirsiniz.

Spring Boot Kontrol Panelinden:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıkla başlatabilir/durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

Sadece "tools"un yanındaki oynat butonuna tıklayarak bu modülü başlatın veya tüm modülleri birden başlatın.

VS Code'daki Spring Boot Kontrol Paneli şöyle görünür:

<img src="../../../translated_images/tr/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Kontrol Paneli" width="400"/>

*VS Code'daki Spring Boot Kontrol Paneli — tüm modülleri tek yerden başlatın, durdurun ve izleyin*

**Seçenek 2: Shell script kullanmak**

Tüm web uygulamalarını başlat (modüller 01-04):

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

Veya sadece bu modülü başlatın:

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

Her iki komut dosyası da kök `.env` dosyasından ortam değişkenlerini otomatik olarak yükler ve JAR dosyaları henüz yoksa oluşturur.

> **Not:** Başlamadan önce tüm modülleri manuel olarak derlemeyi tercih ederseniz:
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

Uygulama, hava durumu ve sıcaklık dönüşüm araçlarına erişimi olan bir AI ajanıyla etkileşim kurabileceğiniz bir web arayüzü sağlar. Arayüz şöyle görünüyor — hızlı başlatma örnekleri ve istek göndermek için bir sohbet paneli içerir:

<a href="images/tools-homepage.png"><img src="../../../translated_images/tr/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools arayüzü - araçlarla etkileşim için hızlı örnekler ve sohbet arayüzü*

### Basit Araç Kullanımını Deneyin

Basit bir istekle başlayın: "100 derece Fahrenheit'ı Celsius'a çevir". Ajan bunun için sıcaklık dönüşüm aracına ihtiyacı olduğunu anlar, doğru parametrelerle aracı çağırır ve sonucu döner. Bu doğal hissettirir — hangi aracı kullanacağınızı veya nasıl çağıracağınızı belirtmediniz.

### Araç Zincirlemeyi Test Edin

Şimdi daha karmaşık bir şey deneyin: "Seattle’da hava durumu nedir ve bunu Fahrenheit’a çevir?" Ajanın bunu adım adım nasıl yönettiğine bakın. Önce hava durumunu alır (Celsius olarak döner), ardından Fahrenheit’a çevirmesi gerektiğini fark eder, dönüşüm aracını çağırır ve her iki sonucu birleştirerek tek bir yanıt verir.

### Konuşma Akışını Görün

Sohbet arayüzü, çok turlu etkileşimlere izin vererek konuşma geçmişini korur. Önceki tüm sorguları ve yanıtları görebilir, böylece konuşmayı takip etmek ve ajanın bağlamı nasıl oluşturduğunu anlamak kolaylaşır.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/tr/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Basit dönüşümleri, hava durumu sorgularını ve araç zincirlemeyi gösteren çok turlu sohbet*

### Farklı İsteklerle Deney Yapın

Çeşitli kombinasyonları deneyin:
- Hava durumu sorguları: "Tokyo’da hava durumu nasıl?"
- Sıcaklık dönüşümleri: "25°C kaç Kelvin eder?"
- Birleşik sorgular: "Paris’te hava durumunu kontrol et ve 20°C’nin üstünde mi söyle"

Ajanın doğal dili nasıl yorumladığına ve uygun araç çağrılarına nasıl eşlediğine dikkat edin.

## Temel Kavramlar

### ReAct Deseni (Düşünme ve Hareket Etme)

Ajan, düşünme (ne yapacağını karar verme) ile hareket etme (araçları kullanma) arasında geçiş yapar. Bu desen, sadece talimatlara cevap vermek yerine özerk problem çözme olanağı sağlar.

### Araç Açıklamaları Önemlidir

Araç açıklamalarınızın kalitesi, ajanın araçları ne kadar iyi kullandığını doğrudan etkiler. Açık ve özgül açıklamalar, modelin ne zaman ve nasıl araç çağıracağını anlamasına yardımcı olur.

### Oturum Yönetimi

`@MemoryId` notasyonu, otomatik oturum tabanlı bellek yönetimini sağlar. Her oturum kimliği, `ChatMemoryProvider` bileşiği tarafından yönetilen kendi `ChatMemory` örneğini alır; böylece birden fazla kullanıcı ajanla aynı anda etkileşime girebilir ve konuşmalar karışmaz. Aşağıdaki diyagram, birden fazla kullanıcının oturum kimliklerine göre izole edilmiş bellek depolarına yönlendirilişini gösterir:

<img src="../../../translated_images/tr/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Her oturum kimliği izole edilmiş bir konuşma geçmişine eşlenir — kullanıcılar birbirlerinin mesajlarını görmez.*

### Hata Yönetimi

Araçlar başarısız olabilir — API’ler zaman aşımına uğrayabilir, parametreler geçersiz olabilir, dış servisler kapanabilir. Üretim ajanslarının hata yönetimine ihtiyacı vardır; böylece model sorunları açıklayabilir veya alternatifler deneyebilir, tüm uygulama çökmekten kurtulur. Bir araç istisna attığında, LangChain4j bunu yakalar ve hata mesajını modele geri besler; model de doğal dilde problemi açıklayabilir.

## Kullanılabilir Araçlar

Aşağıdaki diyagram, oluşturabileceğiniz geniş araç ekosistemini gösterir. Bu modül, hava durumu ve sıcaklık araçlarını gösterse de aynı `@Tool` deseni herhangi bir Java yöntemi için geçerlidir — veritabanı sorgularından ödeme işlemlerine kadar.

<img src="../../../translated_images/tr/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

`@Tool` notasyonu ile açıklanmış herhangi bir Java yöntemi AI tarafından erişilebilir hale gelir — desen veritabanları, API’ler, e-posta, dosya işlemleri ve daha fazlasına kadar genişler.

## Araç Tabanlı Ajanlar Ne Zaman Kullanılır?

Her istek araç gerektirmez. Karar, AI’nın dış sistemlerle etkileşime girip girmeyeceğine veya kendi bilgisinden yanıt verip veremeyeceğine bağlıdır. Aşağıdaki rehber, araçların ne zaman değer kattığını, ne zaman gereksiz olduğunu özetler:

<img src="../../../translated_images/tr/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Hızlı karar rehberi — araçlar gerçek zamanlı veriler, hesaplamalar ve işlemler içindir; genel bilgi ve yaratıcı görevler için gerekmez.*

## Araçlar ve RAG Karşılaştırması

03 ve 04 modülleri, AI’nın yeteneklerini artırır ancak temelde farklı şekillerde. RAG, modele belge tabanlı **bilgi** erişimi sağlar. Araçlar, modele fonksiyon çağırarak **işlem yapma** yeteneği verir. Aşağıdaki diyagram, bu iki yaklaşımı yan yana karşılaştırır — her iş akışının nasıl çalıştığından aralarındaki avantaj ve dezavantajlara kadar:

<img src="../../../translated_images/tr/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG statik belgelerden bilgi getirir — Araçlar eylem gerçekleştirir ve dinamik, gerçek zamanlı veri çeker. Pek çok üretim sistemi her iki yaklaşımı da birleştirir.*

Pratikte, pek çok üretim sistemi her iki yaklaşımı birleştirir: RAG cevapları belgelerinizle desteklemek için; Araçlar ise canlı veri çekmek veya işlemler yapmak için.

## Sonraki Adımlar

**Sonraki Modül:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**Gezinme:** [← Önceki: Modül 03 - RAG](../03-rag/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Önemli bilgiler için profesyonel insan çevirisi önerilmektedir. Bu çevirinin kullanımı sonucunda ortaya çıkabilecek herhangi bir yanlış anlama veya yorumlama konusunda sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->