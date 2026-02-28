# Modül 03: RAG (Retrieval-Augmented Generation)

## İçindekiler

- [Video Anlatımı](../../../03-rag)
- [Neler Öğreneceksiniz](../../../03-rag)
- [Ön Koşullar](../../../03-rag)
- [RAG Anlama](../../../03-rag)
  - [Bu Eğitim Hangi RAG Yaklaşımını Kullanıyor?](../../../03-rag)
- [Nasıl Çalışır](../../../03-rag)
  - [Belge İşleme](../../../03-rag)
  - [Embedding Oluşturma](../../../03-rag)
  - [Anlamsal Arama](../../../03-rag)
  - [Cevap Üretimi](../../../03-rag)
- [Uygulamayı Çalıştırma](../../../03-rag)
- [Uygulamayı Kullanma](../../../03-rag)
  - [Bir Belge Yükleme](../../../03-rag)
  - [Sorular Sorma](../../../03-rag)
  - [Kaynak Referanslarını Kontrol Etme](../../../03-rag)
  - [Sorularla Deney Yapma](../../../03-rag)
- [Anahtar Kavramlar](../../../03-rag)
  - [Parça Bölme Stratejisi](../../../03-rag)
  - [Benzerlik Skorları](../../../03-rag)
  - [Bellekte Depolama](../../../03-rag)
  - [Kontekst Penceresi Yönetimi](../../../03-rag)
