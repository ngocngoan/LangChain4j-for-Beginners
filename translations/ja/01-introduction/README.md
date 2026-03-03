# Module 01: LangChain4j 入門

## 目次

- [動画ウォークスルー](../../../01-introduction)
- [学べること](../../../01-introduction)
- [前提条件](../../../01-introduction)
- [コア問題の理解](../../../01-introduction)
- [トークンの理解](../../../01-introduction)
- [メモリの仕組み](../../../01-introduction)
- [LangChain4j の利用方法](../../../01-introduction)
- [Azure OpenAI インフラの展開](../../../01-introduction)
- [アプリケーションのローカル実行](../../../01-introduction)
- [アプリケーションの使用方法](../../../01-introduction)
  - [ステートレスチャット（左パネル）](../../../01-introduction)
  - [ステートフルチャット（右パネル）](../../../01-introduction)
- [次のステップ](../../../01-introduction)

## 動画ウォークスルー

このモジュールの始め方を説明するライブセッションをご覧ください：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 学べること

クイックスタートでは、GitHub Models を使ってプロンプト送信、ツール呼び出し、RAG パイプラインの構築、ガードレールのテストを行いました。これらのデモは可能なことを示しました — 今回は Azure OpenAI と GPT-5.2 に切り替え、本格的なプロダクションアプリケーションの構築を始めます。このモジュールは、会話のコンテキストを記憶し状態を維持する会話型AIにフォーカスします — クイックスタートデモで使われていたが説明されていなかった概念です。

本ガイドでは Azure OpenAI の GPT-5.2 を通して利用します。高度な推論機能により、異なるパターンの挙動がより明確に分かります。メモリを追加すると、その違いがはっきり見えます。これにより、各コンポーネントがアプリケーションに何をもたらすか理解しやすくなります。

以下の2つのパターンを示すアプリケーションを構築します：

**ステートレスチャット** - 各リクエストは独立しています。モデルは前回のメッセージを記憶しません。クイックスタートで使ったのはこのパターンです。

**ステートフル会話** - 各リクエストに会話履歴を含みます。モデルは複数ターンの文脈を維持します。これがプロダクションアプリケーションで必要なパターンです。

## 前提条件

- Azure OpenAI へのアクセスがある Azure サブスクリプション
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI、Azure Developer CLI (azd) は提供された devcontainer に事前インストールされています。

> **注意：** 本モジュールは Azure OpenAI の GPT-5.2 を使用します。`azd up` で自動的に展開されるため、コード内のモデル名は変更しないでください。

## コア問題の理解

言語モデルはステートレスです。各 API コールは独立しています。「私の名前はジョンです」と送っても、次に「私の名前は何？」と聞いても、モデルは直前に自己紹介を受けたことを知りません。すべてのリクエストをその会話が初めてであるかのように扱います。

これは単純な Q&A には問題ありませんが、本物のアプリケーションには役に立ちません。カスタマーサービスボットはユーザーからの情報を記憶している必要があります。パーソナルアシスタントは文脈を必要とします。マルチターン会話はメモリを要求します。

以下の図は2つのアプローチを対比しています — 左は名前を忘れるステートレス呼び出し、右は ChatMemory が背後にあるステートフル呼び出しです。

<img src="../../../translated_images/ja/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ステートレス（独立した呼び出し）とステートフル（コンテキスト認識）会話の違い*

## トークンの理解

会話に入る前に、トークンの理解が重要です — 言語モデルが処理するテキストの基本単位：

<img src="../../../translated_images/ja/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*テキストがどのようにトークンに分割されるかの例 — 「I love AI!」は4つの別々の処理単位になる*

トークンは AI モデルがテキストを計測・処理する単位です。単語、句読点、スペースもトークンになり得ます。モデルには同時に処理できるトークン数の制限があります（GPT-5.2 は最大400,000トークン、入力最大272,000、出力最大128,000）。トークンの理解は会話の長さやコスト管理に役立ちます。

## メモリの仕組み

チャットメモリはステートレス問題を解決するため、会話履歴を保ちます。リクエストをモデルに送る前に、関連する過去のメッセージを前置きします。「私の名前は何？」と聞くと、実際にはこれまでの会話履歴全体を送り、モデルは「私の名前はジョンです」と言われていたことを参照できます。

LangChain4j はこの処理を自動化するメモリの実装を提供します。保持するメッセージ数を指定すれば、フレームワークがコンテキストウィンドウを管理します。下図は MessageWindowChatMemory が最新メッセージのスライディングウィンドウを維持する様子です。

<img src="../../../translated_images/ja/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory は最近のメッセージのスライディングウィンドウを維持し、古いものを自動削除*

## LangChain4j の利用方法

このモジュールはクイックスタートを拡張し、Spring Boot と会話メモリを統合します。構成は以下の通り：

**依存関係** — 2つの LangChain4j ライブラリを追加：

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

**チャットモデル** — Azure OpenAI を Spring Bean として設定 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

ビルダーは `azd up` で設定された環境変数から認証情報を読み込みます。`baseUrl` を Azure エンドポイントに設定することで、OpenAI クライアントは Azure OpenAI と連携します。

