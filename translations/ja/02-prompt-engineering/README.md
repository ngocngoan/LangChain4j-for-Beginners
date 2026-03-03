# モジュール 02: GPT-5.2 を使ったプロンプトエンジニアリング

## 目次

- [ビデオウォークスルー](../../../02-prompt-engineering)
- [学習内容](../../../02-prompt-engineering)
- [前提条件](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの理解](../../../02-prompt-engineering)
- [プロンプトエンジニアリングの基本](../../../02-prompt-engineering)
  - [ゼロショットプロンプティング](../../../02-prompt-engineering)
  - [フューショットプロンプティング](../../../02-prompt-engineering)
  - [チェーン・オブ・ソート](../../../02-prompt-engineering)
  - [ロールベースプロンプティング](../../../02-prompt-engineering)
  - [プロンプトテンプレート](../../../02-prompt-engineering)
- [高度なパターン](../../../02-prompt-engineering)
- [アプリケーションの実行](../../../02-prompt-engineering)
- [アプリケーションのスクリーンショット](../../../02-prompt-engineering)
- [パターンの探求](../../../02-prompt-engineering)
  - [低い意欲 vs 高い意欲](../../../02-prompt-engineering)
  - [タスク実行（ツールのプレアンブル）](../../../02-prompt-engineering)
  - [自己反省コード](../../../02-prompt-engineering)
  - [構造化分析](../../../02-prompt-engineering)
  - [マルチターンチャット](../../../02-prompt-engineering)
  - [ステップバイステップ推論](../../../02-prompt-engineering)
  - [制約付き出力](../../../02-prompt-engineering)
- [本当に学んでいること](../../../02-prompt-engineering)
- [次のステップ](../../../02-prompt-engineering)

## ビデオウォークスルー

このモジュールの始め方を説明したライブセッションをご覧ください：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="LangChain4jによるプロンプトエンジニアリング - ライブセッション" width="800"/></a>

## 学習内容

以下の図は、このモジュールで開発する主要なトピックとスキルの概要を示しています — プロンプトの洗練手法から、あなたが辿るステップバイステップのワークフローまで。

<img src="../../../translated_images/ja/what-youll-learn.c68269ac048503b2.webp" alt="学習内容" width="800"/>

前のモジュールでは、GitHubモデルとの基本的なLangChain4jのやり取りを探り、Azure OpenAIによる会話型AIがメモリでどのように動作するかを見ました。今回はAzure OpenAIのGPT-5.2を使い、質問の仕方—つまりプロンプトそのものに焦点を当てます。プロンプトの構造は、得られる応答の質に劇的な影響を与えます。基本的なプロンプト技術の復習から始め、次にGPT-5.2の能力を最大限に活かす８つの高度なパターンに進みます。

GPT-5.2は推論制御を導入しているので、モデルに回答前にどれだけ考えるかを指示できます。これにより異なるプロンプト戦略がより明確になり、どのアプローチをいつ使うべきか理解が深まります。また、GPT-5.2はGitHubモデルよりAzureのレート制限が緩いため、その恩恵も受けられます。

## 前提条件

- モジュール 01 を完了していること（Azure OpenAIリソースがデプロイされている）
- ルートディレクトリに Azure 資格情報が入った `.env` ファイルがあること（モジュール 01 で `azd up` により作成されたもの）

> **注意:** モジュール 01 を完了していない場合は、まずそこでのデプロイ手順に従ってください。

## プロンプトエンジニアリングの理解

プロンプトエンジニアリングの本質は、あいまいな指示と正確な指示の違いにあります。以下の比較がそれを示しています。

<img src="../../../translated_images/ja/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="プロンプトエンジニアリングとは？" width="800"/>

プロンプトエンジニアリングは、必要な結果を一貫して引き出す入力テキストを設計することです。単に質問を投げるだけでなく、モデルに欲しい内容とその提供方法を正確に理解させるためのリクエスト構造を作ることです。

言い換えれば同僚に指示を出すようなものです。「バグを修正して」はあいまいです。「UserService.javaの45行目でヌルポインター例外が起きているのでヌルチェックを追加して修正して」は具体的です。言語モデルも同様に、具体性と構造が重要です。

下図はLangChain4jがどのようにこの概念にフィットするかを示しています — あなたのプロンプトパターンを `SystemMessage` と `UserMessage` という構築ブロックを通じてモデルにつなげています。

<img src="../../../translated_images/ja/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4jのフィット感" width="800"/>

