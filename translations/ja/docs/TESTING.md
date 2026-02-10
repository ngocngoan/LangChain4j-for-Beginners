# LangChain4jアプリケーションのテスト

## 目次

- [クイックスタート](../../../docs)
- [テストの範囲](../../../docs)
- [テストの実行](../../../docs)
- [VS Codeでのテスト実行](../../../docs)
- [テストパターン](../../../docs)
- [テストの哲学](../../../docs)
- [次のステップ](../../../docs)

このガイドでは、APIキーや外部サービスを必要とせずにAIアプリケーションをテストする方法を示すテストを紹介します。

## クイックスタート

すべてのテストを単一コマンドで実行します：

**Bash:**
```bash
mvn test
```

**PowerShell:**
```powershell
mvn --% test
```

<img src="../../../translated_images/ja/test-results.ea5c98d8f3642043.webp" alt="成功したテスト結果" width="800"/>

*すべてのテストが通過し、失敗がゼロの成功したテスト実行の表示*

## テストの範囲

このコースはローカルで実行される**ユニットテスト**に焦点を当てています。各テストは特定のLangChain4jの概念を単独で示します。

<img src="../../../translated_images/ja/testing-pyramid.2dd1079a0481e53e.webp" alt="テストピラミッド" width="800"/>

*ユニットテスト（高速で独立）、統合テスト（実際のコンポーネント）、エンドツーエンドテストのバランスを示すテストピラミッド。この研修ではユニットテストを扱います。*

| モジュール | テスト数 | フォーカス | キーファイル |
|--------|-------|-------|-----------|
| **00 - クイックスタート** | 6 | プロンプトテンプレートと変数置換 | `SimpleQuickStartTest.java` |
| **01 - イントロダクション** | 8 | 会話メモリとステートフルチャット | `SimpleConversationTest.java` |
| **02 - プロンプトエンジニアリング** | 12 | GPT-5.2パターン、熱意レベル、構造化出力 | `SimpleGpt5PromptTest.java` |
| **03 - RAG** | 10 | ドキュメント取り込み、埋め込み、類似度検索 | `DocumentServiceTest.java` |
| **04 - ツール** | 12 | 関数呼び出しとツール連携 | `SimpleToolsTest.java` |
| **05 - MCP** | 8 | Model Context Protocol と Stdio トランスポート | `SimpleMcpTest.java` |

## テストの実行

**ルートからすべてのテストを実行：**

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
mvn test -Dtest=SimpleConversationTest#会話履歴を保持する必要があります
```

**PowerShell:**
```powershell
mvn --% test -Dtest=SimpleConversationTest#会話履歴を維持するべきか
```

## VS Codeでのテスト実行

Visual Studio Codeを使用している場合、Test Explorerはテストの実行やデバッグのためのグラフィカルなインターフェイスを提供します。

<img src="../../../translated_images/ja/vscode-testing.f02dd5917289dced.webp" alt="VS Code Test Explorer" width="800"/>

*VS Code Test ExplorerがすべてのJavaテストクラスと個別のテストメソッドをテストツリーで表示*

**VS Codeでテストを実行するには：**

1. アクティビティバーのビーカーアイコンをクリックしてTest Explorerを開く
2. テストツリーを展開してすべてのモジュールとテストクラスを表示
3. 任意のテストの横にある再生ボタンをクリックして個別に実行
4. 「Run All Tests」をクリックして全スイートを実行
5. 任意のテストを右クリックし、「Debug Test」を選択してブレークポイントを設定しステップ実行

Test Explorerは成功したテストに緑のチェックマークを表示し、テスト失敗時には詳細な失敗メッセージを提供します。

## テストパターン

### パターン1: プロンプトテンプレートのテスト

最も単純なパターンはAIモデルを呼び出さずにプロンプトテンプレートをテストします。変数置換が正しく機能し、プロンプトが期待通りにフォーマットされていることを検証します。

<img src="../../../translated_images/ja/prompt-template-testing.b902758ddccc8dee.webp" alt="プロンプトテンプレートテスト" width="800"/>

*プロンプトテンプレートのテスト：プレースホルダーを含むテンプレート → 値が適用される → フォーマット済み出力を検証*

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

このテストは`00-quick-start/src/test/java/com/example/langchain4j/quickstart/SimpleQuickStartTest.java`にあります。

**実行方法：**

**Bash:**
```bash
cd 00-quick-start && mvn test -Dtest=SimpleQuickStartTest#テストプロンプトテンプレートのフォーマット
```

**PowerShell:**
```powershell
cd 00-quick-start; mvn --% test -Dtest=SimpleQuickStartTest#テストプロンプトテンプレートのフォーマット
```

### パターン2: 言語モデルのモック

会話ロジックをテストする際は、Mockitoであらかじめ決定された応答を返す偽モデルを作成します。これによりテストは高速で無料、決定論的になります。

<img src="../../../translated_images/ja/mock-vs-real.3b8b1f85bfe6845e.webp" alt="モック vs 実際のAPI比較" width="800"/>

*なぜテストにモックが好まれるかを示す比較：高速、無料、決定論的でAPIキー不要*

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
        assertThat(history).hasSize(6); // 3つのユーザーと3つのAIメッセージ
    }
}
```

