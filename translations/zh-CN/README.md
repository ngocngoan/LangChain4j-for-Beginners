<img src="../../translated_images/zh-CN/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j 初学者教程

使用 LangChain4j 和 Azure OpenAI GPT-5.2 构建 AI 应用程序的课程，从基础聊天到 AI 代理。

### 🌐 多语言支持

#### 通过 GitHub Action 支持（自动且始终保持最新）

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[阿拉伯语](../ar/README.md) | [孟加拉语](../bn/README.md) | [保加利亚语](../bg/README.md) | [缅甸语 (Myanmar)](../my/README.md) | [中文 (简体)](./README.md) | [中文 (繁体，香港)](../zh-HK/README.md) | [中文 (繁体，澳门)](../zh-MO/README.md) | [中文 (繁体，台湾)](../zh-TW/README.md) | [克罗地亚语](../hr/README.md) | [捷克语](../cs/README.md) | [丹麦语](../da/README.md) | [荷兰语](../nl/README.md) | [爱沙尼亚语](../et/README.md) | [芬兰语](../fi/README.md) | [法语](../fr/README.md) | [德语](../de/README.md) | [希腊语](../el/README.md) | [希伯来语](../he/README.md) | [印地语](../hi/README.md) | [匈牙利语](../hu/README.md) | [印度尼西亚语](../id/README.md) | [意大利语](../it/README.md) | [日语](../ja/README.md) | [卡纳达语](../kn/README.md) | [韩语](../ko/README.md) | [立陶宛语](../lt/README.md) | [马来语](../ms/README.md) | [马拉雅拉姆语](../ml/README.md) | [马拉地语](../mr/README.md) | [尼泊尔语](../ne/README.md) | [尼日利亚皮钦语](../pcm/README.md) | [挪威语](../no/README.md) | [波斯语 (法尔西语)](../fa/README.md) | [波兰语](../pl/README.md) | [葡萄牙语 (巴西)](../pt-BR/README.md) | [葡萄牙语 (葡萄牙)](../pt-PT/README.md) | [旁遮普语 (古鲁穆奇)](../pa/README.md) | [罗马尼亚语](../ro/README.md) | [俄语](../ru/README.md) | [塞尔维亚语 (西里尔文)](../sr/README.md) | [斯洛伐克语](../sk/README.md) | [斯洛文尼亚语](../sl/README.md) | [西班牙语](../es/README.md) | [斯瓦希里语](../sw/README.md) | [瑞典语](../sv/README.md) | [他加禄语 (菲律宾语)](../tl/README.md) | [泰米尔语](../ta/README.md) | [泰卢固语](../te/README.md) | [泰语](../th/README.md) | [土耳其语](../tr/README.md) | [乌克兰语](../uk/README.md) | [乌尔都语](../ur/README.md) | [越南语](../vi/README.md)

> **想要本地克隆？**
>
> 此存储库包含50多种语言的翻译，大幅增加了下载大小。若要克隆时不包含翻译，请使用稀疏检出：
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
> 这样您可以以更快的下载速度获取完成课程所需的所有内容。
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 目录

1. [快速入门](00-quick-start/README.md) - 开始使用 LangChain4j
2. [介绍](01-introduction/README.md) - 学习 LangChain4j 基础
3. [提示工程](02-prompt-engineering/README.md) - 精通有效的提示设计
4. [RAG（检索增强生成）](03-rag/README.md) - 构建智能知识库系统
5. [工具](04-tools/README.md) - 集成外部工具和简单助手
6. [MCP（模型上下文协议）](05-mcp/README.md) - 使用模型上下文协议 (MCP) 和代理模块
---

## 学习路径

**LangChain4j 新手？** 请查看 [术语表](docs/GLOSSARY.md)，了解关键术语和概念定义。

> **快速入门**

1. 将此存储库分叉到您的 GitHub 账户
2. 点击 **Code** → **Codespaces** 选项卡 → **...** → **带选项的新建...**
3. 使用默认设置 — 这将选择本课程创建的开发容器
4. 点击 **创建 codespace**
5. 等待 5-10 分钟，环境准备完成
6. 直接跳转到 [快速入门](./00-quick-start/README.md) 开始学习！

完成模块后，浏览 [测试指南](docs/TESTING.md)，了解 LangChain4j 测试概念的实际应用。

> **注意：** 本培训使用 GitHub 模型和 Azure OpenAI。 [快速入门](00-quick-start/README.md) 模块使用 GitHub 模型（无需 Azure 订阅），而模块 1-5 使用 Azure OpenAI。若没有账号，请使用 [免费 Azure 账户](https://aka.ms/azure-free-account) 快速开始。


## 使用 GitHub Copilot 学习

要快速开始编码，可使用 GitHub Codespace 打开此项目或在本地 IDE 中使用提供的 devcontainer。该课程所用的 devcontainer 已预配置 GitHub Copilot 支持 AI 配对编程。

每个代码示例均包含您可以询问 GitHub Copilot 的建议问题，帮助深化理解。请留意以下位置的 💡/🤖 提示：

- **Java 文件头部** - 针对每个示例的特定问题
- **模块 README 文件** - 代码示例后的探索提示

**使用方法：** 打开任意代码文件，向 Copilot 提问建议的问题。它拥有完整代码上下文，能解释、扩展并建议替代方案。

想了解更多？查看 [AI 配对编程的 Copilot](https://aka.ms/GitHubCopilotAI)。


## 其他资源

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 初学者](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 初学者](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain 初学者](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / 代理
[![AZD 初学者](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI 初学者](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP 初学者](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 代理初学者](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 生成式 AI 系列
[![生成式 AI 初学者](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 核心学习
[![机器学习初学者](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![数据科学初学者](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![人工智能初学者](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![网络安全初学者](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![前端开发初学者](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot 系列
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 获取帮助

如果你遇到困难或对构建 AI 应用有任何疑问，请加入：

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

如果你有产品反馈或构建过程中出现错误，请访问：

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## 许可证

MIT 许可证 - 详情见 [LICENSE](../../LICENSE) 文件。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文档由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。尽管我们力求准确，但请注意自动翻译可能存在错误或不准确之处。请以原文档的原始语言版本为权威来源。对于重要信息，建议使用专业人工翻译。我们对因使用本翻译版本而产生的任何误解或错误解释不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->