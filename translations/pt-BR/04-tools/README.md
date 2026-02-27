# Módulo 04: Agentes de IA com Ferramentas

## Índice

- [O que você vai aprender](../../../04-tools)
- [Pré-requisitos](../../../04-tools)
- [Entendendo Agentes de IA com Ferramentas](../../../04-tools)
- [Como funciona a chamada de ferramentas](../../../04-tools)
  - [Definições de ferramenta](../../../04-tools)
  - [Tomada de decisão](../../../04-tools)
  - [Execução](../../../04-tools)
  - [Geração de resposta](../../../04-tools)
  - [Arquitetura: Auto-Wiring do Spring Boot](../../../04-tools)
- [Encadeamento de ferramentas](../../../04-tools)
- [Rodando a aplicação](../../../04-tools)
- [Usando a aplicação](../../../04-tools)
  - [Tente uso simples de ferramenta](../../../04-tools)
  - [Teste encadeamento de ferramentas](../../../04-tools)
  - [Veja o fluxo da conversa](../../../04-tools)
  - [Experimente diferentes solicitações](../../../04-tools)
- [Conceitos chave](../../../04-tools)
  - [Padrão ReAct (Raciocínio e Ação)](../../../04-tools)
  - [Descrições de ferramenta importam](../../../04-tools)
  - [Gerenciamento de sessão](../../../04-tools)
  - [Tratamento de erros](../../../04-tools)
- [Ferramentas disponíveis](../../../04-tools)
- [Quando usar agentes baseados em ferramentas](../../../04-tools)
- [Ferramentas vs RAG](../../../04-tools)
- [Próximos passos](../../../04-tools)

## O que você vai aprender

Até agora, você aprendeu como ter conversas com IA, estruturar prompts efetivamente e fundamentar respostas em seus documentos. Mas ainda há uma limitação fundamental: modelos de linguagem só conseguem gerar texto. Eles não podem checar o tempo, fazer cálculos, consultar bancos de dados ou interagir com sistemas externos.

As ferramentas mudam isso. Dando ao modelo acesso a funções que ele pode chamar, você o transforma de um gerador de texto em um agente que pode tomar ações. O modelo decide quando precisa de uma ferramenta, qual ferramenta usar e quais parâmetros passar. Seu código executa a função e retorna o resultado. O modelo incorpora esse resultado em sua resposta.

## Pré-requisitos

- Ter completado o Módulo 01 (recursos Azure OpenAI implantados)
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não completou o Módulo 01, siga as instruções de implantação lá primeiro.

## Entendendo Agentes de IA com Ferramentas

> **📝 Nota:** O termo "agentes" neste módulo refere-se a assistentes de IA aprimorados com capacidades de chamada de ferramentas. Isso é diferente dos padrões **Agentic AI** (agentes autônomos com planejamento, memória e raciocínio multi-etapas) que abordaremos no [Módulo 05: MCP](../05-mcp/README.md).

Sem ferramentas, um modelo de linguagem só pode gerar texto a partir dos dados de treinamento. Pergunte a ele qual é o tempo atual, e ele tem que adivinhar. Dê ferramentas e ele pode chamar uma API do tempo, realizar cálculos ou consultar um banco de dados — e então integrar esses resultados reais em sua resposta.

<img src="../../../translated_images/pt-BR/what-are-tools.724e468fc4de64da.webp" alt="Sem Ferramentas vs Com Ferramentas" width="800"/>

*Sem ferramentas o modelo só pode adivinhar — com ferramentas ele pode chamar APIs, realizar cálculos e retornar dados em tempo real.*

Um agente de IA com ferramentas segue um padrão de **Raciocínio e Ação (ReAct)**. O modelo não só responde — ele pensa sobre o que precisa, age chamando uma ferramenta, observa o resultado e então decide se deve agir novamente ou entregar a resposta final:

1. **Raciocina** — O agente analisa a pergunta do usuário e determina quais informações precisa
2. **Age** — O agente seleciona a ferramenta certa, gera os parâmetros corretos e a chama
3. **Observa** — O agente recebe a saída da ferramenta e avalia o resultado
4. **Repete ou Responde** — Se mais dados forem necessários, o agente repete o ciclo; caso contrário, compõe uma resposta em linguagem natural

<img src="../../../translated_images/pt-BR/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="Padrão ReAct" width="800"/>

*O ciclo ReAct — o agente raciocina sobre o que fazer, age chamando uma ferramenta, observa o resultado e repete até poder entregar a resposta final.*

