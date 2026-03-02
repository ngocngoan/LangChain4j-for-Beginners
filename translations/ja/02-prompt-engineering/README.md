# Module 02: GPT-5.2によるプロンプトエンジニアリング

## 目次

- [ビデオウォークスルー](../../../02-prompt-engineering)
- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリング基礎](../../../02-prompt-engineering)
  - [ゼロショットプロンプティング](../../../02-prompt-engineering)
  - [フューショットプロンプティング](../../../02-prompt-engineering)
  - [チェイン・オブ・ソート](../../../02-prompt-engineering)
  - [役割ベースのプロンプティング](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの使用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探究](../../../02-prompt-engineering)
  - [低熱心度 vs 高熱心度](../../../02-prompt-engineering)
  - [タスク実行（ツールの前置き）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化された解析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [ステップ・バイ・ステップ推論](../../../02-prompt-engineering)
  - [制約された出力](../../../02-prompt-engineering)
- [本当に学んでいること](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## ビデオウォークスルー

このモジュールの始め方を説明するライブセッションをご覧ください：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4jによるプロンプトエンジニアリング - ライブセッション" width="800"/></a>

## 学習内容

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="学習内容" width="800"/>

前のモジュールでは、メモリが会話型AIを可能にする仕組みを学び、GitHub Modelsで基本的な対話を試しました。今回はAzure OpenAIのGPT-5.2を使い、質問の仕方―プロンプトそのもの―に焦点を当てます。プロンプトの構造が、得られる回答の質に大きく影響します。まずは基本的なプロンプティング技術の復習を行い、その後にGPT-5.2の能力を最大限に活用する８つの高度なパターンを紹介します。

GPT-5.2を使う理由は、推論制御を導入しているためです。モデルに回答までの思考量を指示できるため、さまざまなプロンプト戦略の違いが明確になり、適切な使い分けが理解しやすくなります。さらに、AzureではGitHub Modelsに比べてGPT-5.2のレート制限が緩い点も利点です。

## 前提条件

- モジュール01を完了済み（Azure OpenAIリソースを展開済み）
- ルートディレクトリにAzure認証情報が入った`.env`ファイルがある（モジュール01の`azd up`コマンドで作成）

> **注意：** モジュール01を完了していない場合は、最初にそちらの展開手順を実行してください。

## プロンプトエンジニアリングの理解

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="プロンプトエンジニアリングとは？" width="800"/>

プロンプトエンジニアリングとは、必要な結果を安定的に引き出す入力テキストを設計することです。ただ単に質問するのではなく、モデルが求められていることを正確に理解し提供できるようにリクエストを構造化することを意味します。

同僚に指示を出すことに例えるとわかりやすいです。「バグを直して」では曖昧ですが、「UserService.javaの45行目のヌルポインタ例外をヌルチェックを追加して修正して」と言えば具体的です。言語モデルも同様で、明確さと構造が重要です。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4jの役割" width="800"/>

LangChain4jはインフラストラクチャを提供します — モデル接続、メモリ、メッセージタイプなど。一方でプロンプトパターンは、そのインフラを通じて送る、細かく構築されたテキストです。重要な構成要素は`SystemMessage`（AIの振る舞いと役割を設定）と`UserMessage`（実際の要求を運ぶ）です。

## プロンプトエンジニアリング基礎

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="５つのプロンプトエンジニアリングパターン概要" width="800"/>

このモジュールの高度なパターンに入る前に、基礎となる５つのプロンプティング手法を復習しましょう。これらはすべてのプロンプトエンジニアが知っておくべき基本です。もし[クイックスタートモジュール](../00-quick-start/README.md#2-prompt-patterns)を既に学習済みなら、実際の例を見ているはずです。ここではその概念的枠組みを示します。

### ゼロショットプロンプティング

最もシンプルな方法：例を与えず、モデルに直接指示を出します。モデルはトレーニングに基づいてタスクを理解し実行します。期待される振る舞いが明白な単純な要求に適しています。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="ゼロショットプロンプティング" width="800"/>

*例を使わず直接指示 — モデルが指示のみでタスクを推測*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 応答: 「ポジティブ」
```

**使う場面:** 単純な分類、直接的な質問、翻訳、追加の指示なしでモデルが対応可能なタスク。

### フューショットプロンプティング

例を示してモデルにパターンを学習させます。モデルは提示された例から入力・出力の形式を理解し、それを新規入力に応用します。求めるフォーマットや振る舞いが明確でない場合でも、一貫性が大きく向上します。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="フューショットプロンプティング" width="800"/>

*例から学習 — パターンを特定し新しい入力に適用*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**使う場面:** カスタム分類、一貫したフォーマット、ドメイン固有のタスク、ゼロショットで結果がばらつく場合。

### チェイン・オブ・ソート

モデルにステップごとの推論を示させます。答えにいきなり飛ぶのではなく、問題を分解し各要素を明示的に解決します。数学、論理、多段階推論で精度が上がります。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="チェイン・オブ・ソートプロンプティング" width="800"/>

*ステップバイステップの推論 — 複雑な問題を明示的な論理ステップに分割*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは示しています：15 - 8 = 7、次に 7 + 12 = 19 個のリンゴ
```

**使う場面:** 数学問題、論理パズル、デバッグ、推論過程を示すことで精度・信頼性が上がるタスク。

### 役割ベースのプロンプティング

AIにペルソナや役割を設定してから質問します。これにより回答のトーン、深さ、焦点が変わります。「ソフトウェアアーキテクト」と「ジュニア開発者」や「セキュリティ監査人」ではアドバイスが異なります。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="役割ベースのプロンプティング" width="800"/>

*コンテキストと役割の設定 — 同じ質問でも役割により回答が変化*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**使う場面:** コードレビュー、指導、ドメイン特化の解析、特定の専門知識レベルや視点に合わせた回答が必要な場合。

### プロンプトテンプレート

変数プレースホルダーを用いて再利用可能なプロンプトを作成します。毎回新しく書く代わりにテンプレートを定義して異なる値を当てはめます。LangChain4jの`PromptTemplate`クラスでは`{{variable}}`構文で簡単に扱えます。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="プロンプトテンプレート" width="800"/>

*変数プレースホルダー付き再利用プロンプト — ひとつのテンプレートで多用途*

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

**使う場面:** 入力が異なる繰り返しクエリ、バッチ処理、再利用可能なAIワークフロー構築、構造は同じでデータだけ変えるケース。

---

これら５つの基礎は多くのプロンプト作成で強力なツールセットを与えます。残りのモジュールは、GPT-5.2の推論制御、自己評価、構造化出力機能を活かした**８つの高度なパターン**へ発展させます。

## 高度なパターン

基礎を押さえたら、このモジュールでしか学べない８つの高度なパターンへ進みましょう。問題によってアプローチは異なります。素早い回答がほしいものもあれば、深い思考や詳細な分析が必要なものもあります。ときには推論の過程を見せることが必要で、また別の場合は結果だけを出せばよい。下記のパターンはそれぞれ異なるシナリオに最適化されており、GPT-5.2の推論制御機能が違いをより際立たせます。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="８つのプロンプトパターン" width="800"/>

*８つのプロンプトエンジニアリングパターンとその利用ケースの概要*

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2の推論制御" width="800"/>

*GPT-5.2の推論制御により、モデルの思考量を指定可能―迅速な直接回答から深い探求まで*

**低熱心度（クイック＆フォーカス）** - シンプルな質問で高速かつ直接的な回答が欲しい場合に。モデルは最小限の推論（最大2ステップ）を行います。計算、検索、単純質問に向いています。

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilotで試す:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開いて質問してみてください：
> - 「低熱心度と高熱心度のプロンプトパターンの違いは何ですか？」
> - 「プロンプト内のXMLタグはAIのレスポンス構造にどう役立っていますか？」
> - 「自己反省パターンは直接指示とどう使い分けるべきですか？」

**高熱心度（深い洞察＆徹底解析）** - 複雑な問題で包括的な分析が必要な場合に。モデルは丁寧に探索し詳細な推論を示します。システム設計、アーキテクチャの意思決定、複雑な調査に適しています。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（ステップごとの進行）** - 複数段階のワークフローに。モデルが最初に計画を提示し、作業しながら各ステップを説明し、最後にまとめを提供します。マイグレーション、実装、多段階プロセスに。

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

チェイン・オブ・ソートは推論過程の明示をモデルに求め、複雑なタスクの精度を高めます。人間もAIも論理を理解しやすくなります。

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** このパターンについて質問してみましょう：
> - 「長時間かかる処理にタスク実行パターンをどう適応しますか？」
> - 「本番アプリケーションでのツール用前置きの構成ベストプラクティスは？」
> - 「UIでの途中経過報告をどうキャプチャして表示すればよい？」

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="タスク実行パターン" width="800"/>

*計画 → 実行 → まとめ、複数ステップのタスクワークフロー*

**自己反省コード** - 本番品質のコード生成向け。モデルが適切なエラーハンドリングを備えたコードを書きます。新機能開発やサービス構築に用います。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自己反省サイクル" width="800"/>

*反復的な改善ループ - 生成 → 評価 → 問題検出 → 改善 → 繰り返し*

**構造化された解析** - 一貫した評価に。モデルが特定のフレームワーク（正確性、実践、パフォーマンス、セキュリティ、保守性）でコードをレビュー。コードレビューや品質分析に利用。

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** 構造化解析について質問してみましょう：
> - 「異なる種類のコードレビューに対し解析フレームワークをどうカスタマイズできますか？」
> - 「構造化出力をプログラム的に解析して活用する最適な方法は？」
> - 「異なるレビューで一貫した重大度レベルをどう確保しますか？」

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="構造化解析パターン" width="800"/>

*重大度レベル付き一貫したコードレビュー用フレームワーク*

**マルチターンチャット** - 文脈を必要とする対話に。モデルは以前のメッセージを記憶し積み重ねていきます。インタラクティブヘルプや複雑なQ&Aに活用。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ja/context-memory.dff30ad9fa78832a.webp" alt="コンテキストメモリ" width="800"/>

*会話の文脈が複数ターンにわたり蓄積しトークン制限まで続く様子*

**ステップ・バイ・ステップ推論** - 可視化された論理が必要な問題に。各ステップで明示的な推論を示します。数学問題、論理パズル、思考過程の理解に適用。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="ステップバイステップパターン" width="800"/>

*問題を明確な論理ステップに分解*

**制約された出力** - 特定のフォーマット要件がある回答に。モデルは形式や長さのルールを厳守します。要約や正確な出力構造が必要な場合に。

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

<img src="../../../translated_images/ja/constrained-output-pattern.0ce39a682a6795c2.webp" alt="制約された出力パターン" width="800"/>

*特定の形式、長さ、構造要件を強制*

## 既存のAzureリソースの使用

**展開の確認：**

ルートディレクトリにAzure認証情報が入った`.env`ファイルが存在することを確認（モジュール01の展開時に作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動：**

> **注意：** もしモジュール01の`./start-all.sh`を使ってすべて起動済みなら、このモジュールは既にポート8083で稼働しています。以下の起動コマンドは省略して、直接 http://localhost:8083 へアクセスしてください。
**オプション 1: Spring Boot Dashboard の利用（VS Code ユーザーにおすすめ）**

dev コンテナには Spring Boot Dashboard 拡張機能が含まれており、すべての Spring Boot アプリケーションを管理するためのビジュアルインターフェイスを提供します。VS Code の左側にあるアクティビティバー（Spring Boot アイコンを探してください）で見つけることができます。

Spring Boot Dashboard では、以下が可能です：
- ワークスペース内のすべての利用可能な Spring Boot アプリケーションを確認
- クリックひとつでアプリケーションを起動／停止
- アプリケーションログをリアルタイムに表示
- アプリケーションの状態を監視

「prompt-engineering」の横にある再生ボタンをクリックするだけでこのモジュールを起動するか、すべてのモジュールを一度に起動できます。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション 2: シェルスクリプトの利用**

全てのウェブアプリケーション（モジュール01〜04）を起動：

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

またはこのモジュールだけを起動：

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

両方のスクリプトはルートの `.env` ファイルから環境変数を自動的にロードし、JAR ファイルが存在しない場合はビルドします。

> **注意:** 起動前にすべてのモジュールを手動でビルドしたい場合：
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

ブラウザで http://localhost:8083 を開いてください。

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


## アプリケーションのスクリーンショット

<img src="../../../translated_images/ja/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*全8つのプロンプトエンジニアリングパターンとその特徴・ユースケースを示すメインダッシュボード*

## パターンの探索

ウェブインターフェイスでは様々なプロンプト戦略を試すことができます。それぞれのパターンは異なる問題を解決します。どのアプローチが有効か、ぜひ試してみてください。

> **注意: ストリーミング vs 非ストリーミング** — 各パターンページには２つのボタンがあります：**🔴 ストリームレスポンス（ライブ）** と **非ストリーミング** オプション。ストリーミングは Server-Sent Events (SSE) を使って、モデルが生成するトークンをリアルタイムに表示するため、進行状況がすぐに見えます。非ストリーミングはレスポンス全体を受け取ってから表示します。深い推論を必要とするプロンプト（例：High Eagerness、Self-Reflecting Code）では、非ストリーミング呼び出しは非常に長時間かかることがあり、数分間、フィードバックが見えないこともあります。**複雑なプロンプトを試すときはストリーミングを使い、モデルの動作を見てリクエストがタイムアウトしたかのような誤解を避けてください。**
>
> **注意: ブラウザ要件** — ストリーミング機能は Fetch Streams API (`response.body.getReader()`) を使用し、完全なブラウザ（Chrome、Edge、Firefox、Safari）が必要です。VS Code 内蔵の Simple Browser では動作しません。これは Simple Browser の webview が ReadableStream API に対応していないためです。Simple Browser を使う場合は非ストリーミングボタンは通常通り動作します。フル機能を使いたい場合は、外部ブラウザで `http://localhost:8083` を開いてください。

### Low Eagerness と High Eagerness

まず「15% の 200 は何ですか？」のような簡単な質問を Low Eagerness で試してください。すぐに直接的な回答が得られます。次に「高トラフィックの API のキャッシュ戦略を設計してください」のような複雑な質問を High Eagerness で試してください。**🔴 ストリームレスポンス（ライブ）** をクリックし、モデルが詳細な推論をトークンごとに生成していく様子を見てください。同じモデル、同じ質問構造でも、プロンプトがどれだけ考えるかを指示しています。

### タスク実行（ツールの前置き）

複数工程のワークフローは、事前計画と進捗のナレーションが有効です。モデルは何をするかを概説し、各ステップを説明し、最後に結果をまとめます。

### Self-Reflecting Code

「メールのバリデーションサービスを作成してください」と試してみてください。ただコードを生成して終わりではなく、モデルは生成したコードを品質基準と照らし合わせて評価し、問題点を特定し改善します。何度も反復しながらコードを改善し、実運用レベルに到達する様子がわかります。

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは正確性、プラクティス、性能、セキュリティの固定カテゴリを用いて、重大度をつけてコード分析を行います。

### マルチターンチャット

「Spring Boot とは何ですか？」と聞き、その直後に「例を見せてください」と続けてみてください。モデルは最初の質問を覚えており、具体的な Spring Boot の例を示します。メモリがなければ２つ目の質問はあいまいすぎて、適切な回答は難しいです。

### ステップバイステップ推論

数学の問題を選び、Step-by-Step Reasoning と Low Eagerness の両方で試してみてください。Low Eagerness は答えだけを高速に返してくれますが説明はありません。Step-by-Step ではすべての計算や判断過程を示します。

### 制約されたアウトプット

特定のフォーマットや語数制限が必要な場合、このパターンは厳密に守らせます。例えば、箇条書き形式でちょうど100語の要約を生成することを試してください。

## 本当に学んでいること

**推論努力がすべてを変える**

GPT-5.2 はプロンプトを通じて計算努力量を制御できます。低い努力は素早い応答で浅い探索に、努力が高いほどモデルはじっくり深く考えます。タスクの複雑さに合わせて努力量を調整することを学び、単純な質問に時間を無駄にせず、複雑な判断は急がないようにします。

**構造が振る舞いを導く**

プロンプト内の XML タグに気づきましたか？飾りではありません。モデルは自由形式のテキストよりも構造化された指示に従いやすいです。複数段階の処理や複雑なロジックの場合、構造があることでモデルは現在どこにいて次に何をするかをより確実に追跡できます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*明確なセクションとXML形式で整理されたよく構造化されたプロンプトの構成*

**自己評価による品質の向上**

自己反省型のパターンは品質基準を明示することによって機能します。モデルに「正しくやってほしい」と期待するのではなく、何が「正しい」のか（正確なロジック、エラーハンドリング、性能、セキュリティ）を明示します。モデルは自身の出力を評価し改善できるため、コード生成が宝くじではなく確実なプロセスになります。

**コンテキストには限界がある**

マルチターン会話は各リクエストにメッセージ履歴を含めることで成り立っていますが、限界があります。すべてのモデルには最大トークン数があり、会話が長くなるとこれを超えないよう工夫が必要です。このモジュールではメモリの動作を学び、あとでいつ要約し、いつ忘れ、いつ再取得するかを習得します。

## 次のステップ

**次のモジュール:** [03-rag - RAG (検索強化生成)](../03-rag/README.md)

---

**ナビゲーション:** [← 前へ: モジュール 01 - はじめに](../01-introduction/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**:  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性を期しておりますが、自動翻訳には誤りや不正確な箇所が含まれる場合があります。原文の言語による文書が正式な情報源となります。重要な情報については、専門の翻訳者による翻訳を推奨いたします。本翻訳の利用により生じたいかなる誤解や誤訳についても、一切の責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->