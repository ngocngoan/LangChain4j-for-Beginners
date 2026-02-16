# Module 00: Snel aan de slag

## Inhoudsopgave

- [Introductie](../../../00-quick-start)
- [Wat is LangChain4j?](../../../00-quick-start)
- [LangChain4j-afhankelijkheden](../../../00-quick-start)
- [Vereisten](../../../00-quick-start)
- [Installatie](../../../00-quick-start)
  - [1. Haal je GitHub-token op](../../../00-quick-start)
  - [2. Stel je token in](../../../00-quick-start)
- [Voer de voorbeelden uit](../../../00-quick-start)
  - [1. Basischat](../../../00-quick-start)
  - [2. Promptpatronen](../../../00-quick-start)
  - [3. Functie-aanroepen](../../../00-quick-start)
  - [4. Document Q&A (RAG)](../../../00-quick-start)
  - [5. Verantwoordelijke AI](../../../00-quick-start)
- [Wat elk voorbeeld laat zien](../../../00-quick-start)
- [Volgende stappen](../../../00-quick-start)
- [Probleemoplossing](../../../00-quick-start)

## Introductie

Deze snelle start is bedoeld om je zo snel mogelijk op weg te helpen met LangChain4j. Het behandelt de absolute basis van het bouwen van AI-toepassingen met LangChain4j en GitHub Models. In de volgende modules ga je Azure OpenAI gebruiken met LangChain4j om geavanceerdere toepassingen te bouwen.

## Wat is LangChain4j?

LangChain4j is een Java-bibliotheek die het bouwen van AI-gestuurde applicaties vereenvoudigt. In plaats van te werken met HTTP-clients en JSON-parsing, werk je met nette Java-API's.

De "chain" in LangChain verwijst naar het aaneenschakelen van meerdere componenten - je kunt een prompt aan een model koppelen, daarna aan een parser, of meerdere AI-aanroepen achter elkaar schakelen waarbij de output van de ene de input is van de volgende. Deze snelle start richt zich op de basisprincipes voordat er complexere ketens worden onderzocht.

<img src="../../../translated_images/nl/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Het schakelen van componenten in LangChain4j – bouwstenen verbinden om krachtige AI-workflows te creëren*

We gebruiken drie kerncomponenten:

**ChatLanguageModel** – De interface voor AI-modelinteracties. Roep `model.chat("prompt")` aan en krijg een antwoordstring terug. We gebruiken `OpenAiOfficialChatModel` die werkt met OpenAI-compatibele endpoints zoals GitHub Models.

**AiServices** – Maakt typeveilige AI-serviceinterfaces aan. Definieer methoden, annoteer ze met `@Tool`, en LangChain4j regelt de orkestratie. De AI roept automatisch jouw Java-methoden aan wanneer nodig.

**MessageWindowChatMemory** – Houdt de gespreksgeschiedenis bij. Zonder dit is elk verzoek onafhankelijk. Met deze component onthoudt de AI vorige berichten en behoudt context over meerdere beurten.

<img src="../../../translated_images/nl/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j-architectuur – kerncomponenten werken samen om je AI-toepassingen aan te drijven*

## LangChain4j-afhankelijkheden

Deze snelle start gebruikt twee Maven-afhankelijkheden in de [`pom.xml`](../../../00-quick-start/pom.xml):

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
```
  
De `langchain4j-open-ai-official` module levert de `OpenAiOfficialChatModel` klasse die verbinding maakt met OpenAI-compatibele API's. GitHub Models gebruikt hetzelfde API-formaat, dus er is geen speciale adapter nodig – wijs de basis-URL gewoon toe aan `https://models.github.ai/inference`.

## Vereisten

**Gebruik je de Dev Container?** Java en Maven zijn al geïnstalleerd. Je hebt alleen een GitHub Personal Access Token nodig.

**Lokale ontwikkeling:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (instructies hieronder)

> **Opmerking:** Deze module gebruikt `gpt-4.1-nano` van GitHub Models. Wijzig de modelnaam in de code niet — deze is geconfigureerd om te werken met de beschikbare modellen van GitHub.

## Installatie

### 1. Haal je GitHub-token op

