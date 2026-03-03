# Módulo 03: RAG (Geração Aumentada por Recuperação)

## Índice

- [Vídeo Demonstrativo](../../../03-rag)
- [O Que Você Vai Aprender](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Entendendo o RAG](../../../03-rag)
  - [Qual Abordagem RAG Este Tutorial Usa?](../../../03-rag)
- [Como Funciona](../../../03-rag)
  - [Processamento de Documentos](../../../03-rag)
  - [Criando Embeddings](../../../03-rag)
  - [Busca Semântica](../../../03-rag)
  - [Geração de Respostas](../../../03-rag)
- [Executando a Aplicação](../../../03-rag)
- [Usando a Aplicação](../../../03-rag)
  - [Enviar um Documento](../../../03-rag)
  - [Fazer Perguntas](../../../03-rag)
  - [Verificar Referências das Fontes](../../../03-rag)
  - [Experimentar com Perguntas](../../../03-rag)
- [Conceitos-Chave](../../../03-rag)
  - [Estratégia de Chunking](../../../03-rag)
  - [Pontuação de Similaridade](../../../03-rag)
  - [Armazenamento em Memória](../../../03-rag)
  - [Gerenciamento da Janela de Contexto](../../../03-rag)
- [Quando o RAG é Importante](../../../03-rag)
- [Próximos Passos](../../../03-rag)

## Vídeo Demonstrativo

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG com LangChain4j - Sessão ao Vivo" width="800"/></a>

## O Que Você Vai Aprender

Nos módulos anteriores, você aprendeu como ter conversas com IA e estruturar seus prompts de forma eficaz. Mas existe uma limitação fundamental: modelos de linguagem só sabem o que aprenderam durante o treinamento. Eles não conseguem responder perguntas sobre as políticas da sua empresa, a documentação do seu projeto ou qualquer informação que não tenha sido usada durante o treinamento.

O RAG (Geração Aumentada por Recuperação) resolve esse problema. Em vez de tentar ensinar suas informações ao modelo (que é caro e impraticável), você dá a ele a capacidade de buscar nos seus documentos. Quando alguém faz uma pergunta, o sistema encontra informações relevantes e as inclui no prompt. O modelo então responde com base nesse contexto recuperado.

Pense no RAG como dar ao modelo uma biblioteca de referência. Quando você faz uma pergunta, o sistema:

1. **Consulta do Usuário** - Você faz uma pergunta  
2. **Embedding** - Converte sua pergunta em um vetor  
3. **Busca Vetorial** - Encontra blocos de documento similares  
4. **Montagem do Contexto** - Adiciona os blocos relevantes ao prompt  
5. **Resposta** - O LLM gera uma resposta baseada no contexto

Isso fundamenta as respostas do modelo nos seus dados reais ao invés de depender só do conhecimento do treinamento ou inventar respostas.

## Pré-requisitos

- Ter completado o [Módulo 00 - Início Rápido](../00-quick-start/README.md) (para o exemplo Easy RAG citado mais adiante neste módulo)  
- Ter completado o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implantados, incluindo o modelo de embedding `text-embedding-3-small`)  
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)

> **Nota:** Se você não completou o Módulo 01, siga as instruções de implantação lá primeiro. O comando `azd up` implanta tanto o modelo de chat GPT quanto o modelo de embedding usados por este módulo.

## Entendendo o RAG

O diagrama abaixo ilustra o conceito central: em vez de depender apenas dos dados de treinamento do modelo, o RAG oferece uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-BR/what-is-rag.1f9005d44b07f2d8.webp" alt="O que é RAG" width="800"/>

*Este diagrama mostra a diferença entre um LLM padrão (que adivinha a partir dos dados de treinamento) e um LLM aprimorado com RAG (que consulta seus documentos primeiro).*

Aqui está como as peças se conectam de ponta a ponta. A pergunta do usuário passa por quatro estágios — embedding, busca vetorial, montagem do contexto e geração da resposta — cada um construindo sobre o anterior:

<img src="../../../translated_images/pt-BR/rag-architecture.ccb53b71a6ce407f.webp" alt="Arquitetura RAG" width="800"/>

