# Modül 02: GPT-5.2 ile İstem Mühendisliği

## İçindekiler

- [Video Tanıtımı](../../../02-prompt-engineering)
- [Neler Öğreneceksiniz](../../../02-prompt-engineering)
- [Önkoşullar](../../../02-prompt-engineering)
- [İstem Mühendisliğini Anlamak](../../../02-prompt-engineering)
- [İstem Mühendisliğinin Temelleri](../../../02-prompt-engineering)
  - [Sıfır-Atış İstemleme](../../../02-prompt-engineering)
  - [Az Örnekli İstemleme](../../../02-prompt-engineering)
  - [Düşünce Zinciri](../../../02-prompt-engineering)
  - [Rol Tabanlı İstemleme](../../../02-prompt-engineering)
  - [İstem Şablonları](../../../02-prompt-engineering)
- [Gelişmiş Kalıplar](../../../02-prompt-engineering)
- [Mevcut Azure Kaynaklarını Kullanma](../../../02-prompt-engineering)
- [Uygulama Ekran Görüntüleri](../../../02-prompt-engineering)
- [Kalıpları Keşfetmek](../../../02-prompt-engineering)
  - [Düşük ve Yüksek Heves](../../../02-prompt-engineering)
  - [Görev Yürütme (Araç Girişleri)](../../../02-prompt-engineering)
  - [Kendi Kendini Değerlendiren Kod](../../../02-prompt-engineering)
  - [Yapılandırılmış Analiz](../../../02-prompt-engineering)
  - [Çok Tur Konuşma](../../../02-prompt-engineering)
  - [Adım Adım Muhakeme](../../../02-prompt-engineering)
  - [Kısıtlanmış Çıktı](../../../02-prompt-engineering)
- [Gerçekte Neler Öğreniyorsunuz](../../../02-prompt-engineering)
- [Sonraki Adımlar](../../../02-prompt-engineering)

## Video Tanıtımı

Bu modüle nasıl başlanacağını açıklayan canlı oturumu izleyin:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4j ile İstem Mühendisliği - Canlı Oturum" width="800"/></a>

## Neler Öğreneceksiniz

<img src="../../../translated_images/tr/what-youll-learn.c68269ac048503b2.webp" alt="Neler Öğreneceksiniz" width="800"/>

Önceki modülde, belleğin konusmalı yapay zekayı nasıl mümkün kıldığına baktınız ve temel etkileşimler için GitHub Modellerini kullandınız. Şimdi soruları nasıl sorduğunuza—yani istemlere—odaklanacağız; bu kez Azure OpenAI'nin GPT-5.2'sini kullanacağız. İstemlerinizi nasıl yapılandırdığınız, aldığınız yanıtların kalitesini önemli ölçüde etkiler. Temel istem tekniklerini gözden geçerek başlıyoruz, sonra GPT-5.2'nin tüm avantajlarını kullanan sekiz gelişmiş kalıba geçiyoruz.

GPT-5.2'yi kullanıyoruz çünkü muhakeme kontrolünü tanıtıyor - modele cevap vermeden önce ne kadar düşünmesi gerektiğini söyleyebilirsiniz. Bu, farklı istem stratejilerini daha belirgin hale getirir ve her yaklaşımı ne zaman kullanmanız gerektiğini anlamanıza yardımcı olur. Ayrıca Azure'un GitHub Modellerine göre GPT-5.2 için daha az oran sınırlaması avantajından da faydalanıyoruz.

## Önkoşullar

