# Module 03: RAG (情報検索強化型生成)

## 目次

- [ビデオウォークスルー](../../../03-rag)
- [学べること](../../../03-rag)
- [前提条件](../../../03-rag)
- [RAGの理解](../../../03-rag)
  - [このチュートリアルで使うRAGのアプローチは？](../../../03-rag)
- [仕組み](../../../03-rag)
  - [ドキュメント処理](../../../03-rag)
  - [埋め込みの作成](../../../03-rag)
  - [セマンティックサーチ](../../../03-rag)
  - [回答生成](../../../03-rag)
- [アプリケーションの実行](../../../03-rag)
- [アプリケーションの使用方法](../../../03-rag)
  - [ドキュメントのアップロード](../../../03-rag)
  - [質問する](../../../03-rag)
  - [ソース参照の確認](../../../03-rag)
  - [質問で実験する](../../../03-rag)
- [重要な概念](../../../03-rag)
  - [チャンク分割戦略](../../../03-rag)
  - [類似度スコア](../../../03-rag)
  - [インメモリストレージ](../../../03-rag)
  - [コンテキストウィンドウ管理](../../../03-rag)
- [RAGが重要になる時](../../../03-rag)
- [次のステップ](../../../03-rag)

## ビデオウォークスルー

このモジュールを始めるためのライブセッションをご覧ください：[RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## 学べること

これまでのモジュールで、AIとの会話方法と効果的なプロンプトの構築方法を学びました。しかし基本的な制約があります。言語モデルは訓練時に学んだことしか知りません。自社のポリシーやプロジェクトのドキュメント、訓練に含まれていない情報に関する質問には答えられません。

RAG (情報検索強化型生成) はこの問題を解決します。モデルに情報を教え込むのではなく（これはコストが高く現実的ではありません）、ドキュメントを検索する能力を与えます。質問が来るとシステムは関連情報を探しプロンプトに含めます。モデルはその取得されたコンテキストに基づいて回答を生成します。

RAGはモデルに参考図書館を与えるようなものです。質問すると、システムは次のように動きます：

1. **ユーザーの質問** - あなたが質問する  
2. **埋め込み変換** - 質問をベクトルに変換  
3. **ベクトル検索** - 類似したドキュメントチャンクを見つける  
4. **コンテキストの組み立て** - 関連チャンクをプロンプトに追加  
5. **回答** - LLMがそのコンテキストに基づいて回答を生成  

これによりモデルの回答は訓練知識に頼ったりでっち上げたりするのではなく、実際のデータに根ざしたものになります。

## 前提条件

- [Module 00 - Quick Start](../00-quick-start/README.md) を完了している（上記のEasy RAG例のため）  
- [Module 01 - Introduction](../01-introduction/README.md) を完了している（Azure OpenAIリソースがデプロイ済みで、`text-embedding-3-small` 埋め込みモデルを含む）  
- ルートディレクトリにAzureクレデンシャルを含む `.env` ファイルがある（Module 01で `azd up` によって作成）  

> **注意:** Module 01を完了していない場合は、まずそこでのデプロイ手順に従ってください。`azd up` コマンドはこのモジュールで使うGPTチャットモデルと埋め込みモデルの両方をデプロイします。

## RAGの理解

以下の図はコアコンセプトを表しています：モデルの訓練データだけに頼るのではなく、RAGは毎回回答を生成する前に参照できるあなたのドキュメントのライブラリを提供します。

<img src="../../../translated_images/ja/what-is-rag.1f9005d44b07f2d8.webp" alt="RAGとは" width="800"/>

*この図は、標準的なLLM（訓練データから推測）とRAG強化LLM（最初にドキュメントを参照）の違いを示しています。*

以下は全体の流れです。ユーザーの質問は4つの段階を経ます — 埋め込み、ベクトル検索、コンテキスト組み立て、回答生成 — 各段階が前の段階の成果を土台にします：

<img src="../../../translated_images/ja/rag-architecture.ccb53b71a6ce407f.webp" alt="RAGのアーキテクチャ" width="800"/>

*この図はRAGのパイプライン全体を示しており、ユーザークエリが埋め込み、ベクトル検索、コンテキスト組み立て、回答生成へと流れていく様子を表しています。*

このモジュールでは、それぞれの段階を詳細に解説し、実行したり変更したりできるコードを示します。

### このチュートリアルで使うRAGのアプローチは？

LangChain4jにはRAGを実装する3つの方法があり、それぞれ抽象度が異なります。以下の図はそれらを並べて比較しています：

<img src="../../../translated_images/ja/rag-approaches.5b97fdcc626f1447.webp" alt="LangChain4jの3つのRAGアプローチ" width="800"/>

*この図はLangChain4jのEasy、Native、Advancedという3つのRAGアプローチの主要な構成要素と用途を比較しています。*

| アプローチ | 内容 | トレードオフ |
|---|---|---|
| **Easy RAG** | `AiServices`と`ContentRetriever`で全て自動的にワイヤー接続。インターフェイスに注釈を付けリトリーバーを設定すれば、埋め込み、検索、プロンプト組み立てをLangChain4jが裏で処理。 | コードは最小限だが、各ステップの詳細は見えない。 |
| **Native RAG** | 埋め込みモデルの呼び出し、ストアの検索、プロンプト組み立て、回答生成を自分で段階的に呼び出す。 | コードは多くなるが各段階を明示的に見て変更可能。 |
| **Advanced RAG** | `RetrievalAugmentor`フレームワークを使い、クエリ変換器、ルーター、再ランク付け器、コンテンツインジェクターなどをプラグイン可能。 | 柔軟性は最大だが複雑さもかなり増す。 |

**このチュートリアルではNativeアプローチを使用します。** RAGパイプラインの各ステップ — クエリの埋め込み、ベクトルストアの検索、コンテキストの組み立て、回答の生成 — は [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) に明示的に書かれています。これは学習用として意図的で、コードを最小化するよりも各段階を見て理解することが重要だからです。流れを理解したら、Quick prototypingにはEasy RAG、運用システムにはAdvanced RAGへ進むことができます。

> **💡 Easy RAGを既に使ったことがありますか？** [Quick Startモジュール](../00-quick-start/README.md)にはDocument Q&Aの例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)）があり、Easy RAGアプローチを使っています — LangChain4jが埋め込み、検索、プロンプト組み立てを自動処理します。このモジュールはそのパイプラインを分解して各段階を見て制御できるようにしています。

