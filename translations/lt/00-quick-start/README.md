# Module 00: Greitas pradėjimas

## Turinys

- [Įvadas](../../../00-quick-start)
- [Kas yra LangChain4j?](../../../00-quick-start)
- [LangChain4j priklausomybės](../../../00-quick-start)
- [Reikalavimai](../../../00-quick-start)
- [Nustatymas](../../../00-quick-start)
  - [1. Gaukite savo GitHub tokeną](../../../00-quick-start)
  - [2. Nustatykite savo tokeną](../../../00-quick-start)
- [Paleiskite pavyzdžius](../../../00-quick-start)
  - [1. Pagrindinis pokalbis](../../../00-quick-start)
  - [2. Užklausų šablonai](../../../00-quick-start)
  - [3. Funkcijų kvietimas](../../../00-quick-start)
  - [4. Dokumentų klausimai ir atsakymai (RAG)](../../../00-quick-start)
  - [5. Atsakingas dirbtinis intelektas](../../../00-quick-start)
- [Ką parodo kiekvienas pavyzdys](../../../00-quick-start)
- [Tolimesni žingsniai](../../../00-quick-start)
- [Problemų sprendimas](../../../00-quick-start)

## Įvadas

Šis greitasis pradžios vadovas skirtas kuo greičiau pradėti naudoti LangChain4j. Jis apima pačius pagrindus, kaip kurti DI programas su LangChain4j ir GitHub modeliais. Kitose moduliuose naudosite Azure OpenAI su LangChain4j, kad kurtumėte pažangesnes programas.

## Kas yra LangChain4j?

LangChain4j yra Java biblioteka, kuri supaprastina DI pagrįstų programų kūrimą. Užuot dirbę su HTTP klientais ir JSON analizavimu, jūs dirbate su švariais Java API.

„Chain“ LangChain pavadinime reiškia kelių komponentų sujungimą – galite sujungti užklausą su modeliu ir su analizatoriumi arba sujungti kelis DI kvietimus, kur vieno išvestis pateikiama kitam įvesti. Šis greitasis pradžios vadovas sutelktas į pagrindus, prieš pradedant tyrinėti sudėtingesnes jungtis.

<img src="../../../translated_images/lt/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Komponentų sujungimas LangChain4j – blokai sujungiami, kad sukurtų galingus DI veiklos scenarijus*

Naudosime tris pagrindinius komponentus:

**ChatLanguageModel** – sąsaja DI modelio sąveikoms. Iškvieskite `model.chat("prompt")` ir gaukite atsakymo eilutę. Naudojame `OpenAiOfficialChatModel`, kuris veikia su OpenAI suderinamais galiniais taškais, tokiais kaip GitHub modeliai.

**AiServices** – sukuria tipui saugias DI paslaugų sąsajas. Apibrėžkite metodus, pažymėkite juos `@Tool` ir LangChain4j koordinuos veiksmus. DI automatiškai kvies jūsų Java metodus pagal poreikį.

**MessageWindowChatMemory** – palaiko pokalbio istoriją. Be jos kiekvienas užklausimas yra nepriklausomas. Su ja DI prisimena ankstesnes žinutes ir palaiko kontekstą per kelis pokalbio etapus.

<img src="../../../translated_images/lt/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architektūra – svarbiausi komponentai veikiantys kartu, kad įgalintų jūsų DI programas*

## LangChain4j priklausomybės

Šiame greitojo paleidimo vadove naudojamos dvi Maven priklausomybės [`pom.xml`](../../../00-quick-start/pom.xml):

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

`langchain4j-open-ai-official` modulis suteikia `OpenAiOfficialChatModel` klasę, kuri jungiasi prie OpenAI suderinamų API. GitHub modeliai naudoja tą pačią API formą, taigi nereikia jokio specialaus adapterio – tiesiog nurodykite bazinį URL kaip `https://models.github.ai/inference`.

## Reikalavimai

**Naudojate Dev Container?** Java ir Maven jau įdiegti. Jums reikalingas tik GitHub asmeninis prieigos tokenas.

**Vietinė plėtra:**
- Java 21+, Maven 3.9+
- GitHub asmeninis prieigos tokenas (instrukcijos žemiau)

> **Pastaba:** Šis modulis naudoja `gpt-4.1-nano` iš GitHub modelių. Nekoreguokite modelio pavadinimo kode – jis sukonfigūruotas dirbti su GitHub siūlomais modeliais.

## Nustatymas

### 1. Gaukite savo GitHub tokeną

