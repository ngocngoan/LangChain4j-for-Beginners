# Module 02: GPT-5.2によるプロンプトエンジニアリング

## 目次

- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの基本](../../../02-prompt-engineering)
  - [ゼロショットプロンプティング](../../../02-prompt-engineering)
  - [数ショットプロンプティング](../../../02-prompt-engineering)
  - [チェーン・オブ・ソート](../../../02-prompt-engineering)
  - [役割ベースのプロンプティング](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの活用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低い熱意 vs 高い熱意](../../../02-prompt-engineering)
  - [タスク実行（ツールの前置き）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化分析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [段階的推論](../../../02-prompt-engineering)
  - [制約された出力](../../../02-prompt-engineering)
- [本当に学ぶこと](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## 学習内容

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="学習内容" width="800"/>

前回のモジュールでは、メモリが会話型AIをどのように可能にするかを学び、GitHub Modelsを使った基本的な対話を体験しました。今回はAzure OpenAIのGPT-5.2を使い、質問の仕方—つまりプロンプトそのものに焦点を当てます。プロンプトの構成方法次第で、得られる応答の質は大きく変わります。まず基本的なプロンプト技術の復習を行い、その後GPT-5.2の能力を最大限に活かす8つの高度なパターンを紹介します。

GPT-5.2を使用する理由は、推論の制御機能が追加されたためです — モデルにどの程度考えさせて答えさせるかを指定できます。これにより様々なプロンプト戦略の違いがより明確になり、どの戦略をいつ使うべきか理解しやすくなります。また、GPT-5.2はGitHub Modelsに比べ、Azureでのレート制限が緩和されています。

## 前提条件

- モジュール01を完了していること（Azure OpenAIリソースが展開済み）
- ルートディレクトリにAzure認証情報を含む `.env` ファイルがあること（モジュール01の `azd up` で作成）

> **注意:** モジュール01を未実施の場合は、まずそちらの展開手順を完了してください。

## プロンプトエンジニアリングの理解

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="プロンプトエンジニアリングとは？" width="800"/>

プロンプトエンジニアリングとは、必要な結果を安定して得られる入力テキストを設計することです。ただ単に質問するだけでなく、モデルが何をどう回答すれば良いか正確に理解できる形に要求を構造化する作業です。

同僚に指示を出すイメージを持つと良いでしょう。「バグを直して」では曖昧ですが、「UserService.javaの45行目のNullPointerExceptionをnullチェックを追加して修正してください」と言えば具体的です。言語モデルも同様に、具体性と構造が重要です。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4jの役割" width="800"/>

LangChain4jはモデル接続、メモリ、メッセージタイプといった基盤インフラを提供し、プロンプトパターンはそのインフラを通じて送る緻密に構造化されたテキストです。重要な構成要素は、AIの振る舞いや役割を設定する `SystemMessage` と、実際の要求を運ぶ `UserMessage` です。

## プロンプトエンジニアリングの基本

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="5つのプロンプトエンジニアリングパターン概要" width="800"/>

このモジュールで高度なパターンを紹介する前に、5つの基本的なプロンプト技術を復習します。これらはすべてのプロンプトエンジニアが知っておくべき基礎であり、すでに [Quick Startモジュール](../00-quick-start/README.md#2-prompt-patterns)で実際に見ている方もいるでしょう。ここにその概念的枠組みを示します。

### ゼロショットプロンプティング

最も単純な方法は、例なしでモデルに直接指示を与えることです。モデルは訓練時の知識のみを頼りにタスクを理解し実行します。期待される動作が明白なシンプルな依頼に適します。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="ゼロショットプロンプティング" width="800"/>

*例なしの直接指示 — モデルは指示だけからタスクを推測する*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 返答: 「ポジティブ」
```

**使用例:** 単純な分類、直接質問、翻訳、追加のガイダンスなしで対応可能なタスク。

### 数ショットプロンプティング

モデルに従ってほしいパターンを示す例を与えます。モデルは例から期待される入出力形式を学び、新規の入力にも適用します。望ましい形式や動作が明確でないタスクにおいて、一貫性を劇的に向上させます。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="数ショットプロンプティング" width="800"/>

*例から学習 — モデルはパターンを識別し、新しい入力に適用する*

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

**使用例:** カスタム分類、一貫したフォーマット、特定ドメインのタスクや、ゼロショットで不安定な結果を改善したい場合。

### チェーン・オブ・ソート

モデルに段階的な推論を示させます。即答せず、問題を分解し各段階を明示的に解析することで、数学や論理、多段階推論の精度が向上します。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="チェーン・オブ・ソートプロンプティング" width="800"/>

*段階的推論 — 複雑な問題を明確な論理ステップに分解*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは次のように示しています：15 - 8 = 7、次に 7 + 12 = 19 個のリンゴ
```

**使用例:** 数学問題、論理パズル、デバッグ、推論プロセスの提示により精度や信頼性を高めたい場合。

### 役割ベースのプロンプティング

質問前にAIのペルソナや役割を設定することで、応答のトーンや深さ、焦点に影響を与えます。「ソフトウェアアーキテクト」と「ジュニア開発者」または「セキュリティ監査員」では助言が異なります。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="役割ベースのプロンプティング" width="800"/>

*コンテキストとペルソナの設定 — 役割によって同じ質問への応答が変わる*

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

**使用例:** コードレビュー、指導、ドメイン固有の分析、特定の専門レベルや視点に応じた応答が必要な場合。

### プロンプトテンプレート

変数プレースホルダーを用いて再利用可能なプロンプトを作成します。毎回新たに書く代わりに、一度テンプレートを定義して異なる値を埋め込むだけ。LangChain4jの `PromptTemplate` クラスは `{{variable}}` 形式で簡単に扱えます。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="プロンプトテンプレート" width="800"/>

*変数プレースホルダー付きの再利用可能なプロンプト — 1つのテンプレート、複数の用途*

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

**使用例:** 異なる入力の繰り返し質問、バッチ処理、再利用可能なAIワークフロー構築、構造は同じでデータだけ変わる場合。

---

これら5つの基本は多くのプロンプトタスクに有用なツールです。本モジュールの残りでは、GPT-5.2の推論制御や自己評価、構造化出力を活用した**8つの高度なパターン**を紹介します。

## 高度なパターン

基本を押さえたので、このモジュール独自の8つの高度なパターンに進みましょう。全ての問題が同じアプローチを必要とするわけではありません。中には素早い回答が必要な質問もあれば、深い思考が求められるものもあります。推論を可視化したい場合もあれば、結果のみ欲しい場合もあります。以下の各パターンは異なるシナリオに最適化されており、GPT-5.2の推論制御がそれぞれの違いを際立たせます。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="8つのプロンプティングパターン" width="800"/>

*8つのプロンプトエンジニアリングパターンとその利用ケースの概要*

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2の推論制御" width="800"/>

*GPT-5.2の推論制御により、モデルにどのくらい考えさせるか指定可能 — 速い直接答えから深い探索まで*

**低い熱意（クイックかつフォーカス）** — シンプルな質問で素早く直接的な回答が欲しい場合。モデルの推論は最小限（最大2ステップ）。計算、検索、単純な質問に適用。

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

> 💡 **GitHub Copilotで探求:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) を開き、以下を質問:
> - 「低い熱意と高い熱意のプロンプトパターンの違いは？」
> - 「プロンプト内のXMLタグはAIの応答構造にどう寄与しているか？」
> - 「自己反省パターンと直接指示はいつ使い分ける？」

**高い熱意（深く徹底的）** — 複雑な問題に対し包括的な分析が欲しい場合。モデルはじっくり探索し詳細な推論を示す。システム設計、アーキテクチャ決定、複雑な調査に最適。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（段階的進行）** — 多段階ワークフローに対応。モデルは事前計画、動作間のナレーション、最後に要約を提示。移行、実装、多段階処理に利用。

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

チェーン・オブ・ソートはモデルに推論過程を明示させ、複雑なタスクの精度向上を図る方法です。段階的な分解は人間とAI双方の理解を助けます。

> **🤖 GitHub Copilot Chatで試す:** 以下を質問:
> - 「長時間操作のためにタスク実行パターンをどう調整する？」
> - 「本番アプリのツール前置きはどう構造化すべきか？」
> - 「中間進捗をUI上で捕捉し表示する方法は？」

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="タスク実行パターン" width="800"/>

*多段階タスクの計画 → 実行 → 要約のワークフロー*

**自己反省コード** — 本番品質のコード生成向け。モデルは適切なエラーハンドリングも含め生産基準に則ったコードを生成。新機能やサービス開発に適用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自己反省サイクル" width="800"/>

*反復的改善サイクル — 生成・評価・問題特定・改良・繰り返し*

**構造化分析** — 一貫した評価向け。モデルは固定フレームワーク（正確性、慣習、性能、セキュリティ、保守性）でコードをレビュー。コードレビューや品質評価に利用。

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

> **🤖 GitHub Copilot Chatで試す:** 構造化分析について以下を質問:
> - 「異なるタイプのコードレビュー用に分析フレームワークをカスタマイズする方法は？」
> - 「構造化出力をプログラムから解析・活用する最良の方法は？」
> - 「異なるレビューで一貫した重大度レベルをどう確保する？」

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="構造化分析パターン" width="800"/>

*重大度レベルを用いた一貫性のあるコードレビュー用フレームワーク*

**マルチターンチャット** — コンテキストが必要な会話向け。モデルは過去のメッセージを記憶し、それに基づき応答を発展させる。インタラクティブヘルプや複雑なQ&Aに適用。

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

*複数ターンを通じて会話コンテキストが蓄積され、トークン制限に達するまで維持される*

**段階的推論** — ロジックを可視化したい問題向け。モデルが各ステップごとに明示的な推論を示す。数学、論理パズル、思考プロセスを理解したい時に適用。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="段階的推論パターン" width="800"/>

*問題を明示的な論理ステップに分解*

**制約された出力** — 特定フォーマットの厳守が必要な応答用。モデルはフォーマットや長さのルールに厳格に従う。要約や正確な出力構造が必要な場合に利用。

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

*特定のフォーマット、長さ、構造要件の厳守*

## 既存のAzureリソースの活用

**展開の確認:**

ルートディレクトリにAzure認証情報を含む `.env` ファイルが存在することを確認（モジュール01で作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動:**

> **注意:** もしモジュール01の `./start-all.sh` ですでにすべてのアプリケーションを起動している場合、本モジュールはポート8083で既に稼働中です。以下の起動コマンドは省略して http://localhost:8083 に直接アクセスできます。

**オプション1: Spring Boot Dashboardの使用（VS Codeユーザーに推奨）**

DevコンテナにはSpring Boot Dashboard拡張機能が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Codeの左側のアクティビティバーにあるSpring Bootアイコンを探してください。

Spring Boot Dashboardからは:
- ワークスペース内の全Spring Bootアプリを一覧表示
- ワンクリックでアプリケーションの起動・停止
- リアルタイムでアプリログを閲覧
- アプリケーションのステータスを監視

単に「prompt-engineering」の横にある再生ボタンをクリックしてこのモジュールを開始するか、すべてのモジュールを一度に開始してください。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ダッシュボード" width="400"/>

**オプション 2: シェルスクリプトの使用**

すべてのウェブアプリケーション（モジュール01-04）を開始する：

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

または、このモジュールのみ開始する：

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

両方のスクリプトはルートの `.env` ファイルから環境変数を自動的に読み込み、JARがなければビルドします。

> **注意:** すべてのモジュールを手動でビルドしてから開始したい場合：
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

*8つのプロンプトエンジニアリングパターンとそれぞれの特性やユースケースを示したメインダッシュボード*

## パターンの探求

ウェブインターフェイスではさまざまなプロンプト戦略を試せます。各パターンは異なる課題を解決します—それぞれのアプローチが活きる場面を試してみてください。

> **注意: ストリーミング vs 非ストリーミング** — 各パターンページには二つのボタンがあります：**🔴 ストリーム応答（ライブ）** と **非ストリーミング** オプション。ストリーミングはサーバー送信イベント（SSE）を使い、モデルが生成するトークンをリアルタイムに表示しますので進捗がすぐに見えます。非ストリーミングは応答全体を待ってから表示します。深い推論を必要とするプロンプト（例：高いイーガネス、自己反省的コード）では非ストリーミングの呼び出しが非常に長時間かかることがあります—時には数分間も進捗が見えません。**複雑なプロンプトを試すときはストリーミングを使うのが良い**でしょう。モデルが動いている様子が見え、リクエストがタイムアウトした印象を避けられます。
>
> **注意: ブラウザ要件** — ストリーミング機能は Fetch Streams API（`response.body.getReader()`）を使用し、完全なブラウザ（Chrome、Edge、Firefox、Safari）が必要です。VS Code内蔵のシンプルブラウザはReadableStream APIをサポートしないためストリーミングは動作しません。シンプルブラウザでは非ストリーミングのボタンは通常通り動作します—影響を受けるのはストリーミングボタンのみです。完全な体験には外部ブラウザで `http://localhost:8083` を開いてください。

### 低イーガネス vs 高イーガネス

「200の15%は何ですか？」のような簡単な質問は低イーガネスで聞いてみてください。即時で直接的な答えが得られます。次に「高トラフィックAPIのキャッシュ戦略を設計してください」のような複雑な質問を高イーガネスで尋ねます。**🔴 ストリーム応答（ライブ）** をクリックし、モデルの詳細な推論がトークンごとに現れる様子を見てください。同じモデル、同じ質問の形でも、プロンプトが考える量を指示しています。

### タスク実行（ツール前置き）

多段階のワークフローは、前もって計画し進捗を解説することが有効です。モデルは何をするか概要を示し、各ステップを解説してから結果をまとめます。

### 自己反省的コード

「メール検証サービスを作ってみてください」と試してください。コードを生成して終わるのではなく、モデルは生成物を質の基準に照らして評価し、弱点を特定して改善します。コードが本番基準を満たすまで繰り返す様子が見られます。

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは正確性、プラクティス、パフォーマンス、セキュリティという固定されたカテゴリでコードを分析し、重大度をつけます。

### マルチターンチャット

「Spring Bootとは何ですか？」と質問し、続けて「例を見せて」と聞いてみてください。モデルは最初の質問を覚えており、具体的なSpring Bootの例を示します。メモリがなければ二つ目の質問はあいまいすぎます。

### ステップバイステップ推論

数学の問題を選び、ステップバイステップ推論と低イーガネスの両方で試してみてください。低イーガネスは答えだけを速く返しますが分かりにくいです。ステップバイステップ推論は計算や判断をすべて示します。

### 制約付き出力

特定の形式や単語数が必要な場合、このパターンが厳密に遵守させます。100語ぴったりで箇条書きの要約を生成してみてください。

## 本当に学んでいること

**推論の努力が全てを変える**

GPT-5.2はプロンプトを通じて計算努力を制御できます。努力が低いと速い応答ですが探索は最小限。努力が高いと深く思考に時間をかけます。課題の複雑さに合わせて努力を調整することを学びます—簡単な質問に時間を無駄にせず、複雑な決定を急がないことです。

**構造が行動を導く**

プロンプト内のXMLタグに気づきましたか？装飾ではありません。モデルは自由形式テキストよりも構造化された指示により信頼性高く従います。多段階処理や複雑な論理が必要なときは、構造がモデルの現在地や次の処理を追跡するのに役立ちます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="プロンプト構造" width="800"/>

*明確なセクションとXMLスタイルの組織化を持つよく構造化されたプロンプトの構成*

**品質は自己評価から**

自己反省パターンは品質基準を明示的に示します。モデルが「正しくやる」と期待するのではなく、「正しい」とは何か（正確なロジック、エラーハンドリング、パフォーマンス、セキュリティ）を正確に伝えます。モデルは自身の出力を評価し改善できます。これによりコード生成が単なるギャンブルからプロセスになります。

**コンテキストは有限**

マルチターン会話はメッセージ履歴をリクエストに含めることで成立しますが、トークン数の上限があります。会話が長くなるとその上限に達しないよう関連するコンテキストを維持する戦略が必要です。このモジュールでメモリの仕組みを学び、後でいつ要約し、いつ忘れ、いつ検索するかを知ることになります。

## 次のステップ

**次のモジュール:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ナビゲーション:** [← 前へ: モジュール 01 - はじめに](../01-introduction/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確性を期しておりますが、自動翻訳には誤りや不正確な箇所が含まれている可能性があることをご了承ください。原文のネイティブ言語による文書が正式な情報源とみなされます。重要な情報については、専門の人間翻訳を推奨します。本翻訳の使用により生じたいかなる誤解や解釈の相違についても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->