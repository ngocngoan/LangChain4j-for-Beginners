# Azure Інфраструктура для LangChain4j Початок роботи

## Зміст

- [Попередні умови](../../../../01-introduction/infra)
- [Архітектура](../../../../01-introduction/infra)
- [Створені ресурси](../../../../01-introduction/infra)
- [Швидкий старт](../../../../01-introduction/infra)
- [Конфігурація](../../../../01-introduction/infra)
- [Команди управління](../../../../01-introduction/infra)
- [Оптимізація витрат](../../../../01-introduction/infra)
- [Моніторинг](../../../../01-introduction/infra)
- [Вирішення проблем](../../../../01-introduction/infra)
- [Оновлення інфраструктури](../../../../01-introduction/infra)
- [Прибирання](../../../../01-introduction/infra)
- [Структура файлів](../../../../01-introduction/infra)
- [Рекомендації з безпеки](../../../../01-introduction/infra)
- [Додаткові ресурси](../../../../01-introduction/infra)

Цей каталог містить інфраструктуру Azure як код (IaC) за допомогою Bicep та Azure Developer CLI (azd) для розгортання ресурсів Azure OpenAI.

## Попередні умови

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (версія 2.50.0 або вище)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (версія 1.5.0 або вище)
- Підписка Azure з правами створення ресурсів

## Архітектура

**Спрощена локальна розробка** - розгортається лише Azure OpenAI, всі додатки працюють локально.

Інфраструктура розгортає наступні ресурси Azure:

### AI сервіси
- **Azure OpenAI**: Cognitive Services з двома розгортаннями моделей:
  - **gpt-5.2**: Модель чат-компліції (пропускна здатність 20K TPM)
  - **text-embedding-3-small**: Модель для ембедингів для RAG (пропускна здатність 20K TPM)

### Локальна розробка
Усі Spring Boot додатки запускаються локально на вашому комп’ютері:
- 01-introduction (порт 8080)
- 02-prompt-engineering (порт 8083)
- 03-rag (порт 8081)
- 04-tools (порт 8084)

## Створені ресурси

| Тип ресурсу | Шаблон імені ресурсу | Призначення |
|--------------|----------------------|-------------|
| Resource Group | `rg-{environmentName}` | Містить всі ресурси |
| Azure OpenAI | `aoai-{resourceToken}` | Хостинг AI моделей |

> **Примітка:** `{resourceToken}` — унікальний рядок, який генерується з ID підписки, імені середовища та розташування

## Швидкий старт

### 1. Розгорніть Azure OpenAI

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

Під час запиту:
- Оберіть вашу підписку Azure
- Оберіть регіон (рекомендовано: `eastus2` для доступності GPT-5.2)
- Підтвердіть ім'я оточення (за замовчуванням: `langchain4j-dev`)

Це створить:
- Ресурс Azure OpenAI з GPT-5.2 та text-embedding-3-small
- Вивід деталей підключення

