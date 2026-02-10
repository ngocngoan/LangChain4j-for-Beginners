# Module 02: GPT-5.2によるプロンプトエンジニアリング

## 目次

- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [LangChain4jの活用方法](../../../02-prompt-engineering)
- [コアパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの利用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低い意欲 vs 高い意欲](../../../02-prompt-engineering)
  - [タスク実行（ツールプリエンブル）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化分析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [段階的推論](../../../02-prompt-engineering)
  - [制約付き出力](../../../02-prompt-engineering)
- [本当に学んでいること](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## 学習内容

前のモジュールでは、メモリが会話型AIを可能にする仕組みを学び、GitHub Modelsを使った基本的な対話を体験しました。今回は質問の仕方、つまりプロンプトそのものの構造に焦点を当てます。Azure OpenAIのGPT-5.2を使って、どのようにプロンプトを構築するかが応答の品質に大きな影響を与えることを理解します。

GPT-5.2は推論制御を導入しているため、モデルに回答前の思考量を指定できます。これにより、異なるプロンプト戦略の違いが明確になり、それぞれの使いどころが理解しやすくなります。また、AzureのGPT-5.2はGitHub Modelsに比べレート制限が緩やかなため、利便性も向上します。

## 前提条件

- モジュール01を完了していること（Azure OpenAIリソースが展開済み）
- ルートディレクトリにAzure認証情報を含む`.env`ファイルがあること（モジュール01の`azd up`で作成済み）

> **注意:** モジュール01を完了していない場合は、まずそちらの展開手順に従ってください。

## プロンプトエンジニアリングの理解

プロンプトエンジニアリングとは、必要な結果を安定的に得るために入力テキストを設計することです。単に質問を投げるだけでなく、モデルが何をどのように返せばよいかを正確に理解できるよう要請を構造化します。

これは同僚に指示を出すのと似ています。「バグを直して」はあいまいですが、「UserService.javaの45行目のnullポインター例外をヌルチェック追加で直して」は具体的です。言語モデルも同じで、具体性と構造が重要です。

## LangChain4jの活用方法

このモジュールでは、前回までと同様にLangChain4jをベースに、高度なプロンプトパターンを実装します。特にプロンプトの構造化と推論制御に重点を置いています。

<img src="../../../translated_images/ja/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4jがプロンプトをAzure OpenAI GPT-5.2に繋ぐ仕組み*

**依存関係** – モジュール02で使う`pom.xml`に定義されたlangchain4j依存ライブラリ：
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

**OpenAiOfficialChatModelの設定** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

チャットモデルはSpring Beanとして手動設定され、OpenAI OfficialクライアントがAzure OpenAIエンドポイントをサポートします。モジュール01との違いはモデル設定ではなく、`chatModel.chat()`へ送るプロンプトの構造です。

**SystemメッセージとUserメッセージ** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4jはメッセージ種別を分けて明確にしています。`SystemMessage`はAIの振る舞いとコンテキスト設定（例：「あなたはコードレビュアーです」）、`UserMessage`は実際の依頼を含みます。この分離で、異なる利用者の問い合わせでも一貫したAI行動を維持します。

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ja/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessageは継続的なコンテキストを提供し、UserMessagesは個別のリクエストを含む*

**MessageWindowChatMemoryによるマルチターン** – マルチターン会話パターンのため、モジュール01で使った`MessageWindowChatMemory`を再利用します。各セッションに固有のメモリインスタンスを`Map<String, ChatMemory>`で管理し、複数の対話を同時に行っても文脈が混ざりません。

**プロンプトテンプレート** – 本モジュールの焦点はLangChain4jの新APIではなくプロンプトエンジニアリングです。各パターン（低意欲、高意欲、タスク実行など）は同じ`chatModel.chat(prompt)`メソッドで処理し、XMLタグや指示、書式はすべてプロンプト本文の一部でありLangChain4jの機能ではありません。

**推論制御** – GPT-5.2の思考量は、「最大2段階推論」や「徹底的に掘り下げて」などのプロンプト指示により制御します。これはLangChain4jの設定ではなくプロンプトエンジニアリング技法です。ライブラリは単にプロンプトをモデルに届けるだけです。

重要なポイントは、LangChain4jがインフラ（[LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)経由のモデル接続、メモリ管理やメッセージ処理[Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)）を提供し、本モジュールはその枠組みの中で効果的なプロンプトの作り方を教えることです。

## コアパターン

すべての課題に同じアプローチは必要ありません。簡単な質問には素早い解答が求められ、複雑な問題には深い考察が必要です。可視的な推論が必要な場合もあれば、結果だけで良い場合もあります。本モジュールでは8つのプロンプトパターンを扱い、それぞれ別のシナリオに最適化されています。全パターンを試して、最良の使いどころを学びましょう。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8つのプロンプトエンジニアリングパターンと利用例の概要*

<img src="../../../translated_images/ja/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低意欲（速くて直接的）vs 高意欲（徹底的で探究的）な推論アプローチ*

**低意欲（早くて焦点が定まっている）** – 簡単な質問で素早く直接的な回答がほしい場合。モデルの推論は最小限（最大2ステップ）に制限します。計算、検索、単純な質問に適用。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilotで探る：** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開き、以下を尋ねてみましょう：
> - 「低意欲と高意欲のプロンプトパターンの違いは何ですか？」
> - 「プロンプト内のXMLタグはAIの応答構造にどう役立ちますか？」
> - 「自己反省パターンと直接指示パターンはいつ使い分けるべきですか？」

**高意欲（深く徹底的）** – 複雑な問題で包括的な分析が必要な場合。モデルは詳細な推論を示しながら掘り下げます。システム設計、アーキテクチャ判断、複雑な調査向け。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（段階的進行）** – 複数ステップのワークフロー向け。モデルは初めに計画を立て、作業の各ステップを説明し、最後に要約を返します。マイグレーション、実装、多段階処理に適用。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

チェイン・オブ・ソート（思考の連鎖）プロンプトはモデルに推論過程を明示的に示させ、複雑タスクの精度向上に寄与。段階的な内訳は人間もAIも論理を理解しやすくします。

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す：** 以下を質問：
> - 「長時間実行処理向けにタスク実行パターンをどう適応しますか？」
> - 「本番環境でのツールプリエンブルの最適な構造は？」
> - 「進捗状況をUIにキャプチャして表示するには？」

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計画 → 実行 → 要約のワークフローによる多段階タスク制御*

**自己反省コード** – 本番品質のコード生成向け。モデルがコードを生成後、品質基準に照らして評価し、改良を繰り返します。新機能やサービス開発時に活用。

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*生成 → 評価 → 問題特定 → 改善 → 繰り返し の反復改善ループ*

**構造化分析** – 一貫性のある評価が必要な場合。モデルに固定フレームワーク（正確性、プラクティス、パフォーマンス、セキュリティ）でコードレビューさせます。コードレビューや品質評価に最適。

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す：** 構造化分析について質問：
> - 「異なる種類のコードレビューに合わせて分析フレームワークをどうカスタマイズしますか？」
> - 「構造化出力をプログラムで解析・活用する最良の方法は？」
> - 「複数レビュー時に一貫した重大度レベルを確保するには？」

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*4カテゴリのフレームワークで一貫したコードレビュー、重大度レベル付き*

**マルチターンチャット** – 文脈が継続する対話向け。モデルは過去メッセージを記憶し、積み上げて回答します。インタラクティブなヘルプや複雑なQ&Aに利用。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ja/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*トークン制限まで複数ターンで会話文脈が蓄積される仕組み*

**段階的推論** – 論理の可視化が必要な場合。モデルが各ステップの推論を明示します。数学問題、論理パズル、思考過程の理解に適用。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*問題を明確な論理ステップに分解*

**制約付き出力** – 特定の形式や長さで応答が必要な場合。モデルに厳格なフォーマット・長さルールを守らせます。要約や厳密な出力が求められる際に利用。

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*特定フォーマット、長さ、構造制約の強制*

## 既存のAzureリソースの利用

**デプロイ確認：**

ルートディレクトリにAzure認証情報を含む`.env`ファイルが存在することを確認（モジュール01の実行時に作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動：**

> **注意:** もしモジュール01の`./start-all.sh`で全アプリをすでに起動している場合、このモジュールはポート8083で動作中です。以下の起動コマンドは省略し、直接 http://localhost:8083 にアクセスしてください。

**オプション1：Spring Bootダッシュボードを使う（VS Codeユーザーに推奨）**

開発コンテナにはSpring Bootダッシュボード拡張機能が含まれており、すべてのSpring Bootアプリ管理をGUIで行えます。VS Codeの左側のアクティビティバーにあるSpring Bootアイコンを探してください。

このダッシュボードでは：
- ワークスペース内のすべてのSpring Bootアプリを一覧表示
- ワンクリックで起動・停止
- リアルタイムログ閲覧
- アプリケーション状態監視

「prompt-engineering」の横の再生ボタンをクリックすればこのモジュールだけ起動、またはすべてのモジュールを一括起動できます。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション2：シェルスクリプトで実行**

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

またはこのモジュールだけ起動：

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

両方のスクリプトはルートの`.env`から環境変数を自動読み込み、JARがなければビルドします。

> **注意:** すべてのモジュールを先に手動ビルドしたい場合：
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

ブラウザで http://localhost:8083 を開きます。

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

## アプリケーションのスクリーンショット

<img src="../../../translated_images/ja/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*8つのプロンプトエンジニアリングパターンの特徴やユースケースを示すメインダッシュボード*

## パターンの探求

Webインターフェイスで様々なプロンプト戦略を試せます。各パターンは異なる課題を解決するので、どんな場合にどれが効果的か体験してみてください。

### 低い意欲 vs 高い意欲

「200の15％はいくつ？」のような簡単な質問を低意欲で投げると、即座に直接的な回答が得られます。次に「高トラフィックAPIのキャッシュ戦略を設計せよ」といった複雑な質問を高意欲で投げると、モデルはゆっくり考え、詳細な推論を示します。同じモデル、同じ質問構造でも、プロンプトが思考量を指示しているのです。
<img src="../../../translated_images/ja/low-eagerness-demo.898894591fb23aa0.webp" alt="低い熱意デモ" width="800"/>

*最小限の推論による迅速な計算*

<img src="../../../translated_images/ja/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高い熱意デモ" width="800"/>

*包括的なキャッシュ戦略（2.8MB）*

### タスク実行（ツール前置き）

複数ステップのワークフローは、事前の計画と進捗の説明から恩恵を受けます。モデルは何をするかを概説し、各ステップを説明し、結果を要約します。

<img src="../../../translated_images/ja/tool-preambles-demo.3ca4881e417f2e28.webp" alt="タスク実行デモ" width="800"/>

*段階的な説明付きRESTエンドポイントの作成（3.9MB）*

### 自己反省コード

「メール検証サービスを作成する」と試してください。単にコードを生成して終わるのではなく、モデルは生成、品質基準で評価、問題点の特定、改善を行います。コードが本番レベルに達するまで繰り返すのが見えます。

<img src="../../../translated_images/ja/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自己反省コードデモ" width="800"/>

*完全なメール検証サービス（5.2MB）*

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは固定されたカテゴリ（正確性、プラクティス、パフォーマンス、セキュリティ）と重大度レベルでコードを分析します。

<img src="../../../translated_images/ja/structured-analysis-demo.9ef892194cd23bc8.webp" alt="構造化分析デモ" width="800"/>

*フレームワークベースのコードレビュー*

### マルチターンチャット

「Spring Bootとは何か？」と尋ねた後、すぐに「例を見せて」と続けてください。モデルは最初の質問を記憶し、特定のSpring Bootの例を提供します。メモリがなければ、2回目の質問は曖昧すぎます。

<img src="../../../translated_images/ja/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="マルチターンチャットデモ" width="800"/>

*質問間のコンテキスト保持*

### ステップバイステップ推論

数学の問題を選んで、ステップバイステップ推論と低い熱意の両方で試してください。低い熱意は答えだけを高速に返し、内容は不透明です。ステップバイステップはすべての計算と判断を示します。

<img src="../../../translated_images/ja/step-by-step-reasoning-demo.12139513356faecd.webp" alt="ステップバイステップ推論デモ" width="800"/>

*明示的なステップ付き数学問題*

### 制約付き出力

特定の形式や語数が必要な場合は、このパターンが厳密な遵守を促します。箇条書き形式で正確に100語の要約を生成してみてください。

<img src="../../../translated_images/ja/constrained-output-demo.567cc45b75da1633.webp" alt="制約付き出力デモ" width="800"/>

*形式制御付き機械学習要約*

## 本当に学んでいること

**推論の努力がすべてを変える**

GPT-5.2はプロンプトを通じて計算努力を制御できます。努力が低いと最小限の探索で迅速な回答になります。努力が高いとモデルはじっくり考えます。簡単な質問で時間を無駄にせず、複雑な判断は急がないように努力をタスクの複雑さに合わせて学んでいます。

**構造が行動を導く**

プロンプトのXMLタグに気づきましたか？装飾ではありません。モデルは自由形式テキストよりも構造化された指示に従いやすいです。複数ステップの処理や複雑なロジックが必要な場合、構造はモデルの現在地と次に何をすべきかの把握を助けます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="プロンプト構造" width="800"/>

*明確なセクションとXML風構成を持つよく構造化されたプロンプトの解剖*

**自己評価による品質向上**

自己反省パターンは品質基準を明示することで機能します。モデルが「正しくやる」ことを期待するのではなく、「正しい」とはどういうことかを正確に伝えます：正しいロジック、エラーハンドリング、パフォーマンス、セキュリティ。モデルは自身の出力を評価して改善できます。これによりコード生成が単なるギャンブルからプロセスへ変わります。

**コンテキストは有限**

マルチターン会話は各リクエストにメッセージ履歴を含みますが、制限があります。どのモデルにも最大トークン数があります。会話が長くなると、関連コンテキストを保持しながら上限を超えない戦略が必要です。本モジュールでメモリの仕組みを学び、後でいつ要約し、いつ忘れ、いつ取り出すかを学びます。

## 次のステップ

**次のモジュール：** [03-rag - RAG（検索強化生成）](../03-rag/README.md)

---

**ナビゲーション：** [← 前へ：モジュール01 - 導入](../01-introduction/README.md) | [メインへ戻る](../README.md) | [次へ：モジュール03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：
本書類はAI翻訳サービス「[Co-op Translator](https://github.com/Azure/co-op-translator)」を使用して翻訳されています。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご了承ください。原文はあくまで正式な情報源とみなされるべきです。重要な情報については、専門の人間翻訳を推奨いたします。本翻訳の使用により生じたいかなる誤解や解釈違いについても、一切の責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->