# Modül 02: GPT-5.2 ile Prompt Mühendisliği

## İçindekiler

- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Önkoşullar](../../../02-prompt-engineering)
- [Prompt Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [Prompt Mühendisliği Temelleri](../../../02-prompt-engineering)
  - [Sıfır-Örnek Promptlama](../../../02-prompt-engineering)
  - [Birkaç Örnekli Promptlama](../../../02-prompt-engineering)
  - [Düşünce Zinciri](../../../02-prompt-engineering)
  - [Rol Tabanlı Promptlama](../../../02-prompt-engineering)
  - [Prompt Şablonları](../../../02-prompt-engineering)
- [Gelişmiş Kalıplar](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarını Kullanma](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Kalıpları Keşfetme](../../../02-prompt-engineering)
  - [Düşük ve Yüksek İsteklilik](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Öntanımları)](../../../02-prompt-engineering)
  - [Öz Yansımalı Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Tur Sohbet](../../../02-prompt-engineering)
  - [Adım Adım Akıl Yürütme](../../../02-prompt-engineering)
  - [Kısıtlı Çıktı](../../../02-prompt-engineering)
- [Gerçekten Neler Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Neler Öğreneceksiniz

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Neler Öğreneceksiniz" width="800"/>

Önceki modülde, belleğin konuşma yapay zekasına nasıl olanak sağladığını gördünüz ve temel etkileşimler için GitHub Modellerini kullandınız. Şimdi ise Azure OpenAI’ın GPT-5.2’sini kullanarak nasıl soru soracağınız — yani promptların kendisi — üzerinde duracağız. Promptlarınızı nasıl yapılandırdığınız, aldığınız yanıtların kalitesini dramatik şekilde etkiler. Temel prompt tekniklerini gözden geçirerek başlıyoruz; ardından GPT-5.2’nin yeteneklerinden tam olarak yararlanan sekiz gelişmiş kalıba geçiyoruz.

GPT-5.2’yi tercih etmemizin sebebi akıl yürütme kontrolü sunmasıdır — modele yanıt vermeden önce ne kadar düşünmesi gerektiğini söyleyebilirsiniz. Bu, farklı prompt stratejilerini daha belirgin hale getirir ve hangi yaklaşımı ne zaman kullanacağınızı anlamanıza yardımcı olur. Ayrıca, GPT-5.2 için Azure’un GitHub Modellerine kıyasla daha az oran kısıtlaması sunması da avantajlıdır.

## Önkoşullar

- Modül 01 tamamlandı (Azure OpenAI kaynakları dağıtıldı)
- Kök dizinde Azure kimlik bilgilerini içeren `.env` dosyası (Modül 01’de `azd up` ile oluşturuldu)

> **Not:** Eğer Modül 01’i tamamlamadıysanız, önce oradaki dağıtım talimatlarını takip edin.

## Prompt Mühendisliğini Anlamak

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Mühendisliği Nedir?" width="800"/>

Prompt mühendisliği, ihtiyaç duyduğunuz sonuçları tutarlı şekilde almanızı sağlayan giriş metnini tasarlamaktır. Sadece soru sormakla ilgili değil — modelin tam olarak ne istediğinizi anlaması ve bunu nasıl sunacağını bilmesi için talepleri yapılandırmakla ilgilidir.

Bunu bir meslektaşa talimat vermeye benzetin. "Hata düzelt" muğlaktır. "UserService.java dosyasının 45. satırındaki null pointer hatasını, null kontrolü ekleyerek düzelt" ise belli ve spesifiktir. Dil modelleri de aynı şekilde çalışır — özgüllük ve yapı önemlidir.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j'nin Yeri" width="800"/>

LangChain4j; model bağlantıları, bellek ve mesaj tipleri gibi altyapıyı sağlar; prompt kalıpları ise bu altyapı üzerinden gönderdiğiniz dikkatle yapılandırılmış metindir. Ana yapı taşları `SystemMessage` (Yapay zekanın davranışını ve rolünü belirler) ve `UserMessage` (gerçek isteğinizi taşır).

## Prompt Mühendisliği Temelleri

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş Prompt Mühendisliği Kalıbı Genel Bakış" width="800"/>

Bu modüldeki gelişmiş kalıplara girmeden önce, beş temel prompt tekniğini gözden geçirelim. Bunlar her prompt mühendisi tarafından bilinmesi gereken yapı taşlarıdır. Eğer zaten [Hızlı Başlangıç modülünü](../00-quick-start/README.md#2-prompt-patterns) kullandıysanız, bunların uygulamalarını gördünüz — işte onların kavramsal çerçevesi.

### Sıfır-Örnek Promptlama

En basit yaklaşım: modele hiç örnek vermeden doğrudan talimat verirsiniz. Model, görevi anlamak ve yerine getirmek için tamamen eğitimine güvenir. Beklenen davranış bariz olduğu basit isteklerde işe yarar.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Sıfır-Örnek Promptlama" width="800"/>

*Örnek olmadan doğrudan talimat — model yalnızca talimattan görevi çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Olumlu"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler veya modelin ek rehberliğe gerek olmadan halledebileceği herhangi bir görev.

### Birkaç Örnekli Promptlama

Modelin takip etmesini istediğiniz kalıbı gösteren örnekler sağlarsınız. Model, örneklerinizden beklenen giriş-çıkış formatını öğrenir ve yeni girdilere uygular. Bu, istenen format veya davranışın bariz olmadığı görevlerde tutarlılığı dramatik şekilde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Birkaç Örnekli Promptlama" width="800"/>

*Örneklerden öğrenme — model kalıbı tanır ve yeni girdilere uygular*

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

Modelden akıl yürütmesini adım adım göstermesini istersiniz. Cevaba doğrudan atlamak yerine problem parçalara ayrılır ve her parça açıkça işlenir. Matematik, mantık ve çok aşamalı akıl yürütme görevlerinde doğruluğu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri Promptlama" width="800"/>

*Adım adım akıl yürütme — karmaşık problemleri açık mantıksal adımlara bölme*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama veya akıl yürütme sürecinin gösterilmesinin doğruluk ve güveni artırdığı her görev.

### Rol Tabanlı Promptlama

Soru sormadan önce yapay zekaya bir kişilik ya da rol atarsınız. Bu bağlam, yanıtın tonu, derinliği ve odağını şekillendirir. "Yazılım mimarı" farklı, "genç geliştirici" veya "güvenlik denetçisi" farklı öneriler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Tabanlı Promptlama" width="800"/>

*Bağlam ve kişilik belirleme — aynı soru atanmış role göre farklı yanıt alır*

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

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan-spesifik analiz veya yanıtların belirli uzmanlık seviyesi ya da bakış açısına göre özelleştirilmesi gereken durumlar.

### Prompt Şablonları

Değişken yer tutucuları olan yeniden kullanılabilir promptlar oluşturursunuz. Her seferinde yeni bir prompt yazmak yerine bir şablon tanımlayıp farklı değerlerle doldurursunuz. LangChain4j’nin `PromptTemplate` sınıfı bunu `{{variable}}` sözdizimiyle kolaylaştırır.

<img src="../../../translated_images/tr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Şablonları" width="800"/>

*Değişken yer tutucuları olan yeniden kullanılabilir promptlar — tek şablon, çok kullanım*

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

**Ne zaman kullanılır:** Farklı girdilerle tekrarlanan sorgular, toplu işlem, yeniden kullanılabilir yapay zeka iş akışları oluşturma veya prompt yapısının sabit kalıp verilerin değiştiği her senaryo.

---

Bu beş temel, çoğu prompt görevi için sağlam bir araç seti sunar. Bu modülün geri kalanı, GPT-5.2’nin akıl yürütme kontrolü, öz değerlendirme ve yapılandırılmış çıktı olanaklarını kullanan **sekiz gelişmiş kalıpla** bunların üzerine inşa eder.

## Gelişmiş Kalıplar

Temelleri tamamladıktan sonra, bu modülü benzersiz kılan sekiz gelişmiş kalıba geçelim. Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı yanıtlar ister, bazıları derin düşünce. Bazıları görünür akıl yürütme gerektirirken diğerleri sadece sonuç ister. Aşağıdaki her kalıp farklı bir senaryo için optimize edilmiştir — ve GPT-5.2’nin akıl yürütme kontrolü farkları daha da belirgin kılar.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz Prompt Kalıbı" width="800"/>

*Sekiz prompt mühendisliği kalıbı ve kullanım durumları genel bakış*

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Akıl Yürütme Kontrolü" width="800"/>

*GPT-5.2’nin akıl yürütme kontrolü modellenin ne kadar düşünmesi gerektiğini belirtmenizi sağlar — hızlı doğrudan yanıtlardan derin keşiflere kadar*

<img src="../../../translated_images/tr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Akıl Yürütme Çabası Karşılaştırması" width="800"/>

*Düşük istek (hızlı, doğrudan) vs Yüksek istek (titiz, keşifçi) akıl yürütme yaklaşımları*

**Düşük İstek (Hızlı & Odaklı)** - Hızlı, doğrudan yanıt istediğiniz basit sorular için. Model minimum akıl yürütme yapar — maksimum 2 adım. Hesaplamalar, aramalar veya basit sorular için kullanın.

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
> - "Düşük istek ve yüksek istek prompt kalıpları arasındaki fark nedir?"
> - "Promptlardaki XML etiketleri yapay zekanın yanıtını nasıl yapılandırıyor?"
> - "Ne zaman öz yansıma kalıplarını, ne zaman doğrudan talimatı kullanmalıyım?"

**Yüksek İstek (Derin & Titiz)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model ayrıntılı akıl yürütme yapar. Sistem tasarımı, mimari kararlar veya kapsamlı araştırmalar için kullanın.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım-Adım İlerleme)** - Çok adımlı iş akışları için. Model önceden plan verir, çalışırken her adımı anlatır, sonrasında özet sunar. Taşınmalar, uygulamalar veya çok adımlı süreçlerde kullanın.

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

Düşünce Zinciri promptlama, modelden akıl yürütme sürecini göstermesini açıkça ister; bu karmaşık görevlerde doğruluğu artırır. Adım adım çözüm hem insanın hem yapay zekanın mantığı anlamasını sağlar.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Bu kalıp hakkında sorun:
> - "Uzun süren işlemler için görev yürütme kalıbını nasıl uyarlardım?"
> - "Üretim uygulamalarında araç öntanımlarını yapılandırmanın en iyi uygulamaları nelerdir?"
> - "Arayüzde ara ilerleme güncellemelerini nasıl yakalar ve görüntülerim?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Kalıbı" width="800"/>

*Planla → Yürüt → Özetle iş akışı çok adımlı görevler için*

**Öz Yansımalı Kod** - Üretim kalitesinde kod üretmek için. Model, uygun hata yönetimi ile üretim standartlarına uygun kod üretir. Yeni özellikler ya da servisler oluştururken kullanın.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Öz-Yansıma Döngüsü" width="800"/>

*Yinelemeli iyileştirme döngüsü - üret, değerlendir, sorunları tespit et, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model, kodu sabit bir çerçeve kullanarak inceler (doğruluk, uygulamalar, performans, güvenlik, sürdürülebilirlik). Kod incelemeleri ya da kalite değerlendirmelerinde kullanın.

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
> - "Farklı tür kod incelemeleri için analiz çerçevesi nasıl özelleştirilir?"
> - "Yapılandırılmış çıktıyı programatik olarak en iyi nasıl ayrıştırır ve kullanırım?"
> - "Farklı inceleme oturumlarında tutarlı şiddet seviyeleri nasıl sağlanır?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Kalıbı" width="800"/>

*Tutarlı kod incelemeleri için çerçeve ve şiddet seviyeleri*

**Çok Tur Sohbet** - Bağlam gereken konuşmalar için. Model önceki mesajları hatırlar ve üzerine inşa eder. İnteraktif yardım oturumları veya karmaşık SSS için kullanın.

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

*Konuşma bağlamı birden çok turda birikir, token sınırına ulaşana kadar*

**Adım Adım Akıl Yürütme** - Görünür mantık gereken problemler için. Model her adımda açıkça akıl yürütme gösterir. Matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istediğiniz durumlarda kullanın.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Adım Adım Kalıp" width="800"/>

*Problemleri açık mantıksal adımlara bölmek*

**Kısıtlı Çıktı** - Belirli format gereksinimlerine sahip yanıtlar için. Model format ve uzunluk kurallarına kesinlikle uyar. Özetler veya kesin çıktı yapısına ihtiyaç duyduğunuzda kullanın.

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

<img src="../../../translated_images/tr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Kısıtlı Çıktı Kalıbı" width="800"/>

*Belirli format, uzunluk ve yapı gereksinimlerini zorunlu kılmak*

## Mevcut Azure Kaynaklarını Kullanma

**Dağıtımı doğrulayın:**

Kök dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01’de oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Modül 01’de `./start-all.sh` ile tüm uygulamaları zaten başlattıysanız, bu modül 8083 portunda zaten çalışıyor. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8083 adresine geçebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanmak (VS Code kullanıcıları için tavsiye edilir)**

Geliştirici konteynerinde, tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlayan Spring Boot Dashboard uzantısı bulunmaktadır. Bunu VS Code’un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesine bakın) bulabilirsiniz.
Spring Boot Panosu'ndan şunları yapabilirsiniz:
- Çalışma alanındaki tüm kullanılabilir Spring Boot uygulamalarını görün
- Uygulamaları tek tıkla başlat/durdur
- Uygulama günlüklerini gerçek zamanlı olarak görüntüle
- Uygulama durumunu izleyin

