# Módulo 03: RAG (Geração Aumentada por Recuperação)

## Índice

- [O que Vai Aprender](../../../03-rag)
- [Compreender o RAG](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Como Funciona](../../../03-rag)
  - [Processamento de Documentos](../../../03-rag)
  - [Criação de Embeddings](../../../03-rag)
  - [Pesquisa Semântica](../../../03-rag)
  - [Geração de Respostas](../../../03-rag)
- [Executar a Aplicação](../../../03-rag)
- [Utilizar a Aplicação](../../../03-rag)
  - [Carregar um Documento](../../../03-rag)
  - [Fazer Perguntas](../../../03-rag)
  - [Verificar Referências de Fonte](../../../03-rag)
  - [Experimentar com Perguntas](../../../03-rag)
- [Conceitos-Chave](../../../03-rag)
  - [Estratégia de Divisão em Partes](../../../03-rag)
  - [Pontuações de Similaridade](../../../03-rag)
  - [Armazenamento em Memória](../../../03-rag)
  - [Gestão da Janela de Contexto](../../../03-rag)
- [Quando o RAG é Importante](../../../03-rag)
- [Próximos Passos](../../../03-rag)

## O que Vai Aprender

Nos módulos anteriores, aprendeu a manter conversas com IA e a estruturar os seus prompts de forma eficaz. Mas existe uma limitação fundamental: os modelos de linguagem só sabem o que aprenderam durante o treino. Não conseguem responder a perguntas sobre as políticas da sua empresa, a documentação dos seus projetos ou qualquer informação que não tenha sido incluída no treino.

O RAG (Geração Aumentada por Recuperação) resolve este problema. Em vez de tentar ensinar o modelo com a sua informação (o que é dispendioso e impraticável), dá-lhe a capacidade de pesquisar nos seus documentos. Quando alguém faz uma pergunta, o sistema encontra a informação relevante e inclui-a no prompt. O modelo responde então com base nesse contexto recuperado.

Pense no RAG como se desse ao modelo uma biblioteca de referência. Quando faz uma pergunta, o sistema:

1. **Consulta do Utilizador** – Você faz uma pergunta  
2. **Embedding** – Converte a sua pergunta num vetor  
3. **Pesquisa Vetorial** – Encontra partes do documento semelhantes  
4. **Montagem do Contexto** – Adiciona as partes relevantes ao prompt  
5. **Resposta** – O LLM gera uma resposta com base no contexto  

Isto fundamenta as respostas do modelo nos seus dados reais em vez de confiar no seu conhecimento do treino ou inventar respostas.

## Compreender o RAG

O diagrama abaixo ilustra o conceito central: em vez de depender apenas dos dados de treino do modelo, o RAG dá-lhe uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-PT/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

Aqui está como as partes se ligam de ponta a ponta. A pergunta do utilizador passa por quatro etapas — embedding, pesquisa vetorial, montagem do contexto e geração de resposta — cada uma baseada na anterior:

<img src="../../../translated_images/pt-PT/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

O resto deste módulo percorre cada etapa em detalhe, com código que pode executar e modificar.

## Pré-requisitos

- Módulo 01 concluído (recursos Azure OpenAI implantados)  
- Ficheiro `.env` na raiz do diretório com as credenciais Azure (criado pelo comando `azd up` no Módulo 01)  

> **Nota:** Se ainda não concluiu o Módulo 01, siga as instruções de implantação aí primeiro.

## Como Funciona

### Processamento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando carrega um documento, o sistema analisa-o (PDF ou texto simples), anexa metadados como o nome do ficheiro e depois divide-o em pedaços — partes menores que cabem confortavelmente na janela de contexto do modelo. Estes pedaços sobrepõem-se ligeiramente para não perder contexto nas fronteiras.

