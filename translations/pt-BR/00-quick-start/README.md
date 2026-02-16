# Módulo 00: Início Rápido

## Índice

- [Introdução](../../../00-quick-start)
- [O que é LangChain4j?](../../../00-quick-start)
- [Dependências do LangChain4j](../../../00-quick-start)
- [Pré-requisitos](../../../00-quick-start)
- [Configuração](../../../00-quick-start)
  - [1. Obtenha seu Token do GitHub](../../../00-quick-start)
  - [2. Configure seu Token](../../../00-quick-start)
- [Executar os Exemplos](../../../00-quick-start)
  - [1. Chat Básico](../../../00-quick-start)
  - [2. Padrões de Prompt](../../../00-quick-start)
  - [3. Chamada de Função](../../../00-quick-start)
  - [4. Perguntas e Respostas em Documentos (RAG)](../../../00-quick-start)
  - [5. IA Responsável](../../../00-quick-start)
- [O Que Cada Exemplo Mostra](../../../00-quick-start)
- [Próximos Passos](../../../00-quick-start)
- [Resolução de Problemas](../../../00-quick-start)

## Introdução

Este início rápido tem o objetivo de colocá-lo em funcionamento com o LangChain4j o mais rápido possível. Cobre os fundamentos absolutos de construção de aplicações de IA com LangChain4j e modelos do GitHub. Nos próximos módulos, você usará o Azure OpenAI com LangChain4j para construir aplicações mais avançadas.

## O que é LangChain4j?

LangChain4j é uma biblioteca Java que simplifica a construção de aplicações impulsionadas por IA. Em vez de lidar com clientes HTTP e parses de JSON, você trabalha com APIs Java limpas.

A “cadeia” em LangChain refere-se a encadear múltiplos componentes - você pode encadear um prompt para um modelo para um parser, ou combinar várias chamadas de IA onde a saída de uma alimenta a entrada da seguinte. Este início rápido foca nos fundamentos antes de explorar cadeias mais complexas.

<img src="../../../translated_images/pt-BR/langchain-concept.ad1fe6cf063515e1.webp" alt="Conceito de Encadeamento LangChain4j" width="800"/>

*Encadeando componentes no LangChain4j - blocos de construção conectam para criar fluxos de trabalho de IA poderosos*

Usaremos três componentes principais:

**ChatLanguageModel** - A interface para interações com modelos de IA. Chame `model.chat("prompt")` e obtenha uma resposta em string. Usamos `OpenAiOfficialChatModel` que funciona com endpoints compatíveis com OpenAI como os Modelos do GitHub.

**AiServices** - Cria interfaces de serviços de IA com tipagem segura. Defina métodos, anote-os com `@Tool`, e o LangChain4j cuida da orquestração. A IA chama automaticamente seus métodos Java quando necessário.

**MessageWindowChatMemory** - Mantém o histórico da conversa. Sem ele, cada requisição é independente. Com ele, a IA lembra mensagens anteriores e mantém o contexto ao longo de múltiplas interações.

<img src="../../../translated_images/pt-BR/architecture.eedc993a1c576839.webp" alt="Arquitetura LangChain4j" width="800"/>

*Arquitetura LangChain4j - componentes principais trabalhando juntos para alimentar suas aplicações de IA*

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

O módulo `langchain4j-open-ai-official` fornece a classe `OpenAiOfficialChatModel` que conecta a APIs compatíveis com OpenAI. O GitHub Models usa o mesmo formato de API, então não é necessário adaptador especial - basta apontar a URL base para `https://models.github.ai/inference`.

## Pré-requisitos

**Usando o Contêiner de Desenvolvimento?** Java e Maven já estão instalados. Você só precisa de um Token de Acesso Pessoal do GitHub.

**Desenvolvimento Local:**
- Java 21+, Maven 3.9+
- Token de Acesso Pessoal do GitHub (instruções abaixo)

> **Nota:** Este módulo usa `gpt-4.1-nano` dos Modelos do GitHub. Não modifique o nome do modelo no código - ele está configurado para funcionar com os modelos disponíveis do GitHub.

## Configuração

### 1. Obtenha seu Token do GitHub

