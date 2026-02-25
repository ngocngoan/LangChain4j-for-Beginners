# Modul 03: RAG (Generiranje z nadgradnjo pridobivanja)

## Kazalo

- [Kaj se boste naučili](../../../03-rag)
- [Razumevanje RAG](../../../03-rag)
- [Pogoji](../../../03-rag)
- [Kako deluje](../../../03-rag)
  - [Obdelava dokumentov](../../../03-rag)
  - [Ustvarjanje vdelav](../../../03-rag)
  - [Semantično iskanje](../../../03-rag)
  - [Generiranje odgovorov](../../../03-rag)
- [Zagon aplikacije](../../../03-rag)
- [Uporaba aplikacije](../../../03-rag)
  - [Naložite dokument](../../../03-rag)
  - [Postavljajte vprašanja](../../../03-rag)
  - [Preverite reference virov](../../../03-rag)
  - [Eksperimentirajte z vprašanji](../../../03-rag)
- [Ključni koncepti](../../../03-rag)
  - [Strategija razdeljevanja](../../../03-rag)
  - [Ocene podobnosti](../../../03-rag)
  - [Shranjevanje v pomnilniku](../../../03-rag)
  - [Upravljanje kontekstnega okna](../../../03-rag)
- [Kdaj je RAG pomemben](../../../03-rag)
- [Naslednji koraki](../../../03-rag)

## Kaj se boste naučili

V prejšnjih modulih ste se naučili, kako imeti pogovore z AI in kako učinkovito strukturirati svoje pozive. Toda obstaja temeljna omejitev: jezikovni modeli poznajo samo tisto, kar so se naučili med usposabljanjem. Ne morejo odgovarjati na vprašanja o pravilnikih vašega podjetja, vaši projektni dokumentaciji ali kakršnih koli informacijah, na katerih niso bili usposobljeni.

RAG (Generiranje z nadgradnjo pridobivanja) rešuje ta problem. Namesto da bi model poskušali naučiti vaših informacij (kar je drago in nepraktično), mu omogočite iskanje v vaših dokumentih. Ko nekdo postavi vprašanje, sistem poišče relevantne informacije in jih vključi v poziv. Model nato odgovori na podlagi pridobljenega konteksta.

Predstavljajte si RAG kot modelovo referenčno knjižnico. Ko postavite vprašanje, sistem:

1. **Uporabniško vprašanje** – postavite vprašanje
2. **Vdelava** – pretvori vaše vprašanje v vektor
3. **Iskanje v vektorskem prostoru** – poišče podobne dele dokumenta
4. **Sestavljanje konteksta** – doda relevantne dele v poziv
5. **Odgovor** – LLM ustvari odgovor na podlagi konteksta

To temelji odgovore modela na vaših dejanskih podatkih namesto da bi se zanašal na svoje znanje usposabljanja ali si izmislil odgovore.

## Razumevanje RAG

Spodnja shema ponazarja osnovni koncept: namesto da bi se zanašal samo na podatke usposabljanja modela, RAG modelu omogoči referenčno knjižnico vaših dokumentov, ki jih lahko pregleda pred generiranjem vsakega odgovora.

<img src="../../../translated_images/sl/what-is-rag.1f9005d44b07f2d8.webp" alt="Kaj je RAG" width="800"/>

Tako so deli povezani od začetka do konca. Uporabnikovo vprašanje prehaja skozi štiri faze — vdelava, vektorsko iskanje, sestavljanje konteksta in generiranje odgovora — vsaka gradi na prejšnji:

<img src="../../../translated_images/sl/rag-architecture.ccb53b71a6ce407f.webp" alt="Arhitektura RAG" width="800"/>

Preostali del tega modula podrobno opisuje vsako fazo, z izvorno kodo, ki jo lahko zaženete in spreminjate.

## Pogoji

- Zaključen Modul 01 (razporejeni Azure OpenAI viri)
- `.env` datoteka v korenskem imeniku z Azure poverilnicami (ustvarjena z `azd up` v Modulu 01)

> **Opomba:** Če niste zaključili Modula 01, najprej sledite navodilom za namestitev tam.

