# Módulo 04: Agentes de IA com Ferramentas

## Índice

- [Vídeo Tutorial](../../../04-tools)
- [O que você vai aprender](../../../04-tools)
- [Pré-requisitos](../../../04-tools)
- [Entendendo Agentes de IA com Ferramentas](../../../04-tools)
- [Como Funciona a Chamada de Ferramentas](../../../04-tools)
  - [Definições de Ferramentas](../../../04-tools)
  - [Tomada de Decisão](../../../04-tools)
  - [Execução](../../../04-tools)
  - [Geração de Respostas](../../../04-tools)
  - [Arquitetura: Injeção Automática do Spring Boot](../../../04-tools)
- [Encadeamento de Ferramentas](../../../04-tools)
- [Executar a Aplicação](../../../04-tools)
- [Usando a Aplicação](../../../04-tools)
  - [Tente Uso Simples de Ferramentas](../../../04-tools)
  - [Teste o Encadeamento de Ferramentas](../../../04-tools)
  - [Veja o Fluxo de Conversa](../../../04-tools)
  - [Experimente com Pedidos Diferentes](../../../04-tools)
- [Conceitos-Chave](../../../04-tools)
  - [Padrão ReAct (Raciocínio e Ação)](../../../04-tools)
  - [Descrições de Ferramentas Importam](../../../04-tools)
  - [Gerenciamento de Sessão](../../../04-tools)
  - [Tratamento de Erros](../../../04-tools)
- [Ferramentas Disponíveis](../../../04-tools)
- [Quando Usar Agentes Baseados em Ferramentas](../../../04-tools)
- [Ferramentas vs RAG](../../../04-tools)
- [Próximos Passos](../../../04-tools)

## Vídeo Tutorial

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="Agentes de IA com Ferramentas e MCP - Sessão ao Vivo" width="800"/></a>

## O que você vai aprender

Até agora, você aprendeu a ter conversas com IA, estruturar prompts efetivamente e fundamentar respostas em seus documentos. Mas ainda existe uma limitação fundamental: modelos de linguagem só podem gerar texto. Eles não conseguem verificar o clima, realizar cálculos, consultar bancos de dados ou interagir com sistemas externos.

Ferramentas mudam isso. Ao dar ao modelo acesso a funções que ele pode chamar, você o transforma de um gerador de texto em um agente que pode tomar ações. O modelo decide quando precisa de uma ferramenta, qual usar e quais parâmetros passar. Seu código executa a função e retorna o resultado. O modelo incorpora esse resultado em sua resposta.

## Pré-requisitos

- Concluído o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implantados)
- Recomendado ter completado os módulos anteriores (este módulo faz referência aos [conceitos RAG do Módulo 03](../03-rag/README.md) na comparação Ferramentas vs RAG)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não concluiu o Módulo 01, siga primeiro as instruções de implantação lá.

## Entendendo Agentes de IA com Ferramentas

> **📝 Nota:** O termo "agentes" neste módulo se refere a assistentes de IA aprimorados com capacidades de chamada de ferramentas. Isso é diferente dos padrões de **Agentic AI** (agentes autônomos com planejamento, memória e raciocínio em múltiplas etapas) que cobriremos no [Módulo 05: MCP](../05-mcp/README.md).

Sem ferramentas, um modelo de linguagem só pode gerar texto baseado em seu treinamento. Pergunte o clima atual e ele tem que adivinhar. Dê ferramentas a ele, e pode chamar uma API de clima, realizar cálculos ou consultar um banco de dados — depois tecer esses resultados reais em sua resposta.

<img src="../../../translated_images/pt-BR/what-are-tools.724e468fc4de64da.webp" alt="Sem Ferramentas vs Com Ferramentas" width="800"/>

*Sem ferramentas, o modelo só pode adivinhar — com ferramentas ele pode chamar APIs, rodar cálculos e retornar dados em tempo real.*

Um agente de IA com ferramentas segue um padrão **Raciocínio e Ação (ReAct)**. O modelo não apenas responde — ele pensa no que precisa, age chamando uma ferramenta, observa o resultado e depois decide se age novamente ou entrega a resposta final:

1. **Raciocinar** — O agente analisa a pergunta do usuário e determina qual informação precisa
2. **Agir** — O agente seleciona a ferramenta correta, gera os parâmetros certos e a chama
3. **Observar** — O agente recebe a saída da ferramenta e avalia o resultado
4. **Repetir ou Responder** — Se precisar de mais dados, o agente retorna ao passo anterior; caso contrário, compõe uma resposta em linguagem natural

<img src="../../../translated_images/pt-BR/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Padrão ReAct" width="800"/>

*O ciclo ReAct — o agente raciocina sobre o que fazer, age chamando uma ferramenta, observa o resultado e repete até entregar a resposta final.*

