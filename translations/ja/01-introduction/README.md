# モジュール 01: LangChain4j入門

## 目次

- [ビデオウォークスルー](../../../01-introduction)
- [学習内容](../../../01-introduction)
- [前提条件](../../../01-introduction)
- [コア問題の理解](../../../01-introduction)
- [トークンの理解](../../../01-introduction)
- [メモリの仕組み](../../../01-introduction)
- [LangChain4jの利用方法](../../../01-introduction)
- [Azure OpenAIインフラのデプロイ](../../../01-introduction)
- [ローカルでのアプリケーション実行](../../../01-introduction)
- [アプリケーションの使用方法](../../../01-introduction)
  - [ステートレスチャット (左パネル)](../../../01-introduction)
  - [ステートフルチャット (右パネル)](../../../01-introduction)
- [次のステップ](../../../01-introduction)

## ビデオウォークスルー

このモジュールの開始方法を説明するライブセッションをご覧ください：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 学習内容

クイックスタートを完了すると、プロンプトを送信し応答を受け取る方法がわかります。それが基礎ですが、本格的なアプリケーションにはもっと必要です。このモジュールでは、文脈を記憶し状態を維持する会話型AIの構築方法を学びます。これは一回限りのデモと本番用アプリケーションの大きな違いです。

本ガイドではAzure OpenAIのGPT-5.2を使用します。高度な推論能力により、さまざまなパターンの挙動の違いが明確にわかります。メモリを追加すると、その違いがはっきり見えるため、それぞれのコンポーネントがアプリケーションに何をもたらすかを理解しやすくなります。

以下の2つのパターンを示すアプリケーションを構築します：

**ステートレスチャット** - 各リクエストは独立しています。モデルは前のメッセージを記憶しません。クイックスタートで使用したパターンです。

**ステートフル会話** - 各リクエストに会話履歴が含まれています。モデルは複数ターンにわたり文脈を維持します。本番アプリケーションに必要なパターンです。

## 前提条件

- Azureサブスクリプション（Azure OpenAIアクセス権付き）
- Java 21、Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注:** Java、Maven、Azure CLI、Azure Developer CLI (azd) は提供されているdevcontainerにプリインストールされています。

> **注:** このモジュールはAzure OpenAIでGPT-5.2を使用します。`azd up`によってデプロイが自動設定されるため、コード内のモデル名は変更しないでください。

## コア問題の理解

言語モデルはステートレスです。各API呼び出しは独立しています。「私の名前はジョンです」と送信してから「私の名前は何ですか？」と聞いても、モデルは自分が自己紹介したことを認識していません。すべてのリクエストを、まるで初めての会話であるかのように扱います。

これは単純なQ&Aには問題ありませんが、本格的なアプリケーションには使えません。カスタマーサービスボットはあなたが話したことを覚えている必要があります。パーソナルアシスタントは文脈が必要です。マルチターンの会話にはメモリが必須です。

<img src="../../../translated_images/ja/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ステートレス（独立呼び出し）とステートフル（文脈認識）会話の違い*

## トークンの理解

会話に進む前に、言語モデルが処理する基本単位であるトークンを理解することが重要です：

<img src="../../../translated_images/ja/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*テキストがどのようにトークンに分割されるかの例 - "I love AI!" は4つの処理単位になる*

トークンはAIモデルがテキストを測定・処理する単位です。単語、句読点、スペースさえもトークンとなることがあります。モデルには一度に処理できるトークン数の制限があります（GPT-5.2では最大400,000トークン、入力は最大272,000トークン、出力は最大128,000トークン）。トークンを理解することで、会話の長さやコストを管理しやすくなります。

## メモリの仕組み

チャットメモリはステートレスの問題を会話履歴を保持することで解決します。リクエストをモデルに送る前に、フレームワークが関連する過去のメッセージを先頭に付加します。「私の名前は何ですか？」と聞くと、実際には会話全履歴を送信し、その中に「私の名前はジョンです」と言った履歴が含まれているためモデルが認識できます。

LangChain4jはこの処理を自動化するメモリ実装を提供します。保持するメッセージ数を選択でき、フレームワークが文脈ウィンドウを管理します。

<img src="../../../translated_images/ja/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemoryは最近のメッセージのスライディングウィンドウを維持し、古いメッセージは自動的に破棄*

## LangChain4jの利用方法

本モジュールはクイックスタートを拡張し、Spring Bootと会話メモリを統合しています。構成要素の関係は以下の通りです：

**依存関係** - 2つのLangChain4jライブラリを追加：

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

**チャットモデル** - Azure OpenAIをSpring Beanとして設定 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java))：

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

ビルダーは`azd up`によって設定される環境変数からクレデンシャルを読み込みます。`baseUrl`をAzureのエンドポイントに設定すると、OpenAIクライアントがAzure OpenAIで動作します。