## Kako deluje

### Obdelava dokumentov

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

Ko naložite dokument, ga sistem obdela (PDF ali navadno besedilo), doda metapodatke, kot je ime datoteke, nato pa ga razdeli na dele — manjše kose, ki udobno ustrezajo kontekstnemu oknu modela. Ti deli se rahlo prekrivajo, da ne izgubite konteksta na mejah.

```java
// Razčleni naloženo datoteko in jo zavij v LangChain4j dokument
Document document = Document.from(content, metadata);

// Razdeli na 300-tokenne koščke z 30-tokennim prekrivanjem
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Spodnja shema prikazuje, kako to deluje vizualno. Opazite, da vsak del z nekaterimi sosedi deli nekaj tokenov — 30-tokena prekrivanje zagotavlja, da pomemben kontekst ne zaide med razpoke:

<img src="../../../translated_images/sl/document-chunking.a5df1dd1383431ed.webp" alt="Razdeljevanje dokumenta" width="800"/>

> **🤖 Poskusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) in vprašajte:
> - "Kako LangChain4j razdeli dokumente na dele in zakaj je prekrivanje pomembno?"
> - "Kakšna je optimalna velikost delov za različne vrste dokumentov in zakaj?"
> - "Kako obravnavam dokumente v več jezikih ali s posebno obliko?"

### Ustvarjanje vdelav

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Vsak del je pretvorjen v numerično predstavitev, imenovano vdelava - bistveno matematični prstni odtis, ki zajema pomen besedila. Podobno besedilo ustvarja podobne vdelave.

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

Razredni diagram spodaj prikazuje, kako so ti LangChain4j sestavni deli povezani. `OpenAiOfficialEmbeddingModel` pretvarja besedilo v vektorje, `InMemoryEmbeddingStore` hrani vektorje skupaj z njihovimi izvirnimi podatki `TextSegment`, `EmbeddingSearchRequest` pa nadzoruje parametre pridobivanja, kot so `maxResults` in `minScore`:

<img src="../../../translated_images/sl/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="Razredi LangChain4j RAG" width="800"/>

Ko so vdelave shranjene, se podobna vsebina naravno združuje skupaj v vektorskem prostoru. Vizualizacija spodaj prikazuje, kako dokumenti o sorodnih temah končajo kot bližnje točke, kar omogoča semantično iskanje:

<img src="../../../translated_images/sl/vector-embeddings.2ef7bdddac79a327.webp" alt="Vektorske vdelave" width="800"/>

### Semantično iskanje

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Ko postavite vprašanje, se vaše vprašanje prav tako pretvori v vdelavo. Sistem primerja vdelavo vašega vprašanja z vsemi vdelavami delov dokumenta. Poišče dele z najsličnejšimi pomeni – ne le ujemanja ključnih besed, ampak dejansko semantično podobnost.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

Spodnja shema primerja semantično iskanje s tradicionalnim iskanjem po ključnih besedah. Iskanje ključne besede "vozilo" izpusti del o "avtomobilih in tovornjakih", toda semantično iskanje razume, da pomenita isto in ga vrne kot ujemanje z visoko oceno:

<img src="../../../translated_images/sl/semantic-search.6b790f21c86b849d.webp" alt="Semantično iskanje" width="800"/>

> **🤖 Poskusite s [GitHub Copilot](https://github.com/features/copilot) Chat:** Odprite [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) in vprašajte:
> - "Kako deluje iskanje podobnosti z vdelavami in kaj določa oceno?"
> - "Katerega praga podobnosti naj uporabim in kako vpliva na rezultate?"
> - "Kako ravnam v primerih, ko ni najdenih relevantnih dokumentov?"

### Generiranje odgovorov

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Najbolj relevantni deli so združeni v strukturiran poziv, ki vključuje eksplicitna navodila, pridobljeni kontekst in uporabnikovo vprašanje. Model prebere te specifične dele in odgovori na podlagi teh informacij — lahko uporabi samo tisto, kar je pred njim, kar preprečuje halucinacijo.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

Spodnja shema prikazuje to sestavljanje v akciji — najvišje ocenjeni deli iz koraka iskanja so vstavljeni v predlogo poziva, `OpenAiOfficialChatModel` pa generira utemeljen odgovor:

<img src="../../../translated_images/sl/context-assembly.7e6dd60c31f95978.webp" alt="Sestavljanje konteksta" width="800"/>

## Zagon aplikacije

**Preverite namestitev:**

Prepričajte se, da `.env` datoteka obstaja v korenskem imeniku z Azure poverilnicami (ustvarjena med Modulom 01):
```bash
cat ../.env  # Prikazati bi moral AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Zaženite aplikacijo:**

