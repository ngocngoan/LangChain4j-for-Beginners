# Módulo 03: RAG (Geração Aprimorada por Recuperação)

## Sumário

- [O que você vai aprender](../../../03-rag)
- [Entendendo o RAG](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Como funciona](../../../03-rag)
  - [Processamento de documentos](../../../03-rag)
  - [Criação de embeddings](../../../03-rag)
  - [Busca semântica](../../../03-rag)
  - [Geração de respostas](../../../03-rag)
- [Executando a aplicação](../../../03-rag)
- [Usando a aplicação](../../../03-rag)
  - [Enviar um documento](../../../03-rag)
  - [Fazer perguntas](../../../03-rag)
  - [Verificar referências das fontes](../../../03-rag)
  - [Experimentar perguntas](../../../03-rag)
- [Conceitos chave](../../../03-rag)
  - [Estratégia de segmentação](../../../03-rag)
  - [Pontuações de similaridade](../../../03-rag)
  - [Armazenamento em memória](../../../03-rag)
  - [Gerenciamento da janela de contexto](../../../03-rag)
- [Quando o RAG importa](../../../03-rag)
- [Próximos passos](../../../03-rag)

## O que você vai aprender

Nos módulos anteriores, você aprendeu a conversar com IA e estruturar seus prompts de forma eficaz. Mas há uma limitação fundamental: os modelos de linguagem só sabem o que aprenderam durante o treinamento. Eles não conseguem responder perguntas sobre as políticas da sua empresa, a documentação do seu projeto ou qualquer informação que não tenham sido treinados para conhecer.

O RAG (Geração Aprimorada por Recuperação) resolve esse problema. Em vez de tentar ensinar o modelo com suas informações (o que é caro e impraticável), você dá a ele a capacidade de buscar em seus documentos. Quando alguém faz uma pergunta, o sistema encontra a informação relevante e a inclui no prompt. O modelo então responde baseado nesse contexto recuperado.

Pense no RAG como dar ao modelo uma biblioteca de referência. Quando você faz uma pergunta, o sistema:

1. **Consulta do usuário** - Você faz uma pergunta  
2. **Embedding** - Converte sua pergunta em um vetor  
3. **Busca vetorial** - Encontra pedaços de documentos semelhantes  
4. **Montagem do contexto** - Adiciona trechos relevantes ao prompt  
5. **Resposta** - O LLM gera uma resposta baseada no contexto  

Isso fundamenta as respostas do modelo nos seus dados reais, em vez de confiar apenas no conhecimento aprendido ou inventar respostas.

## Entendendo o RAG

O diagrama abaixo ilustra o conceito central: em vez de depender apenas dos dados de treinamento do modelo, o RAG fornece a ele uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-BR/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Veja como as partes se conectam de ponta a ponta. A pergunta do usuário passa por quatro etapas — embedding, busca vetorial, montagem do contexto e geração da resposta — cada uma construindo a partir da anterior:

<img src="../../../translated_images/pt-BR/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

O restante deste módulo explica cada etapa em detalhes, com códigos que você pode executar e modificar.

## Pré-requisitos

- Ter completado o Módulo 01 (recursos Azure OpenAI implantados)  
- Arquivo `.env` no diretório raiz com as credenciais do Azure (criado pelo `azd up` no Módulo 01)  

> **Nota:** Se você ainda não completou o Módulo 01, siga as instruções de implantação primeiro.

## Como funciona

### Processamento de documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando você envia um documento, o sistema o analisa (PDF ou texto simples), anexa metadados como o nome do arquivo e o divide em pedaços — partes menores que cabem confortavelmente na janela de contexto do modelo. Esses pedaços se sobrepõem ligeiramente para que você não perca contexto nas bordas.

```java
// Analise o arquivo enviado e o envolva em um Documento LangChain4j
Document document = Document.from(content, metadata);

// Divida em blocos de 300 tokens com sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
O diagrama abaixo mostra como isso funciona visualmente. Note como cada pedaço compartilha alguns tokens com os vizinhos — a sobreposição de 30 tokens garante que nenhum contexto importante fique perdido entre eles:

<img src="../../../translated_images/pt-BR/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:  
> - "Como o LangChain4j divide documentos em pedaços e por que a sobreposição é importante?"  
> - "Qual o tamanho ideal dos pedaços para diferentes tipos de documentos e por quê?"  
> - "Como lidar com documentos em múltiplos idiomas ou com formatações especiais?"

### Criação de embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada pedaço é convertido em uma representação numérica chamada embedding — essencialmente uma impressão digital matemática que captura o significado do texto. Textos semelhantes produzem embeddings semelhantes.

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
O diagrama de classes abaixo mostra como esses componentes do LangChain4j se conectam. `OpenAiOfficialEmbeddingModel` converte texto em vetores, `InMemoryEmbeddingStore` guarda os vetores junto com os dados originais de `TextSegment`, e `EmbeddingSearchRequest` controla parâmetros de recuperação como `maxResults` e `minScore`:

<img src="../../../translated_images/pt-BR/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Depois que os embeddings são armazenados, conteúdos similares naturalmente se agrupam no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados acabam próximos, o que possibilita a busca semântica:

<img src="../../../translated_images/pt-BR/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Busca semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando você faz uma pergunta, sua pergunta também vira um embedding. O sistema compara o embedding da sua pergunta com todos os embeddings dos pedaços de documentos. Ele encontra os pedaços com os significados mais semelhantes — não só correspondência de palavras-chave, mas similitude semântica real.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
O diagrama abaixo contrasta a busca semântica com a tradicional busca por palavras-chave. Uma busca por palavra-chave em "veículo" perde um pedaço sobre "carros e caminhões," mas a busca semântica entende que significam a mesma coisa e o retorna como resultado relevante:

<img src="../../../translated_images/pt-BR/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:  
> - "Como funciona a busca por similaridade com embeddings e o que determina a pontuação?"  
> - "Qual limiar de similaridade devo usar e como isso afeta os resultados?"  
> - "Como lidar com casos em que não são encontrados documentos relevantes?"

### Geração de respostas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os pedaços mais relevantes são montados em um prompt estruturado que inclui instruções explícitas, o contexto recuperado e a pergunta do usuário. O modelo lê esses pedaços específicos e responde baseado nessas informações — ele só pode usar o que está à sua frente, o que evita alucinações.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```
  
