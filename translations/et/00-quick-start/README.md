# Moodul 00: Kiire algus

## Sisukord

- [Sissejuhatus](../../../00-quick-start)
- [Mis on LangChain4j?](../../../00-quick-start)
- [LangChain4j sõltuvused](../../../00-quick-start)
- [Nõuded](../../../00-quick-start)
- [Seadistamine](../../../00-quick-start)
  - [1. Saa oma GitHubi token](../../../00-quick-start)
  - [2. Sea oma token](../../../00-quick-start)
- [Näidete käivitamine](../../../00-quick-start)
  - [1. Põhiline vestlus](../../../00-quick-start)
  - [2. Prompti mustrid](../../../00-quick-start)
  - [3. Funktsioonikõned](../../../00-quick-start)
  - [4. Dokumendi Q&A (RAG)](../../../00-quick-start)
  - [5. Vastutustundlik tehisintellekt](../../../00-quick-start)
- [Mida iga näide näitab](../../../00-quick-start)
- [Järgmised sammud](../../../00-quick-start)
- [Tõrkeotsing](../../../00-quick-start)

## Sissejuhatus

See kiire algus on mõeldud selleks, et saaksite LangChain4j-ga võimalikult kiiresti tööle. See katab tehisintellekti rakenduste loomise absoluutseid aluseid LangChain4j ja GitHubi mudelite kasutamisel. Järgnevates moodulites kasutate Azure OpenAI-d koos LangChain4j-ga keerukamate rakenduste ehitamiseks.

## Mis on LangChain4j?

LangChain4j on Java teek, mis lihtsustab tehisintellekti-põhiste rakenduste loomist. HTTP klientide ja JSON-i parsimise asemel töötate puhaste Java API-dega.

LangChain sõna „chain“ viitab komponentide omavahel jadaühendamisele – võite ühendada prompti mudeliga ja seejärel parseriga või ühendada mitu AI kõnet, kus ühe väljund läheb järgmise sisendiks. See kiire algus keskendub põhialustele enne keerulisemate jadaühenduste uurimist.

<img src="../../../translated_images/et/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j kettide kontseptsioon" width="800"/>

*Komponentide kettline ühendamine LangChain4j-s – ehituskivid, mis loovad võimsad tehisintellekti töövood*

Kasutame kolme põhikomponenti:

**ChatLanguageModel** – liides tehisintellekti mudelitega suhtlemiseks. Kutsu `model.chat("prompt")` ja saa vastuseks string. Kasutame `OpenAiOfficialChatModel`-i, mis töötab OpenAI ühilduvate lõpp-punktidega nagu GitHubi mudelid.

**AiServices** – loob tüübiturvalised AI teenuste liidesed. Määra meetodid, märgista need `@Tool` annotatsiooniga ja LangChain4j tegeleb orkestreerimisega. AI kutsub automaatselt sinu Java meetodeid vastavalt vajadusele.

**MessageWindowChatMemory** – haldab vestluse ajalugu. Ilma selleta on iga päring iseseisev. Sellega mäletab AI eelnevaid sõnumeid ja hoiab konteksti üle mitme vooru.

<img src="../../../translated_images/et/architecture.eedc993a1c576839.webp" alt="LangChain4j arhitektuur" width="800"/>

*LangChain4j arhitektuur – põhikomponendid töötavad koos, et toetada sinu tehisintellekti rakendusi*

## LangChain4j sõltuvused

See kiire algus kasutab kahte Maven sõltuvust [`pom.xml`](../../../00-quick-start/pom.xml):

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

`langchain4j-open-ai-official` moodul pakub `OpenAiOfficialChatModel` klassi, mis ühendub OpenAI-sarnaste API-dega. GitHubi mudelid kasutavad sama API formaati, seega pole vaja spetsiaalset adapterit – lihtsalt suuna baas-URL `https://models.github.ai/inference`-ile.

## Nõuded

**Kas kasutad arenduskonteinerit?** Java ja Maven on juba installitud. Vajad ainult GitHubi isikliku juurdepääsu tokenit.

**Kohalik arendus:**
- Java 21+, Maven 3.9+
- GitHubi isiklik juurdepääsu token (juhised allpool)

> **Märkus:** See moodul kasutab GitHubi mudelit `gpt-4.1-nano`. Ära muuda koodis mudeli nime – see on konfigureeritud töötama GitHubi saadaval olevate mudelitega.

## Seadistamine

### 1. Saa oma GitHubi token

