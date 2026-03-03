# LangChain4j Sözlüğü

## İçindekiler

- [Temel Kavramlar](../../../docs)
- [LangChain4j Bileşenleri](../../../docs)
- [AI/ML Kavramları](../../../docs)
- [Koruyucu Önlemler](../../../docs)
- [İstem Mühendisliği](../../../docs)
- [RAG (Getirme Destekli Üretim)](../../../docs)
- [Ajanlar ve Araçlar](../../../docs)
- [Agentic Modülü](../../../docs)
- [Model Bağlam Protokolü (MCP)](../../../docs)
- [Azure Hizmetleri](../../../docs)
- [Test ve Geliştirme](../../../docs)

Kurs boyunca kullanılan terimler ve kavramlar için hızlı başvuru.

## Temel Kavramlar

**AI Agent** - Yapay zekayı kullanarak kendi kendine düşünen ve hareket eden sistem. [Modül 04](../04-tools/README.md)

**Chain** - Çıkışın sonraki adıma aktarıldığı işlem dizisi.

**Chunking** - Belgelerin daha küçük parçalara bölünmesi. Tipik: 300-500 token ve örtüşme. [Modül 03](../03-rag/README.md)

**Context Window** - Bir modelin işleyebileceği maksimum token sayısı. GPT-5.2: 400K token (en fazla 272K giriş, 128K çıkış).

**Embeddings** - Metin anlamını temsil eden sayısal vektörler. [Modül 03](../03-rag/README.md)

**Function Calling** - Modelin dış işlevleri çağırmak için yapılandırılmış istekler üretmesi. [Modül 04](../04-tools/README.md)

**Hallucination** - Modellerin hatalı ama inanılır bilgiler üretmesi.

**Prompt** - Dil modeline verilen metin girdisi. [Modül 02](../02-prompt-engineering/README.md)

