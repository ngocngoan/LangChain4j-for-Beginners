# Module 01: Aan de slag met LangChain4j

## Inhoudsopgave

- [Wat je zult leren](../../../01-introduction)
- [Vereisten](../../../01-introduction)
- [Begrip van het kernprobleem](../../../01-introduction)
- [Begrip van tokens](../../../01-introduction)
- [Hoe geheugen werkt](../../../01-introduction)
- [Hoe dit LangChain4j gebruikt](../../../01-introduction)
- [Azure OpenAI-infrastructuur implementeren](../../../01-introduction)
- [De applicatie lokaal uitvoeren](../../../01-introduction)
- [De applicatie gebruiken](../../../01-introduction)
  - [Toestandsloze chat (linkerpaneel)](../../../01-introduction)
  - [Toestandsgebonden chat (rechterpaneel)](../../../01-introduction)
- [Volgende stappen](../../../01-introduction)

## Wat je zult leren

Als je de snelle start hebt voltooid, heb je gezien hoe je prompts verzendt en antwoorden krijgt. Dat is de basis, maar echte toepassingen hebben meer nodig. Deze module leert je hoe je conversatie-AI bouwt die context onthoudt en de status behoudt - het verschil tussen een eenmalige demo en een productieklare applicatie.

We gebruiken door deze handleiding Azure OpenAI’s GPT-5.2 omdat de geavanceerde redeneercapaciteiten het gedrag van verschillende patronen duidelijker maken. Wanneer je geheugen toevoegt, zie je het verschil duidelijk. Dit maakt het gemakkelijker te begrijpen wat elk onderdeel aan je applicatie toevoegt.

Je bouwt één applicatie die beide patronen demonstreert:

**Toestandsloze chat** - Elke aanvraag is onafhankelijk. Het model onthoudt geen eerdere berichten. Dit is het patroon dat je in de snelle start gebruikte.

**Toestandsgebonden gesprek** - Elke aanvraag bevat de gespreksgeschiedenis. Het model behoudt context over meerdere beurten. Dit is wat productieapplicaties vereisen.

## Vereisten

- Azure-abonnement met toegang tot Azure OpenAI
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Opmerking:** Java, Maven, Azure CLI en Azure Developer CLI (azd) zijn vooraf geïnstalleerd in de meegeleverde devcontainer.

> **Opmerking:** Deze module gebruikt GPT-5.2 op Azure OpenAI. De implementatie wordt automatisch geconfigureerd via `azd up` - wijzig de modelnaam niet in de code.

## Begrip van het kernprobleem

Taalmodellen zijn toestandsloos. Elke API-aanroep is onafhankelijk. Als je "Mijn naam is John" stuurt en daarna vraagt "Wat is mijn naam?", heeft het model geen idee dat je jezelf net hebt voorgesteld. Het behandelt elke aanvraag alsof het het eerste gesprek is dat je ooit hebt gehad.

Dit is prima voor eenvoudige Q&A, maar nutteloos voor echte toepassingen. Klantenservicebots moeten onthouden wat je vertelde. Persoonlijke assistenten hebben context nodig. Elk gesprek met meerdere beurten vereist geheugen.

<img src="../../../translated_images/nl/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Toestandsloze versus toestandsgebonden gesprekken" width="800"/>

*Het verschil tussen toestandsloze (onafhankelijke oproepen) en toestandsgebonden (contextbewuste) gesprekken*

## Begrip van tokens

Voordat je in gesprekken duikt, is het belangrijk om tokens te begrijpen - de basiseenheden van tekst die taalmodellen verwerken:

<img src="../../../translated_images/nl/token-explanation.c39760d8ec650181.webp" alt="Uitleg token" width="800"/>

*Voorbeeld van hoe tekst wordt opgesplitst in tokens - "I love AI!" wordt 4 afzonderlijke verwerkingsunits*

Tokens zijn hoe AI-modellen tekst meten en verwerken. Woorden, interpunctie en zelfs spaties kunnen tokens zijn. Je model heeft een limiet voor hoeveel tokens het tegelijk kan verwerken (400.000 voor GPT-5.2, met tot 272.000 inputtokens en 128.000 outputtokens). Het begrijpen van tokens helpt je gesprekslengte en kosten te beheren.

## Hoe geheugen werkt

Chatgeheugen lost het toestandsloze probleem op door gespreksgeschiedenis te bewaren. Voordat je je aanvraag naar het model stuurt, plaatst het framework relevante eerdere berichten ervoor. Als je vraagt "Wat is mijn naam?", stuurt het systeem eigenlijk de hele gespreksgeschiedenis mee, zodat het model kan zien dat je eerder zei "Mijn naam is John."

LangChain4j biedt geheugenimplementaties die dit automatisch afhandelen. Je kiest hoeveel berichten je wilt bewaren en het framework beheert het contextvenster.

<img src="../../../translated_images/nl/memory-window.bbe67f597eadabb3.webp" alt="Concept geheugenvenster" width="800"/>

*MessageWindowChatMemory behoudt een schuivend venster van recente berichten, waarbij oude automatisch worden verwijderd*

## Hoe dit LangChain4j gebruikt

Deze module breidt de snelle start uit door Spring Boot te integreren en conversatiegeheugen toe te voegen. Zo passen de onderdelen samen:

**Afhankelijkheden** - Voeg twee LangChain4j-bibliotheken toe:

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

De builder leest referenties uit omgevingsvariabelen die door `azd up` zijn ingesteld. Het instellen van `baseUrl` op je Azure-eindpunt laat de OpenAI-client werken met Azure OpenAI.

