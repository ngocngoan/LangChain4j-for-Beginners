# Module 00: Snel Aan de Slag

## Inhoudsopgave

- [Introductie](../../../00-quick-start)
- [Wat is LangChain4j?](../../../00-quick-start)
- [LangChain4j-afhankelijkheden](../../../00-quick-start)
- [Vereisten](../../../00-quick-start)
- [Installatie](../../../00-quick-start)
  - [1. Verkrijg je GitHub-token](../../../00-quick-start)
  - [2. Stel je token in](../../../00-quick-start)
- [Voer de voorbeelden uit](../../../00-quick-start)
  - [1. Basis Chat](../../../00-quick-start)
  - [2. Promptpatronen](../../../00-quick-start)
  - [3. Functie-aanroepen](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Verantwoorde AI](../../../00-quick-start)
- [Wat elk voorbeeld laat zien](../../../00-quick-start)
- [Volgende stappen](../../../00-quick-start)
- [Probleemoplossing](../../../00-quick-start)

## Introductie

Deze snelstart is bedoeld om je zo snel mogelijk aan de slag te krijgen met LangChain4j. Het behandelt de absolute basis van het bouwen van AI-toepassingen met LangChain4j en GitHub Models. In de volgende modules ga je over op Azure OpenAI en GPT-5.2 en duik je dieper in elk concept.

## Wat is LangChain4j?

LangChain4j is een Java-bibliotheek die het bouwen van AI-aangedreven toepassingen vereenvoudigt. In plaats van te werken met HTTP-clients en JSON-parsing werk je met schone Java-API's.

De "keten" in LangChain verwijst naar het aaneenschakelen van meerdere componenten - je kunt een prompt aan een model ketenen aan een parser, of meerdere AI-aanroepen aan elkaar koppelen waarbij de output van de ene invoer wordt voor de volgende. Deze snelstart richt zich op de fundamenten voordat we complexere ketens verkennen.

<img src="../../../translated_images/nl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Keten van componenten in LangChain4j - bouwstenen verbinden om krachtige AI-workflows te creëren*

We gebruiken drie kerncomponenten:

**ChatModel** - De interface voor AI-modelinteracties. Roep `model.chat("prompt")` aan en krijg een response string terug. We gebruiken `OpenAiOfficialChatModel` dat werkt met OpenAI-compatibele endpoints zoals GitHub Models.

**AiServices** - Maakt type-veilige AI service interfaces. Definieer methoden, annoteer ze met `@Tool`, en LangChain4j regelt de orkestratie. De AI roept automatisch je Java-methoden aan indien nodig.

**MessageWindowChatMemory** - Houdt de gespreksgeschiedenis bij. Zonder dit is elke aanvraag onafhankelijk. Met deze geheugenfunctie onthoudt de AI eerdere berichten en behoudt context over meerdere interacties.

<img src="../../../translated_images/nl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architectuur - kerncomponenten die samenwerken om je AI-toepassingen aan te drijven*

## LangChain4j-afhankelijkheden

Deze snelstart gebruikt drie Maven-afhankelijkheden in de [`pom.xml`](../../../00-quick-start/pom.xml):

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

De `langchain4j-open-ai-official` module levert de `OpenAiOfficialChatModel` klasse die verbinding maakt met OpenAI-compatibele API's. GitHub Models gebruikt hetzelfde API-formaat, dus een speciale adapter is niet nodig - verwijs gewoon de basis-URL naar `https://models.github.ai/inference`.

De `langchain4j-easy-rag` module zorgt voor automatische documentopsplitsing, embedding en ophalen zodat je RAG-toepassingen kunt bouwen zonder elke stap handmatig te configureren.

## Vereisten

**Gebruik je de Dev Container?** Java en Maven zijn al geïnstalleerd. Je hebt alleen een GitHub Personal Access Token nodig.

**Lokale ontwikkeling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instructies hieronder)

> **Opmerking:** Deze module gebruikt `gpt-4.1-nano` van GitHub Models. Wijzig de modelnaam niet in de code - deze is geconfigureerd om te werken met de beschikbare modellen van GitHub.

## Installatie

### 1. Verkrijg je GitHub-token