**Semantic Search** - Anahtar kelime değil, gömülü anlam kullanarak arama. [Modül 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: hafızasız. Stateful: konuşma geçmişini tutar. [Modül 01](../01-introduction/README.md)

**Tokens** - Modellerin işlediği temel metin birimleri. Maliyet ve sınırları etkiler. [Modül 01](../01-introduction/README.md)

**Tool Chaining** - Çıktının sonraki çağrıyı bilgilendirdiği ardışık araç yürütme. [Modül 04](../04-tools/README.md)

## LangChain4j Bileşenleri

**AiServices** - Tip-güvenli yapay zeka servis arayüzleri oluşturur.

**OpenAiOfficialChatModel** - OpenAI ve Azure OpenAI modelleri için birleşik istemci.

**OpenAiOfficialEmbeddingModel** - OpenAI Resmi istemcisi kullanarak gömüler oluşturur (OpenAI ve Azure OpenAI destekler).

**ChatModel** - Dil modelleri için temel arayüz.

**ChatMemory** - Konuşma geçmişini tutar.

**ContentRetriever** - RAG için ilgili belge parçalarını bulur.

**DocumentSplitter** - Belgeleri parçalara böler.

**EmbeddingModel** - Metni sayısal vektörlere dönüştürür.

**EmbeddingStore** - Gömüleri depolar ve geri getirir.

**MessageWindowChatMemory** - Son mesajların kayan penceresini tutar.

**PromptTemplate** - `{{değişken}}` yer tutucularla yeniden kullanılabilir istemler oluşturur.

**TextSegment** - Meta verili metin parçası. RAG'de kullanılır.

**ToolExecutionRequest** - Araç yürütme isteğini temsil eder.

**UserMessage / AiMessage / SystemMessage** - Konuşma mesaj türleri.

## AI/ML Kavramları

**Few-Shot Learning** - İstemlerde örnek sağlama. [Modül 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Büyük metin verisiyle eğitilmiş yapay zeka modelleri.

**Reasoning Effort** - GPT-5.2 parametresi, düşünmenin derinliğini kontrol eder. [Modül 02](../02-prompt-engineering/README.md)

**Temperature** - Çıktı rastgeleliğini kontrol eder. Düşük=belirli, yüksek=yaratıcı.

**Vector Database** - Gömüler için özel veritabanı. [Modül 03](../03-rag/README.md)

**Zero-Shot Learning** - Örnek olmadan görev yapma. [Modül 02](../02-prompt-engineering/README.md)

## Koruyucu Önlemler - [Modül 00](../00-quick-start/README.md)

**Defense in Depth** - Uygulama seviyesinde koruyucu önlemlerle sağlayıcı güvenlik filtrelerini birleştiren çok katmanlı güvenlik yöntemi.

**Hard Block** - Sağlayıcı ciddi içerik ihlallerinde HTTP 400 hatası verir.

**InputGuardrail** - LLM’ye ulaşmadan önce kullanıcı girişini doğrulayan LangChain4j arayüzü. Zararlı istemleri erken engelleyerek maliyet ve gecikmeyi azaltır.

**InputGuardrailResult** - Koruyucu doğrulama dönüş türü: `success()` veya `fatal("sebep")`.

**OutputGuardrail** - AI yanıtlarını kullanıcılara dönmeden önce doğrulama arayüzü.

**Provider Safety Filters** - API düzeyinde ihlalleri yakalayan AI sağlayıcılarının yerleşik içerik filtreleri (örn. GitHub Modelleri).

**Soft Refusal** - Model kibarca hatasız yanıt vermekten kaçınır.

## İstem Mühendisliği - [Modül 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Daha iyi doğruluk için adım adım muhakeme.

**Constrained Output** - Belirli format veya yapı zorunluluğu.

**High Eagerness** - GPT-5.2 için titiz düşünme kalıbı.

**Low Eagerness** - GPT-5.2 için hızlı yanıt kalıbı.

**Multi-Turn Conversation** - Değiş tokuşlar arasında bağlamın korunması.

**Role-Based Prompting** - Sistem mesajlarıyla model kişiliğini belirleme.

**Self-Reflection** - Model kendi çıktısını değerlendirir ve geliştirir.

**Structured Analysis** - Sabit değerlendirme çerçevesi.

**Task Execution Pattern** - Planla → Uygula → Özetle.

## RAG (Getirme Destekli Üretim) - [Modül 03](../03-rag/README.md)

**Document Processing Pipeline** - Yükle → parçala → göm → depola.

**In-Memory Embedding Store** - Test için geçici depolama.

**RAG** - Yanıtları dayandırmak için getirmeyle üretimi birleştirir.

**Similarity Score** - Anlamsal benzerlik ölçüsü (0-1 arası).

**Source Reference** - Getirilen içerik hakkında meta veriler.

## Ajanlar ve Araçlar - [Modül 04](../04-tools/README.md)

**@Tool Annotation** - Java yöntemlerini AI tarafından çağrılabilir araç olarak işaretler.

**ReAct Pattern** - Düşün → Hareket Et → Gözlemle → Tekrarla.

**Session Management** - Farklı kullanıcılar için ayrı bağlamlar.

**Tool** - AI ajanının çağırabileceği işlev.

**Tool Description** - Araç amacı ve parametrelerinin dökümantasyonu.

## Agentic Modülü - [Modül 05](../05-mcp/README.md)

**@Agent Annotation** - AI ajanlarını bildirimsel davranış tanımlamasıyla işaretler.

**Agent Listener** - `beforeAgentInvocation()` ve `afterAgentInvocation()` ile ajan yürütmesini izler.

**Agentic Scope** - Ajanların çıktıları paylaştığı, sonraki ajanların kullandığı ortak hafıza alanı.

**AgenticServices** - `agentBuilder()` ve `supervisorBuilder()` ile ajanlar oluşturan fabrika.

**Conditional Workflow** - Koşullara göre farklı uzman ajanlara yönlendirme.

**Human-in-the-Loop** - Onay veya içerik incelemesi için insan kontrol noktaları ekleyen iş akışı.

**langchain4j-agentic** - Bildirimsel ajan oluşturmak için Maven bağımlılığı (deneysel).

**Loop Workflow** - Belirli koşul gerçekleşene kadar ajan yürütmesini yineleme (örneğin kalite skoru ≥ 0.8).

**outputKey** - Ajan anotasyonu parametresi, sonuçların Agentic Scope’da nereye kaydedileceğini belirtir.

**Parallel Workflow** - Bağımsız görevler için aynı anda birden fazla ajan çalıştırma.

**Response Strategy** - Süpervizörün nihai cevabı oluşturma yöntemi: SON, ÖZET veya PUANLI.

**Sequential Workflow** - Ajanları sırayla yürütme, çıktı bir sonraki adıma akar.

**Supervisor Agent Pattern** - Bir süpervizör LLM’nin hangi alt ajanları çağıracağına dinamik karar verdiği gelişmiş ajan modeli.

## Model Bağlam Protokolü (MCP) - [Modül 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j’de MCP entegrasyonu için Maven bağımlılığı.

**MCP** - Model Bağlam Protokolü: AI uygulamalarını dış araçlara bağlamak için standart. Bir kere oluştur, her yerde kullan.

**MCP Client** - MCP sunucularına bağlanan ve araçları keşfedip kullanan uygulama.

**MCP Server** - Araçları net açıklamalar ve parametre şemalarıyla MCP üzerinden sunan servis.

**McpToolProvider** - LangChain4j bileşeni, MCP araçlarını AI servisleri ve ajanlarında kullanılmak üzere sarar.

**McpTransport** - MCP iletişimi için arayüz. Uygulamaları Stdio ve HTTP içerir.

**Stdio Transport** - Yerel işlem taşımacılığı, stdin/stdout üzerinden. Dosya sistemi erişimi veya komut satırı araçları için uygun.

**StdioMcpTransport** - MCP sunucusunu alt süreç olarak çalıştıran LangChain4j uygulaması.

**Tool Discovery** - İstemcinin sunucudan açıklamalar ve şemalarla mevcut araçları sorgulaması.

## Azure Hizmetleri - [Modül 01](../01-introduction/README.md)

**Azure AI Search** - Vektör özellikli bulut araması. [Modül 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure kaynaklarını dağıtır.

**Azure OpenAI** - Microsoft’un kurumsal AI servisi.

**Bicep** - Azure altyapı kodlama dili. [Altyapı Rehberi](../01-introduction/infra/README.md)

**Deployment Name** - Azure’da model dağıtma için ad.

**GPT-5.2** - En son OpenAI modeli, muhakeme kontrolü sağlıyor. [Modül 02](../02-prompt-engineering/README.md)

## Test ve Geliştirme - [Test Rehberi](TESTING.md)

**Dev Container** - Konteynerleştirilmiş geliştirme ortamı. [Yapılandırma](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Ücretsiz AI model oyun alanı. [Modül 00](../00-quick-start/README.md)

**In-Memory Testing** - Bellekte geçici depolama ile test.

**Integration Testing** - Gerçek altyapı ile test.

**Maven** - Java derleme otomasyon aracı.

**Mockito** - Java için sahte/mock kütüphanesi.

**Spring Boot** - Java uygulama çatısı. [Modül 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->