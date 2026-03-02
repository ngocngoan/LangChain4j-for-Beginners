# Module 00: Quick Start

## Table of Contents

- [Introdução](../../../00-quick-start)
- [O que é LangChain4j?](../../../00-quick-start)
- [Dependências do LangChain4j](../../../00-quick-start)
- [Pré-requisitos](../../../00-quick-start)
- [Configuração](../../../00-quick-start)
  - [1. Obter o seu Token do GitHub](../../../00-quick-start)
  - [2. Definir o seu Token](../../../00-quick-start)
- [Executar os Exemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Padrões de Prompt](../../../00-quick-start)
  - [3. Chamada de Funções](../../../00-quick-start)
  - [4. Perguntas e Respostas a Documentos (Easy RAG)](../../../00-quick-start)
  - [5. IA Responsável](../../../00-quick-start)
- [O que cada exemplo demonstra](../../../00-quick-start)
- [Próximos Passos](../../../00-quick-start)
- [Resolução de Problemas](../../../00-quick-start)

## Introdução

Este quickstart destina-se a colocá-lo a trabalhar com LangChain4j da forma mais rápida possível. Cobre o básico absoluto da construção de aplicações de IA com LangChain4j e Modelos GitHub. Nos próximos módulos, irá mudar para Azure OpenAI e GPT-5.2 e aprofundar cada conceito.

## O que é LangChain4j?

LangChain4j é uma biblioteca Java que simplifica a construção de aplicações potenciadas por IA. Em vez de lidar com cliente HTTP e parsing JSON, trabalha com APIs Java limpas.

O "chain" (cadeia) em LangChain refere-se a encadear vários componentes – pode encadear um prompt a um modelo, a um parser, ou encadear múltiplas chamadas de IA onde uma saída alimenta a próxima entrada. Este início rápido foca-se nos fundamentos antes de explorar cadeias mais complexas.

<img src="../../../translated_images/pt-PT/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Encadeamento de componentes no LangChain4j - blocos de construção que ligam para criar fluxos de trabalho poderosos de IA*

Usaremos três componentes principais:

**ChatModel** - A interface para interações com modelos de IA. Chame `model.chat("prompt")` e obtenha uma string de resposta. Usamos `OpenAiOfficialChatModel` que funciona com endpoints compatíveis com OpenAI, como os Modelos do GitHub.

**AiServices** - Cria interfaces de serviço de IA com tipagem segura. Defina métodos, anote-os com `@Tool`, e LangChain4j gere a orquestração. A IA chama automaticamente os seus métodos Java quando necessário.

**MessageWindowChatMemory** - Mantém o histórico da conversação. Sem isto, cada pedido é independente. Com ele, a IA lembra mensagens anteriores e mantém o contexto através de múltiplos turnos.

<img src="../../../translated_images/pt-PT/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*Arquitetura do LangChain4j - componentes principais a trabalhar juntos para potenciar as suas aplicações de IA*

## Dependências do LangChain4j

Este quick start usa três dependências Maven no [`pom.xml`](../../../00-quick-start/pom.xml):

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

O módulo `langchain4j-open-ai-official` fornece a classe `OpenAiOfficialChatModel` que liga a APIs compatíveis com OpenAI. Os Modelos do GitHub usam o mesmo formato de API, por isso não é necessário nenhum adaptador especial - basta apontar a URL base para `https://models.github.ai/inference`.

O módulo `langchain4j-easy-rag` fornece divisão automática de documentos, embedding e recuperação para que possa construir aplicações RAG sem configurar cada passo manualmente.

## Pré-requisitos

**A usar o Dev Container?** Java e Maven já estão instalados. Só precisa de um Token de Acesso Pessoal GitHub.

**Desenvolvimento Local:**
- Java 21+, Maven 3.9+
- Token de Acesso Pessoal GitHub (instruções abaixo)

> **Nota:** Este módulo usa `gpt-4.1-nano` dos Modelos GitHub. Não modifique o nome do modelo no código - está configurado para funcionar com os modelos disponíveis no GitHub.

## Configuração

### 1. Obter o seu Token do GitHub

