# Módulo 04: Agentes de IA com Ferramentas

## Índice

- [O Que Vai Aprender](../../../04-tools)
- [Pré-requisitos](../../../04-tools)
- [Compreender Agentes de IA com Ferramentas](../../../04-tools)
- [Como Funciona a Chamada a Ferramentas](../../../04-tools)
  - [Definições de Ferramentas](../../../04-tools)
  - [Tomada de Decisão](../../../04-tools)
  - [Execução](../../../04-tools)
  - [Geração de Resposta](../../../04-tools)
  - [Arquitetura: Injeção Automática do Spring Boot](../../../04-tools)
- [Encadeamento de Ferramentas](../../../04-tools)
- [Executar a Aplicação](../../../04-tools)
- [Usar a Aplicação](../../../04-tools)
  - [Experimente o Uso Simples de Ferramentas](../../../04-tools)
  - [Teste o Encadeamento de Ferramentas](../../../04-tools)
  - [Veja o Fluxo da Conversa](../../../04-tools)
  - [Experimente Pedidos Diferentes](../../../04-tools)
- [Conceitos-chave](../../../04-tools)
  - [Padrão ReAct (Raciocínio e Ação)](../../../04-tools)
  - [Descrições de Ferramentas Importam](../../../04-tools)
  - [Gestão de Sessão](../../../04-tools)
  - [Tratamento de Erros](../../../04-tools)
- [Ferramentas Disponíveis](../../../04-tools)
- [Quando Usar Agentes Baseados em Ferramentas](../../../04-tools)
- [Ferramentas vs RAG](../../../04-tools)
- [Próximos Passos](../../../04-tools)

## O Que Vai Aprender

Até agora, aprendeu como ter conversas com IA, estruturar prompts de forma eficaz e fundamentar respostas nos seus documentos. Mas ainda existe uma limitação fundamental: os modelos de linguagem só conseguem gerar texto. Eles não conseguem consultar o tempo, realizar cálculos, consultar bases de dados ou interagir com sistemas externos.

As ferramentas mudam isto. Ao dar ao modelo acesso a funções que pode invocar, transforma-o de um gerador de texto num agente que pode tomar ações. O modelo decide quando precisa de uma ferramenta, qual usar e quais parâmetros passar. O seu código executa a função e devolve o resultado. O modelo incorpora esse resultado na sua resposta.

## Pré-requisitos

- Conclusão do Módulo 01 (recursos Azure OpenAI implantados)
- Ficheiro `.env` na diretoria raiz com credenciais Azure (criado por `azd up` no Módulo 01)

> **Nota:** Se ainda não completou o Módulo 01, siga primeiro as instruções de implantação aí indicadas.

## Compreender Agentes de IA com Ferramentas

> **📝 Nota:** O termo "agentes" neste módulo refere-se a assistentes de IA melhorados com capacidades de invocação de ferramentas. Isto é diferente dos padrões **Agentic AI** (agentes autónomos com planeamento, memória e raciocínio multi-etapa) que abordaremos no [Módulo 05: MCP](../05-mcp/README.md).

Sem ferramentas, um modelo de linguagem só consegue gerar texto a partir dos seus dados de treino. Pergunte-lhe qual é o tempo atual e só consegue adivinhar. Dê-lhe ferramentas e ele pode invocar uma API de meteorologia, realizar cálculos ou consultar uma base de dados — e depois integrar esses resultados reais na sua resposta.

<img src="../../../translated_images/pt-PT/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Sem ferramentas, o modelo só pode adivinhar — com ferramentas, pode chamar APIs, fazer cálculos e devolver dados em tempo real.*

Um agente de IA com ferramentas segue um padrão de **Raciocínio e Ação (ReAct)**. O modelo não se limita a responder — pensa no que precisa, atua ao invocar uma ferramenta, observa o resultado e decide se deve atuar novamente ou entregar a resposta final:

1. **Raciocina** — O agente analisa a questão do utilizador e determina que informação precisa
2. **Age** — O agente seleciona a ferramenta correta, gera os parâmetros apropriados e invoca a ferramenta
3. **Observa** — O agente recebe a saída da ferramenta e avalia o resultado
4. **Repete ou Responde** — Se precisar de mais dados, o agente repete o ciclo; caso contrário, compõe uma resposta em linguagem natural

