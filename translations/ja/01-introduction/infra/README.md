# LangChain4jのAzureインフラストラクチャ入門

## 目次

- [前提条件](../../../../01-introduction/infra)
- [アーキテクチャ](../../../../01-introduction/infra)
- [作成されるリソース](../../../../01-introduction/infra)
- [クイックスタート](../../../../01-introduction/infra)
- [設定](../../../../01-introduction/infra)
- [管理コマンド](../../../../01-introduction/infra)
- [コスト最適化](../../../../01-introduction/infra)
- [モニタリング](../../../../01-introduction/infra)
- [トラブルシューティング](../../../../01-introduction/infra)
- [インフラストラクチャの更新](../../../../01-introduction/infra)
- [クリーンアップ](../../../../01-introduction/infra)
- [ファイル構造](../../../../01-introduction/infra)
- [セキュリティ推奨事項](../../../../01-introduction/infra)
- [追加リソース](../../../../01-introduction/infra)

このディレクトリには、BicepとAzure Developer CLI（azd）を使用してAzure OpenAIリソースをデプロイするためのAzureインフラストラクチャをコードとして管理する(IaC)が含まれています。

## 前提条件

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)（バージョン2.50.0以降）
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd)（バージョン1.5.0以降）
- リソースを作成する権限を持つAzureサブスクリプション

## アーキテクチャ

**簡易ローカル開発セットアップ** - Azure OpenAIのみをデプロイし、すべてのアプリはローカルで実行。

インフラストラクチャは以下のAzureリソースをデプロイします：

### AIサービス
- **Azure OpenAI**：以下の2つのモデルを持つCognitive Services：
  - **gpt-5.2**：チャット完了モデル（20K TPM容量）
  - **text-embedding-3-small**：RAG用埋め込みモデル（20K TPM容量）

### ローカル開発
すべてのSpring Bootアプリケーションはあなたのマシン上でローカルに実行：
- 01-introduction（ポート8080）
- 02-prompt-engineering（ポート8083）
- 03-rag（ポート8081）
- 04-tools（ポート8084）

## 作成されるリソース

| リソース種別 | リソース名の形式 | 目的 |
|--------------|-----------------|-----|
| リソースグループ | `rg-{environmentName}` | すべてのリソースを格納 |
| Azure OpenAI | `aoai-{resourceToken}` | AIモデルのホスティング |

> **注意:** `{resourceToken}`はサブスクリプションID、環境名、ロケーションから生成される一意の文字列です

## クイックスタート

### 1. Azure OpenAIのデプロイ

**Bash:**
```bash
cd 01-introduction
azd up
```

**PowerShell:**
```powershell
cd 01-introduction
azd up
```

プロンプトに従って：
- 使用するAzureサブスクリプションを選択
- ロケーションを選択（推奨：GPT-5.2が利用可能な`eastus2`）
- 環境名を確認（デフォルト：`langchain4j-dev`）

これにより以下が作成されます：
- GPT-5.2およびtext-embedding-3-smallを搭載したAzure OpenAIリソース
- 接続詳細の出力

### 2. 接続情報の取得

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

これらを表示します：
- `AZURE_OPENAI_ENDPOINT`: Azure OpenAIのエンドポイントURL
- `AZURE_OPENAI_KEY`: 認証用APIキー
- `AZURE_OPENAI_DEPLOYMENT`: チャットモデル名（gpt-5.2）
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: 埋め込みモデル名

### 3. アプリケーションのローカル実行

`azd up` コマンドがルートディレクトリに必要な環境変数を含む `.env` ファイルを自動的に作成します。

**推奨:** すべてのWebアプリケーションを起動：

**Bash:**
```bash
# ルートディレクトリから
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# ルートディレクトリから
cd ../..
.\start-all.ps1
```

または単一モジュールを起動：

**Bash:**
```bash
# 例: 導入モジュールのみを開始する
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# 例：導入モジュールだけを開始する
cd ../01-introduction
.\start.ps1
```

両スクリプトは `azd up` によって作成されたルートの `.env` ファイルから環境変数を自動でロードします。