1. Vá a [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Clique em "Generate new token"
3. Defina um nome descritivo (ex.: "LangChain4j Demo")
4. Defina a expiração (recomendado 7 dias)
5. Em "Account permissions", encontre "Models" e defina para "Read-only"
6. Clique em "Generate token"
7. Copie e guarde o seu token - não voltará a vê-lo

### 2. Definir o seu Token

**Opção 1: Usando VS Code (Recomendado)**

Se usa VS Code, adicione o seu token no ficheiro `.env` na raiz do projeto:

Se o ficheiro `.env` não existir, copie `.env.example` para `.env` ou crie um novo ficheiro `.env` na raiz do projeto.

**Exemplo de ficheiro `.env`:**
```bash
# Em /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Depois pode simplesmente clicar com o botão direito em qualquer ficheiro demo (ex.: `BasicChatDemo.java`) no Explorador e selecionar **"Run Java"**, ou usar as configurações de lançamento do painel Run and Debug.

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

## Executar os Exemplos

**Usando VS Code:** Basta clicar com o botão direito em qualquer ficheiro demo no Explorador e selecionar **"Run Java"**, ou usar as configurações de lançamento do painel Run and Debug (certifique-se que adicionou o token ao ficheiro `.env` primeiro).

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

Mostra prompting zero-shot, few-shot, chain-of-thought e baseado em papéis.

### 3. Chamada de Funções

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

A IA chama automaticamente os seus métodos Java quando necessário.

### 4. Perguntas e Respostas a Documentos (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Faça perguntas sobre os seus documentos usando Easy RAG com embedding e recuperação automáticos.

### 5. IA Responsável

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Veja como os filtros de segurança da IA bloqueiam conteúdos prejudiciais.

## O que cada exemplo demonstra

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comece aqui para ver LangChain4j no seu estado mais simples. Vai criar um `OpenAiOfficialChatModel`, enviar um prompt com `.chat()` e obter uma resposta. Isto demonstra a base: como inicializar modelos com endpoints personalizados e chaves API. Quando compreender este padrão, tudo o resto assenta nele.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e pergunte:
> - "Como mudo dos Modelos GitHub para Azure OpenAI neste código?"
> - "Que outros parâmetros posso configurar em OpenAiOfficialChatModel.builder()?"
> - "Como adiciono respostas em streaming em vez de esperar pela resposta completa?"

**Engenharia de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Agora que sabe como falar com um modelo, vamos explorar o que lhe diz. Este demo usa a mesma configuração de modelo mas mostra cinco padrões diferentes de prompting. Experimente prompts zero-shot para instruções diretas, few-shot que aprendem com exemplos, chain-of-thought que revelam os passos do raciocínio, e prompts baseados em papéis que definem contexto. Verá como o mesmo modelo dá resultados dramaticamente diferentes consoante como formula o pedido.

O demo também demonstra templates de prompt, que são uma forma poderosa de criar prompts reutilizáveis com variáveis.
O exemplo abaixo mostra um prompt usando o `PromptTemplate` do LangChain4j para preencher variáveis. A IA responderá com base no destino e atividade fornecidos.

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

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) e pergunte:
> - "Qual é a diferença entre prompting zero-shot e few-shot, e quando devo usar cada um?"
> - "Como o parâmetro temperatura afeta as respostas do modelo?"
> - "Quais são algumas técnicas para prevenir ataques de injeção de prompt em produção?"
> - "Como posso criar objetos PromptTemplate reutilizáveis para padrões comuns?"

**Integração com Ferramentas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aqui é onde LangChain4j se torna poderoso. Vai usar `AiServices` para criar um assistente de IA que pode chamar os seus métodos Java. Basta anotar os métodos com `@Tool("descrição")` e LangChain4j trata do resto – a IA decide automaticamente quando usar cada ferramenta com base no que o utilizador pede. Isto demonstra a chamada de funções, uma técnica chave para construir IA que pode tomar ações, não apenas responder a perguntas.

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

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e pergunte:
> - "Como funciona a anotação @Tool e o que o LangChain4j faz com ela por trás das cenas?"
> - "Pode a IA chamar múltiplas ferramentas em sequência para resolver problemas complexos?"
> - "O que acontece se uma ferramenta lançar uma exceção - como devo tratar erros?"
> - "Como integraria uma API real em vez deste exemplo da calculadora?"

**Perguntas e Respostas a Documentos (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aqui verá RAG (geração aumentada por recuperação) usando a abordagem "Easy RAG" do LangChain4j. Documentos são carregados, automaticamente divididos e embedados numa store em memória, depois um recuperador de conteúdo fornece pedaços relevantes à IA no momento da consulta. A IA responde com base nos seus documentos, não no seu conhecimento geral.

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

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e pergunte:
> - "Como é que o RAG previne alucinações da IA comparado ao uso dos dados de treino do modelo?"
> - "Qual é a diferença entre esta abordagem fácil e uma pipeline RAG personalizada?"
> - "Como escalaria isto para lidar com múltiplos documentos ou bases de conhecimento maiores?"

**IA Responsável** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construa segurança na IA com defesa em profundidade. Este demo mostra duas camadas de proteção a funcionar juntas:

**Parte 1: LangChain4j Input Guardrails** - Bloqueia prompts perigosos antes de chegarem ao LLM. Crie guardrails personalizados que verificam palavras-chave ou padrões proibidos. Estes correm no seu código, portanto são rápidos e gratuitos.

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

**Parte 2: Filtros de Segurança do Provedor** - Os Modelos GitHub têm filtros embutidos que apanham o que os seus guardrails possam falhar. Verá bloqueios fortes (erros HTTP 400) para violações graves e recusas suaves onde a IA recusa educadamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e pergunte:
> - "O que é InputGuardrail e como crio o meu próprio?"
> - "Qual é a diferença entre um bloqueio forte e uma recusa suave?"
> - "Porque usar guardrails e filtros do provedor em conjunto?"

## Próximos Passos

**Módulo Seguinte:** [01-introduction - Começar com LangChain4j](../01-introduction/README.md)

---

**Navegação:** [← Voltar ao Principal](../README.md) | [Seguinte: Módulo 01 - Introdução →](../01-introduction/README.md)

---

## Resolução de Problemas

### Primeira Compilação Maven

**Problema**: `mvn clean compile` ou `mvn package` inicial demora muito (10-15 minutos)

**Causa**: O Maven precisa de descarregar todas as dependências do projeto (Spring Boot, bibliotecas LangChain4j, SDKs Azure, etc.) na primeira compilação.

**Solução**: Este comportamento é normal. As compilações subsequentes serão muito mais rápidas pois as dependências ficam cacheadas localmente. O tempo de descarga depende da velocidade da sua rede.

### Sintaxe dos Comandos Maven no PowerShell

**Problema**: Comandos Maven falham com erro `Unknown lifecycle phase ".mainClass=..."`
**Causa**: PowerShell interpreta `=` como operador de atribuição de variável, quebrando a sintaxe da propriedade Maven

**Solução**: Use o operador de paragem de análise `--%` antes do comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

O operador `--%` indica ao PowerShell que passe todos os argumentos restantes literalmente para o Maven sem interpretação.

### Exibição de Emojis no Windows PowerShell

**Problema**: Respostas da IA mostram caracteres indesejados (ex.: `????` ou `â??`) em vez de emojis no PowerShell

**Causa**: A codificação padrão do PowerShell não suporta emojis UTF-8

**Solução**: Execute este comando antes de executar aplicações Java:
```cmd
chcp 65001
```

Isto força a codificação UTF-8 no terminal. Alternativamente, use o Windows Terminal que tem melhor suporte a Unicode.

### Depuração de Chamadas API

**Problema**: Erros de autenticação, limites de taxa ou respostas inesperadas do modelo IA

**Solução**: Os exemplos incluem `.logRequests(true)` e `.logResponses(true)` para mostrar as chamadas API na consola. Isto ajuda a diagnosticar erros de autenticação, limites de taxa ou respostas inesperadas. Remova estes flags em produção para reduzir o ruído nos logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, tenha em atenção que as traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autoritativa. Para informações críticas, recomendam-se traduções profissionais feitas por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->