1. Ga naar [GitHub Instellingen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Klik op "Generate new token"
3. Geef een beschrijvende naam (bijv. "LangChain4j Demo")
4. Stel een vervaldatum in (7 dagen aanbevolen)
5. Onder "Account permissions", zoek "Models" en stel in op "Read-only"
6. Klik op "Generate token"
7. Kopieer en bewaar je token - je ziet het daarna niet meer terug

### 2. Stel je token in

**Optie 1: Gebruik VS Code (Aanbevolen)**

Als je VS Code gebruikt, voeg je je token toe aan het `.env` bestand in de projectroot:

Als het `.env` bestand niet bestaat, kopieer dan `.env.example` naar `.env` of maak een nieuw `.env` bestand aan in de projectroot.

**Voorbeeld `.env` bestand:**
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Dan kun je eenvoudig met de rechtermuisknop op een demo-bestand klikken (bijv. `BasicChatDemo.java`) in de Verkenner en **"Run Java"** selecteren of de launchconfiguraties gebruiken via het Run en Debug paneel.

**Optie 2: Gebruik de Terminal**

Stel de token in als een omgevingsvariabele:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Voer de voorbeelden uit

**Met VS Code:** Klik met de rechtermuisknop op een demo-bestand in de Verkenner en kies **"Run Java"**, of gebruik de launchconfiguraties vanuit het Run en Debug paneel (zorg dat het `.env` bestand met je token eerst is toegevoegd).

**Met Maven:** Je kunt ook vanaf de commandoregel draaien:

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

Toont zero-shot, few-shot, chain-of-thought en rol-gebaseerde prompts.

### 3. Functie-aanroepen

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

De AI roept automatisch je Java-methoden aan indien nodig.

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Stel vragen over je documenten met Easy RAG via automatische embedding en ophalen.

### 5. Verantwoorde AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Zie hoe AI-veiligheidsfilters schadelijke inhoud blokkeren.

## Wat elk voorbeeld laat zien

**Basis Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Begin hier om LangChain4j in zijn eenvoudigste vorm te zien. Je maakt een `OpenAiOfficialChatModel`, stuurt een prompt met `.chat()`, en ontvangt een antwoord. Dit toont de basis: hoe je modellen initialiseert met aangepaste endpoints en API-sleutels. Als je dit patroon begrijpt, bouwt alles daarop voort.

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
> - "Hoe schakel ik in deze code over van GitHub Models naar Azure OpenAI?"
> - "Welke andere parameters kan ik configureren in OpenAiOfficialChatModel.builder()?"
> - "Hoe voeg ik streaming responses toe in plaats van te wachten op het complete antwoord?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu je weet hoe je met een model praat, laten we verkennen wat je zegt. Deze demo gebruikt dezelfde modelconfiguratie maar toont vijf verschillende promptpatronen. Probeer zero-shot prompts voor directe instructies, few-shot prompts die leren van voorbeelden, chain-of-thought prompts die redeneerstappen onthullen, en rol-gebaseerde prompts die context instellen. Je zult zien hoe hetzelfde model dramatisch verschillende resultaten geeft op basis van hoe je je verzoek formuleert.

De demo toont ook prompttemplates, een krachtige manier om herbruikbare prompts met variabelen te maken.
Het onderstaande voorbeeld toont een prompt die de LangChain4j `PromptTemplate` gebruikt om variabelen in te vullen. De AI zal antwoorden op basis van de opgegeven bestemming en activiteit.

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
> - "Wat is het verschil tussen zero-shot en few-shot prompting, en wanneer gebruik ik welke?"
> - "Hoe beïnvloedt de temperatuurparameter de reacties van het model?"
> - "Welke technieken zijn er om promptinjectie-aanvallen in productie te voorkomen?"
> - "Hoe maak ik herbruikbare PromptTemplate-objecten voor veelvoorkomende patronen?"

**Tool Integratie** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hier wordt LangChain4j krachtig. Je gebruikt `AiServices` om een AI-assistent te maken die je Java-methoden kan aanroepen. Annoteer gewoon methoden met `@Tool("beschrijving")` en LangChain4j regelt de rest - de AI beslist automatisch wanneer elke tool te gebruiken op basis van wat de gebruiker vraagt. Dit demonstreert functie-aanroepen, een sleuteltechniek voor het bouwen van AI die acties kan ondernemen, niet alleen vragen beantwoorden.

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
> - "Hoe werkt de @Tool-annotatie en wat doet LangChain4j ermee achter de schermen?"
> - "Kan de AI meerdere tools achter elkaar aanroepen om complexe problemen op te lossen?"
> - "Wat gebeurt er als een tool een uitzondering gooit - hoe moet ik fouten afhandelen?"
> - "Hoe integreer ik een echte API in plaats van dit rekenvoorbeeld?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier zie je RAG (retrieval-augmented generation) met LangChain4j's "Easy RAG"-aanpak. Documenten worden geladen, automatisch gesplitst en embedded in een in-memory opslag, waarna een content retriever relevante fragmenten aan de AI levert bij een vraag. De AI antwoordt op basis van je documenten, niet op algemene kennis.

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
> - "Wat is het verschil tussen deze makkelijke aanpak en een aangepaste RAG-pijplijn?"
> - "Hoe schaal ik dit op om meerdere documenten of grotere kennisbases te verwerken?"

**Verantwoorde AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bouw AI-veiligheid met defense in depth. Deze demo toont twee beschermingslagen die samenwerken:

**Deel 1: LangChain4j Input Guardrails** - Blokkeer gevaarlijke prompts voordat ze de LLM bereiken. Maak aangepaste guardrails die zoeken naar verboden sleutelwoorden of patronen. Deze draaien in je code, dus ze zijn snel en gratis.

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

**Deel 2: Provider Safety Filters** - GitHub Models heeft ingebouwde filters die opvangen wat je guardrails misschien missen. Je ziet harde blokkades (HTTP 400 errors) voor ernstige overtredingen en zachte weigeringen waarbij de AI beleefd weigert.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) en vraag:
> - "Wat is InputGuardrail en hoe maak ik mijn eigen guardrails?"
> - "Wat is het verschil tussen een harde blokkade en een zachte weigering?"
> - "Waarom gebruik je guardrails en provider-filters samen?"

