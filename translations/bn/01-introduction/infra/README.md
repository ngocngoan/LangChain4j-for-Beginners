# LangChain4j শুরু করার জন্য Azure অবকাঠামো

## বিষয়বস্তু সূচি

- [প্রয়োজনীয়তা](../../../../01-introduction/infra)
- [স্থাপত্য](../../../../01-introduction/infra)
- [তৈরি করা সম্পদসমূহ](../../../../01-introduction/infra)
- [দ্রুত শুরু](../../../../01-introduction/infra)
- [কনফিগারেশন](../../../../01-introduction/infra)
- [পরিচালনা কমান্ডসমূহ](../../../../01-introduction/infra)
- [খরচ অপ্টিমাইজেশন](../../../../01-introduction/infra)
- [মনিটরিং](../../../../01-introduction/infra)
- [সমস্যা সমাধান](../../../../01-introduction/infra)
- [ইনফ্রাস্ট্রাকচার আপডেট করা](../../../../01-introduction/infra)
- [পরিষ্কার করা](../../../../01-introduction/infra)
- [ফাইল স্ট্রাকচার](../../../../01-introduction/infra)
- [নিরাপত্তা পরামর্শ](../../../../01-introduction/infra)
- [অতিরিক্ত সম্পদসমূহ](../../../../01-introduction/infra)

এই ডিরেক্টরিটি Bicep এবং Azure Developer CLI (azd) ব্যবহার করে Azure OpenAI সম্পদ স্থাপনের জন্য Azure অবকাঠামো কোড (IaC) ধারণ করে।

## প্রয়োজনীয়তা

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (সংস্করণ 2.50.0 বা পরবর্তী)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (সংস্করণ 1.5.0 বা পরবর্তী)
- সম্পদ তৈরি করার অনুমোদন সহ একটি Azure সাবস্ক্রিপশন

## স্থাপত্য

**সহজীকৃত লোকাল ডেভেলপমেন্ট সেটআপ** - শুধুমাত্র Azure OpenAI স্থাপন করুন, সব অ্যাপ স্থানীয়ভাবে চালান।

ইনফ্রাস্ট্রাকচার নিম্নলিখিত Azure সম্পদগুলি স্থাপন করে:

### AI পরিষেবাসমূহ
- **Azure OpenAI**: দুটি মডেল স্থাপনের সাথে Cognitive Services:
  - **gpt-5.2**: চ্যাট কমপ্লিশন মডেল (২০ হাজার TPM ক্ষমতা)
  - **text-embedding-3-small**: RAG-এর জন্য এমবেডিং মডেল (২০ হাজার TPM ক্ষমতা)

### লোকাল ডেভেলপমেন্ট
সব Spring Boot অ্যাপ্লিকেশন আপনার মেশিনে স্থানীয়ভাবে চলে:
- 01-introduction (পোর্ট ৮০৮০)
- 02-prompt-engineering (পোর্ট ৮০৮৩)
- 03-rag (পোর্ট ৮০৮১)
- 04-tools (পোর্ট ৮০৮৪)

## তৈরি হওয়া সম্পদসমূহ

| সম্পদ প্রকার | সম্পদের নামের প্যাটার্ন | উদ্দেশ্য |
|--------------|------------------------|---------|
| Resource Group | `rg-{environmentName}` | সমস্ত সম্পদ ধারণ করে |
| Azure OpenAI | `aoai-{resourceToken}` | AI মডেল হোস্টিং |

> **মন্তব্য:** `{resourceToken}` একটি অনন্য স্ট্রিং যা সাবস্ক্রিপশন আইডি, পরিবেশের নাম এবং অবস্থান থেকে তৈরি হয়

## দ্রুত শুরু

### ১. Azure OpenAI স্থাপন করুন

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

প্রম্পট আসলে:
- আপনার Azure সাবস্ক্রিপশন নির্বাচন করুন
- একটি লোকেশন বেছে নিন (সুপারিশকৃত: GPT-5.2 উপলব্ধতার জন্য `eastus2`)
- পরিবেশের নাম নিশ্চিত করুন (ডিফল্ট: `langchain4j-dev`)

এটি তৈরি করবে:
- GPT-5.2 এবং text-embedding-3-small সহ Azure OpenAI সম্পদ
- আউটপুট সংযোগ বিবরণ

### ২. সংযোগ বিবরণ নিন

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

