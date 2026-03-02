# Modül 03: RAG (Geri Getirmeyle Desteklenen Üretim)

## İçindekiler

- [Video Gezdirmesi](../../../03-rag)
- [Neler Öğreneceksiniz](../../../03-rag)
- [Ön Koşullar](../../../03-rag)
- [RAG'ın Anlaşılması](../../../03-rag)
  - [Bu Eğitim Hangi RAG Yaklaşımını Kullanıyor?](../../../03-rag)
- [Nasıl Çalışır](../../../03-rag)
  - [Belge İşleme](../../../03-rag)
  - [Gömmeler Oluşturma](../../../03-rag)
  - [Anlamsal Arama](../../../03-rag)
  - [Cevap Üretme](../../../03-rag)
- [Uygulamayı Çalıştırın](../../../03-rag)
- [Uygulamayı Kullanma](../../../03-rag)
  - [Belge Yükleme](../../../03-rag)
  - [Sorular Sorun](../../../03-rag)
  - [Kaynak Referanslarını Kontrol Etme](../../../03-rag)
  - [Sorularla Deney Yapma](../../../03-rag)
- [Anahtar Kavramlar](../../../03-rag)
  - [Parçalama Stratejisi](../../../03-rag)
  - [Benzerlik Skorları](../../../03-rag)
  - [Bellek içi Depolama](../../../03-rag)
  - [Bağlam Penceresi Yönetimi](../../../03-rag)
- [RAG Ne Zaman Önemlidir](../../../03-rag)
- [Sonraki Adımlar](../../../03-rag)

## Video Gezdirmesi

Bu modülle başlamayı anlatan canlı oturumu izleyin:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="LangChain4j ile RAG - Canlı Oturum" width="800"/></a>

## Neler Öğreneceksiniz

Önceki modüllerde, yapay zeka ile nasıl sohbet edileceğini ve istemlerinizi nasıl yapılandıracağınızı öğrendiniz. Ancak temel bir sınırlama vardır: dil modelleri yalnızca eğitim sırasında öğrendiklerini bilir. Şirketinizin politikaları, proje dokümantasyonunuz veya eğitilmediği herhangi bir bilgi hakkında soruları cevaplayamazlar.

RAG (Geri Getirmeyle Desteklenen Üretim) bu sorunu çözer. Modelinize bilgilerinizi öğretmeye çalışmak (ki bu pahalı ve pratik değildir) yerine, belgelere göz atma yeteneği verirsiniz. Bir kişi soru sorduğunda, sistem ilgili bilgileri bulur ve isteme ekler. Model ardından bu alınan bağlama dayanarak yanıt verir.

RAG'ı modele referans kütüphanesi vermek gibi düşünebilirsiniz. Bir soru sorulduğunda sistem:

1. **Kullanıcı Sorgusu** – Siz bir soru sorarsınız  
2. **Gömme** – Sorunuz vektöre dönüştürülür  
3. **Vektör Araması** – Benzer belge parçalarını bulur  
4. **Bağlam Montajı** – İlgili parçaları isteme ekler  
5. **Yanıt** – LLM, bağlama dayanarak bir cevap üretir

Bu, modelin yanıtlarını eğitim bilgisine dayanmak yerine gerçek verilerinize dayandırır ve uydurmasına engel olur.

## Ön Koşullar

- [Modül 00 - Hızlı Başlangıç](../00-quick-start/README.md) tamamlanmış olması (bu modülde daha sonra referans verilen Kolay RAG örneği için)  
- [Modül 01 - Giriş](../01-introduction/README.md) tamamlanmış olması (Azure OpenAI kaynakları dağıtılmış, özellikle `text-embedding-3-small` gömme modeli dahil)  
- Kök dizinde Azure kimlik bilgileri içeren `.env` dosyası (Modül 01'de `azd up` komutuyla oluşturulur)

> **Not:** Eğer Modül 01 tamamlanmadıysa, önce oradaki dağıtım talimatlarını izleyin. `azd up` komutu bu modülün kullandığı GPT sohbet modeli ve gömme modelini dağıtır.

## RAG'ın Anlaşılması

Aşağıdaki diyagram temel kavramı gösterir: modelin sadece eğitim verisine dayanmak yerine, her cevap üretmeden önce belgelerinizi danışabileceği bir referans kitaplığı sağlanır.

<img src="../../../translated_images/tr/what-is-rag.1f9005d44b07f2d8.webp" alt="RAG Nedir" width="800"/>

*Bu diyagram standart bir LLM (eğitim verisinden tahmin yapan) ile RAG destekli bir LLM (önce belgelerinize danışan) arasındaki farkı gösterir.*

Parçalar uçtan uca nasıl bağlanır? Kullanıcı sorusu dört aşamadan geçer — gömme, vektör arama, bağlam montajı ve cevap üretimi — her biri öncekinin üzerine inşa edilir:

<img src="../../../translated_images/tr/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Mimarisi" width="800"/>

*Bu diyagram uçtan uca RAG hattını gösterir — kullanıcı sorgusu gömme, vektör arama, bağlam montajı ve cevap üretiminden geçer.*

Modülün geri kalan kısmı, çalıştırıp değiştirebileceğiniz kodlarla her aşamayı detaylı şekilde açıklar.

### Bu Eğitim Hangi RAG Yaklaşımını Kullanıyor?

LangChain4j, farklı soyutlama seviyelerinde RAG uygulamak için üç yol sağlar. Aşağıdaki diyagram bunları yan yana kıyaslar:

<img src="../../../translated_images/tr/rag-approaches.5b97fdcc626f1447.webp" alt="LangChain4j'deki Üç RAG Yaklaşımı" width="800"/>

*Bu diyagram, Easy, Native ve Advanced olmak üzere üç LangChain4j RAG yaklaşımını — temel bileşenleri ve ne zaman kullanılacaklarını — karşılaştırır.*

| Yaklaşım | Ne Yapar | Takas |
|---|---|---|
| **Kolay RAG** | Her şeyi `AiServices` ve `ContentRetriever` üzerinden otomatik bağlar. Bir arayüzü açıklarsınız, bir retriever eklersiniz, LangChain4j gömme, arama ve istem montajını perde arkasında halleder. | Minimum kod, ancak her adımda neler olduğunu görmezsiniz. |
| **Yerel RAG** | Gömme modelini çağırır, depoyu arar, istemi oluşturur ve cevabı kendiniz üretirsiniz — her adım açıkça yazılır. | Daha fazla kod, ancak her aşama görünür ve değiştirilebilir. |
| **İleri RAG** | Üretim düzeyinde boru hatları için takılabilir sorgu dönüştürücüler, yönlendiriciler, yeniden sıralayıcılar ve içerik enjektörleri içeren `RetrievalAugmentor` framework'ünü kullanır. | En fazla esneklik, fakat önemli ölçüde daha karmaşık. |

**Bu eğitim Yerel (Native) yaklaşımı kullanır.** RAG hattının her adımı — sorguyu gömme, vektör dükkanında arama, bağlamı monte etme ve cevabı üretme — [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dosyasında açıkça yazılmıştır. Bu kasıtlıdır: öğrenme kaynağı olarak, kodun minimize edilmesinden çok her aşamayı görmek ve anlamak önemlidir. Parçaların nasıl oturduğuna alıştıktan sonra hızlı prototipler için Kolay RAG'a veya üretim sistemleri için İleri RAG'a geçebilirsiniz.

> **💡 Kolay RAG'ı daha önce gördünüz mü?** [Hızlı Başlangıç modülü](../00-quick-start/README.md) Kolay RAG yaklaşımını kullanan Bir Belge Soru-Cevap örneği (`SimpleReaderDemo.java`) içerir — LangChain4j gömme, arama ve istem montajını otomatik olarak halleder. Bu modül, o hattı açarak her aşamanın kendiniz tarafından görülebilmesini ve kontrol edilmesini sağlar.

Aşağıdaki diyagram, Hızlı Başlangıç örneğinden Kolay RAG hattını gösterir. Gördüğünüz gibi `AiServices` ve `EmbeddingStoreContentRetriever` tüm karmaşıklığı gizler — belgeyi yüklersiniz, retriever eklersiniz ve cevap alırsınız. Bu modüldeki Yerel yaklaşım, o gizli adımların her birini açar:

<img src="../../../translated_images/tr/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Kolay RAG Hattı - LangChain4j" width="800"/>

*Bu diyagram `SimpleReaderDemo.java` dosyasından Kolay RAG hattını gösterir. Bu modülde kullanılan Yerel yaklaşım ile kıyaslayın: Kolay RAG gömme, retrieval ve istem montajını `AiServices` ve `ContentRetriever` arkasına gizler — belgeyi yüklersiniz, retriever eklersiniz, cevap alırsınız. Bu modüldeki Yerel yaklaşım ise hattı açar, böylece her aşamayı (gömme, arama, bağlam oluşturma, üretim) kendiniz çağırır ve tam görünürlük ile kontrol sağlar.*

## Nasıl Çalışır

Bu modüldeki RAG hattı, bir kullanıcı her soru sorduğunda ardışık olarak çalışan dört aşamaya ayrılır. İlk olarak, yüklenen belge **parçalanır ve bölünür** — modelin bağlam penceresine sığacak küçük parçalara. Sonra bu parçalar **vektör gömmelerine** dönüştürülür ve matematiksel karşılaştırmaya hazır şekilde saklanır. Sorgu geldiğinde, sistem en ilgili parçaları bulmak için **anlamsal arama** yapar ve son olarak bu parçaları bağlam olarak LLM'ye **cevap oluşturma** için verir. Aşağıdaki bölümler her aşamayı gerçek kod ve diyagramlarla açıklar. İlk adıma bakalım.

### Belge İşleme

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Bir belge yüklediğinizde, sistem onu (PDF veya düz metin) ayrıştırır, dosya adı gibi meta veriler ekler ve sonra modele kolayca sığacak şekilde küçük parçalara böler. Bu parçalar, sınırda bağlam kaybı olmaması için biraz üst üste binerler.

```java
// Yüklenen dosyayı ayrıştırın ve LangChain4j Belgesi içinde sarın
Document document = Document.from(content, metadata);

// 30 token örtüşme ile 300 token parçalarına bölün
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Aşağıdaki diyagram bunu görsel olarak gösterir. Her parçanın komşularıyla 30 token kadar üst üste binmesi, önemli bağlamın kaybolmamasını sağlar:

<img src="../../../translated_images/tr/document-chunking.a5df1dd1383431ed.webp" alt="Belge Parçalama" width="800"/>

*Bu diyagram, belgeyi 30 token üst üste binmeli 300 tokenlık parçalara bölünürken bağlamın korunduğunu gösterir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) dosyasını açın ve sorun:  
> - "LangChain4j belgeleri parçalara nasıl ayırıyor ve üst üste binme neden önemli?"  
> - "Farklı belge türleri için en uygun parça boyutu nedir ve neden?"  
> - "Çok dilli veya özel biçimlendirme içeren belgelerle nasıl başa çıkabilirim?"

### Gömmeler Oluşturma

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Her parça, gömme denilen sayısal bir temsile dönüştürülür — temelde anlamdan sayıya çeviri. Gömme modeli, sohbet modeli gibi "zeki" değildir; talimatları takip edemez, mantık yürütemez veya soru cevaplayamaz. Söyleyebileceği şey, benzer anlamların yakın vektörler aldığı bir matematiksel uzay yaratmaktır — "araba" ve "otomobil" yan yana, "para iade politikası" ve "paramı geri al" de yakın. Sohbet modeli, konuştuğunuz bir kişi gibidir; gömme modeli ise çok iyi organize edilmiş bir dosyalama sistemidir.

Aşağıdaki diyagram bu konsepti görselleştirir — metin girer, sayısal vektörler çıkar, benzer anlamlar vektör uzayında yakın yerde toplanır:

<img src="../../../translated_images/tr/embedding-model-concept.90760790c336a705.webp" alt="Gömme Modeli Konsepti" width="800"/>

*Bu diyagram, gömme modelinin metni sayısal vektörlere çevirdiğini ve "araba" ile "otomobil" gibi benzer anlamları vektör uzayında yakınlaştırdığını gösterir.*

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
  
Aşağıdaki sınıf diyagramı, RAG hattında iki ayrı akışı ve LangChain4j sınıflarını gösterir. **Alım akışı** (yükleme zamanında bir kez çalışır) belgeyi böler, parçaları gömer ve `.addAll()` ile depolar. **Sorgu akışı** (her kullanıcı sorusunda çalışır) soruyu gömer, `.search()` ile arama yapar ve bulunan bağlamı sohbet modeline verir. İki akış, ortak `EmbeddingStore<TextSegment>` arayüzünde buluşur:

<img src="../../../translated_images/tr/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Sınıfları" width="800"/>

*Bu diyagram, RAG hattındaki iki akışı — alım ve sorgu — ve bunların ortak bir EmbeddingStore üzerinden nasıl bağlandığını gösterir.*

Gömmeler depolandıktan sonra benzer içerikler vektör uzayında doğal olarak kümelenir. Aşağıdaki görselleştirme, ilgili konuların yakın noktalarda olduğunu gösterir; bu da anlamsal aramayı mümkün kılar:

<img src="../../../translated_images/tr/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektör Gömme Uzayı" width="800"/>

*Bu görselleştirme, Teknik Dokümanlar, İş Kuralları ve SSS gibi konuların 3D vektör uzayında nasıl ayrı gruplar oluşturduğunu gösterir.*

Bir kullanıcı arama yaptığında sistem dört adım takip eder: belgeleri bir kez gömmek, her aramada sorguyu gömmek, sorgu vektörünü tüm kayıtlı vektörlerle kosinüs benzerliğiyle karşılaştırmak ve en yüksek puanlı üst-K parçaları döndürmek. Aşağıdaki diyagram her adımı ve ilgili LangChain4j sınıflarını gösterir:

<img src="../../../translated_images/tr/embedding-search-steps.f54c907b3c5b4332.webp" alt="Gömme Arama Adımları" width="800"/>

*Bu diyagram, gömme arama sürecinin dört aşamasını gösterir: belgeleri gömme, sorguyu gömme, vektörleri kosinüs benzerliği ile karşılaştırma ve en iyi sonuçları döndürme.*

### Anlamsal Arama

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Bir soru sorduğunuzda, sorunuz da gömme haline gelir. Sistem, soru gömesini tüm belge parçalarının gömeleriyle karşılaştırır. Sadece anahtar kelime eşleşmesi değil, gerçek anlamsal benzerlik arar ve en yakın anlamları bulur.

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
  
Aşağıdaki diyagram anlamsal arama ile geleneksel anahtar kelime arama arasındaki farkı gösterir. "Araç" anahtar kelimesiyle yapılan arama, "arabalar ve kamyonlar" ile ilgili bir parçayı atlar, ancak anlamsal arama bunların aynı anlama geldiğini anlar ve yüksek puanlı sonuç olarak döndürür:

<img src="../../../translated_images/tr/semantic-search.6b790f21c86b849d.webp" alt="Anlamsal Arama" width="800"/>

*Bu diyagram, anahtar kelime tabanlı arama ile anlamsal arama arasındaki farkı gösterir; anlamsal arama, tam anahtar kelime farklılığı olsa bile kavramsal olarak ilişkili içeriği getirir.*
Motorun altında, benzerlik kosinüs benzerliği kullanılarak ölçülür — temelde "bu iki ok aynı yöne mi işaret ediyor?" diye sorar. İki parça tamamen farklı kelimeler kullanabilir, ancak aynı şeyi ifade ediyorlarsa vektörleri aynı yöne işaret eder ve skorları 1.0'a yakın olur:

<img src="../../../translated_images/tr/cosine-similarity.9baeaf3fc3336abb.webp" alt="Kosinüs Benzerliği" width="800"/>

*Bu diyagram, gömme vektörler arasındaki açıyı kosinüs benzerliği olarak göstermektedir — daha hizalanmış vektörler 1.0'a daha yakın skor alır, bu da daha yüksek anlamsal benzerliği gösterir.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ile deneyin:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) dosyasını açın ve sorun:
> - "Benzerlik araması gömmelerle nasıl çalışır ve skoru ne belirler?"
> - "Hangi benzerlik eşiğini kullanmalıyım ve bu sonuçları nasıl etkiler?"
> - "İlgili belge bulunamadığında nasıl işlem yapmalıyım?"

### Yanıt Üretimi

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

En alakalı parçalar, açık talimatlar, alınan bağlam ve kullanıcının sorusunu içeren yapılandırılmış bir prompt oluşturmak için birleştirilir. Model bu belirli parçaları okur ve bu bilgiye dayanarak cevap üretir — sadece önündeki bilgiyi kullanabilir, bu halüsinasyonu önler.

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

Aşağıdaki diyagram bu birleştirmenin nasıl çalıştığını gösterir — arama adımından en yüksek skorlu parçalar prompt şablonuna eklenir ve `OpenAiOfficialChatModel` somut bir yanıt üretir:

<img src="../../../translated_images/tr/context-assembly.7e6dd60c31f95978.webp" alt="Bağlam Birleştirme" width="800"/>

*Bu diyagram, en yüksek skorlu parçaların nasıl yapılandırılmış bir prompt’a birleştirildiğini ve böylece modelin verilerinizden somut bir cevap üretmesini sağladığını gösterir.*

## Uygulamayı Çalıştırma

**Dağıtımı doğrulayın:**

`.env` dosyasının kök dizinde olduğunu ve Azure kimlik bilgilerini içerdiğini kontrol edin (Modül 01 sırasında oluşturuldu). Modül dizininden (`03-rag/`) bunu çalıştırın:

**Bash:**  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT gösterilmelidir
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT göstermeli
```
  
**Uygulamayı başlatın:**

> **Not:** Eğer kök dizinden `./start-all.sh` kullanarak tüm uygulamaları zaten başlattıysanız (Modül 01'de açıklandığı gibi), bu modül zaten 8081 portunda çalışıyor. Aşağıdaki başlatma komutlarını atlayabilir ve doğrudan http://localhost:8081 adresine gidebilirsiniz.

**Seçenek 1: Spring Boot Dashboard kullanımı (VS Code kullanıcıları için önerilir)**

Geliştirme konteynerinde, tüm Spring Boot uygulamalarını görsel olarak yönetmek için Spring Boot Dashboard uzantısı vardır. VS Code'un solundaki Aktivite Çubuğunda (Spring Boot simgesini arayın) bulabilirsiniz.

Spring Boot Dashboard’dan:
- Çalışma alanındaki tüm mevcut Spring Boot uygulamalarını görebilir
- Uygulamaları tek tıkla başlatabilir/durdurabilirsiniz
- Uygulama günlüklerini gerçek zamanlı izleyebilirsiniz
- Uygulama durumunu takip edebilirsiniz

Sadece "rag" karşısındaki oynat butonuna tıklayarak bu modülü başlatın veya tüm modülleri aynı anda başlatın.

<img src="../../../translated_images/tr/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Bu ekran görüntüsü VS Code'da Spring Boot Dashboard’u gösterir; burada uygulamaları başlatıp durdurabilir ve görsel olarak izleyebilirsiniz.*

**Seçenek 2: Shell betikleri kullanımı**

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
  
Veya sadece bu modülü başlatın:

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
  
Her iki betik de otomatik olarak kök `.env` dosyasından ortam değişkenlerini yükler ve JAR dosyaları yoksa derler.

> **Not:** Tüm modülleri elle derlemek isterseniz, başlatmadan önce:
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
  
## Uygulamanın Kullanımı

Uygulama, belge yükleme ve soru sorma için web arayüzü sağlar.

<a href="images/rag-homepage.png"><img src="../../../translated_images/tr/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Uygulama Arayüzü" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Bu ekran görüntüsü RAG uygulama arayüzünü gösterir; burada belgeler yüklenir ve sorular sorulur.*

### Belge Yükleme

Öncelikle bir belge yükleyin - TXT dosyaları test için en uygun olanlardır. Bu dizinde LangChain4j özellikleri, RAG uygulaması ve en iyi uygulamalar hakkında bilgi içeren `sample-document.txt` dosyası mevcuttur - sistemi test etmek için idealdir.

Sistem belgenizi işler, parçalara böler ve her parça için gömme (embedding) oluşturur. Bu otomatik olarak yükleme sırasında gerçekleşir.

### Sorular Sorun

Şimdi, belge içeriği hakkında spesifik sorular sorabilirsiniz. Belgedeki açıkça belirtilmiş gerçeklere yönelik sorular deneyin. Sistem ilgili parçaları arar, bunları prompt’a dahil eder ve cevap üretir.

### Kaynak Referanslarını Kontrol Edin

Her cevabın kaynak referansları ve benzerlik skorları içerdiğine dikkat edin. Bu skorlar (0 ile 1 arasında) her parçanın sorunuza ne kadar alakalı olduğunu gösterir. Yüksek skorlar daha iyi eşleşmeler anlamına gelir. Bu, cevabı kaynak materyal ile doğrulamanızı sağlar.

<a href="images/rag-query-results.png"><img src="../../../translated_images/tr/rag-query-results.6d69fcec5397f355.webp" alt="RAG Sorgu Sonuçları" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Bu ekran görüntüsü, oluşturulan cevapla birlikte sorgu sonuçlarını, kaynak referansları ve her alınan parçanın alaka skorlarını gösterir.*

### Sorularla Deneyler Yapın

Farklı soru türlerini deneyin:  
- Spesifik gerçekler: "Ana konu nedir?"  
- Karşılaştırmalar: "X ve Y arasındaki fark nedir?"  
- Özetler: "Z hakkında temel noktaları özetle"

Sorunuzun belge içeriğiyle ne kadar iyi eşleştiğine bağlı olarak alaka skorlarının nasıl değiştiğini gözlemleyin.

## Temel Kavramlar

### Parçalama Stratejisi

Belgeler, 30 token üst üste binmeyle 300 tokenlik parçalara bölünür. Bu denge, her parçanın anlamlı olacak kadar bağlama sahip olmasını ve prompt’a birden fazla parça dahil edilebilmesi için küçük kalmasını sağlar.

### Benzerlik Skorları

Her alınan parçaya, kullanıcının sorusuyla ne kadar yakın eşleştiğini gösteren 0 ile 1 arasında bir benzerlik skoru verilir. Aşağıdaki diyagram, skor aralıklarını ve sistemin bunları nasıl kullanarak sonuçları filtrelediğini görselleştirir:

<img src="../../../translated_images/tr/similarity-scores.b0716aa911abf7f0.webp" alt="Benzerlik Skorları" width="800"/>

*Bu diyagram, 0 ila 1 arasındaki skor aralıklarını, 0.5’lik minimum eşik değeriyle gösterir; bu eşik, alakasız parçaları filtreler.*

Skor aralıkları:  
- 0.7-1.0: Çok ilgili, tam eşleşme  
- 0.5-0.7: İlgili, iyi bağlam  
- 0.5’in altı: Filtrelenen, çok alakasız

Sistem kaliteyi sağlamak için sadece minimum eşiğin üzerindeki parçaları alır.

Gömme yöntemleri, anlamlar kategorize edildiğinde iyi çalışır, fakat bazı kör noktaları vardır. Aşağıdaki diyagram yaygın başarısızlık durumlarını gösterir — çok büyük parçalar bulanık vektörler üretir, çok küçük parçalar bağlam kaybına yol açar, belirsiz terimler birden fazla kümeye işaret eder ve tam eşleşme aramaları (kimlikler, parça numaraları) gömmelerle hiç çalışmaz:

<img src="../../../translated_images/tr/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Gömme Başarısızlık Modları" width="800"/>

*Bu diyagram yaygın gömme başarısızlık modlarını gösterir: Çok büyük veya çok küçük parçalar, birden çok kümeye işaret eden belirsiz terimler ve kimlik gibi tam eşleşme aramaları.*

### Bellek İçi Depolama

Bu modül basitlik için bellek içi depolama kullanır. Uygulamayı yeniden başlattığınızda, yüklenen belgeler kaybolur. Üretim sistemleri Qdrant veya Azure AI Search gibi kalıcı vektör veritabanları kullanır.

### Bağlam Penceresi Yönetimi

Her modelin maksimum bağlam penceresi vardır. Büyük bir belgeden tüm parçaları dahil edemezsiniz. Sistem, sınırları aşmadan doğru cevap verecek kadar bağlam sağlamak için en alakalı N parçayı (varsayılan 5) alır.

## RAG Neden Önemli?

RAG her zaman doğru yaklaşım değildir. Aşağıdaki karar rehberi, RAG’ın ne zaman değer kattığını ve ne zaman daha basit yaklaşımların — içeriği doğrudan prompt’a dahil etmek veya modelin yerleşik bilgisini kullanmak gibi — yeterli olduğunu belirlemenize yardımcı olur:

<img src="../../../translated_images/tr/when-to-use-rag.1016223f6fea26bc.webp" alt="RAG Ne Zaman Kullanılır?" width="800"/>

*Bu diyagram, RAG’ın ne zaman değer kattığını ve ne zaman daha basit yaklaşımların yeterli olduğunu gösteren karar rehberidir.*

## Sonraki Adımlar

**Sonraki Modül:** [04-tools - Araçlarla AI Ajanları](../04-tools/README.md)

---

**Gezinme:** [← Önceki: Modül 02 - Prompt Mühendisliği](../02-prompt-engineering/README.md) | [Ana Sayfaya Dön](../README.md) | [Sonraki: Modül 04 - Araçlar →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hatalar veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, kendi diliyle yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi tavsiye edilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlaşılmalardan veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->