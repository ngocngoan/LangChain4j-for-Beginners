# Módulo 02: Engenharia de Prompts com GPT-5.2

## Índice

- [O Que Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Compreender a Engenharia de Prompts](../../../02-prompt-engineering)
- [Como Isto Usa LangChain4j](../../../02-prompt-engineering)
- [Os Padrões Principais](../../../02-prompt-engineering)
- [Utilizar Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Ecrã da Aplicação](../../../02-prompt-engineering)
- [Explorar os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrangida](../../../02-prompt-engineering)
- [O Que Está Realmente a Aprender](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O Que Vai Aprender

No módulo anterior, viu como a memória permite a IA conversacional e usou os Modelos GitHub para interações básicas. Agora vamos centrar-nos em como faz perguntas – os próprios prompts – usando o GPT-5.2 do Azure OpenAI. A forma como estrutura os seus prompts afeta dramaticamente a qualidade das respostas que obtém.

Vamos usar o GPT-5.2 porque este introduz controlo do raciocínio – pode indicar ao modelo quanto pensamento deve fazer antes de responder. Isto torna as diferentes estratégias de prompting mais evidentes e ajuda a entender quando usar cada abordagem. Também beneficiamos dos limites de taxa mais elevados do Azure para o GPT-5.2 comparado com os Modelos GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Ficheiro `.env` no diretório raiz com credenciais Azure (criado por `azd up` no Módulo 01)

> **Nota:** Se ainda não concluiu o Módulo 01, siga primeiro as instruções de implantação aí indicadas.

## Compreender a Engenharia de Prompts

Engenharia de prompts é sobre desenhar texto de entrada que consistentemente lhe dá os resultados que precisa. Não se trata só de fazer perguntas – é estruturar os pedidos de forma que o modelo compreenda exatamente o que quer e como entregar.

Pense nisso como dar instruções a um colega. "Corrige o bug" é vago. "Corrige a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação nula" é específico. Os modelos de linguagem funcionam da mesma forma – especificidade e estrutura são importantes.

## Como Isto Usa LangChain4j

Este módulo demonstra padrões avançados de prompting usando a mesma base LangChain4j dos módulos anteriores, com foco na estrutura do prompt e controlo do raciocínio.

<img src="../../../translated_images/pt-PT/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Como o LangChain4j liga os seus prompts ao Azure OpenAI GPT-5.2*

**Dependências** – O Módulo 02 usa as seguintes dependências langchain4j definidas no `pom.xml`:
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

**Mensagens do Sistema e do Utilizador** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j separa os tipos de mensagens para clareza. `SystemMessage` define o comportamento e contexto da IA (como "Você é um revisor de código"), enquanto `UserMessage` contém o pedido real. Esta separação permite manter comportamento consistente da IA entre diferentes consultas do utilizador.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/pt-PT/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage fornece contexto persistente enquanto UserMessages contêm pedidos individuais*

**MessageWindowChatMemory para Multi-Turno** – Para o padrão de conversação multi-turno, reutilizamos `MessageWindowChatMemory` do Módulo 01. Cada sessão tem a sua própria instância de memória guardada num `Map<String, ChatMemory>`, permitindo múltiplas conversas simultâneas sem mistura de contexto.

**Templates de Prompt** – O foco real aqui é engenharia de prompts, não novas APIs LangChain4j. Cada padrão (baixa disposição, alta disposição, execução de tarefas, etc.) usa o mesmo método `chatModel.chat(prompt)`, mas com strings de prompt cuidadosamente estruturadas. As tags XML, instruções e formatação fazem parte do texto do prompt, não são recursos LangChain4j.

**Controlo do Raciocínio** – O esforço de raciocínio do GPT-5.2 é controlado por instruções no prompt como "máximo 2 passos de raciocínio" ou "explorar completamente". Estas são técnicas de engenharia de prompts, não configurações do LangChain4j. A biblioteca apenas entrega os seus prompts ao modelo.

O principal a reter: LangChain4j fornece a infraestrutura (ligação ao modelo via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memória, gestão de mensagens através de [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), enquanto este módulo ensina a criar prompts eficazes dentro dessa infraestrutura.

## Os Padrões Principais

Nem todos os problemas precisam do mesmo método. Algumas perguntas precisam de respostas rápidas, outras exigem pensamento profundo. Algumas requerem raciocínio visível, outras apenas resultados. Este módulo cobre oito padrões de prompting – cada um otimizado para diferentes cenários. Vai experimentar todos para aprender quando funciona melhor cada abordagem.

<img src="../../../translated_images/pt-PT/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Visão geral dos oito padrões de engenharia de prompts e os seus casos de uso*

<img src="../../../translated_images/pt-PT/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Baixa disposição (rápido, direto) vs Alta disposição (detalhado, exploratório) em abordagens de raciocínio*

**Baixa Disposição (Rápido & Focado)** – Para perguntas simples onde quer respostas rápidas e diretas. O modelo faz raciocínio mínimo – máximo 2 passos. Use para cálculos, consultas ou perguntas simples.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore com GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:
> - "Qual a diferença entre padrões de prompting de baixa disposição e alta disposição?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de auto-reflexão vs instrução direta?"

**Alta Disposição (Profundo & Detalhado)** – Para problemas complexos onde quer análises exaustivas. O modelo explora completamente e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura ou pesquisas complexas.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** – Para fluxos de trabalho multi-etapas. O modelo fornece um plano inicial, narra cada passo enquanto trabalha, depois dá um resumo. Use para migrações, implementações ou qualquer processo multi-etapas.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

A prompt Chain-of-Thought pede explicitamente ao modelo para mostrar o seu processo de raciocínio, melhorando a precisão em tarefas complexas. A decomposição passo a passo ajuda tanto humanos como IA a compreenderem a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como adapto o padrão de execução de tarefas para operações de longa duração?"
> - "Quais as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como capturo e mostro atualizações intermediárias de progresso numa interface?"

<img src="../../../translated_images/pt-PT/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planejar → Executar → Resumir fluxo de trabalho para tarefas multi-etapas*

**Código Auto-reflexivo** – Para gerar código com qualidade de produção. O modelo gera código, verifica contra critérios de qualidade e melhora iterativamente. Use ao construir novas funcionalidades ou serviços.

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

<img src="../../../translated_images/pt-PT/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Loop iterativo de melhoria – gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** – Para avaliações consistentes. O modelo revê código usando um quadro fixo (correção, práticas, desempenho, segurança). Use para revisões de código ou avaliações de qualidade.

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

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre análise estruturada:
> - "Como personalizo o quadro de análise para diferentes tipos de revisões de código?"
> - "Qual a melhor forma de analisar e atuar sobre saída estruturada programaticamente?"
> - "Como garantir níveis de severidade consistentes em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-PT/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Quadro de quatro categorias para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turno** – Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões de ajuda interativas ou Q&A complexos.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pt-PT/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Como o contexto da conversa acumula por múltiplos turnos até atingir o limite de tokens*

**Raciocínio Passo a Passo** – Para problemas que requerem lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas de matemática, puzzles lógicos ou quando precisar entender o processo de pensamento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-PT/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Decompor problemas em passos lógicos explícitos*

**Saída Constrangida** – Para respostas com requisitos específicos de formato. O modelo segue rigorosamente regras de formato e comprimento. Use para resumos ou quando precisar de estrutura de saída precisa.

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

<img src="../../../translated_images/pt-PT/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Aplicar requisitos específicos de formato, comprimento e estrutura*

## Utilizar Recursos Azure Existentes

**Verificar implantação:**

Certifique-se que o ficheiro `.env` existe no diretório raiz com credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está a correr na porta 8083. Pode saltar os comandos de arranque abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividade no lado esquerdo do VS Code (procure o ícone do Spring Boot).

Pelo Spring Boot Dashboard pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Ver logs da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de play ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-PT/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usar scripts shell**

Iniciar todas as aplicações web (módulos 01-04):

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

Ou iniciar só este módulo:

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` raiz e constroem os JARs se estes não existirem.

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

Abra http://localhost:8083 no seu navegador.

**Para parar:**

**Bash:**
```bash
./stop.sh  # Este módulo apenas
# Ou
cd .. && ./stop-all.sh  # Todos os módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Capturas de Ecrã da Aplicação

<img src="../../../translated_images/pt-PT/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Painel principal mostrando os 8 padrões de engenharia de prompts com as suas características e casos de uso*

## Explorar os Padrões

A interface web permite experimentar diferentes estratégias de prompting. Cada padrão resolve problemas diferentes – experimente para ver quando cada abordagem é melhor.

### Baixa vs Alta Disposição

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Disposição. Receberá uma resposta instantânea e direta. Agora pergunte algo complexo como "Desenha uma estratégia de cache para uma API de alto tráfego" usando Alta Disposição. Veja como o modelo desacelera e fornece raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta – mas o prompt indica quanto pensamento fazer.
<img src="../../../translated_images/pt-PT/low-eagerness-demo.898894591fb23aa0.webp" alt="Demonstração de Baixa Disposição" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-PT/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demonstração de Alta Disposição" width="800"/>

*Estratégia abrangente de cache (2,8MB)*

### Execução de Tarefas (Preambulo das Ferramentas)

Fluxos de trabalho com múltiplas etapas beneficiam-se de planeamento prévio e narração do progresso. O modelo delineia o que vai fazer, narra cada passo e depois resume os resultados.

<img src="../../../translated_images/pt-PT/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demonstração de Execução de Tarefas" width="800"/>

*Criação de um endpoint REST com narração passo a passo (3,9MB)*

### Código Auto-Refletido

Tente "Criar um serviço de validação de email". Em vez de simplesmente gerar código e parar, o modelo gera, avalia com base em critérios de qualidade, identifica fraquezas e melhora. Verá o modelo iterar até que o código atinja padrões de produção.

<img src="../../../translated_images/pt-PT/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demonstração de Código Auto-Refletido" width="800"/>

*Serviço completo de validação de email (5,2MB)*

### Análise Estruturada

Revisões de código precisam de quadros de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-PT/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demonstração de Análise Estruturada" width="800"/>

*Revisão de código baseada em frameworks*

### Chat em Múltiplas Trocas

Pergunte "O que é Spring Boot?" e logo de seguida siga com "Mostra-me um exemplo". O modelo lembra-se da sua primeira pergunta e oferece-lhe um exemplo de Spring Boot especificamente. Sem memória, a segunda pergunta seria demasiado vaga.

<img src="../../../translated_images/pt-PT/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demonstração de Chat em Múltiplas Trocas" width="800"/>

*Preservação de contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema de matemática e experimente com Raciocínio Passo a Passo e Baixa Disposição. Baixa disposição apenas fornece a resposta - rápido mas opaco. Passo a passo mostra todos os cálculos e decisões.

<img src="../../../translated_images/pt-PT/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demonstração de Raciocínio Passo a Passo" width="800"/>

*Problema de matemática com passos explícitos*

### Saída Constrainada

Quando precisa de formatos específicos ou contagem de palavras, este padrão impõe cumprimento rigoroso. Experimente gerar um resumo com exatamente 100 palavras em formato de pontos.

<img src="../../../translated_images/pt-PT/constrained-output-demo.567cc45b75da1633.webp" alt="Demonstração de Saída Constrainada" width="800"/>

*Resumo de aprendizagem automática com controlo de formato*

## O Que Está Realmente a Aprender

**O Esforço de Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo demora a pensar profundamente. Está a aprender a ajustar o esforço à complexidade da tarefa - não perca tempo em questões simples, mas também não se apresse em decisões complexas.

**A Estrutura Guia o Comportamento**

Reparou nas tags XML nos prompts? Não são decorativas. Os modelos seguem instruções estruturadas de forma mais fiável do que texto livre. Quando precisa de processos em múltiplas etapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-PT/prompt-structure.a77763d63f4e2f89.webp" alt="Estrutura do Prompt" width="800"/>

*Anatomia de um prompt bem estruturado com secções claras e organização ao estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões de autorreflexão funcionam tornando os critérios de qualidade explícitos. Em vez de esperar que o modelo "faça direito", diz-lhe exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar a sua própria saída e melhorar. Isto transforma a geração de código numa processo, não numa lotaria.

**O Contexto É Finito**

Conversas multi-turno funcionam incluindo o histórico de mensagens em cada pedido. Mas há um limite - todo modelo tem um máximo de tokens. À medida que as conversas crescem, vai precisar de estratégias para manter contexto relevante sem atingir esse limite. Este módulo mostra como a memória funciona; mais tarde vai aprender quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração Aumentada por Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, esteja ciente de que as traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->