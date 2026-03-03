# Módulo 03: RAG (Geração Aumentada por Recuperação)

## Índice

- [Guia em Vídeo](../../../03-rag)
- [O Que Vai Aprender](../../../03-rag)
- [Pré-requisitos](../../../03-rag)
- [Compreender o RAG](../../../03-rag)
  - [Que Abordagem RAG Este Tutorial Usa?](../../../03-rag)
- [Como Funciona](../../../03-rag)
  - [Processamento de Documentos](../../../03-rag)
  - [Criação de Embeddings](../../../03-rag)
  - [Pesquisa Semântica](../../../03-rag)
  - [Geração de Respostas](../../../03-rag)
- [Executar a Aplicação](../../../03-rag)
- [Usar a Aplicação](../../../03-rag)
  - [Enviar um Documento](../../../03-rag)
  - [Fazer Perguntas](../../../03-rag)
  - [Verificar Referências de Origem](../../../03-rag)
  - [Experimentar com Perguntas](../../../03-rag)
- [Conceitos-Chave](../../../03-rag)
  - [Estratégia de Divisão em Pedaços](../../../03-rag)
  - [Pontuações de Similaridade](../../../03-rag)
  - [Armazenamento em Memória](../../../03-rag)
  - [Gestão da Janela de Contexto](../../../03-rag)
- [Quando o RAG Importa](../../../03-rag)
- [Próximos Passos](../../../03-rag)

## Guia em Vídeo

Assista a esta sessão ao vivo que explica como começar com este módulo:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG com LangChain4j - Sessão ao Vivo" width="800"/></a>

## O Que Vai Aprender

Nos módulos anteriores, aprendeu como ter conversas com IA e estruturar os seus prompts de forma eficaz. Mas há uma limitação fundamental: os modelos de linguagem só sabem o que aprenderam durante o treino. Não conseguem responder a perguntas sobre as políticas da sua empresa, documentação do seu projeto, ou qualquer informação em que não foram treinados.

O RAG (Geração Aumentada por Recuperação) resolve este problema. Em vez de tentar ensinar ao modelo as suas informações (que é caro e impraticável), dá-lhe a capacidade de pesquisar nos seus documentos. Quando alguém faz uma pergunta, o sistema encontra a informação relevante e inclui-a no prompt. O modelo responde então com base nesse contexto recuperado.

Pense no RAG como dar ao modelo uma biblioteca de referência. Quando faz uma pergunta, o sistema:

1. **Consulta do Utilizador** - Você faz uma pergunta  
2. **Embedding** - Converte a sua pergunta num vetor  
3. **Pesquisa Vetorial** - Encontra pedaços de documentos semelhantes  
4. **Montagem do Contexto** - Adiciona os pedaços relevantes ao prompt  
5. **Resposta** - O LLM gera uma resposta com base no contexto  

Isto fundamenta as respostas do modelo nos seus dados reais em vez de depender no conhecimento aprendido ou inventar respostas.

## Pré-requisitos

- Completar o [Módulo 00 - Começo Rápido](../00-quick-start/README.md) (para o exemplo Easy RAG referido mais à frente neste módulo)  
- Completar o [Módulo 01 - Introdução](../01-introduction/README.md) (recursos Azure OpenAI implementados, incluindo o modelo de embedding `text-embedding-3-small`)  
- Ficheiro `.env` na raiz com as credenciais Azure (criado pelo comando `azd up` no Módulo 01)  

> **Nota:** Se ainda não completou o Módulo 01, siga primeiro as instruções de implementação lá descritas. O comando `azd up` implementa tanto o modelo de chat GPT como o modelo de embedding usados neste módulo.

## Compreender o RAG

O diagrama abaixo ilustra o conceito principal: em vez de depender só dos dados de treino do modelo, o RAG fornece ao modelo uma biblioteca de referência dos seus documentos para consultar antes de gerar cada resposta.

