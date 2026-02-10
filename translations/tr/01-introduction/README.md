# Modül 01: LangChain4j ile Başlarken

## İçindekiler

- [Öğrenecekleriniz](../../../01-introduction)
- [Gereksinimler](../../../01-introduction)
- [Temel Sorunu Anlamak](../../../01-introduction)
- [Token'ları Anlamak](../../../01-introduction)
- [Belleğin Nasıl Çalıştığı](../../../01-introduction)
- [LangChain4j Nasıl Kullanılır](../../../01-introduction)
- [Azure OpenAI Altyapısını Dağıtma](../../../01-introduction)
- [Uygulamayı Yerelde Çalıştırma](../../../01-introduction)
- [Uygulamayı Kullanma](../../../01-introduction)
  - [Durumsuz Sohbet (Sol Panel)](../../../01-introduction)
  - [Durumlu Sohbet (Sağ Panel)](../../../01-introduction)
- [Sonraki Adımlar](../../../01-introduction)

## Öğrenecekleriniz

Hızlı başlangıcı tamamladıysanız, istemleri nasıl gönderip yanıt alacağınızı gördünüz. Bu temel dayanak, ancak gerçek uygulamalar daha fazlasını gerektirir. Bu modül, bağlamı hatırlayan ve durumu koruyan konuşma AI'si nasıl oluşturulacağını öğretir - bir kerelik demo ile üretim hazır uygulama arasındaki fark.

Bu rehber boyunca Azure OpenAI'nin GPT-5.2'sini kullanacağız çünkü gelişmiş mantık yürütme yetenekleri farklı desenlerin davranışlarını daha belirgin kılar. Bellek eklediğinizde farkı açıkça görürsünüz. Bu, her bileşenin uygulamanıza ne kattığını anlamayı kolaylaştırır.

Her iki deseni gösteren bir uygulama oluşturacaksınız:

**Durumsuz Sohbet** - Her istek bağımsızdır. Model önceki mesajları hatırlamaz. Hızlı başlangıçta kullandığınız desen budur.

**Durumlu Konuşma** - Her istek konuşma geçmişini içerir. Model birden fazla tur boyunca bağlamı korur. Üretim uygulamalarının gerektirdiği budur.

## Gereksinimler

- Azure aboneliği ile Azure OpenAI erişimi
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Not:** Java, Maven, Azure CLI ve Azure Developer CLI (azd) sağlanan devcontainer içinde önceden kuruludur.

> **Not:** Bu modül Azure OpenAI üzerinde GPT-5.2 kullanır. Dağıtım `azd up` komutuyla otomatik yapılandırılır - kodda model adını değiştirmeyin.

## Temel Sorunu Anlamak

Dil modelleri durumsuzdur. Her API çağrısı bağımsızdır. "Benim adım John" yazarsanız ve sonra "Adım ne?" diye sorarsanız, model daha önce kendinizi tanıttığınızı bilmez. Her isteği, yapıldığını düşündüğü ilk konuşma gibi işler.

Bu basit Soru-Cevap için iyidir ama gerçek uygulamalar için işe yaramaz. Müşteri hizmetleri botlarının söylediklerinizi hatırlaması gerekir. Kişisel asistanların bağlama ihtiyacı vardır. Her çok tur konuşma belleği gerektirir.

<img src="../../../translated_images/tr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Durumsuz ve Durumlu Konuşmalar" width="800"/>

*Durumsuz (bağımsız çağrılar) ve durumlu (bağlam farkında) konuşmalar arasındaki fark*

## Token'ları Anlamak

Konuşmalara dalmadan önce, dil modellerinin işlediği temel metin birimleri olan token'ları anlamak önemlidir:

<img src="../../../translated_images/tr/token-explanation.c39760d8ec650181.webp" alt="Token Açıklaması" width="800"/>

*Metnin nasıl token'lara bölündüğüne örnek - "I love AI!" 4 ayrı işlem birimine dönüşür*

Token'lar AI modellerinin metni ölçüp işlediği birimlerdir. Kelimeler, noktalama işaretleri ve hatta boşluklar token olabilir. Modelinizin aynı anda işleyebileceği token sayısının bir sınırı vardır (GPT-5.2 için 400.000 toplam, 272.000 giriş tokenı ve 128.000 çıkış tokenı). Token'ları anlamak, konuşma uzunluğunu ve maliyetleri yönetmenize yardımcı olur.

## Belleğin Nasıl Çalıştığı

Sohbet belleği, durumsuz sorunu konuşma geçmişini koruyarak çözer. Modelinize isteği göndermeden önce framework ilgili önceki mesajları öne ekler. "Adım ne?" diye sorduğunuzda, sistem aslında tüm konuşma geçmişini gönderir, böylece model daha önce "Benim adım John" dediğinizi görebilir.

LangChain4j, bu işlemi otomatik yapan bellek uygulamaları sağlar. Kaç mesaj saklayacağınızı seçersiniz, framework bağlam penceresini yönetir.

<img src="../../../translated_images/tr/memory-window.bbe67f597eadabb3.webp" alt="Bellek Penceresi Kavramı" width="800"/>

*MessageWindowChatMemory, son mesajların kayan penceresini korur ve eski mesajları otomatik düşürür*

## LangChain4j Nasıl Kullanılır

Bu modül, hızlı başlangıcı Spring Boot ile entegre ederek ve konuşma belleği ekleyerek genişletir. Parçalar şöyle bir araya gelir:

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

Builder, `azd up` tarafından ayarlanan ortam değişkenlerinden kimlik bilgilerini okur. `baseUrl` değerini Azure uç noktanıza ayarlamak OpenAI istemcisinin Azure OpenAI ile çalışmasını sağlar.

**Konuşma Belleği** - MessageWindowChatMemory ile sohbet geçmişini takip edin ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Belleği son 10 mesajı tutacak şekilde `withMaxMessages(10)` ile oluşturun. Kullanıcı ve AI mesajlarını tipli sarmalayıcılar `UserMessage.from(text)` ve `AiMessage.from(text)` ile ekleyin. Geçmişi `memory.messages()` ile alın ve modele gönderin. Servis, her konuşma kimliği için ayrı bellek örnekleri tutar, böylece çoklu kullanıcılar aynı anda sohbet edebilir.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) dosyasını açın ve sorun:
> - "MessageWindowChatMemory pencere dolduğunda hangi mesajları atmaya nasıl karar veriyor?"
> - "Bellek depolamayı hafıza içi yerine veritabanı kullanarak özelleştirebilir miyim?"
> - "Eski konuşma geçmişini özetleyerek sıkıştırmak için nasıl bir özetleme ekleyebilirim?"

