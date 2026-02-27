# Module 01: Aan de slag met LangChain4j

## Inhoudsopgave

- [Video Walkthrough](../../../01-introduction)
- [Wat je zult leren](../../../01-introduction)
- [Vereisten](../../../01-introduction)
- [Begrijpen van het kernprobleem](../../../01-introduction)
- [Begrijpen van tokens](../../../01-introduction)
- [Hoe geheugen werkt](../../../01-introduction)
- [Hoe dit LangChain4j gebruikt](../../../01-introduction)
- [Implementatie van Azure OpenAI-infrastructuur](../../../01-introduction)
- [De applicatie lokaal uitvoeren](../../../01-introduction)
- [De applicatie gebruiken](../../../01-introduction)
  - [Stateless chat (linkerpaneel)](../../../01-introduction)
  - [Stateful chat (rechterpaneel)](../../../01-introduction)
- [Volgende stappen](../../../01-introduction)

## Video Walkthrough

Bekijk deze live sessie die uitlegt hoe je begint met deze module: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## Wat je zult leren

Als je de quick start hebt voltooid, heb je gezien hoe je prompts stuurt en antwoorden krijgt. Dat is de basis, maar echte applicaties hebben meer nodig. Deze module leert je hoe je een conversatie-AI bouwt die context onthoudt en status bewaart - het verschil tussen een eenmalige demo en een productieklare applicatie.

We gebruiken door deze gids heen Azure OpenAI's GPT-5.2 omdat de geavanceerde redeneervaardigheden het gedrag van verschillende patronen duidelijker maken. Met geheugen toegevoegd zie je het verschil helder. Dit maakt het makkelijker te begrijpen wat elk component aan je applicatie bijdraagt.

Je bouwt één applicatie die beide patronen demonstreert:

**Stateless Chat** - Elke aanvraag is onafhankelijk. Het model onthoudt geen vorige berichten. Dit is het patroon dat je in de quick start gebruikte.

**Stateful Conversation** - Elke aanvraag bevat de gespreksgeschiedenis. Het model behoudt context over meerdere beurten. Dit is wat productieapplicaties vereisen.

## Vereisten

- Azure-abonnement met toegang tot Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opmerking:** Java, Maven, Azure CLI en Azure Developer CLI (azd) zijn vooraf geïnstalleerd in de meegeleverde devcontainer.

> **Opmerking:** Deze module gebruikt GPT-5.2 op Azure OpenAI. De deployment wordt automatisch geconfigureerd via `azd up` - wijzig de modelnaam niet in de code.

## Begrijpen van het kernprobleem

Taalmodellen zijn stateless. Elke API-aanroep is onafhankelijk. Als je "Mijn naam is John" stuurt en daarna vraagt "Wat is mijn naam?", weet het model niet dat je jezelf zojuist hebt voorgesteld. Het behandelt elk verzoek alsof het de eerste gesprek is dat je ooit hebt gehad.

Dit is prima voor eenvoudige Q&A maar nutteloos voor echte applicaties. Klantenservicebots moeten onthouden wat je hen vertelde. Persoonlijke assistenten hebben context nodig. Elke meervoudige beurtgesprek vereist geheugen.

<img src="../../../translated_images/nl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Het verschil tussen stateless (onafhankelijke oproepen) en stateful (contextbewuste) gesprekken*

## Begrijpen van tokens

Voordat je dieper ingaat op gesprekken, is het belangrijk tokens te begrijpen - de basiseenheden van tekst die taalmodellen verwerken:

<img src="../../../translated_images/nl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Voorbeeld van hoe tekst wordt opgesplitst in tokens - "I love AI!" wordt 4 losse verwerkingsunits*

Tokens zijn hoe AI-modellen tekst meten en verwerken. Woorden, leestekens en zelfs spaties kunnen tokens zijn. Je model heeft een limiet aan hoeveel tokens het tegelijk kan verwerken (400.000 voor GPT-5.2, met tot 272.000 input tokens en 128.000 output tokens). Begrip van tokens helpt je de lengte van het gesprek en de kosten te beheren.

## Hoe geheugen werkt

Chatgeheugen lost het stateless probleem op door de gespreksgeschiedenis te bewaren. Voordat je je verzoek naar het model stuurt, voegt het framework relevante eerdere berichten toe. Als je vraagt "Wat is mijn naam?", stuurt het systeem eigenlijk de hele gespreksgeschiedenis mee, zodat het model ziet dat je eerder zei "Mijn naam is John."

LangChain4j biedt geheugenimplementaties die dit automatisch afhandelen. Je kiest hoeveel berichten bewaard worden en het framework beheert het contextvenster.

<img src="../../../translated_images/nl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory onderhoudt een schuivend venster met recente berichten en verwijdert automatisch oude berichten*

## Hoe dit LangChain4j gebruikt

Deze module bouwt voort op de quick start door integratie van Spring Boot en toevoeging van conversatiegeheugen. Hier zie je hoe de onderdelen samenwerken:

**Afhankelijkheden** - Voeg twee LangChain4j bibliotheken toe:

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

