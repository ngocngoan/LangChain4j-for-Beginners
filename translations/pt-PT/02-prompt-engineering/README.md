# Módulo 02: Engenharia de Prompts com GPT-5.2

## Índice

- [O que Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Compreender a Engenharia de Prompts](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompts](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Cadeia de Pensamento](../../../02-prompt-engineering)
  - [Prompt com Base em Papel](../../../02-prompt-engineering)
  - [Modelos de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usar Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorar os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefa (Preambules de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Restrita](../../../02-prompt-engineering)
- [O que Está Realmente a Aprender](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que Vai Aprender

<img src="../../../translated_images/pt-PT/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

No módulo anterior, viu como a memória permite IA conversacional e usou Modelos GitHub para interações básicas. Agora vamos focar em como faz as perguntas — ou seja, os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como estrutura os seus prompts afeta dramaticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting, depois passamos a oito padrões avançados que tiram total partido das capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque este introduz controlo do raciocínio — pode dizer ao modelo quanto deve pensar antes de responder. Isso torna estratégias diferentes de prompting mais evidentes e ajuda a perceber quando usar cada abordagem. Também beneficiamos dos limites de taxa mais permissivos do Azure para o GPT-5.2 em comparação com os Modelos GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implementados)
- Ficheiro `.env` na diretoria raiz com as credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se ainda não completou o Módulo 01, siga primeiro as instruções de implementação aí.

## Compreender a Engenharia de Prompts

<img src="../../../translated_images/pt-PT/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Engenharia de prompts é sobre desenhar texto de entrada que consistentemente lhe dá os resultados que precisa. Não é apenas fazer perguntas — é estruturar pedidos para que o modelo compreenda exatamente o que quer e como entregar.

Pense nisso como dar instruções a um colega. "Corrige o erro" é vago. "Corrige a exceção null pointer em UserService.java linha 45 adicionando uma verificação nula" é específico. Os modelos linguísticos funcionam da mesma forma — especificidade e estrutura são importantes.

<img src="../../../translated_images/pt-PT/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j fornece a infraestrutura — ligações a modelos, memória, e tipos de mensagem — enquanto padrões de prompt são apenas texto cuidadosamente estruturado que envia através dessa infraestrutura. Os blocos fundamentais são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que carrega o seu pedido real).

## Fundamentos da Engenharia de Prompts

<img src="../../../translated_images/pt-PT/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos rever cinco técnicas de prompting fundamentais. Estes são os blocos de construção que todo engenheiro de prompts deve conhecer. Se já trabalhou no [módulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), já viu estes em ação — aqui está o quadro conceptual por detrás deles.

### Prompt Zero-Shot

A abordagem mais simples: dá ao modelo uma instrução direta sem exemplos. O modelo baseia-se totalmente no seu treino para entender e executar a tarefa. Funciona bem para pedidos simples onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-PT/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções, ou qualquer tarefa que o modelo consiga sem orientação adicional.

### Prompt Few-Shot

Forneça exemplos que demonstrem o padrão que quer que o modelo siga. O modelo aprende a estrutura input-output esperada a partir dos seus exemplos e aplica isso a novos inputs. Isto melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-PT/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Aprender com exemplos — o modelo identifica o padrão e aplica-o a novos inputs*

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio, ou quando resultados zero-shot são inconsistentes.

### Cadeia de Pensamento

Peça ao modelo para mostrar o seu raciocínio passo a passo. Em vez de saltar diretamente para uma resposta, o modelo divide o problema e trabalha cada parte explicitamente. Isto melhora a precisão em matemática, lógica e tarefas de raciocínio multi-etapas.

<img src="../../../translated_images/pt-PT/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Raciocínio passo a passo — dividir problemas complexos em etapas lógicas explícitas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// O modelo mostra: 15 - 8 = 7, depois 7 + 12 = 19 maçãs
```

**Quando usar:** Problemas matemáticos, puzzles de lógica, debugging, ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e confiança.

### Prompt com Base em Papel

Defina uma persona ou papel para a IA antes de colocar a sua pergunta. Isto fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou um "auditor de segurança".

<img src="../../../translated_images/pt-PT/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Definir contexto e persona — a mesma pergunta obtém resposta diferente dependendo do papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análises específicas de domínio, ou quando precisa de respostas adaptadas a determinado nível de especialização ou perspetiva.

### Modelos de Prompt

Crie prompts reutilizáveis com espaços variáveis. Em vez de escrever um novo prompt toda vez, defina um template uma vez e preencha com valores diferentes. A classe `PromptTemplate` da LangChain4j torna isto fácil com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-PT/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompts reutilizáveis com espaços variáveis — um template, muitos usos*

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

**Quando usar:** Consultas repetidas com diferentes inputs, processamento em lote, construção de fluxos de trabalho AI reutilizáveis, ou qualquer cenário onde a estrutura do prompt se mantém mas os dados mudam.

---

Estes cinco fundamentos dão-lhe uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O resto deste módulo baseia-se neles com **oito padrões avançados** que aproveitam o controlo do raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos passar aos oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras precisam de reflexão profunda. Algumas precisam de raciocínio visível, outras só de resultados. Cada padrão abaixo é otimizado para um cenário diferente — e o controlo do raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-PT/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Visão geral dos oito padrões de engenharia de prompts e os seus casos de uso*

<img src="../../../translated_images/pt-PT/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*O controlo do raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — desde respostas rápidas e diretas a explorações profundas*

**Baixa Disposição (Rápido & Focado)** - Para perguntas simples onde quer respostas rápidas e diretas. O modelo faz raciocínio mínimo — máximo 2 passos. Use para cálculos, pesquisas ou questões diretas.

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
> - "Qual a diferença entre padrões de prompting de baixa disposição e alta disposição?"
> - "Como é que as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de auto-reflexão vs instrução direta?"

**Alta Disposição (Profundo & Minucioso)** - Para problemas complexos onde quer análise abrangente. O modelo explora a fundo e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura, ou pesquisa complexa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefa (Progresso Passo a Passo)** - Para fluxos de trabalho multi-etapas. O modelo fornece um plano antecipado, narra cada passo enquanto trabalha, e depois dá um resumo. Use para migrações, implementações, ou qualquer processo de múltiplas etapas.

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

O prompting Chain-of-Thought pede explicitamente ao modelo para mostrar o seu processo de raciocínio, melhorando a precisão para tarefas complexas. A decomposição passo a passo ajuda humanos e a IA a entender a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como adaptaria o padrão de execução de tarefa para operações de longa duração?"
> - "Quais as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como capturar e mostrar atualizações de progresso intermédio numa interface?"

<img src="../../../translated_images/pt-PT/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Fluxo de trabalho Planejar → Executar → Resumir para tarefas multi-etapas*

**Código Auto-Reflexivo** - Para gerar código de qualidade de produção. O modelo gera código segundo padrões de produção com tratamento correto de erros. Use ao construir novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-PT/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

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
> - "Como posso personalizar o framework de análise para diferentes tipos de revisões de código?"
> - "Qual a melhor forma de analisar e agir com base em saída estruturada programaticamente?"
> - "Como garantir níveis consistentes de severidade em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-PT/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Estrutura para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões interativas de ajuda ou Q&A complexos.

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

*Como o contexto de conversação se acumula ao longo de múltiplos turnos até atingir o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que precisam de lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas matemáticos, puzzles de lógica, ou quando precisa de entender o processo de pensamento.

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

*Dividir problemas em etapas lógicas explícitas*

**Saída Restrita** - Para respostas com requisitos específicos de formato. O modelo segue rigorosamente regras de formato e comprimento. Use para sumários ou quando precisa de estrutura precisa de saída.

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

*Enforcing requisitos específicos de formato, comprimento e estrutura*

## Usar Recursos Azure Existentes

**Verificar a implementação:**

Garanta que o ficheiro `.env` existe na diretoria raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está a correr na porta 8083. Pode ignorar os comandos de arranque abaixo e ir diretamente a http://localhost:8083.

**Opção 1: Usar a Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na barra de atividades do lado esquerdo do VS Code (procure o ícone do Spring Boot).

A partir da Spring Boot Dashboard pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Ver registos da aplicação em tempo real
- Monitorizar o estado da aplicação
Basta clicar no botão de reprodução junto a "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos ao mesmo tempo.

<img src="../../../translated_images/pt-PT/dashboard.da2c2130c904aaf0.webp" alt="Painel Spring Boot" width="400"/>

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

Ambos os scripts carregam automaticamente variáveis de ambiente do ficheiro `.env` da raiz e irão construir os JARs se eles não existirem.

> **Nota:** Se preferir construir manualmente todos os módulos antes de iniciar:
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

## Capturas de Ecrã da Aplicação

<img src="../../../translated_images/pt-PT/dashboard-home.5444dbda4bc1f79d.webp" alt="Painel Principal" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O painel principal a mostrar todos os 8 padrões de engenharia de prompt com as suas características e casos de uso*

## Explorando os Padrões

A interface web permite experimentar diferentes estratégias de prompting. Cada padrão resolve problemas diferentes - experimente para ver quando cada abordagem se destaca.

> **Nota: Streaming vs Não-Streaming** — Cada página de padrão oferece dois botões: **🔴 Resposta por Streaming (Em direto)** e uma opção **Não-Streaming**. O streaming usa Server-Sent Events (SSE) para mostrar os tokens em tempo real à medida que o modelo os gera, assim vê o progresso imediatamente. A opção não-streaming espera pela resposta completa antes de a mostrar. Para prompts que desencadeiam raciocínio profundo (ex. Alta Vontade, Código Auto-Refletivo), a chamada não-streaming pode demorar muito tempo — por vezes minutos — sem feedback visível. **Use streaming ao experimentar prompts complexos** para poder ver o modelo a funcionar e evitar a impressão de que o pedido expirou.
>
> **Nota: Requisito do Navegador** — A funcionalidade de streaming usa a API Fetch Streams (`response.body.getReader()`) que requer um navegador completo (Chrome, Edge, Firefox, Safari). Não funciona no Simple Browser incorporado do VS Code, pois a sua webview não suporta a API ReadableStream. Se usar o Simple Browser, os botões não-streaming continuam a funcionar normalmente — apenas os botões de streaming são afetados. Abra `http://localhost:8083` num navegador externo para a experiência completa.

### Baixa vs Alta Vontade

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Vontade. Vai obter uma resposta direta e instantânea. Agora faça algo complexo como "Desenhe uma estratégia de caching para uma API de alto tráfego" usando Alta Vontade. Clique em **🔴 Resposta por Streaming (Em direto)** e veja o raciocínio detalhado do modelo aparecer token a token. Mesmo modelo, mesma estrutura de pergunta - mas o prompt diz-lhe quanto pensar.

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapa beneficiam de planeamento prévio e narração do progresso. O modelo descreve o que vai fazer, narra cada passo, depois resume os resultados.

### Código Auto-Refletivo

Experimente "Criar um serviço de validação de email". Em vez de apenas gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica fragilidades e melhora. Vai vê-lo iterar até o código corresponder aos padrões de produção.

### Análise Estruturada

Revisões de código precisam de quadros de avaliação consistentes. O modelo analisa código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

### Conversa Multi-Turno

Pergunte "O que é Spring Boot?" e imediatamente siga com "Mostra-me um exemplo". O modelo lembra-se da primeira pergunta e dá-lhe um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria demasiado vaga.

### Raciocínio Passo a Passo

Escolha um problema de matemática e experimente com Raciocínio Passo a Passo e Baixa Vontade. A baixa vontade dá-lhe só a resposta - rápido, mas opaco. O passo a passo mostra todos os cálculos e decisões.

### Saída Constrangida

Quando precisa de formatos ou contagem de palavras específicas, este padrão impõe uma aderência rigorosa. Tente gerar um resumo com exatamente 100 palavras em formato de pontos.

## O Que Está Realmente a Aprender

**O Esforço de Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo demora a pensar profundamente. Está a aprender a ajustar o esforço à complexidade da tarefa - não perca tempo com perguntas simples, mas também não se precipite em decisões complexas.

**A Estrutura Guia o Comportamento**

Reparou nas tags XML nos prompts? Não são decorativas. Os modelos seguem instruções estruturadas de forma mais fiável do que texto livre. Quando precisa de processos multi-etapa ou lógica complexa, a estrutura ajuda o modelo a saber onde está e o que vem a seguir.

<img src="../../../translated_images/pt-PT/prompt-structure.a77763d63f4e2f89.webp" alt="Estrutura do Prompt" width="800"/>

*Anatomia de um prompt bem estruturado com secções claras e organização ao estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões auto-refletivos funcionam ao tornar explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça bem", diz-lhe exatamente o que "bem" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar a sua própria saída e melhorar. Isto transforma a geração de código de uma lotaria num processo.

**Contexto É Finito**

Conversas multi-turno funcionam ao incluir o histórico de mensagens em cada pedido. Mas há um limite - cada modelo tem um máximo de tokens. À medida que as conversas crescem, vai precisar de estratégias para manter o contexto relevante sem atingir esse limite. Este módulo mostra como a memória funciona; mais tarde vai aprender quando resumir, quando esquecer, e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Recuperação Aumentada)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, por favor note que traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte autorizada. Para informação crítica, recomenda-se tradução profissional por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->