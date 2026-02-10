# LangChain4j Sözlüğü

## İçindekiler

- [Temel Kavramlar](../../../docs)
- [LangChain4j Bileşenleri](../../../docs)
- [AI/ML Kavramları](../../../docs)
- [Koruyucu Önlemler](../../../docs)
- [Prompt Mühendisliği](../../../docs)
- [RAG (Getirme Destekli Üretim)](../../../docs)
- [Ajanlar ve Araçlar](../../../docs)
- [Ajansal Modül](../../../docs)
- [Model Bağlam Protokolü (MCP)](../../../docs)
- [Azure Hizmetleri](../../../docs)
- [Test ve Geliştirme](../../../docs)

Kurs boyunca kullanılan terimler ve kavramlar için hızlı başvuru.

## Temel Kavramlar

**AI Agent** - Özerk şekilde akıl yürütme ve hareket etme yeteneğine sahip yapay zeka sistemi. [Modül 04](../04-tools/README.md)

**Zincir** - Çıktının bir sonraki adıma besleme yaptığı işlem dizisi.

**Parçalama** - Belgeleri daha küçük parçalara bölme. Tipik: 300-500 token ve örtüşme. [Modül 03](../03-rag/README.md)

**Bağlam Penceresi** - Bir modelin işleyebileceği maksimum token sayısı. GPT-5.2: 400K token.

**Öbeklemeler (Embeddings)** - Metnin anlamını temsil eden sayısal vektörler. [Modül 03](../03-rag/README.md)

**Fonksiyon Çağrısı** - Modelin dış fonksiyonları çağırmak için yapılandırılmış istekler üretmesi. [Modül 04](../04-tools/README.md)

**Hallüsinasyon** - Modellerin yanlış ancak mantıklı görünen bilgi üretmesi.

**Prompt** - Dil modeline verilen metin girişi. [Modül 02](../02-prompt-engineering/README.md)

**Anlamsal Arama** - Anahtar kelime yerine öbeklemeler kullanarak anlamla arama. [Modül 03](../03-rag/README.md)

**Durumlu vs Durumsuz** - Durumsuz: hafıza yok. Durumlu: konuşma geçmişini korur. [Modül 01](../01-introduction/README.md)

**Tokenler** - Modellerin işlediği temel metin birimleri. Maliyet ve sınırları etkiler. [Modül 01](../01-introduction/README.md)

**Araç Zinciri** - Çıktının sonraki aracı bilgilendirdiği ardışık araç yürütme. [Modül 04](../04-tools/README.md)

## LangChain4j Bileşenleri

**AiServices** - Tür güvenliği olan AI servis arabirimleri oluşturur.

**OpenAiOfficialChatModel** - OpenAI ve Azure OpenAI modelleri için birleşik istemci.

**OpenAiOfficialEmbeddingModel** - OpenAI Official istemcisi kullanarak öbeklemeler oluşturur (OpenAI ve Azure OpenAI destekler).

**ChatModel** - Dil modelleri için temel arabirim.

**ChatMemory** - Konuşma geçmişini tutar.

**ContentRetriever** - RAG için ilgili belge parçalarını bulur.

**DocumentSplitter** - Belgeleri parçalara böler.

**EmbeddingModel** - Metni sayısal vektörlere dönüştürür.

**EmbeddingStore** - Öbeklemeleri depolar ve geri çağırır.

**MessageWindowChatMemory** - Son mesajların kayan penceresini tutar.

**PromptTemplate** - `{{variable}}` yer tutucularıyla yeniden kullanılabilir promptlar oluşturur.

**TextSegment** - Özniteliği olan metin parçası. RAG'de kullanılır.

**ToolExecutionRequest** - Araç yürütme isteğini temsil eder.

**UserMessage / AiMessage / SystemMessage** - Konuşma mesaj türleri.

## AI/ML Kavramları

**Few-Shot Learning** - Promptlarda örnekler verme. [Modül 02](../02-prompt-engineering/README.md)

**Büyük Dil Modeli (LLM)** - Geniş metin verileriyle eğitilmiş yapay zeka modelleri.

