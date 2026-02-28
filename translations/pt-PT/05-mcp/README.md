# Módulo 05: Protocolo de Contexto do Modelo (MCP)

## Índice

- [O que vais aprender](../../../05-mcp)
- [O que é o MCP?](../../../05-mcp)
- [Como funciona o MCP](../../../05-mcp)
- [O Módulo Agentic](../../../05-mcp)
- [Executar os Exemplos](../../../05-mcp)
  - [Pré-requisitos](../../../05-mcp)
- [Início Rápido](../../../05-mcp)
  - [Operações com Ficheiros (Stdio)](../../../05-mcp)
  - [Agente Supervisor](../../../05-mcp)
    - [Executar a Demonstração](../../../05-mcp)
    - [Como funciona o Supervisor](../../../05-mcp)
    - [Estratégias de Resposta](../../../05-mcp)
    - [Compreender a Saída](../../../05-mcp)
    - [Explicação das Funcionalidades do Módulo Agentic](../../../05-mcp)
- [Conceitos-Chave](../../../05-mcp)
- [Parabéns!](../../../05-mcp)
  - [E agora?](../../../05-mcp)

## O que vais aprender

Já construíste IA conversacional, dominaste os prompts, fundamentaste respostas em documentos e criaste agentes com ferramentas. Mas todas essas ferramentas foram criadas à medida para a tua aplicação específica. E se pudesses dar à tua IA acesso a um ecossistema padronizado de ferramentas que qualquer pessoa pode criar e partilhar? Neste módulo, vais aprender exatamente isso com o Protocolo de Contexto do Modelo (MCP) e o módulo agentic do LangChain4j. Apresentamos primeiro um leitor de ficheiros MCP simples e depois mostramos como se integra facilmente em fluxos de trabalho agentic avançados usando o padrão Agente Supervisor. 

## O que é o MCP?

O Protocolo de Contexto do Modelo (MCP) oferece exatamente isso - uma forma padrão para aplicações de IA descobrirem e usarem ferramentas externas. Em vez de escrever integrações personalizadas para cada fonte de dados ou serviço, tu ligas-te a servidores MCP que expõem as suas capacidades num formato consistente. O teu agente de IA pode então descobrir e usar essas ferramentas automaticamente.

<img src="../../../translated_images/pt-PT/mcp-comparison.9129a881ecf10ff5.webp" alt="Comparação MCP" width="800"/>

*Antes do MCP: integrações complexas ponto a ponto. Após o MCP: Um protocolo, possibilidades infinitas.*

O MCP resolve um problema fundamental no desenvolvimento de IA: cada integração é personalizada. Queres aceder ao GitHub? Código personalizado. Queres ler ficheiros? Código personalizado. Queres consultar uma base de dados? Código personalizado. E nenhuma dessas integrações funciona com outras aplicações de IA.

O MCP padroniza isto. Um servidor MCP expõe ferramentas com descrições claras e esquemas. Qualquer cliente MCP pode ligar, descobrir as ferramentas disponíveis e usá-las. Constrói uma vez, usa em todo o lado.

<img src="../../../translated_images/pt-PT/mcp-architecture.b3156d787a4ceac9.webp" alt="Arquitetura MCP" width="800"/>

*Arquitetura do Protocolo de Contexto do Modelo - descoberta e execução padronizadas de ferramentas*

## Como funciona o MCP

<img src="../../../translated_images/pt-PT/mcp-protocol-detail.01204e056f45308b.webp" alt="Detalhe do Protocolo MCP" width="800"/>

*Como o MCP funciona por trás das cenas — os clientes descobrem ferramentas, trocam mensagens JSON-RPC e executam operações através de uma camada de transporte.*

**Arquitetura Servidor-Cliente**

O MCP usa um modelo cliente-servidor. Os servidores fornecem ferramentas - ler ficheiros, consultar base de dados, chamar APIs. Os clientes (a tua aplicação de IA) ligam-se aos servidores e usam as suas ferramentas.

Para usar MCP com LangChain4j, adiciona esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Descoberta de Ferramentas**

Quando o teu cliente se liga a um servidor MCP, pergunta "Que ferramentas tens?" O servidor responde com uma lista das ferramentas disponíveis, cada uma com descrições e esquemas de parâmetros. O teu agente de IA pode então decidir que ferramentas usar com base nos pedidos do utilizador.

<img src="../../../translated_images/pt-PT/tool-discovery.07760a8a301a7832.webp" alt="Descoberta de Ferramentas MCP" width="800"/>

*A IA descobre ferramentas disponíveis ao início — agora sabe que capacidades estão disponíveis e pode decidir quais usar.*

**Mecanismos de Transporte**