<img src="../../../translated_images/ja/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*この図は`SimpleReaderDemo.java`のEasy RAGパイプラインを示しています。このモジュールで使うNativeアプローチと比較してください：Easy RAGは`AiServices`と`ContentRetriever`の背後に埋め込み、取得、プロンプト組み立てを隠しています — ドキュメントを読み込み、リトリーバーを設定し、回答を得ます。Nativeアプローチは各段階を自分で呼び出して完全に把握・制御可能にします。*

## 仕組み

このモジュールのRAGパイプラインは、ユーザーが質問するたびに順番に実行される4つのステージに分かれます。まず、アップロードされたドキュメントは**解析されチャンクに分割**されます。これらのチャンクは**ベクトル埋め込み**に変換されて保存され、数学的に比較可能になります。クエリが来ると**セマンティックサーチ**で最も関連深いチャンクを探し、最後にそれらをコンテキストとしてLLMに渡して**回答を生成**します。以下の章で実際のコードや図とともに各段階を説明します。まず最初のステップを見ましょう。

### ドキュメント処理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

ドキュメントをアップロードすると、システムはそれを解析（PDFやプレーンテキスト）し、ファイル名などのメタデータを付与してからチャンクに分割します — モデルのコンテキストウィンドウに無理なく収まる小さな単位です。これらのチャンクは少し重複を持たせて境界でコンテキストが欠落しないようにします。