## 設定

### モデルデプロイのカスタマイズ

モデルデプロイを変更するには、`infra/main.bicep` を編集し、`openAiDeployments` パラメーターを修正します：

```bicep
param openAiDeployments array = [
  {
    name: 'gpt-5.2'  // Model deployment name
    model: {
      format: 'OpenAI'
      name: 'gpt-5.2'
      version: '2025-12-11'  // Model version
    }
    sku: {
      name: 'GlobalStandard'
      capacity: 20  // TPM in thousands
    }
  }
  // Add more deployments...
]
```

利用可能なモデルとバージョン：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azureリージョンの変更

別のリージョンにデプロイするには、`infra/main.bicep` を編集します：

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2の利用可能リージョンの確認：https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicepファイルの変更後にインフラを更新するには：

**Bash:**
```bash
# ARM テンプレートを再構築する
az bicep build --file infra/main.bicep

# 変更をプレビューする
azd provision --preview

# 変更を適用する
azd provision
```

**PowerShell:**
```powershell
# ARM テンプレートを再構築する
az bicep build --file infra/main.bicep

# 変更をプレビューする
azd provision --preview

# 変更を適用する
azd provision
```


## クリーンアップ

すべてのリソースを削除するには：

**Bash:**
```bash
# すべてのリソースを削除する
azd down

# 環境を含むすべてを削除する
azd down --purge
```

**PowerShell:**
```powershell
# すべてのリソースを削除します
azd down

# 環境を含むすべてを削除します
azd down --purge
```


**警告**: これによりすべてのAzureリソースが永久に削除されます。

## ファイル構造

## コスト最適化

### 開発/テスト環境
開発・テスト環境では、コストを削減できます：
- Azure OpenAIをStandard層（S0）で使用
- `infra/core/ai/cognitiveservices.bicep` で容量を20Kから10K TPMに低減
- 使用しない時はリソースを削除(`azd down`)

### 本番環境
本番環境では：
- 利用状況に応じてOpenAIの容量を増加（50K TPM以上）
- 高可用性のためゾーン冗長性を有効化
- 適切な監視とコストアラートを設定

### コスト見積もり
- Azure OpenAI：トークン単位課金（入力＋出力）
- GPT-5.2：約3-5ドル／100万トークン（最新価格を確認してください）
- text-embedding-3-small：約0.02ドル／100万トークン

価格計算ツール：https://azure.microsoft.com/pricing/calculator/

## モニタリング

### Azure OpenAIのメトリクスを確認

Azureポータル → OpenAIリソース → メトリクス：
- トークン利用率
- HTTPリクエストレート
- 応答時間
- アクティブトークン数

## トラブルシューティング

### 問題：Azure OpenAIサブドメイン名の競合

**エラーメッセージ:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**原因:**
サブスクリプションや環境名から生成されたサブドメイン名がすでに使用中で、以前のデプロイが完全に削除されていない可能性があります。

**解決策:**
1. **オプション1 - 異なる環境名を使う：**

   **Bash:**
   ```bash
   azd env new my-unique-env-name
   azd up
   ```
   
   **PowerShell:**
   ```powershell
   azd env new my-unique-env-name
   azd up
   ```

2. **オプション2 - Azureポータルから手動デプロイ:**
   - Azureポータルでリソース作成 → Azure OpenAIを選択
   - ユニークな名前を設定
   - 次のモデルをデプロイ：
     - **GPT-5.2**
     - **text-embedding-3-small**（RAGモジュール用）
   - **重要:** デプロイ名は`.env`の設定と一致させること
   - デプロイ完了後、「キーとエンドポイント」からエンドポイントとAPIキーを取得
   - プロジェクトのルートに以下のような `.env` ファイルを作成：

     **例 `.env` ファイル:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**モデルデプロイ名のガイドライン:**
- シンプルで一貫した名前を使う: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- デプロイ名は`.env`で設定した名前と正確に一致させること
- よくあるミス：モデル名とコードでの参照名が異なる

### 問題：選択したリージョンにGPT-5.2がない

