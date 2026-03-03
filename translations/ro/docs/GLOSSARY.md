# Glosar LangChain4j

## Cuprins

- [Concepte de bază](../../../docs)
- [Componente LangChain4j](../../../docs)
- [Concepte AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Ingineria Promptului](../../../docs)
- [RAG (Generare augmentată prin recuperare)](../../../docs)
- [Agenți și Unelte](../../../docs)
- [Modul Agentic](../../../docs)
- [Protocolul Contextului Modelului (MCP)](../../../docs)
- [Servicii Azure](../../../docs)
- [Testare și Dezvoltare](../../../docs)

Referință rapidă pentru termeni și concepte utilizate pe parcursul cursului.

## Concepte de bază

**Agent AI** - Sistem care folosește AI pentru raționament și acțiune autonomă. [Modul 04](../04-tools/README.md)

**Lanț** - Secvență de operațiuni unde ieșirea alimentează pasul următor.

**Fragmentare (Chunking)** - Împărțirea documentelor în bucăți mai mici. Tipic: 300-500 tokeni cu suprapunere. [Modul 03](../03-rag/README.md)

**Fereastra de Context** - Numărul maxim de tokeni pe care un model îl poate procesa. GPT-5.2: 400K tokeni (până la 272K input, 128K output).

**Embeddings** - Vectori numerici care reprezintă semnificația textului. [Modul 03](../03-rag/README.md)

**Apelare Funcții** - Modelul generează cereri structurate pentru a apela funcții externe. [Modul 04](../04-tools/README.md)

**Halucinație** - Când modelele generează informații incorecte dar plauzibile.

**Prompt** - Text de intrare pentru un model de limbaj. [Modul 02](../02-prompt-engineering/README.md)

**Căutare Semantică** - Căutare după sens folosind embeddings, nu cuvinte cheie. [Modul 03](../03-rag/README.md)

**Cu Stare vs Fără Stare** - Stateless: fără memorie. Stateful: păstrează istoricul conversației. [Modul 01](../01-introduction/README.md)

**Tokeni** - Unități de bază de text pe care modelele le procesează. Afectează costurile și limitele. [Modul 01](../01-introduction/README.md)

**Lanț de Unelte** - Execuția secvențială de unelte unde ieșirea informează următorul apel. [Modul 04](../04-tools/README.md)

## Componente LangChain4j

**AiServices** - Creează interfețe de servicii AI tip-safe.

**OpenAiOfficialChatModel** - Client unificat pentru modelele OpenAI și Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Creează embeddings folosind clientul oficial OpenAI (suportă OpenAI și Azure OpenAI).

**ChatModel** - Interfața principală pentru modelele de limbaj.

**ChatMemory** - Menține istoricul conversației.

**ContentRetriever** - Găsește bucăți relevante de documente pentru RAG.

**DocumentSplitter** - Împarte documentele în fragmente.

**EmbeddingModel** - Converteste textul în vectori numerici.

**EmbeddingStore** - Stochează și recuperează embeddings.

**MessageWindowChatMemory** - Menține o fereastră glisantă cu mesajele recente.

**PromptTemplate** - Creează prompturi reutilizabile cu spații `{{variable}}`.

**TextSegment** - Fragment de text cu metadate. Utilizat în RAG.

**ToolExecutionRequest** - Reprezintă o cerere de execuție a unei unelte.

**UserMessage / AiMessage / SystemMessage** - Tipuri de mesaje în conversație.

## Concepte AI/ML

**Învățare Few-Shot** - Oferirea de exemple în prompturi. [Modul 02](../02-prompt-engineering/README.md)

**Model Mare de Limbaj (LLM)** - Modele AI antrenate pe volume mari de text.

**Efort de Raționament** - Parametru GPT-5.2 care controlează adâncimea gândirii. [Modul 02](../02-prompt-engineering/README.md)

**Temperatură** - Controlează gradul de aleatoriu al ieșirii. Mică = determinist, mare = creativ.

**Bază de Date Vectorială** - Bază de date specializată pentru embeddings. [Modul 03](../03-rag/README.md)

**Învățare Zero-Shot** - Realizarea sarcinilor fără exemple. [Modul 02](../02-prompt-engineering/README.md)

## Guardrails - [Modul 00](../00-quick-start/README.md)

**Apărare în profunzime** - Abordare de securitate multi-strat combinând guardrails la nivel de aplicație cu filtrele de siguranță ale furnizorului.

**Blocare dură** - Furnizorul returnează eroare HTTP 400 pentru încălcări severe ale conținutului.

**InputGuardrail** - Interfață LangChain4j pentru validarea input-ului utilizatorului înainte de a ajunge la LLM. Economisește cost și latență blocând prompturi dăunătoare devreme.

**InputGuardrailResult** - Tip de retur pentru validarea guardrail: `success()` sau `fatal("motiv")`.

**OutputGuardrail** - Interfață pentru validarea răspunsurilor AI înainte de a le returna utilizatorilor.

**Filtre de Siguranță ale Furnizorului** - Filtre încorporate în furnizorii AI (ex. GitHub Models) care detectează încălcări la nivel de API.

**Refuz Moale** - Modelul refuză politicos să răspundă fără a genera o eroare.

## Ingineria Promptului - [Modul 02](../02-prompt-engineering/README.md)

**Lanț de Gândire** - Raționament pas cu pas pentru acuratețe sporită.

