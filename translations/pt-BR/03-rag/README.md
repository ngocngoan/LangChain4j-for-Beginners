# Módulo 03: RAG (Geração Aumentada por Recuperação)

## Índice

- [Vídeo Passo a Passo](../../../03-rag)
- [O Que Você Vai Aprender](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Entendendo o RAG](../../../03-rag)
  - [Qual Abordagem de RAG Este Tutorial Usa?](../../../03-rag)
- [Como Funciona](../../../03-rag)
  - [Processamento de Documentos](../../../03-rag)
  - [Criando Embeddings](../../../03-rag)
  - [Busca Semântica](../../../03-rag)
  - [Geração de Respostas](../../../03-rag)
- [Executar a Aplicação](../../../03-rag)
- [Usando a Aplicação](../../../03-rag)
  - [Enviar um Documento](../../../03-rag)
  - [Fazer Perguntas](../../../03-rag)
  - [Verificar Referências das Fontes](../../../03-rag)
  - [Experimentar com Perguntas](../../../03-rag)
- [Conceitos-Chave](../../../03-rag)
  - [Estratégia de Divisão em Blocos](../../../03-rag)
  - [Pontuação de Similaridade](../../../03-rag)
  - [Armazenamento em Memória](../../../03-rag)
  - [Gerenciamento da Janela de Contexto](../../../03-rag)
- [Quando o RAG Importa](../../../03-rag)
- [Próximos Passos](../../../03-rag)

## Vídeo Passo a Passo

Assista a esta sessão ao vivo que explica como começar com este módulo: [RAG com LangChain4j - Sessão Ao Vivo](https://www.youtube.com/watch?v=_olq75ZH_eY)

## O Que Você Vai Aprender

Nos módulos anteriores, você aprendeu como ter conversas com IA e estruturar seus prompts de forma eficaz. Mas existe uma limitação fundamental: os modelos de linguagem só sabem o que aprenderam durante o treinamento. Eles não conseguem responder perguntas sobre as políticas da sua empresa, a documentação do seu projeto ou qualquer informação na qual não foram treinados.

O RAG (Geração Aumentada por Recuperação) resolve esse problema. Em vez de tentar ensinar as informações para o modelo (o que é caro e impraticável), você dá a ele a capacidade de buscar nos seus documentos. Quando alguém faz uma pergunta, o sistema encontra informações relevantes e as inclui no prompt. O modelo então responde com base nesse contexto recuperado.

Pense no RAG como dar ao modelo uma biblioteca de referência. Quando você faz uma pergunta, o sistema:

1. **Consulta do Usuário** - Você faz uma pergunta  
2. **Embedding** - Converte sua pergunta em um vetor  
3. **Busca Vetorial** - Encontra blocos de documentos similares  
4. **Montagem do Contexto** - Adiciona blocos relevantes ao prompt  
5. **Resposta** - LLM gera uma resposta baseada no contexto  

Isso fundamenta as respostas do modelo nos seus dados reais em vez de depender apenas do conhecimento do treinamento ou inventar respostas.

## Pré-requisitos

- Ter completado o [Módulo 00 - Início Rápido](../00-quick-start/README.md) (para o exemplo Easy RAG citado acima)  
- Ter completado o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implantados, incluindo o modelo de embedding `text-embedding-3-small`)  
- Arquivo `.env` no diretório raiz com credenciais Azure (criado pelo `azd up` no Módulo 01)  

> **Nota:** Se você não completou o Módulo 01, siga as instruções de implantação lá primeiro. O comando `azd up` implanta tanto o modelo de chat GPT quanto o modelo de embedding usado neste módulo.

## Entendendo o RAG

O diagrama abaixo ilustra o conceito principal: em vez de depender apenas dos dados de treinamento do modelo, o RAG lhe dá uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-BR/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Este diagrama mostra a diferença entre um LLM padrão (que tenta adivinhar a partir dos dados de treinamento) e um LLM com RAG (que consulta seus documentos primeiro).*