*Este diagrama mostra a pipeline RAG de ponta a ponta — a consulta do usuário passa por embedding, busca vetorial, montagem do contexto e geração da resposta.*

O restante deste módulo explora cada etapa em detalhes, com código que você pode executar e modificar.

### Qual Abordagem RAG Este Tutorial Usa?

LangChain4j oferece três formas de implementar RAG, cada uma com um nível diferente de abstração. O diagrama abaixo as compara lado a lado:

<img src="../../../translated_images/pt-BR/rag-approaches.5b97fdcc626f1447.webp" alt="Três Abordagens RAG em LangChain4j" width="800"/>

*Este diagrama compara as três abordagens RAG do LangChain4j — Easy, Nativo e Avançado — mostrando seus componentes chave e quando usar cada uma.*

| Abordagem | O Que Ela Faz | Compromisso |
|---|---|---|
| **Easy RAG** | Integra tudo automaticamente via `AiServices` e `ContentRetriever`. Você anota uma interface, conecta um retriever, e o LangChain4j cuida do embedding, busca e montagem do prompt nos bastidores. | Código mínimo, mas você não vê o que acontece em cada passo. |
| **Native RAG** | Você chama o modelo de embedding, busca na loja, monta o prompt e gera a resposta você mesmo — um passo explícito de cada vez. | Mais código, mas cada etapa é visível e modificável. |
| **Advanced RAG** | Usa o framework `RetrievalAugmentor` com transformadores de consulta plugáveis, roteadores, reordenadores e injetores de conteúdo para pipelines de nível produção. | Flexibilidade máxima, mas com complexidade muito maior. |