```java
// Analise o ficheiro carregado e encapsule-o num Documento LangChain4j
Document document = Document.from(content, metadata);

// Divida em blocos de 300 tokens com sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
O diagrama abaixo mostra visualmente como isto funciona. Repare como cada pedaço partilha alguns tokens com os seus vizinhos — a sobreposição de 30 tokens garante que nenhum contexto importante se perde entre os espaços:

<img src="../../../translated_images/pt-PT/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:  
> - "Como é que o LangChain4j divide documentos em pedaços e por que é que a sobreposição é importante?"  
> - "Qual é o tamanho ótimo dos pedaços para diferentes tipos de documentos e porquê?"  
> - "Como posso lidar com documentos em várias línguas ou com formatações especiais?"

### Criação de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada pedaço é convertido numa representação numérica chamada embedding – essencialmente uma impressão digital matemática que captura o significado do texto. Textos semelhantes produzem embeddings semelhantes.

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
  
O diagrama de classes abaixo mostra como estes componentes LangChain4j se ligam. `OpenAiOfficialEmbeddingModel` converte texto em vetores, `InMemoryEmbeddingStore` guarda os vetores juntamente com os dados originais `TextSegment`, e `EmbeddingSearchRequest` controla parâmetros de recuperação como `maxResults` e `minScore`:

<img src="../../../translated_images/pt-PT/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

Uma vez guardados, os embeddings de conteúdos semelhantes agrupam-se naturalmente no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados acabam próximos, o que torna possível a pesquisa semântica:

<img src="../../../translated_images/pt-PT/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Pesquisa Semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando faz uma pergunta, a sua própria pergunta é também transformada num embedding. O sistema compara o embedding da sua pergunta com todos os embeddings dos pedaços do documento. Ele encontra os pedaços com significados mais semelhantes — não apenas correspondência de palavras-chave, mas uma real similaridade semântica.

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
  
O diagrama abaixo contrasta a pesquisa semântica com a pesquisa tradicional por palavras-chave. Uma pesquisa por palavra-chave para "veículo" não encontra um pedaço sobre "carros e camiões", mas a pesquisa semântica entende que significam o mesmo e devolve-o como uma correspondência altamente pontuada:

<img src="../../../translated_images/pt-PT/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:  
> - "Como funciona a pesquisa de similaridade com embeddings e o que determina a pontuação?"  
> - "Qual o limiar de similaridade que devo usar e como isto afeta os resultados?"  
> - "Como lidar com casos em que não são encontrados documentos relevantes?"

### Geração de Respostas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os pedaços mais relevantes são reunidos num prompt estruturado que inclui instruções explícitas, o contexto recuperado e a pergunta do utilizador. O modelo lê esses pedaços específicos e responde com base nessa informação — só pode usar o que está à sua frente, o que previne a "alucinação".

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
  
O diagrama abaixo mostra esta montagem em ação — os pedaços com maior pontuação da pesquisa são inseridos no template do prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-PT/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## Executar a Aplicação

**Verifique a implantação:**

Certifique-se de que o ficheiro `.env` existe na raiz do diretório com as credenciais Azure (criado durante o Módulo 01):  
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Inicie a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está a correr na porta 8081. Pode ignorar os comandos de arranque abaixo e ir diretamente para http://localhost:8081.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades no lado esquerdo do VS Code (procure o ícone Spring Boot).

Pelo Spring Boot Dashboard, pode:  
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho  
- Iniciar/parar aplicações com um único clique  
- Ver os logs da aplicação em tempo real  
- Monitorizar o estado da aplicação  

Basta clicar no botão de play junto a "rag" para arrancar este módulo, ou iniciar todos os módulos de uma só vez.

<img src="../../../translated_images/pt-PT/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

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
  
Ou inicie só este módulo:

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
  
Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz e vão compilar os JARs se não existirem.

> **Nota:** Se preferir compilar todos os módulos manualmente antes de começar:  
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


## Utilizar a Aplicação

A aplicação fornece uma interface web para carregar documentos e fazer perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-PT/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Interface da aplicação RAG — carregue documentos e faça perguntas*

### Carregar um Documento

Comece por carregar um documento – ficheiros TXT são os melhores para testar. Está disponível nesta diretoria um `sample-document.txt` que contém informação sobre funcionalidades do LangChain4j, implementação do RAG e boas práticas — perfeito para testar o sistema.

O sistema processa o seu documento, divide-o em pedaços e cria embeddings para cada pedaço. Isto acontece automaticamente quando faz o upload.

### Fazer Perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Tente algo factual que esteja claramente indicado no documento. O sistema procura os pedaços relevantes, inclui-os no prompt e gera uma resposta.

### Verificar Referências de Fonte

Repare que cada resposta inclui referências de fonte com pontuações de similaridade. Estas pontuações (de 0 a 1) mostram quão relevante cada pedaço foi para a sua pergunta. Pontuações mais altas indicam correspondências melhores. Isto permite-lhe verificar a resposta contra o material fonte.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-PT/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Resultados da consulta mostrando resposta com referências de fonte e pontuações de relevância*

### Experimentar com Perguntas

Experimente diferentes tipos de perguntas:  
- Factos específicos: "Qual é o tema principal?"  
- Comparações: "Qual é a diferença entre X e Y?"  
- Resumos: "Resuma os pontos-chave sobre Z"  

Observe como as pontuações de relevância mudam conforme a sua pergunta corresponde ao conteúdo do documento.

## Conceitos-Chave

### Estratégia de Divisão em Partes

Os documentos são divididos em pedaços de 300 tokens com 30 tokens de sobreposição. Este equilíbrio garante que cada pedaço tem contexto suficiente para ser significativo enquanto permanece suficientemente pequeno para incluir vários pedaços num prompt.

### Pontuações de Similaridade

Cada pedaço recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica a proximidade da correspondência com a pergunta do utilizador. O diagrama abaixo visualiza os intervalos de pontuação e como o sistema os usa para filtrar resultados:

<img src="../../../translated_images/pt-PT/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

As pontuações variam de 0 a 1:  
- 0,7-1,0: Altamente relevante, correspondência exata  
- 0,5-0,7: Relevante, bom contexto  
- Abaixo de 0,5: Filtrado, demasiado diferente  

O sistema só recupera peças acima do limiar mínimo para garantir qualidade.

### Armazenamento em Memória

Este módulo usa armazenamento em memória para simplicidade. Quando reinicia a aplicação, os documentos carregados são perdidos. Sistemas de produção usam bases de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gestão da Janela de Contexto

Cada modelo tem uma janela de contexto máxima. Não pode incluir todos os pedaços de um documento grande. O sistema recupera os N pedaços mais relevantes (por omissão 5) para manter-se dentro dos limites e fornecer contexto suficiente para respostas precisas.

## Quando o RAG é Importante

O RAG nem sempre é a abordagem certa. O guia de decisão abaixo ajuda-o a determinar quando o RAG acrescenta valor versus quando abordagens mais simples — como incluir conteúdo diretamente no prompt ou confiar no conhecimento embutido do modelo — são suficientes:

<img src="../../../translated_images/pt-PT/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Use RAG quando:**
- Responder a perguntas sobre documentos proprietários  
- A informação muda frequentemente (políticas, preços, especificações)  
- A precisão requer atribuição da fonte  
- O conteúdo é demasiado grande para caber num único prompt  
- Precisa de respostas verificáveis e fundamentadas  

**Não use RAG quando:**  
- As perguntas requerem conhecimentos gerais que o modelo já possui  
- São necessários dados em tempo real (RAG funciona com documentos carregados)  
- O conteúdo é suficientemente pequeno para incluir diretamente nos prompts  

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompts](../02-prompt-engineering/README.md) | [Voltar ao Início](../README.md) | [Seguinte: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos empenhemos em garantir a precisão, tenha em atenção que as traduções automáticas podem conter erros ou imprecisões. O documento original no seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->