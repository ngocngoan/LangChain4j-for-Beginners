# Module 03: RAG (検索強化生成)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

このモジュールの始め方を解説するライブセッションをご覧ください:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

前のモジュールでは、AIと対話し、効果的にプロンプトを構成する方法を学びました。しかし言語モデルには根本的な制約があります： 言語モデルは学習時に得た知識しか持っていません。会社の方針やプロジェクトのドキュメント、学習していない情報については答えられません。

RAG（検索強化生成）はこの問題を解決します。モデルに情報を教え込む（これは費用も手間もかかる）代わりに、文書を検索できる機能を与えます。質問があったとき、システムは関連する情報を見つけてプロンプトに含めます。モデルはその検索された文脈に基づいて回答します。

RAGをモデルにリファレンスライブラリを与えると考えてください。質問をすると、システムは以下を行います：

1. **ユーザーの質問** - 質問をします
2. **埋め込み処理** - その質問をベクトルに変換します
3. **ベクトル検索** - 類似した文書チャンクを探します
4. **コンテキスト組み立て** - 関連チャンクをプロンプトに追加します
5. **応答生成** - LLMがそのコンテキストに基づいて回答を生成します

これにより、モデルの回答は学習済みの知識に頼るのではなく、実際のデータに基づくものとなります。

## Prerequisites

- [Module 00 - Quick Start](../00-quick-start/README.md) を完了済み（上記の簡単なRAGの例のため）
- [Module 01 - Introduction](../01-introduction/README.md) を完了済み（Azure OpenAIリソースがデプロイされていて、`text-embedding-3-small` 埋め込みモデルが含まれていること）
- ルートディレクトリに `.env` ファイルがあり、Azureの資格情報が設定されていること（Module 01で `azd up` コマンドが作成）

> **注意：** Module 01を完了していない場合は、まずそちらのデプロイ手順に従って下さい。`azd up` コマンドはGPTチャットモデルとこのモジュールで使う埋め込みモデルの両方をデプロイします。

## Understanding RAG

下の図はコアコンセプトを示しています：モデルの学習データだけに頼る代わりに、RAGは文書の参照ライブラリを与え、回答生成前にそれを参照します。

<img src="../../../translated_images/ja/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*この図は標準的なLLM（学習データから推測）とRAG強化LLM（まず文書を参照する）の違いを示しています。*

以下は全体の接続図です。ユーザーの質問は4つの段階を経て処理されます — 埋め込み、ベクトル検索、コンテキスト組み立て、回答生成。前段階を土台に次へ進みます：

<img src="../../../translated_images/ja/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*この図はユーザーの質問が埋め込み、ベクトル検索、コンテキスト組み立て、回答生成を通過するエンドツーエンドのRAGパイプラインを示しています。*

以降のモジュールでは各段階を実際のコードとともに詳細に解説します。

### Which RAG Approach Does This Tutorial Use?

LangChain4jは3種類のRAG実装方法を提供しており、それぞれ抽象度が異なります。下の図は横並びで比較しています：

<img src="../../../translated_images/ja/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*この図はLangChain4jの3つのRAGアプローチ—Easy、Native、Advanced—の主要構成要素と利用タイミングを比較しています。*

| アプローチ | 内容 | トレードオフ |
|---|---|---|
| **Easy RAG** | `AiServices` と `ContentRetriever`を使い、すべてを自動で結線。インターフェースを注釈し、Retrieverを取り付けるだけで、埋め込み、検索、プロンプト組み立てをLangChain4jが裏で処理。 | コードが最小限だが、各ステップの詳細は見えません。 |
| **Native RAG** | 埋め込みモデルを呼び、ストアを検索し、プロンプトを作成し、自分で回答を生成といった各ステップを明示的に記述。 | コードは多くなるが、各段階が明示的に見えて調整可能。 |
| **Advanced RAG** | `RetrievalAugmentor` フレームワークを使い、クエリトランスフォーマー、ルーター、再ランク付け器、コンテンツ注入器をプラグ可能に組み込み、実運用対応のパイプラインを実現。 | 最大限の柔軟性だが、大幅に複雑になる。 |

**本チュートリアルは Native アプローチを使います。** RAGパイプラインの各ステップ（クエリの埋め込み、ベクトルストアの検索、文脈の組み立て、回答生成）は [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) に明示的にコード化されています。学習用リソースとして、コードを最小化するよりも各ステップを見て理解することが重要だからです。仕組みがわかったら、プロトタイプならEasy RAG、本番システムにはAdvanced RAGに進んでください。

> **💡 Easy RAGを既に使ったことがある？** [Quick Startモジュール](../00-quick-start/README.md)には、Easy RAGアプローチを使ったDocument Q&Aの例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)）があります。LangChain4jが埋め込み、検索、プロンプト組み立てを自動で行います。今のモジュールはそのパイプラインを解説し、各段階を自分で制御できるようにします。

