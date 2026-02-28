# Módulo 02: Engenharia de Prompt com GPT-5.2

## Índice

- [Vídeo Demonstrativo](../../../02-prompt-engineering)
- [O Que Você Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompt](../../../02-prompt-engineering)
  - [Prompt Zero-Shot](../../../02-prompt-engineering)
  - [Prompt Few-Shot](../../../02-prompt-engineering)
  - [Cadeia de Pensamento](../../../02-prompt-engineering)
  - [Prompt Baseado em Papel](../../../02-prompt-engineering)
  - [Templates de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usando Recursos Existentes do Azure](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Vontade](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código com Auto-Reflexão](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turn](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrainada](../../../02-prompt-engineering)
- [O Que Você Está Realmente Aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## Vídeo Demonstrativo

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Engenharia de Prompt com LangChain4j - Sessão ao Vivo" width="800"/></a>

## O Que Você Vai Aprender

<img src="../../../translated_images/pt-BR/what-youll-learn.c68269ac048503b2.webp" alt="O Que Você Vai Aprender" width="800"/>

No módulo anterior, você viu como a memória habilita IA conversacional e usou Modelos do GitHub para interações básicas. Agora vamos focar em como você faz perguntas — os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como você estrutura seus prompts afeta drasticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting, depois avançamos para oito padrões avançados que tiram pleno proveito das capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque ele introduz controle de raciocínio - você pode dizer ao modelo quanto pensamento ele deve fazer antes de responder. Isso torna as diferentes estratégias de prompting mais evidentes e ajuda você a entender quando usar cada abordagem. Também nos beneficiamos dos limites de taxa menores do Azure para o GPT-5.2 em comparação com os Modelos do GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais do Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não concluiu o Módulo 01, siga as instruções de implantação lá primeiro.

## Entendendo Engenharia de Prompt

<img src="../../../translated_images/pt-BR/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O Que é Engenharia de Prompt?" width="800"/>

Engenharia de prompt é sobre projetar textos de entrada que consistentemente geram os resultados que você precisa. Não é apenas fazer perguntas - é estruturar solicitações para que o modelo entenda exatamente o que você quer e como entregar isso.

Pense nisso como dar instruções para um colega. "Corrija o bug" é vago. "Corrija a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação de nulo" é específico. Modelos de linguagem funcionam da mesma forma - especificidade e estrutura importam.

<img src="../../../translated_images/pt-BR/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se Encaixa" width="800"/>

LangChain4j fornece a infraestrutura — conexões de modelo, memória e tipos de mensagens — enquanto os padrões de prompt são apenas textos cuidadosamente estruturados que você envia por essa infraestrutura. Os blocos fundamentais são `SystemMessage` (que define o comportamento e o papel da IA) e `UserMessage` (que carrega sua solicitação real).

## Fundamentos da Engenharia de Prompt

<img src="../../../translated_images/pt-BR/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão Geral dos Cinco Padrões de Engenharia de Prompt" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. Esses são os blocos de construção que todo engenheiro de prompt deve conhecer. Se você já trabalhou no [módulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), já viu isso em ação — aqui está a estrutura conceitual por trás deles.

### Prompt Zero-Shot

A abordagem mais simples: dê ao modelo uma instrução direta sem exemplos. O modelo depende inteiramente do seu treinamento para entender e executar a tarefa. Isso funciona bem para solicitações simples onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-BR/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompt Zero-Shot" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa apenas pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções ou qualquer tarefa que o modelo possa realizar sem orientação adicional.

### Prompt Few-Shot

Forneça exemplos que demonstrem o padrão que você deseja que o modelo siga. O modelo aprende o formato esperado de entrada-saída a partir dos seus exemplos e o aplica a novas entradas. Isso melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio ou quando os resultados zero-shot são inconsistentes.

### Cadeia de Pensamento

Peça ao modelo para mostrar seu raciocínio passo a passo. Em vez de pular direto para uma resposta, o modelo divide o problema e trabalha cada parte explicitamente. Isso melhora a precisão em matemática, lógica e tarefas de raciocínio em vários passos.

<img src="../../../translated_images/pt-BR/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompt Cadeia de Pensamento" width="800"/>

*Raciocínio passo a passo — dividindo problemas complexos em etapas lógicas explícitas*

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

### Prompt Baseado em Papel

Defina uma persona ou papel para a IA antes de fazer sua pergunta. Isso fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou um "auditor de segurança".

<img src="../../../translated_images/pt-BR/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompt Baseado em Papel" width="800"/>

*Definindo contexto e persona — a mesma pergunta recebe respostas diferentes dependendo do papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análises específicas de domínio, ou quando você precisa de respostas adaptadas a um nível de especialização ou perspectiva particular.

### Templates de Prompt

Crie prompts reutilizáveis com espaços para variáveis. Em vez de escrever um novo prompt toda vez, defina um template uma vez e preencha com valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isso com a sintaxe `{{variavel}}`.

<img src="../../../translated_images/pt-BR/prompt-templates.14bfc37d45f1a933.webp" alt="Templates de Prompt" width="800"/>

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

**Quando usar:** Consultas repetidas com entradas diferentes, processamento em lote, construção de fluxos de trabalho de IA reutilizáveis ou qualquer cenário onde a estrutura do prompt permanece a mesma, mas os dados mudam.

---

Esses cinco fundamentos dão a você um kit sólido para a maioria das tarefas de prompting. O resto deste módulo constrói sobre eles com **oito padrões avançados** que aproveitam o controle de raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos para os oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras exigem reflexão profunda. Algumas precisam de raciocínio visível, outras apenas de resultados. Cada padrão abaixo é otimizado para um cenário diferente — e o controle de raciocínio do GPT-5.2 torna as diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito Padrões de Prompting" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e seus casos de uso*

<img src="../../../translated_images/pt-BR/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controle de Raciocínio com GPT-5.2" width="800"/>

*O controle de raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — desde respostas rápidas e diretas até exploração profunda*

**Baixa Vontade (Rápido & Focado)** - Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo - máximo 2 passos. Use isso para cálculos, consultas ou perguntas diretas.

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
> - "Qual é a diferença entre os padrões de prompting baixa vontade e alta vontade?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão versus instrução direta?"

**Alta Vontade (Profundo & Completo)** - Para problemas complexos onde você quer uma análise abrangente. O modelo explora detalhadamente e mostra raciocínio detalhado. Use isso para design de sistemas, decisões de arquitetura ou pesquisas complexas.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho multi-etapas. O modelo fornece um plano inicial, narra cada passo conforme avança, e depois dá um resumo. Use isso para migrações, implementações ou qualquer processo multi-etapas.

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

O prompting Cadeia-de-Pensamento pede explicitamente ao modelo para mostrar seu processo de raciocínio, melhorando a precisão para tarefas complexas. A decomposição passo a passo ajuda humanos e IA a entenderem a lógica.

> **🤖 Experimente com Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como eu adaptaria o padrão de execução de tarefa para operações de longa duração?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e exibir atualizações intermediárias de progresso em uma interface?"

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão de Execução de Tarefa" width="800"/>

*Fluxo: Planejar → Executar → Resumir para tarefas multi-etapas*

**Código com Auto-Reflexão** - Para gerar código com qualidade de produção. O modelo gera código seguindo padrões de produção com tratamento adequado de erros. Use isso ao construir novas funcionalidades ou serviços.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexão" width="800"/>

*Loop iterativo de melhoria - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliação consistente. O modelo revisa código usando um framework fixo (correção, práticas, performance, segurança, manutenibilidade). Use isso para revisões de código ou avaliações de qualidade.

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

> **🤖 Experimente com Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre análise estruturada:
> - "Como posso personalizar o framework de análise para diferentes tipos de revisão de código?"
> - "Qual a melhor forma de interpretar e agir sobre saídas estruturadas programaticamente?"
> - "Como garantir níveis de severidade consistentes em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão de Análise Estruturada" width="800"/>

*Framework para revisões consistentes de código com níveis de severidade*

**Chat Multi-Turn** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use isso para sessões de ajuda interativas ou Q&A complexos.

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

*Como o contexto da conversa se acumula ao longo de várias interações até atingir o limite de tokens*

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

<img src="../../../translated_images/pt-BR/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Padrão Passo a Passo" width="800"/>

*Dividindo problemas em etapas lógicas explícitas*

**Saída Constrainada** - Para respostas com requisitos específicos de formato. O modelo segue estritamente regras de formato e tamanho. Use isso para resumos ou quando precisar de estrutura precisa de saída.

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

*Impondo requisitos específicos de formato, tamanho e estrutura*

## Usando Recursos Existentes do Azure

**Verifique a implantação:**

Garanta que o arquivo `.env` exista no diretório raiz com credenciais do Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie o aplicativo:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8083. Você pode pular os comandos de inicialização abaixo e ir diretamente para http://localhost:8083.
**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários do VS Code)**

