# Infraestrutura Azure para LangChain4j Introdução

## Índice

- [Pré-requisitos](../../../../01-introduction/infra)
- [Arquitetura](../../../../01-introduction/infra)
- [Recursos Criados](../../../../01-introduction/infra)
- [Início Rápido](../../../../01-introduction/infra)
- [Configuração](../../../../01-introduction/infra)
- [Comandos de Gestão](../../../../01-introduction/infra)
- [Otimização de Custos](../../../../01-introduction/infra)
- [Monitorização](../../../../01-introduction/infra)
- [Resolução de Problemas](../../../../01-introduction/infra)
- [Atualização da Infraestrutura](../../../../01-introduction/infra)
- [Limpeza](../../../../01-introduction/infra)
- [Estrutura de Ficheiros](../../../../01-introduction/infra)
- [Recomendações de Segurança](../../../../01-introduction/infra)
- [Recursos Adicionais](../../../../01-introduction/infra)

Este directório contém a infraestrutura Azure como código (IaC) usando Bicep e Azure Developer CLI (azd) para implementar recursos Azure OpenAI.

## Pré-requisitos

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (versão 2.50.0 ou superior)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (versão 1.5.0 ou superior)
- Uma subscrição Azure com permissões para criar recursos

## Arquitetura

**Configuração Simplificada para Desenvolvimento Local** - Implanta apenas Azure OpenAI, execute todas as aplicações localmente.

A infraestrutura implementa os seguintes recursos Azure:

### Serviços de IA
- **Azure OpenAI**: Serviços Cognitivos com duas implementações de modelo:
  - **gpt-5.2**: Modelo de chat completions (capacidade 20K TPM)
  - **text-embedding-3-small**: Modelo de embedding para RAG (capacidade 20K TPM)

### Desenvolvimento Local
Todas as aplicações Spring Boot correm localmente na sua máquina:
- 01-introduction (porta 8080)
- 02-prompt-engineering (porta 8083)
- 03-rag (porta 8081)
- 04-tools (porta 8084)

## Recursos Criados

| Tipo de Recurso | Padrão de Nome do Recurso | Propósito |
|-----------------|---------------------------|-----------|
| Grupo de Recursos | `rg-{environmentName}` | Contém todos os recursos |
| Azure OpenAI | `aoai-{resourceToken}` | Hospedagem do modelo IA |

> **Nota:** `{resourceToken}` é uma string única gerada a partir do ID da subscrição, nome do ambiente e localização

## Início Rápido

### 1. Implementar Azure OpenAI

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

Quando solicitado:
- Selecione a sua subscrição Azure
- Escolha uma localização (recomendado: `eastus2` para disponibilidade GPT-5.2)
- Confirme o nome do ambiente (padrão: `langchain4j-dev`)

Isto irá criar:
- Recurso Azure OpenAI com GPT-5.2 e text-embedding-3-small
- Detalhes de conexão em output

