# Modulis 00: Greitas pradėjimas

## Turinys

- [Įvadas](../../../00-quick-start)
- [Kas yra LangChain4j?](../../../00-quick-start)
- [LangChain4j priklausomybės](../../../00-quick-start)
- [Reikalavimai](../../../00-quick-start)
- [Sąranka](../../../00-quick-start)
  - [1. Gaukite savo GitHub raktą](../../../00-quick-start)
  - [2. Nustatykite savo raktą](../../../00-quick-start)
- [Paleiskite pavyzdžius](../../../00-quick-start)
  - [1. Pagrindinis pokalbis](../../../00-quick-start)
  - [2. Prompto šablonai](../../../00-quick-start)
  - [3. Funkcijų iškvietimas](../../../00-quick-start)
  - [4. Dokumentų klausimai ir atsakymai (Easy RAG)](../../../00-quick-start)
  - [5. Atsakingas DI](../../../00-quick-start)
- [Ką rodo kiekvienas pavyzdys](../../../00-quick-start)
- [Kitos veiksmų gairės](../../../00-quick-start)
- [Trikčių šalinimas](../../../00-quick-start)

## Įvadas

Šis greitas pradžios vadovas skirtas kuo greičiau pradėti naudotis LangChain4j. Jame aptariami absoliučiai pagrindai, kaip kurti DI programas su LangChain4j ir GitHub Modeliais. Kitose moduliuose pereisite prie Azure OpenAI ir GPT-5.2 bei gilinsitės į kiekvieną koncepciją.

## Kas yra LangChain4j?

LangChain4j yra Java biblioteka, kuri supaprastina DI varomų programų kūrimą. Vietoje darbo su HTTP klientais ir JSON analizavimu, jūs dirbate su švariais Java API.

„Chain“ LangChain pavadinime reiškia kelių komponentų sujungimą – galite sujungti promptą su modeliu, parseriu ar sujungti kelis DI kvietimus, kur vienas išvestis tampa kito įvestimi. Šis greitas pradžios vadovas fokusuojasi į pagrindus, prieš pereinant prie sudėtingesnių grandinių.

<img src="../../../translated_images/lt/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Komponentų sujungimas LangChain4j – blokai jungiasi, kad sukurtų galingus DI darbo srautus*

Naudosime tris pagrindinius komponentus:

**ChatModel** – sąsaja DI modelio sąveikoms. Iškvieskite `model.chat("prompt")` ir gaukite atsakymą kaip eilutę. Naudojame `OpenAiOfficialChatModel`, kuris veikia su OpenAI suderinamais galais, tokiais kaip GitHub Modeliai.

**AiServices** – sukuria tipo saugias DI paslaugų sąsajas. Apibrėžkite metodus, paženklinkite juos `@Tool`, ir LangChain4j valdo orkestraciją. DI automatiškai kviečia jūsų Java metodus prireikus.

**MessageWindowChatMemory** – išlaiko pokalbio istoriją. Be to, kiekvienas užklausimas yra nepriklausomas. Su ja DI prisimena ankstesnius pranešimus ir palaiko kontekstą per kelis etapus.

<img src="../../../translated_images/lt/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architektūra – pagrindiniai komponentai kartu suteikia jūsų DI programoms galią*

## LangChain4j priklausomybės

Šis greitas pradžios vadovas naudoja tris Maven priklausomybes [`pom.xml`](../../../00-quick-start/pom.xml):

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

Modulis `langchain4j-open-ai-official` suteikia klasę `OpenAiOfficialChatModel`, kuri jungiasi prie OpenAI suderinamų API. GitHub Modeliai naudoja tą patį API formatą, tad nereikia specialaus adapterio – tiesiog nurodykite bazinį URL `https://models.github.ai/inference`.

Modulis `langchain4j-easy-rag` suteikia automatinį dokumentų skaidymą, įterpimą ir traukimą, kad galėtumėte kurti RAG programas be rankinio kiekvieno žingsnio konfigūravimo.

## Reikalavimai

**Naudojate Dev konteinerį?** Java ir Maven jau įdiegti. Jums reikia tik GitHub Asmeninio Prieigos Rakto.

**Vietiniam kūrimui:**
- Java 21+, Maven 3.9+
- GitHub Asmeninis Prieigos Raktas (instrukcijos žemiau)

> **Pastaba:** Šiame modulyje naudojamas `gpt-4.1-nano` GitHub Modelių modelis. Nekeiskite modelio pavadinimo kode – jis nustatytas dirbti su GitHub turimais modeliais.

## Sąranka

### 1. Gaukite savo GitHub raktą

