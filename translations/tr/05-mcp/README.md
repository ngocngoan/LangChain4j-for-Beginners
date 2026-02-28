# Modül 05: Model Bağlam Protokolü (MCP)

## İçindekiler

- [Neler Öğreneceksiniz](../../../05-mcp)
- [MCP Nedir?](../../../05-mcp)
- [MCP Nasıl Çalışır](../../../05-mcp)
- [Agentik Modül](../../../05-mcp)
- [Örnekleri Çalıştırma](../../../05-mcp)
  - [Gereksinimler](../../../05-mcp)
- [Hızlı Başlangıç](../../../05-mcp)
  - [Dosya İşlemleri (Stdio)](../../../05-mcp)
  - [Denetleyici Ajan](../../../05-mcp)
    - [Demo Çalıştırma](../../../05-mcp)
    - [Denetleyici Nasıl Çalışır](../../../05-mcp)
    - [Yanıt Stratejileri](../../../05-mcp)
    - [Çıktıyı Anlama](../../../05-mcp)
    - [Agentik Modül Özelliklerinin Açıklaması](../../../05-mcp)
- [Ana Kavramlar](../../../05-mcp)
- [Tebrikler!](../../../05-mcp)
  - [Sonraki Adımlar?](../../../05-mcp)

## Neler Öğreneceksiniz

Konuşma bazlı yapay zeka oluşturdunuz, istemleri ustalıkla kullandınız, yanıtları belgelere dayandırdınız ve araçlara sahip ajanlar yarattınız. Ancak tüm bu araçlar sizin spesifik uygulamanız için özel olarak tasarlanmıştı. Ya AI'nıza herkesin oluşturup paylaşabileceği standart bir araç ekosistemi erişimi verebilseydiniz? Bu modülde, Model Bağlam Protokolü (MCP) ve LangChain4j'nin agentik modülü ile tam olarak bunu nasıl yapacağınızı öğreneceksiniz. İlk olarak basit bir MCP dosya okuyucu gösteriyoruz, ardından bunun Denetleyici Ajan desenini kullanarak gelişmiş agentik iş akışlarına nasıl kolayca entegre olduğunu ortaya koyuyoruz.

## MCP Nedir?

Model Bağlam Protokolü (MCP) tam olarak bunu sağlar - yapay zeka uygulamalarının dış araçları keşfetmesi ve kullanması için standart bir yol. Her veri kaynağı veya hizmet için özel entegrasyonlar yazmak yerine, yeteneklerini tutarlı bir formatta açığa çıkaran MCP sunucularına bağlanırsınız. Yapay zeka ajanınız da bu araçları otomatik olarak keşfedip kullanabilir.

<img src="../../../translated_images/tr/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP Öncesi: Karmaşık nokta-noktaya entegrasyonlar. MCP Sonrası: Tek protokol, sonsuz imkanlar.*

MCP yapay zeka geliştirmedeki temel bir sorunu çözer: her entegrasyon özeldir. GitHub'a erişmek mi istiyorsunuz? Özel kod. Dosya okumak mı? Özel kod. Veritabanı sorgulamak mı? Özel kod. Ve bu entegrasyonların hiçbiri diğer yapay zeka uygulamalarıyla çalışmaz.

MCP bunu standart hale getirir. Bir MCP sunucusu araçları açık açıklamalar ve şemalarla sunar. Herhangi bir MCP istemcisi bağlanabilir, mevcut araçları keşfedebilir ve onları kullanabilir. Bir kere yap, her yerde kullan.

<img src="../../../translated_images/tr/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Bağlam Protokolü mimarisi - standartlaştırılmış araç keşfi ve yürütme*

## MCP Nasıl Çalışır

<img src="../../../translated_images/tr/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP'nin iç işleyişi — istemciler araçları keşfeder, JSON-RPC mesajları alışveriş eder ve bir taşıma katmanı üzerinden işlemleri yürütür.*

**Sunucu-İstemci Mimarisi**