Bu modülü başlatmak için "prompt-engineering" yanındaki oynat düğmesine tıklayın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell scriptleri kullanarak**

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

Her iki script de kök `.env` dosyasından ortam değişkenlerini otomatik olarak yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlatmadan önce tüm modülleri manuel olarak derlemeyi tercih ederseniz:
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

*Tüm 8 prompt mühendisliği modelini ve özellikleri ile kullanım alanlarını gösteren ana pano*

## Modelleri Keşfetmek

Web arayüzü farklı promptlama stratejilerini denemenizi sağlar. Her model farklı sorunları çözer - hangisinin ne zaman işe yaradığını görmek için deneyin.

### Düşük ve Yüksek İstek Seviyesi

"200'ün %15'i nedir?" gibi basit bir soruyu Düşük İstek Seviyesi ile sorun. Anında, doğrudan bir cevap alırsınız. Şimdi "Yüksek trafikli bir API için önbellekleme stratejisi tasarla" gibi karmaşık bir soruyu Yüksek İstek Seviyesi ile sorun. Modelin yavaşlayıp ayrıntılı gerekçeler sunduğunu göreceksiniz. Aynı model, aynı soru yapısı - ama prompt ona ne kadar düşünmesi gerektiğini söyler.

<img src="../../../translated_images/tr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Minimal gerekçeyle hızlı hesaplama*