O MCP suporta diferentes mecanismos de transporte. Este módulo demonstra o transporte Stdio para processos locais:

<img src="../../../translated_images/pt-PT/transport-mechanisms.2791ba7ee93cf020.webp" alt="Mecanismos de Transporte" width="800"/>

*Mecanismos de transporte MCP: HTTP para servidores remotos, Stdio para processos locais*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

Para processos locais. A tua aplicação cria um servidor como subprocesso e comunica através do input/output padrão. Útil para acesso ao sistema de ficheiros ou ferramentas de linha de comandos.

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

<img src="../../../translated_images/pt-PT/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Fluxo de Transporte Stdio" width="800"/>

*Transporte Stdio em ação — a tua aplicação cria o servidor MCP como processo filho e comunica através de pipes stdin/stdout.*

> **🤖 Experimenta com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) e pergunta:
> - "Como funciona o transporte Stdio e quando devo usá-lo em vez do HTTP?"
> - "Como é que o LangChain4j gere o ciclo de vida dos processos MCP criados?"
> - "Quais são as implicações de segurança de dar à IA acesso ao sistema de ficheiros?"

## O Módulo Agentic

Enquanto o MCP fornece ferramentas padronizadas, o módulo **agentic** do LangChain4j oferece uma forma declarativa de construir agentes que orquestram essas ferramentas. A anotação `@Agent` e o `AgenticServices` permitem definir o comportamento do agente através de interfaces em vez de código imperativo.

Neste módulo, vais explorar o padrão **Agente Supervisor** — uma abordagem agentic avançada na qual um agente "supervisor" decide dinamicamente quais sub-agentes invocar com base nos pedidos do utilizador. Vamos combinar ambos os conceitos dando a um dos nossos sub-agentes capacidades de acesso a ficheiros alimentadas por MCP.

Para usar o módulo agentic, adiciona esta dependência Maven:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ Experimental:** O módulo `langchain4j-agentic` é **experimental** e sujeito a alterações. A forma estável de construir assistentes de IA continua a ser o `langchain4j-core` com ferramentas personalizadas (Módulo 04).

## Executar os Exemplos

### Pré-requisitos

- Java 21+, Maven 3.9+
- Node.js 16+ e npm (para servidores MCP)
- Variáveis de ambiente configuradas no ficheiro `.env` (a partir do diretório raiz):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (igual aos Módulos 01-04)

> **Nota:** Se ainda não configuraste as tuas variáveis de ambiente, vê [Módulo 00 - Início Rápido](../00-quick-start/README.md) para instruções, ou copia `.env.example` para `.env` no diretório raiz e preenche os teus valores.

## Início Rápido

**Usando VS Code:** Basta clicar com o botão direito em qualquer ficheiro demo no Explorador e selecionar **"Run Java"**, ou usar as configurações de lançamento do painel Executar e Depurar (certifica-te de que já adicionaste o teu token ao ficheiro `.env` primeiro).

**Usando Maven:** Alternativamente, podes executar a partir da linha de comandos com os exemplos abaixo.

### Operações com Ficheiros (Stdio)

Este demonstra ferramentas baseadas em subprocessos locais.

**✅ Sem pré-requisitos necessários** - o servidor MCP é iniciado automaticamente.

