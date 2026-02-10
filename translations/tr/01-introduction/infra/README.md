# LangChain4j için Azure Altyapısına Başlangıç

## İçindekiler

- [Ön Koşullar](../../../../01-introduction/infra)
- [Mimari](../../../../01-introduction/infra)
- [Oluşturulan Kaynaklar](../../../../01-introduction/infra)
- [Hızlı Başlangıç](../../../../01-introduction/infra)
- [Yapılandırma](../../../../01-introduction/infra)
- [Yönetim Komutları](../../../../01-introduction/infra)
- [Maliyet Optimizasyonu](../../../../01-introduction/infra)
- [İzleme](../../../../01-introduction/infra)
- [Sorun Giderme](../../../../01-introduction/infra)
- [Altyapıyı Güncelleme](../../../../01-introduction/infra)
- [Temizleme](../../../../01-introduction/infra)
- [Dosya Yapısı](../../../../01-introduction/infra)
- [Güvenlik Önerileri](../../../../01-introduction/infra)
- [Ek Kaynaklar](../../../../01-introduction/infra)

Bu dizin, Azure OpenAI kaynaklarının dağıtımı için Bicep ve Azure Developer CLI (azd) kullanarak altyapıyı kod olarak (IaC) içerir.

## Ön Koşullar

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (sürüm 2.50.0 veya daha yenisi)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (sürüm 1.5.0 veya daha yenisi)
- Kaynak oluşturma izinlerine sahip bir Azure aboneliği

## Mimari

**Basitleştirilmiş Yerel Geliştirme Kurulumu** - Sadece Azure OpenAI dağıtılır, tüm uygulamalar yerel olarak çalıştırılır.

Altyapı aşağıdaki Azure kaynaklarını dağıtır:

### AI Servisleri
- **Azure OpenAI**: İki model dağıtımı ile Bilişsel Hizmetler:
  - **gpt-5.2**: Sohbet tamamlama modeli (20K TPM kapasitesi)
  - **text-embedding-3-small**: RAG için yerleştirme modeli (20K TPM kapasitesi)

### Yerel Geliştirme
Tüm Spring Boot uygulamaları makinenizde yerel olarak çalışır:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Oluşturulan Kaynaklar

| Kaynak Türü    | Kaynak Adı Deseni         | Amaç                   |
|----------------|---------------------------|------------------------|
| Kaynak Grubu   | `rg-{environmentName}`    | Tüm kaynakları içerir  |
| Azure OpenAI   | `aoai-{resourceToken}`    | AI model barındırma    |

> **Not:** `{resourceToken}` abonelik kimliği, ortam adı ve konumdan türetilmiş benzersiz bir dizgedir

## Hızlı Başlangıç

### 1. Azure OpenAI Dağıtın

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

İstendiğinde:
- Azure aboneliğinizi seçin
- Bir konum seçin (önerilen: `eastus2`, GPT-5.2 erişimi için)
- Ortam adını onaylayın (varsayılan: `langchain4j-dev`)

Bu işlem şunları oluşturacak:
- GPT-5.2 ve text-embedding-3-small içeren Azure OpenAI kaynağı
- Bağlantı detaylarının çıktısı

