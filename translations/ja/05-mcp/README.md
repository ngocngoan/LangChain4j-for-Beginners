# モジュール 05: モデルコンテキストプロトコル (MCP)

## 目次

- [学習内容](../../../05-mcp)
- [MCPとは？](../../../05-mcp)
- [MCPの仕組み](../../../05-mcp)
- [エージェントモジュール](../../../05-mcp)
- [実例の実行](../../../05-mcp)
  - [前提条件](../../../05-mcp)
- [クイックスタート](../../../05-mcp)
  - [ファイル操作（Stdio）](../../../05-mcp)
  - [スーパーバイザーエージェント](../../../05-mcp)
    - [デモの実行](../../../05-mcp)
    - [スーパーバイザーの動作](../../../05-mcp)
    - [応答戦略](../../../05-mcp)
    - [出力の理解](../../../05-mcp)
    - [エージェントモジュールの機能説明](../../../05-mcp)
- [キーポイント](../../../05-mcp)
- [おめでとうございます！](../../../05-mcp)
  - [次のステップは？](../../../05-mcp)

## 学習内容

これまでに会話型AIを構築し、プロンプトを習得、文書に基づいた応答を実装し、ツールを備えたエージェントも作りました。しかし、それらのツールはすべて特定のアプリケーション向けにカスタム構築されたものでした。もし、誰でも作成して共有できる標準化されたツールのエコシステムにAIがアクセスできるならどうでしょう？このモジュールでは、Model Context Protocol (MCP) と LangChain4j のエージェントモジュールを用いて、それを実現する方法を学びます。まずはシンプルな MCP ファイルリーダーを紹介し、続いて Supervisor Agent パターンを使った高度なエージェントワークフローへの簡単な統合を示します。

## MCPとは？

Model Context Protocol (MCP) はまさにそれを提供します—AIアプリケーションが外部ツールを発見し利用するための標準的な方法です。各データソースやサービスに対してカスタム統合を書く代わりに、機能を一貫したフォーマットで公開する MCP サーバーに接続します。AIエージェントはこうしたツールを自動的に発見し利用できます。

下図は MCP なしの場合はすべての統合にカスタムのポイントツーポイント接続が必要だったのに対し、MCP では単一のプロトコルでアプリを任意のツールに接続できる違いを示します：

<img src="../../../translated_images/ja/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP以前：複雑なポイントツーポイントの統合。MCP後：単一プロトコル、無限の可能性。*

MCP は AI 開発における根本的な問題を解決します：すべての統合がカスタムであること。GitHubにアクセスしたい？カスタムコード。ファイルを読みたい？カスタムコード。データベースをクエリしたい？カスタムコード。そしてこれらの統合は他のAIアプリと互換性がありません。

MCPはこれを標準化します。MCPサーバーは、明確な説明とスキーマ付きでツールを公開します。任意の MCP クライアントが接続し、利用可能なツールを発見し活用できます。一度構築すれば、どこでも使えます。

次の図はこのアーキテクチャを示します—単一の MCP クライアント（あなたのAIアプリ）が複数の MCP サーバーに接続し、それぞれが標準プロトコルを通じて自分のツールセットを公開しています：

<img src="../../../translated_images/ja/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*モデルコンテキストプロトコルのアーキテクチャ — 標準化されたツールの発見と実行*

## MCPの仕組み

内部的には、MCP は層状のアーキテクチャを使います。あなたのJavaアプリケーション（MCPクライアント）は利用可能なツールを発見し、Transport層（StdioやHTTP）を通じてJSON-RPCリクエストを送信し、MCPサーバーは操作を実行して結果を返します。次の図はこのプロトコルの各層を分解しています：

<img src="../../../translated_images/ja/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCPの内部動作 — クライアントがツールを発見し、JSON-RPCメッセージを交換し、Transport層を通じて操作を実行。*

**サーバークライアントアーキテクチャ**

MCPはクライアントサーバーモデルを使います。サーバーはツール（ファイル読み込み、データベースへのクエリ、API呼び出し等）を提供します。クライアント（あなたのAIアプリ）はサーバーに接続し、ツールを使います。

LangChain4jでMCPを使うには、次のMaven依存関係を追加します。

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**ツールの発見**

