# Módulo 04: Agentes de IA com Ferramentas

## Índice

- [O que Vai Aprender](../../../04-tools)
- [Pré-requisitos](../../../04-tools)
- [Compreender Agentes de IA com Ferramentas](../../../04-tools)
- [Como Funciona a Chamada de Ferramentas](../../../04-tools)
  - [Definições de Ferramentas](../../../04-tools)
  - [Tomada de Decisão](../../../04-tools)
  - [Execução](../../../04-tools)
  - [Geração de Resposta](../../../04-tools)
  - [Arquitetura: Auto-Injeção no Spring Boot](../../../04-tools)
- [Encadeamento de Ferramentas](../../../04-tools)
- [Executar a Aplicação](../../../04-tools)
- [Utilizar a Aplicação](../../../04-tools)
  - [Experimentar Uso Simples de Ferramentas](../../../04-tools)
  - [Testar Encadeamento de Ferramentas](../../../04-tools)
  - [Ver Fluxo da Conversa](../../../04-tools)
  - [Experimentar Diferentes Pedidos](../../../04-tools)
- [Conceitos-Chave](../../../04-tools)
  - [Padrão ReAct (Raciocínio e Ação)](../../../04-tools)
  - [Importância das Descrições das Ferramentas](../../../04-tools)
  - [Gestão de Sessão](../../../04-tools)
  - [Gestão de Erros](../../../04-tools)
- [Ferramentas Disponíveis](../../../04-tools)
- [Quando Usar Agentes Baseados em Ferramentas](../../../04-tools)
- [Ferramentas vs RAG](../../../04-tools)
- [Próximos Passos](../../../04-tools)

## O que Vai Aprender

Até agora, aprendeu como ter conversas com IA, estruturar prompts efetivamente e fundamentar respostas nos seus documentos. Mas há ainda uma limitação fundamental: os modelos de linguagem só conseguem gerar texto. Não podem consultar o tempo, fazer cálculos, consultar bases de dados ou interagir com sistemas externos.

As ferramentas alteram isto. Ao dar ao modelo acesso a funções que pode chamar, transforma-o de gerador de texto num agente capaz de tomar ações. O modelo decide quando precisa de uma ferramenta, qual ferramenta usar e que parâmetros passar. O seu código executa a função e devolve o resultado. O modelo incorpora esse resultado na sua resposta.

## Pré-requisitos

- Ter concluído o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implementados)
- Recomenda-se ter concluído os módulos anteriores (este módulo faz referência aos [conceitos RAG do Módulo 03](../03-rag/README.md) na comparação Ferramentas vs RAG)
- Ficheiro `.env` na raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se ainda não completou o Módulo 01, siga primeiro as instruções de implementação aí.

## Compreender Agentes de IA com Ferramentas

> **📝 Nota:** O termo "agentes" neste módulo refere-se a assistentes de IA aprimorados com capacidades de chamada de ferramentas. Isto é diferente dos padrões de **Agentic AI** (agentes autónomos com planeamento, memória e raciocínio multi-etapas) que abordaremos no [Módulo 05: MCP](../05-mcp/README.md).

Sem ferramentas, um modelo de linguagem só consegue gerar texto com base nos seus dados de treino. Pergunte o tempo atual e ele terá de adivinhar. Dê-lhe ferramentas, e ele pode chamar uma API do tempo, fazer cálculos, ou consultar uma base de dados — e depois integrar esses resultados reais na resposta.

<img src="../../../translated_images/pt-PT/what-are-tools.724e468fc4de64da.webp" alt="Sem Ferramentas vs Com Ferramentas" width="800"/>

*Sem ferramentas, o modelo só pode adivinhar — com ferramentas pode chamar APIs, fazer cálculos e devolver dados em tempo real.*

Um agente de IA com ferramentas segue um padrão **Raciocínio e Ação (ReAct)**. O modelo não se limita a responder — pensa no que precisa, age chamando uma ferramenta, observa o resultado e depois decide se age novamente ou devolve a resposta final:

1. **Raciocinar** — O agente analisa a pergunta do utilizador e determina que informação precisa
2. **Agir** — O agente seleciona a ferramenta certa, gera os parâmetros corretos e chama-a
3. **Observar** — O agente recebe o resultado da ferramenta e avalia-o
4. **Repetir ou Responder** — Se for necessária mais informação, repete o ciclo; caso contrário, compõe uma resposta em linguagem natural

<img src="../../../translated_images/pt-PT/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Padrão ReAct" width="800"/>

