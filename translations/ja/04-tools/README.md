# Module 04: ツールを使用したAIエージェント

## 目次

- [学習内容](../../../04-tools)
- [前提条件](../../../04-tools)
- [ツールを使用したAIエージェントの理解](../../../04-tools)
- [ツール呼び出しの仕組み](../../../04-tools)
  - [ツール定義](../../../04-tools)
  - [意思決定](../../../04-tools)
  - [実行](../../../04-tools)
  - [応答生成](../../../04-tools)
  - [アーキテクチャ: Spring Bootの自動配線](../../../04-tools)
- [ツールチェイニング](../../../04-tools)
- [アプリケーションの実行](../../../04-tools)
- [アプリケーションの使用方法](../../../04-tools)
  - [簡単なツール使用を試す](../../../04-tools)
  - [ツールチェイニングをテストする](../../../04-tools)
  - [会話の流れを見る](../../../04-tools)
  - [さまざまなリクエストを試す](../../../04-tools)
- [重要な概念](../../../04-tools)
  - [ReActパターン（Reasoning and Acting）](../../../04-tools)
  - [ツールの説明は重要](../../../04-tools)
  - [セッション管理](../../../04-tools)
  - [エラー処理](../../../04-tools)
- [利用可能なツール](../../../04-tools)
- [ツールベースのエージェントを使うタイミング](../../../04-tools)
- [ツールとRAGの比較](../../../04-tools)
- [次のステップ](../../../04-tools)

## 学習内容

ここまで、AIとの会話方法、効果的なプロンプトの構築、文書に基づいた応答の根拠付けを学びました。しかし、根本的な制限があります。言語モデルはテキストしか生成できません。天気を確認したり、計算を行ったり、データベースを照会したり、外部システムと連携したりできません。

ツールがこれを変えます。モデルに呼び出し可能な関数を与えることで、テキスト生成器から行動を取れるエージェントへと変わります。モデルはツールが必要な時、どのツールを使うか、どんなパラメーターを渡すかを決定します。あなたのコードは関数を実行して結果を返します。モデルはその結果を応答に組み込みます。

## 前提条件

- [Module 01 - Introduction](../01-introduction/README.md) の完了（Azure OpenAI リソースをデプロイ済み）
- できれば前のモジュールを完了していること（このモジュールでは[Module 03のRAG概念](../03-rag/README.md)に言及しています）
- ルートディレクトリにAzure認証情報を含む `.env` ファイル（Module 01の `azd up` によって作成）

> **注意：** Module 01をまだ完了していない場合は、まずそちらのデプロイ手順に従ってください。

## ツールを使用したAIエージェントの理解

> **📝 注記：** 本モジュールでの「エージェント」という用語は、ツール呼び出し機能を強化されたAIアシスタントを指します。これは[Module 05: MCP](../05-mcp/README.md)で扱う**Agentic AI**パターン（計画、記憶、多段階推論を持つ自律エージェント）とは異なります。

ツールがなければ、言語モデルはトレーニングデータからテキストしか生成できません。現在の天気を尋ねても推測するしかありません。ツールを与えると、天気APIを呼び出したり計算を行ったりデータベースを照会したりでき、その実際の結果を応答に織り込みます。

<img src="../../../translated_images/ja/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*ツールがなければモデルは推測するだけですが、ツールがあればAPIを呼び出し、計算を行い、リアルタイムデータを返せます。*

ツールを持つAIエージェントは**Reasoning and Acting(ReAct)**パターンに従います。モデルは単に返答するだけでなく、自分に必要なものを考え、ツールを呼び出して行動し、結果を観察し、さらに行動するか最終回答を出すか決定します：

1. **推論(Reason)** — ユーザーの質問を分析し必要な情報を判断
2. **行動(Act)** — 適切なツールを選択し、正しいパラメーターを生成して呼び出す
3. **観察(Observe)** — ツールの出力を受け取り、結果を評価
4. **繰り返しまたは回答(Repeat or Respond)** — 追加情報が必要なら繰り返し、そうでなければ自然言語の回答を作成

<img src="../../../translated_images/ja/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReActサイクル — エージェントは何をすべきか推論し、ツールを呼び出して行動し、結果を観察し、最終回答が出せるまでループします。*

これは自動で行われます。あなたはツールとその説明を定義します。モデルはいつどのように使うかを決定します。

## ツール呼び出しの仕組み

