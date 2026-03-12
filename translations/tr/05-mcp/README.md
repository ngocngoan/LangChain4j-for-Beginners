# Modül 05: Model Context Protocol (MCP)

## İçindekiler

- [Video Tanıtımı](../../../05-mcp)
- [Neler Öğreneceksiniz](../../../05-mcp)
- [MCP Nedir?](../../../05-mcp)
- [MCP Nasıl Çalışır](../../../05-mcp)
- [Agentik Modül](../../../05-mcp)
- [Örnekleri Çalıştırmak](../../../05-mcp)
  - [Önkoşullar](../../../05-mcp)
- [Hızlı Başlangıç](../../../05-mcp)
  - [Dosya İşlemleri (Stdio)](../../../05-mcp)
  - [Süpervizör Ajan](../../../05-mcp)
    - [Demo Çalıştırma](../../../05-mcp)
    - [Süpervizör Nasıl Çalışır](../../../05-mcp)
    - [FileAgent MCP Araçlarını Çalışma Zamanında Nasıl Keşfeder](../../../05-mcp)
    - [Yanıt Stratejileri](../../../05-mcp)
    - [Çıktıyı Anlama](../../../05-mcp)
    - [Agentik Modül Özelliklerinin Açıklaması](../../../05-mcp)
- [Temel Kavramlar](../../../05-mcp)
- [Tebrikler!](../../../05-mcp)
  - [Sonraki Adımlar?](../../../05-mcp)

## Video Tanıtımı

Bu modülle başlamayı açıklayan canlı oturumu izleyin:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Araçlar ve MCP ile AI Ajanları - Canlı Oturum" width="800"/></a>

## Neler Öğreneceksiniz

Konuşma bazlı yapay zekâ (AI) oluşturduk, istemleri (prompts) ustalıkla kullandınız, yanıtları belgelerde temellediniz ve araçlara sahip ajanlar yarattınız. Ancak tüm bu araçlar özel uygulamanız için özel olarak geliştirildi. Peki yapay zekânıza, herkesin oluşturup paylaşabileceği standartlaştırılmış bir araç ekosistemine erişim verebilseydiniz? Bu modülde, Model Context Protocol (MCP) ve LangChain4j'nin agentik modülü ile bunu nasıl yapacağınızı öğreneceksiniz. Öncelikle basit bir MCP dosya okuyucusunu gösteriyoruz, ardından nasıl kolayca gelişmiş agentik iş akışlarına Supervisor Agent modeli kullanarak entegre olduğunu gösteriyoruz.

## MCP Nedir?

Model Context Protocol (MCP) tam olarak bunu sağlar - yapay zekâ uygulamalarının harici araçları keşfetmesi ve kullanması için standart bir yol. Her veri kaynağı ya da hizmet için özel entegrasyonlar yazmak yerine, yeteneklerini tutarlı biçimde sunan MCP sunucularına bağlanırsınız. AI ajanınız sonra bu araçları otomatik olarak keşfedip kullanabilir.

Aşağıdaki diyagram farkı gösteriyor — MCP olmadan her entegrasyon özel, nokta-nokta bağlantılar gerektirir; MCP ile tek bir protokol uygulamanızı her araca bağlar:

<img src="../../../translated_images/tr/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Karşılaştırması" width="800"/>

*MCP öncesi: Karmaşık nokta-nokta entegrasyonlar. MCP sonrası: Tek protokol, sonsuz olanaklar.*

MCP yapay zekâ geliştirmede temel bir problemi çözer: her entegrasyon özel olur. GitHub’a erişmek mi istiyorsunuz? Özel kod. Dosya okumak mı? Özel kod. Veritabanı sorgulamak mı? Özel kod. Üstelik bu entegrasyonların hiçbiri diğer AI uygulamaları ile uyumlu değildir.

MCP bunu standartlaştırır. Bir MCP sunucusu, açıklamalı araçlar ve şema ile araçlarını sunar. Her MCP istemcisi bağlanabilir, mevcut araçları keşfedebilir ve kullanabilir. Bir kere kur, her yerde kullan.

Aşağıdaki diyagram bu mimariyi gösterir — tek bir MCP istemcisi (yapay zekâ uygulamanız) birden çok MCP sunucusuna bağlanır ve her biri kendi araç setini standart protokolle sunar:

<img src="../../../translated_images/tr/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Mimarisi" width="800"/>

*Model Context Protocol mimarisi - standartlaştırılmış araç keşfi ve çalıştırma*

## MCP Nasıl Çalışır

Altta, MCP katmanlı bir mimari kullanır. Java uygulamanız (MCP istemcisi) mevcut araçları keşfeder, ulaşım katmanı (Stdio veya HTTP) üzerinden JSON-RPC istekleri gönderir, MCP sunucusu işlemleri gerçekleştirir ve sonuçları döner. Aşağıdaki diyagram bu protokolün her katmanını ayrıntılı gösterir:

<img src="../../../translated_images/tr/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protokol Detayı" width="800"/>

*MCP nasıl çalışır — istemciler araçları keşfeder, JSON-RPC mesajları alışverişi yapar ve işlemleri bir taşımacılık katmanı üzerinden yürütür.*

**Sunucu-İstemci Mimarisi**

MCP, istemci-sunucu modelini kullanır. Sunucular araçlar sağlar - dosya okuma, veritabanı sorgulama, API çağrıları. İstemciler (AI uygulamanız) sunuculara bağlanır ve araçlarını kullanır.

LangChain4j ile MCP kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Araç Keşfi**

İstemciniz bir MCP sunucusuna bağlandığında "Hangi araçlara sahipsiniz?" diye sorar. Sunucu mevcut araçların listesini, açıklamalarını ve parametre şemalarını döner. AI ajanınız kullanıcı isteklerine göre hangi araçları kullanacağına karar verir. Aşağıdaki diyagram bu el sıkışmayı gösterir — istemci `tools/list` isteği gönderir, sunucu kullanılabilir araçları açıklama ve şemalarla döner:

<img src="../../../translated_images/tr/tool-discovery.07760a8a301a7832.webp" alt="MCP Araç Keşfi" width="800"/>

*AI başlangıçta mevcut araçları keşfeder — hangi yeteneklerin erişilebilir olduğunu bilir ve hangilerini kullanacağına karar verir.*

**Taşımacılık Mekanizmaları**

MCP farklı taşımacılık mekanizmalarını destekler. İki seçenek vardır: yerel alt süreç iletişimi için Stdio ve uzak sunucular için Streamable HTTP. Bu modül Stdio taşımayı gösterir:

<img src="../../../translated_images/tr/transport-mechanisms.2791ba7ee93cf020.webp" alt="Taşımacılık Mekanizmaları" width="800"/>

*MCP taşımacılık mekanizmaları: uzak sunucular için HTTP, yerel işlemler için Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Yerel işlemler için. Uygulamanız bir sunucu işlemi başlatır ve standart giriş/çıkış yoluyla iletişim kurar. Dosya sistemi erişimi veya komut satırı araçları için uygundur.

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

`@modelcontextprotocol/server-filesystem` sunucusu aşağıdaki araçları sunar, hepsi sizin belirttiğiniz dizinlerde sandboxed:

| Araç | Açıklama |
|------|-------------|
| `read_file` | Tek bir dosyanın içeriğini okumak |
| `read_multiple_files` | Bir çağrıda birden çok dosya okumak |
| `write_file` | Dosya oluşturmak veya üzerine yazmak |
| `edit_file` | Hedefe yönelik bul ve değiştir düzenlemeleri yapmak |
| `list_directory` | Bir yoldaki dosya ve dizinleri listelemek |
| `search_files` | Bir desene uyan dosyaları rekürsif aramak |
| `get_file_info` | Dosya meta verilerini almak (boyut, zaman damgaları, izinler) |
| `create_directory` | Dizin oluşturmak (ebeveyn dizinler dahil) |
| `move_file` | Dosya veya dizini taşımak ya da yeniden adlandırmak |

Aşağıdaki diyagram Stdio taşımacılığının çalışma zamanında nasıl işlediğini gösterir — Java uygulamanız MCP sunucusunu alt işlem olarak başlatır, stdin/stdout boruları üzerinden iletişim kurar, ağ veya HTTP katmanı yoktur:

<img src="../../../translated_images/tr/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Taşımacılık Akışı" width="800"/>