<img src="../../../translated_images/ja/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*この図は `SimpleReaderDemo.java` のEasy RAGパイプラインです。本モジュールのNativeアプローチと比較してください：Easy RAGは埋め込み、検索、プロンプト組み立てを `AiServices` と `ContentRetriever` に隠蔽し、ドキュメントを読み込みRetrieverを付与するだけで回答を得られます。Nativeはそのパイプラインを分解し、埋め込み、検索、文脈組み立て、生成を自分で呼び出してコントロール可能にします。*

## How It Works

このモジュールのRAGパイプラインは、ユーザーが質問するたびに順次実行される4つの段階に分かれています。まずアップロードされたドキュメントを**解析・チャンク化**し、管理しやすい小片に分割します。その後、チャンクは**ベクトル埋め込み**に変換され、比較可能な形で保存されます。質問が来るとシステムは**セマンティックサーチ**を実行し、もっとも関連性の高いチャンクを見つけ、最後にそのチャンクをコンテキストとしてLLMに渡し**回答を生成**します。以下で各段階をコードと図で解説します。まず最初のステップから見ましょう。

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

ドキュメントをアップロードすると、システムはそれを解析します（PDFかプレーンテキスト）、ファイル名などのメタデータを付与し、モデルのコンテキストウィンドウの大きさに収まる小さなチャンクに分割します。チャンク同士は少し重なりを持たせて境界での文脈の欠落を防ぎます。