> **Opomba:** Če ste že zagnali vse aplikacije z `./start-all.sh` iz Modula 01, ta modul teče na vratih 8081. Ukaze za zagon spodaj lahko preskočite in pojdite neposredno na http://localhost:8081.

**Možnost 1: Uporaba Spring Boot Dashboard (priporočeno za uporabnike VS Code)**

Razvojno okolje vsebuje razširitev Spring Boot Dashboard, ki omogoča vizualni vmesnik za upravljanje vseh Spring Boot aplikacij. Najdete jo lahko na Levem stranskem pasu v VS Code (poiščite ikono Spring Boot).

Iz Spring Boot Dashboard lahko:
- Vidite vse razpoložljive Spring Boot aplikacije v delovnem prostoru
- Zažene ali ustavite aplikacije z enim klikom
- V realnem času spremljate dnevnike aplikacij
- Nadzorujete stanje aplikacij

Preprosto kliknite gumb za zagon ob "rag" za začetek tega modula ali pa zaženite vse module naenkrat.

<img src="../../../translated_images/sl/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Možnost 2: Uporaba skript v terminalu**

Zaženite vse spletne aplikacije (moduli 01-04):

**Bash:**
```bash
cd ..  # Iz korenskega imenika
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # Iz korenskega imenika
.\start-all.ps1
```

Ali zaženite samo ta modul:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

Obe skripti samodejno naložita okoljske spremenljivke iz korenske `.env` datoteke in bosta zgradili JAR datoteke, če še ne obstajajo.

> **Opomba:** Če želite ročno zgraditi vse module pred zagonom:
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

Odprite http://localhost:8081 v svojem brskalniku.

**Za ustavitev:**

**Bash:**
```bash
./stop.sh  # Samo ta modul
# Ali
cd .. && ./stop-all.sh  # Vsi moduli
```

**PowerShell:**
```powershell
.\stop.ps1  # Samo ta modul
# Ali
cd ..; .\stop-all.ps1  # Vsi moduli
```

## Uporaba aplikacije

Aplikacija zagotavlja spletni vmesnik za nalaganje dokumentov in postavljanje vprašanj.

<a href="images/rag-homepage.png"><img src="../../../translated_images/sl/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG uporabniški vmesnik" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Uporabniški vmesnik RAG aplikacije – naložite dokumente in postavljajte vprašanja*

### Naložite dokument

Začnite z nalaganjem dokumenta – za testiranje najbolj ustrezajo TXT datoteke. V tem imeniku je na voljo `sample-document.txt`, ki vsebuje informacije o funkcijah LangChain4j, implementaciji RAG in najboljših praksah – odlično za testiranje sistema.

Sistem obdela vaš dokument, razdeli ga na dele in za vsak del ustvari vdelave. To se zgodi samodejno ob nalaganju.

### Postavljajte vprašanja

Zdaj postavite specifična vprašanja o vsebini dokumenta. Poskusite s kakšnim dejanskim vprašanjem, ki je jasno navedeno v dokumentu. Sistem poišče relevantne dele, jih vključi v poziv in generira odgovor.

### Preverite reference virov

Opazite, da vsak odgovor vključuje reference do virov z ocenami podobnosti. Te ocene (od 0 do 1) kažejo, kako relevanten je bil vsak del vašemu vprašanju. Višje ocene pomenijo boljše ujemanje. To vam omogoča, da preverite odgovor glede na izvorno gradivo.