*Stdio taşımacılık sahnede — uygulamanız MCP sunucusunu alt işlem olarak başlatır ve stdin/stdout boruları ile haberleşir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Sohbet ile deneyin:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) dosyasını açın ve sorun:
> - "Stdio taşımacılık nasıl çalışır ve HTTP ile ne zaman kullanılmalı?"
> - "LangChain4j spawned MCP sunucu işlemlerinin yaşam döngüsünü nasıl yönetir?"
> - "Yapay zekâya dosya sistemine erişim verme güvenlik açısından ne anlama gelir?"

## Agentik Modül

MCP standart araçlar sağlarken, LangChain4j'nin **agentik modülü** bu araçları koordine eden ajanlar oluşturmak için bildirimsel (declarative) bir yol sunar. `@Agent` anotasyonu ve `AgenticServices` arayüzler aracılığıyla ajan davranışını tanımlamanıza olanak tanır, zorlayıcı kod yazmadan.

Bu modülde, gelişmiş agentik bir AI yaklaşımı olan **Süpervizör Ajan** modelini keşfedeceksiniz — bir "süpervizör" ajan, kullanıcı isteklerine göre dinamik olarak hangi alt ajanların çağrılacağına karar verir. Bu iki kavramı birleştirerek alt ajanlarımıza MCP ile güçlendirilmiş dosya erişimi yetenekleri vereceğiz.

Agentik modülü kullanmak için şu Maven bağımlılığını ekleyin:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Not:** `langchain4j-agentic` modülü çekirdek LangChain4j kütüphanelerinden farklı bir sürüm zamanlamasında yayınlandığı için ayrı bir sürüm özelliği (`langchain4j.mcp.version`) kullanır.

> **⚠️ Deneysel:** `langchain4j-agentic` modülü **deneysel**dir ve değişikliğe tabidir. Kararlı AI asistanlarını oluşturmanın yolu hala özel araçlarla `langchain4j-core` (Modül 04).

## Örnekleri Çalıştırmak

### Önkoşullar

- [Modül 04 - Araçlar](../04-tools/README.md) tamamlanmış (bu modül özel araç kavramlarını ve MCP araçlarıyla karşılaştırmasını temel alır)
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01'de `azd up` komutu tarafından oluşturulur)
- Java 21+, Maven 3.9+
- Node.js 16+ ve npm (MCP sunucuları için)

> **Not:** Ortam değişkenlerinizi henüz ayarlamadıysanız, dağıtım talimatları için [Modül 01 - Giriş](../01-introduction/README.md) bölümüne bakın (`azd up` `.env` dosyasını otomatik yaratır) veya kök dizinde `.env.example` dosyasını kopyalayıp `.env` yaparak değerlerinizi doldurun.

## Hızlı Başlangıç

**VS Code Kullanıyorsanız:** Keşfedici panelde herhangi bir demo dosyasına sağ tıklayıp **"Java Çalıştır"** seçin veya Çalıştır ve Hata Ayıkla panelinden başlatma konfigürasyonlarını kullanın (önce `.env` dosyanızdaki Azure kimlik bilgilerinin doğru ayarlandığından emin olun).

**Maven Kullanarak:** Alternatif olarak, aşağıdaki örneklerle komut satırından da çalıştırabilirsiniz.

### Dosya İşlemleri (Stdio)

Bu, yerel alt işlem tabanlı araçları gösterir.

**✅ Önkoşul yok** - MCP sunucusu otomatik olarak başlatılır.

**Başlatma Scriptleriyle (Tavsiye Edilen):**

Başlatma scriptleri kök `.env` dosyasından ortam değişkenlerini otomatik yükler:

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

**VS Code Kullanarak:** `StdioTransportDemo.java` dosyasına sağ tıklayıp **"Java Çalıştır"** seçin (`.env` dosyanızın doğru ayarlandığını kontrol edin).

Uygulama dosya sistemi MCP sunucusunu otomatik başlatır ve yerel bir dosyayı okur. Alt süreç yönetiminin sizin için nasıl yapıldığına dikkat edin.

**Beklenen çıktı:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Süpervizör Ajan

**Süpervizör Ajan modeli,** **esnek** bir agentik AI formudur. Bir Süpervizör, kullanıcının isteğine göre hangi ajanların çağrılacağına otonom olarak karar vermek için LLM kullanır. Bir sonraki örnekte MCP destekli dosya erişimi ile bir LLM ajanı birleştirerek denetimli bir dosya okuma → raporlama iş akışı oluşturuyoruz.

