# Module 01: Aan de slag met LangChain4j

## Inhoudsopgave

- [Video Walkthrough](../../../01-introduction)
- [Wat je zult leren](../../../01-introduction)
- [Vereisten](../../../01-introduction)
- [Het begrijpen van het kernprobleem](../../../01-introduction)
- [Begrijpen van tokens](../../../01-introduction)
- [Hoe geheugen werkt](../../../01-introduction)
- [Hoe dit LangChain4j gebruikt](../../../01-introduction)
- [Azure OpenAI-infrastructuur implementeren](../../../01-introduction)
- [De applicatie lokaal uitvoeren](../../../01-introduction)
- [De applicatie gebruiken](../../../01-introduction)
  - [Zonder status chat (linkerpaneel)](../../../01-introduction)
  - [Met status chat (rechterpaneel)](../../../01-introduction)
- [Volgende stappen](../../../01-introduction)

## Video Walkthrough

Bekijk deze live sessie die uitlegt hoe je aan de slag gaat met deze module:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## Wat je zult leren

Als je de snelle start voltooid hebt, heb je gezien hoe je prompts kunt versturen en reacties kunt ontvangen. Dat is de basis, maar echte toepassingen hebben meer nodig. Deze module leert je hoe je conversationele AI bouwt die context onthoudt en de status bijhoudt - het verschil tussen een eenmalige demo en een productieklare applicatie.

We gebruiken Azure OpenAI's GPT-5.2 door deze gids heen omdat de geavanceerde redeneercapaciteiten het gedrag van verschillende patronen duidelijker maken. Wanneer je geheugen toevoegt, zie je het verschil duidelijk. Dit maakt het makkelijker om te begrijpen wat elk onderdeel aan je applicatie toevoegt.

Je bouwt één applicatie die beide patronen demonstreert:

**Zonder status chat** - Elke aanvraag is onafhankelijk. Het model heeft geen geheugen van eerdere berichten. Dit is het patroon dat je gebruikte in de snelle start.

**Met status gesprek** - Elke aanvraag bevat de gespreksgeschiedenis. Het model behoudt context over meerdere beurten heen. Dit is wat productie-applicaties vereisen.

## Vereisten

- Azure abonnement met toegang tot Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opmerking:** Java, Maven, Azure CLI en Azure Developer CLI (azd) zijn voorgeïnstalleerd in de meegeleverde devcontainer.

> **Opmerking:** Deze module gebruikt GPT-5.2 op Azure OpenAI. De implementatie wordt automatisch geconfigureerd via `azd up` - wijzig de modelnaam niet in de code.

## Het begrijpen van het kernprobleem

Taalmodellen zijn zonder status. Elke API-aanroep is onafhankelijk. Als je "Mijn naam is John" stuurt en daarna vraagt "Wat is mijn naam?", heeft het model geen idee dat je je net hebt voorgesteld. Het behandelt elke aanvraag alsof het de eerste keer is dat je ooit een gesprek hebt.

Dit is prima voor eenvoudige Q&A maar nutteloos voor echte toepassingen. Klantenservicebots moeten onthouden wat je zei. Persoonlijke assistenten hebben context nodig. Elk gesprek met meerdere beurten vereist geheugen.

<img src="../../../translated_images/nl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Het verschil tussen stateloze (onafhankelijke oproepen) en statige (contextbewuste) gesprekken*

## Begrijpen van tokens

Voordat we in gesprekken duiken, is het belangrijk om tokens te begrijpen - de basis eenheden van tekst die taalmodellen verwerken:

<img src="../../../translated_images/nl/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*Voorbeeld van hoe tekst wordt opgesplitst in tokens - "I love AI!" wordt 4 afzonderlijke verwerkingseenheden*

Tokens zijn hoe AI-modellen tekst meten en verwerken. Woorden, leestekens en zelfs spaties kunnen tokens zijn. Je model heeft een limiet aan het aantal tokens dat het tegelijk kan verwerken (400.000 voor GPT-5.2, met maximaal 272.000 inputtokens en 128.000 outputtokens). Tokens begrijpen helpt je om de lengte van een gesprek en kosten te beheren.

## Hoe geheugen werkt

Chatgeheugen lost het stateloos probleem op door de gespreksgeschiedenis bij te houden. Voordat je jouw verzoek naar het model stuurt, voegt het framework relevante eerdere berichten toe. Als je vraagt "Wat is mijn naam?", stuurt het systeem eigenlijk de hele gespreksgeschiedenis mee, waardoor het model kan zien dat je eerder zei "Mijn naam is John."

LangChain4j levert geheugenimplementaties die dit automatisch afhandelen. Je kiest hoeveel berichten je wilt bewaren en het framework beheert het contextvenster.

<img src="../../../translated_images/nl/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory houdt een zogenaamd “sliding window” bij van recente berichten en verwijdert automatisch oude berichten*

## Hoe dit LangChain4j gebruikt

Deze module bouwt verder op de snelle start door Spring Boot te integreren en gesprekgeheugen toe te voegen. Zo passen de onderdelen samen:

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

**Chatmodel** - Configureer Azure OpenAI als een Spring bean ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

