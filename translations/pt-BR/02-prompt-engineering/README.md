# Módulo 02: Engenharia de Prompt com GPT-5.2

## Sumário

- [O que Você Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompt](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Corrente de Pensamento](../../../02-prompt-engineering)
  - [Prompting Baseado em Papel](../../../02-prompt-engineering)
  - [Modelos de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usando Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preambulações de Ferramentas)](../../../02-prompt-engineering)
  - [Código Autorreflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrainada](../../../02-prompt-engineering)
- [O que Você Está Realmente Aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que Você Vai Aprender

<img src="../../../translated_images/pt-BR/what-youll-learn.c68269ac048503b2.webp" alt="O que Você Vai Aprender" width="800"/>

No módulo anterior, você viu como a memória permite IA conversacional e usou Modelos GitHub para interações básicas. Agora focaremos em como fazer perguntas — os próprios prompts — usando o GPT-5.2 da Azure OpenAI. A maneira como você estrutura seus prompts afeta dramaticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting, depois avançamos para oito padrões avançados que aproveitam ao máximo as capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque ele introduz controle de raciocínio - você pode dizer ao modelo quanto ele deve pensar antes de responder. Isso torna as diferentes estratégias de prompting mais evidentes e ajuda você a entender quando usar cada abordagem. Também aproveitaremos os menores limites de taxa do Azure para o GPT-5.2 comparado aos Modelos GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se ainda não concluiu o Módulo 01, siga primeiro as instruções de implantação lá.

## Entendendo Engenharia de Prompt

<img src="../../../translated_images/pt-BR/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O que é Engenharia de Prompt?" width="800"/>

Engenharia de prompt consiste em projetar texto de entrada que consistentemente o leve aos resultados desejados. Não é apenas sobre fazer perguntas – é sobre estruturar solicitações para que o modelo entenda exatamente o que você quer e como fornecer isso.

Pense nisso como dar instruções a um colega. "Conserte o bug" é vago. "Conserte a exceção de ponteiro nulo no UserService.java linha 45 adicionando uma verificação de nulo" é específico. Modelos de linguagem funcionam da mesma maneira – especificidade e estrutura importam.

<img src="../../../translated_images/pt-BR/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se Encaixa" width="800"/>

O LangChain4j fornece a infraestrutura — conexões de modelo, memória e tipos de mensagem — enquanto padrões de prompt são apenas texto estruturado cuidadosamente que você envia por essa infraestrutura. Os blocos de construção principais são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que carrega sua solicitação real).

## Fundamentos da Engenharia de Prompt

<img src="../../../translated_images/pt-BR/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão Geral dos Cinco Padrões de Engenharia de Prompt" width="800"/>

