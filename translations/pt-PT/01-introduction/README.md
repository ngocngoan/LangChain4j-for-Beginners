# Módulo 01: Introdução ao LangChain4j

## Índice

- [O Que Irá Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Compreendendo o Problema Principal](../../../01-introduction)
- [Compreendendo Tokens](../../../01-introduction)
- [Como a Memória Funciona](../../../01-introduction)
- [Como Isto Usa LangChain4j](../../../01-introduction)
- [Desplegar Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Usar a Aplicação](../../../01-introduction)
  - [Chat Stateless (Painel Esquerdo)](../../../01-introduction)
  - [Chat Stateful (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## O Que Irá Aprender

Se completou o quick start, viu como enviar prompts e obter respostas. Essa é a base, mas aplicações reais precisam de mais. Este módulo ensina a construir IA conversacional que lembra o contexto e mantém estado – a diferença entre uma demo pontual e uma aplicação pronta para produção.

Usaremos o GPT-5.2 do Azure OpenAI ao longo deste guia porque as suas capacidades avançadas de raciocínio tornam o comportamento dos diferentes padrões mais evidentes. Quando adicionar memória, verá claramente a diferença. Isto facilita compreender o que cada componente traz à sua aplicação.

Vai construir uma aplicação que demonstra ambos os padrões:

**Chat Stateless** – Cada pedido é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão que usou no quick start.

**Conversação Stateful** – Cada pedido inclui o histórico da conversa. O modelo mantém o contexto ao longo de várias interações. Isto é o que as aplicações de produção exigem.

## Pré-requisitos

- Subscrição Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. O deployment é configurado automaticamente via `azd up` – não modifique o nome do modelo no código.

## Compreendendo o Problema Principal

Modelos de linguagem são stateless. Cada chamada API é independente. Se enviar "O meu nome é João" e depois perguntar "Qual é o meu nome?", o modelo não tem ideia de que acabou de se apresentar. Trata todas as solicitações como se fosse a primeira conversa que tem.

Isto é aceitável para Q&A simples, mas inútil para aplicações reais. Bots de serviço ao cliente precisam lembrar o que disse. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplas interações exige memória.

<img src="../../../translated_images/pt-PT/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversas Stateless vs Stateful" width="800"/>

*A diferença entre conversas stateless (chamadas independentes) e stateful (com consciência do contexto)*

## Compreendendo Tokens

Antes de mergulhar nas conversas, é importante entender tokens – as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-PT/token-explanation.c39760d8ec650181.webp" alt="Explicação de Token" width="800"/>

*Exemplo de como o texto é dividido em tokens – "I love AI!" torna-se 4 unidades de processamento separadas*

Tokens são como os modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. O seu modelo tem um limite de quantos tokens pode processar de cada vez (400,000 para GPT-5.2, com até 272,000 tokens de entrada e 128,000 tokens de saída). Compreender tokens ajuda a gerir o comprimento da conversa e os custos.

## Como a Memória Funciona

A memória de chat resolve o problema stateless mantendo o histórico da conversa. Antes de enviar o seu pedido ao modelo, o framework adiciona mensagens relevantes anteriores. Quando pergunta "Qual é o meu nome?", o sistema envia todo o histórico da conversa, permitindo que o modelo veja que disse anteriormente "O meu nome é João."

O LangChain4j providencia implementações de memória que gerem isto automaticamente. Escolhe quantas mensagens reter e o framework gere a janela de contexto.

<img src="../../../translated_images/pt-PT/memory-window.bbe67f597eadabb3.webp" alt="Conceito da Janela de Memória" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, descartando automaticamente as antigas*

## Como Isto Usa LangChain4j

Este módulo estende o quick start integrando Spring Boot e adicionando memória de conversação. Eis como as peças se juntam:

**Dependências** – Adicione duas bibliotecas LangChain4j:

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
  
**Modelo de Chat** – Configure Azure OpenAI como um bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
O builder lê as credenciais das variáveis de ambiente definidas pelo `azd up`. Definir `baseUrl` para o seu endpoint Azure faz com que o cliente OpenAI funcione com Azure OpenAI.

**Memória de Conversa** – Acompanhe o histórico de chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
Crie memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens do utilizador e da IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie-o ao modelo. O serviço armazena instâncias de memória separadas por ID de conversa, permitindo que vários utilizadores conversem simultaneamente.

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como é que o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando uma base de dados em vez de memória volátil?"
> - "Como iria adicionar sumarização para comprimir histórico de conversa antigo?"

O endpoint de chat stateless ignora a memória – usa apenas `chatModel.chat(prompt)` como no quick start. O endpoint stateful adiciona mensagens à memória, recupera o histórico e inclui esse contexto em cada pedido. Mesma configuração do modelo, padrões diferentes.

## Desplegar Infraestrutura Azure OpenAI

**Bash:**  
```bash
cd 01-introduction
azd up  # Selecionar subscrição e localização (eastus2 recomendado)
```
  
**PowerShell:**  
```powershell
cd 01-introduction
azd up  # Selecione a subscrição e a localização (eastus2 recomendado)
```
  
> **Nota:** Se encontrar um erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), execute novamente `azd up`. Os recursos Azure podem estar a ser provisionados em segundo plano, e tentar novamente permite que o deployment termine quando os recursos atingem um estado terminal.

Isto irá:  
1. Deploy do recurso Azure OpenAI com os modelos GPT-5.2 e text-embedding-3-small  
2. Gerar automaticamente o ficheiro `.env` na raiz do projeto com as credenciais  
3. Configurar todas as variáveis de ambiente necessárias  

**A ter problemas no deployment?** Veja o [README de Infraestrutura](infra/README.md) para resolução detalhada, incluindo conflitos de nomes de subdomínios, passos manuais no Azure Portal e orientações sobre configurações de modelos.

**Verifique se o deployment foi bem-sucedido:**

**Bash:**  
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```
  
> **Nota:** O comando `azd up` gera automaticamente o ficheiro `.env`. Se precisar de o atualizar mais tarde, pode editar o `.env` manualmente ou regenerá-lo executando:  
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

**Verifique o deployment:**

Assegure que o ficheiro `.env` existe no diretório raíz com as credenciais Azure:

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

O dev container inclui a extensão Spring Boot Dashboard, que providencia uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades ao lado esquerdo do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard pode:  
- Ver todas as aplicações Spring Boot disponíveis no workspace  
- Iniciar/parar aplicações com um clique  
- Ver logs da aplicação em tempo real  
- Monitorizar o estado das aplicações  

Basta clicar no botão play ao lado de "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-PT/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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
  
Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz e constroem os JARs se estes não existirem.

> **Nota:** Se preferir construir todos os módulos manualmente antes de iniciar:  
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
  
## Usar a Aplicação

A aplicação providencia uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-PT/home-screen.121a03206ab910c0.webp" alt="Ecrã Inicial da Aplicação" width="800"/>

*Dashboard mostrando as opções de Chat Simples (stateless) e Chat Conversacional (stateful)*

### Chat Stateless (Painel Esquerdo)

Comece por aqui. Pergunte "O meu nome é João" e de seguida pergunte "Qual é o meu nome?" O modelo não vai lembrar-se porque cada mensagem é independente. Isto demonstra o problema principal da integração básica de modelos de linguagem – falta de contexto na conversa.

<img src="../../../translated_images/pt-PT/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Stateless" width="800"/>

*IA não lembra o seu nome da mensagem anterior*

### Chat Stateful (Painel Direito)

Agora experimente a mesma sequência aqui. Pergunte "O meu nome é João" e depois "Qual é o meu nome?" Desta vez lembra-se. A diferença é o MessageWindowChatMemory – que mantém o histórico da conversa e inclui-o em cada pedido. É assim que funciona a IA conversacional em produção.

<img src="../../../translated_images/pt-PT/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Stateful" width="800"/>

*IA lembra-se do seu nome da conversa anterior*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isto torna claro o que a memória traz à sua aplicação e porque é essencial para casos de uso reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompts com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Quick Start](../00-quick-start/README.md) | [Voltar ao Principal](../README.md) | [Seguinte: Módulo 02 - Engenharia de Prompts →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Isenção de responsabilidade**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, por favor tenha em atenção que traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->