Demoda, `FileAgent` MCP dosya sistemi araçlarıyla dosyayı okur, `ReportAgent` ise yürütücü özeti (1 cümle), 3 kilit nokta ve öneriler içeren yapılandırılmış bir rapor oluşturur. Süpervizör bu akışı otomatik koordine eder:

<img src="../../../translated_images/tr/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Süpervizör Ajan Modeli" width="800"/>

*Süpervizör LLM'ini hangi ajanların ve hangi sırayla çağrılacağını kararlaştırmak için kullanır — sabit kodlanmış yönlendirme gerekmez.*

Dosyadan rapora boru hattımızın somut iş akışı şöyle görünür:

<img src="../../../translated_images/tr/file-report-workflow.649bb7a896800de9.webp" alt="Dosyadan Rapor Akışı" width="800"/>

*FileAgent dosyayı MCP araçlarıyla okur, sonra ReportAgent ham içeriği yapılandırılmış rapora dönüştürür.*

Aşağıdaki sıra diyagramı tam Süpervizör koordine sürecini izler — MCP sunucusu başlatılır, Süpervizör otonom ajan seçimi yapar, stdio ile araç çağrıları olur ve son rapor oluşturulur:

<img src="../../../translated_images/tr/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Süpervizör Ajan Sıra Diyagramı" width="800"/>

*Süpervizör otonom olarak FileAgent'ı çağırır (MCP sunucusunu stdio üzerinden araç çağrısıyla dosya okumak için kullanır), ardından ReportAgent'ı çağırır rapor oluşturmak için — her ajan çıktılarını paylaşılan Agentic Scope'ta saklar.*

Her ajan çıktılarını **Agentic Scope** (paylaşılan bellek) içinde saklar, böylece sonraki ajanlar önceki sonuçlara erişebilir. Bu MCP araçlarının agentik iş akışlarına nasıl kesintisiz entegre olduğunu gösterir — Süpervizör dosyaların *nasıl* okunduğunu bilmek zorunda değil, sadece `FileAgent` bunu yapabiliyor.

#### Demo Çalıştırma

Başlatma dosyaları kök `.env` dosyasından ortam değişkenlerini otomatik yükler:

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

**VS Code Kullanarak:** `SupervisorAgentDemo.java` dosyasına sağ tıklayıp **"Java Çalıştır"** seçin (`.env` dosyanız konfigüre edilmiş olmalı).

#### Süpervizör Nasıl Çalışır

Ajanları oluşturmadan önce MCP taşımacılığını bir istemciye bağlamalı ve bir `ToolProvider` olarak sarmalamalısınız. Böylece MCP sunucusunun araçları ajanlarınıza sunulur:

```java
// Taşıyıcıdan bir MCP istemcisi oluşturun
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// İstemciyi bir ToolProvider olarak sarın — bu, MCP araçlarını LangChain4j'ye köprüler
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Artık `mcpToolProvider`'ı MCP araçlarına ihtiyaç duyan herhangi bir ajana enjekte edebilirsiniz:

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

// Supervisor, dosya → rapor iş akışını yönetir
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Nihai raporu döndür
        .build();

// Supervisor, isteğe bağlı olarak hangi ajanların çağrılacağına karar verir
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent MCP Araçlarını Çalışma Zamanında Nasıl Keşfeder

Merak ediyor olabilirsiniz: **`FileAgent` npm dosya sistemi araçlarını nasıl kullanacağını nasıl biliyor?** Cevap şu ki bilmiyor — **LLM** araç şemaları üzerinden çalışma zamanında bunu çözüyor.
`FileAgent` arayüzü sadece bir **prompt tanımıdır**. `read_file`, `list_directory` veya herhangi başka bir MCP aracı hakkında önceden programlanmış bir bilgi içermez. İşte baştan sona olan süreç:

1. **Sunucu başlatılır:** `StdioMcpTransport`, `@modelcontextprotocol/server-filesystem` npm paketini bir alt işlem olarak çalıştırır  
2. **Araç keşfi:** `McpClient`, sunucuya `tools/list` JSON-RPC isteği gönderir, sunucu araç adlarını, açıklamalarını ve parametre şemalarını (örneğin `read_file` — *"Bir dosyanın tüm içeriğini oku"* — `{ path: string }`) yanıtlar  
3. **Şema enjeksiyonu:** `McpToolProvider`, keşfedilen bu şemaları sarar ve LangChain4j'ye kullanılabilir hale getirir  
4. **LLM karar verir:** `FileAgent.readFile(path)` çağrıldığında, LangChain4j sistem mesajı, kullanıcı mesajı **ve araç şemaları listesini** LLM'ye gönderir. LLM, araç açıklamalarını okur ve bir araç çağrısı (örneğin `read_file(path="/some/file.txt")`) oluşturur  
5. **Çalıştırma:** LangChain4j araç çağrısını keser, MCP istemcisi üzerinden Node.js alt işlemi yoluyla çalıştırır, sonucu alır ve LLM'ye geri iletir  

Bu, yukarıda anlatılan [Araç Keşfi](../../../05-mcp) mekanizmasının aynısıdır ancak ajan iş akışına özel olarak uygulanmıştır. `@SystemMessage` ve `@UserMessage` notasyonları LLM'nin davranışını yönlendirirken, enjeksiyonla verilen `ToolProvider` ona **yetenekleri** sağlar — LLM çalışma zamanında ikisini köprüler.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) dosyasını açın ve sorun:  
> - "Bu ajan hangi MCP aracını çağıracağını nasıl biliyor?"  
> - "Eğer ajan yapıcısından ToolProvider'ı kaldırırsam ne olur?"  
> - "Araç şemaları LLM'ye nasıl geçer?"  

#### Yanıt Stratejileri

Bir `SupervisorAgent` yapılandırırken, alt ajanlar görevlerini tamamladıktan sonra kullanıcıya nasıl son yanıtı vermesi gerektiğini belirlersiniz. Aşağıdaki diyagramda üç kullanılabilir strateji gösterilmiştir — LAST son ajanın çıktısını doğrudan döner, SUMMARY tüm çıktıları bir LLM üzerinden sentezler ve SCORED orijinal isteğe göre en yüksek puanı alana göre seçim yapar:

<img src="../../../translated_images/tr/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor’un son yanıtı nasıl oluşturduğuna dair üç strateji — son ajanın çıktısını, sentezlenmiş özetini ya da en yüksek puan almış seçeneği tercihinize göre seçebilirsiniz.*

Mevcut stratejiler şunlardır:

| Strateji | Açıklama |
|----------|----------|
| **LAST** | Supervisor son çağrılan alt ajan veya aracın çıktısını döner. Bu, akıştaki son ajanın tamamen nihai yanıtı üretmek üzere tasarlandığı durumlarda faydalıdır (örneğin araştırma hattında "Özet Ajan"). |
| **SUMMARY** | Supervisor kendi dahili Dil Modelini (LLM) kullanarak tüm etkileşim ve alt ajan çıktılarını özetler ve bu özetin tamamını son yanıt olarak verir. Bu, kullanıcıya temiz ve derlenmiş bir cevap sağlar. |
| **SCORED** | Sistem dahili bir LLM kullanarak LAST yanıtını ve SUMMARY özetini orijinal kullanıcı isteğine göre puanlar ve daha yüksek puan alan yanıtı döner. |

Tam uygulama için [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasına bakabilirsiniz.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) dosyasını açın ve sorun:  
> - "Supervisor hangi ajanları çağıracağına nasıl karar veriyor?"  
> - "Supervisor ve Sequential iş akışı desenleri arasındaki fark nedir?"  
> - "Supervisor'un planlama davranışını nasıl özelleştirebilirim?"  

#### Çıktıyı Anlamak

Demo çalıştırıldığında Supervisor'ın birden fazla ajanı nasıl yönettiği yapılandırılmış şekilde gösterilir. Her bölümün anlamı şudur:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**Başlık** iş akışı konseptini sunar: dosya okuma ile rapor oluşturma arasında odaklanmış bir boru hattı.

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
  
**İş Akışı Diyagramı** ajanlar arasındaki veri akışını gösterir. Her ajanın özel bir rolü vardır:  
- **FileAgent** MCP araçlarıyla dosyayı okur ve ham içeriği `fileContent` içinde saklar  
- **ReportAgent** bu içeriği kullanır ve `report` içinde yapılandırılmış bir rapor üretir  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Kullanıcı İsteği** görevi gösterir. Supervisor bunu ayrıştırır ve FileAgent → ReportAgent çağırmaya karar verir.

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
  
**Supervisor Orkestrasyonu** 2 adımlı akışı gösterir:  
1. **FileAgent** MCP aracılığıyla dosyayı okur ve içeriği kaydeder  
2. **ReportAgent** içeriği alır ve yapılandırılmış bir rapor oluşturur  

Supervisor, kullanıcının isteğine dayanarak **otonom şekilde** bu kararları vermiştir.

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

Örnek, agentic modülün birkaç ileri düzey özelliğini gösterir. Agentic Scope ve Agent Listeners'e daha yakından bakalım.

**Agentic Scope**, ajanların sonuçlarını `@Agent(outputKey="...")` kullanarak depoladığı paylaşılan belleği gösterir. Bu şunları sağlar:  
- Sonraki ajanların önceki ajanların çıktısına erişimi  
- Supervisor’un son yanıtı sentezlemesi  
- Hangi ajanın ne ürettiğini inceleyebilmeniz  

Aşağıdaki diyagram agentic scope’un dosyadan rapora iş akışında paylaşılan bellek olarak nasıl çalıştığını gösterir — FileAgent çıktısını `fileContent` anahtarı altında yazar, ReportAgent bunu okur ve kendi çıktısını `report` anahtarı altında yazar:

<img src="../../../translated_images/tr/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope paylaşılan bir bellek olarak çalışır — FileAgent `fileContent` yazar, ReportAgent okur ve `report` yazar, kodunuz da nihai sonucu okur.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent'tan ham dosya verisi
String report = scope.readState("report");            // ReportAgent'tan yapılandırılmış rapor
```
  