MCP, istemci-sunucu modeli kullanır. Sunucular araçları sağlar - dosya okuma, veritabanı sorgulama, API çağrıları. İstemciler (yapay zeka uygulamanız) sunuculara bağlanıp araçları kullanır.

LangChain4j ile MCP kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Araç Keşfi**

İstemciniz bir MCP sunucusuna bağlandığında "Hangi araçlara sahipsin?" diye sorar. Sunucu, açıklamalar ve parametre şemaları ile mevcut araçların listesini döner. Yapay zeka ajanınız da kullanıcı taleplerine göre hangi araçları kullanacağına karar verir.

<img src="../../../translated_images/tr/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Yapay zeka başlangıçta mevcut araçları keşfeder — hangi yeteneklerin kullanılabilir olduğunu bilir ve hangi araçların kullanılacağına karar verir.*

**Taşıma Mekanizmaları**

MCP farklı taşıma mekanizmalarını destekler. Bu modül, yerel süreçler için Stdio taşımayı gösterir:

<img src="../../../translated_images/tr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP taşıma mekanizmaları: Uzak sunucular için HTTP, yerel süreçler için Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Yerel süreçler için. Uygulamanız bir alt süreç olarak sunucu başlatır ve standart giriş/çıkış vasıtasıyla iletişim kurar. Dosya sistemi erişimi veya komut satırı araçları için faydalıdır.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

<img src="../../../translated_images/tr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio taşıma işlemi — uygulamanız MCP sunucusunu alt süreç olarak başlatır ve stdin/stdout boruları aracılığıyla iletişim kurar.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dosyasını açın ve sorun:
> - "Stdio taşımacılığı nasıl çalışır ve HTTP'ye karşı ne zaman kullanmalıyım?"
> - "LangChain4j, başlatılan MCP sunucu süreçlerinin yaşam döngüsünü nasıl yönetir?"
> - "AI'ya dosya sistemine erişim verme güvenlik riskleri nelerdir?"

## Agentik Modül

MCP standart araçlar sağlarken, LangChain4j'nin **agentik modülü** bu araçları yöneten ajanları bildirimsel olarak oluşturmanın yolunu sunar. `@Agent` açıklaması ve `AgenticServices`, ajan davranışlarını çevresel kod yerine arayüzler üzerinden tanımlamanızı sağlar.

Bu modülde **Denetleyici Ajan** desenini keşfedeceksiniz — bir "denetleyici" ajanın, kullanıcı taleplerine göre hangi alt ajanların çağrılacağına dinamik olarak karar verdiği gelişmiş bir agentik yapay zeka yaklaşımı. Bu iki kavramı, bir alt ajanımıza MCP destekli dosya erişimi yetenekleri vererek birleştireceğiz.

Agentik modülü kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Deneysel:** `langchain4j-agentic` modülü **deneysel** olup değişime tabi olabilir. Yapay zeka asistanları oluşturmanın kararlı yolu hala custom araçlar ile `langchain4j-core` (Modül 04).

## Örnekleri Çalıştırma

### Gereksinimler

- Java 21+, Maven 3.9+
- Node.js 16+ ve npm (MCP sunucuları için)
- `.env` dosyasına çevresel değişkenlerin yapılandırılması (kök dizinden):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (Modüller 01-04 ile aynıdır)

> **Not:** Çevresel değişkenlerinizi henüz ayarlamadıysanız, talimatlar için [Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) sayfasına bakabilir veya `.env.example` dosyasını kök dizinde `.env` olarak kopyalayıp kendi değerlerinizi doldurabilirsiniz.

## Hızlı Başlangıç

**VS Code Kullanarak:** Explorer'da herhangi bir demo dosyasına sağ tıklayıp **"Run Java"** seçebilir veya Run ve Debug panelindeki başlatma yapılandırmalarını kullanabilirsiniz (önce `.env` dosyanızı yapılandırdığınızdan emin olun).

**Maven Kullanarak:** Alternatif olarak, aşağıdaki örneklerle komut satırından çalıştırabilirsiniz.

### Dosya İşlemleri (Stdio)

Yerel alt süreç bazlı araçları gösterir.

**✅ Ön koşul gerekmez** - MCP sunucusu otomatik olarak başlatılır.