1. Mine lehele [GitHub Seaded → Isikliku juurdepääsu tokenid](https://github.com/settings/personal-access-tokens)
2. Klõpsa "Generate new token"
3. Pane tähenduslik nimi (nt "LangChain4j demo")
4. Sea aegumine (soovitatavalt 7 päeva)
5. Lähtuvalt "Account permissions" alt otsi "Models" ja määra see "Read-only"-ks
6. Klõpsa "Generate token"
7. Kopeeri ja hoiusta oma tokenit – seda enam uuesti ei näe

### 2. Sea oma token

**Variant 1: Kasutades VS Code'i (soovitatav)**

Kui kasutad VS Code'i, lisa oma token projekti juurkataloogis asuvasse `.env` faili:

Kui `.env` faili ei ole, kopeeri `.env.example` faili nimeks `.env` või loo uus `.env` fail projekti juurkausta.

**Näide `.env` failist:**
```bash
# Failis /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

Siis saad lihtsalt parema klikiga mingil demofailil (nt `BasicChatDemo.java`) Explorer aknas valida **"Run Java"** või kasutada Run and Debug paneeli käivitamiskonfiguratsioone.

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

**Kasuta VS Code'i:** Paremklõpsa lihtsalt exploreris mõnel demofailil ja vali **"Run Java"** või kasuta Run and Debug paneeli seadistusi (veendu, et oled tokeni `.env` faili lisanud).

**Kasuta Mavenit:** Võid ka käsurealt käivitada:

### 1. Põhiline vestlus

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompti mustrid

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Näitab zero-shot, few-shot, kettarvutlust ja rollipõhist promptimist.

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

### 4. Dokumendi Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Esita küsimusi failis `document.txt` oleva sisu kohta.

### 5. Vastutustundlik tehisintellekt

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

Vaata, kuidas AI turvafiltrid blokeerivad kahjulikku sisu.

## Mida iga näide näitab

**Põhiline vestlus** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

Alusta siit, et näha LangChain4j töötamas kõige lihtsamal kujul. Lood `OpenAiOfficialChatModel` objekti, saadad prompti `.chat()` meetodiga ja saad vastuse. See näitab põhialuseid: kuidas initsialiseerida mudeleid kohandatud lõpp-punktide ja API võtmepäringutega. Kui see muster on selge, ehitatakse kõik ülejäänu selle pealt.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Ava [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ja küsi:
> - "Kuidas ma muudaksin selle koodi GitHubi mudelitelt Azure OpenAI-le?"
> - "Milliseid teisi parameetreid saan seadistada OpenAiOfficialChatModel.builder() sees?"
> - "Kuidas lisada voogedastusega vastuseid selle asemel, et oodata täielikku vastust?"

**Prompti tehnika** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Nüüd kui tead, kuidas mudeliga rääkida, uurime, mida sa talle ütled. See demo kasutab sama mudeli seadistust, kuid näitab viit erinevat promptimise mustrit. Proovi zero-shot prompti otsete instruktsioonide jaoks, few-shot prompti õpiks näidete põhjal, kettarvutlust penelduseks ja rollipõhist prompti konteksti määramiseks. Näed, kuidas sama mudel annab väga erinevaid tulemusi sõltuvalt sellest, kuidas päringu vormistad.

Demo demonstreerib ka prompti malle, mis on võimas viis korduvkasutatavate promptide loomiseks koos muutujatega.
Allpool on näide promptist, mis kasutab LangChain4j `PromptTemplate`-i muutujate täitmiseks. AI vastab vastavalt antud sihtkohale ja tegevusele.

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

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Ava [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ja küsi:
> - "Mis vahe on zero-shot ja few-shot promptimisel ja millal kumbagi kasutada?"
> - "Kuidas mõjutab temperatuuriparameeter mudeli vastuseid?"
> - "Millised on tehnikad prompti süstimise rünnakute vältimiseks tootmises?"
> - "Kuidas luua korduvkasutatavaid PromptTemplate objekte tavapäraste mustrite jaoks?"

**Tööriistade integreerimine** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

Siin muutub LangChain4j võimsaks. Kasutad `AiServices` luua AI assistenti, kes saab kutsuda sinu Java meetodeid. Lihtsalt märgista meetodid `@Tool("kirjeldus")` annotatsiooniga ja LangChain4j võtab ülejäänu enda peale – AI otsustab automaatselt, millal iga tööriista kasutada vastavalt kasutaja päringule. See demonstreerib funktsioonikõnesid, mis on võtmetehnika luua AI, mis suudab tegutseda, mitte ainult vastata küsimustele.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Ava [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ja küsi:
> - "Kuidas @Tool annotatsioon töötab ja mida LangChain4j sellega taga tegeleb?"
> - "Kas AI saab järjest kutsuda mitut tööriista keeruliste probleemide lahendamiseks?"
> - "Mis juhtub, kui tööriist viskab erindi – kuidas peaksin vigadega tegelema?"
> - "Kuidas ma integreeriksin päris API selle kalkulaatori näite asemel?"

**Dokumendi Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

Siin näed RAG (otsingupõhise genereerimise) alustalasid. Selle asemel, et tugineda mudeli treeningandmetele, laed sisu failist [`document.txt`](../../../00-quick-start/document.txt) ja lisad selle prompti. AI vastab sinu dokumendi põhjal, mitte oma üldteadmiste põhjal. See on esimene samm süsteemide ehitamisel, mis töötavad sinu enda andmetega.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **Märkus:** See lihtne lähenemine laadib kogu dokumendi prompti. Suurtes failides (>10KB) ületad konteksti limiidid. Moodul 03 käsitleb faili tükkideks jagamist ja vektoriotsingut tootmises RAG süsteemide jaoks.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Ava [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ja küsi:
> - "Kuidas RAG takistab AI hallutsinatsioone võrreldes mudeli treeningandmete kasutamisega?"
> - "Mis vahe on selle lihtsa lähenemise ja vektor-embedingsi kasutamise vahel otsingus?"
> - "Kuidas ma skaaleeriksin seda mitme dokumendi või suuremate teadmistebaaside jaoks?"
> - "Millised on parimad tavajuhud prompti struktureerimiseks, et AI kasutaks vaid antud konteksti?"

**Vastutustundlik tehisintellekt** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

Ehita AI turvalisust kaitse kihina. See demo näitab kahte kaitsekihte, mis töötavad koos:

**Osa 1: LangChain4j sisendikaitseliinid** – Blokeerib ohtlikud promptid enne LLM-i jõudmist. Loo enda kohandatud kaitseliinid, mis kontrollivad keelatud võtmesõnu või mustreid. Need töötavad sinu koodis, seega on need kiired ja tasuta.

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

**Osa 2: Pakkuja turvafiltrid** – GitHubi mudelitel on sisseehitatud filtrid, mis püüavad kinni, mida sinu kaitseliinid võib-olla ei tabanud. Näed tugevaid blokeeringuid (HTTP 400 vead) rasketes rikkumistes ja pehmeid keeldumisi, kus AI viisakalt keeldub.

> **🤖 Proovi [GitHub Copilot](https://github.com/features/copilot) vestlusega:** Ava [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ja küsi:
> - "Mis on InputGuardrail ja kuidas ma loon enda omasid?"
> - "Mis vahe on tugeval blokeeringul ja pehmel keeldumisel?"
> - "Miks kasutada samaaegselt nii kaitseliine kui ka pakkuja filtreid?"

## Järgmised sammud

**Järgmine moodul:** [01-introduction - LangChain4j ja gpt-5 kasutuselevõtt Azure-s](../01-introduction/README.md)

---

**Navigeerimine:** [← Tagasi põhiosa juurde](../README.md) | [Järgmine: Moodul 01 - Sissejuhatus →](../01-introduction/README.md)

---

## Tõrkeotsing

### Esmane Maven ehitus

**Probleem:** Algne `mvn clean compile` või `mvn package` võtab kaua aega (10-15 minutit)

**Põhjus:** Maven peab esialgsel ehitusel alla laadima kõik projekti sõltuvused (Spring Boot, LangChain4j teegid, Azure SDK-d jms).

**Lahendus:** See on normaalne käitumine. Järgmised ehitused on palju kiirem, kuna sõltuvused on lokaalselt vahemälus. Allalaadimise kiirus sõltub sinu võrguühendusest.
### PowerShelli Maveni käsu süntaks

**Probleem**: Maveni käsud ebaõnnestuvad veaga `Unknown lifecycle phase ".mainClass=..."`

**Põhjus**: PowerShell tõlgendab `=` kui muutujale väärtuse määramist, mis rikub Maven omaduste süntaksit

**Lahendus**: Kasuta käsu ette stop-parsing operaatorit `--%`:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

Operaator `--%` ütleb PowerShellile, et kõik ülejäänud argumendid antakse Mavenile edasi täpselt nii, nagu nad on, tõlgendamata neid.

### Windows PowerShelli emotikonide kuvamine

**Probleem**: AI vastustes kuvatakse PowerShellis emotikonide asemel rämpsmärke (nt `????` või `â??`)

**Põhjus**: PowerShelli vaikekodeering ei toeta UTF-8 emotikone

**Lahendus**: Käivita see käsk Java rakenduste käivitamise eel:
```cmd
chcp 65001
```

See sunnib terminali kasutama UTF-8 kodeeringut. Alternatiivina kasuta Windows Terminali, mis toetab paremini Unicode'i.

### API-kõnede silumine

**Probleem**: Autentimisvead, päringute limiidid või ootamatud vastused AI mudelilt

**Lahendus**: Näidetes on kasutatud `.logRequests(true)` ja `.logResponses(true)`, mis näitavad API-kõnesid konsoolis. See aitab autentimisvigade, limiitide või ootamatute vastuste lahendamisel. Eemalda need lipud tootmiskeskkonnas logimisülekülluse vältimiseks.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:  
See dokument on tõlgitud kasutades tehisintellekti tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame tagada täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega kaasnevate võimalike väärarusaamade või valesti mõistmiste eest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->