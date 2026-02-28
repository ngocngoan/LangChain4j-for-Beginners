# Модул 01: Почетак рада са LangChain4j

## Садржај

- [Видео водич](../../../01-introduction)
- [Шта ћете научити](../../../01-introduction)
- [Предуслови](../../../01-introduction)
- [Разумијевање основног проблема](../../../01-introduction)
- [Разумијевање токена](../../../01-introduction)
- [Како функционише меморија](../../../01-introduction)
- [Како ово користи LangChain4j](../../../01-introduction)
- [Деплојмент Azure OpenAI инфраструктуре](../../../01-introduction)
- [Покретање апликације локално](../../../01-introduction)
- [Коришћење апликације](../../../01-introduction)
  - [Чет без меморије (леви панел)](../../../01-introduction)
  - [Чет са меморијом (десни панел)](../../../01-introduction)
- [Следећи кораци](../../../01-introduction)

## Видео водич

Погледајте овај уживо пренос који објашњава како започети рад са овим модулом:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Шта ћете научити

Ако сте завршили брзи почетак, видели сте како се шаљу упити и добијају одговори. То је основа, али праве апликације захтевају више. Овај модул вас учи како да изградите разговорни АИ који памти контекст и одржава стање - разлика између једнократне демонстрације и апликације спремне за производњу.

Користићемо GPT-5.2 из Azure OpenAI током овог водича јер његове напредне способности резоновања јасније показују понашање различитих узорака. Када додате меморију, јасно ћете видети разлику. То олакшава разумевање шта сваки део доноси вашој апликацији.

Изградићете једну апликацију која показује оба узорка:

**Чет без меморије** - Свакa захтевa је независнa. Модел нема меморију претходних порука. Ово је узорак који сте користили у брзом почетку.

**Разговор са меморијом** - Свакa захтевa укључује историју разговора. Модел одржава контекст кроз више корака. Ово је оно што захтевају апликације у производњи.

## Предуслови

- Azure претплата са приступом Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Напомена:** Java, Maven, Azure CLI и Azure Developer CLI (azd) су пред-инсталирани у пруженом девконтејнеру.

> **Напомена:** Овај модул користи GPT-5.2 на Azure OpenAI. Деплојмент се аутоматски конфигурише преко `azd up` - немојте мењати име модела у коду.

## Разумијевање основног проблема

Језички модели су без меморије. Свако API позив је независан. Ако пошаљете „Зовем се Џон“ а онда питате „Како се зовем?“, модел нема појма да сте се управо представили. Третира сваки захтев као да је то први разговор који икада водите.

Ово је у реду за једноставна питања и одговоре, али бескорисно за праве апликације. Ботови за корисничку службу морају памтити шта сте им рекли. Лични асистенти требају контекст. Свако више-корисничко вођење разговора захтева меморију.

<img src="../../../translated_images/sr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Разлика између разговора без меморије (независни позиви) и са меморијом (са контекстом)*

## Разумијевање токена

Пре уласка у разговоре, важно је разумети токене - основне јединице текста које језички модели обрађују:

<img src="../../../translated_images/sr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Пример како се текст дели на токене - "I love AI!" постаје 4 одвојене јединице обраде*

Токени су начин на који АИ модели мере и обрађују текст. Речи, интерпункција, па чак и размаци могу бити токени. Ваш модел има ограничење колико токена може обрадити одједном (400.000 за GPT-5.2, са до 272.000 улазних и 128.000 излазних токена). Разумевање токена помаже да управљате дужином разговора и трошковима.

## Како функционише меморија

Чет меморија решава проблем без меморије одржавањем историје разговора. Пре слања вашег захтева моделу, оквир додаје релевантне претходне поруке. Када питате „Како се зовем?“, систем у ствари шаље целу историју разговора, омогућавајући моделу да види да сте раније рекли „Зовем се Џон.“

LangChain4j пружа имплементације меморије које ово аутоматски управљају. Ви бирајте колико порука желите да задржите, а оквир управља прозором контекста.

<img src="../../../translated_images/sr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory одржава покретни прозор најновијих порука, аутоматски одбацујући старе*

## Како ово користи LangChain4j

Овај модул проширује брзи почетак интеграцијом Spring Boot и додавањем меморије разговора. Ево како се делови повезују:

**Зависности** - Додајте две LangChain4j библиотеке:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**Чет модел** - Конфигуришите Azure OpenAI као Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder чита креденцијале из промењивих окружења подешених помоћу `azd up`. Постављање `baseUrl` на ваш Azure крајњи тачку чини OpenAI клијента компатибилним са Azure OpenAI.

**Меморија разговора** - Пратите историју чата са MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Креирајте меморију са `withMaxMessages(10)` да задржи последњих 10 порука. Додајте корисничке и АИ поруке са типизованим омотачима: `UserMessage.from(text)` и `AiMessage.from(text)`. Преузмите историју помоћу `memory.messages()` и пошаљите је моделу. Сервис чува посебне инстанце меморије по ИД-у разговора, омогућавајући више корисника да истовремено четују.

