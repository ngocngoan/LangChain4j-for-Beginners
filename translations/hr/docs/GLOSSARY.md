# Rječnik LangChain4j

## Sadržaj

- [Osnovni pojmovi](../../../docs)
- [Komponente LangChain4j](../../../docs)
- [AI/ML pojmovi](../../../docs)
- [Sigurnosne mjere](../../../docs)
- [Inženjerstvo prompta](../../../docs)
- [RAG (generiranje potpomognuto pretraživanjem)](../../../docs)
- [Agent i alati](../../../docs)
- [Agentni modul](../../../docs)
- [Protokol konteksta modela (MCP)](../../../docs)
- [Azure usluge](../../../docs)
- [Testiranje i razvoj](../../../docs)

Brzi pregled pojmova i koncepata korištenih kroz cijeli tečaj.

## Osnovni pojmovi

**AI agent** - Sustav koji koristi AI za rasuđivanje i autonomno djelovanje. [Modul 04](../04-tools/README.md)

**Lanac** - Niz operacija gdje izlaz služi kao ulaz za sljedeći korak.

**Dijeljenje na dijelove** - Razbijanje dokumenata na manje dijelove. Tipično: 300-500 tokena s preklapanjem. [Modul 03](../03-rag/README.md)

**Prozor konteksta** - Maksimalni broj tokena koje model može obraditi. GPT-5.2: 400 tisuća tokena.

**Umetci (embedding)** - Numerički vektori koji predstavljaju značenje teksta. [Modul 03](../03-rag/README.md)

**Pozivanje funkcija** - Model generira strukturirane zahtjeve za pozivanje vanjskih funkcija. [Modul 04](../04-tools/README.md)

**Halucinacija** - Kada modeli generiraju netočne, ali uvjerljive informacije.

**Prompt** - Tekstualni unos za jezični model. [Modul 02](../02-prompt-engineering/README.md)

**Semantičko pretraživanje** - Pretraživanje po značenju koristeći umetke, a ne ključne riječi. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: bez memorije. Stateful: održava povijest razgovora. [Modul 01](../01-introduction/README.md)

**Tokeni** - Temeljne jedinice teksta koje modeli obrađuju. Utječu na troškove i ograničenja. [Modul 01](../01-introduction/README.md)

**Lančano korištenje alata** - Sekvencijsko izvršavanje alata gdje izlaz informira sljedeći poziv. [Modul 04](../04-tools/README.md)

## Komponente LangChain4j

**AiServices** - Stvara tipovno sigurne AI servisne sučelja.

**OpenAiOfficialChatModel** - Jedinstveni klijent za OpenAI i Azure OpenAI modele.

**OpenAiOfficialEmbeddingModel** - Stvara umetke koristeći OpenAI Official klijent (podržava obje, OpenAI i Azure OpenAI).

**ChatModel** - Osnovno sučelje za jezične modele.

**ChatMemory** - Održava povijest razgovora.

**ContentRetriever** - Pronalazi relevantne dijelove dokumenata za RAG.

**DocumentSplitter** - Dijeli dokumente na dijelove.

**EmbeddingModel** - Pretvara tekst u numeričke vektore.

**EmbeddingStore** - Spremište i dohvat umetcima.

**MessageWindowChatMemory** - Održava pomični prozor nedavnih poruka.

**PromptTemplate** - Stvara ponovljive prompte s `{{promjenjivka}}` zamjenskim mjestima.

**TextSegment** - Tekstualni dio s metapodacima. Koristi se u RAG-u.

**ToolExecutionRequest** - Predstavlja zahtjev za izvršavanje alata.

**UserMessage / AiMessage / SystemMessage** - Tipovi poruka u razgovoru.

## AI/ML pojmovi

**Few-Shot učenje** - Davanje primjera u promptu. [Modul 02](../02-prompt-engineering/README.md)

**Veliki jezični model (LLM)** - AI modeli trenirani na velikim količinama teksta.

**Napori u rasuđivanju** - Parametar GPT-5.2 koji kontrolira dubinu razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

**Temperatura** - Kontrolira nasumičnost izlaza. Niska=deterministički, visoka=kreativan.

**Vektorska baza podataka** - Specijalizirana baza za umetke. [Modul 03](../03-rag/README.md)

**Zero-Shot učenje** - Izvršavanje zadataka bez primjera. [Modul 02](../02-prompt-engineering/README.md)

## Sigurnosne mjere - [Modul 00](../00-quick-start/README.md)

**Obrana u dubini** - Višeslojni sigurnosni pristup koji kombinira aplikacijske zaštite sa sigurnosnim filterima pružatelja usluga.

**Čvrsta blokada** - Pružatelj usluge baca HTTP 400 pogrešku za ozbiljne kršenja sadržaja.

**InputGuardrail** - LangChain4j sučelje za provjeru korisničkog unosa prije nego dođe do LLM-a. Štedi troškove i latenciju blokirajući štetne promptove rano.

**InputGuardrailResult** - Povratni tip za validaciju guardraila: `success()` ili `fatal("razlog")`.

**OutputGuardrail** - Sučelje za validaciju AI odgovora prije vraćanja korisnicima.

**Sigurnosni filteri pružatelja** - Ugrađeni filtri sadržaja od AI pružatelja (npr. GitHub modeli) koji odbacuju sadržaje na razini API-ja.

**Nježno odbijanje** - Model ljubazno odbija odgovoriti bez bacanja pogreške.

## Inženjerstvo prompta - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Korak-po-korak razmišljanje za veću točnost.

**Ograničen izlaz** - Nametanje specifičnog formata ili strukture.