LangChain4jはインフラを提供します — モデルの接続、メモリ、メッセージタイプ — 一方でプロンプトパターンはそのインフラを通す、よく構造化されたテキストです。主要な構成要素は `SystemMessage`（AIの挙動・役割を設定）と `UserMessage`（実際のリクエストを運ぶもの）です。

## プロンプトエンジニアリングの基本

以下に示す５つのコア技術が、有効なプロンプトエンジニアリングの基礎をなします。各技術は言語モデルとのコミュニケーションの異なる側面に応えます。

<img src="../../../translated_images/ja/five-patterns-overview.160f35045ffd2a94.webp" alt="５つのプロンプトエンジニアリングパターンの概要" width="800"/>

このモジュールの高度なパターンに入る前に、５つの基礎的なプロンプト技術を復習しましょう。これらは全てのプロンプトエンジニアが知っておくべきビルディングブロックです。もし既に[クイックスタートモジュール](../00-quick-start/README.md#2-prompt-patterns)を経験していれば、これらは実践で見たことがあるでしょう — ここではその概念的な枠組みを示します。

### ゼロショットプロンプティング

もっとも単純な方法：例を一切示さずにモデルに直接指示を与えます。モデルはトレーニングだけを頼りにタスクを理解し実行します。予想される挙動が明白な単純な依頼に適しています。

<img src="../../../translated_images/ja/zero-shot-prompting.7abc24228be84e6c.webp" alt="ゼロショットプロンプティング" width="800"/>

*例なしの直接指示 — モデルは指示からタスクを推測する*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 返信:「ポジティブ」
```

**利用時:** 単純な分類、直接的な質問、翻訳、あるいは追加の指導が不要なタスク。

### フューショットプロンプティング

モデルに従って欲しいパターンの例を示します。モデルは例から期待される入出力フォーマットを学び、新しい入力にも適用します。これにより形式や挙動が明確でないタスクの一貫性が大幅に向上します。

<img src="../../../translated_images/ja/few-shot-prompting.9d9eace1da88989a.webp" alt="フューショットプロンプティング" width="800"/>

*例から学ぶ — モデルはパターンを特定し新しい入力に適用*

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

**利用時:** カスタム分類、一貫したフォーマット、ドメイン固有のタスク、またはゼロショットの結果が不安定な場合。

### チェーン・オブ・ソート

モデルに推論を段階的に示すように頼みます。答えに飛びつくのではなく、問題を分解し各部分を明示的に考えるので、数学、論理、多段階の推論タスクの精度が向上します。

<img src="../../../translated_images/ja/chain-of-thought.5cff6630e2657e2a.webp" alt="チェーン・オブ・ソートプロンプティング" width="800"/>

*ステップごとの推論 — 複雑な問題を明確な論理ステップへ分解*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// モデルは示しています：15 - 8 = 7、次に7 + 12 = 19個のリンゴ
```

**利用時:** 数学の問題、論理パズル、デバッグ、または推論過程の提示が精度と信頼性を高めるタスク。

### ロールベースプロンプティング

AIに質問する前にペルソナや役割を設定します。これにより回答のトーン、深さ、重点が形作られます。「ソフトウェアアーキテクト」と「ジュニア開発者」や「セキュリティ監査官」では異なる助言になります。

<img src="../../../translated_images/ja/role-based-prompting.a806e1a73de6e3a4.webp" alt="ロールベースプロンプティング" width="800"/>

*コンテキストとペルソナの設定 — 同じ質問でも役割に応じて別の回答になる*

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

**利用時:** コードレビュー、チュータリング、ドメイン固有の分析、または特定の専門性や視点に合わせた応答が必要な場合。

### プロンプトテンプレート

変数プレースホルダーを使った再利用可能なプロンプトを作成します。毎回新しいプロンプトを書くのではなく、一度テンプレートを定義し異なる値を埋めます。LangChain4jの `PromptTemplate` クラスは `{{variable}}` 構文でこれを簡単にします。

<img src="../../../translated_images/ja/prompt-templates.14bfc37d45f1a933.webp" alt="プロンプトテンプレート" width="800"/>

*変数プレースホルダーを使った再利用可能なプロンプト — 一つのテンプレートで多用途*

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

**利用時:** 入力が変わる繰り返しの問い合わせ、バッチ処理、再利用可能なAIワークフロー構築、構造は同じでデータだけ変わるシナリオ。

---

これら５つの基本でほとんどのプロンプトタスクに対応可能な堅固なツールキットが得られます。このモジュールの残りは、GPT-5.2の推論制御、自己評価、構造化出力能力を活用する**８つの高度なパターン**に基づいて構築されます。

## 高度なパターン

基本が理解できたところで、このモジュール独自の８つの高度なパターンに進みましょう。すべての問題が同じアプローチを必要としません。いくつかの質問は即座の答えを必要とし、他はじっくり考えることが必要です。推論を明示的に示すものもあれば結果だけを求めるものもあります。以下の各パターンは異なるシナリオに最適化されており、GPT-5.2の推論制御により違いがよりはっきりします。

<img src="../../../translated_images/ja/eight-patterns.fa1ebfdf16f71e9a.webp" alt="８つのプロンプトパターン" width="800"/>

*８つのプロンプトエンジニアリングパターンとその使用例の概要*

GPT-5.2はこれらパターンにもう一つの次元を加えます：*推論制御*。以下のスライダーはモデルの思考量を調整できる様子を示します — 速く直接的な答えから、深く徹底的な分析まで。

<img src="../../../translated_images/ja/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2による推論制御" width="800"/>

*GPT-5.2の推論制御により、モデルがどれだけ考えるかを指定可能 — 高速な直答から深い探求まで*

**低い意欲（速く・集中）** - 単純な質問に速く直接的な答えが欲しい場合。モデルは最少の推論（最大２ステップ）で済ませます。計算やルックアップ、単純質問に最適。

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

> 💡 **GitHub Copilotで探る:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)を開き、以下を聞いてみましょう：
> - 「低い意欲と高い意欲のプロンプトパターンの違いは何ですか？」
> - 「プロンプト内のXMLタグはAIの応答構造にどう役立っていますか？」
> - 「自己反省パターンと直接指示はいつ使い分けるべき？」

