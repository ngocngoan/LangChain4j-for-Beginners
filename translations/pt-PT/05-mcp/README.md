# Módulo 05: Protocolo de Contexto do Modelo (MCP)

## Índice

- [O que vai aprender](../../../05-mcp)
- [O que é o MCP?](../../../05-mcp)
- [Como funciona o MCP](../../../05-mcp)
- [O Módulo Agentic](../../../05-mcp)
- [Executar os exemplos](../../../05-mcp)
  - [Pré-requisitos](../../../05-mcp)
- [Início rápido](../../../05-mcp)
  - [Operações de ficheiros (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Executar a demonstração](../../../05-mcp)
    - [Como funciona o Supervisor](../../../05-mcp)
    - [Como o FileAgent descobre ferramentas MCP em runtime](../../../05-mcp)
    - [Estratégias de resposta](../../../05-mcp)
    - [Entender a saída](../../../05-mcp)
    - [Explicação das funcionalidades do Módulo Agentic](../../../05-mcp)
- [Conceitos-chave](../../../05-mcp)
- [Parabéns!](../../../05-mcp)
  - [Qual é o próximo passo?](../../../05-mcp)

## O que vai aprender

Já construiu IA conversacional, dominou prompts, baseou respostas em documentos e criou agentes com ferramentas. Mas todas essas ferramentas foram feitas à medida para a sua aplicação específica. E se pudesse dar à sua IA acesso a um ecossistema padronizado de ferramentas que qualquer pessoa pode criar e partilhar? Neste módulo, vai aprender exatamente isso com o Protocolo de Contexto do Modelo (MCP) e o módulo agentic do LangChain4j. Apresentamos primeiro um leitor de ficheiros MCP simples e depois mostramos como este se integra facilmente em fluxos de trabalho agentic avançados usando o padrão Supervisor Agent.

## O que é o MCP?

O Protocolo de Contexto do Modelo (MCP) fornece exatamente isso - uma forma padrão para aplicações de IA descobrirem e utilizarem ferramentas externas. Em vez de escrever integrações personalizadas para cada fonte de dados ou serviço, liga-se a servidores MCP que expõem as suas capacidades num formato consistente. O seu agente de IA pode então descobrir e usar essas ferramentas automaticamente.

O diagrama abaixo mostra a diferença — sem MCP, cada integração requer ligações ponto a ponto personalizadas; com MCP, um único protocolo conecta a sua aplicação a qualquer ferramenta:

<img src="../../../translated_images/pt-PT/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Antes do MCP: integrações complexas ponto a ponto. Depois do MCP: um protocolo, possibilidades infinitas.*

O MCP resolve um problema fundamental no desenvolvimento de IA: toda integração é personalizada. Quer aceder ao GitHub? Código personalizado. Quer ler ficheiros? Código personalizado. Quer consultar uma base de dados? Código personalizado. E nenhuma dessas integrações funciona com outras aplicações de IA.

O MCP padroniza isto. Um servidor MCP expõe ferramentas com descrições claras e esquemas. Qualquer cliente MCP pode conectar-se, descobrir as ferramentas disponíveis e usá-las. Desenvolva uma vez, use em todo o lado.

O diagrama abaixo ilustra esta arquitetura — um único cliente MCP (a sua aplicação de IA) conecta-se a múltiplos servidores MCP, cada um expondo o seu conjunto de ferramentas através do protocolo padrão:

<img src="../../../translated_images/pt-PT/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arquitetura do Protocolo de Contexto do Modelo - descoberta e execução de ferramentas padronizadas*

## Como funciona o MCP

Por baixo do capô, o MCP usa uma arquitetura em camadas. A sua aplicação Java (o cliente MCP) descobre as ferramentas disponíveis, envia pedidos JSON-RPC através de uma camada de transporte (Stdio ou HTTP), e o servidor MCP executa operações e devolve resultados. O diagrama seguinte detalha cada camada deste protocolo:

<img src="../../../translated_images/pt-PT/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Como o MCP funciona por baixo do capô — os clientes descobrem ferramentas, trocam mensagens JSON-RPC e executam operações através de uma camada de transporte.*

**Arquitetura Servidor-Cliente**

O MCP usa um modelo cliente-servidor. Os servidores fornecem ferramentas - leitura de ficheiros, consultas a bases de dados, chamadas a APIs. Os clientes (a sua aplicação de IA) ligam-se aos servidores e usam as ferramentas.

Para usar o MCP com LangChain4j, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoberta de Ferramentas**

Quando o seu cliente se liga a um servidor MCP, pergunta "Que ferramentas tens?" O servidor responde com uma lista de ferramentas disponíveis, cada uma com descrições e esquemas de parâmetros. O seu agente IA pode então decidir quais as ferramentas a usar com base nos pedidos do utilizador. O diagrama abaixo mostra este handshake — o cliente envia um pedido `tools/list` e o servidor devolve as suas ferramentas disponíveis com descrições e esquemas de parâmetros:

<img src="../../../translated_images/pt-PT/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*A IA descobre as ferramentas disponíveis no arranque — agora sabe quais as capacidades disponíveis e pode decidir quais usar.*

**Mecanismos de Transporte**

O MCP suporta diferentes mecanismos de transporte. As duas opções são Stdio (para comunicação local com subprocessos) e Streamable HTTP (para servidores remotos). Este módulo demonstra o transporte Stdio:

<img src="../../../translated_images/pt-PT/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para processos locais*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para processos locais. A sua aplicação inicia um servidor como subprocesso e comunica através da entrada/saída padrão. Útil para acesso ao sistema de ficheiros ou ferramentas de linha de comando.

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

O servidor `@modelcontextprotocol/server-filesystem` expõe as seguintes ferramentas, todas confinadas aos diretórios que especificar:

| Ferramenta | Descrição |
|------|-------------|
| `read_file` | Lê o conteúdo de um único ficheiro |
| `read_multiple_files` | Lê múltiplos ficheiros numa única chamada |
| `write_file` | Cria ou sobrescreve um ficheiro |
| `edit_file` | Faz edições específicas de localizar e substituir |
| `list_directory` | Lista ficheiros e diretórios numa determinada localização |
| `search_files` | Pesquisa recursivamente ficheiros que correspondem a um padrão |
| `get_file_info` | Obtém metadados do ficheiro (tamanho, datas, permissões) |
| `create_directory` | Cria um diretório (incluindo diretórios pai) |
| `move_file` | Move ou renomeia um ficheiro ou diretório |

O diagrama seguinte mostra como o transporte Stdio funciona em tempo de execução — a sua aplicação Java inicia o servidor MCP como um processo filho e comunicam pelos pipes stdin/stdout, sem rede ou HTTP envolvidos:

<img src="../../../translated_images/pt-PT/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transporte Stdio em ação — a sua aplicação inicia o servidor MCP como processo filho e comunica pelos pipes stdin/stdout.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e pergunte:
> - "Como funciona o transporte Stdio e quando devo usá-lo em vez do HTTP?"
> - "Como o LangChain4j gere o ciclo de vida dos processos do servidor MCP lançados?"
> - "Quais são as implicações de segurança de dar acesso da IA ao sistema de ficheiros?"

## O Módulo Agentic

Enquanto o MCP fornece ferramentas padronizadas, o módulo **agentic** do LangChain4j fornece uma forma declarativa de construir agentes que orquestram essas ferramentas. A anotação `@Agent` e o `AgenticServices` permitem definir o comportamento do agente por interfaces em vez de código imperativo.

Neste módulo, vai explorar o padrão **Supervisor Agent** — uma abordagem de IA agentic avançada onde um agente "supervisor" decide dinamicamente quais sub-agentes invocar com base nos pedidos do utilizador. Vamos combinar ambos os conceitos ao dar a um dos nossos sub-agentes capacidades de acesso a ficheiros potenciadas pelo MCP.

Para usar o módulo agentic, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** O módulo `langchain4j-agentic` usa uma propriedade de versão separada (`langchain4j.mcp.version`) porque é lançado num calendário diferente do núcleo das bibliotecas LangChain4j.

> **⚠️ Experimental:** O módulo `langchain4j-agentic` é **experimental** e sujeito a alterações. A forma estável de construir assistentes de IA continua a ser `langchain4j-core` com ferramentas personalizadas (Módulo 04).

## Executar os exemplos

### Pré-requisitos

- Ter concluído o [Módulo 04 - Ferramentas](../04-tools/README.md) (este módulo baseia-se nos conceitos de ferramentas personalizadas e compara-os com ferramentas MCP)
- Ficheiro `.env` na raiz do diretório com credenciais Azure (criado pelo `azd up` no Módulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (para servidores MCP)

> **Nota:** Se ainda não configurou as suas variáveis de ambiente, veja [Módulo 01 - Introdução](../01-introduction/README.md) para instruções de deployment (`azd up` cria o ficheiro `.env` automaticamente), ou copie `.env.example` para `.env` no diretório raiz e preencha os seus valores.

## Início rápido

**Usando o VS Code:** Clique com o botão direito em qualquer ficheiro de demonstração no Explorador e selecione **"Run Java"**, ou use as configurações de lançamento no painel de Execução e Depuração (certifique-se de que o ficheiro `.env` está configurado com as credenciais Azure primeiro).

**Usando Maven:** Em alternativa, pode executar pela linha de comandos com os exemplos abaixo.

### Operações de ficheiros (Stdio)

Demonstra ferramentas baseadas em subprocessos locais.

**✅ Sem pré-requisitos necessários** - o servidor MCP é lançado automaticamente.

**Usando os scripts de arranque (recomendado):**

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz:

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

**Usando VS Code:** Clique com o botão direito em `StdioTransportDemo.java` e selecione **"Run Java"** (certifique-se de que o ficheiro `.env` está configurado).

A aplicação inicia automaticamente um servidor MCP de sistema de ficheiros e lê um ficheiro local. Repare como a gestão do subprocesso é tratada para si.

**Saída esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

O padrão **Supervisor Agent** é uma forma **flexível** de IA agentic. Um Supervisor usa um LLM para decidir autonomamente quais agentes invocar com base no pedido do utilizador. No próximo exemplo, combinamos acesso a ficheiros potenciados por MCP com um agente LLM para criar um fluxo de trabalho de leitura de ficheiro → relatório supervisionado.

Na demonstração, o `FileAgent` lê um ficheiro usando ferramentas de sistema de ficheiros MCP, e o `ReportAgent` gera um relatório estruturado com um resumo executivo (1 frase), 3 pontos-chave e recomendações. O Supervisor orquestra este fluxo automaticamente:

<img src="../../../translated_images/pt-PT/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*O Supervisor usa o seu LLM para decidir quais agentes invocar e em que ordem — não é necessário um roteamento codificado.*

Aqui está o fluxo concreto para a nossa pipeline de ficheiro para relatório:

<img src="../../../translated_images/pt-PT/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*O FileAgent lê o ficheiro via ferramentas MCP, depois o ReportAgent transforma o conteúdo bruto num relatório estruturado.*

O seguinte diagrama de sequência traça toda a orquestração do Supervisor — desde o lançamento do servidor MCP, passando pela seleção autónoma de agentes do Supervisor, até às chamadas de ferramentas via stdio e o relatório final:

<img src="../../../translated_images/pt-PT/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*O Supervisor invoca autonomamente o FileAgent (que chama o servidor MCP via stdio para ler o ficheiro), depois invoca o ReportAgent para gerar um relatório estruturado — cada agente guarda a sua saída no Agentic Scope partilhado.*

Cada agente guarda a sua saída no **Agentic Scope** (memória partilhada), permitindo que agentes subsequentes acedam a resultados anteriores. Isto demonstra como as ferramentas MCP se integram perfeitamente em fluxos de trabalho agentic — o Supervisor não precisa de saber *como* os ficheiros são lidos, apenas que o `FileAgent` pode fazê-lo.

#### Executar a demonstração

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz:

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

**Usando VS Code:** Clique com o botão direito em `SupervisorAgentDemo.java` e selecione **"Run Java"** (certifique-se de que o ficheiro `.env` está configurado).

#### Como funciona o Supervisor

Antes de construir agentes, precisa de ligar o transporte MCP a um cliente e empacotá-lo como um `ToolProvider`. É assim que as ferramentas do servidor MCP ficam disponíveis para os seus agentes:

```java
// Criar um cliente MCP a partir do transporte
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Envolver o cliente como um ToolProvider — isto liga as ferramentas MCP ao LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Agora pode injetar `mcpToolProvider` em qualquer agente que necessite de ferramentas MCP:

```java
// Passo 1: O FileAgent lê ficheiros usando as ferramentas MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Tem ferramentas MCP para operações de ficheiros
        .build();

// Passo 2: O ReportAgent gera relatórios estruturados
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// O Supervisor coordena o fluxo de trabalho ficheiro → relatório
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Devolver o relatório final
        .build();

// O Supervisor decide quais os agentes a invocar com base no pedido
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Como o FileAgent descobre ferramentas MCP em runtime

Pode perguntar-se: **como é que o `FileAgent` sabe usar as ferramentas de sistema de ficheiros npm?** A resposta é que não sabe — é o **LLM** que descobre em runtime através dos esquemas das ferramentas.

A interface `FileAgent` é apenas uma **definição de prompt**. Não tem conhecimento codificado de `read_file`, `list_directory` ou qualquer outra ferramenta MCP. Eis o que acontece de ponta a ponta:
1. **Servidor inicia:** `StdioMcpTransport` lança o pacote npm `@modelcontextprotocol/server-filesystem` como um processo filho  
2. **Descoberta da ferramenta:** O `McpClient` envia um pedido JSON-RPC `tools/list` ao servidor, que responde com nomes de ferramentas, descrições e esquemas de parâmetros (ex.: `read_file` — *"Ler o conteúdo completo de um ficheiro"* — `{ path: string }`)  
3. **Injeção do esquema:** `McpToolProvider` envolve estes esquemas descobertos e torna-os disponíveis ao LangChain4j  
4. **Decisão do LLM:** Quando `FileAgent.readFile(path)` é chamado, o LangChain4j envia a mensagem do sistema, mensagem do utilizador, **e a lista de esquemas de ferramenta** para o LLM. O LLM lê as descrições das ferramentas e gera uma chamada de ferramenta (ex.: `read_file(path="/some/file.txt")`)  
5. **Execução:** O LangChain4j intercepta a chamada da ferramenta, encaminha-a pelo cliente MCP de volta ao subprocesso Node.js, obtém o resultado e devolve-o ao LLM  

Este é o mesmo mecanismo de [Descoberta de Ferramentas](../../../05-mcp) descrito acima, mas aplicado especificamente ao fluxo de trabalho do agente. As anotações `@SystemMessage` e `@UserMessage` orientam o comportamento do LLM, enquanto o `ToolProvider` injetado lhe confere as **capacidades** — o LLM estabelece a ligação entre ambos em tempo de execução.

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) e pergunte:  
> - "Como é que este agente sabe qual a ferramenta MCP para chamar?"  
> - "O que aconteceria se removesse o ToolProvider do construtor do agente?"  
> - "Como é que os esquemas das ferramentas são passados para o LLM?"  

#### Estratégias de Resposta

Quando configura um `SupervisorAgent`, especifica como deve formular a sua resposta final ao utilizador após os sub-agentes terem completado as suas tarefas. O diagrama abaixo mostra as três estratégias disponíveis — LAST devolve diretamente o output do último agente, SUMMARY sintetiza todos os outputs através de um LLM, e SCORED escolhe aquele com melhor pontuação face ao pedido original:

<img src="../../../translated_images/pt-PT/response-strategies.3d0cea19d096bdf9.webp" alt="Estratégias de Resposta" width="800"/>

*Três estratégias para a forma como o Supervisor formula a resposta final — escolha com base em se quer o output do último agente, um resumo sintetizado, ou a opção com a melhor pontuação.*

As estratégias disponíveis são:

| Estratégia | Descrição |
|------------|-----------|
| **LAST**   | O supervisor devolve o output do último sub-agente ou ferramenta chamado. Útil quando o agente final no fluxo é especificamente projetado para produzir a resposta final completa (ex.: um "Agente de Sumário" numa pipeline de investigação). |
| **SUMMARY**| O supervisor usa o seu próprio Modelo de Linguagem (LLM) interno para sintetizar um resumo de toda a interação e de todos os outputs dos sub-agentes, e depois devolve esse resumo como resposta final. Isso fornece uma resposta agregada e limpa ao utilizador. |
| **SCORED** | O sistema utiliza um LLM interno para pontuar tanto a resposta LAST como o SUMMARY da interação contrariamente ao pedido original do utilizador, devolvendo aquele que obtém a melhor pontuação. |

Veja [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para a implementação completa.

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e pergunte:  
> - "Como decide o Supervisor quais agentes invocar?"  
> - "Qual é a diferença entre os padrões Supervisor e Sequencial?"  
> - "Como posso personalizar o comportamento de planeamento do Supervisor?"  

#### Compreender o Output

Ao correr a demo verá uma apresentação estruturada de como o Supervisor orquestra múltiplos agentes. Eis o que cada secção significa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**O cabeçalho** introduz o conceito do fluxo de trabalho: uma pipeline focada da leitura de ficheiros até à geração de relatórios.

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
  
**Diagrama do Fluxo de Trabalho** mostra o fluxo de dados entre agentes. Cada agente tem um papel específico:  
- **FileAgent** lê ficheiros usando ferramentas MCP e armazena o conteúdo bruto em `fileContent`  
- **ReportAgent** consome esse conteúdo e produz um relatório estruturado em `report`  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**Pedido do Utilizador** mostra a tarefa. O Supervisor interpreta e decide invocar FileAgent → ReportAgent.

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
  
**Orquestração do Supervisor** mostra a execução em 2 passos:  
1. **FileAgent** lê o ficheiro via MCP e armazena o conteúdo  
2. **ReportAgent** recebe o conteúdo e gera um relatório estruturado  

O Supervisor tomou estas decisões **de forma autónoma** com base no pedido do utilizador.

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
  
#### Explicação das Funcionalidades do Módulo Agente

O exemplo demonstra várias funcionalidades avançadas do módulo agentic. Vamos analisar de perto o Agentic Scope e os Agent Listeners.

**Agentic Scope** mostra a memória partilhada onde os agentes guardaram os seus resultados usando `@Agent(outputKey="...")`. Isto permite:  
- Agentes posteriores acederem aos outputs dos agentes anteriores  
- O Supervisor sintetizar uma resposta final  
- Poder inspecionar o que cada agente produziu

O diagrama abaixo mostra como o Agentic Scope funciona como memória partilhada no fluxo de ficheiro para relatório — FileAgent escreve o seu output sob a chave `fileContent`, ReportAgent lê esse e escreve o seu próprio output sob `report`:

<img src="../../../translated_images/pt-PT/agentic-scope.95ef488b6c1d02ef.webp" alt="Memória Partilhada Agentic Scope" width="800"/>

*Agentic Scope atua como memória partilhada — FileAgent escreve `fileContent`, ReportAgent lê e escreve `report`, e o seu código lê o resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dados brutos do ficheiro do FileAgent
String report = scope.readState("report");            // Relatório estruturado do ReportAgent
```
  
**Agent Listeners** permitem monitorização e depuração da execução dos agentes. O output passo-a-passo que vê na demo provém de um AgentListener que se liga a cada invocação do agente:  
- **beforeAgentInvocation** - Chamado quando o Supervisor seleciona um agente, permitindo ver qual agente foi escolhido e porquê  
- **afterAgentInvocation** - Chamado quando um agente termina, mostrando o seu resultado  
- **inheritedBySubagents** - Quando verdadeiro, o listener monitoriza todos os agentes na hierarquia  

O diagrama seguinte mostra todo o ciclo de vida dos Agent Listeners, incluindo como o `onError` trata falhas durante a execução do agente:

<img src="../../../translated_images/pt-PT/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo de Vida dos Agent Listeners" width="800"/>

*Os Agent Listeners ligam-se ao ciclo de vida da execução — monitorizam quando agentes começam, terminam, ou encontram erros.*

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
  
Para além do padrão Supervisor, o módulo `langchain4j-agentic` oferece vários padrões de workflow poderosos. O diagrama abaixo mostra os cinco padrões — desde pipelines sequenciais simples até workflows de aprovação com intervenção humana:

<img src="../../../translated_images/pt-PT/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Padrões de Workflow de Agentes" width="800"/>

*Cinco padrões de workflow para orquestrar agentes — desde pipelines sequenciais simples até workflows de aprovação com intervenção humana.*

| Padrão           | Descrição                        | Caso de Uso                             |
|------------------|---------------------------------|---------------------------------------|
| **Sequencial**   | Executar agentes em ordem, output flui para o próximo | Pipelines: investigação → análise → relatório |
| **Paralelo**     | Executar agentes simultaneamente | Tarefas independentes: meteorologia + notícias + bolsa |
| **Loop**         | Iterar até condição ser satisfeita | Pontuação de qualidade: refinar até pontuação ≥ 0.8 |
| **Condicional**  | Encaminhar com base em condições | Classificar → encaminhar para agente especialista |
| **Humano no Loop** | Adicionar pontos de verificação humanos | Workflows de aprovação, revisão de conteúdo |

## Conceitos-Chave

Agora que explorou o MCP e o módulo agentic em ação, vamos resumir quando usar cada abordagem.

Uma das maiores vantagens do MCP é o seu ecossistema em crescimento. O diagrama abaixo mostra como um único protocolo universal conecta a sua aplicação AI a uma grande variedade de servidores MCP — desde acesso a sistemas de ficheiros e bases de dados até GitHub, email, web scraping, e mais:

<img src="../../../translated_images/pt-PT/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecossistema MCP" width="800"/>

*MCP cria um ecossistema de protocolo universal — qualquer servidor compatível com MCP funciona com qualquer cliente compatível, permitindo partilha de ferramentas entre aplicações.*

**MCP** é ideal quando quer aproveitar ecossistemas de ferramentas existentes, construir ferramentas que várias aplicações possam partilhar, integrar serviços de terceiros com protocolos padrão, ou trocar implementações das ferramentas sem mudar código.

**O Módulo Agentic** funciona melhor quando quer definições declarativas de agentes com anotações `@Agent`, precisa de orquestração de workflows (sequencial, loop, paralelo), prefere design de agentes baseado em interfaces em vez de código imperativo, ou está a combinar múltiplos agentes que partilham outputs via `outputKey`.

**O padrão Supervisor Agent** brilha quando o workflow não é previsível a priori e quer que o LLM decida, quando tem vários agentes especializados que precisam de orquestração dinâmica, quando está a construir sistemas conversacionais que encaminham para diferentes capacidades, ou quando quer o comportamento de agente mais flexível e adaptativo.

Para ajudar a decidir entre os métodos customizados `@Tool` do Módulo 04 e as ferramentas MCP deste módulo, a seguinte comparação destaca as principais diferenças — ferramentas customizadas dão-lhe acoplamento forte e segurança de tipos para lógica específica da app, enquanto ferramentas MCP oferecem integrações padronizadas e reutilizáveis:

<img src="../../../translated_images/pt-PT/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Ferramentas Customizadas vs MCP" width="800"/>

*Quando usar métodos customizados @Tool vs ferramentas MCP — ferramentas customizadas para lógica app-específica com total segurança de tipos, ferramentas MCP para integrações padronizadas que funcionam entre aplicações.*

## Parabéns!

Concluiu os cinco módulos do curso LangChain4j para Iniciantes! Aqui está uma visão geral da sua jornada de aprendizagem completa — desde chat básico até sistemas agentic com poder MCP:

<img src="../../../translated_images/pt-PT/course-completion.48cd201f60ac7570.webp" alt="Conclusão do Curso" width="800"/>

*A sua jornada de aprendizagem através dos cinco módulos — desde chat básico a sistemas agentic com poder MCP.*

Concluiu o curso LangChain4j para Iniciantes. Aprendeu:

- Como construir AI conversacional com memória (Módulo 01)  
- Padrões de engenharia de prompts para diferentes tarefas (Módulo 02)  
- Fundamentos de respostas ancoradas em documentos com RAG (Módulo 03)  
- Criar agentes AI básicos (assistentes) com ferramentas customizadas (Módulo 04)  
- Integrar ferramentas padronizadas com os módulos LangChain4j MCP e Agentic (Módulo 05)  

### O que vem a seguir?

Depois de completar os módulos, explore o [Guia de Testes](../docs/TESTING.md) para ver conceitos de testes LangChain4j em ação.

**Recursos Oficiais:**  
- [Documentação LangChain4j](https://docs.langchain4j.dev/) - Guias abrangentes e referência API  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Código fonte e exemplos  
- [Tutoriais LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriais passo a passo para vários casos de uso  

Obrigado por completar este curso!

---

**Navegação:** [← Anterior: Módulo 04 - Ferramentas](../04-tools/README.md) | [Voltar ao Início](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->