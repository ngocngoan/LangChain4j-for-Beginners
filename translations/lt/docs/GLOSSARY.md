# LangChain4j Žodynas

## Turinys

- [Pagrindinės sąvokos](../../../docs)
- [LangChain4j komponentai](../../../docs)
- [AI/ML sąvokos](../../../docs)
- [Apsauginiai mechanizmai](../../../docs)
- [Promptų kūrimas](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agentai ir įrankiai](../../../docs)
- [Agentinis modulis](../../../docs)
- [Modelio konteksto protokolas (MCP)](../../../docs)
- [Azure paslaugos](../../../docs)
- [Testavimas ir kūrimas](../../../docs)

Greita terminų ir sąvokų, naudojamų per visą kursą, apžvalga.

## Pagrindinės sąvokos

**AI agentas** – Sistema, kuri naudoja AI mąstymui ir autonomiškam veikimui. [Modulis 04](../04-tools/README.md)

**Grandinė** – Veiksmų seka, kurioje vieno veiksmo rezultatas perduodamas kitam žingsniui.

**Dalijimas į dalis** – Dokumentų suskaidymas į mažesnes dalis. Įprastai: 300-500 žetonų su persidengimu. [Modulis 03](../03-rag/README.md)

**Konteksto langas** – Maksimalus žetonų skaičius, kurį modelis gali apdoroti. GPT-5.2: 400K žetonų.

**Įterpimai (Embeddings)** – Skaitmeniniai vektoriai, atspindintys teksto prasmę. [Modulis 03](../03-rag/README.md)

**Funkcijų kvietimas** – Modelis generuoja struktūruotus prašymus kvietimams išoriniams funkcijoms. [Modulis 04](../04-tools/README.md)

**Halucinacija** – Kai modeliai generuoja neteisingą, bet įtikinamą informaciją.

**Promptas** – Teksto įvestis kalbos modeliui. [Modulis 02](../02-prompt-engineering/README.md)

**Semantinis paieška** – Paieška pagal reikšmę naudojant įterpimus, o ne raktažodžius. [Modulis 03](../03-rag/README.md)

**Būsena su atmintimi vs be atminties** – Be atminties: be prisiminimų. Su atmintimi: palaikoma pokalbio istorija. [Modulis 01](../01-introduction/README.md)

**Žetonai** – Pagrindinės teksto vienetės, kurias apdoroja modeliai. Įtakoja kainas ir apribojimus. [Modulis 01](../01-introduction/README.md)

**Įrankių grandinimas** – Sekantis įrankių naudojimas, kai išvestis paduodama kitam kvietimui. [Modulis 04](../04-tools/README.md)

## LangChain4j komponentai

**AiServices** – Kuria tipams saugias AI paslaugų sąsajas.

**OpenAiOfficialChatModel** – Vieningas klientas OpenAI ir Azure OpenAI modeliams.

**OpenAiOfficialEmbeddingModel** – Kuria įterpimus naudodamas OpenAI Official klientą (palaiko tiek OpenAI, tiek Azure OpenAI).

**ChatModel** – Pagrindinė kalbos modelių sąsaja.

**ChatMemory** – Saugo pokalbio istoriją.

**ContentRetriever** – Randa tinkamas dokumentų dalis RAG.

**DocumentSplitter** – Suskaido dokumentus į dalis.

**EmbeddingModel** – Paverčia tekstą į skaitmeninius vektorius.

**EmbeddingStore** – Saugo ir ieško įterpimų.

**MessageWindowChatMemory** – Palaiko skaidraus lango paskutinių žinučių istoriją.

**PromptTemplate** – Kuria pakartotinai naudojamus promptus su `{{variable}}` vietomis.

**TextSegment** – Teksto dalis su metaduomenimis. Naudojama RAG.

**ToolExecutionRequest** – Atstovauja įrankio vykdymo užklausą.

**UserMessage / AiMessage / SystemMessage** – Pokalbio žinučių tipai.

## AI/ML sąvokos

**Keletas pavyzdžių mokymasis (Few-Shot Learning)** – Pavyzdžių pateikimas promptuose. [Modulis 02](../02-prompt-engineering/README.md)

**Didelis kalbos modelis (LLM)** – AI modeliai, apmokyti dideliais tekstų kiekiais.

