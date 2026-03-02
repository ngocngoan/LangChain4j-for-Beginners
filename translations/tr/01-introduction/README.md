# Modül 01: LangChain4j ile Başlarken

## İçindekiler

- [Video Turu](../../../01-introduction)
- [Öğrenecekleriniz](../../../01-introduction)
- [Ön Koşullar](../../../01-introduction)
- [Temel Problemi Anlamak](../../../01-introduction)
- [Token'ları Anlamak](../../../01-introduction)
- [Bellek Nasıl Çalışır](../../../01-introduction)
- [Bunun LangChain4j ile Kullanımı](../../../01-introduction)
- [Azure OpenAI Altyapısını Yayınla](../../../01-introduction)
- [Uygulamayı Yerel Olarak Çalıştır](../../../01-introduction)
- [Uygulamanın Kullanımı](../../../01-introduction)
  - [Durumsuz Sohbet (Sol Panel)](../../../01-introduction)
  - [Durumlu Sohbet (Sağ Panel)](../../../01-introduction)
- [Sonraki Adımlar](../../../01-introduction)

## Video Turu

Bu modüle nasıl başlanacağını anlatan canlı oturumu izleyin:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j ile Başlarken - Canlı Oturum" width="800"/></a>

## Öğrenecekleriniz

Hızlı başlangıçta, GitHub Modellerini kullanarak istemler gönderdiniz, araçları çağırdınız, bir RAG hattı kurdunuz ve güvenlik sınırlarını test ettiniz. Bu demolar olanakları gösterdi — şimdi Azure OpenAI ve GPT-5.2'ye geçip üretim tarzı uygulamalar geliştirmeye başlıyoruz. Bu modül, bağlamı hatırlayan ve durumu koruyan konuşma yapay zekasına odaklanıyor — hızlı başlangıç demolarının perde arkasında kullandığı ama açıklamadığı kavramlar.

Bu kılavuz boyunca Azure OpenAI'nin GPT-5.2'sini kullanacağız çünkü gelişmiş akıl yürütme yetenekleri farklı desenlerin davranışlarını daha belirgin kılıyor. Bellek eklediğinizde farkı açıkça göreceksiniz. Bu, uygulamanıza her bir bileşenin ne kattığını anlamayı kolaylaştırır.

Her iki deseni gösteren bir uygulama geliştireceksiniz:

**Durumsuz Sohbet** - Her istek bağımsız. Model önceki mesajları hatırlamaz. Hızlı başlangıçta kullandığınız desen budur.

**Durumlu Konuşma** - Her istek, konuşma geçmişini içerir. Model birden fazla adımlı konuşmada bağlamı korur. Üretim uygulamalarının gerektirdiği budur.

## Ön Koşullar

- Azure aboneliği ve Azure OpenAI erişimi
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Not:** Java, Maven, Azure CLI ve Azure Developer CLI (azd) sağlanan geliştirme konteynerinde önceden yüklüdür.

> **Not:** Bu modül Azure OpenAI üzerinde GPT-5.2 kullanır. Dağıtım `azd up` ile otomatik yapılandırılır — koddaki model adını değiştirmeyin.

## Temel Problemi Anlamak

Dil modelleri durumsuzdur. Her API çağrısı bağımsızdır. "Adım John" dediğinizde ve sonra "Adım ne?" diye sorduğunuzda, model kendinizi tanıttığınızı bilmez. Her isteği yaşayabileceğiniz ilk konuşma gibi değerlendirir.

Bu, basit soru-cevaplar için sorun olmaz ancak gerçek uygulamalar için işe yaramaz. Müşteri hizmeti botları size söylediklerinizi hatırlamalıdır. Kişisel asistanlar bağlama ihtiyaç duyar. Her çok adımlı konuşma belleğe ihtiyaç duyar.

Aşağıdaki diagram iki yaklaşımı karşılaştırır — solda, isminizi unutan durumsuz çağrı; sağda ise isminizi hatırlayan ChatMemory destekli durumlu çağrı.

<img src="../../../translated_images/tr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Durumsuz ve Durumlu Konuşmalar" width="800"/>

*Durumsuz (bağımsız çağrılar) ve durumlu (bağlam farkında) konuşmalar arasındaki fark*

## Token'ları Anlamak

Konuşmalara dalmadan önce, dil modellerinin işlediği temel metin birimleri olan token’ları anlamak önemlidir:

<img src="../../../translated_images/tr/token-explanation.c39760d8ec650181.webp" alt="Token Açıklaması" width="800"/>

*Metnin token’lara nasıl bölündüğüne örnek - "I love AI!" 4 ayrı işleme birimine dönüşür*

Token’lar yapay zeka modellerinin metni ölçme ve işleme yöntemidir. Kelimeler, noktalama işaretleri ve hatta boşluklar token olabilir. Modelinizin bir kerede işleyebileceği token sayısı sınırlıdır (GPT-5.2 için 400.000, 272.000 giriş token’ı ve 128.000 çıkış token’ı ile). Token’ları anlamak konuşma uzunluğunu ve maliyetleri yönetmenize yardımcı olur.

## Bellek Nasıl Çalışır

Konuşma belleği, durumsuz problemi, konuşma geçmişini koruyarak çözer. Modele isteğinizi göndermeden önce, çerçeve ilgili önceki mesajları başa ekler. "Adım ne?" diye sorduğunuzda, sistem tüm konuşma geçmişini gönderir; böylece model sizin daha önce "Adım John" dediğinizi görür.

LangChain4j bunu otomatik yapan bellek uygulamaları sağlar. Kaç mesaj korunacağını siz seçersiniz, çerçeve bağlam penceresini yönetir. Aşağıdaki diagram MessageWindowChatMemory’nin yakın zamandaki mesajların kayan penceresini nasıl yönettiğini gösterir.

<img src="../../../translated_images/tr/memory-window.bbe67f597eadabb3.webp" alt="Bellek Penceresi Kavramı" width="800"/>

*MessageWindowChatMemory, eski mesajları otomatik atarak yakın zamandaki mesajlar için kayan bir pencere tutar*

## Bunun LangChain4j ile Kullanımı

Bu modül, hızlı başlangıcı genişleterek Spring Boot entegrasyonu ve konuşma belleği ekler. Parçalar şu şekilde birleşir:

**Bağımlılıklar** — İki LangChain4j kütüphanesi ekleyin:

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

**Sohbet Modeli** - Azure OpenAI’yi Spring bean olarak yapılandırın ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder, `azd up` ile ayarlanmış ortam değişkenlerinden kimlik bilgilerini okur. `baseUrl`’i Azure uç noktanıza ayarlamak, OpenAI istemcisinin Azure OpenAI ile çalışmasını sağlar.

**Konuşma Belleği** - Mesaj geçmişini MessageWindowChatMemory ile izleyin ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` ile belleği son 10 mesajı tutacak şekilde oluşturun. Kullanıcı ve yapay zeka mesajlarını tipli sarmalayıcılarla ekleyin: `UserMessage.from(text)` ve `AiMessage.from(text)`. Geçmişi `memory.messages()` ile alın ve modele gönderin. Servis, her konuşma kimliği için ayrı bellek örnekleri tutar; böylece birden çok kullanıcı aynı anda sohbet edebilir.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dosyasını açın ve sorun:
> - "MessageWindowChatMemory pencere dolduğunda hangi mesajların atılmasına nasıl karar veriyor?"
> - "Belleği bellek içi yerine veritabanı kullanarak özel olarak nasıl uygularım?"
> - "Eski konuşma geçmişini sıkıştırmak için özetlemeyi nasıl eklerim?"

Durumsuz sohbet uç noktası belleği tamamen atlar — hızlı başlangıçta olduğu gibi sadece `chatModel.chat(prompt)`. Durumlu uç noktada mesajlar belleğe eklenir, geçmiş alınır ve her istekle o bağlam dahil edilir. Aynı model yapılandırması, farklı desenler.

## Azure OpenAI Altyapısını Yayınla

**Bash:**
```bash
cd 01-introduction
azd up  # Abonelik ve konumu seçin (eastus2 önerilir)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Aboneliği ve konumu seçin (eastus2 önerilir)
```

> **Not:** Eğer zaman aşımı hatası (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) alırsanız, `azd up` komutunu tekrar çalıştırın. Azure kaynakları arka planda hâlâ dağıtılıyor olabilir; yeniden denemek, kaynaklar terminal duruma geldiğinde dağıtımın tamamlanmasını sağlar.

Bu işlemler:
1. GPT-5.2 ve text-embedding-3-small modelleriyle Azure OpenAI kaynağı dağıtır
2. Proje kökünde kimlik bilgileriyle `.env` dosyasını otomatik oluşturur
3. Gerekli tüm ortam değişkenlerini ayarlar

**Dağıtım sorunları mı yaşıyorsunuz?** Alt alan adı adı çatışmaları, manuel Azure Portal dağıtım adımları ve model yapılandırma rehberi dahil ayrıntılı hata giderme için [Altyapı README dosyasına](infra/README.md) bakın.

**Dağıtımın başarılı olup olmadığını doğrulayın:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, vb. gösterilmelidir.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY vb. gösterilmelidir.
```

> **Not:** `azd up` komutu `.env` dosyasını otomatik oluşturur. Daha sonra güncellemeniz gerekirse, `.env` dosyasını manuel olarak düzenleyebilir veya yeniden oluşturmak için şu komutları çalıştırabilirsiniz:
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

## Uygulamayı Yerel Olarak Çalıştır

**Dağıtımı doğrulayın:**

Azure kimlik bilgileriyle `.env` dosyasının kök dizinde olduğundan emin olun. Modül dizininden (`01-introduction/`) şu komutları çalıştırın:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamaları başlatın:**

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteynerinde, tüm Spring Boot uygulamalarını görsel olarak yönetmek için Spring Boot Dashboard eklentisi vardır. VS Code’un solundaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulunabilir.

Spring Boot Dashboard’dan:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıklamayla başlat/durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu izleyebilirsiniz

`introduction` modülünün yanındaki oynat düğmesine basarak bu modülü başlatın veya tüm modülleri aynı anda çalıştırın.

<img src="../../../translated_images/tr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code’da Spring Boot Dashboard — tüm modülleri bir yerden başlat, durdur ve izle*

**Seçenek 2: Kabuk betikleri kullanımı**

Tüm web uygulamalarını (modüller 01-04) başlat:

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

Ya da sadece bu modülü başlat:

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

Her iki betik de kök `.env` dosyasından ortam değişkenlerini otomatik yükler ve JAR dosyaları yoksa oluşturur.

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

Tarayıcınızda http://localhost:8080 adresini açın.

**Durdurmak için:**

**Bash:**
```bash
./stop.sh  # Yalnızca bu modül
# Veya
cd .. && ./stop-all.sh  # Tüm modüller
```

**PowerShell:**
```powershell
.\stop.ps1  # Sadece bu modül
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```

## Uygulamanın Kullanımı

Uygulama, yan yana iki sohbet uygulamasıyla web arayüzü sağlar.

<img src="../../../translated_images/tr/home-screen.121a03206ab910c0.webp" alt="Uygulama Ana Ekranı" width="800"/>

*Hem Basit Sohbet (durumsuz) hem de Konuşmalı Sohbet (durumlu) seçeneklerini gösteren pano*

### Durumsuz Sohbet (Sol Panel)

Önce bunu deneyin. "Adım John" deyin ve hemen ardından "Adım ne?" diye sorun. Model hatırlamaz çünkü her mesaj bağımsızdır. Bu, temel dil modeli entegrasyonundaki ana sorunu gösterir — konuşma bağlamı yok.

<img src="../../../translated_images/tr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Durumsuz Sohbet Demo" width="800"/>

*Yapay zeka önceki mesajdaki isminizi hatırlamaz*

### Durumlu Sohbet (Sağ Panel)

Şimdi aynı diziyi burada deneyin. "Adım John" deyin ve sonra "Adım ne?" diye sorun. Bu sefer hatırlar. Fark MessageWindowChatMemory'dir — konuşma geçmişini tutar ve her istekle birlikte gönderir. Üretim konuşma yapay zekası böyle çalışır.

<img src="../../../translated_images/tr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Durumlu Sohbet Demo" width="800"/>

*Yapay zeka önceki konuşmadan isminizi hatırlar*

Her iki panelde de aynı GPT-5.2 modeli kullanılır. Tek fark bellek. Bu, belleğin uygulamanıza ne kattığını ve gerçek kullanım durumları için neden gerekli olduğunu açıkça gösterir.

## Sonraki Adımlar

**Sonraki Modül:** [02-prompt-engineering - GPT-5.2 ile İstem Mühendisliği](../02-prompt-engineering/README.md)

---

**Geçiş:** [← Önceki: Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 02 - İstem Mühendisliği →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerde hatalar veya yanlışlıklar olabileceğini lütfen unutmayın. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi tavsiye edilir. Bu çevirinin kullanımıyla ortaya çıkabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->