- [RAG'in Önemi Ne Zaman Ortaya Çıkar?](../../../03-rag)
- [Sonraki Adımlar](../../../03-rag)

## Video Anlatımı

Bu modülle nasıl başlayacağınızı anlatan canlı oturumu izleyin:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="LangChain4j ile RAG - Canlı Oturum" width="800"/></a>

## Neler Öğreneceksiniz

Önceki modüllerde, yapay zeka ile nasıl sohbet edileceğini ve prompt'larınızı etkili şekilde nasıl yapılandıracağınızı öğrendiniz. Fakat temel bir sınırlama vardır: dil modelleri sadece eğitim sırasında öğrendiklerini bilir. Şirketinizin politikaları, proje dokümantasyonunuz ya da eğitim almadığı başka bilgiler hakkında soruları yanıtlayamazlar.

RAG (Retrieval-Augmented Generation) bu problemi çözer. Modele bilgilerinizi öğretmeye çalışmak yerine (ki bu pahalı ve uygulanması zor), ona belgelerinizde arama yapabilme yeteneği verirsiniz. Birisi soru sorduğunda, sistem ilgili bilgileri bulur ve prompt’a ekler. Model ardından o alınan bağlamı temel alarak cevap verir.

RAG’i modele bir referans kütüphanesi vermek olarak düşünün. Bir soru sorduğunuzda sistem:

1. **Kullanıcı Sorgusu** – Siz soru sorarsınız  
2. **Embedding** – Sorunuzu bir vektöre dönüştürür  
3. **Vektör Araması** – Benzer belge parçalarını bulur  
4. **Bağlam Oluşumu** – İlgili parçaları prompt’a ekler  
5. **Yanıt** – LLM, bağlama dayanarak cevap üretir  

Bu, modelin yanıtlarını eğitim bilgisinden veya uydurulan cevaplardan ziyade gerçek verilerinize dayandırır.

## Ön Koşullar

- [Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) tamamlanmış olmalı (yukarıda referans verilen Kolay RAG örneği için)  
- [Modül 01 - Giriş](../01-introduction/README.md) tamamlanmış olmalı (Azure OpenAI kaynakları dağıtılmış, `text-embedding-3-small` embedding modeli dahil)  
- Azure kimlik bilgileri ile birlikte kök dizinde `.env` dosyası (Modül 01’de `azd up` komutuyla oluşturulur)  

> **Not:** Henüz Modül 01’i tamamlamadıysanız önce oradaki dağıtım talimatlarını izleyin. `azd up` komutu hem GPT chat modelini hem de bu modülde kullanılan embedding modelini dağıtır.

## RAG Anlama

Aşağıdaki diyagram temel kavramı gösterir: modelin sadece eğitim verilerine bağımlı olmak yerine, her cevap üretmeden önce başvurabileceği belgelerden oluşan bir referans kütüphanesi sunar.

<img src="../../../translated_images/tr/what-is-rag.1f9005d44b07f2d8.webp" alt="RAG Nedir" width="800"/>

*Bu diyagram, standart bir LLM’in (eğitim verilerinden tahmin yapan) ile RAG destekli bir LLM’in (önce belgelerinize danışan) arasındaki farkı gösterir.*

Bölümler şu şekilde birbirine bağlıdır. Bir kullanıcının sorusu dört aşamadan geçer — embedding, vektör araması, bağlam oluşturma ve cevap üretimi — ve her aşama öncekinin üzerine inşa edilir:

<img src="../../../translated_images/tr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Mimarisi" width="800"/>

*Bu diyagram, RAG hattını baştan sona gösterir — bir kullanıcı sorgusu embedding, vektör araması, bağlam oluşturma ve cevap üretimi aşamalarından geçer.*

Modülün geri kalanı her aşamayı detaylarıyla kod ve diyagramlarla açıklar.

### Bu Eğitim Hangi RAG Yaklaşımını Kullanıyor?

LangChain4j, RAG uygulamak için üç farklı soyutlanma seviyesi sunar. Aşağıdaki diyagram yan yana karşılaştırır:

<img src="../../../translated_images/tr/rag-approaches.5b97fdcc626f1447.webp" alt="LangChain4j’de Üç RAG Yaklaşımı" width="800"/>

*Bu diyagram, LangChain4j’deki üç RAG yaklaşımını — Kolay, Yerel ve İleri Düzey — ana bileşenleri ve kullanım zamanları ile karşılaştırır.*

| Yaklaşım | Neyi Yapar | Dezavantajı |
|---|---|---|
| **Kolay RAG** | `AiServices` ve `ContentRetriever` aracılığıyla her şeyi otomatik bağlar. Bir arayüzü notasyonla işaretlersiniz, bir retriever eklersiniz; LangChain4j arka planda embedding, arama ve prompt oluşturmayı yapar. | Kod çok az, ama her adımın ne yaptığı görünmez. |
| **Yerel RAG** | Embedding modelini çağırır, depoyu arar, prompt’u kendiniz inşa eder, cevabı oluşturursunuz — adım adım açıkça. | Daha çok kod, ama her aşama görünür ve değiştirilebilir. |
| **İleri Düzey RAG** | Üretim kalitesinde pipeline’lar için plug-in yapılabilir sorgu dönüştürücüleri, yönlendiriciler, yeniden sıralayıcılar ve içerik ekleyiciler içeren `RetrievalAugmentor` çerçevesini kullanır. | Maksimum esneklik, ama çok daha karmaşık. |

**Bu eğitim Yerel yaklaşımı kullanır.** RAG hattının her aşaması — sorguyu embed etme, vektör deposunda arama, bağlamı oluşturma ve cevabı üretme — [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) içinde açıkça yazılmıştır. Bu kasıtlıdır: öğrenme kaynağı olarak kodun minimal olmasından çok her aşamanın görünmesi ve anlaşılması daha önemlidir. Parçaların nasıl birleştiğine alıştıktan sonra hızlı prototipler için Kolay RAG’a veya üretim sistemleri için İleri Düzey RAG’a geçebilirsiniz.

> **💡 Kolay RAG’i daha önce gördünüz mü?** [Hızlı Başlangıç modülü](../00-quick-start/README.md), Kolay RAG yaklaşımını kullanan bir Belge Soru-Cevap örneği içerir ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) — LangChain4j embedding, arama ve prompt oluşturmayı otomatik yapar. Bu modül, o pipeline’ı açarak her aşamayı kendiniz görüp kontrol etmenize olanak verir.

<img src="../../../translated_images/tr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Kolay RAG Pipeline - LangChain4j" width="800"/>

