# Glossar LangChain4j

## Cuprins

- [Concepte de bază](../../../docs)
- [Componente LangChain4j](../../../docs)
- [Concepte AI/ML](../../../docs)
- [Garduri de protecție](../../../docs)
- [Ingineria prompturilor](../../../docs)
- [RAG (Generare augmentată prin recuperare)](../../../docs)
- [Agenți și unelte](../../../docs)
- [Modul Agentic](../../../docs)
- [Protocolul Contextului Modelului (MCP)](../../../docs)
- [Servicii Azure](../../../docs)
- [Testare și Dezvoltare](../../../docs)

Referință rapidă pentru termeni și concepte folosite pe parcursul cursului.

## Concepte de bază

**Agent AI** - Sistem care folosește AI pentru a raționa și acționa autonom. [Modul 04](../04-tools/README.md)

**Lanț** - Secvență de operații unde output-ul alimentează următorul pas.

**Fragmentare** - Împărțirea documentelor în bucăți mai mici. Tipic: 300-500 tokeni cu suprapunere. [Modul 03](../03-rag/README.md)

**Fereastra de context** - Numărul maxim de tokeni pe care un model îi poate procesa. GPT-5.2: 400K tokeni.

**Embeddings** - Vectori numerici care reprezintă sensul textului. [Modul 03](../03-rag/README.md)

**Apelarea funcțiilor** - Modelul generează cereri structurate pentru a apela funcții externe. [Modul 04](../04-tools/README.md)

**Halucinație** - Atunci când modelele generează informații incorecte dar plauzibile.

**Prompt** - Textul input pentru un model lingvistic. [Modul 02](../02-prompt-engineering/README.md)

**Căutare semantică** - Căutare bazată pe sens folosind embeddings, nu cuvinte-cheie. [Modul 03](../03-rag/README.md)

**Stare cu memorie vs fără memorie** - Stateless: fără memorie. Stateful: menține istoricul conversației. [Modul 01](../01-introduction/README.md)

**Tokeni** - Unitățile de bază de text pe care modelele le procesează. Afectează costurile și limitele. [Modul 01](../01-introduction/README.md)

**Lanțare de unelte** - Execuția secvențială a uneltelor unde outputul informează următorul apel. [Modul 04](../04-tools/README.md)

## Componente LangChain4j

**AiServices** - Creează interfețe sigure din punct de vedere al tipurilor pentru servicii AI.

**OpenAiOfficialChatModel** - Client unificat pentru modelele OpenAI și Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Creează embeddings folosind clientul oficial OpenAI (suportă atât OpenAI cât și Azure OpenAI).

**ChatModel** - Interfața principală pentru modele lingvistice.

**ChatMemory** - Menține istoricul conversației.

**ContentRetriever** - Găsește fragmente de document relevante pentru RAG.

**DocumentSplitter** - Împarte documentele în bucăți.

**EmbeddingModel** - Convertește textul în vectori numerici.

**EmbeddingStore** - Stochează și recuperează embeddings.

**MessageWindowChatMemory** - Menține o fereastră derulantă a mesajelor recente.

**PromptTemplate** - Creează prompturi reutilizabile cu variabile `{{variable}}`.

**TextSegment** - Fragment de text cu metadate. Folosit în RAG.

**ToolExecutionRequest** - Reprezintă o cerere de execuție a unei unelte.

**UserMessage / AiMessage / SystemMessage** - Tipuri de mesaje din conversație.

## Concepte AI/ML

**Învățare Few-Shot** - Furnizarea de exemple în prompturi. [Modul 02](../02-prompt-engineering/README.md)

**Model lingvistic mare (LLM)** - Modele AI antrenate pe volume vaste de text.

**Efort de raționament** - Parametru GPT-5.2 care controlează adâncimea gândirii. [Modul 02](../02-prompt-engineering/README.md)

**Temperatură** - Controlează aleatorietatea outputului. Mic=determinist, mare=creativ.

**Bază de date vectorială** - Bază de date specializată pentru embeddings. [Modul 03](../03-rag/README.md)

**Învățare Zero-Shot** - Realizarea sarcinilor fără exemple. [Modul 02](../02-prompt-engineering/README.md)

## Garduri de protecție - [Modul 00](../00-quick-start/README.md)

**Apărare în profunzime** - Abordare de securitate multi-strat combinând garduri de protecție la nivel de aplicație cu filtre de siguranță ale furnizorului.

**Blocare dură** - Furnizorul returnează eroare HTTP 400 pentru încălcări grave de conținut.

**InputGuardrail** - Interfață LangChain4j pentru validarea inputului utilizatorului înainte ca acesta să ajungă la LLM. Economisește cost și latență blocând prompturile dăunătoare din timp.

**InputGuardrailResult** - Tipul returnat de validarea gardului: `success()` sau `fatal("motiv")`.

**OutputGuardrail** - Interfață pentru validarea răspunsurilor AI înainte de returnarea către utilizatori.

**Filtre de siguranță ale furnizorului** - Filtre de conținut încorporate de furnizorii AI (ex. GitHub Models) care detectează încălcări la nivelul API.

**Refuz blând** - Modelul refuză politicos să răspundă fără a genera eroare.

## Ingineria prompturilor - [Modul 02](../02-prompt-engineering/README.md)

**Lanț de gândire** - Raționament pas cu pas pentru mai multă acuratețe.

**Output constrâns** - Impunerea unui format sau structură specifică.

**Energia ridicată** - Tipar GPT-5.2 pentru raționament detaliat.

