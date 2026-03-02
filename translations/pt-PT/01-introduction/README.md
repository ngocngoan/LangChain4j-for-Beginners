# Módulo 01: Começar com LangChain4j

## Índice

- [Vídeo Explicativo](../../../01-introduction)
- [O Que Vai Aprender](../../../01-introduction)
- [Pré-requisitos](../../../01-introduction)
- [Compreender o Problema Central](../../../01-introduction)
- [Compreender Tokens](../../../01-introduction)
- [Como Funciona a Memória](../../../01-introduction)
- [Como Isto Usa LangChain4j](../../../01-introduction)
- [Desplegar Infraestrutura Azure OpenAI](../../../01-introduction)
- [Executar a Aplicação Localmente](../../../01-introduction)
- [Usar a Aplicação](../../../01-introduction)
  - [Chat Sem Estado (Painel Esquerdo)](../../../01-introduction)
  - [Chat Com Estado (Painel Direito)](../../../01-introduction)
- [Próximos Passos](../../../01-introduction)

## Vídeo Explicativo

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## O Que Vai Aprender

Se completou o início rápido, viu como enviar prompts e obter respostas. Essa é a base, mas aplicações reais precisam de mais. Este módulo ensina como construir IA conversacional que lembra o contexto e mantém estado – a diferença entre uma demo pontual e uma aplicação pronta para produção.

Vamos usar o GPT-5.2 da Azure OpenAI ao longo deste guia porque as suas capacidades avançadas de raciocínio tornam o comportamento dos vários padrões mais evidente. Ao adicionar memória, verá claramente a diferença. Isto facilita a compreensão do que cada componente traz à sua aplicação.

Construirá uma aplicação que demonstra ambos os padrões:

**Chat Sem Estado** – Cada pedido é independente. O modelo não tem memória das mensagens anteriores. Este é o padrão usado no início rápido.

**Conversa Com Estado** – Cada pedido inclui o histórico da conversa. O modelo mantém o contexto ao longo de múltiplas interações. É isto que as aplicações de produção necessitam.

## Pré-requisitos

- Subscrição Azure com acesso ao Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Nota:** Java, Maven, Azure CLI e Azure Developer CLI (azd) estão pré-instalados no devcontainer fornecido.

> **Nota:** Este módulo usa GPT-5.2 no Azure OpenAI. O deployment é configurado automaticamente via `azd up` – não modifique o nome do modelo no código.

## Compreender o Problema Central

Os modelos de linguagem são stateless. Cada chamada API é independente. Se disser "Chamo-me John" e depois perguntar "Qual é o meu nome?", o modelo não tem ideia de que acabou de se apresentar. Trata cada pedido como se fosse a primeira conversa que já teve.

Isto é aceitável para perguntas e respostas simples mas inútil para aplicações reais. Bots de atendimento ao cliente precisam de lembrar o que lhes disse. Assistentes pessoais precisam de contexto. Qualquer conversa com múltiplos turnos requer memória.

<img src="../../../translated_images/pt-PT/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*A diferença entre conversas stateless (chamadas independentes) e stateful (com consciência de contexto)*

## Compreender Tokens

Antes de mergulhar nas conversas, é importante compreender tokens – as unidades básicas de texto que os modelos de linguagem processam:

<img src="../../../translated_images/pt-PT/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Exemplo de como o texto é separado em tokens – "I love AI!" torna-se 4 unidades distintas para processamento*

Os tokens são como os modelos de IA medem e processam texto. Palavras, pontuação e até espaços podem ser tokens. O seu modelo tem um limite de tokens que pode processar de cada vez (400.000 para GPT-5.2, com até 272.000 tokens de entrada e 128.000 tokens de saída). Compreender os tokens ajuda a gerir o comprimento da conversa e os custos.

## Como Funciona a Memória

A memória de chat resolve o problema stateless ao manter o histórico da conversa. Antes de enviar o seu pedido ao modelo, a framework antepõe mensagens anteriores relevantes. Quando pergunta "Qual é o meu nome?", o sistema envia na verdade todo o histórico da conversa, permitindo ao modelo ver que disse anteriormente "Chamo-me John."

O LangChain4j fornece implementações de memória que tratam disto automaticamente. Escolhe quantas mensagens reter e a framework gere a janela de contexto.

<img src="../../../translated_images/pt-PT/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory mantém uma janela deslizante das mensagens recentes, excluindo automaticamente as antigas*

## Como Isto Usa LangChain4j

