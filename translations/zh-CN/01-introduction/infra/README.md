# LangChain4j Azure 基础设施入门

## 目录

- [先决条件](../../../../01-introduction/infra)
- [架构](../../../../01-introduction/infra)
- [创建的资源](../../../../01-introduction/infra)
- [快速开始](../../../../01-introduction/infra)
- [配置](../../../../01-introduction/infra)
- [管理命令](../../../../01-introduction/infra)
- [成本优化](../../../../01-introduction/infra)
- [监控](../../../../01-introduction/infra)
- [故障排除](../../../../01-introduction/infra)
- [更新基础设施](../../../../01-introduction/infra)
- [清理](../../../../01-introduction/infra)
- [文件结构](../../../../01-introduction/infra)
- [安全建议](../../../../01-introduction/infra)
- [额外资源](../../../../01-introduction/infra)

该目录包含使用 Bicep 和 Azure Developer CLI (azd) 编写的 Azure 基础设施即代码 (IaC)，用于部署 Azure OpenAI 资源。

## 先决条件

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)（版本 2.50.0 或更高）
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd)（版本 1.5.0 或更高）
- 具有创建资源权限的 Azure 订阅

## 架构

**简化的本地开发设置** - 仅部署 Azure OpenAI，所有应用程序在本地运行。

该基础设施部署以下 Azure 资源：

### AI 服务
- **Azure OpenAI**：认知服务，包含两个模型部署：
  - **gpt-5.2**：聊天完成模型（20K TPM 容量）
  - **text-embedding-3-small**：用于 RAG 的嵌入模型（20K TPM 容量）

### 本地开发
所有 Spring Boot 应用程序均在本地机器运行：
- 01-introduction（端口 8080）
- 02-prompt-engineering（端口 8083）
- 03-rag（端口 8081）
- 04-tools（端口 8084）

## 创建的资源

| 资源类型         | 资源名称模式          | 目的           |
|------------------|-----------------------|----------------|
| 资源组           | `rg-{environmentName}` | 包含所有资源    |
| Azure OpenAI      | `aoai-{resourceToken}` | AI 模型托管     |

> **注意：** `{resourceToken}` 是基于订阅 ID、环境名称和位置生成的唯一字符串

## 快速开始

### 1. 部署 Azure OpenAI

**Bash：**
```bash
cd 01-introduction
azd up
```
  
**PowerShell：**
```powershell
cd 01-introduction
azd up
```
  
提示时：
- 选择你的 Azure 订阅
- 选择一个区域（推荐：`eastus2` 以支持 GPT-5.2）
- 确认环境名称（默认：`langchain4j-dev`）

这将创建：
- 带有 GPT-5.2 和 text-embedding-3-small 的 Azure OpenAI 资源
- 输出连接详细信息

### 2. 获取连接详情

**Bash：**
```bash
azd env get-values
```
  
**PowerShell：**
```powershell
azd env get-values
```
  
这会显示：
- `AZURE_OPENAI_ENDPOINT`：你的 Azure OpenAI 端点 URL
- `AZURE_OPENAI_KEY`：用于认证的 API 密钥
- `AZURE_OPENAI_DEPLOYMENT`：聊天模型名称（gpt-5.2）
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`：嵌入模型名称

### 3. 本地运行应用程序

`azd up` 命令会自动在根目录创建 `.env` 文件，包含所有所需的环境变量。

**推荐：启动所有 Web 应用程序：**

**Bash：**
```bash
# 从根目录开始
cd ../..
./start-all.sh
```
  
**PowerShell：**
```powershell
# 从根目录开始
cd ../..
.\start-all.ps1
```
  
或者启动单独模块：

**Bash：**
```bash
# 例子：只启动介绍模块
cd ../01-introduction
./start.sh
```
  
**PowerShell：**
```powershell
# 示例：只启动介绍模块
cd ../01-introduction
.\start.ps1
```
  
两者都会自动从 `azd up` 创建的根目录 `.env` 文件加载环境变量。

## 配置

### 自定义模型部署

若要更改模型部署，请编辑 `infra/main.bicep` 并修改 `openAiDeployments` 参数：

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
  
可用模型与版本请参见：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 更改 Azure 区域

若要在其他区域部署，请编辑 `infra/main.bicep`：

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```
  
查看 GPT-5.2 可用区域：https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

对 Bicep 文件做更改后更新基础设施：

**Bash：**
```bash
# 重新生成 ARM 模板
az bicep build --file infra/main.bicep

# 预览更改
azd provision --preview

# 应用更改
azd provision
```
  
**PowerShell：**
```powershell
# 重新生成ARM模板
az bicep build --file infra/main.bicep

# 预览更改
azd provision --preview

# 应用更改
azd provision
```
  

## 清理

删除所有资源：

**Bash：**
```bash
# 删除所有资源
azd down

# 删除包括环境在内的一切
azd down --purge
```
  
**PowerShell：**
```powershell
# 删除所有资源
azd down

# 删除包括环境在内的所有内容
azd down --purge
```
  
**警告**：此操作将永久删除所有 Azure 资源。

## 文件结构

## 成本优化

