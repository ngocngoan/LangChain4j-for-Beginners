# Módulo 01: Começando com LangChain4j

## Índice

- [Vídeo Tutorial](../../../01-introduction)
- [O que Você Vai Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Entendendo o Problema Central](../../../01-introduction)
- [Entendendo Tokens](../../../01-introduction)
- [Como a Memória Funciona](../../../01-introduction)
- [Como Isso Usa LangChain4j](../../../01-introduction)
- [Implantar Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Usando a Aplicação](../../../01-introduction)
  - [Chat Stateless (Painel Esquerdo)](../../../01-introduction)
  - [Chat Stateful (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## Vídeo Tutorial

Assista a esta sessão ao vivo que explica como começar neste módulo: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## O que Você Vai Aprender

Se você completou o início rápido, viu como enviar prompts e receber respostas. Essa é a base, mas aplicações reais precisam de mais. Este módulo ensina como construir IA conversacional que lembra do contexto e mantém o estado - a diferença entre uma demonstração isolada e uma aplicação pronta para produção.

Usaremos o GPT-5.2 da Azure OpenAI ao longo deste guia porque suas capacidades avançadas de raciocínio tornam o comportamento dos diferentes padrões mais evidente. Quando você adiciona memória, verá claramente a diferença. Isso facilita entender o que cada componente traz para sua aplicação.

Você construirá uma aplicação que demonstra ambos os padrões:

**Chat Stateless** - Cada solicitação é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão que você usou no início rápido.

**Conversa Stateful** - Cada solicitação inclui o histórico da conversa. O modelo mantém o contexto em múltiplas interações. Isso é o que aplicações em produção exigem.

## Pré-requisitos

- Assinatura Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. A implantação é configurada automaticamente via `azd up` - não modifique o nome do modelo no código.

## Entendendo o Problema Central

Modelos de linguagem são stateless. Cada chamada de API é independente. Se você enviar "Meu nome é João" e depois perguntar "Qual é o meu nome?", o modelo não tem ideia de que você acabou de se apresentar. Ele trata cada solicitação como se fosse a primeira conversa que você teve.

Isso é aceitável para Q&A simples, mas inútil para aplicações reais. Bots de atendimento ao cliente precisam lembrar o que você disse. Assistentes pessoais precisam de contexto. Qualquer conversa multi-turno requer memória.

<img src="../../../translated_images/pt-BR/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Conversas Stateless vs Stateful" width="800"/>

*A diferença entre conversas stateless (chamadas independentes) e stateful (com consciência de contexto)*

## Entendendo Tokens

Antes de mergulhar em conversas, é importante entender tokens - as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-BR/token-explanation.c39760d8ec650181.webp" alt="Explicação de Token" width="800"/>

*Exemplo de como o texto é dividido em tokens - "Eu amo IA!" se torna 4 unidades de processamento separadas*

Tokens são como modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. Seu modelo tem um limite de quantos tokens pode processar de uma vez (400.000 para GPT-5.2, com até 272.000 tokens de entrada e 128.000 de saída). Entender tokens ajuda a gerenciar o tamanho da conversa e os custos.

## Como a Memória Funciona

A memória do chat resolve o problema stateless mantendo o histórico da conversa. Antes de enviar sua solicitação para o modelo, o framework adiciona mensagens anteriores relevantes. Quando você pergunta "Qual é o meu nome?", o sistema na verdade envia todo o histórico da conversa, permitindo que o modelo veja que você disse "Meu nome é João" anteriormente.

LangChain4j fornece implementações de memória que fazem isso automaticamente. Você escolhe quantas mensagens reter e o framework gerencia a janela de contexto.

<img src="../../../translated_images/pt-BR/memory-window.bbe67f597eadabb3.webp" alt="Conceito de Janela de Memória" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, descartando automaticamente as antigas*

## Como Isso Usa LangChain4j

Este módulo estende o início rápido integrando Spring Boot e adicionando memória na conversa. Veja como as partes se encaixam:

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

O builder lê credenciais das variáveis de ambiente definidas pelo `azd up`. Definir `baseUrl` para seu endpoint Azure faz o cliente OpenAI funcionar com Azure OpenAI.

**Memória da Conversa** - Rastreamento do histórico do chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie memória com `withMaxMessages(10)` para manter as últimas 10 mensagens. Adicione mensagens do usuário e da IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie para o modelo. O serviço armazena instâncias de memória separadas por ID da conversa, permitindo múltiplos usuários conversarem simultaneamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando um banco de dados em vez de memória volátil?"
> - "Como eu adicionaria sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat stateless ignora a memória totalmente - apenas `chatModel.chat(prompt)` como no início rápido. O endpoint stateful adiciona mensagens à memória, recupera o histórico e inclui esse contexto em cada solicitação. Mesma configuração de modelo, padrões diferentes.

## Implantar Infraestrutura Azure OpenAI

**Bash:**
```bash
cd 01-introduction
azd up  # Selecione a assinatura e a localização (eastus2 recomendado)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecione a assinatura e a localização (eastus2 recomendado)
```

> **Nota:** Se você encontrar um erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), execute `azd up` novamente. Os recursos do Azure podem ainda estar sendo provisionados em segundo plano, e tentar novamente permite que a implantação seja concluída assim que os recursos atingirem estado terminal.

Isso irá:
1. Implantar recurso Azure OpenAI com modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente arquivo `.env` na raiz do projeto com credenciais
3. Configurar todas as variáveis de ambiente necessárias

**Está tendo problemas na implantação?** Veja o [README da Infraestrutura](infra/README.md) para resolução detalhada incluindo conflitos de nome de subdomínio, etapas manuais para implantação pelo Portal Azure e orientações de configuração do modelo.

**Verifique se a implantação foi bem-sucedida:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o arquivo `.env`. Se precisar atualizá-lo depois, você pode editar o arquivo `.env` manualmente ou re-gerá-lo executando:
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

Certifique-se que o arquivo `.env` existe no diretório raiz com as credenciais Azure:

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

O dev container inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure pelo ícone Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs das aplicações em tempo real
- Monitorar o status das aplicações

Basta clicar no botão de play próximo a "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usando scripts shell**

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

Ambos os scripts carregam automaticamente variáveis de ambiente do arquivo `.env` na raiz e compilarão os JARs se eles não existirem.

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

Abra http://localhost:8080 em seu navegador.

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

A aplicação oferece uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-BR/home-screen.121a03206ab910c0.webp" alt="Tela Inicial da Aplicação" width="800"/>

*Dashboard mostrando as opções de Simple Chat (stateless) e Conversational Chat (stateful)*

### Chat Stateless (Painel Esquerdo)

Experimente primeiro. Pergunte "Meu nome é João" e logo em seguida "Qual é o meu nome?" O modelo não vai lembrar porque cada mensagem é independente. Isso demonstra o problema central da integração básica de modelos de linguagem - sem contexto da conversa.

<img src="../../../translated_images/pt-BR/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demonstração Chat Stateless" width="800"/>

*IA não lembra seu nome da mensagem anterior*

### Chat Stateful (Painel Direito)

Agora tente a mesma sequência aqui. Pergunte "Meu nome é João" e depois "Qual é o meu nome?" Desta vez ele lembra. A diferença é o MessageWindowChatMemory - ele mantém o histórico da conversa e inclui isso em cada solicitação. É assim que IA conversacional em produção funciona.

<img src="../../../translated_images/pt-BR/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demonstração Chat Stateful" width="800"/>

*IA lembra seu nome de antes na conversa*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isso deixa claro o que a memória traz para sua aplicação e por que é essencial para casos de uso reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Principal](../README.md) | [Próximo: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos empenhemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma original deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->