1. Eikite į [GitHub Nustatymai → Asmeniniai prieigos raktai](https://github.com/settings/personal-access-tokens)
2. Paspauskite „Sukurti naują raktą“
3. Nustatykite aprašomą pavadinimą (pvz., "LangChain4j Demo")
4. Nustatykite galiojimo laiką (rekomenduojama 7 dienos)
5. Skiltyje „Sąskaitos leidimai“ raskite „Models“ ir nustatykite „Tik skaitymas“
6. Spauskite „Sukurti raktą“
7. Nukopijuokite ir išsaugokite raktą – jo daugiau nepamatysite

### 2. Nustatykite savo raktą

**1 pasirinkimas: Naudojant VS Code (rekomenduojama)**

Jei naudojate VS Code, pridėkite savo raktą `.env` faile projekto šaknyje:

Jei `.env` failo nėra, nukopijuokite `.env.example` į `.env` arba sukurkite naują `.env` failą projekto šaknyje.

**Pavyzdys `.env` failo:**
```bash
# Failas /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Tuomet galite tiesiog dešiniuoju pelės mygtuku spustelėti bet kurį demonstracinį failą (pvz., `BasicChatDemo.java`) naršyklėje ir pasirinkti **„Run Java“** arba naudoti paleidimo konfigūracijas Run and Debug skydelyje.

**2 pasirinkimas: Naudojant terminalą**

Nustatykite raktą kaip aplinkos kintamąjį:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Paleiskite pavyzdžius

**Naudojant VS Code:** Tiesiog dešiniuoju pelės mygtuku spustelėkite bet kurį demonstracinį failą naršyklėje ir pasirinkite **„Run Java“**, arba naudokite paleidimo konfigūracijas Run and Debug skydelyje (įsitikinkite, kad iš pradžių pridėjote savo raktą į `.env` failą).

**Naudojant Maven:** Taip pat galite paleisti komandinėje eilutėje:

### 1. Pagrindinis pokalbis

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompto šablonai

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Rodo zero-shot, few-shot, chain-of-thought ir roliniu pagrindu paremtus promptus.

### 3. Funkcijų iškvietimas

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

DI automatiškai kviečia jūsų Java metodus prireikus.

### 4. Dokumentų klausimai ir atsakymai (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Užduokite klausimus apie savo dokumentus naudodami Easy RAG su automatiniu įterpimu ir traukimu.

### 5. Atsakingas DI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Žiūrėkite, kaip DI saugumo filtrai blokuoja kenksmingą turinį.

## Ką rodo kiekvienas pavyzdys

**Pagrindinis pokalbis** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Pradėkite čia, kad pamatytumėte LangChain4j paprastumą. Sukursite `OpenAiOfficialChatModel`, išsiųsite promptą su `.chat()` ir gausite atsakymą. Tai rodo pagrindą: kaip inicijuoti modelius su pasirinktais galais ir API raktais. Supratus šį modelį, visa kita statoma remiantis juo.

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
> - „Kaip pakeisčiau šį kodą, kad naudotų GitHub Modelius vietoje Azure OpenAI?“
> - „Kokius kitus parametrus galiu konfigūruoti OpenAiOfficialChatModel.builder()?“
> - „Kaip pridėti transliuojamus atsakymus vietoje laukimo viso atsakymo?“

**Prompto inžinerija** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Dabar, kai žinote, kaip bendrauti su modeliu, pažvelkime, ką jam sakote. Ši demonstracija naudoja tą patį modelio nustatymą, bet rodo penkis skirtingus promptų šablonus. Išbandykite zero-shot promptus tiesioginiams nurodymams, few-shot promptus, kurie mokosi iš pavyzdžių, chain-of-thought promptus, kurie atskleidžia loginio mąstymo žingsnius, ir rolinių vaidmenų promptus, nustatančius kontekstą. Matysite, kaip tas pats modelis duoda drastiškai skirtingus rezultatus, priklausomai nuo to, kaip formuluojate užklausą.

Demonstracija taip pat rodo promptų šablonus, kurie yra galingas būdas kurti pakartotinai naudojamus promptus su kintamaisiais.
Toliau pateiktas pavyzdys naudoja LangChain4j `PromptTemplate`, kad užpildytų kintamuosius. DI atsakys remdamasis nurodyta paskirties vieta ir veikla.

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
> - „Kuo skiriasi zero-shot ir few-shot promptai ir kada naudoti kiekvieną?“
> - „Kaip temperatūros parametras veikia modelių atsakymus?“
> - „Kokios yra technikos, kad išvengti promptų injekcijos atakų gamyboje?“
> - „Kaip sukurti pakartotinai naudojamus PromptTemplate objektus dažniausiai naudojamiems šablonams?“

**Įrankių integracija** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Čia LangChain4j tampa galingas. Naudosite `AiServices`, kad sukurtumėte DI pagalbininką, kuris gali iškvietinėti jūsų Java metodus. Tiesiog paženklinkite metodus `@Tool("aprašymas")` ir LangChain4j rūpinasi likusiu – DI automatiškai nusprendžia, kada naudoti kiekvieną įrankį, pagal tai, ko vartotojas paprašo. Tai demonstruoja funkcijų iškvietimą – svarbią techniką kurti DI, kuris gali imtis veiksmų, o ne tik atsakyti į klausimus.

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
> - „Kaip veikia @Tool anotacija ir ką LangChain4j daro su ja užkulisiuose?“
> - „Ar DI gali iškviesti kelis įrankius iš eilės, kad išspręstų sudėtingas problemas?“
> - „Kas atsitinka, jei įrankis meta išimtį – kaip turėčiau tvarkyti klaidas?“
> - „Kaip integruočiau tikrą API vietoje šio skaičiuotuvo pavyzdžio?“

**Dokumentų klausimai ir atsakymai (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Čia matysite RAG (retrieval-augmented generation) naudojant LangChain4j „Easy RAG“ metodą. Dokumentai įkeliami, automatiškai suskaidomi ir įterpiami į atminties saugyklą, tada turinio paieškos įrankis pateikia susijusius fragmentus DI užklausos metu. DI atsako remdamasis jūsų dokumentais, o ne bendromis žiniomis.

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
> - „Kaip RAG sumažina DI haliucinacijas, palyginti su modelio mokymo duomenimis?“
> - „Kuo skiriasi šis paprastas metodas nuo individualaus RAG vamzdyno?“
> - „Kaip išplėsti tai keliems dokumentams ar didesnėms žinių bazėms?“

**Atsakingas DI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Statykite DI saugumą gynybiniu sluoksniu. Ši demonstracija rodo dvi apsaugos sluoksnio funkcijas kartu:

**1 dalis: LangChain4j naudojimo ribojimai (Input Guardrails)** – Blokuoja pavojingus promptus prieš jiems pasiekiant LLM. Sukurkite savo ribojimus, kurie tikrina uždraustus raktinius žodžius ar šablonus. Jie veikia jūsų kode, todėl greiti ir nemokami.

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

**2 dalis: Tiekėjo saugumo filtrai** – GitHub Modeliai turi įmontuotus filtrus, kurie pagauna tai, ką jūsų ribojimai gali praleisti. Matysite griežtus blokus (HTTP 400 klaidas) už rimtus pažeidimus ir švelnius atsisakymus, kai DI mandagiai atsisako.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) Chat:** Atidarykite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ir paklauskite:
> - „Kas yra InputGuardrail ir kaip sukurti savo?“
> - „Kuo skiriasi griežtas užblokavimas nuo švelnaus atsisakymo?“
> - „Kodėl naudoti ir ribojimus, ir tiekėjo filtrus kartu?“

## Kitos veiksmų gairės

**Kitas modulis:** [01-introduction - Pradžia su LangChain4j](../01-introduction/README.md)

---

**Navigacija:** [← Grįžti į pagrindinį](../README.md) | [Kitas: Modulis 01 - Įvadas →](../01-introduction/README.md)

---

## Trikčių šalinimas

### Pirmas Maven sukūrimas

**Problema:** Pirmas `mvn clean compile` arba `mvn package` vykdymas užtrunka (10-15 minučių)

**Priežastis:** Maven pirmą kartą turi atsisiųsti visas projekto priklausomybes (Spring Boot, LangChain4j bibliotekas, Azure SDK ir kt.).

**Sprendimas:** Tai normalu. Tolimesni kūrimai bus ženkliai greitesni, nes priklausomybės bus talpinamos vietoje. Atsisiuntimo laikas priklauso nuo jūsų tinklo greičio.

### PowerShell Maven komandos sintaksės klaida

**Problema:** Maven komandos nepavyksta su klaida `Unknown lifecycle phase ".mainClass=..."`
**Priežastis**: PowerShell interpretuoja `=` kaip kintamojo priskyrimo operatorių, todėl Maven savybių sintaksė sugadinama

**Sprendimas**: Naudokite sustabdymo interpretavimą operatorių `--%` prieš Maven komandą:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorius `--%` nurodo PowerShell perduoti visas likusias argumentų dalis pažodžiui Maven be interpretacijos.

### Windows PowerShell emoji rodymas

**Problema**: AI atsakymai PowerShell rodomi kaip nesuprantami simboliai (pvz., `????` arba `â??`) vietoje emoji

**Priežastis**: PowerShell numatytoji koduotė nepalaiko UTF-8 emoji

**Sprendimas**: Paleiskite šią komandą prieš vykdant Java programas:
```cmd
chcp 65001
```

Tai privers terminalą naudoti UTF-8 koduotę. Alternatyviai naudokite Windows Terminal, kuris geriau palaiko Unicode.

### API kvietimų derinimas

**Problema**: Autentifikacijos klaidos, užklausų limitai arba netikėti AI modelio atsakymai

**Sprendimas**: Pavyzdžiai naudoja `.logRequests(true)` ir `.logResponses(true)`, kad parodytų API kvietimus konsolėje. Tai padeda tirti autentifikacijos klaidas, užklausų limitus ar netikėtus atsakymus. Pašalinkite šias parinktis gamybai, kad sumažintumėte žurnalų triukšmą.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatizuoti vertimai gali turėti klaidų arba netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Esant svarbiai informacijai, rekomenduojamas profesionalus žmogaus vertimas. Mes neatsakome už bet kokius nesusipratimus ar klaidingus interpretavimus, atsiradusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->