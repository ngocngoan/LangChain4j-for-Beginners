# LangChain4j Ordlista

## Innholdsfortegnelse

- [Kjernkonsepter](../../../docs)
- [LangChain4j-komponenter](../../../docs)
- [AI/ML-konsepter](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agenter og Verktøy](../../../docs)
- [Agentisk Modul](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure-tjenester](../../../docs)
- [Testing og Utvikling](../../../docs)

Rask referanse for termer og konsepter brukt gjennom hele kurset.

## Kjernkonsepter

**AI Agent** - System som bruker AI for å resonnere og handle autonomt. [Modul 04](../04-tools/README.md)

**Chain** - Sekvens av operasjoner hvor output mates inn i neste steg.

**Chunking** - Dele dokumenter i mindre biter. Typisk: 300–500 tokens med overlapp. [Modul 03](../03-rag/README.md)

**Context Window** - Maksimalt antall tokens en modell kan behandle. GPT-5.2: 400K tokens.

**Embeddings** - Numeriske vektorer som representerer tekstens betydning. [Modul 03](../03-rag/README.md)

**Function Calling** - Modell genererer strukturerte forespørsler for å kalle eksterne funksjoner. [Modul 04](../04-tools/README.md)

**Hallucination** - Når modeller genererer feilaktig, men plausibel informasjon.

**Prompt** - Tekstinput til en språkmodell. [Modul 02](../02-prompt-engineering/README.md)

