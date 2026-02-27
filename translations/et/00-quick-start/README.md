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
  - [2. Käsu mustrid](../../../00-quick-start)
  - [3. Funktsiooni kutsumine](../../../00-quick-start)
  - [4. Dokumendi küsimused ja vastused (Lihtne RAG)](../../../00-quick-start)
  - [5. Vastutustundlik tehisintellekt](../../../00-quick-start)
- [Mida iga näide näitab](../../../00-quick-start)
- [Järgmised sammud](../../../00-quick-start)
- [Probleemide lahendamine](../../../00-quick-start)

## Sissejuhatus

See kiire algus on mõeldud selleks, et viia teid LangChain4j-ga võimalikult kiiresti tööle. See hõlmab AI-rakenduste loomise absoluutseid põhialuseid LangChain4j ja GitHubi mudelitega. Järgmistes moodulites kasutate Azure OpenAI-d koos LangChain4j-ga, et luua keerukamaid rakendusi.

## Mis on LangChain4j?

LangChain4j on Java teek, mis lihtsustab AI-põhiste rakenduste loomist. Selle asemel, et tegeleda HTTP klientide ja JSON-i töötlemisega, töötate puhaste Java API-dega.

"Chain" LangChainis viitab mitme komponendi ühendamisele ahelaks — näiteks võite ühendada prompti mudeliga ja mudeli parseriga või ahelana mitu AI kõnet, kus ühe väljund sisenduseks järgmisele. See kiire algus keskendub algtõdedele enne keerukamate ahelate uurimist.

<img src="../../../translated_images/et/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j komponendi ahelad - ehitusplokid ühenduvad võimsate AI töövoogude loomiseks*

Kasutame kolme põhikomponenti:

**ChatModel** – liides AI mudeliga suhtlemiseks. Kutsuge `model.chat("prompt")` ja saate vastuse tekstina. Kasutame `OpenAiOfficialChatModel`i, mis töötab OpenAI-ga ühilduvate lõpp-punktidega nagu GitHubi mudelid.

**AiServices** – loob tüüpesõbralikud AI teenuste liidesed. Määratlege meetodid, märgistage need `@Tool` annotatsiooniga ja LangChain4j korraldab orkestreerimise. AI kutsub teie Java meetodeid automaatselt vajadusel.

**MessageWindowChatMemory** – hoiab vestluse ajalugu. Selle ilma on iga päring sõltumatu. Selle abil mäletab AI varasemaid sõnumeid ja säilitab konteksti mitme kõne vältel.

<img src="../../../translated_images/et/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j arhitektuur – põhikomponendid töötavad koos teie AI rakenduste tugevdamiseks*

## LangChain4j sõltuvused

See kiire algus kasutab kolme Maven-i sõltuvust failis [`pom.xml`](../../../00-quick-start/pom.xml):

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

Moodul `langchain4j-open-ai-official` pakub klassi `OpenAiOfficialChatModel`, mis ühendub OpenAI-ga ühilduvate API-dega. GitHubi mudelid kasutavad sama API formaati, seega erilist adapterit pole vaja – lihtsalt seadistage baasu URLiks `https://models.github.ai/inference`.

Moodul `langchain4j-easy-rag` pakub automaatset dokumentide jagamist, manustamist ja otsingut, nii et saate luua RAG-rakendusi ilma, et peaksite iga sammu käsitsi seadistama.

## Eeltingimused

**Kas kasutate arenduskonteinerit?** Java ja Maven on juba installitud. Teil on vaja ainult GitHubi isikliku juurdepääsu tokenit.

**Kohalik arendus:**
- Java 21+, Maven 3.9+
- GitHubi isiklik juurdepääsu token (juhised allpool)

> **Märkus:** See moodul kasutab GitHubi mudelite `gpt-4.1-nano`. Ärge muutke koodi mudeli nime — see on konfigureeritud töötama GitHubi saadaolevate mudelitega.

## Seadistamine

### 1. Hangi oma GitHubi token

