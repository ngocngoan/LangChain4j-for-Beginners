# Módulo 02: Engenharia de Prompt com GPT-5.2

## Índice

- [O que Você Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo a Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompt](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Prompt Baseado em Papel](../../../02-prompt-engineering)
  - [Templates de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usando Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrangida](../../../02-prompt-engineering)
- [O Que Você Realmente Está Aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que Você Vai Aprender

<img src="../../../translated_images/pt-BR/what-youll-learn.c68269ac048503b2.webp" alt="O que Você Vai Aprender" width="800"/>

No módulo anterior, você viu como a memória possibilita a IA conversacional e usou Models do GitHub para interações básicas. Agora vamos focar em como você faz perguntas — os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como você estrutura seus prompts afeta dramaticamente a qualidade das respostas que recebe. Começamos revisando as técnicas fundamentais de prompt e depois avançamos em oito padrões avançados que aproveitam totalmente as capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque ele introduz controle do raciocínio — você pode dizer ao modelo quanto pensar antes de responder. Isso torna diferentes estratégias de prompt mais evidentes e ajuda você a entender quando usar cada abordagem. Também vamos nos beneficiar dos limites de taxa menores do Azure para GPT-5.2 comparado aos Models do GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não completou o Módulo 01, siga as instruções de implantação lá primeiro.

## Entendendo a Engenharia de Prompt

<img src="../../../translated_images/pt-BR/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O que é Engenharia de Prompt?" width="800"/>

Engenharia de prompt é sobre projetar texto de entrada que consistentemente te dá os resultados que você precisa. Não é apenas sobre fazer perguntas – é sobre estruturar pedidos para que o modelo entenda exatamente o que você quer e como entregar.

Pense como dar instruções a um colega. "Conserte o bug" é vago. "Conserte a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação de nulo" é específico. Modelos de linguagem funcionam do mesmo jeito – especificidade e estrutura importam.

<img src="../../../translated_images/pt-BR/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se Encaixa" width="800"/>

LangChain4j fornece a infraestrutura — conexões de modelo, memória, e tipos de mensagem — enquanto os padrões de prompt são apenas texto cuidadosamente estruturado que você envia através dessa infraestrutura. Os blocos-chave são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que carrega sua solicitação real).

## Fundamentos da Engenharia de Prompt

