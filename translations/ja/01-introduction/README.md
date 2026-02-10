# モジュール 01: LangChain4j 入門

## 目次

- [学習内容](../../../01-introduction)
- [前提条件](../../../01-introduction)
- [コア問題の理解](../../../01-introduction)
- [トークンの理解](../../../01-introduction)
- [メモリの仕組み](../../../01-introduction)
- [LangChain4j の使用方法](../../../01-introduction)
- [Azure OpenAI インフラの展開](../../../01-introduction)
- [ローカルでアプリケーションを実行する](../../../01-introduction)
- [アプリケーションの使用方法](../../../01-introduction)
  - [ステートレスチャット（左パネル）](../../../01-introduction)
  - [ステートフルチャット（右パネル）](../../../01-introduction)
- [次のステップ](../../../01-introduction)

## 学習内容

クイックスタートを完了すると、プロンプトを送信して応答を得る方法がわかります。これは基礎ですが、本格的なアプリケーションにはそれ以上のものが必要です。このモジュールでは、コンテキストを記憶し状態を維持する会話型AIの構築を学びます。これは単なるデモと本番対応アプリケーションの違いです。

このガイド全体で Azure OpenAI の GPT-5.2 を使用します。高度な推論能力により、異なるパターンの挙動がよりはっきりと分かるからです。メモリを加えることで、その違いが明確になります。これにより、各コンポーネントがアプリケーションにもたらすものを理解しやすくなります。

両方のパターンを示す1つのアプリケーションを構築します：

**ステートレスチャット** - 各リクエストは独立しています。モデルは以前のメッセージを記憶しません。これはクイックスタートで使ったパターンです。

**ステートフル会話** - それぞれのリクエストに会話履歴を含めます。モデルは複数ターンに渡ってコンテキストを保持します。本番のアプリケーションが必要とするものです。

## 前提条件

- Azure サブスクリプション（Azure OpenAI アクセスあり）
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** 提供されている devcontainer には Java、Maven、Azure CLI、Azure Developer CLI (azd) が事前にインストールされています。

> **Note:** このモジュールでは Azure OpenAI 上の GPT-5.2 を使用します。`azd up` によってデプロイは自動的に設定されるため、コード内のモデル名は変更しないでください。

## コア問題の理解

言語モデルはステートレスです。各API呼び出しは独立しています。「私の名前はジョンです」と送信してから「私の名前は何ですか？」と尋ねると、モデルは直前に自己紹介されたことを知りません。すべてのリクエストを初めての会話のように扱います。

これは単純なQ&Aには問題ありませんが、実際のアプリケーションでは無意味です。カスタマーサービスボットは言った内容を覚えている必要があります。パーソナルアシスタントはコンテキストが必要です。複数ターンの会話にはメモリが必要です。

<img src="../../../translated_images/ja/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="状態なし対状態ありの会話" width="800"/>

*ステートレス（独立した呼び出し）とステートフル（コンテキスト認識）会話の違い*

## トークンの理解

会話に入る前に、トークンについて理解することが重要です。トークンは言語モデルが処理するテキストの基本単位です：

<img src="../../../translated_images/ja/token-explanation.c39760d8ec650181.webp" alt="トークンの説明" width="800"/>

*テキストがどのようにトークンに分割されるかの例 - 「I love AI!」は4つの別個の処理単位になる*

トークンはAIモデルがテキストを測定し処理する単位です。単語、句読点、スペースまでもトークンになり得ます。モデルには一度に処理できるトークンの上限があります（GPT-5.2の場合は合計400,000トークン、入力が最大272,000トークン、出力が最大128,000トークン）。トークンを理解することで会話の長さやコストを管理できます。

## メモリの仕組み

チャットメモリはステートレス問題を解決します。会話履歴を保持して、モデルに送信する前に関連する過去のメッセージを付加します。「私の名前は何ですか？」と尋ねると、システムは実際には全ての会話履歴を送り、モデルは「私の名前はジョンです」と以前に言ったことを参照できます。

LangChain4j はこの処理を自動化するメモリ実装を提供します。保持すべきメッセージ数を指定すれば、フレームワークがコンテキストウィンドウを管理します。

<img src="../../../translated_images/ja/memory-window.bbe67f597eadabb3.webp" alt="メモリウィンドウの概念" width="800"/>

*MessageWindowChatMemory は最近のメッセージのスライディングウィンドウを維持し、古いものを自動で削除*

## LangChain4j の使用方法

このモジュールはクイックスタートを拡張し、Spring Boot を組み込み、会話メモリを追加します。各部分の連携は以下の通りです：

**依存関係** - 二つの LangChain4j ライブラリを追加：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```
　
**チャットモデル** - Azure OpenAI を Spring Bean として設定 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```
　
ビルダーは `azd up` で設定された環境変数から資格情報を読み込みます。`baseUrl` に Azure エンドポイントを設定すると OpenAI クライアントが Azure OpenAI と連携します。

**会話メモリ** - MessageWindowChatMemory でチャット履歴を管理 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
　
`withMaxMessages(10)` で最後の10件のメッセージを保持。ユーザー・AIメッセージは型付きラッパーの `UserMessage.from(text)`、`AiMessage.from(text)` で追加。`memory.messages()` で履歴を取得しモデルに送信。会話IDごとに別々のメモリインスタンスを管理し、複数ユーザーが並列で会話可能。

