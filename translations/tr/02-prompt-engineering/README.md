# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Video Yürütme](../../../02-prompt-engineering)
- [Öğrenecekleriniz](../../../02-prompt-engineering)
- [Önkoşullar](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Prompt Mühendisliği Temelleri](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Şablonları](../../../02-prompt-engineering)
- [İleri Düzey Kalıplar](../../../02-prompt-engineering)
- [Uygulamayı Çalıştır](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Kalıpları İncelemek](../../../02-prompt-engineering)
  - [Düşük ve Yüksek İsteklilik](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Ön Yazıları)](../../../02-prompt-engineering)
  - [Kendini Yansıtan Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Turlu Sohbet](../../../02-prompt-engineering)
  - [Adım Adım Mantık Yürütme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekten Ne Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Video Yürütme

Bu modüle nasıl başlayacağınızı açıklayan canlı oturumu izleyin:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4j ile Prompt Mühendisliği - Canlı Oturum" width="800"/></a>

## Öğrenecekleriniz

Aşağıdaki diyagram, bu modülde geliştireceğiniz temel konular ve beceriler hakkında genel bir bakış sunar — prompt iyileştirme tekniklerinden takip edeceğiniz adım adım iş akışına kadar.

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Öğrenecekleriniz" width="800"/>

Önceki modüllerde, GitHub Modelleri ile temel LangChain4j etkileşimlerini keşfettiniz ve bellek sayesinde Azure OpenAI ile nasıl konuşma tabanlı yapay zeka yapılacağını gördünüz. Şimdi ise, soruları nasıl sorduğunuza – yani promptlara – Azure OpenAI'nin GPT-5.2'sini kullanarak odaklanacağız. Promptlarınızı nasıl yapılandırdığınız, alacağınız yanıtların kalitesini radikal şekilde etkiler. Temel prompt tekniklerini gözden geçirerek başlıyoruz, ardından GPT-5.2’nin tüm yeteneklerinden tam anlamıyla yararlanan sekiz ileri kalıba geçiyoruz.

GPT-5.2'yi kullanmamızın nedeni akıl yürütme kontrolü sunmasıdır - modele yanıt vermeden önce ne kadar düşünmesi gerektiğini söyleyebilirsiniz. Bu, farklı prompt stratejilerini daha belirgin kılar ve hangi yaklaşımı ne zaman kullanmanız gerektiğini anlamanıza yardımcı olur. Ayrıca GPT-5.2 için Azure'daki daha az hız sınırlarından da faydalanacağız.

## Önkoşullar

- Modül 01'in tamamlanması (Azure OpenAI kaynaklarının dağıtılması)
- Kök dizinde Azure kimlik bilgileri ile `.env` dosyası (Modül 01’de `azd up` komutuyla oluşturuldu)

> **Not:** Eğer Modül 01’i tamamlamadıysanız, önce oradaki dağıtım talimatlarını takip edin.

## Prompt Mühendisliğini Anlamak

Özünde, prompt mühendisliği belirsiz talimatlar ile kesin talimatlar arasındaki farktır, aşağıdaki karşılaştırma bunu gösterir.

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Mühendisliği Nedir?" width="800"/>

Prompt mühendisliği, tutarlı şekilde istediğiniz sonuçları almanızı sağlayan giriş metinleri tasarlamaktır. Sadece soru sormak değil — modelin tam olarak ne istediğinizi ve nasıl sunması gerektiğini anlayacağı şekilde istekleri yapılandırmaktır.

Bunu bir meslektaşınıza talimat vermek gibi düşünün. "Hatanın düzeltilmesi" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer istisnasını null kontrolü ekleyerek düzelt" ise özeldir. Dil modelleri de aynı şekilde çalışır — spesifiklik ve yapı önemlidir.

Aşağıdaki diyagram LangChain4j'nin bu resme nasıl uyduğunu gösterir — prompt kalıplarınızı SystemMessage ve UserMessage yapı blokları aracılığıyla modele bağlar.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j'nin Uygunluğu" width="800"/>

LangChain4j altyapıyı sağlar — model bağlantıları, bellek ve mesaj türleri — prompt kalıpları ise bu altyapı üzerinden gönderdiğiniz dikkatlice yapılandırılmış metinlerdir. Ana yapı taşları `SystemMessage` (AI'nın davranışını ve rolünü belirler) ve `UserMessage`dir (gerçek isteğinizi taşır).

## Prompt Mühendisliği Temelleri

Aşağıda gösterilen beş temel teknik, etkili prompt mühendisliğinin temelini oluşturur. Her biri dil modelleriyle iletişimin farklı bir yönüne değinir.

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş Prompt Mühendisliği Kalıbı Genel Bakış" width="800"/>

Bu modüldeki ileri kalıplara girmeden önce, temel beş prompt tekniklerini gözden geçirelim. Bunlar her prompt mühendisi için bilinmesi gereken yapı taşlarıdır. Eğer [Hızlı Başlangıç modülünü](../00-quick-start/README.md#2-prompt-patterns) tamamladıysanız, bunları uygulamada görmüşsünüzdür — işte kavramsal çerçevesi.

### Zero-Shot Prompting

En basit yaklaşım: modele hiçbir örnek vermeden doğrudan talimat verin. Model, görevi anlamak ve yürütmek için tamamen eğitim bilgilerine dayanır. Beklenen davranış açık olduğu basit istekler için işe yarar.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Örnek olmadan doğrudan talimat — model yalnızca talimattan görevi çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Olumlu"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler veya modelin ek rehberlik olmadan yapabileceği herhangi bir görev.

### Few-Shot Prompting

Modelin takip etmesini istediğiniz kalıbı gösteren örnekler verin. Model, örneklerinizden beklenen giriş-çıkış formatını öğrenir ve yeni girdilere uygular. Bu, istenen formatın veya davranışın açık olmadığı görevlerde tutarlılığı dramatik şekilde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Örneklerden öğrenmek — model kalıbı tanır ve yeni girdilere uygular*

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

**Ne zaman kullanılır:** Özel sınıflandırmalar, tutarlı biçimlendirme, alan spesifik görevler veya zero-shot sonuçlar tutarsızsa.

### Chain of Thought

Modelden mantığını adım adım göstermesini isteyin. Doğrudan cevaba atlamaktan ziyade, model problemi parçalar ve her adımı açıkça işler. Bu, matematik, mantık ve çok adımlı akıl yürütmede doğruluğu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri Promptlama" width="800"/>

*Adım adım mantık yürütme — karmaşık problemleri açık mantıksal adımlara bölmek*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama veya mantık sürecini göstermek doğruluk ve güveni artıran görevlerde.

### Role-Based Prompting

Sorunuzu sormadan önce AI için bir rol veya persona belirleyin. Bu, yanıtın tonu, derinliği ve odağını şekillendiren bağlam sağlar. "Yazılım mimarı" farklı, "çaylak geliştirici" veya "güvenlik denetçisi" farklı tavsiyeler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Bazlı Promptlama" width="800"/>

*Bağlam ve persona belirleme — aynı soru, atanmış role göre farklı yanıt alır*

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

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan spesifik analizler veya yanıtların belirli uzmanlık seviyesine veya bakış açısına göre uyarlanması gereken durumlarda.

### Prompt Şablonları

Değişken yer tutucuları olan yeniden kullanılabilir promptlar oluşturun. Her seferinde yeni prompt yazmak yerine, bir defa şablon tanımlayın ve farklı değerlerle doldurun. LangChain4j `PromptTemplate` sınıfı, `{{variable}}` sözdizimi ile bunu kolaylaştırır.

<img src="../../../translated_images/tr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Şablonları" width="800"/>

*Değişken yer tutucuları ile tekrar kullanılabilir promptlar — bir şablon, birçok kullanım*

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

**Ne zaman kullanılır:** Farklı girişlerle tekrar eden sorgular, toplu işlem, yeniden kullanılabilir AI iş akışları oluşturma veya prompt yapısı aynı kalıp veri değişen senaryolarda.

---

Bu beş temel, çoğu promptlama görevinde sağlam bir araç seti sağlar. Bu modülün geri kalanı, GPT-5.2’nin akıl yürütme kontrolü, kendini değerlendirme ve yapılandırılmış çıktı özelliklerini kullanan **sekiz ileri kalıp** üzerine inşa edilir.

## İleri Düzey Kalıplar

Temelleri ele aldığımıza göre, bu modülü benzersiz kılan sekiz ileri kalıba geçelim. Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı yanıt ister, bazılarının derin bir düşünceye ihtiyacı vardır. Bazıları görünür mantık ister, bazıları sadece sonucu ister. Aşağıdaki her kalıp farklı bir senaryo için optimize edilmiştir — ve GPT-5.2’nin akıl yürütme kontrolü farkları daha da belirgin yapar.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz Prompt Kalıbı" width="800"/>

*Sekiz prompt mühendisliği kalıbının genel görünümü ve kullanım alanları*

GPT-5.2 bu kalıplara bir boyut daha ekler: *akıl yürütme kontrolü*. Aşağıdaki kaydırıcı, modelin düşünce çabasını nasıl ayarlayabileceğinizi gösterir — hızlı, doğrudan yanıtlar ile derin, kapsamlı analiz arasında.

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Akıl Yürütme Kontrolü" width="800"/>

*GPT-5.2’nin akıl yürütme kontrolü, modelin ne kadar düşünmesi gerektiğini belirtmenizi sağlar — hızlı doğrudan yanıtlardan derin incelemeye kadar*

**Düşük İsteklilik (Hızlı & Odaklı)** - Hızlı, doğrudan yanıtlar istediğiniz basit sorular için. Model minimal akıl yürütme yapar - en fazla 2 adım. Hesaplamalar, sorgulamalar veya basit sorular için kullanın.

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

> 💡 **GitHub Copilot ile keşfedin:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dosyasını açın ve sorun:
> - "Düşük istekli ve yüksek istekli prompt kalıpları arasındaki fark nedir?"
> - "Promptlardaki XML etiketleri AI yanıtını nasıl yapılandırmaya yardımcı olur?"
> - "Kendini yansıtma kalıplarını ne zaman doğrudan talimatlara tercih etmeliyim?"

**Yüksek İsteklilik (Derin & Kapsamlı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model detaylı bir şekilde keşfeder ve ayrıntılı mantık yürütür. Sistem tasarımı, mimari kararlar veya karmaşık araştırmalar için kullanın.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım-Adım İlerleme)** - Çok adımlı iş akışları için. Model önceden bir plan sunar, çalışırken her adımı anlatır ve ardından özet verir. Göçler, uygulamalar veya çok adımlı süreçler için kullanın.

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

Düşünce Zinciri promptlama, modelden mantık sürecini açıkça göstermesini ister, karmaşık görevlerde doğruluğu artırır. Adım adım ayrıntı, hem insan hem de AI için mantığı anlamaya yardımcı olur.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Bu kalıp hakkında sorun:
> - "Uzun süren işlemler için görev yürütme kalıbını nasıl uyarlardım?"
> - "Üretim uygulamalarında araç ön yazıları nasıl yapılandırılır?"
> - "Ara ilerleme güncellemelerini UI’da nasıl yakalar ve gösteririm?"

Aşağıdaki diyagram bu Planla → Yürüt → Özetle iş akışını gösterir.

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Kalıbı" width="800"/>

*Çok adımlı görevler için Planla → Yürüt → Özetle iş akışı*

**Kendini Yansıtan Kod** - Üretim kalitesinde kod üretmek için. Model, uygun hata yönetimi ile üretim standartlarına uygun kodlar üretir. Yeni özellikler veya servisler geliştirirken kullanın.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

Aşağıdaki diyagram bu yinelemeli geliştirme döngüsünü gösterir — üret, değerlendir, zayıf noktaları belirle ve kod üretim standartlarına ulaşana kadar iyileştir.

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kendini Yansıtma Döngüsü" width="800"/>

*Yinelemeli iyileştirme döngüsü - üret, değerlendir, sorunları tanımla, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model, kodu sabit bir çerçeve kullanarak inceler (doğruluk, uygulamalar, performans, güvenlik, sürdürülebilirlik). Kod incelemeleri veya kalite değerlendirmeleri için kullanın.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Yapılandırılmış analiz hakkında sorun:
> - "Farklı kod inceleme türlerine göre analiz çerçevesi nasıl özelleştirilir?"
> - "Yapılandırılmış çıktıları programatik olarak ayrıştırıp aksiyon almak için en iyi yöntem nedir?"
> - "Farklı inceleme oturumlarında tutarlı şiddet düzeyleri nasıl sağlanır?"

Aşağıdaki diyagram bu yapılandırılmış çerçevenin bir kod incelemesini tutarlı kategorilere ve şiddet seviyelerine nasıl organize ettiğini gösterir.

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Kalıbı" width="800"/>

*Tutarlı kod incelemeleri için şiddet seviyeleri ile çerçeve*

**Çok Turlu Sohbet** - Bağlam gerektiren konuşmalar için. Model önceki mesajları hatırlar ve üzerine inşa eder. Etkileşimli yardım oturumları veya karmaşık SSS için kullanın.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

Aşağıdaki diyagram, konuşma bağlamının her turda nasıl biriktiğini ve modelin token sınırıyla nasıl ilişkili olduğunu görselleştirir.

<img src="../../../translated_images/tr/context-memory.dff30ad9fa78832a.webp" alt="Bağlam Belleği" width="800"/>

*Konuşma bağlamının çoklu turlarda token sınırına ulaşana kadar birikimi*
**Adım Adım Muhakeme** - Görünür mantık gerektiren problemler için. Model her adım için açık muhakeme gösterir. Bunu matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istediğinizde kullanın.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

Aşağıdaki diyagram, modelin problemleri nasıl açık, numaralandırılmış mantıksal adımlara böldüğünü gösterir.

<img src="../../../translated_images/tr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Adım Adım Desen" width="800"/>

*Problemleri açık mantıksal adımlara bölme*

**Kısıtlı Çıktı** - Belirli biçim gereksinimleri içeren yanıtlar için. Model format ve uzunluk kurallarına sıkı sıkıya uyar. Bunu özetler için veya kesin çıktı yapısına ihtiyacınız olduğunda kullanın.

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

Aşağıdaki diyagram, kısıtlamaların modelin çıktıyı tam olarak istenen format ve uzunluk kurallarına uygun üretmesini nasıl sağladığını gösterir.

<img src="../../../translated_images/tr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Kısıtlı Çıktı Deseni" width="800"/>

*Belirli format, uzunluk ve yapı gereksinimlerini uygulama*

## Uygulamayı Çalıştırma

**Dağıtımı doğrulayın:**

Azure kimlik bilgileri ile root dizinde `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu). Bunu modül dizininden çalıştırın (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermelidir
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Eğer root dizinden `./start-all.sh` kullanarak tüm uygulamaları zaten başlattıysanız (Modül 01'de anlatıldığı gibi), bu modül zaten 8083 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8083 adresine geçebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirici konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlayan Spring Boot Dashboard uzantısını içerir. Bunu VS Code'un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesine bakın) bulabilirsiniz.

Spring Boot Dashboard’dan şunları yapabilirsiniz:
- Çalışma alanındaki tüm mevcut Spring Boot uygulamalarını görmek
- Uygulamaları tek tıklamayla başlatmak/durdurmak
- Uygulama günlüklerini gerçek zamanlı izlemek
- Uygulama durumunu takip etmek

"prompt-engineering" yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri aynı anda başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code’daki Spring Boot Dashboard — tüm modülleri tek yerden başlatın, durdurun ve izleyin*

**Seçenek 2: Shell script kullanımı**

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Her iki script de root `.env` dosyasından ortam değişkenlerini otomatik olarak yükler ve JAR dosyaları yoksa oluşturur.

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

Tarayıcınızda http://localhost:8083 adresini açın.

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

## Uygulama Ekran Görüntüleri

İşte prompt engineering modülünün ana arayüzü; burada sekiz desenin tümü yan yana deneyebilirsiniz.

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Ana Sayfa" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Sekiz prompt engineering deseninin özellikleri ve kullanım durumları ile gösterildiği ana panel*

## Desenleri Keşfetmek

Web arayüzü farklı prompt stratejilerini denemenizi sağlar. Her desen farklı problemleri çözer - ne zaman hangi yaklaşımın iyi olduğunu görmek için deneyin.

> **Not: Akışlı (Streaming) vs Akışsız (Non-Streaming)** — Her desen sayfasında iki buton bulunur: **🔴 Akışlı Yanıt (Canlı)** ve bir **Akışsız** seçenek. Akışlıda, Server-Sent Events (SSE) kullanılır; model oluştukça tokenlar gerçek zamanlı gösterilir, böylece ilerlemeyi hemen görürsünüz. Akışsız seçenek ise tüm yanıt gelene kadar bekler. Derin muhakeme gerektiren komutlar için (ör. Yüksek Hırs, Öz-Değerlendiren Kod) akışsız çağrı uzun sürebilir - bazen dakikalarca - ve görünür geri bildirim yoktur. **Karmaşık promptlarla deneme yaparken akışlıyı kullanın** ki modelin çalıştığını görün ve isteğin zaman aşımına uğradığı yanılgısına kapılmayın.
>
> **Not: Tarayıcı Gereksinimi** — Akış özelliği Fetch Streams API (`response.body.getReader()`) kullanır ve tam bir tarayıcı (Chrome, Edge, Firefox, Safari) gerekir. VS Code’un gömülü Simple Browser’ında çalışmaz; çünkü webview ReadableStream API desteklemez. Simple Browser kullanıyorsanız, akışsız butonlar normal çalışır, sadece akışlı butonlar etkilenir. Tam deneyim için `http://localhost:8083` adresini harici bir tarayıcıda açın.

### Düşük vs Yüksek Hırs

"200'ün %15'i nedir?" gibi basit bir soruyu Düşük Hırs ile sorun. Anında, doğrudan yanıt alırsınız. Şimdi "Yüksek trafikli bir API için önbellekleme stratejisi tasarla" gibi karmaşık bir şey sorun. **🔴 Akışlı Yanıt (Canlı)** düğmesine tıklayın ve modelin ayrıntılı muhakemesinin token token belirdiğini izleyin. Aynı model, aynı soru yapısı - ama prompt ona ne kadar düşünmesi gerektiğini söylüyor.

### Görev Yürütme (Araç Ön Bilgileri)

Çok adımlı iş akışları, önceden planlama ve ilerlemenin anlatımıyla fayda sağlar. Model ne yapacağını özetler, her adımı anlatır, sonra sonuçları özetler.

### Öz-Değerlendiren Kod

"Bir e-posta doğrulama servisi oluştur" deyin. Sadece kod üretmekle kalmaz, kalite kriterlerine göre değerlendirir, zayıf noktaları belirler ve geliştirir. Kod üretim standartlarına ulaşana kadar iterasyon yaptığını görürsünüz.

### Yapılandırılmış Analiz

Kod incelemeleri tutarlı değerlendirme çerçeveleri ister. Model kodu sabit kategoriler (doğruluk, uygulama, performans, güvenlik) ve şiddet seviyeleri ile analiz eder.

### Çok Turlu Sohbet

"Spring Boot nedir?" diye sorun, hemen ardından "Bir örnek göster" deyin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği sunar. Bellek olmasaydı, ikinci soru çok belirsiz olurdu.

### Adım Adım Muhakeme

Bir matematik problemi seçin ve hem Adım Adım Muhakeme hem de Düşük Hırs ile deneyin. Düşük hırs sadece cevabı verir - hızlı ama opak. Adım adım her hesaplama ve kararı gösterir.

### Kısıtlı Çıktı

Belirli format veya kelime sayısına ihtiyaç duyduğunuzda, bu desen katı uyumluluğu zorlar. Tam 100 kelimelik madde işaretli bir özet oluşturarak deneyin.

## Gerçekten Ne Öğreniyorsunuz

**Muhakeme Çabası Her Şeyi Değiştirir**

GPT-5.2, promptlarınız aracılığıyla hesaplama çabasını kontrol etmenizi sağlar. Düşük çaba, minimal keşif ile hızlı yanıt verir. Yüksek çaba, modelin derin düşünmesi anlamına gelir. Görev karmaşıklığına göre çabayı ayarlamayı öğreniyorsunuz - basit sorulara zaman harcamayın, ama karmaşık kararlarda acele etmeyin.

**Yapı Davranışı Yönlendirir**

Promptlardaki XML etiketlerine dikkat ettiniz mi? Sadece süs değil. Modeller, serbest metinden çok yapılandırılmış talimatları daha güvenilir takip eder. Çok adımlı süreçler veya karmaşık mantık gerektiğinde, yapı modelin nerede olduğunu ve sıradakini izlemesine yardımcı olur. Aşağıdaki diyagram, iyi yapılandırılmış bir promptu parçalarına ayırır; `<system>`, `<instructions>`, `<context>`, `<user-input>`, ve `<constraints>` gibi etiketlerin talimatlarınızı nasıl net bölümlere organize ettiğini gösterir.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Yapısı" width="800"/>

*Açık bölümler ve XML tarzı organizasyona sahip iyi yapılandırılmış bir promptun anatomisi*

**Kendi Kendini Değerlendirme ile Kalite**

Öz-değerlendiren desenler, kalite kriterlerini açıkça belirterek çalışır. Modelin "doğru yapmasını ummak" yerine, tam olarak "doğru"nun ne demek olduğunu söylersiniz: doğru mantık, hata yönetimi, performans, güvenlik. Model kendi çıktısını değerlendirebilir ve iyileştirebilir. Bu, kod üretimini bir piyangodan sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok turlu sohbetler, her istekte mesaj geçmişinin dahil edilmesiyle çalışır. Ancak bir sınır vardır - her modelin maksimum token sayısı vardır. Sohbetler büyüdükçe, ilgili bağlamı korurken bu limite ulaşmamak için stratejiler gerekir. Bu modül, belleğin nasıl çalıştığını gösterir; ileride ne zaman özetleneceğinizi, ne zaman unutacağınızı ve ne zaman geri çağıracağınızı öğrenirsiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Gezinme:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi ana dilinde resmi kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu oluşabilecek yanlış anlamalar veya yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->