### 2. Отримайте деталі з’єднання

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Це покаже:
- `AZURE_OPENAI_ENDPOINT`: URL вашої точки доступу Azure OpenAI
- `AZURE_OPENAI_KEY`: Ключ API для автентифікації
- `AZURE_OPENAI_DEPLOYMENT`: Назва чат-моделі (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: Назва моделі ембедингів

### 3. Запустіть додатки локально

Команда `azd up` автоматично створює файл `.env` у кореневому каталозі з усіма необхідними змінними оточення.

**Рекомендовано:** Запустити всі веб-додатки:

**Bash:**
```bash
# З кореневого каталогу
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# З кореневого каталогу
cd ../..
.\start-all.ps1
```

Або запустіть окремий модуль:

**Bash:**
```bash
# Приклад: Запустіть лише модуль введення
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Приклад: Запустіть лише модуль вступу
cd ../01-introduction
.\start.ps1
```

Обидва скрипти автоматично завантажують змінні оточення з кореневого файлу `.env`, створеного `azd up`.

## Конфігурація

### Налаштування розгортання моделей

Щоб змінити розгортання моделей, відредагуйте `infra/main.bicep` і змініть параметр `openAiDeployments`:

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

Доступні моделі та версії: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Зміна регіонів Azure

Щоб розгорнути в іншому регіоні, відредагуйте `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Перевірте доступність GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Щоб оновити інфраструктуру після змін у Bicep файлах:

**Bash:**
```bash
# Перебудувати ARM-шаблон
az bicep build --file infra/main.bicep

# Попередній перегляд змін
azd provision --preview

# Застосувати зміни
azd provision
```

**PowerShell:**
```powershell
# Перебудувати ARM шаблон
az bicep build --file infra/main.bicep

# Попередній перегляд змін
azd provision --preview

# Застосувати зміни
azd provision
```

## Прибирання

Щоб видалити всі ресурси:

**Bash:**
```bash
# Видалити всі ресурси
azd down

# Видалити все, включно з оточенням
azd down --purge
```

**PowerShell:**
```powershell
# Видалити всі ресурси
azd down

# Видалити все, включно з середовищем
azd down --purge
```

**Попередження**: Це остаточно видалить усі ресурси Azure.

## Структура файлів

## Оптимізація витрат

### Розробка/Тестування
Для оточень розробки/тестування можна зменшити витрати:
- Використовуйте стандартний рівень (S0) для Azure OpenAI
- Встановіть нижчу пропускну здатність (10K TPM замість 20K) у `infra/core/ai/cognitiveservices.bicep`
- Видаляйте ресурси, коли не використовуєте: `azd down`

### Продакшн
Для продакшну:
- Збільшіть пропускну здатність OpenAI залежно від використання (50K+ TPM)
- Увімкніть зону з резервуванням для більшої доступності
- Впровадьте моніторинг і оповіщення про витрати

### Оцінка вартості
- Azure OpenAI: Оплата за токен (вхідні + вихідні)
- GPT-5.2: приблизно $3–5 за 1 млн токенів (перевірте актуальні тарифи)
- text-embedding-3-small: приблизно $0.02 за 1 млн токенів

Калькулятор цін: https://azure.microsoft.com/pricing/calculator/

## Моніторинг

### Перегляд метрик Azure OpenAI

Зайдіть в Azure Portal → Ваш ресурс OpenAI → Метрики:
- Використання за токенами
- Частота HTTP-запитів
- Час відповіді
- Активні токени

## Вирішення проблем

### Проблема: Конфлікт імені піддомену Azure OpenAI

**Повідомлення про помилку:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Причина:**
Ім'я піддомену, згенероване з підписки/оточення, вже використовується, можливо, через попереднє розгортання, яке не було повністю вилучено.

**Рішення:**
1. **Варіант 1 - Використовуйте інше ім’я оточення:**
   
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

2. **Варіант 2 - Ручне розгортання через Azure Portal:**
   - Зайдіть в Azure Portal → Створити ресурс → Azure OpenAI
   - Оберіть унікальне ім’я для ресурсу
   - Розгорніть такі моделі:
     - **GPT-5.2**
     - **text-embedding-3-small** (для RAG модулів)
   - **Важливо:** Запам'ятайте імена розгортання – вони мають відповідати конфігурації `.env`
   - Після розгортання отримайте endpoint та API ключ у "Keys and Endpoint"
   - Створіть `.env` файл у корені проєкту з таким вмістом:
     
     **Приклад файлу `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Рекомендації щодо іменування розгортань моделей:**
- Використовуйте прості, узгоджені імена: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Імена розгортань мають точно відповідати тим, що вказані у `.env`
- Часте помилкове використання: створення моделі з одним іменем, а у коді посилання на інше

### Проблема: GPT-5.2 недоступна у вибраному регіоні

**Рішення:**
- Оберіть регіон із доступом до GPT-5.2 (наприклад, eastus2)
- Перевірте доступність: https://learn.microsoft.com/azure/ai-services/openai/concepts/models



### Проблема: Недостатньо квоти для розгортання

**Рішення:**
1. Зробіть запит на збільшення квоти у Azure Portal
2. Або використайте нижчу пропускну здатність у `main.bicep` (наприклад, capacity: 10)

### Проблема: "Resource not found" при запуску локально

**Рішення:**
1. Перевірте розгортання: `azd env get-values`
2. Переконайтеся, що endpoint і ключ правильні
3. Перевірте існування Resource Group в Azure Portal

### Проблема: Помилка автентифікації

**Рішення:**
- Переконайтеся, що змінна `AZURE_OPENAI_API_KEY` встановлена правильно
- Формат ключа повинен бути 32-значним шістнадцятковим рядком
- Якщо потрібно, отримайте новий ключ в Azure Portal

### Помилка розгортання

**Проблема**: `azd provision` не вдається через помилки квоти або пропускної здатності

**Рішення**: 
1. Спробуйте інший регіон - див. розділ [Зміна регіонів Azure](../../../../01-introduction/infra)
2. Перевірте, чи є у вашій підписці квота Azure OpenAI:
   
   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```

### Додаток не підключається

**Проблема**: У Java-додатку помилки підключення

**Рішення**:
1. Переконайтеся, що змінні оточення експортуються:
   
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

2. Перевірте правильність формату endpoint (має бути `https://xxx.openai.azure.com`)
3. Переконайтеся, що ключ API є основним або вторинним з Azure Portal

**Проблема**: 401 Unauthorized від Azure OpenAI

**Рішення**:
1. Отримайте новий ключ API з Azure Portal → Keys and Endpoint
2. Заново експортуйте змінну оточення `AZURE_OPENAI_API_KEY`
3. Переконайтеся, що розгортання моделей завершено (перевірте Azure Portal)

### Проблеми з продуктивністю

**Проблема**: Повільна відповідь

**Рішення**:
1. Перевірте використання токенів і обмеження в метриках Azure Portal
2. Збільшіть пропускну здатність TPM, якщо досягли лімітів
3. Розгляньте можливість використання вищого рівня зусиль міркування (низький/середній/високий)

## Оновлення інфраструктури

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

## Рекомендації з безпеки

1. **Ніколи не комітьте ключі API** – використовуйте змінні оточення
2. **Використовуйте .env файли локально** – додайте `.env` до `.gitignore`
3. **Регулярно змінюйте ключі** – генеруйте нові у Azure Portal
4. **Обмежуйте доступ** – використовуйте Azure RBAC для контролю доступу до ресурсів
5. **Моніторте використання** – налаштуйте оповіщення про витрати у Azure Portal

## Додаткові ресурси

- [Документація Azure OpenAI Service](https://learn.microsoft.com/azure/ai-services/openai/)
- [Документація моделі GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Документація Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Документація Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Офіційна інтеграція LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Підтримка

Для вирішення проблем:
1. Перевірте [розділ вирішення проблем](../../../../01-introduction/infra) вище
2. Перегляньте стан служби Azure OpenAI в Azure Portal
3. Відкрийте issue у репозиторії

## Ліцензія

Деталі дивіться у файлі [LICENSE](../../../../LICENSE) у корені.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний переклад людиною. Ми не несемо відповідальності за будь-які непорозуміння чи неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->