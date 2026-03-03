# Módulo 05: Protocolo de Contexto do Modelo (MCP)

## Índice

- [O que vai aprender](../../../05-mcp)
- [O que é o MCP?](../../../05-mcp)
- [Como funciona o MCP](../../../05-mcp)
- [O Módulo Agente](../../../05-mcp)
- [Executar os exemplos](../../../05-mcp)
  - [Pré-requisitos](../../../05-mcp)
- [Início rápido](../../../05-mcp)
  - [Operações de ficheiros (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Executar a demonstração](../../../05-mcp)
    - [Como funciona o Supervisor](../../../05-mcp)
    - [Estratégias de resposta](../../../05-mcp)
    - [Compreender a saída](../../../05-mcp)
    - [Explicação das funcionalidades do Módulo Agente](../../../05-mcp)
- [Conceitos-chave](../../../05-mcp)
- [Parabéns!](../../../05-mcp)
  - [E agora?](../../../05-mcp)

## O que vai aprender

Já construiu IA conversacional, dominou prompts, fundamentou respostas em documentos e criou agentes com ferramentas. Mas todas essas ferramentas foram personalizadas para a sua aplicação específica. E se pudesse dar à sua IA acesso a um ecossistema padronizado de ferramentas que qualquer pessoa pode criar e partilhar? Neste módulo, vai aprender exatamente isso com o Protocolo de Contexto do Modelo (MCP) e o módulo agente do LangChain4j. Mostramos primeiro um leitor de ficheiros MCP simples e depois como ele se integra facilmente em fluxos de trabalho avançados de agentes usando o padrão Agente Supervisor.

## O que é o MCP?

O Protocolo de Contexto do Modelo (MCP) oferece exatamente isso — uma forma padrão para as aplicações de IA descobrirem e usarem ferramentas externas. Em vez de escrever integrações personalizadas para cada fonte de dados ou serviço, liga-se a servidores MCP que expõem as suas capacidades num formato consistente. O seu agente de IA pode depois descobrir e usar essas ferramentas automaticamente.

O diagrama abaixo mostra a diferença — sem MCP, cada integração requer ligação personalizada ponto a ponto; com MCP, um único protocolo liga a sua aplicação a qualquer ferramenta:

<img src="../../../translated_images/pt-PT/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Antes do MCP: Integrações complexas ponto a ponto. Depois do MCP: Um protocolo, possibilidades infinitas.*

O MCP resolve um problema fundamental no desenvolvimento de IA: cada integração é personalizada. Quer aceder ao GitHub? Código personalizado. Quer ler ficheiros? Código personalizado. Quer consultar uma base de dados? Código personalizado. E nenhuma destas integrações funciona com outras aplicações de IA.

O MCP padroniza isso. Um servidor MCP expõe ferramentas com descrições claras e esquemas. Qualquer cliente MCP pode ligar-se, descobrir as ferramentas disponíveis e usá-las. Construa uma vez, use em todo o lado.

O diagrama abaixo ilustra esta arquitetura — um único cliente MCP (a sua aplicação de IA) liga-se a múltiplos servidores MCP, cada um expondo o seu próprio conjunto de ferramentas através do protocolo padrão:

<img src="../../../translated_images/pt-PT/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arquitetura do Protocolo de Contexto do Modelo — descoberta e execução padronizadas de ferramentas*

## Como funciona o MCP

Por baixo, o MCP usa uma arquitetura em camadas. A sua aplicação Java (o cliente MCP) descobre as ferramentas disponíveis, envia pedidos JSON-RPC através de uma camada de transporte (Stdio ou HTTP), e o servidor MCP executa as operações e devolve os resultados. O diagrama seguinte detalha cada camada deste protocolo:

<img src="../../../translated_images/pt-PT/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Como o MCP funciona por baixo — clientes descobrem ferramentas, trocam mensagens JSON-RPC e executam operações através de uma camada de transporte.*

**Arquitetura Servidor-Cliente**

O MCP usa um modelo cliente-servidor. Os servidores fornecem ferramentas — ler ficheiros, consultar bases de dados, chamar APIs. Os clientes (a sua aplicação de IA) ligam-se aos servidores e usam as suas ferramentas.

Para usar o MCP com LangChain4j, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoberta de Ferramentas**

Quando o seu cliente se liga a um servidor MCP, pergunta "Que ferramentas tens?" O servidor responde com uma lista de ferramentas disponíveis, cada uma com descrições e esquemas de parâmetros. O seu agente de IA pode então decidir quais as ferramentas a usar com base nos pedidos do utilizador. O diagrama abaixo mostra esta troca de informações — o cliente envia um pedido `tools/list` e o servidor devolve as suas ferramentas disponíveis com descrições e esquemas de parâmetros:

<img src="../../../translated_images/pt-PT/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*A IA descobre as ferramentas disponíveis ao iniciar — agora sabe que capacidades estão acessíveis e pode decidir quais usar.*

**Mecanismos de Transporte**

O MCP suporta diferentes mecanismos de transporte. As duas opções são Stdio (para comunicação local com subprocessos) e HTTP Streamable (para servidores remotos). Este módulo demonstra o transporte Stdio:

<img src="../../../translated_images/pt-PT/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para processos locais*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para processos locais. A sua aplicação arranca um servidor como subprocesso e comunica através da entrada/saída padrão. Útil para acesso ao sistema de ficheiros ou ferramentas de linha de comando.

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
|------------|-----------|
| `read_file` | Ler o conteúdo de um único ficheiro |
| `read_multiple_files` | Ler múltiplos ficheiros numa só chamada |
| `write_file` | Criar ou sobrescrever um ficheiro |
| `edit_file` | Fazer edições específicas de encontrar e substituir |
| `list_directory` | Listar ficheiros e diretórios num caminho |
| `search_files` | Procurar recursivamente ficheiros que correspondam a um padrão |
| `get_file_info` | Obter metadados do ficheiro (tamanho, datas, permissões) |
| `create_directory` | Criar um diretório (incluindo diretórios pai) |
| `move_file` | Mover ou renomear um ficheiro ou diretório |

O diagrama seguinte mostra como o transporte Stdio funciona em tempo de execução — a sua aplicação Java arranca o servidor MCP como processo filho e comunicam através de pipes stdin/stdout, sem envolver rede ou HTTP:

<img src="../../../translated_images/pt-PT/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transporte Stdio em ação — a sua aplicação arranca o servidor MCP como processo filho e comunica através dos pipes stdin/stdout.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e pergunte:
> - "Como funciona o transporte Stdio e quando devo usá-lo em vez do HTTP?"
> - "Como o LangChain4j gere o ciclo de vida dos processos de servidores MCP arrancados?"
> - "Quais as implicações de segurança ao dar à IA acesso ao sistema de ficheiros?"

## O Módulo Agente

Enquanto o MCP fornece ferramentas padronizadas, o módulo **agentic** do LangChain4j oferece uma forma declarativa de construir agentes que orquestram essas ferramentas. A anotação `@Agent` e `AgenticServices` permitem definir o comportamento do agente por interfaces em vez de código imperativo.

Neste módulo, vai explorar o padrão **Agente Supervisor** — uma abordagem avançada de IA agentic onde um agente "supervisor" decide dinamicamente quais sub-agentes invocar com base nos pedidos do utilizador. Vamos combinar ambos os conceitos dando a um dos nossos sub-agentes capacidades de acesso a ficheiros potenciadas pelo MCP.

Para usar o módulo agentic, adicione esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** O módulo `langchain4j-agentic` usa uma propriedade de versão separada (`langchain4j.mcp.version`) porque é lançado num calendário diferente das bibliotecas principais do LangChain4j.

> **⚠️ Experimental:** O módulo `langchain4j-agentic` é **experimental** e sujeito a alterações. A forma estável de construir assistentes IA continua a ser `langchain4j-core` com ferramentas personalizadas (Módulo 04).

## Executar os exemplos

### Pré-requisitos

- Ter concluído o [Módulo 04 - Ferramentas](../04-tools/README.md) (este módulo baseia-se em conceitos de ferramentas personalizadas e compara-os com ferramentas MCP)
- Ficheiro `.env` na raiz do diretório com credenciais Azure (criado com `azd up` no Módulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (para servidores MCP)

> **Nota:** Se ainda não configurou as variáveis de ambiente, veja [Módulo 01 - Introdução](../01-introduction/README.md) para as instruções de implementação (`azd up` cria o ficheiro `.env` automaticamente), ou copie `.env.example` para `.env` na raiz e preencha os seus valores.

## Início rápido

**Usando VS Code:** Basta clicar com o botão direito em qualquer ficheiro de demonstração no Explorador e escolher **"Run Java"**, ou usar as configurações de arranque no painel Run and Debug (certifique-se de que o seu ficheiro `.env` está configurado com as credenciais Azure primeiro).

**Usando Maven:** Alternativamente, pode executar a partir da linha de comandos com os exemplos abaixo.

### Operações de ficheiros (Stdio)

Isto demonstra ferramentas baseadas em subprocessos locais.

**✅ Sem pré-requisitos** - o servidor MCP é arrancado automaticamente.

**Usando os scripts de arranque (Recomendado):**

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz:

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

**Usando VS Code:** Clique com o botão direito em `StdioTransportDemo.java` e escolha **"Run Java"** (certifique-se que o seu ficheiro `.env` está configurado).

A aplicação arranca automaticamente um servidor MCP de sistema de ficheiros e lê um ficheiro local. Repare como a gestão do subprocesso é tratada por si.

**Saída esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

O padrão **Agente Supervisor** é uma forma **flexível** de IA agentic. Um Supervisor usa um LLM para decidir autonomamente quais agentes invocar com base no pedido do utilizador. No próximo exemplo, combinamos o acesso a ficheiros potenciados pelo MCP com um agente LLM para criar um fluxo supervisionado de leitura de ficheiro → relatório.

Na demonstração, o `FileAgent` lê um ficheiro usando as ferramentas do sistema de ficheiros MCP, e o `ReportAgent` gera um relatório estruturado com um resumo executivo (1 frase), 3 pontos-chave e recomendações. O Supervisor orquestra este fluxo automaticamente:

<img src="../../../translated_images/pt-PT/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*O Supervisor usa o seu LLM para decidir quais agentes invocar e em que ordem — não é necessário código fixo para a roteação.*

Aqui está o fluxo concreto para o nosso pipeline de ficheiro a relatório:

<img src="../../../translated_images/pt-PT/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*O FileAgent lê o ficheiro através das ferramentas MCP, depois o ReportAgent transforma o conteúdo bruto num relatório estruturado.*

Cada agente armazena a sua saída no **Escopo Agente** (memória partilhada), permitindo que agentes seguintes acedam a resultados anteriores. Isto demonstra como ferramentas MCP se integram perfeitamente em fluxos agentic — o Supervisor não precisa de saber *como* os ficheiros são lidos, apenas que o `FileAgent` pode fazê-lo.

#### Executar a demonstração

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz:

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

**Usando VS Code:** Clique com o botão direito em `SupervisorAgentDemo.java` e escolha **"Run Java"** (certifique-se que o seu ficheiro `.env` está configurado).

#### Como funciona o Supervisor

Antes de construir agentes, precisa de ligar o transporte MCP a um cliente e envolvê-lo como um `ToolProvider`. É assim que as ferramentas do servidor MCP ficam disponíveis para os seus agentes:

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

Agora pode injetar `mcpToolProvider` em qualquer agente que precise das ferramentas MCP:

```java
// Passo 1: FileAgent lê ficheiros usando as ferramentas MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Possui ferramentas MCP para operações de ficheiros
        .build();

// Passo 2: ReportAgent gera relatórios estruturados
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// O Supervisor coordena o fluxo de trabalho ficheiro → relatório
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Devolve o relatório final
        .build();

// O Supervisor decide quais os agentes a invocar com base no pedido
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Estratégias de resposta

Quando configura um `SupervisorAgent`, especifica como deve formular a resposta final ao utilizador depois dos sub-agentes concluírem as suas tarefas. O diagrama abaixo mostra as três estratégias disponíveis — LAST devolve diretamente a saída do último agente, SUMMARY sintetiza todas as saídas através de um LLM, e SCORED escolhe a que tiver melhor pontuação contra o pedido original:

<img src="../../../translated_images/pt-PT/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Três estratégias para como o Supervisor formula a sua resposta final — escolha conforme pretenda a saída do último agente, um resumo sintetizado ou a melhor opção pontuada.*

As estratégias disponíveis são:

| Estratégia | Descrição |
|------------|-----------|
| **LAST**   | O supervisor devolve a saída do último sub-agente ou ferramenta chamada. Isto é útil quando o agente final no fluxo é especificamente desenhado para produzir a resposta completa e final (ex: um "Agente de Resumo" numa pipeline de investigação). |
| **SUMMARY**| O supervisor usa o seu próprio Modelo de Linguagem (LLM) interno para sintetizar um resumo de toda a interação e de todas as saídas dos sub-agentes, depois devolve esse resumo como resposta final. Isto fornece uma resposta agregada e limpa ao utilizador. |
| **SCORED** | O sistema usa um LLM interno para pontuar tanto a resposta LAST como o SUMMARY da interação contra o pedido original do utilizador, devolvendo a saída com melhor pontuação. |
Veja [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para a implementação completa.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Abra [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e pergunte:
> - "Como é que o Supervisor decide quais agentes invocar?"
> - "Qual é a diferença entre os padrões de workflow Supervisor e Sequencial?"
> - "Como posso personalizar o comportamento de planeamento do Supervisor?"

#### Compreender a Saída

Quando executar a demonstração, verá um percurso estruturado de como o Supervisor orquestra múltiplos agentes. Eis o que significa cada secção:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**O cabeçalho** introduz o conceito de workflow: uma pipeline focada desde a leitura de ficheiros até à geração de relatórios.

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

**Diagrama do Workflow** mostra o fluxo de dados entre agentes. Cada agente tem um papel específico:
- **FileAgent** lê ficheiros usando ferramentas MCP e armazena o conteúdo bruto em `fileContent`
- **ReportAgent** consome esse conteúdo e produz um relatório estruturado em `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Pedido do Utilizador** mostra a tarefa. O Supervisor analisa isto e decide invocar FileAgent → ReportAgent.

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

**Orquestração do Supervisor** mostra o fluxo de 2 passos em ação:
1. **FileAgent** lê o ficheiro via MCP e armazena o conteúdo
2. **ReportAgent** recebe o conteúdo e gera um relatório estruturado

O Supervisor tomou estas decisões **autonomamente** com base no pedido do utilizador.

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

#### Explicação das Funcionalidades do Módulo Agentic

O exemplo demonstra várias funcionalidades avançadas do módulo agentic. Vamos analisar mais de perto o Agentic Scope e os Agent Listeners.

**Agentic Scope** mostra a memória partilhada onde os agentes armazenaram os seus resultados usando `@Agent(outputKey="...")`. Isto permite:
- Agentes posteriores acederem às saídas dos agentes anteriores
- O Supervisor sintetizar uma resposta final
- Você inspecionar o que cada agente produziu

O diagrama abaixo mostra como o Agentic Scope funciona como memória partilhada no workflow de ficheiro para relatório — o FileAgent escreve a sua saída sob a chave `fileContent`, o ReportAgent lê isso e escreve a sua própria saída sob `report`:

<img src="../../../translated_images/pt-PT/agentic-scope.95ef488b6c1d02ef.webp" alt="Memória Partilhada Agentic Scope" width="800"/>

*Agentic Scope atua como memória partilhada — FileAgent escreve `fileContent`, ReportAgent lê e escreve `report`, e o seu código lê o resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dados de ficheiro bruto do FileAgent
String report = scope.readState("report");            // Relatório estruturado do ReportAgent
```

**Agent Listeners** permitem monitorizar e depurar a execução dos agentes. A saída passo a passo que vê na demonstração vem de um AgentListener que se liga a cada invocação de agente:
- **beforeAgentInvocation** - Chamado quando o Supervisor seleciona um agente, permitindo ver qual agente foi escolhido e porquê
- **afterAgentInvocation** - Chamado quando um agente termina, mostrando o seu resultado
- **inheritedBySubagents** - Quando verdadeiro, o listener monitoriza todos os agentes na hierarquia

O diagrama seguinte mostra todo o ciclo de vida do Agent Listener, incluindo como o `onError` lida com falhas durante a execução do agente:

<img src="../../../translated_images/pt-PT/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo de Vida dos Agent Listeners" width="800"/>

*Agent Listeners ligam-se ao ciclo de vida da execução — monitorizam quando os agentes começam, terminam ou encontram erros.*

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

Para além do padrão Supervisor, o módulo `langchain4j-agentic` fornece vários padrões poderosos de workflows. O diagrama abaixo mostra os cinco principais — desde pipelines simples sequenciais até workflows de aprovação com intervenção humana:

<img src="../../../translated_images/pt-PT/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Padrões de Workflow de Agentes" width="800"/>

*Cinco padrões de workflow para orquestrar agentes — desde pipelines sequenciais simples até workflows de aprovação com intervenção humana.*

| Padrão | Descrição | Caso de Uso |
|---------|-------------|----------|
| **Sequencial** | Executar agentes por ordem, a saída flui para o seguinte | Pipelines: pesquisa → análise → relatório |
| **Paralelo** | Executar agentes simultaneamente | Tarefas independentes: meteorologia + notícias + ações |
| **Loop** | Iterar até a condição ser satisfeita | Avaliação de qualidade: refinar até pontuação ≥ 0.8 |
| **Condicional** | Direcionar com base em condições | Classificar → direcionar ao agente especialista |
| **Intervenção Humana** | Adicionar pontos de controlo humanos | Workflows de aprovação, revisão de conteúdo |

## Conceitos Chave

Agora que explorou o MCP e o módulo agentic em ação, vamos resumir quando usar cada abordagem.

Uma das maiores vantagens do MCP é o seu ecossistema em crescimento. O diagrama abaixo mostra como um protocolo universal único liga a sua aplicação de IA a uma grande variedade de servidores MCP — desde acesso a sistemas de ficheiros e bases de dados até GitHub, email, web scraping, e mais:

<img src="../../../translated_images/pt-PT/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecossistema MCP" width="800"/>

*MCP cria um ecossistema de protocolo universal — qualquer servidor compatível com MCP funciona com qualquer cliente compatível com MCP, permitindo a partilha de ferramentas entre aplicações.*

**MCP** é ideal quando quer tirar partido de ecossistemas de ferramentas existentes, construir ferramentas que várias aplicações possam partilhar, integrar serviços terceiros com protocolos padrão, ou trocar implementações de ferramentas sem alterar código.

**O Módulo Agentic** funciona melhor quando quer definições declarativas de agentes com anotações `@Agent`, necessita de orquestração de workflows (sequencial, loop, paralelo), prefere design de agentes baseado em interfaces em vez de código imperativo, ou está a combinar múltiplos agentes que partilham saídas via `outputKey`.

**O padrão Supervisor Agent** destaca-se quando o workflow não é previsível antecipadamente e quer que o LLM decida, quando tem múltiplos agentes especializados que necessitam de orquestração dinâmica, quando constrói sistemas conversacionais que direcionam para diferentes capacidades, ou quando deseja o comportamento de agente mais flexível e adaptativo.

Para ajudar a decidir entre os métodos customizados `@Tool` do Módulo 04 e as ferramentas MCP deste módulo, a comparação seguinte destaca as principais compensações — ferramentas customizadas oferecem forte acoplamento e total segurança de tipos para lógica específica da aplicação, enquanto as ferramentas MCP oferecem integrações padronizadas e reutilizáveis:

<img src="../../../translated_images/pt-PT/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Ferramentas Customizadas vs Ferramentas MCP" width="800"/>

*Quando usar métodos customizados @Tool vs ferramentas MCP — ferramentas customizadas para lógica específica da aplicação com segurança total de tipos, ferramentas MCP para integrações padronizadas que funcionam entre aplicações.*

## Parabéns!

Completou os cinco módulos do curso LangChain4j para Iniciantes! Aqui está uma vista do percurso completo de aprendizagem que realizou — desde chat básico até sistemas agentic alimentados por MCP:

<img src="../../../translated_images/pt-PT/course-completion.48cd201f60ac7570.webp" alt="Conclusão do Curso" width="800"/>

*O seu percurso de aprendizagem através dos cinco módulos — desde chat básico até sistemas agentic alimentados por MCP.*

Concluiu o curso LangChain4j para Iniciantes. Aprendeu:

- Como construir IA conversacional com memória (Módulo 01)
- Padrões de engenharia de prompts para diferentes tarefas (Módulo 02)
- Fundamentos de respostas apoiadas nos seus documentos com RAG (Módulo 03)
- Criar agentes de IA básicos (assistentes) com ferramentas customizadas (Módulo 04)
- Integrar ferramentas padronizadas com os módulos LangChain4j MCP e Agentic (Módulo 05)

### O que vem a seguir?

Após completar os módulos, explore o [Guia de Testes](../docs/TESTING.md) para ver conceitos de testes LangChain4j em ação.

**Recursos Oficiais:**
- [Documentação LangChain4j](https://docs.langchain4j.dev/) - Guias abrangentes e referência de API
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Código-fonte e exemplos
- [Tutoriais LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriais passo a passo para vários casos de uso

Obrigado por completar este curso!

---

**Navegação:** [← Anterior: Módulo 04 - Ferramentas](../04-tools/README.md) | [Voltar ao Início](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional por um ser humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->