# Modul 02: Prompt Engineering med GPT-5.2

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-prompt-engineering)
- [Förkunskaper](../../../02-prompt-engineering)
- [Förstå Prompt Engineering](../../../02-prompt-engineering)
- [Hur detta använder LangChain4j](../../../02-prompt-engineering)
- [De grundläggande mönstren](../../../02-prompt-engineering)
- [Använda befintliga Azure-resurser](../../../02-prompt-engineering)
- [Applikationsskärmdumpar](../../../02-prompt-engineering)
- [Utforska mönstren](../../../02-prompt-engineering)
  - [Låg vs Hög ivrighet](../../../02-prompt-engineering)
  - [Uppgiftsutförande (Verktygsintroduktioner)](../../../02-prompt-engineering)
  - [Självreflekterande kod](../../../02-prompt-engineering)
  - [Strukturerad analys](../../../02-prompt-engineering)
  - [Flergångschatt](../../../02-prompt-engineering)
  - [Steg-för-steg-resonemang](../../../02-prompt-engineering)
  - [Begränsad utdata](../../../02-prompt-engineering)
- [Vad du egentligen lär dig](../../../02-prompt-engineering)
- [Nästa steg](../../../02-prompt-engineering)

## Vad du kommer att lära dig

I föregående modul såg du hur minne möjliggör konverserande AI och använde GitHub-modeller för grundläggande interaktioner. Nu fokuserar vi på hur du formulerar frågor – själva promptsen – med Azure OpenAIs GPT-5.2. Hur du strukturerar dina prompts påverkar dramatiskt kvaliteten på de svar du får.

Vi använder GPT-5.2 eftersom den introducerar styrning av resonemang – du kan säga till modellen hur mycket tänkande den ska göra innan den svarar. Detta gör olika prompt-strategier tydligare och hjälper dig förstå när du ska använda vilken metod. Vi drar också nytta av Azures färre gränser för GPT-5.2 jämfört med GitHub-modeller.

## Förkunskaper

- Genomgången Modul 01 (Azure OpenAI-resurser distribuerade)
- `.env`-fil i rotkatalogen med Azure-behörigheter (skapad med `azd up` i Modul 01)

> **Notera:** Om du inte har genomfört Modul 01, följ distributionens instruktioner där först.

## Förstå Prompt Engineering

Prompt engineering handlar om att utforma indata-text som konsekvent ger dig de resultat du behöver. Det handlar inte bara om att ställa frågor – utan om att strukturera förfrågningar så att modellen exakt förstår vad du vill ha och hur det ska levereras.

Tänk på det som att ge instruktioner till en kollega. "Fix the bug" är vagt. "Fix the null pointer exception i UserService.java rad 45 genom att lägga till en null-kontroll" är specifikt. Språkmodeller fungerar på samma sätt – specificitet och struktur är viktiga.

## Hur detta använder LangChain4j

Denna modul visar avancerade promptmönster med samma LangChain4j-grund som i tidigare moduler, med fokus på promptstruktur och resonemangsstyrning.

<img src="../../../translated_images/sv/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Hur LangChain4j kopplar dina prompts till Azure OpenAI GPT-5.2*

**Beroenden** – Modul 02 använder följande langchain4j-beroenden definierade i `pom.xml`:  
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
  
