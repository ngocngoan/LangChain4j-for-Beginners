# Module 02: Engenharia de Prompt com GPT-5.2

## Índice

- [Guia em Vídeo](../../../02-prompt-engineering)
- [O que Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Compreender a Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompt](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Cadeia de Pensamento](../../../02-prompt-engineering)
  - [Prompt Baseado em Papel](../../../02-prompt-engineering)
  - [Templates de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usar Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Ecrã da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Vontade](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramenta)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrainada](../../../02-prompt-engineering)
- [O que Está Realmente a Aprender](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## Guia em Vídeo

Veja esta sessão ao vivo que explica como começar com este módulo: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## O que Vai Aprender

<img src="../../../translated_images/pt-PT/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

No módulo anterior, viu como a memória permite IA conversacional e usou os Modelos GitHub para interações básicas. Agora, vamos focar na forma como faz perguntas — os próprios prompts — usando o GPT-5.2 da Azure OpenAI. A forma como estrutura os seus prompts afeta drasticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting e depois passamos a oito padrões avançados que tiram pleno partido das capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque introduz controlo de raciocínio — pode dizer ao modelo quanto pensar antes de responder. Isto torna diferentes estratégias de prompting mais evidentes e ajuda a compreender quando usar cada abordagem. Também beneficiamos dos limites de taxa mais baixos da Azure para o GPT-5.2 comparados aos Modelos GitHub.

## Pré-requisitos

- Conclusão do Módulo 01 (recursos Azure OpenAI implementados)
- Ficheiro `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se ainda não concluiu o Módulo 01, siga primeiro as instruções de implementação lá.

## Compreender a Engenharia de Prompt

<img src="../../../translated_images/pt-PT/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

A engenharia de prompt trata-se de desenhar texto de entrada que consistentemente lhe dá os resultados de que precisa. Não é só fazer perguntas — é estruturar pedidos para que o modelo entenda exatamente o que quer e como entregar.

Pense nisso como dar instruções a um colega. "Corrige o bug" é vago. "Corrige a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação nula" é específico. Os modelos de linguagem funcionam da mesma forma — especificidade e estrutura são importantes.

<img src="../../../translated_images/pt-PT/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

O LangChain4j fornece a infraestrutura — conexões de modelo, memória e tipos de mensagens — enquanto os padrões de prompt são simplesmente texto cuidadosamente estruturado que envia através dessa infraestrutura. Os blocos de construção principais são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que transporta o seu pedido real).

## Fundamentos da Engenharia de Prompt

<img src="../../../translated_images/pt-PT/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. Estes são os blocos de construção que todo engenheiro de prompt deve conhecer. Se já trabalhou no [módulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), já viu estes em ação — aqui está o quadro conceptual por trás deles.

### Prompt Zero-Shot

A abordagem mais simples: dar ao modelo uma instrução direta sem exemplos. O modelo confia inteiramente no seu treino para entender e executar a tarefa. Funciona bem para pedidos simples onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-PT/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrução direta sem exemplos — o modelo deduz a tarefa apenas pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções, ou qualquer tarefa que o modelo consiga gerir sem orientação adicional.

### Prompt Few-Shot

Forneça exemplos que demonstrem o padrão que quer que o modelo siga. O modelo aprende o formato esperado de entrada-saída a partir dos seus exemplos e aplica-o a novas entradas. Isto melhora drasticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-PT/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*Aprender através de exemplos — o modelo identifica o padrão e aplica-o a novas entradas*

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio, ou quando os resultados zero-shot são inconsistentes.

### Cadeia de Pensamento

Peça ao modelo para mostrar o seu raciocínio passo a passo. Em vez de ir direto à resposta, o modelo divide o problema e trabalha explicitamente cada parte. Isto melhora a precisão em tarefas de matemática, lógica e raciocínio multi-etapa.

<img src="../../../translated_images/pt-PT/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*Raciocínio passo a passo — dividir problemas complexos em passos lógicos explícitos*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// O modelo mostra: 15 - 8 = 7, depois 7 + 12 = 19 maçãs
```

**Quando usar:** Problemas matemáticos, puzzles lógicos, debugging, ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e confiança.

### Prompt Baseado em Papel

Defina uma persona ou papel para a IA antes de fazer a sua pergunta. Isto fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou um "auditor de segurança".

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

**Quando usar:** Revisões de código, tutoria, análise específica de domínio, ou quando precisa de respostas ajustadas a um determinado nível de especialização ou perspetiva.

### Templates de Prompt

Crie prompts reutilizáveis com espaços reservados para variáveis. Em vez de escrever um prompt novo cada vez, defina um template uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isto com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-PT/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Prompts reutilizáveis com espaços para variáveis — um template, muitos usos*

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

**Quando usar:** Consultas repetidas com diferentes entradas, processamento em lote, construção de fluxos de trabalho de IA reutilizáveis, ou qualquer cenário onde a estrutura do prompt mantém-se igual mas os dados mudam.

---

Estes cinco fundamentos dão-lhe uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O resto deste módulo baseia-se neles com **oito padrões avançados** que aproveitam o controlo de raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos passar aos oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras precisam de pensamento profundo. Algumas precisam de raciocínio visível, outras só resultados. Cada padrão abaixo está otimizado para um cenário diferente — e o controlo de raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-PT/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e os seus casos de uso*

<img src="../../../translated_images/pt-PT/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*O controlo de raciocínio do GPT-5.2 permite especificar quanto o modelo deve pensar — desde respostas rápidas diretas até exploração profunda*

**Baixa Vontade (Rápido & Focado)** - Para perguntas simples onde quer respostas rápidas e diretas. O modelo faz raciocínio mínimo - máximo 2 passos. Use isto para cálculos, pesquisas, ou perguntas diretas.

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
> - "Qual é a diferença entre os padrões de prompting de baixa vontade e alta vontade?"
> - "Como é que as etiquetas XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão vs instruções diretas?"

**Alta Vontade (Profundo & Cuidadoso)** - Para problemas complexos onde quer análise abrangente. O modelo explora a fundo e mostra raciocínio detalhado. Use isto para design de sistemas, decisões de arquitetura, ou pesquisa complexa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho com múltiplas etapas. O modelo fornece um plano inicial, narra cada passo enquanto executa, depois dá um resumo. Use isto para migrações, implementações, ou qualquer processo multi-etapa.

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

O prompting com Cadeia de Pensamento pede explicitamente ao modelo para mostrar o processo de raciocínio, melhorando a precisão para tarefas complexas. A divisão passo a passo ajuda tanto humanos como IA a entender a lógica.

> **🤖 Tente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como posso adaptar o padrão de execução de tarefas para operações longas?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e mostrar atualizações intermediárias de progresso numa interface?"

<img src="../../../translated_images/pt-PT/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Plano → Executar → Resumir fluxo para tarefas multi-etapa*

**Código Auto-Reflexivo** - Para gerar código de qualidade de produção. O modelo gera código seguindo padrões de produção com tratamento adequado de erros. Use isto quando construir novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-PT/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Ciclo iterativo de melhoria - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliação consistente. O modelo revê o código usando um framework fixo (correção, práticas, desempenho, segurança, manutenção). Use isto para revisões de código ou avaliações de qualidade.

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

> **🤖 Tente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre análise estruturada:
> - "Como personalizar o framework de análise para diferentes tipos de revisões de código?"
> - "Qual é a melhor forma de analisar e atuar na saída estruturada programaticamente?"
> - "Como garantir níveis consistentes de severidade em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-PT/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Framework para revisões de código consistentes com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói a partir delas. Use isto para sessões de ajuda interativas ou Q&A complexas.

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

*Como o contexto da conversa acumula ao longo de múltiplos turnos até atingir o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que requerem lógica visível. O modelo mostra raciocínio explícito para cada passo. Use isto para problemas matemáticos, puzzles lógicos, ou quando precisa entender o processo de pensamento.

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

*Dividir problemas em passos lógicos explícitos*

**Saída Constrainada** - Para respostas com requisitos específicos de formato. O modelo segue estritamente as regras de formato e tamanho. Use isto para sumários ou quando precisar de estrutura de saída precisa.

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

*Impor formato, comprimento e requisitos de estrutura específicos*

## Usar Recursos Azure Existentes

**Verificar implementação:**

Assegure-se que o ficheiro `.env` existe no diretório raiz com credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se já iniciou todas as aplicações com `./start-all.sh` do Módulo 01, este módulo já está a funcionar na porta 8083. Pode ignorar os comandos de arranque abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**
O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades no lado esquerdo do VS Code (procure o ícone do Spring Boot).

A partir do Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um único clique
- Ver os registos da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de play ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma só vez.

<img src="../../../translated_images/pt-PT/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: A usar scripts shell**

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

Ambos os scripts carregam automaticamente variáveis de ambiente do ficheiro `.env` raiz e irão construir os JARs se ainda não existirem.

> **Nota:** Se preferir construir todos os módulos manualmente antes de iniciar:
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
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Capturas de Ecrã da Aplicação

<img src="../../../translated_images/pt-PT/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O dashboard principal mostrando todos os 8 padrões de prompt engineering com as suas características e casos de uso*

## Explorar os Padrões

A interface web permite-lhe experimentar diferentes estratégias de prompting. Cada padrão resolve diferentes problemas - experimente para ver quando cada abordagem se destaca.

> **Nota: Streaming vs Não Streaming** — Cada página de padrão oferece dois botões: **🔴 Stream Response (Live)** e uma opção **Não streaming**. O streaming usa Server-Sent Events (SSE) para mostrar tokens em tempo real conforme o modelo os gera, para que veja o progresso imediatamente. A opção não streaming espera pela resposta completa antes de a mostrar. Para prompts que desencadeiam raciocínio profundo (ex.: High Eagerness, Self-Reflecting Code), a chamada não streaming pode demorar muito tempo — por vezes minutos — sem feedback visível. **Use streaming ao experimentar prompts complexos** para poder ver o modelo a trabalhar e evitar a impressão de que o pedido expirou.
>
> **Nota: Requisito do Navegador** — A funcionalidade de streaming utiliza a Fetch Streams API (`response.body.getReader()`) que requer um navegador completo (Chrome, Edge, Firefox, Safari). **Não** funciona no Simple Browser incorporado do VS Code, pois o seu webview não suporta a API ReadableStream. Se usar o Simple Browser, os botões não streaming funcionarão normalmente — apenas os botões de streaming são afetados. Abra `http://localhost:8083` num browser externo para a experiência completa.

### Low vs High Eagerness

Faça uma pergunta simples como “Qual é 15% de 200?” usando Low Eagerness. Vai obter uma resposta direta e instantânea. Agora faça algo complexo como “Desenha uma estratégia de caching para uma API com muito tráfego” usando High Eagerness. Clique em **🔴 Stream Response (Live)** e veja o raciocínio detalhado do modelo aparecer token a token. Mesmo modelo, mesma estrutura de pergunta — mas o prompt indica-lhe o quanto deve pensar.

### Execução de Tarefas (Preambuleiros de Ferramentas)

Fluxos de trabalho multi-etapa beneficiam de planeamento antecipado e narração do progresso. O modelo descreve o que vai fazer, narra cada passo, depois resume os resultados.

### Código Auto-Refletido

Experimente “Criar um serviço de validação de email”. Em vez de gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica fraquezas e melhora. Vai ver como itera até o código estar ao nível de produção.

### Análise Estruturada

Revisões de código precisam de frameworks de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de gravidade.

### Chat Multi-Turno

Pergunte “O que é o Spring Boot?” e logo a seguir “Mostra-me um exemplo”. O modelo lembra-se da primeira pergunta e dá-lhe um exemplo específico de Spring Boot. Sem memória, essa segunda pergunta seria demasiado vaga.

### Raciocínio Passo a Passo

Escolha um problema de matemática e experimente com Raciocínio Passo a Passo e Low Eagerness. Low eagerness dá-lhe só a resposta — rápido mas opaco. Passo a passo mostra-lhe todos os cálculos e decisões.

### Saída Restringida

Quando precisa de formatos específicos ou contagens de palavras, este padrão impõe uma aderência rigorosa. Experimente gerar um resumo com exatamente 100 palavras em formato de pontos.

## O Que Realmente Está a Aprender

**O Esforço de Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Esforço baixo significa respostas rápidas com exploração mínima. Esforço alto significa que o modelo leva tempo a pensar profundamente. Está a aprender a ajustar o esforço à complexidade da tarefa — não perca tempo com perguntas simples, mas também não se apresse em decisões complexas.

**A Estrutura Guia o Comportamento**

Repara nas tags XML nos prompts? Não são decorativas. Os modelos seguem instruções estruturadas de forma mais fiável que texto livre. Quando precisa de processos multi-etapa ou lógica complexa, a estrutura ajuda o modelo a saber onde está e o que vem a seguir.

<img src="../../../translated_images/pt-PT/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões auto-refletidos funcionam tornando os critérios de qualidade explícitos. Em vez de esperar que o modelo "faça bem", diz-lhe exatamente o que significa "certo": lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar a própria saída e melhorar. Isto transforma a geração de código numa tarefa sistemática.

**O Contexto é Finito**

Conversas multi-turno funcionam incluindo o historial de mensagens em cada pedido. Mas há um limite — cada modelo tem um número máximo de tokens. À medida que as conversas crescem, precisará de estratégias para manter o contexto relevante sem ultrapassar esse limite. Este módulo mostra-lhe como funciona a memória; mais tarde aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor tenha em conta que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->