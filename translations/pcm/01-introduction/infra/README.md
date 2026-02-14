# Azure Infrastructure for LangChain4j Getting Started

## Table of Contents

- [Prerequisites](../../../../01-introduction/infra)
- [Architecture](../../../../01-introduction/infra)
- [Resources Created](../../../../01-introduction/infra)
- [Quick Start](../../../../01-introduction/infra)
- [Configuration](../../../../01-introduction/infra)
- [Management Commands](../../../../01-introduction/infra)
- [Cost Optimization](../../../../01-introduction/infra)
- [Monitoring](../../../../01-introduction/infra)
- [Troubleshooting](../../../../01-introduction/infra)
- [Updating Infrastructure](../../../../01-introduction/infra)
- [Clean Up](../../../../01-introduction/infra)
- [File Structure](../../../../01-introduction/infra)
- [Security Recommendations](../../../../01-introduction/infra)
- [Additional Resources](../../../../01-introduction/infra)

Dis directory get Azure infrastructure as code (IaC) wey use Bicep and Azure Developer CLI (azd) to deploy Azure OpenAI resources.

## Prerequisites

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (version 2.50.0 or later)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (version 1.5.0 or later)
- Azure subscription wey get permission to create resources

## Architecture

**Simplified Local Development Setup** - Deploy Azure OpenAI only, run all apps for local machine.

The infrastructure go deploy dis kain Azure resources:

### AI Services
- **Azure OpenAI**: Cognitive Services wey get two model deployments:
  - **gpt-5.2**: Chat completion model (20K TPM capacity)
  - **text-embedding-3-small**: Embedding model for RAG (20K TPM capacity)

### Local Development
All Spring Boot apps dey run locally for your machine:
- 01-introduction (port 8080)
- 02-prompt-engineering (port 8083)
- 03-rag (port 8081)
- 04-tools (port 8084)

## Resources Created

| Resource Type | Resource Name Pattern | Purpose |
|--------------|----------------------|---------|
| Resource Group | `rg-{environmentName}` | Get all resources inside |
| Azure OpenAI | `aoai-{resourceToken}` | AI model hosting |

> **Note:** `{resourceToken}` na unique string wey dem generate from subscription ID, environment name, and location

## Quick Start

### 1. Deploy Azure OpenAI

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

When e ask you:
- Select your Azure subscription
- Choose location (recommended: `eastus2` for GPT-5.2 availability)
- Confirm environment name (default: `langchain4j-dev`)

Dis go create:
- Azure OpenAI resource with GPT-5.2 and text-embedding-3-small
- Output connection details

### 2. Get Connection Details

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