O diagrama abaixo mostra essa montagem em ação — os pedaços com as maiores pontuações da busca são inseridos no template do prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-BR/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Executando a aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` existe no diretório raiz com as credenciais do Azure (criado durante o Módulo 01):  
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8081. Você pode pular os comandos de inicialização abaixo e ir direto para http://localhost:8081.  

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários de VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades do lado esquerdo do VS Code (procure pelo ícone do Spring Boot).

No Spring Boot Dashboard, você pode:  
- Ver todas as aplicações Spring Boot disponíveis no workspace  
- Iniciar/parar aplicações com um clique  
- Visualizar logs das aplicações em tempo real  
- Monitorar o estado das aplicações  

Basta clicar no botão de play ao lado de "rag" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Opção 2: Usando scripts shell**

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
cd 03-rag
./start.sh
```
  
**PowerShell:**  
```powershell
cd 03-rag
.\start.ps1
```
  
Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e irão compilar os JARs caso eles não existam.

> **Nota:** Se preferir compilar todos os módulos manualmente antes de iniciar:  
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
  
Abra http://localhost:8081 no seu navegador.

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
  
## Usando a aplicação

A aplicação fornece uma interface web para envio de documentos e realização de perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-BR/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface da aplicação RAG - envie documentos e faça perguntas*

### Enviar um documento

Comece enviando um documento - arquivos TXT funcionam melhor para testes. Um `sample-document.txt` é fornecido neste diretório que contém informações sobre recursos do LangChain4j, implementação do RAG e boas práticas - perfeito para testar o sistema.

O sistema processa seu documento, o divide em pedaços e cria embeddings para cada pedaço. Isso acontece automaticamente ao enviar.

### Fazer perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Tente algo factual que esteja claramente declarado no documento. O sistema busca pedaços relevantes, inclui-os no prompt e gera uma resposta.

### Verificar referências das fontes

Perceba que cada resposta inclui referências de fonte com pontuações de similaridade. Essas pontuações (de 0 a 1) indicam quão relevante cada pedaço foi para sua pergunta. Pontuações mais altas significam melhores correspondências. Isso permite que você verifique a resposta com o material original.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-BR/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Resultados da consulta mostrando resposta com referências de fonte e pontuações de relevância*

### Experimentar perguntas

Experimente diferentes tipos de perguntas:  
- Fatos específicos: "Qual é o tema principal?"  
- Comparações: "Qual a diferença entre X e Y?"  
- Resumos: "Resuma os pontos principais sobre Z"  

Observe como a pontuação de relevância muda conforme sua pergunta corresponde ou não ao conteúdo do documento.

## Conceitos chave

### Estratégia de segmentação

Os documentos são divididos em pedaços de 300 tokens com 30 tokens de sobreposição. Esse equilíbrio garante que cada pedaço tenha contexto suficiente para ser significativo, ao mesmo tempo que seja pequeno para caber múltiplos pedaços num prompt.

### Pontuações de similaridade

Cada pedaço recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica o quão próximo ele está da pergunta do usuário. O diagrama abaixo visualiza os intervalos das pontuações e como o sistema os usa para filtrar resultados:

<img src="../../../translated_images/pt-BR/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

As pontuações variam entre 0 e 1:  
- 0.7-1.0: Altamente relevante, correspondência exata  
- 0.5-0.7: Relevante, bom contexto  
- Abaixo de 0.5: Filtrado, muito diferente  

O sistema recupera apenas pedaços acima do limiar mínimo para garantir qualidade.

### Armazenamento em memória

Este módulo usa armazenamento em memória por simplicidade. Quando você reiniciar a aplicação, os documentos enviados serão perdidos. Sistemas em produção usam bancos de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gerenciamento da janela de contexto

Cada modelo tem uma janela máxima de contexto. Você não pode incluir todos os pedaços de um documento grande. O sistema recupera os N pedaços mais relevantes (padrão 5) para ficar dentro dos limites e ainda assim prover contexto suficiente para respostas precisas.

## Quando o RAG importa

O RAG nem sempre é a abordagem ideal. O guia de decisão abaixo ajuda a determinar quando o RAG agrega valor, versus quando abordagens mais simples — como incluir conteúdo diretamente no prompt ou confiar no conhecimento embutido do modelo — já são suficientes:

<img src="../../../translated_images/pt-BR/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Use RAG quando:**
- Respondendo perguntas sobre documentos proprietários
- Informações mudam com frequência (políticas, preços, especificações)
- A precisão requer atribuição da fonte
- O conteúdo é muito grande para caber em um único prompt
- Você precisa de respostas verificáveis e fundamentadas

**Não use RAG quando:**
- As perguntas exigem conhecimento geral que o modelo já possui
- Dados em tempo real são necessários (RAG funciona com documentos carregados)
- O conteúdo é pequeno o suficiente para incluir diretamente nos prompts

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompt](../02-prompt-engineering/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->