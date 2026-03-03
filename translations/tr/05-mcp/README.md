# Modül 05: Model Context Protocol (MCP)

## İçindekiler

- [Neler Öğreneceksiniz](../../../05-mcp)
- [MCP Nedir?](../../../05-mcp)
- [MCP Nasıl Çalışır](../../../05-mcp)
- [Agentic Modül](../../../05-mcp)
- [Örnekleri Çalıştırmak](../../../05-mcp)
  - [Ön Koşullar](../../../05-mcp)
- [Hızlı Başlangıç](../../../05-mcp)
  - [Dosya İşlemleri (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Demoyu Çalıştırmak](../../../05-mcp)
    - [Supervisor Nasıl Çalışır](../../../05-mcp)
    - [Yanıt Stratejileri](../../../05-mcp)
    - [Çıktının Anlaşılması](../../../05-mcp)
    - [Agentic Modül Özelliklerinin Açıklaması](../../../05-mcp)
- [Anahtar Kavramlar](../../../05-mcp)
- [Tebrikler!](../../../05-mcp)
  - [Sırada Ne Var?](../../../05-mcp)

## Neler Öğreneceksiniz

Konuşma tabanlı yapay zeka inşa ettiniz, promptlarda ustalaştınız, cevapları belgelere dayandırdınız ve araçlara sahip ajanlar oluşturdunuz. Ancak tüm bu araçlar sizin özel uygulamanız için özel olarak inşa edilmişti. Ya yapay zekanıza herkesin yaratıp paylaşabileceği standart bir araç ekosistemi erişimi verebilseydiniz? Bu modülde, Model Context Protocol (MCP) ve LangChain4j'nin agentic modülü ile tam olarak bunu nasıl yapacağınızı öğreneceksiniz. İlk olarak basit bir MCP dosya okuyucu gösteriyoruz, ardından bunun Supervisor Agent modeli kullanarak gelişmiş agentic iş akışlarına nasıl kolayca entegre olduğuna değiniyoruz.

## MCP Nedir?

Model Context Protocol (MCP) tam da bunu sağlar — yapay zeka uygulamalarının dış araçları keşfetmesi ve kullanması için standart bir yol. Her veri kaynağı veya servis için özel entegrasyon yazmak yerine, yeteneklerini tutarlı bir formatta açan MCP sunucularına bağlanırsınız. Yapay zeka ajanınız bu araçları otomatik olarak keşfedip kullanabilir.

Aşağıdaki şema farkı gösteriyor — MCP olmadan her entegrasyon özel ve nokta-noktadır; MCP ile tek bir protokol uygulamanızı herhangi bir araca bağlar:

<img src="../../../translated_images/tr/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP Öncesi: Karmaşık nokta-nokta entegrasyonlar. MCP Sonrası: Tek protokol, sonsuz olasılıklar.*

MCP yapay zeka geliştirmedeki temel problemi çözüyor: her entegrasyon özel. GitHub’a erişmek mi istiyorsunuz? Özel kod. Dosya okumak mı? Özel kod. Veritabanı sorgulamak mı? Özel kod. Üstelik bu entegrasyonların hiçbiri diğer yapay zeka uygulamalarıyla çalışmıyor.

MCP bunu standartlaştırır. Bir MCP sunucusu araçları net açıklamalar ve şemalarla açar. Her MCP istemcisi bağlanabilir, kullanılabilir araçları keşfedebilir ve kullanabilir. Bir kez kur, her yerde kullan.

Aşağıdaki şema bu mimariyi gösteriyor — tek bir MCP istemcisi (yapay zeka uygulamanız) birçok MCP sunucusuna bağlanır ve her biri standart protokol üzerinden kendi araç setini açar:

<img src="../../../translated_images/tr/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol mimarisi - standartlaştırılmış araç keşfi ve yürütme*

## MCP Nasıl Çalışır

MCP, altında katmanlı bir mimari kullanır. Java uygulamanız (MCP istemcisi) mevcut araçları keşfeder, bir taşıma katmanı (Stdio veya HTTP) üzerinden JSON-RPC istekleri gönderir, MCP sunucusu operasyonları yürütür ve sonuçları döner. Aşağıdaki şema bu protokolün her katmanını ayrıntılı olarak gösterir:

<img src="../../../translated_images/tr/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP altında nasıl çalışır — istemciler araçları keşfeder, JSON-RPC mesajları alışverişi yapar ve taşıma katmanı üzerinden operasyonları yürütür.*

**Sunucu-İstemci Mimarisi**

MCP, istemci-sunucu modelini kullanır. Sunucular araç sağlar — dosya okuma, veritabanı sorgulama, API çağrıları. İstemciler (yapay zeka uygulamanız) sunuculara bağlanır ve araçlarını kullanır.

LangChain4j ile MCP kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Araç Keşfi**

İstemciniz bir MCP sunucusuna bağlandığında, “Hangi araçlara sahipsin?” diye sorar. Sunucu mevcut araçların listesini, her biri açıklama ve parametre şemaları ile cevaplar. Yapay zeka ajanınız kullanıcı isteklerine göre hangi araçların kullanılacağına karar verir. Aşağıdaki şema bu el sıkışmayı gösterir — istemci `tools/list` isteği gönderir, sunucu açıklamalar ve parametre şemalarıyla birlikte mevcut araçları döner:

<img src="../../../translated_images/tr/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*Yapay zeka başlangıçta mevcut araçları keşfeder — artık hangi yeteneklerin erişilebilir olduğunu bilir ve hangilerinin kullanılacağına karar verebilir.*

**Taşıma Mekanizmaları**

MCP çeşitli taşıma mekanizmalarını destekler. İki seçenek vardır: Stdio (yerel alt süreç iletişimi için) ve Streamable HTTP (uzak sunucular için). Bu modülde Stdio taşıma gösterilmektedir:

<img src="../../../translated_images/tr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP taşıma mekanizmaları: Uzak sunucular için HTTP, yerel işlemler için Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Yerel işlemler için. Uygulamanız sunucuyu bir alt süreç olarak başlatır ve standart giriş/çıkış üzerinden iletişim kurar. Dosya sistemi erişimi veya komut satırı araçları için kullanışlıdır.

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

`@modelcontextprotocol/server-filesystem` sunucu aşağıdaki araçları sunar, tümü sizin belirttiğiniz dizinlerle sınırlandırılmıştır:

| Araç | Açıklama |
|------|----------|
| `read_file` | Tek bir dosyanın içeriğini okur |
| `read_multiple_files` | Bir çağrıyla birden fazla dosya okur |
| `write_file` | Dosya oluşturur veya üzerine yazar |
| `edit_file` | Hedeflenmiş bul-ve-değiştir düzenlemeleri yapar |
| `list_directory` | Bir yoldaki dosya ve dizinleri listeler |
| `search_files` | Desene uyan dosyaları özyinelemeli arar |
| `get_file_info` | Dosya meta verisini alır (boyut, zaman damgaları, izinler) |
| `create_directory` | Dizin oluşturur (ebeveyn dizinler dahil) |
| `move_file` | Dosya veya dizini taşır ya da yeniden adlandırır |

Aşağıdaki şema Stdio taşımanın çalışma zamanında nasıl işlediğini gösterir — Java uygulamanız MCP sunucusunu alt süreç olarak başlatır ve onlar stdin/stdout boruları üzerinden iletişim kurar, ağ veya HTTP yoktur:

<img src="../../../translated_images/tr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio taşımacılığı uygulamada — uygulamanız MCP sunucusunu alt süreç olarak başlatır ve stdin/stdout boruları ile iletişim kurar.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dosyasını açın ve sorun:
> - "Stdio taşıma nasıl çalışır ve HTTP’ye göre ne zaman kullanmalıyım?"
> - "LangChain4j MCP sunucu süreçlerinin yaşam döngüsünü nasıl yönetir?"
> - "Yapay zekaya dosya sistemi erişimi vermenin güvenlik sonuçları nelerdir?"

## Agentic Modül

MCP standart araçlar sağlarken, LangChain4j'nin **agentic modülü**, bu araçları düzenleyen ajanlar oluşturmak için bildirimsel bir yol sağlar. `@Agent` açıklaması ve `AgenticServices` ile davranışları arayüzler aracılığıyla tanımlayabilirsiniz, zorunlu kod yazmadan.

Bu modülde, “Supervisor Agent” modelini keşfedeceksiniz — gelişmiş agentic yapay zeka yaklaşımıdır, burada “süpervizör” ajan kullanıcı isteğine göre alt ajanları dinamik olarak seçer. İki konsepti birleştirerek, alt ajanlardan birine MCP destekli dosya erişimi yetenekleri vereceğiz.

Agentic modülü kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Not:** `langchain4j-agentic` modülü farklı bir sürüm özelliği (`langchain4j.mcp.version`) kullanır çünkü çekirdek LangChain4j kütüphanelerinden farklı zamanlarda yayımlanır.

> **⚠️ Deneysel:** `langchain4j-agentic` modülü **deneysel** olup değişikliğe tabidir. AI asistanları oluşturmanın stabil yolu hala özel araçlar ile `langchain4j-core` (Modül 04).

## Örnekleri Çalıştırmak

### Ön Koşullar

- [Modül 04 - Araçlar](../04-tools/README.md) tamamlanmış olmalı (bu modül özel araç kavramlarını temel alır ve MCP araçları ile kıyaslar)
- Root dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01’de `azd up` ile oluşturulur)
- Java 21+, Maven 3.9+
- MCP sunucular için Node.js 16+ ve npm

