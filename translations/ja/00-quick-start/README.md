# Module 00: クイックスタート

## 目次

- [はじめに](../../../00-quick-start)
- [LangChain4jとは？](../../../00-quick-start)
- [LangChain4jの依存関係](../../../00-quick-start)
- [前提条件](../../../00-quick-start)
- [セットアップ](../../../00-quick-start)
  - [1. GitHubトークンを取得する](../../../00-quick-start)
  - [2. トークンを設定する](../../../00-quick-start)
- [サンプルの実行](../../../00-quick-start)
  - [1. 基本的なチャット](../../../00-quick-start)
  - [2. プロンプトパターン](../../../00-quick-start)
  - [3. 関数呼び出し](../../../00-quick-start)
  - [4. ドキュメントQ&A (RAG)](../../../00-quick-start)
  - [5. 責任あるAI](../../../00-quick-start)
- [各サンプルの説明](../../../00-quick-start)
- [次のステップ](../../../00-quick-start)
- [トラブルシューティング](../../../00-quick-start)

## はじめに

このクイックスタートは、LangChain4jをできるだけ早く使い始めることを目的としています。LangChain4jとGitHub Modelsを使ったAIアプリケーション構築の基本を解説します。次のモジュールでは、LangChain4jでAzure OpenAIを使ってより高度なアプリケーションを作成します。

## LangChain4jとは？

LangChain4jは、AI搭載アプリケーションの構築を簡素化するJavaライブラリです。HTTPクライアントやJSON解析を扱わず、クリーンなJava APIで操作します。

LangChainの「チェーン」は複数のコンポーネントを連結することを意味します。例えば、プロンプトをモデルへ、モデルの出力をパーサへつなぐ、あるいは複数のAI呼び出しを連鎖させて前の出力を次の入力にするなどです。このクイックスタートでは、より複雑なチェーンを探る前に基礎を学びます。

<img src="../../../translated_images/ja/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4jのコンポーネント連結 - ブロックをつなげて強力なAIワークフローを構築*

以下の3つのコアコンポーネントを使います：

**ChatLanguageModel** - AIモデルとの対話のインターフェイス。`model.chat("prompt")`を呼ぶと応答文字列が返ります。OpenAI互換のエンドポイントに対応した`OpenAiOfficialChatModel`を使用します。

**AiServices** - 型安全なAIサービスのインターフェイスを作成。メソッドを定義して`@Tool`で注釈をつけると、LangChain4jがオーケストレーションを処理します。必要に応じてAIが自動的にJavaメソッドを呼び出します。

**MessageWindowChatMemory** - 会話履歴を管理。これがないと各リクエストは独立しますが、これを使うとAIが過去のメッセージを覚え複数ターンにわたってコンテキストを維持します。

<img src="../../../translated_images/ja/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4jアーキテクチャ - コアコンポーネントが連携してAIアプリケーションを支える*

## LangChain4jの依存関係

このクイックスタートでは、[`pom.xml`](../../../00-quick-start/pom.xml)の2つのMaven依存関係を使用します：

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
```

`langchain4j-open-ai-official`モジュールはOpenAI互換APIに接続する`OpenAiOfficialChatModel`クラスを提供します。GitHub Modelsも同じAPI形式を使うため、特殊なアダプターは不要で、ベースURLを`https://models.github.ai/inference`に向けるだけです。

## 前提条件

**Dev Container利用の場合** JavaとMavenはすでにインストール済みです。GitHubパーソナルアクセストークンだけ用意すればOKです。

**ローカル開発環境の場合：**
- Java 21以上、Maven 3.9以上
- GitHubパーソナルアクセストークン（取得手順は以下参照）

> **注意：** 本モジュールはGitHub Modelsの`gpt-4.1-nano`を使用します。コード中のモデル名は変更しないでください。GitHubが提供するモデルに合わせて設定されています。

## セットアップ

### 1. GitHubトークンを取得する