**Mąstymo pastangos (Reasoning Effort)** – GPT-5.2 parametras, kontroliuojantis mąstymo gylį. [Modulis 02](../02-prompt-engineering/README.md)

**Temperatūra** – Valdo atsakymų atsitiktinumą. Žema=deterministinis, aukšta=kūrybingas.

**Vektorinių duomenų bazė** – Specializuota duomenų bazė įterpimams. [Modulis 03](../03-rag/README.md)

**Nulinio pavyzdžio mokymasis (Zero-Shot Learning)** – Užduočių atlikimas be pavyzdžių. [Modulis 02](../02-prompt-engineering/README.md)

## Apsauginiai mechanizmai - [Modulis 00](../00-quick-start/README.md)

**Gynyba sluoksniais** – Daugiapakopė saugumo strategija, derinanti programos lygio apsaugos ribas su paslaugų teikėjo saugos filtrais.

**Griežtas blokas** – Paslaugos teikėjas grąžina HTTP 400 klaidą už rimtus turinio pažeidimus.

**InputGuardrail** – LangChain4j sąsaja, skirta vartotojo įvesties tikrinimui prieš pateikiant LLM. Taupo kaštus ir vėlinimą, anksti blokuodama kenksmingus promptus.

**InputGuardrailResult** – Tikrinimo rezultato tipas: `success()` arba `fatal("priežastis")`.

**OutputGuardrail** – Sąsaja AI atsakymų tikrinimui prieš pateikiant vartotojams.

**Paslaugų teikėjo saugos filtrai** – AI paslaugų teikėjų (pvz., GitHub modelių) integruoti turinio filtrai, kurie aptinka pažeidimus API lygyje.

**Minkštas atsisakymas** – Modelis mandagiai atsisako atsakyti be klaidos išmetimo.

## Promptų kūrimas - [Modulis 02](../02-prompt-engineering/README.md)

**Grandinės mąstymas** – Žingsnis po žingsnio logika geresniam tikslumui.

**Apribota išvestis** – Nustatytos konkrečios formato ar struktūros reikalavimai.

**Didelis entuziazmas** – GPT-5.2 šablonas nuodugniam mąstymui.

**Mažas entuziazmas** – GPT-5.2 šablonas greitiems atsakymams.

**Daugiakartinis pokalbis** – Konteksto palaikymas per mainus.

**Rolės pagrindu kuriamas promptas** – Modelio asmenybės nustatymas per sistemos žinutes.

**Savi-refleksija** – Modelis įvertina ir tobulina savo atsakymus.

**Struktūruota analizė** – Fiksuota vertinimo sistema.

**Užduoties vykdymo modelis** – Planavimas → Vykdymas → Apibendrinimas.

## RAG (Retrieval-Augmented Generation) - [Modulis 03](../03-rag/README.md)

**Dokumentų apdorojimo grandinė** – Įkėlimas → dalijimas → įterpimas → saugojimas.

**Trumpalaikis įterpimų saugykla** – Laikina atmintinė testavimui.

**RAG** – Derina paiešką su generavimu atsakymams pagrįsti.

**Panašumo balas** – Semantinio panašumo matas (0-1).

**Šaltinio nuoroda** – Metaduomenys apie rastą turinį.

## Agentai ir įrankiai - [Modulis 04](../04-tools/README.md)

**@Tool anotacija** – Pažymi Java metodus kaip AI iškviečiamus įrankius.

**ReAct modelis** – Mąstyk → Veik → Stebėk → Kartok.

**Sesijų valdymas** – Atskirti kontekstai skirtingiems vartotojams.

**Įrankis** – Funkcija, kurią gali iškviesti AI agentas.

**Įrankio aprašymas** – Dokumentacija apie įrankio paskirtį ir parametrus.

## Agentinis modulis - [Modulis 05](../05-mcp/README.md)

**@Agent anotacija** – Pažymi sąsajas kaip AI agentus su deklaratyvia elgesio aprašymu.

**Agentų klausytojas** – Priemonė stebėti agentų vykdymą per `beforeAgentInvocation()` ir `afterAgentInvocation()`.

**Agentinė sritis** – Bendroji atmintis, kurioje agentai saugo rezultatus naudodami `outputKey`, kad kiti agentai galėtų juos naudoti.