**Visoka željnost** - GPT-5.2 obrazac za temeljito razmišljanje.

**Niska željnost** - GPT-5.2 obrazac za brze odgovore.

**Višekratni razgovor** - Održavanje konteksta kroz izmjene.

**Promptiranje uloga** - Postavljanje modelne persone putem sistemskih poruka.

**Samo-refleksija** - Model evaluira i poboljšava svoj izlaz.

**Strukturirana analiza** - Fiksni okvir za evaluaciju.

**Obrazac izvršavanja zadatka** - Plan → Izvrši → Sažmi.

## RAG (generiranje potpomognuto pretraživanjem) - [Modul 03](../03-rag/README.md)

**Cjevovod obrade dokumenata** - Učitaj → razdijeli → ugradi → spremi.

**Spremište umetaka u memoriji** - Nepostojana pohrana za testiranje.

**RAG** - Kombinacija dohvaćanja i generiranja za temeljenje odgovora.

**Ocjena sličnosti** - Mjera (0-1) semantičke sličnosti.

**Referenca izvora** - Metapodaci o dohvaćenom sadržaju.

## Agenti i alati - [Modul 04](../04-tools/README.md)

**@Tool anotacija** - Označava Java metode kao AI-pozive alata.

**ReAct obrazac** - Rasudi → Djeluj → Promatraj → Ponavljaj.

**Upravljanje sesijom** - Odvojeni konteksti za različite korisnike.

**Alat** - Funkcija koju AI agent može pozvati.

**Opis alata** - Dokumentacija o svrsi i parametrima alata.

## Agentni modul - [Modul 05](../05-mcp/README.md)

**@Agent anotacija** - Označava sučelja kao AI agente s deklarativnim definiranjam ponašanja.

**Agent Listener** - Hook za nadzor izvršavanja agenta putem `beforeAgentInvocation()` i `afterAgentInvocation()`.

**Agentni opseg** - Dijeljena memorija gdje agenti spremaju izlaze koristeći `outputKey` za potrošnju drugih agenata.

**AgenticServices** - Tvornica za stvaranje agenata koristeći `agentBuilder()` i `supervisorBuilder()`.

**Uvjetni tijek rada** - Usmjeravanje prema različitim specijalistima agentima na temelju uvjeta.

**Čovjek-u-petlji** - Obrazac tijeka rada koji dodaje ljudske kontrolne točke za odobrenje ili pregled sadržaja.

**langchain4j-agentic** - Maven ovisnost za deklarativnu izgradnju agenata (eksperimentalno).

**Petlja u tijeku rada** - Ponavljanje izvršavanja agenta dok se ne zadovolji uvjet (npr. ocjena kvalitete ≥ 0.8).

**outputKey** - Parametar anotacije agenta koji specificira gdje se rezultati spremaju u Agentni opseg.

**Paralelni tijek rada** - Paralelno pokretanje više agenata za neovisne zadatke.

**Strategija odgovora** - Kako nadzornik formulira konačni odgovor: POSLJEDNJI, SAŽETAK ili OCJENJENO.

**Sekvencijalni tijek rada** - Izvršavanje agenata po redu gdje izlaz teče u sljedeći korak.

**Obrazac nadzornog agenta** - Napredni agentni obrazac u kojem nadzornik LLM dinamički odlučuje koje pod-agente pozvati.

## Protokol konteksta modela (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven ovisnost za MCP integraciju u LangChain4j.

**MCP** - Protokol konteksta modela: standard za povezivanje AI aplikacija s vanjskim alatima. Izgradi jednom, koristi svugdje.

**MCP klijent** - Aplikacija koja se povezuje na MCP servere da bi otkrivala i koristila alate.

**MCP server** - Usluga koja izlaže alate preko MCP-a s jasnim opisima i shemama parametara.

**McpToolProvider** - LangChain4j komponenta koja obavija MCP alate za korištenje u AI servisima i agentima.

**McpTransport** - Sučelje za MCP komunikaciju. Implementacije uključuju Stdio i HTTP.

**Stdio transport** - Lokalan prijenos procesa preko stdin/stdout. Koristan za pristup datotečnom sustavu ili alate komandne linije.

**StdioMcpTransport** - LangChain4j implementacija koja pokreće MCP server kao podproces.

**Otkrivanje alata** - Klijent upitima traži dostupne alate sa opisima i shemama.

## Azure usluge - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Oblak pretraživanje s vektorskim mogućnostima. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deploy Azure resursa.

**Azure OpenAI** - Microsoftova poslovna AI usluga.

**Bicep** - Azure jezik infrastrukture kao koda. [Vodič za infrastrukturu](../01-introduction/infra/README.md)

**Naziv implementacije** - Ime za model implementaciju u Azure-u.

**GPT-5.2** - Najnoviji OpenAI model s kontrolom rasuđivanja. [Modul 02](../02-prompt-engineering/README.md)

## Testiranje i razvoj - [Vodič za testiranje](TESTING.md)

**Dev Container** - Kontejnerizirano razvojno okruženje. [Konfiguracija](../../../.devcontainer/devcontainer.json)

**GitHub modeli** - Besplatno AI okruženje za isprobavanje modela. [Modul 00](../00-quick-start/README.md)

**Testiranje u memoriji** - Testiranje s pohranom u memoriji.

**Integracijsko testiranje** - Testiranje s pravom infrastrukturom.

**Maven** - Java alat za automatizaciju gradnje.

**Mockito** - Java okvir za izradu lažnih objekata.

**Spring Boot** - Java aplikacijski okvir. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden pomoću AI usluge za prijevod [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim izvorom. Za kritične informacije preporučuje se profesionalni ljudski prijevod. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->