1. Minge aadressile [GitHub Seaded → Isiklikud juurdepääsu tokenid](https://github.com/settings/personal-access-tokens)
2. Klõpsake "Generate new token"
3. Määrake kirjeldav nimi (nt "LangChain4j Demo")
4. Määrake aegumiskuupäev (soovitatav 7 päeva)
5. Kontokasutusõiguste alt leidke "Models" ja seadke "Ainult lugemine"
6. Klõpsake "Generate token"
7. Kopeerige ja salvestage oma token – te ei näe seda enam uuesti

### 2. Määra oma token

**Variant 1: VS Code kasutades (soovitatav)**

Kui kasutate VS Code'i, lisage token projekti juurkaustas asuvasse `.env` faili:

Kui `.env` faili ei ole, kopeerige `.env.example` faili nimeks `.env` või looge uus `.env` fail projekti juurkausta.

**Näide `.env` failist:**
```bash
# Asub kataloogis /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Seejärel võite lihtsalt hiire parema klahviga klõpsata mistahes näitenupul (nt `BasicChatDemo.java`) Exploreri paneelis ja valida **"Run Java"** või kasutada käivituskonfiguratsioone Run and Debug paneelist.

**Variant 2: Terminali kasutades**

Seadke token keskkonnamuutujana:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Näidete käivitamine

**VS Code kasutades:** Lihtsalt paremklõpsake mis tahes demofailil Exploreri vaatest ja valige **"Run Java"**, või kasutage käivituskonfiguratsioone Run and Debug paneelist (veenduge, et token oleks esmalt `.env` faili lisatud).

**Maveni kasutades:** Võite ka käivitada käsurealt:

### 1. Põhiline vestlus

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Käsu mustrid

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näitab zero-shot, few-shot, mõttekäigu ahelat ja rollipõhist promptimist.

### 3. Funktsiooni kutsumine

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI kutsub automaatselt teie Java meetodeid vastavalt vajadusele.

### 4. Dokumendi küsimused ja vastused (Lihtne RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Esitage küsimusi oma dokumentide kohta Easy RAG-ga, mis kasutab automaatset manustamist ja otsingut.

### 5. Vastutustundlik tehisintellekt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vaadake, kuidas AI turvafiltrid blokeerivad kahjulikku sisu.

## Mida iga näide näitab

**Põhiline vestlus** – [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Alustage siit, et näha LangChain4j lihtsamat külge. Loote `OpenAiOfficialChatModel`i, saadate prompti `.chat()`-ga ja saate vastuse. See demonstreerib alust: kuidas algatada mudelit kohandatud lõpp-punktide ja API võtmetega. Kui see mustrit mõistate, saate kõik muu sellele rajada.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) chatiga:** Avage [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja küsige:
> - "Kuidas ma selles koodis vahetaksin GitHubi mudelid Azure OpenAI vastu?"
> - "Milliseid muid parameetreid saan seadistada OpenAiOfficialChatModel.builder() sees?"
> - "Kuidas lisada voogesituse vastuseid, mitte oodata täielikku vastust?"

**Prompti inseneeria** – [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nüüd kui teate, kuidas mudeliga rääkida, uurime, mida te talle ütlete. See demo kasutab sama mudeli seadistust, kuid näitab viit erinevat prompti mustrit. Proovige zero-shot käsklusi otseste juhistena, few-shot käsklusi näidete põhjal õppimiseks, mõttekäigu ahelat sammude avamiseks ja rollipõhiseid promptimisi konteksti seadmiseks. Näete, kuidas sama mudel annab oluliselt erinevaid vastuseid, sõltuvalt sellest, kuidas päringu vormistate.

Demo demonstreerib ka prompti malle, mis võimaldavad luua korduvkasutatavaid käske koos muutujatega.
Allolev näide kasutab LangChain4j `PromptTemplate`'i muutujate täitmiseks. AI vastab lähtudes määratud sihtkohast ja tegevusest.

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

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) chatiga:** Avage [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja küsige:
> - "Mis vahe on zero-shot ja few-shot promptimisel ning millal kumbagi kasutada?"
> - "Kuidas mõjutab temperatuuri parameeter mudeli vastuseid?"
> - "Millised on tehnikad prompti süstimisrünnakute vältimiseks tootmises?"
> - "Kuidas luua korduvkasutatavaid PromptTemplate objekte tavaliste mustrite jaoks?"

**Tööriistade integreerimine** – [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Siin saab LangChain4j tõeliselt võimsaks. Kasutate `AiServices` AI assistendi loomiseks, mis kutsub teie Java meetodeid. Märgistage meetodid lihtsalt `@Tool("kirjeldus")` annotatsiooniga ja LangChain4j hoolitseb ülejäänu eest – AI otsustab automaatselt, millal tööriistu kasutada vastavalt kasutaja päringule. See demonstreerib funktsiooni kutsumist, mis on võtmetehnika AI jaoks, mis suudab tegutseda, mitte vaid vastata küsimustele.

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

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) chatiga:** Avage [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja küsige:
> - "Kuidas @Tool annotatsioon töötab ja mida LangChain4j sellega taustal teeb?"
> - "Kas AI saab järjestikku kasutada mitut tööriista keerukate probleemide lahendamiseks?"
> - "Mis juhtub, kui tööriist viskab erindi – kuidas vigu korrektselt käidelda?"
> - "Kuidas integreerida päris API selle kalkulaatori näite asemel?"

**Dokumendi küsimused ja vastused (Lihtne RAG)** – [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Siin näete RAG-i (otsingupõhine genereerimine) kasutamist LangChain4j "Lihtsa RAG" lähenemise abil. Dokumendid laetakse, jagatakse automaatselt ja manustatakse mällu, seejärel annab sisuloendaja päringu ajal AI-le asjakohased lõigud. AI vastab teie dokumentide põhjal, mitte oma üldteadmiste põhjal.

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

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) chatiga:** Avage [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja küsige:
> - "Kuidas RAG takistab AI hallutsinatsioone võrreldes mudelitreeningu andmetega?"
> - "Mis vahe on selle lihtsa lähenemise ja kohandatud RAG torustiku vahel?"
> - "Kuidas skaleerida seda mitme dokumendi või suurema teadmistebaasi jaoks?"

**Vastutustundlik tehisintellekt** – [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Looge AI turvalisus kihiliselt. See demo näitab kahte kaitsetaset, mis töötavad koos:

**1. osa: LangChain4j sisendi ohutuspiirid** – Blokeerib ohtlikud promptid enne, kui need jõuavad LLM-i. Looge kohandatud ohutuspiirid, mis kontrollivad keelatud märksõnu või mustreid. Need töötavad teie koodis, nii et on kiired ja tasuta.

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

**2. osa: Teenusepakkuja turvafiltrid** – GitHubi mudelitel on sisseehitatud filtrid, mis püüavad kinni selle, mida teie piirid võivad jätta. Näete raskekujulisi blokeeringuid (HTTP 400 vead) tõsiste rikkumiste korral ja pehmeid keeldumisi, kus AI viisakalt keeldub.

> **🤖 Proovige [GitHub Copilot](https://github.com/features/copilot) chatiga:** Avage [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja küsige:
> - "Mis on InputGuardrail ja kuidas teha oma?"
> - "Mis vahe on raskel blokeeringul ja pehmel keeldumisel?"
> - "Miks kasutada nii ohutuspiire kui ka teenusepakkuja filtreid koos?"

## Järgmised sammud

**Järgmine moodul:** [01-introduction - Sissejuhatus LangChain4j ja gpt-5 kasutamisse Azure'is](../01-introduction/README.md)

---

**Navigatsioon:** [← Tagasi põhiteemale](../README.md) | [Edasi: Moodul 01 - Sissejuhatus →](../01-introduction/README.md)

---

## Probleemide lahendamine

### Esimene Maven ehitus

**Probleem:** Esmane `mvn clean compile` või `mvn package` võtab kaua aega (10-15 minutit)

**Põhjus:** Maven peab esmase ehituse ajal alla laadima kõik projekti sõltuvused (Spring Boot, LangChain4j teegid, Azure SDKd jms).

**Lahendus:** See on normaalne käitumine. Edasised ehitused on palju kiirem, kuna sõltuvused on lokaalselt vahemälus. Allalaadimiskiirus sõltub teie võrguühendusest.

### PowerShelli Maven käsu süntaks

**Probleem:** Maven käsud ebaõnnestuvad veateatega `Unknown lifecycle phase ".mainClass=..."`
**Põhjus**: PowerShell tõlgendab `=` kui muutujamääramise operaatorit, mis rikub Maveni omaduste süntaksit

**Lahendus**: Kasutage stop-parsing operaatorit `--%` enne Maveni käsku:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operaator `--%` käsib PowerShellil edastada kõik ülejäänud argumendid Mavenile täpselt ilma tõlgendamiseta.

### Windows PowerShell Emoji kuva

**Probleem**: AI vastustes kuvatakse PowerShellis emoji asemel rämpsmärgid (nt `????` või `â??`)

**Põhjus**: PowerShelli vaikekodeering ei toeta UTF-8 emojisi

**Lahendus**: Käivitage see käsk enne Java rakenduste käivitamist:
```cmd
chcp 65001
```

See sunnib terminali kasutama UTF-8 kodeeringut. Alternatiivina kasutage Windows Terminali, mis toetab paremini Unicode'i.

### API kõnede silumine

**Probleem**: Autentimise vead, piiresageduse piirangud või ootamatud AI mudeli vastused

**Lahendus**: Näited sisaldavad `.logRequests(true)` ja `.logResponses(true)`, mis näitavad API kõnesid konsoolis. See aitab siluda autentimisvigu, piiresageduse piiranguid või ootamatuid vastuseid. Eemaldage need lipud tootmiskeskkonnas, et vähendada logihäält.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades AI tõlke teenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun arvestage, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles loetakse autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta mis tahes arusaamatuste või valesti mõistmiste eest, mis võivad tekkida selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->