Veja como as etapas se conectam de ponta a ponta. A pergunta do usuário flui por quatro estágios — embedding, busca vetorial, montagem do contexto e geração da resposta — cada um construindo sobre o anterior:

<img src="../../../translated_images/pt-BR/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Este diagrama mostra o pipeline completo do RAG — uma consulta do usuário passa por embedding, busca vetorial, montagem do contexto e geração da resposta.*

O restante deste módulo detalha cada estágio, com código que você pode executar e modificar.

### Qual Abordagem de RAG Este Tutorial Usa?

LangChain4j oferece três formas de implementar RAG, cada uma com um nível diferente de abstração. O diagrama abaixo os compara lado a lado:

<img src="../../../translated_images/pt-BR/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Este diagrama compara as três abordagens RAG do LangChain4j — Easy, Native e Advanced — mostrando seus componentes principais e quando usar cada uma.*

| Abordagem | O Que Faz | Compromisso |
|---|---|---|
| **Easy RAG** | Conecta tudo automaticamente através de `AiServices` e `ContentRetriever`. Você anota uma interface, adiciona um retriever e LangChain4j cuida do embedding, pesquisa e montagem do prompt nos bastidores. | Código mínimo, mas você não vê o que acontece em cada etapa. |
| **Native RAG** | Você chama o modelo de embedding, pesquisa na store, monta o prompt e gera a resposta — um passo explícito de cada vez. | Mais código, mas todas as etapas são visíveis e modificáveis. |
| **Advanced RAG** | Usa o framework `RetrievalAugmentor` com transformadores de consulta plugáveis, roteadores, re-rankers e injetores de conteúdo para pipelines em produção. | Máxima flexibilidade, mas muito mais complexidade. |