### ツール定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

関数は明確な説明とパラメーター仕様と共に定義します。モデルはこれらの説明をシステムプロンプトで見て、各ツールの役割を理解します。

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // あなたの天気検索ロジック
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// アシスタントはSpring Bootによって自動的に接続されています：
// - ChatModel ビーン
// - @Component クラスからのすべての @Tool メソッド
// - セッション管理のためのChatMemoryProvider
```

以下の図は各アノテーションを分解し、AIがいつツールを呼ぶか、どの引数を渡すか理解を助ける仕組みを示しています：

<img src="../../../translated_images/ja/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*ツール定義の仕組み — @Tool がAIに使用タイミングを伝え、@P は各パラメーターを説明し、@AiServiceは起動時に全てを結びつけます。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試す:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) を開き、以下を聞いてみましょう：
> - 「OpenWeatherMapなどの実際の天気APIをモックの代わりに統合するには？」
> - 「AIが正確に使うための良いツール説明とは？」
> - 「ツール実装でAPIエラーやレート制限をどのように扱う？」

### 意思決定

ユーザーが「シアトルの天気は？」と尋ねた時、モデルはランダムにツールを選びません。全てのツール説明をユーザーの意図と比較し、関連度でスコア付けし最適なものを選びます。構造化された関数呼び出しを生成し、パラメーターをセットします。この場合は `location` に `"Seattle"` を設定。

マッチするツールがなければモデル自身の知識から回答します。複数マッチしたら最も特定的なものを選びます。

<img src="../../../translated_images/ja/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*モデルは使用可能な全てのツールをユーザー意図に照らして評価し、最適なものを選択します。だから明確で具体的なツール説明が重要です。*

### 実行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Bootは宣言的な `@AiService` インターフェースを登録済みの全ツールで自動配線し、LangChain4jはツール呼び出しを自動的に実行します。内部で完全なツール呼び出しは６段階を経て、ユーザーの自然言語質問から自然言語回答まで流れます：

<img src="../../../translated_images/ja/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*ユーザーの質問、モデルがツールを選択、LangChain4jが実行、モデルが結果を自然な応答に反映する終端までのフロー。*

Module 00の[ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)を実行済みなら同じパターンを見ています。以下のシーケンス図はDemoの裏側で何が起きたかを示します：

<img src="../../../translated_images/ja/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Quick Startデモのツール呼び出しループ — `AiServices` がメッセージとツールスキーマをLLMに送り、LLMは `add(42, 58)` のような関数呼び出しを返し、LangChain4jが `Calculator` メソッドをローカルで実行し、結果を返して最終回答になる。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試す:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) を開き以下を聞いてみましょう：
> - 「ReActパターンはどのように機能し、なぜAIエージェントに効果的なのか？」
> - 「エージェントはどのようにツールを選び順序を決めるのか？」
> - 「ツール実行が失敗したらどうなる？堅牢なエラー処理はどうすべきか？」

### 応答生成

モデルは天気データを受け取り、ユーザー向けに自然言語の応答をフォーマットします。

### アーキテクチャ: Spring Bootの自動配線

本モジュールではLangChain4jのSpring Boot連携で宣言的な `@AiService` インターフェースを使います。起動時にSpring Bootが `@Tool` メソッドを含む全 `@Component`、`ChatModel` Bean、`ChatMemoryProvider` を検出し、ボイラープレートなしで単一の `Assistant` インターフェースに結合します。

<img src="../../../translated_images/ja/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiServiceインターフェースがChatModel、ツールコンポーネント、メモリプロバイダーを結びつけ、Spring Bootが全自動で配線します。*

以下はHTTPリクエストからコントローラ、サービス、自動配線されたプロキシ、ツール実行、そして戻りまでのリクエストライフサイクルをシーケンス図で示したものです：

<img src="../../../translated_images/ja/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Spring Bootの完全なリクエストライフサイクル — HTTPリクエストがコントローラとサービスを通り、自動配線されたAssistantプロキシへ流れ、LLMとツール呼び出しを自動オーケストレーションします。*

このアプローチの主な利点：

- **Spring Bootの自動配線** — ChatModelとツールの自動挿入
- **@MemoryIdパターン** — セッションベースのメモリ管理を自動化
- **単一インスタンス** — Assistantを一度作成しパフォーマンス向上
- **型安全な実行** — Javaメソッドを直接呼び型変換対応
- **マルチターンオーケストレーション** — ツールチェイニングを自動処理
- **ボイラープレートゼロ** — 手動の `AiServices.builder()` 呼び出しやHashMapは不要

手動で `AiServices.builder()` を使う代替手法はコードが増え、Spring Boot連携の利点を逃します。

## ツールチェイニング

**ツールチェイニング** — ツールベースのエージェントの真の力は、単一の質問で複数ツールが必要な時に発揮されます。「シアトルの華氏の天気は？」と尋ねると、エージェントは自動で2つのツールをチェインします：まず `getCurrentWeather` を呼び摂氏で温度を取得し、次にその値を `celsiusToFahrenheit` に渡して変換します — 単一の会話ターンで完結します。

<img src="../../../translated_images/ja/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*ツールチェイニングの実例 — エージェントはまず getCurrentWeather を呼び、摂氏の結果を celsiusToFahrenheit に渡し、結合された回答を届けます。*

**グレースフルな失敗** — モックデータにない都市の天気を尋ねると、ツールがエラーメッセージを返し、AIはクラッシュせず助けられない旨を説明します。ツールは安全に失敗します。以下の図は正しいエラー処理と無い場合の対比を示します — 適切にエラーをキャッチするとエージェントは丁寧に説明し、それがなければアプリ全体がクラッシュします：

<img src="../../../translated_images/ja/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*ツールが失敗しても、エージェントは例外をキャッチし、クラッシュせず助言的な応答を返します。*

これは単一の会話ターンで行われます。エージェントは複数のツール呼び出しを自律的にオーケストレーションします。

## アプリケーションの実行

**デプロイの確認：**

`.env` ファイルがルートディレクトリに存在しAzure認証情報を含んでいることを確認してください（Module 01で作成済み）。このモジュールのディレクトリ(`04-tools/`)から実行します：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションを起動する：**

> **注意：** ルートディレクトリの `./start-all.sh` を使ってすべてのアプリを起動済みであれば（Module 01の説明参照）、このモジュールは既にポート8084で動作中です。以下の起動コマンドはスキップし、直接 http://localhost:8084 にアクセス可能です。

**オプション1：Spring Boot Dashboardを使用（VS Codeユーザー推奨）**

開発コンテナにはSpring Boot Dashboard拡張機能が含まれており、全Spring Bootアプリを視覚的に管理できます。VS Codeの左側のActivity Bar（Spring Bootアイコン）からアクセス可能です。

Spring Boot Dashboardからは：
- ワークスペース内の全Spring Bootアプリを一覧表示
- クリック一つで起動/停止可能
- アプリケーションのログをリアルタイムで閲覧
- アプリ状況をモニター

「tools」横の再生ボタンをクリックするとこのモジュールを起動できます。全部のモジュールを一括起動も可能です。

VS CodeのSpring Boot Dashboard画面：

<img src="../../../translated_images/ja/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS CodeのSpring Boot Dashboard — 全モジュールを一箇所で起動、停止、監視可能*

**オプション2：シェルスクリプトを使用**

モジュール01～04の全Webアプリケーションを起動：

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

あるいはこのモジュールだけを起動するには：

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

どちらのスクリプトもルートの `.env` ファイルから環境変数を自動的に読み込み、JARが存在しなければビルドします。

> **注意：** 手動ですべてのモジュールをビルドしたい場合は、起動前に以下を実行してください：
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

ブラウザで http://localhost:8084 を開きます。

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

## アプリケーションの使用方法

アプリケーションはウェブインターフェースを提供し、天気情報と温度変換ツールにアクセスできるAIエージェントと対話できます。インターフェースは以下のようなもので、クイックスタート例とリクエスト送信用のチャットパネルが含まれています：

<a href="images/tools-homepage.png"><img src="../../../translated_images/ja/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AIエージェントツールのインターフェース - クイック例とツール対話のチャットインターフェース*

### 簡単なツール使用例を試す

まずはシンプルなリクエスト「100度華氏を摂氏に変換してみて」から始めます。エージェントは温度変換ツールが必要と判断し、適切なパラメータで呼び出して結果を返します。ツールの指定や呼び出し方法を指定しなくても自然に動く点に注目してください。

### ツール連鎖のテスト

次にもう少し複雑な例：「シアトルの天気は？それを華氏に変換して」これをエージェントがステップごとに処理する様子を見てみましょう。まず天気情報を取得（摂氏で返る）、華氏に変換が必要と認識して変換ツールを呼び出し、両結果をまとめて返します。

### 会話の流れを見る

チャットインターフェースは会話履歴を保持し、複数のターンにわたるやりとりが可能です。過去の全クエリと応答を確認でき、エージェントがどのように文脈を構築しているかを追えます。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ja/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*簡単な変換、天気検索、ツール連鎖を含む複数ターン会話*

### さまざまなリクエストを試す

次のような組み合わせを試してみてください：
- 天気検索: 「東京の天気は？」
- 温度変換: 「25℃はケルビンで何度？」
- 複合クエリ: 「パリの天気を確認して20℃を超えているか教えて」

エージェントが自然言語を適切に解釈し、該当するツール呼び出しにマッピングする様子が分かります。

## キーコンセプト

### ReActパターン（推論と行動）

エージェントは推論（何をするか決める）と行動（ツールを使う）を交互に行います。このパターンにより、単に指示に返信するだけでなく自律的に問題解決が可能となります。

### ツールの説明が重要

ツールの説明の質がエージェントの使用精度に直結します。明確かつ具体的な説明があると、モデルはいつどのようにツールを呼ぶかを理解しやすくなります。

### セッション管理

`@MemoryId` アノテーションにより自動的なセッションベースのメモリ管理が可能です。各セッションIDごとに `ChatMemory` インスタンスが `ChatMemoryProvider` ビーンによって管理され、複数ユーザーが同時にエージェントと対話しても会話が混ざりません。下図は、複数ユーザーがセッションIDで識別された独立したメモリストアにルーティングされる様子を示しています：

<img src="../../../translated_images/ja/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*各セッションIDが独立した会話履歴にマッピングされ、ユーザー同士でメッセージが混ざることはありません。*

### エラーハンドリング

ツールは失敗することがあります — APIのタイムアウト、無効なパラメータ、外部サービスの停止など。実運用のエージェントはモデルが問題を説明したり代替手段を試したりできるようエラーハンドリングが必要です。ツールが例外を投げるとLangChain4jがそれを捕捉し、エラーメッセージをモデルに返すことで自然言語で問題を説明できます。

## 利用可能なツール

次の図は構築可能な広範なツールエコシステムを示しています。このモジュールでは天気と温度ツールを使っていますが、同じ `@Tool` パターンはデータベースクエリから決済処理まで任意のJavaメソッドに適用できます。

<img src="../../../translated_images/ja/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*@Toolアノテーション付きのJavaメソッドはAIに利用可能となります。このパターンはデータベース、API、メール、ファイル操作など多方面に拡張可能です。*

## ツールベースのエージェントを使うタイミング

すべてのリクエストにツールが必要なわけではありません。判断基準はAIが外部システムとやりとりすべきか、自身の知識のみで答えられるかに依ります。以下のガイドはツールが価値を発揮する場合と不要な場合をまとめたものです：

<img src="../../../translated_images/ja/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*簡単な判断ガイド — ツールはリアルタイムデータや計算、行動に使い、一般的な知識や創造的作業には不要です。*

## ツールとRAGの比較

モジュール03と04はAIの能力拡張ですが、根本的に異なる方法です。RAGはドキュメントを検索して**知識**にアクセスを与えます。ツールは関数呼び出しで**行動**を実行可能にします。下図はそれぞれのワークフローやトレードオフを比較したものです：

<img src="../../../translated_images/ja/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAGは静的ドキュメントから情報を取得し、ツールは動的なリアルタイムデータの取得や行動を実行します。多くの実運用システムは両者を組み合わせています。*

実際には多くの運用システムが双方を併用しています：RAGでドキュメントに基づく回答の根拠を与え、ツールでライブデータ取得や操作を行います。

## 次のステップ

**次のモジュール：** [05-mcp - モデルコンテキストプロトコル (MCP)](../05-mcp/README.md)

---

**ナビゲーション:** [← 前へ: モジュール03 - RAG](../03-rag/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確性の向上に努めておりますが、自動翻訳には誤りや不正確な部分が含まれている可能性があります。原文の言語によるオリジナル文書が正式な情報源とみなされるべきです。重要な情報については、専門の人間翻訳を推奨いたします。本翻訳の利用に起因する誤解や誤訳について、当方は一切の責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->