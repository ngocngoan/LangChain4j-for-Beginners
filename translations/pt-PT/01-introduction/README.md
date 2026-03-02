# Módulo 01: Começar com LangChain4j

## Índice

- [Vídeo Explicativo](../../../01-introduction)
- [O Que Vai Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Compreender o Problema Central](../../../01-introduction)
- [Compreender Tokens](../../../01-introduction)
- [Como a Memória Funciona](../../../01-introduction)
- [Como Isto Utiliza LangChain4j](../../../01-introduction)
- [Desplegar Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Utilizar a Aplicação](../../../01-introduction)
  - [Chat Sem Estado (Painel Esquerdo)](../../../01-introduction)
  - [Chat Com Estado (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## Vídeo Explicativo

Veja esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## O Que Vai Aprender

No início rápido, usou os Modelos do GitHub para enviar prompts, chamar ferramentas, construir um pipeline RAG e testar guardrails. Essas demonstrações mostraram o que é possível — agora mudamos para Azure OpenAI e GPT-5.2 e começamos a construir aplicações estilo produção. Este módulo foca em IA conversacional que lembra contexto e mantém estado — os conceitos que os demos do início rápido usavam nos bastidores, mas que não foram explicados.

Vamos usar o GPT-5.2 do Azure OpenAI ao longo deste guia porque as suas capacidades avançadas de raciocínio tornam o comportamento dos diferentes padrões mais evidente. Quando adicionar memória, verá claramente a diferença. Isso facilita compreender o que cada componente traz para a sua aplicação.

Vai construir uma aplicação que demonstra ambos os padrões:

**Chat Sem Estado** - Cada pedido é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão que usou no início rápido.

**Conversa Com Estado** - Cada pedido inclui o histórico da conversa. O modelo mantém o contexto ao longo de múltiplos turnos. Isto é o que as aplicações em produção requerem.

## Pré-requisitos

- Subscrição Azure com acesso a Azure OpenAI
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. O despliege está configurado automaticamente através de `azd up` - não modifique o nome do modelo no código.

## Compreender o Problema Central

Os modelos de linguagem são sem estado. Cada chamada API é independente. Se enviar "O meu nome é John" e depois perguntar "Qual é o meu nome?", o modelo não tem ideia que acabou de se apresentar. Trata cada pedido como se fosse a primeira conversa que alguma vez teve.

Isto é aceitável para Q&A simples, mas inútil para aplicações reais. Bots de serviço ao cliente precisam lembrar o que lhes disse. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplos turnos requer memória.

O diagrama seguinte contrasta as duas abordagens — à esquerda, uma chamada sem estado que esquece o seu nome; à direita, uma chamada com estado suportada por ChatMemory que o lembra.

<img src="../../../translated_images/pt-PT/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*A diferença entre conversas sem estado (chamadas independentes) e com estado (com conhecimento do contexto)*

## Compreender Tokens

Antes de mergulhar nas conversas, é importante compreender tokens - as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-PT/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exemplo de como o texto é dividido em tokens - "I love AI!" torna-se 4 unidades separadas de processamento*

Tokens são como os modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. O seu modelo tem um limite sobre quantos tokens pode processar de cada vez (400.000 para o GPT-5.2, com até 272.000 tokens de entrada e 128.000 tokens de saída). Compreender tokens ajuda a gerir o comprimento da conversa e os custos.

## Como a Memória Funciona

A memória de chat resolve o problema de ser sem estado mantendo o histórico da conversa. Antes de enviar o seu pedido para o modelo, o framework adiciona mensagens anteriores relevantes. Quando pergunta "Qual é o meu nome?", o sistema envia na verdade todo o histórico da conversa, permitindo que o modelo veja que disse anteriormente "O meu nome é John."

LangChain4j fornece implementações de memória que tratam disso automaticamente. Escolhe quantas mensagens reter e o framework gere a janela de contexto. O diagrama abaixo mostra como MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes.

<img src="../../../translated_images/pt-PT/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, eliminando automaticamente as mais antigas*

## Como Isto Utiliza LangChain4j

Este módulo estende o início rápido integrando Spring Boot e adicionando memória de conversa. Eis como as peças se encaixam:

**Dependências** - Adicione duas bibliotecas LangChain4j:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Modelo de Chat** - Configure Azure OpenAI como um bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

O builder lê as credenciais das variáveis de ambiente definidas por `azd up`. Definir `baseUrl` para o seu endpoint Azure faz o cliente OpenAI funcionar com Azure OpenAI.

**Memória de Conversa** - Faça o tracking do histórico de chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie a memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens do utilizador e da IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie-o para o modelo. O serviço armazena instâncias de memória separadas por ID da conversa, permitindo que múltiplos utilizadores conversem simultaneamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como é que o MessageWindowChatMemory decide quais mensagens eliminar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando uma base de dados em vez de memória volátil?"
> - "Como adicionaria sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat sem estado ignora totalmente a memória - apenas `chatModel.chat(prompt)` tal como no início rápido. O endpoint com estado adiciona mensagens à memória, recupera o histórico e inclui esse contexto em cada pedido. Mesma configuração do modelo, padrões diferentes.

## Desplegar Infraestrutura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selecionar subscrição e localização (eastus2 recomendado)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecionar subscrição e localização (eastus2 recomendado)
```

> **Nota:** Se encontrar um erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), basta executar `azd up` novamente. Os recursos Azure podem ainda estar a ser provisionados em segundo plano, e tentar outra vez permite que o despliege seja concluído assim que os recursos chegarem a um estado terminal.

Isto irá:
1. Desplegar o recurso Azure OpenAI com modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente o ficheiro `.env` na raiz do projeto com as credenciais
3. Configurar todas as variáveis de ambiente necessárias

**Tem problemas no despliege?** Veja o [README da Infraestrutura](infra/README.md) para resolução detalhada incluindo conflitos de nome de subdomínio, passos manuais no Portal Azure, e orientações para configuração do modelo.

**Verifique se o despliege teve sucesso:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o ficheiro `.env`. Se precisar de o atualizar mais tarde, pode editar manualmente o ficheiro `.env` ou regenerá-lo executando:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Executar a Aplicação Localmente

**Verifique o despliege:**

Assegure-se que o ficheiro `.env` existe na diretoria raiz com as credenciais Azure. Execute isto a partir da diretoria do módulo (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie as aplicações:**

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O dev container inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone Spring Boot).

No Spring Boot Dashboard pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de play junto a "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-PT/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*O Spring Boot Dashboard no VS Code — iniciar, parar e monitorizar todos os módulos num só lugar*

**Opção 2: Usar scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # A partir do diretório raiz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A partir do diretório raiz
.\start-all.ps1
```

Ou inicie apenas este módulo:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz e irão construir os JARs se estes não existirem.

> **Nota:** Se preferir compilar todos os módulos manualmente antes de iniciar:
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

Abra http://localhost:8080 no seu navegador.

**Para parar:**

**Bash:**
```bash
./stop.sh  # Apenas este módulo
# Ou
cd .. && ./stop-all.sh  # Todos os módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Utilizar a Aplicação

A aplicação oferece uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-PT/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Painel mostrando as opções Simples Chat (sem estado) e Conversational Chat (com estado)*

### Chat Sem Estado (Painel Esquerdo)

Experimente primeiro. Pergunte "O meu nome é John" e depois imediatamente pergunte "Qual é o meu nome?". O modelo não irá lembrar porque cada mensagem é independente. Isto demonstra o problema central da integração básica com modelos de linguagem - nenhum contexto de conversa.

<img src="../../../translated_images/pt-PT/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*IA não se lembra do seu nome da mensagem anterior*

### Chat Com Estado (Painel Direito)

Agora experimente a mesma sequência aqui. Pergunte "O meu nome é John" e depois "Qual é o meu nome?". Desta vez lembra-se. A diferença é MessageWindowChatMemory - esta mantém o histórico da conversa e inclui-o em cada pedido. É assim que a IA conversacional em produção funciona.

<img src="../../../translated_images/pt-PT/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*IA lembra-se do seu nome do início da conversa*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isto torna claro o que a memória traz para a sua aplicação e porque é essencial para casos reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso legal**:  
Este documento foi traduzido usando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, tenha em mente que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional feita por um ser humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->