# Azure-инфраструктура для LangChain4j. Начало работы

## Содержание

- [Требования](../../../../01-introduction/infra)
- [Архитектура](../../../../01-introduction/infra)
- [Созданные ресурсы](../../../../01-introduction/infra)
- [Быстрый старт](../../../../01-introduction/infra)
- [Конфигурация](../../../../01-introduction/infra)
- [Команды управления](../../../../01-introduction/infra)
- [Оптимизация затрат](../../../../01-introduction/infra)
- [Мониторинг](../../../../01-introduction/infra)
- [Устранение неполадок](../../../../01-introduction/infra)
- [Обновление инфраструктуры](../../../../01-introduction/infra)
- [Очистка](../../../../01-introduction/infra)
- [Структура файлов](../../../../01-introduction/infra)
- [Рекомендации по безопасности](../../../../01-introduction/infra)
- [Дополнительные ресурсы](../../../../01-introduction/infra)

В этой директории находится инфраструктура Azure как код (IaC) с использованием Bicep и Azure Developer CLI (azd) для развертывания ресурсов Azure OpenAI.

## Требования

- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) (версия 2.50.0 или выше)
- [Azure Developer CLI (azd)](https://learn.microsoft.com/azure/developer/azure-developer-cli/install-azd) (версия 1.5.0 или выше)
- Подписка Azure с правами на создание ресурсов

## Архитектура

**Упрощённая локальная разработка** — развертывается только Azure OpenAI, все приложения запускаются локально.

Инфраструктура развёртывает следующие ресурсы Azure:

### AI Сервисы
- **Azure OpenAI**: Когнитивные сервисы с двумя развертываниями моделей:
  - **gpt-5.2**: модель для чат-комплешена (пропускная способность 20K TPM)
  - **text-embedding-3-small**: модель встраивания для RAG (пропускная способность 20K TPM)

### Локальная разработка
Все приложения Spring Boot запускаются локально на вашем компьютере:
- 01-introduction (порт 8080)
- 02-prompt-engineering (порт 8083)
- 03-rag (порт 8081)
- 04-tools (порт 8084)

## Созданные ресурсы

| Тип ресурса | Шаблон имени ресурса | Назначение |
|--------------|----------------------|-------------|
| Группа ресурсов | `rg-{environmentName}` | Содержит все ресурсы |
| Azure OpenAI | `aoai-{resourceToken}` | Хостинг модели ИИ |

> **Примечание:** `{resourceToken}` — уникальная строка, сгенерированная из ID подписки, имени окружения и региона

## Быстрый старт

### 1. Разверните Azure OpenAI

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

При запросе:
- Выберите подписку Azure
- Выберите регион (рекомендуется: `eastus2` для доступности GPT-5.2)
- Подтвердите имя окружения (по умолчанию: `langchain4j-dev`)

Будут созданы:
- Ресурс Azure OpenAI с GPT-5.2 и text-embedding-3-small
- Вывод деталей подключения

### 2. Получите детали подключения

**Bash:**
```bash
azd env get-values
```

**PowerShell:**
```powershell
azd env get-values
```

Отобразятся:
- `AZURE_OPENAI_ENDPOINT`: URL конечной точки Azure OpenAI
- `AZURE_OPENAI_KEY`: API ключ для аутентификации
- `AZURE_OPENAI_DEPLOYMENT`: имя модели для чата (gpt-5.2)
- `AZURE_OPENAI_EMBEDDING_DEPLOYMENT`: имя модели для встраивания

### 3. Запустите приложения локально

Команда `azd up` автоматически создаёт файл `.env` в корне проекта со всеми необходимыми переменными окружения.

**Рекомендуется:** Запустить все веб-приложения:

**Bash:**
```bash
# Из корневой директории
cd ../..
./start-all.sh
```

**PowerShell:**
```powershell
# Из корневого каталога
cd ../..
.\start-all.ps1
```

Или запустить отдельный модуль:

**Bash:**
```bash
# Пример: Запустить только модуль введения
cd ../01-introduction
./start.sh
```

**PowerShell:**
```powershell
# Пример: Запустить только модуль введения
cd ../01-introduction
.\start.ps1
```

Оба скрипта автоматически загружают переменные окружения из корневого `.env` файла, созданного командой `azd up`.

## Конфигурация

### Настройка развертываний моделей

Чтобы изменить развертывание моделей, отредактируйте файл `infra/main.bicep` и измените параметр `openAiDeployments`:

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

Доступные модели и версии: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Изменение регионов Azure

Чтобы развернуть в другом регионе, отредактируйте `infra/main.bicep`:

```bicep
param openAiLocation string = 'eastus2'  // or other GPT-5.2 region
```

Проверьте доступность GPT-5.2: https://learn.microsoft.com/azure/ai-services/openai/concepts/models#model-summary-table-and-region-availability

Чтобы обновить инфраструктуру после изменений в Bicep-файлах:

**Bash:**
```bash
# Перестроить ARM шаблон
az bicep build --file infra/main.bicep

# Предварительный просмотр изменений
azd provision --preview

# Применить изменения
azd provision
```

**PowerShell:**
```powershell
# Пересобрать ARM шаблон
az bicep build --file infra/main.bicep

# Просмотр изменений
azd provision --preview

# Применить изменения
azd provision
```


## Очистка

Чтобы удалить все ресурсы:

**Bash:**
```bash
# Удалить все ресурсы
azd down

# Удалить всё, включая окружение
azd down --purge
```

**PowerShell:**
```powershell
# Удалить все ресурсы
azd down

# Удалить все, включая окружение
azd down --purge
```

**Внимание**: это удалит все Azure ресурсы безвозвратно.

## Структура файлов

## Оптимизация затрат

### Разработка/тестирование
Для dev/test окружений можно снизить затраты:
- Используйте стандартный тариф (S0) для Azure OpenAI
- Установите меньшую пропускную способность (10K TPM вместо 20K) в `infra/core/ai/cognitiveservices.bicep`
- Удаляйте ресурсы, когда они не нужны: `azd down`

### Продакшн
Для продакшн:
- Увеличивайте мощность OpenAI в зависимости от нагрузки (50K+ TPM)
- Включайте зону избыточности для высокой доступности
- Реализуйте мониторинг и оповещения о тратах

### Оценка стоимости
- Azure OpenAI: оплата за токен (вход + выход)
- GPT-5.2: около $3-5 за 1 млн токенов (проверяйте актуальные цены)
- text-embedding-3-small: около $0.02 за 1 млн токенов

Калькулятор цен: https://azure.microsoft.com/pricing/calculator/

## Мониторинг

### Просмотр метрик Azure OpenAI

Войдите в Azure Portal → Ваш ресурс OpenAI → Метрики:
- Использование на основе токенов
- Количество HTTP-запросов
- Время ответа
- Активные токены

## Устранение неполадок

### Проблема: конфликт имени поддомена Azure OpenAI

**Сообщение об ошибке:**
```
ERROR CODE: CustomDomainInUse
message: "Please pick a different name. The subdomain name 'aoai-xxxxx' 
is not available as it's already used by a resource."
```

**Причина:**
Имя поддомена, сгенерированное из вашей подписки/окружения, уже используется, возможно, от предыдущего развертывания, которое не было полностью удалено.

**Решение:**
1. **Вариант 1 — используйте другое имя окружения:**
   
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

2. **Вариант 2 — ручное развертывание через Azure Portal:**
   - Перейдите в Azure Portal → Создать ресурс → Azure OpenAI
   - Выберите уникальное имя для ресурса
   - Разверните следующие модели:
     - **GPT-5.2**
     - **text-embedding-3-small** (для модулей RAG)
   - **Важно:** Запомните имена развертываний — они должны совпадать с конфигурацией `.env`
   - После развертывания получите конечную точку и API ключ в разделе "Keys and Endpoint"
   - Создайте `.env` файл в корне проекта с:

     **Пример файла `.env`:**
     ```bash
     AZURE_OPENAI_ENDPOINT=https://your-resource-name.openai.azure.com
     AZURE_OPENAI_API_KEY=your-api-key-here
     AZURE_OPENAI_DEPLOYMENT=gpt-5.2
     AZURE_OPENAI_EMBEDDING_DEPLOYMENT=text-embedding-3-small
     ```

**Руководство по именованию развертываний моделей:**
- Используйте простые и согласованные имена: `gpt-5.2`, `gpt-4o`, `text-embedding-3-small`
- Имена развертываний должны точно совпадать с тем, что указано в `.env`
- Распространённая ошибка — создание модели под одним именем, но использование другого в коде

### Проблема: GPT-5.2 недоступен в выбранном регионе

**Решение:**
- Выберите регион с доступом к GPT-5.2 (например, eastus2)
- Проверьте доступность: https://learn.microsoft.com/azure/ai-services/openai/concepts/models

### Проблема: недостаточная квота для развертывания

**Решение:**
1. Запросите увеличение квоты в Azure Portal
2. Или используйте меньшую пропускную способность в `main.bicep` (например, capacity: 10)

### Проблема: "Ресурс не найден" при локальном запуске

**Решение:**
1. Проверьте развертывание: `azd env get-values`
2. Убедитесь, что endpoint и ключ корректны
3. Проверьте, что группа ресурсов существует в Azure Portal

### Проблема: Ошибка аутентификации

**Решение:**
- Убедитесь, что `AZURE_OPENAI_API_KEY` установлен правильно
- Ключ должен быть 32-символьной шестнадцатеричной строкой
- При необходимости получите новый ключ в Azure Portal

### Ошибка развертывания

**Проблема:** `azd provision` завершается ошибками квоты или пропускной способности

**Решение:** 
1. Попробуйте другой регион — см. раздел [Изменение регионов Azure](../../../../01-introduction/infra)
2. Проверьте, что ваша подписка имеет квоту Azure OpenAI:

   **Bash:**
   ```bash
   az cognitiveservices account list-skus --location <your-region>
   ```
   
   **PowerShell:**
   ```powershell
   az cognitiveservices account list-skus --location <your-region>
   ```


### Приложение не подключается

**Проблема:** Java приложение выдает ошибки соединения

**Решение:**
1. Проверьте экспорт переменных окружения:

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

2. Проверьте правильность формата endpoint (должен быть `https://xxx.openai.azure.com`)
3. Убедитесь, что API ключ — это первичный или вторичный ключ из Azure Portal

**Проблема:** 401 Unauthorized от Azure OpenAI

**Решение:**
1. Получите новый API ключ в Azure Portal → Keys and Endpoint
2. Повторно экспортируйте переменную окружения `AZURE_OPENAI_API_KEY`
3. Убедитесь, что развертывания моделей завершены (проверьте в Azure Portal)

### Проблемы с производительностью

**Проблема:** Медленное время отклика

**Решение:**
1. Проверьте использование токенов и ограничения в метриках Azure Portal
2. Увеличьте пропускную способность TPM при необходимости
3. Рассмотрите повышение уровня сложности рассуждений (низкий/средний/высокий)

## Обновление инфраструктуры

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


## Рекомендации по безопасности

1. **Никогда не коммитьте API ключи в репозиторий** — используйте переменные окружения
2. **Используйте файлы .env локально** — добавьте `.env` в `.gitignore`
3. **Регулярно меняйте ключи** — создавайте новые в Azure Portal
4. **Ограничьте доступ** — используйте управление доступом Azure RBAC
5. **Следите за расходами** — настройте оповещения о расходах в Azure Portal

## Дополнительные ресурсы

- [Документация Azure OpenAI Service](https://learn.microsoft.com/azure/ai-services/openai/)
- [Документация модели GPT-5.2](https://learn.microsoft.com/azure/ai-services/openai/concepts/models#gpt-5)
- [Документация Azure Developer CLI](https://learn.microsoft.com/azure/developer/azure-developer-cli/)
- [Документация Bicep](https://learn.microsoft.com/azure/azure-resource-manager/bicep/)
- [Официальная интеграция LangChain4j OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai)

## Поддержка

При возникновении проблем:
1. Проверьте [раздел устранения неполадок](../../../../01-introduction/infra) выше
2. Проверьте состояние сервиса Azure OpenAI в Azure Portal
3. Создайте issue в репозитории

## Лицензия

См. основной файл [LICENSE](../../../../LICENSE) для деталей.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:
Этот документ был переведен с помощью сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, просим учитывать, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на исходном языке следует считать авторитетным источником. Для критически важной информации рекомендуется использовать профессиональный перевод, выполненный человеком. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования данного перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->