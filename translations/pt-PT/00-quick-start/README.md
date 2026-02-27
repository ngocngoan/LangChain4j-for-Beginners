# Módulo 00: Início Rápido

## Índice

- [Introdução](../../../00-quick-start)
- [O que é LangChain4j?](../../../00-quick-start)
- [Dependências do LangChain4j](../../../00-quick-start)
- [Pré-requisitos](../../../00-quick-start)
- [Configuração](../../../00-quick-start)
  - [1. Obter o seu Token do GitHub](../../../00-quick-start)
  - [2. Definir o Seu Token](../../../00-quick-start)
- [Executar os Exemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Padrões de Prompt](../../../00-quick-start)
  - [3. Chamada de Função](../../../00-quick-start)
  - [4. Perguntas & Respostas em Documentos (Easy RAG)](../../../00-quick-start)
  - [5. IA Responsável](../../../00-quick-start)
- [O que Cada Exemplo Mostra](../../../00-quick-start)
- [Próximos Passos](../../../00-quick-start)
- [Resolução de Problemas](../../../00-quick-start)

## Introdução

Este início rápido destina-se a pô-lo a funcionar com LangChain4j o mais rapidamente possível. Cobre o básico absoluto da construção de aplicações de IA com LangChain4j e Modelos GitHub. Nos próximos módulos, usará Azure OpenAI com LangChain4j para construir aplicações mais avançadas.

## O que é LangChain4j?

LangChain4j é uma biblioteca Java que simplifica a construção de aplicações potenciadas por IA. Em vez de lidar com clientes HTTP e parsing JSON, trabalha-se com APIs Java limpas.

A “chain” em LangChain refere-se a encadear múltiplos componentes – pode encadear um prompt a um modelo a um parser, ou encadear múltiplas chamadas de IA em sequência onde a saída de uma alimenta a entrada seguinte. Este início rápido foca-se nos fundamentos antes de explorar cadeias mais complexas.

<img src="../../../translated_images/pt-PT/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Encadeamento de componentes em LangChain4j – blocos de construção que se ligam para criar fluxos de trabalho de IA poderosos*

Usaremos três componentes principais:

**ChatModel** – A interface para interações com o modelo de IA. Chame `model.chat("prompt")` e obtenha uma resposta em texto. Usamos `OpenAiOfficialChatModel` que funciona com endpoints compatíveis com OpenAI, como os Modelos GitHub.

**AiServices** – Cria interfaces de serviços de IA type-safe. Defina métodos, anote-os com `@Tool` e o LangChain4j trata da orquestração. A IA chama automaticamente os seus métodos Java quando necessário.

**MessageWindowChatMemory** – Mantém o histórico da conversa. Sem isto, cada pedido é independente. Com ele, a IA lembra mensagens anteriores e mantém o contexto ao longo de múltiplas interações.

<img src="../../../translated_images/pt-PT/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arquitetura do LangChain4j – componentes principais a trabalhar juntos para potenciar as suas aplicações de IA*

## Dependências do LangChain4j

Este início rápido usa três dependências Maven em [`pom.xml`](../../../00-quick-start/pom.xml):

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

O módulo `langchain4j-open-ai-official` fornece a classe `OpenAiOfficialChatModel` que se liga a APIs compatíveis com OpenAI. Os Modelos GitHub usam o mesmo formato de API, por isso não é preciso um adaptador especial – basta apontar o URL base para `https://models.github.ai/inference`.

O módulo `langchain4j-easy-rag` fornece divisão automática de documentos, embedding e recuperação para que possa construir aplicações RAG sem configurar cada passo manualmente.

## Pré-requisitos

**A usar o Dev Container?** Java e Maven já estão instalados. Só precisa de um Token de Acesso Pessoal do GitHub.

**Desenvolvimento Local:**
- Java 21+, Maven 3.9+
- Token de Acesso Pessoal do GitHub (instruções abaixo)

> **Nota:** Este módulo usa `gpt-4.1-nano` dos Modelos GitHub. Não modifique o nome do modelo no código – está configurado para funcionar com os modelos disponíveis do GitHub.

## Configuração

### 1. Obter o seu Token do GitHub