**OpenAiOfficialChatModel Konfiguration** – [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chatmodellen konfigureras manuellt som en Spring-bean med OpenAI Official-klienten, som stödjer Azure OpenAI-endpoints. Skillnaden mot Modul 01 är hur vi strukturerar promptsen som skickas till `chatModel.chat()`, inte själva modellinställningen.

**System- och användarmeddelanden** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j skiljer på meddelandetyper för tydlighet. `SystemMessage` sätter AI:ns beteende och kontext (som "Du är en kodgranskare"), medan `UserMessage` innehåller själva förfrågan. Denna separation låter dig bibehålla konsekvent AI-beteende för olika användarfrågor.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/sv/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage ger beständig kontext medan UserMessages innehåller individuella förfrågningar*

**MessageWindowChatMemory för Flergång** – För flergångssamtal återanvänder vi `MessageWindowChatMemory` från Modul 01. Varje session får en egen minnesinstans som sparas i en `Map<String, ChatMemory>`, vilket möjliggör flera samtidiga konversationer utan sammanblandning av kontext.

**Promptmallar** – Huvudfokus här är prompt engineering, inte nya LangChain4j-API:er. Varje mönster (låg ivrighet, hög ivrighet, uppgiftsutförande etc.) använder samma `chatModel.chat(prompt)`-metod men med noggrant strukturerade promptsträngar. XML-taggar, instruktioner och formatering är alla en del av prompttexten, inte LangChain4j-funktioner.

**Resonemangsstyrning** – GPT-5.2:s resonemangsinsats styrs via promptinstruktioner som "maximalt 2 resonemangssteg" eller "undersök grundligt". Detta är prompt engineering-tekniker, inte LangChain4j-konfigurationer. Biblioteket levererar helt enkelt dina prompts till modellen.

Huvudpoängen: LangChain4j tillhandahåller infrastrukturen (modellanslutning via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), minne, meddelandehantering via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), medan denna modul lär dig hur du skapar effektiva prompts inom den infrastrukturen.

## De grundläggande mönstren

Inte alla problem kräver samma angreppssätt. Några frågor behöver snabba svar, andra kräver djup eftertanke. Vissa behöver synligt resonemang, andra bara resultat. Denna modul täcker åtta promptmönster – var och ett optimerat för olika scenarier. Du kommer att experimentera med alla för att lära dig när varje metod fungerar bäst.

<img src="../../../translated_images/sv/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Översikt över de åtta prompt engineering-mönstren och deras användningsområden*

<img src="../../../translated_images/sv/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Låg ivrighet (snabb, direkt) vs Hög ivrighet (grundlig, explorativ) resonemangsmetoder*

**Låg ivrighet (Snabb & Fokuserad)** – För enkla frågor där du vill ha snabba, direkta svar. Modellen gör minimalt resonemang – max 2 steg. Använd detta för beräkningar, uppslag eller enkla frågor.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **Utforska med GitHub Copilot:** Öppna [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) och fråga:  
> - "Vad är skillnaden mellan låg och hög ivrighet i prompting-mönster?"  
> - "Hur hjälper XML-taggar i prompts att strukturera AI:s svar?"  
> - "När ska jag använda självreflektionsmönster kontra direkt instruktion?"

**Hög ivrighet (Djup & Grundlig)** – För komplexa problem där du vill ha omfattande analys. Modellen undersöker noggrant och visar detaljerat resonemang. Använd detta för systemdesign, arkitekturval eller komplex forskning.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**Uppgiftsutförande (Steg-för-steg-förlopp)** – För flerstegsarbetsflöden. Modellen ger en plan i förväg, beskriver varje steg medan det utförs, och avslutar med en sammanfattning. Använd detta för migreringar, implementationer eller andra flerstegsprocesser.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
Chain-of-Thought prompting ber modellen visa sitt resonemangsprocess, vilket förbättrar noggrannheten för komplexa uppgifter. Steg-för-steg-upplösningen hjälper både människor och AI att förstå logiken.

> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om detta mönster:  
> - "Hur skulle jag anpassa uppgiftsutförandemönstret för långvariga operationer?"  
> - "Vilka är bästa praxis för att strukturera verktygsintroduktioner i produktionsapplikationer?"  
> - "Hur kan jag fånga och visa uppdateringar om delprogress i ett UI?"

<img src="../../../translated_images/sv/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planera → Utför → Sammanfatta arbetsflöde för flerstegsuppgifter*

**Självreflekterande kod** – För att generera produktionsfärdig kod. Modellen skapar kod, kontrollerar den mot kvalitetskriterier och förbättrar den iterativt. Använd detta vid nyutveckling av funktioner eller tjänster.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sv/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ förbättringscykel – generera, utvärdera, identifiera problem, förbättra, upprepa*