**Energia scăzută** - Tipar GPT-5.2 pentru răspunsuri rapide.

**Conversație multi-tur** - Menținerea contextului pe parcursul schimburilor.

**Prompting bazat pe rol** - Setarea personajului modelului prin mesaje de sistem.

**Auto-reflecție** - Modelul evaluează și îmbunătățește outputul.

**Analiză structurată** - Cadru fixat pentru evaluare.

**Tipar de execuție a sarcinii** - Planificare → Executare → Rezumat.

## RAG (Generare augmentată prin recuperare) - [Modul 03](../03-rag/README.md)

**Linia de procesare a documentelor** - Încărcare → fragmentare → embedding → stocare.

**Stocare embedding în memorie** - Stocare non-persistentă pentru testare.

**RAG** - Combină recuperarea cu generarea pentru a fundamenta răspunsurile.

**Scor de similaritate** - Măsură (0-1) a similarității semantice.

**Referință sursă** - Metadate despre conținutul recuperat.

## Agenți și unelte - [Modul 04](../04-tools/README.md)

**Anotare @Tool** - Marchează metode Java ca unelte apelabile de AI.

**Tipar ReAct** - Raționează → Acționează → Observă → Repetă.

**Gestionarea sesiunilor** - Context separat pentru utilizatori diferiți.

**Unealtă** - Funcție pe care un agent AI o poate apela.

**Descrierea uneltei** - Documentația scopului uneltei și a parametrilor.

## Modul Agentic - [Modul 05](../05-mcp/README.md)

**Anotare @Agent** - Marchează interfețe ca agenți AI cu definire declarativă a comportamentului.

**Agent Listener** - Punct de legătură pentru monitorizarea execuției agentului prin `beforeAgentInvocation()` și `afterAgentInvocation()`.

**Domeniul agentic** - Memorie partajată unde agenții stochează outputuri folosind `outputKey` pentru a fi consumate de agenți ulteriori.

**AgenticServices** - Fabrica pentru crearea agenților folosind `agentBuilder()` și `supervisorBuilder()`.

**Flux condiționat** - Rutare bazată pe condiții către agenți specializați diferiți.

**Human-in-the-Loop** - Tipar workflow care adaugă puncte de control umane pentru aprobare sau revizuire a conținutului.

**langchain4j-agentic** - Dependență Maven pentru construire declarativă de agenți (experimental).

**Flux repetitiv** - Iterează execuția agentului până la îndeplinirea unei condiții (ex: scor calitate ≥ 0.8).

**outputKey** - Parametru al anotării agent care specifică unde sunt stocate rezultatele în domeniul agentic.

**Flux paralel** - Rulează mai mulți agenți simultan pentru sarcini independente.

**Strategie de răspuns** - Cum formulează supervizorul răspunsul final: LAST, SUMMARY sau SCORED.

**Flux secvențial** - Execută agenți în ordine astfel încât outputul să curgă către pasul următor.

**Tipar agent supervizor** - Tipar agentic avansat în care un LLM supervizor decide dinamic care sub-agenti să fie invocați.

## Protocolul Contextului Modelului (MCP) - [Modul 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependență Maven pentru integrarea MCP în LangChain4j.

**MCP** - Protocolul Contextului Modelului: standard pentru conectarea aplicațiilor AI la unelte externe. Se construiește o dată, se folosește peste tot.

**Client MCP** - Aplicație care se conectează la serverele MCP pentru a descoperi și utiliza unelte.

**Server MCP** - Serviciu care expune unelte prin MCP cu descrieri clare și scheme de parametri.

**McpToolProvider** - Componentă LangChain4j care înfășoară uneltele MCP pentru utilizare în servicii AI și agenți.

**McpTransport** - Interfață pentru comunicare MCP. Implementări includ Stdio și HTTP.

**Transport Stdio** - Transport local prin stdin/stdout. Util pentru acces la sistem fișiere sau unelte linie de comandă.

**StdioMcpTransport** - Implementare LangChain4j care inițiază server MCP ca subprocess.

**Descoperirea uneltelor** - Clientul interoghează serverul pentru uneltele disponibile cu descrieri și scheme.

## Servicii Azure - [Modul 01](../01-introduction/README.md)

**Azure AI Search** - Căutare în cloud cu capabilități vectoriale. [Modul 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Deplasează resurse Azure.

**Azure OpenAI** - Serviciu AI enterprise Microsoft.

**Bicep** - Limbaj Azure pentru infrastructură ca cod. [Ghid Infrastructură](../01-introduction/infra/README.md)

**Nume implementare** - Numele pentru implementarea modelului în Azure.

**GPT-5.2** - Cel mai recent model OpenAI cu control al raționamentului. [Modul 02](../02-prompt-engineering/README.md)

## Testare și Dezvoltare - [Ghid Testare](TESTING.md)

**Container Dev** - Mediu de dezvoltare containerizat. [Configurație](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Teren de joacă AI gratuit. [Modul 00](../00-quick-start/README.md)

**Testare în memorie** - Testare cu stocare în memorie.

**Testare de integrare** - Testare cu infrastructură reală.

**Maven** - Unealtă de automatizare a construirii Java.

**Mockito** - Framework Java pentru simulare.

**Spring Boot** - Framework aplicații Java. [Modul 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinarea răspunderii**:  
Acest document a fost tradus utilizând serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim pentru acuratețe, vă rugăm să țineți cont că traducerile automate pot conține erori sau inexactități. Documentul original, în limba sa nativă, trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru orice neînțelegeri sau interpretări greșite ce pot rezulta din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->