<img src="../../../translated_images/pt-PT/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*O ciclo ReAct — o agente raciocina sobre o que fazer, age chamando uma ferramenta, observa o resultado e repete até poder dar a resposta final.*

Isto acontece automaticamente. Você define as ferramentas e as suas descrições. O modelo trata da tomada de decisão sobre quando e como usá-las.

## Como Funciona a Chamada a Ferramentas

### Definições de Ferramentas

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Define funções com descrições claras e especificações dos parâmetros. O modelo vê essas descrições no seu prompt de sistema e compreende o que cada ferramenta faz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // A sua lógica de consulta do tempo
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// O assistente é automaticamente configurado pelo Spring Boot com:
// - Bean ChatModel
// - Todos os métodos @Tool das classes @Component
// - ChatMemoryProvider para gestão de sessões
```

O diagrama abaixo detalha cada anotação e mostra como cada parte ajuda a IA a entender quando chamar a ferramenta e que argumentos passar:

<img src="../../../translated_images/pt-PT/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Anatomia de uma definição de ferramenta — @Tool indica à IA quando a usar, @P descreve cada parâmetro, e @AiService liga tudo no arranque.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e pergunte:
> - "Como integraria uma API real de meteorologia como a OpenWeatherMap em vez de dados simulados?"
> - "O que torna uma boa descrição de ferramenta que ajuda a IA a usá-la corretamente?"
> - "Como devo tratar erros da API e limites de taxa nas implementações das ferramentas?"

### Tomada de Decisão

Quando um utilizador pergunta "Qual é o tempo em Seattle?", o modelo não escolhe uma ferramenta ao acaso. Compara a intenção do utilizador com cada descrição de ferramenta acessível, pontua cada uma pela relevância e seleciona a melhor correspondência. Depois gera uma chamada de função estruturada com os parâmetros corretos — neste caso, definindo `location` para `"Seattle"`.

Se nenhuma ferramenta corresponder ao pedido do utilizador, o modelo responde baseado no seu próprio conhecimento. Se várias ferramentas corresponderem, escolhe a mais específica.

<img src="../../../translated_images/pt-PT/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*O modelo avalia cada ferramenta disponível contra a intenção do utilizador e seleciona a melhor — é por isso que escrever descrições claras e específicas das ferramentas é importante.*

### Execução

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

O Spring Boot injeta automaticamente a interface declarativa `@AiService` com todas as ferramentas registadas, e o LangChain4j executa as chamadas às ferramentas automaticamente. Nos bastidores, uma chamada completa a uma ferramenta percorre seis etapas — da pergunta em linguagem natural do utilizador até à resposta também em linguagem natural:

<img src="../../../translated_images/pt-PT/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*O fluxo de ponta a ponta — o utilizador faz uma pergunta, o modelo seleciona uma ferramenta, o LangChain4j executa-a, e o modelo integra o resultado numa resposta natural.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e pergunte:
> - "Como funciona o padrão ReAct e por que é eficaz para agentes de IA?"
> - "Como o agente decide que ferramenta usar e em que ordem?"
> - "O que acontece se uma execução de ferramenta falhar — como tratar erros de forma robusta?"

### Geração de Resposta

O modelo recebe os dados meteorológicos e formata-os numa resposta em linguagem natural para o utilizador.

### Arquitetura: Injeção Automática do Spring Boot

Este módulo usa a integração do LangChain4j com Spring Boot com interfaces declarativas `@AiService`. No arranque, o Spring Boot descobre todos os `@Component` que contenham métodos `@Tool`, o seu bean `ChatModel`, e o `ChatMemoryProvider` — e liga tudo numa única interface `Assistant` sem necessidade de código repetitivo.

<img src="../../../translated_images/pt-PT/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*A interface @AiService liga o ChatModel, componentes de ferramentas e o fornecedor de memória — o Spring Boot trata de toda a injeção automaticamente.*

Principais vantagens deste método:

- **Injeção automática do Spring Boot** — ChatModel e ferramentas injetadas automaticamente
- **Padrão @MemoryId** — Gestão automática de memória baseada em sessões
- **Instância única** — Assistente criado uma vez e reutilizado para melhor desempenho
- **Execução com segurança de tipos** — Métodos Java chamados diretamente com conversão de tipos
- **Orquestração multi-turno** — Gere o encadeamento de ferramentas automaticamente
- **Zero repetição de código** — Sem chamadas manuais a `AiServices.builder()` ou mapas de memória HashMap

Abordagens alternativas (com `AiServices.builder()` manual) requerem mais código e perdem os benefícios da integração Spring Boot.

## Encadeamento de Ferramentas

**Encadeamento de Ferramentas** — O verdadeiro poder dos agentes baseados em ferramentas manifesta-se quando uma única pergunta exige várias ferramentas. Pergunte "Qual é o tempo em Seattle em Fahrenheit?" e o agente automaticamente encadeia duas ferramentas: primeiro chama `getCurrentWeather` para obter a temperatura em Celsius, depois passa esse valor para `celsiusToFahrenheit` para a conversão — tudo numa única interação da conversa.

<img src="../../../translated_images/pt-PT/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Encadeamento de ferramentas em ação — o agente chama primeiro getCurrentWeather, depois passa o resultado em Celsius para celsiusToFahrenheit, entregando uma resposta combinada.*

Eis como isto aparece na aplicação a funcionar — o agente encadeia duas chamadas de ferramentas numa só interação da conversa:

<a href="images/tool-chaining.png"><img src="../../../translated_images/pt-PT/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Saída real da aplicação — o agente encadeia automaticamente getCurrentWeather → celsiusToFahrenheit numa interação.*

**Falhas Elegantes** — Peça o tempo numa cidade que não exista nos dados simulados. A ferramenta devolve uma mensagem de erro, e a IA explica que não consegue ajudar em vez de falhar. As ferramentas falham de forma segura.

<img src="../../../translated_images/pt-PT/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Quando uma ferramenta falha, o agente deteta o erro e responde com uma explicação útil em vez de crashar.*

Isto sucede numa só interação da conversa. O agente orquestra múltiplas chamadas a ferramentas autonomamente.

## Executar a Aplicação

**Verifique a implantação:**

Certifique-se que o ficheiro `.env` existe na diretoria raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se já iniciou todas as aplicações com `./start-all.sh` do Módulo 01, este módulo já está a correr na porta 8084. Pode saltar os comandos de arranque abaixo e ir diretamente a http://localhost:8084.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades no lado esquerdo do VS Code (procure o ícone do Spring Boot).

A partir do Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um clique
- Ver os logs da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de play ao lado de "tools" para iniciar este módulo, ou iniciar todos os módulos em simultâneo.

<img src="../../../translated_images/pt-PT/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usar scripts shell**

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
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz e constroem os JARs se não existirem.

> **Nota:** Se preferir compilar manualmente todos os módulos antes de iniciar:
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

Abra http://localhost:8084 no seu navegador.

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

## Usar a Aplicação

A aplicação oferece uma interface web onde pode interagir com um agente de IA que tem acesso a ferramentas de meteorologia e conversão de temperatura.

<a href="images/tools-homepage.png"><img src="../../../translated_images/pt-PT/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface das Ferramentas do Agente de IA — exemplos rápidos e interface de chat para interagir com as ferramentas*

### Experimente o Uso Simples de Ferramentas
Comece com um pedido simples: "Converta 100 graus Fahrenheit para Celsius". O agente reconhece que precisa da ferramenta de conversão de temperatura, chama-a com os parâmetros corretos e devolve o resultado. Repare como isto parece natural – você não especificou qual ferramenta usar nem como a chamar.

### Teste de Encadeamento de Ferramentas

Agora experimente algo mais complexo: "Qual é o tempo em Seattle e converta para Fahrenheit?" Observe o agente a trabalhar isto em etapas. Primeiro obtém o tempo (que retorna em Celsius), reconhece que precisa converter para Fahrenheit, chama a ferramenta de conversão e combina ambos os resultados numa única resposta.

### Veja o Fluxo da Conversa

A interface de chat mantém o histórico da conversa, permitindo-lhe ter interações multi-turno. Pode ver todas as perguntas e respostas anteriores, facilitando o acompanhamento da conversa e a compreensão de como o agente constrói o contexto ao longo de múltiplas trocas.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pt-PT/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversa com Chamadas Múltiplas a Ferramentas" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversa multi-turno mostrando conversões simples, consultas meteorológicas e encadeamento de ferramentas*

### Experimente com Pedidos Diferentes

Tente várias combinações:
- Consultas meteorológicas: "Qual é o tempo em Tóquio?"
- Conversões de temperatura: "Quanto é 25°C em Kelvin?"
- Consultas combinadas: "Verifica o tempo em Paris e diz-me se está acima de 20°C"

Repare como o agente interpreta a linguagem natural e a mapeia para chamadas apropriadas às ferramentas.

## Conceitos-Chave

### Padrão ReAct (Raciocínio e Ação)

O agente alterna entre raciocinar (decidir o que fazer) e agir (usar ferramentas). Este padrão permite a resolução autónoma de problemas em vez de apenas responder a instruções.

### As Descrições das Ferramentas Importam

A qualidade das descrições das suas ferramentas afeta diretamente a eficácia com que o agente as usa. Descrições claras e específicas ajudam o modelo a entender quando e como chamar cada ferramenta.

### Gestão de Sessões

A anotação `@MemoryId` permite a gestão automática de memória baseada em sessões. Cada ID de sessão obtém a sua própria instância de `ChatMemory` gerida pelo bean `ChatMemoryProvider`, permitindo que múltiplos utilizadores interajam com o agente simultaneamente sem misturar as suas conversas.

<img src="../../../translated_images/pt-PT/session-management.91ad819c6c89c400.webp" alt="Gestão de Sessão com @MemoryId" width="800"/>

*Cada ID de sessão corresponde a um histórico de conversa isolado — os utilizadores nunca veem as mensagens uns dos outros.*

### Gestão de Erros

As ferramentas podem falhar — APIs podem expirar, parâmetros podem ser inválidos, serviços externos podem parar. Agentes em produção precisam de gestão de erros para que o modelo possa explicar problemas ou tentar alternativas em vez de bloquear toda a aplicação. Quando uma ferramenta lança uma exceção, o LangChain4j captura-a e devolve a mensagem de erro ao modelo, que pode então explicar o problema em linguagem natural.

## Ferramentas Disponíveis

O diagrama abaixo mostra o amplo ecossistema de ferramentas que pode construir. Este módulo demonstra ferramentas de tempo e temperatura, mas o mesmo padrão `@Tool` funciona para qualquer método Java — desde consultas a bases de dados até processamento de pagamentos.

<img src="../../../translated_images/pt-PT/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecossistema de Ferramentas" width="800"/>

*Qualquer método Java anotado com @Tool torna-se disponível para a IA — o padrão estende-se a bases de dados, APIs, email, operações com ficheiros e mais.*

## Quando Usar Agentes Baseados em Ferramentas

<img src="../../../translated_images/pt-PT/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quando Usar Ferramentas" width="800"/>

*Guia rápido de decisão — ferramentas são para dados em tempo real, cálculos e ações; conhecimento geral e tarefas criativas não precisam delas.*

**Use ferramentas quando:**
- A resposta necessitar de dados em tempo real (tempo, preços da bolsa, inventário)
- Precisar realizar cálculos além da matemática simples
- Aceder a bases de dados ou APIs
- Executar ações (enviar emails, criar tickets, atualizar registos)
- Combinar múltiplas fontes de dados

**Não use ferramentas quando:**
- As perguntas possam ser respondidas com conhecimento geral
- A resposta for puramente conversacional
- A latência da ferramenta tornar a experiência demasiado lenta

## Ferramentas vs RAG

Os módulos 03 e 04 estendem ambos o que a IA pode fazer, mas de formas fundamentalmente diferentes. O RAG dá ao modelo acesso a **conhecimento** através da recuperação de documentos. As ferramentas dão ao modelo a capacidade de tomar **ações** chamando funções.

<img src="../../../translated_images/pt-PT/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparação Ferramentas vs RAG" width="800"/>

*O RAG recupera informação de documentos estáticos — As Ferramentas executam ações e obtêm dados dinâmicos e em tempo real. Muitos sistemas em produção combinam ambos.*

Na prática, muitos sistemas em produção combinam ambas as abordagens: RAG para basear respostas na sua documentação, e Ferramentas para obter dados ao vivo ou realizar operações.

## Próximos Passos

**Próximo Módulo:** [05-mcp - Protocolo de Contexto de Modelo (MCP)](../05-mcp/README.md)

---

**Navegação:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos empenhemos em garantir a precisão, por favor tenha em conta que traduções automatizadas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte fidedigna. Para informações críticas, recomenda-se a tradução profissional realizada por um humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas resultantes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->