### 开发/测试
对于开发/测试环境，可以降低成本：
- Azure OpenAI 使用标准层（S0）
- 在 `infra/core/ai/cognitiveservices.bicep` 中设置较低容量（10K TPM 而非 20K）
- 不使用时删除资源：运行 `azd down`

### 生产环境
生产环境：
- 根据使用情况增加 OpenAI 容量（50K+ TPM）
- 启用区域冗余以提高可用性
- 实施适当监控和成本报警

### 成本估算
- Azure OpenAI：按 token 数（输入 + 输出）计费
- GPT-5.2：约 $3-5 每百万 tokens（请查看当前价格）
- text-embedding-3-small：约 $0.02 每百万 tokens

价格计算器：https://azure.microsoft.com/pricing/calculator/

## 监控

### 查看 Azure OpenAI 指标

进入 Azure 门户 → 你的 OpenAI 资源 → 指标：
- 基于 token 的使用
- HTTP 请求率
- 响应时间
- 活跃 token 数量

## 故障排除

### 问题：Azure OpenAI 子域名冲突

**错误信息：**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```
  
**原因：**  
你的订阅/环境生成的子域名已被占用，可能来自之前未完全清理的部署。

**解决方案：**  
1. **选项 1 - 使用不同的环境名称：**  
   
   **Bash：**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell：**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```
  
2. **选项 2 - 通过 Azure 门户手动部署：**
   - 进入 Azure 门户 → 创建资源 → Azure OpenAI
   - 选择唯一的资源名称
   - 部署以下模型：
     - **GPT-5.2**
     - **text-embedding-3-small**（用于 RAG 模块）
   - **重要**：记录部署名称 - 必须与 `.env` 配置匹配
   - 部署后，从“密钥和端点”获取端点和 API 密钥
   - 在项目根目录创建 `.env` 文件，内容示例如下：
     
     **示例 `.env` 文件：**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```
  
**模型部署命名指南：**
- 使用简单且一致的名称：`gpt-5.2`，`gpt-4o`，`text-embedding-3-small`
- 部署名称必须与 `.env` 中配置完全一致
- 常见错误：创建模型名称与代码中引用名称不符

### 问题：选定区域无 GPT-5.2 可用

**解决方案：**  
- 选择支持 GPT-5.2 的区域（例如 eastus2）
- 检查可用性：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 问题：部署配额不足

**解决方案：**  
1. 在 Azure 门户申请增加配额  
2. 或在 `main.bicep` 中使用较低容量（例如 capacity: 10）

### 问题：本地运行时报“资源未找到”

**解决方案：**  
1. 验证部署：运行 `azd env get-values`  
2. 检查端点和密钥是否正确  
3. 确认资源组在 Azure 门户中存在

### 问题：认证失败

**解决方案：**  
- 确认 `AZURE_OPENAI_API_KEY` 设置正确  
- 密钥格式应为 32 字符的十六进制字符串  
- 如需，前往 Azure 门户获取新的密钥

### 部署失败

**问题**：`azd provision` 因配额或容量错误失败

**解决方案**：  
1. 尝试不同区域 — 参见 [更改 Azure 区域](../../../../01-introduction/infra) 部分  
2. 检查订阅是否已有 Azure OpenAI 配额：
   
   **Bash：**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell：**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```
  
### 应用无法连接

**问题**：Java 应用显示连接错误

**解决方案**：  
1. 验证环境变量已导出：
   
   **Bash：**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell：**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```
  
2. 检查端点格式是否正确（应为 `https://xxx.openai.azure.com`）  
3. 确认 API 密钥是 Azure 门户的主密钥或备用密钥  

**问题**：Azure OpenAI 返回 401 Unauthorized

**解决方案**：  
1. 从 Azure 门户 → “密钥和端点” 获取新的 API 密钥  
2. 重新导出 `AZURE_OPENAI_API_KEY` 环境变量  
3. 确保模型部署完成（检查 Azure 门户）

### 性能问题

**问题**：响应时间慢

**解决方案**：  
1. 检查 Azure 门户中 OpenAI token 使用和限流指标  
2. 如果达到限制，增加 TPM 容量  
3. 考虑使用更高的推理努力等级（low/medium/high）

## 更新基础设施

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
  
## 安全建议

1. **切勿提交 API 密钥** — 使用环境变量  
2. **本地使用 .env 文件** — 将 `.env` 添加至 `.gitignore`  
3. **定期轮换密钥** — 在 Azure 门户生成新密钥  
4. **限制访问权限** — 使用 Azure RBAC 控制资源访问  
5. **监控使用情况** — 在 Azure 门户设置成本警报

## 额外资源

- [Azure OpenAI 服务文档](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 模型文档](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI 文档](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep 文档](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI 官方集成](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## 支持

遇到问题时：  
1. 查阅上方的 [故障排除部分](../../../../01-introduction/infra)  
2. 查看 Azure 门户中的 Azure OpenAI 服务健康状况  
3. 在仓库中提交 issue

## 许可

详细信息请参见根目录的 [LICENSE](../../../../LICENSE) 文件。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。虽然我们努力保证译文的准确性，但请注意自动翻译可能包含错误或不准确之处。原始文件的原文版本应被视为权威来源。如涉及重要信息，建议采用专业人工翻译。因使用本翻译而产生的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->