Isso acontece automaticamente. Você define as ferramentas e suas descrições. O modelo cuida da tomada de decisão sobre quando e como usá-las.

## Como Funciona a Chamada de Ferramentas

### Definições de Ferramentas

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Você define funções com descrições claras e especificações de parâmetros. O modelo vê essas descrições em seu prompt do sistema e entende o que cada ferramenta faz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // Sua lógica de consulta do tempo
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// O assistente é automaticamente configurado pelo Spring Boot com:
// - Bean ChatModel
// - Todos os métodos @Tool de classes @Component
// - ChatMemoryProvider para gerenciamento de sessão
```

O diagrama abaixo detalha cada anotação e mostra como cada parte ajuda a IA a entender quando chamar a ferramenta e quais argumentos passar:

<img src="../../../translated_images/pt-BR/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia das Definições de Ferramentas" width="800"/>

*Anatomia de uma definição de ferramenta — @Tool indica à IA quando usá-la, @P descreve cada parâmetro e @AiService conecta tudo na inicialização.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e pergunte:
> - "Como eu integraria uma API real de clima como OpenWeatherMap ao invés de dados simulados?"
> - "O que faz uma boa descrição de ferramenta que ajuda a IA a usá-la corretamente?"
> - "Como lidar com erros de API e limites de requisição nas implementações de ferramentas?"

### Tomada de Decisão

Quando um usuário pergunta "Qual o clima em Seattle?", o modelo não escolhe uma ferramenta aleatoriamente. Ele compara a intenção do usuário contra cada descrição de ferramenta que possui, pontua cada uma por relevância e seleciona a melhor correspondência. Então gera uma chamada de função estruturada com os parâmetros certos — neste caso, definindo `location` como `"Seattle"`.

Se nenhuma ferramenta corresponder ao pedido do usuário, o modelo recai para responder com seu próprio conhecimento. Se múltiplas ferramentas corresponderem, ele escolhe a mais específica.

<img src="../../../translated_images/pt-BR/decision-making.409cd562e5cecc49.webp" alt="Como a IA Decide Qual Ferramenta Usar" width="800"/>

*O modelo avalia todas as ferramentas disponíveis contra a intenção do usuário e seleciona a melhor correspondência — por isso escrever descrições claras e específicas para ferramentas importa.*

### Execução

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot injeta automaticamente a interface declarativa `@AiService` com todas as ferramentas registradas, e LangChain4j executa as chamadas de ferramentas automaticamente. Nos bastidores, uma chamada de ferramenta completa passa por seis etapas — desde a pergunta em linguagem natural do usuário até a resposta também em linguagem natural:

<img src="../../../translated_images/pt-BR/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxo de Chamada de Ferramentas" width="800"/>

*O fluxo de ponta a ponta — o usuário faz uma pergunta, o modelo seleciona uma ferramenta, LangChain4j a executa e o modelo incorpora o resultado em uma resposta natural.*

Se você executou o [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) no Módulo 00, já viu esse padrão em ação — as ferramentas `Calculator` foram chamadas da mesma forma. O diagrama de sequência abaixo mostra exatamente o que aconteceu nos bastidores durante aquela demonstração:

<img src="../../../translated_images/pt-BR/tool-calling-sequence.94802f406ca26278.webp" alt="Diagrama de Sequência de Chamada de Ferramentas" width="800"/>

*O ciclo de chamada de ferramentas do demo Quick Start — `AiServices` envia sua mensagem e esquemas de ferramentas ao LLM, o LLM responde com uma chamada de função como `add(42, 58)`, LangChain4j executa o método `Calculator` localmente e envia o resultado de volta para a resposta final.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e pergunte:
> - "Como funciona o padrão ReAct e por que ele é eficaz para agentes de IA?"
> - "Como o agente decide qual ferramenta usar e em que ordem?"
> - "O que acontece se uma execução de ferramenta falhar — como devo tratar erros de forma robusta?"

### Geração de Respostas

O modelo recebe os dados do clima e os formata em uma resposta em linguagem natural para o usuário.

### Arquitetura: Injeção Automática do Spring Boot

Este módulo usa a integração do LangChain4j com Spring Boot por meio de interfaces declarativas `@AiService`. Na inicialização, o Spring Boot descobre cada `@Component` que contenha métodos anotados com `@Tool`, seu bean `ChatModel` e o `ChatMemoryProvider` — então os conecta todos numa única interface `Assistant` sem necessidade de código repetitivo.

<img src="../../../translated_images/pt-BR/spring-boot-wiring.151321795988b04e.webp" alt="Arquitetura de Injeção Automática do Spring Boot" width="800"/>

*A interface @AiService conecta o ChatModel, componentes de ferramenta e provedor de memória — o Spring Boot cuida de toda a injeção automaticamente.*

Aqui está o ciclo completo da requisição em um diagrama de sequência — desde o pedido HTTP através do controlador, serviço e proxy injetado, até a execução da ferramenta e retorno:

<img src="../../../translated_images/pt-BR/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sequência de Chamada de Ferramenta no Spring Boot" width="800"/>

*O ciclo completo da requisição no Spring Boot — o pedido HTTP flui pelo controlador e serviço para o proxy Assistant injetado automaticamente, que orquestra o LLM e chamadas de ferramentas sem intervenção.*

Principais benefícios dessa abordagem:

- **Injeção automática do Spring Boot** — ChatModel e ferramentas são injetados automaticamente
- **Padrão @MemoryId** — Gerenciamento automático de memória baseada em sessão
- **Instância única** — Assistant criado uma vez e reutilizado para melhor desempenho
- **Execução tipada** — Métodos Java chamados diretamente com conversão de tipos
- **Orquestração multi-turno** — Controla encadeamento de ferramentas automaticamente
- **Zero código repetitivo** — Sem chamadas manuais `AiServices.builder()` ou HashMap de memória

Abordagens alternativas (manual `AiServices.builder()`) exigem mais código e não aproveitam os benefícios da integração com Spring Boot.

## Encadeamento de Ferramentas

**Encadeamento de Ferramentas** — O verdadeiro poder dos agentes baseados em ferramentas aparece quando uma única pergunta requer múltiplas ferramentas. Pergunte "Qual o clima em Seattle em Fahrenheit?" e o agente encadeia automaticamente duas ferramentas: primeiro chama `getCurrentWeather` para obter a temperatura em Celsius, depois passa esse valor para `celsiusToFahrenheit` para conversão — tudo em um único turno de conversa.

<img src="../../../translated_images/pt-BR/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplo de Encadeamento de Ferramentas" width="800"/>

*Encadeamento de ferramentas em ação — o agente chama getCurrentWeather primeiro, depois direciona o resultado em Celsius para celsiusToFahrenheit e entrega uma resposta combinada.*

**Falhas Elegantes** — Peça o clima numa cidade que não está nos dados simulados. A ferramenta retorna uma mensagem de erro, e a IA explica que não pode ajudar em vez de travar. Ferramentas falham de forma segura. O diagrama abaixo contrasta as duas abordagens — com tratamento adequado de erros, o agente captura a exceção e responde com ajuda, enquanto sem isso a aplicação inteira cai:

<img src="../../../translated_images/pt-BR/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fluxo de Tratamento de Erros" width="800"/>

*Quando uma ferramenta falha, o agente captura o erro e responde com uma explicação útil em vez de travar.*

Isso acontece em um único turno de conversa. O agente orquestra múltiplas chamadas de ferramentas autonomamente.

## Executar a Aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` existe no diretório raiz com credenciais Azure (criado durante o Módulo 01). Execute isto a partir do diretório do módulo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do diretório raiz (conforme descrito no Módulo 01), este módulo já está rodando na porta 8084. Você pode pular os comandos de início abaixo e ir direto para http://localhost:8084.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades do lado esquerdo do VS Code (identifique pelo ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs da aplicação em tempo real
- Monitorar status da aplicação
Simplesmente clique no botão de play ao lado de "tools" para iniciar este módulo, ou inicie todos os módulos de uma vez.

