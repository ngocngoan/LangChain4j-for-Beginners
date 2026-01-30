<img src="../../translated_images/vi/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Hỗ trợ đa ngôn ngữ

#### Hỗ trợ qua GitHub Action (Tự động & Luôn luôn cập nhật)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Tiếng Ả Rập](../ar/README.md) | [Tiếng Bengali](../bn/README.md) | [Tiếng Bulgaria](../bg/README.md) | [Tiếng Miến Điện (Myanmar)](../my/README.md) | [Tiếng Trung (Giản thể)](../zh-CN/README.md) | [Tiếng Trung (Phồn thể, Hồng Kông)](../zh-HK/README.md) | [Tiếng Trung (Phồn thể, Macau)](../zh-MO/README.md) | [Tiếng Trung (Phồn thể, Đài Loan)](../zh-TW/README.md) | [Tiếng Croatia](../hr/README.md) | [Tiếng Séc](../cs/README.md) | [Tiếng Đan Mạch](../da/README.md) | [Tiếng Hà Lan](../nl/README.md) | [Tiếng Estonia](../et/README.md) | [Tiếng Phần Lan](../fi/README.md) | [Tiếng Pháp](../fr/README.md) | [Tiếng Đức](../de/README.md) | [Tiếng Hy Lạp](../el/README.md) | [Tiếng Do Thái](../he/README.md) | [Tiếng Hindi](../hi/README.md) | [Tiếng Hungary](../hu/README.md) | [Tiếng Indonesia](../id/README.md) | [Tiếng Ý](../it/README.md) | [Tiếng Nhật](../ja/README.md) | [Tiếng Kannada](../kn/README.md) | [Tiếng Hàn](../ko/README.md) | [Tiếng Litva](../lt/README.md) | [Tiếng Mã Lai](../ms/README.md) | [Tiếng Malayalam](../ml/README.md) | [Tiếng Marathi](../mr/README.md) | [Tiếng Nepal](../ne/README.md) | [Tiếng Pidgin Nigeria](../pcm/README.md) | [Tiếng Na Uy](../no/README.md) | [Tiếng Ba Tư (Farsi)](../fa/README.md) | [Tiếng Ba Lan](../pl/README.md) | [Tiếng Bồ Đào Nha (Brazil)](../pt-BR/README.md) | [Tiếng Bồ Đào Nha (Bồ Đào Nha)](../pt-PT/README.md) | [Tiếng Punjabi (Gurmukhi)](../pa/README.md) | [Tiếng Rumani](../ro/README.md) | [Tiếng Nga](../ru/README.md) | [Tiếng Serbia (Chữ Kirin)](../sr/README.md) | [Tiếng Slovakia](../sk/README.md) | [Tiếng Slovenia](../sl/README.md) | [Tiếng Tây Ban Nha](../es/README.md) | [Tiếng Swahili](../sw/README.md) | [Tiếng Thụy Điển](../sv/README.md) | [Tiếng Tagalog (Philippines)](../tl/README.md) | [Tiếng Tamil](../ta/README.md) | [Tiếng Telugu](../te/README.md) | [Tiếng Thái](../th/README.md) | [Tiếng Thổ Nhĩ Kỳ](../tr/README.md) | [Tiếng Ukraina](../uk/README.md) | [Tiếng Urdu](../ur/README.md) | [Tiếng Việt](./README.md)

> **Ưu tiên sao chép local?**

> Kho lưu trữ này bao gồm hơn 50 bản dịch ngôn ngữ, làm tăng đáng kể kích thước tải xuống. Để sao chép mà không có bản dịch, hãy sử dụng sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Điều này cung cấp cho bạn tất cả những gì cần để hoàn thành khóa học với tốc độ tải xuống nhanh hơn nhiều.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j cho Người mới bắt đầu

Một khóa học xây dựng ứng dụng AI với LangChain4j và Azure OpenAI GPT-5, từ chat cơ bản đến agent AI.

**Mới với LangChain4j?** Xem [Thuật ngữ](docs/GLOSSARY.md) để biết định nghĩa các từ khóa và khái niệm chính.

## Mục lục

