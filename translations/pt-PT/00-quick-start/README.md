# Módulo 00: Início Rápido

## Índice

- [Introdução](../../../00-quick-start)
- [O que é o LangChain4j?](../../../00-quick-start)
- [Dependências do LangChain4j](../../../00-quick-start)
- [Pré-requisitos](../../../00-quick-start)
- [Configuração](../../../00-quick-start)
  - [1. Obtenha o seu Token GitHub](../../../00-quick-start)
  - [2. Defina o seu Token](../../../00-quick-start)
- [Execute os Exemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Padrões de Prompt](../../../00-quick-start)
  - [3. Chamada de Funções](../../../00-quick-start)
  - [4. Perguntas e Respostas de Documentos (RAG)](../../../00-quick-start)
  - [5. IA Responsável](../../../00-quick-start)
- [O Que Cada Exemplo Mostra](../../../00-quick-start)
- [Próximos Passos](../../../00-quick-start)
- [Resolução de Problemas](../../../00-quick-start)

## Introdução

Este início rápido destina-se a pô-lo a usar o LangChain4j o mais rapidamente possível. Cobre o absolutamente básico para criar aplicações de IA com LangChain4j e Modelos GitHub. Nas próximas unidades usará o Azure OpenAI com LangChain4j para construir aplicações mais avançadas.

## O que é LangChain4j?

LangChain4j é uma biblioteca Java que simplifica a construção de aplicações alimentadas por IA. Em vez de lidar com clientes HTTP e análise JSON, trabalha com APIs Java limpas.

A "chain" em LangChain refere-se a encadear vários componentes — pode encadear um prompt a um modelo, a um parser, ou encadear múltiplas chamadas de IA onde uma saída serve de entrada para a seguinte. Este início rápido foca-se nos fundamentos antes de explorar cadeias mais complexas.

<img src="../../../translated_images/pt-PT/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceito de Encadeamento LangChain4j" width="800"/>

*Componentes encadeados no LangChain4j — blocos de construção conectam-se para criar fluxos de trabalho de IA poderosos*

Usaremos três componentes principais:

**ChatLanguageModel** – Interface para interações com modelos de IA. Chame `model.chat("prompt")` e obtenha uma string de resposta. Usamos `OpenAiOfficialChatModel` que funciona com endpoints compatíveis com OpenAI, como os Modelos GitHub.

**AiServices** – Cria interfaces de serviço de IA com tipo seguro. Defina métodos, anote-os com `@Tool`, e o LangChain4j trata da orquestração. A IA chama automaticamente os seus métodos Java quando necessário.

**MessageWindowChatMemory** – Mantém o histórico da conversa. Sem isto, cada pedido é independente. Com isto, a IA lembra-se de mensagens anteriores e mantém contexto em múltiplas trocas.

<img src="../../../translated_images/pt-PT/architecture.eedc993a1c576839.webp" alt="Arquitetura LangChain4j" width="800"/>

*Arquitetura LangChain4j — componentes principais a trabalhar juntos para alimentar as suas aplicações de IA*

## Dependências do LangChain4j

Este início rápido usa duas dependências Maven no [`pom.xml`](../../../00-quick-start/pom.xml):

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
```

O módulo `langchain4j-open-ai-official` fornece a classe `OpenAiOfficialChatModel` que liga a APIs compatíveis com OpenAI. O GitHub Models usa o mesmo formato de API, por isso não é necessário nenhum adaptador especial – basta apontar a URL base para `https://models.github.ai/inference`.

## Pré-requisitos

**Usa o Contêiner Dev?** Java e Maven já estão instalados. Só precisa de um Token de Acesso Pessoal GitHub.

**Desenvolvimento Local:**
- Java 21+, Maven 3.9+
- Token de Acesso Pessoal GitHub (instruções abaixo)

> **Nota:** Este módulo usa `gpt-4.1-nano` dos Modelos GitHub. Não modifique o nome do modelo no código – está configurado para trabalhar com os modelos disponíveis do GitHub.

## Configuração

### 1. Obtenha o seu Token GitHub

