# Slovar LangChain4j

## Kazalo

- [Temeljni pojmi](../../../docs)
- [Komponente LangChain4j](../../../docs)
- [AI/ML pojmi](../../../docs)
- [Varovala](../../../docs)
- [Inženiring pozivov](../../../docs)
- [RAG (generiranje z iskanjem)](../../../docs)
- [Agentje in orodja](../../../docs)
- [Agentski modul](../../../docs)
- [Protokol konteksta modela (MCP)](../../../docs)
- [Azure storitve](../../../docs)
- [Testiranje in razvoj](../../../docs)

Hiter pregled izrazov in pojmov, uporabljenih v celotnem tečaju.

## Temeljni pojmi

**AI agent** - sistem, ki uporablja AI za razmišljanje in samostojno delovanje. [Modul 04](../04-tools/README.md)

**Veriga** - zaporedje operacij, kjer izhod služi kot vhod za naslednji korak.

**Razbijanje na kose** - razdeljevanje dokumentov na manjše dele. Običajno: 300-500 tokenov z prekrivanjem. [Modul 03](../03-rag/README.md)

**Kontekstno okno** - največje število tokenov, ki jih model lahko obdela. GPT-5.2: 400K tokenov (do 272K vhod, 128K izhod).

**Vdelave** - numerične vektorje, ki predstavljajo pomen besedila. [Modul 03](../03-rag/README.md)

**Klic funkcije** - model generira strukturirane zahteve za klicanje zunanjih funkcij. [Modul 04](../04-tools/README.md)

**Halucinacija** - ko modeli generirajo napačne, a verjetne informacije.

**Poziv** - besedilni vhod v jezikovni model. [Modul 02](../02-prompt-engineering/README.md)

**Semantično iskanje** - iskanje po pomenu z uporabo vdelav, ne ključnih besed. [Modul 03](../03-rag/README.md)

**Stanje s spominom vs brez spomina** - brez spomina: brez zgodovine pogovora. S spominom: ohranja zgodovino pogovora. [Modul 01](../01-introduction/README.md)

**Tokeni** - osnovne besedilne enote, ki jih modeli obdelujejo. Vplivajo na stroške in omejitve. [Modul 01](../01-introduction/README.md)

**Verižna uporaba orodij** - zaporedno izvajanje orodij, kjer izhod informira naslednji klic. [Modul 04](../04-tools/README.md)

## Komponente LangChain4j

**AiServices** - ustvarja varne vtipizirane vmesnike za AI storitve.

**OpenAiOfficialChatModel** - združeni odjemalec za OpenAI in Azure OpenAI modele.

**OpenAiOfficialEmbeddingModel** - ustvarja vdelave z uporabo uradnega OpenAI odjemalca (podpira tako OpenAI kot Azure OpenAI).

**ChatModel** - osrednji vmesnik za jezikovne modele.

**ChatMemory** - ohranja zgodovino pogovora.

**ContentRetriever** - išče relevantne kose dokumentov za RAG.

**DocumentSplitter** - razdeli dokumente na dele.

**EmbeddingModel** - pretvori besedilo v numerične vektorje.

**EmbeddingStore** - shrani in pridobi vdelave.

**MessageWindowChatMemory** - ohranja drsno okno zadnjih sporočil.

**PromptTemplate** - ustvarja znova uporabne pozive z označevalci `{{variable}}`.

**TextSegment** - kos besedila z metapodatki. Uporablja se v RAG.

**ToolExecutionRequest** - predstavlja zahtevo za izvajanje orodja.

**UserMessage / AiMessage / SystemMessage** - tipi sporočil v pogovoru.

## AI/ML pojmi

**Učenje z nekaj primeri** - zagotovitev primerov v pozivih. [Modul 02](../02-prompt-engineering/README.md)

**Veliki jezikovni model (LLM)** - AI modeli, usposobljeni na ogromnih količinah besedilnih podatkov.

**Prizadevanje za razumen odgovor** - parameter GPT-5.2 za nadzor globine razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

**Temperatura** - uravnava naključnost izhoda. Nizka = deterministična, visoka = ustvarjalna.

**Vektorska baza podatkov** - specializirana baza za vdelave. [Modul 03](../03-rag/README.md)

**Učenje brez primerov** - izvajanje nalog brez primerov. [Modul 02](../02-prompt-engineering/README.md)

## Varovala - [Modul 00](../00-quick-start/README.md)

**Obramba v globino** - večplastni varnostni pristop, ki združuje varovala na ravni aplikacij s filtri ponudnika.

**Trda blokada** - ponudnik vrže HTTP 400 napako za resne kršitve vsebine.

**InputGuardrail** - vmesnik LangChain4j za preverjanje vhodnih uporabniških podatkov preden dosežejo LLM. Prihrani stroške in zakasnitve z zgodnjo blokado škodljivih pozivov.

**InputGuardrailResult** - tip vrnjene vrednosti za preverjanje varovala: `success()` ali `fatal("razlog")`.

**OutputGuardrail** - vmesnik za preverjanje AI odgovorov preden se vrnejo uporabnikom.

**Varnostni filtri ponudnika** - vgrajeni filtri vsebine pri AI ponudnikih (npr. GitHub modeli), ki zaznajo kršitve na ravni API.

**Nežen zavrnitev** - model vljudno odkloni odgovor brez izmeta napake.

## Inženiring pozivov - [Modul 02](../02-prompt-engineering/README.md)

**Razmišljanje po korakih** - postopno razmišljanje za boljšo natančnost.

**Omejen izhod** - izvajanje specifične oblike ali strukture.

**Visoka zagnanost** - vzorec GPT-5.2 za temeljito razmišljanje.

**Nizka zagnanost** - vzorec GPT-5.2 za hitre odgovore.

