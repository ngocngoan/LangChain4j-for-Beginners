# LangChain4j Slovar

## Kazalo

- [Osnovni koncepti](../../../docs)
- [Komponente LangChain4j](../../../docs)
- [AI/ML koncepti](../../../docs)
- [Varovalke](../../../docs)
- [Inženiring pozivov](../../../docs)
- [RAG (generacija z iskanjem)](../../../docs)
- [Agenti in orodja](../../../docs)
- [Agentni modul](../../../docs)
- [Protokol konteksta modela (MCP)](../../../docs)
- [Azure storitve](../../../docs)
- [Testiranje in razvoj](../../../docs)

Hitri pregled terminov in konceptov, ki se uporabljajo skozi celoten tečaj.

## Osnovni koncepti

**AI agent** - Sistem, ki uporablja AI za razmišljanje in samostojno delovanje. [Modul 04](../04-tools/README.md)

**Veriga** - Zaporedje operacij, kjer izhod služi kot vhod v naslednji korak.

**Deljenje na kose (chunking)** - Razbijanje dokumentov na manjše dele. Običajno: 300-500 žetonov s prekrivanjem. [Modul 03](../03-rag/README.md)

**Kontekstno okno** - Največje število žetonov, ki jih model lahko obdela. GPT-5.2: 400K žetonov.

**Vdelave (embeddings)** - Numerične vektorje, ki predstavljajo pomen besedila. [Modul 03](../03-rag/README.md)

**Klic funkcij** - Model generira strukturirane zahteve za klic zunanjih funkcij. [Modul 04](../04-tools/README.md)

**Halucinacija** - Ko modeli generirajo napačne, a verjetne informacije.

**Poziv (prompt)** - Besedilni vhod v jezikovni model. [Modul 02](../02-prompt-engineering/README.md)

**Semantično iskanje** - Iskanje po pomenu z uporabo vdelav, ne ključnih besed. [Modul 03](../03-rag/README.md)

**Stanje z zgodovino vs brez** - Brez stanja: brez spomina. S stanjem: hrani zgodovino pogovora. [Modul 01](../01-introduction/README.md)

**Žetoni (tokens)** - Osnovne enote besedila, ki jih model obdela. Vplivajo na stroške in omejitve. [Modul 01](../01-introduction/README.md)

**Zaporedno uporabo orodij (tool chaining)** - Zaporedno izvajanje orodij, kjer izhod vpliva na naslednji klic. [Modul 04](../04-tools/README.md)

## Komponente LangChain4j

**AiServices** - Ustvarja tipno varne vmesnike za AI storitve.

**OpenAiOfficialChatModel** - Združeni odjemalec za OpenAI in Azure OpenAI modele.

**OpenAiOfficialEmbeddingModel** - Ustvarja vdelave z uporabo uradnega OpenAI odjemalca (podpira OpenAI in Azure OpenAI).

**ChatModel** - Osnovni vmesnik za jezikovne modele.

**ChatMemory** - Ohranja zgodovino pogovora.

**ContentRetriever** - Najde ustrezne dele dokumentov za RAG.

**DocumentSplitter** - Razbije dokumente na kose.

**EmbeddingModel** - Pretvori besedilo v numerične vektorje.

**EmbeddingStore** - Shranjuje in pridobiva vdelave.

**MessageWindowChatMemory** - Ohranja pomikajoče se okno zadnjih sporočil.

**PromptTemplate** - Ustvari ponovno uporabne pozive z `{{variable}}` mestniki.

**TextSegment** - Besedilni kos z metapodatki. Uporablja se v RAG.

**ToolExecutionRequest** - Predstavlja zahtevo za izvajanje orodja.

**UserMessage / AiMessage / SystemMessage** - Vrste sporočil v pogovoru.

## AI/ML koncepti

**Few-Shot učenje** - Zagotavljanje primerov v pozivih. [Modul 02](../02-prompt-engineering/README.md)