O dev container inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades no lado esquerdo do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um único clique
- Visualizar logs das aplicações em tempo real
- Monitorar o status das aplicações

Basta clicar no botão de play ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e irão compilar os JARs se eles não existirem.

> **Nota:** Se preferir compilar todos os módulos manualmente antes de iniciar:
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

A interface web permite que você experimente diferentes estratégias de prompting. Cada padrão resolve problemas diferentes — experimente para ver quando cada abordagem brilha.

> **Nota: Streaming vs Não-Streaming** — Cada página do padrão oferece dois botões: **🔴 Stream Response (Live)** e uma opção **Não-streaming**. O streaming usa Server-Sent Events (SSE) para exibir tokens em tempo real conforme o modelo os gera, para que você veja o progresso imediatamente. A opção não-streaming espera a resposta completa antes de mostrar. Para prompts que desencadeiam raciocínios profundos (ex.: High Eagerness, Self-Reflecting Code), a chamada não-streaming pode demorar muito — às vezes minutos — sem feedback visível. **Use streaming ao experimentar prompts complexos** para ver o modelo funcionando e evitar a impressão de que a requisição demorou demais.
>
> **Nota: Requisito de Navegador** — O recurso de streaming usa a API Fetch Streams (`response.body.getReader()`), que requer um navegador completo (Chrome, Edge, Firefox, Safari). Não funciona no Simple Browser integrado do VS Code, pois sua webview não suporta a API ReadableStream. Se usar o Simple Browser, os botões não-streaming funcionarão normalmente — somente os botões streaming são impactados. Abra `http://localhost:8083` em um navegador externo para uma experiência completa.