1. [GitHub設定 → パーソナルアクセストークン](https://github.com/settings/personal-access-tokens) にアクセス
2. 「新しいトークンを生成」をクリック
3. わかりやすい名前を設定（例：「LangChain4j Demo」）
4. 有効期限を設定（7日推奨）
5. 「Account permissions」下の「Models」を「読み取り専用」に設定
6. 「トークンを生成」をクリック
7. 表示されたトークンをコピーし保存（再表示不可）

### 2. トークンを設定する

**オプション1：VS Codeを使う（推奨）**

VS Codeの場合、プロジェクトルートの`.env`ファイルにトークンを追加します：

`.env`ファイルが存在しない場合は`.env.example`をコピーして作成するか、新規作成してください。

**例：`.env`ファイル**
```bash
# /workspaces/LangChain4j-for-Beginners/.env内で
GITHUB_TOKEN=your_token_here
```

これでExplorerで任意のデモファイル（例：`BasicChatDemo.java`）を右クリックし、**「Run Java」**を選ぶか、ラン＆デバッグパネルの起動構成から実行できます。

**オプション2：ターミナルを使う**

環境変数としてトークンを設定：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## サンプルの実行

**VS Code利用時:** Explorerの任意のデモファイルを右クリックして**「Run Java」**を選択、またはラン＆デバッグパネルから起動構成を使います（事前に`.env`にトークンを設定済みであること）。

**Maven利用時:** またはコマンドラインから実行可能：

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

ゼロショット、数ショット、チェーンオブソート、役割ベースのプロンプトを表示。

### 3. 関数呼び出し

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

必要に応じてAIがJavaメソッドを自動で呼び出します。

### 4. ドキュメントQ&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt`の内容について質問可能。

### 5. 責任あるAI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI安全フィルターで有害なコンテンツをブロックする様子を確認。

## 各サンプルの説明

**基本的なチャット** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4jの最もシンプルな使い方から始めます。`OpenAiOfficialChatModel`を作成し、`.chat()`でプロンプトを送り応答を得ます。独自エンドポイントとAPIキーでモデルを初期化する基礎を示します。ここを理解すれば他は応用です。

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatと試す:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)を開き、以下を質問：
> - 「このコードでGitHub ModelsからAzure OpenAIに切り替えるには？」
> - 「OpenAiOfficialChatModel.builder()で設定可能な他のパラメータは？」
> - 「完全応答を待たずにストリーミングレスポンスを追加するには？」

**プロンプトエンジニアリング** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

モデルとの対話法がわかったら、今度は何を言うかを探ります。同じモデル設定で5つの異なるプロンプトパターンを紹介。直接指示のゼロショット、例から学ぶ数ショット、推論過程のチェーンオブソート、コンテキスト設定の役割ベースなどです。同じモデルでも要求の仕方で結果が大きく異なることがわかります。

テンプレートプロンプトも示し、変数で使い回せる方法を紹介。
以下はLangChain4jの`PromptTemplate`で変数を埋める例。AIは指定された目的地とアクティビティに基づいて応答します。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatと試す:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)を開き、以下を質問：
> - 「ゼロショットと数ショットの違いは？どちらをいつ使うべき？」
> - 「temperatureパラメータはモデルの応答にどう影響？」
> - 「本番でのプロンプトインジェクション攻撃を防ぐ技術は？」
> - 「共通パターン用の再利用可能なPromptTemplateの作り方は？」

**ツール統合** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ここでLangChain4jの強みが発揮されます。`AiServices`を使ってJavaメソッドを呼べるAIアシスタントを作ります。メソッドを`@Tool("説明")`で注釈するとLangChain4jが管理し、ユーザーの要求に応じてAIが自動で適切なツールを呼び出します。これは質問に答えるだけでなく行動できるAIづくりの重要技術です。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatと試す:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)を開き、以下を質問：
> - 「@Tool注釈はどう動作しLangChain4jは何をしている？」
> - 「AIは複数のツールを順に使って複雑な問題を解けるか？」
> - 「ツールで例外が起きたらどう扱うのが良い？」
> - 「この電卓例以外に本物のAPIを組み込むには？」

**ドキュメントQ&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