このパターンは`01-introduction/src/test/java/com/example/langchain4j/service/SimpleConversationTest.java`に登場します。モックは一貫した動作を保証し、メモリ管理が正しく動作することを検証可能にします。

### パターン3: 会話の分離テスト

会話メモリは複数ユーザーを別々に保持する必要があります。このテストは会話がコンテキストを混同しないことを検証します。

<img src="../../../translated_images/ja/conversation-isolation.e00336cf8f7a3e3f.webp" alt="会話の分離" width="800"/>

*異なるユーザーのために別々のメモリストアを保持する会話分離のテスト*

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

各会話は独立した履歴を保持します。本番システムではこの分離がマルチユーザーアプリケーションにおいて非常に重要です。

### パターン4: ツールの独立テスト

ツールはAIが呼び出す関数です。AIの判断に関わらず、ツール自体が正しく動作するかを直接テストします。

<img src="../../../translated_images/ja/tools-testing.3e1706817b0b3924.webp" alt="ツールテスト" width="800"/>

*ビジネスロジック検証のため、AI呼び出しなしでモックツールを実行するツールの独立テスト*

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

これらのテストは`04-tools/src/test/java/com/example/langchain4j/agents/tools/SimpleToolsTest.java`からのもので、AIの関与なしにツールロジックを検証します。連鎖例では、一つのツールの出力が別のツールの入力になる様子を示します。

### パターン5: インメモリRAGテスト

RAGシステムは一般にベクターデータベースや埋め込みサービスが必要です。インメモリパターンは外部依存なしでパイプライン全体をテスト可能にします。

<img src="../../../translated_images/ja/rag-testing.ee7541b1e23934b1.webp" alt="インメモリRAGテスト" width="800"/>

*データベース不要でドキュメント解析、埋め込み保存、類似度検索を行うインメモリRAGテストワークフロー*

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

このテストは`03-rag/src/test/java/com/example/langchain4j/rag/service/DocumentServiceTest.java`にあり、ドキュメントをメモリ上に作成しチャンク化およびメタデータ処理を検証します。

### パターン6: MCP統合テスト

MCPモジュールはstdioトランスポートを使ったModel Context Protocolの統合をテストします。これらはアプリがMCPサーバーをサブプロセスとして起動し通信できることを検証します。

`05-mcp/src/test/java/com/example/langchain4j/mcp/SimpleMcpTest.java`のテストがMCPクライアントの動作を検証します。

**実行方法：**

**Bash:**
```bash
cd 05-mcp && mvn test
```

**PowerShell:**
```powershell
cd 05-mcp; mvn --% test
```

## テストの哲学

AIではなくコードをテストしてください。テストは、プロンプトがどのように構築されるか、メモリがどのように管理されるか、ツールがどのように実行されるかを確認することで、あなたが書いたコードを検証するものです。AIの応答は変動するため、テストのアサーションに含めるべきではありません。プロンプトテンプレートが正しく変数を置換しているかどうかを尋ねるべきであり、AIが正しい答えを出すかどうかを問うべきではありません。

言語モデルにはモックを使いましょう。言語モデルは速度が遅く、高価で、非決定論的な外部依存です。モックを使うことでテストはミリ秒単位で高速になり、APIコスト不要で決定論的になり、常に同じ結果が得られます。

テストは独立させましょう。各テストは独自のデータをセットアップし、他テストに依存せず、実行後にクリーンアップするべきです。実行順序に関係なくテストは成功するべきです。

正常系以外の境界ケースもテストしましょう。空入力、非常に大きな入力、特殊文字、無効なパラメータ、境界条件などです。これらは通常使用では見つからないバグを明らかにします。

説明的な名前を使いましょう。`shouldMaintainConversationHistoryAcrossMultipleMessages()`と`test1()`を比べてください。前者は何をテストしているのか正確に伝え、失敗時のデバッグをずっと簡単にします。

## 次のステップ

テストパターンを理解したので、各モジュールをさらに深く学びましょう：

- **[00 - クイックスタート](../00-quick-start/README.md)** - プロンプトテンプレートの基本から始める
- **[01 - イントロダクション](../01-introduction/README.md)** - 会話メモリ管理を学ぶ
- **[02 - プロンプトエンジニアリング](../02-prompt-engineering/README.md)** - GPT-5.2のプロンプトパターンをマスター
- **[03 - RAG](../03-rag/README.md)** - 検索拡張生成システムを構築
- **[04 - ツール](../04-tools/README.md)** - 関数呼び出しとツールチェインを実装
- **[05 - MCP](../05-mcp/README.md)** - Model Context Protocolを統合

各モジュールのREADMEには、ここでテストされている概念の詳細な説明があります。

---

**ナビゲーション：** [← メインに戻る](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されました。正確性には努めておりますが、自動翻訳には誤りや不正確な部分が含まれる可能性があります。原文の言語で記載された文書が公式な情報源とみなされます。重要な情報については、専門の翻訳者による人手翻訳を推奨します。本翻訳の利用により生じたいかなる誤解や解釈違いについても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->