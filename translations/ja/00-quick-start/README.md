# モジュール 00: クイックスタート

## 目次

- [はじめに](../../../00-quick-start)
- [LangChain4jとは？](../../../00-quick-start)
- [LangChain4jの依存関係](../../../00-quick-start)
- [前提条件](../../../00-quick-start)
- [セットアップ](../../../00-quick-start)
  - [1. GitHubトークンを取得する](../../../00-quick-start)
  - [2. トークンを設定する](../../../00-quick-start)
- [例を実行する](../../../00-quick-start)
  - [1. 基本的なチャット](../../../00-quick-start)
  - [2. プロンプトパターン](../../../00-quick-start)
  - [3. 関数呼び出し](../../../00-quick-start)
  - [4. ドキュメントQ&A（Easy RAG）](../../../00-quick-start)
  - [5. 責任あるAI](../../../00-quick-start)
- [各例で何を学べるか](../../../00-quick-start)
- [次のステップ](../../../00-quick-start)
- [トラブルシューティング](../../../00-quick-start)

## はじめに

このクイックスタートは、LangChain4jをできるだけ早く始められるように設計されています。LangChain4jとGitHubモデルを使ったAIアプリケーション構築の基本をカバーします。次のモジュールではAzure OpenAIとGPT-5.2に切り替え、それぞれの概念をより深く掘り下げます。

## LangChain4jとは？

LangChain4jはJavaライブラリで、AI搭載アプリケーションの構築を簡素化します。HTTPクライアントやJSON解析を扱う代わりに、クリーンなJava APIを使って作業します。

LangChainの「チェーン」は複数のコンポーネントを連結することを指します。プロンプトからモデル、モデルからパーサーへ、または複数のAI呼び出しを連結して、一つの出力が次の入力に繋がるようにします。このクイックスタートでは、より複雑なチェーンを探索する前に基本に焦点を当てます。

<img src="../../../translated_images/ja/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4jのチェーンコンポーネント – ブロックを組み合わせて強力なAIワークフローを構築*

主に以下の三つのコアコンポーネントを使用します：

**ChatModel** - AIモデルとの対話用インターフェース。`model.chat("prompt")`を呼び出して応答文字列を得ます。OpenAI互換のエンドポイントで動作する`OpenAiOfficialChatModel`を使用します。

**AiServices** - 型安全なAIサービスインターフェースを作成します。メソッドを定義し、`@Tool`で注釈を付けると、LangChain4jが全体のオーケストレーションを処理します。AIは必要に応じて自動的にJavaメソッドを呼び出します。

**MessageWindowChatMemory** - 会話履歴を維持します。これがないと各リクエストは独立していますが、これがあるとAIは前のメッセージを記憶し、複数のターンにわたるコンテキストを保ちます。

<img src="../../../translated_images/ja/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4jアーキテクチャ – コアコンポーネントが協働しAIアプリケーションに力を与える*

## LangChain4jの依存関係

このクイックスタートでは、[`pom.xml`](../../../00-quick-start/pom.xml)の３つのMaven依存を利用します。

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

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official`モジュールは、OpenAI互換APIに接続する`OpenAiOfficialChatModel`クラスを提供します。GitHub Modelsは同じAPI形式を使っているため、特別なアダプターは不要で、ベースURLを`https://models.github.ai/inference`に指定するだけです。

`langchain4j-easy-rag`モジュールは自動的なドキュメント分割、埋め込み、および検索機能を提供し、手動設定なしでRAGアプリケーションを構築できます。

## 前提条件

**Dev Containerを使う場合？** JavaとMavenはすでにインストールされています。必要なのはGitHubパーソナルアクセストークンのみです。

**ローカル開発：**
- Java 21以上、Maven 3.9以上
- GitHubパーソナルアクセストークン（下記手順参照）

> **注意:** このモジュールはGitHub Modelsの`gpt-4.1-nano`を使用します。コード内のモデル名は変更しないでください。GitHubの利用可能モデルに合わせて設定されています。

## セットアップ

### 1. GitHubトークンを取得する