クライアントが MCP サーバーに接続すると「どんなツールがありますか？」と尋ねます。サーバーは利用可能なツールのリストを説明とパラメータスキーマ付きで返答します。あなたのAIエージェントはユーザーのリクエストに基づいてどのツールを使うか決めることができます。下図はこのハンドシェイクを示し、クライアントが `tools/list` リクエストを送信し、サーバーが説明とパラメータスキーマ付きのツール一覧を返す様子を示します：

<img src="../../../translated_images/ja/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AIは起動時に利用可能なツールを発見し、どの機能を使うか判断可能に。*

**トランスポートメカニズム**

MCPは複数のトランスポートメカニズムをサポートします。主な選択肢はStdio（ローカルサブプロセス通信向け）とStreamable HTTP（リモートサーバー向け）です。このモジュールではStdioトランスポートを示します：

<img src="../../../translated_images/ja/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCPのトランスポートメカニズム：リモートサーバー向けHTTP、ローカルプロセス向けStdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

ローカルプロセス用。アプリがサーバーをサブプロセスとして生成し、標準入出力を介して通信します。ファイルシステムアクセスやコマンドラインツールに有用です。

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

`@modelcontextprotocol/server-filesystem` サーバーは指定したディレクトリにサンドボックス化された以下のツールを公開します：

| ツール | 説明 |
|------|-------------|
| `read_file` | 単一ファイルの内容を読み取る |
| `read_multiple_files` | 複数のファイルを一度に読み取る |
| `write_file` | ファイルを作成、または上書きする |
| `edit_file` | 指定したテキストの置換編集を行う |
| `list_directory` | 指定パスのファイルとディレクトリを一覧表示する |
| `search_files` | パターンに一致するファイルを再帰的に検索する |
| `get_file_info` | ファイルのメタデータ（サイズ、タイムスタンプ、パーミッション）を取得する |
| `create_directory` | ディレクトリを作成（親ディレクトリも含む） |
| `move_file` | ファイルまたはディレクトリを移動または名前変更する |

下図は Stdio トランスポートのランタイムでの動きを示します—Javaアプリは MCP サーバーを子プロセスとして起動し、stdin/stdoutパイプで通信、ネットワークやHTTPは関与しません：

<img src="../../../translated_images/ja/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdioトランスポートの動作—アプリがMCPサーバーを子プロセスとして生成し、stdin/stdoutパイプで通信。*

> **🤖 GitHub Copilot Chatで試してみよう:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) を開いて質問：
> - 「Stdioトランスポートはどう動作し、HTTPとはいつ使い分けるべきか？」
> - 「LangChain4jは起動したMCPサーバープロセスのライフサイクルをどう管理するか？」
> - 「AIにファイルシステムアクセスを許可するときのセキュリティ上の懸念は？」

## エージェントモジュール

MCPが標準化されたツールを提供するのに対し、LangChain4j の **エージェントモジュール** はそれらのツールをオーケストレーションするエージェントの宣言的構築手法を提供します。`@Agent` アノテーションと `AgenticServices` により、命令型コードではなくインターフェイスを通じてエージェントの挙動を定義できます。

このモジュールでは、ユーザーリクエストに応じてどのサブエージェントを呼び出すかを動的に決める高度なエージェントAIアプローチである **Supervisor Agent** パターンを探究します。さらに、サブエージェントの一つが MCP により強化されたファイルアクセス機能を持つ例を作ります。

エージェントモジュールを使うには、以下の Maven 依存関係を追加します：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注:** `langchain4j-agentic` モジュールはコアの LangChain4j ライブラリとは別スケジュールでバージョン管理されているため、バージョンプロパティ `langchain4j.mcp.version` を個別に使用しています。

> **⚠️ 実験的:** `langchain4j-agentic` モジュールは**実験段階**であり今後変更される可能性があります。安定的にAIアシスタントを構築する方法は引き続き `langchain4j-core` とカスタムツール（モジュール04を参照）です。

## 実例の実行

### 前提条件

- [モジュール04 - ツール](../04-tools/README.md)の修了（本モジュールはカスタムツール概念の上に構築し、MCPツールとの比較も行う）
- ルートディレクトリに `.env` ファイル（Module 01の `azd up` によるAzure認証情報作成済み）
- Java 21+、Maven 3.9+
- Node.js 16+ とnpm（MCPサーバー用）

