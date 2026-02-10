# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Ön Gereksinimler](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Bunun LangChain4j ile Kullanımı](../../../02-prompt-engineering)
- [Temel Desenler](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarını Kullanmak](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Desenleri Keşfetmek](../../../02-prompt-engineering)
  - [Düşük vs Yüksek İsteklilik](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Ön Bilgilendirmeleri)](../../../02-prompt-engineering)
  - [Kendi Kendini Değerlendiren Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Turlu Sohbet](../../../02-prompt-engineering)
  - [Adım Adım Muhakeme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekten Neler Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Neler Öğreneceksiniz

Önceki modülde, belleğin konuşma tabanlı yapay zekayı nasıl mümkün kıldığına bakmış ve temel etkileşimler için GitHub Modellerini kullanmıştınız. Şimdi ise soruları nasıl sorduğunuza—yani promptlara—odaklanacağız ve Azure OpenAI’ın GPT-5.2’sini kullanacağız. Promptlarınızı nasıl yapılandırdığınız, aldığınız cevapların kalitesini ciddi şekilde etkiler.

GPT-5.2’yi kullanacağız çünkü muhakeme kontrolü getiriyor—modelin cevap vermeden önce ne kadar düşünmesini istediğinizi belirtebiliyorsunuz. Bu, farklı prompt stratejilerini daha net görmenizi sağlar ve hangi yaklaşımı ne zaman kullanmanız gerektiğini anlamanıza yardımcı olur. Ayrıca GPT-5.2 için Azure’ın GitHub Modellerine kıyasla daha az hız sınırı olması da avantaj sağlayacak.

## Ön Gereksinimler

- Modül 01 tamamlama (Azure OpenAI kaynakları kurulu)
- Kök dizinde `.env` dosyası (Modül 01'de `azd up` ile oluşturuldu) ve Azure kimlik bilgileri

> **Not:** Modül 01'i tamamlamadıysanız, lütfen önce oradaki dağıtım talimatlarını izleyin.

## Prompt Mühendisliğini Anlamak

Prompt mühendisliği, ihtiyaç duyduğunuz sonuçları tutarlı şekilde alacak şekilde giriş metni tasarlamaktır. Sadece soru sormak değil, modelin tam olarak ne istediğinizi ve nasıl sunacağını anlamasını sağlayacak şekilde istekleri yapılandırmaktır.

Bunu bir meslektaşınıza talimat vermek gibi düşünün. "Hatanı düzelt" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer hatasını null kontrolü ekleyerek düzelt" ise spesifiktir. Dil modelleri de aynı şekilde çalışır—kesinlik ve yapı önemlidir.

## Bunun LangChain4j ile Kullanımı

Bu modül, önceki modüllerde kullanılan aynı LangChain4j temelini kullanarak gelişmiş promptlama desenlerini gösteriyor; odak nokta prompt yapısı ve muhakeme kontrolü.

<img src="../../../translated_images/tr/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Akışı" width="800"/>

*LangChain4j'in promptlarınızı Azure OpenAI GPT-5.2’ye nasıl bağladığı*

**Bağımlılıklar** - Modül 02, `pom.xml` içinde tanımlanmış aşağıdaki langchain4j bağımlılıklarını kullanır:
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

**OpenAiOfficialChatModel Yapılandırması** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chat modeli, Azure OpenAI uç noktalarını destekleyen OpenAI Resmi istemcisi kullanılarak Spring bean olarak manuel yapılandırılır. Modül 01’den farkı, modeli yapılandırmak değil, `chatModel.chat()` metoduna gönderilen promptların nasıl yapılandırıldığıdır.

**Sistem ve Kullanıcı Mesajları** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j, netlik için mesaj türlerini ayırır. `SystemMessage` yapay zekanın davranışını ve bağlamını belirler (örn. "Sen bir kod denetleyicisin"), `UserMessage` ise gerçek talebi içerir. Bu ayrım, farklı kullanıcı sorguları arasında tutarlı AI davranışı sağlar.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/tr/message-types.93e0779798a17c9d.webp" alt="Mesaj Türleri Mimarisı" width="800"/>

*SystemMessage kalıcı bağlam sağlarken UserMessage’lar bireysel talepleri içerir*

**Çok Turlu için MessageWindowChatMemory** - Çok turlu konuşma deseni için, Modül 01’den `MessageWindowChatMemory` yeniden kullanılır. Her oturum, eşzamanlı konuşmalar arasında bağlam karışmasını önleyen `Map<String, ChatMemory>` içinde kendi belleğine sahiptir.

**Prompt Şablonları** - Buradaki gerçek odak prompt mühendisliği; yeni LangChain4j API'leri değil. Her desen (düşük istek, yüksek istek, görev yürütme vb.) aynı `chatModel.chat(prompt)` metodunu kullanır ancak dikkatlice yapılandırılmış prompt metinleri ile. XML etiketleri, talimatlar ve biçimlendirme prompt metninin parçasıdır, LangChain4j özellikleri değildir.

**Muhakeme Kontrolü** - GPT-5.2'nin muhakeme çabası, "en fazla 2 muhakeme adımı" veya "detaylıca keşfet" gibi prompt talimatlarıyla kontrol edilir. Bunlar prompt mühendisliği teknikleridir, LangChain4j yapılandırmaları değildir. Kütüphane sadece promptlarınızı modele iletir.

Ana mesaj: LangChain4j altyapıyı sağlar (model bağlantısı için [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), bellek, mesaj yönetimi için [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), bu modül ise o altyapı içinde etkili promptlar hazırlamayı öğretir.

## Temel Desenler

Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı cevap ister, bazıları derin düşünce. Bazıları görünür muhakeme ister, bazıları sadece sonuç. Bu modül sekiz promptlama desenini kapsar—her biri farklı senaryolara optimize edilmiştir. Hepsini deneyimleyerek hangi yaklaşımın ne zaman işe yaradığını öğreneceksiniz.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz Promptlama Deseni" width="800"/>

*Sekiz prompt mühendisliği deseninin genel görünümü ve kullanım durumları*

<img src="../../../translated_images/tr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Muhakeme Çabası Karşılaştırması" width="800"/>

*Düşük isteklilik (hızlı, doğrudan) vs Yüksek isteklilik (detaylı, keşifçi) muhakeme yaklaşımları*

**Düşük İsteklilik (Hızlı & Odaklı)** - Basit sorular için, hızlı ve doğrudan cevaplar istersiniz. Model minimal muhakeme yapar - en fazla 2 adım. Hesaplamalar, aramalar veya basit sorular için kullanın.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot ile Keşfedin:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dosyasını açın ve sorun:
> - "Düşük isteklilik ve yüksek isteklilik promptlama desenleri arasındaki fark nedir?"
> - "Promptlardaki XML etiketleri AI’nın cevap yapısını nasıl düzenlemeye yardımcı olur?"
> - "Kendi kendini değerlendirme desenlerini doğrudan talimatlara ne zaman tercih etmeliyim?"

**Yüksek İsteklilik (Derin & Detaylı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model detaylı şekilde keşfeder ve ayrıntılı muhakeme sunar. Sistem tasarımı, mimari kararlar veya karmaşık araştırmalar için kullanılır.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım Adım İlerleme)** - Çok adımlı iş akışları için. Model önceden bir plan belirtir, her adımı ilerlerken anlatır, sonra özet sunar. Geçişler, uygulamalar veya çok adımlı süreçler için uygundur.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Zincirleme Düşünce (Chain-of-Thought) promptlama modeli muhakeme sürecini açıkça göstermeye zorlar, karmaşık görevlerde doğruluğu artırır. Adım adım dağılım hem insanlar hem AI için mantığın anlaşılmasını kolaylaştırır.

> **🤖 GitHub Copilot Chat ile Deneyin:** Bu desen hakkında sorun:
> - "Uzun süren işlemler için görev yürütme desenini nasıl uyarlardım?"
> - "Üretim uygulamalarında araç ön bilgilendirmeleri için en iyi yapılandırma yöntemleri nedir?"
> - "Ara ilerleme güncellemelerini UI’da nasıl yakalar ve gösteririm?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Deseni" width="800"/>

*Planla → Yürüt → Özetle - çok adımlı görevler için iş akışı*

**Kendi Kendini Değerlendiren Kod** - Üretim kalitesinde kod işlemek için. Model kod üretir, kalite kriterleriyle karşılaştırır, tekrar tekrar iyileştirir. Yeni özellikler veya servisler geliştirirken kullanışlıdır.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kendi Kendini Değerlendirme Döngüsü" width="800"/>

*Tekrarlamalı iyileştirme döngüsü - üret, değerlendir, sorunları belirle, geliştir, tekrar et*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model kodu sabit bir çerçeve (doğruluk, uygulamalar, performans, güvenlik) kullanarak inceler. Kod incelemeleri veya kalite değerlendirmeleri için uygun.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 GitHub Copilot Chat ile Deneyin:** Yapılandırılmış analiz hakkında sorun:
> - "Farklı kod inceleme türleri için analiz çerçevesi nasıl özelleştirilir?"
> - "Yapılandırılmış çıktıyı programatik olarak ayrıştırıp nasıl kullanırım?"
> - "Farklı inceleme oturumlarında tutarlı şiddet seviyeleri nasıl sağlanır?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Deseni" width="800"/>

*Şiddet seviyeleriyle tutarlı kod incelemeleri için dört kategorili çerçeve*

**Çok Turlu Sohbet** - Bağlam gerektiren konuşmalar için. Model önceki mesajları hatırlar ve üzerine inşa eder. Etkileşimli yardım seansları veya karmaşık Q&A için uygundur.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/tr/context-memory.dff30ad9fa78832a.webp" alt="Bağlam Belleği" width="800"/>

*Konuşma bağlamının çoklu turlarda token sınırına kadar birikimi*

**Adım Adım Muhakeme** - Görünür mantık gerektiren problemler için. Model her adım için açık muhakeme gösterir. Matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istediğinizde kullanın.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Adım Adım Desen" width="800"/>

*Problemleri açık mantıksal adımlara ayırma*

**Kısıtlı Çıktı** - Belirli format gereksinimleri olan yanıtlar için. Model format ve uzunluk kurallarına sıkı sıkıya uyar. Özetler veya kesin çıktı yapısı istediğinizde tercih edilir.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Kısıtlı Çıktı Deseni" width="800"/>

*Belirli format, uzunluk ve yapı gereksinimlerini zorunlu kılma*

## Mevcut Azure Kaynaklarını Kullanmak

**Dağıtımı doğrulayın:**

Modül 01’de oluşturulan Azure kimlik bilgileri içeren `.env` dosyasının kök dizinde mevcut olduğundan emin olun:
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Modül 01'de `./start-all.sh` komutuyla tüm uygulamaları zaten başlattıysanız, bu modül 8083 portunda çalışıyor demektir. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8083 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code’un solundaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulunur.

Dashboard üzerinden:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıklamayla başlatabilir/durdurabilirsiniz
- Gerçek zamanlı uygulama loglarını izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

Bu modülü başlatmak için "prompt-engineering" yanındaki oynat düğmesine tıklayın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell komutları ile**

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Her iki betik de kök dizindeki `.env` dosyasından ortam değişkenlerini otomatik yükler ve JAR dosyaları yoksa derler.

> **Not:** Başlamadan önce tüm modülleri manuel derlemek isterseniz:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Tarayıcınızda http://localhost:8083 adresini açın.

**Durdurmak için:**

**Bash:**
```bash
./stop.sh  # Bu modül sadece
# Veya
cd .. && ./stop-all.sh  # Tüm modüller
```

**PowerShell:**
```powershell
.\stop.ps1  # Sadece bu modül
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```


## Uygulama Ekran Görüntüleri

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Ana Sayfa" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Tüm 8 prompt mühendisliği desenini, özelliklerini ve kullanım durumlarını gösteren ana kontrol paneli*

## Desenleri Keşfetmek

Web arayüzü farklı promptlama stratejileriyle denemeler yapmanızı sağlar. Her desen farklı sorunları çözer—hangisinin ne zaman parladığını görmek için deneyin.

### Düşük vs Yüksek İsteklilik

“200’ün %15’i nedir?” gibi basit bir soruyu Düşük İsteklilik ile sorun. Anında, doğrudan cevap alacaksınız. Şimdi “Yüksek trafikli bir API için önbellekleme stratejisi tasarlayın” gibi karmaşık bir soruyu Yüksek İsteklilik ile sorun. Modelin nasıl yavaşlayıp ayrıntılı muhakeme sunduğunu izleyin. Aynı model, aynı soru yapısı—ancak prompt ne kadar düşünmesi gerektiğini söylüyor.
<img src="../../../translated_images/tr/low-eagerness-demo.898894591fb23aa0.webp" alt="Düşük İsteklilik Örneği" width="800"/>

*Minimal muhakemeyle hızlı hesaplama*

<img src="../../../translated_images/tr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Yüksek İsteklilik Örneği" width="800"/>

*Kapsamlı önbellekleme stratejisi (2.8MB)*

### Görev Yürütme (Araç Girişleri)

Çok adımlı iş akışları önceden planlama ve ilerleme anlatımı ile fayda sağlar. Model ne yapacağını belirtir, her adımı açıklar, sonra sonuçları özetler.

<img src="../../../translated_images/tr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Görev Yürütme Örneği" width="800"/>

*Adım adım anlatımla REST uç noktası oluşturma (3.9MB)*

### Kendi Kendini Değerlendiren Kod

"Bir e-posta doğrulama servisi oluştur" deneyin. Sadece kod üretip bitirmek yerine, model ürettiği kodu kalite kriterlerine göre değerlendirir, zayıflıkları tespit eder ve iyileştirir. Kod, üretim standartlarına ulaşana kadar tekrarlamalar göreceksiniz.

<img src="../../../translated_images/tr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Kendi Kendini Değerlendiren Kod Örneği" width="800"/>

*Tamamlanmış e-posta doğrulama servisi (5.2MB)*

### Yapılandırılmış Analiz

Kod incelemeleri tutarlı değerlendirme çerçeveleri gerektirir. Model, kodu sabit kategorilerle (doğruluk, uygulamalar, performans, güvenlik) ve şiddet seviyeleri ile analiz eder.

<img src="../../../translated_images/tr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Yapılandırılmış Analiz Örneği" width="800"/>

*Çerçeve tabanlı kod incelemesi*

### Çok Turlu Sohbet

"Spring Boot nedir?" diye sorun, hemen ardından "Bir örnek göster" deyin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği verir. Bellek olmasaydı, ikinci soru çok genel olurdu.

<img src="../../../translated_images/tr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Çok Turlu Sohbet Örneği" width="800"/>

*Sorular arasında bağlam koruma*

### Adım Adım Muhakeme

Bir matematik problemi seçin ve bunu Hem Adım Adım Muhakeme hem de Düşük İsteklilik ile deneyin. Düşük istek sadece cevabı verir - hızlı ama şeffaf değil. Adım adım her hesaplama ve kararı gösterir.

<img src="../../../translated_images/tr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Adım Adım Muhakeme Örneği" width="800"/>

*Açık adımlarla matematik problemi*

### Kısıtlı Çıktı

Belirli formatlar veya kelime sayıları gerektiğinde, bu kalıp sıkı uyumu sağlar. Tam olarak 100 kelimelik bir madde işaretli özet üretmeyi deneyin.

<img src="../../../translated_images/tr/constrained-output-demo.567cc45b75da1633.webp" alt="Kısıtlı Çıktı Örneği" width="800"/>

*Format kontrolü ile makine öğrenimi özeti*

## Gerçekte Öğrendiğiniz

**Muhakeme Çabası Her Şeyi Değiştirir**

GPT-5.2, istemcileriniz aracılığıyla hesaplama çabasını kontrol etmenizi sağlar. Düşük çaba, minimal keşif ile hızlı yanıtlar demektir. Yüksek çaba, modelin derin düşünmek için zaman ayırmasıdır. Görev karmaşıklığına uygun çabayı öğreniyorsunuz - basit sorularda zaman kaybetmeyin, ama karmaşık kararları da hızlıca geçmeyin.

**Yapı Davranışı Yönlendirir**

İstemlerdeki XML etiketlerini fark ettiniz mi? Süs değil. Modeller yapısal talimatları serbest metinden daha güvenilir takip eder. Çok adımlı süreçler veya karmaşık mantık gerektiğinde, yapı modelin nerede olduğunu ve sıradakini izlemesine yardımcı olur.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="İstek Yapısı" width="800"/>

*Açık bölümler ve XML stili organizasyon içeren iyi yapılandırılmış bir isteğin anatomisi*

**Kalite Kendi Kendini Değerlendirerek Gelir**

Kendi kendini değerlendiren kalıplar, kalite kriterlerini açıkça belirterek çalışır. Modelin "doğru yapmasını umut etmek" yerine, "doğru"nun ne olduğunu açıkça söylersiniz: doğru mantık, hata yönetimi, performans, güvenlik. Model çıktısını sonra değerlendirip geliştirir. Bu, kod üretimini bir piyangodan sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok turlu konuşmalar, her isteğe mesaj geçmişini dahil ederek çalışır. Fakat bir sınır vardır — her modelin maksimum token sayısı vardır. Konuşmalar büyüdükçe, ilgili bağlamı koruyup sınıra ulaşmamak için stratejilere ihtiyacınız olacak. Bu modül belleğin nasıl çalıştığını gösteriyor; ileride ne zaman özetleneceğini, ne zaman unutulacağını ve ne zaman geri çağrılacağını öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Getirme Destekli Üretim)](../03-rag/README.md)

---

**Gezinme:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Geri](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, [Co-op Translator](https://github.com/Azure/co-op-translator) adlı yapay zeka çeviri hizmeti kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi ana dilindeki haliyle yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilmektedir. Bu çevirinin kullanımıyla ortaya çıkabilecek herhangi bir yanlış anlaşılma veya hata için sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->