1. Ga naar [GitHub Instellingen → Personal Access Tokens](https://github.com/settings/personal-access-tokens)  
2. Klik op "Generate new token"  
3. Geef een beschrijvende naam op (bijv. "LangChain4j Demo")  
4. Stel de vervaldatum in (7 dagen aanbevolen)  
5. Onder "Account permissions", zoek "Models" en zet op "Read-only"  
6. Klik op "Generate token"  
7. Kopieer en bewaar je token – je ziet het later niet meer terug  

### 2. Stel je token in

**Optie 1: Gebruik VS Code (aanbevolen)**

Als je VS Code gebruikt, voeg je je token toe aan het `.env` bestand in de projectroot:

Als het `.env` bestand niet bestaat, kopieer dan `.env.example` naar `.env` of maak een nieuw `.env` bestand aan in de projectroot.

**Voorbeeld van een `.env` bestand:**  
```bash
# In /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```
  
Dan kun je simpelweg met rechts klikken op een demobestand (bijv. `BasicChatDemo.java`) in de Verkenner en kiezen voor **"Run Java"** of de launch-configuraties gebruiken vanuit het Run and Debug-paneel.

**Optie 2: Gebruik terminal**

Stel het token in als een omgevingsvariabele:

**Bash:**  
```bash
export GITHUB_TOKEN=your_token_here
```
  
**PowerShell:**  
```powershell
$env:GITHUB_TOKEN=your_token_here
```


## Voer de voorbeelden uit

**Met VS Code:** Klik met rechts op een demobestand in de Verkenner en kies **"Run Java"**, of gebruik de launch-configuraties in Run and Debug (zorg dat je token eerst is toegevoegd aan het `.env` bestand).

**Met Maven:** Je kunt ook via de opdrachtregel uitvoeren:

### 1. Basischat

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
  
Toont zero-shot, few-shot, chain-of-thought, en op rol gebaseerde prompts.

### 3. Functie-aanroepen

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```
  
De AI roept automatisch je Java-methoden aan wanneer nodig.

### 4. Document Q&A (RAG)

**Bash:**  
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
**PowerShell:**  
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```
  
Stel vragen over inhoud in `document.txt`.

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

## Wat elk voorbeeld laat zien

**Basischat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Begin hier om LangChain4j in zijn eenvoudigste vorm te zien. Je maakt een `OpenAiOfficialChatModel`, stuurt een prompt met `.chat()`, en krijgt een antwoord terug. Dit toont de basis: hoe modellen te initialiseren met aangepaste endpoints en API-sleutels. Als je dit patroon begrijpt, bouwt alles daarop voort.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
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
> - "Hoe voeg ik streaming reacties toe in plaats van te wachten op het volledige antwoord?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nu je weet hoe je met een model praat, laten we verkennen wat je tegen het model zegt. Deze demo gebruikt dezelfde modelconfiguratie, maar toont vijf verschillende promptingpatronen. Probeer zero-shot prompts voor directe instructies, few-shot prompts die leren van voorbeelden, chain-of-thought prompts die redeneerstappen onthullen, en rol-gebaseerde prompts die de context instellen. Je ziet hoe hetzelfde model drastisch verschillende resultaten geeft, afhankelijk van hoe je je verzoek formuleert.

De demo toont ook prompttemplates, een krachtige manier om herbruikbare prompts met variabelen te maken.  
Het onderstaande voorbeeld toont een prompt met de LangChain4j `PromptTemplate` om variabelen in te vullen. De AI zal antwoorden gebaseerd op de opgegeven bestemming en activiteit.

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
> - "Hoe beïnvloedt de temperatuurparameter de antwoorden van het model?"  
> - "Welke technieken zijn er om promptinjectie-aanvallen in productie te voorkomen?"  
> - "Hoe maak ik herbruikbare PromptTemplate-objecten voor veelvoorkomende patronen?"

**Tool Integratie** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Hier wordt LangChain4j krachtig. Je gebruikt `AiServices` om een AI-assistent te maken die je Java-methoden kan aanroepen. Annotaties met `@Tool("beschrijving")` zijn genoeg; LangChain4j regelt de rest – de AI beslist automatisch welke tool te gebruiken op basis van wat de gebruiker vraagt. Dit demonstreert functie-aanroepen, een sleuteltechniek om AI te bouwen die acties kan ondernemen, niet alleen vragen beantwoorden.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```
  
> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) en vraag:  
> - "Hoe werkt de @Tool-annotatie en wat doet LangChain4j ermee achter de schermen?"  
> - "Kan de AI meerdere tools achtereenvolgens aanroepen om complexe problemen op te lossen?"  
> - "Wat gebeurt er als een tool een uitzondering gooit - hoe behandel ik fouten?"  
> - "Hoe integreer ik een echte API in plaats van dit rekenvoorbeeld?"

**Document Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Hier zie je de basis van RAG (retrieval-augmented generation). In plaats van te vertrouwen op de trainingsdata van het model, laad je content uit [`document.txt`](../../../00-quick-start/document.txt) en neem je die op in de prompt. De AI antwoordt op basis van jouw document, niet op zijn algemene kennis. Dit is de eerste stap naar systemen die met je eigen data werken.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```
  
> **Opmerking:** Deze eenvoudige aanpak laadt het hele document in de prompt. Voor grote bestanden (>10KB) overschrijd je contextlimieten. Module 03 behandelt chunking en vector search voor RAG-systemen in productie.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) en vraag:  
> - "Hoe voorkomt RAG AI-hallucinaties vergeleken met het gebruik van de trainingsdata van het model?"  
> - "Wat is het verschil tussen deze eenvoudige aanpak en het gebruik van vector-embedding voor retrieval?"  
> - "Hoe schaal ik dit om meerdere documenten of grotere kennisbanken te verwerken?"  
> - "Wat zijn best practices voor het structureren van de prompt zodat AI alleen de gegeven context gebruikt?"