1. Eikite į [GitHub nustatymai → Asmeniniai prieigos tokenai](https://github.com/settings/personal-access-tokens)
2. Spauskite „Generate new token“
3. Nustatykite aprašomą pavadinimą (pvz., „LangChain4j demonstracija“)
4. Nustatykite galiojimo laiką (rekomenduojama 7 dienos)
5. Skiltyje „Sąskaitos teises“ raskite „Models“ ir nustatykite į „Tik skaitymui“
6. Spauskite „Generate token“
7. Nukopijuokite ir išsaugokite tokeną – jo daugiau nematysite

### 2. Nustatykite savo tokeną

**1 variantas: Naudojant VS Code (rekomenduojama)**

Jei naudojate VS Code, pridėkite savo tokeną į `.env` failą projekto šaknyje:

Jei `.env` failas neegzistuoja, nukopijuokite `.env.example` į `.env` arba sukurkite naują `.env` failą projekto šaknyje.

**Pavyzdinis `.env` failas:**
```bash
# Failas /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Tada paprastai galite dešiniuoju pelės klavišu spustelėti bet kurį demonstracinį failą (pvz., `BasicChatDemo.java`) naršyklėje ir pasirinkti **„Run Java“** arba naudoti paleidimo konfigūracijas iš Run and Debug skydelio.

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

## Paleiskite pavyzdžius

**Naudojant VS Code:** Paprasčiausiai dešiniuoju pelės klavišu spustelėkite bet kurį demonstracinį failą naršyklėje ir pasirinkite **„Run Java“**, arba naudokite paleidimo konfigūracijas iš Run and Debug skydelio (įsitikinkite, kad prieš tai įtraukėte savo tokeną į `.env` failą).

**Naudojant Maven:** Alternatyviai galite paleisti iš komandinės eilutės:

### 1. Pagrindinis pokalbis

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Užklausų šablonai

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Parodo zero-shot, few-shot, chain-of-thought ir rolės pagrindo užklausas.

### 3. Funkcijų kvietimas

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

DI automatiškai iškviečia jūsų Java metodus, kai reikia.

### 4. Dokumentų klausimai ir atsakymai (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Užduokite klausimus apie turinį faile `document.txt`.

### 5. Atsakingas dirbtinis intelektas

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Pamatykite, kaip DI saugumo filtrai blokuoja kenksmingą turinį.

## Ką parodo kiekvienas pavyzdys

**Pagrindinis pokalbis** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Pradėkite čia, kad pamatytumėte LangChain4j paprastumą. Jūs sukursite `OpenAiOfficialChatModel`, išsiųsite užklausą su `.chat()`, ir gausite atsakymą. Tai demonstruoja pagrindą: kaip inicijuoti modelius su specialiais galiniais taškais ir API raktais. Kai suprasite šį šabloną, visa kita statoma ant jo.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ir paklauskite:
> - „Kaip pakeisti GitHub modelius į Azure OpenAI šiame kode?“
> - „Kokius kitus parametrus galiu konfigūruoti OpenAiOfficialChatModel.builder()?“
> - „Kaip pridėti srautinį atsakymų gavimą, o ne laukti pilno atsakymo?“

**Užklausų kūrimas** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Kai jau žinote, kaip kalbėtis su modeliu, pažiūrėkime, ką jam sakote. Ši demonstracija naudoja tą patį modelio nustatymą, bet parodo penkis skirtingus užklausų šablonus. Išbandykite zero-shot užklausas tiesioms instrukcijoms, few-shot, kai modelis mokosi iš pavyzdžių, chain-of-thought, kurios atskleidžia mąstymo žingsnius, ir rolės pagrindu užklausas, kurios nustato kontekstą. Pamatysite, kaip tas pats modelis duoda labai skirtingus rezultatus pagal tai, kaip pateikiate užklausą.

Demonstracija taip pat parodo užklausų šablonus, kurie yra galingas būdas kurti pakartotinai naudojamas užklausas su kintamaisiais.
Žemiau pateiktas pavyzdys rodo užklausą naudojant LangChain4j `PromptTemplate` kintamųjų užpildymui. DI atsakys remdamasis nurodyta paskirties vieta ir veikla.

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

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ir paklauskite:
> - „Kuo skiriasi zero-shot ir few-shot užklausos, ir kada naudoti kiekvieną?“
> - „Kaip temperatūros parametras įtakoja modelio atsakymus?“
> - „Kokios technikos padeda išvengti užklausų injekcijos atakų gamyboje?“
> - „Kaip sukurti pakartotinai naudojamus PromptTemplate objektus dažnai pasitaikantiems šablonams?“

**Įrankių integracija** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Čia LangChain4j tampa galingas. Naudosite `AiServices`, kad sukurtumėte DI asistentą, kuris gali kvieti jūsų Java metodus. Tiesiog pažymėkite metodus `@Tool("aprašymas")`, o LangChain4j atliks likusius veiksmus – DI automatiškai nuspręs, kada naudoti kiekvieną įrankį pagal tai, ko prašo vartotojas. Tai demonstruoja funkcijų kvietimą, pagrindinę techniką kuriant DI, kuris gali imtis veiksmų, o ne tik atsakyti į klausimus.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ir paklauskite:
> - „Kaip veikia @Tool anotacija ir ką LangChain4j su ja daro fone?“
> - „Ar DI gali iškvieti kelis įrankius sekos tvarka, kad išspręstų sudėtingas problemas?“
> - „Kas nutinka, jei įrankis meta klaidą – kaip geriausia tvarkyti klaidas?“
> - „Kaip integruoti tikrą API vietoj šio skaičiuotuvo pavyzdžio?“

**Dokumentų klausimai ir atsakymai (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Čia pamatysite RAG (papildomos paieškos generavimas) pagrindus. Vietoj to, kad remtumėtės modelio mokymosi duomenimis, įkeliate turinį iš [`document.txt`](../../../00-quick-start/document.txt) ir įtraukiate jį į užklausą. DI atsako remdamasis jūsų dokumentu, o ne bendromis žiniomis. Tai pirmas žingsnis link sistemų, kurios gali dirbti su jūsų duomenimis.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Pastaba:** Šis paprastas metodas įkelia visą dokumentą į užklausą. Dideliems failams (>10KB) viršysite konteksto limitus. 03 modulyje aptariamas dalijimas į gabalus ir vektorinė paieška gamybos RAG sistemoms.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ir paklauskite:
> - „Kaip RAG padeda išvengti DI haliucinacijų, palyginti su modelio mokymosi duomenimis?“
> - „Kuo šis paprastas metodas skiriasi nuo vektorinių įtvarų naudojimo paieškai?“
> - „Kaip išplėsti tai, kad palaikytų kelis dokumentus ar didesnes žinių bazes?“
> - „Kokios geros praktikos, formuojant užklausą, kad DI naudotų tik pateiktą kontekstą?“

**Atsakingas dirbtinis intelektas** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Kurkit DI saugumą daugiapakopiu būdu. Ši demonstracija rodo du apsaugos sluoksnius:

**1 dalis: LangChain4j įėjimo apsaugos taisyklės** – Blokuoja pavojingas užklausas prieš pasiekiant LLM. Kurkite savo taisykles, kurios tikrina draudžiamus žodžius ar šablonus. Jos veikia jūsų kode, tad yra greitos ir nemokamos.

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

**2 dalis: Teikėjo saugumo filtrai** – GitHub modeliai turi įmontuotus filtrus, kurie sugautų tai, ką gali praleisti jūsų taisyklės. Matysite griežtus blokus (HTTP 400 klaidas) rimtiems pažeidimams ir minkštas atmetimo žinutes, kur DI mandagiai atsisako.

> **🤖 Išbandykite su [GitHub Copilot](https://github.com/features/copilot) pokalbiu:** Atidarykite [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ir paklauskite:
> - „Kas yra InputGuardrail ir kaip sukurti savo?“
> - „Kuo skiriasi griežtas blokavimas ir minkštas atmetimas?“
> - „Kodėl verta naudoti tiek taisykles, tiek teikėjo filtrus kartu?“

## Tolimesni žingsniai

**Kitas modulis:** [01-introduction - Pirmieji žingsniai su LangChain4j ir gpt-5 Azure](../01-introduction/README.md)

---

**Navigacija:** [← Atgal į pagrindinį](../README.md) | [Toliau: Modulis 01 - Įvadas →](../01-introduction/README.md)

---

## Problemų sprendimas

### Pirmas kartas statant su Maven

**Problema:** Pirmasis `mvn clean compile` arba `mvn package` užtrunka ilgai (10-15 minučių)

**Priežastis:** Maven turi atsisiųsti visas projekto priklausomybes (Spring Boot, LangChain4j bibliotekas, Azure SDK ir kt.) pirmojo statymo metu.

**Sprendimas:** Tai normalu. Tolimesni statymai bus daug greitesni, nes priklausomybės bus įrašytos į talpyklą vietoje. Atsisiuntimo laikas priklauso nuo jūsų tinklo greičio.
### PowerShell Maven komandos sintaksė

**Problema**: Maven komandos nepavyksta su klaida `Unknown lifecycle phase ".mainClass=..."`

**Priežastis**: PowerShell traktuoja `=` kaip kintamojo priskyrimo operatorių, todėl Maven savybės sintaksė sugriūna

**Sprendimas**: Naudokite sustabdymo analizės operatorių `--%` prieš Maven komandą:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operatorius `--%` nurodo PowerShell perduoti visas likusias argumentų dalis pažodžiui Maven be interpretacijos.

### Windows PowerShell emocijų simbolių rodymas

**Problema**: AI atsakymai PowerShell rodo šiukšlinius simbolius (pvz., `????` arba `â??`) vietoje emocijų simbolių

**Priežastis**: PowerShell numatytoji koduotė nepalaiko UTF-8 emocijų simbolių

**Sprendimas**: Vykdykite šią komandą prieš paleidžiant Java programas:
```cmd
chcp 65001
```

Tai priverčia naudoti UTF-8 koduotę terminale. Alternatyviai naudokite Windows Terminal, kuris geriau palaiko Unicode.

### API kvietimų derinimas

**Problema**: Autentifikacijos klaidos, ribojimai arba netikėti AI modelio atsakymai

**Sprendimas**: Pavyzdžiuose naudojami `.logRequests(true)` ir `.logResponses(true)`, kad API kvietimai būtų rodomi konsolėje. Tai padeda derinti autentifikacijos klaidas, ribojimus ar netikėtus atsakymus. Šiuos flagus gamybinėje aplinkoje pašalinkite, kad sumažintumėte logų triukšmą.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogaus vertimą. Mes neprisiimame atsakomybės už bet kokius nesusipratimus ar neteisingus aiškinimus, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->