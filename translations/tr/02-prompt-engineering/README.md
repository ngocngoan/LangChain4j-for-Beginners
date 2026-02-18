# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Önkoşullar](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Prompt Mühendisliği Temelleri](../../../02-prompt-engineering)
  - [Sıfır-Örnekli Promptlama](../../../02-prompt-engineering)
  - [Az-Örnekli Promptlama](../../../02-prompt-engineering)
  - [Düşünce Zinciri](../../../02-prompt-engineering)
  - [Rol Tabanlı Promptlama](../../../02-prompt-engineering)
  - [Prompt Şablonları](../../../02-prompt-engineering)
- [İleri Düzey Desenler](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarını Kullanmak](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Desenleri Keşfetmek](../../../02-prompt-engineering)
  - [Düşük vs Yüksek Heves](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Girişleri)](../../../02-prompt-engineering)
  - [Öz-Yansıtıcı Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Tur Chat](../../../02-prompt-engineering)
  - [Adım Adım Akıl Yürütme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekten Neler Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Neler Öğreneceksiniz

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Neler Öğreneceksiniz" width="800"/>

Önceki modülde, belleğin konuşma tabanlı yapay zekayı nasıl mümkün kıldığına ve temel etkileşimler için GitHub Modellerini nasıl kullandığınıza baktınız. Şimdi, soru sormanın — yani promptların — kendisine odaklanacağız; Azure OpenAI'nin GPT-5.2'sini kullanarak. Promptlarınızı nasıl yapılandırdığınız, aldığınız yanıtların kalitesini önemli ölçüde etkiler. Öncelikle temel promptlama tekniklerine göz atacağız, ardından GPT-5.2'nin yeteneklerinden tam olarak yararlanan sekiz ileri düzey desene geçeceğiz.

GPT-5.2'yi kullanıyoruz çünkü mantık yürütme kontrolü sunuyor — modele yanıt vermeden önce ne kadar düşünmesi gerektiğini söyleyebiliyorsunuz. Bu, farklı promptlama stratejilerini daha görünür kılar ve hangi yaklaşımın ne zaman kullanılacağını anlamanıza yardımcı olur. Ayrıca, GitHub Modellerine kıyasla Azure'un GPT-5.2 için daha az oran sınırı avantajından faydalanacağız.

## Önkoşullar

- Modül 01'in tamamlanmış olması (Azure OpenAI kaynaklarının dağıtılması)
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (`azd up` komutuyla Modül 01'de oluşturuldu)

> **Not:** Eğer Modül 01'i tamamlamadıysanız, öncelikle oradaki dağıtım talimatlarını izleyin.

## Prompt Mühendisliğini Anlamak

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Mühendisliği Nedir?" width="800"/>

Prompt mühendisliği, size tutarlı şekilde gereken sonuçları getiren giriş metnini tasarlamakla ilgilidir. Sadece soru sormak değil — modelin tam olarak ne istediğinizi ve nasıl sunacağını anlayacağı şekilde istekleri yapılandırmak demektir.

Bir meslektaşınıza talimat vermek gibi düşünün. "Hatayı düzelt" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer hatasını null kontrolü ekleyerek düzelt" ise nettir. Dil modelleri de aynı şekilde çalışır — özgüllük ve yapı önemlidir.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Nasıl Oturur" width="800"/>

LangChain4j, altyapıyı sağlar — model bağlantıları, bellek ve mesaj tipleri — prompt desenleri ise bu altyapı üzerinden gönderdiğiniz özenle yapılandırılmış metinlerdir. Ana yapı taşları `SystemMessage` (yapay zekanın davranışını ve rolünü ayarlar) ve `UserMessage` (gerçek isteğinizi taşır).

## Prompt Mühendisliği Temelleri

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş Prompt Mühendisliği Deseninin Genel Görünümü" width="800"/>

Bu modülde ileri düzey desenlere geçmeden önce, beş temel promptlama tekniğini gözden geçirelim. Bunlar her prompt mühendisi tarafından bilinmesi gereken yapı taşlarıdır. Eğer [Hızlı Başlangıç modülünü](../00-quick-start/README.md#2-prompt-patterns) tamamladıysanız, bunları uygulamalı gördünüz — işte bunların arkasındaki kavramsal çerçeve.

### Sıfır-Örnekli Promptlama

En basit yaklaşım: modele örneksiz doğrudan bir talimat verirsiniz. Model, görevi anlamak ve yerine getirmek için tamamen eğitimine güvenir. Beklenen davranışın açık olduğu basit talepler için iyi çalışır.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Sıfır-Örnekli Promptlama" width="800"/>

*Örnek olmadan doğrudan talimat — model sadece talimattan görevi çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Pozitif"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler veya modelin ek rehberlik olmadan halledebileceği her türlü görev.

### Az-Örnekli Promptlama

Modelin izlemesini istediğiniz kalıbı gösteren örnekler sunarsınız. Model, beklenen giriş-çıkış formatını örneklerinizden öğrenir ve yeni girdilerde uygular. İstenen format ya da davranış belli olmadığında tutarlılığı önemli ölçüde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Az-Örnekli Promptlama" width="800"/>

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

**Ne zaman kullanılır:** Özel sınıflandırmalar, tutarlı biçimlendirme, alan özgü görevler veya sıfır-örnekli sonuçlar tutarsız olduğunda.

### Düşünce Zinciri

Modelden adım adım akıl yürütmesini istemek. Model doğrudan cevaba atlamak yerine problemi parçalara ayırır ve her bölüm üzerinde açıkça çalışır. Matematik, mantık ve çok adımlı akıl yürütme gerektiren görevlerin doğruluğunu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri Promptlama" width="800"/>

*Adım adım akıl yürütme — karmaşık problemleri açık mantıksal adımlara bölmek*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama veya akıl yürütme sürecinin gösterilmesinin doğruluk ve güven arttırdığı her görev.

### Rol Tabanlı Promptlama

Sorunuzu sormadan önce yapay zekaya bir kişi ya da rol atayın. Bu, yanıtın tonunu, derinliğini ve odak noktasını şekillendiren bağlam sağlar. Bir "yazılım mimarı" farklı, bir "genç geliştirici" ya da "güvenlik denetçisi" farklı öneriler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Tabanlı Promptlama" width="800"/>

*Bağlam ve rol belirleme — aynı soru, atanan role göre farklı yanıt alır*

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

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan özgü analiz veya belirli uzmanlık seviyesi ve perspektife göre yanıtlar gerektiğinde.

### Prompt Şablonları

Değişken yer tutucularla yeniden kullanılabilir promptlar oluşturun. Her seferinde yeni prompt yazmak yerine bir şablon tanımlayın ve farklı değerlerle doldurun. LangChain4j'nin `PromptTemplate` sınıfı `{{variable}}` sözdizimi ile bunu kolaylaştırır.

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

**Ne zaman kullanılır:** Farklı girdilerle tekrarlanan sorgular, toplu işler, yeniden kullanılabilir yapay zeka iş akışları veya prompt yapısı değişmez ama veriler değişirse.

---

Bu beş temel, çoğu promptlama görevi için sağlam bir araç seti sağlar. Bu modülün geri kalanı, GPT-5.2'nin akıl yürütme kontrolü, özdeğerlendirme ve yapılandırılmış çıktı özelliklerini kullanan **sekiz ileri düzey desenle** üzerine inşa eder.

## İleri Düzey Desenler

Temelleri tamamladığımıza göre, bu modülü benzersiz kılan sekiz ileri düzey desene geçelim. Tüm sorunlar aynı yaklaşımı gerektirmez. Bazı sorular hızlı cevap ister, bazıları derin düşünce. Bazıları görünür akıl yürütme gerektirir, bazıları sadece sonucu. Aşağıdaki her desen farklı bir senaryo için optimize edilmiştir — ve GPT-5.2'nin akıl yürütme kontrolü farkları çok daha belirgin kılar.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz Promptlama Deseni" width="800"/>

*Sekiz prompt mühendisliği deseninin genel görünümü ve kullanım alanları*

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Akıl Yürütme Kontrolü" width="800"/>

*GPT-5.2'nin akıl yürütme kontrolü, modelin ne kadar düşünmesi gerektiğini belirtmenizi sağlar — hızlı doğrudan yanıtlardan derin keşiflere kadar*

**Düşük Heves (Hızlı & Odaklı)** - Hızlı, doğrudan yanıt istediğiniz basit sorular için. Model minimum akıl yürütme yapar - maksimum 2 adım. Hesaplamalar, sorgular veya doğrudan sorular için kullanın.

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
> - "Düşük heves ve yüksek heves promptlama desenleri arasındaki fark nedir?"
> - "Promptlardaki XML etiketleri yapay zekanın yanıtını nasıl yapılandırmaya yardımcı olur?"
> - "Öz-yansıtma desenlerini doğrudan talimat ile ne zaman kullanmalıyım?"

**Yüksek Heves (Derin & Kapsamlı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model detaylı akıl yürütme yapar ve ayrıntılı açıklamalar sunar. Sistem tasarımı, mimari kararlar veya karmaşık araştırmalar için kullanın.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım Adım İlerleme)** - Çok adımlı iş akışları için. Model baştan bir plan sunar, çalışırken her adımı anlatır, sonra özet verir. Taşınmalar, uygulamalar veya herhangi çok adımlı süreçte kullanın.

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

Düşünce Zinciri promptlama, modelden akıl yürütme sürecini açıkça göstermesini ister; karmaşık görevlerde doğruluğu artırır. Adım adım analiz hem insanlar hem yapay zeka için mantığı anlamayı kolaylaştırır.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Bu desen hakkında sorun:
> - "Uzun süren işlemler için görev yürütme desenini nasıl uyarlardım?"
> - "Üretim uygulamalarında araç girişleri nasıl yapılandırılır? En iyi uygulamalar nelerdir?"
> - "Ara ara ilerleme güncellemelerini UI'da nasıl yakalar ve gösteririm?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Deseni" width="800"/>

*Planla → Yürüt → Özetle çok adımlı görev iş akışı*

**Öz-Yansıtıcı Kod** - Üretim kalitesinde kod oluşturmak için. Model, uygun hata yönetimi ile üretim standartlarına uygun kod üretir. Yeni özellikler veya servisler geliştirirken kullanın.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Öz-Yansıtma Döngüsü" width="800"/>

*Yinelemeli geliştirme döngüsü - oluştur, değerlendir, sorunları belirle, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model, kodu sabit bir çerçeve kullanarak (doğruluk, uygulamalar, performans, güvenlik, bakım kolaylığı) inceler. Kod incelemeleri ya da kalite değerlendirmelerinde kullanın.

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
> - "Farklı kod inceleme türleri için analiz çerçevesini nasıl özelleştiririm?"
> - "Yapılandırılmış çıktıyı programatik olarak çözümleyip kullanmanın en iyi yolu nedir?"
> - "Farklı inceleme oturumlarında tutarlı şiddet seviyelerini nasıl sağlarım?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Deseni" width="800"/>

*Şiddet seviyeleri ile tutarlı kod inceleme çerçevesi*

**Çok Tur Chat** - Bağlam gerektiren sohbetler için. Model, önceki mesajları hatırlar ve üzerine inşa eder. Etkileşimli yardım veya karmaşık SSS için kullanın.

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

*Konuşma bağlamı birden çok tur boyunca toplanır, token sınırına ulaşana kadar*

**Adım Adım Akıl Yürütme** - Görünür mantık gerektiren problemler için. Model, her adım için açık akıl yürütme gösterir. Matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istendiğinde kullanın.

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

*Karmaşık problemleri açık mantıksal adımlara bölme*

**Kısıtlı Çıktı** - Belirli format gereksinimlerine sahip yanıtlar için. Model, biçim ve uzunluk kurallarına kesinlikle uyar. Özetler veya hassas çıktı yapısı gerektiğinde kullanın.

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

*Belirli format, uzunluk ve yapı gereksinimlerini zorlama*

## Mevcut Azure Kaynaklarını Kullanmak

**Dağıtımı doğrulayın:**

Azure kimlik bilgileri içeren `.env` dosyasının kök dizinde mevcut olduğundan emin olun (Modül 01 sırasında oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Eğer Modül 01'den `./start-all.sh` komutuyla tüm uygulamaları zaten başlattıysanız, bu modül 8083 portunda zaten çalışıyor. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8083 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard Kullanmak (VS Code kullanıcıları için önerilir)**

Geliştirme konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code'un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesine bakın) bulabilirsiniz.

Spring Boot Dashboard’dan şunları yapabilirsiniz:
- Çalışma alanındaki mevcut tüm Spring Boot uygulamalarını görmek
- Tek tıkla uygulamaları başlatmak/durdurmak
- Uygulama günlüklerini gerçek zamanlı izlemek
- Uygulama durumunu takip etmek
Sadece "prompt-engineering" yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri aynı anda başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Gösterge Paneli" width="400"/>

**Seçenek 2: Shell betiklerini kullanma**

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

Her iki betik de kök `.env` dosyasından ortam değişkenlerini otomatik olarak yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlamadan önce tüm modülleri manuel olarak oluşturmayı tercih ederseniz:
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

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Gösterge Paneli Ana Sayfa" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Tüm 8 prompt mühendisliği desenini ve bunların özellikleri ile kullanım durumlarını gösteren ana gösterge paneli*

## Desenlerin Keşfi

Web arayüzü, farklı prompt stratejileriyle denemeler yapmanıza olanak tanır. Her desen farklı problemleri çözer - hangi yaklaşımın ne zaman parladığını görmek için deneyin.

### Düşük vs Yüksek İsteklilik

"Düşük İsteklilik" kullanarak "200'ün %15'i nedir?" gibi basit bir soru sorun. Hemen doğrudan bir cevap alırsınız. Şimdi "Yüksek trafik API'si için bir önbellekleme stratejisi tasarla" gibi karmaşık bir soru sorun ve Yüksek İsteklilik kullanın. Modelin nasıl yavaşladığını ve detaylı gerekçelendirme sunduğunu izleyin. Aynı model, aynı soru yapısı - ancak prompt ona ne kadar düşünmesi gerektiğini söyler.

<img src="../../../translated_images/tr/low-eagerness-demo.898894591fb23aa0.webp" alt="Düşük İsteklilik Demo" width="800"/>

*Minimum akıl yürütme ile hızlı hesaplama*

<img src="../../../translated_images/tr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Yüksek İsteklilik Demo" width="800"/>

*Kapsamlı önbellekleme stratejisi (2.8MB)*

### Görev Yürütme (Araç Başlangıçları)

Çok adımlı iş akışları önceden planlama ve ilerleme anlatımı ile fayda sağlar. Model ne yapacağını taslaklar, her adımı anlatır, sonra sonuçları özetler.

<img src="../../../translated_images/tr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Görev Yürütme Demo" width="800"/>

*Adım adım anlatımla REST uç noktası oluşturma (3.9MB)*

### Kendini Değerlendiren Kod

"Bir e-posta doğrulama servisi oluştur" deneyin. Model sadece kod üretmekle kalmaz, kalite kriterlerine göre değerlendirir, zayıf noktaları belirler ve geliştirir. Kod üretim standartlarına ulaşana kadar yineleme süreçlerini görürsünüz.

<img src="../../../translated_images/tr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Kendini Değerlendiren Kod Demo" width="800"/>

*Tam e-posta doğrulama servisi (5.2MB)*

### Yapılandırılmış Analiz

Kod incelemeleri tutarlı değerlendirme çerçeveleri gerektirir. Model sabit kategorilerle (doğruluk, uygulamalar, performans, güvenlik) ve şiddet seviyeleriyle kodu analiz eder.

<img src="../../../translated_images/tr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Yapılandırılmış Analiz Demo" width="800"/>

*Çerçeve tabanlı kod incelemesi*

### Çok Tur Sohbet

"Spring Boot nedir?" diye sorun ardından hemen "Bana bir örnek göster" deyin. Model ilk sorunu hatırlar ve size özel olarak bir Spring Boot örneği sunar. Bellek olmadan, ikinci soru çok belirsiz olurdu.

<img src="../../../translated_images/tr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Çok Tur Sohbet Demo" width="800"/>

*Sorular arasında bağlamın korunması*

### Adım Adım Akıl Yürütme

Bir matematik problemi seçip hem Adım Adım Akıl Yürütme hem de Düşük İsteklilik ile deneyin. Düşük isteklilik sadece cevabı hızlıca verir - hızlı ama şeffaf değil. Adım adım her hesaplamayı ve kararı gösterir.

<img src="../../../translated_images/tr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Adım Adım Akıl Yürütme Demo" width="800"/>

*Belirgin adımlı matematik problemi*

### Kısıtlı Çıktı

Belirli formatlar veya kelime sayıları gerektiğinde bu desen katı uyumu zorunlu kılar. Tam 100 kelimelik madde işaretli bir özet oluşturmayı deneyin.

<img src="../../../translated_images/tr/constrained-output-demo.567cc45b75da1633.webp" alt="Kısıtlı Çıktı Demo" width="800"/>

*Biçim kontrolü ile makine öğrenimi özeti*

## Gerçekten Ne Öğreniyorsunuz

**Akıl Yürütme Çabası Her Şeyi Değiştirir**

GPT-5.2, promptlarınızla hesaplama çabasını kontrol etmenizi sağlar. Düşük çaba, minimal keşif ile hızlı yanıtlar demektir. Yüksek çaba modelin derin düşünmek için zaman harcamasını sağlar. Görevin karmaşıklığına göre çabayı ayarlamayı öğreniyorsunuz - basit sorularda zaman kaybetmeyin, ancak karmaşık kararları da aceleye getirmeyin.

**Yapı Davranışı Yönlendirir**

Promptlardaki XML etiketlerini fark ettiniz mi? Bunlar süs değil. Modeller yapılandırılmış talimatları serbest metinden daha güvenilir takip eder. Çok adımlı süreçler veya karmaşık mantık gerektiğinde, yapı modelin nerede olduğunu ve sıradaki adımı takip etmesine yardımcı olur.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Yapısı" width="800"/>

*Açık bölümler ve XML tarzı organizasyon ile iyi yapılandırılmış bir promptun anatomisi*

**Kalite Kendi Kendini Değerlendirmeyle Gelir**

Kendini değerlendiren desenler kalite kriterlerini açık hale getirerek çalışır. Modelin "doğru yapacağına" umut etmek yerine, "doğru"nun ne anlama geldiğini (doğru mantık, hata yönetimi, performans, güvenlik) tam olarak söylersiniz. Model böylece kendi çıktısını değerlendirebilir ve geliştirir. Bu kod üretimini bir piyangodan sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok tur sohbetler, her istekte mesaj geçmişi ekleyerek çalışır. Ama bir sınır vardır - her modelin maksimum token sayısı vardır. Konuşmalar büyüdükçe, ilgili bağlamı koruyup tavanı aşmamak için stratejilere ihtiyacınız olur. Bu modül belleğin nasıl çalıştığını gösterir; daha sonra ne zaman özetleneceğini, ne zaman unutulacağını ve ne zaman geri çağrılacağını öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Gezinme:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belgenin kendi dilindeki versiyonu yetkili kaynak olarak kabul edilmelidir. Önemli bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu oluşabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->