Aqui está como o Spring Boot Dashboard aparece no VS Code:

<img src="../../../translated_images/pt-BR/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*O Spring Boot Dashboard no VS Code — iniciar, parar e monitorar todos os módulos em um só lugar*

**Opção 2: Usando scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # Do diretório raiz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A partir do diretório root
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

Ambos os scripts carregam automaticamente variáveis de ambiente do arquivo `.env` na raiz e irão construir os JARs caso eles não existam.

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

## Usando a Aplicação

A aplicação fornece uma interface web onde você pode interagir com um agente de IA que tem acesso a ferramentas de previsão do tempo e conversão de temperatura. Veja como a interface é — inclui exemplos rápidos e um painel de chat para enviar solicitações:

<a href="images/tools-homepage.png"><img src="../../../translated_images/pt-BR/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface da Ferramenta do Agente de IA — exemplos rápidos e interface de chat para interagir com as ferramentas*

### Experimente o Uso Simples de Ferramentas

Comece com uma solicitação simples: "Converta 100 graus Fahrenheit para Celsius". O agente reconhece que precisa da ferramenta de conversão de temperatura, a chama com os parâmetros certos e retorna o resultado. Note como isso parece natural – você não especificou qual ferramenta usar nem como chamá-la.

### Teste o Encadeamento de Ferramentas

