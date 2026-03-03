# Module 01: Aan de slag met LangChain4j

## Inhoudsopgave

- [Video Walkthrough](../../../01-introduction)
- [Wat Je Zal Leren](../../../01-introduction)
- [Vereisten](../../../01-introduction)
- [De Kern van het Probleem Begrijpen](../../../01-introduction)
- [Tokens Begrijpen](../../../01-introduction)
- [Hoe Geheugen Werkt](../../../01-introduction)
- [Hoe Dit LangChain4j Gebruikt](../../../01-introduction)
- [Azure OpenAI Infrastructuur Uitrollen](../../../01-introduction)
- [De Applicatie Lokaal Uitvoeren](../../../01-introduction)
- [De Applicatie Gebruiken](../../../01-introduction)
  - [Stateless Chat (Linker Paneel)](../../../01-introduction)
  - [Stateful Chat (Rechter Paneel)](../../../01-introduction)
- [Volgende Stappen](../../../01-introduction)

## Video Walkthrough

Bekijk deze live sessie die uitlegt hoe je aan de slag gaat met deze module:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Wat Je Zal Leren

In de quick start gebruikte je GitHub Models om prompts te versturen, tools aan te roepen, een RAG-pijplijn te bouwen en guardrails te testen. Die demo’s lieten zien wat mogelijk is — nu schakelen we over naar Azure OpenAI en GPT-5.2 en beginnen we productieachtige applicaties te bouwen. Deze module richt zich op conversationele AI die context onthoudt en toestand behoudt — de concepten die die quick start demo’s achter de schermen gebruikten maar niet uitlegden.

We gebruiken Azure OpenAI's GPT-5.2 door deze gids heen omdat zijn geavanceerde redeneervermogen het gedrag van verschillende patronen duidelijker maakt. Als je geheugen toevoegt, zie je het verschil scherp. Dit maakt het gemakkelijker te begrijpen wat elk onderdeel aan je applicatie bijdraagt.

Je bouwt één applicatie die beide patronen demonstreert:

**Stateless Chat** - Elke aanvraag is onafhankelijk. Het model heeft geen geheugen van eerdere berichten. Dit is het patroon dat je in de quick start gebruikte.

**Stateful Conversation** - Elke aanvraag bevat de gespreksgeschiedenis. Het model behoudt context over meerdere beurten. Dit is wat productieapplicaties vereisen.

## Vereisten

- Azure-abonnement met toegang tot Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opmerking:** Java, Maven, Azure CLI en Azure Developer CLI (azd) zijn vooraf geïnstalleerd in de meegeleverde devcontainer.

> **Opmerking:** Deze module gebruikt GPT-5.2 op Azure OpenAI. De uitrol is automatisch geconfigureerd via `azd up` - wijzig de modelnaam in de code niet.

## De Kern van het Probleem Begrijpen

Taalmodellen zijn stateless. Elke API-aanroep is onafhankelijk. Als je "Mijn naam is John" stuurt en daarna vraagt "Wat is mijn naam?", dan weet het model niet dat je jezelf zojuist hebt voorgesteld. Het behandelt elke aanvraag alsof het de eerste conversatie ooit is.

Dit is prima voor eenvoudige Q&A, maar nutteloos voor echte applicaties. Klantenservicebots moeten onthouden wat je hen vertelde. Persoonlijke assistenten hebben context nodig. Elke multi-turn gesprek vereist geheugen.

Het onderstaande diagram vergelijkt de twee benaderingen — links een stateless oproep die je naam vergeet; rechts een stateful oproep ondersteund door ChatMemory die het onthoudt.

<img src="../../../translated_images/nl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Het verschil tussen stateless (onafhankelijke oproepen) en stateful (contextbewuste) gesprekken*

## Tokens Begrijpen

Voordat je in gesprekken duikt, is het belangrijk tokens te begrijpen - de basiseenheden tekst die taalmodellen verwerken:

<img src="../../../translated_images/nl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Voorbeeld van hoe tekst wordt opgesplitst in tokens - "I love AI!" wordt 4 aparte verwerkingsunits*

Tokens zijn hoe AI-modellen tekst meten en verwerken. Woorden, interpunctie en zelfs spaties kunnen tokens zijn. Je model heeft een limiet van hoeveel tokens het tegelijk kan verwerken (400.000 voor GPT-5.2, met maximaal 272.000 inputtokens en 128.000 outputtokens). Tokens begrijpen helpt je om de lengte van gesprekken en kosten te beheren.

## Hoe Geheugen Werkt

Chatgeheugen lost het stateless probleem op door de gespreksgeschiedenis te bewaren. Voordat je je verzoek naar het model stuurt, voegt het framework relevante eerdere berichten toe. Wanneer je vraagt “Wat is mijn naam?”, verstuurt het systeem feitelijk de hele gespreksgeschiedenis, waardoor het model kan “zien” dat je eerder zei "Mijn naam is John."

LangChain4j biedt geheugenimplementaties die dit automatisch afhandelen. Je kiest hoeveel berichten behouden blijven en het framework beheert het contextvenster. Het onderstaande diagram toont hoe MessageWindowChatMemory een schuifvenster van recente berichten behoudt.

<img src="../../../translated_images/nl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory behoudt een schuifvenster van recente berichten door automatisch oude te verwijderen*

## Hoe Dit LangChain4j Gebruikt

Deze module breidt de quick start uit door Spring Boot te integreren en gespreksgeheugen toe te voegen. Zo passen de onderdelen samen:

**Dependencies** - Voeg twee LangChain4j bibliotheken toe:

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

**Chat Model** - Configureer Azure OpenAI als een Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