> **Not:** Henüz ortam değişkenlerinizi oluşturmadıysanız, dağıtım talimatları için [Modül 01 - Giriş](../01-introduction/README.md) sayfasına bakın (`azd up` `.env` dosyasını otomatik yaratır) veya kök dizindeki `.env.example` dosyasını `.env` olarak kopyalayıp kendi bilgilerinizi girin.

## Hızlı Başlangıç

**VS Code kullanıyorsanız:** Keşifçi’den herhangi bir demo dosyasına sağ tıklayın ve **"Run Java"** seçin veya Çalıştır ve Hata Ayıkla panelindeki başlatma yapılandırmalarını kullanın (önce `.env` dosyanızda Azure kimlik bilgilerinin yapılandırıldığından emin olun).

**Maven kullanarak:** Alternatif olarak, komut satırından aşağıdaki örneklerle çalıştırabilirsiniz.

### Dosya İşlemleri (Stdio)

Bu yerel alt süreç tabanlı araçları gösterir.

**✅ Ön koşul gerekmez** — MCP sunucu otomatik başlatılır.

**Başlatma Betikleri ile (Önerilir):**

Başlatma betikleri ortam değişkenlerini kök `.env` dosyasından otomatik yükler:

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

**VS Code Kullanırken:** `StdioTransportDemo.java` dosyasına sağ tıklayın ve **"Run Java"** seçin (`.env` dosyasının yapılandırıldığına emin olun).