**Usando os Scripts de Arranque (Recomendado):**

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` à raiz:

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

**Usando VS Code:** Clica com o direito em `StdioTransportDemo.java` e seleciona **"Run Java"** (certifica-te de que o teu ficheiro `.env` está configurado).

A aplicação cria automaticamente um servidor MCP do sistema de ficheiros e lê um ficheiro local. Repara como o gerenciamento do subprocesso é feito por ti.

**Saída esperada:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Agente Supervisor

O **padrão Agente Supervisor** é uma forma **flexível** de AI agentic. Um Supervisor usa um LLM para decidir autonomamente quais agentes invocar com base no pedido do utilizador. No seguinte exemplo, combinamos o acesso a ficheiros alimentado por MCP com um agente LLM para criar um fluxo supervisionado de leitura de ficheiro → relatório.

Na demo, o `FileAgent` lê um ficheiro usando ferramentas MCP do sistema de ficheiros, e o `ReportAgent` gera um relatório estruturado com um resumo executivo (1 frase), 3 pontos-chave e recomendações. O Supervisor orquestra este fluxo automaticamente:

<img src="../../../translated_images/pt-PT/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Padrão Agente Supervisor" width="800"/>

*O Supervisor usa o seu LLM para decidir que agentes invocar e em que ordem — sem necessidade de roteamento fixo.*

Aqui está o fluxo concreto para o nosso pipeline de ficheiro para relatório:

<img src="../../../translated_images/pt-PT/file-report-workflow.649bb7a896800de9.webp" alt="Fluxo Ficheiro para Relatório" width="800"/>

*O FileAgent lê o ficheiro via ferramentas MCP, depois o ReportAgent transforma o conteúdo bruto num relatório estruturado.*

Cada agente guarda a sua saída no **Agentic Scope** (memória partilhada), permitindo que agentes a jusante acedam a resultados anteriores. Isto demonstra como as ferramentas MCP se integram perfeitamente em fluxos agentic — o Supervisor não precisa de saber *como* os ficheiros são lidos, apenas que o `FileAgent` pode fazê-lo.

#### Executar a Demonstração

Os scripts de arranque carregam automaticamente as variáveis de ambiente do ficheiro `.env` à raiz:

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

**Usando VS Code:** Clica com o botão direito em `SupervisorAgentDemo.java` e seleciona **"Run Java"** (certifica-te de que o teu ficheiro `.env` está configurado).

#### Como funciona o Supervisor

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

// Supervisor orquestra o fluxo de trabalho ficheiro → relatório
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // Devolve o relatório final
        .build();

// O Supervisor decide quais agentes invocar com base no pedido
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Estratégias de Resposta

Quando configuras um `SupervisorAgent`, especificas como ele deve formular a sua resposta final ao utilizador depois dos sub-agentes terminarem as suas tarefas.

<img src="../../../translated_images/pt-PT/response-strategies.3d0cea19d096bdf9.webp" alt="Estratégias de Resposta" width="800"/>

*Três estratégias para como o Supervisor formula a sua resposta final — escolhe conforme se quer a saída do último agente, um resumo sintetizado ou a melhor opção pontuada.*

As estratégias disponíveis são:

| Estratégia | Descrição |
|------------|------------|
| **LAST** | O supervisor retorna a saída do último sub-agente ou ferramenta chamado. Isto é útil quando o agente final no fluxo é especificamente desenhado para produzir a resposta completa final (ex: um "Agente de Resumo" num pipeline de investigação). |
| **SUMMARY** | O supervisor usa o seu próprio Modelo de Linguagem (LLM) interno para sintetizar um resumo de toda a interação e todas as saídas dos sub-agentes, depois retorna esse resumo como resposta final. Isto fornece uma resposta limpa e agregada ao utilizador. |
| **SCORED** | O sistema usa um LLM interno para pontuar tanto a resposta LAST como o SUMMARY da interação face ao pedido original do utilizador, retornando a saída que receber a melhor pontuação. |

Vê [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) para a implementação completa.

> **🤖 Experimenta com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abre [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) e pergunta:
> - "Como é que o Supervisor decide quais agentes invocar?"
> - "Qual é a diferença entre os padrões de fluxo Supervisor e Sequencial?"
> - "Como posso personalizar o comportamento de planeamento do Supervisor?"

#### Compreender a Saída

Quando executares a demo, vais ver uma caminhada estruturada de como o Supervisor orquestra vários agentes. Eis o que cada secção significa:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**O cabeçalho** introduz o conceito do fluxo: um pipeline focado desde a leitura do ficheiro até à geração do relatório.

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

**Diagrama do Fluxo** mostra o fluxo de dados entre os agentes. Cada agente tem um papel específico:
- **FileAgent** lê ficheiros usando ferramentas MCP e guarda o conteúdo bruto em `fileContent`
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
1. **FileAgent** lê o ficheiro via MCP e guarda o conteúdo
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

O exemplo demonstra várias funcionalidades avançadas do módulo agentic. Vamos olhar mais de perto para o Agentic Scope e os Agent Listeners.

**Agentic Scope** mostra a memória partilhada onde os agentes guardaram os seus resultados usando `@Agent(outputKey="...")`. Isto permite:
- Que agentes posteriores acedam às saídas de agentes anteriores
- Que o Supervisor sintetize uma resposta final
- Que possas inspecionar o que cada agente produziu

<img src="../../../translated_images/pt-PT/agentic-scope.95ef488b6c1d02ef.webp" alt="Memória Partilhada Agentic Scope" width="800"/>

*O Agentic Scope atua como memória partilhada — o FileAgent escreve `fileContent`, o ReportAgent lê e escreve `report`, e o teu código lê o resultado final.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // Dados de ficheiro bruto do FileAgent
String report = scope.readState("report");            // Relatório estruturado do ReportAgent
```

**Agent Listeners** permitem monitorizar e depurar a execução do agente. A saída passo a passo que vês na demo vem de um AgentListener que se conecta a cada invocação do agente:

- **beforeAgentInvocation** - Chamado quando o Supervisor seleciona um agente, permitindo ver qual agente foi escolhido e porquê  
- **afterAgentInvocation** - Chamado quando um agente conclui, mostrando o seu resultado  
- **inheritedBySubagents** - Quando verdadeiro, o listener monitora todos os agentes na hierarquia  

