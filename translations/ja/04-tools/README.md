# モジュール 04: ツールを持つAIエージェント

## 目次

- [学習内容](../../../04-tools)
- [前提条件](../../../04-tools)
- [ツールを持つAIエージェントの理解](../../../04-tools)
- [ツール呼び出しの仕組み](../../../04-tools)
  - [ツール定義](../../../04-tools)
  - [意思決定](../../../04-tools)
  - [実行](../../../04-tools)
  - [レスポンス生成](../../../04-tools)
  - [アーキテクチャ: Spring Bootの自動配線](../../../04-tools)
- [ツールチェーン](../../../04-tools)
- [アプリケーションの実行](../../../04-tools)
- [アプリケーションの使い方](../../../04-tools)
  - [簡単なツール使用を試す](../../../04-tools)
  - [ツールチェーンをテストする](../../../04-tools)
  - [会話の流れを見る](../../../04-tools)
  - [さまざまなリクエストを試す](../../../04-tools)
- [重要な概念](../../../04-tools)
  - [ReActパターン（Reasoning and Acting）](../../../04-tools)
  - [ツールの説明が重要](../../../04-tools)
  - [セッション管理](../../../04-tools)
  - [エラー処理](../../../04-tools)
- [利用可能なツール](../../../04-tools)
- [ツールベースのエージェントを使うべき場合](../../../04-tools)
- [ツール vs RAG](../../../04-tools)
- [次のステップ](../../../04-tools)

## 学習内容

これまで、AIとの対話方法、効果的なプロンプト構築、ドキュメントを基にした応答の仕組みについて学んできました。しかし、根本的な制約がまだあります。言語モデルはテキストの生成しかできません。天気の確認、計算、データベースへの問い合わせ、外部システムとの連携はできません。

ツールがこれを変えます。モデルに呼び出せる関数を与えることで、テキスト生成器から行動が可能なエージェントへと変化させることができるのです。モデルはどのタイミングでツールを使うのか、どのツールを使うのか、どのパラメータを渡すのかを判断します。コードが関数を実行して結果を返し、モデルはその結果を応答に組み込みます。

## 前提条件

- [モジュール 01 - はじめに](../01-introduction/README.md) の完了（Azure OpenAIリソースの展開済み）
- 以前のモジュールの完了が推奨される（本モジュールでは[RAGの概念（モジュール 03）](../03-rag/README.md)をツール vs RAG比較で参照）
- ルートディレクトリにAzure認証情報を含む `.env` ファイル（モジュール 01の`azd up`で作成済み）

> **注:** モジュール 01を完了していない場合、まずはそちらの展開手順に従ってください。

## ツールを持つAIエージェントの理解

> **📝 お知らせ:** 本モジュールにおいて「エージェント」とは、ツール呼び出し機能が拡張されたAIアシスタントを指します。これは[モジュール 05: MCP](../05-mcp/README.md)で扱う**Agentic AI**パターン（計画、記憶、多段階推論を持つ自律型エージェント）とは異なります。

ツールなしでは、言語モデルは訓練データからテキストを生成するだけです。現在の天気を尋ねても推測するしかありません。ツールを与えれば、天気APIを呼び出し、計算を行い、データベースを照会し、そのリアルな結果を応答に織り込むことができます。

<img src="../../../translated_images/ja/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*ツールなしではモデルは推測するだけだが、ツールがあればAPIを呼び出し、計算し、リアルタイムデータを返せる。*

ツールを持つAIエージェントは**Reasoning and Acting (ReAct)** パターンに従います。モデルは単に応答するのではなく、必要な情報を考え、ツールを呼び出し、その結果を観察し、さらに行動を起こすか最終回答を返すかを決定します：

1. **推論（Reason）** — エージェントがユーザーの質問を解析し、必要な情報を見極める
2. **行動（Act）** — 適切なツールを選択し、正しいパラメータを生成して呼び出す
3. **観察（Observe）** — ツールの出力を受け取り、結果を評価する
4. **繰り返し or 応答（Repeat or Respond）** — さらにデータが必要ならループし、そうでなければ自然言語で回答を作成する

<img src="../../../translated_images/ja/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReActサイクル — エージェントは何をすべきかを考え、ツールを呼び出し、結果を観察し、最終回答を返すまで繰り返す。*

この処理は自動的に行われます。あなたはツールとその説明を定義し、モデルがいつどのように利用するかを決定します。

