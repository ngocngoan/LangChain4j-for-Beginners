# Modül 01: LangChain4j ile Başlarken

## İçindekiler

- [Video Yürütme](../../../01-introduction)
- [Neler Öğreneceksiniz](../../../01-introduction)
- [Önkoşullar](../../../01-introduction)
- [Temel Problemi Anlama](../../../01-introduction)
- [Tokenları Anlama](../../../01-introduction)
- [Belleğin Nasıl Çalıştığı](../../../01-introduction)
- [LangChain4j'in Kullanımı](../../../01-introduction)
- [Azure OpenAI Altyapısını Dağıtma](../../../01-introduction)
- [Uygulamayı Yerel Çalıştırma](../../../01-introduction)
- [Uygulamayı Kullanma](../../../01-introduction)
  - [Durumsuz Sohbet (Sol Panel)](../../../01-introduction)
  - [Durumlu Sohbet (Sağ Panel)](../../../01-introduction)
- [Sonraki Adımlar](../../../01-introduction)

## Video Yürütme

Bu modülle başlamayı açıklayan canlı oturumu izleyin:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j ile Başlarken - Canlı Oturum" width="800"/></a>

## Neler Öğreneceksiniz

Hızlı başlangıcı tamamladıysanız, istemler göndermeyi ve yanıtlar almayı gördünüz. Bu temel, ancak gerçek uygulamalar daha fazlasını gerektirir. Bu modül, bağlamı hatırlayan ve durumu koruyan sohbet AI'sı oluşturmayı öğretir - tek seferlik demo ile üretime hazır uygulama arasındaki fark.

Bu rehber boyunca Azure OpenAI'nin GPT-5.2'sini kullanacağız çünkü gelişmiş akıl yürütme yetenekleri farklı desenlerin davranışını daha belirgin yapıyor. Bellek eklediğinizde farkı net göreceksiniz. Bu, uygulamanıza her bileşenin ne kattığını anlamayı kolaylaştırır.

İki deseni gösteren bir uygulama inşa edeceksiniz:

**Durumsuz Sohbet** - Her istek bağımsızdır. Model önceki mesajları hatırlamaz. Hızlı başlangıçta kullandığınız desen budur.

**Durumlu Sohbet** - Her istek sohbet geçmişini içerir. Model birden çok tur boyunca bağlamı korur. Üretim uygulamaları için gereklidir.

## Önkoşullar

- Azure aboneliği ve Azure OpenAI erişimi
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Not:** Java, Maven, Azure CLI ve Azure Developer CLI (azd) sağlanan geliştirme konteynerinde önceden yüklüdür.

> **Not:** Bu modül Azure OpenAI üzerinde GPT-5.2 kullanır. Dağıtım `azd up` komutu ile otomatik yapılandırılır - kodda model adını değiştirmeyin.

## Temel Problemi Anlama

Dil modelleri durumsuzdur. Her API çağrısı bağımsızdır. "Benim adım John" yazıp sonra "Adım ne?" diye sorarsanız, model kendinizi tanıttığınızı bilmez. Her isteği sanki şimdi başlanan ilk sohbetmiş gibi işler.

Basit Soru-Cevap için kabul edilebilir, ancak gerçek uygulamalar için yetersizdir. Müşteri hizmeti botları size ne söylediğinizi hatırlamalı. Kişisel asistanlar bağlama ihtiyaç duyar. Her çok tur sohbet bellek gerektirir.

<img src="../../../translated_images/tr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Durumsuz ve Durumlu Sohbetler" width="800"/>

*Durumsuz (bağımsız çağrılar) ve durumlu (bağlam farkında) sohbetler arasındaki fark*

## Tokenları Anlama

Sohbetlere başlamadan önce tokenları anlamak önemlidir - dil modellerinin işlediği temel metin birimleri:

<img src="../../../translated_images/tr/token-explanation.c39760d8ec650181.webp" alt="Token Açıklaması" width="800"/>

*Metnin tokenlara bölünme örneği - "I love AI!" 4 ayrı işleme birimine dönüşür*

Tokenlar, AI modellerinin metni ölçme ve işleme şeklidir. Kelimeler, noktalama işaretleri ve hatta boşluklar token olabilir. Modelinizin aynı anda işleyebileceği token sayısı (GPT-5.2 için 400.000, 272.000 giriş ve 128.000 çıkış tokenı) bir sınırlamadır. Tokenları anlamak sohbet uzunluğunu ve maliyetleri yönetmenize yardımcı olur.

## Belleğin Nasıl Çalıştığı

Sohbet belleği, durumsuz sorunu, sohbet geçmişini koruyarak çözer. İsteğinizi modele göndermeden önce, çerçeve ilgili önceki mesajları ekler. "Adım ne?" dediğinizde sistem tüm sohbet geçmişini gönderir ve model "Benim adım John" dediğinizi görür.

LangChain4j, bunu otomatik yapan bellek uygulamaları sağlar. Kaç mesaj saklayacağınızı seçersiniz, çerçeve bağlam penceresini yönetir.

<img src="../../../translated_images/tr/memory-window.bbe67f597eadabb3.webp" alt="Bellek Penceresi Kavramı" width="800"/>

*MessageWindowChatMemory, yakın zamanda gönderilen mesajların kayan penceresini tutar, eski mesajları otomatik düşürür*

## LangChain4j Kullanımı

Bu modül hızlı başlangıcı Spring Boot entegrasyonu ve sohbet belleği ekleyerek genişletir. Parçalar şöyle birleşir:

**Bağımlılıklar** - İki LangChain4j kütüphanesi ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Sohbet Modeli** - Azure OpenAI'yi Spring bean olarak yapılandırın ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder, `azd up` komutuyla ayarlanan ortam değişkenlerinden kimlik bilgilerini okur. `baseUrl`'i Azure uç noktanıza ayarlamak, OpenAI istemcisinin Azure OpenAI ile çalışmasını sağlar.

**Sohbet Belleği** - Sohbet geçmişini MessageWindowChatMemory ile izleyin ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` ile son 10 mesajı tutacak bellek oluşturun. Kullanıcı ve AI mesajlarını `UserMessage.from(text)` ve `AiMessage.from(text)` ile ekleyin. Geçmişe `memory.messages()` ile erişip modele gönderin. Servis, her sohbet kimliği için ayrı bellek örnekleri tutar, böylece birden çok kullanıcı eşzamanlı sohbet edebilir.

> **🤖 GitHub Copilot Chat ile Deneyin:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dosyasını açın ve sorun:
> - "MessageWindowChatMemory pencere dolduğunda hangi mesajları atmaya nasıl karar verir?"
> - "Belleği hafıza içi yerine veritabanı kullanarak özel olarak nasıl uygularım?"
> - "Eski sohbet geçmişini sıkıştırmak için özetleme nasıl eklenir?"

Durumsuz sohbet uç noktası belleği tamamen atlar - sadece hızlı başlangıç gibi `chatModel.chat(prompt)`. Durumlu uç nokta mesajları belleğe ekler, geçmişi alır ve bağlamla birlikte her isteğe dahil eder. Aynı model yapılandırması, farklı desenler.

## Azure OpenAI Altyapısını Dağıtma

**Bash:**
```bash
cd 01-introduction
azd up  # Aboneliği ve konumu seçin (eastus2 önerilir)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Aboneliği ve konumu seçin (eastus2 önerilir)
```

> **Not:** Eğer zaman aşımı hatası alırsanız (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), `azd up` komutunu tekrar çalıştırın. Azure kaynakları arka planda hala hazırlanıyor olabilir ve yeniden denemek dağıtımın tamamlanmasını sağlar.

Bu şunları yapar:
1. GPT-5.2 ve text-embedding-3-small modelleri ile Azure OpenAI kaynağı dağıtır
2. Kimlik bilgileriyle `.env` dosyasını proje köküne otomatik oluşturur
3. Gerekli tüm ortam değişkenlerini ayarlar

**Dağıtım sorunları mı yaşıyorsunuz?** Alt alan adı çakışmaları, manuel Azure Portal dağıtım adımları ve model yapılandırma rehberi için [Altyapı README'sine](infra/README.md) bakın.

**Dağıtımın başarılı olup olmadığını doğrulayın:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, vb. gösterilmeli.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY vb. gösterilmeli.
```

> **Not:** `azd up` komutu `.env` dosyasını otomatik oluşturur. Daha sonra güncelleme gerekirse ya dosyayı manuel düzenleyin ya da şu komutla yeniden üretin:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## Uygulamayı Yerel Çalıştırma

**Dağıtımı doğrulayın:**

Azure kimlik bilgileri bulunan `.env` dosyasının kök dizinde olduğundan emin olun:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermelidir
```

**Uygulamaları başlatın:**

**Seçenek 1: Spring Boot Panosu kullanmak (VS Code kullanıcıları için önerilir)**

Geliştirme konteynerinde Spring Boot Panosu uzantısı mevcuttur, bu uzantı tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlar. VS Code'da sol taraftaki Etkinlik Çubuğunda (Spring Boot simgesine bakın) bulunur.

Spring Boot Panosu'ndan:
- Tüm mevcut Spring Boot uygulamalarını görün
- Uygulamaları tek tıklamayla başlat/durdur
- Gerçek zamanlı uygulama günlüklerini izleyin
- Uygulama durumunu takip edin

Sadece "introduction" modülünün yanındaki oynat düğmesine tıklayarak modülü başlatabilir veya tüm modülleri aynı anda başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Panosu" width="400"/>

**Seçenek 2: Shell scriptleri kullanmak**

Tüm web uygulamalarını başlatın (modüller 01-04):

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Her iki script, ortam değişkenlerini kök `.env` dosyasından otomatik yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlatmadan önce tüm modülleri manuel derlemek isterseniz:
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

Tarayıcınızda http://localhost:8080 adresini açın.

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

Uygulama, yan yana iki sohbet uygulaması içeren bir web arayüzü sunar.

<img src="../../../translated_images/tr/home-screen.121a03206ab910c0.webp" alt="Uygulama Ana Ekranı" width="800"/>

*Basit Sohbet (durumsuz) ve Konuşmalı Sohbet (durumlu) seçeneklerini gösteren gösterge paneli*

### Durumsuz Sohbet (Sol Panel)

Önce bunu deneyin. "Adım John" deyin ve hemen ardından "Adım ne?" diye sorun. Model hatırlamayacak çünkü her mesaj bağımsız. Bu, temel dil modeli entegrasyonunun ana problemini gösterir - sohbet bağlamı yok.

<img src="../../../translated_images/tr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Durumsuz Sohbet Demo" width="800"/>

*AI önceki mesajdaki adınızı hatırlamaz*

### Durumlu Sohbet (Sağ Panel)

Şimdi aynı diziyi burada deneyin. "Adım John" deyin ve sonra "Adım ne?" Bu kez hatırlıyor. Fark, MessageWindowChatMemory - sohbet geçmişini tutar ve her istekle birlikte gönderir. Üretim sohbet AI'sı böyle çalışır.

<img src="../../../translated_images/tr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Durumlu Sohbet Demo" width="800"/>

*AI, sohbetin başında söylediklerinizi hatırlar*

Her iki panel de aynı GPT-5.2 modelini kullanır. Tek fark bellek. Bu, belleğin uygulamanıza ne kattığını ve gerçek senaryolarda neden gerekli olduğunu açıkça gösterir.

## Sonraki Adımlar

**Sonraki Modül:** [02-prompt-engineering - GPT-5.2 ile İstem Mühendisliği](../02-prompt-engineering/README.md)

---

**Gezinme:** [← Önceki: Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 02 - İstem Mühendisliği →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çeviri kullanımı sonucunda oluşabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->