**Başlatma Betikleri Kullanımı (Önerilir):**

Başlatma betikleri kök `.env` dosyasından çevresel değişkenleri otomatik yükler:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**VS Code Kullanımı:** `StdioTransportDemo.java` üzerinde sağ tıklayın ve **"Run Java"** seçin (`.env` dosyanız yapılandırılmış olmalı).

Uygulama otomatik olarak bir dosya sistemi MCP sunucusu oluşturur ve yerel bir dosyayı okur. Alt süreç yönetiminin sizin için nasıl üstlenildiğine dikkat edin.

**Beklenen çıktı:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Denetleyici Ajan

**Denetleyici Ajan deseni**, agentik yapay zekanın **esnek** bir biçimidir. Bir Denetleyici, kullanıcı talebine göre hangi ajanların çağrılacağına otonom olarak karar veren bir LLM kullanır. Bir sonraki örnekte, MCP destekli dosya erişimini LLM ajanı ile birleştirerek denetimli dosya okuma → rapor oluşturma iş akışı yaratıyoruz.

Demoda, `FileAgent` MCP dosya sistemi araçlarını kullanarak dosya okur ve `ReportAgent` yürütücü özeti (1 cümle), 3 ana nokta ve önerilerle yapılandırılmış rapor üretir. Denetleyici bu akışı otomatik olarak yönetir:

<img src="../../../translated_images/tr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Denetleyici, LLM'sini kullanarak hangi ajanların ve hangi sırayla çağrılacağına karar verir — sabit kodlanmış yönlendirmeye gerek yoktur.*

Dosyadan rapora iş akışımız somut olarak şu şekildedir:

<img src="../../../translated_images/tr/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent MCP araçları aracılığıyla dosyayı okur, ardından ReportAgent ham içeriği yapılandırılmış rapora dönüştürür.*

Her ajan çıktısını **Agentik Alan**a (paylaşılan bellek) kaydeder, böylece sonraki ajanlar önceki sonuçlara erişebilir. Bu, MCP araçlarının agentik iş akışlarına nasıl sorunsuz entegre olduğunu gösterir — Denetleyici dosyaların *nasıl* okunduğunu bilmek zorunda değildir, sadece `FileAgent`'ın bunu yapabildiğini bilir.

#### Demo Çalıştırma

Başlatma betikleri kök `.env` dosyasından çevresel değişkenleri otomatik yükler:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**VS Code Kullanımı:** `SupervisorAgentDemo.java` dosyasına sağ tıklayın ve **"Run Java"** seçin (`.env` dosyanız yapılandırılmış olmalı).

#### Denetleyici Nasıl Çalışır

