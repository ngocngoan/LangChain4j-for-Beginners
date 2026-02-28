# Module 00: Snelstart

## Inhoudsopgave

- [Inleiding](../../../00-quick-start)
- [Wat is LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Vereisten](../../../00-quick-start)
- [Installatie](../../../00-quick-start)
  - [1. Verkrijg je GitHub-token](../../../00-quick-start)
  - [2. Stel je token in](../../../00-quick-start)
- [Voorbeelden uitvoeren](../../../00-quick-start)
  - [1. Basis Chat](../../../00-quick-start)
  - [2. Promptpatronen](../../../00-quick-start)
  - [3. Functieaanroep](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Verantwoordelijke AI](../../../00-quick-start)
- [Wat elk voorbeeld toont](../../../00-quick-start)
- [Volgende stappen](../../../00-quick-start)
- [Probleemoplossing](../../../00-quick-start)

## Inleiding

Deze snelstart is bedoeld om je zo snel mogelijk aan de slag te krijgen met LangChain4j. Het behandelt de absolute basis van het bouwen van AI-toepassingen met LangChain4j en GitHub-modellen. In de volgende modules gebruik je Azure OpenAI met LangChain4j om meer geavanceerde toepassingen te bouwen.

## Wat is LangChain4j?

LangChain4j is een Java-bibliotheek die het bouwen van AI-aangedreven toepassingen vereenvoudigt. In plaats van te werken met HTTP-clients en JSON-parsing werk je met schone Java-API's.

De "keten" in LangChain verwijst naar het aan elkaar koppelen van meerdere componenten - je kunt een prompt aan een model koppelen aan een parser, of meerdere AI-aanroepen aan elkaar schakelen waarbij de output van de ene de input voor de volgende is. Deze snelstart richt zich op de basisprincipes voordat complexere ketens worden onderzocht.

<img src="../../../translated_images/nl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Componenten koppelen in LangChain4j – bouwstenen verbinden zich om krachtige AI-workflows te creëren*

We gebruiken drie kerncomponenten:

**ChatModel** - De interface voor AI-modelinteracties. Roep `model.chat("prompt")` aan en krijg een respons als string terug. We gebruiken `OpenAiOfficialChatModel` dat werkt met OpenAI-compatibele endpoints zoals GitHub-modellen.

**AiServices** - Maakt type-veilige AI-service-interfaces aan. Definieer methoden, annoteer ze met `@Tool`, en LangChain4j regelt de orkestratie. De AI roept automatisch je Java-methoden aan wanneer nodig.

**MessageWindowChatMemory** - Onderhoudt het gespreksverloop. Zonder dit is elk verzoek onafhankelijk. Met deze component onthoudt de AI eerdere berichten en houdt context vast over meerdere interacties.

<img src="../../../translated_images/nl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-architectuur – kerncomponenten werken samen om je AI-toepassingen aan te drijven*

## LangChain4j Dependencies

Deze snelstart gebruikt drie Maven-dependencies in de [`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

De module `langchain4j-open-ai-official` levert de klasse `OpenAiOfficialChatModel` die verbinding maakt met OpenAI-compatibele API's. GitHub-modellen gebruikt hetzelfde API-formaat, dus er is geen speciale adapter nodig - wijs gewoon de basis-URL toe aan `https://models.github.ai/inference`.

De module `langchain4j-easy-rag` verzorgt automatische documentopsplitsing, embedding en retrieval zodat je RAG-toepassingen kunt bouwen zonder elke stap handmatig te configureren.

## Vereisten

**Gebruik je de Dev Container?** Java en Maven zijn al geïnstalleerd. Je hebt alleen een GitHub Personal Access Token nodig.

**Lokaal ontwikkelen:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instructies hieronder)

> **Opmerking:** Deze module gebruikt `gpt-4.1-nano` van GitHub-modellen. Wijzig de modelnaam in de code niet – het is geconfigureerd om te werken met de beschikbare GitHub-modellen.

## Installatie

### 1. Verkrijg je GitHub-token