**Akıl Yürütme Çabası** - GPT-5.2'de düşünme derinliğini kontrol eden parametre. [Modül 02](../02-prompt-engineering/README.md)

**Sıcaklık (Temperature)** - Çıktı rastgeleliğini kontrol eder. Düşük = belirleyici, yüksek = yaratıcı.

**Vektör Veritabanı** - Öbeklemeler için özel veritabanı. [Modül 03](../03-rag/README.md)

**Zero-Shot Learning** - Örnek olmadan görev yapma. [Modül 02](../02-prompt-engineering/README.md)

## Koruyucu Önlemler - [Modül 00](../00-quick-start/README.md)

**Derinliğe Dayalı Savunma** - Uygulama seviyesi koruyucu önlemlerle sağlayıcı güvenlik filtrelerini birleştiren çok katmanlı güvenlik yaklaşımı.

**Katı Engelleme** - Sağlayıcının ciddi içerik ihlalleri için HTTP 400 hatası vermesi.

**InputGuardrail** - Kullanıcı girişi LLM'ye ulaşmadan önce doğrulamak için LangChain4j arabirimi. Zararlı promptları erken engelleyerek maliyet ve gecikmeyi azaltır.

**InputGuardrailResult** - Koruyucu önlem doğrulamasının dönüş türü: `success()` veya `fatal("reason")`.

**OutputGuardrail** - AI cevaplarını kullanıcıya dönmeden önce doğrulama arabirimi.

**Sağlayıcı Güvenlik Filtreleri** - API seviyesinde ihlalleri yakalayan AI sağlayıcılarının (örneğin GitHub Modelleri) yerleşik içerik filtreleri.

**Yumuşak Reddetme** - Model hataya yol açmadan nazikçe yanıt vermeyi reddeder.

## Prompt Mühendisliği - [Modül 02](../02-prompt-engineering/README.md)

**Düşünce Zinciri** - Daha iyi doğruluk için adım adım akıl yürütme.

**Kısıtlı Çıktı** - Belirli format veya yapıyı zorlamak.

**Yüksek İsteklilik** - GPT-5.2'de detaylı akıl yürütme deseni.

**Düşük İsteklilik** - GPT-5.2'de hızlı yanıt deseni.

**Çok Turlu Konuşma** - Değiş tokuşlar arasında bağlam sürdürme.

**Rol Tabanlı Prompt** - Sistem mesajlarıyla model kişiliğini belirleme.

**Öz-Yansıtma** - Modelin çıktısını değerlendirmesi ve geliştirmesi.

**Yapılandırılmış Analiz** - Sabit değerlendirme çerçevesi.

**Görev Yürütme Deseni** - Planla → Yürüt → Özetle.

## RAG (Getirme Destekli Üretim) - [Modül 03](../03-rag/README.md)

**Belge İşleme Hattı** - Yükle → parçalara ayır → öbekle → depola.

**Bellek İçi Öbekleme Deposu** - Test için kalıcı olmayan depolama.

**RAG** - Yanıtları sağlamlaştırmak için getirme ile üretimi birleştirir.

**Benzerlik Puanı** - Anlamsal benzerlik ölçüsü (0-1).

**Kaynak Referansı** - Getirilen içeriğe dair meta veri.

## Ajanlar ve Araçlar - [Modül 04](../04-tools/README.md)

**@Tool Anotasyonu** - Java yöntemlerini AI tarafından çağrılabilir araçlar olarak işaretler.

**ReAct Deseni** - Akıl Yürüt → Hareket Et → Gözlemle → Tekrarla.

**Oturum Yönetimi** - Farklı kullanıcılar için ayrı bağlamlar.

**Araç** - AI ajanının çağırabileceği fonksiyon.

**Araç Açıklaması** - Araç amacı ve parametrelerinin dokümantasyonu.

## Ajansal Modül - [Modül 05](../05-mcp/README.md)

**@Agent Anotasyonu** - Arabirimleri, davranışı bildirimsel olarak tanımlanmış AI ajanları olarak işaretler.

**Agent Listener** - `beforeAgentInvocation()` ve `afterAgentInvocation()` ile ajan yürütmesini izlemek için kanca.

