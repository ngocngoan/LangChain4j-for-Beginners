# Módulo 02: Engenharia de Prompts com GPT-5.2

## Índice

- [O que Vai Aprender](../../../02-prompt-engineering)
- [Pré-requisitos](../../../02-prompt-engineering)
- [Compreender a Engenharia de Prompts](../../../02-prompt-engineering)
- [Fundamentos da Engenharia de Prompts](../../../02-prompt-engineering)
  - [Prompting Zero-Shot](../../../02-prompt-engineering)
  - [Prompting Few-Shot](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Prompting Baseado em Papéis](../../../02-prompt-engineering)
  - [Modelos de Prompt](../../../02-prompt-engineering)
- [Padrões Avançados](../../../02-prompt-engineering)
- [Utilizar Recursos Azure Existentes](../../../02-prompt-engineering)
- [Capturas de Ecrã da Aplicação](../../../02-prompt-engineering)
- [Explorar os Padrões](../../../02-prompt-engineering)
  - [Baixa vs Alta Disposição](../../../02-prompt-engineering)
  - [Execução de Tarefas (Preâmbulos de Ferramentas)](../../../02-prompt-engineering)
  - [Código Auto-Reflexivo](../../../02-prompt-engineering)
  - [Análise Estruturada](../../../02-prompt-engineering)
  - [Chat Multi-Turno](../../../02-prompt-engineering)
  - [Raciocínio Passo a Passo](../../../02-prompt-engineering)
  - [Saída Constrangida](../../../02-prompt-engineering)
- [O que Está Realmente a Aprender](../../../02-prompt-engineering)
- [Próximos Passos](../../../02-prompt-engineering)

## O que Vai Aprender

<img src="../../../translated_images/pt-PT/what-youll-learn.c68269ac048503b2.webp" alt="O que Vai Aprender" width="800"/>

No módulo anterior, viu como a memória capacita a IA conversacional e usou Modelos GitHub para interações básicas. Agora iremos focar em como faz perguntas — os próprios prompts — usando o GPT-5.2 do Azure OpenAI. A forma como estrutura os seus prompts afeta drasticamente a qualidade das respostas que obtém. Começamos por rever as técnicas fundamentais de prompting, e depois avançamos para oito padrões avançados que exploram ao máximo as capacidades do GPT-5.2.

Usaremos o GPT-5.2 porque este introduz controlo de raciocínio — pode indicar ao modelo quanto deve pensar antes de responder. Isto torna diferentes estratégias de prompting mais evidentes e ajuda a perceber quando usar cada abordagem. Também beneficiamos dos limites de taxa mais baixos do Azure para o GPT-5.2 em comparação com os Modelos GitHub.

## Pré-requisitos

- Completar o Módulo 01 (recursos Azure OpenAI implementados)
- Ficheiro `.env` na raiz com as credenciais Azure (criado por `azd up` no Módulo 01)

> **Nota:** Se ainda não completou o Módulo 01, siga primeiro as instruções de implementação aí.

## Compreender a Engenharia de Prompts

<img src="../../../translated_images/pt-PT/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="O que é Engenharia de Prompts?" width="800"/>

Engenharia de prompts é sobre desenhar texto de entrada que consiga consistentemente os resultados de que precisa. Não se trata apenas de fazer perguntas — mas de estruturar pedidos para que o modelo compreenda exatamente o que quer e como entregar isso.

Pense nisso como dar instruções a um colega. “Corrige o erro” é vago. “Corrige a exceção de ponteiro nulo no UserService.java linha 45 adicionando uma verificação nula” é específico. Os modelos de linguagem funcionam da mesma forma — especificidade e estrutura são importantes.

<img src="../../../translated_images/pt-PT/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="Como o LangChain4j se Encaixa" width="800"/>

O LangChain4j fornece a infraestrutura — ligações ao modelo, memória e tipos de mensagem — enquanto os padrões de prompt são apenas texto cuidadosamente estruturado que envia através dessa infraestrutura. Os principais blocos de construção são `SystemMessage` (que define o comportamento e papel da IA) e `UserMessage` (que transporta o seu pedido real).

