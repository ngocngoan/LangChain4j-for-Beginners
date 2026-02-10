# Modul 02: Prompt Engineering med GPT-5.2

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../02-prompt-engineering)
- [Forudsætninger](../../../02-prompt-engineering)
- [Forståelse af Prompt Engineering](../../../02-prompt-engineering)
- [Hvordan Dette Bruger LangChain4j](../../../02-prompt-engineering)
- [Kerne Mønstrene](../../../02-prompt-engineering)
- [Brug af Eksisterende Azure Ressourcer](../../../02-prompt-engineering)
- [Applikationsskærmbilleder](../../../02-prompt-engineering)
- [Udforskning af Mønstrene](../../../02-prompt-engineering)
  - [Lav vs Høj Iver](../../../02-prompt-engineering)
  - [Opgaveudførelse (Værktøjsintroduktioner)](../../../02-prompt-engineering)
  - [Selvreflekterende Kode](../../../02-prompt-engineering)
  - [Struktureret Analyse](../../../02-prompt-engineering)
  - [Multi-Turn Chat](../../../02-prompt-engineering)
  - [Trin-for-Trin Resonnement](../../../02-prompt-engineering)
  - [Begrænset Output](../../../02-prompt-engineering)
- [Hvad Du Virkelig Lærer](../../../02-prompt-engineering)
- [Næste Skridt](../../../02-prompt-engineering)

## Hvad Du Vil Lære

I det forrige modul så du, hvordan hukommelse muliggør konverserende AI og brugte GitHub Models til grundlæggende interaktioner. Nu fokuserer vi på, hvordan du stiller spørgsmål – selve promptene – ved brug af Azure OpenAI's GPT-5.2. Den måde du strukturerer dine prompts på påvirker kraftigt kvaliteten af de svar, du får.

Vi bruger GPT-5.2 fordi det introducerer reasoning control – du kan fortælle modellen, hvor meget tænkning den skal gøre før svar. Det gør forskellige prompt-strategier mere tydelige og hjælper dig med at forstå, hvornår du skal bruge hvilken tilgang. Vi vil også drage fordel af Azures færre ratebegrænsninger for GPT-5.2 sammenlignet med GitHub Models.

## Forudsætninger

- Modul 01 gennemført (Azure OpenAI ressourcer deployeret)
- `.env` fil i root-mappen med Azure legitimationsoplysninger (oprettet via `azd up` i Modul 01)

> **Bemærk:** Hvis du ikke har gennemført Modul 01, skal du følge deploymentsinstruktionerne der først.

## Forståelse af Prompt Engineering

Prompt engineering handler om at designe inputtekst, der konsekvent giver dig de ønskede resultater. Det handler ikke kun om at stille spørgsmål – det handler om at strukturere forespørgsler, så modellen præcist forstår, hvad du vil have, og hvordan det skal leveres.

Tænk på det som at give instrukser til en kollega. "Ret fejlen" er uklart. "Ret null pointer exception i UserService.java linje 45 ved at tilføje en null-check" er konkret. Sprogmodeller fungerer på samme måde – specificitet og struktur betyder noget.

## Hvordan Dette Bruger LangChain4j

Dette modul demonstrerer avancerede prompting-mønstre ved brug af den samme LangChain4j-base som i tidligere moduler, med fokus på promptstruktur og reasoning control.

<img src="../../../translated_images/da/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*Hvordan LangChain4j forbinder dine prompts til Azure OpenAI GPT-5.2*

**Afhængigheder** – Modul 02 bruger følgende langchain4j-afhængigheder defineret i `pom.xml`:
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

Chatmodellen konfigureres manuelt som en Spring bean ved brug af OpenAI Official-klienten, som understøtter Azure OpenAI endpoints. Den væsentlige forskel fra Modul 01 er, hvordan vi strukturerer prompts sendt til `chatModel.chat()`, ikke selve modelopsætningen.