<img src="../../../translated_images/pt-PT/what-is-rag.1f9005d44b07f2d8.webp" alt="O Que é o RAG" width="800"/>

*Este diagrama mostra a diferença entre um LLM padrão (que adivinha a partir dos dados de treino) e um LLM potenciado por RAG (que consulta primeiro os seus documentos).*

Aqui está como as peças se ligam de ponta a ponta. A pergunta de um utilizador passa por quatro fases — embedding, pesquisa vetorial, montagem do contexto e geração da resposta — cada uma construindo a anterior:

<img src="../../../translated_images/pt-PT/rag-architecture.ccb53b71a6ce407f.webp" alt="Arquitetura do RAG" width="800"/>

*Este diagrama mostra a pipeline RAG de ponta a ponta — uma consulta do utilizador passa por embedding, pesquisa vetorial, montagem do contexto e geração da resposta.*

O resto deste módulo explica cada etapa em detalhe, com código que pode executar e modificar.

### Que Abordagem RAG Este Tutorial Usa?

LangChain4j oferece três maneiras de implementar RAG, cada uma com um nível diferente de abstração. O diagrama abaixo compara-as lado a lado:

<img src="../../../translated_images/pt-PT/rag-approaches.5b97fdcc626f1447.webp" alt="Três Abordagens RAG no LangChain4j" width="800"/>

*Este diagrama compara as três abordagens RAG do LangChain4j — Easy, Native e Advanced — mostrando os seus componentes principais e quando usar cada uma.*

| Abordagem | O Que Faz | Compromisso |
|---|---|---|
| **Easy RAG** | Liga tudo automaticamente através de `AiServices` e `ContentRetriever`. Anota-se uma interface, liga-se um recuperador, e o LangChain4j trata dos embeddings, da pesquisa e da montagem do prompt nos bastidores. | Código mínimo, mas não vê o que acontece em cada passo. |
| **Native RAG** | Chama diretamente o modelo de embedding, pesquisa na store, constrói o prompt e gera a resposta — um passo explícito de cada vez. | Mais código, mas cada etapa é visível e pode ser modificada. |
| **Advanced RAG** | Usa o framework `RetrievalAugmentor` com transformadores de consultas, routers, reclassificadores e injetores de conteúdo plugáveis para pipelines de produção. | Flexibilidade máxima, mas muito mais complexo. |

**Este tutorial usa a abordagem Native.** Cada etapa da pipeline RAG — embedding da consulta, pesquisa na store vetorial, montagem do contexto e geração da resposta — está escrita explicitamente em [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Isto é intencional: como recurso de aprendizagem, é mais importante que veja e compreenda cada fase do que que o código esteja minimizado. Quando estiver confortável com o funcionamento das peças, pode passar para Easy RAG para protótipos rápidos ou Advanced RAG para sistemas de produção.

> **💡 Já viu o Easy RAG em ação?** O [módulo Começo Rápido](../00-quick-start/README.md) inclui um exemplo de Perguntas e Respostas com Documentos ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) que usa a abordagem Easy RAG — o LangChain4j trata automaticamente dos embeddings, da pesquisa e da montagem dos prompts. Este módulo dá o passo seguinte ao abrir essa pipeline para que possa ver e controlar cada fase por si próprio.

O diagrama abaixo mostra a pipeline Easy RAG desse exemplo do Começo Rápido. Note como `AiServices` e `EmbeddingStoreContentRetriever` escondem toda a complexidade — carregue um documento, ligue um recuperador e obtenha respostas. A abordagem Native deste módulo abre cada um destes passos ocultos:

<img src="../../../translated_images/pt-PT/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Pipeline Easy RAG - LangChain4j" width="800"/>