- Modül 01 tamamlanmış (Azure OpenAI kaynakları dağıtıldı)
- Ana dizinde `.env` dosyası Azure kimlik bilgileri ile (Modül 01'de `azd up` tarafından oluşturuldu)

> **Not:** Modül 01'i tamamlamadıysanız önce oradaki dağıtım talimatlarını izleyin.

## İstem Mühendisliğini Anlamak

<img src="../../../translated_images/tr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="İstem Mühendisliği Nedir?" width="800"/>

İstem mühendisliği, istediğiniz sonuçları tutarlı şekilde almanızı sağlayan giriş metnini tasarlamaktır. Sadece soru sormak değildir — isteği modelin ne istediğinizi ve nasıl sunacağını tam anlaması için yapılandırmaktır.

Bunu bir iş arkadaşınıza talimat vermek gibi düşünün. "Hatı düzelt" belirsizdir. "UserService.java dosyasının 45. satırındaki null pointer hatasını null kontrolü ekleyerek düzelt" ise spesifiktir. Dil modelleri de aynı şekilde çalışır — özgüllük ve yapı önemlidir.

<img src="../../../translated_images/tr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j Nasıl Uyar?" width="800"/>

LangChain4j altyapıyı sağlar — model bağlantıları, bellek ve mesaj türleri — istem kalıpları ise bu altyapı üzerinden gönderdiğiniz dikkatle yapılandırılmış metinlerdir. Temel yapı taşları `SystemMessage` (Yapay zekânın davranışını ve rolünü belirler) ve `UserMessage` (gerçek isteğinizi taşır) öğeleridir.

## İstem Mühendisliğinin Temelleri

<img src="../../../translated_images/tr/five-patterns-overview.160f35045ffd2a94.webp" alt="Beş İstem Mühendisliği Kalıbı Genel Bakış" width="800"/>

Bu modüldeki gelişmiş kalıplara dalmadan önce beş temel istem tekniğini gözden geçirelim. Bunlar her istem mühendisinin bilmesi gereken yapı taşlarıdır. Eğer zaten [Hızlı Başlangıç modülünü](../00-quick-start/README.md#2-prompt-patterns) tamamladıysanız, bunları uygulamada gördünüz — işte bunların kavramsal çerçevesi.

### Sıfır-Atış İstemleme

En basit yaklaşım: modele örnek vermeden doğrudan talimat verin. Model görevi anlamak ve yerine getirmek için tamamen eğitimine dayanır. Beklenen davranış açık olduğu için basit talepler için uygundur.

<img src="../../../translated_images/tr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Sıfır-Atış İstemleme" width="800"/>

*Örnek olmadan doğrudan talimat — model görevi sadece talimattan çıkarır*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Yanıt: "Pozitif"
```

**Ne zaman kullanılır:** Basit sınıflandırmalar, doğrudan sorular, çeviriler veya modele ek rehberlik olmadan yapılabilen herhangi görevlerde.

### Az Örnekli İstemleme

Modelin izlemesini istediğiniz kalıbı gösteren örnekler verin. Model örneklerden beklenen girdi-çıktı formatını öğrenir ve yeni girdilere uygular. İstenen format veya davranışın belli olmadığı görevlerde tutarlılığı önemli ölçüde artırır.

<img src="../../../translated_images/tr/few-shot-prompting.9d9eace1da88989a.webp" alt="Az Örnekli İstemleme" width="800"/>

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

**Ne zaman kullanılır:** Özel sınıflandırmalar, tutarlı formatlama, alan-spesifik görevler veya sıfır-atış sonuçlar tutarsız olduğunda.

### Düşünce Zinciri

Modelden muhakemesini adım adım göstermesini isteyin. Model doğrudan cevaba atlamak yerine problemi parçalar ve her kısmı açıkça işler. Matematik, mantık ve çok adımlı muhakeme görevlerinde doğruluğu artırır.

<img src="../../../translated_images/tr/chain-of-thought.5cff6630e2657e2a.webp" alt="Düşünce Zinciri İstemleme" width="800"/>

*Adım adım muhakeme — karmaşık problemleri açık mantıksal adımlara bölme*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// Model gösteriyor: 15 - 8 = 7, sonra 7 + 12 = 19 elma
```

**Ne zaman kullanılır:** Matematik problemleri, mantık bulmacaları, hata ayıklama ya da muhakeme sürecini göstermenin doğruluğu ve güveni artırdığı görevlerde.

### Rol Tabanlı İstemleme

Soru sormadan önce yapay zekaya bir persona ya da rol atayın. Bu, yanıtın tonunu, derinliğini ve odağını şekillendiren bağlam sağlar. Bir "yazılım mimarı" ile "jünyor geliştirici" ya da "güvenlik denetçisi" farklı tavsiyeler verir.

<img src="../../../translated_images/tr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Rol Tabanlı İstemleme" width="800"/>

*Bağlam ve persona belirleme — aynı soruya atanan role göre farklı yanıt*

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

**Ne zaman kullanılır:** Kod incelemeleri, eğitim, alan-spesifik analizler veya belirli bir uzmanlık seviyesi ya da perspektife göre uyarlanmış yanıtlar gerektiğinde.

### İstem Şablonları

Değişken yer tutucular içeren yeniden kullanılabilir istemler oluşturun. Her seferinde yeni bir istem yazmak yerine bir defa şablon tanımlayın ve farklı değerlerle doldurun. LangChain4j’nin `PromptTemplate` sınıfı `{{değişken}}` sözdizimi ile bunu kolaylaştırır.

<img src="../../../translated_images/tr/prompt-templates.14bfc37d45f1a933.webp" alt="İstem Şablonları" width="800"/>

*Değişken yer tutucularla yeniden kullanılabilir istemler — tek şablon, çok kullanım*

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

**Ne zaman kullanılır:** Farklı girdilerle tekrar eden sorgular, toplu işlem, yeniden kullanılabilir Yapay Zeka iş akışları yaratma veya istem yapısı sabit kalıp veriler değiştiğinde.

---

Bu beş temel, çoğu istem görevini çözmek için sağlam bir araç seti sağlar. Bu modülün geri kalanı, GPT-5.2’nin muhakeme kontrolü, öz-değerlendirme ve yapılandırılmış çıktı yeteneklerini kullanan **sekiz gelişmiş kalıp** üzerine kurulur.

## Gelişmiş Kalıplar

Temelleri öğrendikten sonra bu modülü benzersiz kılan sekiz gelişmiş kalıba geçelim. Tüm problemler aynı yaklaşımı gerektirmez. Bazı sorular hızlı yanıt ister, bazıları derin düşünce. Bazıları görünür muhakeme, bazıları sadece sonuç ister. Aşağıdaki her kalıp farklı bir senaryo için optimize edilmiştir — ve GPT-5.2’nin muhakeme kontrolü bu farkları daha da belirgin hale getirir.

<img src="../../../translated_images/tr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Sekiz İstem Mühendisliği Kalıbı" width="800"/>

*Sekiz istem mühendisliği kalıbı ve kullanım durumları genel bakışı*

<img src="../../../translated_images/tr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ile Muhakeme Kontrolü" width="800"/>

*GPT-5.2'nin muhakeme kontrolü, modelin ne kadar düşünmesi gerektiğini belirtmenizi sağlar — hızlı doğrudan yanıtlardan derin keşfe kadar*

**Düşük Heves (Hızlı ve Odaklanmış)** - Hızlı, doğrudan yanıt istediğiniz basit sorular için. Model minimal muhakeme yapar - en fazla 2 adım. Hesaplamalar, aramalar veya basit sorular için kullanın.

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
> - "Düşük heves ve yüksek heves istem kalıpları arasındaki fark nedir?"
> - "İstemlerdeki XML etiketleri yapay zekanın yanıtını nasıl yapılandırmaya yardımcı olur?"
> - "Öz-yansıtma kalıplarını mı yoksa doğrudan talimatı mı ne zaman kullanmalıyım?"

**Yüksek Heves (Derin & Kapsamlı)** - Kapsamlı analiz istediğiniz karmaşık problemler için. Model derinlemesine keşif yapar ve detaylı muhakeme gösterir. Sistem tasarımı, mimari kararlar veya karmaşık araştırmalar için idealdir.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Görev Yürütme (Adım Adım İlerleme)** - Çok adımlı iş akışları için. Model baştan bir plan verir, her adımı çalışırken anlatır, sonra özet sunar. Geçişler, uygulamalar veya çok aşamalı süreçlerde kullanın.

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

Düşünce Zinciri istemleme, modelden muhakeme sürecini açıkça göstermesini ister ve karmaşık görevlerde doğruluğu artırır. Adım adım parçalama hem insan hem yapay zekanın mantığı anlamasını kolaylaştırır.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** Bu kalıp hakkında sorun:
> - "Uzun süren işlemler için görev yürütme kalıbını nasıl uyarlardım?"
> - "Üretim uygulamalarında araç girişleri yapılandırmanın en iyi uygulamaları nelerdir?"
> - "UI'da ara ilerleme güncellemelerini nasıl yakalar ve gösteririm?"

<img src="../../../translated_images/tr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Görev Yürütme Kalıbı" width="800"/>

*Planla → Uygula → Özetle iş akışı çok adımlı görevler için*

**Kendi Kendini Değerlendiren Kod** - Üretime hazır kod oluşturmak için. Model üretim standartlarına uygun ve doğru hata yönetmeliği ile kod üretir. Yeni özellikler veya servisler geliştirirken kullanın.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/tr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Kendi Kendini Değerlendirme Döngüsü" width="800"/>

*İteratif iyileştirme döngüsü - üret, değerlendir, sorunları belirle, geliştir, tekrarla*

**Yapılandırılmış Analiz** - Tutarlı değerlendirme için. Model kodu belirlenmiş bir çerçeve kullanarak inceler (doğruluk, uygulamalar, performans, güvenlik, sürdürülebilirlik). Kod incelemeleri veya kalite değerlendirmeleri için kullanışlıdır.

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
> - "Farklı türde kod incelemeleri için analiz çerçevesini nasıl özelleştiririm?"
> - "Yapılandırılmış çıktıyı programlı olarak ayrıştırıp kullanmanın en iyi yolu nedir?"
> - "Farklı inceleme oturumlarında tutarlı şiddet düzeylerini nasıl sağlarım?"

<img src="../../../translated_images/tr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Yapılandırılmış Analiz Kalıbı" width="800"/>

*Şiddet seviyeleri ile tutarlı kod incelemeleri için çerçeve*

**Çok Tur Konuşma** - Bağlam gerektiren konuşmalar için. Model önceki mesajları hatırlar ve üzerine inşa eder. Etkileşimli yardım oturumları veya karmaşık soru-cevaplar için uygun.

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

*Konuşma bağlamı birden çok tur boyunca birikir ta ki token sınırına ulaşana kadar*

**Adım Adım Muhakeme** - Görünür mantık gerektiren problemler için. Model her adım için açık muhakeme gösterir. Matematik problemleri, mantık bulmacaları veya düşünce sürecini anlamak istediğiniz zaman kullanın.

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

*Açık mantıksal adımlara problem parçalama*

**Kısıtlanmış Çıktı** - Belirli format gereksinimleri olan yanıtlar için. Model format ve uzunluk kurallarına katı şekilde uyar. Özetler veya hassas çıktı yapısı gerektiğinde kullanın.

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

<img src="../../../translated_images/tr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Kısıtlanmış Çıktı Kalıbı" width="800"/>

*Özel format, uzunluk ve yapı gereksinimlerinin zorunlu kılınması*

## Mevcut Azure Kaynaklarını Kullanma

**Dağıtımı doğrulayın:**

Ana dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Modül 01'den `./start-all.sh` ile tüm uygulamaları zaten başlattıysanız, bu modül zaten 8083 portunda çalışıyordur. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8083 adresine gidebilirsiniz.
**Seçenek 1: Spring Boot Dashboard Kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlayan Spring Boot Dashboard uzantısını içerir. Bunu VS Code'un sol tarafındaki Aktivite Çubuğunda bulabilirsiniz (Spring Boot simgesine bakın).

Spring Boot Dashboard'dan şunları yapabilirsiniz:
- Çalışma alanındaki tüm mevcut Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıkla başlatabilir/durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

Bu modülü başlatmak için "prompt-engineering" yanındaki oynat düğmesine tıklayın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Shell scriptleri kullanarak**

Tüm web uygulamalarını (modüller 01-04) başlatmak için:

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

Ya da sadece bu modülü başlatmak için:

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

Her iki script de kök dizindeki `.env` dosyasından ortam değişkenlerini otomatik olarak yükler ve JAR dosyaları yoksa oluşturur.

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
.\stop.ps1  # Bu modül yalnızca
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```

## Uygulama Ekran Görüntüleri

<img src="../../../translated_images/tr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Ana Sayfa" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Tüm 8 prompt mühendisliği desenini ve bunların özellikleri ile kullanım durumlarını gösteren ana gösterge tablosu*

## Desenleri Keşfetmek

Web arayüzü size farklı yönlendirme stratejileriyle denemeler yapma imkanı sunar. Her desen farklı problemleri çözer - her yaklaşımın ne zaman parladığını görmek için deneyin.

> **Not: Akış vs Akışsız** — Her desen sayfasında iki düğme vardır: **🔴 Canlı Yanıt (Stream Response)** ve bir **Akışsız** seçeneği. Akış, model tarafından oluşturulan tokenları gerçek zamanlı göstermek için Server-Sent Events (SSE) kullanır, böylece ilerlemeyi hemen görürsünüz. Akışsız seçenek ise tüm yanıt tamamlanana kadar bekler. Derin düşünce gerektiren promptlarda (ör. Yüksek Heves, Öz-Değerlendirmeli Kod) akışsız çağrı çok uzun sürebilir — bazen dakikalarca — görünür geri bildirim olmadan. Kompleks promptları deneylerken akış seçeneğini kullanın, böylece modelin çalışma sürecini görebilir ve isteğin zaman aşımına uğradığı izlenimini önlersiniz.
>
> **Not: Tarayıcı Gereksinimi** — Akış özelliği, Fetch Streams API (`response.body.getReader()`) kullanır ve tam bir tarayıcı (Chrome, Edge, Firefox, Safari) gerektirir. VS Code'un yerleşik Simple Browser'ında çalışmaz, çünkü onun webview bileşeni ReadableStream API desteği sunmaz. Simple Browser kullanıyorsanız, akışsız düğmeler normal çalışır; sadece akış düğmeleri etkilenir. Tam deneyim için `http://localhost:8083` adresini harici bir tarayıcıda açın.

### Düşük vs Yüksek Heves

"Düşük Heves" kullanarak basit bir soru sorun: "200'ün yüzde 15'i nedir?" Anında, doğrudan bir yanıt alırsınız. Şimdi "Yoğun trafik alan bir API için önbellekleme stratejisi tasarla" gibi karmaşık bir soruyu Yüksek Heves ile sorun. **🔴 Canlı Yanıt (Stream Response)** düğmesine tıklayın ve modelin ayrıntılı akıl yürütmesini token token izleyin. Aynı model, aynı soru yapısı — ancak prompt ne kadar derin düşünmesi gerektiğini söyler.

### Görev Yürütme (Araç Ön Açıklamaları)

Çok adımlı iş akışları, önceden planlama ve ilerleme anlatımı ile fayda sağlar. Model ne yapacağını özetler, her adımı açıklar, sonra sonuçları özetler.

### Öz-Değerlendirmeli Kod

"Bir e-posta doğrulama servisi oluştur" deneyin. Sadece kod üretip durmak yerine model üretir, kalite kriterlerine göre değerlendirir, zayıf noktaları tespit eder ve iyileştirir. Kod üretim standartlarına kadar iterasyonları göreceksiniz.

### Yapılandırılmış Analiz

Kod gözden geçirmeleri, tutarlı değerlendirme çerçeveleri gerektirir. Model kodu sabit kategoriler (doğruluk, uygulama, performans, güvenlik) ve şiddet seviyeleri kullanarak analiz eder.

### Çok Turlu Sohbet

"Spring Boot nedir?" sorun, ardından hemen "Örnek göster" deyin. Model ilk soruyu hatırlar ve size özel bir Spring Boot örneği verir. Bellek olmasaydı, ikinci soru çok belirsiz olurdu.

### Adım Adım Akıl Yürütme

Bir matematik problemi seçin ve hem Adım Adım Akıl Yürütme hem de Düşük Heves ile deneyin. Düşük heves sadece cevabı verir — hızlı ama opaktır. Adım adım her hesaplama ve kararı gösterir.

### Kısıtlı Çıktı

Belirli formatlar veya kelime sayıları gerektiğinde, bu desen sıkı uyumu zorunlu kılar. Tam olarak 100 kelimelik madde işaretli bir özet üretmeyi deneyin.

## Gerçekte Ne Öğreniyorsunuz

**Akıl Yürütme Çabası Her Şeyi Değiştirir**

GPT-5.2, hesaplama çabasını promptlarınız yoluyla kontrol etmenizi sağlar. Düşük çaba hızlı yanıtlar ve sınırlı keşif demektir. Yüksek çaba ise modelin derin düşünmesi için zaman ayırır. Görev karmaşıklığına göre çabayı eşleştirmeyi öğreniyorsunuz — basit sorularda zaman kaybetmeyin, karmaşık kararları ise aceleye getirmeyin.

**Yapı Davranışı Yönlendirir**

Promptlardaki XML etiketlerini fark ettiniz mi? Bunlar süs değil. Modeller, serbest metinden daha güvenilir şekilde yapılandırılmış talimatları takip eder. Çok adımlı süreçler veya karmaşık mantık gerekiyorsa, yapı modelin nerede olduğunu ve sıradakini takip etmesini sağlar.

<img src="../../../translated_images/tr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Yapısı" width="800"/>

*Bölümleri net ve XML tarzı organizasyonla iyi yapılandırılmış bir promptun anatomisi*

**Kendi Kendini Değerlendirme ile Kalite**

Öz-değerlendirmeli desenler, kalite kriterlerini açıkça belirtir. Modelin "doğru yapmasını" umutlamak yerine, ona tam olarak "doğru"nun ne olduğunu söylersiniz: doğru mantık, hata yönetimi, performans, güvenlik. Model kendi çıktısını değerlendirebilir ve iyileştirebilir. Bu, kod üretimini bir piyangodan bir sürece dönüştürür.

**Bağlam Sınırlıdır**

Çok turlu sohbetler, her isteğe mesaj geçmişini dahil ederek çalışır. Ancak bir sınır vardır — her modelin maksimum token sayısı. Sohbetler büyüdükçe, ilgili bağlamı sınırı aşmadan tutmak için stratejilere ihtiyacınız olur. Bu modül hafızanın nasıl çalıştığını gösterir; ileride ne zaman özetleneceğini, unutulacağını ve geri çağrılacağını öğreneceksiniz.

## Sonraki Adımlar

**Sonraki Modül:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Gezinme:** [← Önceki: Modül 01 - Giriş](../01-introduction/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, [Co-op Translator](https://github.com/Azure/co-op-translator) adlı yapay zeka çeviri hizmeti kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlamalar veya yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->