De builder leest inloggegevens van omgevingsvariabelen die door `azd up` zijn ingesteld. Door `baseUrl` in te stellen op je Azure endpoint werkt de OpenAI client met Azure OpenAI.

**Conversatiegeheugen** - Houd chatgeschiedenis bij met MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Maak geheugen aan met `withMaxMessages(10)` om de laatste 10 berichten te bewaren. Voeg gebruikers- en AI-berichten toe met getypte wrappers: `UserMessage.from(text)` en `AiMessage.from(text)`. Haal geschiedenis op met `memory.messages()` en stuur dit naar het model. De service slaat aparte geheugeninstanties per gesprek-ID op, zodat meerdere gebruikers tegelijkertijd kunnen chatten.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) en vraag:
> - "Hoe bepaalt MessageWindowChatMemory welke berichten worden verwijderd als het venster vol is?"
> - "Kan ik aangepaste geheugenopslag implementeren met een database in plaats van in geheugen?"
> - "Hoe zou ik samenvatten toevoegen om oude gespreksgeschiedenis te comprimeren?"

De stateless chat endpoint overslaat geheugen volledig - gewoon `chatModel.chat(prompt)` zoals in de quick start. De stateful endpoint voegt berichten toe aan geheugen, haalt geschiedenis op en voegt die context bij elk verzoek toe. Zelfde modelconfiguratie, verschillende patronen.

## Implementatie van Azure OpenAI-infrastructuur

**Bash:**
```bash
cd 01-introduction
azd up  # Selecteer abonnement en locatie (eastus2 aanbevolen)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # Selecteer abonnement en locatie (eastus2 aanbevolen)
```

> **Opmerking:** Als je een timeout foutmelding krijgt (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), voer dan simpelweg `azd up` opnieuw uit. Azure-resources kunnen nog steeds worden ingericht op de achtergrond, en opnieuw proberen laat de deployment voltooien zodra resources een eindtoestand bereiken.

Dit zal:
1. Azure OpenAI-resource implementeren met GPT-5.2 en text-embedding-3-small modellen
2. Automatisch `.env` bestand in de projectroot aanmaken met credentials
3. Alle benodigde omgevingsvariabelen instellen

**Problemen met deployment?** Zie de [Infrastructure README](infra/README.md) voor gedetailleerde probleemoplossing inclusief subdomeinnaam conflicten, handmatige deployment stappen via Azure Portal en modelconfiguratie advies.

**Controleer of deployment geslaagd is:**

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, enz. tonen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, enz. weergeven.
```

> **Opmerking:** Het `azd up` commando genereert automatisch het `.env` bestand. Als je dit later wilt bijwerken, kun je het `.env` bestand handmatig aanpassen of het opnieuw genereren met:
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

## De applicatie lokaal uitvoeren

**Controleer deployment:**

Zorg dat het `.env` bestand in de rootmap met Azure-credentials aanwezig is:

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**Start de applicaties:**

**Optie 1: Gebruik Spring Boot Dashboard (aanbevolen voor VS Code gebruikers)**

De devcontainer bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt het in de Activity Bar aan de linkerkant van VS Code (zoek het Spring Boot icoon).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de workspace zien
- Applicaties starten/stoppen met één klik
- Applicatielogs in realtime bekijken
- De status van applicaties monitoren

Klik eenvoudig op de afspeelknop naast "introduction" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand en bouwen de JARs indien deze nog niet bestaan.

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

## De applicatie gebruiken

De applicatie biedt een webinterface met twee chatimplementaties naast elkaar.

<img src="../../../translated_images/nl/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard toont zowel Simple Chat (stateless) als Conversational Chat (stateful) opties*

### Stateless Chat (linkerpaneel)

Probeer dit eerst. Vraag "Mijn naam is John" en vraag dan direct "Wat is mijn naam?" Het model zal het niet onthouden omdat elk bericht onafhankelijk is. Dit demonstreert het kernprobleem van basis taalmodelintegratie - geen gesprekcontext.

<img src="../../../translated_images/nl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI onthoudt je naam niet van het vorige bericht*

### Stateful Chat (rechterpaneel)

Probeer nu dezelfde volgorde hier. Vraag "Mijn naam is John" en daarna "Wat is mijn naam?" Deze keer onthoudt het. Het verschil is MessageWindowChatMemory - het onderhoudt de gespreksgeschiedenis en voegt die bij elk verzoek toe. Zo werkt conversatie-AI in productie.

<img src="../../../translated_images/nl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI onthoudt je naam van eerder in het gesprek*

Beide panelen gebruiken hetzelfde GPT-5.2 model. Het enige verschil is het geheugen. Dit maakt duidelijk wat geheugen brengt aan je applicatie en waarom het essentieel is voor echte use cases.

## Volgende stappen

**Volgende Module:** [02-prompt-engineering - Prompt Engineering met GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigatie:** [← Vorige: Module 00 - Quick Start](../00-quick-start/README.md) | [Terug naar Hoofdmenu](../README.md) | [Volgende: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vrijwaring**:  
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal dient als de gezaghebbende bron te worden beschouwd. Voor cruciale informatie wordt een professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties voortvloeiend uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->