*Este diagrama mostra a pipeline Easy RAG de `SimpleReaderDemo.java`. Compare com a abordagem Native usada neste módulo: o Easy RAG esconde o embedding, recuperação e montagem do prompt atrás de `AiServices` e `ContentRetriever` — carrega um documento, liga um recuperador e obtém respostas. A abordagem Native neste módulo abre essa pipeline para que chame cada etapa (embed, pesquisar, montar contexto, gerar) por si, dando-lhe total visibilidade e controlo.*

## Como Funciona

A pipeline RAG deste módulo divide-se em quatro fases que correm em sequência cada vez que um utilizador faz uma pergunta. Primeiro, um documento enviado é **analisado e dividido em pedaços** geríveis. Esses pedaços são depois convertidos em **embeddings vetoriais** e armazenados para que possam ser comparados matematicamente. Quando chega uma consulta, o sistema realiza uma **pesquisa semântica** para encontrar os pedaços mais relevantes e finalmente passa-os como contexto ao LLM para a **geração da resposta**. As secções abaixo explicam cada etapa com o código e diagramas reais. Vamos ver o primeiro passo.

### Processamento de Documentos

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Quando envia um documento, o sistema analisa-o (PDF ou texto simples), anexa metadados, como o nome do ficheiro, e depois divide-o em pedaços — partes mais pequenas que cabem confortavelmente na janela de contexto do modelo. Esses pedaços sobrepõem-se ligeiramente para não perder contexto nas fronteiras.

```java
// Analise o ficheiro carregado e envolva-o num Documento LangChain4j
Document document = Document.from(content, metadata);

// Dividir em fragmentos de 300 tokens com uma sobreposição de 30 tokens
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
O diagrama abaixo mostra como isto funciona visualmente. Note como cada pedaço partilha alguns tokens com os vizinhos — a sobreposição de 30 tokens assegura que nenhum contexto importante se perde nas junções:

<img src="../../../translated_images/pt-PT/document-chunking.a5df1dd1383431ed.webp" alt="Divisão em Pedaços de Documento" width="800"/>

*Este diagrama mostra um documento a ser dividido em pedaços de 300 tokens com 30 tokens de sobreposição, preservando o contexto nas fronteiras dos pedaços.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) e pergunte:  
> - "Como é que o LangChain4j divide documentos em pedaços e por que é que a sobreposição é importante?"  
> - "Qual é o tamanho ótimo dos pedaços para diferentes tipos de documentos e por quê?"  
> - "Como tratar documentos em várias línguas ou com formatações especiais?"

### Criação de Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Cada pedaço é convertido numa representação numérica chamada embedding — essencialmente um sistema que converte significado em números. O modelo de embedding não é "inteligente" como um modelo de chat; não consegue seguir instruções, raciocinar ou responder perguntas. O que pode fazer é mapear texto num espaço matemático onde significados semelhantes ficam próximos — “carro” perto de “automóvel”, “política de reembolso” perto de “devolver o meu dinheiro”. Pense num modelo de chat como uma pessoa com quem pode conversar; um modelo de embedding é um sistema de arquivo ultra eficaz.

O diagrama abaixo visualiza este conceito — o texto entra, saem vetores numéricos, e significados semelhantes produzem vetores próximos:

<img src="../../../translated_images/pt-PT/embedding-model-concept.90760790c336a705.webp" alt="Conceito do Modelo de Embedding" width="800"/>

*Este diagrama mostra como um modelo de embedding converte texto em vetores numéricos, colocando significados semelhantes — como “carro” e “automóvel” — próximos no espaço vetorial.*

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
  
O diagrama de classes abaixo mostra os dois fluxos separados numa pipeline RAG e as classes LangChain4j que os implementam. O **fluxo de ingestão** (corre uma vez na altura do upload) divide o documento, cria embeddings dos pedaços, e armazena-os via `.addAll()`. O **fluxo de consulta** (corre cada vez que o utilizador pergunta) cria o embedding da pergunta, pesquisa na store via `.search()`, e passa o contexto combinado ao modelo de chat. Ambos os fluxos encontram-se na interface partilhada `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/pt-PT/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Classes RAG do LangChain4j" width="800"/>