Agora tente algo mais complexo: "Qual é o tempo em Seattle e converta para Fahrenheit?" Observe o agente resolver isso em etapas. Primeiro ele obtém o tempo (que retorna em Celsius), reconhece que precisa converter para Fahrenheit, chama a ferramenta de conversão, e combina ambos os resultados em uma resposta.

### Veja o Fluxo da Conversa

A interface de chat mantém o histórico da conversa, permitindo interações em múltiplas etapas. Você pode ver todas as perguntas e respostas anteriores, facilitando acompanhar a conversa e entender como o agente constrói contexto através de várias trocas.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pt-BR/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversa de múltiplas etapas mostrando conversões simples, buscas de tempo e encadeamento de ferramentas*

### Experimente Diferentes Solicitações

Experimente várias combinações:
- Consultas do tempo: "Qual é o tempo em Tóquio?"
- Conversões de temperatura: "Quanto é 25°C em Kelvin?"
- Consultas combinadas: "Verifique o tempo em Paris e me diga se está acima de 20°C"

Note como o agente interpreta linguagem natural e a mapeia para chamadas apropriadas às ferramentas.

## Conceitos-Chave

### Padrão ReAct (Raciocinar e Agir)

O agente alterna entre raciocinar (decidir o que fazer) e agir (usar ferramentas). Este padrão permite a resolução autônoma de problemas ao invés de apenas responder instruções.

### Descrições de Ferramentas Importam

A qualidade das descrições das suas ferramentas afeta diretamente quão bem o agente as utiliza. Descrições claras e específicas ajudam o modelo a entender quando e como chamar cada ferramenta.

### Gerenciamento de Sessão

A anotação `@MemoryId` permite o gerenciamento automático de memória baseada em sessão. Cada ID de sessão recebe sua própria instância de `ChatMemory` gerenciada pelo bean `ChatMemoryProvider`, para que vários usuários possam interagir com o agente simultaneamente sem misturar as conversas. O diagrama a seguir mostra como múltiplos usuários são roteados para memórias isoladas baseadas em seus IDs de sessão:

<img src="../../../translated_images/pt-BR/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*Cada ID de sessão é mapeado para um histórico de conversa isolado — usuários nunca veem as mensagens uns dos outros.*

### Tratamento de Erros

Ferramentas podem falhar — APIs podem expirar, parâmetros podem ser inválidos, serviços externos podem cair. Agentes de produção precisam de tratamento de erros para que o modelo possa explicar problemas ou tentar alternativas ao invés de travar a aplicação toda. Quando uma ferramenta lança uma exceção, LangChain4j a captura e retorna a mensagem de erro para o modelo, que então pode explicar o problema em linguagem natural.

## Ferramentas Disponíveis

O diagrama abaixo mostra o amplo ecossistema de ferramentas que você pode construir. Este módulo demonstra ferramentas de previsão do tempo e temperatura, mas o mesmo padrão `@Tool` funciona para qualquer método Java — desde consultas a banco de dados até processamento de pagamento.

<img src="../../../translated_images/pt-BR/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Qualquer método Java anotado com @Tool torna-se disponível para a IA — o padrão se estende a bancos de dados, APIs, e-mail, operações de arquivo, e mais.*

## Quando Usar Agentes Baseados em Ferramentas

Nem toda solicitação precisa de ferramentas. A decisão depende de a IA precisar interagir com sistemas externos ou poder responder com seu próprio conhecimento. O guia a seguir resume quando as ferramentas agregam valor e quando não são necessárias:

<img src="../../../translated_images/pt-BR/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*Um guia rápido — ferramentas são para dados em tempo real, cálculos e ações; conhecimento geral e tarefas criativas não precisam delas.*

## Ferramentas vs RAG

Os módulos 03 e 04 ampliam o que a IA pode fazer, mas de formas fundamentalmente diferentes. RAG oferece ao modelo acesso ao **conhecimento** por meio da recuperação de documentos. Ferramentas dão ao modelo a capacidade de realizar **ações** por meio de chamadas de funções. O diagrama abaixo compara essas duas abordagens lado a lado — do funcionamento de cada fluxo até as compensações entre eles:

<img src="../../../translated_images/pt-BR/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG recupera informação de documentos estáticos — Ferramentas executam ações e buscam dados dinâmicos e em tempo real. Muitos sistemas de produção combinam ambos.*

Na prática, muitos sistemas de produção combinam ambas as abordagens: RAG para fundamentar respostas na sua documentação, e Ferramentas para buscar dados ao vivo ou executar operações.

## Próximos Passos

**Próximo Módulo:** [05-mcp - Protocolo de Contexto de Modelo (MCP)](../05-mcp/README.md)

---

**Navegação:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->