### Baixa vs Alta Eageridade

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Eageridade. Você receberá uma resposta direta e instantânea. Agora pergunte algo complexo como "Desenhe uma estratégia de cache para uma API de alto tráfego" usando Alta Eageridade. Clique em **🔴 Stream Response (Live)** e observe o raciocínio detalhado do modelo aparecer token por token. Mesmo modelo, mesma estrutura de pergunta — mas o prompt diz o quanto de reflexão fazer.

### Execução de Tarefas (Preambulos de Ferramentas)

Fluxos de trabalho multietapas se beneficiam de planejamento antecipado e narração do progresso. O modelo indica o que fará, narra cada passo e resume os resultados.

### Código Autorreflexivo

Experimente "Crie um serviço de validação de e-mails". Em vez de só gerar código e parar, o modelo gera, avalia segundo critérios de qualidade, identifica pontos fracos e melhora. Você verá iterações até que o código atinja padrões de produção.

### Análise Estruturada

Revisões de código precisam de um quadro de avaliação consistente. O modelo analisa código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e logo depois "Mostre um exemplo". O modelo se lembra da primeira pergunta e dá um exemplo específico de Spring Boot. Sem memória, a segunda pergunta seria vaga demais.

### Raciocínio Passo a Passo

Escolha um problema matemático e tente com Raciocínio Passo a Passo e Baixa Eageridade. Baixa eageridade dá só a resposta — rápido, mas opaco. O passo a passo mostra cada cálculo e decisão.

### Saída Confinada

Quando precisar de formatos específicos ou contagem exata de palavras, esse padrão impõe aderência rigorosa. Experimente gerar um resumo com exatamente 100 palavras em formato de tópicos.

## O que Você Está Realmente Aprendendo

**O Esforço de Raciocínio Muda Tudo**

O GPT-5.2 te permite controlar o esforço computacional pelos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço quer dizer que o modelo investe tempo para pensar profundamente. Você aprende a combinar esforço com complexidade da tarefa — não desperdice tempo com perguntas simples, mas também não apresse decisões complexas.

**A Estrutura Guia o Comportamento**

Reparou nas tags XML dos prompts? Não são decorativas. Modelos seguem instruções estruturadas de forma mais confiável do que texto livre. Quando precisar de processos multietapas ou lógica complexa, a estrutura ajuda o modelo a acompanhar onde está e o que vem a seguir.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade por Autoavaliação**

Os padrões autorreflexivos funcionam tornando explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça certo", você diz exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. Assim o modelo pode avaliar sua própria saída e melhorar. Isso transforma geração de código de uma loteria a um processo.

**O Contexto é Finito**

Conversas multietapas funcionam incluindo histórico de mensagens em cada requisição. Mas há limite — todo modelo tem um número máximo de tokens. À medida que a conversa cresce, você precisará de estratégias para manter o contexto relevante sem ultrapassar esse limite. Este módulo mostra como funciona a memória; mais tarde você aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração com Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->