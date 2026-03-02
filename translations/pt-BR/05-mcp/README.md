# Módulo 05: Protocolo de Contexto de Modelo (MCP)

## Índice

- [O que você vai aprender](../../../05-mcp)
- [O que é MCP?](../../../05-mcp)
- [Como o MCP funciona](../../../05-mcp)
- [O Módulo Agêncico](../../../05-mcp)
- [Executando os Exemplos](../../../05-mcp)
  - [Pré-requisitos](../../../05-mcp)
- [Início Rápido](../../../05-mcp)
  - [Operações de Arquivo (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Executando a Demonstração](../../../05-mcp)
    - [Como o Supervisor Funciona](../../../05-mcp)
    - [Estratégias de Resposta](../../../05-mcp)
    - [Entendendo a Saída](../../../05-mcp)
    - [Explicação das Funcionalidades do Módulo Agêncico](../../../05-mcp)
- [Conceitos Chave](../../../05-mcp)
- [Parabéns!](../../../05-mcp)
  - [O que vem a seguir?](../../../05-mcp)

## O que você vai aprender

Você já construiu IA conversacional, dominou prompts, fundamentou respostas em documentos e criou agentes com ferramentas. Mas todas essas ferramentas foram criadas sob medida para sua aplicação específica. E se você pudesse dar à sua IA acesso a um ecossistema padronizado de ferramentas que qualquer pessoa pode criar e compartilhar? Neste módulo, você vai aprender exatamente isso com o Protocolo de Contexto de Modelo (MCP) e o módulo agêncico do LangChain4j. Primeiro mostramos um leitor de arquivos MCP simples e depois demonstramos como ele é facilmente integrado em fluxos de trabalho agêncicos avançados usando o padrão Agente Supervisor.

## O que é MCP?

O Protocolo de Contexto de Modelo (MCP) oferece exatamente isso — uma forma padrão para aplicações de IA descobrirem e usarem ferramentas externas. Em vez de escrever integrações personalizadas para cada fonte de dados ou serviço, você se conecta a servidores MCP que expõem suas capacidades em um formato consistente. Seu agente de IA pode então descobrir e usar essas ferramentas automaticamente.

O diagrama abaixo mostra a diferença — sem o MCP, toda integração exige conexões ponto a ponto personalizadas; com o MCP, um único protocolo conecta seu app a qualquer ferramenta:

<img src="../../../translated_images/pt-BR/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Antes do MCP: Integrações complexas ponto a ponto. Depois do MCP: Um protocolo, possibilidades infinitas.*

O MCP resolve um problema fundamental no desenvolvimento de IA: toda integração é personalizada. Quer acessar o GitHub? Código customizado. Quer ler arquivos? Código customizado. Quer consultar um banco de dados? Código customizado. E nenhuma dessas integrações funciona com outras aplicações de IA.

O MCP padroniza isso. Um servidor MCP expõe ferramentas com descrições claras e esquemas. Qualquer cliente MCP pode conectar, descobrir as ferramentas disponíveis e usá-las. Construído uma vez, usado em toda parte.

O diagrama abaixo ilustra essa arquitetura — um único cliente MCP (sua aplicação de IA) conecta-se a múltiplos servidores MCP, cada um expondo seu conjunto próprio de ferramentas através do protocolo padrão:

<img src="../../../translated_images/pt-BR/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Arquitetura do Protocolo de Contexto de Modelo - descoberta e execução de ferramentas padronizadas*

## Como o MCP funciona

Nos bastidores, o MCP usa uma arquitetura em camadas. Sua aplicação Java (o cliente MCP) descobre ferramentas disponíveis, envia requisições JSON-RPC por meio de uma camada de transporte (Stdio ou HTTP), e o servidor MCP executa operações e retorna resultados. O diagrama a seguir detalha cada camada desse protocolo:

<img src="../../../translated_images/pt-BR/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*Como o MCP funciona internamente — clientes descobrem ferramentas, trocam mensagens JSON-RPC e executam operações via uma camada de transporte.*

**Arquitetura Cliente-Servidor**

O MCP usa um modelo cliente-servidor. Servidores fornecem ferramentas — ler arquivos, consultar bancos de dados, chamar APIs. Clientes (sua aplicação de IA) conectam-se aos servidores e usam suas ferramentas.

Para usar MCP com LangChain4j, adicione esta dependência no Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoberta de Ferramentas**

Quando seu cliente conecta a um servidor MCP, ele pergunta "Quais ferramentas você tem?" O servidor responde com uma lista de ferramentas disponíveis, cada uma com descrições e esquemas de parâmetros. Seu agente de IA pode então decidir quais ferramentas usar com base nas solicitações do usuário. O diagrama abaixo mostra esse "aperto de mãos" — o cliente envia uma requisição `tools/list` e o servidor retorna suas ferramentas disponíveis com descrições e esquemas de parâmetros:

<img src="../../../translated_images/pt-BR/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*A IA descobre as ferramentas disponíveis ao iniciar — agora sabe quais capacidades existem e pode decidir quais usar.*

**Mecanismos de Transporte**

O MCP suporta diferentes mecanismos de transporte. As duas opções são Stdio (para comunicação com subprocessos locais) e HTTP Streamable (para servidores remotos). Este módulo demonstra o transporte Stdio:

<img src="../../../translated_images/pt-BR/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para processos locais*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para processos locais. Sua aplicação inicia um servidor como subprocesso e comunica via entrada/saída padrão. Útil para acesso a sistema de arquivos ou ferramentas de linha de comando.

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

O servidor `@modelcontextprotocol/server-filesystem` expõe as seguintes ferramentas, todas isoladas nos diretórios que você especificar:

| Ferramenta | Descrição |
|------------|------------|
| `read_file` | Ler o conteúdo de um único arquivo |
| `read_multiple_files` | Ler múltiplos arquivos em uma chamada |
| `write_file` | Criar ou sobrescrever um arquivo |
| `edit_file` | Fazer substituições direcionadas (find-and-replace) |
| `list_directory` | Listar arquivos e diretórios em um caminho |
| `search_files` | Buscar recursivamente por arquivos que correspondam a um padrão |
| `get_file_info` | Obter metadados do arquivo (tamanho, timestamps, permissões) |
| `create_directory` | Criar um diretório (incluindo os diretórios pais) |
| `move_file` | Mover ou renomear um arquivo ou diretório |

O diagrama abaixo mostra como o transporte Stdio funciona em tempo de execução — sua aplicação Java gera o servidor MCP como processo filho e eles comunicam via pipes stdin/stdout, sem rede ou HTTP envolvidos:

<img src="../../../translated_images/pt-BR/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Transporte Stdio em ação — sua aplicação gera o servidor MCP como processo filho e comunica via pipes stdin/stdout.*

> **🤖 Tente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e pergunte:
> - "Como funciona o transporte Stdio e quando devo usá-lo em vez do HTTP?"
> - "Como o LangChain4j gerencia o ciclo de vida dos processos de servidor MCP iniciados?"
> - "Quais são as implicações de segurança de dar acesso do AI ao sistema de arquivos?"

## O Módulo Agêncico

Enquanto o MCP fornece ferramentas padronizadas, o módulo **agêncico** do LangChain4j oferece uma forma declarativa de construir agentes que orquestram essas ferramentas. A anotação `@Agent` e o `AgenticServices` permitem definir o comportamento do agente via interfaces em vez de código imperativo.

Neste módulo, você vai explorar o padrão **Agente Supervisor** — uma abordagem avançada de IA agêncica em que um agente "supervisor" decide dinamicamente quais subagentes invocar com base nas requisições do usuário. Vamos combinar ambos os conceitos dando a um dos nossos subagentes capacidades de acesso a arquivos via MCP.

Para usar o módulo agêncico, adicione esta dependência no Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **Nota:** O módulo `langchain4j-agentic` usa uma propriedade de versão separada (`langchain4j.mcp.version`) porque é lançado em cronograma diferente do núcleo do LangChain4j.

> **⚠️ Experimental:** O módulo `langchain4j-agentic` é **experimental** e sujeito a mudanças. A forma estável de construir assistentes de IA continua sendo com `langchain4j-core` usando ferramentas customizadas (Módulo 04).

## Executando os Exemplos

### Pré-requisitos

- Ter completado o [Módulo 04 - Ferramentas](../04-tools/README.md) (este módulo baseia-se nos conceitos de ferramentas customizadas e os compara com ferramentas MCP)
- Arquivo `.env` na raiz com credenciais Azure (criado pelo comando `azd up` no Módulo 01)
- Java 21+, Maven 3.9+
- Node.js 16+ e npm (para servidores MCP)

> **Nota:** Se ainda não configurou suas variáveis de ambiente, veja o [Módulo 01 - Introdução](../01-introduction/README.md) para as instruções de implantação (`azd up` cria o arquivo `.env` automaticamente), ou copie `.env.example` para `.env` na raiz e preencha seus valores.

## Início Rápido

**Usando VS Code:** Basta clicar com o botão direito em qualquer arquivo de demonstração no Explorer e selecionar **"Run Java"**, ou usar as configurações de execução no painel Rodar e Depurar (certifique-se de que o arquivo `.env` esteja configurado com credenciais Azure primeiro).

**Usando Maven:** Alternativamente, você pode executar pela linha de comando com os exemplos abaixo.

### Operações de Arquivo (Stdio)

Demonstra ferramentas baseadas em subprocessos locais.

**✅ Nenhum pré-requisito necessário** — o servidor MCP é iniciado automaticamente.

**Usando os Scripts de Inicialização (Recomendado):**

Os scripts de inicialização carregam automaticamente as variáveis de ambiente do arquivo `.env` da raiz:

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

**Usando VS Code:** Clique com o botão direito em `StdioTransportDemo.java` e selecione **"Run Java"** (certifique-se de que o arquivo `.env` esteja configurado).

A aplicação inicia automaticamente um servidor MCP de sistema de arquivos e lê um arquivo local. Repare como o gerenciamento do subprocesso é feito para você.

**Saída esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

O padrão **Agente Supervisor** é uma forma **flexível** de IA agêncica. Um Supervisor usa um LLM para decidir autonomamente quais agentes invocar com base no pedido do usuário. No próximo exemplo, combinamos acesso a arquivos via MCP com um agente LLM para criar um fluxo supervised de leitura de arquivo → relatório.

Na demonstração, o `FileAgent` lê um arquivo usando ferramentas MCP de sistema de arquivos, e o `ReportAgent` gera um relatório estruturado com um resumo executivo (1 frase), 3 pontos-chave e recomendações. O Supervisor orquestra esse fluxo automaticamente:

<img src="../../../translated_images/pt-BR/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*O Supervisor usa seu LLM para decidir quais agentes invocar e em qual ordem — sem roteamento codificado.*

Veja como é o fluxo concreto para nosso pipeline de arquivo para relatório:

<img src="../../../translated_images/pt-BR/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*O FileAgent lê o arquivo via ferramentas MCP, então o ReportAgent transforma o conteúdo bruto em um relatório estruturado.*

Cada agente armazena sua saída no **Escopo Agêncico** (memória compartilhada), permitindo que agentes posteriores acessem resultados anteriores. Isso demonstra como ferramentas MCP se integram perfeitamente em fluxos agêncicos — o Supervisor não precisa saber *como* os arquivos são lidos, apenas que o `FileAgent` pode fazê-lo.

#### Executando a Demonstração

Os scripts de inicialização carregam automaticamente as variáveis de ambiente do arquivo `.env` da raiz:

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

**Usando VS Code:** Clique com o botão direito em `SupervisorAgentDemo.java` e selecione **"Run Java"** (certifique-se de que o arquivo `.env` esteja configurado).

#### Como o Supervisor Funciona

Antes de construir agentes, você precisa conectar o transporte MCP a um cliente e encapsulá-lo como um `ToolProvider`. Assim as ferramentas do servidor MCP ficam disponíveis para seus agentes:

```java
// Crie um cliente MCP a partir do transporte
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// Envolva o cliente como um ToolProvider — isso integra as ferramentas MCP no LangChain4j
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

Agora você pode injetar `mcpToolProvider` em qualquer agente que precise das ferramentas MCP:

```java
// Etapa 1: FileAgent lê arquivos usando ferramentas MCP
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // Possui ferramentas MCP para operações de arquivo
        .build();

// Etapa 2: ReportAgent gera relatórios estruturados
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor orquestra o fluxo de trabalho arquivo → relatório
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Retorna o relatório final
        .build();

// O Supervisor decide quais agentes invocar com base na solicitação
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Estratégias de Resposta

Quando você configura um `SupervisorAgent`, especifica como ele deve formular sua resposta final ao usuário após os sub-agentes completarem suas tarefas. O diagrama abaixo mostra as três estratégias disponíveis — LAST retorna diretamente a saída do último agente, SUMMARY sintetiza todas as saídas com um LLM, e SCORED escolhe a que obtiver melhor pontuação em relação à solicitação original:

<img src="../../../translated_images/pt-BR/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Três estratégias para como o Supervisor formula sua resposta final — escolha com base em se quer o resultado do último agente, um resumo sintetizado ou a melhor opção pontuada.*

As estratégias disponíveis são:

| Estratégia | Descrição |
|------------|------------|
| **LAST** | O supervisor retorna a saída do último sub-agente ou ferramenta chamada. Isso é útil quando o agente final do fluxo de trabalho é especificamente designado para produzir a resposta completa e final (exemplo: um "Agente Resumo" em um pipeline de pesquisa). |
| **SUMMARY** | O supervisor usa seu próprio Modelo de Linguagem Interno (LLM) para sintetizar um resumo de toda a interação e de todas as saídas dos sub-agentes, retornando esse resumo como resposta final. Isso fornece uma resposta limpa e agregada ao usuário. |
| **SCORED** | O sistema usa um LLM interno para pontuar tanto a resposta LAST quanto o SUMMARY da interação em relação ao pedido original do usuário, retornando a saída que obtiver a pontuação mais alta. |
Veja [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para a implementação completa.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Abra [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e pergunte:
> - "Como o Supervisor decide quais agentes invocar?"
> - "Qual a diferença entre os padrões de fluxo de trabalho Supervisor e Sequencial?"
> - "Como posso personalizar o comportamento de planejamento do Supervisor?"

#### Entendendo a Saída

Quando você executar a demonstração, verá uma explicação estruturada de como o Supervisor orquestra múltiplos agentes. Aqui está o que cada seção significa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**O cabeçalho** introduz o conceito do fluxo de trabalho: um pipeline focado da leitura de arquivo até a geração do relatório.

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
- **FileAgent** lê arquivos usando as ferramentas MCP e armazena o conteúdo bruto em `fileContent`
- **ReportAgent** consome esse conteúdo e produz um relatório estruturado em `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**Solicitação do Usuário** mostra a tarefa. O Supervisor analisa isso e decide invocar FileAgent → ReportAgent.

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

**Orquestração do Supervisor** mostra o fluxo em 2 etapas em ação:
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

O exemplo demonstra vários recursos avançados do módulo agentic. Vamos olhar mais de perto o Agentic Scope e os Agent Listeners.

**Agentic Scope** mostra a memória compartilhada onde os agentes armazenaram seus resultados usando `@Agent(outputKey="...")`. Isso permite:
- Que agentes posteriores acessem as saídas dos agentes anteriores
- Que o Supervisor sintetize uma resposta final
- Que você inspecione o que cada agente produziu

O diagrama abaixo mostra como o Agentic Scope funciona como memória compartilhada no fluxo de trabalho de arquivo para relatório — FileAgent grava sua saída sob a chave `fileContent`, ReportAgent lê isso e grava sua própria saída sob `report`:

<img src="../../../translated_images/pt-BR/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope atua como memória compartilhada — FileAgent escreve `fileContent`, ReportAgent o lê e escreve `report`, e seu código lê o resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dados brutos do arquivo do FileAgent
String report = scope.readState("report");            // Relatório estruturado do ReportAgent
```

**Agent Listeners** permitem monitorar e depurar a execução dos agentes. A saída passo a passo que você vê na demonstração vem de um AgentListener que se conecta a cada invocação de agente:
- **beforeAgentInvocation** - Chamado quando o Supervisor seleciona um agente, permitindo ver qual agente foi escolhido e por quê
- **afterAgentInvocation** - Chamado quando um agente termina, mostrando seu resultado
- **inheritedBySubagents** - Quando verdadeiro, o listener monitora todos os agentes na hierarquia

O diagrama a seguir mostra o ciclo de vida completo do Agent Listener, incluindo como o `onError` lida com falhas durante a execução do agente:

<img src="../../../translated_images/pt-BR/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners se conectam ao ciclo de vida da execução — monitore quando agentes começam, terminam ou encontram erros.*

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

Além do padrão Supervisor, o módulo `langchain4j-agentic` fornece vários poderosos padrões de fluxo de trabalho. O diagrama abaixo mostra todos os cinco — desde pipelines sequenciais simples até fluxos de aprovação com intervenção humana:

<img src="../../../translated_images/pt-BR/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Cinco padrões de fluxo de trabalho para orquestrar agentes — de pipelines sequenciais simples a fluxos de aprovação com intervenção humana.*

| Padrão | Descrição | Caso de Uso |
|---------|-------------|----------|
| **Sequencial** | Executa agentes em ordem, saída flui para o próximo | Pipelines: pesquisa → análise → relatório |
| **Paralelo** | Executa agentes simultaneamente | Tarefas independentes: clima + notícias + ações |
| **Loop** | Itera até a condição ser satisfeita | Avaliação de qualidade: refinar até pontuação ≥ 0.8 |
| **Condicional** | Roteia com base em condições | Classificação → encaminhar para agente especialista |
| **Intervenção Humana** | Adiciona pontos de verificação humanos | Fluxos de aprovação, revisão de conteúdo |

## Conceitos-Chave

Agora que você explorou o MCP e o módulo agentic em ação, vamos resumir quando usar cada abordagem.

Uma das maiores vantagens do MCP é seu ecossistema em crescimento. O diagrama abaixo mostra como um único protocolo universal conecta sua aplicação de IA a uma ampla variedade de servidores MCP — desde acesso a sistema de arquivos e banco de dados até GitHub, email, web scraping e mais:

<img src="../../../translated_images/pt-BR/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP cria um ecossistema de protocolo universal — qualquer servidor compatível com MCP funciona com qualquer cliente compatível, permitindo o compartilhamento de ferramentas entre aplicações.*

**O MCP** é ideal quando você quer aproveitar ecossistemas de ferramentas existentes, construir ferramentas que múltiplas aplicações possam compartilhar, integrar serviços terceirizados com protocolos padrões, ou trocar implementações de ferramentas sem alterar o código.

**O Módulo Agentic** funciona melhor quando você quer definições declarativas de agentes com anotações `@Agent`, precisa de orquestração de fluxo de trabalho (sequencial, loop, paralelo), prefere design de agentes baseado em interfaces ao invés de código imperativo, ou está combinando múltiplos agentes que compartilham saídas via `outputKey`.

**O padrão Supervisor Agent** brilha quando o fluxo de trabalho não é previsível antecipadamente e você quer que o LLM decida, quando tem múltiplos agentes especializados que precisam de orquestração dinâmica, ao construir sistemas conversacionais que direcionam para diferentes capacidades, ou quando você quer o comportamento de agente mais flexível e adaptativo.

Para ajudar a decidir entre os métodos customizados `@Tool` do Módulo 04 e as ferramentas MCP deste módulo, a comparação a seguir destaca as principais trocas — ferramentas customizadas dão acoplamento forte e total segurança de tipos para lógica específica da aplicação, enquanto as ferramentas MCP oferecem integrações padronizadas e reutilizáveis:

<img src="../../../translated_images/pt-BR/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*Quando usar métodos @Tool customizados vs ferramentas MCP — ferramentas customizadas para lógica específica da aplicação com segurança total de tipos, ferramentas MCP para integrações padronizadas que funcionam entre aplicações.*

## Parabéns!

Você completou todos os cinco módulos do curso LangChain4j para Iniciantes! Aqui está uma visão da jornada completa de aprendizado que você concluiu — do chat básico até sistemas agentic com MCP:

<img src="../../../translated_images/pt-BR/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Sua jornada de aprendizado por todos os cinco módulos — do chat básico até sistemas agentic com MCP.*

Você completou o curso LangChain4j para Iniciantes. Você aprendeu:

- Como construir IA conversacional com memória (Módulo 01)
- Padrões de engenharia de prompt para diferentes tarefas (Módulo 02)
- Fundamentos para respostas baseadas em seus documentos com RAG (Módulo 03)
- Criar agentes básicos de IA (assistentes) com ferramentas customizadas (Módulo 04)
- Integrar ferramentas padronizadas com os módulos LangChain4j MCP e Agentic (Módulo 05)

### E agora?

Depois de completar os módulos, explore o [Guia de Testes](../docs/TESTING.md) para ver conceitos de testes LangChain4j em ação.

**Recursos Oficiais:**
- [Documentação LangChain4j](https://docs.langchain4j.dev/) - Guias completos e referência API
- [LangChain4j no GitHub](https://github.com/langchain4j/langchain4j) - Código fonte e exemplos
- [Tutoriais LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriais passo a passo para vários casos de uso

Obrigado por completar este curso!

---

**Navegação:** [← Anterior: Módulo 04 - Ferramentas](../04-tools/README.md) | [Voltar ao Início](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->