*Este diagrama mostra os dois fluxos numa pipeline RAG — ingestão e consulta — e como se ligam através de uma EmbeddingStore partilhada.*

Quando os embeddings são armazenados, conteúdos semelhantes agrupam-se naturalmente no espaço vetorial. A visualização abaixo mostra como documentos sobre tópicos relacionados acabam como pontos próximos, o que torna possível a pesquisa semântica:

<img src="../../../translated_images/pt-PT/vector-embeddings.2ef7bdddac79a327.webp" alt="Espaço de Embeddings Vetoriais" width="800"/>

*Esta visualização mostra como documentos relacionados agrupam-se no espaço vetorial 3D, com tópicos como Documentação Técnica, Regras de Negócio e FAQs formando grupos distintos.*

Quando um utilizador pesquisa, o sistema segue quatro passos: embedar os documentos uma vez, embedar a consulta a cada pesquisa, comparar o vetor da consulta contra todos os vetores armazenados usando similaridade cosseno, e devolver os top-K pedaços com melhor pontuação. O diagrama abaixo mostra cada passo e as classes LangChain4j envolvidas:

<img src="../../../translated_images/pt-PT/embedding-search-steps.f54c907b3c5b4332.webp" alt="Passos da Pesquisa por Embedding" width="800"/>

*Este diagrama mostra o processo de pesquisa por embedding em quatro passos: embedar documentos, embedar a consulta, comparar vetores com similaridade cosseno e devolver os resultados top-K.*

### Pesquisa Semântica

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Quando faz uma pergunta, a sua questão também se converte numa embedding. O sistema compara o embedding da sua pergunta contra todos os embeddings dos pedaços do documento. Encontra os pedaços com os significados mais semelhantes — não só correspondência de palavras-chave, mas verdadeira semelhança semântica.

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
  
O diagrama abaixo contrasta a pesquisa semântica com a pesquisa tradicional por palavras-chave. Uma pesquisa por palavra-chave de “veículo” não encontra um pedaço sobre “carros e camiões”, mas a pesquisa semântica percebe que significam o mesmo e devolve-o como uma correspondência com alta pontuação:

<img src="../../../translated_images/pt-PT/semantic-search.6b790f21c86b849d.webp" alt="Pesquisa Semântica" width="800"/>

*Este diagrama compara a pesquisa baseada em palavras-chave com a pesquisa semântica, mostrando como esta última recupera conteúdo conceitualmente relacionado mesmo quando as palavras-chave exatas diferem.*
Por baixo, a similaridade é medida usando similaridade cosseno — basicamente a perguntar "estes dois vetores apontam na mesma direção?" Dois fragmentos podem usar palavras completamente diferentes, mas se significarem a mesma coisa, os seus vetores apontam na mesma direção e a pontuação fica próxima de 1.0:

<img src="../../../translated_images/pt-PT/cosine-similarity.9baeaf3fc3336abb.webp" alt="Similaridade Cosseno" width="800"/>

*Este diagrama ilustra a similaridade cosseno como o ângulo entre vetores de incorporação — vetores mais alinhados obtêm uma pontuação próxima de 1.0, indicando maior similaridade semântica.*

