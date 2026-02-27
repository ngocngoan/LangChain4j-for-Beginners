# Módulo 00: Início Rápido

## Índice

- [Introdução](../../../00-quick-start)
- [O que é LangChain4j?](../../../00-quick-start)
- [Dependências do LangChain4j](../../../00-quick-start)
- [Pré-requisitos](../../../00-quick-start)
- [Configuração](../../../00-quick-start)
  - [1. Obtenha seu Token do GitHub](../../../00-quick-start)
  - [2. Configure seu Token](../../../00-quick-start)
- [Execute os Exemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Padrões de Prompt](../../../00-quick-start)
  - [3. Chamada de Funções](../../../00-quick-start)
  - [4. Perguntas e Respostas sobre Documentos (Easy RAG)](../../../00-quick-start)
  - [5. IA Responsável](../../../00-quick-start)
- [O que Cada Exemplo Mostra](../../../00-quick-start)
- [Próximos Passos](../../../00-quick-start)
- [Resolução de Problemas](../../../00-quick-start)

## Introdução

Este início rápido tem o objetivo de colocá-lo em funcionamento com LangChain4j o mais rápido possível. Ele cobre o básico absoluto de como construir aplicações de IA com LangChain4j e Modelos do GitHub. Nos próximos módulos você usará Azure OpenAI com LangChain4j para construir aplicações mais avançadas.

## O que é LangChain4j?

LangChain4j é uma biblioteca Java que simplifica a construção de aplicações alimentadas por IA. Ao invés de lidar com clientes HTTP e parsing de JSON, você trabalha com APIs Java limpas.

O "chain" em LangChain refere-se a encadear múltiplos componentes — você pode encadear um prompt a um modelo, a um parser, ou encadear múltiplas chamadas de IA onde a saída de uma alimenta a próxima entrada. Este início rápido foca nos fundamentos antes de explorar cadeias mais complexas.

<img src="../../../translated_images/pt-BR/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceito de Encadeamento LangChain4j" width="800"/>

*Encadeamento de componentes no LangChain4j – blocos de construção conectam para criar fluxos de trabalho poderosos de IA*

Usaremos três componentes principais:

**ChatModel** - A interface para interações com modelos de IA. Chame `model.chat("prompt")` e receba uma resposta em string. Usamos `OpenAiOfficialChatModel`, que funciona com endpoints compatíveis com OpenAI, como Modelos do GitHub.

**AiServices** - Cria interfaces de serviço de IA com tipagem segura. Defina métodos, anote-os com `@Tool` e LangChain4j gerencia a orquestração. A IA chama automaticamente seus métodos Java quando necessário.

**MessageWindowChatMemory** - Mantém o histórico da conversa. Sem isso, cada requisição é independente. Com isso, a IA lembra das mensagens anteriores e mantém o contexto ao longo de várias interações.

<img src="../../../translated_images/pt-BR/architecture.eedc993a1c576839.webp" alt="Arquitetura LangChain4j" width="800"/>

*Arquitetura LangChain4j – componentes centrais trabalhando juntos para alimentar suas aplicações de IA*

## Dependências do LangChain4j