1. Ga naar [GitHub Instellingen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik op "Generate new token"
3. Geef een beschrijvende naam op (bijv. "LangChain4j Demo")
4. Stel een vervaldatum in (7 dagen aanbevolen)
5. Onder "Account permissions" vind je "Models" en zet dit op "Read-only"
6. Klik op "Generate token"
7. Kopieer en bewaar je token – je ziet hem daarna niet meer terug

### 2. Stel je token in

**Optie 1: VS Code gebruiken (aanbevolen)**

Als je VS Code gebruikt, voeg dan je token toe aan het `.env`-bestand in de root van het project:

Als het `.env`-bestand niet bestaat, kopieer dan `.env.example` naar `.env` of maak een nieuw `.env`-bestand aan in de projectroot.

**Voorbeeld `.env`-bestand:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Daarna kun je simpelweg rechtsklikken op elk demo-bestand (bijv. `BasicChatDemo.java`) in de Verkenner en **"Run Java"** selecteren of gebruik maken van de launch-configuraties in het Run en Debug paneel.

**Optie 2: Terminal gebruiken**

Stel het token in als een omgevingsvariabele:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Voorbeelden uitvoeren

**Met VS Code:** Rechtsklik eenvoudig op een demo-bestand in de Verkenner en kies **"Run Java"**, of gebruik de launch-configuraties vanuit het Run en Debug paneel (zorg ervoor dat je token eerst aan het `.env`-bestand is toegevoegd).

**Met Maven:** Je kunt ook vanaf de commandoregel uitvoeren:

### 1. Basis Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Promptpatronen

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Toont zero-shot, few-shot, chain-of-thought en rol-gebaseerde prompting.

### 3. Functieaanroep

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

De AI roept automatisch je Java-methoden aan wanneer nodig.

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stel vragen over je documenten met Easy RAG met automatische embedding en retrieval.

### 5. Verantwoordelijke AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Zie hoe AI-veiligheidsfilters schadelijke inhoud blokkeren.

## Wat elk voorbeeld toont

**Basis Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Begin hier om LangChain4j in zijn eenvoudigste vorm te zien. Je maakt een `OpenAiOfficialChatModel` aan, stuurt een prompt met `.chat()` en krijgt een respons terug. Dit demonstreert de basis: hoe je modellen initialiseert met aangepaste endpoints en API-sleutels. Zodra je dit patroon begrijpt, bouwt alles verder hierop voort.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) en vraag:
> - "Hoe schakel ik in deze code van GitHub Models over naar Azure OpenAI?"
> - "Welke andere parameters kan ik configureren in OpenAiOfficialChatModel.builder()?"
> - "Hoe voeg ik streamingresponses toe in plaats van te wachten op het volledige antwoord?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu je weet hoe je met een model praat, bekijken we wat je het zegt. Deze demo gebruikt dezelfde modelinstelling maar toont vijf verschillende promptingpatronen. Probeer zero-shot prompts voor directe instructies, few-shot prompts die leren van voorbeelden, chain-of-thought prompts die redeneringsstappen onthullen, en rol-gebaseerde prompts die context stellen. Je ziet hoe hetzelfde model dramatisch verschillende resultaten geeft afhankelijk van hoe je je verzoek formuleert.

De demo toont ook prompttemplates, een krachtige manier om herbruikbare prompts met variabelen te creëren.
Het onderstaande voorbeeld toont een prompt die met de LangChain4j `PromptTemplate` variabelen invult. De AI antwoordt op basis van de gegeven bestemming en activiteit.

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) en vraag:
> - "Wat is het verschil tussen zero-shot en few-shot prompting, en wanneer gebruik ik elk?"
> - "Hoe beïnvloedt de temperatuurparameter de antwoorden van het model?"
> - "Welke technieken zijn er om prompt injectie-aanvallen in productie te voorkomen?"
> - "Hoe maak ik herbruikbare PromptTemplate-objecten voor veelvoorkomende patronen?"