## ツール呼び出しの仕組み

### ツール定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

関数を明確な説明とパラメータ仕様と共に定義します。モデルはこれらの説明をシステムプロンプト内で見て、それぞれのツールの役割を理解します。

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 天気検索ロジック
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// アシスタントはSpring Bootによって自動的に接続されます：
// - ChatModelビーン
// - @Componentクラスのすべての@Toolメソッド
// - セッション管理のためのChatMemoryProvider
```

下図は各アノテーションの詳細を示し、AIがツールをいつ呼び出し、どの引数を渡すか理解するための手助けとなる各要素を分解しています。

<img src="../../../translated_images/ja/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*ツール定義の構成 — @ToolはAIに使用タイミングを示し、@Pは各パラメータを説明、@AiServiceは起動時にすべてをワイヤリングする。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試そう:** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)を開き、以下を質問する：
> - 「モックデータではなく実際の天気API OpenWeatherMapを統合するにはどうすればいい？」
> - 「AIがツールを正しく使うために役立つ良いツール説明とは何？」
> - 「ツール実装でAPIエラーやレート制限はどう扱うべき？」

### 意思決定

ユーザーが「シアトルの天気は？」と尋ねた時、モデルはランダムにツールを選びません。ユーザーの意図をモデルが保持しているすべてのツール説明と比較し、関連性をスコアリングし、一番合うものを選択します。そして正しいパラメータを含む構造化された関数呼び出しを生成します。この場合は`location`を「Seattle」に設定します。

対象のツールがなければ、モデルは自身の知識から回答します。複数のツールが該当すれば、最も具体的なものを選びます。

<img src="../../../translated_images/ja/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*モデルはユーザーの意図に対しすべての利用可能なツールを評価し、最適なツールを選択する — だから明確で具体的なツール説明が重要。*

### 実行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Bootは宣言的`@AiService`インターフェースをすべての登録されたツールと自動でワイヤリングし、LangChain4jがツール呼び出しを自動で実行します。裏側では、完全なツール呼び出しが６つの段階を経て流れます — ユーザーの自然言語の質問から、自然言語応答に至るまで：

<img src="../../../translated_images/ja/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*エンドツーエンドの流れ — ユーザーが質問し、モデルがツールを選択、LangChain4jが実行し、モデルが結果を自然な応答に織り込む。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試そう:** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)を開き、以下を質問する：
> - 「ReActパターンはどう動作し、なぜAIエージェントに効果的なのか？」
> - 「エージェントはどのツールを使うかと順序をどう判断している？」
> - 「ツールの実行に失敗したらどうなる？堅牢なエラー処理はどう行うべき？」

### レスポンス生成

モデルは天気情報を受け取り、ユーザー向けに自然言語の応答を整形します。

### アーキテクチャ: Spring Bootの自動配線

本モジュールはLangChain4jのSpring Boot統合機能（宣言的`@AiService`インターフェース）を利用しています。起動時にSpring Bootは`@Tool`メソッドを含むすべての`@Component`、`ChatModel`ビーン、`ChatMemoryProvider`を発見し、すべてを1つの`Assistant`インターフェースとしてボイラープレートなしでワイヤリングします。

<img src="../../../translated_images/ja/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiServiceインターフェースはChatModel、ツールコンポーネント、メモリプロバイダーをまとめ、Spring Bootがすべての配線を自動で行う。*

このアプローチの主な利点：

- **Spring Boot自動配線** — ChatModelとツールが自動的に注入される
- **@MemoryIdパターン** — セッションベースのメモリ管理が自動
- **シングルインスタンス** — Assistantは一度作成され、再利用されるため性能向上
- **型安全な実行** — Javaメソッドに直接型変換付きで呼び出し
- **マルチターンオーケストレーション** — ツールチェーンを自動で処理
- **ゼロボイラープレート** — `AiServices.builder()`の手動呼び出しやメモリHashMap不要

手動の`AiServices.builder()`を用いた代替手法はより多くのコードが必要で、Spring Boot統合のメリットを享受できません。

## ツールチェーン

**ツールチェーン** — 複数のツールを同時に使う必要がある質問に対して、ツールベースエージェントの真価が発揮されます。「シアトルの華氏での天気は？」などの質問では、まず`getCurrentWeather`を呼び出し摂氏温度を取得し、その結果を`celsiusToFahrenheit`に渡して変換を行う、といった一連のツール呼び出しを単一の会話ターンで自動で処理します。

<img src="../../../translated_images/ja/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*ツールチェーンの実例 — エージェントは最初にgetCurrentWeatherを呼び、摂氏結果をcelsiusToFahrenheitに繋ぎ、統合回答を返す。*

**優雅な障害対応** — モックデータにない都市の天気を尋ねると、ツールはエラーメッセージを返し、AIはクラッシュせずに支援できない旨を説明します。ツールは安全に失敗します。下図は両者の対比 — 適切なエラー処理では例外をキャッチして助けになる応答を返し、処理無しではアプリ全体がクラッシュします：

<img src="../../../translated_images/ja/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*ツールが失敗すると、エージェントはエラーをキャッチして応答し、クラッシュを防ぐ。*

すべては単一の会話ターン内で起こります。エージェントは複数ツール呼び出しを自律的にオーケストレーションします。

## アプリケーションの実行

**デプロイの確認：**

ルートディレクトリにAzure認証情報を含む`.env`ファイルが存在していることを確認してください（モジュール01実行時に作成）。モジュールディレクトリ(`04-tools/`)から次を実行します：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーション起動：**

> **注:** ルートディレクトリから`./start-all.sh`を使用して全アプリケーションを起動済みの場合（モジュール01に記載）、本モジュールは既にポート8084で起動しています。以下の起動コマンドは不要で、直接 http://localhost:8084 にアクセスできます。

**オプション1: Spring Bootダッシュボード利用（VS Codeユーザーに推奨）**

開発コンテナにはSpring Bootダッシュボード拡張が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Code左側のアクティビティバーにあるSpring Bootアイコンを探してください。

Spring Bootダッシュボードからは：
- ワークスペース内のすべてのSpring Bootアプリを確認
- ワンクリックでアプリの起動・停止
- リアルタイムのアプリログ表示
- アプリの状態監視

「tools」横の再生ボタンを押すだけで本モジュールを起動、または全モジュール同時起動も可能です。

VS CodeのSpring Bootダッシュボード例：

<img src="../../../translated_images/ja/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS CodeのSpring Bootダッシュボード — すべてのモジュールを一箇所で起動・停止・監視できる*

**オプション2: シェルスクリプト使用**

すべてのWebアプリ（モジュール 01-04）を起動：

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

または本モジュールのみ起動：

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

両スクリプトはルートの`.env`ファイルから環境変数を自動読み込みし、JARがなければビルドも行います。

> **注:** 起動前にすべてのモジュールを手動ビルドしたい場合：
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


ブラウザで http://localhost:8084 を開いてください。

**停止方法：**

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

本アプリケーションはウェブインターフェースを提供し、天気情報および温度変換ツールを利用できるAIエージェントと対話できます。インターフェースはクイックスタート例とリクエスト送信用のチャットパネルを含みます。
<a href="images/tools-homepage.png"><img src="../../../translated_images/ja/tools-homepage.4b4cd8b2717f9621.webp" alt="AIエージェントツールインターフェース" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AIエージェントツールのインターフェース - ツールと対話するためのクイック例とチャットインターフェース*

### 簡単なツール使用を試す

まずは簡単なリクエストから始めましょう：「100度華氏をセルシウスに変換して」。エージェントは温度変換ツールを使う必要があることを認識し、適切なパラメーターで呼び出して結果を返します。この自然な感じに注目してください — どのツールを使うかや呼び出し方法を指定しなくてもいいのです。

### ツール連鎖を試す

次にもう少し複雑なことを試してみましょう：「シアトルの天気は？その後華氏に変換して？」エージェントが段階的に動作する様子を見てください。まず天気（セルシウスで返される）を取得し、華氏に変換が必要だと認識し、変換ツールを呼び出して両方の結果を一つの応答にまとめます。

### 会話の流れを見る

チャットインターフェースは会話履歴を保持し、複数回の対話が可能です。過去のすべての問い合わせと応答を見ることができ、会話を追跡し、エージェントが複数回のやり取りを通じてどのように文脈を構築しているか理解しやすくなっています。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/ja/tools-conversation-demo.89f2ce9676080f59.webp" alt="複数ツール呼び出しによる会話" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*シンプルな変換、天気情報の検索、ツール連鎖によるマルチターン会話の例*

### さまざまなリクエストを試す

以下のような組み合わせを試してみてください：
- 天気検索：「東京の天気は？」
- 温度変換：「25℃はケルビンで何ですか？」
- 組み合わせクエリ：「パリの天気を調べて、20℃以上か教えて」

エージェントが自然言語をどのように解釈し、適切なツールの呼び出しにマッピングするかに注目してください。

## 重要な概念

### ReActパターン（推論と行動）

エージェントは推論（何をすべきか決定）と行動（ツールの使用）を交互に行います。このパターンにより、指示に応答するだけでなく、自律的な問題解決が可能になります。

### ツール記述の重要性

ツールの説明の質が、エージェントがそれをどれだけうまく使いこなせるかに直接影響します。明確で具体的な説明は、モデルが各ツールをいつどのように呼び出すべきか理解する上で役立ちます。

### セッション管理

`@MemoryId`アノテーションは、自動のセッションベースメモリ管理を有効にします。各セッションIDは`ChatMemory`インスタンスを持ち、`ChatMemoryProvider`ビーンによって管理されるため、複数のユーザーがエージェントと同時にやり取りしても会話が混ざり合うことはありません。以下の図は複数ユーザーがセッションIDに基づき分離されたメモリーストアにルーティングされる様子を示します：

<img src="../../../translated_images/ja/session-management.91ad819c6c89c400.webp" alt="@MemoryIdによるセッション管理" width="800"/>

*各セッションIDは分離された会話履歴に対応しており、ユーザー同士がメッセージを見合うことはありません。*

### エラー処理

ツールは失敗することがあります — APIのタイムアウト、パラメーターの不正、外部サービスの停止など。実運用のエージェントでは、モデルが問題を説明したり代替案を試したりできるようエラー処理が必要です。ツールが例外をスローすると、LangChain4jはそれをキャッチし、エラーメッセージをモデルに返します。モデルは自然言語で問題を説明できます。

## 利用可能なツール

以下の図は構築可能な広範なツールエコシステムを示しています。このモジュールでは天気と温度ツールを実演していますが、同じ`@Tool`パターンはデータベースクエリから支払い処理まで、あらゆるJavaメソッドに適用できます。

<img src="../../../translated_images/ja/tool-ecosystem.aad3d74eaa14a44f.webp" alt="ツールエコシステム" width="800"/>

*@Toolが付いた任意のJavaメソッドはAIに利用可能になります — このパターンはデータベース、API、メール、ファイル操作などに拡張可能です。*

## ツールベースのエージェントを使うべきケース

すべてのリクエストがツールを必要とするわけではありません。AIが外部システムと連携する必要があるか、自身の知識だけで答えられるかが判断基準です。以下のガイドは、ツールが価値を提供する場合と不要な場合の要約です：

<img src="../../../translated_images/ja/when-to-use-tools.51d1592d9cbdae9c.webp" alt="ツールを使うべき場合" width="800"/>

*簡単な判断ガイド — ツールはリアルタイムデータ、計算、アクション用；一般知識や創造的なタスクは不要。*

## ツール vs RAG

モジュール03と04はどちらもAIの能力を拡張しますが、根本的に異なる方法です。RAGはドキュメントを検索して**知識**へのアクセスを提供します。ツールは関数呼び出しによって**行動**を実行する能力を与えます。次の図は両者のワークフローの違いやトレードオフを比較しています：

<img src="../../../translated_images/ja/tools-vs-rag.ad55ce10d7e4da87.webp" alt="ツール vs RAG 比較" width="800"/>

*RAGは静的ドキュメントから情報を取り出す — ツールは動的でリアルタイムなデータを取得し操作を実行する。多くの実運用システムは両方を組み合わせている。*

実際には、多くの運用システムが両方を組み合わせています：RAGはドキュメントに基づく根拠付け、ツールはライブデータの取得や操作の実行に使います。

## 次のステップ

**次のモジュール:** [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)

---

**ナビゲーション:** [← 前：モジュール03 - RAG](../03-rag/README.md) | [メインに戻る](../README.md) | [次：モジュール05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**:  
本書類はAI翻訳サービス「[Co-op Translator](https://github.com/Azure/co-op-translator)」を使用して翻訳されています。正確性を期しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文の言語によるオリジナル文書を正式な情報源としてご参照ください。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の使用により生じたいかなる誤解や誤訳についても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->