### 2. Bağlantı Detaylarını Alın

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Şunları gösterir:
- `AZURE_OPENAI_ENDPOINT`: Azure OpenAI uç nokta URL'niz
- `AZURE_OPENAI_KEY`: Kimlik doğrulama için API anahtarı
- `AZURE_OPENAI_DEPLOYMENT`: Sohbet modeli adı (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Yerleştirme modeli adı

### 3. Uygulamaları Yerel Olarak Çalıştırın

`azd up` komutu, kök dizinde gerekli tüm ortam değişkenlerini içeren bir `.env` dosyası otomatik olarak oluşturur.

**Önerilen:** Tüm web uygulamalarını başlatın:

**Bash:**
```bash
# Kök dizinden
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Kök dizinden
cd ../..
.\start-all.ps1
```

Ya da tek bir modül başlatın:

**Bash:**
```bash
# Örnek: Yalnızca giriş modülünü başlatın
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Örnek: Sadece giriş modülünü başlatın
cd ../01-introduction
.\start.ps1
```

Her iki betik de `azd up` tarafından oluşturulan kök `.env` dosyasından ortam değişkenlerini otomatik olarak yükler.

## Yapılandırma

### Model Dağıtımlarını Özelleştirme

Model dağıtımlarını değiştirmek için `infra/main.bicep` dosyasını düzenleyin ve `openAiDeployments` parametresini değiştirin:

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

Kullanılabilir modeller ve sürümler: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure Bölgelerini Değiştirme

Farklı bir bölgede dağıtım yapmak için `infra/main.bicep` dosyasını düzenleyin:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 erişimini kontrol edin: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep dosyalarında değişiklik yaptıktan sonra altyapıyı güncellemek için:

**Bash:**
```bash
# ARM şablonunu yeniden oluştur
az bicep build --file infra/main.bicep

# Değişiklikleri önizle
azd provision --preview

# Değişiklikleri uygula
azd provision
```

**PowerShell:**
```powershell
# ARM şablonunu yeniden oluştur
az bicep build --file infra/main.bicep

# Değişiklikleri önizle
azd provision --preview

# Değişiklikleri uygula
azd provision
```

## Temizleme

Tüm kaynakları silmek için:

**Bash:**
```bash
# Tüm kaynakları sil
azd down

# Ortam dahil her şeyi sil
azd down --purge
```

**PowerShell:**
```powershell
# Tüm kaynakları sil
azd down

# Ortam dahil her şeyi sil
azd down --purge
```

**Uyarı**: Bu, Azure’daki tüm kaynakları kalıcı olarak silecektir.

## Dosya Yapısı

## Maliyet Optimizasyonu

### Geliştirme/Test
Geliştirme/test ortamları için maliyetleri düşürebilirsiniz:
- Azure OpenAI için Standart katman (S0) kullanın
- `infra/core/ai/cognitiveservices.bicep`’de kapasiteyi 20K yerine 10K TPM olarak ayarlayın
- Kullanılmadığında kaynakları silin: `azd down`

### Prodüksiyon
Prodüksiyon için:
- Kullanıma göre OpenAI kapasitesini artırın (50K+ TPM)
- Daha yüksek kullanılabilirlik için bölge(yer) çoğaltmayı etkinleştirin
- Doğru izleme ve maliyet uyarıları kurun

### Maliyet Tahmini
- Azure OpenAI: Token başına ödeme (giriş + çıkış)
- GPT-5.2: 1M token başına yaklaşık 3-5$
- text-embedding-3-small: 1M token başına yaklaşık 0.02$

Fiyat hesaplayıcı: https://azure.microsoft.com/pricing/calculator/

## İzleme

### Azure OpenAI Metriklerini Görüntüleme

Azure Portal → OpenAI kaynağınız → Metrikler:
- Token Bazlı Kullanım
- HTTP İstek Oranı
- Yanıt Süresi
- Aktif Tokenlar

## Sorun Giderme

### Sorun: Azure OpenAI alt etki alanı adı çakışması

**Hata Mesajı:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Nedeni:**
Abonelik/ortamdan türetilen alt etki alanı adı zaten kullanılıyor olabilir, muhtemelen tam silinmemiş önceki bir dağıtımdan dolayı.

**Çözüm:**
1. **Seçenek 1 - Farklı bir ortam adı kullanın:**
   
   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **Seçenek 2 - Azure Portal üzerinden manuel dağıtım:**
   - Azure Portal → Kaynak oluştur → Azure OpenAI
   - Kaynağınız için benzersiz bir ad seçin
   - Aşağıdaki modelleri dağıtın:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG modülleri için)
   - **Önemli:** Dağıtım adlarınız `.env` yapılandırmasıyla eşleşmelidir
   - Dağıtımdan sonra "Anahtarlar ve Uç Nokta" bölümünden uç noktanızı ve API anahtarınızı alın
   - Proje kökünde aşağıdaki gibi bir `.env` dosyası oluşturun:
     
     **Örnek `.env` dosyası:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Model Dağıtım Adlandırma Kuralları:**
- Basit ve tutarlı adlar kullanın: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Dağıtım adları `.env` dosyasındaki konfigürasyonla tam uyuşmalıdır
- Yaygın hata: Modeli bir adla oluşturup kodda farklı adlarla referans verme