> **🤖 Испробајте са [GitHub Copilot](https://github.com/features/copilot) четом:** Отворите [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) и питајте:
> - „Како MessageWindowChatMemory одлучује које поруке да одбаци када је прозор пун?“
> - „Могу ли да имплементирам прилагођено складиштење меморије користећи базу података уместо меморије у RAM?“
> - „Како да додам сумирање да компримујем стару историју разговора?“

Endpoints чет без меморије у потпуности прескаче меморију - само `chatModel.chat(prompt)` као у брзом почетку. Stateful endpoint додаје поруке меморији, преузима историју и укључује тај контекст са сваким захтевом. Иста конфигурација модела, различити узорци.

## Деплојмент Azure OpenAI инфраструктуре

**Bash:**
```bash
cd 01-introduction
azd up  # Изаберите претплату и локацију (препоручује се eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Изаберите претплату и локацију (препоручује се eastus2)
```

> **Напомена:** Ако наиђете на timeout грешку (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), једноставно поново покрените `azd up`. Azure ресурси могу још увек бити у фази постављања у позадини, а поновни покушај омогућава завршетак деплојмента када ресурси достигну терминално стање.

Ово ће:
1. Деплојтовати Azure OpenAI ресурс са GPT-5.2 и text-embedding-3-small моделима
2. Аутоматски генерисати `.env` фајл у корену пројекта са креденцијалима
3. Подесити све потребне променљиве окружења

**Имате проблеме са деплојментом?** Погледајте [README инфраструктуре](infra/README.md) за детаљно решавање проблема укључујући конфликте имена поддомене, ручне кораке деплојмента кроз Azure портал, и смернице за конфигурацију модела.

**Потврдите да је деплојмент успео:**

**Bash:**
```bash
cat ../.env  # Требало би да прикаже AZURE_OPENAI_ENDPOINT, API_KEY, итд.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Требало би да прикаже AZURE_OPENAI_ENDPOINT, API_KEY, итд.
```

> **Напомена:** Команда `azd up` аутоматски генерише `.env` фајл. Ако вам треба да га касније ажурирате, можете га уредити ручно или поново генерисати тако што ћете покренути:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## Покрените апликацију локално

**Проверите деплојмент:**

Уверите се да `.env` фајл постоји у коренском директоријуму са Azure креденцијалима:

**Bash:**
```bash
cat ../.env  # Требало би да прикаже AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Треба да приказује AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Покрените апликације:**

**Опција 1: Коришћење Spring Boot Dashboard-а (Препоручено за VS Code кориснике)**

Дев контејнер укључује Spring Boot Dashboard екстензију која пружа визуелни интерфејс за управљање свим Spring Boot апликацијама. Можете је наћи на Activity Bar-у са леве стране VS Code-а (потражите Spring Boot икону).

Из Spring Boot Dashboard-а можете:
- Видети све доступне Spring Boot апликације у радном простору
- Покретати/заустављати апликације једним кликом
- Прати логове апликације у реалном времену
- Контролисати статус апликације

Једноставно кликните на дугме за покретање поред „introduction“ да бисте покренули овај модул, или покрените све модуле одједном.

<img src="../../../translated_images/sr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Опција 2: Коришћење shell скрипти**

Покрените све веб апликације (модули 01-04):

**Bash:**
```bash
cd ..  # Из коренског директоријума
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Из коренског директоријума
.\start-all.ps1
```

Или покрените само овај модул:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

Обе скрипте аутоматски учитавају променљиве окружења из `.env` фајла у корену и саставиће ЈАР ако не постоји.

> **Напомена:** Ако више волите да ручно саставите све модуле пре покретања:
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

Отворите http://localhost:8080 у вашем прегледачу.

**За заустављање:**

**Bash:**
```bash
./stop.sh  # Само овај модул
# Или
cd .. && ./stop-all.sh  # Сви модули
```

**PowerShell:**
```powershell
.\stop.ps1  # Само овај модул
# Или
cd ..; .\stop-all.ps1  # Сви модули
```

## Коришћење апликације

Апликација пружа веб интерфејс са две имплементације ћаскања једна до друге.

<img src="../../../translated_images/sr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Контролна табла која показује једноставни чет (без меморије) и разговорни чет (са меморијом)*

### Чет без меморије (леви панел)

Прво испробајте ово. Реците „Зовем се Џон“ па одмах „Како се зовем?“ Модел неће запамтити јер је свака порука независна. Ово демонстрира основни проблем интеграције језичког модела - нема контекст разговора.

<img src="../../../translated_images/sr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*АИ не памти ваше име из претходне поруке*

### Чет са меморијом (десни панел)

Сада пробајте исти низ овде. Реците „Зовем се Џон“ па „Како се зовем?“ Овог пута памти. Разлика је MessageWindowChatMemory - одржава историју разговора и укључује је сваки пут. Ово је начин на који ради разговорни АИ у производњи.

<img src="../../../translated_images/sr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*АИ памти ваше име из ранијег дела разговора*

Оба панела користе исти GPT-5.2 модел. Једина разлика је меморија. Ово јасно показује шта меморија доноси вашој апликацији и зашто је неопходна за реалне случајеве коришћења.

## Следећи кораци

**Следећи модул:** [02-prompt-engineering - Инжењеринг упита са GPT-5.2](../02-prompt-engineering/README.md)

---

**Навигација:** [← Претходно: Модул 00 - Блок-почетак](../00-quick-start/README.md) | [Натраг на главну страницу](../README.md) | [Следеће: Модул 02 - Инжењеринг упита →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Одрицање од одговорности**:
Овај документ је преведен коришћењем АИ услуге за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако настојимо да превод буде што прецизнији, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Изворни документ на његовом матичном језику треба сматрати ауторитетним извором. За критичне информације препоручује се професионални превод од стране људи. Нисмо одговорни за било каква неспоразума или погрешна тумачења настала употребом овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->