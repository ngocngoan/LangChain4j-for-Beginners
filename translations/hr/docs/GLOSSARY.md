# Rječnik LangChain4j

## Sadržaj

- [Osnovni pojmovi](../../../docs)
- [Komponente LangChain4j](../../../docs)
- [Pojmovi AI/ML](../../../docs)
- [Sigurnosne mjere](../../../docs)
- [Inženjerstvo upita](../../../docs)
- [RAG (Generiranje uz pomoć dohvaćanja)](../../../docs)
- [Agent i alati](../../../docs)
- [Agentni modul](../../../docs)
- [Protokol konteksta modela (MCP)](../../../docs)
- [Azure usluge](../../../docs)
- [Testiranje i razvoj](../../../docs)

Brzi pregled termina i pojmova korištenih tijekom tečaja.

## Osnovni pojmovi

**AI Agent** - Sustav koji koristi AI za autonomno zaključivanje i djelovanje. [Modul 04](../04-tools/README.md)

**Lanac** - Sekvenca operacija gdje izlaz služi kao ulaz za sljedeći korak.

**Chunking** - Razbijanje dokumenata na manje dijelove. Tipično: 300-500 tokena s preklapanjem. [Modul 03](../03-rag/README.md)

**Prozor konteksta** - Maksimalan broj tokena koje model može obraditi. GPT-5.2: 400K tokena (do 272K ulaz, 128K izlaz).

**Ugrađivanja (Embeddings)** - Numerički vektori koji predstavljaju značenje teksta. [Modul 03](../03-rag/README.md)

**Pozivanje funkcije** - Model generira strukturirane zahtjeve za pozivanje vanjskih funkcija. [Modul 04](../04-tools/README.md)

**Halucinacija** - Kada modeli generiraju netočne, ali uvjerljive informacije.

**Upit (Prompt)** - Tekstualni ulaz za jezični model. [Modul 02](../02-prompt-engineering/README.md)

**Semantičko pretraživanje** - Pretraživanje prema značenju koristeći ugrađivanja, ne ključne riječi. [Modul 03](../03-rag/README.md)

**Stanje vs Bez stanja** - Bez stanja: bez memorije. Sa stanjem: čuva povijest razgovora. [Modul 01](../01-introduction/README.md)

**Tokeni** - Osnovne tekstualne jedinice koje modeli obrađuju. Utječu na troškove i ograničenja. [Modul 01](../01-introduction/README.md)

**Lančano korištenje alata** - Sekvencijalno izvođenje alata gdje izlaz informira sljedeći poziv. [Modul 04](../04-tools/README.md)

## Komponente LangChain4j

**AiServices** - Stvara tip-sigurne sučelja AI usluga.

**OpenAiOfficialChatModel** - Ujedinjeni klijent za OpenAI i Azure OpenAI modele.

**OpenAiOfficialEmbeddingModel** - Stvara ugrađivanja pomoću službenog OpenAI klijenta (podržava i OpenAI i Azure OpenAI).

**ChatModel** - Osnovno sučelje za jezične modele.

**ChatMemory** - Čuva povijest razgovora.

**ContentRetriever** - Pronalazi relevantne dijelove dokumenata za RAG.

**DocumentSplitter** - Razbija dokumente na dijelove.

**EmbeddingModel** - Pretvara tekst u numeričke vektore.

**EmbeddingStore** - Pohranjuje i dohvaća ugrađivanja.

**MessageWindowChatMemory** - Održava pomični prozor nedavnih poruka.

**PromptTemplate** - Stvara ponovno upotrebljive upite s oznakama `{{variable}}`.

**TextSegment** - Tekstualni dio s metapodacima. Koristi se u RAG.

**ToolExecutionRequest** - Predstavlja zahtjev za izvršenje alata.

**UserMessage / AiMessage / SystemMessage** - Vrste poruka u razgovoru.

## Pojmovi AI/ML

**Few-Shot učenje** - Davanje primjera u upitima. [Modul 02](../02-prompt-engineering/README.md)

**Veliki jezični model (LLM)** - AI modeli trenirani na ogromnim količinama tekstualnih podataka.

**Napori zaključivanja** - Parametar GPT-5.2 koji kontrolira dubinu razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

**Temperatura** - Kontrolira nasumičnost izlaza. Niska=deterministički, visoka=kreativni.

**Vektorska baza podataka** - Specijalizirana baza za ugrađivanja. [Modul 03](../03-rag/README.md)

**Zero-Shot učenje** - Izvođenje zadataka bez primjera. [Modul 02](../02-prompt-engineering/README.md)

## Sigurnosne mjere - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Višeslojni sigurnosni pristup koji kombinira sigurnosne mjere na razini aplikacije s provjerama pružatelja usluga.

**Hard Block** - Pružatelj vraća HTTP 400 grešku za ozbiljne povrede sadržaja.

**InputGuardrail** - Sučelje LangChain4j za provjeru korisničkog unosa prije nego što dođe do LLM-a. Štedi troškove i kašnjenje blokiranjem štetnih upita rano.

**InputGuardrailResult** - Povratni tip za validaciju čuvara: `success()` ili `fatal("razlog")`.

**OutputGuardrail** - Sučelje za provjeru AI odgovora prije vraćanja korisnicima.

**Provider Safety Filters** - Ugrađeni filtri sadržaja pružatelja AI usluga (npr. GitHub Models) koji hvataju povrede na razini API-ja.

**Soft Refusal** - Model pristojno odbija odgovoriti bez bacanja greške.

## Inženjerstvo upita - [Modul 02](../02-prompt-engineering/README.md)

**Lanac razmišljanja** - Zaključenje korak po korak za veću preciznost.

**Ograničeni izlaz** - Nametanje određenog formata ili strukture.