**Strukturerad analys** – För konsekvent utvärdering. Modellen granskar kod enligt ett fast ramverk (korrekthet, praxis, prestanda, säkerhet). Använd detta för kodgranskningar eller kvalitetsbedömningar.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 Prova med [GitHub Copilot](https://github.com/features/copilot) Chat:** Fråga om strukturerad analys:  
> - "Hur kan jag anpassa analysramverket för olika typer av kodgranskningar?"  
> - "Vad är bästa metoden för att programatiskt tolka och agera på strukturerad utdata?"  
> - "Hur säkerställer jag konsekventa allvarlighetsnivåer över olika granskningssessioner?"

<img src="../../../translated_images/sv/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Fyra-kategoris ramverk för konsekventa kodgranskningar med allvarlighetsnivåer*

**Flergångschatt** – För konversationer som behöver kontext. Modellen minns tidigare meddelanden och bygger vidare på dem. Använd detta för interaktiva hjälpsessioner eller komplexa Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/sv/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hur samtalskontext ackumuleras över flera interaktioner tills tokengränsen nås*

**Steg-för-steg-resonemang** – För problem som kräver synlig logik. Modellen visar tydligt resonemang i varje steg. Använd detta för matteproblem, logikpussel eller när du vill förstå tankeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sv/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Bryta ner problem i explicita logiska steg*

**Begränsad utdata** – För svar med specifika formatkrav. Modellen följer strikt format- och längdregler. Använd detta för sammanfattningar eller när du behöver exakt outputstruktur.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/sv/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Tvingande av specifika format-, längd- och strukturkrav*

## Använda befintliga Azure-resurser

**Verifiera distributionen:**

Säkerställ att `.env`-filen finns i rotkatalogen med Azure-behörigheter (skapad under Modul 01):  
```bash
cat ../.env  # Bör visa AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```
  
**Starta applikationen:**

> **Notera:** Om du redan startade alla applikationer med `./start-all.sh` från Modul 01, kör denna modul redan på port 8083. Du kan hoppa över startkommandona nedan och gå direkt till http://localhost:8083.

**Alternativ 1: Använda Spring Boot Dashboard (Rekommenderas för VS Code-användare)**

Dev-containern inkluderar Spring Boot Dashboard-tillägget, som ger ett visuellt gränssnitt för att hantera alla Spring Boot-appar. Du hittar det i Aktivitetsfältet till vänster i VS Code (leta efter Spring Boot-ikonen).

Från Spring Boot Dashboard kan du:  
- Se alla tillgängliga Spring Boot-appar i arbetsytan  
- Starta/stoppa appar med ett klick  
- Se applikationsloggar i realtid  
- Övervaka applikationsstatus

Klicka helt enkelt på play-knappen bredvid "prompt-engineering" för att starta denna modul, eller starta alla moduler på en gång.

<img src="../../../translated_images/sv/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Alternativ 2: Använda shell-skript**

Starta alla webbapplikationer (moduler 01-04):

**Bash:**  
```bash
cd ..  # Från rotkatalogen
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # Från rotkatalogen
.\start-all.ps1
```
  
Eller starta bara denna modul:

**Bash:**  
```bash
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
Båda skripten läser automatiskt miljövariabler från rotens `.env`-fil och bygger JAR-filer om de saknas.

> **Notera:** Om du föredrar att manuellt bygga alla moduler innan start:  
>  
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
Öppna http://localhost:8083 i din webbläsare.

**För att stoppa:**

**Bash:**  
```bash
./stop.sh  # Endast denna modul
# Eller
cd .. && ./stop-all.sh  # Alla moduler
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # Endast denna modul
# Eller
cd ..; .\stop-all.ps1  # Alla moduler
```
  
## Applikationsskärmdumpar

<img src="../../../translated_images/sv/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Huvuddashboard som visar alla 8 prompt engineering-mönster med deras egenskaper och användningsområden*

## Utforska mönstren

Webbgränssnittet låter dig experimentera med olika prompting-strategier. Varje mönster löser olika problem – prova dem för att se när varje tillvägagångssätt fungerar bäst.

### Låg vs Hög ivrighet

Ställ en enkel fråga som "Vad är 15% av 200?" med låg ivrighet. Du får ett omedelbart, direkt svar. Ställ nu något komplext som "Designa en caching-strategi för en högtrafikerad API" med hög ivrighet. Se hur modellen saktar ner och ger detaljerat resonemang. Samma modell, samma frågestruktur – men prompten talar om för den hur mycket tänkande som ska göras.
<img src="../../../translated_images/sv/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Snabb beräkning med minimal resonemang*

<img src="../../../translated_images/sv/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattande cache-strategi (2.8MB)*

### Utförande av uppgifter (Verktygspreamblar)

Flerstegsarbetsflöden gynnas av planering i förväg och progressberättande. Modellen beskriver vad den kommer att göra, berättar steg för steg och sammanfattar sedan resultaten.

<img src="../../../translated_images/sv/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Skapar en REST-endpoint med steg-för-steg-berättande (3.9MB)*

### Självreflekterande kod

Prova "Skapa en e-postvalideringstjänst". Istället för att bara generera kod och stanna, genererar modellen, utvärderar mot kvalitetskriterier, identifierar svagheter och förbättrar. Du kommer att se hur den itererar tills koden uppfyller produktionsstandarder.

<img src="../../../translated_images/sv/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fullständig e-postvalideringstjänst (5.2MB)*

### Strukturerad analys

Kodgranskningar behöver konsekventa utvärderingsramar. Modellen analyserar kod med fasta kategorier (korrekthet, praxis, prestanda, säkerhet) med allvarlighetsnivåer.

<img src="../../../translated_images/sv/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Ramverksbaserad kodgranskning*

### Flerspårs-chatt

Fråga "Vad är Spring Boot?" och följ sedan omedelbart upp med "Visa mig ett exempel". Modellen kommer ihåg din första fråga och ger dig ett specifikt Spring Boot-exempel. Utan minne vore den andra frågan för vag.

<img src="../../../translated_images/sv/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarande av kontext mellan frågor*

### Steg-för-steg resonemang

Välj ett matematiskt problem och prova med både Steg-för-steg resonemang och Låg ivrighet. Låg ivrighet ger dig bara svaret - snabbt men otydligt. Steg-för-steg visar varje beräkning och beslut.

<img src="../../../translated_images/sv/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matteproblem med tydliga steg*

### Begränsad utdata

När du behöver specifika format eller ordantal, säkerställer detta mönster strikt efterlevnad. Prova att generera en sammanfattning med exakt 100 ord i punktform.

<img src="../../../translated_images/sv/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskininlärningssammanfattning med formatkontroll*

## Vad du verkligen lär dig

**Resonerandets ansträngning förändrar allt**

GPT-5.2 låter dig styra beräkningsinsatsen via dina promptar. Låg ansträngning innebär snabba svar med minimal utforskning. Hög ansträngning innebär att modellen tar tid att tänka djupt. Du lär dig att matcha ansträngning med uppgiftens komplexitet – slösa inte tid på enkla frågor, men skynda inte heller igenom komplexa beslut.

**Struktur styr beteende**

Lägger du märke till XML-taggarna i promptarna? De är inte dekorativa. Modeller följer strukturerade instruktioner mer tillförlitligt än fri text. När du behöver flerstegprocesser eller komplex logik, hjälper struktur modellen att hålla reda på var den är och vad som kommer härnäst.

<img src="../../../translated_images/sv/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi av en välstrukturerad prompt med tydliga sektioner och XML-stilorganisation*

**Kvalitet genom självvärdering**

De självreflekterande mönstren fungerar genom att göra kvalitetskriterier explicita. Istället för att hoppas att modellen "gör rätt", säger du exakt vad "rätt" betyder: korrekt logik, felhantering, prestanda, säkerhet. Modellen kan sedan utvärdera sin egen produktion och förbättra sig. Detta förvandlar kodgenerering från lotteri till en process.

**Kontext är ändlig**

Flerspårs-konversationer fungerar genom att inkludera meddelandehistorik vid varje förfrågan. Men det finns en gräns – varje modell har ett maximalt antal tokens. När konversationer växer behöver du strategier för att behålla relevant kontext utan att nå taket. Den här modulen visar hur minnet fungerar; senare lär du dig när du ska sammanfatta, när du ska glömma och när du ska hämta.

## Nästa steg

**Nästa modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigering:** [← Föregående: Modul 01 - Introduktion](../01-introduction/README.md) | [Tillbaka till huvudmenyn](../README.md) | [Nästa: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, vänligen var medveten om att automatiska översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår från användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->