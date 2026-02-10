# Módulo 02: Engenharia de Prompts com GPT-5.2

## Índice

- [O Que Você Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo Engenharia de Prompts](../../../02-prompt-engineering)
- [Como Isso Usa LangChain4j](../../../02-prompt-engineering)
- [Os Padrões Principais](../../../02-prompt-engineering)
- [Usando Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Empolgação](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat de Múltiplas Rodadas](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrangida](../../../02-prompt-engineering)
- [O Que Você Está Realmente Aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O Que Você Vai Aprender

No módulo anterior, você viu como a memória possibilita IA conversacional e usou os Modelos GitHub para interações básicas. Agora vamos focar em como você faz perguntas – os próprios prompts – usando o GPT-5.2 do Azure OpenAI. A maneira como você estrutura seus prompts afeta dramaticamente a qualidade das respostas que recebe.

Usaremos o GPT-5.2 porque ele introduz controle de raciocínio – você pode dizer ao modelo quanto pensar antes de responder. Isso torna diferentes estratégias de prompt mais evidentes e ajuda a entender quando usar cada abordagem. Também vamos nos beneficiar dos limites de taxa menores do Azure para GPT-5.2 em comparação com os Modelos GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não concluiu o Módulo 01, siga as instruções de implantação de lá antes.

## Entendendo Engenharia de Prompts

Engenharia de prompts é sobre criar texto de entrada que consistentemente entrega os resultados que você precisa. Não é apenas fazer perguntas – é estruturar os pedidos para que o modelo entenda exatamente o que você quer e como entregar.

Pense nisso como dar instruções a um colega. “Corrija o bug” é vago. “Corrija a exceção de ponteiro nulo no UserService.java linha 45 adicionando uma verificação de nulo” é específico. Modelos de linguagem funcionam da mesma forma – especificidade e estrutura importam.

## Como Isso Usa LangChain4j

Este módulo demonstra padrões avançados de prompting usando a mesma base LangChain4j dos módulos anteriores, com foco na estrutura do prompt e controle do raciocínio.

<img src="../../../translated_images/pt-BR/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Como LangChain4j conecta seus prompts ao Azure OpenAI GPT-5.2*

**Dependências** – O Módulo 02 usa as seguintes dependências langchain4j definidas em `pom.xml`:  
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
  
**Configuração OpenAiOfficialChatModel** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

O modelo de chat é configurado manualmente como um bean Spring usando o cliente oficial OpenAI, que suporta endpoints Azure OpenAI. A principal diferença do Módulo 01 é como estruturamos os prompts enviados para `chatModel.chat()`, não a configuração do modelo em si.

**Mensagens do Sistema e do Usuário** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j separa tipos de mensagens para clareza. `SystemMessage` define o comportamento e contexto da IA (como "Você é um revisor de código"), enquanto `UserMessage` contém o pedido real. Essa separação permite manter comportamento consistente da IA entre diferentes consultas do usuário.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/pt-BR/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage fornece contexto persistente enquanto UserMessages contêm pedidos individuais*

**MessageWindowChatMemory para Multi-Rodadas** – Para o padrão de conversa multi-turno, reutilizamos `MessageWindowChatMemory` do Módulo 01. Cada sessão tem sua própria instância de memória armazenada em um `Map<String, ChatMemory>`, permitindo múltiplas conversas concorrentes sem mistura de contexto.

**Templates de Prompt** – O foco real aqui é engenharia de prompts, não novas APIs LangChain4j. Cada padrão (baixa empolgação, alta empolgação, execução de tarefa, etc.) usa o mesmo método `chatModel.chat(prompt)` mas com strings de prompt cuidadosamente estruturadas. As tags XML, instruções e formatação fazem parte do texto do prompt, não são funcionalidades do LangChain4j.

**Controle de Raciocínio** – O esforço de raciocínio do GPT-5.2 é controlado por instruções no prompt como "máximo 2 passos de raciocínio" ou "explore detalhadamente". Essas são técnicas de engenharia de prompts, não configurações do LangChain4j. A biblioteca apenas entrega seus prompts ao modelo.

A principal conclusão: LangChain4j fornece a infraestrutura (conexão com modelo via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memória, manipulação de mensagens via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), enquanto este módulo ensina a criar prompts eficazes dentro dessa infraestrutura.

## Os Padrões Principais

Nem todos os problemas precisam do mesmo approach. Algumas perguntas precisam de respostas rápidas, outras exigem pensamento profundo. Algumas precisam de raciocínio visível, outras só resultados. Este módulo cobre oito padrões de prompting – cada um otimizado para cenários diferentes. Você vai experimentar todos para aprender quando cada abordagem funciona melhor.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Visão geral dos oito padrões de engenharia de prompts e seus casos de uso*

<img src="../../../translated_images/pt-BR/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Baixa empolgação (rápido, direto) vs Alta empolgação (detalhado, exploratório) abordagens de raciocínio*

**Baixa Empolgação (Rápido & Focado)** – Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo – máximo 2 passos. Use para cálculos, consultas ou perguntas diretas.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Explore com GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:  
> - "Qual é a diferença entre os padrões de prompting de baixa empolgação e alta empolgação?"  
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"  
> - "Quando devo usar padrões de autorreflexão versus instrução direta?"

**Alta Empolgação (Profundo & Completo)** – Para problemas complexos onde você quer análise abrangente. O modelo explora profundamente e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura ou pesquisa complexa.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Execução de Tarefa (Progresso Passo a Passo)** – Para fluxos de trabalho multi-etapas. O modelo fornece um plano inicial, narra cada passo enquanto trabalha, depois dá um resumo. Use para migrações, implementações ou qualquer processo com múltiplas etapas.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
O prompting Chain-of-Thought pede explicitamente que o modelo mostre seu processo de raciocínio, melhorando a precisão em tarefas complexas. A divisão passo a passo ajuda humanos e IA a entender a lógica.

> **🤖 Experimente com Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:  
> - "Como adaptar o padrão de execução de tarefa para operações de longa duração?"  
> - "Quais as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"  
> - "Como capturar e mostrar atualizações intermediárias de progresso em uma interface?"

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Fluxo Plan → Execute → Summarize para tarefas multi-etapas*

**Código Auto-Reflexivo** – Para gerar código de qualidade de produção. O modelo gera código, verifica critérios de qualidade e melhora iterativamente. Use para construir novas funcionalidades ou serviços.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ciclo iterativo de melhoria – gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** – Para avaliação consistente. O modelo revisa código usando um framework fixo (correção, práticas, desempenho, segurança). Use para revisões de código ou avaliações de qualidade.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Experimente com Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre análise estruturada:  
> - "Como personalizar o framework de análise para diferentes tipos de revisão de código?"  
> - "Qual a melhor forma de interpretar e agir sobre saída estruturada programaticamente?"  
> - "Como garantir níveis consistentes de severidade entre diferentes sessões de revisão?"

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework de quatro categorias para revisões consistentes com níveis de severidade*

**Chat de Múltiplas Rodadas** – Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões de ajuda interativas ou Q&A complexo.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/pt-BR/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Como o contexto da conversa se acumula ao longo de múltiplas rodadas até atingir o limite de tokens*

**Raciocínio Passo a Passo** – Para problemas que precisam de lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas matemáticos, quebra-cabeças lógicos ou quando precisa entender o processo de pensamento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pt-BR/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Dividindo problemas em passos lógicos explícitos*

**Saída Constrangida** – Para respostas com requisitos específicos de formato. O modelo segue estritamente regras de formato e comprimento. Use para resumos ou quando precisar de estrutura precisa de saída.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pt-BR/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Impondo requisitos específicos de formato, comprimento e estrutura*

## Usando Recursos Azure Existentes

**Verifique a implantação:**

Garanta que o arquivo `.env` exista no diretório raiz com credenciais Azure (criado durante o Módulo 01):  
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8083. Você pode pular os comandos abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você encontra na Barra de Atividades à esquerda do VS Code (ícone do Spring Boot).

No Spring Boot Dashboard, você pode:  
- Ver todas as aplicações Spring Boot no workspace  
- Iniciar/parar aplicações com um clique  
- Visualizar logs da aplicação em tempo real  
- Monitorar o status da aplicação

Basta clicar no botão de play próximo a "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e irão construir os JARs se não existirem.

> **Nota:** Se preferir compilar todos os módulos manualmente antes de iniciar:  
>  
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
Abra http://localhost:8083 no seu navegador.

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
  
## Capturas de Tela da Aplicação

<img src="../../../translated_images/pt-BR/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O painel principal mostrando os 8 padrões de engenharia de prompts com suas características e casos de uso*

## Explorando os Padrões

A interface web permite experimentar diferentes estratégias de prompting. Cada padrão resolve problemas distintos – experimente para ver quando cada abordagem se destaca.

### Baixa vs Alta Empolgação

Faça uma pergunta simples, como "Qual é 15% de 200?" usando Baixa Empolgação. Você obterá uma resposta instantânea e direta. Agora pergunte algo complexo como "Desenhe uma estratégia de cache para uma API de alto tráfego" usando Alta Empolgação. Observe como o modelo desacelera e fornece raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta – mas o prompt diz quanto pensar.
<img src="../../../translated_images/pt-BR/low-eagerness-demo.898894591fb23aa0.webp" alt="Demonstração de Baixa Disposição" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-BR/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demonstração de Alta Disposição" width="800"/>

*Estratégia abrangente de cache (2.8MB)*

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapas se beneficiam do planejamento antecipado e narração do progresso. O modelo descreve o que fará, narra cada passo e depois resume os resultados.

<img src="../../../translated_images/pt-BR/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demonstração de Execução de Tarefas" width="800"/>

*Criando um endpoint REST com narração passo a passo (3.9MB)*

### Código Auto-Reflexivo

Experimente "Criar um serviço de validação de email". Em vez de apenas gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica falhas e melhora. Você verá ele iterar até que o código atenda aos padrões de produção.

<img src="../../../translated_images/pt-BR/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demonstração de Código Auto-Reflexivo" width="800"/>

*Serviço completo de validação de email (5.2MB)*

### Análise Estruturada

Revisões de código precisam de frameworks de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-BR/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demonstração de Análise Estruturada" width="800"/>

*Revisão de código baseada em framework*

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e então imediatamente siga com "Mostre-me um exemplo". O modelo lembra da sua primeira pergunta e fornece um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria vaga demais.

<img src="../../../translated_images/pt-BR/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demonstração de Chat Multi-Turnos" width="800"/>

*Preservação do contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema de matemática e tente tanto com Raciocínio Passo a Passo quanto com Baixa Disposição. Baixa disposição só dá a resposta – rápido, mas opaco. Passo a passo mostra cada cálculo e decisão.

<img src="../../../translated_images/pt-BR/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demonstração de Raciocínio Passo a Passo" width="800"/>

*Problema matemático com passos explícitos*

### Saída Confinada

Quando você precisa de formatos específicos ou contagem exata de palavras, este padrão assegura adesão rigorosa. Tente gerar um resumo com exatamente 100 palavras em formato de lista.

<img src="../../../translated_images/pt-BR/constrained-output-demo.567cc45b75da1633.webp" alt="Demonstração de Saída Confinada" width="800"/>

*Resumo de aprendizado de máquina com controle de formato*

## O Que Você Está Realmente Aprendendo

**Esforço de Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional por meio dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo leva tempo para pensar profundamente. Você está aprendendo a ajustar o esforço à complexidade da tarefa – não perca tempo em perguntas simples, mas também não apresse decisões complexas.

**Estrutura Guia o Comportamento**

Notou as tags XML nos prompts? Elas não são decorativas. Modelos seguem instruções estruturadas mais confiavelmente que texto livre. Quando você precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Estrutura do Prompt" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões auto-reflexivos funcionam ao tornar explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça certo", você diz exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar sua própria saída e melhorar. Isso transforma geração de código de uma loteria em um processo.

**Contexto É Finito**

Conversas multi-turno funcionam incluindo o histórico de mensagens em cada solicitação. Mas há um limite – todo modelo tem contagem máxima de tokens. Conforme as conversas crescem, você precisará de estratégias para manter o contexto relevante sem atingir esse teto. Este módulo mostra como a memória funciona; depois você aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Recuperação Aprimorada)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, por favor, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->