Isso acontece automaticamente. Você define as ferramentas e suas descrições. O modelo cuida da tomada de decisão sobre quando e como usá-las.

## Como funciona a chamada de ferramentas

### Definições de ferramenta

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

Você define funções com descrições claras e especificações de parâmetros. O modelo vê essas descrições em seu prompt de sistema e entende o que cada ferramenta faz.

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

// Assistente é automaticamente configurado pelo Spring Boot com:
// - Bean ChatModel
// - Todos os métodos @Tool de classes @Component
// - ChatMemoryProvider para gerenciamento de sessão
```

O diagrama abaixo detalha cada anotação e mostra como cada parte ajuda a IA a entender quando chamar a ferramenta e quais argumentos passar:

<img src="../../../translated_images/pt-BR/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomia das Definições de Ferramenta" width="800"/>

*Anatomia de uma definição de ferramenta — @Tool diz para o IA quando usá-la, @P descreve cada parâmetro, e @AiService conecta tudo na inicialização.*

> **🤖 Tente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) e pergunte:
> - "Como eu integraria uma API real de clima como OpenWeatherMap em vez de dados simulados?"
> - "O que torna uma boa descrição de ferramenta que ajuda a IA a usá-la corretamente?"
> - "Como eu trato erros de API e limites de taxa nas implementações de ferramentas?"

### Tomada de decisão

Quando um usuário pergunta "Qual é o tempo em Seattle?", o modelo não escolhe uma ferramenta aleatoriamente. Ele compara a intenção do usuário com cada descrição de ferramenta disponível, atribui uma pontuação de relevância e seleciona a melhor correspondência. Em seguida, gera uma chamada de função estruturada com os parâmetros certos — neste caso, definindo `location` como `"Seattle"`.

Se nenhuma ferramenta corresponder ao pedido do usuário, o modelo responde com base em seu próprio conhecimento. Se múltiplas ferramentas corresponderem, escolhe a mais específica.

<img src="../../../translated_images/pt-BR/decision-making.409cd562e5cecc49.webp" alt="Como a IA Decide Qual Ferramenta Usar" width="800"/>

*O modelo avalia cada ferramenta disponível em relação à intenção do usuário e seleciona a melhor — por isso escrever descrições claras e específicas importa.*

### Execução

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

O Spring Boot auto-conecta a interface declarativa `@AiService` com todas as ferramentas registradas, e o LangChain4j executa as chamadas de ferramentas automaticamente. Nos bastidores, uma chamada completa de ferramenta passa por seis etapas — desde a pergunta do usuário em linguagem natural até a resposta em linguagem natural:

<img src="../../../translated_images/pt-BR/tool-calling-flow.8601941b0ca041e6.webp" alt="Fluxo de Chamada de Ferramenta" width="800"/>

*O fluxo completo — o usuário faz uma pergunta, o modelo seleciona uma ferramenta, LangChain4j a executa, e o modelo integra o resultado em uma resposta natural.*

> **🤖 Tente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) e pergunte:
> - "Como funciona o padrão ReAct e por que ele é eficaz para agentes de IA?"
> - "Como o agente decide qual ferramenta usar e em que ordem?"
> - "O que acontece se a execução de uma ferramenta falhar - como devo tratar erros de forma robusta?"

### Geração de resposta

O modelo recebe os dados do tempo e os formata em uma resposta em linguagem natural para o usuário.

### Arquitetura: Auto-Wiring do Spring Boot

Este módulo usa a integração do LangChain4j com Spring Boot por meio de interfaces declarativas `@AiService`. Na inicialização, o Spring Boot descobre cada `@Component` que contém métodos `@Tool`, seu bean `ChatModel` e o `ChatMemoryProvider` — e conecta todos juntos em uma única interface `Assistant` com zero código repetitivo.

<img src="../../../translated_images/pt-BR/spring-boot-wiring.151321795988b04e.webp" alt="Arquitetura de Auto-Wiring do Spring Boot" width="800"/>

*A interface @AiService reúne o ChatModel, componentes de ferramenta e o provedor de memória — o Spring Boot faz todo o wiring automaticamente.*

Principais benefícios dessa abordagem:

- **Auto-wiring do Spring Boot** — ChatModel e ferramentas injetados automaticamente
- **Padrão @MemoryId** — Gerenciamento automático de memória baseado em sessão
- **Instância única** — Assistant criado uma vez e reutilizado para melhor performance
- **Execução tipada segura** — métodos Java chamados diretamente com conversão de tipos
- **Orquestração multi-turno** — trata encadeamento de ferramentas automaticamente
- **Zero código repetitivo** — sem chamadas manuais a `AiServices.builder()` ou HashMap de memória

Abordagens alternativas (como `AiServices.builder()` manual) exigem mais código e não aproveitam os benefícios da integração com Spring Boot.

## Encadeamento de ferramentas

**Encadeamento de ferramentas** — O poder real dos agentes baseados em ferramentas aparece quando uma única pergunta requer múltiplas ferramentas. Pergunte "Qual o tempo em Seattle em Fahrenheit?" e o agente encadeia automaticamente duas ferramentas: primeiro chama `getCurrentWeather` para obter a temperatura em Celsius, depois passa esse valor para `celsiusToFahrenheit` para conversão — tudo em um único turno de conversa.

<img src="../../../translated_images/pt-BR/tool-chaining-example.538203e73d09dd82.webp" alt="Exemplo de Encadeamento de Ferramentas" width="800"/>

*Encadeamento de ferramentas em ação — o agente chama getCurrentWeather primeiro, depois encaminha o resultado em Celsius para celsiusToFahrenheit, e entrega uma resposta combinada.*

Veja como isso fica na aplicação em execução — o agente encadeia duas chamadas de ferramenta em um único turno de conversa:

<a href="images/tool-chaining.png"><img src="../../../translated_images/pt-BR/tool-chaining.3b25af01967d6f7b.webp" alt="Encadeamento de Ferramentas" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Resultado real da aplicação — o agente encadeia automaticamente getCurrentWeather → celsiusToFahrenheit em um turno.*

**Falhas Elegantes** — Solicite o tempo em uma cidade que não está nos dados simulados. A ferramenta retorna uma mensagem de erro, e a IA explica que não pode ajudar em vez de travar. As ferramentas falham com segurança.

<img src="../../../translated_images/pt-BR/error-handling-flow.9a330ffc8ee0475c.webp" alt="Fluxo de Tratamento de Erro" width="800"/>

*Quando uma ferramenta falha, o agente captura o erro e responde com uma explicação útil ao invés de travar.*

Isso acontece em um único turno de conversa. O agente orquestra múltiplas chamadas de ferramentas autonomamente.

## Rodando a aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` exista no diretório raiz com as credenciais Azure (criado durante o Módulo 01):
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8084. Você pode pular os comandos de início abaixo e ir direto para http://localhost:8084.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários do VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Visualizar logs da aplicação em tempo real
- Monitorar o status da aplicação