De builder leest credentials uit omgevingsvariabelen die door `azd up` zijn gezet. Het instellen van `baseUrl` naar je Azure eindpunt maakt de OpenAI client compatibel met Azure OpenAI.

**Gespreksgeheugen** - Houd de chatgeschiedenis bij met MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Maak geheugen aan met `withMaxMessages(10)` om de laatste 10 berichten vast te houden. Voeg gebruikers- en AI-berichten toe met getypte wrappers: `UserMessage.from(text)` en `AiMessage.from(text)`. Haal de geschiedenis op met `memory.messages()` en stuur die naar het model. De service slaat voor elke gesprek-ID aparte geheugeninstellingen op, zodat meerdere gebruikers tegelijk kunnen chatten.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) en vraag:
> - "Hoe bepaalt MessageWindowChatMemory welke berichten verwijderd worden als het venster vol is?"
> - "Kan ik aangepaste geheugentoegang implementeren met een database in plaats van in-memory?"
> - "Hoe zou ik samenvatting toevoegen om oude gespreksgeschiedenis te comprimeren?"

Het stateless chat endpoint slaat geheugen helemaal over — gewoon `chatModel.chat(prompt)` zoals in de quick start. Het stateful endpoint voegt berichten toe aan geheugen, haalt geschiedenis op en voegt die context toe aan elke aanvraag. Zelfde modelconfiguratie, andere patronen.

## Azure OpenAI Infrastructuur Uitrollen

**Bash:**
```bash
cd 01-introduction
azd up  # Selecteer abonnement en locatie (aanbevolen: eastus2)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecteer abonnement en locatie (aanbevolen is eastus2)
```

> **Opmerking:** Als je een time-out fout tegenkomt (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), voer dan gewoon `azd up` opnieuw uit. Azure resources kunnen nog bezig zijn met provisie, en een retry zorgt dat de uitrol voltooid wordt zodra resources een eindtoestand bereiken.

Dit zal:
1. Azure OpenAI resource uitrollen met GPT-5.2 en text-embedding-3-small modellen
2. Automatisch een `.env` bestand genereren in de projectroot met credentials
3. Alle benodigde omgevingsvariabelen instellen

**Problemen met uitrol?** Zie de [Infrastructure README](infra/README.md) voor gedetailleerde troubleshooting waaronder subdomeinnaam conflicten, handmatige Azure Portal uitrolstappen en modelconfiguratie-advies.

**Controleer of de uitrol gelukt is:**

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, enz. weergeven.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Zou AZURE_OPENAI_ENDPOINT, API_KEY, enz. moeten laten zien.
```

> **Opmerking:** Het commando `azd up` genereert automatisch het `.env` bestand. Als je dat later moet bijwerken, kun je het `.env` bestand handmatig aanpassen of opnieuw laten genereren met:
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

## De Applicatie Lokaal Uitvoeren

**Controleer uitrol:**

Zorg dat het `.env` bestand in de hoofdmap staat met Azure credentials. Voer dit uit vanuit de moduledirectory (`01-introduction/`):

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```

**Start de applicaties:**

**Optie 1: Gebruik Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De devcontainer bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt voor het beheren van alle Spring Boot applicaties. Je vindt het in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot icoon).

Vanuit het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de workspace zien
- Applicaties starten/stoppen met één klik
- Applicatielogs in real-time bekijken
- Applicatiestatus monitoren

Klik simpelweg op de afspeelknop naast "introduction" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*Het Spring Boot Dashboard in VS Code — start, stop en monitor alle modules vanaf één plek*

**Optie 2: Gebruik shell scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de hoofdmap
.\start-all.ps1
```

Of start alleen deze module:

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

Beide scripts laden automatisch omgevingvariabelen uit het root `.env` bestand en bouwen de JARs als ze nog niet bestaan.

> **Opmerking:** Als je liever alle modules handmatig bouwt voordat je start:
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

Open http://localhost:8080 in je browser.

**Om te stoppen:**

**Bash:**
```bash
./stop.sh  # Alleen deze module
# Of
cd .. && ./stop-all.sh  # Alle modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Alleen deze module
# Of
cd ..; .\stop-all.ps1  # Alle modules
```

## De Applicatie Gebruiken

De applicatie biedt een webinterface met twee chatimplementaties naast elkaar.

<img src="../../../translated_images/nl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard met zowel Simple Chat (stateless) als Conversational Chat (stateful) opties*

### Stateless Chat (Linker Paneel)

Probeer dit eerst. Vraag "Mijn naam is John" en vraag dan direct "Wat is mijn naam?" Het model zal het niet herinneren omdat elk bericht onafhankelijk is. Dit toont het kernprobleem van eenvoudige integratie met taalmodellen - geen context in het gesprek.

<img src="../../../translated_images/nl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI herinnert zich je naam niet van het vorige bericht*

### Stateful Chat (Rechter Paneel)

Probeer nu dezelfde reeks hier. Vraag "Mijn naam is John" en daarna "Wat is mijn naam?" Dit keer onthoudt het. Het verschil is MessageWindowChatMemory — het onderhoudt de gespreksgeschiedenis en voegt die toe aan elke aanvraag. Zo werkt productionele conversatie-AI.

<img src="../../../translated_images/nl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI herinnert zich je naam van eerder in het gesprek*

Beide panelen gebruiken hetzelfde GPT-5.2 model. Het enige verschil is geheugen. Dit maakt duidelijk wat geheugen aan je applicatie toevoegt en waarom het essentieel is voor echte toepassingen.

## Volgende Stappen

**Volgende Module:** [02-prompt-engineering - Prompt Engineering met GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigatie:** [← Vorige: Module 00 - Quick Start](../00-quick-start/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->