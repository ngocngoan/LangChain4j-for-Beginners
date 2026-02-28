# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [What is LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [What Each Example Shows](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

このクイックスタートは、LangChain4j をできるだけ早く使い始められるように設計されています。LangChain4j と GitHub Models を使った AI アプリケーション構築の基本をカバーしています。次のモジュールでは、Azure OpenAI と LangChain4j を使って、より高度なアプリケーションを構築します。

## What is LangChain4j?

LangChain4j は、AI 搭載アプリケーションの構築を簡素化する Java ライブラリです。HTTP クライアントや JSON パースを扱う代わりに、きれいな Java API を使って作業します。

LangChain の「chain（チェーン）」は複数のコンポーネントをつなげることを指します。例えば、プロンプトをモデルに繋ぎ、さらにパーサーに繋いだり、複数の AI 呼び出しを連鎖させて、ある出力を次の入力に渡すことが可能です。このクイックスタートでは、より複雑なチェーンを探索する前の基礎を中心に扱います。

<img src="../../../translated_images/ja/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j でのコンポーネントのチェーン - ビルディングブロックが繋がり強力な AI ワークフローを作る*

主に以下の3つのコアコンポーネントを使用します：

**ChatModel** — AI モデルとの対話インターフェースです。`model.chat("prompt")` を呼び出してレスポンス文字列を得ます。`OpenAiOfficialChatModel` を使い、GitHub Models のような OpenAI 互換のエンドポイントで動作します。

**AiServices** — 型安全な AI サービスインターフェースを作成します。メソッドを定義し、`@Tool` アノテーションを付けるだけで LangChain4j がオーケストレーションを行います。必要に応じて AI が自動的に Java メソッドを呼び出します。

**MessageWindowChatMemory** — 会話履歴を保持します。これがないと各リクエストは独立しますが、これがあると AI は過去のメッセージを記憶し、複数ターンにわたる文脈を維持します。

<img src="../../../translated_images/ja/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j のアーキテクチャ - コアコンポーネントが協調して AI アプリケーションに力を与える*

## LangChain4j Dependencies

このクイックスタートでは [`pom.xml`](../../../00-quick-start/pom.xml) にある3つの Maven 依存関係を使用します：

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

`langchain4j-open-ai-official` モジュールは OpenAI 互換 API に接続する `OpenAiOfficialChatModel` クラスを提供します。GitHub Models は同じ API 形式を使っているため、特別なアダプターは不要で、ベース URL を `https://models.github.ai/inference` に設定するだけで使えます。

`langchain4j-easy-rag` モジュールは自動でドキュメントの分割、埋め込み、取得を行う機能を提供し、個別に手動設定せずに RAG アプリケーションを構築できます。

## Prerequisites

**Dev Container を使う場合** Java と Maven は既にインストール済みです。GitHub Personal Access Token が必要です。

**ローカル開発環境の場合:**
- Java 21以上、Maven 3.9以上
- GitHub Personal Access Token（以下の手順参照）

> **注意:** このモジュールでは GitHub Models の `gpt-4.1-nano` を使用します。コード内のモデル名は変更しないでください。GitHub の利用可能モデルに合わせて設定されています。

## Setup

### 1. Get Your GitHub Token