Basta clicar no botão de play ao lado de "tools" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usando scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # Do diretório raiz
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

Ambos os scripts carregam automaticamente variáveis de ambiente do arquivo `.env` raiz e irão construir os JARs se eles não existirem.

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
./stop.sh  # Somente este módulo
# Ou
cd .. && ./stop-all.sh  # Todos os módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Somente este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Usando a aplicação

A aplicação fornece uma interface web onde você pode interagir com um agente de IA que tem acesso a ferramentas de clima e conversão de temperatura.

<a href="images/tools-homepage.png"><img src="../../../translated_images/pt-BR/tools-homepage.4b4cd8b2717f9621.webp" alt="Interface de Agente de IA com Ferramentas" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*A interface de Ferramentas do Agente de IA - exemplos rápidos e interface de chat para interagir com as ferramentas*

### Tente uso simples de ferramenta
Comece com uma solicitação simples: "Converta 100 graus Fahrenheit para Celsius". O agente reconhece que precisa da ferramenta de conversão de temperatura, chama-a com os parâmetros corretos e retorna o resultado. Note como isso parece natural - você não especificou qual ferramenta usar ou como chamá-la.

### Testar Encadeamento de Ferramentas

Agora tente algo mais complexo: "Qual é o tempo em Seattle e converta para Fahrenheit?" Observe o agente trabalhar isso em etapas. Primeiro ele obtém o tempo (que retorna em Celsius), reconhece que precisa converter para Fahrenheit, chama a ferramenta de conversão e combina ambos os resultados em uma única resposta.

### Veja o Fluxo da Conversa

A interface de chat mantém o histórico da conversa, permitindo interações em múltiplas etapas. Você pode ver todas as consultas e respostas anteriores, facilitando o acompanhamento da conversa e entendendo como o agente constrói o contexto por várias trocas.

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/pt-BR/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversa com múltiplas chamadas de ferramenta" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Conversa de múltiplas etapas mostrando conversões simples, consultas meteorológicas e encadeamento de ferramentas*