**Večkratni pogovor** - ohranjanje konteksta skozi izmenjave.

**Pozivanje glede na vlogo** - nastavitev osebnosti modela preko sistemskih sporočil.

**Samo-refleksija** - model oceni in izboljša lasten izhod.

**Strukturirana analiza** - fiksiran okvir za ocenjevanje.

**Vzorec izvajanja naloge** - Načrtuj → Izvedi → Povzemi.

## RAG (generiranje z iskanjem) - [Modul 03](../03-rag/README.md)

**Procesna veriga za dokumente** - Nalaganje → razbijanje → vdelava → shranjevanje.

**Shranjevanje vdelav v pomnilniku** - neperzistentno shranjevanje za testiranje.

**RAG** - združuje iskanje z generiranjem za podlago odgovorov.

**Ocena podobnosti** - mera (0-1) semantične podobnosti.

**Vzorčni vir** - metapodatki o pridobljeni vsebini.

## Agentje in orodja - [Modul 04](../04-tools/README.md)

**@Tool anotacija** - označi Java metode kot orodja, ki jih lahko kliče AI.

**ReAct vzorec** - Razmišljaj → Ukrepaj → Opazuj → Ponovi.

**Upravljanje sej** - ločeni konteksti za različne uporabnike.

**Orodje** - funkcija, ki jo lahko kliče AI agent.

**Opis orodja** - dokumentacija namena in parametrov orodja.

## Agentski modul - [Modul 05](../05-mcp/README.md)

**@Agent anotacija** - označi vmesnike kot AI agente z deklarativno definicijo vedenja.

**Agent poslušalec** - vtičnica za spremljanje izvajanja agenta preko `beforeAgentInvocation()` in `afterAgentInvocation()`.

**Agentski obseg** - deljeni spomin, kjer agenti shranjujejo izhode z uporabo `outputKey` za nadaljnjo porabo.

**AgenticServices** - tovarna za ustvarjanje agentov z `agentBuilder()` in `supervisorBuilder()`.

**Pogojni potek dela** - usmerjanje na podlagi pogojev do različnih specializiranih agentov.

**Človek v zanki** - vzorec poteka dela, ki dodaja človeške kontrole za odobritev ali pregled vsebine.

**langchain4j-agentic** - Maven odvisnost za deklarativno gradnjo agentov (eksperimentalno).

**Zanka potek dela** - ponavljanje izvajanja agenta, dokler ni izpolnjen pogoj (npr. ocena kakovosti ≥ 0,8).

**outputKey** - parameter anotacije agenta, ki določa, kam se rezultati shranijo v Agentskem obsegu.

**Vzporedni potek dela** - hkratno izvajanje več agentov za neodvisne naloge.

**Strategija odziva** - kako nadzornik oblikuje končni odgovor: ZADNJI, POVZETEK ali OCENJEN.

**Zaporedni potek dela** - izvajanje agentov po vrsti, kjer izhod teče v naslednji korak.

**Vzorec nadzornega agenta** - napreden agentski vzorec, kjer nadzorni LLM dinamično odloča, katere pod-agente poklicati.

## Protokol konteksta modela (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven odvisnost za integracijo MCP v LangChain4j.

**MCP** - Protokol konteksta modela: standard za povezovanje AI aplikacij z zunanjimi orodji. Zgradi enkrat, uporabljaj povsod.

**MCP odjemalec** - aplikacija, ki se poveže z MCP strežniki za odkrivanje in uporabo orodij.

**MCP strežnik** - storitev, ki izpostavlja orodja preko MCP z jasnimi opisi in shemami parametrov.

**McpToolProvider** - komponenta LangChain4j, ki ovije MCP orodja za uporabo v AI storitvah in agentih.

**McpTransport** - vmesnik za komunikacijo MCP. Implementacije vključujejo Stdio in HTTP.

**Stdio Transport** - lokalni procesni transport preko stdin/stdout. Uporaben za dostop do datotečnega sistema ali orodij ukazne vrstice.

**StdioMcpTransport** - implementacija LangChain4j, ki sproži MCP strežnik kot podproces.

**Odkritje orodij** - odjemalec poizveduje strežnik za razpoložljiva orodja z opisi in shemami.

## Azure storitve - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - oblačno iskanje z vektorskimi zmožnostmi. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - uvaja Azure vire.

**Azure OpenAI** - Microsoftova podjetniška AI storitev.

**Bicep** - jezik za infrastrukturo kot kodo za Azure. [Vodnik o infrastrukturi](../01-introduction/infra/README.md)

**Ime izvedbe** - ime za namestitev modela v Azure.

**GPT-5.2** - najnovejši OpenAI model z nadzorom razmišljanja. [Modul 02](../02-prompt-engineering/README.md)

## Testiranje in razvoj - [Vodnik za testiranje](TESTING.md)

**Razvojni kontejner** - kontejnerizirano razvojno okolje. [Konfiguracija](../../../.devcontainer/devcontainer.json)

**GitHub modeli** - brezplačno igrišče za AI modele. [Modul 00](../00-quick-start/README.md)

**Testiranje v pomnilniku** - testiranje z uporabo shranjevanja v pomnilniku.

**Integracijsko testiranje** - testiranje z resnično infrastrukturo.

**Maven** - orodje za samodejno gradnjo Java.

**Mockito** - okvir za ustvarjanje nadomestkov v Javi.

**Spring Boot** - Java okvir za aplikacije. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da lahko avtomatski prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvorni jezik velja za avtoritativni vir. Za ključne informacije priporočamo strokovni človeški prevod. Za morebitna nesporazume ali napačne razlage, ki izhajajo iz uporabe tega prevoda, ne prevzemamo odgovornosti.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->