> **🤖 GitHub Copilot Chat で試してみよう：** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) を開いて質問してみてください：
> - 「MessageWindowChatMemory はウィンドウが満杯になった時、どのメッセージを削除するかどうやって決めているの？」
> - 「インメモリではなくデータベースを使ったカスタムメモリストレージを実装できる？」
> - 「古い会話履歴を圧縮するための要約機能はどう追加すれば良い？」

ステートレスチャットのエンドポイントはメモリを完全に使わず、クイックスタートと同じく `chatModel.chat(prompt)` のみ。ステートフルエンドポイントはメモリにメッセージを追加し、履歴を取得し、それらをコンテキストとして毎回送信します。モデル設定は同じで使い分けはパターンのみです。

## Azure OpenAI インフラの展開

**Bash:**
```bash
cd 01-introduction
azd up  # サブスクリプションと場所を選択します（eastus2 推奨）
```
　
**PowerShell:**
```powershell
cd 01-introduction
azd up  # サブスクリプションとロケーションを選択します（eastus2 推奨）
```
　
> **Note:** `RequestConflict: Cannot modify resource ... provisioning state is not terminal` のタイムアウトエラーが出た場合は、単純に `azd up` を再度実行してください。Azure リソースはまだ展開中の可能性があり、再試行でリソースが最終状態になるのを待ち自動的に処理が完了します。

これにより：
1. GPT-5.2 と text-embedding-3-small モデルを含む Azure OpenAI リソースが展開されます
2. 資格情報を含む `.env` ファイルがプロジェクトルートに自動生成されます
3. 必要な全環境変数が設定されます

**展開に問題がありますか？** サブドメイン名の競合、Azure ポータルでの手動展開手順、モデル構成など詳細は [Infrastructure README](infra/README.md) を参照してください。

**展開成功を確認：**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```
　
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```
　
> **Note:** `azd up` コマンドは `.env` ファイルを自動生成します。必要に応じてファイルを手動で編集するか以下コマンドで再生成可能です：
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
　
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```
　

## ローカルでアプリケーションを実行する

**展開確認：**

Azure 資格情報が設定された `.env` ファイルがルートディレクトリにあることを確認：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```
　
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```
　

**アプリケーション起動：**

**オプション1：Spring Boot ダッシュボードを使用（VS Code ユーザー推奨）**

devcontainer に Spring Boot ダッシュボード拡張機能が含まれており、VS Code の左のアクティビティバーにある Spring Boot アイコンで管理画面が開けます。

ダッシュボードからは以下が可能です：
- ワークスペース内の全 Spring Boot アプリを一覧表示
- ワンクリックで起動・停止
- アプリケーションログのリアルタイム閲覧
- アプリの状態監視

「introduction」の横の再生ボタンをクリックするとこのモジュールを開始可能。または全モジュールを一度に起動できます。

<img src="../../../translated_images/ja/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot ダッシュボード" width="400"/>

**オプション2：シェルスクリプトを使用**

全Webアプリケーション（モジュール 01-04）を起動：

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
cd 01-introduction
./start.sh
```
　
**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```
　

両スクリプトはルートの `.env` ファイルから環境変数を自動ロードし、JARが無い場合はビルドも行います。

> **Note:** 全モジュールを起動前に手動ビルドする場合：
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
　

ブラウザで http://localhost:8080 を開きます。

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
　

## アプリケーションの使用方法

アプリケーションは2つのチャット実装を並べてウェブインターフェースで提供します。

<img src="../../../translated_images/ja/home-screen.121a03206ab910c0.webp" alt="アプリケーションホーム画面" width="800"/>

*シンプルチャット（ステートレス）と会話チャット（ステートフル）が並ぶダッシュボード*

### ステートレスチャット（左パネル）

まずはこちらを試してください。「私の名前はジョンです」と入力し、すぐに「私の名前は何ですか？」と尋ねると、モデルは覚えていません。各メッセージが独立しているためです。これが基本的な言語モデル統合の問題点、つまり会話のコンテキストがない状態を示しています。

<img src="../../../translated_images/ja/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="ステートレスチャット デモ" width="800"/>

*前のメッセージから名前を覚えていないAI*

### ステートフルチャット（右パネル）

同じやり取りをこちらで試してください。「私の名前はジョンです」から「私の名前は何ですか？」と尋ねると、今回は覚えています。違いは MessageWindowChatMemory で、会話履歴を保持し毎回のリクエストに含めています。これが本番会話型AIの動作方法です。

<img src="../../../translated_images/ja/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="ステートフルチャット デモ" width="800"/>

*会話の初めに言った名前を覚えているAI*

両パネルは同じ GPT-5.2 モデルを使用しており、違いはメモリの有無のみです。これによりメモリがアプリに何をもたらすか、そして実際のユースケースでなぜ不可欠なのかが明確です。

## 次のステップ

**次のモジュール:** [02-prompt-engineering - GPT-5.2 によるプロンプトエンジニアリング](../02-prompt-engineering/README.md)

---

**ナビゲーション:** [← 前へ: モジュール 00 - クイックスタート](../00-quick-start/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 02 - プロンプトエンジニアリング →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
この文書はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確性を期していますが、自動翻訳には誤りや不正確な箇所が含まれる場合があります。原文の原言語版が正式な情報源とみなされるべきです。重要な情報については専門の人間による翻訳を推奨します。本翻訳の使用により生じた誤解や誤訳について、当社は一切の責任を負いません。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->