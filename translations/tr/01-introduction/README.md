# Modül 01: LangChain4j ile Başlarken

## İçindekiler

- [Video Yürütme](../../../01-introduction)
- [Neler Öğreneceksiniz](../../../01-introduction)
- [Önkoşullar](../../../01-introduction)
- [Temel Problemi Anlamak](../../../01-introduction)
- [Tokenları Anlamak](../../../01-introduction)
- [Bellek Nasıl Çalışır](../../../01-introduction)
- [LangChain4j Nasıl Kullanılır](../../../01-introduction)
- [Azure OpenAI Altyapısını Dağıtmak](../../../01-introduction)
- [Uygulamayı Yerel Olarak Çalıştırmak](../../../01-introduction)
- [Uygulamayı Kullanmak](../../../01-introduction)
  - [Durumsuz Sohbet (Sol Panel)](../../../01-introduction)
  - [Durumlu Sohbet (Sağ Panel)](../../../01-introduction)
- [Sonraki Adımlar](../../../01-introduction)

## Video Yürütme

Bu modülle nasıl başlayacağınızı açıklayan canlı oturumu izleyin: [LangChain4j ile Başlarken - Canlı Oturum](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Neler Öğreneceksiniz

Hızlı başlangıcı tamamladıysanız, istem gönderip yanıt alma sürecini gördünüz. Bu temel ancak gerçek uygulamalar daha fazlasını gerektirir. Bu modül size, bağlamı hatırlayan ve durumu koruyan konuşma tabanlı yapay zeka nasıl oluşturulacağını öğretir - tek seferlik demo ile üretime hazır uygulama arasındaki fark budur.

Bu rehber boyunca Azure OpenAI'nin GPT-5.2'sini kullanacağız çünkü gelişmiş akıl yürütme yetenekleri, farklı kalıpların davranışlarını daha belirgin kılar. Bellek eklediğinizde farkı net görürsünüz. Bu, her bileşenin uygulamanıza ne kattığını anlamayı kolaylaştırır.

Her iki kalıbı da gösteren bir uygulama geliştireceksiniz:

**Durumsuz Sohbet** - Her istek bağımsızdır. Model önceki mesajları hatırlamaz. Bu, hızlı başlangıçta kullandığınız kalıptır.

**Durumlu Konuşma** - Her istek, konuşma geçmişini içerir. Model çoklu turlar boyunca bağlamı korur. Üretim uygulamalarının gereksinimidir.

## Önkoşullar

- Azure aboneliği ve Azure OpenAI erişimi
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Not:** Java, Maven, Azure CLI ve Azure Developer CLI (azd) sağlanan geliştirici konteynerinde önceden yüklüdür.

> **Not:** Bu modül Azure OpenAI üzerinde GPT-5.2 kullanır. Dağıtım `azd up` ile otomatik yapılandırılır - koddaki model adını değiştirmeyin.

## Temel Problemi Anlamak

Dil modelleri durumsuzdur. Her API çağrısı bağımsızdır. "Adım John" dediğinizde ve sonra "Adım neydi?" diye sorduğunuzda, model kendini az önce tanıttığınızı bilmez. Her isteği sanki ilk konuşmanızmış gibi ele alır.

Bu basit Soru-Cevap için iyidir ama gerçek uygulamalar için işe yaramaz. Müşteri hizmetleri botları söylediklerinizi hatırlamalı. Kişisel asistanlar bağlama ihtiyaç duyar. Çok turlu konuşmalar bellek gerektirir.

<img src="../../../translated_images/tr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Durumsuz vs Durumlu Konuşmalar" width="800"/>

*Durumsuz (bağımsız çağrılar) ve durumlu (bağlam farkındalığı olan) konuşmalar arasındaki fark*

## Tokenları Anlamak

Konuşmalara girmeden önce tokenları anlamak önemlidir - dil modellerinin işlediği temel metin birimleri:

<img src="../../../translated_images/tr/token-explanation.c39760d8ec650181.webp" alt="Token Açıklaması" width="800"/>

*Metnin tokenlara nasıl ayrıldığına örnek - "I love AI!" 4 ayrı işleme birimine dönüşür*

Tokenlar, yapay zeka modellerinin metni ölçme ve işleme yoludur. Kelimeler, noktalama işaretleri ve hatta boşluklar token olabilir. Modelin aynı anda işleyebileceği token sayısında bir sınır vardır (GPT-5.2 için 400.000, bunun 272.000'i girdi tokenı ve 128.000'i çıktı tokenı olabilir). Tokenları anlamak; konuşma uzunluğunu ve maliyetleri yönetmeye yardımcı olur.

## Bellek Nasıl Çalışır

Sohbet belleği, stateless (durumsuz) problemi, konuşma geçmişini koruyarak çözer. İsteğinizi modele göndermeden önce çerçeve ilgili önceki mesajları başa ekler. "Adım neydi?" dediğinizde sistem aslında tüm konuşma geçmişini gönderir, böylece model az önce "Adım John" dediğinizi görebilir.

LangChain4j, bunu otomatik olarak yöneten bellek uygulamaları sağlar. Kaç mesaj saklayacağınızı seçersiniz, çerçeve bağlam penceresini yönetir.

<img src="../../../translated_images/tr/memory-window.bbe67f597eadabb3.webp" alt="Bellek Penceresi Kavramı" width="800"/>

*MessageWindowChatMemory, yakın zamanda olan mesajlardan oluşan kayan pencereyi tutar, eski mesajları otomatik olarak düşürür*

## LangChain4j Nasıl Kullanılır

Bu modül hızlı başlangıcı spring boot entegrasyonu ve konuşma belleği ekleyerek genişletir. Parçalar şöyle uyuyor:

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

**Sohbet Modeli** - Azure OpenAI'yi Spring Bean olarak yapılandırın ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder, `azd up` tarafından ayarlanan ortam değişkenlerinden kimlik bilgilerini okur. `baseUrl`'yi Azure uç noktanıza ayarlamak OpenAI istemcisinin Azure OpenAI ile çalışmasını sağlar.

**Konuşma Belleği** - MessageWindowChatMemory ile sohbet geçmişini takip edin ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Belleği, son 10 mesajı tutmak için `withMaxMessages(10)` ile oluşturun. Kullanıcı ve yapay zeka mesajlarını `UserMessage.from(text)` ve `AiMessage.from(text)` tipli sarmalayıcılarla ekleyin. Geçmişi `memory.messages()` ile alın ve modele gönderin. Servis her konuşma kimliği için ayrı bellek örnekleri saklar, böylece birden çok kullanıcı aynı anda sohbet edebilir.

> **🤖 GitHub Copilot Chat ile deneyin:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dosyasını açıp sorun:
> - "MessageWindowChatMemory pencere dolduğunda hangi mesajları bırakmaya karar verir?"
> - "Belleği hafızada tutmak yerine bir veritabanı kullanarak nasıl uygulayabilirim?"
> - "Eski konuşma geçmişini özetlemek için nasıl bir sıkıştırma eklerim?"

Durumsuz sohbet uç noktası belleği tamamen atlar - sadece `chatModel.chat(prompt)` çalışır, hızlı başlangıç gibi. Durumlu uç nokta, mesajları belleğe ekler, geçmişi getirir ve bağlamı her isteğe dahil eder. Aynı model yapılandırması, farklı kalıplar.

## Azure OpenAI Altyapısını Dağıtmak

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

> **Not:** Eğer zaman aşımı hatası alırsanız (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), `azd up` komutunu yeniden çalıştırın. Azure kaynakları arka planda kuruluyor olabilir, yeniden denemek dağıtımın tamamlanmasını sağlar.

Bu işlemler:
1. GPT-5.2 ve text-embedding-3-small modelleri ile Azure OpenAI kaynağı dağıtır
2. Proje kökünde kimlik bilgileri ile `.env` dosyasını otomatik oluşturur
3. Gerekli tüm ortam değişkenlerini ayarlar

**Dağıtım sorunları mı yaşıyorsunuz?** Alt alan adı çakışmaları, manuel Azure Portal dağıtım adımları ve model yapılandırma rehberleri için [Altyapı README](infra/README.md) bölümüne bakın.

**Dağıtımın başarılı olduğunu doğrulayın:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, vb. göstermelidir.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY vb. göstermeli
```

> **Not:** `azd up` komutu `.env` dosyasını otomatik oluşturur. Daha sonra manuel güncelleme yapabilir veya yeniden oluşturabilirsiniz:
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

## Uygulamayı Yerel Olarak Çalıştırmak

**Dağıtımı doğrulayın:**

Azure kimlik bilgilerini içeren `.env` dosyasının kök dizinde olduğunu kontrol edin:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamaları başlatın:**

**Seçenek 1: Spring Boot Dashboard kullanarak (VS Code kullanıcıları için önerilir)**

Dev konteyner, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Dashboard eklentisi içerir. VS Code'da sol taraftaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulunabilir.

Spring Boot Dashboard'dan:
- Çalışma alanındaki tüm mevcut Spring Boot uygulamalarını görün
- Uygulamaları tek tıkla başlat/durdur
- Gerçek zamanlı uygulama günlüklerini takip edin
- Uygulama durumunu izleyin

"introduction"un yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri aynı anda başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell scriptlerle**

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

Her iki script de ortam değişkenlerini kök `.env` dosyasından otomatik yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Tüm modülleri manuel derlemeyi tercih ederseniz:
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

## Uygulamayı Kullanmak

Uygulama, yan yana iki sohbet uygulaması içeren bir web arayüzü sunar.

<img src="../../../translated_images/tr/home-screen.121a03206ab910c0.webp" alt="Uygulama Ana Ekranı" width="800"/>

*Hem Basit Sohbet (durumsuz) hem de Konuşma Tabanlı Sohbet (durumlu) seçeneklerinin gösterildiği gösterge paneli*

### Durumsuz Sohbet (Sol Panel)

Önce bunu deneyin. "Adım John" deyin, hemen ardından "Adım neydi?" diye sorun. Model hatırlamaz çünkü her mesaj bağımsızdır. Bu, temel dil modeli entegrasyonundaki temel problemi gösterir - konuşma bağlamı yoktur.

<img src="../../../translated_images/tr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Durumsuz Sohbet Demo" width="800"/>

*Yapay Zeka önceki mesajdaki adınızı hatırlamaz*

### Durumlu Sohbet (Sağ Panel)

Şimdi aynı sırayı burada deneyin. "Adım John" ve sonra "Adım neydi?" deyin. Bu sefer hatırlar. Fark, MessageWindowChatMemory - konuşma geçmişini korur ve her isteğe dahil eder. Üretim konuşma tabanlı yapay zekanın çalışma şekli budur.

<img src="../../../translated_images/tr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Durumlu Sohbet Demo" width="800"/>

*Yapay Zeka konuşmanın önceki bölümünden adınızı hatırlar*

İki panel de aynı GPT-5.2 modelini kullanır. Tek fark bellek. Bu, belleğin uygulamanıza ne kattığını ve gerçek kullanım senaryoları için neden vazgeçilmez olduğunu açıkça gösterir.

## Sonraki Adımlar

**Sonraki Modül:** [02-prompt-engineering - GPT-5.2 ile İstem Mühendisliği](../02-prompt-engineering/README.md)

---

**Gezinme:** [← Önceki: Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 02 - İstem Mühendisliği →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı nedeniyle oluşabilecek herhangi bir yanlış anlama veya yorumdan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->