De builder leest inloggegevens uit omgevingsvariabelen die door `azd up` zijn ingesteld. Door `baseUrl` te zetten op je Azure endpoint werkt de OpenAI client met Azure OpenAI.

**Gesprekgeheugen** - Houd gespreksgeschiedenis bij met MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Maak geheugen aan met `withMaxMessages(10)` om de laatste 10 berichten te bewaren. Voeg berichten van gebruiker en AI toe met getypte wrappers: `UserMessage.from(text)` en `AiMessage.from(text)`. Haal geschiedenis op met `memory.messages()` en stuur die naar het model. De service slaat aparte geheugeninstanties op per gesprek-ID, zodat meerdere gebruikers tegelijk kunnen chatten.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) en vraag:
> - "Hoe bepaalt MessageWindowChatMemory welke berichten worden verwijderd wanneer het venster vol is?"
> - "Kan ik een eigen geheugenopslag implementeren met een database in plaats van in het geheugen?"
> - "Hoe zou ik samenvatting toevoegen om oude gespreksgeschiedenis te comprimeren?"

Het stateloze chatendpoint slaat geheugen volledig over - alleen `chatModel.chat(prompt)` zoals in de snelle start. Het statige endpoint voegt berichten toe aan het geheugen, haalt geschiedenis op, en voegt die context toe aan elk verzoek. Zelfde modelconfiguratie, andere patronen.

## Azure OpenAI-infrastructuur implementeren

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

> **Opmerking:** Als je een timeout-fout krijgt (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), voer dan gewoon nogmaals `azd up` uit. Azure-resources kunnen nog in provisioning zijn op de achtergrond en opnieuw proberen zorgt ervoor dat de implementatie voltooid wordt zodra de resources een eindtoestand bereiken.

Dit zal:
1. Azure OpenAI-resource implementeren met GPT-5.2 en text-embedding-3-small modellen
2. Automatisch `.env` bestand aanmaken in de projectroot met inloggegevens
3. Alle benodigde omgevingsvariabelen instellen

**Problemen met implementatie?** Zie de [Infrastructure README](infra/README.md) voor gedetailleerde oplossingen, inclusief conflicten met subdomeinnamen, handmatige Azure Portal implementatiestappen en modelconfiguratieadvies.

**Controleer of de implementatie gelukt is:**

**Bash:**
```bash
cat ../.env  # Zou AZURE_OPENAI_ENDPOINT, API_KEY, enz. moeten tonen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, enz. tonen.
```

> **Opmerking:** Het `azd up` commando genereert automatisch het `.env` bestand. Als je het later wilt bijwerken, kun je het `.env` bestand handmatig aanpassen of opnieuw genereren door:
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

**Controleer implementatie:**

Zorg dat het `.env` bestand bestaat in de hoofddirectory met Azure-inloggegevens:

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

De devcontainer bevat de Spring Boot Dashboard extensie, die een visuele interface biedt om alle Spring Boot applicaties te beheren. Je vindt het in de Activity Bar aan de linkerkant van VS Code (zoek het Spring Boot icoon).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot applicaties in de werkomgeving zien
- Applicaties starten/stoppen met één klik
- Applicatielogs real-time bekijken
- Applicatiestatus monitoren

Klik gewoon op de play-knop naast "introduction" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik shellscripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanuit de hoofdmap
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Vanuit de hoofddirectory
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

Beide scripts laden automatisch omgevingsvariabelen uit het root `.env` bestand en bouwen de JAR's als ze nog niet bestaan.

> **Opmerking:** Als je liever eerst alle modules handmatig bouwt voordat je start:
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

*Dashboard toont zowel Eenvoudige Chat (stateloos) als Gespreks-chat (statig) opties*

### Zonder status chat (linkerpaneel)

Probeer dit eerst. Vraag "Mijn naam is John" en vraag direct daarna "Wat is mijn naam?" Het model zal het niet onthouden omdat elk bericht onafhankelijk is. Dit demonstreert het kernprobleem met basis integratie van taalmodellen - geen context in het gesprek.

<img src="../../../translated_images/nl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI onthoudt je naam niet van het vorige bericht*

### Met status chat (rechterpaneel)

Probeer hier nu dezelfde volgorde. Vraag "Mijn naam is John" en daarna "Wat is mijn naam?" Dit keer onthoudt het het wel. Het verschil is MessageWindowChatMemory - dat houdt de gespreksgeschiedenis bij en voegt die toe aan elk verzoek. Dit is hoe productie-gespreks-AI werkt.

<img src="../../../translated_images/nl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI onthoudt je naam van eerder in het gesprek*

Beide panelen gebruiken hetzelfde GPT-5.2 model. Het enige verschil is geheugen. Dit maakt duidelijk wat geheugen toevoegt aan je applicatie en waarom het essentieel is voor echte gebruikssituaties.

## Volgende stappen

**Volgende module:** [02-prompt-engineering - Prompt Engineering met GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigatie:** [← Vorige: Module 00 - Quick Start](../00-quick-start/README.md) | [Terug naar hoofdmenu](../README.md) | [Volgende: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet als gezaghebbende bron worden beschouwd. Voor cruciale informatie wordt een professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->