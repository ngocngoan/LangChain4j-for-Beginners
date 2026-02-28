# モジュール 01: LangChain4j のはじめ方

## 目次

- [ビデオウォークスルー](../../../01-introduction)
- [学習内容](../../../01-introduction)
- [前提条件](../../../01-introduction)
- [コア問題の理解](../../../01-introduction)
- [トークンの理解](../../../01-introduction)
- [メモリの仕組み](../../../01-introduction)
- [LangChain4j の利用方法](../../../01-introduction)
- [Azure OpenAI インフラのデプロイ](../../../01-introduction)
- [ローカルでのアプリケーション実行](../../../01-introduction)
- [アプリケーションの使い方](../../../01-introduction)
  - [ステートレスチャット（左パネル）](../../../01-introduction)
  - [ステートフルチャット（右パネル）](../../../01-introduction)
- [次のステップ](../../../01-introduction)

## ビデオウォークスルー

このモジュールのはじめ方を説明するライブセッションをご覧ください：[LangChain4j のはじめ方 - ライブセッション](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## 学習内容

クイックスタートを完了すると、プロンプトの送信と応答の受信の方法がわかります。これは基礎ですが、実際のアプリケーションにはそれ以上のものが必要です。このモジュールでは、コンテキストを覚え、状態を維持する会話型AIの構築方法を学びます。これが、ワンショットのデモと本番対応のアプリケーションの違いです。

このガイドでは、Azure OpenAI の GPT-5.2 を使用します。このモデルは高度な推論機能を持っているため、異なるパターンの挙動の違いがより明確にわかります。メモリを追加すると、その違いがはっきりします。これにより、各コンポーネントがアプリケーションにもたらすものを理解しやすくなります。

両方のパターンを示すアプリケーションを作成します：

**ステートレスチャット** - 各リクエストが独立しています。モデルは前のメッセージを記憶しません。これはクイックスタートで使ったパターンです。

**ステートフル会話** - 各リクエストに会話履歴が含まれます。モデルは複数ターンのコンテキストを維持します。これが本番レベルのアプリケーションに必要なものです。

## 前提条件

- Azure OpenAI にアクセスできる Azure サブスクリプション
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI、および Azure Developer CLI (azd) は提供された devcontainer に事前インストールされています。

> **注意：** このモジュールは Azure OpenAI 上の GPT-5.2 を使用します。デプロイは `azd up` で自動設定されますので、コードのモデル名を変更しないでください。

## コア問題の理解

言語モデルはステートレスです。各API呼び出しは独立しています。もし「私の名前はジョンです」と送った後に「私の名前は？」と聞いても、モデルには自己紹介したことが伝わっていません。すべてのリクエストを初めての会話のように扱います。

これは単純なQ&Aには問題ありませんが、実際のアプリケーションには役に立ちません。カスタマーサービスボットは何を言ったか覚えている必要があります。パーソナルアシスタントはコンテキストが必要です。複数ターンの会話にはメモリが不可欠です。

<img src="../../../translated_images/ja/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="ステートレス vs ステートフル会話" width="800"/>

*ステートレス（独立した呼び出し）とステートフル（コンテキスト認識）会話の違い*

## トークンの理解

会話に入る前に、トークンとは言語モデルが処理するテキストの基本単位であることを理解することが重要です：

<img src="../../../translated_images/ja/token-explanation.c39760d8ec650181.webp" alt="トークンの説明" width="800"/>

*テキストがどのようにトークンに分解されるかの例 - "I love AI!" は 4 つの処理単位になる*

トークンはAIモデルがテキストを測定し処理する単位です。単語、句読点、スペースもトークンです。モデルには一度に処理できるトークンの上限があります（GPT-5.2は最大400,000トークン、272,000入力トークン＋128,000出力トークン）。トークンの理解は、会話の長さやコスト管理に役立ちます。

## メモリの仕組み

チャットメモリは、ステートレス問題を会話履歴の維持で解決します。リクエストをモデルに送る前に、フレームワークは関連する過去のメッセージを先頭に付け加えます。「私の名前は？」と尋ねると、実際には全会話履歴が送信され、モデルは「私の名前はジョン」と以前に言ったことを認識できます。

LangChain4j はこれを自動処理するメモリ実装を提供します。保持するメッセージ数を選択し、フレームワークがコンテキストウィンドウを管理します。

<img src="../../../translated_images/ja/memory-window.bbe67f597eadabb3.webp" alt="メモリウィンドウの概念" width="800"/>

*MessageWindowChatMemory は最近のメッセージのスライディングウィンドウを維持し、古いものは自動的に削除します*

## LangChain4j の利用方法

このモジュールはクイックスタートを拡張し、Spring Boot と会話メモリを統合しています。全体は以下のように構成されています：

**依存関係** - ２つの LangChain4j ライブラリを追加します：

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

**チャットモデル** - Azure OpenAI を Spring Bean として設定([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

ビルダーは `azd up` で設定される環境変数から認証情報を読み込みます。`baseUrl` に Azure エンドポイントを指定することで、OpenAI クライアントが Azure OpenAI と連携可能になります。

**会話メモリ** - MessageWindowChatMemory でチャット履歴を追跡 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` により直近10メッセージを保持します。`UserMessage.from(text)` と `AiMessage.from(text)` でユーザーとAIのメッセージを追加し、`memory.messages()` で履歴を取得してモデルに渡します。サービスは会話IDごとにメモリインスタンスを管理するため、複数ユーザーが同時にチャット可能です。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat で試してみよう:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) を開いて以下を質問してください：
> - 「MessageWindowChatMemory はウィンドウがいっぱいの時にどのメッセージを削除するかどう決めていますか？」
> - 「メモリの保存をメモリ内ではなくデータベースでカスタム実装できますか？」
> - 「古い会話履歴を圧縮するサマリー機能をどのように追加しますか？」

ステートレスチャットのエンドポイントはメモリを完全に省略し、`chatModel.chat(prompt)` のみでクイックスタートと同じです。ステートフルエンドポイントはメッセージをメモリに追加し、履歴を取得してそれをコンテキストとしてリクエストに含めます。同じモデル設定で、異なるパターンを実装しています。

## Azure OpenAI インフラのデプロイ

**Bash:**
```bash
cd 01-introduction
azd up  # サブスクリプションとロケーションを選択します（eastus2推奨）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # サブスクリプションと場所を選択してください（eastus2がおすすめ）
```


> **注意：** タイムアウトエラー（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`）が発生した場合は、単に `azd up` を再度実行してください。Azureリソースはまだプロビジョニング中の可能性があり、リトライでリソースが最終状態になるとデプロイが完了します。

この操作で以下を行います：
1. GPT-5.2 と text-embedding-3-small モデルを備えた Azure OpenAI リソースをデプロイ
2. プロジェクトルートに認証情報付き `.env` ファイルを自動生成
3. 必要な環境変数をすべて設定

**デプロイ問題がある場合は？** [Infrastructure README](infra/README.md) を参照してください。サブドメイン名の衝突、Azure Portalでの手動デプロイ手順、モデル設定の案内があります。

**デプロイ成功の確認：**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```


> **注意：** `azd up` は `.env` ファイルを自動生成します。あとで更新が必要な場合は、手動編集するか以下で再生成してください：
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


## ローカルでのアプリケーション実行

**デプロイの検証：**

ルートディレクトリに Azure 認証情報が入った `.env` ファイルが存在することを確認します：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```


**アプリケーション起動：**

**オプション1: Spring Boot Dashboard（VS Code ユーザー推奨）**

devcontainer には Spring Boot Dashboard 拡張機能がインストールされています。これはSpring Bootアプリケーションを視覚的に管理できるインターフェースで、VS Code の左のアクティビティバーで見つけられます（Spring Boot アイコン）。

Spring Boot Dashboard からは：
- ワークスペース内のすべての Spring Boot アプリケーションを表示
- ワンクリックでアプリの起動／停止
- アプリケーションログのリアルタイム表示
- アプリの状態監視

「introduction」の横の再生ボタンを押してこのモジュールを起動するか、すべてのモジュールを一括起動できます。

<img src="../../../translated_images/ja/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション2: シェルスクリプトによる起動**

全モジュール (01-04) の Web アプリを起動：

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


このモジュール単体で起動：

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


両スクリプトはルートの `.env` ファイルから自動的に環境変数を読み込み、JARがなければビルドも実行します。

> **注意：** もし起動前に全モジュールを手動ビルドしたい場合は：
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


ブラウザで http://localhost:8080 を開いてください。

**停止する場合：**

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

アプリケーションは２つのチャット実装を並べて表示するウェブインターフェイスを提供します。

<img src="../../../translated_images/ja/home-screen.121a03206ab910c0.webp" alt="アプリケーションホーム画面" width="800"/>

*シンプルチャット（ステートレス）と会話チャット（ステートフル）の両方のオプションが表示されるダッシュボード*

### ステートレスチャット（左パネル）

まずはこちらをお試しください。「私の名前はジョンです」と言い、すぐに「私の名前は？」と尋ねてみてください。モデルは記憶していないため答えられません。これは言語モデル統合の基本問題、すなわち会話のコンテキストがないことを示しています。

<img src="../../../translated_images/ja/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="ステートレスチャットデモ" width="800"/>

*AIは前のメッセージから名前を覚えていません*

### ステートフルチャット（右パネル）

同じ順序をこちらでも試してください。「私の名前はジョンです」と言い、「私の名前は？」と尋ねると、今回は覚えています。違いは MessageWindowChatMemory にあります。会話履歴を保持しており、各リクエストに含めて送信するためです。これが本番の会話AIの動作です。

<img src="../../../translated_images/ja/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="ステートフルチャットデモ" width="800"/>

*AIは会話の早い段階で入力した名前を覚えています*

両パネルとも同じ GPT-5.2 モデルを使用しています。違いはメモリの有無だけです。これによりメモリがアプリに何をもたらし、本番環境でなぜ不可欠かが明確になります。

## 次のステップ

**次のモジュール：** [02-prompt-engineering - GPT-5.2 でのプロンプトエンジニアリング](../02-prompt-engineering/README.md)

---

**ナビゲーション：** [← 前へ: モジュール 00 - クイックスタート](../00-quick-start/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 02 - プロンプトエンジニアリング →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確性の確保に努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。正確な情報は原文の言語による原稿を公式な情報源としてご確認ください。重要な情報については、専門の人間による翻訳を推奨いたします。本翻訳の利用により生じたいかなる誤解や誤訳についても責任を負いかねますので、あらかじめご了承ください。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->