RAG（情報検索強化生成）の基礎を示します。モデルの訓練データに頼らず、[`document.txt`](../../../00-quick-start/document.txt)の内容を読み込みプロンプトに含め、ドキュメントに基づいてAIが回答します。これは自分のデータで動作するシステム構築の第一歩です。

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **注意:** このシンプル手法は文書全体をプロンプトに入れます。大きなファイル（10KB超）ではコンテキスト制限を超えます。モジュール03でチャンク分割とベクトル検索を扱い、本格的なRAGを解説します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatと試す:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)を開き、以下を質問：
> - 「RAGはどうやってモデル訓練データ利用よりAIの幻覚を防ぐ？」
> - 「この単純な方法とベクトル埋め込みによる検索の違いは？」
> - 「複数文書や大規模知識ベース対応にどう拡張？」
> - 「AIが提供したコンテキストのみ使うようプロンプト設計のベストプラクティスは？」

**責任あるAI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

多層防御でAI安全を構築します。このデモは2層の保護を連携させて示します：

**パート1: LangChain4j入力ガードレール** - 危険なプロンプトがLLMに届くのを阻止。禁止キーワードやパターンをチェックするカスタムガードレールを作成。コード内で実行するため高速かつ無料です。

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

**パート2: プロバイダー安全フィルター** - GitHub Models自身にもガードレールの見逃しを補うフィルターが内蔵。重大違反にはハードブロック（HTTP 400エラー）、軽微なものにはAIが丁寧に拒否するソフトリフューザルを表示。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatと試す:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)を開き、以下を質問：
> - 「InputGuardrailとは何？自分でどう作る？」
> - 「ハードブロックとソフトリフューザルの違いは？」
> - 「なぜガードレールとプロバイダーフィルターの両方を使う？」

## 次のステップ

**次のモジュール:** [01-introduction - LangChain4jとgpt-5によるAzureでのはじめかた](../01-introduction/README.md)

---

**ナビゲーション:** [← メインに戻る](../README.md) | [次: Module 01 - Introduction →](../01-introduction/README.md)

---

## トラブルシューティング

### 初回のMavenビルド

**問題:** 初回の`mvn clean compile`や`mvn package`が長時間かかる（10〜15分）

**原因:** Mavenが初回ビルド時にすべてのプロジェクト依存関係（Spring Boot、LangChain4jライブラリ、Azure SDKなど）をダウンロードするため。

**対処法:** これは正常な動作です。依存ライブラリはローカルにキャッシュされるので次回以降は高速になります。ダウンロード時間はネットワーク速度に依存します。
### PowerShell Maven コマンド構文

**問題**: Maven コマンドが `Unknown lifecycle phase ".mainClass=..."` エラーで失敗する

**原因**: PowerShell は `=` を変数代入演算子として解釈し、Maven のプロパティ構文が壊れる

**解決策**: Maven コマンドの前にストップパース演算子 `--%` を使用する：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 演算子は、PowerShell に対して残りの引数を解釈せずに文字通り Maven に渡すよう指示します。

### Windows PowerShell の絵文字表示

**問題**: PowerShell で AI 応答が絵文字の代わりに文字化け（例：`????` や `â??`）で表示される

**原因**: PowerShell のデフォルトエンコーディングが UTF-8 絵文字をサポートしていない

**解決策**: Java アプリケーション実行前にこのコマンドを実行してください：
```cmd
chcp 65001
```

これにより端末のエンコーディングが UTF-8 に強制されます。あるいは、Unicode サポートが優れている Windows Terminal を使用してください。

### API 呼び出しのデバッグ

**問題**: 認証エラーやレート制限、想定外の AI モデルからの応答

**解決策**: サンプルには `.logRequests(true)` と `.logResponses(true)` が含まれており、これらは API 呼び出しをコンソールに表示します。これにより認証エラー、レート制限、想定外の応答のトラブルシューティングが容易になります。本番環境ではログのノイズを減らすためにこれらのフラグを削除してください。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。重要な情報については、原文の内容を権威ある情報源とし、専門の人間による翻訳を推奨します。本翻訳の使用により生じたいかなる誤解や誤訳についても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->