**会話メモリ** — MessageWindowChatMemory でチャット履歴をトラック ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` で直近10メッセージを保持します。ユーザーとAIのメッセージは型付きラッパーで追加：`UserMessage.from(text)` と `AiMessage.from(text)`。履歴は `memory.messages()` で取得しモデルへ送信。サービスは会話IDごとに別々のメモリインスタンスを保持し、複数ユーザーの同時チャットに対応します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試すには：** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) を開いて、以下を質問：
> - "MessageWindowChatMemory はウィンドウがいっぱいの時どのメッセージをドロップするかどう決めているの？"
> - "インメモリの代わりにデータベースを使ったカスタムメモリストレージは実装可能？"
> - "古い会話履歴を圧縮するために要約機能を追加するには？"

ステートレスチャットエンドポイントはメモリを完全にスキップし、クイックスタートと同じく `chatModel.chat(prompt)` のみ。ステートフルエンドポイントはメモリにメッセージを追加、履歴を取得し、そのコンテキストを含めてリクエストします。同じモデル設定ですが異なるパターンです。

## Azure OpenAI インフラの展開

**Bash:**
```bash
cd 01-introduction
azd up  # サブスクリプションと場所を選択します（eastus2 推奨）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # サブスクリプションと場所を選択してください（eastus2がおすすめ）
```

> **注意:** タイムアウトエラー (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) が出た場合は、単純に `azd up` を再実行してください。Azure リソースはまだプロビジョニング中の可能性があり、再試行でリソースが確定状態になると展開が完了します。

これで以下が行われます：
1. GPT-5.2 と text-embedding-3-small モデルを備えた Azure OpenAI リソースを展開
2. プロジェクトルートに `.env` ファイルを自動生成（認証情報入り）
3. 必要な環境変数をすべて設定

**展開に問題がある場合**は [Infrastructure README](infra/README.md) を参照してください。サブドメイン名の衝突、Azure ポータルでの手動デプロイ手順、モデル構成の案内を含みます。

**展開成功の確認：**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```

> **注意:** `azd up` コマンドは自動で `.env` ファイルを生成します。後で更新が必要な場合は `.env` を直接編集するか、以下のコマンドで再生成可能です：
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## アプリケーションのローカル実行

**展開の確認：**

Azure 認証情報を含む `.env` ファイルがルートディレクトリに存在していることを確認し、モジュールディレクトリ (`01-introduction/`) で以下を実行：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**アプリケーションの起動：**

**オプション1: Spring Boot Dashboard の利用（VS Code ユーザーに推奨）**

devcontainer には Spring Boot Dashboard 拡張機能が含まれており、Spring Boot アプリを視覚的に管理できます。VS Code 左側のアクティビティバーにある Spring Boot アイコンでアクセス可能です。

Spring Boot Dashboard から：
- ワークスペース内のすべての Spring Boot アプリを確認
- ワンクリックで起動/停止
- リアルタイムでログ表示
- アプリの状態監視

"introduction" の横の再生ボタンをクリックするだけでこのモジュールを起動、またはすべてのモジュールを一括起動できます。

<img src="../../../translated_images/ja/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code の Spring Boot Dashboard — すべてのモジュールをまとめて起動、停止、監視可能*

**オプション2: シェルスクリプト利用**

すべてのウェブアプリ（モジュール01-04）を起動:

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

このモジュールだけ起動:

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

両スクリプトはルートの `.env` ファイルから環境変数を自動読み込みし、JAR がなければビルドも行います。

> **注意:** 起動前にすべてのモジュールを手動ビルドしたい場合：
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

ブラウザで http://localhost:8080 を開いてください。

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

アプリケーションは2つのチャット実装を並べて Web インターフェイスで提供します。

<img src="../../../translated_images/ja/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*シンプルチャット（ステートレス）と会話チャット（ステートフル）の両オプションを表示するダッシュボード*

### ステートレスチャット（左パネル）

まずはこちらを試してください。「私の名前はジョンです」と伝え、直後に「私の名前は何ですか？」と聞いてみましょう。モデルは覚えていません。各メッセージが独立しているためです。これは基本的な言語モデル統合のコア問題 — 会話の文脈がないことを示します。

<img src="../../../translated_images/ja/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AIは前のメッセージからあなたの名前を覚えていません*

### ステートフルチャット（右パネル）

同じ流れをこちらでも試します。「私の名前はジョンです」と伝え、「私の名前は何ですか？」と聞くと、今回は覚えています。違いは MessageWindowChatMemory です — 会話履歴を維持し、それをリクエストに含めます。これが本番の会話 AI の動作方法です。

<img src="../../../translated_images/ja/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AIは会話の初めにあなたの名前を覚えています*

両パネルとも同じ GPT-5.2 モデルを使います。違うのはメモリだけです。これによりメモリがアプリに何をもたらし、実利用でなぜ不可欠かが明瞭になります。

## 次のステップ

**次のモジュール：** [02-prompt-engineering - GPT-5.2 によるプロンプトエンジニアリング](../02-prompt-engineering/README.md)

---

**ナビゲーション：** [← 前：Module 00 - クイックスタート](../00-quick-start/README.md) | [メインに戻る](../README.md) | [次：Module 02 - プロンプトエンジニアリング →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されました。正確性を期していますが、自動翻訳には誤りや不正確な箇所が含まれる場合があります。原文の母国語版が正式な情報源とみなされます。重要な情報については、専門の人間による翻訳をお勧めします。本翻訳の利用により生じた誤解や誤訳について、当方は一切の責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->