**Este tutorial usa a abordagem Native.** Cada etapa do pipeline RAG — embedding da consulta, busca na store vetorial, montagem do contexto e geração da resposta — está escrita explicitamente em [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Isso é intencional: como recurso de aprendizado, é mais importante que você veja e entenda cada etapa do que que o código seja minimizado. Quando você estiver confortável com o funcionamento das partes, poderá avançar para Easy RAG para protótipos rápidos ou Advanced RAG para sistemas em produção.

> **💡 Já viu Easy RAG em ação?** O [módulo Início Rápido](../00-quick-start/README.md) inclui um exemplo Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa a abordagem Easy RAG — LangChain4j cuida do embedding, pesquisa e montagem do prompt automaticamente. Este módulo dá o próximo passo abrindo esse pipeline para que você veja e controle cada etapa.

<img src="../../../translated_images/pt-BR/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Este diagrama mostra o pipeline Easy RAG do `SimpleReaderDemo.java`. Compare com a abordagem Native usada neste módulo: o Easy RAG esconde o embedding, recuperação e montagem do prompt atrás de `AiServices` e `ContentRetriever` — você carrega um documento, adiciona um retriever e obtém respostas. A abordagem Native deste módulo abre esse pipeline para que você chame cada etapa (embed, buscar, montar contexto, gerar) você mesmo, dando visibilidade e controle total.*

## Como Funciona

O pipeline RAG neste módulo se divide em quatro etapas que executam em sequência toda vez que um usuário faz uma pergunta. Primeiro, um documento enviado é **analisado e dividido em blocos** gerenciáveis. Esses blocos são então convertidos em **embeddings vetoriais** e armazenados para que possam ser comparados matematicamente. Quando uma consulta chega, o sistema realiza uma **busca semântica** para encontrar os blocos mais relevantes, e finalmente os passa como contexto para o LLM para a **geração da resposta**. As seções abaixo explicam cada etapa com o código real e diagramas. Vamos ver o primeiro passo.

### Processamento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando você envia um documento, o sistema o analisa (PDF ou texto simples), adiciona metadados como o nome do arquivo, e então o divide em blocos — pedaços menores que cabem confortavelmente na janela de contexto do modelo. Esses blocos se sobrepõem levemente para que não se perca contexto nas bordas.

```java
// Analise o arquivo carregado e envolva-o em um Documento LangChain4j
Document document = Document.from(content, metadata);

// Divida em pedaços de 300 tokens com sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
O diagrama abaixo mostra como isso funciona visualmente. Note como cada bloco compartilha algumas tokens com seus vizinhos — a sobreposição de 30 tokens garante que nenhum contexto importante fique perdido entre as divisões:

<img src="../../../translated_images/pt-BR/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Este diagrama mostra um documento sendo dividido em blocos de 300 tokens com sobreposição de 30 tokens, preservando o contexto nas bordas dos blocos.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:
> - "Como o LangChain4j divide documentos em blocos e por que a sobreposição é importante?"
> - "Qual o tamanho de bloco ideal para diferentes tipos de documentos e por quê?"
> - "Como lidar com documentos em múltiplos idiomas ou com formatação especial?"

### Criando Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada bloco é convertido em uma representação numérica chamada embedding — essencialmente um conversor de significado para números. O modelo de embedding não é "inteligente" como um modelo de chat; ele não pode seguir instruções, raciocinar ou responder perguntas. O que ele pode fazer é mapear texto em um espaço matemático onde significados similares ficam próximos — "carro" perto de "automóvel," "política de reembolso" perto de "devolve meu dinheiro." Pense em um modelo de chat como uma pessoa para conversar; um modelo de embedding é um sistema de arquivamento ultra eficiente.

<img src="../../../translated_images/pt-BR/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Este diagrama mostra como um modelo de embedding converte texto em vetores numéricos, colocando significados similares — como "carro" e "automóvel" — próximos no espaço vetorial.*

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
  
O diagrama de classes abaixo mostra os dois fluxos separados em um pipeline RAG e as classes LangChain4j que os implementam. O **fluxo de ingestão** (executado uma vez no envio) divide o documento, embede os blocos e os armazena via `.addAll()`. O **fluxo de consulta** (que roda a cada pergunta) embede a questão, pesquisa na store via `.search()`, e passa o contexto encontrado para o modelo de chat. Ambos os fluxos se conectam pela interface `EmbeddingStore<TextSegment>` compartilhada:

<img src="../../../translated_images/pt-BR/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Este diagrama mostra os dois fluxos em um pipeline RAG — ingestão e consulta — e como se conectam através de uma EmbeddingStore compartilhada.*

Uma vez armazenados, conteúdos similares naturalmente se agrupam juntos no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados ficam próximos, o que torna possível a busca semântica:

<img src="../../../translated_images/pt-BR/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Esta visualização mostra como documentos relacionados se agrupam no espaço vetorial 3D, com tópicos como Documentos Técnicos, Regras de Negócio e FAQs formando grupos distintos.*

Quando um usuário pesquisa, o sistema segue quatro passos: embede os documentos uma vez, embede a consulta a cada busca, compara o vetor da consulta com todos os vetores armazenados usando similaridade do cosseno, e retorna os top-K blocos com maior pontuação. O diagrama abaixo ilustra cada passo e as classes LangChain4j envolvidas:

<img src="../../../translated_images/pt-BR/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Este diagrama mostra o processo de busca por embedding em quatro etapas: embeder documentos, embedar a consulta, comparar vetores com similaridade do cosseno, e retornar os top-K resultados.*

### Busca Semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando você faz uma pergunta, a própria pergunta vira um embedding. O sistema compara o embedding da sua questão com os embeddings de todos os blocos de documentos. Ele encontra os blocos com significados mais semelhantes — não apenas palavras-chave iguais, mas similaridade semântica real.

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
  
O diagrama abaixo contrasta a busca semântica com a busca tradicional por palavras-chave. Uma busca por palavra-chave por "veículo" perde um bloco sobre "carros e caminhões," mas a busca semântica entende que eles significam a mesma coisa e retorna esse bloco com alta pontuação:

<img src="../../../translated_images/pt-BR/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Este diagrama compara busca baseada em palavra-chave com busca semântica, mostrando como a busca semântica recupera conteúdo conceitualmente relacionado mesmo quando as palavras exatas divergem.*

Por trás das cenas, a similaridade é medida usando similaridade do cosseno — basicamente perguntando "esses dois vetores apontam na mesma direção?" Dois blocos podem usar palavras completamente diferentes, mas se significam a mesma coisa, seus vetores apontam na mesma direção e têm pontuação próxima de 1.0:

<img src="../../../translated_images/pt-BR/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Este diagrama ilustra a similaridade do cosseno como o ângulo entre vetores de embedding — vetores mais alinhados pontuam mais próximos de 1.0, indicando maior similaridade semântica.*
> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:
> - "Como funciona a busca por similaridade com embeddings e o que determina a pontuação?"
> - "Qual limite de similaridade devo usar e como isso afeta os resultados?"
> - "Como lidar com casos onde nenhum documento relevante é encontrado?"

### Geração de Resposta

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os fragmentos mais relevantes são montados em um prompt estruturado que inclui instruções explícitas, o contexto recuperado e a pergunta do usuário. O modelo lê esses fragmentos específicos e responde com base nessa informação — ele só pode usar o que está à sua frente, o que evita alucinações.

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

O diagrama abaixo mostra essa montagem em ação — os fragmentos com maior pontuação da etapa de busca são inseridos no template do prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-BR/context-assembly.7e6dd60c31f95978.webp" alt="Montagem do Contexto" width="800"/>

*Este diagrama mostra como os fragmentos com maior pontuação são montados em um prompt estruturado, permitindo que o modelo gere uma resposta fundamentada a partir dos seus dados.*

## Execute a Aplicação

**Verifique a implantação:**

Certifique-se de que o arquivo `.env` exista no diretório raiz com as credenciais Azure (criado durante o Módulo 01):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Inicie a aplicação:**

> **Nota:** Se você já iniciou todas as aplicações usando `./start-all.sh` do Módulo 01, este módulo já está rodando na porta 8081. Você pode pular os comandos de inicialização abaixo e ir direto para http://localhost:8081.

**Opção 1: Usando o Spring Boot Dashboard (Recomendado para usuários do VS Code)**

O contêiner de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerenciar todas as aplicações Spring Boot. Você pode encontrá-la na Barra de Atividades à esquerda do VS Code (procure o ícone do Spring Boot).

No Spring Boot Dashboard, você pode:
- Ver todas as aplicações Spring Boot disponíveis no espaço de trabalho
- Iniciar/parar aplicações com um único clique
- Visualizar logs da aplicação em tempo real
- Monitorar o status da aplicação

Basta clicar no botão de play ao lado de "rag" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-BR/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta captura de tela mostra o Spring Boot Dashboard no VS Code, onde você pode iniciar, parar e monitorar as aplicações visualmente.*

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do arquivo `.env` na raiz e irão construir os JARs caso não existam.

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
./stop.sh  # Apenas este módulo
# Ou
cd .. && ./stop-all.sh  # Todos os módulos
```

**PowerShell:**
```powershell
.\stop.ps1  # Este módulo apenas
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Usando a Aplicação

A aplicação oferece uma interface web para upload de documentos e perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-BR/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interface da Aplicação RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura de tela mostra a interface da aplicação RAG onde você faz upload de documentos e faz perguntas.*

### Fazer Upload de um Documento

Comece fazendo upload de um documento - arquivos TXT funcionam melhor para testes. Um `sample-document.txt` está disponível neste diretório e contém informações sobre recursos do LangChain4j, implementação RAG e melhores práticas - perfeito para testar o sistema.

O sistema processa seu documento, divide-o em fragmentos e cria embeddings para cada um. Isso acontece automaticamente quando você faz o upload.

### Faça Perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Tente algo factual que esteja claramente declarado no documento. O sistema busca fragmentos relevantes, os inclui no prompt e gera uma resposta.

### Verifique as Referências da Fonte

Note que cada resposta inclui referências das fontes com as pontuações de similaridade. Estas pontuações (de 0 a 1) mostram o quão relevante cada fragmento foi para sua pergunta. Pontuações maiores indicam correspondências melhores. Isso permite que você verifique a resposta com o material fonte.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-BR/rag-query-results.6d69fcec5397f355.webp" alt="Resultados da Consulta RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura de tela mostra os resultados da consulta com a resposta gerada, referências das fontes e pontuações de relevância para cada fragmento recuperado.*

### Experimente com Perguntas

Tente diferentes tipos de perguntas:
- Fatos específicos: "Qual é o tema principal?"
- Comparações: "Qual a diferença entre X e Y?"
- Resumos: "Resuma os pontos-chave sobre Z"

Observe como as pontuações de relevância mudam com base na correspondência da sua pergunta com o conteúdo do documento.

## Conceitos-Chave

### Estratégia de Fragmentação

Os documentos são divididos em fragmentos de 300 tokens com 30 tokens de sobreposição. Esse equilíbrio garante que cada fragmento tenha contexto suficiente para ser significativo, mantendo-os pequenos o bastante para incluir múltiplos fragmentos num prompt.

### Pontuações de Similaridade

Cada fragmento recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica o quão próximo ele corresponde à pergunta do usuário. O diagrama abaixo visualiza as faixas de pontuação e como o sistema as usa para filtrar resultados:

<img src="../../../translated_images/pt-BR/similarity-scores.b0716aa911abf7f0.webp" alt="Pontuações de Similaridade" width="800"/>

*Este diagrama mostra faixas de pontuação de 0 a 1, com um limite mínimo de 0,5 que filtra fragmentos irrelevantes.*

As pontuações variam de 0 a 1:
- 0,7-1,0: Altamente relevante, correspondência exata
- 0,5-0,7: Relevante, bom contexto
- Abaixo de 0,5: Filtrado, muito diferente

O sistema recupera apenas fragmentos acima do limite mínimo para garantir qualidade.

Embeddings funcionam bem quando o significado se agrupa claramente, mas têm pontos cegos. O diagrama abaixo mostra modos comuns de falha — fragmentos muito grandes produzem vetores confusos, fragmentos muito pequenos carecem de contexto, termos ambíguos apontam para múltiplos agrupamentos, e buscas por correspondência exata (IDs, números de peça) não funcionam com embeddings:

<img src="../../../translated_images/pt-BR/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modos de Falha de Embedding" width="800"/>

*Este diagrama mostra modos comuns de falha de embedding: fragmentos muito grandes, fragmentos muito pequenos, termos ambíguos que apontam para múltiplos agrupamentos, e buscas por correspondência exata como IDs.*

### Armazenamento em Memória

Este módulo usa armazenamento em memória para simplicidade. Quando você reinicia a aplicação, os documentos enviados são perdidos. Sistemas de produção usam bancos de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gestão da Janela de Contexto

Cada modelo tem uma janela máxima de contexto. Você não pode incluir todos os fragmentos de um documento grande. O sistema recupera os N fragmentos mais relevantes (padrão 5) para ficar dentro dos limites, enquanto fornece contexto suficiente para respostas precisas.

## Quando o RAG é Importante

RAG nem sempre é a abordagem certa. O guia abaixo ajuda a determinar quando o RAG agrega valor versus quando abordagens mais simples — como incluir conteúdo diretamente no prompt ou confiar no conhecimento embutido do modelo — são suficientes:

<img src="../../../translated_images/pt-BR/when-to-use-rag.1016223f6fea26bc.webp" alt="Quando Usar RAG" width="800"/>

*Este diagrama mostra um guia de decisão para quando o RAG agrega valor versus quando abordagens mais simples são suficientes.*

**Use RAG quando:**
- Responder perguntas sobre documentos proprietários
- Informação muda frequentemente (políticas, preços, especificações)
- Precisar de precisão com atribuição de fontes
- Conteúdo é grande demais para caber num único prompt
- Você precisa de respostas verificáveis e fundamentadas

**Não use RAG quando:**
- Perguntas requerem conhecimento geral que o modelo já possui
- Dados em tempo real são necessários (RAG trabalha com documentos enviados)
- Conteúdo é pequeno o suficiente para incluir diretamente nos prompts

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompt](../02-prompt-engineering/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autorizada. Para informações críticas, recomenda-se tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->