*Bu diyagram, `SimpleReaderDemo.java` içindeki Kolay RAG pipeline’ını gösterir. Bu modülde kullanılan Yerel yaklaşımla karşılaştırın: Kolay RAG embedding, arama ve prompt oluşturmayı `AiServices` ve `ContentRetriever` arkasına gizler — belgenizi yüklersiniz, retriever eklersiniz, cevaplar alırsınız. Yerel yaklaşımda ise her aşama (embed et, ara, bağlamı oluştur, üret) kendiniz çağırarak tam görünürlük ve kontrol sağlarsınız.*

## Nasıl Çalışır

Bu modülde RAG pipeline’ı, kullanıcı her soru sorduğunda sırasıyla çalışan dört aşamaya ayrılmıştır. İlk olarak, yüklenen belge **parçalanır ve yönetilebilir parçalara bölünür**. O parçalar ardından **vektör embedding'lerine** dönüştürülür ve matematiksel karşılaştırma için saklanır. Sorgu geldiğinde sistem **anlamsal arama** yaparak en ilgili parçaları bulur ve son olarak bunları bağlam olarak LLM'e geçer ve **cevap üretimi** gerçekleşir. Aşağıdaki bölümler her aşamayı gerçek kod ve diyagramlarla açıklar. İlk adıma bakalım.

### Belge İşleme

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Bir belge yüklediğinizde sistem onu çözümler (PDF veya düz metin), dosya adı gibi meta veriler ekler ve ardından modele uygun boyutta parçalara böler — modelin kontekst penceresine sığacak küçük parçalar. Bu parçalar arasında biraz örtüşme olur, böylece önemli bağlam sınırda kaybolmaz.