> **注意:** 環境変数の設定がまだの場合は、[モジュール01 - はじめに](../01-introduction/README.md) のデプロイ手順を参照してください（`azd up` で `.env` ファイルが自動生成されます）、または `.env.example` をルートにコピーし値を設定してください。

## クイックスタート

**VS Codeの利用:** エクスプローラー内の任意のデモファイルを右クリックし **「Run Java」** を選ぶだけ、または実行とデバッグパネルの起動構成を使います（先に `.env` ファイルにAzure認証情報を設定してください）。

**Mavenの利用:** あるいは、下記の例を参考にコマンドラインから実行可能です。

### ファイル操作（Stdio）

ローカルサブプロセスベースのツールを実演します。

**✅ 前提条件不要** - MCPサーバーは自動で起動されます。

**スタートスクリプトを使う（推奨）**

これらのスクリプトはルートの `.env` ファイルから環境変数を自動ロードします：

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

**VS Codeの利用:** `StdioTransportDemo.java` を右クリックし **「Run Java」** を選ぶ（`.env` が設定されていることを確認）。

アプリケーションはファイルシステムMCPサーバーを自動で起動し、ローカルファイルを読み取ります。サブプロセスマネジメントが適切に行われているのに注目してください。

**期待される出力：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### スーパーバイザーエージェント

**Supervisor Agentパターン** は柔軟なエージェントAIの形態です。スーパーバイザーはLLMを使いユーザーリクエストに基づいて呼び出すエージェントを自律的に決定します。次の例では、MCPを用いたファイルアクセス機能とLLMエージェントを組み合わせ、ファイル読み込み→報告のワークフローをスーパーバイザーが自動制御します。

デモでは、`FileAgent` が MCPファイルシステムツールを使ってファイルを読み、`ReportAgent` が経営陣向けの要約（1文）、3つの重要ポイント、推奨事項からなる構造化レポートを生成します。スーパーバイザーがこの流れを自動でオーケストレーションします：

<img src="../../../translated_images/ja/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*スーパーバイザーはLLMを活用し、どのエージェントを順番に呼び出すかを決定—硬直的なルーティングは不要。*

ファイルからレポートまでのワークフローの具体像：

<img src="../../../translated_images/ja/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgentがMCPツールを介してファイルを読み込み、ReportAgentが生データを構造化レポートに変換。*

各エージェントは **Agentic Scope** （共有メモリ）に出力を保存し、後続エージェントが前の結果にアクセス可能にします。MCPツールがエージェントワークフローにシームレスに統合されていることを示しています—スーパーバイザーはファイルの読み込み方法を知らずとも、`FileAgent` に任せるだけで済みます。

#### デモの実行

スタートスクリプトはルートの `.env` ファイルから環境変数を自動ロードします：

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

**VS Codeの利用:** `SupervisorAgentDemo.java` を右クリックし **「Run Java」** （`.env` が設定済みであることを確認）。

#### スーパーバイザーの動作

エージェント構築前に、MCPトランスポートをクライアントに接続し、それを `ToolProvider` にラップします。これにより MCP サーバーのツールがエージェントに提供可能になります：

```java
// トランスポートからMCPクライアントを作成します
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// クライアントをToolProviderとしてラップします — これによりMCPツールがLangChain4jに橋渡しされます
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

こうして得た `mcpToolProvider` は MCP ツールを必要とする任意のエージェントへ注入できます：

```java
// ステップ1: FileAgentはMCPツールを使ってファイルを読み込みます
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // ファイル操作のためのMCPツールを持っています
        .build();

// ステップ2: ReportAgentは構造化されたレポートを生成します
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisorはファイルからレポートへのワークフローを調整します
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 最終レポートを返します
        .build();

// Supervisorはリクエストに基づいて呼び出すエージェントを決定します
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 応答戦略

`SupervisorAgent` の設定時に、サブエージェントの処理が完了したあとで、ユーザーへの最終回答の策定方法を指定します。下図は利用可能な3つの戦略を示します—LASTは最後のエージェントの出力を直接返し、SUMMARYは全ての出力をLLMで合成し、SCOREDは元のリクエストに対してより高評価の出力を返します：

<img src="../../../translated_images/ja/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*スーパーバイザーが最終応答を作成する3つの戦略—最後のエージェントの出力、合成した要約、ベストスコア発現の中から選択。*

利用可能な戦略は以下の通りです：