This one show:
- `AZURE_OPENAI_ENDPOINT`: Your Azure OpenAI endpoint URL
- `AZURE_OPENAI_KEY`: API key wey you go use authenticate
- `AZURE_OPENAI_DEPLOYMENT`: Chat model name (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Embedding model name

### 3. Run Applications Locally

The `azd up` command dey automatically create `.env` file for root folder wey get all the environment variables wey you need.

**Recommended:** Start all web apps:

**Bash:**
```bash
# From di root directory
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# From di root directory
cd ../..
.\start-all.ps1
```

Or you fit start one single module:

**Bash:**
```bash
# Example: Start di introduction module only
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Example: Strat only di introduction module
cd ../01-introduction
.\start.ps1
```

Both these scripts go automatically load environment variables from root `.env` file wey `azd up` create.

## Configuration

### Customizing Model Deployments

If you want change model deployments, edit `infra/main.bicep` and modify the `openAiDeployments` parameter:

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

Available models and versions: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Changing Azure Regions

If you want deploy for another region, edit `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Check GPT-5.2 availability: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

To update infrastructure after you change Bicep files:

**Bash:**
```bash
# Build di ARM template again
az bicep build --file infra/main.bicep

# Look di changes first
azd provision --preview

# Put di changes for ground
azd provision
```

**PowerShell:**
```powershell
# Mak di ARM template again
az bicep build --file infra/main.bicep

# See di changes first
azd provision --preview

# Apply di changes
azd provision
```

## Clean Up

To delete all resources:

**Bash:**
```bash
# Comot all di resources dem
azd down

# Comot everytin wey dey, including di environment
azd down --purge
```

**PowerShell:**
```powershell
# Comot all di resources dem
azd down

# Comot everytin wey include di environment
azd down --purge
```

**Warning**: Dis one go permanently delete all Azure resources.

## File Structure

## Cost Optimization

### Development/Testing
For dev/test environments, you fit reduce costs:
- Use Standard tier (S0) for Azure OpenAI
- Set small capacity (10K TPM instead of 20K) inside `infra/core/ai/cognitiveservices.bicep`
- Delete resources when you no dey use: `azd down`

### Production
For production:
- Increase OpenAI capacity based on usage (50K+ TPM)
- Turn on zone redundancy for better availability
- Setup proper monitoring and cost alert

### Cost Estimation
- Azure OpenAI: Pay-per-token (input + output)
- GPT-5.2: ~$3-5 per 1M tokens (check current price)
- text-embedding-3-small: ~$0.02 per 1M tokens

Pricing calculator: https://azure.microsoft.com/pricing/calculator/

## Monitoring

### View Azure OpenAI Metrics

Go Azure Portal → Your OpenAI resource → Metrics:
- Token-Based Utilization
- HTTP Request Rate
- Time To Response
- Active Tokens

## Troubleshooting

### Issue: Azure OpenAI subdomain name conflict

**Error Message:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Cause:**
The subdomain name wey dem generate from your subscription/environment don dey used before, maybe from older deployment wey no clear finish.

**Solution:**
1. **Option 1 - Use different environment name:**
   
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

2. **Option 2 - Manual deployment via Azure Portal:**
   - Go Azure Portal → Create a resource → Azure OpenAI
   - Choose unique name for your resource
   - Deploy these models:
     - **GPT-5.2**
     - **text-embedding-3-small** (for RAG modules)
   - **Important:** Note your deployment names - dem suppose match `.env` configuration
   - After deployment, collect your endpoint and API key from "Keys and Endpoint"
   - Create `.env` file for project root with:
     
     **Example `.env` file:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Model Deployment Naming Guidelines:**
- Use simple, consistent names: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Deployment names must exactly match wetin you put for `.env`
- Common mistake: Create model with one name but reference another name for code

### Issue: GPT-5.2 no dey available for selected region

**Solution:**
- Choose region wey get GPT-5.2 (e.g., eastus2)
- Check availability: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Issue: Insufficient quota for deployment

**Solution:**
1. Request quota increase inside Azure Portal
2. Or use smaller capacity inside `main.bicep` (e.g., capacity: 10)

### Issue: "Resource not found" when you run locally

**Solution:**
1. Verify deployment: `azd env get-values`
2. Check endpoint and key correct
3. Make sure resource group dey for Azure Portal

### Issue: Authentication failed

**Solution:**
- Verify say `AZURE_OPENAI_API_KEY` set well
- Key format suppose be 32-character hexadecimal string
- Get new key from Azure Portal if you need

### Deployment Fails

**Issue**: `azd provision` fail with quota or capacity wahala

**Solution**: 
1. Try different region - See [Changing Azure Regions](../../../../01-introduction/infra) section for how to set regions
2. Check your subscription get Azure OpenAI quota:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Application No Connect

**Issue**: Java app dey show connection wahala

**Solution**:
1. Verify environment variables don export:
   
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

2. Check endpoint format dey correct (suppose be `https://xxx.openai.azure.com`)
3. Make sure API key na primary or secondary key from Azure Portal

**Issue**: 401 Unauthorized from Azure OpenAI

**Solution**:
1. Collect fresh API key from Azure Portal → Keys and Endpoint
2. Re-export the `AZURE_OPENAI_API_KEY` environment variable
3. Confirm model deployments don finish (check Azure Portal)

### Performance Issues

**Issue**: Response too slow

**Solution**:
1. Check OpenAI token usage and throttling inside Azure Portal metrics
2. Increase TPM capacity if you dey hit limits
3. Try use higher reasoning-effort level (low/medium/high)

## Updating Infrastructure

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

## Security Recommendations

1. **No ever commit API keys** - Use environment variables
2. **Use .env files locally** - Add `.env` to `.gitignore`
3. **Rotate keys often** - Generate new keys inside Azure Portal
4. **Limit access** - Use Azure RBAC to control who fit access resources
5. **Monitor usage** - Setup cost alerts inside Azure Portal

## Additional Resources

- [Azure OpenAI Service Documentation](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Model Documentation](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Documentation](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Documentation](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI Official Integration](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Support

For wahala:
1. Check the [troubleshooting section](../../../../01-introduction/infra) wey dey above
2. Look Azure OpenAI service health inside Azure Portal
3. Open issue for repository

## License

See the root [LICENSE](../../../../LICENSE) file for details.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Warning**:  
Dis document dem don use AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) translate am. Even though we try make e dey correct, abeg make you sabi say automated translation fit get mistake or wrong meaning. The original document wey dey im normal language na im be the correct one. If na serious matter, e better make person wey sabi do professional translation check am. We no go responsible for any kasala or wrong meaning wey fit happen because of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->