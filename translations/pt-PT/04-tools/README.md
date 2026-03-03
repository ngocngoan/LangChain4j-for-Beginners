# Módulo 04: Agentes de IA com Ferramentas

## Índice

- [O Que Vai Aprender](../../../04-tools)
- [Pré-requisitos](../../../04-tools)
- [Compreender Agentes de IA com Ferramentas](../../../04-tools)
- [Como Funciona a Chamada às Ferramentas](../../../04-tools)
  - [Definições das Ferramentas](../../../04-tools)
  - [Tomada de Decisão](../../../04-tools)
  - [Execução](../../../04-tools)
  - [Geração de Resposta](../../../04-tools)
  - [Arquitetura: Auto-Wiring do Spring Boot](../../../04-tools)
- [Encadeamento de Ferramentas](../../../04-tools)
- [Executar a Aplicação](../../../04-tools)
- [Usar a Aplicação](../../../04-tools)
  - [Experimentar Uso Simples de Ferramentas](../../../04-tools)
  - [Testar Encadeamento de Ferramentas](../../../04-tools)
  - [Ver o Fluxo da Conversa](../../../04-tools)
  - [Experimentar com Diferentes Pedidos](../../../04-tools)
- [Conceitos-Chave](../../../04-tools)
  - [Padrão ReAct (Raciocinar e Agir)](../../../04-tools)
  - [Descrições das Ferramentas Importam](../../../04-tools)
  - [Gestão de Sessão](../../../04-tools)
  - [Tratamento de Erros](../../../04-tools)
- [Ferramentas Disponíveis](../../../04-tools)
- [Quando Usar Agentes Baseados em Ferramentas](../../../04-tools)
- [Ferramentas vs RAG](../../../04-tools)
- [Próximos Passos](../../../04-tools)

## O Que Vai Aprender

Até agora, aprendeu como ter conversas com IA, estruturar prompts eficazmente e fundamentar respostas nos seus documentos. Mas ainda há uma limitação fundamental: modelos de linguagem só conseguem gerar texto. Eles não podem verificar o tempo, fazer cálculos, consultar bases de dados, ou interagir com sistemas externos.

As ferramentas alteram isso. Ao dar ao modelo acesso a funções que pode chamar, transforma-o de gerador de texto num agente que pode atuar. O modelo decide quando precisa de uma ferramenta, qual usar, e que parâmetros passar. O seu código executa a função e devolve o resultado. O modelo incorpora esse resultado na sua resposta.

## Pré-requisitos

- Ter completado o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implantados)
- Recomenda-se ter completado os módulos anteriores (este módulo refere [conceitos RAG do Módulo 03](../03-rag/README.md) na comparação Ferramentas vs RAG)
- Ficheiro `.env` na diretoria raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se não completou o Módulo 01, siga primeiro as instruções de implantação lá.

## Compreender Agentes de IA com Ferramentas

> **📝 Nota:** O termo "agentes" neste módulo refere-se a assistentes de IA aprimorados com capacidades de chamada a ferramentas. Isto é diferente dos padrões **Agentic AI** (agentes autónomos com planeamento, memória e raciocínio multi-etapa) que abordaremos no [Módulo 05: MCP](../05-mcp/README.md).

Sem ferramentas, um modelo de linguagem só pode gerar texto com base nos seus dados de treino. Pergunte-lhe qual é o tempo atual, e tem de adivinhar. Dê-lhe ferramentas, e pode chamar uma API de meteorologia, fazer cálculos, ou consultar uma base de dados — depois integrar esses resultados reais na sua resposta.

<img src="../../../translated_images/pt-PT/what-are-tools.724e468fc4de64da.webp" alt="Sem Ferramentas vs Com Ferramentas" width="800"/>

*Sem ferramentas, o modelo só pode adivinhar — com ferramentas pode chamar APIs, executar cálculos, e devolver dados em tempo real.*

Um agente de IA com ferramentas segue um padrão de **Raciocinar e Agir (ReAct)**. O modelo não responde apenas — pensa no que precisa, age chamando uma ferramenta, observa o resultado, e depois decide se atua novamente ou entrega a resposta final:

1. **Raciocinar** — O agente analisa a pergunta do utilizador e determina que informação precisa
2. **Agir** — O agente seleciona a ferramenta certa, gera os parâmetros corretos, e chama-a
3. **Observar** — O agente recebe a saída da ferramenta e avalia o resultado
4. **Repetir ou Responder** — Se precisar de mais dados, o agente repete o ciclo; caso contrário, compõe uma resposta em linguagem natural

<img src="../../../translated_images/pt-PT/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Padrão ReAct" width="800"/>

*O ciclo ReAct — o agente raciocina sobre o que fazer, age chamando uma ferramenta, observa o resultado, e repete até conseguir fornecer a resposta final.*