<img src="../../../translated_images/tr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Kapsamlı önbellekleme stratejisi (2.8MB)*

### Görev Yürütme (Araç Başlangıçları)

Çok adımlı iş akışları, önceden planlama ve ilerleme anlatımıyla fayda sağlar. Model ne yapacağını özetler, her adımı anlatır, sonra sonuçları özetler.

<img src="../../../translated_images/tr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Adım adım anlatımla REST uç noktası oluşturma (3.9MB)*

### Kendini Değerlendiren Kod

"Bir e-posta doğrulama servisi oluştur" deyin. Model sadece kod üretip durmak yerine, üretir, kalite kriterlerine göre değerlendirir, zayıf noktaları tespit eder ve geliştirir. Kod üretim standartlarına ulaşana kadar yinelemesini göreceksiniz.

<img src="../../../translated_images/tr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Tamamlanmış e-posta doğrulama servisi (5.2MB)*

### Yapılandırılmış Analiz

Kod incelemeleri için tutarlı değerlendirme çerçeveleri gerekir. Model kodu sabit kategorilere göre analiz eder (doğruluk, uygulamalar, performans, güvenlik) ve şiddet seviyeleri uygular.

<img src="../../../translated_images/tr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Çerçeve tabanlı kod incelemesi*

### Çoklu Tur Sohbet