> **🤖 Experimente com o [GitHub Copilot](https://github.com/features/copilot) Chat:** Abra [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) e pergunte:
> - "Como funciona a pesquisa de similaridade com incorporações e o que determina a pontuação?"
> - "Que limiar de similaridade devo usar e como isso afeta os resultados?"
> - "Como lidar com casos onde nenhum documento relevante é encontrado?"

### Geração de Resposta

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Os fragmentos mais relevantes são montados numa prompt estruturada que inclui instruções explícitas, o contexto recuperado e a pergunta do utilizador. O modelo lê esses fragmentos específicos e responde com base nessa informação — só pode usar o que está à sua frente, o que previne fabulações.

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

O diagrama abaixo mostra esta montagem em ação — os fragmentos com pontuação mais alta do passo de pesquisa são inseridos no template da prompt, e o `OpenAiOfficialChatModel` gera uma resposta fundamentada:

<img src="../../../translated_images/pt-PT/context-assembly.7e6dd60c31f95978.webp" alt="Montagem de Contexto" width="800"/>

*Este diagrama mostra como os fragmentos com melhor pontuação são montados numa prompt estruturada, permitindo que o modelo gere uma resposta fundamentada nos seus dados.*

## Executar a Aplicação

**Verificar a implantação:**

Certifique-se de que o ficheiro `.env` existe na pasta raiz com as credenciais Azure (criadas durante o Módulo 01). Execute isto da pasta do módulo (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Deve mostrar AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Iniciar a aplicação:**

> **Nota:** Se já iniciou todas as aplicações usando `./start-all.sh` da pasta raiz (como descrito no Módulo 01), este módulo já está a correr na porta 8081. Pode saltar os comandos de arranque abaixo e ir diretamente para http://localhost:8081.

**Opção 1: Usar o Spring Boot Dashboard (Recomendado para utilizadores VS Code)**

O contentor de desenvolvimento inclui a extensão Spring Boot Dashboard, que fornece uma interface visual para gerir todas as aplicações Spring Boot. Pode encontrá-la na Barra de Atividades do lado esquerdo do VS Code (procure o ícone Spring Boot).

No Spring Boot Dashboard pode:
- Ver todas as aplicações Spring Boot disponíveis no workspace
- Iniciar/parar aplicações com um único clique
- Ver logs das aplicações em tempo real
- Monitorizar o estado das aplicações

Basta clicar no botão de play ao lado de "rag" para iniciar este módulo, ou iniciar todos os módulos de uma vez.

<img src="../../../translated_images/pt-PT/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Esta captura mostra o Spring Boot Dashboard no VS Code, onde pode iniciar, parar e monitorizar aplicações visualmente.*

**Opção 2: Usar scripts shell**

Iniciar todas as aplicações web (módulos 01-04):

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

Ou iniciar apenas este módulo:

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

Ambos os scripts carregam automaticamente as variáveis de ambiente do ficheiro `.env` da raiz e irão construir os JARs se não existirem.

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
.\stop.ps1  # Apenas este módulo
# Ou
cd ..; .\stop-all.ps1  # Todos os módulos
```

## Usar a Aplicação

A aplicação fornece uma interface web para carregamento de documentos e realização de perguntas.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pt-PT/rag-homepage.d90eb5ce1b3caa94.webp" alt="Interface da Aplicação RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura mostra a interface da aplicação RAG onde pode carregar documentos e colocar perguntas.*

### Carregar um Documento

Comece por carregar um documento - ficheiros TXT funcionam melhor para testes. Está disponível um `sample-document.txt` neste diretório que contém informações sobre funcionalidades do LangChain4j, implementação RAG e boas práticas - perfeito para testar o sistema.

O sistema processa o seu documento, divide-o em fragmentos e cria incorporações para cada fragmento. Isto acontece automaticamente ao carregar.

### Colocar Perguntas

Agora faça perguntas específicas sobre o conteúdo do documento. Tente algo factual que esteja claramente indicado no documento. O sistema pesquisa os fragmentos relevantes, inclui-os na prompt e gera a resposta.

### Verificar Referências de Fonte

Note que cada resposta inclui referências com pontuações de similaridade. Estas pontuações (de 0 a 1) mostram quão relevante foi cada fragmento para a sua pergunta. Pontuações mais altas significam melhores correspondências. Isto permite-lhe verificar a resposta relativamente ao material fonte.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pt-PT/rag-query-results.6d69fcec5397f355.webp" alt="Resultados da Consulta RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Esta captura mostra os resultados da consulta com a resposta gerada, referências de fonte e pontuações de relevância para cada fragmento recuperado.*

### Experimente Perguntas

Experimente diferentes tipos de perguntas:
- Factos específicos: "Qual é o tema principal?"
- Comparações: "Qual é a diferença entre X e Y?"
- Resumos: "Resuma os pontos chave sobre Z"

Observe como as pontuações de relevância mudam consoante o quão bem a sua pergunta corresponde ao conteúdo do documento.

## Conceitos-Chave

### Estratégia de Particionamento

Os documentos são divididos em fragmentos de 300 tokens com 30 tokens de sobreposição. Este equilíbrio garante que cada fragmento tem contexto suficiente para ser significativo, mantendo-os pequenos o suficiente para incluir vários fragmentos numa prompt.

### Pontuações de Similaridade

Cada fragmento recuperado vem com uma pontuação de similaridade entre 0 e 1 que indica quão próximo corresponde à pergunta do utilizador. O diagrama abaixo visualiza as faixas de pontuação e como o sistema as usa para filtrar resultados:

<img src="../../../translated_images/pt-PT/similarity-scores.b0716aa911abf7f0.webp" alt="Pontuações de Similaridade" width="800"/>

*Este diagrama mostra faixas de pontuação de 0 a 1, com um limiar mínimo de 0.5 que filtra fragmentos irrelevantes.*

As pontuações variam de 0 a 1:
- 0.7-1.0: Altamente relevante, correspondência exata
- 0.5-0.7: Relevante, bom contexto
- Abaixo de 0.5: Filtrado, demasiado dissemelhante

O sistema só recupera fragmentos acima do limiar mínimo para garantir qualidade.

As incorporações funcionam bem quando os significados agrupam-se claramente, mas têm pontos cegos. O diagrama abaixo mostra os modos comuns de falha — fragmentos grandes demais produzem vetores confusos, fragmentos pequenos demais carecem de contexto, termos ambíguos apontam para múltiplos grupos, e pesquisas por correspondência exata (IDs, números de peça) não funcionam com incorporações:

<img src="../../../translated_images/pt-PT/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Modos de Falha de Incorporações" width="800"/>

*Este diagrama mostra modos comuns de falha nas incorporações: fragmentos muito grandes, fragmentos muito pequenos, termos ambíguos que apontam para clusters múltiplos e pesquisas por correspondência exata como IDs.*

### Armazenamento em Memória

Este módulo usa armazenamento em memória para simplicidade. Ao reiniciar a aplicação, os documentos carregados são perdidos. Sistemas de produção usam bases de dados vetoriais persistentes como Qdrant ou Azure AI Search.

### Gestão da Janela de Contexto

Cada modelo tem uma janela de contexto máxima. Não pode incluir todos os fragmentos de um documento grande. O sistema recupera os N fragmentos mais relevantes (padrão 5) para manter-se dentro dos limites e fornecer contexto suficiente para respostas precisas.

## Quando o RAG Importa

RAG não é sempre a abordagem certa. O guia de decisão abaixo ajuda a determinar quando o RAG adiciona valor versus quando abordagens mais simples — como incluir conteúdo diretamente na prompt ou confiar no conhecimento embutido do modelo — são suficientes:

<img src="../../../translated_images/pt-PT/when-to-use-rag.1016223f6fea26bc.webp" alt="Quando Usar RAG" width="800"/>

*Este diagrama mostra um guia de decisão para quando o RAG acrescenta valor versus quando abordagens mais simples são suficientes.*

## Próximos Passos

**Próximo Módulo:** [04-tools - Agentes de IA com Ferramentas](../04-tools/README.md)

---

**Navegação:** [← Anterior: Módulo 02 - Engenharia de Prompt](../02-prompt-engineering/README.md) | [Voltar ao Início](../README.md) | [Próximo: Módulo 04 - Ferramentas →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos por garantir a precisão, por favor tenha em conta que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->