**高い意欲（深く・徹底）** - 複雑な問題に包括的な分析が必要な場合。モデルは詳細な推論を示し徹底的に探ります。システム設計やアーキテクチャ決定、複雑な調査に適用。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**タスク実行（ステップごとの進行）** - 複数段階のワークフローの場合。モデルは先に計画を示し、処理中に各ステップをナレーションし、最後に要約を出します。移行作業、実装、マルチステップ処理に活用。

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

チェーン・オブ・ソートプロンプティングはモデルに推論過程を明示的に示すよう頼み、複雑タスクの正確性を高めます。ステップバイステップの分解は人とAI双方の理解を促進します。

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** このパターンについて質問してください：
> - 「長時間実行のオペレーションにタスク実行パターンをどう適応しますか？」
> - 「本番アプリケーションでのツールプレアンブルの構成ベストプラクティスは？」
> - 「UIで中間進捗をどうキャプチャし表示すれば良いですか？」

以下の図はこの計画 → 実行 → 要約のワークフローを示します。

<img src="../../../translated_images/ja/task-execution-pattern.9da3967750ab5c1e.webp" alt="タスク実行パターン" width="800"/>

*マルチステップタスクのための計画 → 実行 → 要約のワークフロー*

**自己反省コード** - 本番品質コードを生成するため。モデルは本番基準を守り、適切なエラー処理付きのコードを生成します。新機能やサービス構築の際に利用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

下図はこの反復改善ループを示しています — 生成、評価、弱点の特定、改良を繰り返し本番基準まで高める。

<img src="../../../translated_images/ja/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自己反省サイクル" width="800"/>

*反復的改善ループ - 生成 → 評価 → 問題識別 → 改良 → 繰り返し*

**構造化分析** - 一貫した評価のため。モデルは固定の枠組み（正確さ、プラクティス、パフォーマンス、セキュリティ、保守性）でコードをレビューします。コードレビューや品質評価に最適。

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot)チャットで試す:** 構造化分析について質問してください：
> - 「コードレビューの種類に合わせて分析フレームワークをカスタマイズするには？」
> - 「構造化出力をプログラムで解析し活用するベスト方法は？」
> - 「レビュー間で一貫した重大度レベルを確保するには？」

以下の図は、一貫性あるコードレビューを重大度レベル別のカテゴリーに分類するフレームワークを示しています。

<img src="../../../translated_images/ja/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="構造化分析パターン" width="800"/>

*一貫したコードレビューのための重大度レベルによるフレームワーク*

**マルチターンチャット** - 文脈を要する対話に。モデルは過去のメッセージを記憶し積み重ねて応答します。対話型ヘルプや複雑なQ&Aに適用。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