Durumsuz sohbet uç noktası belleği tamamen atlar - hızlı başlangıçtaki gibi sadece `chatModel.chat(prompt)`. Durumlu uç nokta mesajları belleğe ekler, geçmişi alır ve bağlamı her isteğe dahil eder. Aynı model yapılandırması, farklı desenler.

## Azure OpenAI Altyapısını Dağıtma

**Bash:**
```bash
cd 01-introduction
azd up  # Aboneliği ve konumu seçin (eastus2 önerilir)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Abonelik ve konum seçin (eastus2 önerilir)
```

> **Not:** Eğer zaman aşımı hatası (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) alırsanız, sadece `azd up` komutunu tekrar çalıştırın. Azure kaynakları arka planda hala oluşturuluyor olabilir ve yeniden denemek, kaynaklar terminal duruma ulaştığında dağıtımın tamamlanmasını sağlar.

Bu şunları yapacak:
1. GPT-5.2 ve text-embedding-3-small modelleri ile Azure OpenAI kaynağı dağıtır
2. Proje kökünde kimlik bilgileriyle `.env` dosyasını otomatik oluşturur
3. Gerekli tüm ortam değişkenlerini ayarlar

**Dağıtımda sorun mu yaşıyorsunuz?** Alt alan adı çakışmaları, manuel Azure Portal dağıtım adımları ve model yapılandırma rehberi için [Altyapı README dosyasına](infra/README.md) bakın.

**Dağıtımın başarılı olduğunu doğrulayın:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY vb. göstermelidir.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY vb. göstermelidir.
```

> **Not:** `azd up` komutu `.env` dosyasını otomatik oluşturur. Sonradan güncellemeniz gerekirse, ya `.env` dosyasını manuel düzenleyebilir ya da şu komutla yeniden oluşturabilirsiniz:
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

## Uygulamayı Yerelde Çalıştırma

**Dağıtımı doğrulayın:**

Kök dizinde Azure kimlik bilgileri ile `.env` dosyasının var olduğundan emin olun:

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

Devcontainer içinde Spring Boot Dashboard uzantısı bulunur, bu uzantı tüm Spring Boot uygulamalarını görsel olarak yönetmenizi sağlar. VS Code sol tarafındaki Aktivite Çubuğunda Spring Boot simgesini arayın.

Spring Boot Dashboard'dan:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıklamayla başlatabilir/durdurabilirsiniz
- Günlükleri gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

Bu modülü başlatmak için "introduction" yanındaki oynat düğmesine tıklayın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell scriptleri kullanarak**

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

Veya sadece bu modülü başlatın:

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

Her iki script de kök dizindeki `.env` dosyasından ortam değişkenlerini otomatik yükler ve JAR dosyaları yoksa oluşturur.

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

Tarayıcıda http://localhost:8080 adresini açın.

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

## Uygulamayı Kullanma

Uygulama, yan yana iki sohbet uygulaması sunan bir web arayüzü sağlar.

<img src="../../../translated_images/tr/home-screen.121a03206ab910c0.webp" alt="Uygulama Ana Ekranı" width="800"/>

*Simple Chat (durumsuz) ve Conversational Chat (durumlu) seçeneklerini gösteren kontrol paneli*

### Durumsuz Sohbet (Sol Panel)

Önce bunu deneyin. "Benim adım John" deyin ve hemen ardından "Adım ne?" diye sorun. Model hatırlamaz çünkü her mesaj bağımsızdır. Bu temel dil modeli entegrasyonunun ana sorunu olan konuşma bağlamının yokluğunu gösterir.

<img src="../../../translated_images/tr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Durumsuz Sohbet Demo" width="800"/>

*AI önceki mesajdan adınızı hatırlamıyor*

### Durumlu Sohbet (Sağ Panel)

Şimdi aynı diziyi burada deneyin. "Benim adım John" deyin ve ardından "Adım ne?" sorun. Bu kez hatırlıyor. Fark MessageWindowChatMemory'dir - konuşma geçmişini tutar ve her istekle birlikte gönderir. Üretim konuşma AI'sinin çalışma şekli budur.

<img src="../../../translated_images/tr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Durumlu Sohbet Demo" width="800"/>

*AI önceki konuşmadan adınızı hatırlıyor*

Her iki panelde de aynı GPT-5.2 modeli kullanılır. Tek fark bellek. Bu, belleğin uygulamanıza ne getirdiğini ve gerçek kullanım durumları için neden zorunlu olduğunu net şekilde gösterir.

## Sonraki Adımlar

**Sonraki Modül:** [02-prompt-engineering - GPT-5.2 ile İstem Mühendisliği](../02-prompt-engineering/README.md)

---

**Gezinme:** [← Önceki: Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 02 - İstem Mühendisliği →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Önemli bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalar ya da yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->