**Agent Listeners** ajan yürütmesini izlemeyi ve hata ayıklamayı mümkün kılar. Demoda gördüğünüz adım adım çıktı, her ajan çağrısına bağlanan bir AgentListener’dan gelir:  
- **beforeAgentInvocation** - Supervisor bir ajan seçtiğinde çağrılır, hangi ajanın neden seçildiğini görmenizi sağlar  
- **afterAgentInvocation** - Bir ajan tamamlandığında çağrılır, sonucunu gösterir  
- **inheritedBySubagents** - true ise, dinleyici hiyerarşideki tüm ajanları izler  

Aşağıdaki diyagram, `onError` kapsam dahili arama hatalarını nasıl yönettiği dahil tam Agent Listener yaşam döngüsünü gösterir:

<img src="../../../translated_images/tr/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listener’lar yürütme yaşam döngüsüne bağlanır — ajanların başlama, tamamlanma veya hata yaşama durumlarını izleyin.*

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
        return true; // Tüm alt ajanlara yayınız
    }
};
```
  
Supervisor deseninin ötesinde, `langchain4j-agentic` modülü çeşitli güçlü iş akışı desenleri sunar. Aşağıdaki diyagram beşini gösterir — basit sıralı boru hatlarından insan-dahil-onay iş akışlarına kadar:

<img src="../../../translated_images/tr/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Ajan orkestrasyonu için beş iş akışı deseni — basit sıralı boru hatlarından insan-dahil-onay iş akışlarına kadar.*

| Desen | Açıklama | Kullanım Durumu |
|--------|----------|-----------------|
| **Sequential** | Ajanları sırayla yürütür, çıktı sonraki aşamaya akar | Boru hatları: araştır → analiz → rapor |
| **Parallel** | Ajanları eşzamanlı çalıştırır | Bağımsız görevler: hava durumu + haber + borsa |
| **Loop** | Koşul sağlanana kadar iterasyon yapar | Kalite puanlaması: puan ≥ 0.8 olana kadar iyileştir |
| **Conditional** | Koşullara göre yönlendirir | Sınıflandır → uzman ajana yönlendir |
| **Human-in-the-Loop** | İnsan onay noktaları ekler | Onay iş akışları, içerik incelemesi |

## Temel Kavramlar

MCP ve agentic modülü uygulamalı olarak keşfettikten sonra, her yaklaşımı ne zaman kullanacağınıza kısaca bakalım.

MCP’nin en büyük avantajlarından biri büyüyen ekosistemidir. Aşağıdaki diyagram, tek bir evrensel protokolün AI uygulamanızı dosya sistemi ve veritabanı erişiminden GitHub, e-posta, web kazıma gibi çok çeşitli MCP sunucularına nasıl bağladığını gösterir:

<img src="../../../translated_images/tr/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP evrensel bir protokol ekosistemi oluşturur — herhangi bir MCP-uyumlu sunucu herhangi bir MCP-uyumlu istemci ile çalışabilir, uygulamalar arasında araç paylaşımına olanak tanır.*

**MCP** mevcut araç ekosistemlerinden faydalanmak, birden fazla uygulamanın paylaşabileceği araçlar oluşturmak, standart protokollerle üçüncü taraf servisleri entegre etmek veya araç uygulamalarını kod değiştirmeden değiştirmek istediğinizde idealdir.

**Agentic Modül** ise `@Agent` notasyonlarıyla deklaratif ajan tanımları yapmak, iş akışı orkestrasyonu (sıralı, döngü, paralel) gerektiren, zorlayıcı kod yerine arayüz tabanlı ajan tasarımını tercih eden veya `outputKey` aracılığıyla çıktıları paylaşan birden fazla ajanı birleştiren durumlarda en iyi şekilde çalışır.

**Supervisor Agent deseni**, iş akışı önceden tahmin edilemez olduğunda ve kararın LLM tarafından verilmesini istediğinizde, dinamik orkestrasyona ihtiyaç duyan uzman ajanlar varsa, yeteneklere göre yönlendirme yapılan konuşma sistemleri kurarken veya en esnek, uyarlanabilir ajan davranışı istediğinizde öne çıkar.

Modül 04’ten özel `@Tool` yöntemleri ile bu modüldeki MCP araçları arasında karar vermenize yardımcı olmak için aşağıdaki karşılaştırma temel avantajları vurgular — özel araçlar uygulamaya özgü mantık için güçlü bağlama ve tam tür güvenliği sağlarken, MCP araçları standartlaştırılmış, yeniden kullanılabilir entegrasyonlar sunar:

<img src="../../../translated_images/tr/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Özel @Tool yöntemlerini ne zaman kullanmalı, MCP araçlarını ne zaman tercih etmeli — özel araçlar uygulamaya özgü kod için tam tür güvenliği, MCP araçları uygulamalar arası standart entegrasyonlar.*

## Tebrikler!

LangChain4j for Beginners kursunun tüm beş modülünü tamamladınız! İşte tamamladığınız tam öğrenme yolculuğu — temel sohbetten MCP destekli agentic sistemlere kadar:

<img src="../../../translated_images/tr/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Tüm beş modül boyunca öğrenme yolculuğunuz — temel sohbetten MCP destekli agentic sistemlere.*

LangChain4j for Beginners kursunu tamamladınız. Şunları öğrendiniz:

- Bellekli sohbet AI nasıl oluşturulur (Modül 01)  
- Farklı görevler için prompt mühendisliği kalıpları (Modül 02)  
- Yanıtları belgelerle temel verme (RAG) (Modül 03)  
- Özel araçlarla temel AI ajanları (asistanlar) oluşturma (Modül 04)  
- Standart araçları LangChain4j MCP ve Agentic modülleriyle entegre etme (Modül 05)  

### Sonraki Adımlar?

Modülleri tamamladıktan sonra LangChain4j test kavramlarının uygulamasını görmek için [Testing Guide](../docs/TESTING.md) bölümünü keşfedin.

**Resmi Kaynaklar:**  
- [LangChain4j Dokümantasyonu](https://docs.langchain4j.dev/) - Kapsamlı rehberler ve API referansı  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Kaynak kod ve örnekler  
- [LangChain4j Eğitimleri](https://docs.langchain4j.dev/tutorials/) - Çeşitli kullanım alanları için adım adım eğitimler  

Bu kursu tamamladığınız için teşekkürler!

---

**Gezinme:** [← Önceki: Modül 04 - Araçlar](../04-tools/README.md) | [Ana Sayfaya Dön](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek herhangi bir yanlış anlama veya yanlış yorumdan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->