```java
// アップロードされたファイルを解析し、LangChain4jのドキュメントにラップします
Document document = Document.from(content, metadata);

// 300トークンのチャンクに分割し、30トークンの重複を持たせます
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
以下の図はこの動作を視覚的に示しています。隣接するチャンクが30トークン重複しているのがわかります。これにより重要な情報が境界で切れません：

<img src="../../../translated_images/ja/document-chunking.a5df1dd1383431ed.webp" alt="ドキュメントのチャンク分割" width="800"/>

*この図は文書が300トークンのチャンクに分割され、30トークンの重複を持たせてチャンク境界のコンテキストを保持している様子を示しています。*

> **🤖 GitHub Copilot Chatで試してみよう：** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) を開き、以下を尋ねてみてください。  
> - "LangChain4jはどうやってドキュメントをチャンクに分割し、なぜ重複が重要なの？"  
> - "異なるドキュメントタイプに最適なチャンクサイズは？なぜ？"  
> - "複数言語や特殊書式のドキュメントはどう処理する？"

### 埋め込みの作成

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

各チャンクは埋め込みと呼ばれる数値表現に変換されます — これは意味を数字に変換するものです。埋め込みモデルはチャットモデルのように「知能的」に対話したり、指示に従ったり、質問に答えたりはできません。できることは類似の意味のテキストを数学的な空間で近くに配置することです — 例えば「車」と「自動車」や「返金ポリシー」と「お金を返してください」が近くにあるように。チャットモデルを話せる人間とすると、埋め込みモデルは超優秀なファイリングシステムのようなものです。

<img src="../../../translated_images/ja/embedding-model-concept.90760790c336a705.webp" alt="埋め込みモデルの概念" width="800"/>

*この図は、埋め込みモデルがテキストを数値ベクトルに変換し、「車」と「自動車」のような類似する意味のものをベクトル空間で近くに配置する様子を示しています。*

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
  
以下のクラス図はRAGパイプライン内の二つの流れと、それを実装するLangChain4jのクラス群を示しています。  
**インジェストフロー**（アップロード時に1回実行）はドキュメントを分割してチャンクを埋め込み、`.addAll()`で保存します。  
**クエリフロー**（ユーザーの質問ごとに実行）は質問を埋め込み、`.search()`でストアから検索し、マッチしたコンテキストをチャットモデルに渡します。双方は共通の `EmbeddingStore<TextSegment>` インターフェイスを介してつながっています：

<img src="../../../translated_images/ja/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4jのRAGクラス" width="800"/>

*この図はRAGパイプラインの2つの流れ — インジェストとクエリ — と、埋め込みストアを介しての接続を示しています。*

埋め込みが保存されると、類似内容のチャンクは自然とベクトル空間で隣接してクラスターになります。以下の図は関連トピックの文書が近くの点として集まる様子で、これがセマンティックサーチを可能にしています：

<img src="../../../translated_images/ja/vector-embeddings.2ef7bdddac79a327.webp" alt="ベクトル埋め込み空間" width="800"/>

*この図は関連トピック（技術文書、業務規則、FAQなど）が3Dのベクトル空間でそれぞれ別のクラスターを形成する様子を示しています。*

ユーザーが検索すると、システムは以下の4ステップを踏みます：  
ドキュメントは一度埋め込み、検索ごとにクエリを埋め込み、コサイン類似度で全保存ベクトルと比較し、トップKのスコアが高いチャンクを返します。以下の図は各ステップと関連クラスを示しています：

<img src="../../../translated_images/ja/embedding-search-steps.f54c907b3c5b4332.webp" alt="埋め込み検索のステップ" width="800"/>

*この図は、ドキュメント埋め込み、クエリ埋め込み、コサイン類似度でのベクトル比較、トップK結果返却という4ステップの検索処理を示しています。*

### セマンティックサーチ

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

質問をすると、その質問も埋め込みに変換されます。システムは質問の埋め込みとドキュメントチャンクの全埋め込みを比較します。キーワードが一致するだけでなく、意味が似ているチャンクを見つけ出します。

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
  
以下の図はキーワード検索とセマンティックサーチの対比です。  
「vehicle」というキーワード検索では「cars and trucks」というチャンクを見逃しますが、セマンティックサーチはそれが同義であることを理解して高得点で返します：

<img src="../../../translated_images/ja/semantic-search.6b790f21c86b849d.webp" alt="セマンティックサーチ" width="800"/>

*この図はキーワード検索とセマンティックサーチを比較し、セマンティックサーチはキーワードが異なっていても概念的に関連した内容を取得することを示しています。*

内部的には類似度はコサイン類似度を使って測定します — これは「この2つの矢印は同じ方向を向いているか？」を評価するものです。表現が異なっても意味が似ていればベクトルは同じ方向を向いており、スコアは1.0に近づきます：

<img src="../../../translated_images/ja/cosine-similarity.9baeaf3fc3336abb.webp" alt="コサイン類似度" width="800"/>

*この図はベクトル間の角度としてのコサイン類似度を示し、角度が小さいほど（つまりベクトルの方向が近いほど）スコアが1.0に近づき、意味的に類似していることを表しています。*
> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試す：** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) を開いて質問してください：
> - 「埋め込みによる類似度検索はどのように機能し、スコアは何で決まりますか？」
> - 「どの類似度閾値を使うべきで、それは結果にどのように影響しますか？」
> - 「関連するドキュメントが見つからなかった場合はどう対処しますか？」

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最も関連性の高いチャンクが、明確な指示、取得されたコンテキスト、ユーザーの質問を含む構造化されたプロンプトに組み込まれます。モデルはそれらの特定のチャンクのみを読み、そこにある情報に基づいて回答します — モデルが使用できるのは目の前にある情報だけで、幻覚を防ぎます。

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

以下の図はその組み立ての様子を示しています — 検索ステップで得られた最上位スコアのチャンクがプロンプトテンプレートに注入され、`OpenAiOfficialChatModel` が根拠のある回答を生成します：

<img src="../../../translated_images/ja/context-assembly.7e6dd60c31f95978.webp" alt="コンテキスト組み立て" width="800"/>

*この図は、最上位スコアのチャンクがどのように構造化されたプロンプトに組み込まれ、モデルがあなたのデータから根拠のある回答を生成できるかを示しています。*

## アプリケーションの実行

**デプロイの確認：**

ルートディレクトリに Azure の認証情報が入った `.env` ファイルが存在することを確認してください（モジュール01で作成済み）。

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**アプリケーションの起動：**

> **注意：** モジュール01の `./start-all.sh` ですでに全てのアプリを起動済みの場合、このモジュールはすでにポート8081で稼働しています。以下の起動コマンドは省略して http://localhost:8081 に直接アクセスしてください。

**オプション1：Spring Boot ダッシュボードの使用（VS Codeユーザーに推奨）**

開発コンテナには Spring Boot ダッシュボード拡張機能が含まれており、全Spring Boot アプリケーションを管理するための視覚的インターフェースを提供します。VS Code の左側のアクティビティバー（Spring Boot アイコン）にあります。

Spring Boot ダッシュボードからは以下が可能です：
- ワークスペース内の全ての Spring Boot アプリケーションを表示
- クリック一つでアプリの起動/停止
- リアルタイムでログ表示
- アプリケーションの状態監視

"rag" の横の再生ボタンをクリックしてモジュールを起動するか、全モジュールを一括起動してください。

<img src="../../../translated_images/ja/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot ダッシュボード" width="400"/>

*このスクリーンショットは VS Code の Spring Boot ダッシュボードを示しており、視覚的にアプリを起動・停止・監視できます。*

**オプション2：シェルスクリプトの使用**

全Webアプリ（モジュール01-04）を起動：

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

またはこのモジュールだけ起動：

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

どちらのスクリプトもルートの `.env` ファイルから環境変数を自動読み込みし、JARファイルがなければビルドします。

> **注意：** 全モジュールを手動でビルドしてから起動したい場合：
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

ブラウザで http://localhost:8081 にアクセスしてください。

**停止するには：**

**Bash:**
```bash
./stop.sh  # このモジュールのみ
# または
cd .. && ./stop-all.sh  # 全てのモジュール
```

**PowerShell:**
```powershell
.\stop.ps1  # このモジュールのみ
# または
cd ..; .\stop-all.ps1  # すべてのモジュール
```

## アプリケーションの利用方法

このアプリはドキュメントのアップロードと質問用のウェブインターフェースを提供します。

<a href="images/rag-homepage.png"><img src="../../../translated_images/ja/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG アプリケーションインターフェース" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*このスクリーンショットは、ドキュメントをアップロードし質問できるRAGアプリのインターフェースを示しています。*

### ドキュメントのアップロード

まずドキュメントをアップロードしてください — テストにはTXTファイルが最適です。このディレクトリには `sample-document.txt` があり、LangChain4jの機能、RAG実装、ベストプラクティスについて記載されています。システムのテストにぴったりです。

ドキュメントは自動的に処理され、チャンクに分割され、それぞれのチャンクに埋め込みベクトルが作成されます。アップロード時にこれが行われます。

### 質問する

次にドキュメント内容に関する具体的な質問をしてください。明確に文書内に記載された事実的な質問が効果的です。システムは関連チャンクを検索し、プロンプトに含めて回答を生成します。

### ソース参照を確認する

各回答には関連チャンクの類似度スコア付きのソース参照が含まれています。これらのスコア（0〜1）は質問との関連度を示し、高いほどマッチしています。これにより、回答を元の資料と照合できます。

<a href="images/rag-query-results.png"><img src="../../../translated_images/ja/rag-query-results.6d69fcec5397f355.webp" alt="RAG クエリ結果" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*このスクリーンショットは質問結果と生成された回答、ソース参照、取得された各チャンクの関連スコアを示しています。*

### 質問を試す

様々な質問タイプを試してみてください：
- 具体的事実：「主なテーマは何ですか？」
- 比較：「XとYの違いは何ですか？」
- 要約：「Zについての重要ポイントをまとめてください」

質問内容による関連性スコアの変化を観察してください。

## キーコンセプト

### チャンク分割戦略

ドキュメントは300トークンのチャンクに分割され、30トークン重複があります。このバランスにより、各チャンクが意味のある文脈を持ちつつ、複数のチャンクをプロンプトに含められるサイズに抑えています。

### 類似度スコア

取得された各チャンクには、0〜1の類似度スコアが付き、質問との近さを示します。以下の図はスコア範囲とフィルタリング基準の活用を示しています：

<img src="../../../translated_images/ja/similarity-scores.b0716aa911abf7f0.webp" alt="類似度スコア" width="800"/>

*この図は0から1までのスコア範囲を示し、0.5の最小閾値で無関係なチャンクを除外しています。*

スコア範囲：
- 0.7〜1.0：非常に関連性が高く、完全一致に近い
- 0.5〜0.7：関連性があり、良い文脈
- 0.5未満：除外、関連性が低すぎる

システムは最小閾値以上のチャンクのみを取得し品質を確保しています。

埋め込みは意味が明確にクラスタ化されると強力ですが、弱点もあります。以下の図はよくある失敗パターンを示します — 大き過ぎるチャンクはベクトルが曖昧に、小さ過ぎるチャンクは文脈不足、曖昧な用語は複数クラスタを指し、IDや部品番号のような完全一致検索は埋め込みでは機能しません：

<img src="../../../translated_images/ja/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="埋め込みの失敗パターン" width="800"/>

*この図は埋め込みの失敗パターンとして、大きすぎるチャンク、小さすぎるチャンク、複数クラスタに分かれる曖昧な用語、IDの完全一致検索が挙げられます。*

### メモリ内ストレージ

このモジュールは簡易化のためメモリ内ストレージを使用しています。アプリを再起動するとアップロードしたドキュメントは消えます。本番環境では Qdrant や Azure AI Search のような永続的ベクトルDBを使います。

### コンテキストウィンドウ管理

各モデルには最大コンテキストウィンドウがあります。大きなドキュメントの全チャンクを含めることはできません。システムは最も関連性の高い上位Nチャンク（デフォルト5）だけを取得し、制限内で十分な文脈を提供します。

## RAG を使うべき時

RAG が常に最適な方法とは限りません。以下の意思決定ガイドは、RAG が価値を発揮する場合と、単純なプロンプト内の内容利用やモデルの内蔵知識だけで十分な場合を示します：

<img src="../../../translated_images/ja/when-to-use-rag.1016223f6fea26bc.webp" alt="RAGを使うべき時" width="800"/>

*この図は、RAGが価値を提供する場合と、より単純なアプローチで十分な場合の判断ガイドです。*

**RAGを使うべき場合：**
- 独自ドキュメントに関する質問に答えるとき
- 情報が頻繁に変わるとき（方針、価格、仕様など）
- 正確性にソースの明示が必要なとき
- コンテンツが大きすぎて単一プロンプトに収まらないとき
- 検証可能かつ根拠のある回答が必要なとき

**RAGを使わない方が良い場合：**
- モデルがすでに持つ一般知識を使う質問の場合
- リアルタイムデータが必要な場合（RAGはアップロードされたドキュメントに基づくため）
- コンテンツが小さくて直接プロンプトに含められる場合

## 次へのステップ

**次のモジュール：** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**ナビゲーション：** [← 前へ: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [メインへ戻る](../README.md) | [次へ: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「[Co-op Translator](https://github.com/Azure/co-op-translator)」を使用して翻訳されました。正確性の向上に努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文の言語による文書が正式な情報源と見なされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や誤訳についても、当社は一切の責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->