1. [GitHub 設定 → Personal Access Tokens](https://github.com/settings/personal-access-tokens) にアクセス
2. 「Generate new token」をクリック
3. 説明的な名前を設定（例: "LangChain4j Demo"）
4. 有効期限を設定（7日がおすすめ）
5. 「Account permissions」で「Models」を「Read-only」に設定
6. 「Generate token」をクリック
7. トークンをコピーして保存 — 後で再表示できません

### 2. Set Your Token

**オプション1: VS Code 使用（推奨）**

VS Code を使う場合は、プロジェクトルートの `.env` ファイルにトークンを追加します：

`.env` ファイルがなければ `.env.example` をコピーして `.env` にするか、新規作成してください。

**`.env` ファイル例:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env にて
GITHUB_TOKEN=your_token_here
```

これで、Explorer 内で任意のデモファイル（例：`BasicChatDemo.java`）を右クリックして **"Run Java"** を選ぶか、実行とデバッグパネルのランチ構成を使えます。

**オプション2: ターミナル使用**

環境変数としてトークンを設定します：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Run the Examples

**VS Code 使用時:** Explorer の任意のデモファイルを右クリックし **"Run Java"** を選択、または実行とデバッグパネルから起動してください（先に `.env` にトークンを追加したことを確認してください）。

**Maven 使用時:** コマンドラインから実行も可能です：

### 1. Basic Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Patterns

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ゼロショット、少数ショット、チェインオブソート、ロールベースのプロンプティングを示します。

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI が必要に応じて自動的にあなたの Java メソッドを呼び出します。

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Easy RAG を使い自動埋め込みと検索でドキュメントに関する質問ができます。

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI セーフティフィルターによる有害コンテンツのブロック例を示します。

## What Each Example Shows

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ここから始めると LangChain4j の最も基本的な使い方が分かります。`OpenAiOfficialChatModel` を作成し、`.chat()` にプロンプトを送りレスポンスを得る流れです。モデルのカスタムエンドポイントや API キーでの初期化の基礎を示します。これが理解できれば、他はすべてこれの応用です。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) を開いて以下を聞いてみてください：
> - 「このコードで GitHub Models から Azure OpenAI に切り替えるには？」
> - 「OpenAiOfficialChatModel.builder() で設定できる他のパラメータは？」
> - 「完全なレスポンスを待つ代わりにストリーミングレスポンスを使うには？」

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

モデルとの対話法がわかったところで、次は何を言うか見ていきましょう。このデモは同じモデル設定で、5つの異なるプロンプトパターンを示します。直接指示のゼロショット、例から学習する少数ショット、推論過程を示すチェインオブソート、文脈を設定するロールベースなどです。リクエストの組み立て方で同じモデルが驚くほど異なる結果を返すことが分かります。

また、このデモは変数を使った再利用可能なプロンプトを作るプロンプトテンプレートも示します。以下の例は LangChain4j の `PromptTemplate` を使い、変数を埋めて回答します。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) を開いて以下を聞いてみてください：
> - 「ゼロショットと少数ショットの違いは？どのような時に使い分けるべき？」
> - 「temperature パラメータはモデルの応答にどう影響するの？」
> - 「本番環境でのプロンプトインジェクション攻撃を防ぐ手法は？」
> - 「よく使うパターンの再利用可能な PromptTemplate オブジェクトはどう作る？」

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j の本領発揮です。`AiServices` を使い、あなたの Java メソッドを呼び出せる AI アシスタントを作ります。メソッドに `@Tool("説明")` を付けるだけで LangChain4j が管理し、AI がユーザーの要求に基づいて適切にツールを使い分けます。アクションを実行できる AI を作るための関数呼び出し技術を示します。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) を開いて以下を聞いてみてください：
> - 「@Tool アノテーションはどう動作し、LangChain4j は何をしている？」
> - 「複数のツールを連続して呼んで複雑な問題を解ける？」
> - 「ツールで例外が発生した場合のエラー処理はどうすべき？」
> - 「この電卓例の代わりに実際の API を組み込むには？」

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ここでは LangChain4j の「Easy RAG」アプローチによる RAG（検索拡張生成）を紹介します。ドキュメントを読み込み、自動的に分割して埋め込み、インメモリーストアに保存します。クエリ時に関連する断片を検索して AI に渡し、一般的な知識ではなくドキュメントに基づいた回答を得ます。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) を開いて以下を聞いてみてください：
> - 「RAG はモデルの訓練データ使用と比べて AI の幻覚をどう防ぐ？」
> - 「この簡易アプローチとカスタム RAG パイプラインの違いは？」
> - 「複数ドキュメントや大規模ナレッジベース対応にスケールする方法は？」

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

安全な AI を多層防御で構築します。このデモは二段階の保護を示します：

**パート1: LangChain4j 入力ガードレール** — 危険なプロンプトを LLM に渡す前にブロックします。禁止ワードやパターンを検出するカスタムガードレールを作成可能。コード内で実行されるため高速で無料です。

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

**パート2: プロバイダー安全フィルター** — GitHub Models のフィルターがガードレールの見逃しも検出。重大違反はハードブロック（HTTP 400 エラー）、軽度は丁寧に拒否するソフト拒否が発生します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) を開いて以下を聞いてみてください：
> - 「InputGuardrail とは？自分で作るには？」
> - 「ハードブロックとソフト拒否の違いは？」
> - 「なぜガードレールとプロバイダーフィルターを両方使うの？」

## Next Steps

**次のモジュール:** [01-introduction - LangChain4j と Azure の gpt-5 のはじめ方](../01-introduction/README.md)

---

**ナビゲーション:** [← メインへ戻る](../README.md) | [次: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### First-Time Maven Build

**問題**: 初回の `mvn clean compile` または `mvn package` に時間がかかる（10-15分）

**原因**: Maven は初回ビルド時に全ての依存関係（Spring Boot、LangChain4j ライブラリ、Azure SDK など）をダウンロードする必要があります。

**対処法**: これは正常な挙動です。依存関係はローカルにキャッシュされるため、次回からは大幅に高速化されます。ダウンロード速度はネットワーク環境に依存します。

### PowerShell Maven Command Syntax

**問題**: Maven コマンドが `Unknown lifecycle phase ".mainClass=..."` エラーで失敗する
**原因**: PowerShell は `=` を変数代入演算子として解釈するため、Maven のプロパティ構文が壊れます

**解決策**: Maven コマンドの前にストップパース演算子 `--%` を使用してください：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 演算子は、PowerShell に対して残りの引数を解釈せず文字通り Maven に渡すよう指示します。

### Windows PowerShell の絵文字表示

**問題**: PowerShell で AI の応答が絵文字の代わりにゴミ文字（例: `????` や `â??`）になる

**原因**: PowerShell の既定のエンコーディングが UTF-8 の絵文字をサポートしていないため

**解決策**: Java アプリを実行する前にこのコマンドを実行してください：
```cmd
chcp 65001
```

これにより端末のエンコーディングが UTF-8 に強制されます。もしくは、Unicode サポートが優れている Windows Terminal を使用してください。

### API コールのデバッグ

**問題**: 認証エラー、レート制限、または AI モデルからの予期しない応答

**解決策**: 例には `.logRequests(true)` と `.logResponses(true)` が含まれており、API コールをコンソールに表示します。これにより認証エラー、レート制限、予期しない応答のトラブルシューティングが可能です。運用時にはログのノイズを減らすためこれらのフラグを削除してください。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「[Co-op Translator](https://github.com/Azure/co-op-translator)」を使用して翻訳されています。正確性の確保に努めておりますが、自動翻訳には誤りや不正確な部分が含まれる場合があります。原文が正本としての権威ある情報源となります。重要な情報については、専門の翻訳者による翻訳を推奨します。本翻訳の利用によって生じたいかなる誤解や誤訳についても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->