Antes de mergulharmos nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. Estes são os blocos básicos que todo engenheiro de prompt deve conhecer. Se você já trabalhou no [módulo de Início Rápido](../00-quick-start/README.md#2-prompt-patterns), já viu esses em ação — aqui está a estrutura conceitual por trás deles.

### Prompting Zero-Shot

A abordagem mais simples: dê ao modelo uma instrução direta sem exemplos. O modelo se baseia inteiramente em seu treinamento para entender e executar a tarefa. Isso funciona bem para solicitações diretas onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-BR/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções ou qualquer tarefa que o modelo possa lidar sem orientação adicional.

### Prompting Few-Shot

Forneça exemplos que demonstrem o padrão que você deseja que o modelo siga. O modelo aprende o formato esperado de entrada-saída a partir dos seus exemplos e o aplica a novas entradas. Isso melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-BR/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Aprendendo a partir de exemplos — o modelo identifica o padrão e o aplica a novas entradas*

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio ou quando resultados zero-shot são inconsistentes.

### Corrente de Pensamento

Peça ao modelo para mostrar seu raciocínio passo a passo. Em vez de pular direto para uma resposta, o modelo detalha o problema e trabalha cada parte explicitamente. Isso melhora a precisão em matemática, lógica e raciocínio com múltiplas etapas.

<img src="../../../translated_images/pt-BR/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting Corrente de Pensamento" width="800"/>

*Raciocínio passo a passo — quebrando problemas complexos em etapas lógicas explícitas*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// O modelo mostra: 15 - 8 = 7, depois 7 + 12 = 19 maçãs
```

**Quando usar:** Problemas matemáticos, quebra-cabeças lógicos, depuração ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e confiança.

### Prompting Baseado em Papel

Defina uma persona ou papel para a IA antes de fazer sua pergunta. Isso fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou um "auditor de segurança".

<img src="../../../translated_images/pt-BR/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting Baseado em Papel" width="800"/>

*Definindo contexto e persona — a mesma pergunta recebe uma resposta diferente dependendo do papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análise específica de domínio ou quando você precisa de respostas adaptadas a um nível de especialização ou perspectiva particular.

### Modelos de Prompt

Crie prompts reutilizáveis com espaços variáveis. Em vez de escrever um novo prompt toda vez, defina um modelo uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isso com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-BR/prompt-templates.14bfc37d45f1a933.webp" alt="Modelos de Prompt" width="800"/>

*Prompts reutilizáveis com espaços variáveis — um modelo, muitos usos*

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

**Quando usar:** Consultas repetidas com diferentes entradas, processamento em lotes, construção de fluxos reutilizáveis de IA ou qualquer cenário onde a estrutura do prompt permanece a mesma mas os dados mudam.

---

Esses cinco fundamentos fornecem uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O restante deste módulo se baseia neles com **oito padrões avançados** que aproveitam o controle de raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos aos oito padrões avançados que tornam este módulo único. Nem todos os problemas necessitam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras de pensamento profundo. Algumas precisam de raciocínio visível, outras só de resultados. Cada padrão abaixo é otimizado para um cenário diferente — e o controle de raciocínio do GPT-5.2 torna as diferenças ainda mais marcantes.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito Padrões de Prompting" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e seus casos de uso*

<img src="../../../translated_images/pt-BR/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controle de Raciocínio com GPT-5.2" width="800"/>

*O controle de raciocínio do GPT-5.2 permite especificar quanto o modelo deve pensar — de respostas rápidas e diretas a explorações profundas*

<img src="../../../translated_images/pt-BR/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparação do Esforço de Raciocínio" width="800"/>

*Disposição baixa (rápido, direto) vs disposição alta (minucioso, exploratório) em abordagens de raciocínio*

**Disposição Baixa (Rápido & Focado)** - Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo - no máximo 2 passos. Use isso para cálculos, consultas ou perguntas diretas.

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
> - "Qual a diferença entre os padrões de prompting de baixa disposição e alta disposição?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão vs instrução direta?"

**Disposição Alta (Profundo & Minucioso)** - Para problemas complexos onde você quer análise abrangente. O modelo explora minuciosamente e mostra raciocínio detalhado. Use para design de sistemas, decisões arquiteturais ou pesquisa complexa.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho com múltiplas etapas. O modelo fornece um plano inicial, narra cada passo enquanto trabalha e depois dá um resumo. Use para migrações, implementações ou qualquer processo multi-etapas.

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

Prompting de Corrente de Pensamento pede explicitamente ao modelo que mostre seu processo de raciocínio, melhorando a precisão para tarefas complexas. A decomposição passo a passo ajuda tanto humanos quanto IA a entender a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como eu adaptaria o padrão de execução de tarefa para operações de longa duração?"
> - "Quais são as melhores práticas para estruturar preambulaçõs de ferramentas em aplicações de produção?"
> - "Como posso capturar e exibir atualizações de progresso intermediárias na interface?"

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão de Execução de Tarefa" width="800"/>

*Fluxo de trabalho Planejar → Executar → Resumir para tarefas multi-etapas*

**Código Autorreflexivo** - Para gerar código com qualidade de produção. O modelo gera código seguindo padrões de produção com tratamento adequado de erros. Use ao construir novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Autorreflexão" width="800"/>

*Loop de melhoria iterativo - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliação consistente. O modelo revisa código usando uma estrutura fixa (correção, práticas, desempenho, segurança, manutenibilidade). Use para revisões de código ou avaliações de qualidade.

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
> - "Como personalizar a estrutura de análise para diferentes tipos de revisões de código?"
> - "Qual a melhor forma de analisar e atuar sobre saída estruturada programaticamente?"
> - "Como garantir níveis consistentes de severidade em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão de Análise Estruturada" width="800"/>

*Estrutura para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões de ajuda interativas ou perguntas e respostas complexas.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pt-BR/context-memory.dff30ad9fa78832a.webp" alt="Memória de Contexto" width="800"/>

*Como o contexto da conversa se acumula ao longo de múltiplas interações até atingir o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que exigem lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas matemáticos, quebra-cabeças lógicos ou quando você precisa entender o processo de pensamento.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-BR/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Padrão Passo a Passo" width="800"/>

*Quebrando problemas em passos lógicos explícitos*

**Saída Constrainada** - Para respostas com requisitos específicos de formato. O modelo segue rigorosamente regras de formato e tamanho. Use para resumos ou quando você precisa de estrutura precisa na saída.

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

<img src="../../../translated_images/pt-BR/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Padrão de Saída Constrainada" width="800"/>

*Aplicando requisitos específicos de formato, tamanho e estrutura*

## Usando Recursos Azure Existentes

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` exista no diretório raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8083. Você pode pular os comandos de inicialização abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades ao lado esquerdo do VS Code (procure pelo ícone do Spring Boot).
A partir do Spring Boot Dashboard, você pode:
- Ver todos os aplicativos Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicativos com um único clique
- Visualizar logs do aplicativo em tempo real
- Monitorar o status do aplicativo

Basta clicar no botão de reprodução ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usando scripts shell**

Inicie todos os aplicativos web (módulos 01-04):

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e irão construir os JARs se eles não existirem.

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

Abra http://localhost:8083 em seu navegador.

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

## Capturas de Tela do Aplicativo

<img src="../../../translated_images/pt-BR/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O painel principal mostrando todos os 8 padrões de engenharia de prompts com suas características e casos de uso*

## Explorando os Padrões

A interface web permite que você experimente diferentes estratégias de prompting. Cada padrão resolve problemas distintos - experimente para ver quando cada abordagem se destaca.

### Baixa vs Alta Disposição

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Disposição. Você receberá uma resposta direta e instantânea. Agora faça algo complexo como "Desenhe uma estratégia de cache para uma API de alto tráfego" usando Alta Disposição. Observe como o modelo desacelera e fornece um raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta - mas o prompt indica quanto pensamento deve ser aplicado.

<img src="../../../translated_images/pt-BR/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-BR/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Estratégia abrangente de cache (2.8MB)*

### Execução de Tarefas (Preambulos de Ferramentas)

Workflows de múltiplas etapas se beneficiam de planejamento inicial e narração de progresso. O modelo esboça o que fará, narra cada passo e depois resume os resultados.

<img src="../../../translated_images/pt-BR/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Criando um endpoint REST com narração passo a passo (3.9MB)*

### Código Auto-Reflexivo

Tente "Criar um serviço de validação de email". Em vez de apenas gerar código e parar, o modelo gera, avalia contra critérios de qualidade, identifica pontos fracos e melhora. Você verá ele iterar até que o código atenda aos padrões de produção.

<img src="../../../translated_images/pt-BR/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Serviço completo de validação de email (5.2MB)*

### Análise Estruturada

Revisões de código precisam de frameworks de avaliação consistentes. O modelo analisa código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-BR/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revisão de código baseada em framework*

### Chat de Várias Rodadas

Pergunte "O que é Spring Boot?" e então imediatamente siga com "Mostre um exemplo". O modelo se lembra da primeira pergunta e te dá um exemplo específico de Spring Boot. Sem memória, essa segunda pergunta seria muito vaga.

<img src="../../../translated_images/pt-BR/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Preservação do contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema de matemática e experimente com Raciocínio Passo a Passo e Baixa Disposição. Baixa disposição só dá a resposta - rápido, mas opaco. Passo a passo mostra cada cálculo e decisão.

<img src="../../../translated_images/pt-BR/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema matemático com passos explícitos*

### Saída Restrita

Quando você precisa de formatos específicos ou contagem exata de palavras, este padrão impõe aderência rigorosa. Experimente gerar um resumo com exatamente 100 palavras em formato de pontos.

<img src="../../../translated_images/pt-BR/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Resumo de aprendizado de máquina com controle de formato*

## O Que Você Está Realmente Aprendendo

**O Esforço de Raciocínio Muda Tudo**

GPT-5.2 permite que você controle o esforço computacional por meio dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo leva tempo para pensar profundamente. Você está aprendendo a casar esforço com complexidade da tarefa - não perca tempo em perguntas simples, mas não se apresse em decisões complexas.

**A Estrutura Guia o Comportamento**

Reparou nas tags XML nos prompts? Elas não são decorativas. Modelos seguem instruções estruturadas de forma mais confiável do que texto livre. Quando precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade Por Autoavaliação**

Os padrões auto-reflexivos funcionam ao tornar explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça certo", você diz exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo então pode avaliar sua própria saída e melhorar. Isso transforma geração de código em um processo, não uma loteria.

**Contexto É Finito**

Conversas de várias rodadas funcionam incluindo o histórico das mensagens em cada requisição. Mas há um limite - todo modelo tem uma contagem máxima de tokens. Conforme a conversa cresce, você precisará de estratégias para manter contexto relevante sem estourar esse limite. Este módulo mostra como a memória funciona; mais tarde você aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos empenhemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->