Este módulo estende o início rápido integrando Spring Boot e adicionando memória à conversa. Eis como as peças encaixam:

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

O construtor lê as credenciais das variáveis de ambiente definidas por `azd up`. Definir `baseUrl` para o seu endpoint Azure faz o cliente OpenAI funcionar com Azure OpenAI.

**Memória da Conversa** – Rastreie o histórico do chat com MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Crie a memória com `withMaxMessages(10)` para reter as últimas 10 mensagens. Adicione mensagens do utilizador e da IA com wrappers tipados: `UserMessage.from(text)` e `AiMessage.from(text)`. Recupere o histórico com `memory.messages()` e envie-o ao modelo. O serviço armazena instâncias de memória separadas por ID de conversa, permitindo múltiplos utilizadores conversarem simultaneamente.

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) e pergunte:
> - "Como é que o MessageWindowChatMemory decide quais mensagens descartar quando a janela está cheia?"
> - "Posso implementar armazenamento de memória personalizado usando uma base de dados em vez da memória em RAM?"
> - "Como poderia adicionar sumarização para comprimir o histórico antigo da conversa?"

O endpoint de chat stateless ignora totalmente a memória – apenas `chatModel.chat(prompt)` como no início rápido. O endpoint stateful adiciona mensagens à memória, recupera o histórico e inclui o contexto com cada pedido. Mesma configuração do modelo, padrões diferentes.

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

> **Nota:** Se encontrar erro de timeout (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), execute `azd up` novamente. Os recursos Azure podem ainda estar a ser provisionados em segundo plano, e tentar novamente permite que o deployment termine quando os recursos alcançarem um estado terminal.

Isto fará:
1. Desplegar o recurso Azure OpenAI com os modelos GPT-5.2 e text-embedding-3-small
2. Gerar automaticamente o ficheiro `.env` na raiz do projeto com as credenciais
3. Configurar todas as variáveis de ambiente necessárias

**Está a ter problemas no deployment?** Consulte o [README da Infraestrutura](infra/README.md) para resolução detalhada, incluindo conflitos de nome de subdomínio, passos manuais no portal Azure, e orientação sobre configuração do modelo.

**Verifique se o deployment teve sucesso:**

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, etc.
```

> **Nota:** O comando `azd up` gera automaticamente o ficheiro `.env`. Se precisar de atualizá-lo mais tarde, pode editar manualmente o `.env` ou regenerá-lo executando:
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

**Verificar deployment:**

Assegure-se que o ficheiro `.env` existe na diretoria raiz com as credenciais Azure:

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

O dev container inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).

A partir do Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis na workspace
- Iniciar/parar aplicações com um clique
- Ver logs da aplicação em tempo real
- Monitorizar o estado das aplicações

Basta clicar no botão de play junto a "introduction" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz e constroem os JARs caso ainda não existam.

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

A aplicação fornece uma interface web com duas implementações de chat lado a lado.

<img src="../../../translated_images/pt-PT/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard mostrando as opções de Chat Simples (sem estado) e Chat Conversacional (com estado)*

### Chat Sem Estado (Painel Esquerdo)

Experimente primeiro. Pergunte "Chamo-me John" e depois imediatamente pergunte "Qual é o meu nome?" O modelo não vai lembrar porque cada mensagem é independente. Isto demonstra o problema central da integração básica de modelos de linguagem – falta de contexto na conversa.

<img src="../../../translated_images/pt-PT/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*A IA não lembra o seu nome da mensagem anterior*

### Chat Com Estado (Painel Direito)

Agora experimente a mesma sequência aqui. Pergunte "Chamo-me John" e depois "Qual é o meu nome?" Desta vez lembra-se. A diferença é o MessageWindowChatMemory – mantém o histórico da conversa e inclui-o em cada pedido. É assim que a IA conversacional de produção funciona.

<img src="../../../translated_images/pt-PT/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*A IA lembra o seu nome de conversas anteriores*

Ambos os painéis usam o mesmo modelo GPT-5.2. A única diferença é a memória. Isto torna claro o que a memória traz à sua aplicação e porque é essencial para casos de uso reais.

## Próximos Passos

**Próximo Módulo:** [02-prompt-engineering - Engenharia de Prompt com GPT-5.2](../02-prompt-engineering/README.md)

---

**Navegação:** [← Anterior: Módulo 00 - Início Rápido](../00-quick-start/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 02 - Engenharia de Prompt →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, por favor esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->