```java
// Yüklenen dosyayı ayrıştırın ve LangChain4j Belgesi içine sarın
Document document = Document.from(content, metadata);

// 30 token örtüşme ile 300 token'lık parçalara bölüştürün
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Aşağıdaki diyagram bunu görsel olarak gösterir. Her parça komşularıyla bazı token’ları paylaşır — 30 token’lık örtüşme önemli bağlamın kaçmasını engeller:

<img src="../../../translated_images/tr/document-chunking.a5df1dd1383431ed.webp" alt="Belge Parçalama" width="800"/>

*Bu diyagram, bir belgenin 300 token’lık parçalar halinde 30 token örtüşmeyle bölünmesini ve bağlamın parça sınırlarında korunduğunu gösterir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dosyasını açın ve sorun:  
> - "LangChain4j belgeleri nasıl parçalara ayırır ve örtüşme neden önemlidir?"  
> - "Farklı belge türleri için ideal parça boyutu nedir ve neden?"  
> - "Çok dilli veya özel formatlı belgelerle nasıl başa çıkarım?"

### Embedding Oluşturma

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Her parça, embedding denilen sayısal bir temsile dönüştürülür — yani anlamın sayılara çevrilmesi. Embedding modeli sohbet modeli gibi "akıllı" değildir; komutları takip edemez, mantık yürütemez veya soruları cevaplayamaz. Ama metni matematiksel bir uzaya haritalar; benzer anlamlar birbirine yakın konumlanır — “araba” “otomobil”e, “iade politikası” “paramı geri al”a yakın olur. Sohbet modeli insan gibi, embedding modeli ise üstün bir dosyalama sistemi gibidir.

<img src="../../../translated_images/tr/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Konsepti" width="800"/>

*Bu diyagram, embedding modelinin metni sayısal vektörlere dönüştürmesini ve benzer anlamları — örneğin “araba” ile “otomobil” — vektör uzayında birbirine yakın yerleştirmesini gösterir.*

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
  
Aşağıdaki sınıf diyagramı RAG pipeline’ındaki iki farklı akışı ve LangChain4j sınıflarını gösterir. **Alım akışı** (yükleme sırasında çalışır) belgeyi böler, parçaları embed eder ve `.addAll()` ile saklar. **Sorgu akışı** (kullanıcı soru sorduğunda çalışır) soruyu embed eder, `.search()` ile depoyu arar ve eşleşen bağlamı sohbet modeline verir. Her iki akış paylaşılan `EmbeddingStore<TextSegment>` arayüzüyle birleşir:

<img src="../../../translated_images/tr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Sınıfları" width="800"/>

*Bu diyagram, RAG pipeline’ındaki iki akışı — alım ve sorgu — ve bunların paylaşılan EmbeddingStore üzerinden nasıl birleştiğini gösterir.*

Embedding’ler depolandıktan sonra benzer içerik doğal olarak vektör uzayında kümelenir. Aşağıdaki görselleştirme ilgili konulardaki belgelerin yakın noktalarda toplandığını gösterir, bu da anlamsal aramayı mümkün kılar:

<img src="../../../translated_images/tr/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektör Embedding Uzayı" width="800"/>

*Bu görselleştirme, Teknik Dokümanlar, İş Kuralları ve SSS gibi konuların 3B vektör uzayında ayrı kümeler oluşturduğunu gösterir.*

Kullanıcı arama yaptığında sistem dört adım izler: belgeleri bir kez embed eder, sorguyu her aramada embed eder, sorgu vektörünü tüm saklanan vektörlerle kosinüs benzerliği kullanarak karşılaştırır ve en yüksek skorlu ilk-K parçayı döndürür. Aşağıdaki diyagram her adımı ve ilgili LangChain4j sınıflarını gösterir:

<img src="../../../translated_images/tr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Arama Adımları" width="800"/>

*Bu diyagram, embedding arama sürecindeki dört adımı gösterir: belgeleri embed et, sorguyu embed et, vektörleri kosinüs benzerliği ile karşılaştır, ve en iyi K sonucu döndür.*

### Anlamsal Arama

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Bir soru sorduğunuzda, soru da embedding'e çevrilir. Sistem, sizin sorunuzun embedding’i ile tüm belge parçalarının embedding’lerini karşılaştırır. En benzer anlamlara sahip parçaları bulur — sadece anahtar kelime eşleşmesi değil, gerçek anlamsal benzerlik.

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
  
Aşağıdaki diyagram, anlamsal aramayı geleneksel anahtar kelime aramasıyla karşılaştırır. “Araç” için bir anahtar kelime araması “arabalar ve kamyonlar” hakkındaki bir parçayı kaçırır; ama anlamsal arama aynı şeyi ifade ettiklerini anlar ve yüksek puanlı eşleşme olarak getirir:

<img src="../../../translated_images/tr/semantic-search.6b790f21c86b849d.webp" alt="Anlamsal Arama" width="800"/>

*Bu diyagram, anahtar kelime tabanlı arama ile anlamsal aramayı karşılaştırır; anlamsal arama tam eşleşme olmasa bile kavramsal olarak ilgili içeriği getirir.*

Altyapıda benzerlik kosinüs benzerliği kullanılarak ölçülür — aslında “bu iki ok aynı yöne mi bakıyor?” sorusu sorulur. İki parça tamamen farklı kelimeler kullanabilir, ama aynı şeyi ifade ediyorsa vektörleri aynı yöne bakar ve skorları 1.0’a yakındır:

<img src="../../../translated_images/tr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinüs Benzerliği" width="800"/>
*Bu diyagram, gömme vektörleri arasındaki açıyı kosinüs benzerliği olarak göstermektedir — daha hizalanmış vektörler 1.0'a daha yakın skor alır, bu da daha yüksek anlamsal benzerlik anlamına gelir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dosyasını açın ve sorun:
> - "Benzerlik araması gömme vektörleri ile nasıl çalışır ve skoru ne belirler?"
> - "Hangi benzerlik eşik değerini kullanmalıyım ve sonuçları nasıl etkiler?"
> - "İlgili belge bulunamadığında nasıl işlem yapmalıyım?"

### Yanıt Oluşturma

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

En alakalı parçalar, açık talimatlar, alınan bağlam ve kullanıcının sorusunu içeren yapılandırılmış bir isteme derlenir. Model, sadece önünde olan bu belirli parçaları okur ve buna dayanarak cevap verir — bu sayede kurgudan kaçınır.

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

Aşağıdaki diyagram bu derleme adımını gösterir — arama adımından en yüksek puan alan parçalar istem şablonuna yerleştirilir ve `OpenAiOfficialChatModel` dayanaklı bir yanıt üretir:

<img src="../../../translated_images/tr/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Bu diyagram, en yüksek puan alan parçaların yapılandırılmış bir isteme nasıl derlendiğini gösterir ve modelin verilerinizden dayanaklı bir yanıt üretmesini sağlar.*

## Uygulamayı Çalıştırma

**Dağıtımı doğrulayın:**

Ana dizinde Azure kimlik bilgileri içeren `.env` dosyasının var olduğundan emin olun (Modül 01 sırasında oluşturuldu):

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```