**Gespreksgeheugen** - Houd chatgeschiedenis bij met MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

Maak geheugen aan met `withMaxMessages(10)` om de laatste 10 berichten te behouden. Voeg gebruikers- en AI-berichten toe met getypte wrappers: `UserMessage.from(text)` en `AiMessage.from(text)`. Haal geschiedenis op met `memory.messages()` en stuur die naar het model. De service slaat aparte geheugeninstanties per gesprek-ID op, zodat meerdere gebruikers tegelijk kunnen chatten.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) en vraag:
> - "Hoe bepaalt MessageWindowChatMemory welke berichten worden verwijderd wanneer het venster vol is?"
> - "Kan ik aangepaste geheugenopslag implementeren met een database in plaats van in het geheugen?"
> - "Hoe voeg ik samenvatting toe om oude gespreksgeschiedenis te comprimeren?"

De toestandsloze chat-endpoint slaat geheugen volledig over - gewoon `chatModel.chat(prompt)` zoals in de snelle start. De toestandsgebonden endpoint voegt berichten toe aan het geheugen, haalt geschiedenis op en voegt die context toe aan elke aanvraag. Zelfde modelconfiguratie, verschillende patronen.

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

> **Opmerking:** Als je een time-outfout krijgt (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), voer dan gewoon nogmaals `azd up` uit. Azure-resources worden mogelijk nog ingericht op de achtergrond, en het opnieuw proberen laat de implementatie voltooien zodra de resources een finale status bereiken.

Dit zal:
1. Azure OpenAI-resource implementeren met GPT-5.2 en text-embedding-3-small modellen
2. Automatisch een `.env` bestand genereren in de projectroot met referenties
3. Alle benodigde omgevingsvariabelen instellen

**Implementatieproblemen?** Zie de [Infrastructure README](infra/README.md) voor gedetailleerde probleemoplossing zoals conflicten met subdomeinnamen, handmatige Azure Portal-implementatiestappen en modelconfiguratieadvies.

**Controleer of de implementatie gelukt is:**

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_SLEUTEL, enz. tonen.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, enz. weergeven.
```

> **Opmerking:** Het `azd up` commando genereert automatisch het `.env` bestand. Als je het later moet bijwerken, kun je het `.env` bestand handmatig bewerken of opnieuw genereren door uit te voeren:
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

**Controleer de implementatie:**

Zorg dat het `.env` bestand zich in de hoofdmap bevindt met Azure-gegevens:

**Bash:**
```bash
cat ../.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT tonen
```

**PowerShell:**
```powershell
Get-Content ..\.env  # Moet AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT weergeven
```

**Start de applicaties:**

**Optie 1: Gebruik maken van Spring Boot Dashboard (Aanbevolen voor VS Code gebruikers)**

De dev container bevat de Spring Boot Dashboard-extensie, die een visuele interface biedt om alle Spring Boot-applicaties te beheren. Je vindt deze in de Activiteitenbalk aan de linkerkant van VS Code (zoek naar het Spring Boot-icoon).

Vanaf het Spring Boot Dashboard kun je:
- Alle beschikbare Spring Boot-applicaties in de werkruimte zien
- Applicaties starten/stoppen met één klik
- Applicatielogs in realtime bekijken
- Applicatiestatus monitoren

Klik simpelweg op de afspeelknop naast "introduction" om deze module te starten, of start alle modules tegelijk.

<img src="../../../translated_images/nl/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**Optie 2: Gebruik maken van shell-scripts**

Start alle webapplicaties (modules 01-04):

**Bash:**
```bash
cd ..  # Vanaf de hoofdmap
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

Beide scripts laden automatisch omgevingsvariabelen uit het hoofd `.env` bestand en bouwen de JARs als die nog niet bestaan.

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

<img src="../../../translated_images/nl/home-screen.121a03206ab910c0.webp" alt="Startscherm van applicatie" width="800"/>

*Dashboard met zowel Simple Chat (toestandsloos) als Conversational Chat (toestandsgebonden) opties*

### Toestandsloze chat (linkerpaneel)

Probeer dit eerst. Vraag "Mijn naam is John" en vraag dan meteen "Wat is mijn naam?" Het model zal het niet onthouden omdat elk bericht onafhankelijk is. Dit demonstreert het kernprobleem van basis taalmodelintegratie - geen gesprekscontext.

<img src="../../../translated_images/nl/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Demo toestandsloze chat" width="800"/>

*AI onthoudt je naam niet van het vorige bericht*

### Toestandsgebonden chat (rechterpaneel)

Probeer nu hier dezelfde reeks. Vraag "Mijn naam is John" en daarna "Wat is mijn naam?" Deze keer onthoudt het. Het verschil is MessageWindowChatMemory - het behoudt gespreksgeschiedenis en voegt die met elke aanvraag toe. Zo werkt productie-gespreks-AI.

<img src="../../../translated_images/nl/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Demo toestandsgebonden chat" width="800"/>

*AI onthoudt je naam van eerder in het gesprek*

Beide panelen gebruiken hetzelfde GPT-5.2 model. Het enige verschil is geheugen. Dit maakt het duidelijk wat geheugen aan je applicatie toevoegt en waarom het essentieel is voor echte gebruikssituaties.

## Volgende stappen

**Volgende module:** [02-prompt-engineering - Prompt Engineering met GPT-5.2](../02-prompt-engineering/README.md)

---

**Navigatie:** [← Vorige: Module 00 - Snelle Start](../00-quick-start/README.md) | [Terug naar hoofdmenu](../README.md) | [Volgende: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de oorspronkelijke taal dient als het gezaghebbende referentiepunt. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->