**Visoka želja za odgovorom** - Uzorak GPT-5.2 za temeljito zaključivanje.

**Niska želja za odgovorom** - Uzorak GPT-5.2 za brze odgovore.

**Višekratni razgovor** - Održavanje konteksta kroz razmjenu poruka.

**Uloge u upitu** - Postavljanje modela kroz sistemske poruke.

**Samosvijest** - Model procjenjuje i poboljšava svoj izlaz.

**Strukturirana analiza** - Fiksni okvir za evaluaciju.

**Uzorkovanje izvršenja zadatka** - Planiraj → Izvrši → Sažmi.

## RAG (Generiranje uz pomoć dohvaćanja) - [Modul 03](../03-rag/README.md)

**Cjevovod obrade dokumenata** - Učitavanje → dijeljenje → ugrađivanje → pohrana.

**Pohrana ugrađivanja u memoriji** - Nepostojana pohrana za testiranje.

**RAG** - Kombinira dohvaćanje i generiranje za utemeljenje odgovora.

**Ocjena sličnosti** - Mjera (0-1) semantičke sličnosti.

**Izvorni referent** - Metapodaci o dohvaćenom sadržaju.

## Agent i alati - [Modul 04](../04-tools/README.md)

**@Tool anotacija** - Označava Java metode kao AI-pozive alata.

**ReAct uzorak** - Razmišljaj → Djeluj → Promatraj → Ponavljaj.

**Upravljanje sesijom** - Odvojeni konteksti za različite korisnike.

**Alat** - Funkcija koju AI agent može pozvati.

**Opis alata** - Dokumentacija svrhe i parametara alata.

## Agentni modul - [Modul 05](../05-mcp/README.md)

**@Agent anotacija** - Označava sučelja kao AI agente s deklarativnim definiranje ponašanja.

**Agentni slušatelj** - Kukica za praćenje izvršenja agenta preko `beforeAgentInvocation()` i `afterAgentInvocation()`.

**Agentni opseg** - Zajednička memorija gdje agenti pohranjuju rezultate koristeći `outputKey` za korištenje od strane drugih agenata.

**AgenticServices** - Tvornica za kreiranje agenata s `agentBuilder()` i `supervisorBuilder()`.

**Uvjetni tok rada** - Usmjeravanje prema uvjetima različitim specijalističkim agentima.

**Ljudski u petlji** - Uzorak toka rada koji dodaje ljudsku kontrolu za odobrenje ili pregled sadržaja.

**langchain4j-agentic** - Maven ovisnost za deklarativnu izgradnju agenata (eksperimentalno).

**Petlja toka rada** - Ponavljanje izvršenja agenta dok se ne zadovolji uvjet (npr. ocjena kvalitete ≥ 0.8).

**outputKey** - Parametar anotacije agenta koji specificira gdje se rezultati pohranjuju u Agentni opseg.

**Paralelni tok rada** - Istovremeno pokretanje više agenata za neovisne zadatke.

**Strategija odgovora** - Kako nadzornik formulira konačni odgovor: LAST, SUMMARY ili SCORED.

**Sekvencijalni tok rada** - Izvršavanje agenata po redu gdje izlaz teče u sljedeći korak.

**Uzorak nadzornog agenta** - Napredni agentni uzorak gdje nadzorni LLM dinamički odlučuje koje pod-agente pozvati.

## Protokol konteksta modela (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven ovisnost za MCP integraciju u LangChain4j.

**MCP** - Protokol konteksta modela: standard za povezivanje AI aplikacija s vanjskim alatima. Izradi jednom, koristi svugdje.

**MCP klijent** - Aplikacija koja se povezuje s MCP serverima za otkrivanje i korištenje alata.

**MCP server** - Usluga koja izlaže alate preko MCP s jasnim opisima i šemama parametara.

**McpToolProvider** - Komponenta LangChain4j koja omotava MCP alate za korištenje u AI uslugama i agentima.

**McpTransport** - Sučelje za MCP komunikaciju. Implementacije uključuju Stdio i HTTP.

**Stdio transport** - Transport lokalnog procesa preko stdin/stdout. Korisno za pristup datotečnom sustavu ili komandnoj liniji.

**StdioMcpTransport** - Implementacija LangChain4j koja pokreće MCP server kao podproces.

**Otkriće alata** - Klijent pita server za dostupne alate s opisima i šemama.

## Azure usluge - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Oblak pretraživanja s vektorskim mogućnostima. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deploy Azure resursa.

**Azure OpenAI** - Microsoftova AI servisna platforma za poduzeća.

**Bicep** - Azure jezik za infrastrukturu kao kod. [Vodič za infrastrukturu](../01-introduction/infra/README.md)

**Naziv implementacije** - Ime za implementaciju modela u Azureu.

**GPT-5.2** - Najnoviji OpenAI model s kontrolom zaključivanja. [Modul 02](../02-prompt-engineering/README.md)

## Testiranje i razvoj - [Vodič za testiranje](TESTING.md)

**Dev Container** - Kontejnerizirano razvojno okruženje. [Konfiguracija](../../../.devcontainer/devcontainer.json)

**GitHub modeli** - Besplatno AI igralište za modele. [Modul 00](../00-quick-start/README.md)

**Testiranje u memoriji** - Testiranje s pohranom u memoriji.

**Integracijsko testiranje** - Testiranje s pravom infrastrukturom.

**Maven** - Alat za automatizaciju gradnje za Javu.

**Mockito** - Java okvir za izradu imitacija (mock).

**Spring Boot** - Java aplikacijski okvir. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Izjava o odricanju odgovornosti**:  
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo biti točni, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim i autoritativnim izvorom. Za ključne informacije preporučuje se profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili kriva tumačenja koja proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->