**Veliki jezikovni model (LLM)** - AI modeli, usposobljeni na ogromnih besedilnih podatkih.

**Prizadevanje za razmišljanje** - Parameter GPT-5.2, ki nadzoruje globino razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

**Temperatura** - Nadzor naključnosti izhoda. Nizka=deterministična, visoka=ustvarjalna.

**Vektorska baza podatkov** - Specializirana baza za vdelave. [Modul 03](../03-rag/README.md)

**Zero-Shot učenje** - Izvajanje nalog brez primerov. [Modul 02](../02-prompt-engineering/README.md)

## Varovalke - [Modul 00](../00-quick-start/README.md)

**Obramba v globini** - Večplastni varnostni pristop, ki združuje varovalke na nivoju aplikacije z varnostnimi filtri ponudnika.

**Trda blokada** - Ponudnik vrne napako HTTP 400 za hude vsebinske kršitve.

**InputGuardrail** - Vmesnik LangChain4j za preverjanje uporabniškega vnosa, preden doseže LLM. Prihrani stroške in zakasnitev z zgodnjim blokiranjem škodljivih pozivov.

**InputGuardrailResult** - Tip vrnitve za preverjanje varovalk: `success()` ali `fatal("razlog")`.

**OutputGuardrail** - Vmesnik za preverjanje AI odgovorov pred vrnitvijo uporabnikom.

**Varnostni filtri ponudnika** - Vgrajeni filtri vsebine AI ponudnikov (npr. GitHub modeli), ki ujamejo kršitve na ravni API.

**Nežna zavrnitev** - Model vljudno zavrne odgovor, ne da bi vrgel napako.

## Inženiring pozivov - [Modul 02](../02-prompt-engineering/README.md)

**Veriga razmišljanja (Chain-of-Thought)** - Korak za korakom razmišljanje za večjo natančnost.

**Omejen izhod** - Uveljavljanje določenega formata ali strukture.

**Visoka prizadetost** - Vzorec GPT-5.2 za temeljito razmišljanje.

**Nizka prizadetost** - Vzorec GPT-5.2 za hitre odgovore.

**Večbesedilni pogovor** - Ohranjanje konteksta skozi izmenjave.

**Pozivanje po vlogah** - Nastavitev osebnosti modela preko sistemskih sporočil.

**Samorefleksija** - Model ovrednoti in izboljša svoj izhod.

**Strukturirana analiza** - Fiksiran okvir za ocenjevanje.

**Vzorec izvajanja nalog** - Načrtuj → Izvajaj → Povzemi.

## RAG (generacija z iskanjem) - [Modul 03](../03-rag/README.md)

**Procesna veriga obdelave dokumentov** - Naloži → razdeli → vdelaj → shrani.

**Vdelave v pomnilniku** - Nepersistentno shranjevanje za testiranje.

**RAG** - Kombinacija iskanja z generacijo za utemeljitev odgovorov.

**Stopnja podobnosti** - Merilo (0-1) semantične podobnosti.

**Referenca vira** - Metapodatki o najdeni vsebini.

## Agenti in orodja - [Modul 04](../04-tools/README.md)

**@Tool označba** - Označuje Java metode kot orodja, ki jih lahko kliče AI.

**ReAct vzorec** - Razmišljaj → Ukrepaj → Opazuj → Ponovi.

**Upravljanje sej** - Ločeni konteksti za različne uporabnike.

**Orodje** - Funkcija, ki jo lahko kliče AI agent.

**Opis orodja** - Dokumentacija namena orodja in parametrov.

## Agentni modul - [Modul 05](../05-mcp/README.md)

**@Agent označba** - Označuje vmesnike kot AI agente z deklarativno definicijo vedenja.

**Poslušalec agenta** - Hook za spremljanje izvajanja agenta prek `beforeAgentInvocation()` in `afterAgentInvocation()`.

