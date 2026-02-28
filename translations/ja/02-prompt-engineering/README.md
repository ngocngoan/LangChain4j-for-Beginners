# Module 02: GPT-5.2を使ったプロンプトエンジニアリング

## 目次

- [ビデオウォークスルー](../../../02-prompt-engineering)
- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの基本](../../../02-prompt-engineering)
  - [ゼロショットプロンプティング](../../../02-prompt-engineering)
  - [フューショットプロンプティング](../../../02-prompt-engineering)
  - [チェーンオブソート](../../../02-prompt-engineering)
  - [ロールベースのプロンプティング](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの利用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低意欲と高意欲の比較](../../../02-prompt-engineering)
  - [タスク実行（ツールの前置き）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化解析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [ステップバイステップ推論](../../../02-prompt-engineering)
  - [制約された出力](../../../02-prompt-engineering)
- [本当に学んでいること](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## ビデオウォークスルー

このモジュールの始め方を説明するライブセッションをご覧ください：[LangChain4jによるプロンプトエンジニアリング - ライブセッション](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 学習内容

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="学習内容" width="800"/>

前のモジュールでは、メモリが会話型AIを可能にする仕組みを見て、GitHubモデルを使って基本的な対話を行いました。今回は、Azure OpenAIのGPT-5.2を使った質問の仕方、すなわち「プロンプト」そのものに焦点を当てます。プロンプトの構造が、回答の質に大きく影響します。まず基本的なプロンプト技術を復習し、その後、GPT-5.2の能力をフル活用する8つの高度なパターンを紹介します。

GPT-5.2は推論制御を導入しており、回答前にどれだけ思考させるかを指定できます。これにより異なるプロンプト戦略の特徴が明確になり、適切な場面で使い分ける理解が深まります。また、GPT-5.2はGitHubモデルよりもAzureでのレート制限が少ないメリットもあります。

## 前提条件

- モジュール01を完了していること（Azure OpenAIリソースがデプロイ済み）
- ルートディレクトリにAzure認証情報入りの`.env`ファイルがあること（モジュール01で`azd up`を実行して作成）

> **注意:** モジュール01を完了していない場合は、まずそこでのデプロイ手順に従ってください。

## プロンプトエンジニアリングの理解

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="プロンプトエンジニアリングとは？" width="800"/>

プロンプトエンジニアリングは、求める結果を一貫して得られるよう入力テキストを設計することです。ただ質問をするだけではなく、モデルが「何をどうすべきか」を正確に理解し、実行できるよう構造化することです。

同僚に指示を出すのに例えるなら、「バグを修正して」はあいまいです。「UserService.javaの45行目のNullPointerExceptionをnullチェックを追加して修正して」は具体的です。言語モデルも同じく、具体性と構造が重要です。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4jの役割" width="800"/>

LangChain4jはモデル接続、メモリ、メッセージタイプなどのインフラを提供し、プロンプトパターンはそのインフラを通じて送る慎重に構造化されたテキストです。主な構成要素は`SystemMessage`（AIの振る舞いや役割を設定）と`UserMessage`（実際のリクエストを伝える）です。

## プロンプトエンジニアリングの基本

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="五つのプロンプトエンジニアリングパターン概要" width="800"/>

このモジュールの高度なパターンに入る前に、まず五つの基本的なプロンプト技法を振り返りましょう。これはすべてのプロンプトエンジニアが知っておくべき土台です。[クイックスタートモジュール](../00-quick-start/README.md#2-prompt-patterns) を既に終えているなら、それらの概念的な枠組みの紹介です。

### ゼロショットプロンプティング

最もシンプルな方法：例を示さず直接指示を与えます。モデルは訓練された知識だけを使ってタスクを理解し実行します。期待される動作が明白な単純な要求に適しています。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="ゼロショットプロンプティング" width="800"/>

*例なしの直接指示 — 指示だけからタスクを推測*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 応答:「ポジティブ」
```

**使う場合:** 簡単な分類、直接的な質問、翻訳、追加の指示がなくてもモデルが対応可能なタスク。

### フューショットプロンプティング

モデルに従うべきパターンを示す例を提供します。モデルは例から入力と出力の形式を学び、新しい入力に適用します。望む形式や振る舞いが明確でないタスクで、一貫性を大きく向上させます。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="フューショットプロンプティング" width="800"/>

*例から学習 — パターンを認識し新しい入力に適用*

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

**使う場合:** カスタム分類、一貫したフォーマット、特定領域のタスク、ゼロショットの結果が不安定な場合。

### チェーンオブソート

モデルに推論過程をステップごとに示してもらいます。答えに直接飛ばず、問題を分解して各部分を明示的に処理します。数学、論理、多段階推論の正確性が向上します。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="チェーンオブソートプロンプティング" width="800"/>

*ステップごとの推論 — 複雑な問題を明確な論理ステップに分解*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは示します：15 - 8 = 7、それから7 + 12 = 19個のリンゴ
```

**使う場合:** 数学問題、論理パズル、デバッグ、推論過程の提示で信頼性と精度が高まるタスク。

### ロールベースのプロンプティング

質問の前にAIに役割やペルソナを設定します。これが回答のトーン、深さ、焦点に影響します。例えば「ソフトウェアアーキテクト」と「ジュニア開発者」や「セキュリティ監査人」では異なるアドバイスになります。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="ロールベースプロンプティング" width="800"/>

*コンテキストとペルソナの設定 — 同じ質問も役割によって異なる回答に*

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

**使う場合:** コードレビュー、教育、特定の専門知識や視点に合わせた分析や回答が必要な時。

### プロンプトテンプレート

変数プレースホルダー付きの再利用可能なプロンプトを作成します。毎回新規作成せずに、テンプレートを一度定義し異なる値を差し込みます。LangChain4jの`PromptTemplate`クラスは`{{variable}}`構文でこれを簡単にします。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="プロンプトテンプレート" width="800"/>

*変数プレースホルダー付きで再利用可能なプロンプト — 1つのテンプレートを多用途に*

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

**使う場合:** 異なる入力で繰り返し問い合わせる処理、一括処理、再利用可能なAIワークフローの構築、構造は同じでデータだけ変わるシナリオ。

---

これら五つの基本を押さえれば、ほとんどのプロンプト作成で強力なツールキットになります。本モジュールの後半は、GPT-5.2の推論制御や自己評価、構造化出力機能を活かす**8つの高度なパターン**を解説します。

## 高度なパターン

基本を確認したら、モジュールの特色となる8つの高度なパターンを見ていきましょう。すべての問題に同じアプローチは不要です。クイックな回答が欲しい質問もあれば、じっくり考える必要があるものもあります。推論過程の可視化を求めることもあれば、結果だけでよい場合もあります。下記の各パターンは異なるシナリオ向けに最適化されており、GPT-5.2の推論制御機能でその違いがより顕著になります。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="8つのプロンプトパターン" width="800"/>

*8つのプロンプトエンジニアリングパターンの概要と使用ケース*

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2の推論制御" width="800"/>

*GPT-5.2の推論制御機能は、モデルがどれだけ思考するかを指定可能 — 速い直接回答から深い探求まで*

**低意欲（高速・焦点絞り）** - シンプルな質問で高速・明確な回答を得たいとき。モデルは最小限の思考（最大2ステップ）で対応。計算、検索、単純質問に適用。

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

> 💡 **GitHub Copilotで探求:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開き、質問してみましょう:
> - 低意欲と高意欲プロンプトパターンの違いは？
> - プロンプト内のXMLタグはAIの応答構造にどう役立つの？
> - 自己反省パターンと直接指示はいつ使い分けるべき？

**高意欲（深掘り・徹底的）** - 複雑な問題を網羅的に分析したい時。モデルは詳細な推論を示しながら丁寧に解決に挑みます。システム設計、アーキテクチャ判断、複雑な調査に適用。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（ステップごとの進捗）** - 多段階のワークフロー向け。モデルがまず計画を示し、作業しながら進捗を説明し、最後にまとめて報告。移行作業、実装、複数ステップのプロセスに使う。

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

チェーンオブソートプロンプティングはモデルに推論過程を示させるため、複雑な作業の精度向上に役立ちます。ステップごとの分解は人間とAI双方に論理の理解を促します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) チャットで試そう:** このパターンについて質問:
> - 長時間処理にタスク実行パターンをどう適応する？
> - 本番アプリのツール前置き構造のベストプラクティスは？
> - UIで中間進捗をキャプチャし表示するには？

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="タスク実行パターン" width="800"/>

*計画 → 実行 → まとめのワークフロー*

**自己反省コード** - 本番品質のコード生成に。モデルがエラーハンドリングを含み生産基準に沿ったコードを生成します。新機能やサービス構築時に利用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自己反省サイクル" width="800"/>

*反復改善ループ — 生成→評価→問題特定→改善→繰り返し*

**構造化解析** - 一貫性のある評価に対応。モデルが固定されたフレームワーク（正確性、プラクティス、パフォーマンス、セキュリティ、保守性）でコードをレビューします。コードレビューや品質評価に適用。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) チャットで試してみよう:** 構造化解析について質問:
> - コードレビューの種類に応じた解析フレームワークのカスタマイズ方法は？
> - 構造化出力をプログラム的に解析し活用するには？
> - セッション間で一貫した重大度レベルを確保するには？

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="構造化解析パターン" width="800"/>

*セキュリティレベルを含む一貫したコードレビュー用のフレームワーク*

**マルチターンチャット** - コンテキストが必要な会話で使用。モデルは過去のメッセージを記憶し、積み重ねて応答します。対話的ヘルプや複雑なQ&Aに適用。

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

*会話コンテキストが複数ターンにわたり蓄積され、トークン制限に達するまで維持される*

**ステップバイステップ推論** - 論理を可視化したい問題向け。モデルが各ステップに対して明示的に推論過程を示します。数学や論理問題、思考過程の理解が必要な場合に適用。

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

*問題を明示的な論理ステップに分解*

**制約された出力** - 特定のフォーマット要件に従うレスポンス。モデルはフォーマットや長さのルールを厳守。要約や厳密な出力構造が求められる場合に最適。

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

*特定のフォーマット、長さ、構造の要件を強制*

## 既存のAzureリソースの利用

**デプロイ状況の確認:**

ルートディレクトリにAzure認証情報入りの`.env`ファイルがあるかを確認します（モジュール01で作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動:**

> **注意:** もしモジュール01で`./start-all.sh`を使いすでにすべてのアプリを起動している場合、このモジュールはポート8083で既に稼働中です。以降の起動コマンドは省略して http://localhost:8083 へ直接アクセスできます。

**方法1: Spring Bootダッシュボードの利用（VS Codeユーザー推奨）**
devコンテナにはSpring Boot Dashboard拡張機能が含まれており、すべてのSpring Bootアプリケーションを管理するためのビジュアルインターフェースを提供します。VS Codeの左側にあるアクティビティバーで（Spring Bootアイコンを探してください）見つけることができます。

Spring Boot Dashboardからは次のことができます：
- ワークスペース内の利用可能なすべてのSpring Bootアプリケーションを見る
- ワンクリックでアプリケーションを開始/停止する
- アプリケーションログをリアルタイムで表示する
- アプリケーションの状態を監視する

「prompt-engineering」の横にある再生ボタンをクリックするだけで、このモジュールを起動できます。または、すべてのモジュールを一度に起動することも可能です。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション2: シェルスクリプトの使用**

すべてのWebアプリケーション（モジュール01-04）を起動：

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

あるいはこのモジュールだけを起動：

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

どちらのスクリプトもルートの`.env`ファイルから環境変数を自動的に読み込み、JARファイルが存在しない場合はビルドを行います。

> **注意：** すべてのモジュールを手動でビルドしてから起動したい場合：
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

ブラウザで http://localhost:8083 を開きます。

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

*すべての8つのプロンプトエンジニアリングパターンとその特徴・ユースケースを表示するメインダッシュボード*

## パターンの探求

Webインターフェースでは、さまざまなプロンプト戦略を試せます。各パターンは異なる問題を解決するためのもので、試してみることでそれぞれのアプローチの得意領域がわかります。

> **注意：ストリーミング vs 非ストリーミング** — 各パターンのページには「🔴 ストリーム応答（ライブ）」ボタンと「非ストリーミング」オプションの2つのボタンがあります。ストリーミングはServer-Sent Events（SSE）を使い、モデルが生成するトークンをリアルタイムで表示するため、進行状況が即座に見えます。非ストリーミングのオプションは応答が全部そろってから表示します。深い推論を必要とするプロンプト（例：High Eagerness、Self-Reflecting Code）では、非ストリーミング呼び出しが非常に長時間かかることがあり、数分間フィードバックがないこともあります。**複雑なプロンプトを試すときはストリーミングを使い、モデルが動いている様子を見てリクエストのタイムアウトと思い込まないようにしてください。**
>
> **注意：ブラウザ要件** — ストリーミング機能はFetch Streams API（`response.body.getReader()`）を使用し、これはフルブラウザ（Chrome、Edge、Firefox、Safari）が必要です。VS Code内蔵のSimple Browserでは動作しません。Simple BrowserのwebviewはReadableStream APIをサポートしていないためです。Simple Browserを使う場合は非ストリーミングボタンが通常通り動作しますが、ストリーミングボタンだけが影響を受けます。全機能を使いたい場合は外部ブラウザで`http://localhost:8083`を開いてください。

### Low vs High Eagerness

Low Eagernessを使って「200の15%は何ですか？」のような簡単な質問をしてください。即座に直接答えが返ってきます。次にHigh Eagernessで「高トラフィックAPIのキャッシュ戦略を設計してください」といった複雑な質問をします。「🔴 ストリーム応答（ライブ）」をクリックし、モデルの詳細な推論がトークンごとに表示される様子を観察しましょう。同じモデル、同じ質問構造でも、プロンプトがどの程度深く考えるかを指示しています。

### タスク実行（ツールの前置き）

複数段階のワークフローは、事前計画と進行状況のナレーションの恩恵を受けます。モデルは何をするかを概説し、各ステップを説明し、結果をまとめます。

### 自己反省コード

「メール検証サービスを作成してください」と試してみてください。ただコードを生成して終わるのではなく、モデルは生成後に品質基準で評価し、弱点を特定して改善します。納得いく品質になるまで繰り返し修正している様子が見られます。

### 構造化分析

コードレビューには一貫した評価枠組みが必要です。モデルは固定カテゴリ（正確性、プラクティス、パフォーマンス、セキュリティ）と重大度レベルを使ってコードを分析します。

### マルチターンチャット

「Spring Bootとは何ですか？」と聞いてから、「具体例を見せてください」とすぐに続けてください。モデルは最初の質問を覚えており、特定のSpring Bootの例を返します。記憶がないと、2つ目の質問はあいまいすぎて答えられません。

### ステップバイステップ推論

数学の問題を選び、Step-by-Step ReasoningとLow Eagernessの両方で試してください。Low Eagernessは答えだけを速く返しますが過程は見えません。Step-by-Stepは計算や判断をすべて示します。

### 制約付き出力

特定のフォーマットや語数が必要な場合、このパターンで厳密に順守させます。100語の箇条書きの要約を生成してみてください。

## 本当に学んでいること

**推論努力がすべてを変える**

GPT-5.2ではプロンプトを通じて計算努力を制御できます。努力が少ないと速い応答で探索が控えめです。努力が多いとモデルは時間をかけて深く考えます。課題の複雑さに合わせて努力を調整することを学んでいます。単純な質問に時間を無駄にせず、複雑な判断は急がないようにしましょう。

**構造が行動を導く**

プロンプト内のXMLタグに気づきましたか？飾りではありません。モデルはフリーテキストよりも構造化された指示に従う方が確実です。多段階プロセスや複雑なロジックが必要な場合、構造がモデルの現在位置や次の手順を追跡するのに役立ちます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*明確なセクションとXMLスタイルの構成を持つよく構造化されたプロンプトの構造*

**自己評価による品質**

自己反省パターンは品質基準を明示的にすることで動作します。モデルに「正しくやる」ことを期待するのではなく、「正しい」とは何か（正確なロジック、エラー処理、パフォーマンス、セキュリティ）を具体的に教えます。モデルは自身の出力を評価し改善できます。これによりコード生成は単なる運試しではなくプロセスになります。

**コンテキストは有限**

マルチターン会話は各リクエストにメッセージ履歴を含めることで機能します。しかしトークン数の上限があります。会話が増えると、その制限に当たらないように関連コンテキストを維持する戦略が必要になります。このモジュールではメモリの仕組みを学び、あとでいつ要約し、いつ忘れ、いつ取り出すかを学びます。

## 次のステップ

**次のモジュール：** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ナビゲーション：** [← 前へ：モジュール01 - はじめに](../01-introduction/README.md) | [メインに戻る](../README.md) | [次へ：モジュール03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があることをご承知ください。原文の言語で記載された文書が正式な情報源とみなされます。重要な情報に関しては、専門の人間による翻訳を推奨します。本翻訳の使用により生じたいかなる誤解や解釈違いについても、当方は責任を負いません。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->