## Volgende stappen

**Volgende Module:** [01-introductie - Aan de slag met LangChain4j](../01-introduction/README.md)

---

**Navigatie:** [← Terug naar Hoofd](../README.md) | [Volgende: Module 01 - Introductie →](../01-introduction/README.md)

---

## Probleemoplossing

### Eerste Maven-build

**Probleem:** Eerste `mvn clean compile` of `mvn package` duurt lang (10-15 minuten)

**Oorzaak:** Maven moet alle projectafhankelijkheden downloaden (Spring Boot, LangChain4j-bibliotheken, Azure SDK's, enz.) bij de eerste build.

**Oplossing:** Dit is normaal gedrag. Volgende builds zijn veel sneller omdat afhankelijkheden lokaal worden gecachet. Downloadtijd hangt af van je netwerksnelheid.

### PowerShell Maven-commando syntax

**Probleem:** Maven-commando's mislukken met fout `Unknown lifecycle phase ".mainClass=..."`
**Oorzaak**: PowerShell interpreteert `=` als een toewijzingsoperator voor variabelen, waardoor de Maven-syntaxis voor eigenschappen wordt verbroken

**Oplossing**: Gebruik de stop-parsing-operator `--%` vóór het Maven-commando:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

De `--%`-operator vertelt PowerShell om alle resterende argumenten letterlijk door te geven aan Maven zonder interpretatie.

### Windows PowerShell Emoji-weergave

**Probleem**: AI-responses tonen vreemde tekens (bijv. `????` of `â??`) in plaats van emoji's in PowerShell

**Oorzaak**: De standaardcodering van PowerShell ondersteunt geen UTF-8 emoji's

**Oplossing**: Voer dit commando uit voordat je Java-toepassingen start:
```cmd
chcp 65001
```

Dit dwingt UTF-8-codering af in de terminal. Gebruik eventueel Windows Terminal, dat betere Unicode-ondersteuning heeft.

### Debuggen van API-aanroepen

**Probleem**: Authenticatiefouten, snelheidslimieten of onverwachte reacties van het AI-model

**Oplossing**: De voorbeelden bevatten `.logRequests(true)` en `.logResponses(true)` om API-aanroepen in de console te tonen. Dit helpt bij het oplossen van authenticatiefouten, snelheidslimieten of onverwachte reacties. Verwijder deze vlaggen in productie om minder loggeluid te hebben.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, moet u er rekening mee houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal dient als de gezaghebbende bron beschouwd te worden. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->