### Sorun: Seçilen bölgede GPT-5.2 kullanılamıyor

**Çözüm:**
- GPT-5.2 erişimi olan bir bölge seçin (ör. eastus2)
- Erişilebilirliği kontrol edin: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Sorun: Dağıtım için yetersiz kota

**Çözüm:**
1. Azure Portal üzerinden kota artışı isteyin
2. Ya da `main.bicep` dosyasında daha düşük kapasite kullanın (örneğin kapasite: 10)

### Sorun: Yerel çalıştırmada "Kaynak bulunamadı"

**Çözüm:**
1. Dağıtımı doğrulayın: `azd env get-values`
2. Uç nokta ve anahtarın doğru olduğundan emin olun
3. Azure Portal’da kaynak grubunun varlığını kontrol edin

### Sorun: Kimlik doğrulama başarısız oldu

**Çözüm:**
- `AZURE_OPENAI_API_KEY` ortam değişkeninin doğru ayarlandığını kontrol edin
- Anahtar 32 karakterlik onaltılık bir dizgi olmalıdır
- Gerekirse Azure Portal’dan yeni anahtar alın

### Dağıtım Hataları

**Sorun**: `azd provision` kota veya kapasite hataları veriyor

**Çözüm:** 
1. Farklı bir bölge deneyin - Bölgeleri yapılandırmak için [Azure Bölgelerini Değiştirme](../../../../01-introduction/infra) bölümüne bakın
2. Azure OpenAI kotanızın olduğundan emin olun:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Uygulama Bağlanmıyor

**Sorun**: Java uygulaması bağlantı hataları veriyor

**Çözüm**:
1. Ortam değişkenlerinin dışa aktarıldığını doğrulayın:
   
   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. Uç nokta formatının doğru olduğundan emin olun (`https://xxx.openai.azure.com` olmalı)
3. Azure Portal’dan alınan birincil veya ikincil API anahtarını kullandığınızdan emin olun

**Sorun**: Azure OpenAI’den 401 Yetkisiz hatası

**Çözüm**:
1. Azure Portal → Anahtarlar ve Uç Nokta'dan yeni bir API anahtarı alın
2. `AZURE_OPENAI_API_KEY` ortam değişkenini tekrar dışa aktarın
3. Model dağıtımlarının tamamlandığını doğrulayın (Azure Portal kontrolü)

### Performans Sorunları

**Sorun**: Yavaş yanıt süreleri

**Çözüm**:
1. Azure Portal metriklerinde OpenAI token kullanımı ve kısıtlamayı kontrol edin
2. Limitlere ulaşıyorsanız TPM kapasitesini artırın
3. Daha yüksek mantık çalışma seviyesi (düşük/orta/yüksek) kullanmayı düşünebilirsiniz

## Altyapıyı Güncelleme

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```

## Güvenlik Önerileri

1. **API anahtarlarını asla commit etmeyin** - Ortam değişkenleri kullanın
2. **Yerelde .env dosyaları kullanın** - `.env` dosyasını `.gitignore`a ekleyin
3. **Anahtarları düzenli döndürün** - Azure Portal’dan yeni anahtarlar oluşturun
4. **Erişimi sınırlandırın** - Azure RBAC kullanarak kaynak erişimini yönetin
5. **Kullanımı izleyin** - Azure Portal’da maliyet uyarıları kurun

## Ek Kaynaklar

- [Azure OpenAI Servis Dokümantasyonu](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Model Dokümantasyonu](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Dokümantasyonu](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Dokümantasyonu](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Resmi Entegrasyonu](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Destek

Sorunlar için:
1. Yukarıdaki [sorun giderme bölümü](../../../../01-introduction/infra) kontrol edin
2. Azure Portal’da Azure OpenAI hizmet durumu inceleyin
3. Depoda bir sorun açın

## Lisans

Detaylar için kök dizindeki [LICENSE](../../../../LICENSE) dosyasına bakınız.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, yapay zeka çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanılması sonucu doğabilecek herhangi bir yanlış anlama veya yorum hatasından sorumlu tutulamayız.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->