以下の図は会話文脈がターンを重ねるごとにどう積み上がり、モデルのトークン制限にどう関係するかを示します。

<img src="../../../translated_images/ja/context-memory.dff30ad9fa78832a.webp" alt="コンテキストメモリ" width="800"/>

*複数ターンで会話コンテキストがどのように積み重なりトークン制限に到達するか*
**ステップバイステップの推論** - 論理が明示的に必要な問題に対して。モデルは各ステップの明示的な推論を示します。数学の問題、論理パズル、または思考過程を理解したい場合に利用してください。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

以下の図は、モデルが問題を明示的で番号付きの論理的ステップに分解する方法を示しています。

<img src="../../../translated_images/ja/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*問題を明示的な論理的ステップに分解する*

**制約付き出力** - 特定のフォーマット要件がある回答の場合。モデルはフォーマットと長さのルールを厳格に守ります。要約や正確な出力構造が必要な場合に利用してください。

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

以下の図は、制約がモデルをどのように案内して厳密にフォーマットと長さの要件に従った出力を生成するかを示しています。

<img src="../../../translated_images/ja/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*特定のフォーマット、長さ、構造の要件を強制*

## アプリケーションの起動

**展開の確認：**

ルートディレクトリに`.env`ファイルがAzureの認証情報と共に存在することを確認してください（Module 01で作成済み）。モジュールのディレクトリ（`02-prompt-engineering/`）から以下を実行します。

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENTを表示する必要があります
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT を表示する必要があります
```

**アプリケーションの開始：**

> **注意：** すでにルートディレクトリから`./start-all.sh`を用いて全アプリケーションを起動している場合（Module 01の説明参照）、このモジュールはすでにポート8083で起動しています。以下の起動コマンドはスキップして http://localhost:8083 に直接アクセスできます。

**オプション 1：Spring Boot Dashboardの使用（VS Codeユーザーに推奨）**

devコンテナにはSpring Boot Dashboard拡張機能が含まれており、すべてのSpring Bootアプリケーションを視覚的に管理できます。VS Codeの左側のアクティビティバーにあるSpring Bootアイコンを探してください。

Spring Boot Dashboardからは以下が可能です：
- ワークスペース内のすべてのSpring Bootアプリケーションを見る
- クリックひとつでアプリケーションの起動・停止
- リアルタイムでログを確認
- アプリケーションの状態を監視

「prompt-engineering」の横の再生ボタンをクリックするとこのモジュールが起動します。全モジュールを一括で起動することも可能です。

<img src="../../../translated_images/ja/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS CodeのSpring Boot Dashboard — 1箇所で全モジュールを開始、停止、監視可能*

**オプション 2：シェルスクリプトの利用**

すべてのウェブアプリケーション（モジュール 01-04）を起動：

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

または、このモジュールだけを起動：

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

これらのスクリプトはどちらもルートの`.env`ファイルから環境変数を自動で読み込み、JARファイルが存在しない場合はビルドを行います。

> **注意：** 事前に全モジュールを手動でビルドしたい場合：
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

こちらはPrompt Engineeringモジュールのメインインターフェイスです。8つのパターンすべてを並べて試せます。

<img src="../../../translated_images/ja/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*8つのPrompt Engineeringパターンの特徴と使用例を示すメインダッシュボード*

## パターンの探索

Webインターフェイスでは異なるプロンプト戦略を試せます。各パターンは異なる問題を解決します。試してみて、どのアプローチがどの場面で有効かを体感してください。

> **注意：ストリーミングと非ストリーミング** — 各パターンページには「🔴 Stream Response (Live)」と「非ストリーミング」ボタンの2つがあります。ストリーミングはServer-Sent Events（SSE）を使い、モデルが生成するトークンをリアルタイムで表示します。進捗が即座に見えるのが特徴です。非ストリーミングはレスポンス全体が完成するまで待って表示します。深い推論を必要とするプロンプト（例：High Eagerness、Self-Reflecting Code）では非ストリーミングの呼び出しが非常に長時間かかり、数分に及ぶこともあります。複雑なプロンプトを試す際はストリーミングを使うのがおすすめです。モデルが動作している様子が見え、リクエストがタイムアウトした印象を避けられます。
>
> **注意：ブラウザ要件** — ストリーミング機能はFetch Streams API（`response.body.getReader()`）を使用しており、完全なブラウザ（Chrome、Edge、Firefox、Safari）が必要です。VS Code内蔵のSimple Browserでは動作しません。これはSimple BrowserのwebviewがReadableStream APIに対応していないためです。Simple Browser使用時でも非ストリーミングボタンは正常に動作します。ストリーミングボタンのみ影響を受けます。ストリーミングを完全に体験したい場合は外部ブラウザで `http://localhost:8083` を開いてください。

