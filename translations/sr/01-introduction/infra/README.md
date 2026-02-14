# Azure инфраструктура за LangChain4j Почетак рада

## Садржај

- [Предуслови](../../../../01-introduction/infra)
- [Архитектура](../../../../01-introduction/infra)
- [Креирани ресурси](../../../../01-introduction/infra)
- [Брзи почетак](../../../../01-introduction/infra)
- [Конфигурација](../../../../01-introduction/infra)
- [Команде за управљање](../../../../01-introduction/infra)
- [Оптимизација трошкова](../../../../01-introduction/infra)
- [Мониторинг](../../../../01-introduction/infra)
- [Решавање проблема](../../../../01-introduction/infra)
- [Ажурирање инфраструктуре](../../../../01-introduction/infra)
- [Чишћење](../../../../01-introduction/infra)
- [Структура фајлова](../../../../01-introduction/infra)
- [Препоруке за безбедност](../../../../01-introduction/infra)
- [Додатни ресурси](../../../../01-introduction/infra)

Овај директоријум садржи Azure инфраструктуру као код (IaC) користећи Bicep и Azure Developer CLI (azd) за деплојовање Azure OpenAI ресурса.

## Предуслови

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (верзија 2.50.0 или новија)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (верзија 1.5.0 или новија)
- Azure претплата са дозволама за креирање ресурса

## Архитектура

**Поједностављена локална развојна конфигурација** - Деплојује само Azure OpenAI, све апликације се извршавају локално.

Инфраструктура деплојује следеће Azure ресурсе:

### AI Услуге
- **Azure OpenAI**: Cognitive Services са две имплементације модела:
  - **gpt-5.2**: Модел за ћаскање (20K TPM капацитета)
  - **text-embedding-3-small**: Модел за уграђивање за RAG (20K TPM капацитета)

### Локални развој
Све Spring Boot апликације се извршавају локално на вашем рачунару:
- 01-introduction (порт 8080)
- 02-prompt-engineering (порт 8083)
- 03-rag (порт 8081)
- 04-tools (порт 8084)

## Креирани ресурси

| Тип ресурса | Образац имена ресурса | Намењеност |
|--------------|----------------------|---------|
| Ресурсна група | `rg-{environmentName}` | Садржи све ресурсе |
| Azure OpenAI | `aoai-{resourceToken}` | Хостирање AI модела |

> **Напомена:** `{resourceToken}` је јединствени низ генерисан из ID претплате, имена окружења и локације

## Брзи почетак

### 1. Деплојујте Azure OpenAI

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

Када будете упитани:
- Изаберите своју Azure претплату
- Одаберите локацију (препоручено: `eastus2` због доступности GPT-5.2)
- Потврдите име окружења (подразумевано: `langchain4j-dev`)

Ово ће креирати:
- Azure OpenAI ресурс са GPT-5.2 и text-embedding-3-small
- Исписати детаље везе