**Este tutorial usa a abordagem Nativa.** Cada passo da pipeline RAG — embedding da consulta, busca na loja vetorial, montagem do contexto e geração da resposta — está explicitamente escrito em [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Isso é intencional: como recurso de aprendizado, é mais importante que você veja e entenda cada etapa do que que o código seja minimizado. Quando se sentir confortável com as peças, pode migrar para Easy RAG para protótipos rápidos ou Advanced RAG para sistemas de produção.

> **💡 Já viu o Easy RAG em ação?** O [módulo Início Rápido](../00-quick-start/README.md) inclui um exemplo de Perguntas & Respostas com Documentos ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa a abordagem Easy RAG — o LangChain4j gerencia embedding, busca e montagem do prompt automaticamente. Este módulo dá o próximo passo, detalhando essa pipeline para que você veja e controle cada etapa.

O diagrama abaixo mostra a pipeline Easy RAG do exemplo do Início Rápido. Repare como `AiServices` e `EmbeddingStoreContentRetriever` escondem toda a complexidade — você carrega um documento, conecta um retriever e obtém respostas. A abordagem Nativa deste módulo abre cada uma dessas etapas ocultas:

<img src="../../../translated_images/pt-BR/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Este diagrama mostra a pipeline Easy RAG de `SimpleReaderDemo.java`. Compare com a abordagem Nativa usada neste módulo: Easy RAG oculta embedding, recuperação e montagem do prompt atrás de `AiServices` e `ContentRetriever` — você carrega um documento, conecta um retriever e obtém respostas. A abordagem Nativa deste módulo abre essa pipeline para que você chame cada etapa (embedar, buscar, montar contexto, gerar) você mesmo, oferecendo visibilidade e controle completos.*

## Como Funciona

A pipeline RAG deste módulo se divide em quatro etapas que são executadas em sequência toda vez que um usuário faz uma pergunta. Primeiro, um documento enviado é **analisado e fragmentado** em pedaços gerenciáveis. Esses pedaços são então convertidos em **embeddings vetoriais** e armazenados para comparações matemáticas. Quando chega uma consulta, o sistema realiza uma **busca semântica** para encontrar os pedaços mais relevantes e, por fim, os passa como contexto para o LLM para **geração da resposta**. As seções abaixo descrevem cada etapa com o código real e diagramas. Vamos olhar o primeiro passo.

### Processamento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando você envia um documento, o sistema o analisa (PDF ou texto simples), anexa metadados como o nome do arquivo e então divide em pedaços — partes menores que cabem confortavelmente na janela de contexto do modelo. Esses pedaços se sobrepõem ligeiramente para que você não perca o contexto nas fronteiras.

```java
// Analise o arquivo enviado e envolva-o em um Documento LangChain4j
Document document = Document.from(content, metadata);

// Divida em pedaços de 300 tokens com sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
O diagrama abaixo mostra como isso funciona visualmente. Note como cada pedaço compartilha alguns tokens com seus vizinhos — a sobreposição de 30 tokens garante que nenhum contexto importante fique perdido:

<img src="../../../translated_images/pt-BR/document-chunking.a5df1dd1383431ed.webp" alt="Fragmentação de Documento" width="800"/>

*Este diagrama mostra um documento sendo dividido em pedaços de 300 tokens com sobreposição de 30 tokens, preservando o contexto nas bordas dos pedaços.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:  
> - "Como o LangChain4j divide documentos em pedaços e por que a sobreposição é importante?"  
> - "Qual é o tamanho ideal dos pedaços para diferentes tipos de documento e por quê?"  
> - "Como lidar com documentos em múltiplos idiomas ou com formatação especial?"

### Criando Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada pedaço é convertido em uma representação numérica chamada embedding — essencialmente um conversor do significado para números. O modelo de embedding não é "inteligente" como um modelo de chat; ele não segue instruções, não raciocina nem responde perguntas. O que ele faz é mapear texto em um espaço matemático onde significados similares ficam próximos — "carro" perto de "automóvel", "política de reembolso" perto de "devolver meu dinheiro". Pense em um modelo de chat como uma pessoa com quem você conversa; um modelo de embedding é um sistema de arquivamento ultra eficaz.

O diagrama abaixo visualiza este conceito — o texto entra, vetores numéricos saem, e significados similares produzem vetores próximos:

<img src="../../../translated_images/pt-BR/embedding-model-concept.90760790c336a705.webp" alt="Conceito de Modelo de Embedding" width="800"/>

*Este diagrama mostra como um modelo de embedding converte texto em vetores numéricos, posicionando significados similares — como "carro" e "automóvel" — próximos uns dos outros no espaço vetorial.*

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
  
O diagrama de classes abaixo mostra os dois fluxos separados numa pipeline RAG e as classes do LangChain4j que os implementam. O **fluxo de ingestão** (executado uma vez na hora do upload) divide o documento, gera embeddings dos pedaços e os armazena via `.addAll()`. O **fluxo de consulta** (executado toda vez que um usuário pergunta) gera embedding da pergunta, busca na loja via `.search()` e passa o contexto correspondente para o modelo de chat. Ambos os fluxos convergem na interface compartilhada `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/pt-BR/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Classes RAG do LangChain4j" width="800"/>

*Este diagrama mostra os dois fluxos numa pipeline RAG — ingestão e consulta — e como eles se conectam através de um EmbeddingStore compartilhado.*

Uma vez que embeddings estão armazenados, conteúdos similares naturalmente formam aglomerados no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados ficam próximos, o que torna possível a busca semântica:

<img src="../../../translated_images/pt-BR/vector-embeddings.2ef7bdddac79a327.webp" alt="Espaço de Embeddings Vetoriais" width="800"/>

*Esta visualização demonstra como documentos relacionados se agrupam no espaço vetorial 3D, com tópicos como Documentação Técnica, Regras de Negócio e FAQs formando grupos distintos.*

Quando um usuário faz uma busca, o sistema segue quatro passos: embedar os documentos uma vez, embedar a consulta a cada busca, comparar o vetor da consulta com todos os vetores armazenados usando similaridade do cosseno e retornar os top-K pedaços com pontuação mais alta. O diagrama abaixo mostra cada passo e as classes LangChain4j envolvidas:

<img src="../../../translated_images/pt-BR/embedding-search-steps.f54c907b3c5b4332.webp" alt="Passos da Busca por Embeddings" width="800"/>

*Este diagrama mostra o processo de busca em quatro etapas: embedar documentos, embedar a consulta, comparar vetores com similaridade do cosseno e retornar os top-K resultados.*

### Busca Semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando você pergunta algo, sua pergunta também vira um embedding. O sistema compara o embedding da sua pergunta com os embeddings de todos os pedaços do documento. Ele encontra os pedaços com significados mais similares — não só combinando palavras-chave, mas com real similaridade semântica.

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
  
O diagrama abaixo contrasta busca semântica com busca tradicional por palavra-chave. Uma busca por palavra-chave por "veículo" perde um pedaço sobre "carros e caminhões", mas a busca semântica entende que significam a mesma coisa e o retorna como uma correspondência com alta pontuação:

<img src="../../../translated_images/pt-BR/semantic-search.6b790f21c86b849d.webp" alt="Busca Semântica" width="800"/>

*Este diagrama compara busca baseada em palavra-chave com busca semântica, mostrando como a busca semântica recupera conteúdo relacionado conceitualmente mesmo quando as palavras exatas diferem.*
Por trás das cenas, a similaridade é medida usando similaridade do cosseno — basicamente perguntando "esses dois vetores estão apontando na mesma direção?" Dois pedaços podem usar palavras completamente diferentes, mas se tiverem o mesmo significado, seus vetores apontam na mesma direção e obtêm uma pontuação próxima de 1,0:

<img src="../../../translated_images/pt-BR/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Este diagrama ilustra a similaridade do cosseno como o ângulo entre os vetores de embedding — vetores mais alinhados obtêm uma pontuação mais próxima de 1,0, indicando maior similaridade semântica.*

> **🤖 Experimente com [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:
> - "Como funciona a busca por similaridade com embeddings e o que determina a pontuação?"
> - "Qual limiar de similaridade devo usar e como isso afeta os resultados?"
> - "Como lidar com casos em que nenhum documento relevante é encontrado?"

### Geração de Resposta

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os pedaços mais relevantes são montados em um prompt estruturado que inclui instruções explícitas, o contexto recuperado e a pergunta do usuário. O modelo lê esses pedaços específicos e responde com base nessa informação — ele só pode usar o que está à sua frente, o que evita alucinações.

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

O diagrama abaixo mostra essa montagem em ação — os pedaços com melhor pontuação da etapa de busca são injetados no template de prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-BR/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Este diagrama mostra como os pedaços com melhor pontuação são montados em um prompt estruturado, permitindo que o modelo gere uma resposta fundamentada a partir dos seus dados.*

## Execute a Aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` exista no diretório raiz com as credenciais do Azure (criadas durante o Módulo 01). Execute isto a partir do diretório do módulo (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` a partir do diretório raiz (como descrito no Módulo 01), este módulo já está rodando na porta 8081. Você pode pular os comandos de inicialização abaixo e ir diretamente para http://localhost:8081.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários do VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-lo na Barra de Atividades do lado esquerdo do VS Code (procure o ícone do Spring Boot).

A partir do Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um único clique
- Visualizar logs das aplicações em tempo real
- Monitorar o status das aplicações

Basta clicar no botão de play ao lado de "rag" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta captura de tela mostra o Spring Boot Dashboard no VS Code, onde você pode iniciar, parar e monitorar aplicações visualmente.*

**Opção 2: Usando scripts shell**

Inicie todas as aplicações web (módulos 01-04):

**Bash:**
```bash
cd ..  # A partir do diretório raiz
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Do diretório raiz
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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` raiz e vão construir os JARs caso não existam.

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

Abra http://localhost:8081 no seu navegador.

**Para parar:**

**Bash:**
```bash
./stop.sh  # Este módulo apenas
# Ou
cd .. && ./stop-all.sh  # Todos os módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Usando a Aplicação

A aplicação fornece uma interface web para upload de documentos e realização de perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-BR/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura de tela mostra a interface da aplicação RAG onde você pode enviar documentos e fazer perguntas.*

### Enviar um Documento

Comece enviando um documento — arquivos TXT funcionam melhor para testes. Um `sample-document.txt` é fornecido neste diretório e contém informações sobre recursos do LangChain4j, implementação do RAG e melhores práticas — perfeito para testar o sistema.

O sistema processa seu documento, divide em pedaços, e cria embeddings para cada pedaço. Isso acontece automaticamente quando você faz o upload.

### Faça Perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Tente algo factual que esteja claramente declarado no documento. O sistema busca pedaços relevantes, inclui-os no prompt, e gera uma resposta.

### Verifique as Referências de Fonte

Observe que cada resposta inclui referências de fonte com pontuações de similaridade. Essas pontuações (de 0 a 1) mostram o quão relevante cada pedaço foi para sua pergunta. Pontuações mais altas significam correspondências melhores. Isso permite que você verifique a resposta com o material fonte.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-BR/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura de tela mostra os resultados da consulta com a resposta gerada, referências de fonte e pontuações de relevância para cada pedaço recuperado.*

### Experimente com Perguntas

Tente diferentes tipos de perguntas:
- Fatos específicos: "Qual é o tema principal?"
- Comparações: "Qual a diferença entre X e Y?"
- Resumos: "Resuma os pontos chave sobre Z"

Observe como as pontuações de relevância mudam com base no quão bem sua pergunta corresponde ao conteúdo do documento.

## Conceitos-Chave

### Estratégia de Quebra em Pedaços

Documentos são divididos em pedaços de 300 tokens com 30 tokens de sobreposição. Esse equilíbrio garante que cada pedaço tenha contexto suficiente para ser significativo, enquanto permanece pequeno o suficiente para incluir múltiplos pedaços em um prompt.

### Pontuações de Similaridade

Cada pedaço recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica o quão próximo ele está da pergunta do usuário. O diagrama abaixo visualiza as faixas de pontuação e como o sistema as usa para filtrar resultados:

<img src="../../../translated_images/pt-BR/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Este diagrama mostra faixas de pontuação de 0 a 1, com um limiar mínimo de 0,5 que filtra pedaços irrelevantes.*

As pontuações variam de 0 a 1:
- 0,7-1,0: Altamente relevante, correspondência exata
- 0,5-0,7: Relevante, bom contexto
- Abaixo de 0,5: Filtrado, muito diferente

O sistema recupera apenas pedaços acima do limiar mínimo para garantir qualidade.

Embeddings funcionam bem quando o significado se agrupa claramente, mas têm pontos cegos. O diagrama abaixo mostra modos comuns de falha — pedaços muito grandes produzem vetores confusos, pedaços muito pequenos carecem de contexto, termos ambíguos apontam para múltiplos agrupamentos e buscas por correspondência exata (IDs, números de peça) não funcionam com embeddings:

<img src="../../../translated_images/pt-BR/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Este diagrama mostra modos comuns de falha em embeddings: pedaços grandes demais, pedaços pequenos demais, termos ambíguos que apontam para múltiplos agrupamentos e buscas por correspondência exata como IDs.*

### Armazenamento em Memória

Este módulo usa armazenamento em memória para simplicidade. Quando você reinicia a aplicação, documentos enviados são perdidos. Sistemas de produção usam bancos de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gerenciamento da Janela de Contexto

Cada modelo tem uma janela máxima de contexto. Você não pode incluir todos os pedaços de um documento grande. O sistema recupera os N pedaços mais relevantes (padrão 5) para permanecer dentro dos limites, oferecendo contexto suficiente para respostas precisas.

## Quando RAG é Importante

RAG nem sempre é a abordagem certa. O guia de decisão abaixo ajuda a determinar quando RAG agrega valor versus quando abordagens mais simples — como incluir conteúdo direto no prompt ou confiar no conhecimento embutido do modelo — são suficientes:

<img src="../../../translated_images/pt-BR/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Este diagrama mostra um guia de decisão para quando RAG agrega valor versus quando abordagens mais simples são suficientes.*

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompt](../02-prompt-engineering/README.md) | [Voltar ao Principal](../README.md) | [Próximo: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->