### Low vs High Eagerness

「200の15%はいくつ？」のような簡単な質問をLow Eagernessで訊くと、即座に直接的な答えが得られます。次に「高トラフィックAPIのキャッシュ戦略を設計してください」といった複雑な問題をHigh Eagernessで試し、**🔴 Stream Response (Live)** をクリックしてモデルの詳細な推論をトークン単位で見てみてください。同じモデル、同じ質問構造でも、プロンプトがどれだけ考えるかを指示しています。

### タスク実行（ツールプレアンブル）

複数ステップのワークフローでは事前計画と進捗報告が役立ちます。モデルは何をするかをアウトライン化し、各ステップを説明し、最後に結果をまとめます。

### 自己反省型コード

「メール検証サービスを作成してください」を試してください。単にコードを生成して終了するのではなく、モデルは生成したコードを評価基準に照らして評価し、弱点を特定し、改善します。コードが本番品質に達するまで繰り返す様子が見られます。

### 構造化分析

コードレビューには一貫した評価フレームワークが必要です。モデルは（正確さ、慣習、パフォーマンス、セキュリティ）という固定カテゴリごとに、重大度レベルも含めてコードを評価します。

### マルチターンチャット

「Spring Bootとは何ですか？」と尋ねた後すぐに「例を見せてください」と続けてみてください。モデルは最初の質問を記憶し、具体的なSpring Bootの例を返します。メモリが無いと、2つ目の質問はあいまいすぎて適切に答えられません。

### ステップバイステップの推論

数学の問題を選び、Step-by-Step ReasoningとLow Eagernessの両方で試してください。Low eagernessは答えのみを高速に返しますが、過程が不透明です。Step-by-Stepではすべての計算と判断プロセスを示します。

### 制約付き出力

特定のフォーマットや語数が必要な場合、このパターンは厳密な遵守を強制します。ちょうど100語の箇条書き要約を生成してみてください。

## 真に学んでいること

**推論努力がすべてを変える**

GPT-5.2はプロンプトを通じて計算努力の度合いを制御できます。努力が少ないほど簡潔に素早い応答が得られ、探索は最小限です。努力が多いほどモデルは深い思考に時間をかけます。タスクの複雑さに合わせて努力を調整する方法を学びましょう。簡単な質問に無駄な時間をかけず、複雑な判断を急がないように。

**構造が動作を導く**

プロンプト内のXMLタグに注目してください。単なる装飾ではありません。モデルは自由形式のテキストよりも構造化された指示に従いやすいです。複数ステップの処理や複雑な論理が必要な場合、構造はモデルが現在地と次の処理を追跡する助けとなります。以下の図はよく構造化されたプロンプトの分解で、`<system>`、`<instructions>`、`<context>`、`<user-input>`、`<constraints>`といったタグがどのように指示を明確なセクションに整理するかを示しています。

<img src="../../../translated_images/ja/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*明確なセクションとXML形式で構成されたよく構造化されたプロンプトの解剖*

**自己評価による品質向上**

自己反省型パターンは品質基準を明示的にします。モデルが「正しくやる」ことを期待するのではなく、「正しい」とは何かを具体的に伝えます：論理の正確さ、エラーハンドリング、パフォーマンス、セキュリティ。モデルは自らの出力を評価し改善可能です。これによりコード生成が運任せではなくプロセスに変わります。

**コンテキストには限りがある**

マルチターン会話は各リクエストにメッセージ履歴を含めることで成立しますが、モデルには最大トークン数の限界があります。会話が長くなるほど、関連するコンテキストを維持しつつ上限に達しない戦略が必要です。このモジュールではメモリの仕組みを学び、その後、いつ要約し、いつ忘れ、いつ再取得するかを学びます。

## 次のステップ

**次のモジュール:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ナビゲーション:** [← 前へ: Module 01 - Introduction](../01-introduction/README.md) | [メインへ戻る](../README.md) | [次へ: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**:  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる場合があります。原文の元の言語による文書が権威ある情報源とみなされるべきです。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や誤訳についても、弊社は一切責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->