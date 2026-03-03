# LangChain4jアプリケーションのテスト

## 目次

- [クイックスタート](../../../docs)
- [テストの対象範囲](../../../docs)
- [テストの実行](../../../docs)
- [VS Codeでのテスト実行](../../../docs)
- [テストパターン](../../../docs)
- [テスト哲学](../../../docs)
- [次のステップ](../../../docs)

このガイドでは、APIキーや外部サービスを必要とせずにAIアプリケーションをテストする方法を示すテストを通じて説明します。

## クイックスタート

以下のコマンドで全テストを実行します：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

すべてのテストが成功すると、以下のスクリーンショットのようにゼロの失敗で実行されます。

<img src="../../../translated_images/ja/test-results.ea5c98d8f3642043.webp" alt="Successful Test Results" width="800"/>

*全テストが失敗なく成功した実行結果の例*

## テストの対象範囲

このコースはローカルで実行される**ユニットテスト**に焦点を当てています。それぞれのテストはLangChain4jの特定の概念を単体で示します。以下のテストピラミッドはユニットテストの位置付けを示しており、テスト戦略の基盤となる高速かつ信頼できるものです。

<img src="../../../translated_images/ja/testing-pyramid.2dd1079a0481e53e.webp" alt="Testing Pyramid" width="800"/>

*ユニットテスト（高速で単体）、統合テスト（実際のコンポーネント）、エンドツーエンドテストのバランスを示すテストピラミッド。このトレーニングはユニットテストを扱っています。*

| モジュール | テスト数 | フォーカス | 主要ファイル |
|--------|-------|-------|-----------|
| **00 - クイックスタート** | 6 | プロンプトテンプレートと変数置換 | `SimpleQuickStartTest.java` |
| **01 - はじめに** | 8 | 会話メモリと状態を持つチャット | `SimpleConversationTest.java` |
| **02 - プロンプトエンジニアリング** | 12 | GPT-5.2パターン、熱意レベル、構造化出力 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ドキュメント取り込み、埋め込み、類似検索 | `DocumentServiceTest.java` |
| **04 - ツール** | 12 | 関数呼び出しとツールの連結 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Stdioトランスポートによるモデルコンテキストプロトコル | `SimpleMcpTest.java` |

## テストの実行

**ルートから全テストを実行：**

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

**特定モジュールのテストを実行：**

**Bash:**
```bash
cd 01-introduction && mvn test
# またはルートから
mvn test -pl 01-introduction
```

**PowerShell:**
```powershell
cd 01-introduction; mvn --% test
# またはルートから
mvn --% test -pl 01-introduction
```

**単一テストクラスの実行：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest
```

**特定のテストメソッドを実行：**

**Bash:**
```bash
mvn test -Dtest=SimpleConversationTest#会話履歴を維持するべきか
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#会話履歴を維持すべきか
```

## VS Codeでのテスト実行

Visual Studio Codeを使用している場合、Test Explorerはテストの実行やデバッグをグラフィカルに行うインターフェースを提供します。

<img src="../../../translated_images/ja/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*すべてのJavaテストクラスおよび個別のテストメソッドを表示したVS Code Test Explorerのテストツリー*

**VS Codeでテストを実行するには：**

1. アクティビティバーのフラスコアイコンをクリックしてTest Explorerを開く
2. テストツリーを展開してすべてのモジュールとテストクラスを確認
3. 任意のテスト横の再生ボタンをクリックして単独で実行
4. 「Run All Tests」をクリックして全テストを実行
5. 任意のテストを右クリックして「Debug Test」を選択し、ブレークポイントを設定してコードをステップ実行

Test Explorerは成功したテストに緑のチェックマークを表示し、失敗時には詳細なエラーメッセージを提供します。

## テストパターン

### パターン1：プロンプトテンプレートのテスト

最も単純なパターンは、AIモデルを呼び出さずにプロンプトテンプレートをテストします。変数置換が正しく機能し、期待した形式でプロンプトが生成されることを検証します。

<img src="../../../translated_images/ja/prompt-template-testing.b902758ddccc8dee.webp" alt="Prompt Template Testing" width="800"/>

*変数置換の流れを示すプロンプトテンプレートのテスト：プレースホルダー付きテンプレート → 値の適用 → フォーマット済み出力の検証*

```java
@Test
@DisplayName("Should format prompt template with variables")
void testPromptTemplateFormatting() {
    PromptTemplate template = PromptTemplate.from(
        "Best time to visit {{destination}} for {{activity}}?"
    );
    
    Prompt prompt = template.apply(Map.of(
        "destination", "Paris",
        "activity", "sightseeing"
    ));
    
    assertThat(prompt.text()).isEqualTo("Best time to visit Paris for sightseeing?");
}
```

このテストは `00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java` にあります。

**実行方法：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#テストプロンプトテンプレートのフォーマット
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#testPromptTemplateFormatting
```