```java
// Adım 1: FileAgent, dosyaları MCP araçları kullanarak okur
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Dosya işlemleri için MCP araçlarına sahiptir
        .build();

// Adım 2: ReportAgent yapılandırılmış raporlar oluşturur
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor, dosya → rapor iş akışını düzenler
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Nihai raporu döndür
        .build();

// Supervisor, isteğe bağlı olarak hangi ajanların çağrılacağına karar verir
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Yanıt Stratejileri

Bir `SupervisorAgent` yapılandırıldığında, alt ajanların görevlerini tamamladıktan sonra kullanıcıya nasıl nihai yanıt vereceği seçilir.

<img src="../../../translated_images/tr/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Denetleyicinin nihai yanıtı oluşturma için üç stratejisi — son ajanın çıktısını mı, sentezlenmiş özeti mi yoksa en yüksek puanlı seçeneği mi almak istediğinize bağlı olarak tercih edin.*

Kullanılabilir stratejiler:

| Strateji | Açıklama |
|----------|-------------|
| **LAST** | Denetleyici, çağrılan son alt ajan veya aracın çıktısını döner. Bu, iş akışındaki son ajanın tüm nihai yanıtı üretmek üzere özel tasarlandığı durumlarda kullanışlıdır (örneğin, araştırma hattında "Özet Ajan"). |
| **SUMMARY** | Denetleyici, kendi dahili Dil Modelini (LLM) kullanarak tüm etkileşim ve alt ajan çıktılarını sentezleyip özetler ve bu özeti nihai yanıt olarak döner. Kullanıcıya temiz, birleşik bir yanıt sağlar. |
| **SCORED** | Sistem, dahili bir LLM kullanarak hem LAST yanıtını hem etkileşimin SUMMARY’sini orijinal kullanıcı isteğiyle karşılaştırarak puanlar, en yüksek puanı alan çıktıyı döner. |

Tam uygulama için [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasına bakınız.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasını açın ve sorun:
> - "Denetleyici hangi ajanları çağıracağına nasıl karar verir?"
> - "Denetleyici ile Ardışık iş akışı desenleri arasındaki fark nedir?"
> - "Denetleyicinin planlama davranışını nasıl özelleştirebilirim?"

#### Çıktıyı Anlama

Demo çalıştırıldığında, Denetleyicinin çoklu ajanları nasıl organize ettiğinin yapılandırılmış bir açıklamasını görürsünüz. Her bölümün anlamı şu şekildedir:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**Başlık**, iş akışı kavramını tanıtır: dosya okumadan rapor oluşturma odaklı bir hat.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**İş Akışı Diyagramı** ajanlar arasındaki veri akışını gösterir. Her ajanın rolü belirgindir:
- **FileAgent** MCP araçlarını kullanarak dosyaları okur ve ham içeriği `fileContent` içine kaydeder
- **ReportAgent** bu içeriği alır ve `report` içinde yapılandırılmış rapor üretir

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Kullanıcı Talebi**, görevi gösterir. Denetleyici bunu çözümler ve FileAgent → ReportAgent çağrısına karar verir.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Denetleyici Yönetimi**, 2 adımlı iş akışını gösterir:
1. **FileAgent** MCP aracılığıyla dosyayı okur ve içeriği kaydeder
2. **ReportAgent** içeriği alır ve yapılandırılmış rapor oluşturur

Denetleyici bu kararları **otonom** olarak kullanıcı isteğine göre verdi.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Agentik Modül Özelliklerinin Açıklaması

Örnek, agentik modülün gelişmiş birkaç özelliğini gösterir. Agentik Alan ve Agent Dinleyicilerine yakından bakalım.

**Agentik Alan**, ajanların `@Agent(outputKey="...")` ile sonuçlarını kaydettiği paylaşılan hafızadır. Bu, şunları sağlar:
- Daha sonraki ajanların önceki ajanların çıktılarına erişimi
- Denetleyicinin nihai yanıtı sentezleyebilmesi
- Her ajanın ne ürettiğini inceleyebilmeniz

<img src="../../../translated_images/tr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentik Alan paylaşılan hafıza görevi görür — FileAgent `fileContent` yazar, ReportAgent okur ve `report` yazar, kodunuz nihai sonucu okur.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent'ten ham dosya verisi
String report = scope.readState("report");            // ReportAgent'ten yapılandırılmış rapor
```

**Agent Dinleyicileri**, ajan yürütmesini izlemenize ve hata ayıklamanıza olanak tanır. Demoda gördüğünüz adım adım çıktı, her ajan çağrısına bağlanan bir AgentListener’dan gelir:
- **beforeAgentInvocation** - Denetleyici bir ajan seçtiğinde çağrılır, hangi ajanın seçildiğini ve nedenini görmenizi sağlar
- **afterAgentInvocation** - Bir ajan tamamlandığında çağrılır, sonucunu gösterir
- **inheritedBySubagents** - True olduğunda, dinleyici hiyerarşideki tüm ajanları izler