1. [GitHub設定 → パーソナルアクセストークン](https://github.com/settings/personal-access-tokens)に移動
2. 「新しいトークンを生成」をクリック
3. 説明的な名前を設定（例：「LangChain4j Demo」）
4. 有効期限を設定（7日推奨）
5. 「アカウント権限」から「Models」を見つけて「読み取り専用」に設定
6. 「トークンを生成」をクリック
7. トークンをコピーし保存してください—再表示されません

### 2. トークンを設定する

**オプション1: VS Codeを使用（推奨）**

VS Codeを使う場合、プロジェクトルートの`.env`ファイルにトークンを追加します：

`.env`ファイルが存在しない場合は、`.env.example`をコピーして`.env`として保存するか、新規作成してください。

**例 `.env` ファイル:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env にて
GITHUB_TOKEN=your_token_here
```

それから、エクスプローラーで任意のデモファイル（例: `BasicChatDemo.java`）を右クリックして**「Run Java」**を選択するか、実行とデバッグパネルの起動構成を使います。

**オプション2: ターミナルを使用**

環境変数としてトークンを設定します：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 例を実行する

**VS Codeを使う場合：** エクスプローラーで任意のデモファイルを右クリックし、**「Run Java」**を選択するか、実行とデバッグパネルの起動構成を使ってください（事前にトークンを`.env`ファイルに設定していることを確認してください）。

**Mavenを使う場合：** コマンドラインから次のように実行できます：

### 1. 基本的なチャット

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. プロンプトパターン

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ゼロショット、数ショット、連鎖思考、ロールベースのプロンプトを示します。

### 3. 関数呼び出し

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AIは必要に応じて自動的にあなたのJavaメソッドを呼び出します。

### 4. ドキュメントQ&A（Easy RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

自動埋め込みと検索を用いたEasy RAGでドキュメントへの質問が可能です。

### 5. 責任あるAI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AIセーフティフィルターが有害な内容をブロックする様子を確認できます。

## 各例で何を学べるか

**基本的なチャット** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ここから始めて、LangChain4jの最も基本的な部分を見てみましょう。`OpenAiOfficialChatModel`を作成し、`.chat()`でプロンプトを送り、応答を得る方法を示します。これはモデル初期化の基礎を示し、エンドポイントやAPIキーのカスタム設定方法を学べます。このパターンを理解すれば他のすべての学習に応用できます。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す：** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)を開いて質問してください：
> - 「このコードでGitHub ModelsからAzure OpenAIに切り替えるにはどうすれば良いですか？」
> - 「OpenAiOfficialChatModel.builder()で設定できる他のパラメータは何ですか？」
> - 「応答完了を待たずにストリーミングレスポンスを追加するにはどうしますか？」

**プロンプトエンジニアリング** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

モデルとの会話方法が分かったら、次は何を話すかを探ります。このデモでは同じモデル設定で5つの異なるプロンプトパターンを示します。ゼロショットプロンプト（直接指示）、少数ショットプロンプト（例から学習）、連鎖思考プロンプト（推論過程の開示）、ロールベースプロンプト（文脈設定）などです。リクエストの枠組み次第で同じモデルが劇的に異なる結果を返すことが分かります。

また、変数付きで再利用可能なプロンプトを作成する強力な方法としてプロンプトテンプレートも示します。
下記例ではLangChain4jの`PromptTemplate`を使って変数を埋めています。AIは指定された目的地とアクティビティに基づいて答えます。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す：** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)を開いて質問してください：
> - 「ゼロショットと少数ショットプロンプトの違いと、それぞれを使うべき場面は？」
> - 「temperatureパラメータはモデルの応答にどう影響しますか？」
> - 「本番環境でのプロンプトインジェクション攻撃を防ぐ技術は何ですか？」
> - 「共通パターン用の再利用可能なPromptTemplateオブジェクトをどう作成しますか？」

**ツール統合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ここでLangChain4jの力を実感できます。`AiServices`を使い、Javaメソッドを呼び出せるAIアシスタントを作成します。メソッドに`@Tool("説明")`を付けるだけで、LangChain4jがすべて処理し、AIはユーザーの要望に応じて適切なツールを呼び出します。これは関数呼び出しの技術で、質問に答えるだけでなくアクションを実行するAI構築の重要技法です。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す：** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)を開いて質問してください：
> - 「@Tool 注釈はどう機能し、LangChain4jはそれを内部でどう扱いますか？」
> - 「AIは複数のツールを順番に呼び出して複雑な問題を解決できますか？」
> - 「ツールが例外を投げた時はどうすれば良いですか？」
> - 「この計算機例の代わりに実際のAPIを統合するにはどうしますか？」

**ドキュメントQ&A（Easy RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ここではLangChain4jの「Easy RAG」アプローチを使ったRAG（検索強化生成）を紹介します。ドキュメントを読み込み、自動分割・埋め込みしてメモリ内ストアに保存し、検索時に関連チャンクをAIに供給します。AIは一般知識ではなくあなたのドキュメントに基づいて回答します。

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す：** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)を開いて質問してください：
> - 「RAGはモデルの訓練データを使うのと比べてAIの幻覚をどう防ぎますか？」
> - 「この簡単な方法とカスタムRAGパイプラインの違いは何ですか？」
> - 「複数ドキュメントや大規模ナレッジベースでのスケールアップはどうしますか？」

**責任あるAI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

多層防御でAIの安全性を構築します。このデモでは二段階の保護を示します：

**パート1: LangChain4jの入力ガードレール** - LLMに到達する前に危険なプロンプトをブロックします。禁止キーワードやパターンをチェックする独自ガードレールを作成可能。コード内で動作するため、高速かつ無料です。

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

**パート2: プロバイダーの安全フィルター** - GitHub Modelsに組み込まれたフィルターで、ガードレールをすり抜ける内容も検出します。重大な違反にはハードブロック（HTTP 400エラー）、軽度のものはAIが丁寧に拒否します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す：** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)を開いて質問してください：
> - 「InputGuardrailとは何ですか？どうやって自分で作るのですか？」
> - 「ハードブロックとソフト拒否の違いは何ですか？」
> - 「なぜガードレールとプロバイダーフィルターの両方を使うのですか？」

## 次のステップ

**次のモジュール:** [01-introduction - LangChain4jを始める](../01-introduction/README.md)

---

**ナビゲーション:** [← メインに戻る](../README.md) | [次へ: モジュール 01 - はじめに →](../01-introduction/README.md)

---

## トラブルシューティング

### 初回のMavenビルド

**問題:** 初めての`mvn clean compile`や`mvn package`が非常に時間がかかる（10-15分）

**原因:** Mavenが初回ビルド時にすべての依存関係（Spring Boot、LangChain4jライブラリ、Azure SDKなど）をダウンロードするため

**解決策:** 正常な挙動です。次回以降は依存関係がローカルにキャッシュされるため高速になります。ダウンロード時間はネットワーク速度に依存します。

### PowerShellでのMavenコマンド構文

**問題:** Mavenコマンドが`Unknown lifecycle phase ".mainClass=..."`エラーで失敗する
**原因**: PowerShell は `=` を変数代入演算子として解釈し、Maven のプロパティ構文を壊してしまう

**解決策**: Maven コマンドの前に停止解析演算子 `--%` を使う：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 演算子は PowerShell に対して、残りのすべての引数を解釈せずに文字通り Maven に渡すよう指示する。

### Windows PowerShell の絵文字表示

**問題**: PowerShell で AI の応答が絵文字の代わりに文字化け（例：`????` や `â??`）として表示される

**原因**: PowerShell の既定のエンコーディングは UTF-8 絵文字に対応していない

**解決策**: Java アプリケーションの実行前に次のコマンドを実行する：
```cmd
chcp 65001
```

これによりターミナルで UTF-8 エンコーディングが強制される。あるいは、Unicode のサポートが優れている Windows Terminal を使用する方法もある。

### API コールのデバッグ

**問題**: 認証エラー、レート制限、または AI モデルからの予期しない応答

**解決策**: サンプルコードには `.logRequests(true)` と `.logResponses(true)` が含まれており、API コールをコンソールに表示する。これにより認証エラーやレート制限、予期しない応答のトラブルシューティングがしやすくなる。本番環境ではログのノイズを減らすためにこれらのフラグを外すこと。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類はAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されました。正確性の確保に努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文（原言語）の書類が正式かつ権威ある情報源とみなされます。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じた誤解や誤訳について、当方は一切責任を負いません。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->