# Moodul 00: Kiire algus

## Sisukord

- [Sissejuhatus](../../../00-quick-start)
- [Mis on LangChain4j?](../../../00-quick-start)
- [LangChain4j sõltuvused](../../../00-quick-start)
- [Eeltingimused](../../../00-quick-start)
- [Seadistamine](../../../00-quick-start)
  - [1. Hangi oma GitHubi token](../../../00-quick-start)
  - [2. Määra oma token](../../../00-quick-start)
- [Näidete käivitamine](../../../00-quick-start)
  - [1. Põhiline vestlus](../../../00-quick-start)
  - [2. Käskude mustrid](../../../00-quick-start)
  - [3. Funktsioonikõned](../../../00-quick-start)
  - [4. Dokumentide küsimused ja vastused (Lihtne RAG)](../../../00-quick-start)
  - [5. Vastutustundlik tehisintellekt](../../../00-quick-start)
- [Mida iga näide näitab](../../../00-quick-start)
- [Järgmised sammud](../../../00-quick-start)
- [Probleemide lahendamine](../../../00-quick-start)

## Sissejuhatus

See kiire algus on mõeldud selleks, et saaksid LangChain4j-ga võimalikult kiiresti tööd alustada. See käsitleb AI-rakenduste ehitamise absoluutseid põhialuseid LangChain4j ja GitHubi mudelitega. Järgmistes moodulites lähed üle Azure OpenAI ja GPT-5.2 kasutamisele ning süüvid iga kontseptsiooni sügavamalt.

## Mis on LangChain4j?

LangChain4j on Java teek, mis lihtsustab AI-põhiste rakenduste loomist. HTTP klientide ja JSON parsimisega tegelemise asemel töötad puhaste Java API-dega.

"Chain" LangChainis viitab mitme komponendi aheldamisele – võid näiteks ühendada käsku mudeliga, seejärel parseriga või ühendada mitmeid AI-kõnesid, kus üks väljund on järgmise sisend. See kiire algus keskendub aluspõhimõtetele, enne kui uurida keerukamaid ahelaid.

<img src="../../../translated_images/et/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*Ahelate ühendamine LangChain4j-s – ehitusplokid ühenduvad võimsate AI töövoogude loomiseks*

Kasutame kolme põhikomponenti:

**ChatModel** – Liides AI mudeliga suhtlemiseks. Kutsu `model.chat("prompt")` ja saa vastuseks tekst. Kasutame `OpenAiOfficialChatModel`-i, mis töötab OpenAI-ga ühilduvate lõpp-punktidega nagu GitHub Models.

**AiServices** – Loob tüübiturvalisi AI teenuste liideseid. Määra meetodid, märgista neid `@Tool`-iga ja LangChain4j korraldab ülejäänu. AI kutsub automaatselt vajadusel sinu Java meetodeid.

**MessageWindowChatMemory** – Säilitab vestlusajaloo. Ilma selleta on iga päring iseseisev, sellega mäletab AI eelnevaid sõnumeid ja hoiab konteksti mitme pöörde vältel.

<img src="../../../translated_images/et/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arhitektuur – põhikomponendid töötavad koos, et võimendada sinu AI-rakendusi*

## LangChain4j sõltuvused

See kiire algus kasutab kolme Maven sõltuvust failis [`pom.xml`](../../../00-quick-start/pom.xml):

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

`langchain4j-open-ai-official` moodul pakub `OpenAiOfficialChatModel` klassi, mis ühendub OpenAI-ga ühilduvate API-dega. GitHub Models kasutab sama API vormingut, nii et eraldi adapterit ei ole vaja — piisas suunamisest baas-URL-iga `https://models.github.ai/inference`.

`langchain4j-easy-rag` moodul pakub automaatset dokumentide jagamist, sisestamist ja päringut, et saaksid ehitada RAG-rakendusi ilma iga sammu käsitsi seadistamiseta.

## Eeltingimused

**Kas kasutad arenduscontainerit?** Java ja Maven on juba installitud. Sul on vaja vaid GitHubi isiklikku juurdepääsutokenit.

**Kohalik arendus:**
- Java 21 või uuem, Maven 3.9 või uuem
- GitHubi isiklik juurdepääsutoken (juhised allpool)

> **Märkus:** See moodul kasutab GitHub Models-ist mudelit `gpt-4.1-nano`. Ära muuda koodis mudelinime – see on seadistatud töötama GitHubi saadaolevate mudelitega.

## Seadistamine

### 1. Hangi oma GitHubi token