**Ajansal Alan** - Ajanların çıktıları `outputKey` ile depolayıp aşağıdaki ajanların kullanabileceği paylaşılan hafıza.

**AgenticServices** - `agentBuilder()` ve `supervisorBuilder()` kullanarak ajan yaratan fabrika.

**Koşullu İş Akışı** - Farklı uzman ajanlara koşullara göre yönlendirme.

**İnsan Döngüde (Human-in-the-Loop)** - Onay veya içerik incelemesi için insan kontrol noktaları ekleyen iş akışı.

**langchain4j-agentic** - Bildirimsel ajan oluşturma için Maven bağımlılığı (deneysel).

**Döngü İş Akışı** - Bir koşul sağlanana kadar (ör. kalite puanı ≥ 0.8) ajan yürütmesini yineleme.

**outputKey** - Ajan anotasyonu parametresi; sonuçların Ajansal Alan'da depolanacağı yer.

**Paralel İş Akışı** - Bağımsız görevler için birden fazla ajanın aynı anda çalıştırılması.

**Yanıt Stratejisi** - Gözetmenin nihai yanıtı oluşturma biçimi: SON, ÖZET veya PUANLI.

**Ardışık İş Akışı** - Ajanların çıktılarının bir sonraki adıma aktarılarak sırayla yürütülmesi.

**Gözetmen Ajan Deseni** - Hangi alt ajanların çağrılacağına dinamik karar veren gelişmiş ajansal desen.

## Model Bağlam Protokolü (MCP) - [Modül 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j’de MCP entegrasyonu için Maven bağımlılığı.

**MCP** - Model Bağlam Protokolü: AI uygulamalarını dış araçlara bağlamak için standart. Bir kere oluştur, her yerde kullan.

**MCP İstemcisi** - MCP sunucularına bağlanan, araçları keşfeden ve kullanan uygulama.

**MCP Sunucusu** - MCP üzerinden araçları açıklamalar ve parametre şemaları ile sunan servis.

**McpToolProvider** - MCP araçlarını AI servisleri ve ajanlarda kullanılmak üzere saran LangChain4j bileşeni.

**McpTransport** - MCP iletişimi arabirimi. Uygulamaları Stdio ve HTTP’yi içerir.

**Stdio Taşımacılığı** - stdin/stdout üzerinden yerel süreç taşımacılığı. Dosya sistemi erişimi veya komut satırı araçları için idealdir.

**StdioMcpTransport** - MCP sunucusunu alt süreç olarak başlatan LangChain4j uygulaması.

**Araç Keşfi** - İstemcinin sunucudan açıklamalar ve şemalarla birlikte mevcut araçları sorgulaması.

## Azure Hizmetleri - [Modül 01](../01-introduction/README.md)

**Azure AI Arama** - Vektör yetenekli bulut arama. [Modül 03](../03-rag/README.md)

**Azure Geliştirici CLI (azd)** - Azure kaynaklarını konuşlandırma aracı.

**Azure OpenAI** - Microsoft’un kurumsal AI servisi.

**Bicep** - Azure altyapı kodu dili. [Altyapı Kılavuzu](../01-introduction/infra/README.md)

**Dağıtım Adı** - Azure’daki model dağıtımı için isim.

**GPT-5.2** - Akıl yürütme kontrolü ile en yeni OpenAI modeli. [Modül 02](../02-prompt-engineering/README.md)

## Test ve Geliştirme - [Test Kılavuzu](TESTING.md)

**Dev Container** - Konteyner tabanlı geliştirme ortamı. [Yapılandırma](../../../.devcontainer/devcontainer.json)

**GitHub Modelleri** - Ücretsiz AI model sahası. [Modül 00](../00-quick-start/README.md)

**Bellek İçi Test** - Bellek içi depolama ile test.

**Entegrasyon Testi** - Gerçek altyapı ile test.

**Maven** - Java derleme otomasyon aracı.

**Mockito** - Java taklit (mock) çerçevesi.

**Spring Boot** - Java uygulama çatısı. [Modül 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda ortaya çıkabilecek yanlış anlamalar veya yorumlamalar nedeniyle sorumluluk kabul edilmemektedir.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->