**Verantwoordelijke AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Bouw AI-veiligheid met defense in depth. Deze demo toont twee beschermingslagen die samenwerken:

**Deel 1: LangChain4j Input Guardrails** – Blokkeer gevaarlijke prompts voordat ze de LLM bereiken. Maak aangepaste guardrails die controleren op verboden trefwoorden of patronen. Deze draaien in je code, dus ze zijn snel en gratis.

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
  
**Deel 2: Provider Safety Filters** – GitHub Models heeft ingebouwde filters die vangen wat je guardrails misschien missen. Je ziet harde blokkades (HTTP 400-fouten) bij ernstige overtredingen en zachte weigeringen waarbij de AI beleefd weigert.

> **🤖 Probeer met [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) en vraag:  
> - "Wat is InputGuardrail en hoe maak ik mijn eigen?"  
> - "Wat is het verschil tussen een harde blokkade en een zachte weigering?"  
> - "Waarom gebruik je guardrails en providerfilters samen?"

## Volgende stappen

**Volgende module:** [01-introductie - Aan de slag met LangChain4j en gpt-5 op Azure](../01-introduction/README.md)

---

**Navigatie:** [← Terug naar hoofdmenu](../README.md) | [Volgende: Module 01 - Introductie →](../01-introduction/README.md)

---

## Probleemoplossing

### Eerste Maven-build

**Probleem:** Eerste `mvn clean compile` of `mvn package` duurt lang (10-15 minuten)

**Oorzaak:** Maven moet alle projectafhankelijkheden (Spring Boot, LangChain4j-bibliotheken, Azure SDK's, enz.) bij de eerste build downloaden.

**Oplossing:** Dit is normaal gedrag. Volgende builds zijn veel sneller omdat afhankelijkheden lokaal worden gecachet. De downloadtijd hangt af van je netwerkverbinding.
### PowerShell Maven Command Syntax

**Probleem**: Maven-commando's mislukken met de fout `Unknown lifecycle phase ".mainClass=..."`

**Oorzaak**: PowerShell interpreteert `=` als een variabele-toewijzingsoperator, waardoor de Maven-eigenschapssyntaxis wordt verbroken

**Oplossing**: Gebruik de stop-parsing-operator `--%` voor het Maven-commando:

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

**Probleem**: AI-antwoorden tonen rommeltekens (bijv. `????` of `â??`) in plaats van emoji's in PowerShell

**Oorzaak**: De standaardcodering van PowerShell ondersteunt geen UTF-8 emoji's

**Oplossing**: Voer dit commando uit voordat je Java-applicaties uitvoert:
```cmd
chcp 65001
```

Dit forceert UTF-8-codering in de terminal. Gebruik eventueel Windows Terminal, dat betere Unicode-ondersteuning heeft.

### Debuggen van API-aanroepen

**Probleem**: Authenticatiefouten, snelheidslimieten, of onverwachte antwoorden van het AI-model

**Oplossing**: De voorbeelden bevatten `.logRequests(true)` en `.logResponses(true)` om API-aanroepen in de console te tonen. Dit helpt bij het oplossen van authenticatiefouten, snelheidslimieten of onverwachte antwoorden. Verwijder deze vlaggen in productie om logruis te verminderen.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI-vertalingsdienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel wij streven naar nauwkeurigheid, dient u er rekening mee te houden dat automatische vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het oorspronkelijke document in de originele taal wordt beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->