"Spring Boot nedir?" diye sorun, ardından hemen "Bir örnek göster" diye devam edin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği verir. Hafıza olmadan ikinci soru çok genel olurdu.

<img src="../../../translated_images/tr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Sorular arasında bağlam koruması*

### Adım Adım Mantık Yürütme

Bir matematik problemi seçin ve hem Adım Adım Mantık Yürütme hem de Düşük İstek Seviyesi ile deneyin. Düşük istek sadece cevabı hızlı ama şeffaf olmayan şekilde verir. Adım adım tüm hesaplamaları ve kararları gösterir.

<img src="../../../translated_images/tr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Açık adımlarla matematik problemi*

### Kısıtlı Çıktı

Belirli formatlar veya kelime sayıları gerektiğinde, bu model katı uyumu zorunlu kılar. Tam olarak 100 kelimelik madde işaretli bir özet oluşturmayı deneyin.

<img src="../../../translated_images/tr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Format kontrolü ile makine öğrenimi özeti*

## Gerçekten Ne Öğreniyorsunuz

**Mantıksal Çaba Her Şeyi Değiştirir**

GPT-5.2, hesaplama çabasını promptlarınızla kontrol etmenizi sağlar. Düşük çaba, hızlı yanıtlar ve minimal keşif demektir. Yüksek çaba, modelin derin düşünmesi için zaman ayırmasıdır. Görev karmaşıklığına göre çaba ayarlamayı öğreniyorsunuz - basit sorulara zaman harcamayın, ama karmaşık kararlarda acele etmeyin.