1. Vá para [Configurações do GitHub → Tokens de Acesso Pessoal](https://github.com/settings/personal-access-tokens)
2. Clique em "Gerar novo token"
3. Defina um nome descritivo (exemplo: "Demo LangChain4j")
4. Defina a validade (recomendado 7 dias)
5. Em "Permissões da conta", encontre "Models" e defina como "Somente leitura"
6. Clique em "Gerar token"
7. Copie e salve seu token - você não verá novamente

### 2. Configure seu Token

**Opção 1: Usando VS Code (Recomendado)**

Se estiver usando o VS Code, adicione seu token no arquivo `.env` na raiz do projeto:

Se o arquivo `.env` não existir, copie `.env.example` para `.env` ou crie um novo arquivo `.env` na raiz do projeto.

**Exemplo de arquivo `.env`:**
```bash
# Em /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Depois, você pode clicar com o botão direito em qualquer arquivo demo (por exemplo, `BasicChatDemo.java`) no Explorer e selecionar **"Run Java"** ou usar as configurações de execução no painel Run and Debug.

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

**Usando VS Code:** Clique com o botão direito em qualquer arquivo demo no Explorer e selecione **"Run Java"**, ou use as configurações de execução no painel Run and Debug (certifique-se de ter adicionado seu token no arquivo `.env` antes).

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

Mostra prompting zero-shot, few-shot, chain-of-thought e baseado em papéis.

### 3. Chamada de Função

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

A IA chama automaticamente seus métodos Java quando necessário.

### 4. Perguntas e Respostas em Documentos (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Faça perguntas sobre o conteúdo no `document.txt`.

### 5. IA Responsável

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Veja como filtros de segurança da IA bloqueiam conteúdos nocivos.

## O Que Cada Exemplo Mostra

**Chat Básico** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Comece aqui para ver o LangChain4j em sua forma mais simples. Você criará um `OpenAiOfficialChatModel`, enviará um prompt com `.chat()` e receberá uma resposta. Isso demonstra a base: como inicializar modelos com endpoints personalizados e chaves de API. Uma vez entendido este padrão, todo o resto se baseia nele.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) e pergunte:
> - "Como eu mudaria do GitHub Models para Azure OpenAI neste código?"
> - "Quais outros parâmetros posso configurar no OpenAiOfficialChatModel.builder()?"
> - "Como adiciono respostas em streaming ao invés de esperar pela resposta completa?"

**Engenharia de Prompt** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Agora que você sabe como falar com um modelo, vamos explorar o que você diz a ele. Este demo usa a mesma configuração de modelo, mas mostra cinco padrões diferentes de prompting. Experimente prompts zero-shot para instruções diretas, few-shot que aprendem com exemplos, chain-of-thought que revelam passos de raciocínio, e prompts baseados em papéis que definem contexto. Você verá como o mesmo modelo gera resultados dramaticamente diferentes dependendo de como você formula sua solicitação.

O demo também demonstra templates de prompt, que são uma forma poderosa de criar prompts reutilizáveis com variáveis.
O exemplo abaixo mostra um prompt usando `PromptTemplate` do LangChain4j para preencher variáveis. A IA responderá com base no destino e na atividade fornecidos.

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
> - "Qual a diferença entre prompting zero-shot e few-shot, e quando devo usar cada um?"
> - "Como o parâmetro de temperatura afeta as respostas do modelo?"
> - "Quais técnicas existem para evitar ataques de injeção de prompt em produção?"
> - "Como posso criar objetos PromptTemplate reutilizáveis para padrões comuns?"

**Integração de Ferramentas** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Aqui o LangChain4j fica poderoso. Você usará `AiServices` para criar um assistente de IA que pode chamar seus métodos Java. Basta anotar métodos com `@Tool("descrição")` e o LangChain4j cuida do resto - a IA decide automaticamente quando usar cada ferramenta baseada no que o usuário pede. Isso demonstra chamada de função, uma técnica chave para construir IA que pode agir, não só responder perguntas.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) e pergunte:
> - "Como funciona a anotação @Tool e o que o LangChain4j faz com ela nos bastidores?"
> - "A IA pode chamar várias ferramentas em sequência para resolver problemas complexos?"
> - "O que acontece se uma ferramenta lançar uma exceção - como devo tratar erros?"
> - "Como eu integraria uma API real ao invés deste exemplo da calculadora?"

**Perguntas e Respostas em Documentos (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Aqui você verá a base do RAG (geração aumentada por recuperação). Ao invés de confiar somente no treinamento do modelo, você carrega conteúdo do [`document.txt`](../../../00-quick-start/document.txt) e inclui no prompt. A IA responde com base no seu documento, não no conhecimento geral dela. Este é o primeiro passo para construir sistemas que funcionam com seus próprios dados.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Nota:** Esta abordagem simples carrega o documento inteiro no prompt. Para arquivos grandes (>10KB), você ultrapassará os limites de contexto. O módulo 03 cobre chunking e busca vetorial para sistemas RAG em produção.

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) e pergunte:
> - "Como o RAG previne alucinações da IA comparado a usar o conjunto de treinamento do modelo?"
> - "Qual a diferença entre essa abordagem simples e usar embeddings vetoriais para recuperação?"
> - "Como eu escalaria isso para múltiplos documentos ou bases de conhecimento maiores?"
> - "Quais são as melhores práticas para estruturar o prompt para garantir que a IA use só o contexto fornecido?"

**IA Responsável** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Construa segurança em IA com defesa em profundidade. Este demo mostra duas camadas de proteção trabalhando juntas:

**Parte 1: LangChain4j Input Guardrails** - Bloqueia prompts perigosos antes de chegarem ao LLM. Crie guardrails personalizados que chequem por palavras-chave ou padrões proibidos. Eles rodam no seu código, então são rápidos e gratuitos.

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

**Parte 2: Filtros de Segurança do Provedor** - O GitHub Models possui filtros embutidos que capturam o que seus guardrails podem não pegar. Você verá bloqueios duros (erros HTTP 400) para violações severas e recusas suaves onde a IA recusa educadamente.

> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) e pergunte:
> - "O que é InputGuardrail e como crio o meu próprio?"
> - "Qual a diferença entre bloqueio duro e recusa suave?"
> - "Por que usar guardrails e filtros do provedor juntos?"

## Próximos Passos

**Próximo Módulo:** [01-introduction - Começando com LangChain4j e gpt-5 no Azure](../01-introduction/README.md)

---

**Navegação:** [← Voltar ao Início](../README.md) | [Próximo: Módulo 01 - Introdução →](../01-introduction/README.md)

---

## Resolução de Problemas

### Primeira Compilação Maven

**Problema**: O comando inicial `mvn clean compile` ou `mvn package` demora muito (10-15 minutos)

**Causa**: O Maven precisa baixar todas as dependências do projeto (Spring Boot, bibliotecas LangChain4j, SDKs Azure, etc.) na primeira compilação.

**Solução**: Este é um comportamento normal. As compilações subsequentes serão muito mais rápidas pois as dependências ficam em cache localmente. O tempo de download depende da velocidade da sua rede.
### Sintaxe do Comando Maven no PowerShell

**Problema**: Comandos Maven falham com o erro `Unknown lifecycle phase ".mainClass=..."`

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

O operador `--%` instrui o PowerShell a passar todos os argumentos restantes literalmente para o Maven, sem interpretação.

### Exibição de Emojis no Windows PowerShell

**Problema**: Respostas da IA mostram caracteres ilegíveis (ex.: `????` ou `â??`) em vez de emojis no PowerShell

**Causa**: A codificação padrão do PowerShell não suporta emojis UTF-8

**Solução**: Execute este comando antes de rodar aplicações Java:
```cmd
chcp 65001
```

Isso força a codificação UTF-8 no terminal. Alternativamente, use o Windows Terminal, que possui suporte melhor ao Unicode.

### Depuração de Chamadas à API

**Problema**: Erros de autenticação, limites de taxa ou respostas inesperadas do modelo de IA

**Solução**: Os exemplos incluem `.logRequests(true)` e `.logResponses(true)` para mostrar as chamadas de API no console. Isso ajuda a solucionar erros de autenticação, limites de taxa ou respostas inesperadas. Remova essas flags em produção para reduzir ruído nos logs.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, por favor, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->