Isto acontece automaticamente. Você define as ferramentas e as suas descrições. O modelo trata da tomada de decisão sobre quando e como usá-las.

## Como Funciona a Chamada às Ferramentas

### Definições das Ferramentas

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Define funções com descrições claras e especificações dos parâmetros. O modelo vê estas descrições no seu prompt de sistema e entende o que cada ferramenta faz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // A sua lógica de consulta de clima
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
// - ChatMemoryProvider para gestão de sessão
```

O diagrama abaixo detalha cada anotação e mostra como cada parte ajuda a IA a entender quando chamar a ferramenta e que argumentos passar:

<img src="../../../translated_images/pt-PT/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia das Definições das Ferramentas" width="800"/>

*Anatomia de uma definição de ferramenta — @Tool indica à IA quando usá-la, @P descreve cada parâmetro, e @AiService liga tudo na inicialização.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e pergunte:
> - "Como integraria uma API real de meteorologia como OpenWeatherMap em vez de dados simulados?"
> - "O que faz uma boa descrição de ferramenta que ajuda a IA a usá-la corretamente?"
> - "Como trato erros e limites de taxa da API nas implementações das ferramentas?"

### Tomada de Decisão

Quando um utilizador pergunta "Qual é o tempo em Seattle?", o modelo não escolhe uma ferramenta aleatoriamente. Compara a intenção do utilizador com cada descrição de ferramenta que tem acesso, atribui uma pontuação de relevância a cada uma, e seleciona a melhor correspondência. Depois gera uma chamada de função estruturada com os parâmetros certos — neste caso, definindo `location` como `"Seattle"`.

Se nenhuma ferramenta corresponder ao pedido do utilizador, o modelo recai para responder com base no seu próprio conhecimento. Se várias ferramentas corresponderem, escolhe a mais específica.

<img src="../../../translated_images/pt-PT/decision-making.409cd562e5cecc49.webp" alt="Como a IA Decide Qual Ferramenta Usar" width="800"/>

*O modelo avalia cada ferramenta disponível contra a intenção do utilizador e seleciona a melhor correspondência — por isso descrições claras e específicas das ferramentas são importantes.*

### Execução

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

O Spring Boot faz o auto-wire da interface declarativa `@AiService` com todas as ferramentas registadas, e o LangChain4j executa as chamadas às ferramentas automaticamente. Nos bastidores, uma chamada completa à ferramenta passa por seis etapas — desde a pergunta em linguagem natural do utilizador até à resposta também em linguagem natural:

<img src="../../../translated_images/pt-PT/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxo de Chamada às Ferramentas" width="800"/>

*O fluxo completo — o utilizador faz uma pergunta, o modelo seleciona uma ferramenta, LangChain4j executa, e o modelo integra o resultado numa resposta natural.*

Se executou o [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) no Módulo 00, já viu este padrão em ação — as ferramentas `Calculator` foram chamadas da mesma forma. O diagrama de sequência abaixo mostra exatamente o que aconteceu por trás nessa demo:

<img src="../../../translated_images/pt-PT/tool-calling-sequence.94802f406ca26278.webp" alt="Diagrama de Sequência da Chamada à Ferramenta" width="800"/>

*O ciclo de chamadas à ferramenta do Quick Start demo — `AiServices` envia a sua mensagem e esquemas das ferramentas para o LLM, o LLM responde com uma chamada de função como `add(42, 58)`, o LangChain4j executa localmente o método `Calculator`, e alimenta o resultado para a resposta final.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e pergunte:
> - "Como funciona o padrão ReAct e por que é eficaz para agentes de IA?"
> - "Como o agente decide qual ferramenta usar e em que ordem?"
> - "O que acontece se a execução de uma ferramenta falhar — como tratar erros de forma robusta?"

### Geração de Resposta

O modelo recebe os dados do tempo e formata numa resposta em linguagem natural para o utilizador.

### Arquitetura: Auto-Wiring do Spring Boot

Este módulo usa a integração do LangChain4j com Spring Boot através de interfaces declarativas `@AiService`. Na inicialização, o Spring Boot descobre cada `@Component` que contém métodos `@Tool`, o seu bean `ChatModel` e o `ChatMemoryProvider` — e liga tudo numa única interface `Assistant` com zero código repetitivo.

<img src="../../../translated_images/pt-PT/spring-boot-wiring.151321795988b04e.webp" alt="Arquitetura Auto-Wiring Spring Boot" width="800"/>

*A interface @AiService liga o ChatModel, componentes das ferramentas e o provedor de memória — o Spring Boot faz todo o wiring automaticamente.*

Aqui está o ciclo completo da requisição como diagrama de sequência — desde o pedido HTTP pelo controlador, serviço, e proxy auto-wire, até à execução da ferramenta e regresso:

<img src="../../../translated_images/pt-PT/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Sequência de Chamada a Ferramenta no Spring Boot" width="800"/>

*Ciclo completo de requisição Spring Boot — O pedido HTTP flui pelo controlador e serviço até ao proxy Assistant auto-wire, que orquestra automaticamente o LLM e chamadas às ferramentas.*

Principais vantagens deste método:

- **Auto-wiring Spring Boot** — ChatModel e ferramentas injetados automaticamente
- **Padrão @MemoryId** — Gestão automática da memória baseada em sessão
- **Instância única** — Assistant criado uma vez e reutilizado para melhor desempenho
- **Execução type-safe** — Métodos Java chamados diretamente com conversão de tipos
- **Orquestração multi-turno** — Gerenciamento automático do encadeamento de ferramentas
- **Zero código repetitivo** — Sem chamadas manuais a `AiServices.builder()` ou HashMap de memória

Abordagens alternativas (manual `AiServices.builder()`) requerem mais código e perdem os benefícios da integração Spring Boot.

## Encadeamento de Ferramentas

**Encadeamento de Ferramentas** — O verdadeiro poder dos agentes baseados em ferramentas manifesta-se quando uma única questão requer múltiplas ferramentas. Pergunte "Qual é o tempo em Seattle em Fahrenheit?" e o agente encadeia automaticamente duas ferramentas: primeiro chama `getCurrentWeather` para obter a temperatura em Celsius, depois passa esse valor para `celsiusToFahrenheit` para conversão — tudo numa só interação da conversa.

<img src="../../../translated_images/pt-PT/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplo de Encadeamento de Ferramentas" width="800"/>

*Encadeamento de ferramentas em ação — o agente chama getCurrentWeather primeiro, depois envia o resultado em Celsius para celsiusToFahrenheit, e entrega uma resposta combinada.*

**Falhas Elegantes** — Peça o tempo para uma cidade que não está nos dados simulados. A ferramenta retorna uma mensagem de erro, e a IA explica que não pode ajudar em vez de falhar. As ferramentas falham com segurança. O diagrama abaixo contrasta as duas abordagens — com tratamento correto de erros, o agente interceta a exceção e responde utilmente; sem ele, a aplicação inteira falha:

<img src="../../../translated_images/pt-PT/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fluxo de Tratamento de Erros" width="800"/>

*Quando uma ferramenta falha, o agente interceta o erro e responde com uma explicação útil em vez de causar crash.*

Isto acontece numa única interação de conversa. O agente orquestra múltiplas chamadas a ferramentas autonomamente.

## Executar a Aplicação

**Verificar implantação:**

Assegure-se que o ficheiro `.env` existe na diretoria raiz com as credenciais Azure (criado durante o Módulo 01). Execute isto a partir da diretoria do módulo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` na diretoria raiz (como descrito no Módulo 01), este módulo já está a correr na porta 8084. Pode saltar os comandos de início abaixo e ir diretamente a http://localhost:8084.