<a href="images/rag-query-results.png"><img src="../../../translated_images/sl/rag-query-results.6d69fcec5397f355.webp" alt="Rezultati poizvedbe RAG" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Rezultati poizvedbe, ki prikazujejo odgovor z referencami virov in ocenami relevantnosti*

### Eksperimentirajte z vprašanji

Poskusite različne vrste vprašanj:
- Specifična dejstva: "Kakšna je glavna tema?"
- Primerjave: "Kakšna je razlika med X in Y?"
- Povzetki: "Povzemite ključne točke o Z"

Opazujte, kako se ocene relevantnosti spreminjajo glede na to, kako dobro se vaše vprašanje ujema z vsebino dokumenta.

## Ključni koncepti

### Strategija razdeljevanja

Dokumenti so razdeljeni na 300-tokenske dele z 30 tokeni prekrivanja. Ta razmerje zagotavlja, da ima vsak del dovolj konteksta, da je smiseln, hkrati pa ostane dovolj majhen, da lahko v pozivu vključite več delov.

### Ocene podobnosti

Vsak pridobljen del ima oceno podobnosti med 0 in 1, ki kaže, kako tesno se ujema z uporabnikovim vprašanjem. Spodnja shema vizualizira obsege ocen in kako jih sistem uporablja za filtriranje rezultatov:

<img src="../../../translated_images/sl/similarity-scores.b0716aa911abf7f0.webp" alt="Ocene podobnosti" width="800"/>

Ocene segajo od 0 do 1:
- 0,7-1,0: Zelo relevantno, natančno ujemanje
- 0,5-0,7: Relevantno, dober kontekst
- Pod 0,5: Izločeno, preveč nepodobno

Sistem pridobi samo dele nad minimalno mejo, da zagotovi kakovost.

### Shranjevanje v pomnilniku

Ta modul uporablja shranjevanje v pomnilniku zaradi preprostosti. Ko znova zaženete aplikacijo, so naloženi dokumenti izgubljeni. Produkcijski sistemi uporabljajo trajne vektorske baze podatkov, kot sta Qdrant ali Azure AI Search.

### Upravljanje kontekstnega okna

Vsak model ima največje kontekstno okno. Ne morete vključiti vseh delov velikega dokumenta. Sistem pridobi top N najbolj relevantnih delov (privzeto 5), da ostane znotraj omejitev, hkrati pa zagotavlja dovolj konteksta za natančne odgovore.

## Kdaj je RAG pomemben

RAG ni vedno pravi pristop. Spodnji vodič vam pomaga določiti, kdaj RAG predstavlja dodano vrednost, v primerjavi s preprostimi pristopi — kot je vključitev vsebine neposredno v poziv ali zanašanje na vgrajeno znanje modela:

<img src="../../../translated_images/sl/when-to-use-rag.1016223f6fea26bc.webp" alt="Kdaj uporabiti RAG" width="800"/>

**Uporabite RAG, ko:**
- Odgovarjanje na vprašanja o lastniških dokumentih  
- Informacije se pogosto spreminjajo (politike, cene, specifikacije)  
- Natančnost zahteva navedbo vira  
- Vsebina je prevelika, da bi jo lahko vključili v en sam poziv  
- Potrebni so preverljivi, utemeljeni odgovori  

**RAG ne uporabljajte, kadar:**  
- Vprašanja zahtevajo splošno znanje, ki ga model že ima  
- Potrebni so podatki v realnem času (RAG deluje na naloženih dokumentih)  
- Vsebina je dovolj majhna, da jo lahko vključite neposredno v pozive  

## Naslednji koraki  

**Naslednji modul:** [04-tools - AI agenti z orodji](../04-tools/README.md)  

---  

**Navigacija:** [← Prejšnji: Modul 02 - Ustvarjanje pozivov](../02-prompt-engineering/README.md) | [Nazaj na glavno](../README.md) | [Naslednji: Modul 04 - Orodja →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Opozorilo**:
Ta dokument je bil preveden z uporabo storitve za prevajanje z umetno inteligenco [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, upoštevajte, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem maternem jeziku naj se šteje za zanesljiv vir. Za kritične informacije priporočamo strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->