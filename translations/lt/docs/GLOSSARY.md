# LangChain4j žodynas

## Turinys

- [Pagrindinės sąvokos](../../../docs)
- [LangChain4j komponentai](../../../docs)
- [AI/ML sąvokos](../../../docs)
- [Apsauginiai barjerai](../../../docs)
- [Užklausų kūrimas](../../../docs)
- [RAG (paieškomoji papildyta generacija)](../../../docs)
- [Agentai ir įrankiai](../../../docs)
- [Agentų modulis](../../../docs)
- [Modelio konteksto protokolas (MCP)](../../../docs)
- [Azure paslaugos](../../../docs)
- [Testavimas ir vystymas](../../../docs)

Greita terminų ir sąvokų, naudojamų visame kurse, nuoroda.

## Pagrindinės sąvokos

**AI agentas** – Sistema, kuri naudoja AI savarankiškiems sprendimams ir veiksmams atlikti. [Modulis 04](../04-tools/README.md)

**Grandinė** – Operacijų seka, kurioje kiekvieno žingsnio išvestis perduodama kitam.

**Dalijimas** – Dokumentų skaidymas į mažesnes dalis. Įprasta: 300–500 žodžių su persidengimu. [Modulis 03](../03-rag/README.md)

**Konteksto langas** – Maksimalus modelio apdorojamų žodžių skaičius. GPT-5.2: 400 tūkst. žodžių (iki 272 tūkst. įvesties, 128 tūkst. išvesties).

**Įterpiniai (Embeddingai)** – Skaitmeniniai vektoriai, atspindintys teksto reikšmę. [Modulis 03](../03-rag/README.md)

**Funkcijų kvietimas** – Modelis generuoja struktūruotas užklausas išoriniams funkcijų kvietimams atlikti. [Modulis 04](../04-tools/README.md)

**Halucinacija** – Kai modeliai generuoja klaidingą, bet įtikinamą informaciją.

**Užklausa (Promptas)** – Tekstinis įvesties duomenų į kalbos modelį formatas. [Modulis 02](../02-prompt-engineering/README.md)

**Semantinė paieška** – Paieška pagal reikšmę naudojant įterpinius, ne raktinius žodžius. [Modulis 03](../03-rag/README.md)

**Būsena su atsiminimu ir be jo** – Be atsiminimo: be atminties. Su atsiminimu: išlaiko pokalbio istoriją. [Modulis 01](../01-introduction/README.md)

**Žodžiai (Tokens)** – Pagrindiniai teksto vienetai, kuriuos apdoroja modeliai. Įtakoja sąnaudas ir ribas. [Modulis 01](../01-introduction/README.md)

**Įrankių grandinimas** – Įrankių seka, kurioje išvestis naudojama kitam kvietimui. [Modulis 04](../04-tools/README.md)

## LangChain4j komponentai

**AiServices** – Kuria tipo saugius AI paslaugų sąsajas.

**OpenAiOfficialChatModel** – Vieningas klientas OpenAI ir Azure OpenAI modeliams.

**OpenAiOfficialEmbeddingModel** – Kuria įterpinius naudojant OpenAI oficialų klientą (palaiko tiek OpenAI, tiek Azure OpenAI).

**ChatModel** – Pagrindinė kalbos modelių sąsaja.

**ChatMemory** – Išlaiko pokalbio istoriją.

**ContentRetriever** – Suranda aktualias dokumentų dalis RAG procesui.

**DocumentSplitter** – Skaido dokumentus į dalis.

**EmbeddingModel** – Paverčia tekstą skaitmeniniais vektoriais.

**EmbeddingStore** – Saugo ir paima įterpinius.

**MessageWindowChatMemory** – Išlaiko naujausių pranešimų slenkamąjį langą.

**PromptTemplate** – Kuria pakartotinai naudojamas užklausas su `{{variable}}` vietos žymomis.

**TextSegment** – Teksto dalis su metaduomenimis. Naudojama RAG.

**ToolExecutionRequest** – Atspindi įrankio vykdymo užklausą.

**UserMessage / AiMessage / SystemMessage** – Pokalbio pranešimų tipai.

## AI/ML sąvokos

**Few-Shot mokymasis** – Pateikiami pavyzdžiai užklausose. [Modulis 02](../02-prompt-engineering/README.md)

