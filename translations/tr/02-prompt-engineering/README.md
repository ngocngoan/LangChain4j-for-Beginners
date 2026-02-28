# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Video Yürütme](../../../02-prompt-engineering)
- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Ön Koşullar](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Prompt Mühendisliğinin Temelleri](../../../02-prompt-engineering)
  - [Sıfır-Örnek İstek](../../../02-prompt-engineering)
  - [Az-Örnek İstek](../../../02-prompt-engineering)
  - [Düşünce Zinciri](../../../02-prompt-engineering)
  - [Rol Bazlı İstek](../../../02-prompt-engineering)
  - [İstek Şablonları](../../../02-prompt-engineering)
- [Gelişmiş Desenler](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarının Kullanımı](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Desenleri Keşfetmek](../../../02-prompt-engineering)
  - [Düşük ve Yüksek İsteklilik](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Girişleri)](../../../02-prompt-engineering)
  - [Kendi Kendini Yansıtan Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Tur Sohbet](../../../02-prompt-engineering)
  - [Adım Adım Muhakeme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekten Ne Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Video Yürütme

Bu modülün başlangıcını anlatan canlı oturumu izleyin: [LangChain4j ile Prompt Mühendisliği - Canlı Oturum](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## Neler Öğreneceksiniz

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Neler Öğreneceksiniz" width="800"/>

Önceki modülde, hafızanın konuşma tabanlı yapay zekayı nasıl etkinleştirdiğini gördünüz ve temel etkileşimler için GitHub Modellerini kullandınız. Şimdi soruları nasıl sorduğunuza — yani prompt’lara — odaklanacağız; Azure OpenAI'nın GPT-5.2’sini kullanarak promptların kendisini yapılandırmayı inceleyeceğiz. Promptlarınızı nasıl yapılandırdığınız, aldığınız yanıtların kalitesini önemli ölçüde etkiler. Öncelikle temel prompt tekniklerini gözden geçiriyoruz, sonra GPT-5.2’nin olanaklarını tam anlamıyla kullanan sekiz gelişmiş desene geçiyoruz.

GPT-5.2’yi kullanacağız çünkü muhakeme kontrolünü getiriyor - modelin cevap vermeden önce ne kadar düşünmesi gerektiğini belirtebiliyorsunuz. Bu, farklı prompt stratejilerini daha belirgin kılıyor ve hangi yöntemin ne zaman kullanılacağını anlamanızı sağlıyor. Ayrıca, GPT-5.2 için Azure’un GitHub Modellerine kıyasla daha az hız sınırlaması avantajından yararlanıyoruz.

## Ön Koşullar

- Modül 01’in tamamlanmış olması (Azure OpenAI kaynakları dağıtıldı)
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01’de `azd up` komutu ile oluşturuldu)

> **Not:** Modül 01’i tamamlamadıysanız önce oradaki dağıtım talimatlarını izleyin.

## Prompt Mühendisliğini Anlamak

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Mühendisliği Nedir?" width="800"/>

Prompt mühendisliği, ihtiyacınız olan sonuçları tutarlı şekilde almanızı sağlayan giriş metnini tasarlamakla ilgilidir. Sadece soru sormak değil — istekleri o kadar yapılandırmak ki model tam olarak ne istediğinizi ve nasıl sunacağını anlasın.

Bir meslektaşınıza talimat vermek gibi düşünün. "Hatayı düzelt" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer hatasını null kontrolü ekleyerek düzelt" ise spesifiktir. Dil modelleri de aynı şekilde çalışır — açıklık ve yapı önemlidir.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Nasıl Uyuyor" width="800"/>

LangChain4j, altyapıyı — model bağlantıları, hafıza ve mesaj türleri — sağlar; prompt desenleri ise bu altyapı üzerinden gönderdiğiniz dikkatlice yapılandırılmış metindir. Ana yapı taşları `SystemMessage` (Yapay zekanın davranışı ve rolünü belirler) ve `UserMessage` (gerçek isteğinizi taşır) bileşenleridir.

## Prompt Mühendisliğinin Temelleri

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş Prompt Mühendisliği Deseninin Genel Görünümü" width="800"/>

Bu modüldeki gelişmiş desenlere dalmadan önce beş temel prompt tekniğini gözden geçirelim. Bunlar her prompt mühendisi için bilinmesi gereken yapı taşlarıdır. Eğer [Hızlı Başlangıç modülünü](../00-quick-start/README.md#2-prompt-patterns) deneyimlediyseniz, bunları uygulamada görmüşsünüzdür — işte bunların arkasındaki kavramsal çerçeve.

### Sıfır-Örnek İstek

En basit yaklaşım: modele örneksiz doğrudan talimat verilir. Model, görevi anlamak ve yerine getirmek için tamamen eğitimine dayanır. Beklenen davranışın açık olduğu basit istekler için iyidir.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Sıfır-Örnek İstek" width="800"/>

*Örnek olmadan doğrudan talimat — model görevi sadece talimattan çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Pozitif"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler veya ek kılavuz olmadan modelin halledebileceği tüm görevler.

### Az-Örnek İstek

Modelin izlemesini istediğiniz deseni gösteren örnekler verilir. Model örneklerden giriş-çıkış formatını öğrenir ve yeni girdilere uygular. Bu, istenen format veya davranışın açık olmadığı görevlerde tutarlılığı önemli ölçüde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Az-Örnek İstek" width="800"/>

*Örneklerden öğrenme — model paterni tanır ve yeni girdilere uygular*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Ne zaman kullanılır:** Özel sınıflandırmalar, tutarlı biçimlendirme, alan-spesifik görevler veya sıfır-örnek sonuçların tutarsız olduğu durumlar.

### Düşünce Zinciri

Modelin muhakemesini adım adım göstermesini ister. Cevaba doğrudan atlamak yerine, model problemi açıkça parçalara ayırır ve her adımı açıklar. Matematik, mantık ve çok adımlı muhakeme görevlerinde doğruluğu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri İsteği" width="800"/>

*Adım adım muhakeme — karmaşık problemleri açıkça mantıksal adımlara bölmek*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama veya muhakeme süreçlerinin güvenilirliği artırdığı tüm görevlerde.

### Rol Bazlı İstek

Sorunuzu sormadan önce Yapay zekaya bir rol veya kişilik belirleyin. Bu, yanıtın tonu, derinliği ve odak noktasını şekillendirir. Bir "yazılım mimarı" farklı, bir "genç geliştirici" veya "güvenlik denetçisi" farklı öneriler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Bazlı İstek" width="800"/>

*Bağlam ve kişilik belirleme — atanan role göre aynı soru farklı yanıtlar alır*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan-spesifik analiz veya belirli uzmanlık seviyesine ya da bakış açısına göre yanıtlar gerektiğinde.

### İstek Şablonları

Değişken yer tutucularıyla yeniden kullanılabilir promptlar oluşturun. Her seferinde yeni prompt yazmak yerine, bir şablon tanımlayıp farklı değerler girin. LangChain4j'nin `PromptTemplate` sınıfı `{{variable}}` sözdizimi ile bunu kolaylaştırır.

<img src="../../../translated_images/tr/prompt-templates.14bfc37d45f1a933.webp" alt="İstek Şablonları" width="800"/>

*Değişken yer tutuculu yeniden kullanılabilir promptlar — bir şablon, birçok kullanım*

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

**Ne zaman kullanılır:** Farklı girdilerle tekrarlanan sorgular, toplu işlem, yeniden kullanılabilir yapay zeka iş akışları oluşturmak veya yapısı aynı kalan ancak verisi değişen senaryolar.

---

Bu temel beş teknik çoğu prompt görevinde sağlam bir araç seti sunar. Bu modülün geri kalanı GPT-5.2’nin muhakeme kontrolü, kendi kendini değerlendirme ve yapılandırılmış çıktı özelliklerini kullanan **sekiz gelişmiş desen** üzerine inşa edilir.

## Gelişmiş Desenler

Temelleri kavradıktan sonra, bu modülü benzersiz kılan sekiz gelişmiş desene geçelim. Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı cevap ister, bazıları derin düşünme. Bazıları görünür muhakeme ister, bazıları sadece sonucu. Aşağıdaki her desen farklı senaryolar için optimize edilmiştir — ve GPT-5.2’nin muhakeme kontrolü farkları çok daha belirgin kılar.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz İstek Mühendisliği Deseni" width="800"/>

*Sekiz prompt mühendisliği deseni ve kullanım durumlarına genel bakış*

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Muhakeme Kontrolü" width="800"/>

*GPT-5.2'nin muhakeme kontrolü, modelin ne kadar düşünmesi gerektiğini belirtmenizi sağlar - hızlı, doğrudan cevaplardan derin keşfe kadar*

**Düşük İsteklilik (Hızlı ve Odaklı)** - Basit sorular için hızlı, doğrudan cevaplar istersiniz. Model minimal muhakeme yapar - maksimum 2 adım. Hesaplamalar, sorgular veya basit sorular için kullanın.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot ile Keşfedin:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dosyasını açın ve sorun:
> - "Düşük isteklilik ve yüksek isteklilik prompt desenleri arasındaki fark nedir?"
> - "Promptlarda XML etiketleri AI yanıtını nasıl yapılandırmaya yardım eder?"
> - "Kendini yansıtma desenlerini ne zaman doğrudan talimatlarla kullanmalıyım?"

**Yüksek İsteklilik (Derin ve Kapsamlı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model detaylı inceler, kapsamlı muhakeme gösterir. Sistem tasarımı, mimari kararlar veya karmaşık araştırmada kullanın.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım Adım İlerleme)** - Çok adımlı iş akışları için. Model önceden plan sunar, çalışırken her adımı anlatır, ardından özet verir. Geçişler, uygulamalar ya da çok adımlı süreçlerde tercih edilir.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Düşünce Zinciri istemi, modelin muhakeme sürecini göstermek için açıkça talimat verir, karmaşık görevlerde doğruluğu artırır. Adım adım parçalama hem insanlar hem AI için mantığı anlamayı kolaylaştırır.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** Bu desen hakkında sorun:
> - "Uzun süren işlemler için görev yürütme desenini nasıl adapte ederim?"
> - "Üretim uygulamalarında araç girişlerini yapılandırmanın en iyi uygulamaları nelerdir?"
> - "Bir UI’da ara ilerleme güncellemelerini nasıl yakalayıp gösterebilirim?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Deseni" width="800"/>

*Planla → Yürüt → Özetle iş akışı çok adımlı görevler için*

**Kendi Kendini Yansıtan Kod** - Üretim kalitesinde kod üretmek için. Model, üretim standartlarına uygun hata yönetimi ile kod oluşturur. Yeni özellikler ya da servisler kurarken kullanılır.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kendi Kendini Yansıtma Döngüsü" width="800"/>

*Yinelemeli iyileştirme döngüsü - üret, değerlendir, sorunları belirle, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model kodu sabit bir çerçevede inceler (doğruluk, uygulamalar, performans, güvenlik, sürdürülebilirlik). Kod incelemeleri veya kalite değerlendirmelerinde kullanılır.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** Yapılandırılmış analiz hakkında sorun:
> - "Farklı kod inceleme türleri için analiz çerçevesini nasıl özelleştiririm?"
> - "Yapılandırılmış çıktıyı programatik olarak ayrıştırıp kullanmanın en iyi yolu nedir?"
> - "Farklı inceleme oturumlarında tutarlı şiddet seviyeleri nasıl sağlanır?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Deseni" width="800"/>

*Tutarlı kod incelemeleri için şiddet seviyeleri ile çerçeve*

**Çok Tur Sohbet** - Bağlam gerektiren konuşmalar için. Model önceki mesajları hatırlar ve bunların üzerine inşa eder. Etkileşimli yardım oturumları veya karmaşık S.S.S. için idealdir.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/tr/context-memory.dff30ad9fa78832a.webp" alt="Bağlam Hafızası" width="800"/>

*Konuşma bağlamı çoklu turlar boyunca token sınırına ulaşana kadar birikir*

**Adım Adım Muhakeme** - Görünür mantık gerektiren problemlerde. Model her adım için açık muhakeme gösterir. Matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istediğinizde kullanılır.

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

*Karmaşık problemleri açık mantıksal adımlara bölmek*

**Kısıtlı Çıktı** - Belirli format gereklilikleri olan yanıtlar için. Model format ve uzunluk kurallarına sıkı sıkıya uyar. Özetler veya kesin çıktı yapısı gereken durumlarda kullanılır.

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

*Belirli format, uzunluk ve yapı gerekliliklerini zorunlu kılma*

## Mevcut Azure Kaynaklarının Kullanımı

**Dağıtımı doğrulayın:**

Kök dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Modül 01’den `./start-all.sh` ile tüm uygulamaları zaten başlattıysanız bu modül 8083 portunda çalışmaktadır. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8083 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Panelini Kullanma (VS Code kullanıcıları için önerilir)**
Geliştirici konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code’un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesine bakın) bulabilirsiniz.

Spring Boot Dashboard'dan şunları yapabilirsiniz:
- Çalışma alanındaki tüm mevcut Spring Boot uygulamalarını görme
- Uygulamaları tek tıklamayla başlatma/durdurma
- Uygulama günlüklerini gerçek zamanlı görüntüleme
- Uygulama durumunu izleme

Bu modülü başlatmak için "prompt-engineering" yanındaki oynat düğmesine tıklayın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell betikleri kullanma**

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

Her iki betik de otomatik olarak kök `.env` dosyasından ortam değişkenlerini yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlatmadan önce tüm modülleri manuel olarak oluşturmayı tercih ederseniz:
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

Tarayıcınızda http://localhost:8083 adresini açın.

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

## Uygulama Ekran Görüntüleri

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Tüm 8 prompt mühendisliği desenini, özelliklerini ve kullanım durumlarını gösteren ana kontrol paneli*

## Desenleri Keşfetmek

Web arayüzü, farklı prompt verme stratejileriyle deneme yapmanızı sağlar. Her desen farklı sorunları çözer - hangi yaklaşımın ne zaman parladığını görmek için deneyin.

> **Not: Akışlı vs Akışsız** — Her desen sayfası iki düğme sunar: **🔴 Canlı Yanıt Akışı** (Stream Response (Live)) ve **Akışsız** (Non-streaming) seçeneği. Akışlı, model tarafından üretilen tokenları gerçek zamanlı göstermek için Server-Sent Events (SSE) kullanır, böylece ilerlemeyi anında görürsünüz. Akışsız seçenek ise yanıtın tamamını bekler ve sonra gösterir. Derin düşünme gerektiren promptlar (örneğin, High Eagerness, Self-Reflecting Code) için akışsız çağrı çok uzun sürebilir - bazen dakikalarca - ve görünür bir geri bildirim olmaz. **Karmaşık promptları denerken akışlı kullanın** ki modelin çalıştığını görebilin ve isteğin zaman aşımına uğradığı izleniminden kaçının.
>
> **Not: Tarayıcı Gereksinimi** — Akış özelliği Fetch Streams API'sini (`response.body.getReader()`) kullanır ve tam bir tarayıcı (Chrome, Edge, Firefox, Safari) gerekir. VS Code’un yerleşik Simple Browser'ında çalışmaz, çünkü onun webview’u ReadableStream API’sini desteklemez. Simple Browser kullanırsanız, akışsız düğmeler normal şekilde çalışır — yalnızca akış düğmeleri etkilenir. Tam deneyim için `http://localhost:8083` adresini harici bir tarayıcıda açın.

### Düşük vs Yüksek Heves

"Düşük Heves" kullanarak "200'ün %15'i nedir?" gibi basit bir soru sorun. Anında, doğrudan yanıt alırsınız. Şimdi "Yüksek Trafikli API için önbellekleme stratejisi tasarla" gibi karmaşık bir şey sorun, Yüksek Heves kullanarak. **🔴 Canlı Yanıt Akışı** düğmesine tıklayın ve modelin detaylı akıl yürütmesini token token izleyin. Aynı model, aynı soru yapısı - ama prompt ona ne kadar düşünmesi gerektiğini söyler.

### Görev Yürütme (Araç Girişleri)

Çok adımlı iş akışları, önceden planlama ve ilerleme anlatımı ile fayda sağlar. Model ne yapacağını özetler, her adımı anlatır ve sonra sonuçları özetler.

### Kendini Yansıtan Kod

"Bir e-posta doğrulama servisi oluştur" deneyin. Model sadece kod üretmekle kalmaz, kalite kriterlerine göre değerlendirir, zayıf yönleri belirler ve geliştirir. Kod üretim standartlarına ulaşana kadar yinelemeleri görürsünüz.

### Yapısal Analiz

Kod incelemeleri tutarlı değerlendirme çerçeveleri gerektirir. Model, kodu doğruluk, uygulama şekilleri, performans, güvenlik gibi sabit kategoriler altında ve şiddet düzeyleriyle analiz eder.

### Çok Turlu Sohbet

"Spring Boot nedir?" diye sorun, sonra hemen "Bana bir örnek göster" deyin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği verir. Bellek yoksa bu ikinci soru çok belirsiz olurdu.

### Adım Adım Akıl Yürütme

Bir matematik problemi seçin ve hem Adım Adım Akıl Yürütme hem de Düşük Heves ile deneyin. Düşük heves sadece cevabı hızlı verir, ama kapalıdır. Adım adım hesaplamaları ve kararları size gösterir.

### Kısıtlanmış Çıktı

Belirli formatlarda veya kelime sayısında çıktı isterseniz, bu desen sıkı uyumu zorunlu kılar. Tam olarak 100 kelimelik madde işaretleriyle bir özet oluşturmayı deneyin.

## Gerçekten Ne Öğreniyorsunuz

**Akıl Yürütme Çabası Her Şeyi Değiştirir**

GPT-5.2, promptlarınız aracılığıyla hesaplama çabasını kontrol etmenizi sağlar. Düşük çaba, minimal keşifle hızlı yanıtlar demektir. Yüksek çaba, modelin derin düşünmek için zaman harcamasıdır. Görev karmaşıklığına uygun çabayı eşlemeyi öğreniyorsunuz - basit sorular için zaman kaybetmeyin ama karmaşık kararlarda acele etmeyin.

**Yapı Davranışı Yönlendirir**

Promptlardaki XML etiketlerine dikkat ettiniz mi? Süs değil. Modeller, serbest metinden daha güvenilir şekilde yapılandırılmış talimatları izler. Çok adımlı süreçler veya karmaşık mantık gerektiğinde, yapı modelin nerede olduğunu ve sıradaki adımı takip etmesine yardım eder.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Açık bölümler ve XML tarzı düzeni olan iyi yapılandırılmış bir prompt anatomisi*

**Kalite Kendi Kendini Değerlendirme İle Gelir**

Kendini yansıtan desenler, kalite kriterlerini açık hale getirir. Modelin "doğru yapmasını" umut etmek yerine, ona "doğru"nun ne olduğunu tam olarak söylersiniz: doğru mantık, hata işleme, performans, güvenlik. Sonra model kendi çıktısını değerlendirebilir ve geliştirebilir. Bu, kod üretimini bir şans oyunundan bir sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok turlu sohbetler, her istekte mesaj geçmişini içerir. Ama bir sınır vardır - her modelin maksimum token sayısı vardır. Sohbetler büyüdükçe, ilgili bağlamı korumak ama bu sınırı aşmamak için stratejiler gerekir. Bu modül size belleğin nasıl çalıştığını gösterir; sonra ne zaman özetlemeniz, ne zaman unutmanız ve ne zaman geri getirme yapmanız gerektiğini öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Gezinme:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluğu sağlamaya çalışsak da, otomatik çeviriler hata veya yanlışlık içerebilir. Orijinal belgenin kendi dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımıyla ortaya çıkabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->