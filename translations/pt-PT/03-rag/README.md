# Módulo 03: RAG (Geração Aumentada por Recuperação)

## Índice

- [Video Walkthrough](../../../03-rag)
- [O Que Vai Aprender](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Compreender o RAG](../../../03-rag)
  - [Qual Abordagem RAG Este Tutorial Usa?](../../../03-rag)
- [Como Funciona](../../../03-rag)
  - [Processamento de Documentos](../../../03-rag)
  - [Criação de Embeddings](../../../03-rag)
  - [Pesquisa Semântica](../../../03-rag)
  - [Geração de Resposta](../../../03-rag)
- [Executar a Aplicação](../../../03-rag)
- [Usar a Aplicação](../../../03-rag)
  - [Carregar um Documento](../../../03-rag)
  - [Fazer Perguntas](../../../03-rag)
  - [Ver Referências das Fontes](../../../03-rag)
  - [Experimentar com Perguntas](../../../03-rag)
- [Conceitos-Chave](../../../03-rag)
  - [Estratégia de Divisão em Partes](../../../03-rag)
  - [Pontuações de Similaridade](../../../03-rag)
  - [Armazenamento em Memória](../../../03-rag)
  - [Gestão da Janela de Contexto](../../../03-rag)
- [Quando o RAG Importa](../../../03-rag)
- [Próximos Passos](../../../03-rag)

## Video Walkthrough

Assista a esta sessão ao vivo que explica como começar com este módulo: [RAG com LangChain4j - Sessão ao Vivo](https://www.youtube.com/watch?v=_olq75ZH_eY)

## O Que Vai Aprender

Nos módulos anteriores, aprendeu como ter conversas com IA e estruturar os seus prompts de forma eficaz. Mas há uma limitação fundamental: os modelos de linguagem só sabem o que aprenderam durante o treino. Eles não conseguem responder a perguntas sobre as políticas da sua empresa, a documentação do seu projeto, ou qualquer informação que não tenham sido treinados para conhecer.

O RAG (Geração Aumentada por Recuperação) resolve este problema. Em vez de tentar ensinar o modelo com a sua informação (o que é caro e impraticável), você dá-lhe a capacidade de pesquisar nos seus documentos. Quando alguém faz uma pergunta, o sistema encontra informações relevantes e inclui-as no prompt. O modelo responde então com base nesse contexto recuperado.

Pense no RAG como dar ao modelo uma biblioteca de referência. Quando faz uma pergunta, o sistema:

1. **Consulta do Utilizador** - Você faz uma pergunta  
2. **Embedding** - Converte a sua pergunta num vetor  
3. **Pesquisa Vetorial** - Encontra partes do documento semelhantes  
4. **Montagem do Contexto** - Adiciona partes relevantes ao prompt  
5. **Resposta** - O LLM gera uma resposta com base no contexto  

Isto ancora as respostas do modelo nos seus dados reais em vez de depender do seu conhecimento de treino ou inventar respostas.

## Pré-requisitos

- Ter concluído o [Módulo 00 - Início Rápido](../00-quick-start/README.md) (para o exemplo Easy RAG referido acima)  
- Ter concluído o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implementados, incluindo o modelo de embedding `text-embedding-3-small`)  
- Ficheiro `.env` no diretório raiz com as credenciais Azure (criado pelo `azd up` no Módulo 01)  

> **Nota:** Se ainda não concluiu o Módulo 01, siga primeiro as instruções de implementação aí presentes. O comando `azd up` implementa tanto o modelo de chat GPT como o modelo de embedding usado neste módulo.

## Compreender o RAG

O diagrama abaixo ilustra o conceito principal: em vez de depender apenas dos dados de treino do modelo, o RAG dá-lhe uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-PT/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Este diagrama mostra a diferença entre um LLM padrão (que adivinha a partir dos dados de treino) e um LLM com RAG (que primeiro consulta os seus documentos).*