1. [Bắt đầu nhanh](00-quick-start/README.md) - Bắt đầu với LangChain4j
2. [Giới thiệu](01-introduction/README.md) - Tìm hiểu những điều cơ bản về LangChain4j
3. [Kỹ thuật thiết kế Prompt](02-prompt-engineering/README.md) - Nắm vững cách thiết kế prompt hiệu quả
4. [RAG (Tạo sinh được tăng cường truy xuất)](03-rag/README.md) - Xây dựng hệ thống kiến thức thông minh
5. [Công cụ](04-tools/README.md) - Tích hợp công cụ bên ngoài và trợ lý đơn giản
6. [MCP (Giao thức Ngữ cảnh Mô hình)](05-mcp/README.md) - Làm việc với Giao thức Ngữ cảnh Mô hình (MCP) và các mô-đun Đại lý
---

## Lộ trình học

> **Bắt đầu nhanh**

1. Fork kho lưu trữ này vào tài khoản GitHub của bạn
2. Nhấp **Code** → tab **Codespaces** → **...** → **New with options...**
3. Sử dụng các thiết lập mặc định – điều này sẽ chọn container Phát triển được tạo cho khóa học này
4. Nhấp **Create codespace**
5. Chờ 5-10 phút để môi trường sẵn sàng
6. Đi thẳng tới [Bắt đầu nhanh](./00-quick-start/README.md) để bắt đầu!

Sau khi hoàn thành các mô-đun, khám phá [Hướng dẫn kiểm thử](docs/TESTING.md) để xem các khái niệm kiểm thử LangChain4j trong thực tế.

> **Lưu ý:** Khóa đào tạo này sử dụng cả Mô hình GitHub và Azure OpenAI. Mô-đun [Bắt đầu nhanh](00-quick-start/README.md) sử dụng Mô hình GitHub (không cần đăng ký Azure), trong khi các mô-đun 1-5 sử dụng Azure OpenAI. Đăng ký một [tài khoản Azure MIỄN PHÍ](https://aka.ms/azure-free-account) nếu bạn chưa có tài khoản.

## Học với GitHub Copilot

Để nhanh chóng bắt đầu code, mở dự án này trong GitHub Codespace hoặc IDE local với devcontainer được cung cấp sẵn. Devcontainer sử dụng trong khóa học này được cấu hình sẵn với GitHub Copilot cho lập trình cặp AI.

Mỗi ví dụ code có các câu hỏi gợi ý bạn có thể hỏi GitHub Copilot để tăng hiểu biết. Tìm các dấu 💡/🤖 trong:

- **Tiêu đề file Java** - Câu hỏi cụ thể cho từng ví dụ
- **READMEs mô-đun** - Các gợi ý khám phá sau ví dụ code

**Cách sử dụng:** Mở bất kỳ file code nào và hỏi Copilot các câu hỏi gợi ý. Copilot có toàn bộ ngữ cảnh của codebase và có thể giải thích, mở rộng, và đề xuất các giải pháp thay thế.

Muốn tìm hiểu thêm? Xem [Copilot cho lập trình cặp AI](https://aka.ms/GitHubCopilotAI).

## Tài nguyên bổ sung

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j cho Người mới bắt đầu](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js cho Người mới bắt đầu](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD cho Người mới bắt đầu](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI cho Người mới bắt đầu](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP cho Người mới bắt đầu](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Đại lý AI cho Người mới bắt đầu](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Chuỗi AI sinh tạo
[![Sinh tạo AI cho Người mới bắt đầu](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Sinh tạo AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Sinh tạo AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Sinh tạo AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Học cốt lõi
[![ML cho Người mới bắt đầu](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Khoa học dữ liệu cho Người mới bắt đầu](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI cho Người mới bắt đầu](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![An ninh mạng cho Người mới bắt đầu](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Phát triển Web cho Người mới bắt đầu](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT cho Người mới bắt đầu](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Phát triển XR cho Người mới bắt đầu](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Loạt bài Copilot
[![Copilot cho Lập trình AI theo cặp](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot cho C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Cuộc phiêu lưu Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Nhận giúp đỡ

Nếu bạn bị mắc kẹt hoặc có bất kỳ câu hỏi nào về xây dựng các ứng dụng AI, hãy tham gia:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Nếu bạn có phản hồi về sản phẩm hoặc gặp lỗi khi xây dựng, hãy truy cập:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Giấy phép

Giấy phép MIT - Xem tệp [LICENSE](../../LICENSE) để biết chi tiết.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Tuyên bố từ chối trách nhiệm**:
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, nhưng xin lưu ý rằng bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ gốc của nó nên được coi là nguồn tham khảo chính xác nhất. Đối với các thông tin quan trọng, nên sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm về bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->