**Yapı Davranışı Yönlendirir**

Promptlardaki XML etiketlerini fark ettiniz mi? Bunlar süs değil. Modeller yapısal talimatları serbest metinden daha güvenilir takip eder. Çok adımlı süreçler veya karmaşık mantık gerektiğinde, yapı modelin nerede olduğunu ve sıradaki adımı takip etmesine yardımcı olur.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Açık bölümler ve XML tarzı düzenlemeyle iyi yapılandırılmış prompt anatomisi*

**Kalite Kendi Kendine Değerlendirme ile Sağlanır**

Kendini değerlendiren modeller, kalite kriterlerini açıkça belirtir. Modelin "doğru yapması" umuduyla değil, size "doğru"nun ne anlama geldiğini (doğru mantık, hata yönetimi, performans, güvenlik) söylersiniz. Model çıktısını kendisi değerlendirip geliştirir. Böylece kod üretimi bir piyangodan sürece dönüşür.

**Bağlam Sınırlıdır**

Çok tur sohbetler, her istekte mesaj geçmişini içerir. Ama bir sınır var - her modelin maksimum token sayısı vardır. Konuşmalar büyüdükçe, ilgili bağlamı koruyup sınırı aşmamak için stratejiler lazım. Bu modül hafızanın nasıl çalıştığını gösterir; ileride ne zaman özetleyeceğinizi, ne zaman unutacağınızı ve ne zaman geri getireceğinizi öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Arama Destekli Üretim)](../03-rag/README.md)

---

**Navigasyon:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek herhangi bir yanlış anlama veya yorum hatasından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->