**Semantic Search** - Søk basert på mening ved bruk av embeddings, ikke søkeord. [Modul 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: ingen hukommelse. Stateful: opprettholder samtalehistorikk. [Modul 01](../01-introduction/README.md)

**Tokens** - Grunnleggende tekstenheter som modeller behandler. Påvirker kostnader og begrensninger. [Modul 01](../01-introduction/README.md)

**Tool Chaining** - Sekvensiell verktøyutførelse der output styrer neste kall. [Modul 04](../04-tools/README.md)

## LangChain4j-komponenter

**AiServices** - Lager typesikre AI tjenestegrensesnitt.

**OpenAiOfficialChatModel** - Enhetlig klient for OpenAI og Azure OpenAI-modeller.

**OpenAiOfficialEmbeddingModel** - Lager embeddings ved bruk av OpenAI Official-klienten (støtter både OpenAI og Azure OpenAI).

**ChatModel** - Kjernegrensesnitt for språkmodeller.

**ChatMemory** - Opprettholder samtalehistorikk.

**ContentRetriever** - Finner relevante dokumentbiter for RAG.

**DocumentSplitter** - Deler dokumenter i biter.

**EmbeddingModel** - Konverterer tekst til numeriske vektorer.

**EmbeddingStore** - Lagrer og henter embeddings.

**MessageWindowChatMemory** - Opprettholder et glidende vindu av nylige meldinger.

**PromptTemplate** - Lager gjenbrukbare prompts med `{{variable}}`-plassholdere.

**TextSegment** - Tekstbit med metadata. Brukes i RAG.

**ToolExecutionRequest** - Representerer verktøyutførelsesforespørsel.

**UserMessage / AiMessage / SystemMessage** - Samtaletypemeldinger.

## AI/ML-konsepter

**Few-Shot Learning** - Gir eksempler i prompts. [Modul 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI-modeller trent på store tekstmengder.

**Reasoning Effort** - GPT-5.2-parameter som styrer tenkedybde. [Modul 02](../02-prompt-engineering/README.md)

**Temperature** - Styrer utgangens tilfeldighet. Lav=deterministisk, høy=kreativ.

**Vector Database** - Spesialisert database for embeddings. [Modul 03](../03-rag/README.md)

**Zero-Shot Learning** - Utføre oppgaver uten eksempler. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Defense in Depth** - Multi-lags sikkerhetstilnærming som kombinerer applikasjonsnivå-guardrails med providers sikkerhetsfiltre.

**Hard Block** - Provider kaster HTTP 400-feil for alvorlige innholdsbrudd.

**InputGuardrail** - LangChain4j-grensesnitt for validering av brukerinput før det når LLM. Spar kostnad og ventetid ved å blokkere skadelige prompts tidlig.

**InputGuardrailResult** - Returtype for guardrail-validering: `success()` eller `fatal("reason")`.

**OutputGuardrail** - Grensesnitt for validering av AI-responser før de returneres til brukere.

**Provider Safety Filters** - Innebygde innholdsfiltre fra AI-leverandører (f.eks. GitHub Models) som fanger brudd på API-nivå.

**Soft Refusal** - Modell nekter høflig å svare uten å kaste feil.

## Prompt Engineering - [Modul 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Trinnvis resonnering for bedre nøyaktighet.

**Constrained Output** - Håndheve spesifikk format eller struktur.

**High Eagerness** - GPT-5.2-mønster for grundig resonnering.

**Low Eagerness** - GPT-5.2-mønster for raske svar.

**Multi-Turn Conversation** - Opprettholde kontekst over flere utvekslinger.

**Role-Based Prompting** - Sette modellpersona via systemmeldinger.

**Self-Reflection** - Modell evaluerer og forbedrer sitt output.

**Structured Analysis** - Fast evalueringsrammeverk.

**Task Execution Pattern** - Planlegg → Utfør → Oppsummer.

## RAG (Retrieval-Augmented Generation) - [Modul 03](../03-rag/README.md)

**Document Processing Pipeline** - Last inn → del → embed → lagre.

**In-Memory Embedding Store** - Ikke-permanent lagring for testing.

**RAG** - Kombinerer søk med generering for å forankre svar.

**Similarity Score** - Måling (0-1) av semantisk likhet.

**Source Reference** - Metadata om hentet innhold.

## Agenter og Verktøy - [Modul 04](../04-tools/README.md)

**@Tool Annotation** - Merker Java-metoder som AI-kallbare verktøy.

**ReAct Pattern** - Resonner → Handle → Observer → Gjenta.

**Session Management** - Separate kontekster for forskjellige brukere.

**Tool** - Funksjon en AI-agent kan kalle.

**Tool Description** - Dokumentasjon av verktøyets formål og parametere.

## Agentisk Modul - [Modul 05](../05-mcp/README.md)

**@Agent Annotation** - Merker grensesnitt som AI-agenter med deklarativ atferdsdefinisjon.

**Agent Listener** - Hook for overvåking av agentutførelse via `beforeAgentInvocation()` og `afterAgentInvocation()`.

**Agentic Scope** - Delt minne hvor agenter lagrer output med `outputKey` for etterfølgende agenter å konsumere.

**AgenticServices** - Fabrikk for å lage agenter ved bruk av `agentBuilder()` og `supervisorBuilder()`.

**Conditional Workflow** - Rute basert på betingelser til forskjellige spesialist-agenter.

**Human-in-the-Loop** - Arbeidsflytmønster som legger til menneskelige kontrollpunkter for godkjenning eller innholdsgransking.

**langchain4j-agentic** - Maven-avhengighet for deklarativ agentbygging (eksperimentell).

**Loop Workflow** - Iterer agentutførelse til en betingelse oppfylles (f.eks. kvalitetsvurdering ≥ 0.8).

**outputKey** - Agentannotasjon-parameter som spesifiserer hvor resultater lagres i Agentic Scope.

**Parallel Workflow** - Kjør flere agenter samtidig for uavhengige oppgaver.

**Response Strategy** - Hvordan supervisor formulerer sluttresultat: LAST, SUMMARY, eller SCORED.

**Sequential Workflow** - Utfør agenter i rekkefølge hvor output flyter til neste steg.

**Supervisor Agent Pattern** - Avansert agentisk mønster hvor en supervisor LLM dynamisk avgjør hvilke sub-agenter som skal kalles.

## Model Context Protocol (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven-avhengighet for MCP-integrasjon i LangChain4j.

**MCP** - Model Context Protocol: standard for å koble AI-apper til eksterne verktøy. Bygg én gang, bruk overalt.

**MCP Client** - Applikasjon som kobler til MCP-servere for å oppdage og bruke verktøy.

**MCP Server** - Tjeneste som eksponerer verktøy via MCP med klare beskrivelser og parameterskjemaer.

**McpToolProvider** - LangChain4j-komponent som pakker MCP-verktøy for bruk i AI-tjenester og agenter.

**McpTransport** - Grensesnitt for MCP-kommunikasjon. Implementasjoner inkluderer Stdio og HTTP.

**Stdio Transport** - Lokal prosess-transport via stdin/stdout. Nyttig for filsystemtilgang eller kommandolinjeverktøy.

**StdioMcpTransport** - LangChain4j-implementasjon som starter MCP-server som underprosess.

**Tool Discovery** - Klient spør server om tilgjengelige verktøy med beskrivelser og skjemaer.

## Azure-tjenester - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Skybasert søk med vektorfunksjonalitet. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Distribuerer Azure-ressurser.

**Azure OpenAI** - Microsofts enterprise AI-tjeneste.

**Bicep** - Azure infrastruktur-som-kode språk. [Infrastrukturguide](../01-introduction/infra/README.md)

**Deployment Name** - Navn for modellutrulling i Azure.

**GPT-5.2** - Nyeste OpenAI-modell med resonneringskontroll. [Modul 02](../02-prompt-engineering/README.md)

## Testing og Utvikling - [Testguide](TESTING.md)

**Dev Container** - Containerisert utviklingsmiljø. [Konfigurasjon](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Gratis AI-modell lekeplass. [Modul 00](../00-quick-start/README.md)

**In-Memory Testing** - Testing med in-memory lagring.

**Integration Testing** - Testing med ekte infrastruktur.

**Maven** - Java byggautomatiseringsverktøy.

**Mockito** - Java mocking-rammeverk.

**Spring Boot** - Java applikasjonsrammeverk. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på det opprinnelige språket skal anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->