### パターン2：言語モデルのモック

会話ロジックをテストする際は、Mockitoを使ってあらかじめ決められた応答を返す偽モデルを作成します。これによりテストは高速かつ無料で決定論的に実行できます。

<img src="../../../translated_images/ja/mock-vs-real.3b8b1f85bfe6845e.webp" alt="Mock vs Real API Comparison" width="800"/>

*テストにモックが推奨される理由を示す比較：高速、無料、決定論的、APIキー不要*

```java
@ExtendWith(MockitoExtension.class)
class SimpleConversationTest {
    
    private ConversationService conversationService;
    
    @Mock
    private OpenAiOfficialChatModel mockChatModel;
    
    @BeforeEach
    void setUp() {
        ChatResponse mockResponse = ChatResponse.builder()
            .aiMessage(AiMessage.from("This is a test response"))
            .build();
        when(mockChatModel.chat(anyList())).thenReturn(mockResponse);
        
        conversationService = new ConversationService(mockChatModel);
    }
    
    @Test
    void shouldMaintainConversationHistory() {
        String conversationId = conversationService.startConversation();
        
        ChatResponse mockResponse1 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 1"))
            .build();
        ChatResponse mockResponse2 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 2"))
            .build();
        ChatResponse mockResponse3 = ChatResponse.builder()
            .aiMessage(AiMessage.from("Response 3"))
            .build();
        
        when(mockChatModel.chat(anyList()))
            .thenReturn(mockResponse1)
            .thenReturn(mockResponse2)
            .thenReturn(mockResponse3);

        conversationService.chat(conversationId, "First message");
        conversationService.chat(conversationId, "Second message");
        conversationService.chat(conversationId, "Third message");

        List<ChatMessage> history = conversationService.getHistory(conversationId);
        assertThat(history).hasSize(6); // 3ユーザー + 3 AIメッセージ
    }
}
```

このパターンは `01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java` にあります。モックにより一貫した動作が保証され、メモリ管理が正しく動くか検証可能です。

### パターン3：会話の分離テスト

会話メモリは複数ユーザーを分けて管理しなければなりません。このテストは会話の文脈が混ざらないことを確認します。

<img src="../../../translated_images/ja/conversation-isolation.e00336cf8f7a3e3f.webp" alt="Conversation Isolation" width="800"/>

*異なるユーザーのためにメモリストアを分離し文脈の混合を防ぐ会話分離テスト*

```java
@Test
void shouldIsolateConversationsByid() {
    String conv1 = conversationService.startConversation();
    String conv2 = conversationService.startConversation();
    
    ChatResponse mockResponse = ChatResponse.builder()
        .aiMessage(AiMessage.from("Response"))
        .build();
    when(mockChatModel.chat(anyList())).thenReturn(mockResponse);

    conversationService.chat(conv1, "Message for conversation 1");
    conversationService.chat(conv2, "Message for conversation 2");

    List<ChatMessage> history1 = conversationService.getHistory(conv1);
    List<ChatMessage> history2 = conversationService.getHistory(conv2);
    
    assertThat(history1).hasSize(2);
    assertThat(history2).hasSize(2);
}
```

それぞれの会話は独立した履歴を保持します。実際のシステムでは、この分離がマルチユーザー環境で非常に重要です。

### パターン4：ツールの単独テスト

ツールはAIが呼び出せる関数です。AIの判断に関わらず正しく動作するか、直接テストします。

<img src="../../../translated_images/ja/tools-testing.3e1706817b0b3924.webp" alt="Tools Testing" width="800"/>

*AI呼び出しなしでモックツールを実行し、ビジネスロジックを検証するツールの単独テスト*