<img src="../../../translated_images/pt-BR/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão Geral dos Cinco Padrões de Engenharia de Prompt" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. São os blocos de construção que todo engenheiro de prompt deve conhecer. Se você já trabalhou pelo [módulo de Início Rápido](../00-quick-start/README.md#2-prompt-patterns), já viu isso em ação — aqui está a estrutura conceitual por trás deles.

### Prompt Zero-Shot

A abordagem mais simples: dê uma instrução direta ao modelo sem exemplos. O modelo confia inteiramente no seu treinamento para entender e executar a tarefa. Isso funciona bem para pedidos diretos onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-BR/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Zero-Shot" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções ou qualquer tarefa que o modelo possa manejar sem orientação adicional.

### Prompt Few-Shot

Forneça exemplos que demonstrem o padrão que você quer que o modelo siga. O modelo aprende o formato entrada-saída esperado pelos seus exemplos e o aplica a novas entradas. Isso melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-BR/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompt Few-Shot" width="800"/>

*Aprendendo com exemplos — o modelo identifica o padrão e o aplica a novas entradas*

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

### Chain of Thought

Peça ao modelo que mostre seu raciocínio passo a passo. Em vez de pular direto para uma resposta, o modelo divide o problema e trabalha cada parte explicitamente. Isso melhora a precisão em matemática, lógica e tarefas de raciocínio multi-etapas.

<img src="../../../translated_images/pt-BR/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Chain of Thought" width="800"/>

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

**Quando usar:** Problemas matemáticos, quebra-cabeças lógicos, depuração, ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e confiança.

### Prompt Baseado em Papel

Defina uma persona ou papel para a IA antes de fazer sua pergunta. Isso fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou "auditor de segurança".

<img src="../../../translated_images/pt-BR/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Baseado em Papel" width="800"/>

*Definindo contexto e persona — a mesma pergunta recebe resposta diferente dependendo do papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análise de domínio específico, ou quando você precisa de respostas ajustadas a um nível de expertise ou perspectiva particular.

### Templates de Prompt

Crie prompts reutilizáveis com espaços variáveis. Ao invés de escrever um novo prompt toda vez, defina um template uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isso com a sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-BR/prompt-templates.14bfc37d45f1a933.webp" alt="Templates de Prompt" width="800"/>

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

**Quando usar:** Consultas repetidas com entradas diferentes, processamento em lote, construindo workflows de IA reutilizáveis, ou qualquer cenário onde a estrutura do prompt permanece a mesma mas os dados mudam.

---

Esses cinco fundamentos dão uma base sólida para a maioria das tarefas de prompting. O restante deste módulo se apoia neles com **oito padrões avançados** que aproveitam o controle do raciocínio do GPT-5.2, auto-avaliação e capacidades de saída estruturada.

## Padrões Avançados

Com os fundamentos cobertos, vamos aos oito padrões avançados que tornam este módulo único. Nem todo problema precisa da mesma abordagem. Algumas perguntas pedem respostas rápidas, outras precisam de pensamento profundo. Algumas precisam de raciocínio visível, outras só os resultados. Cada padrão abaixo é otimizado para um cenário diferente — e o controle do raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito Padrões de Prompting" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e seus casos de uso*

<img src="../../../translated_images/pt-BR/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controle de Raciocínio com GPT-5.2" width="800"/>

*O controle de raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — de respostas rápidas e diretas a profundas explorações*

**Baixa Disposição (Rápido e Focado)** – Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo — no máximo 2 passos. Use para cálculos, consultas ou perguntas diretas.

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
> - "Qual a diferença entre os padrões de prompting baixa e alta disposição?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de auto-reflexão versus instrução direta?"

**Alta Disposição (Profundo e Completo)** – Para problemas complexos onde você quer análise abrangente. O modelo explora detalhadamente e mostra raciocínio detalhado. Use para design de sistemas, decisões arquiteturais, ou pesquisas complexas.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** – Para fluxos de trabalho multi-etapas. O modelo fornece um plano inicial, narra cada passo enquanto trabalha, depois dá um resumo. Use para migrações, implementações, ou qualquer processo com várias etapas.

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

Prompt Chain-of-Thought pede explicitamente que o modelo mostre seu processo de raciocínio, melhorando precisão para tarefas complexas. A divisão passo a passo ajuda tanto humanos quanto IA a entenderem a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como eu adaptaria o padrão de execução de tarefas para operações de longa duração?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e mostrar atualizações de progresso intermediárias em uma interface?"

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão Execução de Tarefas" width="800"/>

*Planejar → Executar → Resumir fluxo para tarefas multi-etapas*

**Código Auto-Reflexivo** – Para gerar código com qualidade de produção. O modelo gera código seguindo padrões de produção com tratamento adequado de erros. Use ao criar novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexão" width="800"/>

*Loop de melhoria iterativa – gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** – Para avaliação consistente. O modelo revisa código usando uma estrutura fixa (correção, práticas, desempenho, segurança, manutenibilidade). Use para revisões de código ou avaliações de qualidade.

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
> - "Como personalizar a estrutura de análise para diferentes tipos de revisão de código?"
> - "Qual a melhor forma de analisar e agir sobre a saída estruturada programaticamente?"
> - "Como garantir níveis de severidade consistentes em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão Análise Estruturada" width="800"/>

*Estrutura para revisões de código consistentes com níveis de severidade*

**Chat Multi-turno** – Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões interativas de ajuda ou dúvidas complexas.

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

**Raciocínio Passo a Passo** – Para problemas que exigem lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas matemáticos, quebra-cabeças lógicos, ou quando precisar entender o processo de pensamento.

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

*Quebrando problemas em etapas lógicas explícitas*

**Saída Constrangida** – Para respostas com requisitos específicos de formato. O modelo segue estritamente regras de formato e comprimento. Use para resumos ou quando precisar de estrutura de saída precisa.

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

<img src="../../../translated_images/pt-BR/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Padrão de Saída Constrangida" width="800"/>

*Aplicando requisitos específicos de formato, comprimento e estrutura*

## Usando Recursos Azure Existentes

**Verifique a implantação:**

Certifique-se que o arquivo `.env` existe no diretório raiz com credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8083. Você pode pular os comandos de início abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O dev container inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades do lado esquerdo do VS Code (procure pelo ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs da aplicação em tempo real
- Monitorar o status da aplicação
Simplesmente clique no botão de play ao lado de "prompt-engineering" para iniciar este módulo, ou inicie todos os módulos de uma vez.

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
cd ..  # Do diretório raiz
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

Ambos os scripts carregam automaticamente variáveis de ambiente do arquivo `.env` raiz e irão construir os JARs caso não existam.

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

## Capturas de Tela da Aplicação

<img src="../../../translated_images/pt-BR/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O painel principal mostrando todos os 8 padrões de engenharia de prompt com suas características e casos de uso*

## Explorando os Padrões

A interface web permite que você experimente diferentes estratégias de prompt. Cada padrão resolve problemas diferentes - teste-os para ver quando cada abordagem brilha.

> **Nota: Streaming vs Não-Streaming** — Cada página de padrão oferece dois botões: **🔴 Resposta em Stream (Ao Vivo)** e uma opção **Não-streaming**. O streaming usa Server-Sent Events (SSE) para exibir os tokens em tempo real conforme o modelo os gera, permitindo que você veja o progresso imediatamente. A opção não-streaming espera a resposta completa antes de exibi-la. Para prompts que acionam raciocínio profundo (ex.: Alta Empolgação, Código Auto-Reflexivo), a chamada não-streaming pode demorar muito — às vezes minutos — sem feedback visível. **Use streaming ao experimentar prompts complexos** para ver o modelo funcionando e evitar a impressão de que a requisição expirou.
>
> **Nota: Requisito do Navegador** — O recurso de streaming usa a API Fetch Streams (`response.body.getReader()`) que requer um navegador completo (Chrome, Edge, Firefox, Safari). Não funciona no Simple Browser embutido do VS Code, pois sua webview não suporta a API ReadableStream. Se usar o Simple Browser, os botões não-streaming funcionarão normalmente — somente os botões de streaming são afetados. Abra `http://localhost:8083` em um navegador externo para a experiência completa.

### Baixa vs Alta Empolgação

Faça uma pergunta simples como "Quanto é 15% de 200?" usando Baixa Empolgação. Você receberá uma resposta direta e instantânea. Agora pergunte algo complexo como "Desenhe uma estratégia de cache para uma API de alto tráfego" usando Alta Empolgação. Clique em **🔴 Resposta em Stream (Ao Vivo)** e observe o raciocínio detalhado do modelo aparecer token por token. Mesmo modelo, mesma estrutura de pergunta — mas o prompt diz quanto pensar.

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapas se beneficiam de planejamento prévio e narração do progresso. O modelo descreve o que fará, narra cada passo e depois resume os resultados.

### Código Auto-Reflexivo

Tente "Crie um serviço de validação de e-mail". Em vez de apenas gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica fraquezas e melhora. Você verá ele iterar até que o código atenda aos padrões de produção.

### Análise Estruturada

Revisões de código precisam de frameworks de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e em seguida imediatamente "Me mostre um exemplo". O modelo lembra sua primeira pergunta e te dá um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria vaga demais.

### Raciocínio Passo a Passo

Escolha um problema matemático e tente tanto com Raciocínio Passo a Passo quanto com Baixa Empolgação. Baixa empolgação só dá a resposta - rápida porém opaca. Passo a passo mostra cada cálculo e decisão.

### Saída Constrangida

Quando precisar de formatos específicos ou contagem de palavras, este padrão impõe estrita conformidade. Tente gerar um resumo com exatamente 100 palavras em formato de tópicos.

## O Que Você Está Realmente Aprendendo

**O Esforço de Raciocínio Muda Tudo**

GPT-5.2 permite controlar o esforço computacional via seus prompts. Baixo esforço significa respostas rápidas com pouca exploração. Alto esforço significa que o modelo leva tempo para pensar profundamente. Você está aprendendo a casar esforço com a complexidade da tarefa — não perca tempo com perguntas simples, mas também não apresse decisões complexas.

**A Estrutura Guia o Comportamento**

Percebeu as tags XML nos prompts? Elas não são decorativas. Modelos seguem instruções estruturadas com mais confiabilidade que texto livre. Quando for necessário processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização ao estilo XML*

**Qualidade Através da Autoavaliação**

Padrões auto-reflexivos funcionam tornando os critérios de qualidade explícitos. Em vez de esperar que o modelo "faça certo", você explica exatamente o que significa "certo": lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar sua própria saída e melhorar. Isso transforma a geração de código de uma loteria em um processo.

**O Contexto É Finito**

Conversas multi-turno funcionam incluindo o histórico de mensagens em cada requisição. Mas existe um limite — cada modelo tem um número máximo de tokens. Conforme as conversas crescem, você precisará de estratégias para manter contexto relevante sem chegar ao teto. Este módulo mostra como a memória funciona; depois você aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração Augmentada por Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->