## Fundamentos da Engenharia de Prompts

<img src="../../../translated_images/pt-PT/five-patterns-overview.160f35045ffd2a94.webp" alt="Visão Geral dos Cinco Padrões de Engenharia de Prompts" width="800"/>

Antes de mergulhar nos padrões avançados deste módulo, vamos rever cinco técnicas fundamentais de prompting. Estes são os blocos de construção que todo engenheiro de prompts deve conhecer. Se já trabalhou no [módulo de arranque rápido](../00-quick-start/README.md#2-prompt-patterns), já viu estes em ação — aqui está a estrutura conceptual por detrás deles.

### Prompting Zero-Shot

A abordagem mais simples: dar ao modelo uma instrução direta sem exemplos. O modelo baseia-se inteiramente no seu treino para compreender e executar a tarefa. Funciona bem para pedidos simples onde o comportamento esperado é óbvio.

<img src="../../../translated_images/pt-PT/zero-shot-prompting.7abc24228be84e6c.webp" alt="Prompting Zero-Shot" width="800"/>

*Instrução direta sem exemplos — o modelo infere a tarefa só pela instrução*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// Resposta: "Positivo"
```

**Quando usar:** Classificações simples, perguntas diretas, traduções, ou qualquer tarefa que o modelo consiga fazer sem orientação adicional.

### Prompting Few-Shot

Forneça exemplos que demonstrem o padrão que pretende que o modelo siga. O modelo aprende o formato esperado de entrada e saída pelos seus exemplos e aplica-o a novas entradas. Isto melhora muito a consistência para tarefas onde o formato ou comportamento desejado não é óbvio.

<img src="../../../translated_images/pt-PT/few-shot-prompting.9d9eace1da88989a.webp" alt="Prompting Few-Shot" width="800"/>

*Aprender com exemplos — o modelo identifica o padrão e aplica-o a novas entradas*

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

### Chain of Thought

Peça ao modelo para mostrar o seu raciocínio passo a passo. Em vez de saltar diretamente para uma resposta, o modelo divide o problema e trabalha cada parte explicitamente. Isto melhora a precisão em matemática, lógica e tarefas de raciocínio multi-passo.

<img src="../../../translated_images/pt-PT/chain-of-thought.5cff6630e2657e2a.webp" alt="Prompting Chain of Thought" width="800"/>

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

**Quando usar:** Problemas matemáticos, puzzles lógicos, depuração, ou qualquer tarefa onde mostrar o raciocínio melhora a precisão e a confiança.

### Prompting Baseado em Papéis

Defina uma persona ou papel para a IA antes de fazer a sua pergunta. Isto fornece contexto que molda o tom, profundidade e foco da resposta. Um “arquiteto de software” dá conselhos diferentes de um “desenvolvedor júnior” ou “auditor de segurança”.

<img src="../../../translated_images/pt-PT/role-based-prompting.a806e1a73de6e3a4.webp" alt="Prompting Baseado em Papéis" width="800"/>

*Definir contexto e persona — a mesma pergunta obtém respostas diferentes consoante o papel atribuído*

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

**Quando usar:** Revisões de código, tutoria, análises específicas de domínio, ou quando precisa de respostas adaptadas a um nível de especialização ou perspetiva particular.

### Modelos de Prompt

Crie prompts reutilizáveis com espaços variáveis. Em vez de escrever um novo prompt a cada vez, defina um modelo uma vez e preencha valores diferentes. A classe `PromptTemplate` do LangChain4j facilita isto com sintaxe `{{variable}}`.

<img src="../../../translated_images/pt-PT/prompt-templates.14bfc37d45f1a933.webp" alt="Modelos de Prompt" width="800"/>

*Prompts reutilizáveis com espaços variáveis — um modelo, muitas utilizações*

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

**Quando usar:** Consultas repetidas com entradas diferentes, processamento em lote, construção de fluxos de trabalho de IA reutilizáveis, ou qualquer cenário onde a estrutura do prompt mantém-se igual mas os dados mudam.

---

Estes cinco fundamentos dão-lhe uma caixa de ferramentas sólida para a maioria das tarefas de prompting. O resto deste módulo expande-os com **oito padrões avançados** que exploram o controlo de raciocínio do GPT-5.2, autoavaliação e capacidades de saída estruturada.

## Padrões Avançados

Com os fundamentos tratados, passemos aos oito padrões avançados que tornam este módulo único. Nem todos os problemas precisam da mesma abordagem. Algumas perguntas exigem respostas rápidas, outras um pensamento profundo. Algumas precisam de raciocínio visível, outras só resultados. Cada padrão abaixo está otimizado para um cenário diferente — e o controlo de raciocínio do GPT-5.2 torna essas diferenças ainda mais evidentes.

<img src="../../../translated_images/pt-PT/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Oito Padrões de Prompting" width="800"/>

*Visão geral dos oito padrões de engenharia de prompts e seus casos de uso*

<img src="../../../translated_images/pt-PT/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Controlo de Raciocínio com GPT-5.2" width="800"/>

*O controlo de raciocínio do GPT-5.2 permite especificar quanto pensamento o modelo deve fazer — desde respostas rápidas diretas a explorações profundas*

<img src="../../../translated_images/pt-PT/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Comparação do Esforço de Raciocínio" width="800"/>

*Baixa disposição (rápido, direto) vs Alta disposição (completo, exploratório) nas abordagens de raciocínio*

**Baixa Disposição (Rápido e Focado)** - Para perguntas simples onde quer respostas rápidas e diretas. O modelo faz raciocínio mínimo — máximo 2 passos. Use para cálculos, pesquisas ou perguntas diretas.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Explore com GitHub Copilot:** Abra [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) e pergunte:
> - "Qual é a diferença entre os padrões de prompting de baixa disposição e alta disposição?"
> - "Como é que as etiquetas XML nos prompts ajudam a estruturar a resposta da IA?"
> - "Quando devo usar padrões de auto-reflexão versus instrução direta?"

**Alta Disposição (Profundo e Completo)** - Para problemas complexos onde quer análise abrangente. O modelo explora a fundo e mostra raciocínio detalhado. Use para design de sistemas, decisões de arquitetura ou pesquisa complexa.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Execução de Tarefas (Progresso Passo a Passo)** - Para fluxos de trabalho multi-step. O modelo fornece um plano inicial, narra cada passo à medida que trabalha, depois dá um resumo. Use para migrações, implementações ou qualquer processo multi-step.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

O prompting Chain-of-Thought pede explicitamente ao modelo para mostrar o seu processo de raciocínio, melhorando a precisão para tarefas complexas. A divisão passo a passo ajuda humanos e IA a entender a lógica.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Pergunte sobre este padrão:
> - "Como adapto o padrão de execução de tarefas para operações demoradas?"
> - "Quais são as melhores práticas para estruturar preâmbulos de ferramentas em aplicações de produção?"
> - "Como posso capturar e mostrar atualizações intermédias de progresso numa interface?"

<img src="../../../translated_images/pt-PT/task-execution-pattern.9da3967750ab5c1e.webp" alt="Padrão de Execução de Tarefas" width="800"/>

*Plano → Executar → Resumir fluxo para tarefas multi-step*

**Código Auto-Reflexivo** - Para gerar código de qualidade de produção. O modelo gera código, verifica contra critérios de qualidade e melhora iterativamente. Use ao construir novas funcionalidades ou serviços.

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

<img src="../../../translated_images/pt-PT/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Ciclo de Auto-Reflexão" width="800"/>

*Loop de melhoria iterativa - gerar, avaliar, identificar problemas, melhorar, repetir*

**Análise Estruturada** - Para avaliação consistente. O modelo revê código usando uma estrutura fixa (correção, práticas, desempenho, segurança). Use para revisões de código ou avaliações de qualidade.

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

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Pergunte sobre análise estruturada:
> - "Como personalizo a estrutura de análise para diferentes tipos de revisões de código?"
> - "Qual a melhor forma de analisar e agir sobre saídas estruturadas programaticamente?"
> - "Como garantir níveis consistentes de severidade em sessões de revisão diferentes?"

<img src="../../../translated_images/pt-PT/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Padrão de Análise Estruturada" width="800"/>

*Estrutura de quatro categorias para revisões de código consistentes com níveis de severidade*

**Chat Multi-Turno** - Para conversas que precisam de contexto. O modelo recorda mensagens anteriores e constrói sobre elas. Use para sessões de ajuda interativas ou Q&A complexas.

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

*Como o contexto da conversa acumula através de múltiplos turnos até atingir o limite de tokens*

**Raciocínio Passo a Passo** - Para problemas que exigem lógica visível. O modelo mostra raciocínio explícito para cada passo. Use para problemas matemáticos, puzzles lógicos ou quando precisa de compreender o processo de pensamento.

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

**Saída Constrangida** - Para respostas com requisitos de formato específicos. O modelo segue rigorosamente regras de formato e comprimento. Use para sumários ou quando precisa de estrutura de saída precisa.

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

<img src="../../../translated_images/pt-PT/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Padrão de Saída Constrangida" width="800"/>

*Aplicação rigorosa de requisitos de formato, comprimento e estrutura*

## Utilizar Recursos Azure Existentes

**Verificar implementação:**

Assegure que existe o ficheiro `.env` na raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está a correr na porta 8083. Pode saltar os comandos de arranque abaixo e ir diretamente para http://localhost:8083.

**Opção 1: Usar o Painel Spring Boot (Recomendado para utilizadores VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que providencia uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades do lado esquerdo do VS Code (procure o ícone do Spring Boot).
A partir do Painel Spring Boot, pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um único clique
- Visualizar os registos da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de reprodução ao lado de "prompt-engineering" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz e vão construir os JARs se estes não existirem.

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

## Capturas de Ecrã da Aplicação

<img src="../../../translated_images/pt-PT/dashboard-home.5444dbda4bc1f79d.webp" alt="Página Inicial do Painel" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*O painel principal mostra os 8 padrões de engenharia de prompt com as suas características e casos de uso*

## Explorar os Padrões

A interface web permite-lhe experimentar diferentes estratégias de prompt. Cada padrão resolve problemas distintos – experimente-os para ver quando cada abordagem brilha.

### Baixa vs Alta Urgência

Faça uma pergunta simples como "Qual é 15% de 200?" usando Baixa Urgência. Vai obter uma resposta instantânea e direta. Agora faça algo complexo como "Desenhe uma estratégia de cache para uma API com elevado tráfego" usando Alta Urgência. Veja como o modelo desacelera e fornece raciocínio detalhado. Mesmo modelo, mesma estrutura de pergunta – mas o prompt indica quanto deve pensar.

<img src="../../../translated_images/pt-PT/low-eagerness-demo.898894591fb23aa0.webp" alt="Demonstração Baixa Urgência" width="800"/>

*Cálculo rápido com raciocínio mínimo*

<img src="../../../translated_images/pt-PT/high-eagerness-demo.4ac93e7786c5a376.webp" alt="Demonstração Alta Urgência" width="800"/>

*Estratégia de cache abrangente (2.8MB)*

### Execução de Tarefas (Preâmbulos de Ferramentas)

Fluxos de trabalho multi-etapas beneficiam de planeamento prévio e narração do progresso. O modelo descreve o que vai fazer, narra cada passo, depois resume os resultados.

<img src="../../../translated_images/pt-PT/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Demonstração Execução de Tarefas" width="800"/>

*Criação de um endpoint REST com narração passo a passo (3.9MB)*

### Código Auto-Reflexivo

Experimente "Criar um serviço de validação de email". Em vez de gerar código e parar, o modelo gera, avalia contra critérios de qualidade, identifica falhas e melhora. Vai vê-lo iterar até o código cumprir os padrões de produção.

<img src="../../../translated_images/pt-PT/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Demonstração Código Auto-Reflexivo" width="800"/>

*Serviço completo de validação de email (5.2MB)*

### Análise Estruturada

Revisões de código necessitam de quadros de avaliação consistentes. O modelo analisa o código usando categorias fixas (correção, práticas, desempenho, segurança) com níveis de severidade.

<img src="../../../translated_images/pt-PT/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Demonstração Análise Estruturada" width="800"/>

*Revisão de código baseada em framework*

### Chat Multi-Turno

Pergunte "O que é Spring Boot?" e depois imediatamente "Mostra-me um exemplo". O modelo lembra-se da sua primeira pergunta e dá-lhe um exemplo específico de Spring Boot. Sem memória, essa segunda pergunta seria demasiado vaga.

<img src="../../../translated_images/pt-PT/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Demonstração Chat Multi-Turno" width="800"/>

*Preservação de contexto entre perguntas*

### Raciocínio Passo a Passo

Escolha um problema de matemática e tente com Raciocínio Passo a Passo e Baixa Urgência. Baixa urgência só dá a resposta – rápida mas opaca. Passo a passo mostra todos os cálculos e decisões.

<img src="../../../translated_images/pt-PT/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Demonstração Raciocínio Passo a Passo" width="800"/>

*Problema matemático com passos explícitos*

### Saída Constrangida

Quando precisa de formatos específicos ou contagem de palavras, este padrão impõe estrita conformidade. Experimente gerar um resumo com exatamente 100 palavras em formato de tópicos.

<img src="../../../translated_images/pt-PT/constrained-output-demo.567cc45b75da1633.webp" alt="Demonstração Saída Constrangida" width="800"/>

*Resumo de aprendizagem automática com controlo de formato*

## O que Está Realmente a Aprender

**O Esforço no Raciocínio Muda Tudo**

O GPT-5.2 permite controlar o esforço computacional através dos seus prompts. Baixo esforço significa respostas rápidas com exploração mínima. Alto esforço significa que o modelo demora a pensar profundamente. Está a aprender a adequar o esforço à complexidade da tarefa – não desperdice tempo em perguntas simples, mas também não apresse decisões complexas.

**A Estrutura Guia o Comportamento**

Reparou nas etiquetas XML nos prompts? Não são decorativas. Os modelos seguem instruções estruturadas de forma mais fiável do que texto livre. Quando precisa de processos multi-etapas ou lógica complexa, a estrutura ajuda o modelo a perceber onde está e o que vem a seguir.

<img src="../../../translated_images/pt-PT/prompt-structure.a77763d63f4e2f89.webp" alt="Estrutura do Prompt" width="800"/>

*Anatomia de um prompt bem estruturado com secções claras e organização em estilo XML*

**Qualidade Através da Autoavaliação**

Os padrões auto-reflexivos funcionam tornando explícitos os critérios de qualidade. Em vez de esperar que o modelo "faça bem", diz-lhe exatamente o que significa "bem": lógica correta, tratamento de erros, desempenho, segurança. O modelo pode então avaliar a sua própria saída e melhorar. Isto transforma a geração de código de uma lotaria num processo.

**O Contexto é Finito**

Conversas multi-turno funcionam incluindo o histórico de mensagens em cada pedido. Mas há um limite – cada modelo tem um valor máximo de tokens. À medida que as conversas crescem, vai precisar de estratégias para manter o contexto relevante sem atingir esse limite. Este módulo mostra-lhe como a memória funciona; mais tarde vai aprender quando resumir, quando esquecer e quando recuperar.

## Próximos Passos

**Próximo Módulo:** [03-rag - RAG (Geração Aumentada por Recuperação)](../03-rag/README.md)

---

**Navegação:** [← Anterior: Módulo 01 - Introdução](../01-introduction/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor, tenha em atenção que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->