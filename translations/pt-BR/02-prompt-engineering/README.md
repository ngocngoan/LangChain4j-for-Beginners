# Módulo 02: Engenharia de Prompt com GPT-5.2

## Índice

- [O que você vai aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Entendendo Engenharia de Prompt](../../../02-prompt-engineering)
- [Fundamentos de Engenharia de Prompt](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Templates de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Usando Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Tela da Aplicação](../../../02-prompt-engineering)
- [Explorando os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefa (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat com Várias Rodadas](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrangida](../../../02-prompt-engineering)
- [O que Você Está Realmente Aprendendo](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que você vai aprender

<img src="../../../translated_images/pt-BR/what-youll-learn.c68269ac048503b2.webp" alt="O que você vai aprender" width="800"/>

No módulo anterior, você viu como a memória possibilita a IA conversacional e usou modelos do GitHub para interações básicas. Agora vamos focar em como você faz perguntas — os próprios prompts — usando o GPT-5.2 da Azure OpenAI. A forma como você estrutura seus prompts afeta dramaticamente a qualidade das respostas que obtém. Começamos com uma revisão das técnicas fundamentais de prompting, depois avançamos para oito padrões avançados que aproveitam ao máximo as capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque ele introduz controle de raciocínio — você pode dizer ao modelo quanto pensar antes de responder. Isso torna as diferentes estratégias de prompting mais evidentes e ajuda a entender quando usar cada abordagem. Também vamos nos beneficiar dos limites de taxa menores da Azure para GPT-5.2 em comparação com os modelos do GitHub.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não concluiu o Módulo 01, siga as instruções de implantação lá primeiro.

## Entendendo Engenharia de Prompt

<img src="../../../translated_images/pt-BR/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O que é Engenharia de Prompt?" width="800"/>

Engenharia de prompt é sobre projetar texto de entrada que consistentemente traz os resultados que você precisa. Não é só fazer perguntas — é estruturar as solicitações para que o modelo entenda exatamente o que você quer e como entregar.

Pense nisso como dar instruções a um colega. "Corrija o bug" é vago. "Corrija a exceção de ponteiro nulo em UserService.java linha 45 adicionando uma verificação de nulo" é específico. Modelos de linguagem funcionam do mesmo jeito — especificidade e estrutura importam.

<img src="../../../translated_images/pt-BR/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se encaixa" width="800"/>

LangChain4j fornece a infraestrutura — conexões de modelo, memória e tipos de mensagem — enquanto padrões de prompt são apenas texto cuidadosamente estruturado que você envia através dessa infraestrutura. Os blocos-chave são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que carrega seu pedido real).

## Fundamentos de Engenharia de Prompt

<img src="../../../translated_images/pt-BR/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão geral de cinco padrões de Engenharia de Prompt" width="800"/>

Antes de mergulharmos nos padrões avançados deste módulo, vamos revisar cinco técnicas fundamentais de prompting. São os blocos de construção que todo engenheiro de prompt deve conhecer. Se você já trabalhou com o [módulo Quick Start](../00-quick-start/README.md#2-prompt-patterns), já viu estes em ação — aqui está a base conceitual por trás deles.

### Zero-Shot Prompting

A abordagem mais simples: dê ao modelo uma instrução direta sem exemplos. O modelo depende inteiramente do seu treinamento para entender e executar a tarefa. Funciona bem para solicitações diretas onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-BR/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções ou qualquer tarefa que o modelo possa lidar sem orientação adicional.

### Few-Shot Prompting

Forneça exemplos que demonstrem o padrão que você quer que o modelo siga. O modelo aprende o formato esperado entrada-saída a partir dos seus exemplos e aplica a novos inputs. Isso melhora dramaticamente a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-BR/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

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

**Quando usar:** Classificações personalizadas, formatação consistente, tarefas específicas de domínio ou quando resultados zero-shot são inconsistentes.

### Chain of Thought

Peça ao modelo para mostrar seu raciocínio passo a passo. Ao invés de pular direto para uma resposta, o modelo separa o problema e trabalha cada parte explicitamente. Isso melhora a precisão em matemática, lógica e tarefas de raciocínio múltiplo.

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

**Quando usar:** Problemas matemáticos, quebra-cabeças lógicos, depuração ou qualquer tarefa onde mostrar o processo de raciocínio melhora a precisão e a confiança.

### Role-Based Prompting

Defina uma persona ou papel para a IA antes de fazer sua pergunta. Isso fornece contexto que molda o tom, profundidade e foco da resposta. Um "arquiteto de software" dá conselhos diferentes de um "desenvolvedor júnior" ou "auditor de segurança".

<img src="../../../translated_images/pt-BR/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

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

**Quando usar:** Revisões de código, tutoria, análises específicas de domínio, ou quando precisa de respostas adaptadas a um nível particular de especialização ou perspectiva.

### Prompt Templates

Crie prompts reutilizáveis com espaços variáveis. Ao invés de escrever um prompt novo toda vez, defina um template uma vez e preencha diferentes valores. A classe `PromptTemplate` do LangChain4j facilita isso com a sintaxe `{{variable}}`.

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

**Quando usar:** Consultas repetidas com diferentes entradas, processamento em lote, construção de fluxos de trabalho AI reutilizáveis ou qualquer cenário onde a estrutura do prompt permanece constante mas os dados mudam.

---

Esses cinco fundamentos dão um conjunto sólido para a maioria das tarefas de prompting. O restante deste módulo se baseia neles com **oito padrões avançados** que aproveitam o controle de raciocínio, autoavaliação e capacidades de saída estruturada do GPT-5.2.

## Padrões Avançados

Com os fundamentos cobertos, vamos aos oito padrões avançados que tornam este módulo único. Nem todo problema precisa da mesma abordagem. Algumas perguntas precisam de respostas rápidas, outras precisam de reflexão profunda. Algumas precisam de raciocínio visível, outras só resultado. Cada padrão abaixo é otimizado para um cenário diferente — e o controle de raciocínio do GPT-5.2 torna essas diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-BR/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito Padrões de Prompt" width="800"/>

*Visão geral dos oito padrões de engenharia de prompt e seus casos de uso*

<img src="../../../translated_images/pt-BR/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controle de Raciocínio com GPT-5.2" width="800"/>

*O controle de raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — de respostas rápidas diretas a explorações profundas*

<img src="../../../translated_images/pt-BR/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparação de Esforço de Raciocínio" width="800"/>

*Comparação entre baixa disposição (rápido, direto) e alta disposição (minucioso, exploratório) para raciocínio*

**Baixa Disposição (Rápido & Focado)** - Para perguntas simples onde você quer respostas rápidas e diretas. O modelo faz raciocínio mínimo — no máximo 2 passos. Use para cálculos, consultas ou perguntas diretas.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore com GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:
> - "Qual a diferença entre os padrões de prompting de baixa disposição e alta disposição?"
> - "Como as tags XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de autorreflexão versus instrução direta?"

**Alta Disposição (Profundo & Minucioso)** - Para problemas complexos onde você quer análise abrangente. O modelo explora a fundo e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura ou pesquisa complexa.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefa (Progresso Passo a Passo)** - Para fluxos de trabalho com vários passos. O modelo fornece um plano adiantado, narra cada passo conforme executa, depois dá um resumo. Use para migrações, implementações ou qualquer processo multi-etapas.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

O prompting Chain-of-Thought pede explicitamente ao modelo que mostre seu processo de raciocínio, melhorando a precisão para tarefas complexas. A divisão passo a passo ajuda humanos e IA a entenderem a lógica.

> **🤖 Experimente com o Chat do [GitHub Copilot](https://github.com/features/copilot):** Pergunte sobre este padrão:
> - "Como eu adaptaria o padrão de execução de tarefas para operações longas?"
> - "Quais as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como capturar e exibir atualizações de progresso intermediárias numa interface de usuário?"

<img src="../../../translated_images/pt-BR/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão de Execução de Tarefa" width="800"/>

*Plano → Executar → Resumir para tarefas multi-etapas*

**Código Auto-Reflexivo** - Para gerar código com qualidade de produção. O modelo gera código, verifica contra critérios de qualidade e melhora iterativamente. Use ao construir novas funcionalidades ou serviços.

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

<img src="../../../translated_images/pt-BR/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexão" width="800"/>

*Loop de melhoria iterativa - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliações consistentes. O modelo revisa código usando um framework fixo (correção, práticas, desempenho, segurança). Use para revisões de código ou avaliações de qualidade.

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
> - "Como personalizar o framework de análise para diferentes tipos de revisão de código?"
> - "Qual a melhor forma de processar e agir sobre a saída estruturada programaticamente?"
> - "Como garantir níveis consistentes de severidade em diferentes sessões de revisão?"

<img src="../../../translated_images/pt-BR/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão de Análise Estruturada" width="800"/>

*Framework dividido em quatro categorias para revisões consistentes com níveis de severidade*

**Chat com Várias Rodadas** - Para conversas que precisam de contexto. O modelo lembra mensagens anteriores e constrói sobre elas. Use para sessões interativas de ajuda ou Q&A complexas.

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

*Como o contexto da conversa se acumula ao longo de múltiplas rodadas até o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que precisam de lógica visível. O modelo mostra raciocínio explícito para cada etapa. Use para problemas matemáticos, quebra-cabeças lógicos ou quando precisar entender o processo de pensamento.

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

**Saída Constrangida** - Para respostas com requisitos específicos de formato. O modelo segue rigorosamente regras de formato e tamanho. Use para resumos ou quando precisar de estrutura precisa na saída.

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

*Enforcement de formato, tamanho e requisitos estruturais específicos*

## Usando Recursos Azure Existentes

**Verifique a implantação:**

Certifique-se que o arquivo `.env` existe no diretório raiz com credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8083. Pode pular os comandos de inicialização abaixo e ir direto para http://localhost:8083.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários do VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).
A partir do Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um único clique
- Visualizar logs da aplicação em tempo real
- Monitorar o status da aplicação

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` na raiz e irão construir os JARs caso não existam.

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
./stop.sh  # Somente este módulo
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

*O dashboard principal mostrando os 8 padrões de engenharia de prompts com suas características e casos de uso*

## Explorando os Padrões

A interface web permite que você experimente diferentes estratégias de prompt. Cada padrão resolve problemas distintos – teste-os para ver quando cada abordagem se destaca.

### Baixa vs Alta Disposição

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Disposição. Você obterá uma resposta instantânea e direta. Agora pergunte algo complexo como "Desenhe uma estratégia de cache para uma API de alto tráfego" usando Alta Disposição. Observe como o modelo desacelera e fornece raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta – mas o prompt determina o quanto de pensamento ele deve fazer.

<img src="../../../translated_images/pt-BR/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-BR/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Estratégia de cache abrangente (2.8MB)*

### Execução de Tarefas (Preâmbulos de Ferramentas)

Workflows multi-etapas se beneficiam de planejamento antecipado e narração do progresso. O modelo descreve o que fará, narra cada passo e então resume os resultados.

<img src="../../../translated_images/pt-BR/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Criando um endpoint REST com narração passo a passo (3.9MB)*

### Código Auto-Reflexivo

Tente "Crie um serviço de validação de e-mail". Em vez de apenas gerar código e parar, o modelo gera, avalia contra critérios de qualidade, identifica pontos fracos e melhora. Você verá ele iterar até que o código atenda aos padrões de produção.

<img src="../../../translated_images/pt-BR/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Serviço completo de validação de e-mail (5.2MB)*

### Análise Estruturada

Revisões de código precisam de frameworks consistentes de avaliação. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-BR/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Revisão de código baseada em framework*

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e em seguida, imediatamente, pergunte "Mostre um exemplo". O modelo lembra sua primeira pergunta e fornece um exemplo específico de Spring Boot. Sem memória, essa segunda pergunta seria vaga demais.

<img src="../../../translated_images/pt-BR/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Preservação de contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema matemático e tente tanto com Raciocínio Passo a Passo quanto com Baixa Disposição. Baixa disposição apenas dá a resposta – rápido mas opaco. O passo a passo mostra cada cálculo e decisão.

<img src="../../../translated_images/pt-BR/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Problema matemático com etapas explícitas*

### Saída Constrainada

Quando você precisa de formatos específicos ou contagem de palavras, esse padrão impõe aderência rigorosa. Tente gerar um resumo com exatamente 100 palavras em formato de tópicos.

<img src="../../../translated_images/pt-BR/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Resumo de machine learning com controle de formato*

## O Que Você Está Realmente Aprendendo

**Esforço de Raciocínio Muda Tudo**

GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo leva tempo para pensar profundamente. Você está aprendendo a combinar esforço com a complexidade da tarefa – não perca tempo em perguntas simples, mas também não apresse decisões complexas.

**Estrutura Guia o Comportamento**

Reparou nas tags XML nos prompts? Elas não são decorativas. Modelos seguem instruções estruturadas de forma mais confiável do que texto livre. Quando você precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a rastrear onde está e o que vem depois.

<img src="../../../translated_images/pt-BR/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomia de um prompt bem estruturado com seções claras e organização estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões auto-reflexivos funcionam ao tornar explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça certo", você diz exatamente o que "certo" significa: lógica correta, tratamento de erros, desempenho, segurança. O modelo então pode avaliar sua própria saída e melhorar. Isso transforma a geração de código de uma loteria em um processo.

**Contexto É Finito**

Conversas multi-turno funcionam incluindo o histórico de mensagens a cada requisição. Mas há um limite – todo modelo tem um número máximo de tokens. Conforme as conversas crescem, você precisará de estratégias para manter contexto relevante sem alcançar esse limite. Este módulo mostra como a memória funciona; depois você aprenderá quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Principal](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomendamos a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações errôneas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->