Aqui está como as peças se ligam de ponta a ponta. A pergunta de um utilizador passa por quatro etapas — embedding, pesquisa vetorial, montagem do contexto e geração de resposta — cada uma construída sobre a anterior:

<img src="../../../translated_images/pt-PT/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Este diagrama mostra o pipeline RAG de ponta a ponta — uma consulta de utilizador passa por embedding, pesquisa vetorial, montagem do contexto e geração da resposta.*

O resto deste módulo percorre cada etapa em detalhe, com código que pode executar e modificar.

### Qual Abordagem RAG Este Tutorial Usa?

O LangChain4j oferece três formas de implementar RAG, cada uma com um diferente nível de abstração. O diagrama abaixo compara-as lado a lado:

<img src="../../../translated_images/pt-PT/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Este diagrama compara as três abordagens RAG do LangChain4j — Easy, Native e Advanced — mostrando os seus principais componentes e quando usar cada uma.*

| Abordagem | O Que Faz | Compromisso |
|---|---|---|
| **Easy RAG** | Liga tudo automaticamente através de `AiServices` e `ContentRetriever`. Anota uma interface, associa um retriever, e o LangChain4j trata dos embeddings, pesquisa e montagem do prompt nos bastidores. | Código mínimo, mas não vê o que acontece em cada etapa. |
| **Native RAG** | Chama o modelo de embedding, pesquisa no armazenamento, constrói o prompt e gera a resposta você mesmo — passo a passo explícito. | Mais código, mas cada etapa é visível e modificável. |
| **Advanced RAG** | Usa a framework `RetrievalAugmentor` com transformadores de consulta, routers, re-rankers e injetores de conteúdo plugáveis para pipelines de produção. | Máxima flexibilidade, mas com complexidade significativa. |

