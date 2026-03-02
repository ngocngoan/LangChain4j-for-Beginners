# LangChain4j Ordliste

## Innholdsfortegnelse

- [Kjernebegreper](../../../docs)
- [LangChain4j Komponenter](../../../docs)
- [AI/ML Begreper](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter og Verktøy](../../../docs)
- [Agentmodul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Tjenester](../../../docs)
- [Testing og Utvikling](../../../docs)

Rask referanse for termer og konsepter brukt gjennom hele kurset.

## Kjernebegreper

**AI Agent** - System som bruker AI til å resonnere og handle autonomt. [Modul 04](../04-tools/README.md)

**Kjede** - Sekvens av operasjoner hvor output mates inn i neste steg.

**Chunking** - Dele dokumenter opp i mindre biter. Typisk: 300-500 tokens med overlapp. [Modul 03](../03-rag/README.md)

**Kontekstvindu** - Maksimalt antall tokens en modell kan prosessere. GPT-5.2: 400K tokens (opptil 272K input, 128K output).

**Inbaking (Embeddings)** - Numeriske vektorer som representerer tekstens mening. [Modul 03](../03-rag/README.md)

**Funksjonskalling** - Modell genererer strukturerte forespørsler for å kalle eksterne funksjoner. [Modul 04](../04-tools/README.md)

**Hallusinasjon** - Når modeller genererer feilaktig men plausibel informasjon.

**Prompt** - Tekstinput til en språkmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantisk Søking** - Søk basert på mening ved hjelp av inbaking, ikke nøkkelord. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ingen minne. Stateful: opprettholder samtalehistorikk. [Modul 01](../01-introduction/README.md)

**Tokens** - Grunnenheter tekstmodeller prosesserer. Påvirker kostnader og begrensninger. [Modul 01](../01-introduction/README.md)

**Verktøykjedning** - Sekvensiell verktøyutførelse hvor output informerer neste kall. [Modul 04](../04-tools/README.md)

## LangChain4j Komponenter

**AiServices** - Lager typesikre AI-tjenestegrensesnitt.

**OpenAiOfficialChatModel** - Forent klient for OpenAI og Azure OpenAI modeller.

**OpenAiOfficialEmbeddingModel** - Lager inbakinger med OpenAI Official-klient (støtter både OpenAI og Azure OpenAI).

**ChatModel** - Kjernegrensesnitt for språkmodeller.

**ChatMemory** - Opprettholder samtalehistorikk.

**ContentRetriever** - Finner relevante dokumentbiter for RAG.

**DocumentSplitter** - Deler dokumenter opp i biter.

**EmbeddingModel** - Konverterer tekst til numeriske vektorer.

**EmbeddingStore** - Lagrer og henter inbakinger.

**MessageWindowChatMemory** - Opprettholder glidende vindu av nylige meldinger.

**PromptTemplate** - Lager gjenbrukbare prompts med `{{variable}}`-plasser.

**TextSegment** - Tekstbit med metadata. Brukes i RAG.

**ToolExecutionRequest** - Representerer verktøyutførelsesforespørsel.

**UserMessage / AiMessage / SystemMessage** - Samtalemeldingstyper.

## AI/ML Begreper

**Few-Shot Learning** - Å gi eksempler i prompts. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modeller trent på store mengder tekstdata.

**Resonnementinnsats** - GPT-5.2 parameter som styrer hvor dypt modellen tenker. [Modul 02](../02-prompt-engineering/README.md)

**Temperatur** - Styrer output-randomness. Lav=deteriministisk, høy=kreativ.

**Vektor Database** - Spesialisert database for inbakinger. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Utføre oppgaver uten eksempler. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Flersjikts sikkerhetstilnærming som kombinerer applikasjonsnivå guardrails med leverandørsikkerhetsfiltre.

**Hard Block** - Leverandør kaster HTTP 400-feil ved alvorlige innholdsbrudd.

**InputGuardrail** - LangChain4j-grensesnitt for validering av brukerinput før den når LLM. Spar kostnad og ventetid ved å blokkere skadelige prompts tidlig.

**InputGuardrailResult** - Returtype for guardrail-validering: `success()` eller `fatal("reason")`.

**OutputGuardrail** - Grensesnitt for validering av AI-svar før de returneres til brukere.

**Leverandørsikkerhetsfiltre** - Innebygde innholdsfiltre fra AI-leverandører (f.eks. GitHub Models) som fanger brudd på API-nivå.

**Soft Refusal** - Modell høflig avslår å svare uten å kaste feil.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Stegvis resonnement for bedre nøyaktighet.

**Constraining Output** - Håndheve spesifikt format eller struktur.

**Høy Iver** - GPT-5.2 mønster for grundig resonnement.

**Lav Iver** - GPT-5.2 mønster for raske svar.

**Multi-Turn Conversation** - Opprettholde kontekst over flere utvekslinger.