1. Mine lehele [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. Vali "Generate new token"
3. Sea kirjeldav nimi (nt "LangChain4j Demo")
4. Sea aegumine (soovitavalt 7 päeva)
5. "Account permissions" alt leia "Models" ja sea "Read-only"
6. Kliki "Generate token"
7. Kopeeri ja salvesta oma token – seda enam ei kuvata

### 2. Määra oma token

**Variant 1: Kasutades VS Code'i (Soovitatav)**

Kui kasutad VS Code'i, lisa oma token projekti juurkataloogi faili `.env`:

Kui `.env` faili pole, kopeeri `.env.example` failist `.env` või loo uus `.env` fail projekti juurkausta.

**Näide `.env` failist:**
```bash
# Kaustas /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Seejärel võid lihtsalt paremklõpsata mis tahes demo failil (nt `BasicChatDemo.java`) Exploreris ja valida **"Run Java"** või kasutada Run and Debug paneeli käivitustingimusi.

**Variant 2: Kasutades terminali**

Sea token keskkonnamuutujana:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Näidete käivitamine

**Kui kasutad VS Code'i:** Paremklõpsa lihtsalt mis tahes demo failil Exploreris ja vali **"Run Java"** või kasuta Run and Debug paneeli käivitustingimusi (veendu, et token on esmalt lisatud `.env` faili).

**Kui kasutad Mavenit:** Võid kasutada ka käsurida:

### 1. Põhiline vestlus

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Käskude mustrid

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näitab zero-shot, few-shot, chain-of-thought ja rollipõhiseid käske.

### 3. Funktsioonikõned

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI kutsub automaatselt sinu Java meetodeid vajadusel.

### 4. Dokumentide küsimused ja vastused (Lihtne RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Esita dokumentide kohta küsimusi, kasutades Easy RAG-i automaatse sisestuse ja otsinguga.

### 5. Vastutustundlik tehisintellekt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vaata, kuidas AI turvafiltrid blokeerivad kahjuliku sisu.

## Mida iga näide näitab

**Põhiline vestlus** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Alusta siit, et näha LangChain4j lihtsaimat kasutust. Lood `OpenAiOfficialChatModel`-i, saadad päringu `.chat()` meetodi kaudu ja saad vastuse. See näitab alustalasid: kuidas algatada mudeleid kohandatud lõpp-punktide ja API võtmetega. Kui sellest mustrist aru saad, ehitub kõik muu sellele peale.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja küsi:
> - "Kuidas ma selle koodiga GitHub Models-ist Azure OpenAI-le üle läheksin?"
> - "Milliseid muid parameetreid saan seadistada OpenAiOfficialChatModel.builder() meetodis?"
> - "Kuidas lisada voogedastusega vastuseid selle asemel, et oodata täielikku vastust?"

**Käskude inseneritöö** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nüüd kui tead, kuidas mudeliga rääkida, uurime, mida talle öelda. See demo kasutab sama mudeli seadistust, kuid näitab viit erinevat käskude mustrit. Proovi zero-shot käske otseste juhistena, few-shot skeeme näidete põhjal õppimiseks, chain-of-thought mustrit, mis paljastab mõttekäigu samme, ja rollipõhiseid käske, mis määravad konteksti. Näed, kuidas sama mudel annab väga erinevaid tulemusi, olenevalt sellest, kuidas päringu vormistad.

Demo demonstreerib ka käskude malle (prompt templates), mis on võimas viis korduvkasutatavate käsu mallide loomiseks muutujatega.
Alljärgnev näide kasutab LangChain4j `PromptTemplate`-d muutujate täitmiseks. AI vastab sihtkoha ja tegevuse põhjal.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja küsi:
> - "Mis vahe on zero-shot ja few-shot käsustamisel, millal kumbagi kasutada?"
> - "Kuidas mõjutab mudeli vastuseid temperatuuriparameeter?"
> - "Milliseid võtteid kasutada, et vältida käskude süstimist tootmises?"
> - "Kuidas luua korduvkasutatavaid PromptTemplate objekte tavaliste mustrite jaoks?"

**Tööriistade integratsioon** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Siin muutub LangChain4j võimsaks. Kasutad `AiServices`-t AI assistendi loomiseks, mis kutsub sinu Java meetodeid. Märgista meetodid `@Tool("kirjeldus")` annotatsiooniga ja LangChain4j korraldab ülejäänu – AI otsustab automaatselt, millal ja millist tööriista kasutada vastavalt kasutaja päringule. See demonstreerib funktsioonikõnesid, mis on võtmetehnika AI jaoks, mis suudab tegutseda, mitte ainult vastata.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja küsi:
> - "Kuidas @Tool annotatsioon töötab ja mida LangChain4j sellega taga teeb?"
> - "Kas AI saab mitut tööriista järjest kutsuda keerukate probleemide lahendamiseks?"
> - "Mis juhtub, kui tööriist viskab vea – kuidas peaksin vead käsitlema?"
> - "Kuidas integreerida päris API selle kalkulaatori näite asemel?"

**Dokumentide küsimused ja vastused (Lihtne RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Siin näed RAG-i (otsingupõhine generaator) kasutamist LangChain4j “Easy RAG” lähenemisega. Dokumendid laaditakse, automaatselt jagatakse ja sisestatakse mällu, seejärel päringut tegeva AI jaoks tarnitakse asjakohased tükid küsimuse ajal. AI vastab dokumentide põhjal, mitte oma üldise teadmiste põhjal.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja küsi:
> - "Kuidas RAG hoiab ära AI hallutsinatsioone võrreldes mudeli treeningandmete kasutamisega?"
> - "Mis vahe on selle lihtsa lähenemise ja kohandatud RAG torustiku vahel?"
> - "Kuidas skaleerida seda mitme dokumendi või suurema teadmistebaasi jaoks?"

**Vastutustundlik tehisintellekt** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Ehita AI turvalisus mitme kaitsetasemega. See demo näitab kahte kaitsest kihti, mis töötavad koos:

**Osa 1: LangChain4j sisendi kontroll** – Blokeerib ohtlikud käsud enne LLM-ile jõudmist. Loo kohandatud reeglid, mis otsivad keelatud märksõnu või mustreid. Need töötavad koodis kiirelt ja tasuta.

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

**Osa 2: Teenusepakkuja turvafiltrid** – GitHub Models-il on sisseehitatud filtrid, mis püüavad kinni, mida su kontrollid võivad vahele jätta. Näed ranget blokeerimist (HTTP 400 vead) tõsiste rikkumiste korral ja pehmet keeldumist, kus AI viisakalt keelduv vastus annab.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) Chat abil:** Ava [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja küsi:
> - "Mis on InputGuardrail ja kuidas luua oma?"
> - "Mis vahe on rangel blokeerimisel ja pehmel keelamisel?"
> - "Miks kasutada korraga nii reegleid kui teenusepakkuja filtreid?"

## Järgmised sammud

**Järgmine moodul:** [01-introduction - LangChain4j kasutuselevõtt](../01-introduction/README.md)

---

**Navigeerimine:** [← Tagasi peamisele](../README.md) | [Järgmine: Moodul 01 - Sissejuhatus →](../01-introduction/README.md)

---

## Probleemide lahendamine

### Esimene Maven ehitus

**Tõrge:** Esimene `mvn clean compile` või `mvn package` võtab kaua aega (10-15 minutit)

**Põhjus:** Maven peab laadima alla kõik projekti sõltuvused (Spring Boot, LangChain4j teegid, Azure SDK-d jne) esimesel ehitusel.

**Lahendus:** See on normaalne käitumine. Järgmised ehitused on palju kiirem, kuna sõltuvused on lokaalselt vahemällu salvestatud. Allalaadimise aeg sõltub teie võrgu kiirusest.

### PowerShelli Maven käsu sünktaks

**Tõrge:** Maven käsk ebaõnnestub veaga `Unknown lifecycle phase ".mainClass=..."`
**Põhjus**: PowerShell tõlgendab `=` kui muutuja määramise operaatorit, mis rikub Maven omaduste süntaksit

**Lahendus**: Kasuta peatamis-parsimise operaatorit `--%` enne Maven käsku:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operaator `--%` käsib PowerShellil edastada kõik järelejäänud argumendid Mavenile kirjapandult, tõlgendamata neid.

### Windows PowerShell emojide kuvamine

**Probleem**: AI vastused kuvavad PowerShellis emotikonide asemel segasemaid märke (nt `????` või `â??`)

**Põhjus**: PowerShelli vaikimisi kodeering ei toeta UTF-8 emotikone

**Lahendus**: Käivita see käsk enne Java rakenduste käivitamist:
```cmd
chcp 65001
```

See sunnib terminali kasutama UTF-8 kodeeringut. Alternatiivina kasuta Windows Terminali, mis toetab paremini Unicode'i.

### API kutsete silumine

**Probleem**: Autentimisvead, kiirusepiirangud või ootamatud vastused AI mudelilt

**Lahendus**: Näidetes on `.logRequests(true)` ja `.logResponses(true)`, et kuvada API kutseid konsoolis. See aitab lahendada autentimisvigu, kiirusepiiranguid või ootamatuid vastuseid. Eemalda need lipud tootmiskeskkonnas, et vähendada logi müra.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Sissetõmbumine**:  
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüame täpsust tagada, tuleb arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Tähtsa teabe korral soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tekkivate arusaamatuste või valemonumuste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->