**Agentni obseg** - Deljeni pomnilnik, kjer agenti shranjujejo izhode z uporabo `outputKey` za uporabo s strani drugih agentov.

**AgenticServices** - Tovor za ustvarjanje agentov z `agentBuilder()` in `supervisorBuilder()`.

**Pogojno potek dela** - Usmerjanje na podlagi pogojev do različnih specializiranih agentov.

**Človek v zanki** - Vzorec poteka dela, ki vključuje človeške kontrolne točke za odobritev ali pregled vsebine.

**langchain4j-agentic** - Maven odvisnost za deklarativno gradnjo agentov (eksperimentalno).

**Petlja potek dela** - Ponovni zagon izvajanja agenta dokler ni izpolnjen pogoj (npr. ocena kakovosti ≥ 0,8).

**outputKey** - Parameter agentne oznake, ki določa, kje so rezultati shranjeni v agentnem obsegu.

**Vzporedni potek dela** - Hkratno izvajanje več agentov za neodvisne naloge.

**Strategija odgovora** - Kako nadzornik oblikuje končni odgovor: ZADNJI, POVZETEK ali OCENJENO.

**Zaporedni potek dela** - Izvajanje agentov po vrsti, kjer izhod teče v naslednji korak.

**Vzorec nadzornega agenta** - Napreden agentni vzorec, kjer nadzornik LLM dinamično odloča, katere podagente izvajati.

## Protokol konteksta modela (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven odvisnost za MCP integracijo v LangChain4j.

**MCP** - Protokol konteksta modela: standard za povezovanje AI aplikacij z zunanjimi orodji. Naredi enkrat, uporabi povsod.

**MCP odjemalec** - Aplikacija, ki se poveže na MCP strežnike za odkrivanje in uporabo orodij.

**MCP strežnik** - Storitev, ki preko MCP razkriva orodja z jasnimi opisi in shemami parametrov.

**McpToolProvider** - Komponenta LangChain4j, ki ovojnica MCP orodja za uporabo v AI storitvah in agentih.

**McpTransport** - Vmesnik za MCP komunikacijo. Implementacije vključujejo Stdio in HTTP.

**Stdio Transport** - Lokalni prenos procesov prek stdin/stdout. Uporaben za dostop do datotečnega sistema ali ukazne vrstice.

**StdioMcpTransport** - LangChain4j implementacija, ki zažene MCP strežnik kot podproces.

**Iskanje orodij** - Odjemalec poizveduje strežnik o razpoložljivih orodjih z opisi in shemami.

## Azure storitve - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Oblačno iskanje z vektorskimi zmogljivostmi. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Namesti Azure vire.

**Azure OpenAI** - Microsoftova enterprise AI storitev.

**Bicep** - Azure jezik za infrastrukturo kot kodo. [Vodnik za infrastrukturo](../01-introduction/infra/README.md)

**Ime nameščanja** - Ime za namestitev modela v Azure.

**GPT-5.2** - Najnovejši OpenAI model z nadzorom razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

## Testiranje in razvoj - [Vodnik za testiranje](TESTING.md)

**Razvojni vsebnik (Dev Container)** - Kontejnerizirano razvojno okolje. [Konfiguracija](../../../.devcontainer/devcontainer.json)

**GitHub modeli** - Brezplačno igrišče za AI modele. [Modul 00](../00-quick-start/README.md)

**Testiranje v pomnilniku** - Testiranje z neperzistentnim shranjevanjem.

**Integracijsko testiranje** - Testiranje z dejansko infrastrukturo.

**Maven** - Orodje za avtomatizacijo gradnje Java.

**Mockito** - Okvir za poniževanje v Javi.

**Spring Boot** - Java aplikacijski okvir. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo storitve za strojno prevajanje [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatski prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v izvorni jeziku velja za avtoritativni vir. Za ključne informacije priporočamo strokovni prevod, opravljen s strani človeka. Za kakršne koli nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda, ne odgovarjamo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->