### 2. Преузмите детаље везе

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Приказује:
- `AZURE_OPENAI_ENDPOINT`: Ваш Azure OpenAI крајњи URL
- `AZURE_OPENAI_KEY`: API кључ за аутентификацију
- `AZURE_OPENAI_DEPLOYMENT`: Име ћаскаћег модела (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Име модела за уграђивање

### 3. Покрените апликације локално

Команда `azd up` аутоматски креира `.env` фајл у коренском директоријуму са свим потребним променљивим окружења.

**Препоручено:** Покрените све веб апликације:

**Bash:**
```bash
# Из главног директоријума
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Из коренског директоријума
cd ../..
.\start-all.ps1
```

Или стартујте само један модул:

**Bash:**
```bash
# Пример: Покрени само модул увод
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Пример: Покрени само модул уводног дела
cd ../01-introduction
.\start.ps1
```

Оба скрипта аутоматски учитавају променљиве окружења из `.env` фајла у корену који је креирао `azd up`.

## Конфигурација

### Прилагођавање имплементација модела

Да бисте променили модел имплементације, уредите `infra/main.bicep` и модификујте параметар `openAiDeployments`:

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

Доступни модели и верзије: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Промена Azure регија

Да бисте деплојовали у другој регији, уредите `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Проверите доступност GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

За ажурирање инфраструктуре након измена Bicep фајлова:

**Bash:**
```bash
# Поново изгради ARM шаблон
az bicep build --file infra/main.bicep

# Прегледај промене
azd provision --preview

# Примени промене
azd provision
```

**PowerShell:**
```powershell
# Поново изгради ARM шаблон
az bicep build --file infra/main.bicep

# Прегледај промене
azd provision --preview

# Примени промене
azd provision
```

## Чишћење

За брисање свих ресурса:

**Bash:**
```bash
# Обриши све ресурсе
azd down

# Обриши све укључујући и окружење
azd down --purge
```

**PowerShell:**
```powershell
# Обриши све ресурсе
azd down

# Обриши све укључујући и окружење
azd down --purge
```

**Упозорење**: Ово ће трајно обрисати све Azure ресурсе.

## Структура фајлова

## Оптимизација трошкова

### Развој/Тестирање
За развојна/тест окружења можете смањити трошкове:
- Користите Standard tier (S0) за Azure OpenAI
- Поставите мању капацитет (10K TPM уместо 20K) у `infra/core/ai/cognitiveservices.bicep`
- Обришите ресурсе када нису у употреби: `azd down`

### Продукција
За продукцију:
- Повећајте капацитет OpenAI у складу са коришћењем (50K+ TPM)
- Омогућите зону редунданцију за већу доступност
- Имплементирајте одговарајући мониторинг и аларме за трошкове

### Процена трошкова
- Azure OpenAI: Плаћање по токену (улаз + излаз)
- GPT-5.2: око $3-5 по 1M токена (проверите актуелне цене)
- text-embedding-3-small: око $0.02 по 1M токена

Калкулатор цена: https://azure.microsoft.com/pricing/calculator/

## Мониторинг

### Приказ метрика Azure OpenAI

Идите у Azure портал → Ваш OpenAI ресурс → Метрике:
- Кориšћење по токенима
- Број HTTP захтева
- Време одговора
- Активни токени

## Решавање проблема

### Проблем: Конфликт имена субдомена Azure OpenAI

**Порука о грешци:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Узрок:**
Име субдомена генерисано из ваше претплате/окружења је већ у употреби, могуће из претходне деплојације која није била потпуно уклоњена.

**Решење:**
1. **Опција 1 - Користите друго име окружења:**
   
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

2. **Опција 2 - Ручно деплојујте преко Azure портала:**
   - Идите у Azure портал → Креирајте ресурс → Azure OpenAI
   - Изаберите јединствено име за ваш ресурс
   - Деплојујте следеће моделе:
     - **GPT-5.2**
     - **text-embedding-3-small** (за RAG модуле)
   - **Важно:** Запамтите имена ваша имплементације - морају одговарати конфигурацији у `.env`
   - Након деплојације, преузмите крајњу тачку и API кључ из "Keys and Endpoint"
   - Креирајте `.env` фајл у корену пројекта са:
     
     **Пример `.env` фајла:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Смернице за именовање имплементација модела:**
- Користите једноставна, доследна имена: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Имена имплементације морају савршено одговарати оним која конфигуришете у `.env`
- Честа грешка: Креирање модела са једним именом, а у коду се користи друго име

### Проблем: GPT-5.2 није доступан у изабраној регији

**Решење:**
- Изаберите регију са приступом GPT-5.2 (нпр. eastus2)
- Проверите доступност: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Проблем: Недовољан квота за имплементацију

**Решење:**
1. Затражите повећање квоте у Azure порталу
2. Или користите мањи капацитет у `main.bicep` (нпр. capacity: 10)

### Проблем: "Resource not found" при локалном покретању

**Решење:**
1. Потврдите имплементацију: `azd env get-values`
2. Проверите да ли су крајња тачка и кључ тачни
3. Уверите се да ресурсна група постоји у Azure порталу

### Проблем: Аутентификација није успела

**Решење:**
- Проверите да ли је `AZURE_OPENAI_API_KEY` исправно подешен
- Формат кључа мора бити 32-карактерски хексадецимални низ
- Ако треба, узмите нови кључ из Azure портала

### Неуспех имплементације

**Проблем**: `azd provision` не успева због квотних или капацитетских грешака

**Решење**: 
1. Покушајте другу регију - Погледајте одељак [Промена Azure регија](../../../../01-introduction/infra)
2. Проверите да ваша претплата има Azure OpenAI квоту:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Апликација се не повезује

**Проблем**: Java апликација приказује грешке везе

**Решење**:
1. Потврдите да су променљиве окружења експороване:
   
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

2. Проверите да ли је формат крајње тачке исправан (треба да је `https://xxx.openai.azure.com`)
3. Проверите да је API кључ примарни или секундарни кључ из Azure портала

**Проблем**: 401 Unauthorized од Azure OpenAI

**Решење**:
1. Узмите свеж API кључ из Azure портала → Keys and Endpoint
2. Поново експортујте променљиве окружења `AZURE_OPENAI_API_KEY`
3. Проверите да су имплементације модела завршене (погледајте у Azure порталу)

### Проблеми са перформансама

**Проблем**: Спори одговори

**Решење**:
1. Проверите коришћење токена и ограничења у метрици Azure портала
2. Повећајте TPM капацитет ако достижете лимите
3. Размислите о коришћењу вишег нивоа напора резоновања (нски/средњи/високи)

## Ажурирање инфраструктуре

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

## Препоруке за безбедност

1. **Никад не уписујте API кључеве у репозиторијум** - Користите променљиве окружења
2. **Локално користите .env фајлове** - Додајте `.env` у `.gitignore`
3. **Редовно ротирајте кључеве** - Генеришите нове у Azure порталу
4. **Ограничавајте приступ** - Користите Azure RBAC за контролу приступа ресурсима
5. **Пратите коришћење** - Поставите аларме за трошкове у Azure порталу

## Додатни ресурси

- [Azure OpenAI Сервис Документација](https://learn.microsoft.com/azure/ai-services/openai/)
- [GPT-5.2 Модел Документација](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Azure Developer CLI Документација](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Bicep Документација](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [LangChain4j OpenAI званична интеграција](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Подршка

За проблеме:
1. Погледајте [одељак за решавање проблема](../../../../01-introduction/infra) изнад
2. Проверите здравствено стање Azure OpenAI сервиса у Azure порталу
3. Отворите issue у репозиторијуму

## Лиценца

Погледајте коренски [LICENSE](../../../../LICENSE) фајл за детаље.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Искључење одговорности**:
Овај документ је преведен помоћу AI услуге за превођење [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо прецизности, молимо имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални превод од стране људских преводилаца. Нисмо одговорни за било каква неспоразуме или погрешне тумачења проистекла из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->