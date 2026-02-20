# Module 03: RAG (リトリーバル強化生成)

## 目次

- [学習内容](../../../03-rag)
- [RAGの理解](../../../03-rag)
- [前提条件](../../../03-rag)
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
  - [質問の実験](../../../03-rag)
- [重要な概念](../../../03-rag)
  - [チャンク戦略](../../../03-rag)
  - [類似度スコア](../../../03-rag)
  - [インメモリストレージ](../../../03-rag)
  - [コンテキストウィンドウ管理](../../../03-rag)
- [RAGが有効な場合](../../../03-rag)
- [次のステップ](../../../03-rag)

## 学習内容

前のモジュールでは、AIと会話し、効果的にプロンプトを構成する方法を学びました。しかし、根本的な制約があります。言語モデルはトレーニング中に学習したことしか知りません。会社の方針やプロジェクトの文書、トレーニングされていない情報については答えられません。

RAG（リトリーバル強化生成）はこの問題を解決します。モデルにあなたの情報を教え込もうとする代わりに（それはコストがかかり非現実的です）、ドキュメントを検索する能力を与えます。質問があった際に、システムは関連情報を見つけてプロンプトに含めます。その取得したコンテキストに基づいてモデルが回答します。

RAGはモデルに参考図書館を与えるようなものです。質問するとシステムは：

1. **ユーザーの質問** - 質問を入力
2. **埋め込み変換** - 質問をベクトルに変換
3. **ベクトル検索** - 類似するドキュメントのチャンクを検索
4. **コンテキスト組み立て** - 関連チャンクをプロンプトに追加
5. **応答生成** - コンテキストに基づきLLMが回答生成

これによりモデルの回答はトレーニング知識や創作ではなく、実際のデータに基づきます。

## RAGの理解

以下の図はコアコンセプトを示します：モデルのトレーニングデータだけに依存せず、RAGは回答生成の前に参照するドキュメントの図書館を与えます。

<img src="../../../translated_images/ja/what-is-rag.1f9005d44b07f2d8.webp" alt="RAGとは何か" width="800"/>

ユーザーの質問は埋め込み、ベクトル検索、コンテキスト組み立て、回答生成という四段階を経て処理され、それぞれが前の段階に基づいて構築されます：

<img src="../../../translated_images/ja/rag-architecture.ccb53b71a6ce407f.webp" alt="RAGアーキテクチャ" width="800"/>

このモジュールの残りでは各段階を詳細に解説し、実行・修正可能なコードを紹介します。

## 前提条件

- モジュール01の完了（Azure OpenAI リソースのデプロイ済み）
- ルートディレクトリにAzure認証情報を含む `.env` ファイル（モジュール01の `azd up` 実行により作成）

> **Note:** モジュール01を完了していない場合は、まずそちらのデプロイ手順に従ってください。

## 仕組み

### ドキュメント処理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

ドキュメントをアップロードすると、システムはPDFやプレーンテキストを解析し、ファイル名などのメタデータを付与したうえで、チャンク（モデルのコンテキストウィンドウに収まる小さな断片）に分割します。チャンクは隣接する部分とわずかに重複しており、境界で文脈を失わないようにしています。