1. Vá a [Configurações do GitHub → Tokens de Acesso Pessoal](https://github.com/settings/personal-access-tokens)
2. Clique em "Generate new token"
3. Defina um nome descritivo (ex., "LangChain4j Demo")
4. Defina a expiração (7 dias recomendado)
5. Em "Account permissions", encontre "Models" e defina para "Read-only"
6. Clique em "Generate token"
7. Copie e guarde o token – não o verá novamente

### 2. Definir o Seu Token

**Opção 1: Usar VS Code (Recomendado)**

Se estiver a usar VS Code, adicione o seu token ao ficheiro `.env` na raiz do projeto:

Se o ficheiro `.env` não existir, copie `.env.example` para `.env` ou crie um novo ficheiro `.env` na raiz do projeto.

**Exemplo de ficheiro `.env`:**
```bash
# Em /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Depois pode simplesmente clicar com o botão direito num ficheiro de demo (ex., `BasicChatDemo.java`) no Explorer e selecionar **"Run Java"** ou usar as configurações de lançamento no painel Executar e Depurar.

**Opção 2: Usar Terminal**

Defina o token como variável de ambiente:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Executar os Exemplos

**Usando VS Code:** Basta clicar com o botão direito em qualquer ficheiro demo no Explorer e escolher **"Run Java"**, ou usar as configurações de lançamento do painel Executar e Depurar (certifique-se de ter adicionado primeiro o seu token no ficheiro `.env`).

**Usando Maven:** Alternativamente, pode executar pela linha de comandos:

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

Mostra prompting zero-shot, few-shot, chain-of-thought, e com base em papéis.

### 3. Chamada de Função

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

A IA chama automaticamente os seus métodos Java quando necessário.

### 4. Perguntas & Respostas em Documentos (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Coloque perguntas sobre os seus documentos usando Easy RAG com embedding e recuperação automáticos.

### 5. IA Responsável

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Veja como os filtros de segurança da IA bloqueiam conteúdos nocivos.

## O que Cada Exemplo Mostra

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comece aqui para ver LangChain4j no seu estado mais simples. Cria um `OpenAiOfficialChatModel`, envia um prompt com `.chat()` e obtém uma resposta. Isto demonstra a base: como inicializar modelos com endpoints personalizados e chaves API. Depois de compreender este padrão, tudo o resto constrói-se a partir daqui.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e pergunte:
> - "Como mudo dos Modelos GitHub para o Azure OpenAI neste código?"
> - "Que outros parâmetros posso configurar em OpenAiOfficialChatModel.builder()?"
> - "Como adiciono respostas em streaming em vez de esperar pela resposta completa?"

**Engenharia de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Agora que sabe como falar com um modelo, explore o que lhe diz. Esta demo usa a mesma configuração de modelo, mas mostra cinco padrões diferentes de prompting. Experimente prompts zero-shot para instruções diretas, few-shot que aprendem com exemplos, chain-of-thought que revelam passos de raciocínio, e prompts baseados em papéis que definem contexto. Verá como o mesmo modelo produz resultados muito diferentes consoante o enquadramento do pedido.

A demo também demonstra templates de prompt, que são uma forma poderosa de criar prompts reutilizáveis com variáveis.
O exemplo abaixo mostra um prompt usando `PromptTemplate` do LangChain4j para preencher variáveis. A IA responde com base no destino e atividade fornecidos.

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

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) e pergunte:
> - "Qual é a diferença entre prompting zero-shot e few-shot, e quando devo usar cada um?"
> - "Como o parâmetro temperature afeta as respostas do modelo?"
> - "Quais são algumas técnicas para prevenir ataques de prompt injection em produção?"
> - "Como posso criar objetos PromptTemplate reutilizáveis para padrões comuns?"

**Integração de Ferramentas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

É aqui que o LangChain4j fica poderoso. Usará `AiServices` para criar um assistente IA que chama os seus métodos Java. Basta anotar os métodos com `@Tool("descrição")` e o LangChain4j trata do resto – a IA decide automaticamente quando usar cada ferramenta baseado no que o utilizador pede. Isto demonstra a chamada de funções, uma técnica chave para construir IA que pode agir, não só responder perguntas.

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

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e pergunte:
> - "Como funciona a anotação @Tool e o que o LangChain4j faz com ela nos bastidores?"
> - "A IA pode chamar múltiplas ferramentas em sequência para resolver problemas complexos?"
> - "O que acontece se uma ferramenta lançar uma exceção – como devo tratar erros?"
> - "Como integraria uma API real em vez deste exemplo de calculadora?"

**Perguntas & Respostas em Documentos (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aqui verá RAG (geração aumentada por recuperação) usando a abordagem "Easy RAG" do LangChain4j. Os documentos são carregados, automaticamente separados e embutidos numa store em memória, depois um recuperador de conteúdos fornece pedaços relevantes à IA no momento da consulta. A IA responde com base nos seus documentos, não no conhecimento geral.

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

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e pergunte:
> - "Como o RAG previne alucinações da IA comparado com usar os dados de treino do modelo?"
> - "Qual é a diferença entre esta abordagem fácil e um pipeline RAG personalizado?"
> - "Como escalar isto para lidar com múltiplos documentos ou bases de conhecimento maiores?"

**IA Responsável** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construa segurança de IA com defesa em profundidade. Esta demo mostra duas camadas de proteção a trabalhar juntas:

**Parte 1: LangChain4j Input Guardrails** – Bloqueie prompts perigosos antes de chegarem ao LLM. Crie guardrails personalizados que verifiquem palavras-chave ou padrões proibidos. Correm no seu código, por isso são rápidos e gratuitos.

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

**Parte 2: Filtros de Segurança do Provedor** – Os Modelos GitHub têm filtros incorporados que detectam o que os seus guardrails podem falhar. Verá bloqueios severos (erros HTTP 400) e recusas suaves onde a IA recusa educadamente.

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e pergunte:
> - "O que é InputGuardrail e como crio o meu próprio?"
> - "Qual é a diferença entre um bloqueio severo e uma recusa suave?"
> - "Por que usar guardrails e filtros do provedor juntos?"

## Próximos Passos

**Próximo Módulo:** [01-introduction - Começando com LangChain4j e gpt-5 no Azure](../01-introduction/README.md)

---

**Navegação:** [← Voltar ao Principal](../README.md) | [Próximo: Módulo 01 - Introdução →](../01-introduction/README.md)

---

## Resolução de Problemas

### Primeira Construção Maven

**Problema**: O comando inicial `mvn clean compile` ou `mvn package` demora muito (10-15 minutos)

**Causa**: O Maven precisa descarregar todas as dependências do projeto (Spring Boot, bibliotecas LangChain4j, SDKs Azure, etc.) na primeira construção.

**Solução**: É um comportamento normal. Construções subsequentes serão muito mais rápidas pois as dependências ficam em cache localmente. A duração do download depende da velocidade da rede.

### Sintaxe dos Comandos Maven no PowerShell

**Problema**: Os comandos Maven falham com o erro `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` como um operador de atribuição de variável, quebrando a sintaxe da propriedade Maven

**Solução**: Use o operador de paragem de parsing `--%` antes do comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

O operador `--%` diz ao PowerShell para passar todos os argumentos restantes literalmente para o Maven sem interpretação.

### Exibição de Emojis no Windows PowerShell

**Problema**: Respostas da IA mostram caracteres ilegíveis (ex.: `????` ou `â??`) em vez de emojis no PowerShell

**Causa**: A codificação padrão do PowerShell não suporta emojis UTF-8

**Solução**: Execute este comando antes de executar aplicações Java:
```cmd
chcp 65001
```

Isto força a codificação UTF-8 no terminal. Alternativamente, use o Windows Terminal que tem melhor suporte a Unicode.

### Depuração de chamadas API

**Problema**: Erros de autenticação, limites de taxa, ou respostas inesperadas do modelo AI

**Solução**: Os exemplos incluem `.logRequests(true)` e `.logResponses(true)` para mostrar chamadas API na consola. Isto ajuda a resolver erros de autenticação, limites de taxa, ou respostas inesperadas. Remova estas flags na produção para reduzir o ruído dos logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por alcançar a máxima precisão, por favor tenha em mente que traduções automatizadas podem conter erros ou imprecisões. O documento original, na sua língua nativa, deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional feita por um ser humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas resultantes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->