# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Ön Koşullar](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Prompt Mühendisliği Temelleri](../../../02-prompt-engineering)
  - [Sıfır-Atış Promptlama](../../../02-prompt-engineering)
  - [Az-Atış Promptlama](../../../02-prompt-engineering)
  - [Düşünce Zinciri](../../../02-prompt-engineering)
  - [Rol Temelli Promptlama](../../../02-prompt-engineering)
  - [Prompt Şablonları](../../../02-prompt-engineering)
- [İleri Düzey Desenler](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarının Kullanımı](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Desenleri Keşfetmek](../../../02-prompt-engineering)
  - [Düşük vs Yüksek Heves](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Ön Açıklamaları)](../../../02-prompt-engineering)
  - [Özünü Yansıtıcı Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Turlu Sohbet](../../../02-prompt-engineering)
  - [Adım Adım Mantıklı Düşünme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekte Neler Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Neler Öğreneceksiniz

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Neler Öğreneceksiniz" width="800"/>

Önceki modülde, hafızanın sohbet bazlı yapay zekayı nasıl mümkün kıldığına ve temel etkileşimler için GitHub Modellerini nasıl kullandığınıza baktınız. Şimdi soruları nasıl sorduğunuza — yani promptlara — Azure OpenAI'nin GPT-5.2'sini kullanarak odaklanacağız. Promptları nasıl yapılandırdığınız, aldığınız yanıtların kalitesini önemli ölçüde etkiler. Temel prompt tekniklerini gözden geçirip, ardından GPT-5.2'nin yeteneklerinden tam olarak yararlanan sekiz ileri düzey desene geçeceğiz.

GPT-5.2'yi kullanacağız çünkü bu sürüm akıl yürütme kontrolü getiriyor — modele cevap vermeden önce ne kadar düşünmesi gerektiğini söyleyebilirsiniz. Bu, farklı promptlama stratejilerini daha belirgin hale getirir ve her yaklaşımı ne zaman kullanacağınızı anlamanıza yardımcı olur. Ayrıca GPT-5.2 için Azure'un GitHub Modellerine göre daha az oran sınırı avantajından faydalanacağız.

## Ön Koşullar

- Modül 01 tamamlanmış (Azure OpenAI kaynakları dağıtıldı)
- Ana dizinde `.env` dosyası, Azure kimlik bilgilerini içerir (Modül 01'de `azd up` komutu tarafından oluşturulmuş)

> **Not:** Eğer Modül 01'i tamamlamadıysanız, önce oradaki dağıtım talimatlarını takip edin.

## Prompt Mühendisliğini Anlamak

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Mühendisliği Nedir?" width="800"/>

Prompt mühendisliği, ihtiyacınız olan sonuçları tutarlı şekilde almanızı sağlayan girdi metinlerini tasarlamakla ilgilidir. Sadece soru sormak değildir — modelin tam olarak ne istediğinizi ve nasıl sunacağını anlaması için talepleri yapılandırmaktır.

Bunu bir iş arkadaşına talimat vermek gibi düşünün. "Hattı düzelt" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer istisnasını null kontrolü ekleyerek düzelt" ise spesifiktir. Dil modelleri de aynı şekilde çalışır — özgüllük ve yapı önemlidir.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Nasıl Uyum Sağlar" width="800"/>

LangChain4j, altyapıyı sağlar — model bağlantıları, bellek ve mesaj türleri — prompt desenleri ise bu altyapı üzerinden gönderdiğiniz dikkatle yapılandırılmış metinlerdir. Temel yapı taşları `SystemMessage` (AI'nın davranışını ve rolünü belirler) ve `UserMessage` (gerçek isteğinizi taşır) bileşenleridir.

## Prompt Mühendisliği Temelleri

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş Prompt Mühendisliği Deseninin Genel Görünümü" width="800"/>

Bu modüldeki ileri desenlere geçmeden önce, beş temel promptlama tekniğini gözden geçirelim. Bunlar her prompt mühendisinin bilmesi gereken yapı taşlarıdır. [Hızlı Başlangıç modülünde](../00-quick-start/README.md#2-prompt-patterns) bu tekniklerin uygulamalarını gördüyseniz, burada kavramsal çerçeveyi bulacaksınız.

### Sıfır-Atış Promptlama

En basit yaklaşım: modele örnek vermeden doğrudan bir talimat verirsiniz. Model tamamen eğitimi üzerinden görevi anlamaya ve yerine getirmeye dayanır. Beklenen davranışın açık olduğu basit istekler için uygundur.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Sıfır-Atış Promptlama" width="800"/>

*Örnek olmadan doğrudan talimat — model görevi yalnızca talimattan çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Olumlu"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler ya da modelin ek rehberlik olmadan yapabileceği herhangi bir görev.

### Az-Atış Promptlama

Modelin izlemesini istediğiniz deseni gösteren örnekler sunarsınız. Model, örneklerinizden beklenen giriş-çıkış formatını öğrenir ve bunu yeni girdilere uygular. İstenen format veya davranışın açık olmadığı görevlerde tutarlılığı önemli ölçüde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Az-Atış Promptlama" width="800"/>

*Örneklerden öğrenme — model deseni tanır ve yeni girdilere uygular*

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

**Ne zaman kullanılır:** Özel sınıflandırmalar, tutarlı biçimlendirme, alan spesifik görevler ya da sıfır-atış sonuçlarının tutarsız olduğu durumlarda.

### Düşünce Zinciri

Modelden akıl yürütmesini adım adım göstermesini istersiniz. Doğrudan cevaba atlamak yerine model problemi parçalar ve her kısmı açıkça işler. Matematik, mantık ve çok adımlı akıl yürütme görevlerinde doğruluğu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri Promptlama" width="800"/>

*Adım adım mantıklı düşünme — karmaşık problemleri açık mantıksal adımlara bölmek*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama veya akıl yürütme sürecini göstermek doğruluk ve güveni artırdığında.

### Rol Temelli Promptlama

Sorunuzdan önce AI için bir kişilik ya da rol belirleyin. Bu, yanıtın tonunu, derinliğini ve odak noktasını şekillendirir. Bir "yazılım mimarı", "genç geliştirici" ya da "güvenlik denetçisi" farklı tavsiyeler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Temelli Promptlama" width="800"/>

*Bağlam ve kişilik belirleme — aynı soru atanmış role göre farklı yanıtlar alır*

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

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan spesifik analiz veya yanıtların belirli bir uzmanlık seviyesi veya perspektife göre uyarlanması gereken durumlarda.

### Prompt Şablonları

Değişken yer tutucularla yeniden kullanılabilir promptlar oluşturun. Her seferinde yeni bir prompt yazmak yerine bir şablon tanımlayıp farklı değerlerle doldurun. LangChain4j'nin `PromptTemplate` sınıfı bunu `{{variable}}` sözdizimiyle kolaylaştırır.

<img src="../../../translated_images/tr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Şablonları" width="800"/>

*Değişken yer tutucularla yeniden kullanılabilir promptlar — tek şablon, çok kullanım*

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

**Ne zaman kullanılır:** Farklı girdilerle tekrar eden sorgular, toplu işleme, yeniden kullanılabilir AI iş akışları ya da prompt yapısının aynı kalıp verinin değiştiği senaryolar.

---

Bu beş temel teknik, çoğu promptlama görevi için sağlam bir araç seti sunar. Bu modülün geri kalanı, GPT-5.2'nin akıl yürütme kontrolü, öz değerlendirme ve yapılandırılmış çıktı özelliklerini kullanan **sekiz ileri düzey desenle** devam eder.

## İleri Düzey Desenler

Temelleri tamamladık, şimdi bu modülü benzersiz kılan sekiz ileri düzey desene geçelim. Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı yanıt ister, bazıları derin düşünme. Bazıları görünür akıl yürütme ister, bazıları sadece sonucu. Aşağıdaki her desen farklı bir senaryoya optimize edilmiştir — ve GPT-5.2'nin akıl yürütme kontrolü farkları daha da belirgin yapar.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz Promptlama Deseni" width="800"/>

*Sekiz prompt mühendisliği deseninin genel görünümü ve kullanım durumları*

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Akıl Yürütme Kontrolü" width="800"/>

*GPT-5.2'nin akıl yürütme kontrolü, modelin ne kadar düşünmesi gerektiğini belirlemenizi sağlar — hızlı doğrudan yanıtlardan derin keşfe*

**Düşük Heves (Hızlı ve Odaklı)** - Basit sorular için hızlı, doğrudan yanıtlar istediğinizde. Model minimum akıl yürütme yapar - maksimum 2 adım. Hesaplama, hızlı sorgular veya basit sorular için tercih edin.

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

> 💡 **GitHub Copilot ile keşfedin:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) dosyasını açıp sorun:
> - "Düşük heves ve yüksek hevesli promptlama desenleri arasındaki fark nedir?"
> - "Promptlardaki XML etiketleri AI yanıtını nasıl yapılandırmaya yardımcı olur?"
> - "Öz yansıtma desenlerini ne zaman doğrudan talimatlarla kıyaslayarak kullanmalıyım?"

**Yüksek Heves (Derin ve Kapsamlı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model derinlemesine keşfeder ve detaylı akıl yürütme gösterir. Sistem tasarımı, mimari kararlar veya karmaşık araştırmalar için kullanın.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım Adım İlerleme)** - Çok adımlı iş akışları için. Model önceden bir plan sunar, çalışırken her adımı açıklar, sonunda özet verir. Taşınmalar, uygulamalar veya çok adımlı süreçlerde tercih edin.

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

Düşünce Zinciri promptlaması, modelden akıl yürütme sürecini açıkça göstermesini ister ve karmaşık görevlerde doğruluğu artırır. Adım adım ayrım hem insanlar hem de AI için mantığı anlamayı kolaylaştırır.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Bu desen hakkında sorun:
> - "Uzun süren işlemler için görev yürütme deseni nasıl uyarlanır?"
> - "Üretim uygulamalarında araç ön açıklamalarını yapılandırmanın en iyi uygulamaları nelerdir?"
> - "Bir arayüzde ara ilerleme güncellemelerini nasıl yakalayıp gösterebilirim?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Deseni" width="800"/>

*Adım Adım: Planla → Yürüt → Özetle iş akışı*

**Özünü Yansıtıcı Kod** - Üretim kalitesinde kod üretmek için. Model, üretim standartlarına uygun, hata yönetimi içeren kodlar oluşturur. Yeni özellikler veya servisler geliştirirken kullanılır.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Öz Yansıtma Döngüsü" width="800"/>

*İteratif gelişim döngüsü - oluştur, değerlendir, sorunları tespit et, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model, sabit bir çerçeve kullanarak kodu inceler (doğruluk, uygulamalar, performans, güvenlik, sürdürülebilirlik). Kod incelemeleri veya kalite değerlendirmelerinde tercih edin.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Yapılandırılmış analiz ile ilgili sorun:
> - "Farklı kod inceleme türleri için analiz çerçevesi nasıl özelleştirilir?"
> - "Yapılandırılmış çıktıyı programatik olarak nasıl ayrıştırır ve işlem yaparım?"
> - "Farklı inceleme oturumlarında tutarlı şiddet seviyeleri nasıl sağlanır?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Deseni" width="800"/>

*Tutarlı kod incelemeleri için şiddet seviyesi içeren çerçeve*

**Çok Turlu Sohbet** - Bağlam gerektiren konuşmalar için. Model önceki mesajları hatırlar ve üzerine inşa eder. Etkileşimli yardım seansları veya karmaşık soru-cevaplarda kullanın.

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

*Konuşma bağlamının çok tur boyunca birikmesi, token limitine ulaşana kadar devam eder*

**Adım Adım Mantıklı Düşünme** - Görünür mantık gerektiren sorunlar için. Model her adımın açık akıl yürütmesini gösterir. Matematik problemleri, mantık bulmacaları veya düşünme sürecini anlamak istediğinizde kullanılır.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Adım Adım Deseni" width="800"/>

*Problemleri açık mantıksal adımlara bölmek*

**Kısıtlı Çıktı** - Belirli format gereksinimlerine sahip yanıtlar için. Model, biçim ve uzunluk kurallarına sıkı uyar. Özetler veya kesin çıktı yapısı gereken durumlarda kullanılır.

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

*Belirli biçim, uzunluk ve yapı kurallarının uygulanması*

## Mevcut Azure Kaynaklarının Kullanımı

**Dağıtımı doğrulayın:**

Ana dizinde Azure kimlik bilgilerini içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturulmuş):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermelidir
```

**Uygulamayı başlatın:**

> **Not:** Eğer Modül 01'de `./start-all.sh` komutuyla tüm uygulamaları zaten başlattıysanız, bu modül 8083 portunda çalışıyor demektir. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8083 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard Kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code'un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulabilirsiniz.

Spring Boot Dashboard'dan şunları yapabilirsiniz:  
- Çalışma alanındaki tüm Spring Boot uygulamalarını görmek  
- Uygulamaları tek tıklamayla başlatmak/durdurmak  
- Uygulama günlüklerini gerçek zamanlı izlemek  
- Uygulama durumunu takip etmek  
Yalnızca "prompt-engineering" yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri aynı anda çalıştırabilirsiniz.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Kontrol Paneli" width="400"/>

**Seçenek 2: Shell komut dosyalarını kullanma**

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
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Her iki komut dosyası da otomatik olarak kök `.env` dosyasından ortam değişkenlerini yükler ve JAR dosyaları yoksa oluşturur.

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
./stop.sh  # Sadece bu modül
# Veya
cd .. && ./stop-all.sh  # Tüm modüller
```

**PowerShell:**
```powershell
.\stop.ps1  # Yalnızca bu modül
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```

## Uygulama Ekran Görüntüleri

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Kontrol Paneli Ana Sayfası" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Özellikleri ve kullanım alanlarıyla birlikte tüm 8 prompt mühendisliği desenini gösteren ana kontrol paneli*

## Desenleri Keşfetmek

Web arayüzü farklı istem stratejileriyle denemeler yapmanıza olanak tanır. Her desen farklı problemleri çözer - hangi yaklaşımın ne zaman öne çıktığını görmek için deneyin.

> **Not: Akışlı (Streaming) vs Akışsız (Non-Streaming)** — Her desen sayfasında iki düğme bulunur: **🔴 Akış Yanıtı (Canlı)** ve **Akışsız** seçeneği. Akışlı yöntem, modelin oluşturduğu tokenleri gerçek zamanlı göstermek için Server-Sent Events (SSE) kullanır; böylece ilerlemeyi anında görürsünüz. Akışsız seçenek ise tüm yanıt gelene kadar bekler. Derin düşünme gerektiren istemlerde (örneğin, Yüksek Heves, Kendini Değerlendiren Kod) akışsız çağrı çok uzun sürebilir — bazen dakikalarca — ve görünür geri bildirim olmaz. **Karışık istemler üzerinde deneme yaparken akışlı modu kullanın** böylece modelin çalıştığını görebilir ve isteğin zaman aşımına uğradığı izlenimini önlersiniz.
>
> **Not: Tarayıcı Gereksinimi** — Akış özelliği, Fetch Streams API (`response.body.getReader()`) kullanır ve tam bir tarayıcı (Chrome, Edge, Firefox, Safari) gerektirir. VS Code'un yerleşik Basit Tarayıcısında çalışmaz çünkü onun web görüntüleyicisi ReadableStream API'sini desteklemez. Basit Tarayıcı kullanırsanız, akışsız düğmeler normal çalışmaya devam eder — sadece akışlı düğmeler etkilenir. Tam deneyim için `http://localhost:8083` adresini harici bir tarayıcıda açın.

### Düşük ve Yüksek Heves

"Düşük Heves" kullanarak "200'ün %15'i nedir?" gibi basit bir soru sorun. Anında, doğrudan cevap alırsınız. Şimdi "Yüksek trafikli bir API için önbellekleme stratejisi tasarla" gibi karmaşık bir soru sorun. **🔴 Akış Yanıtı (Canlı)** düğmesine tıklayın ve modelin ayrıntılı düşüncesinin token token ortaya çıkışını izleyin. Aynı model, aynı soru yapısı - ama istem ona ne kadar düşünmesi gerektiğini söylüyor.

### Görev Yürütme (Araç Önizlemeleri)

Çok adımlı iş akışları, ön planlama ve ilerleme anlatımı sayesinde daha verimlidir. Model ne yapacağını özetler, her adımı anlatır, sonra sonuçları toparlar.

### Kendini Değerlendiren Kod

"Bir e-posta doğrulama servisi oluştur"u deneyin. Sadece kod oluşturup durmak yerine model, kalite kriterlerine göre değerlendirir, zayıflıkları bulur ve geliştirir. Kod üretimi üretim standartlarına ulaşana kadar yinelemeyi göreceksiniz.

### Yapılandırılmış Analiz

Kod incelemeleri için tutarlı değerlendirme çerçeveleri gereklidir. Model kodu, doğruluk, uygulamalar, performans, güvenlik gibi sabit kategorilerle ve şiddet dereceleriyle analiz eder.

### Çok Turlu Sohbet

"Spring Boot nedir?" diye sorun, ardından hemen "Bana bir örnek göster" deyin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği verir. Hafıza olmasaydı, ikinci soru çok belirsiz olurdu.

### Adım Adım Akıl Yürütme

Bir matematik problemi seçin ve hem Adım Adım Akıl Yürütme hem de Düşük Heves ile deneyin. Düşük heves size sadece cevabı verir - hızlı ama şeffaf değil. Adım adım her hesaplama ve kararı gösterir.

### Kısıtlanmış Çıktı

Belirli format veya kelime sayısı gerektiğinde bu desen sıkı uyum sağlar. Tam olarak 100 kelimelik madde işaretli bir özet üretmeyi deneyin.

## Gerçekte Ne Öğreniyorsunuz

**Akıl Yürütme Çabası Her Şeyi Değiştirir**

GPT-5.2, istemleriniz aracılığıyla hesaplama çabasını kontrol etmenizi sağlar. Düşük çaba, hızlı yanıtlar ve az keşif demektir. Yüksek çaba, modelin derin düşünmek için zaman ayırması anlamına gelir. Siz görevin karmaşıklığına göre çabayı ayarlamayı öğreniyorsunuz - basit sorularda zaman kaybetmeyin, ama karmaşık kararlarda da acele etmeyin.

**Yapı Davranışı Yönlendirir**

İstemlerdeki XML etiketlerini fark ettiniz mi? Süs amaçlı değiller. Modeller yapılandırılmış talimatları serbest metinden daha güvenilir uygular. Çok adımlı işlemler veya karmaşık mantık gerektiğinde yapı, modelin nerede olduğunu ve sıradaki adımı takip etmesine yardımcı olur.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Yapısı" width="800"/>

*Açık bölümler ve XML tarzı organizasyon ile iyi yapılandırılmış bir istemin anatomisi*

**Kendini Değerlendirme Yoluyla Kalite**

Kendini değerlendiren desenler kalite ölçütlerini açığa çıkarır. Modelin "doğru yapmasını" ummak yerine neyin "doğru" olduğunu kesin olarak belirtirsiniz: doğru mantık, hata yönetimi, performans, güvenlik. Model kendi çıktısını değerlendirebilir ve iyileştirebilir. Bu kod üretimini bir piyango olmaktan çıkarıp bir sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok turlu sohbetler her istekle mesaj geçmişini içerir. Ancak bir sınır vardır - her modelde maksimum token sayısı vardır. Konuşmalar büyüdükçe ilgili bağlamı korumak için stratejiler geliştirmeniz gerekir. Bu modül size hafızanın nasıl çalıştığını gösterir; ileride ne zaman özetleyeceğinizi, ne zaman unutacağınızı ve ne zaman geri çağıracağınızı öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Bilgi Tabanlı Üretim)](../03-rag/README.md)

---

**Navigasyon:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluğa özen gösterilse de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlamalar veya yorumlamalar nedeniyle sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->