```java
// アップロードされたファイルを解析し、LangChain4jのドキュメントにラップします
Document document = Document.from(content, metadata);

// 30トークンの重複を持つ300トークンのチャンクに分割します
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

以下の図は処理の仕組みを視覚的に示します。各チャンクは隣接するチャンクと30トークン重複しており、重要な文脈が途切れません：

<img src="../../../translated_images/ja/document-chunking.a5df1dd1383431ed.webp" alt="ドキュメントのチャンク分割" width="800"/>

> **🤖 GitHub Copilotで試す:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) を開き、以下を質問してみてください：
> - "LangChain4jはドキュメントをどのようにチャンクに分割し、重複がなぜ重要か？"
> - "ドキュメントの種類別の最適なチャンクサイズは？理由は？"
> - "多言語や特殊フォーマットのドキュメントはどう扱う？"

### 埋め込みの作成

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

各チャンクは埋め込みという数値的表現に変換されます。埋め込みはテキストの意味を捉えた数学的なフィンガープリントのようなものです。意味が似たテキストは類似した埋め込みを生成します。

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

以下のクラス図はLangChain4jのコンポーネントの連携を示します。`OpenAiOfficialEmbeddingModel`がテキストをベクトルに変換し、`InMemoryEmbeddingStore`は元の`TextSegment`データと共にベクトルを保持。`EmbeddingSearchRequest`で`maxResults`や`minScore`などの検索パラメーターを制御します：

<img src="../../../translated_images/ja/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4jのRAGクラス" width="800"/>

埋め込みを保存すると、類似する内容はベクトル空間内で自然にクラスタリングされます。以下の可視化は関連トピックの文書が近接ポイントに配置される様子を示し、セマンティックサーチを可能にします：

<img src="../../../translated_images/ja/vector-embeddings.2ef7bdddac79a327.webp" alt="ベクトル埋め込み空間" width="800"/>

### セマンティックサーチ

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

質問すると、その質問も埋め込みに変換されます。システムは質問の埋め込みと全ドキュメントチャンクの埋め込みを比較し、意味的に最も類似するチャンクを見つけます。単なるキーワード検索ではなく、実際の意味的類似性に基づきます。

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

以下の図はセマンティックサーチと従来のキーワード検索の違いを示します。「vehicle」でのキーワード検索は「cars and trucks」チャンクを見逃しますが、セマンティックサーチは同義と理解し高評価で返します：

<img src="../../../translated_images/ja/semantic-search.6b790f21c86b849d.webp" alt="セマンティックサーチ" width="800"/>

> **🤖 GitHub Copilotで試す:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) を開き、以下を質問してみてください：
> - "埋め込みにおける類似度検索はどのように機能し、スコアはどう決まる？"
> - "どの類似度閾値を使うべきで、結果にどう影響する？"
> - "関連文書が見つからなかった場合の対応は？"

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最も関連性の高いチャンクを構造化されたプロンプトに組み込みます。プロンプトには明示的な指示、取得したコンテキスト、ユーザーの質問が含まれます。モデルはこれらのチャンクの内容を参照し回答を生成します。つまり与えられた情報のみを使用し、虚偽生成（ハルシネーション）を防ぎます。

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

以下の図は組み立ての様子を示しています。検索で上位のチャンクがプロンプトテンプレートに挿入され、`OpenAiOfficialChatModel`が確かな回答を生成：

<img src="../../../translated_images/ja/context-assembly.7e6dd60c31f95978.webp" alt="コンテキスト組み立て" width="800"/>

## アプリケーションの実行

**デプロイ確認：**

Azure認証情報を含む `.env` ファイルがルートに存在することを確認（モジュール01実行時に作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリ起動：**

> **注意:** 既にモジュール01の `./start-all.sh` で全アプリケーションを起動済みなら、このモジュールはポート8081で実行中です。以下の起動コマンドは省略し http://localhost:8081 にアクセスできます。

**オプション1：Spring Boot Dashboard の利用（VS Codeユーザー向け推奨）**

開発コンテナにはSpring Boot Dashboard拡張が含まれており、全Spring BootアプリをGUIで管理できます。VS Codeの左端アクティビティーバーのSpring Bootアイコンをクリックしてください。

Dashboardからは：
- ワークスペース内の全Spring Bootアプリを一覧表示
- ワンクリックで起動・停止
- リアルタイムログ閲覧
- アプリ状況監視

「rag」横の再生ボタンをクリックするとこのモジュール単独が起動。全モジュールをまとめて起動も可。

<img src="../../../translated_images/ja/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot ダッシュボード" width="400"/>

**オプション2：シェルスクリプト利用**

全ウェブアプリ (モジュール01〜04) 起動：

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

またはこのモジュールのみ起動：

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

両スクリプトはルートの `.env` から環境変数を自動読み込みし、JARがなければビルドします。

> **備考:** 起動前に手動で全モジュールをビルドする場合:
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

ブラウザで http://localhost:8081 を開きます。

**停止：**

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

## アプリケーションの使用方法

アプリはドキュメントアップロードと質問用のウェブインターフェースを提供します。

<a href="images/rag-homepage.png"><img src="../../../translated_images/ja/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAGアプリケーションインターフェース" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAGアプリインターフェース - ドキュメントをアップロードし質問可能*

### ドキュメントのアップロード

まずドキュメントをアップロードします。テストにはTXTファイルが適しています。このディレクトリにはLangChain4jの特徴、RAGの実装やベストプラクティスについての情報を含む `sample-document.txt` があり、システムの試験に最適です。

ドキュメントが処理されチャンクに分割され、それぞれに埋め込みが自動生成されます。

### 質問する

ドキュメント内容について具体的な質問をします。文書に明確に記述された事実的な質問が効果的です。システムは関連チャンクを検索しプロンプトに組み込み、回答を生成します。

### ソース参照の確認

各回答に類似度スコア付きのソース参照が表示されます。0から1までのスコアは各チャンクの質問への関連度を示し、高いほど良い一致です。回答を元資料と照合可能です。

<a href="images/rag-query-results.png"><img src="../../../translated_images/ja/rag-query-results.6d69fcec5397f355.webp" alt="RAGクエリ結果" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*回答とソース参照、関連度スコアを示すクエリ結果*

### 質問の実験

様々な種類の質問を試してみましょう：
- 具体的事実：「主なトピックは何か？」
- 比較：「XとYの違いは？」
- 要約：「Zの重要ポイントをまとめて」

質問のマッチ度に応じて関連度スコアが変化する様子を観察してみてください。

## 重要な概念

### チャンク戦略

ドキュメントは300トークンチャンクに分割され、30トークン重複します。このバランスにより、チャンクは意味を保つに十分な文脈を保持しつつ、複数チャンクをプロンプトに含めやすいサイズに抑えられます。

### 類似度スコア

取得したチャンクにはすべて0〜1の類似度スコアが付与され、ユーザーの質問への一致度を示します。以下の図はスコア域とシステムによるフィルタリング利用例を示しています：

<img src="../../../translated_images/ja/similarity-scores.b0716aa911abf7f0.webp" alt="類似度スコア" width="800"/>

スコア範囲：
- 0.7〜1.0：非常に関連性高く完全一致
- 0.5〜0.7：関連性あり良好な文脈
- 0.5未満：除外、類似性不足

システムは最低閾値以上のチャンクのみを取り出し品質を担保します。

### インメモリストレージ

このモジュールでは簡便さのためインメモリストレージを使用します。アプリ再起動でアップロード済ドキュメントは失われます。実運用ではQdrantやAzure AI Searchなど永続ベクトルDBを利用します。

### コンテキストウィンドウ管理

各モデルには最大コンテキストウィンドウがあり、大規模ドキュメントすべてのチャンクを含められません。システムは上位N件（デフォルト5）の最も関連するチャンクのみを取得し、制限内で正確な回答に十分な文脈を提供します。

## RAGが有効な場合

RAGは常に最良のアプローチではありません。以下の意思決定ガイドは、RAGを使うべきか、より単純にプロンプトに直接内容を含めるか、あるいはモデルの内蔵知識に依存すべきかを判断する助けとなります：

<img src="../../../translated_images/ja/when-to-use-rag.1016223f6fea26bc.webp" alt="RAGを使うべき時" width="800"/>

**RAGを使うべき場合：**
- 専有文書に関する質問への回答
- 情報が頻繁に変更される（ポリシー、価格、仕様）
- 正確性には情報源の明示が必要
- コンテンツが大きすぎて1つのプロンプトに収まらない
- 検証可能で根拠のある回答が必要

**次のような場合はRAGを使用しないでください：**
- モデルがすでに持っている一般知識を問う質問
- リアルタイムのデータが必要な場合（RAGはアップロードされた文書で動作）
- コンテンツが小さく、プロンプトに直接含められる場合

## 次のステップ

**次のモジュール：** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**ナビゲーション：** [← 前へ：モジュール 02 - プロンプトエンジニアリング](../02-prompt-engineering/README.md) | [メインへ戻る](../README.md) | [次へ：モジュール 04 - ツール →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類は、AI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性の確保に努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご了承ください。原文の言語による文書が正式な情報源とされます。重要な情報については、専門の人間による翻訳をご利用いただくことを推奨します。本翻訳の利用により生じた誤解や誤訳について、当方は一切責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->