1. Aceda a [Configurações do GitHub → Tokens de Acesso Pessoal](https://github.com/settings/personal-access-tokens)
2. Clique em "Generate new token"
3. Defina um nome descritivo (ex: "Demo LangChain4j")
4. Defina a expiração (recomendado 7 dias)
5. Em "Account permissions", localize "Models" e defina para "Read-only"
6. Clique em "Generate token"
7. Copie e guarde o seu token – não o verá novamente

### 2. Defina o seu Token

**Opção 1: Usar VS Code (Recomendado)**

Se estiver a usar VS Code, adicione o token ao ficheiro `.env` na raiz do projeto:

Se o ficheiro `.env` não existir, copie `.env.example` para `.env` ou crie um novo ficheiro `.env` na raiz do projeto.

**Exemplo de ficheiro `.env`:**
```bash
# Em /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Depois pode simplesmente clicar com o botão direito em qualquer ficheiro demo (ex: `BasicChatDemo.java`) no Explorador e selecionar **"Run Java"** ou usar as configurações de lançamento no painel Run and Debug.

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

## Execute os Exemplos

**Usando VS Code:** Simplesmente faça clique direito em qualquer ficheiro demo no Explorador e selecione **"Run Java"**, ou use as configurações de lançamento no painel Run and Debug (certifique-se de ter adicionado o token ao ficheiro `.env` primeiro).

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

Mostra zero-shot, few-shot, cadeia de pensamento e prompts baseados em papéis.

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

### 4. Perguntas e Respostas de Documentos (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Faça perguntas sobre o conteúdo em `document.txt`.

### 5. IA Responsável

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Veja como filtros de segurança de IA bloqueiam conteúdos nocivos.

## O Que Cada Exemplo Mostra

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comece aqui para ver LangChain4j no seu estado mais simples. Vai criar um `OpenAiOfficialChatModel`, enviar um prompt com `.chat()`, e receber uma resposta. Isto demonstra a base: como inicializar modelos com endpoints personalizados e chaves API. Uma vez que compreenda este padrão, tudo o resto é construído sobre ele.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e pergunte:
> - "Como faço para mudar dos Modelos GitHub para Azure OpenAI neste código?"
> - "Que outros parâmetros posso configurar em OpenAiOfficialChatModel.builder()?"
> - "Como adiciono respostas em streaming em vez de esperar pela resposta completa?"

**Engenharia de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Agora que sabe como falar com um modelo, vamos explorar o que lhe diz. Esta demo usa a mesma configuração de modelo mas mostra cinco padrões diferentes de prompt. Experimente prompts zero-shot para instruções diretas, few-shot que aprendem por exemplos, cadeia de pensamento que revela raciocínio, e prompts baseados em papéis que definem contexto. Verá como o mesmo modelo dá resultados dramaticamente diferentes dependendo de como estrutura o seu pedido.

A demo também demonstra templates de prompt, que são uma forma poderosa de criar prompts reutilizáveis com variáveis.
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
> - "Qual é a diferença entre zero-shot e few-shot prompting, e quando devo usar cada um?"
> - "Como é que o parâmetro temperature afeta as respostas do modelo?"
> - "Quais são algumas técnicas para prevenir ataques de injeção de prompt em produção?"
> - "Como posso criar objetos PromptTemplate reutilizáveis para padrões comuns?"

**Integração de Ferramentas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aqui é onde o LangChain4j ganha poder. Vai usar `AiServices` para criar um assistente IA que pode chamar os seus métodos Java. Basta anotar métodos com `@Tool("descrição")` e o LangChain4j trata do resto – a IA decide automaticamente quando usar cada ferramenta conforme o pedido do utilizador. Isto demonstra a chamada de funções, uma técnica chave para construir IA que pode executar ações, não apenas responder perguntas.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e pergunte:
> - "Como funciona a anotação @Tool e o que o LangChain4j faz com ela nos bastidores?"
> - "A IA pode chamar várias ferramentas em sequência para resolver problemas complexos?"
> - "O que acontece se uma ferramenta lançar uma exceção – como devo tratar erros?"
> - "Como integraria uma API real em vez deste exemplo de calculadora?"

**Perguntas e Respostas de Documentos (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aqui verá a base do RAG (geração aumentada por recuperação). Em vez de confiar nos dados de treino do modelo, carrega conteúdo de [`document.txt`](../../../00-quick-start/document.txt) e inclui-o no prompt. A IA responde com base no seu documento, não no seu conhecimento geral. Este é o primeiro passo para criar sistemas que trabalham com os seus próprios dados.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Nota:** Esta abordagem simples carrega o documento inteiro no prompt. Para ficheiros grandes (>10KB), excederá os limites de contexto. O Módulo 03 cobre chunking e pesquisa vetorial para sistemas RAG de produção.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e pergunte:
> - "Como é que o RAG previne alucinações de IA comparado com usar os dados de treino do modelo?"
> - "Qual é a diferença entre esta abordagem simples e usar embeddings vetoriais para recuperação?"
> - "Como escalar isto para vários documentos ou bases de conhecimento maiores?"
> - "Quais são as melhores práticas para estruturar o prompt para garantir que a IA usa apenas o contexto fornecido?"

**IA Responsável** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construa segurança de IA com defesa em profundidade. Esta demo mostra duas camadas de proteção a trabalhar em conjunto:

**Parte 1: Guardrails de Entrada LangChain4j** – Bloqueiam prompts perigosos antes de chegarem ao LLM. Crie guardrails personalizados que verificam palavras-chave ou padrões proibidos. Correm no seu código, portanto são rápidos e gratuitos.

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

**Parte 2: Filtros de Segurança do Provedor** – Os Modelos GitHub têm filtros incorporados que captam o que os seus guardrails podem falhar. Verá bloqueios difíceis (erros HTTP 400) para violações graves e recusas suaves onde a IA recusa educadamente.

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e pergunte:
> - "O que é InputGuardrail e como criar o meu próprio?"
> - "Qual é a diferença entre um bloqueio duro e uma recusa suave?"
> - "Por que usar guardrails e filtros do provedor juntos?"

## Próximos Passos

**Próximo Módulo:** [01-introduction - Começar com LangChain4j e gpt-5 no Azure](../01-introduction/README.md)

---

**Navegação:** [← Voltar ao Inicial](../README.md) | [Próximo: Módulo 01 - Introdução →](../01-introduction/README.md)

---

## Resolução de Problemas

### Primeira Construção com Maven

**Problema:** O comando inicial `mvn clean compile` ou `mvn package` demora muito (10-15 minutos)

**Causa:** O Maven precisa descarregar todas as dependências do projeto (Spring Boot, bibliotecas LangChain4j, SDKs Azure, etc.) na primeira construção.

**Solução:** Este é um comportamento normal. As construções seguintes serão muito mais rápidas pois as dependências ficam armazenadas localmente. O tempo de download depende da velocidade da sua rede.
### Sintaxe do Comando Maven no PowerShell

**Problema**: Os comandos Maven falham com o erro `Unknown lifecycle phase ".mainClass=..."`

**Causa**: O PowerShell interpreta `=` como operador de atribuição de variável, interrompendo a sintaxe das propriedades do Maven

**Solução**: Use o operador de paragem de análise `--%` antes do comando Maven:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

O operador `--%` diz ao PowerShell para passar todos os argumentos restantes literalmente para o Maven, sem interpretação.

### Exibição de Emojis no Windows PowerShell

**Problema**: Respostas da IA mostram caracteres indecifráveis (ex., `????` ou `â??`) em vez de emojis no PowerShell

**Causa**: A codificação padrão do PowerShell não suporta emojis UTF-8

**Solução**: Execute este comando antes de executar aplicações Java:
```cmd
chcp 65001
```

Isto força a codificação UTF-8 no terminal. Alternativamente, utilize o Windows Terminal que tem melhor suporte a Unicode.

### Depuração de Chamadas à API

**Problema**: Erros de autenticação, limites de taxa ou respostas inesperadas do modelo de IA

**Solução**: Os exemplos incluem `.logRequests(true)` e `.logResponses(true)` para mostrar chamadas à API na consola. Isto ajuda a resolver erros de autenticação, limites de taxa ou respostas inesperadas. Remova estes flags em produção para reduzir o ruído nos registos.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Apesar dos nossos esforços para garantir a precisão, tenha em atenção que traduções automáticas podem conter erros ou imprecisões. O documento original, no seu idioma nativo, deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se a tradução profissional humana. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->