**Este tutorial usa a abordagem Native.** Cada etapa do pipeline RAG — embedding da consulta, pesquisa no armazenamento vetorial, montagem do contexto e geração da resposta — está escrita explicitamente em [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Isto é intencional: como recurso de aprendizagem, é mais importante que veja e compreenda cada etapa do que o código ser minimizado. Quando estiver confortável com a forma como as peças se ligam, pode avançar para Easy RAG para protótipos rápidos ou Advanced RAG para sistemas de produção.

> **💡 Já viu o Easy RAG em ação?** O [módulo Início Rápido](../00-quick-start/README.md) inclui um exemplo de Perguntas & Respostas com Documento ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa a abordagem Easy RAG — o LangChain4j trata automaticamente dos embeddings, pesquisa e montagem do prompt. Este módulo dá o passo seguinte ao abrir esse pipeline para que possa ver e controlar cada etapa.

<img src="../../../translated_images/pt-PT/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Este diagrama mostra o pipeline Easy RAG de `SimpleReaderDemo.java`. Compare com a abordagem Native usada neste módulo: Easy RAG oculta o embedding, recuperação e montagem do prompt atrás de `AiServices` e `ContentRetriever` — carrega um documento, associa um retriever e obtém respostas. A abordagem Native deste módulo abre esse pipeline para que chame cada etapa (embedding, pesquisa, montagem do contexto, geração) você mesmo, dando visibilidade e controlo total.*

## Como Funciona

O pipeline RAG neste módulo divide-se em quatro etapas que correm em sequência cada vez que um utilizador faz uma pergunta. Primeiro, um documento carregado é **analisado e dividido em partes** geríveis. Essas partes são então convertidas em **embeddings vetoriais** e armazenadas para comparação matemática. Quando chega uma consulta, o sistema executa uma **pesquisa semântica** para encontrar as partes mais relevantes, e finalmente passa-as como contexto ao LLM para a **geração da resposta**. As secções abaixo explicam cada etapa com o código real e diagramas. Vamos ver a primeira etapa.

### Processamento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando carrega um documento, o sistema analisa-o (PDF ou texto simples), associa metadados como o nome do ficheiro e depois divide-o em partes — pedaços mais pequenos que cabem confortavelmente na janela de contexto do modelo. Essas partes sobrepõem-se ligeiramente para que não perca contexto nas fronteiras.

```java
// Analise o ficheiro carregado e envolva-o num Documento LangChain4j
Document document = Document.from(content, metadata);

// Divida em blocos de 300 tokens com sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

O diagrama abaixo mostra como isto funciona visualmente. Repare como cada parte partilha alguns tokens com as suas vizinhas — a sobreposição de 30 tokens assegura que nenhum contexto importante fica perdido:

<img src="../../../translated_images/pt-PT/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Este diagrama mostra um documento a ser dividido em partes de 300 tokens com sobreposição de 30 tokens, preservando o contexto nas fronteiras das partes.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:  
> - "Como é que o LangChain4j divide documentos em partes e por que é importante a sobreposição?"  
> - "Qual é o tamanho ideal das partes para diferentes tipos de documento e porquê?"  
> - "Como lidar com documentos em múltiplas línguas ou com formatação especial?"

### Criação de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada parte é convertida numa representação numérica chamada embedding — essencialmente um conversor de significado para números. O modelo de embedding não é “inteligente” como um modelo de chat; não consegue seguir instruções, raciocinar ou responder a perguntas. O que pode fazer é mapear texto num espaço matemático onde significados semelhantes ficam próximos — “carro” perto de “automóvel”, “política de reembolso” perto de “devolver meu dinheiro”. Pense num modelo de chat como uma pessoa com quem pode falar; um modelo de embedding é um sistema ultra eficiente de arquivamento.

<img src="../../../translated_images/pt-PT/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Este diagrama mostra como um modelo de embedding converte texto em vetores numéricos, colocando significados semelhantes — como "carro" e "automóvel" — perto uns dos outros no espaço vetorial.*

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

O diagrama de classes abaixo mostra os dois fluxos separados num pipeline RAG e as classes do LangChain4j que os implementam. O **fluxo de ingestão** (corre uma vez no momento do upload) divide o documento, gera embeddings das partes e armazena-os via `.addAll()`. O **fluxo de consulta** (corre sempre que um utilizador pergunta) gera embedding da pergunta, pesquisa no armazenamento via `.search()`, e passa o contexto encontrado ao modelo de chat. Ambos os fluxos convergem na interface partilhada `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/pt-PT/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Este diagrama mostra os dois fluxos do pipeline RAG — ingestão e consulta — e como se ligam através de um EmbeddingStore partilhado.*

Uma vez que os embeddings estão armazenados, conteúdos semelhantes agrupam-se naturalmente no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados ficam como pontos próximos, o que torna possível fazer pesquisa semântica:

<img src="../../../translated_images/pt-PT/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Esta visualização mostra como documentos relacionados se agrupam no espaço vetorial 3D, com tópicos como Documentação Técnica, Regras de Negócio, e FAQ a formarem grupos distintos.*

Quando um utilizador pesquisa, o sistema segue quatro passos: gerar embedding dos documentos uma vez, gerar embedding da consulta a cada pesquisa, comparar o vetor da consulta com todos os vetores armazenados usando similaridade cosseno, e retornar os top-K pedaços com maior pontuação. O diagrama abaixo explica cada passo e as classes LangChain4j envolvidas:

<img src="../../../translated_images/pt-PT/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Este diagrama mostra o processo de pesquisa em embeddings em quatro passos: embedar documentos, embedar a consulta, comparar vetores com similaridade cosseno, e retornar os top-K resultados.*

### Pesquisa Semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando faz uma pergunta, a sua pergunta também é transformada em embedding. O sistema compara o embedding da sua pergunta com os embeddings de todas as partes do documento. Encontra as partes com significados mais semelhantes — não só palavras-chave coincidentes, mas verdadeira semelhança semântica.

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

O diagrama abaixo contrasta pesquisa semântica com pesquisa tradicional por palavras-chave. Uma pesquisa por palavra-chave por "veículo" perde uma parte sobre "carros e camiões", mas a pesquisa semântica entende que significam o mesmo e retorna-a como uma correspondência de alta pontuação:

<img src="../../../translated_images/pt-PT/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Este diagrama compara pesquisa baseada em palavra-chave com pesquisa semântica, mostrando como a pesquisa semântica recupera conteúdo conceitualmente relacionado mesmo quando as palavras-chave exatas diferem.*

Por baixo dos panos, a similaridade é medida usando similaridade cosseno — basicamente a pergunta "estes dois vetores estão apontados na mesma direção?" Dois pedaços podem usar palavras completamente diferentes, mas se significam a mesma coisa os seus vetores apontam para a mesma direção e a pontuação fica perto de 1.0:

<img src="../../../translated_images/pt-PT/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Este diagrama ilustra similaridade cosseno como o ângulo entre vetores de embeddings — vetores mais alinhados pontuam mais perto de 1.0, indicando maior similaridade semântica.*
> **🤖 Experimente com o Chat [GitHub Copilot](https://github.com/features/copilot):** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:
> - "Como funciona a pesquisa de similaridade com embeddings e o que determina a pontuação?"
> - "Qual o limiar de similaridade que devo usar e como isso afeta os resultados?"
> - "Como lidar com casos em que nenhum documento relevante é encontrado?"

### Geração de Respostas

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os excertos mais relevantes são reunidos num prompt estruturado que inclui instruções explícitas, o contexto recuperado e a pergunta do utilizador. O modelo lê esses excertos específicos e responde com base nessa informação — só pode usar o que está à sua frente, o que evita alucinações.

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

O diagrama abaixo mostra esta montagem em ação — os excertos com melhor pontuação na etapa de pesquisa são inseridos no template do prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-PT/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Este diagrama mostra como os excertos melhor pontuados são reunidos num prompt estruturado, permitindo que o modelo gere uma resposta fundamentada com base nos seus dados.*

## Executar a Aplicação

**Verificar a implementação:**

Certifique-se de que o ficheiro `.env` existe no diretório raiz com as credenciais Azure (criado durante o Módulo 01):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está a funcionar na porta 8081. Pode ignorar os comandos de arranque abaixo e ir diretamente para http://localhost:8081.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O container de desenvolvimento inclui a extensão Spring Boot Dashboard, que oferece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades no lado esquerdo do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um clique
- Ver logs da aplicação em tempo real
- Monitorizar o estado da aplicação

Clique no botão play junto a "rag" para iniciar este módulo, ou inicie todos os módulos em simultâneo.

<img src="../../../translated_images/pt-PT/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta imagem mostra o Spring Boot Dashboard no VS Code, onde pode iniciar, parar e monitorizar aplicações de forma visual.*

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` raiz e irão construir os JARs caso estes não existam.

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

## Usar a Aplicação

A aplicação oferece uma interface web para carregamento de documentos e colocação de perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-PT/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interface da Aplicação RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta imagem mostra a interface da aplicação RAG onde pode carregar documentos e fazer perguntas.*

### Carregar um Documento

Comece por carregar um documento - ficheiros TXT funcionam melhor para testes. É fornecido um `sample-document.txt` neste diretório que contém informações sobre as funcionalidades do LangChain4j, implementação RAG e melhores práticas - perfeito para testar o sistema.

O sistema processa o seu documento, divide-o em excertos e cria embeddings para cada excerto. Isto acontece automaticamente ao carregar.

### Fazer Perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Experimente algo factual que esteja claramente indicado no documento. O sistema procura excertos relevantes, inclui-os no prompt e gera uma resposta.

### Verificar Referências às Fontes

Note que cada resposta inclui referências às fontes com pontuações de similaridade. Estas pontuações (de 0 a 1) mostram quão relevante cada excerto foi para a sua pergunta. Pontuações mais altas significam melhores correspondências. Isto permite verificar a resposta com o material original.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-PT/rag-query-results.6d69fcec5397f355.webp" alt="Resultados da Consulta RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta imagem mostra os resultados da consulta com a resposta gerada, referências às fontes e pontuações de relevância para cada excerto recuperado.*

### Experimente com Perguntas

Experimente diferentes tipos de perguntas:
- Factos específicos: "Qual é o tema principal?"
- Comparações: "Qual a diferença entre X e Y?"
- Resumos: "Resuma os pontos-chave sobre Z"

Observe como as pontuações de relevância mudam consoante a correspondência entre a pergunta e o conteúdo do documento.

## Conceitos Chave

### Estratégia de Fragmentação (Chunking)

Documentos são divididos em excertos de 300 tokens com uma sobreposição de 30 tokens. Este equilíbrio assegura que cada excerto tem contexto suficiente para ser significativo enquanto permanece pequeno o bastante para incluir múltiplos excertos num prompt.

### Pontuações de Similaridade

Cada excerto recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica o quão próximo está da pergunta do utilizador. O diagrama abaixo visualiza os intervalos de pontuação e como o sistema os usa para filtrar resultados:

<img src="../../../translated_images/pt-PT/similarity-scores.b0716aa911abf7f0.webp" alt="Pontuações de Similaridade" width="800"/>

*Este diagrama mostra intervalos de pontuação de 0 a 1, com um limiar mínimo de 0.5 que filtra excertos irrelevantes.*

As pontuações variam entre 0 e 1:
- 0.7-1.0: Altamente relevante, correspondência exata
- 0.5-0.7: Relevante, bom contexto
- Abaixo de 0.5: Filtrado, demasiado dissemelhante

O sistema só recupera excertos acima do limiar mínimo para garantir qualidade.

Embeddings funcionam bem quando o significado clusteriza claramente, mas têm pontos cegos. O diagrama abaixo mostra os modos comuns de falha — excertos grandes demais produzem vetores confusos, excertos pequenos demais carecem de contexto, termos ambíguos apontam para múltiplos clusters, e pesquisas de correspondência exata (IDs, números de peça) não funcionam de todo com embeddings:

<img src="../../../translated_images/pt-PT/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modos de Falha dos Embeddings" width="800"/>

*Este diagrama mostra modos comuns de falha de embeddings: excertos grandes demais, excertos pequenos demais, termos ambíguos que apontam para múltiplos clusters, e pesquisas de correspondência exata como IDs.*

### Armazenamento em Memória

Este módulo usa armazenamento em memória para simplificar. Ao reiniciar a aplicação, os documentos carregados são perdidos. Sistemas de produção usam bases de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gestão da Janela de Contexto

Cada modelo tem uma janela máxima de contexto. Não é possível incluir todos os excertos de um documento grande. O sistema recupera os N excertos mais relevantes (por padrão 5) para manter-se dentro dos limites enquanto fornece contexto suficiente para respostas exatas.

## Quando o RAG é Importante

O RAG nem sempre é a melhor abordagem. O guia de decisão abaixo ajuda a determinar quando o RAG acrescenta valor versus quando abordagens mais simples — como incluir conteúdo diretamente no prompt ou confiar no conhecimento interno do modelo — são suficientes:

<img src="../../../translated_images/pt-PT/when-to-use-rag.1016223f6fea26bc.webp" alt="Quando Usar RAG" width="800"/>

*Este diagrama mostra um guia de decisão para quando o RAG acrescenta valor versus quando são adequadas abordagens mais simples.*

**Use RAG quando:**
- Responder a perguntas sobre documentos proprietários
- Informação muda frequentemente (políticas, preços, especificações)
- A exatidão requer atribuição da fonte
- Conteúdo é demasiado grande para caber num único prompt
- Precisa de respostas verificáveis e fundamentadas

**Não use RAG quando:**
- As perguntas requerem conhecimentos gerais que o modelo já possui
- É necessário dados em tempo real (RAG funciona com documentos carregados)
- O conteúdo é pequeno o suficiente para incluir diretamente nos prompts

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompt](../02-prompt-engineering/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional por um tradutor humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->