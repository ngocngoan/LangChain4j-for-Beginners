# Modül 03: RAG (Arama Destekli Üretim)

## İçindekiler

- [Öğrenecekleriniz](../../../03-rag)
- [RAG'i Anlamak](../../../03-rag)
- [Ön Koşullar](../../../03-rag)
- [Nasıl Çalışır](../../../03-rag)
  - [Belge İşleme](../../../03-rag)
  - [Gömülü Temsil Oluşturma](../../../03-rag)
  - [Anlamsal Arama](../../../03-rag)
  - [Cevap Üretimi](../../../03-rag)
- [Uygulamayı Çalıştırma](../../../03-rag)
- [Uygulamayı Kullanma](../../../03-rag)
  - [Belge Yükleme](../../../03-rag)
  - [Soru Sorma](../../../03-rag)
  - [Kaynak Referanslarını Kontrol Etme](../../../03-rag)
  - [Sorularla Deney Yapma](../../../03-rag)
- [Temel Kavramlar](../../../03-rag)
  - [Parçalama Stratejisi](../../../03-rag)
  - [Benzerlik Skorları](../../../03-rag)
  - [Bellekte Depolama](../../../03-rag)
  - [Bağlam Penceresi Yönetimi](../../../03-rag)
- [RAG'in Önemi Ne Zaman Artar](../../../03-rag)
- [Sonraki Adımlar](../../../03-rag)

## Öğrenecekleriniz

Önceki modüllerde, AI ile nasıl sohbet edeceğinizi ve istemlerinizi nasıl etkili şekilde yapılandıracağınızı öğrendiniz. Ancak temel bir sınırlama var: dil modelleri sadece eğitim sırasında öğrendiklerini bilir. Şirketinizin politikaları, projenizin dokümantasyonu veya eğitim verilmemiş başka bilgilerle ilgili soruları cevaplayamazlar.

RAG (Arama Destekli Üretim) bu sorunu çözer. Modeli bilgilerinizi öğretmeye çalışmak yerine (ki bu pahalı ve pratik değil), modelin belgelerinizde arama yapabilmesi sağlanır. Birisi soru sorduğunda sistem ilgili bilgiyi bulur ve bunu isteme ekler. Model sonra o alınan bağlam üzerinden cevap verir.

RAG'i modelin bir referans kütüphanesi varmış gibi düşünün. Soru sorulduğunda sistem:

1. **Kullanıcı Sorgusu** - Siz soru sorarsınız
2. **Gömülü Temsil** - Sorunuz vektöre dönüştürülür
3. **Vektör Araması** - Benzer belge parçalarını bulur
4. **Bağlam Toplama** - İlgili parçalar isteme eklenir
5. **Yanıt** - LLM, bu bağlama dayanarak yanıt üretir

Bu, modelin cevaplarını eğitim bilgisine bağlı kalmak yerine gerçek veriniz üzerine inşa eder.

## RAG'i Anlamak

Aşağıdaki diyagram temel kavramı gösterir: modelin sadece eğitim verisine güvenmek yerine, her cevabı üretmeden önce danışabileceği belgelerden oluşan bir referans kütüphanesi verilir.

<img src="../../../translated_images/tr/what-is-rag.1f9005d44b07f2d8.webp" alt="RAG Nedir" width="800"/>

Parçaların baştan sona nasıl bağlandığını gösteren şema. Kullanıcının sorusu dört aşamadan geçer — gömülü temsil, vektör arama, bağlam toplama ve cevap üretimi — her biri bir öncekine dayanır:

<img src="../../../translated_images/tr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Mimarisi" width="800"/>

Modülün geri kalanı, çalıştırabileceğiniz ve değiştirebileceğiniz kodla her aşamayı ayrıntılı olarak anlatır.

## Ön Koşullar

- Modül 01 tamamlandı (Azure OpenAI kaynakları dağıtıldı)
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01'de `azd up` komutuyla oluşturuldu)

> **Not:** Modül 01'i tamamlamadıysanız, dağıtım talimatlarını orada ilk uygulayın.


## Nasıl Çalışır

### Belge İşleme

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Bir belge yüklediğinizde, sistem onu çözer (PDF veya düz metin), dosya adı gibi meta veriler ekler ve sonra belgeleri modelin bağlam penceresine rahat sığacak şekilde, küçük parçalara böler. Bu parçalar sınırda bağlam kaybı olmaması için biraz birbirleriyle kesişir.

```java
// Yüklenen dosyayı ayrıştır ve LangChain4j Belgesi içine sar
Document document = Document.from(content, metadata);

// 30 token örtüşmeyle 300 tokenlik parçalara böl
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Aşağıdaki diyagram bunu görsel olarak gösterir. Her parça komşularıyla biraz token paylaşır — 30 tokenlık üst üste binme önemli bağlamın yok olmasını engeller:

<img src="../../../translated_images/tr/document-chunking.a5df1dd1383431ed.webp" alt="Belge Parçalama" width="800"/>

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dosyasını açın ve sorun:
> - "LangChain4j belgeleri parçalara nasıl ayırıyor ve kesişmenin önemi nedir?"
> - "Farklı belge türleri için optimal parça boyutu nedir ve neden?"
> - "Çok dilli veya özel formatlı belgeleri nasıl yönetirim?"

### Gömülü Temsil Oluşturma

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Her parça metin anlamını yakalayan matematiksel parmak izi gibi sayısal bir gösterime, yani gömülü temsile dönüştürülür. Benzer metinler benzer gömülü temsiller üretir.

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

Aşağıdaki sınıf diyagramı bu LangChain4j bileşenlerinin nasıl bağlandığını gösterir. `OpenAiOfficialEmbeddingModel` metni vektöre dönüştürür, `InMemoryEmbeddingStore` vektörleri orijinal `TextSegment` verisiyle birlikte tutar, `EmbeddingSearchRequest` ise `maxResults` ve `minScore` gibi getirme parametrelerini kontrol eder:

<img src="../../../translated_images/tr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Sınıfları" width="800"/>

Gömülü temsiller depolandıktan sonra benzer içerikler vektör alanında doğal olarak kümelenir. Aşağıdaki görsel, ilgili konulardaki belgelerin yakın noktalar olarak nasıl gruplanabileceğini gösterir — bu anlamsal aramayı mümkün kılar:

<img src="../../../translated_images/tr/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektör Gömülü Temsil Alanı" width="800"/>

### Anlamsal Arama

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Soru sorduğunuzda, sorunuz da gömülü temsile dönüştürülür. Sistem, sorunuzun gömülü temsili ile tüm belge parçalarının gömülü temsillerini karşılaştırır. En benzer anlamda olan parçaları bulur — sadece anahtar kelime eşleşmesi değil, gerçek anlamsal benzerlik.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

Aşağıdaki diyagram, anlamsal aramayı geleneksel anahtar kelime aramasıyla karşılaştırır. "Araç" kelimesi için anahtar kelime araması "arabalar ve kamyonlar" hakkında olan parçayı kaçırır, ama anlamsal arama bunların aynı anlama geldiğini anlar ve yüksek skorla sonucu döndürür:

<img src="../../../translated_images/tr/semantic-search.6b790f21c86b849d.webp" alt="Anlamsal Arama" width="800"/>

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dosyasını açın ve sorun:
> - "Benzerlik araması gömülü temsillerle nasıl çalışır ve skoru ne belirler?"
> - "Hangi benzerlik eşik değerini kullanmalıyım ve sonuçları nasıl etkiler?"
> - "İlgili belge bulunamadığında ne yapmalıyım?"

### Cevap Üretimi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

En alakalı parçalar, açık talimatlar, bulunan bağlam ve kullanıcının sorusunu içeren yapılandırılmış bir isteme derlenir. Model bu özel parçaları okur ve bu bilgiler doğrultusunda yanıt üretir — sadece önünde olanı kullanabilir, bu da uydurma (halüsinasyon) olasılığını engeller.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Aşağıdaki diyagram bu birleştirmeyi gösterir — arama aşamasından en yüksek skorlu parçalar istem şablonuna eklenir ve `OpenAiOfficialChatModel` temellendirilmiş bir cevap üretir:

<img src="../../../translated_images/tr/context-assembly.7e6dd60c31f95978.webp" alt="Bağlam Birleştirme" width="800"/>

## Uygulamayı Çalıştırma

**Dağıtımı doğrulayın:**

Kök dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Eğer Modül 01'deki `./start-all.sh` komutu ile tüm uygulamaları zaten başlattıysanız, bu modül 8081 portunda zaten çalışıyor. Aşağıdaki başlatma komutlarını atlayıp doğrudan http://localhost:8081 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirici konteyneri, tüm Spring Boot uygulamalarını yönetmek için görsel bir arayüz sağlayan Spring Boot Dashboard uzantısını içerir. VS Code'un sol yanındaki Etkinlik Çubuğunda (Spring Boot simgesini arayın) bulabilirsiniz.

Spring Boot Dashboard'dan:
- Çalışma alanındaki tüm Spring Boot uygulamalarını görebilirsiniz
- Uygulamaları tek tıklamayla başlatıp durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

"rag" modülünün yanındaki oynat düğmesine tıklayarak bu modülü başlatabilir veya tüm modülleri aynı anda başlatabilirsiniz.

<img src="../../../translated_images/tr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Seçenek 2: Komut dosyaları kullanımı**

Tüm web uygulamalarını başlatın (modüller 01-04):

**Bash:**
```bash
cd ..  # Kök dizininden
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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Her iki betik kök dizindeki `.env` dosyasından ortam değişkenlerini yükler ve JAR dosyaları yoksa oluşturur.

> **Not:** Başlatmadan önce tüm modülleri elle derlemek isterseniz:
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

Tarayıcınızda http://localhost:8081 adresini açın.

**Durdurmak için:**

**Bash:**
```bash
./stop.sh  # Yalnızca bu modül
# Veya
cd .. && ./stop-all.sh  # Tüm modüller
```

**PowerShell:**
```powershell
.\stop.ps1  # Yalnızca bu modül
# Veya
cd ..; .\stop-all.ps1  # Tüm modüller
```

## Uygulamayı Kullanma

Uygulama belge yükleme ve soru sorma için web arayüzü sağlar.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tr/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Uygulama Arayüzü" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG uygulama arayüzü - belge yükleyin ve sorular sorun*

### Belge Yükleme

Başlangıç olarak bir belge yükleyin - denemede TXT dosyaları en iyisidir. Bu klasörde LangChain4j özellikleri, RAG uygulaması ve en iyi uygulamalar hakkında bilgi içeren `sample-document.txt` dosyası mevcuttur - sistemi test etmek için mükemmel.

Sistem belgenizi işler, parçalara böler ve her parça için gömülü temsiller oluşturur. Bu işlem yükleme sırasında otomatik gerçekleşir.

### Soru Sorma

Belge içeriği hakkında spesifik sorular sorun. Belgeden açıkça belirtilmiş somut bir şey deneyin. Sistem ilgili parçaları arar, isteme ekler ve cevap üretir.

### Kaynak Referanslarını Kontrol Etme

Her cevap, benzerlik skorlarıyla kaynak referansları içerir. Bu skorlar (0 ile 1 arasında) her bir parçanın sorunuzla ne kadar alakalı olduğunu gösterir. Yüksek skorlar daha iyi eşleşme demektir. Bu sayede cevabın kaynağını doğrulayabilirsiniz.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tr/rag-query-results.6d69fcec5397f355.webp" alt="RAG Sorgu Sonuçları" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Sorgu sonuçları, cevap ile kaynak referanslar ve alaka puanları gösteriliyor*

### Sorularla Deney Yapma

Farklı türde sorular deneyin:
- Spesifik gerçekler: "Ana konu nedir?"
- Karşılaştırmalar: "X ile Y arasındaki fark nedir?"
- Özetler: "Z hakkında ana noktaları özetle"

Sorunuzun belge içeriğiyle ne kadar iyi eşleştiğine göre benzerlik skorlarının nasıl değiştiğini gözlemleyin.

## Temel Kavramlar

### Parçalama Stratejisi

Belgeler 300 tokenlık parçalar halinde, 30 token üst üste binme ile bölünür. Bu denge, her parçanın anlamlı bağlama sahip olmasını sağlarken çok büyük olmamasını ve birden fazla parçanın bir istemde yer almasını mümkün kılar.

### Benzerlik Skorları

Her getirilen parça, kullanıcının sorusuyla ne kadar yakın eşleştiğini gösteren 0 ile 1 arasında benzerlik skoru ile gelir. Aşağıdaki diyagram skor aralıklarını ve sistemin bunları nasıl filtre için kullandığını görselleştirir:

<img src="../../../translated_images/tr/similarity-scores.b0716aa911abf7f0.webp" alt="Benzerlik Skorları" width="800"/>

Skor aralıkları:
- 0.7-1.0: Çok alakalı, kesin eşleşme
- 0.5-0.7: Alakalı, iyi bağlam
- 0.5'in altında: Filtrelenmiş, çok alakasız

Sistem kaliteyi sağlamak için minimum eşik üstündeki parçaları getirir.

### Bellekte Depolama

Bu modülde basitlik için bellek içi depolama kullanılır. Uygulamayı yeniden başlatınca yüklenen belgeler kaybolur. Üretim sistemleri Qdrant veya Azure AI Search gibi kalıcı vektör veritabanları kullanır.

### Bağlam Penceresi Yönetimi

Her modelin maksimum bağlam penceresi vardır. Büyük belgeden tüm parçaları ekleyemezsiniz. Sistem en alakalı ilk N parçayı (varsayılan 5) getirir, böylece sınırlar aşılmaz ve yeterli bağlam sunulur.

## RAG'in Önemi Ne Zaman Artar

RAG her zaman en iyi yaklaşım değildir. Aşağıdaki karar rehberi, RAG'in ne zaman değer kattığını, ne zaman daha basit yaklaşımların — örneğin içeriği doğrudan isteme eklemek veya modelin yerleşik bilgisini kullanmak — yeterli olduğunu anlamanıza yardımcı olur:

<img src="../../../translated_images/tr/when-to-use-rag.1016223f6fea26bc.webp" alt="RAG Kullanım Zamanı" width="800"/>

**RAG kullanın:**
- Tescilli belgelerle ilgili soruları yanıtlamak  
- Bilgiler sık sık değişir (politikalar, fiyatlar, teknik özellikler)  
- Doğruluk için kaynak gösterimi gereklidir  
- İçerik tek bir istemde sığamayacak kadar büyüktür  
- Doğrulanabilir, dayanaklı yanıtlar istiyorsunuz  

**RAG'ı kullanmayın, eğer:**  
- Sorular modelin zaten sahip olduğu genel bilgi gerektiriyorsa  
- Gerçek zamanlı veri gerekiyorsa (RAG yüklenen belgeler üzerinde çalışır)  
- İçerik doğrudan istemlere dahil edilebilecek kadar küçükse  

## Sonraki Adımlar

**Sonraki Modül:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Gezinme:** [← Önceki: Modül 02 - İstem Mühendisliği](../02-prompt-engineering/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 04 - Araçlar →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilindeki metni yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilmektedir. Bu çevirinin kullanımı sonucunda ortaya çıkabilecek yanlış anlamalar veya yanlış yorumlamalardan sorumlu tutulamayız.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->