এটি প্রদর্শন করবে:
- `AZURE_OPENAI_ENDPOINT`: আপনার Azure OpenAI এন্ডপয়েন্ট URL
- `AZURE_OPENAI_KEY`: প্রমাণীকরণের জন্য API কী
- `AZURE_OPENAI_DEPLOYMENT`: চ্যাট মডেলের নাম (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: এমবেডিং মডেলের নাম

### ৩. অ্যাপ্লিকেশনগুলো স্থানীয়ভাবে চালান

`azd up` কমান্ড স্বয়ংক্রিয়ভাবে রুট ডিরেক্টরিতে `.env` ফাইল তৈরি করে যা সমস্ত প্রয়োজনীয় পরিবেশ পরিবর্তনশীল ধারণ করে।

**সুপারিশ:** সব ওয়েব অ্যাপ্লিকেশন শুরু করুন:

**Bash:**
```bash
# রুট ডিরেক্টরি থেকে
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# রুট ডিরেক্টরি থেকে
cd ../..
.\start-all.ps1
```

অথবা একটি একক মডিউল শুরু করুন:

**Bash:**
```bash
# উদাহরণ: শুধুমাত্র পরিচিতি মডিউল শুরু করুন
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# উদাহরণ: শুধুমাত্র পরিচিতি মডিউল শুরু করুন
cd ../01-introduction
.\start.ps1
```

উভয় স্ক্রিপ্ট `azd up` দ্বারা তৈরি রুট `.env` ফাইল থেকে পরিবেশ পরিবর্তনশীলগুলি স্বয়ংক্রিয়ভাবে লোড করে।

## কনফিগারেশন

### মডেল ডেপ্লয়মেন্ট কাস্টমাইজেশন

মডেল ডেপ্লয়মেন্ট পরিবর্তন করতে, `infra/main.bicep` সম্পাদনা করুন এবং `openAiDeployments` প্যারামিটার পরিবর্তন করুন:

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

উপলব্ধ মডেল এবং সংস্করণসমূহ: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Azure এলাকা পরিবর্তন

অন্য একটি অঞ্চলে ডেপ্লয় করতে, `infra/main.bicep` সম্পাদনা করুন:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

GPT-5.2 উপলব্ধতা দেখুন: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Bicep ফাইল পরিবর্তনের পরে ইনফ্রাস্ট্রাকচার আপডেট করতে:

**Bash:**
```bash
# ARM টেমপ্লেট পুনর্নিমাণ করুন
az bicep build --file infra/main.bicep

# পরিবর্তনগুলি পূর্বরূপ দেখুন
azd provision --preview

# পরিবর্তনগুলি প্রয়োগ করুন
azd provision
```

**PowerShell:**
```powershell
# ARM টেমপ্লেট পুনর্নির্মাণ করুন
az bicep build --file infra/main.bicep

# পরিবর্তনগুলি পূর্বরূপ দেখুন
azd provision --preview

# পরিবর্তনগুলি প্রয়োগ করুন
azd provision
```

## পরিষ্কার করুন

সব সম্পদ মুছে ফেলতে:

**Bash:**
```bash
# সকল সম্পদ মুছে ফেলুন
azd down

# পরিবেশসহ সবকিছু মুছে ফেলুন
azd down --purge
```

**PowerShell:**
```powershell
# সমস্ত সম্পদ মুছুন
azd down

# পরিবেশ সহ সবকিছু মুছুন
azd down --purge
```

**সতর্কতা**: এটি সব Azure সম্পদ স্থায়ীভাবে মুছে ফেলবে।

## ফাইল স্ট্রাকচার

## খরচ অপ্টিমাইজেশন

### ডেভেলপমেন্ট/পরীক্ষা
ডেভ/টেস্ট পরিবেশের জন্য, আপনি খরচ কমাতে পারেন:
- Azure OpenAI এর জন্য Standard স্তর (S0) ব্যবহার করুন
- `infra/core/ai/cognitiveservices.bicep` এ ক্ষমতা কমিয়ে (২০ হাজারের পরিবর্তে ১০ হাজার TPM) সেট করুন
- ব্যবহার না হলে সম্পদ মুছে ফেলুন: `azd down`

### প্রোডাকশন
প্রোডাকশনের জন্য:
- ব্যবহারের ভিত্তিতে OpenAI ক্ষমতা বাড়ান (৫০ হাজার TPM+)
- উচ্চ উপলব্ধতার জন্য জোন রিডন্ডেন্সি সক্রিয় করুন
- সঠিক মনিটরিং ও খরচ সতর্কতা প্রয়োগ করুন

### খরচ অনুমান
- Azure OpenAI: প্রতিটা টোকেনের জন্য (ইনপুট + আউটপুট) পেমেন্ট
- GPT-5.2: প্রতি ১ মিলিয়ন টোকেন $৩-৫ এর মতো (বর্তমান মূল্য যাচাই করুন)
- text-embedding-3-small: প্রতি ১ মিলিয়ন টোকেন $০.০২ এর মতো

মূল্য নিরূপণ যন্ত্র: https://azure.microsoft.com/pricing/calculator/

## মনিটরিং

### Azure OpenAI মেট্রিকস দেখুন

Azure পোর্টালে যান → আপনার OpenAI সম্পদ → মেট্রিকস:
- টোকেন ভিত্তিক ব্যবহার
- HTTP অনুরোধের হার
- প্রতিক্রিয়া সময়
- সক্রিয় টোকেন

## সমস্যা সমাধান

### সমস্যা: Azure OpenAI সাবডোমেইন নামের দ্বন্দ্ব

**ত্রুটি বার্তা:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**কারণ:**
সাবস্ক্রিপশন/পরিবেশ থেকে তৈরি সাবডোমেইন নাম ইতিমধ্যে ব্যবহৃত হচ্ছে, সম্ভবত পূর্ববর্তী ডেপ্লয়মেন্ট সম্পূর্ণরূপে মুছে ফেলা হয়নি।

**সমাধান:**
1. **বিকল্প ১ - অন্য পরিবেশের নাম ব্যবহার করুন:**
   
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

2. **বিকল্প ২ - Azure পোর্টালের মাধ্যমে ম্যানুয়াল ডেপ্লয়মেন্ট:**
   - Azure পোর্টালে যান → রিসোর্স তৈরি করুন → Azure OpenAI
   - আপনার সম্পদের জন্য একটি অনন্য নাম বেছে নিন
   - নিম্নলিখিত মডেলগুলো ডেপ্লয় করুন:
     - **GPT-5.2**
     - **text-embedding-3-small** (RAG মডিউলগুলির জন্য)
   - **গুরুত্বপূর্ণ:** আপনার ডেপ্লয়মেন্ট নামগুলো নোট করুন - এগুলো `.env` কনফিগারেশনের সঙ্গে মেলাতে হবে
   - ডেপ্লয়মেন্টের পরে, "Keys and Endpoint" থেকে এন্ডপয়েন্ট এবং API কী নিন
   - প্রজেক্ট রুটে `.env` ফাইল তৈরি করুন যেখানে:

     **উদাহরণ `.env` ফাইল:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**মডেল ডেপ্লয়মেন্ট নামকরণের নির্দেশিকা:**
- সরল, ধারাবাহিক নাম ব্যবহার করুন: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- ডেপ্লয়মেন্ট নাম অবশ্যই `.env` এ কনফিগার করা নামের সাথে সঠিকভাবে মেলাতে হবে
- সাধারণ ভুল: একটি নাম দিয়ে মডেল তৈরি এবং অন্য নাম দিয়ে কোডে উল্লেখ করা

### সমস্যা: নির্বাচিত অঞ্চলে GPT-5.2 উপলব্ধ নয়

**সমাধান:**
- GPT-5.2 উপলব্ধ একটি অঞ্চল বেছে নিন (যেমন, eastus2)
- উপলব্ধতা যাচাই করুন: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### সমস্যা: ডেপ্লয়মেন্টের জন্য যথেষ্ট কোটা নেই

**সমাধান:**
1. Azure পোর্টালে কোটা বৃদ্ধি প্রার্থনা করুন
2. অথবা `main.bicep` এ কম ক্ষমতা ব্যবহার করুন (যেমন, capacity: 10)

### সমস্যা: স্থানীয়ভাবে চালানোর সময় "Resource not found"

**সমাধান:**
1. ডেপ্লয়মেন্ট যাচাই করুন: `azd env get-values`
2. এন্ডপয়েন্ট এবং কী সঠিক কিনা চেক করুন
3. Azure পোর্টালে রিসোর্স গ্রুপের উপস্থিতি নিশ্চিত করুন

### সমস্যা: প্রমাণীকরণ ব্যর্থ

**সমাধান:**
- `AZURE_OPENAI_API_KEY` সঠিকভাবে সেট আছে কিনা যাচাই করুন
- কী ফরম্যাট ৩২-অক্ষরের হেক্সাডেসিমাল স্ট্রিং হওয়া উচিত
- প্রয়োজন হলে Azure পোর্টাল থেকে নতুন কি নিন

### ডেপ্লয়মেন্ট ব্যর্থ হয়

**সমস্যা**: `azd provision` কোটা বা ক্ষমতা ত্রুটির কারণে ব্যর্থ

**সমাধান**: 
1. অন্য একটি অঞ্চল চেষ্টা করুন - বিস্তারিত দেখুন [Azure অঞ্চল পরিবর্তন](../../../../01-introduction/infra) বিভাগে
2. আপনার সাবস্ক্রিপশনে Azure OpenAI কোটা আছে কিনা যাচাই করুন:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### অ্যাপ্লিকেশন সংযোগ হচ্ছেনা

**সমস্যা**: Java অ্যাপ্লিকেশন সংযোগ ত্রুটি দেখায়

**সমাধান**:
1. পরিবেশ পরিবর্তনশীলগুলি রপ্তানি করা হয়েছে কিনা নিশ্চিত করুন:
   
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

2. এন্ডপয়েন্ট ফরম্যাট সঠিক কিনা যাচাই করুন (`https://xxx.openai.azure.com` হওয়া উচিত)
3. API কী Azure পোর্টালের প্রাইমারি বা সেকেন্ডারি কী কিনা দেখুন

**সমস্যা**: Azure OpenAI থেকে 401 Unauthorized

**সমাধান**:
1. Azure পোর্টাল → Keys and Endpoint থেকে নতুন API কী নিন
2. `AZURE_OPENAI_API_KEY` পরিবেশ পরিবর্তনশীল পুনরায় রপ্তানি করুন
3. মডেল ডেপ্লয়মেন্ট সম্পূর্ণ হয়েছে কিনা Azure পোর্টালে চেক করুন

### কর্মক্ষমতা সমস্যা

**সমস্যা**: ধীর প্রতিক্রিয়া সময়

**সমাধান**:
1. Azure পোর্টাল মেট্রিক্স থেকে OpenAI টোকেন ব্যবহার এবং থ্রটল চেক করুন
2. সীমা পৌঁছালে TPM ক্ষমতা বাড়ান
3. উচ্চ পর্যায়ের যুক্তির চেষ্টা করুন (কম/মধ্যম/উচ্চ)

## ইনফ্রাস্ট্রাকচার আপডেট করা

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

## নিরাপত্তা পরামর্শ

1. **কখনো API কী কমিট করবেন না** - পরিবেশ পরিবর্তনশীল ব্যবহার করুন
2. **লোকাল এ .env ফাইল ব্যবহার করুন** - `.env` কে `.gitignore` এ যোগ করুন
3. **নিয়মিত কী পরিবর্তন করুন** - Azure পোর্টালে নতুন কী তৈরি করুন
4. **প্রবেশাধিকার সীমাবদ্ধ করুন** - Azure RBAC ব্যবহার করে কারা সম্পদে প্রবেশ করতে পারে নিয়ন্ত্রণ করুন
5. **ব্যবহার মনিটর করুন** - Azure পোর্টালে খরচ সতর্কতা সেট করুন

## অতিরিক্ত সম্পদসমূহ

- [Azure OpenAI সার্ভিস ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 মডেল ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI ডকুমেন্টেশন](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep ডকুমেন্টেশন](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI অফিসিয়াল ইন্টিগ্রেশন](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## সহায়তা

সমস্যার জন্য:
1. উপরের [সমস্যা সমাধান অংশ](../../../../01-introduction/infra) পরীক্ষা করুন
2. Azure পোর্টালে Azure OpenAI সার্ভিসের স্বাস্থ্য পর্যালোচনা করুন
3. রিপোজিটরিতে একটি ইস্যু তৈরি করুন

## লাইসেন্স

বিস্তারিত জানার জন্য রুটের [LICENSE](../../../../LICENSE) ফাইল দেখুন।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ছাড়পত্র**:  
এই ডকুমেন্টটি এআই অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। আমরা যথাসাধ্য সঠিকতার চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল ভাষায় লেখাটিকেই কর্তৃত্বপূর্ণ উৎস হিসেবে গণ্য করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদের পরামর্শ দেওয়া হয়। এই অনুবাদের ব্যবহারে প্রাপ্ত কোনো ভুল বোঝাবুঝি বা ভ্রান্ত ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->