**Opção 1: Usar Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um único clique
- Ver logs da aplicação em tempo real
- Monitorizar o estado da aplicação

Basta clicar no botão de play junto a "tools" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

Aqui está como é o Spring Boot Dashboard no VS Code:

<img src="../../../translated_images/pt-PT/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*Spring Boot Dashboard no VS Code — inicie, pare e monitorize todos os módulos num só lugar*

**Opção 2: Usar scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # Desde o diretório raiz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # A partir do diretório raiz
.\start-all.ps1
```

Ou comece apenas este módulo:

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` na raiz e irão criar os JARs se eles não existirem.

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
.\stop.ps1  # Este módulo apenas
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Utilização da Aplicação

A aplicação fornece uma interface web onde pode interagir com um agente de IA que tem acesso a ferramentas de meteorologia e conversão de temperaturas. Eis como a interface se apresenta — inclui exemplos de início rápido e um painel de chat para enviar pedidos:

<a href="images/tools-homepage.png"><img src="../../../translated_images/pt-PT/tools-homepage.4b4cd8b2717f9621.webp" alt="Interface de Ferramentas do Agente de IA" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface das Ferramentas do Agente de IA - exemplos rápidos e interface de chat para interação com as ferramentas*

### Experimente uma Utilização Simples da Ferramenta

Comece com um pedido simples: "Converter 100 graus Fahrenheit para Celsius". O agente reconhece que precisa da ferramenta de conversão de temperatura, chama-a com os parâmetros correctos e retorna o resultado. Note como isto parece natural - não especificou qual ferramenta usar nem como a chamar.

### Teste a Cadeia de Ferramentas

Agora experimente algo mais complexo: "Como está o tempo em Seattle e converte para Fahrenheit?" Veja o agente trabalhar por etapas. Primeiro obtém a meteorologia (que retorna em Celsius), reconhece que precisa converter para Fahrenheit, chama a ferramenta de conversão e combina ambos os resultados numa só resposta.

