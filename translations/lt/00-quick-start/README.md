# Modulis 00: Greitas pradžia

## Turinys

- [Įvadas](../../../00-quick-start)
- [Kas yra LangChain4j?](../../../00-quick-start)
- [LangChain4j priklausomybės](../../../00-quick-start)
- [Išankstiniai reikalavimai](../../../00-quick-start)
- [Nustatymai](../../../00-quick-start)
  - [1. Gaukite savo GitHub tokeną](../../../00-quick-start)
  - [2. Nustatykite savo tokeną](../../../00-quick-start)
- [Vykdykite pavyzdžius](../../../00-quick-start)
  - [1. Pagrindinis pokalbis](../../../00-quick-start)
  - [2. Šablonų pavyzdžiai](../../../00-quick-start)
  - [3. Funkcijų kvietimas](../../../00-quick-start)
  - [4. Dokumentų klausimai ir atsakymai (Easy RAG)](../../../00-quick-start)
  - [5. Atsakingas AI](../../../00-quick-start)
- [Ką rodo kiekvienas pavyzdys](../../../00-quick-start)
- [Tolimesni žingsniai](../../../00-quick-start)
- [Klaidų taisymas](../../../00-quick-start)

## Įvadas

Šis greito pradžios vadovas skirtas kuo greičiau pradėti dirbti su LangChain4j. Jame aptariamos absoliučios pagrindai, kaip kurti AI programas su LangChain4j ir GitHub modeliais. Kitose moduliuose naudosite Azure OpenAI su LangChain4j, kad kurtumėte pažangesnes programas.

## Kas yra LangChain4j?

LangChain4j yra Java biblioteka, palengvinanti AI pagrįstų programų kūrimą. Vietoje to, kad dirbtumėte su HTTP klientais ir JSON analizavimu, jūs naudojate švarias Java API sąsajas.

Žodis "chain" LangChain reiškia kelių komponentų jungimą grandine – galite sujungti šabloną su modeliu ir su parseriu, arba surišti kelis AI kvietimus, kur vieno išvestis perduodama kitam įėjimui. Šis greitas pradžios vadovas orientuojasi į pagrindus prieš nagrinėjant sudėtingesnes grandines.

<img src="../../../translated_images/lt/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j komponentų jungimas grandine – statybiniai blokai sujungiami siekiant kurti galingas AI darbo eigos*

Naudosime tris pagrindinius komponentus:

**ChatModel** – sąsaja dirbti su AI modeliais. Paskambinkite `model.chat("prompt")` ir gaukite atsakymo eilutę. Mes naudojame `OpenAiOfficialChatModel`, kuris veikia su OpenAI suderinamais galiniais taškais, pvz., GitHub Modeliais.

**AiServices** – sukuria tipų saugias AI paslaugų sąsajas. Apibrėžkite metodus, pažymėkite juos `@Tool`, o LangChain4j pasirūpina orkestravimu. AI automatiškai kviečia jūsų Java metodus, kai reikia.

**MessageWindowChatMemory** – palaiko pokalbio istoriją. Be jo kiekvienas užklausimas yra nepriklausomas. Su juo AI prisimena ankstesnius pranešimus ir palaiko kontekstą per kelis atsakymus.

<img src="../../../translated_images/lt/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architektūra – pagrindiniai komponentai kartu veikia, kad suteiktų jūsų AI programoms galią*

## LangChain4j priklausomybės

