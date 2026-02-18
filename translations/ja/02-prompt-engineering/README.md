# Module 02: GPT-5.2によるプロンプトエンジニアリング

## 目次

- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの基本](../../../02-prompt-engineering)
  - [ゼロショットプロンプティング](../../../02-prompt-engineering)
  - [フューショットプロンプティング](../../../02-prompt-engineering)
  - [チェーンオブソート](../../../02-prompt-engineering)
  - [役割ベースのプロンプティング](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの利用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低熱心度 vs 高熱心度](../../../02-prompt-engineering)
  - [タスク実行（ツールプレアンブル）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化分析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [段階的推論](../../../02-prompt-engineering)
  - [制約付き出力](../../../02-prompt-engineering)
- [本当に学んでいること](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## 学習内容

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

前のモジュールでは、会話型AIが記憶により成り立つことを学び、GitHubモデルを使って基本的な対話を行いました。今回は、質問の仕方—つまりプロンプトの構成の仕方—に焦点をあてます。Azure OpenAIのGPT-5.2を用いて、プロンプトの構造が応答の質にどれほど影響を与えるかを見ていきます。まず基本的なプロンプト技術を復習し、その後、GPT-5.2の能力をフル活用する8つの高度なパターンに進みます。

GPT-5.2は推論の制御を導入しているため、答える前にモデルにどれだけ考えさせるかを指定できます。これにより、さまざまなプロンプト戦略の違いが明確になり、それぞれの使いどきが理解しやすくなります。また、AzureのGPT-5.2ではGitHubモデルよりレート制限が緩いため、その恩恵も受けられます。

## 前提条件

- モジュール01の完了（Azure OpenAIリソースがデプロイ済み）
- ルートディレクトリにAzure認証情報を含む `.env` ファイルが存在（モジュール01の `azd up` によって作成）

> **注意:** モジュール01を完了していない場合は、まずそちらのデプロイ手順に従ってください。

## プロンプトエンジニアリングの理解

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

プロンプトエンジニアリングとは、必要な結果が一貫して得られるよう入力テキストを設計することです。単に質問をするだけでなく、モデルが何をどう提供すべきかを正確に理解できるように依頼を構成することが重要です。

同僚に指示を出すことに例えると、「バグを直して」では曖昧です。「UserService.javaの45行目でヌルポインタ例外を防ぐためにヌルチェックを追加して修正してほしい」と具体的に言うほうが良いですよね。言語モデルも同じで、具体性と構造が結果を左右します。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4jはインフラストラクチャ部分—モデル接続、メモリ、メッセージタイプ—を提供し、プロンプトパターンはそのインフラを通して送る精緻に構成されたテキストです。重要な要素は `SystemMessage`（AIの振る舞いや役割を設定）と `UserMessage`（実際の要求を伝える）です。

## プロンプトエンジニアリングの基本

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

このモジュールの高度なパターンに入る前に、基礎的な5つのプロンプト技術を復習しましょう。これらはすべてのプロンプトエンジニアが知っておくべき基本的なビルディングブロックです。もし[クイックスタートモジュール](../00-quick-start/README.md#2-prompt-patterns)を既に試していれば、これらのテクニックは実際に見ているはずですが、ここでは背後にある概念的枠組みを示します。

### ゼロショットプロンプティング

最も単純な方法：例を使わずにモデルに直接指示を出します。モデルは訓練された知識だけで理解し、タスクを実行します。期待される動作が明白な単純な依頼に適しています。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*例を使わず直接指示 — 指示だけでタスクを推測するモデル*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 応答: "肯定"
```

**使用シーン:** 単純な分類、直接質問、翻訳、追加の指示無しでモデルが処理できるタスク。

### フューショットプロンプティング

例を提供し、モデルに従うべきパターンを示します。モデルは例から入力と出力の形式を学習し、新しい入力に適用します。望ましい形式や動作が明らかでないタスクにおいて一貫性が大幅に向上します。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*例から学ぶ — パターンを識別し新しい入力に適用するモデル*

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

**使用シーン:** カスタム分類、一貫したフォーマット、ドメイン特化タスク、ゼロショット結果が不安定な場合。

### チェーンオブソート

モデルに段階的な推論を示すように促します。答えに飛びつくのではなく、問題を分解し明確に論理を展開します。数学、論理、ステップを要する推論タスクの精度が向上します。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*段階的推論 — 複雑な問題を明確な論理的ステップに分解*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは次のように示しています：15 - 8 = 7、そして7 + 12 = 19個のリンゴ
```

**使用シーン:** 数学問題、論理パズル、デバッグ、推論過程を見せることで精度と信頼性が向上するタスク。

### 役割ベースのプロンプティング

質問の前にAIにペルソナや役割を割り当てます。これにより返答のトーン、深さ、焦点が形作られます。「ソフトウェアアーキテクト」は「ジュニア開発者」や「セキュリティ監査人」とは異なる助言をします。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*コンテキストとペルソナの設定 — 同じ質問でも役割によって異なる回答*

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

**使用シーン:** コードレビュー、チュータリング、特定分野の分析、専門レベルや視点に合わせた回答が必要な場合。

### プロンプトテンプレート

変数プレースホルダーを持つ再利用可能なプロンプトを作成します。毎回新しいプロンプトを書く代わりに、テンプレートを1度作り様々な値を埋め込みます。LangChain4jの `PromptTemplate` クラスは `{{variable}}` 構文でこれを簡単にします。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*変数プレースホルダーを使った再利用可能なプロンプト — 1つのテンプレートで多用途*

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

**使用シーン:** 異なる入力を使った繰り返しクエリ、バッチ処理、再利用可能なAIワークフロー構築、構造は変えずデータだけ変わるシナリオ。

---

これらの5つの基本でほとんどのプロンプトタスクに対応できます。この後のモジュールは、GPT-5.2の推論制御、自己評価、構造化出力能力を活用した**8つの高度なパターン**を紹介します。

## 高度なパターン

基本を踏まえた上で、このモジュールで紹介する8つの高度なパターンに進みましょう。問題により最適なアプローチは異なります。簡潔な回答が必要な質問もあれば、深い考察を要するものもあります。推論を可視化したい場合もあれば、結果だけ欲しい場合もあります。以下の各パターンは異なる状況に最適化されており、GPT-5.2の推論制御機能によりこれらの違いがより明確になっています。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*8つのプロンプトエンジニアリングパターンとその用途の概要*

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2の推論制御はモデルに行う思考の深さを指定可能—迅速な直接回答から深い探究まで*

**低熱心度（速く焦点を絞る）** - シンプルな質問に対し素早く直接的な回答が欲しい場合。モデルは最小限の推論（最大2ステップ）で処理します。計算、照会、単純な質問に適しています。

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

> 💡 **GitHub Copilotで試す:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開いて以下を尋ねてみてください：
> - 「低熱心度と高熱心度のプロンプトパターンの違いは何ですか？」
> - 「プロンプト内のXMLタグはAIの応答構造にどう役立っていますか？」
> - 「自己反省パターンと直接指示はどんな場合に使い分けるべきですか？」

**高熱心度（深く徹底的）** - 複雑な問題に対し包括的な分析を求める場合。モデルは徹底的に考察し詳細な推論を示します。システム設計、アーキテクチャの決定、複雑なリサーチに適しています。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（段階的進行）** - 複数ステップのワークフローに。モデルは最初に計画を示し、各ステップの作業を進行状況として説明し、最後にまとめます。マイグレーションや実装、複雑な多段階プロセスに適用。

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

チェーンオブソートプロンプティングは、モデルに推論プロセスを明示的に示させるため、複雑な課題の精度を高めます。ステップごとの分解が人間とAI双方の論理理解を助けます。

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatでも試してみてください:** このパターンについて質問：
> - 「長時間処理を行うタスク実行パターンの適用方法は？」
> - 「本番アプリでのツールプレアンブル構造設計のベストプラクティスは？」
> - 「UIで中間進捗をキャプチャ・表示する方法は？」

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*計画 → 実行 → まとめ の多段階ワークフロー*

**自己反省コード** - 生産レベルのコード生成向け。モデルは本番基準に沿ったコードを生成し、適切なエラーハンドリングも行います。新機能やサービス構築時に利用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*反復的改善サイクル — 生成、評価、課題特定、改善を繰り返す*

**構造化分析** - 一貫した評価向け。モデルは固定のフレームワーク（正確性、プラクティス、性能、セキュリティ、保守性）に沿ってコードをレビューします。コードレビューや品質評価に最適。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chatで試してみましょう:** 構造化分析について質問：
> - 「レビュー対象に応じた分析フレームワークのカスタマイズ方法は？」
> - 「構造化出力をプログラムで解析・活用する最良策は？」
> - 「異なるセッション間で一貫した重大度レベルを保つには？」

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*重大度レベル付きの一貫したコードレビューのためのフレームワーク*

**マルチターンチャット** - コンテキストが必要な会話向け。モデルは前のメッセージを記憶し、それを基に対話を深めます。インタラクティブなサポートや複雑なQ&Aに利用。

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

*会話のコンテキストが複数ターンにわたり蓄積され、トークン上限に達するまで保持される*

**段階的推論** - 論理を可視化したい問題向け。各ステップで明示的に推論を示します。数学問題、論理パズル、思考過程を理解したい場合に最適。

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

*問題を明確な論理ステップに分解する*

**制約付き出力** - 形式と長さの厳格なルールが必要な応答に。モデルは指定された書式と制約を厳密に守ります。要約や正確なフォーマットの出力が求められる場面に利用。

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

*特定の形式、長さ、構造の要件を強制*

## 既存のAzureリソースの利用

**デプロイ確認:**

ルートディレクトリにAzure認証情報を含む `.env` ファイルが存在することを確認（モジュール01で作成）:
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動:**

> **注意:** 既にモジュール01の `./start-all.sh` で全アプリケーションを起動している場合、本モジュールはポート8083で稼働中です。以下の起動コマンドはスキップし、直接 http://localhost:8083 にアクセスしてください。

**オプション1: Spring Boot Dashboardの利用（VS Codeユーザー推奨）**

DevコンテナにはSpring Boot Dashboard拡張機能が含まれており、VS Codeの左側アクティビティバーにあるSpring Bootアイコンから視覚的にすべてのSpring Bootアプリケーションを管理できます。

Spring Boot Dashboardでは：
- ワークスペース内の全Spring Bootアプリケーションの一覧が見られる
- ワンクリックでアプリの起動・停止が可能
- アプリログをリアルタイムで閲覧できる
- アプリのステータスを監視できる
「prompt-engineering」横の再生ボタンをクリックするだけで、このモジュールを開始できます。または、すべてのモジュールを一度に開始してください。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ダッシュボード" width="400"/>

**オプション 2: シェルスクリプトを使用する**

すべてのウェブアプリケーション（モジュール 01-04）を開始するには：

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

または、このモジュールだけを起動する場合：

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

両方のスクリプトは、ルートの `.env` ファイルから環境変数を自動的に読み込み、JARファイルが存在しない場合はビルドします。

> **注意:** すべてのモジュールを手動でビルドしてから起動することを好む場合：
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

<img src="../../../translated_images/ja/dashboard-home.5444dbda4bc1f79d.webp" alt="ダッシュボードホーム" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*特性とユースケースを示した8つのプロンプトエンジニアリングパターンすべてを表示するメインダッシュボード*

## パターンの探求

ウェブインターフェースでは、さまざまなプロンプト戦略を試すことができます。各パターンは異なる問題を解決しますので、それぞれの手法が効果的な場面を試してみてください。

### Low vs High Eagerness（低・高熱意）

「200の15%は何か？」のような単純な質問をLow Eagernessで尋ねると、即座に直接的な答えが得られます。次に、「高トラフィックAPIのキャッシュ戦略を設計してください」といった複雑な質問をHigh Eagernessで試してください。モデルが時間をかけて詳細な推論を提供するのがわかります。同じモデル、同じ質問構造ですが、プロンプトがどれだけ思考するかを指示しています。

<img src="../../../translated_images/ja/low-eagerness-demo.898894591fb23aa0.webp" alt="低熱意デモ" width="800"/>

*最小限の推論で迅速な計算*

<img src="../../../translated_images/ja/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高熱意デモ" width="800"/>

*包括的なキャッシュ戦略 (2.8MB)*

### タスク実行（ツールのプレアンブル）

複数ステップのワークフローは、事前計画と進行状況の説明が有効です。モデルは何をするかを概説し、各ステップを説明し、結果をまとめます。

<img src="../../../translated_images/ja/tool-preambles-demo.3ca4881e417f2e28.webp" alt="タスク実行デモ" width="800"/>

*ステップごとの説明付きでRESTエンドポイントを作成 (3.9MB)*

### セルフリフレクティングコード

「メール検証サービスを作成してください」と試してみてください。コードを生成して終わるのではなく、モデルは生成したコードを品質基準に照らして評価し、欠点を特定して改善します。完成するまで繰り返す様子が見られます。

<img src="../../../translated_images/ja/self-reflecting-code-demo.851ee05c988e743f.webp" alt="セルフリフレクティングコードデモ" width="800"/>

*完全なメール検証サービス (5.2MB)*

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは正確さ、プラクティス、パフォーマンス、セキュリティの固定カテゴリに基づき、重大度レベルでコードを分析します。

<img src="../../../translated_images/ja/structured-analysis-demo.9ef892194cd23bc8.webp" alt="構造化分析デモ" width="800"/>

*フレームワークベースのコードレビュー*

### マルチターンチャット

「Spring Bootとは何ですか？」と尋ねた後に、「例を見せてください」とすぐに続けてください。モデルは最初の質問を覚えているので、特定のSpring Bootの例を提供します。メモリがなければ、2回目の質問はあいまいすぎます。

<img src="../../../translated_images/ja/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="マルチターンチャットデモ" width="800"/>

*質問間のコンテキスト保持*

### ステップバイステップ推論

数学問題を選び、Step-by-Step ReasoningとLow Eagernessの両方で試してみてください。Low eagernessは答えだけを高速に返しますが不透明です。Step-by-stepはすべての計算と判断過程を示します。

<img src="../../../translated_images/ja/step-by-step-reasoning-demo.12139513356faecd.webp" alt="ステップバイステップ推論デモ" width="800"/>

*明示的なステップ付き数学問題*

### 制約付き出力

特定のフォーマットや語数が必要な場合、このパターンは厳密な遵守を強制します。100語ぴったりの箇条書き形式で要約を生成してみてください。

<img src="../../../translated_images/ja/constrained-output-demo.567cc45b75da1633.webp" alt="制約付き出力デモ" width="800"/>

*フォーマット制御された機械学習の要約*

## 実際に学んでいること

**推論努力がすべてを変える**

GPT-5.2では、プロンプトによって計算努力を制御できます。低努力は高速で最小限の探求を伴います。高努力はモデルが深く考えるために時間をかけます。課題の複雑さに応じて努力を調整することを学んでいます。単純な質問に時間を無駄にせず、複雑な決定は慎重に行ってください。

**構造が行動を導く**

プロンプト中のXMLタグに注目してください。装飾ではなく、モデルは自由形式のテキストよりも構造化された指示に従うのが確実です。複数工程や複雑なロジックが必要な場合、構造がモデルに現在の位置と次に何をすべきかを追跡させます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="プロンプト構造" width="800"/>

*明確なセクションとXMLスタイルの構造を持つ良好なプロンプトの構造*

**自己評価による品質向上**

セルフリフレクティングパターンは品質基準を明示することで機能します。モデルが「正しくやることを期待する」のではなく、「正しい」とは何か（論理の正確さ、エラーハンドリング、性能、セキュリティ）を明確に伝えます。モデルは自身の出力を評価して改善でき、コード生成を偶然からプロセスに変えます。

**コンテキストは有限**

マルチターン会話は各リクエストにメッセージ履歴を含めることで機能しますが、トークン数の上限があります。会話が長くなると、この上限を超えないように関連コンテキストを保持する戦略が必要になります。このモジュールはメモリの仕組みを示し、後のモジュールで要約すべき時、忘れるべき時、取得すべき時を学びます。

## 次のステップ

**次のモジュール:** [03-rag - RAG（検索拡張生成）](../03-rag/README.md)

---

**ナビゲーション:** [← 前へ: Module 01 - Introduction](../01-introduction/README.md) | [メインに戻る](../README.md) | [次へ: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本ドキュメントはAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性の向上に努めていますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文が正本としての権威ある情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や誤訳についても責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->