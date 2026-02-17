# Módulo 02: Engenharia de Prompts com GPT-5.2

## Índice

- [O que vai aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Compreender a Engenharia de Prompts](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompts](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Cadeia de Pensamento](../../../02-prompt-engineering)
  - [Prompting Baseado em Função](../../../02-prompt-engineering)
  - [Modelos de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Utilização dos Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Ecrã da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Completude](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrainada](../../../02-prompt-engineering)
- [O que está realmente a aprender](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que vai aprender

<img src="../../../translated_images/pt-PT/what-youll-learn.c68269ac048503b2.webp" alt="O que vai aprender" width="800"/>

No módulo anterior, viu como a memória facilita a IA conversacional e utilizou os Modelos do GitHub para interações básicas. Agora vamos focar em como faz perguntas — os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como estrutura os seus prompts afeta drasticamente a qualidade das respostas que recebe. Começamos com uma revisão das técnicas fundamentais de prompting, e depois avançamos para oito padrões avançados que aproveitam ao máximo as capacidades do GPT-5.2.

Vamos usar o GPT-5.2 porque este introduz controlo do raciocínio - pode indicar ao modelo quanto deve pensar antes de responder. Isto torna as estratégias de prompting mais evidentes e ajuda a perceber quando usar cada abordagem. Também beneficiamos dos limites de taxa mais flexíveis da Azure para o GPT-5.2, comparados com os Modelos GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Ficheiro `.env` na raiz com credenciais Azure (criado por `azd up` no Módulo 01)

> **Nota:** Se ainda não concluiu o Módulo 01, siga primeiro as instruções de implantação desse módulo.

## Compreender a Engenharia de Prompts

<img src="../../../translated_images/pt-PT/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O que é Engenharia de Prompts?" width="800"/>

A engenharia de prompts consiste em criar textos de entrada que garantam consistentemente os resultados que precisa. Não é apenas fazer perguntas — trata-se de estruturar pedidos para que o modelo entenda exatamente o que quer e como o deve entregar.

Pense nisso como dar instruções a um colega. "Corrige o erro" é vago. "Corrige a exceção de ponteiro nulo em UserService.java linha 45, adicionando uma verificação de null" é específico. Os modelos de linguagem funcionam da mesma forma — especificidade e estrutura são importantes.

<img src="../../../translated_images/pt-PT/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se enquadra" width="800"/>

O LangChain4j fornece a infraestrutura — ligações ao modelo, memória e tipos de mensagens — enquanto os padrões de prompt são apenas textos cuidadosamente estruturados que envia por essa infraestrutura. Os blocos de construção principais são `SystemMessage` (que define o comportamento e o papel da IA) e `UserMessage` (que transporta o seu pedido real).

## Fundamentos da Engenharia de Prompts

<img src="../../../translated_images/pt-PT/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão geral dos cinco padrões de engenharia de prompts" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos rever cinco técnicas fundamentais de prompting. Estes são os blocos de construção que todo engenheiro de prompts deve conhecer. Se já trabalhou no [módulo de arranque rápido](../00-quick-start/README.md#2-prompt-patterns), já os viu em ação – aqui está a estrutura conceptual por detrás deles.

### Prompting Zero-Shot

A abordagem mais simples: dar ao modelo uma instrução direta sem exemplos. O modelo baseia-se inteiramente no seu treino para compreender e executar a tarefa. Funciona bem para pedidos simples em que o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-PT/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções, ou qualquer tarefa que o modelo consiga fazer sem orientação adicional.

### Prompting Few-Shot

Forneça exemplos que mostrem o padrão que quer que o modelo siga. O modelo aprende o formato esperado entrada-saída a partir dos seus exemplos e aplica-o a novas entradas. Isto melhora drasticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-PT/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Aprender a partir de exemplos — o modelo identifica o padrão e aplica-o a novas entradas*

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio, ou quando os resultados zero-shot forem inconsistentes.

### Cadeia de Pensamento

Peça ao modelo para mostrar o seu raciocínio passo a passo. Em vez de responder diretamente, o modelo divide o problema e trabalha explicitamente cada parte. Isto melhora a precisão em matemática, lógica e tarefas de raciocínio multi-passo.

<img src="../../../translated_images/pt-PT/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting Cadeia de Pensamento" width="800"/>

*Raciocínio passo a passo — divisão de problemas complexos em etapas lógicas explícitas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// O modelo mostra: 15 - 8 = 7, depois 7 + 12 = 19 maçãs
```

**Quando usar:** Problemas matemáticos, puzzles lógicos, depuração, ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e confiança.

### Prompting Baseado em Função

Defina uma persona ou papel para a IA antes de fazer a pergunta. Isto fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "programador júnior" ou um "auditor de segurança".

<img src="../../../translated_images/pt-PT/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting Baseado em Função" width="800"/>

*Definir contexto e persona — a mesma pergunta recebe respostas diferentes conforme o papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análises específicas de domínio, ou quando precisar de respostas adaptadas a um determinado nível de especialização ou perspetiva.

### Modelos de Prompt

Crie prompts reutilizáveis com espaços para variáveis. Em vez de escrever um novo prompt toda a vez, defina um modelo uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j torna isso fácil com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-PT/prompt-templates.14bfc37d45f1a933.webp" alt="Modelos de Prompt" width="800"/>

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

**Quando usar:** Consultas repetidas com entradas diferentes, processamento em lote, criação de fluxos de trabalho AI reutilizáveis, ou qualquer cenário onde a estrutura do prompt mantém-se, mas os dados mudam.

---

Estes cinco fundamentos dão-lhe uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O resto deste módulo expande-os com **oito padrões avançados** que tiram proveito do controlo do raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, avancemos para os oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras de reflexão profunda. Algumas precisam de raciocínio visível, outras só dos resultados. Cada padrão abaixo está otimizado para um cenário diferente — e o controlo do raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-PT/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito padrões de prompting" width="800"/>

*Visão geral dos oito padrões de engenharia de prompts e seus casos de uso*

<img src="../../../translated_images/pt-PT/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controlo de Raciocínio com GPT-5.2" width="800"/>

*O controlo de raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — desde respostas rápidas até explorações profundas*

<img src="../../../translated_images/pt-PT/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparação de esforço de raciocínio" width="800"/>

*Baixa completude (rápido, direto) vs Alta completude (minucioso, exploratório) — abordagens de raciocínio*

**Baixa Completude (Rápido & Focado)** - Para perguntas simples onde quer respostas rápidas e diretas. O modelo faz raciocínio mínimo - máximo 2 passos. Use isto para cálculos, pesquisas ou perguntas diretas.

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

> 💡 **Explore com o GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:
> - "Qual é a diferença entre os padrões de prompting de baixa e alta completude?"
> - "Como é que as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão versus instruções diretas?"

**Alta Completude (Profundo & Minucioso)** - Para problemas complexos onde quer análise abrangente. O modelo explora completamente e mostra raciocínio detalhado. Use isto para design de sistemas, decisões arquitetónicas ou pesquisas complexas.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho multi-passo. O modelo fornece um plano antecipado, narra cada passo enquanto trabalha, depois dá um resumo. Use isto para migrações, implementações ou qualquer processo multi-passo.

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

O prompting Cadeia de Pensamento pede explicitamente ao modelo para mostrar o seu processo de raciocínio, melhorando a precisão para tarefas complexas. A divisão passo a passo ajuda tanto humanos como IA a entenderem a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como adaptaria o padrão de execução de tarefas para operações de longa duração?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e mostrar atualizações intermédias de progresso numa interface de utilizador?"

<img src="../../../translated_images/pt-PT/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão de Execução de Tarefa" width="800"/>

*Fluxo Planear → Executar → Resumir para tarefas multi-passo*

**Código Auto-Reflexivo** - Para gerar código com qualidade de produção. O modelo gera código que segue padrões de produção com gestão adequada de erros. Use isto ao criar novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-PT/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Autorreflexão" width="800"/>

*Loop iterativo de melhoria - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliações consistentes. O modelo revê código usando um quadro fixo (correcção, práticas, desempenho, segurança, manutenção). Use isto para revisões de código ou avaliação de qualidade.

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
> - "Como posso personalizar o quadro de análise para diferentes tipos de revisões de código?"
> - "Qual é a melhor maneira de interpretar e agir com base na saída estruturada programaticamente?"
> - "Como assegurar níveis de severidade consistentes entre diferentes sessões de análise?"

<img src="../../../translated_images/pt-PT/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão de Análise Estruturada" width="800"/>

*Quadro para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use isto para sessões interativas de ajuda ou Q&A complexos.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pt-PT/context-memory.dff30ad9fa78832a.webp" alt="Memória de Contexto" width="800"/>

*Como o contexto da conversa se acumula ao longo de múltiplos turnos até atingir o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que requerem lógica visível. O modelo mostra raciocínio explícito em cada passo. Use isto para problemas matemáticos, puzzles lógicos, ou quando precisa de entender o processo de pensamento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-PT/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Padrão Passo a Passo" width="800"/>

*Dividir problemas em passos lógicos explícitos*

**Saída Constrainada** - Para respostas com requisitos específicos de formato. O modelo segue estritamente regras de formato e comprimento. Use isto para resumos ou quando precisa de estrutura de saída precisa.

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

<img src="../../../translated_images/pt-PT/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Padrão de Saída Constrainada" width="800"/>

*Imposição de requisitos específicos de formato, comprimento e estrutura*

## Utilização dos Recursos Azure Existentes

**Verificar implantação:**

Assegure que o ficheiro `.env` existe na raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações com `./start-all.sh` no Módulo 01, este módulo já está a correr na porta 8083. Pode saltar os comandos de início abaixo e ir diretamente a http://localhost:8083.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).
A partir do Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar as aplicações com um único clique
- Ver os logs da aplicação em tempo real
- Monitorizar o estado das aplicações

Basta clicar no botão de play junto a "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

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

Ou iniciar apenas este módulo:

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

Ambos os scripts carregam automaticamente as variáveis de ambiente a partir do ficheiro `.env` na raiz e vão construir os JARs caso não existam.

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
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Capturas de Ecrã das Aplicações

<img src="../../../translated_images/pt-PT/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O dashboard principal mostrando todos os 8 padrões de prompt engineering com as suas características e casos de uso*

## Explorar os Padrões

A interface web permite experimentar diferentes estratégias de prompting. Cada padrão resolve problemas diferentes - experimente para ver quando cada abordagem se destaca.

### Baixa vs Alta Vontade

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Vontade. Vai obter uma resposta instantânea e direta. Agora faça uma pergunta complexa como "Desenvolve uma estratégia de cache para uma API de alto tráfego" usando Alta Vontade. Veja como o modelo desacelera e fornece um raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta - mas o prompt indica quanto pensamento deve fazer.

<img src="../../../translated_images/pt-PT/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-PT/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Estratégia de cache abrangente (2.8MB)*

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapas beneficiam de planeamento prévio e narração do progresso. O modelo descreve o que vai fazer, narra cada passo, e depois resume os resultados.

<img src="../../../translated_images/pt-PT/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Criar um endpoint REST com narração passo a passo (3.9MB)*

### Código Auto-Reflexivo

Experimente "Criar um serviço de validação de email". Em vez de apenas gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica fraquezas e melhora. Vai vê-lo iterar até que o código cumpra os padrões de produção.

<img src="../../../translated_images/pt-PT/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Serviço completo de validação de email (5.2MB)*

### Análise Estruturada

Revisões de código precisam de frameworks de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-PT/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revisão de código baseada em framework*

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e siga imediatamente com "Mostra-me um exemplo". O modelo lembra-se da sua primeira pergunta e dá-lhe um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria demasiado vaga.

<img src="../../../translated_images/pt-PT/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Preservação do contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema de matemática e experimente com Raciocínio Passo a Passo e Baixa Vontade. Baixa vontade dá-lhe só a resposta - rápido, mas opaco. Passo a passo mostra todos os cálculos e decisões.

<img src="../../../translated_images/pt-PT/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema de matemática com passos explícitos*

### Saída Constrangida

Quando precisa de formatos específicos ou contagens de palavras, este padrão impõe aderência rigorosa. Experimente gerar um resumo com exatamente 100 palavras em formato de tópicos.

<img src="../../../translated_images/pt-PT/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Resumo de machine learning com controlo de formato*

## O Que Está a Aprender Realmente

**O Esforço de Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo leva tempo a pensar profundamente. Está a aprender a adequar o esforço à complexidade da tarefa - não perca tempo com perguntas simples, mas também não se apresse em decisões complexas.

**A Estrutura Orienta o Comportamento**

Repara nas tags XML nos prompts? Não são decorativas. Os modelos seguem instruções estruturadas com mais fiabilidade do que texto livre. Quando precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-PT/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com secções claras e organização estilo XML*

**Qualidade Através de Autoavaliação**

Os padrões auto-reflexivos funcionam ao tornar explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça bem", diz-lhe exatamente o que "bem" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar a sua própria saída e melhorar. Isto transforma a geração de código de uma lotaria num processo.

**Contexto É Finito**

Conversas multi-turno funcionam por incluir o histórico das mensagens em cada pedido. Mas há um limite - cada modelo tem um número máximo de tokens. À medida que as conversas crescem, vai precisar de estratégias para manter contexto relevante sem atingir esse limite. Este módulo mostra-lhe como funciona a memória; depois vai aprender quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Reforço por Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Principal](../README.md) | [Seguinte: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, por favor esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->