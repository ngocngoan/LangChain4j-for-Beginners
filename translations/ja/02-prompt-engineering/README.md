# Module 02: GPT-5.2によるプロンプトエンジニアリング

## 目次

- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの基本](../../../02-prompt-engineering)
  - [ゼロショットプロンプト](../../../02-prompt-engineering)
  - [ファーショットプロンプト](../../../02-prompt-engineering)
  - [チェーン・オブ・ソート](../../../02-prompt-engineering)
  - [役割ベースのプロンプト](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [既存のAzureリソースの活用](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低意欲 vs 高意欲](../../../02-prompt-engineering)
  - [タスク実行（ツールのプレアンブル）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化分析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [段階的推論](../../../02-prompt-engineering)
  - [制約付き出力](../../../02-prompt-engineering)
- [本当に学ぶべきこと](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## 学習内容

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="学習内容" width="800"/>

前のモジュールでは、メモリが会話型AIを可能にする方法と基本的なやり取りにGitHub Modelsを使用する方法を学びました。今回は質問の仕方、つまりプロンプト自体に焦点を当て、Azure OpenAIのGPT-5.2を使って進めます。プロンプトの組み立て方が、得られる応答の質に大きな影響を与えます。基本的なプロンプト技術の確認から始め、GPT-5.2の能力を最大限に活かした8つの高度なパターンへと進みます。

GPT-5.2は推論制御を導入しており、モデルにどの程度考えるか指示できます。これによりさまざまなプロンプト戦略が明確になり、どの手法をいつ使うか理解しやすくなります。また、Azure上のGPT-5.2はGitHubモデルよりもレート制限が緩いため、その利点も享受します。

## 前提条件

- モジュール01を完了済み（Azure OpenAIリソースのデプロイ済み）
- ルートディレクトリにAzure資格情報が含まれる `.env` ファイル（モジュール01の `azd up` コマンドで作成）

> **注意:** モジュール01を完了していない場合は、まずそこでのデプロイ手順を実行してください。

## プロンプトエンジニアリングの理解

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="プロンプトエンジニアリングとは？" width="800"/>

プロンプトエンジニアリングは、必要な結果を一貫して得るための入力テキスト設計のことです。単に質問をするだけでなく、モデルが求めることとその出力方法を正確に理解するようなリクエスト構造にすることが重要です。

これは同僚に指示を出すのに似ています。「バグを修正して」というのは曖昧ですが、「UserService.java の45行目のヌルポインター例外をヌルチェックを追加して修正して」と具体的に指示するほうが明確です。言語モデルも同じで、具体性と構造が重要です。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4jの位置付け" width="800"/>

LangChain4jはモデル接続、メモリ、メッセージタイプなどの基盤を提供し、プロンプトパターンはその基盤を通じて送る構造化されたテキストです。重要な構成要素はAIの振る舞いと役割を設定する`SystemMessage`と、実際のリクエストを運ぶ`UserMessage`です。

## プロンプトエンジニアリングの基本

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="5つのプロンプトパターン概要" width="800"/>

このモジュールの高度なパターンに入る前に、5つの基礎的なプロンプト技法を復習しましょう。これはすべてのプロンプトエンジニアが知るべき基本ブロックです。[クイックスタートモジュール](../00-quick-start/README.md#2-prompt-patterns)を済ませていれば、実践を通じて見てきた概念的枠組みです。

### ゼロショットプロンプト

最もシンプルなアプローチ：例を示さず直接指示を与えます。モデルは学習済みの知識だけでタスクを理解し実行します。明確な期待動作がある単純な依頼に適します。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="ゼロショットプロンプト" width="800"/>

*例なしの直接指示 — 指示のみでタスクを推測*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 返信:「ポジティブ」
```

**使用する場合:** 単純な分類、直接的な質問、翻訳、追加の指示なしでモデルが扱えるタスク。

### ファーショットプロンプト

モデルに従ってほしいパターンの例を提供します。モデルは例から期待される入出力形式を学び、新しい入力に適用します。望むフォーマットや振る舞いが明確でない場合に一貫性が大きく向上します。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="ファーショットプロンプト" width="800"/>

*例から学ぶ — パターンを理解し新規入力に適用*

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

**使用する場合:** カスタム分類、一貫したフォーマット、ドメイン固有タスクやゼロショット結果が不安定な場合。

### チェーン・オブ・ソート

段階的な推論過程をモデルに示させます。即答せず問題を分解し、各ステップを明示的に処理します。数学、論理、多段階推論タスクの精度向上に効果的です。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="チェーン・オブ・ソートプロンプト" width="800"/>

*段階的な推論 — 複雑な問題を明確な論理ステップに分解*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは次のように示しています：15 - 8 = 7、次に7 + 12 = 19個のリンゴ
```

**使用する場合:** 数学問題、論理パズル、デバッグ、推論過程を見せることで正確さ・信頼性が高まる場合。

### 役割ベースのプロンプト

質問前にAIのペルソナや役割を設定します。これにより応答の口調や深さ、焦点がコンテキストに沿って変わります。「ソフトウェアアーキテクト」では「ジュニア開発者」や「セキュリティ監査人」と異なる助言が得られます。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="役割ベースのプロンプト" width="800"/>

*コンテキストとペルソナ設定 — 役割によって同じ質問でも異なる回答*

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

**使用する場合:** コードレビュー、指導、ドメイン特化の分析、特定の専門性や視点にあわせた回答が必要なとき。

### プロンプトテンプレート

変数プレースホルダーを使って再利用可能なプロンプトを作成します。毎回新しいプロンプトを書く代わりに、一度テンプレートを定義し異なる値を当てはめます。LangChain4jの`PromptTemplate`クラスは`{{variable}}`構文でこれを簡単にします。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="プロンプトテンプレート" width="800"/>

*変数を持つ再利用可能プロンプト — 1つのテンプレートを多用途に*

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

**使用する場合:** 異なる入力での繰り返しクエリ、バッチ処理、再利用可能なAIワークフロー構築、構造は同じでデータだけ変わる場合。

---

これらの5つの基本はほとんどのプロンプトタスクで強力なツールキットになります。このモジュールの残りは、GPT-5.2の推論制御、自己評価、構造化出力の能力を活かした**8つの高度なパターン**に基づいています。

## 高度なパターン

基本を押さえたら、このモジュールの特徴である8つの高度なパターンに進みましょう。すべての問題に同じアプローチが必要なわけではありません。短時間で答えが欲しい質問もあれば、深い検討が必要なこともあります。推論を明示的に見せたい場合もあれば、結果だけが欲しい場合もあります。下記の各パターンは異なるシナリオに最適化されており、GPT-5.2の推論制御によって違いがより際立ちます。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="8つのプロンプトパターン" width="800"/>

*8つのプロンプトエンジニアリングパターンとその使用例の概要*

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2による推論制御" width="800"/>

*GPT-5.2の推論制御は、モデルにどの程度考えさせるかを指定できます — 迅速な直接回答から深い探求まで*

<img src="../../../translated_images/ja/reasoning-effort.db4a3ba5b8e392c1.webp" alt="推論努力の比較" width="800"/>

*低意欲（速く直接的） vs 高意欲（徹底的で探索的）な推論アプローチ*

**低意欲（迅速かつ集中）** - 速く直接的な回答が必要な単純な質問に適しています。モデルは最小限の推論（最大2ステップ）を行います。計算、検索、単純な質問に利用。

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

> 💡 **GitHub Copilotで試す:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開き次のように質問してみましょう:
> - 「低意欲と高意欲プロンプトパターンの違いは何ですか？」
> - 「プロンプト中のXMLタグはAIの応答構造のどのように役立ちますか？」
> - 「自己反省パターンと直接指示はいつ使い分けるべきですか？」

**高意欲（深く徹底的）** - 包括的な分析が欲しい複雑な問題に適しています。モデルは徹底的に探索し詳細な推論を示します。システム設計、アーキテクチャ決定、複雑な調査に使用。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（段階的進行）** - 複数ステップのワークフロー用。モデルは最初に計画を示し、作業しながら各ステップを説明し、最後に要約をします。マイグレーション、実装、多段階処理に適用。

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

チェーン・オブ・ソートプロンプトは推論過程を明示的に示すようモデルに求め、複雑なタスクの正確さを高めます。段階的な問題分解は人間とAI双方の理解を助けます。

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** 以下について質問してみましょう:
> - 「長時間実行の操作にタスク実行パターンをどう適用しますか？」
> - 「本番環境のツールプレアンブル構造のベストプラクティスは何ですか？」
> - 「中間進捗のキャプチャとUI表示はどう行いますか？」

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="タスク実行パターン" width="800"/>

*計画 → 実行 → 要約という多段ステップのワークフロー*

**自己反省コード** - 本番品質コード生成用。モデルは本番向け標準と適切なエラー処理に従ってコードを生成します。新規機能やサービス構築時に使用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自己反省サイクル" width="800"/>

*反復的な改善ループ — 生成、評価、問題特定、改善、繰り返し*

**構造化分析** - 一貫した評価に。モデルは固定のフレームワーク（正確性、プラクティス、パフォーマンス、セキュリティ、保守性）を用いてコードを見直します。コードレビューや品質評価に最適。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** 構造化分析について質問してみましょう:
> - 「コードレビューの種類に応じた分析フレームワークをカスタマイズするには？」
> - 「構造化出力をプログラム的に解析・活用する最善の方法は？」
> - 「異なるレビューセッション間で一貫した重大度レベルをどう確保するか？」

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="構造化分析パターン" width="800"/>

*重大度レベルを伴う一貫したコードレビュー用フレームワーク*

**マルチターンチャット** - コンテキストが必要な対話に。モデルは過去のメッセージを記憶し積み上げます。対話型ヘルプや複雑なQ&Aにお勧め。

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

*複数ターンの会話コンテキストが累積し、トークン制限に達するまで続く様子*

**段階的推論** - 論理が見える必要がある問題用。モデルが明示的に各ステップの推論を提示します。数学問題、論理パズル、思考過程を理解したい場合に利用。

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

*問題を明確な論理ステップに分解*

**制約付き出力** - 特定のフォーマット要件がある回答用。モデルが形式や長さのルールを厳密に守ります。要約や正確な構造が必要なときに適用。

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

<img src="../../../translated_images/ja/constrained-output-pattern.0ce39a682a6795c2.webp" alt="制約付き出力パターン" width="800"/>

*特定の形式、長さ、構造の要件を強制*

## 既存のAzureリソースの活用

**デプロイの確認:**

ルートディレクトリにAzure資格情報が含まれる `.env` ファイルがあることを確認（モジュール01で作成）：
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**アプリケーションの起動:**

> **注意:** もしモジュール01で `./start-all.sh` を使ってすでにすべてのアプリケーションを起動している場合、このモジュールは既にポート8083で動作しています。以下の起動コマンドは省略可能で、http://localhost:8083 へ直接アクセスしてください。

**オプション1: Spring Bootダッシュボードの使用（VS Codeユーザー推奨）**

開発コンテナにはSpring Bootダッシュボード拡張機能が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Codeの左側アクティビティバーでSpring Bootアイコンを探してください。
Spring Boot ダッシュボードから、以下ができます：
- ワークスペース内のすべての Spring Boot アプリケーションの表示
- アプリケーションをワンクリックで起動/停止
- アプリケーションログをリアルタイムで表示
- アプリケーションの状態を監視

「prompt-engineering」の隣にある再生ボタンをクリックしてこのモジュールを起動するか、すべてのモジュールを一度に起動してください。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**オプション 2：シェルスクリプトの使用**

すべてのウェブアプリケーション（モジュール01-04）を起動：

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

両方のスクリプトは、ルートの `.env` ファイルから環境変数を自動的に読み込み、JARファイルがなければビルドします。

> **注意:** 起動前にすべてのモジュールを手動でビルドしたい場合：
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

*8つのプロンプトエンジニアリングパターンの特徴とユースケースを示すメインダッシュボード*

## パターンの探求

ウェブインターフェースではさまざまなプロンプト戦略を試せます。各パターンは異なる問題を解決するので、どのアプローチが効果的か試してみてください。

### 低い熱意 vs 高い熱意

「15% of 200 は？」のような簡単な質問を低い熱意で聞くと、即答でシンプルな答えが返ってきます。次に「高トラフィックAPIのキャッシュ戦略を設計してください」のような複雑な質問を高い熱意で聞くと、モデルがゆっくり考えながら詳細な理由付けを提供します。同じモデル、同じ質問形式でも、プロンプトが思考の深さを指示するのです。

<img src="../../../translated_images/ja/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*最小限の推論で素早い計算*

<img src="../../../translated_images/ja/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*包括的なキャッシュ戦略（2.8MB）*

### タスク実行（ツールの前置き）

複数ステップのワークフローでは、事前計画と進捗のナレーションが役立ちます。モデルはやることを概説し、各ステップを説明し、結果を要約します。

<img src="../../../translated_images/ja/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ステップ毎にナレーションしながらRESTエンドポイントを作成（3.9MB）*

### 自己評価コード

「メール検証サービスを作成して」と試してください。単にコード生成で止まらず、モデルは生成物を品質基準で評価し、弱点を識別して改善します。コードが本番品質標準に達するまで繰り返す様子が見えます。

<img src="../../../translated_images/ja/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完全なメール検証サービス（5.2MB）*

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは正確性、プラクティス、パフォーマンス、セキュリティという固定のカテゴリと重大度レベルを使って解析します。

<img src="../../../translated_images/ja/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*フレームワークベースのコードレビュー*

### マルチターンチャット

「Spring Bootとは？」と質問し、すぐに「例を見せて」と続けてください。モデルは最初の質問を覚えており、特定のSpring Boot例を返します。メモリがなければ、２つめの質問は曖昧すぎます。

<img src="../../../translated_images/ja/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*質問間のコンテキスト保持*

### ステップバイステップ推論

数学の問題を選び、ステップバイステップ推論と低熱意で試してみてください。低熱意は答えだけを高速に返しますが、中身は不透明です。ステップバイステップはすべての計算と判断を詳細に示します。

<img src="../../../translated_images/ja/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*明示的な手順での数学問題*

### 制約付き出力

特定フォーマットや文字数が必要な場合、このパターンは厳格に守らせます。ちょうど100単語の箇条書き要約を生成してみてください。

<img src="../../../translated_images/ja/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*フォーマット制御された機械学習要約*

## 本当に学んでいること

**推論労力がすべてを変える**

GPT-5.2 はプロンプトを通じて計算労力を制御できます。労力が低いと速く最小限の探究で応答。労力が高いとモデルは時間をかけて深く考えます。課題の複雑さに応じて努力を調整することを学んでいます—簡単な質問で時間を無駄にせず、複雑な決断を急がないように。

**構造が行動を導く**

プロンプトにXMLタグがあるのに気づきましたか？装飾ではありません。モデルは自由形式のテキストよりも構造化された指示に従うのが得意です。多段階プロセスや複雑な論理が必要な時、構造はモデルが現在位置や次に何をするかを把握するのに役立ちます。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*明確なセクションとXMLスタイルの構成を持つ良好なプロンプトの構造*

**自己評価による品質**

自己反省パターンは品質基準を明示することで機能します。モデルに「正しくやれ」と望む代わりに、「正しく」とは何か（正確な論理、エラー処理、性能、セキュリティ）を明確に伝えます。モデルは出力を自ら評価し改善できます。これによりコード生成は単なる賭けからプロセスへと変わります。

**コンテキストは有限**

マルチターン会話はリクエストごとにメッセージ履歴を含めることで機能します。しかし制限があります—すべてのモデルに最大トークン数の上限があります。会話が増えると、関連コンテキストを維持しつつその限界を越えない戦略が必要になります。このモジュールでメモリの仕組みを学び、後でいつ要約し忘却し呼び出すべきかを学びます。

## 次のステップ

**次のモジュール:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ナビゲーション:** [← 前へ: モジュール 01 - はじめに](../01-introduction/README.md) | [メインへ戻る](../README.md) | [次へ: モジュール 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性には努めておりますが、自動翻訳には誤りや不正確な表現が含まれる可能性があります。原文の言語で記載されたオリジナルの文書を正式な情報源としてご参照ください。重要な内容については、専門の翻訳者による翻訳をお勧めします。本翻訳の利用により生じたいかなる誤解や誤訳についても、当方は責任を負いかねますのでご了承ください。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->