| 戦略 | 説明 |
|----------|-------------|
| **LAST** | スーパーバイザーは最後に呼び出されたサブエージェントまたはツールの出力をそのまま返します。ワークフローの最後のエージェントが最終的な完全回答を責任持って生成する場合に有効です（例えば、調査ワークフローの「サマリーエージェント」）。 |
| **SUMMARY** | スーパーバイザーが自身の内部LLMを使用して全やり取りとサブエージェント出力を要約し、それを最終応答として返します。クリーンで集約された回答をユーザーに提供します。 |
| **SCORED** | システムは内部LLMを使い、元のユーザーリクエストに対してLAST応答とSUMMARYの両方を評価し、高スコアを得た方を返します。 |
完全な実装は[SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)を参照してください。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試す:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)を開いて、次のように質問してください:
> - 「Supervisorはどのようにして呼び出すエージェントを決定していますか？」
> - 「SupervisorとSequentialワークフローパターンの違いは何ですか？」
> - 「Supervisorのプランニング動作をカスタマイズするにはどうすればよいですか？」

#### 出力内容の理解

デモを実行すると、Supervisorが複数のエージェントをどのように調整しているかの構造化された解説が表示されます。各セクションの意味は以下のとおりです。

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**ヘッダー**はワークフローのコンセプトを紹介します：ファイル読み込みからレポート生成までに特化したパイプラインです。

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

**ワークフローダイアグラム**はエージェント間のデータフローを示します。各エージェントは特定の役割を持っています：
- **FileAgent**はMCPツールを使ってファイルを読み取り、生の内容を`fileContent`に保存
- **ReportAgent**はその内容を受け取り、構造化されたレポートを`report`に生成

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**ユーザーリクエスト**にはタスク内容が示されています。Supervisorはこれを解析し、FileAgent → ReportAgentの呼び出しを判断します。

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

**Supervisorの調整**は2ステップのフローを実演します：
1. **FileAgent**がMCP経由でファイルを読み込み、内容を保存
2. **ReportAgent**が内容を受け取り、構造化レポートを生成

Supervisorはユーザーのリクエストを基に**自律的に**これらの判断を行いました。

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

この例はエージェントモジュールのいくつかの高度な機能を示しています。Agentic ScopeとAgent Listenerに注目しましょう。

**Agentic Scope**は`@Agent(outputKey="...")`を使ってエージェントが結果を保存する共有メモリを示します。これにより以下が可能になります：
- 後続のエージェントが先行エージェントの出力にアクセスできる
- Supervisorが最終的な応答を合成できる
- それぞれのエージェントが何を出力したか確認できる

下図はファイルからレポートへのワークフローでAgentic Scopeが共有メモリとして機能する様子を示しています。FileAgentは`fileContent`キーに出力を書き、ReportAgentはそれを読み取り`report`キーに自分の出力を書きます：