```java
@Test
void shouldConvertCelsiusToFahrenheit() {
    TemperatureTool tempTool = new TemperatureTool();
    String result = tempTool.celsiusToFahrenheit(25.0);
    assertThat(result).containsPattern("77[.,]0°F");
}

@Test
void shouldDemonstrateToolChaining() {
    WeatherTool weatherTool = new WeatherTool();
    TemperatureTool tempTool = new TemperatureTool();

    String weatherResult = weatherTool.getCurrentWeather("Seattle");
    assertThat(weatherResult).containsPattern("\\d+°C");

    String conversionResult = tempTool.celsiusToFahrenheit(22.0);
    assertThat(conversionResult).containsPattern("71[.,]6°F");
}
```

これらのテストは `04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java` にあり、AIの関与なしでツールロジックを検証します。連結例では一つのツールの出力を別のツールの入力として渡します。

### パターン5：インメモリRAGテスト

RAGシステムは通常ベクトルデータベースや埋め込みサービスを必要としますが、インメモリパターンなら外部依存なしにパイプライン全体をテストできます。

<img src="../../../translated_images/ja/rag-testing.ee7541b1e23934b1.webp" alt="In-Memory RAG Testing" width="800"/>

*データベース不要でドキュメント解析、埋め込み保存、類似検索を行うインメモリRAGテストのワークフロー*

```java
@Test
void testProcessTextDocument() {
    String content = "This is a test document.\nIt has multiple lines.";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    
    DocumentService.ProcessedDocument result = 
        documentService.processDocument(inputStream, "test.txt");

    assertNotNull(result);
    assertTrue(result.segments().size() > 0);
    assertEquals("test.txt", result.segments().get(0).metadata().getString("filename"));
}
```

このテストは `03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java` にあり、メモリ内にドキュメントを作成してチャンク分割やメタデータ処理を検証します。

### パターン6：MCP統合テスト

MCPモジュールはstdioトランスポートを使ったモデルコンテキストプロトコルの統合をテストします。これらのテストはMCPサーバーをサブプロセスとして起動し通信できるか検証します。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java` のテストコードがMCPクライアントの挙動をチェックします。

**実行方法：**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## テスト哲学

AIをテストするのではなく、コードをテストしましょう。テストは、プロンプトの構成方法、メモリ管理、ツールの実行を検証するために書いた自分のコードを評価すべきです。AIの回答は変動するため、テストの検証に含めてはいけません。プロンプトテンプレートが変数を正しく置換できているかを問うべきで、AIが正しい回答を返すかは問いません。

言語モデルにはモックを使いましょう。言語モデルは遅く、コストがかかり、決定論的でない外部依存です。モックを使うことでテストはミリ秒単位の高速さ、APIコスト不要の無料、毎回同じ結果を返す決定論的なものになります。

テストは独立させましょう。各テストは自身でデータをセットアップし、他のテストに依存せず、後始末を行うべきです。テストの実行順に関わらず成功する必要があります。

正常系以外の境界ケースもテストしましょう。空入力、非常に大きな入力、特殊文字、無効なパラメータ、境界条件などを試してください。これらは通常の利用では見つからないバグを発見することがあります。

分かりやすい名前を使いましょう。`shouldMaintainConversationHistoryAcrossMultipleMessages()` と `test1()` を比べてください。前者は何をテストしているかすぐ分かり、失敗時のデバッグも容易です。

## 次のステップ

これらのテストパターンを理解したら、各モジュールをさらに深く学んでいきましょう：

- **[00 - クイックスタート](../00-quick-start/README.md)** - プロンプトテンプレートの基礎を開始
- **[01 - はじめに](../01-introduction/README.md)** - 会話メモリ管理を学ぶ
- **[02 - プロンプトエンジニアリング](../02-prompt-engineering/README.md)** - GPT-5.2のプロンプトパターンをマスター
- **[03 - RAG](../03-rag/README.md)** - 検索強化生成システムを構築
- **[04 - ツール](../04-tools/README.md)** - 関数呼び出しとツールチェーンを実装
- **[05 - MCP](../05-mcp/README.md)** - モデルコンテキストプロトコルを統合

各モジュールのREADMEではここで扱った概念の詳細説明が提供されています。

---

**ナビゲーション：** [← メインに戻る](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス[Co-op Translator](https://github.com/Azure/co-op-translator)を使用して翻訳されました。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文の言語によるオリジナル文書を正式な情報源としてください。重要な情報については、専門の人間による翻訳を推奨します。本翻訳の利用により生じる誤解や誤った解釈について、当方は一切責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->