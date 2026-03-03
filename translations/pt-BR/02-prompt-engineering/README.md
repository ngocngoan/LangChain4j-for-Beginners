# Módulo 02: Engenharia de Prompt com GPT-5.2

## Índice

- [Video Walkthrough](../../../02-prompt-engineering)
- [O que você vai aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Modelos de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Execute a Aplicação](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Diligência](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Confinada](../../../02-prompt-engineering)
- [O que você está realmente aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## Video Walkthrough

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## O que você vai aprender

O diagrama a seguir oferece uma visão geral dos principais tópicos e habilidades que você desenvolverá neste módulo — desde técnicas de refinamento de prompts até o fluxo de trabalho passo a passo que você seguirá.

<img src="../../../translated_images/pt-BR/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Nos módulos anteriores, você explorou interações básicas do LangChain4j com modelos do GitHub e viu como a memória permite IA conversacional com Azure OpenAI. Agora, focaremos em como você faz perguntas — os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como você estrutura seus prompts afeta dramaticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting e depois avançamos para oito padrões avançados que aproveitam ao máximo as capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque ele introduz controle de raciocínio - você pode dizer ao modelo quanto ele deve pensar antes de responder. Isso torna as diferentes estratégias de prompting mais evidentes e ajuda você a entender quando usar cada abordagem. Também aproveitaremos os limites de taxa menores do Azure para o GPT-5.2 em comparação com os modelos do GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não completou o Módulo 01, siga as instruções de implantação lá primeiro.

## Entendendo Engenharia de Prompt

Na essência, engenharia de prompt é a diferença entre instruções vagas e precisas, como a comparação abaixo ilustra.

<img src="../../../translated_images/pt-BR/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Engenharia de prompt é sobre projetar texto de entrada que consistentemente gera os resultados que você precisa. Não é apenas fazer perguntas - é estruturar solicitações para que o modelo entenda exatamente o que quer e como entregar.

Pense nisso como dar instruções a um colega. "Conserte o bug" é vago. "Conserte a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação nula" é específico. Modelos de linguagem funcionam da mesma forma - especificidade e estrutura importam.

O diagrama abaixo mostra como o LangChain4j se encaixa nessa imagem — conectando seus padrões de prompt ao modelo através dos blocos de construção SystemMessage e UserMessage.

<img src="../../../translated_images/pt-BR/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j fornece a infraestrutura — conexões com o modelo, memória e tipos de mensagem — enquanto os padrões de prompt são apenas texto cuidadosamente estruturado que você envia por essa infraestrutura. Os blocos essenciais são `SystemMessage` (que define o comportamento e o papel da IA) e `UserMessage` (que carrega sua solicitação real).

## Fundamentos da Engenharia de Prompt

As cinco técnicas centrais mostradas abaixo formam a base da engenharia de prompt eficaz. Cada uma aborda um aspecto diferente da comunicação com modelos de linguagem.

<img src="../../../translated_images/pt-BR/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Antes de mergulharmos nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. Estes são os blocos de construção que todo engenheiro de prompt deve conhecer. Se você já trabalhou no [módulo de Início Rápido](../00-quick-start/README.md#2-prompt-patterns), já viu esses em ação — aqui está a estrutura conceitual por trás deles.

### Zero-Shot Prompting

A abordagem mais simples: dê ao modelo uma instrução direta sem exemplos. O modelo confia inteiramente em seu treinamento para entender e executar a tarefa. Funciona bem para solicitações diretas onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-BR/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa apenas pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções ou qualquer tarefa que o modelo possa lidar sem orientação adicional.

### Few-Shot Prompting

Forneça exemplos que demonstrem o padrão que deseja que o modelo siga. O modelo aprende o formato esperado de entrada-saída pelos seus exemplos e aplica a novos inputs. Isso melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-BR/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Aprendendo com exemplos — o modelo identifica o padrão e o aplica a novos inputs*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**Quando usar:** Classificações customizadas, formatação consistente, tarefas específicas de domínio, ou quando os resultados zero-shot são inconsistentes.

### Chain of Thought

Peça ao modelo para mostrar seu raciocínio passo a passo. Em vez de ir direto à resposta, o modelo divide o problema e trabalha cada parte explicitamente. Isso melhora a precisão em tarefas de matemática, lógica e raciocínio multi-etapas.

<img src="../../../translated_images/pt-BR/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Raciocínio passo a passo — quebrando problemas complexos em etapas lógicas explícitas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// O modelo mostra: 15 - 8 = 7, então 7 + 12 = 19 maçãs
```

**Quando usar:** Problemas matemáticos, quebra-cabeças lógicos, depuração ou qualquer tarefa onde mostrar o processo de raciocínio melhora precisão e confiança.

### Role-Based Prompting

Defina uma persona ou papel para a IA antes de fazer a pergunta. Isso fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou um "auditor de segurança".

<img src="../../../translated_images/pt-BR/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Configurando contexto e persona — a mesma pergunta recebe resposta diferente conforme o papel atribuído*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**Quando usar:** Revisões de código, tutoria, análise específica de domínio ou quando você precisa de respostas ajustadas a um nível de expertise ou perspectiva particular.

### Modelos de Prompt

Crie prompts reutilizáveis com espaços reservados para variáveis. Em vez de escrever um novo prompt toda vez, defina um modelo uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isso com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-BR/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompts reutilizáveis com espaços para variáveis — um modelo, muitos usos*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**Quando usar:** Consultas repetidas com diferentes inputs, processamento em lote, construção de fluxos de trabalho reutilizáveis de IA ou qualquer cenário onde a estrutura do prompt permanece a mesma mas os dados mudam.

---

Esses cinco fundamentos oferecem uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O restante deste módulo constrói sobre eles com **oito padrões avançados** que aproveitam o controle de raciocínio, autoavaliação e capacidade de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos avançar para os oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras de pensamento profundo. Algumas precisam de raciocínio visível, outras só dos resultados. Cada padrão abaixo é otimizado para um cenário diferente — e o controle de raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e seus casos de uso*

O GPT-5.2 adiciona outra dimensão a esses padrões: *controle de raciocínio*. O controle abaixo mostra como você pode ajustar o esforço de pensamento do modelo — de respostas rápidas e diretas a análises profundas e detalhadas.

<img src="../../../translated_images/pt-BR/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*O controle de raciocínio do GPT-5.2 permite especificar quanto o modelo deve pensar — de respostas rápidas e diretas a explorações profundas*

**Baixa Diligência (Rápido e Focado)** - Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo - máximo 2 passos. Use para cálculos, consultas ou perguntas diretas.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore com GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:
> - "Qual a diferença entre os padrões de prompting de baixa e alta diligência?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão versus instrução direta?"

**Alta Diligência (Profundo e Detalhado)** - Para problemas complexos onde você quer análise abrangente. O modelo explora a fundo e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura ou pesquisas complexas.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho multi-etapas. O modelo fornece um plano inicial, narra cada passo enquanto executa e depois faz um resumo. Use para migrações, implementações ou qualquer processo com múltiplas etapas.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting pede explicitamente ao modelo para mostrar seu processo de raciocínio, melhorando a precisão para tarefas complexas. A divisão passo a passo ajuda tanto humanos quanto IA a entenderem a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre esse padrão:
> - "Como eu adaptaria o padrão de execução de tarefas para operações de longa duração?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e exibir atualizações intermediárias de progresso em uma interface?"

O diagrama abaixo ilustra esse fluxo Plano → Executar → Resumir.

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Fluxo Plano → Executar → Resumir para tarefas multi-etapas*

**Código Auto-Reflexivo** - Para gerar código com qualidade de produção. O modelo gera código seguindo padrões de produção com tratamento adequado de erros. Use para construir novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

O diagrama abaixo mostra esse ciclo iterativo de melhoria — gerar, avaliar, identificar fraquezas e refinar até que o código atenda aos padrões de produção.

<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ciclo iterativo de melhoria - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliação consistente. O modelo revisa código usando uma estrutura fixa (correção, práticas, desempenho, segurança, manutenção). Use para revisões de código ou avaliações de qualidade.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre análise estruturada:
> - "Como posso personalizar a estrutura de análise para diferentes tipos de revisões de código?"
> - "Qual a melhor maneira de interpretar e agir com saída estruturada programaticamente?"
> - "Como garantir níveis consistentes de severidade em diferentes sessões de revisão?"

O diagrama a seguir mostra como essa estrutura organizada divide uma revisão de código em categorias consistentes com níveis de severidade.

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Estrutura para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e vai construindo sobre elas. Use para sessões de ajuda interativas ou perguntas e respostas complexas.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

O diagrama abaixo visualiza como o contexto da conversa se acumula a cada turno e como isso se relaciona com o limite de tokens do modelo.

<img src="../../../translated_images/pt-BR/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Como o contexto da conversa se acumula em múltiplos turnos até atingir o limite de tokens*
**Raciocínio Passo a Passo** - Para problemas que exigem lógica visível. O modelo mostra raciocínio explícito para cada etapa. Use isso para problemas matemáticos, quebra-cabeças lógicos ou quando precisar entender o processo de pensamento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

O diagrama abaixo ilustra como o modelo divide problemas em etapas lógicas explícitas numeradas.

<img src="../../../translated_images/pt-BR/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Padrão Passo a Passo" width="800"/>

*Dividindo problemas em etapas lógicas explícitas*

**Saída Constrita** - Para respostas com requisitos específicos de formato. O modelo segue rigorosamente as regras de formato e comprimento. Use isso para resumos ou quando precisar de uma estrutura precisa de saída.

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

O diagrama a seguir mostra como restrições orientam o modelo a produzir uma saída que segue estritamente seu formato e requisitos de comprimento.

<img src="../../../translated_images/pt-BR/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Padrão de Saída Constrita" width="800"/>

*Impondo requisitos específicos de formato, comprimento e estrutura*

## Execute a Aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` exista no diretório raiz com as credenciais do Azure (criado durante o Módulo 01). Execute isso a partir do diretório do módulo (`02-prompt-engineering/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` a partir do diretório raiz (conforme descrito no Módulo 01), este módulo já está rodando na porta 8083. Você pode pular os comandos de início abaixo e ir direto para http://localhost:8083.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure pelo ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs das aplicações em tempo real
- Monitorar o status das aplicações

Basta clicar no botão de play ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*O Spring Boot Dashboard no VS Code — inicie, pare e monitore todos os módulos a partir de um único lugar*

**Opção 2: Usando scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # A partir do diretório raiz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Desde o diretório raiz
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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e construirão os JARs se eles não existirem.

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

Aqui está a interface principal do módulo de engenharia de prompt, onde você pode experimentar todos os oito padrões lado a lado.

<img src="../../../translated_images/pt-BR/dashboard-home.5444dbda4bc1f79d.webp" alt="Tela Inicial do Dashboard" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O dashboard principal mostrando os 8 padrões de engenharia de prompt com suas características e casos de uso*

## Explorando os Padrões

A interface web permite que você experimente diferentes estratégias de prompting. Cada padrão resolve problemas distintos — experimente para ver quando cada abordagem se destaca.

> **Nota: Streaming vs Não Streaming** — Cada página de padrão oferece dois botões: **🔴 Stream Response (Live)** e uma opção **Não streaming**. Streaming usa Server-Sent Events (SSE) para mostrar os tokens em tempo real enquanto o modelo os gera, para que você veja o progresso imediatamente. A opção não streaming espera pela resposta completa antes de exibi-la. Para prompts que exigem raciocínio aprofundado (ex: High Eagerness, Self-Reflecting Code), a chamada não streaming pode demorar muito — às vezes minutos — sem feedback visível. **Use streaming ao experimentar prompts complexos** para ver o modelo trabalhando e evitar a impressão de timeout.
>
> **Nota: Requisito do Navegador** — O recurso de streaming usa a Fetch Streams API (`response.body.getReader()`) que requer um navegador completo (Chrome, Edge, Firefox, Safari). Não funciona no Simple Browser embutido do VS Code, pois seu webview não suporta a API ReadableStream. Se usar o Simple Browser, os botões não streaming funcionarão normalmente — apenas os botões de streaming são afetados. Abra `http://localhost:8083` em um navegador externo para a experiência completa.

### Baixa vs Alta Empolgação (Eagerness)

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Empolgação. Você receberá uma resposta direta e instantânea. Agora pergunte algo complexo como "Desenvolva uma estratégia de cache para uma API de alto tráfego" usando Alta Empolgação. Clique em **🔴 Stream Response (Live)** e veja o raciocínio detalhado do modelo aparecer token por token. Mesmo modelo, mesma estrutura de pergunta — mas o prompt indica quanto pensamento fazer.

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapas se beneficiam de planejamento prévio e narração do progresso. O modelo descreve o que fará, narra cada etapa e depois resume os resultados.

### Código Auto-Reflexivo

Experimente "Crie um serviço de validação de e-mail". Em vez de apenas gerar o código e parar, o modelo gera, avalia com critérios de qualidade, identifica pontos fracos e melhora. Você verá iterar até que o código alcance padrões de produção.

### Análise Estruturada

Revisões de código precisam de frameworks consistentes de avaliação. O modelo analisa código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e em seguida "Mostre um exemplo". O modelo lembra a primeira pergunta e fornece um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria vaga demais.

### Raciocínio Passo a Passo

Escolha um problema matemático e tente tanto com Raciocínio Passo a Passo quanto com Baixa Empolgação. A baixa empolgação só dá a resposta — rápido, mas opaco. Passo a passo mostra cada cálculo e decisão.

### Saída Constrita

Quando você precisa de formatos ou contagens precisas de palavras, este padrão impõe aderência estrita. Experimente gerar um resumo com exatamente 100 palavras em formato de lista.

## O Que Você Está Realmente Aprendendo

**O Esforço de Raciocínio Muda Tudo**

GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço faz o modelo pensar profundamente e com calma. Você está aprendendo a ajustar o esforço à complexidade da tarefa — não perca tempo com perguntas simples, mas também não apresse decisões complexas.

**Estrutura Guia o Comportamento**

Percebe as tags XML nos prompts? Elas não são decorativas. Modelos seguem instruções estruturadas de forma mais confiável que texto livre. Quando você precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a rastrear onde está e o que vem a seguir. O diagrama abaixo detalha um prompt bem estruturado, mostrando como tags como `<system>`, `<instructions>`, `<context>`, `<user-input>` e `<constraints>` organizam suas instruções em seções claras.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Estrutura do Prompt" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade Por Autoavaliação**

Os padrões auto-reflexivos funcionam tornando explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça certo", você define exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar sua própria saída e melhorar. Isso transforma geração de código de loteria em processo.

**O Contexto é Finito**

Conversas multi-turno funcionam incluindo o histórico das mensagens em cada requisição. Mas há limite — todo modelo tem contagem máxima de tokens. Conforme as conversas crescem, você precisará de estratégias para manter contexto relevante sem atingir esse teto. Este módulo mostra como a memória funciona; depois você aprenderá quando resumir, esquecer ou recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->