**会話メモリ** - MessageWindowChatMemoryでチャット履歴を追跡 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java))：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)`で直近10メッセージを保持。ユーザーメッセージとAIメッセージは型付きラッパー`UserMessage.from(text)`, `AiMessage.from(text)`で追加。履歴は`memory.messages()`で取得しモデルに送信します。サービスは会話IDごとに別々のメモリインスタンスを保持し、複数ユーザーの同時チャットを可能にします。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) チャットで試す:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)を開き、以下の質問をしてみましょう:
> - MessageWindowChatMemoryはウィンドウが満杯になるとどのメッセージを破棄するかどうやって決めていますか？
> - メモリストレージをインメモリではなくデータベースでカスタム実装できますか？
> - 古い会話履歴を圧縮するために要約を追加するにはどうすればよいですか？

ステートレスチャットのエンドポイントはメモリを完全にスキップし、クイックスタート同様`chatModel.chat(prompt)`を呼びます。ステートフルエンドポイントはメモリにメッセージを追加し履歴を取得、各リクエストにその文脈を含めます。同じモデル構成で異なるパターンを実現しています。

## Azure OpenAIインフラのデプロイ

**Bash:**
```bash
cd 01-introduction
azd up  # サブスクリプションとロケーションを選択してください（eastus2を推奨）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # サブスクリプションとロケーションを選択してください（eastus2を推奨）
```

> **注:** タイムアウトエラー（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`）が発生した場合は、単に再度`azd up`を実行してください。Azureリソースはまだプロビジョニング中の場合があり、リトライによりリソースが状態遷移を完了してデプロイが完了します。

これにより：
1. GPT-5.2 と text-embedding-3-small モデルを持つ Azure OpenAI リソースがデプロイされる
2. プロジェクトルートに認証情報入りの `.env` ファイルが自動生成される
3. 必要なすべての環境変数が設定される

**デプロイに問題がありますか？** サブドメイン名の競合、手動Azure Portalデプロイ手順、モデル設定ガイドなど詳細なトラブルシューティングは[Infrastructure README](infra/README.md)を参照してください。

**デプロイ完了を確認：**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEYなどを表示する必要があります。
```

> **注:** `azd up`は`.env`ファイルを自動生成します。あとで更新する必要があれば、手動で`.env`を編集するか、以下で再生成可能です：
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

## ローカルでのアプリケーション実行

**デプロイを確認：**

Azure認証情報が含まれた`.env`ファイルがプロジェクトルートに存在することを確認：

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションを起動：**

**オプション1: Spring Boot Dashboardを使用 (VS Codeユーザー推奨)**

DevコンテナにはSpring Boot Dashboard拡張機能が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Code左側のアクティビティバーのSpring Bootアイコンから開けます。

Dashboardでは：
- ワークスペース内の全Spring Bootアプリを一覧表示
- ワンクリックで起動/停止可能
- リアルタイムのアプリケーションログを閲覧
- アプリのステータス監視

"introduction"の横の再生ボタンを押すだけでこのモジュールを起動できます。すべてのモジュールを一括起動も可能です。

<img src="../../../translated_images/ja/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション2: シェルスクリプトを使用**

すべてのWebアプリ（モジュール01-04）を起動：

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

またはこのモジュールのみを起動：

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

どちらのスクリプトもルートの`.env`ファイルから環境変数を自動読み込みし、JARがなければビルドします。

> **注:** すべてのモジュールを手動でビルドしてから起動したい場合は：
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

ブラウザで http://localhost:8080 にアクセスします。

**停止：**

**Bash:**
```bash
./stop.sh  # このモジュールのみ
# または
cd .. && ./stop-all.sh  # 全てのモジュール
```

**PowerShell:**
```powershell
.\stop.ps1  # このモジュールのみ
# または
cd ..; .\stop-all.ps1  # すべてのモジュール
```

## アプリケーションの使用方法

アプリは2つのチャット実装が左右に並ぶWebインターフェイスを提供します。

<img src="../../../translated_images/ja/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*シンプルチャット（ステートレス）と会話チャット（ステートフル）の両方のオプションを表示するダッシュボード*

### ステートレスチャット (左パネル)

まずこちらを試してください。「私の名前はジョンです」と訊いてからすぐに「私の名前は何ですか？」と訊きます。モデルは覚えていません。なぜなら各メッセージが独立しているためです。基本的な言語モデル統合の問題を示しています—会話の文脈はありません。

<img src="../../../translated_images/ja/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AIは前のメッセージで伝えた名前を覚えていない*

### ステートフルチャット (右パネル)

同じシーケンスをこちらで試してください。「私の名前はジョンです」と訊き、その後「私の名前は何ですか？」と訊きます。今回は覚えています。違いはMessageWindowChatMemoryです—会話履歴を維持し、それをリクエストに含めます。本番の会話型AIはこの方法で動きます。

<img src="../../../translated_images/ja/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AIは会話の初めに伝えた名前を覚えている*

両パネルは同じGPT-5.2モデルを使用しています。唯一の違いはメモリです。これによりメモリがアプリケーションに何をもたらすか、また実用上なぜ必須なのかが明確になります。

## 次のステップ

**次のモジュール:** [02-prompt-engineering - GPT-5.2によるプロンプトエンジニアリング](../02-prompt-engineering/README.md)

---

**ナビゲーション:** [← 前へ: モジュール 00 - クイックスタート](../00-quick-start/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 02 - プロンプトエンジニアリング →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されています。正確性には努めておりますが、自動翻訳には誤りや不正確な箇所が含まれる可能性があることをご承知ください。原文は常に権威ある情報源としてご参照ください。重要な情報については専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や誤訳についても責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->
