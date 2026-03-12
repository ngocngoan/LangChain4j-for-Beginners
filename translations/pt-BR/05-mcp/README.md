# Módulo 05: Protocolo de Contexto do Modelo (MCP)

## Índice

- [Video Walkthrough](../../../05-mcp)
- [O Que Você Vai Aprender](../../../05-mcp)
- [O Que é MCP?](../../../05-mcp)
- [Como o MCP Funciona](../../../05-mcp)
- [O Módulo Agente](../../../05-mcp)
- [Executando os Exemplos](../../../05-mcp)
  - [Pré-requisitos](../../../05-mcp)
- [Início Rápido](../../../05-mcp)
  - [Operações com Arquivos (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Executando o Demo](../../../05-mcp)
    - [Como o Supervisor Funciona](../../../05-mcp)
    - [Como o FileAgent Descobre Ferramentas MCP em Tempo de Execução](../../../05-mcp)
    - [Estratégias de Resposta](../../../05-mcp)
    - [Entendendo a Saída](../../../05-mcp)
    - [Explicação dos Recursos do Módulo Agente](../../../05-mcp)
- [Conceitos-Chave](../../../05-mcp)
- [Parabéns!](../../../05-mcp)
  - [E Agora?](../../../05-mcp)

## Video Walkthrough

Assista esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agentes de IA com Ferramentas e MCP - Sessão Ao Vivo" width="800"/></a>

## O Que Você Vai Aprender

Você já construiu IA conversacional, dominou prompts, fundamentou respostas em documentos e criou agentes com ferramentas. Mas todas essas ferramentas foram feitas sob medida para sua aplicação específica. E se você pudesse dar à sua IA acesso a um ecossistema padronizado de ferramentas que qualquer pessoa pode criar e compartilhar? Neste módulo, você vai aprender exatamente isso com o Protocolo de Contexto do Modelo (MCP) e o módulo agente do LangChain4j. Primeiro, mostramos um leitor de arquivos MCP simples e depois mostramos como ele se integra facilmente em fluxos de trabalho avançados de agentes usando o padrão Agente Supervisor.

## O Que é MCP?

O Protocolo de Contexto do Modelo (MCP) oferece exatamente isso - uma maneira padrão para aplicações de IA descobrirem e usarem ferramentas externas. Em vez de escrever integrações personalizadas para cada fonte de dados ou serviço, você se conecta a servidores MCP que expõem suas capacidades em um formato consistente. Seu agente de IA pode então descobrir e usar essas ferramentas automaticamente.

O diagrama abaixo mostra a diferença — sem MCP, cada integração exige ligação ponto a ponto personalizada; com MCP, um único protocolo conecta seu app a qualquer ferramenta:

<img src="../../../translated_images/pt-BR/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparação MCP" width="800"/>

*Antes do MCP: Integrações ponto a ponto complexas. Depois do MCP: Um protocolo, infinitas possibilidades.*

O MCP resolve um problema fundamental no desenvolvimento de IA: cada integração é personalizada. Quer acessar o GitHub? Código personalizado. Quer ler arquivos? Código personalizado. Quer consultar um banco de dados? Código personalizado. E nenhuma dessas integrações funciona com outras aplicações de IA.

O MCP padroniza isso. Um servidor MCP expõe ferramentas com descrições claras e esquemas. Qualquer cliente MCP pode conectar-se, descobrir ferramentas disponíveis e usá-las. Construa uma vez, use em qualquer lugar.

O diagrama abaixo ilustra esta arquitetura — um único cliente MCP (sua aplicação de IA) conecta-se a vários servidores MCP, cada um expondo seu próprio conjunto de ferramentas através do protocolo padrão:

<img src="../../../translated_images/pt-BR/mcp-architecture.b3156d787a4ceac9.webp" alt="Arquitetura MCP" width="800"/>

*Arquitetura do Protocolo de Contexto do Modelo - descoberta e execução padronizadas de ferramentas*

## Como o MCP Funciona

Por trás das cenas, o MCP usa uma arquitetura em camadas. Sua aplicação Java (o cliente MCP) descobre ferramentas disponíveis, envia requisições JSON-RPC através de uma camada de transporte (Stdio ou HTTP), e o servidor MCP executa operações e retorna resultados. O diagrama a seguir detalha cada camada deste protocolo:

<img src="../../../translated_images/pt-BR/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalhes do Protocolo MCP" width="800"/>

*Como o MCP funciona por baixo dos panos — clientes descobrem ferramentas, trocam mensagens JSON-RPC, e executam operações através de uma camada de transporte.*

**Arquitetura Cliente-Servidor**

O MCP usa um modelo cliente-servidor. Servidores fornecem ferramentas - leitura de arquivos, consulta a bancos de dados, chamadas de APIs. Clientes (sua aplicação de IA) conectam-se aos servidores e usam suas ferramentas.

Para usar o MCP com LangChain4j, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoberta de Ferramentas**

Quando seu cliente conecta a um servidor MCP, ele pergunta "Quais ferramentas você tem?" O servidor responde com uma lista de ferramentas disponíveis, cada uma com descrições e esquemas de parâmetros. Seu agente de IA pode então decidir quais ferramentas usar com base nas requisições do usuário. O diagrama abaixo mostra este aperto de mão — o cliente envia uma requisição `tools/list` e o servidor retorna suas ferramentas disponíveis, com descrições e esquemas de parâmetros:

<img src="../../../translated_images/pt-BR/tool-discovery.07760a8a301a7832.webp" alt="Descoberta de Ferramentas MCP" width="800"/>

*A IA descobre as ferramentas disponíveis na inicialização — agora sabe quais capacidades estão disponíveis e pode decidir quais usar.*

**Mecanismos de Transporte**

O MCP suporta diferentes mecanismos de transporte. As duas opções são Stdio (para comunicação local entre subprocessos) e HTTP Streamable (para servidores remotos). Este módulo demonstra o transporte Stdio:

<img src="../../../translated_images/pt-BR/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mecanismos de Transporte" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para processos locais*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para processos locais. Sua aplicação inicia um servidor como subprocesso e se comunica através da entrada/saída padrão. Útil para acesso ao sistema de arquivos ou ferramentas de linha de comando.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

O servidor `@modelcontextprotocol/server-filesystem` expõe as seguintes ferramentas, todas limitadas aos diretórios que você especificar:

| Ferramenta | Descrição |
|------------|-----------|
| `read_file` | Ler o conteúdo de um único arquivo |
| `read_multiple_files` | Ler múltiplos arquivos em uma chamada |
| `write_file` | Criar ou sobrescrever um arquivo |
| `edit_file` | Fazer edições específicas de busca e substituição |
| `list_directory` | Listar arquivos e diretórios em um caminho |
| `search_files` | Buscar recursivamente arquivos que correspondam a um padrão |
| `get_file_info` | Obter metadados do arquivo (tamanho, timestamps, permissões) |
| `create_directory` | Criar um diretório (incluindo diretórios pais) |
| `move_file` | Mover ou renomear um arquivo ou diretório |

O diagrama a seguir mostra como o transporte Stdio funciona em tempo de execução — sua aplicação Java inicia o servidor MCP como um processo filho e eles se comunicam via pipes stdin/stdout, sem rede ou HTTP envolvidos:

<img src="../../../translated_images/pt-BR/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Fluxo do Transporte Stdio" width="800"/>

*Transporte Stdio em ação — sua aplicação inicia o servidor MCP como processo filho e se comunica via pipes stdin/stdout.*

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e pergunte:
> - "Como funciona o transporte Stdio e quando devo usá-lo em vez de HTTP?"
> - "Como o LangChain4j gerencia o ciclo de vida dos processos MCP iniciados?"
> - "Quais são as implicações de segurança ao dar à IA acesso ao sistema de arquivos?"

## O Módulo Agente

Enquanto o MCP fornece ferramentas padronizadas, o módulo **agentic** do LangChain4j oferece uma maneira declarativa de construir agentes que orquestram essas ferramentas. A anotação `@Agent` e o `AgenticServices` permitem definir o comportamento do agente por meio de interfaces, em vez de código imperativo.

Neste módulo, você vai explorar o padrão de IA agente avançado **Agente Supervisor** — onde um agente "supervisor" decide dinamicamente quais sub-agentes chamar com base nas requisições do usuário. Vamos combinar ambos conceitos dando a um dos nossos sub-agentes capacidades de acesso a arquivos potenciadas por MCP.

Para usar o módulo agente, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** O módulo `langchain4j-agentic` usa uma propriedade de versão separada (`langchain4j.mcp.version`) porque é lançado em uma programação diferente das bibliotecas principais do LangChain4j.

> **⚠️ Experimental:** O módulo `langchain4j-agentic` é **experimental** e sujeito a mudanças. A forma estável de construir assistentes de IA continua sendo o `langchain4j-core` com ferramentas personalizadas (Módulo 04).

## Executando os Exemplos

### Pré-requisitos

- Conclusão do [Módulo 04 - Ferramentas](../04-tools/README.md) (este módulo baseia-se em conceitos de ferramentas personalizadas e os compara com ferramentas MCP)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (para servidores MCP)

> **Nota:** Se você ainda não configurou suas variáveis de ambiente, veja [Módulo 01 - Introdução](../01-introduction/README.md) para instruções de implantação (`azd up` cria o arquivo `.env` automaticamente), ou copie `.env.example` para `.env` no diretório raiz e preencha seus valores.

## Início Rápido

**Usando VS Code:** Simplesmente clique com o botão direito em qualquer arquivo demo no Explorer e selecione **"Run Java"**, ou use as configurações de lançamento no painel Run and Debug (certifique-se de que seu arquivo `.env` está configurado com credenciais Azure primeiro).

**Usando Maven:** Alternativamente, você pode rodar pela linha de comando com os exemplos abaixo.

### Operações com Arquivos (Stdio)

Este demonstra ferramentas baseadas em subprocessos locais.

**✅ Nenhum pré-requisito necessário** - o servidor MCP é iniciado automaticamente.

**Usando os Scripts de Inicialização (Recomendado):**

Os scripts de inicialização carregam automaticamente as variáveis de ambiente do arquivo `.env` na raiz:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**Usando VS Code:** Clique com o botão direito em `StdioTransportDemo.java` e selecione **"Run Java"** (certifique-se de que seu arquivo `.env` está configurado).

A aplicação inicia automaticamente um servidor MCP do sistema de arquivos e lê um arquivo local. Note como o gerenciamento do subprocesso é feito para você.

**Saída esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

O **padrão Agente Supervisor** é uma forma **flexível** de IA agentic. Um Supervisor usa um LLM para decidir autonomamente quais agentes invocar com base na solicitação do usuário. No próximo exemplo, combinamos acesso a arquivos MCP com um agente LLM para criar um fluxo de trabalho supervisionado de leitura de arquivo → relatório.

No demo, o `FileAgent` lê um arquivo usando ferramentas MCP do sistema de arquivos, e o `ReportAgent` gera um relatório estruturado com um resumo executivo (1 frase), 3 pontos-chave e recomendações. O Supervisor orquestra esse fluxo automaticamente:

<img src="../../../translated_images/pt-BR/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Padrão Agente Supervisor" width="800"/>

*O Supervisor usa seu LLM para decidir quais agentes chamar e em que ordem — roteamento codificado não é necessário.*

Veja como é o fluxo concreto do pipeline de arquivo para relatório:

<img src="../../../translated_images/pt-BR/file-report-workflow.649bb7a896800de9.webp" alt="Fluxo Arquivo para Relatório" width="800"/>

*O FileAgent lê o arquivo via ferramentas MCP, depois o ReportAgent transforma o conteúdo em um relatório estruturado.*

O diagrama de sequência abaixo traça a orquestração completa do Supervisor — desde a inicialização do servidor MCP, passando pela seleção autônoma de agentes pelo Supervisor, até as chamadas de ferramentas via stdio e o relatório final:

<img src="../../../translated_images/pt-BR/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Diagrama de Sequência do Agente Supervisor" width="800"/>

*O Supervisor invoca autonomamente o FileAgent (que chama o servidor MCP via stdio para ler o arquivo), depois invoca o ReportAgent para gerar um relatório estruturado — cada agente armazena seu resultado no Escopo Agente compartilhado.*

Cada agente armazena sua saída no **Escopo Agente** (memória compartilhada), permitindo que agentes posteriores acessem resultados anteriores. Isso demonstra como ferramentas MCP se integram perfeitamente em fluxos agentic — o Supervisor não precisa saber *como* os arquivos são lidos, apenas que o `FileAgent` pode fazer isso.

#### Executando o Demo

Os scripts de inicialização carregam automaticamente as variáveis de ambiente do arquivo `.env` na raiz:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**Usando VS Code:** Clique com o botão direito em `SupervisorAgentDemo.java` e selecione **"Run Java"** (certifique-se de que seu arquivo `.env` está configurado).

#### Como o Supervisor Funciona

Antes de construir agentes, você precisa conectar o transporte MCP a um cliente e envolvê-lo como um `ToolProvider`. É assim que as ferramentas do servidor MCP ficam disponíveis para seus agentes:

```java
// Crie um cliente MCP a partir do transporte
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Envolva o cliente como um ToolProvider — isso conecta as ferramentas MCP ao LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Agora você pode injetar `mcpToolProvider` em qualquer agente que precise de ferramentas MCP:

```java
// Etapa 1: FileAgent lê arquivos usando ferramentas MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Possui ferramentas MCP para operações com arquivos
        .build();

// Etapa 2: ReportAgent gera relatórios estruturados
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor coordena o fluxo de trabalho arquivo → relatório
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retornar o relatório final
        .build();

// O Supervisor decide quais agentes invocar com base na solicitação
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Como o FileAgent Descobre Ferramentas MCP em Tempo de Execução

Você pode estar se perguntando: **como o `FileAgent` sabe como usar as ferramentas npm do sistema de arquivos?** A resposta é que ele não sabe — o **LLM** descobre isso em tempo de execução através dos esquemas das ferramentas.
A interface `FileAgent` é apenas uma **definição de prompt**. Ela não possui conhecimento codificado sobre `read_file`, `list_directory` ou qualquer outra ferramenta MCP. Veja o que acontece de ponta a ponta:

1. **Servidor inicia:** `StdioMcpTransport` lança o pacote npm `@modelcontextprotocol/server-filesystem` como um processo filho
2. **Descoberta de ferramentas:** O `McpClient` envia uma requisição JSON-RPC `tools/list` para o servidor, que responde com nomes de ferramentas, descrições e esquemas de parâmetros (por exemplo, `read_file` — *"Ler o conteúdo completo de um arquivo"* — `{ path: string }`)
3. **Injeção de esquema:** `McpToolProvider` envolve esses esquemas descobertos e os torna disponíveis para LangChain4j
4. **Decisão do LLM:** Quando `FileAgent.readFile(path)` é chamado, o LangChain4j envia a mensagem do sistema, mensagem do usuário, **e a lista de esquemas de ferramentas** para o LLM. O LLM lê as descrições das ferramentas e gera uma chamada de ferramenta (por exemplo, `read_file(path="/some/file.txt")`)
5. **Execução:** LangChain4j intercepta a chamada da ferramenta, encaminha-a através do cliente MCP de volta para o subprocesso Node.js, obtém o resultado e o alimenta de volta para o LLM

Este é o mesmo mecanismo de [Descoberta de Ferramentas](../../../05-mcp) descrito acima, mas aplicado especificamente ao fluxo de trabalho do agente. As anotações `@SystemMessage` e `@UserMessage` guiam o comportamento do LLM, enquanto o `ToolProvider` injetado fornece as **capacidades** — o LLM conecta os dois em tempo de execução.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Abra [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) e pergunte:
> - "Como este agente sabe qual ferramenta MCP chamar?"
> - "O que aconteceria se eu removesse o ToolProvider do construtor do agente?"
> - "Como os esquemas das ferramentas são passados para o LLM?"

#### Estratégias de Resposta

Ao configurar um `SupervisorAgent`, você especifica como ele deve formular sua resposta final ao usuário após os sub-agentes terem completado suas tarefas. O diagrama abaixo mostra as três estratégias disponíveis — LAST retorna diretamente a saída final do agente, SUMMARY sintetiza todas as saídas através de um LLM, e SCORED escolhe aquela que pontuar melhor em relação à solicitação original:

<img src="../../../translated_images/pt-BR/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Três estratégias para como o Supervisor formula sua resposta final — escolha com base se você quer a saída do último agente, um resumo sintetizado ou a opção com melhor pontuação.*

As estratégias disponíveis são:

| Estratégia | Descrição |
|------------|-----------|
| **LAST**  | O supervisor retorna a saída do último sub-agente ou ferramenta chamada. Isso é útil quando o agente final do fluxo foi projetado especificamente para produzir a resposta completa e final (ex.: um "Agente de Resumo" em uma pipeline de pesquisa). |
| **SUMMARY** | O supervisor usa seu próprio Modelo de Linguagem (LLM) interno para sintetizar um resumo de toda a interação e das saídas dos sub-agentes, retornando esse resumo como resposta final. Isso fornece uma resposta limpa e agregada para o usuário. |
| **SCORED** | O sistema usa um LLM interno para pontuar tanto a resposta LAST quanto o SUMMARY da interação em relação à solicitação original do usuário, retornando a saída que recebeu a maior pontuação. |

Veja a implementação completa em [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java).

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Abra [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e pergunte:
> - "Como o Supervisor decide quais agentes invocar?"
> - "Qual a diferença entre os padrões de fluxo Supervisor e Sequencial?"
> - "Como posso customizar o comportamento de planejamento do Supervisor?"

#### Entendendo a Saída

Ao executar a demonstração, você verá um passo a passo estruturado de como o Supervisor orquestra múltiplos agentes. Veja o que cada seção significa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**O cabeçalho** introduz o conceito do fluxo de trabalho: uma pipeline focada da leitura de arquivos até a geração de relatórios.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**Diagrama do Fluxo de Trabalho** mostra o fluxo de dados entre os agentes. Cada agente tem um papel específico:
- **FileAgent** lê arquivos usando ferramentas MCP e armazena o conteúdo bruto em `fileContent`
- **ReportAgent** consome esse conteúdo e produz um relatório estruturado em `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Solicitação do Usuário** mostra a tarefa. O Supervisor a analisa e decide invocar FileAgent → ReportAgent.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Orquestração do Supervisor** mostra a execução em 2 etapas:
1. **FileAgent** lê o arquivo via MCP e armazena o conteúdo
2. **ReportAgent** recebe o conteúdo e gera um relatório estruturado

O Supervisor tomou essas decisões **autonomamente** com base na solicitação do usuário.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Explicação dos Recursos do Módulo Agentic

O exemplo demonstra várias funcionalidades avançadas do módulo agentic. Vamos olhar mais de perto o Escopo Agentic e os Listeners de Agentes.

**Escopo Agentic** mostra a memória compartilhada onde os agentes armazenaram seus resultados usando `@Agent(outputKey="...")`. Isso permite:
- Que agentes posteriores acessem as saídas dos agentes anteriores
- Que o Supervisor sintetize uma resposta final
- Que você inspecione o que cada agente produziu

O diagrama abaixo mostra como o Escopo Agentic funciona como memória compartilhada no fluxo de arquivo para relatório — FileAgent grava sua saída sob a chave `fileContent`, ReportAgent a lê e grava sua própria saída sob `report`:

<img src="../../../translated_images/pt-BR/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Escopo Agentic atua como memória compartilhada — FileAgent escreve `fileContent`, ReportAgent lê e escreve `report`, e seu código lê o resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dados brutos do arquivo do FileAgent
String report = scope.readState("report");            // Relatório estruturado do ReportAgent
```

**Listeners de Agentes** possibilitam monitoramento e depuração da execução dos agentes. A saída passo a passo que você vê na demo vem de um AgentListener que se conecta a cada invocação do agente:
- **beforeAgentInvocation** - Chamado quando o Supervisor seleciona um agente, permitindo que você veja qual agente foi escolhido e por quê
- **afterAgentInvocation** - Chamado quando um agente completa, mostrando seu resultado
- **inheritedBySubagents** - Quando verdadeiro, o listener monitora todos os agentes na hierarquia

O diagrama a seguir mostra o ciclo de vida completo do Agent Listener, inclusive como o `onError` trata falhas durante a execução do agente:

<img src="../../../translated_images/pt-BR/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Listeners de Agentes conectam-se ao ciclo de vida da execução — monitore quando agentes iniciam, completam ou encontram erros.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // Propagar para todos os sub-agentes
    }
};
```

Além do padrão Supervisor, o módulo `langchain4j-agentic` fornece vários padrões poderosos de fluxo de trabalho. O diagrama abaixo mostra todos os cinco — de pipelines simples sequenciais até fluxos de aprovação com checkpoint humano:

<img src="../../../translated_images/pt-BR/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinco padrões de fluxo para orquestrar agentes — de pipelines simples sequenciais até fluxos de aprovação com humano no loop.*

| Padrão | Descrição | Caso de Uso |
|--------|-----------|-------------|
| **Sequencial** | Executa agentes em ordem, saída flui para o próximo | Pipelines: pesquisa → análise → relatório |
| **Paralelo** | Executa agentes simultaneamente | Tarefas independentes: clima + notícias + ações |
| **Loop** | Itera até a condição ser satisfeita | Avaliação de qualidade: refinamento até nota ≥ 0,8 |
| **Condicional** | Roteia com base em condições | Classificar → direcionar para agente especialista |
| **Humano no Loop** | Adiciona checkpoints humanos | Fluxos de aprovação, revisão de conteúdo |

## Conceitos-Chave

Agora que você explorou o MCP e o módulo agentic em ação, vamos resumir quando usar cada abordagem.

Uma das maiores vantagens do MCP é seu ecossistema crescente. O diagrama abaixo mostra como um protocolo universal conecta sua aplicação de IA a uma ampla variedade de servidores MCP — desde acesso a sistemas de arquivos e bancos de dados até GitHub, email, scraping web e mais:

<img src="../../../translated_images/pt-BR/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP cria um ecossistema de protocolo universal — qualquer servidor compatível com MCP funciona com qualquer cliente compatível, permitindo compartilhamento de ferramentas entre aplicações.*

**MCP** é ideal quando você quer aproveitar ecossistemas de ferramentas existentes, construir ferramentas para serem compartilhadas por múltiplas aplicações, integrar serviços terceiros com protocolos padrão, ou trocar implementações de ferramentas sem mudar código.

**O Módulo Agentic** funciona melhor quando você quer definições declarativas de agentes com anotações `@Agent`, precisa de orquestração de fluxo (sequencial, loop, paralelo), prefere design de agentes baseado em interfaces em vez de código imperativo, ou está combinando múltiplos agentes que compartilham saídas via `outputKey`.

**O padrão Supervisor Agent** brilha quando o fluxo não é previsível antecipadamente e você quer que o LLM decida, quando tem múltiplos agentes especializados que precisam de orquestração dinâmica, quando constrói sistemas conversacionais que roteiam para diferentes capacidades, ou quando quer o comportamento de agente mais flexível e adaptativo.

Para ajudar na escolha entre os métodos personalizados `@Tool` do Módulo 04 e ferramentas MCP deste módulo, a comparação abaixo destaca os principais trade-offs — ferramentas personalizadas dão acoplamento estreito e total segurança de tipo para lógica específica, enquanto ferramentas MCP oferecem integrações padronizadas e reutilizáveis:

<img src="../../../translated_images/pt-BR/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Quando usar métodos customizados @Tool vs ferramentas MCP — ferramentas customizadas para lógica específica da aplicação com total segurança tipada, ferramentas MCP para integrações padronizadas que funcionam entre aplicações.*

## Parabéns!

Você completou os cinco módulos do curso LangChain4j para Iniciantes! Aqui está um panorama da jornada completa que você realizou — do chat básico até sistemas agentic com poder MCP:

<img src="../../../translated_images/pt-BR/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Sua jornada de aprendizado pelos cinco módulos — do chat básico a sistemas agentic com poder MCP.*

Você aprendeu:

- Como construir IA conversacional com memória (Módulo 01)
- Padrões de engenharia de prompt para diferentes tarefas (Módulo 02)
- Fundamentar respostas em seus documentos com RAG (Módulo 03)
- Criar agentes básicos de IA (assistentes) com ferramentas customizadas (Módulo 04)
- Integrar ferramentas padronizadas com os módulos LangChain4j MCP e Agentic (Módulo 05)

### E agora?

Após completar os módulos, explore o [Guia de Testes](../docs/TESTING.md) para ver conceitos de teste em LangChain4j em ação.

**Recursos Oficiais:**
- [Documentação LangChain4j](https://docs.langchain4j.dev/) – Guias completos e referência de API
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) – Código-fonte e exemplos
- [Tutoriais LangChain4j](https://docs.langchain4j.dev/tutorials/) – Tutoriais passo a passo para vários casos de uso

Obrigado por completar este curso!

---

**Navegação:** [← Módulo Anterior: 04 - Tools](../04-tools/README.md) | [Voltar ao Início](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional feita por um humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações errôneas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->