Este início rápido usa três dependências Maven no [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

O módulo `langchain4j-open-ai-official` fornece a classe `OpenAiOfficialChatModel` que conecta a APIs compatíveis com OpenAI. Os Modelos do GitHub usam o mesmo formato de API, então não é necessário adaptador especial — basta configurar a URL base para `https://models.github.ai/inference`.

O módulo `langchain4j-easy-rag` fornece divisão automática de documentos, embedding e recuperação para que você possa construir aplicações RAG sem configurar manualmente cada etapa.

## Pré-requisitos

**Usando o Contêiner de Desenvolvimento?** Java e Maven já estão instalados. Você só precisa de um Token de Acesso Pessoal do GitHub.

**Desenvolvimento Local:**
- Java 21+, Maven 3.9+
- Token de Acesso Pessoal do GitHub (instruções abaixo)

> **Nota:** Este módulo usa `gpt-4.1-nano` dos Modelos do GitHub. Não modifique o nome do modelo no código – ele está configurado para funcionar com os modelos disponíveis do GitHub.

## Configuração

### 1. Obtenha seu Token do GitHub

1. Acesse [Configurações do GitHub → Tokens de Acesso Pessoal](https://github.com/settings/personal-access-tokens)
2. Clique em "Gerar novo token"
3. Defina um nome descritivo (ex.: "Demo LangChain4j")
4. Defina a expiração (7 dias recomendado)
5. Em "Permissões da conta", localize "Models" e defina como "Somente leitura"
6. Clique em "Gerar token"
7. Copie e salve seu token – você não poderá vê-lo novamente

### 2. Configure seu Token

**Opção 1: Usando VS Code (Recomendado)**

Se você usa VS Code, adicione seu token no arquivo `.env` na raiz do projeto:

Se o arquivo `.env` não existir, copie `.env.example` para `.env` ou crie um novo `.env` na raiz do projeto.

**Exemplo de arquivo `.env`:**
```bash
# Em /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Então você pode simplesmente clicar com o botão direito em qualquer arquivo de demonstração (ex.: `BasicChatDemo.java`) no Explorer e selecionar **"Run Java"** ou usar as configurações de execução no painel de Execução e Depuração.

**Opção 2: Usando Terminal**

Defina o token como variável de ambiente:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Execute os Exemplos

**Usando VS Code:** Apenas clique com o botão direito em qualquer arquivo de demo no Explorer e selecione **"Run Java"**, ou use as configurações de lançamento no painel de Execução e Depuração (certifique-se de ter adicionado seu token no arquivo `.env` primeiro).

**Usando Maven:** Alternativamente, você pode executar pelo terminal:

### 1. Chat Básico

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Padrões de Prompt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Mostra prompts zero-shot, few-shot, cadeia de pensamento e baseados em função.

### 3. Chamada de Funções

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

A IA chama automaticamente seus métodos Java quando necessário.

### 4. Perguntas e Respostas sobre Documentos (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Pergunte sobre seus documentos usando Easy RAG com embedding e recuperação automáticos.

### 5. IA Responsável

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Veja como filtros de segurança da IA bloqueiam conteúdo nocivo.

## O que Cada Exemplo Mostra

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comece aqui para ver o LangChain4j em sua forma mais simples. Você criará um `OpenAiOfficialChatModel`, enviará um prompt com `.chat()` e obterá uma resposta. Isso demonstra a base: como inicializar modelos com endpoints e chaves API customizados. Quando entender esse padrão, o restante é construído sobre ele.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e pergunte:
> - "Como eu mudaria de Modelos GitHub para Azure OpenAI neste código?"
> - "Quais outros parâmetros posso configurar no OpenAiOfficialChatModel.builder()?"
> - "Como adiciono respostas em streaming ao invés de esperar pela resposta completa?"

**Engenharia de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Agora que você sabe como conversar com um modelo, vamos explorar o que você diz a ele. Este demo usa a mesma configuração de modelo, mas mostra cinco padrões diferentes de prompt. Experimente prompts zero-shot para instruções diretas, few-shot que aprendem com exemplos, cadeia-de-pensamento que revela passos raciocínio, e prompts baseados em papéis que definem contexto. Você verá como o mesmo modelo pode dar resultados dramaticamente diferentes dependendo de como o pedido é formulado.

O demo também demonstra templates de prompt, que são uma forma poderosa de criar prompts reutilizáveis com variáveis.
O exemplo a seguir mostra um prompt usando o `PromptTemplate` do LangChain4j para preencher variáveis. A IA responderá com base no destino e atividade fornecidos.

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

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) e pergunte:
> - "Qual a diferença entre prompts zero-shot e few-shot, e quando usar cada um?"
> - "Como o parâmetro temperature afeta as respostas do modelo?"
> - "Quais técnicas existem para prevenir ataques de injeção de prompt em produção?"
> - "Como criar objetos PromptTemplate reutilizáveis para padrões comuns?"

**Integração de Ferramentas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

É aqui que LangChain4j se torna poderoso. Você usará `AiServices` para criar um assistente de IA que pode chamar seus métodos Java. Basta anotar métodos com `@Tool("descrição")` e o LangChain4j cuida do resto — a IA decide automaticamente quando usar cada ferramenta com base no que o usuário pede. Isso demonstra a chamada de funções, técnica-chave para construir IA que pode agir, não só responder perguntas.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e pergunte:
> - "Como funciona a anotação @Tool e o que o LangChain4j faz com ela nos bastidores?"
> - "A IA pode chamar várias ferramentas em sequência para resolver problemas complexos?"
> - "O que acontece se uma ferramenta lançar uma exceção — como devo tratar erros?"
> - "Como eu integraria uma API real ao invés deste exemplo de calculadora?"

**Perguntas e Respostas sobre Documentos (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aqui você verá RAG (geração aumentada por recuperação) usando a abordagem Easy RAG do LangChain4j. Documentos são carregados, automaticamente divididos e embedados em uma base de dados em memória, depois um recuperador de conteúdo fornece pedaços relevantes para a IA no momento da consulta. A IA responde com base nos seus documentos, não no conhecimento geral.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e pergunte:
> - "Como o RAG evita alucinações da IA comparado ao uso dos dados de treinamento do modelo?"
> - "Qual a diferença entre essa abordagem fácil e um pipeline RAG personalizado?"
> - "Como escalar isso para lidar com múltiplos documentos ou bases de conhecimento maiores?"

**IA Responsável** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construa segurança de IA com defesa em profundidade. Este demo mostra duas camadas de proteção funcionando juntas:

**Parte 1: Guardrails de Entrada do LangChain4j** - Bloqueia prompts perigosos antes que cheguem ao LLM. Crie guardrails customizados que verificam palavras ou padrões proibidos. Eles rodem no seu código, portanto são rápidos e gratuitos.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Parte 2: Filtros de Segurança do Provedor** - Modelos do GitHub têm filtros embutidos que capturam o que seus guardrails podem perder. Você verá bloqueios rígidos (erros HTTP 400) para violações severas e recusas suaves onde a IA educadamente recusa.

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e pergunte:
> - "O que é InputGuardrail e como crio o meu próprio?"
> - "Qual a diferença entre um bloqueio rígido e uma recusa suave?"
> - "Por que usar guardrails e filtros do provedor juntos?"

## Próximos Passos

**Próximo Módulo:** [01-introduction - Começando com LangChain4j e gpt-5 no Azure](../01-introduction/README.md)

---

**Navegação:** [← Voltar ao Principal](../README.md) | [Próximo: Módulo 01 - Introdução →](../01-introduction/README.md)

---

## Resolução de Problemas

### Primeiro Build Maven

**Problema:** O comando inicial `mvn clean compile` ou `mvn package` leva muito tempo (10-15 minutos)

**Causa:** O Maven precisa baixar todas as dependências do projeto (Spring Boot, bibliotecas LangChain4j, SDKs do Azure, etc.) na primeira build.

**Solução:** Isso é comportamento normal. Builds subsequentes serão muito mais rápidos pois as dependências ficam em cache localmente. O tempo de download depende da velocidade da sua conexão.

### Sintaxe de Comando Maven no PowerShell

**Problema:** Comandos Maven falham com erro `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` como um operador de atribuição de variável, quebrando a sintaxe da propriedade do Maven

**Solução**: Use o operador de parada de parsing `--%` antes do comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

O operador `--%` instrui o PowerShell a passar todos os argumentos restantes literalmente para o Maven sem interpretação.

### Exibição de Emoji no Windows PowerShell

**Problema**: Respostas da IA mostram caracteres inválidos (ex.: `????` ou `â??`) em vez de emojis no PowerShell

**Causa**: A codificação padrão do PowerShell não suporta emojis UTF-8

**Solução**: Execute este comando antes de rodar aplicações Java:
```cmd
chcp 65001
```

Isso força a codificação UTF-8 no terminal. Alternativamente, use o Windows Terminal que possui melhor suporte a Unicode.

### Depuração de Chamadas de API

**Problema**: Erros de autenticação, limites de taxa ou respostas inesperadas do modelo de IA

**Solução**: Os exemplos incluem `.logRequests(true)` e `.logResponses(true)` para mostrar as chamadas da API no console. Isso ajuda a diagnosticar erros de autenticação, limites de taxa ou respostas inesperadas. Remova essas flags em produção para reduzir o ruído dos logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->