**Rollebasert Prompting** - Sette modellpersonlighet via systemmeldinger.

**Selvrefleksjon** - Modell evaluerer og forbedrer egen output.

**Strukturert Analyse** - Fast evalueringsrammeverk.

**Oppgaveutførelsesmønster** - Planlegge → Utføre → Oppsummere.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Dokumentprosesspipeline** - Last → del opp → lag inbaking → lagre.

**In-Memory Embedding Store** - Ikke-persistent lagring for testing.

**RAG** - Kombinerer innhenting med generering for å forankre svar.

**Likhetspoeng** - Mål (0-1) på semantisk likhet.

**Kildehenvisning** - Metadata om hentet innhold.

## Agenter og Verktøy - [Modul 04](../04-tools/README.md)

**@Tool Annotasjon** - Marker Java-metoder som AI-kallbare verktøy.

**ReAct Mønster** - Resonner → Handle → Observer → Gjenta.

**Sesjonshåndtering** - Separate kontekster for ulike brukere.

**Verktøy** - Funksjon en AI-agent kan kalle.

**Verktøybeskrivelse** - Dokumentasjon av verktøyets formål og parametre.

## Agentmodul - [Modul 05](../05-mcp/README.md)

**@Agent Annotasjon** - Marker grensesnitt som AI-agenter med deklarativ adferdsdefinisjon.

**Agent Listener** - Hook for overvåking av agentutførelse via `beforeAgentInvocation()` og `afterAgentInvocation()`.

**Agentic Scope** - Delt minne der agenter lagrer output ved bruk av `outputKey` for at downstream agenter skal konsumere.

**AgenticServices** - Fabrikk for å lage agenter ved bruk av `agentBuilder()` og `supervisorBuilder()`.

**Betinget Arbeidsflyt** - Rute basert på betingelser til ulike spesialistagenter.

**Human-in-the-Loop** - Arbeidsflytmønster som legger til menneskelige kontrollpunkter for godkjenning eller innholdsgranskning.

**langchain4j-agentic** - Maven-avhengighet for deklarativ agentbygging (eksperimentell).

**Løkkearbeidsflyt** - Iterer agentutførelse til en betingelse er oppfylt (f.eks. kvalitets-score ≥ 0.8).

**outputKey** - Agentannotasjonsparameter som spesifiserer hvor resultater lagres i Agentic Scope.

**Parallell Arbeidsflyt** - Kjør flere agenter samtidig for uavhengige oppgaver.

**Responsstrategi** - Hvordan supervisor formulerer endelig svar: SISTE, OPPSUMMERING, eller SCORET.

**Sekvensiell Arbeidsflyt** - Utfør agenter i rekkefølge hvor output flyter til neste steg.

**Supervisor Agent Mønster** - Avansert agentisk mønster hvor en supervisor LLM dynamisk bestemmer hvilke sub-agenter som skal kalles.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven-avhengighet for MCP-integrasjon i LangChain4j.

**MCP** - Model Context Protocol: standard for å koble AI-applikasjoner til eksterne verktøy. Bygg én gang, bruk overalt.

**MCP Klient** - Applikasjon som kobler til MCP-servere for å oppdage og bruke verktøy.

**MCP Server** - Tjeneste som eksponerer verktøy via MCP med klare beskrivelser og parameterskjemaer.

**McpToolProvider** - LangChain4j-komponent som pakker MCP-verktøy for bruk i AI-tjenester og agenter.

**McpTransport** - Grensesnitt for MCP-kommunikasjon. Implementasjoner inkluderer Stdio og HTTP.

**Stdio Transport** - Lokal prosess-transport via stdin/stdout. Nyttig for filsystemtilgang eller kommandolinjeverktøy.

**StdioMcpTransport** - LangChain4j-implementasjon som starter MCP-server som underprosess.

**Verktøyoppdagelse** - Klient spørrer server om tilgjengelige verktøy med beskrivelser og skjemaer.

## Azure Tjenester - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Skytjeneste for søk med vektorfunktioner. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Distribuerer Azure-ressurser.

**Azure OpenAI** - Microsofts bedrifts-AI-tjeneste.

**Bicep** - Azure infrastruktur-som-kode språk. [Infrastrukturguide](../01-introduction/infra/README.md)

**Deploy-navn** - Navn for modellutplassering i Azure.

**GPT-5.2** - Nyeste OpenAI-modell med resonnementskontroll. [Modul 02](../02-prompt-engineering/README.md)

## Testing og Utvikling - [Testingguide](TESTING.md)

**Dev Container** - Containerisert utviklingsmiljø. [Konfigurasjon](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI-modell lekeplass. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Test med minnebasert lagring.

**Integrasjonstesting** - Testing med ekte infrastruktur.

**Maven** - Java byggverktøy.

**Mockito** - Java mocking-rammeverk.

**Spring Boot** - Java applikasjonsrammeverk. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->