*O ciclo ReAct — o agente raciocina sobre o que fazer, age chamando uma ferramenta, observa o resultado e repete até poder entregar a resposta final.*

Isto acontece automaticamente. Define as ferramentas e as suas descrições. O modelo trata da decisão sobre quando e como as usar.

## Como Funciona a Chamada de Ferramentas

### Definições de Ferramentas

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Define funções com descrições claras e especificações dos parâmetros. O modelo vê estas descrições no prompt de sistema e entende o que cada ferramenta faz.

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // A sua lógica de pesquisa de meteorologia
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
// - ChatMemoryProvider para gestão da sessão
```

O diagrama abaixo explica cada anotação e mostra como cada peça ajuda a IA a saber quando chamar a ferramenta e que argumentos passar:

<img src="../../../translated_images/pt-PT/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia das Definições de Ferramentas" width="800"/>

*Anatomia de uma definição de ferramenta — @Tool indica à IA quando usar, @P descreve cada parâmetro, e @AiService liga tudo na inicialização.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e pergunte:
> - "Como posso integrar uma API de tempo real como OpenWeatherMap em vez de dados simulados?"
> - "O que torna uma boa descrição de ferramenta que ajuda a IA a usá-la corretamente?"
> - "Como devo gerir erros e limites de taxa da API em implementações de ferramentas?"

### Tomada de Decisão

Quando o utilizador pergunta "Qual é o tempo em Seattle?", o modelo não escolhe uma ferramenta aleatoriamente. Compara a intenção do utilizador contra cada descrição de ferramenta que tem, pontua a relevância de cada uma e escolhe a melhor. Depois gera uma chamada estruturada da função com os parâmetros corretos — neste caso, definindo `location` para `"Seattle"`.

Se nenhuma ferramenta coincidir com o pedido, o modelo responde com o seu próprio conhecimento. Se múltiplas ferramentas coincidirem, escolhe a mais específica.

<img src="../../../translated_images/pt-PT/decision-making.409cd562e5cecc49.webp" alt="Como a IA Decide que Ferramenta Usar" width="800"/>

*O modelo avalia todas as ferramentas disponíveis em relação à intenção do utilizador e escolhe a melhor — por isso é importante escrever descrições claras e específicas.*

### Execução

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

O Spring Boot injeta automaticamente a interface declarativa `@AiService` com todas as ferramentas registadas, e o LangChain4j executa chamadas de ferramentas automaticamente. Nos bastidores, a chamada completa de uma ferramenta passa por seis etapas — desde a pergunta em linguagem natural do utilizador até à resposta final em linguagem natural:

<img src="../../../translated_images/pt-PT/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxo de Chamada de Ferramenta" width="800"/>

*O fluxo completo — o utilizador faz uma pergunta, o modelo escolhe uma ferramenta, o LangChain4j executa e o modelo incorpora o resultado na resposta.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e pergunte:
> - "Como funciona o padrão ReAct e por que é eficaz para agentes de IA?"
> - "Como é que o agente decide que ferramenta usar e em que ordem?"
> - "O que acontece se a execução de uma ferramenta falhar - como devo gerir erros de forma robusta?"

### Geração de Resposta

O modelo recebe os dados do tempo e formata-os numa resposta em linguagem natural para o utilizador.

### Arquitetura: Auto-Injeção no Spring Boot

Este módulo usa a integração LangChain4j com Spring Boot via interfaces declarativas `@AiService`. Na inicialização, o Spring Boot descobre todos os `@Component` que contenham métodos `@Tool`, o seu bean `ChatModel` e o `ChatMemoryProvider` — e liga-os num único interface `Assistant` sem código repetitivo.

<img src="../../../translated_images/pt-PT/spring-boot-wiring.151321795988b04e.webp" alt="Arquitetura de Auto-Injeção no Spring Boot" width="800"/>

*A interface @AiService liga o ChatModel, os componentes de ferramentas e o fornecedor de memória — o Spring Boot trata da injeção automaticamente.*

Principais benefícios desta abordagem:

- **Auto-injeção Spring Boot** — ChatModel e ferramentas injetados automaticamente
- **Padrão @MemoryId** — Gestão automática de memória baseada em sessão
- **Instância única** — Assistant criado uma vez e reutilizado para melhor desempenho
- **Execução tipo-segura** — Métodos Java chamados diretamente com conversão de tipos
- **Orquestração multi-turno** — Suporta encadeamento de ferramentas automaticamente
- **Zero boilerplate** — Sem chamadas manuais `AiServices.builder()` ou HashMaps para memória

Abordagens alternativas (manuais com `AiServices.builder()`) exigem mais código e perdem benefícios da integração Spring Boot.

## Encadeamento de Ferramentas

**Encadeamento de Ferramentas** — O poder real dos agentes baseados em ferramentas mostra-se quando uma única pergunta requer várias ferramentas. Pergunte "Qual é o tempo em Seattle em Fahrenheit?" e o agente encadeia automaticamente duas ferramentas: primeiro chama `getCurrentWeather` para obter a temperatura em Celsius, e depois passa esse valor para `celsiusToFahrenheit` para conversão — tudo numa única interação.

<img src="../../../translated_images/pt-PT/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplo de Encadeamento de Ferramentas" width="800"/>

*Encadeamento de ferramentas em ação — o agente chama primeiro getCurrentWeather, depois encaminha o resultado em Celsius para celsiusToFahrenheit, entregando uma resposta combinada.*

**Falhas Controladas** — Peça o tempo numa cidade que não está nos dados simulados. A ferramenta devolve uma mensagem de erro, e a IA explica que não pode ajudar em vez de falhar. As ferramentas falham com segurança. O diagrama abaixo compara as duas abordagens — com gestão correta de erros, o agente apanha a exceção e responde de forma útil; sem isso, a aplicação falha completamente:

<img src="../../../translated_images/pt-PT/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fluxo de Gestão de Erros" width="800"/>

*Quando uma ferramenta falha, o agente apanha o erro e responde com uma explicação útil em vez de falhar.*

Isto acontece numa única interação. O agente orquestra múltiplas chamadas de ferramentas autonomamente.

## Executar a Aplicação

**Verificar a implementação:**

Assegure-se que o ficheiro `.env` existe na raiz com credenciais Azure (criado durante o Módulo 01). Execute na diretoria do módulo (`04-tools/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` na raiz (conforme descrito no Módulo 01), este módulo já está a funcionar na porta 8084. Pode ignorar os comandos de início abaixo e ir diretamente a http://localhost:8084.

**Opção 1: Usar o Spring Boot Dashboard (recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis na área de trabalho
- Iniciar/parar aplicações com um único clique
- Ver logs das aplicações em tempo real
- Monitorizar o estado das aplicações

Basta clicar no botão play junto a "tools" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

Assim se apresenta o Spring Boot Dashboard no VS Code:

<img src="../../../translated_images/pt-PT/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*O Spring Boot Dashboard no VS Code — comece, pare e monitorize todos os módulos num só lugar*

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz e compilam os JARs se não existirem.

> **Nota:** Se preferir compilar manualmente todos os módulos antes de iniciar:
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

## Utilizar a Aplicação

A aplicação fornece uma interface web onde pode interagir com um agente de IA que tem acesso a ferramentas de tempo e conversão de temperatura. Eis como é a interface — inclui exemplos para arranque rápido e um painel de chat para enviar pedidos:
<a href="images/tools-homepage.png"><img src="../../../translated_images/pt-PT/tools-homepage.4b4cd8b2717f9621.webp" alt="Interface de Ferramentas de Agente de IA" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface das Ferramentas de Agente de IA - exemplos rápidos e interface de chat para interação com ferramentas*

### Experimente o Uso Simples de Ferramentas

Comece com um pedido direto: "Converter 100 graus Fahrenheit para Celsius". O agente reconhece que precisa da ferramenta de conversão de temperatura, chama-a com os parâmetros corretos e devolve o resultado. Repare como isto parece natural - não especificou qual ferramenta usar nem como a chamar.

### Teste a Encadeação de Ferramentas

Agora experimente algo mais complexo: "Qual é o tempo em Seattle e converte para Fahrenheit?" Veja o agente a trabalhar em passos. Primeiro obtém o tempo (que retorna em Celsius), reconhece que precisa converter para Fahrenheit, chama a ferramenta de conversão e combina ambos os resultados numa só resposta.

### Veja o Fluxo da Conversa

A interface de chat mantém o histórico da conversa, permitindo interações com múltiplos turnos. Pode ver todas as consultas e respostas anteriores, facilitando acompanhar a conversa e compreender como o agente constrói contexto ao longo de várias trocas.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pt-PT/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversa com Múltiplas Chamadas de Ferramentas" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversa com múltiplos turnos mostrando conversões simples, consultas meteorológicas e encadeação de ferramentas*

### Experimente com Diferentes Pedidos

Experimente várias combinações:
- Consultas meteorológicas: "Qual é o tempo em Tóquio?"
- Conversões de temperatura: "Quanto é 25°C em Kelvin?"
- Consultas combinadas: "Verifica o tempo em Paris e diz-me se está acima de 20°C"

Repare como o agente interpreta a linguagem natural e a associa às chamadas das ferramentas apropriadas.

## Conceitos-chave

### Padrão ReAct (Raciocinar e Agir)

O agente alterna entre raciocinar (decidir o que fazer) e agir (usar ferramentas). Este padrão permite resolução autónoma de problemas em vez de apenas responder a instruções.

### As Descrições das Ferramentas Importam

A qualidade das descrições das suas ferramentas afeta diretamente o quão bem o agente as usa. Descrições claras e específicas ajudam o modelo a entender quando e como chamar cada ferramenta.

### Gestão de Sessões

A anotação `@MemoryId` permite a gestão automática da memória baseada em sessões. Cada ID de sessão obtém uma instância própria de `ChatMemory` gerida pelo bean `ChatMemoryProvider`, pelo que múltiplos utilizadores podem interagir com o agente simultaneamente sem que as suas conversas se misturem. O diagrama seguinte mostra como múltiplos utilizadores são direcionados para memórias isoladas consoante os seus IDs de sessão:

<img src="../../../translated_images/pt-PT/session-management.91ad819c6c89c400.webp" alt="Gestão de Sessão com @MemoryId" width="800"/>

*Cada ID de sessão corresponde a um histórico de conversa isolado — os utilizadores nunca vêem as mensagens uns dos outros.*

### Gestão de Erros

As ferramentas podem falhar — as APIs podem perder tempo, parâmetros podem ser inválidos, serviços externos podem falhar. Agentes de produção precisam de gestão de erros para que o modelo possa explicar os problemas ou tentar alternativas em vez de derrubar a aplicação inteira. Quando uma ferramenta lança uma exceção, o LangChain4j captura-a e envia a mensagem de erro de volta ao modelo, que pode então explicar o problema em linguagem natural.

## Ferramentas Disponíveis

O diagrama abaixo mostra o amplo ecossistema de ferramentas que pode construir. Este módulo demonstra ferramentas de tempo e temperatura, mas o mesmo padrão `@Tool` funciona para qualquer método Java — desde consultas a bases de dados a processamento de pagamentos.

<img src="../../../translated_images/pt-PT/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecossistema de Ferramentas" width="800"/>

*Qualquer método Java anotado com @Tool fica disponível ao AI — o padrão estende-se a bases de dados, APIs, email, operações em ficheiros e mais.*

## Quando Usar Agentes Baseados em Ferramentas

Nem todos os pedidos precisam de ferramentas. A decisão depende se o AI precisa interagir com sistemas externos ou pode responder a partir do seu próprio conhecimento. O guia seguinte resume quando as ferramentas são úteis e quando não são necessárias:

<img src="../../../translated_images/pt-PT/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quando Usar Ferramentas" width="800"/>

*Um guia rápido de decisão — ferramentas são para dados em tempo real, cálculos e ações; conhecimento geral e tarefas criativas não precisam delas.*

## Ferramentas vs RAG

Os módulos 03 e 04 estendem o que o AI pode fazer, mas de formas fundamentalmente diferentes. RAG dá acesso ao **conhecimento** ao recuperar documentos. Ferramentas dão a capacidade de tomar **ações** ao chamar funções. O diagrama abaixo compara estas duas abordagens lado a lado — desde como cada fluxo de trabalho opera até aos seus compromissos:

<img src="../../../translated_images/pt-PT/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparação Ferramentas vs RAG" width="800"/>

*RAG recupera informação de documentos estáticos — Ferramentas executam ações e obtêm dados dinâmicos e em tempo real. Muitos sistemas de produção combinam ambos.*

Na prática, muitos sistemas de produção combinam ambas as abordagens: RAG para suportar respostas baseadas na sua documentação, e Ferramentas para obter dados ao vivo ou executar operações.

## Próximos Passos

**Próximo Módulo:** [05-mcp - Protocolo de Contexto de Modelo (MCP)](../05-mcp/README.md)

---

**Navegação:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a exatidão, por favor tenha em conta que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deverá ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional humana. Não nos responsabilizamos por qualquer mal-entendido ou interpretação errada decorrente do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->