<img src="../../../translated_images/tr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Ajan Dinleyicileri yürütme yaşam döngüsüne bağlanır — ajanların ne zaman başladığını, tamamlandığını veya hata yaşadığını izler.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Tüm alt ajanlara yayılım sağla
    }
};
```

Denetleyici deseninin ötesinde, `langchain4j-agentic` modülü birkaç güçlü iş akışı deseni ve özellik sağlar:

<img src="../../../translated_images/tr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Ajanları orkestre etmek için beş iş akışı deseni — basit ardışık boru hatlarından insan müdahaleli onay iş akışlarına kadar.*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | Ajanları sırayla çalıştır, çıktı bir sonraki aşamaya akar | Boru hatları: araştırma → analiz → raporlama |
| **Parallel** | Ajanları eşzamanlı çalıştır | Bağımsız görevler: hava durumu + haberler + hisse senetleri |
| **Loop** | Şart sağlanana kadar yinele | Kalite puanlaması: puan ≥ 0.8 olana kadar iyileştir |
| **Conditional** | Koşullara göre yönlendir | Sınıflandır → uzman ajana yönlendir |
| **Human-in-the-Loop** | İnsan kontrol noktaları ekle | Onay iş akışları, içerik incelemesi |

## Temel Kavramlar

MCP ve agentic modülünü uygulamada keşfettikten sonra, her yaklaşımı ne zaman kullanacağınıza bakalım.

<img src="../../../translated_images/tr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP evrensel bir protokol ekosistemi yaratır — herhangi bir MCP uyumlu sunucu, herhangi bir MCP uyumlu istemci ile çalışır, uygulamalar arasında araç paylaşımını sağlar.*

**MCP**, mevcut araç ekosistemlerinden yararlanmak, birden fazla uygulamanın paylaşabileceği araçlar oluşturmak, standart protokollerle üçüncü taraf hizmetleri entegre etmek veya araç uygulamalarını kodu değiştirmeden değiştirmek istediğinizde ideal.

**Agentic Modül**, `@Agent` açıklamaları ile deklaratif ajan tanımlamaları istiyorsanız, iş akışı orkestrasyonu (ardışık, döngü, paralel) gerekiyorsa, zorlayıcı kod yerine arayüz tabanlı ajan tasarımını tercih ediyorsanız veya `outputKey` aracılığıyla çıktıları paylaşan birden çok ajanı birleştiriyorsanız en iyi şekilde çalışır.

**Denetleyici Ajan deseni**, iş akışı önceden tahmin edilemediğinde ve karar vermesi için LLM kullanmak istediğinizde, dinamik orkestrasyon gereken birden fazla uzman ajanınız olduğunda, farklı yeteneklere yönlendiren konuşma sistemleri oluştururken veya en esnek, uyarlanabilir ajan davranışını istediğinizde parıldar.

<img src="../../../translated_images/tr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Uygulamaya özel mantık için tam tip güvenliği ile özel @Tool yöntemlerini, uygulamalar arası çalışan standart entegrasyonlar için MCP araçlarını ne zaman kullanmalı.*

## Tebrikler!

<img src="../../../translated_images/tr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Tüm beş modül boyunca temel sohbetten MCP destekli agentic sistemlere kadar öğrenme yolculuğunuz.*

LangChain4j for Beginners kursunu tamamladınız. Şunları öğrendiniz:

- Bellekli konuşma yapay zekası nasıl kurulur (Modül 01)
- Farklı görevler için istem mühendisliği desenleri (Modül 02)
- Yanıtları belgelerinizle temellendirme (RAG) (Modül 03)
- Özel araçlarla temel yapay zeka ajanları yaratma (Modül 04)
- LangChain4j MCP ve Agentic modülleri ile standart araç entegrasyonu (Modül 05)

### Sırada Ne Var?

Modülleri tamamladıktan sonra, LangChain4j test konseptlerini uygulamalı görmek için [Testing Guide](../docs/TESTING.md) bölümünü keşfedin.

**Resmi Kaynaklar:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Kapsamlı rehberler ve API referansı
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kaynak kod ve örnekler
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Çeşitli kullanım durumları için adım adım eğitimler

Bu kursu tamamladığınız için teşekkürler!

---

**Navigasyon:** [← Önceki: Modül 04 - Araçlar](../04-tools/README.md) | [Ana Sayfaya Dön](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için özen gösterilse de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Önemli bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanılması sonucu ortaya çıkabilecek yanlış anlamalar veya yanlış yorumlamalardan sorumlu olmadığımızı bildirmek isteriz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->