**AgenticServices** – Fabrikas agentų kūrimui su `agentBuilder()` ir `supervisorBuilder()`.

**Sąlyginis darbo srautas** – Maršruto nukreipimas į skirtingus specializuotus agentus pagal sąlygas.

**Žmogus procese (Human-in-the-Loop)** – Darbo srauto modelis su žmogaus patvirtinimu ar turinio peržiūra.

**langchain4j-agentic** – Maven priklausomybė deklaratyviam agentų kūrimui (eksperimentinė).

**Ciklinis darbo srautas** – Kartojamas agento vykdymas, kol įvykdoma sąlyga (pvz., kokybės balas ≥ 0.8).

**outputKey** – Agentų anotacijos parametras, nurodantis, kur Agentinėje srityje saugomi rezultatai.

**Lygiagretus darbo srautas** – Kelis agentus vykdyti vienu metu nepriklausomoms užduotims.

**Atsako strategija** – Kaip vadovas formuluoja galutinį atsakymą: PASKUTINIS, SANTRAUKA arba ĮVERTINTAS.

**Sekos darbo srautas** – Agentų vykdymas iš eilės, kai rezultatas perduodamas kitam žingsniui.

**Vadovo agento modelis** – Sudėtingas agentinis modelis, kuriame vadovas LLM dinamiškai nusprendžia, kuriuos subagentus kviesti.

## Modelio konteksto protokolas (MCP) - [Modulis 05](../05-mcp/README.md)

**langchain4j-mcp** – Maven priklausomybė MCP integracijai LangChain4j.

**MCP** – Modelio konteksto protokolas: standartas AI programoms jungtis prie išorinių įrankių. Sukurk vieną kartą, naudok visur.

**MCP klientas** – Programa, jungiantis prie MCP serverių, kad rastų ir naudotų įrankius.

**MCP serveris** – Paslauga, teikianti įrankius per MCP su aiškiais aprašymais ir parametrų schemomis.

**McpToolProvider** – LangChain4j komponentas, apgaubiantis MCP įrankius AI paslaugoms ir agentams.

**McpTransport** – Sąsaja MCP komunikacijai. Realizacijos apima Stdio ir HTTP.

**Stdio transportas** – Vietinis procesas per stdin/stdout. Naudinga failų sistemų prieigai ar komandų eilutės įrankiams.

**StdioMcpTransport** – LangChain4j realizacija, kurios metu MCP serveris paleidžiamas kaip subprocess.

**Įrankių atradimas** – Klientas užduoda serveriui klausimus apie turimus įrankius su aprašymais ir schemomis.

## Azure paslaugos - [Modulis 01](../01-introduction/README.md)

**Azure AI Search** – Debesų paieška su vektorinėmis galimybėmis. [Modulis 03](../03-rag/README.md)

**Azure Developer CLI (azd)** – Azure išteklių diegimas.

**Azure OpenAI** – Microsoft įmonių lygio AI paslauga.

**Bicep** – Azure infrastruktūros kaip kodo kalba. [Infrastruktūros vadovas](../01-introduction/infra/README.md)

**Diegimo pavadinimas** – Modelio diegimo pavadinimas Azure.

**GPT-5.2** – Naujausias OpenAI modelis su mąstymo kontrole. [Modulis 02](../02-prompt-engineering/README.md)

## Testavimas ir kūrimas - [Testavimo vadovas](TESTING.md)

**Dev konteineris** – Kūrimui skirtas konteinerizuotas aplinkas. [Konfigūracija](../../../.devcontainer/devcontainer.json)

**GitHub modeliai** – Nemokama AI modelių poligonas. [Modulis 00](../00-quick-start/README.md)

**Testavimas atmintyje** – Testavimas naudojant trumpalaikę saugyklą.

**Integracinis testavimas** – Testavimas su realia infrastruktūra.

**Maven** – Java programų kūrimo įrankis.

**Mockito** – Java imitavimo (mock) karkasas.

**Spring Boot** – Java programų karkasas. [Modulis 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Pirminis dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojame naudotis profesionalaus žmogiško vertimo paslaugomis. Mes neatsakome už bet kokius nesusipratimus ar klaidingus supratimus, kilusius dėl šio vertimo panaudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->