### 2. Obter Detalhes de Conexão

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Isto mostra:
- `AZURE_OPENAI_ENDPOINT`: URL do seu endpoint Azure OpenAI
- `AZURE_OPENAI_KEY`: Chave API para autenticação
- `AZURE_OPENAI_DEPLOYMENT`: Nome do modelo de chat (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Nome do modelo embedding

### 3. Executar Aplicações Localmente

O comando `azd up` cria automaticamente um ficheiro `.env` na raiz com todas as variáveis de ambiente necessárias.

**Recomendado:** Iniciar todas as aplicações web:

**Bash:**
```bash
# A partir do diretório raiz
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# A partir do diretório raiz
cd ../..
.\start-all.ps1
```

Ou iniciar um único módulo:

**Bash:**
```bash
# Exemplo: Começar apenas o módulo de introdução
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Exemplo: Iniciar apenas o módulo de introdução
cd ../01-introduction
.\start.ps1
```

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz criado pelo `azd up`.

## Configuração

### Personalizar Implementações de Modelo

Para alterar as implementações de modelo, edite `infra/main.bicep` e modifique o parâmetro `openAiDeployments`:

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

Modelos e versões disponíveis: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Alterar Regiões Azure

Para implementar numa região diferente, edite `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Verifique a disponibilidade do GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Para atualizar a infraestrutura após alterações nos ficheiros Bicep:

**Bash:**
```bash
# Reconstruir o modelo ARM
az bicep build --file infra/main.bicep

# Pré-visualizar alterações
azd provision --preview

# Aplicar alterações
azd provision
```

**PowerShell:**
```powershell
# Reconstruir o modelo ARM
az bicep build --file infra/main.bicep

# Pré-visualizar alterações
azd provision --preview

# Aplicar alterações
azd provision
```

## Limpeza

Para eliminar todos os recursos:

**Bash:**
```bash
# Eliminar todos os recursos
azd down

# Eliminar tudo, incluindo o ambiente
azd down --purge
```

**PowerShell:**
```powershell
# Apagar todos os recursos
azd down

# Apagar tudo incluindo o ambiente
azd down --purge
```

**Aviso**: Isto eliminará permanentemente todos os recursos Azure.

## Estrutura de Ficheiros

## Otimização de Custos

### Desenvolvimento/Teste
Para ambientes de desenvolvimento/teste, pode reduzir custos:
- Use o nível Standard (S0) para Azure OpenAI
- Defina capacidade inferior (10K TPM em vez de 20K) em `infra/core/ai/cognitiveservices.bicep`
- Elimine recursos quando não estiver a usá-los: `azd down`

### Produção
Para produção:
- Aumente a capacidade OpenAI conforme uso (50K+ TPM)
- Active redundância de zona para maior disponibilidade
- Implemente monitorização adequada e alertas de custo

### Estimativa de Custos
- Azure OpenAI: Pagamento por token (entrada + saída)
- GPT-5.2: ~3-5$ por 1M tokens (verifique preços atuais)
- text-embedding-3-small: ~0,02$ por 1M tokens

Calculadora de preços: https://azure.microsoft.com/pricing/calculator/

## Monitorização

### Visualizar Métricas Azure OpenAI

Aceda ao Portal Azure → O seu recurso OpenAI → Métricas:
- Utilização baseada em tokens
- Taxa de pedidos HTTP
- Tempo de resposta
- Tokens ativos

## Resolução de Problemas

### Problema: Conflito no nome do subdomínio Azure OpenAI

**Mensagem de erro:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Causa:**
O nome do subdomínio gerado a partir da sua subscrição/ambiente já está em uso, possivelmente por uma implementação anterior que não foi totalmente eliminada.

**Solução:**
1. **Opção 1 - Use um nome de ambiente diferente:**
   
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

2. **Opção 2 - Implementação manual via Portal Azure:**
   - Vá ao Portal Azure → Criar um recurso → Azure OpenAI
   - Escolha um nome único para o recurso
   - Implemente os seguintes modelos:
     - **GPT-5.2**
     - **text-embedding-3-small** (para módulos RAG)
   - **Importante:** Anote os nomes das implementações — devem corresponder à configuração `.env`
   - Após implementado, obtenha o endpoint e a chave API em "Keys and Endpoint"
   - Crie um ficheiro `.env` na raiz do projeto com:
     
     **Exemplo de ficheiro `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Diretrizes para Nomes de Implementação dos Modelos:**
- Use nomes simples e consistentes: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Os nomes das implementações devem corresponder exatamente ao que configura em `.env`
- Erro comum: criar o modelo com um nome e referenciá-lo com outro diferente no código

### Problema: GPT-5.2 não disponível na região selecionada

**Solução:**
- Escolha uma região com acesso a GPT-5.2 (ex: eastus2)
- Verifique disponibilidade: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Problema: Quota insuficiente para implementação

**Solução:**
1. Solicite aumento de quota no Portal Azure
2. Ou use capacidade inferior em `main.bicep` (ex: capacity: 10)

### Problema: "Resource not found" quando corre localmente

**Solução:**
1. Verifique implementação com: `azd env get-values`
2. Confirme que endpoint e chave estão corretos
3. Assegure que o grupo de recursos existe no Portal Azure

### Problema: Falha na autenticação

**Solução:**
- Verifique se `AZURE_OPENAI_API_KEY` está corretamente configurado
- Formato da chave deve ser uma string hexadecimal de 32 caracteres
- Obtenha uma nova chave no Portal Azure se necessário

### Implementação Falha

**Problema**: `azd provision` falha com erros de quota ou capacidade

**Solução**: 
1. Experimente uma região diferente - Veja a secção [Alterar Regiões Azure](../../../../01-introduction/infra) para configurar regiões
2. Verifique se a sua subscrição tem quota para Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Aplicação Não Consegue Conectar

**Problema**: Aplicação Java mostra erros de conexão

**Solução**:
1. Verifique que as variáveis de ambiente foram exportadas:
   
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

2. Confirme que o formato do endpoint está correto (deve ser `https://xxx.openai.azure.com`)
3. Confirme que a chave API é a chave primária ou secundária do Portal Azure

**Problema**: 401 Unauthorized do Azure OpenAI

**Solução**:
1. Obtenha uma nova chave API fresca no Portal Azure → Keys and Endpoint
2. Re-exporte a variável de ambiente `AZURE_OPENAI_API_KEY`
3. Garanta que as implementações dos modelos estão completas (verifique no Portal Azure)

### Problemas de Performance

**Problema**: Tempos de resposta lentos

**Solução**:
1. Consulte utilização de tokens e throttling no Portal Azure nas métricas OpenAI
2. Aumente a capacidade TPM se estiver a atingir limites
3. Considere usar um nível de esforço de raciocínio mais elevado (baixo/médio/alto)

## Atualização da Infraestrutura

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

## Recomendações de Segurança

1. **Nunca comprometa chaves API** - Use variáveis de ambiente
2. **Use ficheiros .env localmente** - Adicione `.env` ao `.gitignore`
3. **Roteie chaves regularmente** - Gere novas chaves no Portal Azure
4. **Limite o acesso** - Use Azure RBAC para controlar quem acede aos recursos
5. **Monitorize o uso** - Configure alertas de custo no Portal Azure

## Recursos Adicionais

- [Documentação do Azure OpenAI Service](https://learn.microsoft.com/azure/ai-services/openai/)
- [Documentação do Modelo GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Documentação do Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Documentação Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Integração Oficial LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Suporte

Para problemas:
1. Verifique a [secção de resolução de problemas](../../../../01-introduction/infra) acima
2. Reveja o estado do serviço Azure OpenAI no Portal Azure
3. Abra uma issue no repositório

## Licença

Consulte o ficheiro [LICENSE](../../../../LICENSE) na raiz para detalhes.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->