**System- og Brugermeddelelser** – [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j adskiller beskedtyper for klarhed. `SystemMessage` sætter AI’ens adfærd og kontekst (som "Du er en kodeanmelder"), mens `UserMessage` indeholder den faktiske anmodning. Denne adskillelse gør det muligt at opretholde ensartet AI-adfærd på tværs af forskellige brugerforespørgsler.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/da/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage leverer vedvarende kontekst, mens UserMessages indeholder individuelle forespørgsler*

**MessageWindowChatMemory til Multi-Turn** – Til multi-turn samtalemønsteret genbruger vi `MessageWindowChatMemory` fra Modul 01. Hver session får sin egen hukommelsesinstans gemt i et `Map<String, ChatMemory>`, hvilket tillader flere samtidige samtaler uden kontekstblanding.

**Prompt Templates** – Det egentlige fokus her er prompt engineering, ikke nye LangChain4j API’er. Hvert mønster (lav iver, høj iver, opgaveudførelse osv.) bruger den samme `chatModel.chat(prompt)` metode, men med nøje strukturerede prompt-strenge. XML-tags, instruktioner og formatering er alle en del af prompt-teksten, ikke LangChain4j-funktioner.

**Reasoning Control** – GPT-5.2’s reasoning effort styres gennem prompt-instruktioner som "maximum 2 reasoning steps" eller "explore thoroughly". Dette er prompt engineering-teknikker, ikke LangChain4j-konfigurationer. Biblioteket leverer blot dine prompts til modellen.

Hovedpointen: LangChain4j leverer infrastrukturen (modelforbindelse via [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), hukommelse, beskedhåndtering via [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)), mens dette modul lærer dig, hvordan du laver effektive prompts inden for denne infrastruktur.

## Kerne Mønstrene

Ikke alle problemer kræver den samme tilgang. Nogle spørgsmål skal have hurtige svar, andre kræver dyb tænkning. Nogle behøver synligt resonnement, andre kun resultater. Dette modul dækker otte prompting-mønstre – hvert optimeret til forskellige scenarier. Du vil prøve alle for at lære, hvornår hver tilgang fungerer bedst.

<img src="../../../translated_images/da/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Oversigt over de otte prompt engineering-mønstre og deres anvendelsesområder*

<img src="../../../translated_images/da/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*Lav iver (hurtig, direkte) vs Høj iver (grundig, udforskende) resonnementstilgange*

**Lav Iver (Hurtig & Fokuseret)** – Til simple spørgsmål, hvor du vil have hurtige, direkte svar. Modellen laver minimal resonnement – maksimum 2 trin. Brug dette til beregninger, opslag eller ligetil spørgsmål.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **Udforsk med GitHub Copilot:** Åbn [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) og spørg:
> - "Hvad er forskellen mellem lav iver og høj iver prompting-mønstre?"
> - "Hvordan hjælper XML-tags i prompts med at strukturere AI’ens svar?"
> - "Hvornår skal jeg bruge selvrefleksionsmønstre kontra direkte instruktion?"

**Høj Iver (Dyb & Grundig)** – Til komplekse problemer, hvor du ønsker omfattende analyse. Modellen udforsker grundigt og viser detaljeret resonnement. Brug dette til systemdesign, arkitekturvalg eller avanceret forskning.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Opgaveudførelse (Trin-for-Trin Fremgang)** – Til workflows med flere trin. Modellen giver en indledende plan, fortæller hver enkelt trin under arbejdet og giver så en opsummering. Brug dette til migrationer, implementeringer eller enhver proces med flere trin.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting beder eksplicit modellen om at vise sin resoneringsproces, hvilket forbedrer nøjagtigheden for komplekse opgaver. Den trin-for-trin opdeling hjælper både mennesker og AI med at forstå logikken.

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om dette mønster:
> - "Hvordan skulle jeg tilpasse opgaveudførelsesmønstret til langvarige operationer?"
> - "Hvad er bedste praksis til at strukturere værktøjsintroduktioner i produktionsapplikationer?"
> - "Hvordan kan jeg fange og vise mellemliggende fremdriftsopdateringer i en brugerflade?"

<img src="../../../translated_images/da/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*Planlæg → Udfør → Opsummer workflow til opgaver med flere trin*

**Selvreflekterende Kode** – Til generering af produktionsklar kode. Modellen genererer kode, tjekker den mod kvalitetskriterier og forbedrer den iterativt. Brug dette ved opbygning af nye funktioner eller services.

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

<img src="../../../translated_images/da/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*Iterativ forbedringscyklus – generer, evaluer, identificer problemer, forbedr, gentag*

**Struktureret Analyse** – Til konsekvent evaluering. Modellen gennemgår kode med et fast framework (korrekthed, praksis, ydeevne, sikkerhed). Brug dette til kodeanmeldelser eller kvalitetsvurderinger.

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

> **🤖 Prøv med [GitHub Copilot](https://github.com/features/copilot) Chat:** Spørg om struktureret analyse:
> - "Hvordan kan jeg tilpasse analyseframeworket til forskellige typer kodeanmeldelser?"
> - "Hvad er bedste måde at parse og handle på struktureret output programmatisk?"
> - "Hvordan sikrer jeg konsekvente alvorlighedsniveauer på tværs af forskellige review-sessioner?"

<img src="../../../translated_images/da/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*Fire-kategori framework til konsekvente kodeanmeldelser med alvorlighedsniveauer*

**Multi-Turn Chat** – Til samtaler, der kræver kontekst. Modellen husker tidligere beskeder og bygger videre på dem. Brug dette til interaktive hjælpesessioner eller komplekse Q&A.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/da/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*Hvordan samtalekontekst opsamles over flere ture indtil tokens-grænsen nås*

**Trin-for-Trin Resonnement** – Til problemer, der kræver synlig logik. Modellen viser eksplicit resonnement for hvert trin. Brug dette til matematikopgaver, logikpuslespil eller når du skal forstå tænkeprocessen.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/da/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*Opdeling af problemer i eksplicitte logiske trin*

**Begrænset Output** – Til svar med specifikke formatkrav. Modellen følger strengt formaterings- og længde-regler. Brug dette til opsummeringer eller når du har brug for præcis outputstruktur.

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

<img src="../../../translated_images/da/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*Håndhævelse af specifikke format-, længde- og strukturkrav*

## Brug af Eksisterende Azure Ressourcer

**Verificer deployment:**

Sørg for, at `.env` filen eksisterer i root-mappen med Azure legitimationsoplysninger (oprettet under Modul 01):
```bash
cat ../.env  # Skal vise AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start applikationen:**

> **Bemærk:** Hvis du allerede startede alle applikationer med `./start-all.sh` fra Modul 01, kører dette modul allerede på port 8083. Du kan springe startkommandoerne over og gå direkte til http://localhost:8083.

**Mulighed 1: Brug Spring Boot Dashboard (Anbefalet til VS Code brugere)**

Dev containeren inkluderer Spring Boot Dashboard-udvidelsen, som giver en visuel grænseflade til at administrere alle Spring Boot-applikationer. Du finder den i Activity Bar til venstre i VS Code (se efter Spring Boot-ikonet).

Fra Spring Boot Dashboard kan du:
- Se alle tilgængelige Spring Boot-applikationer i workspace
- Starte/stoppe applikationer med et klik
- Se applikationslogfiler i realtid
- Overvåge applikationsstatus

Klik blot på play-knappen ved siden af "prompt-engineering" for at starte dette modul, eller start alle moduler på én gang.

<img src="../../../translated_images/da/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**Mulighed 2: Brug shell scripts**

Start alle webapplikationer (moduler 01-04):

**Bash:**
```bash
cd ..  # Fra rodmappen
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Fra rodkataloget
.\start-all.ps1
```

Eller start kun dette modul:

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

Begge scripts indlæser automatisk miljøvariabler fra root `.env` fil og vil bygge JAR-filerne, hvis de ikke findes.

> **Bemærk:** Hvis du foretrækker at bygge alle moduler manuelt før start:
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

Åbn http://localhost:8083 i din browser.

**Stop applikationen:**

**Bash:**
```bash
./stop.sh  # Kun dette modul
# Eller
cd .. && ./stop-all.sh  # Alle moduler
```

**PowerShell:**
```powershell
.\stop.ps1  # Kun denne modul
# Eller
cd ..; .\stop-all.ps1  # Alle moduler
```

## Applikationsskærmbilleder

<img src="../../../translated_images/da/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*Hoveddashboard, der viser alle 8 prompt engineering-mønstre med deres karakteristika og anvendelsestilfælde*

## Udforskning af Mønstrene

Webinterface’et lader dig eksperimentere med forskellige prompting-strategier. Hvert mønster løser forskellige problemer – prøv dem for at se, hvornår hver tilgang fungerer bedst.

### Lav vs Høj Iver

Stil et simpelt spørgsmål som "Hvad er 15% af 200?" med Lav Iver. Du får et øjeblikkeligt, direkte svar. Stil nu noget komplekst som "Design en caching-strategi for en højt trafikeret API" med Høj Iver. Se, hvordan modellen går langsommere til værks og giver detaljeret resonnement. Samme model, samme spørgsmålstruktur – men prompten fortæller, hvor meget tænkning der skal lægges i.
<img src="../../../translated_images/da/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*Hurtig beregning med minimal begrundelse*

<img src="../../../translated_images/da/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*Omfattende cache-strategi (2,8MB)*

### Opgaveudførelse (Værktøjsforord)

Flertrins arbejdsgange har fordel af forudgående planlægning og løbende beretning. Modellen skitserer, hvad den vil gøre, fortæller om hvert trin og opsummerer derefter resultaterne.

<img src="../../../translated_images/da/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Oprettelse af en REST-endpoint med trinvis beretning (3,9MB)*

### Selvreflekterende kode

Prøv "Opret en emailvalideringstjeneste". I stedet for bare at generere kode og stoppe, genererer modellen, evaluerer efter kvalitetskriterier, identificerer svagheder og forbedrer. Du vil se den iterere, indtil koden opfylder produktionsstandarder.

<img src="../../../translated_images/da/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*Fuldstændig emailvalideringstjeneste (5,2MB)*

### Struktureret analyse

Kodegennemgange kræver konsistente evalueringsrammer. Modellen analyserer kode ved hjælp af faste kategorier (korrekthed, praksis, ydeevne, sikkerhed) med alvorlighedsniveauer.

<img src="../../../translated_images/da/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*Rammebaseret kodegennemgang*

### Flerdelschat

Spørg "Hvad er Spring Boot?" og følg straks op med "Vis mig et eksempel". Modellen husker dit første spørgsmål og giver dig specifikt et Spring Boot-eksempel. Uden hukommelse ville det andet spørgsmål være for vagt.

<img src="../../../translated_images/da/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*Bevarelse af kontekst på tværs af spørgsmål*

### Trinvis begrundelse

Vælg et matematikproblem og prøv det med både Trinvis begrundelse og Lav iver. Lav iver giver dig bare svaret – hurtigt, men uigennemsigtigt. Trin-for-trin viser dig hver beregning og beslutning.

<img src="../../../translated_images/da/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*Matematikproblem med eksplicitte trin*

### Begrænset output

Når du har brug for specifikke formater eller ordantal, håndhæver dette mønster streng overholdelse. Prøv at generere et resumé med nøjagtigt 100 ord i punktopstillingsformat.

<img src="../../../translated_images/da/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*Maskinlæringsresumé med formatkontrol*

## Hvad du virkelig lærer

**Begrundelsesindsats ændrer alt**

GPT-5.2 lader dig styre den beregningsmæssige indsats via dine prompts. Lav indsats betyder hurtige svar med minimal udforskning. Høj indsats betyder, at modellen tager tid til at tænke dybt. Du lærer at matche indsats til opgavens kompleksitet – spild ikke tid på simple spørgsmål, men hast heller ikke dig for komplekse beslutninger.

**Struktur styrer adfærd**

Læg mærke til XML-tags i prompts? De er ikke dekorative. Modeller følger strukturerede instruktioner mere pålideligt end frit tekst. Når du har brug for flertrinsprocesser eller kompleks logik, hjælper struktur modellen med at holde styr på, hvor den er, og hvad der kommer næste.

<img src="../../../translated_images/da/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*Anatomi af en veldesignet prompt med klare sektioner og XML-lignende organisering*

**Kvalitet gennem selvevaluering**

De selvreflekterende mønstre virker ved at gøre kvalitetskriterier eksplicitte. I stedet for at håbe på, at modellen "gør det rigtigt", fortæller du den præcis, hvad "rigtigt" betyder: korrekt logik, fejlhåndtering, ydeevne, sikkerhed. Modellen kan så evaluere sit eget output og forbedre det. Det forvandler kodegenerering fra lotteri til en proces.

**Kontekst er begrænset**

Flerdels-samtaler fungerer ved at inkludere beskedhistorik med hver anmodning. Men der er en grænse – hver model har et maksimum antal tokens. Efterhånden som samtaler vokser, skal du have strategier for at bevare relevant kontekst uden at ramme loftet. Dette modul viser dig, hvordan hukommelse fungerer; senere lærer du, hvornår du skal opsummere, glemme og hente oplysninger.

## Næste skridt

**Næste modul:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Forrige: Modul 01 - Introduktion](../01-introduction/README.md) | [Tilbage til hovedmenu](../README.md) | [Næste: Modul 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, bedes du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det oprindelige dokument på modersmålet bør betragtes som den autoritative kilde. For kritiske informationer anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->