# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [What You'll Learn](../../../05-mcp)
- [What is MCP?](../../../05-mcp)
- [How MCP Works](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [Running the Examples](../../../05-mcp)
  - [Prerequisites](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Running the Demo](../../../05-mcp)
    - [How the Supervisor Works](../../../05-mcp)
    - [How FileAgent Discovers MCP Tools at Runtime](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## What You'll Learn

あなたは会話型AIを構築し、プロンプトを習得し、ドキュメントに基づいた応答を出し、ツールを使ったエージェントを作成してきました。しかし、これらのツールはすべてあなたの特定のアプリケーション向けにカスタムで作られていました。もしAIに誰でも作成して共有できる標準化されたツールのエコシステムへのアクセスを与えられたらどうでしょうか？このモジュールでは、Model Context Protocol（MCP）とLangChain4jのagenticモジュールを使って、まさにそれを行う方法を学びます。まずシンプルなMCPファイルリーダーを示し、その後、Supervisor Agentパターンで使う洗練されたagenticワークフローに簡単に統合できることを紹介します。

## What is MCP?

Model Context Protocol（MCP）はまさにそれを提供します—AIアプリケーションが外部ツールを発見し利用するための標準的な方法です。各データソースやサービスごとにカスタム統合を書く代わりに、機能を統一フォーマットで公開するMCPサーバーに接続します。AIエージェントはそこからツールを自動で発見し利用できます。

以下の図は違いを示します—MCPなしの場合、すべての統合はカスタムのポイントツーポイント配線が必要ですが、MCPを使うと単一のプロトコルでアプリをどんなツールにも接続できます：

<img src="../../../translated_images/ja/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Before MCP: 複雑なポイントツーポイント統合。After MCP: 一つのプロトコルで無限の可能性。*

MCPはAI開発における根本的な問題を解決します：すべての統合はカスタムです。GitHubにアクセスしたい？カスタムコードが必要。ファイルを読みたい？カスタムコード。データベースに問い合わせたい？カスタムコード。しかもこれらの統合は他のAIアプリケーションと連携しません。

MCPはこれを標準化します。MCPサーバーは明確な説明とスキーマ付きのツールを公開し、任意のMCPクライアントが接続して利用可能なツールを発見できます。一度構築すれば、どこでも使えます。

以下の図はこのアーキテクチャを示します—単一のMCPクライアント（あなたのAIアプリ）が複数のMCPサーバーに接続し、それぞれが標準プロトコルで独自のツールセットを公開します：

<img src="../../../translated_images/ja/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocolアーキテクチャ - 標準化されたツールの発見と実行*

## How MCP Works

内部的には、MCPはレイヤードアーキテクチャを使用します。Javaアプリケーション（MCPクライアント）は利用可能なツールを発見し、JSON-RPCリクエストをトランスポートレイヤー（StdioやHTTP）経由で送信し、MCPサーバーが操作を実行し結果を返します。以下の図はこのプロトコルの各層を分解して示します：

<img src="../../../translated_images/ja/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCPの仕組み — クライアントはツールを発見し、JSON-RPCメッセージを交換し、トランスポートレイヤー経由で操作を実行します。*

**サーバークライアントアーキテクチャ**

MCPはクライアント-サーバーモデルを使用します。サーバーはファイル読み取り、データベース問い合わせ、API呼び出しなどのツールを提供します。クライアント（あなたのAIアプリ）はサーバーに接続しツールを利用します。

LangChain4jでMCPを使うには、以下のMaven依存関係を追加してください：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**ツール発見**

クライアントがMCPサーバーに接続すると、「どんなツールがありますか？」と尋ねます。サーバーは使用可能なツール一覧を、説明やパラメータスキーマ付きで返します。AIエージェントはユーザーのリクエストに基づきどのツールを使うか決めることができます。以下の図はこのやりとりを示します—クライアントは `tools/list` リクエストを送り、サーバーは説明とパラメータスキーマ付きツールを返します：

<img src="../../../translated_images/ja/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AIは起動時に利用可能なツールを発見し、使うべき能力を認識できます。*

**トランスポートメカニズム**

MCPは様々なトランスポートメカニズムに対応しています。選択肢はStdio（ローカルのサブプロセス通信向け）とストリーム可能なHTTP（リモートサーバー向け）です。このモジュールではStdioトランスポートを実演します：

<img src="../../../translated_images/ja/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCPのトランスポート: リモートサーバー用のHTTP、ローカルプロセス用のStdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

ローカルプロセス向け。アプリは子プロセスとしてサーバーを起動し、標準入出力を介して通信します。ファイルシステムアクセスやコマンドラインツール用に便利です。

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

`@modelcontextprotocol/server-filesystem` サーバーは以下のツールを公開し、指定したディレクトリ内のみアクセス可能です：

| ツール | 説明 |
|------|-------------|
| `read_file` | 単一ファイルの内容を読み取る |
| `read_multiple_files` | まとめて複数ファイルを読み取る |
| `write_file` | ファイルを作成または上書き |
| `edit_file` | 対象を絞った検索置換編集を行う |
| `list_directory` | パス内のファイルとディレクトリを一覧表示 |
| `search_files` | パターンにマッチするファイルを再帰的に検索 |
| `get_file_info` | ファイルのメタデータ（サイズ、タイムスタンプ、権限など）を取得 |
| `create_directory` | ディレクトリ（親ディレクトリ含む）を作成 |
| `move_file` | ファイルやディレクトリの移動または名前変更 |

以下の図はStdioトランスポートの動作を示します—JavaアプリがMCPサーバーを子プロセスとして生成し、stdin/stdoutパイプを通じて通信し、ネットワークやHTTPは使いません：

<img src="../../../translated_images/ja/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdioトランスポートの動作例 — アプリがMCPサーバーを子プロセスとして起動し、stdin/stdoutパイプで通信します。*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) チャットで試そう:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) を開いて質問:
> - "Stdioトランスポートはどのように動作し、HTTPとはいつ使い分けるべき？"
> - "LangChain4jはMCPサーバーの子プロセスのライフサイクルをどう管理している？"
> - "AIにファイルシステムアクセスを許可する際のセキュリティ上の考慮点は？"

## The Agentic Module

MCPが標準化されたツールを提供する一方、LangChain4jの **agenticモジュール** はこれらのツールをオーケストレーションするエージェントを宣言的に構築する方法を提供します。`@Agent` アノテーションと `AgenticServices` によって、命令型コードではなくインターフェースでエージェントの振る舞いを定義できます。

このモジュールでは、**Supervisor Agent** パターンを探求します—「スーパーバイザー」エージェントがユーザーのリクエストに応じて動的にサブエージェントを呼び出す高度なagentic AIアプローチです。MCPによるファイルアクセス能力を持つサブエージェントを一つ組み込み、両者を組み合わせます。

agenticモジュールを使うには、以下のMaven依存関係を追加してください：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **注意:** `langchain4j-agentic` モジュールはコアのLangChain4jライブラリとは別のリリーススケジュールでバージョン管理(`langchain4j.mcp.version`)されています。

> **⚠️ 実験的:** `langchain4j-agentic` モジュールは**実験的**であり、今後変更される可能性があります。安定したAIアシスタント構築は `langchain4j-core` とカスタムツール（Module 04）が推奨されます。

## Running the Examples

### Prerequisites

- [Module 04 - Tools](../04-tools/README.md) を完了済み（このモジュールはカスタムツール概念を基にし、MCPツールと比較します）
- ルートディレクトリにAzure認証情報入りの `.env` ファイル（Module 01の `azd up` で作成）
- Java 21+、Maven 3.9+
- Node.js 16+ と npm（MCPサーバー用）

> **注意:** 環境変数をまだ設定していない場合は、[Module 01 - Introduction](../01-introduction/README.md) のデプロイ手順を参照してください（`azd up` により `.env` ファイルが自動作成されます）、または `.env.example` をルートにコピーし値を入力してください。

## Quick Start

**VS Codeで使用する場合:** エクスプローラーから任意のデモファイルを右クリックし **「Run Java」** を選択、または「実行とデバッグ」パネルの起動構成を使います（`.env` ファイルでAzure認証情報設定済みを確認）。

**Mavenで実行する場合:** 以下の例をコマンドラインから実行可能です。

### File Operations (Stdio)

ローカルのサブプロセスベースツールのデモです。

**✅ 前提条件不要** - MCPサーバーは自動的に起動されます。

**スタートスクリプトの使用（推奨）:**

スタートスクリプトはルートの `.env` ファイルの環境変数を自動読み込みします：

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**VS Code使用時:** `StdioTransportDemo.java` を右クリックして **「Run Java」** を選択（`.env` ファイル設定済みを確認）。

アプリはファイルシステムMCPサーバーを自動的に起動しローカルファイルを読み取ります。サブプロセスマネジメントが自動化されているのに注目してください。

**期待される出力:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agentパターン** は **柔軟** なagentic AIの形態です。スーパーバイザーはLLMを利用し、ユーザーのリクエストに応じてどのエージェントを呼び出すか自律的に決定します。次の例では、MCPによるファイルアクセスをLLMエージェントと組み合わせ、ファイル読み取りからレポート生成への監督されたワークフローを作成します。

デモでは `FileAgent` がMCPファイルシステムツールを使ってファイルを読み取り、`ReportAgent` は構造化されたレポートを作成します。レポートは1文の要約、3つの主要ポイント、推薦事項で構成されます。Supervisorがこの流れを自動でオーケストレーションします：

<img src="../../../translated_images/ja/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*スーパーバイザーはLLMを活用し、呼び出すエージェントと順序を決定—固定ルーティング不要。*

ファイルからレポートへの具体的なワークフローは以下の通りです：

<img src="../../../translated_images/ja/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgentがMCPツール経由でファイルを読み、ReportAgentが生の内容を構造的なレポートに変換。*

次のシーケンス図はSupervisorの完全なオーケストレーションを示します—MCPサーバー起動からSupervisorの自律的エージェント選択、stdio経由のツール呼び出し、最終レポート生成まで：

<img src="../../../translated_images/ja/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*スーパーバイザーは自律的にFileAgentを呼び出し（stdioでMCPサーバーを通じファイルを読み取り）、続けてReportAgentに構造化レポートを生成させる—各エージェントは共通のAgentic Scopeに結果を保存。*

各エージェントの出力は**Agentic Scope**（共有メモリ）に保存され、後続エージェントが前の結果にアクセス可能です。これによりMCPツールがエージェントワークフローにシームレスに統合できることを示しています—スーパーバイザーはファイルが*どのように*読まれるかを知らず、`FileAgent` が実行可能と知っているだけです。

#### Running the Demo

スタートスクリプトはルート `.env` ファイルの環境変数を自動読み込み：

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**VS Code使用時:** `SupervisorAgentDemo.java` を右クリックし **「Run Java」** を選択（`.env` 設定済みを確認）。

#### How the Supervisor Works

エージェントを構築する前に、MCPトランスポートをクライアントに接続し、`ToolProvider` にラップします。これによりMCPサーバーのツールがエージェントから利用可能になります：

```java
// トランスポートからMCPクライアントを作成する
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// クライアントをToolProviderとしてラップする — これによりMCPツールをLangChain4jに橋渡しする
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

この `mcpToolProvider` をMCPツールが必要なエージェントに注入できます：

```java
// ステップ1: FileAgentはMCPツールを使ってファイルを読み取ります
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // ファイル操作のためのMCPツールを持っています
        .build();

// ステップ2: ReportAgentは構造化されたレポートを生成します
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisorはファイル→レポートのワークフローを調整します
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 最終レポートを返します
        .build();

// Supervisorはリクエストに基づいてどのエージェントを呼び出すか決定します
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### How FileAgent Discovers MCP Tools at Runtime

疑問があるかもしれません：**`FileAgent` はnpmのファイルシステムツールをどのように使うことを知っている？** 答えは知りません — **LLMがツールスキーマを通じて実行時に理解します。**

`FileAgent` インターフェースは単なる**プロンプト定義**です。`read_file`、`list_directory` などのMCPツールの知識はハードコードされていません。エンドツーエンドでは以下のように動作します：
1. **サーバースポーン:** `StdioMcpTransport` が `@modelcontextprotocol/server-filesystem` npm パッケージを子プロセスとして起動します  
2. **ツール発見:** `McpClient` が `tools/list` JSON-RPC リクエストをサーバーに送信し、サーバーはツール名、説明、およびパラメータスキーマ（例：`read_file` — *「ファイルの完全な内容を読み取る」* — `{ path: string }`）を返します  
3. **スキーマ注入:** `McpToolProvider` がこれらの発見されたスキーマをラップし、LangChain4j に提供します  
4. **LLM 決定:** `FileAgent.readFile(path)` が呼ばれると、LangChain4j はシステムメッセージ、ユーザーメッセージ、**およびツールスキーマのリスト**を LLM に送ります。LLM はツール説明を読み取り、ツールコールを生成します（例：`read_file(path="/some/file.txt")`）  
5. **実行:** LangChain4j はツールコールをインターセプトし、それを MCP クライアント経由で Node.js のサブプロセスにルーティングし、結果を取得して LLM に返します  

これは上記で説明した [ツール発見](../../../05-mcp) メカニズムと同じですが、エージェントワークフローに特化して適用されています。`@SystemMessage` と `@UserMessage` アノテーションは LLM の振る舞いをガイドし、注入された `ToolProvider` は LLM に**機能**を提供します — LLM はこれら二つを実行時に紐づけます。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試す:** [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) を開き、次のように尋ねてみましょう：  
> - 「このエージェントはどのようにしてどの MCP ツールを呼び出すか知っているのか？」  
> - 「エージェントビルダーから ToolProvider を削除したらどうなるか？」  
> - 「ツールスキーマはどうやって LLM に渡されるのか？」

#### レスポンス戦略

`SupervisorAgent` を設定するとき、サブエージェントがタスクを完了した後にユーザーへ最終回答をどのように生成するかを指定します。以下の図は利用可能な3つの戦略を示しており — LAST は最終エージェントの出力を直接返し、SUMMARY はすべての出力を LLM で総合し、SCORED は元のリクエストに対して高スコアの方を選びます：

<img src="../../../translated_images/ja/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor が最終回答を生成するための3つの戦略 — 最終エージェントの出力、要約された回答、または最高評価の出力を選択できます。*

利用可能な戦略は以下の通りです：

| 戦略 | 説明 |
|----------|-------------|
| **LAST** | スーパーバイザーは最後に呼び出されたサブエージェントまたはツールの出力を返します。最終エージェントが完全な最終回答を生成するよう設計されている場合に有効です（例：研究パイプラインの「サマリーエージェント」）。 |
| **SUMMARY** | スーパーバイザーの内部言語モデル（LLM）を使い、すべてのやり取りとサブエージェントの出力を要約し、それを最終回答として返します。ユーザーに対して簡潔で総合的な答えを提供します。 |
| **SCORED** | システムは内部の LLM を使い LAST の応答と SUMMARY の要約を元のユーザーリクエストに照らしてスコア付けし、より高得点の出力を返します。 |

完全な実装は [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) を参照してください。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試す:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) を開いて質問してみましょう：  
> - 「スーパーバイザーはどのようにして呼び出すエージェントを決定するのか？」  
> - 「Supervisor と Sequential ワークフローパターンの違いは何か？」  
> - 「スーパーバイザーのプランニング動作はどうカスタマイズできるか？」

#### 出力の理解

デモを実行すると、スーパーバイザーが複数エージェントを調整する流れが構造的に示されます。各セクションの意味は以下の通りです：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**ヘッダー** はワークフローのコンセプトを紹介します：ファイル読取からレポート生成までの集中したパイプライン。

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**ワークフローダイアグラム** はエージェント間のデータフローを示します。各エージェントには特定の役割があります：  
- **FileAgent** は MCP ツールでファイルを読み込み、`fileContent` に生データを格納  
- **ReportAgent** はその内容を取り込み、`report` に構造化レポートを生成

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**ユーザーリクエスト** はタスクを示します。スーパーバイザーはこれを解析し、FileAgent → ReportAgent の順に呼び出すことを決定。

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**スーパーバイザーのオーケストレーション** は2ステップの流れを実演：  
1. **FileAgent** は MCP 経由でファイルを読み込み内容を格納  
2. **ReportAgent** は内容を受け取り構造化レポートを生成

ユーザーリクエストに基づき、スーパーバイザーは**自律的に**これらの決定を下しました。

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### エージェントモジュール機能の説明

この例は agentic モジュールのいくつかの高度な機能を示しています。Agentic Scope と Agent Listener に注目しましょう。

**Agentic Scope** はエージェントが `@Agent(outputKey="...")` を使って結果を共有メモリに格納する仕組みを示します。これにより：  
- 後続のエージェントが前の出力を利用可能  
- スーパーバイザーが最終回答を合成可能  
- 開発者が各エージェントの出力を検査可能

以下の図はファイルからレポートへのワークフローにおける共有メモリとしての Agentic Scope を示しています — FileAgent は `fileContent` キーで出力を書き込み、ReportAgent はそれを読み `report` に出力を書き込みます：

<img src="../../../translated_images/ja/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope は共有メモリとして機能 — FileAgent が `fileContent` に書き、ReportAgent がそれを読み `report` に書き込み、最終的にコードが結果を読み取ります。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgentからの生のファイルデータ
String report = scope.readState("report");            // ReportAgentからの構造化されたレポート
```
  
**Agent Listener** はエージェントの実行を監視・デバッグするための仕組みです。デモのステップごとの出力はエージェント呼び出しごとにフックする AgentListener から得ています：  
- **beforeAgentInvocation** - スーパーバイザーがエージェントを選択した際に呼ばれ、どのエージェントがなぜ選ばれたかを確認可能  
- **afterAgentInvocation** - エージェント完了時に呼び出され、結果を表示  
- **inheritedBySubagents** - true の場合、階層内のすべてのエージェントを監視

以下の図は Agent Listener のライフサイクル全体を示し、`onError` がエージェント実行中の失敗をどう扱うかも含みます：

<img src="../../../translated_images/ja/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listener は実行ライフサイクルにフックし、エージェントの開始、完了、エラー発生を監視します。*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // すべてのサブエージェントに伝播する
    }
};
```
  
スーパーバイザーパターンを超え、`langchain4j-agentic` モジュールは強力なワークフローパターンをいくつか提供します。以下の図は単純な直列パイプラインからヒューマンインザループの承認ワークフローまで全5パターンを示しています：

<img src="../../../translated_images/ja/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*エージェントの調整に使える5つのワークフローパターン — 単純な直列パイプラインからヒューマンインザループ承認ワークフローまで。*

| パターン | 説明 | ユースケース |
|---------|-------------|----------|
| **Sequential** | エージェントを順番に実行、出力が次に流れる | パイプライン：研究 → 分析 → レポート |
| **Parallel** | エージェントを同時に実行 | 独立タスク：天気＋ニュース＋株価 |
| **Loop** | 条件を満たすまで繰り返し | 品質スコアリング：スコア ≥ 0.8 まで改善 |
| **Conditional** | 条件に基づきルーティング | 分類 → 専門エージェントへルート |
| **Human-in-the-Loop** | 人間のチェックポイントを追加 | 承認ワークフロー、コンテンツレビュー |

## 重要な概念

MCP と agentic モジュールを実際に使ってみた今、それぞれの利用シーンをまとめましょう。

MCP 最大の利点の一つはその拡大中のエコシステムです。以下の図は単一のユニバーサルプロトコルが様々な MCP サーバーを AI アプリケーションに繋げる様子を示しています — ファイルシステムやデータベースアクセスから GitHub、メール、ウェブスクレイピングなど多様に対応：

<img src="../../../translated_images/ja/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP はユニバーサルプロトコルのエコシステムを形成 — MCP 対応サーバーは MCP 対応クライアントと連携し、アプリ間でツールを共有可能に。*

**MCP** は既存のツールエコシステムを活用したい場合、複数アプリで共有するツールを構築したい場合、サードパーティサービスを標準プロトコルで統合したい場合、あるいはコード変更なしにツール実装を差し替えたい場合に最適です。

**Agentic モジュール** は `@Agent` アノテーションによる宣言的エージェント定義、ワークフローのオーケストレーション（順序、ループ、並列）、インターフェースベースの設計好み、複数エージェントの出力共有管理をしたい場合に最適です。

**Supervisor エージェントパターン** はワークフローが事前に予測不可能で LLM に判断させたい場合、専門化エージェントが動的に調整される必要がある場合、異なる機能にルーティングする会話システムを構築したい場合、最も柔軟で適応的なエージェント動作を求める時に輝きます。

カスタム `@Tool` メソッド（モジュール04）とこのモジュールの MCP ツールの選択を助けるために、以下の比較は主要なトレードオフを示しています — カスタムツールはアプリ固有ロジックへの密結合と完全な型安全性を提供し、MCP ツールは標準化され再利用可能な統合を実現します：

<img src="../../../translated_images/ja/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*カスタム @Tool メソッドと MCP ツールの使い分け — アプリ固有ロジックにはカスタムツール、標準化統合には MCP ツール。*

## おめでとうございます！

LangChain4j for Beginners コース全5モジュールを修了しました！以下が基本チャットから MCP パワード agentic システムまでの学習の全体像です：

<img src="../../../translated_images/ja/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*全5モジュールを通じた学習の旅 — 基本チャットから MCP パワード agentic システムまで。*

このコースで学んだこと：

- メモリを持つ会話型 AI の構築方法（モジュール01）  
- タスク別のプロンプトエンジニアリングパターン（モジュール02）  
- ドキュメントに基づく応答（RAG）の実装（モジュール03）  
- カスタムツールを使った基本的な AI エージェント作成（モジュール04）  
- LangChain4j MCP と Agentic モジュールで標準化ツールを統合（モジュール05）

### 次に何を？

モジュール修了後は [Testing Guide](../docs/TESTING.md) を探索し、LangChain4j のテスト概念を実践してみましょう。

**公式リソース:**  
- [LangChain4j ドキュメント](https://docs.langchain4j.dev/) - 詳細なガイドと API 参照  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - ソースコードとサンプル  
- [LangChain4j チュートリアル](https://docs.langchain4j.dev/tutorials/) - 様々なユースケース向けのステップバイステップチュートリアル

ご受講ありがとうございました！

---

**ナビゲーション:** [← 前へ: モジュール04 - ツール](../04-tools/README.md) | [メインに戻る](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本ドキュメントはAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されています。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる場合があります。原文の言語で記載されたドキュメントが正式な情報源としてご利用ください。重要な情報については、専門の翻訳者による翻訳を推奨いたします。本翻訳の利用に起因するいかなる誤解や誤訳についても責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->