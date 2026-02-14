<img src="../../translated_images/tr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Çok Dilli Destek

#### GitHub Aksiyonu ile Desteklenmektedir (Otomatik ve Her Zaman Güncel)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](./README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Yerel Olarak Klonlamayı Tercih Ediyor musunuz?**
>
> Bu depo 50'den fazla dil çevirisini içerir ve bu da indirme boyutunu önemli ölçüde artırır. Çeviriler olmadan klonlamak için seyreltik kontrol (sparse checkout) kullanın:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> Bu, kursu tamamlamak için ihtiyacınız olan her şeyi çok daha hızlı bir indirme ile sağlar.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j Başlangıç Rehberi

LangChain4j ve Azure OpenAI GPT-5.2 ile temel sohbetten AI ajanlarına kadar AI uygulamaları oluşturmak için bir kurs.

**LangChain4j'ye yeni misiniz?** Anahtar terimler ve kavramlar için [Sözlüğe](docs/GLOSSARY.md) göz atın.

## İçindekiler

1. [Hızlı Başlangıç](00-quick-start/README.md) - LangChain4j ile hemen başlayın
2. [Giriş](01-introduction/README.md) - LangChain4j'nın temellerini öğrenin
3. [Prompt Mühendisliği](02-prompt-engineering/README.md) - Etkili prompt tasarımında ustalaşın
4. [RAG (Retrieve-Augmented Generation)](03-rag/README.md) - Akıllı bilgi tabanlı sistemler kurun
5. [Araçlar](04-tools/README.md) - Harici araçlar ve basit asistanlarla entegrasyon
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) ve Agentic modüllerle çalışın
---

## Öğrenme Yolu

> **Hızlı Başlangıç**

1. Bu depoyu kendi GitHub hesabınıza fork yapın
2. **Code** → **Codespaces** sekmesine tıklayın → **...** → **Seçeneklerle Yeni...** seçeneğini seçin
3. Varsayılanları kullanın – bu, kurs için oluşturulan Geliştirme konteynerini seçecektir
4. **Codespace oluştur** butonuna tıklayın
5. Ortamın hazır olması için 5-10 dakika bekleyin
6. Hemen başlamak için [Hızlı Başlangıç](./00-quick-start/README.md) sayfasına gidin!

Modülleri tamamladıktan sonra LangChain4j testi kavramlarını uygulamalı görmek için [Test Rehberini](docs/TESTING.md) inceleyin.

> **Not:** Bu eğitim hem GitHub Modellerini hem de Azure OpenAI'yi kullanmaktadır. [Hızlı Başlangıç](00-quick-start/README.md) modülü GitHub Modellerini kullanır (Azure aboneliği gerekmez), modüller 1-5 ise Azure OpenAI kullanır. Henüz bir hesabınız yoksa [ÜCRETSİZ Azure hesabı](https://aka.ms/azure-free-account) ile başlayabilirsiniz.


## GitHub Copilot ile Öğrenme

Kodlamaya hızlı başlamak için bu projeyi bir GitHub Codespace içinde veya sağlanan devcontainer ile yerel IDE'nizde açın. Bu kursta kullanılan devcontainer, yapay zeka eşliğinde programlama için önceden yapılandırılmış GitHub Copilot ile gelir.

Her kod örneği, GitHub Copilot'a sorabileceğiniz ve anlayışınızı derinleştirebileceğiniz önerilen soruları içerir. Aşağıdaki yerlerde 💡/🤖 şeklinde ipuçları arayın:

- **Java dosya başlıkları** - Her örneğe özgü sorular
- **Modül README dosyaları** - Kod örneklerinden sonra keşif soruları

**Nasıl kullanılır:** Herhangi bir kod dosyasını açın ve Copilot’a önerilen soruları sorun. Copilot kod tabanının tamamını bilir ve açıklayabilir, genişletebilir, alternatifler önerebilir.

Daha fazlasını mı öğrenmek istiyorsunuz? [Yapay Zeka Eşli Programlama için Copilot](https://aka.ms/GitHubCopilotAI) sayfasına göz atın.


## Ek Kaynaklar

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j Başlangıç](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js Başlangıç](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain Başlangıç](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Ajanlar
[![AZD Başlangıç](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI Başlangıç](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP Başlangıç](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Ajanları Başlangıç](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### Üretken AI Serisi
[![Üretken AI Başlangıç](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Üretken AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Üretken AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Üretken AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---

### Temel Öğrenme
[![ML Başlangıç](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Veri Bilimi Başlangıç](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Başlangıç](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Siber Güvenlik Başlangıç](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Geliştirme Başlangıç](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için Nesnelerin İnterneti](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için XR Geliştirme](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Serisi
[![Yapay Zeka Eşli Programlama için Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET için Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Macerası](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Yardım Alma

Takılırsanız veya AI uygulamaları oluşturmayla ilgili sorularınız olursa, katılın:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Ürün geri bildirimi veya hata bildirimi yapmak isterseniz, ziyaret edin:

[![Azure AI Foundry Geliştirici Forumu](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Lisans

MIT Lisansı - Ayrıntılar için [LİSANS](../../LICENSE) dosyasına bakınız.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:  
Bu belge, AI çeviri servisi [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlıklar içerebileceğini lütfen unutmayınız. Orijinal belge, ana dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucunda oluşabilecek yanlış anlamalar veya yorum hatalarından sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->