```java
// アップロードされたファイルを解析し、それをLangChain4jドキュメントにラップする
Document document = Document.from(content, metadata);

// 30トークンの重複を持つ300トークンのチャンクに分割する
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下図はこれを視覚的に示しています。チャンクは隣接するものと30トークン重なっていることに注目してください。これにより重要な文脈の切れ目を防止します：

<img src="../../../translated_images/ja/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*この図は文書を300トークンのチャンクに30トークンの重なりを持たせて分割し、チャンク境界での文脈を保持する様子を示しています。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) を開き、以下のように聞いてみてください：
> - 「LangChain4jはどのようにドキュメントをチャンクに分割し、なぜ重なりが重要なの？」
> - 「ドキュメントの種類によって最適なチャンクサイズはどれくらい？理由も教えて」
> - 「多言語ドキュメントや特殊フォーマットの扱い方は？」

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

各チャンクは埋め込みと呼ばれる数値表現に変換されます — 意味を数値化する変換器です。埋め込みモデルはチャットモデルのように「知的」ではなく、指示に従ったり推論したり質問に答えたりはできません。できるのはテキストを数学的空間にマッピングし、似た意味のもの同士を近くに配置することです — 「car」と「automobile」、「refund policy」と「return my money」のように。チャットモデルが人と話す相手なら、埋め込みモデルは優秀なファイリングシステムと考えてください。

<img src="../../../translated_images/ja/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*この図は埋め込みモデルがテキストを数値ベクトルに変換し、「car」と「automobile」のような似た意味を空間内で近くに配置する様子を示しています。*

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

下のクラス図はRAGパイプラインの二つのフローと、それらを実装するLangChain4jクラスを示しています。**取り込みフロー**（アップロード時に1回実行）はドキュメントを分割し、チャンクを埋め込み、`.addAll()` で保存します。**クエリフロー**（ユーザーが問うたときに毎回実行）は質問を埋め込み、`.search()` でストアから検索し、マッチした文脈をチャットモデルに渡します。両フローは `EmbeddingStore<TextSegment>` インターフェースを共有しています：

<img src="../../../translated_images/ja/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*この図はRAGパイプラインの取り込みとクエリの二つのフローと、共有されたEmbeddingStoreを通じた接続を示しています。*

埋め込みが保存されると、類似コンテンツはベクトル空間で自然にクラスターを形成します。下の可視化は関連話題の文書が近くに散らばり、セマンティックサーチを可能にしていることを示しています：

<img src="../../../translated_images/ja/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*この可視化は技術文書、ビジネスルール、FAQなどのトピックごとに関連文書が3Dベクトル空間でまとまっている様子を示しています。*

検索時は4つのステップを辿ります：ドキュメントの埋め込みは1回作成済み。検索ごとにクエリを埋め込み、コサイン類似度で全ベクトルと比較、上位K個のチャンクを返します。下図はそれぞれのステップと関わるLangChain4jクラスを示しています：

<img src="../../../translated_images/ja/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*この図は埋め込み検索の4ステッププロセスを示します：ドキュメントの埋め込み、クエリの埋め込み、コサイン類似度による比較、上位K件の返却。*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

質問すると、その質問自体も埋め込みに変換されます。システムは質問の埋め込みと文書チャンクの埋め込みを比較し、意味的に最も近いチャンクを見つけます。キーワード一致ではなく、実際の意味の類似性です。

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

下図はセマンティックサーチと従来のキーワード検索の対比です。キーワード検索で「vehicle」を探した場合、「cars and trucks」に関するチャンクは見つけられませんが、セマンティックサーチは意味が同じと理解し、高得点で返します：

<img src="../../../translated_images/ja/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*この図はキーワード検索とセマンティックサーチを比較し、キーワードが異なっても概念的に関連する内容をセマンティックサーチが返す様子を示しています。*

内部ではコサイン類似度で類似度を測ります — つまり「この2つの矢印は同じ方向を向いているか？」を計ります。違う言葉を使ったチャンクでも意味が同じならベクトルは同じ方向を向くので、スコアはほぼ1.0になります：

<img src="../../../translated_images/ja/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*この図は埋め込みベクトル間の角度としてのコサイン類似度を示しています — より整列したベクトルは1.0に近いスコアを持ち、より高い意味的類似性を示します。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試してみましょう：** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) を開いて以下を尋ねてください：
> - 「類似度検索は埋め込みでどのように機能し、スコアは何で決まりますか？」
> - 「どの類似度閾値を使うべきで、それは結果にどう影響しますか？」
> - 「関連文書が見つからない場合はどう対処しますか？」

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最も関連性の高いチャンクは、明示的な指示、取得されたコンテキスト、ユーザーの質問を含む構造化されたプロンプトに組み込まれます。モデルはこれら特定のチャンクを読み、それに基づいて回答します — 前にある情報のみを使用可能で、幻覚を防ぎます。

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

下の図はこのアセンブリの実例を示しています — 検索段階でトップスコアのチャンクがプロンプトテンプレートに挿入され、`OpenAiOfficialChatModel`が根拠のある回答を生成します：

<img src="../../../translated_images/ja/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*この図は、トップスコアのチャンクが構造化されたプロンプトにまとめられ、モデルがデータから根拠のある回答を生成できる様子を示しています。*

## アプリケーションの実行

**デプロイを確認する：**

ルートディレクトリにAzure資格情報が入った `.env` ファイルが存在することを確認してください（モジュール01で作成）。

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動：**

> **注意：** すでにモジュール01の `./start-all.sh` を使ってすべてのアプリケーションを起動している場合、このモジュールはポート8081で既に稼働中です。以下の起動コマンドはスキップして http://localhost:8081 に直接アクセスできます。

**オプション1：Spring Boot ダッシュボードの利用（VS Codeユーザー推奨）**

開発用コンテナにはSpring Boot Dashboard拡張機能が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Codeの左側のアクティビティバーにあるSpring Bootアイコンで見つけられます。

Spring Bootダッシュボードでは：
- ワークスペース内のすべてのSpring Bootアプリケーションを確認可能
- ワンクリックでアプリの開始/停止
- アプリケーションログのリアルタイム表示
- アプリ状態のモニタリング

単に「rag」の横の再生ボタンをクリックすればこのモジュールを起動できますし、すべてのモジュールを一括起動も可能です。

<img src="../../../translated_images/ja/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*このスクリーンショットはVS CodeのSpring Boot Dashboardで、ここからアプリケーションを視覚的に開始・停止・監視できます。*

**オプション2：シェルスクリプトの利用**

すべてのWebアプリケーション（モジュール01〜04）を起動：

**Bash:**
```bash
cd ..  # ルートディレクトリから
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # ルートディレクトリから
.\start-all.ps1
```

または、このモジュールだけを起動：

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

両方のスクリプトはルートの `.env` ファイルから環境変数を自動読み込みし、JARファイルがなければビルドも自動で行います。

> **注意：** 起動前にすべてのモジュールを手動でビルドしたい場合：
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

ブラウザで http://localhost:8081 を開いてください。

**停止するには：**

**Bash:**
```bash
./stop.sh  # このモジュールのみ
# または
cd .. && ./stop-all.sh  # すべてのモジュール
```

**PowerShell:**
```powershell
.\stop.ps1  # このモジュールのみ
# または
cd ..; .\stop-all.ps1  # すべてのモジュール
```

## アプリケーションの使い方

このアプリケーションでは文書のアップロードと質問のためのウェブインターフェースが提供されています。

<a href="images/rag-homepage.png"><img src="../../../translated_images/ja/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*このスクリーンショットはRAGアプリケーションのインターフェースで、ここで文書をアップロードして質問を行います。*

### 文書のアップロード

まず文書をアップロードしてください — テストにはTXTファイルが最適です。このディレクトリに含まれる `sample-document.txt` はLangChain4jの特徴、RAGの実装、ベストプラクティスに関する情報が入っており、システムのテストに最適です。

システムはアップロードされた文書を処理し、チャンクに分割して各チャンクの埋め込みを作成します。これはアップロード時に自動で行われます。

### 質問する

文書内容に関して具体的な質問をしてください。文書に明確に記載されている事実を尋ねると良いでしょう。システムは関連チャンクを検索し、プロンプトに含めたうえで回答を生成します。

### 出典の参照を確認する

各回答には類似度スコア付きの出典参照が含まれています。このスコア（0〜1）は各チャンクが質問にどれだけ関連しているかを示します。スコアが高いほどマッチ度が良く、元の資料と回答を検証するのに役立ちます。

<a href="images/rag-query-results.png"><img src="../../../translated_images/ja/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*このスクリーンショットは、生成された回答、出典参照、取得された各チャンクの関連度スコア付きのクエリ結果を示しています。*

### 質問を試す

さまざまなタイプの質問を試しましょう：
- 具体的な事実：「主なトピックは何ですか？」
- 比較：「XとYの違いは何ですか？」
- 要約：「Zに関する重要な点を要約してください」

質問が文書内容とどの程度マッチするかで関連度スコアがどのように変化するかを観察してください。

## 主要な概念

### チャンク分割戦略

文書は300トークンのチャンクに分割され、30トークンの重なりがあります。このバランスで、各チャンクに十分な文脈が保たれつつ、複数チャンクをプロンプトに含められるサイズを維持しています。

### 類似度スコア

取得された各チャンクには0～1の類似度スコアが付き、ユーザーの質問とのマッチ度を示します。以下の図はスコア帯域とシステムによるフィルタリングの使い方を視覚化しています：

<img src="../../../translated_images/ja/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*この図は0から1までのスコア範囲を示し、0.5の最小閾値が無関係なチャンクを除外します。*

スコアの範囲：
- 0.7-1.0: 非常に関連性が高い、完全な一致
- 0.5-0.7: 関連性あり、良好な文脈
- 0.5未満: 除外、関連性が低すぎる

システムは品質を確保するため最小閾値以上のチャンクのみを取得します。

埋め込みは意味が明確にクラスタリングされる場合に優れていますが、盲点もあります。以下の図はよくある失敗モードを示しています — チャンクが大きすぎるとベクトルが不鮮明になり、小さすぎると文脈が不足し、曖昧な用語は複数のクラスタに指示し、IDや部品番号のような完全一致検索は埋め込みでは機能しません：

<img src="../../../translated_images/ja/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*この図は埋め込みの典型的な失敗例を示します：チャンク大きすぎ、小さすぎ、複数クラスタに指す曖昧な用語、IDなどの完全一致検索の非対応。*

### インメモリストレージ

このモジュールは簡単のためインメモリストレージを使用しています。アプリケーションを再起動するとアップロードした文書は消えます。本番環境ではQdrantやAzure AI Searchのような永続的ベクターデータベースを使用します。

### コンテキストウィンドウ管理

各モデルには最大コンテキストウィンドウがあります。大きな文書のすべてのチャンクを含めることはできません。システムは制限内かつ正確な回答のため、最も関連する上位Nチャンク（デフォルト5）を取得します。

## RAGが意味を持つとき

RAGは常に最適な手法ではありません。以下の意思決定ガイドは、RAGが効果を発揮する場合と、プロンプトに直接内容を含めるかモデルの知識に頼るだけで十分な場合を判断する助けになります：

<img src="../../../translated_images/ja/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*この図はRAGが価値を提供する場合と、より単純な方法で十分な場合の意思決定ガイドを示しています。*

**RAGを使うべき場合：**
- 独自の文書に関する質問に答えるとき
- 情報が頻繁に変わる（方針、価格、仕様）
- 正確さにソースの帰属が必要なとき
- コンテンツが単一のプロンプトに収まらないほど大きいとき
- 検証可能で根拠のある回答が必要なとき

**RAGを使わないほうがよい場合：**
- モデルがすでに持っている一般知識が必要な質問の場合
- リアルタイムデータが必要な場合（RAGはアップロード文書に依存）
- プロンプトに直接含められる程度にコンテンツが小さい場合

## 次のステップ

**次のモジュール：** [04-tools - Toolsを使ったAIエージェント](../04-tools/README.md)

---

**ナビゲーション：** [← 前へ: モジュール02 - プロンプトエンジニアリング](../02-prompt-engineering/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類はAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されました。正確性を期しておりますが、自動翻訳には誤りや不正確な表現が含まれる可能性があります。原文の原語版を公式の情報源と見なしてください。重要な情報については、専門の翻訳者による人力翻訳をお勧めします。本翻訳の利用により生じた誤解や誤解釈について、当方は一切の責任を負いません。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->