Uygulama otomatik olarak dosya sistemi MCP sunucusu başlatır ve yerel bir dosyayı okur. Alt süreç yönetiminin sizin için nasıl halledildiğine dikkat edin.

**Beklenen çıktı:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent modeli**, agentic yapay zekanın **esnek** bir formudur. Supervisor, LLM kullanarak kullanıcının isteğine göre hangi ajanların çağrılacağına otonom karar verir. Sonraki örnekte, MCP destekli dosya erişimini bir LLM ajanı ile birleştirerek gözetimli bir dosya okuma → raporlama iş akışı oluşturacağız.

Demoda, `FileAgent` MCP dosya sistemi araçlarıyla bir dosya okur, `ReportAgent` ise yönetici özeti (1 cümle), 3 ana nokta ve öneriler içeren yapılandırılmış bir rapor üretir. Supervisor bu akışı otomatik yönetir:

<img src="../../../translated_images/tr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor, LLM’ini kullanarak hangi ajanların ve hangi sırayla çağrılacağına karar verir — sabit kodlanmış yönlendirme yok.*

Dosyadan rapora iş akışının somut hali şöyle görünür:

<img src="../../../translated_images/tr/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent MCP araçlarıyla dosyayı okur, ardından ReportAgent ham içeriği yapılandırılmış bir rapora dönüştürür.*

Her ajan çıktısını **Agentic Scope** (paylaşılan bellek) içinde depolar, bu sayede sonraki ajanlar önceki sonuçlara erişebilir. Bu, MCP araçlarının agentic iş akışlarına sorunsuz entegrasyonunu gösterir — Supervisor dosyaların *nasıl* okunduğunu bilmek zorunda değil, sadece `FileAgent`'in yapabildiğini bilmesi yeterli.