**解決策:**
- GPT-5.2が利用可能なリージョンを選択（例：eastus2）
- 利用可能地域を確認：https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### 問題：デプロイのクォータ不足

**解決策:**
1. Azureポータルでクォータ増加を申請
2. または`main.bicep`の容量を低く設定（例：capacity: 10）

### 問題：ローカル実行時に「リソースが見つかりません」

**解決策:**
1. デプロイ情報を確認：`azd env get-values`
2. エンドポイントとキーが正しいか確認
3. Azureポータルにリソースグループが存在するか確認

### 問題：認証失敗

**解決策:**
- `AZURE_OPENAI_API_KEY` が正しく設定されているか確認
- キーは32文字の16進数文字列形式であること
- 必要ならAzureポータルから新しいキーを取得

### デプロイが失敗する

**問題:** `azd provision` がクォータや容量エラーで失敗する

**解決策:** 
1. 別のリージョンで試す［[Changing Azure Regions](../../../../01-introduction/infra)参照］
2. サブスクリプションにAzure OpenAIのクォータがあるか確認

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```


### アプリケーションが接続しない

**問題:** Javaアプリケーションが接続エラーを表示

**解決策:**
1. 環境変数が正しくエクスポートされているか確認：

   **Bash:**
   ```bash
   echo $AZURE_OPENAI_ENDPOINT
   echo $AZURE_OPENAI_API_KEY
   ```
   
   **PowerShell:**
   ```powershell
   Write-Host $env:AZURE_OPENAI_ENDPOINT
   Write-Host $env:AZURE_OPENAI_API_KEY
   ```

2. エンドポイント形式が正しいか確認（例：`https://xxx.openai.azure.com`）
3. APIキーがAzureポータルのプライマリかセカンダリキーか確認

**問題:** Azure OpenAIからの401 Unauthorized

**解決策:**
1. Azureポータル → 「キーとエンドポイント」から新しいAPIキーを取得
2. `AZURE_OPENAI_API_KEY` 環境変数を再設定
3. モデルデプロイが完了しているか確認（Azureポータルで確認）

### パフォーマンス問題

**問題:** 応答が遅い

**解決策:**
1. AzureポータルのメトリクスでOpenAIのトークン使用率とスロットリングを確認
2. TPM容量を増やす（制限に達している場合）
3. 推論処理レベル（低/中/高）を上げることを検討

## インフラストラクチャの更新

```
infra/
├── main.bicep                       # Main infrastructure definition
├── main.json                        # Compiled ARM template (auto-generated)
├── main.bicepparam                  # Parameter file
├── README.md                        # This file
└── core/
    └── ai/
        └── cognitiveservices.bicep  # Azure OpenAI module
```


## セキュリティ推奨事項

1. **APIキーを絶対にコミットしないこと** - 環境変数を使用する
2. **ローカルでは.envファイルを使用** - `.env`は`.gitignore`に追加
3. **キーは定期的にローテーション** - Azureポータルで新しいキーを発行
4. **アクセス制限を設ける** - Azure RBACを利用しアクセス権を管理
5. **使用状況を監視** - Azureポータルでコストアラートを設定

## 追加リソース

- [Azure OpenAI Service ドキュメント](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 モデルドキュメント](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ドキュメント](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ドキュメント](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI公式連携](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## サポート

問題があれば：
1. 上記の[トラブルシューティング](../../../../01-introduction/infra)セクションを参照
2. AzureポータルでAzure OpenAIサービスのヘルスを確認
3. リポジトリにIssueを投稿

## ライセンス

詳細はルートの[LICENSE](../../../../LICENSE)ファイルを参照してください。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責事項**：  
本書類はAI翻訳サービス「Co-op Translator」（https://github.com/Azure/co-op-translator）を使用して翻訳されています。正確さを期していますが、自動翻訳には誤りや不正確な部分が含まれる場合があります。元の書類の原文が公式の情報源として優先されます。重要な情報については、専門の翻訳者による翻訳を推奨します。本翻訳の使用により生じたいかなる誤解や誤解釈についても、当方は責任を負いかねます。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->