**Tool Integratie** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Dit is waar LangChain4j krachtig wordt. Je gebruikt `AiServices` om een AI-assistent te maken die je Java-methoden kan aanroepen. Annotaties met `@Tool("beschrijving")` zijn voldoende en LangChain4j regelt de rest – de AI beslist automatisch wanneer elke tool wordt gebruikt op basis van wat de gebruiker vraagt. Dit demonstreert functieaanroep, een belangrijke techniek om AI acties te laten uitvoeren, en niet alleen vragen te beantwoorden.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) en vraag:
> - "Hoe werkt de @Tool annotatie en wat doet LangChain4j er achter de schermen mee?"
> - "Kan de AI meerdere tools achter elkaar aanroepen om complexe problemen op te lossen?"
> - "Wat gebeurt er als een tool een uitzondering gooit – hoe ga ik om met fouten?"
> - "Hoe integreer ik een echte API in plaats van deze rekenmachinevoorbeelden?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier zie je RAG (retrieval-augmented generation) met LangChain4j’s "Easy RAG"-benadering. Documenten worden geladen, automatisch opgesplitst en embedded in een in-memory store, vervolgens levert een content-retriever relevante stukken aan de AI op het moment van vraag. De AI antwoordt op basis van jouw documenten, niet op zijn algemene kennis.

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) en vraag:
> - "Hoe voorkomt RAG AI-hallucinaties in vergelijking met het gebruik van de trainingsdata van het model?"
> - "Wat is het verschil tussen deze eenvoudige aanpak en een aangepaste RAG-pijplijn?"
> - "Hoe schaal ik dit op voor meerdere documenten of grotere kennisbanken?"

**Verantwoordelijke AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bouw AI-veiligheid met defense in depth. Deze demo toont twee lagen bescherming die samenwerken:

**Deel 1: LangChain4j Input Guardrails** - Blokkeer gevaarlijke prompts voordat ze de LLM bereiken. Maak aangepaste guardrails die controleren op verboden trefwoorden of patronen. Deze draaien in je code, dus ze zijn snel en gratis.

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**Deel 2: Provider Safety Filters** - GitHub-modellen hebben ingebouwde filters die opvangen wat je guardrails mogelijk missen. Je ziet harde blokkades (HTTP 400-fouten) bij ernstige overtredingen en zachte weigeringen waarbij de AI beleefd weigert.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) en vraag:
> - "Wat is InputGuardrail en hoe maak ik mijn eigen guardrails?"
> - "Wat is het verschil tussen een harde blokkade en een zachte weigering?"
> - "Waarom gebruik je guardrails en provider-filters samen?"

## Volgende stappen

**Volgende module:** [01-introductie - Aan de slag met LangChain4j en gpt-5 op Azure](../01-introduction/README.md)

---

**Navigatie:** [← Terug naar Hoofdmenu](../README.md) | [Volgende: Module 01 - Introductie →](../01-introduction/README.md)

---

## Probleemoplossing

### Eerste Maven-build

**Probleem**: De eerste `mvn clean compile` of `mvn package` duurt lang (10-15 minuten)

**Oorzaak**: Maven moet alle projectdependencies downloaden (Spring Boot, LangChain4j bibliotheken, Azure SDK's, etc.) bij de eerste build.

**Oplossing**: Dit is normaal gedrag. Volgende builds gaan veel sneller omdat dependencies lokaal worden gecached. De downloadtijd hangt af van je netwerksnelheid.

### PowerShell Maven-commando syntax

**Probleem**: Maven-commando's mislukken met fout `Unknown lifecycle phase ".mainClass=..."`
**Oorzaak**: PowerShell interpreteert `=` als een variabele-toewijzingsoperator, waardoor de Maven-eigenschappensyntaxis wordt verbroken

**Oplossing**: Gebruik de stop-parseeroperator `--%` vóór het Maven-commando:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

De `--%` operator vertelt PowerShell om alle resterende argumenten letterlijk door te geven aan Maven zonder interpretatie.

### Emoji-weergave in Windows PowerShell

**Probleem**: AI-reacties tonen onleesbare tekens (bijv. `????` of `â??`) in plaats van emoji’s in PowerShell

**Oorzaak**: De standaardcodering van PowerShell ondersteunt geen UTF-8 emoji’s

**Oplossing**: Voer dit commando uit voordat je Java-applicaties start:
```cmd
chcp 65001
```

Dit forceert UTF-8-codering in de terminal. Gebruik eventueel Windows Terminal, dat betere Unicode-ondersteuning heeft.

### API-aanroepen debuggen

**Probleem**: Authenticatiefouten, snelheidslimieten of onverwachte reacties van het AI-model

**Oplossing**: De voorbeelden bevatten `.logRequests(true)` en `.logResponses(true)` om API-aanroepen in de console te tonen. Dit helpt bij het oplossen van authenticatiefouten, snelheidslimieten of onverwachte reacties. Verwijder deze instellingen in productie om logruis te verminderen.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal wordt beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->