#### Demoyu Çalıştırmak

Başlatma betikleri ortam değişkenlerini kök `.env` dosyasından otomatik yükler:

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

**VS Code Kullanırken:** `SupervisorAgentDemo.java` dosyasına sağ tıklayın ve **"Run Java"** seçin (`.env` dosyasının yapılandırıldığına emin olun).

#### Supervisor Nasıl Çalışır

Ajanları oluşturmadan önce, MCP taşıma katmanını bir istemciye bağlamalı ve bunu bir `ToolProvider` olarak sarmalısınız. Bu, MCP sunucusunun araçlarının ajanlarınıza erişilebilir hale gelmesini sağlar:

```java
// Taşıma katmanından bir MCP istemcisi oluştur
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// İstemciyi bir ToolProvider olarak sar — bu MCP araçlarını LangChain4j'ye bağlar
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Artık `mcpToolProvider`'ı MCP araçlarını gerektiren ajanlara enjekte edebilirsiniz:

```java
// Adım 1: FileAgent, MCP araçlarını kullanarak dosyaları okur
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Dosya işlemleri için MCP araçlarına sahiptir
        .build();

// Adım 2: ReportAgent, yapılandırılmış raporlar oluşturur
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor, dosya → rapor iş akışını düzenler
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Nihai raporu döndürür
        .build();

// Supervisor, isteğe göre hangi ajanların çağrılacağına karar verir
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Yanıt Stratejileri

Bir `SupervisorAgent` yapılandırırken, alt ajanların görevleri tamamlandıktan sonra kullanıcıya nasıl nihai yanıt ileteceğini belirtirsiniz. Aşağıdaki şema mevcut üç stratejiyi gösterir — LAST son ajanın çıktısını direkt döner, SUMMARY tüm çıktıları LLM ile sentezler, SCORED ise orijinal isteğe göre daha yüksek puanı alan sonucu seçer:

<img src="../../../translated_images/tr/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor’ın nihai yanıtı nasıl oluşturacağı için üç strateji — son ajanın çıktısını mı, sentezlenmiş bir özeti mi yoksa en yüksek puan alanı mı istediğinize göre seçim yapın.*

Kullanılabilir stratejiler:

| Strateji | Açıklama |
|----------|----------|
| **LAST** | Supervisor, çağrılan son alt ajan veya aracın çıktısını döner. Bu, iş akışındaki son ajan özellikle tamamlayıcı, nihai cevabı üretmek üzere tasarlandığında kullanışlıdır (örneğin araştırma hattında "Özet Ajan"). |
| **SUMMARY** | Supervisor, kendi dahili Dil Modelini (LLM) kullanarak tüm etkileşimin ve alt ajan çıktılarının özetini sentezler ve bunu nihai yanıt olarak döner. Kullanıcıya temiz, derlenmiş bir cevap sağlar. |
| **SCORED** | Sistem dahili bir LLM kullanarak hem LAST yanıtını hem de SUMMARY özetini orijinal kullanıcı isteğine göre puanlar, daha yüksek puan alan çıktıyı döner. |
Tam uygulama için [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasına bakın.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasını açın ve sorun:
> - "Supervisor hangi ajanların çağrılacağına nasıl karar veriyor?"
> - "Supervisor ile Ardışık iş akışı desenleri arasındaki fark nedir?"
> - "Supervisor'un planlama davranışını nasıl özelleştirebilirim?"

#### Çıktıyı Anlamak

Demoyu çalıştırdığınızda, Supervisor'un birden fazla ajanı nasıl organize ettiğine dair yapılandırılmış bir yürüyüş göreceksiniz. Her bölümün ne anlama geldiği aşağıdadır:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Başlık**, iş akışı kavramını tanıtır: dosya okumadan rapor oluşturmaya odaklanmış bir boru hattı.

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
  
**İş Akışı Diyagramı**, ajanlar arasındaki veri akışını gösterir. Her ajanın belirli bir rolü vardır:  
- **FileAgent** MCP araçlarıyla dosyaları okur ve ham içeriği `fileContent` içinde saklar  
- **ReportAgent** bu içeriği tüketir ve `report` içinde yapılandırılmış bir rapor üretir

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Kullanıcı Talebi**, görevi gösterir. Supervisor bunu çözümleyip FileAgent → ReportAgent çağırmaya karar verir.

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
  
**Supervisor Organizesi**, 2 aşamalı akışın eylemde olduğunu gösterir:  
1. **FileAgent** MCP aracılığıyla dosyayı okur ve içeriği saklar  
2. **ReportAgent** içeriği alır ve yapılandırılmış bir rapor oluşturur

Supervisor bu kararları **kendi kendine**, kullanıcının talebine dayanarak verdi.

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
  
#### Agentic Modül Özelliklerinin Açıklaması

Örnek, agentic modülün birkaç gelişmiş özelliğini gösterir. Agentic Scope ve Agent Dinleyicilerine daha yakından bakalım.

**Agentic Scope**, ajanların sonuçlarını `@Agent(outputKey="...")` kullanarak kaydettikleri paylaşılan belleği gösterir. Bu şunları sağlar:  
- Sonraki ajanların önceki ajan çıktılarına erişebilmesi  
- Supervisor'un nihai yanıtı sentezlemesi  
- Üretilen her ajanın çıktısını denetleyebilmeniz  

Aşağıdaki diyagram, Agentic Scope'un dosyadan rapora iş akışında paylaşılan bellek olarak nasıl çalıştığını gösteriyor – FileAgent çıktısını `fileContent` anahtarı altında yazar, ReportAgent bunu okur ve kendi çıktısını `report` altında yazar:

<img src="../../../translated_images/tr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope paylaşılan bellek görevi görür — FileAgent `fileContent`’i yazar, ReportAgent bunu okur ve `report` yazar, kodunuz da nihai sonucu okur.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent'ten ham dosya verisi
String report = scope.readState("report");            // ReportAgent'ten yapılandırılmış rapor
```
  
**Agent Dinleyicileri**, ajan yürütmesini izleme ve hata ayıklama olanağı sağlar. Demoda adım adım gördüğünüz çıktı, her ajan çağrısına bağlanan bir AgentListener’dan gelir:  
- **beforeAgentInvocation** - Supervisor bir ajan seçtiğinde çağrılır, hangi ajanın neden seçildiğini görmenizi sağlar  
- **afterAgentInvocation** - Bir ajan tamamlandığında çağrılır, sonucu gösterir  
- **inheritedBySubagents** - Doğruysa, dinleyici hiyerarşideki tüm ajanları izler  

Aşağıdaki diyagram, Agent Listener yaşam döngüsünü tam olarak gösterir, `onError`’un ajan yürütülmesi sırasında hata durumlarını nasıl ele aldığı dahil:

<img src="../../../translated_images/tr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Dinleyicileri, yürütme yaşam döngüsüne bağlanır — ajanlar başladığında, tamamlandığında veya hata aldığında izleme yapar.*

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
        return true; // Tüm alt ajanlara yayıl
    }
};
```
  
Supervisor deseninin ötesinde, `langchain4j-agentic` modülü çeşitli güçlü iş akışı desenleri sağlar. Aşağıdaki diyagram, basit ardışık boru hatlarından insan denetimli onay iş akışlarına kadar beşini gösteriyor:

<img src="../../../translated_images/tr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Ajanları organize etmek için beş iş akışı deseni — basit ardışık boru hatlarından insan denetimli onay iş akışlarına.*

| Desen | Açıklama | Kullanım Durumu |
|---------|-------------|----------|
| **Ardışık (Sequential)** | Ajanları sırayla çalıştırır, çıktı sonraki ajana akar | Boru hatları: araştır → analiz → rapor |
| **Paralel (Parallel)** | Ajanları aynı anda çalıştırır | Bağımsız görevler: hava durumu + haberler + borsa |
| **Döngü (Loop)** | Koşul sağlanana kadar yineleme yapar | Kalite puanlama: puan ≥ 0.8 olana kadar iyileştir |
| **Koşullu (Conditional)** | Koşullara göre yönlendirir | Sınıflandır → uzman ajana yönlendir |
| **İnsan Denetimli (Human-in-the-Loop)** | İnsan denetim noktaları ekler | Onay iş akışları, içerik inceleme |

## Anahtar Kavramlar

Şimdi MCP ve agentic modül ile çalışmayı keşfettiğinize göre, her yaklaşımı ne zaman kullanacağınıza kısaca bakalım.

MCP’nin en büyük avantajlarından biri gelişen ekosistemidir. Aşağıdaki diyagram, tek bir evrensel protokolün AI uygulamanızı dosya sistemi ve veritabanı erişiminden GitHub, e-posta, web kazımaya kadar çeşitli MCP sunucularına nasıl bağladığını gösterir:

<img src="../../../translated_images/tr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP evrensel bir protokol ekosistemi oluşturur — herhangi bir MCP uyumlu sunucu, her MCP uyumlu istemci ile çalışır, böylece uygulamalar arasında araç paylaşımı mümkün olur.*

**MCP**, mevcut araç ekosistemlerinden faydalanmak, birden fazla uygulamanın paylaşabileceği araçlar oluşturmak, üçüncü taraf hizmetleri standart protokollerle entegre etmek veya araç uygulamalarını kod değiştirmeden değiştirmek istediğinizde idealdir.

**Agentic Modül**, `@Agent` açıklamalarıyla bildirimsel ajan tanımları yapmak istediğinizde, iş akışı organizasyonu (ardışık, döngü, paralel) gerektiğinde, zorunlu kod yerine arabirim tabanlı ajan tasarımından hoşlandığınızda veya birden çok ajanın çıktıları `outputKey` ile paylaştığı durumlarda en iyi şekilde çalışır.

**Supervisor Ajan deseni**, iş akışının önceden tahmin edilemediği durumlarda ve kararları LLM’e bırakmak istediğinizde, dinamik organizasyon gereken çok özel ajanlarınız olduğunda, farklı becerilere yönlendiren konuşma sistemleri kurarken veya en esnek, uyarlanabilir ajan davranışını istediğinizde öne çıkar.

Özel `@Tool` yöntemleri ile bu modüldeki MCP araçları arasında karar vermenize yardımcı olması için, aşağıdaki karşılaştırma temel avantajları vurgular — özel araçlar uygulama-özel mantık için sıkı bağlılık ve tam tür güvenliği verirken, MCP araçları standartlaşmış, yeniden kullanılabilir entegrasyonlar sunar:

<img src="../../../translated_images/tr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Özel @Tool yöntemlerini veya MCP araçlarını ne zaman kullanmalı — özel araçlar uygulama-özel mantık için tam tür güvenliği ile, MCP araçları uygulamalar arasında çalışan standart entegrasyonlar için.*

## Tebrikler!

LangChain4j için Yeni Başlayanlar kursunun tüm beş modülünü tamamladınız! İşte tamamladığınız tam öğrenme yolculuğunun görünümü — temel sohbetten MCP destekli agentic sistemlere kadar:

<img src="../../../translated_images/tr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Beş modül boyunca öğrenme yolculuğunuz — temel sohbetten MCP destekli agentic sistemlere kadar.*

LangChain4j için Yeni Başlayanlar kursunu tamamladınız. Öğrendikleriniz:  

- Bellekli konuşma AI nasıl kurulur (Modül 01)  
- Farklı görevler için prompt mühendisliği desenleri (Modül 02)  
- Belgelerinizle yanıtları RAG ile temellendirme (Modül 03)  
- Özel araçlarla temel AI ajanları yaratma (Modül 04)  
- Standartlaştırılmış araçları LangChain4j MCP ve Agentic modülleri ile entegre etme (Modül 05)

### Sonrasında Ne Yapmalı?

Modülleri tamamladıktan sonra [Test Rehberi](../docs/TESTING.md) ile LangChain4j test konseptlerini uygulamada görün.

**Resmi Kaynaklar:**  
- [LangChain4j Dokümantasyonu](https://docs.langchain4j.dev/) - Kapsamlı rehberler ve API referansı  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kaynak kod ve örnekler  
- [LangChain4j Eğitimleri](https://docs.langchain4j.dev/tutorials/) - Çeşitli kullanım durumları için adım adım öğreticiler

Bu kursu tamamladığınız için teşekkürler!

---

**Geçiş:** [← Önceki: Modül 04 - Araçlar](../04-tools/README.md) | [Ana Sayfaya Dön](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba gösterilse de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->