**Didelis kalbos modelis (LLM)** – AI modeliai, apmokyti pagal milžiniškus tekstų kiekius.

**Mąstymo pastangos** – GPT-5.2 parametras, valdantis mąstymo gylį. [Modulis 02](../02-prompt-engineering/README.md)

**Temperatūra** – Valdo išvesties atsitiktinumą. Žema – deterministinė, aukšta – kūrybiška.

**Vektorinė duomenų bazė** – Specializuota duomenų bazė įterpiniams. [Modulis 03](../03-rag/README.md)

**Zero-Shot mokymasis** – Užuot naudojant pavyzdžius, atliekant užduotis be jų. [Modulis 02](../02-prompt-engineering/README.md)

## Apsauginiai barjerai - [Modulis 00](../00-quick-start/README.md)

**Gylio gynyba** – Daugiasluoksnė saugumo sistema, jungiant programos lygmens barjerus su paslaugų filtrais.

**Stiprus blokavimas** – Paslaugos teikėjas meta HTTP 400 klaidą už griežtus turinio pažeidimus.

**InputGuardrail** – LangChain4j sąsaja vartotojo įvesties patikrinimui prieš patekiant į LLM. Labai taupo išlaidas ir laiką, anksti blokuodama kenksmingas užklausas.

**InputGuardrailResult** – Vartotojo įvesties saugumo grįžtamoji reikšmė: `success()` arba `fatal("priežastis")`.

**OutputGuardrail** – Sąsaja AI atsakymų peržiūrai prieš pateikiant vartotojui.

**Paslaugų teikėjo saugumo filtrai** – AI paslaugų įmontuoti turinio filtrai (pvz., GitHub modeliai), susekiantys pažeidimus API lygyje.

**Minkštas atsisakymas** – Modelis mandagiai atsisako atsakyti, nekeldamas klaidos.

## Užklausų kūrimas - [Modulis 02](../02-prompt-engineering/README.md)

**Grandininis mąstymas** – Žingsnis po žingsnio argumentavimas geresniam tikslumui.

**Ribotas išvesties formatas** – Reikalavimo laikytis konkretaus formato ar struktūros.

**Didelis gatavumas** – GPT-5.2 šablonas išsamiai analizei.

**Mažas gatavumas** – GPT-5.2 greitiems atsakymams.

**Daugiapakopis pokalbis** – Konteksto palaikymas per kelias žinutes.

**Vaidmens užklausų kūrimas** – Modelio personažo nustatymas per sistemos pranešimus.

**Savioverta** – Modelis vertina ir tobulina savo išvestį.

**Struktūruota analizė** – Fiksuota vertinimo sistema.

**Užduočių vykdymo modelis** – Planavimas → Vykdymas → Apibendrinimas.

## RAG (paieškomoji papildyta generacija) - [Modulis 03](../03-rag/README.md)

**Dokumentų apdorojimo kanalas** – Įkėlimas → skaidymas → įterpimas → saugojimas.

**Atminties įterpinių saugykla** – Neišlaikoma atminties saugykla testavimui.

**RAG** – Derina paiešką su generavimu, siekiant pagrįsti atsakymus.

**Panašumo įvertinimas** – Matavimo skalė (0-1) semantiniam panašumui.

**Šaltinio nuoroda** – Paieškos turinio metaduomenys.

## Agentai ir įrankiai - [Modulis 04](../04-tools/README.md)

**@Tool anotacija** – Žymi Java metodus kaip AI kviečiamus įrankius.

**ReAct modelis** – Mąstyti → veikti → stebėti → kartoti.

**Seansų valdymas** – Atskiri kontekstai skirtingiems vartotojams.

**Įrankis** – Funkcija, kurią gali kviesti AI agentas.

**Įrankio aprašymas** – Dokumentacija apie paskirtį ir parametrus.

## Agentų modulis - [Modulis 05](../05-mcp/README.md)

**@Agent anotacija** – Žymi sąsajas kaip AI agentus su deklaratyviu elgesio aprašymu.

**Agentų klausytojas** – Priemonė stebėti agento vykdymą per `beforeAgentInvocation()` ir `afterAgentInvocation()`.