**Uygulamayı başlatın:**

> **Not:** Eğer Modül 01’den `./start-all.sh` komutuyla tüm uygulamaları zaten başlattıysanız, bu modül zaten 8081 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8081 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard Kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteyneri, tüm Spring Boot uygulamalarınızı yönetmek için görsel bir arabirim sağlayan Spring Boot Dashboard eklentisini içerir. Bunu VS Code’un sol tarafındaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulabilirsiniz.

Spring Boot Dashboard’dan şunları yapabilirsiniz:
- Çalışma alanındaki mevcut tüm Spring Boot uygulamalarını görmek
- Uygulamaları tek tıkla başlatmak/durdurmak
- Uygulama günlüklerini gerçek zamanlı izlemek
- Uygulama durumunu takip etmek

Bu modülü başlatmak için "rag" yazan satırın yanındaki oynat düğmesine tıklayın ya da tüm modülleri birden başlatın.

<img src="../../../translated_images/tr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Bu ekran görüntüsü, VS Code’daki Spring Boot Dashboard’u gösterir; burada uygulamaları görsel olarak başlatabilir, durdurabilir ve takip edebilirsiniz.*

**Seçenek 2: Shell komut dosyalarını kullanmak**

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
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Her iki komut dosyası da otomatik olarak kök `.env` dosyasından çevre değişkenlerini yükler ve JAR dosyaları yoksa derler.

> **Not:** Başlatmadan önce tüm modülleri manuel derlemeyi tercih ederseniz:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

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

Uygulama, belge yükleme ve soru sorma için bir web arayüzü sağlar.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tr/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Bu ekran görüntüsü, RAG uygulamasının kullanıcıda belgeleri yükleyip sorular sorabildiğiniz arayüzünü gösterir.*

### Belge Yükleme

Bir belge yükleyerek başlayın - test için TXT dosyaları en iyisidir. Bu dizinde LangChain4j özellikleri, RAG uygulaması ve en iyi uygulamalar hakkında bilgi içeren `sample-document.txt` adlı bir belge sağlanmıştır — sisteminizi test etmek için ideal.

Sistem belgenizi işler, parçalara böler ve her parça için gömme vektörleri oluşturur. Bu işlem, yükleme sırasında otomatik olarak gerçekleşir.

### Sorular Sorun

Şimdi belge içeriği ile ilgili spesifik sorular sorun. Belgede açıkça belirtilmiş gerçek bir şeyi deneyin. Sistem ilgili parçaları arar, bunları isteme dahil eder ve yanıt üretir.

### Kaynak Referanslarını Kontrol Edin

Her yanıtın, benzerlik skorları ile birlikte kaynak referanslarını içerdiğine dikkat edin. Bu skorlar (0 ile 1 arasında) her parçanın sorunuzla ne kadar ilgili olduğunu gösterir. Daha yüksek skorlar daha iyi eşleşmeyi ifade eder. Böylece yanıtı kaynak materyale karşı kontrol edebilirsiniz.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tr/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Bu ekran görüntüsü, oluşturulan yanıtı, kaynak referanslarını ve alınan her parçanın alakalı skorlarını gösterir.*

### Sorularla Deney Yapın

Farklı soru tiplerini deneyin:
- Spesifik gerçekler: "Ana konu nedir?"
- Karşılaştırmalar: "X ile Y arasındaki fark nedir?"
- Özetler: "Z hakkındaki temel noktaları özetle"

Sorunuzun belge içeriğiyle ne kadar eşleştiğine bağlı olarak benzerlik skorlarının nasıl değiştiğini gözlemleyin.

## Temel Kavramlar

### Parça Bölme Stratejisi