<img src="../../../translated_images/pt-PT/agent-listeners.784bfc403c80ea13.webp" alt="Ciclo de Vida dos Listeners de Agente" width="800"/>

*Os Listeners de Agente ligam-se ao ciclo de execução — monitorizam quando os agentes iniciam, concluem ou encontram erros.*

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
  
Para além do padrão Supervisor, o módulo `langchain4j-agentic` oferece vários padrões e funcionalidades poderosas para fluxos de trabalho:

<img src="../../../translated_images/pt-PT/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Padrões de Fluxos de Trabalho de Agentes" width="800"/>

*Cinco padrões de fluxo de trabalho para orquestrar agentes — desde pipelines sequenciais simples até fluxos de aprovação com intervenção humana.*

| Padrão | Descrição | Caso de Uso |
|---------|-------------|----------|
| **Sequencial** | Executar agentes por ordem, o output flui para o próximo | Pipelines: pesquisa → análise → relatório |
| **Paralelo** | Executar agentes simultaneamente | Tarefas independentes: tempo + notícias + ações |
| **Loop** | Iterar até que a condição seja cumprida | Avaliação de qualidade: refinar até score ≥ 0,8 |
| **Condicional** | Roteamento baseado em condições | Classificar → encaminhar para agente especialista |
| **Humano na Intervenção** | Adicionar pontos de verificação humanos | Fluxos de aprovação, revisão de conteúdos |

## Conceitos-Chave

Agora que explorou o MCP e o módulo agentic em ação, vamos resumir quando usar cada abordagem.

<img src="../../../translated_images/pt-PT/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="Ecossistema MCP" width="800"/>

*O MCP cria um ecossistema de protocolo universal — qualquer servidor compatível com MCP funciona com qualquer cliente compatível com MCP, permitindo partilha de ferramentas entre aplicações.*

**MCP** é ideal quando quer aproveitar ecossistemas de ferramentas existentes, construir ferramentas que várias aplicações possam partilhar, integrar serviços de terceiros com protocolos standard, ou trocar implementações de ferramentas sem alterar código.

**O Módulo Agentic** funciona melhor quando quer definições declarativas de agentes com anotações `@Agent`, necessita orquestração de fluxos de trabalho (sequencial, loop, paralelo), prefere design de agentes baseado em interfaces em vez de código imperativo, ou combina múltiplos agentes que partilham outputs via `outputKey`.

**O padrão Supervisor Agent** destaca-se quando o fluxo de trabalho não é previsível antecipadamente e quer que o LLM decida, quando tem vários agentes especializados que precisam de orquestração dinâmica, quando está a construir sistemas conversacionais que direcionam para diferentes capacidades, ou quando quer o comportamento de agente mais flexível e adaptável.

<img src="../../../translated_images/pt-PT/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Ferramentas Personalizadas vs Ferramentas MCP" width="800"/>

*Quando usar métodos @Tool personalizados vs ferramentas MCP — ferramentas personalizadas para lógica específica da app com total segurança de tipo, ferramentas MCP para integrações padronizadas que funcionam em várias aplicações.*

## Parabéns!

<img src="../../../translated_images/pt-PT/course-completion.48cd201f60ac7570.webp" alt="Conclusão do Curso" width="800"/>

*A sua jornada de aprendizagem através de todos os cinco módulos — desde chat básico a sistemas agentic com o poder do MCP.*

Concluiu o curso LangChain4j para Iniciantes. Aprendeu:

- Como construir IA conversacional com memória (Módulo 01)  
- Padrões de engenharia de prompt para tarefas diferentes (Módulo 02)  
- Fundamentar respostas nos seus documentos com RAG (Módulo 03)  
- Criar agentes de IA básicos (assistentes) com ferramentas personalizadas (Módulo 04)  
- Integrar ferramentas padronizadas com os módulos LangChain4j MCP e Agentic (Módulo 05)  

### E agora?

Depois de completar os módulos, explore o [Guia de Testes](../docs/TESTING.md) para ver conceitos de testes LangChain4j em ação.

**Recursos Oficiais:**  
- [Documentação LangChain4j](https://docs.langchain4j.dev/) - Guias completos e referência API  
- [GitHub LangChain4j](https://github.com/langchain4j/langchain4j) - Código fonte e exemplos  
- [Tutoriais LangChain4j](https://docs.langchain4j.dev/tutorials/) - Tutoriais passo a passo para vários casos de uso  

Obrigado por concluir este curso!

---

**Navegação:** [← Anterior: Módulo 04 - Tools](../04-tools/README.md) | [Voltar ao Início](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, é recomendada a tradução profissional por um humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->