**Agentinis kontekstas** – Bendroji atmintis, kur agentai saugo rezultatus naudodami `outputKey` tolimesniems agentams.

**AgenticServices** – Fabrikas agentams kurti naudojant `agentBuilder()` ir `supervisorBuilder()`.

**Sąlyginis darbo eigos valdymas** – Nukreipia pagal sąlygas skirtingiems specialistams.

**Žmogaus dalyvavimas** – Darbo modelis su žmonių patvirtinimu ar turinio peržiūra.

**langchain4j-agentic** – Maven priklausomybė deklaratyviems agentams kurti (eksperimentinė).

**Ciklinis darbo eigos valdymas** – Kartojamas agentų vykdymas, kol įvykdoma sąlyga (pvz., kokybės įvertis ≥ 0.8).

**outputKey** – Agentų anotacijos parametras, nurodantis, kur rezultatai saugomi agentiniame kontekste.

**Paralelinis darbo eigos valdymas** – Kelis agentus vykdyti lygiagrečiai nepriklausomoms užduotims.

**Atsakymo strategija** – Kaip prižiūrėtojas formuluoja galutinį atsakymą: PASKUTINIS, SANTRAUKA ar ĮVERTINTAS.

**Sekveninis darbo eigos valdymas** – Vykdyti agentus iš eilės, išvestis perduodama kitam etapui.

**Prižiūrėtojo agento modelis** – Pažangus agentinis modelis, kuriame prižiūrėtojas LLM dinamiškai nusprendžia, kuriuos subagentus kviesti.

## Modelio konteksto protokolas (MCP) - [Modulis 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven priklausomybė MCP integracijai LangChain4j.

**MCP** – Modelio konteksto protokolas: standartas AI programoms susijungti su išoriniais įrankiais. Sukurk kartą, naudok visur.

**MCP klientas** – Programa, jungiasi prie MCP serverių, ieško ir naudoja įrankius.

**MCP serveris** – Paslauga, viešoji įrankių per MCP su aiškiais aprašymais ir parametrų schemomis.

**McpToolProvider** – LangChain4j komponentas, apgaubiantis MCP įrankius AI paslaugoms ir agentams.

**McpTransport** – Sąsaja MCP komunikacijai. Įgyvendinimai: Stdio ir HTTP.

**Stdio transportas** – Vietinis procesas per stdin/stdout. Naudinga failų sistema ar komandų eilutės įrankiams.

**StdioMcpTransport** – LangChain4j įgyvendinimas, paleidžiantis MCP serverį kaip antrinį procesą.

**Įrankių paieška** – Klientas užklausia serverį apie galimus įrankius su aprašymais ir schemomis.

## Azure paslaugos - [Modulis 01](../01-introduction/README.md)

**Azure AI Search** – Debesų paieška su vektorinėmis galimybėmis. [Modulis 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Azure išteklių diegimas.

**Azure OpenAI** – Microsoft įmonių lygmens AI paslauga.

**Bicep** – Azure infrastruktūros kaip kodo kalba. [Infrastruktūros gidas](../01-introduction/infra/README.md)

**Diegimo pavadinimas** – Modelio diegimo pavadinimas Azure.

**GPT-5.2** – Naujausias OpenAI modelis su mąstymo kontrolės funkcija. [Modulis 02](../02-prompt-engineering/README.md)

## Testavimas ir vystymas - [Testavimo gidas](TESTING.md)

**Dev Container** – Kapsuliuota vystymo aplinka. [Konfigūracija](../../../.devcontainer/devcontainer.json)

**GitHub modeliai** – Nemokama AI modelių žaidimų aikštelė. [Modulis 00](../00-quick-start/README.md)

**Atminties testavimas** – Testavimas naudojant atminties saugyklą.

**Integracinis testavimas** – Testavimas su realia infrastruktūra.

**Maven** – Java kūrimo automatizavimo įrankis.

**Mockito** – Java imitavimo (mock) biblioteka.

**Spring Boot** – Java programų karkasas. [Modulis 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas natūralia kalba turėtų būti laikomas pagrindiniu šaltiniu. Svarbiai informacijai rekomenduojamas profesionalus žmogiškasis vertimas. Mes neatsakome už bet kokius nesusipratimus ar neteisingus aiškinimus, kilusius naudojant šį vertimą.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->