Belgeler, 30 token üst üste binme ile 300 tokenlık parçalara ayrılır. Bu denge, her parçanın anlamlı olmasını sağlarken, bir istemde birçok parçayı dahil edecek kadar küçük kalmasını sağlar.

### Benzerlik Puanları

Alınan her parça, kullanıcının sorusuyla ne kadar yakın eşleştiğini gösteren 0 ile 1 arasında bir benzerlik puanı ile birlikte gelir. Aşağıdaki diyagram puan aralıklarını ve sistemin sonuçları süzmek için bunları nasıl kullandığını görselleştirir:

<img src="../../../translated_images/tr/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Bu diyagram, 0'dan 1'e skor aralıklarını ve alakasız parçaları filtrelemek için kullanılan 0.5 minimum eşik değerini gösterir.*

Puanlar 0 ile 1 arasında değişir:
- 0.7-1.0: Çok ilgili, tam eşleşme
- 0.5-0.7: İlgili, iyi bağlam
- 0.5'in altında: Filtrelendi, çok alakasız

Sistem kalitenin garantilenmesi için sadece minimum eşiğin üzerindeki parçaları getirir.

Gömme vektörleri, anlam kümeleri temizce ayrıldığında iyi çalışır ama kör noktaları vardır. Aşağıdaki diyagram yaygın başarısızlık modlarını gösterir — çok büyük parçalar bulanık vektör üretiyor, çok küçük parçalar bağlamdan yoksun, belirsiz terimler birden çok kümeye işaret ediyor ve tam eşleşmeli aramalar (kimlikler, parça numaraları) gömme ile hiç çalışmıyor:

<img src="../../../translated_images/tr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Bu diyagram, yaygın gömme başarısızlık modlarını gösterir: çok büyük parçalar, çok küçük parçalar, çoklu kümelere işaret eden belirsiz terimler ve kimlik gibi tam eşleşmeli aramalar.*

### Bellek İçi Depolama

Bu modül basitlik için bellek içi depolama kullanır. Uygulamayı yeniden başlattığınızda yüklenen belgeler kaybolur. Prodüksiyon sistemleri Qdrant veya Azure AI Search gibi kalıcı vektör veritabanları kullanır.

### Bağlam Penceresi Yönetimi

Her modelin maksimum bir bağlam penceresi vardır. Büyük bir belgedeki tüm parçaları dahil edemezsiniz. Sistem, sınırlar içinde kalmak ve doğru yanıtlar için yeterli bağlam sağlamak üzere en alakalı ilk N parçayı (varsayılan 5) seçer.

## RAG'in Önemi

RAG her zaman doğru yaklaşım değildir. Aşağıdaki karar rehberi, RAG'in değer kattığı durumlar ile daha basit yaklaşımların — içeriği doğrudan isteme eklemek veya modelin yerleşik bilgisini kullanmak gibi — yeterli olduğu durumları belirlemenize yardımcı olur:

<img src="../../../translated_images/tr/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Bu diyagram, RAG'in değer kattığı durumları ve basit yaklaşımların yeterli olduğu durumları gösteren bir karar rehberidir.*

**RAG’i şu durumlarda kullanın:**
- Özel belgelerle ilgili soruları yanıtlarken
- Bilgiler sık sık değişiyorsa (politikalar, fiyatlar, spesifikasyonlar)
- Doğruluk için kaynak gösterimi gerekiyorsa
- İçerik tek bir isteme sığmayacak kadar büyükse
- Doğrulanabilir, dayanaklı yanıtlar istiyorsanız

**RAG’i şu durumlarda kullanmayın:**
- Sorular modelin zaten sahip olduğu genel bilgi gerektiriyorsa
- Gerçek zamanlı veri gerekiyorsa (RAG sadece yüklenen belgelerle çalışır)
- İçerik doğrudan isteme sığacak kadar küçükse

## Sonraki Adımlar

**Sonraki Modül:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Gezinme:** [← Önceki: Modül 02 - İstem Mühendisliği](../02-prompt-engineering/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 04 - Araçlar →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba gösterilmekle birlikte, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda ortaya çıkabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->