### Veja o Fluxo da Conversa

A interface de chat mantém o histórico da conversa, permitindo interacções de múltiplas trocas. Pode ver todas as consultas e respostas anteriores, facilitando o acompanhamento da conversa e compreendendo como o agente constrói contexto ao longo das diversas interacções.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pt-PT/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversa com Múltiplas Chamadas de Ferramentas" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversa de múltiplas trocas mostrando conversões simples, consultas meteorológicas e cadeias de ferramentas*

### Experimente Pedidos Diferentes

Experimente várias combinações:
- Consultas meteorológicas: "Como está o tempo em Tóquio?"
- Conversões de temperatura: "Quanto é 25°C em Kelvin?"
- Consultas combinadas: "Verifica o tempo em Paris e diz-me se está acima de 20°C"

Note como o agente interpreta a linguagem natural e a mapeia para chamadas apropriadas às ferramentas.

## Conceitos-Chave

### Padrão ReAct (Raciocinar e Agir)

O agente alterna entre raciocinar (decidir o que fazer) e agir (usar ferramentas). Este padrão permite a resolução autónoma de problemas em vez de apenas responder a instruções.

### As Descrições das Ferramentas Importam

A qualidade das descrições das suas ferramentas afecta directamente a forma como o agente as utiliza. Descrições claras e específicas ajudam o modelo a compreender quando e como chamar cada ferramenta.

### Gestão de Sessões

A anotação `@MemoryId` permite a gestão automática da memória baseada em sessões. Cada ID de sessão obtém uma instância própria de `ChatMemory` gerida pelo bean `ChatMemoryProvider`, permitindo que múltiplos utilizadores interajam com o agente simultaneamente sem que as suas conversas se misturem. O diagrama seguinte mostra como múltiplos utilizadores são encaminhados para memórias isoladas com base nos seus IDs de sessão:

<img src="../../../translated_images/pt-PT/session-management.91ad819c6c89c400.webp" alt="Gestão de Sessão com @MemoryId" width="800"/>

*Cada ID de sessão mapeia para um histórico de conversação isolado — os utilizadores nunca veem as mensagens uns dos outros.*

### Gestão de Erros

Ferramentas podem falhar — APIs expiram, parâmetros podem ser inválidos, serviços externos ficam indisponíveis. Agentes de produção precisam de gestão de erros para que o modelo possa explicar problemas ou tentar alternativas em vez de falhar toda a aplicação. Quando uma ferramenta lança uma exceção, o LangChain4j captura-a e passa a mensagem de erro de volta para o modelo, que pode então explicar o problema em linguagem natural.

## Ferramentas Disponíveis

O diagrama abaixo mostra o vasto ecossistema de ferramentas que pode construir. Este módulo demonstra ferramentas de meteorologia e temperatura, mas o mesmo padrão `@Tool` funciona para qualquer método Java — desde consultas a bases de dados até processamento de pagamentos.

<img src="../../../translated_images/pt-PT/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecossistema de Ferramentas" width="800"/>

*Qualquer método Java anotado com @Tool torna-se disponível para a IA — o padrão estende-se a bases de dados, APIs, email, operações em ficheiros e muito mais.*

## Quando Usar Agentes Baseados em Ferramentas

Nem todos os pedidos precisam de ferramentas. A decisão depende se a IA precisa interagir com sistemas externos ou pode responder com base no seu próprio conhecimento. O guia seguinte resume quando as ferramentas adicionam valor e quando são desnecessárias:

<img src="../../../translated_images/pt-PT/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quando Usar Ferramentas" width="800"/>

*Um guia rápido de decisão — ferramentas são para dados em tempo real, cálculos e acções; conhecimento geral e tarefas criativas não precisam delas.*

## Ferramentas vs RAG

Os módulos 03 e 04 ambos ampliam o que a IA pode fazer, mas de formas fundamentalmente diferentes. O RAG dá ao modelo acesso ao **conhecimento** ao recuperar documentos. As Ferramentas dão ao modelo a capacidade de tomar **acções** ao chamar funções. O diagrama abaixo compara estas duas abordagens lado a lado — desde como cada fluxo de trabalho opera até às compensações entre elas:

<img src="../../../translated_images/pt-PT/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparação Ferramentas vs RAG" width="800"/>

*O RAG recupera informação de documentos estáticos — as Ferramentas executam acções e obtêm dados dinâmicos e em tempo real. Muitos sistemas de produção combinam ambas.*

Na prática, muitos sistemas de produção combinam ambas as abordagens: RAG para fundamentar respostas na sua documentação, e Ferramentas para obter dados vivos ou realizar operações.

## Passos Seguintes

**Próximo Módulo:** [05-mcp - Protocolo de Contexto de Modelo (MCP)](../05-mcp/README.md)

---

**Navegação:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor tenha em conta que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->