<img src="../../../translated_images/ja/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scopeは共有メモリとして機能します。FileAgentは`fileContent`を書き込み、ReportAgentは読み込んで`report`を書き込み、コードは最終結果を読み取ります。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgentからの生ファイルデータ
String report = scope.readState("report");            // ReportAgentからの構造化レポート
```

**Agent Listener**はエージェントの実行を監視・デバッグするための機能です。デモで見るステップごとの出力は、各エージェント呼び出し時にフックするAgentListenerからのものです：
- **beforeAgentInvocation** - Supervisorがエージェントを選んだ時に呼ばれ、どのエージェントが選ばれたかと理由がわかる
- **afterAgentInvocation** - エージェントの処理完了時に呼ばれ、その結果を表示
- **inheritedBySubagents** - trueのとき、階層内の全エージェントを監視

以下の図はAgent Listenerのライフサイクル全体を示し、エージェント実行の失敗時に`onError`がどのように処理するかも含みます：

<img src="../../../translated_images/ja/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listenerは実行ライフサイクルにフックして、エージェントが開始、完了、エラー発生したタイミングを監視します。*

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

Supervisorパターンを越えて、`langchain4j-agentic`モジュールは強力なワークフローパターンを複数提供しています。下図は単純な逐次パイプラインから人間の介在する承認ワークフローまでの5つのパターンを示しています：

<img src="../../../translated_images/ja/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*エージェントのオーケストレーションに使われる5つのワークフローパターン — 単純逐次処理から人間の介在する承認ワークフローまで。*

| パターン | 説明 | 利用例 |
|---------|-------------|----------|
| **Sequential** | エージェントを順に実行し、出力を次へ流す | パイプライン：調査 → 分析 → レポート |
| **Parallel** | エージェントを同時に実行 | 独立タスク：天気+ニュース+株価 |
| **Loop** | 条件達成まで繰り返す | 品質スコアリング：スコアが0.8以上になるまで改善 |
| **Conditional** | 条件に基づいてルーティング | 分類 → 専門エージェントへルート |
| **Human-in-the-Loop** | 人間のチェックポイントを追加 | 承認ワークフロー、コンテンツレビュー |

## 重要な概念

MCPとエージェントモジュールの実践を体験した今、それぞれのアプローチをいつ使うかをまとめましょう。

MCPの最大の利点の一つは拡張し続けるエコシステムです。下図は単一のユニバーサルプロトコルが、ファイルシステムやデータベースアクセスからGitHub、メール、ウェブスクレイピングまでの多様なMCPサーバーにAIアプリケーションを接続する様子を示します：

<img src="../../../translated_images/ja/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCPはユニバーサルプロトコルによるエコシステムを構築します。任意のMCP互換サーバーは任意のMCP互換クライアントと連携でき、アプリ間でツールの共有が可能です。*

**MCP**は既存のツールエコシステムを活用したい場合、複数のアプリで共有できるツールを構築したい場合、標準プロトコルでサードパーティサービスを統合したい場合、またはツールの実装をコード変更なしに切り替えたい場合に最適です。

**エージェントモジュール**は`@Agent`アノテーションで宣言的なエージェント定義を行いたい場合、ワークフローのオーケストレーション（逐次、ループ、並列）を必要とする場合、命令的コードよりインターフェースベースのエージェント設計を好む場合、複数エージェントが`outputKey`で出力を共有するケースに向いています。

**Supervisor Agentパターン**はワークフローの先読み不可の場合やLLMに判断させたい場合、複数の専門エージェントの動的な調整が必要な場合、能力別ルーティングを伴う対話システム構築に適しており、最も柔軟で適応的なエージェント動作を実現します。

モジュール04のカスタム`@Tool`メソッドと本モジュールのMCPツールの比較では、カスタムツールはアプリ固有ロジックに対する強固な結合と完全な型安全を提供し、MCPツールは標準化された再利用可能な統合を提供している点が主な違いです：

<img src="../../../translated_images/ja/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*カスタム@ToolメソッドとMCPツールの使い分け — アプリ固有の型安全なロジックにカスタムツール、標準統合でアプリ間共通利用にMCPツール。*

## おめでとうございます！

LangChain4j初心者コースの5つのモジュールをすべて修了しました！以下は基本的なチャットからMCP対応のエージェントシステムまでの学習の全体像です：

<img src="../../../translated_images/ja/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*全5モジュールを通じた学習の旅 — 基本チャットからMCP対応エージェントへ。*

このコースで学んだこと：

- メモリ付き対話型AIの構築方法（モジュール01）
- さまざまなタスク向けのプロンプトエンジニアリングパターン（モジュール02）
- RAGを使った文書に基づいた応答の接地（モジュール03）
- カスタムツールで基本的なAIエージェント（アシスタント）の作成（モジュール04）
- 標準ツールをLangChain4j MCPとAgenticモジュールで統合（モジュール05）

### 次に何をする？

モジュールを完了したら、[Testing Guide](../docs/TESTING.md)でLangChain4jのテスト概念を確認してみてください。

**公式リソース：**
- [LangChain4jドキュメント](https://docs.langchain4j.dev/) - 包括的なガイドとAPIリファレンス
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - ソースコードとサンプル
- [LangChain4jチュートリアル](https://docs.langchain4j.dev/tutorials/) - さまざまなユースケースのステップバイステップチュートリアル

このコースを修了していただき、ありがとうございました！

---

**ナビゲーション：** [← 前：モジュール04 - ツール](../04-tools/README.md) | [メインに戻る](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本ドキュメントはAI翻訳サービス「[Co-op Translator](https://github.com/Azure/co-op-translator)」を使用して翻訳されました。正確性を期しておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文のネイティブ言語によるドキュメントが権威ある情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨いたします。本翻訳の使用によって生じた誤解や誤訳については、一切の責任を負いかねますのでご了承ください。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->