### Experimente com Diferentes Solicitações

Tente várias combinações:
- Consultas de clima: "Qual é o tempo em Tóquio?"
- Conversões de temperatura: "Quanto é 25°C em Kelvin?"
- Consultas combinadas: "Verifique o tempo em Paris e me diga se está acima de 20°C"

Perceba como o agente interpreta linguagem natural e a mapeia para chamadas apropriadas das ferramentas.

## Conceitos Principais

### Padrão ReAct (Raciocínio e Ação)

O agente alterna entre raciocínio (decidir o que fazer) e ação (usar ferramentas). Esse padrão permite resolução autônoma de problemas ao invés de apenas responder instruções.

### Descrições das Ferramentas Importam

A qualidade das descrições das suas ferramentas impacta diretamente o quão bem o agente as utiliza. Descrições claras e específicas ajudam o modelo a entender quando e como chamar cada ferramenta.

### Gerenciamento de Sessão

A anotação `@MemoryId` permite gerenciamento automático de memória baseado em sessão. Cada ID de sessão recebe sua própria instância `ChatMemory` gerenciada pelo bean `ChatMemoryProvider`, para que múltiplos usuários possam interagir com o agente simultaneamente sem suas conversas se misturarem.

<img src="../../../translated_images/pt-BR/session-management.91ad819c6c89c400.webp" alt="Gerenciamento de Sessão com @MemoryId" width="800"/>

*Cada ID de sessão mapeia para um histórico de conversa isolado — os usuários nunca veem as mensagens uns dos outros.*

### Tratamento de Erros

Ferramentas podem falhar — APIs podem expirar, parâmetros podem ser inválidos, serviços externos podem ficar indisponíveis. Agentes em produção precisam de tratamento de erros para que o modelo possa explicar problemas ou tentar alternativas ao invés de travar a aplicação inteira. Quando uma ferramenta lança uma exceção, o LangChain4j a captura e envia a mensagem de erro de volta para o modelo, que então pode explicar o problema em linguagem natural.

## Ferramentas Disponíveis

O diagrama abaixo mostra o amplo ecossistema de ferramentas que você pode construir. Este módulo demonstra ferramentas de clima e temperatura, mas o mesmo padrão `@Tool` funciona para qualquer método Java — desde consultas a bancos de dados até processamento de pagamentos.

<img src="../../../translated_images/pt-BR/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Ecossistema de Ferramentas" width="800"/>

*Qualquer método Java anotado com @Tool fica disponível para a IA — o padrão se estende a bancos de dados, APIs, e-mail, operações de arquivos e mais.*

## Quando Usar Agentes Baseados em Ferramentas

<img src="../../../translated_images/pt-BR/when-to-use-tools.51d1592d9cbdae9c.webp" alt="Quando Usar Ferramentas" width="800"/>

*Um guia rápido de decisão — ferramentas são para dados em tempo real, cálculos e ações; tarefas de conhecimento geral e criativas não precisam delas.*

**Use ferramentas quando:**
- A resposta requer dados em tempo real (clima, preços de ações, inventário)
- Precisa realizar cálculos além da matemática simples
- Acessar bancos de dados ou APIs
- Executar ações (enviar e-mails, criar tickets, atualizar registros)
- Combinar múltiplas fontes de dados

**Não use ferramentas quando:**
- Perguntas podem ser respondidas a partir de conhecimento geral
- A resposta é puramente conversacional
- Latência da ferramenta tornaria a experiência muito lenta

## Ferramentas vs RAG

Os módulos 03 e 04 ampliam o que a IA pode fazer, mas de maneiras fundamentalmente diferentes. RAG dá ao modelo acesso a **conhecimento** recuperando documentos. Ferramentas dão ao modelo a capacidade de tomar **ações** chamando funções.

<img src="../../../translated_images/pt-BR/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Comparação Ferramentas vs RAG" width="800"/>

*RAG recupera informação de documentos estáticos — Ferramentas executam ações e buscam dados dinâmicos em tempo real. Muitos sistemas produtivos combinam ambos.*

Na prática, muitos sistemas de produção combinam essas abordagens: RAG para fundamentar respostas na sua documentação, e Ferramentas para buscar dados ao vivo ou realizar operações.

## Próximos Passos

**Próximo Módulo:** [05-mcp - Protocolo de Contexto de Modelo (MCP)](../05-mcp/README.md)

---

**Navegação:** [← Anterior: Módulo 03 - RAG](../03-rag/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido usando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->