# Módulo 01: Introdução ao LangChain4j

## Índice

- [Vídeo Tutorial](../../../01-introduction)
- [O que Vai Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Compreender o Problema Principal](../../../01-introduction)
- [Compreender Tokens](../../../01-introduction)
- [Como Funciona a Memória](../../../01-introduction)
- [Como Isto Usa o LangChain4j](../../../01-introduction)
- [Implementar a Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Utilizar a Aplicação](../../../01-introduction)
  - [Chat Sem Estado (Painel Esquerdo)](../../../01-introduction)
  - [Chat Com Estado (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## Vídeo Tutorial

Veja esta sessão ao vivo que explica como começar com este módulo: [Introdução ao LangChain4j - Sessão ao Vivo](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## O que Vai Aprender

Se completou o início rápido, viu como enviar prompts e obter respostas. Essa é a base, mas aplicações reais precisam de mais. Este módulo ensina como construir IA conversacional que lembra o contexto e mantém o estado – a diferença entre uma demo isolada e uma aplicação pronta para produção.

Vamos usar o GPT-5.2 da Azure OpenAI ao longo deste guia porque as suas capacidades avançadas de raciocínio tornam o comportamento dos diferentes padrões mais evidentes. Quando adiciona memória, verá claramente a diferença. Isso facilita compreender o que cada componente traz para a sua aplicação.

Vai construir uma aplicação que demonstra ambos os padrões:

**Chat Sem Estado** – Cada pedido é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão que usou no início rápido.

**Conversa Com Estado** – Cada pedido inclui o histórico da conversa. O modelo mantém o contexto ao longo de múltiplas trocas. Isto é o que aplicações em produção exigem.

## Pré-requisitos

- Subscrição Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 na Azure OpenAI. A implementação é configurada automaticamente via `azd up` – não modifique o nome do modelo no código.

## Compreender o Problema Principal

Modelos de linguagem são sem estado. Cada chamada à API é independente. Se enviar "O meu nome é João" e depois perguntar "Qual é o meu nome?", o modelo não tem ideia de que se apresentou. Ele trata cada pedido como se fosse a primeira conversa que teve.

Isto é adequado para perguntas e respostas simples, mas inútil em aplicações reais. Bots de serviço ao cliente precisam lembrar o que lhes disse. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplas trocas requer memória.

<img src="../../../translated_images/pt-PT/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversas Sem Estado vs Com Estado" width="800"/>

*A diferença entre conversas sem estado (chamadas independentes) e com estado (sensíveis ao contexto)*

## Compreender Tokens

Antes de mergulhar nas conversas, é importante compreender tokens – as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-PT/token-explanation.c39760d8ec650181.webp" alt="Explicação de Token" width="800"/>

*Exemplo de como o texto é dividido em tokens – "Eu adoro IA!" torna-se em 4 unidades de processamento separadas*

Tokens são como os modelos de IA medem e processam o texto. Palavras, pontuação e até espaços podem ser tokens. O seu modelo tem um limite de quantos tokens pode processar de uma vez (400.000 para o GPT-5.2, com até 272.000 tokens de entrada e 128.000 tokens de saída). Compreender os tokens ajuda a gerir o comprimento da conversa e os custos.

## Como Funciona a Memória

A memória de chat resolve o problema sem estado mantendo o histórico da conversa. Antes de enviar o seu pedido ao modelo, o framework adiciona as mensagens anteriores relevantes. Quando pergunta "Qual é o meu nome?", o sistema na realidade envia todo o histórico da conversa, permitindo que o modelo veja que disse anteriormente "O meu nome é João".

O LangChain4j fornece implementações de memória que fazem isto automaticamente. Escolhe quantas mensagens manter e o framework gere a janela de contexto.

<img src="../../../translated_images/pt-PT/memory-window.bbe67f597eadabb3.webp" alt="Conceito de Janela de Memória" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, descartando automaticamente as antigas*

## Como Isto Usa o LangChain4j

Este módulo estende o início rápido ao integrar o Spring Boot e adicionar memória à conversa. Eis como as peças se ligam:

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

**Modelo de Chat** – Configure Azure OpenAI como bean do Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

O construtor lê as credenciais das variáveis de ambiente definidas pelo `azd up`. Definir `baseUrl` para o seu endpoint Azure faz com que o cliente OpenAI funcione com Azure OpenAI.

**Memória da Conversa** – Acompanhe o histórico do chat com o MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie a memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens do utilizador e da IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie-o ao modelo. O serviço armazena instâncias de memória separadas por ID de conversa, permitindo que múltiplos utilizadores conversem simultaneamente.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando uma base de dados em vez de memória na aplicação?"
> - "Como adicionaria sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat sem estado ignora a memória completamente – é só `chatModel.chat(prompt)` como no início rápido. O endpoint com estado adiciona mensagens à memória, recupera o histórico e inclui esse contexto em cada pedido. Mesma configuração de modelo, padrões diferentes.

## Implementar a Infraestrutura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selecionar subscrição e localização (eastus2 recomendado)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecione a subscrição e o local (eastus2 recomendado)
```

> **Nota:** Se encontrar um erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), apenas execute `azd up` novamente. Os recursos Azure podem ainda estar a ser provisionados em segundo plano, e tentar novamente permite que a implantação termine assim que os recursos alcançam um estado terminal.

Isto irá:
1. Implementar o recurso Azure OpenAI com modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente o ficheiro `.env` na raiz do projeto com as credenciais
3. Configurar todas as variáveis de ambiente necessárias

**Está a ter problemas na implantação?** Consulte o [README da Infraestrutura](infra/README.md) para resolução detalhada incluíndo conflitos de nomes de subdomínio, passos para implantação manual via Portal Azure e orientações de configuração do modelo.

**Verificar se a implantação foi bem-sucedida:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o ficheiro `.env`. Se precisar de o atualizar mais tarde, pode editar manualmente o `.env` ou gerá-lo novamente executando:
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

**Verificar implantação:**

Assegure que o ficheiro `.env` existe na raiz com as credenciais Azure:

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar as aplicações:**

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O devcontainer inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Encontra-a na Barra de Atividades à esquerda no VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um clique
- Visualizar os logs das aplicações em tempo real
- Monitorizar o estado das aplicações

Basta clicar no botão de play ao lado de "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-PT/dashboard.69c7479aef09ff6b.webp" alt="Painel Spring Boot" width="400"/>

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz e irão construir os JARs se ainda não existirem.

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
.\stop.ps1  # Este módulo apenas
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Utilizar a Aplicação

A aplicação oferece uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-PT/home-screen.121a03206ab910c0.webp" alt="Ecrã Inicial da Aplicação" width="800"/>

*Painel que mostra as opções Chat Simples (sem estado) e Chat Conversacional (com estado)*

### Chat Sem Estado (Painel Esquerdo)

Experimente primeiro aqui. Pergunte "O meu nome é João" e depois imediatamente "Qual é o meu nome?" O modelo não vai lembrar porque cada mensagem é independente. Isto demonstra o problema central da integração básica de modelos de linguagem – sem contexto de conversa.

<img src="../../../translated_images/pt-PT/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo Chat Sem Estado" width="800"/>

*IA não se lembra do seu nome da mensagem anterior*

### Chat Com Estado (Painel Direito)

Agora experimente a mesma sequência aqui. Pergunte "O meu nome é João" e depois "Qual é o meu nome?" Desta vez ele lembra-se. A diferença é o MessageWindowChatMemory – mantém o histórico da conversa e inclui-o em cada pedido. É assim que a IA conversacional de produção funciona.

<img src="../../../translated_images/pt-PT/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo Chat Com Estado" width="800"/>

*IA lembra-se do seu nome de momentos antes na conversa*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isto torna claro o que a memória traz para a sua aplicação e porque é essencial para casos reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, esteja ciente de que as traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional por um humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->