**Output Restricționat** - Impunerea unui format sau structură specifică.

**Entuziasm Ridicat** - Tipar GPT-5.2 pentru raționament detaliat.

**Entuziasm Scăzut** - Tipar GPT-5.2 pentru răspunsuri rapide.

**Conversație Multi-Turn** - Menținerea contextului peste schimburi multiple.

**Promptare Bazată pe Rol** - Stabilirea personajului modelului prin mesaje de sistem.

**Auto-Reflecție** - Modelul evaluează și îmbunătățește propria ieșire.

**Analiză Structurată** - Cadru fix de evaluare.

**Tipar de Execuție a Sarcinii** - Planificare → Executare → Rezumat.

## RAG (Generare augmentată prin recuperare) - [Modul 03](../03-rag/README.md)

**Pipeline de procesare documente** - Încărcare → fragmentare → embedding → stocare.

**Depozit Embedding în Memorie** - Stocare nepermanentă pentru testare.

**RAG** - Combină recuperarea cu generarea pentru a fundamenta răspunsuri.

**Scor de Similaritate** - Măsură (0-1) a similarității semantice.

**Referință Surse** - Metadate despre conținutul recuperat.

## Agenți și Unelte - [Modul 04](../04-tools/README.md)

**Anotare @Tool** - Marchează metode Java ca unelte apelabile prin AI.

**Tipar ReAct** - Raționează → Acționează → Observează → Repetă.

**Managementul Sesiunii** - Contexte separate pentru utilizatori diferiți.

**Unealtă** - Funcție pe care un agent AI o poate apela.

**Descriere Unealtă** - Documentație despre scopul și parametrii uneltei.

## Modul Agentic - [Modul 05](../05-mcp/README.md)

**Anotare @Agent** - Marchează interfețele ca agenți AI cu definiție declarativă de comportament.

**Agent Listener** - Hook pentru monitorizarea execuției agentului prin `beforeAgentInvocation()` și `afterAgentInvocation()`.

**Agentic Scope** - Memorie partajată unde agenții stochează ieșirile folosind `outputKey` pentru a fi consumate de agenți ulteriori.

**AgenticServices** - Fabrica pentru crearea de agenți folosind `agentBuilder()` și `supervisorBuilder()`.

**Flux Condițional** - Rutează pe baza condițiilor către agenți specialiști diferiți.

**Human-in-the-Loop** - Tipar de flux de lucru care adaugă verificări umane pentru aprobare sau revizuirea conținutului.

**langchain4j-agentic** - Dependență Maven pentru construire declarativă de agenți (experimental).

**Flux Loop** - Iterează execuția agentului până când o condiție este îndeplinită (de ex. scor calitate ≥ 0.8).

**outputKey** - Parametru de anotare agent care specifică unde sunt stocate rezultatele în Agentic Scope.

**Flux Paralel** - Rulează mai mulți agenți simultan pentru sarcini independente.

**Strategia de Răspuns** - Cum formulează supraveghetorul răspunsul final: LAST, SUMMARY sau SCORED.

**Flux Secvențial** - Execută agenți în ordine unde ieșirea curge către pasul următor.

**Tipar Agent Supraveghetor** - Tipar avansat agentic în care un LLM supraveghetor decide dinamic ce subagenți să invoce.

## Protocolul Contextului Modelului (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependență Maven pentru integrarea MCP în LangChain4j.

**MCP** - Protocolul Contextului Modelului: standard pentru conectarea aplicațiilor AI la unelte externe. Construiește o dată, folosește oriunde.

**Client MCP** - Aplicație care se conectează la serverele MCP pentru a descoperi și folosi unelte.

**Server MCP** - Serviciu care expune unelte prin MCP cu descrieri clare și scheme de parametri.

**McpToolProvider** - Componentă LangChain4j ce învelește uneltele MCP pentru folosire în servicii AI și agenți.

**McpTransport** - Interfață pentru comunicare MCP. Implementări includ Stdio și HTTP.

**Transport Stdio** - Transport local prin stdin/stdout. Util pentru acces la sistem de fișiere sau unelte în linia de comandă.

**StdioMcpTransport** - Implementare LangChain4j care lansează server MCP ca subproces.

**Descoperire Unelte** - Clientul interoghează serverul pentru unelte disponibile cu descrieri și scheme.

## Servicii Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Căutare cloud cu capabilități vectoriale. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deplasează resurse Azure.

**Azure OpenAI** - Serviciul AI enterprise al Microsoft.

**Bicep** - Limbaj Azure pentru infrastructură ca cod. [Ghid Infrastructură](../01-introduction/infra/README.md)

**Nume Deplasărilor** - Numele pentru deployment model în Azure.

**GPT-5.2** - Cel mai recent model OpenAI cu control al raționamentului. [Modul 02](../02-prompt-engineering/README.md)

## Testare și Dezvoltare - [Ghid Testare](TESTING.md)

**Dev Container** - Mediu de dezvoltare containerizat. [Configurație](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuit pentru modele AI. [Modul 00](../00-quick-start/README.md)

**Testare în Memorie** - Testare cu stocare în memorie.

**Testare de Integrare** - Testare cu infrastructură reală.

**Maven** - Instrument Java de automatizare a construirii.

**Mockito** - Framework Java pentru mocking.

**Spring Boot** - Framework pentru aplicații Java. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare de responsabilitate**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite cauzate de utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->