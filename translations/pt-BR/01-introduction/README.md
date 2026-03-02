# Módulo 01: Começando com LangChain4j

## Sumário

- [Videoaula](../../../01-introduction)
- [O Que Você Vai Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Compreendendo o Problema Principal](../../../01-introduction)
- [Compreendendo Tokens](../../../01-introduction)
- [Como a Memória Funciona](../../../01-introduction)
- [Como Isto Usa LangChain4j](../../../01-introduction)
- [Implantar Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Usando a Aplicação](../../../01-introduction)
  - [Chat Sem Estado (Painel Esquerdo)](../../../01-introduction)
  - [Chat com Estado (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## Videoaula

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Começando com LangChain4j - Sessão ao Vivo" width="800"/></a>

## O Que Você Vai Aprender

No início rápido, você usou Modelos do GitHub para enviar prompts, chamar ferramentas, construir um pipeline RAG e testar guardrails. Essas demonstrações mostraram o que é possível — agora mudamos para Azure OpenAI e GPT-5.2 e começamos a construir aplicações estilo produção. Este módulo foca em IA conversacional que lembra o contexto e mantém estado — os conceitos que aquelas demos iniciais usaram nos bastidores mas não explicaram.

Usaremos o GPT-5.2 do Azure OpenAI ao longo deste guia porque suas capacidades avançadas de raciocínio tornam o comportamento de diferentes padrões mais perceptível. Quando você adiciona memória, verá claramente a diferença. Isso facilita entender o que cada componente traz para sua aplicação.

Você construirá uma aplicação que demonstra ambos os padrões:

**Chat Sem Estado** - Cada requisição é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão que você usou no início rápido.

**Conversa com Estado** - Cada requisição inclui o histórico da conversa. O modelo mantém contexto ao longo de várias interações. Isso é o que aplicações de produção exigem.

## Pré-requisitos

- Assinatura Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) já estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. A implantação é configurada automaticamente via `azd up` - não modifique o nome do modelo no código.

## Compreendendo o Problema Principal

Modelos de linguagem são sem estado. Cada chamada API é independente. Se você enviar "Meu nome é João" e depois perguntar "Qual é o meu nome?", o modelo não sabe que você acabou de se apresentar. Ele trata cada requisição como se fosse a primeira conversa que você teve.

Isso é aceitável para perguntas e respostas simples, mas inútil para aplicações reais. Bots de atendimento ao cliente precisam lembrar o que você falou. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplos turnos requer memória.

O diagrama a seguir contrapõe as duas abordagens — à esquerda, uma chamada sem estado que esquece seu nome; à direita, uma chamada com estado suportada por ChatMemory que lembra dele.

<img src="../../../translated_images/pt-BR/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversas Stateless vs Stateful" width="800"/>

*A diferença entre conversas sem estado (chamadas independentes) e com estado (cientes do contexto)*

## Compreendendo Tokens

Antes de mergulhar nas conversas, é importante entender tokens - as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-BR/token-explanation.c39760d8ec650181.webp" alt="Explicação de Tokens" width="800"/>

*Exemplo de como o texto é dividido em tokens - "Eu amo IA!" se torna 4 unidades separadas para processamento*

Tokens são como modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. Seu modelo tem um limite de quantos tokens pode processar de uma vez (400.000 para GPT-5.2, com até 272.000 tokens de entrada e 128.000 tokens de saída). Compreender tokens ajuda a gerenciar o tamanho da conversa e custos.

## Como a Memória Funciona

A memória de chat resolve o problema do estado mantendo o histórico da conversa. Antes de enviar sua requisição para o modelo, o framework antepõe mensagens anteriores relevantes. Quando você pergunta "Qual é o meu nome?", o sistema na verdade envia todo o histórico da conversa, permitindo que o modelo veja que você disse antes "Meu nome é João".

LangChain4j fornece implementações de memória que cuidam disso automaticamente. Você escolhe quantas mensagens reter e o framework gerencia a janela de contexto. O diagrama abaixo mostra como MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes.

<img src="../../../translated_images/pt-BR/memory-window.bbe67f597eadabb3.webp" alt="Conceito de Janela de Memória" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, descartando automaticamente as antigas*

## Como Isto Usa LangChain4j

Este módulo expande o início rápido integrando Spring Boot e adicionando memória de conversa. Veja como as partes se encaixam:

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

O builder lê credenciais a partir de variáveis de ambiente definidas pelo `azd up`. Definir `baseUrl` para seu endpoint Azure faz o cliente OpenAI funcionar com Azure OpenAI.

**Memória de Conversa** - Acompanhe o histórico de chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens de usuário e IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie ao modelo. O serviço armazena instâncias de memória separadas por ID de conversa, permitindo múltiplos usuários conversarem simultaneamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando um banco de dados em vez de memória em RAM?"
> - "Como eu adicionaria sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat sem estado ignora memória completamente - apenas `chatModel.chat(prompt)` como no início rápido. O endpoint com estado adiciona mensagens à memória, recupera histórico e inclui esse contexto a cada requisição. Mesma configuração de modelo, padrões diferentes.

## Implantar Infraestrutura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selecione a assinatura e localização (eastus2 recomendado)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecione a assinatura e o local (eastus2 recomendado)
```

> **Nota:** Se você encontrar erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), simplesmente execute `azd up` novamente. Os recursos do Azure podem ainda estar sendo provisionados em segundo plano, e tentar novamente permite que a implantação complete quando os recursos atingirem estado terminal.

Isto irá:
1. Implantar recurso Azure OpenAI com modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente arquivo `.env` na raiz do projeto com credenciais
3. Configurar todas as variáveis de ambiente requeridas

**Problemas com implantação?** Veja o [README da Infraestrutura](infra/README.md) para solução detalhada de problemas incluindo conflitos de nomes de subdomínio, passos manuais para implantar pelo Portal Azure e orientações de configuração dos modelos.

**Verifique se a implantação foi bem sucedida:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o arquivo `.env`. Se precisar atualizá-lo depois, você pode editar manualmente ou regenerar rodando:
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

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` existe no diretório raiz com credenciais Azure. Execute isso no diretório do módulo (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie as aplicações:**

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O dev container inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Você pode encontrá-la na Activity Bar à esquerda do VS Code (procure o ícone do Spring Boot).

A partir do Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Ver logs da aplicação em tempo real
- Monitorar status da aplicação

Basta clicar no botão play ao lado de "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.69c7479aef09ff6b.webp" alt="Painel Spring Boot" width="400"/>

*O Spring Boot Dashboard no VS Code — inicie, pare e monitore todos os módulos de um lugar*

**Opção 2: Usando scripts shell**

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

Ambos os scripts carregam automaticamente variáveis de ambiente do arquivo `.env` da raiz e irão construir os JARs se eles não existirem.

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
.\stop.ps1  # Somente este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Usando a Aplicação

A aplicação fornece uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-BR/home-screen.121a03206ab910c0.webp" alt="Tela Inicial da Aplicação" width="800"/>

*Dashboard mostrando opções de Chat Simples (sem estado) e Chat Conversacional (com estado)*

### Chat Sem Estado (Painel Esquerdo)

Experimente primeiro. Pergunte "Meu nome é João" e então imediatamente pergunte "Qual é o meu nome?" O modelo não irá lembrar porque cada mensagem é independente. Isso demonstra o problema principal da integração básica do modelo de linguagem - sem contexto de conversa.

<img src="../../../translated_images/pt-BR/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demonstração Chat Sem Estado" width="800"/>

*IA não lembra seu nome da mensagem anterior*

### Chat com Estado (Painel Direito)

Agora tente a mesma sequência aqui. Pergunte "Meu nome é João" e depois "Qual é o meu nome?" Dessa vez ele lembra. A diferença é o MessageWindowChatMemory - ele mantém o histórico da conversa e o inclui em cada requisição. É assim que funciona IA conversacional de produção.

<img src="../../../translated_images/pt-BR/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demonstração Chat com Estado" width="800"/>

*IA lembra seu nome de antes na conversa*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isso deixa claro o que a memória traz para sua aplicação e porque é essencial para casos de uso reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, é importante estar ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->