Ši greito pradžios pamoka naudoja tris Maven priklausomybes faile [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulis `langchain4j-open-ai-official` suteikia klasę `OpenAiOfficialChatModel`, jungiančią prie OpenAI suderinamų API. GitHub Modeliai naudoja tą pačią API struktūrą, todėl nereikia jokio specialaus adapterio – tiesiog nurodykite bazinį URL kaip `https://models.github.ai/inference`.

Modulis `langchain4j-easy-rag` suteikia automatizuotą dokumentų skaidymą, įterpimą ir paiešką, todėl galite kurti RAG programas nereikia rankiniu būdu konfigūruoti kiekvieno žingsnio.

## Išankstiniai reikalavimai

**Naudojate Dev Container?** Java ir Maven jau įdiegtas. Reikia tik GitHub personalinio prieigos tokeno.

**Vietinis vystymas:**
- Java 21+, Maven 3.9+
- GitHub personalinio prieigos tokenas (instrukcijos žemiau)

> **Pastaba:** Šiame modulyje naudojamas `gpt-4.1-nano` iš GitHub Modelių. Nesikeiskite modelio pavadinimo kode – jis sukonfigūruotas dirbti su GitHub prieinamais modeliais.

## Nustatymai

### 1. Gaukite savo GitHub tokeną

1. Eikite į [GitHub nustatymai → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Spauskite "Generate new token"
3. Nustatykite vardą (pvz., "LangChain4j demo")
4. Nustatykite galiojimo laiką (rekomenduojama 7 dienos)
5. Skiltyje "Account permissions" raskite "Models" ir nustatykite kaip "Read-only"
6. Spauskite "Generate token"
7. Nukopijuokite ir išsaugokite tokeną – jo daugiau nematysite

### 2. Nustatykite savo tokeną

**1 variantas: Naudojant VS Code (rekomenduojama)**

Jei naudositės VS Code, pridėkite savo tokeną į `.env` failą projekto šaknyje:

Jei `.env` failas neegzistuoja, nukopijuokite `.env.example` į `.env` arba sukurkite naują `.env` failą projekto šaknyje.

**.env failo pavyzdys:**
```bash
# Faile /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Tada galite tiesiog dešiniuoju pelės mygtuku spustelėti bet kurį demonstracinį failą (pvz., `BasicChatDemo.java`) Explorer lange ir pasirinkti **"Run Java"** arba naudoti paleidimo konfigūracijas Run ir Debug skydelyje.

**2 variantas: Naudojant terminalą**

Nustatykite tokeną kaip aplinkos kintamąjį:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Vykdykite pavyzdžius

**Naudojant VS Code:** Tiesiog dešiniuoju pelės klavišu spustelėkite bet kurį demonstracinį failą Explorer lange ir pasirinkite **"Run Java"**, arba naudokite paleidimo konfigūracijas Run ir Debug skydelyje (įsitikinkite, kad jau pridėjote tokeną į `.env` failą).

**Naudojant Maven:** Taip pat galite paleisti iš komandų eilutės:

### 1. Pagrindinis pokalbis

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Šablonų pavyzdžiai

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Rodo zero-shot, few-shot, chain-of-thought ir role-based šablonus.

### 3. Funkcijų kvietimas

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI automatiškai kviečia jūsų Java metodus, kai reikia.

### 4. Dokumentų klausimai ir atsakymai (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Klauskite apie savo dokumentus naudodami Easy RAG su automatiniu įterpimu ir paieška.

### 5. Atsakingas AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Pamatykite, kaip AI saugumo filtrai blokuoja kenksmingą turinį.

## Ką rodo kiekvienas pavyzdys

**Pagrindinis pokalbis** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Pradėkite čia, kad pamatytumėte LangChain4j paprastą esmę. Sukursite `OpenAiOfficialChatModel`, išsiųsite šabloną su `.chat()`, ir gausite atsakymą. Tai parodo pagrindus: kaip inicializuoti modelius su pasirinktiniu galiniu tašku ir API raktu. Kai suprasite šį šabloną, viskas kita bus paremta juo.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ir paklauskite:
> - "Kaip pakeisiu iš GitHub Modelių į Azure OpenAI šiame kode?"
> - "Kokius kitus parametrus galiu konfigūruoti OpenAiOfficialChatModel.builder()?"
> - "Kaip pridėti srautinį atsakymą, o ne laukti viso atsakymo?"

**Šablonų inžinerija** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Dabar, kai žinote, kaip kalbėtis su modeliu, pažiūrėkime, ką jam sakote. Ši demonstracija naudoja tą patį modelį, bet rodo penkis skirtingus šablonus. Išbandykite zero-shot tiesioginius nurodymus, few-shot mokymąsi iš pavyzdžių, chain-of-thought parodos loginio mąstymo žingsnius ir role-based, nustatant kontekstą. Pamatysite, kaip tas pats modelis duoda dramatiškai skirtingus rezultatus, priklausomai nuo prašymo formulavimo.

Demonstracija taip pat parodo šablonų šablonus (prompt templates), kurie leidžia kurti pakartotinai naudojamus šablonus su kintamaisiais.
Žemiau pateiktas pavyzdys rodo šabloną naudojant LangChain4j `PromptTemplate` užpildant kintamuosius. AI atsakys remdamasis pateikta paskirties vieta ir veikla.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ir paklauskite:
> - "Kuo skiriasi zero-shot ir few-shot šablonai, ir kada naudoti kiekvieną?"
> - "Kaip temperatūros parametras veikia modelio atsakymus?"
> - "Kokios priemonės apsaugo nuo šablonų įterpimo (prompt injection) atakų gamyboje?"
> - "Kaip sukurti pakartotinai naudojamus PromptTemplate objektus dažnai naudojamiems šablonams?"

**Įrankių integracija** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Čia LangChain4j tampa galingas. Naudosite `AiServices` sukurti AI asistentą, kuris gali kviesti jūsų Java metodus. Tiesiog pažymėkite metodus `@Tool("aprašymas")` ir LangChain4j pasirūpina visa kita – AI automatiškai nusprendžia, kada naudoti kiekvieną įrankį, remiantis vartotojo klausimais. Tai demonstruoja funkcijų kvietimą, svarbią techniką kuriant AI, galinčius išlaikyti veiksmus, o ne tik atsakyti į klausimus.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ir paklauskite:
> - "Kaip veikia @Tool anotacija ir ką LangChain4j daro su ja užkulisiuose?"
> - "Ar AI gali kviesti kelis įrankius iš eilės, sprendžiant sudėtingas problemas?"
> - "Kas nutinka, jei įrankis meta išimtį – kaip turėčiau tvarkyti klaidas?"
> - "Kaip integruočiau realų API vietoje šio skaičiuoklio pavyzdžio?"

**Dokumentų klausimai ir atsakymai (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Čia matysite RAG (retrieval-augmented generation) naudojant LangChain4j "Easy RAG" metodą. Dokumentai įkelti, automatiškai suskaidyti ir įterpti į atmintinę skirtą saugojimui, o turinio paieškotojas pateikia aktualias dalis AI užklausos metu. AI atsako remdamasis jūsų dokumentais, o ne savo bendromis žiniomis.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ir paklauskite:
> - "Kaip RAG padeda išvengti AI haliucinacijų, palyginti su modelio mokymo duomenimis?"
> - "Kuo skiriasi šis lengvas metodas nuo specialaus RAG proceso?"
> - "Kaip masteliais pritaikysiu tai keliems dokumentams ar didesnėms žinių bazėms?"

**Atsakingas AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Kurkite AI saugumą gynybos sluoksniais. Ši demonstracija rodo du apsaugos sluoksnius, veikiančius kartu:

**1 dalis: LangChain4j įėjimo apsaugos (Input Guardrails)** – Blokuokite pavojingus šablonus prieš jie pasiekia LLM. Kurkite pasirinktines apsaugas, tikrinančias draudžiamus žodžius ar šablonus. Jos vykdomos jūsų kode, tad yra greitos ir nemokamos.

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

**2 dalis: Teikėjo saugumo filtrai** – GitHub Modeliai turi įmontuotus filtrus, kurie sugautų tai, ką apsaugos galėjo praleisti. Matysite griežtus blokavimus (HTTP 400 klaidas) rimtiems pažeidimams ir švelnius atsisakymus, kai AI mandagiai atsisako.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ir paklauskite:
> - "Kas yra InputGuardrail ir kaip sukurti savo?"
> - "Kuo skiriasi griežtas blokavimas nuo švelnaus atsisakymo?"
> - "Kodėl naudoti ir apsaugas, ir teikėjo filtrus kartu?"

## Tolimesni žingsniai

**Kitas modulis:** [01-introduction - Pradžia su LangChain4j ir gpt-5 Azure](../01-introduction/README.md)

---

**Navigacija:** [← Grįžti į pagrindinį](../README.md) | [Kitas: Modulis 01 - Įvadas →](../01-introduction/README.md)

---

## Klaidų taisymas

### Pirmas kartas Maven kompiluojant

**Problema:** Pirmas `mvn clean compile` arba `mvn package` užtrunka ilgai (10-15 minučių)

**Priežastis:** Maven pirmą kartą atsisiunčia visas projekto priklausomybes (Spring Boot, LangChain4j bibliotekas, Azure SDK ir t.t.).

**Sprendimas:** Tai normalu. Vėlesni kompiliavimai bus daug greitesni, nes priklausomybės yra kešuojamos lokaliai. Atsisiuntimo greitis priklauso nuo jūsų interneto ryšio.

### PowerShell Maven komandų sintaksės klaida

**Problema:** Maven komandos nepavyksta su klaida `Unknown lifecycle phase ".mainClass=..."`
**Priežastis**: PowerShell interpretuoja `=` kaip kintamojo priskyrimo operatorių, kuris pažeidžia Maven savybių sintaksę

**Sprendimas**: Naudokite stabdymo analizės operatorių `--%` prieš Maven komandą:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorius `--%` nurodo PowerShell perduoti visus likusius argumentus tiesiogiai Maven be interpretacijos.

### Windows PowerShell emocijų rodymas

**Problema**: AI atsakymai rodo šiukšlinį tekstą (pvz., `????` arba `â??`), o ne emocijas PowerShell

**Priežastis**: PowerShell numatytasis kodavimas nepalaiko UTF-8 emocijų

**Sprendimas**: Paleiskite šią komandą prieš vykdant Java programas:
```cmd
chcp 65001
```

Tai verčia terminalą naudoti UTF-8 koduotę. Alternatyviai naudokite Windows Terminal, kuris geriau palaiko Unicode.

### API kvietimų derinimas

**Problema**: Autentifikacijos klaidos, ribojimai ar netikėti AI modelio atsakymai

**Sprendimas**: Pavyzdžiuose įtrauktos `.logRequests(true)` ir `.logResponses(true)`, kad API kvietimai būtų matomi konsolėje. Tai padeda spręsti autentifikacijos klaidas, ribojimus ar netikėtus atsakymus. Produkcijoje pašalinkite šias parinktis, kad sumažintumėte žurnalų triukšmą.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:  
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų arba netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogaus atliktą vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingus aiškinimus, kurie gali kilti naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->