# Módulo 01: Introdução ao LangChain4j

## Sumário

- [Vídeo Tutorial](../../../01-introduction)
- [O que você vai aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Entendendo o problema central](../../../01-introduction)
- [Entendendo tokens](../../../01-introduction)
- [Como a memória funciona](../../../01-introduction)
- [Como isso usa LangChain4j](../../../01-introduction)
- [Implantar infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a aplicação localmente](../../../01-introduction)
- [Usando a aplicação](../../../01-introduction)
  - [Chat sem estado (painel esquerdo)](../../../01-introduction)
  - [Chat com estado (painel direito)](../../../01-introduction)
- [Próximos passos](../../../01-introduction)

## Vídeo Tutorial

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## O que você vai aprender

Se você completou o início rápido, viu como enviar prompts e obter respostas. Essa é a base, mas aplicações reais precisam de mais. Este módulo te ensina a construir IA conversacional que lembra do contexto e mantém o estado — a diferença entre um demo isolado e uma aplicação pronta para produção.

Usaremos o GPT-5.2 do Azure OpenAI ao longo deste guia porque suas capacidades avançadas de raciocínio tornam o comportamento dos diferentes padrões mais claro. Quando você adiciona memória, verá claramente a diferença. Isso facilita entender o que cada componente traz para sua aplicação.

Você construirá uma aplicação que demonstra ambos os padrões:

**Chat sem Estado** – Cada requisição é independente. O modelo não lembra das mensagens anteriores. Esse é o padrão usado no início rápido.

**Conversa com Estado** – Cada requisição inclui o histórico da conversa. O modelo mantém o contexto em múltiplas interações. É isso que aplicações de produção exigem.

## Pré-requisitos

- Assinatura Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) já estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. O deployment é configurado automaticamente via `azd up` — não modifique o nome do modelo no código.

## Entendendo o problema central

Modelos de linguagem são sem estado. Cada chamada de API é independente. Se você mandar "Meu nome é João" e depois perguntar "Qual é meu nome?", o modelo não tem ideia de que você acabou de se apresentar. Ele trata cada requisição como se fosse a primeira conversa que você teve.

Isso é aceitável para perguntas simples, mas inútil para aplicações reais. Bots de atendimento precisam lembrar o que você disse. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplas interações requer memória.

<img src="../../../translated_images/pt-BR/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*A diferença entre conversas sem estado (chamadas independentes) e com estado (conscientes do contexto)*

## Entendendo tokens

Antes de mergulhar nas conversas, é importante entender tokens — as unidades básicas de texto que modelos de linguagem processam:

<img src="../../../translated_images/pt-BR/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exemplo de como texto é dividido em tokens — "Eu amo IA!" vira 4 unidades de processamento separadas*

Tokens são como os modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. Seu modelo tem um limite de tokens que pode processar de uma vez (400.000 para GPT-5.2, com até 272.000 tokens de entrada e 128.000 de saída). Entender tokens ajuda a controlar o tamanho da conversa e custos.

## Como a memória funciona

A memória do chat resolve o problema da ausência de estado ao manter o histórico da conversa. Antes de enviar sua requisição ao modelo, o framework adiciona as mensagens anteriores relevantes. Quando você pergunta "Qual é meu nome?", o sistema envia todo o histórico da conversa, permitindo que o modelo veja que você disse "Meu nome é João" anteriormente.

LangChain4j oferece implementações de memória que fazem isso automaticamente. Você escolhe quantas mensagens reter e o framework gerencia a janela de contexto.

<img src="../../../translated_images/pt-BR/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, descartando automaticamente as mais antigas*

## Como isso usa LangChain4j

Este módulo amplia o início rápido integrando Spring Boot e adicionando memória conversacional. Veja como as peças se encaixam:

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

**Modelo de Chat** – Configure o Azure OpenAI como um bean Spring ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

O builder lê as credenciais das variáveis de ambiente definidas pelo `azd up`. Configurar `baseUrl` para seu endpoint Azure faz o cliente OpenAI funcionar com Azure OpenAI.

**Memória da Conversa** – Gerencie o histórico de chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie a memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens do usuário e da IA com os wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie para o modelo. O serviço armazena instâncias de memória separadas por ID de conversa, permitindo múltiplos usuários conversarem simultaneamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando um banco de dados em vez de memória em cache?"
> - "Como eu adicionaria sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat sem estado ignora a memória totalmente — é só `chatModel.chat(prompt)` como no início rápido. O endpoint com estado adiciona mensagens à memória, recupera o histórico e inclui esse contexto em cada requisição. Mesma configuração do modelo, padrões diferentes.

## Implantar infraestrutura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selecione assinatura e local (eastus2 recomendado)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecione a assinatura e a localização (eastus2 recomendado)
```

> **Nota:** Se encontrar erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), simplesmente execute `azd up` novamente. Os recursos Azure podem estar ainda sendo provisionados em segundo plano, e tentar de novo permite que o deployment termine quando os recursos estiverem num estado final.

Isso irá:
1. Implantar recurso Azure OpenAI com modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente o arquivo `.env` na raiz do projeto com as credenciais
3. Configurar todas as variáveis de ambiente necessárias

**Tendo problemas com o deployment?** Veja o [README da infraestrutura](infra/README.md) para solução detalhada de conflitos de nome de subdomínio, passos manuais no Portal Azure e orientações para configuração de modelo.

**Verifique se o deployment foi bem-sucedido:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o arquivo `.env`. Se precisar atualizá-lo depois, você pode editar manualmente o `.env` ou regenerá-lo executando:
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

## Execute a aplicação localmente

**Verifique o deployment:**

Confirme que o arquivo `.env` existe no diretório raiz com as credenciais Azure:

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie as aplicações:**

**Opção 1: Usando Spring Boot Dashboard (Recomendado para usuários VS Code)**

O devcontainer inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone Spring Boot).

Pelo Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Ver logs da aplicação em tempo real
- Monitorar o status das aplicações

Basta clicar no botão play ao lado de "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usar scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # Do diretório raiz
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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` na raiz e vão construir os JARs caso ainda não existam.

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

## Usando a aplicação

A aplicação oferece uma interface web com duas versões de chat lado a lado.

<img src="../../../translated_images/pt-BR/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Painel mostrando as opções de Chat Simples (sem estado) e Chat Conversacional (com estado)*

### Chat sem Estado (Painel Esquerdo)

Experimente primeiro este. Pergunte "Meu nome é João" e logo após "Qual é meu nome?" O modelo não lembrará porque cada mensagem é independente. Isso demonstra o problema central da integração básica de modelo de linguagem — sem contexto de conversa.

<img src="../../../translated_images/pt-BR/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*IA não lembra seu nome da mensagem anterior*

### Chat com Estado (Painel Direito)

Agora tente a mesma sequência aqui. Diga "Meu nome é João" e depois "Qual é meu nome?" Desta vez, a IA lembra. A diferença é o MessageWindowChatMemory — ele mantém o histórico da conversa e o inclui em cada requisição. É assim que funciona a IA conversacional em produção.

<img src="../../../translated_images/pt-BR/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*IA lembra seu nome de uma interação anterior*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isso deixa claro o que a memória adiciona à sua aplicação e por que ela é essencial para casos de uso reais.

## Próximos passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Principal](../README.md) | [Próximo: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->